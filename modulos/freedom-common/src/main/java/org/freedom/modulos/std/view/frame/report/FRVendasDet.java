/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FRVendasDet.java <BR>
 * 
 *                      Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                      modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                      na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                      Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                      sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                      Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                      Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                      de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                      Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.std.view.frame.report;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
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

public class FRVendasDet extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtCidCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtIdUsu = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );
	
	private JTextFieldPad txtRefProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldFK txtNomeVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JCheckBoxPad cbVendaCanc = new JCheckBoxPad( "Mostrar Canceladas", "S", "N" );

	private JCheckBoxPad cbVendaSubcli = new JCheckBoxPad( "Listar sub-clientes", "S", "N" );

	private JRadioGroup<String, String> rgTipo = null;

	private JRadioGroup<String, String> rgFaturados = null;

	private JRadioGroup<String, String> rgFinanceiro = null;

	private JRadioGroup<String, String> rgOrdem = null;

	private ListaCampos lcCliente = new ListaCampos( this );

	private ListaCampos lcProd = new ListaCampos( this );

	private ListaCampos lcVend = new ListaCampos( this );
	
	private JCheckBoxPad cbPorConserto = new JCheckBoxPad( "Por item de O.S", "S", "N" );
	
	private JRadioGroup<?, ?> rgEmitidos = null;
	
	private Vector<String> vLabsEmit = new Vector<String>();

	private Vector<String> vValsEmit = new Vector<String>();

	public FRVendasDet() {

		setTitulo( "Vendas Detalhadas" );
		setAtribos( 80, 80, 520, 480 );

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

		vLabs1.addElement( "Faturado" );
		vLabs1.addElement( "Não Faturado" );
		vLabs1.addElement( "Ambos" );
		vVals1.addElement( "S" );
		vVals1.addElement( "N" );
		vVals1.addElement( "A" );
		rgFaturados = new JRadioGroup<String, String>( 3, 1, vLabs1, vVals1 );
		rgFaturados.setVlrString( "S" );

		Vector<String> vLabs2 = new Vector<String>();
		Vector<String> vVals2 = new Vector<String>();

		vLabs2.addElement( "Financeiro" );
		vLabs2.addElement( "Não Finaceiro" );
		vLabs2.addElement( "Ambos" );
		vVals2.addElement( "S" );
		vVals2.addElement( "N" );
		vVals2.addElement( "A" );
		rgFinanceiro = new JRadioGroup<String, String>( 3, 1, vLabs2, vVals2 );
		rgFinanceiro.setVlrString( "S" );

		Vector<String> vLabs3 = new Vector<String>();
		Vector<String> vVals3 = new Vector<String>();

		vLabs3.addElement( "Data de emissão" );
		vLabs3.addElement( "Data de saída" );
		vVals3.addElement( "V.DTEMITVENDA " );
		vVals3.addElement( "V.DTSAIDAVENDA " );
		rgOrdem = new JRadioGroup<String, String>( 2, 1, vLabs3, vVals3 );
		rgOrdem.setVlrString( "S" );

		
		vLabsEmit.addElement( "Emitidos" );
		vLabsEmit.addElement( "Não emitidos" );
		vLabsEmit.addElement( "Ambos" );
		vValsEmit.addElement( "S" );
		vValsEmit.addElement( "N" );
		vValsEmit.addElement( "A" );
		rgEmitidos = new JRadioGroup<String, String>( 3, 1, vLabsEmit, vValsEmit );
		rgEmitidos.setVlrString( "A" );


		lcCliente.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCliente.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		txtCodCli.setTabelaExterna( lcCliente, null );
		txtCodCli.setNomeCampo( "CodCli" );
		txtCodCli.setFK( true );
		lcCliente.setReadOnly( true );
		lcCliente.montaSql( false, "CLIENTE", "VD" );

		lcProd.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcProd.add( new GuardaCampo( txtRefProd, "RefProd", "Referência do produto", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		
		txtCodProd.setTabelaExterna( lcProd, null );
		txtCodProd.setNomeCampo( "CodProd" );
		txtCodProd.setFK( true );
		lcProd.setReadOnly( true );
		lcProd.montaSql( false, "PRODUTO", "EQ" );

		lcVend.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.comiss.", ListaCampos.DB_PK, false ) );
		lcVend.add( new GuardaCampo( txtNomeVend, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, false ) );
		txtCodVend.setTabelaExterna( lcVend, null );
		txtCodVend.setNomeCampo( "CodCli" );
		txtCodVend.setFK( true );
		lcVend.setReadOnly( true );
		lcVend.montaSql( false, "VENDEDOR", "VD" );

		txtDataini.setRequerido( true );
		txtDatafim.setRequerido( true );

		GregorianCalendar cal = new GregorianCalendar();
		txtDatafim.setVlrDate( cal.getTime() );
		cal.roll( Calendar.MONTH, -1 );
		txtDataini.setVlrDate( cal.getTime() );

		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );

		adic( new JLabelPad( "Periodo:" ), 7, 5, 100, 20 );
		adic( lbLinha, 60, 15, 210, 2 );
		adic( new JLabelPad( "De:" ), 7, 30, 30, 20 );
		adic( txtDataini, 32, 30, 97, 20 );
		adic( new JLabelPad( "Até:" ), 140, 30, 30, 20 );
		adic( txtDatafim, 170, 30, 100, 20 );

		adic( new JLabelPad( "Cidade do Cliente" ), 283, 5, 130, 20 );
		adic( txtCidCli, 283, 30, 130, 20 );
		
		adic( new JLabelPad( "Cód.cli." ), 7, 60, 80, 20 );
		adic( txtCodCli, 7, 80, 80, 20 );
		adic( new JLabelPad( "Razão social do cliente" ), 90, 60, 320, 20 );
		adic( txtRazCli, 90, 80, 325, 20 );
		adic( new JLabelPad( "Cód.prod." ), 7, 100, 80, 20 );
		adic( txtCodProd, 7, 120, 80, 20 );
		adic( new JLabelPad( "Descrição do produto" ), 90, 100, 325, 20 );
		adic( txtDescProd, 90, 120, 325, 20 );
		adic( new JLabelPad( "Cód.comiss." ), 7, 140, 80, 20 );
		adic( txtCodVend, 7, 160, 80, 20 );
		adic( new JLabelPad( "Nome do comissionado" ), 90, 140, 325, 20 );
		adic( txtNomeVend, 90, 160, 325, 20 );

		adic( new JLabelPad( "Usuário" ), 7, 180, 132, 20 );
		adic( txtIdUsu, 7, 200, 132, 20 );
		
		adic( rgTipo, 		150, 		190, 	265, 	30 );
		adic( rgFaturados, 	7, 		230, 	120, 	70 );
		adic( rgFinanceiro, 153,	230, 	120, 	70 );
		adic( rgEmitidos,	293,	230, 	120, 	70 );
		
		adic( cbVendaSubcli, 7,	310, 	180, 	20 );
		adic( cbVendaCanc, 	 7,	330, 	180, 	20 );
		adic( cbPorConserto, 7, 350, 	180, 	20 );

		JLabelPad lbOrdem = new JLabelPad(" Ordem ");
		lbOrdem.setOpaque( true );
		adic( lbOrdem, 270, 300, 80, 20 );
		adic( rgOrdem, 260, 320, 	150, 	50 );
		
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
		String sWhere1 = null;
		String sWhere2 = null;
		String sWhere3 = "";
		String sWhere4 = "";
		String sWhere5 = "";
		String sWhere6 = "";
		String sWhere7 = "";
		String sWhere8 = "";

		boolean bComRef = comRef();

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
			if ( txtCodCli.getVlrInteger().intValue() > 0 ) {
				if ( sCab.length() > 0 ) {
					sCab.append( "\n" );
				}
				if ( cbVendaSubcli.getVlrString().equals( "S" ) ) {
					sWhere6 = " AND C.CODPESQ=" + txtCodCli.getVlrInteger().intValue() + " ";
					sCab.append( "Cliente principal: " );
				}
				else {
					sWhere4 = " AND C.CODCLI=" + txtCodCli.getVlrInteger().intValue() + " ";
					sCab.append( "Cliente : " );
				}
				sCab.append( txtRazCli.getVlrString() );
			}
			if ( txtCodProd.getVlrInteger().intValue() > 0 ) {
				sWhere5 = " AND IT.CODPROD=" + txtCodProd.getVlrInteger().intValue() + " ";
				if ( sCab.length() > 0 ) {
					sCab.append( "\n" );
				}
				sCab.append( "Produto : " );
				sCab.append( txtDescProd.getVlrString() );
			}
			if ( txtCodVend.getVlrInteger().intValue() > 0 ) {
				sWhere5 = " AND V.CODVEND=" + txtCodVend.getVlrInteger().intValue() + " ";
				if ( sCab.length() > 0 ) {
					sCab.append( "\n" );
				}
				sCab.append( "Comissionado :" );
				sCab.append( txtNomeVend.getVlrString() );
			}
			if ( !"".equals( txtCidCli.getVlrString().trim() ) ) {
				sWhere7 = " AND C.CIDCLI='" + txtCidCli.getVlrString().trim() + "'";

				sCab.append( " - CID.:  " );
				sCab.append( txtCidCli.getVlrString() );
			}

			if ( !"".equals( txtIdUsu.getVlrString().trim() ) ) {
				sWhere7 = " AND V.IDUSUINS='" + txtIdUsu.getVlrString().trim().toUpperCase() + "'";

				sCab.append( " - USU.:  " );
				sCab.append( txtIdUsu.getVlrString().trim().toUpperCase() );
			}
			
			if ( rgEmitidos.getVlrString().equals( "S" ) ) {
				sWhere8 = " AND V.STATUSVENDA IN ('V2','V3','P3') " ;
				sCab.append(" EMITIDOS " );
			}
			else if ( rgEmitidos.getVlrString().equals( "N" ) ) {
				sWhere8 = " AND V.STATUSVENDA NOT IN ('V2','V3','P3') ";
				sCab.append( "NAO EMITIDOS" );
			}

			sSQL.append( "SELECT " );
			sSQL.append( "	( SELECT VO.CODORC FROM VDVENDAORC VO " );
			sSQL.append( "	  WHERE VO.CODEMP=IT.CODEMP AND VO.CODFILIAL=IT.CODFILIAL " );
			sSQL.append( "	  AND VO.CODVENDA=IT.CODVENDA AND VO.CODITVENDA=IT.CODITVENDA AND VO.TIPOVENDA=IT.TIPOVENDA ) AS CODORC, " );
			sSQL.append( "V.CODVENDA,V.DOCVENDA,V.DTEMITVENDA,V.DTSAIDAVENDA,PP.DESCPLANOPAG,V.CODCLI," );
			sSQL.append( "C.RAZCLI,V.VLRDESCVENDA,V.VLRLIQVENDA,IT.CODPROD,IT.REFPROD,");
			
			sSQL.append( "P.DESCPROD,");
			
			sSQL.append( "IT.CODLOTE," );
			sSQL.append( "IT.QTDITVENDA,IT.PRECOITVENDA,IT.VLRDESCITVENDA,IT.VLRLIQITVENDA, " );
			sSQL.append( "V.CODVEND, VE.NOMEVEND " );
			sSQL.append( "FROM VDVENDA V, FNPLANOPAG PP, VDCLIENTE C, VDITVENDA IT, EQPRODUTO P, EQTIPOMOV TM, VDVENDEDOR VE  " );
			sSQL.append( "WHERE V.DTEMITVENDA BETWEEN ? AND ? AND V.CODEMP=? AND V.CODFILIAL=? " );
			sSQL.append( "AND PP.CODEMP=V.CODEMPPG AND PP.CODFILIAL=V.CODFILIALPG AND PP.CODPLANOPAG=V.CODPLANOPAG " );
			sSQL.append( "AND C.CODEMP=V.CODEMPCL AND C.CODFILIAL=V.CODFILIALCL AND C.CODCLI=V.CODCLI " );
			sSQL.append( "AND TM.CODEMP=V.CODEMPTM AND TM.CODFILIAL=V.CODFILIALTM AND TM.CODTIPOMOV=V.CODTIPOMOV " );
			sSQL.append( "AND IT.CODEMP=V.CODEMP AND IT.CODFILIAL=V.CODFILIAL AND IT.CODVENDA=V.CODVENDA AND IT.TIPOVENDA=V.TIPOVENDA " );
			sSQL.append( "AND P.CODEMP=IT.CODEMPPD AND P.CODFILIAL=IT.CODFILIALPD AND P.CODPROD=IT.CODPROD " );
			sSQL.append( "AND VE.CODEMP=V.CODEMPVD AND VE.CODFILIAL=V.CODFILIALVD AND VE.CODVEND=V.CODVEND ");
			
			sSQL.append( sWhere1 );
			sSQL.append( sWhere2 );
			sSQL.append( sWhere3 );
			sSQL.append( sWhere4 );
			sSQL.append( sWhere5 );
			sSQL.append( sWhere6 );
			sSQL.append( sWhere7 );
			sSQL.append( sWhere8 );
			
			sSQL.append( "ORDER BY ");
			sSQL.append( rgOrdem.getVlrString() );
			sSQL.append( ", V.CODVENDA, IT.CODITVENDA " );

			ps = con.prepareStatement( sSQL.toString() );
			ps.setDate( 1, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( 2, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "VDVENDA" ) );
			rs = ps.executeQuery();

			if ( "T".equals( rgTipo.getVlrString() ) ) {
				imprimirTexto( bVisualizar, rs, sCab.toString(), bComRef );
			}
			else if ( "G".equals( rgTipo.getVlrString() ) ) {
				imprimirGrafico( bVisualizar, rs, sCab.toString(), bComRef );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( Exception err ) {
			Funcoes.mensagemErro( this, "Erro ao montar relatorio!" + err.getMessage(), true, con, err );
			err.printStackTrace();
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
		String sWhere1 = null;
		String sWhere2 = null;
		String sWhere3 = "";
		String sWhere4 = "";
		String sWhere5 = "";
		String sWhere6 = "";
		String sWhere7 = "";
		String sWhere8 = "";

		boolean bComRef = comRef();

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
			if ( txtCodCli.getVlrInteger().intValue() > 0 ) {
				if ( sCab.length() > 0 ) {
					sCab.append( "\n" );
				}
				if ( cbVendaSubcli.getVlrString().equals( "S" ) ) {
					sWhere6 = " AND C.CODPESQ=" + txtCodCli.getVlrInteger().intValue() + " ";
					sCab.append( "Cliente principal: " );
				}
				else {
					sWhere4 = " AND C.CODCLI=" + txtCodCli.getVlrInteger().intValue() + " ";
					sCab.append( "Cliente : " );
				}
				sCab.append( txtRazCli.getVlrString() );
			}
			if ( txtCodProd.getVlrInteger().intValue() > 0 ) {
				sWhere5 = " AND coalesce(pd2.CODPROD,pd1.codprod)=" + txtCodProd.getVlrInteger().intValue() + " ";
				if ( sCab.length() > 0 ) {
					sCab.append( "\n" );
				}
				sCab.append( "Produto : " );
				sCab.append( txtDescProd.getVlrString() );
			}
			if ( txtCodVend.getVlrInteger().intValue() > 0 ) {
				sWhere5 = " AND V.CODVEND=" + txtCodVend.getVlrInteger().intValue() + " ";
				if ( sCab.length() > 0 ) {
					sCab.append( "\n" );
				}
				sCab.append( "Comissionado :" );
				sCab.append( txtNomeVend.getVlrString() );
			}
			if ( !"".equals( txtCidCli.getVlrString().trim() ) ) {
				sWhere7 = " AND C.CIDCLI='" + txtCidCli.getVlrString().trim() + "'";

				sCab.append( " - CID.:  " );
				sCab.append( txtCidCli.getVlrString() );
			}

			if ( !"".equals( txtIdUsu.getVlrString().trim() ) ) {
				sWhere7 = " AND V.IDUSUINS='" + txtIdUsu.getVlrString().trim().toUpperCase() + "'";

				sCab.append( " - USU.:  " );
				sCab.append( txtIdUsu.getVlrString().trim().toUpperCase() );
			}
			
			if ( rgEmitidos.getVlrString().equals( "S" ) ) {
				sWhere8 = " AND V.STATUSVENDA IN ('V2','V3','P3') " ;
				sCab.append(" EMITIDOS " );
			}
			else if ( rgEmitidos.getVlrString().equals( "N" ) ) {
				sWhere8 = " AND V.STATUSVENDA NOT IN ('V2','V3','P3') ";
				sCab.append( "NAO EMITIDOS" );
			}


			sSQL.append( "SELECT VO.CODORC, " );
			
			sSQL.append( "V.CODVENDA,V.DOCVENDA,V.DTEMITVENDA,V.DTSAIDAVENDA,PP.DESCPLANOPAG,V.CODCLI," );
			sSQL.append( "C.RAZCLI,V.VLRDESCVENDA,V.VLRLIQVENDA,coalesce(pd2.CODPROD,pd1.codprod) codprod, coalesce(pd2.REFPROD,pd1.refprod) refprod,");
			
			sSQL.append( "coalesce(pd2.DESCPROD,pd1.descprod) descprod,");
			
			sSQL.append( "Iv.CODLOTE," );
			sSQL.append( "Iv.QTDITVENDA,Iv.PRECOITVENDA,Iv.VLRDESCITVENDA,Iv.VLRLIQITVENDA " );
			sSQL.append( ", v.codvend, vv.nomevend ");
			
			sSQL.append( "from vdvendedor vv, fnplanopag pp, vdcliente c, eqtipomov tm, vdvenda v, vditvenda iv "); 
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
			
			sSQL.append( "WHERE V.DTEMITVENDA BETWEEN ? AND ? AND V.CODEMP=? AND V.CODFILIAL=? " );
			sSQL.append( "AND PP.CODEMP=V.CODEMPPG AND PP.CODFILIAL=V.CODFILIAL AND PP.CODPLANOPAG=V.CODPLANOPAG " );
			sSQL.append( "AND C.CODEMP=V.CODEMPCL AND C.CODFILIAL=V.CODFILIALCL AND C.CODCLI=V.CODCLI " );
			sSQL.append( "AND TM.CODEMP=V.CODEMPTM AND TM.CODFILIAL=V.CODFILIALTM AND TM.CODTIPOMOV=V.CODTIPOMOV " );
			sSQL.append( "AND Iv.CODEMP=V.CODEMP AND Iv.CODFILIAL=V.CODFILIAL AND Iv.CODVENDA=V.CODVENDA AND Iv.TIPOVENDA=V.TIPOVENDA " );
			sSQL.append( "and vv.codemp=v.codempvd and vv.codfilial=v.codfilialvd and vv.codvend=v.codvend ");
			
			sSQL.append( sWhere1 );
			sSQL.append( sWhere2 );
			sSQL.append( sWhere3 );
			sSQL.append( sWhere4 );
			sSQL.append( sWhere5 );
			sSQL.append( sWhere6 );
			sSQL.append( sWhere7 );
			sSQL.append( sWhere8 );
			
			sSQL.append( "ORDER BY V.CODVENDA,Iv.CODITVENDA,V.DTEMITVENDA" );

			ps = con.prepareStatement( sSQL.toString() );
			
			ps.setDate( 1, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( 2, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "VDVENDA" ) );
			
			rs = ps.executeQuery();

			if ( "T".equals( rgTipo.getVlrString() ) ) {
				imprimirTexto( bVisualizar, rs, sCab.toString(), bComRef );
			}
			else if ( "G".equals( rgTipo.getVlrString() ) ) {
				imprimirGrafico( bVisualizar, rs, sCab.toString(), bComRef );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( Exception err ) {
			Funcoes.mensagemErro( this, "Erro ao montar relatorio!" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	private void imprimirTexto( final TYPE_PRINT bVisualizar, final ResultSet rs, final String sCab, final boolean bComRef ) {

		String sLinFina = StringFunctions.replicate( "-", 133 );
		String sLinDupla = StringFunctions.replicate( "=", 133 );
		BigDecimal bVlrDesc = new BigDecimal( 0 );
		BigDecimal bVlrLiq = new BigDecimal( 0 );
		BigDecimal bVlrDescTot = new BigDecimal( 0 );
		BigDecimal bVlrLiqTot = new BigDecimal( 0 );
		BigDecimal bQtd = new BigDecimal( 0 );
		BigDecimal bQtdTot = new BigDecimal( 0 );
		ImprimeOS imp = null;
		int linPag = 0;
		int iCodVendaAnt = 0;
		boolean montou = false;

		try {

			imp = new ImprimeOS( "", con );
			linPag = imp.verifLinPag() - 1;
			imp.montaCab();
			imp.setTitulo( "Relatório de Vendas Detalhado" );
			imp.addSubTitulo( "RELATORIO DE VENDAS DETALHADO   -   PERIODO DE :" + txtDataini.getVlrString() + " Até: " + txtDatafim.getVlrString() );
			imp.addSubTitulo( sCab );
			imp.limpaPags();

			while ( rs.next() ) {

				if ( !montou ) {
					montou = true;
				}

				if ( imp.pRow() >= linPag - 1 ) {
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "+" + sLinFina + "+" );
					imp.incPags();
					imp.eject();
				}

				if ( imp.pRow() == 0 ) {
					imp.impCab( 136, true );
					imp.pulaLinha( 0, imp.comprimido() );
					imp.say( 0, "|" + sLinFina + "|" );
				}

				if ( iCodVendaAnt != rs.getInt( "CodVenda" ) ) {
					if ( iCodVendaAnt != 0 ) {
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|" + sLinDupla + "|" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|" );
						imp.say( 44, " Totais da venda: " );
						imp.say( 69, "| " + Funcoes.strDecimalToStrCurrency( 12, Aplicativo.casasDec, String.valueOf( bQtd.setScale( Aplicativo.casasDec, BigDecimal.ROUND_HALF_UP ) ) ) );
						imp.say( 94, "| " + Funcoes.strDecimalToStrCurrency( 12, Aplicativo.casasDecPre, String.valueOf( bVlrDesc.setScale( Aplicativo.casasDecPre, BigDecimal.ROUND_HALF_UP ) ) ) );
						imp.say( 109, "| " + Funcoes.strDecimalToStrCurrency( 12, Aplicativo.casasDecPre, String.valueOf( bVlrLiq.setScale( Aplicativo.casasDecPre, BigDecimal.ROUND_HALF_UP ) ) ) );
						imp.say( 124, "|" );
						imp.say( 135, "|" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|" + sLinDupla + "|" );
					}
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "| Pedido: " );
					imp.say( 10, StringFunctions.strZero( rs.getString( "CodVenda" ), 8 ) );
					imp.say( 25, "Doc: " );
					imp.say( 30, StringFunctions.strZero( rs.getString( "DocVenda" ), 8 ) );
					imp.say( 45, "Emissão: " );
					imp.say( 53, StringFunctions.sqlDateToStrDate( rs.getDate( "DtEmitVenda" ) ) );
					imp.say( 68, "Saida: " );
					imp.say( 75, StringFunctions.sqlDateToStrDate( rs.getDate( "DtSaidaVenda" ) ) );
					imp.say( 90, "Plano Pagto.: " );
					imp.say( 104, Funcoes.copy( rs.getString( "DescPlanoPag" ), 30 ) );
					imp.say( 135, "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "| Cliente: " );
					imp.say( 11, rs.getInt( "CodCli" ) + " - " + rs.getString( "RazCli" ) );
					imp.say( 135, "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + sLinFina + "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "| Cod/Ref" );
					imp.say( 16, "| Descrição" );
					imp.say( 54, "| Lote" );
					imp.say( 69, "| Quant." );
					imp.say( 79, "| Preco" );
					imp.say( 94, "| Vlr.Desc." );
					imp.say( 109, "| Vlr.Liq." );
					imp.say( 124, "| Orcam." );
					imp.say( 135, "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|" + sLinFina + "|" );

					bVlrDesc = new BigDecimal( 0 );
					bVlrLiq = new BigDecimal( 0 );
					bQtd = new BigDecimal( 0 );
				}

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "| " + ( bComRef ? rs.getString( "RefProd" ) : rs.getString( "CodProd" ) ) );
				imp.say( 16, "| " + Funcoes.copy( rs.getString( "DescProd" ), 34 ) );
				imp.say( 54, "| " + Funcoes.copy( rs.getString( "CodLote" ), 13 ) );
				imp.say( 69, "| " + rs.getBigDecimal( "QtdItVenda" ).setScale( Aplicativo.casasDec, BigDecimal.ROUND_HALF_UP ) );
				imp.say( 79, "| " + Funcoes.strDecimalToStrCurrency( 12, Aplicativo.casasDecPre, String.valueOf( rs.getBigDecimal( "PrecoItVenda" ) ) ) );
				imp.say( 94, "| " + Funcoes.strDecimalToStrCurrency( 12, Aplicativo.casasDecPre, String.valueOf( rs.getBigDecimal( "VlrDescItVenda" ) ) ) );
				imp.say( 109, "| " + Funcoes.strDecimalToStrCurrency( 12, Aplicativo.casasDecPre, String.valueOf( rs.getBigDecimal( "VlrLiqItVenda" ) ) ) );
				imp.say( 124, "| " + ( rs.getString( 1 ) != null ? rs.getString( 1 ) : "" ) );
				imp.say( 135, "|" );

				if ( rs.getBigDecimal( "QtdItVenda" ) != null ) {
					bQtd = bQtd.add( rs.getBigDecimal( "QtdItVenda" ).setScale( Aplicativo.casasDec, BigDecimal.ROUND_HALF_UP ) );
					bQtdTot = bQtdTot.add( rs.getBigDecimal( "QtdItVenda" ).setScale( Aplicativo.casasDec, BigDecimal.ROUND_HALF_UP ) );
				}
				if ( rs.getBigDecimal( "VlrLiqItVenda" ) != null ) {
					bVlrLiq = bVlrLiq.add( rs.getBigDecimal( "VlrLiqItVenda" ).setScale( Aplicativo.casasDecPre, BigDecimal.ROUND_HALF_UP ) );
					bVlrLiqTot = bVlrLiqTot.add( rs.getBigDecimal( "VlrLiqItVenda" ).setScale( Aplicativo.casasDecPre, BigDecimal.ROUND_HALF_UP ) );
				}
				if ( rs.getBigDecimal( "VlrDescItVenda" ) != null ) {
					bVlrDesc = bVlrDesc.add( rs.getBigDecimal( "VlrDescItVenda" ).setScale( Aplicativo.casasDecPre, BigDecimal.ROUND_HALF_UP ) );
					bVlrDescTot = bVlrDescTot.add( rs.getBigDecimal( "VlrDescItVenda" ).setScale( Aplicativo.casasDecPre, BigDecimal.ROUND_HALF_UP ) );
				}
				iCodVendaAnt = rs.getInt( "CodVenda" );
			}

			if ( montou ) {
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" + sLinDupla + "|" );
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" );
				imp.say( 44, " Totais da venda: " );
				imp.say( 69, "| " + Funcoes.strDecimalToStrCurrency( 12, Aplicativo.casasDec, String.valueOf( bQtd.setScale( Aplicativo.casasDec, BigDecimal.ROUND_HALF_UP ) ) ) );
				imp.say( 94, "| " + Funcoes.strDecimalToStrCurrency( 12, Aplicativo.casasDecPre, String.valueOf( bVlrDesc.setScale( Aplicativo.casasDecPre, BigDecimal.ROUND_HALF_UP ) ) ) );
				imp.say( 109, "| " + Funcoes.strDecimalToStrCurrency( 12, Aplicativo.casasDecPre, String.valueOf( bVlrLiq.setScale( Aplicativo.casasDecPre, BigDecimal.ROUND_HALF_UP ) ) ) );
				imp.say( 124, "|" );
				imp.say( 135, "|" );
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" + sLinDupla + "|" );
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" + sLinDupla + "|" );
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" );
				imp.say( 44, " TOTAL GERAL : " );
				imp.say( 69, "| " + Funcoes.strDecimalToStrCurrency( 12, Aplicativo.casasDec, String.valueOf( bQtdTot.setScale( Aplicativo.casasDec, BigDecimal.ROUND_HALF_UP ) ) ) );
				imp.say( 94, "| " + Funcoes.strDecimalToStrCurrency( 12, Aplicativo.casasDecPre, String.valueOf( bVlrDescTot.setScale( Aplicativo.casasDecPre, BigDecimal.ROUND_HALF_UP ) ) ) );
				imp.say( 109, "| " + Funcoes.strDecimalToStrCurrency( 12, Aplicativo.casasDecPre, String.valueOf( bVlrLiqTot.setScale( Aplicativo.casasDecPre, BigDecimal.ROUND_HALF_UP ) ) ) );
				imp.say( 124, "|" );
				imp.say( 135, "|" );
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "+" + sLinDupla + "+" );
			}

			imp.eject();
			imp.fechaGravacao();

			if ( bVisualizar==TYPE_PRINT.VIEW ) {
				imp.preview( this );
			}
			else {
				imp.print();
			}

		} catch ( Exception err ) {
			Funcoes.mensagemErro( this, "Erro ao montar relatorio!" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	public void imprimirGrafico( final TYPE_PRINT bVisualizar, final ResultSet rs, final String sCab, final boolean bComRef ) {

		HashMap<String, Object> hParam = new HashMap<String, Object>();
		hParam.put( "COMREF", bComRef ? "S" : "N" );

		FPrinterJob dlGr = new FPrinterJob( "relatorios/VendasDetalhadas.jasper", "Vendas Detalhadas", sCab, rs, hParam, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório de vendas detalhadas!" + err.getMessage(), true, con, err );
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
			if ( rs.next() ) {
				if ( rs.getString( "UsaRefProd" ).trim().equals( "S" ) )
					bRetorno = true;
			}
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage(), true, con, err );
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}
		return bRetorno;
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcProd.setConexao( cn );
		lcCliente.setConexao( cn );
		lcVend.setConexao( cn );
	}
}
