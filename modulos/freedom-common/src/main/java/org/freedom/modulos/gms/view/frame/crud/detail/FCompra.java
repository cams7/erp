/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * @version 31/08/2009 - Alex Rodrigues
 * @version 16/12/2009 - Anderson Sanchez
 * @version 10/03/2010 - Anderson Sanchez
 * @version 22/03/2010 - Anderson Sanchez
 * 
 * 
 *          Projeto: Freedom <BR>
 * 
 *          Pacote: org.freedom.modulos.gms <BR>
 *          Classe:
 * @(#)FCompra.java <BR>
 * 
 *                  Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                  modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                  na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                  Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                  sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                  Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                  Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                  de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                  Tela para cadastro de pedidos e notas fiscais de compra.
 * 
 */

package org.freedom.modulos.gms.view.frame.crud.detail;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.bmps.Icone;
import org.freedom.business.component.NFEntrada;
import org.freedom.infra.functions.ConversionFunctions;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.object.EmailBean;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.component.Layout;
import org.freedom.library.component.Leiaute;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDetalhe;
import org.freedom.library.swing.frame.FObservacao;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.util.SwingParams;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modules.nfe.control.AbstractNFEFactory;
import org.freedom.modulos.cfg.view.frame.crud.plain.FPais;
import org.freedom.modulos.cfg.view.frame.crud.plain.FUF;
import org.freedom.modulos.gms.business.component.NumSerie;
import org.freedom.modulos.gms.business.object.TipoMov;
import org.freedom.modulos.gms.business.object.TipoProd;
import org.freedom.modulos.gms.dao.DAOImportacao;
import org.freedom.modulos.gms.inter.InterCompra;
import org.freedom.modulos.gms.view.dialog.utility.DLBuscaImportacao;
import org.freedom.modulos.gms.view.dialog.utility.DLBuscaPedCompra;
import org.freedom.modulos.gms.view.dialog.utility.DLLote;
import org.freedom.modulos.gms.view.dialog.utility.DLSerieGrid;
import org.freedom.modulos.gms.view.frame.crud.plain.FBuscaCpCompl;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FTipoMov;
import org.freedom.modulos.lvf.business.component.CalcImpostos;
import org.freedom.modulos.lvf.business.object.SeqSerie;
import org.freedom.modulos.lvf.view.frame.crud.detail.FCLFiscal;
import org.freedom.modulos.nfe.database.jdbc.NFEConnectionFactory;
import org.freedom.modulos.pcp.view.dialog.utility.DLFinalizaOP;
import org.freedom.modulos.std.DLBuscaEstoq;
import org.freedom.modulos.std.DLCodProd;
import org.freedom.modulos.std.view.dialog.report.DLRPedido;
import org.freedom.modulos.std.view.dialog.report.DLRetRemessa;
import org.freedom.modulos.std.view.dialog.utility.DLBuscaDescProd;
import org.freedom.modulos.std.view.dialog.utility.DLBuscaProd;
import org.freedom.modulos.std.view.dialog.utility.DLFechaCompra;
import org.freedom.modulos.std.view.dialog.utility.DLGuiaTraf;
import org.freedom.modulos.std.view.frame.crud.detail.FPlanoPag;
import org.freedom.modulos.std.view.frame.crud.plain.FAlmox;
import org.freedom.modulos.std.view.frame.crud.plain.FModNota;
import org.freedom.modulos.std.view.frame.crud.plain.FNatoPer;
import org.freedom.modulos.std.view.frame.crud.plain.FSerie;
import org.freedom.modulos.std.view.frame.crud.tabbed.FFornecedor;
import org.freedom.modulos.std.view.frame.crud.tabbed.FTransp;

public class FCompra extends FDetalhe implements InterCompra, PostListener, CarregaListener, FocusListener, ActionListener, InsertListener, MouseListener {

	private static final long serialVersionUID = 1L;

	private static final int TAMANHOCHAVE = 44;

	private int casasDec = Aplicativo.casasDec;

	private int casasDecFin = Aplicativo.casasDecFin;

	private int casasDecPre = Aplicativo.casasDecPre;

	private String codProdutoFornecedor;

	private JTabbedPanePad tpnAbas = new JTabbedPanePad();

	private JPanelPad pinCab = new JPanelPad();

	private JPanelPad pinDet = new JPanelPad();

	private JPanelPad pinCabCompra = new JPanelPad();

	private JPanelPad pinCabTransp = new JPanelPad();

	private JPanelPad pinCabFiscal = new JPanelPad();

	private JPanelPad pinCabObs01 = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	private JPanelPad pinCabObs02 = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	private JPanelPad pinCabObs03 = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	private JPanelPad pinCabObs04 = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	private JPanelPad pinCabInfCompl = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	private JPanelPad pnGeralDet = new JPanelPad();

	private JPanelPad pnIpiIcms = new JPanelPad();

	private JPanelPad pnImpDet = new JPanelPad();

	private JPanelPad pinNfe = new JPanelPad();

	private JPanelPad pinCabSolCompra = new JPanelPad();

	private JPanelPad pinCabImportacao = new JPanelPad();

	private JPanelPad pinTot = new JPanelPad( 200, 200 );

	private JTabbedPanePad tpnCab = new JTabbedPanePad();

	private JPanelPad pnTot = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	private JPanelPad pnCenter = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JButtonPad btFechaCompra = new JButtonPad( Icone.novo( "btOk.png" ) );

	private JButtonPad btObs = new JButtonPad( Icone.novo( "btObs1.png" ) );

	private JButtonPad btBuscaRemessa = new JButtonPad( "Remessa", Icone.novo( "btExecuta.png" ) );

	private JTextFieldPad txtCodCompra = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodCompraLf = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodItCompraLf = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodTipoMov = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoMov = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodImp = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDtEmitCompra = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDtEntCompra = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodForImp = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodPlanoPagImp = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtCNPJFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtCodPlanoPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodItCompra = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtQtdItCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 13, 0 );

	private JTextFieldPad txtRefProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCLoteProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtTipoProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtSerieProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtPrecoItCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtPercDescItCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, 2 );

	private JTextFieldPad txtVlrDescItCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtPercComItCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, 2 );

	private JTextFieldPad txtCalcTrib = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtCodNat = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtVlrBaseICMSItCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtPercICMSItCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtVlrICMSItCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtAliqISSItCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtVlrISSItCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtVlrLiqItCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtPercICMSStItCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtVlrBaseICMSStItCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtVlrICMSStItCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldFK txtVlrPISItCompra = new JTextFieldFK( JTextFieldFK.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldFK txtVlrCOFINSItCompra = new JTextFieldFK( JTextFieldFK.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldFK txtVlrSISCOMEXItCompra = new JTextFieldFK( JTextFieldFK.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtDesBloqCV = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtCodLote = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtNumSerie = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldFK txtObsSerie = new JTextFieldFK( JTextFieldPad.TP_STRING, 150, 0 );

	private JTextFieldPad txtCodAlmoxProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtTipoFisc = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtRedFisc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, 2 );

	private JTextFieldPad txtTpRedIcmsFisc = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtCodUnid = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtCustoPEPSProd = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtCustoMPMProd = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtVlrIPICompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtVlrFreteCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtVlrAdicCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtVlrICMSSTCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtVlrDescCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtVlrLiqCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtVlrTotalNota = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtVlrProdItCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtPrecoBaseProd = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtVlrBaseIPIItCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtPercIPIItCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, 2 );

	private JTextFieldPad txtVlrIPIItCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtCustoItCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtVlrII = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtAliqIPIFisc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, 5 );

	private JTextFieldPad txtAliqFisc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, 5 );

	private JTextFieldPad txtVlrBrutCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtSerieCompra = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtDocSerie = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 9, 0 ); // Tem que ter esse campo para não gerar N.de documento automático

	private JTextFieldPad txtDocCompra = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 9, 0 );

	private JTextFieldPad txtStatusCompra = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtTipoMov = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtDescFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldFK txtSiglaUFFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtEstFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtEmailFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldFK txtDescPlanoPag = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtDescNat = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtVenctoLote = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldFK txtDescAlmox = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodBarProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtCodFabProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldFK txtDescFisc = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodAlmoxItCompra = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtCodTran = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldFK txtRazTran = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtCNPJTran = new JTextFieldFK( JTextFieldPad.TP_STRING, 20, 0);

	private JTextFieldPad txtCodSol = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtIDUsu = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtDtEmitSolicitacao = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodCC = new JTextFieldPad( JTextFieldPad.TP_STRING, 19, 0 );

	private JTextFieldFK txtDescCC = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtAnoCC = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtChaveNfe = new JTextFieldPad( JTextFieldPad.TP_STRING, TAMANHOCHAVE, 0 );

	private JTextFieldPad txtCodModNota = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtEmitCompra = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtTipoModNota = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextAreaPad txaObsItCompra = new JTextAreaPad( 500 );

	private JTextFieldPad txtCodPaisDesembDI = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescPaisDesembDI = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtSiglaUFDesembDI = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtNomeUFDEsembDI = new JTextFieldFK( JTextFieldPad.TP_STRING, 80, 0 );

	private JTextFieldPad txtDI = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtDtRegDI = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDtDesembDI = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtIdentContainer = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtLocDesembDI = new JTextFieldPad( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtNumAcDraw = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtNAdicao = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 3, 0 );

	private JTextFieldPad txtSeqAdic = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 3, 0 );

	private JTextFieldPad txtDescDI = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtCodOrdCP = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtNroOrdemCompra = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldFK txtAceitaVenda = new JTextFieldFK( JTextFieldPad.TP_STRING, 1, 0 );

	private JCheckBoxPad cbChaveNFEValida = new JCheckBoxPad( "Chave Válida?", "S", "N" );

	private JRadioGroup<?, ?> rgTipoDocImp = null;

	private JLabelPad lbStatus = new JLabelPad();

	private JLabelPad lbCodLote = new JLabelPad();

	private JLabelPad lbNAdicao = new JLabelPad();

	private JLabelPad lbSeqAdic = new JLabelPad();

	private JLabelPad lbDescDI = new JLabelPad();

	private JLabelPad lbNumSerie = new JLabelPad();

	private JCheckBoxPad cbSeqNfTipoMov = new JCheckBoxPad( "Aloc.NF", "S", "N" );

	private ListaCampos lcTipoMov = new ListaCampos( this, "TM" );

	private ListaCampos lcImportacao = new ListaCampos( this, "IM" );

	private ListaCampos lcLFItCompra = new ListaCampos( this );

	private ListaCampos lcSerie = new ListaCampos( this, "SE" );

	private ListaCampos lcFor = new ListaCampos( this, "FR" );

	private ListaCampos lcPlanoPag = new ListaCampos( this, "PG" );

	private ListaCampos lcProd = new ListaCampos( this, "PD" );

	private ListaCampos lcProd2 = new ListaCampos( this, "PD" );

	private ListaCampos lcNat = new ListaCampos( this, "NT" );

	private ListaCampos lcLote = new ListaCampos( this, "LE" );

	private ListaCampos lcNumSerie = new ListaCampos( this, "NS" );

	private ListaCampos lcFisc = new ListaCampos( this );

	private ListaCampos lcCompra2 = new ListaCampos( this );

	private ListaCampos lcAlmoxItem = new ListaCampos( this, "AX" );

	private ListaCampos lcAlmoxProd = new ListaCampos( this, "AX" );

	private ListaCampos lcSolCompra = new ListaCampos( this, "SOL" );

	private ListaCampos lcModNota = new ListaCampos( this, "MN" );

	private final ListaCampos lcTran = new ListaCampos( this, "TN" );

	private ListaCampos lcPais = new ListaCampos( this, "" );

	private ListaCampos lcUF = new ListaCampos( this );

	private String sOrdNota = "";

	private boolean comref = false;

	private boolean buscagenericaprod = false;

	private boolean podeBloq = false;

	private boolean buscaVlrUltCompra = false;

	private boolean habconvcp = false;

	private boolean habilitaCusto = false;

	private boolean bloqprecoaprov = false;

	private boolean habcompracompl = false;

	private boolean npermitdtmaior = false;
	
	private String proceminfe = "3";

	private Integer codtipomovim = null;

	private String abaTransp = "N";

	private String abaSolCompra = "N";

	private String abaImport = "N";

	private String abaFisc = "S";

	private String classcp = "";

	private String labelobs01cp = "";

	private String labelobs02cp = "";

	private String labelobs03cp = "";

	private String labelobs04cp = "";

	private String bloqseqicp = "";

	private String utilordcpint = "";

	private String totcpsfrete = "";

	private String utilizatbcalcca = "";

	private String consistChaveNFE = "";

	private JTextAreaPad txaObs01 = new JTextAreaPad();

	private JTextAreaPad txaObs02 = new JTextAreaPad();

	private JTextAreaPad txaObs03 = new JTextAreaPad();

	private JTextAreaPad txaObs04 = new JTextAreaPad();

	private JTextAreaPad txaInfCompl = new JTextAreaPad();

	private JScrollPane spnObs01 = new JScrollPane( txaObs01 );

	private JScrollPane spnObs02 = new JScrollPane( txaObs02 );

	private JScrollPane spnObs03 = new JScrollPane( txaObs03 );

	private JScrollPane spnObs04 = new JScrollPane( txaObs04 );

	private JScrollPane spnInfCompl = new JScrollPane( txaInfCompl );

	private JButtonPad btBuscaCompra = new JButtonPad( "Pedido", Icone.novo( "btEntrada.png" ) );

	private JButtonPad btBuscaImportacao = new JButtonPad( "Importação", Icone.novo( "btImportacao.png" ) );

	private JButtonPad btBuscaCpComplementar = new JButtonPad( "Complementar", Icone.novo( "btExecuta.png" ) );

	private JLabelPad lbChaveNfe = null;

	private boolean novo = false;

	private JPanelPad pnAdicionalCab = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 4 ) );

	private NFEConnectionFactory nfecf = null;

	private NumSerie numserie = null;

	private CalcImpostos impostos = new CalcImpostos();

	private JTextFieldPad txtCodEmpIf = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodFilialIf = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldFK txtCodFisc = new JTextFieldFK( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtCodFiscIf = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtCodItFisc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtTicket = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	public static BigDecimal zero = new BigDecimal( 0 );

	public static BigDecimal cem = new BigDecimal( 100 );

	private DAOImportacao daoimp = null;

	private int codigoNfe = 55;

	private enum PROCEDUREOP {
		TIPOPROCESS, CODEMPOP, CODFILIALOP, CODOP, SEQOP, CODEMPPD, CODFILIALPD, CODPROD, CODEMPOC, CODFILIALOC, CODORC, TIPOORC, CODITORC, QTDSUGPRODOP, DTFABROP, SEQEST, CODEMPET, CODFILIALET, CODEST, AGRUPDATAAPROV, AGRUPDTFABROP, AGRUPCODCLI, CODEMPCL, CODFILIALCL, CODCLI, DATAAPROV, CODEMPCP, CODFILIALCP, CODCOMPRA, CODITCOMPRA, JUSTFICQTDPROD, CODEMPPDENTRADA, CODFILIALPDENTRADA, CODPRODENTRADA, QTDENTRADA
	}

	private enum ECOL_ITENS {
		CODITCOMPRA, CODPROD, DESCPROD, REFPROD
	}

	public FCompra() {

		nav.setNavigation( true );

		setTitulo( "Compra" );
		setAtribos( 15, 10, 815, 570 );

	}

	public void montaTela() {

		adicAbas();

		adicPaineis();

		adicListaCampos();

		adicToolTips();

		txtVlrIPICompra.setAtivo( false );
		txtVlrFreteCompra.setAtivo( false );
		txtVlrDescCompra.setAtivo( false );
		txtVlrAdicCompra.setAtivo( false );
		txtVlrLiqCompra.setAtivo( false );
		txtVlrBrutCompra.setAtivo( false );
		//btBuscaCpComplementar.setEnabled( false );
		txtVlrTotalNota.setEditable( false );
		txtAceitaVenda.setVlrString( "N" );
		Vector<String> vValsTipoDocImp = new Vector<String>();

		Vector<String> vLabsTipoDocImp = new Vector<String>();

		vValsTipoDocImp.addElement( "0" );
		vValsTipoDocImp.addElement( "1" );
		vLabsTipoDocImp.addElement( "DI" );
		vLabsTipoDocImp.addElement( "DSI" );
		rgTipoDocImp = new JRadioGroup<String, String>( 2, 1, vLabsTipoDocImp, vValsTipoDocImp );

		pinCab = new JPanelPad( 740, 130 );
		setListaCampos( lcCampos );
		setAltCab( 155 );
		setPainel( pinCabCompra );

		adicCampo( txtCodCompra, 7, 20, 80, 20, "CodCompra", "Nº Compra", ListaCampos.DB_PK, true );
		adicCampo( txtCodTipoMov, 90, 20, 77, 20, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_FK, txtDescTipoMov, true );
		adicDescFK( txtDescTipoMov, 170, 20, 247, 20, "DescTipoMov", "Descrição do tipo de movimento" );
		adicCampo( txtSerieCompra, 420, 20, 77, 20, "Serie", "Série", ListaCampos.DB_FK, true );
		adicCampo( txtDocCompra, 500, 20, 77, 20, "DocCompra", "Documento", ListaCampos.DB_SI, true );
		adicCampo( txtDtEmitCompra, 580, 20, 75, 20, "DtEmitCompra", "Dt.emissão", ListaCampos.DB_SI, true );
		adicCampo( txtDtEntCompra, 658, 20, 75, 20, "DtEntCompra", "Dt.entrada", ListaCampos.DB_SI, true );
		adicCampo( txtCodFor, 7, 60, 80, 20, "CodFor", "Cód.for.", ListaCampos.DB_FK, txtDescFor, true );
		adicDescFK( txtDescFor, 90, 60, 264, 20, "RazFor", "Razão social do fornecedor" );
		adicDescFK( txtSiglaUFFor, 357, 60, 20, 20, "UfFor", "UF" );
		adicCampo( txtCodPlanoPag, 380, 60, 60, 20, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_FK, txtDescPlanoPag, true );
		adicDescFK( txtDescPlanoPag, 443, 60, 195, 20, "DescPlanoPag", "Descrição do plano de pag." );
		if( "S".equals( utilordcpint ) ) {
			//Número ordem de compra Integrada.
			adicCampo( txtCodOrdCP, 640, 60, 94, 20, "CodOrdCP", "Nro.O.Compra I", ListaCampos.DB_SI, false );
		} else {
			//Número ordem de compra Digitada.
			adicCampo( txtNroOrdemCompra, 640, 60, 94, 20, "NroOrdemCompra", "Nro.O.Compra D", ListaCampos.DB_SI, false );
		}
		adicDBLiv( txaObs01, "Obs01", labelobs01cp == null ? "Observações" : labelobs01cp, false );
		adicDBLiv( txaObs02, "Obs02", labelobs01cp == null ? "Observações" : labelobs01cp, false );
		adicDBLiv( txaObs03, "Obs03", labelobs01cp == null ? "Observações" : labelobs01cp, false );
		adicDBLiv( txaObs04, "Obs04", labelobs01cp == null ? "Observações" : labelobs01cp, false );
		adicDBLiv( txaInfCompl, "InfCompl", "Informações complementares (fisco)", false );

		if ( abaTransp.equals( "S" ) ) {
			setListaCampos( lcCampos );
			setPainel( pinCabTransp );
			adicCampo( txtCodTran, 7, 25, 70, 20, "Codtran", "Cód.transp.", ListaCampos.DB_FK, false );
			adicDescFK( txtRazTran, 80, 25, 250, 20, "Raztran", "Razão social da transportadora" );
		}

		if ( "S".equals( abaImport ) ) {
			setAltCab( 195 );
			setPainel( pinCabImportacao );

			adicCampo( txtCodImp, 7, 25, 80, 20, "CodImp", "Cod.Imp.", ListaCampos.DB_FK, false );

			adicDB( rgTipoDocImp, 7, 65, 97, 61, "TipoDocImp", "Tipo Documento", false );

			adicCampo( txtDI, 107, 65, 85, 20, "NroDI", "Nro. da DI", ListaCampos.DB_SI, false );
			adicCampo( txtDtRegDI, 195, 65, 70, 20, "DtRegDI", "Dt.reg. DI", ListaCampos.DB_SI, false );
			adicCampo( txtDtDesembDI, 268, 65, 70, 20, "DtDesembDI", "Dt.desemb.", ListaCampos.DB_SI, false );
			adicCampo( txtIdentContainer, 341, 65, 150, 20, "IdentContainer", "Identificação do container", ListaCampos.DB_SI, false );

			adicCampo( txtCodPaisDesembDI, 494, 65, 70, 20, "CodPaisDesembDI", "Cod.país", ListaCampos.DB_FK, txtDescPaisDesembDI, false );
			adicDescFK( txtDescPaisDesembDI, 567, 65, 170, 20, "NomePais", "Nome do país" );

			adicCampo( txtLocDesembDI, 107, 105, 231, 20, "LocDesembDI", "Local do desembaraço", ListaCampos.DB_SI, false );

			adicCampo( txtNumAcDraw, 341, 105, 150, 20, "NumAcDraw", "Ato (DrawBack)", ListaCampos.DB_SI, false );

			adicCampo( txtSiglaUFDesembDI, 494, 105, 70, 20, "SiglaUfDesembDI", "Sigla UF", ListaCampos.DB_FK, txtNomeUFDEsembDI, false );
			adicDescFK( txtNomeUFDEsembDI, 567, 105, 170, 20, "NomeUF", "Nome UF" );

		}

		if ( abaFisc.equals( "S" ) ) {
			setListaCampos( lcCampos );
			setPainel( pinCabFiscal );

			adicCampo( txtChaveNfe, 7, 20, 410, 20, "ChaveNfeCompra", "Chave de acesso NFe", ListaCampos.DB_SI, false );
		}

		adicCampoInvisivel( txtCalcTrib, "CalcTrib", "Calculo de tributos", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtStatusCompra, "StatusCompra", "Status", ListaCampos.DB_SI, false );

		adicCampoInvisivel( txtTicket, "ticket", "Ticket", ListaCampos.DB_SI, false );

		adicDB( cbChaveNFEValida, 420, 20, 200, 15, "ChaveNFEValida", "",  ListaCampos.DB_SI, false);
		cbChaveNFEValida.setEnabled( false );

		setListaCampos( true, "COMPRA", "CP" );
		lcCampos.setQueryInsert( false );

		if ( "S".equals( abaSolCompra ) ) {
			setListaCampos( lcSolCompra );
			setPainel( pinCabSolCompra );
			adicCampo( txtCodSol, 7, 25, 70, 20, "CodSol", "Cód.sol.", ListaCampos.DB_FK, false );
			adicCampo( txtIDUsu, 451, 20, 80, 20, "IdUsu", "Id do usuário", ListaCampos.DB_FK, true );
			adicCampo( txtDtEmitSolicitacao, 539, 20, 86, 20, "DtEmitSol", "Data da Sol.", ListaCampos.DB_SI, true );
			adicDescFKInvisivel( txtDescCC, "DescCC", "Descrição do centro de custos" );
			adicCampo( txtCodCC, 80, 20, 130, 20, "CodCC", "Cód.CC.", ListaCampos.DB_FK, txtDescCC, true );
			adicCampo( txtAnoCC, 213, 20, 70, 20, "AnoCC", "Ano CC.", ListaCampos.DB_FK, true );
			adicDescFK( txtDescCC, 286, 20, 162, 20, "DescCC", "Descrição do centro de custos" );

			txtCodSol.setNaoEditavel( true );
			txtIDUsu.setNaoEditavel( true );
			txtDtEmitSolicitacao.setNaoEditavel( true );
			txtDescCC.setNaoEditavel( true );
			txtCodCC.setNaoEditavel( true );
			txtAnoCC.setNaoEditavel( true );
		}

		adicListeners();

		setImprimir( true );
	}

	private void adicToolTips() {

		btFechaCompra.setToolTipText( "Fechar a Compra (F4)" );
		txtDescProd.setToolTipText( "Clique aqui duas vezes para alterar a descrição." );
	}

	private void adicListaCampos() {

		lcTipoMov.add( new GuardaCampo( txtCodTipoMov, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false ) );
		lcTipoMov.add( new GuardaCampo( txtDescTipoMov, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false ) );
		lcTipoMov.add( new GuardaCampo( txtCodModNota, "CodModNota", "Código do modelo de nota", ListaCampos.DB_FK, false ) );
		lcTipoMov.add( new GuardaCampo( txtEmitCompra, "EmitNFCpMov", "Emite NF", ListaCampos.DB_SI, false ) );
		lcTipoMov.add( new GuardaCampo( txtTipoMov, "TipoMov", "Tipo mov.", ListaCampos.DB_SI, false ) );
		lcTipoMov.add( new GuardaCampo( cbSeqNfTipoMov, "SeqNfTipomov", "Aloc.NF", ListaCampos.DB_SI, true ) );
		lcTipoMov.add( new GuardaCampo( txtSerieCompra, "Serie", "Série", ListaCampos.DB_FK, false ) );
		lcTipoMov.add( new GuardaCampo( txtDesBloqCV, "DesBloqCV", "Desabilita Bloqueio Compra/Venda", ListaCampos.DB_SI, false ) );	
		lcTipoMov.setWhereAdic( "((ESTIPOMOV = 'E') AND" + " ( TUSUTIPOMOV='S' OR	EXISTS (SELECT * FROM EQTIPOMOVUSU TU " + "WHERE TU.CODEMP=EQTIPOMOV.CODEMP AND TU.CODFILIAL=EQTIPOMOV.CODFILIAL AND " + "TU.CODTIPOMOV=EQTIPOMOV.CODTIPOMOV AND TU.CODEMPUS=" + Aplicativo.iCodEmp
				+ " AND TU.CODFILIALUS=" + ListaCampos.getMasterFilial( "SGUSUARIO" ) + " AND TU.IDUSU='" + Aplicativo.getUsuario().getIdusu() + "') ) )" );
		lcTipoMov.montaSql( false, "TIPOMOV", "EQ" );
		lcTipoMov.setQueryCommit( false );
		lcTipoMov.setReadOnly( true );
		txtCodTipoMov.setTabelaExterna( lcTipoMov, FTipoMov.class.getCanonicalName() );

		lcSerie.add( new GuardaCampo( txtSerieCompra, "Serie", "Série", ListaCampos.DB_PK, false ) );
		lcSerie.add( new GuardaCampo( txtDocSerie, "DocSerie", "Documento", ListaCampos.DB_SI, false ) );
		lcSerie.montaSql( false, "SERIE", "LF" );
		lcSerie.setQueryCommit( false );
		lcSerie.setReadOnly( true );
		txtSerieCompra.setTabelaExterna( lcSerie, FSerie.class.getCanonicalName() );

		lcImportacao.add( new GuardaCampo( txtCodImp, "CodImp", "Cód.Imp.", ListaCampos.DB_PK, false ) );
		lcImportacao.add( new GuardaCampo( txtCodForImp, "CodFor", "Cód.For.", ListaCampos.DB_SI, false ) );
		lcImportacao.add( new GuardaCampo( txtCodPlanoPagImp, "CodPlanoPag", "Cód.P.Pag.", ListaCampos.DB_SI, false ) );
		lcImportacao.add( new GuardaCampo( txtDI, "DI", "DI", ListaCampos.DB_SI, false ) );
		lcImportacao.add( new GuardaCampo( txtDtRegDI, "DtRegDI", "Dt.Reg.DI", ListaCampos.DB_SI, false ) );
		lcImportacao.add( new GuardaCampo( txtDtDesembDI, "DtDesembDI", "Dt.Desemb.DI", ListaCampos.DB_SI, false ) );
		lcImportacao.add( new GuardaCampo( txtIdentContainer, "IdentContainer", "Ident.Container", ListaCampos.DB_SI, false ) );
		lcImportacao.add( new GuardaCampo( txtLocDesembDI, "LocDesembDI", "Local.Desemb.DI", ListaCampos.DB_SI, false ) );

		lcImportacao.montaSql( false, "IMPORTACAO", "CP" );
		lcImportacao.setReadOnly( true );

		txtCodImp.setTabelaExterna( lcImportacao, null );
		txtCodImp.setFK( true );
		txtCodImp.setNomeCampo( "CodImp" );

		lcFor.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.for.", ListaCampos.DB_PK, false ) );
		lcFor.add( new GuardaCampo( txtDescFor, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );
		lcFor.add( new GuardaCampo( txtSiglaUFFor, "UfFor", "UF", ListaCampos.DB_SI, false ) );
		lcFor.add( new GuardaCampo( txtCNPJFor, "CnpjFor", "CNPJ", ListaCampos.DB_SI, false ) );
		lcFor.add( new GuardaCampo( txtEstFor, "UFFor", "UF", ListaCampos.DB_SI, false ) );
		lcFor.add( new GuardaCampo( txtEmailFor, "EmailFor", "Email", ListaCampos.DB_SI, false ) );

		lcFor.montaSql( false, "FORNECED", "CP" );
		lcFor.setQueryCommit( false );
		lcFor.setReadOnly( true );
		txtCodFor.setTabelaExterna( lcFor, FFornecedor.class.getCanonicalName() );

		lcPlanoPag.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_PK, false ) );
		lcPlanoPag.add( new GuardaCampo( txtDescPlanoPag, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPag.setWhereAdic( "ATIVOPLANOPAG='S' AND CVPLANOPAG IN ('C','A')" );
		lcPlanoPag.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag.setQueryCommit( false );
		lcPlanoPag.setReadOnly( true );
		txtCodPlanoPag.setTabelaExterna( lcPlanoPag, FPlanoPag.class.getCanonicalName() );

		lcTran.add( new GuardaCampo( txtCodTran, "CodTran", "Cód.tran.", ListaCampos.DB_PK, false ) );
		lcTran.add( new GuardaCampo( txtRazTran, "RazTran", "Razão social da transportadora", ListaCampos.DB_SI, false ) );
		lcTran.add( new GuardaCampo( txtCNPJTran, "CNPJTran", "CNPJ da transportadora", ListaCampos.DB_SI, false ) );
		lcTran.montaSql( false, "TRANSP", "VD" );
		lcTran.setQueryCommit( false );
		lcTran.setReadOnly( true );
		txtCodTran.setTabelaExterna( lcTran, FTransp.class.getCanonicalName() );

		lcSolCompra.add( new GuardaCampo( txtCodSol, "CodSol", "Cód.sol.", ListaCampos.DB_PK, false ) );
		lcSolCompra.add( new GuardaCampo( txtIDUsu, "IDUsu", "Cód.Usu.", ListaCampos.DB_SI, false ) );
		lcSolCompra.add( new GuardaCampo( txtDtEmitSolicitacao, "Dt.Emit.Solicitacao", "Razão social da transportadora", ListaCampos.DB_SI, false ) );
		lcSolCompra.add( new GuardaCampo( txtCodCC, "CodCC", "Cód.CC", ListaCampos.DB_SI, false ) );
		lcSolCompra.add( new GuardaCampo( txtDescCC, "DescCC", "Desc.CC", ListaCampos.DB_SI, false ) );
		lcSolCompra.add( new GuardaCampo( txtAnoCC, "AnoCC", "Ano.CC", ListaCampos.DB_SI, false ) );
		lcSolCompra.montaSql( false, "SOLICITACAO", "CP" );
		lcSolCompra.setQueryCommit( false );
		lcSolCompra.setReadOnly( true );
		txtCodSol.setTabelaExterna( lcSolCompra, FSolicitacaoCompra.class.getCanonicalName() );

		lcFisc.add( new GuardaCampo( txtCodFisc, "CodFisc", "Código", ListaCampos.DB_PK, false ) );
		lcFisc.add( new GuardaCampo( txtDescFisc, "DescFisc", "Descrição", ListaCampos.DB_SI, false ) );
		lcFisc.add( new GuardaCampo( txtTipoFisc, "TipoFisc", "Tipo", ListaCampos.DB_SI, false ) );
		lcFisc.add( new GuardaCampo( txtTpRedIcmsFisc, "TpRedIcmsFisc", "Tp.red.", ListaCampos.DB_SI, false ) );
		lcFisc.add( new GuardaCampo( txtRedFisc, "RedFisc", "Redução", ListaCampos.DB_SI, false ) );
		lcFisc.add( new GuardaCampo( txtAliqFisc, "AliqFisc", "% ICMS", ListaCampos.DB_SI, false ) );
		lcFisc.add( new GuardaCampo( txtAliqIPIFisc, "AliqIPIFisc", "% IPI", ListaCampos.DB_SI, false ) );
		lcFisc.montaSql( false, "CLFISCAL", "LF" );
		lcFisc.setQueryCommit( false );
		lcFisc.setReadOnly( true );
		txtCodFisc.setTabelaExterna( lcFisc, FCLFiscal.class.getCanonicalName() );
		txtDescFisc.setListaCampos( lcFisc );

		lcProd.add( new GuardaCampo( txtCodProd, "codprod", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcProd.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtRefProd, "RefProd", "Referência", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtCodFabProd, "CodFabProd", "Cod.Fabricante", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtCodBarProd, "CodBarProd", "Cod.Barra", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtCodFisc, "CodFisc", "Cod.Fiscal", ListaCampos.DB_FK, false ) );
		lcProd.add( new GuardaCampo( txtCodUnid, "CodUnid", "Unidade", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtCodAlmoxProd, "CodAlmox", "Unidade", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtCLoteProd, "CLoteProd", "C/Lote", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtSerieProd, "SerieProd", "C/Série", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtPrecoBaseProd, "PrecoBaseProd", "Preco Base", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtTipoProd, "TipoProd", "Tipo de produto", ListaCampos.DB_SI, false ) );

		txtCodUnid.setAtivo( false );
		//lcProd.setWhereAdic( "ATIVOPROD='S'" );

		//lcProd.setWhereAdic( " ATIVOPROD='S' AND CVPROD in ('C','A')" );
		lcProd.setDinWhereAdic( "ATIVOPROD='S' AND CVPROD in ('C','A', '#N') ", txtAceitaVenda );
		lcProd.montaSql( false, "PRODUTO", "EQ" );
		lcProd.setQueryCommit( false );
		lcProd.setReadOnly( true );
		txtCodProd.setTabelaExterna( lcProd, FProduto.class.getCanonicalName() );

		lcProd2.add( new GuardaCampo( txtRefProd, "RefProd", "Referência", ListaCampos.DB_PK, false ) );
		lcProd2.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtCodProd, "codprod", "Cód.prod.", ListaCampos.DB_SI, false ) );

		lcProd2.add( new GuardaCampo( txtCodFabProd, "CodFabProd", "Cod.Fabricante", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtCodBarProd, "CodBarProd", "Cod.Barras", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtCodFisc, "CodFisc", "Cód.Fisc.", ListaCampos.DB_FK, false ) );

		lcProd2.add( new GuardaCampo( txtCodUnid, "CodUnid", "Unidade", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtCodAlmoxProd, "CodAlmox", "Unidade", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtCLoteProd, "CLoteProd", "C/Lote", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtSerieProd, "SerieProd", "C/Série", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtTipoProd, "TipoProd", "Tipo de produto", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtPrecoBaseProd, "PrecoBaseProd", "Preco Base", ListaCampos.DB_SI, false ) );

		txtRefProd.setNomeCampo( "RefProd" );
		txtRefProd.setListaCampos( lcDet );
		lcProd2.setWhereAdic( "ATIVOPROD='S'" );
		lcProd2.montaSql( false, "PRODUTO", "EQ" );
		lcProd2.setQueryCommit( false );
		lcProd2.setReadOnly( true );
		txtRefProd.setTabelaExterna( lcProd2, FProduto.class.getCanonicalName() );

		// FK do Lote

		lcLote.add( new GuardaCampo( txtCodLote, "CodLote", "Cód.lote", ListaCampos.DB_PK, false ) );
		lcLote.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcLote.add( new GuardaCampo( txtVenctoLote, "VenctoLote", "Vencimento", ListaCampos.DB_SI, false ) );
		lcLote.setDinWhereAdic( "CODPROD=#N", txtCodProd );
		lcLote.setAutoLimpaPK( false );
		lcLote.montaSql( false, "LOTE", "EQ" );
		lcLote.setQueryCommit( false );
		lcLote.setReadOnly( true );
		txtCodLote.setTabelaExterna( lcLote, null );
		txtVenctoLote.setListaCampos( lcLote );
		txtVenctoLote.setNomeCampo( "VenctoLote" );
		txtVenctoLote.setLabel( "Vencimento" );

		// FK do número de série

		lcNumSerie.add( new GuardaCampo( txtNumSerie, "NumSerie", "Num.Série", ListaCampos.DB_PK, false ) );
		lcNumSerie.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcNumSerie.add( new GuardaCampo( txtObsSerie, "ObsSerie", "Observações", ListaCampos.DB_SI, false ) );
		lcNumSerie.setDinWhereAdic( "CODPROD=#N", txtCodProd );
		lcNumSerie.setAutoLimpaPK( false );
		lcNumSerie.montaSql( false, "SERIE", "EQ" );
		lcNumSerie.setQueryCommit( false );
		lcNumSerie.setReadOnly( true );
		txtNumSerie.setTabelaExterna( lcNumSerie, FSerie.class.getCanonicalName() );
		txtObsSerie.setListaCampos( lcNumSerie );
		txtObsSerie.setNomeCampo( "ObsSerie" );
		txtObsSerie.setLabel( "Observações" );

		// FK de Almoxarifado Produto

		lcAlmoxProd.add( new GuardaCampo( txtCodAlmoxProd, "codalmox", "Cod.Almox.", ListaCampos.DB_PK, false ) );
		lcAlmoxProd.montaSql( false, "ALMOX", "EQ" );
		lcAlmoxProd.setQueryCommit( false );
		lcAlmoxProd.setReadOnly( true );
		txtCodAlmoxItCompra.setTabelaExterna( lcAlmoxProd, FAlmox.class.getCanonicalName() );

		// FK de Almoxarifado Item

		lcAlmoxItem.add( new GuardaCampo( txtCodAlmoxItCompra, "codalmox", "Cod.Almox.", ListaCampos.DB_PK, false ) );
		lcAlmoxItem.montaSql( false, "ALMOX", "EQ" );
		lcAlmoxItem.setQueryCommit( false );
		lcAlmoxItem.setReadOnly( true );
		txtCodAlmoxItCompra.setTabelaExterna( lcAlmoxItem, FAlmox.class.getCanonicalName() );

		lcNat.add( new GuardaCampo( txtCodNat, "CodNat", "CFOP", ListaCampos.DB_PK, false ) );
		lcNat.add( new GuardaCampo( txtDescNat, "DescNat", "Descrição da CFOP", ListaCampos.DB_SI, false ) );
		lcNat.montaSql( false, "NATOPER", "LF" );
		lcNat.setQueryCommit( false );
		lcNat.setReadOnly( true );
		txtCodNat.setTabelaExterna( lcNat, FNatoPer.class.getCanonicalName() );
		txtDescNat.setListaCampos( lcNat );

		lcCompra2.add( new GuardaCampo( txtCodCompra, "CodCompra", "Código", ListaCampos.DB_PK, false ) );
		lcCompra2.add( new GuardaCampo( txtVlrIPICompra, "VlrIPICompra", "IPI", ListaCampos.DB_SI, false ) );
		lcCompra2.add( new GuardaCampo( txtVlrFreteCompra, "VlrFreteCompra", "Frete", ListaCampos.DB_SI, false ) );
		lcCompra2.add( new GuardaCampo( txtVlrDescCompra, "VlrDescCompra", "Desconto", ListaCampos.DB_SI, false ) );
		lcCompra2.add( new GuardaCampo( txtVlrDescItCompra, "VlrDescItCompra", "Desconto dos itens", ListaCampos.DB_SI, false ) );
		lcCompra2.add( new GuardaCampo( txtVlrLiqCompra, "VlrLiqCompra", "Geral", ListaCampos.DB_SI, false ) );
		lcCompra2.add( new GuardaCampo( txtVlrBrutCompra, "VlrProdCompra", "Geral", ListaCampos.DB_SI, false ) );
		lcCompra2.add( new GuardaCampo( txtVlrICMSSTCompra, "VlrICMSSTCompra", "ICMS ST", ListaCampos.DB_SI, false ) );
		lcCompra2.add( new GuardaCampo( txtVlrAdicCompra, "VlrAdicCompra", "Valor adicional", ListaCampos.DB_SI, false ) );

		lcCompra2.montaSql( false, "COMPRA", "CP" );
		lcCompra2.setQueryCommit( false );
		lcCompra2.setReadOnly( true );

		lcModNota.add( new GuardaCampo( txtCodModNota, "CodModNota", "Cód.Mod.Nota", ListaCampos.DB_PK, false ) );
		lcModNota.add( new GuardaCampo( txtTipoModNota, "TipoModNota", "Tipo. Mod. nota", ListaCampos.DB_SI, false ) );
		lcModNota.montaSql( false, "MODNOTA", "LF" );
		lcModNota.setQueryCommit( false );
		lcModNota.setReadOnly( true );
		txtCodModNota.setTabelaExterna( lcModNota, FModNota.class.getCanonicalName() );
		txtTipoModNota.setListaCampos( lcModNota );

		lcPais.setUsaME( false );
		lcPais.add( new GuardaCampo( txtCodPaisDesembDI, "CodPais", "Cod.país.", ListaCampos.DB_PK, false ) );
		lcPais.add( new GuardaCampo( txtDescPaisDesembDI, "NomePais", "Nome", ListaCampos.DB_SI, false ) );
		lcPais.montaSql( false, "PAIS", "SG" );
		lcPais.setQueryCommit( false );
		lcPais.setReadOnly( true );
		txtCodPaisDesembDI.setTabelaExterna( lcPais, FPais.class.getCanonicalName() );

		lcUF.setUsaME( false );
		lcUF.add( new GuardaCampo( txtSiglaUFDesembDI, "SiglaUf", "Sigla", ListaCampos.DB_PK, false ) );
		lcUF.add( new GuardaCampo( txtNomeUFDEsembDI, "NomeUf", "Nome", ListaCampos.DB_SI, false ) );
		lcUF.setDinWhereAdic( "CODPAIS = #N", txtCodPaisDesembDI );
		lcUF.montaSql( false, "UF", "SG" );
		lcUF.setQueryCommit( false );
		lcUF.setReadOnly( true );
		txtSiglaUFDesembDI.setTabelaExterna( lcUF, FUF.class.getCanonicalName() );

		lcLFItCompra.add( new GuardaCampo( txtCodCompra, "CodCompra", "Cód.Compra.", ListaCampos.DB_PF, false ) );
		lcLFItCompra.add( new GuardaCampo( txtCodItCompra, "CodItCompra", "Cód.it.Compra.", ListaCampos.DB_PF, false ) );
		lcLFItCompra.add( new GuardaCampo( txtVlrPISItCompra, "VLRPIS", "VLRPIS", ListaCampos.DB_SI, false ) );
		lcLFItCompra.add( new GuardaCampo( txtVlrCOFINSItCompra, "VLRCOFINS", "VLRCOFINS", ListaCampos.DB_SI, false ) );
		lcLFItCompra.montaSql( false, "ITCOMPRA", "LF" );
		//lcLFItCompra.setMaster( lcDet );
		lcLFItCompra.setReadOnly( true );

	}

	private void adicPaineis() {

		pnNavCab.add( pnAdicionalCab, BorderLayout.EAST );

		btBuscaRemessa.setVisible( false );
		btBuscaCpComplementar.setVisible( habcompracompl );

		desabilitaBotoes( false );

		btBuscaRemessa.setPreferredSize( new Dimension( 118, 0 ) );
		btBuscaCpComplementar.setPreferredSize( new Dimension( 130, 0 ) );
		btBuscaCompra.setPreferredSize( new Dimension( 118, 0 ) );
		btBuscaImportacao.setPreferredSize( new Dimension( 118, 0 ) );

		btBuscaRemessa.setFont( SwingParams.getFontpadmed() );
		btBuscaCpComplementar.setFont( SwingParams.getFontpadmed() );
		btBuscaCompra.setFont( SwingParams.getFontpadmed() );
		btBuscaImportacao.setFont( SwingParams.getFontpadmed() );

		pnAdicionalCab.add( btBuscaRemessa );
		pnAdicionalCab.add( btBuscaCompra );
		pnAdicionalCab.add( btBuscaImportacao );
		pnAdicionalCab.add( btBuscaCpComplementar );


		lbStatus.setForeground( Color.WHITE );
		lbStatus.setBackground( Color.BLACK );
		lbStatus.setFont( SwingParams.getFontboldmed() );
		lbStatus.setHorizontalAlignment( SwingConstants.CENTER );
		lbStatus.setOpaque( true );
		lbStatus.setText( "NÃO SALVO" );

		JPanelPad navEast = new JPanelPad();
		navEast.setPreferredSize( new Dimension( 116, 30 ) );
		navEast.adic( lbStatus, 3, 3, 116, 20 );
		navEast.tiraBorda();
		pnAdicionalCab.add( navEast );
		// pnAdicionalCab.add( lbStatus );
	
		pnMaster.remove( 2 ); // Remove o JPanelPad predefinido da class FDados
		pnGImp.removeAll(); // Remove os botões de impressão para adicionar logo embaixo
		pnGImp.setLayout( new GridLayout( 1, 5 ) ); // redimensiona o painel de impressão
		pnGImp.setPreferredSize( new Dimension( 280, 26 ) );
		pnGImp.add( btPrevimp );
		pnGImp.add( btImp );
		pnGImp.add( btFechaCompra );
		pnGImp.add( btObs );


		pnTot.setPreferredSize( new Dimension( 140, 200 ) );
		pnTot.add( pinTot );
		pnCenter.add( pnTot, BorderLayout.EAST );
		pnCenter.add( spTab, BorderLayout.CENTER );

		JPanelPad pnLab = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );
		pnLab.add( new JLabelPad( " Totais:" ) );

		pnMaster.add( pnCenter, BorderLayout.CENTER );
	}

	private void adicAbas() {

		pnCliCab.add( tpnCab );
		tpnCab.addTab( "Compra", pinCabCompra );

		if ( "S".equals( abaTransp ) ) {
			tpnCab.addTab( "Transportadora", pinCabTransp );
		}
		if ( "S".equals( abaSolCompra ) ) {
			tpnCab.addTab( "Solicitação de Compra", pinCabSolCompra );
		}
		if ( "S".equals( abaImport ) ) {
			tpnCab.addTab( "Importação", pinCabImportacao );
		}
		if ( "S".equals( abaFisc ) ) {
			tpnCab.addTab( "Fiscal", pinCabFiscal );
		}
		if ( labelobs01cp != null && !"".equals( labelobs01cp.trim() ) ) {
			pinCabObs01.add( spnObs01 );
			tpnCab.addTab( labelobs01cp.trim(), pinCabObs01 );
		}
		if ( labelobs02cp != null && !"".equals( labelobs02cp.trim() ) ) {
			pinCabObs02.add( spnObs02 );
			tpnCab.addTab( labelobs02cp.trim(), pinCabObs02 );
		}
		if ( labelobs03cp != null && !"".equals( labelobs03cp.trim() ) ) {
			pinCabObs03.add( spnObs03 );
			tpnCab.addTab( labelobs03cp.trim(), pinCabObs03 );
		}
		if ( labelobs04cp != null && !"".equals( labelobs04cp.trim() ) ) {
			pinCabObs04.add( spnObs04 );
			tpnCab.addTab( labelobs04cp.trim(), pinCabObs04 );
		}

		pinCabInfCompl.add( spnInfCompl );
		tpnCab.addTab( "Inf.Compl (Fisco)", pinCabInfCompl );

	}

	private void adicListeners() {

		// Mouse Listeners

		txtDescProd.addMouseListener( this );

		// Action Listeners

		btFechaCompra.addActionListener( this );
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		btObs.addActionListener( this );
		btBuscaRemessa.addActionListener( this );
		btBuscaCompra.addActionListener( this );
		btBuscaImportacao.addActionListener( this );
		btBuscaCpComplementar.addActionListener( this );
		nav.btCancelar.addActionListener( this );
		// Focus Listeners

		txtPercDescItCompra.addFocusListener( this );
		txtPercComItCompra.addFocusListener( this );
		txtVlrDescItCompra.addFocusListener( this );
		txtQtdItCompra.addFocusListener( this );
		txtCodNat.addFocusListener( this );
		txtPrecoItCompra.addFocusListener( this );
		txtPercICMSItCompra.addFocusListener( this );
		txtVlrIPIItCompra.addFocusListener( this );
		txtVlrBaseICMSItCompra.addFocusListener( this );
		txtNAdicao.addFocusListener( this );
		txtCodLote.addFocusListener( this );
		txtNumSerie.addFocusListener( this );
		txtVlrICMSStItCompra.addFocusListener( this );
		txtPercICMSStItCompra.addFocusListener( this );
		txtAliqISSItCompra.addFocusListener( this );
		txtVlrII.addFocusListener( this );

		// Key Listeners

		txtNroOrdemCompra.addKeyListener( this );

		// Carrega Listeners

		lcCampos.addCarregaListener( this );
		lcFor.addCarregaListener( this );
		lcSerie.addCarregaListener( this );
		lcProd.addCarregaListener( this );
		lcProd2.addCarregaListener( this );
		lcFisc.addCarregaListener( this );
		lcNat.addCarregaListener( this );
		lcLote.addCarregaListener( this );
		lcDet.addCarregaListener( this );
		lcTipoMov.addCarregaListener( this );
		lcAlmoxProd.addCarregaListener( this );
		lcModNota.addCarregaListener( this );
		lcCompra2.addCarregaListener( this );

		// Insert Listeners
		lcCampos.addInsertListener( this );

		// Post Listeners
		lcCampos.addPostListener( this );
		lcDet.addPostListener( this );
		lcDet.addInsertListener( this );

	}

	private void redimensionaDet( int alt ) {

		setAltDet( alt );
		pinDet.setPreferredSize( new Dimension( 740, alt ) );

	}

	private void adicionaAbas() {

		pnDet.add( tpnAbas );

		tpnAbas.setTabLayoutPolicy( JTabbedPanePad.SCROLL_TAB_LAYOUT );

		tpnAbas.setPreferredSize( new Dimension( 600, 30 ) );

		tpnAbas.setTabPlacement( SwingConstants.BOTTOM );

		tpnAbas.addTab( "Geral", pnGeralDet );

		tpnAbas.addTab( "ICMS/IPI", pnIpiIcms );

		tpnAbas.addTab( "Importação/Custos", pnImpDet );

	}

	private void montaDetalhe() {

		redimensionaDet( 140 );

		adicionaAbas();

		setListaCampos( lcDet );

		lcDet.setOrdem( "coditcompra" );

		setPainel( pnGeralDet );
		setNavegador( navRod );

		adicCampo( txtCodItCompra, 7, 20, 45, 20, "CodItCompra", "Item", ListaCampos.DB_PK, true );

		if ( comref ) {
			txtRefProd.setBuscaAdic( new DLBuscaProd( con, "REFPROD", lcProd2.getWhereAdic() ) );
			adicCampoInvisivel( txtCodProd, "CodProd", "Cód.Prod.", ListaCampos.DB_FK, txtDescProd, false );
			adicCampoInvisivel( txtRefProd, "RefProd", "Referência", ListaCampos.DB_FK, false );

			adic( new JLabelPad( "Referência" ), 55, 0, 70, 20 );
			adic( txtRefProd, 55, 20, 70, 20 );
			txtRefProd.setFK( true );
		}
		else {
			txtCodProd.setBuscaAdic( new DLBuscaProd( con, "CODPROD", lcProd.getWhereAdic() ) );
			adicCampo( txtCodProd, 55, 20, 70, 20, "CodProd", "Cód.prod.", ListaCampos.DB_FK, txtDescProd, false );
		}


		adicDescFK( txtDescProd, 128, 20, 220, 20, "DescProd", "Descrição do produto" );
		adic( new JLabelPad( "Unid." ), 351, 0, 50, 20 );

		adic( txtCodUnid, 351, 20, 50, 20 );
		adicCampo( txtQtdItCompra, 404, 20, 70, 20, "qtditcompra", "Quant.", ListaCampos.DB_SI, true );

		adicCampoInvisivel( txtCodAlmoxItCompra, "codalmox", "Cod.Almox", ListaCampos.DB_FK, false );

		txtQtdItCompra.setBuscaAdic( new DLBuscaEstoq( lcDet, lcAlmoxItem, lcProd, con, "qtditcompra" ) );

		adicCampo( txtPrecoItCompra, 477, 20, 70, 20, "PrecoItCompra", "Preço", ListaCampos.DB_SI, true );

		adicCampo( txtPercDescItCompra, 550, 20, 50, 20, "PercDescItCompra", "% Desc.", ListaCampos.DB_SI, false );
		adicCampo( txtVlrDescItCompra, 603, 20, 70, 20, "VlrDescItCompra", "Vlr.Desc.", ListaCampos.DB_SI, false );

		adicCampo( txtVlrLiqItCompra, 676, 20, 90, 20, "VlrLiqItCompra", "Valor Item", ListaCampos.DB_SI, false );

		adicCampo( txtCodNat, 7, 60, 45, 20, "CodNat", "CFOP", ListaCampos.DB_FK, txtDescNat, true );
		adicDescFK( txtDescNat, 55, 60, 294, 20, "DescNat", "Descrição da CFOP" );


		lbCodLote = adicCampo( txtCodLote, 351, 60, 117, 20, "CodLote", "Lote", ListaCampos.DB_FK, txtVenctoLote, false );
		lbNumSerie = adicCampo( txtNumSerie, 471, 60, 150, 20, "NumSerieTmp", "Número de série", ListaCampos.DB_FK, txtObsSerie, false );


		setPainel( pnIpiIcms );

		adicCampo( txtAliqISSItCompra, 7, 20, 35, 20, "AliqISSItCompra", "% ISS", ListaCampos.DB_SI, false );
		adicCampo( txtVlrISSItCompra, 45, 20, 58, 20, "VlrISSItCompra", "Vlr. ISS", ListaCampos.DB_SI, false );

		adicCampo( txtVlrBaseICMSItCompra, 106, 20, 75, 20, "VlrBaseICMSItCompra", "B. ICMS", ListaCampos.DB_SI, false );
		adicCampo( txtPercICMSItCompra, 184, 20, 70, 20, "PercICMSItCompra", "% ICMS", ListaCampos.DB_SI, false );
		adicCampo( txtVlrICMSItCompra, 257, 20, 70, 20, "VlrICMSItCompra", "Vlr. ICMS", ListaCampos.DB_SI, false );

		adicCampo( txtVlrBaseICMSStItCompra, 330, 20, 70, 20, "VlrBaseICMSStItCompra", "B. ICMS ST", ListaCampos.DB_SI, false );
		adicCampo( txtPercICMSStItCompra, 403, 20, 70, 20, "PercICMSStItCompra", "% ICMS ST", ListaCampos.DB_SI, false );
		adicCampo( txtVlrICMSStItCompra, 476, 20, 67, 20, "VlrICMSStItCompra", "Vlr.ICMS ST", ListaCampos.DB_SI, false );

		adicCampo( txtVlrBaseIPIItCompra, 546, 20, 70, 20, "VlrBaseIPIItCompra", "B. IPI", ListaCampos.DB_SI, false );
		adicCampo( txtPercIPIItCompra, 619, 20, 50, 20, "PercIPIItCompra", "% IPI", ListaCampos.DB_SI, false );
		adicCampo( txtVlrIPIItCompra, 672, 20, 70, 20, "VlrIPIItCompra", "Vlr. IPI", ListaCampos.DB_SI, false );

		adicCampoInvisivel( txtVlrProdItCompra, "VlrProdItCompra", "V. Bruto", ListaCampos.DB_SI, false );


		adicCampoInvisivel( txtCodEmpIf, "codempif", "Cod.emp.it.fiscal.", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtCodFilialIf, "codfilialif", "Cod.filial it.fiscal", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtCodFiscIf, "codfisc", "codfisc", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtCodItFisc, "coditfisc", "coditfisc", ListaCampos.DB_SI, false );

		adicDBLiv( txaObsItCompra, "ObsItCompra", "Observação", false );

		// Editaveis apenas na importação

		setPainel( pnImpDet );

		adicCampo( txtNAdicao, 7, 20, 50, 20, "nadicao", "N.Adição", ListaCampos.DB_SI, false );
		adicCampo( txtSeqAdic, 60, 20, 70, 20, "seqadic", "Seq.Adição", ListaCampos.DB_SI, false );
		adicCampo( txtDescDI, 133, 20, 70, 20, "descdi", "Vlr.Desc.DI", ListaCampos.DB_SI, false );

		adicCampo( txtVlrII, 206, 20, 70, 20, "vlriiItcompra", "Vlr.II", ListaCampos.DB_SI, false );
		txtCustoItCompra.setSoLeitura( !habilitaCusto );
		adicCampo( txtCustoItCompra, 279, 20, 90, 20, "CustoItCompra", "Custo", ListaCampos.DB_SI, false );



		//adicCampo(txtVlrCOFINSItCompra, 374, 60, 90, 20,"VLRCOFINS","VLRCOFINS", ListaCampos.DB_SI, false );


		lbNumSerie.setVisible( false );
		//		lbCodLote.setVisible( false );

		pinTot.adic( txtVlrBrutCompra	, 7		, 20	, 120	, 20	, "Total Produtos" );
		pinTot.adic( txtVlrIPICompra	, 7		, 60	, 60	, 20	, "+ Total IPI" );
		pinTot.adic( txtVlrFreteCompra	, 70	, 60	, 60	, 20	, "+ Frete" );
		pinTot.adic( txtVlrDescCompra	, 7		, 100	, 60	, 20	, "- Desc." );
		pinTot.adic( txtVlrAdicCompra	, 70	, 100	, 60	, 20	, "+ Adic." );
		pinTot.adic( txtVlrTotalNota	, 7		, 140	, 120	, 25	, "= Total Geral da Nota" );

		txtVlrTotalNota.setFont( SwingParams.getFontboldmax() );

		txtCodNat.setStrMascara( "#.###" );
		/*
		 * txtRefProd.addKeyListener(new KeyAdapter() { public void keyPressed(KeyEvent kevt) { lcDet.edit(); } });
		 */

		//setListaCampos(lcLFItCompra  );
		adic( txtVlrPISItCompra, 372, 20, 90, 20, "Vlr.PIS" );
		adic( txtVlrCOFINSItCompra, 465, 20, 90, 20, "Vlr.COFINS" );

		txtVlrSISCOMEXItCompra.setSoLeitura( true );
		adicCampo( txtVlrSISCOMEXItCompra, 558, 20, 90, 20, "VLRTXSISCOMEXITCOMPRA", "Vlr.Tx.SISCOMEX", ListaCampos.DB_SI, false  );

		setListaCampos( true, "ITCOMPRA", "CP" );
		lcDet.setQueryInsert( false );

		//txtVlrPISItCompra.setSoLeitura( true );




		montaTab();

		tab.setTamColuna( 30, ECOL_ITENS.CODITCOMPRA.ordinal());
		tab.setTamColuna( 70, ECOL_ITENS.CODPROD.ordinal() );
		tab.setTamColuna( 230, ECOL_ITENS.DESCPROD.ordinal() );
		tab.setTamColuna( 70, ECOL_ITENS.REFPROD.ordinal() );
		tab.setTamColuna( 80, 4 );
		tab.setTamColuna( 70, 6 );
		tab.setTamColuna( 70, 7 );
		tab.setTamColuna( 60, 8 );
		tab.setTamColuna( 70, 9 );
		tab.setTamColuna( 60, 10 );
		tab.setTamColuna( 70, 11 );
		tab.setTamColuna( 200, 12 );
		tab.setTamColuna( 70, 13 );
		tab.setTamColuna( 60, 14 );
		tab.setTamColuna( 70, 15 );
		tab.setTamColuna( 70, 16 );
		tab.setTamColuna( 60, 17 );
		tab.setTamColuna( 70, 18 );
		tab.setTamColuna( 80, 19 );
		tab.setTamColuna( 90, 20 );

	}

	private void adicIPI() {

		double deVlrProd = Funcoes.arredDouble( txtVlrProdItCompra.doubleValue() - txtVlrDescItCompra.doubleValue() + txtVlrIPIItCompra.doubleValue(), casasDecFin );
		txtVlrLiqItCompra.setVlrBigDecimal( new BigDecimal( deVlrProd ) );
	}

	public void mostraObs( String sTabela, int iCod ) {

		FObservacao obs = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQLselect = null;
		String sSQLupdate = null;

		try {

			try {

				if ( sTabela.equals( "CPCOMPRA" ) ) {

					sSQLselect = "SELECT OBSERVACAO FROM CPCOMPRA WHERE CODEMP=? AND CODFILIAL=? AND CODCOMPRA=?";
					sSQLupdate = "UPDATE CPCOMPRA SET OBSERVACAO=? WHERE CODEMP=? AND CODFILIAL=? AND CODCOMPRA=?";
				}

				ps = con.prepareStatement( sSQLselect );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( sTabela ) );
				ps.setInt( 3, iCod );
				rs = ps.executeQuery();

				if ( rs.next() ) {
					obs = new FObservacao( ( rs.getString( 1 ) != null ? rs.getString( 1 ) : "" ) );
				}
				else {
					obs = new FObservacao( "" );
				}

				rs.close();
				ps.close();

				con.commit();

			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao carregar a observação!\n" + err.getMessage(), true, con, err );
			}
			if ( obs != null ) {

				obs.setVisible( true );

				if ( obs.OK ) {

					try {
						ps = con.prepareStatement( sSQLupdate );
						ps.setString( 1, obs.getTexto() );
						ps.setInt( 2, Aplicativo.iCodEmp );
						ps.setInt( 3, ListaCampos.getMasterFilial( sTabela ) );
						ps.setInt( 4, iCod );
						ps.executeUpdate();

						ps.close();

						con.commit();

					} catch ( SQLException err ) {
						Funcoes.mensagemErro( this, "Erro ao inserir observação no orçamento!\n" + err.getMessage(), true, con, err );
					}
				}

				obs.dispose();

			}

		} finally {
			ps = null;
			rs = null;
			sSQLselect = null;
			sSQLupdate = null;
		}

	}

	public void buscaRetornoRemessa() {

		if ( txtCodCompra.getVlrInteger() == 0 ) {
			return;
		}

		DLRetRemessa buscaRemessa = new DLRetRemessa( "VR" );
		buscaRemessa.setConexao( con );
		buscaRemessa.setVisible( true );

		if ( buscaRemessa.OK ) {

			List<DLRetRemessa.GridBuscaRetorno> gridBuscaRemessa = buscaRemessa.getGridBuscaRemessa();

			for ( DLRetRemessa.GridBuscaRetorno g : gridBuscaRemessa ) {

				lcDet.cancel( true );
				lcDet.insert( true );

				txtCodProd.setVlrInteger( g.getCodigoProduto() );

				lcProd.carregaDados();

				txtQtdItCompra.setVlrBigDecimal( g.getSaldo() );
				txtPrecoItCompra.setVlrBigDecimal( g.getPrecoRemessa() );

				calcVlrProd();
				calcImpostos( true );

				lcDet.post();
			}

			lcCampos.carregaDados();
		}

		buscaRemessa.dispose();
	}

	private void bloqCompra() {

		PreparedStatement ps = null;
		String sSQL = null;
		int iCodCompra = 0;
		try {
			iCodCompra = txtCodCompra.getVlrInteger().intValue();
			if ( iCodCompra != 0 ) {
				sSQL = "EXECUTE PROCEDURE CPBLOQCOMPRASP(?,?,?,?)";
				ps = con.prepareStatement( sSQL );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
				ps.setInt( 3, iCodCompra );
				ps.setString( 4, "S" );
				ps.executeUpdate();
				ps.close();
				con.commit();
			}
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro bloqueando a compra!\n" + err.getMessage(), true, con, err );
		} finally {
			ps = null;
			sSQL = null;
		}
	}

	private void calcICMSOld( boolean bCalcBase ) {

		String tpredicmfisc = txtTpRedIcmsFisc.getVlrString();
		float fRed = txtRedFisc.floatValue();
		float fVlrProd = Funcoes.arredFloat( txtVlrProdItCompra.floatValue() - txtVlrDescItCompra.floatValue(), casasDecFin );
		float fBaseICMS = Funcoes.arredFloat( txtVlrBaseICMSItCompra.floatValue(), casasDecFin );
		float fBaseICMSSt = Funcoes.arredFloat( txtVlrBaseICMSStItCompra.floatValue(), casasDecFin );
		float fBaseIPI = txtVlrBaseIPIItCompra.floatValue();

		float fICMS = 0;
		if ( fVlrProd > 0 ) {
			if ( bCalcBase ) {
				if ( "B".equals( tpredicmfisc ) ) {
					fBaseICMS = Funcoes.arredFloat( fVlrProd - ( fVlrProd * fRed / 100 ), casasDecFin );
				}
				else {
					fBaseICMS = Funcoes.arredFloat( fVlrProd, casasDecFin );
				}
				fBaseIPI = fVlrProd;
			}
			if ( ( "V".equals( tpredicmfisc ) ) && ( fRed > 0 ) ) {
				fICMS = Funcoes.arredFloat( fBaseICMS * txtPercICMSItCompra.floatValue() / 100, casasDecFin );
				fICMS -= Funcoes.arredFloat( fICMS * fRed / 100, casasDecFin );
			}
			else {
				fICMS = Funcoes.arredFloat( fBaseICMS * txtPercICMSItCompra.floatValue() / 100, casasDecFin );
			}
		}
		txtVlrICMSItCompra.setVlrBigDecimal( new BigDecimal( fICMS ) );

		if ( bCalcBase ) {
			txtVlrBaseICMSItCompra.setVlrBigDecimal( new BigDecimal( String.valueOf( fBaseICMS ) ) );
			txtVlrBaseICMSStItCompra.setVlrBigDecimal( new BigDecimal( String.valueOf( fBaseICMSSt ) ) );
			txtVlrBaseIPIItCompra.setVlrBigDecimal( new BigDecimal( String.valueOf( fBaseIPI ) ) );
		}
		txtVlrLiqItCompra.setVlrBigDecimal( new BigDecimal( String.valueOf( fVlrProd ) ) );
		txtPercIPIItCompra.setVlrBigDecimal( txtAliqIPIFisc.getVlrBigDecimal() );

	}

	private void calcIpi( boolean vlr ) {

		BigDecimal vlripi = null;
		BigDecimal baseipi = null;
		BigDecimal percipi = null;

		try {

			baseipi = txtVlrBaseIPIItCompra.getVlrBigDecimal();
			percipi = txtPercIPIItCompra.getVlrBigDecimal();
			vlripi = txtVlrIPIItCompra.getVlrBigDecimal();

			if ( baseipi.floatValue() > 0 ) {
				if ( vlr && percipi.floatValue() > 0 ) {
					vlripi = ( baseipi.multiply( percipi ) ).divide( new BigDecimal( 100 ), 5, BigDecimal.ROUND_HALF_EVEN );
					txtVlrIPIItCompra.setVlrBigDecimal( vlripi );
				}
				else if ( vlripi.floatValue() > 0 ) {
					percipi = vlripi.divide( baseipi, 5, BigDecimal.ROUND_HALF_EVEN ).multiply( new BigDecimal( 100 ) );
					txtPercIPIItCompra.setVlrBigDecimal( percipi );
				}
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private void calcVlrProd() {

		BigDecimal preco = txtPrecoItCompra.getVlrBigDecimal();
		BigDecimal qtd = txtQtdItCompra.getVlrBigDecimal();
		BigDecimal vlrtot = qtd.multiply( preco );
		txtVlrProdItCompra.setVlrBigDecimal( vlrtot );
	}

	public void calcISS() {

		try {

			BigDecimal baseiss = txtVlrLiqItCompra.getVlrBigDecimal();
			BigDecimal aliqiss = txtAliqISSItCompra.getVlrBigDecimal();

			BigDecimal vlriss = baseiss.multiply( aliqiss.divide( cem ) );

			txtVlrISSItCompra.setVlrBigDecimal( vlriss );

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	private void emiteNFE() {

		nfecf.setTpNF( AbstractNFEFactory.TP_NF_IN );

		nfecf.setKey( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "CPCOMPRA" ), txtCodCompra.getVlrInteger(), txtDocCompra.getVlrInteger() );

		nfecf.post();

	}

	private void emiteNotaFiscal( final String sTipo ) {

		if ( ( nfecf.getHasNFE() && "E".equals( txtTipoModNota.getVlrString() ) ) ) {
			emiteNFE();
		}
		else {
			emiteNF( sTipo );
		}
	}

	private void emiteNF( String tipo ) {

		Object layNF = null;
		Vector<Integer> parans = null;
		NFEntrada nf = null;
		String sTipo = tipo;
		boolean bImpOK = false;
		int iCodCompra = txtCodCompra.getVlrInteger().intValue();
		ImprimeOS imp = new ImprimeOS( "", con, sTipo, true );
		imp.verifLinPag( sTipo );
		imp.setTitulo( "Nota Fiscal" );
		DLRPedido dl = new DLRPedido( sOrdNota, "coditcompra", false );
		dl.setVisible( true );
		if ( dl.OK == false ) {
			dl.dispose();
			return;
		}
		try {
			layNF = Class.forName( "org.freedom.layout.nf." + imp.getClassNota() ).newInstance();
		} catch ( Exception err ) {
			Funcoes.mensagemInforma( this, "Não foi possível carregar o leiaute de Nota Fiscal!\n" + err.getMessage() );
		}
		try {
			if ( layNF != null ) {
				if ( layNF instanceof Layout ) {
					parans = new Vector<Integer>();
					parans.addElement( new Integer( Aplicativo.iCodEmp ) );
					parans.addElement( new Integer( ListaCampos.getMasterFilial( "CPCOMPRA" ) ) );
					parans.addElement( new Integer( iCodCompra ) );
					nf = new NFEntrada( casasDec );
					nf.carregaTabelas( con, parans );
					bImpOK = ( (Layout) layNF ).imprimir( nf, imp );
				}
				else if ( layNF instanceof Leiaute ) {
					Funcoes.mensagemInforma( this, "O layout de Nota Fiscal\nnão se aplica para nota de entrada " );
				}
			}
		} catch ( Exception err ) {
			Funcoes.mensagemErro( this, "Erro ao emitir nota de Compra\n!" + err.getMessage(), true, con, err );
		}
		dl.dispose();
		if ( bImpOK )
			imp.preview( this );
		imp.fechaPreview();
	}

	public void exec( int iCodCompra ) {

		txtCodCompra.setVlrString( iCodCompra + "" );
		lcCampos.carregaDados();
	}

	public void execDev( int iCodFor, int iCodTipoMov, String sSerie, int iDoc ) {

		lcCampos.insert( true );
		txtCodFor.setVlrString( iCodFor + "" );
		lcFor.carregaDados();
		txtCodTipoMov.setVlrString( iCodTipoMov + "" );
		lcTipoMov.carregaDados();
		txtSerieCompra.setVlrString( sSerie );
		txtDocCompra.setVlrString( iDoc + "" );
	}

	private void getCFOP() {

		txtCodNat.setVlrString( buscaCFOP( txtCodProd.getVlrInteger(), txtCodFor.getVlrInteger(), txtCodTipoMov.getVlrInteger(), txtCodItFisc.getVlrInteger(), con ) );
		lcNat.carregaDados();

	}

	/**
	 * Busca da Natureza de Operação . Busca a natureza de operação através da tabela de regras fiscais.
	 */
	private static String buscaCFOP( Integer codprod, Integer codfor, Integer codtipomov, Integer coditfisc, DbConnection conex ) {

		String ret = null;
		String sSQL = "SELECT CODNAT FROM LFBUSCANATSP (?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try {

			PreparedStatement ps = conex.prepareStatement( sSQL );

			ps.setInt( 1, Aplicativo.iCodFilial );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			ps.setInt( 4, codprod );
			ps.setNull( 5, Types.INTEGER );
			ps.setNull( 6, Types.INTEGER );
			ps.setNull( 7, Types.INTEGER );
			ps.setInt( 8, Aplicativo.iCodEmp );
			ps.setInt( 9, ListaCampos.getMasterFilial( "CPFORNECED" ) );
			ps.setInt( 10, codfor );
			ps.setInt( 11, ListaCampos.getMasterFilial( "EQTIPOMOV" ) );
			ps.setInt( 12, codtipomov );

			// Incluido parametro com o código do item fiscal
			if ( null == coditfisc || coditfisc <= 0 ) {
				ps.setNull( 13, Types.INTEGER );
			}
			else {
				ps.setInt( 13, coditfisc );
			}

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				ret = rs.getString( "CODNAT" );

			}
			rs.close();
			ps.close();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( null, "Erro ao buscar natureza da operação!\n" + err.getMessage(), true, conex, err );
		}

		return ret;

	}

	/**
	 * Busca de icms. Busca a percentagem de ICMS conforme a regra fiscal.
	 */
	/*
	 * private void getICMS() {
	 * 
	 * String sSQL = "SELECT PERCICMS FROM LFBUSCAICMSSP(?,?,?,?)"; PreparedStatement ps = null; ResultSet rs = null; try { ps = con.prepareStatement( sSQL ); ps.setString( 1, txtCodNat.getVlrString() ); ps.setString( 2, txtEstFor.getVlrString() ); ps.setInt( 3, Aplicativo.iCodEmp ); ps.setInt( 4,
	 * Aplicativo.iCodFilialMz ); rs = ps.executeQuery(); if ( rs.next() ) { txtPercICMSItCompra.setVlrBigDecimal( new BigDecimal( rs.getString( 1 ) ) ); } calcImpostos( true ); } catch ( SQLException err ) { Funcoes.mensagemErro( this, "Erro ao buscar percentual de ICMS!\n" + err.getMessage(),
	 * true, con, err ); } }
	 */
	private void getCustoProd() {

		try {
			PreparedStatement ps = con.prepareStatement( "SELECT NCUSTOPEPS, NCUSTOMPM FROM EQPRODUTOSP01(?,?,?,?,?,?)" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			ps.setInt( 3, txtCodProd.getVlrInteger().intValue() );
			ps.setInt( 4, Aplicativo.iCodEmp );
			ps.setInt( 5, ListaCampos.getMasterFilial( "EQALMOX" ) );
			ps.setInt( 6, txtCodAlmoxProd.getVlrInteger().intValue() );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {

				txtCustoPEPSProd.setVlrBigDecimal( rs.getBigDecimal( "NCUSTOPEPS" ) == null ? new BigDecimal( 0 ) : rs.getBigDecimal( "NCUSTOPEPS" ) );
				txtCustoMPMProd.setVlrBigDecimal( rs.getBigDecimal( "NCUSTOMPM" ) == null ? new BigDecimal( 0 ) : rs.getBigDecimal( "NCUSTOMPM" ) );

				txtCustoItCompra.setVlrBigDecimal( txtCustoMPMProd.getVlrBigDecimal() );

				/*
				 * if ( ? ) { // Implementar futuramente caso deva utilizar mpm ou peps. txtCustoItCompra.setVlrBigDecimal( txtCustoMPMProd.getVlrBigDecimal() ); } else { txtCustoItCompra.setVlrBigDecimal( txtCustoPEPSProd.getVlrBigDecimal() ); }
				 */
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar custo do produto.", true, con, e );
		}
	}

	private void getPrefere() {

		StringBuffer sql = new StringBuffer();
		try {

			sql.append( "SELECT P1.USAREFPROD,P1.ORDNOTA,P1.BLOQCOMPRA,P1.BUSCAVLRULTCOMPRA,P1.CUSTOCOMPRA, " );
			sql.append( "P1.TABTRANSPCP, P1.TABSOLCP,P1.TABIMPORTCP, P1.CLASSCP, P1.LABELOBS01CP, P1.LABELOBS02CP, " );
			sql.append( "P1.LABELOBS03CP, P1.LABELOBS04CP, P5.HABCONVCP, P1.USABUSCAGENPRODCP, COALESCE(P1.BLOQPRECOAPROV, 'N') BLOQPRECOAPROV, " );
			sql.append( "P1.CODTIPOMOVIM, P1.BLOQSEQICP, P1.UTILORDCPINT, P1.TOTCPSFRETE, P1.UTILIZATBCALCCA, P1.CCNFECP, P1.HABCOMPRACOMPL ");
			sql.append( ", P1.NPERMITDTMAIOR, P1.PROCEMINFE " );
			sql.append( "FROM SGPREFERE1 P1 LEFT OUTER JOIN SGPREFERE5 P5 ON " );
			sql.append( "P1.CODEMP=P5.CODEMP AND P1.CODFILIAL=P5.CODFILIAL " );
			sql.append( "WHERE P1.CODEMP=? AND P1.CODFILIAL=?" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {

				comref = rs.getString( "USAREFPROD" ).trim().equals( "S" );
				buscagenericaprod = rs.getString( "USABUSCAGENPRODCP" ).trim().equals( "S" );
				podeBloq = rs.getString( "BLOQCOMPRA" ).trim().equals( "S" );
				buscaVlrUltCompra = rs.getString( "BUSCAVLRULTCOMPRA" ).trim().equals( "S" );
				sOrdNota = rs.getString( "ORDNOTA" );
				habilitaCusto = rs.getString( "CUSTOCOMPRA" ).trim().equals( "S" );
				abaTransp = rs.getString( "TABTRANSPCP" );
				abaSolCompra = rs.getString( "TABSOLCP" );
				abaImport = rs.getString( "TABIMPORTCP" );
				classcp = rs.getString( "CLASSCP" );
				labelobs01cp = rs.getString( "LABELOBS01CP" );
				labelobs02cp = rs.getString( "LABELOBS02CP" );
				labelobs03cp = rs.getString( "LABELOBS03CP" );
				labelobs04cp = rs.getString( "LABELOBS04CP" );
				habconvcp = rs.getString( "HABCONVCP" ) == null ? false : rs.getString( "HABCONVCP" ).equals( "S" );
				bloqprecoaprov = rs.getString( "BLOQPRECOAPROV" ).equals( "S" );
				codtipomovim = rs.getInt( "codtipomovim" );
				bloqseqicp = rs.getString("BLOQSEQICP");
				utilordcpint = rs.getString("UTILORDCPINT");
				totcpsfrete = rs.getString( "TOTCPSFRETE" );
				utilizatbcalcca = rs.getString( "UTILIZATBCALCCA" );
				consistChaveNFE = rs.getString( "CCNFECP" );
				habcompracompl = rs.getString( "HABCOMPRACOMPL" ) == null ? false : rs.getString( "HABCOMPRACOMPL" ).equals( "S" );
				npermitdtmaior = rs.getString( "NPERMITDTMAIOR" ) == null ? false : rs.getString( "NPERMITDTMAIOR" ).equals( "S" );
				proceminfe = rs.getString( "PROCEMINFE" ) == null ? "3" : rs.getString("PROCEMINFE");

			}
			con.commit();
		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + e.getMessage(), true, con, e );
		}
	}

	private void getVlrUltimaCompra() {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			sql.append("SELECT FIRST 1 IT.PRECOITCOMPRA ");
			sql.append("FROM CPCOMPRA C, CPITCOMPRA IT ");
			sql.append("WHERE C.CODEMP=IT.CODEMP AND C.CODFILIAL=IT.CODFILIAL AND C.CODCOMPRA=IT.CODCOMPRA AND IT.CODEMP=? AND IT.CODFILIAL=? AND IT.CODPROD=? ");
			sql.append("ORDER BY C.DTENTCOMPRA DESC, C.CODCOMPRA DESC");

			ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "CPITCOMPRA" ) );

			if ( comref ) {
				ps.setInt( 3, txtRefProd.getVlrInteger().intValue() );
			}
			else {
				ps.setInt( 3, txtCodProd.getVlrInteger().intValue() );
			}

			rs = ps.executeQuery();

			// Se encontrou ultima compra, carrega o valor, do contrário sugere o preço base.
			if ( rs.next() ) {
				txtPrecoItCompra.setVlrBigDecimal( rs.getBigDecimal( "precoitcompra" ) );
			}
			else {
				txtPrecoItCompra.setVlrBigDecimal( txtPrecoBaseProd.getVlrBigDecimal() );
			}

		} catch ( Exception err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar valor da ultima compra!\n" + err.getMessage(), true, con, err );
		}
	}

	private void imprimir( TYPE_PRINT bVisualizar, int iCodCompra ) {

		DLRPedido dl = new DLRPedido( sOrdNota, "I.CODITCOMPRA", false );
		dl.setConexao( con );
		dl.setVisible( true );
		String ordem = dl.getValor();
		//if (!"I.CODITCOMPRA".equalsIgnoreCase( ordem )) {
		//	ordem = "P."+ ordem;
		//}
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();

		if ( dl.OK == false ) {

			dl.dispose();
			return;
		}
		/*
		sSQL.append( "SELECT (SELECT COUNT(IC.CODITCOMPRA) FROM CPITCOMPRA IC WHERE IC.CODCOMPRA=C.CODCOMPRA " );
		sSQL.append( "AND IC.CODEMP=C.CODEMP AND IC.CODFILIAL=C.CODFILIAL), " );
		sSQL.append( "C.CODCOMPRA,C.CODFOR,F.RAZFOR,F.CNPJFOR,F.CPFFOR,C.DTEMITCOMPRA,F.ENDFOR, " );
		sSQL.append( "F.BAIRFOR,F.CEPFOR,C.DTENTCOMPRA,F.CIDFOR,F.UFFOR,F.FONEFOR,F.DDDFONEFOR, " );
		sSQL.append( "F.FAXFOR,F.INSCFOR,F.RGFOR,I.CODPROD,P.REFPROD,P.DESCPROD,P.CODUNID, " );
		sSQL.append( "I.QTDITCOMPRA,I.PRECOITCOMPRA,I.VLRPRODITCOMPRA,I.CODNAT,I.PERCICMSITCOMPRA, " );
		sSQL.append( "PERCIPIITCOMPRA,VLRIPIITCOMPRA,C.VLRBASEICMSCOMPRA,C.VLRICMSCOMPRA,C.VLRPRODCOMPRA, " );
		sSQL.append( "C.VLRDESCCOMPRA,C.VLRDESCITCOMPRA,C.VLRADICCOMPRA,C.VLRIPICOMPRA,F.CONTFOR, C.TIPOFRETECOMPRA, C.VLRFRETECOMPRA, C.OBSERVACAO, " );
		sSQL.append( "C.VLRLIQCOMPRA,C.CODPLANOPAG,PG.DESCPLANOPAG, C.OBS01, C.OBS02, C.OBS03, C.OBS04, " );
		sSQL.append( "SG.LABELOBS01CP, SG.LABELOBS02CP, SG.LABELOBS03CP, SG.LABELOBS04CP, I.CODITCOMPRA " );
		sSQL.append( "FROM CPCOMPRA C, CPFORNECED F,CPITCOMPRA I, EQPRODUTO P, FNPLANOPAG PG, SGPREFERE1 SG " );
		sSQL.append( "WHERE C.CODCOMPRA=? AND C.CODEMP=? AND C.CODFILIAL=? AND " );
		sSQL.append( "F.CODEMP=C.CODEMPFR AND F.CODFILIAL=C.CODFILIALFR AND F.CODFOR=C.CODFOR AND " );
		sSQL.append( "I.CODEMP=C.CODEMP AND I.CODFILIAL=C.CODFILIAL AND I.CODCOMPRA=C.CODCOMPRA AND " );
		sSQL.append( "P.CODEMP=I.CODEMPPD AND P.CODFILIAL=I.CODFILIALPD AND P.CODPROD=I.CODPROD AND " );
		sSQL.append( "PG.CODEMP=C.CODEMPPG AND PG.CODFILIAL=C.CODFILIALPG AND PG.CODPLANOPAG=C.CODPLANOPAG AND " );
		sSQL.append( "SG.CODEMP=? AND SG.CODFILIAL=? " );
		sSQL.append( "ORDER BY C.CODCOMPRA," + ordem );
		 */


		sql.append( "SELECT (SELECT COUNT(IC.CODITCOMPRA) FROM CPITCOMPRA IC " );
		sql.append( "WHERE IC.CODCOMPRA=C.CODCOMPRA AND IC.CODEMP=C.CODEMP AND " );
		sql.append( "IC.CODFILIAL=C.CODFILIAL),C.CODCOMPRA,C.CODFOR,F.RAZFOR,F.CNPJFOR, " );
		sql.append( "F.CPFFOR,C.DTEMITCOMPRA,F.ENDFOR,F.BAIRFOR,F.CEPFOR,C.DTENTCOMPRA, " );
		sql.append( "F.CIDFOR,F.UFFOR,F.FONEFOR,F.DDDFONEFOR,F.FAXFOR,F.INSCFOR,F.RGFOR, " );
		sql.append( "I.CODPROD,P.REFPROD,P.DESCPROD,P.CODUNID,I.QTDITCOMPRA,I.PRECOITCOMPRA, " );
		sql.append( "I.VLRPRODITCOMPRA,I.CODNAT,I.PERCICMSITCOMPRA,PERCIPIITCOMPRA,VLRIPIITCOMPRA, " );
		sql.append( "C.VLRBASEICMSCOMPRA,C.VLRICMSCOMPRA,C.VLRPRODCOMPRA,C.VLRDESCCOMPRA,I.VLRLIQITCOMPRA, " );
		sql.append( "C.VLRDESCITCOMPRA,C.VLRADICCOMPRA,C.VLRIPICOMPRA,C.VLRLIQCOMPRA,F.CONTFOR, " );
		sql.append( "C.TIPOFRETECOMPRA, C.VLRFRETECOMPRA, C.OBSERVACAO, I.CODITCOMPRA, I.PERCIPIITCOMPRA, " );
		sql.append( "C.CODPLANOPAG, PG.DESCPLANOPAG, C.Obs01, C.Obs02, C.Obs03, C.Obs04, SG.LABELOBS01CP, " );
		sql.append( "SG.LABELOBS02CP, SG.LABELOBS03CP, SG.LABELOBS04CP, P.COMPRIMENTO, " );
		sql.append( "E.ENDFILIAL, E.NUMFILIAL, E.BAIRFILIAL, E.CEPFILIAL, E.CNPJFILIAL, E.SIGLAUF, E.RAZFILIAL, " );
		sql.append( "E.INSCFILIAL, E.DDDFILIAL, E.FONEFILIAL, E.UFFILIAL, M.NOMEMUNIC, PG.DESCPLANOPAG, C.CODORDCP " );
		sql.append( "FROM CPCOMPRA C, CPFORNECED F, SGFILIAL E, SGMUNICIPIO M, " );
		sql.append( "CPITCOMPRA I, EQPRODUTO P, FNPLANOPAG PG, SGPREFERE1 SG " );
		sql.append( "WHERE C.CODCOMPRA=? AND C.CODEMP=? AND C.CODFILIAL=? " );
		sql.append( "AND F.CODFOR=C.CODFOR AND I.CODCOMPRA=C.CODCOMPRA AND P.CODPROD=I.CODPROD " );
		sql.append( "AND PG.CODPLANOPAG=C.CODPLANOPAG " );
		sql.append( "AND E.CODMUNIC=M.CODMUNIC AND E.SIGLAUF=M.SIGLAUF AND  E.CODPAIS=M.CODPAIS " );
		sql.append( "AND I.CODEMP=E.CODEMP AND I.CODFILIAL=E.CODFILIAL " );
		sql.append( "AND SG.CODEMP=? AND SG.CODFILIAL=? ");
		//sql.append( "AND c.CODCOMPRA=? AND E.CODEMP=? " );
		sql.append( "ORDER BY C.CODCOMPRA, " + ordem  );


		try {

			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, iCodCompra );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
			ps.setInt( 4, Aplicativo.iCodEmp );
			ps.setInt( 5, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

			System.out.println( sql.toString() );

			rs = ps.executeQuery();

		} catch ( SQLException err ) {

			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar dados da compra" );
		}
		if ( dl.getTipo() == "G" ) {

			imprimiGrafico( rs, bVisualizar );

		}
		else {

			imprimeTexto( rs, bVisualizar );
		}

	}


	private boolean isChaveNFEValid(Integer codemp, Integer codfilial, Integer codtipomov ) {
		boolean result = true;
		PreparedStatement ps = null;

		String tipomodnota = "N"; // Define o tipo de emissão de nota N=Normal - E-Eletrônica

		String emitnfcpmov = "N"; // Define se a emissão é própria N=Não - S=Sim

		String chaveValida = "N"; // Define se a chave é valída N=Não - S=Sim

		StringBuilder sql = new StringBuilder();

		sql.append( "select mn.tipomodnota, tm.emitnfcpmov from eqtipomov tm, lfmodnota mn " );
		sql.append( "where tm.codemp=? and tm.codfilial=? and tm.codtipomov=? ");
		sql.append( "and mn.codemp=tm.codempmn and mn.codfilial=tm.codfilialmn and mn.codmodnota=tm.codmodnota" );

		try {

			ps = con.prepareStatement( sql.toString() );
			int param = 1;

			ps.setInt( param++, codemp);
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codtipomov);

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				tipomodnota = rs.getString( "tipomodnota" );
				emitnfcpmov = rs.getString( "emitnfcpmov" );


				//result = "E".equals(rs.getString( "tipomodnota" )) && "N".equals(rs.getString( "emitnfcpmov" ) );
			} else {
				Funcoes.mensagemInforma( this, "Tipo de movimento não encontrado para validação de chave da NFe !" );
				result = false;
			}

			//			Funcoes.mensagemInforma( this, "Campo Chave de Acesso da Nota Fiscal Eletrônica é obrigatório!!!" );

			rs.close();
			ps.close();
			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( null, "Erro ao buscar modelo da nota no tipo de movimento!\n" + err.getMessage(), true, con, err );
		}

		if ( result && "N".equals(emitnfcpmov) && "E".equals( tipomodnota ) ) {
			if ("".equals( txtChaveNfe.getVlrString().trim() )) {
				Funcoes.mensagemInforma( this, "Campo chave de acesso da nota fiscal eletrônica é obrigatório !" );
				result = false;
			} else if (txtChaveNfe.getVlrString().trim() .length()<TAMANHOCHAVE ) {
				Funcoes.mensagemInforma( this, "Campo chave de acesso da nota fiscal eletrônica deve conter "+ TAMANHOCHAVE +" caracteres !" );
				result = false;
			}
		}
		else if (result && "S".equals(emitnfcpmov) && "E".equals( tipomodnota ) ) {
			if (!"".equals( txtChaveNfe.getVlrString() ) && txtChaveNfe.getVlrString().trim().length()<TAMANHOCHAVE ) {
				Funcoes.mensagemInforma( this, "Campo chave de acesso da nota fiscal eletrônica deve conter "+TAMANHOCHAVE+" caracteres !" );
				result = false;
			}
		}
		if ( result && "N".equals( emitnfcpmov ) && nfecf != null ) { 

			nfecf.setKey( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "CPCOMPRA" ), txtCodCompra.getVlrInteger(), txtDocCompra.getVlrInteger() );


			result = nfecf.consistChaveNFE( txtChaveNfe.getVlrString() );
			// Remover hardcode após conclusão da rotina de pesquisa
			int codretorno = nfecf.getReturnKey().getCodeReturn();
			String mensagem = nfecf.getReturnKey().getMessage();
			chaveValida = nfecf.getReturnKey().isValidKey();

			if(codretorno!= 100){
				Funcoes.mensagemInforma( this, codretorno + " - " + mensagem );
			}

			gravaLogConsultaNfe(Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "CPCOMPRA" ), txtCodCompra.getVlrInteger()
					, codretorno, mensagem, chaveValida, txtChaveNfe.getVlrString());

			cbChaveNFEValida.setVlrString( chaveValida );
		}

		return result;
	}

	private void gravaLogConsultaNfe(int codemp, int codfilial, int codcompra, int codretorno, String mensagem, String chaveValida, String chavenfe) {
		/*		ID BIGINT NOT NULL, 
		CODEMP INTEGER NOT NULL,
		CODFILIAL INTEGER NOT NULL, 
		CODCOMPRA INTEGER NOT NULL,
		DTCONSULTA DATE NOT NULL, 
		HCONSULTA TIME NOT NULL,
		CODRETORNO INTEGER NOT NULL,
		MENSAGEM VARCHAR(2000) NOT NULL,
		CHAVEVALIDA DEFAULT 'N' NOT NULL,
		DTINS DATE DEFAULT 'now' NOT NULL,
        HINS TIME DEFAULT 'now' NOT NULL,
        IDUSUINS VARCHAR(128) DEFAULT USER NOT NULL,
        DTALT DATE DEFAULT 'now',
        HALT TIME DEFAULT 'now',
        IDUSUALT VARCHAR(128) DEFAULT USER,
		 * */
		StringBuilder sqlinsert = new StringBuilder("insert into cpcompralcchave ");
		sqlinsert.append( "(id, codemp, codfilial, codcompra, dtconsulta, hconsulta, codretorno, mensagem, chavevalida, chavenfe) " );
		sqlinsert.append( "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
		PreparedStatement ps = null;
		ResultSet rs = null;
		int param = 1;
		int id = -1;
		try {
			ps = con.prepareStatement( "select biseq from sgsequence_idsp(?)" );
			ps.setString( param, "LOGNFE" );

			rs = ps.executeQuery();
			if (rs.next()) {
				id = rs.getInt( "biseq" ); 
			}
			rs.close();
			ps.close();

			ps = con.prepareStatement( sqlinsert.toString() );
			ps.setInt( param++, id );
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codcompra );
			ps.setDate( param++, Funcoes.dateToSQLDate( new Date() ) );
			ps.setTime( param++, Funcoes.dateToSQLTime( new Date() ) );
			ps.setInt( param++, codretorno );
			ps.setString( param++, mensagem  );
			ps.setString( param++, chaveValida  );
			ps.setString( param++, chavenfe  );
			ps.executeUpdate();
			ps.close();
			con.commit();

		} catch (SQLException e) {
			Funcoes.mensagemInforma( this, "Erro carregando sequencia de log.\n" + e.getMessage() );
			try {
				con.rollback();
			} catch (SQLException err) {
				Funcoes.mensagemInforma( this, "Erro carregando sequencia de log no comando rollback.\n" + e.getMessage() );
			}
		}

	}

	private String getModeloNota( Integer codemp, Integer codfilial, Integer codtipomov ) {

		String result = null;
		PreparedStatement ps = null;

		StringBuilder sql = new StringBuilder();


		sql.append( "select tm.codmodnota from eqtipomov tm " );
		sql.append( "where tm.codemp=? and tm.codfilial=? and tm.codtipomov=? ");

		try {

			ps = con.prepareStatement( sql.toString() );
			int param = 1;

			ps.setInt( param++, codemp);
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codtipomov);

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				result = rs.getString( "codmodnota" );

			}
			rs.close();
			ps.close();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( null, "Erro ao buscar modelo da nota no tipo de movimento!\n" + err.getMessage(), true, con, err );
		}

		return result;
	}


	public void imprimeTexto( final ResultSet rs, final TYPE_PRINT bVisualizar ) {

		ImprimeOS imp = new ImprimeOS( "", con );
		int iItImp = 0;
		int iMaxItem = 0;

		try {

			imp.limpaPags();
			iMaxItem = imp.verifLinPag() - 23;

			while ( rs.next() ) {
				if ( imp.pRow() == 0 ) {
					imp.montaCab();
					imp.setTitulo( "Relatório de Pedidos de Compras" );
					imp.impCab( 136, false );

					imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
					imp.say( imp.pRow() + 0, 4, "PEDIDO DE COMPRA No.: " );
					imp.say( imp.pRow() + 0, 25, rs.getString( "CodCompra" ) );
					imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
					imp.say( imp.pRow() + 0, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 62, "FORNECEDOR" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 4, "[ Nome/Razao Social ]" );
					imp.say( imp.pRow() + 0, 76, rs.getString( "CpfFor" ) != null ? "[ CPF ]" : "[ CNPJ ]" );
					imp.say( imp.pRow() + 0, 96, "[ Data de Emissão ]" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 4, rs.getString( "CodFor" ) + " - " + rs.getString( "RazFor" ) );
					imp.say( imp.pRow() + 0, 76, rs.getString( "CpfFor" ) != null ? Funcoes.setMascara( rs.getString( "CpfFor" ), "###.###.###-##" ) : Funcoes.setMascara( rs.getString( "CnpjFor" ), "##.###.###/####-##" ) );
					imp.say( imp.pRow() + 0, 100, StringFunctions.sqlDateToStrDate( rs.getDate( "DtEmitCompra" ) ) );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 4, "[ Endereco ]" );
					imp.say( imp.pRow() + 0, 55, "[ Bairro ]" );
					imp.say( imp.pRow() + 0, 86, "[ CEP ]" );
					imp.say( imp.pRow() + 0, 96, "[ Data de Saída ]" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 4, rs.getString( "EndFor" ) );
					imp.say( imp.pRow() + 0, 55, rs.getString( "BairFor" ) );
					imp.say( imp.pRow() + 0, 86, Funcoes.setMascara( rs.getString( "CepFor" ), "#####-###" ) );
					imp.say( imp.pRow() + 0, 100, StringFunctions.sqlDateToStrDate( rs.getDate( "DtEntCompra" ) ) );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 4, "[ Municipio ]" );
					imp.say( imp.pRow() + 0, 39, "[ UF ]" );
					imp.say( imp.pRow() + 0, 46, "[ Fone/Fax ]" );
					imp.say( imp.pRow() + 0, 76, rs.getString( "RgFor" ) != null ? "[ RG ]" : "[ Insc. Est. ]" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 4, rs.getString( "CidFor" ) );
					imp.say( imp.pRow() + 0, 39, rs.getString( "UfFor" ) );
					imp.say( imp.pRow() + 0, 46, ( rs.getString( "DDDFONEFOR" ) != null ? "(" + rs.getString( "DDDFONEFOR" ) + ")" : "" ) + ( rs.getString( "FoneFor" ) != null ? Funcoes.setMascara( rs.getString( "FoneFor" ).trim(), "####-####" ) : "" ).trim() + " - "
							+ Funcoes.setMascara( rs.getString( "FaxFor" ), "####-####" ) );
					imp.say( imp.pRow() + 0, 76, rs.getString( "RgFor" ) != null ? rs.getString( "RgFor" ) : rs.getString( "CnpjFor" ) );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 57, "DADOS DO(S) PRODUTO(S)" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 4, "Referencia" );
					imp.say( imp.pRow() + 0, 18, "Descrição dos Produtos" );
					imp.say( imp.pRow() + 0, 56, "Unidade" );
					imp.say( imp.pRow() + 0, 65, "Quant." );
					imp.say( imp.pRow() + 0, 72, "Valor Unit." );
					imp.say( imp.pRow() + 0, 87, "Valor Total" );
					imp.say( imp.pRow() + 0, 102, "ICM%" );
					imp.say( imp.pRow() + 0, 108, "IPI%" );
					imp.say( imp.pRow() + 0, 114, "Valor do IPI" );
					imp.say( imp.pRow() + 0, 129, "Nat." );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "" );
				}
				imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
				imp.say( imp.pRow() + 0, 4, rs.getString( "RefProd" ) );
				imp.say( imp.pRow() + 0, 18, rs.getString( "DescProd" ).substring( 0, 39 ) );
				imp.say( imp.pRow() + 0, 56, rs.getString( "CodUnid" ) );
				imp.say( imp.pRow() + 0, 65, "" + rs.getDouble( "QtdItCompra" ) );
				imp.say( imp.pRow() + 0, 72, Funcoes.strDecimalToStrCurrency( 14, 2, rs.getString( "PrecoItCompra" ) ) );
				imp.say( imp.pRow() + 0, 87, Funcoes.strDecimalToStrCurrency( 14, 2, rs.getString( "VlrProdItCompra" ) ) );
				imp.say( imp.pRow() + 0, 102, "" + rs.getDouble( "PercICMSItCompra" ) );
				imp.say( imp.pRow() + 0, 108, "" + rs.getDouble( "PercIPIItCompra" ) );
				imp.say( imp.pRow() + 0, 114, Funcoes.strDecimalToStrCurrency( 14, 2, rs.getString( "VlrIPIItCompra" ) ) );
				imp.say( imp.pRow() + 0, 129, Funcoes.setMascara( rs.getString( "CodNat" ), "#.###" ) );

				iItImp++;

				if ( ( imp.pRow() >= iMaxItem ) | ( iItImp == rs.getInt( 1 ) ) ) {
					if ( ( iItImp == rs.getInt( 1 ) ) ) {
						int iRow = imp.pRow();
						for ( int i = 0; i < ( iMaxItem - iRow ); i++ ) {
							imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
							imp.say( imp.pRow() + 0, 0, "" );
						}
					}

					if ( rs.getInt( 1 ) == iItImp ) {
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 56, "CALCULO DO(S) IMPOSTO(S)" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 4, "[ Base de Calculo ICMS ]" );
						imp.say( imp.pRow() + 0, 29, "[ Valor do ICMS ]" );
						imp.say( imp.pRow() + 0, 54, "[ B. Calc. ICMS Subst. ]" );
						imp.say( imp.pRow() + 0, 79, "[ Valor do ICMS Subst. ]" );
						imp.say( imp.pRow() + 0, 104, "[ Valor dos Produtos ]" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 4, Funcoes.strDecimalToStrCurrency( 14, 2, rs.getString( "VlrBaseICMSCompra" ) ) );
						imp.say( imp.pRow() + 0, 29, Funcoes.strDecimalToStrCurrency( 14, 2, rs.getString( "VlrICMSCompra" ) ) );
						imp.say( imp.pRow() + 0, 104, Funcoes.strDecimalToStrCurrency( 14, 2, rs.getString( "VlrProdCompra" ) ) );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 4, "[ Valor do Frete ]" );
						imp.say( imp.pRow() + 0, 29, "[ Valor do Desconto ]" );
						imp.say( imp.pRow() + 0, 54, "[ Outras Despesas ]" );
						imp.say( imp.pRow() + 0, 79, "[ Valor do IPI ]" );
						imp.say( imp.pRow() + 0, 104, "[ VALOR TOTAL ]" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 29, Funcoes.strDecimalToStrCurrency( 14, 2, rs.getString( "VlrDescCompra" ) == null ? rs.getString( "VlrDescItCompra" ) : rs.getString( "VlrDescCompra" ) ) );
						imp.say( imp.pRow() + 0, 64, Funcoes.strDecimalToStrCurrency( 14, 2, rs.getString( "VlrAdicCompra" ) ) );
						imp.say( imp.pRow() + 0, 79, Funcoes.strDecimalToStrCurrency( 14, 2, rs.getString( "VlrIPICompra" ) ) );
						imp.say( imp.pRow() + 0, 104, Funcoes.strDecimalToStrCurrency( 14, 2, rs.getString( "VlrLiqCompra" ) ) );
						iItImp = 0;
					}
					else {
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 56, "CALCULO DO(S) IMPOSTO(S)" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 4, "[ Base de Calculo ICMS ]" );
						imp.say( imp.pRow() + 0, 29, "[ Valor do ICMS ]" );
						imp.say( imp.pRow() + 0, 54, "[ B. Calc. ICMS Subst. ]" );
						imp.say( imp.pRow() + 0, 79, "[ Valor do ICMS Subst. ]" );
						imp.say( imp.pRow() + 0, 104, "[ Valor dos Produtos ]" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 4, "***************" );
						imp.say( imp.pRow() + 0, 29, "***************" );
						imp.say( imp.pRow() + 0, 104, "***************" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 4, "[ Valor do Frete ]" );
						imp.say( imp.pRow() + 0, 29, "[ Valor do Desconto ]" );
						imp.say( imp.pRow() + 0, 54, "[ Outras Despesas ]" );
						imp.say( imp.pRow() + 0, 79, "[ Valor do IPI ]" );
						imp.say( imp.pRow() + 0, 104, "[ VALOR TOTAL ]" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 4, "***************" );
						imp.say( imp.pRow() + 0, 29, "***************" );
						imp.say( imp.pRow() + 0, 54, "***************" );
						imp.say( imp.pRow() + 0, 79, "***************" );
						imp.say( imp.pRow() + 0, 104, "***************" );
						imp.incPags();
					}
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 60, "DADOS ADICIONAIS" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, ( 116 - rs.getString( "DescPlanoPag" ).trim().length() ) / 2, "FORMA DE PAGAMENTO : " + rs.getString( "DescPlanoPag" ) );
					imp.eject();
				}
			}
			imp.fechaGravacao();

			con.commit();
		} catch ( Exception err ) {
			err.printStackTrace();
		}
		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}

	}

	private void imprimiGrafico( final ResultSet rs, final TYPE_PRINT bVisualizar ) {

		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "CPCOMPRA" ) );
		hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
		hParam.put( "CODCOMPRA", txtCodCompra.getVlrInteger() );

		//hParam.put( "LOGOEMP", );
		EmailBean email = Aplicativo.getEmailBean();
		email.setPara( txtEmailFor.getVlrString() );

		if ( classcp == null || "".equals( classcp.trim() ) ) {
			// dlGr = new FPrinterJob( "relatorios/ordemCompra.jasper", "Ordem de Compra", "", rs, hParam, this );
			dlGr = new FPrinterJob( "relatorios/ordemCompra.jasper", "Ordem de Compra", "", rs, hParam, this, email );

		}
		else {
			dlGr = new FPrinterJob( "layout/oc/" + classcp, "Ordem de Compra", "", rs, hParam, this, email );
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório Compras Geral!" + err.getMessage(), true, con, err );
			}
		}
	}

	private boolean consistSeq(Vector<Vector<Object>> datavector){
		int seq = 1;
		boolean result = true;

		for( Vector<Object> row : datavector){
			if( (Integer) row.elementAt( ECOL_ITENS.CODITCOMPRA.ordinal()  ) != seq ){
				result = false;
				break;
			}
			seq++;
		}

		return  result;
	}

	private boolean verificaBloq() {

		boolean retorno = false;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sSQL = null;
		int iCodCompra = 0;
		try {
			iCodCompra = txtCodCompra.getVlrInteger().intValue();
			if ( iCodCompra != 0 ) {
				sSQL = "SELECT BLOQCOMPRA FROM CPCOMPRA WHERE CODEMP=? AND CODFILIAL=? AND CODCOMPRA=? ";
				ps = con.prepareStatement( sSQL );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
				ps.setInt( 3, iCodCompra );
				rs = ps.executeQuery();

				if ( rs.next() ) {
					if ( rs.getString( 1 ).equals( "S" ) )
						retorno = true;
				}

				ps.close();
				con.commit();
			}
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro verificando bloqueido da compra!\n" + err.getMessage(), true, con, err );
		} finally {
			rs = null;
			ps = null;
			sSQL = null;
		}
		return retorno;
	}

	private void testaCodCompra() { // Traz o verdadeiro número do codCompra

		// através do generator do banco
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement( "SELECT * FROM SPGERANUM(?,?,?)" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			ps.setString( 3, "CP" );
			rs = ps.executeQuery();
			rs.next();
			txtCodCompra.setVlrString( rs.getString( 1 ) );
			// rs.close();
			// ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao confirmar código da Compra!\n" + err.getMessage(), true, con, err );
		}
	}

	public boolean testaCodLote() {

		boolean bRetorno = false;
		boolean bValido = false;
		String sSQL = "SELECT COUNT(*) FROM EQLOTE WHERE CODLOTE=? AND CODPROD=?" + " AND CODEMP=? AND CODFILIAL=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement( sSQL );
			ps.setString( 1, txtCodLote.getText().trim() );
			ps.setInt( 2, txtCodProd.getVlrInteger().intValue() );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, lcLote.getCodFilial() );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				if ( rs.getInt( 1 ) > 0 ) {
					bValido = true;
				}
			}
			rs.close();
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao consultar a tabela EQLOTE!\n" + err.getMessage(), true, con, err );
		}
		if ( !bValido ) {
			DLLote dl = new DLLote( this, txtCodLote.getText(), txtCodProd.getText(), txtDescProd.getText(), con );
			dl.setVisible( true );
			if ( dl.OK ) {
				bRetorno = true;
				txtCodLote.setVlrString( dl.getValor() );
				lcLote.carregaDados();
			}
			dl.dispose();
		}
		else {
			bRetorno = true;
		}
		return bRetorno;
	}
	private void fechaCompra(){

		String[] sValores = null;

		lcCampos.carregaDados();

		if ( !consistSeq( tab.getDataVector() ) ) {
			if( "S".equals( bloqseqicp ) ) {
				Funcoes.mensagemInforma( this, "Sequência de itens inválida !\nFavor ajustar em tabelas->ferramentas->Reorganização de seq. de itens" );
				return;
			} else if ( Funcoes.mensagemConfirma( this, "Sequência de itens inválida !\nDeseja Continuar?" ) == JOptionPane.NO_OPTION ){
				return;						
			} 
		}


		if( txtCodImp.getVlrInteger() > 0 && ( txtCodPaisDesembDI.getVlrInteger() == 0 || "".equals( txtSiglaUFDesembDI.getVlrString() ) ) ) {
			Funcoes.mensagemInforma( this, "Local de desembaraço em branco, preencha os campos Código do País e Sigla UF!!! " );
			return;
		}


		DLFechaCompra dl = new DLFechaCompra( con, txtCodCompra.getVlrInteger(), this, getVolumes(), ( nfecf.getHasNFE() && "E".equals( txtTipoModNota.getVlrString() ) ) );
		dl.setVisible( true );
		if ( dl.OK ) {
			sValores = dl.getValores();
			dl.dispose();
		}
		else {
			dl.dispose();
		}
		lcCampos.carregaDados();
		if ( sValores != null ) {
			lcCampos.edit();
			if ( sValores[ 4 ].equals( "S" ) ) {
				if ( txtTipoMov.getVlrString().equals( TipoMov.TM_PEDIDO_COMPRA.getValue() ) || txtTipoMov.getVlrString().equals( TipoMov.TM_COMPRA.getValue() ) || txtTipoMov.getVlrString().equals( TipoMov.TM_DEVOLUCAO_VENDA.getValue() )
						|| txtTipoMov.getVlrString().equals( TipoMov.TM_DEVOLUCAO_CONSIGNACAO.getValue() ) || txtTipoMov.getVlrString().equals( TipoMov.TM_DEVOLUCAO_REMESSA.getValue() ) ) {

					emiteNotaFiscal( "NF" );

				}

				else if ( "CP,CO,DI".indexOf( txtTipoMov.getVlrString() ) > -1 && "S".equals( cbSeqNfTipoMov.getVlrString() ) ) {
					emiteNotaFiscal( "NF" );
				}
				// else if ( txtTipoMov.getVlrString().equals( "SE" ) ) {
				// emiteNotaFiscal( "NS" );
				// }
				else {
					Funcoes.mensagemErro( this, "O tipo de movimento utilizado não permite impressão de nota!\n" + "Verifique o cadastro do tipo de movimento." );
					return;
				}
			}
			else if ( sValores[ 3 ].equals( "S" ) ) {
				imprimir( TYPE_PRINT.VIEW,txtCodCompra.getVlrInteger().intValue() );
			}

			// Gerando informacoes complementares (fisco)

			geraInfoCompl();

			lcCampos.post();

			if ( podeBloq ) {
				bloqCompra();
			}
		}
	}

	public void actionPerformed( ActionEvent evt ) {



		if ( evt.getSource() == btFechaCompra ) {
			fechaCompra();
		}
		else if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW,txtCodCompra.getVlrInteger().intValue() );
		}
		else if ( evt.getSource() == btImp ) {
			imprimir( TYPE_PRINT.PRINT, txtCodCompra.getVlrInteger().intValue() );
		}
		else if ( evt.getSource() == btObs ) {
			mostraObs( "CPCOMPRA", txtCodCompra.getVlrInteger().intValue() );
		}
		else if ( evt.getSource() == btBuscaRemessa ) {
			buscaRetornoRemessa();
		}
		else if ( evt.getSource() == btBuscaCompra ) {
			abreBuscaCompra();
		}
		else if ( evt.getSource() == btBuscaImportacao ) {
			abreBuscaImportacao(null, codtipomovim);
		}
		else if ( evt.getSource() == btBuscaCpComplementar ) {
			abreBuscaCpComplementar();
		}
		else if ( evt.getSource() == nav.btCancelar){
			desabilitaBotoes( false );
		}


		super.actionPerformed( evt );
	}

	private void abreBuscaCompra() {

		if ( !Aplicativo.telaPrincipal.temTela( "Busca pedido de compra" ) ) {
			DLBuscaPedCompra tela = new DLBuscaPedCompra( this, bloqprecoaprov );
			Aplicativo.telaPrincipal.criatela( "Busca pedido de compra", tela, con );
		}
	}

	private void abreBuscaCpComplementar() {

		if ( !Aplicativo.telaPrincipal.temTela( "Gera nota fiscal complementar de entrada" ) ) {
			FBuscaCpCompl tela = new FBuscaCpCompl( this, this );
			Aplicativo.telaPrincipal.criatela( "Gera nota fiscal complementar de entrada", tela, con );
		}
	}


	public void abreBuscaImportacao(Integer codimp, Integer codtipomov) {

		try {
			DLBuscaImportacao dl = null;
			if ( lcCampos.getStatus() == ListaCampos.LCS_INSERT || lcCampos.getStatus() == ListaCampos.LCS_NONE ) {

				if ( codtipomovim != null && codtipomovim > 0 ) {
					if(codimp == null) {
						dl = new DLBuscaImportacao( this, con );

						dl.setVisible( true );
					}

					if ( codimp != null || dl.OK ) {

						if ( lcCampos.getStatus() != ListaCampos.LCS_INSERT ) {
							lcCampos.insert( true );
						}

						if(codimp == null) {
							codimp = dl.getCodImp();
						}

						if ( codimp != null && codimp > 0 ) {

							txtCodTipoMov.setVlrInteger( codtipomov );
							lcTipoMov.carregaDados();

							txtCodImp.setVlrInteger( codimp );

							lcImportacao.carregaDados();

							txtCodFor.setVlrInteger( txtCodForImp.getVlrInteger() );
							txtCodPlanoPag.setVlrInteger( txtCodPlanoPagImp.getVlrInteger() );

							lcFor.carregaDados();
							lcPlanoPag.carregaDados();

						}

					}

					if(dl != null){
						dl.dispose();
					}
				}
				else {
					Funcoes.mensagemInforma( this, "Não existe tipo de movimento configurado para importação!\nVerifique as preferências gerais aba compra." );
				}
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	public void focusGained( FocusEvent fevt ) {

	}

	public void focusLost( FocusEvent fevt ) {

		//MathContext mcFin = new MathContext( casasDecFin, RoundingMode.HALF_EVEN );
		MathContext mcPerc= new MathContext( 5, RoundingMode.HALF_EVEN   );

		if ( fevt.getSource() == txtPercDescItCompra ) {
			if ( txtPercDescItCompra.getVlrBigDecimal().floatValue() <= 0 ) {
				txtVlrDescItCompra.setAtivo( true );
			}
			else {

				BigDecimal vlrdesconto = txtVlrProdItCompra.getVlrBigDecimal().multiply( txtPercDescItCompra.getVlrBigDecimal().divide( new BigDecimal( 100 ) , mcPerc ) );

				txtVlrDescItCompra.setVlrBigDecimal( vlrdesconto );

				// txtVlrDescItCompra.setVlrBigDecimal( new BigDecimal( Funcoes.arredDouble( txtVlrProdItCompra.doubleValue() * txtPercDescItCompra.doubleValue() / 100, casasDecFin ) ) );
				calcVlrProd();
				calcImpostos( true );
				txtVlrDescItCompra.setAtivo( false );
			}
		}

		else if ( fevt.getSource() == txtVlrIPIItCompra ) {
			//			adicIPI();
		}
		else if ( fevt.getSource() == txtAliqISSItCompra ) {
			calcISS();
		}

		else if ( fevt.getSource() == txtVlrDescItCompra ) {
			if ( txtVlrDescItCompra.getVlrBigDecimal().floatValue() <= 0 ) {
				txtPercDescItCompra.setAtivo( true );
			}
			else if ( txtVlrDescItCompra.getAtivo() ) {

				BigDecimal percdesconto = ( txtVlrDescItCompra.getVlrBigDecimal().multiply( new BigDecimal( 100 ) ) ).divide( txtVlrProdItCompra.getVlrBigDecimal(), mcPerc);

				txtPercDescItCompra.setVlrBigDecimal( percdesconto );

				calcVlrProd();
				calcImpostos( true );

				txtPercDescItCompra.setAtivo( false );

			}
		}

		else if ( fevt.getSource() == txtPercICMSItCompra ) {
			calcVlrProd();
			calcImpostos( false ); 
		}

		else if ( fevt.getSource() == txtPercICMSStItCompra ) {
			if ( txtPercICMSStItCompra.getVlrBigDecimal().floatValue() <= 0 ) {

				txtVlrICMSStItCompra.setAtivo( true );
			}
			else {

				BigDecimal vlrICMSSt = txtVlrBaseICMSStItCompra.getVlrBigDecimal().multiply( txtPercICMSStItCompra.getVlrBigDecimal().divide( new BigDecimal( 100 ), mcPerc ) );

				txtVlrICMSStItCompra.setVlrBigDecimal( vlrICMSSt );
				txtVlrICMSStItCompra.setAtivo( false );
			}
		}

		else if ( fevt.getSource() == txtVlrICMSStItCompra ) {
			if ( txtVlrICMSStItCompra.getVlrBigDecimal().floatValue() <= 0 ) {
				txtPercICMSStItCompra.setAtivo( true );

			}
			else {

				BigDecimal vlrPercICMSSt = txtVlrICMSStItCompra.getVlrBigDecimal().divide( txtVlrBaseICMSStItCompra.getVlrBigDecimal() ).multiply( new BigDecimal( 100 ), mcPerc );

				txtPercICMSStItCompra.setVlrBigDecimal( vlrPercICMSSt );
				txtPercICMSStItCompra.setAtivo( false );
			}
		}

		else if ( ( fevt.getSource() == txtQtdItCompra ) || ( fevt.getSource() == txtPrecoItCompra ) || ( fevt.getSource() == txtCodNat ) ) {
			calcVlrProd();
			calcImpostos( true );
			habilitaSerie();
		}
	}

	public void keyPressed( KeyEvent kevt ) {

		if ( kevt.getKeyCode() == KeyEvent.VK_ENTER ) {



			if( kevt.getSource() == txtChaveNfe ) {
				// ultimo
				// campo do itcompra.
				if ( lcCampos.getStatus() == ListaCampos.LCS_INSERT || lcCampos.getStatus() == ListaCampos.LCS_EDIT ) {
					lcCampos.post();
					lcCampos.carregaDados();

					lcDet.cancel( true );
					lcDet.insert( true );
					txtRefProd.requestFocus();
					//ZERANDO O Código do teclado para evitar replicação do evento.
					kevt.setKeyCode( 0 );
				}
				else if ( lcDet.getStatus() == ListaCampos.LCS_EDIT ) {
					lcCampos.post();
					txtCodItCompra.requestFocus();
					//ZERANDO O Código do teclado para evitar replicação do evento.
					kevt.setKeyCode( 0 );
				}

			}	
			else if ( "N".equals( abaFisc )  && kevt.getSource() == txtNroOrdemCompra ) {// Talvez este possa ser o
				// ultimo
				// campo do itcompra.
				if ( lcCampos.getStatus() == ListaCampos.LCS_INSERT || lcCampos.getStatus() == ListaCampos.LCS_EDIT ) {
					lcCampos.post();
					lcCampos.carregaDados();

					lcDet.cancel( true );
					lcDet.insert( true );
					txtRefProd.requestFocus();
					//ZERANDO O Código do teclado para evitar replicação do evento.
					kevt.setKeyCode( 0 );
				}
				else if ( lcDet.getStatus() == ListaCampos.LCS_EDIT ) {
					lcCampos.post();
					txtCodItCompra.requestFocus();
					//ZERANDO O Código do teclado para evitar replicação do evento.
					kevt.setKeyCode( 0 );
				}
			} else if( "S".equals( abaFisc )  && kevt.getSource() == txtNroOrdemCompra ) {
				tpnCab.setSelectedIndex( 1 );
				this.txtChaveNfe.requestFocus();
			}

			else if( kevt.getSource() == txtCodNat){
				// É o último se o número de serie e o lote não estiver habilitado.
				if ( ( "N".equals( txtSerieProd.getVlrString() ) || txtQtdItCompra.getVlrBigDecimal().floatValue() > 1 ) && "N".equals( txtCLoteProd.getVlrString() ) ) {
					tpnAbas.setSelectedIndex( 1 );
					tpnAbas.doLayout();
					txtVlrBaseICMSItCompra.requestFocus();
				}

			}

			else if ( kevt.getSource() == txtCodLote ) {
				// É o último se estiver habilitado.
				if ( txtCodLote.isEditable() && !txtNumSerie.isEditable() ) {
					if ( lcDet.getStatus() == ListaCampos.LCS_INSERT ) {
						tpnAbas.setSelectedIndex( 1 );
						tpnAbas.doLayout();
						txtVlrBaseICMSItCompra.requestFocus();
					}

					else if ( lcDet.getStatus() == ListaCampos.LCS_EDIT ) {
						lcDet.post();
						tpnAbas.setSelectedIndex( 1 );
						tpnAbas.doLayout();
						txtVlrBaseICMSItCompra.requestFocus();
						//ZERANDO O Código do teclado para evitar replicação do evento.
						kevt.setKeyCode( 0 );
					}

				}
			}
			else if ( kevt.getSource() == txtNumSerie ) {
				// É o último se estiver habilitado.
				if ( txtNumSerie.isEditable() ) {
					if ( lcDet.getStatus() == ListaCampos.LCS_INSERT ) {
						tpnAbas.setSelectedIndex( 1 );
						tpnAbas.doLayout();
						txtVlrBaseICMSItCompra.requestFocus();
					}

					else if ( lcDet.getStatus() == ListaCampos.LCS_EDIT ) {
						lcDet.post();
						tpnAbas.setSelectedIndex( 1 );
						tpnAbas.doLayout();
						txtVlrBaseICMSItCompra.requestFocus();
						//ZERANDO O Código do teclado para evitar replicação do evento.
						kevt.setKeyCode( 0 );
					}

				}
			}
			else if ( kevt.getSource() == txtVlrIPIItCompra ) {
				// É o último se o custo/serie e lote não estiver habilitado.
				if ( !habilitaCusto && ( "N".equals( txtSerieProd.getVlrString() ) || txtQtdItCompra.getVlrBigDecimal().floatValue() > 1 ) && txtCodImp.getVlrInteger() <= 0 ) {
					if ( lcDet.getStatus() == ListaCampos.LCS_INSERT ) {
						postaNovoItem();
					}
					else if ( lcDet.getStatus() == ListaCampos.LCS_EDIT ) {
						lcDet.post();
						tpnAbas.setSelectedIndex( 0 );
						tpnAbas.doLayout();
						txtCodItCompra.requestFocus();
					}
				}
				else {
					tpnAbas.setSelectedIndex( 2 );
					tpnAbas.doLayout();
					if(txtCodImp.getVlrInteger() <= 0) {
						txtCustoItCompra.requestFocus();
					} else {
						txtNAdicao.requestFocus();
					}
				}
			}

			else if ( kevt.getSource() == txtCustoItCompra ) {
				// É o último se estiver habilitado.
				//	if ( habilitaCusto && !ehImportacao() ) {
				if ( habilitaCusto ) {
					if ( lcDet.getStatus() == ListaCampos.LCS_INSERT ) {
						postaNovoItem();
					}
					else if ( lcDet.getStatus() == ListaCampos.LCS_EDIT ) {
						lcDet.post();
						tpnAbas.setSelectedIndex( 0 );
						tpnAbas.doLayout();
						txtCodItCompra.requestFocus();
					}
				}
			}
			else if ( kevt.getSource() == txtPercIPIItCompra ) {
				if ( txtPercIPIItCompra.floatValue() > 0 ) {
					calcIpi( true );
				}
			}
			else if ( kevt.getSource() == txtVlrIPIItCompra ) {
				if ( txtVlrIPIItCompra.floatValue() > 0 ) {
					calcIpi( false );
				}
			}
			else if ( kevt.getSource() == txtDescDI && ehImportacao() ) {
				postaNovoItem();
			}
		}
		else if ( kevt.getKeyCode() == KeyEvent.VK_F4 ) {
			btFechaCompra.doClick();
		}
		if ( kevt.getSource() == txtRefProd ) {
			lcDet.edit();
		}


		super.keyPressed( kevt );
	}

	private void postaNovoItem() {

		lcDet.post();
		lcDet.limpaCampos( true );
		lcDet.setState( ListaCampos.LCS_NONE );
		tpnAbas.setSelectedIndex( 0 );
		tpnAbas.doLayout();
		lcDet.edit();
		if ( comref ) {
			txtRefProd.requestFocus();
		}
		else {
			txtCodProd.requestFocus();
		}


	}

	private boolean ehImportacao() {

		return TipoMov.TM_NOTA_FISCAL_IMPORTACAO.getValue().equals( txtTipoMov.getVlrString() );
	}

	public void keyReleased( KeyEvent kevt ) {

		super.keyReleased( kevt );
	}

	public void keyTyped( KeyEvent kevt ) {

		super.keyTyped( kevt );
	}

	public void beforeCarrega( CarregaEvent cevt ) {

		txtVlrDescItCompra.setAtivo( true );
		txtPercICMSStItCompra.setAtivo( true );
		txtVlrICMSStItCompra.setAtivo( true );

		if ( lcDet.getStatus() != ListaCampos.LCS_INSERT ) {
			if ( ( cevt.getListaCampos() == lcProd ) || ( cevt.getListaCampos() == lcProd2 ) ) {
				cevt.getListaCampos().cancLerCampo( 6, true ); // Código da Classificação Fiscal
			}
		}
		else {
			if ( ( cevt.getListaCampos() == lcProd ) || ( cevt.getListaCampos() == lcProd2 ) ) {
				cevt.getListaCampos().cancLerCampo( 6, false ); // Código da Classificação Fiscal

			}
		}
		if ( cevt.getListaCampos() == lcLote ) {
			if ( txtCodLote.getText().trim().length() == 0 ) {
				cevt.cancela(); // Cancela o carregaDados do lcLote para não
				// zerar o codprod.
			}
		}
	}

	public BigDecimal getTotalNota() {

		BigDecimal totalnota = new BigDecimal(0);

		try {

			totalnota = txtVlrBrutCompra.getVlrBigDecimal();
			totalnota = totalnota.add( txtVlrIPICompra.getVlrBigDecimal() );
			totalnota = totalnota.add( txtVlrICMSSTCompra.getVlrBigDecimal() );
			totalnota = totalnota.add( txtVlrAdicCompra.getVlrBigDecimal() );
			if(!"S".equals( totcpsfrete )){
				totalnota = totalnota.add( txtVlrFreteCompra.getVlrBigDecimal() );
			}
			totalnota = totalnota.subtract( txtVlrDescCompra.getVlrBigDecimal() );

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return totalnota;

	}

	public void afterCarrega( CarregaEvent cevt ) {

		if ( cevt.getListaCampos() == lcProd || cevt.getListaCampos() == lcProd2 ) {
			if ( "S".equals( txtCLoteProd.getVlrString() ) || "S".equals( txtSerieProd.getVlrString() ) || ehImportacao() ) {

				redimensionaDet( 130 );

				txtCodLote.setEditable( true );
				txtNumSerie.setEditable( true );

				if ( "S".equals( txtCLoteProd.getVlrString() ) ) {
					txtCodLote.setEditable( true );
				}
				else {
					txtCodLote.setEditable( false );
				}
				habilitaSerie();

			}
			else {

				redimensionaDet( 130 );

				txtCodLote.setEditable( false );
				txtNumSerie.setEditable( false );

			}

			lcAlmoxProd.carregaDados();

			if(TipoProd.SERVICO.getValue().equals( txtTipoProd.getVlrString() )) {

				txtAliqISSItCompra.setEditable( true );
				txtVlrISSItCompra.setEditable( true );

			}
			else {

				txtAliqISSItCompra.setEditable( false );
				txtVlrISSItCompra.setEditable( false );

			}

			if ( buscaVlrUltCompra ) {
				getVlrUltimaCompra();
			}


		}
		else if ( cevt.getListaCampos() == lcFisc && lcDet.getStatus() == ListaCampos.LCS_INSERT ) {
			getCFOP();
		}
		else if ( cevt.getListaCampos() == lcDet ) {
			String s = txtCodCompra.getText();
			lcCompra2.carregaDados(); // Carrega os Totais
			txtCodCompra.setVlrString( s );
			habilitaSerie();
			lcLFItCompra.carregaDados();


		}
		else if ( cevt.getListaCampos() == lcCampos ) {
			String s = txtCodCompra.getText();
			lcCompra2.carregaDados(); // Carrega os Totais
			txtCodCompra.setVlrString( s );

			if ( buscagenericaprod ) {

				if ( comref ) {
					txtRefProd.setBuscaGenProd( new DLCodProd( con, null, txtCodFor.getVlrInteger() ) );
				}
				else {
					txtCodProd.setBuscaGenProd( new DLCodProd( con, null, txtCodFor.getVlrInteger() ) );
				}
			}

			if ("S".equals(consistChaveNFE)) {
				// Verifica se a emissão é própria e se a chave tem o tamanho correto, nesta caso o campo é desabilitado para evitar alteração na chave
				if ("S".equals( txtEmitCompra.getVlrString() ) && txtChaveNfe.getVlrString().length()==TAMANHOCHAVE) {
					txtChaveNfe.setEnabled( false );
				} else {
					txtChaveNfe.setEnabled( true );
				}
			}


			if(!( lcCampos.getStatus() == ListaCampos.LCS_INSERT )) {
				desabilitaBotoes( false );
			}
		}
		else if ( cevt.getListaCampos() == lcCompra2 ) {

			txtVlrTotalNota.setVlrBigDecimal( getTotalNota() ); 

		}
		else if ( cevt.getListaCampos() == lcFor ) {
			if ( buscagenericaprod ) {

				if ( comref ) {
					txtRefProd.setBuscaGenProd( new DLCodProd( con, null, txtCodFor.getVlrInteger() ) );
				}
				else {
					txtCodProd.setBuscaGenProd( new DLCodProd( con, null, txtCodFor.getVlrInteger() ) );
				}
			}

		}
		else if ( cevt.getListaCampos() == lcSerie ) {
			if ( lcCampos.getStatus() == ListaCampos.LCS_INSERT && "S".equals( cbSeqNfTipoMov.getVlrString() ) ) {
				// Busca de sequencia pela LFSEQSERIE
				try {

					SeqSerie ss = new SeqSerie( txtSerieCompra.getVlrString() );
					txtDocSerie.setVlrInteger( ss.getDocserie() );

				} catch ( Exception e ) {
					e.printStackTrace();
				}
				txtDocCompra.setVlrInteger( new Integer( txtDocSerie.getVlrInteger().intValue() + 1 ) );
			}
		}
		else if ( cevt.getListaCampos() == lcNat ) {
			if ( cevt.ok && lcDet.getStatus() == ListaCampos.LCS_INSERT ) {
				getICMS();
			}
		}
		else if ( cevt.getListaCampos() == lcTipoMov ) {

			//System.out.println(txtDesBloqCV.getVlrString() + "ENTROU AQUI");
			// Verifica se possui código de importação, se tiver, não deve calcular os tributos pela tela de compra. (Já foram calculados na tela de importação.
			if( "S".equals( txtDesBloqCV.getVlrString() ))	{
				txtAceitaVenda.setVlrString("V");
			} else {
				txtAceitaVenda.setVlrString("N");
			}

			if ( txtCodImp.getVlrInteger() > 0 ) {
				txtCalcTrib.setVlrString( "N" );
			}

			// Corrigido em 29/07/2011 por Robson, pois a lógica estava invertida.
			// Corrigido em 17/11/2011 por Anderson, pois a lógica inicial estava correta, se a não deve ser emitida a nota, os tributos não devem ser calculados
			else if ( "N".equals( txtEmitCompra.getVlrString() ) ) { 
				txtCalcTrib.setVlrString( "N" );
			}
			else {
				txtCalcTrib.setVlrString( "S" );
			}

			if ( "S".equals( cbSeqNfTipoMov.getVlrString() ) ) {
				txtDocCompra.setAtivo( false );
			}
			else {
				txtDocCompra.setAtivo( true );
			}

			btBuscaRemessa.setVisible( "DR".equals( txtTipoMov.getVlrString() ) );


			if ( TipoMov.TM_NOTA_FISCAL_IMPORTACAO.getValue().equals( txtTipoMov.getVlrString() ) ) {

				redimensionaDet( 130 );

				//				txtNAdicao.setVisible( true );
				txtNAdicao.setEditable( true );
				//				lbNAdicao.setVisible( true );
				//				lbSeqAdic.setVisible( true );
				//				txtDescDI.setVisible( true );
				txtDescDI.setEditable( true );
				//				txtSeqAdic.setVisible( true );
				txtSeqAdic.setEditable( true );
				//				lbDescDI.setVisible( true );
				txtVlrII.setEditable( true );

				btBuscaImportacao.setVisible( true );

			}
			else {
				/*
				txtNAdicao.setVisible( false );
				lbNAdicao.setVisible( false );
				lbSeqAdic.setVisible( false );
				txtDescDI.setVisible( false );
				txtSeqAdic.setVisible( false );
				lbDescDI.setVisible( false );
				 */

				txtNAdicao.setEditable( false );
				//				lbNAdicao.setVisible( false );
				//				lbSeqAdic.setVisible( false );
				txtDescDI.setEditable( false );
				txtSeqAdic.setEditable( false );
				txtVlrII.setEditable( false );
				//				lbDescDI.setVisible( false );

			}

		}
		else if ( cevt.getListaCampos() == lcAlmoxProd && lcDet.getStatus() == ListaCampos.LCS_INSERT ) {
			if ( habilitaCusto ) {
				getCustoProd();
			}
		}
		/*
		 * else if ( cevt.getListaCampos() == lcModNota ) { // Caso seja nota fiscal eletrônica deve aparecer o campo de para chave da nota fiscal
		 * 
		 * if( "E".equals( txtTipoModNota.getVlrString() ) ) { lbChaveNfe.setText( "Chave de acesso NFe" ); setAltCab( 195 ); setSize( 770, 535 ); } else { lbChaveNfe.setText( "" ); setAltCab( 160 ); setSize( 770, 500 ); }
		 * 
		 * }
		 */

		String statuscompra = txtStatusCompra.getVlrString().trim();

		if ( statuscompra.length() > 0 && ( statuscompra.equals( "C2" ) || statuscompra.equals( "C3" ) ) ) {
			lbStatus.setText( "EMITIDA" );
			lbStatus.setBackground( new Color( 45, 190, 60 ) );
			lbStatus.setVisible( true );
		}
		else if ( verificaBloq() ) {
			lbStatus.setText( "BLOQUEADA" );
			lbStatus.setBackground( Color.BLUE );
			lbStatus.setVisible( true );
		}
		else if ( statuscompra.length() > 0 && statuscompra.substring( 0, 1 ).equals( "X" ) ) {
			lbStatus.setText( "CANCELADA" );
			lbStatus.setBackground( Color.RED );
			lbStatus.setVisible( true );
		}
		else if ( statuscompra.length() > 0 && statuscompra.equals( "P2" ) ) {
			lbStatus.setText( "EM ABERTO" );
			lbStatus.setBackground( Color.ORANGE );
			lbStatus.setVisible( true );
		}
		else if ( statuscompra.length() > 0 && statuscompra.equals( "P3" ) ) {
			lbStatus.setText( "FINALIZADO" );
			lbStatus.setBackground( new Color( 45, 190, 60 ) );
			lbStatus.setVisible( true );
		}
		else if ( statuscompra.equals( "P1" ) ) {
			lbStatus.setText( "PENDENTE" );
			lbStatus.setBackground( Color.ORANGE );
			lbStatus.setVisible( true );
		}
		else if ( statuscompra.equals( "EP" ) ) {
			lbStatus.setText( "ENTREGUE PARCIAL" );
			lbStatus.setBackground( Color.BLUE );
			lbStatus.setVisible( true );
		}
		else if ( statuscompra.equals( "ET" ) ) {
			lbStatus.setText( "ENTREGUE TOTAL" );
			lbStatus.setBackground( new Color( 45, 190, 60 ) );
			lbStatus.setVisible( true );
		}
		else if ( "".equals( statuscompra ) || statuscompra == null ) {
			lbStatus.setForeground( Color.WHITE );
			lbStatus.setBackground( Color.BLACK );
			lbStatus.setHorizontalAlignment( SwingConstants.CENTER );
			lbStatus.setOpaque( true );
			lbStatus.setText( "NÃO SALVO" );
		}
		else {
			lbStatus.setText( statuscompra );
			lbStatus.setBackground( Color.DARK_GRAY );
			lbStatus.setVisible( true );
		}

	}

	private void habilitaSerie() {

		if ( "S".equals( txtSerieProd.getVlrString() ) && txtQtdItCompra.getVlrBigDecimal().compareTo( new BigDecimal( 1 ) ) == 0 ) {

			txtNumSerie.setEditable( true );
		}
		else {
			txtNumSerie.setEditable( false );
		}

	}

	public void beforeInsert( InsertEvent e ) {

	}

	public void afterInsert( InsertEvent e ) {

		if ( e.getListaCampos() == lcCampos ) {

			txtDtEntCompra.setVlrDate( new Date() );
			txtDtEmitCompra.setVlrDate( new Date() );

			desabilitaBotoes( true );	
		} 
	}

	private void desabilitaBotoes(boolean flag){
		btBuscaCpComplementar.setEnabled( flag );
		btBuscaImportacao.setEnabled( flag );
		btBuscaCompra.setEnabled( flag );
	}

	private void geraItensImportacao() {
		daoimp.geraItensCompras( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "CPITCOMPRA" ), txtCodCompra.getVlrInteger(), txtCodImp.getVlrInteger(), 
				Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "CPFORNECED" ),txtCodFor.getVlrInteger(), txtCodTipoMov.getVlrInteger(), utilizatbcalcca );
		lcCampos.carregaDados();
	}


	/*private void geraItensImportacao() {

		PreparedStatement ps_imp = null;
		PreparedStatement ps_trib = null;
		PreparedStatement ps_comp = null;

		ResultSet rs1 = null;
		ResultSet rs2 = null;

		Integer iparam = null;

		try {

			// Query para percorrer os itens de importação e inserir na tabela de itens de compra

			ps_imp = daoimp.getStatementItensImportacao(utilizatbcalcca);

			ps_imp.setInt( 1, Aplicativo.iCodEmp );
			ps_imp.setInt( 2, ListaCampos.getMasterFilial( "CPIMPORTACAO" ) );
			ps_imp.setInt( 3, txtCodImp.getVlrInteger() );

			rs1 = ps_imp.executeQuery();

			// Gerando statement para inserção na CPITCOMPRA;

			ps_comp = daoimp.getStatementInsertCPItCompra();

			while ( rs1.next() ) {

				// Inserindo os ítens de importação

				iparam = 1;

				ps_comp.setString( iparam++, "S" );

				ps_comp.setInt( iparam++, Aplicativo.iCodEmp );
				ps_comp.setInt( iparam++, ListaCampos.getMasterFilial( "CPITCOMPRA" ) );
				ps_comp.setInt( iparam++, txtCodCompra.getVlrInteger() );
				ps_comp.setInt( iparam++, rs1.getInt( "CODITIMP" ) );

				ps_comp.setInt( iparam++, rs1.getInt( "codemppd" ) );
				ps_comp.setInt( iparam++, rs1.getInt( "codfilialpd" ) );
				ps_comp.setInt( iparam++, rs1.getInt( "codprod" ) );
				ps_comp.setString( iparam++, rs1.getString( "refprod" ) );

				ps_comp.setInt( iparam++, Aplicativo.iCodEmp );
				ps_comp.setInt( iparam++, ListaCampos.getMasterFilial( "LFNATOPER" ) );

				String codnat = buscaCFOP( rs1.getInt( "codprod" ), txtCodFor.getVlrInteger(), txtCodTipoMov.getVlrInteger(), rs1.getInt( "coditfisc" ), con );

				ps_comp.setString( iparam++, codnat );

				ps_comp.setInt( iparam++, Aplicativo.iCodEmp );
				ps_comp.setInt( iparam++, ListaCampos.getMasterFilial( "EQALMOX" ) );
				ps_comp.setInt( iparam++, rs1.getInt( "codalmox" ) );

				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "qtd" ) );
				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "precoitcompra" ) );
				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "vlrliqitcompra" ) );
				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "vlrproditcompra" ) );

				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "vlrbaseicms" ) );
				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "aliqicmsuf" ) );
				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "vlricmsitcompra" ) );
				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "vlrfreteitcompra" ) );

				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "vlrbaseipiitcompra" ) );
				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "aliqipi" ) );
				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "vlripi" ) );

				ps_comp.setInt( iparam++, Aplicativo.iCodEmp );
				ps_comp.setInt( iparam++, ListaCampos.getMasterFilial( "LFCLFISCAL" ) );
				ps_comp.setString( iparam++, rs1.getString( "codfisc" ) );
				ps_comp.setInt( iparam++, rs1.getInt( "coditfisc" ) );

				ps_comp.setInt( iparam++, rs1.getInt( "nadicao" ) );
				ps_comp.setInt( iparam++, rs1.getInt( "seqadic" ) );
				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "vlradicitcompra" ) );
				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "custoitcompra" ) );
				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "vlrii" ) );
				ps_comp.setString( iparam++, rs1.getString( "adicicmstotnota" ) );
				ps_comp.setString( iparam++, rs1.getString( "vlritdespad" ) );

				ps_comp.execute();

			}

			con.commit();

			// Query para percorrer os itens de importação e inserir na tabela LFITCOMPRA

			ps_imp = daoimp.getStatementItensImportacao(utilizatbcalcca);

			ps_imp.setInt( 1, Aplicativo.iCodEmp );
			ps_imp.setInt( 2, ListaCampos.getMasterFilial( "CPIMPORTACAO" ) );
			ps_imp.setInt( 3, txtCodImp.getVlrInteger() );

			rs2 = ps_imp.executeQuery();

			// Gerando statement para inserção na CPITCOMPRA;

			ps_trib = daoimp.getStatementInsertLFItCompra();

			while ( rs2.next() ) {

				// Inserção da LFITCOMPRA

				iparam = 1;

				ps_trib.setInt( iparam++, Aplicativo.iCodEmp );
				ps_trib.setInt( iparam++, ListaCampos.getMasterFilial( "LFITCOMPRA" ) );
				ps_trib.setInt( iparam++, txtCodCompra.getVlrInteger() );
				ps_trib.setInt( iparam++, rs2.getInt( "CODITIMP" ) );

				ps_trib.setInt( iparam++, rs2.getInt( "codempsp" ) );
				ps_trib.setInt( iparam++, rs2.getInt( "codfilialsp" ) );
				ps_trib.setString( iparam++, rs2.getString( "codsittribpis" ) );
				ps_trib.setString( iparam++, rs2.getString( "impsittribpis" ) );
				ps_trib.setBigDecimal( iparam++, rs2.getBigDecimal( "vlrbasepis" ) );
				ps_trib.setBigDecimal( iparam++, rs2.getBigDecimal( "aliqpis" ) );
				ps_trib.setBigDecimal( iparam++, rs2.getBigDecimal( "vlrpis" ) );

				ps_trib.setInt( iparam++, rs2.getInt( "codempsc" ) );
				ps_trib.setInt( iparam++, rs2.getInt( "codfilialsc" ) );
				ps_trib.setString( iparam++, rs2.getString( "codsittribcof" ) );
				ps_trib.setString( iparam++, rs2.getString( "impsittribcof" ) );
				ps_trib.setBigDecimal( iparam++, rs2.getBigDecimal( "vlrbasecofins" ) );
				ps_trib.setBigDecimal( iparam++, rs2.getBigDecimal( "aliqcofins" ) );
				ps_trib.setBigDecimal( iparam++, rs2.getBigDecimal( "vlrcofins" ) );

				ps_trib.setInt( iparam++, rs2.getInt( "codempsi" ) );
				ps_trib.setInt( iparam++, rs2.getInt( "codfilialsi" ) );
				ps_trib.setString( iparam++, rs2.getString( "codsittribipi" ) );
				ps_trib.setString( iparam++, rs2.getString( "impsittribipi" ) );

				ps_trib.setInt( iparam++, rs2.getInt( "modbcicms" ) );
				ps_trib.setBigDecimal( iparam++, rs2.getBigDecimal( "redfisc" ) );
				ps_trib.setString( iparam++, rs2.getString( "origfisc" ) );
				ps_trib.setString( iparam++, rs2.getString( "codtrattrib" ) );

				ps_trib.setBigDecimal( iparam++, rs2.getBigDecimal( "vlrbaseii" ) );
				ps_trib.setBigDecimal( iparam++, rs2.getBigDecimal( "aliqii" ) );
				ps_trib.setBigDecimal( iparam++, rs2.getBigDecimal( "vlrii" ) );

				ps_trib.setBigDecimal( iparam++, rs2.getBigDecimal( "vlricmsdiferido" ) );
				ps_trib.setBigDecimal( iparam++, rs2.getBigDecimal( "vlricmsrecolhimento" ) );
				ps_trib.setBigDecimal( iparam++, rs2.getBigDecimal( "vlricmscredpresum" ) );

				ps_trib.execute();

			}

			con.commit();

			ps_comp.close();
			ps_trib.close();
			ps_imp.close();

			rs1.close();
			rs2.close();

			lcCampos.carregaDados();

		} catch ( Exception e ) {
			Funcoes.mensagemErro( this, "Erro ao gerar itens de compra!", false, e );
			e.printStackTrace();
		}

	}*/

	public void afterPost( PostEvent pevt ) {

		String s = txtCodCompra.getText();
		lcCompra2.carregaDados(); // Carrega os Totais
		txtCodCompra.setVlrString( s );

		if ( pevt.getListaCampos() == lcDet && novo ) {
			if ( habconvcp ) {
				geraOpConversao();
			}

		}
		if ( pevt.getListaCampos() == lcDet ) {
			if ( txtSerieProd.getVlrString().equals( "S" ) && txtQtdItCompra.getVlrBigDecimal().floatValue() > 1 ) {
				testaNumSerie();
			}
		}

		if ( pevt.getListaCampos() == lcCampos && novo ) {
			if ( txtCodImp.getVlrInteger() != null && txtCodImp.getVlrInteger() > 0 ) {

				geraItensImportacao();

			}
		}

		novo = false;
	}

	private boolean testaNumSerie() {

		boolean ret = false;

		try {

			numserie = new NumSerie( this, lcDet, txtCodCompra.getVlrInteger(), txtCodItCompra.getVlrInteger(), txtCodProd.getVlrInteger(), txtDescProd.getVlrString(), txtNumSerie.getVlrString(), txtNumSerie.isEditable() );

			ret = numserie.testaNumSerie( DLSerieGrid.TIPO_COMPRA );

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return ret;

	}

	public void beforePost( PostEvent pevt ) {

		boolean tem = false;
		if ( pevt.getListaCampos() == lcDet ) {
			txtRefProd.setVlrString( txtRefProd.getText() ); // ??? que que é isso.
			if ( "".equals( txtPercICMSStItCompra.getText() ) ) {
				txtPercICMSStItCompra.setVlrBigDecimal( new BigDecimal(0) );
			}
			if ( txtCLoteProd.getVlrString().equals( "S" ) ) {
				if ( !testaCodLote() ) {
					txtCodLote.requestFocus();
					throw new RuntimeException();
					// pevt.cancela();
				}
			}
			if ( txtSerieProd.getVlrString().equals( "S" ) && txtQtdItCompra.getVlrBigDecimal().floatValue() == 1 ) {
				if ( !testaNumSerie() ) {
					pevt.cancela();
				}
			}
			if ( getGuiaTraf() ) {
				DLGuiaTraf dl = new DLGuiaTraf( txtCodCompra.getVlrInteger(), txtCodItCompra.getVlrInteger(), con );
				tem = dl.getGuiaTraf();
				dl.setVisible( true );
				if ( dl.OK ) {
					if ( tem ) {
						dl.updatGuiaTraf();
					}
					else {
						dl.insertGuiaTraf();
					}
				}
				else {
					pevt.cancela();
				}
			}

			if ( txtVenctoLote.getVlrDate() != null && txtVenctoLote.getVlrDate().compareTo( new Date() ) < 0 ) {
				boolean permiteRevalidarLote = this.permiteRevalidarLote( con.getConnection() );

				if ( permiteRevalidarLote ) {
					int retorno = Funcoes.mensagemConfirma( this, "Lote Vencido. Deseja Revalidar?" );

					if ( retorno == JOptionPane.YES_OPTION ) {
						DLLote dl = new DLLote( this, txtCodLote.getText(), txtCodProd.getText(), txtDescProd.getText(), txtVenctoLote.getVlrDate(), con );
						dl.setVisible( true );
						if ( dl.OK ) {
							txtCodLote.setVlrString( dl.getValor() );
							lcLote.carregaDados();
						}
						else {
							txtCodLote.requestFocus();
							throw new RuntimeException();
						}
						dl.dispose();

					}
					else {
						txtCodLote.requestFocus();
						throw new RuntimeException();
					}
				}
				else {
					Funcoes.mensagemInforma( this, "Lote Vencido." );
					txtCodLote.requestFocus();
					throw new RuntimeException();
				}
				// pevt.cancela();
			}

			adicIPI();


		}

		if ( pevt.getListaCampos() == lcCampos ) {


			if ( lcCampos.getStatus() == ListaCampos.LCS_INSERT ) {
				testaCodCompra();

				if ( txtCodImp.getVlrInteger() > 0 ) {
					txtCalcTrib.setVlrString( "N" );
				}
				// txtStatusCompra.setVlrString( "*" );
			}

			if ( ( lcCampos.getStatus() == ListaCampos.LCS_INSERT ) || ( lcCampos.getStatus() == ListaCampos.LCS_EDIT ) ) {

				/*
				 * Caso a opção Não permitir data emissão/entrada maior que a data atual aba Compras
				 * no Preferências gerais estiver selecionada realiza a consistência abaixo. 
				 */
				
				if (npermitdtmaior) {
					if ( txtDtEmitCompra.getVlrDate().after( new Date() ) ) {
						Funcoes.mensagemErro( this, "A data de emissão não pode ser posterior à data de hoje!" );
						this.txtDtEmitCompra.requestFocus();
						pevt.cancela();
						return;
					}

					if ( txtDtEntCompra.getVlrDate().after( new Date() ) ) {
						Funcoes.mensagemErro( this, "A data de entrada não pode ser posterior à data de hoje!" );
						this.txtDtEntCompra.requestFocus();
						pevt.cancela();
						return;
					}
				}

				if ( txtDtEmitCompra.getVlrDate().after( txtDtEntCompra.getVlrDate() ) ) {
					Funcoes.mensagemErro( this, "A data de Entrada não pode ser anterior à data de Emissão!" );
					this.txtDtEntCompra.requestFocus();
					pevt.cancela();
					return;
				}


			}
			if(!validaDocumento()) {

				if( Funcoes.mensagemConfirma( this, "Este documento (" + txtDocCompra.getVlrString() + ") já foi registrado no sistema!\n Confirma a inserção do documento duplicado?" ) == JOptionPane.NO_OPTION ) {

					txtDocCompra.requestFocus();
					pevt.cancela();


				}

			}


			//String modeloNota = getModeloNota( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "EQTIPOMOV" ), txtCodTipoMov.getVlrInteger() );
			//Se for nfe.

			if("S".equals( consistChaveNFE ) && txtCodModNota.getVlrInteger() == codigoNfe ) {

				if (  !isChaveNFEValid(Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "EQTIPOMOV" ), txtCodTipoMov.getVlrInteger() ) ) {
					tpnCab.setSelectedIndex( 2 );
					this.txtChaveNfe.requestFocus();
					pevt.cancela();
					return;
				}

			}

		}


		if ( pevt.getEstado() == ListaCampos.LCS_INSERT ) {
			novo = true;
		}

	}

	private boolean permiteRevalidarLote( Connection con ) {

		String sSql = "SELECT REVALIDARLOTECOMPRA FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement( sSql );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			rs = ps.executeQuery();
			rs.next();

			String revalidar = rs.getString( "REVALIDARLOTECOMPRA" );
			return "S".equals( revalidar ) ? true : false;
		} catch ( SQLException e ) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean getGuiaTraf() {

		boolean retorno = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();

		try {

			sSQL.append( "SELECT GUIATRAFPROD FROM EQPRODUTO WHERE CODEMP=? AND CODFILIAL=? AND CODPROD=?  " );

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			ps.setInt( 3, txtCodProd.getVlrInteger().intValue() );
			rs = ps.executeQuery();

			if ( rs.next() ) {
				if ( rs.getString( 1 ).equals( "S" ) ) {
					retorno = true;
				}
			}

		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar dados do produto " + e.getMessage() );
		}

		return retorno;
	}

	public BigDecimal getVolumes() {

		BigDecimal retorno = new BigDecimal( 0 );

		for ( int i = 0; i < tab.getNumLinhas(); i++ ) {

			retorno = retorno.add( ConversionFunctions.stringCurrencyToBigDecimal( tab.getValor( i, 5 ).toString() ) );
		}

		return retorno;
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		getPrefere();

		setNfecf( new NFEConnectionFactory( cn, Aplicativo.getInstace().getConexaoNFE(), AbstractNFEFactory.TP_NF_IN, false, proceminfe ) );

		lcTipoMov.setConexao( cn );
		lcSerie.setConexao( cn );
		lcNumSerie.setConexao( cn );
		lcFor.setConexao( cn );
		lcPlanoPag.setConexao( cn );
		lcProd.setConexao( cn );
		lcProd2.setConexao( cn );
		lcNat.setConexao( cn );
		lcLote.setConexao( cn );
		lcFisc.setConexao( cn );
		lcCompra2.setConexao( cn );
		lcAlmoxItem.setConexao( cn );
		lcAlmoxProd.setConexao( cn );
		lcTran.setConexao( cn );
		lcSolCompra.setConexao( cn );
		lcModNota.setConexao( cn );
		lcPais.setConexao( cn );
		lcUF.setConexao( cn );
		lcImportacao.setConexao( cn );
		lcLFItCompra.setConexao( cn );


		montaTela();
		montaDetalhe();
		daoimp = new DAOImportacao( cn );

	}

	/**
	 * mostra uma FObsevacao contendo a descrição completa do produto, quando clicado duas vezes sobre o JTextFieldFK do item.
	 * 
	 * @param txaObsIt
	 *            JTextAreaPad.
	 * @param iCodProd
	 *            codigo do produto.
	 * @param sDescProd
	 *            descrição do produto.
	 */
	protected void mostraTelaDecricao( JTextAreaPad txaObsIt, int iCodProd, String sDescProd ) {

		if ( iCodProd == 0 ) {
			return;
		}

		String sDesc = txaObsIt.getVlrString();

		if ( sDesc.equals( "" ) ) {
			sDesc = buscaDescComp( iCodProd );
		}
		if ( sDesc.equals( "" ) ) {
			sDesc = sDescProd;
		}

		DLBuscaDescProd obs = new DLBuscaDescProd( sDesc );
		obs.setConexao( con );
		obs.setVisible( true );

		if ( obs.OK ) {
			txaObsIt.setVlrString( obs.getTexto() );
			lcDet.edit();
		}

		obs.dispose();

	}

	/**
	 * Busca descrição completa do produto na tabela de produtos .
	 * 
	 * @param iCodProd
	 *            codigo do produto a pesquizar.
	 * @return String contendo a descrição completa do produto.
	 */
	private String buscaDescComp( int iCodProd ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sRet = "";
		String sSQL = "SELECT DESCCOMPPROD FROM EQPRODUTO WHERE CODPROD=?" + " AND CODEMP=? AND CODFILIAL=?";

		try {
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, iCodProd );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			rs = ps.executeQuery();

			if ( rs.next() ) {
				sRet = rs.getString( "DescCompProd" );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar descrição completa!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}

		return sRet != null ? sRet : "";
	}

	private void geraOpConversao() {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Vector<Integer> codops = new Vector<Integer>();
		Vector<Integer> seqops = new Vector<Integer>();

		Integer codempet = null;
		Integer codfilialet = null;
		Integer codprodet = null;
		Integer seqest = null;

		BigDecimal qtdest = null;
		BigDecimal qtditcompra = null;
		BigDecimal qtdformula = null;
		BigDecimal qtdsugerida = null;
		Integer codprod = null;
		String justificativa = "";

		DLFinalizaOP dl = null;

		try {

			codprod = txtCodProd.getVlrInteger();
			qtditcompra = txtQtdItCompra.getVlrBigDecimal();

			// Query para busca de informçõs da estrutura para conversão e informações sobre o item de estrutura vinculado ao produto da compra.

			sql.append( "select first 1 fc.codempet, fc.codfilialet, fc.codprodet, fc.seqest, et.qtdest, ie.qtditest  " );
			sql.append( "from eqfatconv fc, ppestrutura et, ppitestrutura ie " );
			sql.append( "where fc.codemp=? and fc.codfilial=? and fc.codprod=? and fc.cpfatconv='S' " );
			sql.append( "and et.codemp=fc.codempet and et.codfilial=fc.codfilialet and et.codprod=fc.codprodet and et.seqest=fc.seqest " );
			sql.append( "and ie.codemp=et.codemp and ie.codfilial=et.codfilial and ie.codprod=et.codprod and ie.seqest=et.seqest " );
			sql.append( "and ie.codemppd=fc.codemp and ie.codfilialpd=fc.codfilial and ie.codprodpd=fc.codprod and ie.qtdvariavel='S'" );
			sql.append( "and et.ativoest='S'" );

			ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, lcProd.getCodEmp() );
			ps.setInt( 2, lcProd.getCodFilial() );
			ps.setInt( 3, codprod );

			rs = ps.executeQuery();

			if ( rs.next() ) {

				codempet = rs.getInt( "codempet" );
				codfilialet = rs.getInt( "codfilialet" );
				codprodet = rs.getInt( "codprodet" );
				seqest = rs.getInt( "seqest" );
				qtdest = rs.getBigDecimal( "qtdest" );
				qtdformula = rs.getBigDecimal( "qtditest" );

				if ( qtdformula != null && qtdformula.compareTo( new BigDecimal( 0 ) ) > 0 ) {
					qtdsugerida = ( qtditcompra.divide( qtdformula,  RoundingMode.HALF_EVEN   ) );

				}

			}


			rs.close();
			con.commit();

			if ( qtdsugerida != null && qtdsugerida.compareTo( new BigDecimal( 0 ) ) > 0 ) {

				// Dialog de confirmação

				String bloqqtdprod = getBloqQtdProd( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "PPESTRUTURA" ), txtCodProd.getVlrInteger(), seqest );


				dl = new DLFinalizaOP( this, qtdsugerida, bloqqtdprod );

				dl.setVisible( true );

				if ( dl.OK ) {
					qtdsugerida = dl.getValor();
					justificativa = dl.getObs();
				}

				dl.dispose();

				sql = new StringBuilder();

				sql.append( "select codopret,seqopret " );
				sql.append( "from ppgeraop(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) " );

				ps = con.prepareStatement( sql.toString() );

				ps.setString( PROCEDUREOP.TIPOPROCESS.ordinal() + 1, "C" );
				ps.setInt( PROCEDUREOP.CODEMPOP.ordinal() + 1, Aplicativo.iCodEmp );
				ps.setInt( PROCEDUREOP.CODFILIALOP.ordinal() + 1, Aplicativo.iCodFilial );
				ps.setNull( PROCEDUREOP.CODOP.ordinal() + 1, Types.INTEGER );
				ps.setNull( PROCEDUREOP.SEQOP.ordinal() + 1, Types.INTEGER );
				ps.setInt( PROCEDUREOP.CODEMPPD.ordinal() + 1, codempet );
				ps.setInt( PROCEDUREOP.CODFILIALPD.ordinal() + 1, codfilialet );
				ps.setInt( PROCEDUREOP.CODPROD.ordinal() + 1, codprodet );
				ps.setNull( PROCEDUREOP.CODEMPOC.ordinal() + 1, Types.INTEGER );
				ps.setNull( PROCEDUREOP.CODFILIALOC.ordinal() + 1, Types.INTEGER );
				ps.setNull( PROCEDUREOP.CODORC.ordinal() + 1, Types.INTEGER );
				ps.setNull( PROCEDUREOP.CODITORC.ordinal() + 1, Types.INTEGER );
				ps.setNull( PROCEDUREOP.TIPOORC.ordinal() + 1, Types.CHAR );
				ps.setBigDecimal( PROCEDUREOP.QTDSUGPRODOP.ordinal() + 1, qtdsugerida );
				ps.setDate( PROCEDUREOP.DTFABROP.ordinal() + 1, Funcoes.dateToSQLDate( txtDtEntCompra.getVlrDate() ) );
				ps.setInt( PROCEDUREOP.SEQEST.ordinal() + 1, seqest );

				ps.setNull( PROCEDUREOP.CODEMPET.ordinal() + 1, Types.INTEGER );
				ps.setNull( PROCEDUREOP.CODFILIALET.ordinal() + 1, Types.INTEGER );
				ps.setNull( PROCEDUREOP.CODEST.ordinal() + 1, Types.INTEGER );
				ps.setNull( PROCEDUREOP.AGRUPDATAAPROV.ordinal() + 1, Types.CHAR );
				ps.setNull( PROCEDUREOP.AGRUPDTFABROP.ordinal() + 1, Types.CHAR );
				ps.setNull( PROCEDUREOP.AGRUPCODCLI.ordinal() + 1, Types.CHAR );
				ps.setNull( PROCEDUREOP.CODEMPCL.ordinal() + 1, Types.INTEGER );
				ps.setNull( PROCEDUREOP.CODFILIALCL.ordinal() + 1, Types.INTEGER );
				ps.setNull( PROCEDUREOP.CODCLI.ordinal() + 1, Types.INTEGER );
				ps.setNull( PROCEDUREOP.DATAAPROV.ordinal() + 1, Types.DATE );

				ps.setInt( PROCEDUREOP.CODEMPCP.ordinal() + 1, lcDet.getCodEmp() );
				ps.setInt( PROCEDUREOP.CODFILIALCP.ordinal() + 1, lcDet.getCodFilial() );
				ps.setInt( PROCEDUREOP.CODCOMPRA.ordinal() + 1, txtCodCompra.getVlrInteger() );
				ps.setInt( PROCEDUREOP.CODITCOMPRA.ordinal() + 1, txtCodItCompra.getVlrInteger() );

				ps.setString( PROCEDUREOP.JUSTFICQTDPROD.ordinal() + 1, justificativa );

				ps.setInt( PROCEDUREOP.CODEMPPDENTRADA.ordinal() + 1, lcProd.getCodEmp() );
				ps.setInt( PROCEDUREOP.CODFILIALPDENTRADA.ordinal() + 1, lcProd.getCodEmp() );
				ps.setInt( PROCEDUREOP.CODPRODENTRADA.ordinal() + 1, codprod );
				ps.setBigDecimal( PROCEDUREOP.QTDENTRADA.ordinal() + 1, qtditcompra );

				rs = ps.executeQuery();

				if ( rs.next() ) {
					codops.addElement( rs.getInt( 1 ) );
					seqops.addElement( rs.getInt( 2 ) );
				}

				con.commit();

				if ( codops.size() > 0 ) {

					sql = new StringBuilder();
					sql.append( "update ppitop io set io.qtditop=? " );
					sql.append( "where io.codemp=? and io.codfilial=? and io.codop=? and io.seqop=?" );
					sql.append( "and io.codemppd=? and io.codfilialpd=? and io.codprod=?" );

					ps = con.prepareStatement( sql.toString() );

					ps.setBigDecimal( 1, qtditcompra );

					ps.setInt( 2, Aplicativo.iCodEmp );
					ps.setInt( 3, Aplicativo.iCodFilial );
					ps.setInt( 4, (Integer) codops.elementAt( 0 ) );
					ps.setInt( 5, (Integer) seqops.elementAt( 0 ) );
					ps.setInt( 6, lcProd.getCodEmp() );
					ps.setInt( 7, lcProd.getCodFilial() );
					ps.setInt( 8, codprod );

					ps.executeUpdate();

					con.commit();

					Funcoes.mensagemInforma( this, "A seguinte ordem de produção foi gerada:\n" + codops.toString() );

				}

			}
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();

				rs = null;
				ps = null;
				codops = null;
				seqops = null;
			} catch ( Exception e2 ) {
				e2.printStackTrace();
			}

		}
	}

	public NFEConnectionFactory getNfecf() {

		return nfecf;
	}

	public void setNfecf( NFEConnectionFactory nfecf ) {

		this.nfecf = nfecf;
	}

	public void mouseClicked( MouseEvent mevt ) {

		if ( mevt.getSource() == txtDescProd ) {
			if ( mevt.getClickCount() == 2 ) {
				mostraTelaDecricao( txaObsItCompra, txtCodProd.getVlrInteger().intValue(), txtDescProd.getVlrString() );
			}
		}
	}

	public void mouseEntered( MouseEvent arg0 ) {

		// TODO Auto-generated method stub

	}

	public void mouseExited( MouseEvent arg0 ) {

		// TODO Auto-generated method stub

	}

	public void mousePressed( MouseEvent arg0 ) {

		// TODO Auto-generated method stub

	}

	public void mouseReleased( MouseEvent arg0 ) {

		// TODO Auto-generated method stub

	}

	private void getTratTrib() {

		try {

			impostos.setCodprod( txtCodProd.getVlrInteger() );
			impostos.setTipotransacao( CalcImpostos.TRANSACAO_ENTRADA );
			impostos.setCoddestinatario( txtCodFor.getVlrInteger() );
			impostos.setCodtipomov( txtCodTipoMov.getVlrInteger() );
			impostos.setCodfisc( null );
			impostos.setCoditfisc( null );

			impostos.calcTratTrib();

			// txtOrigFisc.setVlrString( impostos.getOrigfisc() );
			// txtCodTratTrib.setVlrString( impostos.getCodtrattrib() );
			txtRedFisc.setVlrBigDecimal( impostos.getRedfisc() );
			txtTipoFisc.setVlrString( impostos.getTipofisc() );
			// txtTipoST.setVlrString( impostos.getTipost() );
			// txtCodMens.setVlrInteger( impostos.getCodmens() );
			txtPercICMSItCompra.setVlrBigDecimal( impostos.getAliqfisc() );
			txtAliqFisc.setVlrBigDecimal( impostos.getAliqfisc() );

			txtAliqIPIFisc.setVlrBigDecimal( impostos.getAliqipifisc() );
			txtTpRedIcmsFisc.setVlrString( impostos.getTpredicmsfisc() );
			// txtMargemVlAgr.setVlrBigDecimal( impostos.getMargemvlragr() );

			// Carregando campos para gravação do item de classificação selecionado

			if ( impostos.getCoditfisc() != null && impostos.getCoditfisc().floatValue() > 0 ) {
				txtCodEmpIf.setVlrInteger( impostos.getCodempif() );
				txtCodFilialIf.setVlrInteger( impostos.getCodfilialif() );
				txtCodFiscIf.setVlrString( impostos.getCodfisc() );
				txtCodItFisc.setVlrInteger( impostos.getCoditfisc() );
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	private void getICMS() {

		try {

			impostos.setCodnat( txtCodNat.getVlrString() );
			impostos.setUftransacao( txtEstFor.getVlrString() );

			impostos.calcAliqFisc( txtAliqFisc.getVlrBigDecimal() );
			txtPercICMSItCompra.setVlrBigDecimal( impostos.getAliqfisc() );

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	private void calcImpostos( boolean buscabase ) {

		if ( "S".equals( txtCalcTrib.getVlrString() ) ) {
			setCalcImpostos( buscabase );
			getCalcImpostos();
		}
		else {
			calcICMSOld( buscabase );
		}

	}

	private void setCalcImpostos( boolean buscabase ) {

		try {

			impostos.setBuscabase( buscabase );
			impostos.setVlrprod( txtVlrProdItCompra.getVlrBigDecimal() );
			impostos.setVlrdescit( txtVlrDescItCompra.getVlrBigDecimal() );
			impostos.setAliqiss( txtAliqISSItCompra.getVlrBigDecimal() );
			impostos.setVlriss( txtVlrISSItCompra.getVlrBigDecimal() );

			if (buscabase) {
				getTratTrib();
				getICMS();
			} else {
				impostos.setVlrbaseicmsit( txtVlrBaseICMSItCompra.getVlrBigDecimal() );
				impostos.setAliqfisc( txtPercICMSItCompra.getVlrBigDecimal() );
			}

			//			System.out.println(txtVlrBaseICMSItCompra.getVlrBigDecimal());
			impostos.calcICMS();
			//System.out.println(txtVlrBaseICMSItCompra.getVlrBigDecimal());
			impostos.calcIPI();
			//System.out.println(txtVlrBaseICMSItCompra.getVlrBigDecimal());
			impostos.calcVlrLiqIt();
			//System.out.println(txtVlrBaseICMSItCompra.getVlrBigDecimal());
			impostos.calcISS();

		} catch ( Exception e ) {
			e.printStackTrace(); 
		}
	}

	private void getCalcImpostos() {

		try {

			txtVlrBaseICMSItCompra.setVlrBigDecimal( impostos.getVlrbaseicmsit() );
			txtPercICMSItCompra.setVlrBigDecimal( impostos.getAliqfisc() );
			txtVlrICMSItCompra.setVlrBigDecimal( impostos.getVlricmsit() );

			txtVlrBaseIPIItCompra.setVlrBigDecimal( impostos.getVlrbaseipiit() );
			txtPercIPIItCompra.setVlrBigDecimal( impostos.getAliqipifisc() );
			txtVlrIPIItCompra.setVlrBigDecimal( impostos.getVlripiit() );

			txtVlrBaseICMSStItCompra.setVlrBigDecimal( impostos.getVlrbaseicmsstit() );
			//txtPercICMSStItCompra.setVlrBigDecimal( impostos.getAliqfisc() );
			txtVlrICMSStItCompra.setVlrBigDecimal( impostos.getVlricmsitst() );

			txtVlrLiqItCompra.setVlrBigDecimal( impostos.getVlrliqit() );

			// defineUltimoCampo();

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	private void geraInfoCompl() {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs_inf_compl = null;
		StringBuilder inf_compl = new StringBuilder( "" );
		String separador = " - ";

		try {

			sql.append( "select coalesce(me.mens,'') inf_compl " );

			sql.append( "from cpitcompra ic " );
			sql.append( "left outer join lfitclfiscal icl on " );
			sql.append( "icl.codemp=ic.codempif and icl.codfilial=ic.codfilialif and icl.codfisc=ic.codfisc and icl.coditfisc=ic.coditfisc " );
			sql.append( "left outer join lfmensagem me on " );
			sql.append( "me.codemp=icl.codempme and me.codfilial=icl.codfilialme and me.codmens=icl.codmens " );

			sql.append( "where " );
			sql.append( "ic.codemp=? and ic.codfilial=? and ic.codcompra=? " );

			ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDVENDA" ) );
			ps.setInt( 3, txtCodCompra.getVlrInteger() );

			rs_inf_compl = ps.executeQuery();

			int count = 0;

			while ( rs_inf_compl.next() ) {

				if (!"".equals(rs_inf_compl.getString( "inf_compl" )) &&  count > 0 ) {
					inf_compl.append( separador );
				}

				inf_compl.append( rs_inf_compl.getString( "inf_compl" ) );
				count++;

			}

			txaInfCompl.setVlrString( inf_compl.toString() );

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	public void insertItem( Integer codprod, String refprod, BigDecimal qtd ) {

		lcDet.insert( true );
		txtCodProd.requestFocus();
		txtCodProd.setVlrInteger( codprod );
		txtRefProd.requestFocus();
		txtRefProd.setVlrString( refprod );
		txtQtdItCompra.requestFocus();
		txtQtdItCompra.setVlrBigDecimal( qtd );
		txtPrecoItCompra.requestFocus();

		if ( comref ) {
			lcProd2.carregaDados();
		}
		else {
			lcProd.carregaDados();
		}

		txtPrecoItCompra.setVlrBigDecimal( txtPrecoBaseProd.getVlrBigDecimal() );

		getCFOP();
		getTratTrib();

		lcDet.post();
	}

	public String  getBloqQtdProd(Integer codemp, Integer codfilial, Integer codprod, Integer seqest){
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String bloqqtdprod = null;

		try{
			sql.append( "select e.bloqqtdprod from ppestrutura e where e.codemp=? and e.codfilial=? and e.codprod=? and e.seqest=? " );
			ps = con.prepareStatement( sql.toString() );
			int param = 1;
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codprod );
			ps.setInt( param++, seqest );
			rs = ps.executeQuery();

			if(rs.next()){
				bloqqtdprod = rs.getString( "bloqqtdprod" );
			}		
		}catch (Exception e) {
			e.printStackTrace();
		}

		return bloqqtdprod;
	}

	private boolean validaDocumento( ) {

		String sSql = "SELECT DOCCOMPRA FROM CPCOMPRA CP WHERE CP.CODEMP=? AND CP.CODFILIAL=? AND CP.DOCCOMPRA=? AND CP.SERIE=? AND CP.CODEMPFR=? AND CP.CODFILIALFR=? AND CP.CODFOR=? AND CP.CODCOMPRA!=?";
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean ret = true;

		try {

			ps = con.prepareStatement( sSql );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
			ps.setInt( 3, txtDocCompra.getVlrInteger() );
			ps.setString( 4, txtSerieCompra.getVlrString() );
			ps.setInt( 5, Aplicativo.iCodEmp );
			ps.setInt( 6, ListaCampos.getMasterFilial( "CPFORNECED" ) );
			ps.setInt( 7, txtCodFor.getVlrInteger() );
			ps.setInt( 8, txtCodCompra.getVlrInteger() ); 

			rs = ps.executeQuery();

			if(rs.next()) {

				ret = false;

			}


		} catch ( SQLException e ) {
			e.printStackTrace();
			return false;
		}
		return ret;
	}

	public void carregaCompra(Integer codcompra) {
		txtCodCompra.setVlrInteger( codcompra );
		lcCampos.carregaDados();
	}

	public static void createCompra( DbConnection cn,  int codcompra) {
		String titulo = "Compra";
		String nome = FCompra.class.getName();
		FCompra compra = null;
		compra = (FCompra) Aplicativo.telaPrincipal.getTela( nome );
		if ( compra == null ) {
			compra = new FCompra();
			Aplicativo.telaPrincipal.criatela( titulo, compra, cn );
		}
		compra.carregaCompra(codcompra);
		compra.show();
	}

	public int post() {
		if(lcCampos.getStatus() == ListaCampos.LCS_INSERT || lcCampos.getStatus() == ListaCampos.LCS_EDIT )
			lcCampos.post();
		return txtCodCompra.getVlrInteger();
	}


}
