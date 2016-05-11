/**
 * @version 16/11/2012 <BR>
 * @author Setpoint Informática Ltda./ Bruno Nascimento <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FRVendasTipoCli.java <BR>
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
 *                       Tela para impressão de relatório detalhado e resumo de vendas por tipo de cliente.
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

public class FRVendasTipoCli extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodTipoCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JCheckBoxPad cbVendaCanc = new JCheckBoxPad( "Mostrar Canceladas", "S", "N" );

	private JCheckBoxPad cbDetalhado = new JCheckBoxPad( "Detalhado", "S", "N" );

	private JRadioGroup<?, ?> rgFaturados = null;

	private JRadioGroup<?, ?> rgFinanceiro = null;

	private Vector<String> vLabsFat = new Vector<String>();

	private Vector<String> vValsFat = new Vector<String>();

	private Vector<String> vLabsFin = new Vector<String>();

	private Vector<String> vValsFin = new Vector<String>();

	private JRadioGroup<?, ?> rgTipo = null;

	private Vector<String> vLabs1 = new Vector<String>();

	private Vector<String> vVals1 = new Vector<String>();

	private ListaCampos lcCli = new ListaCampos( this, "NT" );

	private ListaCampos lcTipoCli = new ListaCampos( this, "TM" );
	
	private ListaCampos lcVend = new ListaCampos(this);

	private BigDecimal bTotalCFOP;

	private BigDecimal bTotalGeral;

	private String sLinhaFina = StringFunctions.replicate( "-", 133 );

	private String sLinhaLarga = StringFunctions.replicate( "=", 133 );

	public FRVendasTipoCli()  {
		super( false );

		setTitulo( "Vendas por Tipo de Cliente" );
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

		vLabs1.addElement( "Detalhado" );
		vLabs1.addElement( "Resumido" );
		vVals1.addElement( "D" );
		vVals1.addElement( "R" );

		rgTipo = new JRadioGroup<String, String>( 1, 2, vLabs1, vVals1 );
		rgTipo.setVlrString( "D" );

		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.Cli", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo( txtRazCli, "RazCli", "Nome do Cliente", ListaCampos.DB_SI, false ) );
		lcCli.montaSql( false, "CLIENTE", "VD" );
		lcCli.setQueryCommit( false );
		lcCli.setReadOnly( true );
		txtCodCli.setNomeCampo( "CodCli" );
		txtCodCli.setFK( true );
		txtCodCli.setTabelaExterna( lcCli, null );

		lcTipoCli.add( new GuardaCampo( txtCodTipoCli, "CodTipoCli", "Cód.tp.cli.", ListaCampos.DB_PK, false ) );
		lcTipoCli.add( new GuardaCampo( txtDescTipoCli, "DescTipoCli", "Descrição do tipo de Cliente", ListaCampos.DB_SI, false ) );
		lcTipoCli.montaSql( false, "TIPOCLI", "VD" );
		lcTipoCli.setQueryCommit( false );
		lcTipoCli.setReadOnly( true );
		txtCodTipoCli.setNomeCampo( "CodTipoCli" );
		txtCodTipoCli.setFK( true );
		txtCodTipoCli.setTabelaExterna( lcTipoCli, null );
		
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
		adic( new JLabelPad( "Cód.tp.cli." ), 7, 60, 210, 20 );
		adic( txtCodTipoCli, 7, 80, 70, 20 );
		adic( new JLabelPad( "Descrição do tipo de cliente" ), 80, 60, 210, 20 );
		adic( txtDescTipoCli, 80, 80, 190, 20 );
		adic( new JLabelPad( "Cód.Comis." ), 7, 100, 210, 20 );
		adic( txtCodVend, 7, 120, 70, 20 );
		adic( new JLabelPad( "Descrição do Comissionado" ), 80, 100, 210, 20 );
		adic( txtNomeVend, 80, 120, 190, 20 );
		adic( new JLabelPad( "Cód.Cli" ), 7, 140, 210, 20 );
		adic( txtCodCli, 7, 160, 70, 20 );
		adic( new JLabelPad( "Nome do Cliente" ), 80, 140, 210, 20 );
		adic( txtRazCli, 80, 160, 190, 20 );

		adic( rgTipo, 7, 190, 265, 30 );
		adic( rgFaturados, 7, 223, 120, 70 );
		adic( rgFinanceiro, 153, 223, 120, 70 );
		adic( cbVendaCanc, 4, 293, 143, 20 );
		adic( cbDetalhado, 149, 293, 200, 20 );

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcCli.setConexao( con );
		lcTipoCli.setConexao( con );
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
		String caminhoRel = null;
		String sql = null;
		String sWhere = "";
		String sWhere1 = "";
		String sWhere2 = "";
		String sWhere3 = "";
		String sCab = "PERÍODO DE "+txtDataini.getVlrString()+" ATÉ "+txtDatafim.getVlrString();

		if ( txtCodCli.getVlrInteger().intValue() > 0 )
			sWhere += " AND C.CODCLI= ? ";

		if ( txtCodTipoCli.getVlrInteger().intValue() > 0 ) {
			sWhere += " AND C.CODTIPOCLI= ? ";
			sCab += "FILTRADO POR TIPO DE CLIENTE - " + txtDescTipoCli.getVlrString();
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
		

		try {
			
			if ( "D".equals( rgTipo.getVlrString() ) ) {
				sql = queryDetalhado( sWhere, sWhere1, sWhere2, sWhere3 );
				caminhoRel = "relatorios/VendasTipoCliDet.jasper";
			} else {
				sql = queryResumido( sWhere, sWhere1, sWhere2, sWhere3 );
				caminhoRel = "relatorios/VendasTipoCli.jasper";
			}
			

			int param = 1;
			
			ps = con.prepareStatement( sql );
		
		
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "VDITVENDA" ) );
			
			if ( txtCodCli.getVlrInteger().intValue() > 0 )
				ps.setInt( param++, txtCodCli.getVlrInteger().intValue());
		
			
			if ( txtCodTipoCli.getVlrInteger().intValue() > 0 ) 
				ps.setInt( param++, txtCodTipoCli.getVlrInteger().intValue());
			
			
			if ( txtCodVend.getVlrInteger().intValue() > 0 )
				ps.setInt( param++, txtCodVend.getVlrInteger().intValue());
			
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			rs = ps.executeQuery();	
			
			if("S".equals( cbDetalhado.getVlrString())	){
				sqlSubTxt = imprimirDetalhado();
			}

		} catch ( Exception e ) {

			Funcoes.mensagemErro( this, "Erro ao buscar dados da venda !\n" + e.getMessage() );
			e.printStackTrace();
		}

		
			imprimeGrafico( rs, bVisualizar, sCab , sqlSubTxt, caminhoRel);
	}
	
	
	
	public String queryResumido(String where, String where1, String where2, String where3 ){
		StringBuilder sql = new StringBuilder();
		
		sql.append( "SELECT V.CODVENDA, V.DOCVENDA, V.DTEMITVENDA, V.DTSAIDAVENDA, VV.CODVEND, VV.NOMEVEND , " );
		sql.append( "I.CODNAT, NT.DESCNAT, V.CODCLI, C.RAZCLI, c.nomecli, P.DESCPLANOPAG , SUM(I.VLRDESCITVENDA) VLRDESCITVENDA," );
		sql.append( "SUM(I.VLRLIQITVENDA) VLRLIQITVENDA, SUM(I.VLRLIQITVENDA+I.VLRDESCITVENDA) VLRITVENDA, tc.codtipocli, TC.desctipocli ");
		sql.append( "FROM VDVENDA V,VDITVENDA I,VDCLIENTE C, VDTIPOCLI TC, EQTIPOMOV TM, LFNATOPER NT, FNPLANOPAG P, VDVENDEDOR VV " );
		sql.append( "WHERE I.CODEMP=? AND I.CODFILIAL=? AND I.CODEMP=V.CODEMP AND I.CODFILIAL=V.CODFILIAL AND I.CODVENDA=V.CODVENDA AND " );
		sql.append( "C.CODEMP=V.CODEMPCL AND C.CODFILIAL=V.CODFILIALCL AND C.CODCLI=V.CODCLI AND " );
		
		sql.append( "TC.codemp = C.codemp and TC.codfilial=c.codfilial and tc.codtipocli=c.codtipocli and " );
		sql.append( "TM.CODEMP=V.CODEMPTM AND TM.CODFILIAL=V.CODFILIALTM AND TM.CODTIPOMOV=V.CODTIPOMOV AND " );
		sql.append( " NT.CODEMP=I.CODEMPNT AND NT.CODFILIAL=I.CODFILIALNT AND NT.CODNAT=I.CODNAT AND " );
		sql.append( "P.CODEMP=V.CODEMPPG AND P.CODFILIAL=V.CODFILIALPG AND P.CODPLANOPAG=V.CODPLANOPAG AND " );
		sql.append( "VV.CODEMP = V.CODEMP and VV.CODFILIAL = V.CODFILIAL AND VV.CODVEND=V.CODVEND ");
		sql.append( where );
		sql.append( where1 );
		sql.append( where2 );
		sql.append( where3 );
		sql.append( "AND V.DTEMITVENDA BETWEEN ? AND ? " );
		sql.append( "GROUP BY VV.codvend,  VV.NOMEVEND, TC.codtipocli,  TC.DesctipoCli, V.codvenda, V.DocVenda, V.dtemitvenda, v.dtsaidavenda " );
		sql.append( " ,I.CODNAT, NT.descnat, V.codcli, C.razcli, C.nomecli, P.descplanopag " );
		sql.append( "ORDER BY VV.codVend, VV.NOMEVEND, Tc.codtipocli, tc.desctipocli " );		
		
		return sql.toString();
		
	}
	
	
	public String queryDetalhado(String where, String where1, String where2, String where3 ){
		StringBuilder sql = new StringBuilder();


		sql.append( "SELECT V.CODVENDA, V.DOCVENDA, V.DTEMITVENDA, V.DTSAIDAVENDA, VV.CODVEND, VV.NOMEVEND , ");
		sql.append( "I.CODNAT, NT.DESCNAT, V.CODCLI, C.RAZCLI, c.nomecli, P.DESCPLANOPAG , ");
		sql.append( "I.codprod, pd.descprod, i.codlote, ");
		sql.append( "i.vlrproditvenda, i.qtditvenda, ");
		sql.append( "(i.vlrliqitvenda + i.vlrdescitvenda) vlrbruto, i.vlrdescitvenda vlrdesc, (i.vlrliqitvenda) vlrliq ");
		sql.append( ", tc.codtipocli, TC.desctipocli ");


		sql.append( "FROM vdvenda V, VDITVENDA I, EQPRODUTO PD,VDCLIENTE C, VDTIPOCLI TC, EQTIPOMOV TM, LFNATOPER NT, FNPLANOPAG P, VDVENDEDOR VV ");
		sql.append( "where ");
		sql.append( "I.CODEMP=? AND I.CODFILIAL=? AND  I.CODEMP=V.CODEMP AND I.CODFILIAL=V.CODFILIAL AND I.CODVENDA=V.CODVENDA AND ");
		sql.append( "I.codemp = V.CODEMP AND I.CODFILIAL = V.CODFILIAL AND i.codvenda = v.codvenda AND ");
		sql.append( "Pd.codemp = i.codemppd and pd.codfilial= i.codfilialpd and pd.codprod=i.codprod AND ");
		sql.append( "C.CODEMP=V.CODEMPCL AND C.CODFILIAL=V.CODFILIALCL AND C.CODCLI=V.CODCLI AND ");
		sql.append( "TC.codemp = C.codemp and TC.codfilial=c.codfilial and tc.codtipocli=c.codtipocli and ");
		sql.append( "TM.CODEMP=V.CODEMPTM AND TM.CODFILIAL=V.CODFILIALTM AND TM.CODTIPOMOV=V.CODTIPOMOV AND ");
		sql.append( "NT.CODEMP=I.CODEMPNT AND NT.CODFILIAL=I.CODFILIALNT AND NT.CODNAT=I.CODNAT AND ");
		sql.append( "P.CODEMP=V.CODEMPPG AND P.CODFILIAL=V.CODFILIALPG AND P.CODPLANOPAG=V.CODPLANOPAG AND ");
		sql.append( "VV.CODEMP = V.CODEMP and VV.CODFILIAL = V.CODFILIAL AND VV.CODVEND=V.CODVEND ");

		sql.append( where );
		sql.append( where1 );
		sql.append( where2 );
		sql.append( where3 );
		
		sql.append( " and  V.DTEMITVENDA BETWEEN ? AND ?");
		sql.append( " ORDER BY ");
		sql.append( " VV.codVend, Tc.codtipocli, v.codvenda ");

		
		return sql.toString();
		
	}
	
	
	
	public String imprimirDetalhado() throws SQLException{
		StringBuilder sql = new StringBuilder();
		PreparedStatement psSub = null;
		ResultSet rsSub = null;
		String sWhere = "";
		String sWhere1 = "";
		String sWhere2 = "";
		String sWhere3 = "";
		String sCab = "PERÍODO DE "+txtDataini.getVlrString()+" ATÉ "+txtDatafim.getVlrString();

		if ( txtCodCli.getVlrInteger().intValue() > 0 )
			sWhere += " AND C.CODCLI=  '" + txtCodCli.getVlrInteger() + "'";

		if ( txtCodTipoCli.getVlrInteger().intValue() > 0 ) {
			sWhere += " AND C.CODTIPOCLI= " + txtCodTipoCli.getVlrInteger();
			sCab += "FILTRADO POR TIPO DE MOVIMENTO - " + txtDescTipoCli.getVlrString();
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
		
		
		sql.append( "SELECT C.codtipocli, TC.desctipocli, SUM(I.VLRLIQITVENDA) VLRLIQITVENDA , SUM(I.VLRDESCITVENDA) VLRDESCITVENDA, " );
		sql.append( "SUM( I.VLRLIQITVENDA+I.VLRDESCITVENDA ) VLRITVENDA " );
		sql.append( "FROM VDVENDA V,VDITVENDA I, VDCLIENTE C, EQTIPOMOV TM, LFNATOPER NT, FNPLANOPAG P, vdtipocli TC " );
		sql.append( "WHERE I.CODEMP= ");
		sql.append( Aplicativo.iCodEmp );
		sql.append( " AND I.CODFILIAL= ");
		sql.append( ListaCampos.getMasterFilial( "VDITVENDA") );
		sql.append( " AND I.CODEMP=V.CODEMP AND I.CODFILIAL=V.CODFILIAL AND I.CODVENDA=V.CODVENDA " );
		sql.append( "AND C.CODEMP=V.CODEMPCL AND C.CODFILIAL=V.CODFILIALCL AND C.CODCLI=V.CODCLI " );
		sql.append( "AND TC.codemp = C.codemp and TC.codfilial=c.codfilial and tc.codtipocli=c.codtipocli " );
		
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
		sql.append( "' GROUP BY C.codtipocli, tc.desctipocli " );
		sql.append( "ORDER BY  C.codtipocli, tc.desctipocli " );
		
	
		
		System.out.println(sql.toString());
		return sql.toString();
	}

	public void imprimeGrafico( final ResultSet rs, final TYPE_PRINT bVisualizar, final String sCab, final String sqlSubTxt, final String caminhoRel ) {

		HashMap<String, Object> hParam = new HashMap<String, Object>();

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "VDVENDA" ) );
		hParam.put( "FILTROS", sCab );
		hParam.put( "RESUMO", cbDetalhado.getVlrString() );
		hParam.put( "SUBREPORT_DIR", "org/freedom/relatorios/" );
		hParam.put( "CONEXAO", con.getConnection() );
		hParam.put( "sqlTable", sqlSubTxt );

		FPrinterJob dlGr = new FPrinterJob( caminhoRel, "Compras por Tipo de cliente", null, rs, hParam, this );

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
}
