--Busca código CNAE (Classificação Nacional de Atividades Econômicas)
SELECT *  FROM sgcnae where codcnae like '62.01-5%';
SELECT *  FROM sgcnae where codcnae like '96.02-5%';
SELECT codcnae,desccnae  FROM sgcnae where desccnae like '%programa%';
SELECT codcnae,desccnae  FROM sgcnae where desccnae like '%beleza%';

--Busca código do banco
SELECT *  FROM fnbanco where nomebanco like '%Santander%';
SELECT codbanco,nomebanco  FROM fnbanco where nomebanco like '%Santander%';