/**
 * @version 14/07/2010 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.gms <BR>
 *         Classe:
 * @(#)DLAdicPedCompra.java <BR>
 * 
 *                          Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                          modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                          na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                          Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                          sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                          Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                          Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                          de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                          Dialog para busca e geração de pedido de compra com base em outros pedidos de compra.
 */

package org.freedom.modulos.gms.view.dialog.utility;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.RadioGroupEvent;
import org.freedom.acao.RadioGroupListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.gms.business.object.PrefereGMS;
import org.freedom.modulos.gms.view.frame.crud.detail.FCompra;
import org.freedom.modulos.std.view.dialog.utility.DLCriaVendaCompra;
import org.freedom.modulos.std.view.frame.crud.detail.FPlanoPag;

public class DLBuscaPedCompra extends FDialogo implements ActionListener, RadioGroupListener, CarregaListener, MouseListener, FocusListener {

	private static final long serialVersionUID = 1L;

	private JTablePad tabitcompra = new JTablePad();

	private JScrollPane spntabitcompra = new JScrollPane( tabitcompra );

	private JTablePad tabcompra = new JTablePad();

	private JScrollPane spntabcompra = new JScrollPane( tabcompra );

	private JPanelPad pinCab = new JPanelPad( 0, 105 );

	private JPanelPad pnRod = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnSubRod = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinRod = new JPanelPad( 480, 55 );

	private JPanelPad pinSair = new JPanelPad( 120, 45 );

	private JPanelPad pinBtSel = new JPanelPad( 40, 110 );

	private JPanelPad pinBtSelOrc = new JPanelPad( 40, 110 );

	private JPanelPad pnFor = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnTabCompra = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnForTab = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTextFieldPad txtCodCompra = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 9, 0 );

	private JTextFieldPad txtDocCompra = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 9, 0 );

	private JTextFieldPad txtSerie = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldFK txtDtEntCompra = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldFK txtDtEmitCompra = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRazFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldFK txtStatusCompra = new JTextFieldFK( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtVlrProd = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtVlrDesc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldFK txtVlrProdCompra = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldFK txtVlrLiqCompra = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtVlrLiq = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JButtonPad btBuscar = new JButtonPad( "Buscar", Icone.novo( "btPesquisa.png" ) );

	private JButtonPad btExec = new JButtonPad( Icone.novo( "btExecuta.png" ) );

	private JButtonPad btTodosCompra = new JButtonPad( Icone.novo( "btTudo.png" ) );

	private JButtonPad btNenhumCompra = new JButtonPad( Icone.novo( "btNada.png" ) );

	private JButtonPad btTodosItCompra = new JButtonPad( Icone.novo( "btTudo.png" ) );

	private JButtonPad btNenhumItCompra = new JButtonPad( Icone.novo( "btNada.png" ) );

	private JButtonPad btGerar = new JButtonPad( Icone.novo( "btGerar.png" ) );

	private JButtonPad btAgruparItens = new JButtonPad( Icone.novo( "btAdic2.gif" ) );
	
	private JButtonPad btRecarregaPrecos = new JButtonPad( Icone.novo( "btOrcamento2.gif" ) );

	private JButtonPad btSair = new JButtonPad( "Sair", Icone.novo( "btSair.png" ) );

	private JButtonPad btResetCompra = new JButtonPad( Icone.novo( "btReset.png" ) );

	private JButtonPad btResetItCompra = new JButtonPad( Icone.novo( "btReset.png" ) );

	private ListaCampos lcFor = new ListaCampos( this, "FR" );
	
	private ListaCampos lcPlanoPag = new ListaCampos( this, "PG" );

	private ListaCampos lcCompra = new ListaCampos( this, "CP" );

	private Vector<Object> vValidos = new Vector<Object>();

	private org.freedom.modulos.gms.view.frame.crud.detail.FCompra telacompra = null;

	private PrefereGMS preferegms = PrefereGMS.getInstance();
	
	private boolean bloqprecoaprov = false;
	
	private JTextFieldPad txtCodPlanoPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtDescPlanoPag = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );


	public static enum enum_compra {
		SEL, CODCOMPRA, CODPLANOPAG, CODEMPFR, CODFILIALFR, CODFOR, RAZFOR, NROITENS, NROITENSLIB, VLRLIQCOMPRA, VLRLIB
	}

	public static enum enum_itcompra {
		SEL, CODITCOMPRA, CODEMPPD, CODFILIALPD, CODPROD, DESCPROD, QTDITCOMPRA, PRECOITCOMPRA, VLRDESCITCOMPRA, VLRLIQITCOMPRA, TPAGRUP, AGRUP, VLRAGRUP, CODCOMPRA, CODLOTE, APROVPRECO, CODEMPFR, CODFILIALFR, CODFOR, DTENTCOMPRA
	}

	public DLBuscaPedCompra( Object cp, boolean bloqprecoaprov ) {

		super();

		this.bloqprecoaprov = bloqprecoaprov;
		
		setTitulo( "Busca pedido de compra", this.getClass().getName() );

		telacompra = (FCompra) cp;

		setAtribos( 750, 520 );

		montaTela();

		habilitaCampos();

		montaListaCampos();

		montaTabelas();

		adicListeners();

		adicToolTips();

	}

	private void habilitaCampos() {

		txtVlrProd.setAtivo( false );
		txtVlrDesc.setAtivo( false );
		txtVlrLiq.setAtivo( false );

	}

	private void montaTela() {

		c.setLayout( new BorderLayout() );
		c.add( pnRod, BorderLayout.SOUTH );
		c.add( pnFor, BorderLayout.CENTER );
		c.add( pinCab, BorderLayout.NORTH );

		pinCab.adic( new JLabelPad( "Pedido" ), 7, 5, 60, 20 );
		pinCab.adic( txtCodCompra, 7, 25, 60, 20 );

		pinCab.adic( new JLabelPad( "Cód.For." ), 70, 5, 50, 20 );
		pinCab.adic( txtCodFor, 70, 25, 50, 20 );

		pinCab.adic( new JLabelPad( "Razão social do fornecedor" ), 123, 5, 300, 20 );
		pinCab.adic( txtRazFor, 123, 25, 300, 20 );

//		pinCab.adic( txtStatusCompra, 396, 5, 27, 20 );

		pinCab.adic( new JLabelPad( "Vlr.Prod." ), 426, 5, 77, 20 );
		pinCab.adic( txtVlrProdCompra, 426, 25, 77, 20 );

		pinCab.adic( new JLabelPad( "Vlr.Liq." ), 506, 5, 77, 20 );
		pinCab.adic( txtVlrLiqCompra, 506, 25, 77, 20 );
		
		pinCab.adic( new JLabelPad( "Cód.Pg." ), 7, 45, 60, 20 );
		pinCab.adic( txtCodPlanoPag, 7, 65, 60, 20 );

		pinCab.adic( new JLabelPad( "Descrição do plano de pagamento" ), 70, 45, 353, 20 );
		pinCab.adic( txtDescPlanoPag, 70, 65, 353, 20 );

		pinCab.adic( btBuscar, 632, 20, 100, 30 );

		pnRod.setPreferredSize( new Dimension( 600, 50 ) );

		pnSubRod.setPreferredSize( new Dimension( 600, 50 ) );
		pnRod.add( pnSubRod, BorderLayout.SOUTH );

		pinSair.tiraBorda();
		pinSair.adic( btSair, 10, 10, 100, 30 );
		btSair.setPreferredSize( new Dimension( 120, 30 ) );

		pnSubRod.add( pinSair, BorderLayout.EAST );
		pnSubRod.add( pinRod, BorderLayout.CENTER );

		pinRod.tiraBorda();
		pinRod.adic( new JLabelPad( "Vlr.bruto" ), 7, 0, 100, 20 );
		pinRod.adic( txtVlrProd, 7, 20, 100, 20 );
		pinRod.adic( new JLabelPad( "Vlr.desc." ), 110, 0, 97, 20 );
		pinRod.adic( txtVlrDesc, 110, 20, 97, 20 );
		pinRod.adic( new JLabelPad( "Vlr.liq." ), 210, 0, 97, 20 );
		pinRod.adic( txtVlrLiq, 210, 20, 97, 20 );

		pnTabCompra.setPreferredSize( new Dimension( 600, 133 ) );

		pnTabCompra.add( spntabcompra, BorderLayout.CENTER );
		pnTabCompra.add( pinBtSelOrc, BorderLayout.EAST );

		pinBtSelOrc.adic( btTodosCompra, 3, 3, 30, 30 );
		pinBtSelOrc.adic( btNenhumCompra, 3, 34, 30, 30 );
		pinBtSelOrc.adic( btResetCompra, 3, 65, 30, 30 );
		pinBtSelOrc.adic( btExec, 3, 96, 30, 30 );

		pnForTab.add( spntabitcompra, BorderLayout.CENTER );
		pnForTab.add( pinBtSel, BorderLayout.EAST );

		pinBtSel.adic( btTodosItCompra, 3, 3, 30, 30 );
		pinBtSel.adic( btNenhumItCompra, 3, 34, 30, 30 );
		pinBtSel.adic( btResetItCompra, 3, 65, 30, 30 );
		pinBtSel.adic( btAgruparItens, 3, 96, 30, 30 );
		pinBtSel.adic( btGerar, 3, 127, 30, 30 );
		pinBtSel.adic( btRecarregaPrecos, 3, 158, 30, 30 );

		pnFor.add( pnTabCompra, BorderLayout.NORTH );
		pnFor.add( pnForTab, BorderLayout.CENTER );

	}

	private void adicToolTips() {

		btExec.setToolTipText( "Executar montagem" );
		btTodosCompra.setToolTipText( "Selecionar tudo" );
		btNenhumCompra.setToolTipText( "Limpar seleção" );
		btGerar.setToolTipText( "Gerar compra" );
		btAgruparItens.setToolTipText( "Agrupar ítens" );
		btRecarregaPrecos.setToolTipText( "Recarrega Preços" );

	}

	private void adicListeners() {

		tabitcompra.addKeyListener( this );
		tabitcompra.addMouseListener( this );
		tabcompra.addKeyListener( this );

		btBuscar.addKeyListener( this );
		btGerar.addKeyListener( this );
		btRecarregaPrecos.addKeyListener( this );
		btAgruparItens.addKeyListener( this );

		txtCodCompra.addActionListener( this );
		btSair.addActionListener( this );
		btBuscar.addActionListener( this );
		btExec.addActionListener( this );
		btGerar.addActionListener( this );
		btRecarregaPrecos.addActionListener( this );
		btAgruparItens.addActionListener( this );
		btTodosCompra.addActionListener( this );
		btNenhumCompra.addActionListener( this );
		btTodosItCompra.addActionListener( this );
		btNenhumItCompra.addActionListener( this );
		btResetCompra.addActionListener( this );
		btResetItCompra.addActionListener( this );

		lcCompra.addCarregaListener( this );
		lcPlanoPag.addCarregaListener( this );

		txtCodCompra.addFocusListener( this );

		addWindowListener( this );

	}

	private void montaTabelas() {

		// Monta as tabelas

		tabcompra.adicColuna( "S/N" );
		tabcompra.adicColuna( "Cód.Cp." );
		tabcompra.adicColuna( "Cod.Plan.Pg." );
		tabcompra.adicColuna( "" );
		tabcompra.adicColuna( "" );
		tabcompra.adicColuna( "Cód.For." );
		tabcompra.adicColuna( "Razão do fornecedor" );
		tabcompra.adicColuna( "Nº itens." );
		tabcompra.adicColuna( "Nº lib." );
		tabcompra.adicColuna( "Valor total" );
		tabcompra.adicColuna( "Valor liberado" );

		tabcompra.setTamColuna( 25, enum_compra.SEL.ordinal() );
		tabcompra.setTamColuna( 60, enum_compra.CODCOMPRA.ordinal() );
		
		tabcompra.setColunaInvisivel( enum_compra.CODEMPFR.ordinal() );
		tabcompra.setColunaInvisivel( enum_compra.CODFILIALFR.ordinal() );
		
		tabcompra.setTamColuna( 60, enum_compra.CODFOR.ordinal() );
		tabcompra.setTamColuna( 210, enum_compra.RAZFOR.ordinal() );
		tabcompra.setTamColuna( 60, enum_compra.NROITENS.ordinal() );
		tabcompra.setTamColuna( 60, enum_compra.NROITENSLIB.ordinal() );
		tabcompra.setTamColuna( 100, enum_compra.VLRLIQCOMPRA.ordinal() );
		tabcompra.setTamColuna( 100, enum_compra.VLRLIB.ordinal() );

		tabcompra.setColunaEditavel( enum_compra.SEL.ordinal(), true );
		tabcompra.setColunaInvisivel( enum_compra.CODPLANOPAG.ordinal() );

		tabitcompra.adicColuna( "S/N" );
		tabitcompra.adicColuna( "Ítem" );
		tabitcompra.adicColuna( "" );
		tabitcompra.adicColuna( "" );
		tabitcompra.adicColuna( "Cód.Pd." );
		tabitcompra.adicColuna( "Descrição do produto" );
		tabitcompra.adicColuna( "Qtd." );
		tabitcompra.adicColuna( "Preço" );
		tabitcompra.adicColuna( "Vlr.desc." );
		tabitcompra.adicColuna( "Vlr.liq." );
		tabitcompra.adicColuna( "Tp.Agr." );
		tabitcompra.adicColuna( "Agr." );
		tabitcompra.adicColuna( "Vlr.Agr." );
		tabitcompra.adicColuna( "Compra" );
		tabitcompra.adicColuna( "Lote" );
		tabitcompra.adicColuna( "" );
		tabitcompra.adicColuna( "" );
		tabitcompra.adicColuna( "" );
		tabitcompra.adicColuna( "" );
		tabitcompra.adicColuna( "" ); // Data de entrada

		tabitcompra.setTamColuna( 25, enum_itcompra.SEL.ordinal() );
		tabitcompra.setTamColuna( 30, enum_itcompra.CODITCOMPRA.ordinal() );
		tabitcompra.setColunaInvisivel( enum_itcompra.CODEMPPD.ordinal() );
		tabitcompra.setColunaInvisivel( enum_itcompra.CODFILIALPD.ordinal() );
		tabitcompra.setTamColuna( 50, enum_itcompra.CODPROD.ordinal() );
		tabitcompra.setTamColuna( 190, enum_itcompra.DESCPROD.ordinal() );
		tabitcompra.setTamColuna( 40, enum_itcompra.QTDITCOMPRA.ordinal() );
		tabitcompra.setTamColuna( 60, enum_itcompra.PRECOITCOMPRA.ordinal() );
//		tabitcompra.setColunaEditavel( enum_itcompra.PRECOITCOMPRA.ordinal(), true );
		tabitcompra.setTamColuna( 60, enum_itcompra.VLRDESCITCOMPRA.ordinal() );
		tabitcompra.setTamColuna( 60, enum_itcompra.VLRLIQITCOMPRA.ordinal() );

		tabitcompra.setColunaInvisivel( enum_itcompra.TPAGRUP.ordinal() );
		tabitcompra.setColunaInvisivel( enum_itcompra.AGRUP.ordinal() );

		tabitcompra.setTamColuna( 60, enum_itcompra.VLRAGRUP.ordinal() );
		tabitcompra.setTamColuna( 40, enum_itcompra.CODCOMPRA.ordinal() );

		tabitcompra.setTamColuna( 60, enum_itcompra.CODLOTE.ordinal() );

		tabitcompra.setColunaEditavel( enum_itcompra.SEL.ordinal(), true );
		
		tabitcompra.setTamColuna( 20, enum_itcompra.APROVPRECO.ordinal() );
		tabitcompra.setColunaInvisivel( enum_itcompra.CODEMPFR.ordinal() );
		tabitcompra.setColunaInvisivel( enum_itcompra.CODFILIALFR.ordinal() );
		tabitcompra.setColunaInvisivel( enum_itcompra.CODFOR.ordinal() );
		tabitcompra.setColunaInvisivel( enum_itcompra.DTENTCOMPRA.ordinal() );
		

	}

	private void montaListaCampos() {

		// Lista campos do pedido de compra

		lcCompra.add( new GuardaCampo( txtCodCompra, "CodCompra", "Cód.Comp.", ListaCampos.DB_PK, null, false ) );
		lcCompra.add( new GuardaCampo( txtDocCompra, "DocCompra", "Doc.", ListaCampos.DB_SI, null, false ) );
		lcCompra.add( new GuardaCampo( txtSerie, "Serie", "Serie", ListaCampos.DB_SI, null, false ) );
		lcCompra.add( new GuardaCampo( txtDtEntCompra, "DtEntCompra", "Dt.entrada", ListaCampos.DB_SI, null, false ) );
		lcCompra.add( new GuardaCampo( txtDtEmitCompra, "DtEmitCompra", "Dt.emissão", ListaCampos.DB_SI, null, false ) );
		lcCompra.add( new GuardaCampo( txtCodFor, "CodFor", "Cod.Forn.", ListaCampos.DB_FK, txtRazFor, false ) );
		lcCompra.add( new GuardaCampo( txtVlrProdCompra, "VlrProdCompra", "Vlr.Prod.", ListaCampos.DB_SI, null, false ) );
		lcCompra.add( new GuardaCampo( txtVlrLiqCompra, "VlrLiqCompra", "Vlr.Liq.", ListaCampos.DB_SI, null, false ) );
		lcCompra.add( new GuardaCampo( txtStatusCompra, "StatusCompra", "Status", ListaCampos.DB_SI, null, false ) );
		lcCompra.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cod.Pg.", ListaCampos.DB_FK, txtDescPlanoPag, false ) ); 

		txtCodCompra.setTabelaExterna( lcCompra, null );
		txtCodCompra.setNomeCampo( "CodCompra" );
		txtCodCompra.setFK( true );

		lcCompra.setQueryCommit( false );
		lcCompra.setReadOnly( true );

		txtCodCompra.setListaCampos( lcCompra );
		lcCompra.montaSql( false, "COMPRA", "CP" );

		// Lista campos do fornecedor
		lcFor.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.Forn.", ListaCampos.DB_PK, txtRazFor, false ) );
		lcFor.add( new GuardaCampo( txtRazFor, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );
		txtCodFor.setTabelaExterna( lcFor, null );
		txtCodFor.setNomeCampo( "CodFor" );
		txtCodFor.setFK( true );
		lcFor.setReadOnly( true );
		lcFor.montaSql( false, "FORNECED", "CP" );

		// Plano de pagamento
		lcPlanoPag.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_PK, false ) );
		lcPlanoPag.add( new GuardaCampo( txtDescPlanoPag, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPag.setWhereAdic( "ATIVOPLANOPAG='S' AND CVPLANOPAG IN ('C','A')" );
		lcPlanoPag.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag.setQueryCommit( false );
		lcPlanoPag.setReadOnly( true );
		txtCodPlanoPag.setTabelaExterna( lcPlanoPag, FPlanoPag.class.getCanonicalName() );
		txtCodPlanoPag.setNomeCampo( "CodPlanoPag" );
		txtCodPlanoPag.setFK( true );


		
	}

	private void buscaItCompra() {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer codcompra = null;

		BigDecimal vlrprod = new BigDecimal( 0 );
		BigDecimal vlrdesc = new BigDecimal( 0 );
		BigDecimal vlrliq = new BigDecimal( 0 );

		try {

			tabitcompra.limpa();
			vValidos.clear();

			sql.append( "select " );
			sql.append( "ic.coditcompra, ic.codprod, pd.descprod, ic.qtditcompra, ic.precoitcompra, ic.vlrdescitcompra, " );
			sql.append( "ic.vlrliqitcompra, ic.codcompra, cp.codplanopag, ic.codlote, coalesce(ic.aprovpreco,'N') aprovpreco, " );
			sql.append( "cp.codempfr, cp.codfilialfr, cp.codfor, ic.codemppd, ic.codfilialpd, ic.codprod, cp.dtentcompra " );
			
			sql.append( "from cpcompra cp, cpitcompra ic, eqproduto pd " );

			sql.append( "where ic.codemp=cp.codemp and ic.codfilial=cp.codfilial and ic.codcompra=cp.codcompra " );
			sql.append( "and pd.codemp=ic.codemppd and pd.codfilial=ic.codfilialpd and pd.codprod=ic.codprod " );
			sql.append( "and cp.codemp=? and cp.codfilial=? and ic.codcompra=? " );

			sql.append( "order by ic.codprod " );

			for ( int i = 0; i < tabcompra.getNumLinhas(); i++ ) {

				if ( ! ( (Boolean) tabcompra.getValor( i, enum_compra.SEL.ordinal() ) ).booleanValue() ) {
					continue;
				}

				codcompra = (Integer) tabcompra.getValor( i, enum_compra.CODCOMPRA.ordinal() );

				ps = con.prepareStatement( sql.toString() );

				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
				ps.setInt( 3, codcompra );

				rs = ps.executeQuery();

				int irow = tabitcompra.getNumLinhas();

				String aprovpreco = "N";
				
				while ( rs.next() ) {

					tabitcompra.adicLinha();

					aprovpreco = rs.getString( enum_itcompra.APROVPRECO.toString());
					
					if( (bloqprecoaprov) && ("N".equals( aprovpreco )) ) {
						tabitcompra.setValor( new Boolean( false ), irow, enum_itcompra.SEL.ordinal() );
					}
					else {
						tabitcompra.setValor( new Boolean( true ), irow, enum_itcompra.SEL.ordinal() );	
					}
					
					tabitcompra.setValor( rs.getInt( enum_itcompra.CODITCOMPRA.toString() ), irow, enum_itcompra.CODITCOMPRA.ordinal() );
					tabitcompra.setValor( rs.getInt( enum_itcompra.CODEMPPD.toString() ), irow, enum_itcompra.CODEMPPD.ordinal() );
					tabitcompra.setValor( rs.getInt( enum_itcompra.CODFILIALPD.toString() ), irow, enum_itcompra.CODFILIALPD.ordinal() );
					tabitcompra.setValor( rs.getInt( enum_itcompra.CODPROD.toString() ), irow, enum_itcompra.CODPROD.ordinal() );
					tabitcompra.setValor( rs.getString( enum_itcompra.DESCPROD.toString() ), irow, enum_itcompra.DESCPROD.ordinal() );
 
					tabitcompra.setValor( Funcoes.strDecimalToStrCurrencyd( Aplicativo.casasDec, rs.getString( enum_itcompra.QTDITCOMPRA.toString() ) != null ? rs.getString( enum_itcompra.QTDITCOMPRA.toString() ) : "0" ), irow, enum_itcompra.QTDITCOMPRA.ordinal() );
					tabitcompra.setValor( Funcoes.strDecimalToStrCurrencyd( Aplicativo.casasDecPre, rs.getString( enum_itcompra.PRECOITCOMPRA.toString() ) != null ? rs.getString( enum_itcompra.PRECOITCOMPRA.toString() ) : "0" ), irow, enum_itcompra.PRECOITCOMPRA.ordinal() );
					tabitcompra.setValor( Funcoes.strDecimalToStrCurrencyd( Aplicativo.casasDecFin, rs.getString( enum_itcompra.VLRDESCITCOMPRA.toString() ) != null ? rs.getString( enum_itcompra.VLRDESCITCOMPRA.toString() ) : "0" ), irow, enum_itcompra.VLRDESCITCOMPRA.ordinal() );
					tabitcompra.setValor( Funcoes.strDecimalToStrCurrencyd( Aplicativo.casasDecFin, rs.getString( enum_itcompra.VLRLIQITCOMPRA.toString() ) != null ? rs.getString( enum_itcompra.VLRLIQITCOMPRA.toString() ) : "0" ), irow, enum_itcompra.VLRLIQITCOMPRA.ordinal() );

					tabitcompra.setValor( "", irow, enum_itcompra.TPAGRUP.ordinal() );
					tabitcompra.setValor( "", irow, enum_itcompra.AGRUP.ordinal() );
					tabitcompra.setValor( "0,00", irow, enum_itcompra.VLRAGRUP.ordinal() );

					tabitcompra.setValor( rs.getInt( enum_itcompra.CODCOMPRA.toString() ), irow, enum_itcompra.CODCOMPRA.ordinal() );
					tabitcompra.setValor( rs.getString( enum_itcompra.CODLOTE.toString() ) == null ? "" : rs.getString( enum_itcompra.CODLOTE.toString() ), irow, enum_itcompra.CODLOTE.ordinal() );
					
					tabitcompra.setValor( aprovpreco, irow, enum_itcompra.APROVPRECO.ordinal() );
					tabitcompra.setValor( rs.getInt( enum_itcompra.CODEMPFR.toString() ), irow, enum_itcompra.CODEMPFR.ordinal() );
					tabitcompra.setValor( rs.getInt( enum_itcompra.CODFILIALFR.toString() ), irow, enum_itcompra.CODFILIALFR.ordinal() );
					tabitcompra.setValor( rs.getInt( enum_itcompra.CODFOR.toString() ), irow, enum_itcompra.CODFOR.ordinal() );
					tabitcompra.setValor( Funcoes.dateToStrDate( Funcoes.sqlDateToDate( rs.getDate( enum_itcompra.DTENTCOMPRA.toString() ))), irow, enum_itcompra.DTENTCOMPRA.ordinal() );
 
					vlrprod.add( rs.getBigDecimal( enum_itcompra.PRECOITCOMPRA.toString() ) );
					vlrdesc.add( rs.getBigDecimal( enum_itcompra.VLRDESCITCOMPRA.toString() ) );
					vlrliq.add( rs.getBigDecimal( enum_itcompra.VLRLIQITCOMPRA.toString() ) );

					vValidos.addElement( new Integer[] { codcompra, rs.getInt( enum_itcompra.CODITCOMPRA.toString() ) } );

					irow++;

				}

				con.commit();

			}

			txtVlrProd.setVlrBigDecimal( vlrprod );
			txtVlrDesc.setVlrBigDecimal( vlrdesc );
			txtVlrLiq.setVlrBigDecimal( vlrliq );

		}

		catch ( Exception e ) {
			e.printStackTrace();
		}

	}
	
	private Integer geraCodCompra() { // Traz o verdadeiro número do codCompra
		// através do generator do banco
		
		Integer ret = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement( "SELECT * FROM SPGERANUM(?,?,?)" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			ps.setString( 3, "CP" );
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				ret = rs.getInt( 1 ) ;
			}
			
			// rs.close();
			// ps.close();
			con.commit();
			
		} 
		catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao confirmar código da Compra!\n" + err.getMessage(), true, con, err );
		}
		return ret;
	}

	private boolean geraCompra() {

		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;

		ResultSet rs = null;
		String sSQL = null;

		boolean bPrim = true;
		Integer codcompra = null;
		int[] iValsVec = null;

		StringBuffer obs = new StringBuffer();
		DLCriaVendaCompra diag = null;

		Integer codplanopag = null;
		Integer codfor = null;
		Integer doccompra = null;
		Integer codtipomov = preferegms.getCodtipomovtc();

		try {

			if ( tabitcompra.getNumLinhas() > 0 ) {

				// Loop para pegar o primeiro item selecionado no grid de compras
				// afim de carregar parametros na tela de confirmação de dados para geração da compra.
				for ( int i = 0; i < tabcompra.getNumLinhas(); i++ ) {

					// Se o ítem estiver selecionado no grid de pedidos de compra
					if ( (Boolean) tabcompra.getValor( i, enum_compra.SEL.ordinal() ) ) {

						codplanopag = (Integer) tabcompra.getValor( i, enum_compra.CODPLANOPAG.ordinal() );
						codfor = (Integer) tabcompra.getValor( i, enum_compra.CODFOR.ordinal() );

					}

				}

				diag = new DLCriaVendaCompra( true, "C", false );
				diag.setCodplanopag( codplanopag );
				diag.setCodTipoMov( codtipomov );

				diag.setConexao( con );

//				diag.setNewCodigo( Integer.parseInt( telacompra.lcCampos.getNovoCodigo() ) );
				diag.setNewCodigo( geraCodCompra() );

				diag.setVisible( true );

				if ( diag.OK ) {
					codcompra = diag.getNewCodigo();
					codplanopag = diag.getCodplanopag();
					codtipomov = diag.getCodTipoMov();
					doccompra = diag.getDoc();
				}
				else {
					return false;
				}

				for ( int i = 0; i < tabitcompra.getNumLinhas(); i++ ) {

					if ( ! ( (Boolean) tabitcompra.getValor( i, enum_itcompra.SEL.ordinal() ) ).booleanValue() ) {
						continue;
					}

					// Verifica se deve bloquear preço não aprovado (cotação)
					
					if( (bloqprecoaprov) && ("N".equals( (String) tabitcompra.getValor( i, enum_itcompra.APROVPRECO.ordinal() ))) ) {
						continue;
					}

					if ( bPrim ) {

						try {

							int param = 1;

							// Executando procedure para geração do cabeçalho da compra.

							sSQL = "SELECT IRET FROM CPADICCOMPRAPEDSP(?,?,?,?,?,?,?,?,?,?,?,?,?)";
							ps = con.prepareStatement( sSQL );

							ps.setInt( param++, Aplicativo.iCodEmp );
							ps.setInt( param++, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
							ps.setInt( param++, codcompra );
							ps.setInt( param++, doccompra );

							ps.setInt( param++, Aplicativo.iCodEmp );
							ps.setInt( param++, ListaCampos.getMasterFilial( "EQTIPOMOV" ) );
							ps.setInt( param++, codtipomov );

							ps.setInt( param++, Aplicativo.iCodEmp );
							ps.setInt( param++, ListaCampos.getMasterFilial( "CPFORNECED" ) );
							ps.setInt( param++, codfor );

							ps.setInt( param++, Aplicativo.iCodEmp );
							ps.setInt( param++, ListaCampos.getMasterFilial( "FNPLANOPAG" ) );
							ps.setInt( param++, codplanopag );

							rs = ps.executeQuery();

							if ( rs.next() ) {
								codcompra = rs.getInt( 1 );
							}
							// Se houve algum problema na procedure e não inseriu a compra deve anular a variável para não tentar inserir os ítens
							else {
								codcompra = null;
							}

							rs.close();
							ps.close();

						} catch ( SQLException err ) {
							if ( err.getErrorCode() == 335544665 ) {
								Funcoes.mensagemErro( this, "Compra já existe!" );
								return geraCompra();
							}
							else
								Funcoes.mensagemErro( this, "Erro ao gerar compra!\n" + err.getMessage(), true, con, err );
							codcompra = null;

							err.printStackTrace();
							return false;
						} catch ( Exception e ) {
							Funcoes.mensagemErro( this, "Erro genérico ao gerar compra!\n" + e.getMessage(), true, con, e );
							codcompra = null;
						}
						bPrim = false;
					}

					// Se o cabeçalho da compra foi inserido corretamente, o código da compra não é nulo, portanto deve inserir os ítens.
					if ( codcompra != null ) {

						try {

							sSQL = "EXECUTE PROCEDURE CPADICITCOMPRAPEDSP(?,?,?,?,?,?,?,?,?,?,?)";
							ps2 = con.prepareStatement( sSQL );
							ps2.setInt( 1, Aplicativo.iCodEmp );
							ps2.setInt( 2, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
							ps2.setInt( 3, codcompra );
							ps2.setInt( 4, Aplicativo.iCodEmp );
							ps2.setInt( 5, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
							ps2.setInt( 6, (Integer) tabitcompra.getValor( i, enum_itcompra.CODCOMPRA.ordinal() ) );
							ps2.setInt( 7, (Integer) tabitcompra.getValor( i, enum_itcompra.CODITCOMPRA.ordinal() ) );
							ps2.setString( 8, (String) tabitcompra.getValor( i, enum_itcompra.TPAGRUP.ordinal() ) );
							ps2.setFloat( 9, new Float( Funcoes.strCurrencyToDouble( tabitcompra.getValor( i, enum_itcompra.QTDITCOMPRA.ordinal() ).toString() ) ) );
							ps2.setFloat( 10, new Float( Funcoes.strCurrencyToDouble( tabitcompra.getValor( i, enum_itcompra.VLRDESCITCOMPRA.ordinal() ).toString() ) ) );
							ps2.setFloat( 11, new Float( Funcoes.strCurrencyToDouble( tabitcompra.getValor( i, enum_itcompra.PRECOITCOMPRA.ordinal() ).toString() ) ) );

							ps2.execute();
							ps2.close();

						} catch ( SQLException err ) {
							Funcoes.mensagemErro( this, "Erro ao gerar itcompra: '" + ( i + 1 ) + "'!\n" + err.getMessage(), true, con, err );
							try {
								con.rollback();
							} catch ( SQLException err1 ) {
								err1.printStackTrace();
							}
							err.printStackTrace();
							return false;

						}

					}

				}

				if ( Funcoes.mensagemConfirma( null, "Compra '" + codcompra + "' gerada com sucesso!!!\n\n" + "Deseja edita-la?" ) == JOptionPane.YES_OPTION ) {
					telacompra.exec( codcompra );
					dispose();
				}
				// }
				// PDV

			}
			else
				Funcoes.mensagemInforma( this, "Não existe nenhum item pra gerar uma compra!" );
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			ps = null;
			ps2 = null;
			rs = null;
			iValsVec = null;
			diag = null;
		}

		return true;
	}

	private void buscaCompra() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		Vector<Object> vVals = null;

		try {

			if ( txtCodFor.getVlrInteger() > 0 || txtCodCompra.getVlrInteger() > 0 ) {

				sql.append( "select cp.statuscompra, cp.codcompra, cp.codplanopag, cp.codfor, fr.razfor, " );
				sql.append( "(select count(*) from cpitcompra ic where ic.codemp=cp.codemp and ic.codfilial=cp.codfilial and ic.codcompra=cp.codcompra) nroitens , " );
				sql.append( "(select count(*) from cpitcompra ic where ic.codemp=cp.codemp and ic.codfilial=cp.codfilial and ic.codcompra=cp.codcompra) nroitenslib, " );
				sql.append( "cp.vlrliqcompra, cp.vlrliqcompra vlrlib " );
				sql.append( "from cpcompra cp, cpforneced fr " );
				sql.append( "where " );
				sql.append( "fr.codemp=cp.codempfr and fr.codfilial=cp.codfilialfr and fr.codfor=cp.codfor " );
				sql.append( "and cp.statuscompra in ('P1','P2','P3') and cp.impnotacompra='N' " );

				if ( txtCodFor.getVlrInteger() > 0 && txtCodCompra.getVlrInteger() <= 0 ) {
					sql.append( "and cp.codempfr=? and cp.codfilialfr=? and cp.codfor=? " );
				}

				sql.append( "and cp.codemp=? and cp.codfilial=? " );

				if ( txtCodCompra.getVlrInteger() > 0 ) {
					sql.append( " and cp.codcompra=? " );
				}

				ps = con.prepareStatement( sql.toString() );

				int param = 1;

				if ( txtCodFor.getVlrInteger() > 0 && txtCodCompra.getVlrInteger() <= 0 ) {
					ps.setInt( param++, lcFor.getCodEmp() );
					ps.setInt( param++, lcFor.getCodFilial() );
					ps.setInt( param++, txtCodFor.getVlrInteger() );
				}

				ps.setInt( param++, Aplicativo.iCodEmp );
				ps.setInt( param++, ListaCampos.getMasterFilial( "CPCOMPRA" ) );

				if ( txtCodCompra.getVlrInteger() > 0 ) {
					ps.setInt( param++, txtCodCompra.getVlrInteger() );
				}

				rs = ps.executeQuery();

				tabcompra.limpa();

				int irow = 0;

				while ( rs.next() ) {

					tabcompra.adicLinha();

					tabcompra.setValor( new Boolean( true ), irow, enum_compra.SEL.ordinal() );

					tabcompra.setValor( rs.getInt( enum_compra.CODCOMPRA.toString() ), irow, enum_compra.CODCOMPRA.ordinal() );
					tabcompra.setValor( rs.getInt( enum_compra.CODPLANOPAG.toString() ), irow, enum_compra.CODPLANOPAG.ordinal() );
					tabcompra.setValor( rs.getInt( enum_compra.CODFOR.toString() ), irow, enum_compra.CODFOR.ordinal() );
					tabcompra.setValor( rs.getString( enum_compra.RAZFOR.toString() ), irow, enum_compra.RAZFOR.ordinal() );

					tabcompra.setValor( Funcoes.strDecimalToStrCurrencyd( 2, rs.getString( enum_compra.NROITENS.toString() ) ), irow, enum_compra.NROITENS.ordinal() );
					tabcompra.setValor( Funcoes.strDecimalToStrCurrencyd( 2, rs.getString( enum_compra.NROITENSLIB.toString() ) ), irow, enum_compra.NROITENSLIB.ordinal() );
					tabcompra.setValor( Funcoes.strDecimalToStrCurrencyd( 2, rs.getString( enum_compra.VLRLIQCOMPRA.toString() ) ), irow, enum_compra.VLRLIQCOMPRA.ordinal() );
					tabcompra.setValor( Funcoes.strDecimalToStrCurrencyd( 2, rs.getString( enum_compra.VLRLIB.toString() ) ), irow, enum_compra.VLRLIB.ordinal() );

					irow++;

				}

				rs.close();
				ps.close();
			}
			else {
				Funcoes.mensagemInforma( this, "Selecione um pedido ou um fornecedor para busca!" );
			}
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar compras!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			vVals = null;
		}

	}

	private void limpaNaoSelecionados( JTablePad ltab ) {

		int linhas = ltab.getNumLinhas();
		int pos = 0;
		try {
			for ( int i = 0; i < linhas; i++ ) {
				if ( ! ( (Boolean) ltab.getValor( i, 0 ) ).booleanValue() ) {
					ltab.tiraLinha( i );
					vValidos.remove( i );
					i--;
				}
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private void limpaFilhos( JTablePad ltab ) {

		int linhas = ltab.getNumLinhas();
		int pos = 0;

		try {
			for ( int i = 0; i < linhas; i++ ) {
				if ( "F".equals( ltab.getValor( i, enum_itcompra.TPAGRUP.ordinal() ).toString() ) ) {
					ltab.tiraLinha( i );
					i--;
				}
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private Float marcaFilhos( final int iLinha, final Integer codprodpai, final Float precopai ) {

		Integer codprodfilho = null;
		Float precofilho = null;
		Float vlrliqfilho = null;
		Float qtdfilho = null;
		Float ret = new Float( 0 );
		String tpagrup = null;
		int i = iLinha;
		int iPai = iLinha - 1;

		try {
			while ( i < tabitcompra.getNumLinhas() ) {

				codprodfilho = new Integer( tabitcompra.getValor( i, enum_itcompra.CODPROD.ordinal() ).toString() );
				qtdfilho = new Float( Funcoes.strCurrencyToDouble( tabitcompra.getValor( i, enum_itcompra.QTDITCOMPRA.ordinal() ).toString() ) );
				vlrliqfilho = new Float( Funcoes.strCurrencyToDouble( tabitcompra.getValor( i, enum_itcompra.VLRLIQITCOMPRA.ordinal() ).toString() ) );
				tpagrup = tabitcompra.getValor( i, enum_itcompra.TPAGRUP.ordinal() ).toString();
				precofilho = new Float( Funcoes.strCurrencyToDouble( tabitcompra.getValor( i, enum_itcompra.PRECOITCOMPRA.ordinal() ).toString() ) );

				if ( ( codprodfilho.compareTo( codprodpai ) == 0 ) && ( precopai.compareTo( precofilho ) == 0 ) ) {
					tabitcompra.setValor( "F", i, enum_itcompra.TPAGRUP.ordinal() );
					ret += qtdfilho;
					tabitcompra.setValor( "AGRUPADO", i, enum_itcompra.DESCPROD.ordinal() );
				}

				i++;
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return ret;
	}

	private void agrupaItens() {

		Integer codprodpai = null;
		Float vlrdescnovopai = new Float( 0 );
		Float qtdatupai = null;
		Float qtdnovopai = new Float( 0 );
		Float precopai = null;
		String tpagr = "";

		try {
			limpaNaoSelecionados( tabitcompra );

			int linhaPai = -1;

			for ( int i = 0; i < tabitcompra.getNumLinhas(); i++ ) {

				codprodpai = new Integer( tabitcompra.getValor( i, enum_itcompra.CODPROD.ordinal() ).toString() );
				qtdatupai = new Float( Funcoes.strCurrencyToDouble( tabitcompra.getValor( i, enum_itcompra.QTDITCOMPRA.ordinal() ).toString() ) );
				precopai = new Float( Funcoes.strCurrencyToDouble( tabitcompra.getValor( i, enum_itcompra.PRECOITCOMPRA.ordinal() ).toString() ) );
				tpagr = tabitcompra.getValor( i, enum_itcompra.TPAGRUP.ordinal() ).toString();
				vlrdescnovopai += new Float( Funcoes.strCurrencyToDouble( tabitcompra.getValor( i, enum_itcompra.VLRDESCITCOMPRA.ordinal() ).toString() ) );

				if ( tpagr.equals( "" ) ) {
					qtdnovopai = qtdatupai;
					qtdnovopai += marcaFilhos( i + 1, codprodpai, precopai );

					if ( qtdatupai.compareTo( qtdnovopai ) != 0 ) {
						tabitcompra.setValor( "P", i, enum_itcompra.TPAGRUP.ordinal() );
						tabitcompra.setValor( Funcoes.strDecimalToStrCurrencyd( 2, String.valueOf( qtdnovopai ) ), i, enum_itcompra.QTDITCOMPRA.ordinal() );
						linhaPai = i;
					}
					else {
						tabitcompra.setValor( "N", i, enum_itcompra.TPAGRUP.ordinal() );
					}
				}
			}

			if ( linhaPai > -1 ) {
				tabitcompra.setValor( Funcoes.strDecimalToStrCurrencyd( 2, String.valueOf( vlrdescnovopai ) ), linhaPai, enum_itcompra.VLRDESCITCOMPRA.ordinal() );
			}

			// limpaFilhos( tabitcompra );

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private void carregaTudo( JTablePad tb ) {

		for ( int i = 0; i < tb.getNumLinhas(); i++ ) {
			tb.setValor( new Boolean( true ), i, 0 );
		}
	}

	private void carregaNada( JTablePad tb ) {

		for ( int i = 0; i < tb.getNumLinhas(); i++ ) {
			tb.setValor( new Boolean( false ), i, 0 );
		}
	}

	public void keyPressed( KeyEvent kevt ) {

		if ( kevt.getKeyCode() == KeyEvent.VK_ENTER ) {
			if ( kevt.getSource() == btBuscar ) {
				btBuscar.doClick();
				tabcompra.requestFocus();
			}
			else if ( kevt.getSource() == tabcompra ) {
				btExec.doClick();
				tabitcompra.requestFocus();
			}
			else if ( kevt.getSource() == btGerar ) {
				if ( !geraCompra() ) {
					try {
						con.rollback();
					} catch ( SQLException err ) {
						Funcoes.mensagemErro( this, "Erro ao realizar rollback!!\n" + err.getMessage(), true, con, err );
					}
				}
			}
			else if ( kevt.getSource() == tabitcompra ) {
				btGerar.requestFocus();
			}
		}
		// super.keyPressed(kevt);
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btSair ) {
			dispose();
		}
		else if ( evt.getSource() == btBuscar ) {
			buscaCompra();
		}
		else if ( evt.getSource() == btExec ) {
			buscaItCompra();
		}
		else if ( evt.getSource() == btGerar ) {
			if ( !geraCompra() ) {
				try {
					con.rollback();
				} catch ( SQLException err ) {
					Funcoes.mensagemErro( this, "Erro ao realizar rollback!!\n" + err.getMessage(), true, con, err );
				}
			}
		}
		else if ( evt.getSource() == btAgruparItens ) {
			try {
				if ( Funcoes.mensagemConfirma( null, "Confirma o agrupamento dos ítens iguais?\nSerão agrupados apenas os ítens de código e preços iguais." ) == JOptionPane.YES_OPTION ) {
					agrupaItens(); // comentar
				}
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro ao realizar agrupamento de ítens!!\n" + err.getMessage(), true, con, err );
			}
		}
		else if ( evt.getSource() == btTodosCompra ) {
			carregaTudo( tabcompra );
		}
		else if ( evt.getSource() == btNenhumCompra ) {
			carregaNada( tabcompra );
		}
		else if ( evt.getSource() == btTodosItCompra ) {
			carregaTudo( tabitcompra );
		}
		else if ( evt.getSource() == btNenhumItCompra ) {
			carregaNada( tabitcompra );
		}
		else if ( evt.getSource() == txtCodCompra ) {
			if ( txtCodCompra.getVlrInteger().intValue() > 0 )
				btBuscar.requestFocus();
		}
		else if ( evt.getSource() == btResetCompra ) {
			tabcompra.limpa();
			tabitcompra.limpa();
		}
		else if ( evt.getSource() == btResetItCompra ) {
			tabitcompra.limpa();
		}
		else if ( evt.getSource() == btRecarregaPrecos ) {

			if(txtCodPlanoPag.getVlrInteger()>0) {
				recarregaPrecos();
			}
			else {
				Funcoes.mensagemInforma( null, "Para recarregar os preços de cotações é necessário informar\n o plano de pagamento!" );
				txtCodPlanoPag.requestFocus();
			}
		}


	}

	
	private void recarregaPrecos() {
		StringBuilder sql_preco = new StringBuilder();
		StringBuilder sql_update = new StringBuilder();
		StringBuilder sql_update_status_preco = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		BigDecimal preconovo = null;
		
		try {
			
			// Query para atualizar o preço;
			sql_update.append( "update cpitcompra ic ");
			sql_update.append( "set ic.precoitcompra=?, ic.aprovpreco=?, ic.vlrliqitcompra = ( (? * ic.qtditcompra) - (coalesce(ic.vlrdescitcompra,0) ) ) ");
			sql_update.append( "where ic.codemp=? and ic.codfilial=? and ic.codcompra=? and ic.coditcompra=? " ); 
			
			// Query para atualizar o status do preco;
			sql_update_status_preco.append( "update cpitcompra ic ");
			sql_update_status_preco.append( "set ic.aprovpreco=? ");
			sql_update_status_preco.append( "where ic.codemp=? and ic.codfilial=? and ic.codcompra=? and ic.coditcompra=? " );
			
			// Se deve buscar preço de cotação.			
			sql_preco.append( "select first 1 ct.precocot ");
			sql_preco.append( "from cpcotacao ct, cpsolicitacao sl, cpitsolicitacao iso, cpcompra cp ");
			
			sql_preco.append( "left outer join eqrecmerc rm on ");
			sql_preco.append( "rm.codemp=cp.codemprm and rm.codfilial=cp.codfilialrm and rm.ticket=cp.ticket ");
			
			sql_preco.append( "where ");
			sql_preco.append( "iso.codemp = sl.codemp and iso.codfilial=sl.codfilial and iso.codsol=sl.codsol ");
			sql_preco.append( "and ct.codemp=iso.codemp and ct.codfilial=iso.codfilial and ct.codsol=iso.codsol and ct.coditsol=iso.coditsol ");
			sql_preco.append( "and iso.codemppd=? and iso.codfilialpd=? and iso.codprod=? ");
			sql_preco.append( "and ct.codempfr=? and ct.codfilialfr=? and ct.codfor=? ");
			sql_preco.append( "and cp.codemp=? and cp.codfilial=? and cp.codcompra=? ");

			//Filtro da validade e da data de vigor 
			sql_preco.append( "and (ct.dtvalidcot>=? and (ct.dtcot<=? ))");
			
			//Filtro do statuso
			sql_preco.append( "and ct.sititsol not in ('EF','CA') ");
			
			// Filtro da renda
			sql_preco.append( "and ( (ct.rendacot = rm.rendaamostragem) or ( coalesce(ct.usarendacot,'N') = 'N') ) ");

			// Filtro do plano de pagamento
			sql_preco.append( "and ( (ct.codemppp=? and ct.codfilialpp=? and ct.codplanopag=? ) ");
			sql_preco.append( "   or (ct.codplanopag is null)) ");
			
			sql_preco.append( "order by ct.dtcot desc ");
			
			Integer codemppd = null;
			Integer codfilialpd = null;
			Integer codprod = null;
			Integer codempfr = null;
			Integer codfilialfr = null;
			Integer codfor = null;
			Integer codempcp = null;
			Integer codfilialcp = null;
			Integer codcompra = null;
			Date dtent = null;
			
			for(int i = 0; tabitcompra.getNumLinhas() > i; i++) {
				
				codemppd = (Integer) tabitcompra.getValor( i, enum_itcompra.CODEMPPD.ordinal() );
				codfilialpd = (Integer) tabitcompra.getValor( i, enum_itcompra.CODFILIALPD.ordinal() );				
				codprod = (Integer) tabitcompra.getValor( i, enum_itcompra.CODPROD.ordinal() );
				
				codempfr = (Integer) tabitcompra.getValor( i, enum_itcompra.CODEMPFR.ordinal() );
				codfilialfr = (Integer) tabitcompra.getValor( i, enum_itcompra.CODFILIALFR.ordinal() );				
				codfor = Integer.parseInt( tabitcompra.getValor( i, enum_itcompra.CODFOR.ordinal() ).toString());
				
				codcompra = Integer.parseInt( tabitcompra.getValor( i, enum_itcompra.CODCOMPRA.ordinal() ).toString());

				dtent = Funcoes.strDateToDate( tabitcompra.getValor( i, enum_itcompra.DTENTCOMPRA.ordinal() ).toString()); 

				ps = con.prepareStatement( sql_preco.toString() );
				
				ps.setInt( 1, codemppd );
				ps.setInt( 2, codfilialpd );
				ps.setInt( 3, codprod );
				
				ps.setInt( 4, codempfr );
				ps.setInt( 5, codfilialfr );
				ps.setInt( 6, codfor );
				
				ps.setInt( 7, Aplicativo.iCodEmp );
				ps.setInt( 8, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
				ps.setInt( 9, codcompra );
								
				ps.setDate( 10, Funcoes.dateToSQLDate( dtent ) );
				ps.setDate( 11, Funcoes.dateToSQLDate( dtent ) );
				ps.setInt( 12, Aplicativo.iCodEmp );
				ps.setInt( 13, ListaCampos.getMasterFilial( "FNPLANOPAG" ) );
				ps.setInt( 14, txtCodPlanoPag.getVlrInteger() );
			
				rs = ps.executeQuery();
			
			
				if(rs.next()) {
					
					preconovo = rs.getBigDecimal( "precocot" ); 
				
				}
				else { 
					preconovo = null;
				}
			
				if(preconovo!=null) {

					ps = con.prepareStatement( sql_update.toString() );
				
					ps.setBigDecimal( 1, preconovo );
					ps.setString( 2, "S" );
					ps.setBigDecimal( 3, preconovo );					
					ps.setInt( 4, Aplicativo.iCodEmp );
					ps.setInt( 5, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
					ps.setInt( 6, (Integer) tabitcompra.getValor( i, enum_itcompra.CODCOMPRA.ordinal() ) );
					ps.setInt( 7, (Integer) tabitcompra.getValor( i, enum_itcompra.CODITCOMPRA.ordinal() ) );					
					
					System.out.println("preço atualizado: sql:" + sql_update.toString());
					System.out.println("preço novo:" + preconovo );
					System.out.println("compra:" +  tabitcompra.getValor( i, enum_itcompra.CODCOMPRA.ordinal() ) + "-" +  tabitcompra.getValor( i, enum_itcompra.CODITCOMPRA.ordinal() ) );
					

					ps.executeUpdate();
					ps.close();
					con.commit();
					
				}
				else {
					ps = con.prepareStatement( sql_update_status_preco.toString() );
					
					ps.setString( 1, "N" );
					
					ps.setInt( 2, Aplicativo.iCodEmp );
					ps.setInt( 3, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
					ps.setInt( 4, (Integer) tabitcompra.getValor( i, enum_itcompra.CODCOMPRA.ordinal() ) );
					ps.setInt( 5, (Integer) tabitcompra.getValor( i, enum_itcompra.CODITCOMPRA.ordinal() ) );					
					
					System.out.println("status preço atualizado: sql:" + sql_update.toString());					
					System.out.println("compra:" +  tabitcompra.getValor( i, enum_itcompra.CODCOMPRA.ordinal() ) + "-" +  tabitcompra.getValor( i, enum_itcompra.CODITCOMPRA.ordinal() ) );

					ps.executeUpdate();
					ps.close();
					con.commit();
					
				}
			}
			
			buscaItCompra();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void beforeCarrega( CarregaEvent e ) {

	}

	public void afterCarrega( CarregaEvent e ) {

		if ( e.getListaCampos() == lcCompra ) {
			txtCodFor.setAtivo( false );
			// lcFor.limpaCampos( true );
		}
		else if(e.getListaCampos()==lcPlanoPag && bloqprecoaprov) {
			recarregaPrecos();			
		}
	

	}

	public void valorAlterado( RadioGroupEvent rgevt ) {

		lcCompra.limpaCampos( true );
	}

	public void firstFocus() {

		txtCodCompra.requestFocus();
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcFor.setConexao( cn );
		lcCompra.setConexao( cn );		
		lcPlanoPag.setConexao( cn );

		txtCodCompra.setFocusable( true );
		setFirstFocus( txtCodCompra );
	}

	public void mouseClicked( MouseEvent e ) {

		if ( e.getSource() == tabitcompra ) {

		}

	}

	public void mouseEntered( MouseEvent arg0 ) {

	}

	public void mouseExited( MouseEvent arg0 ) {

	}

	public void mousePressed( MouseEvent arg0 ) {

	}

	public void mouseReleased( MouseEvent arg0 ) {

	}

	public void focusGained( FocusEvent arg0 ) {

		// TODO Auto-generated method stub

	}

	public void focusLost( FocusEvent evt ) {

		if ( evt.getSource() == txtCodCompra ) {
			if ( txtCodCompra.getVlrInteger() > 0 ) {
				txtCodFor.setAtivo( false );
			}
			else {
				txtCodFor.setAtivo( true );
			}
		}

	}
}
