Freedom ERP
========================
* Autor: Setpoint Informática Ltda.
* Tecnologias: JDBC, Swing.
* Banco de dados: Firebird 1.5.6.
* Resumo: Sistema integrado de gestão empresarial.
* Linguagem: Java
* Fonte: <http://freedom-erp.googlecode.com/svn/tags/1.2/1.2.6/1.2.6.5>.  

O que é o Freedom ERP?
-----------

É um software livre para Gestão Empresarial. Pode ser utilizado em estabelecimentos comerciais, varejistas, atacadistas e industriais. Dividido em vários módulos, atende a diversas necessidades de empresas em diferentes áreas de atuação.

Descrição dos módulos do Freedom ERP
-------------------

* __FreedomCRM__: CRM - (Customer Relationship Management) é uma expressão em inglês que pode ser traduzida para a língua portuguesa como Gestão de Relacionamento com o Cliente. Para mais informações acesse o link: <http://www.freedom.org.br/faces/pages/crm.xhtml>.

* __FreedomGMS__: O Módulo FreedomGMS permite a gestão de ordens de serviços. Para mais informações acesse o link: <http://www.freedom.org.br/faces/pages/gms.xhtml>.

* __FreedomPDV__: O Módulo PDV controla o caixa dos estabelecimentos provendo um mecanismo de venda simples e ágil, com a impressão de cupons em impressoras fiscais (Bematech). Para mais informações acesse o link: <http://www.freedom.org.br/faces/pages/pdv.xhtml>.

* __FreedomFNC__: O Módulo Financeiro disponibiliza ferramentas para o total controle financeiro da sua empresa, como contas a pagar, contas a receber, movimentação bancária, emissão de recibos, emissão de boletos bancários com código de barras (Banco do Brasil, Caixa Econômica Federal, Bradesco, Sicredi e Itaú), arquivos de remessa padrão CNAB e CIAC, baixa automática de títulos através de arquivos de retorno (Banco do Brasil, Caixa Econômica Federal, Bradesco e Sicredi). Para mais informações acesse o link: <http://www.freedom.org.br/faces/pages/fnc.xhtml>.

* __FreedomPCP__: Acesse as informações desse módulo através do link: <http://www.freedom.org.br/faces/pages/pcp.xhtml>.

* __FreedomSTD__: O FreedomSTD é o módulo que reúne diversas funcionalidades necessárias para agilizar e controlar os processos de uma empresa. Para mais informações acesse o link: <http://www.freedom.org.br/faces/pages/std.xhtml>.

Sistema requerido
-------------------

* JDK 1.8
* Maven 3.3.9
* Freedom 1.2.6.5
* Firebird 1.5.6

Configurações iniciais
-------------------

1. Através do link: <https://code.google.com/p/freedom-erp/downloads/list>, baixe o __freedomERP-1.2.6.5-windows-installer__ e o instale no diretório `c:\opt\freedom`.
2. Através do link: <http://www.firebirdsql.org/en/firebird-1-5>, baixe o __Firebird-1.5.6.5026-0-Win32__ e o instale no diretório `c:\opt\firebird`.

Para incluir algumas depedências que não são encontradas no repositório oficial do maven
-------------------

No diretório onde o projeto foi baixado, execute as linhas de comando abaixo:

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

No diretório onde o projeto foi baixado, execute as linhas de comando abaixo:
	
	mvn clean install
	java -DARQINI='\opt\freedom\ini\freedom.ini' -jar freedom/target/freedom.jar