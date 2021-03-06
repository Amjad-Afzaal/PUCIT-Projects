// Serial Cieve algorithm

#include <iostream>
#include <cstdlib>

using namespace std;

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


int main(int argc, char * argv[])
{
	system("clear");
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
	cout << "Series is following ";
	for(int i=0; i<n; i++)
		cout << arr[i].value << ", ";
	cout << "\n\n";
	// Algorithm
	for(int i=0; i<n; i++)
	{
		if(arr[i].isMarked == false)
		{
			for(int j=i+1; j<n; j++)
			{
				if(arr[j].value % arr[i].value == 0)	arr[j].isMarked = true;	
			}
		}
	}
	cout << "Prime numbers in the series are ";
	for(int i=0; i<n; i++)
	{
		if(arr[i].isMarked == false)	cout << arr[i].value << ", ";
	}	
	cout << "\n\n";
	return 0;
}
