#!/bin/sh

/usr/lib/firebird2/bin/isql -i stp_udf_lin.sql /usr/lib/firebird2/security.fdb -u sysdba -p masterkey > stp_udf_lin.log
