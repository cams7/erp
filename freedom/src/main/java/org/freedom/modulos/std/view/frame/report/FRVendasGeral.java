/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FRVendasGeral.java <BR>
 * 
 *                        Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                        modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                        na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                        Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                        sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                        Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                        Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                        de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                        Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.std.view.frame.report;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.AplicativoPD;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCliente;

public class FRVendasGeral extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JCheckBoxPad cbVendaCanc = new JCheckBoxPad( "Mostrar Canceladas", "S", "N" );

	private JRadioGroup<?, ?> rgFaturados = null;

	private JRadioGroup<?, ?> rgFinanceiro = null;
	
	private Vector<String> vLabsFat = new Vector<String>();

	private Vector<String> vValsFat = new Vector<String>();

	private Vector<String> vLabsFin = new Vector<String>();

	private Vector<String> vValsFin = new Vector<String>();

	private JRadioGroup<?, ?> rgEmitidos = null;
	
	private Vector<String> vLabsEmit = new Vector<String>();

	private Vector<String> vValsEmit = new Vector<String>();
	
	private JRadioGroup<?, ?> rgTipo = null;

	private JRadioGroup<?, ?> rgData = null;

	private Vector<String> vLabs1 = new Vector<String>();

	private Vector<String> vVals1 = new Vector<String>();

	private Vector<String> vLabsData = new Vector<String>();

	private Vector<String> vValsData = new Vector<String>();

	private ListaCampos lcVend = new ListaCampos( this );

	private ListaCampos lcCli = new ListaCampos( this, "CL" );

	public FRVendasGeral() {

		setTitulo( "Vendas em Geral" );
		setAtribos( 80, 80, 325, 460 );

		txtDataini.setVlrDate( new Date() );
		txtDatafim.setVlrDate( new Date() );
		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );

		vLabsFat.addElement( "Faturado" );
		vLabsFat.addElement( "Não Faturado" );
		vLabsFat.addElement( "Ambos" );
		vValsFat.addElement( "S" );
		vValsFat.addElement( "N" );
		vValsFat.addElement( "A" );
		rgFaturados = new JRadioGroup<String, String>( 3, 1, vLabsFat, vValsFat );
		rgFaturados.setVlrString( "S" );

		vLabsFin.addElement( "Financeiro" );
		vLabsFin.addElement( "Não Finaceiro" );
		vLabsFin.addElement( "Ambos" );
		vValsFin.addElement( "S" );
		vValsFin.addElement( "N" );
		vValsFin.addElement( "A" );
		rgFinanceiro = new JRadioGroup<String, String>( 3, 1, vLabsFin, vValsFin );
		rgFinanceiro.setVlrString( "S" );
		
		vLabsEmit.addElement( "Emitidos" );
		vLabsEmit.addElement( "Não emitidos" );
		vLabsEmit.addElement( "Ambos" );
		vValsEmit.addElement( "S" );
		vValsEmit.addElement( "N" );
		vValsEmit.addElement( "A" );
		rgEmitidos = new JRadioGroup<String, String>( 3, 1, vLabsEmit, vValsEmit );
		rgEmitidos.setVlrString( "A" );

		vLabs1.addElement( "Texto" );
		vLabs1.addElement( "Grafico" );
		vVals1.addElement( "T" );
		vVals1.addElement( "G" );

		rgTipo = new JRadioGroup<String, String>( 1, 2, vLabs1, vVals1 );
		rgTipo.setVlrString( "T" );

		vLabsData.addElement( "Emissão" );
		vLabsData.addElement( "Saída" );
		vValsData.addElement( "E" );
		vValsData.addElement( "S" );

		rgData = new JRadioGroup<String, String>( 1, 2, vLabsData, vValsData );
		rgData.setVlrString( "E" );

		lcVend.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.comiss.", ListaCampos.DB_PK, false ) );
		lcVend.add( new GuardaCampo( txtDescVend, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, false ) );
		lcVend.montaSql( false, "VENDEDOR", "VD" );
		lcVend.setQueryCommit( false );
		lcVend.setReadOnly( true );
		txtCodVend.setNomeCampo( "CodVend" );
		txtCodVend.setFK( true );
		txtCodVend.setTabelaExterna( lcVend, FRVendasGeral.class.getCanonicalName() );

		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo( txtNomeCli, "NomeCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		txtCodCli.setTabelaExterna( lcCli, FCliente.class.getCanonicalName() );
		txtCodCli.setNomeCampo( "CodCli" );
		txtCodCli.setFK( true );
		lcCli.setReadOnly( true );
		lcCli.montaSql( false, "CLIENTE", "VD" );

		adic( new JLabelPad( "Periodo:" ), 7, 5, 100, 20 );
		adic( lbLinha, 60, 15, 210, 2 );

		adic( new JLabelPad( "De:" ), 7, 30, 30, 20 );
		adic( txtDataini, 32, 30, 97, 20 );

		adic( new JLabelPad( "Até:" ), 140, 30, 30, 20 );
		adic( txtDatafim, 170, 30, 100, 20 );

		adic( rgData, 7, 65, 265, 30 );

		adic( new JLabelPad( "Cód.comiss." ), 7, 100, 210, 20 );
		adic( txtCodVend, 7, 120, 70, 20 );

		adic( new JLabelPad( "Nome do comissionado" ), 80, 100, 210, 20 );
		adic( txtDescVend, 80, 120, 190, 20 );


		adic( txtCodCli, 7, 160, 70, 20, "Cód.Cli" );
		adic( txtNomeCli, 80, 160, 190, 20, "Nome do cliente" );

		adic( rgTipo, 7, 190, 265, 30 );
		adic( rgFaturados, 7, 230, 120, 70 );
		adic( rgFinanceiro, 153, 230, 120, 70 );
		
		adic( rgEmitidos, 7, 310, 120, 70 );
		
		adic( cbVendaCanc, 145, 310, 180, 20 );
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcVend.setConexao( con );
		lcCli.setConexao( con );
	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();
		String sWhere = "";
		String sWhere1 = "";
		String sWhere2 = "";
		String sWhere3 = "";
		String sWhere4 = "";
		String sCab = "";

		if ( txtCodVend.getText().trim().length() > 0 ) {
			sWhere += " AND V.CODVEND = " + txtCodVend.getText().trim();
			sCab = "REPR.: " + txtCodVend.getVlrString() + " - " + txtDescVend.getText().trim();
			sWhere += " AND V.CODEMPVD=" + Aplicativo.iCodEmp + " AND V.CODFILIALVD=" + lcVend.getCodFilial();
		}
		if ( txtCodCli.getVlrString().trim().length() > 0 ) {
			sWhere += "AND C.CODCLI = " + txtCodCli.getVlrString().trim();
			sCab += "CLIENTE: " + txtNomeCli.getVlrString().trim();
		}

		if ( rgFaturados.getVlrString().equals( "S" ) ) {
			sWhere1 = " AND TM.FISCALTIPOMOV='S' ";
			sCab += " - SO FATURADO";
		}
		else if ( rgFaturados.getVlrString().equals( "N" ) ) {
			sWhere1 = " AND TM.FISCALTIPOMOV='N' ";
			sCab += " - NAO FATURADO";
		}
		else if ( rgFaturados.getVlrString().equals( "A" ) ) {
			sWhere1 = " AND TM.FISCALTIPOMOV IN ('S','N') ";
		}

		if ( rgFinanceiro.getVlrString().equals( "S" ) ) {
			sWhere2 = " AND TM.SOMAVDTIPOMOV='S' ";
			sCab += sCab.length() > 0 ? " - SO FINANCEIRO" : "SO FINANCEIRO";
		}
		else if ( rgFinanceiro.getVlrString().equals( "N" ) ) {
			sWhere2 = " AND TM.SOMAVDTIPOMOV='N' ";
			sCab += sCab.length() > 0 ? " - NAO FINANCEIRO" : "NAO FINANCEIRO";
		}
		else if ( rgFinanceiro.getVlrString().equals( "A" ) ) {
			sWhere2 = " AND TM.SOMAVDTIPOMOV IN ('S','N') ";
		}
		
		if ( rgEmitidos.getVlrString().equals( "S" ) ) {
			sWhere4 = " AND V.STATUSVENDA IN ('V2','V3','P3') ";
			sCab += sCab.length() > 0 ? " - SO EMITIDOS" : "SO EMITIDOS";
		}
		else if ( rgEmitidos.getVlrString().equals( "N" ) ) {
			sWhere4 = " AND V.STATUSVENDA NOT IN ('V2','V3','P3') ";
			sCab += sCab.length() > 0 ? " - NAO EMITIDOS" : "NAO EMITIDOS";
		}

		if ( cbVendaCanc.getVlrString().equals( "N" ) ) {
			sWhere3 = " AND NOT SUBSTR(V.STATUSVENDA,1,1)='C' ";
		}

		sSQL.append( "SELECT V.DTSAIDAVENDA,V.CODVENDA,V.SERIE,V.STATUSVENDA,V.DOCVENDA," );
		sSQL.append( "V.DTEMITVENDA,V.VLRPRODVENDA,V.VLRLIQVENDA,V.CODPLANOPAG,P.DESCPLANOPAG," );
		sSQL.append( "V.VLRCOMISVENDA,V.VLRDESCVENDA,V.VLRDESCITVENDA,V.VLRFRETEVENDA,V.CODCLI,C.RAZCLI " );
		sSQL.append( "FROM VDVENDA V,VDCLIENTE C,FNPLANOPAG P,EQTIPOMOV TM " );
		sSQL.append( "WHERE V.CODEMP=? AND V.CODFILIAL=? " );
		sSQL.append( "AND C.CODEMP=V.CODEMPCL AND C.CODFILIAL=V.CODFILIALCL AND C.CODCLI=V.CODCLI " );
		sSQL.append( "AND P.CODEMP=V.CODEMPPG AND P.CODFILIAL=V.CODFILIALPG AND P.CODPLANOPAG=V.CODPLANOPAG " );
		sSQL.append( "AND TM.CODEMP=V.CODEMPTM AND TM.CODFILIAL=V.CODFILIALTM AND TM.CODTIPOMOV=V.CODTIPOMOV " );
		sSQL.append( "AND TM.TIPOMOV IN ('VD','VE','PV','VT','SE') " );
		
		sSQL.append( sWhere );
		sSQL.append( sWhere1 );
		sSQL.append( sWhere2 );
		sSQL.append( sWhere4 );
		sSQL.append( sWhere3 );

		if ( "E".equals( rgData.getVlrString() ) ) {
			sSQL.append( "AND V.DTEMITVENDA BETWEEN ? AND ? " );
		}
		else {
			sSQL.append( "AND V.DTSAIDAVENDA BETWEEN ? AND ? " );
		}

		sSQL.append( "AND V.FLAG IN " + AplicativoPD.carregaFiltro( con, org.freedom.library.swing.frame.Aplicativo.iCodEmp ) );
		sSQL.append( "ORDER BY " );

		if ( "E".equals( rgData.getVlrString() ) ) {
			sSQL.append( "V.DTEMITVENDA" );
		}
		else {
			sSQL.append( "V.DTSAIDAVENDA" );
		}

		sSQL.append( ", V.DOCVENDA " );

		try {
			System.out.print( "SQL:" + sSQL.toString() );
			
			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDVENDA" ) );
			ps.setDate( 3, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( 4, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			rs = ps.executeQuery();

		} catch ( Exception e ) {

			Funcoes.mensagemErro( this, "Erro ao buscar dados da venda !\n" + e.getMessage() );
			e.printStackTrace();
		}

		if ( "T".equals( rgTipo.getVlrString() ) ) {

			imprimeTexto( rs, bVisualizar, sCab );
		}
		else {
			imprimeGrafico( rs, bVisualizar, sCab );
		}
	}

	public void imprimeTexto( final ResultSet rs, final TYPE_PRINT bVisualizar, final String sCab ) {

		PreparedStatement ps = null;
		StringBuffer sSQL = new StringBuffer();
		ImprimeOS imp = null;
		int linPag = 0;
		BigDecimal bTotalVal = null;
		BigDecimal bTotalDesc = null;
		BigDecimal bTotalLiq = null;
		BigDecimal bTotalAcres = null;
		String sDatasaidavenda = null;

		try {

			bTotalVal = new BigDecimal( "0" );
			bTotalDesc = new BigDecimal( "0" );
			bTotalLiq = new BigDecimal( "0" );
			bTotalAcres = new BigDecimal( "0" );

			imp = new ImprimeOS( "", con );
			linPag = imp.verifLinPag() - 1;
			imp.verifLinPag();
			imp.montaCab();
			imp.setTitulo( "Relatório de Vendas Geral" );
			imp.addSubTitulo( "RELATORIO DE VENDAS GERAL   -   PERIODO DE :" + txtDataini.getVlrString() + " Até: " + txtDatafim.getVlrString() );
			imp.limpaPags();

			if ( sCab.length() > 0 ) {

				imp.addSubTitulo( sCab );
			}
			while ( rs.next() ) {

				if ( imp.pRow() >= ( linPag - 1 ) ) {
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
					imp.incPags();
					imp.eject();
				}
				if ( imp.pRow() == 0 ) {
					imp.impCab( 136, true );
					imp.say( imp.pRow(), 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|Dt.Saida" );
					imp.say( imp.pRow(), 12, "| NF./Ped." );
					imp.say( imp.pRow(), 27, "| Cod.Cli." );
					imp.say( imp.pRow(), 36, "| Cliente" );
					imp.say( imp.pRow(), 80, "|   Valor" );
					imp.say( imp.pRow(), 93, "| Desconto" );
					imp.say( imp.pRow(), 106, "|  Liquido" );
					imp.say( imp.pRow(), 119, "| F.Pagto." );
					imp.say( imp.pRow(), 135, "|" );
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
				}

				imp.say( imp.pRow() + 1, 0, imp.comprimido() );
				if ( !rs.getString( "dtsaidavenda" ).equals( sDatasaidavenda ) )
					imp.say( imp.pRow(), 0, "|" + StringFunctions.sqlDateToStrDate( rs.getDate( "dtsaidavenda" ) ) );
				else
					imp.say( imp.pRow(), 0, "|" );

				if ( Funcoes.copy( rs.getString( "statusvenda" ), 0, 1 ) == "P" )
					imp.say( imp.pRow(), 12, "|" + Funcoes.copy( rs.getString( "codvenda" ), 0, 13 ) );
				else {
					imp.say( imp.pRow(), 12, "| " + Funcoes.copy( rs.getString( "docvenda" ), 0, 13 ) );
					imp.say( imp.pRow(), 26, "| " + Funcoes.copy( rs.getString( "CodCli" ), 0, 8 ) );
					imp.say( imp.pRow(), 36, "| " + Funcoes.copy( rs.getString( "razcli" ), 0, 40 ) );
					imp.say( imp.pRow(), 80, "| " + Funcoes.strDecimalToStrCurrency( 10, 2, rs.getString( "vlrprodvenda" ) ) );
					imp.say( imp.pRow(), 93, "| " + ( rs.getDouble( "VlrDescVenda" ) > 0 ? Funcoes.strDecimalToStrCurrency( 10, 2, rs.getString( "vlrdescvenda" ) ) : Funcoes.strDecimalToStrCurrency( 10, 2, rs.getString( "vlrdescitvenda" ) ) ) );
					imp.say( imp.pRow(), 106, "| " + Funcoes.strDecimalToStrCurrency( 10, 2, rs.getString( "vlrliqvenda" ) ) );
					imp.say( imp.pRow(), 119, "| " + Funcoes.copy( rs.getString( "descplanopag" ), 0, 13 ) );
					imp.say( imp.pRow(), 135, "|" );
				}

				if ( rs.getString( "VlrProdVenda" ) != null ) {

					bTotalVal = bTotalVal.add( new BigDecimal( rs.getString( "VlrProdVenda" ) ) );
				}
				if ( rs.getString( "VlrDescvenda" ) != null ) {

					bTotalDesc = bTotalDesc.add( new BigDecimal( rs.getDouble( "VlrDescVenda" ) > 0 ? rs.getDouble( "VlrDescVenda" ) : rs.getDouble( "VlrDescItVenda" ) ) );
				}
				if ( rs.getString( "VlrLiqVenda" ) != null ) {

					bTotalLiq = bTotalLiq.add( new BigDecimal( rs.getString( "VlrLiqVenda" ) ) );
				}
				if ( rs.getString( "VlrFreteVenda" ) != null ) {

					bTotalAcres = bTotalAcres.add( new BigDecimal( rs.getString( "VlrFreteVenda" ) ) );
				}

				sDatasaidavenda = rs.getString( "dtsaidavenda" );
			}

			imp.say( imp.pRow() + 1, 0, imp.comprimido() );
			imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "=", 133 ) + "+" );
			imp.say( imp.pRow() + 1, 0, imp.comprimido() );
			imp.say( imp.pRow(), 0, "|" );
			imp.say( imp.pRow(), 100, "Total Geral:" );
			imp.say( imp.pRow(), 115, Funcoes.strDecimalToStrCurrency( 10, 2, "" + bTotalVal ) );
			imp.say( imp.pRow(), 135, "|" );
			imp.say( imp.pRow() + 1, 0, imp.comprimido() );
			imp.say( imp.pRow(), 0, "|" );
			imp.say( imp.pRow(), 100, "-Desconto:" );
			imp.say( imp.pRow(), 115, Funcoes.strDecimalToStrCurrency( 10, 2, "" + bTotalDesc ) );
			imp.say( imp.pRow(), 135, "|" );
			imp.say( imp.pRow() + 1, 0, imp.comprimido() );
			imp.say( imp.pRow(), 0, "|" );
			imp.say( imp.pRow(), 100, "+Acréscimo:" );
			imp.say( imp.pRow(), 115, Funcoes.strDecimalToStrCurrency( 10, 2, "" + bTotalAcres ) );
			imp.say( imp.pRow(), 135, "|" );
			imp.say( imp.pRow() + 1, 0, imp.comprimido() );
			imp.say( imp.pRow(), 0, "|" );
			imp.say( imp.pRow(), 100, "=Valor líquido:" );
			imp.say( imp.pRow(), 115, Funcoes.strDecimalToStrCurrency( 10, 2, "" + bTotalLiq ) );
			imp.say( imp.pRow(), 135, "|" );
			imp.say( imp.pRow() + 1, 0, imp.comprimido() );
			imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "=", 133 ) + "+" );

			imp.eject();
			imp.fechaGravacao();

			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela de preços!\n" + err.getMessage(), true, con, err );
		}

		if ( bVisualizar==TYPE_PRINT.VIEW )

			imp.preview( this );

		else {

			imp.print();
		}
	}

	public void imprimeGrafico( final ResultSet rs, final TYPE_PRINT bVisualizar, final String sCab ) {

		HashMap<String, Object> hParam = new HashMap<String, Object>();

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "VDVENDA" ) );
		hParam.put( "FILTROS", sCab );

		FPrinterJob dlGr = new FPrinterJob( "relatorios/VendasGeral.jasper", "Vendas Geral", null, rs, hParam, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {

			dlGr.setVisible( true );

		}
		else {
			try {

				JasperPrintManager.printReport( dlGr.getRelatorio(), true );

			} catch ( Exception err ) {

				Funcoes.mensagemErro( this, "Erro na impressão do relatório de Últimas compras/produto!\n" + err.getMessage(), true, con, err );
			}
		}
	}
}
