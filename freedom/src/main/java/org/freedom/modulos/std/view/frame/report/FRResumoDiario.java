/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FRResumoDiario.java <BR>
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
 *                         Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.std.view.frame.report;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
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

public class FRResumoDiario extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JCheckBoxPad cbVendaCanc = new JCheckBoxPad( "Mostrar Canceladas", "S", "N" );
	
	private JCheckBoxPad cbAgruparVendedor = new JCheckBoxPad( "Agrupar por Vendedor", "S", "N" );
	
	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );	
	
	private JRadioGroup<?, ?> rgTipo = null;

	private JRadioGroup<?, ?> rgFaturados = null;

	private JRadioGroup<?, ?> rgFinanceiro = null;

	private JRadioGroup<?, ?> rgFormato = null;

	private ListaCampos lcVend = new ListaCampos( this );
	
	private ListaCampos lcCli = new ListaCampos( this, "CL" );
	
	private JRadioGroup<?, ?> rgEmitidos = null;
	
	private Vector<String> vLabsEmit = new Vector<String>();

	private Vector<String> vValsEmit = new Vector<String>();


	public FRResumoDiario() {

		setTitulo( "Resumo Diario" );
		setAtribos( 80, 80, 333, 480 );

		Vector<String> vLabs = new Vector<String>();
		Vector<String> vVals = new Vector<String>();

		vLabs.addElement( "Grafico" );
		vLabs.addElement( "Texto" );
		vVals.addElement( "G" );
		vVals.addElement( "T" );
		rgTipo = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgTipo.setVlrString( "T" );

		Vector<String> vLabs1 = new Vector<String>();
		Vector<String> vVals1 = new Vector<String>();

		vLabs1.addElement( "Detalhado" );
		vLabs1.addElement( "Resumido" );
		vVals1.addElement( "D" );
		vVals1.addElement( "R" );
		rgFormato = new JRadioGroup<String, String>( 1, 2, vLabs1, vVals1 );
		rgFormato.setVlrString( "D" );

		Vector<String> vLabs2 = new Vector<String>();
		Vector<String> vVals2 = new Vector<String>();

		vLabs2.addElement( "Faturado" );
		vLabs2.addElement( "Não Faturado" );
		vLabs2.addElement( "Ambos" );
		vVals2.addElement( "S" );
		vVals2.addElement( "N" );
		vVals2.addElement( "A" );
		rgFaturados = new JRadioGroup<String, String>( 3, 1, vLabs2, vVals2 );
		rgFaturados.setVlrString( "S" );

		Vector<String> vLabs3 = new Vector<String>();
		Vector<String> vVals3 = new Vector<String>();

		vLabs3.addElement( "Financeiro" );
		vLabs3.addElement( "Não Finaceiro" );
		vLabs3.addElement( "Ambos" );
		vVals3.addElement( "S" );
		vVals3.addElement( "N" );
		vVals3.addElement( "A" );
		rgFinanceiro = new JRadioGroup<String, String>( 3, 1, vLabs3, vVals3 );
		rgFinanceiro.setVlrString( "S" );
		
		vLabsEmit.addElement( "Emitidos" );
		vLabsEmit.addElement( "Não emitidos" );
		vLabsEmit.addElement( "Ambos" );
		vValsEmit.addElement( "S" );
		vValsEmit.addElement( "N" );
		vValsEmit.addElement( "A" );
		rgEmitidos = new JRadioGroup<String, String>( 3, 1, vLabsEmit, vValsEmit );
		rgEmitidos.setVlrString( "A" );


		lcVend.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.comiss.", ListaCampos.DB_PK, false ) );
		lcVend.add( new GuardaCampo( txtDescVend, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, false ) );
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
		
		
		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo( txtNomeCli, "NomeCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		txtCodCli.setTabelaExterna( lcCli, FCliente.class.getCanonicalName() );
		txtCodCli.setNomeCampo( "CodCli" );
		txtCodCli.setFK( true );
		lcCli.setReadOnly( true );
		lcCli.montaSql( false, "CLIENTE", "VD" );


		adic( new JLabelPad( "Periodo:" ), 7, 5, 100, 20 );
		adic( lbLinha, 60, 15, 210, 2 );

		adic( new JLabelPad("De:"), 10, 30, 97, 20  );
		adic( txtDataini, 32, 30, 97, 20  );
		
		adic( new JLabelPad("Até:"), 140, 30, 100, 20  );
		adic( txtDatafim, 170, 30, 100, 20  );

		adic( txtCodVend, 7, 80, 70, 20, "Cód.comiss." );
		adic( txtDescVend, 80, 80, 190, 20,"Nome do comissionado" );
		
		adic( txtCodCli, 7, 120, 70, 20, "Cód.Cli" );
		adic( txtNomeCli, 80, 120, 190, 20, "Nome do cliente" );

		adic( rgTipo, 			7, 		155, 	265, 	30 );
		adic( rgFormato, 		7, 		205, 	265, 	30 );
		adic( rgFaturados, 		7, 		240, 	120, 	70 );

		adic( rgFinanceiro, 	153, 	240, 	120, 	70 );
		
		
		adic( rgEmitidos,		  7,	320, 	120, 	70 );
		adic( cbVendaCanc, 		153, 	320, 	200, 	20 );
		adic( cbAgruparVendedor,153, 	340, 	200, 	20 );
		
	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();
		StringBuffer sCab = new StringBuffer();
		StringBuffer sWhere = new StringBuffer();
		String orderBy = "";
		String sWhere1 = "";
		String sWhere2 = "";
		String sWhere3 = "";

		try {

			if ( rgFaturados.getVlrString().equals( "S" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV='S' ";
				sCab.append( "FATURADO" );
			}
			else if ( rgFaturados.getVlrString().equals( "N" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV='N' ";
				if ( sCab.length() > 0 ) {
					sCab.append( " - " );
				}
				sCab.append( "NAO FATURADO" );
			}
			else if ( rgFaturados.getVlrString().equals( "A" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV IN ('S','N') ";
			}

			if ( rgFinanceiro.getVlrString().equals( "S" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV='S' ";
				if ( sCab.length() > 0 ) {
					sCab.append( " - " );
				}
				sCab.append( "FINANCEIRO" );
			}
			else if ( rgFinanceiro.getVlrString().equals( "N" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV='N' ";
				if ( sCab.length() > 0 ) {
					sCab.append( " - " );
				}
				sCab.append( "NAO FINANCEIRO" );
			}
			else if ( rgFinanceiro.getVlrString().equals( "A" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV IN ('S','N') ";
			}

			if ( cbVendaCanc.getVlrString().equals( "N" ) ) {
				sWhere3 = " AND NOT SUBSTR(V.STATUSVENDA,1,1)='C' ";
			}

			if ( txtCodVend.getText().trim().length() > 0 ) {
				sWhere.append( " AND V.CODVEND = " );
				sWhere.append( txtCodVend.getText().trim() );
				sWhere.append( " AND V.CODEMPVD=" );
				sWhere.append( Aplicativo.iCodEmp );
				sWhere.append( " AND V.CODFILIALVD=" );
				sWhere.append( lcVend.getCodFilial() );
				sCab.append( "\nCOMISS.: " + txtCodVend.getVlrString() + " - " + txtDescVend.getText().trim() );
			}
			
			if ( txtCodCli.getVlrString().trim().length() > 0 ) {
				sWhere.append (" AND V.CODCLI = " + txtCodCli.getVlrInteger());;
				sCab.append("\nCLIENTE: " + txtNomeCli.getVlrInteger());;
			}
			
			if ( rgEmitidos.getVlrString().equals( "S" ) ) {
				sWhere.append(" AND V.STATUSVENDA IN ('V2','V3','P3') " );
				sCab.append(" EMITIDOS " );
			}
			else if ( rgEmitidos.getVlrString().equals( "N" ) ) {
				sWhere.append(" AND V.STATUSVENDA NOT IN ('V2','V3','P3') ");
				sCab.append( "NAO EMITIDOS" );
			}

			orderBy = " ORDER BY V.DTEMITVENDA,V.DOCVENDA";
			if ( "S".equals( cbAgruparVendedor.getVlrString()) && "G".equals( rgTipo.getVlrString()) ){
				orderBy = " ORDER BY V.CODVEND ";
			}
			if ( rgFormato.getVlrString().equals( "D" ) ) {
				sSQL.append( "SELECT V.DTEMITVENDA,V.CODTIPOMOV,V.CODVENDA,V.DOCVENDA,V.SERIE," );
				sSQL.append( "V.STATUSVENDA,V.VLRPRODVENDA,V.VLRLIQVENDA,V.CODCLI,C.RAZCLI," );
				sSQL.append( "V.CODPLANOPAG,P.DESCPLANOPAG,V.VLRCOMISVENDA,V.VLRDESCITVENDA, " );
				sSQL.append( "VD.CODVEND, VD.NOMEVEND " );
				sSQL.append( "FROM VDVENDA V,VDCLIENTE C,FNPLANOPAG P, EQTIPOMOV TM, VDVENDEDOR VD " );
				sSQL.append( "WHERE TM.CODTIPOMOV=V.CODTIPOMOV AND TM.CODEMP=V.CODEMPTM " );
				sSQL.append( "AND TM.CODFILIAL=V.CODFILIALTM AND C.CODCLI=V.CODCLI " );
				sSQL.append( "AND VD.CODEMP = V.CODEMPVD AND VD.CODFILIAL=V.CODFILIALVD AND VD.CODVEND=V.CODVEND " );
				sSQL.append( "AND C.CODEMP=V.CODEMPCL AND C.CODFILIAL=V.CODFILIALCL " );
				sSQL.append( "AND V.DTEMITVENDA BETWEEN ? AND ? AND " );
				sSQL.append( " P.CODEMP = V.CODEMP AND P.CODFILIAL = V.CODFILIAL AND P.CODPLANOPAG=V.CODPLANOPAG AND V.FLAG IN " );
				sSQL.append( AplicativoPD.carregaFiltro( con, org.freedom.library.swing.frame.Aplicativo.iCodEmp ) );
				sSQL.append( " AND V.CODEMP=? AND V.CODFILIAL=?" );
				sSQL.append( sWhere );
				sSQL.append( sWhere1 );
				sSQL.append( sWhere2 );
				sSQL.append( sWhere3 );
				sSQL.append( orderBy );
			}
			else if ( rgFormato.getVlrString().equals( "R" ) ) {
				sSQL.append( "SELECT V.DTEMITVENDA,SUM(V.VLRLIQVENDA) AS VALOR " );
				sSQL.append( "FROM VDVENDA V, EQTIPOMOV TM " );
				sSQL.append( "WHERE V.DTEMITVENDA BETWEEN ? AND ? AND V.FLAG IN " );
				sSQL.append( AplicativoPD.carregaFiltro( con, org.freedom.library.swing.frame.Aplicativo.iCodEmp ) );
				sSQL.append( " AND TM.CODEMP=V.CODEMPTM AND TM.CODFILIAL=V.CODFILIALTM " );
				sSQL.append( " AND TM.CODTIPOMOV=V.CODTIPOMOV" );
				sSQL.append( sWhere );
				sSQL.append( sWhere1 );
				sSQL.append( sWhere2 );
				sSQL.append( sWhere3 );
				sSQL.append( " AND V.CODEMP=? AND V.CODFILIAL=? " );
				sSQL.append( " GROUP BY DTEMITVENDA" );
				sSQL.append( " ORDER BY DTEMITVENDA" );
			}

			ps = con.prepareStatement( sSQL.toString() );
			ps.setDate( 1, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( 2, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "VDVENDA" ) );
			rs = ps.executeQuery();

			if ( "T".equals( rgTipo.getVlrString() ) ) {
				imprimirTexto( bVisualizar, rs, sCab.toString() );
			}
			else if ( "G".equals( rgTipo.getVlrString() ) ) {
				imprimirGrafico( bVisualizar, rs, sCab.toString() );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela de vendas!\n" + err.getMessage(), true, con, err );
		} finally {
			System.gc();
		}
	}

	private void imprimirTexto( final TYPE_PRINT bVisualizar, final ResultSet rs, final String sCab ) {

		String sDtemitvenda = "";
		String sLinhaFina = StringFunctions.replicate( "-", 133 );
		String sLinhaDupla = StringFunctions.replicate( "=", 133 );
		BigDecimal bTotalDiaVal = new BigDecimal( "0" );
		BigDecimal bTotalDiaDesc = new BigDecimal( "0" );
		BigDecimal bTotalVal = new BigDecimal( "0" );
		BigDecimal bTotalDesc = new BigDecimal( "0" );
		BigDecimal bTotalLiq = new BigDecimal( "0" );
		BigDecimal bTotalDiaLiq = new BigDecimal( "0" );
		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		int iLinha = 1;
		int iCol = 0;
		boolean bFimDia = false;

		try {

			if ( rgFormato.getVlrString().equals( "D" ) ) {
				imp.montaCab();
				imp.setTitulo( "Resumo Diário de Vendas" );
				imp.addSubTitulo( "RESUMO DIARIO DE VENDAS   -   PERIODO DE :" + txtDataini.getVlrString() + " Até: " + txtDatafim.getVlrString() );

				if ( sCab.length() > 0 ) {
					imp.addSubTitulo( sCab );
				}

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
						imp.pulaLinha( 0, imp.comprimido() );
						imp.say( 0, "|" );
						imp.say( 135, "|" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|" + sLinhaFina + "|" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "| Dt. Emissao" );
						imp.say( 17, "NF./Ped." );
						imp.say( 31, "Cliente" );
						imp.say( 85, "|    Valor   Desconto " + "  Liquido    F.Pagto." );
						imp.say( 135, "|" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|" + sLinhaFina + "|" );
					}
					if ( ( !StringFunctions.sqlDateToStrDate( rs.getDate( "dtemitvenda" ) ).equals( sDtemitvenda ) ) & ( bFimDia ) ) {
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|" + sLinhaFina + "|" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|" );
						imp.say( 58, "Totais do Dia-> " + sDtemitvenda + " |" + Funcoes.strDecimalToStrCurrency( 10, 2, "" + bTotalDiaVal ) + Funcoes.strDecimalToStrCurrency( 10, 2, "" + bTotalDiaDesc ) + Funcoes.strDecimalToStrCurrency( 11, 2, "" + bTotalDiaLiq ) );
						imp.say( 135, "|" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( imp.pRow(), 0, "|" + sLinhaFina + "|" );
						bTotalDiaVal = new BigDecimal( "0" );
						bTotalDiaDesc = new BigDecimal( "0" );
						bTotalDiaLiq = new BigDecimal( "0" );
						bFimDia = false;
					}
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" );
					if ( !StringFunctions.sqlDateToStrDate( rs.getDate( "dtemitvenda" ) ).equals( sDtemitvenda ) ) {
						imp.say( 3, StringFunctions.sqlDateToStrDate( rs.getDate( "dtemitvenda" ) ) );
					}

					imp.say( 17, rs.getString( "StatusVenda" ).substring( 0, 1 ).equals( "P" ) ? "P-" + Funcoes.copy( rs.getString( "codvenda" ), 0, 6 ) : "V-" + Funcoes.copy( rs.getString( "docvenda" ), 0, 6 ) );
					imp.say( 31, Funcoes.copy( rs.getString( "codcli" ), 0, 8 ) + "-" + Funcoes.copy( rs.getString( "razcli" ), 0, 45 ) + "|" + Funcoes.strDecimalToStrCurrency( 10, 2, rs.getString( "vlrprodvenda" ) ) + Funcoes.strDecimalToStrCurrency( 10, 2, rs.getString( "vlrdescitvenda" ) )
							+ Funcoes.strDecimalToStrCurrency( 11, 2, rs.getString( "vlrliqvenda" ) ) + "  " + Funcoes.copy( rs.getString( "descplanopag" ), 0, 16 ) + "|" );
					if ( rs.getString( "VlrProdVenda" ) != null ) {
						bTotalDiaVal = bTotalDiaVal.add( new BigDecimal( rs.getString( "VlrProdVenda" ) ) );
						bTotalVal = bTotalVal.add( new BigDecimal( rs.getString( "VlrProdVenda" ) ) );
					}

					if ( rs.getString( "VlrDescitvenda" ) != null ) {
						bTotalDiaDesc = bTotalDiaDesc.add( new BigDecimal( rs.getString( "VlrDescitVenda" ) ) );
						bTotalDesc = bTotalDesc.add( new BigDecimal( rs.getString( "VlrDescitVenda" ) ) );
					}

					if ( rs.getString( "VlrLiqVenda" ) != null ) {
						bTotalDiaLiq = bTotalDiaLiq.add( new BigDecimal( rs.getString( "VlrLiqVenda" ) ) );
						bTotalLiq = bTotalLiq.add( new BigDecimal( rs.getString( "VlrLiqVenda" ) ) );
					}

					bFimDia = true;
					sDtemitvenda = StringFunctions.sqlDateToStrDate( rs.getDate( "Dtemitvenda" ) );

				}

				if ( bFimDia ) {
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + sLinhaFina + "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" );
					imp.say( 58, "Totais do Dia-> " + sDtemitvenda + " |" + Funcoes.strDecimalToStrCurrency( 10, 2, "" + bTotalDiaVal ) + Funcoes.strDecimalToStrCurrency( 10, 2, "" + bTotalDiaDesc ) + Funcoes.strDecimalToStrCurrency( 11, 2, "" + bTotalDiaLiq ) );
					imp.say( 135, "|" );
				}

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" + sLinhaDupla + "|" );
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" );
				imp.say( 69, "Totais Geral    |" + Funcoes.strDecimalToStrCurrency( 10, 2, "" + bTotalVal ) + Funcoes.strDecimalToStrCurrency( 10, 2, "" + bTotalDesc ) + Funcoes.strDecimalToStrCurrency( 11, 2, "" + bTotalLiq ) );
				imp.say( 135, "|" );
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" + sLinhaDupla + "|" );

			}
			else if ( rgFormato.getVlrString().equals( "R" ) ) {

				imp.montaCab();
				imp.setTitulo( "Resumo Diário de Vendas" );
				imp.addSubTitulo( "RESUMO DE TOTAL DE VENDAS - PERIODO DE :" + txtDataini.getVlrString() + " Até: " + txtDatafim.getVlrString() );

				if ( sCab.length() > 0 ) {
					imp.addSubTitulo( sCab );
				}

				while ( rs.next() ) {
					if ( imp.pRow() >= ( linPag - 1 ) ) {
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "+" + sLinhaFina + "+" );
						imp.incPags();
						imp.eject();
					}
					if ( imp.pRow() == 0 ) {
						imp.impCab( 136, true );
						imp.say( 0, "|" + sLinhaFina + "|" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|  Data" );
						imp.say( 22, "Valor" );
						imp.say( 35, "|  Data" );
						imp.say( 57, "Valor" );
						imp.say( 70, "|  Data" );
						imp.say( 92, "Valor" );
						imp.say( 105, "|  Data" );
						imp.say( 126, "Valor" );
						imp.say( 135, "|" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|" + sLinhaFina + "|" );
					}

					imp.pulaLinha( iLinha, imp.comprimido() );
					imp.say( iCol, "|  " + StringFunctions.sqlDateToStrDate( rs.getDate( 1 ) ) );
					imp.say( iCol + 14, " " + Funcoes.strDecimalToStrCurrency( 15, 2, "" + rs.getString( 2 ) ) );

					if ( iCol == 0 ) {
						iLinha = 0;
						iCol = 35;
					}
					else if ( iCol == 35 )
						iCol = 70;
					else if ( iCol == 70 )
						iCol = 105;
					else {
						imp.say( 135, "|" );
						iCol = 0;
						iLinha = 1;
					}

					if ( rs.getString( 2 ) != null ) {
						bTotalDiaLiq = bTotalDiaLiq.add( new BigDecimal( rs.getString( 2 ) ) );
						bTotalLiq = bTotalLiq.add( new BigDecimal( rs.getString( 2 ) ) );
					}
				}
				if ( ( iCol < 105 ) && ( iLinha == 0 ) )
					imp.say( 135, "|" );

			}

			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "+" + sLinhaDupla + "+" );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "|" );
			imp.say( 85, "| Total Geral do Período   | " + Funcoes.strDecimalToStrCurrency( 11, 2, "" + bTotalLiq ) );
			imp.say( 135, "|" );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "+" + sLinhaDupla + "+" );

			imp.eject();
			imp.fechaGravacao();

			if ( bVisualizar==TYPE_PRINT.VIEW ) {
				imp.preview( this );
			}
			else {
				imp.print();
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao montar o relatorio!\n" + e.getMessage(), true, con, e );
		}
	}	

	private void imprimirGrafico( final TYPE_PRINT bVisualizar, final ResultSet rs, final String sCab ) {

		FPrinterJob dlGr = null;

		if ( "D".equals( rgFormato.getVlrString() ) ) {
			String fileRelDetalhado = "relatorios/ResumoDiarioDetalhado.jasper";
			if ("S".equals( cbAgruparVendedor.getVlrString())) {
				fileRelDetalhado = "relatorios/ResumoDiarioDetalhado_02.jasper";
			}
			dlGr = new FPrinterJob( fileRelDetalhado, "Resumo de Vendas diario - detalhado", sCab, rs, null, this );
		}
		else if ( "R".equals( rgFormato.getVlrString() ) ) {
			dlGr = new FPrinterJob( "relatorios/ResumoDiarioResumido.jasper", "Resumo de Vendas diario - resumido", sCab, rs, null, this );
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório de resumo diario!" + err.getMessage(), true, con, err );
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcVend.setConexao( con );
		lcCli.setConexao( con );
	}
}
