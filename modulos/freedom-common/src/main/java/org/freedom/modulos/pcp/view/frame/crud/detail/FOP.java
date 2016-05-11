/**
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.pcp <BR>
 *         Classe:
 * @(#)FOP.java <BR>
 * 
 *              Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *              modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *              na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *              Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *              sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *              Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *              Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *              de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *              Tela de cadastro de ordens de produção.
 * 
 */

package org.freedom.modulos.pcp.view.frame.crud.detail;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.acao.CancelEvent;
import org.freedom.acao.CancelListener;
import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.TabelaEditEvent;
import org.freedom.acao.TabelaEditListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.beans.Usuario;
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
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.dialog.DLJustCanc;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDetalhe;
import org.freedom.library.swing.frame.FObservacao;
import org.freedom.library.swing.frame.FPrincipal;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.gms.view.dialog.utility.DLItensEstruturaProd;
import org.freedom.modulos.gms.view.frame.crud.detail.FOrdemServico;
import org.freedom.modulos.gms.view.frame.crud.detail.FRecMerc;
import org.freedom.modulos.gms.view.frame.crud.detail.FRma;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FTipoMov;
import org.freedom.modulos.pcp.Interface.Recarrega;
import org.freedom.modulos.pcp.business.object.ModLote;
import org.freedom.modulos.pcp.view.dialog.report.DLROP;
import org.freedom.modulos.pcp.view.dialog.utility.DLContrQualidade;
import org.freedom.modulos.pcp.view.dialog.utility.DLDistrib;
import org.freedom.modulos.pcp.view.dialog.utility.DLFinalizaOPParcial;
import org.freedom.modulos.pcp.view.dialog.utility.DLObsJust;
import org.freedom.modulos.pcp.view.dialog.utility.DLRemIndustria;
import org.freedom.modulos.pcp.view.dialog.utility.DLRetIndustria;
import org.freedom.modulos.pcp.view.frame.crud.plain.FModLote;
import org.freedom.modulos.std.view.dialog.utility.DLBuscaProd;
import org.freedom.modulos.std.view.frame.crud.plain.FAlmox;


public class FOP extends FDetalhe implements ChangeListener, CancelListener, InsertListener, CarregaListener, FocusListener, TabelaEditListener, Recarrega {

	private static final long serialVersionUID = 1L;

	private int casasDec = Aplicativo.casasDec;

	private JPanelPad pinCab = new JPanelPad();

	private JPanelPad pinDet = new JPanelPad();

	private JPanelPad pinStatus = new JPanelPad( 150, 20 );

	private JLabelPad lbStatus = new JLabelPad();

	private JTabbedPanePad tpnAbas = new JTabbedPanePad();

	private JTextFieldPad txtCodOP = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtTicket = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtCodItRecMerc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtCodItOS = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtSeqOP = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtQtdEst = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtCodProdDet = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtRefProdDet = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtUsaDensidadeOP = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtRefProdEst = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldFK txtDescProdDet = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtDtFabProd = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtQtdPrevProdOP = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtQtdDistOp = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtQtdItSp = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );
	
	private JTextFieldPad txtQtdSugProdOP = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldFK txtVlrDensidade = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtQtdFinalProdOP = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtDtValidOP = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtSeqItOp = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtQtdItOp = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtQtdCopiaItOp = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtCodLoteProdRat = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCodLoteProdDet = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldFK txtDescLoteProdDet = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodLoteProdEst = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtGeraRMAAut = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtSeqAc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtBloqOp = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );
	
	private JTextFieldPad txtPermiteAjusteItOp = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldFK txtDescLoteProdEst = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldFK txtSldLiqProd = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, casasDec );
	
	private JTextFieldFK txtCodUnidProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldFK txtUsaLoteDet = new JTextFieldFK( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldFK txtSitOp = new JTextFieldFK( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtUsaLoteEst = new JTextFieldFK( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtCodAlmoxEst = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldFK txtDescAlmoxEst = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodModLote = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescModLote = new JTextFieldFK( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldFK txtModLote = new JTextFieldFK( JTextFieldPad.TP_STRING, 100, 0 );

	private JTextFieldPad txtNroDiasValid = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtCodProdEst = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescEst = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtSeqEst = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtQtdDigRat = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtLoteRat = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldFK txtVencLoteRat = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtSldLoteRat = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private ListaCampos lcEstruturaCod = new ListaCampos( this, "PD" );

	private ListaCampos lcEstruturaRef = new ListaCampos( this, "PD" );

	private ListaCampos lcProdDetCod = new ListaCampos( this, "PD" );

	private ListaCampos lcProdDetRef = new ListaCampos( this, "PD" );

	private ListaCampos lcLoteProdDet = new ListaCampos( this, "LE" );

	private ListaCampos lcLoteProdEst = new ListaCampos( this, "LE" );

	private ListaCampos lcLoteProdRat = new ListaCampos( this, "LE" );

	private ListaCampos lcModLote = new ListaCampos( this, "ML" );

	private JButtonPad btFinaliza = new JButtonPad( Icone.novo( "btFinalizaOP.png" ) );
	
	private JButtonPad btSubProd = new JButtonPad( Icone.novo( "btSubProd.png" ) );
	
	private JButtonPad btRemessa = new JButtonPad( Icone.novo( "btRemessa.png" ) );
	
	private JButtonPad btRetorno = new JButtonPad( Icone.novo( "btRetorno1.png" ) );

	private JButtonPad btCancela = new JButtonPad( Icone.novo( "btCancelar.png" ) );

	private JButtonPad btRMA = new JButtonPad( Icone.novo( "btRma.png" ) );

	private JButtonPad btLote = new JButtonPad( Icone.novo( "btSimilar.png" ) );

	private JButtonPad btRatearItem = new JButtonPad( Icone.novo( "btAdic2.gif" ) );

	private JButtonPad btDistrib = new JButtonPad( Icone.novo( "btDistOP.png" ) );

	private JButtonPad btContrQuali = new JButtonPad( Icone.novo( "btCQ.png" ) );

	private JButtonPad btObs = new JButtonPad( Icone.novo( "btObs1.png" ) );

	private JButtonPad btObs2 = new JButtonPad( Icone.novo( "btObs1.png" ) );

	private JButtonPad btReprocessaItens = new JButtonPad( Icone.novo( "btReset.png" ) );

	private FPrinterJob dl = null;

	private ListaCampos lcTipoMov = new ListaCampos( this, "TM" );

	private JTextFieldPad txtCodTpMov = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldFK txtDescTipoMov = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private Integer iCodTpMov = null;
	
	private boolean entrada_parcial = false;

	private JPanelPad pinBotCab = new JPanelPad( 104, 33 );

	private JPanelPad pnBtObs = new JPanelPad();

	private ListaCampos lcAlmoxEst = new ListaCampos( this, "AX" );

	private JTablePad tabSimu = new JTablePad();

	public JTablePad tabRMA = new JTablePad();

	public JTablePad tabOPS = new JTablePad();
	
	public JTablePad tabOSS = new JTablePad();
	
	public JTablePad tabEntradaParcial = new JTablePad();

	private JScrollPane spSimu = new JScrollPane( tabSimu );

	public JScrollPane spRma = new JScrollPane( tabRMA );

	public JScrollPane spOPS = new JScrollPane( tabOPS );
	
	public JScrollPane spOSS = new JScrollPane( tabOSS );

	public JScrollPane spEntradaParcial = new JScrollPane( tabEntradaParcial );
	
	private ImageIcon imgCancelada = Icone.novo( "clVencido.gif" );

	private ImageIcon imgExpedida = Icone.novo( "clPago.gif" );

	private ImageIcon imgAprovada = Icone.novo( "clPagoParcial.gif" );

	private ImageIcon imgOPPrinc = Icone.novo( "clEng_dourada.gif" );

	private ImageIcon imgOPSub = Icone.novo( "clEng_azul.gif" );

	private ImageIcon imgPendente = Icone.novo( "clNaoVencido.gif" );

	private ImageIcon imgColunaRMA = null;

	private ImageIcon imgItemCorrecao = Icone.novo( "clItemCorrecao.gif" );

	private ImageIcon imgItemComum = Icone.novo( "clItemComum.gif" );

	private ImageIcon imgItemBloqueado = Icone.novo( "clCadeado.gif" );

	private ImageIcon imgBranco = Icone.novo( "clBranco.gif" );

	private boolean bBuscaRMA = false;

	private boolean bBuscaOPS = false;

	private JCheckBoxPad cbEstDinamica = new JCheckBoxPad( "Inserir ítens dinamicamente?", "S", "N" );
	
	private JCheckBoxPad cbGarantia = new JCheckBoxPad( "Substituição de garantia?", "S", "N" );
	
	private boolean bnovo = false;

	// painel

	private JPanelPad pinLb = new JPanelPad();

	private JPanelPad pinQuantidades = new JPanelPad();

	private JLabelPad lSitOp = null;

	private String SitOp = "";

	private HashMap<String, Object> prefere = null;
	
	private JPanelPad pnAdicEstrutura = new JPanelPad();
	
	private JButtonPad btAdicProdutoEstrutura = new JButtonPad( Icone.novo( "btEstProduto.png" ) );

	public FOP( int codOp, int seqOp ) {

		txtCodOP.setVlrInteger( codOp );
		txtSeqOP.setVlrInteger( seqOp );
	}

	public FOP() {

	}

	public FOP( boolean bnovo ) {

		this.bnovo = bnovo;
	}

	private void montaTela() {

		if ( fPrim == null ) {
			setTelaPrim( Aplicativo.telaPrincipal );
		}

		nav.setNavigation( true );
		
		btRatearItem.setBorder( BorderFactory.createEmptyBorder() );
		//setName( "Ordens de produção" );
		setTitulo( "Ordens de produção" );
		setAtribos( 15, 10, 645, 600 );
		setAltCab( 238 );

		btObs.setVisible( false );
		btObs2.setVisible( true );
		btReprocessaItens.setVisible( false );

		//pnRodape.remove( btSair );
		pnRodape.removeAll();
		
		pnAdicEstrutura.setBorder( null );

		pnRodape.add( navRod, BorderLayout.WEST );
		
		pnRodape.add( btSair, BorderLayout.EAST );
		
		pnRodape.add( pnAdicEstrutura, BorderLayout.CENTER );
		
		pnAdicEstrutura.adic( btPrevimp, 0, 0, 30, 26 );
		pnAdicEstrutura.adic( btImp, 30, 0, 30, 26 );
		pnAdicEstrutura.adic( btAdicProdutoEstrutura, 60, 0, 30, 26 );
		
		btAdicProdutoEstrutura.setEnabled( false );
		cbEstDinamica.setEnabled( false );
		
		pnMaster.remove( spTab );

		tpnAbas.addTab( "Previsão", spSimu );
		tpnAbas.addTab( "OP", spTab );
		tpnAbas.addTab( "Rma", spRma );
		tpnAbas.addTab( "OP's relacionadas", spOPS );
		tpnAbas.addTab( "OS's relacionadas", spOSS );
		
		
		if ( (Boolean) prefere.get( "PRODETAPAS" ) ) {
		
			tpnAbas.addTab( "Entradas parciais", spEntradaParcial );
		}
		
		pnMaster.add( tpnAbas, BorderLayout.CENTER );

		btFinaliza.setToolTipText( "Fases/Finalização" );
		btSubProd.setToolTipText( "Subprodutos" );
		btRemessa.setToolTipText( "Remessa" );
		btRetorno.setToolTipText( "Retorno" );
		
		btRMA.setToolTipText( "Gera ou exibe RMA." );
		btLote.setToolTipText( "Cadastra lote" );
		btRatearItem.setToolTipText( "Ratear ítem" );
		btDistrib.setToolTipText( "Distribuição" );
		btContrQuali.setToolTipText( "Controle de qualidade" );
		btCancela.setToolTipText( "Cancela O.P." );
		btObs.setToolTipText( "Motivo do cancelamento" );
		btObs2.setToolTipText( "Observação" );
		btReprocessaItens.setToolTipText( "Reprocessar itens" );
		btAdicProdutoEstrutura.setToolTipText( "Busca itens da estrutura" );
		
		pinCab.adic( pinQuantidades, 5, 125, 550, 50 );
		pinCab.adic( pinBotCab, 560, 5, 66, 190 );

		pinBotCab.adic( btLote, 		0, 	0, 		30, 30 );
		pinBotCab.adic( btRMA, 			0, 	31, 	30, 30 );
		pinBotCab.adic( btContrQuali, 	0, 	62, 	30, 30 );
		pinBotCab.adic( btDistrib, 		0,	93, 	30, 30 );
		pinBotCab.adic( btFinaliza, 	0, 	124, 	30, 30 );
		pinBotCab.adic( btCancela, 		0, 	155, 	30, 30 );
		pinBotCab.adic( btSubProd, 		31, 0, 		30, 30 );
		pinBotCab.adic( btRemessa, 		31, 31, 	30, 30 );
		pinBotCab.adic( btRetorno, 		31, 62,		30, 30 );
		
		pnNavCab.add( pinStatus, BorderLayout.EAST );
		pinStatus.tiraBorda();
		pinStatus.adic( pinLb, 38, 0, 110, 25 );
		pinStatus.adic( btObs, 0, 0, 35, 25 );
		pinStatus.adic( btReprocessaItens, 0, 0, 35, 25 );

		lcModLote.add( new GuardaCampo( txtCodModLote, "CodModLote", "Cod.Mod.Lote", ListaCampos.DB_PK, txtDescModLote, false ) );
		lcModLote.add( new GuardaCampo( txtDescModLote, "DescModLote", "Descrição do modelo de lote", ListaCampos.DB_SI, false ) );
		lcModLote.add( new GuardaCampo( txtModLote, "txaModLote", "Corpo", ListaCampos.DB_SI, false ) );
		lcModLote.montaSql( false, "MODLOTE", "EQ" );
		lcModLote.setQueryCommit( false );
		lcModLote.setReadOnly( true );
		txtCodModLote.setTabelaExterna( lcModLote, FModLote.class.getCanonicalName() );

		lcAlmoxEst.add( new GuardaCampo( txtCodAlmoxEst, "CodAlmox", "Cod.almox.", ListaCampos.DB_PK, txtDescAlmoxEst, false ) );
		lcAlmoxEst.add( new GuardaCampo( txtDescAlmoxEst, "DescAlmox", "Descrição do almoxarifado", ListaCampos.DB_SI, false ) );
		lcAlmoxEst.montaSql( false, "ALMOX", "EQ" );
		lcAlmoxEst.setQueryCommit( false );
		lcAlmoxEst.setReadOnly( true );
		txtDescAlmoxEst.setSoLeitura( true );
		txtCodAlmoxEst.setTabelaExterna( lcAlmoxEst, FAlmox.class.getCanonicalName() );

		// FK de Lotes
		lcLoteProdEst.add( new GuardaCampo( txtCodLoteProdEst, "CodLote", "Lote", ListaCampos.DB_PK, txtDescLoteProdEst, false ) );
		lcLoteProdEst.add( new GuardaCampo( txtDescLoteProdEst, "VenctoLote", "Dt.Vencimento", ListaCampos.DB_SI, false ) );
		lcLoteProdEst.add( new GuardaCampo( txtSldLiqProd, "SldLiqLote", "Saldo", ListaCampos.DB_SI, false ) );
		lcLoteProdEst.montaSql( false, "LOTE", "EQ" );
		lcLoteProdEst.setQueryCommit( false );
		lcLoteProdEst.setReadOnly( true );
		txtCodLoteProdEst.setTabelaExterna( lcLoteProdEst, null );

		// lista campos para o lote do rateio
		txtLoteRat.setNomeCampo( "CodLote" );
		txtLoteRat.setFK( true );
		lcLoteProdRat.add( new GuardaCampo( txtLoteRat, "CodLote", "Lote", ListaCampos.DB_PK, txtVencLoteRat, false ) );
		lcLoteProdRat.add( new GuardaCampo( txtVencLoteRat, "VenctoLote", "Dt.Vencimento", ListaCampos.DB_SI, false ) );
		lcLoteProdRat.add( new GuardaCampo( txtSldLoteRat, "SldLiqLote", "Saldo", ListaCampos.DB_SI, false ) );
		lcLoteProdRat.setDinWhereAdic( "CODPROD=#N", txtCodProdDet );
		lcLoteProdRat.montaSql( false, "LOTE", "EQ" );
		lcLoteProdRat.setQueryCommit( false );
		lcLoteProdRat.setReadOnly( true );
		txtLoteRat.setTabelaExterna( lcLoteProdRat, null );

		lcTipoMov.add( new GuardaCampo( txtCodTpMov, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false ) );
		lcTipoMov.add( new GuardaCampo( txtDescTipoMov, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false ) );
		lcTipoMov.setWhereAdic( "((ESTIPOMOV = 'E') AND TIPOMOV='OP' AND" + " ( TUSUTIPOMOV='S' OR	EXISTS (SELECT * FROM EQTIPOMOVUSU TU " + "WHERE TU.CODEMP=EQTIPOMOV.CODEMP AND TU.CODFILIAL=EQTIPOMOV.CODFILIAL AND " + "TU.CODTIPOMOV=EQTIPOMOV.CODTIPOMOV AND TU.CODEMPUS=" + Aplicativo.iCodEmp
				+ " AND " + "TU.CODFILIALUS=" + ListaCampos.getMasterFilial( "SGUSUARIO" ) + " AND TU.IDUSU='" + Aplicativo.getUsuario().getIdusu() + "') ) " + ")" );
		lcTipoMov.montaSql( false, "TIPOMOV", "EQ" );
		lcTipoMov.setReadOnly( true );
		txtCodTpMov.setTabelaExterna( lcTipoMov, FTipoMov.class.getCanonicalName() );

		lcEstruturaCod.add( new GuardaCampo( txtCodProdEst, "Codprod", "Cód.pd.", ListaCampos.DB_PK, txtDescEst, true ) );
		lcEstruturaCod.add( new GuardaCampo( txtSeqEst, "seqest", "Seq.Est.", ListaCampos.DB_PK, txtDescEst, true ) );
		lcEstruturaCod.add( new GuardaCampo( txtDescEst, "DescEst", "Descrição da estrutura", ListaCampos.DB_SI, false ) );
		lcEstruturaCod.add( new GuardaCampo( txtRefProdEst, "refprod", "Referência", ListaCampos.DB_SI, false ) );
		lcEstruturaCod.add( new GuardaCampo( txtQtdEst, "QtdEst", "Quantidade", ListaCampos.DB_SI, false ) );
		lcEstruturaCod.add( new GuardaCampo( txtCodModLote, "CodModLote", "Modelo de Lote", ListaCampos.DB_FK, false ) );
		lcEstruturaCod.add( new GuardaCampo( txtNroDiasValid, "NroDiasValid", "Dias de validade", ListaCampos.DB_SI, false ) );
		lcEstruturaCod.add( new GuardaCampo( txtUsaDensidadeOP, "UsaDensidadeOp", "Usa Densidade", ListaCampos.DB_SI, false ) );
		lcEstruturaCod.add( new GuardaCampo( cbEstDinamica, "EstDinamica", "", ListaCampos.DB_SI, false ) );
		

		lcEstruturaCod.setWhereAdic( "ATIVOEST='S'" );
		lcEstruturaCod.montaSql( false, "ESTRUTURA", "PP" );
		lcEstruturaCod.setQueryCommit( false );
		lcEstruturaCod.setReadOnly( true );
		txtCodProdEst.setTabelaExterna( lcEstruturaCod, FEstrutura.class.getCanonicalName() );
		txtSeqEst.setTabelaExterna( lcEstruturaCod, FEstrutura.class.getCanonicalName() );
		txtCodProdEst.setNomeCampo( "codprod" );

		lcEstruturaRef.add( new GuardaCampo( txtRefProdEst, "refprod", "Referência", ListaCampos.DB_PK, txtDescEst, true ) );
		lcEstruturaRef.add( new GuardaCampo( txtSeqEst, "seqest", "Seq.Est.", ListaCampos.DB_PK, txtDescEst, true ) );
		lcEstruturaRef.add( new GuardaCampo( txtCodProdEst, "Codprod", "Cód.pd.", ListaCampos.DB_SI, true ) );
		lcEstruturaRef.add( new GuardaCampo( txtDescEst, "DescEst", "Descrição da estrutura", ListaCampos.DB_SI, false ) );
		lcEstruturaRef.add( new GuardaCampo( txtQtdEst, "QtdEst", "Quantidade", ListaCampos.DB_SI, false ) );
		lcEstruturaRef.add( new GuardaCampo( txtCodModLote, "CodModLote", "Modelo de Lote", ListaCampos.DB_FK, false ) );
		lcEstruturaRef.add( new GuardaCampo( txtNroDiasValid, "NroDiasValid", "Dias de validade", ListaCampos.DB_SI, false ) );
		lcEstruturaRef.add( new GuardaCampo( txtUsaDensidadeOP, "UsaDensidadeOp", "Usa Densidade", ListaCampos.DB_SI, false ) );
		lcEstruturaRef.add( new GuardaCampo( cbEstDinamica, "EstDinamica", "", ListaCampos.DB_SI, false ) );

		lcEstruturaRef.setWhereAdic( "ATIVOEST='S'" );
		lcEstruturaRef.montaSql( false, "ESTRUTURA", "PP" );
		lcEstruturaRef.setQueryCommit( false );
		lcEstruturaRef.setReadOnly( true );
		txtRefProdEst.setTabelaExterna( lcEstruturaRef, FEstrutura.class.getCanonicalName() );
		txtSeqEst.setTabelaExterna( lcEstruturaRef, FEstrutura.class.getCanonicalName() );
		txtRefProdEst.setNomeCampo( "refprod" );

		setListaCampos( lcCampos );
		setPainel( pinCab, pnCliCab );

		adicCampo( txtCodOP, 7, 20, 70, 20, "CodOP", "Nº OP.", ListaCampos.DB_PK, true );
		adicCampo( txtSeqOP, 80, 20, 60, 20, "SeqOP", "Seq. OP.", ListaCampos.DB_PK, true );
		adicCampo( txtCodTpMov, 143, 20, 70, 20, "CodTipoMov", "Cód.Tp.Mov.", ListaCampos.DB_FK, txtDescTipoMov, true );
		adicDescFK( txtDescTipoMov, 216, 20, 256, 20, "DescTipoMov", "Descrição do tipo de movimento" );
		adicCampo( txtDtFabProd, 475, 20, 80, 20, "dtfabrop", "Dt.Fabricação", ListaCampos.DB_SI, true );

		if ( (Boolean) prefere.get( "USAREFPROD" ) ) {
			adicCampo( txtRefProdEst, 7, 60, 133, 20, "refprod", "Referência", ListaCampos.DB_FK, true );
			adicCampoInvisivel( txtCodProdEst, "CodProd", "Cód.prod.", ListaCampos.DB_FK, txtDescEst, true );
			txtCodProdEst.setFK( true );
		}
		else {
			adicCampo( txtCodProdEst, 7, 60, 133, 20, "codprod", "Cód.prod.", ListaCampos.DB_FK, txtDescEst, true );
			adicCampoInvisivel( txtRefProdEst, "RefProd", "Ref.prod.", ListaCampos.DB_FK, null, true );
			txtRefProdEst.setFK( true );
		}

		adicCampo( txtSeqEst, 143, 60, 30, 20, "seqest", "Seq.", ListaCampos.DB_FK, txtDescEst, true );
		adicDescFK( txtDescEst, 176, 60, 208, 20, "descprod", "Descrição da estrutura" );
		adicDescFK( txtQtdEst, 387, 60, 85, 20, "qtdest", "Qtd.Estrutura" );

		pinCab.adic( new JLabelPad( "Densidade" ), 475, 40, 80, 20 );
		pinCab.adic( txtVlrDensidade, 475, 60, 80, 20 );

		formataCampoLimpo( txtQtdPrevProdOP, new Color( 0, 0, 255 ) );
		formataCampoLimpo( txtQtdFinalProdOP, new Color( 255, 0, 0 ) );
		formataCampoLimpo( txtQtdDistOp, new Color( 255, 0, 0 ) );
		formataCampoLimpo( txtQtdItSp, new Color( 255, 0, 0 ) );

		adicCampo( txtCodAlmoxEst, 7, 100, 70, 20, "codalmox", "Cód.Almox.", ListaCampos.DB_FK, txtDescAlmoxEst, true );
		adicDescFK( txtDescAlmoxEst, 80, 100, 303, 20, "descalmox", "Descrição do almoxarifado" );
		adicCampo( txtCodLoteProdEst, 386, 100, 87, 20, "CodLote", "Lote", ListaCampos.DB_FK, txtDescLoteProdEst, false );
		adicDescFKInvisivel( txtDescLoteProdEst, "VenctoLote", "Vencto.Lote" );
		adicCampo( txtDtValidOP, 475, 100, 80, 20, "dtvalidpdop", "Dt.Validade", ListaCampos.DB_SI, false );

		adicCampoInvisivel( txtSitOp, "sitop", "sit.op.", ListaCampos.DB_SI, false );

		adicDB( cbEstDinamica, 5, 175, 200, 20, "estdinamica", "", true );
		adicDB( cbGarantia, 208, 175, 208, 20, "garantia", "", true );
		
		setPainel( pinQuantidades );

		txtQtdDistOp.setSoLeitura( true );
		txtQtdItSp.setSoLeitura( true );

		adicDescFK( txtCodUnidProd, 7, 20, 30, 20, "CodUnid", "Unid."); // Qtd.Sugerida
		adicCampo( txtQtdSugProdOP, 40, 20, 95, 20, "qtdsugprodop", "Qtd. Sugerida", ListaCampos.DB_SI, true ); // Qtd.Sugerida
		adicCampo( txtQtdPrevProdOP, 137, 20, 100, 20, "qtdprevprodop", "Qtd. Prevista", ListaCampos.DB_SI, false ); // Qtd.prevista
		adicCampo( txtQtdFinalProdOP, 240, 20, 100, 20, "qtdfinalprodop", "Qtd. Realizada", ListaCampos.DB_SI, false ); // Qtd.Realizada
		adicCampo( txtQtdDistOp, 343, 20, 100, 20, "QTDDISTPOP", "Qtd. Distribuida", ListaCampos.DB_SI, false ); // Qtd.Produzida
		adic( txtQtdItSp, 446, 20, 95, 20, "Qtd.Subproduto" ); // Qtd.sub.produto
		
		cbEstDinamica.setVlrString( "N" );
		
		adicCampoInvisivel( txtTicket, "ticket", "Ticket", ListaCampos.DB_SI, false ); // Ticket
		adicCampoInvisivel( txtCodItRecMerc, "coditrecmerc", "Cod.It.Rec.Merc.", ListaCampos.DB_SI, false ); // Ticket
		adicCampoInvisivel( txtCodItOS, "coditos", "Cod.It.OS.", ListaCampos.DB_SI, false ); // Ticket
		
		setListaCampos( true, "OP", "PP" );

		txtCodTpMov.setAtivo( false );

		lcCampos.addCarregaListener( this );
		lcDet.addCarregaListener( this );
		lcEstruturaCod.addCarregaListener( this );
		lcEstruturaRef.addCarregaListener( this );
		lcLoteProdEst.addCarregaListener( this );
		lcModLote.addCarregaListener( this );

		lcCampos.addInsertListener( this );
		lcDet.addInsertListener( this );

		lcCampos.addPostListener( this );
		lcDet.addPostListener( this );

		lcCampos.addCancelListener( this );
		lcDet.addCancelListener( this );

		btFinaliza.addActionListener( this );
		btSubProd.addActionListener( this );
		btObs.addActionListener( this );
		btObs2.addActionListener( this );
		btReprocessaItens.addActionListener( this );
		btRemessa.addActionListener( this );
		btRetorno.addActionListener( this );
		
		btContrQuali.addActionListener( this );
		btRMA.addActionListener( this );
		btLote.addActionListener( this );
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		btRatearItem.addActionListener( this );
		btDistrib.addActionListener( this );
		btCancela.addActionListener( this );
		btAdicProdutoEstrutura.addActionListener( this );
		
		
		txtQtdSugProdOP.addFocusListener( this );
		txtSeqEst.addFocusListener( this );

		montaDet();

		tabSimu.adicColuna( "Fase" );// 0
		tabSimu.adicColuna( "Cód.prod" );// 1
		tabSimu.adicColuna( "Descrição do produto" );// 2
		tabSimu.adicColuna( "und." );// 3
		tabSimu.adicColuna( "Composição" );// 4
		tabSimu.adicColuna( "Saldo" );// 5
		tabSimu.adicColuna( "Utilizado" );// 6
		tabSimu.adicColuna( "Rma?" );// 7

		tabSimu.setTamColuna( 38, 0 );
		tabSimu.setTamColuna( 60, 1 );
		tabSimu.setTamColuna( 200, 2 );
		tabSimu.setTamColuna( 35, 3 );
		tabSimu.setTamColuna( 76, 4 );
		tabSimu.setTamColuna( 80, 5 );
		tabSimu.setTamColuna( 70, 6 );
		tabSimu.setTamColuna( 35, 7 );

		tabRMA.adicColuna( "" );// 0
		tabRMA.adicColuna( "Rma" );// 1
		tabRMA.adicColuna( "Cód.Prod." );// 2
		tabRMA.adicColuna( "Descrição do produto" );// 3
		tabRMA.adicColuna( "Dt. req." );// 4
		tabRMA.adicColuna( "Qt. req." );// 5
		tabRMA.adicColuna( "Qt. exp" );// 7
		tabRMA.adicColuna( "Saldo" );// 8

		tabRMA.setTamColuna( 13, 0 );
		tabRMA.setTamColuna( 55, 1 );
		tabRMA.setTamColuna( 55, 2 );
		tabRMA.setTamColuna( 180, 3 );
		tabRMA.setTamColuna( 70, 4 );
		tabRMA.setTamColuna( 70, 5 );
		tabRMA.setTamColuna( 70, 6 );
		tabRMA.setTamColuna( 70, 7 );

		tabOPS.adicColuna( "" );// 0
		tabOPS.adicColuna( "Cód.OP." );// 1
		tabOPS.adicColuna( "Seq.OP." );// 2
		tabOPS.adicColuna( "Cód.Prod." );// 3
		tabOPS.adicColuna( "Seq.Est." );// 4
		tabOPS.adicColuna( "Descrição do produto" );// 5
		tabOPS.adicColuna( "Descrição da estrutura" );// 6
		
		tabOPS.setTamColuna( 13, 0 );
		tabOPS.setTamColuna( 50, 1 );
		tabOPS.setTamColuna( 50, 2 );
		tabOPS.setTamColuna( 65, 3 );
		tabOPS.setTamColuna( 50, 4 );
		tabOPS.setColunaInvisivel( 5 );
		tabOPS.setTamColuna( 350, 6 );
		
		tabOSS.adicColuna( "Ticket" );// 0
		tabOSS.adicColuna( "It.Rec." );// 1
		tabOSS.adicColuna( "It.OS." );// 2
		tabOSS.adicColuna( "Série" );// 3
		tabOSS.adicColuna( "Cod.Cli." );// 4
		tabOSS.adicColuna( "Cliente" );// 5

		tabOSS.setTamColuna( 40, 0 );
		tabOSS.setTamColuna( 30, 1 );
		tabOSS.setTamColuna( 30, 2 );
		tabOSS.setTamColuna( 100, 3 );
		tabOSS.setTamColuna( 50, 4 );
		tabOSS.setTamColuna( 300, 5 );
		
		tabEntradaParcial.adicColuna( "Seq." );// 0
		tabEntradaParcial.adicColuna( "Data" );// 1
		tabEntradaParcial.adicColuna( "Hora" );// 2
		tabEntradaParcial.adicColuna( "Qtd." );// 3
		tabEntradaParcial.adicColuna( "Obs." );// 4
		tabEntradaParcial.adicColuna( "Usuário" );// 5

		tabEntradaParcial.setTamColuna( 40, 0 );
		tabEntradaParcial.setTamColuna( 70, 1 );
		tabEntradaParcial.setTamColuna( 70, 2 );
		tabEntradaParcial.setTamColuna( 100, 3 );
		tabEntradaParcial.setTamColuna( 150, 4 );
		tabEntradaParcial.setTamColuna( 100, 5 );
		
		tabRMA.addMouseListener( new MouseAdapter() {

			public void mouseClicked( MouseEvent mevt ) {

				if ( mevt.getSource() == tabRMA && mevt.getClickCount() == 2 )
					abreRma();
			}
		} );

		tabOPS.addMouseListener( new MouseAdapter() {

			public void mouseClicked( MouseEvent mevt ) {

				if ( mevt.getSource() == tabOPS && mevt.getClickCount() == 2 )
					abreOps();
			}
		} );
		
		tabOSS.addMouseListener( new MouseAdapter() {

			public void mouseClicked( MouseEvent mevt ) {

				if ( mevt.getSource() == tabOSS && mevt.getClickCount() == 2 )
					abreOS();
			}
		} );


		lSitOp = new JLabelPad();
		lSitOp.setForeground( Color.WHITE );
		lSitOp.setHorizontalAlignment( SwingConstants.CENTER );
		pinLb.adic( lSitOp, 0, 0, 110, 20 );

		tpnAbas.addChangeListener( this );
		txtCodOP.addFocusListener( this );
		txtSeqOP.addKeyListener( this );
		txtCodProdEst.addKeyListener( this );
		txtRefProdEst.addKeyListener( this );
		setImprimir( true );
		setAcessoPaineisNavegacao();
	}

	private void formataCampoLimpo( JTextFieldPad campo, Color cor ) {

		try {
			campo.setAtivo( false );

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private void montaDet() {

		setAltDet( 60 );
		pinDet = new JPanelPad( 440, 60 );
		setPainel( pinDet, pnDet );

		txtCodLoteProdDet.setAtivo( true );
		txtSeqItOp.setAtivo( false );
		txtQtdItOp.setAtivo( false );
		txtCodProdDet.setAtivo( false );
		txtRefProdDet.setAtivo( false );

		// FK de Lotes
		lcLoteProdDet.add( new GuardaCampo( txtCodLoteProdDet, "CodLote", "Lote", ListaCampos.DB_PK, txtDescLoteProdDet, false ) );
		lcLoteProdDet.add( new GuardaCampo( txtDescLoteProdDet, "VenctoLote", "Dt.vencto.", ListaCampos.DB_SI, false ) );
		lcLoteProdDet.add( new GuardaCampo( txtSldLiqProd, "SldLiqLote", "Saldo", ListaCampos.DB_SI, false ) );
		lcLoteProdDet.setDinWhereAdic( "CODPROD=#N", txtCodProdDet );
		lcLoteProdDet.montaSql( false, "LOTE", "EQ" );
		lcLoteProdDet.setQueryCommit( false );
		lcLoteProdDet.setReadOnly( true );
		txtCodLoteProdDet.setTabelaExterna( lcLoteProdDet, null );

		lcProdDetCod.add( new GuardaCampo( txtCodProdDet, "Codprod", "Cód.prod.", ListaCampos.DB_PK, txtDescProdDet, true ) );
		lcProdDetCod.add( new GuardaCampo( txtDescProdDet, "Descprod", "Descriçao do produto", ListaCampos.DB_SI, false ) );
		lcProdDetCod.add( new GuardaCampo( txtRefProdDet, "refprod", "referencia", ListaCampos.DB_SI, false ) );
		lcProdDetCod.add( new GuardaCampo( txtUsaLoteDet, "CLOTEPROD", "Usa Lote", ListaCampos.DB_SI, false ) );
		lcProdDetCod.montaSql( false, "PRODUTO", "EQ" );
		lcProdDetCod.setQueryCommit( false );
		lcProdDetCod.setReadOnly( true );
		txtCodProdDet.setTabelaExterna( lcProdDetCod, FProduto.class.getCanonicalName() );
		txtCodProdDet.setNomeCampo( "codprod" );

		lcProdDetRef.add( new GuardaCampo( txtRefProdDet, "refprod", "Cód.prod.", ListaCampos.DB_PK, txtDescProdDet, true ) );
		lcProdDetRef.add( new GuardaCampo( txtDescProdDet, "Descprod", "Descriçao do produto", ListaCampos.DB_SI, false ) );
		lcProdDetRef.add( new GuardaCampo( txtCodProdDet, "Codprod", "Cód.prod.", ListaCampos.DB_SI, true ) );
		lcProdDetRef.add( new GuardaCampo( txtUsaLoteDet, "CLOTEPROD", "Usa Lote", ListaCampos.DB_SI, false ) );
		lcProdDetRef.montaSql( false, "PRODUTO", "EQ" );
		lcProdDetRef.setQueryCommit( false );
		lcProdDetRef.setReadOnly( true );
		txtRefProdDet.setTabelaExterna( lcProdDetRef, FProduto.class.getCanonicalName() );
		txtRefProdDet.setNomeCampo( "refprod" );
		txtRefProdDet.setFK( true );

		setListaCampos( lcDet );
		setNavegador( navRod );

		adicCampo( txtSeqItOp, 7, 20, 50, 20, "seqitop", "Sq.", ListaCampos.DB_PK, true );

		if ( (Boolean) prefere.get( "USAREFPROD" ) ) {
			adicCampo( txtRefProdDet, 60, 20, 70, 20, "RefProd", "Ref.prod.", ListaCampos.DB_PF, txtDescProdDet, true );
			adicCampoInvisivel( txtCodProdDet, "CodProd", "Cód.prod.", ListaCampos.DB_PF, txtDescProdDet, true );
			//adicCampo( txtCodProdDet, 60, 20, 70, 20, "CodProd", "Cód.prod.", ListaCampos.DB_PF, txtDescProdDet, true );
			//txtCodProdDet.setBuscaAdic( new DLBuscaProd( con, "CODPROD", lcProdDetCod.getWhereAdic() ) );
		}
		else {
			adic( new JLabelPad( "Referência" ), 60, 0, 70, 20 );
			adic( txtRefProdDet, 60, 20, 70, 20 );
			adicCampoInvisivel( txtCodProdDet, "CodProd", "Cód.prod.", ListaCampos.DB_PF, txtDescProdDet, true );
			txtRefProdDet.setFK( true );
			txtRefProdDet.setBuscaAdic( new DLBuscaProd( con, "REFPROD", lcProdDetRef.getWhereAdic() ) );
		}

		adicDescFK( txtDescProdDet, 133, 20, 250, 20, "descprod", "Descrição do produto" );
		adicCampo( txtCodLoteProdDet, 386, 20, 90, 20, "codlote", "Lote", ListaCampos.DB_FK, false );
		adicCampo( txtQtdItOp, 479, 20, 90, 20, "qtditop", "Qtd.", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtQtdCopiaItOp, "qtdcopiaitop", "Qtd.rat.", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtCodLoteProdRat, "codloterat", "Lote rat.", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtGeraRMAAut, "GERARMA", "Rma?", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtSeqAc, "SeqAc", "", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtBloqOp, "BloqOp", "", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtPermiteAjusteItOp, "PermiteAjusteItOp", "", ListaCampos.DB_SI, false );
		
		setListaCampos( true, "ITOP", "PP" );
		lcDet.setQueryInsert( false );

		adic( btRatearItem, 572, 20, 20, 20 );
		// adic( btObs2, 600, 30, 30, 30 );

		btFinaliza.setEnabled( false );
		pnBtObs.setPreferredSize( new Dimension( 30, 30 ) );
		pnGImp.setPreferredSize( new Dimension( 110, 26 ) );
		pnGImp.add( btObs2 );

		btRMA.setEnabled( false );
		btLote.setEnabled( false );
		btDistrib.setEnabled( false );
		btCancela.setEnabled( false );
		btContrQuali.setEnabled( false );
		btSubProd.setEnabled( false );
		btRemessa.setEnabled( false );
		btRetorno.setEnabled( false );
		
		montaTab();
		
		tab.adicColuna( "" );
		tab.adicColuna( "" );
		
		tab.setTamColuna( 30, 0 ); // Seq.
		
		if ( (Boolean) prefere.get( "USAREFPROD" ) ) {
			tab.setTamColuna( 112, 1 ); // RefProd.
		}
		
		tab.setTamColuna( 145, 2 ); // Descrição do produto
		
		if ( (Boolean) prefere.get( "USAREFPROD" ) ) {
//			tab.setColunaInvisivel( 3 ); // Código do produto
		}
		
		tab.setTamColuna( 65, 4 ); // Lote
//		tab.setColunaInvisivel( 5 ); // Não identificado
		tab.setTamColuna( 65, 6 ); // Qtd.
		tab.setTamColuna( 65, 7 ); // Qtd.rat.
		tab.setTamColuna( 65, 8 ); // Lote.rat.
		tab.setTamColuna( 50, 9 ); // RMA
		
//		tab.setColunaInvisivel( 10 );		
		
		tab.setTamColuna( 13, 11 ); // Acao corretiva imagem
		tab.setTamColuna( 13, 12 ); // Bloqueio de OP imagem
	
//		tab.setColunaInvisivel( 13 );
		
		if(tab.getNumColunas()>14) {
//			tab.setColunaInvisivel( 14 );
		}

	}

	private void processaTab() {

		try {
			for ( int i = 0; tab.getNumLinhas() > i; i++ ) {

			//	if ( tab.getValor( i, 9 ) != null && ( !"".equals( tab.getValor( i, 9 ) ) ) && ( (Integer) ( tab.getValor( i, 9 ) ) ) > 0 ) {
				
				int colunaseqac = 9;

				if ( (Boolean) prefere.get( "USAREFPROD" ) ) {
					colunaseqac = 10;
				}


				if ( tab.getValor( i, colunaseqac ) != null && ( !"".equals( tab.getValor( i, colunaseqac ) ) ) && ( (Integer) ( tab.getValor( i, colunaseqac ) ) ) > 0 ) {	
					tab.setValor( imgItemCorrecao, i, 11 );
				}
				else {
					tab.setValor( imgItemComum, i, 11 );
				}
				if ( "S".equals( tab.getValor( i, colunaseqac + 1 ) ) ) {
					tab.setValor( imgItemBloqueado, i, 12 );
				}
				else {
					tab.setValor( imgBranco, i, 12 );
				}

			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private BigDecimal getQtdTotal( BigDecimal arg ) {

		BigDecimal ret = null;

		try {
			ret = arg.multiply( txtQtdPrevProdOP.getVlrBigDecimal() );
		} catch ( Exception e ) {
			ret = new BigDecimal( 0 );
		}

		ret = ret.setScale( Aplicativo.casasDec, BigDecimal.ROUND_HALF_UP );
		return ret;

	}

	private String getLote( ListaCampos lcProd, JTextFieldPad txtProd, boolean bSaldoPos ) {

		String sRet = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;
		try {

			sSQL = "SELECT MIN(L.CODLOTE) FROM EQLOTE L " + "WHERE L.CODPROD=? AND L.CODFILIAL=? " + ( bSaldoPos ? "AND L.SLDLIQLOTE>0 " : "" ) + "AND L.CODEMP=? AND " + "L.VENCTOLOTE=(SELECT MIN(VENCTOLOTE) FROM EQLOTE LS "
					+ "WHERE LS.CODPROD=L.CODPROD AND LS.CODFILIAL=L.CODFILIAL AND LS.CODEMP=L.CODEMP AND " + "LS.SLDLIQLOTE>0 AND VENCTOLOTE >= CAST('today' AS DATE))";

			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, txtProd.getVlrInteger().intValue() );
			ps.setInt( 2, lcProd.getCodFilial() );
			ps.setInt( 3, Aplicativo.iCodEmp );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				String sCodLote = rs.getString( 1 );
				if ( sCodLote != null )
					sRet = sCodLote.trim();
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar lote!\n" + err );
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}
		return sRet;
	}
	
	private Boolean temRemessa() {
		boolean ret = false;
		
		try {
		
			ret = temRemessaRetorno( "E" );
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
		
	}
	
	private Boolean temRetorno() {
		boolean ret = false;
		
		try {
		
			ret = temRemessaRetorno( "R" );
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
		
	}
	
	private boolean temRemessaRetorno( String tipo ) {

		boolean ret = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();

		try {

			sql.append("select first 1 ");
			sql.append("it.tipoexterno, fs.externafase ");
			sql.append("from ppitestrutura it, ppfase fs ");
			sql.append("where fs.codemp=it.codempfs and fs.codfilial=it.codfilialfs and fs.codfase=it.codfase and ");
			sql.append("it.codemp=? and it.codfilial=? and it.codprod=? and it.seqest=? and ");
			sql.append("fs.externafase='S' and it.tipoexterno=?");

			ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, lcCampos.getCodEmp() );
			ps.setInt( 2, lcCampos.getCodFilial() );
			ps.setInt( 3, txtCodProdEst.getVlrInteger() );
			ps.setInt( 4, txtSeqEst.getVlrInteger() );
			ps.setString( 5, tipo );
			
			rs = ps.executeQuery();
			
			if ( rs.next() ) {
				
				ret = true;
				
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar remessa/retorno!\n" + err );
		}
		finally {
			ps = null;
			rs = null;
			sql = null;
		}
		return ret;
	}

	private void getRma() {

		String codop = null;
		String seqop = null;
		String sitrma = null;
		String sitaprovrma = null;
		String sitexprma = null;
		StringBuffer sql = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;
		int iLin = 0;

		try {

			codop = txtCodOP.getVlrString();
			seqop = txtSeqOP.getVlrString();
			tabRMA.limpa();

			if ( ( codop.trim().equals( "" ) ) || ( seqop.trim().equals( "" ) ) ) {
				return;
			}

			sql.append( "SELECT R.CODRMA, IT.CODPROD,IT.REFPROD,PD.DESCPROD,IT.SITITRMA,IT.DTINS," );
			sql.append( "IT.QTDITRMA,IT.QTDEXPITRMA,PD.SLDPROD " );
			sql.append( "FROM EQRMA R, EQITRMA IT, EQPRODUTO PD " );
			sql.append( "WHERE R.CODEMP=IT.CODEMP AND R.CODFILIAL=IT.CODFILIAL AND R.CODRMA=IT.CODRMA " );
			sql.append( "AND PD.CODEMP=IT.CODEMP AND PD.CODFILIAL=IT.CODFILIAL AND PD.CODPROD=IT.CODPROD " );
			sql.append( "AND R.CODEMPOF=? AND R.CODFILIALOF=? AND R.CODOP=? AND R.SEQOP=?" );

			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, lcCampos.getCodFilial() );
			ps.setInt( 3, Integer.parseInt( codop ) );
			ps.setInt( 4, Integer.parseInt( seqop ) );

			rs = ps.executeQuery();

			while ( rs.next() ) {

				tabRMA.adicLinha();

				sitrma = rs.getString( 5 );
				sitaprovrma = rs.getString( 6 );
				sitexprma = rs.getString( 7 );

				if ( sitrma.equalsIgnoreCase( "PE" ) ) {
					imgColunaRMA = imgPendente;
				}
				else if ( sitrma.equalsIgnoreCase( "CA" ) ) {
					imgColunaRMA = imgCancelada;
				}
				else if ( sitrma.equalsIgnoreCase( "EF" ) || sitexprma.equals( "EP" ) || sitexprma.equals( "ET" ) ) {
					imgColunaRMA = imgExpedida;
				}
				else if ( sitrma.equalsIgnoreCase( "AF" ) || sitaprovrma.equals( "AP" ) || sitaprovrma.equals( "AT" ) ) {
					imgColunaRMA = imgAprovada;
				}

				tabRMA.setValor( imgColunaRMA, iLin, 0 );// SitItRma

				tabRMA.setValor( new Integer( rs.getInt( 1 ) ), iLin, 1 );// CodRma

				tabRMA.setValor( rs.getString( "CODPROD" ) == null ? "" : rs.getString( "CODPROD" ) + "", iLin, 2 );// CodProd
				tabRMA.setValor( rs.getString( "DESCPROD" ) == null ? "" : rs.getString( "DESCPROD" ).trim() + "", iLin, 3 );// DescProd
				tabRMA.setValor( rs.getString( "DTINS" ) == null ? "" : StringFunctions.sqlDateToStrDate( rs.getDate( "DTINS" ) ) + "", iLin, 4 );// Dt Req
				tabRMA.setValor( rs.getString( "QTDITRMA" ) == null ? "" : rs.getString( "QTDITRMA" ) + "", iLin, 5 );// Qtd Req
				tabRMA.setValor( rs.getString( "QTDEXPITRMA" ) == null ? "" : rs.getString( "QTDEXPITRMA" ) + "", iLin, 6 );// Qdt Exp
				tabRMA.setValor( rs.getString( "SLDPROD" ) == null ? "" : rs.getString( "SLDPROD" ) + "", iLin, 7 );// Saldo Prod

				iLin++;

			}

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela EQRMA!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sql = null;
			sitrma = null;
			sitaprovrma = null;
			sitexprma = null;
		}
	}
	
	public boolean situacaoRMA(){
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		ResultSet rs = null;
		boolean situacao = false;
		
		try {
		    sql.append( "SELECT IR.coditrma, ir.sititrma ");
	        sql.append( "FROM EQRMA R , eqitrma IR ");
	        sql.append( "WHERE R.CODEMPOF=? AND R.CODFILIALOF=? AND R.CODOP=? AND R.SEQOP=?  AND IR.CODEMP=R.CODEMP AND IR.CODFILIAL=R.CODFILIAL AND IR.CODRMA=R.CODRMA ");
		
			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, lcCampos.getCodFilial() );
			ps.setInt( 3, txtCodOP.getVlrInteger());
			ps.setInt( 4, txtSeqOP.getVlrInteger() );
			
			rs = ps.executeQuery();
			
			while(rs.next()){
				situacao = "EF".equals(rs.getString( "SITITRMA" ));
				
				if(situacao){
					break;
				}
			}
			
		} catch (SQLException e) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela EQRMA!\n" + e.getMessage(), true, con, e );
			e.printStackTrace(); 
		}
		
		return situacao;
	}
	

	private void getOPS() {

		String codop;
		String seqop;
		int iLin = 0;
		StringBuffer sql = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			tabOPS.limpa();

			codop = txtCodOP.getVlrString();
			seqop = txtSeqOP.getVlrString();

			if ( ( codop.equals( "" ) ) || ( seqop.equals( "" ) ) ) {
				return;
			}

			sql.append( "SELECT OP.CODOP,OP.SEQOP,PD.CODPROD,OP.SEQEST,PD.DESCPROD,ET.DESCEST, " );
			sql.append( "OP.QTDPREVPRODOP, OP.QTDFINALPRODOP,OP.DTFABROP,OP.SITOP " );
			sql.append( "FROM PPOP OP, EQPRODUTO PD, PPESTRUTURA ET " );
			sql.append( "WHERE ET.CODEMP=OP.CODEMPPD AND ET.CODFILIAL=OP.CODFILIALPD AND ET.CODPROD=OP.CODPROD " );
			sql.append( "AND ET.SEQEST=OP.SEQEST AND PD.CODEMP=OP.CODEMPPD AND PD.CODFILIAL=OP.CODFILIALPD " );
			sql.append( "AND PD.CODPROD=OP.CODPROD AND OP.CODEMP=? AND OP.CODFILIAL=? AND OP.CODOP=? " );
			sql.append( "ORDER BY OP.SEQOP" );

			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, lcCampos.getCodEmp() );
			ps.setInt( 2, lcCampos.getCodFilial() );
			ps.setInt( 3, txtCodOP.getVlrInteger().intValue() );
			rs = ps.executeQuery();

			while ( rs.next() ) {

				tabOPS.adicLinha();

				if ( rs.getInt( "SEQOP" ) == 0 ) {
					tabOPS.setValor( imgOPPrinc, iLin, 0 );
				}
				else {
					tabOPS.setValor( imgOPSub, iLin, 0 );
				}

				tabOPS.setValor( new Integer( rs.getInt( "CODOP" ) ), iLin, 1 );
				tabOPS.setValor( new Integer( rs.getInt( "SEQOP" ) ), iLin, 2 );
				tabOPS.setValor( new Integer( rs.getInt( "CODPROD" ) ), iLin, 3 );
				tabOPS.setValor( new Integer( rs.getInt( "SEQEST" ) ), iLin, 4 );
				tabOPS.setValor( rs.getString( "DESCPROD" ), iLin, 5 );
				tabOPS.setValor( rs.getString( "DESCEST" ), iLin, 6 );

				iLin++;

			}

			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela EQRMA!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sql = null;
		}
	}
	
	private void getOSS() {

		String codop;
		String seqop;
		int iLin = 0;
		StringBuffer sql = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			tabOSS.limpa();

			if ( "".equals(txtCodOP.getVlrString()) )  {
				return;
			}

			sql.append( "SELECT RM.TICKET, IR.CODITRECMERC, OS.CODITOS, RM.CODCLI, CL.RAZCLI, IR.NUMSERIE " );
			sql.append( "FROM EQRECMERC RM, EQITRECMERC IR, EQITRECMERCITOS OS, VDCLIENTE CL " );
			sql.append( "WHERE OS.CODEMP=? AND OS.CODFILIAL=? AND OS.TICKET=? AND OS.CODITRECMERC=? AND OS.CODITOS=? " );
			sql.append( "AND IR.CODEMP=OS.CODEMP AND IR.CODFILIAL=OS.CODFILIAL AND IR.TICKET=OS.TICKET AND IR.CODITRECMERC=OS.CODITRECMERC " );
			sql.append( "AND RM.CODEMP=IR.CODEMP AND RM.CODFILIAL=IR.CODFILIAL AND RM.TICKET=IR.TICKET " );
			sql.append( "AND CL.CODEMP=RM.CODEMPCL AND CL.CODFILIAL=RM.CODFILIALCL AND CL.CODCLI=RM.CODCLI " );

			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, lcCampos.getCodEmp() );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQRECMERC" ) );
			ps.setInt( 3, txtTicket.getVlrInteger() );
			ps.setInt( 4, txtCodItRecMerc.getVlrInteger() );
			ps.setInt( 5, txtCodItOS.getVlrInteger() );
			rs = ps.executeQuery();

			while ( rs.next() ) {

				tabOSS.adicLinha();

				tabOSS.setValor( rs.getInt( "TICKET" ) , iLin, 0 );
				tabOSS.setValor( rs.getInt( "CODITRECMERC" ), iLin, 1 );
				tabOSS.setValor( rs.getInt( "CODITOS" ), iLin, 2 );
				tabOSS.setValor( rs.getInt( "NUMSERIE" ), iLin, 3 );
				tabOSS.setValor( rs.getInt( "CODCLI" ), iLin, 4 );
				tabOSS.setValor( rs.getString( "RAZCLI" ), iLin, 5 );

				iLin++;

			}

			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela de ordens de serviço!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sql = null;
		}
	}
	
	private void getEntradaParcial() {

		String codop;
		String seqop;
		int iLin = 0;
		StringBuffer sql = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			tabEntradaParcial.limpa();

			if ( "".equals(txtCodOP.getVlrString()) )  {
				return;
			}

			sql.append( "select et.seqent, et.dtent, et.qtdent,et.hent,obsent,idusuins from ppopentrada et " );
			sql.append( "where et.codemp=? and et.codfilial=? and et.codop=? and et.seqop=? " );

			ps = con.prepareStatement( sql.toString() );
			
			ps.setInt( 1, lcCampos.getCodEmp() );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PPOP" ) );
			ps.setInt( 3, txtCodOP.getVlrInteger() );
			ps.setInt( 4, txtSeqOP.getVlrInteger() );
			
			rs = ps.executeQuery();

			while ( rs.next() ) {

				tabEntradaParcial.adicLinha();

				tabEntradaParcial.setValor( rs.getInt( "seqent" ) , iLin, 0 );
				tabEntradaParcial.setValor( rs.getDate( "dtent" ), iLin, 1 );
				tabEntradaParcial.setValor( rs.getTime( "hent" ).toString(), iLin, 2 );
				tabEntradaParcial.setValor( rs.getBigDecimal( "qtdent" ), iLin, 3 );				
				tabEntradaParcial.setValor( rs.getString( "obsent" ), iLin, 4 );
				tabEntradaParcial.setValor( rs.getString( "idusuins" ), iLin, 5 );
				
				iLin++;

			}

			con.commit();
			
		} 
		catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela de entradas parciais!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} 
		finally {
			ps = null;
			rs = null;
			sql = null;
		}
	}

	private boolean temCQ() {

		boolean ret = true;
		StringBuffer sSQL = new StringBuffer();
		PreparedStatement ps = null;

		sSQL.append( "SELECT COUNT(*) FROM PPOPCQ WHERE CODEMP=? AND CODFILIAL=? AND CODOP=? AND SEQOP=?" );

		try {

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PPOPCQ" ) );
			ps.setInt( 3, txtCodOP.getVlrInteger() );
			ps.setInt( 4, txtSeqOP.getVlrInteger() );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() && rs.getInt( 1 ) == 0 ) {
				ret = true;
			}
			else {
				ret = false;
			}

		} catch ( SQLException e ) {

			e.printStackTrace();
		}

		return ret;
	}

	private void getTipoMov() {

		if ( txtCodTpMov.getVlrString().equals( "" ) ) {
			PreparedStatement ps = null;
			ResultSet rs = null;
			String sSQL = "SELECT CODTIPOMOV FROM SGPREFERE5 WHERE CODEMP=? AND CODFILIAL=?";

			try {
				ps = con.prepareStatement( sSQL );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE5" ) );
				rs = ps.executeQuery();
				if ( rs.next() ) {
					if ( rs.getString( 1 ) != null ) {
						iCodTpMov = new Integer( rs.getInt( 1 ) );
					}
					else {
						iCodTpMov = new Integer( 0 );
						Funcoes.mensagemInforma( null, "Não existe um tipo de movimento padrão para OP definido nas preferências!" );
					}
				}
				rs.close();
				ps.close();
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao buscar documento de preferências!\n" + err.getMessage() );
			} finally {
				ps = null;
				rs = null;
				sSQL = null;
			}
		}
	}

	public void carregaProduto() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = "SELECT CLOTEPROD,VLRDENSIDADE FROM EQPRODUTO WHERE CODEMP=? AND CODFILIAL=? AND CODPROD=?";
		try {
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			ps.setInt( 3, txtCodProdEst.getVlrInteger().intValue() );
			rs = ps.executeQuery();

			if ( rs.next() ) {
				txtUsaLoteEst.setVlrString( rs.getString( "CLOTEPROD" ) );
				txtVlrDensidade.setVlrBigDecimal( rs.getBigDecimal( "VLRDENSIDADE" ) == null ? new BigDecimal( 1 ) : rs.getBigDecimal( "VLRDENSIDADE" ) );
			}

			rs.close();
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar obrigatoriedade de lote no produto!\n", true, con, err );
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}
	}

	private void simularOP() {

		String sSQL = null;
		Object[] linha = new Object[ 8 ];

		try {

			tabSimu.limpa();

			tpnAbas.setSelectedIndex( 0 );
			tpnAbas.setEnabledAt( 0, true );

			if ( txtUsaDensidadeOP.getVlrString().equals( "S" ) ) {
				txtQtdPrevProdOP.setVlrBigDecimal( txtQtdSugProdOP.getVlrBigDecimal().multiply( txtVlrDensidade.getVlrBigDecimal() ) );
			}
			else {
				txtQtdPrevProdOP.setVlrBigDecimal( txtQtdSugProdOP.getVlrBigDecimal() );
			}

			sSQL = "SELECT IT.CODFASE, IT.SEQITEST, IT.CODPRODPD, P.DESCPROD, " 
				+ "P.CODUNID, coalesce(IT.QTDITEST/cast(e.qtdest as numeric(15,7)),0) qtditest, P.SLDLIQPROD, IT.RMAAUTOITEST, IT.QTDFIXA " 
				+ "FROM PPESTRUTURA E, PPITESTRUTURA IT, EQPRODUTO P " 
				+ "WHERE E.CODEMP=? AND E.CODFILIAL=? AND E.CODPROD=? AND E.SEQEST=? "
				+ "AND E.CODEMP=IT.CODEMP AND E.CODFILIAL=IT.CODFILIAL " 
				+ "AND E.CODPROD=IT.CODPROD AND E.SEQEST=IT.SEQEST " 
				+ "AND P.CODEMP=IT.CODEMPPD AND P.CODFILIAL=IT.CODFILIALPD AND P.CODPROD=IT.CODPRODPD " 
				+ "ORDER BY IT.CODFASE, IT.SEQITEST";

			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PPITESTRUTURA" ) );
			ps.setInt( 3, txtCodProdEst.getVlrInteger().intValue() );
			ps.setInt( 4, txtSeqEst.getVlrInteger().intValue() );

			ResultSet rs = ps.executeQuery();

			while ( rs.next() ) {

				linha[ 0 ] = new Integer( rs.getInt( "CODFASE" ) );
				linha[ 1 ] = new Integer( rs.getInt( "CODPRODPD" ) );
				linha[ 2 ] = rs.getString( "DESCPROD" ) != null ? rs.getString( "DESCPROD" ).trim() : "";
				linha[ 3 ] = rs.getString( "CODUNID" ) != null ? rs.getString( "CODUNID" ) : "";
				
				BigDecimal qtditest = rs.getBigDecimal( "QTDITEST" ).setScale( 7, BigDecimal.ROUND_HALF_UP );
				
				//linha[ 4 ] = rs.getBigDecimal( "QTDITEST" ) != null ? rs.getBigDecimal( "QTDITEST" ) : new BigDecimal( 0 );
				linha[ 4 ] = qtditest;
				
				linha[ 5 ] = rs.getBigDecimal( "SLDLIQPROD" ) != null ? rs.getBigDecimal( "SLDLIQPROD" ) : new BigDecimal( 0 );
				linha[ 5 ] = ( (BigDecimal) linha[ 5 ] ).setScale( Aplicativo.casasDec, BigDecimal.ROUND_HALF_UP );
				linha[ 6 ] = "S".equals( rs.getString( "QTDFIXA" ) ) ? ( rs.getBigDecimal( "QTDITEST" ) != null ? rs.getBigDecimal( "QTDITEST" ) : new BigDecimal( 0 ) ) : getQtdTotal( ( rs.getBigDecimal( "QTDITEST" ) != null ? rs.getBigDecimal( "QTDITEST" ) : new BigDecimal( 0 ) ) );
				linha[ 7 ] = rs.getString( "RMAAUTOITEST" ) != null ? rs.getString( "RMAAUTOITEST" ) : "";

				tabSimu.adicLinha( linha );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao consulrar tabela de PPITESTRUTURA.\n" + e.getMessage() );
		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao simular OP.\n" + e.getMessage() );
		}
	}

	private void abreRma() {
		
		Container cont = getContentPane();
		while ( true ) {
			if ( cont instanceof FPrincipal )
				break;
			cont = cont.getParent();
		}
		int iRma = ( (Integer) tabRMA.getValor( tabRMA.getLinhaSel(), 1 ) ).intValue();
		if ( !((FPrincipal) cont ).temTela( "Requisição de material" ) ) {
			FRma tela = new FRma();
			fPrim.criatela( "Requisição de material", tela, con );
			tela.exec( iRma );
			tela.addInternalFrameListener( new InternalFrameAdapter() {
				public void internalFrameClosed( InternalFrameEvent ievt ) {
					lcCampos.carregaDados();
					desabilitaBtFinaliza();
				}
			} );
		}
	}

	private void abreOps() {

		try {
			Integer codop = ( (Integer) tabOPS.getValor( tabOPS.getLinhaSel(), 1 ) );
			Integer seqop = ( (Integer) tabOPS.getValor( tabOPS.getLinhaSel(), 2 ) );

			txtCodOP.setVlrInteger( codop );
			txtSeqOP.setVlrInteger( seqop );
			lcCampos.carregaDados();
		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}
	
	private void abreOS() {

		FOrdemServico ordemservico = null;

		try {

			if ( tabOSS.getLinhaSel() > -1 ) {

				if ( Aplicativo.telaPrincipal.temTela( FRecMerc.class.getName() ) ) {
					ordemservico = (FOrdemServico) Aplicativo.telaPrincipal.getTela( FOrdemServico.class.getName() );
				}
				else {
					ordemservico = new FOrdemServico( false );
					Aplicativo.telaPrincipal.criatela( "Recepção de mercadorias", ordemservico, con );
				}

				int ticket = (Integer) tabOSS.getValor( tabOSS.getLinhaSel(), 0 );

				ordemservico.exec( ticket, null, this );
			}
			else {
				Funcoes.mensagemInforma( this, "Não há nenhum registro selecionado para edição!" );
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	
	private Integer getSeqEntradaParcial(){
		Integer ret = 1;
		ResultSet rs = null;
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		try {
			
			sql.append("select coalesce(max(seqent),0) + 1 seq from ppopentrada where codemp=? and codfilial=? and codop=? and seqop=?");
			
			ps = con.prepareStatement( sql.toString() );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, lcCampos.getCodFilial() );
			ps.setInt( 3, txtCodOP.getVlrInteger() );
			ps.setInt( 4,txtSeqOP.getVlrInteger() );
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				
				ret = rs.getInt( "seq" );
				
			}
		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
		
	}
	
	
	private void insereEntradaParcial(BigDecimal qtdent, String obsent, Date dtent, String hent) {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		
		try {
			
			sql.append( "insert into ppopentrada (codemp, codfilial, codop, seqop, seqent, dtent, hent, qtdent, obsent ) " );
			sql.append( "values (?,?,?,?,?,?,?,?,?)" );
			
			ps = con.prepareStatement( sql.toString() );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, lcCampos.getCodFilial() );
			ps.setInt( 3, txtCodOP.getVlrInteger() );
			ps.setInt( 4, txtSeqOP.getVlrInteger() );
			
			ps.setInt( 5, getSeqEntradaParcial() );
			ps.setDate( 6, Funcoes.dateToSQLDate( dtent ) );
			ps.setTime( 7, Funcoes.strTimeTosqlTime( hent ) );

			ps.setBigDecimal( 8, qtdent );
			ps.setString( 9, obsent );
			
			ps.execute();
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void deletaEntradaParcial( Integer seqent ) {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		
		try {
			
			sql.append( "delete from ppopentrada where codemp=? and codfilial=? and codop=? and seqop=? and seqent=? " );
			
			ps = con.prepareStatement( sql.toString() );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, lcCampos.getCodFilial() );
			ps.setInt( 3, txtCodOP.getVlrInteger() );
			ps.setInt( 4, txtSeqOP.getVlrInteger() );
			ps.setInt( 5, seqent );
			
			ps.execute();			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void finalizaOP() {

		//String expedirRMA = getExpedirRMA();
		
		
		// Se a aba de entrada parcial estiver selecioada... 
		
		if ( tpnAbas.getSelectedIndex() == 5 ) {		
			
			 if("FN".equals( txtSitOp.getVlrString())) {
				 
				 Funcoes.mensagemInforma( this, "Não é possível realizar novas entradas, pois a O.P está finalizada! " );				 
				 
			 }
			 else {
			 			
				DLFinalizaOPParcial dl = new DLFinalizaOPParcial( this, txtQtdPrevProdOP.getVlrBigDecimal().subtract( txtQtdFinalProdOP.getVlrBigDecimal() ) );
				
				dl.setVisible( true );
	 
				if ( dl.OK == false ) {
					dl.dispose();
					return;
				}
	
				BigDecimal qtdent = dl.getQtdEnt();
				String obsent = dl.getObs();
				Date dtent = dl.getDtEnt();
				String hent = dl.getHEnt();
				
				insereEntradaParcial( qtdent, obsent, dtent, hent );
				
				lcCampos.carregaDados();
				
				tpnAbas.setSelectedIndex( 5 );
				
			 }
			
		}
		else {
			if ( fPrim.temTela( "Fases da OP" ) == false ) {
				
				int codop = txtCodOP.getVlrInteger().intValue();
				int	seqop = txtSeqOP.getVlrInteger().intValue();
				int seqest = txtSeqEst.getVlrInteger().intValue();

				FOPFase tela = new FOPFase( codop, seqop, seqest, this, (Boolean) prefere.get( "USAREFPROD" ), (Boolean) prefere.get( "VALIDAFASE" ) );
				
				fPrim.criatela( "Fases da OP", tela, con );
				
				tela.setConexao( con );
				
			}
		}

	}

	private boolean temSldLote() {

		boolean bRet = false;

		try {

			String sSaida = "";
			int iSldNeg = 0;
			int iTemp = 0;
			float fSldLote = 0f;

			String sSQL = "SELECT SLDLOTE FROM EQLOTE WHERE CODEMP=? AND CODFILIAL=? AND CODPROD=? AND CODLOTE=? ";

			for ( int i = 0; i < tab.getRowCount(); i++ ) {

				PreparedStatement ps = con.prepareStatement( sSQL );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, Aplicativo.iCodFilial );

				if ( !(Boolean) prefere.get( "USAREFPROD" ) ) {
					ps.setInt( 3, ( (Integer) tab.getValor( i, 1 ) ).intValue() );
				}
				else {
					ps.setInt( 3, ( (Integer) tab.getValor( i, 3 ) ).intValue() );
					
				}	
				
				ps.setString( 4, (String) tab.getValor( i, 4 ) );

				ResultSet rs = ps.executeQuery();

				if ( rs.next() ) {
					fSldLote = rs.getFloat( "SLDLOTE" );
				}

				if ( fSldLote < ConversionFunctions.stringCurrencyToBigDecimal( (String) tab.getValor( i, 6 ) ).subtract( ConversionFunctions.stringCurrencyToBigDecimal( (String) tab.getValor( i, 7 ) ) ).floatValue() && !"".equals( (String) tab.getValor( i, 4 ) ) ) {
					iSldNeg++;
					sSaida += "\nProduto: " + tab.getValor( i, 1 ) + StringFunctions.replicate( " ", 20 ) + "Lote: " + tab.getValor( i, 4 );
				}

				rs.close();
				ps.close();
			}

			con.commit();

			if ( iSldNeg > 0 ) {

				if ( (Boolean) prefere.get( "RATAUTO" ) ) {
					bloquearOPSemSaldo( true );
					Funcoes.mensagemInforma( this, "Esta OP será bloqueada devido a falta de saldo para alguns itens.\n" + sSaida );
					return true;
				}

				iTemp = Funcoes.mensagemConfirma( this, "Estes lotes possuem saldo menor que a quantidade solicitada." + sSaida + "\n\nDeseja gerar RMA com lote sem saldo?" );
				if ( iTemp == JOptionPane.NO_OPTION ) {
					bRet = false;
				}
				else if ( iTemp == JOptionPane.YES_OPTION ) {
					bRet = true;
				}
			}
			else {
				bRet = true;
			}
		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro ao verificar quantidade de Lote\n" + e.getMessage(), true, con, e );
			e.printStackTrace();
		} catch ( Exception e ) {
			Funcoes.mensagemErro( this, "Erro ao verificar quantidade de Lote\n" + e.getMessage(), true, con, e );
			e.printStackTrace();
		}

		return bRet;
	}

	private boolean liberaRMA() {

		boolean retorno = true;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sUsaLote = null;
		String sSQL = null;
		String codLote = null;
		Integer codProd = null;
		try {
			for ( int i = 0; i < lcDet.getTab().getRowCount(); i++ ) {
				
				if ( !(Boolean) prefere.get( "USAREFPROD" ) ) {
					codProd = (Integer) lcDet.getTab().getValor( i, 1 );	
				}
				else {
					codProd = (Integer) lcDet.getTab().getValor( i, 3 );
				}	
				
				codLote = "" + lcDet.getTab().getValor( i, 4 );
				

				sSQL = "SELECT CLOTEPROD FROM EQPRODUTO WHERE CODEMP=? AND CODFILIAL=? AND CODPROD=?";

				ps = con.prepareStatement( sSQL );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
				ps.setInt( 3, codProd.intValue() );
				rs = ps.executeQuery();
				if ( rs.next() )
					sUsaLote = rs.getString( 1 );

				if ( sUsaLote.equals( "S" ) ) {
					if ( !FOP.existeLote( con, codProd.intValue(), codLote ) ) {
						retorno = false;
						break;
					}
				}

				rs.close();
				ps.close();
			}

			con.commit();

		} catch ( SQLException ex ) {
			Funcoes.mensagemErro( this, "Erro ao verificar condições para RMA\n" + ex.getMessage() );
			ex.printStackTrace();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		} finally {
			codProd = null;
			sUsaLote = null;
			sSQL = null;
			codLote = null;
			ps = null;
			rs = null;
		}
		return retorno;
	}

	private ResultSet itensRma() {

		StringBuffer sql = new StringBuffer();
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {
			sql.append( "SELECT GERARMA FROM PPITOP WHERE CODEMP=? AND CODFILIAL=? AND CODOP=? AND SEQOP=? AND GERARMA='S'" );

			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PPITOP" ) );
			ps.setInt( 3, txtCodOP.getVlrInteger().intValue() );
			ps.setInt( 4, txtSeqOP.getVlrInteger().intValue() );

			rs = ps.executeQuery();
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return rs;
	}
	
	private Integer getNumItensRMA() {

		StringBuffer sql = new StringBuffer();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Integer ret = 0;
		
		try {
			sql.append( "SELECT COUNT(*) FROM PPITOP WHERE CODEMP=? AND CODFILIAL=? AND CODOP=? AND SEQOP=? AND GERARMA='S'" );

			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PPITOP" ) );
			ps.setInt( 3, txtCodOP.getVlrInteger().intValue() );
			ps.setInt( 4, txtSeqOP.getVlrInteger().intValue() );

			rs = ps.executeQuery();
			
			if(rs.next()) {
				ret = rs.getInt( 1 );
			}
			
			rs = ps.executeQuery();
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return ret;
	}

	private boolean temDistrib() {

		StringBuffer sql = new StringBuffer();
		boolean ret = false;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			sql.append( "SELECT count(*) FROM PPDISTRIB D, PPOP O, PPESTRUTURA ED, PPFASE F," );
			sql.append( "PPITESTRUTURA ID, PPESTRUTURA E , EQPRODUTO PD " );
			sql.append( "WHERE " );
			sql.append( "D.CODEMP=O.CODEMPPD AND D.CODFILIAL=O.CODFILIALPD " );
			sql.append( "AND D.CODPROD=O.CODPROD AND D.SEQEST=O.SEQEST " );
			sql.append( "AND ED.CODEMP=D.CODEMPDE AND ED.CODFILIAL=D.CODFILIALDE " );
			sql.append( "AND ED.CODPROD=D.CODPRODDE AND ED.SEQEST=D.SEQESTDE " );
			sql.append( "AND F.CODEMP=D.CODEMPFS AND F.CODFILIAL=D.CODFILIALFS " );
			sql.append( "AND F.CODFASE=D.CODFASE AND ID.CODEMP=ED.CODEMP " );
			sql.append( "AND ID.CODFILIAL=ED.CODFILIAL AND ID.CODPROD=ED.CODPROD " );
			sql.append( "AND ID.SEQEST=ED.SEQEST AND ID.CODEMPPD=D.CODEMP " );
			sql.append( "AND ID.CODFILIALPD=D.CODFILIAL AND ID.CODPRODPD=D.CODPROD " );
			sql.append( "AND E.CODEMP=D.CODEMP AND E.CODFILIAL=D.CODFILIAL " );
			sql.append( "AND E.CODPROD=D.CODPROD AND E.SEQEST=D.SEQEST " );
			sql.append( "AND D.CODEMPDE=PD.CODEMP AND D.CODFILIALDE=PD.CODFILIAL " );
			sql.append( "AND D.CODPRODDE=pd.codprod AND O.CODEMP=? AND O.CODFILIAL=? " );
			sql.append( "AND O.CODOP=? AND O.SEQOP=?" );

			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PPDISTRIB" ) );
			ps.setInt( 3, txtCodOP.getVlrInteger().intValue() );
			ps.setInt( 4, txtSeqOP.getVlrInteger().intValue() );

			rs = ps.executeQuery();

			if ( rs.next() && rs.getInt( 1 ) == 0 ) {
				ret = true;
			}
			else {
				ret = false;
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return ret;
	}

	private boolean faltaRma() {

		boolean ret = true;

		try {
//			ret = ( (ResultSet) itensRma() ).getFetchSize() > 0;
			ret = getNumItensRMA() > 0;
			
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return ret;
	}

	public void geraRMA() {

		String sSQL = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;

		try {

			rs = itensRma();

			if ( rs.next() ) {
				try {
					if ( temSldLote() ) {
						boolean confirmar = (Boolean) prefere.get( "RATAUTO" );
						if ( !confirmar ) {
							confirmar = Funcoes.mensagemConfirma( this, "Confirma a geração de RMA para a OP:" + txtCodOP.getVlrString() + " SEQ:" + txtSeqOP.getVlrString() + "?" ) == JOptionPane.YES_OPTION;
						}
						if ( confirmar ) {
							ps2 = con.prepareStatement( "EXECUTE PROCEDURE EQGERARMASP(?,?,?,?)" );
							ps2.setInt( 1, Aplicativo.iCodEmp );
							ps2.setInt( 2, ListaCampos.getMasterFilial( "PPOP" ) );
							ps2.setInt( 3, txtCodOP.getVlrInteger().intValue() );
							ps2.setInt( 4, txtSeqOP.getVlrInteger().intValue() );
							ps2.execute();
							ps2.close();

							con.commit();

							try {
								ps3 = con.prepareStatement( "SELECT CODRMA FROM EQRMA WHERE CODEMP=? AND CODFILIAL=? AND CODEMPOF=CODEMP AND CODFILIALOF=? AND CODOP=? AND SEQOP=?" );
								ps3.setInt( 1, Aplicativo.iCodEmp );
								ps3.setInt( 2, ListaCampos.getMasterFilial( "PPITOP" ) );
								ps3.setInt( 3, ListaCampos.getMasterFilial( "PPOP" ) );
								ps3.setInt( 4, txtCodOP.getVlrInteger().intValue() );
								ps3.setInt( 5, txtSeqOP.getVlrInteger().intValue() );

								rs2 = ps3.executeQuery();
								String sRma = "";
								while ( rs2.next() ) {
									sRma += rs2.getString( 1 ) + " - ";
								}
								if ( sRma.length() > 0 ) {
									Funcoes.mensagemInforma( this, "Foram geradas as seguintes RMA:\n" + sRma );
								}

								rs2.close();
							} catch ( Exception err ) {
								Funcoes.mensagemErro( this, "Erro ao buscar RMA criada", true, con, err );
								err.printStackTrace();
							}
						}
					}
				} catch ( SQLException err ) {
					System.out.println( err.getMessage() );
					Funcoes.mensagemErro( this, "Erro ao criar RMA\n" + err.getMessage(), true, con, err );
					err.printStackTrace();
				} catch ( Exception err ) {
					Funcoes.mensagemErro( this, "Erro ao criar RMA", true, con, err );
					err.printStackTrace();
				}
			}
			else {
				Funcoes.mensagemInforma( this, "Não há itens para gerar RMA.\n " + "Os itens não geram RMA automaticamente\n" + "ou o processo de geração de RMA já foi efetuado." );
			}

			rs.close();

			con.commit();
			
			lcCampos.carregaDados();
			
		} catch ( Exception err ) {
			Funcoes.mensagemErro( this, "Erro ao consultar RMA", true, con, err );
			err.printStackTrace();
		} finally {
			sSQL = null;
			rs = null;
			rs2 = null;
			ps2 = null;
			ps3 = null;
		}
	}

	private void ratearItem( boolean bPergunta ) {

		boolean bResposta = true;
		BigDecimal bdVlrNova = null;
		BigDecimal bdQtdDigitada = null;
		String sCodLote = null;
		try {
			if ( bPergunta ) {
				bResposta = ( Funcoes.mensagemConfirma( Aplicativo.framePrinc, "Deseja realmente ratear este item para a OP?" ) == JOptionPane.YES_OPTION );
			}

			if ( bResposta ) {
				try {

					FFDialogo diag = new FFDialogo( this );
					diag.setTitulo( "Rateio" );
					diag.setAtribos( 250, 160 );
					diag.adic( new JLabelPad( "Quantidade: ", SwingConstants.RIGHT ), 7, 10, 80, 20 );
					diag.adic( txtQtdDigRat, 90, 10, 120, 20 );
					diag.adic( new JLabelPad( "Lote: ", SwingConstants.RIGHT ), 7, 40, 80, 20 );
					diag.adic( txtLoteRat, 90, 40, 120, 20 );
					diag.setVisible( true );

					if ( diag.OK ) {
						if ( !"".equals( String.valueOf( txtQtdDigRat.getVlrBigDecimal() ) ) && !"".equals( txtLoteRat.getVlrString() ) ) {
							bdQtdDigitada = txtQtdDigRat.getVlrBigDecimal();
							sCodLote = txtLoteRat.getVlrString();

							bdVlrNova = txtQtdItOp.getVlrBigDecimal().subtract( bdQtdDigitada );

							if ( bdVlrNova.compareTo( txtQtdItOp.getVlrBigDecimal() ) > 0 || bdVlrNova.compareTo( new BigDecimal( 0 ) ) <= 0 ) {
								Funcoes.mensagemErro( Aplicativo.framePrinc, "Quantidade inválida!" );
								ratearItem( false );
							}

							txtQtdCopiaItOp.setVlrBigDecimal( bdQtdDigitada );
							txtQtdItOp.setVlrBigDecimal( bdVlrNova );
							txtCodLoteProdRat.setVlrString( sCodLote );
							lcDet.edit();
							lcDet.post();
							lcCampos.carregaDados();
						}
					}

					txtQtdDigRat.setVlrString( "" );
					txtLoteRat.setVlrString( "" );
				} catch ( Exception err ) {
					Funcoes.mensagemErro( Aplicativo.framePrinc, "Valor inválido!" );
					err.printStackTrace();
					return;
				}
			}
		} catch ( Exception err ) {
			Funcoes.mensagemErro( Aplicativo.framePrinc, "Erro no rateamento!" );
			err.printStackTrace();
			return;
		} finally {
			bdVlrNova = null;
			bdQtdDigitada = null;
			sCodLote = null;
		}
	}

	private boolean ratearOp() {

		boolean result = false;

		try {

			HashMap<Integer, List<String>> lotes = new HashMap<Integer, List<String>>();
			Integer seq;
			Integer codprod;
			String lote;
			BigDecimal quantidade;
			BigDecimal novaquantidade;

			StringBuilder sql = new StringBuilder();

			sql.append( "SELECT SEQITOP, CODPROD, CODLOTE, QTDITOP " );
			sql.append( "FROM PPITOP " );
			sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODOP=? AND SEQOP=? " );
			sql.append( "ORDER BY SEQITOP" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PPITOP" ) );
			ps.setInt( 3, txtCodOP.getVlrInteger() );
			ps.setInt( 4, txtSeqOP.getVlrInteger() );

			ResultSet rs = ps.executeQuery();

			while ( rs.next() ) {

				seq = rs.getInt( "SEQITOP" );
				codprod = rs.getInt( "CODPROD" );
				lote = rs.getString( "CODLOTE" );
				quantidade = rs.getBigDecimal( "QTDITOP" );

				if ( lotes.get( codprod ) == null ) {
					lotes.put( codprod, new ArrayList<String>() );
				}

				novaquantidade = verificaSaldoLote( codprod, lote, quantidade );

				if ( novaquantidade.floatValue() > 0 ) {
					lotes.get( codprod ).add( lote );
					result = rateiaItemSemSaldo( seq, codprod, novaquantidade, lotes.get( codprod ) );
				}
				else {
					result = true;
				}
			}

			con.commit();

			lcCampos.carregaDados();

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return result;
	}

	private BigDecimal verificaSaldoLote( Integer codprod, String lote, BigDecimal quantidade ) throws Exception {

		BigDecimal novaquantidade = new BigDecimal( "0.00" );
		BigDecimal saldolote = new BigDecimal( "0.00" );

		StringBuilder sql = new StringBuilder();

		sql.append( "SELECT L.SLDLOTE FROM EQLOTE L " );
		sql.append( "WHERE L.CODEMP=? AND L.CODFILIAL=? AND L.CODPROD=? AND L.CODLOTE=? " );

		PreparedStatement ps = con.prepareStatement( sql.toString() );
		ps.setInt( 1, Aplicativo.iCodEmp );
		ps.setInt( 2, ListaCampos.getMasterFilial( "EQLOTE" ) );
		ps.setInt( 3, codprod );
		ps.setString( 4, lote );

		ResultSet rs = ps.executeQuery();

		if ( rs.next() ) {
			saldolote = rs.getBigDecimal( "SLDLOTE" );
		}

		rs.close();
		ps.close();

		if ( quantidade.max( saldolote ) == quantidade ) {
			novaquantidade = quantidade.subtract( saldolote );
		}

		return novaquantidade;
	}

	private boolean rateiaItemSemSaldo( Integer seq, Integer codprod, BigDecimal quantidade, List<String> lotesutilizados ) throws Exception {

		boolean rateio = false;
		boolean novorateio = false;

		String lotes = "";

		for ( int i = 0; i < lotesutilizados.size(); i++ ) {
			if ( i > 0 ) {
				lotes += ",";
			}
			lotes += "'" + lotesutilizados.get( i ) + "'";
		}

		String lote = null;
		BigDecimal saldo = new BigDecimal( "0.00" );

		StringBuilder sql = new StringBuilder();
		sql.append( "SELECT FIRST 1 L.CODLOTE, L.SLDLOTE " );
		sql.append( "FROM EQLOTE L " );
		sql.append( "WHERE L.CODEMP=? AND L.CODFILIAL=? AND L.CODPROD=? AND " );
		sql.append( "L.SLDLIQLOTE>0 AND L.VENCTOLOTE>=cast('today' as date) AND " );
		sql.append( "NOT L.CODLOTE IN ( " + lotes + " ) " );
		sql.append( "ORDER BY L.VENCTOLOTE, L.CODLOTE " );

		PreparedStatement ps = con.prepareStatement( sql.toString() );
		ps.setInt( 1, Aplicativo.iCodEmp );
		ps.setInt( 2, ListaCampos.getMasterFilial( "EQLOTE" ) );
		ps.setInt( 3, codprod );

		ResultSet rs = ps.executeQuery();

		if ( rs.next() ) {
			lote = rs.getString( "CODLOTE" );
			saldo = rs.getBigDecimal( "SLDLOTE" );
		}

		rs.close();
		ps.close();

		if ( saldo.floatValue() > 0 ) {
			if ( quantidade.max( saldo ) == quantidade ) {
				novorateio = true;
			}

			sql = new StringBuilder();
			sql.append( "UPDATE PPITOP SET QTDCOPIAITOP=?, CODLOTERAT=?, BLOQOP='N' " );
			sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODOP=? AND SEQOP=? AND SEQITOP=?" );

			ps = con.prepareStatement( sql.toString() );
			ps.setBigDecimal( 1, quantidade );
			ps.setString( 2, lote );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "PPITOP" ) );
			ps.setInt( 5, txtCodOP.getVlrInteger() );
			ps.setInt( 6, txtSeqOP.getVlrInteger() );
			ps.setInt( 7, seq );

			ps.executeUpdate();
			ps.close();

			rateio = true;

			if ( novorateio ) {
				sql = new StringBuilder();
				sql.append( "SELECT MAX(SEQITOP) FROM PPITOP WHERE CODEMP=? AND CODFILIAL=? AND CODOP=? AND SEQOP=?" );

				ps = con.prepareStatement( sql.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "PPITOP" ) );
				ps.setInt( 3, txtCodOP.getVlrInteger() );
				ps.setInt( 4, txtSeqOP.getVlrInteger() );

				rs = ps.executeQuery();

				if ( rs.next() ) {
					seq = rs.getInt( 1 );
				}

				lotesutilizados.add( lote );
				rateio = rateiaItemSemSaldo( seq, codprod, quantidade.subtract( saldo ), lotesutilizados );
			}
		}
		else {

			sql = new StringBuilder();
			sql.append( "UPDATE PPITOP SET BLOQOP='S' " );
			sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODOP=? AND SEQOP=? AND SEQITOP=?" );

			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PPITOP" ) );
			ps.setInt( 3, txtCodOP.getVlrInteger() );
			ps.setInt( 4, txtSeqOP.getVlrInteger() );
			ps.setInt( 5, seq );

			ps.executeUpdate();
			ps.close();

			rateio = true;
		}

		return rateio;
	}

	private void reprocessaItens() {

		try {

			StringBuilder sql = new StringBuilder();
			sql.append( "UPDATE EQITRMA SET SITITRMA='PE' " );
			sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODRMA IN " );
			sql.append( "(SELECT CODRMA FROM EQRMA " );
			sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODEMPOF=? AND CODFILIALOF=? AND CODOP=? AND SEQOP=?)" );
			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQITRMA" ) );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "EQRMA" ) );
			ps.setInt( 5, Aplicativo.iCodEmp );
			ps.setInt( 6, ListaCampos.getMasterFilial( "PPOPFASE" ) );
			ps.setInt( 7, txtCodOP.getVlrInteger() );
			ps.setInt( 8, txtSeqOP.getVlrInteger() );
			ps.executeUpdate();
			ps.close();

			con.commit();

			sql = new StringBuilder();
			sql.append( "DELETE FROM EQITRMA " );
			sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODRMA IN " );
			sql.append( "(SELECT CODRMA FROM EQRMA " );
			sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODEMPOF=? AND CODFILIALOF=? AND CODOP=? AND SEQOP=?)" );
			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQITRMA" ) );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "EQRMA" ) );
			ps.setInt( 5, Aplicativo.iCodEmp );
			ps.setInt( 6, ListaCampos.getMasterFilial( "PPOPFASE" ) );
			ps.setInt( 7, txtCodOP.getVlrInteger() );
			ps.setInt( 8, txtSeqOP.getVlrInteger() );
			ps.executeUpdate();
			ps.close();

			con.commit();

			sql = new StringBuilder();
			sql.append( "DELETE FROM EQRMA R " );
			sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODEMPOF=? AND CODFILIALOF=? AND CODOP=? AND SEQOP=? " );
			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQITRMA" ) );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "PPOPFASE" ) );
			ps.setInt( 5, txtCodOP.getVlrInteger() );
			ps.setInt( 6, txtSeqOP.getVlrInteger() );
			ps.executeUpdate();
			ps.close();

			con.commit();

			sql = new StringBuilder();
			sql.append( "DELETE FROM PPITOP WHERE CODEMP=? AND CODFILIAL=? AND CODOP=? AND SEQOP=? " );
			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PPITOP" ) );
			ps.setInt( 3, txtCodOP.getVlrInteger() );
			ps.setInt( 4, txtSeqOP.getVlrInteger() );
			ps.executeUpdate();
			ps.close();

			sql = new StringBuilder();
			sql.append( "DELETE FROM PPOPFASE WHERE CODEMP=? AND CODFILIAL=? AND CODOP=? AND SEQOP=? " );
			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PPOPFASE" ) );
			ps.setInt( 3, txtCodOP.getVlrInteger() );
			ps.setInt( 4, txtSeqOP.getVlrInteger() );
			ps.executeUpdate();
			ps.close();

			sql = new StringBuilder();
			sql.append( "DELETE FROM PPITRETCP WHERE CODEMP=? AND CODFILIAL=? AND CODOP=? AND SEQOP=? " );
			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PPITRETCP" ) );
			ps.setInt( 3, txtCodOP.getVlrInteger() );
			ps.setInt( 4, txtSeqOP.getVlrInteger() );
			ps.executeUpdate();
			ps.close();

			sql = new StringBuilder();
			sql.append( "DELETE FROM PPRETCP WHERE CODEMP=? AND CODFILIAL=? AND CODOP=? AND SEQOP=? " );
			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PPRETCP" ) );
			ps.setInt( 3, txtCodOP.getVlrInteger() );
			ps.setInt( 4, txtSeqOP.getVlrInteger() );
			ps.executeUpdate();
			ps.close();

			con.commit();

			sql = new StringBuilder();
			sql.append( "EXECUTE PROCEDURE PPITOPSP01(?, ?, ?, ?)" );
			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PPITOP" ) );
			ps.setInt( 3, txtCodOP.getVlrInteger() );
			ps.setInt( 4, txtSeqOP.getVlrInteger() );
			ps.executeUpdate();
			ps.close();

			con.commit();

			if ( ratearOp() ) {
				Funcoes.mensagemInforma( this, "Itens foram reprocessados com sucesso." );
				bloquearOPSemSaldo( false );
			}

			geraRMA();

			lcCampos.carregaDados();

		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro ao reprocessar itens!\n" + e.getMessage(), true, con, e );
			e.printStackTrace();
		}
	}

	private void bloquearOPSemSaldo( boolean bloquear ) {

		try {
			StringBuilder sql = new StringBuilder();

			sql.append( "UPDATE PPOP SET SITOP='" + ( bloquear ? "BL" : "PE" ) + "' " );
			sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODOP=? AND SEQOP=?" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PPOP" ) );
			ps.setInt( 3, txtCodOP.getVlrInteger() );
			ps.setInt( 4, txtSeqOP.getVlrInteger() );
			ps.executeUpdate();
			ps.close();

			con.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
	}

	public static boolean existeLote( DbConnection cn, int iCodProd, String sCodLote ) {

		boolean bRet = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = "SELECT CODLOTE FROM EQLOTE WHERE CODEMP=? AND CODFILIAL=? AND CODPROD=? AND CODLOTE=?";
		try {
			ps = cn.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQLOTE" ) );
			ps.setInt( 3, iCodProd );
			ps.setString( 4, sCodLote );
			rs = ps.executeQuery();
			if ( rs.next() )
				bRet = true;

			rs.close();
			ps.close();
			cn.commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( null, "Erro ao buscar existencia do lote!\n", true, cn, err );
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}
		return bRet;
	}

	public static Object[] gravaLote( DbConnection cn, boolean bInsere, String sCodModLote, String sUsaLoteEst, String sModLote, int iCodProd, Date dtFabProd, int iNroDiasValid, String sCodLote ) {

		Object[] retorno = null;
		ModLote objMl = null;
		try {
			if ( ! ( sCodModLote.equals( "" ) ) ) {
				if ( sCodLote == null ) {
					objMl = new ModLote();
					objMl.setTexto( sModLote );
					sCodLote = objMl.getLote( new Integer( iCodProd ), dtFabProd, cn );
				}
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime( dtFabProd );
				cal.add( GregorianCalendar.DAY_OF_YEAR, iNroDiasValid );
				Date dtVenctoLote = cal.getTime();
				retorno = new Object[ 3 ];
				retorno[ 0 ] = sCodLote;
				retorno[ 1 ] = dtVenctoLote;
				retorno[ 2 ] = new Boolean( false );
				if ( ( !existeLote( cn, iCodProd, sCodLote ) ) && ( bInsere ) ) {
					if ( Funcoes.mensagemConfirma( null, "Deseja criar o lote " + sCodLote.trim() + " ?" ) == JOptionPane.YES_OPTION ) {
						PreparedStatement ps = null;
						String sSql = "INSERT INTO EQLOTE (CODEMP,CODFILIAL,CODPROD,CODLOTE,DINILOTE,VENCTOLOTE) VALUES(?,?,?,?,?,?)";
						try {
							ps = cn.prepareStatement( sSql );
							ps.setInt( 1, Aplicativo.iCodEmp );
							ps.setInt( 2, ListaCampos.getMasterFilial( "EQLOTE" ) );
							ps.setInt( 3, iCodProd );
							ps.setString( 4, sCodLote );
							ps.setDate( 5, Funcoes.dateToSQLDate( dtFabProd ) );
							ps.setDate( 6, Funcoes.dateToSQLDate( dtVenctoLote ) );
							if ( ps.executeUpdate() == 0 )
								Funcoes.mensagemInforma( null, "Não foi possível inserir registro na tabela de Lotes!" );

							cn.commit();
							retorno[ 2 ] = new Boolean( true );
						} catch ( SQLException err ) {
							Funcoes.mensagemErro( null, "Erro ao inserir registro na tabela de Lotes!\n" + err.getMessage(), true, cn, err );
						} finally {
							ps = null;
							sSql = null;
						}
					}
				}
				else if ( bInsere )
					Funcoes.mensagemInforma( null, "Lote já cadastrado para o produto!" );
			}
		} finally {
			sCodLote = null;
			objMl = null;
		}
		return retorno;
	}

	public void gravaLote( boolean bInsere ) {

		Object[] lote = gravaLote( con, bInsere, txtCodModLote.getVlrString(), txtUsaLoteEst.getVlrString(), txtModLote.getVlrString(), txtCodProdEst.getVlrInteger().intValue(), txtDtFabProd.getVlrDate(), txtNroDiasValid.getVlrInteger().intValue(), null );
		try {
			if ( lote != null ) {
				txtCodLoteProdEst.setVlrString( (String) lote[ 0 ] );
				txtDtValidOP.setVlrDate( (Date) lote[ 1 ] );
			}
		} finally {
			lote = null;
		}
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSql = "SELECT CLASSOP FROM SGPREFERE5 WHERE CODEMP=? AND CODFILIAL=?";
		String sClassOP = "";
		Vector<Object> vParamOP = new Vector<Object>();
		DLROP dl = new DLROP();

		dl.setVisible( true );

		if ( dl.OK == false ) {
			dl.dispose();
			return;
		}

		String ordem = dl.getValor();

		try {
			try {
				ps = con.prepareStatement( sSql );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE5" ) );
				rs = ps.executeQuery();

				if ( rs.next() ) {
					if ( rs.getString( "CLASSOP" ) != null ) {
						sClassOP = rs.getString( "CLASSOP" ).trim();
					}
				}
				rs.close();
				ps.close();
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao carregar a tabela SGPREFERE5!\n" + err.getMessage(), true, con, err );
			}
			if ( sClassOP.trim().equals( "" ) )
				Funcoes.mensagemErro( this, "Não existe org.freedom.layout para ordem de produção. \n Cadastre o org.freedom.layout no documento de preferências do módulo de produção \n e tente novamente." );
			else {

				try {

					FPrinterJob dlGr = null;
					HashMap<String, Object> hParam = new HashMap<String, Object>();

					hParam.put( "CODEMP", Aplicativo.iCodEmp );
					hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "PPOP" ) );
					hParam.put( "CODOP", txtCodOP.getVlrInteger() );
					hParam.put( "SEQOP", txtSeqOP.getVlrInteger() );					
					hParam.put( "SUBREPORT_DIR", "org/freedom/layout/op/" );
					hParam.put( "ORDEM", ordem );

					dlGr = new FPrinterJob( "layout/op/" + sClassOP, "Ordem de produção", "", this, hParam, con );

					if ( bVisualizar==TYPE_PRINT.VIEW ) {
						dlGr.setVisible( true );
					}
					else {
						try {
							JasperPrintManager.printReport( dlGr.getRelatorio(), true );
						} catch ( Exception err ) {
							Funcoes.mensagemErro( this, "Erro na impressão de Ordem de produção!" + err.getMessage(), true, con, err );
						}
					}
				} catch ( Exception err ) {
					Funcoes.mensagemInforma( this, "Não foi possível carregar o leiaute de Ordem de produção!\n" + err.getMessage() );
					err.printStackTrace();
				}
			}
		} finally {
			ps = null;
			rs = null;
			sSql = null;
			sClassOP = null;
			vParamOP = null;
			// leiOP = null;
			System.gc();
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		super.actionPerformed( evt );
		if ( evt.getSource() == btImp ) {
			imprimir( TYPE_PRINT.PRINT);
		}
		else if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
		else if ( evt.getSource() == btFinaliza ) {
		
			finalizaOP();
		}
		else if ( evt.getSource() == btRMA ) {
			geraRMA();
		}
		else if ( evt.getSource() == btLote ) {
			gravaLote( true );
		}
		else if ( evt.getSource() == btRatearItem ) {
			ratearItem( true );
		}
		else if ( evt.getSource() == btDistrib ) {
			distribuicao();
		}
		else if ( evt.getSource() == btObs2 ) {
			observacao();
		}
		else if ( evt.getSource() == btCancela ) {
			cancelaOP();
		}
		else if ( evt.getSource() == btObs ) {
			DLObsJust dl = new DLObsJust( con, txtSeqOP.getVlrInteger(), txtCodOP.getVlrInteger() );
			dl.setVisible( true );
		}
		else if ( evt.getSource() == btContrQuali ) {
			contrQualidade();
		}
		else if ( evt.getSource() == btReprocessaItens ) {
			reprocessaItens();
		}
		else if ( evt.getSource() == btAdicProdutoEstrutura ) {
			buscaEstrutura();
		}
		else if ( evt.getSource() == btSubProd ) {
			if ( fPrim.temTela( "Subprodutos" ) == false ) {
				
				int codop = txtCodOP.getVlrInteger().intValue();
				int	seqop = txtSeqOP.getVlrInteger().intValue();
				int seqest = txtSeqEst.getVlrInteger().intValue();

				FOPSubProd tela = new FOPSubProd( codop, seqop, seqest, this, (Boolean) prefere.get( "USAREFPROD" )  );
				fPrim.criatela( "Subprodutos", tela, con );
				tela.setConexao( con );
			}
		}
		else if ( evt.getSource() == btRemessa ) {
			geraRemessa();
		}
		else if ( evt.getSource() == btRetorno ) {
			geraRetorno();
		}
	}

	private void observacao() {

		StringBuffer sSQL = new StringBuffer();
		StringBuffer sSQLupdate = new StringBuffer();
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sObs = "";

		sSQL.append( "SELECT P.OBSOP FROM PPOP P WHERE P.CODEMP=? AND P.CODFILIAL=? AND P.CODOP=? AND P.SEQOP=? " );
		sSQLupdate.append( "UPDATE PPOP SET OBSOP=? WHERE CODEMP=? AND CODFILIAL=? AND CODOP=? AND SEQOP=?" );

		try {

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PPOP" ) );
			ps.setInt( 3, txtCodOP.getVlrInteger() );
			ps.setInt( 4, txtSeqOP.getVlrInteger() );
			rs = ps.executeQuery();

			if ( rs.next() ) {
				sObs = rs.getString( "OBSOP" );
			}

		} catch ( SQLException err ) {

			err.printStackTrace();
		}

		FObservacao dl = new FObservacao( sObs );
		dl.setVisible( true );

		if ( dl.OK ) {

			try {
				ps = con.prepareStatement( sSQLupdate.toString() );
				ps.setString( 1, dl.getTexto() );
				ps.setInt( 2, Aplicativo.iCodEmp );
				ps.setInt( 3, ListaCampos.getMasterFilial( "PPOP" ) );
				ps.setInt( 4, txtCodOP.getVlrInteger() );
				ps.setInt( 5, txtSeqOP.getVlrInteger() );

				ps.executeUpdate();

				ps.close();

				con.commit();

			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao inserir observação na OP!\n" + err.getMessage(), true, con, err );
			}
		}
	}

	// Busca Numero de ops relacioadas
	private int getQtdOPS() {

		int ret = 0;
		StringBuffer sql = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			sql.append( "select count(*) from ppop opr " );
			sql.append( "where opr.codemp=? and opr.codfilial=? and opr.codop=? " );
			sql.append( "and opr.seqop<>?" );

			ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, lcCampos.getCodEmp() );
			ps.setInt( 2, lcCampos.getCodFilial() );
			ps.setInt( 3, txtCodOP.getVlrInteger().intValue() );
			ps.setInt( 4, txtSeqOP.getVlrInteger().intValue() );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				ret = rs.getInt( 1 );
			}

			con.commit();

		} catch ( Exception e ) {
			Funcoes.mensagemErro( null, "Erro ao buscar O.P's. relacionadas!", true, con, e );
		}
		return ret;
	}

	private void bloqueiaOp() {

		String sitop = null;
		boolean lote = false;
		boolean rma = false;

		try {

			sitop = txtSitOp.getVlrString();
			lote = existeLote( con, txtCodProdEst.getVlrInteger(), txtCodLoteProdEst.getVlrString() );
			rma = faltaRma() && liberaRMA();

			setAcessoBotoes(btContrQuali, !temCQ() );
			setAcessoBotoes(btDistrib, !temDistrib() );
			btObs.setVisible( false );
			btReprocessaItens.setVisible( false );

			setAcessoBotoes(btFinaliza, true );
			if ( sitop.equals( "PE" ) ) {

				setAcessoBotoes(btLote, !lote );
				setAcessoBotoes(btRMA, rma );

				setAcessoBotoes(btCancela, true );

				txtCodProdEst.setAtivo( false );
				txtSeqEst.setAtivo( false );

				txtQtdSugProdOP.setAtivo( true );
				txtCodLoteProdEst.setAtivo( true );
				txtDtValidOP.setAtivo( true );
				txtDtFabProd.setAtivo( true );
				txtCodAlmoxEst.setAtivo( true );

				txtCodLoteProdDet.setAtivo( true );
				navRod.setAtivo( Navegador.BT_NOVO, true );
				navRod.setAtivo( Navegador.BT_EDITAR, true );
				navRod.setAtivo( Navegador.BT_EXCLUIR, true );
				navRod.setAtivo( Navegador.BT_SALVAR, true );

				Date dtfab = Funcoes.getDataPura( txtDtFabProd.getVlrDate() );
				Date dtatual = Funcoes.getDataPura( new Date() );

				if ( dtfab.before( dtatual ) ) {
					SitOp = "Atrasada";
					pinLb.setBackground( cor( 210, 50, 30 ) );
				}
				else {
					SitOp = "Pendente";
					pinLb.setBackground( cor( 240, 180, 10 ) );
				}

				lSitOp.setText( SitOp );

			}
			else if ( sitop.equals( "FN" ) ) {

				btLote.setEnabled( false );
				setAcessoBotoes(btRMA, rma );
				// btFinaliza.setEnabled( false );
				// btDistrb.setEnabled( true );
				setAcessoBotoes(btCancela, true );

				txtCodProdEst.setAtivo( false );
				txtSeqEst.setAtivo( false );
				txtQtdSugProdOP.setAtivo( false );
				txtCodLoteProdEst.setAtivo( false );
				txtDtValidOP.setAtivo( false );
				txtDtFabProd.setAtivo( false );
				txtCodAlmoxEst.setAtivo( false );

				txtCodLoteProdDet.setAtivo( false );

				navRod.setAtivo( Navegador.BT_NOVO, false );
				navRod.setAtivo( Navegador.BT_EDITAR, false );
				navRod.setAtivo( Navegador.BT_EXCLUIR, false );
				navRod.setAtivo( Navegador.BT_SALVAR, false );

				SitOp = "Finalizada";
				lSitOp.setText( SitOp );
				pinLb.setBackground( cor( 0, 170, 30 ) );

			}
			else if ( sitop.equals( "CA" ) ) {

				setAcessoBotoes(btLote, false );
				setAcessoBotoes(btRMA,  false );
				// btFinaliza.setEnabled( false );
				setAcessoBotoes(btDistrib, false );
				setAcessoBotoes(btCancela, false );

				txtCodProdEst.setAtivo( false );
				txtSeqEst.setAtivo( false );
				txtQtdSugProdOP.setAtivo( false );
				txtCodLoteProdEst.setAtivo( false );
				txtDtValidOP.setAtivo( false );
				txtDtFabProd.setAtivo( false );
				txtCodAlmoxEst.setAtivo( false );

				txtCodLoteProdDet.setAtivo( false );

				navRod.setAtivo( Navegador.BT_NOVO, false );
				navRod.setAtivo( Navegador.BT_EDITAR, false );
				navRod.setAtivo( Navegador.BT_EXCLUIR, false );
				navRod.setAtivo( Navegador.BT_SALVAR, false );

				btObs.setVisible( true );
				SitOp = "Cancelada";
				lSitOp.setText( SitOp );

				pinLb.setBackground( cor( 210, 50, 30 ) );

			}
			else if ( sitop.equals( "BL" ) ) {

				btLote.setEnabled( false );
				btRMA.setEnabled( false );
				// btFinaliza.setEnabled( false );
				setAcessoBotoes( btDistrib, false );
				btCancela.setEnabled( false );

				txtCodProdEst.setAtivo( false );
				txtSeqEst.setAtivo( false );
				txtQtdSugProdOP.setAtivo( false );
				txtCodLoteProdEst.setAtivo( false );
				txtDtValidOP.setAtivo( false );
				txtDtFabProd.setAtivo( false );
				txtCodAlmoxEst.setAtivo( false );

				txtCodLoteProdDet.setAtivo( false );

				navRod.setAtivo( Navegador.BT_NOVO, false );
				navRod.setAtivo( Navegador.BT_EDITAR, false );
				navRod.setAtivo( Navegador.BT_EXCLUIR, false );
				navRod.setAtivo( Navegador.BT_SALVAR, false );

				btReprocessaItens.setVisible( true );
				SitOp = "Bloqueada";
				lSitOp.setText( SitOp );

				pinLb.setBackground( Color.BLUE );

			}
			else if ( sitop.equals( "" ) ) {
				btLote.setEnabled( false );
				btRMA.setEnabled( false );
				// btFinaliza.setEnabled( false );
				// btDistrb.setEnabled( false );
				btCancela.setEnabled( false );

				txtCodProdEst.setAtivo( true );
				txtSeqEst.setAtivo( true );

				txtQtdSugProdOP.setAtivo( true );
				txtCodLoteProdEst.setAtivo( true );
				txtDtValidOP.setAtivo( true );
				txtDtFabProd.setAtivo( true );
				txtCodAlmoxEst.setAtivo( true );
				txtCodLoteProdDet.setAtivo( true );

				SitOp = "";
				lSitOp.setText( SitOp );
				pinLb.setBackground( cor( 238, 238, 238 ) );
			}
			
			setAcessoBotoes(btSubProd, true );
			

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	private void cancelaOP() {

		StringBuffer sql = new StringBuffer();
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		DLJustCanc dl = null;

		try {

			if ( tpnAbas.getSelectedIndex() == 5 ) {		
				
				Integer seqent = (Integer) tabEntradaParcial.getValor( tabEntradaParcial.getSelectedRow(), 0 );
				
				if( seqent>0 ) {
					
					if(Funcoes.mensagemConfirma( this, "Confirma a exclusão da entrada de produção?" )==JOptionPane.YES_OPTION) {
					
						deletaEntradaParcial( seqent );
					
						lcCampos.carregaDados();
						
						tpnAbas.setSelectedIndex( 5 );
						
					}
					
				}
				else {
					
					Funcoes.mensagemInforma( this, "Selecione uma entrada para exclusão!" );
					
				}
				 
			}
			else if ( Funcoes.mensagemConfirma( null, "Confirma o cancelamento da O.P.?" ) == JOptionPane.OK_OPTION ) {

				dl = new DLJustCanc();
				dl.setVisible( true );

				if ( dl.OK ) {

					int qtdops = getQtdOPS();

					if ( qtdops > 0 ) {

						if ( Funcoes.mensagemConfirma( null, "Existe" + ( qtdops > 1 ? "m " : " " ) + qtdops + " Ordem" + ( qtdops > 1 ? "s " : " " ) + "de Produção ativa" + ( qtdops > 1 ? "s " : " " ) + ", vinculadas a esta O.P!\n" + "Deseja cancelar também?" ) == JOptionPane.OK_OPTION ) {

							sql.append( "update ppop opr set opr.sitop='CA', JUSTIFICCANC=? " );
							sql.append( "where opr.codemp=? and opr.codfilial=? and opr.codop=? " );
							sql.append( "and opr.seqop<>?" );

							ps1 = con.prepareStatement( sql.toString() );

							ps1.setString( 1, dl.getValor() );
							ps1.setInt( 2, lcCampos.getCodEmp() );
							ps1.setInt( 3, lcCampos.getCodFilial() );
							ps1.setInt( 4, txtCodOP.getVlrInteger().intValue() );
							ps1.setInt( 5, txtSeqOP.getVlrInteger().intValue() );

							ps1.executeUpdate();
							ps1.close();

						}
					}

					sql.delete( 0, sql.length() );

					sql.append( "UPDATE PPOP SET SITOP='CA', JUSTIFICCANC=? " );
					sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODOP=? AND SEQOP=?" );

					ps2 = con.prepareStatement( sql.toString() );

					ps2.setString( 1, dl.getValor() );
					ps2.setInt( 2, lcCampos.getCodEmp() );
					ps2.setInt( 3, lcCampos.getCodFilial() );
					ps2.setInt( 4, txtCodOP.getVlrInteger().intValue() );
					ps2.setInt( 5, txtSeqOP.getVlrInteger().intValue() );

					ps2.executeUpdate();
					ps2.close();

					con.commit();

					lcCampos.carregaDados();

				}
				else {
					dl.cancel();
				}
			}
		} catch ( Exception e ) {
			Funcoes.mensagemErro( this, "Erro ao realizar a conversão de produtos!\n" + e.getMessage() );
			e.printStackTrace();
		}
	}

	
	public Integer testaCodPK() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer retorno = new Integer( 0 );

		try {
			ps = con.prepareStatement( "SELECT ISEQ FROM SPGERANUM(?,?,?)" );
			ps.setInt( 1, Aplicativo.iCodEmp );

			ps.setInt( 2, ListaCampos.getMasterFilial( "PPOP" ) );
			ps.setString( 3, "OP" );

			rs = ps.executeQuery();
			rs.next();

			retorno = new Integer( rs.getString( "ISEQ" ) );

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao confirmar número da OP!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
		}

		return retorno;

	}

	private void distribuicao() {

		Object[] sValores = new Object[ 9 ];

		try {
			lcCampos.carregaDados();

			sValores[ 0 ] = txtCodOP.getVlrInteger();
			sValores[ 1 ] = txtSeqOP.getVlrInteger();
			sValores[ 2 ] = txtCodProdEst.getVlrInteger();
			sValores[ 3 ] = txtRefProdEst.getVlrString();
			sValores[ 4 ] = txtSeqEst.getVlrInteger();
			sValores[ 5 ] = txtDescEst.getVlrString();
			sValores[ 6 ] = txtQtdFinalProdOP.getVlrBigDecimal();
			sValores[ 7 ] = txtQtdPrevProdOP.getVlrBigDecimal();
			sValores[ 8 ] = txtCodLoteProdEst.getVlrString();

			DLDistrib dl = new DLDistrib( con, this, (Boolean) prefere.get( "USAREFPROD" ) );
			dl.carregaCampos( sValores );
			dl.carregaTabela( txtCodOP.getVlrInteger().intValue(), txtSeqOP.getVlrInteger().intValue() );
			dl.setVisible( true );

			dl.dispose();

		} catch ( Exception e ) {

			e.printStackTrace();
		}
	}

	private void contrQualidade() {

		Object[] sValores = new Object[ 8 ];

		try {

			lcCampos.carregaDados();

			sValores[ 0 ] = txtCodOP.getVlrInteger();
			sValores[ 1 ] = txtSeqOP.getVlrInteger();
			sValores[ 2 ] = txtCodProdEst.getVlrInteger();
			sValores[ 3 ] = txtRefProdEst.getVlrString();
			sValores[ 4 ] = txtSeqEst.getVlrInteger();
			sValores[ 5 ] = txtDescEst.getVlrString();
			sValores[ 6 ] = txtQtdFinalProdOP.getVlrBigDecimal();
			sValores[ 7 ] = txtQtdPrevProdOP.getVlrBigDecimal();

			DLContrQualidade dl = new DLContrQualidade( con, (Boolean) prefere.get( "USAREFPROD" ) );
			dl.carregaCampos( sValores );
			dl.carregaTabela( txtCodOP.getVlrInteger().intValue(), txtSeqOP.getVlrInteger().intValue() );
			dl.setVisible( true );
			if ( dl.OK ) {
				lcCampos.carregaDados();
			}
		} catch ( Exception e ) {

			e.printStackTrace();

		}
	}

	private HashMap<String, Object> getPrefere( DbConnection con ) {

		HashMap<String, Object> retorno = new HashMap<String, Object>();
		boolean[] bRetorno = new boolean[ 1 ];
		StringBuffer sql = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			sql.append( "SELECT P1.USAREFPROD, P5.RATAUTO, coalesce(prodetapas,'S') prodetapas ");
			sql.append( ", coalesce(P5.VALIDAQTDOP,'N') VALIDAQTDOP, coalesce(P5.VALIDAFASEOP,'N') VALIDAFASE");
			sql.append( ", coalesce(P5.EDITQTDOP, 'S') EDITQTDOP, coalesce(P5.OPSEQ,'N') OPSEQ ");
			sql.append( "FROM SGPREFERE1 P1,SGPREFERE5 P5 " );
			sql.append( "WHERE P1.CODEMP=? AND P1.CODFILIAL=? " );
			sql.append( "AND P5.CODEMP=? AND P5.CODFILIAL=?" );

			bRetorno[ 0 ] = false;
			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "SGPREFERE5" ) );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				retorno.put( "USAREFPROD", new Boolean( rs.getString( "USAREFPROD" ).trim().equals( "S" ) ) );
				retorno.put( "RATAUTO", new Boolean( rs.getString( "RATAUTO" ).trim().equals( "S" ) ) );
				retorno.put( "PRODETAPAS", new Boolean( rs.getString( "prodetapas" ).trim().equals( "S" ) ) );
				retorno.put( "VALIDAQTDOP", new Boolean( rs.getString( "VALIDAQTDOP" ).trim().equals( "S" )));
				retorno.put( "VALIDAFASE", new Boolean( rs.getString( "VALIDAFASE" ).trim().equals( "S" )));
				retorno.put( "EDITQTDOP", new Boolean( rs.getString( "EDITQTDOP" ).trim().equals( "S" )));
				retorno.put( "OPSEQ", new Boolean(rs.getString( "OPSEQ" ).trim().equals( "S" )));
			}
			else {
				retorno.put( "USAREFPROD", new Boolean( false ) );
				retorno.put( "RATAUTO", new Boolean( false ) );
				retorno.put( "PRODETAPAS", new Boolean( true ) );
				retorno.put( "VALIDAQTDOP", new Boolean( false));
				retorno.put( "VALIDAFASE", new Boolean( false));
				retorno.put( "EDITQTDOP", new Boolean(true));
				retorno.put( "OPSEQ", new Boolean(false));
				Funcoes.mensagemInforma( null, "Não foram encontradas preferências para o módulo PCP!" );

			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage(), true, con, err );
		} finally {
			ps = null;
			rs = null;
			sql = null;
		}
		return retorno;
	}
	
	
	public String getExpedirRMA(){
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String expedirrma = null;
		
		try{
			sql.append( "select e.expedirrma from ppestrutura e where e.codemp=? and e.codfilial=? and e.codprod=? and e.seqest=? " );
			ps = con.prepareStatement( sql.toString() );
			int param = 1;
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "PPESTRUTURA" ));
			ps.setInt( param++, txtCodProdEst.getVlrInteger() );
			ps.setInt( param++, txtSeqEst.getVlrInteger() );
			rs = ps.executeQuery();
			
			if(rs.next()){
				expedirrma = rs.getString( "expedirrma" );
			}		
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return expedirrma;
	}
	
	
	public void desabilitaBtFinaliza(){
		String expedirrma = getExpedirRMA();
		boolean situacao = false;	
		if("S".equals( expedirrma )){
			
			situacao = situacaoRMA();
			setAcessoBotoes(btFinaliza, situacao );
		}
	}

	public void keyPressed( KeyEvent kevt ) {

		if ( kevt.getSource() == txtSeqOP )
			if ( ( (JTextFieldPad) kevt.getSource() ).getVlrString().trim().equals( "" ) )
				( (JTextFieldPad) kevt.getSource() ).setVlrInteger( new Integer( 0 ) );
	
		if ( kevt.getKeyCode() == KeyEvent.VK_ENTER ){ 
			if ( kevt.getSource() == txtCodProdEst || kevt.getSource() == txtRefProdEst )
					txtCodUnidProd.setVlrString( getCodUnid() );
		}
	}

	public void keyTyped( KeyEvent kevt ) {

	}

	public void keyReleased( KeyEvent kevt ) {

	}

	public void focusGained( FocusEvent arg0 ) {

	}

	public void focusLost( FocusEvent campo ) {

		if ( campo.getSource() == txtCodOP ) {
			if ( ( ! ( (JTextFieldPad) campo.getSource() ).getVlrString().trim().equals( "" ) ) && ( txtSeqOP.getVlrString().trim().equals( "" ) ) )
				txtSeqOP.setVlrInteger( new Integer( 0 ) );
		}
		else if ( campo.getSource() == txtQtdSugProdOP && txtQtdSugProdOP.getVlrString().trim().length() > 0 ) {
			simularOP();
		}
		else if ( campo.getSource() == txtSeqEst ) {
			if ( (Boolean) prefere.get( "USAREFPROD" ) ) {
				lcEstruturaRef.carregaDados();
			}
			else {
				lcEstruturaCod.carregaDados();
			}
		}

	}

	public void stateChanged( ChangeEvent cevt ) {

		if ( ( (JTabbedPanePad) ( cevt.getSource() ) ) == tpnAbas ) {
			if ( tpnAbas.getSelectedIndex() == 2 ) {
				if ( bBuscaRMA )
					getRma();
			}
			else if ( tpnAbas.getSelectedIndex() == 3 ) {
				if ( bBuscaOPS )
					getOPS();
			}
			else if ( tpnAbas.getSelectedIndex() == 4 ) {
				getOSS();
			}
			else if ( tpnAbas.getSelectedIndex() == 5 ) {
				getEntradaParcial();
			}
	

		}
	}

	public void beforeCarrega( CarregaEvent cevt ) {

		if ( cevt.getListaCampos() == lcCampos ) {
			
			bBuscaRMA = false;
			bBuscaOPS = false;
			
		}
	}

	public void afterCarrega( CarregaEvent cevt ) {

		String sSitOp = txtSitOp.getVlrString();
		String expedirrma =  null;
		
		try {
			
			if ( cevt.getListaCampos() == lcCampos ) {
				bloqueiaOp();
				
				desabilitaBtFinaliza();
			
				bBuscaRMA = true;
				bBuscaOPS = true;
				tabSimu.limpa();

				tpnAbas.setSelectedIndex( 1 );
				tpnAbas.setEnabledAt( 0, false );

				btAdicProdutoEstrutura.setEnabled( "S".equals( cbEstDinamica.getVlrString() ) );
				
				setAcessoBotoes(btRemessa, temRemessa( ) );
				setAcessoBotoes(btRetorno, temRetorno( ) );
				
				btPrevimp.setEnabled( ! txtSitOp.getVlrString().equals( "BL" ) );
				btImp.setEnabled( ! txtSitOp.getVlrString().equals( "BL" ) );
				txtQtdItSp.setVlrBigDecimal( getQtdSubProd() );
				txtCodUnidProd.setVlrString( getCodUnid() );
				
				if(!(Boolean) prefere.get( "EDITQTDOP" ) ){
					if ( lcCampos.getStatus() == ListaCampos.LCS_INSERT ) {
						txtQtdSugProdOP.setEditable(  true );
					} else {
						txtQtdSugProdOP.setEditable(  false );
					}
				}

			} else if ( ( cevt.getListaCampos() == lcEstruturaCod ) || ( cevt.getListaCampos() == lcEstruturaRef ) ) {

				carregaProduto();

				if ( txtQtdPrevProdOP.getVlrString().equals( "" ) ) {
					txtQtdSugProdOP.setVlrBigDecimal( txtQtdEst.getVlrBigDecimal() );
				}

				if ( ( txtCodLoteProdEst.getVlrString().equals( "" ) ) && ( txtUsaLoteEst.getVlrString().equals( "S" ) ) ) {
					txtCodLoteProdEst.setVlrString( getLote( lcEstruturaCod, txtCodProdEst, false ) );
					txtDtValidOP.setAtivo( false );

					lcLoteProdEst.setDinWhereAdic( "CODPROD=#N AND (VENCTOLOTE >= #D)", txtCodProdEst );
					lcLoteProdEst.setDinWhereAdic( "", txtDtFabProd );

					lcLoteProdEst.carregaDados();
				}
				else if ( ( txtUsaLoteEst.getVlrString().equals( "N" ) ) ) {
					txtCodLoteProdEst.setAtivo( false );
					txtDtValidOP.setAtivo( true );
				}

			} else  if ( cevt.getListaCampos() == lcLoteProdEst ) {
				txtDtValidOP.setVlrDate( txtDescLoteProdEst.getVlrDate() );
			} else 	if ( cevt.getListaCampos() == lcDet ) {
				
				if ( txtUsaLoteDet.getVlrString().equals( "S" ) ) {
					
					txtCodLoteProdDet.setVlrString( getLote( lcProdDetCod, txtCodProdDet, true ) );
					txtCodLoteProdDet.setAtivo( true );
					lcLoteProdDet.carregaDados();
					setAcessoBotoes(btRMA, liberaRMA() );
					
				}
				else if ( ( txtUsaLoteDet.getVlrString().equals( "N" ) ) ) {
					
					txtCodLoteProdDet.setAtivo( false );
					
				}
				
				if( "S".equals( txtPermiteAjusteItOp.getVlrString() )) {
					
					txtQtdItOp.setAtivo( true );
					
				}
				else {
					
					txtQtdItOp.setAtivo( false );
					
				}
				
				processaTab();
				if(tab.getNumColunas()>14) {
					tab.setColunaInvisivel( 14 );
				}
			} else 	if ( cevt.getListaCampos() == lcModLote ) {
				if ( ! ( txtCodModLote.getVlrString().equals( "" ) ) && ( txtCodLoteProdEst.getVlrString().equals( "" ) ) ) {
					gravaLote( false );
					setAcessoBotoes(btLote, true );
				}
			}
		} catch ( Exception e ) {

			e.printStackTrace();
		}
	}

	private void setAcessoPaineisNavegacao() {
		if (! "S".equals(Aplicativo.getUsuario().getAcesopveritens())) {
			pnDet.setVisible( false );
			tab.setVisible( false );
			tabEntradaParcial.setVisible( false );
			tabOPS.setVisible( false );
			tabOSS.setVisible( false );
			tabRMA.setVisible( false );
			tabSimu.setVisible( false );
			nav.btNovo.setVisible( false );
			nav.btExcluir.setVisible( false );
			navRod.setVisible( false );
			btImp.setVisible( false );
			btPrevimp.setVisible( false );
			//pnGImp.setVisible( false );
			//pnImp.setVisible( false );
		}
	}
	
	private void setAcessoBotoes(JButtonPad botaoAcesso, boolean enabled) {
		Usuario usuario = Aplicativo.getUsuario();
		if (enabled) {
			if (botaoAcesso==btLote && ! "S".equals( usuario.getAcesopbtcadlote() )) {
				enabled = false;
			} else if (botaoAcesso==btCancela && ! "S".equals( usuario.getAcesopbtcanc() )) {
				enabled = false;
			} else if (botaoAcesso==btDistrib && ! "S".equals( usuario.getAcesopbtdistr() )) {
				enabled = false;
			} else if (botaoAcesso==btFinaliza && ! "S".equals( usuario.getAcesopbtfase() )) {
				enabled = false;
			} else if (botaoAcesso==btContrQuali && ! "S".equals( usuario.getAcesopbtqualid() )) {
				enabled = false;
			} else if (botaoAcesso==btRemessa && ! "S".equals( usuario.getAcesopbtremessa() )) {
				enabled = false;
			} else if (botaoAcesso==btRetorno && ! "S".equals( usuario.getAcesopbtretorno() )) {
				enabled = false;
			} else if (botaoAcesso==btRMA && ! "S".equals( usuario.getAcesopbtrma() )) {
				enabled = false;
			} else if (botaoAcesso==btSubProd && ! "S".equals( usuario.getAcesopbtsubprod() )) {
				enabled = false;
			}
		}
		botaoAcesso.setEnabled( enabled );
	}
	
	public void beforeInsert( InsertEvent ievt ) {

		lcCampos.limpaCampos( false );
		simularOP();
		bloqueiaOp();
	}
	
	
	public boolean validaQuantidade(){
		boolean result = false;
		
		HashMap<String, BigDecimal> valores = getValoresFSC();
		BigDecimal nroPlanos = valores.get( "NROPLANOS" );
	    BigDecimal qtdPlanos = valores.get( "QTDPORPLANO" );
	    BigDecimal fatorfsc = valores.get( "FATORFSC" );
	    
	    BigDecimal qtdMinimaEtiquetas = qtdPlanos.divide( nroPlanos );
	    BigDecimal quantidadeOP = txtQtdSugProdOP.getVlrBigDecimal().multiply( fatorfsc );
	    
	    
	    if(quantidadeOP.remainder(qtdMinimaEtiquetas).compareTo(new BigDecimal(0) ) == 0){
	    	System.out.println("OK");
	    	result = true;
	    }	else {
	    
	    	BigDecimal valor = quantidadeOP.divide( qtdMinimaEtiquetas );
	    	BigDecimal qtdMinimaOP = new BigDecimal(valor.intValue()).multiply( qtdMinimaEtiquetas );
	    	BigDecimal qtdASeguirOP =qtdMinimaOP.add(  qtdMinimaEtiquetas  );
	    	//.multiply( new BigDecimal(fatorfsc.intValue()) 
	    	Funcoes.mensagemInforma( this, "Quantidade invalida!!!\nQuantidade Sugerida:\nMenor: " + qtdMinimaOP + "\nMaior: " + qtdASeguirOP );
	    }
	    
	    return result;
	}

	public void beforePost( PostEvent pevt ) {
		
		if( pevt.getListaCampos() == lcCampos){	
			
			if((Boolean) prefere.get( "VALIDAQTDOP" )){
			
				if(!validaQuantidade()){
					pevt.cancela();
				}
			}
		}	

		if ( ! ( txtQtdFinalProdOP.getVlrBigDecimal().compareTo( new BigDecimal( 0 ) ) > 0 ) ) {
			txtQtdFinalProdOP.setVlrBigDecimal( new BigDecimal( 0 ) );
			txtSitOp.setVlrString( "PE" );
		}

	}

	public void afterPost( PostEvent pevt ) {

		if ( pevt.getListaCampos() == lcCampos ) {
			if ( tpnAbas.getSelectedIndex() == 0 ) {
				tpnAbas.setSelectedIndex( 1 );
			}
			if ( (Boolean) prefere.get( "RATAUTO" ) ) {
				ratearOp();
				geraRMA();
			}
			
			btAdicProdutoEstrutura.setEnabled( "S".equals( cbEstDinamica.getVlrString() ) );
			
		}
		else if ( pevt.getListaCampos() == lcDet ) {
			setAcessoBotoes(btRMA, liberaRMA() );
		}
	}

	public void afterInsert( InsertEvent ievt ) {

		if ( ievt.getListaCampos() == lcCampos ) {
			getTipoMov();
			if ( (Boolean) prefere.get( "OPSEQ" )) {
				txtCodOP.setVlrInteger( testaCodPK() );
			}
			txtCodTpMov.setVlrInteger( iCodTpMov );
			lcTipoMov.carregaDados();
			txtDtFabProd.setVlrDate( new Date() );
			if ( txtSeqOP.getVlrString().trim().equals( "" ) ) {
				txtSeqOP.setVlrInteger( new Integer( 0 ) );
			}
			txtCodUnidProd.setVlrString( "" );
			txtQtdSugProdOP.setVlrBigDecimal( new BigDecimal(0) );
			txtQtdSugProdOP.setEditable( true );
		}
	}

	public void beforeCancel( CancelEvent cevt ) {

	}

	public void afterCancel( CancelEvent cevt ) {

	}

	public void valorAlterado( TabelaEditEvent e ) {

		if ( e.getTabela() == tab ) {
			if ( tab.getValor( tab.getNumColunas() - 1, 8 ) != null ) {
				// alterar cor....
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		prefere = getPrefere( cn );
		montaTela();
		lcEstruturaCod.setConexao( cn );
		lcEstruturaRef.setConexao( cn );
		lcProdDetCod.setConexao( cn );
		lcProdDetRef.setConexao( cn );
		lcTipoMov.setConexao( cn );
		lcLoteProdDet.setConexao( cn );
		lcLoteProdRat.setConexao( cn );
		lcLoteProdEst.setConexao( cn );
		lcAlmoxEst.setConexao( cn );
		lcModLote.setConexao( cn );
		lcCampos.carregaDados();

		if ( bnovo )
			lcCampos.insert( true );

	}

	public Color cor( int r, int g, int b ) {

		Color color = new Color( r, g, b );
		return color;
	}

	public void recarrega() {

		lcCampos.carregaDados();
	}
	
	private Integer getSeqItOP() {
		
		Integer ret = 1;
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
		
			sql.append( "select coalesce(max(io.seqitop),0) + 1 from ppitop io ");
			sql.append( "where io.codemp=? and io.codfilial=? and io.codop=? and io.seqop=? " );
			
			ps = con.prepareStatement( sql.toString() );
			
			ps.setInt( 1, lcCampos.getCodEmp() );
			ps.setInt( 2, lcCampos.getCodFilial() );
			ps.setInt( 3, txtCodOP.getVlrInteger() );
			ps.setInt( 4, txtSeqOP.getVlrInteger() );
			
			rs = ps.executeQuery();
			
			
			
			if(rs.next()) {
				ret = rs.getInt( 1 );
			}
			
			con.commit();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ret; 
	}
	
	private void insereItOpDinamica(Integer codfase, Integer codprod, String refprod, BigDecimal qtditop, String gerarma) {
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;		
		
		try {
			
			sql.append( "insert into ppitop (" );
			
			sql.append( "codemp, codfilial, codop, seqop, seqitop, ");
			sql.append( "codempfs, codfilialfs, codfase, ");
			sql.append( "codemppd, codfilialpd, codprod, refprod, ");
			sql.append( "qtditop, gerarma");
			
			sql.append( ") values ( ");
			
			sql.append( "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
			
			ps = con.prepareStatement( sql.toString() );
			
			Integer iparam = 1;
			
			ps.setInt( iparam++, lcCampos.getCodEmp() );
			ps.setInt( iparam++, lcCampos.getCodFilial() );
			ps.setInt( iparam++, txtCodOP.getVlrInteger() );
			ps.setInt( iparam++, txtSeqOP.getVlrInteger() );
			ps.setInt( iparam++, getSeqItOP() );
			
			ps.setInt( iparam++, lcCampos.getCodEmp() );
			ps.setInt( iparam++, lcCampos.getCodFilial() );
			ps.setInt( iparam++, codfase );
			
			ps.setInt( iparam++, lcCampos.getCodEmp() );
			ps.setInt( iparam++, lcCampos.getCodFilial() );
			ps.setInt( iparam++, codprod );
			ps.setString( iparam++, refprod );
			
			ps.setBigDecimal( iparam++, qtditop );
			ps.setString( iparam++, gerarma );
			
			ps.execute();
			
			con.commit();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void buscaEstrutura() {
		
		try {
		
			DLItensEstruturaProd dl = new DLItensEstruturaProd(txtQtdSugProdOP.getVlrBigDecimal());
			
			dl.setCodemp( Aplicativo.iCodEmp );
			dl.setCodemppd( Aplicativo.iCodEmp );
			
			dl.setCodfilial( ListaCampos.getMasterFilial( "EQPRODUTO" ) );			
			dl.setCodfilialpd( ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			
			dl.setCodprod( txtCodProdEst.getVlrInteger() );
			dl.setSeqest( txtSeqEst.getVlrInteger() );
			dl.carregaItens(false);
			
			dl.setVisible( true );
			
			
			if ( dl.OK ) {
				
				Vector<HashMap<String,Object>> valores = dl.getValores();
				HashMap<String,Object> item = new HashMap<String, Object>();
				
				Integer codprod = null;
				String refprod = null;
				BigDecimal qtditest = null;
				Integer codempfs = null;
				Integer codfilialfs = null;
				Integer codfase = null;
				String gerarma = null;
				
				for(int i=0; i< valores.size(); i++) {
					
					item = valores.elementAt( i );
					
					codprod = (Integer) item.get( DLItensEstruturaProd.ITENS.CODPRODPD.name() );
					refprod = (String) item.get( DLItensEstruturaProd.ITENS.REFPRODPD.name() );
					qtditest = (BigDecimal) item.get( DLItensEstruturaProd.ITENS.QTDITEST.name() );
					
					
					
					codfase = (Integer) item.get( DLItensEstruturaProd.ITENS.CODFASE.name() );
					
					gerarma = (String) item.get( "GERARMA" );
					
					insereItOpDinamica( codfase, codprod, refprod, qtditest, gerarma );
					
				
				}
				lcCampos.carregaDados();
			}
			dl.dispose();		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	private HashMap<String, BigDecimal> getValoresFSC() {
		String result = null;
		PreparedStatement ps;
		ResultSet rs;
		
		StringBuilder sql = null;
		HashMap<String, BigDecimal> valores = new HashMap<String, BigDecimal>();
		
		try {

			sql = new StringBuilder("");
			sql.append( "select pd.NroPlanos , pd.QtdPorPlano, pd.fatorfsc from eqproduto pd ");
			sql.append( "where pd.codemp=? and pd.codfilial=? ");
			
			if(txtCodProdEst.getVlrInteger()>0){
				sql.append( "and pd.codprod=? ");
			} else {
				sql.append( "and pd.refprod=? ");
			}
			
			
			ps = con.prepareStatement( sql.toString() );
			int param = 1;	
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			if(txtCodProdEst.getVlrInteger()>0){
				ps.setInt( param++, txtCodProdEst.getVlrInteger() );
			} else {
				ps.setString( param++, txtRefProdEst.getVlrString() );
			}
			rs = ps.executeQuery();
			
			if (rs.next()) {
				valores.put("NROPLANOS", rs.getBigDecimal( "NROPLANOS" ));
				valores.put("QTDPORPLANO", rs.getBigDecimal( "QTDPORPLANO" ));
				valores.put("FATORFSC", rs.getBigDecimal( "FATORFSC" ));
			}
			
			rs.close();
			ps.close();
			con.commit();
		} catch ( SQLException e ) {
			Funcoes.mensagemInforma( this, "Não foi possível carregar valores para conversão FSC !\n" + e.getMessage() );
			e.printStackTrace();
		}
		return valores;
	}
	
	private String getCodUnid() {
		String result = null;
		StringBuilder sql = new StringBuilder("select pd.codunid from eqproduto pd ");
		sql.append( "where pd.codemp=? and pd.codfilial=? ");
		if(txtCodProdEst.getVlrInteger()>0){
			sql.append( "and pd.codprod=? ");
		} else {
			sql.append( "and pd.refprod=? ");
		}
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement( sql.toString() );
			int param = 1;
			
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			if(txtCodProdEst.getVlrInteger()>0){
				ps.setInt( param++, txtCodProdEst.getVlrInteger() );
			} else {
				ps.setString( param++, txtRefProdEst.getVlrString() );
			}
			rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getString( "codunid" );
			}
			rs.close();
			ps.close();
			con.commit();
		} catch ( SQLException e ) {
			Funcoes.mensagemInforma( this, "Não foi possível carregar unidade !\n" + e.getMessage() );
			e.printStackTrace();
		}
		return result;
	}

	private BigDecimal getQtdSubProd() {
		BigDecimal result = BigDecimal.ZERO;
		StringBuilder sql = new StringBuilder("select sum(sp.qtditsp) qtditsp from ppopsubprod sp ");
		sql.append( "where sp.codemp=? and sp.codfilial=? and sp.codop=? and sp.seqop=?");
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PPOPSUBPROD" ) );
			ps.setInt( 3, txtCodOP.getVlrInteger() );
			ps.setInt( 4, txtSeqOP.getVlrInteger() );
			rs = ps.executeQuery();
			if (rs.next()) {
				result = getBigDecimal(rs.getBigDecimal( "qtditsp" ));
			}
			rs.close();
			ps.close();
			con.commit();
		} catch ( SQLException e ) {
			Funcoes.mensagemInforma( this, "Não foi possível carregar qtd. de sub-produtos !\n" + e.getMessage() );
			e.printStackTrace();
		}
		return result;
	}
	
	private BigDecimal getBigDecimal( BigDecimal value ) {
		BigDecimal result = null;
		
		if (value == null){
			result = BigDecimal.ZERO;
		} else {
			result = value;
		}
		return result;
	}
	
	private void geraRemessa() {
		
		try { 
		
			DLRemIndustria dl = new DLRemIndustria( con, txtCodOP.getVlrInteger(), txtSeqOP.getVlrInteger() );
//			dl.carregaCampos( sValores );
//			dl.carregaTabela( txtCodOP.getVlrInteger().intValue(), txtSeqOP.getVlrInteger().intValue() );
			dl.setVisible( true );
			if ( dl.OK ) {
	//			lcCampos.carregaDados();
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void geraRetorno() {
		
		try { 
		
			DLRetIndustria dl = new DLRetIndustria( con, txtCodOP.getVlrInteger(), txtSeqOP.getVlrInteger() );
//			dl.carregaCampos( sValores );
//			dl.carregaTabela( txtCodOP.getVlrInteger().intValue(), txtSeqOP.getVlrInteger().intValue() );
			dl.setVisible( true );
			if ( dl.OK ) {
	//			lcCampos.carregaDados();
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}


