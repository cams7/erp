del stp_udf.dll
del stp_udf_win.o
gcc -c -o stp_udf_win.o -IC:\opt\eclipse\workspace\freedom\src\org\freedom\udf stp_udf_win.c
rem ld --shared -LC:\MinGW\lib stp_udf_win.o -o stp_udf.dll -lmycrypt
gcc --enable-stdcall-fixup -shared -o stp_udf.dll stp_udf_win.o


