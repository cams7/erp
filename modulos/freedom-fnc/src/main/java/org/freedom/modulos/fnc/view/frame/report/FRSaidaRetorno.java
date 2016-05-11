/**
 * @version 06/06/2005 <BR>
 * @author Setpoint Informática Ltda / Robson Sanchez.
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.fnc <BR>
 *         Classe:
 * @(#)FRSaidaRetorno.java <BR>
 * 
 *                         Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                         modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                         na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                         Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                         sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                         Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                         Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                         de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                         ? <BR>
 * 
 */
package org.freedom.modulos.fnc.view.frame.report;

import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.BorderFactory;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

public class FRSaidaRetorno extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCliente = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	public FRSaidaRetorno() {

		setTitulo( "SAÍDA X RETORNO" );
		setAtribos( 160, 80, 285, 180 );

		txtDataini.setVlrDate( new Date() );
		txtDatafim.setVlrDate( new Date() );

		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );

		adic( new JLabelPad( "Periodo:" ), 7, 5, 100, 20 );
		adic( lbLinha, 60, 15, 210, 2 );
		adic( new JLabelPad( "De:" ), 7, 30, 30, 20 );
		adic( txtDataini, 32, 30, 97, 20 );
		adic( new JLabelPad( "Até:" ), 135, 30, 30, 20 );
		adic( txtDatafim, 165, 30, 97, 20 );
		adic( new JLabelPad( "Cliente" ), 7, 55, 300, 20 );
		adic( txtCliente, 7, 75, 212, 20 );

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
	}

	public void actionPerformed( ActionEvent evt ) {

		super.actionPerformed( evt );
	}

	public ResultSet getResultSet() {

		String NomeCli = "";

		NomeCli = "'" + txtCliente.getVlrString() + "%'";

		String sSQL = " select vdcliente.Nomecli as nome, fnbanco.Nomebanco as banco, " + " sgcheque.NCHEQUE, sgcheque.predata, sgcheque.valor, " + " sgcheque.codbanco, sgcheque.codcli, sgscheque.NSAIDA ,sgscheque.NCHEQUE, sgscheque.TSAIDA, "
				+ " sgscheque.DESCRICAO, sgscheque.DTINS as Dates, sgrcheque.NSAIDA, sgrcheque.NCHEQUE, sgrcheque.DTINS as Dater, " + " sgrcheque.codigor from vdcliente, fnbanco, sgcheque, sgscheque, sgrcheque where " + " vdcliente.CODCLI=sgcheque.CODCLI and "
				+ " fnbanco.codbanco=sgcheque.codbanco and UPPER(vdcliente.Nomecli) LIKE UPPER( " + NomeCli + " ) " + " and sgcheque.NCHEQUE=sgscheque.NCHEQUE and sgcheque.CODBANCO=sgscheque.CODBANCO and sgscheque.TSAIDA=1 "
				+ " and sgrcheque.NCHEQUE=sgscheque.NCHEQUE and sgrcheque.CODBANCO=sgscheque.CODBANCO and sgrcheque.NSAIDA=sgscheque.NSAIDA and sgscheque.dtins BETWEEN ? AND ? ORDER BY sgscheque.DTINS";

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement( sSQL );
			ps.setDate( 1, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( 2, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

			rs = ps.executeQuery();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}

		return rs;
	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}

		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		int numcheque = 0;
		boolean pass = false;

		String sPag = "";

		String sDataini = "";
		String sDatafim = "";

		sDataini = txtDataini.getVlrString();
		sDatafim = txtDatafim.getVlrString();

		ResultSet rs = getResultSet();

		try {

			imp.limpaPags();

			while ( rs.next() ) {

				if ( numcheque != rs.getInt( "NCHEQUE" ) ) {
					pass = true;
					numcheque = rs.getInt( "NCHEQUE" );
				}
				else
					pass = false;

				if ( imp.pRow() >= ( linPag - 1 ) ) {
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "|" + StringFunctions.replicate( "-", 89 ) + "|" );
					imp.incPags();
					imp.eject();
				}
				if ( imp.pRow() == 0 ) {
					imp.montaCab();
					imp.setTitulo( "Relatório de saida x retorno" + sPag );
					imp.addSubTitulo( "RELATÓRIO DE SAIDA X RETORNO " + sPag + "   -   PERIODO DE :" + sDataini + " ATE: " + sDatafim );
					imp.impCab( 91, true );
					imp.say( imp.pRow() + 0, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "|" + StringFunctions.replicate( "-", 88 ) + "|" );
				}

				if ( pass == true ) {

					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "=", 90 ) );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );

					imp.say( imp.pRow() + 0, 0, " Cliente:" );
					imp.say( imp.pRow() + 0, 2, rs.getString( "NOMECLI" ) );

					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );

					imp.say( imp.pRow() + 0, 2, "Num.Cheque:" );
					imp.say( imp.pRow() + 0, 2, rs.getString( "NCHEQUE" ) );
					imp.say( imp.pRow() + 0, 3, "Valor:" );
					imp.say( imp.pRow() + 0, 2, rs.getString( "VALOR" ) );
					imp.say( imp.pRow() + 0, 3, "Banco:" );
					imp.say( imp.pRow() + 0, 2, rs.getString( "banco" ) );

					imp.say( imp.pRow() + 2, 0, "" + imp.comprimido() );

					imp.say( imp.pRow() + 0, 2, "Data/Saída" );
					imp.say( imp.pRow() + 0, 3, "Descrição" );
					imp.say( imp.pRow() + 0, 42, "Data/Retorno" );
					imp.say( imp.pRow() + 0, 5, "Cód.Retorno" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "-", 90 ) );
				}

				imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
				imp.say( imp.pRow() + 0, 2, Funcoes.dateToStrDate( rs.getDate( "Dates" ) ) );
				imp.say( imp.pRow() + 0, 3, rs.getString( "DESCRICAO" ) );
				imp.say( imp.pRow() + 0, 5, Funcoes.dateToStrDate( rs.getDate( "Dater" ) ) );
				imp.say( imp.pRow() + 0, 11, rs.getString( "Codigor" ) );

				if ( imp.pRow() >= linPag ) {
					imp.incPags();
					imp.eject();
				}
			}

			imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
			imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "=", 90 ) );

			imp.eject();

			imp.fechaGravacao();

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela de Cheques!" + err.getMessage() );
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
	}
}
