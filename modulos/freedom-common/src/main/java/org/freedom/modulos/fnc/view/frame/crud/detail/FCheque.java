/*
 * Projeto: Freedom Pacote: org.freedom.modules.fnc Classe: @(#)FCheque.java
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR> modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR> na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR> sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR> Veja a Licença Pública Geral GNU para maiores detalhes. <BR> Você deve ter recebido uma cópia da Licença Pública
 * Geral GNU junto com este programa, se não, <BR> escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA <BR> <BR>
 */

package org.freedom.modulos.fnc.view.frame.crud.detail;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.DeleteEvent;
import org.freedom.acao.DeleteListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.JComboBoxEvent;
import org.freedom.acao.JComboBoxListener;
import org.freedom.acao.PostEvent;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDetalhe;
import org.freedom.library.swing.util.SwingParams;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.fnc.business.object.Cheque;
import org.freedom.modulos.fnc.view.frame.crud.plain.FBanco;
import org.freedom.modulos.fnc.view.frame.crud.tabbed.FConta;

/**
 * Cadastro cheques.
 * 
 * @author Setpoint Informática Ltda./Anderson Sanchez
 * @version 25/08/2010
 */

public class FCheque extends FDetalhe implements CarregaListener, InsertListener, DeleteListener, JComboBoxListener {

	private static final long serialVersionUID = 1L;

	private static final Color GREEN = new Color( 45, 190, 60 );

	private static final Color YELLOW = new Color( 255, 204, 51 );

	private JPanelPad panelMaster = new JPanelPad();

	private JPanelPad panelDetalhe = new JPanelPad();

	private JTextFieldPad txtSeqCheq = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtCodEmpCH = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtCodFilialCH = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodBanc = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private JTextFieldPad txtContaCheq = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtAgenciaCheq = new JTextFieldPad( JTextFieldPad.TP_STRING, 7, 0 );

	private JTextFieldFK txtDescConta = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldFK txtNomeBanc = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtDtEmitCheq = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDtVenctoCheq = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDtCompCheq = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtNomeEmitCheq = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNomeFavCheq = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldPad txtObsPag = new JTextFieldPad( JTextFieldPad.TP_STRING, 250, 0 );

	private JTextFieldPad txtNumCheq = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextAreaPad txaHistCheq = new JTextAreaPad( 500 );

	private JButtonPad btCompletar = new JButtonPad( Icone.novo( "btOk.png" ) );
	
	private JTextFieldPad txtCodPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
//	private JTextFieldPad txtRazaoFor = new JTextFieldPad( JTextFieldPad.TP_STRING, 100, 0 );

	private JTextFieldFK txtDocPag = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtNParcPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtVlrParcItRec = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldFK txtVlrApagItRec = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldFK txtVlrPagoItRec = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtVlrCheq = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldFK txtDtPagoItPag = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldFK txtDtVencItPag = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtStatusItPag = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtCnpjEmitCheq = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtCpfEmitCheq = new JTextFieldPad( JTextFieldPad.TP_STRING, 11, 0 );

	private JTextFieldPad txtCnpjFavCheq = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtCpfFavCheq = new JTextFieldPad( JTextFieldPad.TP_STRING, 11, 0 );

	private JTextFieldPad txtDDDEmitCheq = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtDDDFavCheq = new JTextFieldPad( JTextFieldPad.TP_STRING, 12, 0 );

	private JTextFieldPad txtFoneEmitCheq = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtFoneFavCheq = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );
	
	private JCheckBoxPad cbPreDatCheq = new JCheckBoxPad( "Pré-datado", "S", "N" );

	private JComboBoxPad cbTipoCheq = null;

	private JComboBoxPad cbSitCheq = null;
	
	private JLabelPad lbStatus = new JLabelPad( "", SwingConstants.CENTER );

	private ListaCampos lcConta = new ListaCampos( this, "" );

	private ListaCampos lcPlan = new ListaCampos( this, "PN" );

	private ListaCampos lcItPagar = new ListaCampos( this, "" );

	private ListaCampos lcPagar = new ListaCampos( this, "" );
	
//	private ListaCampos lcFornecedor = new ListaCampos( this, "" );
	
	protected final ListaCampos lcBanco = new ListaCampos( this, "BO" );

	public FCheque() {

		super( false );

		nav.setNavigation( true );

		setTitulo( "Cheque" );
		setAtribos( 50, 50, 780, 600 );

		montaListaCampos();
		montaTela();
		montaCombos();

		lcDet.addCarregaListener( this );
		lcCampos.addCarregaListener( this );
		lcItPagar.addCarregaListener( this );

		lcDet.addInsertListener( this );
		lcDet.addDeleteListener( this );
		lcDet.addPostListener( this );

		btCompletar.addActionListener( this );
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		cbTipoCheq.addComboBoxListener( this );
	}

	public void exec( int seqcheq ) {
		txtSeqCheq.setVlrInteger( seqcheq );
		lcCampos.carregaDados();
	}
	
	private void montaCombos() {
		montaCbTipoCheque();
		montaCbSitCheque();
	}

	private void montaCbTipoCheque() {
		cbTipoCheq.limpa();
		cbTipoCheq.setItens( Cheque.getLabelsTipoCheq(), Cheque.getValoresTipoCheq() );
	}

	private void montaCbSitCheque() {
		cbSitCheq.limpa();
		cbSitCheq.setItens( Cheque.getLabelsSitCheq(), Cheque.getValoresSitCheq() );
	}

	private void montaListaCampos() {
		lcConta.setUsaME( false );
		lcConta.add( new GuardaCampo( txtContaCheq, "NumConta", "Nº Conta", ListaCampos.DB_PK, false ) );
		lcConta.add( new GuardaCampo( txtDescConta, "DescConta", "Descrição da conta", ListaCampos.DB_SI, false ) );
		lcConta.montaSql( false, "CONTA", "FN" );
		lcConta.setReadOnly( true );
		txtContaCheq.setTabelaExterna( lcConta, FConta.class.getCanonicalName() );
		txtContaCheq.setFK( true );
		txtContaCheq.setNomeCampo( "numconta" );
		
		/***************
		 * FNBANCO *
		 ***************/
		
		lcBanco.add( new GuardaCampo( txtCodBanc, "CodBanco", "Cód.banco", ListaCampos.DB_PK, true ) );
		lcBanco.add( new GuardaCampo( txtNomeBanc, "NomeBanco", "Nome do Banco", ListaCampos.DB_SI, false ) );
		lcBanco.montaSql( false, "BANCO", "FN" );
		lcBanco.setQueryCommit( false );
		lcBanco.setReadOnly( true );
		txtCodBanc.setNomeCampo( "CodBanco" );
		txtCodBanc.setTabelaExterna( lcBanco, FBanco.class.getCanonicalName() );
		txtCodBanc.setListaCampos( lcBanco );
		txtCodBanc.setFK( true );

		//Fornecedor
//		lcFornecedor.add( new GuardaCampo( txtCodFor, "CodFor", "Cod. For.", ListaCampos.DB_PK, false ) );
//		lcFornecedor.add( new GuardaCampo( txtRazaoFor, "RazFor", "Razão For.", ListaCampos.DB_SI, false ) );
//		lcFornecedor.montaSql( false, "FORNECED", "CP" );
//		lcFornecedor.setQueryCommit( false );
//		lcFornecedor.setReadOnly( true );
//		txtCodFor.setTabelaExterna( lcFornecedor, null );
//		txtCodFor.setFK( true );
//		txtCodFor.setNomeCampo( "CodFor" );
		
		// Itens de contas a pagar
		lcItPagar.add( new GuardaCampo( txtCodPag, "CodPag", "Cód.Pag.", ListaCampos.DB_PK, false ) );
		lcItPagar.add( new GuardaCampo( txtNParcPag, "NParcPag", "Cód.It.pag.", ListaCampos.DB_PK, false ) );
		lcItPagar.add( new GuardaCampo( txtDtVencItPag, "DtVencItPag", "Vencimento", ListaCampos.DB_SI, false ) );
		lcItPagar.add( new GuardaCampo( txtDtPagoItPag, "DtPagoItPag", "Dt.Pagto.", ListaCampos.DB_SI, false ) );
		lcItPagar.add( new GuardaCampo( txtVlrParcItRec, "VlrParcItPag", "Vlr.titulo", ListaCampos.DB_SI, false ) );
		lcItPagar.add( new GuardaCampo( txtVlrApagItRec, "VlrApagItPag", "Vlr.Aberto", ListaCampos.DB_SI, false ) );
		lcItPagar.add( new GuardaCampo( txtVlrPagoItRec, "VlrPagoItPag", "Vlr.Pago", ListaCampos.DB_SI, false ) );
		lcItPagar.add( new GuardaCampo( txtStatusItPag, "StatusItPag", "Status", ListaCampos.DB_SI, false ) );
		// lcItReceber.setWhereAdic( " NOT EXISTS (SELECT B.NPARCITREC FROM FNITBORDERO B " +
		// "WHERE B.CODEMPRC=IR.CODEMP AND B.CODFILIALRC=IR.CODFILIAL AND " +
		// "B.CODREC=IR.CODREC AND B.NPARCITREC=IR.NPARCITREC) " );
		lcItPagar.montaSql( false, "ITPAGAR", "FN" );
		lcItPagar.setQueryCommit( false );
		lcItPagar.setReadOnly( true );
		txtCodPag.setTabelaExterna( lcItPagar, null );
		txtCodPag.setFK( true );
		txtCodPag.setNomeCampo( "CodPag" );
		txtNParcPag.setTabelaExterna( lcItPagar, null );
		txtNParcPag.setFK( true );
		txtNParcPag.setNomeCampo( "NParcPag" );
		
		// Contas a pagar
		lcPagar.add( new GuardaCampo( txtCodPag, "CodPag", "Cód.Pag.", ListaCampos.DB_PK, txtDocPag, false ) );
		lcPagar.add( new GuardaCampo( txtDocPag, "DocPag", "Documento", ListaCampos.DB_SI, false ) );
		lcPagar.add( new GuardaCampo( txtCodFor, "CodFor", "Cod.For.", ListaCampos.DB_SI, false ) );
		lcPagar.add( new GuardaCampo( txtObsPag, "ObsPag", "Obs.", ListaCampos.DB_SI, false ) );
		lcPagar.montaSql( false, "PAGAR", "FN" );
		lcPagar.setQueryCommit( false );
		lcPagar.setReadOnly( true );
		txtCodPag.setTabelaExterna( lcPagar, null );
		txtCodPag.setFK( true );
		txtCodPag.setNomeCampo( "CodPag" );
		
	}

	private void montaTela() {

		txtCpfEmitCheq.setMascara( JTextFieldPad.MC_CPF );
		txtCnpjEmitCheq.setMascara( JTextFieldPad.MC_CNPJ );

		txtCpfFavCheq.setMascara( JTextFieldPad.MC_CPF );
		txtCnpjFavCheq.setMascara( JTextFieldPad.MC_CNPJ );

		txtFoneEmitCheq.setMascara( JTextFieldPad.MC_FONE );
		txtFoneFavCheq.setMascara( JTextFieldPad.MC_FONE );

		cbTipoCheq = new JComboBoxPad( null, null, JComboBoxPad.TP_STRING, 2, 0 );
		cbSitCheq = new JComboBoxPad( null, null, JComboBoxPad.TP_STRING, 2, 0 );

		setAltCab( 380 );
//		lcCampos.setSigla( "CH" ); 
		setListaCampos( lcCampos );
		setPainel( panelMaster, pnCliCab );

		JPanelPad pnInfoCheque = new JPanelPad();
		pnInfoCheque.setBorder( SwingParams.getPanelLabel( "Informações do cheque", Color.BLACK ) );

		JPanelPad pnDatas = new JPanelPad();
		pnDatas.setBorder( SwingParams.getPanelLabel( "Datas", Color.BLUE ) );

		JPanelPad pnEmitente = new JPanelPad();
		pnEmitente.setBorder( SwingParams.getPanelLabel( "Informações do emitente", Color.RED ) );

		JPanelPad pnFavorecido = new JPanelPad();
		pnFavorecido.setBorder( SwingParams.getPanelLabel( "Informações do favorecido", Color.BLUE ) );

		adic( pnInfoCheque, 5, 0, 750, 120 );
		adic( pnDatas, 620, 124, 135, 150 );
		adic( pnEmitente, 5, 124, 615, 74 );
		adic( pnFavorecido, 5, 201, 615, 76 );

		/** INFORMACOES DO CHEQUE **/
		setPainel( pnInfoCheque );
		adicCampo( txtSeqCheq, 7, 15, 80, 20, "seqcheq", "Seq.", ListaCampos.DB_PK, true );
		adicCampo( txtCodBanc, 90, 15, 80, 20, "codbanc", "Banco", ListaCampos.DB_FK, true );
		adicCampo( txtContaCheq, 173, 15, 90, 20, "Contacheq", "Nº Conta", ListaCampos.DB_FK, txtDescConta, true );
		adicCampo( txtAgenciaCheq, 266, 15, 80, 20, "AgenciaCheq", "Agencia", ListaCampos.DB_SI, true );
		adicCampo( txtNumCheq, 349, 15, 90, 20, "Numcheq", "Nro.Cheq.", ListaCampos.DB_SI, true );
		adicCampo( txtVlrCheq, 442, 15, 130, 20, "VlrCheq", "Valor", ListaCampos.DB_SI, true );
		adicDB( cbTipoCheq, 575, 15, 145, 20, "tipocheq", "Tipo", false );
		adicDB( cbSitCheq, 575, 60, 145, 20, "sitcheq", "Status", false );
		
		txtCodBanc.setNomeCampo( "codbanco" );
		txtContaCheq.setNomeCampo( "numconta" );
		
		/** INFORMACOES DE DATAS **/
		setPainel( pnDatas );
		adicCampo( txtDtEmitCheq, 4, 15, 110, 20, "DtEmitCheq", "Emissão", ListaCampos.DB_SI, true );
		adicCampo( txtDtVenctoCheq, 4, 55, 110, 20, "DtVenctoCheq", "Vencimento", ListaCampos.DB_SI, true );
		adicCampo( txtDtCompCheq, 4, 95, 110, 20, "DtCompCheq", "Compem.", ListaCampos.DB_SI, false );

		/** INFORMACOES DO EMITENTE **/
		setPainel( pnEmitente );
		adicCampo( txtNomeEmitCheq, 7, 15, 175, 20, "nomeemitcheq", "Nome", ListaCampos.DB_SI, true );
		adicCampo( txtCnpjEmitCheq, 185, 15, 152, 20, "cnpjemitcheq", "CNPJ", ListaCampos.DB_SI, false );
		adicCampo( txtCpfEmitCheq, 338, 15, 130, 20, "cpfemitcheq", "CPF", ListaCampos.DB_SI, false );
		adicCampo( txtDDDEmitCheq, 471, 15, 40, 20, "dddemitcheq", "DDD", ListaCampos.DB_SI, false );
		adicCampo( txtFoneEmitCheq, 512, 15, 90, 20, "foneemitcheq", "Fone", ListaCampos.DB_SI, false );

		/** INFORMACOES DO FAVORECIDO **/
		setPainel( pnFavorecido );
		adicCampo( txtNomeFavCheq, 7, 15, 175, 20, "nomeFavcheq", "Nome", ListaCampos.DB_SI, true );
		adicCampo( txtCnpjFavCheq, 185, 15, 152, 20, "cnpjFavcheq", "CNPJ", ListaCampos.DB_SI, false );
		adicCampo( txtCpfFavCheq, 338, 15, 130, 20, "cpfFavcheq", "CPF", ListaCampos.DB_SI, false );
		adicCampo( txtDDDFavCheq, 471, 15, 40, 20, "dddFavcheq", "DDD", ListaCampos.DB_SI, false );
		adicCampo( txtFoneFavCheq, 512, 15, 90, 20, "foneFavcheq", "Fone", ListaCampos.DB_SI, false );

		setPainel( panelMaster, pnCliCab );
		adicDB( txaHistCheq, 7, 300, 750, 40, "histcheq", "Histórico", true );

		setListaCampos( true, "CHEQUE", "FN" );
		lcCampos.setQueryInsert( false );

		setAltDet( 60 );
		setPainel( panelDetalhe, pnDet );
		setListaCampos( lcDet );
		setNavegador( navRod );

		adicCampo( txtCodPag, 7, 20, 80, 20, "CodPag", "Cód.pag.", ListaCampos.DB_PK, false );		
		adicDescFK( txtDocPag, 90, 20, 70, 20, "docpag", "Doc." );
		adicCampo( txtNParcPag, 163, 20, 30, 20, "NParcPag", "Parc.", ListaCampos.DB_PF, false );
		
		adicDescFK( txtDtVencItPag, 196, 20, 90, 20, "DtVencItPag", "Vencimento" );
		adicDescFK( txtDtPagoItPag, 289, 20, 90, 20, "DtPagoItPag", "Dt.Pagto." );

		adicDescFK( txtVlrParcItRec, 381, 20, 100, 20, "VlrParcItRec", "Vlr.titulo" );
		adicDescFK( txtVlrPagoItRec, 484, 20, 100, 20, "VlrPagoItRec", "Vlr.Pago" );
		adicDescFK( txtVlrApagItRec, 587, 20, 100, 20, "VlrApagItRec", "Vlr.Aberto" );

		adic( lbStatus, 691, 20, 70, 20 );
		
		txtCodPag.setNomeCampo( "CodPag" );
		txtNParcPag.setNomeCampo( "NParcPag" );

		adicCampoInvisivel( txtCodEmpCH, "codempch", "codempch", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtCodFilialCH, "codfilialch", "codfilialch", ListaCampos.DB_SI, false );

		setListaCampos( false, "PAGCHEQ", "FN" );
		lcDet.setQueryInsert( false );

		montaTab();
		tab.adicColuna( "Cod For." );//4
		tab.adicColuna( "Nome Fornecedor" );//5
		tab.setColunaInvisivel( 2 );//codempch
		tab.setColunaInvisivel( 3 );//codfilialch
		
		tab.setTamColuna( 300, 5 );
		
		
		pnGImp.removeAll();
		pnGImp.setLayout( new GridLayout( 1, 3 ) );
		pnGImp.setPreferredSize( new Dimension( 93, 26 ) );
		pnGImp.add( btCompletar );
		pnGImp.add( btImp );
		pnGImp.add( btPrevimp );

		setImprimir( true );

		txtCodPag.setFK( true );
		navRod.setAtivo( Navegador.BT_EDITAR, false );

		lbStatus.setForeground( Color.WHITE );
		lbStatus.setFont( new Font( "Arial", Font.BOLD, 13 ) );
		lbStatus.setOpaque( true );
		lbStatus.setVisible( false );
	}

	private void showStatus() {

		lbStatus.setVisible( true );

		if ( "RB".equals( txtStatusItPag.getVlrString() ) ) {
			lbStatus.setVisible( false );
		} else if ( "CR".equals( txtStatusItPag.getVlrString() ) ) {
			lbStatus.setText( "Cancelada" );
			lbStatus.setBackground( Color.DARK_GRAY );
		} else if ( "RP".equals( txtStatusItPag.getVlrString() ) && txtVlrApagItRec.getVlrBigDecimal().doubleValue() == 0 ) {
			lbStatus.setText( "Recebida" );
			lbStatus.setBackground( GREEN );
		} else if ( txtVlrPagoItRec.getVlrBigDecimal().doubleValue() > 0 ) {
			lbStatus.setText( "Rec. parcial" );
			lbStatus.setBackground( Color.BLUE );
		}
		
		if ( txtDtVencItPag.getVlrDate() != null ) {
			if ( txtDtVencItPag.getVlrDate().before( Calendar.getInstance().getTime() ) ) {
				lbStatus.setText( "Vencida" );
				lbStatus.setBackground( Color.RED );
			} else if ( txtDtVencItPag.getVlrDate().after( Calendar.getInstance().getTime() ) ) {
				lbStatus.setText( "À vencer" );
				lbStatus.setBackground( YELLOW );
			}
		} else {
			lbStatus.setVisible( false );
		}
	}

	private synchronized void imprimir( TYPE_PRINT visualizar ) {
		PreparedStatement ps = null;

		ImprimeOS imp = new ImprimeOS( "", Aplicativo.getInstace().getConexao(), "CH", true );

		imp.setImpEject( false );
		Vector<Object> item = null;
		Map<String, Object> itemMap = null;

		// imprime comprimido na coluna 2 para utiliar a linha 0
		imp.say( 0, 2, imp.comprimido() );

		Cheque cheque = new Cheque( txtSeqCheq.getVlrInteger() );

		itemMap = cheque.montaMap() ;

		cheque.montaLayoutCheq( imp, itemMap );

		if ( visualizar == TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
	}

	@ Override
	public void actionPerformed( ActionEvent e ) {
		if ( e.getSource() == btImp ) {
			imprimir( TYPE_PRINT.PRINT);
		} else if ( e.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		} else {
			super.actionPerformed( e );
		}
	}

	public void beforeCarrega( CarregaEvent e ) { }

	public void afterCarrega( CarregaEvent e ) {
		showStatus();
		
		if ( e.getListaCampos() == lcDet ) {
			lcPagar.carregaDados();
		}else if(e.getListaCampos() == lcCampos){
			carregaFornecedor();
		}
	}
	
	private void carregaFornecedor(){
		StringBuilder sql = new StringBuilder();
		sql.append( "select f.codfor, f.razfor from cpforneced f ");
		sql.append( "inner join fnpagar p on p.codemp = f.codemp and p.codfilial = f.codfilial and p.codfor = f.codfor ");
		sql.append( "where f.codemp = ? and f.codfilial = ? and p.codpag = ? " );
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			for(int i = 0; i < tab.getNumLinhas(); i++){
				Integer codPag = (Integer) tab.getValor( i, 0 );
				
				ps = con.prepareStatement( sql.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, Aplicativo.iCodFilial );
				ps.setInt( 3, codPag );
				
				rs = ps.executeQuery();
				if(rs.next()){
					tab.setValor( rs.getString( "codfor" ), i, 4 );
					tab.setValor( rs.getString( "razfor" ), i, 5 );
				}
			}
		} catch ( SQLException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void beforeInsert( InsertEvent e ) {
		lbStatus.setVisible( false );
	}

	public void afterInsert( InsertEvent e ) { }

	public void beforeDelete( DeleteEvent e ) { }

	public void afterDelete( DeleteEvent e ) {
		lbStatus.setVisible( false );
	}

	@ Override
	public void beforePost( PostEvent e ) {
		super.beforePost( e );

		if(e.getListaCampos() == lcCampos ) {
			if(Cheque.SIT_CHEQUE_CANCELADO.getValue().equals( cbSitCheq.getVlrString() )) {
				
				StringBuilder mensagem = new StringBuilder();
				
				mensagem.append( "O cancelamento do cheque implica no estorno do pagamento da conta \n");
				mensagem.append( "bem como a exclusão dos vínculos entre o cheque e a conta!\n");
				mensagem.append( "Confirma o cancelamento?" );
				
				if( ( Funcoes.mensagemConfirma( this, mensagem.toString() ) == JOptionPane.NO_OPTION) ) {
					e.cancela();
				}
			}
		} else if(e.getListaCampos() == lcDet ) {
			txtCodEmpCH.setVlrInteger( lcCampos.getCodEmp() );
			txtCodFilialCH.setVlrInteger( lcCampos.getCodFilial() );
		}
	}

	public void afterPost( PostEvent e ) {
		super.afterPost( e );
		showStatus();
	}

	public void setConexao( DbConnection con ) {
		super.setConexao( con );

		lcConta.setConexao( con );
		lcItPagar.setConexao( con );
		lcPagar.setConexao( con );
		lcBanco.setConexao( con );
//		lcFornecedor.setConexao( con );
	}

	public void valorAlterado( JComboBoxEvent evt ) { }
}
