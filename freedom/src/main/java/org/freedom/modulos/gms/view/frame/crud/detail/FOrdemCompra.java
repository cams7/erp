/**
 * @version 11/11/2011 <BR>
 * @author Anderson Sanchez <BR>
 *         Projeto: Freedom <BR>
 *         Pacote: org.freedom.modulos.gms.view.frame.crud.detail <BR>
 *         Classe:
 * @(#)FOrdemCompra.java <BR>
 *                       Este programa é licenciado de acordo com a LPG-PC (Licença Pública Geral para Programas de Computador), <BR>
 *                       modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                       A LPG-PC deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste Programa. <BR>
 *                       Caso uma cópia da LPG-PC não esteja disponível junto com este Programa, você pode contatar <BR>
 *                       sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                       Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                       Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa é preciso estar <BR>
 *                       de acordo com os termos da LPG-PC <BR>
 * <BR>
 *                       Tela para cadastro de ordens de compra.
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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.infra.pojos.Status;
import org.freedom.library.business.object.EmailBean;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDetalhe;
import org.freedom.library.swing.frame.FObservacao;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.util.SwingParams;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.std.DLCodProd;
import org.freedom.modulos.std.view.dialog.utility.DLBuscaProd;
import org.freedom.modulos.std.view.frame.crud.detail.FPlanoPag;
import org.freedom.modulos.std.view.frame.crud.tabbed.FFornecedor;

public class FOrdemCompra extends FDetalhe implements PostListener, CarregaListener, FocusListener, ActionListener, InsertListener, ChangeListener {

	private static final long serialVersionUID = 1L;

	private int casasDec = Aplicativo.casasDec;

	private JPanelPad pinCab = new JPanelPad( 740, 242 );

	private JPanelPad pinBotCab = new JPanelPad( 104, 92 );

	private JPanelPad pinBotDet = new JPanelPad( 104, 93 );

	private JPanelPad pinLb = new JPanelPad();

	private JLabelPad statusitoc = new JLabelPad();

	private JPanelPad pinDet = new JPanelPad();

	private JPanelPad pinDetProgramacao = new JPanelPad( 590, 110 );

	private JButtonPad btAprovaOC = new JButtonPad( "Aprovar", Icone.novo( "btTudo.png" ) );

	private JButtonPad btFinAprovOC = new JButtonPad( "Finaliz. aprov.", Icone.novo( "btFechaVenda.png" ) );

	private JButtonPad btCancelaOC = new JButtonPad( "Cancelar", Icone.novo( "btRetorno1.png" ) );

	private JButtonPad btReprocessaEmpenhos = new JButtonPad( "Reprocessa", Icone.novo( "btOrcamentoGMS.png" ) );

	private JButtonPad btAprovaItemOC = new JButtonPad( "Aprovar", Icone.novo( "btTudo.png" ) );

	private JButtonPad btFinAprovItOC = new JButtonPad( "Finaliz. aprov.", Icone.novo( "btFechaVenda.png" ) );

	private JButtonPad btCancelaItem = new JButtonPad( "Cancelar", Icone.novo( "btRetorno1.png" ) );

	private JTextFieldPad txtCodOrdCP = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDtEmitOrdCp = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDtAprovOrdCp = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodItOrdCp = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtSeqItOrdCpPe = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtQtdItOrdCp = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtQtdApItOrdCp = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtPrecoItOrdCp = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtAliqIpiItOrdCp = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtVlrIpiItOrdCp = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtVlrIpiApItOrdCp = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtVlrLiqItOrdCP = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtVlrLiqApItOrdCP = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtVlrBrutItOrdCP = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtVlrBrutApItOrdCP = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtQtdItPe = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtQtdItEntPe = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtDtItPe = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtRefProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtOrigSolicitacao = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextAreaPad txaJustifCancOC = new JTextAreaPad();

	private JTextAreaPad txaObservacoes = new JTextAreaPad();

	private JTextAreaPad txaJustCancItOc = new JTextAreaPad();

	private JTextAreaPad txaMotivoPrior = new JTextAreaPad();

	private JTextFieldPad txtStatusOC = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtStatusApOC = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtStatusItOC = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtStatusRecItOC = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtStatusApItOC = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtSituacaoItComp = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtStatusRecOC = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtIDUsu = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtCodUnid = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtPrecoBaseProd = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecPre );

	private JRadioGroup<?, ?> rgPriod = null;

	private Vector<String> vLabsTipo = new Vector<String>();

	private Vector<String> vValsTipo = new Vector<String>();

	private JScrollPane spnObservacoes = new JScrollPane( txaObservacoes );

	private JScrollPane spnJustificCanc = new JScrollPane( txaJustifCancOC );

	private JTextFieldPad txtCodFabProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private ListaCampos lcProd = new ListaCampos( this, "PD" );

	private ListaCampos lcProd2 = new ListaCampos( this, "PD" );

	private ListaCampos lcFor = new ListaCampos( this, "FR" );

	private String statusRecOC = txtStatusRecOC.getVlrString();

	private String sOrdSol = "";

	private Integer iCodTpMov = null;

	private boolean bAprovaParcial = false;

	private String SitSol = "";

	private boolean[] bPrefs = null;

	private boolean bAprovaCab = false;

	private int cont = 0;

	private Vector<String> vItem = new Vector<String>();

	private Vector<String> vProdCan = new Vector<String>();

	private Vector<String> vMotivoCan = new Vector<String>();

	private String statusOC;

	private String statusAprovOC;

	private String sSitItExp;

	private JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRazFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldFK txtNomeFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtContFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtFoneFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldFK txtSiglaUFFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtCNPJFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldFK txtEstFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtEmailFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtCodPlanoPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescPlanoPag = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private ListaCampos lcPlanoPag = new ListaCampos( this, "PG" );

	private JTabbedPanePad tpnCab = new JTabbedPanePad();

	private JPanelPad pinCabObsOrdCompra = new JPanelPad();

	private JPanelPad pinCabMotCancOrdCompra = new JPanelPad();

	public static Status STATUS_OC_PEND = new Status( "Pendente", "PE", Color.GRAY );

	public static Status STATUS_OC_CANC = new Status( "Cancelada", "CA", Color.RED );

	public static Status STATUS_OC_AGUAR_RECEB = new Status( "Aguardando Recebimento", "AR", Color.YELLOW );

	public static Status STATUS_OC_EM_RECEB = new Status( "Em Recebimento", "ER", Color.BLUE );

	public static Status STATUS_OC_FINALIZ = new Status( "FInalizada", "FN", SwingParams.COR_VERDE_FREEDOM );

	public static Status STATUS_APROV_OC_TOT = new Status( "Aprovada totalmente", "AT", SwingParams.COR_VERDE_FREEDOM );

	public static Status STATUS_APROV_OC_PARC = new Status( "Aprovada parcialmente", "AP", Color.YELLOW );

	public static Status STATUS_APROV_OC_NAO_APROV = new Status( "Não aprovada", "NA", Color.RED );

	public static Status STATUS_RECEB_OC_TOT = new Status( "Recebida totalmente", "RT", SwingParams.COR_VERDE_FREEDOM );

	public static Status STATUS_RECEB_OC_PARC = new Status( "Recebida parcialmente", "RP", Color.YELLOW );

	public static Status STATUS_RECEB_OC_PEND = new Status( "Recebimento Pendente", "PE", Color.RED );

	private boolean usuario_aprovador = false;

	private JTablePad tabProgramacao = new JTablePad();

	private JScrollPane spProgramacao = new JScrollPane( tabProgramacao );

	private JPanelPad pinDetItens = new JPanelPad( 590, 110 );

	private ListaCampos lcDetProgramacao = new ListaCampos( this );

	private JTabbedPanePad tpnAbas = new JTabbedPanePad();

	private boolean comref = false;

	private String obspadc = "";

	private boolean buscagenericaprod = false;

	public FOrdemCompra() {

		setTitulo( "Ordem de compra" );
		setAtribos( 15, 10, 763, 580 );

		nav.setNavigation( true );

		setImprimir( true );

		desabAprov( true );

		adicAbas();

		getPrefere();

		montaListaCampos();

		montaCabecalho();

		montaDetalhe();

		configCampos();

		adicListeners();

		adicBotoes();

	}

	private void configCampos() {

		txtVlrBrutItOrdCP.setEditable( false );
		txtVlrBrutApItOrdCP.setEditable( false );
		txtVlrIpiItOrdCp.setEditable( false );

		txtVlrIpiApItOrdCp.setEditable( false );
		txtVlrLiqApItOrdCP.setEditable( false );
		txtVlrLiqItOrdCP.setEditable( false );

		txtVlrBrutItOrdCP.setFont( SwingParams.getFontboldmedmax() );
		txtVlrBrutApItOrdCP.setFont( SwingParams.getFontboldmedmax() );
		txtVlrIpiItOrdCp.setFont( SwingParams.getFontboldmedmax() );

		txtVlrIpiApItOrdCp.setFont( SwingParams.getFontboldmedmax() );
		txtVlrLiqApItOrdCP.setFont( SwingParams.getFontboldmedmax() );
		txtVlrLiqItOrdCP.setFont( SwingParams.getFontboldmedmax() );

	}

	private void adicBotoes() {

		// Botões do cabeçalho

		btAprovaOC.setToolTipText( "Aprovar todos os ítens." );
		btFinAprovOC.setToolTipText( "Finaliza Aprovação." );
		btCancelaOC.setToolTipText( "Cancelar todos os ítens." );
		btCancelaItem.setToolTipText( "Cancelar ítem." );
		btReprocessaEmpenhos.setToolTipText( "Reprocessa empenhos" );

		pinCab.adic( pinBotCab, 627, 1, 115, 128 );
		pinBotCab.adic( btAprovaOC, 0, 0, 110, 30 );
		// pinBotCab.adic( btFinAprovOC, 0, 31, 110, 30 );
		pinBotCab.adic( btReprocessaEmpenhos, 0, 31, 110, 30 );
		pinBotCab.adic( btCancelaOC, 0, 62, 110, 30 );

		// Botões dos ítens

		pinDet.adic( pinBotDet, 627, 1, 115, 147 );

		pinBotDet.adic( btAprovaItemOC, 0, 0, 110, 28 );
		pinBotDet.adic( btFinAprovItOC, 0, 30, 110, 28 );
		pinBotDet.adic( btCancelaItem, 0, 60, 110, 28 );

		statusitoc.setForeground( Color.WHITE );

		pinBotDet.adic( statusitoc, 0, 90, 110, 18 );

		// Provisório

		btFinAprovItOC.setEnabled( false );
		btFinAprovOC.setEnabled( false );
		btReprocessaEmpenhos.setEnabled( false );
		btCancelaItem.setEnabled( false );
		btCancelaOC.setEnabled( false );

	}

	private void adicListeners() {

		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		btAprovaOC.addActionListener( this );
		btCancelaOC.addActionListener( this );
		btCancelaItem.addActionListener( this );
		btFinAprovOC.addActionListener( this );
		btReprocessaEmpenhos.addActionListener( this );

		txtQtdItOrdCp.addFocusListener( this );
		lcCampos.addPostListener( this );
		lcCampos.addCarregaListener( this );
		lcProd.addCarregaListener( this );
		lcProd2.addCarregaListener( this );
		lcDet.addPostListener( this );
		lcDet.addCarregaListener( this );
		lcDet.addInsertListener( this );
		lcCampos.addInsertListener( this );

		tpnAbas.addChangeListener( this );

	}

	private void montaListaCampos() {

		lcFor.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.for.", ListaCampos.DB_PK, false ) );
		lcFor.add( new GuardaCampo( txtRazFor, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );
		lcFor.add( new GuardaCampo( txtNomeFor, "NomeFor", "Nome Fantazia", ListaCampos.DB_SI, false ) );
		lcFor.add( new GuardaCampo( txtContFor, "ContFor", "Contato", ListaCampos.DB_SI, false ) );
		lcFor.add( new GuardaCampo( txtFoneFor, "FoneFor", "Fone", ListaCampos.DB_SI, false ) );
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

		String sWhereAdicProd = "ATIVOPROD='S' AND ((SELECT ANOCCUSU||CODCCUSU FROM sgretinfousu(" + Aplicativo.iCodEmp + ",'" + Aplicativo.getUsuario().getIdusu() + "')) IN " + "(SELECT ANOCC||CODCC FROM EQPRODACESSO PA WHERE TIPOPA='RMA' AND PA.codemp=EQPRODUTO.CODEMP AND "
				+ "PA.CODFILIAL=EQPRODUTO.CODFILIAL AND PA.CODPROD=EQPRODUTO.CODPROD) " + "OR " + "((SELECT coalesce(COUNT(1),0) FROM EQPRODACESSO PA WHERE TIPOPA='RMA' AND PA.codemp=EQPRODUTO.CODEMP AND " + "PA.CODFILIAL=EQPRODUTO.CODFILIAL AND PA.CODPROD=EQPRODUTO.CODPROD)=0) " + "OR "
				+ "((SELECT ALMOXARIFE FROM sgretinfousu(" + Aplicativo.iCodEmp + ",'" + Aplicativo.getUsuario().getIdusu() + "'))='S') " + "OR " + "((SELECT APROVARMA FROM sgretinfousu(" + Aplicativo.iCodEmp + ",'" + Aplicativo.getUsuario().getIdusu() + "'))='TD') " + ") ";

		lcProd.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcProd.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtRefProd, "RefProd", "Referência", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtCodFabProd, "CodFabProd", "Código do fabricante", ListaCampos.DB_SI, true ) );
		lcProd.add( new GuardaCampo( txtCodUnid, "CodUnid", "Cód.und.", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtPrecoBaseProd, "PrecoBaseProd", "Preco Base", ListaCampos.DB_SI, false ) );
		lcProd.setWhereAdic( sWhereAdicProd );
		lcProd.montaSql( false, "PRODUTO", "EQ" );
		lcProd.setReadOnly( true );
		txtCodProd.setTabelaExterna( lcProd, FProduto.class.getCanonicalName() );

		lcProd2.add( new GuardaCampo( txtRefProd, "RefProd", "Referência", ListaCampos.DB_PK, false ) );
		lcProd2.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.rod.", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtCodFabProd, "CodFabProd", "Código do fabricante", ListaCampos.DB_SI, true ) );
		lcProd2.add( new GuardaCampo( txtCodUnid, "CodUnid", "Cód.und.", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtPrecoBaseProd, "PrecoBaseProd", "Preco Base", ListaCampos.DB_SI, false ) );
		txtRefProd.setNomeCampo( "RefProd" );
		txtRefProd.setListaCampos( lcDet );
		lcProd2.setWhereAdic( sWhereAdicProd );
		lcProd2.montaSql( false, "PRODUTO", "EQ" );
		lcProd2.setQueryCommit( false );
		lcProd2.setReadOnly( true );
		txtRefProd.setTabelaExterna( lcProd2, FProduto.class.getCanonicalName() );

	}

	private void montaCabecalho() {

		// Ajuste de abas

		pnMaster.remove( spTab );
		pnMaster.remove( pnDet );

		tpnAbas.addTab( "Itens", spTab );
		tpnAbas.addTab( "Programação de entrega", spProgramacao );

		pnMaster.add( tpnAbas, BorderLayout.CENTER );

		pnGImp.removeAll();
		pnGImp.setLayout( new GridLayout( 1, 2 ) );
		pnGImp.setPreferredSize( new Dimension( 100, 26 ) );
		pnGImp.add( btPrevimp );
		pnGImp.add( btImp );

		// Fim do ajuste de abas

		vValsTipo.addElement( "M" );
		vValsTipo.addElement( "A" );
		vLabsTipo.addElement( "Normal" );
		vLabsTipo.addElement( "Urgente" );
		rgPriod = new JRadioGroup<String, String>( 2, 1, vLabsTipo, vValsTipo );
		rgPriod.setVlrString( "M" );

		setListaCampos( lcCampos );
		setAltCab( 180 );
		setPainel( pinCab );

		adicCampo( txtCodOrdCP, 7, 20, 80, 20, "CodOrdCp", "Nro.O.C.", ListaCampos.DB_PK, true );

		adicCampo( txtCodFor, 90, 20, 80, 20, "CodFor", "Cód.for.", ListaCampos.DB_FK, txtRazFor, true );
		adicDescFK( txtRazFor, 173, 20, 250, 20, "RazFor", "Razão social do fornecedor" );
		adicDescFK( txtSiglaUFFor, 426, 20, 20, 20, "UfFor", "UF" );

		adicCampo( txtDtEmitOrdCp, 449, 20, 80, 20, "DtEmitOrdCp", "Dt. Emissão", ListaCampos.DB_SI, true );

		adicCampo( txtCodPlanoPag, 7, 60, 80, 20, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_FK, txtDescPlanoPag, true );
		adicDescFK( txtDescPlanoPag, 90, 60, 356, 20, "DescPlanoPag", "Descrição do plano de pagamento" );

		adicCampo( txtDtAprovOrdCp, 449, 60, 80, 20, "DtApOrdCp", "Dt. Aprovação", ListaCampos.DB_SI, false );

		adicCampoInvisivel( txtStatusApOC, "StatusApOc", "Status de aprovação", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtStatusOC, "StatusOc", "Status", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtStatusRecOC, "StatusRecOc", "Status de recebimento", ListaCampos.DB_SI, false );

		adicDBLiv( txaObservacoes, "ObsOrdCP", "Observações", false );

		adicDBLiv( txaJustifCancOC, "JustifCancOrdCP", "Justificativa de cancelamento", false );

		txtDtAprovOrdCp.setNaoEditavel( true );

		setListaCampos( true, "ORDCOMPRA", "CP" );

		lcCampos.setQueryInsert( false );

	}

	private void adicAbas() {

		pnCliCab.add( tpnCab );

		tpnCab.addTab( "Geral", pinCab );
		tpnCab.addTab( "Observacoes", spnObservacoes );
		tpnCab.addTab( "Justificativa de cancelamento", spnJustificCanc );

	}

	private void montaDetalhe() {

		setAltDet( 165 );
		pinDet = new JPanelPad( 740, 122 );
		setPainel( pinDet, pnDet );
		setListaCampos( lcDet );
		setNavegador( navRod );
		txtCodUnid.setSoLeitura( true );

		lcDetProgramacao.setMaster( lcDet );
		lcDet.adicDetalhe( lcDetProgramacao );

		adicCampo( txtCodItOrdCp, 7, 20, 30, 20, "CodItOrdCp", "Item", ListaCampos.DB_PK, true );

		if ( comref ) {
			adicCampo( txtRefProd, 40, 20, 87, 20, "RefProd", "Referência", ListaCampos.DB_FK, txtDescProd, true );
			adicCampoInvisivel( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_SI, false );
			txtRefProd.setBuscaAdic( new DLBuscaProd( con, "REFPROD", lcProd2.getWhereAdic() ) );
		}
		else {
			adicCampo( txtCodProd, 40, 20, 87, 20, "CodProd", "Cód.prod.", ListaCampos.DB_FK, txtDescProd, true );
			adicCampoInvisivel( txtRefProd, "RefProd", "Referência", ListaCampos.DB_SI, false );
			txtCodProd.setBuscaAdic( new DLBuscaProd( con, "CODPROD", lcProd.getWhereAdic() ) );
		}

		adicDescFK( txtDescProd, 130, 20, 200, 20, "DescProd", "Descrição do produto" );
		adic( txtCodUnid, 333, 20, 35, 20 );

		adicCampo( txtQtdItOrdCp, 371, 20, 70, 20, "QTDITORDCP", "Qtd.Sol.", ListaCampos.DB_SI, true );
		adicCampo( txtQtdApItOrdCp, 444, 20, 70, 20, "QTDAPITORDCP", "Qtd.Aprov.", ListaCampos.DB_SI, true );

		adicCampo( txtPrecoItOrdCp, 517, 20, 60, 20, "PrecoItOrdCp", "Preço", ListaCampos.DB_SI, true );
		adicCampo( txtAliqIpiItOrdCp, 580, 20, 40, 20, "AliqIpiItOrdCp", "% IPI", ListaCampos.DB_SI, true );

		JPanelPad pnSolicita = new JPanelPad();
		pnSolicita.setBorder( SwingParams.getPanelLabel( "Solicitado", Color.RED ) );

		JPanelPad pnAprova = new JPanelPad();
		pnAprova.setBorder( SwingParams.getPanelLabel( "Aprovado", SwingParams.COR_VERDE_FREEDOM ) );

		adic( pnSolicita, 7, 50, 307, 100 );
		adic( pnAprova, 317, 50, 307, 100 );

		setPainel( pnSolicita );

		adicCampo( txtVlrBrutItOrdCP, 7, 30, 90, 30, "VlrBrutItOrdCp", "Valor Bruto", ListaCampos.DB_SI, false );
		adicCampo( txtVlrIpiItOrdCp, 100, 30, 90, 30, "VlrIpiItOrdCp", "Valor IPI", ListaCampos.DB_SI, false );
		adicCampo( txtVlrLiqItOrdCP, 193, 30, 90, 30, "VlrLiqItOrdCp", "Valor Líquido", ListaCampos.DB_SI, false );

		setPainel( pnAprova );

		adicCampo( txtVlrBrutApItOrdCP, 7, 30, 90, 30, "VlrBrutApItOrdCp", "Valor Bruto", ListaCampos.DB_SI, false );
		adicCampo( txtVlrIpiApItOrdCp, 100, 30, 90, 30, "VlrIpiApItOrdCp", "Valor IPI", ListaCampos.DB_SI, false );
		adicCampo( txtVlrLiqApItOrdCP, 193, 30, 90, 30, "VlrLiqApItOrdCp", "Valor Líquido", ListaCampos.DB_SI, false );

		setPainel( pnDet );

		adicCampoInvisivel( txtStatusApItOC, "StatusApItOc", "Status aprovação.", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtStatusRecItOC, "StatusRecItOc", "Status recebimento", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtStatusApItOC, "StatusRecItOc", "Status recebimento", ListaCampos.DB_SI, false );

		adicDBLiv( txaJustCancItOc, "justCancItOc", "Justificativa de cancelamento", false );

		txtRefProd.addKeyListener( new KeyAdapter() {

			public void keyPressed( KeyEvent kevt ) {

				lcDet.edit();
			}
		} );

		setListaCampos( true, "ITORDCOMPRA", "CP" );

		lcDet.setQueryInsert( false );

		montaTab();

		tab.setTamColuna( 250, 2 );

		// Detalhe Programação de entrega

		setPainel( pinDetProgramacao );
		setListaCampos( lcDetProgramacao );
		setNavegador( navRod );

		adicCampo( txtSeqItOrdCpPe, 7, 20, 40, 20, "SEQITORDCPPE", "Seq.", ListaCampos.DB_PK, true );
		adicCampo( txtQtdItPe, 50, 20, 100, 20, "QTDITPE", "Qtd.Prevista", ListaCampos.DB_SI, true );
		adicCampo( txtQtdItEntPe, 153, 20, 100, 20, "QTDITENTPE", "Qtd.Entregue", ListaCampos.DB_SI, true );
		adicCampo( txtDtItPe, 256, 20, 80, 20, "DTITPE", "Data.Prev.", ListaCampos.DB_SI, true );

		setListaCampos( true, "ITORDCOMPRAPE", "CP" );
		lcDetProgramacao.setQueryInsert( false );
		lcDetProgramacao.setTabela( tabProgramacao );
		lcDetProgramacao.montaTab();

		// Fim Detalhe Itens

		navRod.setListaCampos( lcDet );

	}

	private void calculaValores() {

		BigDecimal qtdsol = new BigDecimal( 0 );
		BigDecimal qtdaprov = new BigDecimal( 0 );

		BigDecimal preco = new BigDecimal( 0 );

		BigDecimal vlrbrutosol = new BigDecimal( 0 );
		BigDecimal vlrbrutoaprov = new BigDecimal( 0 );

		BigDecimal Vlrliquidosol = new BigDecimal( 0 );
		BigDecimal Vlrliquidoaprov = new BigDecimal( 0 );

		BigDecimal aliqipi = new BigDecimal( 0 );
		BigDecimal vlripi = new BigDecimal( 0 );
		BigDecimal vlripiaprov = new BigDecimal( 0 );

		try {

			// Carregando valores digitados
			qtdsol = txtQtdItOrdCp.getVlrBigDecimal();
			qtdaprov = txtQtdApItOrdCp.getVlrBigDecimal();
			preco = txtPrecoItOrdCp.getVlrBigDecimal();
			aliqipi = txtAliqIpiItOrdCp.getVlrBigDecimal();

			// Calculando valor bruto solicitado ( preço X qtd )
			vlrbrutosol = qtdsol.multiply( preco );

			// Calculando valor do ipi solicitado (vlrbruto / % ipi )
			vlripi = vlrbrutosol.multiply( aliqipi ).divide( new BigDecimal( 100 ), BigDecimal.ROUND_CEILING );

			// Calculando valor liquido solicitado ( valor bruto + ipi );
			Vlrliquidosol = vlrbrutosol.add( vlripi );

			// Calculando valor bruto aprovado ( preço X qtd )
			vlrbrutoaprov = qtdaprov.multiply( preco );

			// Calculando valor do ipi aprovado (vlrbruto / % ipi )
			vlripiaprov = vlrbrutoaprov.multiply( aliqipi ).divide( new BigDecimal( 100 ), BigDecimal.ROUND_CEILING );

			// Calculando valor liquido aprovado ( valor bruto + ipi );
			Vlrliquidoaprov = vlrbrutoaprov.add( vlripiaprov );

			txtVlrBrutItOrdCP.setVlrBigDecimal( vlrbrutosol );
			txtVlrBrutApItOrdCP.setVlrBigDecimal( vlrbrutoaprov );

			txtVlrIpiItOrdCp.setVlrBigDecimal( vlripi );
			txtVlrIpiApItOrdCp.setVlrBigDecimal( vlripiaprov );

			txtVlrLiqItOrdCP.setVlrBigDecimal( Vlrliquidosol );
			txtVlrLiqApItOrdCP.setVlrBigDecimal( Vlrliquidoaprov );

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	private void buscaInfoUsuAtual() {

		String sql = "select anocc, codcc, codalmox, aprovordcp from sgusuario where codemp=? and codfilial=? and idusu=?";
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = con.prepareStatement( sql );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGUSUARIO" ) );
			ps.setString( 3, Aplicativo.getUsuario().getIdusu() );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				usuario_aprovador = "S".equals( rs.getString( "aprovordcp" ) );
			}

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela de usuários!\n" + err.getMessage() );
		}

	}

	public void focusGained( FocusEvent fevt ) {

	}

	public void focusLost( FocusEvent fevt ) {

	}

	public void beforeCarrega( CarregaEvent cevt ) {

	}

	public void afterPost( PostEvent pevt ) {

	}

	private void desabCampos( boolean bHab ) {

	}

	private void desabAprov( boolean bHab ) {

	}

	public void carregaWhereAdic() {

	}

	public void afterCarrega( CarregaEvent cevt ) {

		if ( cevt.getListaCampos() == lcCampos ) {

			// Se estiver aprovado, aguardando recebimento ou em recebimento, permite o reprocessamento dos empenhos.

			if ( ( STATUS_APROV_OC_PARC.getValue().equals( txtStatusApOC.getVlrString() ) || STATUS_APROV_OC_TOT.getValue().equals( txtStatusApOC.getVlrString() ) )
					&& ( STATUS_OC_AGUAR_RECEB.getValue().equals( txtStatusOC.getVlrString() ) || STATUS_OC_EM_RECEB.getValue().equals( txtStatusOC.getVlrString() ) ) ) {

				btReprocessaEmpenhos.setEnabled( true );

			}
			else {
				btReprocessaEmpenhos.setEnabled( false );
			}

			if ( buscagenericaprod ) {

				if ( comref ) {
					txtRefProd.setBuscaGenProd( new DLCodProd( con, null, txtCodFor.getVlrInteger() ) );
				}
				else {
					txtCodProd.setBuscaGenProd( new DLCodProd( con, null, txtCodFor.getVlrInteger() ) );
				}

			}

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

		else if ( ( cevt.getListaCampos() == lcProd || cevt.getListaCampos() == lcProd2 ) && ( lcDet.getStatus() == ListaCampos.LCS_INSERT ) ) {

			getPrecoItOC();

		}

	}

	public void getPrefere() {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			sql.append( "SELECT P1.USAREFPROD, P1.USABUSCAGENPRODCP, COALESCE(P8.OBSPADOC,'') OBSPADOC FROM SGPREFERE1 P1 " );
			sql.append( "LEFT OUTER JOIN SGPREFERE8 P8 " );
			sql.append( "ON P8.CODEMP=P1.CODEMP AND P8.CODFILIAL=P1.CODFILIAL " );
			sql.append( "WHERE " );
			sql.append( "P1.CODEMP=? AND P1.CODFILIAL=? " );

			ps = Aplicativo.getInstace().getConexao().prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			rs = ps.executeQuery();

			if ( rs.next() ) {

				comref = "S".equals( rs.getString( "UsaRefProd" ) );
				buscagenericaprod = "S".equals( rs.getString( "USABUSCAGENPRODCP" ) );
				obspadc = rs.getString( "obspadoc" );
			}

			Aplicativo.getInstace().getConexao().commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERERENCIAS!\n" + err.getMessage(), true, con, err );
		} finally {
			rs = null;
			ps = null;
		}
	}

	private boolean dialogObsCab() {

		boolean bRet = false;
		FObservacao obs = new FObservacao( txaJustifCancOC.getVlrString() );
		if ( obs != null ) {
			if ( ( !bAprovaCab ) || ( statusRecOC.equals( "CA" ) ) )
				obs.txa.setEnabled( false );
			obs.setVisible( true );
			if ( obs.OK ) {
				txaJustifCancOC.setVlrString( obs.getTexto() );
				bRet = true;
			}
		}
		obs.dispose();
		return bRet;
	}

	private boolean dialogObsDet() {

		boolean bRet = false;
		FObservacao obs = new FObservacao( txaJustCancItOc.getVlrString() );
		if ( obs != null ) {
			if ( ( !bAprovaCab ) || ( statusRecOC.equals( "CA" ) ) )
				obs.txa.setEnabled( false );
			obs.setVisible( true );
			if ( obs.OK ) {
				txaJustCancItOc.setVlrString( obs.getTexto() );
				bRet = true;
			}
		}
		obs.dispose();
		return bRet;
	}

	private boolean dialogObsPrior() {

		boolean bRet = false;
		FObservacao obs = new FObservacao( txaMotivoPrior.getVlrString() );
		if ( obs != null ) {
			if ( ( rgPriod.getVlrString().equals( "A" ) ) && ( txtIDUsu.getVlrString().equals( Aplicativo.getUsuario().getIdusu() ) ) ) {
				obs.txa.setEnabled( true );
			}
			else
				obs.txa.setEnabled( false );
			obs.setVisible( true );
			if ( obs.OK ) {
				txaMotivoPrior.setVlrString( obs.getTexto() );
				bRet = true;
			}
		}
		obs.dispose();
		return bRet;
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW,txtCodOrdCP.getVlrInteger().intValue() );
		}
		else if ( evt.getSource() == btImp ) {
			imprimir( TYPE_PRINT.PRINT, txtCodOrdCP.getVlrInteger().intValue() );
		}
		else if ( evt.getSource() == btCancelaOC ) {
			lcCampos.setState( ListaCampos.LCS_EDIT );
			if ( Funcoes.mensagemConfirma( null, "Deseja cancelar a ordem de compra de todos os itens?" ) == JOptionPane.YES_OPTION ) {
				if ( dialogObsCab() ) {
					txtStatusOC.setVlrString( STATUS_OC_CANC.getValue().toString() );
					lcCampos.post();
				}
			}
		}
		else if ( evt.getSource() == btCancelaItem ) {
			lcDet.setState( ListaCampos.LCS_EDIT );
			if ( Funcoes.mensagemConfirma( null, "Deseja cancelar item da Ordem de Compra?" ) == JOptionPane.YES_OPTION ) {
				if ( dialogObsDet() ) {
					txtStatusItOC.setVlrString( STATUS_OC_CANC.getValue().toString() );
					lcDet.post();
				}
			}
		}

		else if ( evt.getSource() == btAprovaOC ) {
			lcCampos.setState( ListaCampos.LCS_EDIT );
			if ( Funcoes.mensagemConfirma( null, "Deseja Aprovar todos os ítens da Ordem de Compra?\n Caso você não tenha informado as quantidades\n a serem aprovadas" + " estará aprovando as quantidades requeridas!" ) == JOptionPane.OK_OPTION ) {
				txtStatusApOC.setVlrString( STATUS_APROV_OC_TOT.getValue().toString() );
				// Provisório até implementar definitivamente o processo de finalização de aprovação.
				txtStatusOC.setVlrString( STATUS_OC_AGUAR_RECEB.getValue().toString() );
				txtDtAprovOrdCp.setVlrDate( new Date() );
				nav.btSalvar.doClick();
			}
		}
		else if ( evt.getSource() == btFinAprovOC ) {
			lcCampos.setState( ListaCampos.LCS_EDIT );
			if ( Funcoes.mensagemConfirma( null, "Deseja finalizar o processo de aprovação da Ordem de Compra?\n Após este procedimento a Ordem de Compra não poderá mais ser alterada!\n" ) == JOptionPane.OK_OPTION ) {
				txtStatusOC.setVlrString( STATUS_OC_AGUAR_RECEB.getValue().toString() );
				nav.btSalvar.doClick();
			}
		}
		else if ( evt.getSource() == btReprocessaEmpenhos ) {

			lcCampos.setState( ListaCampos.LCS_EDIT );

			if ( Funcoes.mensagemConfirma( null, "Deseja reprocessar os empenhos de pagamento da ordem de compra?" ) == JOptionPane.OK_OPTION ) {

				txtStatusOC.setVlrString( STATUS_OC_EM_RECEB.getValue().toString() );
				txtStatusRecOC.setVlrString( STATUS_RECEB_OC_PARC.getValue().toString() );

				reprocessarEmpenhos();

				nav.btSalvar.doClick();
			}
		}

		super.actionPerformed( evt );
	}

	private void imprimir( TYPE_PRINT bVisualizar, int iCodSol ) {

		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			// Consultando dados
			sql.append( "select " );
			sql.append( "oc.codordcp, oc.codfor, fr.razfor, fr.cnpjfor, fr.cpffor, oc.dtemitordcp, fr.endfor, fr.bairfor, fr.cepfor, " );
			sql.append( "fr.cidfor, fr.uffor, fr.fonefor, fr.dddfonefor, fr.faxfor, fr.inscfor, fr.rgfor, ioc.codprod, ioc.refprod, " );
			sql.append( "pd.descprod, pd.codunid, ioc.qtditordcp, ioc.precoitordcp, ioc.vlrliqapitordcp, ioc.vlrliqitordcp, " );
			sql.append( "ioc.vlripiitordcp, ioc.vlripiapitordcp, fr.contfor, oc.obsordcp, oc.codplanopag, pg.descplanopag, " );
			sql.append( "pd.codfabprod, ioc.qtdapitordcp, oc.dtemitordcp, fr.emailfor, oc.dtapordcp, oc.statusoc, oc.statusapoc, oc.statusrecoc, ioc.vlrbrutitordcp, ioc.vlrbrutapitordcp, ioc.vlripiapitordcp " );
			sql.append( "from cpordcompra oc, cpitordcompra ioc, cpforneced fr, eqproduto pd, fnplanopag pg " );
			sql.append( "where " );
			sql.append( "oc.codemp=? and oc.codfilial=? and oc.codordcp=? and " );
			sql.append( "ioc.codemp=oc.codemp and ioc.codfilial=oc.codfilial and ioc.codordcp=oc.codordcp and " );
			sql.append( "fr.codemp=oc.codempfr and fr.codfilial=oc.codfilialfr and fr.codfor=oc.codfor and " );
			sql.append( "pd.codemp=ioc.codemppd and pd.codfilial=ioc.codfilialpd and pd.codprod=ioc.codprod and " );
			sql.append( "pg.codemp=oc.codemppg and pg.codfilial=oc.codfilialpg and pg.codplanopag=oc.codplanopag  " );

			ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, lcCampos.getCodEmp() );
			ps.setInt( 2, lcCampos.getCodFilial() );
			ps.setInt( 3, txtCodOrdCP.getVlrInteger().intValue() );

			rs = ps.executeQuery();

			// Preparando parametros

			hParam.put( "CODEMP", Aplicativo.iCodEmp );
			hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "CPORDCOMPRA" ) );
			hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
			hParam.put( "ENDEMP", Aplicativo.empresa.getEnderecoCompleto() );
			hParam.put( "FONEFILIAL", Aplicativo.empresa.getFoneFilial() );
			hParam.put( "DDDFILIAL", Aplicativo.empresa.getDDDFilial() );
			hParam.put( "WWWFILIAL", Aplicativo.empresa.getWWWFilial() );
			hParam.put( "EMAILFILIAL", Aplicativo.empresa.getEmailFilial() );
			hParam.put( "DDDFILIAL", Aplicativo.empresa.getDDDFilial() );
			hParam.put( "SUBREPORT_DIR", "org/freedom/layout/oc/" );
			hParam.put( "REPORT_CONNECTION", con.getConnection() );

			hParam.put( "CODORDCP", txtCodOrdCP.getVlrInteger() );

			EmailBean email = Aplicativo.getEmailBean();
			email.setPara( txtEmailFor.getVlrString() );

			dlGr = new FPrinterJob( "layout/oc/OC_PD.jasper", "Ordem de Compra", "", rs, hParam, this, email );
			// dlGr = new FPrinterJob( "layout/oc/OC_PD.jasper", "Ordem de Compra", "", this, hParam, con, email );

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao consultar a tabela de Ordens de Compra!" + err.getMessage(), true, con, err );
		}

		if ( dlGr != null ) {

			if ( bVisualizar==TYPE_PRINT.VIEW ) {
				dlGr.setVisible( true );
			}
			else {
				try {
					JasperPrintManager.printReport( dlGr.getRelatorio(), true );
				} catch ( Exception err ) {
					Funcoes.mensagemErro( this, "Erro na impressão da ordem de compra!" + err.getMessage(), true, con, err );
				}
			}
		}
	}

	public void keyTyped( KeyEvent kevt ) {

		super.keyTyped( kevt );
	}

	public void keyReleased( KeyEvent kevt ) {

		super.keyReleased( kevt );
	}

	public void beforePost( PostEvent pevt ) {

		if ( pevt.getListaCampos() == lcDet ) {

			calculaValores();

		}

		/*
		 * String sMotvProir = rgPriod.getVlrString(); if ( pevt.getListaCampos() == lcDet ) { if ( txtPrecoItOrdCp.getVlrDouble().doubleValue() > txtQtdItOrdCp.getVlrDouble().doubleValue() ) { Funcoes.mensagemInforma( null, "Quantidade aprovada maior que a requerida!" );
		 * pevt.getListaCampos().cancelPost(); } if ( txtSituacaoIt.getVlrString().equals( "" ) ) { txtSituacaoIt.setVlrString( "PE" ); } if ( txtSituacaoItAprov.getVlrString().equals( "" ) ) { txtSituacaoItAprov.setVlrString( "PE" ); } if ( txtSituacaoItComp.getVlrString().equals( "" ) ) {
		 * txtSituacaoItComp.setVlrString( "PE" ); } if ( txtPrecoItOrdCp.getVlrString().equals( "" ) ) { txtPrecoItOrdCp.setVlrBigDecimal( new BigDecimal( 0 ) ); } if ( txtQtdItOrdCp.getVlrString().equals( "" ) ) { txtQtdItOrdCp.setVlrBigDecimal( new BigDecimal( 0 ) ); } if ( sMotvProir.equals( "A"
		 * ) ) { dialogObsPrior(); } } else if ( pevt.getListaCampos() == lcCampos ) { txtOrigSolicitacao.setVlrString( "AX" ); if ( txtStatusSolicitacao.getVlrString().equals( "" ) ) { txtStatusSolicitacao.setVlrString( "PE" ); } if ( txtSituacaoItAprov.getVlrString().equals( "" ) ) {
		 * txtSituacaoItAprov.setVlrString( "PE" ); } if ( txtSituacaoItComp.getVlrString().equals( "" ) ) { txtSituacaoItComp.setVlrString( "PE" ); } }
		 */
	}

	public void beforeInsert( InsertEvent ievt ) {

		// if(ievt.getListaCampos() == lcCampos) {

		// }

	}

	public void afterInsert( InsertEvent ievt ) {

		if ( ievt.getListaCampos() == lcCampos ) {
			txtIDUsu.setVlrString( Aplicativo.getUsuario().getIdusu() );
			txtDtEmitOrdCp.setVlrDate( new Date() );
			txtStatusOC.setVlrString( "PE" );
			txtStatusApOC.setVlrString( "PE" );
			txtStatusRecOC.setVlrString( "PE" );
			txaObservacoes.setVlrString( obspadc );

			lcCampos.carregaDados();
		}
		if ( ievt.getListaCampos() == lcDet ) {
			txtStatusItOC.setVlrString( "PE" );
			txtStatusApItOC.setVlrString( "PE" );
			txtStatusRecItOC.setVlrString( "PE" );

		}

	}

	public void exec( int iCodCompra ) {

		txtCodOrdCP.setVlrString( iCodCompra + "" );
		lcCampos.carregaDados();
	}

	public void execDev( int iCodFor, int iCodTipoMov, String sSerie, int iDoc ) {

		lcCampos.insert( true );
	}

	private int buscaVlrPadrao() {

		int iRet = 0;
		String sSQL = "SELECT ANOCENTROCUSTO FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			ResultSet rs = ps.executeQuery();
			if ( rs.next() )
				iRet = rs.getInt( "ANOCENTROCUSTO" );
			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar o ano-base para o centro de custo.\n" + err.getMessage() );
		}

		return iRet;
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		// lcUnid.setConexao(cn);
		lcProd.setConexao( cn );
		lcProd2.setConexao( cn );
		lcPlanoPag.setConexao( cn );
		lcFor.setConexao( cn );
		lcDetProgramacao.setConexao( cn );

		carregaWhereAdic();

	}

	public void stateChanged( ChangeEvent cevt ) {

		if ( ( (JTabbedPanePad) ( cevt.getSource() ) ) == tpnAbas ) {
			if ( tpnAbas.getSelectedIndex() == 0 ) {
				setAltDet( 200 );
				pnDet.removeAll();
				setPainel( pinDet, pnDet );
				setListaCampos( lcDet );
				pnDet.repaint();
				navRod.setListaCampos( lcDet );
				navRod.setAtivo( 6, true );
			}
			else if ( tpnAbas.getSelectedIndex() == 1 ) {
				setAltDet( 110 );
				pnDet.removeAll();
				setPainel( pinDetProgramacao, pnDet );
				setListaCampos( lcDetProgramacao );
				pnDet.repaint();
				navRod.setListaCampos( lcDetProgramacao );
				navRod.setAtivo( 6, true );
			}

		}

	}

	private void reprocessarEmpenhos() {

		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();

		try {

			sql.append( "execute procedure fnadicpagarsp02(" );
			// codemp, codfilial, codordcp, codemppg, codfilialpg, codplanopag,codempfr, codfilialfr, codfor, obs
			sql.append( "?, ?, ?," );
			sql.append( "?, ?, ?," );
			sql.append( "?, ?, ?," );
			sql.append( "? )" );

			ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "CPORDCOMPRA" ) );
			ps.setInt( 3, txtCodOrdCP.getVlrInteger() );

			ps.setInt( 4, Aplicativo.iCodEmp );
			ps.setInt( 5, ListaCampos.getMasterFilial( "FNPLANOPAG" ) );
			ps.setInt( 6, txtCodPlanoPag.getVlrInteger() );

			ps.setInt( 7, Aplicativo.iCodEmp );
			ps.setInt( 8, ListaCampos.getMasterFilial( "CPFORNECED" ) );
			ps.setInt( 9, txtCodFor.getVlrInteger() );

			ps.setString( 10, txaObservacoes.getVlrString() );

			ps.execute();
			ps.close();

			con.commit();

		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao reprocessar empenhos!\n" + e.getMessage() );
		}
	}

	private void getPrecoItOC() {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			sql.append( "SELECT FIRST 1 IT.PRECOITCOMPRA " );
			sql.append( "FROM CPCOMPRA C, CPITCOMPRA IT " );
			sql.append( "WHERE C.CODEMP=IT.CODEMP AND C.CODFILIAL=IT.CODFILIAL AND C.CODCOMPRA=IT.CODCOMPRA AND IT.CODEMP=? AND IT.CODFILIAL=? AND IT.CODPROD=? " );
			sql.append( "ORDER BY C.DTENTCOMPRA DESC, C.CODCOMPRA DESC" );

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
				txtPrecoItOrdCp.setVlrBigDecimal( rs.getBigDecimal( "precoitcompra" ) );
			}
			else {
				txtPrecoItOrdCp.setVlrBigDecimal( txtPrecoBaseProd.getVlrBigDecimal() );
			}

		} catch ( Exception err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar valor da ultima compra!\n" + err.getMessage(), true, con, err );
		}
	}

}
