/**
 * @version 05/04/2011 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 * Projeto: Freedom <BR>
 * 
 * Pacote: org.freedom.modulos.gms.view.frame.crud.detail <BR>
 * Classe:
 * @(#)FImportacao.java <BR>
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
 * Tela de cadastramento de compras de importação.
 * 
 */

package org.freedom.modulos.gms.view.frame.crud.detail;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.DeleteEvent;
import org.freedom.acao.DeleteListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.TabelaEditEvent;
import org.freedom.acao.TabelaEditListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.functions.ConversionFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDetalhe;
import org.freedom.library.swing.util.SwingParams;
import org.freedom.modulos.gms.dao.DAOImportacao;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.lvf.view.frame.crud.detail.FCLFiscal;
import org.freedom.modulos.std.view.frame.crud.plain.FUnidade;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCliente;
import org.freedom.modulos.std.view.frame.crud.tabbed.FMoeda;
import org.freedom.modulos.std.view.frame.crud.tabbed.FTransp;

public class FImportacao extends FDetalhe implements ActionListener, ChangeListener, InsertListener, DeleteListener, CarregaListener, TabelaEditListener, MouseListener {

	private static final long serialVersionUID = 1L;

	private JTabbedPanePad 	tpnGrid 			= 	new JTabbedPanePad();
	private JTabbedPanePad 	tpnCab	 			= 	new JTabbedPanePad();
	
	private JPanelPad 		pnCliCabComplementar= 	new JPanelPad( 500, 300 );
	private JPanelPad 		pnCliCabPrincipal	= 	new JPanelPad( 500, 300 );

	private JTextFieldPad 	txtCodImp 			= 	new JTextFieldPad( 	JTextFieldPad.TP_INTEGER	, 8		, 0 );
	private JTextFieldPad 	txtInvoice 			= 	new JTextFieldPad( 	JTextFieldPad.TP_STRING		, 20	, 0 );
	private JTextFieldPad 	txtDI	 			= 	new JTextFieldPad( 	JTextFieldPad.TP_STRING		, 10	, 0 );
	private JTextFieldPad 	txtManifesto		= 	new JTextFieldPad( 	JTextFieldPad.TP_STRING		, 20	, 0 );
	private JTextFieldPad 	txtCertOrigem		= 	new JTextFieldPad( 	JTextFieldPad.TP_STRING		, 200	, 0 );
	private JTextFieldPad 	txtLacre			= 	new JTextFieldPad( 	JTextFieldPad.TP_STRING		, 100	, 0 );
	private JTextFieldPad 	txtPresCarga		= 	new JTextFieldPad( 	JTextFieldPad.TP_STRING		, 200	, 0 );
	private JTextFieldPad 	txtIdentHouse		= 	new JTextFieldPad( 	JTextFieldPad.TP_STRING		, 100	, 0 );
	private JTextFieldPad 	txtDta				= 	new JTextFieldPad( 	JTextFieldPad.TP_STRING		, 50	, 0 );
	private JTextFieldPad 	txtConhecCarga		= 	new JTextFieldPad( 	JTextFieldPad.TP_STRING		, 100	, 0 );
	private JTextFieldPad 	txtIdentContainer	= 	new JTextFieldPad( 	JTextFieldPad.TP_STRING		, 20	, 0 );
	private JTextFieldPad 	txtTipoManifesto	= 	new JTextFieldPad( 	JTextFieldPad.TP_STRING		, 20	, 0 );
	private JTextFieldPad 	txtDtImp			= 	new JTextFieldPad( 	JTextFieldPad.TP_DATE		, 10	, 0 );
	private JTextFieldPad 	txtDtEmb			= 	new JTextFieldPad( 	JTextFieldPad.TP_DATE		, 10	, 0 );
	private JTextFieldPad 	txtDtChegada		= 	new JTextFieldPad( 	JTextFieldPad.TP_DATE		, 10	, 0 );
	private JTextFieldPad 	txtDtDesembDI		= 	new JTextFieldPad( 	JTextFieldPad.TP_DATE		, 10	, 0 );
	private JTextFieldPad 	txtDtRegDI			= 	new JTextFieldPad( 	JTextFieldPad.TP_DATE		, 10	, 0 );
	private JTextAreaPad 	txtaObs				= 	new JTextAreaPad( 1024 );
	private JTextFieldPad 	txtLocalEmb			= 	new JTextFieldPad( 	JTextFieldPad.TP_STRING 	, 200	, 0 );
	private JTextFieldPad 	txtVeiculo			= 	new JTextFieldPad( 	JTextFieldPad.TP_STRING 	, 200	, 0 );
	private JTextFieldPad 	txtRecintoAduaneiro	= 	new JTextFieldPad( 	JTextFieldPad.TP_STRING 	, 200	, 0 );
	private JTextFieldPad 	txtLocalDesembDI	= 	new JTextFieldPad( 	JTextFieldPad.TP_STRING 	, 60	, 0 );
	private JTextFieldPad 	txtSiglaUFDesembDI	= 	new JTextFieldPad( 	JTextFieldPad.TP_STRING 	, 2		, 0 );
	private JTextFieldPad 	txtCodPaisDesembDI	= 	new JTextFieldPad( 	JTextFieldPad.TP_INTEGER 	, 8		, 0 );

	private JTextFieldPad 	txtPesoBrutoTot		= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, 0 );
	private JTextFieldPad 	txtPesoLiquidoTot	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, 0 );
	private JTextFieldPad 	txtVlrFreteMITOT	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrFreteTOT 		= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVMLEMITOT		= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVMLDMITOT		= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVMLETOT	 		= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVMLDTOT			= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVLRADTOT			= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVLRADMITOT		= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrSeguroMITOT	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrSeguroTOT		= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrIITOT	 		= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrIPITOT 		= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrPISTOT 		= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrCOFINSTOT		= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrDireitosAD	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrTHCTOT 		= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrTHCMITOT 		= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrTXSisComexTOT	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrBaseICMSTOT  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrICMSRecolhimentoTOT 	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL, 15	, Aplicativo.casasDecFin );
	
	private JTextFieldPad 	txtCotacaoMoeda 		= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );

	private JTextFieldPad 	txtCodFor 			= 	new JTextFieldPad( 	JTextFieldPad.TP_INTEGER	, 8		, 0 );
	private JTextFieldFK 	txtRazFor 			= 	new JTextFieldFK( 	JTextFieldPad.TP_STRING		, 60	, 0 );
	private JTextFieldFK 	txtCodPais 			= 	new JTextFieldFK( 	JTextFieldPad.TP_INTEGER	, 4		, 0 );
	
	private JTextFieldPad 	txtCodPlanoPag 		= 	new JTextFieldPad( 	JTextFieldPad.TP_INTEGER	, 8		, 0 );
	private JTextFieldFK 	txtDescPlanoPag 	= 	new JTextFieldFK( 	JTextFieldPad.TP_STRING		, 50	, 0 );
	
	private JTextFieldPad 	txtCodMoeda 		= 	new JTextFieldPad( 	JTextFieldPad.TP_STRING		, 4		, 0 );
	private JTextFieldFK 	txtSingMoeda 		= 	new JTextFieldFK( 	JTextFieldPad.TP_STRING		, 20	, 0 );

	private JTextFieldPad 	txtCodItImp 		= 	new JTextFieldPad( 	JTextFieldPad.TP_INTEGER	, 4		, 0 );
	private JTextFieldPad 	txtCodProd 			= 	new JTextFieldPad( 	JTextFieldPad.TP_STRING		, 10	, 0 );
	private JTextFieldFK 	txtRefProdPd 		= 	new JTextFieldFK( 	JTextFieldPad.TP_STRING		, 20	, 0 );
	private JTextFieldFK 	txtCodFabProd 		= 	new JTextFieldFK( 	JTextFieldPad.TP_STRING		, 20	, 0 );
	private JTextFieldFK 	txtDescProd 		= 	new JTextFieldFK( 	JTextFieldPad.TP_STRING		, 100	, 0 );
	private JTextFieldPad 	txtCodUnid 			= 	new JTextFieldPad( 	JTextFieldPad.TP_STRING		, 20	, 0 );
	private JTextFieldFK 	txtDescUnid 		= 	new JTextFieldFK( 	JTextFieldPad.TP_STRING		, 20	, 0 );

	private JTextFieldPad 	txtCodFisc 			= 	new JTextFieldPad( 	JTextFieldPad.TP_STRING		, 13	, 0 );
	private JTextFieldPad 	txtCodItFisc 		= 	new JTextFieldPad( 	JTextFieldPad.TP_INTEGER	, 5		, 0 );
	private JTextFieldFK 	txtCodNCM	 		= 	new JTextFieldFK( 	JTextFieldPad.TP_STRING		, 10	, 0 );
	
	private JTextFieldPad	txtQtd				= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 10	, Aplicativo.casasDec );
	private JTextFieldPad 	txtPesoBruto		= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDec );
	private JTextFieldPad 	txtPesoLiquido  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDec );
	private JTextFieldPad 	txtPrecoMI		  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecPre );
	
	private JTextFieldPad 	txtVMLEMI		  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVMLDMI		  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVMLE			  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVMLD			  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrFreteMI	  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrFrete		  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrSeguroMI	  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrSeguro	  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrTHCMI		  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrTHC		  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrADMI		  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrAD		  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtAliqICMSImp	  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtAliqICMSUF	  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtPercDiferICMS  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtAliqPIS		  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtAliqCOFINS	  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtAliqII		  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtAliqIPI		  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtPercCredPresImp 	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrII		  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrIPI		  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrPIS		  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrCOFINS	  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrBaseICMS	  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrICMS		  	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrICMSDiferido 	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrICMSDevido 	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrICMSCredPresum	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrICMSRecolhimento 	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrTXSisComex	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrComplTot		= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrCompl  		= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtTipoImp 			=   new JTextFieldPad( 	JTextFieldPad.TP_STRING	, 1 , 0 );
	
	private JTextFieldFK 	txtAliqIIFisc 		= 	new JTextFieldFK( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldFK 	txtAliqIPIFisc 		= 	new JTextFieldFK( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldFK 	txtAliqPISFisc 		= 	new JTextFieldFK( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldFK 	txtAliqCOFINSFisc	= 	new JTextFieldFK( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldFK 	txtAliqICMSUFFisc 	= 	new JTextFieldFK( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldFK 	txtAliqICMSImpFisc 	= 	new JTextFieldFK( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldFK 	txtRedFisc 			= 	new JTextFieldFK( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	
	private JTextFieldFK 	txtItCodFisc 		= 	new JTextFieldFK( 	JTextFieldPad.TP_INTEGER	, 5		, 0 );
	
	private JTextFieldFK 	txtPesoLiqProd 		= 	new JTextFieldFK( 	JTextFieldPad.TP_DECIMAL	, 10	, Aplicativo.casasDec );
	private JTextFieldFK 	txtPesoBrutProd 	= 	new JTextFieldFK( 	JTextFieldPad.TP_DECIMAL	, 10	, Aplicativo.casasDec );
	
	private JTextFieldFK 	txtCodAdic	 		= 	new JTextFieldFK( 	JTextFieldPad.TP_INTEGER	, 5		, 0 );
	private JTextFieldFK 	txtCodNCMAdic 		= 	new JTextFieldFK( 	JTextFieldPad.TP_STRING		, 10	, 0 );
	private JTextFieldPad 	txtVlrTXSisComexAdic= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlrDespAd	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	private JTextFieldPad 	txtVlritDespAd	= 	new JTextFieldPad( 	JTextFieldPad.TP_DECIMAL	, 15	, Aplicativo.casasDecFin );
	
	private JButtonPad btRateioFrete = new JButtonPad( "Rateio Frete/THC", Icone.novo( "btRateio.png" ));
	
	private JButtonPad btGerarAdicoes = new JButtonPad( "Gerar adições", Icone.novo( "btGerar.png" ) );
	
	private JButtonPad btRateioSiscomex = new JButtonPad( "Rateio Tx. Siscomex", Icone.novo( "btRateio.png" ) );
	
	private ListaCampos lcForneced = new ListaCampos( this, "FR" );

	private ListaCampos lcPlanoPag = new ListaCampos( this, "PG" );

	private ListaCampos lcMoeda = new ListaCampos( this, "MI" );

	private ListaCampos lcProduto = new ListaCampos( this, "PD" );

	private ListaCampos lcUnid = new ListaCampos( this, "UN" );
	
	private ListaCampos lcClFiscal = new ListaCampos( this, "CF" );
	
	private ListaCampos lcItClFiscal = new ListaCampos( this );
	
	private ListaCampos lcAdicao = new ListaCampos( this );
	
	private JPanelPad pinDet = new JPanelPad();

	private String codmoeda = Aplicativo.codmoeda.trim();
	
	private JTablePad tabAdicao = new JTablePad();
	
	private JScrollPane spnAdicao = new JScrollPane( tabAdicao );
	
	public Navegador navAdic = new Navegador(true);
	
	public enum GRID_ADICAO { CODNCM, CODADIC, VLRTXSISCOMEXADIC, PESOLIQUIDO, VLRTHCMI, VMLEMI, VLRFRETEMI, VMCV, VLRADUANEIRO, VLRII, VLRIPI, VLRPIS, VLRCOFINS, VLRBASEICMS, VLRICMSRECOLHER };

	private DAOImportacao daoimp = null;
	
	
	public FImportacao() {

		nav.setNavigation( true );

		setTitulo( "Compras (Importação)" );

		setAltCab( 300 );
		setAtribos( 20, 20, 930, 700 );

		montaRadios();
		montaListaCampos();

		montaTela();

		adicListeners();

		adicToolTips();

	}

	private void adicToolTips() {

		btRateioFrete.setToolTipText( "Rateia o valor total do frete e THC entre os ítens" );
		btGerarAdicoes.setToolTipText( "Gerar tabela de adições" );
		btRateioSiscomex.setToolTipText( "Rateia o valor total da taxa siscomex entre os ítens" );

	}

	private void adicListeners() {

		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		btRateioFrete.addActionListener( this );
		btGerarAdicoes.addActionListener( this );
		btRateioSiscomex.addActionListener( this );
		
		lcCampos.addInsertListener( this );
		lcDet.addInsertListener( this );
		
		lcDet.addPostListener( this );
		
		lcDet.addDeleteListener( this );

		lcForneced.addCarregaListener( this );
		lcClFiscal.addCarregaListener( this );
		lcItClFiscal.addCarregaListener( this );
		lcAdicao.addCarregaListener( this );

		tpnGrid.addChangeListener( this );

	}

	private void montaRadios() {
		/*
		Vector<String> vVals = new Vector<String>();
		Vector<String> vLabs = new Vector<String>();
		vVals.addElement( "1" );
		vVals.addElement( "2" );
		vLabs.addElement( "CIF" );
		vLabs.addElement( "FOB" );
		rgTipoFrete = new JRadioGroup<String, String>( 2, 1, vLabs, vVals );

		vVals = new Vector<String>();
		vLabs = new Vector<String>();
		vVals.addElement( "P" );
		vVals.addElement( "A" );
		vLabs.addElement( "Pago" );
		vLabs.addElement( "À pagar" );
		rgTipoPgto = new JRadioGroup<String, String>( 2, 1, vLabs, vVals );
		*/
	}

	private void montaListaCampos() {

		/********************
		 * FORNECEDOR *
		 ********************/
		lcForneced.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.For.", ListaCampos.DB_PK, true ) );
		lcForneced.add( new GuardaCampo( txtRazFor, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );
		lcForneced.add( new GuardaCampo( txtCodPais, "CodPais", "Cód.País", ListaCampos.DB_SI, false ) );

		txtCodFor.setTabelaExterna( lcForneced, FTransp.class.getCanonicalName() );
		txtCodFor.setNomeCampo( "CodFor" );
		txtCodFor.setFK( true );
		
		lcForneced.montaSql( false, "FORNECED", "CP" );
		lcForneced.setReadOnly( true );


		/**********************
		 * PLANO DE PAGAMENTO *
		 *********************/
		lcPlanoPag.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.Pag.", ListaCampos.DB_PK, true ) );
		lcPlanoPag.add( new GuardaCampo( txtDescPlanoPag, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		
		txtCodPlanoPag.setTabelaExterna( lcPlanoPag, FCliente.class.getCanonicalName() );
		txtCodPlanoPag.setNomeCampo( "CodPlanoPag" );
		txtCodPlanoPag.setFK( true );
		
		lcPlanoPag.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag.setReadOnly( true );

		/************
		 * MOEDA    *
		 ************/
		lcMoeda.add( new GuardaCampo( txtCodMoeda, "CodMoeda", "Moeda", ListaCampos.DB_PK, true ) );
		lcMoeda.add( new GuardaCampo( txtSingMoeda, "SingMoeda", "Singular", ListaCampos.DB_SI, false ) );
			
		txtCodMoeda.setTabelaExterna( lcMoeda, FMoeda.class.getCanonicalName() );
		txtCodMoeda.setNomeCampo( "CodCompra" );
		txtCodMoeda.setFK( true );
		
		lcMoeda.montaSql( false, "MOEDA", "FN" );
		lcMoeda.setReadOnly( true );
		
		/**********************
		 * PRODUTO POR CODIGO *
		 **********************/
		lcProduto.add( new GuardaCampo( txtCodProd		, "CodProd"		, "Cód.Prod."				, ListaCampos.DB_PK, true ) );
		lcProduto.add( new GuardaCampo( txtRefProdPd	, "RefProd"		, "Referência"				, ListaCampos.DB_SI, false ) );
		lcProduto.add( new GuardaCampo( txtCodFabProd	, "CodFabProd"	, "Cod.Fabric."				, ListaCampos.DB_SI, false ) );
		
		lcProduto.add( new GuardaCampo( txtDescProd		, "DescProd"	, "Descrição do produto"	, ListaCampos.DB_SI, false ) );
		lcProduto.add( new GuardaCampo( txtCodUnid		, "CodUnid"		, "Cód.Unid."				, ListaCampos.DB_SI, false ) );
		lcProduto.add( new GuardaCampo( txtCodFisc		, "CodFisc"		, "Cód.Fisc."				, ListaCampos.DB_FK, false ) );
		lcProduto.add( new GuardaCampo( txtPesoLiqProd	, "PesoLiqProd"	, "Peso Liq."				, ListaCampos.DB_SI, false ) );
		lcProduto.add( new GuardaCampo( txtPesoBrutProd	, "PesoBrutProd", "Peso Bruto"				, ListaCampos.DB_SI, false ) );
		
		txtCodProd.setTabelaExterna( lcProduto, FProduto.class.getCanonicalName() );
		txtCodProd.setNomeCampo( "CodProd" );
		txtCodProd.setFK( true );
		
		lcProduto.montaSql( false, "PRODUTO", "EQ" );
		lcProduto.setReadOnly( true );
		
		/**********************
		 * UNIDADE *
		 **********************/
		lcUnid.add( new GuardaCampo( txtCodUnid, "CodUnid", "Cód.unid.", ListaCampos.DB_PK, false ) );
		lcUnid.add( new GuardaCampo( txtDescUnid, "DescUnid", "Unidade", ListaCampos.DB_SI, false ) );
		lcUnid.montaSql( false, "UNIDADE", "EQ" );
		lcUnid.setReadOnly( true );
		lcUnid.setQueryCommit( false );
		txtCodUnid.setTabelaExterna( lcUnid, FUnidade.class.getCanonicalName() );
		
		/**********************
		 * CLASSIFICAÇÂO FISCAL *
		 **********************/
		lcClFiscal.add( new GuardaCampo( txtCodFisc, "CodFisc", "Cód.fisc.", ListaCampos.DB_PK, false ) );
		lcClFiscal.add( new GuardaCampo( txtCodNCM, "CodNCM", "NCM", ListaCampos.DB_SI, false ) );
		lcClFiscal.montaSql( false, "CLFISCAL", "LF" );
		lcClFiscal.setReadOnly( true );
		lcClFiscal.setQueryCommit( false );
		txtCodFisc.setTabelaExterna( lcClFiscal, FCLFiscal.class.getCanonicalName() );

		/********************************
		 * ITEM DE CLASSIFICACAO FISCAL *
		 ********************************/

		lcItClFiscal.add( new GuardaCampo( txtCodFisc, "CodFisc", "Cód.fisc.", ListaCampos.DB_PK, false ) );
		lcItClFiscal.add( new GuardaCampo( txtCodItFisc, "CodItFisc", "Cód.It.fisc.", ListaCampos.DB_PK, false ) );
		lcItClFiscal.add( new GuardaCampo( txtAliqIIFisc, "AliqIIFisc", "Aliq.II", ListaCampos.DB_SI, false ) );
		lcItClFiscal.add( new GuardaCampo( txtAliqIPIFisc, "AliqIPIFisc", "Aliq.IPI", ListaCampos.DB_SI, false ) );
		lcItClFiscal.add( new GuardaCampo( txtAliqPISFisc, "AliqPisFisc", "Aliq.Pis", ListaCampos.DB_SI, false ) );
		lcItClFiscal.add( new GuardaCampo( txtAliqCOFINSFisc, "AliqCofinsFisc", "Aliq.Cofins", ListaCampos.DB_SI, false ) );
		lcItClFiscal.add( new GuardaCampo( txtAliqICMSUFFisc, "AliqFisc", "Aliq.ICMS.UF", ListaCampos.DB_SI, false ) );
		lcItClFiscal.add( new GuardaCampo( txtAliqICMSImpFisc, "AliqIcmsImp", "Aliq.ICMS.Imp.", ListaCampos.DB_SI, false ) );
		lcItClFiscal.add( new GuardaCampo( txtRedFisc, "RedFisc", "Red.Fisc.", ListaCampos.DB_SI, false ) );
		lcItClFiscal.add( new GuardaCampo( txtPercCredPresImp, "PercCredPresImp", "% Cred. Pres", ListaCampos.DB_SI, false) );
		

		lcItClFiscal.montaSql( false, "ITCLFISCAL", "LF" );
		lcItClFiscal.setReadOnly( true );
		lcItClFiscal.setQueryCommit( false );
		txtCodItFisc.setTabelaExterna( lcItClFiscal, FCLFiscal.class.getCanonicalName() );

		lcAdicao.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcAdicao );
		lcAdicao.setTabela( tabAdicao );
		
		
		
	}

	private void montaTela() {

	   /*********************************
		* 
		* DEFINIÇÕES DO CABEÇALHO
		* 
		********************************/

		setAltCab( 270 );
		
		setListaCampos( lcCampos );
		lcCampos.setNavegador( nav );
		
		pnCab.add( tpnCab );
		
		tpnCab.add( "Principal" 	, pnCliCabPrincipal );
		tpnCab.add( "Complementar" 	, pnCliCabComplementar );
		
		// Criando abas
		
		pnMaster.remove( spTab );
		
		pnMaster.add( tpnGrid );
		
		tpnGrid.add( "Itens", spTab );
		tpnGrid.add( "Agrupamento (NCM)", spnAdicao );
	
		
		JPanelPad pnValoresTotaisMI = new JPanelPad();
		pnValoresTotaisMI.setBorder( SwingParams.getPanelLabel( "Valores em moeda estrangeira", Color.BLUE ) );
		
		JPanelPad pnValoresTotais = new JPanelPad();
		pnValoresTotais.setBorder( SwingParams.getPanelLabel( "Valores em " + codmoeda, SwingParams.COR_VERDE_FREEDOM ) );
		
		
		// Adicionando campos na aba principal
		
		setPainel( pnCliCabPrincipal );
		
		adicCampo( txtCodImp		, 7		, 20	, 70	, 20	, "CodImp"		, "Cód. Imp."	, ListaCampos.DB_PK	, true 	);
		adicCampo( txtCodFor		, 80	, 20	, 60	, 20	, "CodFor"		, "Cód. For."	, ListaCampos.DB_FK	, true 	);		
		adicDescFK( txtRazFor		, 143	, 20	, 243	, 20	, "RazFor"		, "Razão Social do Fornecedor" );

		adicCampo( txtDtImp			, 7		, 60	, 70	, 20	, "DtImp"		, "Data"		, ListaCampos.DB_SI	, true 	);
		
		adicCampo( txtCodPlanoPag	, 80	, 60	, 60	, 20	, "CodPlanoPag"	, "Cód. Pag."	, ListaCampos.DB_FK	, true 	);		
		adicDescFK( txtDescPlanoPag	, 143	, 60	, 243	, 20	, "DescPlanoPag", "Descrição do plano de pagamento" );

		adicCampo( txtCotacaoMoeda	, 7		, 100	, 70	, 20	, "CotacaoMoeda", "Cotação"		, ListaCampos.DB_SI	, true 	);	
		
		adicCampo( txtCodMoeda		, 80	, 100	, 60	, 20	, "CodMoeda"	, "Moeda"		, ListaCampos.DB_FK	, true 	);		
		adicDescFK( txtSingMoeda	, 143	, 100	, 243	, 20	, "SingMoeda"	, "" );
		
		adicCampo( txtInvoice		, 7		, 140	, 70	, 20	, "Invoice"			, "Invoice"			, ListaCampos.DB_SI	, false );
		adicCampo( txtDI			, 80	, 140	, 60	, 20	, "DI"				, "D.I."			, ListaCampos.DB_SI	, false );

		adicCampo( txtPesoBrutoTot	, 143	, 140	, 120	, 20	, "PesoBruto"	, "Peso Bruto"	, ListaCampos.DB_SI	, false	);
		adicCampo( txtPesoLiquidoTot, 266	, 140	, 120	, 20	, "PesoLiquido"	, "Peso Liquido", ListaCampos.DB_SI	, false	);

		adicCampo( txtTipoImp		, 7		, 180	, 50	, 20	, "TipoImp"			, "Tipo"			, ListaCampos.DB_SI	, false );

		txtPesoBrutoTot.setSoLeitura( true );
		txtPesoLiquidoTot.setSoLeitura( true );
		txtTipoImp.setSoLeitura( true );
		
		pnCliCabPrincipal.adic( pnValoresTotaisMI	, 396	, 0	, 250	, 163 );
		pnCliCabPrincipal.adic( pnValoresTotais		, 648	, 0	, 250	, 195 );
	/*	
		adic( btRateioFrete			, 398	, 198	, 158	, 22 );
		adic( btGerarAdicoes		, 558	, 198	, 160	, 22 );
		adic( btRateioSiscomex		, 720	, 198	, 176	, 22 );
	*/
		adic( btRateioFrete			, 144, 168	, 158	, 22 );
		adic( btGerarAdicoes		, 305	, 168	, 160	, 22 );
		adic( btRateioSiscomex		, 468	, 168	, 176	, 22 );

		setPainel( pnValoresTotaisMI );
				
		adicCampo( txtVlrFreteMITOT	, 7		, 20	, 110	, 20	, "VlrFreteMI"	, "Frete"						, ListaCampos.DB_SI	, true 	);
		adicCampo( txtVlrSeguroMITOT, 120	, 20	, 110	, 20	, "VlrSeguroMI"	, "Seguro"						, ListaCampos.DB_SI	, true 	);
		
		adicCampo( txtVMLEMITOT		, 7		, 60	, 110	, 20	, "VMLEMI"		, "VMLE"						, ListaCampos.DB_SI	, false	);
		adicCampo( txtVMLDMITOT		, 120	, 60	, 110	, 20	, "VMLDMI"		, "VMLD"						, ListaCampos.DB_SI	, false	);
		
		adicCampo( txtVlrTHCMITOT	, 7		, 100	, 110	, 20	, "VlrTHCMI"	, "THC"							, ListaCampos.DB_SI	, false	);
		adicCampo( txtVLRADMITOT	, 120	, 100	, 110	, 20	, "VLRADMI"		, "Vlr.Aduaneiro"				, ListaCampos.DB_SI	, false	);
		
		setPainel( pnValoresTotais );
		
		adicCampo( txtVlrFreteTOT	, 7		, 20	, 110	, 20	, "VlrFrete"	, "Frete "			+ codmoeda	, ListaCampos.DB_SI	, false	);
		adicCampo( txtVlrSeguroTOT	, 120	, 20	, 110	, 20	, "VlrSeguro"	, "Seguro "			+ codmoeda	, ListaCampos.DB_SI	, false	);		
		
		adicCampo( txtVMLETOT		, 7		, 60	, 110	, 20	, "VMLE"		, "VMLE "			+ codmoeda	, ListaCampos.DB_SI	, false	);
		adicCampo( txtVMLDTOT		, 120	, 60	, 110	, 20	, "VMLD"		, "VMLD "			+ codmoeda	, ListaCampos.DB_SI	, false	);
		
		adicCampo( txtVlrTHCTOT		, 7		, 100	, 110	, 20	, "VlrTHC"		, "THC "			+ codmoeda	, ListaCampos.DB_SI	, true 	);
		adicCampo( txtVLRADTOT		, 120	, 100	, 110	, 20	, "VLRAD"		, "Vlr.Aduaneiro " 	+ codmoeda	, ListaCampos.DB_SI	, false	);
		adicCampo( txtVlrDespAd	, 7	, 140	, 110	, 20	, "VLRDESPAD", "Desp.Aduaneira " + codmoeda	, ListaCampos.DB_SI	, false	);
		
		
		txtVlrFreteTOT.setSoLeitura( true );
		txtVlrSeguroTOT.setSoLeitura( true );
		txtVlrTHCMITOT.setSoLeitura( true );
		txtVMLETOT.setSoLeitura( true );
		txtVMLEMITOT.setSoLeitura( true );
		txtVMLDTOT.setSoLeitura( true );
		txtVMLDMITOT.setSoLeitura( true );
		txtVLRADMITOT.setSoLeitura( true );
		txtVLRADTOT.setSoLeitura( true );
		txtVlrBaseICMSTOT.setSoLeitura( true );
		txtVlrICMSRecolhimentoTOT.setSoLeitura( true );
		
		setPainel( pnCliCabPrincipal );
		
		
		// Adicionando campos na aba complementar
		
		setPainel( pnCliCabComplementar );

		adicCampo( txtDtEmb						, 7		, 20	, 100	, 20	, "DtEmb"			, "Dt.Embarque"				, ListaCampos.DB_SI	, false );
		adicCampo( txtManifesto					, 110	, 20	, 100	, 20	, "Manifesto"		, "Manifesto"				, ListaCampos.DB_SI	, false );
		adicCampo( txtTipoManifesto				, 213	, 20	, 100	, 20	, "TipoManifesto"	, "Tipo de manifesto"		, ListaCampos.DB_SI	, false );
		adicCampo( txtLocalEmb					, 316	, 20	, 200	, 20	, "LocalEmb"		, "Local Embarque"			, ListaCampos.DB_SI	, false );
		adicCampo( txtVeiculo					, 519	, 20	, 180	, 20	, "Veiculo"			, "Veículo"					, ListaCampos.DB_SI	, false );
		adicCampo( txtCertOrigem				, 702	, 20	, 100	, 20	, "CertOrigem"		, "Cert.Origem"				, ListaCampos.DB_SI	, false );
		adicCampo( txtLacre						, 805	, 20	, 90	, 20	, "Lacre"			, "Lacre"					, ListaCampos.DB_SI	, false );

		adicCampo( txtDtChegada					, 7		, 60	, 100	, 20	, "DtChegada"		, "Dt.Chegada"				, ListaCampos.DB_SI	, false );
		adicCampo( txtPresCarga					, 110	, 60	, 100	, 20	, "PresCarga"		, "Presença de carga"		, ListaCampos.DB_SI	, false );
		adicCampo( txtIdentHouse				, 213	, 60	, 100	, 20	, "IdentHouse"		, "Ident.House"				, ListaCampos.DB_SI	, false );
		adicCampo( txtIdentContainer			, 316	, 60	, 200	, 20	, "IdentContainer"	, "Ident.Container"			, ListaCampos.DB_SI	, false );
		adicCampo( txtDta						, 519	, 60	, 180	, 20	, "DTA"				, "DTA"						, ListaCampos.DB_SI	, false );
		adicCampo( txtConhecCarga				, 702	, 60	, 192	, 20	, "ConhecCarga"		, "Conhecimento de carga"	, ListaCampos.DB_SI	, false );

		adicCampo( txtDtDesembDI				, 7		, 100	, 100	, 20	, "DtDesembDI"		, "Dt.Desemb."				, ListaCampos.DB_SI	, false );
		adicCampo( txtLocalDesembDI				, 110	, 100	, 100	, 20	, "LocDesembDI"		, "Local Desemb.DI"			, ListaCampos.DB_SI	, false );

		adicCampo( txtDtRegDI					, 213	, 100	, 100	, 20	, "DtRegDI"			, "Dt.Reg.DI"				, ListaCampos.DB_SI	, false );
		adicCampo( txtRecintoAduaneiro			, 316	, 100	, 200	, 20	, "RecintoAduaneiro", "Recinto aduaneiro"		, ListaCampos.DB_SI	, false );
		
		adicCampo( txtVlrTXSisComexTOT			, 7		, 140	, 100	, 20	, "VlrTXSisComex"	, "Tx.SisComex"		, ListaCampos.DB_SI	, false );
			
		adicCampo( txtVlrIITOT					, 110	, 140	, 100	, 20	, "VlrII"			, "Vlr.II"			, ListaCampos.DB_SI	, false );
		adicCampo( txtVlrIPITOT					, 213	, 140	, 100	, 20	, "VlrIPI"			, "Vlr.IPI"			, ListaCampos.DB_SI	, false );
		adicCampo( txtVlrPISTOT					, 316	, 140	, 100	, 20	, "VlrPIS"			, "Vlr.PIS"			, ListaCampos.DB_SI	, false );
		adicCampo( txtVlrCOFINSTOT				, 419	, 140	, 100	, 20	, "VlrCOFINS"		, "Vlr.COFINS"		, ListaCampos.DB_SI	, false );
		adicCampo( txtVlrBaseICMSTOT			, 522	, 140	, 100	, 20	, "VlrBaseICMS"		, "Vlr.Base ICMS"	, ListaCampos.DB_SI	, false );		
		adicCampo( txtVlrICMSRecolhimentoTOT	, 625	, 140	, 100	, 20	, "VlrICMSRecolhimento"	, "Vlr.ICMS"	, ListaCampos.DB_SI	, false );		
		adicCampo( txtVlrComplTot				, 728  , 140	, 100	, 20	, "VlrCompl"		, "Vlr. complementar"	, ListaCampos.DB_SI	, false );
		
		
		txtVlrIITOT.setSoLeitura( true );
		txtVlrIPITOT.setSoLeitura( true );
		txtVlrPISTOT.setSoLeitura( true );
		
		txtVlrTXSisComexTOT.setSoLeitura( true );
		txtVlrComplTot.setSoLeitura( true );
		txtVMLDMITOT.setSoLeitura( true );
		txtVMLEMITOT.setSoLeitura( true );
		txtVMLDTOT.setSoLeitura( true );
		txtVMLETOT.setSoLeitura( true );
		txtVlrCOFINSTOT.setSoLeitura( true );
		
		
//		JPanelPad pnValoresTotaisTributos = new JPanelPad();
//		pnValoresTotaisTributos.setBorder( SwingParams.getPanelLabel( "Totais", Color.BLUE ) );

//		setPainel( pnValoresTotaisTributos );
		
		// Definindo a tabela do banco de dados
		setListaCampos( true, "IMPORTACAO", "CP" );
		lcCampos.setQueryInsert( true );

	   /*********************************
		* 
		* DEFINIÇÕES DO DETALHAMENTO
		* 
		********************************/
		
		setAltDet( 230 );
				
		setPainel( pinDet, pnDet );
		setListaCampos( lcDet );
		
		setNavegador( navRod );
		
		lcDet.setMaster( lcCampos );
		
		
		// Adicionando campos
		adicCampo( txtCodItImp		, 7		, 20	, 50	, 20	, "CodItImp"			, "Item"					, ListaCampos.DB_PK	, true 	);
	
		adicCampoInvisivel( txtRefProdPd, "refprod", "Referência", ListaCampos.DB_SI, true );
		
		adicCampo( txtCodProd		, 60	, 20	, 100	, 20	, "CodProd"				, "Cód.Prod."				, ListaCampos.DB_FK	, true 	);
		
		adicDescFK( txtDescProd		, 163	, 20	, 355	, 20	, "DescProd"			, "Descrição do produto" );
		
		adicCampo( txtCodUnid		, 521	, 20	, 40	, 20	, "CodUnid"				, "Unid."					, ListaCampos.DB_FK	, false	);		
		adicCampo( txtQtd			, 564	, 20	, 80	, 20	, "Qtd"					, "Qtd."					, ListaCampos.DB_SI	, true 	);
		adicCampo( txtPesoBruto		, 647	, 20	, 80	, 20	, "PesoBruto"			, "Peso.Bruto"				, ListaCampos.DB_SI	, true 	);
		adicCampo( txtPesoLiquido	, 730	, 20	, 80	, 20	, "PesoLiquido"			, "Peso.Liq."				, ListaCampos.DB_SI	, true 	);
		
		adicCampo( txtPrecoMI		, 813	, 20	, 80	, 20	, "PrecoMI"				, "Preço " 					, ListaCampos.DB_SI	, false ).setForeground( Color.BLUE );
		
		adicCampo( txtVlrFreteMI	, 7		, 60	, 90	, 20	, "VlrFreteMI"			, "Vlr.Frete" 				, ListaCampos.DB_SI	, false	).setForeground( Color.BLUE );
		adicCampo( txtVlrFrete		, 100	, 60	, 90	, 20	, "VlrFrete"			, "Vlr.Frete " 	+ codmoeda 	, ListaCampos.DB_SI	, false ).setForeground( SwingParams.COR_VERDE_FREEDOM );

		adicCampo( txtVMLEMI		, 193	, 60	, 90	, 20	, "VMLEMI"				, "VMLE"					, ListaCampos.DB_SI	, false ).setForeground( Color.BLUE );
		adicCampo( txtVMLE			, 286	, 60	, 90	, 20	, "VMLE"				, "VMLE " 		+ codmoeda	, ListaCampos.DB_SI	, false ).setForeground( SwingParams.COR_VERDE_FREEDOM );
		
		adicCampo( txtVMLDMI		, 379	, 60	, 90	, 20	, "VMLDMI"				, "VMLD"					, ListaCampos.DB_SI	, false ).setForeground( Color.BLUE );
		adicCampo( txtVMLD			, 471	, 60	, 90	, 20	, "VMLD"				, "VMLD "		+ codmoeda	, ListaCampos.DB_SI	, false ).setForeground( SwingParams.COR_VERDE_FREEDOM );
		
		adicCampo( txtVlrTHCMI		, 564	, 60	, 80	, 20	, "VlrTHCMI"			, "THC"						, ListaCampos.DB_SI	, false ).setForeground( Color.BLUE );
		adicCampo( txtVlrTHC		, 647	, 60	, 80	, 20	, "VlrTHC"				, "THC "		+ codmoeda	, ListaCampos.DB_SI	, false ).setForeground( SwingParams.COR_VERDE_FREEDOM );
		adicCampo( txtVlrTXSisComex	, 730	, 60	, 80	, 20	, "VlrTxSisComex"		, "Tx.Siscomex"				, ListaCampos.DB_SI	, false );
		adicCampo( txtVlritDespAd	, 813	, 60	, 80	, 20	, "VlrItDespAd"	, "Vlr.Desp.Ad."		, ListaCampos.DB_SI	, false );
		
		adicCampo( txtCodNCM		, 7		, 100	, 90	, 20	, "CodNCM"				, "NCM"						, ListaCampos.DB_SI	, false	);
		
		adicCampoInvisivel( txtCodFisc,		 "CodFisc"				, "CodFisc"				, ListaCampos.DB_FK			, false	);
		adicCampoInvisivel( txtCodItFisc,	 "CodItFisc"			, "CodItFisc"			, ListaCampos.DB_SI			, false	);
		
		adicCampo( txtAliqII				, 100	, 100	, 90	, 20	, "AliqII"				, "% II"					, ListaCampos.DB_SI	, false	);
		adicCampo( txtAliqIPI				, 193	, 100	, 90	, 20	, "AliqIPI"				, "% IPI"					, ListaCampos.DB_SI	, false	);
		adicCampo( txtAliqPIS				, 286	, 100	, 90	, 20	, "AliqPIS"				, "% PIS"					, ListaCampos.DB_SI	, false	);
		adicCampo( txtAliqCOFINS			, 379	, 100	, 90	, 20	, "AliqCOFINS"			, "% COFINS"				, ListaCampos.DB_SI	, false	);
		adicCampo( txtAliqICMSImp			, 472	, 100	, 90	, 20	, "AliqICMSImp"			, "% ICMS Imp."				, ListaCampos.DB_SI	, false	);
		adicCampo( txtAliqICMSUF			, 565	, 100	, 80	, 20	, "AliqICMSUF"			, "% ICMS"					, ListaCampos.DB_SI	, false	);
		adicCampo( txtPercDiferICMS			, 648	, 100	, 80	, 20	, "PercDiferICMS"		, "% Difer."				, ListaCampos.DB_SI	, false	);
		adicCampo( txtVlrBaseICMS   		, 730	, 100	, 80	, 20	, "VlrBaseICMS"			, "Vl.Base ICMS"			, ListaCampos.DB_SI	, false );
		adicCampo( txtPercCredPresImp 		, 814	, 100	, 80	, 20	, "PercCredPresImp"		, "% Cred.Pres."			, ListaCampos.DB_SI	, false );

		adicCampo( txtVlrAD					, 7		, 140	, 90	, 20	, "VlrAD"				, "Vl.Aduan. "	+ codmoeda	, ListaCampos.DB_SI	, false );
		adicCampo( txtVlrII					, 100	, 140	, 90	, 20	, "VlrII"				, "Vl.II"					, ListaCampos.DB_SI	, false );
		adicCampo( txtVlrIPI				, 193	, 140	, 90	, 20	, "VlrIPI"				, "Vl.IPI"					, ListaCampos.DB_SI	, false );
		adicCampo( txtVlrPIS				, 286	, 140	, 90	, 20	, "VlrPIS"				, "Vl.PIS"					, ListaCampos.DB_SI	, false );
		adicCampo( txtVlrCOFINS				, 379	, 140	, 90	, 20	, "VlrCOFINS"			, "Vl.COFINS"				, ListaCampos.DB_SI	, false );
		adicCampo( txtVlrICMS   			, 472	, 140	, 90	, 20	, "VlrICMS"				, "Vl.ICMS"					, ListaCampos.DB_SI	, false );
		adicCampo( txtVlrICMSDiferido  		, 565	, 140	, 80	, 20	, "VlrICMSDiferido"		, "Vl.ICMS Dife."			, ListaCampos.DB_SI	, false );
		adicCampo( txtVlrICMSDevido  		, 648	, 140	, 80	, 20	, "VlrICMSDevido"		, "Vl.ICMS Dev."			, ListaCampos.DB_SI	, false );
		adicCampo( txtVlrICMSCredPresum 	, 731	, 140	, 80	, 20	, "VlrICMSCredPresum"	, "Vl.ICMS Pres."			, ListaCampos.DB_SI	, false );
		adicCampo( txtVlrICMSRecolhimento 	, 814	, 140	, 80	, 20	, "VlrICMSRECOLHIMENTO"	, "Vl.ICMS Reco."				, ListaCampos.DB_SI	, false );
		adicCampo( txtVlrCompl				, 7		, 180	, 90	, 20	, "VlrCompl"			, "Vl.Compl. "			, ListaCampos.DB_SI	, false );
		
		
		txtVlrFreteMI.setSoLeitura( true );
		txtVlrFrete.setSoLeitura( true );
		txtVMLEMI.setSoLeitura( true );
		txtVMLE.setSoLeitura( true );
		txtVMLDMI.setSoLeitura( true );
		txtVMLD.setSoLeitura( true );
		txtVlrTHC.setSoLeitura( true );
		txtVlrTHCMI.setSoLeitura( true );
		txtCodUnid.setEditable( false );
		
		txtAliqII.setEditable( false );
		txtAliqIPI.setEditable( false );
		txtAliqPIS.setEditable( false );
		txtAliqPIS.setEditable( false );
		txtAliqCOFINS.setEditable( false );
		txtAliqICMSImp.setEditable( false );
		txtAliqICMSUF.setEditable( false );
		txtPercCredPresImp.setEditable( false );
		txtPercDiferICMS.setEditable( false );
		
		txtVlrAD.setSoLeitura( true );
		txtVlrII.setSoLeitura( true );
		txtVlrIPI.setSoLeitura( true );
		txtVlrPIS.setEditable( false );
		txtVlrCOFINS.setEditable( false );
		
		txtVlrBaseICMS.setSoLeitura( true );
		txtVlrICMS.setSoLeitura( true );
		txtVlrICMSDiferido.setSoLeitura( true );
		txtVlrICMSDevido.setSoLeitura( true );
		txtVlrICMSCredPresum.setSoLeitura( true );
		txtVlrICMSRecolhimento.setSoLeitura( true );
		
		txtVlrTXSisComex.setSoLeitura( true );
		txtVlritDespAd.setSoLeitura( true );
		txtVlrCompl.setSoLeitura( true );
		// Definindo a tabela do banco de dados
		 
		setListaCampos( true, "ITIMPORTACAO", "CP" );
		lcDet.setQueryInsert( true );
		montaTab();

		/*************************************************
		* 
		* ABA DE AGRUPAMENTO POR NCM (TABELA DE ADIÇÕES)
		* 
		*************************************************/
		
		
		setListaCampos( lcAdicao );
		
		setNavegador( navAdic );
		

		adicCampoInvisivel( txtCodNCMAdic		, "CodNCM"			, "NCM"				, ListaCampos.DB_PK, true );
		adicCampoInvisivel( txtCodAdic			, "CodAdic"			, "Adição"			, ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtVlrTXSisComexAdic, "VlrTxSisComex"	, "Taxa Siscomex"	, ListaCampos.DB_SI, false );

		setListaCampos( false, "IMPORTACAOADIC", "CP" );
		
		lcAdicao.setOrdem( "CODADIC" );

		lcAdicao.setQueryInsert( false );
		lcAdicao.setQueryCommit( false );
		
		lcAdicao.montaTab();
				
		tabAdicao.adicColuna( "Peso Liq." );
		tabAdicao.adicColuna( "Vlr.THC" );
		tabAdicao.adicColuna( "VMLE" );
		tabAdicao.adicColuna( "Vlr.Frete" );
		tabAdicao.adicColuna( "VMCM" );
		tabAdicao.adicColuna( "Vlr.Ad." );
		tabAdicao.adicColuna( "Vlr.II" );
		tabAdicao.adicColuna( "Vlr.IPI" );
		tabAdicao.adicColuna( "Vlr.PIS" );
		tabAdicao.adicColuna( "Vlr.COFINS" );
		tabAdicao.adicColuna( "Vlr.BASEICMS" );
		tabAdicao.adicColuna( "Vlr.ICMS" );
		
		tabAdicao.setTamColuna( 100	, GRID_ADICAO.CODNCM.ordinal()				);
		tabAdicao.setTamColuna( 40	, GRID_ADICAO.CODADIC.ordinal()				);
		tabAdicao.setTamColuna( 120	, GRID_ADICAO.VLRTXSISCOMEXADIC.ordinal()	);
		
		tabAdicao.setTamColuna( 120	, GRID_ADICAO.VLRADUANEIRO.ordinal()		);
		
		tabAdicao.addTabelaEditListener( this );
		tabAdicao.addMouseListener( this );
	}
	
/*	private void excluiAdicoes() {
		
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		
		try {
			
			sql.append("delete from cpimportacaoadic where codemp=? and codfilial=? and codimp=? ");
			
			ps = con.prepareStatement( sql.toString() );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "CPIMPORTACAO" ) );
			ps.setInt( 3, txtCodImp.getVlrInteger() );
			
			ps.execute();
			
			con.commit();
			
		}
		catch (Exception e) {
			Funcoes.mensagemErro( this, "Erro ao excluir adições!", false, e );
			e.printStackTrace();
		}
		
	}*/
	
	private void completaGridNCM() {
		
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			
			sql.append("select ");
			sql.append("coalesce(sum(vlrad),0) vlrad, ");
			sql.append("coalesce(sum(vlrii),0) vlrii,");
			sql.append("coalesce(sum(vlripi),0) vlripi,");
			sql.append("coalesce(sum(vlrpis),0) vlrpis,");
			sql.append("coalesce(sum(vlrcofins),0) vlrcofins,");
			sql.append("coalesce(sum(vlrbaseicms),0) vlrbaseicms,");
			sql.append("coalesce(sum(vlricmsrecolhimento),0) vlricmsrecolhimento, ");
			sql.append("coalesce(sum(vlrfretemi),0) vlrfretemi, ");
			sql.append("coalesce(sum(vmlemi),0) vmlemi, ");
			sql.append("coalesce(sum(pesoliquido),0) pesoliquido, ");
			sql.append("coalesce(sum(vlrthcmi),0) vlrthcmi, ");
			sql.append("coalesce(sum(vlrvmcv),0) vlrvmcv ");
			
			sql.append("from cpitimportacao where codemp=? and codfilial=? and codimp=? and codncm=? ");
			
			ps = con.prepareStatement( sql.toString() );
			
			String ncm = null;
			
			for(int i = 0; tabAdicao.getNumLinhas()> i; i++) {
			
				ncm = tabAdicao.getValor( i, GRID_ADICAO.CODNCM.ordinal() ).toString();
				
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "CPITIMPORTACAO" ) );
				ps.setInt( 3, txtCodImp.getVlrInteger() );
				ps.setString( 4, ncm );
				
				rs = ps.executeQuery();
				
				if(rs.next()) {
					
					tabAdicao.setValor( Funcoes.bdToStrd( rs.getBigDecimal( "vlrad" )), i, GRID_ADICAO.VLRADUANEIRO.ordinal() );
					tabAdicao.setValor( Funcoes.bdToStrd( rs.getBigDecimal( "vlrii" )), i, GRID_ADICAO.VLRII.ordinal() );
					tabAdicao.setValor( Funcoes.bdToStrd( rs.getBigDecimal( "vlripi" )), i, GRID_ADICAO.VLRIPI.ordinal() );
					tabAdicao.setValor( Funcoes.bdToStrd( rs.getBigDecimal( "vlrpis" )), i, GRID_ADICAO.VLRPIS.ordinal() );
					tabAdicao.setValor( Funcoes.bdToStrd( rs.getBigDecimal( "vlrcofins" )), i, GRID_ADICAO.VLRCOFINS.ordinal() );
					tabAdicao.setValor( Funcoes.bdToStrd( rs.getBigDecimal( "vlrbaseicms" )), i, GRID_ADICAO.VLRBASEICMS.ordinal() );
					tabAdicao.setValor( Funcoes.bdToStrd( rs.getBigDecimal( "vlricmsrecolhimento" )), i, GRID_ADICAO.VLRICMSRECOLHER.ordinal() );
					tabAdicao.setValor( Funcoes.bdToStrd( rs.getBigDecimal( "vlrfretemi" )), i, GRID_ADICAO.VLRFRETEMI.ordinal() );					
					tabAdicao.setValor( Funcoes.bdToStrd( rs.getBigDecimal( "vmlemi" )), i, GRID_ADICAO.VMLEMI.ordinal() );
					tabAdicao.setValor( Funcoes.bdToStrd( rs.getBigDecimal( "pesoliquido" )), i, GRID_ADICAO.PESOLIQUIDO.ordinal() );
					tabAdicao.setValor( Funcoes.bdToStrd( rs.getBigDecimal( "vlrthcmi" )), i, GRID_ADICAO.VLRTHCMI.ordinal() );
					tabAdicao.setValor( Funcoes.bdToStrd( rs.getBigDecimal( "vlrvmcv" )), i, GRID_ADICAO.VMCV.ordinal() );
					
				}
				
			}
			
			con.commit();
			
		}
		catch (Exception e) {
			Funcoes.mensagemErro( this, "Erro ao completar grid de adicoes!", false, e );
			e.printStackTrace();
		}
	}
	
	private void geraAdicoes() {
		
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		int codadic = 1;
		
		try {
			
			if(tabAdicao.getNumLinhas()>0) {
			
				if(Funcoes.mensagemConfirma( this, "Já existem adições geradas para esse processo.\nGostaria de excluí-las e gerar novamente?" )==JOptionPane.YES_OPTION) {
					daoimp.excluiAdicoes( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "CPIMPORTACAO" ), txtCodImp.getVlrInteger() );
				}
				else {
					return;
				}
				
			}
			/*
			sql.append( "select codncm, sum(vlrad) vlrad from cpitimportacao where codemp=? and codfilial=? and codimp=? group by codncm " );
				
			ps = con.prepareStatement( sql.toString() );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "CPIMPORTACAO" ) );
			ps.setInt( 3, txtCodImp.getVlrInteger() );
			
			rs = ps.executeQuery();
			*/
			rs = daoimp.buscaAdicao( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "CPIMPORTACAO" ), txtCodImp.getVlrInteger() );
			
			sql.append( "insert into cpimportacaoadic (codemp, codfilial, codimp, codncm, codadic, vlrtxsiscomex) values (?, ?, ?, ?, ?, ?) " );
			
			while (rs.next()) {
				
				String ncm = rs.getString( "codncm" );
				BigDecimal vlradadic = rs.getBigDecimal( "vlrad" );
				
				ps = con.prepareStatement( sql.toString() );
				
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "CPIMPORTACAO" ) );
				ps.setInt( 3, txtCodImp.getVlrInteger() );
				ps.setString( 4, ncm );
				ps.setInt( 5, codadic );
				ps.setBigDecimal( 6, new BigDecimal(0) );
				
				ps.execute();

//				tabAdicao.setValor( Funcoes.bdToStrd( rs.getBigDecimal( "vlrad" )), i, GRID_ADICAO.VLRADUANEIRO.ordinal() );
				
				codadic++;
				
			}
			
			lcCampos.carregaDados();
			
			con.commit();
			
			
		}
		catch (Exception e) {
			Funcoes.mensagemErro( this, "Erro ao gerar adições!", false, e );
			e.printStackTrace();
		}
	}
	
	/*private void rateioSiscomex() {
		
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			

			sql.append( "update cpitimportacao set ");

			// Rateando o Frete pelo valor aduaneiro
			sql.append( "vlrtxsiscomex=( ( ? * (vlrad) ) / ? )" );				
			sql.append( "where codemp=? and codfilial=? and codimp=? and codncm=?" );

			ps = con.prepareStatement( sql.toString() );
			
			for(int i = 0; tabAdicao.getNumLinhas()>i; i++) {
			
				BigDecimal siscomex = ConversionFunctions.stringCurrencyToBigDecimal( tabAdicao.getValor( i, GRID_ADICAO.VLRTXSISCOMEXADIC.ordinal()).toString() );
				BigDecimal vlradadic = ConversionFunctions.stringCurrencyToBigDecimal( tabAdicao.getValor( i, GRID_ADICAO.VLRADUANEIRO.ordinal()).toString() );
				String ncm = tabAdicao.getValor( i, GRID_ADICAO.CODNCM.ordinal()).toString() ;
				
				ps.setBigDecimal( 1, siscomex );
				ps.setBigDecimal( 2, vlradadic );
	
				ps.setInt( 3, Aplicativo.iCodEmp );
				ps.setInt( 4, ListaCampos.getMasterFilial( "CPIMPORTACAO" ) );
				ps.setInt( 5, txtCodImp.getVlrInteger() );
				ps.setString( 6, ncm );
				
				ps.execute();
			
			}
			
			con.commit();
			
			Funcoes.mensagemInforma( this, "Valores rateados entre os ítens!" );
			lcCampos.carregaDados();
			
			
		}
		catch (Exception e) {
			Funcoes.mensagemErro( this, "Erro ao realizar o rateio do frete.", false, e );
			e.printStackTrace();
		}
	}
	*/
	private void atualizaAdicao(Integer linha) {
		
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			sql.append( "update cpimportacaoadic set ");
			sql.append( "codadic=? , vlrtxsiscomex=? " );				
			sql.append( "where codemp=? and codfilial=? and codimp=? and codncm=? " );

			ps = con.prepareStatement( sql.toString() );
			
			ps.setInt( 1, Integer.parseInt( tabAdicao.getValor( linha, GRID_ADICAO.CODADIC.ordinal() ).toString() ));
			ps.setBigDecimal( 2, ConversionFunctions.stringCurrencyToBigDecimal( tabAdicao.getValor( linha, GRID_ADICAO.VLRTXSISCOMEXADIC.ordinal()).toString() )); 

			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "CPIMPORTACAO" ) );
			ps.setInt( 5, txtCodImp.getVlrInteger() );
			ps.setString( 6, tabAdicao.getValor( linha, GRID_ADICAO.CODNCM.ordinal()).toString() );
			
			ps.execute();
			
			con.commit();
			
		}
		catch (Exception e) {
			Funcoes.mensagemErro( this, "Erro ao atualizar tabela de adições", false, e );
			e.printStackTrace();
		}
	}
	/*
	private void execRateio() {
		
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			sql.append( "update cpitimportacao set ");

			// Rateando o Frete pelo peso bruto
			
			sql.append( "vlrfretemi=( ( ? * (pesoliquido) ) / ? )," );

			// Rateando o THC pelo valor + frete
			sql.append( "vlrthcmi=( ( ? * (vmldmi) ) / ? ) " );
			
			sql.append( "where codemp=? and codfilial=? and codimp=?" );

			BigDecimal pesobrutoitem = null;
			
			ps = con.prepareStatement( sql.toString() );
			
			ps.setBigDecimal( 1, txtVlrFreteMITOT.getVlrBigDecimal() );
			
			//Condição para evitar divisão por 0
			if(txtPesoLiquidoTot.getVlrBigDecimal().compareTo( new BigDecimal(0)) > 0 ){
				ps.setBigDecimal( 2, txtPesoLiquidoTot.getVlrBigDecimal() );
			} else {
				if(txtPesoLiquido.getVlrBigDecimal().compareTo( new BigDecimal(0)) > 0 ){
					ps.setBigDecimal( 2, txtPesoLiquido.getVlrBigDecimal() );
				} else {
					ps.setBigDecimal( 2, new BigDecimal(1) );	
				}
			}
			
			
			ps.setBigDecimal( 3, txtVlrTHCMITOT.getVlrBigDecimal() );
			
			//Condição para evitar divisão por 0
			if(txtVMLDMITOT.getVlrBigDecimal().compareTo( new BigDecimal(0)) > 0 ){
				ps.setBigDecimal( 4, txtVMLDMITOT.getVlrBigDecimal() );
			} else {
				ps.setBigDecimal( 4, new BigDecimal(1) );
			}
			
			ps.setInt( 5, Aplicativo.iCodEmp );
			ps.setInt( 6, ListaCampos.getMasterFilial( "CPIMPORTACAO" ) );
			ps.setInt( 7, txtCodImp.getVlrInteger() );
			
			ps.execute();
			
			if( txtVlrDespAd.getVlrBigDecimal().compareTo( new BigDecimal( 0 ) ) > 0 ){
				execRateioDespAD();
			} else {
				daoimp.zeraVlrDesp(txtCodImp.getVlrInteger());	
			}
				
			con.commit();
			
			lcDet.carregaItens();
			lcCampos.carregaDados();
		}
		catch (Exception e) {
			Funcoes.mensagemErro( this, "Erro ao realizar o rateio do frete.", false, e );
			e.printStackTrace();
		}
	}*/
	
	/*private void zeraVlrDesp(){
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		try {
			sql.append( "update cpitimportacao it set it.VLRITDESPAD = 0 ");
			sql.append(" where it.codemp=? and it.CODFILIAL=? and it.codimp=? ");
			
			ps = con.prepareStatement( sql.toString() );
			int param = 1;
		
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "CPIMPORTACAO" ) );
			ps.setInt( param++, txtCodImp.getVlrInteger() );
			ps.execute();
		} catch (SQLException e) {
			Funcoes.mensagemErro( this, "Erro ao atualizar valor total das despesas da compra de importacao!\n" + e.getMessage(), true, con, e );
		} finally {
			ps = null;
		}
	}*/
	
	/*private void execRateioDespAD() throws SQLException {
		BigDecimal vlrTotDesp = BigDecimal.ZERO;
		BigDecimal diferenca = BigDecimal.ZERO;	

		//atualizaDespAd();
		daoimp.atualizaDespAd( txtCodImp.getVlrInteger(), txtVLRADMITOT.getVlrBigDecimal(), 
				txtVlrFreteMITOT.getVlrBigDecimal(), txtVlrTHCMITOT.getVlrBigDecimal(), txtVlrDespAd.getVlrBigDecimal() );
		
		
		vlrTotDesp = daoimp.getTotalDespAd( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "CPIMPORTACAO" ), txtCodImp.getVlrInteger()  );
		
		diferenca = txtVlrDespAd.getVlrBigDecimal().subtract( vlrTotDesp );
		
		if( (diferenca.compareTo( BigDecimal.ZERO ) > 0) || (diferenca.compareTo( BigDecimal.ZERO )< 0) ) {
			daoimp.atualizaDiferenca( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "CPIMPORTACAO" ), txtCodImp.getVlrInteger(), diferenca );
		}
		
	}*/
	
/*	public void atualizaDespAd() throws SQLException{
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		try {
			sql.append( "update cpitimportacao it set it.VLRITDESPAD = ");
	
			//Rateando desp.Aduaneiras pelo valor do produto + frete + thc
			sql.append("(((it.vlradmi + it.vlrfretemi + it.vlrthcmi ) / (? + ? + ? )) * ?)" );
			
			sql.append(" where it.codemp=? and it.CODFILIAL=? and it.codimp=? ");
			
			ps = con.prepareStatement( sql.toString() );
			int param = 1;
		
			//Totalizadores utilizado na contato
			ps.setBigDecimal( param++, txtVLRADMITOT.getVlrBigDecimal() );
			ps.setBigDecimal( param++, txtVlrFreteMITOT.getVlrBigDecimal() );
			ps.setBigDecimal( param++, txtVlrTHCMITOT.getVlrBigDecimal() );
			
			//Valor que será reteado
			ps.setBigDecimal( param++, txtVlrDespAd.getVlrBigDecimal() );
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "CPIMPORTACAO" ) );
			ps.setInt( param++, txtCodImp.getVlrInteger() );
			ps.execute();
		} catch (SQLException e) {
			Funcoes.mensagemErro( this, "Erro ao ratear despesas da compra de importacao!\n" + e.getMessage(), true, con, e );
		} finally {
			ps = null;
		}
	}
	*/
/*	public BigDecimal getTotalDespAd(Integer codemp, Integer codfilial, Integer codimp) {
		
		BigDecimal vlrTotDesp = BigDecimal.ZERO;
		PreparedStatement ps = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append( "select  SUM(it.vlritdespad) vlrtotdespad from cpitimportacao it where it.codemp=? and it.codfilial=? and it.codimp=? ");
		
		try{
			ps = con.prepareStatement( sql.toString() );
			int param = 1;
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codimp );
			
			rs = ps.executeQuery();
			
			if(rs.next()){
				vlrTotDesp = rs.getBigDecimal( "vlrtotdespad" );
			}
			rs.close();
			ps.close();
		
		} catch (SQLException e) {
			Funcoes.mensagemErro( this, "Erro ao buscar valor total das despesas da compra de importacao!\n" + e.getMessage(), true, con, e );
		} finally {
			rs = null;
			ps = null;
		}
	
		return vlrTotDesp;
	
	}*/
	
	
/*	public void atualizaDiferenca(Integer codemp, Integer codfilial, Integer codimp, BigDecimal diferenca){
		
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		sql.append( "update cpitimportacao it set it.VLRITDESPAD = it.VLRITDESPAD + ? where it.codemp=? and it.CODFILIAL=? and it.codimp=? and ");
		sql.append( " it.coditimp=( select first 1 itm.coditimp from cpitimportacao itm where itm.codemp=it.codemp and itm.CODFILIAL=it.codfilial and itm.codimp=it.codimp order by itm.vlritdespad desc ) ");
		
		try{
			ps = con.prepareStatement( sql.toString() );
			int param = 1;
			ps.setBigDecimal( param++, diferenca );
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codimp );
			ps.execute();

		} catch (SQLException e) {
			Funcoes.mensagemErro( this, "Erro ao atualizar diferança no maior termo da compra de importacao!\n" + e.getMessage(), true, con, e );
		} finally {
			ps = null;
		}		
	}*/

	private void buscaClassificacaoFiscal() {
		
		txtCodItFisc.setVlrInteger( daoimp.buscaClassificacaoFiscal( txtCodFisc.getVlrString(), txtCodPais.getVlrInteger() ) );
		
		lcItClFiscal.carregaDados();
		carregaAliquotas();
		
		/* StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			sql.append( "select coditfisc from lfitclfiscal " );
			sql.append( "where " );
			sql.append( "codemp=? and codfilial=? and codfisc=? and tipousoitfisc='CP' and codpais=?" );

			ps = con.prepareStatement( sql.toString() );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "LFITCLFISCAL" ) );
			ps.setString( 3, txtCodFisc.getVlrString() );
			ps.setInt( 4, txtCodPais.getVlrInteger() );
			
			rs = ps.executeQuery();
			
			if( rs.next() ) {
				txtCodItFisc.setVlrInteger( rs.getInt( "coditfisc" ) );				
			}
			
			lcItClFiscal.carregaDados();
			
			carregaAliquotas();
			
		}
		catch (Exception e) {
			Funcoes.mensagemErro( this, "Erro ao realizar o rateio do frete.", false, e );
			e.printStackTrace();
		} */
		
	}

	
	public void beforeInsert( InsertEvent e ) {

	}

	public void afterInsert( InsertEvent e ) {

		if ( e.getListaCampos() == lcCampos ) {
			txtTipoImp.setVlrString( "O" );
		}
		else if ( e.getListaCampos() == lcDet ) {

		}
	}

	public void beforePost( PostEvent e ) {

		if ( e.getListaCampos() == lcDet ) {
			
			calcPISCOFINS();

		}
	}
	
	private void calcPISCOFINS() {
		
		try {
			//(1+(I16*(M16+(J16*(1+M16)))))/((1-K16-L16)*(1-I16))
			
			float A		=	txtPercDiferICMS.getVlrBigDecimal().floatValue();	
			
			float I		=	0.00f;
			
			if( A > 0 ) {
			
				I 	=	txtAliqICMSImp.getVlrBigDecimal().floatValue() / 100f ; //I16
			
			}
			else {
				
				I	=	txtAliqICMSUF.getVlrBigDecimal().floatValue() / 100f ; //I16
				
			}
			
			float M 		= 	txtAliqII.getVlrBigDecimal().floatValue() / 100f;//M16;
			float K 		= 	txtAliqPIS.getVlrBigDecimal().floatValue() / 100f; //K16
			float L 		= 	txtAliqCOFINS.getVlrBigDecimal().floatValue() / 100f; //L16
			float J 		= 	txtAliqIPI.getVlrBigDecimal().floatValue() / 100f; //J16
			float N			=	txtVlrAD.getVlrBigDecimal().floatValue();
			
			float Q 		=	(1f+(I*(M+(J*(1+M)))))/((1-K-L)*(1-I));
			
			float pis			=	K * ( N *  Q );
			float cofins		=	L * ( N *  Q );

			txtVlrPIS.setVlrBigDecimal( new BigDecimal(pis) );
			txtVlrCOFINS.setVlrBigDecimal( new BigDecimal(cofins) );
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void afterPost( PostEvent e ) {

		if ( e.getListaCampos() == lcDet ) {
			
			

		}
	}

	public void beforeDelete( DeleteEvent e ) {

		if ( e.getListaCampos() == lcDet ) {
		}
	}

	public void afterDelete( DeleteEvent e ) {

		if ( e.getListaCampos() == lcDet ) {

		}
	}

	public void beforeCarrega( CarregaEvent e ) {
		
		if(e.getListaCampos() == lcAdicao) {
			tabAdicao.setColunaEditavel( GRID_ADICAO.CODADIC.ordinal(), false );
			tabAdicao.setColunaEditavel( GRID_ADICAO.VLRTXSISCOMEXADIC.ordinal(), false );
		}

	}

	public void afterCarrega( CarregaEvent e ) {

		if ( e.getListaCampos() == lcClFiscal && ( lcDet.getStatus() == ListaCampos.LCS_EDIT || lcDet.getStatus() == ListaCampos.LCS_INSERT ) ) {
			buscaClassificacaoFiscal();
		}
		else if ( e.getListaCampos() == lcAdicao ) {
			completaGridNCM();
		}
		
	}
	
	private void carregaAliquotas() {
		
		txtAliqII.setVlrBigDecimal( txtAliqIIFisc.getVlrBigDecimal() );
		txtAliqIPI.setVlrBigDecimal( txtAliqIPIFisc.getVlrBigDecimal() );
		txtAliqPIS.setVlrBigDecimal( txtAliqPISFisc.getVlrBigDecimal() );
		txtAliqCOFINS.setVlrBigDecimal( txtAliqCOFINSFisc.getVlrBigDecimal() );
		txtAliqICMSImp.setVlrBigDecimal( txtAliqICMSImpFisc.getVlrBigDecimal() );
		txtAliqICMSUF.setVlrBigDecimal( txtAliqICMSUFFisc.getVlrBigDecimal() );
		txtPercDiferICMS.setVlrBigDecimal( txtRedFisc.getVlrBigDecimal() );
		
		
	}
	
	public void execRateio(){
		try {
			daoimp.execRateio( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "CPITIPORTACAO" ), txtCodImp.getVlrInteger(), txtVlrFreteMITOT.getVlrBigDecimal(),
					txtVMLDMITOT.getVlrBigDecimal(), txtPesoLiquido.getVlrBigDecimal(), txtPesoLiquidoTot.getVlrBigDecimal(), txtVlrDespAd.getVlrBigDecimal(), 
						txtVLRADTOT.getVlrBigDecimal(), txtVlrTHCMITOT.getVlrBigDecimal() );
			
			lcDet.carregaItens();
			lcCampos.carregaDados();
			
		} catch (Exception e) {
			Funcoes.mensagemErro( null, "Erro ao realizar o rateio do frete.", false, e );
			e.printStackTrace();
		}
		
	}
	
	public void actionPerformed( ActionEvent e ) {

		if ( e.getSource() == btRateioFrete ) {
			execRateio();
		}
		else if ( e.getSource() == btGerarAdicoes ) {
			geraAdicoes();
		} 
		else if ( e.getSource() == btRateioSiscomex ) {
			//rateioSiscomex();
			daoimp.rateioSiscomex( tabAdicao.getDataVector(), txtCodImp.getVlrInteger() );
			lcCampos.carregaDados();
		}

		super.actionPerformed( e );
	}

	public void stateChanged( ChangeEvent e ) {

		if ( e.getSource() == tpnGrid ) {
		}
	}

	public void setConexao( DbConnection con ) {

		super.setConexao( con );
	
		lcForneced.setConexao( con );
		lcProduto.setConexao( con );
		lcUnid.setConexao( con );
		lcPlanoPag.setConexao( con );
		lcMoeda.setConexao( con );
		lcClFiscal.setConexao( con );
		lcItClFiscal.setConexao( con );
		lcAdicao.setConexao( con );
		
		
		daoimp = new DAOImportacao( con );
		
	}

	public void valorAlterado( TabelaEditEvent evt ) {

		if(evt.getTabela() == tabAdicao 
				&& tabAdicao.getSelectedRow() > -1
				&&
				( tabAdicao.getColunaEditada()==GRID_ADICAO.CODADIC.ordinal() || tabAdicao.getColunaEditada()==GRID_ADICAO.VLRTXSISCOMEXADIC.ordinal() )
				&& tabAdicao.isCellEditable( tabAdicao.getLinhaEditada(), tabAdicao.getColunaEditada() )
				
		){
			
			atualizaAdicao( tabAdicao.getLinhaEditada() );
			
		}
		
	}

	public void mouseClicked( MouseEvent mevt ) {

		if ( mevt.getSource() == tabAdicao && mevt.getClickCount() == 2 ) {
			
			tabAdicao.setColunaEditavel( GRID_ADICAO.CODADIC.ordinal(), true );
			tabAdicao.setColunaEditavel( GRID_ADICAO.VLRTXSISCOMEXADIC.ordinal(), true );
			
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
}
