#!/bin/sh
rm JBemaFI32_lin.o
rm JBemaFI32.so
gcc -c -O -fpic -I $JAVA_HOME/include -I $JAVA_HOME/include/linux -I ./ JBemaFI32_lin.c
ld --shared -o JBemaFI32.so JBemaFI32_lin.o
