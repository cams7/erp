/**
 * @version 11/02/2002 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * @version 11/05/2012 
 * @author Anderson Sancez
 * 
 * Projeto: Freedom <BR>
 * 
 * Pacote: org.freedom.modulos.fnc.view.frame.utility <BR>
 * Classe:
 * @(#)FManutRec.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * Tela de manutenção de contas a receber.
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
import org.freedom.library.business.component.Banco;
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
import org.freedom.modulos.crm.view.frame.utility.FCRM;
import org.freedom.modulos.fnc.view.dialog.report.DLImpBoletoRec;
import org.freedom.modulos.fnc.view.dialog.utility.DLBaixaRec;
import org.freedom.modulos.fnc.view.dialog.utility.DLBordero;
import org.freedom.modulos.fnc.view.dialog.utility.DLConsultaBaixaRecebimento;
import org.freedom.modulos.fnc.view.dialog.utility.DLEditaRec;
import org.freedom.modulos.fnc.view.dialog.utility.DLEditaRec.EColEdit;
import org.freedom.modulos.fnc.view.dialog.utility.DLEstornoMultiplaBaixaRecebimento;
import org.freedom.modulos.fnc.view.dialog.utility.DLNovoRec;
import org.freedom.modulos.fnc.view.dialog.utility.DLRenegRec;
import org.freedom.modulos.fnc.view.frame.crud.plain.FSinalizadores;
import org.freedom.modulos.std.business.object.ConsultaReceber;
import org.freedom.modulos.std.dao.DAOMovimento;
import org.freedom.modulos.std.view.dialog.utility.DLCancItem;
import org.freedom.modulos.std.view.dialog.utility.DLConsultaVenda;

public class FManutRec extends FFilho implements ActionListener, CarregaListener, ChangeListener, MouseListener, KeyListener {

	private static final long serialVersionUID = 1L;

	// private static final String HISTORICO_PADRAO = "RECEBIMENTO REF. AO PED.: <DOCUMENTO>";

	public enum EColTabManut {
		SEL, IMGSTATUS, STATUS, DTVENC, DTEMIT, DTPREV, CODCLI, RAZCLI, CODREC, NPARCITREC, DOCLANCA,
		DOCVENDA, VLRPARCITREC, DTLIQITREC, DTPAGTOITREC, VLRPAGOITREC, VLRDESCITREC, VLRJUROSITREC, 
		VLRDEVOLUCAOITREC, VLRAPAGITREC, VLRCANCITREC, NUMCONTA, DESCCONTA, CODPLAN, DESCPLAN, CODCC, 
		DESCCC, CODTIPOCOB, DESCTIPOCOB, CODBANCO, NOMEBANCO, CODCARTCOB, DESCCARTCOB, OBSITREC, 
		DESCPONTITREC, SEQNOSSONUMERO, MULTIBAIXA
	};

	private enum EColTabBaixa {
		IMGSTATUS, DTVENC, CODREC, NPARCITREC, DOC, CODVENDA, VLRPARC, DTLIQ, DTPAGTO, VLRPAGO, VLRDESC, VLRJUROS, VLRAPAG, VLRCANC, NUMCONTA, DESCCONTA, CODPLAN, DESCPLAN, CODCC, DESCCC, OBS
	};

	private enum EColTabConsulta {
		IMGSTATUS, DTVENC, CODREC, NPARCITREC, DOC, DTVENDA, VLRPARC, VLRDESC, VLRPAGO, DTLIQ, DTPAGTO, DIASATRASO, VLRJUROS, VLRCANC, SERIE, CODVENDA, CODBANCO, NOMEBANCO, OBS, TV
	};

	private JTabbedPanePad tpn = new JTabbedPanePad();

	private JTablePad tabBaixa = new JTablePad();

	private JScrollPane spnBaixa = new JScrollPane( tabBaixa );

	private JTablePad tabManut = new JTablePad();

	private JScrollPane spnManut = new JScrollPane( tabManut );

	private JTablePad tabConsulta = new JTablePad();

	private JScrollPane spnConsulta = new JScrollPane( tabConsulta );

	private JPanelPad pnLegenda = new JPanelPad( JPanelPad.TP_JPANEL );

	private JPanelPad pnTabConsulta = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinBotoesConsulta = new JPanelPad( 40, 100 );

	private JPanelPad pnRod = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinConsulta = new JPanelPad( 500, 140 );

	private JPanelPad pnConsulta = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnTabBaixa = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinBotoesBaixa = new JPanelPad( 40, 100 );

	private JPanelPad pinBaixa = new JPanelPad( 500, 140 );

	private JPanelPad pnBaixa = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnTabManut = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinBotoesManut = new JPanelPad( 70, 70 );

	private JPanelPad pinManut = new JPanelPad( 800, 155 );

	private JPanelPad pnManut = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtSeqNossoNumero = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCnpjCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtCodCliFiltro = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCnpjCliFiltro = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtPrimCompr = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtUltCompr = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDataMaxFat = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtVlrMaxFat = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtVlrTotVendLiq = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtVlrTotVendBrut = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtVlrTotPago = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtVlrTotAberto = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtDataMaxAcum = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtVlrMaxAcum = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtCodRecBaixa = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodRecManut = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDoc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtSerie = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtCodVendaBaixa = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodCliBaixa = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCnpjCliBaixa = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtDtEmisBaixa = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodBancoBaixa = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private JTextFieldPad txtTotRecBaixa = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtTotAbertoBaixa = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtTotPagoBaixa = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtJurosBaixa = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtDatainiManut = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafimManut = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtTotalVencido = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtTotalParcial = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtTotalRecebido = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtTotalVencer = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtTotalCancelado = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtTotalEmBordero = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtTotalRenegociado = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtTotalEmRenegociacao = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtDocManut = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtPedidoManut = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtCodCliManut = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCnpjCliManut = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldFK txtRazCliManut = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldFK txtDtEmitManut = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldFK txtRazCliFiltro = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldFK txtRazCliBaixa = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldFK txtDescBancoBaixa = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JPanelPad pinFiltroStatus = new JPanelPad( 350, 150 );

	private JButtonPad btBaixaConsulta = new JButtonPad( Icone.novo( "btOk.png" ) );

	private JButtonPad btBaixaManut = new JButtonPad( Icone.novo( "btOk.png" ) );

	private JButtonPad btEditManut = new JButtonPad( Icone.novo( "btEditar.gif" ) );

	private JButtonPad btNovoManut = new JButtonPad( Icone.novo( "btNovo.png" ) );

	private JButtonPad btExcluirManut = new JButtonPad( Icone.novo( "btExcluir.png" ) );

	private JButtonPad btEstorno = new JButtonPad( Icone.novo( "btCancelar.png" ) );

	private JButtonPad btCancItem = new JButtonPad( Icone.novo( "btCancItem.png" ) );

	private JButtonPad btCarregaGridManut = new JButtonPad( Icone.novo( "btExecuta.png" ) );

	private JButtonPad btCarregaBaixas = new JButtonPad( Icone.novo( "btConsBaixa.png" ) );

	private JButtonPad btCarregaBaixasMan = new JButtonPad( Icone.novo( "btConsBaixa.png" ) );

	private JButtonPad btBaixa = new JButtonPad( Icone.novo( "btOk.png" ) );

	private JButtonPad btImpBol = new JButtonPad( Icone.novo( "btCodBar.png" ) );

	private JButtonPad btSair = new JButtonPad( "Sair", Icone.novo( "btSair.png" ) );

	private JButtonPad btCarregaVenda = new JButtonPad( "Consulta venda", Icone.novo( "btSaida.png" ) );

	private JButtonPad btHistorico = new JButtonPad( Icone.novo( "btTelefone.png" ) );

	private JButtonPad btRenegRec = new JButtonPad( Icone.novo( "btRenegRec.png" ) );

	private JButtonPad btBordero = new JButtonPad( Icone.novo( "clPriorAlta.png" ) );

	private JCheckBoxPad cbRecebidas = new JCheckBoxPad( "Recebidas", "S", "N" );

	private JCheckBoxPad cbAReceber = new JCheckBoxPad( "À Receber", "S", "N" );

	private JCheckBoxPad cbRecParcial = new JCheckBoxPad( "Rec. Parcial", "S", "N" );

	private JCheckBoxPad cbCanceladas = new JCheckBoxPad( "Canceladas", "S", "N" );

	private JCheckBoxPad cbEmBordero = new JCheckBoxPad( "Descontados", "S", "N" );

	private JCheckBoxPad cbRenegociado = new JCheckBoxPad( "Renegociados", "S", "N" );

	private JCheckBoxPad cbEmRenegociacao = new JCheckBoxPad( "Em Reneg.", "S", "N" );

	private JRadioGroup<?, ?> rgData = null;

	private JRadioGroup<?, ?> rgVenc = null;

	private ListaCampos lcCli = new ListaCampos( this );

	private ListaCampos lcCliBaixa = new ListaCampos( this );

	private ListaCampos lcCliFiltro = new ListaCampos( this );

	private ListaCampos lcCliManut = new ListaCampos( this, "CL" );

	private ListaCampos lcVendaBaixa = new ListaCampos( this );

	private ListaCampos lcRecManut = new ListaCampos( this );

	private ListaCampos lcRecBaixa = new ListaCampos( this );

	private ListaCampos lcBancoBaixa = new ListaCampos( this, "BC" );

	private Vector<?> vNParcBaixa = new Vector<Object>();

	private Vector<String> vValsData = new Vector<String>();

	private Vector<String> vLabsData = new Vector<String>();

	private Vector<String> vValsVenc = new Vector<String>();

	private Vector<String> vLabsVenc = new Vector<String>();

	private Date dIniManut = null;

	private Date dFimManut = null;

	private ImageIcon imgVencido = Icone.novo( "clVencido.gif" );

	private ImageIcon imgVencido2 = Icone.novo( "clVencido2.gif" );

	private ImageIcon imgPago = Icone.novo( "clPago.gif" );

	private ImageIcon imgPago2 = Icone.novo( "clPago2.gif" );

	private ImageIcon imgPagoParcial = Icone.novo( "clPagoParcial.gif" );

	private ImageIcon imgPagoParcial2 = Icone.novo( "clPagoParcial2.gif" );

	private ImageIcon imgNaoVencido = Icone.novo( "clNaoVencido.gif" );

	private ImageIcon imgNaoVencido2 = Icone.novo( "clNaoVencido2.gif" );

	private ImageIcon imgCancelado = Icone.novo( "clCancelado.gif" );

	private ImageIcon imgCancelado2 = Icone.novo( "clCancelado2.gif" );

	private ImageIcon imgPagoComParciais = Icone.novo( "clQuitadoParcial.gif" );

	private ImageIcon imgBordero = Icone.novo( "clEstrela.gif" );

	private ImageIcon imgRenegociado = Icone.novo( "clRenegociado.png" );

	private ImageIcon imgEmRenegociacao = Icone.novo( "btRenegRec.png" );

	private ImageIcon imgRenegociadoVencido = Icone.novo( "clVencido3.gif" );

	private ImageIcon imgRenegociadoPago = Icone.novo( "clPago3.gif" );

	private ImageIcon imgRenegociadoNaoVencido = Icone.novo( "clNaoVencido3.gif" );

	private JLabelPad lbVencido = new JLabelPad( "Vencido", imgVencido, SwingConstants.LEFT );

	private JLabelPad lbParcial = new JLabelPad( "Rec.Parcial", imgPagoParcial, SwingConstants.LEFT );

	private JLabelPad lbPago = new JLabelPad( "Quitado", imgPago, SwingConstants.LEFT );

	private JLabelPad lbVencer = new JLabelPad( "À vencer", imgNaoVencido, SwingConstants.LEFT );

	private JLabelPad lbCancelado = new JLabelPad( "Cancelado", imgCancelado, SwingConstants.LEFT );

	private JLabelPad lbEmBordero = new JLabelPad( "Bordero", imgBordero, SwingConstants.LEFT );

	private JLabelPad lbRenegociado = new JLabelPad( "Renegociados", imgRenegociado, SwingConstants.LEFT );

	private JLabelPad lbEmRenegociacao = new JLabelPad( "Em Reneg.", imgEmRenegociacao, SwingConstants.LEFT );

	private ImageIcon imgColuna = null;

	private boolean bBuscaAtual = true;

	private JPopupMenu menuCores = new JPopupMenu();

	private int iAnoCC = 0;

	private Map<String, Object> prefere = null;

	private JMenuItem menucancelacor = new JMenuItem();

	private JMenuItem menucadastracor = new JMenuItem();

	private DAOMovimento daomovimento = null;

	public FManutRec() {

		super( false );
		setTitulo( "Manutenção de contas a receber" );
		setAtribos( 20, 20, 940, 570 );

		cbAReceber.setVlrString( "S" );
		cbRecParcial.setVlrString( "S" );

		btBaixaConsulta.setToolTipText( "Baixar" );
		btBaixaManut.setToolTipText( "Baixar" );
		btEditManut.setToolTipText( "Editar lançamento" );
		btNovoManut.setToolTipText( "Novo lançamento" );
		btExcluirManut.setToolTipText( "Excluir" );
		btEstorno.setToolTipText( "Estorno" );
		btCancItem.setToolTipText( "Cancela item" );
		btCarregaGridManut.setToolTipText( "Executar consulta" );
		btCarregaBaixas.setToolTipText( "Carrega baixas" );
		btCarregaBaixasMan.setToolTipText( "Carrega baixas" );
		btBaixa.setToolTipText( "Baixar" );
		btImpBol.setToolTipText( "Imprimir boleto" );
		btSair.setToolTipText( "Sair" );
		btCarregaVenda.setToolTipText( "Consulta venda" );
		btHistorico.setToolTipText( "Histórico de contatos" );
		btBordero.setToolTipText( "Adiantamento de recebíveis" );
		btRenegRec.setToolTipText( "Renegociação de Títulos" );

		Container c = getContentPane();
		c.setLayout( new BorderLayout() );
		c.add( pnRod, BorderLayout.SOUTH );
		c.add( tpn, BorderLayout.CENTER );
	//	btSair.setPreferredSize( new Dimension( 90, 30 ) );

		pnLegenda.setPreferredSize( new Dimension( 940, 40 ) );
		pnLegenda.setLayout( null );

		lbVencido.setBounds( 5, 0, 150, 17 );
		txtTotalVencido.setBounds( 5, 18, 150, 18 );
		lbParcial.setBounds( 160, 0, 150, 17 );
		txtTotalParcial.setBounds( 160, 18, 150, 18 );
		lbPago.setBounds( 315, 0, 150, 17 );
		txtTotalRecebido.setBounds( 315, 18, 150, 18 );
		lbVencer.setBounds( 470, 0, 150, 17 );
		txtTotalVencer.setBounds( 470, 18, 150, 18 );
		
		
		lbCancelado.setBounds( 5, 36, 150, 17 );
		txtTotalCancelado.setBounds( 5, 54, 150, 18 );
		lbEmBordero.setBounds( 160, 36, 150, 17 );
		txtTotalEmBordero.setBounds( 160, 54, 150, 18 );
		lbRenegociado.setBounds( 315, 36, 150, 17 );
		txtTotalRenegociado.setBounds( 315, 54, 150, 18 );
		lbEmRenegociacao.setBounds( 470, 36, 150, 17 );
		txtTotalEmRenegociacao.setBounds( 470, 54, 150, 18 );
		btSair.setBounds( 820, 18, 90, 40);

		pnLegenda.add( lbVencido );
		pnLegenda.add( txtTotalVencido );
		pnLegenda.add( lbParcial );
		pnLegenda.add( txtTotalParcial );
		pnLegenda.add( lbPago );
		pnLegenda.add( txtTotalRecebido );
		pnLegenda.add( lbVencer );
		pnLegenda.add( txtTotalVencer );
		pnLegenda.add( lbCancelado );
		pnLegenda.add( txtTotalCancelado );
		pnLegenda.add( lbEmBordero );
		pnLegenda.add( txtTotalEmBordero );
		pnLegenda.add( lbRenegociado );
		pnLegenda.add( txtTotalRenegociado );
		pnLegenda.add( lbEmRenegociacao );
		pnLegenda.add( txtTotalEmRenegociacao );
		pnLegenda.add( btSair );
		
		txtTotalVencido.setSoLeitura( true );
		txtTotalParcial.setSoLeitura( true );
		txtTotalRecebido.setSoLeitura( true );
		txtTotalVencer.setSoLeitura( true );
		txtTotalCancelado.setSoLeitura( true );
		txtTotalEmBordero.setSoLeitura( true );
		txtTotalRenegociado.setSoLeitura( true );
		txtTotalEmRenegociacao.setSoLeitura( true );

		txtTotalVencido.setFont( SwingParams.getFontbold() );
		txtTotalParcial.setFont( SwingParams.getFontbold() );
		txtTotalRecebido.setFont( SwingParams.getFontbold() );
		txtTotalCancelado.setFont( SwingParams.getFontbold() );
		txtTotalVencer.setFont( SwingParams.getFontbold() );
		txtTotalEmBordero.setFont( SwingParams.getFontbold() );
		txtTotalRenegociado.setFont( SwingParams.getFontbold() );
		txtTotalEmRenegociacao.setFont( SwingParams.getFontbold() );

		pnRod.setBorder( BorderFactory.createEtchedBorder() );
		pnRod.setPreferredSize( new Dimension( 500, 80 ) );
		//pnRod.add( btSair, BorderLayout.EAST );
		pnRod.add( pnLegenda, BorderLayout.WEST );

		btSair.addActionListener( this );
		tabManut.addMouseListener( this );

		// Consulta:

		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do clliente", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtCnpjCli, "CnpjCli", "CNPJ", ListaCampos.DB_SI, false ) );

		lcCli.montaSql( false, "CLIENTE", "VD" );
		lcCli.setQueryCommit( false );
		lcCli.setReadOnly( true );
		txtCodCli.setTabelaExterna( lcCli, null );
		txtCodCli.setFK( true );
		txtCodCli.setNomeCampo( "CodCli" );

		txtPrimCompr.setAtivo( false );
		txtUltCompr.setAtivo( false );
		txtDataMaxFat.setAtivo( false );
		txtVlrMaxFat.setAtivo( false );
		txtVlrTotVendLiq.setAtivo( false );
		txtVlrTotVendBrut.setAtivo( false );
		txtVlrTotPago.setAtivo( false );
		txtVlrTotAberto.setAtivo( false );
		txtDataMaxAcum.setAtivo( false );
		txtVlrMaxAcum.setAtivo( false );

		txtCodCli.setRequerido( true );
		txtCodRecManut.setRequerido( true );

		tpn.addTab( "Consulta", pnConsulta );

		pnConsulta.add( pinConsulta, BorderLayout.NORTH );

		pnTabConsulta.add( pinBotoesConsulta, BorderLayout.EAST );
		pnTabConsulta.add( spnConsulta, BorderLayout.CENTER );
		pnConsulta.add( pnTabConsulta, BorderLayout.CENTER );

		pinConsulta.adic( new JLabelPad( "Cód.cli." ), 7, 0, 250, 20 );
		pinConsulta.adic( txtCodCli, 7, 20, 80, 20 );
		pinConsulta.adic( new JLabelPad( "Razão social do cliente" ), 90, 0, 250, 20 );
		pinConsulta.adic( txtRazCli, 90, 20, 217, 20 );
		pinConsulta.adic( new JLabelPad( "P. compra" ), 310, 0, 97, 20 );
		pinConsulta.adic( txtPrimCompr, 310, 20, 97, 20 );
		pinConsulta.adic( new JLabelPad( "U. compra" ), 410, 0, 100, 20 );
		pinConsulta.adic( txtUltCompr, 410, 20, 100, 20 );
		pinConsulta.adic( new JLabelPad( "Data" ), 7, 40, 98, 20 );
		pinConsulta.adic( txtDataMaxFat, 7, 60, 98, 20 );
		pinConsulta.adic( new JLabelPad( "Valor da maior fatura" ), 108, 40, 139, 20 );
		pinConsulta.adic( txtVlrMaxFat, 108, 60, 139, 20 );
		pinConsulta.adic( new JLabelPad( "Data" ), 250, 40, 98, 20 );
		pinConsulta.adic( txtDataMaxAcum, 250, 60, 120, 20 );
		pinConsulta.adic( new JLabelPad( "Valor do maior acumulo" ), 373, 40, 139, 20 );
		pinConsulta.adic( txtVlrMaxAcum, 373, 60, 137, 20 );
		pinConsulta.adic( new JLabelPad( "Total de vendas liquido" ), 7, 80, 150, 20 );
		pinConsulta.adic( txtVlrTotVendLiq, 7, 100, 165, 20 );
		pinConsulta.adic( new JLabelPad( "Total de vendas bruto" ), 175, 80, 150, 20 );
		pinConsulta.adic( txtVlrTotVendBrut, 175, 100, 165, 20 );
		pinConsulta.adic( new JLabelPad( "Total pago" ), 343, 80, 97, 20 );
		pinConsulta.adic( txtVlrTotPago, 343, 100, 165, 20 );
		pinConsulta.adic( new JLabelPad( "Total em aberto" ), 511, 80, 117, 20 );
		pinConsulta.adic( txtVlrTotAberto, 511, 100, 167, 20 );
		pinConsulta.adic( btCarregaVenda, 700, 93, 150, 30 );

		btCarregaVenda.addActionListener( this );

		pinBotoesConsulta.adic( btBaixaConsulta, 3, 10, 30, 30 );
		tabConsulta.adicColuna( "" );
		tabConsulta.adicColuna( "Dt.vencto." );// 1
		tabConsulta.adicColuna( "Cód.rec." );// 2
		tabConsulta.adicColuna( "Nro.parc." );// 3
		tabConsulta.adicColuna( "Doc." );// 4
		tabConsulta.adicColuna( "Dt.venda." );// 5
		tabConsulta.adicColuna( "Valor parc." );// 6
		tabConsulta.adicColuna( "Desc.fin" );// 7
		tabConsulta.adicColuna( "Valor pago." );// 8
		tabConsulta.adicColuna( "Dt.liq."); // 9
		tabConsulta.adicColuna( "Dt.pagto." );// 10
		tabConsulta.adicColuna( "Atraso" );// 11
		tabConsulta.adicColuna( "Valor juros" );// 12
		tabConsulta.adicColuna( "Valor cancelado" );// 13
		tabConsulta.adicColuna( "Série" );// 14
		tabConsulta.adicColuna( "Cód.venda" );// 15
		tabConsulta.adicColuna( "Cód.banco" ); // 16
		tabConsulta.adicColuna( "Nome banco" );// 17
		tabConsulta.adicColuna( "Observações" );// 18
		tabConsulta.adicColuna( "TV" );// 19

		tabConsulta.setTamColuna( 0, EColTabConsulta.IMGSTATUS.ordinal() );// status
		tabConsulta.setTamColuna( 90, EColTabConsulta.DTVENC.ordinal() );// venc
		tabConsulta.setTamColuna( 80, EColTabConsulta.CODREC.ordinal() );// codrec
		tabConsulta.setTamColuna( 80, EColTabConsulta.NPARCITREC.ordinal() );// nparcitrec
		tabConsulta.setTamColuna( 80, EColTabConsulta.DOC.ordinal() );// doc
		tabConsulta.setTamColuna( 90, EColTabConsulta.DTVENDA.ordinal() );// data venda
		tabConsulta.setTamColuna( 100, EColTabConsulta.VLRPARC.ordinal() );// valor
		tabConsulta.setTamColuna( 100, EColTabConsulta.VLRDESC.ordinal() );// Desc.Fin
		tabConsulta.setTamColuna( 100, EColTabConsulta.VLRPAGO.ordinal() );// valor pago
		tabConsulta.setTamColuna( 120, EColTabConsulta.DTLIQ.ordinal() );// data liquidação
		tabConsulta.setTamColuna( 120, EColTabConsulta.DTPAGTO.ordinal() );// data pagamento/compensação
		tabConsulta.setTamColuna( 60, EColTabConsulta.DIASATRASO.ordinal() );// atraso
		tabConsulta.setTamColuna( 100, EColTabConsulta.VLRJUROS.ordinal() );// juros
		tabConsulta.setTamColuna( 100, EColTabConsulta.VLRCANC.ordinal() );// cancelado
		tabConsulta.setTamColuna( 50, EColTabConsulta.SERIE.ordinal() );// serie
		tabConsulta.setTamColuna( 80, EColTabConsulta.CODVENDA.ordinal() );// codvenda
		tabConsulta.setTamColuna( 50, EColTabConsulta.CODBANCO.ordinal() );// codbanco
		tabConsulta.setTamColuna( 200, EColTabConsulta.NOMEBANCO.ordinal() );// nome banco
		tabConsulta.setTamColuna( 200, EColTabConsulta.OBS.ordinal() );// observ
		tabConsulta.setTamColuna( 20, EColTabConsulta.TV.ordinal() );// Tipo venda

		tabConsulta.setRowHeight(20);

		// Baixa:

		lcVendaBaixa.add( new GuardaCampo( txtCodVendaBaixa, "CodVenda", "Cód.venda", ListaCampos.DB_PK, false ) );
		lcVendaBaixa.add( new GuardaCampo( txtSerie, "Serie", "Série", ListaCampos.DB_SI, false ) );
		lcVendaBaixa.montaSql( false, "VENDA", "VD" );
		lcVendaBaixa.setQueryCommit( false );
		lcVendaBaixa.setReadOnly( true );
		txtCodVendaBaixa.setTabelaExterna( lcVendaBaixa, null );
		txtCodVendaBaixa.setFK( true );
		txtCodVendaBaixa.setNomeCampo( "CodVenda" );

		lcCliBaixa.add( new GuardaCampo( txtCodCliBaixa, "CodCli", "Cód.cli", ListaCampos.DB_PK, false ) );
		lcCliBaixa.add( new GuardaCampo( txtRazCliBaixa, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCliBaixa.add( new GuardaCampo( txtCnpjCliBaixa, "CnpjCli", "CNPJ", ListaCampos.DB_SI, false ) );

		lcCliBaixa.montaSql( false, "CLIENTE", "VD" );
		lcCliBaixa.setQueryCommit( false );
		lcCliBaixa.setReadOnly( true );
		txtCodCliBaixa.setTabelaExterna( lcCliBaixa, null );
		txtCodCliBaixa.setFK( true );
		txtCodCliBaixa.setNomeCampo( "CodCli" );

		lcBancoBaixa.add( new GuardaCampo( txtCodBancoBaixa, "CodBanco", "Cód.banco", ListaCampos.DB_PK, false ) );
		lcBancoBaixa.add( new GuardaCampo( txtDescBancoBaixa, "NomeBanco", "Nome do banco", ListaCampos.DB_SI, false ) );
		lcBancoBaixa.montaSql( false, "BANCO", "FN" );
		lcBancoBaixa.setQueryCommit( false );
		lcBancoBaixa.setReadOnly( true );
		txtCodBancoBaixa.setTabelaExterna( lcBancoBaixa, null );
		txtCodBancoBaixa.setFK( true );
		txtCodBancoBaixa.setNomeCampo( "CodBanco" );

		lcRecBaixa.add( new GuardaCampo( txtCodRecBaixa, "CodRec", "Cód.rec.", ListaCampos.DB_PK, false ) );
		lcRecBaixa.add( new GuardaCampo( txtCodVendaBaixa, "CodVenda", "Cód.venda", ListaCampos.DB_FK, false ) );
		lcRecBaixa.add( new GuardaCampo( txtDoc, "DocRec", "Doc", ListaCampos.DB_SI, false ) );
		lcRecBaixa.add( new GuardaCampo( txtTotRecBaixa, "VlrRec", "Tot.rec.", ListaCampos.DB_SI, false ) );
		lcRecBaixa.add( new GuardaCampo( txtCodCliBaixa, "CodCli", "Cód.cli.", ListaCampos.DB_FK, false ) );
		lcRecBaixa.add( new GuardaCampo( txtDtEmisBaixa, "DataRec", "Data emissão", ListaCampos.DB_SI, false ) );
		lcRecBaixa.add( new GuardaCampo( txtCodBancoBaixa, "CodBanco", "Cód.banco", ListaCampos.DB_FK, false ) );
		lcRecBaixa.add( new GuardaCampo( txtTotAbertoBaixa, "VlrApagRec", "Total aberto", ListaCampos.DB_SI, false ) );
		lcRecBaixa.add( new GuardaCampo( txtTotPagoBaixa, "VlrPagoRec", "Total pago", ListaCampos.DB_SI, false ) );
		lcRecBaixa.add( new GuardaCampo( txtJurosBaixa, "VlrJurosRec", "Total juros", ListaCampos.DB_SI, false ) );
		lcRecBaixa.montaSql( false, "RECEBER", "FN" );
		lcRecBaixa.setQueryCommit( false );
		lcRecBaixa.setReadOnly( true );
		txtCodRecBaixa.setTabelaExterna( lcRecBaixa, null );
		txtCodRecBaixa.setFK( true );
		txtCodRecBaixa.setNomeCampo( "CodRec" );

		txtDoc.setAtivo( false );
		txtCodVendaBaixa.setAtivo( false );
		txtSerie.setAtivo( false );
		txtCodCliBaixa.setAtivo( false );
		txtDtEmisBaixa.setAtivo( false );
		txtCodBancoBaixa.setAtivo( false );
		txtTotRecBaixa.setAtivo( false );
		txtTotAbertoBaixa.setAtivo( false );
		txtTotPagoBaixa.setAtivo( false );
		txtJurosBaixa.setAtivo( false );

		txtCodRecBaixa.setRequerido( true );

		tpn.addTab( "Baixa", pnBaixa );

		pnBaixa.add( pinBaixa, BorderLayout.NORTH );

		pnTabBaixa.add( pinBotoesBaixa, BorderLayout.EAST );
		pnTabBaixa.add( spnBaixa, BorderLayout.CENTER );

		pnBaixa.add( pnTabBaixa, BorderLayout.CENTER );

		pinBaixa.adic( new JLabelPad( "Cód.rec" ), 7, 0, 80, 20 );
		pinBaixa.adic( txtCodRecBaixa, 7, 20, 80, 20 );
		pinBaixa.adic( new JLabelPad( "Doc." ), 90, 0, 100, 20 );
		pinBaixa.adic( txtDoc, 90, 20, 100, 20 );
		pinBaixa.adic( new JLabelPad( "-", SwingConstants.CENTER ), 190, 20, 10, 20 );
		pinBaixa.adic( new JLabelPad( "Série" ), 200, 0, 40, 20 );
		pinBaixa.adic( txtSerie, 200, 20, 40, 20 );
		pinBaixa.adic( new JLabelPad( "Pedido" ), 243, 0, 100, 20 );
		pinBaixa.adic( txtCodVendaBaixa, 243, 20, 97, 20 );

		pinBaixa.adic( new JLabelPad( "Cód.cli." ), 7, 40, 80, 20 );
		pinBaixa.adic( txtCodCliBaixa, 7, 60, 80, 20 );
		pinBaixa.adic( new JLabelPad( "Descrição do cliente" ), 90, 40, 250, 20 );
		pinBaixa.adic( txtRazCliBaixa, 90, 60, 250, 20 );
		pinBaixa.adic( new JLabelPad( "Cód.banco" ), 343, 40, 80, 20 );
		pinBaixa.adic( txtCodBancoBaixa, 343, 60, 80, 20 );
		pinBaixa.adic( new JLabelPad( "Descrição do banco" ), 426, 40, 250, 20 );
		pinBaixa.adic( txtDescBancoBaixa, 426, 60, 250, 20 );

		pinBaixa.adic( new JLabelPad( "Data de emissão" ), 7, 80, 110, 20 );
		pinBaixa.adic( txtDtEmisBaixa, 7, 100, 110, 20 );
		pinBaixa.adic( new JLabelPad( "Total a pagar" ), 120, 80, 110, 20 );
		pinBaixa.adic( txtTotRecBaixa, 120, 100, 110, 20 );
		pinBaixa.adic( new JLabelPad( "Total pago" ), 233, 80, 107, 20 );
		pinBaixa.adic( txtTotPagoBaixa, 233, 100, 107, 20 );
		pinBaixa.adic( new JLabelPad( "Total em aberto" ), 343, 80, 110, 20 );
		pinBaixa.adic( txtTotAbertoBaixa, 343, 100, 110, 20 );
		pinBaixa.adic( new JLabelPad( "Juros" ), 456, 80, 110, 20 );
		pinBaixa.adic( txtJurosBaixa, 456, 100, 110, 20 );
		pinBaixa.adic( btCarregaBaixas, 646, 90, 30, 30 );

		pinBotoesBaixa.adic( btBaixa, 3, 10, 30, 30 );

		tabBaixa.adicColuna( "" );// 0
		tabBaixa.adicColuna( "Vencimento" ); // 1
		tabBaixa.adicColuna( "Cód.rec." ); // 2
		tabBaixa.adicColuna( "Nro.parc." ); // 3
		tabBaixa.adicColuna( "Doc." ); // 4
		tabBaixa.adicColuna( "Pedido" ); // 5
		tabBaixa.adicColuna( "Valor parcela" ); // 6
		tabBaixa.adicColuna( "Data liquidação" ); // 7
		tabBaixa.adicColuna( "Data pagamento" ); // 8
		tabBaixa.adicColuna( "Valor pago" ); // 9
		tabBaixa.adicColuna( "Valor desc." ); // 10
		tabBaixa.adicColuna( "Valor juros" ); // 11
		tabBaixa.adicColuna( "Valor aberto" ); // 12
		tabBaixa.adicColuna( "Valor cancelado" ); // 13
		tabBaixa.adicColuna( "Nro.Conta" ); // 14
		tabBaixa.adicColuna( "Descrição conta" ); // 15
		tabBaixa.adicColuna( "Cód.planej." ); // 16
		tabBaixa.adicColuna( "Descrição planej." ); // 17
		tabBaixa.adicColuna( "Cód.c.c." ); // 18
		tabBaixa.adicColuna( "Descrição c.c." ); // 19
		tabBaixa.adicColuna( "Observação" ); // 20

		tabBaixa.setTamColuna( 0, EColTabBaixa.IMGSTATUS.ordinal() );
		tabBaixa.setTamColuna( 100, EColTabBaixa.DTVENC.ordinal() );
		tabBaixa.setTamColuna( 100, EColTabBaixa.CODREC.ordinal() );
		tabBaixa.setTamColuna( 100, EColTabBaixa.NPARCITREC.ordinal() );
		tabBaixa.setTamColuna( 70, EColTabBaixa.DOC.ordinal() );
		tabBaixa.setTamColuna( 70, EColTabBaixa.CODVENDA.ordinal() );
		tabBaixa.setTamColuna( 100, EColTabBaixa.VLRPARC.ordinal() );
		tabBaixa.setTamColuna( 120, EColTabBaixa.DTLIQ.ordinal() );
		tabBaixa.setTamColuna( 120, EColTabBaixa.DTPAGTO.ordinal() );
		tabBaixa.setTamColuna( 100, EColTabBaixa.VLRPAGO.ordinal() );
		tabBaixa.setTamColuna( 100, EColTabBaixa.VLRDESC.ordinal() );
		tabBaixa.setTamColuna( 100, EColTabBaixa.VLRJUROS.ordinal() );
		tabBaixa.setTamColuna( 100, EColTabBaixa.VLRAPAG.ordinal() );
		tabBaixa.setTamColuna( 100, EColTabBaixa.VLRCANC.ordinal() );
		tabBaixa.setTamColuna( 80, EColTabBaixa.NUMCONTA.ordinal() );
		tabBaixa.setTamColuna( 180, EColTabBaixa.DESCCONTA.ordinal() );
		tabBaixa.setTamColuna( 80, EColTabBaixa.CODPLAN.ordinal() );
		tabBaixa.setTamColuna( 180, EColTabBaixa.DESCPLAN.ordinal() );
		tabBaixa.setTamColuna( 80, EColTabBaixa.CODCC.ordinal() );
		tabBaixa.setTamColuna( 180, EColTabBaixa.DESCCC.ordinal() );
		tabBaixa.setTamColuna( 200, EColTabBaixa.OBS.ordinal() );

		tabBaixa.setRowHeight(20);

		// Manutenção

		tpn.addTab( "Manutenção", pnManut );

		pnManut.add( pinManut, BorderLayout.NORTH );

		pnTabManut.add( pinBotoesManut, BorderLayout.EAST );
		pnTabManut.add( spnManut, BorderLayout.CENTER );

		pnManut.add( pnTabManut, BorderLayout.CENTER );

		lcRecManut.add( new GuardaCampo( txtCodRecManut, "CodRec", "Cód.rec", ListaCampos.DB_PK, false ) );
		lcRecManut.add( new GuardaCampo( txtDocManut, "DocRec", "Doc.rec", ListaCampos.DB_SI, false ) );
		lcRecManut.add( new GuardaCampo( txtPedidoManut, "CodVenda", "Pedido", ListaCampos.DB_SI, false ) );
		lcRecManut.add( new GuardaCampo( txtCodCliManut, "CodCli", "Cod.cli", ListaCampos.DB_FK, false ) );
		lcRecManut.add( new GuardaCampo( txtDtEmitManut, "DataRec", "Data emissão", ListaCampos.DB_SI, false ) );
		lcRecManut.montaSql( false, "RECEBER", "FN" );
		lcRecManut.setQueryCommit( false );
		lcRecManut.setReadOnly( true );
		txtCodRecManut.setTabelaExterna( lcRecManut, null );
		txtCodRecManut.setFK( true );
		txtCodRecManut.setNomeCampo( "CodRec" );

		txtDatainiManut.setVlrDate( new Date() );
		txtDatafimManut.setVlrDate( new Date() );

		pinManut.adic( new JLabelPad( "Periodo" ), 7, 0, 250, 20 );
		pinManut.adic( txtDatainiManut, 7, 20, 100, 20 );
		pinManut.adic( new JLabelPad( "até", SwingConstants.CENTER ), 107, 20, 30, 20 );
		pinManut.adic( txtDatafimManut, 137, 20, 100, 20 );

		pinManut.adic( new JLabelPad( "Cód.cli." ), 7, 45, 80, 20 );
		pinManut.adic( txtCodCliFiltro, 7, 65, 80, 20 );
		pinManut.adic( new JLabelPad( "Razão social do cliente" ), 90, 45, 215, 20 );
		pinManut.adic( txtRazCliFiltro, 90, 65, 215, 20 );

		vValsData.addElement( "V" );
		vValsData.addElement( "E" );
		vValsData.addElement( "P" );
		vLabsData.addElement( "Vencto." );
		vLabsData.addElement( "Emissão" );
		vLabsData.addElement( "Previsão" );

		rgData = new JRadioGroup<String, String>( 3, 1, vLabsData, vValsData );
		rgData.setVlrString( "V" );
		pinManut.adic( new JLabelPad( "Filtrar por:" ), 310, 0, 100, 20 );
		pinManut.adic( rgData, 310, 20, 100, 65 );

		vValsVenc.addElement( "VE" );
		vValsVenc.addElement( "AV" );
		vValsVenc.addElement( "TT" );

		vLabsVenc.addElement( "Vencidas" );
		vLabsVenc.addElement( "À vencer" );
		vLabsVenc.addElement( "Ambas" );

		rgVenc = new JRadioGroup<String, String>( 3, 2, vLabsVenc, vValsVenc );
		rgVenc.setVlrString( "TT" );
		pinManut.adic( new JLabelPad( "Filtrar por:" ), 415, 0, 100, 20 );
		pinManut.adic( rgVenc, 415, 20, 100, 65 );

		pinFiltroStatus.adic( cbAReceber		, 5		, 0		, 90	, 20 );
		pinFiltroStatus.adic( cbRecebidas		, 5		, 20	, 90	, 20 );
		pinFiltroStatus.adic( cbEmBordero		, 5		, 40	, 100	, 20 );

		pinFiltroStatus.adic( cbRecParcial		, 117	, 0		, 120	, 20 );
		pinFiltroStatus.adic( cbCanceladas		, 117	, 20	, 120	, 20 );
		pinFiltroStatus.adic( cbRenegociado		, 117	, 40	, 120	, 20 );

		pinFiltroStatus.adic( cbEmRenegociacao	, 240	, 0		, 120	, 20 );

		pinManut.adic( new JLabelPad( "Filtrar por:" ), 520, 0, 100, 20 );
		pinManut.adic( pinFiltroStatus, 520, 20, 360, 65 );

		pinManut.adic( btCarregaGridManut, 885, 20, 30, 64 );

		lcCliFiltro.add( new GuardaCampo( txtCodCliFiltro, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCliFiltro.add( new GuardaCampo( txtRazCliFiltro, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCliFiltro.add( new GuardaCampo( txtCnpjCliFiltro, "CnpjCli", "CNPJ", ListaCampos.DB_SI, false ) );
		lcCliFiltro.montaSql( false, "CLIENTE", "VD" );
		lcCliFiltro.setQueryCommit( false );
		lcCliFiltro.setReadOnly( true );
		txtCodCliFiltro.setTabelaExterna( lcCliFiltro, null );
		txtCodCliFiltro.setFK( true );
		txtCodCliFiltro.setNomeCampo( "CodCli" );

		lcCliManut.add( new GuardaCampo( txtCodCliManut, "CodCli", "Cód.cli", ListaCampos.DB_PK, false ) );
		lcCliManut.add( new GuardaCampo( txtRazCliManut, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCliManut.add( new GuardaCampo( txtCnpjCliManut, "CnpjCli", "CNPJ", ListaCampos.DB_SI, false ) );

		lcCliManut.montaSql( false, "CLIENTE", "VD" );
		lcCliManut.setQueryCommit( false );
		lcCliManut.setReadOnly( true );
		txtCodCliManut.setTabelaExterna( lcCliManut, null );
		txtCodCliManut.setFK( true );
		txtCodCliManut.setNomeCampo( "CodCli" );

		JLabelPad separacao = new JLabelPad();
		separacao.setBorder( BorderFactory.createEtchedBorder() );
		pinManut.adic( separacao, 7, 95, 908, 2 );

		pinManut.adic( txtCodRecManut		, 7		, 120	, 80	, 20	, "Cod.rec." 				);
		pinManut.adic( txtDocManut			, 90	, 120	, 77	, 20	, "Documento" 				);
		pinManut.adic( txtPedidoManut		, 170	, 120	, 77	, 20	, "Pedido" 					);
		pinManut.adic( txtCodCliManut		, 250	, 120	, 77	, 20	, "Cód.cli." 				);
		pinManut.adic( txtRazCliManut		, 330	, 120	, 300	, 20	, "Razão social do cliente" );
		pinManut.adic( txtDtEmitManut		, 633	, 120	, 90	, 20	, "Data emissão" 			);
		pinManut.adic( txtSeqNossoNumero	, 726	, 120	, 90	, 20	, "Seq.Nosso Nro." 			);

		pinBotoesManut.adic( btCarregaBaixasMan, 3,   3, 30, 30 );
		pinBotoesManut.adic( btBaixaManut, 		 3,  34, 30, 30 );
		pinBotoesManut.adic( btEditManut, 		 3,  65, 30, 30 );
		pinBotoesManut.adic( btNovoManut, 		 3,  96, 30, 30 );
		pinBotoesManut.adic( btHistorico, 		 3, 127, 30, 30 );
		pinBotoesManut.adic( btRenegRec, 		 3, 158, 30, 30 );

		pinBotoesManut.adic( btBordero, 		34,   3, 30, 30 );
		pinBotoesManut.adic( btEstorno, 		34,  34, 30, 30 );
		pinBotoesManut.adic( btExcluirManut, 	34,  65, 30, 30 );
		pinBotoesManut.adic( btCancItem,		34,  96, 30, 30 );
		pinBotoesManut.adic( btImpBol, 			34, 127, 30, 30 );

		tabManut.adicColuna( "Sel." );
		tabManut.adicColuna( "" ); // 0
		tabManut.adicColuna( "St." ); // 1
		tabManut.adicColuna( "Vencto." ); // 2
		tabManut.adicColuna( "Emissão" ); // 3
		tabManut.adicColuna( "Previsão" ); // 4
		tabManut.adicColuna( "Cód.cli." ); // 5
		tabManut.adicColuna( "Razão social do cliente" ); // 6
		tabManut.adicColuna( "Cód.rec." ); // 7
		tabManut.adicColuna( "Parc." ); // 8
		tabManut.adicColuna( "Doc.lanca" ); // 9
		tabManut.adicColuna( "Doc.venda" ); // 10
		tabManut.adicColuna( "Valor parc." ); // 11
		tabManut.adicColuna( "Data liquidação" ); // 12
		tabManut.adicColuna( "Data pagamento" ); // 13
		tabManut.adicColuna( "Valor pago" ); // 14
		tabManut.adicColuna( "Valor desconto" ); // 15
		tabManut.adicColuna( "Valor juros" ); // 16
		tabManut.adicColuna( "Valor devolução" ); // 17
		tabManut.adicColuna( "Valor aberto" ); // 18
		tabManut.adicColuna( "Valor cancelado" ); // 19
		tabManut.adicColuna( "Nro.conta" ); // 20
		tabManut.adicColuna( "Descrição da conta" ); // 21
		tabManut.adicColuna( "Cód.categ." ); // 22
		tabManut.adicColuna( "Categoria" ); // 23
		tabManut.adicColuna( "Cód.c.c." ); // 24
		tabManut.adicColuna( "Descrição do centro de custo" ); // 25
		tabManut.adicColuna( "Cód.tp.cob" ); // 26
		tabManut.adicColuna( "Descrição do tipo de cobrança" ); // 27
		tabManut.adicColuna( "Cód.banco" ); // 28
		tabManut.adicColuna( "Nome do banco" ); // 29
		tabManut.adicColuna( "Cód.cart.cob." ); // 30
		tabManut.adicColuna( "Descrição da carteira de cobrança" ); // 31
		tabManut.adicColuna( "Observação" ); // 32
		tabManut.adicColuna( "pontualidade" ); // 33
		tabManut.adicColuna( "Seq.Nosso.Nro." ); // 343
		tabManut.adicColuna( "M.B." ); // 343

		tabManut.setColunaEditavel( EColTabManut.SEL.ordinal(), true );
		tabManut.setTamColuna( 20, EColTabManut.SEL.ordinal() );
		tabManut.setTamColuna( 0, EColTabManut.IMGSTATUS.ordinal() );
		tabManut.setColunaInvisivel( EColTabManut.STATUS.ordinal() );
		tabManut.setTamColuna( 62, EColTabManut.DTVENC.ordinal() );
		tabManut.setTamColuna( 62, EColTabManut.DTEMIT.ordinal() );
		tabManut.setTamColuna( 62, EColTabManut.DTPREV.ordinal() );
		tabManut.setTamColuna( 50, EColTabManut.CODCLI.ordinal() );
		tabManut.setTamColuna( 200, EColTabManut.RAZCLI.ordinal() );
		tabManut.setTamColuna( 50, EColTabManut.CODREC.ordinal() );
		tabManut.setTamColuna( 30, EColTabManut.NPARCITREC.ordinal() );
		tabManut.setTamColuna( 60, EColTabManut.DOCLANCA.ordinal() );
		tabManut.setTamColuna( 60, EColTabManut.DOCVENDA.ordinal() );
		tabManut.setTamColuna( 90, EColTabManut.VLRPARCITREC.ordinal() );
		tabManut.setTamColuna( 100, EColTabManut.DTLIQITREC.ordinal() );
		tabManut.setTamColuna( 100, EColTabManut.DTPAGTOITREC.ordinal() );
		tabManut.setTamColuna( 90, EColTabManut.VLRPAGOITREC.ordinal() );
		tabManut.setTamColuna( 90, EColTabManut.VLRDESCITREC.ordinal() );
		tabManut.setTamColuna( 90, EColTabManut.VLRJUROSITREC.ordinal() );
		tabManut.setTamColuna( 90, EColTabManut.VLRDEVOLUCAOITREC.ordinal() );
		tabManut.setTamColuna( 90, EColTabManut.VLRAPAGITREC.ordinal() );
		tabManut.setTamColuna( 90, EColTabManut.VLRCANCITREC.ordinal() );
		tabManut.setTamColuna( 80, EColTabManut.NUMCONTA.ordinal() );
		tabManut.setTamColuna( 100, EColTabManut.DESCCONTA.ordinal() );
		tabManut.setTamColuna( 80, EColTabManut.CODPLAN.ordinal() );
		tabManut.setTamColuna( 100, EColTabManut.DESCPLAN.ordinal() );
		tabManut.setTamColuna( 80, EColTabManut.CODCC.ordinal() );
		tabManut.setTamColuna( 130, EColTabManut.DESCCC.ordinal() );
		tabManut.setTamColuna( 50, EColTabManut.CODTIPOCOB.ordinal() );
		tabManut.setTamColuna( 230, EColTabManut.DESCTIPOCOB.ordinal() );
		tabManut.setTamColuna( 50, EColTabManut.CODBANCO.ordinal() );
		tabManut.setTamColuna( 240, EColTabManut.NOMEBANCO.ordinal() );
		tabManut.setTamColuna( 50, EColTabManut.CODCARTCOB.ordinal() );
		tabManut.setTamColuna( 240, EColTabManut.DESCCARTCOB.ordinal() );
		tabManut.setTamColuna( 240, EColTabManut.OBSITREC.ordinal() );
		tabManut.setTamColuna( 30, EColTabManut.DESCPONTITREC.ordinal() );
		tabManut.setTamColuna( 80, EColTabManut.SEQNOSSONUMERO.ordinal() ); 
		tabManut.setTamColuna( 20, EColTabManut.MULTIBAIXA.ordinal() ); 

		tabManut.setRowHeight(20);

		lcCli.addCarregaListener( this );
		lcRecBaixa.addCarregaListener( this );
		lcRecManut.addCarregaListener( this );
		btBaixa.addActionListener( this );
		btBaixaConsulta.addActionListener( this );
		btBaixaManut.addActionListener( this );
		btEditManut.addActionListener( this );
		btNovoManut.addActionListener( this );
		btExcluirManut.addActionListener( this );
		btImpBol.addActionListener( this );
		btCarregaGridManut.addActionListener( this );
		btEstorno.addActionListener( this );
		btCancItem.addActionListener( this );
		btCarregaBaixas.addActionListener( this );
		btCarregaBaixasMan.addActionListener( this );
		btHistorico.addActionListener( this );
		btBordero.addActionListener( this );
		btRenegRec.addActionListener( this );
		tpn.addChangeListener( this );

		txtDocManut.addKeyListener( this );
		txtPedidoManut.addKeyListener( this );
		txtSeqNossoNumero.addKeyListener( this );

	}

	private void visualizaRec() {

		DLEditaRec dl = null;
		Object[] sVals = new Object[ 18 ];
		try {

			int iLin = tabManut.getLinhaSel();
			ImageIcon imgStatus = (ImageIcon) tabManut.getValor( iLin, EColTabManut.IMGSTATUS.ordinal() );
			boolean renegociacao = false;
			if ( imgStatus == imgRenegociadoNaoVencido || imgStatus == imgRenegociadoPago ||
					imgStatus == imgRenegociadoVencido){
				renegociacao = true;
			}

			Integer iCodRec = (Integer) tabManut.getValor( iLin, EColTabManut.CODREC.ordinal() );
			//Integer iNParcItRec = (Integer) tabManut.getValor( iLin, EColTabManut.NPARCITREC.ordinal() );

			dl = new DLEditaRec( this, false, renegociacao );
			sVals = getTabManutValores();
			dl.setConexao( con );
			dl.setValores( sVals );
			dl.setVisible( true );
			dl.dispose();

			if(dl.OK){
				if( Funcoes.mensagemConfirma( this, "Confirmar exclusão da renegociação?" ) == JOptionPane.YES_OPTION){
					daomovimento.excluirReceber( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "FNRECEBER" ), iCodRec );
				}
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private void limpaConsulta() {

		txtPrimCompr.setVlrString( "" );
		txtUltCompr.setVlrString( "" );
		txtDataMaxFat.setVlrString( "" );
		txtVlrMaxFat.setVlrBigDecimal( BigDecimal.ZERO  );
		txtVlrTotVendLiq.setVlrBigDecimal( BigDecimal.ZERO  );
		txtVlrTotVendBrut.setVlrBigDecimal( BigDecimal.ZERO  );
		txtVlrTotPago.setVlrBigDecimal( BigDecimal.ZERO  );
		txtVlrTotAberto.setVlrBigDecimal( BigDecimal.ZERO  );
		txtDataMaxAcum.setVlrBigDecimal( BigDecimal.ZERO  );
		txtVlrMaxAcum.setVlrBigDecimal( BigDecimal.ZERO  );
	}

	private void carregaConsulta() {

		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		StringBuilder sql = new StringBuilder();

		limpaConsulta();
		tabConsulta.limpa(); 

		try {

			ConsultaReceber consulta = daomovimento.buscaConsultaReceber( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "FNRECEBER" ), 
					Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "VDCLIENTE" ), txtCodCli.getVlrInteger() );

			txtVlrTotVendLiq.setVlrBigDecimal( consulta.getVlrtotvendliq() );
			txtVlrTotPago.setVlrBigDecimal( consulta.getVlrtotpago() );
			txtVlrTotVendBrut.setVlrBigDecimal( consulta.getVlrtotvendbrut() );
			txtVlrTotAberto.setVlrBigDecimal( consulta.getVlrtotaberto() );
			txtPrimCompr.setVlrString( consulta.getPrimcompra() != null ? Funcoes.dateToStrDate( consulta.getPrimcompra() ) : "" );
			txtUltCompr.setVlrString( consulta.getUltcompra() != null ? Funcoes.dateToStrDate( consulta.getUltcompra() ) : "" );
			if(consulta.getVlrmaxfat() != null	) {
				txtVlrMaxFat.setVlrBigDecimal( consulta.getVlrmaxfat() );
			}
			txtDataMaxFat.setVlrString( Funcoes.dateToStrDate( consulta.getDatamaxfat() ));
			txtDataMaxAcum.setVlrString( consulta.getDatamaxacum() );
			if(consulta.getVlrmaxacum() != null) {
				txtVlrMaxAcum.setVlrBigDecimal( consulta.getVlrmaxacum() );
			}



			carregaGridConsulta();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a consulta!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sql = null;
		}
	}

	private void carregaGridConsulta() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();
		float bdVlrAReceber = 0.0f;
		float bdVlrPago = 0.0f;

		Float bdTotVencido = 0.0f;
		Float bdTotParcial = 0.0f;
		Float bdTotRecebido = 0.0f;
		Float bdTotVencer = 0.0f;
		Float bdTotCancelado = 0.0f;

		try {

			/*	sSQL.append( "SELECT IT.DTVENCITREC,IT.STATUSITREC," );
			sSQL.append( "(SELECT SERIE FROM VDVENDA V " );
			sSQL.append( "WHERE V.CODEMP=R.CODEMPVA AND V.CODFILIAL=R.CODFILIALVA " );
			sSQL.append( "AND V.TIPOVENDA=R.TIPOVENDA AND V.CODVENDA=R.CODVENDA) SERIE," );
			sSQL.append( "R.DOCREC,R.CODVENDA,R.DATAREC,IT.VLRPARCITREC,IT.DTLIQITREC, IT.DTPAGOITREC,IT.VLRPAGOITREC," );
			sSQL.append( "(CASE WHEN IT.DTLIQITREC IS NULL THEN CAST('today' AS DATE)-IT.DTVENCITREC " );
			sSQL.append( "ELSE IT.DTLIQITREC - IT.DTVENCITREC END ) DIASATRASO, R.OBSREC," );
			sSQL.append( "IT.CODBANCO, (SELECT B.NOMEBANCO FROM FNBANCO B " );
			sSQL.append( "WHERE B.CODBANCO=IT.CODBANCO AND B.CODEMP=IT.CODEMPBO AND B.CODFILIAL=IT.CODFILIALBO) NOMEBANCO," );
			sSQL.append( "R.CODREC,IT.NPARCITREC,IT.VLRDESCITREC,IT.VLRJUROSITREC,R.TIPOVENDA,IT.VLRAPAGITREC, IT.VLRCANCITREC " );
			sSQL.append( "FROM FNRECEBER R,FNITRECEBER IT " );
			sSQL.append( "WHERE R.CODEMPCL=? AND R.CODFILIALCL=? AND R.CODCLI=? AND R.CODEMP=? AND R.CODFILIAL=? AND IT.CODREC=R.CODREC " );
			sSQL.append( "AND IT.CODEMP=R.CODEMP AND IT.CODFILIAL=R.CODFILIAL " );
			sSQL.append( "ORDER BY R.CODREC DESC,IT.NPARCITREC DESC" );

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, lcCli.getCodFilial() );
			ps.setInt( 3, txtCodCli.getVlrInteger().intValue() );
			ps.setInt( 4, Aplicativo.iCodEmp );
			ps.setInt( 5, ListaCampos.getMasterFilial( "FNRECEBER" ) );

			rs = ps.executeQuery();*/
			rs = daomovimento.carregaGridConsulta( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "FNRECEBER" ), Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "VDCLIENTE" ), txtCodCli.getVlrInteger() );

			for ( int i = 0; rs.next(); i++ ) {

				bdVlrAReceber = Funcoes.strDecimalToBigDecimal( 2, rs.getString( "VlrApagItRec" ) ).floatValue();
				bdVlrPago = Funcoes.strDecimalToBigDecimal( 2, rs.getString( "VlrPagoItRec" ) ).floatValue();

				if ( rs.getString( "StatusItRec" ).equals( "RP" ) && bdVlrAReceber == 0.0f ) {
					imgColuna = imgPago;
					bdTotRecebido += Funcoes.strDecimalToBigDecimal( 2, rs.getString( "VLRPAGOITREC" ) ).floatValue();
				}
				else if ( "RN".equals( rs.getString( "StatusItRec" ) ) ) {
					imgColuna = imgRenegociado;
				}
				else if ( "RR".equals( rs.getString( "StatusItRec" ) ) ) {
					if ( bdVlrPago > 0 ) {
						imgColuna = rs.getString( "ATEND" ) == null ? imgPagoParcial : imgPagoParcial2;
					}
					else if ( rs.getDate( "DtVencItRec" ).before( Calendar.getInstance().getTime() ) ) {
						imgColuna = imgRenegociadoVencido;
					}
					else if ( rs.getDate( "DtVencItRec" ).after( Calendar.getInstance().getTime() ) ) {
						imgColuna = imgRenegociadoNaoVencido;
					}
				}
				else if ( "CR".equals( rs.getString( "StatusItRec" ) ) ) {
					imgColuna = imgCancelado ;
					bdTotCancelado += Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VLRCANCITREC" ) ).floatValue();
				}
				else if ( bdVlrPago > 0 ) {
					imgColuna = imgPagoParcial;
					bdTotParcial += Funcoes.strDecimalToBigDecimal( 2, rs.getString( "VLRPAGOITREC" ) ).floatValue();

					if ( rs.getDate( "DtVencItRec" ).before( Calendar.getInstance().getTime() ) ) {
						bdTotVencido += Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VLRApagItRec" ) ).floatValue();
					}
					else {
						bdTotVencer += Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VLRApagItRec" ) ).floatValue();
					}
				}
				else if ( rs.getDate( "DtVencItRec" ).before( Calendar.getInstance().getTime() ) ) {
					imgColuna = imgVencido;
					bdTotVencido += Funcoes.strDecimalToBigDecimal( 2, rs.getString( "VLRPARCITREC" ) ).floatValue();
				}
				else if ( rs.getDate( "DtVencItRec" ).after( Calendar.getInstance().getTime() ) ) {
					imgColuna = imgNaoVencido;
					bdTotVencer += Funcoes.strDecimalToBigDecimal( 2, rs.getString( "VlrApagItRec" ) ).floatValue();
				}

				tabConsulta.adicLinha();
				tabConsulta.setValor( imgColuna, i, EColTabConsulta.IMGSTATUS.ordinal() );
				tabConsulta.setValor( ( rs.getDate( "DTVENCITREC" ) != null ? StringFunctions.sqlDateToStrDate( rs.getDate( "DTVENCITREC" ) ) : "" ), i, EColTabConsulta.DTVENC.ordinal() );
				tabConsulta.setValor( rs.getInt( "CODREC" ), i, EColTabConsulta.CODREC.ordinal() );
				tabConsulta.setValor( rs.getInt( "NPARCITREC" ), i, EColTabConsulta.NPARCITREC.ordinal() );
				tabConsulta.setValor( ( rs.getString( "DOCREC" ) != null ? rs.getString( "DOCREC" ) : "" ), i, EColTabConsulta.DOC.ordinal() );
				tabConsulta.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "DATAREC" ) ), i, EColTabConsulta.DTVENDA.ordinal() );
				tabConsulta.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRPARCITREC" ) ), i, EColTabConsulta.VLRPARC.ordinal() );
				tabConsulta.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRDESCITREC" ) ), i, EColTabConsulta.VLRDESC.ordinal() );
				tabConsulta.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRPAGOITREC" ) ), i, EColTabConsulta.VLRPAGO.ordinal() );
				tabConsulta.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRCANCITREC" ) ), i, EColTabConsulta.VLRCANC.ordinal() );
				tabConsulta.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "DTLIQITREC" ) ), i, EColTabConsulta.DTLIQ.ordinal() );
				tabConsulta.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "DTPAGOITREC" ) ), i, EColTabConsulta.DTPAGTO.ordinal() );
				tabConsulta.setValor( rs.getInt( "DIASATRASO" ), i, EColTabConsulta.DIASATRASO.ordinal() );
				tabConsulta.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRJUROSITREC" ) ), i, EColTabConsulta.VLRJUROS.ordinal() );
				tabConsulta.setValor( ( rs.getString( "SERIE" ) != null ? rs.getString( "SERIE" ) : "" ), i, EColTabConsulta.SERIE.ordinal() );
				tabConsulta.setValor( rs.getInt( "CODVENDA" ), i, EColTabConsulta.CODVENDA.ordinal() );
				tabConsulta.setValor( rs.getString( "CODBANCO" ) != null ? rs.getString( "CODBANCO" ) : "", i, EColTabConsulta.CODBANCO.ordinal() );
				tabConsulta.setValor( rs.getString( "NOMEBANCO" ) != null ? rs.getString( "NOMEBANCO" ) : "", i, EColTabConsulta.NOMEBANCO.ordinal() );
				tabConsulta.setValor( rs.getString( "OBSREC" ) != null ? rs.getString( "OBSREC" ) : "", i, EColTabConsulta.OBS.ordinal() );
				tabConsulta.setValor( rs.getString( "TIPOVENDA" ), i, EColTabConsulta.TV.ordinal() );
			}

			rs.close();


			txtTotalVencido.setVlrBigDecimal( new BigDecimal(Funcoes.arredDouble( bdTotVencido.doubleValue(), Aplicativo.casasDecFin )) );
			txtTotalParcial.setVlrBigDecimal( new BigDecimal(Funcoes.arredDouble( bdTotParcial.doubleValue(), Aplicativo.casasDecFin )) );
			txtTotalRecebido.setVlrBigDecimal( new BigDecimal(Funcoes.arredDouble( bdTotRecebido.doubleValue(), Aplicativo.casasDecFin )) );
			txtTotalVencer.setVlrBigDecimal( new BigDecimal(Funcoes.arredDouble( bdTotVencer.doubleValue(), Aplicativo.casasDecFin )) );
			txtTotalCancelado.setVlrBigDecimal( new BigDecimal(Funcoes.arredDouble( bdTotCancelado.doubleValue(), Aplicativo.casasDecFin )) );

			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao montar a tabela de consulta!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
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
		String sCodBanco = null;
		float bdVlrAReceber = 0.0f;
		float bdVlrRecebido = 0.0f;

		Float bdTotVencido = 0.0f;
		Float bdTotParcial = 0.0f;
		Float bdTotRecebido = 0.0f;
		Float bdTotVencer = 0.0f;
		Float bdTotCancelado = 0.0f;

		try {

			vNParcBaixa.clear();
			tabBaixa.limpa();

			/*sSQL.append( "SELECT IR.DTVENCITREC,IR.STATUSITREC,R.CODREC,IR.DOCLANCAITREC,R.DOCREC," );
			sSQL.append( "R.CODVENDA,IR.VLRPARCITREC, IR.DTLIQITREC, IR.DTPAGOITREC,IR.VLRPAGOITREC," );
			sSQL.append( "IR.VLRAPAGITREC,IR.NUMCONTA,IR.VLRDESCITREC," );
			sSQL.append( "(SELECT C.DESCCONTA FROM FNCONTA C " );
			sSQL.append( "WHERE C.NUMCONTA=IR.NUMCONTA " );
			sSQL.append( "AND C.CODEMP=IR.CODEMPCA AND C.CODFILIAL=IR.CODFILIALCA) DESCCONTA,IR.CODPLAN," );
			sSQL.append( "(SELECT P.DESCPLAN FROM FNPLANEJAMENTO P " );
			sSQL.append( "WHERE P.CODPLAN=IR.CODPLAN AND P.CODEMP=IR.CODEMPPN AND P.CODFILIAL=IR.CODFILIALPN) DESCPLAN," );
			sSQL.append( "IR.CODCC," );
			sSQL.append( "(SELECT CC.DESCCC FROM FNCC CC " );
			sSQL.append( "WHERE CC.CODCC=IR.CODCC AND CC.CODEMP=IR.CODEMPCC AND CC.CODFILIAL=IR.CODFILIALCC AND CC.ANOCC=IR.ANOCC) DESCCC," );
			sSQL.append( "IR.OBSITREC,IR.NPARCITREC,IR.VLRJUROSITREC,IR.DTITREC," );
			sSQL.append( "(SELECT V.DOCVENDA FROM VDVENDA V " );
			sSQL.append( "WHERE V.CODEMP=R.CODEMPVA " );
			sSQL.append( "AND V.CODFILIAL=R.CODFILIALVA AND V.TIPOVENDA=R.TIPOVENDA AND V.CODVENDA=R.CODVENDA) DOCVENDA," );
			sSQL.append( "IR.CODBANCO, IR.VLRCANCITREC " );
			sSQL.append( "FROM FNITRECEBER IR,FNRECEBER R  " );
			sSQL.append( "WHERE IR.CODEMP=R.CODEMP AND IR.CODFILIAL=R.CODFILIAL AND IR.CODREC=R.CODREC AND R.CODREC=? AND R.CODEMP=? AND R.CODFILIAL=? " );
			sSQL.append( "ORDER BY IR.DTVENCITREC,IR.STATUSITREC" );

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, txtCodRecBaixa.getVlrInteger().intValue() );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "FNRECEBER" ) );
			rs = ps.executeQuery();*/
			
			rs = daomovimento.carregaGridBaixa( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "FNRECEBER" ), txtCodRecBaixa.getVlrInteger().intValue() );

			for ( int i = 0; rs.next(); i++ ) {

				bdVlrAReceber = Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VlrApagItRec" ) ).floatValue();
				bdVlrRecebido = Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VlrPagoItRec" ) ).floatValue();

				if ( "CR".equals( rs.getString( "StatusItRec" ) ) ) {
					imgColuna = imgCancelado;
					bdTotCancelado += Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VLRCANCITREC" ) ).floatValue();
				}
				else if ( rs.getString( "StatusItRec" ).equals( "RP" ) && bdVlrAReceber == 0.0f ) {
					imgColuna = imgPago;
					bdTotRecebido += Funcoes.strDecimalToBigDecimal( 2, rs.getString( "VLRPAGOITREC" ) ).floatValue();
				}
				else if ( "RN".equals( rs.getString( "StatusItRec" ) ) ) {
					imgColuna = imgRenegociado;
				}
				else if ( "RR".equals( rs.getString( "StatusItRec" ) ) ) {
					if ( bdVlrRecebido > 0 ) {
						imgColuna = rs.getString( "ATEND" ) == null ? imgPagoParcial : imgPagoParcial2;
					}
					else if ( rs.getDate( "DtVencItRec" ).before( Calendar.getInstance().getTime() ) ) {
						imgColuna = imgRenegociadoVencido;
					}
					else if ( rs.getDate( "DtVencItRec" ).after( Calendar.getInstance().getTime() ) ) {
						imgColuna = imgRenegociadoNaoVencido;
					}
				}
				else if ( bdVlrRecebido > 0 ) {
					imgColuna = imgPagoParcial;
					bdTotParcial += Funcoes.strDecimalToBigDecimal( 2, rs.getString( "VLRPAGOITREC" ) ).floatValue();

					if ( rs.getDate( "DtVencItRec" ).before( Calendar.getInstance().getTime() ) ) {
						bdTotVencido += Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VLRApagItRec" ) ).floatValue();
					}
					else {
						bdTotVencer += Funcoes.strDecimalToBigDecimal( Aplicativo.casasDecFin, rs.getString( "VLRApagItRec" ) ).floatValue();
					}

				}
				else if ( rs.getDate( "DtVencItRec" ).before( Calendar.getInstance().getTime() ) ) {
					imgColuna = imgVencido;
					bdTotVencido += Funcoes.strDecimalToBigDecimal( 2, rs.getString( "VLRPARCITREC" ) ).floatValue();
				}
				else if ( rs.getDate( "DtVencItRec" ).after( Calendar.getInstance().getTime() ) ) {
					imgColuna = imgNaoVencido;
					bdTotVencer += Funcoes.strDecimalToBigDecimal( 2, rs.getString( "VlrApagItRec" ) ).floatValue();
				}

				tabBaixa.adicLinha();
				tabBaixa.setValor( imgColuna, i, EColTabBaixa.IMGSTATUS.ordinal() );
				tabBaixa.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "DTVENCITREC" ) ), i, EColTabBaixa.DTVENC.ordinal() );
				tabBaixa.setValor( rs.getInt( "CODREC" ), i, EColTabBaixa.CODREC.ordinal() );
				tabBaixa.setValor( rs.getInt( "NPARCITREC" ), i, EColTabBaixa.NPARCITREC.ordinal() );

				String doc = ( (rs.getString( "DocLancaItRec" ) != null && ! "".equals( rs.getString( "DocLancaItRec" ))) ? 
						rs.getString( "DocLancaItRec" ) : 
							( rs.getString( "DocRec" ) != null ? rs.getString( "DocRec" ) + "/" + rs.getString( "NParcItRec" ) : "" ) );

				tabBaixa.setValor( doc, i, EColTabBaixa.DOC.ordinal() );

				tabBaixa.setValor( rs.getInt( "CODVENDA" ), i, EColTabBaixa.CODVENDA.ordinal() );
				tabBaixa.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRPARCITREC" ) ), i, EColTabBaixa.VLRPARC.ordinal() );
				tabBaixa.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "DTLIQITREC" ) ), i, EColTabBaixa.DTLIQ.ordinal() );
				tabBaixa.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "DTPAGOITREC" ) ), i, EColTabBaixa.DTPAGTO.ordinal() );
				tabBaixa.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRPAGOITREC" ) ), i, EColTabBaixa.VLRPAGO.ordinal() );
				tabBaixa.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRDESCITREC" ) ), i, EColTabBaixa.VLRDESC.ordinal() );
				tabBaixa.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRJUROSITREC" ) ), i, EColTabBaixa.VLRJUROS.ordinal() );
				tabBaixa.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRAPAGITREC" ) ), i, EColTabBaixa.VLRAPAG.ordinal() );
				tabBaixa.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRCANCITREC" ) ), i, EColTabBaixa.VLRCANC.ordinal() );
				tabBaixa.setValor( rs.getString( "NUMCONTA" ) != null ? rs.getString( "NUMCONTA" ) : "", i, EColTabBaixa.NUMCONTA.ordinal() );
				tabBaixa.setValor( rs.getString( "DESCCONTA" ) != null ? rs.getString( "DESCCONTA" ) : "", i, EColTabBaixa.DESCCONTA.ordinal() );
				tabBaixa.setValor( rs.getString( "CODCC" ) != null ? rs.getString( "CODCC" ) : "", i, EColTabBaixa.CODCC.ordinal() );
				tabBaixa.setValor( rs.getString( "DESCCC" ) != null ? rs.getString( "DESCCC" ) : "", i, EColTabBaixa.DESCCC.ordinal() );
				tabBaixa.setValor( rs.getInt( "NPARCITREC" ), i, EColTabBaixa.NPARCITREC.ordinal() );
				tabBaixa.setValor( rs.getString( "OBSITREC" ) != null ? rs.getString( "OBSITREC" ) : "", i, EColTabBaixa.OBS.ordinal() );

				tabBaixa.setValor( rs.getString( "CODPLAN" ), i, EColTabBaixa.CODPLAN.ordinal() );


				sCodBanco = rs.getString( "CODBANCO" );

			}

			txtCodBancoBaixa.setVlrString( sCodBanco );
			lcBancoBaixa.carregaDados();

			txtTotalVencido.setVlrBigDecimal( new BigDecimal( Funcoes.arredDouble( bdTotVencido.doubleValue(), Aplicativo.casasDecFin )) );
			txtTotalParcial.setVlrBigDecimal( new BigDecimal( Funcoes.arredDouble( bdTotParcial.doubleValue(), Aplicativo.casasDecFin )) );
			txtTotalRecebido.setVlrBigDecimal( new BigDecimal( Funcoes.arredDouble( bdTotRecebido.doubleValue(), Aplicativo.casasDecFin )) );
			txtTotalVencer.setVlrBigDecimal( new BigDecimal( Funcoes.arredDouble( bdTotVencer.doubleValue(), Aplicativo.casasDecFin )) );
			txtTotalCancelado.setVlrBigDecimal( new BigDecimal( Funcoes.arredDouble( bdTotCancelado.doubleValue(), Aplicativo.casasDecFin )) );

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

	/*	private ResultSet getResultSetManut( boolean bAplicFiltros ) throws SQLException {

		return getResultSetManut( bAplicFiltros, false, false );
	}
	 */	/*
	private ResultSet getResultSetManut( boolean bAplicFiltros, boolean bordero, boolean renegociveis ) throws SQLException {

		ResultSet rs = null;

		StringBuffer sSQL = new StringBuffer();
		StringBuffer sWhereManut = new StringBuffer();
		StringBuffer sWhereStatus = new StringBuffer();

		if ( !validaPeriodo() ) {
			return null;
		}

		if ( bAplicFiltros ) {

			sWhereManut.append( " AND " );

			if ( "V".equals( rgData.getVlrString() ) ) {
				sWhereManut.append( "IR.DTVENCITREC" );
			}
			else if ( "E".equals( rgData.getVlrString() ) ) {
				sWhereManut.append( "IR.DTITREC" );
			}
			else {
				sWhereManut.append( "COALESCE(IR.DTPREVITREC,IR.DTVENCITREC)" );
			}

			// sWhereManut.append( rgData.getVlrString().equals( "V" ) ? "IR.DTVENCITREC" : "IR.DTITREC" );
			sWhereManut.append( " BETWEEN ? AND ? AND R.CODEMP=? AND R.CODFILIAL=?" );

			if ( "S".equals( cbRecebidas.getVlrString() ) || "S".equals( cbAReceber.getVlrString() ) || "S".equals( cbRecParcial.getVlrString() ) || 
					"S".equals( cbCanceladas.getVlrString() ) || "S".equals( cbEmBordero.getVlrString() )  || "S".equals( cbRenegociado.getVlrString() ) || 
					"S".equals( cbEmRenegociacao.getVlrString() ) ) {

				boolean bStatus = false;

				if ( "S".equals( cbRecebidas.getVlrString() ) && !renegociveis) {
					sWhereStatus.append( "IR.STATUSITREC='RP'" );
					bStatus = true;
				}
				if ( "S".equals( cbAReceber.getVlrString() ) ) {
					sWhereStatus.append( bStatus ? " OR IR.STATUSITREC='R1' " : " IR.STATUSITREC='R1' " );
					bStatus = true;
				}
				if ( "S".equals( cbRecParcial.getVlrString() ) ) {
					sWhereStatus.append( bStatus ? " OR IR.STATUSITREC='RL' " : " IR.STATUSITREC='RL' " );
					bStatus = true;
				}
				if ( "S".equals( cbCanceladas.getVlrString() ) && !renegociveis) {
					sWhereStatus.append( bStatus ? " OR IR.STATUSITREC='CR'" : " IR.STATUSITREC='CR' " );
					bStatus = true;
				}
				if ( "S".equals( cbEmBordero.getVlrString() ) ) {
					sWhereStatus.append( bStatus ? " OR IR.STATUSITREC='RB'" : " IR.STATUSITREC='RB' " );
					bStatus = true;
				}
				if ( "S".equals( cbRenegociado.getVlrString() ) && !renegociveis) {
					sWhereStatus.append( bStatus ? " OR IR.STATUSITREC='RN'" : " IR.STATUSITREC='RN' " );
					bStatus = true;
				}
				if ( "S".equals( cbEmRenegociacao.getVlrString() ) && !renegociveis) {
					sWhereStatus.append( bStatus ? " OR IR.STATUSITREC='RR'" : " IR.STATUSITREC='RR' " );
					bStatus = true;
				}

				sWhereManut.append( " AND (" );
				sWhereManut.append( sWhereStatus );
				sWhereManut.append( ")" );
			}
			else {
				Funcoes.mensagemInforma( null, "Você deve selecionar ao menos um filtro de status!" );
				return null;
			}

			if ( !"TT".equals( rgVenc.getVlrString() ) ) {

				sWhereManut.append( " AND IR.DTVENCITREC" );

				if ( rgVenc.getVlrString().equals( "VE" ) ) {
					sWhereManut.append( " <'" );
					sWhereManut.append( Funcoes.dateToStrDB( Calendar.getInstance().getTime() ) );
					sWhereManut.append( "'" );
				}
				else {
					sWhereManut.append( " >='" );
					sWhereManut.append( Funcoes.dateToStrDB( Calendar.getInstance().getTime() ) );
					sWhereManut.append( "'" );
				}
			}
			if ( !"".equals( txtCodCliFiltro.getVlrString().trim() ) ) {
				sWhereManut.append( " AND R.CODCLI=" );
				sWhereManut.append( txtCodCliFiltro.getVlrString() );
			}

			if ( bordero ) {
				sWhereManut.append( " AND NOT EXISTS (SELECT B.NPARCITREC FROM FNITBORDERO B " );
				sWhereManut.append( "WHERE B.CODEMPRC=IR.CODEMP AND B.CODFILIALRC=IR.CODFILIAL AND " );
				sWhereManut.append( "B.CODREC=IR.CODREC AND B.NPARCITREC=IR.NPARCITREC) " );
			}

			if( renegociveis ){
				sWhereManut.append( " AND NOT EXISTS (SELECT B.NPARCITREC FROM FNITRENEGREC B " );
				sWhereManut.append( "WHERE B.CODEMPIR=IR.CODEMP AND B.CODFILIALIR=IR.CODFILIAL AND " );
				sWhereManut.append( "B.CODREC=IR.CODREC AND B.NPARCITREC=IR.NPARCITREC) " );
			}
		}
		else {
			if(txtCodRecManut.getVlrInteger()>0) {
				sWhereManut.append( " AND R.CODREC=? ");
			}
			else {
				sWhereManut.append( " AND " );

				if ( "V".equals( rgData.getVlrString() ) ) {
					sWhereManut.append( "IR.DTVENCITREC" );
				}
				else if ( "E".equals( rgData.getVlrString() ) ) {
					sWhereManut.append( "IR.DTITREC" );
				}
				else {
					sWhereManut.append( "COALESCE(IR.DTPREVITREC,IR.DTVENCITREC)" );
				}

				sWhereManut.append( " BETWEEN ? AND ? " );

			}
			sWhereManut.append( " AND R.CODEMP=? AND R.CODFILIAL=? " );
		}

		sSQL.append( "SELECT IR.DTVENCITREC,IR.DTPREVITREC,IR.STATUSITREC,R.CODCLI,C.RAZCLI,R.CODREC,IR.DOCLANCAITREC," );
		sSQL.append( "R.CODVENDA,IR.VLRPARCITREC, IR.DTLIQITREC, IR.DTPAGOITREC,IR.VLRPAGOITREC,IR.VLRAPAGITREC,IR.NUMCONTA," );
		sSQL.append( "IR.VLRDESCITREC,IR.CODPLAN,IR.CODCC,IR.OBSITREC,IR.NPARCITREC,IR.VLRJUROSITREC," );
		sSQL.append( "IR.DTITREC,IR.CODBANCO,IR.CODCARTCOB, " );
		sSQL.append( "(SELECT C.DESCCONTA FROM FNCONTA C " );
		sSQL.append( "WHERE C.NUMCONTA=IR.NUMCONTA " );
		sSQL.append( "AND C.CODEMP=IR.CODEMPCA AND C.CODFILIAL=IR.CODFILIALCA) DESCCONTA," );
		sSQL.append( "(SELECT P.DESCPLAN FROM FNPLANEJAMENTO P " );
		sSQL.append( "WHERE P.CODPLAN=IR.CODPLAN " );
		sSQL.append( "AND P.CODEMP=IR.CODEMPPN AND P.CODFILIAL=IR.CODFILIALPN) DESCPLAN," );
		sSQL.append( "(SELECT CC.DESCCC FROM FNCC CC " );
		sSQL.append( "WHERE CC.CODCC=IR.CODCC " );
		sSQL.append( "AND CC.CODEMP=IR.CODEMPCC AND CC.CODFILIAL=IR.CODFILIALCC AND CC.ANOCC=IR.ANOCC) DESCCC," );
		sSQL.append( "(SELECT VD.DOCVENDA FROM VDVENDA VD " );
		sSQL.append( "WHERE VD.TIPOVENDA=R.TIPOVENDA AND VD.CODVENDA=R.CODVENDA AND " );
		sSQL.append( " VD.CODEMP=R.CODEMPVA AND VD.CODFILIAL=R.CODFILIALVA) DOCVENDA," );
		sSQL.append( "IR.CODTIPOCOB, " );
		sSQL.append( "(SELECT TP.DESCTIPOCOB FROM FNTIPOCOB TP " );
		sSQL.append( "WHERE TP.CODEMP=IR.CODEMPTC " );
		sSQL.append( "AND TP.CODFILIAL=IR.CODFILIALTC AND TP.CODTIPOCOB=IR.CODTIPOCOB) DESCTIPOCOB, " );
		sSQL.append( "(SELECT BO.NOMEBANCO FROM FNBANCO BO WHERE BO.CODBANCO=IR.CODBANCO " );
		sSQL.append( "AND BO.CODEMP=IR.CODEMPBO AND BO.CODFILIAL=IR.CODFILIALBO) NOMEBANCO," );
		sSQL.append( "(SELECT CB.DESCCARTCOB FROM FNCARTCOB CB WHERE CB.CODBANCO=IR.CODBANCO " );
		sSQL.append( "AND CB.CODEMP=IR.CODEMPBO AND CB.CODFILIAL=IR.CODFILIALBO AND CB.CODCARTCOB=IR.CODCARTCOB) DESCCARTCOB, " );
		sSQL.append( "R.DOCREC, IR.VLRDEVITREC, IR.DESCPONT, IR.VLRCANCITREC, IR.SEQNOSSONUMERO, " );

		sSQL.append( "(SELECT FIRST 1 ITR.CODATENDO FROM ATATENDIMENTOITREC ITR " );
		sSQL.append( "WHERE ITR.CODEMPIR=IR.CODEMP AND ITR.CODFILIALIR=IR.CODFILIAL " );
		sSQL.append( "AND ITR.CODREC=IR.CODREC AND ITR.NPARCITREC=IR.NPARCITREC ) AS ATEND, " );

		sSQL.append( "SN.CORSINAL, IR.MULTIBAIXA ");

		sSQL.append( "FROM FNRECEBER R, VDCLIENTE C, FNITRECEBER IR " );

		sSQL.append( "LEFT OUTER JOIN FNSINAL SN ON SN.CODEMP=IR.CODEMPSN AND SN.CODFILIAL=IR.CODFILIALSN AND SN.CODSINAL=IR.CODSINAL ");

		sSQL.append( "WHERE IR.CODEMP=R.CODEMP AND IR.CODFILIAL=R.CODFILIAL AND R.CODREC=IR.CODREC AND " );
		sSQL.append( "C.CODCLI=R.CODCLI AND C.CODEMP=R.CODEMPCL AND C.CODFILIAL=R.CODFILIALCL " );
		sSQL.append( sWhereManut );

		if(txtSeqNossoNumero.getVlrInteger()>0){
			sSQL.append( "and ir.seqnossonumero="  + txtSeqNossoNumero.getVlrString() );
		}

		sSQL.append( " ORDER BY IR.DTVENCITREC,IR.STATUSITREC,IR.CODREC,IR.NPARCITREC" );

		PreparedStatement ps = con.prepareStatement( sSQL.toString() );

		if ( bAplicFiltros ) {
			ps.setDate( 1, Funcoes.dateToSQLDate( dIniManut ) );
			ps.setDate( 2, Funcoes.dateToSQLDate( dFimManut ) );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "FNRECEBER" ) );
		}
		else {
			int iparam = 1;
			if(txtCodRecManut.getVlrInteger()>0) {
				ps.setInt( iparam++, txtCodRecManut.getVlrInteger().intValue() );
			}
			else
			{
				ps.setDate( iparam++, Funcoes.dateToSQLDate( dIniManut ) );
				ps.setDate( iparam++, Funcoes.dateToSQLDate( dFimManut ) );
			}
			ps.setInt( iparam++, Aplicativo.iCodEmp );
			ps.setInt( iparam++, ListaCampos.getMasterFilial( "FNRECEBER" ) );
		}

		rs = ps.executeQuery();

		return rs;
	}
	  */


	private void carregaGridManut( boolean bAplicFiltros ) {

		try {
			//ResultSet rs = getResultSetManut(bAplicFiltros);

			ResultSet rs = daomovimento.getResultSetManut( bAplicFiltros, false, false , validaPeriodo(), rgData.getVlrString(), rgVenc.getVlrString(), cbRecebidas.getVlrString(), cbCanceladas.getVlrString(),
					cbEmBordero.getVlrString(), cbRenegociado.getVlrString(), cbAReceber.getVlrString(), cbEmRenegociacao.getVlrString(),
					cbRecParcial.getVlrString(), txtCodCliFiltro.getVlrInteger(), txtCodRecManut.getVlrInteger(), txtSeqNossoNumero.getVlrInteger(),
					dIniManut, dFimManut);

			if ( rs == null ) {
				return;
			}

			BigDecimal bdVlrAReceber = new BigDecimal( "0.00" );
			BigDecimal bdVlrRecebido = new BigDecimal( "0.00" );
			BigDecimal bdTotVencido = new BigDecimal( "0.00" );
			BigDecimal bdTotParcial = new BigDecimal( "0.00" );
			BigDecimal bdTotRecebido = new BigDecimal( "0.00" );
			BigDecimal bdTotVencer = new BigDecimal( "0.00" );
			BigDecimal bdTotCancelado = new BigDecimal( "0.00" );
			BigDecimal bdTotBordero = new BigDecimal( "0.00" );
			BigDecimal bdTotRenegociado = new BigDecimal( "0.00" );
			BigDecimal bdTotEmRenegociacao = new BigDecimal( "0.00" );

			tabManut.limpa();

			for ( int i = 0; rs.next(); i++ ) {

				tabManut.adicLinha();

				bdVlrAReceber = rs.getBigDecimal( "VlrApagItRec" );
				bdVlrRecebido = rs.getBigDecimal( "VlrPagoItRec" );

				if ( "CR".equals( rs.getString( "StatusItRec" ) ) ) {
					imgColuna = rs.getString( "ATEND" ) == null ? imgCancelado : imgCancelado2;
					bdTotCancelado = bdTotCancelado.add( rs.getBigDecimal( "VLRCANCITREC" ) );
				}
				else if ( "RB".equals( rs.getString( "StatusItRec" ) ) ) {
					imgColuna = imgBordero;
					bdTotBordero = bdTotBordero.add( rs.getBigDecimal( "VLRPARCITREC" ) );
				}
				else if ( "RP".equals( rs.getString( "StatusItRec" ) ) && bdVlrAReceber.floatValue() == 0 ) {
					StringBuilder sqlLanca = new StringBuilder();
					sqlLanca.append( "SELECT COUNT (CODLANCA) FROM FNSUBLANCA ");
					sqlLanca.append( "WHERE CODREC = ? AND NPARCITREC = ? ");
					sqlLanca.append( "AND CODEMPRC = ? AND CODFILIALRC = ? ");
					sqlLanca.append( "AND CODEMP = ? AND CODFILIAL = ? ");

					PreparedStatement ps = con.prepareStatement( sqlLanca.toString() );
					ps.setInt( 1, rs.getInt( "CODREC" ) );
					ps.setInt( 2, rs.getInt( "NPARCITREC" ) );
					ps.setInt( 3, Aplicativo.iCodEmp );
					ps.setInt( 4, ListaCampos.getMasterFilial( "FNRECEBER" ) );
					ps.setInt( 5, Aplicativo.iCodEmp );
					ps.setInt( 6, ListaCampos.getMasterFilial( "FNSUBLANCA" ) );
					ResultSet rsLanca = ps.executeQuery();
					rsLanca.next();

					if(rsLanca.getInt( 1 ) > 1){
						imgColuna = imgPagoComParciais;
					}else{
						imgColuna = rs.getString( "ATEND" ) == null ? imgPago : imgPago2;
					}
					if(rs.getBigDecimal( "VLRPAGOITREC" ) != null) {
						bdTotRecebido = bdTotRecebido.add( rs.getBigDecimal( "VLRPAGOITREC" ) );	
					}

				}
				else if ( "RN".equals( rs.getString( "StatusItRec" ) ) ) {
					imgColuna = imgRenegociado;
					bdTotRenegociado = bdTotRenegociado.add( rs.getBigDecimal( "VLRPARCITREC" ) );
				}
				else if ( "RR".equals( rs.getString( "StatusItRec" ) ) ) {

					if ( bdVlrRecebido.floatValue() > 0 ) {
						imgColuna = rs.getString( "ATEND" ) == null ? imgPagoParcial : imgPagoParcial2;
						bdTotEmRenegociacao = bdTotEmRenegociacao.add( rs.getBigDecimal( "VLRPAGOITREC" ) );
					}
					else if ( rs.getDate( "DtVencItRec" ).before( Calendar.getInstance().getTime() ) ) {
						imgColuna = imgRenegociadoVencido;
						bdTotEmRenegociacao = bdTotEmRenegociacao.add( rs.getBigDecimal( "VLRPARCITREC" ) );
					}
					else if ( rs.getDate( "DtVencItRec" ).after( Calendar.getInstance().getTime() ) ) {
						imgColuna = imgRenegociadoNaoVencido;
						bdTotEmRenegociacao = bdTotEmRenegociacao.add( rs.getBigDecimal( "VlrApagItRec" ) );
					}
					//imgColuna = imgRenegociadoNaoVencido;
					//bdTotEmRenegociacao = bdTotEmRenegociacao.add( rs.getBigDecimal( "VLRPARCITREC" ) );
				}
				else if ( bdVlrRecebido.floatValue() > 0 ) {
					imgColuna = rs.getString( "ATEND" ) == null ? imgPagoParcial : imgPagoParcial2;
					bdTotParcial = bdTotParcial.add( rs.getBigDecimal( "VLRPAGOITREC" ) );

					if ( rs.getDate( "DtVencItRec" ).before( Calendar.getInstance().getTime() ) ) {
						bdTotVencido = bdTotVencido.add( rs.getBigDecimal( "VLRApagItRec" ) );
					}
					else {
						bdTotVencer = bdTotVencer.add( rs.getBigDecimal( "VLRApagItRec" ) );
					}

				}
				else if ( rs.getDate( "DtVencItRec" ).before( Calendar.getInstance().getTime() ) ) {
					imgColuna = rs.getString( "ATEND" ) == null ? imgVencido : imgVencido2;
					bdTotVencido = bdTotVencido.add( rs.getBigDecimal( "VLRPARCITREC" ) );
				}
				else if ( rs.getDate( "DtVencItRec" ).after( Calendar.getInstance().getTime() ) ) {
					imgColuna = rs.getString( "ATEND" ) == null ? imgNaoVencido : imgNaoVencido2;
					bdTotVencer = bdTotVencer.add( rs.getBigDecimal( "VlrApagItRec" ) );
				}

				Color corsinal = rs.getString( "corsinal" ) == null ? null : new Color(rs.getInt( "corsinal" ));

				tabManut.setValor( new Boolean(false), i, EColTabManut.SEL.ordinal() );
				tabManut.setValor( imgColuna, i, EColTabManut.IMGSTATUS.ordinal(), corsinal );
				tabManut.setValor( rs.getString( "STATUSITREC" ), i, EColTabManut.STATUS.ordinal(), corsinal );
				tabManut.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "DTVENCITREC" ) ), i, EColTabManut.DTVENC.ordinal(), corsinal );
				tabManut.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "DTITREC" ) ), i, EColTabManut.DTEMIT.ordinal(), corsinal );

				tabManut.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "DTPREVITREC" ) ), i, EColTabManut.DTPREV.ordinal(), corsinal );

				tabManut.setValor( rs.getInt( "CODCLI" ), i, EColTabManut.CODCLI.ordinal(), corsinal );
				tabManut.setValor( rs.getString( "RAZCLI" ), i, EColTabManut.RAZCLI.ordinal(), corsinal );
				tabManut.setValor( rs.getInt( "CODREC" ), i, EColTabManut.CODREC.ordinal(), corsinal );
				tabManut.setValor( rs.getInt( "NPARCITREC" ), i, EColTabManut.NPARCITREC.ordinal(), corsinal );
				tabManut.setValor( ( rs.getString( "DocLancaItRec" ) != null ? rs.getString( "DocLancaItRec" ) : ( rs.getString( "DocRec" ) != null ? rs.getString( "DocRec" ) + "/" + rs.getString( "NParcItRec" ) : "" ) ), i, EColTabManut.DOCLANCA.ordinal(), corsinal );
				tabManut.setValor( rs.getInt( "DOCVENDA" ), i, EColTabManut.DOCVENDA.ordinal(), corsinal );// DOCVENDA
				tabManut.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRPARCITREC" ) ), i, EColTabManut.VLRPARCITREC.ordinal(), corsinal );
				tabManut.setValor( ( rs.getDate( "DTLIQITREC" ) != null ? StringFunctions.sqlDateToStrDate( rs.getDate( "DtLiqItRec" ) ) : "" ), i, EColTabManut.DTLIQITREC.ordinal(), corsinal );
				tabManut.setValor( ( rs.getDate( "DTPAGOITREC" ) != null ? StringFunctions.sqlDateToStrDate( rs.getDate( "DtPagoItRec" ) ) : "" ), i, EColTabManut.DTPAGTOITREC.ordinal(), corsinal );

				tabManut.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRPAGOITREC" ) ), i, EColTabManut.VLRPAGOITREC.ordinal(), corsinal );
				tabManut.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRDESCITREC" ) ), i, EColTabManut.VLRDESCITREC.ordinal(), corsinal );
				tabManut.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRJUROSITREC" ) ), i, EColTabManut.VLRJUROSITREC.ordinal(), corsinal );
				tabManut.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRDEVITREC" ) ), i, EColTabManut.VLRDEVOLUCAOITREC.ordinal(), corsinal );
				tabManut.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRAPAGITREC" ) ), i, EColTabManut.VLRAPAGITREC.ordinal(), corsinal );
				tabManut.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRCANCITREC" ) ), i, EColTabManut.VLRCANCITREC.ordinal(), corsinal );

				tabManut.setValor( rs.getString( "NUMCONTA" ) != null ? rs.getString( "NUMCONTA" ) : "", i, EColTabManut.NUMCONTA.ordinal(), corsinal );// NUMCONTA
				tabManut.setValor( rs.getString( "DESCCONTA" ) != null ? rs.getString( "DESCCONTA" ) : "", i, EColTabManut.DESCCONTA.ordinal(), corsinal );// DESCCONTA
				tabManut.setValor( rs.getString( "CODPLAN" ) != null ? rs.getString( "CODPLAN" ) : "", i, EColTabManut.CODPLAN.ordinal(), corsinal );// CODPLAN
				tabManut.setValor( rs.getString( "DESCPLAN" ) != null ? rs.getString( "DESCPLAN" ) : "", i, EColTabManut.DESCPLAN.ordinal(), corsinal );// DESCPLAN
				tabManut.setValor( rs.getString( "CODCC" ) != null ? rs.getString( "CODCC" ) : "", i, EColTabManut.CODCC.ordinal(), corsinal );// CODCC
				tabManut.setValor( rs.getString( "DESCCC" ) != null ? rs.getString( "DESCCC" ) : "", i, EColTabManut.DESCCC.ordinal(), corsinal );// DESCCC
				tabManut.setValor( rs.getString( "CODTIPOCOB" ) != null ? rs.getString( "CODTIPOCOB" ) : "", i, EColTabManut.CODTIPOCOB.ordinal(), corsinal );// TIPOCOB
				tabManut.setValor( rs.getString( "DESCTIPOCOB" ) != null ? rs.getString( "DESCTIPOCOB" ) : "", i, EColTabManut.DESCTIPOCOB.ordinal(), corsinal );// DESCTIPOCOB
				tabManut.setValor( rs.getString( "CODBANCO" ) != null ? rs.getString( "CODBANCO" ) : "", i, EColTabManut.CODBANCO.ordinal(), corsinal );// CODBANCO
				tabManut.setValor( rs.getString( "NOMEBANCO" ) != null ? rs.getString( "NOMEBANCO" ) : "", i, EColTabManut.NOMEBANCO.ordinal(), corsinal );// NOMEBANCO
				tabManut.setValor( rs.getString( "CODCARTCOB" ) != null ? rs.getString( "CODCARTCOB" ) : "", i, EColTabManut.CODCARTCOB.ordinal(), corsinal );// CODBANCO
				tabManut.setValor( rs.getString( "DESCCARTCOB" ) != null ? rs.getString( "DESCCARTCOB" ) : "", i, EColTabManut.DESCCARTCOB.ordinal(), corsinal );// NOMEBANCO
				tabManut.setValor( rs.getString( "ObsItRec" ) != null ? rs.getString( "ObsItRec" ) : "", i, EColTabManut.OBSITREC.ordinal(), corsinal );
				tabManut.setValor( rs.getString( "DescPont" ) != null ? rs.getString( "DescPont" ) : "", i, EColTabManut.DESCPONTITREC.ordinal(), corsinal );
				tabManut.setValor( rs.getString( "SeqNossoNumero" ) != null ? rs.getString( "SeqNossoNumero" ) : 0, i, EColTabManut.SEQNOSSONUMERO.ordinal(), corsinal );
				tabManut.setValor( rs.getString( "MULTIBAIXA" ) != null ? rs.getString( "MULTIBAIXA" ) : 0, i, EColTabManut.MULTIBAIXA.ordinal(), corsinal );
				// tabManut.setValor( rs.getString( "CODREC" ) != null ? rs.getString( "CODREC" ) : "", i, EColTabManut.CODREC.ordinal() );

			}

			txtTotalVencido.setVlrBigDecimal( bdTotVencido );
			txtTotalParcial.setVlrBigDecimal( bdTotParcial );
			txtTotalRecebido.setVlrBigDecimal( bdTotRecebido );
			txtTotalVencer.setVlrBigDecimal( bdTotVencer );
			txtTotalCancelado.setVlrBigDecimal( bdTotCancelado );
			txtTotalEmBordero.setVlrBigDecimal( bdTotBordero );
			txtTotalRenegociado.setVlrBigDecimal( bdTotRenegociado );
			txtTotalEmRenegociacao.setVlrBigDecimal( bdTotEmRenegociacao );

			con.commit();

			if(txtSeqNossoNumero.getVlrInteger()>0) {
				txtSeqNossoNumero.setVlrString( "" );
			}

		} catch ( Exception err ) {
			Funcoes.mensagemErro( this, "Erro ao montar a tabela de baixa!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	@ SuppressWarnings ( "unchecked" )
	private void montaMenuCores() {

		try {
			HashMap<String, Vector<?>> cores = daomovimento.montaListaCores();


			Vector<Color> labels = (Vector<Color>) cores.get( "LAB" );
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

			menucancelacor.setText( "Limpa cor" );
			menucadastracor.setText( "Cadastro nova cor" );

			menucancelacor.addActionListener( this );
			menucadastracor.addActionListener( this );

			menuCores.add( menucancelacor );
			menuCores.add( menucadastracor );

		} catch (Exception e) {
			Funcoes.mensagemErro(null, "Erro ao buscar sinais");
			e.printStackTrace();
		}


	}



	private void carregaVenda() {

		int iLin = tabConsulta.getLinhaSel();
		if ( iLin < 0 ) {
			Funcoes.mensagemInforma( this, "Selecione uma parcela." );
			return;
		}

		int iCodVenda = (Integer) tabConsulta.getValor( iLin, EColTabConsulta.CODVENDA.ordinal() );

		String sTipoVenda = (String) tabConsulta.getValor( iLin, EColTabConsulta.TV.ordinal() );

		if ( iCodVenda > 0 ) {

			DLConsultaVenda dl = new DLConsultaVenda( this, con, iCodVenda, sTipoVenda );
			dl.setVisible( true );
			dl.dispose();
		}
		else {
			Funcoes.mensagemInforma( null, "Este lançamento não possui vínculo com uma venda!" );
		}
	}

	private boolean getUsaBol() {

		return true;
		/*
		 * boolean retorno = false; StringBuilder sSQL = new StringBuilder(); ResultSet rs = null; PreparedStatement ps = null;
		 * 
		 * sSQL.append( "SELECT COUNT(*) FROM fnitmodboleto IM WHERE " ); sSQL.append( "IM.CODEMP=? AND IM.CODFILIAL=? AND IM.CODBANCO IN ('001','104')" );
		 * 
		 * try {
		 * 
		 * ps = con.prepareStatement( sSQL.toString() ); ps.setInt( 1, Aplicativo.iCodEmp ); ps.setInt( 2, ListaCampos.getMasterFilial( "FNITMODBOLETO" ) ); rs = ps.executeQuery();
		 * 
		 * if( rs.next() ){ if( rs.getInt( 1 ) == 0 ){ retorno = false; }else{ retorno = true; } }
		 * 
		 * } catch ( SQLException err ) { err.printStackTrace(); Funcoes.mensagemErro( this, "Erro ao verificar utilização de boleto!\n" + err.getMessage() ); }
		 * 
		 * return retorno;
		 */
	}

	private Object[] getTabManutValores() {

		Object[] sRet = new Object[ EColEdit.values().length ];
		Integer iCodRec;
		Integer iNParcItRec;
		// ObjetoHistorico historico = null;
		Integer codhistrec = null;

		try {

			/*
			 * codhistrec = (Integer) prefere.get( "codhistrec" );
			 * 
			 * if ( codhistrec != 0 ) { historico = new ObjetoHistorico( codhistrec, con ); } else { historico = new ObjetoHistorico(); historico.setHistoricocodificado( HISTORICO_PADRAO ); }
			 */
			int iLin = tabManut.getLinhaSel();

			sRet[ EColEdit.CODCLI.ordinal() ] = (Integer) tabManut.getValor( iLin, EColTabManut.CODCLI.ordinal() );
			sRet[ EColEdit.RAZCLI.ordinal() ] = String.valueOf( tabManut.getValor( iLin, EColTabManut.RAZCLI.ordinal() ) );
			sRet[ EColEdit.NUMCONTA.ordinal() ] = String.valueOf( tabManut.getValor( iLin, EColTabManut.NUMCONTA.ordinal() ) );
			sRet[ EColEdit.CODPLAN.ordinal() ] = String.valueOf( tabManut.getValor( iLin, EColTabManut.CODPLAN.ordinal() ) );
			sRet[ EColEdit.CODCC.ordinal() ] = String.valueOf( tabManut.getValor( iLin, EColTabManut.CODCC.ordinal() ) );
			if ( "".equals( tabManut.getValor( iLin, EColTabManut.DOCLANCA.ordinal() ) ) ) {
				sRet[ EColEdit.DOC.ordinal() ] = String.valueOf( tabManut.getValor( iLin, EColTabManut.DOCVENDA.ordinal() ) );
			}
			else {
				sRet[ EColEdit.DOC.ordinal() ] = tabManut.getValor( iLin, EColTabManut.DOCLANCA.ordinal() );
			}
			sRet[ EColEdit.DOC.ordinal() ] = (String) tabManut.getValor( iLin, EColTabManut.DOCLANCA.ordinal() );
			sRet[ EColEdit.DTEMIS.ordinal() ] = Funcoes.strDateToDate( tabManut.getValor( iLin, EColTabManut.DTEMIT.ordinal() ).toString() );
			sRet[ EColEdit.DTVENC.ordinal() ] = Funcoes.strDateToDate( tabManut.getValor( iLin, EColTabManut.DTVENC.ordinal() ).toString() );
			sRet[ EColEdit.VLRJUROS.ordinal() ] = ConversionFunctions.stringToBigDecimal( tabManut.getValor( iLin, EColTabManut.VLRJUROSITREC.ordinal() ) );
			sRet[ EColEdit.VLRDEVOLUCAO.ordinal() ] = ConversionFunctions.stringToBigDecimal( tabManut.getValor( iLin, EColTabManut.VLRDEVOLUCAOITREC.ordinal() ) );
			sRet[ EColEdit.VLRDESC.ordinal() ] = ConversionFunctions.stringToBigDecimal( tabManut.getValor( iLin, EColTabManut.VLRDESCITREC.ordinal() ) );
			sRet[ EColEdit.VLRPARC.ordinal() ] = ConversionFunctions.stringToBigDecimal( tabManut.getValor( iLin, EColTabManut.VLRPARCITREC.ordinal() ) );
			sRet[ EColEdit.NPARCITREC.ordinal() ] = tabManut.getValor( iLin, EColTabManut.NPARCITREC.ordinal() );

			sRet[ EColEdit.CODREC.ordinal() ] = tabManut.getValor( iLin, EColTabManut.CODREC.ordinal() );
			/*
			 * if ( "".equals( String.valueOf( tabManut.getValor( iLin, EColTabManut.OBS.ordinal() ) ).trim() ) ) { historico.setData( Funcoes.strDateToDate( tabManut.getValor( iLin, EColTabManut.DTEMIT.ordinal() ).toString() ) ); historico.setDocumento( tabManut.getValor( iLin,
			 * EColTabManut.DOCVENDA.ordinal() ).toString().trim() ); historico.setPortador( tabManut.getValor( iLin, EColTabManut.RAZCLI.ordinal() ).toString().trim() ); historico.setValor( Funcoes.strToBd( tabManut.getValor( iLin, EColTabManut.VLRPARC.ordinal() ).toString() ) ); sRet[
			 * EColEdit.OBS.ordinal() ] = historico.getHistoricodecodificado(); } else { sRet[ EColEdit.OBS.ordinal() ] = tabManut.getValor( iLin, EColTabManut.OBS.ordinal() ); }
			 */

			sRet[ EColEdit.OBS.ordinal() ] = tabManut.getValor( iLin, EColTabManut.OBSITREC.ordinal() );

			sRet[ EColEdit.CODBANCO.ordinal() ] = tabManut.getValor( iLin, EColTabManut.CODBANCO.ordinal() );
			sRet[ EColEdit.CODTPCOB.ordinal() ] = String.valueOf( tabManut.getValor( iLin, EColTabManut.CODTIPOCOB.ordinal() ) );
			sRet[ EColEdit.DESCTPCOB.ordinal() ] = String.valueOf( tabManut.getValor( iLin, EColTabManut.DESCTIPOCOB.ordinal() ) );
			sRet[ EColEdit.CODCARTCOB.ordinal() ] = String.valueOf( tabManut.getValor( iLin, EColTabManut.CODCARTCOB.ordinal() ) );
			sRet[ EColEdit.DESCCARTCOB.ordinal() ] = String.valueOf( tabManut.getValor( iLin, EColTabManut.DESCCARTCOB.ordinal() ) );
			sRet[ EColEdit.DESCPONT.ordinal() ] = String.valueOf( tabManut.getValor( iLin, EColTabManut.DESCPONTITREC.ordinal() ) );
			sRet[ EColEdit.DTPREV.ordinal() ] = Funcoes.strDateToDate( tabManut.getValor( iLin, EColTabManut.DTPREV.ordinal() ).toString() );

			sRet[ EColEdit.DTLIQITREC.ordinal() ] = Funcoes.strDateToDate( tabManut.getValor( iLin, EColTabManut.DTLIQITREC.ordinal() ).toString() );

		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return sRet;
	}

	private void cancelaItem() {

		String sit = "";
		int sel = tabManut.getSelectedRow();
		int codrec = 0;
		int nparcitrec = 0;
		if ( sel < 0 ) {
			Funcoes.mensagemInforma( this, "Selecione um título!" );
		}
		else {
			sit = tabManut.getValor( sel, EColTabManut.STATUS.ordinal() ).toString();
			if ( "R1".equals( sit ) ) {
				if ( Funcoes.mensagemConfirma( this, "Confirma cancelamento do título?" ) == JOptionPane.YES_OPTION ) {
					DLCancItem dlCanc = new DLCancItem( this );
					dlCanc.setVisible( true );
					if ( dlCanc.OK ) {
						codrec = ( (Integer) tabManut.getValor( sel, EColTabManut.CODREC.ordinal() ) ).intValue();
						nparcitrec = ( (Integer) tabManut.getValor( sel, EColTabManut.NPARCITREC.ordinal() ) ).intValue();
						try {
							daomovimento.execCancItemRec( codrec, nparcitrec, dlCanc.getValor() );
						} catch (SQLException e) {
							Funcoes.mensagemErro( this, "Não foi possível efetuar o cancelamento!\n" + e.getMessage() );
						}
						carregaGridManut( bBuscaAtual );
					}
				}
			}
			else if ( "CR".equals( sit ) ) {
				Funcoes.mensagemInforma( this, "Título já está cancelado!" );
			}
			else {
				Funcoes.mensagemInforma( this, "Situação do título não permite cancelamento!" );
			}
		}
	}

	private void novo() {

		DLNovoRec dl = new DLNovoRec( this );
		dl.setConexao( con );
		dl.setVisible( true );
		dl.dispose();
		carregaGridManut( bBuscaAtual );
	}

	private void editar() {

		Object[] sVals = new Object[ 19 ];
		Object[] oRets = null;
		DLEditaRec dl = null;
		ImageIcon imgStatusAt = null;
		BigDecimal vlrPago = new BigDecimal( 0 );
		int iCodRec = 0;
		int iNParcItRec = 0;

		try {
			int iLin = tabManut.getLinhaSel();

			if ( tabManut.getLinhaSel() > -1 ) {

				imgStatusAt = (ImageIcon) tabManut.getValor( iLin, EColTabManut.IMGSTATUS.ordinal() );
				vlrPago = ConversionFunctions.stringToBigDecimal( tabManut.getValor( iLin, EColTabManut.VLRPAGOITREC.ordinal() ) );


				if ( imgStatusAt == imgPago || imgStatusAt == imgRenegociadoPago) {
					Funcoes.mensagemInforma( this, "Parcela(s) selecionada(s) já baixada(s)!" );
					return;
				}

				if ( imgStatusAt != imgPago || !vlrPago.equals( new BigDecimal( 0 ) ) ) {

					iCodRec = (Integer) tabManut.getValor( iLin, EColTabManut.CODREC.ordinal() );
					iNParcItRec = (Integer) tabManut.getValor( iLin, EColTabManut.NPARCITREC.ordinal() );

					dl = new DLEditaRec( this, true );

					sVals = getTabManutValores();

					dl.setConexao( con );
					dl.setValores( sVals );
					dl.setVisible( true );

					if ( dl.OK ) {

						oRets = dl.getValores();
						
						try {
							daomovimento.editarTitulo( oRets, iCodRec, iNParcItRec, iAnoCC );
						} catch ( SQLException err ) {
							Funcoes.mensagemErro( this, "Erro ao editar parcela!\n" + err.getMessage(), true, con, err );
							err.printStackTrace();
						}
					}

					dl.dispose();

					carregaGridManut( bBuscaAtual );
				}
				else {
					Funcoes.mensagemErro( this, "NÃO É POSSIVEL EDITAR.\n" + "A PARCELA JÁ FOI PAGA." );
				}
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			sVals = new String[ 13 ];
			oRets = null;
			dl = null;
			imgStatusAt = null;
		}
	}

	private void excluir() {

		PreparedStatement ps = null;
		ImageIcon imgStatusAt = null;

		try {

			if ( tabManut.getLinhaSel() > -1 ) {

				imgStatusAt = (ImageIcon) tabManut.getValor( tabManut.getLinhaSel(), iAnoCC );

				if ( ! ( imgStatusAt == imgPagoParcial || imgStatusAt == imgPago ) ) {

					if ( Funcoes.mensagemConfirma( this, "Deseja realmente excluir esta conta e todas as suas parcelas?" ) == 0 ) {

						try {

							/*ps = con.prepareStatement( "DELETE FROM FNRECEBER WHERE CODREC=? AND CODEMP=? AND CODFILIAL=?" );
							ps.setInt( 1,);
							ps.setInt( 2, Aplicativo.iCodEmp );
							ps.setInt( 3, ListaCampos.getMasterFilial( "FNRECEBER" ) );

							ps.executeUpdate();

							con.commit();*/
							daomovimento.excluirReceber( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "FNRECEBER" ),  (Integer) tabManut.getValor( tabManut.getLinhaSel(), EColTabManut.CODREC.ordinal() )  );

							carregaGridManut( bBuscaAtual );
						} catch ( SQLException err ) {
							if ( err.getErrorCode() == 335544517 ) {
								Funcoes.mensagemErro( this, "NÃO FOI POSSIVEL EXCLUIR.\n" + "A PARCELA JÁ FOI PAGA." );
							}
							else {
								Funcoes.mensagemErro( this, "Erro ao excluir parcela!\n" + err.getMessage(), true, con, err );
							}
							err.printStackTrace();
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
			Funcoes.mensagemErro( null, "Erro ao excluir recebimento.", true, con, e );
			e.printStackTrace();
		} finally {
			ps = null;
			imgStatusAt = null;
		}
	}

	private void estorno() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		ImageIcon imgStatusAt = null;
		int iCodRec = 0;
		int iNParcItRec = 0;
		int countLanca = 0;

		try {

			if ( tabManut.getLinhaSel() > -1 ) {

				imgStatusAt = (ImageIcon) tabManut.getValor( tabManut.getLinhaSel(), EColTabManut.IMGSTATUS.ordinal() );

				if ( ( imgStatusAt == imgPago ) || ( imgStatusAt == imgPagoParcial ) || ( imgStatusAt == imgPago2 ) || ( imgStatusAt == imgPagoParcial2 ) ||
						(imgStatusAt == imgPagoComParciais)) {

					int iLin = tabManut.getLinhaSel();
					iCodRec = (Integer) tabManut.getValor( iLin, EColTabManut.CODREC.ordinal() );
					iNParcItRec = (Integer) tabManut.getValor( iLin, EColTabManut.NPARCITREC.ordinal() );

					try {

						List<Integer> selecionados = null;

						List<Integer> lanctos = daomovimento.getListaLanca( iCodRec, iNParcItRec );
						
						/*StringBuilder sqlLanca = new StringBuilder();
						sqlLanca.append( "SELECT CODLANCA FROM FNLANCA L ");
						sqlLanca.append( "WHERE EXISTS( SELECT * FROM FNSUBLANCA SL ");
						sqlLanca.append( "WHERE SL.CODREC = ? AND SL.NPARCITREC = ? ");
						sqlLanca.append( "AND SL.CODEMPRC = ? AND SL.CODFILIALRC = ? ");
						sqlLanca.append( "AND SL.CODEMP=L.CODEMP AND SL.CODFILIAL=L.CODFILIAL ");
						sqlLanca.append( "AND SL.CODLANCA=L.CODLANCA ) ");
						sqlLanca.append( "AND L.CODEMP = ? AND L.CODFILIAL = ? ");

						ps = con.prepareStatement( sqlLanca.toString() );
						ps.setInt( 1, iCodRec );
						ps.setInt( 2, iNParcItRec );
						ps.setInt( 3, Aplicativo.iCodEmp );
						ps.setInt( 4, ListaCampos.getMasterFilial( "FNRECEBER" ) );
						ps.setInt( 5, Aplicativo.iCodEmp );
						ps.setInt( 6, ListaCampos.getMasterFilial( "FNLANCA" ) );

						rs = ps.executeQuery();
						int countLanca = 0;
						while(rs.next()){
							lanctos.add( new Integer(rs.getInt( "CODLANCA" ) ) );  
						}*/
						countLanca = lanctos.size();

						//if ( ("S".equals( tabManut.getValor( iLin, EColTabManut.MULTIBAIXA.ordinal() ) ) ) && 
						if (countLanca>1 ) {
							selecionados = this.estornoMultiplaBaixa( iCodRec, iNParcItRec );
						}

						String statusItRec = "";
						if (selecionados != null && selecionados.size() >= 1 &&
								selecionados.size() != countLanca) {
							statusItRec = "RL";
						} else if (selecionados == null || selecionados.size() == countLanca){
							if (countLanca <= 1) {
								if ( Funcoes.mensagemConfirma( this, "Confirma o estorno do lançamento?" ) == JOptionPane.YES_OPTION ) {
									statusItRec = "R1";
								} else {
									return;
								}
							}else{
								statusItRec = "R1";
							}
						}
						
						daomovimento.estorno( iCodRec, iNParcItRec, lanctos, selecionados, statusItRec );

						/*if( "R1".equals( statusItRec ) ){
							StringBuilder sql = new StringBuilder();
							sql.append( " select codrenegrec from fnreceber where codemp = ? and codfilial = ? and codrec = ? " );
							ps = con.prepareStatement( sql.toString() );
							ps.setInt( 1, Aplicativo.iCodEmp );
							ps.setInt( 2, ListaCampos.getMasterFilial( "FNRECEBER" ) );
							ps.setInt( 3, iCodRec );

							rs = ps.executeQuery();
							if( rs.next() ){
								Integer codRenegRec = rs.getInt( "codrenegrec" );
								if(codRenegRec != null && codRenegRec > 0){
									statusItRec = "RR";
								}
							}

							daomovimento.setAltUsuItRec( iCodRec, iNParcItRec, "S" );

							StringBuilder sqlDelete = new StringBuilder();
							sqlDelete.append( "DELETE FROM FNSUBLANCA WHERE CODREC = ? AND NPARCITREC = ? ");
							sqlDelete.append( "AND CODEMPRC= ? AND CODFILIALRC = ? ");
							sqlDelete.append( "AND CODEMP = ? AND CODFILIAL = ? ");

							ps = con.prepareStatement( sqlDelete.toString() );
							ps.setInt( 1, iCodRec );
							ps.setInt( 2, iNParcItRec );
							ps.setInt( 3, Aplicativo.iCodEmp );
							ps.setInt( 4, ListaCampos.getMasterFilial( "FNRECEBER" ) );
							ps.setInt( 5, Aplicativo.iCodEmp );
							ps.setInt( 6, ListaCampos.getMasterFilial( "FNSUBLANCA" ) );
							ps.executeUpdate();
							ps.close();

							StringBuilder sqlDeleteLanca = new StringBuilder();
							sqlDeleteLanca.append( "DELETE FROM FNLANCA ");
							sqlDeleteLanca.append( "WHERE CODEMP = ? AND CODFILIAL=? AND CODLANCA=? ");
							sqlDeleteLanca.append( "AND VLRLANCA=0 ");

							StringBuilder sqlDeleteSublanca = new StringBuilder();
							sqlDeleteSublanca.append( "DELETE FROM FNSUBLANCA ");
							sqlDeleteSublanca.append( "WHERE CODEMP = ? AND CODFILIAL=? AND CODLANCA=? ");
							sqlDeleteSublanca.append( "AND VLRSUBLANCA=0 ");


							for (Integer codlanca: lanctos) {
								ps = con.prepareStatement( sqlDeleteSublanca.toString() );
								ps.setInt( 1, Aplicativo.iCodEmp );
								ps.setInt( 2, ListaCampos.getMasterFilial( "FNLANCA" ) );
								ps.setInt( 3, codlanca );
								ps.executeUpdate();	
								ps.close();
								ps = con.prepareStatement( sqlDeleteLanca.toString() );
								ps.setInt( 1, Aplicativo.iCodEmp );
								ps.setInt( 2, ListaCampos.getMasterFilial( "FNLANCA" ) );
								ps.setInt( 3, codlanca );
								ps.executeUpdate();	
								ps.close();
							}

							StringBuilder sqlUpdate = new StringBuilder();
							sqlUpdate.append( "UPDATE FNITRECEBER SET STATUSITREC='" );
							sqlUpdate.append( statusItRec );
							sqlUpdate.append( "', DTPAGOITREC = null, DTLIQITREC = null " );
							sqlUpdate.append( "WHERE CODREC=? AND NPARCITREC=? AND CODEMP=? AND CODFILIAL=? ");

							ps = con.prepareStatement( sqlUpdate.toString() );
							ps.setInt( 1, iCodRec );
							ps.setInt( 2, iNParcItRec );
							ps.setInt( 3, Aplicativo.iCodEmp );
							ps.setInt( 4, ListaCampos.getMasterFilial( "FNRECEBER" ) );
							ps.executeUpdate();
							ps.close();

							daomovimento.setAltUsuItRec( iCodRec, iNParcItRec, "N" );

						} 
						else if( "RL".equals( statusItRec )){

							daomovimento.setAltUsuItRec( iCodRec, iNParcItRec, "S" );

							StringBuilder sqlDelete = new StringBuilder();
							sqlDelete.append( "DELETE FROM FNSUBLANCA WHERE CODREC = ? AND NPARCITREC = ? ");
							sqlDelete.append( "AND CODEMPRC= ? AND CODFILIALRC = ? ");
							sqlDelete.append( "AND CODEMP = ? AND CODFILIAL = ? AND CODLANCA=?");

							StringBuilder sqlDeleteLanca = new StringBuilder();
							sqlDeleteLanca.append( "DELETE FROM FNLANCA ");
							sqlDeleteLanca.append( "WHERE CODEMP = ? AND CODFILIAL=? AND CODLANCA=? ");
							sqlDeleteLanca.append( "AND VLRLANCA=0 ");

							StringBuilder sqlDeleteSublanca = new StringBuilder();
							sqlDeleteSublanca.append( "DELETE FROM FNSUBLANCA ");
							sqlDeleteSublanca.append( "WHERE CODEMP = ? AND CODFILIAL=? AND CODLANCA=? ");
							sqlDeleteSublanca.append( "AND VLRSUBLANCA=0 ");

							for(Integer codlanca : selecionados){
								ps = con.prepareStatement( sqlDelete.toString() );
								ps.setInt( 1, iCodRec );
								ps.setInt( 2, iNParcItRec );
								ps.setInt( 3, Aplicativo.iCodEmp );
								ps.setInt( 4, ListaCampos.getMasterFilial( "FNRECEBER" ) );
								ps.setInt( 5, Aplicativo.iCodEmp );
								ps.setInt( 6, ListaCampos.getMasterFilial( "FNLANCA" ) );
								ps.setInt( 7, codlanca );
								ps.executeUpdate();
								ps.close();
								ps = con.prepareStatement( sqlDeleteSublanca.toString() );
								ps.setInt( 1, Aplicativo.iCodEmp );
								ps.setInt( 2, ListaCampos.getMasterFilial( "FNLANCA" ) );
								ps.setInt( 3, codlanca );
								ps.executeUpdate();	
								ps.close();
								ps = con.prepareStatement( sqlDeleteLanca.toString() );
								ps.setInt( 1, Aplicativo.iCodEmp );
								ps.setInt( 2, ListaCampos.getMasterFilial( "FNLANCA" ) );
								ps.setInt( 3, codlanca );
								ps.executeUpdate();	
								ps.close();
							}

							BigDecimal saldo = new BigDecimal( 0 );

							StringBuilder sql = new StringBuilder();
							sql.append( "SELECT SUM(SL.VLRSUBLANCA)*-1 SALDO FROM FNITRECEBER IR ");
							sql.append( "INNER JOIN FNSUBLANCA SL ON (SL.CODEMPRC = IR.CODEMP AND SL.CODFILIALRC = IR.CODFILIAL AND ");
							sql.append( "SL.CODREC = IR.CODREC AND SL.NPARCITREC = IR.NPARCITREC) ");
							sql.append( "WHERE IR.CODREC = ? AND IR.NPARCITREC = ? ");
							sql.append( "AND IR.CODEMP = ? AND IR.CODFILIAL = ? ");
							ps = con.prepareStatement( sql.toString() );
							ps.setInt( 1, iCodRec );
							ps.setInt( 2, iNParcItRec );
							ps.setInt( 3, Aplicativo.iCodEmp );
							ps.setInt( 4, ListaCampos.getMasterFilial( "FNRECEBER" ) );
							rs = ps.executeQuery();

							if(rs.next()){
								saldo = rs.getBigDecimal( "SALDO" );
							}

							StringBuilder sqlUpdate = new StringBuilder();
							sqlUpdate.append( "UPDATE FNITRECEBER SET STATUSITREC = 'RL', VLRPAGOITREC= ? " );
							sqlUpdate.append( "WHERE CODREC=? AND NPARCITREC=? AND CODEMP=? AND CODFILIAL=? ");
							ps = con.prepareStatement( sqlUpdate.toString() );
							ps.setBigDecimal( 1, saldo );
							ps.setInt( 2, iCodRec );
							ps.setInt( 3, iNParcItRec );
							ps.setInt( 4, Aplicativo.iCodEmp );
							ps.setInt( 5, ListaCampos.getMasterFilial( "FNRECEBER" ) );
							ps.executeUpdate();

							daomovimento.setAltUsuItRec( iCodRec, iNParcItRec, "N" );

						}

						con.commit();
*/
					} catch ( SQLException err ) {
						con.rollback();
						Funcoes.mensagemErro( this, "Erro ao estornar registro!\n" + err.getMessage(), true, con, err );
					}
					carregaGridManut( bBuscaAtual );
				}
				else {
					Funcoes.mensagemInforma( this, "PARCELA AINDA NÃO FOI PAGA!" );
				}
			}
			else {
				Funcoes.mensagemInforma( this, "Não ha nenhum item selecionado." );
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			ps = null;
			imgStatusAt = null;
		}
	}

	private List<Integer> estornoMultiplaBaixa(Integer codRec, Integer nParcItRec) throws SQLException{
		Integer rowSelected = tabManut.getLinhaSel();

		DLEstornoMultiplaBaixaRecebimento dl = new DLEstornoMultiplaBaixaRecebimento( this, con, codRec, nParcItRec );

		dl.setValores( new BigDecimal[] { ConversionFunctions.stringToBigDecimal( tabManut.getValor( rowSelected, EColTabManut.VLRPARCITREC.ordinal() ) ), 
				ConversionFunctions.stringToBigDecimal( tabManut.getValor( rowSelected, EColTabManut.VLRPAGOITREC.ordinal() ) ), 
				ConversionFunctions.stringToBigDecimal( tabManut.getValor( rowSelected, EColTabManut.VLRDESCITREC.ordinal() ) ), 
				ConversionFunctions.stringToBigDecimal( tabManut.getValor( rowSelected, EColTabManut.VLRJUROSITREC.ordinal() ) ), 
				ConversionFunctions.stringToBigDecimal( tabManut.getValor( rowSelected, EColTabManut.VLRAPAGITREC.ordinal() ) )  } );

		dl.setVisible( true );


		List<Integer> selecionados = new ArrayList<Integer>();
		selecionados = dl.getSelecionados();

		dl.dispose();
		return selecionados;

	}

	private void consBaixa( int codRec, int nparcItRec, BigDecimal vlrParc, BigDecimal vlrPago, BigDecimal vlrDesc, BigDecimal vlrJuros, BigDecimal vlrApag ) {

		DLConsultaBaixaRecebimento dl = new DLConsultaBaixaRecebimento( this, con, codRec, nparcItRec );

		dl.setValores( new BigDecimal[] { vlrParc, vlrPago, vlrDesc, vlrJuros, vlrApag } );

		dl.setVisible( true );
		dl.dispose();
	}

	private void baixaConsulta() {

		if ( tabConsulta.getLinhaSel() != -1 ) {

			txtCodRecBaixa.setVlrInteger( (Integer) tabConsulta.getValor( tabConsulta.getLinhaSel(), EColTabConsulta.CODREC.ordinal() ) );

			lcRecBaixa.carregaDados();
			tpn.setSelectedIndex( 1 );
			btBaixa.requestFocus();

		}
	}

	private void baixar( char cOrig ) {

		PreparedStatement ps = null;
		StringBuffer sSQL = new StringBuffer();
		DLBaixaRec dl = null;
		ImageIcon imgStatusAt = null;
		int iCodRec = 0;
		int iNParcItRec = 0;
		// ObjetoHistorico historico = null;
		Integer codhistrec = null;

		try {

			codhistrec = (Integer) prefere.get( "codhistrec" );

			/*
			 * if ( codhistrec != 0 ) { historico = new ObjetoHistorico( codhistrec, con ); } else { historico = new ObjetoHistorico(); historico.setHistoricocodificado( HISTORICO_PADRAO ); }
			 */
			if ( 'M' == cOrig && tabManut.getLinhaSel() > -1 ) {
				this.baixaTabManut();
			}
			else if ( cOrig == 'B' && tabBaixa.getLinhaSel() > -1 ) {

				imgStatusAt = (ImageIcon) tabBaixa.getValor( tabBaixa.getLinhaSel(), 0 );

				if ( imgStatusAt == imgPago ) {
					Funcoes.mensagemInforma( this, "Parcela já foi baixada!" );
					return;
				}
				if ( (imgStatusAt == imgRenegociado) ) {
					Funcoes.mensagemInforma( this, "Parcela renegociada, não é possível baixar!" );
					return;
				}

				int iLin = tabBaixa.getLinhaSel();

				iCodRec = (Integer) tabBaixa.getValor( iLin, EColTabBaixa.CODREC.ordinal() );
				iNParcItRec = (Integer) tabBaixa.getValor( iLin, EColTabBaixa.NPARCITREC.ordinal() );

				dl = new DLBaixaRec( this );

				DLBaixaRec.BaixaRecBean baixaRecBean = dl.new BaixaRecBean();

				baixaRecBean.setRecebimento( (Integer) tabManut.getValor( iLin, EColTabManut.CODREC.ordinal() ) );
				baixaRecBean.setParcela( (Integer) tabManut.getValor( iLin, EColTabManut.NPARCITREC.ordinal() ) );
				baixaRecBean.setCliente( txtCodCliBaixa.getVlrInteger() );
				baixaRecBean.setRazaoSocialCliente( txtRazCliBaixa.getVlrString() );
				baixaRecBean.setConta( (String) tabBaixa.getValor( iLin, EColTabBaixa.NUMCONTA.ordinal() ) );
				baixaRecBean.setPlanejamento( (String) tabBaixa.getValor( iLin, EColTabBaixa.CODPLAN.ordinal() ) );
				baixaRecBean.setDocumento( (String) tabBaixa.getValor( iLin, EColTabBaixa.DOC.ordinal() ) );
				baixaRecBean.setDataEmissao( txtDtEmisBaixa.getVlrDate() );
				baixaRecBean.setDataVencimento( Funcoes.strDateToDate( (String) tabBaixa.getValor( iLin, EColTabBaixa.DTVENC.ordinal() ) ) );
				baixaRecBean.setValorParcela( ConversionFunctions.stringCurrencyToBigDecimal(  
						( (StringDireita) tabBaixa.getValor( iLin, EColTabBaixa.VLRPARC.ordinal() ) ).toString() ) );
				baixaRecBean.setValorAPagar( ConversionFunctions.stringCurrencyToBigDecimal(  
						( (StringDireita) tabBaixa.getValor(  iLin, EColTabBaixa.VLRAPAG.ordinal() ) ).toString() ) );
				baixaRecBean.setValorDesconto( ConversionFunctions.stringCurrencyToBigDecimal(  
						( (StringDireita) tabBaixa.getValor(  iLin, EColTabBaixa.VLRDESC.ordinal() ) ).toString() ) );
				baixaRecBean.setValorJuros( ConversionFunctions.stringCurrencyToBigDecimal(  
						( (StringDireita) tabBaixa.getValor(  iLin, EColTabBaixa.VLRJUROS.ordinal() ) ).toString() ) );
				baixaRecBean.setCentroCusto( (String) tabBaixa.getValor( iLin, EColTabBaixa.CODCC.ordinal() ) );

				//BigDecimal valorAPagar =ConversionFunctions.stringCurrencyToBigDecimal( ( (StringDireita) tabBaixa.getValor(  iLin, EColTabBaixa.VLRAPAG.ordinal() ) ).toString() );

				if ( "".equals( tabBaixa.getValor( iLin, EColTabBaixa.DTPAGTO.ordinal() ) ) ) {
					baixaRecBean.setDataPagamento( new Date() );
					//baixaRecBean.setValorPago( ConversionFunctions.stringCurrencyToBigDecimal(  
					//		( (StringDireita) tabBaixa.getValor(  iLin, EColTabBaixa.VLRPAGO.ordinal() ) ).toString() ) );
					baixaRecBean.setValorPago(new BigDecimal(0));
				}
				else {
					baixaRecBean.setDataPagamento( Funcoes.strDateToDate( (String) tabBaixa.getValor( iLin, EColTabBaixa.DTPAGTO.ordinal() ) ) );
					//baixaRecBean.setValorPago( ConversionFunctions.stringCurrencyToBigDecimal(  
					//		( (StringDireita) tabBaixa.getValor( iLin, EColTabBaixa.VLRPAGO.ordinal() ) ).toString() ) );
					baixaRecBean.setValorPago(new BigDecimal(0));
				}
				/*
				 * if ( "".equals( ( (String) tabBaixa.getValor( iLin, EColTabBaixa.OBS.ordinal() ) ).trim() ) ) { historico.setData( txtDtEmisBaixa.getVlrDate() ); historico.setDocumento( txtCodVendaBaixa.getVlrString() ); historico.setPortador( txtRazCliBaixa.getVlrString().trim() );
				 * historico.setValor( Funcoes.strToBd( tabBaixa.getValor( iLin, EColTabBaixa.VLRPARC.ordinal() ).toString() ) ); baixaRecBean.setObservacao( historico.getHistoricodecodificado() ); } else { baixaRecBean.setObservacao( (String) tabBaixa.getValor( iLin, EColTabBaixa.OBS.ordinal() ) );
				 * }
				 */

				baixaRecBean.setObservacao( (String) tabBaixa.getValor( iLin, EColTabBaixa.OBS.ordinal() ) );
				dl.setConexao( con );
				dl.setValores( baixaRecBean );
				dl.setVisible( true );



				if ( dl.OK ) {

					baixaRecBean = dl.getValores();
					//baixaRecBean.getValorPago();
					//baixaRecBean.setValorPago( valorAPagar);

					try {
						daomovimento.updateItReceber(baixaRecBean, iAnoCC, iCodRec, iNParcItRec);
					} catch ( SQLException err ) {
						Funcoes.mensagemErro( this, "Erro ao baixar parcela!\n" + err.getMessage(), true, con, err );
					}

				}

				dl.dispose();
				carregaGridBaixa();
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}



	private void baixaTabManut(){
		PreparedStatement ps = null;
		StringBuffer sSQL = new StringBuffer();
		DLBaixaRec dl = null;
		ImageIcon imgStatusAt = null;
		Integer codcliuniq = null;
		Integer codclianterior = null;
		boolean clienteuniq = true;
		int iCodRec = 0;
		int iNParcItRec = 0;
		BigDecimal saldoABaixar = new BigDecimal(0);


		imgStatusAt = (ImageIcon) tabManut.getValor( tabManut.getLinhaSel(), EColTabManut.IMGSTATUS.ordinal() );

		List<Integer> selecionados = new ArrayList<Integer>();
		BigDecimal valorTotalParc = new BigDecimal( 0 );
		BigDecimal valorTotalPag = new BigDecimal( 0 );

		for ( int row = 0; row < tabManut.getNumLinhas(); row++ ) {

			Boolean selecionado = (Boolean)tabManut.getValor( row, EColTabManut.SEL.ordinal() ) ;

			if (selecionado){

				if( (ImageIcon) tabManut.getValor( row, EColTabManut.IMGSTATUS.ordinal() ) == imgPago || 
						(ImageIcon) tabManut.getValor( row, EColTabManut.IMGSTATUS.ordinal() ) == imgRenegociado ||
						(ImageIcon) tabManut.getValor( row, EColTabManut.IMGSTATUS.ordinal() ) == imgRenegociadoPago){
					imgStatusAt = (ImageIcon) tabManut.getValor( row, EColTabManut.IMGSTATUS.ordinal() );
				}
				codcliuniq = (Integer) tabManut.getValor( row, EColTabManut.CODCLI.ordinal() );

				valorTotalParc = valorTotalParc.add( 
						ConversionFunctions.stringCurrencyToBigDecimal( 
								((StringDireita) tabManut.getValor( row, EColTabManut.VLRPARCITREC.ordinal()) ).toString() ) );

				valorTotalPag = valorTotalPag.add( 
						ConversionFunctions.stringCurrencyToBigDecimal( 
								((StringDireita) tabManut.getValor( row, EColTabManut.VLRAPAGITREC.ordinal()) ).toString() ) );

				selecionados.add( row );
				if ( (clienteuniq) && (codclianterior!=null) && (!codclianterior.equals( codcliuniq ) ) ) {
					clienteuniq = false;
				}
				codclianterior = codcliuniq;
			}
		}

		if(selecionados.size() == 0 ){

			if( tabManut.getSelectedRow() < 0 ) {
				Funcoes.mensagemInforma( this, "Selecione um título!" );
				return;
			} 

			int row = tabManut.getLinhaSel();
			imgStatusAt = (ImageIcon) tabManut.getValor( row, EColTabManut.IMGSTATUS.ordinal() );

			valorTotalParc = valorTotalParc.add( 
					ConversionFunctions.stringCurrencyToBigDecimal( 
							((StringDireita) tabManut.getValor( row, EColTabManut.VLRPARCITREC.ordinal()) ).toString() ) );

			valorTotalPag = valorTotalPag.add( 
					ConversionFunctions.stringCurrencyToBigDecimal( 
							((StringDireita) tabManut.getValor( row, EColTabManut.VLRAPAGITREC.ordinal()) ).toString() ) );

			selecionados.add(row);

		}

		if ( imgStatusAt == imgPago || imgStatusAt == imgRenegociadoPago || imgStatusAt == imgPagoComParciais ) {
			Funcoes.mensagemInforma( this, "Parcela(s) selecionada(s) já baixada(s)!" );
			return;
		}

		if ( imgStatusAt == imgRenegociado ) {
			Funcoes.mensagemInforma( this, "Parcela(s) selecionada(s) já renegociada(s)!" );
			return;
		}

		int primeiroSelecionado = selecionados.get( 0 );

		iCodRec = Integer.parseInt( tabManut.getValor( primeiroSelecionado, EColTabManut.CODREC.ordinal() ).toString() );
		iNParcItRec = Integer.parseInt( tabManut.getValor( primeiroSelecionado, EColTabManut.NPARCITREC.ordinal() ).toString() );

		String[] sPlanoConta = null;
		try {
			sPlanoConta = daomovimento.getPlanejamentoContaRec( iCodRec );
		} catch ( SQLException e) {
			Funcoes.mensagemErro( this, "Erro ao buscar Conta!\n" + e.getMessage(), true, con, e );
		}


		String numconta = (String) tabManut.getValor( primeiroSelecionado, EColTabManut.NUMCONTA.ordinal() );
		String codplan = (String) tabManut.getValor( primeiroSelecionado, EColTabManut.CODPLAN.ordinal() );
		String codcc = (String) tabManut.getValor( primeiroSelecionado, EColTabManut.CODCC.ordinal() );

		boolean categoriaRequerida = false;
		for(Integer row : selecionados){
			String codPlan = (String) tabManut.getValor( row , EColTabManut.CODPLAN.ordinal() );

			if(codPlan == null || codPlan.trim().length() == 0){
				categoriaRequerida = true;
			}
		}

		if ( "".equals( numconta ) || numconta == null ) {
			numconta = sPlanoConta[ 2 ];
			if ( numconta == null ) {
				numconta = "";
			}
		}
		if ( "".equals( codplan ) || codplan == null ) {
			codplan = sPlanoConta[ 1 ];
			if ( codplan == null ) {
				codplan = "";
			}
		}
		if ( "".equals( codcc ) || codcc == null ) {
			codcc = sPlanoConta[ 3 ];
			if ( codcc == null ) {
				codcc = "";
			}
		}

		dl = new DLBaixaRec( this, selecionados.size() > 1, categoriaRequerida, clienteuniq );
		DLBaixaRec.BaixaRecBean baixaRecBean = dl.new BaixaRecBean();

		baixaRecBean.setRecebimento( (Integer) tabManut.getValor( primeiroSelecionado, EColTabManut.CODREC.ordinal() ) );
		baixaRecBean.setParcela( (Integer) tabManut.getValor( primeiroSelecionado, EColTabManut.NPARCITREC.ordinal() ) );
		baixaRecBean.setCliente( (Integer) tabManut.getValor( primeiroSelecionado, EColTabManut.CODCLI.ordinal() ) );
		baixaRecBean.setRazaoSocialCliente( (String) tabManut.getValor( primeiroSelecionado, EColTabManut.RAZCLI.ordinal() ) );
		baixaRecBean.setConta( numconta );
		baixaRecBean.setPlanejamento( codplan );
		baixaRecBean.setCentroCusto( codcc );
		baixaRecBean.setDocumento( "".equals( tabManut.getValor( primeiroSelecionado, EColTabManut.DOCLANCA.ordinal() ) ) ? String.valueOf( tabManut.getValor( primeiroSelecionado, EColTabManut.DOCVENDA.ordinal() ) ) : String.valueOf( tabManut.getValor( primeiroSelecionado, EColTabManut.DOCLANCA.ordinal() ) ) );
		baixaRecBean.setDataEmissao( Funcoes.strDateToDate( (String) tabManut.getValor( primeiroSelecionado, EColTabManut.DTEMIT.ordinal() ) ) );
		baixaRecBean.setDataVencimento( Funcoes.strDateToDate( (String) tabManut.getValor( primeiroSelecionado, EColTabManut.DTVENC.ordinal() ) ) );
		baixaRecBean.setValorDesconto( ConversionFunctions.stringCurrencyToBigDecimal(  
				( (StringDireita) tabManut.getValor( primeiroSelecionado, EColTabManut.VLRDESCITREC.ordinal() ) ).toString() ) ) ;
		baixaRecBean.setValorJuros( ConversionFunctions.stringCurrencyToBigDecimal(  
				( (StringDireita) tabManut.getValor( primeiroSelecionado, EColTabManut.VLRJUROSITREC.ordinal() ) ).toString() ) ) ;
		baixaRecBean.setValorPagoParc( ConversionFunctions.stringCurrencyToBigDecimal(  
				( (StringDireita) tabManut.getValor( primeiroSelecionado, EColTabManut.VLRPAGOITREC.ordinal() ) ).toString() ) ) ;
		baixaRecBean.setValorParcela( valorTotalParc );
		baixaRecBean.setValorAPagar( valorTotalPag );

		if ( "".equals( tabManut.getValor( primeiroSelecionado, EColTabManut.DTPAGTOITREC.ordinal() ) ) ) {
			baixaRecBean.setDataPagamento( new Date() );
			baixaRecBean.setValorPago( new BigDecimal( 0 ) );
		} else {
			baixaRecBean.setDataPagamento( Funcoes.strDateToDate( (String) tabManut.getValor( primeiroSelecionado, EColTabManut.DTPAGTOITREC.ordinal() ) ) );
			baixaRecBean.setValorPago( new BigDecimal( 0 ) );
		}

		baixaRecBean.setDataLiquidacao( new Date() );

		baixaRecBean.setObservacao( (String) tabManut.getValor( primeiroSelecionado, EColTabManut.OBSITREC.ordinal() ) );

		baixaRecBean.setEmBordero( "RB".equals( tabManut.getValor( primeiroSelecionado, EColTabManut.STATUS.ordinal() ) ) );

		dl.setConexao( con );
		dl.setValores( baixaRecBean );
		// Entra na dialog modal
		dl.setVisible( true );

		if ( dl.OK ) {

			baixaRecBean = dl.getValores();

			saldoABaixar = baixaRecBean.getValorPago(); 


			boolean manterDados = false;
			if(baixaRecBean.getPlanejamento() != null && baixaRecBean.getPlanejamento().trim().length() > 0) {
				for(Integer row : selecionados){
					String codCategoria = (String) tabManut.getValor( row , EColTabManut.CODPLAN.ordinal() );
					String codCC = (String) tabManut.getValor( row, EColTabManut.CODCC.ordinal() );
					if( ( (codCategoria != null) && (!"".equals(codCategoria.trim()) ) ) ||
							( (codCC !=null) && (!"".equals( codCC.trim() ))) ){
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

			/*BigDecimal valorRestante = baixaRecBean.getValorPago();
			BigDecimal baixaRecBean.getValorAPagar();
			if( valorRestante.compareTo( valorTotalPag ) != 0 ){
				if(Funcoes.mensagemConfirma( this, "Valor informado é menor que o valor total selecionado. Deseja Continuar?" ) 
						== JOptionPane.NO_OPTION){
					return;
				}
			}*/

			BigDecimal valorapagar= baixaRecBean.getValorAPagar();
			BigDecimal valorpagto = baixaRecBean.getValorPago();
			String statusitrec = "RP";

			if( valorapagar.doubleValue() > valorpagto.doubleValue() ){
				if(Funcoes.mensagemConfirma( this, "Valor do Pagamento é menor que o valor total a ser pago. Deseja Continuar?" ) 
						== JOptionPane.NO_OPTION){
					return;
				}	
				statusitrec = "RL";
			}

			/*sSQL.append( "UPDATE FNITRECEBER SET NUMCONTA=?,CODEMPCA=?,CODFILIALCA=?,CODPLAN=?,CODEMPPN=?,CODFILIALPN=?," );
			sSQL.append( "DOCLANCAITREC=?,DTPAGOITREC=?,VLRPAGOITREC=VLRPAGOITREC+?,VLRDESCITREC=?,VLRJUROSITREC=?,ANOCC=?," );
			sSQL.append( "CODCC=?,CODEMPCC=?,CODFILIALCC=?,OBSITREC=? ");
			sSQL.append( ", STATUSITREC=? " );
			sSQL.append( ", DTLIQITREC=?, MULTIBAIXA=? , ALTUSUITREC=? ");
			sSQL.append( "WHERE CODREC=? AND NPARCITREC=? AND CODEMP=? AND CODFILIAL=?" );*/

			try {
				
				daomovimento.baixaTabManut( selecionados, baixaRecBean, tabManut, manterDados, valorpagto, statusitrec, iAnoCC );
				/*BigDecimal valorapagitrec = null;
				BigDecimal valorpagoitrec = null;

				for(Integer row : selecionados){
					if ( selecionados.size() > 1 ) {
						baixaRecBean.setValorDesconto( ConversionFunctions.stringCurrencyToBigDecimal(  
								( (StringDireita) tabManut.getValor( row, EColTabManut.VLRDESCITREC.ordinal() ) ).toString() ) ) ;
						baixaRecBean.setValorJuros( ConversionFunctions.stringCurrencyToBigDecimal(  
								( (StringDireita) tabManut.getValor( row, EColTabManut.VLRJUROSITREC.ordinal() ) ).toString() ) ) ;
						baixaRecBean.setValorPagoParc( ConversionFunctions.stringCurrencyToBigDecimal(  
								( (StringDireita) tabManut.getValor( row, EColTabManut.VLRPAGOITREC.ordinal() ) ).toString() ) ) ;
					}


					ps = con.prepareStatement( sSQL.toString() );
					ps.setString( PARAM_UPDATE_IR.NUMCONTA.ordinal() , baixaRecBean.getConta() );
					ps.setInt( PARAM_UPDATE_IR.CODEMPCA.ordinal(), Aplicativo.iCodEmp );
					ps.setInt( PARAM_UPDATE_IR.CODFILIALCA.ordinal(), ListaCampos.getMasterFilial( "FNCONTA" ) );

					if(manterDados &&  
							( ((String) tabManut.getValor(row, EColTabManut.CODPLAN.ordinal()) ).trim().length() > 0 ) ){
						ps.setString( PARAM_UPDATE_IR.CODPLAN.ordinal(), (String) tabManut.getValor( row, EColTabManut.CODPLAN.ordinal() ) );
					}else{
						ps.setString( PARAM_UPDATE_IR.CODPLAN.ordinal(), baixaRecBean.getPlanejamento() );
					}

					ps.setInt( PARAM_UPDATE_IR.CODEMPPN.ordinal(), Aplicativo.iCodEmp );
					ps.setInt( PARAM_UPDATE_IR.CODFILIALPN.ordinal(), ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
					ps.setDate( PARAM_UPDATE_IR.DTPAGOITREC.ordinal(), Funcoes.dateToSQLDate( baixaRecBean.getDataPagamento() ) );

					if (selecionados.size() == 1) {
						ps.setString( PARAM_UPDATE_IR.DOCLANCAITREC.ordinal(), baixaRecBean.getDocumento() );						
						ps.setBigDecimal( PARAM_UPDATE_IR.VLRPAGOITREC.ordinal(), baixaRecBean.getValorPago() );
					} else {
						valorapagitrec = ConversionFunctions.stringCurrencyToBigDecimal( 
								((StringDireita) tabManut.getValor( row, EColTabManut.VLRAPAGITREC.ordinal()) ).toString() );
						valorpagoitrec = valorapagitrec;
						// Se o valor digitado na dialog de baixa for maior que o valor a pagar da parcela e
						// o item não for o último, então, o valor pago será o total a pagar 
						if ( (valorpagto.compareTo( valorapagitrec )>0 ) && ( row.equals( (Integer) selecionados.get( (selecionados.size()-1 ) ) ) ) ) {
							valorpagoitrec = valorapagitrec;
							statusitrec = "RP";
						} else if ( (valorpagto.compareTo( valorapagitrec )>0) && (row.equals( (Integer) selecionados.get( (selecionados.size()-1 ) ) ) ) ) {
							valorpagoitrec = valorpagto;
							baixaRecBean.setValorPago( valorpagoitrec ) ;// Setar o valor do pagamento
							baixaRecBean.setValorJuros( valorpagoitrec.subtract( valorapagitrec ));  // Setar o valor de juros
							tabManut.setValor( Funcoes.bdToStr( baixaRecBean.getValorJuros() ), row, EColTabManut.VLRPAGOITREC.ordinal() );
							statusitrec = "RP";
						} else if ( (valorpagto.compareTo( valorapagitrec )<0) ) {
							valorpagoitrec = valorpagto;
							statusitrec = "RL";
						} else {
							statusitrec = "RP";
						}
						// Setando o valor pago no Bean
						baixaRecBean.setValorPago( valorpagoitrec );
						// Removendo o valor pago do totalizador de saldo
						valorpagto = valorpagto.subtract( valorpagoitrec );
						// Ajustar o tabManut para evitar problemas na geração dos lançamentos,
						// Pois o método para gerar lançamentos financeiros busca da tabManut

						tabManut.setValor( Funcoes.bdToStr( valorpagoitrec ), row, EColTabManut.VLRAPAGITREC.ordinal() );

						// Passando os parâmetro de documento e valor de pagamento
						ps.setString( PARAM_UPDATE_IR.DOCLANCAITREC.ordinal(), "".equals( tabManut.getValor( row, EColTabManut.DOCLANCA.ordinal() ) ) ? 
								String.valueOf( tabManut.getValor( row, EColTabManut.DOCVENDA.ordinal() ) ) : 
									String.valueOf( tabManut.getValor( row, EColTabManut.DOCLANCA.ordinal() ) ) );
						ps.setBigDecimal( PARAM_UPDATE_IR.VLRPAGOITREC.ordinal(), baixaRecBean.getValorPago() );
					}
					NONE, NUMCONTA, CODEMPCA, CODFILIALCA, CODPLAN, CODEMPPN, CODFILIALPN, 
					DOCLANCAITREC, DTPAGOITREC, VLRPAGOITREC, VLRDESCITREC, VLRJUROSITREC, ANOCC, CODCC, CODEMPCC, 
					CODFILIALCC, OBSITREC, DTLIGITREC, MULTIBAIXA, ALTUSUITREC, CODREC, NPARCITREC, CODEMP, CODFILAIL

					ps.setBigDecimal( PARAM_UPDATE_IR.VLRDESCITREC.ordinal(), baixaRecBean.getValorDesconto() );
					ps.setBigDecimal( PARAM_UPDATE_IR.VLRJUROSITREC.ordinal(), baixaRecBean.getValorJuros() );
					if (manterDados) {
						if (!"".equals( (String ) tabManut.getValor( row, EColTabManut.CODCC.ordinal() ) ) ) {
							baixaRecBean.setCentroCusto( (String ) tabManut.getValor( row, EColTabManut.CODCC.ordinal() ) );
						}
					}
					if ( baixaRecBean.getCentroCusto() == null || "".equals( baixaRecBean.getCentroCusto().trim() ) ) {
						ps.setNull( PARAM_UPDATE_IR.ANOCC.ordinal(), Types.INTEGER );
						ps.setNull( PARAM_UPDATE_IR.CODCC.ordinal(), Types.CHAR );
						ps.setNull( PARAM_UPDATE_IR.CODEMPCC.ordinal(), Types.INTEGER );
						ps.setNull( PARAM_UPDATE_IR.CODFILIALCC.ordinal(), Types.INTEGER );
					}
					else {
						ps.setInt( PARAM_UPDATE_IR.ANOCC.ordinal(), iAnoCC );
						ps.setString( PARAM_UPDATE_IR.CODCC.ordinal(), baixaRecBean.getCentroCusto() );
						ps.setInt( PARAM_UPDATE_IR.CODEMPCC.ordinal(), Aplicativo.iCodEmp );
						ps.setInt( PARAM_UPDATE_IR.CODFILIALCC.ordinal(), ListaCampos.getMasterFilial( "FNCC" ) );
						tabManut.setValor( baixaRecBean.getCentroCusto() ,row, EColTabManut.CODCC.ordinal() );
					}

					ps.setString( PARAM_UPDATE_IR.OBSITREC.ordinal(), baixaRecBean.getObservacao() );

					ps.setString( PARAM_UPDATE_IR.STATUSITREC.ordinal(), statusitrec );

					ps.setDate( PARAM_UPDATE_IR.DTLIGITREC.ordinal(), Funcoes.dateToSQLDate( baixaRecBean.getDataLiquidacao() ) );

					ps.setString( PARAM_UPDATE_IR.MULTIBAIXA.ordinal(), (selecionados.size() > 1 ? "S" : "N") );
					ps.setString( PARAM_UPDATE_IR.ALTUSUITREC.ordinal(), "S" );
					ps.setInt( PARAM_UPDATE_IR.CODREC.ordinal(), (Integer) tabManut.getValor( row, EColTabManut.CODREC.ordinal() ) );
					ps.setInt( PARAM_UPDATE_IR.NPARCITREC.ordinal(), (Integer) tabManut.getValor( row, EColTabManut.NPARCITREC.ordinal() ) );
					ps.setInt( PARAM_UPDATE_IR.CODEMP.ordinal(), Aplicativo.iCodEmp );
					ps.setInt( PARAM_UPDATE_IR.CODFILIAL.ordinal(), ListaCampos.getMasterFilial( "FNRECEBER" ) );

					ps.executeUpdate();

					if ( valorpagto.compareTo( BigDecimal.ZERO )<=0) {
						break;
					}
				}*/
				this.geraLancamentosFinanceiros( selecionados, baixaRecBean, manterDados, saldoABaixar);
				daomovimento.setAltUsuItRec( iCodRec, iNParcItRec, "N" );
				con.commit();

			} catch ( SQLException err ) {
				try {
					con.rollback();
				} catch ( SQLException e ) {
					e.printStackTrace();
				}

				Funcoes.mensagemErro( this, "Erro ao baixar parcela!\n" + err.getMessage(), true, con, err );
			}
		}

		dl.dispose();
		carregaGridManut( bBuscaAtual );
	}

	private void geraLancamentosFinanceiros(List<Integer> selecionados, DLBaixaRec.BaixaRecBean baxaRec, boolean manterDados, BigDecimal saldoABaixar) throws SQLException{
		if(selecionados.size() == 1){
			return;
		}

		int codrec = 0;
		int nparcitrec = 0;
		BigDecimal vlrdescitrec = null;
		BigDecimal vlrjurositrec = null;
		String codcc = null;
		String codplandc = ( (String) prefere.get( "codplandc" ) ).trim();
		String codplanjr = ( (String) prefere.get( "codplanjr" ) ).trim();

		PreparedStatement ps = null;
		ResultSet rs = null;

/*		//Recupera o Próximo Sequencial 
		ps = con.prepareStatement( "SELECT ISEQ FROM SPGERANUM(?, ?, 'LA') " );
		ps.setInt( 1, Aplicativo.iCodEmp );
		ps.setInt( 2, ListaCampos.getMasterFilial( "SPGERANUM" ) );

		rs = ps.executeQuery();
		rs.next();*/
		int codlanca = daomovimento.gerarSeqLanca( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "SPGERANUM" ) );  
		// param 1 = (Integer) tabManut.getValor( selecionados.get( 0 ) , EColTabManut.CODREC.ordinal())
		daomovimento.geraFNLanca( (Integer) tabManut.getValor( selecionados.get( 0 ) , EColTabManut.CODREC.ordinal()), baxaRec, codlanca );
		 
		int codsublanca = 1;

		for(Integer row : selecionados){

			codrec = (Integer) tabManut.getValor( row, EColTabManut.CODREC.ordinal() );
			nparcitrec = (Integer) tabManut.getValor( row, EColTabManut.NPARCITREC.ordinal() );
			vlrdescitrec = ConversionFunctions.stringCurrencyToBigDecimal( 
					((StringDireita) tabManut.getValor( row , EColTabManut.VLRDESCITREC.ordinal()) ).toString() ); 
			vlrjurositrec = ConversionFunctions.stringCurrencyToBigDecimal( 
					((StringDireita) tabManut.getValor( row , EColTabManut.VLRJUROSITREC.ordinal()) ).toString() ); 

			String codplan = null;
			if(manterDados && 
					((String) tabManut.getValor( row , EColTabManut.CODPLAN.ordinal())).trim().length() > 0){
				codplan = (String) tabManut.getValor( row , EColTabManut.CODPLAN.ordinal() );
			}else{
				codplan = baxaRec.getPlanejamento() ;
			}
			Integer codcli = (Integer) tabManut.getValor( row , EColTabManut.CODCLI.ordinal() );

			codcc = ( (String)  tabManut.getValor( row, EColTabManut.CODCC.ordinal() ) ).trim();			

			Date datasublanca = Funcoes.dateToSQLDate( baxaRec.getDataPagamento() );
			Date dtprevsublanca = Funcoes.dateToSQLDate( baxaRec.getDataPagamento() );
			String dtitrec = (String) tabManut.getValor( row , EColTabManut.DTVENC.ordinal() );

			BigDecimal vlrsublanca = ConversionFunctions.stringCurrencyToBigDecimal( 
					((StringDireita) tabManut.getValor( row , EColTabManut.VLRAPAGITREC.ordinal()) ).toString() );

			if (vlrsublanca.compareTo( saldoABaixar ) > 0) {
				vlrsublanca = saldoABaixar;
			}

			vlrsublanca = (vlrsublanca.add( vlrdescitrec ).subtract( vlrjurositrec ) ).negate();
			saldoABaixar = saldoABaixar.subtract( vlrsublanca.negate() );

			daomovimento.geraSublanca(codrec, nparcitrec, codlanca, codsublanca, codplan, codcli, codcc, dtitrec, datasublanca, dtprevsublanca, vlrsublanca, "P" , iAnoCC);

			if(vlrdescitrec.compareTo( new BigDecimal( 0 ) ) > 0 ) {	
				codsublanca++;
				vlrsublanca = ConversionFunctions.stringCurrencyToBigDecimal( 
						((StringDireita) tabManut.getValor( row , EColTabManut.VLRDESCITREC.ordinal()) ).toString() ); 
				if( !"".equals( codplandc ) ){
					codplan = codplandc;
				}
				daomovimento.geraSublanca(codrec, nparcitrec, codlanca, codsublanca, codplan, codcli, codcc, dtitrec, datasublanca, dtprevsublanca, vlrsublanca, "D", iAnoCC);

			}

			if(vlrjurositrec.compareTo( new BigDecimal(0) ) > 0 ) {	
				codsublanca++;
				vlrsublanca = ConversionFunctions.stringCurrencyToBigDecimal( 
						((StringDireita) tabManut.getValor( row , EColTabManut.VLRJUROSITREC.ordinal()) ).toString() ).negate() ;
				if(!"".equals( codplanjr ) ) {
					codplan = codplanjr;
				}
				daomovimento.geraSublanca(codrec, nparcitrec, codlanca, codsublanca, codplan, codcli, codcc, dtitrec, datasublanca, dtprevsublanca, vlrsublanca, "J", iAnoCC);						
			}

			if( saldoABaixar.compareTo( BigDecimal.ZERO ) <=0 ){
				break;
			}

			codsublanca++;			
		}
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

	private void impBoleto() {

		DLImpBoletoRec dl = null;

		if ( tabManut.getLinhaSel() < 0 ) {
			Funcoes.mensagemInforma( this, "Selecione uma parcela no grid!" );
			return;
		}

		dl = new DLImpBoletoRec( this, con, (Integer) tabManut.getValor( tabManut.getLinhaSel(), EColTabManut.CODREC.ordinal() ), (Integer) tabManut.getValor( tabManut.getLinhaSel(), EColTabManut.NPARCITREC.ordinal() ) );

		dl.setVisible( true );

		if ( dl.OK ) {

			dl.imprimir();
		}
	}

	private void abreHistorico() {

		try {

			if ( tabManut.getLinhaSel() < 0 ) {
				Funcoes.mensagemInforma( this, "Selecione uma parcela!" );
				return;
			}

			FCRM tela = null;

			if ( fPrim.temTela( "Gestão de relacionamento com clientes" ) ) {
				tela = (FCRM) fPrim.getTela( "org.freedom.modulos.crm.FCRM" );
				if ( tela != null ) {
					tela.show();
				}
			}
			else {
				Integer codcli = ( (Integer) tabManut.getValor( tabManut.getLinhaSel(), EColTabManut.CODCLI.ordinal() ) );
				Integer codrec = ( (Integer) tabManut.getValor( tabManut.getLinhaSel(), EColTabManut.CODREC.ordinal() ) );
				Integer nparcitrec = ( (Integer) tabManut.getValor( tabManut.getLinhaSel(), EColTabManut.NPARCITREC.ordinal() ) );
				tela = new FCRM();
				fPrim.criatela( "Gestão de relacionamento com clientes", tela, con );
				tela.loadFin( codcli, codrec, nparcitrec, true);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private void abreBordero() {

		try {

			DLBordero bordero = new DLBordero();
			List<DLBordero.GridBordero> gridBordero = new ArrayList<DLBordero.GridBordero>();

			ResultSet rs = daomovimento.getResultSetManut( true, true, false, validaPeriodo(), rgData.getVlrString(), rgVenc.getVlrString(), cbRecebidas.getVlrString(), cbCanceladas.getVlrString(),
					cbEmBordero.getVlrString(), cbRenegociado.getVlrString(), cbAReceber.getVlrString(), cbEmRenegociacao.getVlrString(),
					cbRecParcial.getVlrString(), txtCodCliFiltro.getVlrInteger(), txtCodRecManut.getVlrInteger(), txtSeqNossoNumero.getVlrInteger(),
					dIniManut, dFimManut);
			DLBordero.GridBordero grid = null;

			while ( rs.next() ) {

				grid = bordero.new GridBordero();
				grid.setStatus( rs.getString( "STATUSITREC" ) );
				grid.setDataVencimento( Funcoes.sqlDateToDate( rs.getDate( "DTVENCITREC" ) ) );
				grid.setCodigoReceber( rs.getInt( "CODREC" ) );
				grid.setParcela( rs.getInt( "NPARCITREC" ) );
				grid.setDocumentoLancamento( rs.getString( "DOCLANCAITREC" ) != null ? rs.getString( "DOCLANCAITREC" ) : ( rs.getString( "DOCREC" ) != null ? rs.getString( "DOCREC" ) + "/" + rs.getString( "NPARCITREC" ) : "" ) );
				grid.setCodigoCliente( rs.getInt( "CODCLI" ) );
				grid.setRazaoCliente( rs.getString( "RAZCLI" ) );
				grid.setDocumentoVenda( rs.getString( "DOCVENDA" ) );
				grid.setValorParcela( rs.getBigDecimal( "VLRPARCITREC" ) );
				grid.setDataPagamento( Funcoes.sqlDateToDate( rs.getDate( "DTPAGOITREC" ) ) );
				grid.setValorPago( rs.getBigDecimal( "VLRPAGOITREC" ) );
				grid.setValorDesconto( rs.getBigDecimal( "VLRDESCITREC" ) );
				grid.setValorJuros( rs.getBigDecimal( "VLRJUROSITREC" ) );
				grid.setValorAReceber( rs.getBigDecimal( "VLRAPAGITREC" ) );
				grid.setConta( rs.getString( "NUMCONTA" ) );
				grid.setDescricaoConta( rs.getString( "DESCCONTA" ) );
				grid.setPlanejamento( rs.getString( "CODPLAN" ) );
				grid.setDescricaoPlanejamento( rs.getString( "DESCPLAN" ) );
				grid.setBanco( rs.getString( "CODBANCO" ) );
				grid.setNomeBanco( rs.getString( "NOMEBANCO" ) );
				grid.setObservacao( rs.getString( "OBSITREC" ) );

				gridBordero.add( grid );
			}

			con.commit();

			bordero.setConexao( con );
			bordero.carregaGrid( gridBordero );

			bordero.setVisible( true );
			bordero.dispose();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private void abreRenegocReceb() {

		try {

			DLRenegRec renegociacao = new DLRenegRec();
			List<DLRenegRec.GridRenegRec> gridRenegociacao = new ArrayList<DLRenegRec.GridRenegRec>();

			ResultSet rs = daomovimento.getResultSetManut( true, false, true , validaPeriodo(), rgData.getVlrString(), rgVenc.getVlrString(), cbRecebidas.getVlrString(), cbCanceladas.getVlrString(),
					cbEmBordero.getVlrString(), cbRenegociado.getVlrString(), cbAReceber.getVlrString(), cbEmRenegociacao.getVlrString(),
					cbRecParcial.getVlrString(), txtCodCliFiltro.getVlrInteger(), txtCodRecManut.getVlrInteger(), txtSeqNossoNumero.getVlrInteger(),
					dIniManut, dFimManut);

			DLRenegRec.GridRenegRec grid = null;

			while ( rs.next() ) {

				grid = renegociacao.new GridRenegRec();

				grid.setStatus( rs.getString( "STATUSITREC" ) );
				grid.setDataVencimento( Funcoes.sqlDateToDate( rs.getDate( "DTVENCITREC" ) ) );
				grid.setCodigoReceber( rs.getInt( "CODREC" ) );
				grid.setParcela( rs.getInt( "NPARCITREC" ) );
				grid.setDocumentoLancamento( rs.getString( "DOCLANCAITREC" ) != null ? rs.getString( "DOCLANCAITREC" ) : ( rs.getString( "DOCREC" ) != null ? rs.getString( "DOCREC" ) + "/" + rs.getString( "NPARCITREC" ) : "" ) );
				grid.setCodigoCliente( rs.getInt( "CODCLI" ) );
				grid.setRazaoCliente( rs.getString( "RAZCLI" ) );
				grid.setDocumentoVenda( rs.getString( "DOCVENDA" ) );
				grid.setValorParcela( rs.getBigDecimal( "VLRPARCITREC" ) );
				grid.setDataPagamento( Funcoes.sqlDateToDate( rs.getDate( "DTPAGOITREC" ) ) );
				grid.setValorPago( rs.getBigDecimal( "VLRPAGOITREC" ) );
				grid.setValorDesconto( rs.getBigDecimal( "VLRDESCITREC" ) );
				grid.setValorJuros( rs.getBigDecimal( "VLRJUROSITREC" ) );
				grid.setValorAReceber( rs.getBigDecimal( "VLRAPAGITREC" ) );
				grid.setConta( rs.getString( "NUMCONTA" ) );
				grid.setDescricaoConta( rs.getString( "DESCCONTA" ) );
				grid.setPlanejamento( rs.getString( "CODPLAN" ) );
				grid.setDescricaoPlanejamento( rs.getString( "DESCPLAN" ) );
				grid.setBanco( rs.getString( "CODBANCO" ) );
				grid.setNomeBanco( rs.getString( "NOMEBANCO" ) );
				grid.setObservacao( rs.getString( "OBSITREC" ) );

				gridRenegociacao.add( grid );
			}

			con.commit();

			renegociacao.setConexao( con );
			renegociacao.carregaGrid( gridRenegociacao );
			renegociacao.txtCodCli.setVlrInteger( txtCodCliFiltro.getVlrInteger() );

			renegociacao.setVisible( true );
			renegociacao.dispose();

		} 
		catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	public void setRec( int codigoRecebimento ) {

		txtCodRecManut.setVlrInteger( codigoRecebimento );
		lcRecManut.carregaDados();
		tpn.setSelectedIndex( 2 );
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
		else if ( evt.getSource() == btEstorno ) {
			estorno();
		}
		else if ( evt.getSource() == btCancItem ) {
			cancelaItem();
		}
		else if ( evt.getSource() == btCarregaBaixas ) {
			if ( tabBaixa.getLinhaSel() > -1 ) {
				int rowSelected = tabBaixa.getLinhaSel();
				consBaixa( txtCodRecBaixa.getVlrInteger().intValue(), 
						Integer.parseInt( tabBaixa.getValor( rowSelected, 3 ).toString() ), 
						ConversionFunctions.stringToBigDecimal( tabBaixa.getValor( rowSelected, EColTabBaixa.VLRPARC.ordinal() ) ), 
						ConversionFunctions.stringToBigDecimal( tabBaixa.getValor( rowSelected, EColTabBaixa.VLRPAGO.ordinal() ) ), 
						ConversionFunctions.stringToBigDecimal( tabBaixa.getValor( rowSelected, EColTabBaixa.VLRDESC.ordinal() ) ), 
						ConversionFunctions.stringToBigDecimal( tabBaixa.getValor( rowSelected, EColTabBaixa.VLRJUROS.ordinal() ) ), 
						ConversionFunctions.stringToBigDecimal( tabBaixa.getValor( rowSelected, EColTabBaixa.VLRAPAG.ordinal() ) ) );
			}
			else {
				Funcoes.mensagemInforma( this, "Selecione um título no grid!" );
			}
		}
		else if ( evt.getSource() == btCarregaBaixasMan ) {
			if ( tabManut.getLinhaSel() > -1 ) {
				int rowSelected = tabManut.getLinhaSel();
				consBaixa( Integer.parseInt( tabManut.getValor( rowSelected, EColTabManut.CODREC.ordinal() ).toString() ), 
						Integer.parseInt( tabManut.getValor( rowSelected, EColTabManut.NPARCITREC.ordinal() ).toString() ), 
						ConversionFunctions.stringToBigDecimal( tabManut.getValor( rowSelected, EColTabManut.VLRPARCITREC.ordinal() ) ), 
						ConversionFunctions.stringToBigDecimal( tabManut.getValor( rowSelected, EColTabManut.VLRPAGOITREC.ordinal() ) ), 
						ConversionFunctions.stringToBigDecimal( tabManut.getValor( rowSelected, EColTabManut.VLRDESCITREC.ordinal() ) ), 
						ConversionFunctions.stringToBigDecimal( tabManut.getValor( rowSelected, EColTabManut.VLRJUROSITREC.ordinal() ) ), 
						ConversionFunctions.stringToBigDecimal( tabManut.getValor( rowSelected, EColTabManut.VLRAPAGITREC.ordinal() ) ) );
			}else {
				Funcoes.mensagemInforma( this, "Selecione um título no grid!" );
			}
		}
		else if ( evt.getSource() == btCarregaGridManut ) {
			bBuscaAtual = true;
			carregaGridManut( bBuscaAtual );
		}
		else if ( evt.getSource() == btCarregaVenda ) {
			carregaVenda();
		}
		else if ( evt.getSource() == btImpBol ) {
			impBoleto();
		}
		else if ( evt.getSource() == btHistorico ) {
			abreHistorico();
		}
		else if ( evt.getSource() == btBordero ) {
			abreBordero();
		}
		else if ( evt.getSource() == btRenegRec ) {
			if(txtCodCliFiltro.getText().trim().equals( "" ) ){
				Funcoes.mensagemInforma( this, "Selecione um cliente para renegocição." );
				return;
			}
			abreRenegocReceb();
		}
		else if(evt.getSource() instanceof JMenuItem) {

			JMenuItem menu = (JMenuItem) evt.getSource();

			String opcao  = menu.getText();

			Integer codsinal = null;

			if(menu != menucancelacor && menu != menucadastracor ) {

				codsinal = Integer.parseInt( opcao.substring( 0, opcao.indexOf( "-" ) ));

			}
			else if (evt.getSource() == menucadastracor){

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
				daomovimento.atualizaCor( codsinal, 
						Integer.parseInt( tabManut.getValor( tabManut.getLinhaSel(), EColTabManut.CODREC.ordinal() ).toString() ), 
						Integer.parseInt( tabManut.getValor( tabManut.getLinhaSel(), EColTabManut.NPARCITREC.ordinal() ).toString()) );
			} catch ( NumberFormatException e ) {
				e.printStackTrace();
			} catch ( SQLException e ) {
				Funcoes.mensagemErro( this, "Erro ao atualizar cor no grid!!!" );
			}
			carregaGridManut( true );				
		}
	}

	public void beforeCarrega( CarregaEvent cevt ) { }

	public void afterCarrega( CarregaEvent cevt ) {

		if ( cevt.getListaCampos() == lcRecBaixa ) {

			tabBaixa.limpa();
			carregaGridBaixa();
			lcBancoBaixa.carregaDados();
		}
		else if ( cevt.getListaCampos() == lcRecManut ) {

			bBuscaAtual = false;
			carregaGridManut( bBuscaAtual );
		}
		else if ( cevt.getListaCampos() == lcCli ) {

			carregaConsulta();
		}
	}

	public void stateChanged( ChangeEvent cevt ) {

		if ( tpn.getSelectedIndex() == 0 ) {

			carregaConsulta();
		}
		if ( tpn.getSelectedIndex() == 1 ) {

			lcRecBaixa.carregaDados();
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao(cn);
		lcCli.setConexao(cn);
		lcCliBaixa.setConexao(cn);
		lcCliFiltro.setConexao(cn);
		lcCliManut.setConexao(cn);
		lcVendaBaixa.setConexao(cn);
		lcBancoBaixa.setConexao(cn);
		lcRecBaixa.setConexao(cn);
		lcRecManut.setConexao(cn);

		daomovimento = new DAOMovimento(cn);
		try {
			prefere = daomovimento.getPrefere();
		} catch (SQLException e) {
			Funcoes.mensagemErro(this, "Erro ao buscar informações preferênciais!!!", false, con, e);
		}

		iAnoCC = (Integer) prefere.get("anocc");

		btImpBol.setEnabled( getUsaBol() );

		montaMenuCores();



	}

	public void mouseClicked( MouseEvent e ) {

		if ( e.getSource() == tabManut && e.getClickCount() == 2 ) {
			visualizaRec();
		}
		else if ( e.getSource() == tabManut && e.getClickCount() == 1 ) {
			Object banco = tabManut.getValor( tabManut.getSelectedRow(), EColTabManut.CODBANCO.ordinal() );
			if ( Banco.BANCO_DO_BRASIL.equals( banco ) ) {
				btImpBol.setIcon( Icone.novo( "btBB.gif" ) );
			}
			else if ( Banco.CAIXA_ECONOMICA.equals( banco ) ) {
				btImpBol.setIcon( Icone.novo( "btCEF.gif" ) );
			}
			else if ( Banco.BRADESCO.equals( banco ) ) {
				btImpBol.setIcon( Icone.novo( "btBD.png" ) );
			}
			else {
				btImpBol.setIcon( Icone.novo( "btCodBar.png" ) );
			}
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

	public void keyPressed( KeyEvent arg0 ) { }

	public void keyReleased( KeyEvent arg0 ) { }

	public void keyTyped( KeyEvent kevt ) {

		if(kevt.getSource() == txtDocManut ) {

			Integer docrec = txtDocManut.getVlrInteger();

			if(docrec !=null && docrec >0) {

				if ( (kevt.getKeyChar() == KeyEvent.VK_ENTER ) || ( kevt.getKeyChar() == KeyEvent.VK_TAB )) {
					try {
						Integer codrec = daomovimento.pesquisaDocRec( docrec );

						if(codrec!=null && codrec>0) {
							txtCodRecManut.setVlrInteger( codrec );
							lcRecManut.carregaDados();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}

		}
		else if(kevt.getSource() == txtPedidoManut ) {

			Integer codvenda = txtPedidoManut.getVlrInteger();

			if(codvenda !=null && codvenda >0) {

				if ( (kevt.getKeyChar() == KeyEvent.VK_ENTER ) || ( kevt.getKeyChar() == KeyEvent.VK_TAB )) {

					Integer codrec = daomovimento.pesquisaPedidoRec( codvenda );

					if(codrec!=null && codrec>0) {
						txtCodRecManut.setVlrInteger( codrec );
						lcRecManut.carregaDados();
					}

				}
			}

		}	
		else if(kevt.getSource() == txtSeqNossoNumero ) {

			Integer seqnossonumero = txtSeqNossoNumero.getVlrInteger();

			if(seqnossonumero !=null && seqnossonumero >0) {

				if ( (kevt.getKeyChar() == KeyEvent.VK_ENTER ) || ( kevt.getKeyChar() == KeyEvent.VK_TAB )) {

					carregaGridManut( false );

				}
			}

		}
	}
	private String getString(String str) {
		String result;
		if (str==null) {
			result = "";
		} else {
			result = str.trim();
		}
		return result;
	}
}
