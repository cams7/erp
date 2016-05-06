/**
 * @version 10/06/2003 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.pdv <BR>
 *         Classe:
 * @(#)FVenda.java <BR>
 * 
 *                 Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                 modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                 na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                 Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                 sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                 Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                 Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                 de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                 Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.pdv;

import java.awt.AWTKeyStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import org.freedom.infra.model.jdbc.DbConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.bmps.Icone;
import org.freedom.ecf.app.ControllerECF;
import org.freedom.ecf.driver.StatusStandard;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.functions.Logger;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.StatusBar;
import org.freedom.library.swing.dialog.DlgCalc;
import org.freedom.library.swing.dialog.FDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.AplicativoPDV;
import org.freedom.library.swing.frame.FPassword;
import org.freedom.modulos.std.DLCodProd;
import org.freedom.modulos.std.view.dialog.utility.DLBuscaOrc;
import org.freedom.plugin.AbstractControleVendaPDV;

public class FVenda extends FDialogo implements KeyListener, CarregaListener, PostListener, FocusListener {

	private static final long serialVersionUID = 1L;

	private static final int CASAS_DEC = Aplicativo.casasDec;

	private static final int CASAS_DEC_FIN = Aplicativo.casasDecFin;

	private static final int CASAS_DEC_PRE = Aplicativo.casasDecPre;
	
	private final StatusBar sbVenda = new StatusBar( new BorderLayout() );

	private final JPanelPad pnStatusBar = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad pnClienteGeral = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad pnCliente = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad pnTabela = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad pnNorte = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad pnEntrada = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad pinBarra = new JPanelPad( 798, 45 );

	private final JPanelPad pinCab = new JPanelPad( 798, 45 );

	private final JPanelPad pinProduto = new JPanelPad( 798, 130 );

	private final JPanelPad pinEntrada = new JPanelPad( 190, 180 );

	private final JPanelPad pnRodTb = new JPanelPad( new BorderLayout() );

	private final JPanelPad pnTots = new JPanelPad( 440, 45 );

	private final JTablePad tbItem = new JTablePad();

	private final JScrollPane spTb = new JScrollPane( tbItem );

	private final JButtonPad btF3 = new JButtonPad( Icone.novo( "btPdvCancelaItem.gif" ) );

	private final JButtonPad btCtrlF3 = new JButtonPad( Icone.novo( "btPdvCtrlCancelaItem.gif" ) );

	private final JButtonPad btF4 = new JButtonPad( Icone.novo( "btPdvFechaVenda.gif" ) );

	private final JButtonPad btF5 = new JButtonPad( Icone.novo( "btPdvLeituraX.gif" ) );

	private final JButtonPad btF6 = new JButtonPad( Icone.novo( "btPdvGaveta.gif" ) );

	private final JButtonPad btF7 = new JButtonPad( Icone.novo( "btPdvCalc.gif" ) );

	private final JButtonPad btF8 = new JButtonPad( Icone.novo( "btPdvCopiaItem.gif" ) );

	private final JButtonPad btF9 = new JButtonPad( Icone.novo( "btPdvSelCliente.gif" ) );

	private final JButtonPad btF10 = new JButtonPad( Icone.novo( "btPdvFechaCaixa.gif" ) );

	private final JButtonPad btF11 = new JButtonPad( Icone.novo( "btOrcVendaPdv.gif" ) );

	private final JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtCodPlanoPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtCodVenda = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtCodCaixa = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtTipoVenda = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private final JTextFieldPad txtCodTipoMov = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtDtEmitVenda = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtDtSaidaVenda = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtNomeVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCodClComis = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtDescClComis = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCodProd1 = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 13, 0 );

	private final JTextFieldPad txtDescProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private final JTextFieldPad txtQtdade = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, CASAS_DEC );

	private final JTextFieldPad txtPreco = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, CASAS_DEC_PRE );

	private final JTextFieldPad txtBaseCalc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 12, CASAS_DEC_PRE );

	private final JTextFieldPad txtAliqIcms = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 7, 2 );

	private final JTextFieldPad txtTotalItem = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 12, CASAS_DEC_PRE );

	private final JTextFieldPad txtValorIcms = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 12, CASAS_DEC_PRE );

	private final JTextFieldPad txtBaseCalc1 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 12, CASAS_DEC_PRE );

	private final JTextFieldPad txtValorIcms1 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 12, CASAS_DEC_PRE );

	private final JTextFieldPad txtTotalCupom = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 12, CASAS_DEC_PRE );

	private final JTextFieldPad txtNumeroCupom = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtSerieCupom = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private final JTextFieldPad txtQtdadeItem = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 12, CASAS_DEC );

	private final JTextFieldPad txtValorTotalItem = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 12, CASAS_DEC_PRE );

	private final JTextFieldPad txtValorTotalCupom = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 12, CASAS_DEC_PRE );

	private final JTextFieldPad txtTelaAdicPDV = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private final JTextFieldPad txtCodFisc = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private final JTextFieldPad txtTipoFisc = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private final JTextFieldPad txtPercDescItOrc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, CASAS_DEC_PRE );

	private final JTextFieldPad txtVlrDescItOrc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, CASAS_DEC_PRE );

	private final JTextFieldPad txtCodLote = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private final JTextFieldPad txtCLoteProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private final JTextFieldPad txtCodConv = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldFK txtNomeConv = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private final JTextFieldFK txtDescPlanoPag = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private final JTextFieldFK txtDescLote = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldFK txtSldLiqProd = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, CASAS_DEC );

	private final JLabelPad lValorTotalItem = new JLabelPad( "Valor total do item" );

	private final JLabelPad lValorTotalCupom = new JLabelPad( "Valor total do cupom" );

	private final JLabelPad lbAvisoImp = new JLabelPad();

	private final ListaCampos lcVenda = new ListaCampos( this );

	private final ListaCampos lcTipoMov = new ListaCampos( this, "TM" );

	private final ListaCampos lcSerie = new ListaCampos( this, "SE" );

	private final ListaCampos lcCliente = new ListaCampos( this, "CL" );

	private final ListaCampos lcVendedor = new ListaCampos( this, "VD" );

	private final ListaCampos lcClComis = new ListaCampos( this, "CM" );

	private final ListaCampos lcPlanoPag = new ListaCampos( this, "PG" );

	private final ListaCampos lcProduto = new ListaCampos( this, "PD" );

	private final ListaCampos lcClFiscal = new ListaCampos( this, "FC" );

	private final ListaCampos lcLote = new ListaCampos( this, "LE" );

	private final ListaCampos lcConv = new ListaCampos( this, "CV" );

	private Vector<Object> vCacheItem = new Vector<Object>();

	private Font fntTotalItem = null;

	private Font fntTotalCupom = null;

	private Object[] pref = new Object[ 3 ];

	private boolean obrigaOrcamento = false;

	private boolean trocouCli = false;

	private boolean colocouFrete = false;

	private boolean carregaPesoFrete = false;

	private boolean CTRL = false;

	private float pesoBrutFrete = 0;

	private float pesoLiqFrete = 0;

	private float vlrFrete = 0;

	private int iCodItVenda = 0;

	private int CODTIPOMOV = 0;

	private int CODORCMASTER = 0;

	private int CODCLI = 0;

	private int CODVEND = 0;

	private int PLANOPAG = 0;

	private String nomePluginVenda;

	private static AbstractControleVendaPDV pluginVenda;

	private final ControllerECF ecf;

	public FVenda() {

		setTitulo( "Venda", this.getClass().getName() );
		setAtribos( 798, 580 );
		setToFrameLayout();
		setResizable( false );
		setLocation( 0, 0 );
		Dimension size = Aplicativo.telaPrincipal.getSize();
		size.height -= 9;
		size.width -= 9;
		setSize( size );

		montaListaCampos();
		montaTela();

		txtPreco.setAtivo( false );
		txtCodVenda.setAtivo( false );
		txtSerieCupom.setAtivo( false );
		txtNumeroCupom.setAtivo( false );

		txtCodProd1.setSoLeitura( true );
		txtDescProd.setSoLeitura( true );
		txtBaseCalc.setSoLeitura( true );
		txtAliqIcms.setSoLeitura( true );
		txtTotalItem.setSoLeitura( true );
		txtValorIcms.setSoLeitura( true );

		txtBaseCalc1.setSoLeitura( true );
		txtValorIcms1.setSoLeitura( true );
		txtTotalCupom.setSoLeitura( true );
		txtQtdadeItem.setSoLeitura( true );
		txtValorTotalItem.setSoLeitura( true );
		txtValorTotalCupom.setSoLeitura( true );

		btF3.addActionListener( this );
		btCtrlF3.addActionListener( this );
		btF4.addActionListener( this );
		btF5.addActionListener( this );
		btF6.addActionListener( this );
		btF7.addActionListener( this );
		btF8.addActionListener( this );
		btF9.addActionListener( this );
		btF10.addActionListener( this );
		btF11.addActionListener( this );

		btF3.addKeyListener( this );
		btCtrlF3.addKeyListener( this );
		btF4.addKeyListener( this );
		btF5.addKeyListener( this );
		btF6.addKeyListener( this );
		btF7.addKeyListener( this );
		btF8.addKeyListener( this );
		btF9.addKeyListener( this );
		btF10.addKeyListener( this );
		btF11.addKeyListener( this );

		addKeyListener( this );

		txtCodProd.addKeyListener( this );
		txtQtdade.addKeyListener( this );
		txtPercDescItOrc.addKeyListener( this );
		txtVlrDescItOrc.addKeyListener( this );
		txtCodLote.addKeyListener( this );

		txtQtdade.addFocusListener( this );
		txtPercDescItOrc.addFocusListener( this );
		txtVlrDescItOrc.addFocusListener( this );

		lcVenda.addPostListener( this );

		lcProduto.addCarregaListener( this );

		ecf = new ControllerECF( AplicativoPDV.getEcfdriver(), AplicativoPDV.getPortaECF(), AplicativoPDV.bModoDemo, AplicativoPDV.getEcflayout() );

		setPrimeiroFoco( txtCodProd );
	}

	private void montaListaCampos() {

		lcCliente.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCliente.add( new GuardaCampo( txtRazCli, "RazCli", "Razão Social do cliente", ListaCampos.DB_SI, false ) );
		lcCliente.montaSql( false, "CLIENTE", "VD" );
		lcCliente.setReadOnly( true );
		txtCodCli.setTabelaExterna( lcCliente, null );
		txtCodCli.setFK( true );
		txtCodCli.setNomeCampo( "CodCli" );

		lcVendedor.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.comis.", ListaCampos.DB_PK, false ) );
		lcVendedor.add( new GuardaCampo( txtNomeVend, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, false ) );
		lcVendedor.add( new GuardaCampo( txtCodClComis, "CodClComis", "Cód.cl.comis.", ListaCampos.DB_FK, false ) );
		lcVendedor.montaSql( false, "VENDEDOR", "VD" );
		lcVendedor.setReadOnly( true );
		txtCodVend.setTabelaExterna( lcVendedor, null );
		txtCodVend.setFK( true );
		txtCodVend.setNomeCampo( "CodVend" );

		lcClComis.add( new GuardaCampo( txtCodClComis, "CodClComis", "Cód.cl.comis.", ListaCampos.DB_PK, false ) );
		lcClComis.add( new GuardaCampo( txtDescClComis, "DescClComis", "Descrição da classificação da comissão", ListaCampos.DB_SI, false ) );
		lcClComis.montaSql( false, "CLCOMIS", "VD" );
		lcClComis.setReadOnly( true );
		txtCodClComis.setTabelaExterna( lcClComis, null );
		txtCodClComis.setFK( true );
		txtCodClComis.setNomeCampo( "CodClComis" );

		lcClFiscal.add( new GuardaCampo( txtCodFisc, "CodFisc", "Cód.fisc.", ListaCampos.DB_PK, false ) );
		lcClFiscal.add( new GuardaCampo( txtTipoFisc, "TipoFisc", "Cód.tp.fisc.", ListaCampos.DB_SI, false ) );
		lcClFiscal.montaSql( false, "CLFISCAL", "LF" );
		lcClFiscal.setReadOnly( true );
		txtCodFisc.setTabelaExterna( lcClFiscal, null );

		lcPlanoPag.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_PK, false ) );
		lcPlanoPag.add( new GuardaCampo( txtDescPlanoPag, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPag.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag.setReadOnly( true );
		txtCodPlanoPag.setTabelaExterna( lcPlanoPag, null );
		txtCodPlanoPag.setFK( true );
		txtCodPlanoPag.setNomeCampo( "CodPlanoPag" );

		lcSerie.add( new GuardaCampo( txtSerieCupom, "Serie", "Serie", ListaCampos.DB_PK, false ) );
		lcSerie.add( new GuardaCampo( txtNumeroCupom, "DocSerie", "Doc", ListaCampos.DB_SI, true ) );
		lcSerie.montaSql( false, "SERIE", "LF" );
		lcSerie.setReadOnly( true );
		txtSerieCupom.setTabelaExterna( lcSerie, null );
		txtSerieCupom.setFK( true );
		txtSerieCupom.setNomeCampo( "Serie" );

		lcTipoMov.add( new GuardaCampo( txtCodTipoMov, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, true ) );
		lcTipoMov.add( new GuardaCampo( txtSerieCupom, "Serie", "Serie", ListaCampos.DB_FK, false ) );
		lcTipoMov.montaSql( false, "TIPOMOV", "EQ" );
		lcTipoMov.setReadOnly( true );
		txtCodTipoMov.setTabelaExterna( lcTipoMov, null );
		txtCodTipoMov.setFK( true );
		txtCodTipoMov.setNomeCampo( "CodTipoMov" );

		lcConv.add( new GuardaCampo( txtCodConv, "CodConv", "Cód.conv.", ListaCampos.DB_PK, true ) );
		lcConv.add( new GuardaCampo( txtNomeConv, "NomeConv", "Nome do conveniado", ListaCampos.DB_SI, false ) );
		lcConv.montaSql( false, "CONVENIADO", "AT" );
		lcConv.setReadOnly( true );
		txtCodConv.setTabelaExterna( lcConv, null );
		txtCodConv.setFK( true );
		txtCodConv.setNomeCampo( "CodConv" );

		lcVenda.add( new GuardaCampo( txtCodVenda, "CodVenda", "Nº pedido", ListaCampos.DB_PK, true ) );
		lcVenda.add( new GuardaCampo( txtTipoVenda, "TipoVenda", "Tipo venda.", ListaCampos.DB_PK, true ) );
		lcVenda.add( new GuardaCampo( txtCodTipoMov, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_FK, true ) );
		lcVenda.add( new GuardaCampo( txtSerieCupom, "Serie", "Serie", ListaCampos.DB_FK, false ) );
		lcVenda.add( new GuardaCampo( txtNumeroCupom, "DocVenda", "Doc", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtDtSaidaVenda, "DtSaidaVenda", "Saída", ListaCampos.DB_SI, true ) );
		lcVenda.add( new GuardaCampo( txtDtEmitVenda, "DtEmitVenda", "Emissão", ListaCampos.DB_SI, true ) );
		lcVenda.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_FK, true ) );
		lcVenda.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_FK, true ) );
		lcVenda.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.comiss.", ListaCampos.DB_FK, false ) );
		lcVenda.add( new GuardaCampo( txtCodClComis, "CodClComis", "Cód.cl.comiss.", ListaCampos.DB_FK, false ) );
		lcVenda.add( new GuardaCampo( txtCodCaixa, "CodCaixa", "Cód. caixa", ListaCampos.DB_SI, false ) );
		lcVenda.montaSql( false, "VENDA", "VD" );
		txtCodVenda.setListaCampos( lcVenda );
		txtCodVenda.setPK( true );
		txtCodVenda.setNomeCampo( "CodVenda" );

		lcLote.add( new GuardaCampo( txtCodLote, "CodLote", "Lote", ListaCampos.DB_PK, txtDescLote, false ) );
		lcLote.add( new GuardaCampo( txtDescLote, "VenctoLote", "Dt.vencto.", ListaCampos.DB_SI, false ) );
		lcLote.add( new GuardaCampo( txtSldLiqProd, "SldLiqLote", "Saldo", ListaCampos.DB_SI, false ) );
		lcLote.setDinWhereAdic( "CODPROD=#N ", txtCodProd );
		lcLote.montaSql( false, "LOTE", "EQ" );
		lcLote.setReadOnly( true );
		txtCodLote.setTabelaExterna( lcLote, null );
		txtCodLote.setFK( true );
		txtCodLote.setNomeCampo( "CodLote" );

		lcProduto.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_PK, true ) );
		lcProduto.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProduto.add( new GuardaCampo( txtCodProd1, "CodProd", "Cód.prod.", ListaCampos.DB_SI, false ) );
		lcProduto.add( new GuardaCampo( txtCodFisc, "CodFisc", "Cód.fisc.", ListaCampos.DB_FK, false ) );
		lcProduto.add( new GuardaCampo( txtCLoteProd, "CLoteProd", "C/Lote", ListaCampos.DB_SI, false ) );
		lcProduto.add( new GuardaCampo( txtTelaAdicPDV, "UsaTelaAdicPDV", "PDV", ListaCampos.DB_SI, false ) );
		lcProduto.setWhereAdic( "CVPROD IN ('V','A')" );
		lcProduto.montaSql( false, "PRODUTO", "EQ" );
		lcProduto.setReadOnly( true );
		txtCodProd.setTabelaExterna( lcProduto, null );
		txtCodProd.setFK( true );
		txtCodProd.setNomeCampo( "CodProd" );
	}

	private void montaTela() {

		fntTotalItem = new Font( lValorTotalItem.getFont().getFontName(), lValorTotalItem.getFont().getStyle(), 26 );
		fntTotalCupom = new Font( lValorTotalCupom.getFont().getFontName(), lValorTotalCupom.getFont().getStyle(), 26 );

		lbAvisoImp.setFont( new Font( lValorTotalItem.getFont().getFontName(), lValorTotalItem.getFont().getStyle(), 12 ) );
		lbAvisoImp.setForeground( Color.RED );
		lbAvisoImp.setHorizontalAlignment( SwingConstants.CENTER );

		tbItem.adicColuna( "Item" );
		tbItem.adicColuna( "Cód.prod." );
		tbItem.adicColuna( "Descrição do produto" );
		tbItem.adicColuna( "Qtd." );
		tbItem.adicColuna( "Preço" );
		tbItem.adicColuna( "% Icms" );
		tbItem.adicColuna( "Base cálc." );
		tbItem.adicColuna( "Valor Icms" );
		tbItem.adicColuna( "C" );
		tbItem.adicColuna( "Conv" );

		tbItem.setTamColuna( 40, 0 );
		tbItem.setTamColuna( 100, 1 );
		tbItem.setTamColuna( 250, 2 );
		tbItem.setTamColuna( 70, 3 );
		tbItem.setTamColuna( 90, 4 );
		tbItem.setTamColuna( 90, 5 );
		tbItem.setTamColuna( 90, 6 );
		tbItem.setTamColuna( 90, 7 );
		tbItem.setTamColuna( 20, 8 );
		tbItem.setTamColuna( 50, 9 );

		c.add( pnClienteGeral, BorderLayout.CENTER );

		btF3.setToolTipText( "Cancela último item." );
		btCtrlF3.setToolTipText( "Cancela item por posição." );
		btF4.setToolTipText( "Fecha venda." );
		btF5.setToolTipText( "Leitura X." );
		btF6.setToolTipText( "Abre gaveta." );
		btF7.setToolTipText( "Calculadora." );
		btF8.setToolTipText( "Repete item." );
		btF9.setToolTipText( "Seleciona cliente." );
		btF10.setToolTipText( "Fecha caixa." );
		btF11.setToolTipText( "Busca orçamento." );

		pinBarra.adic( btF3, 5, 3, 60, 35 );
		pinBarra.adic( btCtrlF3, 70, 3, 60, 35 );
		pinBarra.adic( btF4, 135, 3, 60, 35 );
		pinBarra.adic( btF5, 200, 3, 60, 35 );
		pinBarra.adic( btF6, 265, 3, 60, 35 );
		pinBarra.adic( btF7, 330, 3, 60, 35 );
		pinBarra.adic( btF8, 395, 3, 60, 35 );
		pinBarra.adic( btF9, 460, 3, 60, 35 );
		pinBarra.adic( btF10, 525, 3, 60, 35 );
		pinBarra.adic( btF11, 590, 3, 60, 35 );

		pinCab.adic( new JLabelPad( "Cód.cli." ), 5, 3, 70, 15 );
		pinCab.adic( new JLabelPad( "Razão social do cliente" ), 75, 3, 200, 15 );
		pinCab.adic( txtCodCli, 5, 20, 68, 20 );
		pinCab.adic( txtRazCli, 75, 20, 200, 20 );
		pinCab.adic( new JLabelPad( "Cód.p.pag." ), 283, 3, 70, 15 );
		pinCab.adic( new JLabelPad( "Descrição do plano de pagamento" ), 353, 3, 200, 15 );
		pinCab.adic( txtCodPlanoPag, 283, 20, 68, 20 );
		pinCab.adic( txtDescPlanoPag, 353, 20, 200, 20 );
		pinCab.adic( new JLabelPad( "Nº seq.venda" ), 561, 3, 105, 15 );
		pinCab.adic( txtCodVenda, 561, 20, 105, 20 );

		txtDescProd.setFont( fntTotalItem );
		txtCodProd1.setFont( fntTotalItem );
		txtQtdadeItem.setFont( fntTotalItem );
		txtValorTotalItem.setFont( fntTotalItem );
		txtValorTotalCupom.setFont( fntTotalCupom );

		txtDescProd.setForeground( Color.BLUE );
		txtCodProd1.setForeground( Color.BLUE );
		txtQtdadeItem.setForeground( Color.BLUE );
		txtValorTotalItem.setForeground( Color.BLUE );
		txtValorTotalCupom.setForeground( Color.RED );

		pinProduto.adic( new JLabelPad( "Descrição do produto" ), 5, 3, 450, 15 );
		pinProduto.adic( new JLabelPad( "Cód.prod." ), 460, 3, 206, 15 );
		pinProduto.adic( txtDescProd, 5, 20, 450, 40 );
		pinProduto.adic( txtCodProd1, 460, 20, 206, 40 );
		pinProduto.adic( new JLabelPad( "Quantidade do item" ), 5, 65, 215, 15 );
		pinProduto.adic( lValorTotalItem, 225, 65, 215, 15 );
		pinProduto.adic( lValorTotalCupom, 445, 65, 220, 15 );
		pinProduto.adic( txtQtdadeItem, 5, 82, 215, 40 );
		pinProduto.adic( txtValorTotalItem, 225, 82, 215, 40 );
		pinProduto.adic( txtValorTotalCupom, 445, 82, 220, 40 );

		pinEntrada.adic( new JLabelPad( "Cód.prod." ), 5, 0, 70, 20 );
		pinEntrada.adic( txtCodProd, 5, 20, 175, 20 );
		pinEntrada.adic( new JLabelPad( "Quantidade" ), 5, 40, 85, 20 );
		pinEntrada.adic( txtQtdade, 5, 60, 85, 20 );
		pinEntrada.adic( new JLabelPad( "Preço" ), 95, 40, 85, 20 );
		pinEntrada.adic( txtPreco, 95, 60, 85, 20 );
		pinEntrada.adic( new JLabelPad( "% Desc." ), 5, 80, 50, 20 );
		pinEntrada.adic( txtPercDescItOrc, 5, 100, 50, 20 );
		pinEntrada.adic( new JLabelPad( "Valor desc." ), 60, 80, 120, 20 );
		pinEntrada.adic( txtVlrDescItOrc, 60, 100, 120, 20 );
		pinEntrada.adic( new JLabelPad( "Total item" ), 5, 120, 85, 20 );
		pinEntrada.adic( txtTotalItem, 5, 140, 85, 20 );
		pinEntrada.adic( new JLabelPad( "Base cálc." ), 95, 120, 85, 20 );
		pinEntrada.adic( txtBaseCalc, 95, 140, 85, 20 );
		pinEntrada.adic( new JLabelPad( "Còd.lote" ), 5, 160, 70, 20 );
		pinEntrada.adic( txtCodLote, 5, 180, 175, 20 );
		pinEntrada.adic( new JLabelPad( "% Icms" ), 5, 200, 50, 20 );
		pinEntrada.adic( txtAliqIcms, 5, 220, 50, 20 );
		pinEntrada.adic( new JLabelPad( "Valor Icms" ), 60, 200, 120, 20 );
		pinEntrada.adic( txtValorIcms, 60, 220, 120, 20 );

		pnTots.adic( new JLabelPad( "Base cálculo" ), 5, 3, 90, 15 );
		pnTots.adic( new JLabelPad( "Total Icms" ), 100, 3, 90, 15 );
		pnTots.adic( new JLabelPad( "Total cupom" ), 195, 3, 90, 15 );
		pnTots.adic( new JLabelPad( "Número cupom" ), 290, 3, 90, 15 );
		pnTots.adic( new JLabelPad( "Série" ), 385, 3, 50, 15 );

		pnTots.adic( txtBaseCalc1, 5, 20, 90, 20 );
		pnTots.adic( txtValorIcms1, 100, 20, 90, 20 );
		pnTots.adic( txtTotalCupom, 195, 20, 90, 20 );
		pnTots.adic( txtNumeroCupom, 290, 20, 90, 20 );
		pnTots.adic( txtSerieCupom, 385, 20, 50, 20 );

		pnTots.tiraBorda();
		pnRodTb.add( pnTots, BorderLayout.WEST );
		pnRodTb.add( lbAvisoImp, BorderLayout.CENTER );
		pnRodTb.setBorder( BorderFactory.createEtchedBorder() );

		pnNorte.add( pinBarra, BorderLayout.NORTH );
		pnNorte.add( pinCab, BorderLayout.SOUTH );
		pnTabela.add( pnRodTb, BorderLayout.SOUTH );
		pnTabela.add( spTb, BorderLayout.CENTER );
		pnEntrada.add( pinEntrada, BorderLayout.CENTER );
		pnCliente.add( pinProduto, BorderLayout.NORTH );
		pnCliente.add( pnEntrada, BorderLayout.EAST );
		pnCliente.add( pnTabela, BorderLayout.CENTER );
		pnClienteGeral.add( pnNorte, BorderLayout.NORTH );
		pnClienteGeral.add( pnCliente, BorderLayout.CENTER );

		Set<AWTKeyStroke> empyt_set = new HashSet<AWTKeyStroke>();
		txtCodProd.setFocusTraversalKeys( KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, empyt_set );
	}

	private void abreAdicOrc() {

		if ( !Aplicativo.telaPrincipal.temTela( "Busca orçamento" ) ) {
			DLBuscaOrc tela = new DLBuscaOrc( this, "E", "Venda" );
			Aplicativo.telaPrincipal.criatela( "Orcamento", tela, con );
		}
	}

	private void abreGaveta() {

		if ( mostraTelaPass() ) {
			if ( !ecf.abrirGaveta() ) {
				Funcoes.mensagemErro( this, ecf.getMessageLog() );
			}
		}
	}

	public synchronized void adicItemOrc( int[] iCodItOrc ) {

		try {

			StringBuilder sql = new StringBuilder();

			sql.append( "SELECT CODPROD, QTDITORC, PRECOITORC, PERCDESCITORC, VLRDESCITORC, CODLOTE " );
			sql.append( "FROM VDITORCAMENTO WHERE CODEMP=? AND CODFILIAL=? AND CODORC=? AND CODITORC=?" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, AplicativoPDV.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "ORCAMENTO" ) );
			ps.setInt( 3, iCodItOrc[ 0 ] );
			ps.setInt( 4, iCodItOrc[ 1 ] );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {

				txtCodProd.setVlrString( rs.getString( "CODPROD" ) );
				lcProduto.carregaDados();

				actionCodProd();

				txtPreco.setVlrBigDecimal( rs.getBigDecimal( "PRECOITORC" ) );

				txtPercDescItOrc.setVlrBigDecimal( rs.getBigDecimal( "PERCDESCITORC" ) != null ? rs.getBigDecimal( "PERCDESCITORC" ) : new BigDecimal( "0" ) );
				txtVlrDescItOrc.setVlrBigDecimal( rs.getBigDecimal( "VLRDESCITORC" ) != null ? rs.getBigDecimal( "VLRDESCITORC" ) : new BigDecimal( "0" ) );
				txtCodLote.setVlrString( rs.getString( "CODLOTE" ) );
				txtQtdade.setVlrBigDecimal( rs.getBigDecimal( "QTDITORC" ) );

				actionQtdade();

				lcLote.carregaDados();

				if ( actionPostVendaForStatus() ) {

					sql.delete( 0, sql.length() );
					sql.append( "EXECUTE PROCEDURE VDUPVENDAORCSP(?,?,?,?,?,?,?,?)" );

					PreparedStatement ps2 = con.prepareStatement( sql.toString() );
					ps2.setInt( 1, AplicativoPDV.iCodEmp );
					ps2.setInt( 2, ListaCampos.getMasterFilial( "ORCAMENTO" ) );
					ps2.setInt( 3, iCodItOrc[ 0 ] );
					ps2.setInt( 4, iCodItOrc[ 1 ] );
					ps2.setInt( 5, ListaCampos.getMasterFilial( "VDVENDA" ) );
					ps2.setInt( 6, txtCodVenda.getVlrInteger().intValue() );
					ps2.setInt( 7, iCodItVenda );
					ps2.setString( 8, txtTipoVenda.getVlrString() );
					ps2.execute();
					ps2.close();
				}
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro ao gerar venda do orçamento.\n" + e.getMessage() );
			e.printStackTrace();
		} catch ( Exception e ) {
			Funcoes.mensagemErro( this, "Erro ao gerar venda do orçamento.\n" + e.getMessage() );
			e.printStackTrace();
		}
	}

	private void addItemFrete() {

		txtCodProd.setVlrString( (String) prefs( 1 ) );
		lcProduto.carregaDados();
		txtQtdade.setVlrBigDecimal( new BigDecimal( 1 ) );
		vlrFrete = txtPreco.getVlrBigDecimal().floatValue();

		if ( insereItem() ) {

			iniItem();

			carregaPesoFrete = true;
			colocouFrete = true;
		}
		else {

			txtCodProd.setVlrString( "" );
			lcProduto.carregaDados();
			txtQtdade.setVlrBigDecimal( new BigDecimal( 0 ) );

			return;
		}
	}

	private void addPesoFrete( int iCodProd, BigDecimal iQtd ) {

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT PESOBRUTPROD, PESOLIQPROD FROM EQPRODUTO WHERE CODEMP=? AND CODFILIAL=? AND CODPROD=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			ps.setInt( 3, iCodProd );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {

				pesoBrutFrete += ( rs.getFloat( 1 ) * iQtd.floatValue() );
				pesoLiqFrete += ( rs.getFloat( 2 ) * iQtd.floatValue() );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro ao somar peso do produto!\n" + e.getMessage(), true, con, e );
			e.printStackTrace();
		}
	}

	private void atualizaTot() {

		try {

			StringBuilder sql = new StringBuilder();
			sql.append( "SELECT VLRLIQVENDA,VLRBASEICMSVENDA,VLRICMSVENDA " );
			sql.append( "FROM VDVENDA WHERE CODVENDA=? AND CODEMP=? AND CODFILIAL=? AND TIPOVENDA='E'" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, txtCodVenda.getVlrInteger().intValue() );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, lcVenda.getCodFilial() );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {

				txtValorTotalCupom.setVlrBigDecimal( rs.getBigDecimal( "VlrLiqVenda" ) );
				txtBaseCalc1.setVlrBigDecimal( rs.getBigDecimal( "VlrBaseICMSVenda" ) );
				txtValorIcms1.setVlrBigDecimal( rs.getBigDecimal( "VlrICMSVenda" ) );
				txtTotalCupom.setVlrBigDecimal( rs.getBigDecimal( "VlrLiqVenda" ) );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( null, "Erro ao atualizar o saldo!\n" + "Talvez esta venda ainda não esteja salva!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	private void cancItem() {

		if ( tbItem.getNumLinhas() < 1 ) {
			Funcoes.mensagemErro( this, "Não existe nenhum item para ser cancelado!" );
			return;
		}

		int iItem = ( (Integer) tbItem.getValor( tbItem.getNumLinhas() - 1, 0 ) ).intValue();

		if ( Funcoes.mensagemConfirma( null, "Deseja realmente cancelar o item anterior?" ) == JOptionPane.YES_OPTION ) {
			if ( cancItem( iItem ) ) {
				if ( ecf.cancelaItem() ) {
					btOK.doClick();
				}
				else {
					Funcoes.mensagemErro( this, ecf.getMessageLog() );
				}
				atualizaTot();
			}
			else {
				Funcoes.mensagemErro( null, "Não foi possível cancelar o item." );
			}
		}
	}

	private boolean cancItem( int iItem ) {

		boolean action_return = false;
		int iLinha = -1;

		try {

			StringBuilder sql = new StringBuilder();
			sql.append( "UPDATE VDITVENDA SET CANCITVENDA='S' " );
			sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODVENDA=? AND TIPOVENDA='E' AND CODITVENDA=?" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDVENDA" ) );
			ps.setInt( 3, txtCodVenda.getVlrInteger().intValue() );
			ps.setInt( 4, iItem );
			ps.executeUpdate();
			ps.close();

			con.commit();

			iLinha = getLinha( iItem );

			if ( iLinha > -1 ) {

				tbItem.setValor( new BigDecimal( "0.00" ), iLinha, 3 );
				tbItem.setValor( new BigDecimal( "0.00" ), iLinha, 6 );
				tbItem.setValor( new BigDecimal( "0.00" ), iLinha, 7 );
				tbItem.setValor( "C", iLinha, 8 );

				marcaLinha( iItem );
				minPesoFrete( txtCodProd1.getVlrInteger().intValue(), txtQtdade.getVlrBigDecimal() );
				atualizaTot();
			}

			action_return = true;

		} catch ( SQLException err ) {
			err.printStackTrace();
			Logger.gravaLogTxt( "", Aplicativo.getUsuario().getIdusu(), Logger.LGEB_BD, "Erro cancelar o item " + err.getMessage() );
		}

		return action_return;
	}

	private void cancCupom() {

		if ( lcVenda.getStatus() != ListaCampos.LCS_SELECT ) {
			Funcoes.mensagemErro( this, "Não existe nenhuma venda ativa!" );
			return;
		}

		DLCancCupom canc = new DLCancCupom();

		canc.setVenda( txtCodVenda.getVlrInteger().intValue() );
		canc.setConexao( con );
		canc.setVisible( true );

		if ( canc.isCancCupom() ) {
			iniVenda();
		}
		else {

			int[] iItens = canc.getCancItem();
			int iLinha = 0;

			for ( int i = 0; i < iItens.length; i++ ) {

				iLinha = getLinha( iItens[ i ] );

				if ( iLinha > -1 ) {

					tbItem.setValor( new BigDecimal( "0.00" ), iLinha, 3 );
					tbItem.setValor( new BigDecimal( "0.00" ), iLinha, 6 );
					tbItem.setValor( new BigDecimal( "0.00" ), iLinha, 7 );
					tbItem.setValor( "C", iLinha, 8 );

					marcaLinha( iItens[ i ] );
					minPesoFrete( txtCodProd1.getVlrInteger().intValue(), txtQtdade.getVlrBigDecimal() );
					atualizaTot();
				}
			}
		}

		canc.dispose();
	}

	private void carregaPlugin() {

		nomePluginVenda = AplicativoPDV.getPluginVenda();

		if ( nomePluginVenda.length() > 0 ) {

			try {

				pluginVenda = (AbstractControleVendaPDV) Class.forName( nomePluginVenda ).newInstance();

				pluginVenda.addAttribute( "conexao", con );

				pluginVenda.addAttribute( "CodEmp", new Integer( AplicativoPDV.iCodEmp ) );
				pluginVenda.addAttribute( "CodFilial", new Integer( AplicativoPDV.iCodFilial ) );
				pluginVenda.addAttribute( "IDUsuario", AplicativoPDV.getUsuario().getIdusu() );
				pluginVenda.addAttribute( "CodCaixa", new Integer( AplicativoPDV.iCodCaixa ) );

				pluginVenda.addAttribute( "txtCodVenda", txtCodVenda );
				pluginVenda.addAttribute( "txtTipoVenda", txtTipoVenda );
				pluginVenda.addAttribute( "txtNumeroCupom", txtNumeroCupom );
				pluginVenda.addAttribute( "txtCodProd", txtCodProd );
				pluginVenda.addAttribute( "txtQtdade", txtQtdade );
				pluginVenda.addAttribute( "txtPreco", txtPreco );

				pluginVenda.addAttribute( "txtCodConv", txtCodConv );

				pluginVenda.inicializa();

			} catch ( Exception e ) {
				e.printStackTrace();
				Funcoes.mensagemErro( null, "Erro ao carregar plugin de venda", true, con, e );
			}
		}
	}

	private void fechaCaixa() {

		if ( lcVenda.getStatus() == ListaCampos.LCS_SELECT ) {
			Funcoes.mensagemInforma( this, "Ainda existe uma venda ativa!" );
			return;
		}

		DLFechaDia fecha = new DLFechaDia();
		fecha.setConexao( con );
		fecha.setVisible( true );

		if ( fecha.OK ) {
			fecha.dispose();
			this.dispose();
		}

		fecha.dispose();
	}

	public synchronized void fechaVenda() {

		if ( lcVenda.getStatus() != ListaCampos.LCS_SELECT ) {
			Funcoes.mensagemErro( this, "Não existe nenhuma venda ativa!" );
			return;
		}

		if ( txtCodCli.getVlrInteger().intValue() != ( CODCLI ) ) {
			trocouCli = true;
		}

		if ( ( (Boolean) prefs( 0 ) ).booleanValue() ) {
			if ( prefs( 1 ) != null ) {
				if ( Funcoes.mensagemConfirma( null, "Deseja adicionar o frete ao cupom?" ) == JOptionPane.YES_OPTION ) {
					addItemFrete();
				}
			}
		}

		DLFechaVenda fecha = new DLFechaVenda( paramFecha() );
		fecha.setVisible( true );

		if ( fecha.OK ) {

			if ( pluginVenda != null ) {
				pluginVenda.afterFechaVenda();
			}

			iniVenda();
		}
		else if ( colocouFrete ) {

			cancItem( ( (Integer) tbItem.getValor( tbItem.getNumLinhas() - 1, 0 ) ).intValue() );

			if ( ecf.cancelaItem() ) {
				btOK.doClick();
			}
			else {
				Funcoes.mensagemErro( this, ecf.getMessageLog() );
			}

			colocouFrete = false;
		}

		fecha.dispose();

		setFocusProd();
		trocouCli = false;
	}

	private synchronized void iniItem() {

		txtBaseCalc.setVlrString( "" );
		txtAliqIcms.setVlrString( "" );
		txtValorIcms.setVlrString( "" );
		txtQtdade.setVlrBigDecimal( new BigDecimal( 1 ) );
		txtPreco.setVlrString( "" );
		txtCodProd.setVlrString( "" );
		txtTelaAdicPDV.setVlrString( "" );
		txtPercDescItOrc.setVlrString( "" );
		txtVlrDescItOrc.setVlrString( "" );
		txtCodLote.setVlrString( "" );
		txtCodLote.setAtivo( false );
		setFocusProd();
	}

	private boolean insereItem() {

		// passa -1 pra que no metodo getItComis(int) seja buscado pelo produto e não pelo orçamento.
		return insereItem( -1 );
	}

	private boolean insereItem( int iCodItOrc ) {

		if ( txtCLoteProd.getVlrString().equals( "S" ) ) {
			if ( txtCodLote.getVlrString().length() <= 0 || testaCodLote() ) {
				Funcoes.mensagemErro( null, "Código do lote é requerido!" );
				return false;
			}
		}

		try {

			BigDecimal[] argsComis = getItComis( iCodItOrc );

			StringBuilder sql = new StringBuilder();
			sql.append( "SELECT CODITVENDA,PERCICMSITVENDA,VLRBASEICMSITVENDA,VLRICMSITVENDA,VLRLIQITVENDA, TIPOFISC " );
			sql.append( "FROM VDADICITEMPDVSP(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, txtCodVenda.getVlrInteger().intValue() );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "VDITVENDA" ) );
			ps.setString( 4, txtCodProd.getVlrString().trim() );
			ps.setInt( 5, Aplicativo.iCodEmp );
			ps.setInt( 6, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			ps.setBigDecimal( 7, txtQtdade.getVlrBigDecimal() );
			ps.setBigDecimal( 8, txtPreco.getVlrBigDecimal() );
			ps.setBigDecimal( 9, txtVlrDescItOrc.getVlrBigDecimal() );
			ps.setBigDecimal( 10, txtPercDescItOrc.getVlrBigDecimal() );
			if ( argsComis[ 1 ] != null ) {
				ps.setBigDecimal( 11, argsComis[ 1 ] );
				ps.setBigDecimal( 12, argsComis[ 0 ] );
			}
			else {
				ps.setNull( 11, Types.NUMERIC );
				ps.setNull( 12, Types.NUMERIC );
			}
			if ( txtCLoteProd.getVlrString().equals( "S" ) ) {
				ps.setString( 13, txtCodLote.getVlrString().trim() );
				ps.setInt( 14, Aplicativo.iCodEmp );
				ps.setInt( 15, ListaCampos.getMasterFilial( "EQLOTE" ) );
			}
			else {
				ps.setNull( 13, Types.INTEGER );
				ps.setNull( 14, Types.SMALLINT );
				ps.setNull( 15, Types.CHAR );
			}
			if ( txtCodConv.getVlrInteger().intValue() > 0 ) {
				ps.setInt( 16, ListaCampos.getMasterFilial( "ATCONVENIADO" ) );
				ps.setInt( 17, txtCodConv.getVlrInteger().intValue() );
			}
			else {
				ps.setNull( 16, Types.SMALLINT );
				ps.setNull( 17, Types.INTEGER );
			}

			ResultSet rs = ps.executeQuery();

			boolean inseriu = false;

			if ( rs.next() ) {

				iCodItVenda = rs.getInt( "CodItVenda" );
				txtAliqIcms.setVlrBigDecimal( rs.getBigDecimal( "PercICMSItVenda" ) );
				txtBaseCalc.setVlrBigDecimal( rs.getBigDecimal( "VlrBaseICMSItVenda" ) );
				txtValorIcms.setVlrBigDecimal( rs.getBigDecimal( "VlrICMSItVenda" ) );
				txtValorTotalItem.setVlrBigDecimal( rs.getBigDecimal( "VlrLiqItVenda" ) == null ? new BigDecimal( 0 ) : rs.getBigDecimal( "VlrLiqItVenda" ) );
				txtQtdadeItem.setVlrBigDecimal( txtQtdade.getVlrBigDecimal() );
				txtTipoFisc.setVlrString( rs.getString("TipoFisc") );
				
				tbItem.adicLinha( new Object[] { new Integer( iCodItVenda ), txtCodProd.getVlrString().trim(), txtDescProd.getVlrString(), txtQtdade.getVlrBigDecimal(), txtPreco.getVlrBigDecimal(), txtAliqIcms.getVlrBigDecimal(), txtBaseCalc.getVlrBigDecimal(), txtValorIcms.getVlrBigDecimal(), "",
						txtCodConv.getVlrInteger().intValue() > 0 ? txtCodConv.getVlrString() : "" } );

				inseriu = true;
			}

			rs.close();
			ps.close();

			con.commit();

			if ( inseriu ) {
				if ( ecf.vendaItem( txtCodProd.getVlrString().trim(), txtDescProd.getVlrString().trim(), txtAliqIcms.getVlrBigDecimal(), txtTipoFisc.getVlrString().trim(), txtQtdade.getVlrBigDecimal(), txtPreco.getVlrBigDecimal(), txtVlrDescItOrc.getVlrBigDecimal() ) ) {

					addPesoFrete( Integer.parseInt( txtCodProd.getVlrString().trim() ), txtQtdade.getVlrBigDecimal() );
					atualizaTot();

					vCacheItem.clear();
					vCacheItem.add( txtCodProd.getVlrString() );
					vCacheItem.add( txtQtdade.getVlrBigDecimal() );
				}
				else {

					Funcoes.mensagemErro( this, ecf.getMessageLog() );

					ps = con.prepareStatement( "DELETE FROM VDITVENDA " + "WHERE CODEMP=? AND CODFILIAL=? AND TIPOVENDA=? AND CODVENDA=? AND CODITVENDA=?" );
					ps.setInt( 1, Aplicativo.iCodEmp );
					ps.setInt( 2, ListaCampos.getMasterFilial( "VDITVENDA" ) );
					ps.setString( 3, "E" );
					ps.setInt( 4, txtCodVenda.getVlrInteger() );
					ps.setInt( 5, iCodItVenda );
					ps.executeUpdate();

					ps.close();
					con.commit();

					tbItem.delLinha( tbItem.getNumLinhas() - 1 );
				}
			}

		} catch ( Exception e ) {
			Funcoes.mensagemErro( null, "Erro ao inserir o ítem.\n" + "Informe esta mensagem ao administrador:\n" + e.getMessage() );
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private void invalidaParaNaoOrcamento() {

		txtCodProd.setAtivo( false );
		txtQtdade.setAtivo( false );
		txtPreco.setAtivo( false );
		txtPercDescItOrc.setAtivo( false );
		txtVlrDescItOrc.setAtivo( false );
		txtTotalItem.setAtivo( false );
		txtBaseCalc.setAtivo( false );
		txtCodLote.setAtivo( false );
		txtAliqIcms.setAtivo( false );
		txtValorIcms.setAtivo( false );

		Funcoes.mensagemInforma( null, "Este caixa somente está habilitado à efetuar vendas de orçamentos!" );

		btF11.requestFocus();

	}

	private synchronized boolean checkVendaAberta() {

		boolean ret = true;

		if ( ecf.isDocumentoAberto() ) {

			try {

				StringBuilder sql = new StringBuilder();
				sql.append( "select v.codvenda, v.docvenda, v.tipovenda, v.codcli, v.codplanopag, v.codtipomov, v.codvend, " );
				sql.append( "v.dtemitvenda, v.dtsaidavenda, v.vlrliqvenda, v.vlrbaseicmsvenda, v.vlricmsvenda " );
				sql.append( "from vdvenda v, pvcaixa c " );
				sql.append( "where v.codemp=? and v.codfilial=? and v.tipovenda='E' and substr(v.statusvenda,1,1)='P'" );
				sql.append( "and c.codemp=v.codempcx and c.codfilial=v.codfilialcx " );
				sql.append( "and c.codcaixa=v.codcaixa and c.codcaixa=?  " );
				sql.append( "order by v.dtins desc, v.hins desc" );

				PreparedStatement ps = con.prepareStatement( sql.toString() );
				ps.setInt( 1, AplicativoPDV.iCodEmp );
				ps.setInt( 2, AplicativoPDV.iCodFilial );
				ps.setInt( 3, AplicativoPDV.iCodCaixa );

				ResultSet rs = ps.executeQuery();

				if ( rs.next() ) {

					txtCodVenda.setVlrInteger( new Integer( rs.getInt( "codvenda" ) ) );
					txtTipoVenda.setVlrString( rs.getString( "tipovenda" ) );
					lcVenda.carregaDados();

					txtCodCli.setVlrInteger( new Integer( rs.getInt( "codcli" ) ) );
					lcCliente.carregaDados();

					txtCodPlanoPag.setVlrInteger( new Integer( rs.getInt( "codplanopag" ) ) );
					lcPlanoPag.carregaDados();

					txtCodTipoMov.setVlrInteger( new Integer( rs.getInt( "codtipomov" ) ) );
					lcTipoMov.carregaDados();
					txtCodVend.setVlrInteger( new Integer( rs.getInt( "codvend" ) ) );
					lcVendedor.carregaDados();
					lcClComis.carregaDados();

					txtValorTotalCupom.setVlrBigDecimal( rs.getBigDecimal( "vlrliqvenda" ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
					txtBaseCalc1.setVlrBigDecimal( rs.getBigDecimal( "vlrbaseicmsvenda" ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
					txtValorIcms1.setVlrBigDecimal( rs.getBigDecimal( "vlricmsvenda" ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
					txtTotalCupom.setVlrBigDecimal( rs.getBigDecimal( "vlrliqvenda" ).setScale( 2, BigDecimal.ROUND_HALF_UP ) );
					txtDtEmitVenda.setVlrDate( rs.getDate( "dtemitvenda" ) );
					txtDtSaidaVenda.setVlrDate( rs.getDate( "dtsaidavenda" ) );
					txtNumeroCupom.setVlrInteger( new Integer( rs.getInt( "docvenda" ) ) );

					sql = new StringBuilder();
					sql.append( "select i.coditvenda, i.codprod, p.descprod, i.qtditvenda, i.precoitvenda, i.cancitvenda, " );
					sql.append( "i.percicmsitvenda, i.vlrbaseicmsitvenda, i.vlricmsitvenda, i.vlrliqitvenda, i.codconv " );
					sql.append( "from vditvenda i, eqproduto p " );
					sql.append( "where i.codemp=? and i.codfilial=? and i.codvenda=? and i.tipovenda='E' " );
					sql.append( "and p.codemp=i.codemppd and p.codfilial=i.codfilialpd and p.codprod=i.codprod " );
					sql.append( "order by i.coditvenda" );

					ps = con.prepareStatement( sql.toString() );
					ps.setInt( 1, AplicativoPDV.iCodEmp );
					ps.setInt( 2, AplicativoPDV.iCodFilial );
					ps.setInt( 3, txtCodVenda.getVlrInteger().intValue() );

					ResultSet rs2 = ps.executeQuery();

					while ( rs2.next() ) {

						tbItem.adicLinha( new Object[] { new Integer( rs2.getInt( "coditvenda" ) ), new Integer( rs2.getInt( "codprod" ) ), rs2.getString( "descprod" ), rs2.getBigDecimal( "qtditvenda" ).setScale( Aplicativo.casasDec, BigDecimal.ROUND_HALF_UP ),
								rs2.getBigDecimal( "precoitvenda" ).setScale( Aplicativo.casasDecFin, BigDecimal.ROUND_HALF_UP ), rs2.getBigDecimal( "vlricmsitvenda" ).setScale( Aplicativo.casasDecFin, BigDecimal.ROUND_HALF_UP ),
								rs2.getBigDecimal( "vlrbaseicmsitvenda" ).setScale( Aplicativo.casasDecFin, BigDecimal.ROUND_HALF_UP ), rs2.getBigDecimal( "vlricmsitvenda" ).setScale( Aplicativo.casasDecFin, BigDecimal.ROUND_HALF_UP ), "S".equals( rs2.getString( "cancitvenda" ) ) ? "C" : "",
								new Integer( rs2.getInt( "codconv" ) ).intValue() > 0 ? rs2.getString( "codconv" ) : "" } );

						txtCodProd1.setVlrString( rs2.getString( "codprod" ) );
						txtDescProd.setVlrString( rs2.getString( "descprod" ) );
						txtQtdadeItem.setVlrBigDecimal( rs2.getBigDecimal( "qtditvenda" ).setScale( Aplicativo.casasDec, BigDecimal.ROUND_HALF_UP ) );
						txtValorTotalItem.setVlrBigDecimal( rs2.getBigDecimal( "vlrliqitvenda" ).setScale( Aplicativo.casasDecPre, BigDecimal.ROUND_HALF_UP ) );
					}

					ret = false;
				}

				Funcoes.mensagemInforma( null, "Cupom fiscal em aberto!" );

				con.commit();
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}

		return ret;
	}

	private synchronized boolean iniVenda() {

		if ( checkVendaAberta() ) {
			if ( !obrigaOrcamento ) {
				return iniVenda( CODCLI, PLANOPAG, CODVEND );
			}
		}

		return false;
	}

	private synchronized boolean iniVenda( int codCli, int codPlanoPag, int vend ) {

		CODORCMASTER = 0;

		int iseq = getCodSeqCaixa();

		if ( iseq == -1 ) {
			setVisible( false );
			return false;
		}

		lcVenda.insert( false );
		txtCodCaixa.setVlrInteger( AplicativoPDV.iCodCaixa );
		txtCodVenda.setVlrInteger( iseq );
		txtTipoVenda.setVlrString( "E" );
		txtCodCli.setVlrInteger( new Integer( codCli ) );
		lcCliente.carregaDados();
		lcProduto.limpaCampos( true );
		txtCodPlanoPag.setVlrInteger( new Integer( codPlanoPag ) );
		txtQtdadeItem.setVlrString( "" );
		txtValorTotalCupom.setVlrString( "" );
		txtValorTotalItem.setVlrString( "" );
		txtBaseCalc1.setVlrString( "" );
		txtValorIcms1.setVlrString( "" );
		txtTotalCupom.setVlrString( "" );
		lcPlanoPag.carregaDados();
		txtCodTipoMov.setVlrInteger( new Integer( CODTIPOMOV ) );
		lcTipoMov.carregaDados();
		txtCodVend.setVlrInteger( new Integer( vend ) );
		lcVendedor.carregaDados();
		lcClComis.carregaDados();
		txtDtEmitVenda.setVlrDate( new Date() );
		txtDtSaidaVenda.setVlrDate( new Date() );

		txtNumeroCupom.setVlrInteger( ecf.getNumeroDocumento() + 1 );

		tbItem.limpa();
		mostraInfoImp();

		iniItem();

		return true;
	}

	private void leituraX() {

		if ( lcVenda.getStatus() == ListaCampos.LCS_SELECT ) {

			Funcoes.mensagemInforma( this, "Ainda existe uma venda ativa!" );
			return;
		}
		if ( Funcoes.mensagemConfirma( null, "Confirma impressão de leitura X?" ) == JOptionPane.YES_OPTION ) {
			if ( !ecf.leituraX() ) {
				Funcoes.mensagemErro( this, ecf.getMessageLog() );
			}
		}
	}

	private void marcaLinha( int iItem ) {

		int iLinha = -1;
		iLinha = getLinha( iItem );

		if ( iLinha >= 0 ) {

			tbItem.setRowBackGround( iLinha, new Color( 254, 213, 192 ) );
			tbItem.updateUI();
			tbItem.repaint();
			tbItem.setVisible( true );
		}
	}

	private void minPesoFrete( int iCodProd, BigDecimal iQtd ) {

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT PESOBRUTPROD, PESOLIQPROD FROM EQPRODUTO WHERE CODEMP=? AND CODFILIAL=? AND CODPROD=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			ps.setInt( 3, iCodProd );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				pesoBrutFrete -= ( rs.getFloat( 1 ) * iQtd.floatValue() );
				pesoLiqFrete -= ( rs.getFloat( 2 ) * iQtd.floatValue() );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro ao somar peso do produto!\n" + e.getMessage(), true, con, e );
			e.printStackTrace();
		}
	}

	public synchronized boolean montaVendaOrc( int arg ) {

		boolean retorno = true;

		try {

			CODORCMASTER = 0;

			Vector<Integer> vArgs = new Vector<Integer>();

			PreparedStatement ps = con.prepareStatement( "SELECT CODCLI, CODPLANOPAG, CODVEND FROM VDORCAMENTO WHERE CODEMP=? AND CODFILIAL=? AND CODORC=?" );
			ps.setInt( 1, AplicativoPDV.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "ORCAMENTO" ) );
			ps.setInt( 3, arg );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {

				vArgs.addElement( new Integer( rs.getInt( 1 ) ) );
				vArgs.addElement( new Integer( rs.getInt( 2 ) ) );
				vArgs.addElement( new Integer( rs.getInt( 3 ) ) );
			}

			rs.close();
			ps.close();

			con.commit();

			if ( vArgs.size() == 3 ) {

				txtCodCli.setVlrInteger( vArgs.elementAt( 0 ) );
				lcCliente.carregaDados();
				txtCodPlanoPag.setVlrInteger( vArgs.elementAt( 1 ) );
				lcPlanoPag.carregaDados();
				txtCodVend.setVlrInteger( vArgs.elementAt( 2 ) );
				lcVendedor.carregaDados();
				lcClComis.carregaDados();

				CODORCMASTER = arg;
			}

		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro ao gerar venda do orçamento.", true, con, e );
			e.printStackTrace();
			retorno = false;
		}

		return retorno;

	}

	private void mostraInfoImp() {

		String status = ecf.getStatusImpressora();
		status = status != null ? status.replace( StatusStandard.RETORNO_OK.getMessage() + "\n", "" ) : "";

		String str = "<HTML><CENTER>" + status + "</CENTER></HTML>";

		lbAvisoImp.setText( str );
	}

	private boolean mostraTelaPass() {

		boolean retorno = false;

		FPassword fpw = new FPassword( this, FPassword.ABRE_GAVETA, "Abrir gaveta", con );
		fpw.execShow();

		retorno = fpw.OK;

		fpw.dispose();

		return retorno;
	}

	private Object[] paramFecha() {

		Object[] param = new Object[ 13 ];

		param[ 0 ] = txtCodVenda.getVlrInteger();
		param[ 1 ] = txtTipoVenda.getVlrString();
		param[ 2 ] = txtTotalCupom.getVlrBigDecimal();
		param[ 3 ] = txtNumeroCupom.getVlrInteger();
		param[ 4 ] = txtCodPlanoPag.getVlrInteger();
		param[ 5 ] = con;
		param[ 6 ] = getInfoCli( txtCodCli.getVlrInteger().intValue() );
		param[ 7 ] = new Boolean( trocouCli );

		if ( carregaPesoFrete ) {

			param[ 8 ] = new BigDecimal( pesoBrutFrete );
			param[ 9 ] = new BigDecimal( pesoLiqFrete );
			param[ 10 ] = new BigDecimal( vlrFrete );
		}
		else {

			param[ 8 ] = new Boolean( false );
			param[ 9 ] = null;
			param[ 10 ] = null;
		}

		param[ 11 ] = new Boolean( CODORCMASTER > 0 );
		param[ 12 ] = txtCodVend.getVlrInteger();

		carregaPesoFrete = false;

		return param;
	}

	private void repeteItem() {

		if ( vCacheItem.size() == 2 ) {

			txtCodProd.setVlrString( (String) vCacheItem.elementAt( 0 ) );

			actionCodProd();

			txtQtdade.setVlrBigDecimal( (BigDecimal) vCacheItem.elementAt( 1 ) );

			actionQtdade();
		}
	}

	private boolean testaCodLote() {

		boolean bValido = false;

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT COUNT(*) FROM EQLOTE WHERE CODLOTE=? AND CODPROD=? AND CODEMP=? AND CODFILIAL=?" );
			ps.setString( 1, txtCodLote.getVlrString().trim() );
			ps.setInt( 2, txtCodPlanoPag.getVlrInteger().intValue() );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "EQLOTE" ) );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				bValido = rs.getInt( 1 ) > 0;
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao consultar a tabela EQLOTE!\n" + err.getMessage(), true, con, err );
		}

		return bValido;

	}

	private void trocaCli() {

		int codcliant = txtCodCli.getVlrInteger();

		try {

			txtCodCli.mostraDLF2FK();

			int codclinovo = txtCodCli.getVlrInteger();

			if ( codcliant != codclinovo ) {

				PreparedStatement ps = con.prepareStatement( "UPDATE VDVENDA SET CODCLI=? WHERE CODEMP=? AND CODFILIAL=? AND CODVENDA=? AND TIPOVENDA='E'" );
				ps.setInt( 1, codclinovo );
				ps.setInt( 2, Aplicativo.iCodEmp );
				ps.setInt( 3, ListaCampos.getMasterFilial( "VDVENDA" ) );
				ps.setInt( 4, txtCodVenda.getVlrInteger() );
				ps.executeUpdate();
				ps.close();

				con.commit();
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao trocar cliente!\n" + e.getMessage(), true, con, e );
		} finally {
			lcVenda.carregaDados();
		}
	}

	private void buscaPreco() {

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT PRECO FROM VDBUSCAPRECOSP(?,?,?,?,?,?,?,?,?,?,?,?)" );
			ps.setInt( 1, Integer.parseInt( txtCodProd.getVlrString().trim() ) );
			ps.setInt( 2, txtCodCli.getVlrInteger().intValue() );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
			ps.setInt( 5, txtCodPlanoPag.getVlrInteger().intValue() );
			ps.setInt( 6, Aplicativo.iCodEmp );
			ps.setInt( 7, lcPlanoPag.getCodFilial() );
			ps.setInt( 8, txtCodTipoMov.getVlrInteger().intValue() );
			ps.setInt( 9, Aplicativo.iCodEmp );
			ps.setInt( 10, ListaCampos.getMasterFilial( "EQTIPOMOV" ) );
			ps.setInt( 11, Aplicativo.iCodEmp );
			ps.setInt( 12, Aplicativo.iCodFilial );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				txtPreco.setVlrBigDecimal( rs.getString( 1 ) != null ? ( new BigDecimal( rs.getString( 1 ) ) ) : ( new BigDecimal( 0 ) ) );
			}
			else {
				txtPreco.setVlrBigDecimal( new BigDecimal( 0 ) );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar o preço!\n" + err.getMessage(), true, con, err );
		}
	}

	public synchronized void setFocusProd() {

		if ( txtCodProd.isFocusable() ) {
			txtCodProd.requestFocus();
		}
	}

	private int getLinha( int iItem ) {

		int iLinha = -1;
		for ( int i = 0; i < tbItem.getNumLinhas(); i++ ) {
			if ( ! ( (String) tbItem.getValor( i, 8 ) ).equals( "C" ) ) {
				if ( iItem == ( (Integer) tbItem.getValor( i, 0 ) ).intValue() ) {
					iLinha = i;
					break;
				}
			}
		}
		return iLinha;
	}

	private void getLote() {

		try {

			StringBuilder sql = new StringBuilder();
			sql.append( "SELECT MIN(L.CODLOTE) FROM EQLOTE L WHERE L.CODPROD=? AND L.CODFILIAL=? " );
			sql.append( ( (Boolean) prefs( 2 ) ).booleanValue() ? "AND L.SLDLIQLOTE>0 " : "" );
			sql.append( "AND L.CODEMP=? AND L.VENCTOLOTE = ( SELECT MIN(VENCTOLOTE) FROM EQLOTE LS " );
			sql.append( "WHERE LS.CODPROD=L.CODPROD AND LS.CODFILIAL=L.CODFILIAL AND LS.CODEMP=L.CODEMP " );
			sql.append( ( (Boolean) prefs( 2 ) ).booleanValue() ? "AND LS.SLDLIQLOTE>0 " : "" );
			sql.append( "AND VENCTOLOTE >= CAST('today' AS DATE))" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, txtCodProd.getVlrInteger().intValue() );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQLOTE" ) );
			ps.setInt( 3, Aplicativo.iCodEmp );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {

				txtCodLote.setVlrString( rs.getString( 1 ) != null ? rs.getString( 1 ) : "" );
				lcLote.carregaDados();
			}

			rs.close();
			ps.close();
			con.commit();
		} catch ( Exception err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar lote!\n" + err );
		}
	}

	private String[] getInfoCli( int codcli ) {

		String[] ret = new String[ 6 ];

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT RAZCLI, CPFCLI, ENDCLI, NUMCLI, CIDCLI, UFCLI " + "FROM VDCLIENTE WHERE CODEMP=? AND CODFILIAL=? AND CODCLI=? " );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			ps.setInt( 3, codcli );

			ResultSet rs = ps.executeQuery();

			while ( rs.next() ) {
				for ( int i = 0; i < 6; i++ ) {
					ret[ i ] = rs.getString( i + 1 );
				}
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro ao pegar dados do cliente!\n" + e.getMessage(), true, con, e );
			e.printStackTrace();
		}

		return ret;
	}

	public String getDescEst() {

		String sDesc = "ESTAÇÃO DE TRABALHO NÃO CADASTRADA";

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT DESCEST FROM SGESTACAO WHERE CODEST=? AND CODEMP=? AND CODFILIAL=?" );
			ps.setInt( 1, Aplicativo.iNumEst );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, Aplicativo.iCodFilial );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				sDesc = rs.getString( "DescEst" );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( null, err.getMessage() );
			sDesc = "NÃO FOI POSSÍVEL REGISTRAR A ESTAÇÃO DE TRABALHO! ! !";
		}

		return sDesc;
	}

	private int getTipoMov() {

		int retorno = 0;

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT CODTIPOMOV FROM SGPREFERE4 WHERE CODEMP=? AND CODFILIAL=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				retorno = rs.getInt( "CODTIPOMOV" );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar o tipo de movimento.\n" + "Provavelmente não foram gravadas corretamente as preferências!\n" + err.getMessage() );
			err.printStackTrace();
		}

		return retorno;
	}

	private int getVendedor() {

		int codvend = 0;
		int codatend = 0;
		String nomeatend = "";

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT CODATEND,NOMEATEND,CODVEND FROM ATATENDENTE " + "WHERE CODEMPUS=? AND CODFILIALUS=? AND IDUSU=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "ATENDENTE" ) );
			ps.setString( 3, Aplicativo.getUsuario().getIdusu() );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				codatend = rs.getInt( "CODATEND" );
				nomeatend = rs.getString( "NOMEATEND" );
				codvend = rs.getInt( "CODVEND" );
			}

			rs.close();
			ps.close();

			con.commit();

			if ( codatend != 0 && codvend == 0 ) {
				Funcoes.mensagemInforma( this, "O atendente " + nomeatend + "\nvinculado ao usuário " + Aplicativo.getUsuario().getIdusu() + "\nnão possui um comissionado vinculado!" + "\nVerifique o cadastro de atendentes." );
			}
			else if ( codatend == 0 ) {
				Funcoes.mensagemInforma( this, "O usuário " + Aplicativo.getUsuario().getIdusu() + "\nnão possui um atendente vinculado!" + "\nVerifique o cadastro de atendentes." );
			}

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar o comissionado.\n" + err.getMessage() );
			err.printStackTrace();
			dispose();
		}

		return codvend;
	}

	private BigDecimal[] getItComis( int iCodItOrc ) {

		BigDecimal[] retorno = new BigDecimal[ 2 ];

		if ( iCodItOrc > 0 ) {

			try {

				StringBuilder sql = new StringBuilder();
				sql.append( "SELECT P.COMISPROD, ( ( IT.VLRLIQITORC * P.COMISPROD ) / 100 ) as VLRLIQCOMIS " );
				sql.append( "FROM VDITORCAMENTO IT, VDORCAMENTO O, VDVENDEDOR VD, EQPRODUTO P " );
				sql.append( "WHERE IT.CODEMP=? AND IT.CODFILIAL=? AND IT.CODORC=? AND IT.CODITORC=? " );
				sql.append( "AND O.CODEMP=IT.CODEMP AND O.CODFILIAL=IT.CODFILIAL AND O.CODORC=IT.CODORC " );
				sql.append( "AND VD.CODEMP=O.CODEMPVD AND VD.CODFILIAL=O.CODFILIALVD AND VD.CODVEND=O.CODVEND " );
				sql.append( "AND P.CODEMP=IT.CODEMPPD AND P.CODFILIAL=IT.CODFILIALPD AND P.CODPROD=IT.CODPROD" );

				PreparedStatement ps = con.prepareStatement( sql.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "VDITORCAMENTO" ) );
				ps.setInt( 3, CODORCMASTER );
				ps.setInt( 4, iCodItOrc );

				ResultSet rs = ps.executeQuery();

				if ( rs.next() ) {
					retorno[ 0 ] = rs.getBigDecimal( "COMISPROD" );
					retorno[ 1 ] = rs.getBigDecimal( "VLRLIQCOMIS" );
				}

				rs.close();
				ps.close();

				con.commit();
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao buscar comissão.\n" + err.getMessage() );
				err.printStackTrace();
			}
		}
		else {

			try {

				PreparedStatement ps = con.prepareStatement( "SELECT P.COMISPROD FROM EQPRODUTO P " + "WHERE P.CODEMP=? AND P.CODFILIAL=? AND P.CODPROD=?" );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
				ps.setInt( 3, txtCodProd.getVlrInteger().intValue() );

				ResultSet rs = ps.executeQuery();

				if ( rs.next() ) {
					retorno[ 0 ] = rs.getBigDecimal( "COMISPROD" ) != null ? rs.getBigDecimal( "COMISPROD" ) : new BigDecimal( "0.00" );
					retorno[ 1 ] = ( retorno[ 0 ].multiply( txtPreco.getVlrBigDecimal() ) ).divide( new BigDecimal( 100 ), AplicativoPDV.casasDecPre, BigDecimal.ROUND_HALF_UP );
				}

				rs.close();
				ps.close();

				con.commit();
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao buscar comissão.\n" + err.getMessage() );
				err.printStackTrace();
			}
		}

		return retorno;
	}

	private int getCodCli() {

		int iRet = 0;

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT CODCLI FROM SGPREFERE4 WHERE CODEMP=? AND CODFILIAL=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				iRet = rs.getInt( "CODCLI" );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar o código do cliente.\n" + "Provavelmente não foram gravadas corretamente as preferências!\n" + err.getMessage() );
			err.printStackTrace();
		}

		return iRet;
	}

	private int getPlanoPag() {

		int iRet = 0;

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT CODPLANOPAG FROM SGPREFERE4 WHERE CODEMP=? AND CODFILIAL=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				iRet = rs.getInt( "CODPLANOPAG" );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar o plano de pagamento.\n" + "Provavelmente não foram gravadas corretamente as preferências!\n" + err.getMessage() );
			err.printStackTrace();
		}

		return iRet;
	}

	private Integer getCodSeqCaixa() {

		Integer retorno = new Integer( -1 );

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT ISEQ FROM SPGERANUMPDV(?,?,?)" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			ps.setInt( 3, AplicativoPDV.iCodCaixa );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				retorno = new Integer( rs.getInt( 1 ) );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar sequencia." + err.getMessage() );
			err.printStackTrace();
		}

		return retorno;
	}

	private boolean getObrigatorioOrcamento() {

		boolean result = false;

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT ORCCAIXA FROM PVCAIXA WHERE CODEMP=? AND CODFILIAL=? AND CODCAIXA=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PVCAIXA" ) );
			ps.setInt( 3, AplicativoPDV.iCodCaixa );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {

				result = "S".equals( rs.getString( "ORCCAIXA" ) );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + e.getMessage(), true, con, e );
		}

		return result;
	}

	private Object prefs( int index ) {

		if ( pref[ 0 ] == null ) {

			pref = new Object[ 3 ];

			StringBuilder sql = new StringBuilder();
			sql.append( "SELECT P4.ADICPDV, P4.CODPROD, P1.CONTESTOQ " );
			sql.append( "FROM SGPREFERE1 P1, SGPREFERE4 P4 " );
			sql.append( "WHERE P4.CODEMP=? AND P4.CODFILIAL=? " );
			sql.append( "AND P1.CODEMP=P4.CODEMP AND P1.CODFILIAL=P4.CODFILIAL" );

			try {

				PreparedStatement ps = con.prepareStatement( sql.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE4" ) );

				ResultSet rs = ps.executeQuery();

				if ( rs.next() ) {

					pref[ 0 ] = new Boolean( rs.getString( "ADICPDV" ).equals( "S" ) );
					pref[ 1 ] = rs.getString( "CODPROD" );
					pref[ 2 ] = new Boolean( rs.getString( "CONTESTOQ" ).equals( "S" ) );
				}

				rs.close();
				ps.close();

				con.commit();
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage(), true, con, err );
			}
		}

		return pref[ index ];
	}

	private void loadStatusBar() {

		pnStatusBar.add( sbVenda, BorderLayout.CENTER );
		pnRodape.add( pnStatusBar, BorderLayout.CENTER );
		sbVenda.setUsuario( Aplicativo.getUsuario().getIdusu() );
		sbVenda.setCodFilial( Aplicativo.iCodFilial );
		sbVenda.setNomeFilial( Aplicativo.sNomeFilial );
		sbVenda.setNumEst( Aplicativo.iNumEst );
		sbVenda.setDescEst( getDescEst() );
	}

	private synchronized void actionCodProd() {

		if ( "S".equals( txtTelaAdicPDV.getVlrString().trim() ) && pluginVenda != null ) {

			if ( pluginVenda.beforeVendaItem() ) {
				actionPostVendaForStatus();
			}
			if ( pluginVenda.afterVendaItem() ) {
				txtTelaAdicPDV.setVlrString( "" );
				txtCodProd.setVlrString( "" );
				txtQtdade.setVlrString( "" );
				txtPreco.setVlrString( "" );
				txtCodConv.setVlrString( "" );
			}
		}
	}

	private synchronized void actionQtdade() {

		if ( txtCodProd.getVlrString().length() == 0 ) {
			Funcoes.mensagemInforma( null, "Produto em branco." );
		}
		else if ( txtQtdade.getVlrDouble().doubleValue() == 0 ) {
			Funcoes.mensagemInforma( null, "Quantidade em branco." );
		}
		else if ( txtPercDescItOrc.getAtivo() ) {
			txtPercDescItOrc.requestFocus();
		}
		else {
			actionPostVendaForStatus();
		}
	}

	private synchronized void actionVlrDescItProd() {

		if ( txtCodLote.isEditable() ) {
			txtCodLote.requestFocus();
		}
		else {
			actionPostVendaForStatus();
		}
	}

	private synchronized void actionCodLote() {

		if ( txtCodLote.getText().trim().length() == 0 ) {
			Funcoes.mensagemErro( this, "Lote não informado!" );
			txtCodLote.requestFocus();
		}
		else {
			actionPostVendaForStatus();
		}
	}

	private synchronized boolean actionPostVendaForStatus() {

		boolean action_return = false;

		if ( lcVenda.getStatus() == ListaCampos.LCS_INSERT ) {
			if ( lcVenda.post() ) {
				action_return = insereItem();
				iniItem();
			}
		}
		else if ( lcVenda.getStatus() == ListaCampos.LCS_SELECT ) {
			action_return = insereItem();
			iniItem();
			lcVenda.carregaDados();
		}

		return action_return;
	}

	public void beforeCarrega( CarregaEvent cevt ) {

	}

	public void afterCarrega( CarregaEvent cevt ) {

		if ( cevt.getListaCampos() == lcProduto && txtCodProd.getVlrString().length() > 0 ) {

			buscaPreco();

			if ( txtCLoteProd.getVlrString().equals( "S" ) ) {
				txtCodLote.setAtivo( true );
				getLote();
			}
			else {
				txtCodLote.setVlrString( "" );
				txtCodLote.setAtivo( false );
			}
		}
	}

	public void beforePost( PostEvent pevt ) {

	}

	public void afterPost( PostEvent pevt ) {

		if ( pevt.getListaCampos() == lcVenda && pevt.ok ) {
			if ( !ecf.abrirCupom() ) {
				Funcoes.mensagemErro( this, ecf.getMessageLog() );
			}
		}
	}

	public synchronized void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btF3 ) {
			cancItem();
		}
		else if ( evt.getSource() == btCtrlF3 ) {
			cancCupom();
		}
		else if ( evt.getSource() == btF4 ) {
			fechaVenda();
		}
		else if ( evt.getSource() == btF5 ) {
			leituraX();
		}
		else if ( evt.getSource() == btF6 ) {
			abreGaveta();
		}
		else if ( evt.getSource() == btF7 ) {
			DlgCalc calc = new DlgCalc();
			Aplicativo.telaPrincipal.criatela( "Calc", calc, con );
			calc.setTelaPrim( Aplicativo.telaPrincipal );
		}
		else if ( evt.getSource() == btF8 ) {
			repeteItem();
		}
		else if ( evt.getSource() == btF9 ) {
			trocaCli();
		}
		else if ( evt.getSource() == btF10 ) {
			fechaCaixa();
		}
		else if ( evt.getSource() == btF11 ) {
			abreAdicOrc();
		}
	}

	public void keyPressed( KeyEvent kevt ) {

		switch ( kevt.getKeyCode() ) {
			case KeyEvent.VK_CONTROL :
				CTRL = true;
				break;
			case KeyEvent.VK_F3 :
				if ( CTRL ) {
					btCtrlF3.doClick();
				}
				else {
					btF3.doClick();
				}
				CTRL = false;
				break;
			case KeyEvent.VK_F4 :
				btF4.doClick();
				break;
			case KeyEvent.VK_F5 :
				btF5.doClick();
				break;
			case KeyEvent.VK_F6 :
				btF6.doClick();
				break;
			case KeyEvent.VK_F7 :
				btF7.doClick();
				break;
			case KeyEvent.VK_F8 :
				btF8.doClick();
				break;
			case KeyEvent.VK_F9 :
				btF9.doClick();
				break;
			case KeyEvent.VK_F10 :
				btF10.doClick();
				break;
			case KeyEvent.VK_F11 :
				btF11.doClick();
				break;
		}
		if ( kevt.getKeyCode() == KeyEvent.VK_ENTER ) {
			if ( kevt.getSource() == txtCodProd ) {
				actionCodProd();
			}
			else if ( kevt.getSource() == txtQtdade ) {
				actionQtdade();
			}
			else if ( kevt.getSource() == txtVlrDescItOrc ) {
				actionVlrDescItProd();
			}
			else if ( kevt.getSource() == txtCodLote ) {
				actionCodLote();
			}
		}
	}

	public void focusGained( FocusEvent fevt ) {

	}

	public void focusLost( FocusEvent fevt ) {

		if ( fevt.getSource() == txtPercDescItOrc ) {

			if ( txtPercDescItOrc.getText().trim().length() == 0 ) {

				txtVlrDescItOrc.setAtivo( true );
				txtVlrDescItOrc.requestFocus();
			}
			else {

				txtVlrDescItOrc.setVlrBigDecimal( txtQtdade.getVlrBigDecimal().multiply( txtPreco.getVlrBigDecimal().multiply( txtPercDescItOrc.getVlrBigDecimal() ).divide( new BigDecimal( "100" ), 2, BigDecimal.ROUND_HALF_UP ) ) );
				txtVlrDescItOrc.setAtivo( false );

				if ( txtCodLote.isEditable() ) {
					txtCodLote.requestFocus();
				}
				else {
					actionPostVendaForStatus();
				}
			}
		}
		else if ( fevt.getSource() == txtVlrDescItOrc ) {
			if ( txtVlrDescItOrc.getText().trim().length() == 0 ) {
				txtPercDescItOrc.setAtivo( true );
			}
			else if ( txtVlrDescItOrc.getAtivo() ) {
				txtPercDescItOrc.setAtivo( false );
			}
		}
	}

	// O botão sair execute este método para sair:
	public void setVisible( boolean show ) {

		if ( FreedomPDV.bECFTerm ) {

			if ( show ) {

				int result = FreedomPDV.abreCaixa( con, ecf );

				if ( result == -1 ) {
					Funcoes.mensagemInforma( null, "Caixa não foi aberto!" );
					super.setVisible( false );
				}
				else if ( result == 3 ) {
					dispose();
				}
				else {
					if ( AplicativoPDV.caixaAberto( con ) || FreedomPDV.pegaValorINI( con ) ) {
						iniVenda();
						if ( obrigaOrcamento ) {
							invalidaParaNaoOrcamento();
						}
						super.setVisible( true );
					}
					else {
						super.setVisible( false );
					}
				}
			}
			else {
				if ( ecf.isDocumentoAberto() ) {
					Funcoes.mensagemInforma( null, "Cupom fiscal está aberto!" );
					return;
				}
				super.setVisible( false );
			}
		}
		else {
			FreedomPDV.killProg( 1, "Esta estação de trabalho não é um PDV!\n" + "Verifique o cadastro desta estação de trabalho através do modulo Standard." );
		}
	}

	public void setConexao( DbConnection con ) {

		super.setConexao( con );

		lcCliente.setConexao( con );
		lcVendedor.setConexao( con );
		lcClComis.setConexao( con );
		lcPlanoPag.setConexao( con );
		lcVenda.setConexao( con );
		lcProduto.setConexao( con );
		lcLote.setConexao( con );
		lcTipoMov.setConexao( con );
		lcSerie.setConexao( con );
		lcClFiscal.setConexao( con );
		lcConv.setConexao( con );

		loadStatusBar();

		obrigaOrcamento = getObrigatorioOrcamento();

		CODTIPOMOV = getTipoMov();
		CODCLI = getCodCli();
		PLANOPAG = getPlanoPag();
		CODVEND = getVendedor();

		txtCodTipoMov.setVlrInteger( new Integer( CODTIPOMOV ) );
		txtCodCli.setVlrInteger( new Integer( CODCLI ) );

		txtCodProd.setBuscaGenProd( new DLCodProd( con, txtQtdade, null ) );

		carregaPlugin();
	}

	public void windowGainedFocus( WindowEvent e ) {

		setFocusProd();
	}

	public void windowLostFocus( WindowEvent e ) {

	}
}
