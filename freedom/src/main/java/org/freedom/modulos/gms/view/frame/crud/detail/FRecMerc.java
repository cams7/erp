package org.freedom.modulos.gms.view.frame.crud.detail;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.JComboBoxEvent;
import org.freedom.acao.JComboBoxListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.functions.ConversionFunctions;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.infra.util.AbstractCalcRenda;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JComboBoxPad;
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
import org.freedom.library.swing.frame.FPassword;
import org.freedom.library.type.StringDireita;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.cfg.view.frame.crud.plain.FBairro;
import org.freedom.modulos.cfg.view.frame.crud.plain.FMunicipio;
import org.freedom.modulos.gms.business.object.Expedicao;
import org.freedom.modulos.gms.business.object.TipoRecMerc;
import org.freedom.modulos.gms.dao.DAORecMerc;
import org.freedom.modulos.gms.view.dialog.utility.DLPesagem;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.gms.view.frame.utility.FControleRecMerc;
import org.freedom.modulos.std.view.frame.crud.plain.FAlmox;
import org.freedom.modulos.std.view.frame.crud.tabbed.FFornecedor;
import org.freedom.modulos.std.view.frame.crud.tabbed.FTransp;

public class FRecMerc extends FDetalhe implements FocusListener, JComboBoxListener, CarregaListener, PostListener, InsertListener, KeyListener {

	// *** Constantes

	private static final long serialVersionUID = 1L;

	// *** Variaveis

	private HashMap<String, Object> prefere = null;

	private boolean novo = true;

	private Vector<String> vValsFrete = new Vector<String>();

	private Vector<String> vLabsFrete = new Vector<String>();

	private Vector<Object> vValsBairro = new Vector<Object>();

	private Vector<String> vLabsBairro = new Vector<String>();

	// *** Campos (Cabeçalho)

	private JTextFieldPad txtTicket = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtPlacaTran = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodTipoRecMercDet = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodTipoRecMerc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtRefProdCab = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtRefProdDet = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldFK txtDescTipoRecMerc = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodProdCab = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescProdCab = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodProdPadrao = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodTran = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeTran = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtCNPJTran = new JTextFieldFK( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtCNPJFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldFK txtRazFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCodBairro = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeBairro = new JTextFieldFK( JTextFieldPad.TP_STRING, 14, 0 );
	
	private JTextFieldFK txtVlrFrete = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 12, Aplicativo.casasDecFin );
	
	private JTextFieldPad txtDesconto = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 2, Aplicativo.casasDecFin );

	private JTextFieldFK txtCodPais = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtSiglaUF = new JTextFieldFK( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtCodMunic = new JTextFieldPad( JTextFieldPad.TP_STRING, 7, 0 );

	private JTextFieldFK txtDescMun = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodProcRecMerc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldFK txtDescProcRecMerc = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtTipoProcRecMerc = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtStatus = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtStatusItRecMerc = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JComboBoxPad cbBairro = new JComboBoxPad( vLabsBairro, vValsBairro, JComboBoxPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtPeso1 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtPeso2 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtMediaAmostragem = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtRendaAmostragem = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtDataPesagem = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );
	
	private JTextFieldPad txtDtEnt = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtHoraPesagem = new JTextFieldPad( JTextFieldPad.TP_TIME, 8, 0 );

	private JRadioGroup<String, String> rgFrete = null;
	
	private JTextFieldPad txtCodAlmox = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtDescAlmox = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	// *** Campos (Detalhe)

	private JTextFieldPad txtCodItRecMerc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodProdDet = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescProdDet = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	// *** Paineis

	private JPanelPad pinCab = new JPanelPad();

	private JPanelPad pinDet = new JPanelPad();

	private JPanelPad pinDetGrid = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 2 ) );
	
	private JPanelPad pinCabObs = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	// *** Lista Campos

	private ListaCampos lcTran = new ListaCampos( this, "TN" );

	private ListaCampos lcFor = new ListaCampos( this, "FR" );

	private ListaCampos lcProdCab = new ListaCampos( this, "PD" );

	private ListaCampos lcProdDet = new ListaCampos( this, "PD" );

	private ListaCampos lcTipoRecMerc = new ListaCampos( this, "TR" );

	private ListaCampos lcBairro = new ListaCampos( this );

	private ListaCampos lcMunic = new ListaCampos( this );

	private ListaCampos lcProc = new ListaCampos( this, "TP" );
	
	private ListaCampos lcAlmox = new ListaCampos( this, "AX" );

	// *** Labels

	private JLabelPad lbBairro = new JLabelPad( "Bairro" );

	private JLabelPad lbStatus = new JLabelPad();

	private JLabelPad lbMedia = new JLabelPad( "Media" );

	private JLabelPad lbRenda = new JLabelPad( "Renda" );

	// *** Separador label

	private JLabelPad sepdet = new JLabelPad();

	// *** Botões

	private JButtonPad btAdicBairro = new JButtonPad( Icone.novo( "btAdic2.gif" ) );

	private JButtonPad btPesagem = new JButtonPad( "Pesagem", Icone.novo( "btBalanca3.png" ) );

	// *** Tela do Painel de controle
	private FControleRecMerc tela_mae = null;

	// *** Tabelas

	private JTablePad tabPesagem = null;

	// *** Plugin

	private AbstractCalcRenda classplugin = null;

	private AbstractCalcRenda objplugin = null;
	
	private boolean atualizaPesoManual = false;
	
	private JTextAreaPad txaObs = new JTextAreaPad();
	
	private JScrollPane spnObs = new JScrollPane( txaObs );
	
	private JTabbedPanePad tpnCab = new JTabbedPanePad();
	
	private JPanelPad pinCabRecMerc = new JPanelPad();


	public FRecMerc() {

		montaTela();
	}

	private void montaTela() {

		setTitulo( "Recepção de mercadorias" );
		setAtribos( 50, 50, 653, 500 );

		nav.setNavigation( true );

		configuraCampos();
		carregaPlugin();
		criaTabelas();
		montaListaCampos();
		montaPaineis();
		montaTab();
		ajustaTabela();
		adicListeners();

		setImprimir( true );

	}

	public FRecMerc( boolean novo ) {

		super();

		this.novo = novo;

		montaTela();

	}

	private void configuraCampos() {

		txtPlacaTran.setUpperCase( true );
		txtPlacaTran.setMascara( JTextFieldPad.MC_PLACA );

//		txtCodMunic.setAtivo( false );

		vValsFrete.addElement( "C" );
		vValsFrete.addElement( "F" );
		vLabsFrete.addElement( "CIF" );
		vLabsFrete.addElement( "FOB" );

		rgFrete = new JRadioGroup<String, String>( 1, 2, vLabsFrete, vValsFrete, -4 );

		lbStatus.setForeground( Color.WHITE );
		lbStatus.setBackground( Color.GRAY );
		lbStatus.setFont( new Font( "Arial", Font.BOLD, 13 ) );
		lbStatus.setOpaque( true );
		lbStatus.setHorizontalAlignment( SwingConstants.CENTER );
		lbStatus.setText( "NÃO SALVO" );
		lbStatus.setVisible( true );

		txtMediaAmostragem.setAtivo( false );
		txtRendaAmostragem.setAtivo( false );

	}

	private void criaTabelas() {

		// Tabela de pesagens

		tabPesagem = new JTablePad();
		tabPesagem.setRowHeight( 21 );

		tabPesagem.adicColuna( "Seq." );
		tabPesagem.adicColuna( "Data" );
		tabPesagem.adicColuna( "Hora" );
		tabPesagem.adicColuna( "Peso 1" );
		tabPesagem.adicColuna( "Peso 2" );

		tabPesagem.setColunaInvisivel( 0 );
		tabPesagem.setTamColuna( 60, 1 );
		tabPesagem.setTamColuna( 50, 2 );
		tabPesagem.setTamColuna( 100, 3 );
		tabPesagem.setTamColuna( 100, 4 );

	}

	private void montaPaineis() {

		pnMaster.remove( spTab );
		pnMaster.add( pinDetGrid, BorderLayout.CENTER );

		pinDetGrid.add( spTab );
		pinDetGrid.add( new JScrollPane( tabPesagem ) );

		pnCliCab.add( tpnCab );
		tpnCab.addTab( "Geral", pinCabRecMerc );

		
		pinCabObs.add( spnObs );
		tpnCab.addTab( "Observações", pinCabObs );
		
		
		
		montaCabecalho();
		montaDetalhe();
		mostraRenda( false );

	}

	private void montaCabecalho() {

		setAltCab( 230 );

		setListaCampos( lcCampos );
		setPainel( pinCabRecMerc );

		adicCampo( txtTicket, 7, 20, 70, 20, "Ticket", "Ticket", ListaCampos.DB_PK, true );
		adicCampo( txtPlacaTran, 80, 20, 70, 20, "PlacaVeiculo", "Placa", ListaCampos.DB_SI, true );

		adicCampo( txtCodTipoRecMerc, 153, 20, 40, 20, "CodTipoRecMerc", "Cód.T.", ListaCampos.DB_FK, txtDescTipoRecMerc, true );
		adicDescFK( txtDescTipoRecMerc, 196, 20, 228, 20, "DescTipoRecMerc", "Tipo de recebimento" );
		
		adicCampo( txtDtEnt, 427, 20, 70, 20, "DtEnt", "Data", ListaCampos.DB_SI, true );

		adicCampo( txtCodProdCab, 7, 60, 70, 20, "CodProd", "Cod.Pd.", ListaCampos.DB_FK, txtDescProdCab, true );
		adicDescFK( txtDescProdCab, 80, 60, 200, 20, "DescProd", "Descrição do Produto" );
		adicCampoInvisivel( txtRefProdCab, "RefProd", "Referência", ListaCampos.DB_SI, false );

		adicCampo( txtCodAlmox, 283, 60, 50, 20, "CodAlmox", "Almox.", ListaCampos.DB_FK, true );
		adicDescFK( txtDescAlmox, 336, 60, 160, 20, "DescAlmox", "Descrição do almoxarifado" );

		adicCampo( txtCodTran, 7, 100, 70, 20, "CodTran", "Cod.Tran.", ListaCampos.DB_FK, txtNomeTran, true );
		adicDescFK( txtNomeTran, 80, 100, 345, 20, "NomeTran", "Nome da transportadora" );

		adicCampo( txtDesconto, 428, 100, 70, 20, "Desconto", "%Desc.Peso", ListaCampos.DB_SI, false );
		
		adicDB( rgFrete, 500, 100, 123, 20, "TipoFrete", "Tipo de frete", false );

		adicCampo( txtCodFor, 7, 140, 70, 20, "CodFor", "Cod.For.", ListaCampos.DB_FK, txtRazFor, true );
		adicDescFK( txtRazFor, 80, 140, 200, 20, "RazFor", "Razão social do fornecedor" );

		adicCampoInvisivel( txtCodPais, "CodPais", "País", ListaCampos.DB_SI, false );
		adicCampo( txtSiglaUF, 283, 140, 30, 20, "SiglaUF", "UF", ListaCampos.DB_FK, false );
		adicCampo( txtCodMunic, 316, 140, 58 , 20, "CodMunic", "Cód.Mun.", ListaCampos.DB_FK, false );
		adicDescFK( txtDescMun, 377, 140, 120, 20, "DescMunic", "Município" );

//		adicCampo( txtCodBairro, 7,180,100,20, "CodBairro", "Cód.Bairro", ListaCampos.DB_FK, false );
		adicCampoInvisivel( txtCodBairro, "CodBairro", "Cód.Bairro", ListaCampos.DB_FK, true );
		adicCampoInvisivel( txtStatus, "Status", "Status", ListaCampos.DB_SI, false );
		
		adicDBLiv( txaObs, "ObsRecMerc", "Observações" , false );

		adic( lbBairro, 500, 120, 100, 20 );
		adic( cbBairro, 500, 140, 100, 20 );

		adic( btAdicBairro, 603, 140, 20, 20 );

		adic( lbStatus, 500, 20, 123, 60 );

		setListaCampos( true, "RECMERC", "EQ" );
		lcCampos.setQueryInsert( true );

	}

	private void montaDetalhe() {

		setAltDet( 70 );

		setPainel( pinDet, pnDet );
		setListaCampos( lcDet );
		setNavegador( navRod );

		lcDet.setPodeIns( false );
		lcDet.setPodeExc( false );

		navRod.btNovo.setEnabled( false );
		navRod.btExcluir.setEnabled( false );

		adicCampo( txtCodItRecMerc, 7, 20, 40, 20, "CodItRecMerc", "Seq.", ListaCampos.DB_PK, true );
		adicCampo( txtCodProdDet, 50, 20, 50, 20, "CodProd", "Cód.Pd.", ListaCampos.DB_FK, txtDescProdDet, true );
		adicDescFK( txtDescProdDet, 103, 20, 203, 20, "DescProd", "Descrição do Produto" );
		adicCampoInvisivel( txtRefProdDet, "RefProd", "Referência", ListaCampos.DB_SI, false );

		adicCampoInvisivel( txtCodProcRecMerc, "CodProcRecMerc", "Cod.Proc.", ListaCampos.DB_FK, txtDescProcRecMerc, true );
		adicDescFKInvisivel( txtDescProcRecMerc, "DescProcRecMerc", "Descrição do processo" );
		adicCampoInvisivel( txtCodTipoRecMercDet, "CodTipoRecMerc", "Cod.Tp.Rec.Merc", ListaCampos.DB_SI, true );

		lbMedia = adicCampo( txtMediaAmostragem, 320, 20, 70, 20, "mediaamostragem", "Media", ListaCampos.DB_SI, false );
		lbRenda = adicCampo( txtRendaAmostragem, 393, 20, 70, 20, "rendaamostragem", "Renda", ListaCampos.DB_SI, false );

		txtStatusItRecMerc.setSoLeitura( true );
		adicCampoInvisivel( txtStatusItRecMerc, "StatusItRecMerc", "Status", ListaCampos.DB_SI, false );

		setListaCampos( true, "ITRECMERC", "EQ" );
		lcDet.setQueryInsert( false );

		sepdet.setBorder( BorderFactory.createEtchedBorder() );
		adic( sepdet, 315, 4, 2, 52 );

		adic( btPesagem, 475, 5, 150, 50 );
		btPesagem.setToolTipText( "Realiza pesagem - (F12)" );
		pinDetGrid.setBackground( Color.RED );

	}

	private void carregaTipoRec() {

		txtCodTipoRecMerc.setVlrInteger( (Integer) prefere.get( "codtiporecmerc" ) );
		lcTipoRecMerc.carregaDados();

	}

	private void buscaPrefere() {

		StringBuilder sql = new StringBuilder();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			prefere = new HashMap<String, Object>();

			sql.append( "select coalesce(pf8.codtiporecmerc,0) codtiporecmerc, coalesce(sincticket,'N') sincticket " );
			sql.append( "from sgprefere8 pf8 " );
			sql.append( "where pf8.codemp=? and pf8.codfilial=? " );

			ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE8" ) );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				prefere.put( "codtiporecmerc", rs.getInt( "codtiporecmerc" ) );
				prefere.put( "sincticket", rs.getString( "sincticket" ).equals( "S" ) );
			}

			con.commit();

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private void buscaTransp( String placa ) {

		StringBuilder sql = new StringBuilder();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			sql.append( "select tp.codtran from vdtransp tp " );
			sql.append( "where tp.codemp=? and tp.codfilial=? and tp.placatran=?" );

			ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDTRANSP" ) );
			ps.setString( 3, placa );

			rs = ps.executeQuery();

			if ( rs.next() ) {

				txtCodTran.setVlrInteger( rs.getInt( "codtran" ) );
				lcTran.carregaDados();

			}

			con.commit();

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	private void ajustaTabela() {

		tab.setRowHeight( 21 );

		tab.setTamColuna( 30, 0 );
		tab.setColunaInvisivel( 1 );
		tab.setColunaInvisivel( 2 );
		tab.setTamColuna( 140, 4 );
		tab.setColunaInvisivel( 3 );
		tab.setColunaInvisivel( 5 );
		tab.setTamColuna( 70, 6 );
		tab.setTamColuna( 70, 7 );
		tab.setColunaInvisivel( 8 );

	}

	public void exec( int ticket, int tiporecmerc, FControleRecMerc tela_mae ) {

		try {
			lcCampos.edit();
			txtTicket.setVlrInteger( ticket );
			txtCodTipoRecMerc.setVlrInteger( tiporecmerc );
			lcCampos.carregaDados();

			setTelaMae( tela_mae );

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	private void adicListeners() {

		txtPlacaTran.addFocusListener( this );

		cbBairro.addComboBoxListener( this );

		lcCampos.addInsertListener( this );
		lcCampos.addCarregaListener( this );
		lcDet.addPostListener( this );
		lcBairro.addCarregaListener( this );
		lcMunic.addCarregaListener( this );
		lcFor.addCarregaListener( this );
		lcDet.addCarregaListener( this );
		lcTipoRecMerc.addCarregaListener( this );

		btAdicBairro.addActionListener( this );
		btPesagem.addActionListener( this );
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );

	}

	private void montaListaCampos() {

		// * Tipo de Recebimento Cabeçalho

		lcTipoRecMerc.add( new GuardaCampo( txtCodTipoRecMerc, "CodTipoRecMerc", "Cód.Tipo.Rec.", ListaCampos.DB_PK, false ) );
		lcTipoRecMerc.add( new GuardaCampo( txtDescTipoRecMerc, "DescTipoRecMerc", "Descrição do tipo de recepção de mercadoria", ListaCampos.DB_SI, false ) );
		lcTipoRecMerc.add( new GuardaCampo( txtCodProdPadrao, "Codprod", "Cód.Prod.", ListaCampos.DB_SI, false ) );

		txtCodTipoRecMerc.setTabelaExterna( lcTipoRecMerc, FTipoRecMerc.class.getCanonicalName() );
		txtCodTipoRecMerc.setNomeCampo( "CodTipoRecMerc" );
		txtCodTipoRecMerc.setFK( true );

		lcTipoRecMerc.setReadOnly( true );
		lcTipoRecMerc.montaSql( false, "TIPORECMERC", "EQ" );

		// * Produto (Cabeçalho)

		lcProdCab.add( new GuardaCampo( txtCodProdCab, "CodProd", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcProdCab.add( new GuardaCampo( txtDescProdCab, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProdCab.add( new GuardaCampo( txtRefProdCab, "RefProd", "Referência", ListaCampos.DB_SI, false ) );

		txtCodProdCab.setTabelaExterna( lcProdCab, FProduto.class.getCanonicalName() );
		txtCodProdCab.setNomeCampo( "CodProd" );
		txtCodProdCab.setFK( true );

		lcProdCab.setReadOnly( true );
		lcProdCab.montaSql( false, "PRODUTO", "EQ" );
		
		// * Almoxarifado (Cabeçalho)
		
		lcAlmox.add( new GuardaCampo( txtCodAlmox, "CodAlmox", "Cód.almox.", ListaCampos.DB_PK, true ) );
		lcAlmox.add( new GuardaCampo( txtDescAlmox, "DescAlmox", "Descrição do almoxarifado", ListaCampos.DB_SI, false ) );
		lcAlmox.montaSql( false, "ALMOX", "EQ" );
		lcAlmox.setReadOnly( true );
		lcAlmox.setQueryCommit( false );
		txtCodAlmox.setTabelaExterna( lcAlmox, FAlmox.class.getCanonicalName() );


		// * Fornecedor

		lcFor.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.For.", ListaCampos.DB_PK, false ) );
		lcFor.add( new GuardaCampo( txtRazFor, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );
		lcFor.add( new GuardaCampo( txtCNPJFor, "CnpjFor", "CNPJ", ListaCampos.DB_SI, false ) );
		lcFor.add( new GuardaCampo( txtCodPais, "CodPais", "Cód.País", ListaCampos.DB_SI, false ) );
		lcFor.add( new GuardaCampo( txtSiglaUF, "SiglaUF", "UF", ListaCampos.DB_SI, false ) );
		lcFor.add( new GuardaCampo( txtCodMunic, "CodMunic", "Cód.Mun.", ListaCampos.DB_SI, false ) );

		txtCodFor.setTabelaExterna( lcFor, FFornecedor.class.getCanonicalName() );
		txtCodFor.setNomeCampo( "CodFor" );
		txtCodFor.setFK( true );

		lcFor.setReadOnly( true );
		lcFor.montaSql( false, "FORNECED", "CP" );

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

		lcProdDet.add( new GuardaCampo( txtCodProdDet, "CodProd", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcProdDet.add( new GuardaCampo( txtDescProdDet, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProdDet.add( new GuardaCampo( txtRefProdDet, "RefProd", "Referência", ListaCampos.DB_SI, false ) );

		txtCodProdDet.setTabelaExterna( lcProdDet, FProduto.class.getCanonicalName() );
		txtCodProdDet.setNomeCampo( "CodProd" );
		txtCodProdDet.setFK( true );

		lcProdDet.setReadOnly( true );
		lcProdDet.montaSql( false, "PRODUTO", "EQ" );

		/***************
		 * MUNICIPIO *
		 **************/

		lcMunic.setUsaME( false );
		lcMunic.add( new GuardaCampo( txtCodMunic, "CodMunic", "Cód.Muni", ListaCampos.DB_PK, false ) );
		lcMunic.add( new GuardaCampo( txtDescMun, "NomeMunic", "Nome Muni.", ListaCampos.DB_SI, false ) );

		lcMunic.setDinWhereAdic( "SIGLAUF = #S", txtSiglaUF );
		lcMunic.montaSql( false, "MUNICIPIO", "SG" );
		lcMunic.setQueryCommit( false );
		lcMunic.setReadOnly( true );
		txtCodMunic.setTabelaExterna( lcMunic, FMunicipio.class.getCanonicalName() );

		/***************
		 * BAIRRO *
		 ***************/

		lcBairro.setUsaME( false );
		lcBairro.add( new GuardaCampo( txtCodBairro, "CodBairro", "Cód.Bairro", ListaCampos.DB_PK, true ) );
		lcBairro.add( new GuardaCampo( txtCodMunic, "CodMunic", "Cód.Munic", ListaCampos.DB_PK, true ) );
		lcBairro.add( new GuardaCampo( txtSiglaUF, "SiglaUF", "Sigla.UF", ListaCampos.DB_PK, false ) );
		lcBairro.add( new GuardaCampo( txtCodPais, "CodPais", "Cód.País", ListaCampos.DB_PK, false ) );
		lcBairro.add( new GuardaCampo( txtNomeBairro, "NomeBairro", "Nome do Bairro", ListaCampos.DB_SI, false ) );
		lcBairro.add( new GuardaCampo( txtVlrFrete, "VlrFrete", "Valor Frete", ListaCampos.DB_SI, false ) );

		lcBairro.setDinWhereAdic( "CODPAIS = #N", txtCodPais );
		lcBairro.setDinWhereAdic( "SIGLAUF = #S", txtSiglaUF );
		lcBairro.setDinWhereAdic( "CODMUNIC = #S", txtCodMunic );

		lcBairro.montaSql( false, "BAIRRO", "SG" );
		lcBairro.setQueryCommit( false );
		lcBairro.setReadOnly( true );
		txtCodBairro.setTabelaExterna( lcBairro, FBairro.class.getCanonicalName() );

		/***************
		 * PROCESSOS *
		 ***************/

		lcProc.add( new GuardaCampo( txtCodProcRecMerc, "CodProcRecMerc", "Cód.Proc.", ListaCampos.DB_PK, false ) );
		lcProc.add( new GuardaCampo( txtDescProcRecMerc, "DescProcRecMerc", "Descrição do processo", ListaCampos.DB_SI, false ) );
		lcProc.add( new GuardaCampo( txtCodTipoRecMercDet, "CodTipoRecMerc", "Cod.Tp.Rec.Merc.", ListaCampos.DB_PF, false ) );
		lcProc.add( new GuardaCampo( txtTipoProcRecMerc, "TipoProcRecMerc", "Tp.Proc.Rec.Merc.", ListaCampos.DB_SI, false ) );

		txtCodProcRecMerc.setTabelaExterna( lcProc, null );
		txtCodProcRecMerc.setNomeCampo( "CodProcRecMerc" );
		txtCodProcRecMerc.setFK( true );

		lcProc.setReadOnly( true );
		lcProc.montaSql( false, "PROCRECMERC", "EQ" );

	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
		else if ( evt.getSource() == btImp ) {
			imprimir( TYPE_PRINT.PRINT);
		}
		else if ( evt.getSource() == btAdicBairro ) {

			FBairro bairro = new FBairro( true, con );

			try {

				bairro.setConexao( con );

				bairro.setCodPais( txtCodPais.getVlrInteger() );
				bairro.setSiglaUF( txtSiglaUF.getVlrString() );
				bairro.setCodMunic( txtCodMunic.getVlrString() );

				Aplicativo.telaPrincipal.criatela( "Recepção de mercadorias", bairro, con );

			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}
		else if ( evt.getSource() == btPesagem ) {
			if ( validaProcesso() ) {
				capturaAmostra();
			}
		}

		super.actionPerformed( evt );
	}

	private boolean validaProcesso() {

		boolean ret = false;
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			if ( "PE".equals( txtStatusItRecMerc.getVlrString() ) ) {

				sql.append( "select first 1 statusitrecmerc from eqitrecmerc " );
				sql.append( "where codemp=? and codfilial=? and ticket=? and coditrecmerc < ? " );
				sql.append( "order by coditrecmerc desc" );

				ps = con.prepareStatement( sql.toString() );

				ps.setInt( 1, lcCampos.getCodEmp() );
				ps.setInt( 2, lcCampos.getCodFilial() );
				ps.setInt( 3, txtTicket.getVlrInteger() );
				ps.setInt( 4, txtCodItRecMerc.getVlrInteger() );

				rs = ps.executeQuery();

				if ( rs.next() ) {

					String statusant = rs.getString( "statusitrecmerc" );

					if ( "PE".equals( statusant ) ) {
						Funcoes.mensagemInforma( this, "Processo anterior ainda está pendente!" );
						ret = false;
					}
					else if ( "FN".equals( statusant ) ) {
						ret = true;
					}

				}
				else {
					ret = true;
				}
			}
			else {
				
				if(atualizaPesoManual) {
					AtualizaAmostra();
				}
				else {
					Funcoes.mensagemInforma( this, "Processo ja foi finalizado" );
				}
				ret = false;
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return ret;
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		ImprimeOS imp = new ImprimeOS( "", con );

		String DataP1 = null;
		String HoraP1 = null;
		String UnidP1 = null;
		String interno = null;
		BigDecimal PesoP1 = null;

		String DataP2 = null;
		String HoraP2 = null;
		String UnidP2 = null;

		BigDecimal PesoP2 = null;
		BigDecimal PesoLiq = null;
		BigDecimal PesoTotal = null;
		BigDecimal media = null;
		BigDecimal desconto = null;

		String renda = null;

		DAORecMerc recmerc = new DAORecMerc( this, txtTicket.getVlrInteger(), con );

		try {

			try {

				HashMap<String, Object> p1 = recmerc.getPrimeirapesagem();

				PesoP1 = (BigDecimal) p1.get( "peso" );
				DataP1 = (String) p1.get( "data" );
				HoraP1 = (String) p1.get( "hora" );
				UnidP1 = (String) p1.get( "unid" );
				interno = (String) p1.get( "interno" );

				HashMap<String, Object> p2 = recmerc.getSegundapesagem();

				PesoP2 = (BigDecimal) p2.get( "peso" );
				DataP2 = (String) p2.get( "data" );
				HoraP2 = (String) p2.get( "hora" );
				UnidP2 = (String) p2.get( "unid" );

				PesoLiq = PesoP1.subtract( PesoP2 );
				
				HashMap<String, Object> p3 = recmerc.getRendapesagem();

				media = (BigDecimal) p3.get( "media" );
				renda = (String) p3.get( "renda" );
				
				desconto = recmerc.getDesconto();
				
				if(desconto==null) {
					desconto = new BigDecimal(0);
				}
				
				if(desconto.floatValue()>0) {
					
					BigDecimal pesodesc = PesoLiq.multiply( desconto.divide( new BigDecimal(100) ) );
					PesoTotal = PesoLiq.subtract( pesodesc );
					
					System.out.println("Aplicado desconto no peso de :" + pesodesc.toString());
					
					
				}
				else {
					PesoTotal = PesoLiq;
				}
				
				

			} catch ( Exception e ) {
				System.out.println( "Erro ao buscar pesagens!" );
			}

			imp.pulaLinha( 0, imp.comprimido() + " " );

			if(cbBairro.getSelectedItem()==null) {
			
				imp.say( imp.pRow(), 77, txtDescMun.getVlrString() );
			
			}
			else {
				imp.say( imp.pRow(), 77, cbBairro.getSelectedItem().toString() );
			}

			imp.pulaLinha( 1, imp.comprimido() );
			
			imp.say( imp.pRow(), 77, txtDescAlmox.getVlrString() );
			
			imp.pulaLinha( 2, imp.comprimido() );

			imp.say( imp.pRow(), 3, "PLACA:............:" );
			imp.say( imp.pRow(), 24, Funcoes.setMascara( txtPlacaTran.getVlrString(), JTextFieldPad.mascplaca ) );

			imp.say( imp.pRow(), 35, "FRETE:" );
			imp.say( imp.pRow(), 41, "F".equals( rgFrete.getVlrString()) ? "FOB" : "CIF" );
			
			imp.say( imp.pRow(), 70, "INTERNO.:" );

			imp.say( imp.pRow(), 0, " " + imp.normal() );

			imp.say( imp.pRow(), 48, StringFunctions.strZero( txtTicket.getVlrString(), 10 ) );

			imp.pulaLinha( 1, imp.comprimido() );

			imp.say( imp.pRow(), 3, "PRODUTO:..........:" );

			imp.say( imp.pRow(), 0, " " + imp.normal() );
			imp.say( imp.pRow(), 15, txtDescProdCab.getVlrString().trim() );

			imp.pulaLinha( 1, imp.comprimido() );

			imp.say( imp.pRow(), 3, "PRODUTOR:.........:" );

			imp.say( imp.pRow(), 24, txtCodFor.getVlrString().trim() + " " + txtRazFor.getVlrString().trim() );

			imp.pulaLinha( 2, imp.comprimido() );

			imp.say( imp.pRow(), 3, "PRIMEIRA PESAGEM..:" );

			imp.say( imp.pRow(), 0, " " + imp.normal() );

			imp.say( imp.pRow(), 15, Funcoes.strDecimalToStrCurrency( 7, 0, String.valueOf( PesoP1 ) ) );

			imp.say( imp.pRow(), 23, UnidP1 );
			imp.say( imp.pRow(), 30, DataP1 );
			imp.say( imp.pRow(), 46, HoraP1 );

			imp.pulaLinha( 1, imp.comprimido() );

			imp.say( imp.pRow(), 3, "SEGUNDA PESAGEM...:" );

			imp.say( imp.pRow(), 0, " " + imp.normal() );

			imp.say( imp.pRow(), 15, Funcoes.strDecimalToStrCurrency( 7, 0, String.valueOf( PesoP2 ) ) );
			imp.say( imp.pRow(), 23, UnidP2 );
			imp.say( imp.pRow(), 30, DataP2 );
			imp.say( imp.pRow(), 46, HoraP2 );

			imp.pulaLinha( 1, imp.comprimido() );

			imp.say( imp.pRow(), 3, "LIQUIDO...........:" );
			
			imp.say( imp.pRow(), 0, " " + imp.normal() );

			imp.say( imp.pRow(), 15, Funcoes.strDecimalToStrCurrency( 7, 0, String.valueOf( PesoLiq ) ) );
			imp.say( imp.pRow(), 23, UnidP1 );

			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( imp.pRow(), 3, "DESCONTO..........:" );
			imp.say( imp.pRow(), 15, Funcoes.strDecimalToStrCurrency( 2, 2, String.valueOf( desconto ) ) + " %");
			
			imp.say( imp.pRow(), 30, "OBS.:" + Funcoes.copy( txaObs.getVlrString(), 0, 30 ));
			
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( imp.pRow(), 3, "TOTAL.............:" );
			
			imp.say( imp.pRow(), 15, Funcoes.strDecimalToStrCurrency( 7, 0, String.valueOf( PesoTotal ) ) );
			imp.say( imp.pRow(), 23, UnidP1 );
			
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( imp.pRow(), 3, "Renda.............:" );
			imp.say( imp.pRow(), 24, String.valueOf( media.intValue() ) );

			imp.pulaLinha( 1, imp.comprimido() );

			imp.say( imp.pRow(), 3, "Renda Classif.....:" );

			imp.say( imp.pRow(), 0, " " + imp.normal() );

			imp.say( imp.pRow(), 15, renda );

			imp.pulaLinha( 3 );
			imp.fechaGravacao();

		} catch ( Exception err ) {
			Funcoes.mensagemErro( this, "Erro na impressão de ticket!\n" + err.getMessage(), true, con, err );
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcProdCab.setConexao( cn );
		lcProdDet.setConexao( cn );
		lcAlmox.setConexao( cn );		
		lcTran.setConexao( cn );
		lcFor.setConexao( cn );
		lcTipoRecMerc.setConexao( cn );
		lcMunic.setConexao( cn );
		lcBairro.setConexao( cn );
		lcProc.setConexao( cn );

		buscaPrefere();

		if ( novo ) {
			lcCampos.insert( true );
		}

	}

	private void capturaAmostra() {

		DLPesagem dl = null;

		try {

			dl = new DLPesagem( this, txtTipoProcRecMerc.getVlrString(),false );

			dl.setConexao( con );
			dl.execShow();

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		if ( dl.OK ) {

			txtPeso1.setVlrBigDecimal( dl.getPeso1() );
			txtPeso2.setVlrBigDecimal( dl.getPeso2() );

			txtDataPesagem.setVlrDate( dl.getData() );
			txtHoraPesagem.setVlrString( dl.getHora() );

			if ( txtPeso1.getVlrBigDecimal().floatValue() > 0 && txtDataPesagem.getVlrDate() != null && txtHoraPesagem.getVlrString() != null ) {

				salvaAmostra();

				if ( TipoRecMerc.PROCESSO_DESCARREGAMENTO.getValue().equals( txtTipoProcRecMerc.getVlrString() ) ) {

					calcRenda();

				}
				else {

					mostraRenda( false );

				}

			}

			limpaAmostra();
			montaTabPesagem();

		}

		dl.dispose();
	}

	private void calcRenda() {

		try {

			if ( objplugin != null ) {

				objplugin.inicializa( txtTicket.getVlrInteger(), txtCodItRecMerc.getVlrInteger(), con );

				if ( objplugin.isInicialized() ) {

					txtMediaAmostragem.setVlrBigDecimal( objplugin.getMedia() );
					txtRendaAmostragem.setVlrInteger( objplugin.getRenda() );

					mostraRenda( true );

					lcDet.edit();
					lcDet.post();

				}
			}
			else {
				System.out.println( " Plugin de calculo de renda nulo!" );
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	private void limpaAmostra() {

		txtPeso1.setVlrBigDecimal( new BigDecimal( 0 ) );
		txtDataPesagem.setVlrDate( null );
		txtHoraPesagem.setVlrString( null );
	}

	private void salvaAmostra() {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		Integer codamostragem = 0;
		ResultSet rs = null;

		try {

			sql.append( "select coalesce( max(codamostragem), 0 ) " );
			sql.append( "from eqrecamostragem " );
			sql.append( "where " );
			sql.append( "codemp=? and codfilial=? and ticket=? and coditrecmerc=? " );

			ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, lcDet.getCodEmp() );
			ps.setInt( 2, lcDet.getCodFilial() );
			ps.setInt( 3, txtTicket.getVlrInteger() );
			ps.setInt( 4, txtCodItRecMerc.getVlrInteger() );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				codamostragem = rs.getInt( 1 );
			}

			codamostragem++;

			sql = new StringBuilder();

			sql.append( "insert into eqrecamostragem " );
			sql.append( "(codemp,codfilial,ticket,coditrecmerc,codamostragem,pesoamost,pesoamost2,dataamost,horaamost)" );
			sql.append( "values(?, ?, ?, ?, ?, ?, ?, ?, ?)" );

			ps = con.prepareStatement( sql.toString() );

			Integer coditrecmerc = txtCodItRecMerc.getVlrInteger();

			ps.setInt( 1, lcDet.getCodEmp() );
			ps.setInt( 2, lcDet.getCodFilial() );
			ps.setInt( 3, txtTicket.getVlrInteger() );
			ps.setInt( 4, coditrecmerc );
			ps.setInt( 5, codamostragem );
			ps.setBigDecimal( 6, txtPeso1.getVlrBigDecimal() );
			ps.setBigDecimal( 7, txtPeso2.getVlrBigDecimal() );
			ps.setDate( 8, Funcoes.dateToSQLDate( txtDataPesagem.getVlrDate() ) );
			ps.setTime( 9, Funcoes.strTimeTosqlTime( txtHoraPesagem.getVlrString() ) );

			ps.execute();
			con.commit();

			lcDet.edit();
			lcDet.post();

			txtCodItRecMerc.setVlrInteger( coditrecmerc );
			lcDet.carregaDados();

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	
	private void AtualizaAmostra() {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		Integer codamostragem = 0;
		ResultSet rs = null;

		try {

			sql = new StringBuilder();

			sql.append( "update eqrecamostragem set pesoamost=?, pesoamost2=? " );
			sql.append( "where codemp=? and codfilial=? and ticket=? and coditrecmerc=? and codamostragem=? ");

			
			int i = 0;
			
			while( tabPesagem.getNumLinhas() > i ) {
			
				codamostragem = (Integer) tabPesagem.getValor( i , 0 );
			
				txtPeso1.setVlrBigDecimal( ConversionFunctions.stringCurrencyToBigDecimal( ( (StringDireita) tabPesagem.getValor( i, 3 ) ).toString() ) );
				txtPeso2.setVlrBigDecimal( ConversionFunctions.stringCurrencyToBigDecimal( ( (StringDireita) tabPesagem.getValor( i, 4 ) ).toString() ) );
				
				ps = con.prepareStatement( sql.toString() );

				ps.setBigDecimal( 1, txtPeso1.getVlrBigDecimal() );
				ps.setBigDecimal( 2, txtPeso2.getVlrBigDecimal() );

				ps.setInt( 3, lcDet.getCodEmp() );
				ps.setInt( 4, lcDet.getCodFilial() );
				ps.setInt( 5, txtTicket.getVlrInteger() );
				ps.setInt( 6, txtCodItRecMerc.getVlrInteger() );
				ps.setInt( 7, codamostragem );

				ps.execute();

				
				i++;
			}

			con.commit();
			
//			lcDet.edit();
//			lcDet.post();
		
			montaTabPesagem();
			

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	public void focusGained( FocusEvent e ) {

		// TODO Auto-generated method stub

	}

	public void focusLost( FocusEvent e ) {

		if ( e.getSource() == txtPlacaTran ) {

			if ( txtCodTran.getVlrInteger() == 0 ) {
				buscaTransp( txtPlacaTran.getVlrString() );
			}

		}

	}

	private void montaTabPesagem() {

		try {

			StringBuilder sql = new StringBuilder();

			sql.append( "select " );
			sql.append( "codamostragem, pesoamost,pesoamost2, dataamost, horaamost " );
			sql.append( "from eqrecamostragem " );
			sql.append( "where codemp=? and codfilial=? and ticket=? and coditrecmerc=? " );

			StringBuffer status = new StringBuffer( "" );

			System.out.println( "SQL:" + sql.toString() );

			PreparedStatement ps = con.prepareStatement( sql.toString() );

			int iparam = 1;

			ps.setInt( iparam++, lcDet.getCodEmp() );
			ps.setInt( iparam++, lcDet.getCodFilial() );
			ps.setInt( iparam++, txtTicket.getVlrInteger() );
			ps.setInt( iparam++, txtCodItRecMerc.getVlrInteger() );

			ResultSet rs = ps.executeQuery();

			tabPesagem.limpa();

			int row = 0;

			while ( rs.next() ) {

				tabPesagem.adicLinha();

				tabPesagem.setValor( rs.getInt( "codamostragem" ), row, 0 );
				tabPesagem.setValor( rs.getDate( "dataamost" ), row, 1 );
				tabPesagem.setValor( rs.getString( "horaamost" ), row, 2 );
				tabPesagem.setValor( Funcoes.bdToStr( rs.getBigDecimal( "pesoamost" ), 2 ), row, 3 );
				tabPesagem.setValor( Funcoes.bdToStr( rs.getBigDecimal( "pesoamost2" ), 2 ), row, 4 );

//				tabPesagem.setValor( rs.getBigDecimal( "pesoamost" ).setScale( 0 ), row, 3 );
//				tabPesagem.setValor( rs.getBigDecimal( "pesoamost2" ).setScale( 0 ), row, 4 );

				
				row++;

			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	private void mostraRenda( boolean mostra ) {

		try {

			lbMedia.setVisible( mostra );
			lbRenda.setVisible( mostra );

			txtMediaAmostragem.setVisible( mostra );
			txtRendaAmostragem.setVisible( mostra );

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	private void montaComboBairro() {

		try {
			StringBuilder sql = new StringBuilder();
			sql.append( "select codbairro, nomebairro " );
			sql.append( "from sgbairro " );
			sql.append( "where codpais=? and siglauf=? and codmunic=? " );
			sql.append( "order by nomebairro" );
			sql.append( "" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, txtCodPais.getVlrInteger() );
			ps.setString( 2, txtSiglaUF.getVlrString() );
			ps.setString( 3, txtCodMunic.getVlrString() );

			ResultSet rs = ps.executeQuery();

			vValsBairro.clear();
			vLabsBairro.clear();

			while ( rs.next() ) {
				vValsBairro.addElement( rs.getInt( "CodBairro" ) );
				vLabsBairro.addElement( rs.getString( "NomeBairro" ) );
			}

			cbBairro.setItens( vLabsBairro, vValsBairro );

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao carregar os bairros!\n" + e.getMessage(), true, con, e );
		}
	}

	public void valorAlterado( JComboBoxEvent evt ) {

		if ( evt.getComboBoxPad() == cbBairro ) {
				if ( txtCodBairro.getVlrInteger() != cbBairro.getVlrInteger() ) {
					txtCodBairro.setVlrInteger( cbBairro.getVlrInteger() );
				}
		}

	}

	private void atualizaStatus() {

		DAORecMerc.atualizaStatus( txtStatus.getVlrString(), lbStatus );

	}

	public void afterCarrega( CarregaEvent cevt ) {

		if ( cevt.getListaCampos() == lcFor ) {
			lcMunic.carregaDados();
		}
		else if ( cevt.getListaCampos() == lcBairro ) {
			if ( txtCodBairro.getVlrInteger() != cbBairro.getVlrInteger() ) {
				cbBairro.setVlrInteger( txtCodBairro.getVlrInteger() );
				lcCampos.edit();
			}
		}
		else if ( cevt.getListaCampos() == lcMunic ) {
			montaComboBairro();
		}
		else if ( cevt.getListaCampos() == lcCampos ) {
			atualizaStatus();
			// Se náo foi realizada nenhuma pesagem deve carregar a sequencia 1 para facilitar a utilizacao
			if ( txtStatus.getVlrString().equals( DAORecMerc.STATUS_PENDENTE.getValue() ) ) {
				carregaSequencia( 0 );
			}
			// Se ja tiver sido realizada a pesagem 1 deve carregar a sequencia 2 para facilitar a utilizacao
			if ( txtStatus.getVlrString().equals( DAORecMerc.STATUS_PESAGEM_1.getValue() ) ) {
				carregaSequencia( 1 );
			}
			// Se ja tiver sido realizada a pesagem 2 deve carregar a proxima sequencia
			else if ( txtStatus.getVlrString().equals( DAORecMerc.STATUS_DESCARREGAMENTO.getValue() ) ) {
				carregaSequencia( 2 );
			}

		}
		else if ( cevt.getListaCampos() == lcTipoRecMerc ) {
			carregaProdutoPadrao();
		}

		else if ( cevt.getListaCampos() == lcDet ) {

			limpaAmostra();

			montaTabPesagem();

			if ( TipoRecMerc.PROCESSO_DESCARREGAMENTO.getValue().equals( txtTipoProcRecMerc.getVlrString() ) ) {

				mostraRenda( true );

			}
			else {

				mostraRenda( false );
				
			}

		}

	}

	// Futuramente deve ser implementado para buscar automaticamente o
	// coditrecmerc a partir do status e do tipo de processo.
	private void carregaSequencia( Integer proximos ) {

		try {

			if(proximos == 0){ 
				lcDet.carregaItem( 0 );
			}
			
			for ( int i = 0; i < proximos; i++ ) {
				lcDet.next(Types.INTEGER);
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private void carregaProdutoPadrao() {

		try {
//			if ( txtCodProdPadrao.getVlrInteger() > 0 && ( ! ( txtCodProdCab.getVlrInteger() > 0 ) ) ) {
			if ( txtCodProdPadrao.getVlrInteger() > 0 && ( ListaCampos.LCS_INSERT == lcCampos.getStatus() )) {
				txtCodProdCab.setVlrInteger( txtCodProdPadrao.getVlrInteger() );
				lcProdCab.carregaDados();
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	public void beforeCarrega( CarregaEvent cevt ) {

		// TODO Auto-generated method stub

	}

	public void beforePost( PostEvent pevt ) {

		super.beforePost( pevt );

		if ( pevt.getListaCampos() == lcCampos ) {
			if ( "".equals( txtStatus.getVlrString() ) ) {
				txtStatus.setVlrString( "PE" );
			}

			if( cbBairro.getVlrInteger()>0) {
				if ( txtCodBairro.getVlrInteger() != cbBairro.getVlrInteger() ) {
					txtCodBairro.setVlrInteger( cbBairro.getVlrInteger() );
				}
			}
			else {
				Funcoes.mensagemInforma( this, "O bairro de origem não foi definido!" );
				pevt.cancela();
				cbBairro.requestFocus();
			}
		}
	}

	public void afterPost( PostEvent pevt ) {

		super.beforePost( pevt );

		if ( pevt.getListaCampos() == lcDet ) {
			lcCampos.carregaDados();
			liberaRenda( false );
		}
		else if (pevt.getListaCampos() == lcCampos) {
			lcDet.carregaDados();
			carregaSequencia( 0 );
		}
	
	}

	public void beforeInsert( InsertEvent ievt ) {

		// TODO Auto-generated method stub

	}

	public void afterInsert( InsertEvent ievt ) {

		if ( ievt.getListaCampos() == lcCampos ) {
			carregaTipoRec();
			
			txtDtEnt.setVlrDate( new Date() );
	
			if((Boolean)prefere.get( "sincticket" )) {
				
				txtTicket.setVlrInteger( Expedicao.getSequenciaTicketUnificado() );
			
			}

			
		}

	}
	
	public void setTelaMae( FControleRecMerc tela_mae ) {

		this.tela_mae = tela_mae;
	}

	public void dispose() {

		super.dispose();

		if ( tela_mae != null ) {
			tela_mae.montaGrid();
		}

	}

	@ SuppressWarnings ( "unchecked" )
	private void carregaPlugin() {

		String strplugin = null;
		StringBuilder messagesError = new StringBuilder( "Classe de integração para cálculo de renda \"" );

		int errorCount = 0;
		try {

			strplugin = Aplicativo.getParameter( "plugin_calc_renda" );

			if ( strplugin == null || "".equals( strplugin ) ) {
				return;
			}
			else {

				Class<AbstractCalcRenda> classplugin = ( (Class<AbstractCalcRenda>) Class.forName( strplugin ) );
				objplugin = classplugin.newInstance();

			}
		} catch ( ClassNotFoundException error ) {
			messagesError.append( strplugin );
			messagesError.append( "\" não encontrada.\nVerifique a variável CLASSPATH !\n" );
			errorCount++;
		} catch ( ClassCastException error ) {
			messagesError.append( strplugin );
			messagesError.append( "\" não é derivada do tipo esperado\n\"org.freedom.infra.util.AbstractCalcRenda\"!" );
			errorCount++;
		} catch ( InstantiationException error ) {
			messagesError.append( strplugin );
			messagesError.append( "\" não foi carregada!\n" );
			messagesError.append( error.getMessage() );
			errorCount++;
		} catch ( IllegalAccessException error ) {
			messagesError.append( strplugin );
			messagesError.append( "\" não foi carregada!\n" );
			messagesError.append( error.getMessage() );
			errorCount++;
		} catch ( Exception error ) {
			messagesError.append( strplugin );
			messagesError.append( "\" não foi carregada!\n" );
			messagesError.append( error.getMessage() );
			errorCount++;
		} finally {
			if ( errorCount > 0 ) {
				Funcoes.mensagemErro( this, messagesError.toString() );
			}
		}

	}

	public void keyPressed( KeyEvent kevt ) {

		if ( kevt.getKeyCode() == KeyEvent.VK_F12 ) {
			btPesagem.doClick();
		}
		if ( kevt.getKeyCode() == KeyEvent.VK_F11 ) {
			liberaCampo( true );
		}


	}
	
	private void liberaRenda( boolean libera){
		
		txtRendaAmostragem.setAtivo( libera );
		txtMediaAmostragem.setAtivo( libera );
		
	}
	
	private void liberaPeso( boolean libera){

		tabPesagem.setColunaEditavel( 3, libera );
		tabPesagem.setColunaEditavel( 4, libera );
		
		atualizaPesoManual = libera;
	
	}

	
	private void liberaCampo( boolean libera ) {

		FPassword fpw = new FPassword( this, FPassword.LIBERA_CAMPO_PESAGEM, null, con );
		fpw.execShow();

		if ( fpw.OK ) {
			liberaRenda(libera);			
			liberaPeso(libera);
		}

		fpw.dispose();

	}

	

	

}
