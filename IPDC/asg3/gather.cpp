#include <iostream>
#include <mpi.h>
#include <cstdlib>
#include <cmath>
#include <string.h>

using namespace std;

int rank, size;

int MPI_Mygather(MPICH2_CONST void *sendbuf, int sendcnt, MPI_Datatype sendtype,
                      void *recvbuf, int recvcnt, MPI_Datatype recvtype,
                      int root, MPI_Comm comm)

{
	MPI_Status status;
	int * send_buf = (int *) sendbuf;
	int * recv_buf = (int *) recvbuf;
	int i = 1, n = log2(size), send_cnt = sendcnt;
	float tmp = log2(size);
	tmp > n ? n++ : tmp = n;
	if(rank % 2 == 0 && (rank + 1) < size)	
		MPI_Send(send_buf, sendcnt, MPI_INT, rank + 1, 0 , comm);
	if(rank % 2 == 1 && (rank - 1) >= 0)
	{
		for(int j=0; j<sendcnt; j++)	recv_buf[sendcnt * rank + j] = send_buf[j];		
		MPI_Recv(&recv_buf[(rank - 1) * sendcnt], recvcnt, MPI_INT, rank - 1, 0, comm, &status);
	}	
	send_cnt *= 2;
	if(rank % 2 == 1)
	{
		while(i<n)
		{
			if((rank + pow(2.0, i)) < size && (rank - pow(2.0, i-1) >= 0))
				MPI_Send(&recv_buf[(int)(rank - pow(2.0, i-1)) * sendcnt], send_cnt, MPI_INT, rank + pow(2.0, i), 0 , comm);
			if(rank - pow(2.0, i) >= 0 && (((rank - pow(2.0, i)) * sendcnt) - send_cnt / 2) >= 0)
			{
				for(int j=0; j<sendcnt; j++)	recv_buf[sendcnt * rank + j] = send_buf[j];
				MPI_Recv(&recv_buf[(int)((rank - pow(2.0, i)) * sendcnt) - send_cnt / 2], send_cnt,  MPI_INT, rank - pow(2.0, i), 0 , comm, &status);				
			}			
			send_cnt *= 2;			
			i++;
		}
	}
	if(size % 2 == 1 && rank == size - 2)
		MPI_Send(recv_buf, sendcnt * size, MPI_INT, size - 1, 0, comm);
	if(size % 2 == 1 && rank == size -1 )
	{
		MPI_Recv(recv_buf, sendcnt * size, MPI_INT, size - 2, 0, comm, &status);
		for(int j=0; j<sendcnt; j++)	recv_buf[sendcnt * rank + j] = send_buf[j];
	}
	if(root != size - 1 && rank == size - 1)
		MPI_Send(recv_buf, sendcnt * size, MPI_INT, root, 0, comm);
	if(root != size -1 && rank == root)
		MPI_Recv(recv_buf, sendcnt * size, MPI_INT, size - 1, 0 , comm, &status);	
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
	int recv_buf[send_count * size], recv_count = send_count;
	int send_buf[send_count];	
	// populating bufferd
	for(int i=0; i<send_count; i++)	send_buf[i] = send_count * rank + i;
	for(int i=0; i<send_count * size; i++)	recv_buf[i] = -1+i+1;	
	MPI_Mygather(send_buf, send_count, MPI_INT, recv_buf, recv_count, MPI_INT, root, MPI_COMM_WORLD);
	if(rank == root)
	{
	cout << "########################################################################################################################\n\n";
	cout << "My Rank = " << rank << endl;
	cout << "My Receiving buffer conatins following values ";
	for(int i=0; i<recv_count * size; i++)		cout << recv_buf[i] << ",";
	cout << "\n\n########################################################################################################################\n\n";
	}
	MPI_Finalize();
	return 0;
}
