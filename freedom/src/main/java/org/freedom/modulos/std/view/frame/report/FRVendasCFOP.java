/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./ Alex Rodrigues <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FRVendasCFOP.java <BR>
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
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

public class FRVendasCFOP extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodCFOP = new JTextFieldPad( JTextFieldPad.TP_STRING, 5, 0 );

	private JTextFieldFK txtDescCFOP = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodTipoMov = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoMov = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JCheckBoxPad cbVendaCanc = new JCheckBoxPad( "Mostrar canceladas", "S", "N" );

	private JCheckBoxPad cbResumo = new JCheckBoxPad( "Imprimir resumo", "S", "N" );

	private JRadioGroup<?, ?> rgFaturados = null;

	private JRadioGroup<?, ?> rgFinanceiro = null;

	private Vector<String> vLabsFat = new Vector<String>();

	private Vector<String> vValsFat = new Vector<String>();

	private Vector<String> vLabsFin = new Vector<String>();

	private Vector<String> vValsFin = new Vector<String>();

	private JRadioGroup<?, ?> rgTipo = null;

	private Vector<String> vLabs1 = new Vector<String>();

	private Vector<String> vVals1 = new Vector<String>();

	private ListaCampos lcCFOP = new ListaCampos( this, "NT" );

	private ListaCampos lcMov = new ListaCampos( this, "TM" );
	
	private ListaCampos lcVend = new ListaCampos(this);

	private BigDecimal bTotalCFOP;

	private BigDecimal bTotalGeral;

	private String sLinhaFina = StringFunctions.replicate( "-", 133 );

	private String sLinhaLarga = StringFunctions.replicate( "=", 133 );

	public FRVendasCFOP()  {
		super( false );

		setTitulo( "Vendas em Geral" );
		setAtribos( 80, 80, 293, 390 );

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

		vLabs1.addElement( "Texto" );
		vLabs1.addElement( "Grafico" );
		vVals1.addElement( "T" );
		vVals1.addElement( "G" );

		rgTipo = new JRadioGroup<String, String>( 1, 2, vLabs1, vVals1 );
		rgTipo.setVlrString( "G" );

		lcCFOP.add( new GuardaCampo( txtCodCFOP, "CodNat", "CFOP", ListaCampos.DB_PK, false ) );
		lcCFOP.add( new GuardaCampo( txtDescCFOP, "DescNat", "Descrição da CFOP", ListaCampos.DB_SI, false ) );
		lcCFOP.montaSql( false, "NATOPER", "LF" );
		lcCFOP.setQueryCommit( false );
		lcCFOP.setReadOnly( true );
		txtCodCFOP.setNomeCampo( "CodNat" );
		txtCodCFOP.setFK( true );
		txtCodCFOP.setTabelaExterna( lcCFOP, null );

		lcMov.add( new GuardaCampo( txtCodTipoMov, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false ) );
		lcMov.add( new GuardaCampo( txtDescTipoMov, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false ) );
		lcMov.montaSql( false, "TIPOMOV", "EQ" );
		lcMov.setQueryCommit( false );
		lcMov.setReadOnly( true );
		txtCodTipoMov.setNomeCampo( "CodTipoMov" );
		txtCodTipoMov.setFK( true );
		txtCodTipoMov.setTabelaExterna( lcMov, null );
		
		lcVend.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.Comiss.", ListaCampos.DB_PK, false ) );
		lcVend.add( new GuardaCampo( txtNomeVend, "NomeVend", "Descrição do Comissionado", ListaCampos.DB_SI, false ) );
		lcVend.montaSql( false, "VENDEDOR", "VD" );
		lcVend.setQueryCommit( false );
		lcVend.setReadOnly( true );
		txtCodVend.setNomeCampo( "CodVend" );
		txtCodVend.setFK( true );
		txtCodVend.setTabelaExterna( lcVend, null );
		
		txtDataini.setVlrDate( new Date() );
		txtDatafim.setVlrDate( new Date() );
		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );

		adic( new JLabelPad( "Periodo:" ), 7, 5, 100, 20 );
		adic( lbLinha, 60, 15, 210, 2 );
		adic( new JLabelPad( "De:" ), 7, 30, 30, 20 );
		adic( txtDataini, 32, 30, 97, 20 );
		adic( new JLabelPad( "Até:" ), 140, 30, 30, 20 );
		adic( txtDatafim, 170, 30, 100, 20 );
		adic( new JLabelPad( "Cód.CFOP" ), 7, 60, 210, 20 );
		adic( txtCodCFOP, 7, 80, 70, 20 );
		adic( new JLabelPad( "Descrição da CFOP" ), 80, 60, 210, 20 );
		adic( txtDescCFOP, 80, 80, 190, 20 );
		adic( new JLabelPad( "Cód.tp.mov." ), 7, 100, 210, 20 );
		adic( txtCodTipoMov, 7, 120, 70, 20 );
		adic( new JLabelPad( "Descrição do tipo de movimento" ), 80, 100, 210, 20 );
		adic( txtDescTipoMov, 80, 120, 190, 20 );
		adic( new JLabelPad( "Cód.Comis." ), 7, 140, 210, 20 );
		adic( txtCodVend, 7, 160, 70, 20 );
		adic( new JLabelPad( "Descrição do Comissionado" ), 80, 140, 210, 20 );
		adic( txtNomeVend, 80, 160, 190, 20 );
		adic( rgTipo, 7, 190, 265, 30 );
		adic( rgFaturados, 7, 223, 120, 70 );
		adic( rgFinanceiro, 153, 223, 120, 70 );
		adic( cbVendaCanc, 4, 293, 143, 20 );
		adic( cbResumo, 149, 293, 200, 20 );

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcCFOP.setConexao( con );
		lcMov.setConexao( con );
		lcVend.setConexao( con );
	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sqlSubTxt = null;
		StringBuffer sSQL = new StringBuffer();
		String sWhere = "";
		String sWhere1 = "";
		String sWhere2 = "";
		String sWhere3 = "";
		String sCab = "PERÍODO DE "+txtDataini.getVlrString()+" ATÉ "+txtDatafim.getVlrString();

		if ( txtCodCFOP.getVlrInteger().intValue() > 0 )
			sWhere += " AND I.CODNAT= ? ";

		if ( txtCodTipoMov.getVlrInteger().intValue() > 0 ) {
			sWhere += " AND V.CODTIPOMOV= ? ";
			sCab += "FILTRADO POR TIPO DE MOVIMENTO - " + txtDescTipoMov.getVlrString();
		}
		
		if ( txtCodVend.getVlrInteger().intValue() > 0 ) {
			sWhere += " AND V.CODVEND = ? ";
			sCab += "FILTRADO PELO COMISSIONADO - " + txtNomeVend.getVlrString();
		}
	
		if ( rgFaturados.getVlrString().equals( "S" ) ) {
			sWhere1 = " AND TM.FISCALTIPOMOV='S' ";
			sCab += " - FATURADO";
		}
		else if ( rgFaturados.getVlrString().equals( "N" ) ) {
			sWhere1 = " AND TM.FISCALTIPOMOV='N' ";
			sCab += " - NAO FATURADO";
		}
		else if ( rgFaturados.getVlrString().equals( "A" ) )
			sWhere1 = " AND TM.FISCALTIPOMOV IN ('S','N') ";

		if ( rgFinanceiro.getVlrString().equals( "S" ) ) {
			sWhere2 = " AND TM.SOMAVDTIPOMOV='S' ";
			sCab += " - FINANCEIRO";
		}
		else if ( rgFinanceiro.getVlrString().equals( "N" ) ) {
			sWhere2 = " AND TM.SOMAVDTIPOMOV='N' ";
			sCab += " - NAO FINANCEIRO";
		}
		else if ( rgFinanceiro.getVlrString().equals( "A" ) )
			sWhere2 = " AND TM.SOMAVDTIPOMOV IN ('S','N') ";

		if ( cbVendaCanc.getVlrString().equals( "N" ) )
			sWhere3 = " AND NOT SUBSTR(V.STATUSVENDA,1,1)='C' ";

		sSQL.append( "SELECT V.CODVENDA, V.DOCVENDA, V.DTEMITVENDA, V.DTSAIDAVENDA, VV.CODVEND, VV.NOMEVEND " );
		sSQL.append( ", I.CODNAT, NT.DESCNAT, V.CODCLI, C.RAZCLI, P.DESCPLANOPAG " );
		sSQL.append( ", SUM(I.VLRDESCITVENDA) VLRDESCITVENDA, SUM(I.VLRLIQITVENDA) VLRLIQITVENDA, SUM(I.VLRLIQITVENDA+I.VLRDESCITVENDA) VLRITVENDA ");
		sSQL.append( "FROM VDVENDA V,VDITVENDA I,VDCLIENTE C, EQTIPOMOV TM, LFNATOPER NT, FNPLANOPAG P, VDVENDEDOR VV " );
		sSQL.append( "WHERE I.CODEMP=? AND I.CODFILIAL=? " );
		sSQL.append( "AND I.CODEMP=V.CODEMP AND I.CODFILIAL=V.CODFILIAL AND I.CODVENDA=V.CODVENDA " );
		sSQL.append( "AND C.CODEMP=V.CODEMPCL AND C.CODFILIAL=V.CODFILIALCL AND C.CODCLI=V.CODCLI " );
		sSQL.append( "AND TM.CODEMP=V.CODEMPTM AND TM.CODFILIAL=V.CODFILIALTM AND TM.CODTIPOMOV=V.CODTIPOMOV " );
		sSQL.append( "AND NT.CODEMP=I.CODEMPNT AND NT.CODFILIAL=I.CODFILIALNT AND NT.CODNAT=I.CODNAT " );
		sSQL.append( "AND P.CODEMP=V.CODEMPPG AND P.CODFILIAL=V.CODFILIALPG AND P.CODPLANOPAG=V.CODPLANOPAG " );
		sSQL.append( "AND VV.CODEMP = V.CODEMP and VV.CODFILIAL = V.CODFILIAL AND VV.CODVEND=V.CODVEND ");
		sSQL.append( sWhere );
		sSQL.append( sWhere1 );
		sSQL.append( sWhere2 );
		sSQL.append( sWhere3 );
		sSQL.append( "AND V.DTEMITVENDA BETWEEN ? AND ? " );
		sSQL.append( "GROUP BY V.CODVENDA, V.DOCVENDA, V.DTEMITVENDA, V.DTSAIDAVENDA, " );
		sSQL.append( "I.CODNAT, NT.DESCNAT, V.CODCLI, C.RAZCLI, P.DESCPLANOPAG,VV.CODVEND, VV.NOMEVEND " );
		sSQL.append( "ORDER BY I.CODNAT, V.DTEMITVENDA, V.DOCVENDA, V.CODVENDA " );

		try {

			int param = 1;
			
			ps = con.prepareStatement( sSQL.toString() );
			
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "VDITVENDA" ) );
			
			if ( txtCodCFOP.getVlrInteger().intValue() > 0 )
				ps.setInt( param++, txtCodCFOP.getVlrInteger().intValue());
		
			
			if ( txtCodTipoMov.getVlrInteger().intValue() > 0 ) 
				ps.setInt( param++, txtCodTipoMov.getVlrInteger().intValue());
			
			
			if ( txtCodVend.getVlrInteger().intValue() > 0 )
				ps.setInt( param++, txtCodVend.getVlrInteger().intValue());
			
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			rs = ps.executeQuery();	
			
			if("S".equals( cbResumo.getVlrString())	){
				sqlSubTxt = imprimirResumo();
			}

		} catch ( Exception e ) {

			Funcoes.mensagemErro( this, "Erro ao buscar dados da venda !\n" + e.getMessage() );
			e.printStackTrace();
		}

		if ( "T".equals( rgTipo.getVlrString() ) ) {

			imprimeTexto( rs, bVisualizar, sCab );
		}
		else {
			imprimeGrafico( rs, bVisualizar, sCab , sqlSubTxt);
		}
	}
	
	
	public String imprimirResumo() throws SQLException{
		StringBuilder sql = new StringBuilder();
		PreparedStatement psSub = null;
		ResultSet rsSub = null;
		String sWhere = "";
		String sWhere1 = "";
		String sWhere2 = "";
		String sWhere3 = "";
		String sCab = "PERÍODO DE "+txtDataini.getVlrString()+" ATÉ "+txtDatafim.getVlrString();

		if ( txtCodCFOP.getVlrInteger().intValue() > 0 )
			sWhere += " AND I.CODNAT=  '" + txtCodCFOP.getVlrInteger() + "'";

		if ( txtCodTipoMov.getVlrInteger().intValue() > 0 ) {
			sWhere += " AND V.CODTIPOMOV= " + txtCodTipoMov.getVlrInteger();
			sCab += "FILTRADO POR TIPO DE MOVIMENTO - " + txtDescTipoMov.getVlrString();
		}
		
		if ( txtCodVend.getVlrInteger().intValue() > 0 ) {
			sWhere += " AND V.CODVEND = ? ";
			sCab += "FILTRADO PELO COMISSIONADO - " + txtNomeVend.getVlrString();
		}
	
		if ( rgFaturados.getVlrString().equals( "S" ) ) {
			sWhere1 = " AND TM.FISCALTIPOMOV='S' ";
			sCab += " - FATURADO";
		}
		else if ( rgFaturados.getVlrString().equals( "N" ) ) {
			sWhere1 = " AND TM.FISCALTIPOMOV='N' ";
			sCab += " - NAO FATURADO";
		}
		else if ( rgFaturados.getVlrString().equals( "A" ) )
			sWhere1 = " AND TM.FISCALTIPOMOV IN ('S','N') ";

		if ( rgFinanceiro.getVlrString().equals( "S" ) ) {
			sWhere2 = " AND TM.SOMAVDTIPOMOV='S' ";
			sCab += " - FINANCEIRO";
		}
		else if ( rgFinanceiro.getVlrString().equals( "N" ) ) {
			sWhere2 = " AND TM.SOMAVDTIPOMOV='N' ";
			sCab += " - NAO FINANCEIRO";
		}
		else if ( rgFinanceiro.getVlrString().equals( "A" ) )
			sWhere2 = " AND TM.SOMAVDTIPOMOV IN ('S','N') ";

		if ( cbVendaCanc.getVlrString().equals( "N" ) )
			sWhere3 = " AND NOT SUBSTR(V.STATUSVENDA,1,1)='C' ";
		
		
		sql.append( "SELECT I.CODNAT, NT.DESCNAT, SUM(I.VLRLIQITVENDA) VLRLIQITVENDA " );
		sql.append( ", SUM(I.VLRDESCITVENDA) VLRDESCITVENDA, SUM( I.VLRLIQITVENDA+I.VLRDESCITVENDA ) VLRITVENDA " );
		sql.append( "FROM VDVENDA V,VDITVENDA I, " );
		sql.append( "VDCLIENTE C, EQTIPOMOV TM, LFNATOPER NT, FNPLANOPAG P " );
		sql.append( "WHERE I.CODEMP= ");
		sql.append( Aplicativo.iCodEmp );
		sql.append( " AND I.CODFILIAL= ");
		sql.append( ListaCampos.getMasterFilial( "VDITVENDA") );
		sql.append( " AND I.CODEMP=V.CODEMP AND I.CODFILIAL=V.CODFILIAL AND I.CODVENDA=V.CODVENDA " );
		sql.append( "AND C.CODEMP=V.CODEMPCL AND C.CODFILIAL=V.CODFILIALCL AND C.CODCLI=V.CODCLI " );
		sql.append( "AND TM.CODEMP=V.CODEMPTM AND TM.CODFILIAL=V.CODFILIALTM AND TM.CODTIPOMOV=V.CODTIPOMOV " );
		sql.append( "AND NT.CODEMP=I.CODEMPNT AND NT.CODFILIAL=I.CODFILIALNT AND NT.CODNAT=I.CODNAT " );
		sql.append( "AND P.CODEMP=V.CODEMPPG AND P.CODFILIAL=V.CODFILIALPG AND P.CODPLANOPAG=V.CODPLANOPAG " );
		sql.append( sWhere );
		sql.append( sWhere1 );
		sql.append( sWhere2 );
		sql.append( sWhere3 );
		sql.append( "AND V.DTEMITVENDA BETWEEN '");
		sql.append(Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
		sql.append( "' AND '" );
		sql.append(Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
		sql.append( "' GROUP BY I.CODNAT, NT.DESCNAT " );
		sql.append( "ORDER BY I.CODNAT, NT.DESCNAT " );
		
		System.out.println(sql.toString());
		return sql.toString();
	}

	public void imprimeTexto( final ResultSet rs, final TYPE_PRINT bVisualizar, final String sCab ) {

		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		String sCFOP = "";
		bTotalCFOP = new BigDecimal( "0" );
		bTotalGeral = new BigDecimal( "0" );

		try {

			imp.verifLinPag();
			imp.montaCab();
			imp.setTitulo( "Relatório de Vendas por CFOP" );
			imp.addSubTitulo( "PERIODO DE :" + txtDataini.getVlrString() + " Até: " + txtDatafim.getVlrString() );
			imp.limpaPags();

			while ( rs.next() ) {

				if ( imp.pRow() >= ( linPag - 1 ) ) {
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "+" + sLinhaFina + "+" );
					imp.incPags();
					imp.eject();
				}

				if ( imp.pRow() == 0 ) {
					imp.impCab( 136, true );
					imp.say( 0, imp.comprimido() );
					imp.say( 0, "|" + sLinhaFina + "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "| NF" );
					imp.say( 14, "| EMISSÃO" );
					imp.say( 27, "| SAIDA" );
					imp.say( 40, "| PEDIDO" );
					imp.say( 50, "| COD.CLI." );
					imp.say( 61, "| NOME DO CLIENTE" );
					imp.say( 114, "|      VLR. TOTAL" );
					imp.say( 135, "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + sLinhaFina + "|" );
				}

				if ( !rs.getString( "CODNAT" ).equals( sCFOP ) ) {
					subTotal( imp, rs );
					sCFOP = rs.getString( "CODNAT" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + sLinhaLarga + "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" );
					imp.say( 7, sCFOP + " - " + rs.getString( "DESCNAT" ) );
					imp.say( 135, "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + sLinhaFina + "|" );
				}

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "| " + Funcoes.copy( rs.getString( "DocVenda" ), 0, 10 ) );
				imp.say( 14, "| " + StringFunctions.sqlDateToStrDate( rs.getDate( "DtEmitVenda" ) ) );
				imp.say( 27, "| " + StringFunctions.sqlDateToStrDate( rs.getDate( "DtSaidaVenda" ) ) );
				imp.say( 40, "| " + rs.getInt( "CodVenda" ) );
				imp.say( 50, "| " + rs.getInt( "CodCli" ) );
				imp.say( 61, "| " + Funcoes.copy( rs.getString( "RazCli" ), 0, 50 ) );
				imp.say( 114, "| " + Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( rs.getFloat( 9 ) ) ) );
				imp.say( 135, "|" );

				if ( rs.getString( 9 ) != null ) {
					bTotalCFOP = bTotalCFOP.add( new BigDecimal( rs.getString( 9 ) ) );
				}
			}

			subTotal( imp, rs );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "+" + sLinhaLarga + "+" );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "|" );
			imp.say( 96, "TOTAL GERAL       | " + Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( bTotalGeral ) ) );
			imp.say( 135, "|" );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "+" + sLinhaLarga + "+" );

			imp.eject();
			imp.fechaGravacao();

			con.commit();

		} catch ( Exception err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro consulta tabela de venda!\n" + err.getMessage(), true, con, err );
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
	}

	public void imprimeGrafico( final ResultSet rs, final TYPE_PRINT bVisualizar, final String sCab, final String sqlSubTxt ) {

		HashMap<String, Object> hParam = new HashMap<String, Object>();

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "VDVENDA" ) );
		hParam.put( "FILTROS", sCab );
		hParam.put( "RESUMO", cbResumo.getVlrString() );
		hParam.put( "SUBREPORT_DIR", "org/freedom/relatorios/" );
		hParam.put( "CONEXAO", con.getConnection() );
		hParam.put( "sqlTable", sqlSubTxt );

		FPrinterJob dlGr = new FPrinterJob( "relatorios/VendasCFOP.jasper", "Vendas por CFOP", null, rs, hParam, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {

			dlGr.setVisible( true );

		}
		else {
			try {

				JasperPrintManager.printReport( dlGr.getRelatorio(), true );

			} catch ( Exception err ) {

				Funcoes.mensagemErro( this, "Erro na impressão do relatório de Compras por CFOP!\n" + err.getMessage(), true, con, err );
			}
		}
	}

	private void subTotal( ImprimeOS imp, ResultSet rs ) {

		try {
			if ( bTotalCFOP.floatValue() != 0f ) {
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" + sLinhaFina + "|" );
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" );
				imp.say( 96, "SubTotal          | " + Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( bTotalCFOP ) ) );
				imp.say( 135, "|" );

				if ( rs.getString( 9 ) != null ) {
					bTotalGeral = bTotalGeral.add( bTotalCFOP );
				}

				bTotalCFOP = new BigDecimal( "0" );
			}
		} catch ( Exception err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro consulta tabela de venda!\n" + err.getMessage(), true, con, err );
		}
	}
}
