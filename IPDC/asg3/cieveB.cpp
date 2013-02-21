// paralel cieve Algorithm

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
	bool isVisited;
	Series()
	{
		value = 0;
		isMarked = false;
		isVisited = false;
	}
};


int cieveAlgo(Series *arr, int n, int total)
{
	for(int i=0; i<total; i++)
	{
		int recv = -4;	
		int root = (total - i) % size;
		if(rank == root)
		{
			for(int j=0; j<n; j++)
			{
				if(arr[j].isVisited == false && arr[j].isMarked == false)
				{
					recv = arr[j].value;
					arr[j].isVisited = true; 
					break;
				}
			}
		}
		MPI_Bcast(&recv, 1, MPI_INT, root, MPI_COMM_WORLD);
		for(int j=0; j<n; j++)
		{
			if(arr[j].isMarked == false && arr[j].value % recv == 0 && recv != arr[j].value)
				arr[j].isMarked = true; 
		}
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
	Series arr[n];
	for(int i=2, j=0; j<n; i++, j++)
		arr[j].value = i;
	if(rank == 0)
	{
		cout << "Series is following ";
		for(int i=0; i<n; i++)
			cout << arr[i].value << ", ";
		cout << "\n\n";
	}
	long s = 0;
	// Partitioning
	for(int i=0; (rank + i * size) < n; i++)
		s++;
		
	Series parr[s];
	for(int i=0; (rank + i * size) < n && i < s; i++)
			parr[i].value = arr[(rank + i * size)].value;
	// Algorithm
	cieveAlgo(parr, s, n);
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
