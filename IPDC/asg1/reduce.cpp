#include <iostream>
#include <mpi.h>
#include <cstdlib>
#include <cmath>
#include <string.h>

using namespace std;

int rank, size;

void display(int * recvbuf, int n, bool isMine)
{
	if(isMine)
	{
		cout << "\n\n######################################## My Result #################################################\n\n";
		cout << "\n\n Result = ";
		for(int i=0; i<n; i++)	cout << recvbuf[i] << ", ";
		cout << "\n\n####################################################################################################\n\n";	
	}
	else
	{
		cout << "\n\n######################################## MPI_Reduce Result #########################################\n\n";
		cout << "\n\n Result = ";
		for(int i=0; i<n; i++)	cout << recvbuf[i] << ", ";
		cout << "\n\n####################################################################################################\n\n";	
	}
}

int MPI_Myreduce(const void *sendbuf, void *recvbuf, int count, MPI_Datatype datatype,
                      MPI_Op op, int root, MPI_Comm comm)
{
	if(count == 0) return 0;
	MPI_Status status;
	int * send = (int *) sendbuf;
	int * recv = (int *) recvbuf;
	int i=0, n = log2(size);
	float tmp = log2(size);
	tmp > n ? n++ : tmp = n;
	//cout << "\n\nlog2(size) = " << log2(size) << ", n = " << n << ", tmp = " << tmp << "\n\n";
	switch(op)
	{
		case MPI_SUM:
			if(rank % 2 == 0 && (rank + 1) < size)
				MPI_Send(send, count, datatype, (rank + 1), 0, comm);
			if(rank % 2 == 1 && (rank - 1) >= 0)
			{
				MPI_Recv(recv, count, datatype, (rank - 1), 0, comm, &status);
				for(int j=0; j<count; j++)
				{
					recv[j] += send[j];
					send[j] = recv[j];
				}
			}
			i++;
			while(i < n)
			{
				if(rank % 2 == 1 && (rank + pow(2, i)) < size)
					MPI_Send(send, count, datatype, (rank + pow(2, i)), 0, comm);
				if(rank % 2 == 1 && (rank - pow(2, i)) >= 0)
				{
					MPI_Recv(recv, count, datatype, (rank - pow(2, i)), 0, comm, &status);
					for(int j=0; j<count; j++)
					{
						recv[j] += send[j];
						send[j] = recv[j];
					}
				}
				i++;	
			}
			if(size % 2 == 1 && rank == (size - 2))
			{
				MPI_Send(send, count, datatype, size - 1, 0, comm);
			}
			if(size % 2 == 1 && rank == (size - 1))
			{
				MPI_Recv(recv, count, datatype, size - 2, 0, comm, &status);
				for(int j=0; j<count; j++)
				{
					recv[j] += send[j];
					send[j] = recv[j];
				}
			}
			if(rank == (size - 1) && (size - 1) != root)		MPI_Send(recv, count, datatype, root, 0, comm);
			if(rank == root && (size - 1) != root)			MPI_Recv(recv, count, datatype, (size - 1), 0, comm, &status);
		return 0;

//###########################################################################################################################################//

		case MPI_PROD:
			if(rank % 2 == 0 && (rank + 1) < size)
				MPI_Send(send, count, datatype, (rank + 1), 0, comm);
			if(rank % 2 == 1 && (rank - 1) >= 0)
			{
				MPI_Recv(recv, count, datatype, (rank - 1), 0, comm, &status);
				for(int j=0; j<count; j++)
				{
					recv[j] *= send[j];
					send[j] = recv[j];
				}
			}
			i++;
			while(i < n)
			{
				if(rank % 2 == 1 && (rank + pow(2, i)) < size)
					MPI_Send(send, count, datatype, (rank + pow(2, i)), 0, comm);
				if(rank % 2 == 1 && (rank - pow(2, i)) >= 0)
				{
					MPI_Recv(recv, count, datatype, (rank - pow(2, i)), 0, comm, &status);
					for(int j=0; j<count; j++)
					{
						recv[j] *= send[j];
						send[j] = recv[j];
					}
				}
				i++;	
			}
			if(size % 2 == 1 && rank == (size - 2))
			{
				MPI_Send(send, count, datatype, size - 1, 0, comm);
			}
			if(size % 2 == 1 && rank == (size - 1))
			{
				MPI_Recv(recv, count, datatype, size - 2, 0, comm, &status);
				for(int j=0; j<count; j++)
				{
					recv[j] *= send[j];
					send[j] = recv[j];
				}
			}
			if(rank == (size - 1) && (size - 1) != root)		MPI_Send(recv, count, datatype, root, 0, comm);
			if(rank == root && (size - 1) != root)			MPI_Recv(recv, count, datatype, (size - 1), 0, comm, &status);		return 0;

//###########################################################################################################################################//

		case MPI_BAND:
			if(rank % 2 == 0 && (rank + 1) < size)
				MPI_Send(send, count, datatype, (rank + 1), 0, comm);
			if(rank % 2 == 1 && (rank - 1) >= 0)
			{
				MPI_Recv(recv, count, datatype, (rank - 1), 0, comm, &status);
				for(int j=0; j<count; j++)
				{
					recv[j] &= send[j];
					send[j] = recv[j];
				}
			}
			i++;
			while(i < n)
			{
				if(rank % 2 == 1 && (rank + pow(2, i)) < size)
					MPI_Send(send, count, datatype, (rank + pow(2, i)), 0, comm);
				if(rank % 2 == 1 && (rank - pow(2, i)) >= 0)
				{
					MPI_Recv(recv, count, datatype, (rank - pow(2, i)), 0, comm, &status);
					for(int j=0; j<count; j++)
					{
						recv[j] &= send[j];
						send[j] = recv[j];
					}
				}
				i++;	
			}
			if(size % 2 == 1 && rank == (size - 2))
			{
				MPI_Send(send, count, datatype, size - 1, 0, comm);
			}
			if(size % 2 == 1 && rank == (size - 1))
			{
				MPI_Recv(recv, count, datatype, size - 2, 0, comm, &status);
				for(int j=0; j<count; j++)
				{
					recv[j] &= send[j];
					send[j] = recv[j];
				}
			}
			if(rank == (size - 1) && (size - 1) != root)		MPI_Send(recv, count, datatype, root, 0, comm);
			if(rank == root && (size - 1) != root)			MPI_Recv(recv, count, datatype, (size - 1), 0, comm, &status);		return 0;

//###########################################################################################################################################//

		case MPI_BOR:
			if(rank % 2 == 0 && (rank + 1) < size)
				MPI_Send(send, count, datatype, (rank + 1), 0, comm);
			if(rank % 2 == 1 && (rank - 1) >= 0)
			{
				MPI_Recv(recv, count, datatype, (rank - 1), 0, comm, &status);
				for(int j=0; j<count; j++)
				{
					recv[j] |= send[j];
					send[j] = recv[j];
				}
			}
			i++;
			while(i < n)
			{
				if(rank % 2 == 1 && (rank + pow(2, i)) < size)
					MPI_Send(send, count, datatype, (rank + pow(2, i)), 0, comm);
				if(rank % 2 == 1 && (rank - pow(2, i)) >= 0)
				{
					MPI_Recv(recv, count, datatype, (rank - pow(2, i)), 0, comm, &status);
					for(int j=0; j<count; j++)
					{
						recv[j] |= send[j];
						send[j] = recv[j];
					}
				}
				i++;	
			}
			if(size % 2 == 1 && rank == (size - 2))
			{
				MPI_Send(send, count, datatype, size - 1, 0, comm);
			}
			if(size % 2 == 1 && rank == (size - 1))
			{
				MPI_Recv(recv, count, datatype, size - 2, 0, comm, &status);
				for(int j=0; j<count; j++)
				{
					recv[j] |= send[j];
					send[j] = recv[j];
				}
			}
			if(rank == (size - 1) && (size - 1) != root)		MPI_Send(recv, count, datatype, root, 0, comm);
			if(rank == root && (size - 1) != root)			MPI_Recv(recv, count, datatype, (size - 1), 0, comm, &status);		return 0;

//###########################################################################################################################################//

		case MPI_BXOR:
			if(rank % 2 == 0 && (rank + 1) < size)
				MPI_Send(send, count, datatype, (rank + 1), 0, comm);
			if(rank % 2 == 1 && (rank - 1) >= 0)
			{
				MPI_Recv(recv, count, datatype, (rank - 1), 0, comm, &status);
				for(int j=0; j<count; j++)
				{
					recv[j] ^= send[j];
					send[j] = recv[j];
				}
			}
			i++;
			while(i < n)
			{
				if(rank % 2 == 1 && (rank + pow(2, i)) < size)
					MPI_Send(send, count, datatype, (rank + pow(2, i)), 0, comm);
				if(rank % 2 == 1 && (rank - pow(2, i)) >= 0)
				{
					MPI_Recv(recv, count, datatype, (rank - pow(2, i)), 0, comm, &status);
					for(int j=0; j<count; j++)
					{
						recv[j] ^= send[j];
						send[j] = recv[j];
					}
				}
				i++;	
			}
			if(size % 2 == 1 && rank == (size - 2))
			{
				MPI_Send(send, count, datatype, size - 1, 0, comm);
			}
			if(size % 2 == 1 && rank == (size - 1))
			{
				MPI_Recv(recv, count, datatype, size - 2, 0, comm, &status);
				for(int j=0; j<count; j++)
				{
					recv[j] ^= send[j];
					send[j] = recv[j];
				}
			}
			if(rank == (size - 1) && (size - 1) != root)		MPI_Send(recv, count, datatype, root, 0, comm);
			if(rank == root && (size - 1) != root)			MPI_Recv(recv, count, datatype, (size - 1), 0, comm, &status);		return 0;

//###########################################################################################################################################//

		case MPI_MAX:
			if(rank % 2 == 0 && (rank + 1) < size)
				MPI_Send(send, count, datatype, (rank + 1), 0, comm);
			if(rank % 2 == 1 && (rank - 1) >= 0)
			{
				MPI_Recv(recv, count, datatype, (rank - 1), 0, comm, &status);
				for(int j=0; j<count; j++)
				{
					recv[j] >= send[j] ? send[j] = recv[j] : recv[j] = send[j];
				}
			}
			i++;
			while(i < n)
			{
				if(rank % 2 == 1 && (rank + pow(2, i)) < size)
					MPI_Send(send, count, datatype, (rank + pow(2, i)), 0, comm);
				if(rank % 2 == 1 && (rank - pow(2, i)) >= 0)
				{
					MPI_Recv(recv, count, datatype, (rank - pow(2, i)), 0, comm, &status);
					for(int j=0; j<count; j++)
					{
						recv[j] >= send[j] ? send[j] = recv[j] : recv[j] = send[j];
					}
				}
				i++;	
			}
			if(size % 2 == 1 && rank == (size - 2))
			{
				MPI_Send(send, count, datatype, size - 1, 0, comm);
			}
			if(size % 2 == 1 && rank == (size - 1))
			{
				MPI_Recv(recv, count, datatype, size - 2, 0, comm, &status);
				for(int j=0; j<count; j++)
				{
					recv[j] >= send[j] ? send[j] = recv[j] : recv[j] = send[j];
				}
			}
			if(rank == (size - 1) && (size - 1) != root)		MPI_Send(recv, count, datatype, root, 0, comm);
			if(rank == root && (size - 1) != root)			MPI_Recv(recv, count, datatype, (size - 1), 0, comm, &status);		return 0;

//###########################################################################################################################################//

		case MPI_MIN:
			if(rank % 2 == 0 && (rank + 1) < size)
				MPI_Send(send, count, datatype, (rank + 1), 0, comm);
			if(rank % 2 == 1 && (rank - 1) >= 0)
			{
				MPI_Recv(recv, count, datatype, (rank - 1), 0, comm, &status);
				for(int j=0; j<count; j++)
				{
					recv[j] <= send[j] ? send[j] = recv[j] : recv[j] = send[j];
				}
			}
			i++;
			while(i < n)
			{
				if(rank % 2 == 1 && (rank + pow(2, i)) < size)
					MPI_Send(send, count, datatype, (rank + pow(2, i)), 0, comm);
				if(rank % 2 == 1 && (rank - pow(2, i)) >= 0)
				{
					MPI_Recv(recv, count, datatype, (rank - pow(2, i)), 0, comm, &status);
					for(int j=0; j<count; j++)
					{
						recv[j] <= send[j] ? send[j] = recv[j] : recv[j] = send[j];
					}
				}
				i++;	
			}
			if(size % 2 == 1 && rank == (size - 2))
			{
				MPI_Send(send, count, datatype, size - 1, 0, comm);
			}
			if(size % 2 == 1 && rank == (size - 1))
			{
				MPI_Recv(recv, count, datatype, size - 2, 0, comm, &status);
				for(int j=0; j<count; j++)
				{
					recv[j] <= send[j] ? send[j] = recv[j] : recv[j] = send[j];
				}
			}
			if(rank == (size - 1) && (size - 1) != root)		MPI_Send(recv, count, datatype, root, 0, comm);
			if(rank == root && (size - 1) != root)			MPI_Recv(recv, count, datatype, (size - 1), 0, comm, &status);
		return 0;
	}
}



//===========================================================================================================================================//
//===========================================================================================================================================//
//===========================================================================================================================================//


int main (int argc, char * argv[])
{
	system("clear");
	if(argc != 4)
	{
		cout << "\nInvalid number of aurguments!, required 4\n";
		return 1;
	}
	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &size);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	int n = atoi(argv[3]);
	if(n <=0 )	
	{
		cout << "\n\nPlease Enter valid number of elements i.e. >= 1\n\n";
		return 1;
	}
	if(size == 1)
	{
		cout << "\n\nWarning this will create the deadlock! Program terminating\n\n";
		return 1;
	}
	int sendbuf[n], recvbuf[n], tmpsend[n], tmprecv[n], root = atoi(argv[1]);
	for(int i=0; i<n; i++)	
	{
		sendbuf[i] = rand() % (rank + 1) + size;
		tmpsend[i] = sendbuf[i];
	}
	if(root >= size || root < 0)
	{
		cout << "\nInvalid root! it should be less than total process or greater than -1 !\n";
		return 1;
	}
	//if(rank == root)
	{	
		cout << "\nRank = Values\n\n";
		cout << rank << " = ";
		for(int i=0; i<n; i++)	cout << sendbuf[i] << ", ";
	}
	
//###########################################################################################################################################//
	if(strcmp(argv[2], "+") == 0)
	{
		MPI_Myreduce(&sendbuf, &recvbuf, n, MPI_INT, MPI_SUM, root, MPI_COMM_WORLD);
		if(rank == root)	display(recvbuf, n, true);
		MPI_Reduce(&tmpsend, &tmprecv, n, MPI_INT, MPI_SUM, root, MPI_COMM_WORLD);
		if(rank == root)	display(tmprecv, n, false);
	}
//###########################################################################################################################################//
	else if(strcmp(argv[2], "MUL") == 0)
	{
		MPI_Myreduce(&sendbuf, &recvbuf, n, MPI_INT, MPI_PROD, root, MPI_COMM_WORLD);
		if(rank == root)	display(recvbuf, n, true);
		MPI_Reduce(&tmpsend, &tmprecv, n, MPI_INT, MPI_PROD, root, MPI_COMM_WORLD);
		if(rank == root)	display(tmprecv, n, false);
	}
//###########################################################################################################################################//
	else if(strcmp(argv[2], "&") == 0)
	{
		MPI_Myreduce(&sendbuf, &recvbuf, n, MPI_INT, MPI_BAND, root, MPI_COMM_WORLD);
		if(rank == root)	display(recvbuf, n, true);
		MPI_Reduce(&tmpsend, &tmprecv, 1, MPI_INT, MPI_BAND, root, MPI_COMM_WORLD);
		if(rank == root)	display(tmprecv, n, false);
	}
//###########################################################################################################################################//
	else if(strcmp(argv[2], "|") == 0)
	{
		MPI_Myreduce(&sendbuf, &recvbuf, n, MPI_INT, MPI_BOR, root, MPI_COMM_WORLD);
		if(rank == root)	display(recvbuf, n, true);
		MPI_Reduce(&tmpsend, &tmprecv, n, MPI_INT, MPI_BOR, root, MPI_COMM_WORLD);
		if(rank == root)	display(tmprecv, n, false);
	}
//###########################################################################################################################################//
	else if(strcmp(argv[2], "^") == 0)
	{
		MPI_Myreduce(&sendbuf, &recvbuf, n, MPI_INT, MPI_BXOR, root, MPI_COMM_WORLD);
		if(rank == root)	display(recvbuf, n, true);
		MPI_Reduce(&tmpsend, &tmprecv, n, MPI_INT, MPI_BXOR, root, MPI_COMM_WORLD);
		if(rank == root)	display(tmprecv, n, false);
	}
//###########################################################################################################################################//
	else if(strcmp(argv[2], "MIN") == 0)
	{
		MPI_Myreduce(&sendbuf, &recvbuf, n, MPI_INT, MPI_MIN, root, MPI_COMM_WORLD);
		if(rank == root)	display(recvbuf, n, true);
		MPI_Reduce(&tmpsend, &tmprecv, 1, MPI_INT, MPI_MIN, root, MPI_COMM_WORLD);
		if(rank == root)	display(tmprecv, n, false);
	}
//###########################################################################################################################################//
	else if(strcmp(argv[2], "MAX") == 0)
	{
		MPI_Myreduce(&sendbuf, &recvbuf, n, MPI_INT, MPI_MAX, root, MPI_COMM_WORLD);
		if(rank == root)	display(recvbuf, n, true);
		MPI_Reduce(&tmpsend, &tmprecv, 1, MPI_INT, MPI_MAX, root, MPI_COMM_WORLD);
		if(rank == root)	display(tmprecv, n, false);
	}
//###########################################################################################################################################//
	else
	{
		cout << "\n\nOperator MPI_LXOR, MPI_MAXLOC, MPI_MINLOC, MPI_LOR, MPI_LAND and datatypes other than int is not supported yet\n";
		return 1;
	}

	MPI_Finalize();
	return 0;
}
