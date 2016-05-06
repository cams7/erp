#!/bin/sh
#gcc -c -O -fpic -fwritable-strings stp_udf_lin.ci
gcc -c -O -fpic stp_udf_lin.c

ld --shared -lcrypt -o stp_udf.so stp_udf_lin.o
