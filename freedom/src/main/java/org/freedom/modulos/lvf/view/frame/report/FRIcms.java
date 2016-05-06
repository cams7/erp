/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FRVendasIcms.java <BR>
 * 
 *                       Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                       modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                       na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                       Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                       sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                       Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                       Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                       de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                       Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.lvf.view.frame.report;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

public class FRIcms extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	public FRIcms() {

		setTitulo( "Icms sobre Vendas e Compras" );
		setAtribos( 80, 80, 290, 130 );

		txtDataini.setTipo( JTextFieldPad.TP_DATE, 10, 0 );
		txtDatafim.setTipo( JTextFieldPad.TP_DATE, 10, 0 );
		txtDataini.setVlrDate( new Date() );
		txtDatafim.setVlrDate( new Date() );
		adic( new JLabelPad( "Periodo:" ), 7, 5, 120, 20 );
		adic( new JLabelPad( "De:" ), 7, 30, 25, 20 );
		adic( txtDataini, 35, 30, 97, 20 );
		adic( new JLabelPad( "Até:" ), 140, 30, 25, 20 );
		adic( txtDatafim, 170, 30, 97, 20 );
	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;
		BigDecimal bVlrLiqVenda = null;
		BigDecimal bVlrBaseIcmsVenda = null;
		BigDecimal bVlrIcmsVenda = null;
		BigDecimal bVlrLiqCompra = null;
		BigDecimal bVlrBaseIcmsCompra = null;
		BigDecimal bVlrIcmsCompra = null;
		BigDecimal bVlrIcmsPagar = null;
		ImprimeOS imp = null;
		int linPag = 0;
		int cont = 0;

		try {

			imp = new ImprimeOS( "", con );
			linPag = imp.verifLinPag() - 1;
			imp.montaCab();
			imp.setTitulo( "Icms sobre Vendas" );
			imp.addSubTitulo( "ICMS SOBRE COMPRAS E VENDAS - PERIODO DE :" + txtDataini.getVlrString() + " Até: " + txtDatafim.getVlrString() );
			imp.limpaPags();

			bVlrLiqVenda = new BigDecimal( "0" );
			bVlrBaseIcmsVenda = new BigDecimal( "0" );
			bVlrIcmsVenda = new BigDecimal( "0" );
			bVlrLiqCompra = new BigDecimal( "0" );
			bVlrBaseIcmsCompra = new BigDecimal( "0" );
			bVlrIcmsCompra = new BigDecimal( "0" );
			bVlrIcmsPagar = new BigDecimal( "0" );

			sSQL = "SELECT V.DTEMITVENDA,T.ESTIPOMOV,SUM(V.VLRLIQVENDA)," + "SUM(V.VLRBASEICMSVENDA),SUM(V.VLRICMSVENDA) " + "FROM VDVENDA V,EQTIPOMOV T " + "WHERE V.DTEMITVENDA BETWEEN ? AND ? AND " + "V.CODTIPOMOV=T.CODTIPOMOV AND T.FISCALTIPOMOV='S' "
					+ "AND (NOT SUBSTR(V.STATUSVENDA,1,1)='C') GROUP BY V.DTEMITVENDA,T.ESTIPOMOV " + "UNION ALL " + "SELECT C.DTENTCOMPRA,T1.ESTIPOMOV,SUM(C.VLRLIQCOMPRA)," + "SUM(C.VLRBASEICMSCOMPRA),SUM(C.VLRICMSCOMPRA) " + "FROM CPCOMPRA C,EQTIPOMOV T1 "
					+ "WHERE C.DTENTCOMPRA BETWEEN ? AND ? AND " + "C.CODTIPOMOV=T1.CODTIPOMOV AND T1.FISCALTIPOMOV='S' " + "GROUP BY C.DTENTCOMPRA,T1.ESTIPOMOV ORDER BY 1,3";

			ps = con.prepareStatement( sSQL );
			ps.setDate( 1, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( 2, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			ps.setDate( 3, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( 4, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			rs = ps.executeQuery();

			while ( rs.next() ) {
				cont++;
				if ( imp.pRow() >= ( linPag - 1 ) ) {
					imp.say( imp.pRow() + 1, 0, imp.normal() );
					imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "-", 77 ) + "+" );
					imp.incPags();
					imp.eject();
				}

				if ( imp.pRow() == 0 ) {
					imp.impCab( 80, true );

					imp.say( imp.pRow(), 0, imp.normal() );
					imp.say( imp.pRow(), 0, "|" + StringFunctions.replicate( "-", 77 ) + "|" );
					imp.say( imp.pRow() + 1, 0, imp.normal() );
					imp.say( imp.pRow(), 0, "| Dt. Emissao | Tipo | Valor Liq.   | Base Calc.   | Valor Icms" );
					imp.say( imp.pRow(), 79, "|" );
					imp.say( imp.pRow() + 1, 0, imp.normal() );
					imp.say( imp.pRow(), 0, "|" + StringFunctions.replicate( "-", 77 ) + "|" );
				}

				imp.say( imp.pRow() + 1, 0, imp.normal() );
				imp.say( imp.pRow(), 0, "| " + StringFunctions.sqlDateToStrDate( rs.getDate( 1 ) ) );
				imp.say( imp.pRow(), 15, "| " + Funcoes.copy( rs.getString( 2 ), 0, 1 ) );
				imp.say( imp.pRow(), 22, "| " + Funcoes.strDecimalToStrCurrency( 13, 2, rs.getString( 3 ) ) + "| " + Funcoes.strDecimalToStrCurrency( 13, 2, rs.getString( 4 ) ) + "| " + Funcoes.strDecimalToStrCurrency( 13, 2, rs.getString( 5 ) ) );
				imp.say( imp.pRow(), 79, "|" );

				if ( Funcoes.copy( rs.getString( 2 ), 0, 1 ).equals( "S" ) ) {
					bVlrLiqVenda = bVlrLiqVenda.add( new BigDecimal( ( rs.getString( 3 ) != null ? rs.getString( 3 ) : "0" ) ) );
					bVlrBaseIcmsVenda = bVlrBaseIcmsVenda.add( new BigDecimal( ( rs.getString( 4 ) != null ? rs.getString( 4 ) : "0" ) ) );
					bVlrIcmsVenda = bVlrIcmsVenda.add( new BigDecimal( ( rs.getString( 5 ) != null ? rs.getString( 5 ) : "0" ) ) );
				}
				else {
					bVlrLiqCompra = bVlrLiqCompra.add( new BigDecimal( ( rs.getString( 3 ) != null ? rs.getString( 3 ) : "0" ) ) );
					bVlrBaseIcmsCompra = bVlrBaseIcmsCompra.add( new BigDecimal( ( rs.getString( 4 ) != null ? rs.getString( 4 ) : "0" ) ) );
					bVlrIcmsCompra = bVlrIcmsCompra.add( new BigDecimal( ( rs.getString( 5 ) != null ? rs.getString( 5 ) : "0" ) ) );
				}
			}

			if ( cont != 0 ) {
				bVlrIcmsPagar = bVlrIcmsPagar.add( bVlrIcmsVenda );
				bVlrIcmsPagar = bVlrIcmsPagar.add( bVlrIcmsCompra.negate() );
				imp.say( imp.pRow() + 1, 0, imp.normal() );
				imp.say( imp.pRow(), 0, "|" + StringFunctions.replicate( "-", 77 ) + "|" );
				imp.say( imp.pRow() + 1, 0, imp.normal() );
				imp.say( imp.pRow(), 0, "|" );
				imp.say( imp.pRow(), 2, "TOTAL COMPRAS" );
				imp.say( imp.pRow(), 22, "| " + Funcoes.strDecimalToStrCurrency( 13, 2, "" + bVlrLiqCompra ) + "| " + Funcoes.strDecimalToStrCurrency( 13, 2, "" + bVlrBaseIcmsCompra ) + "| " + Funcoes.strDecimalToStrCurrency( 13, 2, "" + bVlrIcmsCompra ) );
				imp.say( imp.pRow(), 79, "|" );

				imp.say( imp.pRow() + 1, 0, imp.normal() );
				imp.say( imp.pRow(), 0, "|" );
				imp.say( imp.pRow(), 2, "TOTAL VENDAS" );
				imp.say( imp.pRow(), 22, "| " + Funcoes.strDecimalToStrCurrency( 13, 2, "" + bVlrLiqVenda ) + "| " + Funcoes.strDecimalToStrCurrency( 13, 2, "" + bVlrBaseIcmsVenda ) + "| " + Funcoes.strDecimalToStrCurrency( 13, 2, "" + bVlrIcmsVenda ) );
				imp.say( imp.pRow(), 79, "|" );

				imp.say( imp.pRow() + 1, 0, imp.normal() );
				imp.say( imp.pRow(), 0, "|" );
				imp.say( imp.pRow(), 2, "ICMS A PAGAR" );
				imp.say( imp.pRow(), 22, "| " + StringFunctions.replicate( " ", 13 ) + "| " + StringFunctions.replicate( " ", 13 ) + "| " + Funcoes.strDecimalToStrCurrency( 13, 2, "" + bVlrIcmsPagar ) );
				imp.say( imp.pRow(), 79, "|" );

				imp.say( imp.pRow() + 1, 0, imp.normal() );
				imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "-", 77 ) + "+" );
			}
			imp.eject();
			imp.fechaGravacao();

			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela de preços!\n" + err.getMessage(), true, con, err );
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
			bVlrLiqVenda = null;
			bVlrBaseIcmsVenda = null;
			bVlrIcmsVenda = null;
			bVlrLiqCompra = null;
			bVlrBaseIcmsCompra = null;
			bVlrIcmsCompra = null;
			bVlrIcmsPagar = null;
			System.gc();
		}

		if ( bVisualizar==TYPE_PRINT.VIEW )
			imp.preview( this );
		else
			imp.print();
	}
}
