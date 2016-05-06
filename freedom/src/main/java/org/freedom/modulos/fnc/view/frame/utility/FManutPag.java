/**
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 *  
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FManutPag.java <BR>
 * 
 *                    Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                    modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                    na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                    Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                    sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                    Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                    Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                    de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                    Tela de manutenção de contas a pagar.
 * 
 */

package org.freedom.modulos.fnc.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.functions.ConversionFunctions;
import org.freedom.infra.functions.StringFunctions;
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
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.library.swing.util.SwingParams;
import org.freedom.library.type.StringDireita;
import org.freedom.modulos.fnc.business.object.Cheque;
import org.freedom.modulos.fnc.view.dialog.report.DLImpReciboPag;
import org.freedom.modulos.fnc.view.dialog.utility.DLBaixaPag;
import org.freedom.modulos.fnc.view.dialog.utility.DLConsultaBaixaPagamento;
import org.freedom.modulos.fnc.view.dialog.utility.DLEditaPag;
import org.freedom.modulos.fnc.view.dialog.utility.DLEstornoMultiplaBaixaPagamento;
import org.freedom.modulos.fnc.view.dialog.utility.DLNovoPag;
import org.freedom.modulos.fnc.view.dialog.utility.DLBaixaPag.RET_BAIXA_PAG;
import org.freedom.modulos.fnc.view.dialog.utility.DLBaixaPag.VAL_BAIXAMANUT;
import org.freedom.modulos.fnc.view.dialog.utility.DLEditaPag.EDIT_PAG_SETVALORES;
import org.freedom.modulos.fnc.view.frame.crud.plain.FSinalizadores;
import org.freedom.modulos.std.dao.DAOMovimento;
import org.freedom.modulos.std.view.dialog.utility.DLCancItem;

public class FManutPag extends FFilho implements ActionListener, CarregaListener, ChangeListener, MouseListener, KeyListener {

	private static final long serialVersionUID = 1L;

	// private static final String HISTORICO_PADRAO = "PAGAMENTO REF. A COMPRA: <DOCUMENTO>";

	private ImageIcon imgVencido = Icone.novo( "clVencido.gif" );

	private ImageIcon imgPago = Icone.novo( "clPago.gif" );

	private ImageIcon imgPagoParcial = Icone.novo( "clPagoParcial.gif" );

	private ImageIcon imgNaoVencido = Icone.novo( "clNaoVencido.gif" );

	private ImageIcon imgCancelado = Icone.novo( "clCancelado.gif" );

	private ImageIcon imgPagoComParciais = Icone.novo( "clQuitadoParcial.gif" );

	private JPanelPad pnLegenda = new JPanelPad( JPanelPad.TP_JPANEL );

	private JLabelPad lbVencido = new JLabelPad( "Vencido", imgVencido, SwingConstants.LEFT );

	private JLabelPad lbParcial = new JLabelPad( "Pg.Parcial", imgPagoParcial, SwingConstants.LEFT );

	private JLabelPad lbPago = new JLabelPad( "Quitado", imgPago, SwingConstants.LEFT );

	private JLabelPad lbVencer = new JLabelPad( "À vencer", imgNaoVencido, SwingConstants.LEFT );

	private JLabelPad lbCancelado = new JLabelPad( "Cancelado", imgCancelado, SwingConstants.LEFT );

	private JPanelPad pnTabConsulta = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinBotoesConsulta = new JPanelPad( 40, 100 );

	private JPanelPad pnRod = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinConsulta = new JPanelPad( 500, 140 );

	private JPanelPad pnConsulta = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTabbedPanePad tpn = new JTabbedPanePad();

	private JTablePad tabConsulta = new JTablePad();

	private JScrollPane spnConsulta = new JScrollPane( tabConsulta );

	private JPanelPad pnTabBaixa = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinBotoesBaixa = new JPanelPad( 40, 100 );

	private JPanelPad pinBaixa = new JPanelPad( 500, 140 );

	private JPanelPad pnBaixa = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTablePad tabBaixa = new JTablePad();

	private JScrollPane spnBaixa = new JScrollPane( tabBaixa );

	private JPanelPad pnTabManut = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinBotoesManut = new JPanelPad( 40, 210 );

	private JPanelPad pinManut = new JPanelPad( 500, 155 );

	private JPanelPad pnManut = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTablePad tabManut = new JTablePad();

	private JScrollPane spnManut = new JScrollPane( tabManut );

	private JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtCNPJFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldFK txtRazFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCodForManut = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtCodForManut2 = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtCNPJForManut = new JTextFieldFK( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtPrimCompr = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtUltCompr = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtVlrMaxFat = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtDataMaxFat = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtVlrTotCompr = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtVlrTotPago = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtVlrTotAberto = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtDataMaxAcum = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtVlrMaxAcum = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtCodPagBaixa = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodPagManut = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDoc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDocManut = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtSerie = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtCodCompraBaixa = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodCompraManut = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtNumCheque = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodOrdCp = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCorSinal = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodSinal = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDescSinal = new JTextFieldPad( JTextFieldPad.TP_STRING, 100, 0 );

	private JTextFieldPad txtCodForOC = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDtApOrdCP = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtObsOrdCP = new JTextFieldPad( JTextFieldPad.TP_STRING, 500, 0 );

	private JTextFieldPad txtCodForBaixa = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtCNPJForBaixa = new JTextFieldFK( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtDtEmisBaixa = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldFK txtDtEmisManut = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodBancoBaixa = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private JTextFieldPad txtTotPagBaixa = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtTotAbertoBaixa = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtTotPagoBaixa = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtJurosBaixa = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtDatainiManut = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafimManut = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldFK txtRazForManut = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldFK txtRazForManut2 = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldFK txtRazForBaixa = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldFK txtDescBancoBaixa = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtTotalVencido = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 10, Aplicativo.casasDecFin );

	private JTextFieldPad txtTotalParcial = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 10, Aplicativo.casasDecFin );

	private JTextFieldPad txtTotalPago = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 10, Aplicativo.casasDecFin );

	private JTextFieldPad txtTotalVencer = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 10, Aplicativo.casasDecFin );

	private JTextFieldPad txtTotalCancelado = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 10, Aplicativo.casasDecFin );

	private JLabelPad lbFiltroStatus = new JLabelPad( "Filtrar por:" );

	private JPanelPad pinLbFiltroStatus = new JPanelPad( 53, 15 );

	private JPanelPad pinFiltroStatus = new JPanelPad( 300, 150 );

	private JButtonPad btBaixaConsulta = new JButtonPad( Icone.novo( "btOk.png" ) );

	private JButtonPad btCarregaBaixasMan = new JButtonPad( Icone.novo( "btConsBaixa.png" ) );

	private JButtonPad btBaixaManut = new JButtonPad( Icone.novo( "btOk.png" ) );

	private JButtonPad btEditManut = new JButtonPad( Icone.novo( "btEditar.gif" ) );

	private JButtonPad btNovoManut = new JButtonPad( Icone.novo( "btNovo.png" ) );

	private JButtonPad btExcluirManut = new JButtonPad( Icone.novo( "btExcluir.png" ) );

	private JButtonPad btEstManut = new JButtonPad( Icone.novo( "btCancelar.png" ) );

	private JButtonPad btCancItem = new JButtonPad( Icone.novo( "btCancItem.png" ) );

	private JButtonPad btImpRec = new JButtonPad( Icone.novo( "btImprimeRec.png" ) );

	private JButtonPad btExecManut = new JButtonPad( Icone.novo( "btExecuta.png" ) );

	private JButtonPad btBaixa = new JButtonPad( Icone.novo( "btOk.png" ) );

	private JButtonPad btCarregaBaixas = new JButtonPad( Icone.novo( "btConsBaixa.png" ) );

	private JButtonPad btSair = new JButtonPad( "Sair", Icone.novo( "btSair.png" ) );

	private JCheckBoxPad cbPagas = new JCheckBoxPad( "Pagas", "S", "N" );

	private JCheckBoxPad cbAPagar = new JCheckBoxPad( "À Pagar", "S", "N" );

	private JCheckBoxPad cbPagParcial = new JCheckBoxPad( "Parcial", "S", "N" );

	private JCheckBoxPad cbCanceladas = new JCheckBoxPad( "Canceladas", "S", "N" );

	private JCheckBoxPad cbEmpenhos = new JCheckBoxPad( "Empenhos", "S", "N" );

	private JRadioGroup<?, ?> rgData = null;

	private JRadioGroup<?, ?> rgVenc = null;

	private Vector<String> vCodPag = new Vector<String>();

	private Vector<String> vNParcPag = new Vector<String>();

	private Vector<String> vNParcBaixa = new Vector<String>();

	private Vector<String> vCodPed = new Vector<String>();

	private Vector<String> vNumContas = new Vector<String>();

	private Vector<String> vCodPlans = new Vector<String>();

	private Vector<String> vCodCCs = new Vector<String>();

	private Vector<String> vDtEmiss = new Vector<String>();

	private Vector<String> vValsData = new Vector<String>();

	private Vector<String> vLabsData = new Vector<String>();

	private Vector<String> vValsVenc = new Vector<String>();

	private Vector<String> vLabsVenc = new Vector<String>();

	private ImageIcon imgColuna = null;

	private ListaCampos lcFor = new ListaCampos( this );

	private ListaCampos lcForBaixa = new ListaCampos( this );

	private ListaCampos lcForManut = new ListaCampos( this );

	private ListaCampos lcSinal = new ListaCampos( this );

	private ListaCampos lcForManut2 = new ListaCampos( this );

	private ListaCampos lcOrdCompra = new ListaCampos( this );

	private ListaCampos lcCompraBaixa = new ListaCampos( this );

	private ListaCampos lcPagBaixa = new ListaCampos( this );

	private ListaCampos lcPagManut = new ListaCampos( this );

	private ListaCampos lcBancoBaixa = new ListaCampos( this );

	private Date dIniManut = null;

	private Date dFimManut = null;

	private int iCodPag = 0;

	private int iNParcPag = 0;

	private int iAnoCC = 0;

	private JPopupMenu menuCores = new JPopupMenu();

	private Map<String, Object> prefere = null;

	private JMenuItem menu_limpa_cor_linha = new JMenuItem();

	private JMenuItem menu_cadastra_cor = new JMenuItem();

	private JMenuItem menu_limpa_cor_tudo = new JMenuItem();

	private DAOMovimento daoMovimento = null;

	private static final String PAGAMENTO_PARCIAL = "PL";

	private static final String PAGAMENTO_TOTAL = "PP";

	private static final String CANCELADO = "CP";

	private enum UPDATE_BAIXAMANUT_PARAMS {NONE, NUMCONTA, CODEMPCA, CODFILIALCA, CODPLAN, CODEMPPN, CODFILIALPN,
		DOCLANCAITPAG, DTPAGOITPAG, VLRDESCITPAG, VLRJUROSITPAG, VLRPAGOITPAG, ANOCC, CODCC, CODEMPCC, CODFILIALCC,
		CODTIPOCOB, CODEMPTC, CODFILIALTC, OBSITPAG, MULTIBAIXA, 
		CODCONTR, CODITCONTR, CODEMPCT, CODFILIALCT,
		CODPAG, NPARCPAG, CODEMP, CODFILIAL}

	public enum enum_tab_manut {SEL, IMGSTATUS, DTVENCITPAG, STATUSITPAG, CODFOR, RAZFOR, OBSITPAG, CODPAG
		, CHEQUES, NPARCPAG, DOC, DOCCOMPRA, CODORDCP, VLRPARCITPAG
		, DTPAGOITPAG, VLRPAGOITPAG, VLRDESCITPAG, VLRJUROSITPAG, VLRDEVITPAG, VLRADICITPAG, VLRAPAGITPAG
		, VLRCANCITPAG, NUMCONTA, DESCPLAN, DESCCC, CODTIPOCOB, DESCTIPOCOB, CODCOMPRA, CODPLAN, CODCC
		, DTITPAG, CODCONTR, DESCCONTR, CODITCONTR, DESCITCONTR   }

	public enum enum_tab_baixa {STATUS, DTVENCTO, NPARC, DOC, CODCOMPRA, VLRPARC, DTPAGTO, VLRPAGO, VLRDESC
		, VLRJUROS, VLRAPAG, VLRCANC, NUMCONTA, DESCCONTA, CODPLAN, DESCPLAN, CODCC,  DESCCC, OBS
		, CODCONTR, DESCCONTR, CODITCONTR, DESCITCONTR}

	public FManutPag() {

		super( false );
		setTitulo( "Manutenção de contas a pagar" );
		setAtribos( 20, 20, 840, 600 );
		
		montarListaCampos();
		montarGridConsultaPag();
		montarGridBaixaPag();
		montarGridManutPag();
		montarTela();
		carregarListeners();

	/*	cbAPagar.setVlrString( "S" );
		cbPagParcial.setVlrString( "S" );

		Container c = getContentPane();
		c.setLayout( new BorderLayout() );
		c.add( pnRod, BorderLayout.SOUTH );
		c.add( tpn, BorderLayout.CENTER );


		//	btSair.setPreferredSize( new Dimension( 90, 30 ) );

		pnLegenda.setPreferredSize( new Dimension( 840, 50 ) );
		pnLegenda.setLayout( null );

		lbVencido.setBounds( 5, 0, 150, 17 );
		txtTotalVencido.setBounds( 5, 18, 150, 18 );
		lbParcial.setBounds( 160, 0, 150, 17 );
		txtTotalParcial.setBounds( 160, 18, 150, 18 );
		lbPago.setBounds( 315, 0, 150, 17 );
		txtTotalPago.setBounds( 315, 18, 150, 18 );
		lbVencer.setBounds( 5, 36, 150, 17 );
		txtTotalVencer.setBounds( 5, 54, 150, 18 );
		lbCancelado.setBounds( 160, 36, 150, 17 );
		txtTotalCancelado.setBounds( 160, 54, 150, 18 );
		btSair.setBounds( 720, 18, 90, 40 );

		pnLegenda.add( lbVencido );
		pnLegenda.add( txtTotalVencido );
		pnLegenda.add( lbParcial );
		pnLegenda.add( txtTotalParcial );
		pnLegenda.add( lbPago );
		pnLegenda.add( txtTotalPago );
		pnLegenda.add( lbVencer );
		pnLegenda.add( txtTotalVencer );
		pnLegenda.add( lbCancelado );
		pnLegenda.add( txtTotalCancelado );
		pnLegenda.add( btSair );

		txtTotalVencido.setSoLeitura( true );
		txtTotalParcial.setSoLeitura( true );
		txtTotalPago.setSoLeitura( true );
		txtTotalVencer.setSoLeitura( true );
		txtTotalCancelado.setSoLeitura( true );

		txtTotalVencido.setFont( SwingParams.getFontbold() );
		txtTotalParcial.setFont( SwingParams.getFontbold() );
		txtTotalPago.setFont( SwingParams.getFontbold() );
		txtTotalVencer.setFont( SwingParams.getFontbold() );
		txtTotalCancelado.setFont( SwingParams.getFontbold() );

		pnRod.setBorder( BorderFactory.createEtchedBorder() );
		pnRod.setPreferredSize( new Dimension( 600, 80 ) );

		//pnRod.add( btSair, BorderLayout.EAST );
		pnRod.add( pnLegenda, BorderLayout.WEST );

		btSair.addActionListener( this );
		btImpRec.addActionListener( this );

		// Consulta:

		lcFor.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.for.", ListaCampos.DB_PK, false ) );
		lcFor.add( new GuardaCampo( txtRazFor, "RazFor", "Descrição do fornecedor", ListaCampos.DB_SI, false ) );
		lcFor.add( new GuardaCampo( txtCNPJFor, "CnpjFor", "CNPJ", ListaCampos.DB_SI, false ) );

		lcFor.montaSql( false, "FORNECED", "CP" );
		lcFor.setQueryCommit( false );
		lcFor.setReadOnly( true );
		txtCodFor.setTabelaExterna( lcFor, null );
		txtCodFor.setFK( true );
		txtCodFor.setNomeCampo( "CodFor" );

		txtPrimCompr.setAtivo( false );
		txtUltCompr.setAtivo( false );
		txtDataMaxFat.setAtivo( false );
		txtVlrMaxFat.setAtivo( false );
		txtVlrTotCompr.setAtivo( false );
		txtVlrTotPago.setAtivo( false );
		txtVlrTotAberto.setAtivo( false );
		txtDataMaxAcum.setAtivo( false );
		txtVlrMaxAcum.setAtivo( false );

		Funcoes.setBordReq( txtCodFor );

		tpn.addTab( "Consulta", pnConsulta );

		btBaixaConsulta.setToolTipText( "Baixa" );

		pnConsulta.add( pinConsulta, BorderLayout.NORTH );
		pnTabConsulta.add( pinBotoesConsulta, BorderLayout.EAST );
		pnTabConsulta.add( spnConsulta, BorderLayout.CENTER );
		pnConsulta.add( pnTabConsulta, BorderLayout.CENTER );

		pinConsulta.adic( new JLabelPad( "Cód.for." ), 7, 0, 250, 20 );
		pinConsulta.adic( txtCodFor, 7, 20, 80, 20 );
		pinConsulta.adic( new JLabelPad( "Descrição do fornecedor" ), 90, 0, 250, 20 );
		pinConsulta.adic( txtRazFor, 90, 20, 217, 20 );
		pinConsulta.adic( new JLabelPad( "Primeira compra" ), 310, 0, 98, 20 );
		pinConsulta.adic( txtPrimCompr, 310, 20, 98, 20 );
		pinConsulta.adic( new JLabelPad( "Última compra" ), 411, 0, 97, 20 );
		pinConsulta.adic( txtUltCompr, 411, 20, 100, 20 );
		pinConsulta.adic( new JLabelPad( "Data" ), 7, 40, 98, 20 );
		pinConsulta.adic( txtDataMaxFat, 7, 60, 98, 20 );
		pinConsulta.adic( new JLabelPad( "Valor da maior fatura" ), 108, 40, 139, 20 );
		pinConsulta.adic( txtVlrMaxFat, 108, 60, 139, 20 );
		pinConsulta.adic( new JLabelPad( "Data" ), 250, 40, 98, 20 );
		pinConsulta.adic( txtDataMaxAcum, 250, 60, 120, 20 );
		pinConsulta.adic( new JLabelPad( "Valor do maior acumulo" ), 373, 40, 139, 20 );
		pinConsulta.adic( txtVlrMaxAcum, 373, 60, 139, 20 );
		pinConsulta.adic( new JLabelPad( "Total de compras" ), 7, 80, 150, 20 );
		pinConsulta.adic( txtVlrTotCompr, 7, 100, 165, 20 );
		pinConsulta.adic( new JLabelPad( "Total pago" ), 175, 80, 97, 20 );
		pinConsulta.adic( txtVlrTotPago, 175, 100, 165, 20 );
		pinConsulta.adic( new JLabelPad( "Total em aberto" ), 343, 80, 117, 20 );
		pinConsulta.adic( txtVlrTotAberto, 343, 100, 167, 20 );
		pinBotoesConsulta.adic( btBaixaConsulta, 5, 10, 30, 30 );
		
		montarGridConsultaPag();
		tabConsulta.adicColuna( "" );
		tabConsulta.adicColuna( "Vencimento" );
		tabConsulta.adicColuna( "Série" );
		tabConsulta.adicColuna( "Doc." );
		tabConsulta.adicColuna( "Cód. compra" );
		tabConsulta.adicColuna( "Data da compra" );
		tabConsulta.adicColuna( "Valor" );
		tabConsulta.adicColuna( "Data pagamento" );
		tabConsulta.adicColuna( "Valor pago" );
		tabConsulta.adicColuna( "Atraso" );
		tabConsulta.adicColuna( "Observações" );
		tabConsulta.adicColuna( "Banco" );
		tabConsulta.adicColuna( "Valor canc." );

		tabConsulta.setTamColuna( 0, 0 );
		tabConsulta.setTamColuna( 90, 1 );
		tabConsulta.setTamColuna( 50, 2 );
		tabConsulta.setTamColuna( 50, 3 );
		tabConsulta.setTamColuna( 90, 4 );
		tabConsulta.setTamColuna( 110, 5 );
		tabConsulta.setTamColuna( 90, 6 );
		tabConsulta.setTamColuna( 110, 7 );
		tabConsulta.setTamColuna( 100, 8 );
		tabConsulta.setTamColuna( 60, 9 );
		tabConsulta.setTamColuna( 100, 10 );
		tabConsulta.setTamColuna( 80, 11 );
		tabConsulta.setTamColuna( 90, 12 );

		tabConsulta.setRowHeight( 20 );
		// Baixa:

		lcCompraBaixa.add( new GuardaCampo( txtCodCompraBaixa, "CodCompra", "Cód.compra", ListaCampos.DB_PK, false ) );
		lcCompraBaixa.add( new GuardaCampo( txtSerie, "Serie", "Série", ListaCampos.DB_SI, false ) );
		lcCompraBaixa.montaSql( false, "COMPRA", "CP" );
		lcCompraBaixa.setQueryCommit( false );
		lcCompraBaixa.setReadOnly( true );
		txtCodCompraBaixa.setTabelaExterna( lcCompraBaixa, null );
		txtCodCompraBaixa.setFK( true );
		txtCodCompraBaixa.setNomeCampo( "CodCompra" );

		lcForBaixa.add( new GuardaCampo( txtCodForBaixa, "CodFor", "Cód.for", ListaCampos.DB_PK, false ) );
		lcForBaixa.add( new GuardaCampo( txtRazForBaixa, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );
		lcForBaixa.add( new GuardaCampo( txtCNPJForBaixa, "CnpjFor", "CNPJ", ListaCampos.DB_SI, false ) );
		lcForBaixa.montaSql( false, "FORNECED", "CP" );
		lcForBaixa.setQueryCommit( false );
		lcForBaixa.setReadOnly( true );
		txtCodForBaixa.setTabelaExterna( lcForBaixa, null );
		txtCodForBaixa.setFK( true );
		txtCodForBaixa.setNomeCampo( "CodFor" );

		lcBancoBaixa.add( new GuardaCampo( txtCodBancoBaixa, "CodBanco", "Cód.banco", ListaCampos.DB_PK, false ) );
		lcBancoBaixa.add( new GuardaCampo( txtDescBancoBaixa, "NomeBanco", "Nome banco", ListaCampos.DB_SI, false ) );
		lcBancoBaixa.montaSql( false, "BANCO", "FN" );
		lcBancoBaixa.setQueryCommit( false );
		lcBancoBaixa.setReadOnly( true );
		txtCodBancoBaixa.setTabelaExterna( lcBancoBaixa, null );
		txtCodBancoBaixa.setFK( true );
		txtCodBancoBaixa.setNomeCampo( "CodBanco" );

		lcPagBaixa.add( new GuardaCampo( txtCodPagBaixa, "CodPag", "Cód.pag", ListaCampos.DB_PK, false ) );
		lcPagBaixa.add( new GuardaCampo( txtCodCompraBaixa, "CodCompra", "Cód.compra", ListaCampos.DB_FK, false ) );
		lcPagBaixa.add( new GuardaCampo( txtDoc, "DocPag", "Doc.", ListaCampos.DB_SI, false ) );
		lcPagBaixa.add( new GuardaCampo( txtTotPagBaixa, "VlrPag", "Total pag.", ListaCampos.DB_SI, false ) );
		lcPagBaixa.add( new GuardaCampo( txtCodForBaixa, "CodFor", "Cód.for", ListaCampos.DB_FK, false ) );
		lcPagBaixa.add( new GuardaCampo( txtDtEmisBaixa, "DataPag", "Data emis.", ListaCampos.DB_SI, false ) );
		lcPagBaixa.add( new GuardaCampo( txtCodBancoBaixa, "CodBanco", "Cód.banco", ListaCampos.DB_FK, false ) );
		lcPagBaixa.add( new GuardaCampo( txtTotAbertoBaixa, "VlrApagPag", "Total aberto", ListaCampos.DB_SI, false ) );
		lcPagBaixa.add( new GuardaCampo( txtTotPagoBaixa, "VlrPagoPag", "Total pago", ListaCampos.DB_SI, false ) );
		lcPagBaixa.add( new GuardaCampo( txtJurosBaixa, "VlrJurosPag", "Total juros", ListaCampos.DB_SI, false ) );
		lcPagBaixa.montaSql( false, "PAGAR", "FN" );
		lcPagBaixa.setQueryCommit( false );
		lcPagBaixa.setReadOnly( true );

		txtCodPagBaixa.setTabelaExterna( lcPagBaixa, null );
		txtCodPagBaixa.setFK( true );
		txtCodPagBaixa.setNomeCampo( "CodPag" );
		txtDoc.setAtivo( false );
		txtCodCompraBaixa.setAtivo( false );
		txtSerie.setAtivo( false );
		txtCodForBaixa.setAtivo( false );
		txtDtEmisBaixa.setAtivo( false );
		txtCodBancoBaixa.setAtivo( false );
		txtTotPagBaixa.setAtivo( false );
		txtTotAbertoBaixa.setAtivo( false );
		txtTotPagoBaixa.setAtivo( false );
		txtJurosBaixa.setAtivo( false );

		Funcoes.setBordReq( txtCodPagBaixa );

		tpn.addTab( "Baixa", pnBaixa );

		btCarregaBaixas.setToolTipText( "Carrega Baixas" );
		btBaixa.setToolTipText( "Baixa" );

		pnBaixa.add( pinBaixa, BorderLayout.NORTH );
		pnTabBaixa.add( pinBotoesBaixa, BorderLayout.EAST );
		pnTabBaixa.add( spnBaixa, BorderLayout.CENTER );
		pnBaixa.add( pnTabBaixa, BorderLayout.CENTER );

		pinBaixa.adic( new JLabelPad( "Cód.pag" ), 7, 0, 80, 20 );
		pinBaixa.adic( txtCodPagBaixa, 7, 20, 80, 20 );
		pinBaixa.adic( new JLabelPad( "Doc." ), 90, 0, 77, 20 );
		pinBaixa.adic( txtDoc, 90, 20, 77, 20 );
		pinBaixa.adic( new JLabelPad( " -" ), 170, 20, 7, 20 );
		pinBaixa.adic( new JLabelPad( "Série" ), 180, 0, 50, 20 );
		pinBaixa.adic( txtSerie, 180, 20, 50, 20 );
		pinBaixa.adic( new JLabelPad( "Pedido" ), 240, 0, 77, 20 );
		pinBaixa.adic( txtCodCompraBaixa, 240, 20, 77, 20 );
		pinBaixa.adic( new JLabelPad( "Cód.for." ), 7, 40, 250, 20 );
		pinBaixa.adic( txtCodForBaixa, 7, 60, 80, 20 );
		pinBaixa.adic( new JLabelPad( "Descrição do fornecedor" ), 90, 40, 250, 20 );
		pinBaixa.adic( txtRazForBaixa, 90, 60, 197, 20 );
		pinBaixa.adic( new JLabelPad( "Cód.banco" ), 290, 40, 250, 20 );
		pinBaixa.adic( txtCodBancoBaixa, 290, 60, 67, 20 );
		pinBaixa.adic( new JLabelPad( "Descrição do banco" ), 360, 40, 250, 20 );
		pinBaixa.adic( txtDescBancoBaixa, 360, 60, 150, 20 );
		pinBaixa.adic( new JLabelPad( "Data de emissão" ), 7, 80, 100, 20 );
		pinBaixa.adic( txtDtEmisBaixa, 7, 100, 100, 20 );
		pinBaixa.adic( new JLabelPad( "Total a pagar" ), 110, 80, 97, 20 );
		pinBaixa.adic( txtTotPagBaixa, 110, 100, 97, 20 );
		pinBaixa.adic( new JLabelPad( "Total pago" ), 210, 80, 97, 20 );
		pinBaixa.adic( txtTotPagoBaixa, 210, 100, 97, 20 );
		pinBaixa.adic( new JLabelPad( "Total em aberto" ), 310, 80, 107, 20 );
		pinBaixa.adic( txtTotAbertoBaixa, 310, 100, 107, 20 );
		pinBaixa.adic( new JLabelPad( "Juros" ), 420, 80, 80, 20 );
		pinBaixa.adic( txtJurosBaixa, 420, 100, 90, 20 );

		pinBotoesBaixa.adic( btCarregaBaixas, 5, 10, 30, 30 );
		pinBotoesBaixa.adic( btBaixa, 5, 40, 30, 30 );

		montarGridBaixaPag();
		tabBaixa.adicColuna( "" );
		tabBaixa.adicColuna( "Vencimento" );
		tabBaixa.adicColuna( "Nº de parcelas" );
		tabBaixa.adicColuna( "Doc." );
		tabBaixa.adicColuna( "Pedido" );
		tabBaixa.adicColuna( "Valor da parcela" );
		tabBaixa.adicColuna( "Data pagamento" );
		tabBaixa.adicColuna( "Valor pago" );
		tabBaixa.adicColuna( "Valor desconto" );
		tabBaixa.adicColuna( "Valor juros" );
		tabBaixa.adicColuna( "Valor aberto" );
		tabBaixa.adicColuna( "Valor cancelado" );
		tabBaixa.adicColuna( "N.conta" );
		tabBaixa.adicColuna( "Descrição da conta" );
		tabBaixa.adicColuna( "Cód.planej." );
		tabBaixa.adicColuna( "Descrição do planejamento" );
		tabBaixa.adicColuna( "Cód.c.c." );
		tabBaixa.adicColuna( "Descrição do centro de custo" );
		tabBaixa.adicColuna( "Observação" );
		tabBaixa.adicColuna( "Cód.contr." ); 					// CODCONTR
		tabBaixa.adicColuna( "Descrição do contrato/projeto" ); // DESCCONTR		
		tabBaixa.adicColuna( "Cód.it.contr." ); 				// CODITCONTR
		tabBaixa.adicColuna( "Descrição do item de cont./proj." ); // DESCITCONTR		

		tabBaixa.setTamColuna( 0, enum_tab_baixa.STATUS.ordinal() );
		tabBaixa.setTamColuna( 110, enum_tab_baixa.DTVENCTO.ordinal() );
		tabBaixa.setTamColuna( 120, enum_tab_baixa.NPARC.ordinal() );
		tabBaixa.setTamColuna( 50, enum_tab_baixa.DOC.ordinal() );
		tabBaixa.setTamColuna( 70, enum_tab_baixa.CODCOMPRA.ordinal() );
		tabBaixa.setTamColuna( 140, enum_tab_baixa.VLRPARC.ordinal() );
		tabBaixa.setTamColuna( 110, enum_tab_baixa.DTPAGTO.ordinal() );
		tabBaixa.setTamColuna( 90, enum_tab_baixa.VLRPAGO.ordinal() );
		tabBaixa.setTamColuna( 110, enum_tab_baixa.VLRDESC.ordinal() );
		tabBaixa.setTamColuna( 100, enum_tab_baixa.VLRJUROS.ordinal() );
		tabBaixa.setTamColuna( 100, enum_tab_baixa.VLRAPAG.ordinal() );
		tabBaixa.setTamColuna( 90, enum_tab_baixa.VLRCANC.ordinal() );

		tabBaixa.setTamColuna( 80, enum_tab_baixa.NUMCONTA.ordinal() );
		tabBaixa.setTamColuna( 150, enum_tab_baixa.DESCCONTA.ordinal() );
		tabBaixa.setTamColuna( 100, enum_tab_baixa.CODPLAN.ordinal() );
		tabBaixa.setTamColuna( 150, enum_tab_baixa.DESCPLAN.ordinal() );
		tabBaixa.setTamColuna( 100, enum_tab_baixa.CODCC.ordinal() );
		tabBaixa.setTamColuna( 150, enum_tab_baixa.DESCCC.ordinal() );
		tabBaixa.setTamColuna( 100, enum_tab_baixa.OBS.ordinal() );
		tabBaixa.setTamColuna( 60, 	enum_tab_manut.CODCONTR.ordinal() );
		tabBaixa.setTamColuna( 200, enum_tab_manut.DESCCONTR.ordinal() );		
		tabBaixa.setTamColuna( 60, 	enum_tab_manut.CODITCONTR.ordinal() );
		tabBaixa.setTamColuna( 200, enum_tab_manut.DESCITCONTR.ordinal() );		


		tabBaixa.setRowHeight( 20 );

		// Manutenção

		tpn.addTab( "Manutenção", pnManut );

		lcPagManut.add( new GuardaCampo( txtCodPagManut, "CodPag", "Cód.pag.", ListaCampos.DB_PK, false ) );
		lcPagManut.add( new GuardaCampo( txtDocManut, "DocPag", "Doc.pag.", ListaCampos.DB_SI, false ) );
		lcPagManut.add( new GuardaCampo( txtCodCompraManut, "CodCompra", "Compra", ListaCampos.DB_SI, false ) );
		lcPagManut.add( new GuardaCampo( txtCodForManut2, "CodFor", "Cod.for.", ListaCampos.DB_FK, false ) );
		lcPagManut.add( new GuardaCampo( txtDtEmisManut, "DataPag", "Data emissão", ListaCampos.DB_SI, false ) );
		lcPagManut.montaSql( false, "PAGAR", "FN" );
		lcPagManut.setQueryCommit( false );
		lcPagManut.setReadOnly( true );
		txtCodPagManut.setTabelaExterna( lcPagManut, null );
		txtCodPagManut.setFK( true );
		txtCodPagManut.setNomeCampo( "CodPag" );

		btCarregaBaixasMan.setToolTipText( "Carrega Baixas" );
		btBaixaManut.setToolTipText( "Baixa" );
		btEditManut.setToolTipText( "Editar" );
		btNovoManut.setToolTipText( "Novo" );
		btExcluirManut.setToolTipText( "Excluir" );
		btCancItem.setToolTipText( "Cancela Item" );
		btExecManut.setToolTipText( "Listar" );

		pnManut.add( pinManut, BorderLayout.NORTH );
		pnTabManut.add( pinBotoesManut, BorderLayout.EAST );
		pnTabManut.add( spnManut, BorderLayout.CENTER );
		pnManut.add( pnTabManut, BorderLayout.CENTER );

		txtDatainiManut.setVlrDate( new Date() );
		txtDatafimManut.setVlrDate( new Date() );

		pinManut.adic( new JLabelPad( "Período" ), 7, 0, 200, 20 );
		pinManut.adic( txtDatainiManut, 7, 20, 100, 20 );
		pinManut.adic( new JLabelPad( "até" ), 113, 20, 27, 20 );
		pinManut.adic( txtDatafimManut, 140, 20, 100, 20 );
		pinManut.adic( btExecManut, 776, 20, 30, 64 );

		pinManut.adic( txtCodForManut, 7, 65, 50, 20, "Cód.for." );
		pinManut.adic( txtRazForManut, 60, 65, 180, 20, "Descrição do fornecedor" );

		JLabelPad separacao = new JLabelPad();
		separacao.setBorder( BorderFactory.createEtchedBorder() );
		pinManut.adic( separacao, 7, 95, 800, 2 );

		pinManut.adic( txtCodPagManut		, 7		, 120	, 70	, 20, "Cod.pag." );
		pinManut.adic( txtDocManut			, 80	, 120	, 70	, 20, "Documento" );
		pinManut.adic( txtCodCompraManut	, 153	, 120	, 70	, 20, "Pedido" );
		pinManut.adic( txtCodForManut2		, 226	, 120	, 60	, 20, "Cód.for." );
		pinManut.adic( txtRazForManut2		, 289	, 120	, 200	, 20, "Razão social do fornecedor" );
		pinManut.adic( txtDtEmisManut		, 492	, 120	, 90	, 20, "Data emissão" );
		pinManut.adic( txtNumCheque			, 585	, 120	, 80	, 20, "Nro.Cheque" );
		pinManut.adic( txtCodOrdCp			, 668	, 120	, 80	, 20, "Ord.Compra" );
		pinManut.adic( txtCodSinal			, 751	, 120	, 60	, 20, "Cor" );

		vValsData.addElement( "V" );
		vValsData.addElement( "E" );
		vLabsData.addElement( "Vencimento" );
		vLabsData.addElement( "Emissão" );

		rgData = new JRadioGroup<String, String>( 2, 1, vLabsData, vValsData );
		rgData.setVlrString( "V" );
		pinManut.adic( new JLabelPad( "Filtrar por:" ), 247, 0, 115, 20 );
		pinManut.adic( rgData, 247, 20, 115, 65 );

		vValsVenc.addElement( "VE" );
		vValsVenc.addElement( "AV" );
		vValsVenc.addElement( "TT" );
		vLabsVenc.addElement( "Vencidas" );
		vLabsVenc.addElement( "À vencer" );
		vLabsVenc.addElement( "Ambas" );

		pinLbFiltroStatus.adic( lbFiltroStatus, 0, 0, 350, 15 );
		pinLbFiltroStatus.tiraBorda();

		pinManut.adic( pinLbFiltroStatus, 488, 3, 80, 15 );
		pinManut.adic( pinFiltroStatus, 488, 20, 280, 65 );

		pinFiltroStatus.adic( cbAPagar		, 5		, 5		, 80	, 20 );
		pinFiltroStatus.adic( cbPagas		, 5		, 30	, 80	, 20 );
		pinFiltroStatus.adic( cbPagParcial	, 85	, 5		, 80	, 20 );
		pinFiltroStatus.adic( cbCanceladas	, 85	, 30	, 100	, 20 );
		pinFiltroStatus.adic( cbEmpenhos	, 175	, 5		, 100	, 20 );

		rgVenc = new JRadioGroup<String, String>( 3, 2, vLabsVenc, vValsVenc );
		rgVenc.setVlrString( "TT" );
		pinManut.adic( new JLabelPad( "Filtrar por:" ), 365, 0, 150, 20 );
		pinManut.adic( rgVenc, 365, 20, 115, 65 );

		lcForManut.add( new GuardaCampo( txtCodForManut, "CodFor", "Cód.for.", ListaCampos.DB_PK, false ) );
		lcForManut.add( new GuardaCampo( txtRazForManut, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );
		lcForManut.add( new GuardaCampo( txtCNPJForManut, "CnpjFor", "CNPJ", ListaCampos.DB_SI, false ) );

		lcForManut.montaSql( false, "FORNECED", "CP" );
		lcForManut.setQueryCommit( false );
		lcForManut.setReadOnly( true );

		txtCodForManut.setTabelaExterna( lcForManut, null );
		txtCodForManut.setFK( true );
		txtCodForManut.setNomeCampo( "CodFor" );

		lcSinal.add( new GuardaCampo( txtCodSinal, "CodSinal", "Sinal", ListaCampos.DB_PK, false ) );
		lcSinal.add( new GuardaCampo( txtDescSinal, "DescSinal", "Descrição do Sinal", ListaCampos.DB_SI, false ) );
		lcSinal.add( new GuardaCampo( txtCorSinal, "CorSinal", "Cor", ListaCampos.DB_SI, false ) );

		lcSinal.montaSql( false, "SINAL", "FN" );
		lcSinal.setQueryCommit( false );
		lcSinal.setReadOnly( true );

		txtCodSinal.setTabelaExterna( lcSinal, null );
		txtCodSinal.setFK( true );
		txtCodSinal.setNomeCampo( "CodSinal" );


		lcForManut2.add( new GuardaCampo( txtCodForManut2, "CodFor", "Cód.for.", ListaCampos.DB_PK, false ) );
		lcForManut2.add( new GuardaCampo( txtRazForManut2, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );

		lcForManut2.montaSql( false, "FORNECED", "CP" );
		lcForManut2.setQueryCommit( false );
		lcForManut2.setReadOnly( true );

		txtCodForManut2.setTabelaExterna( lcForManut2, null );
		txtCodForManut2.setFK( true );
		txtCodForManut2.setNomeCampo( "CodFor" );

		lcOrdCompra.add( new GuardaCampo( txtCodOrdCp, "CodOrdCp", "Cód.O.C", ListaCampos.DB_PK, false ) );
		lcOrdCompra.add( new GuardaCampo( txtCodForOC, "CodFor", "Código do fornecedor", ListaCampos.DB_SI, false ) );
		lcOrdCompra.add( new GuardaCampo( txtDtApOrdCP, "DtApOrdCP", "Data de aprovação", ListaCampos.DB_SI, false ) );
		lcOrdCompra.add( new GuardaCampo( txtObsOrdCP, "ObsOrdCp", "Observações", ListaCampos.DB_SI, false ) );

		lcOrdCompra.montaSql( false, "ORDCOMPRA", "CP" );
		lcOrdCompra.setQueryCommit( false );
		lcOrdCompra.setReadOnly( true );

		txtCodOrdCp.setTabelaExterna( lcOrdCompra, null );
		txtCodOrdCp.setFK( true );
		txtCodOrdCp.setNomeCampo( "CodOrdCp" );

		pinBotoesManut.adic( btCarregaBaixasMan, 5, 10, 30, 30 );
		pinBotoesManut.adic( btBaixaManut, 5, 40, 30, 30 );
		pinBotoesManut.adic( btEditManut, 5, 70, 30, 30 );
		pinBotoesManut.adic( btNovoManut, 5, 100, 30, 30 );
		pinBotoesManut.adic( btEstManut, 5, 130, 30, 30 );
		pinBotoesManut.adic( btExcluirManut, 5, 160, 30, 30 );
		pinBotoesManut.adic( btCancItem, 5, 190, 30, 30 );
		pinBotoesManut.adic( btImpRec, 5, 220, 30, 30 );		
		
		montarGridManutPag();
		tabManut.adicColuna( "Sel." );
		tabManut.adicColuna( "" ); 								// STATUS
		tabManut.adicColuna( "Vencto." ); 						// DTVENCITPAG
		tabManut.adicColuna( "St" ); 							// STATUSITPAG
		tabManut.adicColuna( "Cd.for." ); 						// CODFOR
		tabManut.adicColuna( "Razão social do fornecedor" ); 	// RAZFOR
		tabManut.adicColuna( "Observações" ); 					// OBSITPAG
		tabManut.adicColuna( "Cód.pg." ); 						// CODPAG
		tabManut.adicColuna( "Cheques" ); 						// CHEQUES
		tabManut.adicColuna( "Parc." ); 						// NPARCPAG
		tabManut.adicColuna( "Doc.lanc" ); 						// DOC
		tabManut.adicColuna( "Compra" ); 						// DOCCOMPRA
		tabManut.adicColuna( "Ord.Cp." ); 						// Codigo da ordem de compra
		tabManut.adicColuna( "Valor" ); 						// VLRPARCITPAG
		tabManut.adicColuna( "Dt.pagto." ); 					// DTPAGOITPAG
		tabManut.adicColuna( "Pago" ); 							// VLRPAGOITPAG
		tabManut.adicColuna( "Desconto" ); 						// VLRDESCITPAG
		tabManut.adicColuna( "Juros" );							// VLRJUROSITPAG
		tabManut.adicColuna( "Devolução" ); 					// VLRDEVITPAG
		tabManut.adicColuna( "Adicional" ); 					// VLRADICITPAG
		tabManut.adicColuna( "Aberto" ); 						// VLRAPAGITPAG
		tabManut.adicColuna( "Cancelado" ); 					// VLRCANCITPAG
		tabManut.adicColuna( "Conta" ); 						// NUMCONTA
		tabManut.adicColuna( "Categoria" ); 					// DESCPLAN
		tabManut.adicColuna( "Centro de custo" ); 				// DESCC
		tabManut.adicColuna( "Tp.Cob." ); 						// CODTIPOCOB
		tabManut.adicColuna( "Descrição do tipo de cobrança" ); // DESCTIPOCOB		
		tabManut.adicColuna( "Cod.Compra" ); 					// CODCOMPRA
		tabManut.adicColuna( "Cod.Plan" ); 						// CODPLAN
		tabManut.adicColuna( "Cod.CC" ); 						// CODCC
		tabManut.adicColuna( "Dt.It.Pag" ); 					// DTITPAG
		tabManut.adicColuna( "Cód.contr." ); 					// CODCONTR
		tabManut.adicColuna( "Descrição do contrato/projeto" ); // DESCCONTR		
		tabManut.adicColuna( "Cód.it.contr." ); 				// CODITCONTR
		tabManut.adicColuna( "Descrição do item de cont./proj." ); // DESCITCONTR		

		tabManut.setColunaEditavel( enum_tab_manut.SEL.ordinal(), true );

		tabManut.setTamColuna( 20, enum_tab_manut.SEL.ordinal() );
		tabManut.setTamColuna( 0,	enum_tab_manut.IMGSTATUS.ordinal()  );			
		tabManut.setTamColuna( 60, 	enum_tab_manut.DTVENCITPAG.ordinal() );	
		tabManut.setTamColuna( 20, 	enum_tab_manut.STATUSITPAG.ordinal() );			
		tabManut.setTamColuna( 45, 	enum_tab_manut.CODFOR.ordinal() );
		tabManut.setTamColuna( 150, enum_tab_manut.RAZFOR.ordinal() );
		tabManut.setTamColuna( 150, enum_tab_manut.OBSITPAG.ordinal() );
		tabManut.setTamColuna( 50, 	enum_tab_manut.CODPAG.ordinal() );
		tabManut.setTamColuna( 55, 	enum_tab_manut.CHEQUES.ordinal() );		
		tabManut.setTamColuna( 35,	enum_tab_manut.NPARCPAG.ordinal() );
		tabManut.setTamColuna( 50, 	enum_tab_manut.DOC.ordinal() );
		tabManut.setTamColuna( 50, 	enum_tab_manut.DOCCOMPRA.ordinal() ); 
		tabManut.setTamColuna( 50, 	enum_tab_manut.CODORDCP.ordinal() ); 
		tabManut.setTamColuna( 80, enum_tab_manut.VLRPARCITPAG.ordinal() );
		tabManut.setTamColuna( 60, enum_tab_manut.DTPAGOITPAG.ordinal() );
		tabManut.setTamColuna( 60, enum_tab_manut.VLRPAGOITPAG.ordinal() );
		tabManut.setTamColuna( 60, enum_tab_manut.VLRDESCITPAG.ordinal() );
		tabManut.setTamColuna( 60, enum_tab_manut.VLRJUROSITPAG.ordinal() );
		tabManut.setTamColuna( 60, enum_tab_manut.VLRDEVITPAG.ordinal() );
		tabManut.setTamColuna( 60, enum_tab_manut.VLRADICITPAG.ordinal() );
		tabManut.setTamColuna( 60, enum_tab_manut.VLRAPAGITPAG.ordinal() );
		tabManut.setTamColuna( 60, enum_tab_manut.VLRCANCITPAG.ordinal() );
		tabManut.setTamColuna( 60, enum_tab_manut.NUMCONTA.ordinal() );
		tabManut.setTamColuna( 150, enum_tab_manut.DESCPLAN.ordinal() );
		tabManut.setTamColuna( 150, enum_tab_manut.DESCCC.ordinal() );
		tabManut.setTamColuna( 60, 	enum_tab_manut.CODTIPOCOB.ordinal() );
		tabManut.setTamColuna( 150, enum_tab_manut.DESCTIPOCOB.ordinal() );		
		tabManut.setTamColuna( 50, enum_tab_manut.CODCOMPRA.ordinal() );
		tabManut.setTamColuna( 150, enum_tab_manut.CODPLAN.ordinal() );
		tabManut.setTamColuna( 150, enum_tab_manut.CODCC.ordinal() );
		tabManut.setTamColuna( 60, enum_tab_manut.DTITPAG.ordinal() );
		tabManut.setTamColuna( 60, 	enum_tab_manut.CODCONTR.ordinal() );
		tabManut.setTamColuna( 150, enum_tab_manut.DESCCONTR.ordinal() );		
		tabManut.setTamColuna( 60, 	enum_tab_manut.CODITCONTR.ordinal() );
		tabManut.setTamColuna( 150, enum_tab_manut.DESCITCONTR.ordinal() );		

		tabManut.setRowHeight( 20 );
		carregarListeners();*/
	}
	
	private void montarListaCampos() {
		lcFor.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.for.", ListaCampos.DB_PK, false ) );
		lcFor.add( new GuardaCampo( txtRazFor, "RazFor", "Descrição do fornecedor", ListaCampos.DB_SI, false ) );
		lcFor.add( new GuardaCampo( txtCNPJFor, "CnpjFor", "CNPJ", ListaCampos.DB_SI, false ) );
		lcFor.montaSql( false, "FORNECED", "CP" );
		lcFor.setQueryCommit( false );
		lcFor.setReadOnly( true );
		txtCodFor.setTabelaExterna( lcFor, null );
		txtCodFor.setFK( true );
		txtCodFor.setNomeCampo( "CodFor" );
		
		lcCompraBaixa.add( new GuardaCampo( txtCodCompraBaixa, "CodCompra", "Cód.compra", ListaCampos.DB_PK, false ) );
		lcCompraBaixa.add( new GuardaCampo( txtSerie, "Serie", "Série", ListaCampos.DB_SI, false ) );
		lcCompraBaixa.montaSql( false, "COMPRA", "CP" );
		lcCompraBaixa.setQueryCommit( false );
		lcCompraBaixa.setReadOnly( true );
		txtCodCompraBaixa.setTabelaExterna( lcCompraBaixa, null );
		txtCodCompraBaixa.setFK( true );
		txtCodCompraBaixa.setNomeCampo( "CodCompra" );

		lcForBaixa.add( new GuardaCampo( txtCodForBaixa, "CodFor", "Cód.for", ListaCampos.DB_PK, false ) );
		lcForBaixa.add( new GuardaCampo( txtRazForBaixa, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );
		lcForBaixa.add( new GuardaCampo( txtCNPJForBaixa, "CnpjFor", "CNPJ", ListaCampos.DB_SI, false ) );
		lcForBaixa.montaSql( false, "FORNECED", "CP" );
		lcForBaixa.setQueryCommit( false );
		lcForBaixa.setReadOnly( true );
		txtCodForBaixa.setTabelaExterna( lcForBaixa, null );
		txtCodForBaixa.setFK( true );
		txtCodForBaixa.setNomeCampo( "CodFor" );

		lcBancoBaixa.add( new GuardaCampo( txtCodBancoBaixa, "CodBanco", "Cód.banco", ListaCampos.DB_PK, false ) );
		lcBancoBaixa.add( new GuardaCampo( txtDescBancoBaixa, "NomeBanco", "Nome banco", ListaCampos.DB_SI, false ) );
		lcBancoBaixa.montaSql( false, "BANCO", "FN" );
		lcBancoBaixa.setQueryCommit( false );
		lcBancoBaixa.setReadOnly( true );
		txtCodBancoBaixa.setTabelaExterna( lcBancoBaixa, null );
		txtCodBancoBaixa.setFK( true );
		txtCodBancoBaixa.setNomeCampo( "CodBanco" );

		lcPagBaixa.add( new GuardaCampo( txtCodPagBaixa, "CodPag", "Cód.pag", ListaCampos.DB_PK, false ) );
		lcPagBaixa.add( new GuardaCampo( txtCodCompraBaixa, "CodCompra", "Cód.compra", ListaCampos.DB_FK, false ) );
		lcPagBaixa.add( new GuardaCampo( txtDoc, "DocPag", "Doc.", ListaCampos.DB_SI, false ) );
		lcPagBaixa.add( new GuardaCampo( txtTotPagBaixa, "VlrPag", "Total pag.", ListaCampos.DB_SI, false ) );
		lcPagBaixa.add( new GuardaCampo( txtCodForBaixa, "CodFor", "Cód.for", ListaCampos.DB_FK, false ) );
		lcPagBaixa.add( new GuardaCampo( txtDtEmisBaixa, "DataPag", "Data emis.", ListaCampos.DB_SI, false ) );
		lcPagBaixa.add( new GuardaCampo( txtCodBancoBaixa, "CodBanco", "Cód.banco", ListaCampos.DB_FK, false ) );
		lcPagBaixa.add( new GuardaCampo( txtTotAbertoBaixa, "VlrApagPag", "Total aberto", ListaCampos.DB_SI, false ) );
		lcPagBaixa.add( new GuardaCampo( txtTotPagoBaixa, "VlrPagoPag", "Total pago", ListaCampos.DB_SI, false ) );
		lcPagBaixa.add( new GuardaCampo( txtJurosBaixa, "VlrJurosPag", "Total juros", ListaCampos.DB_SI, false ) );
		lcPagBaixa.montaSql( false, "PAGAR", "FN" );
		lcPagBaixa.setQueryCommit( false );
		lcPagBaixa.setReadOnly( true );
		txtCodPagBaixa.setTabelaExterna( lcPagBaixa, null );
		txtCodPagBaixa.setFK( true );
		txtCodPagBaixa.setNomeCampo( "CodPag" );
		
		// manutenção
		
		lcPagManut.add( new GuardaCampo( txtCodPagManut, "CodPag", "Cód.pag.", ListaCampos.DB_PK, false ) );
		lcPagManut.add( new GuardaCampo( txtDocManut, "DocPag", "Doc.pag.", ListaCampos.DB_SI, false ) );
		lcPagManut.add( new GuardaCampo( txtCodCompraManut, "CodCompra", "Compra", ListaCampos.DB_SI, false ) );
		lcPagManut.add( new GuardaCampo( txtCodForManut2, "CodFor", "Cod.for.", ListaCampos.DB_FK, false ) );
		lcPagManut.add( new GuardaCampo( txtDtEmisManut, "DataPag", "Data emissão", ListaCampos.DB_SI, false ) );
		lcPagManut.montaSql( false, "PAGAR", "FN" );
		lcPagManut.setQueryCommit( false );
		lcPagManut.setReadOnly( true );
		txtCodPagManut.setTabelaExterna( lcPagManut, null );
		txtCodPagManut.setFK( true );
		txtCodPagManut.setNomeCampo( "CodPag" );
		
		lcForManut.add( new GuardaCampo( txtCodForManut, "CodFor", "Cód.for.", ListaCampos.DB_PK, false ) );
		lcForManut.add( new GuardaCampo( txtRazForManut, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );
		lcForManut.add( new GuardaCampo( txtCNPJForManut, "CnpjFor", "CNPJ", ListaCampos.DB_SI, false ) );
		lcForManut.montaSql( false, "FORNECED", "CP" );
		lcForManut.setQueryCommit( false );
		lcForManut.setReadOnly( true );
		txtCodForManut.setTabelaExterna( lcForManut, null );
		txtCodForManut.setFK( true );
		txtCodForManut.setNomeCampo( "CodFor" );
		
		lcForManut2.add( new GuardaCampo( txtCodForManut2, "CodFor", "Cód.for.", ListaCampos.DB_PK, false ) );
		lcForManut2.add( new GuardaCampo( txtRazForManut2, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );
		lcForManut2.montaSql( false, "FORNECED", "CP" );
		lcForManut2.setQueryCommit( false );
		lcForManut2.setReadOnly( true );
		txtCodForManut2.setTabelaExterna( lcForManut2, null );
		txtCodForManut2.setFK( true );
		txtCodForManut2.setNomeCampo( "CodFor" );

		lcOrdCompra.add( new GuardaCampo( txtCodOrdCp, "CodOrdCp", "Cód.O.C", ListaCampos.DB_PK, false ) );
		lcOrdCompra.add( new GuardaCampo( txtCodForOC, "CodFor", "Código do fornecedor", ListaCampos.DB_SI, false ) );
		lcOrdCompra.add( new GuardaCampo( txtDtApOrdCP, "DtApOrdCP", "Data de aprovação", ListaCampos.DB_SI, false ) );
		lcOrdCompra.add( new GuardaCampo( txtObsOrdCP, "ObsOrdCp", "Observações", ListaCampos.DB_SI, false ) );
		lcOrdCompra.montaSql( false, "ORDCOMPRA", "CP" );
		lcOrdCompra.setQueryCommit( false );
		lcOrdCompra.setReadOnly( true );
		txtCodOrdCp.setTabelaExterna( lcOrdCompra, null );
		txtCodOrdCp.setFK( true );
		txtCodOrdCp.setNomeCampo( "CodOrdCp" );
		
		lcSinal.add( new GuardaCampo( txtCodSinal, "CodSinal", "Sinal", ListaCampos.DB_PK, false ) );
		lcSinal.add( new GuardaCampo( txtDescSinal, "DescSinal", "Descrição do Sinal", ListaCampos.DB_SI, false ) );
		lcSinal.add( new GuardaCampo( txtCorSinal, "CorSinal", "Cor", ListaCampos.DB_SI, false ) );

		lcSinal.montaSql( false, "SINAL", "FN" );
		lcSinal.setQueryCommit( false );
		lcSinal.setReadOnly( true );

		txtCodSinal.setTabelaExterna( lcSinal, null );
		txtCodSinal.setFK( true );
		txtCodSinal.setNomeCampo( "CodSinal" );

	}
	
	private void carregarListeners() {
		lcFor.addCarregaListener( this );
		lcSinal.addCarregaListener( this );
		lcPagManut.addCarregaListener( this );
		lcPagBaixa.addCarregaListener( this );
		btBaixa.addActionListener( this );
		btCarregaBaixas.addActionListener( this );
		btBaixaConsulta.addActionListener( this );
		btCarregaBaixasMan.addActionListener( this );
		btBaixaManut.addActionListener( this );
		btEditManut.addActionListener( this );
		btNovoManut.addActionListener( this );
		btExcluirManut.addActionListener( this );
		btExecManut.addActionListener( this );
		btEstManut.addActionListener( this );
		btCancItem.addActionListener( this );
		tpn.addChangeListener( this );

		tabManut.addMouseListener( this );
		txtDocManut.addKeyListener( this );
		txtCodSinal.addKeyListener( this );
		txtCodCompraManut.addKeyListener( this );
		txtNumCheque.addKeyListener( this );		
	}
	
	
	private void montarGridBaixaPag() {
		tabBaixa.adicColuna( "" );
		tabBaixa.adicColuna( "Vencimento" );
		tabBaixa.adicColuna( "Nº de parcelas" );
		tabBaixa.adicColuna( "Doc." );
		tabBaixa.adicColuna( "Pedido" );
		tabBaixa.adicColuna( "Valor da parcela" );
		tabBaixa.adicColuna( "Data pagamento" );
		tabBaixa.adicColuna( "Valor pago" );
		tabBaixa.adicColuna( "Valor desconto" );
		tabBaixa.adicColuna( "Valor juros" );
		tabBaixa.adicColuna( "Valor aberto" );
		tabBaixa.adicColuna( "Valor cancelado" );
		tabBaixa.adicColuna( "N.conta" );
		tabBaixa.adicColuna( "Descrição da conta" );
		tabBaixa.adicColuna( "Cód.planej." );
		tabBaixa.adicColuna( "Descrição do planejamento" );
		tabBaixa.adicColuna( "Cód.c.c." );
		tabBaixa.adicColuna( "Descrição do centro de custo" );
		tabBaixa.adicColuna( "Observação" );
		tabBaixa.adicColuna( "Cód.contr." ); 					// CODCONTR
		tabBaixa.adicColuna( "Descrição do contrato/projeto" ); // DESCCONTR		
		tabBaixa.adicColuna( "Cód.it.contr." ); 				// CODITCONTR
		tabBaixa.adicColuna( "Descrição do item de cont./proj." ); // DESCITCONTR		

		tabBaixa.setTamColuna( 0, enum_tab_baixa.STATUS.ordinal() );
		tabBaixa.setTamColuna( 110, enum_tab_baixa.DTVENCTO.ordinal() );
		tabBaixa.setTamColuna( 120, enum_tab_baixa.NPARC.ordinal() );
		tabBaixa.setTamColuna( 50, enum_tab_baixa.DOC.ordinal() );
		tabBaixa.setTamColuna( 70, enum_tab_baixa.CODCOMPRA.ordinal() );
		tabBaixa.setTamColuna( 140, enum_tab_baixa.VLRPARC.ordinal() );
		tabBaixa.setTamColuna( 110, enum_tab_baixa.DTPAGTO.ordinal() );
		tabBaixa.setTamColuna( 90, enum_tab_baixa.VLRPAGO.ordinal() );
		tabBaixa.setTamColuna( 110, enum_tab_baixa.VLRDESC.ordinal() );
		tabBaixa.setTamColuna( 100, enum_tab_baixa.VLRJUROS.ordinal() );
		tabBaixa.setTamColuna( 100, enum_tab_baixa.VLRAPAG.ordinal() );
		tabBaixa.setTamColuna( 90, enum_tab_baixa.VLRCANC.ordinal() );

		tabBaixa.setTamColuna( 80, enum_tab_baixa.NUMCONTA.ordinal() );
		tabBaixa.setTamColuna( 150, enum_tab_baixa.DESCCONTA.ordinal() );
		tabBaixa.setTamColuna( 100, enum_tab_baixa.CODPLAN.ordinal() );
		tabBaixa.setTamColuna( 150, enum_tab_baixa.DESCPLAN.ordinal() );
		tabBaixa.setTamColuna( 100, enum_tab_baixa.CODCC.ordinal() );
		tabBaixa.setTamColuna( 150, enum_tab_baixa.DESCCC.ordinal() );
		tabBaixa.setTamColuna( 100, enum_tab_baixa.OBS.ordinal() );
		tabBaixa.setTamColuna( 60, 	enum_tab_manut.CODCONTR.ordinal() );
		tabBaixa.setTamColuna( 200, enum_tab_manut.DESCCONTR.ordinal() );		
		tabBaixa.setTamColuna( 60, 	enum_tab_manut.CODITCONTR.ordinal() );
		tabBaixa.setTamColuna( 200, enum_tab_manut.DESCITCONTR.ordinal() );		


		tabBaixa.setRowHeight( 20 );
	}
	
	private void montarGridConsultaPag() {
		tabConsulta.adicColuna( "" );
		tabConsulta.adicColuna( "Vencimento" );
		tabConsulta.adicColuna( "Série" );
		tabConsulta.adicColuna( "Doc." );
		tabConsulta.adicColuna( "Cód. compra" );
		tabConsulta.adicColuna( "Data da compra" );
		tabConsulta.adicColuna( "Valor" );
		tabConsulta.adicColuna( "Data pagamento" );
		tabConsulta.adicColuna( "Valor pago" );
		tabConsulta.adicColuna( "Atraso" );
		tabConsulta.adicColuna( "Observações" );
		tabConsulta.adicColuna( "Banco" );
		tabConsulta.adicColuna( "Valor canc." );

		tabConsulta.setTamColuna( 0, 0 );
		tabConsulta.setTamColuna( 90, 1 );
		tabConsulta.setTamColuna( 50, 2 );
		tabConsulta.setTamColuna( 50, 3 );
		tabConsulta.setTamColuna( 90, 4 );
		tabConsulta.setTamColuna( 110, 5 );
		tabConsulta.setTamColuna( 90, 6 );
		tabConsulta.setTamColuna( 110, 7 );
		tabConsulta.setTamColuna( 100, 8 );
		tabConsulta.setTamColuna( 60, 9 );
		tabConsulta.setTamColuna( 100, 10 );
		tabConsulta.setTamColuna( 80, 11 );
		tabConsulta.setTamColuna( 90, 12 );

		tabConsulta.setRowHeight( 20 );
	}
	
	private void montarGridManutPag() {
		tabManut.adicColuna( "Sel." );
		tabManut.adicColuna( "" ); 								// STATUS
		tabManut.adicColuna( "Vencto." ); 						// DTVENCITPAG
		tabManut.adicColuna( "St" ); 							// STATUSITPAG
		tabManut.adicColuna( "Cd.for." ); 						// CODFOR
		tabManut.adicColuna( "Razão social do fornecedor" ); 	// RAZFOR
		tabManut.adicColuna( "Observações" ); 					// OBSITPAG
		tabManut.adicColuna( "Cód.pg." ); 						// CODPAG
		tabManut.adicColuna( "Cheques" ); 						// CHEQUES
		tabManut.adicColuna( "Parc." ); 						// NPARCPAG
		tabManut.adicColuna( "Doc.lanc" ); 						// DOC
		tabManut.adicColuna( "Compra" ); 						// DOCCOMPRA
		tabManut.adicColuna( "Ord.Cp." ); 						// Codigo da ordem de compra
		tabManut.adicColuna( "Valor" ); 						// VLRPARCITPAG
		tabManut.adicColuna( "Dt.pagto." ); 					// DTPAGOITPAG
		tabManut.adicColuna( "Pago" ); 							// VLRPAGOITPAG
		tabManut.adicColuna( "Desconto" ); 						// VLRDESCITPAG
		tabManut.adicColuna( "Juros" );							// VLRJUROSITPAG
		tabManut.adicColuna( "Devolução" ); 					// VLRDEVITPAG
		tabManut.adicColuna( "Adicional" ); 					// VLRADICITPAG
		tabManut.adicColuna( "Aberto" ); 						// VLRAPAGITPAG
		tabManut.adicColuna( "Cancelado" ); 					// VLRCANCITPAG
		tabManut.adicColuna( "Conta" ); 						// NUMCONTA
		tabManut.adicColuna( "Categoria" ); 					// DESCPLAN
		tabManut.adicColuna( "Centro de custo" ); 				// DESCC
		tabManut.adicColuna( "Tp.Cob." ); 						// CODTIPOCOB
		tabManut.adicColuna( "Descrição do tipo de cobrança" ); // DESCTIPOCOB		
		tabManut.adicColuna( "Cod.Compra" ); 					// CODCOMPRA
		tabManut.adicColuna( "Cod.Plan" ); 						// CODPLAN
		tabManut.adicColuna( "Cod.CC" ); 						// CODCC
		tabManut.adicColuna( "Dt.It.Pag" ); 					// DTITPAG
		tabManut.adicColuna( "Cód.contr." ); 					// CODCONTR
		tabManut.adicColuna( "Descrição do contrato/projeto" ); // DESCCONTR		
		tabManut.adicColuna( "Cód.it.contr." ); 				// CODITCONTR
		tabManut.adicColuna( "Descrição do item de cont./proj." ); // DESCITCONTR		

		tabManut.setColunaEditavel( enum_tab_manut.SEL.ordinal(), true );

		tabManut.setTamColuna( 20, enum_tab_manut.SEL.ordinal() );
		tabManut.setTamColuna( 0,	enum_tab_manut.IMGSTATUS.ordinal()  );			
		tabManut.setTamColuna( 60, 	enum_tab_manut.DTVENCITPAG.ordinal() );	
		tabManut.setTamColuna( 20, 	enum_tab_manut.STATUSITPAG.ordinal() );			
		tabManut.setTamColuna( 45, 	enum_tab_manut.CODFOR.ordinal() );
		tabManut.setTamColuna( 150, enum_tab_manut.RAZFOR.ordinal() );
		tabManut.setTamColuna( 150, enum_tab_manut.OBSITPAG.ordinal() );
		tabManut.setTamColuna( 50, 	enum_tab_manut.CODPAG.ordinal() );
		tabManut.setTamColuna( 55, 	enum_tab_manut.CHEQUES.ordinal() );		
		tabManut.setTamColuna( 35,	enum_tab_manut.NPARCPAG.ordinal() );
		tabManut.setTamColuna( 50, 	enum_tab_manut.DOC.ordinal() );
		tabManut.setTamColuna( 50, 	enum_tab_manut.DOCCOMPRA.ordinal() ); 
		tabManut.setTamColuna( 50, 	enum_tab_manut.CODORDCP.ordinal() ); 
		tabManut.setTamColuna( 80, enum_tab_manut.VLRPARCITPAG.ordinal() );
		tabManut.setTamColuna( 60, enum_tab_manut.DTPAGOITPAG.ordinal() );
		tabManut.setTamColuna( 60, enum_tab_manut.VLRPAGOITPAG.ordinal() );
		tabManut.setTamColuna( 60, enum_tab_manut.VLRDESCITPAG.ordinal() );
		tabManut.setTamColuna( 60, enum_tab_manut.VLRJUROSITPAG.ordinal() );
		tabManut.setTamColuna( 60, enum_tab_manut.VLRDEVITPAG.ordinal() );
		tabManut.setTamColuna( 60, enum_tab_manut.VLRADICITPAG.ordinal() );
		tabManut.setTamColuna( 60, enum_tab_manut.VLRAPAGITPAG.ordinal() );
		tabManut.setTamColuna( 60, enum_tab_manut.VLRCANCITPAG.ordinal() );
		tabManut.setTamColuna( 60, enum_tab_manut.NUMCONTA.ordinal() );
		tabManut.setTamColuna( 150, enum_tab_manut.DESCPLAN.ordinal() );
		tabManut.setTamColuna( 150, enum_tab_manut.DESCCC.ordinal() );
		tabManut.setTamColuna( 60, 	enum_tab_manut.CODTIPOCOB.ordinal() );
		tabManut.setTamColuna( 150, enum_tab_manut.DESCTIPOCOB.ordinal() );		
		tabManut.setTamColuna( 50, enum_tab_manut.CODCOMPRA.ordinal() );
		tabManut.setTamColuna( 150, enum_tab_manut.CODPLAN.ordinal() );
		tabManut.setTamColuna( 150, enum_tab_manut.CODCC.ordinal() );
		tabManut.setTamColuna( 60, enum_tab_manut.DTITPAG.ordinal() );
		tabManut.setTamColuna( 60, 	enum_tab_manut.CODCONTR.ordinal() );
		tabManut.setTamColuna( 150, enum_tab_manut.DESCCONTR.ordinal() );		
		tabManut.setTamColuna( 60, 	enum_tab_manut.CODITCONTR.ordinal() );
		tabManut.setTamColuna( 150, enum_tab_manut.DESCITCONTR.ordinal() );		

		tabManut.setRowHeight( 20 );
	}
	
	private void montarTela() {
		
		cbAPagar.setVlrString( "S" );
		cbPagParcial.setVlrString( "S" );

		Container c = getContentPane();
		c.setLayout( new BorderLayout() );
		c.add( pnRod, BorderLayout.SOUTH );
		c.add( tpn, BorderLayout.CENTER );

		pnLegenda.setPreferredSize( new Dimension( 840, 50 ) );
		pnLegenda.setLayout( null );

		lbVencido.setBounds( 5, 0, 150, 17 );
		txtTotalVencido.setBounds( 5, 18, 150, 18 );
		lbParcial.setBounds( 160, 0, 150, 17 );
		txtTotalParcial.setBounds( 160, 18, 150, 18 );
		lbPago.setBounds( 315, 0, 150, 17 );
		txtTotalPago.setBounds( 315, 18, 150, 18 );
		lbVencer.setBounds( 5, 36, 150, 17 );
		txtTotalVencer.setBounds( 5, 54, 150, 18 );
		lbCancelado.setBounds( 160, 36, 150, 17 );
		txtTotalCancelado.setBounds( 160, 54, 150, 18 );
		btSair.setBounds( 720, 18, 90, 40 );

		pnLegenda.add( lbVencido );
		pnLegenda.add( txtTotalVencido );
		pnLegenda.add( lbParcial );
		pnLegenda.add( txtTotalParcial );
		pnLegenda.add( lbPago );
		pnLegenda.add( txtTotalPago );
		pnLegenda.add( lbVencer );
		pnLegenda.add( txtTotalVencer );
		pnLegenda.add( lbCancelado );
		pnLegenda.add( txtTotalCancelado );
		pnLegenda.add( btSair );

		txtTotalVencido.setSoLeitura( true );
		txtTotalParcial.setSoLeitura( true );
		txtTotalPago.setSoLeitura( true );
		txtTotalVencer.setSoLeitura( true );
		txtTotalCancelado.setSoLeitura( true );

		txtTotalVencido.setFont( SwingParams.getFontbold() );
		txtTotalParcial.setFont( SwingParams.getFontbold() );
		txtTotalPago.setFont( SwingParams.getFontbold() );
		txtTotalVencer.setFont( SwingParams.getFontbold() );
		txtTotalCancelado.setFont( SwingParams.getFontbold() );

		pnRod.setBorder( BorderFactory.createEtchedBorder() );
		pnRod.setPreferredSize( new Dimension( 600, 80 ) );
		pnRod.add( pnLegenda, BorderLayout.WEST );

		btSair.addActionListener( this );
		btImpRec.addActionListener( this );

		// Consulta:
		txtPrimCompr.setAtivo( false );
		txtUltCompr.setAtivo( false );
		txtDataMaxFat.setAtivo( false );
		txtVlrMaxFat.setAtivo( false );
		txtVlrTotCompr.setAtivo( false );
		txtVlrTotPago.setAtivo( false );
		txtVlrTotAberto.setAtivo( false );
		txtDataMaxAcum.setAtivo( false );
		txtVlrMaxAcum.setAtivo( false );

		Funcoes.setBordReq( txtCodFor );

		tpn.addTab( "Consulta", pnConsulta );

		btBaixaConsulta.setToolTipText( "Baixa" );

		pnConsulta.add( pinConsulta, BorderLayout.NORTH );
		pnTabConsulta.add( pinBotoesConsulta, BorderLayout.EAST );
		pnTabConsulta.add( spnConsulta, BorderLayout.CENTER );
		pnConsulta.add( pnTabConsulta, BorderLayout.CENTER );

		pinConsulta.adic( new JLabelPad( "Cód.for." ), 7, 0, 250, 20 );
		pinConsulta.adic( txtCodFor, 7, 20, 80, 20 );
		pinConsulta.adic( new JLabelPad( "Descrição do fornecedor" ), 90, 0, 250, 20 );
		pinConsulta.adic( txtRazFor, 90, 20, 217, 20 );
		pinConsulta.adic( new JLabelPad( "Primeira compra" ), 310, 0, 98, 20 );
		pinConsulta.adic( txtPrimCompr, 310, 20, 98, 20 );
		pinConsulta.adic( new JLabelPad( "Última compra" ), 411, 0, 97, 20 );
		pinConsulta.adic( txtUltCompr, 411, 20, 100, 20 );
		pinConsulta.adic( new JLabelPad( "Data" ), 7, 40, 98, 20 );
		pinConsulta.adic( txtDataMaxFat, 7, 60, 98, 20 );
		pinConsulta.adic( new JLabelPad( "Valor da maior fatura" ), 108, 40, 139, 20 );
		pinConsulta.adic( txtVlrMaxFat, 108, 60, 139, 20 );
		pinConsulta.adic( new JLabelPad( "Data" ), 250, 40, 98, 20 );
		pinConsulta.adic( txtDataMaxAcum, 250, 60, 120, 20 );
		pinConsulta.adic( new JLabelPad( "Valor do maior acumulo" ), 373, 40, 139, 20 );
		pinConsulta.adic( txtVlrMaxAcum, 373, 60, 139, 20 );
		pinConsulta.adic( new JLabelPad( "Total de compras" ), 7, 80, 150, 20 );
		pinConsulta.adic( txtVlrTotCompr, 7, 100, 165, 20 );
		pinConsulta.adic( new JLabelPad( "Total pago" ), 175, 80, 97, 20 );
		pinConsulta.adic( txtVlrTotPago, 175, 100, 165, 20 );
		pinConsulta.adic( new JLabelPad( "Total em aberto" ), 343, 80, 117, 20 );
		pinConsulta.adic( txtVlrTotAberto, 343, 100, 167, 20 );
		pinBotoesConsulta.adic( btBaixaConsulta, 5, 10, 30, 30 );
		
		txtDoc.setAtivo( false );
		txtCodCompraBaixa.setAtivo( false );
		txtSerie.setAtivo( false );
		txtCodForBaixa.setAtivo( false );
		txtDtEmisBaixa.setAtivo( false );
		txtCodBancoBaixa.setAtivo( false );
		txtTotPagBaixa.setAtivo( false );
		txtTotAbertoBaixa.setAtivo( false );
		txtTotPagoBaixa.setAtivo( false );
		txtJurosBaixa.setAtivo( false );

		Funcoes.setBordReq( txtCodPagBaixa );

		tpn.addTab( "Baixa", pnBaixa );

		btCarregaBaixas.setToolTipText( "Carrega Baixas" );
		btBaixa.setToolTipText( "Baixa" );

		pnBaixa.add( pinBaixa, BorderLayout.NORTH );
		pnTabBaixa.add( pinBotoesBaixa, BorderLayout.EAST );
		pnTabBaixa.add( spnBaixa, BorderLayout.CENTER );
		pnBaixa.add( pnTabBaixa, BorderLayout.CENTER );

		pinBaixa.adic( new JLabelPad( "Cód.pag" ), 7, 0, 80, 20 );
		pinBaixa.adic( txtCodPagBaixa, 7, 20, 80, 20 );
		pinBaixa.adic( new JLabelPad( "Doc." ), 90, 0, 77, 20 );
		pinBaixa.adic( txtDoc, 90, 20, 77, 20 );
		pinBaixa.adic( new JLabelPad( " -" ), 170, 20, 7, 20 );
		pinBaixa.adic( new JLabelPad( "Série" ), 180, 0, 50, 20 );
		pinBaixa.adic( txtSerie, 180, 20, 50, 20 );
		pinBaixa.adic( new JLabelPad( "Pedido" ), 240, 0, 77, 20 );
		pinBaixa.adic( txtCodCompraBaixa, 240, 20, 77, 20 );
		pinBaixa.adic( new JLabelPad( "Cód.for." ), 7, 40, 250, 20 );
		pinBaixa.adic( txtCodForBaixa, 7, 60, 80, 20 );
		pinBaixa.adic( new JLabelPad( "Descrição do fornecedor" ), 90, 40, 250, 20 );
		pinBaixa.adic( txtRazForBaixa, 90, 60, 197, 20 );
		pinBaixa.adic( new JLabelPad( "Cód.banco" ), 290, 40, 250, 20 );
		pinBaixa.adic( txtCodBancoBaixa, 290, 60, 67, 20 );
		pinBaixa.adic( new JLabelPad( "Descrição do banco" ), 360, 40, 250, 20 );
		pinBaixa.adic( txtDescBancoBaixa, 360, 60, 150, 20 );
		pinBaixa.adic( new JLabelPad( "Data de emissão" ), 7, 80, 100, 20 );
		pinBaixa.adic( txtDtEmisBaixa, 7, 100, 100, 20 );
		pinBaixa.adic( new JLabelPad( "Total a pagar" ), 110, 80, 97, 20 );
		pinBaixa.adic( txtTotPagBaixa, 110, 100, 97, 20 );
		pinBaixa.adic( new JLabelPad( "Total pago" ), 210, 80, 97, 20 );
		pinBaixa.adic( txtTotPagoBaixa, 210, 100, 97, 20 );
		pinBaixa.adic( new JLabelPad( "Total em aberto" ), 310, 80, 107, 20 );
		pinBaixa.adic( txtTotAbertoBaixa, 310, 100, 107, 20 );
		pinBaixa.adic( new JLabelPad( "Juros" ), 420, 80, 80, 20 );
		pinBaixa.adic( txtJurosBaixa, 420, 100, 90, 20 );

		pinBotoesBaixa.adic( btCarregaBaixas, 5, 10, 30, 30 );
		pinBotoesBaixa.adic( btBaixa, 5, 40, 30, 30 );

		// Manutenção

		tpn.addTab( "Manutenção", pnManut );

		btCarregaBaixasMan.setToolTipText( "Carrega Baixas" );
		btBaixaManut.setToolTipText( "Baixa" );
		btEditManut.setToolTipText( "Editar" );
		btNovoManut.setToolTipText( "Novo" );
		btExcluirManut.setToolTipText( "Excluir" );
		btCancItem.setToolTipText( "Cancela Item" );
		btExecManut.setToolTipText( "Listar" );

		pnManut.add( pinManut, BorderLayout.NORTH );
		pnTabManut.add( pinBotoesManut, BorderLayout.EAST );
		pnTabManut.add( spnManut, BorderLayout.CENTER );
		pnManut.add( pnTabManut, BorderLayout.CENTER );

		txtDatainiManut.setVlrDate( new Date() );
		txtDatafimManut.setVlrDate( new Date() );

		pinManut.adic( new JLabelPad( "Período" ), 7, 0, 200, 20 );
		pinManut.adic( txtDatainiManut, 7, 20, 100, 20 );
		pinManut.adic( new JLabelPad( "até" ), 113, 20, 27, 20 );
		pinManut.adic( txtDatafimManut, 140, 20, 100, 20 );
		pinManut.adic( btExecManut, 776, 20, 30, 64 );

		pinManut.adic( txtCodForManut, 7, 65, 50, 20, "Cód.for." );
		pinManut.adic( txtRazForManut, 60, 65, 180, 20, "Descrição do fornecedor" );

		JLabelPad separacao = new JLabelPad();
		separacao.setBorder( BorderFactory.createEtchedBorder() );
		pinManut.adic( separacao, 7, 95, 800, 2 );

		pinManut.adic( txtCodPagManut		, 7		, 120	, 70	, 20, "Cod.pag." );
		pinManut.adic( txtDocManut			, 80	, 120	, 70	, 20, "Documento" );
		pinManut.adic( txtCodCompraManut	, 153	, 120	, 70	, 20, "Pedido" );
		pinManut.adic( txtCodForManut2		, 226	, 120	, 60	, 20, "Cód.for." );
		pinManut.adic( txtRazForManut2		, 289	, 120	, 200	, 20, "Razão social do fornecedor" );
		pinManut.adic( txtDtEmisManut		, 492	, 120	, 90	, 20, "Data emissão" );
		pinManut.adic( txtNumCheque			, 585	, 120	, 80	, 20, "Nro.Cheque" );
		pinManut.adic( txtCodOrdCp			, 668	, 120	, 80	, 20, "Ord.Compra" );
		pinManut.adic( txtCodSinal			, 751	, 120	, 60	, 20, "Cor" );

		vValsData.addElement( "V" );
		vValsData.addElement( "E" );
		vLabsData.addElement( "Vencimento" );
		vLabsData.addElement( "Emissão" );

		rgData = new JRadioGroup<String, String>( 2, 1, vLabsData, vValsData );
		rgData.setVlrString( "V" );
		pinManut.adic( new JLabelPad( "Filtrar por:" ), 247, 0, 115, 20 );
		pinManut.adic( rgData, 247, 20, 115, 65 );

		vValsVenc.addElement( "VE" );
		vValsVenc.addElement( "AV" );
		vValsVenc.addElement( "TT" );
		vLabsVenc.addElement( "Vencidas" );
		vLabsVenc.addElement( "À vencer" );
		vLabsVenc.addElement( "Ambas" );

		pinLbFiltroStatus.adic( lbFiltroStatus, 0, 0, 350, 15 );
		pinLbFiltroStatus.tiraBorda();

		pinManut.adic( pinLbFiltroStatus, 488, 3, 80, 15 );
		pinManut.adic( pinFiltroStatus, 488, 20, 280, 65 );

		pinFiltroStatus.adic( cbAPagar		, 5		, 5		, 80	, 20 );
		pinFiltroStatus.adic( cbPagas		, 5		, 30	, 80	, 20 );
		pinFiltroStatus.adic( cbPagParcial	, 85	, 5		, 80	, 20 );
		pinFiltroStatus.adic( cbCanceladas	, 85	, 30	, 100	, 20 );
		pinFiltroStatus.adic( cbEmpenhos	, 175	, 5		, 100	, 20 );

		rgVenc = new JRadioGroup<String, String>( 3, 2, vLabsVenc, vValsVenc );
		rgVenc.setVlrString( "TT" );
		pinManut.adic( new JLabelPad( "Filtrar por:" ), 365, 0, 150, 20 );
		pinManut.adic( rgVenc, 365, 20, 115, 65 );

		pinBotoesManut.adic( btCarregaBaixasMan, 5, 10, 30, 30 );
		pinBotoesManut.adic( btBaixaManut, 5, 40, 30, 30 );
		pinBotoesManut.adic( btEditManut, 5, 70, 30, 30 );
		pinBotoesManut.adic( btNovoManut, 5, 100, 30, 30 );
		pinBotoesManut.adic( btEstManut, 5, 130, 30, 30 );
		pinBotoesManut.adic( btExcluirManut, 5, 160, 30, 30 );
		pinBotoesManut.adic( btCancItem, 5, 190, 30, 30 );
		pinBotoesManut.adic( btImpRec, 5, 220, 30, 30 );		
	}

	private void limpaConsulta() {

		txtPrimCompr.setVlrString( "" );
		txtUltCompr.setVlrString( "" );
		txtDataMaxFat.setVlrString( "" );
		txtVlrMaxFat.setVlrString( "" );
		txtVlrTotCompr.setVlrString( "" );
		txtVlrTotPago.setVlrString( "" );
		txtVlrTotAberto.setVlrString( "" );
		txtDataMaxAcum.setVlrString( "" );
		txtVlrMaxAcum.setVlrString( "" );
	}

	private void carregaConsulta() {

		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		StringBuffer sSQL = new StringBuffer();

		limpaConsulta();
		tabConsulta.limpa();

		try {

			// Busca os totais ...
			sSQL.append( "SELECT SUM(VLRPARCPAG),SUM(VLRPAGOPAG),SUM(VLRAPAGPAG),MIN(DATAPAG),MAX(DATAPAG) " );
			sSQL.append( "FROM FNPAGAR " );
			sSQL.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODFOR=? " );

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNPAGAR" ) );
			ps.setInt( 3, txtCodFor.getVlrInteger().intValue() );

			rs = ps.executeQuery();

			if ( rs.next() ) {

				txtVlrTotCompr.setVlrString( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( 1 ) ) );
				txtVlrTotPago.setVlrString( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( 2 ) ) );
				txtVlrTotAberto.setVlrString( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( 3 ) ) );
				txtPrimCompr.setVlrString( rs.getDate( 4 ) == null ? "" : StringFunctions.sqlDateToStrDate( rs.getDate( 4 ) ) );
				txtUltCompr.setVlrString( rs.getDate( 5 ) == null ? "" : StringFunctions.sqlDateToStrDate( rs.getDate( 5 ) ) );
			}

			rs.close();
			ps.close();

			con.commit();

			// Busca a maior fatura ...
			sSQL.delete( 0, sSQL.length() );
			sSQL.append( "SELECT MAX(VLRPARCPAG),DATAPAG " );
			sSQL.append( "FROM FNPAGAR " );
			sSQL.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODFOR=? " );
			sSQL.append( "GROUP BY DATAPAG " );
			sSQL.append( "ORDER BY 1 DESC" );

			ps1 = con.prepareStatement( sSQL.toString() );
			ps1.setInt( 1, Aplicativo.iCodEmp );
			ps1.setInt( 2, ListaCampos.getMasterFilial( "FNPAGAR" ) );
			ps1.setInt( 3, txtCodFor.getVlrInteger().intValue() );

			rs1 = ps1.executeQuery();

			if ( rs1.next() ) {

				txtVlrMaxFat.setVlrString( Funcoes.strDecimalToStrCurrency( 15, 2, rs1.getString( 1 ) ) );
				txtDataMaxFat.setVlrString( rs1.getDate( "DATAPAG" ) == null ? "" : StringFunctions.sqlDateToStrDate( rs1.getDate( "DATAPAG" ) ) );
			}

			rs1.close();
			ps1.close();

			con.commit();

			// Busca o maior acumulo ...
			sSQL.delete( 0, sSQL.length() );
			sSQL.append( "SELECT EXTRACT(MONTH FROM DATAPAG), SUM(VLRPARCPAG), EXTRACT(YEAR FROM DATAPAG) " );
			sSQL.append( "FROM FNPAGAR " );
			sSQL.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODFOR=? " );
			sSQL.append( "GROUP BY 1, 3 " );
			sSQL.append( "ORDER BY 2 DESC" );

			ps2 = con.prepareStatement( sSQL.toString() );
			ps2.setInt( 1, Aplicativo.iCodEmp );
			ps2.setInt( 2, ListaCampos.getMasterFilial( "FNPAGAR" ) );
			ps2.setInt( 3, txtCodFor.getVlrInteger().intValue() );

			rs2 = ps2.executeQuery();

			if ( rs2.next() ) {

				txtDataMaxAcum.setVlrString( Funcoes.strMes( rs2.getInt( 1 ) ) + " de " + rs2.getInt( 3 ) );
				txtVlrMaxAcum.setVlrString( Funcoes.strDecimalToStrCurrency( 15, 2, rs2.getString( 2 ) ) );
			}

			rs2.close();
			ps2.close();

			con.commit();

			carregaGridConsulta();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a consulta!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			ps1 = null;
			ps2 = null;
			rs = null;
			rs1 = null;
			rs2 = null;
			sSQL = null;
		}
	}

	private void carregaGridConsulta() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();
		float bdVlrAPagar = 0.0f;
		float bdVlrPago = 0.0f;

		Float bdTotVencido = 0.0f;
		Float bdTotParcial = 0.0f;
		Float bdTotPago = 0.0f;
		Float bdTotVencer = 0.0f;
		Float bdTotCancelado = 0.0f;

		try {

			vCodPag.clear();
			vNParcPag.clear();

			sSQL.append( "SELECT IT.DTVENCITPAG," );
			sSQL.append( "(SELECT C.SERIE FROM CPCOMPRA C " );
			sSQL.append( "WHERE C.CODCOMPRA=P.CODCOMPRA AND C.CODEMP=P.CODEMPCP AND C.CODFILIAL=P.CODFILIALCP)," );
			sSQL.append( "P.DOCPAG,P.CODCOMPRA,P.DATAPAG,IT.VLRPARCITPAG,IT.DTPAGOITPAG,IT.VLRPAGOITPAG," );
			sSQL.append( "(CASE WHEN IT.DTPAGOITPAG IS NULL THEN CAST('today' AS DATE)-IT.DTVENCITPAG " );
			sSQL.append( "ELSE IT.DTPAGOITPAG-IT.DTVENCITPAG END ) ATRASO, P.OBSPAG," );
			sSQL.append( "(SELECT B.NOMEBANCO FROM FNBANCO B " );
			sSQL.append( "WHERE B.CODBANCO=P.CODBANCO AND B.CODEMP=P.CODEMPBO AND B.CODFILIAL=P.CODFILIALBO) AS NOMEBANCO," );
			sSQL.append( "P.CODPAG,IT.NPARCPAG,IT.VLRPAGOITPAG,IT.VLRAPAGITPAG,IT.STATUSITPAG, IT.VLRCANCITPAG " );
			sSQL.append( "FROM FNPAGAR P,FNITPAGAR IT " );
			sSQL.append( "WHERE P.CODFOR=? AND P.CODEMP=? AND P.CODFILIAL=? " );
			sSQL.append( "AND IT.CODPAG = P.CODPAG AND IT.CODEMP=P.CODEMP " );
			sSQL.append( "AND IT.CODFILIAL=P.CODFILIAL " );
			sSQL.append( "ORDER BY P.CODPAG,IT.NPARCPAG" );

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, txtCodFor.getVlrInteger().intValue() );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "FNPAGAR" ) );

			rs = ps.executeQuery();

			for (int i = 0; rs.next(); i++) {

				bdVlrAPagar = Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VlrApagItPag" ) ).floatValue();
				bdVlrPago = Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VlrPagoItPag" ) ).floatValue();

				if (CANCELADO.equals(rs.getString("StatusItPag"))) {
					imgColuna = imgCancelado;
					bdTotCancelado += Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VlrCancItPag" ) ).floatValue();
				}
				else if ( rs.getString( "StatusItPag" ).equals( PAGAMENTO_TOTAL ) && bdVlrAPagar == 0.0f ) {
					imgColuna = imgPago;
					bdTotPago += Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VlrPagoItPag" ) ).floatValue();
				} else if (bdVlrPago > 0) {
					imgColuna = imgPagoParcial;
					bdTotParcial += Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VlrPagoItPag" ) ).floatValue();

					if (rs.getDate( "DtVencItPag" ).before( Calendar.getInstance().getTime())) {
						bdTotVencido += Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VLRApagItPag" ) ).floatValue();
					} else {
						bdTotVencer += Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VLRApagItPag" ) ).floatValue();
					}

				} else if (rs.getDate( "DtVencItPag" ).before( Calendar.getInstance().getTime())) {
					imgColuna = imgVencido;
					bdTotVencido += Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VLRPARCITPAG" ) ).floatValue();
				} else if (rs.getDate( "DtVencItPag" ).after( Calendar.getInstance().getTime())) {
					imgColuna = imgNaoVencido;
					bdTotVencer += Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VlrApagItPag" ) ).floatValue();
				}

				tabConsulta.adicLinha();
				tabConsulta.setValor( imgColuna, i, 0 );
				tabConsulta.setValor( rs.getDate( "DtVencItPag" ) == null ? "" : StringFunctions.sqlDateToStrDate( rs.getDate( "DtVencItPag" ) ), i, 1 );
				tabConsulta.setValor( rs.getString( 2 ) == null ? "" : rs.getString( 2 ), i, 2 );
				tabConsulta.setValor( rs.getString( "DocPag" ) == null ? "" : rs.getString( "DocPag" ), i, 3 );
				tabConsulta.setValor( String.valueOf( rs.getInt( "CodCompra" ) ), i, 4 );
				tabConsulta.setValor( rs.getDate( "DataPag" ) == null ? "" : StringFunctions.sqlDateToStrDate( rs.getDate( "DataPag" ) ), i, 5 );
				tabConsulta.setValor( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( "VlrParcItPag" ) ), i, 6 );
				tabConsulta.setValor( rs.getDate( "DtPagoItPag" ) == null ? "" : StringFunctions.sqlDateToStrDate( rs.getDate( "DtPagoItPag" ) ), i, 7 );
				tabConsulta.setValor( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( "VlrPagoItPag" ) ), i, 8 );
				tabConsulta.setValor( new Integer( rs.getInt( 9 ) ), i, 9 );
				tabConsulta.setValor( rs.getString( "ObsPag" ) == null ? "" : rs.getString( "ObsPag" ), i, 10 );
				tabConsulta.setValor( rs.getString( 11 ) == null ? "" : rs.getString( 11 ), i, 11 );
				tabConsulta.setValor( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( "VlrCancItPag" ) ), i, 12 );

				vCodPag.addElement( rs.getString( "CodPag" ) );
				vNParcPag.addElement( rs.getString( "NParcPag" ) );
			}

			txtTotalVencido.setVlrBigDecimal( new BigDecimal(Funcoes.arredDouble( bdTotVencido.doubleValue(), Aplicativo.casasDecFin )) );
			txtTotalParcial.setVlrBigDecimal( new BigDecimal(Funcoes.arredDouble( bdTotParcial.doubleValue(), Aplicativo.casasDecFin )) );
			txtTotalPago.setVlrBigDecimal( new BigDecimal(Funcoes.arredDouble( bdTotPago.doubleValue(), Aplicativo.casasDecFin )) );
			txtTotalVencer.setVlrBigDecimal( new BigDecimal(Funcoes.arredDouble( bdTotVencer.doubleValue(), Aplicativo.casasDecFin )) );
			txtTotalCancelado.setVlrBigDecimal( new BigDecimal(Funcoes.arredDouble( bdTotCancelado.doubleValue(), Aplicativo.casasDecFin )) );

			con.commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao montar a tabela de consulta!\n" + err.getMessage(), true, con, err );
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}
	}

	private void carregaGridBaixa() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();

		Float bdTotVencido = 0.0f;
		Float bdTotParcial = 0.0f;
		Float bdTotPago = 0.0f;
		Float bdTotVencer = 0.0f;
		Float bdTotCancelado = 0.0f;

		try {

			vNParcBaixa.clear();
			vNumContas.clear();
			vCodPlans.clear();
			vCodCCs.clear();
			vCodPed.clear();
			tabBaixa.limpa();

			sSQL.append( "SELECT IT.DTVENCITPAG,IT.STATUSITPAG,P.CODPAG,IT.DOCLANCAITPAG " );
			sSQL.append( ", P.CODCOMPRA,IT.VLRPARCITPAG,IT.DTPAGOITPAG,IT.VLRPAGOITPAG " );
			sSQL.append( ", IT.VLRAPAGITPAG, IT.VLRDESCITPAG, IT.NUMCONTA" );
			sSQL.append( ", (SELECT C.DESCCONTA FROM FNCONTA C " );
			sSQL.append( "WHERE C.NUMCONTA=IT.NUMCONTA AND C.CODEMP=IT.CODEMPCA AND C.CODFILIAL=IT.CODFILIALCA) DESCCONTA, IT.CODPLAN " );
			sSQL.append( ", (SELECT PL.DESCPLAN FROM FNPLANEJAMENTO PL " );
			sSQL.append( "WHERE PL.CODPLAN=IT.CODPLAN AND PL.CODEMP=IT.CODEMPPN AND PL.CODFILIAL=IT.CODFILIALPN) DESCPLAN, IT.CODCC " );
			sSQL.append( ", (SELECT CC.DESCCC FROM FNCC CC " );
			sSQL.append( "WHERE CC.CODCC=IT.CODCC AND CC.CODEMP=IT.CODEMPCC AND CC.CODFILIAL=IT.CODFILIALCC AND CC.ANOCC=IT.ANOCC) DESCCC " );
			sSQL.append( ", IT.OBSITPAG,IT.NPARCPAG,IT.VLRJUROSITPAG,IT.DTITPAG, IT.VLRCANCITPAG " );
			sSQL.append( ", ct.codcontr, ct.desccontr, ict.coditcontr, ict.descitcontr ");
			sSQL.append( "FROM FNPAGAR P, FNITPAGAR IT " );
			sSQL.append( "left outer join vdcontrato ct on ct.codemp=it.codempct and ct.codfilial=it.codfilialct and ct.codcontr=it.codcontr ");
			sSQL.append( "left outer join vditcontrato ict on ict.codemp=it.codempct and ict.codfilial=it.codfilialct and ict.codcontr=it.codcontr and ict.coditcontr=it.coditcontr ");
			sSQL.append( "WHERE P.CODPAG=? AND P.CODEMP=? AND P.CODFILIAL=? " );
			sSQL.append( "AND IT.CODPAG=P.CODPAG AND IT.CODEMP=P.CODEMP AND IT.CODFILIAL=P.CODFILIAL " );
			sSQL.append( "ORDER BY IT.DTVENCITPAG,IT.STATUSITPAG " );

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, txtCodPagBaixa.getVlrInteger().intValue() );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "FNPAGAR" ) );

			System.out.println( "SQL grid baixa:" + sSQL.toString() );

			rs = ps.executeQuery();

			double bdVlrAPagar = 0.0;
			double bdVlrPago = 0.0;

			for (int i = 0; rs.next(); i++) {

				bdVlrAPagar = Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VlrApagItPag" ) ).doubleValue();
				bdVlrPago = Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VlrPagoItPag" ) ).doubleValue();
				if (CANCELADO.equals(rs.getString("StatusItPag"))) {
					imgColuna = imgCancelado;
					bdTotCancelado += Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VlrCancItPag" ) ).floatValue();
				} else if (PAGAMENTO_TOTAL.equals( rs.getString( "StatusItPag" ) ) && bdVlrAPagar == 0.0) {
					imgColuna = imgPago;
					bdTotPago += Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VlrPagoItPag" ) ).floatValue();
				} else if ( bdVlrPago > 0 ) {
					imgColuna = imgPagoParcial;
					bdTotParcial += Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VlrPagoItPag" ) ).floatValue();

					if (rs.getDate("DtVencItPag").before(Calendar.getInstance().getTime())) {
						bdTotVencido += Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VLRApagItPag" ) ).floatValue();
					} else {
						bdTotVencer += Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VLRApagItPag" ) ).floatValue();
					}
				} else if (rs.getDate("DtVencItPag").before(Calendar.getInstance().getTime())) {
					imgColuna = imgVencido;
					bdTotVencido += Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VLRPARCITPAG" ) ).floatValue();
				} else if (rs.getDate("DtVencItPag").after(Calendar.getInstance().getTime())) {
					imgColuna = imgNaoVencido;
					bdTotVencer += Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VlrApagItPag" ) ).floatValue();
				}

				tabBaixa.adicLinha();
				tabBaixa.setValor( imgColuna, i, enum_tab_baixa.STATUS.ordinal() );
				tabBaixa.setValor( rs.getDate( "DtVencItPag" ) == null ? "" : StringFunctions.sqlDateToStrDate( rs.getDate( "DtVencItPag" ) ), i, enum_tab_baixa.DTVENCTO.ordinal() );
				tabBaixa.setValor( rs.getString( "NParcPag" ), i, enum_tab_baixa.NPARC.ordinal() );
				tabBaixa.setValor( rs.getString( "DocLancaItPag" ) == null ? "" : rs.getString( "DocLancaItPag" ), i, enum_tab_baixa.DOC.ordinal() );
				tabBaixa.setValor( String.valueOf( rs.getInt( "CodCompra" ) ), i, enum_tab_baixa.CODCOMPRA.ordinal() );
				tabBaixa.setValor( Funcoes.strDecimalToStrCurrency( 15, 2, rs.getString( "VlrParcItPag" ) ), i, enum_tab_baixa.VLRPARC.ordinal() );
				tabBaixa.setValor( rs.getDate( "DtPagoItPag" ) == null ? "" : StringFunctions.sqlDateToStrDate( rs.getDate( "DtPagoItPag" ) ), i, enum_tab_baixa.DTPAGTO.ordinal() );
				tabBaixa.setValor( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( "VlrPagoItPag" ) ), i, enum_tab_baixa.VLRPAGO.ordinal() );
				tabBaixa.setValor( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( "VlrDescItPag" ) ), i, enum_tab_baixa.VLRDESC.ordinal() );
				tabBaixa.setValor( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( "VlrJurosItPag" ) ), i, enum_tab_baixa.VLRJUROS.ordinal() );
				tabBaixa.setValor( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( "VlrApagItPag" ) ), i, enum_tab_baixa.VLRAPAG.ordinal() );
				tabBaixa.setValor( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( "VlrCancItPag" ) ), i, enum_tab_baixa.VLRCANC.ordinal() );
				tabBaixa.setValor( rs.getString( "NUMCONTA" ) == null ? "" : rs.getString( "NUMCONTA" ), i, enum_tab_baixa.NUMCONTA.ordinal() );
				tabBaixa.setValor( rs.getString( "DESCCONTA" ) == null ? "" : rs.getString( "DESCCONTA" ), i, enum_tab_baixa.DESCCONTA.ordinal() );
				tabBaixa.setValor( rs.getString( "CODPLAN" ) == null ? "" : rs.getString( "CODPLAN" ), i, enum_tab_baixa.CODPLAN.ordinal() );
				tabBaixa.setValor( rs.getString( "DESCPLAN" ) == null ? "" : rs.getString( "DESCPLAN" ), i, enum_tab_baixa.DESCPLAN.ordinal() );
				tabBaixa.setValor( rs.getString( "CODCC" ) == null ? "" : rs.getString( "CODCC" ), i, enum_tab_baixa.CODCC.ordinal() );
				tabBaixa.setValor( rs.getString( "DESCCC" ) == null ? "" : rs.getString( "DESCCC" ), i, enum_tab_baixa.DESCCC.ordinal() );
				tabBaixa.setValor( rs.getString( "ObsItPag" ) == null ? "" : rs.getString( "ObsItPag" ), i, enum_tab_baixa.OBS.ordinal() );
				tabBaixa.setValor( rs.getString( enum_tab_baixa.CODCONTR.name() ), i, enum_tab_baixa.CODCONTR.ordinal() );
				tabBaixa.setValor( rs.getString( enum_tab_baixa.DESCCONTR.name() ), i, enum_tab_baixa.DESCCONTR.ordinal() );
				tabBaixa.setValor( rs.getString( enum_tab_baixa.CODITCONTR.name() ), i, enum_tab_baixa.CODITCONTR.ordinal() );
				tabBaixa.setValor( rs.getString( enum_tab_baixa.DESCITCONTR.name() ), i, enum_tab_baixa.DESCITCONTR.ordinal() );
				vCodPed.addElement( rs.getString( "CodCompra" ) );
				vNParcBaixa.addElement( rs.getString( "NParcPag" ) );
				vNumContas.addElement( rs.getString( "NumConta" ) == null ? "" : rs.getString( "NumConta" ) );
				vCodPlans.addElement( rs.getString( "CodPlan" ) == null ? "" : rs.getString( "CodPlan" ) );
				vCodCCs.addElement( rs.getString( "CodCC" ) == null ? "" : rs.getString( "CodCC" ) );
			}

			rs.close();
			ps.close();

			txtTotalVencido.setVlrBigDecimal( new BigDecimal(Funcoes.arredDouble( bdTotVencido.doubleValue(), Aplicativo.casasDecFin )) );
			txtTotalParcial.setVlrBigDecimal( new BigDecimal(Funcoes.arredDouble( bdTotParcial.doubleValue(), Aplicativo.casasDecFin )) );
			txtTotalPago.setVlrBigDecimal( new BigDecimal(Funcoes.arredDouble( bdTotPago.doubleValue(), Aplicativo.casasDecFin )) );
			txtTotalVencer.setVlrBigDecimal( new BigDecimal(Funcoes.arredDouble( bdTotVencer.doubleValue(), Aplicativo.casasDecFin )) );
			txtTotalCancelado.setVlrBigDecimal( new BigDecimal(Funcoes.arredDouble( bdTotCancelado.doubleValue(), Aplicativo.casasDecFin )) );

			con.commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao montar a tabela de baixa!\n" + err.getMessage(), true, con, err );
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}
	}

	private void carregaGridManut() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		StringBuffer sWhereManut = new StringBuffer();
		StringBuffer sWhereStatus = new StringBuffer();

		Float bdTotVencido = 0.0f;
		Float bdTotParcial = 0.0f;
		Float bdTotPago = 0.0f;
		Float bdTotVencer = 0.0f;
		Float bdTotCancelado = 0.0f;

		try {

			if (validaPeriodo()) {

				tabManut.limpa();
				vNumContas.clear();
				vCodPlans.clear();
				vCodCCs.clear();
				vDtEmiss.clear();
				vCodPed.clear();

				if (txtCodPagManut.getVlrInteger()>0) {
					sWhereManut.append( " and IT.CODEMP="+Aplicativo.iCodEmp+ " AND IT.CODFILIAL=" + 	
							lcPagManut.getCodFilial()+ " AND IT.CODPAG= " + txtCodPagManut.getVlrInteger()+ " " );
				} else if (txtNumCheque.getVlrInteger()>0) {
					sWhereManut.append( " and exists ( " );
					sWhereManut.append( "select ch.numcheq from fnpagcheq pc, fncheque ch " );
					sWhereManut.append( "where ch.codemp=pc.codempch and ch.codfilial=pc.codfilialch and ch.seqcheq=pc.seqcheq " );
					sWhereManut.append( "and pc.codemp=it.codemp and pc.codfilial=it.codfilial and pc.codpag=it.codpag and pc.nparcpag=it.nparcpag " );
					sWhereManut.append( "and ch.numcheq="+txtNumCheque.getVlrString());
					sWhereManut.append( " ) " );

				} else {

					sWhereManut.append( " AND " );
					sWhereManut.append( "V".equals( rgData.getVlrString() ) ? "IT.DTVENCITPAG" : "IT.DTITPAG" );
					sWhereManut.append( " BETWEEN ? AND ? AND P.CODEMP=? AND P.CODFILIAL=?" );

					if ("S".equals(cbPagas.getVlrString()) || "S".equals(cbAPagar.getVlrString()) 
							|| "S".equals(cbPagParcial.getVlrString()) || "S".equals(cbCanceladas.getVlrString())) {

						boolean bStatus = false;

						if ("S".equals(cbPagas.getVlrString())) {
							sWhereStatus.append( "IT.STATUSITPAG='PP'" );
							bStatus = true;
						}
						if ("S".equals(cbAPagar.getVlrString())) {
							sWhereStatus.append( bStatus ? " OR IT.STATUSITPAG='P1'" : " IT.STATUSITPAG='P1'" );
							bStatus = true;
						}
						if ("S".equals(cbPagParcial.getVlrString())) {
							sWhereStatus.append( bStatus ? " OR IT.STATUSITPAG='PL'" : " IT.STATUSITPAG='PL'" );
							bStatus = true;
						}
						if ("S".equals(cbCanceladas.getVlrString())) {
							sWhereStatus.append( bStatus ? " OR IT.STATUSITPAG='CP'" : " IT.STATUSITPAG='CP'" );
						}

						sWhereManut.append( " AND (" );
						sWhereManut.append( sWhereStatus );
						sWhereManut.append( ")" );
					} else {
						Funcoes.mensagemInforma( null, "Você deve selecionar ao menos um filtro de status!" );
						return;
					}

					if (!"TT".equals(rgVenc.getVlrString())) {

						sWhereManut.append( " AND IT.DTVENCITPAG" );

						if ("VE".equals(rgVenc.getVlrString())) {
							sWhereManut.append( " <'" );
							sWhereManut.append( Funcoes.dateToStrDB( new Date() ) );
							sWhereManut.append( "'" );
						} else {
							sWhereManut.append( " >='" );
							sWhereManut.append( Funcoes.dateToStrDB( new Date() ) );
							sWhereManut.append( "'" );
						}
					}

					if (!"".equals(txtCodForManut.getText().trim())) {
						sWhereManut.append( " AND P.CODFOR=" );
						sWhereManut.append( txtCodForManut.getText().trim() );
					}

					if ("S".equals(cbEmpenhos.getVlrString())) {
						sWhereManut.append( " and (p.codordcp is not null) " );

					} else {
						sWhereManut.append( " and (p.codordcp is null) " );
					}

					if (txtCodOrdCp.getVlrInteger()>0) {
						sWhereManut.append( " and ( (p.codempoc=" );
						sWhereManut.append( lcOrdCompra.getCodEmp());
						sWhereManut.append( " and p.codfilialoc= "); 
						sWhereManut.append( lcOrdCompra.getCodFilial() );
						sWhereManut.append( " and p.codordcp= ");
						sWhereManut.append( txtCodOrdCp.getVlrString() );

						sWhereManut.append( " ) or exists ( " );
						sWhereManut.append( "select cp.codordcp from cpcompra cp " );
						sWhereManut.append( "where cp.codemp=p.codempcp and cp.codfilial=p.codfilialcp and cp.codcompra=p.codcompra " );
						sWhereManut.append( " ) " );

					}

					if (txtCodSinal.getVlrInteger()>0) {
						sWhereManut.append( " and it.codsinal=" + txtCodSinal.getVlrInteger() + " ");
					}
				}

				sql.append( "select ");

				sql.append( "it.dtitpag, it.dtvencitpag, it.statusitpag, p.codfor, f.razfor, p.codpag, it.nparcpag, " );
				sql.append( "it.doclancaitpag, p.codcompra, it.vlrparcitpag, it.dtpagoitpag, it.vlrpagoitpag, " );
				sql.append( "it.vlrapagitpag, it.vlrdescitpag, it.vlrdevitpag, it.vlradicitpag, it.vlrjurositpag, " );
				sql.append( "it.codplan, it.codcc, it.dtitpag, p.docpag, " );

				sql.append( "coalesce(it.numconta,'') numconta, ");
				sql.append( "coalesce(it.codtipocob,'') codtipocob, ");
				sql.append( "coalesce(it.vlrcancitpag,0) vlrcancitpag, " );

				sql.append( "coalesce(( select pl.descplan from fnplanejamento pl " );
				sql.append( "where PL.CODPLAN=IT.CODPLAN AND PL.CODEMP=IT.CODEMPPN AND PL.CODFILIAL=IT.CODFILIALPN),'') descplan, ");

				sql.append( "coalesce(( select cc.desccc from fncc cc " );
				sql.append( "where CC.CODCC=IT.CODCC AND CC.CODEMP=IT.CODEMPCC AND CC.CODFILIAL=IT.CODFILIALCC AND CC.ANOCC=IT.ANOCC),'') DESCCC," );

				sql.append( "coalesce(IT.OBSITPAG,'') obsitpag, " );

				sql.append( "coalesce((select co.doccompra from cpcompra co " );				
				sql.append( "where co.codcompra=p.codcompra and co.codemp=p.codempcp and co.codfilial=p.codfilialcp),'') doccompra," );

				sql.append( "coalesce((select com.codordcp from cpcompra com " );				
				sql.append( "where com.codcompra=p.codcompra and com.codemp=p.codempcp and com.codfilial=p.codfilialcp),'') codordcp_efetiva," );

				sql.append( "coalesce((select oc.codordcp from cpordcompra oc " );				
				sql.append( "where oc.codordcp=p.codordcp and oc.codemp=p.codempoc and oc.codfilial=p.codfilialoc),'') codordcp_empenho," );

				sql.append( "coalesce((SELECT T.DESCTIPOCOB FROM FNTIPOCOB T " );
				sql.append( "WHERE IT.CODEMPTC=T.CODEMP AND IT.CODFILIALTC=T.CODFILIAL AND IT.CODTIPOCOB=T.CODTIPOCOB),'') DESCTIPOCOB, sn.corsinal, " );

				// Verifica se existe cheque para buscar...
				sql.append( "coalesce((select count(*) from fnpagcheq pc where pc.codemp=it.codemp and pc.codfilial=it.codfilial and pc.codpag=it.codpag and pc.nparcpag=it.nparcpag),0) temcheque " );

				sql.append( ", ct.codcontr, ct.desccontr, ict.coditcontr, ict.descitcontr ");

				sql.append( "from fnpagar p, cpforneced f, fnitpagar it  " );

				sql.append( "LEFT OUTER JOIN FNSINAL SN ON SN.CODEMP=It.CODEMPSN AND SN.CODFILIAL=It.CODFILIALSN AND SN.CODSINAL=It.CODSINAL ");
				sql.append( "left outer join vdcontrato ct on ct.codemp=it.codempct and ct.codfilial=it.codfilialct and ct.codcontr=it.codcontr ");
				sql.append( "left outer join vditcontrato ict on ict.codemp=it.codempct and ict.codfilial=it.codfilialct and ict.codcontr=it.codcontr and ict.coditcontr=it.coditcontr ");

				sql.append( "WHERE P.CODPAG=IT.CODPAG AND F.CODFOR=P.CODFOR AND F.CODEMP=P.CODEMPFR AND F.CODFILIAL=P.CODFILIALFR " );
				sql.append( sWhereManut );
				sql.append( " AND IT.CODEMP=P.CODEMP AND IT.CODFILIAL=P.CODFILIAL " );
				sql.append( "ORDER BY IT.DTVENCITPAG, IT.CODPAG, IT.NPARCPAG, IT.STATUSITPAG " );
				//sql.append( "ORDER BY IT.DTVENCITPAG " );

				try {

					ps = con.prepareStatement( sql.toString() );

					if (!(txtCodPagManut.getVlrInteger()>0) && !(txtNumCheque.getVlrInteger()>0)) {
						ps.setDate( 1, Funcoes.dateToSQLDate( dIniManut ) );
						ps.setDate( 2, Funcoes.dateToSQLDate( dFimManut ) );
						ps.setInt( 3, Aplicativo.iCodEmp );
						ps.setInt( 4, ListaCampos.getMasterFilial( "FNPAGAR" ) );
					}

					rs = ps.executeQuery();

					double bdVlrAPagar = 0.0;
					double bdVlrPago = 0.0;

					System.out.println( sql.toString() );

					Vector<Cheque> cheques = null;

					for (int i = 0; rs.next(); i++) {

						tabManut.adicLinha();

						bdVlrAPagar = Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VlrApagItPag" ) ).doubleValue();
						bdVlrPago = Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VlrPagoItPag" ) ).doubleValue();

						if (CANCELADO.equals(rs.getString("StatusItPag"))) {
							imgColuna = imgCancelado;
							bdTotCancelado += Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VlrCancItPag" ) ).floatValue();
						} else if (PAGAMENTO_TOTAL.equals(rs.getString("StatusItPag")) && bdVlrAPagar == 0.0) {
							StringBuilder sqlLanca = new StringBuilder();
							sqlLanca.append( "SELECT COUNT (CODLANCA) FROM FNLANCA ");
							sqlLanca.append( "WHERE CODPAG = ? AND NPARCPAG = ? ");
							sqlLanca.append( "AND CODEMPPG = ? AND CODFILIALPG = ? ");
							sqlLanca.append( "AND CODEMP = ? AND CODFILIAL = ? ");

							ps = con.prepareStatement( sqlLanca.toString() );
							ps.setInt( 1, rs.getInt( enum_tab_manut.CODPAG.name() ) );
							ps.setInt( 2, rs.getInt( enum_tab_manut.NPARCPAG.name() ) );
							ps.setInt( 3, Aplicativo.iCodEmp );
							ps.setInt( 4, ListaCampos.getMasterFilial( "FNPAGAR" ) );
							ps.setInt( 5, Aplicativo.iCodEmp );
							ps.setInt( 6, ListaCampos.getMasterFilial( "FNLANCA" ) );
							ResultSet rsLanca = ps.executeQuery();
							rsLanca.next();

							if (rsLanca.getInt( 1 ) > 1) {
								imgColuna = imgPagoComParciais;
							} else {
								imgColuna = imgPago;
							}
							bdTotPago += Funcoes.strDecimalToBigDecimal(Aplicativo.casasDecFin, rs.getString("VlrPagoItPag")).floatValue();
						} else if (bdVlrPago > 0) {
							imgColuna = imgPagoParcial;
							bdTotParcial += Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VlrPagoItPag" ) ).floatValue();

							if (rs.getDate("DtVencItPag").before(Calendar.getInstance().getTime())) {
								bdTotVencido += Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VLRApagItPag" ) ).floatValue();
							} else {
								bdTotVencer += Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VLRApagItPag" ) ).floatValue();
							}
						} else if (rs.getDate("DtVencItPag").before(Calendar.getInstance().getTime())) {
							imgColuna = imgVencido;
							bdTotVencido += Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VLRPARCITPAG" ) ).floatValue();
						} else if (rs.getDate("DtVencItPag").after(Calendar.getInstance().getTime())) {
							imgColuna = imgNaoVencido;
							bdTotVencer += Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VlrApagItPag" ) ).floatValue();
						}

						Color corsinal = rs.getString( "corsinal" ) == null ? null : new Color(rs.getInt( "corsinal" ));

						tabManut.setValor( new Boolean(false), i, enum_tab_manut.SEL.ordinal() );
						tabManut.setValor( imgColuna, i, enum_tab_manut.IMGSTATUS.ordinal(), corsinal );

						tabManut.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( enum_tab_manut.DTVENCITPAG.name() ) ), i, enum_tab_manut.DTVENCITPAG.ordinal(), corsinal );

						tabManut.setValor( rs.getString( enum_tab_manut.STATUSITPAG.name() ),	i, enum_tab_manut.STATUSITPAG.ordinal(), corsinal );
						tabManut.setValor( rs.getString( enum_tab_manut.CODFOR.name() ),	 	i, enum_tab_manut.CODFOR.ordinal(), corsinal );
						tabManut.setValor( rs.getString( enum_tab_manut.RAZFOR.name() ), 		i, enum_tab_manut.RAZFOR.ordinal(), corsinal );
						tabManut.setValor( new Integer(rs.getInt( enum_tab_manut.CODPAG.name() )), 		i, enum_tab_manut.CODPAG.ordinal(), corsinal );
						tabManut.setValor( new Integer(rs.getInt( enum_tab_manut.NPARCPAG.name()) ), 		i, enum_tab_manut.NPARCPAG.ordinal(), corsinal );

						String doclanca = rs.getString( "DocLancaItPag" );
						String docpag = rs.getString( "DocPag" );
						String doc = "";

						if (doclanca == null) {
							if (docpag != null) {
								doc = rs.getString( "DocPag" ) + "/" + rs.getString( "NParcPag" );
							}
						} else {
							doc = doclanca;
						}

						tabManut.setValor( doc, i, enum_tab_manut.DOC.ordinal(), corsinal );

						tabManut.setValor( rs.getString( enum_tab_manut.DOCCOMPRA.name() ), i, enum_tab_manut.DOCCOMPRA.ordinal(), corsinal );

						tabManut.setValor( new StringDireita( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( "VlrParcItPag" ) ) ), i, enum_tab_manut.VLRPARCITPAG.ordinal(), corsinal );
						tabManut.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( enum_tab_manut.DTPAGOITPAG.name() ) ), i, enum_tab_manut.DTPAGOITPAG.ordinal(), corsinal );
						tabManut.setValor( new StringDireita( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( enum_tab_manut.VLRPAGOITPAG.name()  ) )), i, 	enum_tab_manut.VLRPAGOITPAG.ordinal(), corsinal );
						tabManut.setValor( new StringDireita( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( enum_tab_manut.VLRDESCITPAG.name() ) )), i, 	enum_tab_manut.VLRDESCITPAG.ordinal(), corsinal );
						tabManut.setValor( new StringDireita( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( enum_tab_manut.VLRJUROSITPAG.name() ) )), i, 	enum_tab_manut.VLRJUROSITPAG.ordinal(), corsinal );
						tabManut.setValor( new StringDireita( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( enum_tab_manut.VLRDEVITPAG.name() ) )), i, 	enum_tab_manut.VLRDEVITPAG.ordinal(), corsinal );
						tabManut.setValor( new StringDireita( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( enum_tab_manut.VLRADICITPAG.name() ) )), i, 	enum_tab_manut.VLRADICITPAG.ordinal(), corsinal );
						tabManut.setValor( new StringDireita( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( enum_tab_manut.VLRAPAGITPAG.name() ) )), i, 	enum_tab_manut.VLRAPAGITPAG.ordinal(), corsinal );
						tabManut.setValor( new StringDireita( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( enum_tab_manut.VLRCANCITPAG.name() ) )), i, 	enum_tab_manut.VLRCANCITPAG.ordinal(), corsinal );
						tabManut.setValor( rs.getString( enum_tab_manut.NUMCONTA.name() 	) 	, i, enum_tab_manut.NUMCONTA.ordinal(), corsinal );
						tabManut.setValor( rs.getString( enum_tab_manut.DESCPLAN.name() 	) 	, i, enum_tab_manut.DESCPLAN.ordinal(), corsinal );
						tabManut.setValor( rs.getString( enum_tab_manut.DESCCC.name() 		) 	, i, enum_tab_manut.DESCCC.ordinal(), corsinal );
						tabManut.setValor( rs.getString( enum_tab_manut.CODTIPOCOB.name()	) 	, i, enum_tab_manut.CODTIPOCOB.ordinal(), corsinal );
						tabManut.setValor( rs.getString( enum_tab_manut.DESCTIPOCOB.name() 	) 	, i, enum_tab_manut.DESCTIPOCOB.ordinal(), corsinal );						
						tabManut.setValor( rs.getString( enum_tab_manut.OBSITPAG.name() 	) 	, i, enum_tab_manut.OBSITPAG.ordinal(), corsinal );
						tabManut.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( enum_tab_manut.DTITPAG.name()) ) 	, i, enum_tab_manut.DTITPAG.ordinal(), corsinal );
						tabManut.setValor( rs.getString( enum_tab_manut.CODCC.name() 		) 	, i, enum_tab_manut.CODCC.ordinal(), corsinal );
						tabManut.setValor( rs.getString( enum_tab_manut.CODPLAN.name() 		) 	, i, enum_tab_manut.CODPLAN.ordinal(), corsinal );
						tabManut.setValor( rs.getString( enum_tab_manut.CODCOMPRA.name() 	) 	, i, enum_tab_manut.CODCOMPRA.ordinal(), corsinal );
						tabManut.setValor( rs.getString( enum_tab_manut.NUMCONTA.name() 	) 	, i, enum_tab_manut.NUMCONTA.ordinal(), corsinal );
						tabManut.setValor( "".equals( rs.getString("codordcp_efetiva")) ? (rs.getString("codordcp_empenho")) : "" 	, i, enum_tab_manut.CODORDCP.ordinal(), corsinal );
						tabManut.setValor( rs.getString( enum_tab_manut.CODCONTR.name()	), i, enum_tab_manut.CODCONTR.ordinal(), corsinal );
						tabManut.setValor( rs.getString( enum_tab_manut.DESCCONTR.name()	), i, enum_tab_manut.DESCCONTR.ordinal(), corsinal );
						tabManut.setValor( rs.getString( enum_tab_manut.CODITCONTR.name()	), i, enum_tab_manut.CODITCONTR.ordinal(), corsinal );
						tabManut.setValor( rs.getString( enum_tab_manut.DESCITCONTR.name()	), i, enum_tab_manut.DESCITCONTR.ordinal(), corsinal );

						vCodPed.addElement( rs.getString( enum_tab_manut.CODCOMPRA.name()  ) );
						vNumContas.addElement( rs.getString( enum_tab_manut.NUMCONTA.name() ) );
						vCodPlans.addElement( rs.getString( enum_tab_manut.CODPLAN.name() ) );
						vCodCCs.addElement( rs.getString( enum_tab_manut.CODCC.name() ) );
						vDtEmiss.addElement( Funcoes.dateToStrDate( rs.getDate( enum_tab_manut.DTITPAG.name() ) ) );

						if (rs.getInt( "temcheque" )>0) {
							cheques = DLEditaPag.buscaCheques( rs.getInt( enum_tab_manut.CODPAG.name()), rs.getInt( enum_tab_manut.NPARCPAG.name() ), con);
							Vector<String> numcheques = new Vector<String>();
							for (int ic = 0; cheques.size() > ic; ic++) {
								Cheque cheque = (Cheque) cheques.get( ic );
								numcheques.add( cheque.getNumcheq().toString() );
							}
							tabManut.setValor( numcheques, i, enum_tab_manut.CHEQUES.ordinal(), corsinal );
						} else {
							tabManut.setValor( "", i, enum_tab_manut.CHEQUES.ordinal(), corsinal );
						}

					}	

					rs.close();
					ps.close();

					txtTotalVencido.setVlrBigDecimal( new BigDecimal(Funcoes.arredDouble( bdTotVencido.doubleValue(), Aplicativo.casasDecFin )) );
					txtTotalParcial.setVlrBigDecimal( new BigDecimal(Funcoes.arredDouble( bdTotParcial.doubleValue(), Aplicativo.casasDecFin ) ));
					txtTotalPago.setVlrBigDecimal( new BigDecimal(Funcoes.arredDouble( bdTotPago.doubleValue(), Aplicativo.casasDecFin ) ));
					txtTotalVencer.setVlrBigDecimal( new BigDecimal(Funcoes.arredDouble( bdTotVencer.doubleValue(), Aplicativo.casasDecFin ) ));
					txtTotalCancelado.setVlrBigDecimal( new BigDecimal(Funcoes.arredDouble( bdTotCancelado.doubleValue(), Aplicativo.casasDecFin ) ));

					con.commit();

				} catch ( SQLException err ) {
					err.printStackTrace();
					Funcoes.mensagemErro( this, "Erro ao montar a tabela de manutenção!\n" + err.getMessage(), true, con, err );
				}
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sql = null;
			sWhereManut = null;
			sWhereStatus = null;
		}
	}

	private void baixaConsulta() {
		if (tabConsulta.getLinhaSel() != -1) {

			txtCodPagBaixa.setVlrString( vCodPag.elementAt( tabConsulta.getLinhaSel() ) );
			int iNParc = ( new Integer( vNParcPag.elementAt( tabConsulta.getLinhaSel() ) ) ).intValue();

			lcPagBaixa.carregaDados();
			tpn.setSelectedIndex( 1 );
			btBaixa.requestFocus();

			for (int i = 0; i < vNParcBaixa.size(); i++) {
				if (iNParc == (new Integer(vNParcBaixa.elementAt(i))).intValue()) {
					tabBaixa.setLinhaSel( i );
					break;
				}
			}
		}
	}

	private void baixar(char cOrig) {

		PreparedStatement ps = null;
		StringBuffer sSQL = new StringBuffer();
		String[] sVals = null;
		String[] sRelPlanPag = null;
		String[] sRets = null;
		DLBaixaPag dl = null;
		ImageIcon imgStatusAt = null;
		Integer codhistpag = null;
		boolean lancafincontr = "S".equals( prefere.get( "lancafincontr" ) ); 

		try {
			if ( ( cOrig == 'M' ) && ( tabManut.getLinhaSel() > -1 ) ) { // Quando a função eh chamada da tab MANUTENÇÂO
				this.baixaTabManut();

			} else if ( ( cOrig == 'B' ) & ( tabBaixa.getLinhaSel() > -1 ) ) { // Quando a função eh chamada da tab BAIXAR

				imgStatusAt = ( (ImageIcon) tabBaixa.getValor( tabBaixa.getLinhaSel(), 0 ) );

				if ( imgStatusAt == imgPago ) {
					Funcoes.mensagemInforma( this, "A PARCELA JÁ FOI PAGA!" );
					return;
				}

				int iLin = tabBaixa.getLinhaSel();

				iCodPag = txtCodPagBaixa.getVlrInteger().intValue();
				iNParcPag = Integer.parseInt( (String) tabBaixa.getValor( iLin, 2 ) );

				sVals = new String[ VAL_BAIXAMANUT.values().length ];

				try {
					sRelPlanPag = daoMovimento.buscaRelPlanPag( txtCodPagBaixa.getVlrInteger().intValue() );
				} catch ( SQLException err ) {
					err.printStackTrace();
					Funcoes.mensagemErro( this, "Erro ao buscar Conta!\n" + err.getMessage(), true, con, err );
				} 	

				dl = new DLBaixaPag( this, lancafincontr, true );

				//					private enum VAL_BAIXAMANUT {CODFOR, RAZFOR, CODCONTA, CODPLAN, DOC, DTEMIS, DTVENC, VLRPARC, DTPAGTO, VLRPAGO, CODCC, CODTIPOCOB, OBS}

				sVals[ VAL_BAIXAMANUT.CODFOR.ordinal() ] = txtCodForBaixa.getVlrString();
				sVals[ VAL_BAIXAMANUT.RAZFOR.ordinal() ] = txtRazForBaixa.getVlrString();
				sVals[ VAL_BAIXAMANUT.CODCONTA.ordinal() ] = "".equals( vNumContas.elementAt( iLin ) ) ? sRelPlanPag[ 2 ] : vNumContas.elementAt( iLin );
				sVals[ VAL_BAIXAMANUT.CODPLAN.ordinal() ] = "".equals( vCodPlans.elementAt( iLin ) ) ? sRelPlanPag[ 1 ] : vCodPlans.elementAt( iLin );
				//				sVals[ 4 ] = txtDoc.getVlrString();
				sVals[ VAL_BAIXAMANUT.DOC.ordinal() ] = (String) tabBaixa.getValor( iLin, 3 ); 

				sVals[ VAL_BAIXAMANUT.DTEMIS.ordinal() ] = txtDtEmisBaixa.getVlrString();
				sVals[ VAL_BAIXAMANUT.DTVENC.ordinal() ] = (String) tabBaixa.getValor( iLin, 1 );
				sVals[ VAL_BAIXAMANUT.VLRPARC.ordinal() ] = (String) tabBaixa.getValor( iLin, 5 );
				sVals[ VAL_BAIXAMANUT.DTPAGTO.ordinal() ] = Funcoes.dateToStrDate( new Date() );
				sVals[ VAL_BAIXAMANUT.CODCC.ordinal() ] = "".equals( vCodCCs.elementAt( iLin ) ) ? sRelPlanPag[ 3 ] : vCodCCs.elementAt( iLin );

				if ( "".equals( ( (String) tabBaixa.getValor( iLin, 6 ) ).trim() ) ) {
					sVals[ VAL_BAIXAMANUT.VLRPAGO.ordinal() ] = (String) tabBaixa.getValor( iLin, 5 );
				} else {
					sVals[ VAL_BAIXAMANUT.CODTIPOCOB.ordinal() ] = (String) tabBaixa.getValor( iLin, 14 );
					sVals[ VAL_BAIXAMANUT.VLRPAGO.ordinal() ] = (String) tabBaixa.getValor( iLin, 7 );
				}
				sVals[VAL_BAIXAMANUT.OBS.ordinal() ] = (String) tabBaixa.getValor( iLin, enum_tab_baixa.OBS.ordinal() );
				sVals[VAL_BAIXAMANUT.CODCONTR.ordinal()] = (String) tabBaixa.getValor( iLin, enum_tab_baixa.CODCONTR.ordinal() );
				sVals[VAL_BAIXAMANUT.CODITCONTR.ordinal()] = (String) tabBaixa.getValor( iLin, enum_tab_baixa.CODITCONTR.ordinal() );

				dl.setValores( sVals );
				dl.setConexao( con );
				dl.setVisible( true );

				if ( dl.OK ) {

					sRets = dl.getValores();

					sSQL.append( "UPDATE FNITPAGAR " );
					sSQL.append( "SET NUMCONTA=?,CODEMPCA=?,CODFILIALCA=?,CODPLAN=?,CODEMPPN=?,CODFILIALPN=? " );
					sSQL.append( ",ANOCC=?,CODCC=?,CODEMPCC=?,CODFILIALCC=?,DOCLANCAITPAG =?,DTPAGOITPAG=?,VLRPAGOITPAG=? " );
					sSQL.append( ", OBSITPAG=?,STATUSITPAG='PP' " );
					sSQL.append( ", CODCONTR=?, CODITCONTR=?, CODEMPCT=?, CODFILIALCT=? " );
					sSQL.append( "WHERE CODPAG=? AND NPARCPAG=? AND CODEMP=? AND CODFILIAL=?" );

					try {

						int param = 1;
						ps = con.prepareStatement( sSQL.toString() );
						ps.setString( param++, sRets[ RET_BAIXA_PAG.CODCONTA.ordinal() ] );
						ps.setInt( param++, Aplicativo.iCodEmp );
						ps.setInt( param++, ListaCampos.getMasterFilial( "FNCONTA" ) );
						ps.setString( param++, sRets[ RET_BAIXA_PAG.CODPLAN.ordinal() ] );
						ps.setInt( param++, Aplicativo.iCodEmp );
						ps.setInt( param++, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );

						if ( "".equals( sRets[ RET_BAIXA_PAG.CODCC.ordinal() ].trim() ) ) {
							ps.setNull( param++, Types.INTEGER );
							ps.setNull( param++, Types.CHAR );
							ps.setNull( param++, Types.INTEGER );
							ps.setNull( param++, Types.INTEGER );
						} else {
							ps.setInt( param++, iAnoCC );
							ps.setString( param++, sRets[ RET_BAIXA_PAG.CODCC.ordinal() ] );
							ps.setInt( param++, Aplicativo.iCodEmp );
							ps.setInt( param++, ListaCampos.getMasterFilial( "FNCC" ) );
						}

						ps.setString( param++, sRets[ RET_BAIXA_PAG.DOC.ordinal() ] );
						ps.setDate( param++, Funcoes.strDateToSqlDate( sRets[ RET_BAIXA_PAG.DTPAGTO.ordinal() ] ) );
						ps.setBigDecimal( param++, ConversionFunctions.stringCurrencyToBigDecimal( sRets[ RET_BAIXA_PAG.VLRPAGO.ordinal() ] ) );
						ps.setString( param++, sRets[ RET_BAIXA_PAG.OBS.ordinal() ] );
						if ( "".equals( sRets[ RET_BAIXA_PAG.CODCONTR.ordinal() ].trim() ) 
								|| "".equals( sRets[ RET_BAIXA_PAG.CODITCONTR.ordinal() ].trim() )  ) {
							ps.setNull( param++, Types.INTEGER );
							ps.setNull( param++, Types.INTEGER );
							ps.setNull( param++, Types.INTEGER );
							ps.setNull( param++, Types.INTEGER );
						} else {
							ps.setInt( param++, new Integer(sRets[ RET_BAIXA_PAG.CODCONTR.ordinal()] ) );
							ps.setInt( param++, new Integer(sRets[ RET_BAIXA_PAG.CODITCONTR.ordinal()] ) );
							ps.setInt( param++, Aplicativo.iCodEmp );
							ps.setInt( param++, ListaCampos.getMasterFilial( "VDITCONTR" ) );
						}
						ps.setInt( param++, iCodPag );
						ps.setInt( param++, iNParcPag );
						ps.setInt( param++, Aplicativo.iCodEmp );
						ps.setInt( param++, ListaCampos.getMasterFilial( "FNPAGAR" ) );

						ps.executeUpdate();

						con.commit();
					} catch ( SQLException err ) {
						err.printStackTrace();
						Funcoes.mensagemErro( this, "Erro ao baixar parcela!\n" + err.getMessage(), true, con, err );
					}
				}

				carregaGridBaixa();
				dl.dispose();
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			ps = null;
			sSQL = null;
			sVals = null;
			sRelPlanPag = null;
			sRets = null;
			dl = null;
			imgStatusAt = null;
		}
	}

	private void baixaTabManut(){
		PreparedStatement ps = null;
		StringBuffer sSQL = new StringBuffer();
		String[] sVals = null;
		String[] sRelPlanPag = null;
		String[] sRets = null;
		DLBaixaPag dl = null;
		ImageIcon imgStatusAt = null;
		boolean lancaFinanceiroContrato = "S".equals( prefere.get( "lancafincontr" ) ); 

		BigDecimal valorTotalParc = new BigDecimal( 0 );
		BigDecimal valorTotalPag = new BigDecimal( 0 );
		BigDecimal vlrapagitpag = new BigDecimal(0);
		List<Integer> selecionados = new ArrayList<Integer>();

		for (int row = 0; row < tabManut.getNumLinhas(); row++) {

			Boolean selecionado = (Boolean)tabManut.getValor( row, enum_tab_manut.SEL.ordinal() ) ;

			if (selecionado){

				if( (ImageIcon) tabManut.getValor( row, enum_tab_manut.IMGSTATUS.ordinal() ) == imgPago){
					imgStatusAt = (ImageIcon) tabManut.getValor( row, enum_tab_manut.IMGSTATUS.ordinal() );
				}

				valorTotalParc = valorTotalParc.add( 
						ConversionFunctions.stringCurrencyToBigDecimal( 
								((StringDireita) tabManut.getValor( row, enum_tab_manut.VLRPARCITPAG.ordinal()) ).toString() ) );

				valorTotalPag = valorTotalPag.add( 
						ConversionFunctions.stringCurrencyToBigDecimal( 
								((StringDireita) tabManut.getValor( row, enum_tab_manut.VLRAPAGITPAG.ordinal()) ).toString() ) );

				vlrapagitpag = ConversionFunctions.stringCurrencyToBigDecimal( 
						((StringDireita) tabManut.getValor( row, enum_tab_manut.VLRAPAGITPAG.ordinal()) ).toString()  );

				selecionados.add( row );
			}
		}

		if(selecionados.size() == 0 ){

			if( tabManut.getSelectedRow() < 0 ) {
				Funcoes.mensagemInforma( this, "Selecione um título!" );
				return;
			} 

			int row = tabManut.getLinhaSel();
			imgStatusAt = (ImageIcon) tabManut.getValor( row, enum_tab_manut.IMGSTATUS.ordinal() );

			valorTotalParc = valorTotalParc.add( 
					ConversionFunctions.stringCurrencyToBigDecimal( 
							((StringDireita) tabManut.getValor( row, enum_tab_manut.VLRPARCITPAG.ordinal()) ).toString() ) );

			valorTotalPag = valorTotalPag.add( 
					ConversionFunctions.stringCurrencyToBigDecimal( 
							((StringDireita) tabManut.getValor( row, enum_tab_manut.VLRAPAGITPAG.ordinal()) ).toString() ) );

			vlrapagitpag = ConversionFunctions.stringCurrencyToBigDecimal( 
					((StringDireita) tabManut.getValor( row, enum_tab_manut.VLRAPAGITPAG.ordinal()) ).toString()  );

			selecionados.add(row);
		}

		if ( imgStatusAt == imgPago ) {
			if(selecionados.size() == 1){
				Funcoes.mensagemInforma( this, "A PARCELA JÁ FOI PAGA!" );
			}else{
				Funcoes.mensagemInforma( this, "A PARCELA(S) SELECIONADA(S) JÁ PAGA(S)!" );
			}
			return;
		}

		try{
			sVals = new String[ VAL_BAIXAMANUT.values().length ];

			try {
				sRelPlanPag = daoMovimento.buscaRelPlanPag( (Integer) tabManut.getValor( 
						selecionados.get( 0 ), enum_tab_manut.CODPAG.ordinal() )  );
			} catch ( SQLException err ) {
				err.printStackTrace();
				Funcoes.mensagemErro( this, "Erro ao buscar Conta!\n" + err.getMessage(), true, con, err );
			} 
			sRets = null;

			boolean categoriaRequerida = false;
			for(Integer row : selecionados){
				String codPlan = (String) tabManut.getValor( row , enum_tab_manut.CODPLAN.ordinal() );

				if(codPlan == null || codPlan.trim().length() == 0){
					categoriaRequerida = true;
				}
			}

			dl = new DLBaixaPag( this, selecionados.size() > 1, categoriaRequerida, lancaFinanceiroContrato );
			//	public static enum VAL_BAIXAMANUT {CODFOR, RAZFOR, CODCONTA, CODPLAN, DOC, DTEMIS, DTVENC, VLRPARC, DTPAGTO, VLRPAGO, CODCC, CODTIPOCOB, OBS}

			sVals[ VAL_BAIXAMANUT.CODFOR.ordinal() ] = (String) tabManut.getValor( selecionados.get( 0 ), enum_tab_manut.CODFOR.ordinal() ) ;
			sVals[ VAL_BAIXAMANUT.RAZFOR.ordinal() ] = (String) tabManut.getValor( selecionados.get( 0 ), enum_tab_manut.RAZFOR.ordinal() );

			sVals[ VAL_BAIXAMANUT.CODCONTA.ordinal() ] = "".equals( vNumContas.elementAt( selecionados.get( 0 ) ) ) ? sRelPlanPag[ 2 ] : vNumContas.elementAt( selecionados.get( 0 ) );				
			sVals[ VAL_BAIXAMANUT.CODPLAN.ordinal() ] = "".equals( vCodPlans.elementAt( selecionados.get( 0 ) ) ) ? sRelPlanPag[ 1 ] : vCodPlans.elementAt( selecionados.get( 0 ) );


			sVals[ VAL_BAIXAMANUT.DOC.ordinal() ] = "".equals( (String) tabManut.getValor( selecionados.get( 0 ), enum_tab_manut.DOC.ordinal() ) ) ? 
					(String) tabManut.getValor( selecionados.get( 0 ), enum_tab_manut.DOCCOMPRA.ordinal() ) : 
						(String) tabManut.getValor( selecionados.get( 0 ), enum_tab_manut.DOC.ordinal() );

					sVals[ VAL_BAIXAMANUT.DTEMIS.ordinal() ] = (String) tabManut.getValor( selecionados.get( 0 ), enum_tab_manut.DTVENCITPAG.ordinal() );
					sVals[ VAL_BAIXAMANUT.DTVENC.ordinal() ] = (String) tabManut.getValor( selecionados.get( 0 ), enum_tab_manut.DTVENCITPAG.ordinal() );

					sVals[ VAL_BAIXAMANUT.VLRPARC.ordinal() ] = Funcoes.bdToStr( valorTotalParc ).toString();
					sVals[ VAL_BAIXAMANUT.DTPAGTO.ordinal() ] = Funcoes.dateToStrDate( new Date() );
					sVals[ VAL_BAIXAMANUT.VLRPAGO.ordinal() ] = Funcoes.bdToStr( valorTotalPag ).toString();

					sVals[ VAL_BAIXAMANUT.CODCC.ordinal() ] = "".equals( vCodCCs.elementAt( selecionados.get( 0 ) ) ) ? sRelPlanPag[ 3 ] : vCodCCs.elementAt( selecionados.get( 0 ) );
					sVals[ VAL_BAIXAMANUT.CODTIPOCOB.ordinal() ] = (String) tabManut.getValor( selecionados.get( 0 ), enum_tab_manut.CODTIPOCOB.ordinal() );

					sVals[ VAL_BAIXAMANUT.OBS.ordinal() ] = (String) tabManut.getValor( selecionados.get( 0 ), enum_tab_manut.OBSITPAG.ordinal() );

					sVals[VAL_BAIXAMANUT.CODCONTR.ordinal()] = (String) tabManut.getValor( selecionados.get( 0 ), enum_tab_manut.CODCONTR.ordinal() );
					sVals[VAL_BAIXAMANUT.CODITCONTR.ordinal()] = (String) tabManut.getValor( selecionados.get( 0 ), enum_tab_manut.CODITCONTR.ordinal() );

					dl.setValores( sVals );
					dl.setConexao( con );
					dl.setVisible( true );

					if ( dl.OK ) {
						sRets = dl.getValores();
						boolean manterDados = false;
						if(sRets[ RET_BAIXA_PAG.CODPLAN.ordinal()].trim().length() > 0){
							for(Integer row : selecionados){
								String codCategoria = (String) tabManut.getValor( row , enum_tab_manut.CODPLAN.ordinal() );
								String codCC = (String) tabManut.getValor( row, enum_tab_manut.CODCC.ordinal() );
								if( ( (codCategoria != null) && (!"".equals(codCategoria.trim() ) ) ) ||
										( (codCC !=null) && (!"".equals( codCC.trim() ) ) ) ){
									if(selecionados.size() > 1){
										if(Funcoes.mensagemConfirma( this, "Há contas já categorizadas deseja manter as informações?") == JOptionPane.YES_OPTION)
											manterDados = true;
									}
									else {
										manterDados = true;
									}
									break;
								}
							}
						}else{
							manterDados = true;
						}

						sSQL.append( "UPDATE FNITPAGAR SET " );
						sSQL.append( "NUMCONTA=?,CODEMPCA=?,CODFILIALCA=?,CODPLAN=?,CODEMPPN=?,CODFILIALPN=?" );
						sSQL.append( ", DOCLANCAITPAG=?, DTPAGOITPAG=?, VLRDESCITPAG=?, VLRJUROSITPAG=?, VLRPAGOITPAG=?");
						sSQL.append( ", ANOCC=?,CODCC=?,CODEMPCC=?,CODFILIALCC=? " );
						sSQL.append( ", CODTIPOCOB=?,CODEMPTC=?,CODFILIALTC=?,OBSITPAG=?, MULTIBAIXA = ?, STATUSITPAG='PP' " );
						sSQL.append( ", CODCONTR=?, CODITCONTR=?, CODEMPCT=?, CODFILIALCT=? " );
						sSQL.append( "WHERE CODPAG=? AND NPARCPAG=? AND CODEMP=? AND CODFILIAL=?" );

						String multibaixa = (selecionados.size() > 1 ? "S" : "N");

						try {
							for(Integer row : selecionados){

								BigDecimal vlrdescitpag = new BigDecimal(0);
								BigDecimal vlrjurositpag = new BigDecimal(0);

								iCodPag = (Integer) tabManut.getValor( row, enum_tab_manut.CODPAG.ordinal()  );
								iNParcPag = (Integer) tabManut.getValor( row, enum_tab_manut.NPARCPAG.ordinal()  );

								ps = con.prepareStatement( sSQL.toString() );
								ps.setString( UPDATE_BAIXAMANUT_PARAMS.NUMCONTA.ordinal(), sRets[ RET_BAIXA_PAG.CODCONTA.ordinal() ] );
								ps.setInt( UPDATE_BAIXAMANUT_PARAMS.CODEMPCA.ordinal(), Aplicativo.iCodEmp );
								ps.setInt( UPDATE_BAIXAMANUT_PARAMS.CODFILIALCA.ordinal(), ListaCampos.getMasterFilial( "FNCONTA" ) );

								if(manterDados &&  
										( ((String) tabManut.getValor(row, enum_tab_manut.CODPLAN.ordinal()) ).trim().length() > 0 ) ){
									ps.setString( UPDATE_BAIXAMANUT_PARAMS.CODPLAN.ordinal(), (String) tabManut.getValor( row, enum_tab_manut.CODPLAN.ordinal() ) );
								}else{
									ps.setString( UPDATE_BAIXAMANUT_PARAMS.CODPLAN.ordinal(), sRets[ RET_BAIXA_PAG.CODPLAN.ordinal() ] );
								}

								ps.setInt( UPDATE_BAIXAMANUT_PARAMS.CODEMPPN.ordinal(), Aplicativo.iCodEmp );
								ps.setInt( UPDATE_BAIXAMANUT_PARAMS.CODFILIALPN.ordinal(), ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );

								ps.setDate( UPDATE_BAIXAMANUT_PARAMS.DTPAGOITPAG.ordinal(), Funcoes.strDateToSqlDate( sRets[ RET_BAIXA_PAG.DTPAGTO.ordinal() ] ) );

								if ( selecionados.size() == 1) {
									// Se não for multibaixa calcular juros
									ps.setString( UPDATE_BAIXAMANUT_PARAMS.DOCLANCAITPAG.ordinal(), sRets[ RET_BAIXA_PAG.DOC.ordinal() ] );
									BigDecimal vlrpagoitpag = ConversionFunctions.stringCurrencyToBigDecimal( sRets[ RET_BAIXA_PAG.VLRPAGO.ordinal() ] );
									if (vlrpagoitpag.compareTo( vlrapagitpag )>0) {
										vlrjurositpag = vlrpagoitpag.subtract( vlrapagitpag );
									}
									ps.setBigDecimal( UPDATE_BAIXAMANUT_PARAMS.VLRPAGOITPAG.ordinal(), vlrpagoitpag );
								} else{
									ps.setString( UPDATE_BAIXAMANUT_PARAMS.DOCLANCAITPAG.ordinal(), (String) tabManut.getValor( row, enum_tab_manut.DOC.ordinal()) );
									ps.setBigDecimal( UPDATE_BAIXAMANUT_PARAMS.VLRPAGOITPAG.ordinal(), ConversionFunctions.stringCurrencyToBigDecimal( 
											((StringDireita) tabManut.getValor( row, enum_tab_manut.VLRAPAGITPAG.ordinal()) ).toString() ) );
								}

								ps.setBigDecimal( UPDATE_BAIXAMANUT_PARAMS.VLRDESCITPAG.ordinal(), vlrdescitpag );
								ps.setBigDecimal(  UPDATE_BAIXAMANUT_PARAMS.VLRJUROSITPAG.ordinal(), vlrjurositpag );

								if ( "".equals( sRets[ RET_BAIXA_PAG.CODCC.ordinal() ].trim() ) ) {
									ps.setNull( UPDATE_BAIXAMANUT_PARAMS.ANOCC.ordinal(), Types.INTEGER );
									ps.setNull( UPDATE_BAIXAMANUT_PARAMS.CODCC.ordinal(), Types.CHAR );
									ps.setNull( UPDATE_BAIXAMANUT_PARAMS.CODEMPCC.ordinal(), Types.INTEGER );
									ps.setNull( UPDATE_BAIXAMANUT_PARAMS.CODFILIALCC.ordinal(), Types.INTEGER );
								} else {
									ps.setInt( UPDATE_BAIXAMANUT_PARAMS.ANOCC.ordinal(), iAnoCC );
									if( manterDados && !"".equals( (String ) tabManut.getValor( row, enum_tab_manut.CODCC.ordinal() ) ) ){
										ps.setString( UPDATE_BAIXAMANUT_PARAMS.CODCC.ordinal(), (String) tabManut.getValor( row, enum_tab_manut.CODCC.ordinal() ) );
									} else {
										ps.setString( UPDATE_BAIXAMANUT_PARAMS.CODCC.ordinal(), sRets[ RET_BAIXA_PAG.CODCC.ordinal() ] );
									}
									ps.setInt( UPDATE_BAIXAMANUT_PARAMS.CODEMPCC.ordinal(), Aplicativo.iCodEmp );
									ps.setInt( UPDATE_BAIXAMANUT_PARAMS.CODFILIALCC.ordinal(), ListaCampos.getMasterFilial( "FNCC" ) );
								}

								if ( "".equals( sRets[ RET_BAIXA_PAG.CODTIPOCOB.ordinal() ].trim() ) ) {
									ps.setNull( UPDATE_BAIXAMANUT_PARAMS.CODTIPOCOB.ordinal(), Types.INTEGER );
									ps.setNull( UPDATE_BAIXAMANUT_PARAMS.CODEMPTC.ordinal(), Types.INTEGER );
									ps.setNull( UPDATE_BAIXAMANUT_PARAMS.CODFILIALTC.ordinal(), Types.INTEGER );
								} else {
									ps.setInt( UPDATE_BAIXAMANUT_PARAMS.CODTIPOCOB.ordinal(), Integer.parseInt( sRets[ RET_BAIXA_PAG.CODTIPOCOB.ordinal() ] ) );
									ps.setInt( UPDATE_BAIXAMANUT_PARAMS.CODEMPTC.ordinal(), Aplicativo.iCodEmp );
									ps.setInt( UPDATE_BAIXAMANUT_PARAMS.CODFILIALTC.ordinal(), ListaCampos.getMasterFilial( "FNTIPOCOB" ) );
								}

								ps.setString( UPDATE_BAIXAMANUT_PARAMS.OBSITPAG.ordinal(), sRets[ RET_BAIXA_PAG.OBS.ordinal() ] );
								ps.setString( UPDATE_BAIXAMANUT_PARAMS.MULTIBAIXA.ordinal(), multibaixa );

								if ( "".equals( sRets[ RET_BAIXA_PAG.CODITCONTR.ordinal() ].trim() ) 
										|| "".equals( sRets[ RET_BAIXA_PAG.CODCONTR.ordinal() ].trim() ) ) {
									ps.setNull( UPDATE_BAIXAMANUT_PARAMS.CODCONTR.ordinal(), Types.INTEGER );
									ps.setNull( UPDATE_BAIXAMANUT_PARAMS.CODITCONTR.ordinal(), Types.INTEGER );
									ps.setNull( UPDATE_BAIXAMANUT_PARAMS.CODEMPCT.ordinal(), Types.INTEGER );
									ps.setNull( UPDATE_BAIXAMANUT_PARAMS.CODFILIALCT.ordinal(), Types.INTEGER );
								} else {
									ps.setInt( UPDATE_BAIXAMANUT_PARAMS.CODCONTR.ordinal(), Integer.parseInt( sRets[ RET_BAIXA_PAG.CODCONTR.ordinal() ] ) );
									ps.setInt( UPDATE_BAIXAMANUT_PARAMS.CODITCONTR.ordinal(), Integer.parseInt( sRets[ RET_BAIXA_PAG.CODITCONTR.ordinal() ] ) );
									ps.setInt( UPDATE_BAIXAMANUT_PARAMS.CODEMPCT.ordinal(), Aplicativo.iCodEmp );
									ps.setInt( UPDATE_BAIXAMANUT_PARAMS.CODFILIALCT.ordinal(), ListaCampos.getMasterFilial( "VDITCONTRATO" ) );
								}

								ps.setInt( UPDATE_BAIXAMANUT_PARAMS.CODPAG.ordinal(), iCodPag );
								ps.setInt( UPDATE_BAIXAMANUT_PARAMS.NPARCPAG.ordinal(), iNParcPag );
								ps.setInt( UPDATE_BAIXAMANUT_PARAMS.CODEMP.ordinal(), Aplicativo.iCodEmp );
								ps.setInt( UPDATE_BAIXAMANUT_PARAMS.CODFILIAL.ordinal(), ListaCampos.getMasterFilial( "FNPAGAR" ) );

								ps.executeUpdate();
							}

							this.geraLancamentosFinanceiros(selecionados, sRets, manterDados);

							con.commit();
						} catch ( SQLException err ) {
							try {
								con.rollback();
							} catch ( SQLException e ) {
								e.printStackTrace();
							}

							err.printStackTrace();
							Funcoes.mensagemErro( this, "Erro ao baixar parcela!\n" + err.getMessage(), true, con, err );
						}
					}

					dl.dispose();
					carregaGridManut();
		}finally{
			ps = null;
			sSQL = null;
			sVals = null;
			sRelPlanPag = null;
			sRets = null;
			dl = null;
			imgStatusAt = null;
		}
	}

	private void geraLancamentosFinanceiros(List<Integer> selecionados, String[] sRets, boolean manterDados) throws SQLException{
		if(selecionados.size() == 1){
			return;
		}

		int codpag = 0;
		int nparcpag = 0;
		BigDecimal vlrdescitpag = null;
		BigDecimal vlrjurositpag = null;
		String codplandr = ( (String) prefere.get( "codplandr" ) ).trim();
		String codplanjp = ( (String) prefere.get( "codplanjp" ) ).trim();
		Integer codcontr = null;
		Integer coditcontr = null;

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sqlLanca = new StringBuilder();

		//Recupera o Próximo Sequencial 
		ps = con.prepareStatement( "SELECT ISEQ FROM SPGERANUM(?, ?, 'LA') " );
		ps.setInt( 1, Aplicativo.iCodEmp );
		ps.setInt( 2, Aplicativo.iCodFilial );

		rs = ps.executeQuery();
		rs.next();
		int codlanca = rs.getInt( "ISEQ" );
		ps.close();
		//Recupera DataCompPag
		ps = con.prepareStatement( "SELECT DTCOMPPAG FROM FNPAGAR WHERE CODEMP = ? AND CODFILIAL = ? AND CODPAG = ?");
		ps.setInt( 1, Aplicativo.iCodEmp );
		ps.setInt( 2, Aplicativo.iCodFilial );
		ps.setInt( 3,  (Integer) tabManut.getValor( selecionados.get( 0 ) , enum_tab_manut.CODPAG.ordinal()) ) ;

		rs = ps.executeQuery();
		rs.next();
		Date dtCompLanca = rs.getDate( 1 );
		ps.close();
		//Recupera Plano De Contas
		ps = con.prepareStatement( "SELECT CODPLAN,CODEMP,CODFILIAL FROM FNCONTA WHERE NUMCONTA= ? AND CODEMP = ? AND CODFILIAL = ?" );
		ps.setString( 1, sRets[ 0 ] );
		ps.setInt( 2, Aplicativo.iCodEmp );
		ps.setInt( 3, Aplicativo.iCodFilial );

		rs = ps.executeQuery();
		rs.next();
		String codPlan = rs.getString( "CODPLAN" );
		int codEmpPlan = rs.getInt( "CODEMP" );
		int codFilialPlan = rs.getInt( "CODFILIAL" );
		ps.close();

		sqlLanca.append("INSERT INTO FNLANCA (TIPOLANCA,CODEMP,CODFILIAL,CODLANCA, ");
		sqlLanca.append("CODEMPPN,CODFILIALPN,CODPLAN,DTCOMPLANCA,DATALANCA,DOCLANCA, ");
		sqlLanca.append("HISTBLANCA,DTPREVLANCA, CODEMPPN,CODFILIALPN,CODPLAN, VLRLANCA) ");
		sqlLanca.append("VALUES ('F', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0 )");

		ps = con.prepareStatement( sqlLanca.toString() );
		ps.setInt( 1, Aplicativo.iCodEmp );
		ps.setInt( 2, Aplicativo.iCodFilial );
		ps.setInt( 3, codlanca );

		ps.setInt( 4, Aplicativo.iCodEmp );
		ps.setInt( 5, ListaCampos.getMasterFilial( "FNCONTA" ) );
		ps.setString( 6, sRets[1] );

		ps.setDate( 7, Funcoes.dateToSQLDate( dtCompLanca ) );
		ps.setDate( 8, Funcoes.strDateToSqlDate( sRets[ 3 ] ) );

		ps.setString( 9, sRets[ 2 ] );
		ps.setString( 10, sRets[ 7 ] );

		ps.setDate( 11, Funcoes.strDateToSqlDate( sRets[ 3 ] ) );

		ps.setInt( 12, codEmpPlan );
		ps.setInt( 13, codFilialPlan );
		ps.setString( 14, codPlan );

		ps.executeUpdate();


		int codsublanca = 1;
		for(Integer row : selecionados){
			codpag = (Integer) tabManut.getValor( row, enum_tab_manut.CODPAG.ordinal() );
			nparcpag = (Integer) tabManut.getValor( row, enum_tab_manut.NPARCPAG.ordinal() );
			vlrdescitpag = ConversionFunctions.stringCurrencyToBigDecimal( 
					((StringDireita) tabManut.getValor( row , enum_tab_manut.VLRDESCITPAG.ordinal()) ).toString() ); 
			vlrjurositpag = ConversionFunctions.stringCurrencyToBigDecimal( 
					((StringDireita) tabManut.getValor( row , enum_tab_manut.VLRJUROSITPAG.ordinal()) ).toString() ); 

			String codplan = null;
			String codcc = null;

			if(manterDados && 
					((String) tabManut.getValor( row , enum_tab_manut.CODPLAN.ordinal())).trim().length() > 0){
				codplan = (String) tabManut.getValor( row , enum_tab_manut.CODPLAN.ordinal() );
			}else{
				codplan = sRets[1];
			}
			Integer codfor = Integer.parseInt( (String) tabManut.getValor( row , enum_tab_manut.CODFOR.ordinal() ) );

			if(manterDados && 
					!"".equals( (String) tabManut.getValor( row , enum_tab_manut.CODCC.ordinal() ) ) ){
				codcc  = (String) tabManut.getValor( row , enum_tab_manut.CODCC.ordinal() );
			}else{
				codcc = sRets[ 5 ].trim();
			}				
			String dtitpag = (String) tabManut.getValor( row , enum_tab_manut.DTITPAG.ordinal() );
			String datasublanca = sRets[ 3 ];
			String dtprevsublanca = sRets[ 3 ];
			BigDecimal vlrsublanca = ConversionFunctions.stringCurrencyToBigDecimal( 
					((StringDireita) tabManut.getValor( row , enum_tab_manut.VLRAPAGITPAG.ordinal()) ).toString() ); 
			vlrsublanca = vlrsublanca.add( vlrdescitpag ).subtract( vlrjurositpag );
			if ("".equals( tabManut.getValor( row , enum_tab_manut.CODCONTR.ordinal() ) ) ) {
				codcontr = null;
			} else {
				codcontr = new Integer((String)tabManut.getValor( row , enum_tab_manut.CODCONTR.ordinal() ));
			}
			if ("".equals( tabManut.getValor( row , enum_tab_manut.CODITCONTR.ordinal() ) ) ) {
				coditcontr = null;
			} else {
				coditcontr = new Integer((String)tabManut.getValor( row , enum_tab_manut.CODITCONTR.ordinal() ));
			}
			daoMovimento.gerarSublanca(codpag, nparcpag, codlanca, codsublanca, codplan, codfor, codcc, 
					dtitpag, datasublanca, dtprevsublanca, vlrsublanca, "P", codcontr, coditcontr, iAnoCC);

			if(vlrdescitpag.compareTo( new BigDecimal( 0 ) ) > 0 ) {	
				codsublanca++;
				vlrsublanca = ConversionFunctions.stringCurrencyToBigDecimal( 
						((StringDireita) tabManut.getValor( row , enum_tab_manut.VLRDESCITPAG.ordinal()) ).toString() ).multiply( new BigDecimal(-1) ); 
				if( !"".equals( codplandr ) ){
					codplan = codplandr;
				}
				daoMovimento.gerarSublanca(codpag, nparcpag, codlanca, codsublanca, codplan, codfor, 
						codcc, dtitpag, datasublanca, dtprevsublanca, vlrsublanca, "D", codcontr, coditcontr, iAnoCC);

			}

			if(vlrjurositpag.compareTo( new BigDecimal(0) ) > 0 ) {	
				codsublanca++;
				vlrsublanca = ConversionFunctions.stringCurrencyToBigDecimal( 
						((StringDireita) tabManut.getValor( row , enum_tab_manut.VLRJUROSITPAG.ordinal()) ).toString() );
				if(!"".equals( codplanjp ) ) {
					codplan = (String) prefere.get( "codplanjp" );
				}
				daoMovimento.gerarSublanca(codpag, nparcpag, codlanca, codsublanca, codplan, codfor, codcc, dtitpag, 
						datasublanca, dtprevsublanca, vlrsublanca, "J", codcontr, coditcontr, iAnoCC);

			}

			codsublanca++;
		}

	}

	/*private void geraSublanca(Integer codpag, Integer nparcpag, Integer codlanca, Integer codsublanca, String codplan, Integer codfor, 
				String codcc, String dtitpag, String datasublanca, String dtprevsublanca, BigDecimal vlrsublanca, String tiposublanca
				, Integer codcontr, Integer coditcontr) throws SQLException {
			PreparedStatement ps = null;
			StringBuilder sqlSubLanca = new StringBuilder();
			sqlSubLanca.append( "INSERT INTO FNSUBLANCA (CODEMP,CODFILIAL,CODLANCA,CODSUBLANCA,CODEMPFR,CODFILIALFR,CODFOR,CODEMPPN,CODFILIALPN, CODPLAN, ");
			sqlSubLanca.append( "CODEMPPG, CODFILIALPG, CODPAG, NPARCPAG," );
			sqlSubLanca.append( "CODEMPCC,CODFILIALCC,ANOCC,CODCC,ORIGSUBLANCA,DTCOMPSUBLANCA,DATASUBLANCA,DTPREVSUBLANCA,VLRSUBLANCA, TIPOSUBLANCA");
			sqlSubLanca.append( ", CODEMPCT, CODFILIALCT, CODCONTR, CODITCONTR) ");
			sqlSubLanca.append( "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,'E', ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			ps = con.prepareStatement( sqlSubLanca.toString() );

			int param = 1;

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "FNSUBLANCA" ) );
			ps.setInt( param++, codlanca );
			ps.setInt( param++, codsublanca );

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "CPFORNECED" ) );
			ps.setInt( param++, codfor );

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
			ps.setString( param++, codplan );
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
			ps.setInt( param++, codpag );
			ps.setInt( param++, nparcpag );


			if ( "".equals( codcc ) ) {
				ps.setNull( param++, Types.INTEGER );
				ps.setNull( param++, Types.INTEGER );
				ps.setNull( param++, Types.CHAR );
				ps.setNull( param++, Types.INTEGER );
			} else {
				ps.setInt( param++, Aplicativo.iCodEmp );
				ps.setInt( param++, ListaCampos.getMasterFilial( "FNCC" ) );
				ps.setInt( param++, iAnoCC );
				ps.setString( param++, codcc );
			}

			ps.setDate( param++, Funcoes.dateToSQLDate( 
					ConversionFunctions.strDateToDate( dtitpag ) )  ) ;

			ps.setDate( param++, Funcoes.strDateToSqlDate( datasublanca ) );
			ps.setDate( param++, Funcoes.strDateToSqlDate( dtprevsublanca) );

			ps.setBigDecimal( param++, vlrsublanca );
			ps.setString( param++, tiposublanca );

			if ( codcontr==null || coditcontr==null ) {
				ps.setNull( param++, Types.INTEGER );
				ps.setNull( param++, Types.INTEGER );
				ps.setNull( param++, Types.INTEGER );
				ps.setNull( param++, Types.INTEGER );
			} else {
				ps.setInt( param++, Aplicativo.iCodEmp );
				ps.setInt( param++, ListaCampos.getMasterFilial( "VDITCONTRATO" ) );
				ps.setInt( param++, codcontr );
				ps.setInt( param++, coditcontr );
			}

			ps.executeUpdate();

		}*/

	private void editar() {

		PreparedStatement ps = null;
		StringBuffer sql = new StringBuffer();
		Object[] sVals = null;
		Object[] sRets = null;
		DLEditaPag dl = null;
		ImageIcon imgStatusAt = null;
		int iLin;
		boolean lancafincontr = "S".equals( prefere.get( "lancafincontr" ) ); 
		Integer codhistpag = null;

		try {

			if ( tabManut.getLinhaSel() > -1 ) {

				codhistpag = (Integer) prefere.get( "codhistpag" );

				imgStatusAt = (ImageIcon) tabManut.getValor( tabManut.getLinhaSel(), enum_tab_manut.IMGSTATUS.ordinal() );

				iLin = tabManut.getLinhaSel();

				iCodPag =  (Integer) tabManut.getValor( iLin, enum_tab_manut.CODPAG.ordinal() );
				iNParcPag = (Integer) tabManut.getValor( iLin, enum_tab_manut.NPARCPAG.ordinal() );

				sVals = new Object[ EDIT_PAG_SETVALORES.values().length ];

				dl = new DLEditaPag( this, imgStatusAt != imgPago, lancafincontr );

				sVals[ EDIT_PAG_SETVALORES.CODFOR.ordinal() ] = (String) tabManut.getValor( iLin, enum_tab_manut.CODFOR.ordinal() ).toString();
				sVals[ EDIT_PAG_SETVALORES.RAZFOR.ordinal() ] = (String) tabManut.getValor( iLin, enum_tab_manut.RAZFOR.ordinal() ).toString();

				sVals[ EDIT_PAG_SETVALORES.CODCONTA.ordinal() ] = (String) tabManut.getValor( iLin, enum_tab_manut.NUMCONTA.ordinal() ).toString();
				sVals[ EDIT_PAG_SETVALORES.CODPLAN.ordinal() ] = (String) tabManut.getValor( iLin, enum_tab_manut.CODPLAN.ordinal() ).toString();
				sVals[ EDIT_PAG_SETVALORES.CODCC.ordinal() ] = (String) tabManut.getValor( iLin, enum_tab_manut.CODCC.ordinal() ).toString();

				sVals[ EDIT_PAG_SETVALORES.DOC.ordinal() ] = (String) tabManut.getValor( iLin, enum_tab_manut.DOC.ordinal() ).toString();
				sVals[ EDIT_PAG_SETVALORES.DTEMIS.ordinal() ] = (String) tabManut.getValor( iLin, enum_tab_manut.DTITPAG.ordinal() ).toString();					
				sVals[ EDIT_PAG_SETVALORES.DTVENC.ordinal() ] = (String) tabManut.getValor( iLin, enum_tab_manut.DTVENCITPAG.ordinal() ).toString();

				sVals[ EDIT_PAG_SETVALORES.VLRPARC.ordinal() ] = 	( (StringDireita) tabManut.getValor( iLin, enum_tab_manut.VLRPARCITPAG.ordinal() )).toString();
				sVals[ EDIT_PAG_SETVALORES.VLRJUROS.ordinal() ] = 	( (StringDireita) tabManut.getValor( iLin, enum_tab_manut.VLRJUROSITPAG.ordinal() )).toString();
				sVals[ EDIT_PAG_SETVALORES.VLRDESC.ordinal() ] = 	( (StringDireita) tabManut.getValor( iLin, enum_tab_manut.VLRDESCITPAG.ordinal() )).toString(); 
				sVals[ EDIT_PAG_SETVALORES.VLRADIC.ordinal() ] = 	( (StringDireita) tabManut.getValor( iLin, enum_tab_manut.VLRADICITPAG.ordinal() )).toString(); 

				sVals[ EDIT_PAG_SETVALORES.OBS.ordinal() ] = (String) tabManut.getValor( iLin, enum_tab_manut.OBSITPAG.ordinal() ).toString();
				sVals[ EDIT_PAG_SETVALORES.CODTIPOCOB.ordinal() ] = (String) tabManut.getValor( iLin, enum_tab_manut.CODTIPOCOB.ordinal() ).toString();
				sVals[ EDIT_PAG_SETVALORES.VLRDEV.ordinal() ] = (String) tabManut.getValor( iLin, enum_tab_manut.VLRDEVITPAG.ordinal() ).toString();

				// Cod. pagar e nparc para carregar lista de cheques
				sVals[ EDIT_PAG_SETVALORES.CODPAG.ordinal() ] = (Integer) tabManut.getValor( iLin, enum_tab_manut.CODPAG.ordinal() );
				sVals[ EDIT_PAG_SETVALORES.NPARCPAG.ordinal() ] = (Integer) tabManut.getValor( iLin, enum_tab_manut.NPARCPAG.ordinal() );

				sVals[ EDIT_PAG_SETVALORES.CODCONTR.ordinal() ] =  (String) tabManut.getValor( iLin, enum_tab_manut.CODCONTR.ordinal() );
				sVals[ EDIT_PAG_SETVALORES.CODITCONTR.ordinal() ] = (String) tabManut.getValor( iLin, enum_tab_manut.CODITCONTR.ordinal() );

				// Se o doccompra estiver em branco getvalor(8) quer dizer que o lançamento foi feito pelo usuário.
				dl.setValores( sVals, "".equals( tabManut.getValor( iLin, enum_tab_manut.DOCCOMPRA.ordinal() ).toString().trim() ) );

				dl.setConexao( con );
				dl.setVisible( true );

				if ( dl.OK && imgStatusAt != imgPago) {

					try {
						dl.salvaPag();
					} catch ( Exception err ) {
						Funcoes.mensagemErro( this, "Erro ao editar parcela!\n" + err.getMessage(), true, con, err );
						err.printStackTrace();
					}

					dl.dispose();
					carregaGridManut();
				}
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			ps = null;
			sql = null;
			sVals = null;
			sRets = null;
			dl = null;
			imgStatusAt = null;
		}
	}

	private void novo() {

		DLNovoPag dl = new DLNovoPag( this );
		dl.setConexao( con );
		dl.setVisible( true );
		dl.dispose();
		carregaGridManut();
	}

	private void excluir() {

		PreparedStatement ps = null;
		ImageIcon imgStatusAt = null;

		try {

			if ( tabManut.getLinhaSel() > -1 ) {

				imgStatusAt = (ImageIcon) tabManut.getValor( tabManut.getLinhaSel(), enum_tab_manut.IMGSTATUS.ordinal() );

				if ( ! ( imgStatusAt == imgPagoParcial || imgStatusAt == imgPago ) ) {

					if ( Funcoes.mensagemConfirma( this, "Deseja realmente excluir esta conta e todas as suas parcelas?" ) == 0 ) {

						try {

							ps = con.prepareStatement( "DELETE FROM FNPAGAR WHERE CODPAG=? AND CODEMP=? AND CODFILIAL=?" );
							ps.setInt( 1, (Integer) tabManut.getValor( tabManut.getLinhaSel(), enum_tab_manut.CODPAG.ordinal() ) );
							ps.setInt( 2, Aplicativo.iCodEmp );
							ps.setInt( 3, ListaCampos.getMasterFilial( "FNPAGAR" ) );

							ps.executeUpdate();

							con.commit();

							carregaGridManut();

						} catch ( SQLException err ) {
							err.printStackTrace();
							Funcoes.mensagemErro( this, "Erro ao excluir parcela!\n" + err.getMessage(), true, con, err );
						}
					}
				}
				else {
					Funcoes.mensagemErro( this, "NÃO FOI POSSIVEL EXCLUIR.\n" + "A PARCELA JÁ FOI PAGA." );
				}
			}
			else {
				Funcoes.mensagemInforma( this, "Nenhum item foi selecionado." );
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			ps = null;
			imgStatusAt = null;
		}
	}

	private void estorno() {

		ResultSet rs = null;
		PreparedStatement ps = null;
		ImageIcon imgStatusAt = null;
		StringBuilder sqlUpdate = null;

		try {

			if ( tabManut.getLinhaSel() > -1 ) {

				imgStatusAt = (ImageIcon) tabManut.getValor( tabManut.getLinhaSel(), enum_tab_manut.IMGSTATUS.ordinal() );

				if ( imgStatusAt == imgPagoParcial || imgStatusAt == imgPago || imgStatusAt == imgPagoComParciais) {

					int iLin = tabManut.getLinhaSel();

					iCodPag = (Integer) tabManut.getValor( iLin, enum_tab_manut.CODPAG.ordinal() );
					iNParcPag = (Integer) tabManut.getValor( iLin, enum_tab_manut.NPARCPAG.ordinal() );

					try {



						List<Integer> selecionados = null;

						StringBuilder sqlLanca = new StringBuilder();
						sqlLanca.append( "SELECT COUNT (CODLANCA) FROM FNLANCA ");
						sqlLanca.append( "WHERE CODPAG = ? AND NPARCPAG = ? ");
						sqlLanca.append( "AND CODEMPPG = ? AND CODFILIALPG = ? ");
						sqlLanca.append( "AND CODEMP = ? AND CODFILIAL = ? ");

						ps = con.prepareStatement( sqlLanca.toString() );
						ps.setInt( 1, iCodPag );
						ps.setInt( 2, iNParcPag );
						ps.setInt( 3, Aplicativo.iCodEmp );
						ps.setInt( 4, ListaCampos.getMasterFilial( "FNRECEBER" ) );
						ps.setInt( 5, Aplicativo.iCodEmp );
						ps.setInt( 6, ListaCampos.getMasterFilial( "FNLANCA" ) );

						rs = ps.executeQuery();

						int countLanca = 0;
						if(rs.next()){
							countLanca = rs.getInt( 1 );
							if(countLanca > 1){
								selecionados = this.estornoMultiplaBaixa( iCodPag, iNParcPag );
							}
						}

						String statusItPag = "";
						if(selecionados != null && selecionados.size() >= 1 &&
								selecionados.size() != countLanca){
							statusItPag = PAGAMENTO_PARCIAL;
						}else if (selecionados == null || selecionados.size() == countLanca){
							if(countLanca <= 1){
								if ( Funcoes.mensagemConfirma( this, "Confirma o estorno do lançamento?" ) == JOptionPane.YES_OPTION ) {
									statusItPag = "P1";
								}
							}else{
								statusItPag = "P1";
							}
						}

						if( "P1".equals( statusItPag ) ){
							sqlUpdate = new StringBuilder();
							sqlUpdate.append( "UPDATE FNITPAGAR SET STATUSITPAG='");
							sqlUpdate.append( statusItPag );
							sqlUpdate.append( "' WHERE CODPAG = ? AND NPARCPAG = ? AND CODEMP = ? AND CODFILIAL = ?" );

							ps = con.prepareStatement( sqlUpdate.toString() );
							ps.setInt( 1, iCodPag );
							ps.setInt( 2, iNParcPag );
							ps.setInt( 3, Aplicativo.iCodEmp );
							ps.setInt( 4, ListaCampos.getMasterFilial( "FNPAGAR" ) );
							ps.executeUpdate();
						}

						if( PAGAMENTO_PARCIAL.equals( statusItPag ) ){
							StringBuilder sqlSelectSaldo = new StringBuilder();
							sqlSelectSaldo.append( "SELECT SUM(L.VLRLANCA)  AS SALDO   FROM FNITPAGAR IP ");
							sqlSelectSaldo.append( "INNER JOIN FNLANCA L ON (L.CODEMP = IP.CODEMP AND L.CODFILIAL = IP.CODFILIAL AND ");
							sqlSelectSaldo.append( "L.CODPAG = IP.CODPAG AND L.NPARCPAG = IP.NPARCPAG) ");
							sqlSelectSaldo.append( "WHERE IP.CODPAG = ? AND IP.NPARCPAG = ? ");
							sqlSelectSaldo.append( "AND IP.CODEMP = ? AND IP.CODFILIAL = ? ");

							BigDecimal saldo = new BigDecimal( 0 );

							//Recupera o SALDO antes de excluir os lançamentos para fazer o calculo.
							ps = con.prepareStatement( sqlSelectSaldo.toString() );
							ps.setInt( 1, iCodPag );
							ps.setInt( 2, iNParcPag );
							ps.setInt( 3, Aplicativo.iCodEmp );
							ps.setInt( 4, ListaCampos.getMasterFilial( "FNPAGAR" ) );
							rs = ps.executeQuery();

							if(rs.next()){
								saldo = rs.getBigDecimal( 1 );
							}

							//Excluir os lançamentos selecionados	
							StringBuilder sqlDeleteLanca = new StringBuilder();
							sqlDeleteLanca.append( "DELETE FROM FNLANCA WHERE CODPAG = ? AND NPARCPAG = ? ");
							sqlDeleteLanca.append( "AND CODEMPPG= ? AND CODFILIALPG = ? ");
							sqlDeleteLanca.append( "AND CODEMP = ? AND CODFILIAL = ? AND CODLANCA = ?");
							for(Integer codLanca : selecionados){
								ps = con.prepareStatement( sqlDeleteLanca.toString() );
								ps.setInt( 1, iCodPag );
								ps.setInt( 2, iNParcPag );
								ps.setInt( 3, Aplicativo.iCodEmp );
								ps.setInt( 4, ListaCampos.getMasterFilial( "FNPAGAR" ) );
								ps.setInt( 5, Aplicativo.iCodEmp );
								ps.setInt( 6, ListaCampos.getMasterFilial( "FNLANCA" ) );
								ps.setInt( 7, codLanca );
								ps.executeUpdate();
							}

							//Recupera saldo após exlcuir lançamentos 
							ps = con.prepareStatement( sqlSelectSaldo.toString() );
							ps.setInt( 1, iCodPag );
							ps.setInt( 2, iNParcPag );
							ps.setInt( 3, Aplicativo.iCodEmp );
							ps.setInt( 4, ListaCampos.getMasterFilial( "FNPAGAR" ) );
							rs = ps.executeQuery();
							//Calcula diferença entre os saldos para ver qual o valor pago.
							if(rs.next()){
								saldo = saldo.subtract( rs.getBigDecimal( 1 ) );
							}

							sqlUpdate = new StringBuilder();
							sqlUpdate.append( "UPDATE FNITPAGAR SET STATUSITPAG = ?, VLRPAGOITPAG= ? " );
							sqlUpdate.append( "WHERE CODPAG=? AND NPARCPAG=? AND CODEMP=? AND CODFILIAL=? ");
							ps = con.prepareStatement( sqlUpdate.toString() );
							ps.setString( 1, statusItPag );
							ps.setBigDecimal( 2, saldo );
							ps.setInt( 3, iCodPag );
							ps.setInt( 4, iNParcPag );
							ps.setInt( 5, Aplicativo.iCodEmp );
							ps.setInt( 6, ListaCampos.getMasterFilial( "FNPAGAR" ) );
							ps.executeUpdate();
						}

						con.commit();
					} catch ( SQLException err ) {
						err.printStackTrace();
						Funcoes.mensagemErro( this, "Erro ao estornar registro!\n" + err.getMessage(), true, con, err );
					}

					carregaGridManut();
				}
				else {
					Funcoes.mensagemInforma( this, "PARCELA AINDA NÃO FOI PAGA." );
				}
			}
			else {
				Funcoes.mensagemInforma( this, "Nenhum item foi selecionado." );
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			ps = null;
			imgStatusAt = null;
		}
	}

	private List<Integer> estornoMultiplaBaixa(Integer codPag, Integer nParcPag) throws SQLException{
		Integer rowSelected = tabManut.getLinhaSel();

		DLEstornoMultiplaBaixaPagamento dl = new DLEstornoMultiplaBaixaPagamento( this, con, codPag, nParcPag );
		dl.setValores( new BigDecimal[] { ConversionFunctions.stringToBigDecimal( tabManut.getValor( rowSelected, enum_tab_manut.VLRPARCITPAG.ordinal() ) ), 
				ConversionFunctions.stringToBigDecimal( tabManut.getValor( rowSelected, enum_tab_manut.VLRPAGOITPAG.ordinal() ) ), 
				ConversionFunctions.stringToBigDecimal( tabManut.getValor( rowSelected, enum_tab_manut.VLRDESCITPAG.ordinal() ) ), 
				ConversionFunctions.stringToBigDecimal( tabManut.getValor( rowSelected, enum_tab_manut.VLRJUROSITPAG.ordinal() ) ), 
				ConversionFunctions.stringToBigDecimal( tabManut.getValor( rowSelected, enum_tab_manut.VLRAPAGITPAG.ordinal() ) )  } );
		dl.setVisible( true );

		List<Integer> selecionados = new ArrayList<Integer>();
		selecionados = dl.getSelecionados();

		dl.dispose();
		return selecionados;
	}

	private void consBaixa( int codPag, int nparcItPag, BigDecimal vlrParc, BigDecimal vlrPago, BigDecimal vlrDesc, BigDecimal vlrJuros, BigDecimal vlrApag ) {

		DLConsultaBaixaPagamento dl = new DLConsultaBaixaPagamento( this, con, codPag, nparcItPag );

		dl.setValores( new BigDecimal[] { vlrParc, vlrPago, vlrDesc, vlrJuros, vlrApag } );

		dl.setVisible( true );
		dl.dispose();
	}

	private boolean validaPeriodo() {

		boolean bRetorno = false;

		if ( txtDatainiManut.getText().trim().length() < 10 ) {

			Funcoes.mensagemInforma( this, "Data inicial inválida!" );
		}
		else if ( txtDatafimManut.getText().trim().length() < 10 ) {

			Funcoes.mensagemInforma( this, "Data final inválida!" );
		}
		else if ( txtDatafimManut.getVlrDate().before( txtDatainiManut.getVlrDate() ) ) {

			Funcoes.mensagemInforma( this, "Data inicial maior que a data final!" );
		}
		else {

			dIniManut = txtDatainiManut.getVlrDate();
			dFimManut = txtDatafimManut.getVlrDate();
			bRetorno = true;
		}

		return bRetorno;
	}

	/*private String[] buscaRelPlanPag( int iCodPag ) {

			PreparedStatement ps = null;
			ResultSet rs = null;
			StringBuffer sSQL = new StringBuffer();
			String[] retorno = new String[ 4 ];

			try {

				sSQL.append( " SELECT C.CODPLANOPAG, PP.CODPLAN, PP.NUMCONTA, PP.CODCC " );
				sSQL.append( "FROM CPCOMPRA C, FNPLANOPAG PP, FNPAGAR P " );
				sSQL.append( "WHERE C.CODEMPPG=PP.CODEMP AND C.CODFILIALPG=PP.CODFILIAL AND C.CODPLANOPAG=PP.CODPLANOPAG " );
				sSQL.append( "AND C.CODEMP=P.CODEMPCP AND C.CODFILIAL=P.CODFILIALCP AND C.CODCOMPRA=P.CODCOMPRA " );
				sSQL.append( "AND P.CODEMP=? AND P.CODFILIAL=? AND P.CODPAG=?" );

				ps = con.prepareStatement( sSQL.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "FNPAGAR" ) );
				ps.setInt( 3, iCodPag );

				rs = ps.executeQuery();

				if ( rs.next() ) {

					for ( int i = 0; i < retorno.length; i++ ) {

						retorno[ i ] = rs.getString( i + 1 ) == null ? "" : rs.getString( i + 1 );
					}
				}

				rs.close();
				ps.close();

				con.commit();
			} catch ( SQLException err ) {
				err.printStackTrace();
				Funcoes.mensagemErro( this, "Erro ao buscar Conta!\n" + err.getMessage(), true, con, err );
			} finally {
				ps = null;
				rs = null;
				sSQL = null;
			}

			return retorno;
		}*/


	/*private Map<String, Object> getPrefere() {

			PreparedStatement ps = null;
			ResultSet rs = null;
			Integer anocc = null;
			Integer codhistpag = null;
			String codplandr = null;
			String codplanjp = null;
			String lancafincontr = null;

			Map<String, Object> retorno = new HashMap<String, Object>();

			try {

				ps = con.prepareStatement( "SELECT ANOCENTROCUSTO,CODHISTPAG, CODPLANJP, CODPLANDR, LANCAFINCONTR FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?" );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

				rs = ps.executeQuery();

				if ( rs.next() ) {
					anocc = rs.getInt( "ANOCENTROCUSTO" );
					codhistpag = rs.getInt( "CODHISTPAG" );
					codplanjp =  getString( rs.getString( "CODPLANJP" ) );
					codplandr =  getString( rs.getString( "CODPLANDR" ) );
					lancafincontr = getString( rs.getString( "LANCAFINCONTR" ));
				}

				retorno.put( "codhistpag", codhistpag );
				retorno.put( "anocc", anocc );
				retorno.put( "codplanjp", getString( codplanjp )  );
				retorno.put( "codplandr", getString( codplandr )  );
				retorno.put( "lancafincontr", getString( lancafincontr ) );

				rs.close();
				ps.close();

				con.commit();
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao buscar o ano-base para o centro de custo.\n" + err.getMessage(), true, con, err );
			} finally {
				ps = null;
				rs = null;
			}
			return retorno;
		}*/

	public void beforeCarrega( CarregaEvent cevt ) { }

	public void afterCarrega( CarregaEvent cevt ) {

		tabBaixa.limpa();
		carregaGridBaixa();

		if ( cevt.getListaCampos() == lcFor ) {

			carregaConsulta();
		}
		if ( cevt.getListaCampos() == lcPagManut ) {
			carregaGridManut();
			txtNumCheque.setVlrString( "" );
		}
		if ( cevt.getListaCampos() == lcSinal ) {

			if(txtCorSinal.getVlrInteger()>0 || txtCorSinal.getVlrInteger()<0) {
				txtCodSinal.setBackground( new Color(txtCorSinal.getVlrInteger()) ) ;
			}
			else {
				txtCodSinal.setBackground( Color.WHITE );
			}
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btSair ) {
			dispose();
		}
		else if ( evt.getSource() == btBaixaConsulta ) {
			baixaConsulta();
		}
		else if ( evt.getSource() == btBaixa ) {
			baixar( 'B' );
		}
		else if ( evt.getSource() == btBaixaManut ) {
			baixar( 'M' );
		}
		else if ( evt.getSource() == btEditManut ) {
			editar();
		}
		else if ( evt.getSource() == btNovoManut ) {
			novo();
		}
		else if ( evt.getSource() == btExcluirManut ) {
			excluir();
		}
		else if ( evt.getSource() == btExecManut ) {
			carregaGridManut();
		}
		else if ( evt.getSource() == btEstManut ) {
			estorno();
		}
		else if ( evt.getSource() == btCancItem ) {
			cancelaItem();
		}
		else if ( evt.getSource() == btImpRec ) {
			impRecibo();
		}
		else if ( evt.getSource() == btCarregaBaixasMan){
			if ( tabManut.getLinhaSel() > -1 ) {
				int rowSelected = tabManut.getLinhaSel();
				consBaixa( (Integer) tabManut.getValor( rowSelected, enum_tab_manut.CODPAG.ordinal() ), 
						(Integer) tabManut.getValor( rowSelected, enum_tab_manut.NPARCPAG.ordinal() ), 
						ConversionFunctions.stringToBigDecimal( tabManut.getValor( rowSelected, enum_tab_manut.VLRPARCITPAG.ordinal() ) ), 
						ConversionFunctions.stringToBigDecimal( tabManut.getValor( rowSelected, enum_tab_manut.VLRPAGOITPAG.ordinal() ) ), 
						ConversionFunctions.stringToBigDecimal( tabManut.getValor( rowSelected, enum_tab_manut.VLRDESCITPAG.ordinal() ) ), 
						ConversionFunctions.stringToBigDecimal( tabManut.getValor( rowSelected, enum_tab_manut.VLRJUROSITPAG.ordinal() ) ), 
						ConversionFunctions.stringToBigDecimal( tabManut.getValor( rowSelected, enum_tab_manut.VLRAPAGITPAG.ordinal() ) ) );
			}else {
				Funcoes.mensagemInforma( this, "Selecione um título no grid!" );
			}
		}else if ( evt.getSource() == btCarregaBaixas){
			if ( tabBaixa.getLinhaSel() > -1 ) {
				int rowSelected = tabBaixa.getLinhaSel();
				consBaixa( txtCodPagBaixa.getVlrInteger().intValue() , 
						Integer.parseInt( tabBaixa.getValor( rowSelected, 2 ).toString() ), 
						ConversionFunctions.stringToBigDecimal( tabBaixa.getValor( rowSelected, 5 ) ), 
						ConversionFunctions.stringToBigDecimal( tabBaixa.getValor( rowSelected, 7 ) ), 
						ConversionFunctions.stringToBigDecimal( tabBaixa.getValor( rowSelected, 8 ) ), 
						ConversionFunctions.stringToBigDecimal( tabBaixa.getValor( rowSelected, 9 ) ), 
						ConversionFunctions.stringToBigDecimal( tabBaixa.getValor( rowSelected, 10 ) ) );
			}else {
				Funcoes.mensagemInforma( this, "Selecione um título no grid!" );
			}
		}
		else if(evt.getSource() instanceof JMenuItem) {

			JMenuItem menu = (JMenuItem) evt.getSource();

			String opcao  = menu.getText();

			Integer codsinal = null;

			if(menu != menu_limpa_cor_linha && menu != menu_cadastra_cor && menu != menu_limpa_cor_tudo) {

				codsinal = Integer.parseInt( opcao.substring( 0, opcao.indexOf( "-" ) ));

			}
			else if (evt.getSource() == menu_cadastra_cor){

				if (Funcoes.verificaAcessoClasse(FSinalizadores.class.getCanonicalName())) {
					Aplicativo.getInstace().abreTela("Sinalizadores", FSinalizadores.class);
				}
				else {
					Funcoes.mensagemInforma(null, "O usuário " + Aplicativo.getUsuario().getIdusu() + " não possui acesso a tela solicitada (" + FSinalizadores.class.getName()
							+ ").\nSolicite a liberação do acesso ao administrador do sistema.");
				}

				return;
			}

			try {
				if (menu == menu_limpa_cor_tudo) {

					daoMovimento.atualizaCor( null, 
							(Integer) tabManut.getValor( tabManut.getLinhaSel(), enum_tab_manut.CODPAG.ordinal() ), 
							(Integer) tabManut.getValor( tabManut.getLinhaSel(), enum_tab_manut.NPARCPAG.ordinal() ) , true );

				} else {
					daoMovimento.atualizaCor( codsinal, 
							(Integer) tabManut.getValor( tabManut.getLinhaSel(), enum_tab_manut.CODPAG.ordinal() ),
							(Integer) tabManut.getValor( tabManut.getLinhaSel(), enum_tab_manut.NPARCPAG.ordinal() ), false );
				}
			} catch (SQLException e) {
				Funcoes.mensagemErro(this, "Erro ao atualizar cor na grid!!!");
			}
			carregaGridManut();
		}
	}

	public void stateChanged( ChangeEvent cevt ) {

		if ( tpn.getSelectedIndex() == 0 ) {
			carregaConsulta();
		}
		if ( tpn.getSelectedIndex() == 1 ) {
			lcPagBaixa.carregaDados();
		}
	}

	private void cancelaItem() {

		String sit = "";
		int sel = tabManut.getSelectedRow();
		int codpag = 0;
		int nparcitpag = 0;
		if ( sel < 0 ) {
			Funcoes.mensagemInforma( this, "Selecione um título!" );
		}
		else {
			sit = tabManut.getValor( sel, enum_tab_manut.STATUSITPAG.ordinal() ).toString();
			if ( "P1".equals( sit ) ) {
				if ( Funcoes.mensagemConfirma( this, "Confirma cancelamento do título?" ) == JOptionPane.YES_OPTION ) {
					DLCancItem dlCanc = new DLCancItem( this );
					dlCanc.setVisible( true );
					if ( dlCanc.OK ) {
						codpag = (Integer) tabManut.getValor( sel, enum_tab_manut.CODPAG.ordinal() );
						nparcitpag = (Integer) tabManut.getValor( sel, enum_tab_manut.NPARCPAG.ordinal() );
						execCancItem( codpag, nparcitpag, dlCanc.getValor() );
						carregaGridManut();
					}
				}
			}
			else if ( CANCELADO.equals( sit ) ) {
				Funcoes.mensagemInforma( this, "Título já está cancelado!" );
			}
			else {
				Funcoes.mensagemInforma( this, "Situação do título não permite cancelamento!" );
			}
		}
	}

	private void execCancItem( int codpag, int nparcitpag, String obs ) {

		StringBuilder sql = new StringBuilder( "UPDATE FNITPAGAR SET STATUSITPAG='CP', OBSITPAG=? " );
		sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODPAG=? AND NPARCPAG=? " );
		try {
			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setString( 1, obs );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "FNITPAGAR" ) );
			ps.setInt( 4, codpag );
			ps.setInt( 5, nparcitpag );
			ps.executeUpdate();
			ps.close();
			con.commit();
		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Não foi possível efetuar o cancelamento!\n" + e.getMessage() );
		}
	}

	@ SuppressWarnings ( "unchecked" )
	private void impRecibo() {
		DLImpReciboPag dl = null;

		if ( tabManut.getLinhaSel() < 0 ) {
			Funcoes.mensagemInforma( this, "Selecione uma parcela no grid!" );
			return;
		}

		dl = new DLImpReciboPag( this, con, (Integer) tabManut.getValor( tabManut.getLinhaSel(), enum_tab_manut.CODPAG.ordinal() ), 
				(Integer) tabManut.getValor( tabManut.getLinhaSel(), enum_tab_manut.NPARCPAG.ordinal() ),
				tabManut.getValor( tabManut.getLinhaSel(), enum_tab_manut.CHEQUES.ordinal()).equals("") ? null : (Vector<String>) tabManut.getValor( tabManut.getLinhaSel(), enum_tab_manut.CHEQUES.ordinal()));

		dl.setVisible( true );

		if ( dl.OK ) {
			dl.imprimir();
		}
	}

	public void setConexao( DbConnection cn ) {
		super.setConexao( cn );
		lcFor.setConexao( cn );
		lcForBaixa.setConexao( cn );
		lcForManut.setConexao( cn );
		lcForManut2.setConexao( cn );
		lcCompraBaixa.setConexao( cn );
		lcBancoBaixa.setConexao( cn );
		lcPagBaixa.setConexao( cn );
		lcPagManut.setConexao( cn );
		lcOrdCompra.setConexao( cn );
		lcSinal.setConexao( cn );
		//		prefere = getPrefere();


		daoMovimento = new DAOMovimento( cn );
		try {
			prefere = daoMovimento.getPrefere();
		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro ao carregar preferências gerais!!!" );
		}

		iAnoCC = (Integer) prefere.get( "anocc" );			
		montaMenuCores();

	}

	public void mouseClicked( MouseEvent mevt ) {

		if ( mevt.getSource() == tabManut && mevt.getClickCount() == 2 ) {
			editar();
		}
	}

	public void mouseEntered( MouseEvent e ) { }

	public void mouseExited( MouseEvent e ) { }

	public void mousePressed( MouseEvent e ) {
		if (e.getModifiers() == InputEvent.BUTTON3_MASK && e.getSource()==tabManut) {
			menuCores.show(this, e.getX(), e.getY());
		}
	}

	public void mouseReleased( MouseEvent e ) { }

	private void montaMenuCores() {

		try {

			HashMap<String, Vector<?>> cores = null; 

			try {
				cores = daoMovimento.montaListaCores();
			} catch (SQLException e) {
				Funcoes.mensagemErro(this, "Erro ao montar lista de cores!!!");
			}

			@ SuppressWarnings ( "unchecked" )
			Vector<Color> labels = (Vector<Color>) cores.get( "LAB" );
			@ SuppressWarnings ( "unchecked" )
			Vector<HashMap<String, Object>> valores = (Vector<HashMap<String, Object>>) cores.get("VAL");

			for( int i =0; i < valores.size(); i++ ) {

				JMenuItem menucor = new JMenuItem();

				menucor.addActionListener(this);

				menucor.setBackground( labels.elementAt( i ) );

				HashMap<String, Object> hvalores = valores.elementAt( i );

				menucor.setText( (Integer) hvalores.get( "CODSINAL" ) + "-" + (String) hvalores.get( "DESCSINAL" ) );

				menuCores.add(menucor);
			}

			menuCores.addSeparator();

			menu_limpa_cor_linha.setText( "Limpa cor" );
			menu_cadastra_cor.setText( "Cadastro nova cor" );
			menu_limpa_cor_tudo.setText( "Limpa tudo" );

			menu_limpa_cor_linha.addActionListener( this );
			menu_cadastra_cor.addActionListener( this );
			menu_limpa_cor_tudo.addActionListener( this );
			menuCores.add( menu_limpa_cor_tudo );

			menuCores.add( menu_limpa_cor_linha );
			menuCores.add( menu_cadastra_cor );

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*private void atualizaCor(Integer codsinal, Integer codrec, Integer coditpagar, boolean tudo ) {

			StringBuilder sql = new StringBuilder();
			PreparedStatement ps = null;

			try {

				sql.append( "update fnitpagar set codempsn=?, codfilialsn=?, codsinal=? " );
				sql.append( "where codemp=? and codfilial=?  " );

				if(!tudo) {
					sql.append( "and codpag=? and nparcpag=? " );
				}
				else {
					sql.append( "and codsinal is not null " );
				}


				ps = con.prepareStatement( sql.toString() );

				if(codsinal!=null) {

					ps.setInt( 1, Aplicativo.iCodEmp );
					ps.setInt( 2, ListaCampos.getMasterFilial( "FNSINAL" ) );
					ps.setInt( 3, codsinal );

				}
				else {

					ps.setNull( 1, Types.INTEGER );
					ps.setNull( 2, Types.INTEGER );
					ps.setNull( 3, Types.INTEGER );

				}

				ps.setInt( 4, Aplicativo.iCodEmp );
				ps.setInt( 5, ListaCampos.getMasterFilial( "FNITPAGAR" ) );

				if(!tudo) {
					ps.setInt( 6, codrec );
					ps.setInt( 7, coditpagar );

				}

				ps.execute();

				con.commit();

				sql.append( "update fnitpagar set codempsn=?, codfilialsn=?, codsinal=? " );
				sql.append( "where codemp=? and codfilial=?, emmanut='S'  " );



			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}*/

	/*public HashMap<String, Vector<?>> montaListaCores() {

			Vector<HashMap<String, Object>> vVals = new Vector<HashMap<String, Object>>();
			Vector<Color> vLabs = new Vector<Color>();

			HashMap<String, Vector<?>> ret = new HashMap<String, Vector<?>>();

			PreparedStatement ps = null;
			ResultSet rs = null;
			StringBuffer sql = new StringBuffer();

			sql.append("select s.codsinal, s.descsinal, s.corsinal ");
			sql.append("from fnsinal s ");
			sql.append("where s.codemp=? and s.codfilial=? ");

			try {

				ps = con.prepareStatement(sql.toString());
				ps.setInt(1, Aplicativo.iCodEmp);
				ps.setInt(2, ListaCampos.getMasterFilial("FNSINAL"));

				rs = ps.executeQuery();

				while (rs.next()) {

					HashMap<String, Object> hvalores = new HashMap<String, Object>();

					hvalores.put( "CODSINAL", rs.getInt( "CODSINAL" ) );
					hvalores.put( "DESCSINAL", rs.getString( "DESCSINAL" ) );

					vVals.addElement( hvalores );

					Color cor = new Color(rs.getInt( "corsinal" ));

					vLabs.addElement( cor );

				}

				ret.put("VAL",  vVals);
				ret.put("LAB",  vLabs);


			}
			catch (SQLException err) {
				err.printStackTrace();
				Funcoes.mensagemErro(null, "Erro ao buscar sinais");
			}
			return ret;
		}*/

	public void keyPressed( KeyEvent arg0 ) { }

	public void keyReleased( KeyEvent arg0 ) { }

	public void keyTyped( KeyEvent kevt ) {

		if(kevt.getSource() == txtDocManut ) {

			Integer docrec = txtDocManut.getVlrInteger();

			if(docrec !=null && docrec >0) {

				if ( (kevt.getKeyChar() == KeyEvent.VK_ENTER ) || ( kevt.getKeyChar() == KeyEvent.VK_TAB )) {

					Integer codpag = null;
					try {
						codpag = daoMovimento.pesquisarDoc( docrec );
					} catch (SQLException e) {
						Funcoes.mensagemErro(this, "Erro ao pesquisar pedido pelo doc!!!");
					}

					if(codpag!=null && codpag>0) {
						txtCodPagManut.setVlrInteger( codpag );
						lcPagManut.carregaDados();
					}
				}
			}
		} else if (kevt.getSource() == txtCodCompraManut ) {
			Integer codcompra = txtCodCompraManut.getVlrInteger();

			if(codcompra !=null && codcompra >0) {

				if ((kevt.getKeyChar() == KeyEvent.VK_ENTER) || (kevt.getKeyChar() == KeyEvent.VK_TAB)) {
					Integer codpag = null;
					try {
						codpag = daoMovimento.pesquisarPedido( codcompra );
					} catch (SQLException e) {
						Funcoes.mensagemErro(this, "Erro ao pesquisar pedido pelo código da compra!!!");
					}

					if (codpag!=null && codpag>0) {
						txtCodPagManut.setVlrInteger(codpag);
						lcPagManut.carregaDados();
					}
				}
			}
		} 
		else if (kevt.getSource() == txtNumCheque) {

			Integer nrocheque = txtNumCheque.getVlrInteger();

			if (nrocheque !=null && nrocheque >0) {

				txtCodPagManut.setVlrString( "" );
				txtDocManut.setVlrString( "" );
				txtCodForManut2.setVlrString( "" );
				txtRazForManut2.setVlrString( "" );
				txtDtEmisManut.setVlrString( "" );
				lcPagManut.carregaDados();
				lcForManut2.carregaDados();

				if ((kevt.getKeyChar() == KeyEvent.VK_ENTER) || (kevt.getKeyChar() == KeyEvent.VK_TAB)) {
					carregaGridManut();
				}
			}
		} else if ((kevt.getSource() == txtCodSinal && (kevt.getKeyChar() == KeyEvent.VK_ENTER) 
				|| (kevt.getKeyChar() == KeyEvent.VK_TAB))) {

			if (txtCodSinal.getVlrInteger()==0) {
				txtCodSinal.setBackground( Color.WHITE );
			}
		}
	}

	/*private Integer pesquisaDoc(Integer docpag) {

			StringBuilder sql = new StringBuilder();
			PreparedStatement ps = null;
			ResultSet rs = null;
			Integer ret = null;

			try {

				sql.append( "select codpag from fnpagar where codemp=? and codfilial=? and docpag=?" );

				ps = con.prepareStatement( sql.toString() );

				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "FNPAGAR" ) );
				ps.setInt( 3, docpag );

				rs = ps.executeQuery();

				if(rs.next()) {

					ret = rs.getInt( "codpag" );

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return ret;
		}*/

	/*private Integer pesquisaPedido(Integer codcompra) {
			StringBuilder sql = new StringBuilder();
			PreparedStatement ps = null;
			ResultSet rs = null;
			Integer ret = null;

			try {

				sql.append( "select codpag from fnpagar where codemp=? and codfilial=? and codcompra=? " );

				ps = con.prepareStatement( sql.toString() );

				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "FNPAGAR" ) );
				ps.setInt( 3, codcompra );

				rs = ps.executeQuery();

				if(rs.next()) {
					ret = rs.getInt( "codpag" );
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ret;
		}*/

	/*	private String getString(String str) {
			String result;
			if (str==null) {
				result = "";
			} else {
				result = str.trim();
			}
			return result;
		}
	 */
}