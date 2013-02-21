#include <iostream>
#include <mpi.h>
#include <string>
#include <cstdlib>

using namespace std;

struct Process
{
	int prime[100000];
	int locations[100000];
	Process()
	{
		for(int i=0; i<100000; i++)
		{
			prime[i] = 0;
			locations[i] = -1;
		}
	}
	int getTotal()
	{
		int total = 0;
		for(int i=0; i<100000; i++)
		{
			if(prime[i] != 0)	total++;
			else break;
		}
		return total;
	}
};

void prime(int * arr, int start, int end, int rank)
{
	int prime[100000], locations[100000], k=0;
	for(int i=0; i<100000; i++)
	{
		prime[i] = 0;
		locations[i] = -1;
	}
	for(int i=start; i<=end; i++)
	{
		bool isPrime = true;
		if(arr[i] == 1 || arr[i] == 2)
		{
			prime[k] = arr[i];
			locations[k] = i+1;
			k++;
		}
		else
		{
			for(int j=2; j<=arr[i]/2; j++)
			{
				if(arr[i] % j == 0)
				{
					isPrime = false;
					break;
				}
			}
			if(arr[i] == 0)		isPrime = false;
			if(isPrime)	
			{
				prime[k] = arr[i];
				locations[k] = i+1;
				k++;				
			}
		}
	}
	MPI_Send(prime, k, MPI_INT, 0, rank, MPI_COMM_WORLD);
	MPI_Send(locations, k, MPI_INT, 0, rank, MPI_COMM_WORLD);	
}

int main(int argc, char * argv [])
{
	MPI_Init(&argc, &argv);
	int size, n = atoi(argv[1]), rank;
	MPI_Comm_size(MPI_COMM_WORLD, &size);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	// Creating n size array
	int * arr = new int [n];
	// Initializing array
	for(int i=0; i<n; i++)	arr[i] = rand() % n;
	// Displaying array
	if(rank == 0)
	{	
		cout << "Randomly populated array is \n";	
		for(int i=0; i<n; i++)	cout << arr[i] << "\n";	
	}	
	int start, end, partition_size = n/size;
	if(rank != 0)
	{	
		start = rank * partition_size;
		end = start + partition_size - 1;
		prime(arr, start, end, rank); 
	}
	// Sumarize result
	else
	{
		Process parr[size];
		MPI_Status status;
		for(int i=0, k=0; i<partition_size; i++)
		{
			bool isPrime = true;
			if(arr[i] == 1 || arr[i] == 2)
			{
				parr[0].prime[k] = arr[i];
				parr[0].locations[k] = i+1;
				k++;
			}
			else
			{
				for(int j=2; j<=arr[i]/2; j++)
				{
					if(arr[i] % j == 0)
					{
						isPrime = false;
						break;
					}
				}
				if(arr[i] == 0)		isPrime = false;
				if(isPrime)	
				{
					parr[0].prime[k] = arr[i];
					parr[0].locations[k] = i+1;
					k++;				
				}
			}
		}		
		int total = parr[0].getTotal();
		for(int i=1; i<size; i++)
		{
			MPI_Recv(parr[i].prime, partition_size, MPI_INT, i, i, MPI_COMM_WORLD, &status);
			MPI_Recv(parr[i].locations, partition_size, MPI_INT, i, i, MPI_COMM_WORLD, &status);
			total += parr[i].getTotal();
		}
		// Display sumerizing result
		cout << "\n\n\n============================Summary==========================================\n\n";
		cout << "I have found total " << total << " prime numbers which are following ";
		for(int i=0; i<size; i++)
		{
			for(int j=0; j<100000; j++)
			{
				if(parr[i].prime[j] != 0)	cout << parr[i].prime[j] << ", ";
				else break;
			}
		}
		cout << "\n\nOn following locations ";
		for(int i=0; i<size; i++)
		{
			for(int j=0; j<100000; j++)
			{
				if(parr[i].locations[j] != -1)	cout << parr[i].locations[j] << ", ";
				else break;
			}
		}
		cout << " respectively";
		cout << "\n\n==============================================================================\n\n";
	}
	delete arr;
	arr = NULL;
	MPI_Finalize();
	return 0;
}
