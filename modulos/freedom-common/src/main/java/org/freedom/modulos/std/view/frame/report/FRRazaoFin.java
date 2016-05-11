/**
 * @version 25/06/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FRRazaoFin.java <BR>
 * 
 *                     Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                     modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                     na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                     Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                     sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                     Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                     Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                     de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                     Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.std.view.frame.report;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Vector;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.fnc.library.swing.component.JTextFieldPlan;

public class FRRazaoFin extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldFK txtDescPlan = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPlan txtCodPlan = new JTextFieldPlan( JTextFieldPad.TP_STRING, 13, 0 );

	private ListaCampos lcPlan = new ListaCampos( this );

	public FRRazaoFin() {

		super( false );
		setTitulo( "Relatório razão financeiro" );
		setAtribos( 80, 80, 330, 180 );

		lcPlan.add( new GuardaCampo( txtCodPlan, "CodPlan", "Cód.plan", ListaCampos.DB_PK, false ) );
		lcPlan.add( new GuardaCampo( txtDescPlan, "DescPlan", "Descrição do planejamento", ListaCampos.DB_SI, false ) );
		lcPlan.montaSql( false, "PLANEJAMENTO", "FN" );
		lcPlan.setWhereAdic( "NIVELPLAN=6" );
		lcPlan.setReadOnly( true );
		txtCodPlan.setTabelaExterna( lcPlan, null );
		txtCodPlan.setFK( true );
		txtCodPlan.setNomeCampo( "CodPlan" );

		adic( new JLabelPad( "Periodo:" ), 7, 5, 125, 20 );
		adic( new JLabelPad( "De:" ), 7, 25, 30, 20 );
		adic( txtDataini, 32, 25, 125, 20 );
		adic( new JLabelPad( "Até:" ), 160, 25, 22, 20 );
		adic( txtDatafim, 185, 25, 125, 20 );
		adic( new JLabelPad( "Nº planejamento" ), 7, 50, 250, 20 );
		adic( txtCodPlan, 7, 70, 100, 20 );
		adic( new JLabelPad( "Descrição do planejamento" ), 110, 50, 240, 20 );
		adic( txtDescPlan, 110, 70, 200, 20 );

		Calendar cPeriodo = Calendar.getInstance();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataini.setVlrDate( cPeriodo.getTime() );
		txtCodPlan.setRequerido( true );
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcPlan.setConexao( cn );
	}

	private double buscaSaldo() {

		double dRet = 0.00;

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			int iParam = 1;

			ps = con.prepareStatement( "SELECT SL.CODPLAN, SALDOSL FROM FNSALDOLANCA SL " + "WHERE SL.CODEMP=? AND SL.CODFILIAL=? AND SL.CODEMPPN=? AND SL.CODFILIALPN=? AND " + "SL.CODPLAN=? AND SL.DATASL=(SELECT MAX(SL2.DATASL) FROM FNSALDOLANCA SL2 "
					+ "WHERE SL2.CODPLAN=SL.CODPLAN AND SL2.DATASL<?)" );
			ps.setInt( iParam++, Aplicativo.iCodEmp );
			ps.setInt( iParam++, Aplicativo.iCodFilial );
			ps.setInt( iParam++, Aplicativo.iCodEmp );
			ps.setInt( iParam++, lcPlan.getCodFilial() );
			ps.setString( iParam++, txtCodPlan.getVlrString() );
			ps.setDate( iParam++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			rs = ps.executeQuery();

			if ( rs.next() ) {
				dRet = Funcoes.strCurrencyToDouble( rs.getString( "SALDOSL" ) == null ? "0,00" : rs.getString( "SALDOSL" ) );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro ao buscar saldo!\n" + e.getMessage(), true, con, e );
			e.printStackTrace();
		}

		return dRet;
	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;
		String sCodPlan = txtCodPlan.getVlrString().trim();
		String sConta = "";
		String sSaldoAnt = "";
		BigDecimal bVlrSubLanca = new BigDecimal( 0 );
		BigDecimal bTotal = new BigDecimal( 0 );
		BigDecimal bTotDesp = new BigDecimal( 0 );
		BigDecimal bTotRec = new BigDecimal( 0 );
		ImprimeOS imp = null;
		int linPag = 0;
		Vector<String> hist = null;

		if ( sCodPlan.equals( "" ) ) {
			Funcoes.mensagemInforma( this, "Informe um código de conta !" );
			return;
		}

		try {

			imp = new ImprimeOS( "", con );
			linPag = imp.verifLinPag() - 1;
			imp.montaCab();
			imp.setTitulo( "Razão financeiro" );
			imp.addSubTitulo( "RELATORIO RAZÃO FINANCEIRO" );
			if ( ! ( sCodPlan.trim().equals( "" ) ) ) {
				sConta = "CONTA: " + sCodPlan + " - " + txtDescPlan.getVlrString();
				imp.addSubTitulo( sConta );
			}
			imp.limpaPags();

			sSaldoAnt = Funcoes.strDecimalToStrCurrency( 13, 2, String.valueOf( buscaSaldo() ) );

			sSQL = "SELECT SL.DATASUBLANCA,SL.CODLANCA,SL.HISTSUBLANCA,SL.VLRSUBLANCA " + "FROM FNSUBLANCA SL " + "WHERE SL.CODEMP=? AND SL.CODFILIAL=? AND SL.CODPLAN=? AND " + "SL.CODEMPPN=? AND CODFILIALPN=? AND SL.DATASUBLANCA BETWEEN ? AND ? " + "ORDER BY SL.DATASUBLANCA";

			ps = con.prepareStatement( sSQL );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			ps.setString( 3, txtCodPlan.getVlrString() );
			ps.setInt( 4, Aplicativo.iCodEmp );
			ps.setInt( 5, lcPlan.getCodFilial() );
			ps.setDate( 6, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( 7, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			rs = ps.executeQuery();

			while ( rs.next() ) {
				if ( imp.pRow() >= linPag ) {
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
					imp.eject();
					imp.incPags();
				}
				if ( imp.pRow() == 0 ) {
					imp.impCab( 136, true );
					imp.pulaLinha( 0, imp.comprimido() );
					imp.say( 0, "|" + StringFunctions.replicate( "=", 133 ) + "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" );
					imp.say( 105, "SALDO ANTERIOR" );
					imp.say( 121, sSaldoAnt );
					imp.say( 135, "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + StringFunctions.replicate( "=", 133 ) + "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" );
					imp.say( 6, "Data" );
					imp.say( 14, "|" );
					imp.say( 15, "Lançamento" );
					imp.say( 25, "|" );
					imp.say( 27, "Histórico" );
					imp.say( 90, "|" );
					imp.say( 94, "Receita" );
					imp.say( 105, "|" );
					imp.say( 109, "Despesa" );
					imp.say( 120, "|" );
					imp.say( 126, "Saldo" );
					imp.say( 135, "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
				}

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" );
				imp.say( 3, Funcoes.dateToStrDate( rs.getDate( "DATASUBLANCA" ) ) );
				imp.say( 14, "|" );
				imp.say( 16, rs.getString( "CODLANCA" ) );
				imp.say( 25, "|" );

				hist = Funcoes.strToVectorSilabas( rs.getString( "HISTSUBLANCA" ), 62 );
				if ( rs.getString( "HISTSUBLANCA" ) != null ) {
					imp.say( 27, hist.get( 0 ) );
				}
				bVlrSubLanca = rs.getBigDecimal( "VLRSUBLANCA" );
				bTotal = bTotal.add( bVlrSubLanca );

				if ( bVlrSubLanca.doubleValue() < 0 ) {
					imp.say( 90, "|" );
					imp.say( 92, Funcoes.strDecimalToStrCurrency( 12, 2, String.valueOf( bVlrSubLanca.doubleValue() * -1 ) ) );
					imp.say( 105, "|" );
					imp.say( 120, "|" );
					bTotRec = bTotRec.add( new BigDecimal( bVlrSubLanca.doubleValue() * -1 ) );
				}
				else {
					imp.say( 90, "|" );
					imp.say( 105, "|" );
					imp.say( 107, Funcoes.strDecimalToStrCurrency( 12, 2, String.valueOf( bVlrSubLanca.doubleValue() ) ) );
					imp.say( 120, "|" );
					bTotDesp = bTotDesp.add( rs.getBigDecimal( "VLRSUBLANCA" ) );
				}

				imp.say( 122, Funcoes.strDecimalToStrCurrency( 12, 2, String.valueOf( bTotal ) ) );
				imp.say( 135, "|" );

				if ( hist.size() > 1 ) {
					for ( int i = 1; i < hist.size(); i++ ) {
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|" );
						imp.say( 14, "|" );
						imp.say( 25, "|" );
						imp.say( 27, hist.get( i ) );
						imp.say( 90, "|" );
						imp.say( 105, "|" );
						imp.say( 120, "|" );
						imp.say( 135, "|" );
					}
				}
			}

			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "|" + StringFunctions.replicate( "=", 133 ) + "|" );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "|" );
			imp.say( 90, "|" );
			imp.say( 91, Funcoes.strDecimalToStrCurrency( 12, 2, String.valueOf( bTotRec ) ) );
			imp.say( 105, "|" );
			imp.say( 106, Funcoes.strDecimalToStrCurrency( 12, 2, String.valueOf( bTotDesp ) ) );
			imp.say( 120, "|" );
			imp.say( 122, Funcoes.strDecimalToStrCurrency( 12, 2, String.valueOf( bTotal ) ) );
			imp.say( 135, "|" );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "+" + StringFunctions.replicate( "=", 133 ) + "+" );

			imp.eject();

			imp.fechaGravacao();

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro na consulta de sublançamentos!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			System.gc();
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
	}
}
