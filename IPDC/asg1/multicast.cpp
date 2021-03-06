#include <iostream>
#include <mpi.h>
#include <cstdlib>
#include <cmath>
#include <string.h>

using namespace std;

int rank, size;

int MPI_Mcast( void *buffer, int count, MPI_Datatype datatype, int root, int *totalProcesses,
			int num, MPI_Comm comm )
{
	int i=1, n = log2(num);
	float tmp = log2(num);
	int *msg = (int *) buffer;
	MPI_Status status;
	tmp > n ? n++ : tmp = n;
	if(root == rank)	MPI_Send(msg, count, datatype, totalProcesses[0], 0, comm);
	if(rank == totalProcesses[0])	MPI_Recv(msg, count, datatype, root, 0, comm, &status);
	while(i < num)
	{
		//tmp = -1;
		//int numReceive = 0;
		for(int j=0; j<num; j++)
		{
			if(rank == j && j+i < num)
			{
				//cout << j << "--->" << (j + pow(2,i)) << endl;
				//tmp = totalProcesses[(int)(j + pow(2,i))];
				MPI_Send(buffer, count, datatype, totalProcesses[j+i], 0, comm);
			}
			else if(rank == j+i && j+i < num)
			{
				//cout << j << "<---" << (j - pow(2,i)) << endl;
				MPI_Recv(buffer, count, datatype, totalProcesses[j], 0, comm, &status);
			}
					
		}
		i*=2;	
	}	
}

int main (int argc, char * argv[])
{
	system("clear");
	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &size);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	int root = atoi(argv[1]), num = atoi(argv[3]), msg = -32767;
	int totalProcesses[num];
	if(argc != 4 + num)
	{
		cout << "\nInvalid number of aurguments!\n\n";
		return 1;
	} 
	for(int i=0, j=4; i<num; i++, j++)
	{
		if(atoi(argv[j]) < size && atoi(argv[j]) >= 0 && atoi(argv[j]) != root)
			totalProcesses[i] = atoi(argv[j]);
		else
		{	
			cout << "\nInvalid multicast receiver!\n\n";
			return 1;
		}
	}
	if(root >= size || root < 0)
	{
		cout << "\nInvalid root!\n";
		return 1;
	}
	if(num > size || num < 2)
	{
		cout << "\nInvalid number of receivers!\n";
		return 1;
	}
	
	if(rank == root)
	{
		msg = atoi(argv[2]);		
	}
	MPI_Mcast(&msg, 1, MPI_INT, root, totalProcesses, num, MPI_COMM_WORLD);
	//MPI_Bcast(&msg, 1, MPI_INT, root, MPI_COMM_WORLD);
	cout << msg << "\n";
	MPI_Finalize();
	return 0;
}
