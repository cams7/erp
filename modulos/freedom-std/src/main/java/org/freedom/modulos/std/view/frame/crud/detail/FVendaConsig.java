/*
 * Projeto: Freedom Pacote: org.freedom.modules.std Classe: @(#)FVendaConsig.java
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR> modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR> na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR> sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR> Veja a Licença Pública Geral GNU para maiores detalhes. <BR> Você deve ter recebido uma cópia da Licença Pública
 * Geral GNU junto com este programa, se não, <BR> escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA <BR> <BR>
 */
package org.freedom.modulos.std.view.frame.crud.detail;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDetalhe;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.std.view.dialog.utility.DLBuscaListaVendas;
import org.freedom.modulos.std.view.frame.crud.special.FSubLanca;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCliente;
import org.freedom.modulos.std.view.frame.crud.tabbed.FVendedor;

/**
 * Manutenção de vendas consignadas.<br>
 * <br>
 * Gerencia processo de vendas consignadas, a partir da remessa, seguido do registro de vendas, finalizando com os lançamentos financeiros na conta do comissionado.
 * 
 * @author Setpoint Informática Ltda./Alex Rodrigues
 * @version 31/08/2009
 */
public class FVendaConsig extends FDetalhe implements MouseListener, ChangeListener, InsertListener {

	private static final long serialVersionUID = 1L;

	private enum ETabReceber {
		SEL, STATUS, CODREC, NPARCITREC, CODCLI, RAZCLI, CODPLANOPAG, DESCPLANOPAG, VENCIMENTO, PAGAMENTO, VLRPARCELA, VLRPAGO, VLRDESCONTO, VLRJUROS, VLRMULTA, VLRAPAGAR;
	};

	private enum ETabFechamento {
		NLANCA, CODSUBLANCA, DATA, DOC, VALOR, HISTORICO
	};

	private JTabbedPanePad tpnPrincipal = new JTabbedPanePad();

	private JTabbedPanePad tpnGeral = new JTabbedPanePad();

	private JPanelPad panelGeral = new JPanelPad( 500, 80 );

	private JPanelPad panelGeralDetalhe = new JPanelPad( 500, 80 );

	private JPanelPad panelRemessaCampos = new JPanelPad( new GridLayout( 1, 1 ) );

	private JPanelPad panelDevolucaoCampos = new JPanelPad( new GridLayout( 1, 1 ) );

	private JPanelPad panelVendas = new JPanelPad( new BorderLayout() );

	private JPanelPad panelVendasCab = new JPanelPad( 500, 70 );

	private JPanelPad panelVendasDetale = new JPanelPad( new BorderLayout() );

	private JPanelPad panelVendasCampos = new JPanelPad( 500, 110 );

	private JPanelPad panelVendasNavegador = new JPanelPad( new BorderLayout() );

	private JTablePad tabVendas = new JTablePad();

	private JScrollPane spVendas = new JScrollPane( tabVendas );

	private JPanelPad panelReceber = new JPanelPad( new BorderLayout() );

	private JPanelPad panelReceberFiltros = new JPanelPad();

	private JPanelPad panelReceberFuncoes = new JPanelPad( 44, 500 );

	private JPanelPad panelReceberStatus = new JPanelPad( 500, 50 );

	private JTablePad tabReceber = new JTablePad();

	private JScrollPane spReceber = new JScrollPane( tabReceber );

	private JPanelPad panelFechamento = new JPanelPad( new BorderLayout() );

	private JPanelPad panelFechamentoCampos = new JPanelPad( 500, 80 );

	private JTablePad tabFechamento = new JTablePad();

	private JScrollPane spFechamento = new JScrollPane( tabFechamento );

	// campos da aba geral

	private JTextFieldPad txtCodConsig = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtNomeVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtDocConsig = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtDataConsig = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JButtonPad btImportarVendaRemessa = new JButtonPad( "Importar nota de remessa", Icone.novo( "btFinalizaOP.png" ) );

	// campos da aba detale geral

	private JTextFieldPad txtCodRemCo = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtCodProdRem = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtDescProdRem = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtPrecoRem = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecPre );

	private JTextFieldPad txtQtdSaidaRem = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private JTextFieldPad txtQtdDevolucaoRem = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private JLabelPad lbQtdSaidaRem = new JLabelPad( "Qtd. Saída" );

	private JLabelPad lbQtdDevolucaoRem = new JLabelPad( "Qtd. Devolução" );

	// campos da aba venda

	private JTextFieldPad txtCodCliVenda = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtRazCliVenda = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCodPlanoPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtDescPlanoPag = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodVendaCo = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtCodProdVenda = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtDescProdVenda = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtPreco = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecPre );

	private JTextFieldPad txtPrecoVenda = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecPre );

	private JTextFieldPad txtQtdVenda = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private JTextFieldPad txtQtdTroca = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private JTextFieldPad txtQtdBonif = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private JTextFieldPad txtVlrDescVenda = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JCheckBoxPad cbRecebido = new JCheckBoxPad( "Recebido", "S", "N" );

	private JButtonPad btImportarItensVenda = new JButtonPad( "Importar itens de venda", Icone.novo( "btVenda2.gif" ) );

	// campos da aba contas a receber

	private JTextFieldPad txtDataIniRedeber = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDataFimReceber = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JRadioGroup<String, String> rgDataReceber = null;

	private JTextFieldPad txtCodCliRec = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtRazCliRec = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JCheckBoxPad cbRecParcial = new JCheckBoxPad( "Parcial", "S", "N" );

	private JCheckBoxPad cbAReceber = new JCheckBoxPad( "À Receber", "S", "N" );

	private JCheckBoxPad cbRecebidas = new JCheckBoxPad( "Recebidas", "S", "N" );

	private JButtonPad btPesquisaReceber = new JButtonPad( "Pesquisar", Icone.novo( "btPesquisa.png" ) );

	private JButtonPad btSelecionTodosReceber = new JButtonPad( Icone.novo( "btTudo.png" ) );

	private JButtonPad btSelecionaNenhumReceber = new JButtonPad( Icone.novo( "btNada.png" ) );

	private JButtonPad btColocarEmCobrancaReceber = new JButtonPad( Icone.novo( "btGerar.png" ) );

	private JTextFieldPad txtVlrRecVencido = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtVlrRecPagoParcial = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtVlrRecPago = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtVlrRecAVencer = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtVlrEmCobranca = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	// campos da aba fechamento

	private JTextFieldPad txtRemessas = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 12, Aplicativo.casasDecFin );

	private JTextFieldPad txtDevolucoes = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 12, Aplicativo.casasDecFin );

	private JTextFieldPad txtVendasConsig = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 12, Aplicativo.casasDecFin );

	private JTextFieldPad txtVendas = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 12, Aplicativo.casasDecFin );

	private JTextFieldPad txtDescontos = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 12, Aplicativo.casasDecFin );

	private JTextFieldPad txtTrocas = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 12, Aplicativo.casasDecFin );

	private JTextFieldPad txtBonificacoes = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 12, Aplicativo.casasDecFin );

	private JTextFieldPad txtTotalFechamento = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 12, Aplicativo.casasDecFin );

	private JTextFieldPad txtSaldoFechamento = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 12, Aplicativo.casasDecFin );

	private JButtonPad btConsolidacao = new JButtonPad( "Consolidar", Icone.novo( "btReset.png" ) );

	private JButtonPad btNovoLancamento = new JButtonPad( "Lançamento", Icone.novo( "btNovo.png" ) );

	private ListaCampos lcVendedor = new ListaCampos( this, "VD" );

	private ListaCampos lcProdutoRemessa = new ListaCampos( this, "PD" );

	private ListaCampos lcDetVendas = new ListaCampos( this );

	private ListaCampos lcProdutoVenda = new ListaCampos( this, "PD" );

	private ListaCampos lcClienteVenda = new ListaCampos( this, "CL" );

	private ListaCampos lcPlanoPagamento = new ListaCampos( this, "PG" );

	private ListaCampos lcClienteRec = new ListaCampos( this, "CL" );

	private Navegador navegadorVendas = new Navegador( true );

	private ImageIcon imgVencido = Icone.novo( "clVencido.gif" );

	private ImageIcon imgPagoParcial = Icone.novo( "clPagoParcial.gif" );

	private ImageIcon imgPago = Icone.novo( "clPago.gif" );

	private ImageIcon imgNaoVencido = Icone.novo( "clNaoVencido.gif" );

	private ImageIcon imgEmCobranca = Icone.novo( "clInativo.gif" );

	private ImageIcon imgColuna = null;

	private Integer oldCliente = 0;

	private Integer oldPlanoPagamento = 0;

	private BigDecimal valorReceberVencido = new BigDecimal( "0.00" );

	private BigDecimal valorReceberPagoParcial = new BigDecimal( "0.00" );

	private BigDecimal valorReceberPago = new BigDecimal( "0.00" );

	private BigDecimal valorReceberAVencer = new BigDecimal( "0.00" );

	private BigDecimal valorEmCobranca = new BigDecimal( "0.00" );

	private JTextFieldPad txtSQLRemessa = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtSQLDevolucao = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private int codempco, codfilialco;

	private String codplanconsig;

	private int codempvd, codfilialvd;

	private String codplanvdconsig;

	public FVendaConsig() {

		super( false );
		setTitulo( "Vendas Consignadas" );
		setAtribos( 50, 50, 750, 605 );

		montaListaCampos();

		montaTela();

		navRod.setListaCampos( lcDet );

		lcCampos.addInsertListener( this );
		lcDet.addInsertListener( this );
		lcDetVendas.addInsertListener( this );

		tpnGeral.addChangeListener( this );
		tpnPrincipal.addChangeListener( this );

		tabReceber.addMouseListener( this );
		tabFechamento.addMouseListener( this );

		btImportarVendaRemessa.addActionListener( this );
		btImportarItensVenda.addActionListener( this );
		btPesquisaReceber.addActionListener( this );
		btSelecionTodosReceber.addActionListener( this );
		btSelecionaNenhumReceber.addActionListener( this );
		btColocarEmCobrancaReceber.addActionListener( this );
		btNovoLancamento.addActionListener( this );
		btConsolidacao.addActionListener( this );

		txtSQLRemessa.setVlrString( "S" );
		txtSQLDevolucao.setVlrString( "N" );
		tab.getColumnModel().getColumn( 5 ).setMaxWidth( 0 );
		tab.getColumnModel().getColumn( 5 ).setMinWidth( 0 );
		tab.getTableHeader().getColumnModel().getColumn( 5 ).setMaxWidth( 0 );
		tab.getTableHeader().getColumnModel().getColumn( 5 ).setMinWidth( 0 );

		txtDataIniRedeber.setVlrDate( new Date() );
		txtDataFimReceber.setVlrDate( new Date() );

		cbAReceber.setVlrString( "S" );
		cbRecParcial.setVlrString( "S" );
	}

	private void montaListaCampos() {

		/************
		 * VENDEDOR *
		 ************/
		txtCodVend.setTabelaExterna( lcVendedor, FVendedor.class.getCanonicalName() );
		txtCodVend.setFK( true );
		txtCodVend.setNomeCampo( "CodVend" );
		lcVendedor.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.vendedor", ListaCampos.DB_PK, false ) );
		lcVendedor.add( new GuardaCampo( txtNomeVend, "NomeVend", "Nome do vendedor", ListaCampos.DB_SI, false ) );
		lcVendedor.montaSql( false, "VENDEDOR", "VD" );
		lcVendedor.setReadOnly( true );

		/***********
		 * PRODUTO *
		 ***********/
		txtCodProdRem.setTabelaExterna( lcProdutoRemessa, FProduto.class.getCanonicalName() );
		txtCodProdRem.setFK( true );
		txtCodProdRem.setNomeCampo( "CodProd" );
		lcProdutoRemessa.add( new GuardaCampo( txtCodProdRem, "CodProd", "Cód.prod", ListaCampos.DB_PK, false ) );
		lcProdutoRemessa.add( new GuardaCampo( txtDescProdRem, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProdutoRemessa.add( new GuardaCampo( txtPrecoRem, "PrecoBaseProd", "preco", ListaCampos.DB_SI, false ) );
		lcProdutoRemessa.montaSql( false, "PRODUTO", "EQ" );
		lcProdutoRemessa.setReadOnly( true );

		/****************************
		 * PRODUTO VENDA CONSIGNADA *
		 ****************************/
		txtCodProdVenda.setTabelaExterna( lcProdutoVenda, FProduto.class.getCanonicalName() );
		txtCodProdVenda.setFK( true );
		txtCodProdVenda.setNomeCampo( "CodProd" );
		lcProdutoVenda.add( new GuardaCampo( txtCodProdVenda, "CodProd", "Cód.prod", ListaCampos.DB_PK, false ) );
		lcProdutoVenda.add( new GuardaCampo( txtDescProdVenda, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProdutoVenda.add( new GuardaCampo( txtPreco, "PrecoBaseProd", "preco", ListaCampos.DB_SI, false ) );
		lcProdutoVenda.montaSql( false, "PRODUTO", "EQ" );
		lcProdutoVenda.setReadOnly( true );

		/***********
		 * CLIENTE *
		 ***********/
		txtCodCliVenda.setTabelaExterna( lcClienteVenda, FCliente.class.getCanonicalName() );
		txtCodCliVenda.setFK( true );
		txtCodCliVenda.setNomeCampo( "CodCli" );
		lcClienteVenda.add( new GuardaCampo( txtCodCliVenda, "CodCli", "Cód.cli", ListaCampos.DB_PK, false ) );
		lcClienteVenda.add( new GuardaCampo( txtRazCliVenda, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcClienteVenda.montaSql( false, "CLIENTE", "VD" );
		lcClienteVenda.setReadOnly( true );

		/**********************
		 * PLANO DE PAGEMENTO *
		 **********************/
		txtCodPlanoPag.setTabelaExterna( lcPlanoPagamento, FPlanoPag.class.getCanonicalName() );
		txtCodPlanoPag.setFK( true );
		txtCodPlanoPag.setNomeCampo( "CodCli" );
		lcPlanoPagamento.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.pl.pag.", ListaCampos.DB_PK, false ) );
		lcPlanoPagamento.add( new GuardaCampo( txtDescPlanoPag, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPagamento.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPagamento.setReadOnly( true );

		/***********
		 * CLIENTE *
		 ***********/
		txtCodCliRec.setTabelaExterna( lcClienteRec, FCliente.class.getCanonicalName() );
		txtCodCliRec.setFK( true );
		txtCodCliRec.setNomeCampo( "CodCli" );
		lcClienteRec.add( new GuardaCampo( txtCodCliRec, "CodCli", "Cód.cli", ListaCampos.DB_PK, false ) );
		lcClienteRec.add( new GuardaCampo( txtRazCliRec, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcClienteRec.montaSql( false, "CLIENTE", "VD" );
		lcClienteRec.setReadOnly( true );
	}

	private void montaTela() {

		pnPrincipal.add( tpnPrincipal );

		lcDet.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcDetVendas );
		lcDetVendas.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcDetVendas );

		// ********** Início aba Geral **********

		tpnPrincipal.addTab( "Geral", pnCliente );
		pnCab.add( panelGeral );

		setPainel( panelGeral );
		setAltCab( 100 );

		setListaCampos( lcCampos );
		adicCampo( txtCodConsig, 7, 30, 80, 20, "CodConsig", "Cód.Consig.", ListaCampos.DB_PK, true );
		adicCampo( txtCodVend, 90, 30, 80, 20, "CodVend", "Cód.Comiss", ListaCampos.DB_FK, txtNomeVend, true );
		adicDescFK( txtNomeVend, 173, 30, 300, 20, "NomeVend", "Nome do comissionado" );
		adicCampo( txtDocConsig, 476, 30, 100, 20, "DocConsig", "Documento", ListaCampos.DB_SI, true );
		adicCampo( txtDataConsig, 579, 30, 100, 20, "DtConsig", "Data consig.", ListaCampos.DB_SI, true );
		setListaCampos( true, "CONSIGNACAO", "VD" );
		lcCampos.setQueryInsert( false );

		pnNavCab.add( btImportarVendaRemessa, BorderLayout.EAST );

		// ********** Início aba Remessa **********

		pnDet.add( tpnGeral );
		tpnGeral.addTab( "Remessa", panelRemessaCampos );

		setPainel( panelGeralDetalhe );
		setAltDet( 100 );

		setListaCampos( lcDet );
		setNavegador( navRod );

		adicCampo( txtCodRemCo, 7, 30, 60, 20, "CodRemCo", "Item", ListaCampos.DB_PK, true );
		adicCampo( txtCodProdRem, 70, 30, 80, 20, "CodProd", "Cód.Prod", ListaCampos.DB_FK, txtDescProdRem, true );
		adicDescFK( txtDescProdRem, 153, 30, 260, 20, "DescProd", "Descrição do produto" );
		adicCampo( txtPrecoRem, 416, 30, 100, 20, "Preco", "Preço", ListaCampos.DB_SI, true );
		adicCampoInvisivel( txtQtdSaidaRem, "QtdSaida", "Qtd. Saída", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtQtdDevolucaoRem, "QtdDevol", "Qtd. Devolução", ListaCampos.DB_SI, false );
		adic( lbQtdSaidaRem, 519, 10, 100, 20 );
		adic( txtQtdSaidaRem, 519, 30, 100, 20 );
		adic( lbQtdDevolucaoRem, 519, 10, 100, 20 );
		adic( txtQtdDevolucaoRem, 519, 30, 100, 20 );
		lcDet.setDinWhereAdic( "((QtdSaida>0 AND 'S' = #S) OR (QtdDevol>0 AND 'S' = #S))", txtSQLRemessa );
		lcDet.setDinWhereAdic( "", txtSQLDevolucao );
		setListaCampos( true, "REMCONSIG", "VD" );
		lcDet.setQueryInsert( false );

		montaTab();
		tab.setTamColuna( 55, 0 );
		tab.setTamColuna( 80, 1 );
		tab.setTamColuna( 280, 2 );
		tab.setTamColuna( 100, 3 );
		tab.setTamColuna( 100, 4 );
		tab.setTamColuna( 100, 5 );

		panelRemessaCampos.add( panelGeralDetalhe );
		lbQtdDevolucaoRem.setVisible( false );
		txtQtdDevolucaoRem.setVisible( false );

		tpnGeral.addTab( "Devolução", panelDevolucaoCampos );

		// ********** Fim aba Remessa **********

		// ********** Início aba Venda **********

		tpnPrincipal.addTab( "Vendas", panelVendas );
		panelVendas.add( panelVendasCab, BorderLayout.NORTH );
		panelVendas.add( spVendas, BorderLayout.CENTER );
		panelVendas.add( panelVendasDetale, BorderLayout.SOUTH );

		panelVendasDetale.add( panelVendasCampos, BorderLayout.CENTER );
		panelVendasDetale.add( panelVendasNavegador, BorderLayout.SOUTH );
		panelVendasNavegador.setBorder( BorderFactory.createEtchedBorder() );

		panelVendasNavegador.add( navegadorVendas, BorderLayout.WEST );

		setPainel( panelVendasCampos );

		setListaCampos( lcDetVendas );
		setNavegador( navegadorVendas );

		adicCampo( txtCodVendaCo, 7, 30, 50, 20, "CodVendaCo", "Item", ListaCampos.DB_PK, true );
		adicCampo( txtCodProdVenda, 60, 30, 80, 20, "CodProd", "Cód.Prod", ListaCampos.DB_FK, txtDescProdVenda, true );
		adicDescFK( txtDescProdVenda, 143, 30, 270, 20, "DescProd", "Descrição do produto" );
		adicCampo( txtPreco, 416, 30, 120, 20, "Preco", "Preço Consiginado", ListaCampos.DB_SI, true );

		adicCampo( txtQtdVenda, 7, 70, 130, 20, "QtdVendaCo", "Qtd. Venda", ListaCampos.DB_SI, false );
		adicCampo( txtQtdTroca, 140, 70, 130, 20, "QtdTroca", "Qtd. Troca", ListaCampos.DB_SI, false );
		adicCampo( txtQtdBonif, 273, 70, 130, 20, "QtdBonif", "Qtd. Bonificação", ListaCampos.DB_SI, false );
		adicCampo( txtVlrDescVenda, 406, 70, 130, 20, "Desconto", "Desconto", ListaCampos.DB_SI, false );
		adicCampo( txtPrecoVenda, 539, 70, 120, 20, "PrecoVenda", "Preço Venda", ListaCampos.DB_SI, false );

		adicDB( cbRecebido, 550, 30, 150, 20, "Recebido", "", false );

		setPainel( panelVendasCab );
		adicCampo( txtCodCliVenda, 7, 30, 80, 20, "CodCli", "Cód.cli", ListaCampos.DB_FK, txtRazCliVenda, true );
		adicDescFK( txtRazCliVenda, 90, 30, 270, 20, "RazCli", "Razão social do cliente" );
		adicCampo( txtCodPlanoPag, 363, 30, 80, 20, "CodPlanoPag", "Cód.pl.pag.", ListaCampos.DB_FK, txtDescPlanoPag, true );
		adicDescFK( txtDescPlanoPag, 446, 30, 270, 20, "DescPlanoPag", "Descrição do plano de pagamento" );

		panelVendasNavegador.add( btImportarItensVenda, BorderLayout.EAST );

		setListaCampos( true, "VENDACONSIG", "VD" );
		lcDetVendas.setQueryInsert( true );
		lcDetVendas.setTabela( tabVendas );
		lcDetVendas.montaTab();

		tabVendas.setTamColuna( 250, 2 );
		tabVendas.setTamColuna( 250, 7 );
		tabVendas.setTamColuna( 250, 9 );

		// ********** Fim aba Vendas **********

		// ********** Início aba Contas a Receber **********

		// tpnPrincipal.addTab( "Contas a receber", panelReceber );
		//
		// panelReceber.add( panelReceberFiltros, BorderLayout.NORTH );
		// panelReceber.add( spReceber, BorderLayout.CENTER );
		// panelReceber.add( panelReceberFuncoes, BorderLayout.EAST );
		// panelReceber.add( panelReceberStatus, BorderLayout.SOUTH );
		//
		// JLabelPad lbLinha = new JLabelPad();
		// lbLinha.setBorder( BorderFactory.createEtchedBorder() );
		// JLabelPad lbPeriodo = new JLabelPad( "Período:", SwingConstants.CENTER );
		// lbPeriodo.setOpaque( true );
		//
		// Vector<String> vValsData = new Vector<String>();
		// Vector<String> vLabsData = new Vector<String>();
		// vValsData.addElement( "V" );
		// vValsData.addElement( "E" );
		// vLabsData.addElement( "Vencimento" );
		// vLabsData.addElement( "Emissão" );
		//
		// rgDataReceber = new JRadioGroup<String, String>( 1, 2, vLabsData, vValsData );
		// rgDataReceber.setVlrString( "V" );
		//		
		// JLabelPad lbLinha2 = new JLabelPad();
		// lbLinha2.setBorder( BorderFactory.createEtchedBorder() );
		//
		// panelReceberFiltros.setPreferredSize( new Dimension( 500, 130 ) );
		// panelReceberFiltros.adic( lbPeriodo, 17, 10, 80, 20 );
		// panelReceberFiltros.adic( lbLinha, 7, 20, 250, 40 );
		// panelReceberFiltros.adic( txtDataIniRedeber, 17, 30, 100, 20 );
		// panelReceberFiltros.adic( new JLabelPad( "até", SwingConstants.CENTER ), 117, 30, 30, 20 );
		// panelReceberFiltros.adic( txtDataFimReceber, 147, 30, 100, 20 );
		// panelReceberFiltros.adic( new JLabelPad( "Cód.cli." ), 270, 10, 80, 20 );
		// panelReceberFiltros.adic( txtCodCliRec, 270, 30, 90, 20 );
		// panelReceberFiltros.adic( new JLabelPad( "Razão social do cliente" ), 363, 10, 300, 20 );
		// panelReceberFiltros.adic( txtRazCliRec, 363, 30, 337, 20 );
		// panelReceberFiltros.adic( new JLabelPad( "Filtrar por:" ), 7, 60, 100, 20 );
		// panelReceberFiltros.adic( rgDataReceber, 7, 80, 250, 30 );
		// panelReceberFiltros.adic( new JLabelPad( "Filtrar por:" ), 270, 60, 100, 20 );
		// panelReceberFiltros.adic( lbLinha2, 270, 80, 295, 30 );
		// panelReceberFiltros.adic( cbAReceber, 285, 85, 90, 20 );
		// panelReceberFiltros.adic( cbRecParcial, 380, 85, 80, 20 );
		// panelReceberFiltros.adic( cbRecebidas, 460, 85, 85, 20 );
		// panelReceberFiltros.adic( btPesquisaReceber, 580, 80, 120, 30 );
		//
		// panelReceberFuncoes.adic( btSelecionTodosReceber, 5, 5, 30, 30 );
		// panelReceberFuncoes.adic( btSelecionaNenhumReceber, 5, 40, 30, 30 );
		// panelReceberFuncoes.adic( btColocarEmCobrancaReceber, 5, 75, 30, 30 );
		//		
		// btSelecionTodosReceber.setToolTipText( "Selecionar todos da lista" );
		// btSelecionaNenhumReceber.setToolTipText( "Desfaser seleção de todos da lista" );
		// btColocarEmCobrancaReceber.setToolTipText( "Marcar recebimento para cobrança" );
		//
		// tabReceber.adicColuna( "" );
		// tabReceber.adicColuna( "" );
		// tabReceber.adicColuna( "Cod.rec." );
		// tabReceber.adicColuna( "Parcela" );
		// tabReceber.adicColuna( "Cód.Cli" );
		// tabReceber.adicColuna( "Razão social do cliente" );
		// tabReceber.adicColuna( "Cód.p.pag." );
		// tabReceber.adicColuna( "Descrição do plano de pagamento" );
		// tabReceber.adicColuna( "Vencimento" );
		// tabReceber.adicColuna( "Pagamento" );
		// tabReceber.adicColuna( "Vlr. Parcela" );
		// tabReceber.adicColuna( "Vlr. Pago" );
		// tabReceber.adicColuna( "Vlr. Desconto" );
		// tabReceber.adicColuna( "Vlr. Juros" );
		// tabReceber.adicColuna( "Vlr. Multa" );
		// tabReceber.adicColuna( "Vlr. A Pagar" );
		//
		// tabReceber.setColunaEditavel( ETabReceber.SEL.ordinal(), true );
		// tabReceber.setTamColuna( 20, ETabReceber.SEL.ordinal() );
		// tabReceber.setTamColuna( 20, ETabReceber.STATUS.ordinal() );
		// tabReceber.setTamColuna( 60, ETabReceber.CODREC.ordinal() );
		// tabReceber.setTamColuna( 50, ETabReceber.NPARCITREC.ordinal() );
		// tabReceber.setTamColuna( 70, ETabReceber.CODCLI.ordinal() );
		// tabReceber.setTamColuna( 150, ETabReceber.RAZCLI.ordinal() );
		// tabReceber.setTamColuna( 70, ETabReceber.CODPLANOPAG.ordinal() );
		// tabReceber.setTamColuna( 150, ETabReceber.DESCPLANOPAG.ordinal() );
		// tabReceber.setTamColuna( 70, ETabReceber.VENCIMENTO.ordinal() );
		// tabReceber.setTamColuna( 70, ETabReceber.PAGAMENTO.ordinal() );
		// tabReceber.setTamColuna( 80, ETabReceber.VLRPARCELA.ordinal() );
		// tabReceber.setTamColuna( 80, ETabReceber.VLRPAGO.ordinal() );
		// tabReceber.setTamColuna( 80, ETabReceber.VLRDESCONTO.ordinal() );
		// tabReceber.setTamColuna( 80, ETabReceber.VLRJUROS.ordinal() );
		// tabReceber.setTamColuna( 80, ETabReceber.VLRMULTA.ordinal() );
		// tabReceber.setTamColuna( 80, ETabReceber.VLRAPAGAR.ordinal() );
		//
		// panelReceberStatus.adic( new JLabelPad( imgVencido ), 10, 0, 20, 20 );
		// panelReceberStatus.adic( new JLabelPad( "Vencido" ), 30, 0, 90, 20 );
		// panelReceberStatus.adic( txtVlrRecVencido, 10, 20, 120, 20 );
		// panelReceberStatus.adic( new JLabelPad( imgPagoParcial ), 135, 0, 20, 20 );
		// panelReceberStatus.adic( new JLabelPad( "Pago parcial" ), 155, 0, 90, 20 );
		// panelReceberStatus.adic( txtVlrRecPagoParcial, 135, 20, 120, 20 );
		// panelReceberStatus.adic( new JLabelPad( imgPago ), 260, 0, 20, 20 );
		// panelReceberStatus.adic( new JLabelPad( "Pago" ), 280, 0, 90, 20 );
		// panelReceberStatus.adic( txtVlrRecPago, 260, 20, 120, 20 );
		// panelReceberStatus.adic( new JLabelPad( imgNaoVencido ), 385, 0, 20, 20 );
		// panelReceberStatus.adic( new JLabelPad( "À vencer" ), 405, 0, 90, 20 );
		// panelReceberStatus.adic( txtVlrRecAVencer, 385, 20, 120, 20 );
		// panelReceberStatus.adic( new JLabelPad( imgEmCobranca ), 510, 0, 20, 20 );
		// panelReceberStatus.adic( new JLabelPad( "Em Cobrança" ), 535, 0, 90, 20 );
		// panelReceberStatus.adic( txtVlrEmCobranca, 510, 20, 120, 20 );
		//		
		// txtVlrRecVencido.setAtivo( false );
		// txtVlrRecPagoParcial.setAtivo( false );
		// txtVlrRecPago.setAtivo( false );
		// txtVlrRecAVencer.setAtivo( false );
		// txtVlrEmCobranca.setAtivo( false );

		// ********** Fim aba Contas a Receber **********

		// ********** Início aba Fechamento **********

		tpnPrincipal.addTab( "Fechamento", panelFechamento );
		setPainel( panelFechamento );
		panelFechamento.add( panelFechamentoCampos, BorderLayout.NORTH );
		panelFechamento.add( spFechamento, BorderLayout.CENTER );

		panelFechamentoCampos.adic( new JLabelPad( "Remessa" ), 7, 10, 120, 20 );
		panelFechamentoCampos.adic( txtRemessas, 7, 30, 120, 20 );

		panelFechamentoCampos.adic( new JLabelPad( "Devoluções" ), 130, 10, 120, 20 );
		panelFechamentoCampos.adic( txtDevolucoes, 130, 30, 120, 20 );

		panelFechamentoCampos.adic( new JLabelPad( "Consignado" ), 253, 10, 120, 20 );
		panelFechamentoCampos.adic( txtVendasConsig, 253, 30, 120, 20 );

		panelFechamentoCampos.adic( new JLabelPad( "Vendas" ), 376, 10, 120, 20 );
		panelFechamentoCampos.adic( txtVendas, 376, 30, 120, 20 );

		panelFechamentoCampos.adic( btConsolidacao, 580, 5, 140, 30 );
		panelFechamentoCampos.adic( btNovoLancamento, 580, 40, 140, 30 );

		tabFechamento.adicColuna( "Nº Lanç." );
		tabFechamento.adicColuna( "cod.sub.Lanç." );
		tabFechamento.adicColuna( "Data" );
		tabFechamento.adicColuna( "Nº Doc." );
		tabFechamento.adicColuna( "Valor" );
		tabFechamento.adicColuna( "Histórico" );

		tabFechamento.setColunaInvisivel( 0 );
		tabFechamento.setColunaInvisivel( 1 );
		tabFechamento.setTamColuna( 75, 2 );
		tabFechamento.setColunaInvisivel( 3 );
		tabFechamento.setTamColuna( 550, 5 );

		txtRemessas.setSoLeitura( true );
		txtDevolucoes.setSoLeitura( true );
		txtVendasConsig.setSoLeitura( true );
		txtVendas.setSoLeitura( true );
		txtDescontos.setSoLeitura( true );
		txtTrocas.setSoLeitura( true );
		txtBonificacoes.setSoLeitura( true );
		// txtAVista.setSoLeitura( true );
		// txtACredito.setSoLeitura( true );
		// txtAReceber.setSoLeitura( true );
		// txtNaoRecebido.setSoLeitura( true );
		// txtTotalFechamento.setSoLeitura( true );
		// txtSaldoFechamento.setSoLeitura( true );

		// ********** Fim aba Fechamento **********
	}

	// private void montaGridReceber() {
	//
	// try {
	//
	// tabReceber.limpa();
	// valorReceberVencido = new BigDecimal( "0.00" );
	// valorReceberPagoParcial = new BigDecimal( "0.00" );
	// valorReceberPago = new BigDecimal( "0.00" );
	// valorReceberAVencer = new BigDecimal( "0.00" );
	// valorEmCobranca = new BigDecimal( "0.00" );
	//			
	// if ( txtCodVend.getVlrInteger() <= 0 ) {
	// Funcoes.mensagemInforma( this, "Selecione o comissionado." );
	// tpnPrincipal.setSelectedIndex( 0 );
	// txtCodConsig.requestFocus();
	// return;
	// }
	// if ( txtDataIniRedeber.getVlrString() != null && txtDataIniRedeber.getVlrString().trim().length() != 10 ) {
	// Funcoes.mensagemInforma( this, "Selecione a data de início." );
	// txtDataIniRedeber.requestFocus();
	// return;
	// }
	// if ( txtDataFimReceber.getVlrString() != null && txtDataFimReceber.getVlrString().trim().length() != 10 ) {
	// Funcoes.mensagemInforma( this, "Selecione a data de término." );
	// txtDataFimReceber.requestFocus();
	// return;
	// }
	//
	// StringBuilder statusRecebimento = new StringBuilder();
	// if ( "S".equals(cbAReceber.getVlrString()) ) {
	// statusRecebimento.append( "'R1'" );
	// }
	// if ( "S".equals(cbRecParcial.getVlrString()) ) {
	// if ( statusRecebimento.length() > 0 ) {
	// statusRecebimento.append( "," );
	// }
	// statusRecebimento.append( "'RL'" );
	// }
	// if ( "S".equals(cbRecebidas.getVlrString()) ) {
	// if ( statusRecebimento.length() > 0 ) {
	// statusRecebimento.append( "," );
	// }
	// statusRecebimento.append( "'RP'" );
	// }
	//
	// StringBuilder sql = new StringBuilder();
	// sql.append( "select i.codrec, i.nparcitrec, i.statusitrec, i.recemcob," );
	// sql.append( "c.codcli, c.razcli, p.codplanopag, p.descplanopag," );
	// sql.append( "i.dtvencitrec, i.dtpagoitrec, i.vlrparcitrec, i.vlrpagoitrec, i.vlrdescitrec, i.vlrjurositrec, i.vlrmultaitrec, i.vlrapagitrec " );
	// sql.append( "from fnitreceber i, fnreceber r, vdcliente c, vdvendedor v, fnplanopag p " );
	// sql.append( "where i.codemp=r.codemp and i.codfilial=r.codfilial and i.codrec=r.codrec and " );
	// sql.append( "r.codempcl=c.codemp and r.codfilialcl=c.codfilial and r.codcli=c.codcli and " );
	// sql.append( "r.codempvd=v.codemp and r.codfilialvd=v.codfilial and r.codvend=v.codvend and " );
	// sql.append( "r.codemppg=p.codemp and r.codfilialpg=r.codfilial and r.codplanopag=p.codplanopag and " );
	// sql.append( "c.codempvd=v.codemp and c.codfilialvd=v.codfilial and c.codvend=v.codvend and " );
	// if ( txtCodCliRec.getVlrInteger() > 0 ) {
	// sql.append( "c.codemp=" + Aplicativo.iCodEmp + " and " );
	// sql.append( "c.codfilial=" + ListaCampos.getMasterFilial( "VDCLIENTE" ) + " and " );
	// sql.append( "c.codcli=" + txtCodCliRec.getVlrInteger() + " and " );
	// }
	// if ( statusRecebimento.length() > 0 ) {
	// sql.append( "i.statusitrec in (" + statusRecebimento.toString() + ") and " );
	// }
	// sql.append( "v.codemp=? and v.codfilial=? and v.codvend=? and " );
	// sql.append( "V".equals( rgDataReceber.getVlrString() ) ? "i.dtvencitrec " : "i.dtitrec " );
	// sql.append( "between ? and ? " );
	// sql.append( "order by c.codcli, i.dtvencitrec, i.codrec, i.nparcitrec" );
	//
	// PreparedStatement ps = con.prepareStatement( sql.toString() );
	// ps.setInt( 1, Aplicativo.iCodEmp );
	// ps.setInt( 2, ListaCampos.getMasterFilial( "VDVENDEDOR" ) );
	// ps.setInt( 3, txtCodVend.getVlrInteger() );
	// ps.setDate( 4, Funcoes.dateToSQLDate( txtDataIniRedeber.getVlrDate() ) );
	// ps.setDate( 5, Funcoes.dateToSQLDate( txtDataFimReceber.getVlrDate() ) );
	//
	// ResultSet rs = ps.executeQuery();
	// int row = 0;
	// float aPagar = 0f;
	// float pago = 0f;
	//
	// for ( ; rs.next(); row++ ) {
	//
	// tabReceber.adicLinha();
	//				
	// aPagar = Funcoes.strDecimalToBigDecimal( 2, rs.getString( "vlrapagitrec" ) ).floatValue();
	// pago = Funcoes.strDecimalToBigDecimal( 2, rs.getString( "vlrpagoitrec" ) ).floatValue();
	//				
	// if ( "S".equals( rs.getString( "recemcob" ) ) ) {
	// imgColuna = imgEmCobranca;
	// valorEmCobranca = valorEmCobranca.add( rs.getBigDecimal( "vlrapagitrec" ) );
	// }
	// else if ( "RP".equals( rs.getString( "statusitrec" ) ) && aPagar == 0.0f ) {
	// imgColuna = imgPago;
	// valorReceberPago = valorReceberPago.add( rs.getBigDecimal( "vlrpagoitrec" ) );
	// }
	// else if ( pago > 0 ) {
	// imgColuna = imgPagoParcial;
	// valorReceberPagoParcial = valorReceberPagoParcial.add( rs.getBigDecimal( "vlrpagoitrec" ) );
	// }
	// else if ( rs.getDate( "DtVencItRec" ).before( Calendar.getInstance().getTime() ) ) {
	// imgColuna = imgVencido;
	// valorReceberVencido = valorReceberVencido.add( rs.getBigDecimal( "vlrparcitrec" ) );
	// }
	// else if ( rs.getDate( "DtVencItRec" ).after( Calendar.getInstance().getTime() ) ) {
	// imgColuna = imgNaoVencido;
	// valorReceberAVencer = valorReceberAVencer.add( rs.getBigDecimal( "vlrapagitrec" ) );
	// }
	//
	// tabReceber.setValor( new Boolean( false ), row, ETabReceber.SEL.ordinal() );
	// tabReceber.setValor( imgColuna, row, ETabReceber.STATUS.ordinal() );
	// tabReceber.setValor( rs.getInt( "codrec" ), row, ETabReceber.CODREC.ordinal() );
	// tabReceber.setValor( rs.getInt( "nparcitrec" ), row, ETabReceber.NPARCITREC.ordinal() );
	// tabReceber.setValor( rs.getInt( "codcli" ), row, ETabReceber.CODCLI.ordinal() );
	// tabReceber.setValor( rs.getString( "razcli" ), row, ETabReceber.RAZCLI.ordinal() );
	// tabReceber.setValor( rs.getInt( "codplanopag" ), row, ETabReceber.CODPLANOPAG.ordinal() );
	// tabReceber.setValor( rs.getString( "descplanopag" ), row, ETabReceber.DESCPLANOPAG.ordinal() );
	// tabReceber.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "dtvencitrec" ) ), row, ETabReceber.VENCIMENTO.ordinal() );
	// tabReceber.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "dtpagoitrec" ) ), row, ETabReceber.PAGAMENTO.ordinal() );
	// tabReceber.setValor( rs.getBigDecimal( "vlrparcitrec" ), row, ETabReceber.VLRPARCELA.ordinal() );
	// tabReceber.setValor( rs.getBigDecimal( "vlrpagoitrec" ), row, ETabReceber.VLRPAGO.ordinal() );
	// tabReceber.setValor( rs.getBigDecimal( "vlrdescitrec" ), row, ETabReceber.VLRDESCONTO.ordinal() );
	// tabReceber.setValor( rs.getBigDecimal( "vlrjurositrec" ), row, ETabReceber.VLRJUROS.ordinal() );
	// tabReceber.setValor( rs.getBigDecimal( "vlrmultaitrec" ), row, ETabReceber.VLRMULTA.ordinal() );
	// tabReceber.setValor( rs.getBigDecimal( "vlrapagitrec" ), row, ETabReceber.VLRAPAGAR.ordinal() );
	// }
	//			
	// txtVlrRecVencido.setVlrBigDecimal( valorReceberVencido );
	// txtVlrRecPagoParcial.setVlrBigDecimal( valorReceberPagoParcial );
	// txtVlrRecPago.setVlrBigDecimal( valorReceberPago );
	// txtVlrRecAVencer.setVlrBigDecimal( valorReceberAVencer );
	// txtVlrEmCobranca.setVlrBigDecimal( valorEmCobranca );
	//
	// rs.close();
	// ps.close();
	//
	// con.commit();
	//
	// } catch ( Exception e ) {
	// e.printStackTrace();
	// Funcoes.mensagemErro( this, "Erro ao listar recebimentos!\n" + e.getMessage(), true, con, e );
	// }
	// }
	//
	// private void abreTelaRec() {
	//
	// Integer codrec = (Integer) tabReceber.getValor( tabReceber.getLinhaSel(), ETabReceber.CODREC.ordinal() );
	// FManutRec tela = null;
	//
	// if ( Aplicativo.telaPrincipal.temTela( FManutRec.class.getName() ) ) {
	// tela = (FManutRec)Aplicativo.telaPrincipal.getTela( FManutRec.class.getName() );
	// }
	// else {
	// tela = new FManutRec();
	// Aplicativo.telaPrincipal.criatela( "Manutenção de contas a receber", tela, con );
	// }
	//		
	// tela.setRec( codrec );
	// }
	//
	// private void selecionaTodosReceber() {
	// for ( int row = 0; row < tabReceber.getNumLinhas(); row++ ) {
	// tabReceber.setValor( new Boolean( true ), row, ETabReceber.SEL.ordinal() );
	// }
	// }
	//
	// private void selecionaNenhumReceber() {
	// for ( int row = 0; row < tabReceber.getNumLinhas(); row++ ) {
	// tabReceber.setValor( new Boolean( false ), row, ETabReceber.SEL.ordinal() );
	// }
	// }
	//	
	// private void colocarEmCobranca() {
	//		
	// try {
	//			
	// StringBuilder sql = new StringBuilder();
	// sql.append( "update FNITRECEBER set recemcob='S' where codemp=? and codfilial=? and codrec=? and nparcitrec=?" );
	//			
	// PreparedStatement ps = null;
	//			
	// for ( int row = 0; row < tabReceber.getNumLinhas(); row++ ) {
	//				
	// if ( (Boolean)tabReceber.getValor( row, ETabReceber.SEL.ordinal() ) ) {
	//
	// ps = con.prepareStatement( sql.toString() );
	// ps.setInt( 1, Aplicativo.iCodEmp );
	// ps.setInt( 2, ListaCampos.getMasterFilial( "FNITRECEBER" ) );
	// ps.setInt( 3, (Integer)tabReceber.getValor( row, ETabReceber.CODREC.ordinal() ) );
	// ps.setInt( 4, (Integer)tabReceber.getValor( row, ETabReceber.NPARCITREC.ordinal() ) );
	// ps.executeUpdate();
	// ps.close();
	// }
	// }
	//
	// con.commit();
	//			
	// montaGridReceber();
	//			
	// } catch ( SQLException e ) {
	// e.printStackTrace();
	// }
	// }

	private String getPlanejamentos() {

		String planejamento = null;

		try {

			StringBuilder sql = new StringBuilder();
			sql.append( "select codemppc, codfilialpc, codplanconsig," );
			sql.append( "codemppv, codfilialpv, codplanvdconsig " );
			sql.append( "from sgprefere7 " );
			sql.append( "where codemp=? and codfilial=?" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "sgprefere7" ) );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				codempco = rs.getInt( "codemppc" );
				codfilialco = rs.getInt( "codfilialpc" );
				codplanconsig = rs.getString( "codplanconsig" );
				codempvd = rs.getInt( "codemppv" );
				codfilialvd = rs.getInt( "codfilialpv" );
				codplanvdconsig = rs.getString( "codplanvdconsig" );
			}
			if ( codplanconsig == null || codplanvdconsig == null ) {
				btConsolidacao.setEnabled( false );
				Funcoes.mensagemInforma( this, "Preferências de vendas consignadas não encontradas." );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao verificar preferencias!" );
		}

		return planejamento;
	}

	private void carregaFechamento() {

		montaGridFechamento();
		carregaInfoVendas();
		carregaInfoRemessa();
	}

	private void montaGridFechamento() {

		tabFechamento.limpa();

		try {

			StringBuilder sql = new StringBuilder();
			sql.append( "select fs.codsublanca, fs.codlanca, fs.datasublanca, fl.doclanca, fs.vlrsublanca, fs.histsublanca " );
			sql.append( "from fnsublanca fs, fnlanca fl, vdvendedor vd, fnconta ca " );
			// sql.append( "where fs.codemp=? and fs.codfilial=? and fs.datasublanca between ? and ? and " );
			sql.append( "where fs.codemp=? and fs.codfilial=? and fs.datasublanca = ? and " );
			sql.append( "fl.codemp=fs.codemp and fl.codfilial=fs.codfilial and fl.codlanca=fs.codlanca and " );
			sql.append( "vd.codemp=? and vd.codfilial=? and vd.codvend=? and " );
			sql.append( "fs.codemppn=ca.codemppn and fs.codfilialpn=ca.codfilialpn and fs.codplan=ca.codplan and " );
			sql.append( "ca.codemp=vd.codempca and ca.codfilial=vd.codfilialca and ca.numconta=vd.numconta " );
			sql.append( "order by fs.datasublanca, fs.codlanca" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "fnlanca" ) );
			ps.setDate( 3, Funcoes.dateToSQLDate( txtDataConsig.getVlrDate() ) );
			// ps.setDate( 4, Funcoes.dateToSQLDate( txtDataFimReceber.getVlrDate() ) );
			ps.setInt( 4, Aplicativo.iCodEmp );
			ps.setInt( 5, ListaCampos.getMasterFilial( "fnlanca" ) );
			ps.setInt( 6, txtCodVend.getVlrInteger() );

			ResultSet rs = ps.executeQuery();

			for ( int row = 0; rs.next(); row++ ) {

				tabFechamento.adicLinha();
				tabFechamento.setValor( rs.getString( "codlanca" ), row, ETabFechamento.NLANCA.ordinal() );
				tabFechamento.setValor( rs.getInt( "codsublanca" ), row, ETabFechamento.CODSUBLANCA.ordinal() );
				tabFechamento.setValor( rs.getDate( "datasublanca" ), row, ETabFechamento.DATA.ordinal() );
				tabFechamento.setValor( rs.getString( "doclanca" ), row, ETabFechamento.DOC.ordinal() );
				tabFechamento.setValor( rs.getBigDecimal( "vlrsublanca" ), row, ETabFechamento.VALOR.ordinal() );
				tabFechamento.setValor( rs.getString( "histsublanca" ), row, ETabFechamento.HISTORICO.ordinal() );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar lançamentos do vendedor!\n" + e.getMessage(), true, con, e );
		}
	}

	private void carregaInfoVendas() {

		try {

			StringBuilder sql = new StringBuilder();
			sql.append( "select v.preco, v.precovenda, v.qtdvendaco, v.desconto, v.qtdtroca, v.qtdbonif " );
			sql.append( "from VDVENDACONSIG v, VDCONSIGNACAO c " );
			sql.append( "where c.codemp=? and c.codfilial=? and c.codconsig=? and " );
			sql.append( "v.codemp=c.codemp and v.codfilial=c.codfilial and v.codconsig=c.codconsig" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDVENDACONSIG" ) );
			ps.setInt( 3, txtCodConsig.getVlrInteger() );

			ResultSet rs = ps.executeQuery();

			BigDecimal precoConsig = null;
			BigDecimal precoVenda = null;
			BigDecimal vendasConsig = new BigDecimal( "0.00" );
			BigDecimal descontos = new BigDecimal( "0.00" );
			BigDecimal trocas = new BigDecimal( "0.00" );
			BigDecimal bonificacoes = new BigDecimal( "0.00" );
			BigDecimal vendas = new BigDecimal( "0.00" );
			BigDecimal totalVendasConsig = new BigDecimal( "0.00" );
			BigDecimal totalDescontos = new BigDecimal( "0.00" );
			BigDecimal totalTrocas = new BigDecimal( "0.00" );
			BigDecimal totalBonificacoes = new BigDecimal( "0.00" );
			BigDecimal totalVendas = new BigDecimal( "0.00" );

			txtVendasConsig.setVlrBigDecimal( totalVendasConsig );
			txtVendas.setVlrBigDecimal( totalVendas );
			txtDescontos.setVlrBigDecimal( totalDescontos );
			txtTrocas.setVlrBigDecimal( totalTrocas );
			txtBonificacoes.setVlrBigDecimal( totalBonificacoes );

			while ( rs.next() ) {

				precoConsig = rs.getBigDecimal( "preco" );
				precoVenda = rs.getBigDecimal( "precoVenda" );

				vendasConsig = precoConsig.multiply( rs.getBigDecimal( "qtdvendaco" ) );
				trocas = precoConsig.multiply( rs.getBigDecimal( "qtdtroca" ) );
				bonificacoes = precoConsig.multiply( rs.getBigDecimal( "qtdbonif" ) );

				if ( vendasConsig.floatValue() > 0f ) {
					descontos = vendasConsig.multiply( rs.getBigDecimal( "desconto" ) );
				}
				else if ( trocas.floatValue() > 0f ) {
					descontos = trocas.multiply( rs.getBigDecimal( "desconto" ) );
				}
				else if ( bonificacoes.floatValue() > 0f ) {
					descontos = bonificacoes.multiply( rs.getBigDecimal( "desconto" ) );
				}

				totalVendasConsig = totalVendasConsig.add( vendasConsig );
				totalDescontos = totalDescontos.add( descontos );
				totalTrocas = totalTrocas.add( trocas );
				totalBonificacoes = totalBonificacoes.add( bonificacoes );

				if ( precoVenda != null ) {
					vendas = precoVenda.multiply( rs.getBigDecimal( "qtdvendaco" ) );
					totalVendas = totalVendas.add( vendas );
				}
			}

			rs.close();
			ps.close();

			con.commit();

			txtVendasConsig.setVlrBigDecimal( totalVendasConsig );
			txtDescontos.setVlrBigDecimal( totalDescontos );
			txtTrocas.setVlrBigDecimal( totalTrocas );
			txtBonificacoes.setVlrBigDecimal( totalBonificacoes );
			txtVendas.setVlrBigDecimal( totalVendas );

		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar informações de vendas!\n" + e.getMessage(), true, con, e );
		}
	}

	private void carregaInfoRemessa() {

		try {

			StringBuilder sql = new StringBuilder();
			sql.append( "select r.preco, r.qtdsaida, r.qtddevol " );
			sql.append( "from VDREMCONSIG r, VDCONSIGNACAO c " );
			sql.append( "where c.codemp=? and c.codfilial=? and c.codconsig=? and " );
			sql.append( "r.codemp=c.codemp and r.codfilial=c.codfilial and r.codconsig=c.codconsig" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDREMCONSIG" ) );
			ps.setInt( 3, txtCodConsig.getVlrInteger() );

			ResultSet rs = ps.executeQuery();

			BigDecimal preco = null;
			BigDecimal remessas = new BigDecimal( "0.00" );
			BigDecimal devolucoes = new BigDecimal( "0.00" );

			txtRemessas.setVlrBigDecimal( remessas );
			txtDevolucoes.setVlrBigDecimal( devolucoes );

			while ( rs.next() ) {

				preco = rs.getBigDecimal( "preco" );
				remessas = remessas.add( preco.multiply( rs.getBigDecimal( "qtdsaida" ) ) );
				devolucoes = devolucoes.add( preco.multiply( rs.getBigDecimal( "qtddevol" ) ) );
			}

			rs.close();
			ps.close();

			con.commit();

			txtRemessas.setVlrBigDecimal( remessas );
			txtDevolucoes.setVlrBigDecimal( devolucoes );

		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar informações de vendas!\n" + e.getMessage(), true, con, e );
		}
	}

	// private void carregaInfoRecebimento() {
	//
	// try {
	//
	// StringBuilder sql = new StringBuilder();
	// sql.append( "select r.preco, r.qtdsaida, r.qtddevol " );
	// sql.append( "from VDREMCONSIG r, VDCONSIGNACAO c " );
	// sql.append( "where c.codemp=? and c.codfilial=? and c.codconsig=? and " );
	// sql.append( "r.codemp=c.codemp and r.codfilial=c.codfilial and r.codconsig=c.codconsig" );
	//
	// PreparedStatement ps = con.prepareStatement( sql.toString() );
	// ps.setInt( 1, Aplicativo.iCodEmp );
	// ps.setInt( 2, ListaCampos.getMasterFilial( "VDREMCONSIG" ) );
	// ps.setInt( 3, txtCodConsig.getVlrInteger() );
	//
	// ResultSet rs = ps.executeQuery();
	//			
	// BigDecimal preco = null;
	// BigDecimal remessas = new BigDecimal( "0.00" );
	// BigDecimal devolucoes = new BigDecimal( "0.00" );
	//			
	// txtRemessas.setVlrBigDecimal( remessas );
	// txtDevolucoes.setVlrBigDecimal( devolucoes );
	//
	// while( rs.next() ) {
	//
	// preco = rs.getBigDecimal( "preco" );
	// remessas = remessas.add( preco.multiply( rs.getBigDecimal( "qtdsaida" ) ) );
	// devolucoes = devolucoes.add( preco.multiply( rs.getBigDecimal( "qtddevol" ) ) );
	// }
	//
	// rs.close();
	// ps.close();
	//
	// con.commit();
	//			
	// txtRemessas.setVlrBigDecimal( remessas );
	// txtDevolucoes.setVlrBigDecimal( devolucoes );
	//
	// } catch ( SQLException e ) {
	// e.printStackTrace();
	// Funcoes.mensagemErro( this, "Erro ao buscar informações de vendas!\n" + e.getMessage(), true, con, e );
	// }
	// }

	private void consolidacao() {

		try {

			lancaRemessa();
			lancaDevolucao();
			lancaVendas();

			con.commit();

			Funcoes.mensagemInforma( this, "Consolidação efetuada." );

			carregaFechamento();

		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao consiliar consignação!\n" + e.getMessage(), true, con, e );
		}

	}

	private void lancaRemessa() throws SQLException {

		PreparedStatement psR = null;

		String codplanvend = getPlanejamentoVendedor();

		int codlanca = getLancamentoConsignacao();

		BigDecimal remessas = txtRemessas.getVlrBigDecimal();

		if ( codlanca <= 0 ) {

			codlanca = getSequenciaLancamento();

			StringBuilder insertLanca = new StringBuilder();
			insertLanca.append( "INSERT INTO FNLANCA " );
			insertLanca.append( "(TIPOLANCA,CODEMP,CODFILIAL,CODLANCA," );
			insertLanca.append( "CODEMPPN,CODFILIALPN,CODPLAN," );
			insertLanca.append( "DATALANCA,HISTBLANCA,VLRLANCA,FLAG,DOCLANCA) " );
			insertLanca.append( "VALUES (" );
			insertLanca.append( "?,?,?,?," );
			insertLanca.append( "?,?,?," );
			insertLanca.append( "?,?,?,'S',?)" );

			psR = con.prepareStatement( insertLanca.toString() );
			psR.setString( 1, "A" );
			psR.setInt( 2, Aplicativo.iCodEmp );
			psR.setInt( 3, ListaCampos.getMasterFilial( "FNLANCA" ) );
			psR.setInt( 4, codlanca );
			psR.setInt( 5, Aplicativo.iCodEmp );
			psR.setInt( 6, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
			psR.setString( 7, codplanvend );
			psR.setDate( 8, Funcoes.dateToSQLDate( txtDataConsig.getVlrDate() ) );
			psR.setString( 9, "REMESSA CONSIGNADA" );
			psR.setBigDecimal( 10, remessas );
			psR.setString( 11, txtCodConsig.getVlrString() );
			psR.executeUpdate();

			StringBuilder insertSubLanca = new StringBuilder();
			insertSubLanca.append( "INSERT INTO FNSUBLANCA " );
			insertSubLanca.append( "(CODEMP,CODFILIAL,CODLANCA,CODSUBLANCA," );
			insertSubLanca.append( "CODEMPPN,CODFILIALPN,CODPLAN," );
			insertSubLanca.append( "ORIGSUBLANCA,DATASUBLANCA,VLRSUBLANCA,HISTSUBLANCA,FLAG) " );
			insertSubLanca.append( "VALUES (" );
			insertSubLanca.append( "?,?,?,?," );
			insertSubLanca.append( "?,?,?," );
			insertSubLanca.append( "?,?,?,?,'S');" );

			psR = con.prepareStatement( insertSubLanca.toString() );
			psR.setInt( 1, Aplicativo.iCodEmp );
			psR.setInt( 2, ListaCampos.getMasterFilial( "FNSUBLANCA" ) );
			psR.setInt( 3, codlanca );
			psR.setInt( 4, 1 );
			psR.setInt( 5, codempco );
			psR.setInt( 6, codfilialco );
			psR.setString( 7, codplanconsig );
			psR.setString( 8, "S" );
			psR.setDate( 9, Funcoes.dateToSQLDate( txtDataConsig.getVlrDate() ) );
			psR.setBigDecimal( 10, remessas.multiply( new BigDecimal( "-1" ) ) );
			psR.setString( 11, "REMESSA CONSIGNADA" );
			psR.executeUpdate();

			StringBuilder updateConsignacao = new StringBuilder();
			updateConsignacao.append( "UPDATE VDCONSIGNACAO SET " );
			updateConsignacao.append( "CODEMPSL=?,CODFILIALSL=?,CODLANCA=?,CODSUBLANCA=? " );
			updateConsignacao.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODCONSIG=?" );

			psR = con.prepareStatement( updateConsignacao.toString() );
			psR.setInt( 1, Aplicativo.iCodEmp );
			psR.setInt( 2, ListaCampos.getMasterFilial( "FNLANCA" ) );
			psR.setInt( 3, codlanca );
			psR.setInt( 4, 1 );
			psR.setInt( 5, Aplicativo.iCodEmp );
			psR.setInt( 6, ListaCampos.getMasterFilial( "VDCONSIGNACAO" ) );
			psR.setInt( 7, txtCodConsig.getVlrInteger() );
			psR.executeUpdate();
		}
		else {

			StringBuilder updateSubLanca = new StringBuilder();
			updateSubLanca.append( "UPDATE FNSUBLANCA SET VLRSUBLANCA=? " );
			updateSubLanca.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODLANCA=? AND CODSUBLANCA=?" );

			psR = con.prepareStatement( updateSubLanca.toString() );
			psR.setBigDecimal( 1, remessas );
			psR.setInt( 2, Aplicativo.iCodEmp );
			psR.setInt( 3, ListaCampos.getMasterFilial( "FNSUBLANCA" ) );
			psR.setInt( 4, codlanca );
			psR.setInt( 5, 1 );
			psR.executeUpdate();

			StringBuilder updateLanca = new StringBuilder();
			updateLanca.append( "UPDATE FNLANCA SET VLRLANCA=? " );
			updateLanca.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODLANCA=?" );

			psR = con.prepareStatement( updateLanca.toString() );
			psR.setBigDecimal( 1, remessas );
			psR.setInt( 2, Aplicativo.iCodEmp );
			psR.setInt( 3, ListaCampos.getMasterFilial( "FNLANCA" ) );
			psR.setInt( 4, codlanca );
			psR.executeUpdate();
		}
	}

	private void lancaDevolucao() throws SQLException {

		PreparedStatement psR = null;

		String codplanvend = getPlanejamentoVendedor();

		int codlanca = getLancamentoConsignacaoDevolucao();

		BigDecimal devolucoes = txtDevolucoes.getVlrBigDecimal();

		if ( codlanca <= 0 && devolucoes.doubleValue() > 0 ) {

			codlanca = getSequenciaLancamento();

			StringBuilder insertLanca = new StringBuilder();
			insertLanca.append( "INSERT INTO FNLANCA " );
			insertLanca.append( "(TIPOLANCA,CODEMP,CODFILIAL,CODLANCA," );
			insertLanca.append( "CODEMPPN,CODFILIALPN,CODPLAN," );
			insertLanca.append( "DATALANCA,HISTBLANCA,VLRLANCA,FLAG,DOCLANCA) " );
			insertLanca.append( "VALUES (" );
			insertLanca.append( "?,?,?,?," );
			insertLanca.append( "?,?,?," );
			insertLanca.append( "?,?,?,'S',?)" );

			psR = con.prepareStatement( insertLanca.toString() );
			psR.setString( 1, "A" );
			psR.setInt( 2, Aplicativo.iCodEmp );
			psR.setInt( 3, ListaCampos.getMasterFilial( "FNLANCA" ) );
			psR.setInt( 4, codlanca );
			psR.setInt( 5, Aplicativo.iCodEmp );
			psR.setInt( 6, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
			psR.setString( 7, codplanvend );
			psR.setDate( 8, Funcoes.dateToSQLDate( txtDataConsig.getVlrDate() ) );
			psR.setString( 9, "DEVOLUÇÃO CONSIGNADA" );
			psR.setBigDecimal( 10, devolucoes.multiply( new BigDecimal( "-1" ) ) );
			psR.setString( 11, txtCodConsig.getVlrString() );
			psR.executeUpdate();

			StringBuilder insertSubLanca = new StringBuilder();
			insertSubLanca.append( "INSERT INTO FNSUBLANCA " );
			insertSubLanca.append( "(CODEMP,CODFILIAL,CODLANCA,CODSUBLANCA," );
			insertSubLanca.append( "CODEMPPN,CODFILIALPN,CODPLAN," );
			insertSubLanca.append( "ORIGSUBLANCA,DATASUBLANCA,VLRSUBLANCA,HISTSUBLANCA,FLAG) " );
			insertSubLanca.append( "VALUES (" );
			insertSubLanca.append( "?,?,?,?," );
			insertSubLanca.append( "?,?,?," );
			insertSubLanca.append( "?,?,?,?,'S')" );

			psR = con.prepareStatement( insertSubLanca.toString() );
			psR.setInt( 1, Aplicativo.iCodEmp );
			psR.setInt( 2, ListaCampos.getMasterFilial( "FNSUBLANCA" ) );
			psR.setInt( 3, codlanca );
			psR.setInt( 4, 1 );
			psR.setInt( 5, codempvd );
			psR.setInt( 6, codfilialvd );
			psR.setString( 7, codplanvdconsig );
			psR.setString( 8, "S" );
			psR.setDate( 9, Funcoes.dateToSQLDate( txtDataConsig.getVlrDate() ) );
			psR.setBigDecimal( 10, devolucoes );
			psR.setString( 11, "DEVOLUÇÃO CONSIGNADA" );
			psR.executeUpdate();

			StringBuilder updateConsignacao = new StringBuilder();
			updateConsignacao.append( "UPDATE VDCONSIGNACAO SET " );
			updateConsignacao.append( "CODEMPSD=?,CODFILIALSD=?,CODLANCASD=?,CODSUBLANCASD=? " );
			updateConsignacao.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODCONSIG=?" );

			psR = con.prepareStatement( updateConsignacao.toString() );
			psR.setInt( 1, Aplicativo.iCodEmp );
			psR.setInt( 2, ListaCampos.getMasterFilial( "FNLANCA" ) );
			psR.setInt( 3, codlanca );
			psR.setInt( 4, 1 );
			psR.setInt( 5, Aplicativo.iCodEmp );
			psR.setInt( 6, ListaCampos.getMasterFilial( "VDCONSIGNACAO" ) );
			psR.setInt( 7, txtCodConsig.getVlrInteger() );
			psR.executeUpdate();
		}
		else {

			StringBuilder updateSubLanca = new StringBuilder();
			updateSubLanca.append( "UPDATE FNSUBLANCA SET VLRSUBLANCA=? " );
			updateSubLanca.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODLANCA=? AND CODSUBLANCA=?" );

			psR = con.prepareStatement( updateSubLanca.toString() );
			psR.setBigDecimal( 1, devolucoes );
			psR.setInt( 2, Aplicativo.iCodEmp );
			psR.setInt( 3, ListaCampos.getMasterFilial( "FNSUBLANCA" ) );
			psR.setInt( 4, codlanca );
			psR.setInt( 5, 1 );
			psR.executeUpdate();

			StringBuilder updateLanca = new StringBuilder();
			updateLanca.append( "UPDATE FNLANCA SET VLRLANCA=? " );
			updateLanca.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODLANCA=?" );

			psR = con.prepareStatement( updateLanca.toString() );
			psR.setBigDecimal( 1, devolucoes.multiply( new BigDecimal( "-1" ) ) );
			psR.setInt( 2, Aplicativo.iCodEmp );
			psR.setInt( 3, ListaCampos.getMasterFilial( "FNLANCA" ) );
			psR.setInt( 4, codlanca );
			psR.executeUpdate();
		}
	}

	private void lancaVendas() throws SQLException {

		String venda = "VENDA CONSIGNADA - (Cliente: %d, Pagamento: %d, Item: %d)";
		String recebimento = "RECEBIMENTO EM CARTEIRA - (Cliente: %d, Pagamento: %d, Item: %d)";
		String naoRecebimento = "RECEBIMENTO PENDENTE - (Cliente: %d, Pagamento: %d, Item: %d)";

		String codplanvend = getPlanejamentoVendedor();

		StringBuilder selectVendas = new StringBuilder();
		selectVendas.append( "SELECT CODVENDACO, (QTDVENDACO*PRECO) VALOR, COALESCE((QTDVENDACO*PRECOVENDA), 0) VALOR_VENDA, " );
		selectVendas.append( "CODCLI, CODPLANOPAG, CODLANCA, CODSUBLANCA, CODLANCASC, CODSUBLANCASC, RECEBIDO " );
		selectVendas.append( "FROM VDVENDACONSIG " );
		selectVendas.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODCONSIG=? " );
		selectVendas.append( "ORDER BY CODVENDACO" );

		PreparedStatement ps = con.prepareStatement( selectVendas.toString() );
		ps.setInt( 1, Aplicativo.iCodEmp );
		ps.setInt( 2, ListaCampos.getMasterFilial( "VDVENDACONSIG" ) );
		ps.setInt( 3, txtCodConsig.getVlrInteger() );
		ResultSet rs = ps.executeQuery();

		PreparedStatement psV = null;

		int codlanca = 0;
		BigDecimal valor = null;
		BigDecimal valorVenda = null;
		boolean bRec = false;

		while ( rs.next() ) {

			bRec = "S".equals( rs.getString( "RECEBIDO" ) );
			valor = rs.getBigDecimal( "VALOR" );
			valorVenda = rs.getBigDecimal( "VALOR_VENDA" );

			if ( rs.getInt( "CODSUBLANCA" ) <= 0 ) {

				codlanca = getSequenciaLancamento();

				StringBuilder insertLanca = new StringBuilder();
				insertLanca.append( "INSERT INTO FNLANCA " );
				insertLanca.append( "(TIPOLANCA,CODEMP,CODFILIAL,CODLANCA," );
				insertLanca.append( "CODEMPPN,CODFILIALPN,CODPLAN," );
				insertLanca.append( "DATALANCA,HISTBLANCA,VLRLANCA,FLAG,DOCLANCA) " );
				insertLanca.append( "VALUES (" );
				insertLanca.append( "?,?,?,?," );
				insertLanca.append( "?,?,?," );
				insertLanca.append( "?,?,?,'S',?)" );

				psV = con.prepareStatement( insertLanca.toString() );
				psV.setString( 1, "A" );
				psV.setInt( 2, Aplicativo.iCodEmp );
				psV.setInt( 3, ListaCampos.getMasterFilial( "FNLANCA" ) );
				psV.setInt( 4, codlanca );
				psV.setInt( 5, Aplicativo.iCodEmp );
				psV.setInt( 6, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
				psV.setString( 7, codplanvend );
				psV.setDate( 8, Funcoes.dateToSQLDate( txtDataConsig.getVlrDate() ) );
				if ( bRec ) {
					psV.setString( 9, String.format( venda, rs.getInt( "CODCLI" ), rs.getInt( "CODPLANOPAG" ), rs.getInt( "CODVENDACO" ) ) );
					psV.setBigDecimal( 10, valor.multiply( new BigDecimal( "-1" ) ) );
				}
				else {
					psV.setString( 9, String.format( venda, rs.getInt( "CODCLI" ), rs.getInt( "CODPLANOPAG" ), rs.getInt( "CODVENDACO" ) ) );
					psV.setBigDecimal( 10, valorVenda.multiply( new BigDecimal( "-1" ) ) );
				}
				psV.setString( 11, txtCodConsig.getVlrString() );
				psV.executeUpdate();

				StringBuilder insertSubLanca = new StringBuilder();
				insertSubLanca.append( "INSERT INTO FNSUBLANCA " );
				insertSubLanca.append( "(CODEMP,CODFILIAL,CODLANCA,CODSUBLANCA," );
				insertSubLanca.append( "CODEMPPN,CODFILIALPN,CODPLAN," );
				insertSubLanca.append( "ORIGSUBLANCA,DATASUBLANCA,VLRSUBLANCA,HISTSUBLANCA,FLAG) " );
				insertSubLanca.append( "VALUES (" );
				insertSubLanca.append( "?,?,?,?," );
				insertSubLanca.append( "?,?,?," );
				insertSubLanca.append( "?,?,?,?,'S');" );

				psV = con.prepareStatement( insertSubLanca.toString() );
				psV.setInt( 1, Aplicativo.iCodEmp );
				psV.setInt( 2, ListaCampos.getMasterFilial( "FNSUBLANCA" ) );
				psV.setInt( 3, codlanca );
				psV.setInt( 4, 1 );
				psV.setInt( 5, codempvd );
				psV.setInt( 6, codfilialvd );
				psV.setString( 7, codplanvdconsig );
				psV.setString( 8, "S" );
				psV.setDate( 9, Funcoes.dateToSQLDate( txtDataConsig.getVlrDate() ) );
				if ( bRec ) {
					psV.setBigDecimal( 10, valor );
					psV.setString( 11, String.format( venda, rs.getInt( "CODCLI" ), rs.getInt( "CODPLANOPAG" ), rs.getInt( "CODVENDACO" ) ) );
				}
				else {
					psV.setBigDecimal( 10, valor );
					psV.setString( 11, String.format( venda, rs.getInt( "CODCLI" ), rs.getInt( "CODPLANOPAG" ), rs.getInt( "CODVENDACO" ) ) );
				}
				psV.executeUpdate();

				StringBuilder updateVendaConsignada = new StringBuilder();
				updateVendaConsignada.append( "UPDATE VDVENDACONSIG SET " );
				updateVendaConsignada.append( "CODEMPSL=?,CODFILIALSL=?,CODLANCA=?,CODSUBLANCA=? " );
				updateVendaConsignada.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODCONSIG=? AND CODVENDACO=?" );

				psV = con.prepareStatement( updateVendaConsignada.toString() );
				psV.setInt( 1, Aplicativo.iCodEmp );
				psV.setInt( 2, ListaCampos.getMasterFilial( "FNLANCA" ) );
				psV.setInt( 3, codlanca );
				psV.setInt( 4, 1 );
				psV.setInt( 5, Aplicativo.iCodEmp );
				psV.setInt( 6, ListaCampos.getMasterFilial( "VDCONSIGNACAO" ) );
				psV.setInt( 7, txtCodConsig.getVlrInteger() );
				psV.setInt( 8, rs.getInt( "CODVENDACO" ) );
				psV.executeUpdate();

				// CONTRAPARTIDA
				codlanca = getSequenciaLancamento();
				psV = con.prepareStatement( insertLanca.toString() );
				psV.setString( 1, "A" );
				psV.setInt( 2, Aplicativo.iCodEmp );
				psV.setInt( 3, ListaCampos.getMasterFilial( "FNLANCA" ) );
				psV.setInt( 4, codlanca );
				psV.setInt( 5, Aplicativo.iCodEmp );
				psV.setInt( 6, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
				psV.setString( 7, codplanvend );
				psV.setDate( 8, Funcoes.dateToSQLDate( txtDataConsig.getVlrDate() ) );
				if ( bRec ) {
					psV.setString( 9, String.format( recebimento, rs.getInt( "CODCLI" ), rs.getInt( "CODPLANOPAG" ), rs.getInt( "CODVENDACO" ) ) );
					psV.setBigDecimal( 10, valor );
				}
				else {
					psV.setString( 9, String.format( naoRecebimento, rs.getInt( "CODCLI" ), rs.getInt( "CODPLANOPAG" ), rs.getInt( "CODVENDACO" ) ) );
					psV.setBigDecimal( 10, valorVenda.subtract( valor ).multiply( new BigDecimal( "-1" ) ) );
				}
				psV.setString( 11, txtCodConsig.getVlrString() );
				psV.executeUpdate();

				psV = con.prepareStatement( insertSubLanca.toString() );
				psV.setInt( 1, Aplicativo.iCodEmp );
				psV.setInt( 2, ListaCampos.getMasterFilial( "FNSUBLANCA" ) );
				psV.setInt( 3, codlanca );
				psV.setInt( 4, 1 );
				psV.setInt( 5, codempvd );
				psV.setInt( 6, codfilialvd );
				psV.setString( 7, codplanvdconsig );
				psV.setString( 8, "S" );
				psV.setDate( 9, Funcoes.dateToSQLDate( txtDataConsig.getVlrDate() ) );
				if ( bRec ) {
					psV.setBigDecimal( 10, valor.multiply( new BigDecimal( "-1" ) ) );
					psV.setString( 11, String.format( recebimento, rs.getInt( "CODVENDACO" ), rs.getInt( "CODCLI" ), rs.getInt( "CODPLANOPAG" ) ) );
				}
				else {
					psV.setBigDecimal( 10, valorVenda.subtract( valor ) );
					psV.setString( 11, String.format( naoRecebimento, rs.getInt( "CODVENDACO" ), rs.getInt( "CODCLI" ), rs.getInt( "CODPLANOPAG" ) ) );
				}
				psV.executeUpdate();

				StringBuilder updateVendaConsignadaContrapartida = new StringBuilder();
				updateVendaConsignadaContrapartida.append( "UPDATE VDVENDACONSIG SET " );
				updateVendaConsignadaContrapartida.append( "CODEMPSC=?,CODFILIALSC=?,CODLANCASC=?,CODSUBLANCASC=? " );
				updateVendaConsignadaContrapartida.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODCONSIG=? AND CODVENDACO=?" );

				psV = con.prepareStatement( updateVendaConsignadaContrapartida.toString() );
				psV.setInt( 1, Aplicativo.iCodEmp );
				psV.setInt( 2, ListaCampos.getMasterFilial( "FNLANCA" ) );
				psV.setInt( 3, codlanca );
				psV.setInt( 4, 1 );
				psV.setInt( 5, Aplicativo.iCodEmp );
				psV.setInt( 6, ListaCampos.getMasterFilial( "VDCONSIGNACAO" ) );
				psV.setInt( 7, txtCodConsig.getVlrInteger() );
				psV.setInt( 8, rs.getInt( "CODVENDACO" ) );
				psV.executeUpdate();
			}
			else {

				StringBuilder updateSubLanca = new StringBuilder();
				updateSubLanca.append( "UPDATE FNSUBLANCA SET VLRSUBLANCA=? " );
				updateSubLanca.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODLANCA=? AND CODSUBLANCA=?" );

				psV = con.prepareStatement( updateSubLanca.toString() );
				psV.setBigDecimal( 1, valor );
				psV.setInt( 2, Aplicativo.iCodEmp );
				psV.setInt( 3, ListaCampos.getMasterFilial( "FNSUBLANCA" ) );
				psV.setInt( 4, rs.getInt( "CODLANCA" ) );
				psV.setInt( 5, rs.getInt( "CODSUBLANCA" ) );
				psV.executeUpdate();

				StringBuilder updateLanca = new StringBuilder();
				updateLanca.append( "UPDATE FNLANCA SET VLRLANCA=? " );
				updateLanca.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODLANCA=?" );

				psV = con.prepareStatement( updateLanca.toString() );
				psV.setBigDecimal( 1, valor.multiply( new BigDecimal( "-1" ) ) );
				psV.setInt( 2, Aplicativo.iCodEmp );
				psV.setInt( 3, ListaCampos.getMasterFilial( "FNLANCA" ) );
				psV.setInt( 4, rs.getInt( "CODLANCA" ) );
				psV.executeUpdate();

				// Contrapartida
				psV = con.prepareStatement( updateSubLanca.toString() );
				if ( bRec ) {
					psV.setBigDecimal( 1, valor );
				}
				else {
					psV.setBigDecimal( 1, valorVenda.subtract( valor ).multiply( new BigDecimal( "-1" ) ) );
				}
				psV.setBigDecimal( 1, valor );
				psV.setInt( 2, Aplicativo.iCodEmp );
				psV.setInt( 3, ListaCampos.getMasterFilial( "FNSUBLANCA" ) );
				psV.setInt( 4, rs.getInt( "CODLANCASC" ) );
				psV.setInt( 5, rs.getInt( "CODSUBLANCASC" ) );
				psV.executeUpdate();

				psV = con.prepareStatement( updateLanca.toString() );
				if ( bRec ) {
					psV.setBigDecimal( 1, valor.multiply( new BigDecimal( "-1" ) ) );
				}
				else {
					psV.setBigDecimal( 1, valorVenda.subtract( valor ) );
				}
				psV.setInt( 2, Aplicativo.iCodEmp );
				psV.setInt( 3, ListaCampos.getMasterFilial( "FNLANCA" ) );
				psV.setInt( 4, rs.getInt( "CODLANCASC" ) );
				psV.executeUpdate();
			}
		}
	}

	private String getPlanejamentoVendedor() {

		String planejamento = null;

		try {

			StringBuilder sql = new StringBuilder();
			sql.append( "select c.codplan from vdvendedor v, fnconta c " );
			sql.append( "where v.codemp=? and v.codfilial=? and v.codvend=? " );
			sql.append( "and v.codempca=c.codemp and v.codfilialca=c.codfilial " );
			sql.append( "and v.numconta=c.numconta " );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "vdvendedor" ) );
			ps.setInt( 3, txtCodVend.getVlrInteger() );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				planejamento = rs.getString( "codplan" );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao verificar conta do vendedor!" );
		}

		return planejamento;
	}

	private int getLancamentoConsignacao() {

		int lancamento = -1;

		try {

			StringBuilder sql = new StringBuilder();
			sql.append( "select codlanca from vdconsignacao " );
			sql.append( "where codemp=? and codfilial=? and codconsig=? " );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "vdconsignacao" ) );
			ps.setInt( 3, txtCodConsig.getVlrInteger() );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				lancamento = rs.getInt( "codlanca" );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao verificar conta do vendedor!" );
		}

		return lancamento;
	}

	private int getLancamentoConsignacaoDevolucao() {

		int lancamento = -1;

		try {

			StringBuilder sql = new StringBuilder();
			sql.append( "select codlancasd from vdconsignacao " );
			sql.append( "where codemp=? and codfilial=? and codconsig=? " );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "vdconsignacao" ) );
			ps.setInt( 3, txtCodConsig.getVlrInteger() );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				lancamento = rs.getInt( "codlancasd" );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao verificar conta do vendedor!" );
		}

		return lancamento;
	}

	private int getSequenciaLancamento() {

		int codlanca = -1;

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT ISEQ FROM SPGERANUM(?,?,'LA')" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNLANCA" ) );
			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				codlanca = rs.getInt( "ISEQ" );
			}

		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao verificar conta do vendedor!" );
		}

		return codlanca;
	}

	private void importarVendaRemessa() {

		if ( txtCodConsig.getVlrInteger() == 0 ) {
			return;
		}

		DLBuscaListaVendas buscaRemessa = new DLBuscaListaVendas( "VR" );
		buscaRemessa.setConexao( con );
		buscaRemessa.setVisible( true );

		if ( buscaRemessa.OK ) {

			List<DLBuscaListaVendas.GridBuscaRemessa> gridBuscaRemessa = buscaRemessa.getGridBuscaRemessa();

			for ( DLBuscaListaVendas.GridBuscaRemessa g : gridBuscaRemessa ) {

				lcDet.cancel( true );
				lcDet.insert( true );

				txtCodProdRem.setVlrInteger( g.getCodigoProduto() );
				txtPrecoRem.setVlrBigDecimal( g.getPreco() );
				txtQtdSaidaRem.setVlrBigDecimal( g.getQuantidade() );

				lcDet.post();
			}

			lcCampos.carregaDados();
		}

		buscaRemessa.dispose();
	}

	private void importarItensVenda() {

		if ( txtCodConsig.getVlrInteger() == 0 ) {
			return;
		}

		DLBuscaListaVendas buscaRemessa = new DLBuscaListaVendas( null );
		buscaRemessa.setConexao( con );
		buscaRemessa.setVisible( true );

		if ( buscaRemessa.OK ) {

			List<DLBuscaListaVendas.GridBuscaRemessa> gridBuscaRemessa = buscaRemessa.getGridBuscaRemessa();

			for ( DLBuscaListaVendas.GridBuscaRemessa g : gridBuscaRemessa ) {

				lcDetVendas.cancel( true );
				lcDetVendas.insert( true );

				txtCodProdVenda.setVlrInteger( g.getCodigoProduto() );
				txtPreco.setVlrBigDecimal( g.getPreco() );
				txtQtdVenda.setVlrBigDecimal( g.getQuantidade() );
				txtCodCliVenda.setVlrInteger( g.getCliente() );
				txtCodPlanoPag.setVlrInteger( g.getPlanoPagamento() );

				cbRecebido.setVlrString( getTipoMovimento( g.getCodigoVenda(), "V" ) ? "N" : "S" );

				lcDetVendas.post();

				try {

					PreparedStatement ps = con.prepareStatement( "UPDATE VDVENDACONSIG SET CODEMPVD=?, CODFILIALVD=?, TIPOVENDA=?, CODVENDA=?, CODITVENDA=? " + "WHERE CODEMP=? AND CODFILIAL=? AND CODCONSIG=? AND CODVENDACO=?" );
					ps.setInt( 1, Aplicativo.iCodEmp );
					ps.setInt( 2, ListaCampos.getMasterFilial( "VDVENDA" ) );
					ps.setString( 3, "V" );
					ps.setInt( 4, g.getCodigoVenda() );
					ps.setInt( 5, g.getItemVenda() );
					ps.setInt( 6, Aplicativo.iCodEmp );
					ps.setInt( 7, ListaCampos.getMasterFilial( "VDVENDACONSIG" ) );
					ps.setInt( 8, txtCodConsig.getVlrInteger() );
					ps.setInt( 9, txtCodVendaCo.getVlrInteger() );

					ps.executeUpdate();

					con.commit();

				} catch ( SQLException err ) {
					err.printStackTrace();
				}
			}

			lcCampos.carregaDados();
		}

		buscaRemessa.dispose();
	}

	private boolean getTipoMovimento( int codigoVenda, String tipoVenda ) {

		boolean tipoMovimentoFiscal = false;

		try {

			StringBuilder sql = new StringBuilder();
			sql.append( "SELECT TM.FISCALTIPOMOV FROM EQTIPOMOV TM, VDVENDA V " );
			sql.append( "WHERE TM.CODEMP=V.CODEMPTM TM.CODFILIAL=V.CODFILIALTM AND TM.CODTIPOMOV=V.CODTIPOMOV AND " );
			sql.append( "V.CODEMP=? AND V.CODFILIAL=? AND V.CODVENDA=? AND V.TIPOVENDA=?" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQTIPOMOV" ) );
			ps.setInt( 3, codigoVenda );
			ps.setString( 4, tipoVenda );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				tipoMovimentoFiscal = "S".equals( rs.getString( "TIPOMOV" ) );
			}

			con.commit();

		} catch ( SQLException err ) {
			err.printStackTrace();
		}

		return tipoMovimentoFiscal;
	}

	private void novoLanca() {

		String planejamento = getPlanejamentoVendedor();
		if ( planejamento == null ) {
			Funcoes.mensagemInforma( this, "Não foi encontrado planejamento do vendedor." );
			return;
		}

		if ( !Aplicativo.telaPrincipal.temTela( FSubLanca.class.getName() ) ) {
			FSubLanca form = new FSubLanca( null, planejamento, txtDataIniRedeber.getVlrDate(), txtDataFimReceber.getVlrDate() );
			Aplicativo.telaPrincipal.criatela( "FSubLanca", form, con );

			form.addInternalFrameListener( new InternalFrameAdapter() {

				public void internalFrameClosed( InternalFrameEvent ievt ) {

					carregaFechamento();
				}
			} );
		}
	}

	private void abreSubLanca() {

		String codLanca = (String) tabFechamento.getValor( tabFechamento.getLinhaSel(), ETabFechamento.NLANCA.ordinal() );
		Date dtini = txtDataIniRedeber.getVlrDate();
		Date dtfim = txtDataFimReceber.getVlrDate();

		if ( !Aplicativo.telaPrincipal.temTela( FSubLanca.class.getName() ) ) {
			FSubLanca tela = new FSubLanca( codLanca, null, dtini, dtfim );
			Aplicativo.telaPrincipal.criatela( "Lançamentos", tela, con );
		}
	}

	public void actionPerformed( ActionEvent e ) {

		super.actionPerformed( e );

		if ( e.getSource() == btImportarVendaRemessa ) {
			importarVendaRemessa();
		}
		else if ( e.getSource() == btImportarItensVenda ) {
			importarItensVenda();
		}
		else if ( e.getSource() == btConsolidacao ) {
			consolidacao();
		}
		else if ( e.getSource() == btNovoLancamento ) {
			novoLanca();
		}
		else if ( e.getSource() == btPesquisaReceber ) {
			// montaGridReceber();
		}
		else if ( e.getSource() == btSelecionTodosReceber ) {
			// selecionaTodosReceber();
		}
		else if ( e.getSource() == btSelecionaNenhumReceber ) {
			// selecionaNenhumReceber();
		}
		else if ( e.getSource() == btColocarEmCobrancaReceber ) {
			// colocarEmCobranca();
		}
	}

	public void mouseClicked( MouseEvent e ) {

		if ( e.getClickCount() == 2 ) {
			if ( e.getSource() == tabReceber ) {
				// abreTelaRec();
			}
			else if ( e.getSource() == tabFechamento ) {
				abreSubLanca();
			}
		}
	}

	public void mouseEntered( MouseEvent e ) {

	}

	public void mouseExited( MouseEvent e ) {

	}

	public void mousePressed( MouseEvent e ) {

	}

	public void mouseReleased( MouseEvent e ) {

	}

	public void stateChanged( ChangeEvent e ) {

		if ( e.getSource() == tpnGeral ) {
			if ( tpnGeral.getSelectedIndex() == 0 ) {
				panelDevolucaoCampos.removeAll();
				panelRemessaCampos.add( panelGeralDetalhe );
				lbQtdSaidaRem.setVisible( true );
				txtQtdSaidaRem.setVisible( true );
				lbQtdDevolucaoRem.setVisible( false );
				txtQtdDevolucaoRem.setVisible( false );
				txtSQLRemessa.setVlrString( "S" );
				txtSQLDevolucao.setVlrString( "N" );

				tab.getColumnModel().getColumn( 4 ).setMaxWidth( 100 );
				tab.getColumnModel().getColumn( 4 ).setMinWidth( 100 );
				tab.getTableHeader().getColumnModel().getColumn( 4 ).setMaxWidth( 100 );
				tab.getTableHeader().getColumnModel().getColumn( 4 ).setMinWidth( 100 );

				tab.getColumnModel().getColumn( 5 ).setMaxWidth( 0 );
				tab.getColumnModel().getColumn( 5 ).setMinWidth( 0 );
				tab.getTableHeader().getColumnModel().getColumn( 5 ).setMaxWidth( 0 );
				tab.getTableHeader().getColumnModel().getColumn( 5 ).setMinWidth( 0 );

				lcDet.limpaCampos( true );
				lcCampos.carregaDados();
			}
			else if ( tpnGeral.getSelectedIndex() == 1 ) {
				panelRemessaCampos.removeAll();
				panelDevolucaoCampos.add( panelGeralDetalhe );
				lbQtdSaidaRem.setVisible( false );
				txtQtdSaidaRem.setVisible( false );
				lbQtdDevolucaoRem.setVisible( true );
				txtQtdDevolucaoRem.setVisible( true );
				txtSQLRemessa.setVlrString( "N" );
				txtSQLDevolucao.setVlrString( "S" );

				tab.getColumnModel().getColumn( 4 ).setMaxWidth( 0 );
				tab.getColumnModel().getColumn( 4 ).setMinWidth( 0 );
				tab.getTableHeader().getColumnModel().getColumn( 4 ).setMaxWidth( 0 );
				tab.getTableHeader().getColumnModel().getColumn( 4 ).setMinWidth( 0 );

				tab.getColumnModel().getColumn( 5 ).setMaxWidth( 100 );
				tab.getColumnModel().getColumn( 5 ).setMinWidth( 100 );
				tab.getTableHeader().getColumnModel().getColumn( 5 ).setMaxWidth( 100 );
				tab.getTableHeader().getColumnModel().getColumn( 5 ).setMinWidth( 100 );

				lcDet.limpaCampos( true );
				lcCampos.carregaDados();
			}
		}
		if ( e.getSource() == tpnPrincipal ) {
			if ( tpnPrincipal.getSelectedIndex() == 0 ) {
				navRod.setVisible( true );
			}
			else if ( tpnPrincipal.getSelectedIndex() == 1 ) {
				navRod.setVisible( false );
			}
			else if ( tpnPrincipal.getSelectedIndex() == 2 ) {
				navRod.setVisible( false );
				carregaFechamento();
			}
		}
	}

	public void beforeInsert( InsertEvent e ) {

		if ( e.getListaCampos() == lcDetVendas ) {
			oldCliente = txtCodCliVenda.getVlrInteger();
			oldPlanoPagamento = txtCodPlanoPag.getVlrInteger();
		}
	}

	public void afterInsert( InsertEvent e ) {

		if ( e.getListaCampos() == lcCampos ) {
			txtDataConsig.setVlrDate( new Date() );
		}
		else if ( e.getListaCampos() == lcDet ) {
			txtQtdSaidaRem.setVlrBigDecimal( new BigDecimal( "0.00" ) );
			txtQtdDevolucaoRem.setVlrBigDecimal( new BigDecimal( "0.00" ) );
			txtCodProdRem.requestFocus();
		}
		else if ( e.getListaCampos() == lcDetVendas ) {
			if ( oldCliente > 0 ) {
				txtCodCliVenda.setVlrInteger( oldCliente );
				lcClienteVenda.carregaDados();
			}
			if ( oldPlanoPagamento > 0 ) {
				txtCodPlanoPag.setVlrInteger( oldPlanoPagamento );
				lcPlanoPagamento.carregaDados();
			}
			txtQtdVenda.setVlrBigDecimal( new BigDecimal( "0.00" ) );
			txtQtdTroca.setVlrBigDecimal( new BigDecimal( "0.00" ) );
			txtQtdBonif.setVlrBigDecimal( new BigDecimal( "0.00" ) );
			txtVlrDescVenda.setVlrBigDecimal( new BigDecimal( "0.00" ) );
		}
	}

	public void setConexao( DbConnection con ) {

		super.setConexao( con );

		lcVendedor.setConexao( con );
		lcProdutoRemessa.setConexao( con );
		lcProdutoVenda.setConexao( con );
		lcClienteVenda.setConexao( con );
		lcPlanoPagamento.setConexao( con );
		lcClienteRec.setConexao( con );

		getPlanejamentos();
	}
}
