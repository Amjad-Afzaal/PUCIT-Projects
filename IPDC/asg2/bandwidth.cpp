#include <iostream>
#include <mpi.h>
#include <sys/time.h>
#include <cstdlib>
#include <math.h>

using namespace std;

int rank, size;

int main(int argc, char * argv[])
{
	system("clear");
	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &size);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	MPI_Status status;
	long count = 1;
	double avgLatency = 0.3214; // get by latency program and it is in microseconds
	double bandwidth = 0.0;
	if(rank == 0)
	{
		for(int i=0; i<21; i++){
		timeval st;
		gettimeofday(&st, NULL);
		int buf[count];
		MPI_Send(&buf, count, MPI_INT, size - 1, 0, MPI_COMM_WORLD);
		MPI_Recv(&buf, count, MPI_INT, size - 1, 0, MPI_COMM_WORLD, &status);
		timeval rt;
		gettimeofday(&rt, NULL);
		; // < 0 ? 0 : (rt.tv_usec  - st.tv_usec ) / 2;
		if((double)((rt.tv_usec  - st.tv_usec ) / 2) > avgLatency)	bandwidth = count;
		count *= 2;		
		//cout << "sum = " << sum << "\tLatency = " << (rt.tv_usec  - st.tv_usec ) / 2 << "\n";
		}
		cout << "Avg bandwidth is = " << (double) ((bandwidth * 4)/pow(2.0,20)) << " MB/ microsends" << endl;
	}
	if(rank == size -1)
	{
		for(int i=0; i<21; i++){
		int buf[count];
		MPI_Recv(&buf, count, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
		MPI_Send(&buf, count, MPI_INT, 0, 0, MPI_COMM_WORLD);
		count *= 2;
		}
	}
	MPI_Finalize();
	return 0;
}
