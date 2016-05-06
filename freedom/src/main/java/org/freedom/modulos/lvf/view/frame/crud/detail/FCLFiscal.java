/**
 * @version 05/03/2009 <BR>
 * @author Setpoint Informática Ltda / Anderson Sanchez.
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FVendaConsig.java <BR>
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
 *                       Cadastro de classificações fiscais e suas exceções <BR>
 * 
 */

package org.freedom.modulos.lvf.view.frame.crud.detail;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.EditEvent;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.JComboBoxEvent;
import org.freedom.acao.JComboBoxListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.acao.RadioGroupEvent;
import org.freedom.acao.RadioGroupListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.infra.x.swing.JMyComboBoxRenderer;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDetalhe;
import org.freedom.modulos.cfg.view.frame.crud.plain.FPais;
import org.freedom.modulos.cfg.view.frame.crud.plain.FUF;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FTipoMov;
import org.freedom.modulos.lvf.view.dialog.utility.DLCopiaClassificacao;
import org.freedom.modulos.lvf.view.frame.crud.plain.FServico;
import org.freedom.modulos.lvf.view.frame.crud.plain.FSitTrib;
import org.freedom.modulos.lvf.view.frame.crud.plain.FTratTrib;
import org.freedom.modulos.std.view.frame.crud.detail.FCalcCusto;
import org.freedom.modulos.std.view.frame.crud.plain.FMensagem;
import org.freedom.modulos.std.view.frame.crud.plain.FTipoFisc;
import org.freedom.modulos.std.view.frame.report.FRegraFiscal;

public class FCLFiscal extends FDetalhe implements MouseListener, ChangeListener, CarregaListener, InsertListener, RadioGroupListener, PostListener, JComboBoxListener {

	private static final long serialVersionUID = 1L;

	private JTabbedPanePad tpnPrincipal = new JTabbedPanePad();

	private JTabbedPanePad tpnGeral = new JTabbedPanePad();

	private JPanelPad panelGeral = new JPanelPad( 500, 80 );

	private JPanelPad panelServico = new JPanelPad( 500, 60 );

	private JPanelPad pnServico = new JPanelPad( new BorderLayout() );

	private JPanelPad panelVariantesCampos = new JPanelPad( 500, 80 );

	private JPanelPad panelVariantes = new JPanelPad( new GridLayout( 1, 1 ) );

	private JPanelPad panelICMS = new JPanelPad( new GridLayout( 1, 1 ) );

	private JPanelPad panelICMSCampos = new JPanelPad( 500, 80 );

	private JPanelPad panelIPI = new JPanelPad( new GridLayout( 1, 1 ) );

	private JPanelPad panelIPICampos = new JPanelPad( 500, 80 );

	private JPanelPad panelPIS = new JPanelPad( new GridLayout( 1, 1 ) );

	private JPanelPad panelPISCampos = new JPanelPad( 500, 80 );

	private JPanelPad panelCOFINS = new JPanelPad( new GridLayout( 1, 1 ) );

	private JPanelPad panelCOFINSCampos = new JPanelPad( 500, 80 );

	private JPanelPad panelFUNRURAL = new JPanelPad( new GridLayout( 1, 1 ) );

	private JPanelPad panelFUNRURALCampos = new JPanelPad( 500, 80 );

	private JPanelPad panelIR = new JPanelPad( new GridLayout( 1, 1 ) );

	private JPanelPad panelIRCampos = new JPanelPad( 500, 80 );

	private JPanelPad panelCSocial = new JPanelPad( new GridLayout( 1, 1 ) );
	
	private JPanelPad pnCopiaClass = new JPanelPad(JPanelPad.TP_JPANEL, new GridBagLayout());

	private JPanelPad panelCSocialCampos = new JPanelPad( 500, 80 );

	private JPanelPad panelII = new JPanelPad( new GridLayout( 1, 1 ) );
	
	private JPanelPad panelCSOSN = new JPanelPad( new GridLayout( 1, 1 ) );
	
	private JPanelPad panelSimples = new JPanelPad( new GridLayout( 1, 1 ) );

	private JPanelPad panelIICampos = new JPanelPad( 500, 80 );
	
	private JPanelPad panelCSOSNCampos = new JPanelPad( 500, 80 );
	
	private JPanelPad panelIISimples = new JPanelPad( 500, 80 );
	
	private JPanelPad panelISS = new JPanelPad( new GridLayout( 1, 1 ) );

	private JPanelPad panelISSCampos = new JPanelPad( 500, 80 );

	private JPanelPad panelNomeComum = new JPanelPad( new BorderLayout() );

	private JPanelPad panelNomeComumNCM = new JPanelPad( new BorderLayout() );

	private JPanelPad panelNomeComumNCMCampos = new JPanelPad( 500, 100 );

	private JSplitPane panelNomeComumNCMDescricoes = new JSplitPane( JSplitPane.VERTICAL_SPLIT );

	private JPanelPad panelNomeComumNBM = new JPanelPad( 500, 60 );

	private JTextFieldPad txtCodFisc = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtDescFisc = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodRegra = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldFK txtDescRegra = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodRegraIt = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldFK txtDescRegraIt = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );
	
	private JTextFieldPad txtCodCalc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescCalc = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCodTratTrib = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtDescTratTrib = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCodMens = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescMens = new JTextFieldFK( JTextFieldPad.TP_STRING, 10000, 0 );

	private JTextFieldPad txtCodNBM = new JTextFieldPad( JTextFieldPad.TP_STRING, 12, 0 );

	private JTextFieldFK txtDescNBM = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCodNCM = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldFK txtDescNCM = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtExTIPI = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtCodServ = new JTextFieldPad( JTextFieldPad.TP_STRING, 5, 0 );

	private JTextAreaPad txaDescServ = new JTextAreaPad( 500 );

	private JTextAreaPad txaDescExTIPI = new JTextAreaPad( 1000 );

	private JScrollPane spDescExTIPI = new JScrollPane( txaDescExTIPI );

	private JTextAreaPad txaDescNCM = new JTextAreaPad( 2000 );

	private JScrollPane spDescNCM = new JScrollPane( txaDescNCM );

	private JTextFieldFK txtCodItClFiscal = new JTextFieldFK( JTextFieldFK.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtCodTipoMov = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 9, 2 );

	private JTextFieldFK txtDescTipoMov = new JTextFieldFK( JTextFieldPad.TP_STRING, 200, 0 );

	private JTextFieldPad txtCodTipoFisc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescFiscCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtRedFisc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 9, 2 );

	private JTextFieldPad txtAliqFisc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 9, 2 );

	private JTextFieldPad txtAliqLFisc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, 2 );

	private JTextFieldPad txtAliqFiscIntra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 9, 2 );
	
	private JTextFieldPad txtAliqICMSImp = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 9, 2 );

	private JTextFieldPad txtCodSitTribIPI = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtImpSitTribIPI = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtDescSitTribIPI = new JTextFieldFK( JTextFieldPad.TP_STRING, 200, 0 );

	private JTextFieldPad txtAliqIPIFisc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 9, 2 );

	private JTextFieldPad txtAliqIIFisc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, 2 );

	private JTextFieldPad txtVlrIpiUnidTrib = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 9, 2 );

	private JTextFieldPad txtCodSitTribPIS = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtImpSitTribPIS = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtDescSitTribPIS = new JTextFieldFK( JTextFieldPad.TP_STRING, 200, 0 );

	private JTextFieldPad txtAliqPisFisc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, 2 );

	private JTextFieldPad txtVlrPisUnidTrib = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 9, 2 );

	private JTextFieldPad txtCodSitTribCOF = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtImpSitTribCOF = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtDescSitTribCOF = new JTextFieldFK( JTextFieldPad.TP_STRING, 200, 0 );

	private JTextFieldPad txtAliqCofinsFisc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, 2 );

	private JTextFieldPad txtAliqFunRuralFisc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, 2 );

	private JTextFieldPad txtVlrCofUnidTrib = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 9, 2 );

	private JTextFieldPad txtCodSitTribISS = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtImpSitTribISS = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtDescSitTribISS = new JTextFieldFK( JTextFieldPad.TP_STRING, 200, 0 );

	private JTextFieldPad txtVlrIssUnidTrib = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 9, 2 );

	private JTextFieldPad txtCodSitTribIR = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtImpSitTribIR = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtDescSitTribIR = new JTextFieldFK( JTextFieldPad.TP_STRING, 200, 0 );

	private JTextFieldPad txtAliqIrFisc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, 2 );

	private JTextFieldPad txtVlrIrUnidTrib = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 9, 2 );

	private JTextFieldPad txtCodSitTribCS = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtImpSitTribCS = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtDescSitTribCs = new JTextFieldFK( JTextFieldPad.TP_STRING, 200, 0 );

	private JTextFieldPad txtAliqCSocialFisc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, 2 );

	private JTextFieldPad txtVlrCsUnidTrib = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 9, 2 );

	private JTextFieldPad txtCodSitTribII = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	//private JTextFieldPad txtImpSitTribII = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtDescSitTribII = new JTextFieldFK( JTextFieldPad.TP_STRING, 200, 0 );

	private JTextFieldPad txtAliqIiFisc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, 2 );
	
	private JTextFieldPad txtPercCredPresImp = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, 2 );
	
	private JTextFieldPad txtAliqISSFisc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, 2 );

	private JTextFieldPad txtVlrIiUnidTrib = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 9, 2 );

	private JCheckBoxPad cbGeralFisc = new JCheckBoxPad( "Regra geral?", "S", "N" );

	private JTextFieldPad txtCodPais = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescPais = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtSiglaUF = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtNomeUF = new JTextFieldFK( JTextFieldPad.TP_STRING, 80, 0 );

	private JTextFieldPad txtMargemVlAgr = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, 2 );
	
	private JTextFieldPad txtAdicICMSTotNotaPrefere = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );
	
	private JTextFieldPad txtAliqCargaMedia = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 9, 2 );
	
	private JTextFieldPad txtOperacaoPIS = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );
	
	private JTextFieldPad txtOperacaoCOF = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );
	
	private JTextFieldPad txtOperacao = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );
	
	private JTextFieldPad txtCstNatOp = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );
	
	private JTextFieldPad txtCodNatOp = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );
	
	private JTextFieldFK txtDescNatOp = new JTextFieldFK( JTextFieldPad.TP_STRING, 500, 0 );
	
	private JComboBoxPad cbOrig = null;

	private JComboBoxPad cbTpCalcIPI = null;

	private JComboBoxPad cbModBCICMS = null;

	private JComboBoxPad cbModBCICMSST = null;

	private JCheckBoxPad cbRedBaseST = new JCheckBoxPad( "Red. base Subst.", "S", "N" );

	private JCheckBoxPad cbRedBaseFrete = new JCheckBoxPad( "Red. base Frete", "S", "N" );

	private JRadioGroup<String, String> rgNoUF = null;

	private JRadioGroup<String, String> rgTipo = null;

	private JRadioGroup<String, String> rgTipoFisc = null;

	private JRadioGroup<String, String> rgTipoST = null;

	private JRadioGroup<String, String> rgTpRedIcmsFisc = null;

	private JRadioGroup<String, String> rgIndApurIPI = null;

	private JButtonPad btCopiarVariante = new JButtonPad( "Copiar", Icone.novo( "btCopiar.png" ) );

	//private JButtonPad btStatusLc = new JButtonPad( "Status LC", Icone.novo( "btCopiar.png" ) );
	
	private JButtonPad btCopiar = new JButtonPad( Icone.novo( "btCopiarModel.png" ) );

	private ListaCampos lcRegraFiscal = new ListaCampos( this, "RA" );

	private ListaCampos lcRegraFiscalIt = new ListaCampos( this, "RA" );
	
	private ListaCampos lcCalcCusto = new ListaCampos( this, "CC" );

	private ListaCampos lcTratTrib = new ListaCampos( this, "TT" );

	private ListaCampos lcMens = new ListaCampos( this, "ME" );
	
	private ListaCampos lcCSOSN = new ListaCampos( this, "CN" );

	private ListaCampos lcTipoMov = new ListaCampos( this, "TM" );

	private ListaCampos lcTipoFiscCli = new ListaCampos( this, "FC" );

	private ListaCampos lcSitTribIPI = new ListaCampos( this, "SI" );
	
	private ListaCampos lcSitTribISS = new ListaCampos( this, "IS" );

	private ListaCampos lcSitTribPIS = new ListaCampos( this, "SP" );

	private ListaCampos lcSitTribCOF = new ListaCampos( this, "SC" );
	
	private ListaCampos lcServico = new ListaCampos( this );

	private ListaCampos lcNCM = new ListaCampos( this );

	private ListaCampos lcNBM = new ListaCampos( this );

	private ListaCampos lcUF = new ListaCampos( this );
	
	private ListaCampos lcPais = new ListaCampos( this );
	
	private ListaCampos lcSpNatOP = new ListaCampos( this );
	
	private JCheckBoxPad cbRetensaoISS = new JCheckBoxPad( "Recolhimento de ISS pelo cliente?", "S", "N" );
	
	private JCheckBoxPad cbAdicIPIBaseICMS = new JCheckBoxPad( "Adicionar IPI a base de ICMS?", "S", "N" );
	
	private JCheckBoxPad cbAdicICMSTotNota = new JCheckBoxPad( "Adicionar ICMS ao total da Nota.", "S", "N" );
	
	private JCheckBoxPad cbCalcSTCM = new JCheckBoxPad( "Cálculo de ST por carga média. ", "S", "N" );
	
	private JTextFieldPad txtCSOSN = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldFK txtDescCSOSN = new JTextFieldFK( JTextFieldPad.TP_STRING, 200, 0 );
	
	private DLCopiaClassificacao dlCopiar = null;
	
	

	public FCLFiscal() {
		super( false );
		nav.setNavigation( true );
		setTitulo( "Classificações Fiscais" );
		setAtribos( 50, 50, 765, 600 );
		
		montaTela();
		montaDetalhe();

	}

	private void adicListeners() {

		// Adicionando valores padrão

		rgTipoFisc.setVlrString( "TT" );
		rgTipoST.setVlrString( "SI" );
		rgTpRedIcmsFisc.setVlrString( "B" );
		rgIndApurIPI.setVlrString( "0" );
		
		/*
		rgTipoST.setAtivo( false );
		txtMargemVlAgr.setAtivo( false );
		cbModBCICMSST.setAtivo( false );
		rgTpRedIcmsFisc.setAtivo( false );
		txtRedFisc.setAtivo( false );
		rgTipoFisc.setAtivo( false );
		*/
		
		txaDescServ.setAtivo( false );
		txtVlrIpiUnidTrib.setAtivo( false );
		txtAliqIPIFisc.setAtivo( false );
		txtVlrPisUnidTrib.setAtivo( false );
		txtAliqPisFisc.setAtivo( false );
		txtVlrCofUnidTrib.setAtivo( false );
		txtAliqCofinsFisc.setAtivo( false );

		txtVlrIssUnidTrib.setAtivo( false );
		//txtAliqISSFisc.setAtivo( false );
		txtVlrIrUnidTrib.setAtivo( false );
		txtAliqIrFisc.setAtivo( true );
		txtVlrCsUnidTrib.setAtivo( false );
		txtAliqCSocialFisc.setAtivo( true );
		txtVlrIiUnidTrib.setAtivo( false );
		// txtAliqIiFisc.setAtivo( false );

		// Adicionando Listeners

		lcCampos.addCarregaListener( this );
		lcCampos.addInsertListener( this );
		lcCampos.addPostListener( this );
		lcTratTrib.addCarregaListener( this );
		lcSitTribPIS.addCarregaListener( this );
		lcSitTribCOF.addCarregaListener( this );

		lcDet.addInsertListener( this );
		lcDet.addCarregaListener( this );

		btCopiarVariante.addActionListener( this );
		btCopiar.addActionListener( this );
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		
		//btStatusLc.addActionListener( this );
		
		/*txtCodSitTribIPI.addEditListener( this );
		txtCodSitTribCOF.addEditListener( this );
		txtCodSitTribIR.addEditListener( this );
*/
		rgTipoFisc.addRadioGroupListener( this );
		rgTipoST.addRadioGroupListener( this );

		tpnGeral.addChangeListener( this );
		tpnPrincipal.addChangeListener( this );
		
	}

	private void montaCombos() {

		/*		Vector<String> vLabsOrig = new Vector<String>();
		Vector<String> vValsOrig = new Vector<String>();
		vLabsOrig.addElement( "<--Selecione-->" );
		vLabsOrig.addElement( "Nacional" );
		vLabsOrig.addElement( "Estrangeira - Importação direta" );
		vLabsOrig.addElement( "Estrangeira - Adquirida no mercado interno" );
		vValsOrig.addElement( "" );
		vValsOrig.addElement( "0" );
		vValsOrig.addElement( "1" );
		vValsOrig.addElement( "2" );*/

		Vector<String> vLabsOrig = new Vector<String>();
		Vector<String> vValsOrig = new Vector<String>();
		vLabsOrig.addElement( "<--Selecione-->" );
		vLabsOrig.addElement( "Nacional" );
		vLabsOrig.addElement( "Estrangeira - Importação direta, exceto a indicada no código 6" );
		vLabsOrig.addElement( "Estrangeira - Adquirida no mercado interno, exceto a indicada no código 7" );
		vLabsOrig.addElement( "Nacional, mercadoria ou bem com Conteúdo de Importação superior a 40% (quarenta por cento)" );
		vLabsOrig.addElement( "Nacional, cuja produção tenha sido feita em conformidade com os processos produtivos básicos de que tratam o Decreto-Lei nº 288/67, e as Leis nos8.248/91, 8.387/91, 10.176/01 e 11.484/ 07" );
		vLabsOrig.addElement( "Nacional, mercadoria ou bem com Conteúdo de Importação inferior ou igual a 40% (quarenta por cento)" );
		vLabsOrig.addElement( "Estrangeira - Importação direta, sem similar nacional, constante em lista de Resolução CAMEX" );
		vLabsOrig.addElement( "Estrangeira - Adquirida no mercado interno, sem similar nacional, constante em lista de Resolução CAMEX." );
		
		vValsOrig.addElement( "" );
		vValsOrig.addElement( "0" );
		vValsOrig.addElement( "1" );
		vValsOrig.addElement( "2" );
		vValsOrig.addElement( "3" );
		vValsOrig.addElement( "4" );
		vValsOrig.addElement( "5" );
		vValsOrig.addElement( "6" );
		vValsOrig.addElement( "7" );
		cbOrig = new JComboBoxPad( vLabsOrig, vValsOrig, JComboBoxPad.TP_STRING, 1, 0 );
		cbOrig.setRenderer( new JMyComboBoxRenderer(vLabsOrig) );

		/** Destino da mercadoria */

		Vector<String> vNoUFLabs = new Vector<String>();
		Vector<String> vNoUFVals = new Vector<String>();
		vNoUFLabs.addElement( "Dentro do Estado" );
		vNoUFLabs.addElement( "Fora do Estado" );
		vNoUFVals.addElement( "S" );
		vNoUFVals.addElement( "N" );

		rgNoUF = new JRadioGroup<String, String>( 2, 1, vNoUFLabs, vNoUFVals );
		rgNoUF.setVlrString( "S" );

		/** Tipo */

		Vector<String> vTipoLabs = new Vector<String>();
		Vector<String> vTipoVals = new Vector<String>();
		vTipoLabs.addElement( "Venda" );
		vTipoLabs.addElement( "Compra" );
		vTipoVals.addElement( "VD" );
		vTipoVals.addElement( "CP" );

		rgTipo = new JRadioGroup<String, String>( 2, 1, vTipoLabs, vTipoVals );
		rgTipo.setVlrString( "VD" );

		rgTipo.addRadioGroupListener( this );

		/** Tipo do ICMS */

		Vector<String> vTipoIcmsVals = new Vector<String>();
		Vector<String> vTipoIcmsLabs = new Vector<String>();

		vTipoIcmsLabs.addElement( "Isento" );
		vTipoIcmsLabs.addElement( "Subst. Trib." );
		vTipoIcmsLabs.addElement( "Não inside" );
		vTipoIcmsLabs.addElement( "Trib. Integral" );

		vTipoIcmsVals.addElement( "II" );
		vTipoIcmsVals.addElement( "FF" );
		vTipoIcmsVals.addElement( "NN" );
		vTipoIcmsVals.addElement( "TT" );

		rgTipoFisc = new JRadioGroup<String, String>( 2, 2, vTipoIcmsLabs, vTipoIcmsVals );

		/** Tipo de substituição tributária */

		Vector<String> vSTLabs = new Vector<String>();
		vSTLabs.addElement( "Substituto" );
		vSTLabs.addElement( "Substituído" );
		Vector<String> vSTVals = new Vector<String>();
		vSTVals.addElement( "SU" );
		vSTVals.addElement( "SI" );
		rgTipoST = new JRadioGroup<String, String>( 2, 1, vSTLabs, vSTVals );

		Vector<String> vTpRedIcmsFiscLabs = new Vector<String>();
		vTpRedIcmsFiscLabs.addElement( "Base ICMS" );
		vTpRedIcmsFiscLabs.addElement( "Valor ICMS" );
		Vector<String> vTpRedIcmsFiscVals = new Vector<String>();
		vTpRedIcmsFiscVals.addElement( "B" );
		vTpRedIcmsFiscVals.addElement( "V" );
		rgTpRedIcmsFisc = new JRadioGroup<String, String>( 2, 1, vTpRedIcmsFiscLabs, vTpRedIcmsFiscVals );

		/** Modalidade de determinação da BC do ICMS */

		Vector<String> vLabsModBCICMS = new Vector<String>();
		Vector<Integer> vValsModBCICMS = new Vector<Integer>();
		vLabsModBCICMS.addElement( "0-Margem Valor Agregado (%)" );
		vLabsModBCICMS.addElement( "1-Pauta (valor)" );
		vLabsModBCICMS.addElement( "2-Preço Tabelado Máx.(valor)" );
		vLabsModBCICMS.addElement( "3-Valor da Operação" );
		vValsModBCICMS.addElement( new Integer( 0 ) );
		vValsModBCICMS.addElement( new Integer( 1 ) );
		vValsModBCICMS.addElement( new Integer( 2 ) );
		vValsModBCICMS.addElement( new Integer( 3 ) );

		cbModBCICMS = new JComboBoxPad( vLabsModBCICMS, vValsModBCICMS, JComboBoxPad.TP_INTEGER, 1, 0 );
		cbModBCICMS.setVlrInteger( new Integer( 3 ) );

		/** Modalidade de determinação da BC do ICMS de Substituição tributária */

		Vector<String> vLabsModBCICMSST = new Vector<String>();
		Vector<Integer> vValsModBCICMSST = new Vector<Integer>();
		vLabsModBCICMSST.addElement( "0-Preço tabelado ou máx. sugerido" );
		vLabsModBCICMSST.addElement( "1-Lista Negativa (valor)" );
		vLabsModBCICMSST.addElement( "2-Lista Positiva (valor)" );
		vLabsModBCICMSST.addElement( "3-Lista Neutra (valor)" );
		vLabsModBCICMSST.addElement( "4-Margem valor agregado (%)" );
		vLabsModBCICMSST.addElement( "5-Pauta (valor)" );
		vValsModBCICMSST.addElement( new Integer( 0 ) );
		vValsModBCICMSST.addElement( new Integer( 1 ) );
		vValsModBCICMSST.addElement( new Integer( 2 ) );
		vValsModBCICMSST.addElement( new Integer( 3 ) );
		vValsModBCICMSST.addElement( new Integer( 4 ) );
		vValsModBCICMSST.addElement( new Integer( 5 ) );

		cbModBCICMSST = new JComboBoxPad( vLabsModBCICMSST, vValsModBCICMSST, JComboBoxPad.TP_INTEGER, 1, 0 );
		cbModBCICMSST.setVlrInteger( new Integer( 4 ) );

		/** Tipo de cálculo do IPI */

		Vector<String> vLabsTpCalcIPI = new Vector<String>();
		Vector<String> vValsTpCalcIPI = new Vector<String>();
		vLabsTpCalcIPI.addElement( "<--Selecione-->" );
		vLabsTpCalcIPI.addElement( "Percentual" );
		vLabsTpCalcIPI.addElement( "Em valor" );
		vValsTpCalcIPI.addElement( "" );
		vValsTpCalcIPI.addElement( "P" );
		vValsTpCalcIPI.addElement( "V" );

		cbTpCalcIPI = new JComboBoxPad( vLabsTpCalcIPI, vValsTpCalcIPI, JComboBoxPad.TP_STRING, 1, 0 );

		cbTpCalcIPI.addComboBoxListener( this );
		
		
		/** Indicador de apuracao do IPI*/

		Vector<String> vLabsIndApurIPI = new Vector<String>();
		Vector<String> vValsIndApurIPI = new Vector<String>();

		vLabsIndApurIPI.addElement( "Mensal" );
		vLabsIndApurIPI.addElement( "Decendial" );
		
		vValsIndApurIPI.addElement( "0" );
		vValsIndApurIPI.addElement( "1" );
		
		rgIndApurIPI = new JRadioGroup<String, String>( 1, 2, vLabsIndApurIPI, vValsIndApurIPI );
	}

	private void montaListaCampos() {

		// Regra fiscal padrão no cabeçalho
		lcRegraFiscalIt.setUsaME( true );
		lcRegraFiscal.add( new GuardaCampo( txtCodRegra, "CodRegra", "Cód.reg.fisc.", ListaCampos.DB_PK, true ) );
		lcRegraFiscal.add( new GuardaCampo( txtDescRegra, "DescRegra", "Descrição da regra fiscal", ListaCampos.DB_SI, null, false ) );
		lcRegraFiscal.montaSql( false, "REGRAFISCAL", "LF" );
		lcRegraFiscal.setQueryCommit( false );
		lcRegraFiscal.setReadOnly( true );
		txtCodRegra.setTabelaExterna( lcRegraFiscal, FRegraFiscal.class.getCanonicalName() );

		// Regra fiscal específica no item
		lcRegraFiscalIt.setUsaME( true );
		lcRegraFiscalIt.add( new GuardaCampo( txtCodRegraIt, "CodRegra", "Cód.reg.fisc.", ListaCampos.DB_PK, false ) );
		lcRegraFiscalIt.add( new GuardaCampo( txtDescRegraIt, "DescRegra", "Descrição da regra fiscal", ListaCampos.DB_SI, null, false ) );
		lcRegraFiscalIt.montaSql( false, "REGRAFISCAL", "LF" );
		lcRegraFiscalIt.setQueryCommit( false );
		lcRegraFiscalIt.setReadOnly( true );
		txtCodRegraIt.setTabelaExterna( lcRegraFiscalIt, FRegraFiscal.class.getCanonicalName() );
		
		// Regra fiscal padrão no cabeçalho
		lcCalcCusto.setUsaME( true );
		lcCalcCusto.add( new GuardaCampo( txtCodCalc, "CodCalc", "Cód.calc.custo", ListaCampos.DB_PK, false ) );
		lcCalcCusto.add( new GuardaCampo( txtDescCalc, "DescCalc", "Formula de Custo de Aquisição", ListaCampos.DB_SI, null, false ) );
		lcCalcCusto.montaSql( false, "CALCCUSTO", "LF" );
		lcCalcCusto.setQueryCommit( false );
		lcCalcCusto.setReadOnly( true );
		txtCodCalc.setTabelaExterna( lcCalcCusto, FCalcCusto.class.getCanonicalName() );

		lcNCM.setUsaME( false );
		lcNCM.add( new GuardaCampo( txtCodNCM, "CodNCM", "Cód.NCM", ListaCampos.DB_PK, txtDescNCM, false ) );
		lcNCM.add( new GuardaCampo( txtDescNCM, "DescNCM", "Descrição da nomenclatura comum do Mercosul", ListaCampos.DB_SI, false ) );
		lcNCM.add( new GuardaCampo( txaDescNCM, "TextoNCM", "Descrição completa da nomenclatura comum do Mercosul", ListaCampos.DB_SI, false ) );
		lcNCM.add( new GuardaCampo( txaDescExTIPI, "ExcecaoNCM", "Descrição das exceções", ListaCampos.DB_SI, false ) );
		lcNCM.setDinWhereAdic( "EXISTS(SELECT NN.CODNCM FROM LFNCMNBM NN WHERE NN.CODNCM=LFNCM.CODNCM)", txtCodNCM );
		lcNCM.montaSql( false, "NCM", "LF" );
		lcNCM.setQueryCommit( false );
		lcNCM.setReadOnly( true );
		txtCodNCM.setTabelaExterna( lcNCM, FNCM.class.getCanonicalName() );
		txtDescNCM.setListaCampos( lcNCM );

		lcNBM.setUsaME( false );
		lcNBM.add( new GuardaCampo( txtCodNBM, "CodNBM", "Cód.NBM", ListaCampos.DB_PK, txtDescNBM, false ) );
		lcNBM.add( new GuardaCampo( txtDescNBM, "DescNBM", "Descrição da nomenclatura brasileira de mercadorias", ListaCampos.DB_SI, false ) );
		lcNBM.setDinWhereAdic( "EXISTS(SELECT NN.CODNBM FROM LFNCMNBM NN WHERE NN.CODNCM = #S AND NN.CODNBM=LFNBM.CODNBM)", txtCodNCM );
		lcNBM.montaSql( false, "NBM", "LF" );
		lcNBM.setQueryCommit( false );
		lcNBM.setReadOnly( true );
		txtCodNBM.setTabelaExterna( lcNBM, FNBM.class.getCanonicalName() );
		txtDescNBM.setListaCampos( lcNBM );

		lcTratTrib.setUsaME( true );
		lcTratTrib.add( new GuardaCampo( txtCodTratTrib, "CodTratTrib", "Cód.t.trib.", ListaCampos.DB_PK, null, true ) );
		lcTratTrib.add( new GuardaCampo( txtDescTratTrib, "DescTratTrib", "Descrição do tratamento tributario", ListaCampos.DB_SI, false ) );
		lcTratTrib.montaSql( false, "TRATTRIB", "LF" );
		lcTratTrib.setQueryCommit( false );
		lcTratTrib.setReadOnly( true );
		txtCodTratTrib.setTabelaExterna( lcTratTrib, FTratTrib.class.getCanonicalName() );

		lcMens.setUsaME( true );
		lcMens.add( new GuardaCampo( txtCodMens, "CodMens", "Cód.mens.", ListaCampos.DB_PK, false ) );
		lcMens.add( new GuardaCampo( txtDescMens, "Mens", "Mensagem", ListaCampos.DB_SI, false ) );
		lcMens.montaSql( false, "MENSAGEM", "LF" );
		lcMens.setQueryCommit( false );
		lcMens.setReadOnly( true );
		txtCodMens.setTabelaExterna( lcMens, FMensagem.class.getCanonicalName() );

		//lcTipoMov.setUsaME( true );
		lcTipoMov.add( new GuardaCampo( txtCodTipoMov, "CodTipoMov", "Cód.tp.Mov.", ListaCampos.DB_PK, false ) );
		lcTipoMov.add( new GuardaCampo( txtDescTipoMov, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false ) );
		lcTipoMov.montaSql( false, "TIPOMOV", "EQ" );
		lcTipoMov.setQueryCommit( false );
		lcTipoMov.setReadOnly( true );
		txtCodTipoMov.setTabelaExterna( lcTipoMov, FTipoMov.class.getCanonicalName() );
		
		//lcTipoFiscCli.setUsaME( true );
		lcTipoFiscCli.add( new GuardaCampo( txtCodTipoFisc, "CodFiscCli", "Cód.c.fisc.", ListaCampos.DB_PK, false ) );
		lcTipoFiscCli.add( new GuardaCampo( txtDescFiscCli, "DescFiscCli", "Descrição da classificação fiscal", ListaCampos.DB_SI, false ) );
		lcTipoFiscCli.montaSql( false, "TIPOFISCCLI", "LF" );
		lcTipoFiscCli.setQueryCommit( false );
		lcTipoFiscCli.setReadOnly( true );
		txtCodTipoFisc.setTabelaExterna( lcTipoFiscCli, FTipoFisc.class.getCanonicalName() );

		//lcSitTribCOF.setUsaME( true );
		lcSitTribCOF.add( new GuardaCampo( txtCodSitTribCOF, "CodSitTrib", "Cód.sit.trib.", ListaCampos.DB_PK, false ) );
		lcSitTribCOF.add( new GuardaCampo( txtImpSitTribCOF, "ImpSitTrib", "Cofins", ListaCampos.DB_PK, false ) );
		lcSitTribCOF.add( new GuardaCampo( txtDescSitTribCOF, "DescSitTrib", "Descrição da Situação Tributária", ListaCampos.DB_SI, false ) );
		//lcSitTribCOF.add( new GuardaCampo( txtOperacaoCOF, "Operacao", "Operacao", ListaCampos.DB_SI, false ) );
		lcSitTribCOF.setWhereAdic( "IMPSITTRIB='CO'" );
		lcSitTribCOF.setDinWhereAdic( "OPERACAO=#S", txtOperacaoCOF);
		
		lcSitTribCOF.montaSql( false, "SITTRIB", "LF" );
		lcSitTribCOF.setQueryCommit( false );
		lcSitTribCOF.setReadOnly( true );
		txtCodSitTribCOF.setTabelaExterna( lcSitTribCOF, FSitTrib.class.getCanonicalName() );
		txtImpSitTribCOF.setTabelaExterna( lcSitTribCOF, FSitTrib.class.getCanonicalName() );
		//txtDescSitTribCOF.setListaCampos( lcSitTribCOF );

		//lcSitTribPIS.setUsaME( true );
		lcSitTribPIS.add( new GuardaCampo( txtCodSitTribPIS, "CodSitTrib", "Cód.sit.trib.", ListaCampos.DB_PK, false ) );
		lcSitTribPIS.add( new GuardaCampo( txtImpSitTribPIS, "ImpSitTrib", "Pis", ListaCampos.DB_PK, false ) );
		lcSitTribPIS.add( new GuardaCampo( txtDescSitTribPIS, "DescSitTrib", "Descrição da Situação Tributária", ListaCampos.DB_SI, false ) );
		//lcSitTribPIS.add( new GuardaCampo( txtOperacaoPIS, "Operacao", "Operacao", ListaCampos.DB_SI, false ) );
		lcSitTribPIS.setWhereAdic( "IMPSITTRIB='PI' " );
		lcSitTribPIS.setDinWhereAdic( "OPERACAO=#S", txtOperacaoPIS);
		
		lcSitTribPIS.montaSql( false, "SITTRIB ", "LF" ); // Nome da tabela com espaço em branco no final, para contornar bug do lista campos
		lcSitTribPIS.setQueryCommit( false );
		lcSitTribPIS.setReadOnly( true );
		txtCodSitTribPIS.setTabelaExterna( lcSitTribPIS, FSitTrib.class.getCanonicalName() );
		txtImpSitTribPIS.setTabelaExterna( lcSitTribPIS, FSitTrib.class.getCanonicalName() );
		//txtDescSitTribPIS.setListaCampos( lcSitTribPIS );

		//lcSitTribIPI.setUsaME( true );
		lcSitTribIPI.add( new GuardaCampo( txtCodSitTribIPI, "CodSitTrib", "Cód.sit.trib.", ListaCampos.DB_PK, true ) );
		lcSitTribIPI.add( new GuardaCampo( txtImpSitTribIPI, "ImpSitTrib", "IPI", ListaCampos.DB_PK, false ) );
		lcSitTribIPI.add( new GuardaCampo( txtDescSitTribIPI, "DescSitTrib", "Descrição da Situação Tributária", ListaCampos.DB_SI, false ) );
		lcSitTribIPI.setWhereAdic( "IMPSITTRIB='IP'" );
		lcSitTribIPI.montaSql( false, "SITTRIB  ", "LF" ); // Nome da tabela com 2 espaços em branco no final, para contornar bug do lista campos
		lcSitTribIPI.setQueryCommit( false );
		lcSitTribIPI.setReadOnly( true );
		txtCodSitTribIPI.setTabelaExterna( lcSitTribIPI, FSitTrib.class.getCanonicalName() );
		txtImpSitTribIPI.setTabelaExterna( lcSitTribIPI, FSitTrib.class.getCanonicalName() );
		//txtDescSitTribIPI.setListaCampos( lcSitTribIPI );

		//lcSitTribISS.setUsaME( true );
		lcSitTribISS.add( new GuardaCampo( txtCodSitTribISS, "CodSitTrib", "Cód.sit.trib.", ListaCampos.DB_PK, false ) );
		lcSitTribISS.add( new GuardaCampo( txtImpSitTribISS, "ImpSitTrib", "Pis", ListaCampos.DB_PK, false ) );
		lcSitTribISS.add( new GuardaCampo( txtDescSitTribISS, "DescSitTrib", "Descrição da Situação Tributária", ListaCampos.DB_SI, false ) );
		lcSitTribISS.setWhereAdic( "IMPSITTRIB='IS'" );
		lcSitTribISS.montaSql( false, "SITTRIB ", "LF" ); // Nome da tabela com espaço em branco no final, para contornar bug do lista campos
		lcSitTribISS.setQueryCommit( false );
		lcSitTribISS.setReadOnly( true );
		txtCodSitTribISS.setTabelaExterna( lcSitTribISS, FSitTrib.class.getCanonicalName() );
		txtImpSitTribISS.setTabelaExterna( lcSitTribISS, FSitTrib.class.getCanonicalName() );
		//xtDescSitTribISS.setListaCampos( lcSitTribISS );
		
		lcPais.setUsaME( false );
		lcPais.add( new GuardaCampo( txtCodPais, "CodPais", "Cod.país.", ListaCampos.DB_PK, false ) );
		lcPais.add( new GuardaCampo( txtDescPais, "NomePais", "Nome", ListaCampos.DB_SI, false ) );
		lcPais.montaSql( false, "PAIS", "SG" );
		lcPais.setQueryCommit( false );
		lcPais.setReadOnly( true );
		txtCodPais.setTabelaExterna( lcPais, FPais.class.getCanonicalName() );
		//txtDescPais.setListaCampos( lcPais );

		lcUF.setUsaME( false );
		lcUF.add( new GuardaCampo( txtSiglaUF, "SiglaUf", "Sigla", ListaCampos.DB_PK, false ) );
		lcUF.add( new GuardaCampo( txtNomeUF, "NomeUf", "Nome", ListaCampos.DB_SI, false ) );
		lcUF.setDinWhereAdic( "CODPAIS = #N", txtCodPais );
		lcUF.montaSql( false, "UF", "SG" );
		lcUF.setQueryCommit( false );
		lcUF.setReadOnly( true );
		txtSiglaUF.setTabelaExterna( lcUF, FUF.class.getCanonicalName() );
		//txtNomeUF.setListaCampos( lcUF );

		lcServico.setUsaME( false );
		lcServico.add( new GuardaCampo( txtCodServ, "CodServ", "Cod.Serviço", ListaCampos.DB_PK, false ) );
		lcServico.add( new GuardaCampo( txaDescServ, "DescServ", "Descrição do Serviço", ListaCampos.DB_SI, false ) );
		lcServico.montaSql( false, "SERVICO", "LF" );
		lcServico.setQueryCommit( false );
		lcServico.setReadOnly( true );
		txtCodServ.setTabelaExterna( lcServico, FServico.class.getCanonicalName() );
		//txaDescServ.setListaCampos( lcServico );
		
		//lcCSOSN.setUsaME( true );
		lcCSOSN.add( new GuardaCampo( txtCSOSN, "CSOSN", "CSOSN", ListaCampos.DB_PK, false ) );
		lcCSOSN.add( new GuardaCampo( txtDescCSOSN, "DescCSOSN", "Descrição da CSOSN", ListaCampos.DB_SI, false ) );
		lcCSOSN.montaSql( false, "CSOSN", "LF" );
		lcCSOSN.setQueryCommit( false );
		lcCSOSN.setReadOnly( true );
		txtCSOSN.setTabelaExterna( lcCSOSN, FServico.class.getCanonicalName() );
		
		
		lcSpNatOP.setUsaME( false );
		lcSpNatOP.add( new GuardaCampo( txtCstNatOp, "CST", "CST.", ListaCampos.DB_PK,txtDescNatOp, false ) );
		lcSpNatOP.add( new GuardaCampo( txtCodNatOp, "CODIGO", "Cód.", ListaCampos.DB_PK, false ) );
		lcSpNatOP.add( new GuardaCampo( txtDescNatOp, "DESC_PROD", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcSpNatOP.montaSql( false, "NATOPER", "SP" ); // Nome da tabela com 2 espaços em branco no final, para contornar bug do lista campos
		lcSpNatOP.setQueryCommit( false );
		lcSpNatOP.setReadOnly( true );
		txtCstNatOp.setTabelaExterna( lcSpNatOP, null);
		txtCodNatOp.setTabelaExterna( lcSpNatOP, null);		
	}

	private void montaTela() {
		
		montaCombos();
		montaListaCampos();

		pnPrincipal.add( tpnPrincipal );

		lcDet.setMaster( lcCampos );

		// ********** Início aba Geral **********

		tpnPrincipal.addTab( "Geral", pnCliente );
		pnCab.add( panelGeral );
		setPainel( panelGeral );
		setListaCampos( lcCampos );
		setAltCab( 130 );

		adicCampo( txtCodFisc, 7, 20, 100, 20, "CodFisc", "Cód.class.fiscal", ListaCampos.DB_PK, true );
		adicCampo( txtDescFisc, 110, 20, 595, 20, "DescFisc", "Descrição da classificação fiscal", ListaCampos.DB_SI, true );

		adicCampo( txtCodRegra, 7, 60, 100, 20, "CodRegra", "Cód.reg.CFOP", ListaCampos.DB_FK, txtDescRegra, true );
		adicDescFK( txtDescRegra, 110, 60, 595, 20, "DescRegra", "Descrição da regra fiscal" );
		
		// ********** Aba Nomenclatura Comum **********

		tpnPrincipal.addTab( "Nomenclatura Comum", panelNomeComum );

		panelNomeComum.add( panelNomeComumNCM, BorderLayout.CENTER );

		panelNomeComumNCM.add( panelNomeComumNCMCampos, BorderLayout.NORTH );
		panelNomeComumNCM.add( panelNomeComumNCMDescricoes, BorderLayout.CENTER );

		panelNomeComumNCMDescricoes.setTopComponent( spDescNCM );
		panelNomeComumNCMDescricoes.setBottomComponent( spDescExTIPI );
		panelNomeComumNCMDescricoes.setDividerLocation( 220 );
		panelNomeComumNCMDescricoes.setDividerSize( 3 );
		panelNomeComumNCMDescricoes.setBorder( BorderFactory.createEmptyBorder() );

		panelNomeComum.add( panelNomeComumNBM, BorderLayout.SOUTH );

		spDescNCM.setBorder( BorderFactory.createTitledBorder( "Descrição completa da nomenclatura comum do Mercosul" ) );
		spDescExTIPI.setBorder( BorderFactory.createTitledBorder( "Texto auxíliar para exceções" ) );
		txaDescNCM.setBorder( BorderFactory.createEtchedBorder() );
		txaDescExTIPI.setBorder( BorderFactory.createEtchedBorder() );

		setPainel( panelNomeComumNCMCampos );
		

		adicCampo( txtCodNCM, 7, 20, 100, 20, "CodNCM", "Cód.NCM", ListaCampos.DB_FK, txtDescNCM, false );
		adicDescFK( txtDescNCM, 110, 20, 320, 20, "DescNCM", "Descrição da nomenclatura comum do Mercosul" );
		adicCampo( txtExTIPI, 433, 20, 47, 20, "ExTIPI", "Cód.ex.", ListaCampos.DB_SI, false );

		adicCampo( txtCstNatOp, 7, 60, 100, 20, "CST", "CST Nat.Oper.", ListaCampos.DB_FK, false );
		adicCampoInvisivel( txtCodNatOp, "CODIGO", "Cód.nat.oper", ListaCampos.DB_FK, false );
		adicDescFK( txtDescNatOp, 110, 60, 595, 20, "DESC_PROD", "Descrição do produto");

		
		setPainel( panelNomeComumNBM );

		adicCampo( txtCodNBM, 7, 25, 100, 20, "CodNBM", "Cód.NBM", ListaCampos.DB_FK, txtDescNBM, false );
		adicDescFK( txtDescNBM, 110, 25, 370, 20, "DescNBM", "Descrição da nomenclatura brasileira de mercadorias" );
		
		btCopiar.setPreferredSize(new Dimension(26,26));
		pnCopiaClass.add(btCopiar);
		pnNavCab.add(pnCopiaClass,BorderLayout.EAST);
		btCopiar.setToolTipText("Copia classificação fiscal.");

		// *******************************

		// ********** Aba Serviço **********

		tpnPrincipal.addTab( "Definição do serviço", pnServico );
		pnServico.add( panelServico, BorderLayout.CENTER );

		setPainel( panelServico );

		adicCampo( txtCodServ, 7, 20, 70, 20, "CodServ", "Cód.Serv.", ListaCampos.DB_FK, false );
		adic( txaDescServ, 80, 20, 650, 100, "Descrição do serviço" );

		// *******************************

		setListaCampos( false, "CLFISCAL", "LF" );
		lcCampos.setQueryInsert( false );
		
	}
	
	private void montaDetalhe(){
		// ********** ABA VARIANTES **/
		
		pnDet.add( tpnGeral );
		tpnGeral.addTab( "Variantes", panelVariantes );
		setPainel( panelVariantesCampos );
		setListaCampos( lcDet );
		setAltDet( 265 );
		setNavegador( navRod );
		
		adicCampo( txtCodItClFiscal, 7, 20, 40, 20, "CodItFisc", "Item", ListaCampos.DB_PK, false );
	
		adicCampo( txtCodTipoFisc, 50, 20, 70, 20, "CodFiscCli", "Cód.fisc.cli.", ListaCampos.DB_FK, txtDescFiscCli, false );
		adicDescFK( txtDescFiscCli, 123, 20, 345, 20, "DescFiscCli", "Descrição do tipo fiscal de cliente" );

		adicCampo( txtCodTipoMov, 7, 60, 70, 20, "CodTipoMov", "Cód.tp.Mov", ListaCampos.DB_FK, txtDescTipoMov, false );
		adicDescFK( txtDescTipoMov, 80, 60, 388, 20, "DescTipoMov", "Descrição do tipo de movimento" );

		adicCampo( txtCodMens, 7, 100, 70, 20, "CodMens", "Cód.mens.", ListaCampos.DB_FK, txtDescMens, false );
		adicDescFK( txtDescMens, 80, 100, 388, 20, "Mens", "Mensagem" );

		adicDB( rgNoUF, 471, 20, 150, 60, "NoUFItFisc", "Destino da mercadoria", true );
		adicDB( rgTipo, 631, 20, 100, 60, "TipoUsoItFisc", "Uso para", true );

		adicDB( cbGeralFisc, 471, 100, 150, 20, "GeralFisc", "Padrão", true );

		adicCampo( txtCodPais, 7, 140, 70, 20, "CodPais", "Cod.país", ListaCampos.DB_FK, txtDescPais, false );
		adicDescFK( txtDescPais, 80, 140, 227, 20, "NomePais", "Nome do país" );
		adicCampo( txtSiglaUF, 310, 140, 70, 20, "SiglaUf", "Sigla UF", ListaCampos.DB_FK, txtNomeUF, false );
		adicDescFK( txtNomeUF, 383, 140, 238, 20, "NomeUF", "Nome UF" );

		adicCampo( txtCodRegraIt, 7, 180, 70, 20, "CodRegra", "Cód.reg.CFOP", ListaCampos.DB_FK, txtDescRegraIt, false );
		adicDescFK( txtDescRegraIt, 80, 180, 227, 20, "DescRegra", "Descrição da regra fiscal" );
		
		adicCampo( txtCodCalc, 310, 180, 70, 20, "CodCalc", "Cód.cal.custo", ListaCampos.DB_FK, txtDescCalc, false );
		adicDescFK( txtDescCalc, 383, 180, 238, 20, "DescCalc", "Formula de custo de aquisição" );

		adic( btCopiarVariante, 630, 135, 100, 30 );

		// ********** ABA ICMS **/

		tpnGeral.addTab( "ICMS", panelICMS );

		setPainel( panelICMSCampos );

		// ********** Inclusão dos campos **/

		adicCampo( txtCodTratTrib, 7, 20, 50, 20, "CodTratTrib", "Cód.trat.", ListaCampos.DB_FK, txtDescTratTrib, true );
		adicDescFK( txtDescTratTrib, 60, 20, 200, 20, "DescTratTrib", "Descrição do tratamento tributário" );

		adicDB( cbOrig, 7, 60, 253, 25, "OrigFisc", "Origem", true );
		adicDB( cbModBCICMS, 7, 110, 253, 25, "ModBcIcms", "Modalidade da base de cálculo ", true );
		adicDB( cbModBCICMSST, 7, 160, 253, 25, "ModBcIcmsST", "Modalidade da base de cálculo ST", true );

		JLabelPad separacao = new JLabelPad();
		separacao.setBorder( BorderFactory.createEtchedBorder() );
		adic( separacao, 272, 7, 2, 180 );

		adicDB( rgTipoFisc, 283, 20, 220, 70, "TipoFisc", "Situação do ICMS:", true );
		adicDB( rgTipoST, 506, 20, 110, 70, "TipoST", "Tipo de Sub.Trib.", true );
		adicDB( rgTpRedIcmsFisc, 619, 20, 110, 70, "tpredicmsfisc", "Tipo de Redução", false );

		adicCampo( txtMargemVlAgr, 506, 110, 110, 20, "MargemVlAgr", "% Vlr.Agregado", ListaCampos.DB_SI, false );
		adicCampo( txtRedFisc, 619, 110, 110, 20, "RedFisc", "% Redução ICMS", ListaCampos.DB_SI, false );

		adicDB( cbRedBaseST, 615, 140, 180, 20, "redbasest", "", false );

		adicDB( cbRedBaseFrete, 615, 160, 180, 20, "redbasefrete", "", false );

		adicDB( cbAdicICMSTotNota, 283, 180, 300, 20, "AdicICMSTotNota","", false );
		adicDB( cbCalcSTCM, 283, 200, 300, 20, "CALCSTCM","", false );
		
		adicCampo( txtAliqFisc, 283, 110, 108, 20, "AliqFisc", "% ICMS Interest.", ListaCampos.DB_SI, false );
		adicCampo( txtAliqLFisc, 394, 110, 110, 20, "AliqlFisc", "% Aliq.liv.ICMS", ListaCampos.DB_SI, null, false );
		adicCampo( txtAliqFiscIntra, 283, 150, 108, 20, "AliqFiscIntra", "% ICMS Intraest.", ListaCampos.DB_SI, false );
		adicCampo( txtAliqICMSImp, 394, 150, 108, 20, "AliqICMSImp", "% ICMS Import.", ListaCampos.DB_SI, false );
		adicCampo( txtAliqCargaMedia, 505, 150, 108, 20, "AliqICMSSTCM", "% Carga Média.", ListaCampos.DB_SI, false );
		
		
		// ********** ABA IPI **/

		tpnGeral.addTab( "IPI", panelIPI );
		setPainel( panelIPICampos );

		adicCampo( txtCodSitTribIPI, 7, 20, 80, 20, "CodSitTribIPI", "Cód.sit.trib.", ListaCampos.DB_FK, txtDescSitTribPIS, false );
		adicCampoInvisivel( txtImpSitTribIPI, "ImpSitTribIPI", "Imposto", ListaCampos.DB_SI, false );
		adicDescFK( txtDescSitTribIPI, 90, 20, 300, 20, "DescSitTrib", "Descrição da Situação Tributária" );
		adicDB( cbTpCalcIPI, 7, 60, 200, 20, "TpCalcIPI", "Tipo de cálculo", false );

		adicCampo( txtAliqIPIFisc, 7, 100, 98, 20, "AliqIPIFisc", "% Alíq.IPI", ListaCampos.DB_SI, false );
		adicCampo( txtVlrIpiUnidTrib, 108, 100, 99, 20, "VlrIPIUnidTrib", "Vlr.por unidade", ListaCampos.DB_SI, false );

		adicDB( rgIndApurIPI, 7, 140, 200, 30, "IndApurIPI", "Período de apuração", false );
		
		adicDB( cbAdicIPIBaseICMS, 7, 170, 200, 30, "AdicIPIBaseICMS","", false );

		
		// ********** ABA PIS **/

		tpnGeral.addTab( "PIS", panelPIS );
		setPainel( panelPISCampos );

		adicCampo( txtCodSitTribPIS, 7, 20, 80, 20, "CodSitTribPIS", "Cód.sit.trib.", ListaCampos.DB_FK, txtDescSitTribPIS, true );
		adicCampoInvisivel( txtImpSitTribPIS, "ImpSitTribPIS", "Imposto", ListaCampos.DB_SI, false );
		adicDescFK( txtDescSitTribPIS, 90, 20, 300, 20, "DescSitTrib", "Descrição da Situação Tributária" );
		adicCampo( txtAliqPisFisc, 7, 60, 80, 20, "AliqPisFisc", "Aliq.PIS", ListaCampos.DB_SI, null, true );
		adicCampo( txtVlrPisUnidTrib, 90, 60, 99, 20, "VlrPisUnidTrib", "Vlr.por unidade", ListaCampos.DB_SI, false );

		// ********** ABA COFINS **/

		tpnGeral.addTab( "COFINS", panelCOFINS );
		setPainel( panelCOFINSCampos );

		adicCampo( txtCodSitTribCOF, 7, 20, 80, 20, "CodSitTribCOF", "Cód.sit.trib.", ListaCampos.DB_FK, txtDescSitTribCOF, true );
		adicCampoInvisivel( txtImpSitTribCOF, "ImpSitTribCOF", "Imposto", ListaCampos.DB_SI, false );
		adicDescFK( txtDescSitTribCOF, 90, 20, 300, 20, "DescSitTrib", "Descrição da Situação Tributária" );

		adicCampo( txtAliqCofinsFisc, 7, 60, 80, 20, "AliqCofinsFisc", "Aliq.Cofins", ListaCampos.DB_SI, null, true );
		adicCampo( txtVlrCofUnidTrib, 90, 60, 99, 20, "VlrCofUnidTrib", "Vlr.por unidade", ListaCampos.DB_SI, false );

		// ********** ABA FUNRURAL **/

		tpnGeral.addTab( "FUNRURAL", panelFUNRURAL );
		setPainel( panelFUNRURALCampos );

		adicCampo( txtAliqFunRuralFisc, 7, 20, 80, 20, "AliqFunRuralFisc", "Aliq.FunRural", ListaCampos.DB_SI, null, true );

		// ********** ABA ISS **/
						
		 tpnGeral.addTab( "ISS", panelISS );
		 setPainel( panelISSCampos );
//		 tpnGeral.setEnabledAt( 5, false );
				
		 adicCampo( txtCodSitTribISS, 7, 20, 80, 20, "CodSitTribISS", "Cód.sit.trib.", ListaCampos.DB_FK, txtDescSitTribISS, false );
		 adicCampoInvisivel( txtImpSitTribISS, "ImpSitTribISS", "Imposto", ListaCampos.DB_SI, false );
		 adicDescFK( txtDescSitTribISS, 90, 20, 300, 20, "DescSitTrib", "Descrição da Situação Tributária" );
			
		 adicCampo( txtAliqISSFisc, 7, 60, 80, 20, "AliqISSFisc", "Aliq. ISS", ListaCampos.DB_SI, null, false );
		 adicDB( cbRetensaoISS, 7, 100, 250, 20, "RetensaoISS", "", false );
		//		
		// ********** ABA IR **/

		tpnGeral.addTab( "IR", panelIR );
		setPainel( panelIRCampos );

		adicCampo( txtAliqIrFisc, 7, 20, 80, 20, "AliqIrFisc", "Aliq. IR", ListaCampos.DB_SI, true );

		// ********** ABA CONTRIBUIÇÃO SOCIAL **/
		//				
		tpnGeral.addTab( "Contribuição Social", panelCSocial );
		setPainel( panelCSocialCampos );

		adicCampo( txtAliqCSocialFisc, 7, 20, 80, 20, "AliqCSocialFisc", "Aliq. C.Social", ListaCampos.DB_SI, true );

		// ********** ABA II **/

		tpnGeral.addTab( "Imposto de importação", panelII );
		setPainel( panelIICampos );
		// tpnGeral.setEnabledAt( 8, false );
		adicCampo( txtAliqIiFisc, 7, 20, 80, 20, "AliqIIFisc", "Aliq.II", ListaCampos.DB_SI, null, true );
		
		adicCampo( txtPercCredPresImp, 7, 60, 80, 20, "PercCredPresImp", "% Cred.Pres. ", ListaCampos.DB_SI, null, false );
		
		if(Aplicativo.simples) {
		
			tpnGeral.addTab( "Simples Nacional", panelSimples );
			setPainel( panelCSOSNCampos );

			adicCampo( txtCSOSN, 7, 20, 80, 20, "CSOSN", "CSOSN", ListaCampos.DB_FK, txtDescCSOSN, true );
			adicDescFK( txtDescCSOSN, 90, 20, 500, 20, "DescCSOSN", "Descrição da CSOSN" );
			
			txtCSOSN.setRequerido( true );
			
		}
			
		setListaCampos( true, "ITCLFISCAL", "LF" );
		lcDet.setQueryInsert( false );

		montaTab();

		tab.setTamColuna( 30, 0 ); // Ítem
		tab.setColunaInvisivel( 1 ); // Cod. fisc. cli
		tab.setTamColuna( 150, 2 ); // Desc fisc. cli
		tab.setTamColuna( 30, 3 ); // cod. tipomov
		tab.setTamColuna( 150, 4 ); // desc. tipomo
		tab.setColunaInvisivel( 5 ); // cod. mensagem
		tab.setTamColuna( 150, 6 ); // desc. mensagem
		tab.setTamColuna( 30, 7 ); // Destino
		tab.setTamColuna( 30, 8 ); // Padrão
		tab.setColunaInvisivel( 9 ); // cod.trat.trib.
		tab.setColunaInvisivel( 10 ); // desc.trat.trib.
		tab.setColunaInvisivel( 11 ); // origem
		tab.setColunaInvisivel( 12 ); // situação tributaria
		tab.setColunaInvisivel( 13 ); // tipo de substituição
		tab.setColunaInvisivel( 14 ); // tipo de redução
		tab.setTamColuna( 50, 15 ); // IVA
		tab.setTamColuna( 50, 16 ); // % redução
		tab.setTamColuna( 50, 17 ); // % ICMS
		tab.setTamColuna( 50, 18 ); // % Liv. ICMS
		tab.setTamColuna( 50, 19 ); // % IPI
		tab.setColunaInvisivel( 20 ); // % Cód.sittrib.pis
		tab.setColunaInvisivel( 21 ); // % Imposto PIS
		tab.setColunaInvisivel( 22 ); // % Desc. Sit. Trib. PIS
		tab.setTamColuna( 50, 23 ); // % Aliq. PIS
		tab.setColunaInvisivel( 24 ); // % Cód.sittrib.COFINS
		tab.setColunaInvisivel( 25 ); // % Imposto COFINS
		tab.setColunaInvisivel( 26 ); // % Desc. Sit. Trib. COFINS
		tab.setTamColuna( 50, 27 ); // % Aliq. COFINS

		panelVariantes.add( panelVariantesCampos );
		panelICMS.add( panelICMSCampos );
		panelIPI.add( panelIPICampos );
		panelPIS.add( panelPISCampos );
		panelCOFINS.add( panelCOFINSCampos );
		panelISS.add( panelISSCampos );
		panelIR.add( panelIRCampos );
		panelCSocial.add( panelCSocialCampos );
		panelII.add( panelIICampos );
		panelFUNRURAL.add( panelFUNRURALCampos );
		panelSimples.add(panelCSOSNCampos);
		
		adicListeners();
		
		setTipoFiscCOFPIS();
	}
	
	private void copiarClassificao(){
		
		if(txtCodFisc.getVlrString() == null || "".equals(txtCodFisc.getVlrString()) ){
			Funcoes.mensagemInforma( this, "Selecione uma classificação fiscal." );		
		} else {
			
			dlCopiar = new DLCopiaClassificacao(txtCodFisc.getVlrString());
	    	dlCopiar.setConexao( con );
	    	dlCopiar.setVisible( true );
	    	if(dlCopiar.OK){
	    		lcCampos.setCanedit( false );
	    		txtCodFisc.setVlrString( dlCopiar.getCodfisc() );
	    		lcCampos.carregaDados();
	    		lcCampos.setCanedit(  true );	    		
	    		Funcoes.mensagemInforma( this, "Classificação fiscal copiada com sucesso." );
	
	    		
	    	}
		}
	}

	private void copiarVariante() {

		if ( txtCodItClFiscal.getVlrInteger() == 0 ) {
			Funcoes.mensagemInforma( this, "Selecione o item a ser copiado" );
			return;
		}

		try {

			StringBuilder sql = new StringBuilder();
			sql.append( "INSERT INTO LFITCLFISCAL ( CODEMP, CODFILIAL, CODFISC, " );
			sql.append( "CODITFISC, ORIGFISC, CODEMPTT, CODFILIALTT, TIPOFISC, TPREDICMSFISC, REDFISC, " );
			sql.append( "CODTRATTRIB, NOUFITFISC, CODEMPFC, CODFILIALFC, CODFISCCLI, ALIQFISC, ALIQLFISC, " );
			sql.append( "ALIQIPIFISC, ALIQPISFISC, ALIQCOFINSFISC, CODEMPME, CODFILIALME, CODMENS, " );
			sql.append( "CODEMPTM, CODFILIALTM, CODTIPOMOV, TIPOST, MARGEMVLAGR, GERALFISC, DTINS, " );
			sql.append( "IDUSUINS, DTALT, IDUSUALT, HINS, HALT, CODEMPSP, CODFILIALSP, CODSITTRIBPIS, " );
			sql.append( "IMPSITTRIBPIS, CODEMPSC, CODFILIALSC, CODSITTRIBCOF, IMPSITTRIBCOF, CODEMPSI, " );
			sql.append( "CODFILIALSI, CODSITTRIBIPI, IMPSITTRIBIPI, TPCALCIPI, VLRIPIUNIDTRIB, MODBCICMS, " );
			sql.append( "MODBCICMSST, CODPAIS, SIGLAUF, VLRPISUNIDTRIB, VLRCOFUNIDTRIB) " );
			sql.append( "SELECT CODEMP, CODFILIAL, CODFISC, " );
			sql.append( "(SELECT MAX(CODITFISC)+1 FROM LFITCLFISCAL WHERE CODEMP=I.CODEMP AND CODFILIAL=I.CODFILIAL AND CODFISC=I.CODFISC), " );
			sql.append( "ORIGFISC, CODEMPTT, CODFILIALTT, TIPOFISC, TPREDICMSFISC, REDFISC, " );
			sql.append( "CODTRATTRIB, NOUFITFISC, CODEMPFC, CODFILIALFC, CODFISCCLI, ALIQFISC, ALIQLFISC, " );
			sql.append( "ALIQIPIFISC, ALIQPISFISC, ALIQCOFINSFISC, CODEMPME, CODFILIALME, CODMENS, " );
			sql.append( "CODEMPTM, CODFILIALTM, CODTIPOMOV, TIPOST, MARGEMVLAGR, GERALFISC, DTINS, " );
			sql.append( "IDUSUINS, DTALT, IDUSUALT, HINS, HALT, CODEMPSP, CODFILIALSP, CODSITTRIBPIS, " );
			sql.append( "IMPSITTRIBPIS, CODEMPSC, CODFILIALSC, CODSITTRIBCOF, IMPSITTRIBCOF, CODEMPSI, " );
			sql.append( "CODFILIALSI, CODSITTRIBIPI, IMPSITTRIBIPI, TPCALCIPI, VLRIPIUNIDTRIB, MODBCICMS, " );
			sql.append( "MODBCICMSST, CODPAIS, SIGLAUF, VLRPISUNIDTRIB, VLRCOFUNIDTRIB " );
			sql.append( "FROM LFITCLFISCAL I " );
			sql.append( "WHERE I.CODEMP=? AND I.CODFILIAL=? AND I.CODFISC=? AND I.CODITFISC=? " );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "LFITCLFISCAL" ) );
			ps.setString( 3, txtCodFisc.getVlrString() );
			ps.setInt( 4, txtCodItClFiscal.getVlrInteger() );

			ps.executeUpdate();

			con.commit();

			Funcoes.mensagemInforma( this, "Item copiado com sucesso." );

			lcCampos.carregaDados();

		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao copiar item!\n" + e.getMessage(), true, con, e );
		}

	}

	private void statusLC() {
		StringBuilder texto = new StringBuilder();
		texto.append("Situação lcCampos: ");
		texto.append(lcCampos.getStatus());
		texto.append("\nSituação lcDetalhe: ");
		texto.append(lcDet.getStatus());
		Funcoes.mensagemInforma( this, texto.toString() );
	}
	
	public void actionPerformed( ActionEvent e ) {

		if ( e.getSource() == btCopiarVariante ) {
			copiarVariante();
		} else if (e.getSource() == btCopiar){
			copiarClassificao();
		} else {
			super.actionPerformed( e );
		}
	}

	public void mouseClicked( MouseEvent e ) {

		if ( e.getClickCount() == 2 ) {
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

	}

	public void beforeCarrega( CarregaEvent e ) {
		if ( e.getListaCampos() == lcDet ) {
			carregaImpSitTrib();
		}
	}

	public void afterCarrega( CarregaEvent e ) {

		// System.out.println("Regrafiscal filial "+ ListaCampos.getMasterFilial("LFREGRAFISCAL"));
		if ( e.getListaCampos() == lcCampos ) {
			// aba de serviços
			// tpnGeral.setEnabledAt( 5, txtCodServ.getVlrInteger() > 0 );
			tpnGeral.setSelectedIndex( 0 );
		} 
		else if ( e.getListaCampos() == lcDet ) {
			
		}
		else if ( e.getListaCampos() == lcTratTrib ) {
			// Redução na base de calculo.
			if ( "90".equals( txtCodTratTrib.getVlrString() ) || "20".equals( txtCodTratTrib.getVlrString() ) || "51".equals( txtCodTratTrib.getVlrString() ) || "70".equals( txtCodTratTrib.getVlrString() ) || "00".equals( txtCodTratTrib.getVlrString() ) ) {
				
				rgTpRedIcmsFisc.setAtivo( true );
				cbRedBaseST.setEnabled( true );
				txtRedFisc.setAtivo( true );
				
				if ( "70".equals( txtCodTratTrib.getVlrString() ) ) {
					rgTipoFisc.setVlrString( "FF" );
				}
				else if("51".equals( txtCodTratTrib.getVlrString() )) {
					rgTipoFisc.setVlrString( "II" );
				}
				else {
					rgTipoFisc.setVlrString( "TT" );
				}
				
			}
			else {

				rgTpRedIcmsFisc.setAtivo( false );
				cbRedBaseST.setEnabled( false );
				txtRedFisc.setVlrBigDecimal( new BigDecimal( 0 ) ); 
				txtRedFisc.setAtivo( false );

				// Substituição tributária
				if ( "10".equals( txtCodTratTrib.getVlrString() ) || "70".equals( txtCodTratTrib.getVlrString() ) ) {
					rgTipoST.setAtivo( true );
					rgTipoFisc.setVlrString( "FF" );
				}
				else {
					rgTipoST.setAtivo( false );

					// Tributado integralmente
					if ( "00".equals( txtCodTratTrib.getVlrString() ) ) {
						rgTipoFisc.setVlrString( "TT" );
					}
					// Isento ou não tribut. ou diferimento integral
					else if ( "30".equals( txtCodTratTrib.getVlrString() ) || "40".equals( txtCodTratTrib.getVlrString())) {
						rgTipoFisc.setVlrString( "II" );
						txtAliqFisc.setVlrBigDecimal( new BigDecimal( 0 ) );
					}
					// Não insidência
					else if ( "41".equals( txtCodTratTrib.getVlrString() ) || "50".equals( txtCodTratTrib.getVlrString() ) ) {
						rgTipoFisc.setVlrString( "NN" );
						txtAliqFisc.setVlrBigDecimal( new BigDecimal( 0 ) );
					}
				}
			}
		}
		else if ( e.getListaCampos() == lcSitTribPIS ) {
			if ( "03".equals( txtCodSitTribPIS.getVlrString() ) ) {
				txtVlrPisUnidTrib.setAtivo( true );
				txtAliqPisFisc.setVlrBigDecimal( new BigDecimal( 0 ) );
				txtAliqPisFisc.setAtivo( false );
			}
			else {
				txtAliqPisFisc.setAtivo( true );
				txtVlrPisUnidTrib.setVlrBigDecimal( new BigDecimal( 0 ) );
				txtVlrPisUnidTrib.setAtivo( false );
			}
		}
		else if ( e.getListaCampos() == lcSitTribCOF ) {
			if ( "03".equals( txtCodSitTribCOF.getVlrString() ) ) {
				txtVlrCofUnidTrib.setAtivo( true );
				txtAliqCofinsFisc.setVlrBigDecimal( new BigDecimal( 0 ) );
				txtAliqCofinsFisc.setAtivo( false );
			}
			else {
				txtAliqCofinsFisc.setAtivo( true );
				txtVlrCofUnidTrib.setVlrBigDecimal( new BigDecimal( 0 ) );
				txtVlrCofUnidTrib.setAtivo( false );
			}
		}
		
	}

	public void beforeInsert( InsertEvent e ) {

	}

	public void afterInsert( InsertEvent e ) {
		
		if (e.getListaCampos() == lcDet) {
			txtPercCredPresImp.setVlrInteger(100);	
			cbAdicIPIBaseICMS.setVlrString( "N" );
			cbCalcSTCM.setVlrString( "N" );
			cbAdicICMSTotNota.setVlrString( "".equals( txtAdicICMSTotNotaPrefere.getVlrString().trim() ) ? "N" : txtAdicICMSTotNotaPrefere.getVlrString() );
			txtAliqCSocialFisc.setVlrBigDecimal( BigDecimal.ZERO);
		}
	}

	public void beforePost( PostEvent e ) {

		if ( e.getListaCampos() == lcCampos ) {
			if ( txtCodNCM.getVlrString().trim().length() > 0 && txtCodNBM.getVlrString().trim().length() == 0 ) {
				lcCampos.cancelPost();
				Funcoes.mensagemInforma( this, "A nomenclatura brasileira de mercadorias deve estar amarrada a nomenclatura comum do mercosul!" );
				txtCodNBM.requestFocus();
			}
		}

		super.beforePost( e );
	}

	public void valorAlterado( RadioGroupEvent e ) {

		if ( e.getSource() == rgTipoFisc ) {
			if ( "FF".equals( rgTipoFisc.getVlrString() ) ) { // Caso seja substituição tributária
				rgTipoST.setAtivo( true );
			}
			else {
				rgTipoST.setVlrString( "SI" );
				rgTipoST.setAtivo( false );
			}
		}
		else if ( e.getSource() == rgTipoST || e.getSource() == rgTipoFisc ) {
			if ( /* "SU".equals( rgTipoST.getVlrString() ) && */"FF".equals( rgTipoFisc.getVlrString() ) ) { // Substituído
				txtMargemVlAgr.setAtivo( true );
				cbModBCICMSST.setAtivo( true );
			}
			else {
				txtMargemVlAgr.setVlrBigDecimal( new BigDecimal( 0 ) );
				txtMargemVlAgr.setAtivo( false );
				cbModBCICMSST.setAtivo( false );
			}
		}
		else if ( e.getSource() == rgTipo ) {
			tpnGeral.setEnabledAt( 8, "CP".equals( rgTipo.getVlrString() ) );
			if("VD".equals( rgTipo.getVlrString() )){
				txtAliqCSocialFisc.setVlrBigDecimal( new BigDecimal(0) );
			}
			setTipoFiscCOFPIS();
			txtCodSitTribIPI.setVlrString( null );
			txtDescSitTribIPI.setVlrString( "" );
			txtCodSitTribPIS.setVlrString( null );
			txtDescSitTribPIS.setVlrString( "" );
			txtCodSitTribCOF.setVlrString( null );
			txtDescSitTribCOF.setVlrString( "" );
			//lcCampos.carregaDados();
			//lcSitTribPIS.carregaDados();
			//lcSitTribCOF.carregaDados();
		}
	}

    private void setTipoFiscCOFPIS() {
	    if("CP".equals( rgTipo.getVlrString() )){
	    	txtOperacaoPIS.setVlrString("E");
			txtOperacaoCOF.setVlrString("E");
		} else {
			txtOperacaoPIS.setVlrString("S");
			txtOperacaoCOF.setVlrString("S");
		}
    }
		
	public void valorAlterado( JComboBoxEvent evt ) {

		if ( evt.getComboBoxPad() == cbTpCalcIPI ) {
			if ( "V".equals( cbTpCalcIPI.getVlrString() ) ) {
				txtAliqIPIFisc.setVlrBigDecimal( new BigDecimal( 0 ) );
				txtAliqIPIFisc.setAtivo( false );
				txtVlrIpiUnidTrib.setAtivo( true );
			}
			else if ( "P".equals( cbTpCalcIPI.getVlrString() ) ) {
				txtVlrIpiUnidTrib.setVlrBigDecimal( new BigDecimal( 0 ) );
				txtVlrIpiUnidTrib.setAtivo( false );
				txtAliqIPIFisc.setAtivo( true );
			}
		}
	}

	public void setConexao( DbConnection con ) {

		super.setConexao( con );

		lcRegraFiscal.setConexao( con );
		lcRegraFiscalIt.setConexao( con );
		lcCalcCusto.setConexao( con );
		lcNBM.setConexao( con );
		lcNCM.setConexao( con );
		lcTratTrib.setConexao( con );
		lcMens.setConexao( con );
		lcTipoMov.setConexao( con );
		lcTipoFiscCli.setConexao( con );
		lcSitTribCOF.setConexao( con );
		lcSitTribPIS.setConexao( con );
		lcSitTribIPI.setConexao( con );
		lcSitTribISS.setConexao( con );
		lcPais.setConexao( con );
		lcUF.setConexao( con );
		lcServico.setConexao( con );
		lcCSOSN.setConexao( con );
		lcSpNatOP.setConexao( con );
		
		txtAdicICMSTotNotaPrefere.setVlrString( getAdicICMSTotNota() );

	}

	public void beforeEdit( EditEvent eevt ) {
		if (eevt.getSource()==txtCodSitTribCOF) {
			if (!"".equals( txtCodSitTribCOF.getVlrString() ) ) {
				txtImpSitTribCOF.setVlrString( "CO" );
			}
		} else if (eevt.getSource()==txtCodSitTribCS) {
			if (!"".equals( txtCodSitTribCS.getVlrString() ) ) {
				txtImpSitTribCS.setVlrString( "CS" );
			}
		} else if (eevt.getSource()==txtCodSitTribIPI) {
			if (!"".equals( txtCodSitTribIPI.getVlrString() ) ) {
				txtImpSitTribIPI.setVlrString( "IP" );
			}
		} else if (eevt.getSource()==txtCodSitTribIR) {
			if (!"".equals( txtCodSitTribIR.getVlrString() ) ) {
				txtImpSitTribIR.setVlrString( "IR" );
			}
		} else if (eevt.getSource()==txtCodSitTribISS) {
			if (!"".equals( txtCodSitTribISS.getVlrString() ) ) {
				txtImpSitTribISS.setVlrString( "IS" );
			}
		} else if (eevt.getSource()==txtCodSitTribPIS) {
			if (!"".equals( txtCodSitTribPIS.getVlrString() ) ) {
				txtImpSitTribPIS.setVlrString( "PI" );
			}
		}
	
	}

	public void afterEdit( EditEvent eevt ) {

/*		txtImpSitTribCOF.setVlrString( "CO" );
		txtImpSitTribCS.setVlrString( "CS" );
		txtImpSitTribIPI.setVlrString( "IP" );
		txtImpSitTribIR.setVlrString( "IR" );
		txtImpSitTribISS.setVlrString( "IS" );
		txtImpSitTribPIS.setVlrString( "PI" );
*/

	}

	public void edit( EditEvent eevt ) {

	
	}
	
	private String getAdicICMSTotNota(){

		String adicICMSTotNota = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		sql.append( "SELECT PF.ADICICMSTOTNOTA FROM SGPREFERE1 PF " );
		sql.append( "WHERE PF.CODEMP=? AND PF.CODFILIAL=? " );

		try{
			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "LFITCLFISCAL" ) );
	
			rs = ps.executeQuery();
			if(rs.next()){
				adicICMSTotNota = rs.getString( "ADICICMSTOTNOTA" );
			}
			
			ps.close();
			rs.close();
		}catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return adicICMSTotNota;
	}

	private void carregaImpSitTrib() {
		txtImpSitTribCOF.setVlrString( "CO" );
		txtImpSitTribCS.setVlrString( "CS" );
		txtImpSitTribIPI.setVlrString( "IP" );
		txtImpSitTribIR.setVlrString( "IR" );
		txtImpSitTribISS.setVlrString( "IS" );
		txtImpSitTribPIS.setVlrString( "PI" );
	}
}
