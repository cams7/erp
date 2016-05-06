/*
 * Projeto: Freedom Pacote: org.freedom.modules.fnc Classe: @(#)FConsultaCheque.java
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR> modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR> na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR> sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR> Veja a Licença Pública Geral GNU para maiores detalhes. <BR> Você deve ter recebido uma cópia da Licença Pública
 * Geral GNU junto com este programa, se não, <BR> escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA <BR>
 */

package org.freedom.modulos.fnc.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.RowSorter;
import javax.swing.SwingConstants;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.TabelaSelEvent;
import org.freedom.acao.TabelaSelListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
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
import org.freedom.library.swing.frame.FFilho;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.util.SwingParams;
import org.freedom.library.type.StringDireita;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.fnc.business.object.Cheque;
import org.freedom.modulos.fnc.view.dialog.utility.DLDataCompensacao;
import org.freedom.modulos.fnc.view.frame.crud.detail.FCheque;

/**
 * Consulta cheques emitidos/recebidos.
 * 
 * @author Setpoint Informática Ltda./Anderson Sanchez
 * @version 07/10/2010
 */
public class FConsultaCheque extends FFilho implements ActionListener, TabelaSelListener, MouseListener, KeyListener, CarregaListener, FocusListener {

	private static final long serialVersionUID = 1L;

	private static final Color GREEN = new Color( 45, 190, 60 );

	private JPanelPad panelGeral = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad panelMaster = new JPanelPad( 700, 140 );
	
	private JPanelPad panelEsquerdo = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad panelDetail = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	private JTabbedPanePad tabbedDetail = new JTabbedPanePad();

	private JPanelPad panelTotais = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad panelTabTotais = new JPanelPad( 700, 60 );

	private JPanelPad panelGridCheques = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout(  ) );

	private JPanelPad panelTabCheques = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	private JPanelPad panelTabContasPagas = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	private JPanelPad panelReceber = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad panelHistorico = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad panelSouth = new JPanelPad( new BorderLayout() );

	private JPanelPad pnBotoes = new JPanelPad( new GridLayout( 1,2 ) );

	// *** Geral

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JLabelPad lbAtivoCli = new JLabelPad( "Ativo", SwingConstants.CENTER );

	private JButtonPad btBuscar = new JButtonPad( "Buscar cheques", Icone.novo( "btExecuta.png" ) );
	
	private JButtonPad btImprimir = new JButtonPad( "Imprimir", Icone.novo( "btImprime.png" ) );
	
	private JButtonPad btPrevImp = new JButtonPad( "Visualizar", Icone.novo( "btPrevimp.png" ) );
	
	private JButtonPad btCompensar = new JButtonPad( "Compensar", Icone.novo( "btCheque.png" ) );
	
	private JTextFieldPad txtCodBanco = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );
	
	private JTextFieldFK txtDescBanco = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	// *** Vendas

	private JTextFieldFK txtVlrTotalChequesGeral = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 12, Aplicativo.casasDecFin );
	
	private JTextFieldFK txtVlrTotalChequesCadastrados = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 12, Aplicativo.casasDecFin );
	
	private JTextFieldFK txtVlrTotalChequesEmitidos = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 12, Aplicativo.casasDecFin );
	
	private JTextFieldFK txtVlrTotalChequesCompensados = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 12, Aplicativo.casasDecFin );
	
	private JTextFieldFK txtVlrTotalChequesDevolvidos = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 12, Aplicativo.casasDecFin );
	
	private JTablePad tabPrincipal = new JTablePad();
	
	private JTablePad tabCheques = new JTablePad();

	private JTablePad tabContasPagas = new JTablePad();

	private ImageIcon imgCancelado = Icone.novo( "clVencido.gif" );

	private ImageIcon imgPedido = Icone.novo( "clPagoParcial.gif" );

	private ImageIcon imgFaturado = Icone.novo( "clPago.gif" );

	private ImageIcon imgColuna = null;

	// *** Listacampos

	private ListaCampos lcBanco = new ListaCampos( this );

	private boolean carregandoVendas = false;
	
	private Vector<String> vValsData = new Vector<String>();

	private Vector<String> vLabsData = new Vector<String>();
	
	private JRadioGroup<?, ?> rgData = null;
	
	private ListaCampos lcConta = new ListaCampos( this );
	
	private JTextFieldPad txtNumConta = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );
	
	private JTextFieldFK txtDescConta = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldPad txtNomeFav = new JTextFieldPad( JTextFieldPad.TP_STRING, 100, 0 );
	
	private JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtCNPJFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldFK txtRazFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );
	
	private JCheckBoxPad cbCadastrado = new JCheckBoxPad( Cheque.SIT_CHEQUE_CADASTRADO.getName() + "s" , "S", "N" );
	
	private JCheckBoxPad cbEmitido = new JCheckBoxPad( Cheque.SIT_CHEQUE_EMITIDO.getName() + "s", "S", "N" );
	
	private JCheckBoxPad cbCompensado = new JCheckBoxPad( Cheque.SIT_CHEQUE_COMPENSADO.getName() + "s", "S", "N" );
	
	private JCheckBoxPad cbDevolvido = new JCheckBoxPad( Cheque.SIT_CHEQUE_DEVOLVIDO.getName() + "s", "S", "N" );
	
	private JCheckBoxPad cbCancelado = new JCheckBoxPad( Cheque.SIT_CHEQUE_CANCELADO.getName() + "s", "S", "N" );
	
	private ListaCampos lcFor = new ListaCampos( this );
	
	private int periodoCons = 2;


	private enum enum_grid_cheques {
		SEL, NUMCHEQ, NOMEFAVCHEQ, VLRCHEQ, DTEMITCHEQ, DTVENCTOCHEQ, DTCOMPCHEQ, SITCHEQ, SEQCHEQ, CODBANC, AGENCIACHEQ, CONTACHEQ, NOMEEMITCHEQ,  TIPOCHEQ  ;
	}

	private enum enum_grid_contas_pagas {
		STATUSITPAG, CODFOR, RAZFOR, CODPAG, NPARCPAG, DOCLANCAITPAG, OBSITPAG, DTITPAG, DTVENCITPAG, DTPAGOITPAG, VLRPARCITPAG, VLRPAGOITPAG, VLRAPAGITPAG ;
	}

	public FConsultaCheque() {

		super(false);
		setTitulo( "Consulta cheques", this.getClass().getName() );
		setAtribos( 20, 20, 1000 , 800 );
	
		int x = (int) ( Aplicativo.telaPrincipal.dpArea.getSize().getWidth() - getWidth() ) / 2;
		int y = (int) ( Aplicativo.telaPrincipal.dpArea.getSize().getHeight() - getHeight() ) / 2;
		setLocation( x, y );

		montaListaCampos();
		montaTela();

		btBuscar.addActionListener( this );
		btPrevImp.addActionListener( this );
		btImprimir.addActionListener( this );
		btCompensar.addActionListener( this );
		
		tabCheques.addTabelaSelListener( this );
		tabCheques.addMouseListener( this );
		tabContasPagas.addMouseListener( this );
		
		btBuscar.addKeyListener( this );
		txtCodBanco.addFocusListener( this );
		
	}

	private void montaListaCampos() {

		lcBanco.add( new GuardaCampo( txtCodBanco, "CodBanco", "Cód.banco", ListaCampos.DB_PK, false ) );
		lcBanco.add( new GuardaCampo( txtDescBanco, "NomeBanco", "Nome do banco", ListaCampos.DB_SI, false ) );
		lcBanco.montaSql( false, "BANCO", "FN" );
		lcBanco.setQueryCommit( false );
		lcBanco.setReadOnly( true );
		txtCodBanco.setTabelaExterna( lcBanco, null );
		txtCodBanco.setFK( true );
		txtCodBanco.setNomeCampo( "CodBanco" );
		
		lcConta.add( new GuardaCampo( txtNumConta, "NumConta", "Nº Conta", ListaCampos.DB_PK, false ) );
		lcConta.add( new GuardaCampo( txtDescConta, "DescConta", "Descrição da conta", ListaCampos.DB_SI, false ) );
		lcConta.add( new GuardaCampo( txtCodBanco, "CodBanco", "Cod.Banco", ListaCampos.DB_FK, false ) );	
		lcConta.montaSql( false, "CONTA", "FN" );
		lcConta.setReadOnly( true );
		txtNumConta.setTabelaExterna( lcConta, null );
		txtNumConta.setFK( true );
		txtNumConta.setNomeCampo( "NumConta" );

		lcFor.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.for.", ListaCampos.DB_PK, false ) );
		lcFor.add( new GuardaCampo( txtRazFor, "RazFor", "Descrição do fornecedor", ListaCampos.DB_SI, false ) );
		lcFor.add( new GuardaCampo( txtCNPJFor, "CnpjFor", "CNPJ", ListaCampos.DB_SI, false ) );

		lcFor.montaSql( false, "FORNECED", "CP" );
		lcFor.setQueryCommit( false );
		lcFor.setReadOnly( true );
		txtCodFor.setTabelaExterna( lcFor, null );
		txtCodFor.setFK( true );
		txtCodFor.setNomeCampo( "CodFor" );
	}

	private void montaTela() {

		vValsData.addElement( "E" );
		vValsData.addElement( "V" );
		vValsData.addElement( "C" );
		
		vLabsData.addElement( "Emissão" );
		vLabsData.addElement( "Vencimento" );
		vLabsData.addElement( "Compensação" );
		
		rgData = new JRadioGroup<String, String>( 3, 1, vLabsData, vValsData );
		rgData.setVlrString( "E" );
		
		rgData.setRequestFocusEnabled( false );
		
		Container c = getContentPane();
		c.setLayout( new BorderLayout() );

		c.add( panelGeral, BorderLayout.CENTER );
		panelGeral.add( panelMaster, BorderLayout.NORTH );

		// *****Cabeçalho
		JPanelPad cabecalho = new JPanelPad();
		panelMaster.adic( cabecalho, 0, 0, 485, 90, "" );
		
		cabecalho.adic( txtCodBanco, 7, 20, 70, 20, "Cod.Banc." );
		cabecalho.adic( txtDescBanco, 80, 20, 150, 20, "Nome do banco" );
		cabecalho.adic( txtCodFor, 233, 20, 70, 20, "Cod.For." );
		cabecalho.adic( txtRazFor, 306, 20, 170, 20, "Razão do fornecedor" );
		cabecalho.adic( txtNomeFav, 233, 60, 243, 20 ,"Nome do favorecido");
		

		cabecalho.adic( txtNumConta, 7, 60, 70, 20, "Conta" );
		cabecalho.adic( txtDescConta, 80, 60, 150, 20, "Nome da conta" );
		
		panelMaster.adic( btBuscar, 622, 60, 215, 30, "" );
		panelMaster.adic( rgData, 490, 8, 130, 83, "" );
		
		JLabelPad separacao = new JLabelPad();
		separacao.setBorder( BorderFactory.createEtchedBorder() );
		panelMaster.adic( separacao, 7, 93, 830, 2, "" );
		
		JPanelPad periodo = new JPanelPad();
		periodo.setBorder( SwingParams.getPanelLabel( "Período", Color.BLUE ) );
		panelMaster.adic( periodo, 620, 0, 220, 60, "" );
		periodo.adic( txtDataini, 7, 5, 70, 20, "" );
		periodo.adic( new JLabel( "até", SwingConstants.CENTER ), 80, 5, 40, 20,"" );
		periodo.adic( txtDatafim, 120, 5, 70, 20, "" );
		
		
		panelMaster.adic( cbCadastrado, 7, 105, 120, 20, "" );
		panelMaster.adic( cbEmitido, 130, 105, 120, 20, "" );
		panelMaster.adic( cbDevolvido, 260, 105, 120, 20, "" );
		panelMaster.adic( cbCompensado, 390, 105, 120, 20, "" );
		panelMaster.adic( cbCancelado, 510, 105, 120, 20, "" );
		
		cbEmitido.setVlrString( "S" );

		// ***** Detalhamento (abas)

	//	panelGeral.add( panelDetail, BorderLayout.CENTER );
		panelGeral.add( tabbedDetail );
		tabbedDetail.addTab( "Totais", panelTotais );
		
		panelTabTotais.adic( txtVlrTotalChequesCadastrados,   7	, 20, 120, 20, "Total cadastrados" );
		panelTabTotais.adic( txtVlrTotalChequesEmitidos, 	130	, 20, 120, 20, "Total emitidos" );
		panelTabTotais.adic( txtVlrTotalChequesDevolvidos, 	253	, 20, 120, 20, "Total devolvidos" );
		panelTabTotais.adic( txtVlrTotalChequesCompensados, 376	, 20, 120, 20, "Total compensados" );
		panelTabTotais.adic( txtVlrTotalChequesGeral, 		499	, 20, 120, 20, "Total geral" );

		// ***** Totais

		panelTotais.add( panelTabTotais, BorderLayout.NORTH );
		panelTotais.add( panelGridCheques, BorderLayout.CENTER );
		
		panelGridCheques.add( panelTabCheques, BorderLayout.CENTER );
		panelGridCheques.add( panelTabContasPagas, BorderLayout.SOUTH );

		panelTabCheques.setBorder( BorderFactory.createTitledBorder( "Cheques" ) );
		panelTabContasPagas.setBorder( BorderFactory.createTitledBorder( "Contas pagas" ) );
		panelTabContasPagas.setPreferredSize( new Dimension( 700, 100 ) );
		//NUMCHEQ, NOMEFAVCHEQ, VLRCHEQ, DTEMITCHEQ, DTVENCTOCHEQ, DTCOMPCHEQ, SITCHEQ, SEQCHEQ, CODBANC, AGENCIACHEQ, CONTACHEQ, NOMEEMITCHEQ,  TIPOCHEQ 
		
		tabCheques.adicColuna( "" 	);					// Selecionar
		tabCheques.adicColuna( "Cheque" 	);			// NUMCHEQ
		tabCheques.adicColuna( "Favorecido" );			// NOMEFAVCHEQ
		tabCheques.adicColuna( "Valor" 		);			// VLRCHEQ
		tabCheques.adicColuna( "Emissão" 	);			// DTEMITCHEQ
		tabCheques.adicColuna( "Vencto." 	);			// DTVENCTOCHEQ		
		tabCheques.adicColuna( "Compens." 	);			// DTCOMPCHEQ
		tabCheques.adicColuna( "Situação" 	);			// SITCHQ
		tabCheques.adicColuna( "Seq." 		);			// SEQCHEQ
		tabCheques.adicColuna( "Banco"		);			// CODBANC
		tabCheques.adicColuna( "Agencia"	);			// AGENCIAQUEQ
		tabCheques.adicColuna( "Conta" 		);			// CONTACHEQ
		tabCheques.adicColuna( "Emitente" 	);			// NOMEEMITCHEQ

		tabCheques.adicColuna( "Tipo" 		);			// TIPOCHEQ

		tabCheques.setTamColuna( 20		,enum_grid_cheques.SEL.ordinal() );
		tabCheques.setTamColuna( 70		,enum_grid_cheques.NUMCHEQ.ordinal() );
		tabCheques.setTamColuna( 185	,enum_grid_cheques.NOMEFAVCHEQ.ordinal() );
		tabCheques.setTamColuna( 70		,enum_grid_cheques.VLRCHEQ.ordinal() );
		tabCheques.setTamColuna( 70		,enum_grid_cheques.DTEMITCHEQ.ordinal() );
		tabCheques.setTamColuna( 70		,enum_grid_cheques.DTVENCTOCHEQ.ordinal() );
		tabCheques.setTamColuna( 70		,enum_grid_cheques.DTCOMPCHEQ.ordinal() );
		tabCheques.setTamColuna( 20		,enum_grid_cheques.SITCHEQ.ordinal() );
		tabCheques.setTamColuna( 30		,enum_grid_cheques.SEQCHEQ.ordinal() );
		tabCheques.setTamColuna( 40		,enum_grid_cheques.CODBANC.ordinal() );
		tabCheques.setTamColuna( 50		,enum_grid_cheques.AGENCIACHEQ.ordinal() );
		tabCheques.setTamColuna( 45		,enum_grid_cheques.CONTACHEQ.ordinal() );
		tabCheques.setTamColuna( 125	,enum_grid_cheques.NOMEEMITCHEQ.ordinal() );
		
		tabCheques.setTamColuna( 30		,enum_grid_cheques.TIPOCHEQ.ordinal() );
		
		tabCheques.setColunaEditavel( enum_grid_cheques.SEL.ordinal(), true );

		tabCheques.setRowHeight( 21 );
		
		panelTabCheques.add( new JScrollPane( tabCheques ) );
		
		tabContasPagas.adicColuna( "" 				);			//	STATUSITPAG
		tabContasPagas.adicColuna( "Cód.For." 		);			//	CODFOR	
		tabContasPagas.adicColuna( "Fornecedor"		);			//	RAZFOR
		tabContasPagas.adicColuna( "Cod.Pag." 		);			//	CODPAG
		tabContasPagas.adicColuna( "N.Parc." 		);			//	NPARCPAG
		tabContasPagas.adicColuna( "Doc." 			);			//	DOCLANCAITPAG
		tabContasPagas.adicColuna( "Obs." 			);			//	OBSITPAG
		tabContasPagas.adicColuna( "Emissão" 		);			//	DTITPAG
		tabContasPagas.adicColuna( "Vencto." 		);			//	DTVENCITPAG
		tabContasPagas.adicColuna( "Dt.Pgto." 		);			//	DTPAGOITPAG
		tabContasPagas.adicColuna( "Vlr.orig." 		);			//	VLRPARCITPAG
		tabContasPagas.adicColuna( "Vlr.pago" 		);			//	VLRPAGOITPAG
		tabContasPagas.adicColuna( "Vlr.a.pag." 	);			//	VLRPAGITPAG

		tabContasPagas.setTamColuna( 20		, enum_grid_contas_pagas.STATUSITPAG.ordinal() 	);
		tabContasPagas.setTamColuna( 50		, enum_grid_contas_pagas.CODFOR.ordinal() 		);
		tabContasPagas.setTamColuna( 145	, enum_grid_contas_pagas.RAZFOR.ordinal()	 	);
		tabContasPagas.setTamColuna( 55		, enum_grid_contas_pagas.CODPAG.ordinal()		);
		tabContasPagas.setTamColuna( 45		, enum_grid_contas_pagas.NPARCPAG.ordinal() 	);
		tabContasPagas.setTamColuna( 45		, enum_grid_contas_pagas.DOCLANCAITPAG.ordinal());
		tabContasPagas.setTamColuna( 100	, enum_grid_contas_pagas.OBSITPAG.ordinal() 	);
		tabContasPagas.setTamColuna( 70		, enum_grid_contas_pagas.DTITPAG.ordinal() 		);
		tabContasPagas.setTamColuna( 70		, enum_grid_contas_pagas.DTVENCITPAG.ordinal() 	);
		tabContasPagas.setTamColuna( 70		, enum_grid_contas_pagas.DTPAGOITPAG.ordinal() 	);
		tabContasPagas.setTamColuna( 60		, enum_grid_contas_pagas.VLRPARCITPAG.ordinal() );
		tabContasPagas.setTamColuna( 60		, enum_grid_contas_pagas.VLRPAGOITPAG.ordinal() );
		tabContasPagas.setTamColuna( 60		, enum_grid_contas_pagas.VLRAPAGITPAG.ordinal() );		

		tabContasPagas.setRowHeight( 21 );
		
		panelTabContasPagas.add( new JScrollPane( tabContasPagas ) );

		// ***** Rodapé

		//panelGeral.add( panelSouth, BorderLayout.SOUTH );
		panelGeral.add( panelSouth, BorderLayout.SOUTH );
		panelSouth.setBorder( BorderFactory.createEtchedBorder() );

		pnBotoes.add( btPrevImp );
		pnBotoes.add( btImprimir );
		pnBotoes.add (btCompensar);
		
		panelSouth.add( adicBotaoSair(), BorderLayout.EAST );
		panelSouth.add( pnBotoes, BorderLayout.WEST );

	}
	
	private void inserePeriodo() {
		
		Calendar periodoConsulta = Calendar.getInstance();
		txtDatafim.setVlrDate( periodoConsulta.getTime() );
		//periodo igual a 1 = year e 2 = mes
		periodoConsulta.set( periodoCons, periodoConsulta.get( periodoCons ) - 1 );
		txtDataini.setVlrDate( periodoConsulta.getTime() );
		
	}

	private void compensaCheques( Date datacomp) {
		
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		
		try {
			
			sql.append("update fncheque ");
			sql.append("set dtcompcheq=?, sitcheq=? ");
			sql.append("where codemp=? and codfilial=? and seqcheq=? and sitcheq='ED' ");
			
			int compensados = 0;
			
			for(int i=0; tabCheques.getNumLinhas()> i; i++) {
			
				Boolean compensar = (Boolean) tabCheques.getValor( i, enum_grid_cheques.SEL.ordinal() );
				
				if(compensar) {
					
					ps = con.prepareStatement( sql.toString() );
					
					ps.setDate( 	1	, Funcoes.dateToSQLDate( datacomp ) );
					ps.setString( 	2	, "CD" );
					
					ps.setInt( 		3	, Aplicativo.iCodEmp );
					ps.setInt( 		4	, ListaCampos.getMasterFilial( "FNCHEQUE" ) );
					ps.setInt( 		5	, (Integer) tabCheques.getValor( i, enum_grid_cheques.SEQCHEQ.ordinal() ));
					
					compensados += ps.executeUpdate();
					
					con.commit();
					
					ps.close();
					
					
				}				
				
			}
			
			Funcoes.mensagemInforma( this, compensados + " cheques compesados!" );
			
			buscaCheques();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void buscaCheques() {
		
		BigDecimal total_cheques_geral 			= new BigDecimal(0);
		BigDecimal total_cheques_cadastrados	= new BigDecimal(0);
		BigDecimal total_cheques_emitidos 		= new BigDecimal(0);
		BigDecimal total_cheques_compensados 	= new BigDecimal(0);
		BigDecimal total_cheques_devolvidos 	= new BigDecimal(0);

		try {

			tabCheques.setRowSorter( null );
			
			ResultSet rs = queryCheques();

			carregandoVendas = true;
			tabCheques.limpa();
			tabContasPagas.limpa();

			int row = 0;
			
			while ( rs.next() ) {

				tabCheques.adicLinha();
				
				tabCheques.setValor( new Boolean(false), row, enum_grid_cheques.SEL.ordinal() );
				
				tabCheques.setValor( rs.getString( enum_grid_cheques.SITCHEQ.name()), row, enum_grid_cheques.SITCHEQ.ordinal() );				
				tabCheques.setValor( rs.getInt( enum_grid_cheques.SEQCHEQ.name() ), row, enum_grid_cheques.SEQCHEQ.ordinal() );
				tabCheques.setValor( rs.getString( enum_grid_cheques.CODBANC.name() ), row, enum_grid_cheques.CODBANC.ordinal() );
				tabCheques.setValor( rs.getString( enum_grid_cheques.AGENCIACHEQ.name() ), row, enum_grid_cheques.AGENCIACHEQ.ordinal() );
				tabCheques.setValor( rs.getString( enum_grid_cheques.CONTACHEQ.name() ), row, enum_grid_cheques.CONTACHEQ.ordinal() );
				
				tabCheques.setValor( StringFunctions.strZero( rs.getString( enum_grid_cheques.NUMCHEQ.name() ), 6), row, enum_grid_cheques.NUMCHEQ.ordinal() );
				
				tabCheques.setValor( rs.getString( enum_grid_cheques.NOMEEMITCHEQ.name() ), row, enum_grid_cheques.NOMEEMITCHEQ.ordinal() );
				tabCheques.setValor( rs.getString( enum_grid_cheques.NOMEFAVCHEQ.name() ), row, enum_grid_cheques.NOMEFAVCHEQ.ordinal() );
				tabCheques.setValor( rs.getDate( enum_grid_cheques.DTEMITCHEQ.name() ), row, enum_grid_cheques.DTEMITCHEQ.ordinal() );
				tabCheques.setValor( rs.getDate( enum_grid_cheques.DTVENCTOCHEQ.name() ), row, enum_grid_cheques.DTVENCTOCHEQ.ordinal() );
				tabCheques.setValor( rs.getDate( enum_grid_cheques.DTCOMPCHEQ.name() ), row, enum_grid_cheques.DTCOMPCHEQ.ordinal() );
				tabCheques.setValor( rs.getString( enum_grid_cheques.TIPOCHEQ.name() ), row, enum_grid_cheques.TIPOCHEQ.ordinal() );
						
				tabCheques.setValor( rs.getBigDecimal( enum_grid_cheques.VLRCHEQ.name() ) , row, enum_grid_cheques.VLRCHEQ.ordinal() );

				row++;
				
				total_cheques_geral = total_cheques_geral.add(  rs.getBigDecimal( enum_grid_cheques.VLRCHEQ.name() ) );
				
				if( Cheque.SIT_CHEQUE_CADASTRADO.getValue().equals( rs.getString( enum_grid_cheques.SITCHEQ.name()) ) ) {
					total_cheques_cadastrados = total_cheques_cadastrados.add( rs.getBigDecimal( enum_grid_cheques.VLRCHEQ.name() ) );
				}
				else if( Cheque.SIT_CHEQUE_EMITIDO.getValue().equals( rs.getString( enum_grid_cheques.SITCHEQ.name()) ) ) {
					total_cheques_emitidos = total_cheques_emitidos.add( rs.getBigDecimal( enum_grid_cheques.VLRCHEQ.name() ) );
				}
				else if( Cheque.SIT_CHEQUE_DEVOLVIDO.getValue().equals( rs.getString( enum_grid_cheques.SITCHEQ.name()) ) ) {
					total_cheques_devolvidos = total_cheques_devolvidos.add( rs.getBigDecimal( enum_grid_cheques.VLRCHEQ.name() ) ); 
				}
				else if( Cheque.SIT_CHEQUE_COMPENSADO.getValue().equals( rs.getString( enum_grid_cheques.SITCHEQ.name()) ) ) {
					total_cheques_compensados = total_cheques_compensados.add( rs.getBigDecimal( enum_grid_cheques.VLRCHEQ.name() ) ); 
				}
				
				
			}
			
			txtVlrTotalChequesGeral.setVlrBigDecimal( total_cheques_geral );
			txtVlrTotalChequesCadastrados.setVlrBigDecimal( total_cheques_cadastrados );
			txtVlrTotalChequesEmitidos.setVlrBigDecimal( total_cheques_emitidos );
			txtVlrTotalChequesDevolvidos.setVlrBigDecimal( total_cheques_devolvidos );			
			txtVlrTotalChequesCompensados.setVlrBigDecimal( total_cheques_compensados );
			

			rs.close();
			
			con.commit();

			carregandoVendas = false;
			//tabCheques.requestFocus();
		
			// Permitindo reordenação
			if ( row > 0 ) {
				RowSorter<TableModel> sorter = new TableRowSorter<TableModel>( tabCheques.getModel() );
				tabCheques.setRowSorter( sorter );
			} else {
				tabCheques.setRowSorter( null );
			}
			
			
		} 
		catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	private ResultSet queryCheques() {
		
		ResultSet rs = null;
		
		try {

			StringBuilder sql = new StringBuilder();
			
			sql.append( "select ");
			sql.append( "ch.sitcheq, ch.seqcheq, ch.codbanc, ch.agenciacheq, ch.contacheq, ch.numcheq, ch.nomeemitcheq, ch.nomefavcheq, ch.dtemitcheq, ");
			sql.append( "ch.dtvenctocheq, ch.dtcompcheq, ch.tipocheq, ch.vlrcheq ");
			sql.append( "from fncheque ch ");
			sql.append( "where ");
			sql.append( "ch.codemp=? and ch.codfilial=? and ");
			
			if("E".equals(rgData.getVlrString())){
				sql.append( "ch.dtemitcheq ");
			}
			else if("V".equals(rgData.getVlrString())){
				sql.append( "ch.dtvenctocheq ");
			}
			else if("C".equals(rgData.getVlrString())){
				sql.append( "ch.dtcompcheq "); 	
			}
			
			sql.append( " between ? and ? ");
			
			if( txtCodBanco.getVlrString().length()>0 ) {
				
				sql.append( " and ch.codempbo=? and ch.codfilialbo=? and ch.codbanc=? " );
				
			}
			
			if( txtNumConta.getVlrString().length()>0 ) {
				
				sql.append( " and ch.contacheq=? " );
				
			}

			if( txtCodFor.getVlrInteger()>0 ) {
				sql.append( "and ch.seqcheq in (" );
				sql.append( "select pc.seqcheq ");
				sql.append( "from  fnpagcheq pc, fnpagar pg ");
				sql.append( "where pg.codemp=pc.codemp and pg.codfilial=pc.codfilial and pg.codpag=pc.codpag ");
				sql.append( "and pg.codempfr=? and pg.codfilialfr=? and pg.codfor=? ) "); 
				
			}
			
			if(txtNomeFav.getVlrString().length()>0) {
				
				sql.append( " and ch.nomefavcheq ");
				
				if(txtNomeFav.getVlrString().indexOf( "%" )>-1) {					
					sql.append( " like ? " );					
				}
				else {
					sql.append( " = ? " );
				}				
				
			}
			
			Vector<String> status_cheque = new Vector<String>();
			
			if( "S".equals(cbCadastrado.getVlrString()) ) {
				status_cheque.addElement( Cheque.SIT_CHEQUE_CADASTRADO.getValue().toString() );
			}
			if ( "S".equals(cbEmitido.getVlrString()) ) {
				status_cheque.addElement( Cheque.SIT_CHEQUE_EMITIDO.getValue().toString() );
			}
			if ( "S".equals(cbDevolvido.getVlrString()) ) {
				status_cheque.addElement( Cheque.SIT_CHEQUE_DEVOLVIDO.getValue().toString() );
			}
			if ( "S".equals(cbCompensado.getVlrString()) ) {
				status_cheque.addElement( Cheque.SIT_CHEQUE_COMPENSADO.getValue().toString() );
			}
			if ( "S".equals(cbCancelado.getVlrString()) ) {
				status_cheque.addElement( Cheque.SIT_CHEQUE_CANCELADO.getValue().toString() );
			}
			
			sql.append( " and ch.sitcheq in ('" + Funcoes.vectorToString( status_cheque, "','" ) + "') " );
			
			sql.append( " order by ch.dtemitcheq, ch.numcheq " );
			
			System.out.println("SQL:" + sql.toString());		

			PreparedStatement ps = con.prepareStatement( sql.toString() );

			int iparam = 1;

			ps.setInt( iparam++, Aplicativo.iCodEmp );
			ps.setInt( iparam++, ListaCampos.getMasterFilial( "FNCHEQUE" ) );
			ps.setDate( iparam++, Funcoes.dateToSQLDate( txtDataini.getVlrDate()) );
			ps.setDate( iparam++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate()) );
			
			if( txtCodBanco.getVlrString().length()>0 ) {
				
				ps.setInt( iparam++, Aplicativo.iCodEmp );
				ps.setInt( iparam++, ListaCampos.getMasterFilial( "FNBANCO" ) );
				ps.setString( iparam++, txtCodBanco.getVlrString() );
	
				
			}
			
			if( txtNumConta.getVlrString().length()>0 ) {
				
				ps.setString( iparam++, txtNumConta.getVlrString() );
				
			}
			
			if( txtCodFor.getVlrInteger()>0 ) {
				
				ps.setInt( iparam++, Aplicativo.iCodEmp );
				ps.setInt( iparam++, ListaCampos.getMasterFilial( "CPFORNECED" ) );
				ps.setInt( iparam++, txtCodFor.getVlrInteger() );
				
			}
			
			if(txtNomeFav.getVlrString().length()>0) {
				ps.setString( iparam++, txtNomeFav.getVlrString() );
			}
			
			rs = ps.executeQuery();

		} 
		catch ( Exception e ) {
			e.printStackTrace();
		}
		
		return rs;
		
	}

	private void buscaContasPagas() {

		try {
			StringBuilder sql = new StringBuilder();
			
			sql.append( "select ");
			sql.append( "ip.statusitpag, pg.codfor, fr.razfor, ip.codpag, ip.nparcpag, ip.doclancaitpag, ip.obsitpag, ");
			sql.append( "ip.dtitpag, ip.dtvencitpag, ip.dtpagoitpag, ip.vlrparcitpag, ip.vlrpagoitpag, ip.vlrpagoitpag, ");
			sql.append( "ip.vlrapagitpag ");
			sql.append( "from fnpagcheq pc, fnpagar pg, cpforneced fr, fnitpagar ip ");
			sql.append( "where ");
			sql.append( "pg.codemp=ip.codemp and pg.codfilial=ip.codfilial and pg.codpag=ip.codpag ");
			sql.append( "and fr.codemp=pg.codempfr and fr.codfilial=pg.codfilialfr and fr.codfor=pg.codfor ");
			sql.append( "and pc.codemp=ip.codemp and pc.codfilial=ip.codfilial and pc.codpag=ip.codpag and pc.nparcpag=ip.nparcpag ");
			sql.append( "and pc.codempch=? and pc.codfilial=? and pc.seqcheq=? " );
			
			sql.append( "order by ip.dtitpag " );

			PreparedStatement ps = con.prepareStatement( sql.toString() );

			int iparam = 1;

			ps.setInt( iparam++, Aplicativo.iCodEmp );
			ps.setInt( iparam++, ListaCampos.getMasterFilial( "FNCHEQUE" ) );
			ps.setInt( iparam++, (Integer) tabCheques.getValor( tabCheques.getSelectedRow(), enum_grid_cheques.SEQCHEQ.ordinal() ) );

			ResultSet rs = ps.executeQuery();

			tabContasPagas.limpa();

			int row = 0;

			while ( rs.next() ) {

				tabContasPagas.adicLinha();

				tabContasPagas.setValor( rs.getString( enum_grid_contas_pagas.STATUSITPAG.name() ), row, enum_grid_contas_pagas.STATUSITPAG.ordinal() );
				tabContasPagas.setValor( rs.getInt( enum_grid_contas_pagas.CODFOR.name() ), row, enum_grid_contas_pagas.CODFOR.ordinal() );
				tabContasPagas.setValor( rs.getString( enum_grid_contas_pagas.RAZFOR.name() ), row, enum_grid_contas_pagas.RAZFOR.ordinal() );
				tabContasPagas.setValor( rs.getInt( enum_grid_contas_pagas.CODPAG.name() ), row, enum_grid_contas_pagas.CODPAG.ordinal() );
				tabContasPagas.setValor( rs.getInt( enum_grid_contas_pagas.NPARCPAG.name() ), row, enum_grid_contas_pagas.NPARCPAG.ordinal() );
				tabContasPagas.setValor( rs.getString( enum_grid_contas_pagas.DOCLANCAITPAG.name() ), row, enum_grid_contas_pagas.DOCLANCAITPAG.ordinal() );
				tabContasPagas.setValor( rs.getString( enum_grid_contas_pagas.OBSITPAG.name() ), row, enum_grid_contas_pagas.OBSITPAG.ordinal() );
				tabContasPagas.setValor( rs.getDate( enum_grid_contas_pagas.DTITPAG.name() ), row, enum_grid_contas_pagas.DTITPAG.ordinal() );
				tabContasPagas.setValor( rs.getDate( enum_grid_contas_pagas.DTVENCITPAG.name() ), row, enum_grid_contas_pagas.DTVENCITPAG.ordinal() );
				tabContasPagas.setValor( rs.getDate( enum_grid_contas_pagas.DTPAGOITPAG.name() ), row, enum_grid_contas_pagas.DTPAGOITPAG.ordinal() );

				tabContasPagas.setValor( new StringDireita( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( enum_grid_contas_pagas.VLRPARCITPAG.name() ) )), row, enum_grid_contas_pagas.VLRPARCITPAG.ordinal() );
				tabContasPagas.setValor( new StringDireita( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( enum_grid_contas_pagas.VLRPAGOITPAG.name() ) )), row, enum_grid_contas_pagas.VLRPAGOITPAG.ordinal() );
				tabContasPagas.setValor( new StringDireita( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( enum_grid_contas_pagas.VLRAPAGITPAG.name() ) )), row, enum_grid_contas_pagas.VLRAPAGITPAG.ordinal() );

				row++;
			}

			rs.close();
			ps.close();
			con.commit();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	public void actionPerformed( ActionEvent e ) {

		if ( e.getSource() == btBuscar ) {
			buscaCheques();
		}
		else if ( e.getSource() == btImprimir ) {
			imprimir( TYPE_PRINT.PRINT);
		}
		else if ( e.getSource() == btPrevImp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
		else if (e.getSource() == btCompensar) {
			
			DLDataCompensacao dl = new DLDataCompensacao( this );
			dl.setVisible( true );
			
			if ( !dl.OK ) {
				dl.dispose();
				return;
			}
			
			Date datacomp = dl.getValor();
			
			dl.dispose();
			
			compensaCheques( datacomp);
		}
	}

	public void valorAlterado( TabelaSelEvent e ) {

		if ( e.getTabela() == tabCheques && tabCheques.getLinhaSel() > -1 && !carregandoVendas ) {
			buscaContasPagas( );
		}
	}

	public void mouseClicked( MouseEvent e ) {

		if ( e.getClickCount() == 2 ) {
			if ( e.getSource() == tabCheques && tabCheques.getLinhaSel() > -1 ) {
				FCheque cheque = null;
				if ( Aplicativo.telaPrincipal.temTela( FCheque.class.getName() ) ) {
					cheque = (FCheque) Aplicativo.telaPrincipal.getTela( FCheque.class.getName() );
				}
				else {
					cheque = new FCheque();
					Aplicativo.telaPrincipal.criatela( "Cheque", cheque, con );
				}
				cheque.exec( (Integer) tabCheques.getValor( tabCheques.getLinhaSel(), enum_grid_cheques.SEQCHEQ.ordinal() ) );
			}
		}
	}
	
	private String buscaPrefereFNC() {
		String periodoConsulta = null;
		
		try {
			StringBuilder sql = new StringBuilder();
			
			sql.append( "select periodoConsCH ");
			sql.append( "from sgprefere1 sg ");
			sql.append( "where ");
			sql.append( "sg.codemp=? and sg.codfilial=? ");
			
			PreparedStatement ps = con.prepareStatement( sql.toString() );

			int iparam = 1;

			ps.setInt( iparam++, Aplicativo.iCodEmp );
			ps.setInt( iparam++, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			
			ResultSet rs = ps.executeQuery();
		
			if (rs.next()) {
				periodoConsulta = rs.getString( "periodoConsCH" );
			}

			rs.close();
			ps.close();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		
		return periodoConsulta;
	}

	public void mouseEntered( MouseEvent e ) {

	}

	public void mouseExited( MouseEvent e ) {

	}

	public void mousePressed( MouseEvent e ) {

	}

	public void mouseReleased( MouseEvent e ) {

	}

	public void keyPressed( KeyEvent e ) {

		if ( e.getSource() == btBuscar && e.getKeyCode() == KeyEvent.VK_ENTER ) {
			btBuscar.doClick();
		}
		
		
	}

	public void keyReleased( KeyEvent e ) {

	}

	public void keyTyped( KeyEvent e ) {

	}

	public void beforeCarrega( CarregaEvent e ) {

	}

	public void afterCarrega( CarregaEvent e ) {
	
	}
	
	private void imprimir(TYPE_PRINT bVisualizar) {
	
		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();
	
		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "VDVENDA" ) );
		hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
		hParam.put( "FILTROS", " " );
	
		ResultSet rs = queryCheques();
		
		dlGr = new FPrinterJob( "layout/rel/REL_CHEQUES_01.jasper", "Relatório de Cheques ", "filtros", rs, hParam, this );
	
		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão do relatório!" + err.getMessage(), true, con, err );
			}
		}
	}
	
/*	public void firstFocus() {
		txtCodBanco.requestFocus();
	}
*/
	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcBanco.setConexao( con );
		lcConta.setConexao( con );
		lcFor.setConexao( con );
		
		String periodoConsulta = buscaPrefereFNC();
		//Se período for de um ano modifica valor do perido para 1, igual a constante Calendar.YEAR
		if ("A".equals(periodoConsulta)) 
			periodoCons = 1;
			
		inserePeriodo();
		
	/*	txtCodBanco.setFocusable( true );
		txtCodBanco.setRequerido( true );
		//setFirstFocus( txtCodBanco );
		
		firstFocus();*/
	
//		firstFocus();
	}

	public void focusGained( FocusEvent e ) {
	}

	public void focusLost( FocusEvent e ) {
	}

}
