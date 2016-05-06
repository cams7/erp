#include </usr/src/freedom_udf/unixpass.h>
#include <stdio.h>
char *ib_password(char *val);
char *printLog(char *val);
char *ib_password(char *val) {
        val = (char *)mycrypt(val,"9z");
        val = (char *)(mycrypt(val+2,"9z")+2);
        return (char *)val;     
}

char *printLog(char *val) {
        FILE *myfile;
        char* fname = "/fb_freedom.log";
        myfile = fopen (fname, "a");
        if (!myfile) {
           printf("File %s not open\n", fname);
           val = "Erro";
        }
    else {
       fprintf(myfile, "\n");
       fprintf(myfile, val);
           fclose(myfile);
        }
    return (char *)val;
}
