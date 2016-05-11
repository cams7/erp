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
 *                  Tela para cadastro de solicitações de compra.
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
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDetalhe;
import org.freedom.library.swing.frame.FObservacao;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.cfg.view.frame.crud.tabbed.FUsuario;
import org.freedom.modulos.gms.dao.DAOSolCompra;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.std.DLBuscaEstoq;
import org.freedom.modulos.std.view.dialog.report.DLRPedido;
import org.freedom.modulos.std.view.dialog.utility.DLBuscaProd;
import org.freedom.modulos.std.view.frame.crud.plain.FAlmox;
import org.freedom.modulos.std.view.frame.crud.special.FCentroCusto;

public class FSolicitacaoCompra extends FDetalhe implements PostListener, CarregaListener, FocusListener, ActionListener, InsertListener {

	private static final long serialVersionUID = 1L;

	private int casasDec = Aplicativo.casasDec;

	private JPanelPad pinCab = new JPanelPad( 740, 242 );

	private JPanelPad pinBotCab = new JPanelPad( 104, 92 );

	private JPanelPad pinBotDet = new JPanelPad( 104, 63 );

	private JPanelPad pinLb = new JPanelPad();

	private JLabelPad lSitItSol = null;

	private JPanelPad pinDet = new JPanelPad();

	private JButtonPad btAprovaSol = new JButtonPad( "Aprovar", Icone.novo( "btTudo.png" ) );

	private JButtonPad btFinAprovSol = new JButtonPad( "Finaliz. aprov.", Icone.novo( "btFechaVenda.png" ) );

	private JButtonPad btCancelaSol = new JButtonPad( "Cancelar", Icone.novo( "btRetorno.png" ) );

	private JButtonPad btCancelaItem = new JButtonPad( "Cancelar", Icone.novo( "btRetorno.png" ) );

	private JButtonPad btMotivoCancelaSol = new JButtonPad( "Mot.Can", Icone.novo( "btObs1.png" ) );

	private JButtonPad btMotivoCancelaItem = new JButtonPad( "Mot.Can", Icone.novo( "btObs1.png" ) );

	private JButtonPad btMotivoPrior = new JButtonPad( "Mot.Prior", Icone.novo( "btObs1.png" ) );

	private JTextFieldPad txtCodSolicitacao = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDtEmitSolicitacao = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodItSolicitacao = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtQtdItSolicitado = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtQtdItAprovado = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtIDUsu = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtRefProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCodCC = new JTextFieldPad( JTextFieldPad.TP_STRING, 19, 0 );

	private JTextFieldFK txtDescCC = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtAnoCC = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtOrigSolicitacao = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodAlmox = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldFK txtDescAlmox = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNomeUsu = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodCCUsu = new JTextFieldPad( JTextFieldPad.TP_STRING, 19, 0 );

	private JTextAreaPad txaMotivoSol = new JTextAreaPad();

	private JTextAreaPad txaMotivoCancSol = new JTextAreaPad();

	private JTextAreaPad txaMotivoCancItem = new JTextAreaPad();

	private JTextAreaPad txaMotivoPrior = new JTextAreaPad();

	private JTextFieldPad txtStatusSolicitacao = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtSituacaoItAprov = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtSituacaoItComp = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtSituacaoIt = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtCodUnid = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JRadioGroup<?, ?> rgPriod = null;

	private Vector<String> vLabsTipo = new Vector<String>();

	private Vector<String> vValsTipo = new Vector<String>();

	private JScrollPane spnMotivo = new JScrollPane( txaMotivoSol );

	private JTextFieldPad txtCodFabProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private ListaCampos lcAlmox = new ListaCampos( this, "AM" );

	private ListaCampos lcProd = new ListaCampos( this, "PD" );

	private ListaCampos lcProd2 = new ListaCampos( this, "PD" );

	private ListaCampos lcCC = new ListaCampos( this, "CC" );

	private ListaCampos lcUsu = new ListaCampos( this, "UU" );

	String sSitItSol = txtSituacaoIt.getVlrString();

	String sOrdSol = "";

	Integer anoCC = null;
	
	Integer anoCCPadrao = null;

	Integer iCodTpMov = null;

	String codCC = null;

	boolean bAprovaParcial = false;

	String SitSol = "";

	boolean[] bPrefs = null;

	boolean bAprovaCab = false;

	int cont = 0;

	Vector<String> vItem = new Vector<String>();

	Vector<String> vProdCan = new Vector<String>();

	Vector<String> vMotivoCan = new Vector<String>();

	String sSitSol;

	String sSitItAprov;

	String sSitItExp;
	
	private DAOSolCompra daosolcompra = null;
	
	Map<String, Object>  params = null;

	public FSolicitacaoCompra() {

		setTitulo( "Solicitação de Compra" );
		setAtribos( 15, 10, 763, 580 );

		pnMaster.remove( 2 );
		pnGImp.removeAll();
		pnGImp.setLayout( new GridLayout( 1, 3 ) );
		pnGImp.setPreferredSize( new Dimension( 220, 26 ) );
		pnGImp.add( btPrevimp );
		pnGImp.add( btImp );
		pnMaster.add( spTab, BorderLayout.CENTER );

		montaListaCampos();
		montaTela();
		carregaListener();

	}
	
	private void montaTela() {
		
		nav.setNavigation( true );
		
		vValsTipo.addElement( "M" );
		vValsTipo.addElement( "A" );
		vLabsTipo.addElement( "Normal" );
		vLabsTipo.addElement( "Urgente" );
		rgPriod = new JRadioGroup<String, String>( 2, 1, vLabsTipo, vValsTipo );
		rgPriod.setVlrString( "M" );
				
		setListaCampos( lcCampos );
		setAltCab( 190 );
		setPainel( pinCab, pnCliCab );

		adicCampo( txtCodSolicitacao, 7, 20, 70, 20, "CodSol", "Cód.Sol", ListaCampos.DB_PK, true );
		adicCampo( txtIDUsu, 451, 20, 80, 20, "IdUsu", "Id do usuário", ListaCampos.DB_FK, true );
		adicCampo( txtDtEmitSolicitacao, 539, 20, 86, 20, "DtEmitSol", "Data da Sol.", ListaCampos.DB_SI, true );

		adicDescFKInvisivel( txtDescCC, "DescCC", "Descrição do centro de custos" );
		adicCampo( txtCodCC, 80, 20, 130, 20, "CodCC", "Cód.CC.", ListaCampos.DB_FK, txtDescCC, true );
		adicCampo( txtAnoCC, 213, 20, 70, 20, "AnoCC", "Ano CC.", ListaCampos.DB_FK, true );
		adicDescFK( txtDescCC, 286, 20, 162, 20, "DescCC", "Descrição do centro de custos" );

		adicCampoInvisivel( txtStatusSolicitacao, "SitSol", "Sit.Sol.", ListaCampos.DB_SI, false );
		adicDBLiv( txaMotivoCancSol, "MotivoSol", "Motivo do cancelamento", false );
		adicCampoInvisivel( txtOrigSolicitacao, "OrigSol", "Origem", ListaCampos.DB_SI, false );

		adicDBLiv( txaMotivoSol, "MotivoSol", "Observações", false );
		adic( new JLabelPad( "Observações" ), 7, 40, 100, 20 );
		adic( spnMotivo, 7, 60, 617, 90 );

		txtIDUsu.setNaoEditavel( true );
		txtDtEmitSolicitacao.setNaoEditavel( true );
		txtCodCC.setNaoEditavel( true );
		txtAnoCC.setNaoEditavel( true );

		setListaCampos( true, "SOLICITACAO", "CP" );
		lcCampos.setQueryInsert( false );
		
		btAprovaSol.setToolTipText( "Aprovar todos os ítens." );
		btFinAprovSol.setToolTipText( "Finaliza Aprovação." );
		btCancelaSol.setToolTipText( "Cancelar todos os ítens." );
		btCancelaItem.setToolTipText( "Cancelar ítem." );
		btMotivoCancelaSol.setToolTipText( "Motivo do cancelamento da Solicitação." );
		btMotivoCancelaItem.setToolTipText( "Motivo do cancelamento do ítem." );
		btMotivoPrior.setToolTipText( "Motivo da prioridade do ítem." );
		
		pinCab.adic( pinBotCab, 630, 1, 114, 150 );
		pinBotCab.adic( btAprovaSol, 0, 0, 110, 30 );
		pinBotCab.adic( btFinAprovSol, 0, 31, 110, 30 );
		pinBotCab.adic( btCancelaSol, 0, 62, 110, 30 );
		pinBotCab.adic( btMotivoCancelaSol, 0, 93, 110, 30 );
		
		setImprimir( true );

		desabAprov( true );
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
	lcProd.add( new GuardaCampo( txtCodUnid, "CodUnid", "Cód.und.", ListaCampos.DB_SI, false ) );
	lcProd.setWhereAdic( sWhereAdicProd );
	lcProd.montaSql( false, "PRODUTO", "EQ" );
	lcProd.setReadOnly( true );
	txtCodProd.setTabelaExterna( lcProd, FProduto.class.getCanonicalName() );

	lcProd2.add( new GuardaCampo( txtRefProd, "RefProd", "Referência", ListaCampos.DB_PK, false ) );
	lcProd2.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição", ListaCampos.DB_SI, false ) );
	lcProd2.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.rod.", ListaCampos.DB_SI, false ) );
	lcProd2.add( new GuardaCampo( txtCodFabProd, "CodFabProd", "Código do fabricante", ListaCampos.DB_SI, true ) );
	lcProd2.add( new GuardaCampo( txtCodUnid, "CodUnid", "Cód.und.", ListaCampos.DB_SI, false ) );
	txtRefProd.setNomeCampo( "RefProd" );
	txtRefProd.setListaCampos( lcDet );
	lcProd2.setWhereAdic( sWhereAdicProd );
	lcProd2.montaSql( false, "PRODUTO", "EQ" );
	lcProd2.setQueryCommit( false );
	lcProd2.setReadOnly( true );
	txtRefProd.setTabelaExterna( lcProd2, FProduto.class.getCanonicalName() );

	lcAlmox.add( new GuardaCampo( txtCodAlmox, "CodAlmox", "Cod.almox.", ListaCampos.DB_PK, txtDescAlmox, false ) );
	lcAlmox.add( new GuardaCampo( txtDescAlmox, "DescAlmox", "Descrição do almoxarifado;", ListaCampos.DB_SI, false ) );
	lcAlmox.montaSql( false, "ALMOX", "EQ" );
	lcAlmox.setQueryCommit( false );
	lcAlmox.setReadOnly( true );
	txtDescAlmox.setSoLeitura( true );
	txtCodAlmox.setTabelaExterna( lcAlmox, FAlmox.class.getCanonicalName() );

	lcCC.add( new GuardaCampo( txtCodCC, "CodCC", "Cód.c.c.", ListaCampos.DB_PK, false ) );
	lcCC.add( new GuardaCampo( txtAnoCC, "AnoCC", "Ano c.c.", ListaCampos.DB_PK, false ) );
	lcCC.add( new GuardaCampo( txtDescCC, "DescCC", "Descrição do centro de custo", ListaCampos.DB_SI, false ) );
	lcCC.montaSql( false, "CC", "FN" );
	lcCC.setQueryCommit( false );
	lcCC.setReadOnly( true );
	txtCodCC.setTabelaExterna( lcCC, FCentroCusto.class.getCanonicalName() );
	txtAnoCC.setTabelaExterna( lcCC, FCentroCusto.class.getCanonicalName() );

	lcUsu.add( new GuardaCampo( txtIDUsu, "idusu", "Id.Usu.", ListaCampos.DB_PK, false ) );
	lcUsu.add( new GuardaCampo( txtNomeUsu, "nomeusu", "Nome do usuário", ListaCampos.DB_SI, false ) );
	lcUsu.add( new GuardaCampo( txtCodCCUsu, "codcc", "C.Custo Usuário", ListaCampos.DB_SI, false ) );
	lcUsu.montaSql( false, "USUARIO", "SG" );
	lcUsu.setQueryCommit( false );
	lcUsu.setReadOnly( true );
	txtIDUsu.setTabelaExterna( lcUsu, FUsuario.class.getCanonicalName() );

	}
	
	private void montaDetalhe() {

		setAltDet( 125 );
		pinDet = new JPanelPad( 740, 122 );
		setPainel( pinDet, pnDet );
		setListaCampos( lcDet );
		setNavegador( navRod );
		txtCodUnid.setSoLeitura( true );

		adicCampo( txtCodItSolicitacao, 7, 20, 30, 20, "CodItSol", "Item", ListaCampos.DB_PK, true );
		if ( comRef() ) {
			adicCampo( txtRefProd, 40, 20, 87, 20, "RefProd", "Referência", ListaCampos.DB_FK, txtDescProd, true );
			adicCampoInvisivel( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_SI, false );
			txtRefProd.setBuscaAdic( new DLBuscaProd( con, "REFPROD", lcProd2.getWhereAdic() ) );
			txtQtdItSolicitado.setBuscaAdic( new DLBuscaEstoq( lcDet, lcAlmox, lcProd2, con, "qtditsol" ) );
		}
		else {
			adicCampo( txtCodProd, 40, 20, 87, 20, "CodProd", "Cód.prod.", ListaCampos.DB_FK, txtDescProd, true );
			adicCampoInvisivel( txtRefProd, "RefProd", "Referência", ListaCampos.DB_SI, false );
			txtCodProd.setBuscaAdic( new DLBuscaProd( con, "CODPROD", lcProd.getWhereAdic() ) );
			txtQtdItSolicitado.setBuscaAdic( new DLBuscaEstoq( lcDet, lcAlmox, lcProd, con, "qtditsol" ) );
		}

		adicDescFK( txtDescProd, 130, 20, 297, 20, "DescProd", "Descrição do produto" );
		adicDB( rgPriod, 513, 20, 100, 50, "PriorItSol", "Prioridade:", true );
		adicCampo( txtQtdItSolicitado, 297, 60, 80, 20, "QtdItSol", "Qtd.solic.", ListaCampos.DB_SI, true );
		adic( txtCodUnid, 380, 60, 100, 20 );

		adicCampo( txtQtdItAprovado, 210, 60, 80, 20, "QtdAprovItSol", "Qtd.aprov.", ListaCampos.DB_SI, false );

		txtCodAlmox.setNaoEditavel( true );

		adicCampoInvisivel( txtCodAlmox, "CodAlmox", "Cód.Almox.", ListaCampos.DB_FK, txtDescAlmox, false );
		adicDescFK( txtDescAlmox, 7, 60, 200, 20, "DescAlmox", "Descrição do almoxarifado" );

		adicCampoInvisivel( txtSituacaoIt, "SitItSol", "Sit.It.Sol.", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtSituacaoItAprov, "SitAprovItSol", "Sit.Ap.It.Sol.", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtSituacaoItComp, "SitCompItSol", "Sit.Cot.It.Sol.", ListaCampos.DB_SI, false );
		adicDBLiv( txaMotivoCancItem, "motivocancitsol", "Motivo do cancelamento", false );
		adicDBLiv( txaMotivoPrior, "MotivoPriorItSol", "Motivo da Prioridade", false );

		txtRefProd.addKeyListener( new KeyAdapter() {

			public void keyPressed( KeyEvent kevt ) {

				lcDet.edit();
			}
		} );

		setListaCampos( true, "ITSOLICITACAO", "CP" );
		lcDet.setQueryInsert( false );
		montaTab();

		tab.setTamColuna( 30, 0 );
		tab.setTamColuna( 70, 1 );
		tab.setTamColuna( 250, 2 );
		tab.setTamColuna( 70, 3 );
		tab.setTamColuna( 70, 4 );
		tab.setTamColuna( 70, 5 );
		tab.setTamColuna( 70, 6 );
		tab.setTamColuna( 70, 7 );
		tab.setTamColuna( 250, 8 );

		btMotivoPrior.setEnabled( false );

		pinBotDet.adic( btCancelaItem, 0, 0, 110, 28 );
		pinBotDet.adic( btMotivoCancelaItem, 0, 29, 110, 28 );
		pinBotDet.adic( btMotivoPrior, 0, 58, 110, 28 );
		pinDet.adic( pinBotDet, 630, 1, 114, 90 );
		lSitItSol = new JLabelPad();
		lSitItSol.setForeground( Color.WHITE );
		pinLb.adic( lSitItSol, 31, 0, 110, 20 );
		pinDet.adic( pinLb, 630, 91, 114, 24 );
	}
	
	private void carregaListener() {
		
		txtQtdItSolicitado.addFocusListener( this );
		lcCampos.addPostListener( this );
		lcCampos.addCarregaListener( this );
		lcProd.addCarregaListener( this );
		lcProd2.addCarregaListener( this );
		lcDet.addPostListener( this );
		lcDet.addCarregaListener( this );
		lcDet.addInsertListener( this );
		lcCampos.addInsertListener( this );
		lcUsu.addCarregaListener( this );

		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		btAprovaSol.addActionListener( this );
		btCancelaSol.addActionListener( this );
		btCancelaItem.addActionListener( this );
		btMotivoCancelaSol.addActionListener( this );
		btMotivoCancelaItem.addActionListener( this );
		btMotivoPrior.addActionListener( this );
		btFinAprovSol.addActionListener( this );
	
	}
/*
	private void buscaInfoUsuAtual() {
		Integer codempcc = lcCC.getCodEmp();
		Integer codfilialcc = lcCC.getCodFilial();
	
		try {
			bAprovaCab = daosolcompra.buscaInfoUsuAtual( bAprovaCab, txtCodCC.getVlrString(), codempcc, codfilialcc );
		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro ao carregar Usuário !" );
			e.printStackTrace();
		}
	}
*/
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
	}

	private void desabCampos( boolean bHab ) {

		txtCodProd.setNaoEditavel( bHab );
		txtRefProd.setNaoEditavel( bHab );
		txtQtdItSolicitado.setNaoEditavel( bHab );
		txaMotivoSol.setEnabled( !bHab );
		rgPriod.setAtivo( !bHab );
	}

	private void desabAprov( boolean bHab ) {

		if ( txtStatusSolicitacao.getVlrString().equals( "AT" ) ) {
			btAprovaSol.setEnabled( false );
			if ( !txtStatusSolicitacao.getVlrString().equals( "AF" ) )
				btFinAprovSol.setEnabled( true );
			else {
				btFinAprovSol.setEnabled( false );
			}
		}
		else {
			btAprovaSol.setEnabled( !bHab );
		}
		if ( txtStatusSolicitacao.getVlrString().equals( "CA" ) ) {
			btMotivoCancelaSol.setEnabled( true );
		}
		if ( txtSituacaoItAprov.getVlrString().equals( "CA" ) ) {
			btMotivoCancelaItem.setEnabled( true );
		}
		else {
			btMotivoCancelaSol.setEnabled( !bHab );
			btMotivoCancelaItem.setEnabled( !bHab );
		}

		btFinAprovSol.setEnabled( !bHab );
		btCancelaSol.setEnabled( !bHab );
		btCancelaItem.setEnabled( !bHab );
		txtQtdItAprovado.setNaoEditavel( bHab );
	}

	public void carregaWhereAdic() {

		if ( bAprovaCab ) {
			if ( bAprovaParcial ) {
				lcCampos.setWhereAdic( "CODCC='" + Aplicativo.getUsuario().getCodcc() + "' AND ANOCC=" + Aplicativo.getUsuario().getAnocc() );
			}
		}
		else {
			lcCampos.setWhereAdic( "IDUSU='" + Aplicativo.getUsuario().getIdusu() + "'" );
		}
	}

	public void afterCarrega( CarregaEvent cevt ) {

		
        getParams();
		sSitSol = txtStatusSolicitacao.getVlrString();
		sSitItAprov = txtSituacaoItAprov.getVlrString();
		sSitItExp = txtSituacaoItComp.getVlrString();
		sSitItSol = txtSituacaoIt.getVlrString();

		boolean bStatusTravaTudo = ( ( sSitItSol.equals( "AF" ) ) || ( sSitItSol.equals( "EF" ) ) || ( sSitItSol.equals( "CA" ) ) );

		if ( cevt.getListaCampos() == lcDet ) {
			if ( sSitItSol.equals( "CA" ) ) {
				desabCampos( true );
				btMotivoCancelaItem.setEnabled( true );
			}
		}

		if ( rgPriod.getVlrString().equals( "A" ) && sSitSol.equals( "PE" ) ) {
			btMotivoPrior.setEnabled( true );
		}
		else
			btMotivoPrior.setEnabled( false );

		if ( sSitSol.equals( "CA" ) )
			btMotivoCancelaSol.setEnabled( true );
		else
			btMotivoCancelaSol.setEnabled( false );

		if ( ! ( txtIDUsu.getVlrString().equals( Aplicativo.getUsuario().getIdusu() ) ) || ( bStatusTravaTudo ) )
			desabCampos( true );
		else
			desabCampos( false );

		if ( !bAprovaCab || bStatusTravaTudo ) {
			desabAprov( true );
			txaMotivoCancSol.setEnabled( false );
		}
		else {
			if ( !bStatusTravaTudo )
				txaMotivoCancSol.setEnabled( true );
			desabAprov( false );
		}

		if ( ( ( cevt.getListaCampos() == lcProd ) || ( cevt.getListaCampos() == lcProd2 ) ) && ( ( lcDet.getStatus() == ListaCampos.LCS_EDIT ) || ( ( lcDet.getStatus() == ListaCampos.LCS_INSERT ) ) ) ) {
		}

		if ( sSitItSol.equals( "CA" ) ) {
			SitSol = "Cancelada";
			lSitItSol.setText( SitSol );
			pinLb.setBackground( cor( 250, 50, 50 ) );
		}
		else if ( sSitItSol.equals( "PE" ) ) {
			SitSol = "Pendente";
			lSitItSol.setText( SitSol );
			pinLb.setBackground( cor( 255, 204, 51 ) );
		}
		else if ( sSitItExp.equals( "ET" ) || sSitItExp.equals( "EP" ) ) {
			SitSol = "Expedida";
			lSitItSol.setText( SitSol );
			pinLb.setBackground( cor( 0, 170, 30 ) );
		}
		else if ( sSitItAprov.equals( "AT" ) || sSitItAprov.equals( "AP" ) ) {
			SitSol = "Aprovada";
			lSitItSol.setText( SitSol );
			pinLb.setBackground( cor( 26, 140, 255 ) );
		}

		if ( ( cevt.getListaCampos() == lcProd ) || ( cevt.getListaCampos() == lcProd2 ) ) {
			txtCodUnid.atualizaFK();
		}

		if ( cevt.getListaCampos() == lcDet ) {
			txtCodUnid.atualizaFK();
			if ( txtQtdItAprovado.isEditable() ) {
				if ( txtQtdItAprovado.getVlrBigDecimal().compareTo( new BigDecimal( 0 ) ) <= 0 )
					txtQtdItAprovado.setVlrBigDecimal( txtQtdItAprovado.getVlrBigDecimal() );
			}
			if ( txtQtdItSolicitado.isEditable() ) {
				if ( txtQtdItSolicitado.getVlrBigDecimal().compareTo( new BigDecimal( 0 ) ) <= 0 )
					txtQtdItSolicitado.setVlrBigDecimal( txtQtdItAprovado.getVlrBigDecimal() );
			}
		}
	}

	public boolean[] prefs() {

		boolean[] bRet = { false };
		String sSQL = "SELECT USAREFPROD FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				if ( rs.getString( "UsaRefProd" ).trim().equals( "S" ) )
					bRet[ 0 ] = true;
			}
			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage(), true, con, err );
		} finally {
			rs = null;
			ps = null;
			sSQL = null;
		}
		return bRet;
	}

	private boolean dialogObsCab() {

		boolean bRet = false;
		FObservacao obs = new FObservacao( txaMotivoCancSol.getVlrString() );
		if ( obs != null ) {
			if ( ( !bAprovaCab ) || ( sSitItSol.equals( "CA" ) ) )
				obs.txa.setEnabled( false );
			obs.setVisible( true );
			if ( obs.OK ) {
				txaMotivoCancSol.setVlrString( obs.getTexto() );
				bRet = true;
			}
		}
		obs.dispose();
		return bRet;
	}

	private boolean dialogObsDet() {

		boolean bRet = false;
		FObservacao obs = new FObservacao( txaMotivoCancItem.getVlrString() );
		if ( obs != null ) {
			if ( ( !bAprovaCab ) || ( sSitItSol.equals( "CA" ) ) )
				obs.txa.setEnabled( false );
			obs.setVisible( true );
			if ( obs.OK ) {
				txaMotivoCancItem.setVlrString( obs.getTexto() );
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

		if ( evt.getSource() == btPrevimp )
			imprimir( TYPE_PRINT.VIEW,txtCodSolicitacao.getVlrInteger().intValue() );
		else if ( evt.getSource() == btImp )
			imprimir( TYPE_PRINT.PRINT, txtCodSolicitacao.getVlrInteger().intValue() );
		else if ( evt.getSource() == btMotivoCancelaSol ) {
			dialogObsCab();
		}
		else if ( evt.getSource() == btMotivoCancelaItem ) {
			dialogObsDet();
		}
		else if ( evt.getSource() == btMotivoPrior ) {
			dialogObsPrior();
		}
		else if ( evt.getSource() == btCancelaSol ) {
			lcCampos.setState( ListaCampos.LCS_EDIT );
			if ( Funcoes.mensagemConfirma( null, "Deseja cancelar a solicitação de compra de todos os ítens?" ) == JOptionPane.YES_OPTION ) {
				if ( dialogObsCab() ) {
					txtStatusSolicitacao.setVlrString( "CA" );
					lcCampos.post();
				}
			}
		}
		else if ( evt.getSource() == btCancelaItem ) {
			lcDet.setState( ListaCampos.LCS_EDIT );
			if ( Funcoes.mensagemConfirma( null, "Deseja cancelar ítem da solicitação de compra?" ) == JOptionPane.YES_OPTION ) {
				if ( dialogObsDet() ) {
					txtSituacaoIt.setVlrString( "CA" );
					lcDet.post();
				}
			}
		}

		else if ( evt.getSource() == btAprovaSol ) {
			lcCampos.setState( ListaCampos.LCS_EDIT );
			if ( Funcoes.mensagemConfirma( null, "Deseja Aprovar todos os ítens da solicitação de compra?\n Caso você não tenha informado as quantidades\n a serem aprovadas" + " estará aprovando as quantidades requeridas!" ) == JOptionPane.OK_OPTION ) {
				;
				txtStatusSolicitacao.setVlrString( "AT" );
				nav.btSalvar.doClick();
			}
		}
		else if ( evt.getSource() == btFinAprovSol ) {
			lcCampos.setState( ListaCampos.LCS_EDIT );
			if ( Funcoes.mensagemConfirma( null, "Deseja finalizar o processo de aprovação da solicitação de compra?\n Após este procedimento a solicitação de compra não poderá mais ser alterada\n" + "e estará disponível para cotação!" ) == JOptionPane.OK_OPTION ) {
				;
				txtStatusSolicitacao.setVlrString( "AF" );
				nav.btSalvar.doClick();
			}
		}

		super.actionPerformed( evt );
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
		if ( ! "I.CODITSOL".equalsIgnoreCase( ordem ) ) {
			ordem = "P."+ordem;
		}
		imp.verifLinPag();
		imp.montaCab();

		imp.setTitulo( "Relatório de Solicitação de Compras" );

		String sSQL = "SELECT  (SELECT COUNT(IT.CODITSOL) FROM CPITSOLICITACAO IT " + " WHERE IT.CODEMP=R.CODEMP AND IT.CODFILIAL = R.CODFILIAL AND IT.CODSOL=R.CODSOL)," + "R.CODSOL,R.DTINS,R.SITSOL,R.IDUSU,R.MOTIVOSOL,R.IDUSUAPROV,R.DTAAPROVSOL,R.MOTIVOCANCSOL,"
				+ "I.CODPROD, I.QTDITSOL, I.QTDAPROVITSOL, I.SITITSOL," + "I.SITITSOL,I.SITAPROVITSOL,I.CODITSOL," + "P.REFPROD,P.DESCPROD, P.CODUNID," + "A.CODALMOX, A.DESCALMOX, CC.CODCC, CC.ANOCC, CC.DESCCC," + "(SELECT U.CODCC FROM SGUSUARIO U WHERE U.IDUSU=R.IDUSUAPROV),"
				+ "(SELECT C.DESCCC FROM FNCC C, SGUSUARIO U " + "WHERE C.CODEMP=U.CODEMPCC AND C.CODFILIAL=U.CODEMPCC AND C.ANOCC=U.ANOCC " + " AND C.CODCC=U.CODCC AND U.IDUSU=R.IDUSUAPROV)," + " I.MOTIVOCANCITSOL, I.CODPROD"
				+ " FROM CPSOLICITACAO R, CPITSOLICITACAO I, EQALMOX A, FNCC CC, EQPRODUTO P" + " WHERE R.CODEMP=? AND R.CODFILIAL=? AND R.CODSOL=?" + " AND I.CODEMP=R.CODEMP AND I.CODFILIAL=R.CODFILIAL AND I.CODSOL=R.CODSOL"
				+ " AND P.CODEMP=I.CODEMPPD AND P.CODFILIAL=I.CODFILIALPD AND P.CODPROD=I.CODPROD" + " AND I.CODEMP=R.CODEMP AND I.CODFILIAL=R.CODFILIAL " + " AND CC.CODEMP=R.CODEMPCC AND CC.CODFILIAL=R.CODFILIALCC AND CC.CODCC=R.CODCC"
				+ " AND A.CODEMP=I.CODEMPAM AND A.CODFILIAL=I.CODFILIALAM AND A.CODALMOX=I.CODALMOX " + " ORDER BY R.CODSOL," + ordem + ";";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement( sSQL );

			ps.setInt( 1, lcCampos.getCodEmp() );
			ps.setInt( 2, lcCampos.getCodFilial() );
			ps.setInt( 3, txtCodSolicitacao.getVlrInteger().intValue() );

			rs = ps.executeQuery();

			imp.limpaPags();

			while ( rs.next() ) {
				if ( imp.pRow() >= ( linPag - 1 ) ) {
					imp.incPags();
					imp.eject();
				}
				if ( imp.pRow() == 0 ) {
					imp.impCab( 136, true );
					imp.say( imp.pRow() + 0, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "|" + StringFunctions.replicate( "=", 133 ) + "|" );
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
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "|" + StringFunctions.replicate( "=", 133 ) + "|" );

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
					imp.say( imp.pRow() + 0, 60, "Qtd.req." );
					imp.say( imp.pRow() + 0, 75, "Qtd.aprov." );
					imp.say( imp.pRow() + 0, 100, "Sit.item" );
					imp.say( imp.pRow() + 0, 110, "Sit.aprov." );
					imp.say( imp.pRow() + 0, 136, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 136, "|" );
				}
				imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
				imp.say( imp.pRow() + 0, 1, "|" );
				imp.say( imp.pRow() + 0, 2, rs.getString( "CODITSOL" ) );
				imp.say( imp.pRow() + 0, 8, rs.getString( "REFPROD" ) );
				imp.say( imp.pRow() + 0, 22, rs.getString( "DESCPROD" ).substring( 0, 37 ) );
				imp.say( imp.pRow() + 0, 60, "" + rs.getDouble( "QTDITSOL" ) );
				imp.say( imp.pRow() + 0, 75, "" + rs.getDouble( "QTDAPROVITSOL" ) );
				if ( !rs.getString( "SITITSOL" ).equals( "CA" ) )
					imp.say( imp.pRow() + 0, 105, "" + rs.getString( "SITITSOL" ) );
				if ( !rs.getString( "SITAPROVITSOL" ).equals( "NA" ) )
					imp.say( imp.pRow() + 0, 115, "" + rs.getString( "SITAPROVITSOL" ) );
				imp.say( imp.pRow() + 0, 136, "|" );

				if ( ( rs.getString( "SITITSOL" ).equals( "CA" ) ) || ( rs.getString( "SITAPROVITSOL" ).equals( "NA" ) ) ) {
					if ( comRef() )
						vProdCan.addElement( rs.getString( "REFPROD" ) );
					else
						vProdCan.addElement( rs.getString( "CODPROD" ) );
					vItem.addElement( rs.getString( "CODITSOL" ) );
					vMotivoCan.addElement( rs.getString( "MOTIVOCANCSOL" ) != null ? rs.getString( "MOTIVOCANCSOL" ) : "" );
					cont++;
				}

			}
			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 0, 0, "|" + StringFunctions.replicate( "=", 133 ) + "|" );
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
				imp.say( imp.pRow() + 0, 4, "ITENS NÃO APROVADOS:" );
				imp.say( imp.pRow() + 0, 136, "|" );
				for ( int i = 0; vProdCan.size() > i; i++ ) {
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 4, vItem.elementAt( i ).toString() );
					imp.say( imp.pRow() + 0, 9, vProdCan.elementAt( i ).toString() );
					String sMotivoCanc = vMotivoCan.elementAt( i ).toString();

					imp.say( imp.pRow() + 0, 25, "- " + sMotivoCanc.substring( 0, sMotivoCanc.length() > 108 ? 108 : sMotivoCanc.length() ) );
					imp.say( imp.pRow() + 0, 136, "|" );
				}
			}

			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 0, 0, "+" + StringFunctions.replicate( "=", 133 ) + "+" );
			imp.say( imp.pRow() + 2, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 0, 52, StringFunctions.replicate( "_", 41 ) );
			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 0, 62, "Ass. do solicitante" );

			imp.eject();

			imp.fechaGravacao();

			con.commit();
			dl.dispose();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao consultar a tabela de Solicitações!" + err.getMessage(), true, con, err );
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
	}

	private boolean comRef() {

		return bPrefs[ 0 ];
	}

	public void keyTyped( KeyEvent kevt ) {

		super.keyTyped( kevt );
	}

	public void keyReleased( KeyEvent kevt ) {

		super.keyReleased( kevt );
	}

	public void beforePost( PostEvent pevt ) {

		String sMotvProir = rgPriod.getVlrString();
		if ( pevt.getListaCampos() == lcDet ) {
			if ( txtQtdItAprovado.getVlrDouble().doubleValue() > txtQtdItSolicitado.getVlrDouble().doubleValue() ) {
				Funcoes.mensagemInforma( null, "Quantidade aprovada maior que a requerida!" );
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
			if ( txtQtdItSolicitado.getVlrString().equals( "" ) ) {
				txtQtdItSolicitado.setVlrBigDecimal( new BigDecimal( 0 ) );
			}
			if ( sMotvProir.equals( "A" ) ) {
				dialogObsPrior();
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

		if ( ievt.getListaCampos() == lcCampos ) {
			txtAnoCC.setVlrInteger( anoCC );
			txtCodCC.setVlrString( codCC );
			lcCC.carregaDados();
			txtIDUsu.setVlrString( Aplicativo.getUsuario().getIdusu() );
			txtDtEmitSolicitacao.setVlrDate( new Date() );
			lcCampos.carregaDados();
		}
	}

	public void exec( int iCodCompra ) {

		txtCodSolicitacao.setVlrString( iCodCompra + "" );
		lcCampos.carregaDados();
	}

	public void execDev( int iCodFor, int iCodTipoMov, String sSerie, int iDoc ) {

		lcCampos.insert( true );
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		bPrefs = prefs();
		montaDetalhe();

		lcProd.setConexao( cn );
		lcProd2.setConexao( cn );
		lcCC.setConexao( cn );
		
		lcAlmox.setConexao( cn );
		lcUsu.setConexao( cn );
		
		daosolcompra = new DAOSolCompra( cn );		
		getParams();

		lcCC.setWhereAdic( "NIVELCC=10 AND ANOCC=" + anoCCPadrao );
		carregaWhereAdic();
	}

	public Color cor( int r, int g, int b ) {

		Color color = new Color( r, g, b );
		return color;
	}
	
	private void getParams(){
		
		try {
			params = daosolcompra.getAnocc( bAprovaCab, txtCodCC.getVlrString(), Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "SGUSUARIO" ),  Aplicativo.getUsuario().getIdusu()  );
		} catch (SQLException e) {
			Funcoes.mensagemErro( this, "Erro carregando informações do usuário !\b" + e.getMessage() );
		}
		
		anoCC = (Integer) params.get("anocc");
		codCC = (String) params.get("codcc");
		bAprovaCab = (Boolean) params.get( "baprova");
		
		if ( anoCC.intValue() == 0 )
			anoCC = new Integer( anoCCPadrao );
	}
}
