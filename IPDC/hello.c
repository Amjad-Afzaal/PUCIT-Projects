#include <stdio.h>
#include <mpi.h>

int main(int argc, char * argv [])
{
	MPI_Init(&argc, &argv);
	int size, rank, length;
	char name[20];
	MPI_Comm_size(MPI_COMM_WORLD, &size);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	MPI_Get_processor_name(name, &length);
	printf("I am process with rank %d, running on %s\n", rank, name);
	MPI_Finalize();
	return 0;
}
