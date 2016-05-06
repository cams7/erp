DECLARE EXTERNAL FUNCTION ib_password
        CSTRING(32)
        RETURNS CSTRING(32) /*FREE_IT*/
        ENTRY_POINT 'ib_password' MODULE_NAME 'stp_udf';

DECLARE EXTERNAL FUNCTION printLog
        CSTRING(256)
        RETURNS INT
        ENTRY_POINT 'printLog' MODULE_NAME 'stp_udf';
                
create exception exc_not_permitted 'Aç nãpermitida';

set term ^ ;

create procedure AddUser
 (user_name varchar(128), passwd varchar(32))
 as
  begin
    if ((USER != 'SYSDBA') or
        (:user_name is null) or
        (:user_name = '') or
        (:passwd is null) or
        (:passwd = '')) then
      exception exc_not_permitted;

    insert into users (user_name, passwd)
      values (UPPER(:user_name), IB_PASSWORD(:passwd));
  end ^

CREATE PROCEDURE "CHECKUSER" 
(
  "SLOGIN" CHAR(128)
)
RETURNS
(
  "SRET" CHAR(1)
)
AS
declare variable ICONTA SMALLINT;
begin
  SRET = 'N';
  SELECT COUNT(*) FROM USERS WHERE UPPER(USER_NAME) = UPPER(:SLOGIN) INTO ICONTA;
  if (ICONTA > 0) then
    SRET = 'S';
  suspend;
end  ^

create procedure ChangePassword
 (user_name varchar(128), passwd varchar(32))
 as
  begin
    if (((USER != 'SYSDBA') and (USER != :user_name)) or
        (:user_name is null) or
        (:user_name = '') or
        (:passwd is null) or
        (:passwd = '')) then
      exception exc_not_permitted;

    update users
      set passwd = IB_PASSWORD(:passwd)
      where user_name = UPPER(:user_name);
  end ^
    
create procedure DeleteUser
 (user_name varchar(128))
 as
  begin
    if ((USER != 'SYSDBA') or
        (:user_name is null) or
        (:user_name = '')) then
      exception exc_not_permitted;

    delete from users
      where user_name = UPPER(:user_name);
  end ^

set term ; ^

grant execute on procedure ChangePassword to public;
grant update (passwd) on users to procedure ChangePassword;
