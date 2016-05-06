Freedom ERP
========================
Autor: Setpoint Informática Ltda.
Tecnologias: JDBC, Swing.
Banco de dados: Firebird 1.5.6.
Resumo: Sistema integrado de gestão empresarial.
Linguagem: Java
Fonte: <http://freedom-erp.googlecode.com/svn/tags/1.2/1.2.6/1.2.6.5>.  

O que é o Freedom ERP?
-----------

É um software livre para Gestão Empresarial. Pode ser utilizado em estabelecimentos comerciais, varejistas, atacadistas e industriais. Dividido em vários módulos, atende a diversas necessidades de empresas em diferentes áreas de atuação.

Sistema requerido
-------------------

* JDK 1.8
* Maven 3.3.9
* Firebird 1.5.6

Configurações iniciais
-------------------
1. Baixe o firebird e instale-o no diretório `c:\opt\firebird`.
2. Baixe o freedom-erp e instale-o no diretório `c:\opt\freedom`.

Para incluir algumas depedências que não são encontradas no repositório oficial do maven
-------------------

No direitório onde o projeto foi baixado, execute os comandos abaixo:

	mvn install:install-file -DgroupId=comm -DartifactId=comm -Dversion=2.0 -Dpackaging=jar -Dfile=other-libs/comm-2.0.jar
	mvn install:install-file -DgroupId=bizcal -DartifactId=bizcal -Dversion=NOT_INFORMED -Dpackaging=jar -Dfile=other-libs/bizcal.jar
	mvn install:install-file -DgroupId=br.gov.sp.fazenda.dsge.brazilutils -DartifactId=dsgebrazilutils -Dversion=NOT_INFORMED -Dpackaging=jar -Dfile=other-libs/dsgebrazilutils.jar
	mvn install:install-file -DgroupId=softwareexpress.sitef -DartifactId=jclisitef -Dversion=NOT_INFORMED -Dpackaging=jar -Dfile=other-libs/jclisitef.jar
	mvn install:install-file -DgroupId=net.sf.nachocalendar -DartifactId=nachocalendar -Dversion=0.23 -Dpackaging=jar -Dfile=other-libs/nachocalendar-0.23.jar
	mvn install:install-file -DgroupId=lu.tudor.santec -DartifactId=santec -Dversion=NOT_INFORMED -Dpackaging=jar -Dfile=other-libs/santec.jar
	mvn install:install-file -DgroupId=com.birosoft.liguid -DartifactId=liquidlnf -Dversion=2.9.1 -Dpackaging=jar -Dfile=other-libs/liquidlnf-2.9.1.jar
	mvn install:install-file -DgroupId=net.beeger.squareness -DartifactId=squareness -Dversion=NOT_INFORMED -Dpackaging=jar -Dfile=other-libs/squareness.jar
	mvn install:install-file -DgroupId=org.eclipse.jdt.core.compiler -DartifactId=jdt-compiler -Dversion=NOT_INFORMED -Dpackaging=jar -Dfile=other-libs/jdt-compiler.jar
	
Para rodar o programa
-------------------

No direitório onde o projeto foi baixado, execute o comando abaixo:

	java -DARQINI='\opt\freedom\ini\freedom.ini' -jar freedom/target/freedom.jar