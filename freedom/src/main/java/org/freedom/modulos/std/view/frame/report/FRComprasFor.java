/**
 * @version 08/12/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FRComprasFor.java <BR>
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

package org.freedom.modulos.std.view.frame.report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.AplicativoPD;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

public class FRComprasFor extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtDescFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private ListaCampos lcFor = new ListaCampos( this );

	private Vector<String> vLabs1 = new Vector<String>();

	private Vector<String> vVals1 = new Vector<String>();

	private Vector<String> vLabs2 = new Vector<String>();

	private Vector<String> vVals2 = new Vector<String>();

	private JRadioGroup<?, ?> rgTipo = null;

	private JRadioGroup<?, ?> rgResumido = null;

	private String sCodProd = "CODPROD";

	public FRComprasFor() {

		setTitulo( "Compras por Fornecedor" );
		setAtribos( 50, 50, 340, 265 );

		lcFor.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.for.", ListaCampos.DB_PK, false ) );
		lcFor.add( new GuardaCampo( txtDescFor, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );
		txtCodFor.setTabelaExterna( lcFor, null );
		txtCodFor.setNomeCampo( "CodFor" );
		txtCodFor.setFK( true );
		lcFor.setReadOnly( true );
		lcFor.montaSql( false, "FORNECED", "CP" );

		vLabs1.addElement( "Texto" );
		vLabs1.addElement( "Gráfico" );
		vVals1.addElement( "T" );
		vVals1.addElement( "G" );

		rgTipo = new JRadioGroup<String, String>( 1, 2, vLabs1, vVals1 );
		rgTipo.setVlrString( "T" );

		vLabs2.addElement( "Resumido" );
		vLabs2.addElement( "Detalhado" );
		vVals2.addElement( "R" );
		vVals2.addElement( "D" );

		rgResumido = new JRadioGroup<String, String>( 1, 2, vLabs2, vVals2 );
		rgResumido.setVlrString( "D" );

		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );
		JLabelPad lbPeriodo = new JLabelPad( "Período:", SwingConstants.CENTER );
		lbPeriodo.setOpaque( true );

		adic( lbPeriodo, 7, 1, 80, 20 );
		adic( lbLinha, 5, 10, 300, 45 );

		adic( new JLabelPad( "De:" ), 10, 25, 30, 20 );
		adic( txtDataini, 40, 25, 97, 20 );
		adic( new JLabelPad( "Até:" ), 140, 25, 37, 20 );
		adic( txtDatafim, 180, 25, 100, 20 );
		adic( new JLabelPad( "Cód.for." ), 7, 60, 280, 20 );
		adic( txtCodFor, 7, 80, 70, 20 );
		adic( new JLabelPad( "Descrição do fornecedor" ), 80, 60, 280, 20 );
		adic( txtDescFor, 80, 80, 225, 20 );
		adic( rgTipo, 7, 105, 300, 30 );
		adic( rgResumido, 7, 145, 300, 30 );

		Calendar cPeriodo = Calendar.getInstance();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataini.setVlrDate( cPeriodo.getTime() );
	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();
		StringBuffer sWhere = new StringBuffer();
		StringBuilder sCab = new StringBuilder();
		int iparam = 1;

		ehRef();

		if ( txtCodFor.getText().trim().length() > 0 ) {
			sWhere.append( " AND C.CODEMPFR=? AND C.CODFILIALFR=? AND C.CODFOR = ? " );
			sCab.append( "Fornecedor: " + txtDescFor.getVlrString() );
		}

		if ( rgResumido.getVlrString().equals( "R" ) ) {
		
			sSQL.append( "SELECT C.CODFOR,F.RAZFOR,C.DTEMITCOMPRA,C.CODCOMPRA,C.DOCCOMPRA,C.DTEMITCOMPRA, ");
			sSQL.append( "C.DTENTCOMPRA, C.VLRICMSCOMPRA, C.VLRCOMPRA,C.VLRLIQCOMPRA " );
			//sSQL.append( ",P.DESCPROD,IT.QTDITCOMPRA,IT.PRECOITCOMPRA,IT.VLRLIQITCOMPRA,C.VLRLIQCOMPRA " );
			sSQL.append( "FROM CPCOMPRA C,CPFORNECED F " );
			sSQL.append( "WHERE " );
			sSQL.append( "F.CODEMP=C.CODEMPFR AND F.CODFILIAL=C.CODFILIALFR AND F.CODFOR = C.CODFOR " );
			sSQL.append( "AND C.DTENTCOMPRA BETWEEN ? AND ? " );
			sSQL.append( sWhere );
			sSQL.append( " AND C.FLAG IN " + AplicativoPD.carregaFiltro( con, org.freedom.library.swing.frame.Aplicativo.iCodEmp ) );
			sSQL.append( " AND C.CODEMP=? AND C.CODFILIAL=? " );
			sSQL.append( " ORDER BY C.CODFOR, C.CODCOMPRA, C.DTENTCOMPRA" );

		}
		else {

			sSQL.append( "SELECT C.CODFOR,F.RAZFOR,C.DTEMITCOMPRA,C.CODCOMPRA,C.DOCCOMPRA,C.DTEMITCOMPRA, C.DTENTCOMPRA, C.VLRICMSCOMPRA, C.VLRCOMPRA," );
			sSQL.append( "IT.CODPROD,P.REFPROD" );
			sSQL.append( ",P.DESCPROD,IT.QTDITCOMPRA,IT.PRECOITCOMPRA,IT.VLRLIQITCOMPRA,C.VLRLIQCOMPRA " );
			sSQL.append( "FROM CPCOMPRA C,CPITCOMPRA IT,EQPRODUTO P,CPFORNECED F " );
			sSQL.append( "WHERE P.CODEMP=IT.CODEMPPD AND P.CODFILIAL=IT.CODFILIALPD AND P.CODPROD=IT.CODPROD AND " );
			sSQL.append( "IT.CODEMP=C.CODEMP AND IT.CODFILIAL=C.CODFILIAL AND IT.CODCOMPRA=C.CODCOMPRA AND " );
			sSQL.append( "F.CODEMP=C.CODEMPFR AND F.CODFILIAL=C.CODFILIALFR AND F.CODFOR = C.CODFOR " );
			sSQL.append( "AND C.DTENTCOMPRA BETWEEN ? AND ? " );
			sSQL.append( sWhere );
			sSQL.append( " AND C.FLAG IN " + AplicativoPD.carregaFiltro( con, org.freedom.library.swing.frame.Aplicativo.iCodEmp ) );
			sSQL.append( " AND C.CODEMP=? AND C.CODFILIAL=? " );
			sSQL.append( " ORDER BY C.CODFOR, C.CODCOMPRA, IT.CODPROD, C.DTENTCOMPRA" );

			
		}
			
		try {

			ps = con.prepareStatement( sSQL.toString() );
			ps.setDate( iparam++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( iparam++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

			if ( txtCodFor.getText().trim().length() > 0 ) {

				ps.setInt( iparam++, lcFor.getCodEmp() );
				ps.setInt( iparam++, lcFor.getCodFilial() );
				ps.setInt( iparam++, txtCodFor.getVlrInteger() );
			}

			ps.setInt( iparam++, Aplicativo.iCodEmp );
			ps.setInt( iparam++, ListaCampos.getMasterFilial( "CPCOMPRA" ) );

			rs = ps.executeQuery();

		} catch ( SQLException e ) {

			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao consultar a tabela de compras", true, con, e );
		}

		if ( "T".equals( rgTipo.getVlrString() ) ) {

			imprimeTexto( rs, bVisualizar, sCab.toString() );
		}
		else {
			imprimiGrafico( rs, bVisualizar, sCab.toString() );
		}
	}

	public void imprimeTexto( final ResultSet rs, final TYPE_PRINT bVisualizar, final String sCab ) {

		float fVlr = 0;
		float fQtd = 0;
		float fVlrFor = 0;
		float fQtdFor = 0;
		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		int iCodFor = 0;
		int iCodForAnt = -1;
		String sLinhaFina = StringFunctions.replicate( "-", 133 );
		boolean termFor = false;
		String sTmp = "";

		try {

			imp.montaCab();
			imp.setTitulo( "Relatório de Compras por Fornecedor" );
			imp.addSubTitulo( "RELATORIO DE COMPRAS POR FORNECEDOR  -  PERIODO DE: " + txtDataini.getVlrString() + " Até: " + txtDatafim.getVlrString() );
			imp.limpaPags();

			while ( rs.next() ) {

				iCodFor = rs.getInt( "CodFor" );

				if ( imp.pRow() == linPag ) {

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "+" + sLinhaFina + "+" );
					imp.eject();
					imp.incPags();
				}

				if ( imp.pRow() == 0 ) {

					imp.impCab( 136, true );

					imp.say( 0, imp.comprimido() );
					imp.say( 0, "|" + sLinhaFina + "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "| Data       " );
					imp.say( 14, "| Pedido   " );
					imp.say( 26, "| Doc      " );
					imp.say( 38, "| Cod. Produto   " );
					imp.say( 55, "| Desc. Produto  " );
					imp.say( 89, "| Qtd.    " );
					imp.say( 100, "| Preco     " );
					imp.say( 113, "| Total     " );
					imp.say( 135, "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + sLinhaFina + "|" );
				}
				if ( iCodFor != iCodForAnt ) {

					if ( termFor ) {

						fQtd += fQtdFor;
						fVlr += fVlrFor;

						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|" + sLinhaFina + "|" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|" );
						imp.say( 1, "Sub-total fornecedor:" );
						imp.say( 30, "Quant. comprada -> " );
						imp.say( 50, Funcoes.copy( String.valueOf( fQtdFor ), 0, 6 ) );
						imp.say( 60, "Valor comprado -> " );
						imp.say( 78, Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( fVlrFor ) ) );
						imp.say( 135, "|" );
					}

					fQtdFor = 0;
					fVlrFor = 0;

					sTmp = "FORNECEDOR: " + rs.getInt( "CodFor" ) + " - " + rs.getString( "RazFor" ).trim();

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + sLinhaFina + "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" );
					imp.say( ( 133 - sTmp.length() ) / 2, sTmp );
					imp.say( 135, "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + sLinhaFina + "|" );

					termFor = true;
				}

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "| " + StringFunctions.sqlDateToStrDate( rs.getDate( "DtEmitCompra" ) ) );
				imp.say( 14, "| " + Funcoes.copy( rs.getString( "CodCompra" ), 0, 8 ) );
				imp.say( 26, "| " + Funcoes.copy( rs.getString( "DocCompra" ), 0, 8 ) );
				imp.say( 38, "| " + Funcoes.copy( rs.getString( sCodProd ), 0, 13 ) );
				imp.say( 55, "| " + Funcoes.copy( rs.getString( "DescProd" ), 0, 30 ) );
				imp.say( 89, "| " + Funcoes.strDecimalToStrCurrency( 7, 1, rs.getString( "QtdItCompra" ) ) );
				imp.say( 100, "| " + Funcoes.strDecimalToStrCurrency( 9, 2, rs.getString( "PrecoItCompra" ) ) );
				imp.say( 113, "| " + Funcoes.strDecimalToStrCurrency( 9, 2, rs.getString( "VlrLiqItCompra" ) ) );
				imp.say( 135, "|" );

				fQtdFor += rs.getFloat( "QtdItCompra" );
				fVlrFor += rs.getFloat( "VlrLiqItCompra" );

				iCodForAnt = iCodFor;
			}

			fQtd += fQtdFor;
			fVlr += fVlrFor;

			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "|" + sLinhaFina + "|" );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "|" );
			imp.say( 1, "Sub-total fornecedor:" );
			imp.say( 30, "Quant. comprada -> " );
			imp.say( 50, Funcoes.copy( String.valueOf( fQtdFor ), 0, 6 ) );
			imp.say( 60, "Valor comprado -> " );
			imp.say( 78, Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( fVlrFor ) ) );
			imp.say( 135, "|" );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "|" + sLinhaFina + "|" );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "|" );
			imp.say( 1, "Total compras:" );
			imp.say( 30, "Quant. comprada -> " );
			imp.say( 50, Funcoes.copy( String.valueOf( fQtd ), 0, 6 ) );
			imp.say( 60, "Valor comprado -> " );
			imp.say( 78, Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( fVlr ) ) );
			imp.say( 135, "|" );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "+" + sLinhaFina + "+" );

			imp.eject();
			imp.fechaGravacao();

			con.commit();

		} catch ( SQLException err ) {

		}
		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
	}

	private void imprimiGrafico( final ResultSet rs, final TYPE_PRINT bVisualizar, final String sCab ) {

		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();
		String relatorio = "";

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "CPCOMPRA" ) );
		hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
		hParam.put( "FILTROS", sCab );

		if ( rgResumido.getVlrString().equals( "R" ) ) {
			relatorio = "FRComprasForRes.jasper";
		}
		else if ( rgResumido.getVlrString().equals( "D" ) ) {
			relatorio = "FRComprasFor.jasper";
		}

		dlGr = new FPrinterJob( "relatorios/" + relatorio, "Relatório de Compras por fornecedor", sCab, rs, hParam, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório Compras por fornecedor!" + err.getMessage(), true, con, err );
			}
		}
	}

	private void ehRef() {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = con.prepareStatement( "SELECT USAREFPROD FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

			rs = ps.executeQuery();

			if ( rs.next() ) {

				sCodProd = "S".equals( rs.getString( "UsaRefProd" ) ) ? "REFPROD" : "CODPROD";
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage() );
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcFor.setConexao( cn );
	}
}
