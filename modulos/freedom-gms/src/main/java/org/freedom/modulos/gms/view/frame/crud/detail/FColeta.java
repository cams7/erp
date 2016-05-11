package org.freedom.modulos.gms.view.frame.crud.detail;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.JComboBoxEvent;
import org.freedom.acao.JComboBoxListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.object.EmailBean;
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
import org.freedom.library.swing.frame.FDetalhe;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.gms.DLBuscaSerie;
import org.freedom.modulos.gms.business.component.NumSerie;
import org.freedom.modulos.gms.business.object.Coleta.PREFS;
import org.freedom.modulos.gms.dao.DAOColeta;
import org.freedom.modulos.gms.dao.DAORecMerc;
import org.freedom.modulos.gms.view.dialog.utility.DLSerie;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.std.dao.DAOCliente;
import org.freedom.modulos.std.view.dialog.utility.DLBuscaProd;
import org.freedom.modulos.std.view.frame.crud.detail.FPlanoPag;
import org.freedom.modulos.std.view.frame.crud.plain.FSerie;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCliente;
import org.freedom.modulos.std.view.frame.crud.tabbed.FTransp;
import org.freedom.modulos.std.view.frame.crud.tabbed.FVendedor;

public class FColeta extends FDetalhe implements FocusListener, JComboBoxListener, CarregaListener, PostListener, InsertListener {

	// *** Constantes

	private static final long serialVersionUID = 1L;

	private final int casasDec = Aplicativo.casasDec;

	// *** Variaveis

	private Map<String, Object> preferecli = null;

	private boolean novo = true;

	private Vector<String> vValsFrete = new Vector<String>();

	private Vector<String> vLabsFrete = new Vector<String>();

	// *** Campos (Cabeçalho)

	private JTextFieldPad txtTicket = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtTicketCP = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
                                                              
	private JTextFieldPad txtCodTipoRecMercDet = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodTipoRecMerc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodTran = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeTran = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtCNPJTran = new JTextFieldFK( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtCNPJCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );
	
	private JTextFieldFK txtCodPais = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtSiglaUF = new JTextFieldFK( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtCodMunic = new JTextFieldPad( JTextFieldPad.TP_STRING, 7, 0 );

	private JTextFieldPad txtStatus = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtStatusItRecMerc = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JRadioGroup<String, String> rgFrete = null;

	private JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtDtEnt = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDtPrevRet = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDocRecMerc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodProcRecMerc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldFK txtDescProcRecMerc = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtEmailCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtTipoProcRecMerc = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtTipoFrete = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );
	
	private JTextFieldPad txtCodBarProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtCodFabProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtCodUnid = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtCodLote = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtNumSerie = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldFK txtObsSerie = new JTextFieldFK( JTextFieldPad.TP_STRING, 150, 0 );

	private JTextFieldPad txtCodAlmoxProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCLoteProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtSerieProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtQtdItColeta = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );
	
	private JTextFieldPad txtPlacaVeiculo = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );
	
	private JTextFieldPad txtNroFrota = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );
	
	private JTextFieldPad txtGaragem = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );
	
	
	//Campos da aba Compra
	
	private JTextFieldFK txtCodCompra = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtCodFor = new JTextFieldFK( JTextFieldFK.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtRazFor = new JTextFieldFK( JTextFieldFK.TP_STRING, 60, 0 );
	
	private JTextFieldFK txtSiglaUFFor = new JTextFieldFK( JTextFieldFK.TP_STRING, 2, 0 );
	
	private JTextFieldFK txtCodTipoMov = new JTextFieldFK( JTextFieldFK.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtDescTipoMov = new JTextFieldFK( JTextFieldFK.TP_STRING, 40, 0 );
	
	private JTextFieldFK txtSerieCompra = new JTextFieldFK( JTextFieldFK.TP_STRING, 4, 0 );
	
	private JTextFieldFK txtDocCompra = new JTextFieldFK(JTextFieldFK.TP_INTEGER, 9, 0 );
	
	private JTextFieldFK txtDtEmitCompra = new JTextFieldFK(  JTextFieldFK.TP_DATE, 10, 0 );
	
	private JTextFieldFK txtDtEntCompra = new JTextFieldFK(  JTextFieldFK.TP_DATE, 10, 0);
	
	private JTextFieldFK txtCodPlanoPag = new JTextFieldFK( JTextFieldFK.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtDescPlanoPag = new JTextFieldFK( JTextFieldFK.TP_STRING, 40, 0 );
	
	//Campos da aba Devolução.
	
	private JTextFieldPad txtTicketVD = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtCodVenda = new JTextFieldFK( JTextFieldFK.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtCodTipoMovVD = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtDescTipoMovVD = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );
	
	private JTextFieldFK txtCodSerie = new JTextFieldFK( JTextFieldPad.TP_STRING, 4, 0 );
	
	private JTextFieldFK txtDocVenda = new JTextFieldFK( JTextFieldFK.TP_INTEGER, 9, 0 );
	
	private JTextFieldFK txtDtEmitVenda = new JTextFieldFK(  JTextFieldFK.TP_DATE, 10, 0 );
	
	private JTextFieldFK txtDtSaidaVenda = new JTextFieldFK(  JTextFieldFK.TP_DATE, 10, 0);
	
	private JTextFieldFK txtCodCliVD = new JTextFieldFK( JTextFieldFK.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtRazCliVD = new JTextFieldFK( JTextFieldFK.TP_STRING, 60, 0 );
	
	private JTextFieldFK txtSiglaUFVD = new JTextFieldFK( JTextFieldFK.TP_STRING, 2, 0 );
	
	private JTextFieldFK txtCodPlanoPagVD = new JTextFieldFK( JTextFieldFK.TP_INTEGER, 8, 0 );
		
	private JTextFieldFK txtDescPlanoPagVD = new JTextFieldFK( JTextFieldFK.TP_STRING, 40, 0 );
	
	

	// *** Campos (Detalhe)

	private JTextFieldPad txtCodItRecMerc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtRefProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodFisc = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );
	
	private JTextFieldPad txtGarantia = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );
	
	private JTextFieldFK txtDtValidSerie = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );
	
	private JTextFieldFK txtDtFabricSerie = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );
	
	private JCheckBoxPad cbGarantia = new JCheckBoxPad( "Sim", "S", "N" );

	// *** Paineis

	private JPanelPad pinCab = new JPanelPad();
	
	private JPanelPad pinCabColeta = new JPanelPad();
	
	private JPanelPad pinCabCompra = new JPanelPad();
	
	private JPanelPad pinCabDevolucao = new JPanelPad();
	
	private JPanelPad pinDet = new JPanelPad();// JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinDetGrid = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 2 ) );
	
	
	private JTabbedPanePad tpnCab = new JTabbedPanePad();
	
	

	// *** Lista Campos

	private ListaCampos lcTran = new ListaCampos( this, "TN" );

	private ListaCampos lcCompra = new ListaCampos( this , "RM" );
	
	private ListaCampos lcVenda = new ListaCampos( this , "RM" );
	
	private ListaCampos lcCli = new ListaCampos( this, "CL" );
	
	private ListaCampos lcCliVD = new ListaCampos( this, "CL" );
	
	private ListaCampos lcProd = new ListaCampos( this, "PD" );

	private ListaCampos lcProd2 = new ListaCampos( this, "PD" );

	private ListaCampos lcVendedor = new ListaCampos( this, "VD" );

	private ListaCampos lcTipoRecMerc = new ListaCampos( this, "TR" );

	private ListaCampos lcProc = new ListaCampos( this, "TP" );

	private ListaCampos lcNumSerie = new ListaCampos( this, "NS" );
	
	private ListaCampos lcFor = new ListaCampos( this, "TF" );
	
	private ListaCampos lcTipoMov = new ListaCampos( this, "TM" );
	
	private ListaCampos lcTipoMovVD = new ListaCampos( this, "TM" );

	private ListaCampos lcPlanoPag = new ListaCampos( this, "PG" );
	
	private ListaCampos lcPlanoPagVD = new ListaCampos( this, "PG" );
	
	private ListaCampos lcSerie = new ListaCampos( this, "SE" );


	// *** Labels

	private JLabelPad lbStatus = new JLabelPad();

	private JLabelPad lbNumSerie = new JLabelPad();
	
	private JLabelPad lbDtFabricSerie = new JLabelPad();

	private JLabelPad lbDtValidSerie = new JLabelPad();
	
	private JLabelPad lbGarantia = new JLabelPad();
	
	// *** Botoes
	private JPanelPad pnBtGerar = new JPanelPad(JPanelPad.TP_JPANEL, new GridBagLayout());
	
	private JButtonPad  btGerar = new JButtonPad( Icone.novo( "btGerar.png" ) );
	
	private Integer codplanopag = null;
	
	// Aba de compra.
	
	private JTablePad tabCompra = new JTablePad();
	
	private JScrollPane spCompra = new JScrollPane( tabCompra );
	
	// *** DAO
	
	private DAOColeta daocoleta = null;

	private DAORecMerc daorecmerc = null;
	
	private DAOCliente daocli = null;

	public FColeta() {

		// super();

		setTitulo( "Coleta de materiais" );
		setAtribos( 50, 50, 772, 480 );

		configuraCampos();
		montaListaCampos();

	}

	private void configuraCampos() {

		txtCodMunic.setAtivo( false );

		vValsFrete.addElement( "C" );
		vValsFrete.addElement( "F" );
		vLabsFrete.addElement( "CIF" );
		vLabsFrete.addElement( "FOB" );

		rgFrete = new JRadioGroup<String, String>( 1, 2, vLabsFrete, vValsFrete, -4 );

		DAORecMerc.atualizaStatus( (String) DAORecMerc.STATUS_NAO_SALVO.getValue(), lbStatus );

		lbStatus.setText( "NÃO SALVO" );
		lbStatus.setVisible( true );

	}

	private void montaTela() {

		nav.setNavigation( true );

		pnMaster.remove( spTab );
		pnMaster.add( pinDetGrid, BorderLayout.CENTER );

		pinDetGrid.add( spTab );
		// pinDetGrid.add( new JScrollPane( tabPesagem ) );
		

		montaCabecalho();
		montaDetalhe();
		montaTab();
		ajustaTabela();
		adicListeners();
		setImprimir( true );

	}

	private void montaCabecalho() {

		setAltCab( 153 );

		setListaCampos( lcCampos );
	
		pnCliCab.add( tpnCab );
		tpnCab.addTab( "Coleta", pinCabColeta );
		tpnCab.addTab( "Compra", pinCabCompra );
		tpnCab.addTab( "Devolução", pinCabDevolucao );
		setPainel( pinCabColeta);	

		adicCampo( txtTicket, 7, 20, 70, 20, "Ticket", "Ticket", ListaCampos.DB_PK, true );

		adicCampoInvisivel( txtCodTipoRecMerc, "CodTipoRecMerc", "Cód.T.", ListaCampos.DB_FK, true );

		adicCampo( txtCodCli, 80, 20, 67, 20, "CodCli", "Cód.Cli.", ListaCampos.DB_FK, txtRazCli, true );
		adicDescFK( txtRazCli, 150, 20, 197, 20, "RazCli", "Razão social do cliente" );

		adicCampo( txtCodVend, 350, 20, 67, 20, "CodVend", "Cód.comis.", ListaCampos.DB_FK, txtNomeVend, true );
		adicDescFK( txtNomeVend, 420, 20, 197, 20, "NomeVend", "Nome do comissionado" );

		adicCampo( txtDtEnt, 7, 60, 70, 20, "DtEnt", "Dt.Entrada", ListaCampos.DB_SI, true );
		adicCampo( txtDtPrevRet, 80, 60, 70, 20, "DtPrevRet", "Dt.Prevista", ListaCampos.DB_SI, true );
		adicCampo( txtDocRecMerc, 153, 60, 70, 20, "DocRecMerc", "Documento", ListaCampos.DB_SI, true );
		
		adicCampo( txtCodTran,  233, 60, 70, 20, "CodTran", "Cód.Tran", ListaCampos.DB_FK, txtNomeTran,  true );
		adicDescFK( txtNomeTran,  306, 60, 310, 20, "NomeTran", "Cód.Tran" );

		adicCampoInvisivel( txtStatus, "Status", "Status", ListaCampos.DB_SI, false );

		adicCampoInvisivel( txtTipoFrete, "TipoFrete", "Tp.frete", ListaCampos.DB_SI, true );

		adic( lbStatus, 620, 20, 123, 60 );
		
		setListaCampos( lcCampos );
		setPainel( pinCabCompra );
		
		adicDescFK( txtCodCompra , 7, 20, 80, 20, "CodCompra", "Cód.compra" );
		adicDescFK( txtCodTipoMov, 90, 20, 77, 20, "CodTipoMov", "Cód.tp.mov." );
		adicDescFK( txtDescTipoMov, 170, 20, 247, 20, "DescTipoMov", "Descrição do tipo de movimento" );
		adicDescFK( txtSerieCompra, 420, 20, 77, 20, "Serie", "Série" );
		adicDescFK( txtDocCompra, 500, 20, 77, 20, "DocCompra", "Documento" );
		adicDescFK( txtDtEmitCompra, 580, 20, 75, 20, "DtEmitCompra", "Dt.emissão" );
		adicDescFK( txtDtEntCompra, 658, 20, 75, 20, "DtEntCompra", "Dt.entrada" );
		adicDescFK( txtCodFor , 7, 60, 80, 20, "CodFor", "Cód.for." );
		adicDescFK( txtRazFor , 90, 60, 264, 20, "RazFor", "Razão social do fornecedor" );
		adicDescFK( txtSiglaUFFor, 357, 60, 20, 20, "UfFor", "UF" );
		adicDescFK( txtCodPlanoPag, 380, 60, 60, 20, "CodPlanoPag", "Cód.p.pag." );
		adicDescFK( txtDescPlanoPag, 443, 60, 195, 20, "DescPlanoPag", "Descrição do plano de pag." );
		
		setListaCampos( lcCampos );
		setPainel( pinCabDevolucao );
		
		adicDescFK( txtCodVenda , 7, 20, 80, 20, "CodVenda", "Cód.venda" );
		adicDescFK( txtCodTipoMovVD, 90, 20, 77, 20, "CodTipoMov", "Cód.tp.mov." );
		adicDescFK( txtDescTipoMovVD, 170, 20, 247, 20, "DescTipoMov", "Descrição do tipo de movimento" );
		adicDescFK( txtCodSerie, 420, 20, 77, 20, "Serie", "Série" );
		adicDescFK( txtDocVenda, 500, 20, 77, 20, "DocVenda", "Documento" );
		adicDescFK( txtDtEmitVenda, 580, 20, 75, 20, "DtEmitVenda", "Dt.emissão" );
		adicDescFK( txtDtSaidaVenda, 658, 20, 75, 20, "DtSaidaVenda", "Dt.Saida" );
		adicDescFK( txtCodCliVD , 7, 60, 80, 20, "CodCli", "Cód.cli." );
		adicDescFK( txtRazCliVD , 90, 60, 264, 20, "RazCli", "Razão social do cliente" );
		adicDescFK( txtSiglaUFVD, 357, 60, 20, 20, "UfCli", "UF" );
		adicDescFK( txtCodPlanoPagVD, 380, 60, 60, 20, "CodPlanoPag", "Cód.p.pag." );
		adicDescFK( txtDescPlanoPagVD, 443, 60, 195, 20, "DescPlanoPag", "Descrição do plano de pag." );
		
		setListaCampos( true, "RECMERC", "EQ" );
		lcCampos.setQueryInsert( true );
	
	}

	private void montaDetalhe() {

		setAltDet( 100 );

		setPainel( pinDet, pnDet );
		setListaCampos( lcDet );
		setNavegador( navRod );
		navRod.setPreferredSize( new Dimension ( 300, 30 ) );
		btGerar.setPreferredSize( new Dimension ( 26, 26 ) );
		pnBtGerar.add(btGerar);
		navRod.add( pnBtGerar );
		btGerar.setToolTipText( "Gerar compra" );

		adicCampo( txtCodItRecMerc, 7, 20, 42, 20, "CodItRecMerc", "Seq.", ListaCampos.DB_PK, true );

		if ( comRef() ) {
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
			adicCampoInvisivel( txtRefProd, "RefProd", "Referência", ListaCampos.DB_FK, false );
		}

		adicDescFK( txtDescProd, 128, 20, 300, 20, "DescProd", "Descrição do Produto" );
		adicCampo( txtQtdItColeta, 431, 20, 70, 20, "QtdItRecMerc", "Qtd.", ListaCampos.DB_SI, true );
		
		adicCampo( txtPlacaVeiculo, 504, 20, 80, 20, "PlacaVeiculo", "Placa veiculo.", ListaCampos.DB_SI, false );
		adicCampo( txtNroFrota, 587, 20, 80, 20, "NroFrota", "Nro.frota.", ListaCampos.DB_SI, false );
		adicCampo( txtGaragem, 669, 20, 80, 20, "Garagem", "Garagem.", ListaCampos.DB_SI, false );
		
		
		
		txtQtdItColeta.setBuscaAdic( new DLBuscaSerie( lcDet, lcNumSerie, lcProd, con, "qtditrecmerc", true ) );

		lbNumSerie = adicCampo( txtNumSerie, 7, 60, 119, 20, "NumSerie", "Número de série", ListaCampos.DB_FK, txtObsSerie, false );
		lbDtFabricSerie = adicDescFK( txtDtFabricSerie, 129, 60, 80, 20, "DtFabricSerie", "Fabricação" );
		lbDtValidSerie = adicDescFK( txtDtValidSerie, 212, 60, 80, 20, "DtFabricSerie", " Validade" );
		lbGarantia = adicDB( cbGarantia, 306, 60, 80, 20, "garantia", "Garantia", false );

		lbNumSerie.setVisible( false );
		lbDtFabricSerie.setVisible( false );
		lbDtValidSerie.setVisible( false );

		txtNumSerie.setVisible( false );
		txtDtFabricSerie.setVisible( false );
		txtDtValidSerie.setVisible( false );
		lbGarantia.setVisible( false );

		adicCampoInvisivel( txtCodProcRecMerc, "CodProcRecMerc", "Cod.Proc.", ListaCampos.DB_FK, txtDescProcRecMerc, true );
		adicDescFKInvisivel( txtDescProcRecMerc, "DescProcRecMerc", "Descrição do processo" );
		adicCampoInvisivel( txtCodTipoRecMercDet, "CodTipoRecMerc", "Cod.Tp.Rec.Merc", ListaCampos.DB_SI, true );

		txtStatusItRecMerc.setSoLeitura( true );
		adicCampoInvisivel( txtStatusItRecMerc, "StatusItRecMerc", "Status", ListaCampos.DB_SI, false );
		
		

		setListaCampos( true, "ITRECMERC", "EQ" );
		lcDet.setQueryInsert( true );

		pinDetGrid.setBackground( Color.RED );

	}

	private void carregaTipoRec() {

		txtCodTipoRecMerc.setVlrInteger( (Integer) daocoleta.getPrefs()[ PREFS.CODTIPORECMERC.ordinal() ] );
		lcTipoRecMerc.carregaDados();

	}

	private void ajustaTabela() {

		tab.setRowHeight( 21 );

		tab.setTamColuna( 30, 0 );
		tab.setTamColuna( 60, 1 );
		tab.setTamColuna( 350, 2 );
		tab.setTamColuna( 70, 3 );

		tab.setColunaInvisivel( 5 );
		tab.setColunaInvisivel( 6 );
		tab.setColunaInvisivel( 7 );
		tab.setColunaInvisivel( 8 );
	}

	private void adicListeners() {

		lcCampos.addInsertListener( this );
		lcCampos.addCarregaListener( this );
		lcCampos.addPostListener( this );
		lcDet.addPostListener( this );
		lcCli.addCarregaListener( this );
		lcDet.addCarregaListener( this );

		lcProd.addCarregaListener( this );
		lcProd2.addCarregaListener( this );

		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		btGerar.addActionListener( this );
		txtNumSerie.addFocusListener( this );
		txtQtdItColeta.addFocusListener( this );
		txtCodTran.addKeyListener( this );
		txtQtdItColeta.addKeyListener( this );
		txtNumSerie.addKeyListener( this );
	}

	private void montaListaCampos() {

		// * Tipo de Recebimento Cabeçalho

		lcTipoRecMerc.add( new GuardaCampo( txtCodTipoRecMerc, "CodTipoRecMerc", "Cód.Tipo.Rec.", ListaCampos.DB_PK, false ) );
		txtCodTipoRecMerc.setTabelaExterna( lcTipoRecMerc, FTipoRecMerc.class.getCanonicalName() );
		txtCodTipoRecMerc.setNomeCampo( "CodTipoRecMerc" );
		txtCodTipoRecMerc.setFK( true );
		lcTipoRecMerc.setReadOnly( true );
		lcTipoRecMerc.montaSql( false, "TIPORECMERC", "EQ" );

		// * Cliente

		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.Cli.", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtCNPJCli, "CnpjCli", "CNPJ", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtCodPais, "CodPais", "Cód.País", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtSiglaUF, "SiglaUF", "UF", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtCodMunic, "CodMunic", "Cód.Mun.", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.comis.", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtEmailCli, "EmailCli", "Email do cliente.", ListaCampos.DB_SI, false ) );
		txtCodCli.setTabelaExterna( lcCli, FCliente.class.getCanonicalName() );
		txtCodCli.setNomeCampo( "CodCli" );
		txtCodCli.setFK( true );
		lcCli.setReadOnly( true );
		lcCli.montaSql( false, "CLIENTE", "VD" );
		
		lcCliVD.add( new GuardaCampo( txtCodCliVD, "CodCli", "Cód.Cli.", ListaCampos.DB_PK, false ) );
		lcCliVD.add( new GuardaCampo( txtRazCliVD, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCliVD.add( new GuardaCampo( txtSiglaUFVD, "SiglaUF", "UF", ListaCampos.DB_SI, false ) ); 
		txtCodCliVD.setTabelaExterna( lcCliVD, FCliente.class.getCanonicalName() );
		txtCodCliVD.setNomeCampo( "CodCli" );
		txtCodCliVD.setFK( true );
		lcCliVD.setReadOnly( true );
		lcCliVD.montaSql( false, "CLIENTE", "VD" );

		// * Transportadora

		lcTran.add( new GuardaCampo( txtCodTran, "CodTran", "Cód.For.", ListaCampos.DB_PK, false ) );
		lcTran.add( new GuardaCampo( txtNomeTran, "NomeTran", "Nome da transportadora", ListaCampos.DB_SI, false ) );
		lcTran.add( new GuardaCampo( txtCNPJTran, "CnpjTran", "CNPJ", ListaCampos.DB_SI, false ) );
		txtCodTran.setTabelaExterna( lcTran, FTransp.class.getCanonicalName() );
		txtCodTran.setNomeCampo( "CodTran" );
		txtCodTran.setFK( true );
		lcTran.setReadOnly( true );
		lcTran.montaSql( false, "TRANSP", "VD" );

		// * Produto (Detalhe)

		lcProd.add( new GuardaCampo( txtCodProd, "codprod", "Cód.prod.", ListaCampos.DB_PK, true ) );
		lcProd.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtRefProd, "RefProd", "Referência", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtCodFabProd, "CodFabProd", "Cod.Fabricante", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtCodBarProd, "CodBarProd", "Cod.Barra", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtCodFisc, "CodFisc", "Cod.Fiscal", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtCodUnid, "CodUnid", "Unidade", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtCodAlmoxProd, "CodAlmox", "Unidade", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtCLoteProd, "CLoteProd", "C/Lote", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtSerieProd, "SerieProd", "C/Série", ListaCampos.DB_SI, false ) );
		txtCodUnid.setAtivo( false );
		lcProd.setWhereAdic( "ATIVOPROD='S'" );
		lcProd.montaSql( false, "PRODUTO", "EQ" );
		lcProd.setQueryCommit( false );
		lcProd.setReadOnly( true );
		txtCodProd.setTabelaExterna( lcProd, FProduto.class.getCanonicalName() );

		// * Produto referencia

		lcProd2.add( new GuardaCampo( txtRefProd, "RefProd", "Referência", ListaCampos.DB_PK, true ) );
		lcProd2.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtCodProd, "codprod", "Cód.prod.", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtCodFabProd, "CodFabProd", "Cod.Fabricante", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtCodBarProd, "CodBarProd", "Cod.Barras", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtCodUnid, "CodUnid", "Unidade", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtCodAlmoxProd, "CodAlmox", "Unidade", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtCLoteProd, "CLoteProd", "C/Lote", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtSerieProd, "SerieProd", "C/Série", ListaCampos.DB_SI, false ) );
		txtRefProd.setNomeCampo( "RefProd" );
		txtRefProd.setListaCampos( lcDet );
		lcProd2.setWhereAdic( "ATIVOPROD='S'" );
		lcProd2.montaSql( false, "PRODUTO", "EQ" );
		lcProd2.setQueryCommit( false );
		lcProd2.setReadOnly( true );
		txtRefProd.setTabelaExterna( lcProd2, FProduto.class.getCanonicalName() );

		// FK do número de série
		lcNumSerie.add( new GuardaCampo( txtNumSerie, "NumSerie", "Num.Série", ListaCampos.DB_PK, false ) );
		lcNumSerie.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcNumSerie.add( new GuardaCampo( txtObsSerie, "ObsSerie", "Observações", ListaCampos.DB_SI, false ) );
		lcNumSerie.add( new GuardaCampo( txtDtFabricSerie, "dtfabricserie", "Fabricação", ListaCampos.DB_SI, false ) );
		lcNumSerie.add( new GuardaCampo( txtDtValidSerie, "dtvalidserie", "Validade", ListaCampos.DB_SI, false ) );
		lcNumSerie.setDinWhereAdic( "CODPROD=#N", txtCodProd );
		lcNumSerie.setAutoLimpaPK( false );
		lcNumSerie.montaSql( false, "SERIE", "EQ" );
		lcNumSerie.setQueryCommit( false );
		lcNumSerie.setReadOnly( true );
		txtNumSerie.setTabelaExterna( lcNumSerie, FSerie.class.getCanonicalName() );
		txtObsSerie.setListaCampos( lcNumSerie );
		txtObsSerie.setNomeCampo( "ObsSerie" );
		txtObsSerie.setLabel( "Observações" );

		// FK Vendedor
		lcVendedor.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.Venda", ListaCampos.DB_PK, false ) );
		lcVendedor.add( new GuardaCampo( txtNomeVend, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, false ) );
		lcVendedor.setWhereAdic( "ATIVOCOMIS='S'" );
		lcVendedor.montaSql( false, "VENDEDOR", "VD" );
		lcVendedor.setQueryCommit( false );
		lcVendedor.setReadOnly( true );
		txtCodVend.setTabelaExterna( lcVendedor, FVendedor.class.getCanonicalName() );

		/***************
		 * PROCESSOS *
		 ***************/

		lcProc.add( new GuardaCampo( txtCodProcRecMerc, "CodProcRecMerc", "Cód.Proc.", ListaCampos.DB_PK, false ) );
		lcProc.add( new GuardaCampo( txtDescProcRecMerc, "DescProcRecMerc", "Descrição do processo", ListaCampos.DB_SI, false ) );
		lcProc.add( new GuardaCampo( txtCodTipoRecMercDet, "CodTipoRecMerc", "Cod.Tp.Rec.Merc.", ListaCampos.DB_SI, false ) );
		lcProc.add( new GuardaCampo( txtTipoProcRecMerc, "TipoProcRecMerc", "Tp.Proc.Rec.Merc.", ListaCampos.DB_SI, false ) );
		txtCodProcRecMerc.setTabelaExterna( lcProc, null );
		txtCodProcRecMerc.setNomeCampo( "CodProcRecMerc" );
		txtCodProcRecMerc.setFK( true );
		lcProc.setReadOnly( true );
		lcProc.montaSql( false, "PROCRECMERC", "EQ" );
		
		// FK Compra
		lcCompra.setMaster( lcCampos );
		lcCompra.add( new GuardaCampo( txtTicketCP, "Ticket", "Ticket", ListaCampos.DB_PK, false ) );
		lcCompra.add( new GuardaCampo( txtCodCompra, "CodCompra", "Cód.Compra", ListaCampos.DB_SI, false ) );
		lcCompra.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.Fornecedor", ListaCampos.DB_FK, txtRazFor, false ) );
		lcCompra.add( new GuardaCampo( txtCodTipoMov, "CodTipoMov", "Cód.Tp.Mov", ListaCampos.DB_FK, txtDescTipoMov, false ) );
		lcCompra.add( new GuardaCampo( txtSerieCompra, "Serie", "Serie", ListaCampos.DB_SI, false ) );
		lcCompra.add( new GuardaCampo( txtDocCompra, "DocCompra", "Doc.Compra", ListaCampos.DB_SI, false ) );
		lcCompra.add( new GuardaCampo( txtDtEmitCompra, "DtEmitCompra", "Data de Emissão da compra", ListaCampos.DB_SI, false ) );
		lcCompra.add( new GuardaCampo( txtDtEntCompra, "DtEntCompra", "Data de entrada da compra", ListaCampos.DB_SI, false ) );
		lcCompra.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.Plan.Pag.", ListaCampos.DB_FK, txtDescPlanoPag, false ) );
		lcCompra.montaSql( false, "COMPRA", "CP" );
		lcCompra.setQueryCommit( false );
		lcCompra.setReadOnly( true );
			
		// FK Fornecedor
		lcFor.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.Fornecedor", ListaCampos.DB_PK, false ) );
		lcFor.add( new GuardaCampo( txtRazFor, "RazFor", "Razão do Fornecedor", ListaCampos.DB_SI, false ) );
		lcFor.add( new GuardaCampo( txtSiglaUFFor, "UfFor", "UF", ListaCampos.DB_SI, false ) );
		lcFor.montaSql( false, "FORNECED", "CP" );
		lcFor.setQueryCommit( false );
		lcFor.setReadOnly( true );
		txtCodFor.setTabelaExterna( lcFor, null );
		
		// FK Tipo de movimento
		lcTipoMov.add( new GuardaCampo( txtCodTipoMov, "CodTipoMov", "Cód.Tp.Mov", ListaCampos.DB_PK, false ) );
		lcTipoMov.add( new GuardaCampo( txtDescTipoMov, "DescTipoMov", "Descrição do tipo de documento", ListaCampos.DB_SI, false ) );
		lcTipoMov.montaSql( false, "TIPOMOV", "EQ" );
		lcTipoMov.setQueryCommit( false );
		lcTipoMov.setReadOnly( true );
		txtCodTipoMov.setTabelaExterna( lcTipoMov, null );
		
		// FK Tipo de movimento para venda.
		lcTipoMovVD.add( new GuardaCampo( txtCodTipoMovVD, "CodTipoMov", "Cód.Tp.Mov", ListaCampos.DB_PK, false ) );
		lcTipoMovVD.add( new GuardaCampo( txtDescTipoMovVD, "DescTipoMov", "Descrição do tipo de documento", ListaCampos.DB_SI, false ) );
		lcTipoMovVD.montaSql( false, "TIPOMOV", "EQ" );
		lcTipoMovVD.setQueryCommit( false );
		lcTipoMovVD.setReadOnly( true );
		txtCodTipoMovVD.setTabelaExterna( lcTipoMovVD, null );
		
		//FK Plano de pagamento.
		lcPlanoPag.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_PK, false ) );
		lcPlanoPag.add( new GuardaCampo( txtDescPlanoPag, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPag.setWhereAdic( "ATIVOPLANOPAG='S' AND CVPLANOPAG IN ('C','A')" );
		lcPlanoPag.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag.setQueryCommit( false );
		lcPlanoPag.setReadOnly( true );
		txtCodPlanoPag.setTabelaExterna( lcPlanoPag, FPlanoPag.class.getCanonicalName() );
		
		lcPlanoPagVD.add( new GuardaCampo( txtCodPlanoPagVD, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_PK, false ) );
		lcPlanoPagVD.add( new GuardaCampo( txtDescPlanoPagVD, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPagVD.setWhereAdic( "ATIVOPLANOPAG='S' AND CVPLANOPAG IN ('C','A')" );
		lcPlanoPagVD.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPagVD.setQueryCommit( false );
		lcPlanoPagVD.setReadOnly( true );
		txtCodPlanoPagVD.setTabelaExterna( lcPlanoPagVD, FPlanoPag.class.getCanonicalName() );

		
		//FK Venda
		lcVenda.setMaster( lcCampos );
		lcVenda.add( new GuardaCampo( txtTicketVD, "Ticket", "Ticket", ListaCampos.DB_PK, false ) );
		lcVenda.add( new GuardaCampo( txtCodVenda, "CodVenda", "Cód.Venda", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtCodCliVD, "CodCli", "Cód.Cli.", ListaCampos.DB_FK, txtRazCliVD, false ) );
		lcVenda.add( new GuardaCampo( txtCodTipoMovVD, "CodTipoMov", "Cód.Tp.Mov", ListaCampos.DB_FK, txtDescTipoMovVD, false ) );
		lcVenda.add( new GuardaCampo( txtCodSerie, "Serie", "Serie", ListaCampos.DB_FK, false ) );
		lcVenda.add( new GuardaCampo( txtDocVenda, "DocVenda", "DocVenda", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtDtEmitVenda, "DtEmitVenda", "Data de Emissão da venda", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtDtSaidaVenda, "DtSaidaVenda", "Data de entrada da venda", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtCodPlanoPagVD, "CodPlanoPag", "Cód.Plan.Pag.", ListaCampos.DB_FK, txtDescPlanoPagVD, false ) );
		lcVenda.montaSql( false, "VENDA", "VD" );
		lcVenda.setQueryCommit( false );
		lcVenda.setReadOnly( true );
		
		// FK Série
		lcSerie.add( new GuardaCampo( txtCodSerie, "Serie", "Série", ListaCampos.DB_PK, false ) );
		lcSerie.montaSql( false, "SERIE", "LF" );
		lcSerie.setQueryCommit( false );
		lcSerie.setReadOnly( true );
		txtCodSerie.setTabelaExterna( lcSerie, FSerie.class.getCanonicalName() );
		
	}

	
	public Integer getCodplanopag() {
	
		return codplanopag;
	}

	
	public void setCodplanopag( Integer codplanopag ) {
	
		this.codplanopag = codplanopag;
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		} else if ( evt.getSource() == btImp ) {
			imprimir( TYPE_PRINT.PRINT);
		} else if ( evt.getSource() == btGerar ) {
			gerarCompra();
		}

		super.actionPerformed( evt );

	}

	private void gerarCompra() {
		Integer codfor = null;
		Integer codcompra = null; 
		Integer codtran = null;
		Integer codtipofor = null;
		if (preferecli!=null) {
			codtipofor = (Integer) preferecli.get( "CODTIPOFOR" );
		}
		try {
			codcompra = daocoleta.loadCompra(Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "EQRECMERC" ), txtTicket.getVlrInteger() );
			if ( (codcompra==null) || (codcompra.intValue()==0) ) {
				codfor = daocoleta.loadCodfor(Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "VDCLIENTE" ), txtCodCli.getVlrInteger());
				if ( (codfor==null) || (codfor.intValue()==0) ) {
					if ( Funcoes.mensagemConfirma( this, "Cliente não possui fornecedor vinculado para fins de NF de entrada !\nVincular automaticamente ? " ) == JOptionPane.YES_OPTION ) {
						if (codtipofor==null) {
							Funcoes.mensagemInforma( this, "Configure o tipo de fornecedor no preferências gerais !" );
							return;
						}
						codfor = daocli.pesquisaFor( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "VDCLIENTE" ), txtCodCli.getVlrInteger() );
						// Se não achar um fornecedor com mesmo CPF ou mesmo CNPJ, retorna 0 e insere nas tabelas CPFORNECED e EQCLIFOR
						if (codfor==0) {
							codfor = daocli.insereFor( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "CPFORNECED"), Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "VDCLIENTE" ),
									txtCodCli.getVlrInteger(), Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "CPTIPOFOR" ), codtipofor );
						}
						if (codfor!=0) {
							daocli.insereCliFor( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "VDCLIENTE"  ), txtCodCli.getVlrInteger(), Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "CPFORNECED" ), codfor );
						}
						daocli.getConn().commit();
						
					} else {
						return;
					}
				}
				if ( (getCodplanopag()==null) || (getCodplanopag().intValue()==0) ) {
					Funcoes.mensagemInforma( this, "Não existe plano de pagamento cadastrado no preferências GMS para criação da NF !" );
					return;
				}
				if ( (getCodplanopag()==null) || (getCodplanopag().intValue()==0) ) {
					Funcoes.mensagemInforma( this, "Não existe plano de pagamento cadastrado no preferências GMS para criação da NF !" );
					return;
				}
				codtran = txtCodTran.getVlrInteger();
				if ( (codtran==null) || (codtran.intValue()==0) ) {
					codtran = (Integer) daocoleta.getPrefs()[PREFS.CODTRAN.ordinal()];
					if ( (codtran==null) || (codtran.intValue()==0) ) {
						Funcoes.mensagemInforma( this, "Esta coleta não possui transportador vinculado!\nCadastre um transportador padrão em Preferências GMS !" );
					} else {
						if ( Funcoes.mensagemConfirma( this, 
								"Esta coleta não possui transportador vinculado!\nGostaria de utilizar o transportador padrão ? " ) == JOptionPane.YES_OPTION ) {
							lcCampos.edit();
							txtCodTran.setVlrInteger( codtran );
							lcTran.carregaDados();
						} else {
							return;
						}
					}
				}
				if  ( (lcCampos.getStatus()==ListaCampos.LCS_EDIT) || (lcCampos.getStatus()==ListaCampos.LCS_INSERT) ) {
					lcCampos.post();
				}
				daorecmerc.setCodplanopag( getCodplanopag() );
				daorecmerc.setCodcli( txtCodCli.getVlrInteger() );
				//daorecmerc.setCodtipomov( getCodtipomovcn() );
				daorecmerc.setTicket( txtTicket.getVlrInteger() );
				daorecmerc.CarregaRecMerc();
				daorecmerc.setCodfor( codfor );
				if(Funcoes.mensagemConfirma( this, "Confirmar geração da compra ?" ) == JOptionPane.YES_OPTION	){
					codcompra = daorecmerc.geraCompra(true);
					if (Funcoes.mensagemConfirma( this, "Gerada a compra número " + codcompra + ", deseja edita-la ?" )==JOptionPane.YES_OPTION) {
						editaCompra(codcompra);
					}
				}
			} else {
				if (Funcoes.mensagemConfirma( this, "Compra já foi gerada anteriormente sob o número " + codcompra + ", deseja edita-la ?" )==JOptionPane.YES_OPTION) {
					editaCompra(codcompra);
				}
			}
			
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException err) {
				Funcoes.mensagemErro( this, "Erro executando rollback!\n"+err.getMessage() );
			}
			Funcoes.mensagemErro( this, "Erro carregando fornecedor!\n" + e.getMessage() );
			e.printStackTrace();
		}
		
	}
	
	private void editaCompra(Integer codcompra) {
		FCompra compra = null;				
		if (Aplicativo.telaPrincipal.temTela( "Compra" )) {
			compra = (FCompra) Aplicativo.telaPrincipal.getTela( "Compra" );
		} else {
			compra = new FCompra();
			Aplicativo.telaPrincipal.criatela( "Compra", compra, con );
		}
		if (compra!=null) {
			compra.carregaCompra(codcompra);
		}
	}
	
	private void imprimiGrafico( final ResultSet rs, final TYPE_PRINT bVisualizar ) {

		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "CPCOMPRA" ) );
		hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
		hParam.put( "TICKET", txtTicket.getVlrInteger() );

		EmailBean email = Aplicativo.getEmailBean();
		email.setPara( txtEmailCli.getVlrString() );

		dlGr = new FPrinterJob( "layout/col/COL_PD.jasper", "Coleta", "", rs, hParam, this, email );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório coleta!" + err.getMessage(), true, con, err );
			}
		}
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();

		sql.append( "select " );
		sql.append( "se.codsecao, se.descsecao, rm.dtent, rm.hins, rm.dtprevret, it.qtditrecmerc, pd.codprod, pd.refprod, " );
		sql.append( "pd.descprod, rm.ticket, cl.codcli, cl.razcli, rm.docrecmerc, vd.nomevend, mn.nomemunic " );
		sql.append( "from " );
		sql.append( "eqrecmerc rm " );
		sql.append( "left outer join vdcliente cl on " );
		sql.append( "cl.codemp=rm.codempcl and cl.codfilial=rm.codfilialcl and cl.codcli=rm.codcli " );
		sql.append( "left outer join sgmunicipio mn on " );
		sql.append( "mn.codpais=cl.codpais and mn.siglauf=cl.siglauf and mn.codmunic=cl.codmunic " );
		sql.append( "left outer join vdvendedor vd on " );
		sql.append( "vd.codemp=rm.codempvd and vd.codfilial=rm.codfilialvd and vd.codvend=rm.codvend " );
		sql.append( "left outer join eqitrecmerc it on " );
		sql.append( "it.codemp=rm.codemp and it.codfilial=rm.codfilial and it.ticket=rm.ticket " );
		sql.append( "left outer join eqproduto pd on " );
		sql.append( "pd.codemp=it.codemppd and pd.codfilial=it.codfilialpd and pd.codprod=it.codprod " );
		sql.append( "left outer join eqsecao se on " );
		sql.append( "se.codemp=pd.codempsc and se.codfilial=pd.codfilialsc and se.codsecao=pd.codsecao " );
		sql.append( "where " );
		sql.append( "rm.codemp=? and rm.codfilial=? and rm.ticket=? " );
		sql.append( "order by it.coditrecmerc " );

		try {

			System.out.println( "SQL:" + sql.toString() );

			ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQCOLETA" ) );
			ps.setInt( 3, txtTicket.getVlrInteger() );

			System.out.println( sql.toString() );

			rs = ps.executeQuery();

		} catch ( SQLException err ) {

			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar dados da coleta" );

		}

		imprimiGrafico( rs, bVisualizar );

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcProd.setConexao( cn );
		lcProd2.setConexao( cn );
		lcNumSerie.setConexao( cn );
		lcTran.setConexao( cn );
		lcCli.setConexao( cn );
		lcVendedor.setConexao( cn );
		lcTipoRecMerc.setConexao( cn );
		lcProc.setConexao( cn );
		lcFor.setConexao( cn );
		lcTipoMov.setConexao( cn );
		lcPlanoPag.setConexao( cn );
		lcSerie.setConexao( cn );
		lcVenda.setConexao( cn );
		lcTipoMovVD.setConexao( cn );
		lcCliVD.setConexao( cn );
		lcPlanoPagVD.setConexao( cn );
		lcCliVD.setConexao( cn );
		lcCompra.setConexao( cn );
		lcVenda.setConexao( cn );
	
		daocoleta = new DAOColeta( cn );
		daorecmerc = new DAORecMerc( this, null, con );

		try{
			daocli = new DAOCliente( cn );
			preferecli = daocli.getPrefere(Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "SGUSUARIO" ), Aplicativo.getUsuario().getIdusu().toLowerCase(), 
					Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "SGPREFERE1" ));
			daocoleta.setPrefs( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			setCodplanopag( (Integer) daocoleta.getPrefs()[PREFS.CODPLANOPAG.ordinal()] );
		} catch (SQLException e) {
			Funcoes.mensagemErro( this, "Erro carregando preferências !\b" + e.getMessage() );
		}

		montaTela();

		lcCampos.insert( true );
	

	}

	private boolean comRef() {

		return ( (Boolean) daocoleta.getPrefs()[ PREFS.USAREFPROD.ordinal() ] );
	}

	public void focusGained( FocusEvent e ) {

		// TODO Auto-generated method stub

	}

	public void focusLost( FocusEvent e ) {

		if ( ( e.getSource() == txtQtdItColeta ) ) {
			// habilitaSerie();
		}

	}

	public void valorAlterado( JComboBoxEvent evt ) {

	}
	
	private void verificaGarantia() {

		cbGarantia.setVlrString( NumSerie.isGarantia( txtDtEnt.getVlrDate(), txtDtValidSerie.getVlrDate() ) ? "S" : "N" );
	}

	public void afterCarrega( CarregaEvent cevt ) {

		if ( cevt.getListaCampos() == lcCampos ) {
			DAORecMerc.atualizaStatus( txtStatus.getVlrString(), lbStatus );
			txtTicketCP.setVlrInteger( txtTicket.getVlrInteger() );
			lcCompra.carregaDados();
			txtTicketVD.setVlrInteger( txtTicket.getVlrInteger() );
			lcVenda.carregaDados();
			
		}
		else if ( cevt.getListaCampos() == lcProd || cevt.getListaCampos() == lcProd2 ) {
			if ( "S".equals( txtSerieProd.getVlrString() ) ) {

				lbNumSerie.setVisible( true );
				txtNumSerie.setVisible( true );

				lbDtFabricSerie.setVisible( true );
				txtDtFabricSerie.setVisible( true );

				lbDtValidSerie.setVisible( true );
				txtDtValidSerie.setVisible( true );
				lbGarantia.setVisible( true );
				cbGarantia.setVisible( true );


				// habilitaSerie();

			}
			else {

				lbNumSerie.setVisible( false );
				txtNumSerie.setVisible( false );

				lbDtFabricSerie.setVisible( false );
				txtDtFabricSerie.setVisible( false );

				lbDtValidSerie.setVisible( false );
				txtDtValidSerie.setVisible( false );
				
				lbGarantia.setVisible( false );
				cbGarantia.setVisible( false );

			}
		}
		else if ( cevt.getListaCampos() == lcNumSerie && lcDet.getStatus() == ListaCampos.LCS_INSERT ) {
			verificaGarantia();
		}

		

	}

	/*
	 * private void habilitaSerie() { if( "S".equals( txtSerieProd.getVlrString() ) && txtQtdItColeta.getVlrBigDecimal().compareTo( new BigDecimal( 1 ) )==0 ) {
	 * 
	 * txtNumSerie.setEditable( true ); } else { txtNumSerie.setEditable( false ); }
	 * 
	 * }
	 */

	public void beforeCarrega( CarregaEvent cevt ) {

		// TODO Auto-generated method stub

	}

	public boolean testaNumSerie() {

		boolean bRetorno = false;
		boolean bValido = false;

		// Validação e abertura da tela para cadastramento da serie unitária
		if ( txtNumSerie.isEditable() ) {

			String sSQL = "SELECT COUNT(*) FROM EQSERIE WHERE NUMSERIE=? AND CODPROD=? AND CODEMP=? AND CODFILIAL=?";

			PreparedStatement ps = null;
			ResultSet rs = null;

			try { 

				ps = con.prepareStatement( sSQL );
				ps.setString( 1, txtNumSerie.getVlrString() );
				ps.setInt( 2, txtCodProd.getVlrInteger() );
				ps.setInt( 3, Aplicativo.iCodEmp );
				ps.setInt( 4, lcNumSerie.getCodFilial() );

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
				Funcoes.mensagemErro( this, "Erro ao consultar a tabela EQSERIE!\n" + err.getMessage(), true, con, err );
			}
			if ( !bValido ) {

				DLSerie dl = new DLSerie( this, txtNumSerie.getVlrString(), txtCodProd.getVlrInteger(), txtDescProd.getVlrString() );

				dl.setVisible( true );

				if ( dl.OK ) {
					bRetorno = true;
					txtNumSerie.setVlrString( dl.getNumSerie() );
					lcNumSerie.carregaDados();
				}
				dl.dispose();
			}
			else {
				bRetorno = true;
			}
		}
		// Tela para cadastramento da série para quantidade maior que 1
		else {

			// abreDlSerieMuitiplos();

		}

		return bRetorno;
	}
	
	private void consistDocRecMerc( PostEvent pevt) {
		if (lcCampos.getStatus()==ListaCampos.LCS_INSERT) {
			try {
				Integer ticket = daocoleta.consistDocColeta( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "EQRECMERC" ), 
					txtTicket.getVlrInteger(), txtDocRecMerc.getVlrInteger() );
				if (ticket!=null) {
					Funcoes.mensagemInforma( this, "Este documento já foi utilizado no ticket "+ticket );
					pevt.cancela();
				}
			} catch (SQLException err) {
				Funcoes.mensagemErro( this, "Erro consistindo número do documento !\n"+err.getMessage() );
				pevt.cancela();
			}
		}
	}
	
	public void beforePost( PostEvent pevt ) {

		if ( pevt.getListaCampos() == lcCampos ) {
			if (txtDocRecMerc.getVlrInteger().intValue()==0) {
				Funcoes.mensagemInforma( this, "Campo número do documento é obrigatório !" );
				txtDocRecMerc.requestFocus();
				pevt.cancela();
				return;
			}
			carregaTipoRec();
			if ( "".equals( txtStatus.getVlrString() ) ) {
				txtStatus.setVlrString( (String) DAORecMerc.STATUS_PENDENTE.getValue() );
			}

			if ( pevt.getEstado() == ListaCampos.LCS_INSERT ) {
				novo = true;
			}
			consistDocRecMerc( pevt );
		}
		else if ( pevt.getListaCampos() == lcDet ) {
			txtCodTipoRecMercDet.setVlrInteger( txtCodTipoRecMerc.getVlrInteger() );
			txtCodProcRecMerc.setVlrInteger( 1 );
			
			
			if ( txtSerieProd.getVlrString().equals( "S" ) && txtQtdItColeta.getVlrBigDecimal().floatValue() == 1 ) {
				if ( !testaNumSerie() ) {
					pevt.cancela();
				}
			}
		}
		super.beforePost( pevt );
	}

	public void afterPost( PostEvent pevt ) {

		super.beforePost( pevt );

		if ( pevt.getListaCampos() == lcCampos ) {
			if ( novo ) {
				lcDet.insert( true );
				novo = false;
				if ( comRef() ) {
					txtRefProd.requestFocus();
				}
				else {
					txtCodProd.requestFocus();
				}
			}
		}
		if ( pevt.getListaCampos() == lcDet ) {
			lcCampos.carregaDados();
		}
	}

	public void beforeInsert( InsertEvent e ) {

	}

	public void afterInsert( InsertEvent ievt ) {
		Integer codtran = null;
		
		if ( ievt.getListaCampos() == lcCampos ) {
			carregaTipoRec();
			codtran = (Integer) daocoleta.getPrefs()[PREFS.CODTRAN.ordinal()];
			if( ( codtran!=null ) && ( codtran.intValue()!=0 ) ){
				txtCodTran.setVlrInteger( codtran );
				lcTran.carregaDados();
			}
			// Muda o tipo de frete default para F, evitando a geração de conhecimento de frete nas coletas.
			txtTipoFrete.setVlrString( "F" );
		}

	}

	public void keyPressed( KeyEvent kevt ) {

		if ( kevt.getKeyCode() == KeyEvent.VK_ENTER ) {
			if ( kevt.getSource() == txtCodTran) {// Talvez este possa ser o ultimo campo do cabecalho.
				if ( lcCampos.getStatus() == ListaCampos.LCS_INSERT || lcCampos.getStatus() == ListaCampos.LCS_EDIT ) {
					lcCampos.post();
				}
				/*
				 * else if ( lcDet.getStatus() == ListaCampos.LCS_EDIT ) { lcCampos.post(); txtCodItRecMerc.requestFocus(); }
				 */
			}

			if ( ( (kevt.getSource() == txtQtdItColeta && !txtNumSerie.isEditable()) || kevt.getSource() == txtNumSerie ) && ( ( lcDet.getStatus() == ListaCampos.LCS_INSERT ) || ( lcDet.getStatus() == ListaCampos.LCS_EDIT ) ) ) {
				if ( "S".equals( txtSerieProd.getVlrString() ) && kevt.getSource() == txtNumSerie ) {

					lcDet.post();

					lcDet.limpaCampos( true );

					lcDet.setState( ListaCampos.LCS_NONE );

					if ( comRef() ) {
						txtRefProd.requestFocus();
					}
					else {
						txtCodProd.requestFocus();
					}

				}
				else if ( !"S".equals( txtSerieProd.getVlrString() ) && kevt.getSource() == txtQtdItColeta ) {

					lcDet.post();

					lcDet.limpaCampos( true );

					lcDet.setState( ListaCampos.LCS_NONE );

					if ( comRef() ) {
						txtRefProd.requestFocus();
					}
					else {
						txtCodProd.requestFocus();
					}

				}
			}
		}

		// super.keyPressed( kevt );

	}
	
	public static void createColeta( DbConnection cn,  int ticket) {
		String titulo = "Coleta de materiais";
		String nome = FColeta.class.getName();
		FColeta coleta = null;
		coleta = (FColeta) Aplicativo.telaPrincipal.getTela( nome );
		if ( coleta == null ) {
			coleta = new FColeta();
			Aplicativo.telaPrincipal.criatela( titulo, coleta, cn );
		}
		coleta.txtTicket.setVlrInteger( ticket );
		coleta.lcCampos.carregaDados();
		coleta.show();
	}

}
