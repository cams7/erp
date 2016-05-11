select idgrpusu,nomegrpusu,comentgrpusu,dtins,hins,idusuins,dtalt,halt,idusualt from sggrpusu where codemp = 99 and codfilial = 1 and idgrpusu='DESENV';
--insert into sggrpusu (codemp,codfilial,idgrpusu,nomegrpusu,comentgrpusu) values (?,?,?,?,?)
--delete from sggrpusu where codemp=? and codfilial = ? and idgrpusu=?
--update sggrpusu set  nomegrpusu=?,comentgrpusu=? where idgrpusu=? and codemp=? and codfilial=?
--insert into sggrpusu (codemp,codfilial,idgrpusu,nomegrpusu,comentgrpusu) values (?,?,?,?,?)
--delete from sggrpusu where codemp=? and codfilial = ? and idgrpusu=?
--update sggrpusu set  nomegrpusu=?,comentgrpusu=? where idgrpusu=? and codemp=? and codfilial=?
SELECT idgrpusu,nomegrpusu  FROM sggrpusu;
SELECT codcc,anocc,desccc  FROM fncc WHERE CODEMP=99 AND CODFILIAL=1 AND NIVELCC=10 AND ANOCC=2013