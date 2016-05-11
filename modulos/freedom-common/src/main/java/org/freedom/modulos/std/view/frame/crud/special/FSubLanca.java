/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FSubLanca.java <BR>
 * 
 *                    Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                    modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                    na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                    Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                    sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                    Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                    Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                    de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                    Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.std.view.frame.crud.special;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.event.InternalFrameEvent;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.DeleteEvent;
import org.freedom.acao.DeleteListener;
import org.freedom.acao.EditEvent;
import org.freedom.acao.EditListener;
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
import org.freedom.library.functions.Funcoes;
import org.freedom.library.functions.FuncoesCRM;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDetalhe;
import org.freedom.modulos.crm.view.frame.crud.detail.FContrato;
import org.freedom.modulos.fnc.library.swing.component.JTextFieldPlan;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCliente;
import org.freedom.modulos.std.view.frame.crud.tabbed.FFornecedor;

public class FSubLanca extends FDetalhe implements RadioGroupListener, FocusListener, InsertListener, EditListener, PostListener, DeleteListener, ActionListener, CarregaListener, JComboBoxListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCab = new JPanelPad( 500, 200 );

	private JPanelPad pinDet = new JPanelPad( 500, 130 );

	private JTextFieldPad txtCodLanca = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDataLanca = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDocLanca = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private JTextFieldPlan txtCodPlan = new JTextFieldPlan( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtCodEmpPlan = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodFilialPlan = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtHistLanca = new JTextFieldPad( JTextFieldPad.TP_STRING, 100, 0 );

	private JTextFieldPad txtHistSubLanca = new JTextFieldPad( JTextFieldPad.TP_STRING, 100, 0 );

	private JTextFieldPlan txtCodPlanSub = new JTextFieldPlan( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldFK txtDescPlan = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodCC = new JTextFieldPad( JTextFieldPad.TP_STRING, 19, 0 );

	private JTextFieldPad txtAnoCC = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );

	private JTextFieldFK txtSiglaCC = new JTextFieldFK( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldFK txtDescCC = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtVlrLanca = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtCodSubLanca = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 2, 0 );

	private JTextFieldPad txtVlrAtualLanca = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtCodContr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtCodRec = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodItContr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldFK txtRazFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextAreaPad txaHistLanca = new JTextAreaPad();

	private JCheckBoxPad cbTransf = new JCheckBoxPad( "Sim!", "S", "N" );

	private JScrollPane spnTxa = new JScrollPane( txaHistLanca );

	private JButtonPad btSalvar = new JButtonPad( Icone.novo( "btSalvar.gif" ) );

	private JButtonPad btNovo = new JButtonPad( Icone.novo( "btNovo.png" ) );

	private ListaCampos lcPlan = new ListaCampos( this, "PN" );

	private ListaCampos lcCC = new ListaCampos( this, "CC" );

	private ListaCampos lcCli = new ListaCampos( this, "CL" );

	private ListaCampos lcFor = new ListaCampos( this, "FR" );

	private ListaCampos lcContrato = new ListaCampos( this, "CT" );

	private ListaCampos lcItContrato = new ListaCampos( this, "CT" );
	
	//private ListaCampos lcLanca = new ListaCampos(this);

	private String sCodLanca = "";

	private String sCodPlan = "";

	private Date dIni = null;

	private Date dFim = null;

	private JRadioGroup<?, ?> rgTipoLanca;

	private JLabelPad lbRazCli = null;

	private JLabelPad lbRazFor = null;

	private JLabelPad lbCodCli = null;

	private JLabelPad lbCodFor = null;

	private HashMap<String, Object> prefere = null;

	private JPanelPad pnTipoLanca = new JPanelPad();

	private Vector<String> vValsTipo = null;

	private Vector<String> vLabsTipo = null;

	private Vector<Integer> vValsContr = new Vector<Integer>();

	private Vector<String> vLabsContr = new Vector<String>();

	private JComboBoxPad cbContr = new JComboBoxPad( vLabsContr, vValsContr, JComboBoxPad.TP_INTEGER, 8, 0 );

	private Vector<Integer> vValsitContr = new Vector<Integer>();

	private Vector<String> vLabsitContr = new Vector<String>();

	private JComboBoxPad cbitContr = new JComboBoxPad( vLabsitContr, vValsitContr, JComboBoxPad.TP_INTEGER, 8, 0 );

	private boolean bfoco = true;
	
	public enum COL_VALS{ CODLANCA, DATALANCA, TRANSF, DOCLANCA, VLRATUALLANCA, HISTLANCA, CODPLAN }

	public FSubLanca( String sCodL, String sCodP, Date dini, Date dfim ) {

		lcItContrato.setMaster( lcCampos );
		dIni = dini;
		dFim = dfim;

		sCodLanca = sCodL;
		sCodPlan = sCodP;

		pnCab.remove( 1 ); // Remove o navegador do cabeçalho

		setTitulo( "Sub-Lançamentos" );
		setAtribos( 20, 1, 655, 500 );

		prefere = getPrefere( Aplicativo.getInstace().getConexao() );

		txtVlrAtualLanca.setAtivo( false );

		txtCodPlan.setTipo( JTextFieldPad.TP_STRING, 13, 0 );
		lcPlan.add( new GuardaCampo( txtCodPlanSub, "CodPlan", "Cód.planejamento", ListaCampos.DB_PK, txtDescPlan, false ) );
		lcPlan.add( new GuardaCampo( txtDescPlan, "DescPlan", "Descrição do planejamento", ListaCampos.DB_SI, false ) );
		lcPlan.setReadOnly( true );
		lcPlan.setQueryCommit( false );
		lcPlan.montaSql( false, "PLANEJAMENTO", "FN" );
		txtCodPlanSub.setTabelaExterna( lcPlan, FPlanejamento.class.getCanonicalName() );
		txtDescPlan.setListaCampos( lcPlan );

		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli", ListaCampos.DB_PK, txtRazCli, false ) );
		lcCli.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCli.setReadOnly( true );
		lcCli.setQueryCommit( false );
		lcCli.montaSql( false, "CLIENTE", "VD" );
		txtCodCli.setTabelaExterna( lcCli, FCliente.class.getCanonicalName() );
		txtRazCli.setListaCampos( lcCli );

		lcFor.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.for.", ListaCampos.DB_PK, txtDescPlan, false ) );
		lcFor.add( new GuardaCampo( txtRazFor, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );
		lcFor.setReadOnly( true );
		lcFor.setQueryCommit( false );
		lcFor.montaSql( false, "FORNECED", "CP" );
		txtCodFor.setTabelaExterna( lcFor, FFornecedor.class.getCanonicalName() );
		txtRazFor.setListaCampos( lcFor );

		lcCC.add( new GuardaCampo( txtCodCC, "CodCC", "Cód.c.c.", ListaCampos.DB_PK, txtDescCC, false ) );
		lcCC.add( new GuardaCampo( txtAnoCC, "AnoCC", "Ano-Base", ListaCampos.DB_PK, txtDescCC, false ) );
		lcCC.add( new GuardaCampo( txtDescCC, "DescCC", "Descrição do centro de custo", ListaCampos.DB_SI, false ) );
		lcCC.add( new GuardaCampo( txtSiglaCC, "SiglaCC", "Sigla", ListaCampos.DB_SI, false ) );
		lcCC.setReadOnly( true );
		lcCC.setQueryCommit( false );
		lcCC.setWhereAdic( "NIVELCC=10" );
		lcCC.montaSql( false, "CC", "FN" );
		txtCodCC.setTabelaExterna( lcCC, FCentroCusto.class.getCanonicalName() );
		txtAnoCC.setTabelaExterna( lcCC, FCentroCusto.class.getCanonicalName() );
		txtDescCC.setListaCampos( lcCC );
		txtSiglaCC.setListaCampos( lcCC );

		lcContrato.add( new GuardaCampo( txtCodContr, "CodContr", "Cód.Contr.", ListaCampos.DB_PK, false ) );
		lcContrato.setReadOnly( true );
		lcContrato.setQueryCommit( false );
		lcContrato.montaSql( false, "CONTRATO", "VD" );
		txtCodContr.setTabelaExterna( lcContrato, FContrato.class.getCanonicalName() );

		lcItContrato.add( new GuardaCampo( txtCodContr, "CodContr", "Cód.Contr.", ListaCampos.DB_PK, false ) );
		lcItContrato.add( new GuardaCampo( txtCodItContr, "CodItContr", "Cód.It.Contr.", ListaCampos.DB_PK, false ) );
		lcItContrato.setReadOnly( true );
		lcItContrato.setQueryCommit( false );
		lcItContrato.montaSql( false, "ITCONTRATO", "VD" );
		txtCodItContr.setTabelaExterna( lcItContrato, null );
		
		//lcLanca.add( new GuardaCampo( txtCodLanca, "CodLanca", "Cód.Lanc.", ListaCampos.DB_PK, false ) );
//		lcLanca.add( new GuardaCampo( txtCodPag, "CodPag", "Cód.Pag.", ListaCampos.DB_SI, false ) );
//		lcLanca.add( new GuardaCampo( txtCodRec, "CodRec", "Cód.Rec.", ListaCampos.DB_SI, false ) );
		//lcLanca.setReadOnly( true );
		//lcLanca.setQueryCommit( false );
		//lcLanca.montaSql( false, "LANCA", "FN" );
		//txtCodLanca.setTabelaExterna( lcLanca, null );

		setAltCab( 190 );
		setListaCampos( lcCampos );
		setPainel( pinCab, pnCliCab );
		adicCampo( txtCodLanca, 7, 20, 80, 20, "CodLanca", "Nº Lçto.", ListaCampos.DB_PK, true );
		adicCampoInvisivel( txtCodPlan, "CodPlan", "Cod. Planejamento", ListaCampos.DB_SI, true );
		adicCampoInvisivel( txtCodEmpPlan, "CodEmpPN", "Emp. Planejamento", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtCodFilialPlan, "CodFilialPN", "Filial. Planejamento", ListaCampos.DB_SI, false );
		adicCampo( txtDataLanca, 90, 20, 97, 20, "DataLanca", "Data", ListaCampos.DB_SI, true );
		adicCampo( txtDocLanca, 190, 20, 77, 20, "DocLanca", "Doc.", ListaCampos.DB_SI, false );
		adicCampo( txtHistLanca, 270, 20, 355, 20, "HistBLanca", "Histório Bancário", ListaCampos.DB_SI, true );

		adicDB( cbTransf, 7, 60, 80, 20, "TransfLanca", "Transferência", true );
		adic( new JLabelPad( "Vlr. Lançamento" ), 95, 40, 100, 20 );
		adic( txtVlrAtualLanca, 95, 60, 97, 20 );

		JPanelPad pnTxa = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );
		pnTxa.add( spnTxa );
		adic( pnTxa, 7, 95, 618, 50 );
		adicDBLiv( txaHistLanca, "HistLanca", "Histórico descriminado", false );
		adicDB( rgTipoLanca, 210, 60, 414, 28, "tipolanca", "Tipo de lançamento", true );

		adic( btNovo, 7, 150, 30, 30 );
		adic( btSalvar, 37, 150, 30, 30 );

		setListaCampos( true, "LANCA", "FN" );
		//setAltDet( 100 );
		setPainel( pinDet, pnDet );

		setListaCampos( lcDet );
		setNavegador( navRod );
		adicCampoInvisivel( txtCodSubLanca, "CodSubLanca", "Item", ListaCampos.DB_PK, false );
		adicCampo( txtCodPlanSub, 7, 20, 100, 20, "CodPlan", "Cód.Planej.", ListaCampos.DB_FK, txtDescPlan, true );
		adicDescFK( txtDescPlan, 110, 20, 207, 20, "DescPlan", "Descrição do plano de contas" );

		adicCampo( txtCodCC, 320, 20, 100, 20, "CodCC", "Cód.c.c.", ListaCampos.DB_FK, txtDescCC, false );
		adicCampoInvisivel( txtAnoCC, "AnoCC", "Ano-base", ListaCampos.DB_FK, txtDescCC, false );
		adicDescFK( txtDescCC, 423, 20, 202, 20, "DescCC", "Descrição do centro de custo" );
		adicCampo( txtVlrLanca, 7, 60, 100, 20, "VlrSubLanca", " Valor", ListaCampos.DB_SI, true );
		adicCampo( txtHistSubLanca, 110, 60, 516, 20, "HistSubLanca", "Histórico do lançamento", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtCodPag, "CodPag", "Cód.Pag.", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtCodRec, "CodRec", "Cód.Rec.", ListaCampos.DB_SI, false );

		txtCodCli.setRequerido( true );
		txtCodFor.setRequerido( true );
		txtCodCli.setVisible( false );
		txtRazCli.setVisible( false );
		txtCodFor.setVisible( false );
		txtRazFor.setVisible( false );

		adic( pnTipoLanca, 7, 85, 618, 55 );
		pnTipoLanca.setVisible( false );

		setPainel( pnTipoLanca );

		lbCodCli = adicCampo( txtCodCli, 2, 18, 95, 20, "CodCli", "Cód. Cliente", ListaCampos.DB_FK, false );
		lbRazCli = adicDescFK( txtRazCli, 100, 18, 503, 20, "RazCli", "Razão social do cliente" );

		lbCodFor = adicCampo( txtCodFor, 2, 18, 95, 20, "CodFor", "Cód. Fornecedor", ListaCampos.DB_FK, false );
		lbRazFor = adicDescFK( txtRazFor, 100, 18, 503, 20, "RazFor", "Razão social do fornecedor" );

		if ( "S".equals( (String) prefere.get( "LANCAFINCONTR" ) ) ) {
			adicCampoInvisivel( txtCodContr, "CodContr", "Cod.Contr.", ListaCampos.DB_FK, false );
			// adicCampoInvisivel( txtCodContr2, "CodContr", "Cod.Contr.", ListaCampos.DB_FK, false );
			adicCampoInvisivel( txtCodItContr, "CodItContr", "Cod.It.Contr.", ListaCampos.DB_SI, false );
		}

		lbCodCli.setVisible( false );
		lbRazCli.setVisible( false );
		lbCodFor.setVisible( false );
		lbRazFor.setVisible( false );

		adic( new JLabelPad( "Contrato/Projeto" ), 3, 40, 284, 20 );
		adic( cbContr, 3, 60, 284, 20 );
		adic( new JLabelPad( "Item" ), 294, 40, 320, 20 );
		adic( cbitContr, 294, 60, 310, 20 );

		lcDet.setWhereAdic( "CODSUBLANCA > 0" );
		setListaCampos( true, "SUBLANCA", "FN" );
		montaTab();

		tab.setTamColuna( 30, 0 );
		tab.setTamColuna( 95, 1 );
		tab.setTamColuna( 200, 2 );
		tab.setTamColuna( 0, 3 );
		tab.setTamColuna( 0, 4 );
		tab.setTamColuna( 0, 5 );
		tab.setTamColuna( 200, 7 );

		txtCodPlanSub.addFocusListener( this );
		lcCampos.addPostListener( this );
		lcCampos.addEditListener( this );
		lcDet.addInsertListener( this);
		lcDet.addPostListener( this );
		lcDet.addDeleteListener( this );
		lcDet.addEditListener( this );
		btSalvar.addActionListener( this );
		btNovo.addActionListener( this );
		lcCli.addCarregaListener( this );
		lcDet.addCarregaListener( this );
		cbContr.addComboBoxListener( this );
		cbitContr.addComboBoxListener( this );
		rgTipoLanca.addRadioGroupListener( this );

	}

	private void atualizaSaldo() {

		try {
			PreparedStatement ps = con.prepareStatement( "SELECT VLRLANCA FROM FNLANCA WHERE CODEMP=? AND CODFILIAL=? AND CODLANCA=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNLANCA" ) );
			ps.setInt( 3, txtCodLanca.getVlrInteger().intValue() );
			ResultSet rs = ps.executeQuery();
			if ( rs.next() )
				txtVlrAtualLanca.setVlrBigDecimal( new BigDecimal( rs.getString( "VlrLanca" ) ) );
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao atualizar o saldo!\n" + err.getMessage(), true, con, err );
		}
	}
/*	
	private void verificaCodPagERec() {

		try {
			PreparedStatement ps = con.prepareStatement( "select CODPAG, CODREC from fnlanca where CODEMP=? AND CODFILIAL= ? AND CODLANCA=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNLANCA" ) );
			ps.setInt( 3, txtCodLanca.getVlrInteger().intValue() );
			ResultSet rs = ps.executeQuery();
			if ( rs.next() ){
				txtCodPag.setVlrInteger( new Integer( rs.getInt( "CODPAG" ) ) );
				txtCodRec.setVlrInteger( new Integer( rs.getInt( "CODREC" ) ) );
			}
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao Consultar CodPag e CodRec!\n" + err.getMessage(), true, con, err );
		}
		
	}
*/
	public void valorAlterado( RadioGroupEvent rgevt ) {

		if ( rgTipoLanca.getVlrString().compareTo( "A" ) == 0 ) {
			setAtribos( this.getX(), this.getY(), 655, 450 );
			setAltDet( 100 );

			txtCodCli.setVisible( false );
			txtRazCli.setVisible( false );
			txtCodFor.setVisible( false );
			txtRazFor.setVisible( false );

			lbCodCli.setVisible( false );
			lbRazCli.setVisible( false );
			lbCodFor.setVisible( false );
			lbRazFor.setVisible( false );

			pnTipoLanca.setVisible( false );

		}
		else if ( rgTipoLanca.getVlrString().compareTo( "F" ) == 0 ) {
			setAtribos( this.getX(), this.getY(), 655, 520 );
			setAltDet( 200 );
			pnTipoLanca.setBorder( BorderFactory.createTitledBorder( "" ) );
			pnTipoLanca.setSize( pnTipoLanca.getWidth(), 55 + 40 );

			pnTipoLanca.setVisible( true );
			txtCodCli.setVisible( false );
			txtRazCli.setVisible( false );
			txtCodFor.setVisible( true );
			txtRazFor.setVisible( true );

			lbCodCli.setVisible( false );
			lbRazCli.setVisible( false );
			lbCodFor.setVisible( true );
			lbRazFor.setVisible( true );
		}
		else if ( rgTipoLanca.getVlrString().compareTo( "C" ) == 0 ) {
			setAtribos( this.getX(), this.getY(), 655, 520 );
			setAltDet( 160 );
			pnTipoLanca.setBorder( BorderFactory.createTitledBorder( "" ) );
			pnTipoLanca.setSize( pnTipoLanca.getWidth(), 55 );

			pnTipoLanca.setVisible( true );
			txtCodCli.setVisible( true );
			txtRazCli.setVisible( true );
			txtCodFor.setVisible( false );
			txtRazFor.setVisible( false );

			lbCodCli.setVisible( true );
			lbRazCli.setVisible( true );
			lbCodFor.setVisible( false );
			lbRazFor.setVisible( false );
		}
		else if ( rgTipoLanca.getVlrString().compareTo( "O" ) == 0 ) {
			setAtribos( this.getX(), this.getY(), 655, 540 );
			setAltDet( 200 );
			pnTipoLanca.setBorder( BorderFactory.createTitledBorder( "" ) );
			pnTipoLanca.setSize( pnTipoLanca.getWidth(), 55 + 40 );
			pnTipoLanca.setVisible( true );

			txtCodCli.setVisible( true );
			txtRazCli.setVisible( true );

			txtCodFor.setVisible( false );
			txtRazFor.setVisible( false );

			lbCodCli.setVisible( true );
			lbRazCli.setVisible( true );

			lbCodFor.setVisible( false );
			lbRazFor.setVisible( false );
		}

	}

	private void carregar() {

		if ( sCodLanca == null ) {
			lcCampos.insert( true );
			txtCodPlan.setText( sCodPlan );
			txtCodEmpPlan.setVlrString( "" + Aplicativo.iCodEmp );
			txtCodFilialPlan.setVlrString( "" + ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
			txtVlrAtualLanca.setVlrBigDecimal( new BigDecimal( 0 ) );
			cbTransf.setVlrString( "N" );
			cbTransf.setEnabled( true );
			txtDataLanca.setVlrDate( dFim );
			txtDataLanca.requestFocus();
		}
		else {
			txtCodLanca.setText( sCodLanca );
			lcCampos.carregaDados();
			lcCampos.edit();
			atualizaSaldo();
			txtDataLanca.requestFocus();
			if ( tab.getNumLinhas() > 0 ) {
				cbTransf.setEnabled( false );
				rgTipoLanca.setAtivo( false );
			}
			else {
				rgTipoLanca.setAtivo( true );
			}
		}

	}

	private void novo() {

		fireInternalFrameEvent( InternalFrameEvent.INTERNAL_FRAME_CLOSED );
		sCodLanca = null;
		carregar();
	}

	private boolean testaData() {

		boolean bRetorno = false;
		if ( ( txtDataLanca.getVlrDate().after( dFim ) ) || ( txtDataLanca.getVlrDate().before( dIni ) ) ) {
			txtDataLanca.requestFocus();
			Funcoes.mensagemInforma( null, "Data não está contida no periodo inicial!" );
		}
		else
			bRetorno = true;
		return bRetorno;
	}

	public Object[] getValores() {

		Object[] result = new Object[ COL_VALS.values().length ];
		result[ COL_VALS.CODLANCA.ordinal() ] = txtCodLanca.getVlrString().trim();
		result[ COL_VALS.DATALANCA.ordinal() ] = txtDataLanca.getVlrString().trim();
		result[ COL_VALS.TRANSF.ordinal() ] = cbTransf.getVlrString().trim();
		result[ COL_VALS.DOCLANCA.ordinal() ] = txtDocLanca.getVlrString().trim();
		
		result[ COL_VALS.VLRATUALLANCA.ordinal() ] = txtVlrAtualLanca.getVlrString().trim();
		
		result[ COL_VALS.HISTLANCA.ordinal() ] = txtHistLanca.getVlrString().trim();
		result[ COL_VALS.CODPLAN.ordinal() ] = txtCodPlan.getVlrString().trim();
		return result;
	}

	public void focusGained( FocusEvent fevt ) {

		if ( fevt.getSource() == txtCodPlanSub ) {
			if ( ( ( lcCampos.getStatus() == ListaCampos.LCS_EDIT ) || ( lcCampos.getStatus() == ListaCampos.LCS_INSERT ) ) && ( testaData() ) ) {
				if ( bfoco ) { // Flag para evitar loop infinito no focusgained. (devido a abertura de dialog de consistencia de campos)
					bfoco = false;
					lcCampos.post();
				}
				else {
					bfoco = true;
				}
			}
			if ( cbTransf.getVlrString() == "S" ) {
				lcPlan.setWhereAdic( "NIVELPLAN=6 AND TIPOPLAN IN ('C','B')" );
				txtAnoCC.setVlrString( "" );
				txtCodCC.setAtivo( false );
			}
			else {
				lcPlan.setWhereAdic( "NIVELPLAN=6 AND TIPOPLAN IN ('D','R')" );
				txtAnoCC.setVlrInteger( (Integer) prefere.get( "ANOCC" ) );
				txtCodCC.setAtivo( true );
			}
			lcPlan.montaSql( false, "PLANEJAMENTO", "FN" );
		}
	}

	private void testaCodLanca() { // Traz o verdadeiro número do codvenda através do generator do banco

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement( "SELECT * FROM SPGERANUM(?,?,?)" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNLANCA" ) );
			ps.setString( 3, "LA" );
			rs = ps.executeQuery();
			rs.next();
			txtCodLanca.setText( rs.getString( 1 ) );
			// rs.close();
			// ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao confirmar código do lanca!\n" + err.getMessage(), true, con, err );
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btNovo ) {
			novo();
		}
		else if ( evt.getSource() == btSalvar ) {
			if ( ( ( lcCampos.getStatus() == ListaCampos.LCS_EDIT ) || ( lcCampos.getStatus() == ListaCampos.LCS_INSERT ) ) && ( testaData() ) )
				lcCampos.post();
			if ( cbTransf.getVlrString() == "S" ) {
				lcPlan.setWhereAdic( "NIVELPLAN=6 AND TIPOPLAN IN ('C','B')" );
				txtAnoCC.setVlrString( "" );
				txtCodCC.setAtivo( false );
			}
			else {
				lcPlan.setWhereAdic( "NIVELPLAN=6 AND TIPOPLAN IN ('D','R')" );
				txtAnoCC.setVlrInteger( (Integer) prefere.get( "ANOCC" ) );
				txtCodCC.setAtivo( true );
			}
			lcPlan.montaSql( false, "PLANEJAMENTO", "FN" );
		}
		super.actionPerformed( evt );
	}
	
	
	public void afterInsert( InsertEvent ievt ) {

		// TODO Auto-generated method stub
		
	}

	public void beforeInsert( InsertEvent ievt ) {
		if(ievt.getListaCampos() == lcDet){
			if(!consist(true)){
				ievt.cancela();
			} 
		}	
	}

	public void afterPost( PostEvent pevt ) {

		if ( pevt.getListaCampos() == lcDet ) {
			atualizaSaldo();
			if ( tab.getNumLinhas() > 0 )
				cbTransf.setEnabled( false );
		}
		else if ( pevt.getListaCampos() == lcCampos ) {
			btSalvar.setEnabled( false );
		}

		if ( tab.getNumLinhas() > 0 ) {
			cbTransf.setEnabled( false );
			rgTipoLanca.setAtivo( false );
		}
		else {
			cbTransf.setEnabled( true );
			rgTipoLanca.setAtivo( true );
		}
	}
	private boolean consist(boolean mensagem){
		boolean result = true;
		if( ( txtCodRec.getVlrInteger() > 0 ) || ( txtCodPag.getVlrInteger() >0 ) ){
			result = false;
			if (mensagem) {
				Funcoes.mensagemInforma( this, 
						"Operação não permitida!\nEsta operação deve ser efetuada pela tela de manutenção de contas a "+
						(txtCodRec.getVlrInteger()>0?"receber":"pagar") + " !");
			}
		}
		return result;
	}

	public void beforePost( PostEvent pevt ) {

		if ( pevt.getListaCampos() == lcCampos ) {
			if ( lcCampos.getStatus() == ListaCampos.LCS_INSERT ) {
				testaCodLanca();
			}
		}
		if ( pevt.getListaCampos() == lcDet ) {
			
			if ( ( rgTipoLanca.getVlrString().equals( "C" ) ) && ( txtCodCli.getVlrString().trim().equals( "" ) ) ) {
				Funcoes.mensagemErro( this, "Código do cliente não pode ser nulo!" );
				pevt.cancela();
			} else if ( ( rgTipoLanca.getVlrString().equals( "F" ) ) && ( txtCodFor.getVlrString().trim().equals( "" ) ) ) {
				Funcoes.mensagemErro( this, "Código do fornecedor não pode ser nulo!" );
				pevt.cancela();
			} else {
				System.out.println( "codcontr:" + txtCodContr.getVlrInteger() );
				System.out.println( "coditcontr:" + txtCodItContr.getVlrInteger() );
			}
			/*
			 * if ( (lcCampos.getStatus() == ListaCampos.LCS_INSERT ) && ( testaData() ) ) { lcCampos.post(); lcDet.cancelInsert(); }
			 */
		}
		
		
	}

	public void afterEdit( EditEvent eevt ) {

		if ( eevt.getListaCampos() == lcCampos ) {
			btSalvar.setEnabled( true );
		}
	}

	public void afterDelete( DeleteEvent devt ) {
		atualizaSaldo();
	}

	public void beforeDelete( DeleteEvent devt ) {
		if (devt.getListaCampos()==lcDet) {
			if(!consist(true)){
				devt.cancela();
			}
		}
	}

	public void beforeEdit( EditEvent eevt ) {
	}

	public void focusLost( FocusEvent fevt ) {

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
//		lcLanca.setConexao( cn );
		lcPlan.setConexao( cn );
		lcCC.setConexao( cn );
		lcFor.setConexao( cn );
		lcCli.setConexao( cn );
		lcContrato.setConexao( cn );
		lcItContrato.setConexao( cn );

		carregar();
	}

	private HashMap<String, Object> getPrefere( DbConnection con ) {

		HashMap<String, Object> prefere = new HashMap<String, Object>();
		String sSQL = "SELECT ANOCENTROCUSTO, LANCAFINCONTR FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				prefere.put( "ANOCC", rs.getInt( "ANOCENTROCUSTO" ) );
				prefere.put( "LANCAFINCONTR", rs.getString( "LANCAFINCONTR" ) );

			}

			vValsTipo = new Vector<String>();
			vValsTipo.addElement( "A" );
			vValsTipo.addElement( "F" );
			vValsTipo.addElement( "C" );

			vLabsTipo = new Vector<String>();
			vLabsTipo.addElement( "Avulso" );
			vLabsTipo.addElement( "Fornecedor" );
			vLabsTipo.addElement( "Cliente" );

			if ( "S".equals( (String) prefere.get( "LANCAFINCONTR" ) ) ) {
				vValsTipo.addElement( "O" );
				vLabsTipo.addElement( "Contrato" );
				rgTipoLanca = new JRadioGroup<String, String>( 1, 4, vLabsTipo, vValsTipo );
			}
			else {
				rgTipoLanca = new JRadioGroup<String, String>( 1, 3, vLabsTipo, vValsTipo );
			}

			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar preferências do sistema \n" + err.getMessage(), true, con, err );
		}
		return prefere;
	}

	public void edit( EditEvent eevt ) {

	}

	public void afterCarrega( CarregaEvent cevt ) {

		if ( cevt.getListaCampos() == lcCli ) {
			HashMap<String, Vector<Object>> vals = FuncoesCRM.montaComboContr( con, txtCodCli.getVlrInteger(), "<Não selecionado>", true );
			cbContr.setItensGeneric( (Vector<?>) vals.get( "LAB" ), (Vector<?>) vals.get( "VAL" ) );
		} else if ( cevt.getListaCampos() == lcDet ) {
			if ( "O".equals( rgTipoLanca.getVlrString() ) ) {
				cbContr.setVlrInteger( txtCodContr.getVlrInteger() );
				cbitContr.setVlrInteger( txtCodItContr.getVlrInteger() );
			}
			if (consist(false)) {
				txtDataLanca.setEnabled( true );
				txtVlrLanca.setEnabled( true );
				txtCodFor.setEnabled( true );
				txtCodCli.setEnabled( true );
			} else {
				txtDataLanca.setEnabled( false );
				txtVlrLanca.setEnabled( false );
				txtCodFor.setEnabled( false );
				txtCodCli.setEnabled( false );
			}
		}
	}

	public void beforeCarrega( CarregaEvent cevt ) {

	}

	public void valorAlterado( JComboBoxEvent evt ) {

		if ( evt.getComboBoxPad() == cbContr ) {
			HashMap<String, Vector<Object>> vals = FuncoesCRM.montaComboItContr( con, cbContr.getVlrInteger(), "<Não selecionado>" );
			cbitContr.setItensGeneric( (Vector<?>) vals.get( "LAB" ), (Vector<?>) vals.get( "VAL" ) );
		}
		else if ( evt.getComboBoxPad() == cbitContr ) {
			if ( cbContr.getVlrInteger() > 0 )
				txtCodContr.setVlrInteger( cbContr.getVlrInteger() );
			if ( cbitContr.getVlrInteger() > 0 )
				txtCodItContr.setVlrInteger( cbitContr.getVlrInteger() );
		}

	}



}
