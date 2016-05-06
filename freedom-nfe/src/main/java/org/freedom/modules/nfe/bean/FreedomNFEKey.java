/*
 * Projeto: Setpoint-nfe
 * Pacote: org.freedom.modulos.nfe.database.jdbc
 * Classe: @(#)NFEConnectionFactory.java
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA <BR> <BR>
 */
package org.freedom.modules.nfe.bean;

/**
 * 
 * @author Setpoint Informática Ltda./Robson Sanchez
 * @version 04/04/2009
 * @version 16/03/2010 Anderson Sanchez
 */
public class FreedomNFEKey extends AbstractNFEKey {

	public static final String CODEMP = "CODEMP";

	public static final String CODFILIAL = "CODFILIAL";

	public static final String CODVENDA = "CODVENDA";

	public static final String CODCOMPRA = "CODCOMPRA";

	public static final String TIPOVENDA = "TIPOVENDA";

	public static final String DOCVENDA = "DOCVENDA";

	public static final String DOCCOMPRA = "DOCCOMPRA";

	public static final String DIRNFE = "DIRNFE";

	public FreedomNFEKey(Integer codemp, Integer codfilial, String tipovenda, Integer codvenda, Integer docvenda, String dirNFE) {

		put(CODEMP, codemp);
		put(CODFILIAL, codfilial);
		put(TIPOVENDA, tipovenda);
		put(CODVENDA, codvenda);
		put(DOCVENDA, docvenda);
		put(DIRNFE, dirNFE);
	}

	public FreedomNFEKey(Integer codemp, Integer codfilial, Integer codcompra, Integer doccompra, String dirNFE) {

		put(CODEMP, codemp);
		put(CODFILIAL, codfilial);
		put(CODCOMPRA, codcompra);
		put(DOCCOMPRA, doccompra);
		put(DIRNFE, dirNFE);
	}

}
