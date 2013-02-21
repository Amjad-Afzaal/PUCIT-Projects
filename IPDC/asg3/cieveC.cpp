// Optimize paralel cieve Algorithm

#include <iostream>
#include <mpi.h>
#include <sys/time.h>
#include <cstdlib>
#include <math.h>

using namespace std;

int rank, size;

struct Series
{
	int value;
	bool isMarked;
	Series()
	{
		value = 0;
		isMarked = false;
	}
};


int cieveAlgo(Series *arr, int n, int total)
{
	for(int i=0; i<n; i++)
	{
		if(arr[i].isMarked == false)
		{
			for(int j=i+1; j<n; j++)
			{
				if(arr[j].value % arr[i].value == 0)
					arr[j].isMarked = true;
			}
		}
	}
	int N = total % size == 0 ? n : total / size + 1;
	Series send[N], recv[N];
	for(int i=0; i<N; i++)	recv[i].value = -4;
	for(int i=0; i<N; i++)	send[i].value = -4;
	for(int i=0, j=0; i<n; i++)
	{
		if(arr[i].isMarked == false)
		{
			send[j].value = arr[i].value;
			j++;
		}
	}
	for(int i=0; i<size; i++)
	{
		if(rank == i)
		{
			for(int j=0; j<N; j++)	recv[j].value = send[j].value;
		}
		MPI_Bcast(recv, N, MPI_INT, i, MPI_COMM_WORLD);
		if(rank != i)
		{
			for(int j=0; j<N; j++)
			{
				if(recv[j].value != -4)		
				{
					for(int k=0; k<n; k++)
					{
						if(arr[k].value % recv[j].value == 0)	arr[k].isMarked = true;  
					}
				}
			}
		}
		for(int i=0; i<n; i++)	recv[i].value = -4;
	}
}

int main(int argc, char * argv[])
{
	system("clear");
	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &size);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	MPI_Status status;
	if(argc != 2)
	{
		cout << "Invalid no. of cmd aurguments! required 1\n\n";
		return 1;	
	}
	int n = atoi(argv[1]) - 1;
	if(n <= 0)
	{
		cout << "Please give positive cmd aurgument and greater than 1!\n\n";
		return 1;
	}
	Series tmp[n];
	for(int i=2, j=0; j<n; i++, j++)
		tmp[j].value = i;
	if(rank == 0)
	{
		/*cout << "Series is following ";
		for(int i=0; i<n; i++)
			cout << tmp[i].value << ", ";
		cout << "\n\n";*/
	}
	// Discarding even numbers as they cannot be prime
	n % 2 == 1 ? n = (n/2) + 1 : n = n / 2;
	Series arr[n];
	arr[0].value = 2;
	for(int i=3, j=1; j<n; i+=2, j++)
		arr[j].value = i;
	long s = 0;
	// Partitioning
	for(int i=0; (rank + i * size) < n; i++)
		s++;		
	Series parr[s];
	for(int i=0; (rank + i * size) < n && i < s; i++)
			parr[i].value = arr[(rank + i * size)].value;
	// Algorithm
	cieveAlgo(parr, s, n);
	//sleep(10);	
	if(rank == 0)
		cout << "Prime Numbers are ";
	for(int i=0; i<s; i++)
	{
		if(parr[i].isMarked == false)
			cout << parr[i].value << ", ";
	}
	cout << "\n\n";
	MPI_Finalize();
	return 0;
}
