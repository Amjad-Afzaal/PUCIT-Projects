#include <iostream>
#include <mpi.h>
#include <sys/time.h>
#include <cstdlib>

using namespace std;

int rank, size;

int main(int argc, char * argv[])
{
	system("clear");
	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &size);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	MPI_Status status;
	if(rank == 0)
	{
		double sum = 0.0;
		for(int i=0; i<100000; i++){
		timeval st;
		gettimeofday(&st, NULL);
		int buf = 0;
		MPI_Send(&buf, 1, MPI_INT, size - 1, 0, MPI_COMM_WORLD);
		MPI_Recv(&buf, 1, MPI_INT, size - 1, 0, MPI_COMM_WORLD, &status);
		timeval rt;
		gettimeofday(&rt, NULL);
		sum += (double)((rt.tv_usec  - st.tv_usec ) / 2);// < 0 ? 0 : (rt.tv_usec  - st.tv_usec ) / 2;
		//cout << "sum = " << sum << "\tLatency = " << (double)((rt.tv_usec  - st.tv_usec ) / 2) << "\n";
		}
		cout << "Avg Latency in microseconds = " << (double)sum / 100000<< endl;
	}
	if(rank == size -1)
	{
		int buf = 0;
		for(int i=0; i<100000; i++){
		MPI_Recv(&buf, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
		MPI_Send(&buf, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
		}
	}
	MPI_Finalize();
	return 0;
}
