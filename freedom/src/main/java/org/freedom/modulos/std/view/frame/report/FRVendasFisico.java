/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FRVendasFisico.java <BR>
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
import java.util.Calendar;
import java.util.GregorianCalendar;
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

public class FRVendasFisico extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JCheckBoxPad cbVendaCanc = new JCheckBoxPad( "Mostrar Canceladas", "S", "N" );
	
	private JCheckBoxPad cbPorConserto = new JCheckBoxPad( "Por item de O.S", "S", "N" );

	private JRadioGroup<?, ?> rgTipo = null;
	
	private JRadioGroup<?, ?> rgTipoCusto = null;

	private JRadioGroup<?, ?> rgOrdem = null;

	private JRadioGroup<?, ?> rgFaturados = null;

	private JRadioGroup<?, ?> rgFinanceiro = null;

	private ListaCampos lcVend = new ListaCampos( this );
	
	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private ListaCampos lcCli = new ListaCampos( this, "CL" );
	
	private JRadioGroup<?, ?> rgEmitidos = null;
	
	private Vector<String> vLabsEmit = new Vector<String>();

	private Vector<String> vValsEmit = new Vector<String>();

	public FRVendasFisico() {

		setTitulo( "Físico de Vendas" );
		setAtribos( 80, 80, 325, 540 );

		GregorianCalendar cPeriodo = new GregorianCalendar();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataini.setVlrDate( cPeriodo.getTime() );
		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );

		Vector<String> vLabs = new Vector<String>();
		Vector<String> vVals = new Vector<String>();

		vLabs.addElement( "Grafico" );
		vLabs.addElement( "Texto" );
		vVals.addElement( "G" );
		vVals.addElement( "T" );
		rgTipo = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgTipo.setVlrString( "G" );

		Vector<String> vLabsCusto = new Vector<String>();
		Vector<String> vValsCusto = new Vector<String>();
		
		vLabsCusto.addElement( "MPM" );
		vLabsCusto.addElement( "Informado" );
		vValsCusto.addElement( "ncustompm" );
		vValsCusto.addElement( "ncustoinfo" );
		rgTipoCusto = new JRadioGroup<String, String>( 1, 2, vLabsCusto, vValsCusto );
		rgTipoCusto.setVlrString( "I" );

		Vector<String> vLabs1 = new Vector<String>();
		Vector<String> vVals1 = new Vector<String>();

		vLabs1.addElement( "Código" );
		vLabs1.addElement( "Descrição" );
		vVals1.addElement( "C" );
		vVals1.addElement( "D" );
		rgOrdem = new JRadioGroup<String, String>( 1, 2, vLabs1, vVals1 );
		rgOrdem.setVlrString( "D" );

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
		
		vLabsEmit.addElement( "Emitidos" );
		vLabsEmit.addElement( "Não emitidos" );
		vLabsEmit.addElement( "Ambos" );
		vValsEmit.addElement( "S" );
		vValsEmit.addElement( "N" );
		vValsEmit.addElement( "A" );
		rgEmitidos = new JRadioGroup<String, String>( 3, 1, vLabsEmit, vValsEmit );
		rgEmitidos.setVlrString( "A" );

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

		lcVend.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.comiss.", ListaCampos.DB_PK, false ) );
		lcVend.add( new GuardaCampo( txtDescVend, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, false ) );
		lcVend.montaSql( false, "VENDEDOR", "VD" );
		lcVend.setQueryCommit( false );
		lcVend.setReadOnly( true );
		txtCodVend.setNomeCampo( "CodVend" );
		txtCodVend.setFK( true );
		txtCodVend.setTabelaExterna( lcVend, null );
		
		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo( txtNomeCli, "NomeCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		txtCodCli.setTabelaExterna( lcCli, FCliente.class.getCanonicalName() );
		txtCodCli.setNomeCampo( "CodCli" );
		txtCodCli.setFK( true );
		lcCli.setReadOnly( true );
		lcCli.montaSql( false, "CLIENTE", "VD" );


		adic( new JLabelPad( "Periodo:" ), 7, 5, 100, 20 );
		adic( lbLinha, 7, 25, 273, 40 );
		adic( new JLabelPad( "De:" ), 15, 35, 30, 20 );
		adic( txtDataini, 45, 35, 95, 20 );
		adic( new JLabelPad( "Até:" ), 145, 35, 30, 20 );
		adic( txtDatafim, 175, 35, 95, 20 );

		adic( txtCodVend, 		7, 		90, 	70, 	20, "Cód.comiss." );		
		adic( txtDescVend, 		80, 	90, 	200, 	20, "Nome do comissionado" );
		
		adic( txtCodCli, 		7, 		130, 	70, 	20, "Cód.Cli" );
		adic( txtNomeCli, 		80, 	130, 	190, 	20, "Nome do cliente" );
		
		adic( new JLabelPad( "Ordenar por:" ), 7, 155, 80, 20 );
		
		adic( rgOrdem, 			7, 		180, 	273, 	30 ); 
		adic( rgTipo, 			7, 		220, 	273, 	30 );
		adic( rgTipoCusto,		7, 		260, 	273, 	30 );
		
		adic( rgFaturados, 		7, 		295, 	125, 	70 );
		adic( rgFinanceiro, 	156, 	295, 	125, 	70 );
		
		adic( rgEmitidos,		 7,		370, 	125, 	70 );
		adic( cbVendaCanc, 		153, 	370, 	200, 	20 );
		adic( cbPorConserto,	153, 	390, 	200, 	20 );
		
	}
	
	public void imprimir( TYPE_PRINT bVisualizar ) {
		
		if("S".equals( cbPorConserto.getVlrString() )) {
			imprimirPorConserto( bVisualizar );
		}
		else {
			imprimirPorVenda( bVisualizar );
		}
		
	}

	public void imprimirPorVenda( TYPE_PRINT bVisualizar ) {

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();
		StringBuffer sCab = new StringBuffer();
		StringBuffer sWhere = new StringBuffer();

		/**
		 * @version 24/01/2008 <BR>
		 *          "Inicialização de variáveis com valor branco" - Implementação realizada por
		 * @author Pedro Henrique (pedro.enrique.contech@gmail.com) <BR>
		 *         Revisado e "commitado" por Anderson Sanchez (Setpoint Informática Ltda)
		 */

		String sWhere1 = "";
		String sWhere2 = "";
		String sWhere3 = "";
		String sWhere4 = "";

		try {
			
			boolean usareferencia = comRef();

			if ( "S".equals( rgFaturados.getVlrString() ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV='S' ";
				sCab.append( "FATURADO" );
			}
			else if ( "N".equals( rgFaturados.getVlrString() ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV='N' ";
				if ( sCab.length() > 0 ) {
					sCab.append( " - " );
				}
				sCab.append( "NAO FATURADO" );
			}
			else if ( "A".equals( rgFaturados.getVlrString() ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV IN ('S','N') ";
			}

			if ( "S".equals( rgFinanceiro.getVlrString() ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV='S' ";
				if ( sCab.length() > 0 ) {
					sCab.append( " - " );
				}
				sCab.append( "FINANCEIRO" );
			}
			else if ( "N".equals( rgFinanceiro.getVlrString() ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV='N' ";
				if ( sCab.length() > 0 ) {
					sCab.append( " - " );
				}
				sCab.append( "NAO FINANCEIRO" );
			}
			else if ( "A".equals( rgFinanceiro.getVlrString() ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV IN ('S','N') ";
			}

			if ( "N".equals( cbVendaCanc.getVlrString() ) ) {
				sWhere3 = " AND NOT SUBSTR(V.STATUSVENDA,1,1)='C' ";
			}

			if ( txtCodVend.getText().trim().length() > 0 ) {
				sWhere.append( " AND V.CODVEND=" );
				sWhere.append( txtCodVend.getText().trim() );
				sWhere.append( " AND V.CODEMPVD=" );
				sWhere.append( Aplicativo.iCodEmp );
				sWhere.append( " AND V.CODFILIALVD=" );
				sWhere.append( lcVend.getCodFilial() );
				if ( sCab.length() > 0 ) {
					sCab.append( "\n" );
				}
				sCab.append( "Representante : " );
				sCab.append( txtCodVend.getVlrString() );
				sCab.append( " - " );
				sCab.append( txtDescVend.getText().trim() );
			}
			
			if ( txtCodCli.getVlrString().trim().length() > 0 ) {
				sWhere.append (" AND V.CODCLI = " + txtCodCli.getVlrInteger());;
				sCab.append("\nCLIENTE: " + txtNomeCli.getVlrInteger());;
			}
			
			if ( rgEmitidos.getVlrString().equals( "S" ) ) {
				sWhere4 = " AND V.STATUSVENDA IN ('V2','V3','P3') " ;
				sCab.append(" EMITIDOS " );
			}
			else if ( rgEmitidos.getVlrString().equals( "N" ) ) {
				sWhere4 = " AND V.STATUSVENDA NOT IN ('V2','V3','P3') ";
				sCab.append( "NAO EMITIDOS" );
			}

			sSQL.append( "SELECT SUBSTRING(P.CODGRUP FROM 1 FOR 4) as GRUPO," );
			sSQL.append( "P.CODEMP, P.CODFILIAL, P.CODPROD,P.REFPROD," );
			
			sSQL.append( "P.DESCPROD,");
			
			sSQL.append( "G.DESCGRUP, " );

			// sSQL.append( "(SELECT NCUSTOMPM FROM EQPRODUTOSP01(P.CODEMP,P.CODFILIAL,P.CODPROD,P.CODEMPAX,P.CODFILIALAX,P.CODALMOX)) AS CUSTOMPMPROD ");

			sSQL.append( "coalesce((SELECT NCUSTOMPM FROM EQPRODUTOSP01(p.codemp,p.codfilial,p.codprod,null,null,null)),0) AS CUSTOMPMPROD " );

			sSQL.append( ",IT.CODITVENDA," );
			 
			sSQL.append( "coalesce(SUM(IT.QTDITVENDA),0) QTDITVENDA, coalesce(SUM(IT.VLRDESCITVENDA),0) VLRDESCITVENDA, coalesce(SUM(IT.VLRLIQITVENDA),0) VLRLIQITVENDA " );
			
			sSQL.append( "FROM VDVENDA V,VDITVENDA IT,EQPRODUTO P,EQGRUPO G,EQTIPOMOV TM " );
			sSQL.append( "WHERE V.DTEMITVENDA BETWEEN ? AND ? AND IT.CODEMP=? AND IT.CODFILIAL=? " );
			sSQL.append( "AND V.CODEMP=IT.CODEMP AND V.CODFILIAL=IT.CODFILIAL AND V.CODVENDA=IT.CODVENDA AND V.TIPOVENDA=IT.TIPOVENDA " );
			sSQL.append( "AND TM.CODEMP=V.CODEMPTM AND TM.CODFILIAL=V.CODFILIALTM AND TM.CODTIPOMOV=V.CODTIPOMOV " );
			sSQL.append( "AND P.CODEMP=IT.CODEMPPD AND P.CODFILIAL=IT.CODFILIALPD AND P.CODPROD=IT.CODPROD " );
			sSQL.append( "AND G.CODEMP=P.CODEMPGP AND G.CODFILIAL=P.CODFILIALGP AND G.CODGRUP=P.CODGRUP " );

			sSQL.append( sWhere );
			sSQL.append( sWhere1 );
			sSQL.append( sWhere2 );
			sSQL.append( sWhere3 );
			sSQL.append( sWhere4 );			

			sSQL.append( "AND IT.QTDITVENDA > 0 " );
			sSQL.append( "AND (V.FLAG IN " + AplicativoPD.carregaFiltro( con, org.freedom.library.swing.frame.Aplicativo.iCodEmp ) + ") " );
			sSQL.append( "AND TM.TIPOMOV IN ('VD','VE','PV','VT','SE') " );
			sSQL.append( "GROUP BY 1, 2, 3, 4, 5, 6, G.DESCGRUP, IT.CODITVENDA, P.CUSTOMPMPROD  " );
			sSQL.append( "ORDER BY 1," ); 
			sSQL.append( "C".equals( rgOrdem.getVlrString() ) ? comRef() ? "P.REFPROD" : "P.CODPROD" : "6" );

			ps = con.prepareStatement( sSQL.toString() );

			int iparam = 1;

			// ps.setInt( 1, Aplicativo.iCodEmp );
			// ps.setInt( 2, ListaCampos.getMasterFilial( "VDITVENDA" ) );

			ps.setDate( iparam++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( iparam++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			ps.setInt( iparam++, Aplicativo.iCodEmp );
			ps.setInt( iparam++, ListaCampos.getMasterFilial( "VDITVENDA" ) );

			rs = ps.executeQuery();

			if ( "T".equals( rgTipo.getVlrString() ) ) {
				imprimirTexto( bVisualizar, rs, Funcoes.strToVectorSilabas( sCab.toString(), 130 ), usareferencia );
			}
			else if ( "G".equals( rgTipo.getVlrString() ) ) {
				imprimirGrafico( bVisualizar, rs, sCab.toString() ); 
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro consulta ao relatório de vendas fisico!" + err.getMessage() );
		} finally {
			System.gc();
		}
	}

	public void imprimirPorConserto( TYPE_PRINT bVisualizar ) {

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();
		StringBuffer sCab = new StringBuffer();
		StringBuffer sWhere = new StringBuffer();

		/**
		 * @version 24/01/2008 <BR>
		 *          "Inicialização de variáveis com valor branco" - Implementação realizada por
		 * @author Pedro Henrique (pedro.enrique.contech@gmail.com) <BR>
		 *         Revisado e "commitado" por Anderson Sanchez (Setpoint Informática Ltda)
		 */

		String sWhere1 = "";
		String sWhere2 = "";
		String sWhere3 = "";
		String sWhere4 = "";

		try {
			
			boolean usareferencia = comRef();

			if ( "S".equals( rgFaturados.getVlrString() ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV='S' ";
				sCab.append( "FATURADO" );
			}
			else if ( "N".equals( rgFaturados.getVlrString() ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV='N' ";
				if ( sCab.length() > 0 ) {
					sCab.append( " - " );
				}
				sCab.append( "NAO FATURADO" );
			}
			else if ( "A".equals( rgFaturados.getVlrString() ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV IN ('S','N') ";
			}

			if ( "S".equals( rgFinanceiro.getVlrString() ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV='S' ";
				if ( sCab.length() > 0 ) {
					sCab.append( " - " );
				}
				sCab.append( "FINANCEIRO" );
			}
			else if ( "N".equals( rgFinanceiro.getVlrString() ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV='N' ";
				if ( sCab.length() > 0 ) {
					sCab.append( " - " );
				}
				sCab.append( "NAO FINANCEIRO" );
			}
			else if ( "A".equals( rgFinanceiro.getVlrString() ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV IN ('S','N') ";
			}

			if ( "N".equals( cbVendaCanc.getVlrString() ) ) {
				sWhere3 = " AND NOT SUBSTR(V.STATUSVENDA,1,1)='C' ";
			}

			if ( txtCodVend.getText().trim().length() > 0 ) {
				sWhere.append( " AND V.CODVEND=" );
				sWhere.append( txtCodVend.getText().trim() );
				sWhere.append( " AND V.CODEMPVD=" );
				sWhere.append( Aplicativo.iCodEmp );
				sWhere.append( " AND V.CODFILIALVD=" );
				sWhere.append( lcVend.getCodFilial() );
				if ( sCab.length() > 0 ) {
					sCab.append( "\n" );
				}
				sCab.append( "Representante : " );
				sCab.append( txtCodVend.getVlrString() );
				sCab.append( " - " );
				sCab.append( txtDescVend.getText().trim() );
			}
			
			if ( txtCodCli.getVlrString().trim().length() > 0 ) {
				sWhere.append (" AND V.CODCLI = " + txtCodCli.getVlrInteger());;
				sCab.append("\nCLIENTE: " + txtNomeCli.getVlrInteger());;
			}
			
			if ( rgEmitidos.getVlrString().equals( "S" ) ) {
				sWhere4 = " AND V.STATUSVENDA IN ('V2','V3','P3') " ;
				sCab.append(" EMITIDOS " );
			}
			else if ( rgEmitidos.getVlrString().equals( "N" ) ) {
				sWhere4 = " AND V.STATUSVENDA NOT IN ('V2','V3','P3') ";
				sCab.append( "NAO EMITIDOS" );
			}

			sSQL.append( "select ");
//			sSQL.append( "coalesce(pd2.codgrup, pd1.codgrup) codgrup, ");
			sSQL.append( "substring(coalesce(pd2.codgrup, pd1.codgrup) from 1 for 4) grupo, ");
			sSQL.append( "coalesce(pd2.codemp, pd1.codemp) codemp , coalesce(pd2.codfilial, pd1.codfilial) codfilial, ");
			sSQL.append( "coalesce(pd2.codprod,pd1.codprod) codprod, coalesce(pd2.refprod,pd1.refprod) refprod, ");
			sSQL.append( "coalesce(pd2.descprod,pd1.descprod) descprod, coalesce(g2.descgrup,g1.descgrup) descgrup, ");
			sSQL.append( "(coalesce((select first 1 ");
			
			sSQL.append( rgTipoCusto.getVlrString() );
			
 			sSQL.append(" from eqprodutosp01(pd2.codemp, pd2.codfilial, pd2.codprod,null,null,null)),0)) custompmprod, ");
			sSQL.append( "iv.coditvenda, ");
			sSQL.append( "coalesce(sum(iv.qtditvenda),0) qtditvenda, ");
			sSQL.append( "coalesce(sum(iv.vlrdescitvenda),0) vlrdescitvenda, ");
			sSQL.append( "coalesce(sum(iv.vlrliqitvenda),0) vlrliqitvenda ");

			sSQL.append( "from eqtipomov tm, vdvenda v, vditvenda iv "); 
			sSQL.append( "left outer join eqproduto pd1 on ");
			sSQL.append( "pd1.codemp=iv.codemppd and pd1.codfilial=iv.codfilialpd and pd1.codprod=iv.codprod ");
			sSQL.append( "left outer join eqgrupo g1 on ");
			sSQL.append( "g1.codemp=pd1.codempgp and g1.codfilial=pd1.codfilialgp and g1.codgrup=pd1.codgrup ");
			sSQL.append( "left outer join vdvendaorc vo on ");
			sSQL.append( "vo.codemp=iv.codemp and vo.codfilial=iv.codfilial and vo.codvenda=iv.codvenda and vo.tipovenda=iv.tipovenda and vo.coditvenda=iv.coditvenda ");
			sSQL.append( "left outer join eqitrecmercitositorc iro on ");
			sSQL.append( "iro.codempoc=vo.codempor and iro.codfilialoc=vo.codfilialor and iro.codorc=vo.codorc and iro.tipoorc=vo.tipoorc and iro.coditorc=vo.coditorc ");
			sSQL.append( "left outer join eqitrecmerc ir on ");
			sSQL.append( "ir.codemp=iro.codemp and ir.codfilial=iro.codfilial and ir.ticket=iro.ticket and ir.coditrecmerc=iro.coditrecmerc ");
			sSQL.append( "left outer join eqproduto pd2 on ");
			sSQL.append( "pd2.codemp=ir.codemppd and pd2.codfilial=ir.codfilialpd and pd2.codprod=ir.codprod ");
			sSQL.append( "left outer join eqgrupo g2 on ");
			sSQL.append( "g2.codemp=pd2.codempgp and g2.codfilial=pd2.codfilialgp and g2.codgrup=pd2.codgrup " );
			
			sSQL.append( "where V.DTEMITVENDA BETWEEN ? AND ? AND Iv.CODEMP=? AND Iv.CODFILIAL=? and ");
			sSQL.append( "tm.codemp=v.codemptm and tm.codfilial=v.codfilialtm and tm.codtipomov=v.codtipomov " );
			sSQL.append( "and iv.codemp=v.codemp and iv.codfilial=v.codfilial and iv.tipovenda=v.tipovenda and iv.codvenda=v.codvenda " );
						
			sSQL.append( sWhere );
			sSQL.append( sWhere1 );
			sSQL.append( sWhere2 );
			sSQL.append( sWhere3 ); 
			sSQL.append( sWhere4 );			

			sSQL.append( "AND iv.QTDITVENDA > 0 " );
			sSQL.append( "AND (V.FLAG IN " + AplicativoPD.carregaFiltro( con, org.freedom.library.swing.frame.Aplicativo.iCodEmp ) + ") " );
			sSQL.append( "AND TM.TIPOMOV IN ('VD','VE','PV','VT','SE') " );
			
			sSQL.append( "group by 1, 2, 3, 4, 5, 6, 7 , Pd2.codemp, pd2.codfilial, pd2.codprod, 9 " ); 
			sSQL.append( "ORDER BY 1," ); 
			sSQL.append( "C".equals( rgOrdem.getVlrString() ) ? comRef() ? "5" : "4" : "6" );
			
			System.out.println("SQL por conserto:" + sSQL.toString());
			
			ps = con.prepareStatement( sSQL.toString() );

			int iparam = 1;

			// ps.setInt( 1, Aplicativo.iCodEmp );
			// ps.setInt( 2, ListaCampos.getMasterFilial( "VDITVENDA" ) );

			ps.setDate( iparam++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( iparam++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			ps.setInt( iparam++, Aplicativo.iCodEmp );
			ps.setInt( iparam++, ListaCampos.getMasterFilial( "VDITVENDA" ) );

	
			
			rs = ps.executeQuery();
			
			if ( "T".equals( rgTipo.getVlrString() ) ) {
				imprimirTexto( bVisualizar, rs, Funcoes.strToVectorSilabas( sCab.toString(), 130 ), usareferencia );
			}
			else if ( "G".equals( rgTipo.getVlrString() ) ) {
				imprimirGrafico( bVisualizar, rs, sCab.toString() ); 
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro consulta ao relatório de vendas fisico!" + err.getMessage() );
		} finally {
			System.gc();
		}
	}

	
	
	
	private void imprimirTexto( final TYPE_PRINT bVisualizar, final ResultSet rs, final Vector<?> cab, boolean usareferencia ) {

		String sCodGrup = null;
		String sGrup = null;
		final String sLinhaFina = StringFunctions.replicate( "-", 133 );
		final String sLinhaDupla = StringFunctions.replicate( "=", 133 );
		ImprimeOS imp = null;
		int linPag = 0;

		try {

			imp = new ImprimeOS( "", con );
			linPag = imp.verifLinPag() - 1;
			imp.limpaPags();
			imp.montaCab();
			imp.setTitulo( "Relatório Fisco de Vendas" );
			imp.addSubTitulo( "RELATORIO FISICO DE VENDAS   -   PERIODO DE :" + txtDataini.getVlrString() + " ATE: " + txtDatafim.getVlrString() );
			for ( int i = 0; i < cab.size(); i++ ) {
				imp.addSubTitulo( (String) cab.elementAt( i ) );
			}

			/*
			 * 1 GRUPO 2 CODPROD 3 REFPROD 4 DESCPROD 5 DESCGRUP 6 CUSTOMPMPROD 7 CODITVENDA," ); 8 SUM(IT.QTDITVENDA) QTDITVENDA 9 SUM(IT.VLRDESCITVENDA) VLRDESCITVENDA 10 SUM(IT.VLRLIQITVENDA) VLRLIQITVENDA
			 */

			BigDecimal bQtdVenda = new BigDecimal( "0" );
			BigDecimal bDescVenda = new BigDecimal( "0" );
			BigDecimal bVlrUnitVenda = new BigDecimal( "0" );
			BigDecimal bVlrTotVenda = new BigDecimal( "0" );
			BigDecimal bVlrUnitCusto = new BigDecimal( "0" );
			BigDecimal bVlrTotCusto = new BigDecimal( "0" );
			BigDecimal bVlrUnitLucro = new BigDecimal( "0" );
			BigDecimal bVlrTotLucro = new BigDecimal( "0" );
			BigDecimal bMargem = new BigDecimal( "0" );

			BigDecimal bTotalVendaGrupo = new BigDecimal( "0" );
			BigDecimal bTotalCustoGrupo = new BigDecimal( "0" );
			BigDecimal bTotalDescGrupo = new BigDecimal( "0" );
			BigDecimal bTotalLucroGrupo = new BigDecimal( "0" );
			BigDecimal bMargemGrupo = new BigDecimal( "0" );

			BigDecimal bTotalVenda = new BigDecimal( "0" );
			BigDecimal bTotalCusto = new BigDecimal( "0" );
			BigDecimal bTotalDesc = new BigDecimal( "0" );
			BigDecimal bTotalLucro = new BigDecimal( "0" );
			BigDecimal bMargemTotal = new BigDecimal( "0" );

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
					imp.say( 0, "|" );
					imp.say( 15, "|" );
					imp.say( 46, "|" );
					imp.say( 60, "V E N D A" );
					imp.say( 79, "|" );
					imp.say( 87, "C U S T O" );
					imp.say( 103, "|" );
					imp.say( 113, "LUCRO ESTIMADO" );
					imp.say( 135, "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" );
					imp.say( 3, "Referência" );
					imp.say( 15, "|" );
					imp.say( 16, "Descrição" );
					imp.say( 46, "|" );
					imp.say( 49, "Qtd." );
					imp.say( 56, "Vlr. Unit." );
					imp.say( 68, "Vlr. Total" );
					imp.say( 79, "|" );
					imp.say( 81, "Vlr. Unit. " );
					imp.say( 92, "Vlr. Total" );
					imp.say( 103, "|" );
					imp.say( 106, "P/ Unid. " );
					imp.say( 116, "Vlr. Total " );
					imp.say( 128, "Margem" );
					imp.say( 135, "|" );
				}

				imp.pulaLinha( 1, imp.comprimido() );

				if ( sCodGrup == null || !sCodGrup.equals( rs.getString( 1 ) ) ) {

					if ( sCodGrup != null && !sCodGrup.equals( rs.getString( 1 ) ) ) {

						if ( bTotalVendaGrupo.floatValue() > 0 ) {
							bMargemGrupo = new BigDecimal( bTotalLucroGrupo.floatValue() / ( bTotalVendaGrupo.floatValue() / 100 ) ).setScale( 2, BigDecimal.ROUND_HALF_UP );
						}
						else {
							bMargemGrupo = new BigDecimal( "100" );
						}

						imp.say( 0, "|" + sLinhaFina + "|" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|" );
						imp.say( 15, "|" );
						imp.say( 23, "Totais do Grupo :" );
						imp.say( 46, "|VENDA" );
						imp.say( 65, Funcoes.strDecimalToStrCurrency( 14, 2, String.valueOf( bTotalVendaGrupo ) ) );
						imp.say( 79, "|CUSTO" );
						imp.say( 89, Funcoes.strDecimalToStrCurrency( 14, 2, String.valueOf( bTotalCustoGrupo ) ) );
						imp.say( 103, "|LUCRO" );
						imp.say( 110, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( bTotalLucroGrupo ) ) );
						imp.say( 121, "MARGEN" );
						imp.say( 128, Funcoes.strDecimalToStrCurrency( 5, 2, String.valueOf( bMargemGrupo ) ) );
						imp.say( 134, "%|" );
						imp.pulaLinha( 1, imp.comprimido() );

						bTotalVenda = bTotalVenda.add( bTotalVendaGrupo );
						bTotalCusto = bTotalCusto.add( bTotalCustoGrupo );
						bTotalDesc = bTotalDesc.add( bTotalDescGrupo );
						bTotalLucro = bTotalLucro.add( bTotalLucroGrupo );

						bTotalVendaGrupo = new BigDecimal( "0" );
						bTotalCustoGrupo = new BigDecimal( "0" );
						bTotalDescGrupo = new BigDecimal( "0" );
						bTotalLucroGrupo = new BigDecimal( "0" );
					}

					sCodGrup = rs.getString( 1 );

					sGrup = "GRUPO: " + sCodGrup + " - " + rs.getString( "DescGrup" );

					imp.say( 0, "|" + sLinhaFina + "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" );
					imp.say( ( 135 - sGrup.length() ) / 2, sGrup );
					imp.say( 135, "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + sLinhaFina + "|" );
					imp.pulaLinha( 1, imp.comprimido() );
				}

				bQtdVenda = rs.getBigDecimal( 10 ) != null ? rs.getBigDecimal( 10 ).setScale( 2, BigDecimal.ROUND_HALF_UP ) : new BigDecimal( "0" );
				bDescVenda = rs.getBigDecimal( 11 ) != null ? rs.getBigDecimal( 11 ).setScale( 2, BigDecimal.ROUND_HALF_UP ) : new BigDecimal( "0" );
				bVlrTotVenda = rs.getBigDecimal( 12 ) != null ? rs.getBigDecimal( 12 ).setScale( 2, BigDecimal.ROUND_HALF_UP ) : new BigDecimal( "0" );
				bVlrUnitVenda = bVlrTotVenda.divide( bQtdVenda, 2, BigDecimal.ROUND_HALF_UP );
				bVlrUnitCusto = rs.getBigDecimal( 8 ) != null ? rs.getBigDecimal( 8 ).setScale( 2, BigDecimal.ROUND_HALF_UP ) : new BigDecimal( "0" );
				bVlrTotCusto = bVlrUnitCusto.multiply( bQtdVenda ).setScale( 2, BigDecimal.ROUND_HALF_UP );
				bVlrUnitLucro = bVlrUnitVenda.subtract( bVlrUnitCusto ).setScale( 2, BigDecimal.ROUND_HALF_UP );
				bVlrTotLucro = bVlrTotVenda.subtract( bVlrTotCusto ).setScale( 2, BigDecimal.ROUND_HALF_UP );

				if ( bVlrTotVenda.floatValue() > 0 ) {
					bMargem = new BigDecimal( bVlrTotLucro.floatValue() / ( bVlrTotVenda.floatValue() / 100 ) ).setScale( 2, BigDecimal.ROUND_HALF_UP );
				}
				else {
					bMargem = new BigDecimal( "100" );
				}

				imp.say( 0, "|" );
				
				if(usareferencia) {
					imp.say( 2, rs.getString( "refprod" ) + "|" );	
				}
				else {
					imp.say( 2, rs.getString( "codprod" ) + "|" );
				}
				
				imp.say( 16, Funcoes.copy( rs.getString( "DescProd" ), 0, 30 ) + "|" );
				imp.say( 47, Funcoes.strDecimalToStrCurrency( 8, 2, String.valueOf( bQtdVenda ) ) );
				imp.say( 57, Funcoes.strDecimalToStrCurrency( 8, 2, String.valueOf( bVlrUnitVenda ) ) );
				imp.say( 67, Funcoes.strDecimalToStrCurrency( 12, 2, String.valueOf( bVlrTotVenda ) ) + "|" );
				imp.say( 81, Funcoes.strDecimalToStrCurrency( 8, 2, String.valueOf( bVlrUnitCusto ) ) );
				imp.say( 91, Funcoes.strDecimalToStrCurrency( 12, 2, String.valueOf( bVlrTotCusto ) ) + "|" );
				imp.say( 105, Funcoes.strDecimalToStrCurrency( 8, 2, String.valueOf( bVlrUnitLucro ) ) );
				imp.say( 115, Funcoes.strDecimalToStrCurrency( 12, 2, String.valueOf( bVlrTotLucro ) ) );
				imp.say( 128, Funcoes.strDecimalToStrCurrency( 5, 2, String.valueOf( bMargem ) ) );
				imp.say( 134, "%|" );

				bTotalVendaGrupo = bTotalVendaGrupo.add( bVlrTotVenda );
				bTotalCustoGrupo = bTotalCustoGrupo.add( bVlrTotCusto );
				bTotalDescGrupo = bTotalDescGrupo.add( bDescVenda );
				bTotalLucroGrupo = bTotalLucroGrupo.add( bVlrTotLucro );
			}

			if ( bTotalVendaGrupo.floatValue() > 0 ) {
				bMargemGrupo = new BigDecimal( bTotalLucroGrupo.floatValue() / ( bTotalVendaGrupo.floatValue() / 100 ) ).setScale( 2, BigDecimal.ROUND_HALF_UP );
			}
			else {
				bMargemGrupo = new BigDecimal( "100" );
			}

			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "|" + sLinhaFina + "|" );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "|" );
			imp.say( 15, "|" );
			imp.say( 23, "Totais do Grupo :" );
			imp.say( 46, "|VENDA" );
			imp.say( 65, Funcoes.strDecimalToStrCurrency( 14, 2, String.valueOf( bTotalVendaGrupo ) ) );
			imp.say( 79, "|CUSTO" );
			imp.say( 89, Funcoes.strDecimalToStrCurrency( 14, 2, String.valueOf( bTotalCustoGrupo ) ) );
			imp.say( 103, "|LUCRO" );
			imp.say( 110, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( bTotalLucroGrupo ) ) );
			imp.say( 121, "MARGEN" );
			imp.say( 128, Funcoes.strDecimalToStrCurrency( 5, 2, String.valueOf( bMargemGrupo ) ) );
			imp.say( 134, "%|" );

			bTotalVenda = bTotalVenda.add( bTotalVendaGrupo );
			bTotalCusto = bTotalCusto.add( bTotalCustoGrupo );
			bTotalDesc = bTotalDesc.add( bTotalDescGrupo );
			bTotalLucro = bTotalLucro.add( bTotalLucroGrupo );

			if ( bTotalVenda.floatValue() > 0 ) {
				bMargemTotal = new BigDecimal( bTotalLucro.floatValue() / ( bTotalVenda.floatValue() / 100 ) ).setScale( 2, BigDecimal.ROUND_HALF_UP );
			}
			else {
				bMargemTotal = new BigDecimal( "100" );
			}

			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "+" + sLinhaDupla + "+" );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "|" );
			imp.say( 51, "R E S U M O  G E R A L" );
			imp.say( 135, "|" );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "|" );
			imp.say( 30, "VENDA TOTAL:    " + Funcoes.strDecimalToStrCurrency( 16, 2, String.valueOf( bTotalVenda ) ) );
			imp.say( 64, "LUCRO ESTIMADO: " + Funcoes.strDecimalToStrCurrency( 16, 2, String.valueOf( bTotalLucro ) ) );
			imp.say( 135, "|" );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "|" );
			imp.say( 30, "CUSTO TOTAL:    " + Funcoes.strDecimalToStrCurrency( 16, 2, String.valueOf( bTotalCusto ) ) );
			imp.say( 64, "MARGEM MEDIA:   " + Funcoes.strDecimalToStrCurrency( 16, 2, String.valueOf( bMargemTotal ) ) + "%" );
			imp.say( 135, "|" );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "|" );
			imp.say( 30, "DESCONTOS:      " + Funcoes.strDecimalToStrCurrency( 16, 2, String.valueOf( bTotalDesc ) ) );
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
		} catch ( Exception err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro consulta ao relatório de vendas fisico!" + err.getMessage() );
		}
	}

	private void imprimirGrafico( final TYPE_PRINT bVisualizar, final ResultSet rs, final String sCab ) {

		FPrinterJob dlGr = null;

		dlGr = new FPrinterJob( "relatorios/VendasFisico.jasper", "Vendas Fisico - resumido", sCab, rs, null, this );

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

	private boolean comRef() {

		boolean bRetorno = false;
		String sSQL = "SELECT USAREFPROD FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			rs = ps.executeQuery();
			if ( rs.next() )
				if ( rs.getString( "UsaRefProd" ).trim().equals( "S" ) )
					bRetorno = true;
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage(), true, con, err );
		} finally {
			sSQL = null;
			ps = null;
			rs = null;
		}
		return bRetorno;
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcVend.setConexao( con );
		lcCli.setConexao( con );
	}
}
