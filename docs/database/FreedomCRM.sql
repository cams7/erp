--Busca código CNAE (Classificação Nacional de Atividades Econômicas)
SELECT *  FROM sgcnae where codcnae like '62.01-5%';
SELECT *  FROM sgcnae where codcnae like '96.02-5%';
SELECT codcnae,desccnae  FROM sgcnae where desccnae like '%programa%';
SELECT codcnae,desccnae  FROM sgcnae where desccnae like '%beleza%';
SELECT codcnae,desccnae  FROM sgcnae where desccnae like '%papelaria%';
SELECT codcnae,desccnae  FROM sgcnae where desccnae like '%civil%';
SELECT codcnae,desccnae  FROM sgcnae where desccnae like '%transporte%';


--Busca código do banco
SELECT *  FROM fnbanco where nomebanco like '%Santander%';
SELECT codbanco,nomebanco  FROM fnbanco where nomebanco like '%Santander%';

SELECT codsetor,descsetor  FROM vdsetor WHERE CODEMP=99 AND CODFILIAL=1;

SELECT codclascli,descclascli  FROM vdclascli WHERE CODEMP=99 AND CODFILIAL=1;
SELECT codtipocli,desctipocli  FROM vdtipocli WHERE CODEMP=99 AND CODFILIAL=1;
SELECT codmarca,descmarca,siglamarca,dtins,hins,idusuins,dtalt,halt,idusualt  FROM eqmarca WHERE CODEMP=99 AND CODFILIAL=1;
SELECT codfisc,descfisc  FROM lfclfiscal;
SELECT *  FROM lfclfiscal;
SELECT codvarg,descvarg  FROM eqvargrade WHERE CODEMP=99 AND CODFILIAL=1;