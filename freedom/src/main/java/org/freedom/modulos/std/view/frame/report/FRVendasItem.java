/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FRVendasItem.java <BR>
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

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.acao.CheckBoxEvent;
import org.freedom.acao.CheckBoxListener;
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

public class FRVendasItem extends FRelatorio implements CheckBoxListener, FocusListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtDescVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCodGrup = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldFK txtDescGrup = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodMarca = new JTextFieldPad( JTextFieldPad.TP_STRING, 6, 0 );

	private JTextFieldFK txtDescMarca = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtSiglaMarca = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );
	
	private JCheckBoxPad cbTipoPorAno = new JCheckBoxPad( "Anual", "S", "N" );
	
	private JTextFieldPad txtAno = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );

	private JCheckBoxPad cbListaFilial = new JCheckBoxPad( "Listar vendas das filiais ?", "S", "N" );

	private JCheckBoxPad cbVendaCanc = new JCheckBoxPad( "Mostrar Canceladas", "S", "N" );

	private JRadioGroup<?, ?> rgTipo = null;

	private JRadioGroup<?, ?> rgOrdem = null;

	private JRadioGroup<?, ?> rgFaturados = null;

	private JRadioGroup<?, ?> rgFinanceiro = null;
	
	private JRadioGroup<?, ?> rgQtdVlr = null;

	private ListaCampos lcVend = new ListaCampos( this );

	private ListaCampos lcGrup = new ListaCampos( this );

	private ListaCampos lcCliente = new ListaCampos( this );

	private ListaCampos lcMarca = new ListaCampos( this );

	private boolean comref = false;
	
	private JCheckBoxPad cbPorConserto = new JCheckBoxPad( "Por item de O.S", "S", "N" );
	
	private JRadioGroup<?, ?> rgEmitidos = null;
	
	private Vector<String> vLabsEmit = new Vector<String>();

	private Vector<String> vValsEmit = new Vector<String>();

	public FRVendasItem() {

		setTitulo( "Vendas por Item" );
		setAtribos( 80, 80, 620, 440 );

		txtDescVend.setAtivo( false );
		txtDescGrup.setAtivo( false );
		txtDescMarca.setAtivo( false );
		txtRazCli.setAtivo( false );

		Vector<String> vLabs = new Vector<String>();
		Vector<String> vVals = new Vector<String>();

		vLabs.addElement( "Grafico" );
		vLabs.addElement( "Texto" );
		vVals.addElement( "G" );
		vVals.addElement( "T" );
		rgTipo = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgTipo.setVlrString( "G" );

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
		
		Vector<String> vLabs4 = new Vector<String>();
		Vector<String> vVals4 = new Vector<String>();

		vLabs4.addElement( "Por qtde" );
		vLabs4.addElement( "Por valor" );
		vVals4.addElement( "Q" );
		vVals4.addElement( "V" );
		
		rgQtdVlr = new JRadioGroup<String, String>( 2, 1, vLabs4, vVals4 );
		rgQtdVlr.setVlrString( "Q" );

		
		Vector<String> vLabs3 = new Vector<String>();
		Vector<String> vVals3 = new Vector<String>();

		vLabs3.addElement( "Código" );
		vLabs3.addElement( "Descrição" );
		vLabs3.addElement( "Quant." );
		vLabs3.addElement( "Valor" );
		
		vVals3.addElement( "C" );
		vVals3.addElement( "D" );
		vVals3.addElement( "QD" );
		vVals3.addElement( "V" );

		rgOrdem = new JRadioGroup<String, String>( 1, 2, vLabs3, vVals3 );
		rgOrdem.setVlrString( "D" );
		// Inativa por default a ordenação por valor
		//rgOrdem.setAtivo( 3, false );
		
		vLabsEmit.addElement( "Emitidos" );
		vLabsEmit.addElement( "Não emitidos" );
		vLabsEmit.addElement( "Ambos" );
		vValsEmit.addElement( "S" );
		vValsEmit.addElement( "N" );
		vValsEmit.addElement( "A" );
		rgEmitidos = new JRadioGroup<String, String>( 3, 1, vLabsEmit, vValsEmit );
		rgEmitidos.setVlrString( "A" );
		

		lcGrup.add( new GuardaCampo( txtCodGrup, "CodGrup", "Cód.grupo", ListaCampos.DB_PK, false ) );
		lcGrup.add( new GuardaCampo( txtDescGrup, "DescGrup", "Descrição do grupo", ListaCampos.DB_SI, false ) );
		txtCodGrup.setTabelaExterna( lcGrup, null );
		txtCodGrup.setNomeCampo( "CodGrup" );
		txtCodGrup.setFK( true );
		lcGrup.setReadOnly( true );
		lcGrup.montaSql( false, "GRUPO", "EQ" );

		lcMarca.add( new GuardaCampo( txtCodMarca, "CodMarca", "Cód.marca", ListaCampos.DB_PK, false ) );
		lcMarca.add( new GuardaCampo( txtDescMarca, "DescMarca", "Descrição da marca", ListaCampos.DB_SI, false ) );
		lcMarca.add( new GuardaCampo( txtSiglaMarca, "SiglaMarca", "Sigla", ListaCampos.DB_SI, false ) );
		txtCodMarca.setTabelaExterna( lcMarca, null );
		txtCodMarca.setNomeCampo( "CodMarca" );
		txtCodMarca.setFK( true );
		lcMarca.setReadOnly( true );
		lcMarca.montaSql( false, "MARCA", "EQ" );

		lcVend.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.comiss.", ListaCampos.DB_PK, false ) );
		lcVend.add( new GuardaCampo( txtDescVend, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, false ) );
		txtCodVend.setTabelaExterna( lcVend, null );
		txtCodVend.setNomeCampo( "CodVend" );
		txtCodVend.setFK( true );
		lcVend.setReadOnly( true );
		lcVend.montaSql( false, "VENDEDOR", "VD" );

		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );
		txtDataini.setVlrDate( new Date() );
		txtDatafim.setVlrDate( new Date() );

		lcCliente.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCliente.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		txtCodCli.setTabelaExterna( lcCliente, null );
		txtCodCli.setNomeCampo( "CodCli" );
		txtCodCli.setFK( true );
		lcCliente.setReadOnly( true );
		lcCliente.montaSql( false, "CLIENTE", "VD" );

		adic( lbLinha, 7, 25, 273, 70, "Período:" );
		
		adic( cbTipoPorAno, 15, 30, 80, 20);
		adic( new JLabelPad("Ano:"), 110, 30, 50, 20);
		adic( txtAno, 143, 30, 80, 20 );
		
		adic( new JLabelPad("De:"), 15, 60, 90, 20);
		adic( txtDataini, 45, 60, 90, 20 );
		
		adic( new JLabelPad("Até:"), 150, 60, 90, 20 );
		adic( txtDatafim, 180, 60, 90, 20 );
		
		adic( rgOrdem, 295, 25, 273, 30, "Ordenado por:" );
		
		adic( rgTipo, 295, 65, 273, 30 );
		
		adic( txtCodVend, 7, 120, 70, 20, "Cód.comiss." );
		adic( txtDescVend, 80, 120, 200, 20, "Nome do comissionado" );
		adic( txtCodGrup, 295, 120, 70, 20, "Cód.grupo" );
		adic( txtDescGrup, 368, 120, 200, 20, "Descrição do grupo" );
		adic( txtCodCli, 7, 160, 70, 20, "Cód.cli." );
		
		adic( txtRazCli, 80, 160, 200, 20, "Razão social do cliente" );		
		adic( txtCodMarca, 295, 160, 70, 20, "Cód.marca" );		
		adic( txtDescMarca, 368, 160, 200, 20, "Descrição da marca" );
		
		adic( rgFaturados, 		7, 		200, 	125, 	70 );
		adic( rgFinanceiro, 	157, 	200, 	125, 	70 );
		
		adic( rgQtdVlr, 	157, 	280, 	125, 	50 );
		adic( rgEmitidos, 		7, 		280, 	125, 	70 );
		
		adic( cbListaFilial, 	295, 	200, 	200, 	20 );
		adic( cbVendaCanc, 		295, 	225, 	200, 	20 );		
		adic( cbPorConserto,  	295,	250, 	200, 	20 );
		
		cbTipoPorAno.setVlrString( "N" );
		
		cbTipoPorAno.addCheckBoxListener( this );
		cbPorConserto.addCheckBoxListener( this );
		
		txtAno.setEnabled( false );
		rgQtdVlr.setVisible( true );
		rgQtdVlr.setAtivo( false );
		
		txtAno.addFocusListener( this );

	}
	
	public void imprimir( TYPE_PRINT bVisualizar ) {
		String tipoPorAno = cbTipoPorAno.getVlrString();
		if("S".equals( cbPorConserto.getVlrString() )) {
			imprimirPorConserto( bVisualizar, "S".equals(tipoPorAno ));
		}
		else {
			imprimirPorVenda( bVisualizar, "S".equals( tipoPorAno ));
		}
		
	}


	public void imprimirPorVenda( final TYPE_PRINT bVisualizar, final boolean tipoPorAno ) {
		
		if ( (tipoPorAno) && (txtAno.getVlrInteger().intValue()==0 ) ) {
			Funcoes.mensagemInforma( this, "Selecione o ano a imprimir !" );
			txtAno.requestFocus();
			return;
		}
		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();
		StringBuffer sCab = new StringBuffer();
		StringBuffer sCab2 = new StringBuffer();
		String sWhere = "";
		String sWhere1 = null;
		String sWhere2 = null;
		String sWhere3 = "";
		String sOrdem = rgOrdem.getVlrString();
		String sOrdenado = "";
		String qtdVlr = null;
		int paran = 1;
		boolean listaFilial = false;

		try {

			sCab.append( "PERIODO DE :" + txtDataini.getVlrString() + " Até: " + txtDatafim.getVlrString());
			
			if ( txtCodVend.getText().trim().length() > 0 ) {
				sWhere += " AND V.CODVEND=" + txtCodVend.getText().trim();
				sCab.append( " - REPR.: " + txtDescVend.getText().trim() );
			}
			if ( txtCodGrup.getText().trim().length() > 0 ) {
				sWhere += " AND P.CODGRUP LIKE '" + txtCodGrup.getText().trim() + "%'";
				if ( sCab.length() > 0 ) {
					sCab.append( " - " );
				}
				sCab.append( " - GRUPO: " + txtDescGrup.getText().trim() );
			}
			if ( txtCodMarca.getText().trim().length() > 0 ) {
				sWhere += " AND P.CODMARCA='" + txtCodMarca.getText().trim() + "'";
				if ( sCab.length() > 0 ) {
					sCab.append( " - " );
				}
				sCab.append( " - MARCA: " + txtDescMarca.getText().trim() );
			}
			if ( txtCodCli.getText().trim().length() > 0 ) {
				if ( cbListaFilial.getVlrString().equals( "S" ) ) {
					sWhere += " AND (C.CODPESQ=" + txtCodCli.getText().trim() + " OR C.CODCLI=" + txtCodCli.getText().trim() + ")";
				}
				else {
					sWhere += " AND V.CODCLI=" + txtCodCli.getText().trim();
				}
				if ( sCab.length() > 0 ) {
					sCab.append( " - " );
				}
				sCab.append( " CLIENTE: " + txtRazCli.getText().trim() );
			}
			
			if ("V".equals(rgQtdVlr.getVlrString()) ) {
				sCab.append( " - Valores expressos em milhares de reais" );
			}

			if ( rgFaturados.getVlrString().equals( "S" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV='S' ";
				sCab2.append( "FATURADO" );
			}
			else if ( rgFaturados.getVlrString().equals( "N" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV='N' ";
				if ( sCab2.length() > 0 ) {
					sCab2.append( " - " );
				}
				sCab2.append( "NAO FATURADO" );
			}
			else if ( rgFaturados.getVlrString().equals( "A" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV IN ('S','N') ";
			}

			if ( rgFinanceiro.getVlrString().equals( "S" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV='S' ";
				if ( sCab2.length() > 0 ) {
					sCab2.append( " - " );
				}
				sCab2.append( "FINANCEIRO" );
			}
			else if ( rgFinanceiro.getVlrString().equals( "N" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV='N' ";
				if ( sCab2.length() > 0 ) {
					sCab2.append( " - " );
				}
				sCab2.append( "NAO FINANCEIRO" );
			}
			else if ( rgFinanceiro.getVlrString().equals( "A" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV IN ('S','N') ";
			}

			if ( cbVendaCanc.getVlrString().equals( "N" ) ) {
				sWhere3 = " AND NOT SUBSTR(V.STATUSVENDA,1,1)='C' ";
			}

			// Inicia as condições para ordenação 
			if ( sOrdem.equals( "C" ) ) {
				sOrdem = comref ? "P.REFPROD" : "P.CODPROD";
				sOrdenado = "\nORDENADO POR " + ( comref ? "REFERENCIA" : "CODIGO" );
			}
			else if ( sOrdem.equals( "D" ) ) {

				if( "N".equals( cbPorConserto.getVlrString() )) {	
				
					sOrdem = "P.DESCPROD";
					sOrdenado = "\nORDENADO POR DESCRICAO";
				}
				else {
					sOrdem = "IT.OBSITVENDA";
					sOrdenado = "\nORDENADO POR OBSERVAÇÃO DO ITEM";
				}	
			}
			else if ( sOrdem.equals( "QD" ) ) {
				if ("S".equals( cbTipoPorAno.getVlrString() ) ) {
					sOrdem = " 17 desc ";
				} else {
					sOrdem = " 5 desc ";
				}
				sOrdenado = "\nORDENADO DECRESCENTE POR QUANTIDADE";
			}
			else if ( sOrdem.equals( "V" ) ) {
				if ("S".equals( cbTipoPorAno.getVlrString() ) ) {
					sOrdem = " 17 desc ";
				} else {
					sOrdem = " 6 desc ";
				}
				sOrdenado = "\nORDENADO DECRESCENTE POR VALOR";
			}

			// Define se a coluna do relatório será qtdade ou valor.
			if ("Q".equals(rgQtdVlr.getVlrString()) ) {
				qtdVlr="it.qtditvenda else 0 end)";
			} else {
				qtdVlr="it.vlrliqitvenda else 0 end) / 1000";
			}
			
			sCab2.append( sOrdenado );

			if ( cbListaFilial.getVlrString().equals( "S" ) && ( txtCodCli.getVlrInteger() > 0 ) ) {
				
				sSQL.append( "select p.codprod, p.refprod,");
								
				if( "N".equals( cbPorConserto.getVlrString() )) {
					sSQL.append( "p.descprod, ");
				}
				else {
					sSQL.append( "coalesce(it.obsitvenda, p.descprod) as descprod, ");
				}
				sSQL.append( "p.codunid, ");
				if (tipoPorAno) {
				    sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=1 then "+qtdVlr+" mes01, ");
		    		sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=2 then "+qtdVlr+" mes02,");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=3 then "+qtdVlr+" mes03,");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=4 then "+qtdVlr+" mes04, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=5 then "+qtdVlr+" mes05, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=6 then "+qtdVlr+" mes06, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=7 then "+qtdVlr+" mes07, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=8 then "+qtdVlr+" mes08, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=9 then "+qtdVlr+" mes09, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=10 then "+qtdVlr+" mes10, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=11 then "+qtdVlr+" mes11, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=12 then "+qtdVlr+" mes12, ");
					if ("Q".equals(rgQtdVlr.getVlrString()) ) {
						sSQL.append( "sum (it.qtditvenda ) total ");
					} else {
						sSQL.append( "sum (it.vlrliqitvenda ) / 1000 total ");						
					}
				}
				else {
					sSQL.append( "sum(it.qtditvenda) qtditvenda, sum(it.vlrliqitvenda) vlrliqitvenda " );
				}
				sSQL.append( "FROM VDVENDA V,EQTIPOMOV TM, VDCLIENTE C, VDITVENDA IT, EQPRODUTO P " );
				sSQL.append( "WHERE P.CODPROD=IT.CODPROD AND IT.CODVENDA=V.CODVENDA " );
				sSQL.append( sWhere );
				sSQL.append( sWhere1 );
				sSQL.append( sWhere2 );
				sSQL.append( sWhere3 );
				sSQL.append( "AND TM.CODEMP=V.CODEMPTM AND TM.CODFILIAL=V.CODFILIALTM AND TM.CODTIPOMOV=V.CODTIPOMOV " );
				sSQL.append( "AND TM.TIPOMOV IN ('VD','VE','PV','VT','SE') " );
				sSQL.append( "AND V.CODCLI=C.CODCLI AND V.CODEMPCL=C.CODEMP AND V.CODFILIALCL=C.CODFILIAL " );
				sSQL.append( "AND V.DTEMITVENDA BETWEEN ? AND ? " );
				sSQL.append( "AND V.FLAG IN " );
				sSQL.append( AplicativoPD.carregaFiltro( con, org.freedom.library.swing.frame.Aplicativo.iCodEmp ) );
				sSQL.append( " GROUP BY " );
				sSQL.append( ( comref ? "P.REFPROD,P.CODPROD," : "P.CODPROD,P.REFPROD," ) );
				
				if( "N".equals( cbPorConserto.getVlrString() )) {				
					sSQL.append( "P.DESCPROD,P.CODUNID " );
				}
				else { 
					sSQL.append( "IT.OBSITVENDA, P.CODUNID " );
				}
				
				sSQL.append( "ORDER BY " + sOrdem );
				listaFilial = true;
			}
			else {
				sSQL.append( "SELECT P.CODPROD,P.REFPROD,");
				
				if( "N".equals( cbPorConserto.getVlrString() )) {
					sSQL.append( "P.DESCPROD,");
				}
				else {
					sSQL.append( "coalesce(IT.OBSITVENDA,p.descprod) AS DESCPROD, ");
				}
				
				sSQL.append( "p.codunid, ");
				if (tipoPorAno) {
				    sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=1 then "+qtdVlr+" mes01, ");
		    		sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=2 then "+qtdVlr+" mes02,");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=3 then "+qtdVlr+" mes03,");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=4 then "+qtdVlr+" mes04, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=5 then "+qtdVlr+" mes05, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=6 then "+qtdVlr+" mes06, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=7 then "+qtdVlr+" mes07, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=8 then "+qtdVlr+" mes08, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=9 then "+qtdVlr+" mes09, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=10 then "+qtdVlr+" mes10, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=11 then "+qtdVlr+" mes11, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=12 then "+qtdVlr+" mes12, ");
					if ("Q".equals(rgQtdVlr.getVlrString()) ) {
						sSQL.append( "sum (it.qtditvenda ) total ");
					} else {
						sSQL.append( "sum (it.vlrliqitvenda ) / 1000 total ");						
					}
				}
				else {
					sSQL.append( "sum(it.qtditvenda) qtditvenda, sum(it.vlrliqitvenda) vlrliqitvenda " );
				}
				sSQL.append( "FROM VDVENDA V,EQTIPOMOV TM,VDITVENDA IT, EQPRODUTO P " );
				sSQL.append( "WHERE P.CODEMP=? AND P.CODFILIAL=? " );
				sSQL.append( "AND IT.CODEMPPD=P.CODEMP AND IT.CODFILIALPD=P.CODFILIAL AND IT.CODPROD=P.CODPROD " );
				sSQL.append( "AND V.CODEMP=IT.CODEMP AND V.CODFILIAL=IT.CODFILIAL AND V.CODVENDA=IT.CODVENDA " );
				sSQL.append( "AND V.DTEMITVENDA BETWEEN ? AND ? AND V.FLAG IN ('S','N') " );
				
				sSQL.append( sWhere );
				sSQL.append( sWhere1 );
				sSQL.append( sWhere2 );
				sSQL.append( sWhere3 );
				
				sSQL.append( "AND TM.CODEMP=V.CODEMPTM AND TM.CODFILIAL=V.CODFILIALTM AND TM.CODTIPOMOV=V.CODTIPOMOV " );
				sSQL.append( "AND TM.TIPOMOV IN ('VD','VE','PV','VT','SE') " );
				sSQL.append( "GROUP BY " );
				
				
				sSQL.append( ( comref ? "P.REFPROD,P.CODPROD," : "P.CODPROD,P.REFPROD," ) );

				if( "N".equals( cbPorConserto.getVlrString() )) {				
					sSQL.append( "P.DESCPROD,P.CODUNID " );
				}
				else {
					sSQL.append( "IT.OBSITVENDA, P.CODUNID " );
				}
				
				sSQL.append( "ORDER BY " + sOrdem );
				
				
				
			}

			System.out.println("SQL:" + sSQL.toString());
			
			ps = con.prepareStatement( sSQL.toString() );
			if ( !listaFilial ) {
				ps.setInt( paran++, Aplicativo.iCodEmp );
				ps.setInt( paran++, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			}
			ps.setDate( paran++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( paran++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			rs = ps.executeQuery();

			if ( "T".equals( rgTipo.getVlrString() ) ) {
				imprimirTexto( bVisualizar, rs, Funcoes.strToVectorSilabas( sCab.toString() + "\n" + sCab2.toString(), 130 ), comref );
			}
			else if ( "G".equals( rgTipo.getVlrString() ) ) {
				imprimirGrafico( bVisualizar, rs, sCab.toString() + "\n" + sCab2.toString(), comref, tipoPorAno );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( Exception err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao montar relatorio!\n" + err.getMessage(), true, con, err );
		} finally {
			System.gc();
		}
	}
	
	public void imprimirPorConserto( final TYPE_PRINT bVisualizar, final boolean tipoPorAno ) {

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();
		StringBuffer sCab = new StringBuffer();
		StringBuffer sCab2 = new StringBuffer();
		String sWhere = "";
		String sWhere1 = null;
		String sWhere2 = null;
		String sWhere3 = "";
		String qtdVlr = null;
		String sOrdem = rgOrdem.getVlrString();
		String sOrdenado = "";
		int paran = 1;
		boolean listaFilial = false;

		try {

			sCab.append( "PERIODO DE :" + txtDataini.getVlrString() + " Até: " + txtDatafim.getVlrString());
			
			if ( txtCodVend.getText().trim().length() > 0 ) {
				sWhere += " AND V.CODVEND=" + txtCodVend.getText().trim();
				sCab.append( " - REPR.: " + txtDescVend.getText().trim() );
			}
			if ( txtCodGrup.getText().trim().length() > 0 ) {
				sWhere += " AND coalesce(Pd2.CODGRUP,pd1.codgrup) LIKE '" + txtCodGrup.getText().trim() + "%'";
				if ( sCab.length() > 0 ) {
					sCab.append( " - " );
				}
				sCab.append( " - GRUPO: " + txtDescGrup.getText().trim() );
			}
			if ( txtCodMarca.getText().trim().length() > 0 ) {
				sWhere += " AND coalesce(Pd2.CODMARCA,pd1.codmarca)='" + txtCodMarca.getText().trim() + "'";
				if ( sCab.length() > 0 ) {
					sCab.append( " - " );
				}
				sCab.append( " - MARCA: " + txtDescMarca.getText().trim() );
			}
			if ( txtCodCli.getVlrInteger() > 0 ) {
				if ( cbListaFilial.getVlrString().equals( "S" ) ) {
					sWhere += " AND (C.CODPESQ=" + txtCodCli.getVlrInteger() + " OR C.CODCLI=" + txtCodCli.getVlrInteger() + ")";
				}
				else {
					sWhere += " AND V.CODCLI=" + txtCodCli.getText().trim();
				}
				if ( sCab.length() > 0 ) {
					sCab.append( " - " );
				}
				sCab.append( " CLIENTE: " + txtRazCli.getText().trim() );
			}
			
			if ("V".equals(rgQtdVlr.getVlrString()) ) {
				sCab.append( " - Valores expressos em milhares de reais" );
			}

			if ( rgFaturados.getVlrString().equals( "S" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV='S' ";
				sCab2.append( "FATURADO" );
			}
			else if ( rgFaturados.getVlrString().equals( "N" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV='N' ";
				if ( sCab2.length() > 0 ) {
					sCab2.append( " - " );
				}
				sCab2.append( "NAO FATURADO" );
			}
			else if ( rgFaturados.getVlrString().equals( "A" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV IN ('S','N') ";
			}

			if ( rgFinanceiro.getVlrString().equals( "S" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV='S' ";
				if ( sCab2.length() > 0 ) {
					sCab2.append( " - " );
				}
				sCab2.append( "FINANCEIRO" );
			}
			else if ( rgFinanceiro.getVlrString().equals( "N" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV='N' ";
				if ( sCab2.length() > 0 ) {
					sCab2.append( " - " );
				}
				sCab2.append( "NAO FINANCEIRO" );
			}
			else if ( rgFinanceiro.getVlrString().equals( "A" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV IN ('S','N') ";
			}

			if ( cbVendaCanc.getVlrString().equals( "N" ) ) {
				sWhere3 = " AND NOT SUBSTR(V.STATUSVENDA,1,1)='C' ";
			}

			if ( sOrdem.equals( "C" ) ) {
				sOrdem = comref ? " 2 " : " 1 ";
				sOrdenado = "\nORDENADO POR " + ( comref ? "REFERENCIA" : "CODIGO" );
			}
			else if ( sOrdem.equals( "D" ) ) {
				sOrdem = " 3 ";
				sOrdenado = "\nORDENADO POR DESCRICAO";
			}
			else if ( sOrdem.equals( "QD" ) ) {
				if ( "S".equals(cbTipoPorAno.getVlrString() ) ) {
					sOrdem = " 17 desc ";
				} else {
					sOrdem = " 5 desc ";
				}
				sOrdenado = "\nORDENADO DECRESCENTE POR QUANTIDADE";
			}
			else if ( sOrdem.equals( "V" ) ) {
				if ( "S".equals(cbTipoPorAno.getVlrString() ) ) {
					sOrdem = " 17 desc ";
				} else {
					sOrdem = " 6 desc ";
				}
				sOrdenado = "\nORDENADO DECRESCENTE POR VALOR";
			}
			
			// Define se a coluna do relatório será qtdade ou valor.
			if ("Q".equals(rgQtdVlr.getVlrString()) ) {
				qtdVlr="it.qtditvenda else 0 end)";
			} else {
				qtdVlr="it.vlrliqitvenda else 0 end) / 1000";
			}

			sCab2.append( sOrdenado );

			if ( cbListaFilial.getVlrString().equals( "S" ) && ( txtCodCli.getVlrInteger() > 0 ) ) {
				
				sSQL.append( "select " );
				sSQL.append( "coalesce(pd2.codprod,pd1.codprod) codprod, coalesce(pd2.refprod,pd1.refprod) refprod, coalesce(pd2.descprod,pd1.descprod) descprod, ");
				sSQL.append( "coalesce(pd2.codunid,pd1.codunid) codunid, ");
				if (tipoPorAno) {
				    sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=1 then "+qtdVlr+" mes01, ");
		    		sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=2 then "+qtdVlr+" mes02,");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=3 then "+qtdVlr+" mes03,");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=4 then "+qtdVlr+" mes04, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=5 then "+qtdVlr+" mes05, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=6 then "+qtdVlr+" mes06, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=7 then "+qtdVlr+" mes07, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=8 then "+qtdVlr+" mes08, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=9 then "+qtdVlr+" mes09, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=10 then "+qtdVlr+" mes10, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=11 then "+qtdVlr+" mes11, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=12 then "+qtdVlr+" mes12, ");
    				if ("Q".equals(rgQtdVlr.getVlrString()) ) {
						sSQL.append( "sum (it.qtditvenda ) total ");
					} else {
						sSQL.append( "sum (it.vlrliqitvenda ) / 1000 total ");
					}
				}
				else {
					sSQL.append( "sum(it.qtditvenda) qtditvenda, sum(it.vlrliqitvenda) vlrliqitvenda " );
				}
				
	 			sSQL.append( "from vdcliente c, eqtipomov tm, vdvenda v, vditvenda it "); 
				sSQL.append( "left outer join eqproduto pd1 on ");
				sSQL.append( "pd1.codemp=it.codemppd and pd1.codfilial=it.codfilialpd and pd1.codprod=it.codprod ");
				sSQL.append( "left outer join eqgrupo g1 on ");
				sSQL.append( "g1.codemp=pd1.codempgp and g1.codfilial=pd1.codfilialgp and g1.codgrup=pd1.codgrup ");
				sSQL.append( "left outer join vdvendaorc vo on ");
				sSQL.append( "vo.codemp=it.codemp and vo.codfilial=it.codfilial and vo.codvenda=it.codvenda and vo.tipovenda=it.tipovenda and vo.coditvenda=it.coditvenda ");
				sSQL.append( "left outer join eqitrecmercitositorc iro on ");
				sSQL.append( "iro.codempoc=vo.codempor and iro.codfilialoc=vo.codfilialor and iro.codorc=vo.codorc and iro.tipoorc=vo.tipoorc and iro.coditorc=vo.coditorc ");
				sSQL.append( "left outer join eqitrecmerc ir on ");
				sSQL.append( "ir.codemp=iro.codemp and ir.codfilial=iro.codfilial and ir.ticket=iro.ticket and ir.coditrecmerc=iro.coditrecmerc ");
				sSQL.append( "left outer join eqproduto pd2 on ");
				sSQL.append( "pd2.codemp=ir.codemppd and pd2.codfilial=ir.codfilialpd and pd2.codprod=ir.codprod ");
				sSQL.append( "left outer join eqgrupo g2 on ");
				sSQL.append( "g2.codemp=pd2.codempgp and g2.codfilial=pd2.codfilialgp and g2.codgrup=pd2.codgrup " );
				
				sSQL.append( sWhere );
				sSQL.append( sWhere1 );
				sSQL.append( sWhere2 );
				sSQL.append( sWhere3 );

				sSQL.append( "AND TM.CODEMP=V.CODEMPTM AND TM.CODFILIAL=V.CODFILIALTM AND TM.CODTIPOMOV=V.CODTIPOMOV " );
				sSQL.append( "AND TM.TIPOMOV IN ('VD','VE','PV','VT','SE') " );
				sSQL.append( "AND V.CODCLI=C.CODCLI AND V.CODEMPCL=C.CODEMP AND V.CODFILIALCL=C.CODFILIAL " );
				sSQL.append( "AND V.DTEMITVENDA BETWEEN ? AND ? " );
				sSQL.append( "AND V.FLAG IN " );
				
				sSQL.append( AplicativoPD.carregaFiltro( con, org.freedom.library.swing.frame.Aplicativo.iCodEmp ) );
				
				sSQL.append( " GROUP BY 1 , 2, 3, 4 " );
				
				sSQL.append( "ORDER BY " + sOrdem );
				listaFilial = true;
				
			}
			else {
				
				sSQL.append( "select " );
				sSQL.append( "coalesce(pd2.codprod,pd1.codprod) codprod, coalesce(pd2.refprod,pd1.refprod) refprod, coalesce(pd2.descprod,pd1.descprod) descprod, ");
				sSQL.append( "coalesce(pd2.codunid,pd1.codunid) codunid, ");
				
				if (tipoPorAno) {
				    sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=1 then "+qtdVlr+" mes01, ");
		    		sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=2 then "+qtdVlr+" mes02,");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=3 then "+qtdVlr+" mes03,");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=4 then "+qtdVlr+" mes04, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=5 then "+qtdVlr+" mes05, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=6 then "+qtdVlr+" mes06, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=7 then "+qtdVlr+" mes07, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=8 then "+qtdVlr+" mes08, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=9 then "+qtdVlr+" mes09, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=10 then "+qtdVlr+" mes10, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=11 then "+qtdVlr+" mes11, ");
    				sSQL.append( "sum(case  when extract(month from v.dtemitvenda)=12 then "+qtdVlr+" mes12, ");
    				if ("Q".equals(rgQtdVlr.getVlrString()) ) {
						sSQL.append( "sum (it.qtditvenda ) total ");
					} else {
						sSQL.append( "sum (it.vlrliqitvenda ) / 1000 total ");						
					}
				}
				else {
					sSQL.append( "sum(it.qtditvenda) qtditvenda, sum(it.vlrliqitvenda) vlrliqitvenda " );
				}
							
	 			sSQL.append( "from vdcliente c, eqtipomov tm, vdvenda v, vditvenda it "); 
				sSQL.append( "left outer join eqproduto pd1 on ");
				sSQL.append( "pd1.codemp=it.codemppd and pd1.codfilial=it.codfilialpd and pd1.codprod=it.codprod ");
				sSQL.append( "left outer join eqgrupo g1 on ");
				sSQL.append( "g1.codemp=pd1.codempgp and g1.codfilial=pd1.codfilialgp and g1.codgrup=pd1.codgrup ");
				sSQL.append( "left outer join vdvendaorc vo on ");
				sSQL.append( "vo.codemp=it.codemp and vo.codfilial=it.codfilial and vo.codvenda=it.codvenda and vo.tipovenda=it.tipovenda and vo.coditvenda=it.coditvenda ");
				sSQL.append( "left outer join eqitrecmercitositorc iro on ");
				sSQL.append( "iro.codempoc=vo.codempor and iro.codfilialoc=vo.codfilialor and iro.codorc=vo.codorc and iro.tipoorc=vo.tipoorc and iro.coditorc=vo.coditorc ");
				sSQL.append( "left outer join eqitrecmerc ir on ");
				sSQL.append( "ir.codemp=iro.codemp and ir.codfilial=iro.codfilial and ir.ticket=iro.ticket and ir.coditrecmerc=iro.coditrecmerc ");
				sSQL.append( "left outer join eqproduto pd2 on ");
				sSQL.append( "pd2.codemp=ir.codemppd and pd2.codfilial=ir.codfilialpd and pd2.codprod=ir.codprod ");
				sSQL.append( "left outer join eqgrupo g2 on ");
				sSQL.append( "g2.codemp=pd2.codempgp and g2.codfilial=pd2.codfilialgp and g2.codgrup=pd2.codgrup " );
				
				sSQL.append( "WHERE it.CODEMP=? AND it.CODFILIAL=? " );
				
				sSQL.append( "AND V.CODEMP=it.CODEMP AND V.CODFILIAL=it.CODFILIAL AND V.CODVENDA=it.CODVENDA and v.tipovenda=it.tipovenda " );
				sSQL.append( "AND V.CODCLI=C.CODCLI AND V.CODEMPCL=C.CODEMP AND V.CODFILIALCL=C.CODFILIAL " );
				sSQL.append( "AND V.DTEMITVENDA BETWEEN ? AND ? AND V.FLAG IN ('S','N') " );
				
				sSQL.append( sWhere );
				sSQL.append( sWhere1 );
				sSQL.append( sWhere2 );
				sSQL.append( sWhere3 );
				
				sSQL.append( "AND TM.CODEMP=V.CODEMPTM AND TM.CODFILIAL=V.CODFILIALTM AND TM.CODTIPOMOV=V.CODTIPOMOV " );
				sSQL.append( "AND TM.TIPOMOV IN ('VD','VE','PV','VT','SE') " );
				
				sSQL.append( " GROUP BY 1 , 2, 3, 4 " );
				
				sSQL.append( "ORDER BY " + sOrdem );
				
				
			}

			System.out.println("SQL NOVA:" + sSQL.toString());
			
			ps = con.prepareStatement( sSQL.toString() );
			if ( !listaFilial ) {
				ps.setInt( paran++, Aplicativo.iCodEmp );
				ps.setInt( paran++, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			}
			ps.setDate( paran++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( paran++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			rs = ps.executeQuery();

			if ( "T".equals( rgTipo.getVlrString() ) ) {
				imprimirTexto( bVisualizar, rs, Funcoes.strToVectorSilabas( sCab.toString() + "\n" + sCab2.toString(), 130 ), comref );
			}
			else if ( "G".equals( rgTipo.getVlrString() ) ) {
				imprimirGrafico( bVisualizar, rs, sCab.toString() + "\n" + sCab2.toString(), comref, tipoPorAno );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( Exception err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao montar relatorio!\n" + err.getMessage(), true, con, err );
		} finally {
			System.gc();
		}
	}

	public void imprimirTexto( final TYPE_PRINT bVisualizar, final ResultSet rs, final Vector<?> cab, final boolean bComRef ) {

		if ( "S".equals(cbTipoPorAno.getVlrString() ) ) {
			Funcoes.mensagemInforma( this, "Relatório disponível apenas no formato gráfico !" );
			return;
		}
		String sLinhaFina = StringFunctions.replicate( "-", 133 );
		String sLinhaLarga = StringFunctions.replicate( "=", 133 );
		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		BigDecimal bdQtd = new BigDecimal( "0" );
		BigDecimal bdVlr = new BigDecimal( "0" );

		try {

			imp.limpaPags();
			imp.montaCab();
			imp.setTitulo( "Relatório de Vendas por ítem" );
			imp.addSubTitulo( "RELATORIO DE VENDAS POR ITEM  -  PERIODO DE :" + txtDataini.getVlrString() + " Até: " + txtDatafim.getVlrString() );

			for ( int i = 0; i < cab.size(); i++ ) {
				imp.addSubTitulo( (String) cab.elementAt( i ) );
			}

			while ( rs.next() ) {
				if ( imp.pRow() == linPag ) {
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "+" + sLinhaFina + "+" );
					imp.eject();
					imp.incPags();
				}
				if ( imp.pRow() == 0 ) {
					imp.impCab( 136, true );
					imp.pulaLinha( 0, imp.comprimido() );
					imp.say( 0, "|" );
					imp.say( 135, "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + sLinhaFina + "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 1, "| Cod.prod." );
					imp.say( 14, "| Ref.prod." );
					imp.say( 27, "| Desc.produto" );
					imp.say( 80, "| Unid. " );
					imp.say( 88, "|   Quantidade " );
					imp.say( 113, "|    Vlr.tot.item. " );
					imp.say( 136, "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + sLinhaFina + "|" );
				}

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" );
				imp.say( 2, Funcoes.copy( rs.getString( "CODPROD" ), 0, 10 ) + " | " );
				imp.say( 14, Funcoes.copy( rs.getString( "REFPROD" ), 0, 10 ) + " | " );
				imp.say( 27, Funcoes.copy( rs.getString( "DESCPROD" ), 0, 50 ) + " | " );
				imp.say( 81, Funcoes.copy( rs.getString( "CODUNID" ), 0, 5 ) + " | " );
				imp.say( 89, Funcoes.strDecimalToStrCurrency( 10, 1, rs.getString( "QTDITVENDA" ) ) );
				imp.say( 112, "|" );
				imp.say( 113, Funcoes.strDecimalToStrCurrency( 20, 2, rs.getString( "vlrliqitvenda" ) ) );
				imp.say( 135, "|" );

				bdQtd = bdQtd.add( rs.getBigDecimal( 5 ) );
				bdVlr = bdVlr.add( rs.getBigDecimal( 6 ) );
			}

			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "|" + sLinhaLarga + "|" );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "|" );
			imp.say( 30, "Quant. vendida -> " );
			imp.say( 50, Funcoes.copy( String.valueOf( bdQtd ), 6 ) );
			imp.say( 60, "Valor vendido -> " );
			imp.say( 78, Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( bdVlr ) ) );
			imp.say( 135, "|" );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "+" + sLinhaLarga + "+" );

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
			Funcoes.mensagemErro( this, "Erro ao montar relatorio!\n" + err.getMessage(), true, con, err );
		}
	}

	public void imprimirGrafico( final TYPE_PRINT bVisualizar, final ResultSet rs, final String sCab, final boolean bComRef, final boolean tipoPorAno ) {

		HashMap<String, Object> hParam = new HashMap<String, Object>();
		hParam.put( "COMREF", bComRef ? "S" : "N" );

		String filereport = null;
		if (tipoPorAno) {
			filereport = "relatorios/VendasItem02.jasper";
		} else {
			filereport = "relatorios/VendasItem.jasper";
		}
		FPrinterJob dlGr = new FPrinterJob( filereport, "Vendas por Item", sCab, rs, hParam, this );

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
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement( "SELECT USAREFPROD FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			rs = ps.executeQuery();

			if ( rs.next() ) {
				bRetorno = "S".equals( rs.getString( "UsaRefProd" ) );
			}

			rs.close();
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage(), true, con, err );
		}
		return bRetorno;
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcVend.setConexao( cn );
		lcGrup.setConexao( cn );
		lcMarca.setConexao( cn );
		lcCliente.setConexao( cn );

		comref = comRef();
	}

	private void valorAlteradoTipoPorAno() {
		if ( "S".equals(cbTipoPorAno.getVlrString() ) ) {
			txtAno.setEnabled( true );
			txtDataini.setEnabled( false );
			txtDatafim.setEnabled( false );
			txtAno.requestFocus();
			rgQtdVlr.setAtivo( true );
			//rgOrdem.setAtivo( 3, true );

		} else {
			txtAno.setEnabled( false );
			txtDataini.setEnabled( true );
			txtDatafim.setEnabled( true );
			txtDataini.requestFocus();
			rgQtdVlr.setAtivo( false );
			rgOrdem.setVlrString( "D" );
			//rgOrdem.setAtivo( 3, false );
		}
	}
	
	public void valorAlterado( CheckBoxEvent evt ) {
		if (evt.getCheckBox()==cbTipoPorAno) {
			valorAlteradoTipoPorAno();
		}
	}

	public void focusGained( FocusEvent arg0 ) {
		if (arg0.getSource()==txtAno) {
			if ( ! txtAno.getVlrInteger().equals( Funcoes.getAno( txtDataini.getVlrDate() ) ) ) {
				txtAno.setVlrInteger( Funcoes.getAno( txtDataini.getVlrDate() ) );
			}
		}
		
	}

	public void focusLost( FocusEvent arg0 ) {
		if (arg0.getSource()==txtAno) {
			txtDataini.setVlrDate( Funcoes.encodeDate( txtAno.getVlrInteger(), 1, 1 ) );
			txtDatafim.setVlrDate( Funcoes.encodeDate( txtAno.getVlrInteger(), 12, 31 ) );
		}
		
	}
	
	
}
