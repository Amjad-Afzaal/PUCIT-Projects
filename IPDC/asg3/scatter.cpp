#include <iostream>
#include <mpi.h>
#include <cstdlib>
#include <cmath>
#include <string.h>

using namespace std;

int rank, size;

int MPI_Myscatter(MPICH2_CONST void *sendbuf, int sendcnt, MPI_Datatype sendtype, void *recvbuf, 
			int recvcnt, MPI_Datatype recvtype, int root, MPI_Comm comm)
{
	MPI_Status status;
	MPI_Bcast(sendbuf, sendcnt*size, MPI_INT, root, comm);	
	int * send_buf = (int *) sendbuf;
	int *recv_buf = (int *) recvbuf;
	for(int i=0; i<recvcnt; i++)
	{
		recv_buf[i] = send_buf[sendcnt*rank + i];
	}	
}

int main (int argc, char * argv[])
{
	system("clear");
	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &size);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	if(argc != 3)
	{
		cout << "Invalid number of aurgument, Required 2!";
		return 1;
	}	
	int root = atoi(argv[1]), send_count = atoi(argv[2]);
	int recv_buf[send_count], recv_count = send_count;
	int send_buf[send_count * size];
	if(rank == root)
	{
		// populating bufferd
		for(int i=0; i<send_count * size; i++)	send_buf[i] = i;
	}
	MPI_Myscatter(send_buf, send_count, MPI_INT, recv_buf, recv_count, MPI_INT, root, MPI_COMM_WORLD);
	cout << "########################################################################################################################\n\n";
	cout << "My Rank = " << rank << endl;
	cout << "My Receiving buffer conatins following values ";
	for(int i=0; i<recv_count; i++)		cout << recv_buf[i] << ",";
	cout << "########################################################################################################################\n\n";
	MPI_Finalize();
	return 0;
}
