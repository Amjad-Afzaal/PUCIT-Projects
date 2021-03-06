#include <iostream>
#include <mpi.h>

using namespace std;

int main(int argc, char * argv [])
{
	MPI_Init(&argc, &argv);
	int size, rank, length;
	char name[20];
	MPI_Comm_size(MPI_COMM_WORLD, &size);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	MPI_Get_processor_name(name, &length);
	cout << "I am process with rank " << rank << " running on " << name <<endl;
	MPI_Finalize();
	return 0;
}
