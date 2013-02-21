#include <iostream>
#include <mpi.h>
#include <cstdlib>
#include <cmath>

using namespace std;

int size, rank;


int floyds(int **arr, int s, int n)
{
	int kcol[n];
	int root;
	for(int k=0; k<n; k++)
	{
		root = k % size;
		if(rank == root)
		{
			for(int i=0; i<n; i++)	kcol[i] = arr[k % s][i];
		}
		MPI_Bcast(kcol, n, MPI_INT, root, MPI_COMM_WORLD);
		for(int i=0; i<s; i++)
		{
			for(int j=0; j<n; j++)	arr[i][j] = min(arr[i][j], (arr[i][k] + kcol[j]));
		}
	}
}

int main(int argc, char * argv[])
{
	system("clear");
	if(argc != 2)
	{
		cout << "Invalid number of aurguments! Required 1\n";
		return -1;
	}
	int arr[atoi(argv[1])][atoi(argv[1])], n = atoi(argv[1]);
	if(n < size)
	{
		cout << "Rows cannot be less than total number of process!\n";
		return 1;
	}
	//populating buffer
	for(int i=0; i<n; i++)
	{
		for(int j=0; j<n; j++)
		{
			if(i == j)	arr[i][j] = 0;
			else	arr[i][j] = rand() % (int) pow(n,2.0);
		}
	}
	// Displaying buffer
	if(rank == 0)
	{
		cout << "DAG is \n";
		for(int i=0; i<n; i++)
		{
			for(int j=0; j<n; j++)
				cout << arr[i][j] << "\t";
			cout << "\n";
		}
		cout << "\n\nSerial Floyds Algo \n";
		for(int k=0; k<n; k++)
		{
			for(int i=0; i<n; i++)
			{
				for(int j=0; j<n; j++)	arr[i][j] = min(arr[i][j], (arr[k][j] + arr[i][k]));
			}
		}
		for(int i=0; i<n; i++)
		{
			for(int j=0; j<n; j++)
				cout << arr[i][j] << "\t";
			cout << "\n";
		}
		cout << "\n\n";
	}
	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &size);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	long s = 0;
	// Partitioning
	for(int i=0; (rank + i * size) < n; i++)
		s++;	
	int ** parr = new int * [s];
	for(int i=0; i<s; i++)	parr[i] = new int [n];
	for(int i=0; (rank + i * size) < n && i < s; i++)
	{
		for(int j=0; j<n; j++)	parr[i][j] = arr[(rank + i * size)][j];
	}
	// Algo
	floyds(parr, s, n);
	sleep(1);
	// Displaying buffer
	if(rank == 0)
	{
	cout << "########################################################################################################################\n\n";
	cout << "DAG after Paralel fluyds algo\n";	
	}
	for(int i=0; i<s; i++)
	{
		for(int j=0; j<n; j++)
			cout << parr[i][j] << "\t";
		cout << "\n";
	}
	sleep(1);
	if(rank == 0)
	cout << "\n\n########################################################################################################################\n\n";
	delete  parr;
	parr = NULL;
	MPI_Finalize();
	return 0;
}
