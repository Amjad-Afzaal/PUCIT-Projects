#include <iostream>
#include <mpi.h>
#include <string>
#include <strings.h>
#include <cstdlib>
#include <stdlib.h>


using namespace std;

struct Process
{
	int locations[100];
	Process()
	{
		for(int i=0; i<100; i++)	locations[i] = -1;		
	}
	void copy(int * arr, int size)
	{
		//for(int i=0; i<100; i++)	locations[i] = -1;
		for(int i=0; i<100; i++)	locations[i] = arr[i];
	}
};

void search(int * arr, int start, int end, int key, int rank)
{
	int numOfTimes = 0;
	bool isFound = false;
	int locations[end - start + 1];
	for(int i=0; i<end-start+1; i++)	locations[i] = -1;
	for(int i=start, j=0; i<=end; i++)
	{
		if(arr[i] == key)
		{	
			numOfTimes ++ ;
			locations[j] = i+1;
			j++;
			isFound = true;
		}
	}
	if(isFound)
	{
		MPI_Send(&numOfTimes, 1, MPI_INT, 0, rank, MPI_COMM_WORLD);
		MPI_Send(locations, end-start+1, MPI_INT, 0, rank, MPI_COMM_WORLD);
	}
	else
	{
		int buf = 0;
		MPI_Send(&buf, 1, MPI_INT, 0, rank, MPI_COMM_WORLD);
		MPI_Send(locations, end-start+1, MPI_INT, 0, rank, MPI_COMM_WORLD);
	}
}

int main(int argc, char * argv [])
{
	MPI_Init(&argc, &argv);
	int size, rank, n = atoi(argv[1]);
	MPI_Comm_size(MPI_COMM_WORLD, &size);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	// Creating n size array
	int * arr = new int [n];
	// Initializing array
	for(int i=0; i<n; i++)	arr[i] = rand() % n;
	// Displaying array
	if(rank == 0)
	{	
		std::cout << "Randomly populated array is \n";	
		for(int i=0; i<n; i++)	std::cout << arr[i] << "\n";	
	}
	int start, end, partition_size = n/size, key = rand() % n + arr[n/2];
	if(rank == 0)	std::cout << "\nRandomly generated Key = " << key << std::endl << std::endl; 
	else
	{
		start = rank * partition_size;
		end = start + partition_size - 1;
		search(arr, start, end, key, rank);
	}
	// Receive the result
	if(rank == 0)
	{	
		Process parr[size];
		int totalTimes = 0, k=0;
		for(int i=0; i<partition_size; i++)
		{
			if(arr[i] == key)
			{		
				totalTimes++;
				parr[0].locations[k] = i+1;
				k++;
			}
		}


		for(int i=1; i<size; i++)
		{
			MPI_Status status;
			int numOfTimes;
			int locations[partition_size];
			MPI_Recv(&numOfTimes, 1, MPI_INT, i, i, MPI_COMM_WORLD, &status);
			MPI_Recv(locations, partition_size, MPI_INT, i, i, MPI_COMM_WORLD, &status);
			totalTimes += numOfTimes;
			parr[i].copy(locations, partition_size);
		}
		// Display sumerized result
		cout << "\n\n=================================Summary============================================\n\n";
		cout << "\n  I have found " << key << ", " << totalTimes << " number of times";
		cout << "\n  Locations are ";
		for(int i=0; i<size; i++)
		{
			for(int j=0; j<100; j++)
			{
				if(parr[i].locations[j] != -1)	cout << parr[i].locations[j] << ", ";
				else break;
			}
		}
		cout << "\n\n\n====================================================================================\n\n";		
	}

	delete arr;
	arr = NULL;
	MPI_Finalize();
	return 0;
}
