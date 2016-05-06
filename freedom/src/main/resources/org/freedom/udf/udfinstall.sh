#!/bin/sh
/opt/firebird/bin/isql -i stp_udf_lin.sql /opt/firebird/security.fdb -u sysdba -p masterkey > stp_udf_lin.log
