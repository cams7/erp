/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Alexandre Rocha Lima e Marcondes <BR>
 *         Projeto: Freedom <BR>
 *         Pacote: org.freedom.modulos.gms <BR>
 *         Classe:
 * @(#)FCompra.java <BR>
 *                  Este programa é licenciado de acordo com a LPG-PC (Licença Pública Geral para Programas de Computador), <BR>
 *                  modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                  A LPG-PC deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste Programa. <BR>
 *                  Caso uma cópia da LPG-PC não esteja disponível junto com este Programa, você pode contatar <BR>
 *                  sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                  Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                  Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa é preciso estar <BR>
 *                  de acordo com os termos da LPG-PC <BR>
 * <BR>
 *                  Tela para cadastro de cotações de preço para compra.
 */

package org.freedom.modulos.gms.view.frame.crud.detail;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDetalhe;
import org.freedom.library.swing.frame.FObservacao;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.gms.business.object.CotacaoPrecos;
import org.freedom.modulos.gms.dao.DAOCotPreco;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.std.view.dialog.report.DLRPedido;
import org.freedom.modulos.std.view.dialog.utility.DLBuscaProd;
import org.freedom.modulos.std.view.frame.crud.detail.FPlanoPag;

public class FCotacaoPrecos extends FDetalhe implements PostListener, CarregaListener, FocusListener, ActionListener, InsertListener {

	private static final long serialVersionUID = 1L;

	private int casasDec = Aplicativo.casasDec;

	private int casasDecFin = Aplicativo.casasDecFin;

	private int casasDecPre = Aplicativo.casasDecPre;
	
	private JPanelPad pinCabGeral = new JPanelPad(new BorderLayout());
	
	private JPanelPad pinCab = new JPanelPad(new BorderLayout());
	
	private JPanelPad pinCabCampos = new JPanelPad( 780, 100);

	private JPanelPad pinBotCab = new JPanelPad(115,0);

	private JPanelPad pinBotDet = new JPanelPad();

	private JPanelPad pinLb = new JPanelPad();

	private JLabelPad lSitItSol = null;

	private JPanelPad pinDet = new JPanelPad();

	private JButtonPad btAprovar = new JButtonPad( "Aprovar", Icone.novo( "btTudo.png" ) );

	private JButtonPad btFinalizar = new JButtonPad( "Finalizar", Icone.novo( "btFechaVenda.png" ) );

	private JButtonPad btComprar = new JButtonPad( "Comprar", Icone.novo( "btMedida.png" ) );

	private JButtonPad btCancelaItem = new JButtonPad( "Cancelar", Icone.novo( "btRetorno.png" ) );

	private JButtonPad btMotivoCancelaItem = new JButtonPad( "Mot.Can", Icone.novo( "btObs1.png" ) );

	private JButtonPad btMotivoAbaixo = new JButtonPad( "Mot.Abaixo", Icone.novo( "btObs1.png" ) );

	private JTextFieldPad txtCodSolicitacao = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDtEmitSol = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodItSolicitacao = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtQtdItAprovado = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtIDUsu = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtRefProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCodProd2 = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 20, 0 );

	private JTextFieldPad txtRefProd2 = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCodCC = new JTextFieldPad( JTextFieldPad.TP_STRING, 19, 0 );

	private JTextFieldFK txtDescCC = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtAnoCC = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtOrigSolicitacao = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtDescProd2 = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtStatusSolicitacao = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtSituacaoItAprov = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtSituacaoCompItAprov = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtSituacaoItComp = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtSituacaoIt = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtCodCot = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtDtCot = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );
	
	private JTextFieldPad txtDtValidCot = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtIdUsuCot = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldFK txtCodUnid = new JTextFieldFK( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldFK txtDescUnid = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtQtdCot = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtQtdAprovCot = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtPrecoCot = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtCodFabProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtNomeUsu = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodCCUsu = new JTextFieldPad( JTextFieldPad.TP_STRING, 19, 0 );

	private JTextFieldPad txtVlrFreteItCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtPercIpiItCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 9, casasDec );

	private JTextFieldPad txtVlrLiqItCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtVlrBaseIpiItCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtVlrIpiItCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextAreaPad txaMotivoCancCot = new JTextAreaPad();

	private JTextAreaPad txaMotivoCotAbaixo = new JTextAreaPad();

	private JTablePad tabCot = new JTablePad();

	private JScrollPane spTabCot = new JScrollPane( tabCot );

	private Navegador navCot = new Navegador( true );
	
	private Navegador navGeral = new Navegador( true );
	
	private JRadioGroup<?, ?> rgPriod = null;

	private Vector<String> vLabsTipo = new Vector<String>();

	private Vector<String> vValsTipo = new Vector<String>();

	private ListaCampos lcUsu = new ListaCampos( this, "UU" );

	private ListaCampos lcProd = new ListaCampos( this, "PD" );

	private ListaCampos lcProd2 = new ListaCampos( this, "PD" );
	
	private ListaCampos lcPlanoPag = new ListaCampos( this, "PP" );

	private ListaCampos lcProd3 = new ListaCampos( this, "PD" );

	private ListaCampos lcProd4 = new ListaCampos( this, "PD" );

	private ListaCampos lcCC = new ListaCampos( this, "CC" );

	private ListaCampos lcFor = new ListaCampos( this, "FR" );

	private ListaCampos lcCotacao = new ListaCampos( this, "" );

	private ListaCampos lcUnid = new ListaCampos( this, "UD" );

	private String sSitItSol = txtSituacaoIt.getVlrString();

	private String sOrdSol = "";
	
	private Integer anoCCPadrao = null;

	private Integer anoCC = null;

	private Integer iCodTpMov = null;

	private String codCC = null;

	private boolean bAprovaParcial = false;

	private String SitSol = "";

	Boolean bUsaRef = null;

	boolean bAprovaCab = false;

	boolean bCotacao = false;
	
	String utilRendaCot = null;

	private int cont = 0;

	private Vector<String> vItem = new Vector<String>();

	private Vector<String> vProdCan = new Vector<String>();

	private Vector<String> vMotivoCan = new Vector<String>();

	private Vector<String> vPrecCan = new Vector<String>();

	private Vector<String> vQtdCan = new Vector<String>();

	private String sSitSol;

	private String sSitItAprov;

	private String sSitItExp;

	private JCheckBoxPad cbUsaRendaCot = new JCheckBoxPad( "Usa renda", "S", "N" );
	
	private JTextFieldPad txtRendaCot = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );
	
	private JTextFieldPad txtCodPlanoPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtDescPlanoPag = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JButtonPad btRecarregaPrecos = new JButtonPad( Icone.novo( "btOrcamento2.gif" ) );
	
	private DAOCotPreco daocotpreco = null;
	
	private Map<String, Object>  params = null;
	
	private Map<String, Object>  prefs = null;
	
	private Map<String, Object>  prefsgms = null;
	
	public FCotacaoPrecos() {

		setTitulo( "Cotação de Preços" );
		setAtribos( 15, 10, 780, 580 );
		
		montaListaCampos();
		montaTela();
		carregaListener();

	}
	
	private void montaListaCampos() {
		
		String sWhereAdicProd = "ATIVOPROD='S' AND ((SELECT ANOCCUSU||CODCCUSU FROM sgretinfousu("+Aplicativo.iCodEmp+",'" + Aplicativo.getUsuario().getIdusu() + "')) IN " 
		+ "(SELECT ANOCC||CODCC FROM EQPRODACESSO PA WHERE TIPOPA='RMA' AND PA.codemp=EQPRODUTO.CODEMP AND "
		+ "PA.CODFILIAL=EQPRODUTO.CODFILIAL AND PA.CODPROD=EQPRODUTO.CODPROD) " + "OR " 
		+ "((SELECT coalesce(COUNT(1),0) FROM EQPRODACESSO PA WHERE TIPOPA='RMA' AND PA.codemp=EQPRODUTO.CODEMP AND " 
		+ "PA.CODFILIAL=EQPRODUTO.CODFILIAL AND PA.CODPROD=EQPRODUTO.CODPROD)=0) " + "OR "
		+ "((SELECT ALMOXARIFE FROM sgretinfousu("+Aplicativo.iCodEmp+",'" + Aplicativo.getUsuario().getIdusu() + "'))='S') " + "OR " 
		+ "((SELECT APROVARMA FROM sgretinfousu("+Aplicativo.iCodEmp+",'" + Aplicativo.getUsuario().getIdusu() + "'))='TD') " + ") ";
	
		lcProd.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcProd.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtRefProd, "RefProd", "Referência", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtCodFabProd, "CodFabProd", "Código do fabricante", ListaCampos.DB_SI, true ) );
		lcProd.add( new GuardaCampo( txtCodUnid, "CodUnid", "Cód.und.", ListaCampos.DB_SI, txtDescUnid, false ) );
	
		lcProd.setWhereAdic( sWhereAdicProd );
		lcProd.montaSql( false, "PRODUTO", "EQ" );
		lcProd.setReadOnly( true );
		txtCodProd.setTabelaExterna( lcProd, null );
	
		lcProd2.add( new GuardaCampo( txtRefProd, "RefProd", "Referência", ListaCampos.DB_PK, false ) );
		lcProd2.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.rod.", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtCodFabProd, "CodFabProd", "Código do fabricante", ListaCampos.DB_SI, true ) );
		lcProd2.add( new GuardaCampo( txtCodUnid, "CodUnid", "Cód.und.", ListaCampos.DB_SI, txtDescUnid, false ) );
	
		txtRefProd.setNomeCampo( "RefProd" );
		txtRefProd.setListaCampos( lcDet );
		lcProd2.setWhereAdic( sWhereAdicProd );
		lcProd2.montaSql( false, "PRODUTO", "EQ" );
		lcProd2.setQueryCommit( false );
		lcProd2.setReadOnly( true );
		txtRefProd.setTabelaExterna( lcProd2, null );
	
		lcProd3.add( new GuardaCampo( txtCodProd2, "CodProd", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcProd3.add( new GuardaCampo( txtDescProd2, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProd3.add( new GuardaCampo( txtRefProd2, "RefProd", "Referência", ListaCampos.DB_SI, false ) );
		lcProd3.montaSql( false, "PRODUTO", "EQ" );
		lcProd3.setReadOnly( true );
		txtCodProd2.setTabelaExterna( lcProd3, null );
	
		lcProd4.add( new GuardaCampo( txtRefProd2, "RefProd", "Referência", ListaCampos.DB_PK, false ) );
		lcProd4.add( new GuardaCampo( txtDescProd2, "DescProd", "Descrição", ListaCampos.DB_SI, false ) );
		lcProd4.add( new GuardaCampo( txtCodProd2, "CodProd", "Cód.rod.", ListaCampos.DB_SI, false ) );
	
		txtRefProd2.setNomeCampo( "RefProd" );
		txtRefProd2.setListaCampos( lcCotacao );
		lcProd4.montaSql( false, "PRODUTO", "EQ" );
		lcProd4.setQueryCommit( false );
		lcProd4.setReadOnly( true );
		txtRefProd2.setTabelaExterna( lcProd4, null );
	
		lcCC.add( new GuardaCampo( txtCodCC, "CodCC", "Código do centro de custos", ListaCampos.DB_PK, false ) );
		lcCC.add( new GuardaCampo( txtAnoCC, "AnoCC", "Ano c.c.", ListaCampos.DB_PK, false ) );
		lcCC.add( new GuardaCampo( txtDescCC, "DescCC", "Descrição do C.Custo", ListaCampos.DB_SI, false ) );
	
		lcCC.montaSql( false, "CC", "FN" );
		lcCC.setQueryCommit( false );
		lcCC.setReadOnly( true );
		txtCodCC.setTabelaExterna( lcCC, null );
		txtAnoCC.setTabelaExterna( lcCC, null );
	
		lcUsu.add( new GuardaCampo( txtIDUsu, "idusu", "Usuário", ListaCampos.DB_PK, false ) );
		lcUsu.add( new GuardaCampo( txtNomeUsu, "nomeusu", "Nome do usuário", ListaCampos.DB_SI, false ) );
		lcUsu.add( new GuardaCampo( txtCodCCUsu, "codcc", "C.Custo Usuário", ListaCampos.DB_SI, false ) );
	
		lcUsu.montaSql( false, "USUARIO", "SG" );
		lcUsu.setQueryCommit( false );
		lcUsu.setReadOnly( true );
		txtIDUsu.setTabelaExterna( lcUsu, null );
	
		lcUnid.add( new GuardaCampo( txtCodUnid, "CodUnid", "Cód.unid.", ListaCampos.DB_PK, false ) );
		lcUnid.add( new GuardaCampo( txtDescUnid, "DescUnid", "Unidade", ListaCampos.DB_SI, false ) );
		lcUnid.montaSql( false, "UNIDADE", "EQ" );
		lcUnid.setReadOnly( true );
		lcUnid.setQueryCommit( false );
		txtCodUnid.setTabelaExterna( lcUnid, null );
	
		lcFor.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.for.", ListaCampos.DB_PK, false ) );
		lcFor.add( new GuardaCampo( txtDescFor, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );
		lcFor.montaSql( false, "FORNECED", "CP" );
		lcFor.setQueryCommit( false );
		lcFor.setReadOnly( true );
		txtCodFor.setTabelaExterna( lcFor, null );
		
		lcPlanoPag.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_PK, false ) );
		lcPlanoPag.add( new GuardaCampo( txtDescPlanoPag, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPag.setWhereAdic( "ATIVOPLANOPAG='S' AND CVPLANOPAG IN ('C','A')" );
		lcPlanoPag.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag.setQueryCommit( false );
		lcPlanoPag.setReadOnly( true );
		txtCodPlanoPag.setTabelaExterna( lcPlanoPag, FPlanoPag.class.getCanonicalName() );
	
		vValsTipo.addElement( "M" );
		vValsTipo.addElement( "A" );
		vLabsTipo.addElement( "Normal" );
		vLabsTipo.addElement( "Urgente" );
		rgPriod = new JRadioGroup<String, String>( 2, 1, vLabsTipo, vValsTipo );
		rgPriod.setVlrString( "M" );
	
	}
	
	private void montaTela() { 
		
		pnMaster.remove( 2 );
		pnGImp.removeAll();
		pnGImp.setLayout( new GridLayout( 1, 3 ) );
		pnGImp.setPreferredSize( new Dimension( 220, 26 ) );
		pnGImp.add( btPrevimp );
		pnGImp.add( btImp );
		
		pnMaster.add( spTab, BorderLayout.CENTER );
		
		setListaCampos( lcCampos );
		setAltCab( 200 ); 
		setPainel( pinCabCampos );
		txtDtEmitSol.setEditable( false );
		lcCampos.setPodeExc( false );
		lcCampos.setPodeIns( false );
		nav.setAtivo( 0, false );
		nav.setAtivo( 1, false );
		nav.setAtivo( 2, false );
		nav.setAtivo( 3, false );
		nav.setAtivo( 4, false );

		adicCampo( txtCodSolicitacao, 7, 20, 60, 20, "CodSol", "Cód.Solic.", ListaCampos.DB_PK, true );
		adicCampo( txtAnoCC, 70, 20, 50, 20, "AnoCC", "Ano CC.", ListaCampos.DB_FK, true );
		adicCampo( txtCodCC, 123, 20, 140, 20, "CodCC", "Cód.CC.", ListaCampos.DB_FK, txtDescCC, true );
		adicDescFK( txtDescCC, 266, 20, 182, 20, "DescCC", "Descrição do centro de custos" );
		adicCampo( txtIDUsu, 451, 20, 80, 20, "IdUsu", "Usuário", ListaCampos.DB_FK, true );
		adicCampo( txtDtEmitSol, 533, 20, 80, 20, "DtEmitSol", "Data da Solic.", ListaCampos.DB_SI, true );
		adicDescFKInvisivel( txtDescCC, "DescCC", "Descrição do c.c." );
		adicCampo( txtStatusSolicitacao, 490, 60, 30, 20, "SitSol", "Sit.Sol.", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtOrigSolicitacao, "OrigSol", "Origem", ListaCampos.DB_SI, false );
		adic( btAprovar, 643, 0, 110, 30 );
		adic( btFinalizar, 643, 31, 110, 30 );
		adic( btComprar, 643, 62, 110, 30 );

		txtIDUsu.setNaoEditavel( true );
		txtDtEmitSol.setNaoEditavel( true );
		txtCodCC.setNaoEditavel( true );
		txtAnoCC.setNaoEditavel( true );

		lcCampos.setWhereAdic( "SITSOL NOT IN ('CA','PE')" );
		setListaCampos( true, "SOLICITACAO", "CP" );
		lcCampos.setQueryInsert( false );

		lcCotacao.setMaster( lcDet );
		lcDet.adicDetalhe( lcCotacao );
		/*
		pinBotCab.adic( btAprovar, 0, 0, 110, 30 );
		pinBotCab.adic( btFinalizar, 0, 31, 110, 30 );
		pinBotCab.adic( btComprar, 0, 62, 110, 30 );
		*/
		pinDet = new JPanelPad( 740, 100 );
		setPainel( pinDet, pnDet );

		setImprimir( true );
		desabAprov( true );
		desabCot( true );
		
		btAprovar.setToolTipText( "Aprovar todos os ítens." );
		btFinalizar.setToolTipText( "Finaliza Aprovação." );
		btComprar.setToolTipText( "Comprar todos os ítens." );
		btCancelaItem.setToolTipText( "Cancelar ítem." );
		btMotivoCancelaItem.setToolTipText( "Motivo do cancelamento do ítem." );
		btMotivoAbaixo.setToolTipText( "Motivo do número de cotações baixo." );
		btRecarregaPrecos.setToolTipText( "Recarrega Preços" );
		
	}

	private void carregaListener() {

		txtQtdItAprovado.addFocusListener( this );
		lcCampos.addPostListener( this );
		lcCampos.addCarregaListener( this );
		lcCotacao.addCarregaListener( this );
		lcCotacao.addPostListener( this );
		lcCotacao.addInsertListener( this );
		lcProd.addCarregaListener( this );
		lcProd2.addCarregaListener( this );
		lcDet.addPostListener( this );
		lcDet.addCarregaListener( this );
		lcDet.addInsertListener( this );
		lcCampos.addInsertListener( this );
		lcUsu.addCarregaListener( this );

		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		btAprovar.addActionListener( this );
		btCancelaItem.addActionListener( this );
		btComprar.addActionListener( this );
		btMotivoCancelaItem.addActionListener( this );
		btMotivoAbaixo.addActionListener( this );
		btFinalizar.addActionListener( this );
		btRecarregaPrecos.addActionListener( this );
		
	}
	
	private void montaDetalhe() {

		setAltDet( 140 );
		setListaCampos( lcDet );
		pnCliCab.add( pinCabGeral );
		pinCab.add( pinCabCampos, BorderLayout.CENTER );		
    	//pinCab.add( pinBotCab, BorderLayout.EAST );
		pinCabGeral.add( pinCab, BorderLayout.NORTH);
		pinCabGeral.add( spTabCot, BorderLayout.CENTER );

		setPainel( pinCabCampos );
	//	setNavegador( navCot );
		
		lcDet.setTabela( tabCot );
	//	lcDet.setNavegador( navCot );
		navCot.setListaCampos( lcDet );

		lcDet.setPodeExc( false );
		lcDet.setPodeIns( false );
		txtCodItSolicitacao.setEditable( false );
		txtCodProd.setEditable( false );
		txtRefProd.setEditable( false );
		txtQtdItAprovado.setEditable( false );

		adicCampo( txtCodItSolicitacao, 7, 60, 30, 20, "CodItSol", "Item", ListaCampos.DB_PK, true );
		if ( comRef() ) {
			adicCampo( txtRefProd, 40, 60, 87, 20, "RefProd", "Referência", ListaCampos.DB_FK, txtDescProd, true );
			adicCampoInvisivel( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_SI, false );
			txtRefProd.setBuscaAdic( new DLBuscaProd( con, "REFPROD", lcProd2.getWhereAdic() ) );
		}
		else {
			adicCampo( txtCodProd, 40, 60, 87, 20, "CodProd", "Cód.prod.", ListaCampos.DB_FK, txtDescProd, true );
			adicCampoInvisivel( txtRefProd, "RefProd", "Referência", ListaCampos.DB_SI, false );
			txtCodProd.setBuscaAdic( new DLBuscaProd( con, "CODPROD", lcProd.getWhereAdic() ) );
		}
		adicDescFK( txtDescProd, 130, 60, 302, 20, "DescProd", "Descrição do produto" );
		adic( txtCodUnid, 435, 60, 50, 20 );
		adicDB( rgPriod, 635, 127, 110, 50, "PriorItSol", "Prioridade:", true );
		rgPriod.setEnabled( false );
		adicCampo( txtQtdItAprovado, 533, 60, 80, 20, "QtdAprovItSol", "Qtd.aprov.", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtSituacaoCompItAprov, "SitCompItSol", "Sit.Comp.It.Sol.", ListaCampos.DB_SI, false );

		lcDet.montaSql( true, "ITSOLICITACAO", "CP" );
		lcDet.setWhereAdic( "SitAprovItSol <> 'NA' AND SitItSol <> 'CA'" );
		lcDet.setQueryInsert( false );
		lcDet.montaTab();
		tabCot.setTamColuna( 30, 0 );
		tabCot.setTamColuna( 60, 1 );
		tabCot.setTamColuna( 200, 2 );
		tabCot.setColunaInvisivel( 3 );
		tabCot.setTamColuna( 70, 4 );
		tabCot.setTamColuna( 70, 5 );
		tabCot.setTamColuna( 70, 6 );
		tabCot.setTamColuna( 70, 7 );
		tabCot.setTamColuna( 70, 8 );
		tabCot.setTamColuna( 70, 9 );
		tabCot.setTamColuna( 70, 10 );

		nav.setName( "Mestre" );
		navCot.setNavigationOnly();
		navCot.setName( "Detalhe 1" );
		navRod.setName( "Detalhe 2" );
		
		FlowLayout flNavCot = new FlowLayout( FlowLayout.LEFT, 0, 0 );
		JPanelPad pnNavCot = new JPanelPad( JPanelPad.TP_JPANEL, flNavCot );
		
		pnNavCot.setBorder( null );
		pnNavCot.add( navCot );
		pnNavCot.add( nav );
		pnNavCab.add( pnNavCot, BorderLayout.WEST );

		setListaCampos( lcCotacao );
		setPainel( pinDet, pnDet );
		setNavegador( navRod );
		navRod.setListaCampos( lcCotacao );
	
		lcCotacao.setNavegador( navRod );
		lcCotacao.setTabela( tab );
		txtQtdItAprovado.setNaoEditavel( true );
		txtRefProd2.setSoLeitura( true );
		txtCodProd2.setSoLeitura( true );

		adicCampo( txtCodCot, 7, 20, 47, 20, "CodCot", "Cot.", ListaCampos.DB_PK, true );
		adicCampo( txtDtCot, 57, 20, 70, 20, "DtCot", "Data ini.", ListaCampos.DB_SI, false );
		adicCampo( txtDtValidCot, 130, 20, 70, 20, "DtValidCot", "Validade", ListaCampos.DB_SI, true );
		adicCampo( txtCodFor, 203, 20, 57, 20, "CodFor", "Cd.For.", ListaCampos.DB_FK, txtDescFor, false );
		adicDescFK( txtDescFor, 263, 20, 191, 20, "RazFor", "Razão social do fornecedor" );
		adicCampo( txtQtdCot, 457, 20, 84, 20, "QtdCot", "Qtd.Cot.", ListaCampos.DB_SI, false );
		adicCampo( txtQtdAprovCot, 544, 20, 82, 20, "QtdAprovCot", "Qtd.Aprov.", ListaCampos.DB_SI, false );
		adicCampo( txtPrecoCot, 7, 60, 87, 20, "PrecoCot", "Preço", ListaCampos.DB_SI, false );
		adicCampo( txtVlrFreteItCompra, 97, 60, 87, 20, "VlrFreteItCompra", "V.Frete", ListaCampos.DB_SI, false );
		adicCampo( txtVlrBaseIpiItCompra, 187, 60, 87, 20, "VlrBaseIpiItCompra", "V.Base.IPI", ListaCampos.DB_SI, false );
		adicCampo( txtPercIpiItCompra, 277, 60, 87, 20, "PercIpiItCompra", "%IPI", ListaCampos.DB_SI, false );
		adicCampo( txtVlrIpiItCompra, 367, 60, 87, 20, "VlrIpiItCompra", "V.IPI", ListaCampos.DB_SI, false );
		adicCampo( txtVlrLiqItCompra, 457, 60, 87, 20, "VlrLiqItCompra", "V.Liq", ListaCampos.DB_SI, true );
		adicCampo( txtCodPlanoPag, 7, 100, 47, 20, "CodPlanoPag", "Cód.pg.", ListaCampos.DB_FK, txtDescPlanoPag, true );
		adicDescFK( txtDescPlanoPag, 57, 100, 216, 20, "DescPlanoPag", "Descrição do plano de pagamento" );
		adicCampo( txtRendaCot, 277, 100, 84, 20, "RendaCot", "Renda", ListaCampos.DB_SI, false );
		adicDB( cbUsaRendaCot, 367, 100, 100, 20, "UsaRendacot", "" , false );
		
		adicCampoInvisivel( txtIdUsuCot, "IdUsuCot", "Usuário", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtSituacaoIt, "SitItSol", "Sit.", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtSituacaoItAprov, "SitAprovItSol", "Ap.", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtSituacaoItComp, "SitCompItSol", ".Comp.", ListaCampos.DB_SI, false );

		lcCotacao.montaSql( true, "COTACAO", "CP" );
		lcCotacao.montaTab();

		tab.setTamColuna( 25, 0 );
		tab.setColunaInvisivel( 1 );
		tab.setTamColuna( 60, 2 );
		tab.setTamColuna( 40, 3 );
		tab.setTamColuna( 170, 4 );
		tab.setTamColuna( 60, 5 );
		tab.setTamColuna( 60, 6 );
		tab.setTamColuna( 50, 7 );
		tab.setTamColuna( 50, 8 );
		tab.setColunaInvisivel( 9 );
		tab.setTamColuna( 30, 10 );
		tab.setTamColuna( 50, 11 );
		tab.setTamColuna( 50, 12 );
		tab.setTamColuna( 50, 13 );
		tab.setTamColuna( 20, 14 );
		tab.setTamColuna( 20, 15 );
		tab.setTamColuna( 20, 16 );
		
		btMotivoAbaixo.setEnabled( false );

		pinBotDet.adic( btCancelaItem, 0, 0, 110, 28 );
		pinBotDet.adic( btMotivoCancelaItem, 0, 29, 110, 28 );
		pinDet.adic( pinBotDet, 630, 1, 114, 63 );
		lSitItSol = new JLabelPad();
		lSitItSol.setForeground( Color.WHITE );
		pinLb.adic( lSitItSol, 31, 0, 110, 20 );
		pinDet.adic( pinLb, 630, 66, 114, 24 );
	}

	private void calcValorLiquido() {

		BigDecimal vlripi = new BigDecimal( 0 );
		BigDecimal vlrfrete = new BigDecimal( 0 );
		BigDecimal vlrpreco = new BigDecimal( 0 );
		BigDecimal vlrliqitcot = new BigDecimal( 0 );
		BigDecimal qtditcot = new BigDecimal( 0 );

		try {

			vlripi = txtVlrIpiItCompra.getVlrBigDecimal();
			vlrfrete = txtVlrFreteItCompra.getVlrBigDecimal();
			vlrpreco = txtPrecoCot.getVlrBigDecimal();
			qtditcot = txtQtdCot.getVlrBigDecimal();

			vlrliqitcot = qtditcot.multiply( vlrpreco );

			vlrliqitcot = vlrliqitcot.add( vlripi );
			vlrliqitcot = vlrliqitcot.add( vlrfrete );

			txtVlrLiqItCompra.setVlrBigDecimal( vlrliqitcot );

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private void buscaInfoUsuAtual() {
		
		Integer codempcc = lcCC.getCodEmp();
		Integer codfilialcc = lcCC.getCodFilial();
		boolean [] buscainfo = null;
		
		try{
		buscainfo = daocotpreco.buscaInfoUsuAtual( bAprovaCab, bCotacao, txtCodCC.getVlrString(), codempcc , codfilialcc );	
		bAprovaCab = buscainfo[0];
		bCotacao = buscainfo[1];
		
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage() );
		}
	}

	public void focusGained( FocusEvent fevt ) {

	}

	public void focusLost( FocusEvent fevt ) {

	}

	public void beforeCarrega( CarregaEvent cevt ) {

	}

	public void afterPost( PostEvent pevt ) {

		if ( pevt.getListaCampos() == lcCampos ) {
			lcCampos.carregaDados();
		}
		if ( pevt.getListaCampos() == lcDet ) {
			lcCampos.carregaDados();
			
		}
		else if( pevt.getListaCampos() == lcCotacao ) {
			try {
			update();
			} catch (Exception e) {
				Funcoes.mensagemErro( this, "Erro na alteração de dados do grid !" );
				e.printStackTrace();
			}
		}
	}

	private void desabCampos( boolean bHab ) {

		txtCodProd.setNaoEditavel( bHab );
		txtQtdCot.setNaoEditavel( bHab );
		txtPrecoCot.setNaoEditavel( bHab );
		txtCodFor.setNaoEditavel( bHab );
		txtQtdAprovCot.setNaoEditavel( bHab );
		rgPriod.setAtivo( !bHab );

	}

	private void desabAprov( boolean bHab ) {

		if ( txtStatusSolicitacao.getVlrString().equals( "AT" ) ) {

			btAprovar.setEnabled( false );

			if ( !txtStatusSolicitacao.getVlrString().equals( "AF" ) ) {
				btFinalizar.setEnabled( true );
			}
			else {
				btFinalizar.setEnabled( false );
			}

		}
		else {
			btAprovar.setEnabled( !bHab );
		}

		btMotivoCancelaItem.setEnabled( txtSituacaoItAprov.getVlrString().equals( "CA" ) );

		btFinalizar.setEnabled( !bHab );
		btCancelaItem.setEnabled( !bHab );
		txtQtdAprovCot.setEnabled( btAprovar.isEnabled() );

	}

	private void desabCot( boolean bHab ) {

		btComprar.setEnabled( !bHab );
	}

	public void carregaWhereAdic() {

		buscaInfoUsuAtual();
		if ( ( bAprovaCab ) || ( bCotacao ) ) {
			if ( bAprovaParcial ) {
				lcCampos.setWhereAdic( "CODCC='" + Aplicativo.getUsuario().getCodcc() + "' AND ANOCC=" + Aplicativo.getUsuario().getAnocc() );
			}
		}
		else {
			lcCampos.setWhereAdic( "IDUSU='" + Aplicativo.getUsuario().getIdusu() + "'" );
		}
	}

	public void afterCarrega( CarregaEvent cevt ) {

		buscaInfoUsuAtual();

		sSitSol = txtStatusSolicitacao.getVlrString();
		sSitItAprov = txtSituacaoItAprov.getVlrString();
		sSitItExp = txtSituacaoItComp.getVlrString();
		sSitItSol = txtSituacaoIt.getVlrString();

		boolean bStatusTravaTudo = ( sSitItExp.equals( "AF" ) || ( sSitSol.equals( "CF" ) ) || sSitSol.equals( "EF" ) || sSitItExp.equals( "EF" ) || sSitItSol.equals( "CA" ) || sSitItExp.equals( "CA" ) );
		boolean bCot = bCotacao && sSitItExp.equals( "CF" );

		if ( cevt.getListaCampos() == lcDet ) {
			if ( comRef() ) {
				txtRefProd2.setVlrString( txtRefProd.getVlrString() );
				lcProd4.carregaDados();
			}
			else {
				txtCodProd2.setVlrString( txtCodProd.getVlrString() );
				lcProd3.carregaDados();
			}

			if ( sSitItExp.equals( "CA" ) ) {
				desabCampos( true );
				btMotivoCancelaItem.setEnabled( true );
			}
			// btProduto.setEnabled(!txtCodProd.getVlrString().equals(""));
		}

		if ( !txtIDUsu.getVlrString().equals( Aplicativo.getUsuario().getIdusu() ) && !bCotacao || bStatusTravaTudo )
			desabCampos( true );
		else
			desabCampos( false );

		if ( !bAprovaCab || bStatusTravaTudo ) {
			desabAprov( true );
		}
		else {
			desabAprov( false );
		}

		if ( bCot )
			desabCot( false );
		else
			desabCot( true );

		if ( sSitItSol.equals( "CA" ) ) {
			SitSol = "Cancelado";
			lSitItSol.setText( SitSol );
			pinLb.setBackground( cor( 250, 50, 50 ) );
		}
		else if ( sSitItSol.equals( "PE" ) || sSitItExp.equals( "PE" ) ) {
			SitSol = "Pendente";
			lSitItSol.setText( SitSol );
			pinLb.setBackground( cor( 255, 204, 51 ) );
		}
		else if ( sSitItSol.equals( "ET" ) || sSitItExp.equals( "EP" ) ) {
			SitSol = "Cotado";
			lSitItSol.setText( SitSol );
			pinLb.setBackground( cor( 0, 170, 30 ) );
		}
		else if ( sSitItAprov.equals( "AT" ) || sSitItAprov.equals( "AP" ) ) {
			SitSol = "Aprovado";
			lSitItSol.setText( SitSol );
			pinLb.setBackground( cor( 26, 140, 255 ) );
		}

		if ( ( cevt.getListaCampos() == lcProd ) || ( cevt.getListaCampos() == lcProd2 ) ) {
			txtCodUnid.atualizaFK();
		}

		if ( cevt.getListaCampos() == lcDet ) {
			if ( txtQtdItAprovado.isEditable() ) {
				if ( txtQtdAprovCot.getVlrBigDecimal().compareTo( new BigDecimal( 0 ) ) <= 0 )
					txtQtdAprovCot.setVlrBigDecimal( txtQtdItAprovado.getVlrBigDecimal() );
			}
		}
	}

	private boolean dialogObsDet() {

		boolean bRet = false;
		FObservacao obs = new FObservacao( txaMotivoCancCot.getVlrString() );
		if ( obs != null ) {
			if ( ( !bAprovaCab ) || ( sSitItExp.equals( "CA" ) ) )
				obs.txa.setEnabled( false );
			obs.setVisible( true );
			if ( obs.OK ) {
				txaMotivoCancCot.setVlrString( obs.getTexto() );
				bRet = true;
			}
		}
		obs.dispose();
		return bRet;
	}

	private boolean dialogObsAbaixo() {

		boolean bRet = false;
		FObservacao obs = new FObservacao( txaMotivoCotAbaixo.getVlrString() );
		if ( obs != null ) {
			if ( ( rgPriod.getVlrString().equals( "A" ) ) && ( txtIDUsu.getVlrString().equals( Aplicativo.getUsuario().getIdusu() ) ) ) {
				obs.txa.setEnabled( true );
			}
			else
				obs.txa.setEnabled( false );
			obs.setVisible( true );
			if ( obs.OK ) {
				txaMotivoCotAbaixo.setVlrString( obs.getTexto() );
				bRet = true;
			}
		}
		obs.dispose();
		return bRet;
	}

	private void abreProd() {

		int iCodOrc = txtCodProd.getVlrInteger().intValue();
		if ( fPrim.temTela( "Cadastro de Produtos" ) == false ) {
			FProduto tela = new FProduto();
			fPrim.criatela( "Cadastro de Produtos", tela, con );
			tela.exec( iCodOrc );
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp )
			imprimir( TYPE_PRINT.VIEW,txtCodSolicitacao.getVlrInteger().intValue() );
		else if ( evt.getSource() == btImp )
			imprimir( TYPE_PRINT.PRINT, txtCodSolicitacao.getVlrInteger().intValue() );
		else if ( evt.getSource() == btMotivoCancelaItem ) {
			dialogObsDet();
		}
		else if ( evt.getSource() == btMotivoAbaixo ) {
			dialogObsAbaixo();
		}
		else if ( evt.getSource() == btCancelaItem ) {
			lcCotacao.setState( ListaCampos.LCS_EDIT );
			if ( Funcoes.mensagemConfirma( null, "Deseja cancelar ítem da compra?" ) == JOptionPane.YES_OPTION ) {
				if ( dialogObsDet() ) {
					txtSituacaoItComp.setVlrString( "CA" );
					lcCotacao.post();
				}
			}
		}

		else if ( evt.getSource() == btAprovar ) {
			if ( Funcoes.mensagemConfirma( null, "Deseja Aprovar todos os ítens da compra?\n Caso você não tenha informado as quantidades\n a serem aprovadas" 
					+ " estará aprovando as quantidades requeridas!" ) == JOptionPane.OK_OPTION ) {		
				lcCampos.setState( ListaCampos.LCS_EDIT );
				txtStatusSolicitacao.setVlrString( "CT" );
				lcCampos.post();
			}
		}


  		else if ( evt.getSource() == btFinalizar ) {
 
			if ( Funcoes.mensagemConfirma( null, "Deseja finalizar o processo de aprovação da compra?\n Após este procedimento a compra não poderá mais ser alterada\n" 
					+ "e estará disponível para expedição da nota fiscal!" ) == JOptionPane.OK_OPTION ) {
				;
				lcCampos.setState( ListaCampos.LCS_EDIT );
				txtStatusSolicitacao.setVlrString( "CF" );
				lcCampos.post();
			}
		}

		else if ( evt.getSource() == btComprar ) {
			if ( Funcoes.mensagemConfirma( null, "Deseja cotar todos os ítens da solicitação de compra?\n Caso você não tenha informado as quantidades\n a serem cotadas" 
					+ " estará cotando as quantidades aprovadas!" ) == JOptionPane.OK_OPTION ) {
				;
				lcCampos.setState( ListaCampos.LCS_EDIT );
				txtStatusSolicitacao.setVlrString( "EF" );
				lcCampos.post();
			}
		}
		else if(evt.getSource() == btRecarregaPrecos) {
			try{
				update();
			}catch (Exception e) {
				Funcoes.mensagemErro( this, "Erro na alteração de dados do grid !" );
				e.printStackTrace();
			}
		}
		super.actionPerformed( evt );
	}
	
	private void update() throws Exception {
			
		CotacaoPrecos cot = new CotacaoPrecos();
		
		if (txtCodFor.getVlrInteger().intValue()!= 0) {	
			cot.setCodempfr( Aplicativo.iCodEmp );
			cot.setCodfilialfr( ListaCampos.getMasterFilial( "CPFORNECED" ) );
			cot.setCodfor( txtCodFor.getVlrInteger() );
		}
		if (txtCodPlanoPag.getVlrInteger().intValue()!= 0) {
			cot.setCodemppg( Aplicativo.iCodEmp );
			cot.setCodfilialpg( ListaCampos.getMasterFilial( "FNPLANOPAG" ) );
			cot.setCodplanpag( txtCodPlanoPag.getVlrInteger() );
		}
		
		cot.setDtcot( txtDtCot.getVlrDate() );
		cot.setDtvalidcot( txtDtValidCot.getVlrDate() );
		
		if(txtCodProd2.getVlrInteger().intValue() != 0) {
			cot.setCodemppd( Aplicativo.iCodEmp );
			cot.setCodfilialpd( ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			cot.setCodprod( txtCodProd2.getVlrInteger() );
		}
		
		if( txtPrecoCot.getVlrInteger().intValue() != 0 ) {
			cot.setPrecocot( txtPrecoCot.getVlrBigDecimal() );
		}
		cot.setAprovpreco( "S" );
		
		cot.setUsarendacot( cbUsaRendaCot.getVlrString() );
		
		cot.setRenda( txtRendaCot.getVlrInteger() );
		
		daocotpreco.recarregaPrecosPedidos( cot );
		
	}

	private void imprimir( TYPE_PRINT bVisualizar, int iCodSol ) {

		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		DLRPedido dl = new DLRPedido( sOrdSol, "I.CODITSOL", false );
		dl.setVisible( true );
		if ( dl.OK == false ) {
			dl.dispose();
			return;
		}
		String ordem = dl.getValor();
		if (! "I.CODITSOL".equalsIgnoreCase( ordem )) {
			ordem = "P."+ordem;
		}
		imp.verifLinPag();
		imp.montaCab();

		imp.setTitulo( "Relatório de Cotação de Preços" );

		StringBuilder sql = new StringBuilder();
		sql.append( "SELECT  (SELECT COUNT(IT.CODITSOL) FROM CPITSOLICITACAO IT " );
		sql.append("WHERE IT.CODEMP=R.CODEMP AND IT.CODFILIAL = R.CODFILIAL AND IT.CODSOL=R.CODSOL), ");
		sql.append("R.CODSOL,R.DTINS,R.SITSOL,R.IDUSU,R.MOTIVOSOL,R.IDUSUAPROV,R.DTAAPROVSOL,R.MOTIVOCANCSOL, ");
		sql.append("I.CODPROD, I.QTDITSOL, I.QTDAPROVITSOL, I.SITITSOL," + "I.SITITSOL,I.SITAPROVITSOL,I.CODITSOL, ");
		sql.append("P.REFPROD,P.DESCPROD, P.CODUNID," + "A.CODALMOX, A.DESCALMOX, CC.CODCC, CC.ANOCC, CC.DESCCC, ");
		sql.append("(SELECT U.CODCC FROM SGUSUARIO U WHERE U.IDUSU=R.IDUSUAPROV), ");
		sql.append("(SELECT C.DESCCC FROM FNCC C, SGUSUARIO U " );
		sql.append("WHERE C.CODEMP=U.CODEMPCC AND C.CODFILIAL=U.CODEMPCC AND C.ANOCC=U.ANOCC ");
		sql.append("AND C.CODCC=U.CODCC AND U.IDUSU=R.IDUSUAPROV), " );
		sql.append("I.MOTIVOCANCITSOL, I.CODPROD, C.DTCOT, C.IDUSUCOT, C.CODEMPFR, C.CODFILIALFR, C.CODFOR, ");
		sql.append("C.QTDCOT, C.QTDAPROVCOT, C.PRECOCOT, C.SITCOMPITSOL AS SITCOMPITCOT, C.SITAPROVITSOL AS SITAPROVITCOT, ");
		sql.append("C.SITITSOL AS SITITCOT, C.MOTIVOCANCCOT, C.MOTIVOCOTABAIXO, F.RAZFOR, F.NOMEFOR, F.PESSOAFOR, F.CNPJFOR, F.INSCFOR, ");
		sql.append("F.CPFFOR, F.RGFOR, F.FONEFOR, F.FAXFOR, F.CELFOR, F.EMAILFOR, F.OBSFOR FROM CPCOTACAO C, CPSOLICITACAO R, CPITSOLICITACAO I, ");
		sql.append("EQALMOX A, FNCC CC, EQPRODUTO P, CPFORNECED F WHERE R.CODEMP=? AND R.CODFILIAL=? AND R.CODSOL=? ");
		sql.append("AND C.CODEMP=R.CODEMP AND C.CODFILIAL=R.CODFILIAL AND C.CODSOL=R.CODSOL AND C.SITAPROVITSOL <> 'NA' AND C.SITITSOL <> 'CA' ");
		sql.append("AND R.SITSOL <> 'NA' AND R.SITSOL <> 'CA' AND F.CODEMP=C.CODEMPFR AND F.CODFILIAL=C.CODFILIALFR AND F.CODFOR=C.CODFOR ");
		sql.append("AND I.CODEMP=R.CODEMP AND I.CODFILIAL=R.CODFILIAL AND I.CODSOL=R.CODSOL AND P.CODEMP=I.CODEMPPD AND P.CODFILIAL=I.CODFILIALPD AND P.CODPROD=I.CODPROD ");
		sql.append("AND I.CODEMP=R.CODEMP AND I.CODFILIAL=R.CODFILIAL AND CC.CODEMP=R.CODEMPCC AND CC.CODFILIAL=R.CODFILIALCC AND CC.CODCC=R.CODCC " );
		sql.append("AND A.CODEMP=I.CODEMPAM AND A.CODFILIAL=I.CODFILIALAM AND A.CODALMOX=I.CODALMOX ORDER BY R.CODSOL," + ordem );
		sql.append(";");

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, lcCampos.getCodEmp() );
			ps.setInt( 2, lcCampos.getCodFilial() );
			ps.setInt( 3, txtCodSolicitacao.getVlrInteger().intValue() );

			rs = ps.executeQuery();

			imp.limpaPags();

			int codItSol = 0;

			while ( rs.next() ) {
				if ( imp.pRow() >= ( linPag - 1 ) ) {
					imp.incPags();
					imp.eject();
				}
				if ( imp.pRow() == 0 ) {
					imp.impCab( 137, true );
					imp.say( imp.pRow() + 0, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "|" + StringFunctions.replicate( "=", 134 ) + "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 4, "Sol. Comp. No.: " );
					imp.say( imp.pRow() + 0, 19, rs.getString( "CODSOL" ) );
					imp.say( imp.pRow() + 0, 136, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 4, "Requisitante: " );
					imp.say( imp.pRow() + 0, 19, rs.getString( "IDUSU" ) );
					imp.say( imp.pRow() + 0, 30, "- C.C.: " );
					imp.say( imp.pRow() + 0, 38, ( rs.getString( "CODCC" ) != null ? rs.getString( "CODCC" ).trim() : "" ) );
					imp.say( imp.pRow() + 0, 62, "-" + ( rs.getString( "DESCCC" ) != null ? rs.getString( "DESCCC" ).trim() : "" ) );
					imp.say( imp.pRow() + 0, 113, "- Data : " );
					imp.say( imp.pRow() + 0, 123, StringFunctions.sqlDateToStrDate( rs.getDate( "DTINS" ) ) );
					imp.say( imp.pRow() + 0, 136, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 4, "Aprovação   : " );
					imp.say( imp.pRow() + 0, 19, rs.getString( "IDUSUAPROV" ) );
					imp.say( imp.pRow() + 0, 113, "- Data : " );
					imp.say( imp.pRow() + 0, 123, StringFunctions.sqlDateToStrDate( rs.getDate( "DTAAPROVSOL" ) ) );
					imp.say( imp.pRow() + 0, 136, "|" );

				}

				if ( rs.getInt( "CODITSOL" ) != codItSol ) {
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "|" + StringFunctions.replicate( "=", 134 ) + "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 57, "DADOS DO(S) PRODUTO(S)" );
					imp.say( imp.pRow() + 0, 136, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 136, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 2, "Item" );
					imp.say( imp.pRow() + 0, 8, "Referencia" );
					imp.say( imp.pRow() + 0, 22, "Descrição dos produtos" );
					imp.say( imp.pRow() + 0, 60, "Qtd.aprov." );
					imp.say( imp.pRow() + 0, 100, "Sit.item" );
					imp.say( imp.pRow() + 0, 110, "Sit.aprov." );
					imp.say( imp.pRow() + 0, 136, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 136, "|" );

					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );

					codItSol = rs.getInt( "CODITSOL" );
					imp.say( imp.pRow() + 0, 2, rs.getString( "CODITSOL" ) );
					imp.say( imp.pRow() + 0, 8, rs.getString( "REFPROD" ) );
					imp.say( imp.pRow() + 0, 22, rs.getString( "DESCPROD" ).substring( 0, 37 ) );
					imp.say( imp.pRow() + 0, 60, "" + rs.getBigDecimal( "QTDAPROVITSOL" ) );
					imp.say( imp.pRow() + 0, 105, "" + rs.getString( "SITITSOL" ) );
					imp.say( imp.pRow() + 0, 115, "" + rs.getString( "SITAPROVITSOL" ) );
					imp.say( imp.pRow() + 0, 136, "|" );

					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 136, "|" );

					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 57, "DADOS DO(S) FORNECEDOR(ES)" );
					imp.say( imp.pRow() + 0, 136, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 136, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 2, "Cod.For." );
					imp.say( imp.pRow() + 0, 8, "Nome" );
					imp.say( imp.pRow() + 0, 60, "Fone" );
					imp.say( imp.pRow() + 0, 78, "Fax" );
					imp.say( imp.pRow() + 0, 96, "Cel." );
					imp.say( imp.pRow() + 0, 114, "Preco" );
					imp.say( imp.pRow() + 0, 127, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 136, "|" );
				}

				if ( ( rs.getString( "SITITCOT" ).equals( "CA" ) ) || ( rs.getString( "SITAPROVITCOT" ).equals( "NA" ) ) ) {
					vProdCan.addElement( rs.getString( "NOMEFOR" ) );
					vPrecCan.addElement( rs.getString( "PRECOCOT" ) );
					vQtdCan.addElement( rs.getString( "QTDCOT" ) );
					vItem.addElement( rs.getString( "CODITSOL" ) );
					vMotivoCan.addElement( rs.getString( "MOTIVOCANCCOT" ) != null ? rs.getString( "MOTIVOCANCCOT" ) : "" );
					cont++;
				}

				imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
				imp.say( imp.pRow() + 0, 1, imp.comprimido() + "|" );
				imp.say( imp.pRow() + 0, 2, rs.getString( "CODFOR" ) );
				imp.say( imp.pRow() + 0, 8, rs.getString( "NOMEFOR" ) );
				imp.say( imp.pRow() + 0, 12, rs.getString( "FONEFOR" ) );
				imp.say( imp.pRow() + 0, 28, rs.getString( "FAXFOR" ) );
				imp.say( imp.pRow() + 0, 36, rs.getString( "CELFOR" ) );
				imp.say( imp.pRow() + 0, 64, "" + rs.getBigDecimal( "PRECOCOT" ) );
				imp.say( imp.pRow() + 0, 78, "|" );

			}
			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 0, 0, "|" + StringFunctions.replicate( "=", 134 ) + "|" );
			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 0, 1, "|" );
			imp.say( imp.pRow() + 0, 57, "INFORMAÇÕES ADICIONAIS" );
			imp.say( imp.pRow() + 0, 136, "|" );
			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 0, 1, "|" );
			imp.say( imp.pRow() + 0, 136, "|" );
			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 0, 1, "|" );
			imp.say( imp.pRow() + 0, 2, "MOTIVO DA SOLICITAÇÃO: " );
			String sMotivoRMA = ( rs.getString( "MOTIVOSOL" ) != null ? rs.getString( "MOTIVOSOL" ) : "" ).trim();
			imp.say( imp.pRow() + 0, 26, sMotivoRMA.substring( 0, sMotivoRMA.length() > 109 ? 109 : sMotivoRMA.length() ) );
			imp.say( imp.pRow() + 0, 136, "|" );
			if ( cont > 0 ) {
				imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
				imp.say( imp.pRow() + 0, 1, "|" );
				imp.say( imp.pRow() + 0, 4, "COTAÇÕES NÃO APROVADAS:" );
				imp.say( imp.pRow() + 0, 136, "|" );
				imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
				imp.say( imp.pRow() + 0, 1, "|" );
				imp.say( imp.pRow() + 0, 2, "Item" );
				imp.say( imp.pRow() + 0, 19, "Nome" );
				imp.say( imp.pRow() + 0, 60, "Preco" );
				imp.say( imp.pRow() + 0, 78, "Qtd." );
				imp.say( imp.pRow() + 0, 96, "Motivo" );
				imp.say( imp.pRow() + 0, 136, "|" );
				imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
				imp.say( imp.pRow() + 0, 1, "|" );
				imp.say( imp.pRow() + 0, 136, "|" );
				for ( int i = 0; vProdCan.size() > i; i++ ) {
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 2, vItem.elementAt( i ).toString() );
					imp.say( imp.pRow() + 0, 19, vProdCan.elementAt( i ).toString() );
					imp.say( imp.pRow() + 0, 60, vPrecCan.elementAt( i ).toString() );
					imp.say( imp.pRow() + 0, 78, vProdCan.elementAt( i ).toString() );
					String sMotivoCanc = vMotivoCan.elementAt( i ).toString();

					imp.say( imp.pRow() + 0, 96, "- " + sMotivoCanc.substring( 0, sMotivoCanc.length() > 108 ? 108 : sMotivoCanc.length() ) );
					imp.say( imp.pRow() + 0, 136, "|" );
				}
			}

			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 0, 0, "+" + StringFunctions.replicate( "=", 134 ) + "+" );
			imp.say( imp.pRow() + 2, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 0, 52, StringFunctions.replicate( "_", 41 ) );
			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 0, 62, "Ass. do funcionário" );

			imp.eject();

			imp.fechaGravacao();

			con.commit();
			dl.dispose();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao consultar a tabela de Cotações!" + err.getMessage(), true, con, err );
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
	}

	private boolean comRef() {

		return bUsaRef;
	}

	public void keyTyped( KeyEvent kevt ) {

		super.keyTyped( kevt );
	}

	public void keyReleased( KeyEvent kevt ) {

		super.keyReleased( kevt );
	}

	public void beforePost( PostEvent pevt ) {

		if ( pevt.getListaCampos() == lcCotacao ) {
			if ( txtQtdAprovCot.getVlrDouble().doubleValue() > txtQtdCot.getVlrDouble().doubleValue() ) {
				Funcoes.mensagemInforma( null, "Quantidade aprovada maior que a cotada!" );
				pevt.getListaCampos().cancelPost();
			}
			if ( txtSituacaoIt.getVlrString().equals( "" ) ) {
				txtSituacaoIt.setVlrString( "PE" );
			}
			if ( txtSituacaoItAprov.getVlrString().equals( "" ) ) {
				txtSituacaoItAprov.setVlrString( "PE" );
			}
			if ( txtSituacaoItComp.getVlrString().equals( "" ) ) {
				txtSituacaoItComp.setVlrString( "PE" );
			}
			if ( txtQtdItAprovado.getVlrString().equals( "" ) ) {
				txtQtdItAprovado.setVlrBigDecimal( new BigDecimal( 0 ) );
			}
		}
		else if ( pevt.getListaCampos() == lcCampos ) {
			txtOrigSolicitacao.setVlrString( "AX" );
			if ( txtStatusSolicitacao.getVlrString().equals( "" ) ) {
				txtStatusSolicitacao.setVlrString( "PE" );
			}
			if ( txtSituacaoItAprov.getVlrString().equals( "" ) ) {
				txtSituacaoItAprov.setVlrString( "PE" );
			}
			if ( txtSituacaoItComp.getVlrString().equals( "" ) ) {
				txtSituacaoItComp.setVlrString( "PE" );
			}
		}
	}

	public void beforeInsert( InsertEvent ievt ) {

	}

	public void afterInsert( InsertEvent ievt ) {

		if ( ievt.getListaCampos() == lcCotacao ) {
			
			Calendar dtatu = Calendar.getInstance();
			Calendar dtvencto = Calendar.getInstance();
			
			dtvencto.add( Calendar.WEEK_OF_YEAR, 1 );
			
			txtDtCot.setVlrDate( dtatu.getTime() );
			txtDtValidCot.setVlrDate( dtvencto.getTime() );
			
			txtQtdCot.setVlrBigDecimal( txtQtdItAprovado.getVlrBigDecimal() );
			txtIdUsuCot.setVlrString( Aplicativo.getUsuario().getIdusu() );
			if ( comRef() ) {
				txtRefProd2.setVlrString( txtRefProd.getVlrString() );
				lcProd4.carregaDados();
			}
			else {
				txtCodProd2.setVlrString( txtCodProd.getVlrString() );
				lcProd3.carregaDados();
			}

			txtQtdAprovCot.setVlrBigDecimal( new BigDecimal( 0.0 ) );
		}
	}

	public void exec( int iCodCompra ) {

		txtCodSolicitacao.setVlrString( iCodCompra + "" );
		lcCampos.carregaDados();
		
	}
	
	public void abreCotacao( int codsolicitacao, int codfor, int renda ) {

		txtCodSolicitacao.setVlrInteger( codsolicitacao );
		lcCampos.carregaDados();
		lcCotacao.insert( true );
		txtCodFor.setVlrInteger( codfor );
		lcFor.carregaDados();
		txtRendaCot.setVlrInteger( renda );
		txtPrecoCot.requestFocus();
		
	}

	public void execDev( int iCodFor, int iCodTipoMov, String sSerie, int iDoc ) {

		lcCampos.insert( true );
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcUnid.setConexao( cn );
		lcCotacao.setConexao( cn );
		lcProd.setConexao( cn );
		lcProd2.setConexao( cn );
		lcProd3.setConexao( cn );
		lcProd4.setConexao( cn );
		lcPlanoPag.setConexao( cn );
	
		daocotpreco = new DAOCotPreco( cn );
		
		carregaPrefs();
		carregaPrefsGMS();
		carregaParams();
		
		lcCC.setConexao( cn );
		lcCC.setWhereAdic( "NIVELCC=10 AND ANOCC=" + anoCCPadrao );
		lcUsu.setConexao( cn );
		lcFor.setConexao( cn );
		
		montaDetalhe();
		carregaWhereAdic();
	
	}

	public Color cor( int r, int g, int b ) {

		Color color = new Color( r, g, b );
		return color;
	}
	
	private void carregaPrefs() {
		try {
			prefs = daocotpreco.getPrefs(  Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro ao carregar SGPrefere1 !" );
			e.printStackTrace();
		}
		
		anoCCPadrao = (Integer) prefs.get("anocentrocusto");
		bUsaRef = (Boolean) prefs.get("usarefprod");
	}
	
	private void carregaPrefsGMS() {
		try {
			prefsgms = daocotpreco.getPrefsGMS(  Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "SGPREFERE8" ) );
		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro ao carregar SGPrefere8 !" );
			e.printStackTrace();
		}
		
		utilRendaCot = (String) prefsgms.get( "utilrendacot" );
		
		if( "S".equals( utilRendaCot ) ){
			txtRendaCot.setEnabled( true );
			cbUsaRendaCot.setEnabled( true );
		} else {
			txtRendaCot.setEnabled( false );
			cbUsaRendaCot.setEnabled( false );
		}
	}
	
	private void carregaParams(){
		try {
			params = daocotpreco.setParam( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "SGUSUARIO" ),  Aplicativo.getUsuario().getIdusu()  );
			
		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro ao carregar Preferências" );
			e.printStackTrace();
		}
		
		anoCC = (Integer) params.get("anocc");
		codCC = (String) params.get("codcc");
		
	}
	/*
	public static void recarregaPrecosPedidos( Integer codprod, Integer codfor, Integer codplanopag, Date dataini, Date datafim, BigDecimal preconovo, boolean usarenda, Integer renda ) {

		StringBuilder sql_itens = new StringBuilder();
		StringBuilder sql_update = new StringBuilder();
	
		PreparedStatement ps_update = null;
		PreparedStatement ps_select = null;
		
		ResultSet rs = null;

		DbConnection con = Aplicativo.getInstace().getConexao();

		try {
	
			
			// Query para selecionar a compra ;
			sql_itens.append( "select " );
			sql_itens.append( "cp.codcompra, ic.coditcompra " );
			sql_itens.append( "from cpitcompra ic, cpcompra cp " );
			
			sql_itens.append( "left outer join eqrecmerc rm on ");
			sql_itens.append( "rm.codemp=cp.codemprm and rm.codfilial=cp.codfilialrm and rm.ticket=cp.ticket ");
			
			sql_itens.append( "where " ); 
			sql_itens.append( "cp.codempfr=? and cp.codfilialfr=? and cp.codfor=? and " );
			sql_itens.append( "cp.codemppg=? and cp.codfilialpg=? and cp.codplanopag=? and cp.dtentcompra between ? and ? " );
			sql_itens.append( "and ic.codemp=cp.codemp and ic.codfilial=cp.codfilial and ic.codcompra=cp.codcompra and " );
			sql_itens.append( "ic.codemppd=? and ic.codfilialpd=? and ic.codprod=? " );
			sql_itens.append( "and cp.statuscompra in ('P1','P2','P3') " );

			// Query para atualizar o preço;
			sql_update.append( "update cpitcompra ic " );
			sql_update.append( "set ic.precoitcompra=?, ic.aprovpreco=?, ic.vlrliqitcompra = ( (? * ic.qtditcompra) - (coalesce(ic.vlrdescitcompra,0) ) ) " );
			sql_update.append( "where ic.codemp=? and ic.codfilial=? and ic.codcompra=? and ic.coditcompra=? " );
			

			if(usarenda) {			
				sql_itens.append( "and rm.rendaamostragem=? ");				
			}
	
			// Executando query dos itens de compra a serem atualizados

			ps_select = con.prepareStatement( sql_itens.toString() );

			int iparam = 1;

			ps_select.setInt( iparam++, Aplicativo.iCodEmp );
			ps_select.setInt( iparam++, ListaCampos.getMasterFilial( "CPFORNECED" ) );
			ps_select.setInt( iparam++, codfor );

			ps_select.setInt( iparam++, Aplicativo.iCodEmp );
			ps_select.setInt( iparam++, ListaCampos.getMasterFilial( "FNPLANOPAG" ) );
			ps_select.setInt( iparam++, codplanopag );

			ps_select.setDate( iparam++, Funcoes.dateToSQLDate( dataini ) );
			ps_select.setDate( iparam++, Funcoes.dateToSQLDate( datafim ) );

			ps_select.setInt( iparam++, Aplicativo.iCodEmp );
			ps_select.setInt( iparam++, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			ps_select.setInt( iparam++, codprod );
			
			if(usarenda) {
				ps_select.setInt( iparam++, renda );
			}

			rs = ps_select.executeQuery();
			
			System.out.println("Query:" + sql_itens.toString());

			// Percorrendo os itens para realização da atualização do preço.
			while ( rs.next() ) {
				
				Integer codcompra = rs.getInt( "CODCOMPRA" );
				Integer coditcompra = rs.getInt( "CODITCOMPRA" );
				
				System.out.println("CODCOMPRA:" + codcompra );
				System.out.println("CODITCOMPRA:" + coditcompra );
				
				ps_update = con.prepareStatement( sql_update.toString() );

				iparam = 1;
				

				ps_update.setBigDecimal( iparam++, preconovo );
				ps_update.setString( iparam++, "S" );
				ps_update.setBigDecimal( iparam++, preconovo );

				ps_update.setInt( iparam++, Aplicativo.iCodEmp );
				ps_update.setInt( iparam++, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
				ps_update.setInt( iparam++, codcompra );
				ps_update.setInt( iparam++, coditcompra );

				ps_update.executeUpdate();

			}

			con.commit();

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	*/
	
}
