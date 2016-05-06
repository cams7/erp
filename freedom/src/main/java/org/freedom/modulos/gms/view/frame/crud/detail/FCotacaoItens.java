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
import java.util.Date;
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
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDetalhe;
import org.freedom.library.swing.frame.FObservacao;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.cfg.view.frame.crud.tabbed.FUsuario;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.std.view.dialog.report.DLRPedido;
import org.freedom.modulos.std.view.dialog.utility.DLBuscaProd;
import org.freedom.modulos.std.view.frame.crud.plain.FUnidade;
import org.freedom.modulos.std.view.frame.crud.tabbed.FFornecedor;

public class FCotacaoItens extends FDetalhe implements PostListener, CarregaListener, FocusListener, ActionListener, InsertListener {

	private static final long serialVersionUID = 1L;

	private int casasDec = Aplicativo.casasDec;

	private int casasDecFin = Aplicativo.casasDecFin;

	private int casasDecPre = Aplicativo.casasDecPre;

	private JPanelPad pinCab = new JPanelPad( 740, 242 );

	private JPanelPad pinBotCab = new JPanelPad( 104, 92 );

	private JPanelPad pinBotDet = new JPanelPad( 104, 63 );

	private JPanelPad pinLb = new JPanelPad();

	private JLabelPad lSitItSol = null;

	private JPanelPad pinDet = new JPanelPad();

	private JButtonPad btProduto = new JButtonPad( "Produto", Icone.novo( "btProduto2.gif" ) );

	private JButtonPad btAprovaSol = new JButtonPad( "Aprovar", Icone.novo( "btTudo.png" ) );

	private JButtonPad btFinAprovSol = new JButtonPad( "Finaliz. aprov.", Icone.novo( "btFechaVenda.png" ) );

	private JButtonPad btCompra = new JButtonPad( "Comprar", Icone.novo( "btMedida.png" ) );

	private JButtonPad btCancelaItem = new JButtonPad( "Cancelar", Icone.novo( "btRetorno.png" ) );

	private JButtonPad btMotivoCancelaItem = new JButtonPad( "Mot.Can", Icone.novo( "btObs1.png" ) );

	private JButtonPad btMotivoAbaixo = new JButtonPad( "Mot.Abaixo", Icone.novo( "btObs1.png" ) );

	private JTextFieldPad txtCodSumSol = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodItSolicitacao = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtQtdAprovItSol = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtIDUsu = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtRefProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCodProd2 = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtRefProd2 = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtDescProd2 = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtStatusSolicitacao = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtSituacaoItAprov = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtSituacaoItComp = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtSituacaoIt = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtCodCot = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtDtCot = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtIdUsuCot = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtCodUnid = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldFK txtDescUnid = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtQtdCot = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtPrecoCot = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtCodFabProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtNomeUsu = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodCCUsu = new JTextFieldPad( JTextFieldPad.TP_STRING, 19, 0 );

	private JTextAreaPad txaMotivoCancCot = new JTextAreaPad();

	private JTextAreaPad txaMotivoCotAbaixo = new JTextAreaPad();

	private JTablePad tabCot = new JTablePad();

	private JScrollPane spTabCot = new JScrollPane( tabCot );

	private Navegador navCot = new Navegador( true );

	private ListaCampos lcUsu = new ListaCampos( this, "UU" );

	private ListaCampos lcProd = new ListaCampos( this, "PD" );

	private ListaCampos lcProd2 = new ListaCampos( this, "PD" );

	private ListaCampos lcProd3 = new ListaCampos( this, "PD" );

	private ListaCampos lcProd4 = new ListaCampos( this, "PD" );

	private ListaCampos lcFor = new ListaCampos( this, "FR" );

	private ListaCampos lcUnid = new ListaCampos( this, "UD" );

	String sSitItSol = txtSituacaoIt.getVlrString();

	String sOrdSol = "";

	Integer anoCC = null;

	Integer iCodTpMov = null;

	String codCC = null;

	boolean bAprovaParcial = false;

	String SitSol = "";

	boolean[] bPrefs = null;

	boolean bCotacao = false;

	int cont = 0;

	Vector<String> vItem = new Vector<String>();

	Vector<String> vProdCan = new Vector<String>();

	Vector<String> vMotivoCan = new Vector<String>();

	String sSitSol;

	String sSitItAprov;

	String sSitItExp;

	public FCotacaoItens() {

		setTitulo( "Cotação Sumarizada de Preços" );
		setAtribos( 15, 10, 763, 455 );

		pnMaster.remove( 2 );
		pnGImp.removeAll();
		pnGImp.setLayout( new GridLayout( 1, 3 ) );
		pnGImp.setPreferredSize( new Dimension( 220, 26 ) );
		pnGImp.add( btPrevimp );
		pnGImp.add( btImp );

		pnMaster.add( spTab, BorderLayout.CENTER );

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
		txtCodProd.setTabelaExterna( lcProd, FProduto.class.getCanonicalName() );

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
		txtRefProd.setTabelaExterna( lcProd2, FProduto.class.getCanonicalName() );

		lcProd3.add( new GuardaCampo( txtCodProd2, "CodProd", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcProd3.add( new GuardaCampo( txtDescProd2, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProd3.add( new GuardaCampo( txtRefProd2, "RefProd", "Referência", ListaCampos.DB_SI, false ) );
		lcProd3.montaSql( false, "PRODUTO", "EQ" );
		lcProd3.setReadOnly( true );
		txtCodProd2.setTabelaExterna( lcProd3, FProduto.class.getCanonicalName() );

		lcProd4.add( new GuardaCampo( txtRefProd2, "RefProd", "Referência", ListaCampos.DB_PK, false ) );
		lcProd4.add( new GuardaCampo( txtDescProd2, "DescProd", "Descrição", ListaCampos.DB_SI, false ) );
		lcProd4.add( new GuardaCampo( txtCodProd2, "CodProd", "Cód.rod.", ListaCampos.DB_SI, false ) );

		txtRefProd2.setNomeCampo( "RefProd" );
		txtRefProd2.setListaCampos( lcDet );
		lcProd4.montaSql( false, "PRODUTO", "EQ" );
		lcProd4.setQueryCommit( false );
		lcProd4.setReadOnly( true );
		txtRefProd2.setTabelaExterna( lcProd4, FProduto.class.getCanonicalName() );

		lcUsu.add( new GuardaCampo( txtIDUsu, "idusu", "Id.Usu.", ListaCampos.DB_PK, false ) );
		lcUsu.add( new GuardaCampo( txtNomeUsu, "nomeusu", "Nome do usuário", ListaCampos.DB_SI, false ) );
		lcUsu.add( new GuardaCampo( txtCodCCUsu, "codcc", "C.Custo Usuário", ListaCampos.DB_SI, false ) );
		lcUsu.montaSql( false, "USUARIO", "SG" );
		lcUsu.setQueryCommit( false );
		lcUsu.setReadOnly( true );
		txtIDUsu.setTabelaExterna( lcUsu, FUsuario.class.getCanonicalName() );

		lcUnid.add( new GuardaCampo( txtCodUnid, "CodUnid", "Cód.unid.", ListaCampos.DB_PK, true ) );
		lcUnid.add( new GuardaCampo( txtDescUnid, "DescUnid", "Unidade", ListaCampos.DB_SI, false ) );
		lcUnid.montaSql( false, "UNIDADE", "EQ" );
		lcUnid.setReadOnly( true );
		lcUnid.setQueryCommit( false );
		txtCodUnid.setTabelaExterna( lcUnid, FUnidade.class.getCanonicalName() );

		lcFor.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.for.", ListaCampos.DB_PK, false ) );
		lcFor.add( new GuardaCampo( txtDescFor, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );
		lcFor.montaSql( false, "FORNECED", "CP" );
		lcFor.setQueryCommit( false );
		lcFor.setReadOnly( true );
		txtCodFor.setTabelaExterna( lcFor, FFornecedor.class.getCanonicalName() );

		txtQtdAprovItSol.addFocusListener( this );
		lcCampos.addPostListener( this );
		lcCampos.addCarregaListener( this );
		lcProd.addCarregaListener( this );
		lcProd2.addCarregaListener( this );
		lcDet.addPostListener( this );
		lcDet.addCarregaListener( this );
		lcDet.addInsertListener( this );
		lcCampos.addInsertListener( this );
		lcUsu.addCarregaListener( this );

		btProduto.setToolTipText( "Ver a descrição do produto." );
		btAprovaSol.setToolTipText( "Aprovar todos os ítens." );
		btFinAprovSol.setToolTipText( "Finaliza Aprovação." );
		btCompra.setToolTipText( "Comprar todos os ítens." );
		btCancelaItem.setToolTipText( "Cancelar ítem." );
		btMotivoCancelaItem.setToolTipText( "Motivo do cancelamento do ítem." );
		btMotivoAbaixo.setToolTipText( "Motivo do número de cotações baixo." );

		pinCab.adic( pinBotCab, 630, 1, 114, 99 );
		pinBotCab.adic( btAprovaSol, 0, 0, 110, 30 );
		pinBotCab.adic( btFinAprovSol, 0, 31, 110, 30 );
		pinBotCab.adic( btCompra, 0, 62, 110, 30 );

		btProduto.addActionListener( this );
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		btAprovaSol.addActionListener( this );
		btCancelaItem.addActionListener( this );
		btCompra.addActionListener( this );
		btMotivoCancelaItem.addActionListener( this );
		btMotivoAbaixo.addActionListener( this );
		btFinAprovSol.addActionListener( this );

		setImprimir( true );

		desabAprov( true );
		desabCot( true );
	}

	private void montaMestre() {

		pinCab = new JPanelPad( 740, 200 );
		setListaCampos( lcCampos );
		setAltCab( 200 );
		setPainel( pinCab, pnCliCab );
		setNavegador( navCot );
		lcDet.setTabela( tab );
		lcCampos.setTabela( tabCot );
		lcCampos.setNavegador( navCot );
		navCot.setListaCampos( lcCampos );

		lcCampos.setPodeExc( false );
		lcCampos.setPodeIns( false );
		nav.setAtivo( 0, false );
		nav.setAtivo( 1, false );
		nav.setAtivo( 2, false );
		nav.setAtivo( 3, false );
		nav.setAtivo( 4, false );

		txtCodProd.setNaoEditavel( true );
		txtRefProd.setNaoEditavel( true );

		adicCampo( txtCodSumSol, 7, 20, 70, 20, "CodSumSol", "Cód.Sum.Sol", ListaCampos.DB_PK, true );

		if ( comRef() ) {
			adicCampo( txtRefProd, 80, 20, 87, 20, "RefProd", "Referência", ListaCampos.DB_FK, txtDescProd, true );
			adicCampoInvisivel( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_SI, false );
			txtRefProd.setBuscaAdic( new DLBuscaProd( con, "REFPROD", lcProd2.getWhereAdic() ) );
		}
		else {
			adicCampo( txtCodProd, 80, 20, 87, 20, "CodProd", "Cód.prod.", ListaCampos.DB_FK, txtDescProd, true );
			adicCampoInvisivel( txtRefProd, "RefProd", "Referência", ListaCampos.DB_SI, false );
			txtCodProd.setBuscaAdic( new DLBuscaProd( con, "CODPROD", lcProd.getWhereAdic() ) );
		}

		adicDescFK( txtDescProd, 170, 20, 302, 20, "DescProd", "Descrição do produto" );

		adicCampo( txtQtdAprovItSol, 475, 20, 80, 20, "QtdAprovItSol", "Qtd.aprov.", ListaCampos.DB_SI, false );
		adic( btProduto, 558, 5, 105, 35 );
		btProduto.setEnabled( false );

		txtIDUsu.setNaoEditavel( true );

		setListaCampos( true, "SUMSOL", "CP" );
		lcCampos.setQueryInsert( false );
		lcCampos.montaTab();
		lcCampos.carregaDados();

		tabCot.setTamColuna( 80, 0 );
		tabCot.setTamColuna( 80, 1 );
		tabCot.setTamColuna( 260, 2 );
		tabCot.setTamColuna( 115, 3 );
		tabCot.setTamColuna( 70, 4 );

		pinCab.adic( spTabCot, 7, 47, 640, 100 );
	}

	private void montaDetalhe() {

		pinDet = new JPanelPad( 740, 100 );
		setPainel( pinDet, pnDet );
		setAltDet( 100 );
		setListaCampos( lcDet );

		txtCodItSolicitacao.setEditable( false );

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

		setNavegador( navRod );
		navRod.setListaCampos( lcDet );
		lcDet.setNavegador( navRod );
		lcDet.setTabela( tab );

		txtQtdAprovItSol.setEditable( false );

		txtQtdAprovItSol.setNaoEditavel( true );
		txtDtCot.setSoLeitura( true );

		txtRefProd2.setSoLeitura( true );
		txtCodProd2.setSoLeitura( true );

		adicCampo( txtCodCot, 7, 20, 77, 20, "CodItSol", "Cód.it.Sol.", ListaCampos.DB_PK, true );
		if ( comRef() ) {
			adic( txtRefProd2, 187, 60, 87, 20 );
		}
		else {
			adic( txtCodProd2, 187, 60, 87, 20 );
		}
		adicDescFK( txtDescProd2, 277, 60, 302, 20, "DescProd", "Descrição do produto" );
		adicCampo( txtDtCot, 87, 20, 97, 20, "DtCot", "Dt.Cot.", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtIdUsuCot, "IdUsuCot", "Usu.Cot.", ListaCampos.DB_SI, false );
		adicCampo( txtCodFor, 187, 20, 77, 20, "CodFor", "Cod.For.", ListaCampos.DB_FK, txtDescFor, false );
		adicDescFK( txtDescFor, 267, 20, 197, 20, "RazFor", "Razão social do fornecedor" );
		adicCampo( txtQtdCot, 467, 20, 57, 20, "QtdCot", "Qtd.Cot.", ListaCampos.DB_SI, false );
		adic( txtDescUnid, 527, 20, 100, 20 );
		adicCampo( txtPrecoCot, 97, 60, 87, 20, "PrecoCot", "Preco.Cot.", ListaCampos.DB_SI, false );

		lcDet.montaSql( true, "ITSUMSOL", "CP" );
		lcDet.setQueryInsert( false );
		lcDet.montaTab();

		tab.setTamColuna( 30, 0 );
		tab.setTamColuna( 80, 1 );
		tab.setTamColuna( 115, 2 );
		tab.setTamColuna( 115, 3 );
		tab.setTamColuna( 230, 4 );
		tab.setTamColuna( 70, 5 );
		tab.setTamColuna( 70, 6 );

		btMotivoAbaixo.setEnabled( false );

		pinBotDet.adic( btCancelaItem, 0, 0, 110, 28 );
		pinBotDet.adic( btMotivoCancelaItem, 0, 29, 110, 28 );
		pinDet.adic( pinBotDet, 630, 1, 114, 63 );
		lSitItSol = new JLabelPad();
		lSitItSol.setForeground( Color.WHITE );
		pinLb.adic( lSitItSol, 31, 0, 110, 20 );
		pinDet.adic( pinLb, 630, 66, 114, 24 );
	}

	private void buscaInfoUsuAtual() {

		String sSQL = "SELECT ANOCC,CODCC,CODEMPCC,CODFILIALCC,APROVCPSOLICITACAOUSU,COMPRASUSU " + "FROM SGUSUARIO WHERE CODEMP=? AND CODFILIAL=? " + "AND IDUSU=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGUSUARIO" ) );
			ps.setString( 3, Aplicativo.getUsuario().getIdusu() );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				String sCotacao = rs.getString( "COMPRASUSU" );
				if ( sCotacao != null ) {
					if ( sCotacao.equals( "S" ) )
						bCotacao = true;
					else
						bCotacao = false;
				}
			}
			con.commit();

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
	}

	private void desabCampos( boolean bHab ) {

		txtCodProd.setNaoEditavel( bHab );
		txtQtdCot.setNaoEditavel( bHab );
		txtPrecoCot.setNaoEditavel( bHab );
		txtCodFor.setNaoEditavel( bHab );
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
		btMotivoCancelaItem.setEnabled( txtSituacaoItAprov.getVlrString().equals( "CA" ) );

		btFinAprovSol.setEnabled( !bHab );
		btCancelaItem.setEnabled( !bHab );
	}

	private void desabCot( boolean bHab ) {

		btCompra.setEnabled( !bHab );
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
			btProduto.setEnabled( !txtCodProd.getVlrString().equals( "" ) );
		}

		if ( !txtIDUsu.getVlrString().equals( Aplicativo.getUsuario().getIdusu() ) && !bCotacao || bStatusTravaTudo )
			desabCampos( true );
		else
			desabCampos( false );

		if ( bStatusTravaTudo ) {
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

	private boolean dialogObsDet() {

		boolean bRet = false;
		FObservacao obs = new FObservacao( txaMotivoCancCot.getVlrString() );
		if ( obs != null ) {
			if ( sSitItExp.equals( "CA" ) )
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

		if ( evt.getSource() == btProduto )
			abreProd();
		if ( evt.getSource() == btPrevimp )
			imprimir( TYPE_PRINT.VIEW,txtCodSumSol.getVlrInteger().intValue() );
		else if ( evt.getSource() == btImp )
			imprimir( TYPE_PRINT.PRINT, txtCodSumSol.getVlrInteger().intValue() );
		else if ( evt.getSource() == btMotivoCancelaItem ) {
			dialogObsDet();
		}
		else if ( evt.getSource() == btMotivoAbaixo ) {
			dialogObsAbaixo();
		}
		else if ( evt.getSource() == btCancelaItem ) {
			lcDet.setState( ListaCampos.LCS_EDIT );
			if ( Funcoes.mensagemConfirma( null, "Deseja cancelar ítem da compra?" ) == JOptionPane.YES_OPTION ) {
				if ( dialogObsDet() ) {
					txtSituacaoItComp.setVlrString( "CA" );
					lcDet.post();
				}
			}
		}

		else if ( evt.getSource() == btAprovaSol ) {
			if ( Funcoes.mensagemConfirma( null, "Deseja Aprovar todos os ítens da compra?\n Caso você não tenha informado as quantidades\n a serem aprovadas" + " estará aprovando as quantidades requeridas!" ) == JOptionPane.OK_OPTION ) {
				;
				lcCampos.setState( ListaCampos.LCS_EDIT );
				txtStatusSolicitacao.setVlrString( "CT" );
				lcCampos.post();
			}
		}
		else if ( evt.getSource() == btFinAprovSol ) {
			if ( Funcoes.mensagemConfirma( null, "Deseja finalizar o processo de aprovação da compra?\n Após este procedimento a compra não poderá mais ser alterada\n" + "e estará disponível para expedição da nota fiscal!" ) == JOptionPane.OK_OPTION ) {
				;
				lcCampos.setState( ListaCampos.LCS_EDIT );
				txtStatusSolicitacao.setVlrString( "CF" );
				lcCampos.post();
			}
		}
		else if ( evt.getSource() == btCompra ) {
			if ( Funcoes.mensagemConfirma( null, "Deseja cotar todos os ítens da solicitação de compra?\n Caso você não tenha informado as quantidades\n a serem cotadas" + " estará cotando as quantidades aprovadas!" ) == JOptionPane.OK_OPTION ) {
				;
				lcCampos.setState( ListaCampos.LCS_EDIT );
				txtStatusSolicitacao.setVlrString( "EF" );
				lcCampos.post();
			}
		}

		super.actionPerformed( evt );
	}

	private void imprimir( TYPE_PRINT bVisualizar, int iCodSol ) {

		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		DLRPedido dl = new DLRPedido( sOrdSol, "I.CODITRMA", false );
		dl.setVisible( true );
		if ( dl.OK == false ) {
			dl.dispose();
			return;
		}
		String ordem = dl.getValor();
		if (! "I.CODITRMA".equalsIgnoreCase( ordem )) {
			ordem = "P."+ordem;
		}
		imp.verifLinPag();
		imp.montaCab();
		/*
		 * String sSQL = "SELECT (SELECT COUNT(IC.CODITSOL) FROM CPITSOLICITACAO IC WHERE IC.CODSOL=S.CODSOL)," + "S.CODSOL,S.DTEMITSOL,S.SITSOL,S.MOTIVOSOL," + "I.CODPROD, I.QTDITSOL, I.QTDAPROVITSOL,I.SITAPROVITSOL, I.SITCOMPITSOL, I.SITITSOL," + "P.REFPROD,P.DESCPROD, P.CODUNID," + "A.CODALMOX,
		 * A.DESCALMOX, CC.CODCC, CC.ANOCC" + " FROM CPSOLICITACAO S, CPITSOLICITACAO I, EQALMOX A, FNCC CC, EQPRODUTO P" + " WHERE S.CODSOL=" + iCodSol + " AND I.CODSOL=S.CODSOL" + " AND P.CODPROD=I.CODPROD" + " AND I.CODALMOX=I.CODALMOX" + " AND CC.CODCC=I.CODCC" + " ORDER BY
		 * S.CODSOL,P." + dl.getValor() + ";";
		 */
		imp.setTitulo( "Relatório de Solicitação de Compras" );
		/*
		 * String sSQL = "SELECT (SELECT COUNT(IC.CODITSOL) FROM CPITSOLICITACAO IC WHERE IC.CODSOL=S.CODSOL)," + "S.CODSOL,S.DTEMITSOL,S.SITSOL,S.MOTIVOSOL," + "I.CODPROD, I.QTDITSOL, I.QTDAPROVITSOL,I.SITAPROVITSOL, I.SITCOMPITSOL, I.SITITSOL," + "P.REFPROD,P.DESCPROD, P.CODUNID," +
		 * "A.CODALMOX, A.DESCALMOX, CC.CODCC, CC.ANOCC" + " FROM CPSOLICITACAO S, CPITSOLICITACAO I, EQALMOX A, FNCC CC, EQPRODUTO P" + " WHERE S.CODSOL=" + iCodSol + " AND I.CODSOL=S.CODSOL" + " AND P.CODPROD=I.CODPROD" + " AND I.CODALMOX=I.CODALMOX" + " AND CC.CODCC=I.CODCC" +
		 * " ORDER BY S.CODSOL,P." + dl.getValor() + ";";
		 */
		String sSQL = "SELECT  (SELECT COUNT(IT.CODITRMA) FROM EQITRMA IT " + " WHERE IT.CODEMP=R.CODEMP AND IT.CODFILIAL = R.CODFILIAL AND IT.CODRMA=R.CODRMA)," + "R.CODRMA,R.DTINS,R.SITRMA,R.MOTIVORMA,R.IDUSU,R.IDUSUAPROV,R.IDUSUEXP,R.DTAAPROVRMA,R.DTAEXPRMA,R.MOTIVOCANCRMA,"
				+ "I.CODPROD, I.QTDITRMA, I.QTDAPROVITRMA, I.QTDEXPITRMA, I.SITITRMA," + "I.SITITRMA,I.SITAPROVITRMA,I.SITEXPITRMA,I.CODITRMA," + "P.REFPROD,P.DESCPROD, P.CODUNID," + "A.CODALMOX, A.DESCALMOX, CC.CODCC, CC.ANOCC, CC.DESCCC,"
				+ "(SELECT U.CODCC FROM SGUSUARIO U WHERE U.IDUSU=R.IDUSUAPROV)," + "(SELECT C.DESCCC FROM FNCC C, SGUSUARIO U " + "WHERE C.CODEMP=U.CODEMPCC AND C.CODFILIAL=U.CODEMPCC AND C.ANOCC=U.ANOCC " + " AND C.CODCC=U.CODCC AND U.IDUSU=R.IDUSUAPROV),"
				+ "(SELECT C.DESCCC FROM FNCC C, SGUSUARIO U " + "WHERE C.CODEMP=U.CODEMPCC AND C.CODFILIAL=U.CODEMPCC AND C.ANOCC=U.ANOCC " + " AND C.CODCC=U.CODCC AND U.IDUSU=R.IDUSUEXP)," + "(SELECT U.CODCC FROM SGUSUARIO U WHERE U.IDUSU=R.IDUSUEXP),"
				+ " I.MOTIVOCANCITRMA, I.CODPROD , R.CODOP, R.SEQOP" + " FROM EQRMA R, EQITRMA I, EQALMOX A, FNCC CC, EQPRODUTO P" + " WHERE R.CODEMP=? AND R.CODFILIAL=? AND R.CODRMA=?" + " AND I.CODEMP=R.CODEMP AND I.CODFILIAL=R.CODFILIAL AND I.CODRMA=R.CODRMA"
				+ " AND P.CODEMP=I.CODEMPPD AND P.CODFILIAL=I.CODFILIALPD AND P.CODPROD=I.CODPROD" + " AND I.CODEMP=R.CODEMP AND I.CODFILIAL=R.CODFILIAL " + " AND CC.CODEMP=R.CODEMPCC AND CC.CODFILIAL=R.CODFILIALCC AND CC.CODCC=R.CODCC"
				+ " AND A.CODEMP=I.CODEMPAX AND A.CODFILIAL=I.CODFILIALAX AND A.CODALMOX=I.CODALMOX " + " ORDER BY R.CODRMA," +ordem+  ";";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement( sSQL );

			ps.setInt( 1, lcCampos.getCodEmp() );
			ps.setInt( 2, lcCampos.getCodFilial() );
			ps.setInt( 3, txtCodSumSol.getVlrInteger().intValue() );

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
					imp.say( imp.pRow() + 0, 4, "R.M.A.   No.: " );
					imp.say( imp.pRow() + 0, 19, rs.getString( "CODRMA" ) );
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
					imp.say( imp.pRow() + 0, 30, "- C.C.: " );
					imp.say( imp.pRow() + 0, 38, ( rs.getString( 29 ) != null ? rs.getString( 29 ).trim() : "" ) );
					imp.say( imp.pRow() + 0, 62, "-" + ( rs.getString( 30 ) != null ? rs.getString( 30 ).trim() : "" ) );
					imp.say( imp.pRow() + 0, 113, "- Data : " );
					imp.say( imp.pRow() + 0, 123, StringFunctions.sqlDateToStrDate( rs.getDate( "DTAAPROVRMA" ) ) );
					imp.say( imp.pRow() + 0, 136, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 4, "Expedição   : " );
					imp.say( imp.pRow() + 0, 19, rs.getString( "IDUSUEXP" ) );
					imp.say( imp.pRow() + 0, 30, "- C.C.: " );
					imp.say( imp.pRow() + 0, 38, ( rs.getString( 31 ) != null ? rs.getString( 31 ).trim() : "" ) );
					imp.say( imp.pRow() + 0, 62, "-" + ( rs.getString( 32 ) != null ? rs.getString( 32 ).trim() : "" ) );
					imp.say( imp.pRow() + 0, 113, "- Data : " );
					imp.say( imp.pRow() + 0, 123, StringFunctions.sqlDateToStrDate( rs.getDate( "DTAEXPRMA" ) ) );
					imp.say( imp.pRow() + 0, 136, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 4, "O.P/OS.:" );
					imp.say( imp.pRow() + 0, 13, rs.getString( "CodOP" ) != null ? rs.getString( "CodOP" ).trim() : "" );
					imp.say( imp.pRow() + 0, 25, "Seq. O.P.:" );
					imp.say( imp.pRow() + 0, 37, rs.getString( "SeqOP" ) != null ? rs.getString( "SeqOP" ).trim() : "" );
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
					imp.say( imp.pRow() + 0, 90, "Qtd.exp." );
					imp.say( imp.pRow() + 0, 100, "Sit.item" );
					imp.say( imp.pRow() + 0, 110, "Sit.aprov." );
					imp.say( imp.pRow() + 0, 122, "Sit.exp." );
					imp.say( imp.pRow() + 0, 136, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 136, "|" );
				}
				imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
				imp.say( imp.pRow() + 0, 1, "|" );
				imp.say( imp.pRow() + 0, 2, rs.getString( "CODITRMA" ) );
				imp.say( imp.pRow() + 0, 8, rs.getString( "REFPROD" ) );
				imp.say( imp.pRow() + 0, 22, rs.getString( "DESCPROD" ).substring( 0, 37 ) );
				imp.say( imp.pRow() + 0, 60, "" + rs.getDouble( "QTDITRMA" ) );
				imp.say( imp.pRow() + 0, 75, "" + rs.getDouble( "QTDAPROVITRMA" ) );
				imp.say( imp.pRow() + 0, 90, "" + rs.getDouble( "QTDEXPITRMA" ) );
				if ( !rs.getString( "SITITRMA" ).equals( "CA" ) )
					imp.say( imp.pRow() + 0, 105, "" + rs.getString( "SITITRMA" ) );
				if ( !rs.getString( "SITAPROVITRMA" ).equals( "NA" ) )
					imp.say( imp.pRow() + 0, 115, "" + rs.getString( "SITAPROVITRMA" ) );
				if ( !rs.getString( "SITEXPITRMA" ).equals( "NE" ) )
					imp.say( imp.pRow() + 0, 125, "" + rs.getString( "SITEXPITRMA" ) );
				imp.say( imp.pRow() + 0, 136, "|" );

				if ( ( rs.getString( "SITITRMA" ).equals( "CA" ) ) || ( rs.getString( "SITAPROVITRMA" ).equals( "NA" ) ) || ( rs.getString( "SITEXPITRMA" ).equals( "NE" ) ) ) {
					if ( comRef() )
						vProdCan.addElement( rs.getString( "REFPROD" ) );
					else
						vProdCan.addElement( rs.getString( "CODPROD" ) );
					vItem.addElement( rs.getString( "CODITRMA" ) );
					vMotivoCan.addElement( rs.getString( "MOTIVOCANCRMA" ) != null ? rs.getString( "MOTIVOCANCRMA" ) : "" );
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
			imp.say( imp.pRow() + 0, 2, "MOTIVO DA REQUISIÇÃO: " );
			String sMotivoRMA = ( rs.getString( "MOTIVORMA" ) != null ? rs.getString( "MOTIVORMA" ) : "" ).trim();
			imp.say( imp.pRow() + 0, 26, sMotivoRMA.substring( 0, sMotivoRMA.length() > 109 ? 109 : sMotivoRMA.length() ) );
			imp.say( imp.pRow() + 0, 136, "|" );
			if ( cont > 0 ) {
				imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
				imp.say( imp.pRow() + 0, 1, "|" );
				imp.say( imp.pRow() + 0, 4, "ITENS NÃO EXPEDIDOS:" );
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
			imp.say( imp.pRow() + 0, 62, "Ass. do requisitante" );

			imp.eject();

			imp.fechaGravacao();

			con.commit();
			dl.dispose();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao consultar a tabela de Compra!" + err.getMessage(), true, con, err );
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

		if ( pevt.getListaCampos() == lcDet ) {
			if ( txtSituacaoIt.getVlrString().equals( "" ) ) {
				txtSituacaoIt.setVlrString( "PE" );
			}
			if ( txtSituacaoItAprov.getVlrString().equals( "" ) ) {
				txtSituacaoItAprov.setVlrString( "PE" );
			}
			if ( txtSituacaoItComp.getVlrString().equals( "" ) ) {
				txtSituacaoItComp.setVlrString( "PE" );
			}
			if ( txtQtdAprovItSol.getVlrString().equals( "" ) ) {
				txtQtdAprovItSol.setVlrBigDecimal( new BigDecimal( 0 ) );
			}
		}
		else if ( pevt.getListaCampos() == lcCampos ) {
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

		if ( ievt.getListaCampos() == lcDet ) {
			txtDtCot.setVlrDate( new Date() );
			txtQtdCot.setVlrBigDecimal( txtQtdAprovItSol.getVlrBigDecimal() );
			txtIdUsuCot.setVlrString( Aplicativo.getUsuario().getIdusu() );
			if ( comRef() ) {
				txtRefProd2.setVlrString( txtRefProd.getVlrString() );
				lcProd4.carregaDados();
			}
			else {
				txtCodProd2.setVlrString( txtCodProd.getVlrString() );
				lcProd3.carregaDados();
			}
		}
	}

	public void exec( int iCodCompra ) {

		txtCodSumSol.setVlrString( iCodCompra + "" );
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

		bPrefs = prefs();
		montaMestre();
		montaDetalhe();

		lcUnid.setConexao( cn );
		lcProd.setConexao( cn );
		lcProd2.setConexao( cn );
		lcProd3.setConexao( cn );
		lcProd4.setConexao( cn );
		lcUsu.setConexao( cn );
		lcFor.setConexao( cn );

		String sSQL = "SELECT anoCC, codCC, codAlmox, aprovCPSolicitacaoUsu FROM SGUSUARIO WHERE CODEMP=? AND CODFILIAL=? AND IDUsu=?";
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			ps.setString( 3, Aplicativo.getUsuario().getIdusu() );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				anoCC = new Integer( rs.getInt( "anoCC" ) );
				if ( anoCC.intValue() == 0 )
					anoCC = new Integer( buscaVlrPadrao() );
				codCC = rs.getString( "codCC" );
			}

			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage() );
		}
	}

	public Color cor( int r, int g, int b ) {

		Color color = new Color( r, g, b );
		return color;
	}

}
