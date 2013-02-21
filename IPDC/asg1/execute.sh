#!/bin/bash

programName=$1
if [ $programName == "reduce" ]
	then 
	mpicxx $programName.cpp -o $programName
	mpiexec -n $2 ./$programName $3 $4 $5

elif [ $programName == "sendrecv" ]
	then
	mpicxx $programName.cpp -o $programName
	mpiexec -n $2 ./$programName 

elif [ $programName == "allreduce" ]
	then 
	mpicxx $programName.cpp -o $programName
	echo $3
	echo $4
	mpiexec -n $2 ./$programName $3 $4

fi
