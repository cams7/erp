/**
 * @version 30/07/2011 <BR>
 * @author Setpoint Informática Ltda. <BR>
 * @author Fabiano Frizzo(ffrizzo at gmail.com) <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.fnc.view.dialog.utility <BR>
 *         Classe:
 * @(#)DLEstornoMultiplaBaixaPagamento.java <BR>
 * 
 *                   Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                   modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                   na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                   Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                   sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                   Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                   Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                   de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * 
 */

package org.freedom.modulos.fnc.view.dialog.utility;

import java.awt.Component;

import org.freedom.infra.model.jdbc.DbConnection;

public class DLEstornoMultiplaBaixaPagamento extends DLEstornoMultiplaBaixa {

	private static final long serialVersionUID = 1L;

	public DLEstornoMultiplaBaixaPagamento( Component cOrig, DbConnection cn, int iCodRec, int iNParc ) {

		super( cOrig, cn, iCodRec, iNParc );
	}

	@ Override
	public String getSqlSelect() {
		StringBuilder sSQL = new StringBuilder();
		sSQL.append( "SELECT  S.DATASUBLANCA, S.VLRSUBLANCA * -1 VLRSUBLANCA ,S.HISTSUBLANCA, S.CODLANCA " );
		sSQL.append( "FROM FNSUBLANCA S, FNLANCA L WHERE S.CODLANCA=L.CODLANCA " );
		sSQL.append( "AND S.CODEMP=L.CODEMP AND S.CODFILIAL=L.CODFILIAL " );
		sSQL.append( "AND L.CODPAG=? AND L.NPARCPAG=? AND L.CODEMP=? " );
		sSQL.append( "AND L.CODFILIAL=? AND S.CODSUBLANCA=0 " );
		sSQL.append( "ORDER BY DATASUBLANCA" );
		return sSQL.toString();
	}

	
}
