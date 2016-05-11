/**
 * @version 01/08/2010 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.fnc.view.frame.utility <BR>
 *         Classe:
 *         
 * @(#)FPagCheque.java <BR>
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
 *                    Tela de emissão de cheques para pagamento de fornecedores.
 * 
 */

package org.freedom.modulos.fnc.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.apache.commons.lang.StringUtils;
import org.freedom.acao.TabelaEditEvent;
import org.freedom.acao.TabelaEditListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.functions.ConversionFunctions;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.library.type.StringDireita;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.fnc.business.object.Cheque;
import org.freedom.modulos.fnc.view.dialog.utility.DLEditaPag;
import org.freedom.modulos.fnc.view.dialog.utility.DLEditaPag.EDIT_PAG_SETVALORES;
import org.freedom.modulos.fnc.view.frame.crud.plain.FTalaoCheq;
import org.freedom.modulos.fnc.view.frame.crud.tabbed.FConta;
import org.freedom.modulos.fnc.view.frame.utility.FManutPag.enum_tab_manut;

public class FPagCheque extends FFilho implements ActionListener, TabelaEditListener, KeyListener {

	private static final long serialVersionUID = 1L;

	// private static final String HISTORICO_PADRAO = "PAGAMENTO REF. A COMPRA: <DOCUMENTO>";

	private ImageIcon imgApag = Icone.novo( "clVencido.gif" );

	private ImageIcon imgSelecionadoPag = Icone.novo( "clPago.gif" );

	private ImageIcon imgCheq = Icone.novo( "clVencido.gif" );

	private ImageIcon imgSelecionadoCheq = Icone.novo( "clPago.gif" );

	private JButtonPad btSelTudoPag = new JButtonPad( Icone.novo( "btTudo.png" ) );

	private JButtonPad btSelNadaPag = new JButtonPad( Icone.novo( "btNada.png" ) );

	private JButtonPad btGerarPag = new JButtonPad( Icone.novo( "btGerar.png" ) );	

	private JButtonPad btSelTudoCheq = new JButtonPad( Icone.novo( "btTudo.png" ) );

	private JButtonPad btSelNadaCheq = new JButtonPad( Icone.novo( "btNada.png" ) );

	private JButtonPad btImpCheq = new JButtonPad( Icone.novo( "btImprime.png" ) );	

	private JButtonPad btPrevCheq = new JButtonPad( Icone.novo( "btPrevimp.png" ) );	

	private JPanelPad pnLegenda = new JPanelPad( JPanelPad.TP_JPANEL );

	private JLabelPad lbVlrapag = new JLabelPad( "Total a pagar", imgApag, SwingConstants.LEFT );

	private JLabelPad lbSelecionadoPag = new JLabelPad( "Total selecionado", imgSelecionadoPag, SwingConstants.LEFT );

	private JLabelPad lbVlrcheq = new JLabelPad( "Total cheques", imgCheq, SwingConstants.LEFT );

	private JLabelPad lbSelecionadoCheq = new JLabelPad( "Total selecionado", imgSelecionadoCheq, SwingConstants.LEFT );

	private JPanelPad pnRod = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTabbedPanePad tpn = new JTabbedPanePad();

	// Aba pagar

	private JPanelPad pnTabPagar = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinBotoesPagar = new JPanelPad( 40, 210 );

	private JPanelPad pinPagar = new JPanelPad( 500, 60 );

	private JPanelPad pnPagar = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTablePad tabPagar = new JTablePad();

	private JScrollPane spnPagar = new JScrollPane( tabPagar );

	private JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtVlrTotApag = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtVlrTotSelPag = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtDatainiPagar = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafimPagar = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldFK txtRazFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCNPJFor = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0);

	private JButtonPad btExecpagar = new JButtonPad( Icone.novo( "btExecuta.png" ) );

	// Aba cheques

	private JPanelPad pnTabCheq = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinBotoesCheq = new JPanelPad( 40, 210 );

	private JPanelPad pinCheq = new JPanelPad( 500, 60 );

	private JPanelPad pnCheq = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTablePad tabCheq = new JTablePad();

	private JScrollPane spnCheq = new JScrollPane( tabCheq );

	private JTextFieldPad txtVlrTotCheq = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtVlrTotSelCheq = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtDatainiCheq = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafimCheq = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JButtonPad btExeccheq = new JButtonPad( Icone.novo( "btExecuta.png" ) );

	// Fim aba cheques

	private JButtonPad btSair = new JButtonPad( "Sair", Icone.novo( "btSair.png" ) );

	private ListaCampos lcFor = new ListaCampos( this );

	private String layoutCheq = null; 

	private Date dIniPagar = null;

	private Date dFimPagar = null;

	private Date dIniCheq = null;

	private Date dFimCheq = null;

	private boolean carregandoTabela = false; 

	private static String LABEL_SEL = "Sel.";

	//	private enum COLS_PAG {SEL, DTEMIT, DTVENCTO, STATUS, CODCOMPRA, CODPAG, NPARC, DOCLANCA, DOCCOMPRA, 
	//		VLRAPAG, NUMCONTA, CODTIPOCOB, DESCTIPOCOB, HISTPAG};

	private enum COLS_PAG { SEL, STATUSITPAG, DTVENCITPAG, CODFOR, RAZFOR, OBSITPAG, CODPAG, CHEQUES, NPARCPAG, DOC, DOCCOMPRA, VLRPARCITPAG, 
		DTPAGOITPAG, VLRPAGOITPAG, VLRDESCITPAG, VLRJUROSITPAG, VLRDEVITPAG, VLRADICITPAG, VLRAPAGITPAG, 
		VLRCANCITPAG, NUMCONTA, DESCPLAN, DESCCC, CODTIPOCOB, DESCTIPOCOB, CODCOMPRA, CODPLAN, CODCC, DTITPAG   };


		private enum SQL_PARAMS_PAG {NONE, DATAINI, DATAFIM, CODEMP, CODFILIAL, CODEMPFR, CODFILIALFR, CODFOR };

		private enum SQL_PARAMS_CHEQ {NONE, CODEMP, CODFILIAL, DATAINI, DATAFIM, NUMCONTA };

		private JTextFieldPad txtNumconta = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

		private JTextFieldFK txtDescconta = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

		private JTextFieldPad txtCodbanco = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

		private JTextFieldPad txtSeqtalao = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 6, 0 );	

		private ListaCampos lcConta = new ListaCampos( this, "" );

		private ListaCampos lcTalaoCheq = new ListaCampos( this, "" );

		private Map<String, String> prefs = new HashMap<String, String>();

		private int cheqatualtalao = 0;

		public FPagCheque() {

			super( false );
			setTitulo( "Emissão de cheques" );
			setAtribos( 20, 20, 820, 480 );

			lcConta.add( new GuardaCampo( txtNumconta, "Numconta", "Número conta", ListaCampos.DB_PK, txtDescconta, false ) );
			lcConta.add( new GuardaCampo( txtDescconta, "Descconta", "Descriçao do conta", ListaCampos.DB_SI, null, false ) );
			lcConta.add( new GuardaCampo( txtCodbanco, "Codbanco", "Cód.banco", ListaCampos.DB_SI, null, false ) );
			lcConta.montaSql( false, "CONTA", "FN" );
			lcConta.setQueryCommit( false );
			lcConta.setReadOnly( true );
			txtNumconta.setTabelaExterna( lcConta, FConta.class.getCanonicalName() );
			txtNumconta.setFK( true );
			txtNumconta.setNomeCampo( "NUMCONTA" );
			txtNumconta.setRequerido( true );

			lcTalaoCheq.add( new GuardaCampo( txtNumconta, "Numconta", "Número conta", ListaCampos.DB_PF, txtDescconta, false ) );
			lcTalaoCheq.add( new GuardaCampo( txtSeqtalao, "Seqtalao", "Seq.", ListaCampos.DB_PK, txtDescconta, false ) );
			lcTalaoCheq.setDinWhereAdic( "NUMCONTA = #S", txtNumconta );
			lcTalaoCheq.montaSql( false, "TALAOCHEQ", "FN" );
			lcTalaoCheq.setQueryCommit( false );
			lcTalaoCheq.setReadOnly( true );
			txtSeqtalao.setTabelaExterna( lcTalaoCheq, FTalaoCheq.class.getCanonicalName() );
			txtSeqtalao.setFK( true );
			txtSeqtalao.setNomeCampo( "SEQTALAO" );
			txtSeqtalao.setRequerido( true );

			Container c = getContentPane();
			c.setLayout( new BorderLayout() );
			c.add( pnRod, BorderLayout.SOUTH );
			c.add( tpn, BorderLayout.CENTER );
			btSair.setPreferredSize( new Dimension( 90, 30 ) );

			pnLegenda.setPreferredSize( new Dimension( 700, 50 ) );
			pnLegenda.setLayout( null );

			lbVlrapag.setBounds( 5, 0, 130, 17 );
			txtVlrTotApag.setBounds( 5, 18, 130, 18 );
			lbSelecionadoPag.setBounds( 140, 0, 130, 17 );
			txtVlrTotSelPag.setBounds( 140, 18, 130, 18 );

			lbVlrcheq.setBounds( 275, 0, 130, 17 );
			txtVlrTotCheq.setBounds( 275, 18, 130, 18 );
			lbSelecionadoCheq.setBounds( 410, 0, 130, 17 );
			txtVlrTotSelCheq.setBounds( 410, 18, 130, 18 );

			pnLegenda.add( lbVlrapag );
			pnLegenda.add( txtVlrTotApag );
			pnLegenda.add( lbSelecionadoPag );
			pnLegenda.add( txtVlrTotSelPag );

			pnLegenda.add( lbVlrcheq );
			pnLegenda.add( txtVlrTotCheq );
			pnLegenda.add( lbSelecionadoCheq );
			pnLegenda.add( txtVlrTotSelCheq );

			txtVlrTotApag.setSoLeitura( true );
			txtVlrTotSelPag.setSoLeitura( true );
			txtVlrTotCheq.setSoLeitura( true );
			txtVlrTotSelCheq.setSoLeitura( true );

			pnRod.setBorder( BorderFactory.createEtchedBorder() );
			pnRod.setPreferredSize( new Dimension( 600, 42 ) );

			pnRod.add( btSair, BorderLayout.EAST );
			pnRod.add( pnLegenda, BorderLayout.WEST );

			btSair.addActionListener( this );

			// Pagar

			tpn.addTab( "Pagar", pnPagar );

			btSelTudoPag.setToolTipText( "Marcar todos" );
			btSelNadaPag.setToolTipText( "Demarcar todos" );
			btGerarPag.setToolTipText( "Emitir cheque" );
			btExecpagar.setToolTipText( "Listar pagamentos" );

			pnPagar.add( pinPagar, BorderLayout.NORTH );
			pnTabPagar.add( pinBotoesPagar, BorderLayout.EAST );
			pnTabPagar.add( spnPagar, BorderLayout.CENTER );
			pnPagar.add( pnTabPagar, BorderLayout.CENTER );

			txtDatainiPagar.setVlrDate( new Date() );
			txtDatafimPagar.setVlrDate( new Date() );

			pinPagar.adic( new JLabelPad( "Cód.forn." ), 7, 0, 80, 20 );
			pinPagar.adic( txtCodFor, 7, 20, 80, 20 );
			pinPagar.adic( new JLabelPad( "Razão social do fornecedor" ), 90, 0, 250, 20 );
			pinPagar.adic( txtRazFor, 90, 20, 250, 20 );

			pinPagar.adic( new JLabelPad( "Período" ), 343, 0, 100, 20 );
			pinPagar.adic( txtDatainiPagar, 343, 20, 100, 20 );
			pinPagar.adic( new JLabelPad( "até" ), 446, 0, 100, 20 );
			pinPagar.adic( txtDatafimPagar, 446, 20, 100, 20 );
			pinPagar.adic( btExecpagar, 556, 10, 30, 30 );

			lcFor.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.for.", ListaCampos.DB_PK, false ) );
			lcFor.add( new GuardaCampo( txtRazFor, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );
			lcFor.add( new GuardaCampo( txtCNPJFor, "CnpjFor", "CNPJ", ListaCampos.DB_SI, false ) );

			lcFor.montaSql( false, "FORNECED", "CP" );
			lcFor.setQueryCommit( false );
			lcFor.setReadOnly( true );

			txtCodFor.setTabelaExterna( lcFor, null );
			txtCodFor.setFK( true );
			txtCodFor.setNomeCampo( "CodFor" );

			txtCodFor.setRequerido( true );
			txtDatainiPagar.setRequerido( true );
			txtDatafimPagar.setRequerido( true );
			//txtCodFor.requestFocus();

			pinBotoesPagar.adic( btSelTudoPag, 5, 10, 30, 30 );
			pinBotoesPagar.adic( btSelNadaPag, 5, 40, 30, 30 );
			pinBotoesPagar.adic( btGerarPag, 5, 70, 30, 30 );

			tabPagar.adicColuna( LABEL_SEL ); 

			tabPagar.adicColuna( "St" ); 							// STATUSITPAG
			tabPagar.adicColuna( "Vencto." ); 						// DTVENCITPAG			
			tabPagar.adicColuna( "Cd.for." ); 						// CODFOR
			tabPagar.adicColuna( "Razão social do fornecedor" ); 	// RAZFOR
			tabPagar.adicColuna( "Observações" ); 					// OBSITPAG
			tabPagar.adicColuna( "Cód.pg." ); 						// CODPAG
			tabPagar.adicColuna( "Cheques" ); 						// CODPAG
			tabPagar.adicColuna( "Parc." ); 						// NPARCPAG
			tabPagar.adicColuna( "Doc.lanc" ); 						// DOC
			tabPagar.adicColuna( "Compra" ); 						// DOCCOMPRA
			tabPagar.adicColuna( "Valor parc." ); 					// VLRPARCITPAG
			tabPagar.adicColuna( "Dt.pagto." ); 					// DTPAGOITPAG
			tabPagar.adicColuna( "Valor pago" ); 					// VLRPAGOITPAG
			tabPagar.adicColuna( "Valor desc." ); 					// VLRDESCITPAG
			tabPagar.adicColuna( "Valor juros" );					// VLRJUROSITPAG
			tabPagar.adicColuna( "Valor devolução" ); 				// VLRDEVITPAG
			tabPagar.adicColuna( "Valor adic" ); 					// VLRADICITPAG
			tabPagar.adicColuna( "Valor aberto" ); 					// VLRAPAGITPAG
			tabPagar.adicColuna( "Valor cancelado" ); 				// VLRCANCITPAG
			tabPagar.adicColuna( "Conta" ); 						// NUMCONTA
			tabPagar.adicColuna( "Categoria" ); 					// DESCPLAN
			tabPagar.adicColuna( "Centro de custo" ); 				// DESCC
			tabPagar.adicColuna( "Tp.Cob." ); 						// CODTIPOCOB
			tabPagar.adicColuna( "Descrição do tipo de cobrança" ); // DESCTIPOCOB		
			tabPagar.adicColuna( "Cod.Compra" ); 					// CODCOMPRA
			tabPagar.adicColuna( "Cod.Plan" ); 						// CODPLAN
			tabPagar.adicColuna( "Cod.CC" ); 						// CODCC
			tabPagar.adicColuna( "Dt.It.Pag" ); 					// DTITPAG

			tabPagar.setTamColuna( 20,	COLS_PAG.SEL.ordinal()  );
			tabPagar.setTamColuna( 20,	COLS_PAG.STATUSITPAG.ordinal()  );					
			tabPagar.setTamColuna( 60, 	COLS_PAG.DTVENCITPAG.ordinal() );				
			tabPagar.setTamColuna( 45, 	COLS_PAG.CODFOR.ordinal() );
			tabPagar.setTamColuna( 150, COLS_PAG.RAZFOR.ordinal() );
			tabPagar.setTamColuna( 150, COLS_PAG.OBSITPAG.ordinal() );
			tabPagar.setTamColuna( 50, 	COLS_PAG.CODPAG.ordinal() );
			tabPagar.setTamColuna( 55, 	COLS_PAG.CHEQUES.ordinal() );		
			tabPagar.setTamColuna( 35,	COLS_PAG.NPARCPAG.ordinal() );
			tabPagar.setTamColuna( 50, 	COLS_PAG.DOC.ordinal() );
			tabPagar.setTamColuna( 50, 	COLS_PAG.DOCCOMPRA.ordinal() ); 
			tabPagar.setTamColuna( 80, COLS_PAG.VLRPARCITPAG.ordinal() );
			tabPagar.setTamColuna( 60, COLS_PAG.DTPAGOITPAG.ordinal() );
			tabPagar.setTamColuna( 60, COLS_PAG.VLRPAGOITPAG.ordinal() );
			tabPagar.setTamColuna( 60, COLS_PAG.VLRDESCITPAG.ordinal() );
			tabPagar.setTamColuna( 60, COLS_PAG.VLRJUROSITPAG.ordinal() );
			tabPagar.setTamColuna( 60, COLS_PAG.VLRDEVITPAG.ordinal() );
			tabPagar.setTamColuna( 60, COLS_PAG.VLRADICITPAG.ordinal() );
			tabPagar.setTamColuna( 60, COLS_PAG.VLRAPAGITPAG.ordinal() );
			tabPagar.setTamColuna( 60, COLS_PAG.VLRCANCITPAG.ordinal() );
			tabPagar.setTamColuna( 60, COLS_PAG.NUMCONTA.ordinal() );
			tabPagar.setTamColuna( 150, COLS_PAG.DESCPLAN.ordinal() );
			tabPagar.setTamColuna( 150, COLS_PAG.DESCCC.ordinal() );
			tabPagar.setTamColuna( 60, 	COLS_PAG.CODTIPOCOB.ordinal() );
			tabPagar.setTamColuna( 150, COLS_PAG.DESCTIPOCOB.ordinal() );		
			tabPagar.setTamColuna( 50, COLS_PAG.CODCOMPRA.ordinal() );
			tabPagar.setTamColuna( 150, COLS_PAG.CODPLAN.ordinal() );
			tabPagar.setTamColuna( 150, COLS_PAG.CODCC.ordinal() );
			tabPagar.setTamColuna( 60, COLS_PAG.DTITPAG.ordinal() );
			
			tabPagar.setColunaEditavel( COLS_PAG.SEL.ordinal(), true );

			tabPagar.addTabelaEditListener( this );
			
			tabPagar.addMouseListener( new MouseAdapter() {
				public void mouseClicked( MouseEvent mevt ) {
					if ( mevt.getSource() == tabPagar && mevt.getClickCount() == 2 )
						editar();
				}
			} );

			tabPagar.addKeyListener( this );
			btExecpagar.addActionListener( this );
			btSelTudoPag.addActionListener( this );
			btSelNadaPag.addActionListener( this );
			btGerarPag.addActionListener( this );

			//btExecpagar.setFocusable( false );

			// Aba cheques

			tpn.addTab( "Cheques", pnCheq );
			btSelTudoCheq.setToolTipText( "Marcar todos" );
			btSelNadaCheq.setToolTipText( "Demarcar todos" );
			btImpCheq.setToolTipText( "Imprimir cheques" );
			btPrevCheq.setToolTipText( "Previsão da impressão de cheques" );
			btExeccheq.setToolTipText( "Listar cheques" );

			pnCheq.add( pinCheq, BorderLayout.NORTH );
			pnTabCheq.add( pinBotoesCheq, BorderLayout.EAST );
			pnTabCheq.add( spnCheq, BorderLayout.CENTER );
			pnCheq.add( pnTabCheq, BorderLayout.CENTER );
			txtDatainiCheq.setVlrDate( new Date() );
			txtDatafimCheq.setVlrDate( new Date() );
			pinCheq.adic( new JLabelPad( "Período" ), 7, 0, 100, 20 );
			pinCheq.adic( txtDatainiCheq, 7, 20, 100, 20 );
			pinCheq.adic( new JLabelPad( "até" ), 110, 0, 100, 20 );
			pinCheq.adic( txtDatafimCheq, 110, 20, 100, 20 );

			pinCheq.adic( new JLabelPad( "Nº conta" ), 223, 0, 80, 20 );
			pinCheq.adic( txtNumconta, 223, 20, 80, 20 );
			pinCheq.adic( new JLabelPad( "Descricção da conta" ), 306, 0, 230, 20 );
			pinCheq.adic( txtDescconta, 306, 20, 230, 20 );
			pinCheq.adic( new JLabelPad( "Seq.talão" ), 539, 0, 60, 20 );
			pinCheq.adic( txtSeqtalao, 539, 20, 60, 20 );

			pinCheq.adic( btExeccheq, 612, 10, 30, 30 );

			txtDatainiCheq.setRequerido( true );
			txtDatafimCheq.setRequerido( true );

			pinBotoesCheq.adic( btSelTudoCheq, 5, 10, 30, 30 );
			pinBotoesCheq.adic( btSelNadaCheq, 5, 40, 30, 30 );
			pinBotoesCheq.adic( btImpCheq, 5, 70, 30, 30 );
			pinBotoesCheq.adic( btPrevCheq, 5, 100, 30, 30 );

			//		private enum Cheque.COLS_CHEQ { SEL, DTEMIT, DTVENCTO, NOMEFAVCHEQ, SITCHEQ, VLRCHEQ, NUMCONTA };

			tabCheq.adicColuna( LABEL_SEL ); 
			tabCheq.adicColuna( "Seq." ); 
			tabCheq.adicColuna( "Emissão" ); 
			tabCheq.adicColuna( "Vencto." ); 
			tabCheq.adicColuna( "Número" ); 
			tabCheq.adicColuna( "Cod.for." ); 						
			tabCheq.adicColuna( "Nome favorecido" ); 
			tabCheq.adicColuna( "Sit." ); 
			tabCheq.adicColuna( "Valor" ); 
			tabCheq.adicColuna( "Nº conta" );  
			tabCheq.adicColuna( "Histórico" ); 

			tabCheq.setColunaEditavel( Cheque.COLS_CHEQ.SEL.ordinal(), true );
			tabCheq.setColunaEditavel( Cheque.COLS_CHEQ.VLRCHEQ.ordinal(), true );

			tabCheq.setTamColuna( 20, Cheque.COLS_CHEQ.SEL.ordinal() );
			tabCheq.setTamColuna( 70, Cheque.COLS_CHEQ.SEQ.ordinal() );
			tabCheq.setTamColuna( 90, Cheque.COLS_CHEQ.DTEMIT.ordinal() );
			tabCheq.setTamColuna( 90, Cheque.COLS_CHEQ.DTVENCTO.ordinal() );
			tabCheq.setTamColuna( 40, Cheque.COLS_CHEQ.NUMCHEQ.ordinal() );
			tabCheq.setTamColuna( 70, Cheque.COLS_CHEQ.CODFOR.ordinal() );
			tabCheq.setTamColuna( 200, Cheque.COLS_CHEQ.NOMEFAVCHEQ.ordinal() );
			tabCheq.setTamColuna( 20, Cheque.COLS_CHEQ.SITCHEQ.ordinal() );
			tabCheq.setTamColuna( 90, Cheque.COLS_CHEQ.VLRCHEQ.ordinal() );
			tabCheq.setTamColuna( 80, Cheque.COLS_CHEQ.NUMCONTA.ordinal() );
			tabCheq.setTamColuna( 250, Cheque.COLS_CHEQ.HISTCHEQ.ordinal() );

			tabCheq.addTabelaEditListener( this );
			tabCheq.addKeyListener( this );
			btExeccheq.addActionListener( this );
			btSelTudoCheq.addActionListener( this );
			btSelNadaCheq.addActionListener( this );
			btImpCheq.addActionListener( this );
			btPrevCheq.addActionListener( this );

			//pinPagar.setFirstFocus( txtCodFor );
			txtCodFor.requestFocus();
			txtCodFor.addKeyListener( this );
			txtDatafimPagar.addKeyListener( this );
			txtSeqtalao.addKeyListener( this );
			btExeccheq.setFocusable( false );

		}

		private synchronized void marcarTodosPag() {
			try {
				carregandoTabela = true; // Evitar execução circular
				for ( int i=0; i < tabPagar.getNumLinhas(); i++ ) {
					tabPagar.setValor( new Boolean( true ), i, COLS_PAG.SEL.ordinal() );
				}
			}
			finally {
				carregandoTabela = false;
			}
			calcTotaisPag();
		}

		private synchronized void desmarcarTodosPag() {
			try {
				carregandoTabela = true;
				for ( int i=0; i < tabPagar.getNumLinhas(); i++ ) {
					tabPagar.setValor( new Boolean( false ), i, COLS_PAG.SEL.ordinal() );
				}
			} finally {
				carregandoTabela = false;
			}
			calcTotaisPag();
		}

		private synchronized void marcarTodosCheq() {
			try {
				carregandoTabela = true; // Evitar execução circular
				for ( int i=0; i < tabCheq.getNumLinhas(); i++ ) {
					tabCheq.setValor( new Boolean( true ), i, Cheque.COLS_CHEQ.SEL.ordinal() );
				}
			} finally {
				carregandoTabela = false;
			}
			calcTotaisCheq();
		}

		private synchronized void desmarcarTodosCheq() {
			try {
				carregandoTabela = true;
				for ( int i=0; i < tabCheq.getNumLinhas(); i++ ) {
					tabCheq.setValor( new Boolean( false ), i, Cheque.COLS_CHEQ.SEL.ordinal() );
				}
			} finally {
				carregandoTabela = false;
			}
			calcTotaisCheq();
		}

		private synchronized void calcTotaisPag() {
			BigDecimal vlrtotapag = new BigDecimal(0);
			BigDecimal vlrtotsel = new BigDecimal(0);
			BigDecimal vlrapag = new BigDecimal(0);
			//Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( "VLRAPAGITPAG" ) )
			for ( int i=0; i < tabPagar.getNumLinhas(); i++ ) {
				vlrapag = ConversionFunctions.stringCurrencyToBigDecimal( 
						(String) tabPagar.getValor( i, COLS_PAG.VLRAPAGITPAG.ordinal() ).toString() );
				vlrtotapag = vlrtotapag.add( vlrapag  );
				if ( (Boolean) tabPagar.getValor( i, COLS_PAG.SEL.ordinal() ) ) {
					vlrtotsel = vlrtotsel.add( vlrapag );
				}
			}
			txtVlrTotApag.setVlrBigDecimal( vlrtotapag );
			txtVlrTotSelPag.setVlrBigDecimal( vlrtotsel );
		}

		private synchronized void calcTotaisCheq() {
			BigDecimal vlrtotcheq = new BigDecimal(0);
			BigDecimal vlrtotsel = new BigDecimal(0);
			BigDecimal vlrcheq = new BigDecimal(0);
			//Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( "VLRAPAGITPAG" ) )
			for ( int i=0; i < tabCheq.getNumLinhas(); i++ ) {
				vlrcheq = ConversionFunctions.stringCurrencyToBigDecimal( 
						(String) tabCheq.getValor( i, Cheque.COLS_CHEQ.VLRCHEQ.ordinal() ) );
				vlrtotcheq = vlrtotcheq.add( vlrcheq  );
				if ( (Boolean) tabCheq.getValor( i, Cheque.COLS_CHEQ.SEL.ordinal() ) ) {
					vlrtotsel = vlrtotsel.add( vlrcheq );
				} 
			}
			txtVlrTotCheq.setVlrBigDecimal( vlrtotcheq );
			txtVlrTotSelCheq.setVlrBigDecimal( vlrtotsel );
		}

		private synchronized void carregaTabPagar() {

			PreparedStatement ps = null;
			ResultSet rs = null;
			StringBuffer sql = new StringBuffer();

			BigDecimal vlrtotapag = new BigDecimal(0);
			BigDecimal vlrtotsel = new BigDecimal(0);

			try {

				carregandoTabela = true;
				if ( validaFiltrosPag() ) {

					tabPagar.limpa();
					sql.append( "select ");

					sql.append( "it.dtitpag, it.dtvencitpag, it.statusitpag, p.codfor, f.razfor, p.codpag, it.nparcpag, " );
					sql.append( "it.doclancaitpag, p.codcompra, it.vlrparcitpag, it.dtpagoitpag, it.vlrpagoitpag, " );
					sql.append( "it.vlrapagitpag, it.vlrdescitpag, it.vlrdevitpag, it.vlradicitpag, it.vlrjurositpag, " );
					sql.append( "it.codplan, it.codcc, it.dtitpag, p.docpag, " );

					sql.append( "coalesce(it.numconta,'') numconta, ");
					sql.append( "coalesce(it.codtipocob,'') codtipocob, ");
					sql.append( "coalesce(it.vlrcancitpag,0) vlrcancitpag, " );

					sql.append( "coalesce(( select pl.descplan from fnplanejamento pl " );
					sql.append( "where PL.CODPLAN=IT.CODPLAN AND PL.CODEMP=IT.CODEMPPN AND PL.CODFILIAL=IT.CODFILIALPN),'') descplan, ");

					sql.append( "coalesce(( select cc.desccc from fncc cc " );
					sql.append( "where CC.CODCC=IT.CODCC AND CC.CODEMP=IT.CODEMPCC AND CC.CODFILIAL=IT.CODFILIALCC AND CC.ANOCC=IT.ANOCC),'') DESCCC," );

					sql.append( "coalesce(IT.OBSITPAG,'') obsitpag, " );

					sql.append( "coalesce((select co.doccompra from cpcompra co " );				
					sql.append( "where co.codcompra=p.codcompra and co.codemp=p.codempcp and co.codfilial=p.codfilialcp),'') doccompra," );

					sql.append( "coalesce((SELECT T.DESCTIPOCOB FROM FNTIPOCOB T " );
					sql.append( "WHERE IT.CODEMPTC=T.CODEMP AND IT.CODFILIALTC=T.CODFILIAL AND IT.CODTIPOCOB=T.CODTIPOCOB),'') DESCTIPOCOB " );

					sql.append( "from fnitpagar it, fnpagar p, cpforneced f " );

					sql.append( "WHERE P.CODPAG=IT.CODPAG AND F.CODFOR=P.CODFOR AND F.CODEMP=P.CODEMPFR AND F.CODFILIAL=P.CODFILIALFR " );
					
					sql.append( " AND IT.CODEMP=P.CODEMP AND IT.CODFILIAL=P.CODFILIAL " );
					
					
					sql.append( "and IT.STATUSITPAG='P1' AND ");
					sql.append( "IT.DTITPAG BETWEEN ? AND ? AND " );				
					sql.append( "P.CODEMP=? AND P.CODFILIAL=? AND " );
					sql.append( "P.CODEMPFR=? AND P.CODFILIALFR=? AND P.CODFOR=? AND " );
					sql.append( "NOT EXISTS (SELECT * FROM FNPAGCHEQ PC " );
					sql.append( "WHERE PC.CODEMP=IT.CODEMP AND PC.CODFILIAL=IT.CODFILIAL AND ");
					sql.append( "PC.CODPAG=IT.CODPAG AND PC.NPARCPAG=IT.NPARCPAG ) " );
					sql.append( "ORDER BY IT.DTITPAG, IT.CODPAG, IT.NPARCPAG" );
					
					try {

						ps = con.prepareStatement( sql.toString() );
						ps.setDate( SQL_PARAMS_PAG.DATAINI.ordinal(), Funcoes.dateToSQLDate( dIniPagar ) );
						ps.setDate( SQL_PARAMS_PAG.DATAFIM.ordinal(), Funcoes.dateToSQLDate( dFimPagar ) );
						ps.setInt( SQL_PARAMS_PAG.CODEMP.ordinal(), Aplicativo.iCodEmp );
						ps.setInt( SQL_PARAMS_PAG.CODFILIAL.ordinal(), ListaCampos.getMasterFilial( "FNPAGAR" ) );
						ps.setInt( SQL_PARAMS_PAG.CODEMPFR.ordinal(), Aplicativo.iCodEmp );
						ps.setInt( SQL_PARAMS_PAG.CODFILIALFR.ordinal(), ListaCampos.getMasterFilial( "CPFORNECED" ) );
						ps.setInt( SQL_PARAMS_PAG.CODFOR.ordinal(), txtCodFor.getVlrInteger() );
						rs = ps.executeQuery();
						//					System.out.println( sSQL.toString() );

						for ( int i = 0; rs.next(); i++ ) {

							vlrtotapag  = vlrtotapag.add( rs.getBigDecimal( "VLRAPAGITPAG" )  ) ;
							vlrtotsel  = vlrtotsel.add( rs.getBigDecimal( "VLRAPAGITPAG" )  ) ;
							
							tabPagar.adicLinha();
							tabPagar.setValor( new Boolean(true), i, COLS_PAG.SEL.ordinal() );
							tabPagar.setValor( rs.getString( enum_tab_manut.STATUSITPAG.name() ),	i, enum_tab_manut.STATUSITPAG.ordinal() );
							tabPagar.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( COLS_PAG.DTVENCITPAG.name() ) ), i, COLS_PAG.DTVENCITPAG.ordinal() );
							tabPagar.setValor( rs.getString( COLS_PAG.STATUSITPAG.name() ),	i, COLS_PAG.STATUSITPAG.ordinal() );
							tabPagar.setValor( rs.getString( COLS_PAG.CODFOR.name() ),	 	i, COLS_PAG.CODFOR.ordinal() );
							tabPagar.setValor( rs.getString( COLS_PAG.RAZFOR.name() ), 		i, COLS_PAG.RAZFOR.ordinal() );
							tabPagar.setValor( rs.getInt( COLS_PAG.CODPAG.name() ), 		i, COLS_PAG.CODPAG.ordinal() );
							tabPagar.setValor( rs.getInt( COLS_PAG.NPARCPAG.name() ), 		i, COLS_PAG.NPARCPAG.ordinal() );

							String doclanca = rs.getString( "DocLancaItPag" );
							String docpag = rs.getString( "DocPag" );
							String doc = "";

							if( doclanca == null ) {
								if(docpag != null ) {
									doc = rs.getString( "DocPag" ) + "/" + rs.getString( "NParcPag" );
								}
							} else {
								doc = doclanca;
							}

							tabPagar.setValor( doc, i, COLS_PAG.DOC.ordinal() );

							tabPagar.setValor( rs.getString( COLS_PAG.DOCCOMPRA.name() ), i, COLS_PAG.DOCCOMPRA.ordinal() );

							tabPagar.setValor( new StringDireita( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( "VlrParcItPag" ) ) ), i, COLS_PAG.VLRPARCITPAG.ordinal() );
							tabPagar.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( COLS_PAG.DTPAGOITPAG.name() ) ), i, COLS_PAG.DTPAGOITPAG.ordinal() );
							tabPagar.setValor( new StringDireita( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( COLS_PAG.VLRPAGOITPAG.name()  ) )), i, 	COLS_PAG.VLRPAGOITPAG.ordinal() );
							tabPagar.setValor( new StringDireita( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( COLS_PAG.VLRDESCITPAG.name() ) )), i, 	COLS_PAG.VLRDESCITPAG.ordinal() );
							tabPagar.setValor( new StringDireita( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( COLS_PAG.VLRJUROSITPAG.name() ) )), i, 	COLS_PAG.VLRJUROSITPAG.ordinal() );
							tabPagar.setValor( new StringDireita( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( COLS_PAG.VLRDEVITPAG.name() ) )), i, 	COLS_PAG.VLRDEVITPAG.ordinal() );
							tabPagar.setValor( new StringDireita( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( COLS_PAG.VLRADICITPAG.name() ) )), i, 	COLS_PAG.VLRADICITPAG.ordinal() );
							tabPagar.setValor( new StringDireita( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( COLS_PAG.VLRAPAGITPAG.name() ) )), i, 	COLS_PAG.VLRAPAGITPAG.ordinal() );
							tabPagar.setValor( new StringDireita( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( COLS_PAG.VLRCANCITPAG.name() ) )), i, 	COLS_PAG.VLRCANCITPAG.ordinal() );

							tabPagar.setValor( rs.getString( COLS_PAG.NUMCONTA.name() 	) 	, i, COLS_PAG.NUMCONTA.ordinal() );
							tabPagar.setValor( rs.getString( COLS_PAG.DESCPLAN.name() 	) 	, i, COLS_PAG.DESCPLAN.ordinal() );
							tabPagar.setValor( rs.getString( COLS_PAG.DESCCC.name() 		) 	, i, COLS_PAG.DESCCC.ordinal() );
							tabPagar.setValor( rs.getString( COLS_PAG.CODTIPOCOB.name()	) 	, i, COLS_PAG.CODTIPOCOB.ordinal() );
							tabPagar.setValor( rs.getString( COLS_PAG.DESCTIPOCOB.name() 	) 	, i, COLS_PAG.DESCTIPOCOB.ordinal() );						
							tabPagar.setValor( rs.getString( COLS_PAG.OBSITPAG.name() 	) 	, i, COLS_PAG.OBSITPAG.ordinal() );

							tabPagar.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( COLS_PAG.DTITPAG.name()) ) 	, i, COLS_PAG.DTITPAG.ordinal() );
							tabPagar.setValor( rs.getString( COLS_PAG.CODCC.name() 		) 	, i, COLS_PAG.CODCC.ordinal() );
							tabPagar.setValor( rs.getString( COLS_PAG.CODPLAN.name() 		) 	, i, COLS_PAG.CODPLAN.ordinal() );
							tabPagar.setValor( rs.getString( COLS_PAG.CODCOMPRA.name() 	) 	, i, COLS_PAG.CODCOMPRA.ordinal() );
							tabPagar.setValor( rs.getString( COLS_PAG.NUMCONTA.name() 	) 	, i, COLS_PAG.NUMCONTA.ordinal() );
							
							
							/*
							
							
							tabPagar.setValor( new Boolean(true), i, COLS_PAG.SEL.ordinal() );
							tabPagar.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "DTITPAG" ) ), i, COLS_PAG.DTITPAG.ordinal() );
							tabPagar.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "DTVENCITPAG" ) ), i, COLS_PAG.DTVENCITPAG.ordinal() );
							tabPagar.setValor( rs.getString( "STATUSITPAG" ), i, COLS_PAG.STATUSITPAG.ordinal() );
							tabPagar.setValor( rs.getString( "CODCOMPRA" ), i, COLS_PAG.CODCOMPRA.ordinal() );
							tabPagar.setValor( new Integer(rs.getInt( "CODPAG" )), i, COLS_PAG.CODPAG.ordinal() );
							tabPagar.setValor( new Integer(rs.getInt( "NPARCPAG" )), i, COLS_PAG.NPARCPAG.ordinal() );
							tabPagar.setValor( ( rs.getString( "DOCLANCAITPAG" ) != null ? rs.getString( "DOCLANCAITPAG" ) : 
								( rs.getString( "DOCPAG" ) != null ? rs.getString( "DOCPAG" ) + "/" + rs.getString( "NPARCPAG" ) : "" ) ), i, COLS_PAG.DOC.ordinal() );
							tabPagar.setValor( rs.getString( "DOCCOMPRA" ), i, COLS_PAG.DOCCOMPRA.ordinal() );
							tabPagar.setValor( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs.getString( "VLRAPAGITPAG" ) ), i, COLS_PAG.VLRAPAGITPAG.ordinal() );
							tabPagar.setValor( rs.getString( "NUMCONTA" ), i, COLS_PAG.NUMCONTA.ordinal() );
							tabPagar.setValor( rs.getString( "CODTIPOCOB" ), i, COLS_PAG.CODTIPOCOB.ordinal() );
							tabPagar.setValor( rs.getString( "DESCTIPOCOB" ), i, COLS_PAG.DESCTIPOCOB.ordinal() );
							tabPagar.setValor( rs.getString( "OBSITPAG" ), i, COLS_PAG.OBSITPAG.ordinal() );
							*/
						}

						rs.close();
						ps.close();

						txtVlrTotApag.setVlrBigDecimal( vlrtotapag );
						txtVlrTotSelPag.setVlrBigDecimal( vlrtotsel );

						con.commit();
					} catch ( SQLException err ) {
						err.printStackTrace();
						Funcoes.mensagemErro( this, "Erro ao montar a tabela de pagamentos!\n" + err.getMessage(), true, con, err );
					}
				}
			} catch ( Exception e ) {
				e.printStackTrace();
			} finally {
				ps = null;
				rs = null;
				sql = null;
				carregandoTabela = false;
			}

		}

		private boolean validaFiltrosPag() {

			boolean bRetorno = false;

			if ( "".equals(txtCodFor.getText())) {
				txtCodFor.requestFocus();
			}

			if ( txtDatainiPagar.getText().trim().length() < 10 ) {
				Funcoes.mensagemInforma( this, "Data inicial inválida!" );
			} else if ( txtDatafimPagar.getText().trim().length() < 10 ) {
				Funcoes.mensagemInforma( this, "Data final inválida!" );
			} else if ( txtDatafimPagar.getVlrDate().before( txtDatainiPagar.getVlrDate() ) ) {
				Funcoes.mensagemInforma( this, "Data inicial maior que a data final!" );
			} else {
				dIniPagar = txtDatainiPagar.getVlrDate();
				dFimPagar = txtDatafimPagar.getVlrDate();
				bRetorno = true;
			}

			return bRetorno;
		}

		private boolean validaFiltrosCheq() {

			boolean bRetorno = false;
			String codbanco = txtCodbanco.getVlrString();

			if ( txtDatainiCheq.getText().trim().length() < 10 ) {
				Funcoes.mensagemInforma( this, "Data inicial inválida!" );
			} else if ( txtDatafimCheq.getText().trim().length() < 10 ) {
				Funcoes.mensagemInforma( this, "Data final inválida!" );
			} else if ( txtDatafimCheq.getVlrDate().before( txtDatainiCheq.getVlrDate() ) ) {
				Funcoes.mensagemInforma( this, "Data inicial maior que a data final!" );
			} else if ( "".equals( txtNumconta.getText().trim() ) ) {
				Funcoes.mensagemInforma( this, "Selecione uma conta!" );
			} else if ( "".equals( txtSeqtalao.getText().trim() ) ) {
				Funcoes.mensagemInforma( this, "Selecione uma talonário!" );
			} else {
				layoutCheq = Cheque.carregaLayoutCheque( codbanco, this );
				if ( (layoutCheq == null) || ("".equals( layoutCheq )) ) {
					Funcoes.mensagemInforma( this, 
							"Não existe layout de cheques configurado para o banco " + 
							codbanco + "!" );
				} else { 
					dIniCheq = txtDatainiCheq.getVlrDate();
					dFimCheq = txtDatafimCheq.getVlrDate();
					bRetorno = true;
				}
			}

			return bRetorno;
		}

		private int getCheqatualtalao( String numconta, int seqtalao ) throws SQLException {
			int result = 0;
			ResultSet rs = null;
			PreparedStatement ps = null;
			StringBuffer sql = new StringBuffer("SELECT T.CHEQATUALTALAO FROM FNTALAOCHEQ T ");
			sql.append( "WHERE T.CODEMP=? AND T.CODFILIAL=? AND T.NUMCONTA=? AND T.SEQTALAO=?" );
			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNTALAOCHEQ" ) );
			ps.setString( 3, numconta );
			ps.setInt( 4, seqtalao );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				result = rs.getInt( "CHEQATUALTALAO" );
			}
			rs.close();
			ps.close();
			return result;
		}

		private synchronized void carregaTabCheq() {

			PreparedStatement ps = null;
			ResultSet rs = null;
			StringBuffer sSQL = new StringBuffer();
			BigDecimal vlrtotcheq = new BigDecimal(0);
			BigDecimal vlrtotsel = new BigDecimal(0);
			int seqtalao = txtSeqtalao.getVlrInteger();
			String numconta = txtNumconta.getVlrString();
			int numcheqtab = 0;

			try {

				carregandoTabela = true;

				if ( validaFiltrosCheq() ) {

					tabCheq.limpa();
//					sSQL.append( "SELECT CH.SEQCHEQ, CH.DTEMITCHEQ, CH.DTVENCTOCHEQ, CH.NUMCHEQ, ");
//					sSQL.append( "CH.NOMEFAVCHEQ, CH.SITCHEQ, CH.VLRCHEQ, CH.CONTACHEQ, CH.HISTCHEQ ");
//					sSQL.append( "FROM FNCHEQUE CH ");
//					sSQL.append( "WHERE CH.CODEMP=? AND CH.CODFILIAL=? AND " );
//					sSQL.append( "CH.DTEMITCHEQ BETWEEN ? AND ? AND CH.TIPOCHEQ='PF' AND " );
//					sSQL.append( "CH.SITCHEQ='CA' AND CH.CONTACHEQ=? " );
//					sSQL.append( "ORDER BY CH.DTEMITCHEQ, CH.SEQCHEQ" );
					
					sSQL.append( "SELECT CH.SEQCHEQ, CH.DTEMITCHEQ, CH.DTVENCTOCHEQ, CH.NUMCHEQ, "); 
					sSQL.append( "CH.NOMEFAVCHEQ, CH.SITCHEQ, CH.VLRCHEQ, CH.CONTACHEQ, CH.HISTCHEQ, P.CODFOR "); 
					sSQL.append( "FROM FNCHEQUE CH "); 
					sSQL.append( "INNER JOIN FNPAGCHEQ PCH ON "); 
					sSQL.append( "(PCH.CODEMP = CH.CODEMP AND PCH.CODFILIAL = CH.CODFILIAL AND PCH.SEQCHEQ = CH.SEQCHEQ) "); 
					sSQL.append( "INNER JOIN FNITPAGAR IP ON "); 
					sSQL.append( "(IP.CODEMP = PCH.CODEMP AND IP.CODFILIAL = PCH.CODFILIAL AND IP.CODPAG = PCH.CODPAG AND IP.NPARCPAG = PCH.NPARCPAG) "); 
					sSQL.append( "INNER JOIN FNPAGAR P ON "); 
					sSQL.append( "(P.CODEMP = IP.CODEMP AND P.CODFILIAL = IP.CODFILIAL AND P.CODPAG = IP.CODPAG) "); 
//					sSQL.append( "INNER JOIN CPFORNECED F ON "); 
//					sSQL.append( "(F.CODEMP = P.CODEMP AND F.CODFILIAL = P.CODFILIAL AND F.CODFOR = P.CODFOR) " );
					sSQL.append( "WHERE CH.CODEMP=? AND CH.CODFILIAL=? AND " );
					sSQL.append( "CH.DTEMITCHEQ BETWEEN ? AND ? AND CH.TIPOCHEQ='PF' AND " );
					sSQL.append( "CH.SITCHEQ='CA' AND CH.CONTACHEQ=? " );
					sSQL.append( "ORDER BY CH.DTEMITCHEQ, CH.SEQCHEQ" );

					try {

						cheqatualtalao = getCheqatualtalao( numconta, seqtalao );
						numcheqtab = cheqatualtalao;
						ps = con.prepareStatement( sSQL.toString() );
						ps.setInt( SQL_PARAMS_CHEQ.CODEMP.ordinal(), Aplicativo.iCodEmp );
						ps.setInt( SQL_PARAMS_CHEQ.CODFILIAL.ordinal(), ListaCampos.getMasterFilial( "FNPAGAR" ) );
						ps.setDate( SQL_PARAMS_CHEQ.DATAINI.ordinal(), Funcoes.dateToSQLDate( dIniCheq ) );
						ps.setDate( SQL_PARAMS_CHEQ.DATAFIM.ordinal(), Funcoes.dateToSQLDate( dFimCheq ) );
						ps.setString( SQL_PARAMS_CHEQ.NUMCONTA.ordinal(), numconta );
						rs = ps.executeQuery();
						//					System.out.println( sSQL.toString() );

						for ( int i = 0; rs.next(); i++ ) {
							vlrtotcheq  = vlrtotcheq.add( rs.getBigDecimal( "VLRCHEQ" )  ) ;
							vlrtotsel  = vlrtotsel.add( rs.getBigDecimal( "VLRCHEQ" )  ) ;
							if ( rs.getInt( "NUMCHEQ" ) == 0 ) {
								numcheqtab ++;
							} else {
								numcheqtab = rs.getInt( "NUMCHEQ" );
							}
							tabCheq.adicLinha();
							tabCheq.setValor( new Boolean(true), i, Cheque.COLS_CHEQ.SEL.ordinal() );
							tabCheq.setValor( new Integer(rs.getInt( "SEQCHEQ" )), i, Cheque.COLS_CHEQ.SEQ.ordinal() );
							tabCheq.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "DTEMITCHEQ" ) ), i, Cheque.COLS_CHEQ.DTEMIT.ordinal() );
							tabCheq.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "DTVENCTOCHEQ" ) ), i, Cheque.COLS_CHEQ.DTVENCTO.ordinal() );
							tabCheq.setValor( new Integer( numcheqtab ), i, Cheque.COLS_CHEQ.NUMCHEQ.ordinal() );
							tabCheq.setValor( rs.getString( "CODFOR" ), i, Cheque.COLS_CHEQ.CODFOR.ordinal() );
							tabCheq.setValor( rs.getString( "NOMEFAVCHEQ" ), i, Cheque.COLS_CHEQ.NOMEFAVCHEQ.ordinal() );
							tabCheq.setValor( rs.getString( "SITCHEQ" ), i, Cheque.COLS_CHEQ.SITCHEQ.ordinal() );
							tabCheq.setValor( Funcoes.strDecimalToStrCurrency( Aplicativo.casasDecFin, rs.getString( "VLRCHEQ" ) ), i, Cheque.COLS_CHEQ.VLRCHEQ.ordinal() );
							tabCheq.setValor( rs.getString( "CONTACHEQ" ), i, Cheque.COLS_CHEQ.NUMCONTA.ordinal() );
							tabCheq.setValor( rs.getString( "HISTCHEQ" ), i, Cheque.COLS_CHEQ.HISTCHEQ.ordinal() );
						}

						rs.close();
						ps.close();

						txtVlrTotCheq.setVlrBigDecimal( vlrtotcheq );
						txtVlrTotSelCheq.setVlrBigDecimal( vlrtotsel );

						con.commit();
					} catch ( SQLException err ) {
						err.printStackTrace();
						Funcoes.mensagemErro( this, "Erro ao montar a tabela de cheques!\n" + err.getMessage(), true, con, err );
					}
				}
			} catch ( Exception e ) {
				e.printStackTrace();
			} finally {
				ps = null;
				rs = null;
				sSQL = null;
				carregandoTabela = false;
			}

		}

		public void actionPerformed( ActionEvent evt ) {

			if ( evt.getSource() == btExecpagar ) {
				carregaTabPagar();
			} else if ( evt.getSource() == btSair ) {
				dispose();
			} else if ( evt.getSource() == btSelTudoPag ) {
				marcarTodosPag();
			} else if ( evt.getSource() == btSelNadaPag ) {
				desmarcarTodosPag();
			} else if ( evt.getSource() == btGerarPag ) {
				gerar();		
			} else if ( evt.getSource() == btSelTudoCheq ) {
				marcarTodosCheq();
			} else if ( evt.getSource() == btSelNadaCheq ) {
				desmarcarTodosCheq();
			} else if ( evt.getSource() == btImpCheq ) {
				imprimir(TYPE_PRINT.PRINT);		
			} else if ( evt.getSource() == btPrevCheq ) {
				imprimir(TYPE_PRINT.VIEW);		
			} else if ( evt.getSource() == btExeccheq ) {
				carregaTabCheq();
			}
		}

		private synchronized void gerar() {
			LinkedList<Vector<Object>> listapagar = new LinkedList<Vector<Object>>();
			listapagar = getListapagar( listapagar );
			if (validaListapagar( listapagar ) ) {
				if ( Funcoes.mensagemConfirma( this, "Executar a geração do cheque?" )==JOptionPane.YES_OPTION) {
					gerarCheque( listapagar );
				}
			}
		}

		private synchronized void imprimir(TYPE_PRINT visualizar) {
			if ( validaValores() && validaImpressora() ) {
				LinkedList<Vector<Object>> listacheq = new LinkedList<Vector<Object>>();
				listacheq = getListacheq( listacheq );
				if (validaListacheq( listacheq ) ) {
					if ( (visualizar==TYPE_PRINT.VIEW ) || ( Funcoes.mensagemConfirma( this, "Imprimir cheques?" )==JOptionPane.YES_OPTION ) ) {
						imprimirCheque( listacheq, visualizar );
					}
				}
			}
		}

		private int execSqlInsertPagcheq(LinkedList<Vector<Object>> listapagar, int seqcheq) throws SQLException {
			StringBuffer sqlins = new StringBuffer();
			PreparedStatement ps = null;
			int count = 0;

			sqlins.append( "INSERT INTO FNPAGCHEQ ( CODEMP, CODFILIAL, CODPAG, NPARCPAG, " );
			sqlins.append( "CODEMPCH, CODFILIALCH, SEQCHEQ ) " );
			sqlins.append( "VALUES ( ?, ?, ?, ?, ?, ?, ?)" );

			for (int i=0; i<listapagar.size(); i++) {
				ps = con.prepareStatement( sqlins.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "FNITPAGAR" ));
				ps.setInt( 3, (Integer) ( (Vector<Object>) listapagar.get( i ) ).elementAt( COLS_PAG.CODPAG.ordinal() ) );
				ps.setInt( 4, (Integer) ( (Vector<Object>) listapagar.get( i ) ).elementAt( COLS_PAG.NPARCPAG.ordinal() ) );
				ps.setInt( 5, Aplicativo.iCodEmp );
				ps.setInt( 6, ListaCampos.getMasterFilial( "FNCHEQUE" ));
				ps.setInt( 7, seqcheq );
				
				count += ps.executeUpdate();
			}

			
			return count;
		}

		private int execSqlInsertCheque( int codfor, String numconta, int seqcheq, BigDecimal vlrcheque ) throws SQLException {
			StringBuffer sqlins = new StringBuffer();
			PreparedStatement ps = null;

			StringBuffer histcheq = new StringBuffer( "'CHEQUE FORNEC.: " );
			histcheq.append( codfor );
			histcheq.append( " - '|| " );
			// Não estão todos os campos da tabela cheque no comando INSERT, 
			// alguns valores são inseridos por padrão.

			sqlins.append( "INSERT INTO FNCHEQUE ( " );
			sqlins.append( "CODEMP, CODFILIAL, CODEMPBO, CODFILIALBO, CODBANC, SEQCHEQ, NUMCHEQ, ");
			sqlins.append( "AGENCIACHEQ, CONTACHEQ, NOMEEMITCHEQ,  NOMEFAVCHEQ, ");
			sqlins.append( "VLRCHEQ,  HISTCHEQ, CNPJEMITCHEQ, CPFEMITCHEQ, DDDEMITCHEQ, FONEEMITCHEQ, ");
			sqlins.append( "CNPJFAVCHEQ, CPFFAVCHEQ ) ");
			sqlins.append( "SELECT ");
			sqlins.append( Aplicativo.iCodEmp );
			sqlins.append(" CODEMP, ");
			sqlins.append( ListaCampos.getMasterFilial( "FNCHEQUE" ) );
			sqlins.append( " CODFILIAL, ");
			sqlins.append( "CT.CODEMPBO, CT.CODFILIALBO, CT.CODBANCO CODBANC, ");
			sqlins.append( seqcheq );
			sqlins.append( " SEQCHEQ, 0 NUMCHEQ, CT.AGENCIACONTA AGENCIACHEQ, '");
			sqlins.append( numconta.trim() );
			sqlins.append( "' CONTACHEQ, F.RAZFILIAL NOMEEMITCHEQ, FR.RAZFOR NOMEFAVCHEQ, ");
			sqlins.append( vlrcheque );
			sqlins.append( " VLRCHEQ, ");
			sqlins.append( histcheq.toString() );
			sqlins.append(" FR.RAZFOR  HISTCHEQ, F.CNPJFILIAL CNPJEMITCHEQ, '' CPFEMITCHEQ, ");
			sqlins.append( "F.DDDFILIAL DDDEMITCHEQ, SUBSTRING( F.FONEFILIAL FROM 1 FOR 8) FONEEMITCHEQ, ");
			sqlins.append( "FR.CNPJFOR CNPJFAVCHEQ, FR.CPFFOR CPFFAVCHEQ ");
			sqlins.append( "FROM SGFILIAL F, CPFORNECED FR, FNCONTA CT ");
			sqlins.append( "WHERE F.CODEMP=? AND F.CODFILIAL=? AND ");
			sqlins.append( "FR.CODEMP=? AND FR.CODFILIAL=? AND FR.CODFOR=? AND " );
			sqlins.append( "CT.CODEMP=? AND CT.CODFILIAL=? AND CT.NUMCONTA=? ");

			ps = con.prepareStatement( sqlins.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGFILIAL" ) );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "CPFORNECED" ) );
			ps.setInt( 5, codfor );
			ps.setInt( 6, Aplicativo.iCodEmp );
			ps.setInt( 7, ListaCampos.getMasterFilial( "FNCONTA" ) );
			ps.setString( 8, numconta );
			return ps.executeUpdate(); 
		}

		private String getNumconta(LinkedList<Vector<Object>> listapagar) {
			String numconta = "";
			for ( int i=0; i<listapagar.size(); i++ ) {
				if ( (Boolean) ( (Vector<Object>) listapagar.get( i ) ).elementAt( COLS_PAG.SEL.ordinal() ) ) {
					numconta = ( String ) ( (Vector<Object>) listapagar.get( i ) ).elementAt( COLS_PAG.NUMCONTA.ordinal() ) ;
				}
			}
			return numconta;
		}

		private int getSeqcheque() throws SQLException {
			int seqcheq = 1;
			String sqlquery = "SELECT COALESCE(NROSEQ,0)+1 NROSEQ FROM SGSEQUENCIA WHERE CODEMP=? AND CODFILIAL=? AND SGTAB=?";
			String sqlinsert = "INSERT INTO SGSEQUENCIA (CODEMP, CODFILIAL, SGTAB, NROSEQ) VALUES (?,?,?,?)";
			String sqlupdate = "UPDATE SGSEQUENCIA SET NROSEQ=? WHERE CODEMP=? AND CODFILIAL=? AND SGTAB=?";
			PreparedStatement ps = con.prepareStatement( sqlquery );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNCHEQUE" ));
			ps.setString( 3, "CH" );
			ResultSet rs =  ps.executeQuery();
			if (rs.next()) {
				seqcheq = rs.getInt( "NROSEQ" );
				ps =  con.prepareStatement( sqlupdate );
				ps.setInt( 1, seqcheq );
				ps.setInt( 2, Aplicativo.iCodEmp );
				ps.setInt( 3, ListaCampos.getMasterFilial( "FNCHEQUE" ) );
				ps.setString( 4, "CH" );
				ps.executeUpdate();
			} else {
				ps.close();
				ps = con.prepareStatement( sqlinsert );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "FNCHEQUE" ) );
				ps.setString( 3, "CH" );
				ps.setInt( 4, seqcheq );
				ps.executeUpdate();
			}
			return seqcheq;
		}

		private int getSeqtalao( final String numconta ) throws SQLException {
			ResultSet rs = null;
			PreparedStatement ps = null;
			int result = 0;
			StringBuffer sql = new StringBuffer( "SELECT MAX(T.SEQTALAO) SEQTALAO FROM FNTALAOCHEQ T " );
			sql.append( "WHERE T.CODEMP=? AND T.CODFILIAL=? AND T.NUMCONTA=? AND T.ATIVOTALAO=? " );
			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNTALAOCHEQ" ) );
			ps.setString( 3, numconta );
			ps.setString( 4, "S" );
			rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getInt( "SEQTALAO" );
			}
			rs.close();
			ps.close();
			con.commit();
			return result;
		}

		private void gerarCheque(LinkedList<Vector<Object>> listapagar) {
			String numconta = getNumconta(listapagar);
			BigDecimal vlrcheque = txtVlrTotSelPag.getVlrBigDecimal();
			int seqtalao = 0;
			int seqcheq = 0;
			try {
				seqcheq = getSeqcheque();
				execSqlInsertCheque( txtCodFor.getVlrInteger() , numconta, seqcheq, vlrcheque );
				execSqlInsertPagcheq( listapagar, seqcheq );
				con.commit();
				carregaTabPagar();
				// getSeqtalao tem que ser após commit, pois o método possui chamada interna ao commit.
				seqtalao = getSeqtalao( numconta );
				// As rotinas abaixo são responsáveis pela carga dos cheques
				txtNumconta.setVlrString( numconta );
				txtSeqtalao.setVlrInteger( seqtalao );
				lcConta.carregaDados();
				carregaTabCheq();
				tpn.setSelectedIndex( 1 ); // Seleciona aba de cheques
				tabCheq.requestFocus();
			} catch (SQLException e) {
				try {
					con.rollback();
				} catch ( SQLException err ) {
					Funcoes.mensagemErro( this, "Erro executando rollback!\n" + err.getMessage() );
				}
				Funcoes.mensagemErro( this, "Erro executando inserção do cheque!\n" + e.getMessage() );
				e.printStackTrace();
			}
		}

		private synchronized void imprimirCheque( LinkedList<Vector<Object>> listacheq, TYPE_PRINT visualizar ) {
			PreparedStatement ps = null;
			BigDecimal vlrcheque = txtVlrTotSelCheq.getVlrBigDecimal();
			String numconta = txtNumconta.getVlrString();
			int seqtalao = txtSeqtalao.getVlrInteger();
			ImprimeOS imp = new ImprimeOS("", con, "CH", true );
			imp.setImpEject( false );
			Vector<Object> item = null;
			Map<String, Object> itemMap = null;
			// imprime comprimido na coluna 2 para utiliar a linha 0
			imp.say( 0, 2, imp.comprimido() );

			Cheque cheque = null;

			for ( int i=0; i<listacheq.size(); i++ ) {
				item = listacheq.get( i );

				Integer seqcheque = (Integer) item.elementAt( Cheque.COLS_CHEQ.SEQ.ordinal() );
				cheque = new Cheque( seqcheque );

				itemMap = cheque.montaMap(item);

				cheque.montaLayoutCheq( imp, itemMap );

				imp.setPrc( 0, 0 );
			}
			
			if ( visualizar == TYPE_PRINT.VIEW ) {
				imp.setEnabledBotaoImp( false );
				imp.preview( this );
			} else 	if ( ajustaNumcheq( listacheq, numconta, seqtalao ) ) {
				imp.print();
				if ( confirmaImpressao() ) {
					atualizaSitCheq( listacheq, numconta, seqtalao );
					carregaTabCheq();
				}
			}
		}

		private boolean atualizaSitCheq( LinkedList<Vector<Object>> listacheq, String numconta, int seqtalao ) {
			boolean result = false;
			PreparedStatement ps = null;
			StringBuffer sqlcheq = new StringBuffer();
			StringBuffer sqltalao = new StringBuffer();
			sqlcheq.append( "UPDATE FNCHEQUE SET NUMCHEQ=?, SITCHEQ=?, VLRCHEQ=? " );
			sqlcheq.append( "WHERE CODEMP=? AND CODFILIAL=? AND SEQCHEQ=?" );
			sqltalao.append( "UPDATE FNTALAOCHEQ SET CHEQATUALTALAO=? " );
			sqltalao.append( "WHERE CODEMP=? AND CODFILIAL=? AND NUMCONTA=? AND SEQTALAO=?" );
			try {
				for ( int i=0; i<listacheq.size(); i++ ) {
					ps = con.prepareStatement( sqlcheq.toString() );
					ps.setInt( 1, (Integer) listacheq.get( i ).elementAt( Cheque.COLS_CHEQ.NUMCHEQ.ordinal() ) );
					ps.setString( 2, "ED" );
					ps.setBigDecimal( 3, ConversionFunctions.stringCurrencyToBigDecimal( (String) listacheq.get( i ).elementAt( Cheque.COLS_CHEQ.VLRCHEQ.ordinal()+1 ) ) );
					ps.setInt( 4, Aplicativo.iCodEmp );
					ps.setInt( 5, ListaCampos.getMasterFilial( "FNCHEQUE" ) );
					ps.setInt( 6, (Integer) listacheq.get( i ).elementAt( Cheque.COLS_CHEQ.SEQ.ordinal() ) );
					ps.executeUpdate();
					ps.close();
				}
				ps = con.prepareStatement( sqltalao.toString() );
				ps.setInt( 1, cheqatualtalao );
				ps.setInt( 2, Aplicativo.iCodEmp );
				ps.setInt( 3, ListaCampos.getMasterFilial( "FNTALAOCHEQ" ) );
				ps.setString( 4, numconta );
				ps.setInt( 5, seqtalao );

				ps.executeUpdate();
				con.commit();
			} catch ( SQLException e ) {
				e.printStackTrace();

			} finally {
				sqlcheq = null;
				sqltalao = null;
			}
			return result;
		}

		private boolean confirmaImpressao() {
			boolean result = false;
			if ( Funcoes.mensagemConfirma( this, "Confirma impressão dos cheques?" ) == JOptionPane.YES_OPTION ) {
				result = true;
			} 
			return result;
		}

		private boolean ajustaNumcheq( LinkedList<Vector<Object>> listacheq, String numconta, int seqtalao ) {
			boolean result = true;
			try { 
				cheqatualtalao = getCheqatualtalao( numconta, seqtalao );
				// Execução de commit para liberar tabela de talonário.
				con.commit();
				for ( int i=0; i<listacheq.size(); i++ ) {
					cheqatualtalao ++;
					listacheq.get( i ).add( Cheque.COLS_CHEQ.NUMCHEQ.ordinal(), new Integer(cheqatualtalao) ) ;
				}
			} catch ( SQLException e ) {
				result = false;
				Funcoes.mensagemErro( this, "Erro carregando a numeração de cheques!\n" + e.getMessage() );
			}
			return result;
		}
		
		private boolean validaValores(){
			boolean result = true;
			for ( int i=0; i < tabCheq.getNumLinhas(); i++ ) {
				Object valor = tabCheq.getValor( i, Cheque.COLS_CHEQ.VLRCHEQ.ordinal() );
				BigDecimal bValor = null;
				try{
					
					valor = StringUtils.replace( valor.toString(), ".", "" );
					valor = StringUtils.replace( valor.toString(), ",", "." );
					
					bValor = new BigDecimal(  valor.toString() );
					
				}catch(Exception e){
					result = false;
				}
				
				if(result && bValor.doubleValue() <= 0){
					result = false;
				}
			}
			
			if(!result){
				Funcoes.mensagemErro( this, "Ha cheques com valores incorretos." );
			}
			
			return result;
		}

		private boolean validaImpressora() {
			boolean result = false;
			ResultSet rs = null;
			PreparedStatement ps = null;
			StringBuffer sql = new StringBuffer("SELECT EI.TIPOUSOIMP FROM SGESTACAOIMP EI " );
			sql.append( "WHERE EI.CODEMP=? AND EI.CODFILIAL=? AND CODEST=? AND TIPOUSOIMP=?" );
			try { 
				ps = con.prepareStatement( sql.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "SGESTACAOIMP" ) );
				ps.setInt( 3, Aplicativo.iNumEst );
				ps.setString( 4, "CH");
				rs = ps.executeQuery();
				if ( rs.next() ) {
					result = true;
				}
				if ( !result ) {
					Funcoes.mensagemInforma( this, "Configure impressora de cheques na estação de trabalho!" );
				}
			} catch ( SQLException e ) {
				Funcoes.mensagemInforma( this, "Erro consultando impressora!\n" + e.getMessage() );
			}
			return result;
		}

		private boolean validaListapagar(LinkedList<Vector<Object>> listapagar) {
			boolean result = false;
			Vector<Object> item = null;
			String numconta = "";
			if ( listapagar.size()>0 ) {
				for ( int i=0; i<listapagar.size(); i++ ) {
					item = listapagar.get( i );
					if ( "".equals( item.elementAt( COLS_PAG.NUMCONTA.ordinal() ) ) ) {
						Funcoes.mensagemInforma( this, "Um item selecionado não possui conta definida!" );
						break;
					} else if ( "".equals( item.elementAt( COLS_PAG.NUMCONTA.ordinal() ) ) ) {
						if ( "".equals( numconta ) ) {
							numconta = (String) item.elementAt( COLS_PAG.NUMCONTA.ordinal() );
						} else if ( numconta.equals( item.elementAt( COLS_PAG.NUMCONTA.ordinal() ) ) ) {
							Funcoes.mensagemInforma( this, "Um item selecionado possui conta diferente dos demais!" );
							break;
						}
					} else if ("".equals( item.elementAt( COLS_PAG.CODTIPOCOB.ordinal() ) )) {
						Funcoes.mensagemInforma( this, "Um item selecionado não possui tipo de cobrança definido!" );
						break;
					} else {
						result = true;
					}
				}
			} else {
				Funcoes.mensagemInforma( this, "Selecione algum item na lista!" );
			}
			return result;
		}

		private boolean validaListacheq(LinkedList<Vector<Object>> listacheq) {
			boolean result = false;
			Vector<Object> item = null;
			String numconta = "";
			if ( listacheq.size()>0 ) {
				for ( int i=0; i<listacheq.size(); i++ ) {
					item = listacheq.get( i );
					if ( "".equals( item.elementAt( Cheque.COLS_CHEQ.NUMCONTA.ordinal() ) ) ) {
						Funcoes.mensagemInforma( this, "Um item selecionado não possui conta definida!" );
						break;
					} else if ( "".equals( item.elementAt( Cheque.COLS_CHEQ.NUMCONTA.ordinal() ) ) ) {
						if ( "".equals( numconta ) ) {
							numconta = (String) item.elementAt( Cheque.COLS_CHEQ.NUMCONTA.ordinal() );
						} else if ( numconta.equals( item.elementAt( Cheque.COLS_CHEQ.NUMCONTA.ordinal() ) ) ) {
							Funcoes.mensagemInforma( this, "Um item selecionado possui conta diferente dos demais!" );
							break;
						}
					} else {
						result = true;
					}
				}
			} else {
				Funcoes.mensagemInforma( this, "Selecione algum cheque na lista!" );
			}
			return result;
		}

		private LinkedList<Vector<Object>> getListapagar(LinkedList<Vector<Object>> listapagar) {
			Vector<Object> item = null;
			for (int i=0; i<tabPagar.getNumLinhas(); i++) {
				item = (Vector<Object>) tabPagar.getLinha( i );
				if ( (Boolean) item.elementAt( COLS_PAG.SEL.ordinal() ) ) {
					listapagar.add( item );
				}
			}
			return listapagar;
		}

		private LinkedList<Vector<Object>> getListacheq(LinkedList<Vector<Object>> listacheq) {
			Vector<Object> item = null;
			for (int i=0; i<tabCheq.getNumLinhas(); i++) {
				item = (Vector<Object>) tabCheq.getLinha( i );
				if ( (Boolean) item.elementAt( Cheque.COLS_CHEQ.SEL.ordinal() ) ) {
					listacheq.add( item );
				}
			}
			return listacheq;
		}

		public void setConexao( DbConnection cn ) {
			super.setConexao( cn );
			lcFor.setConexao( cn );
			lcConta.setConexao( cn );
			lcTalaoCheq.setConexao( cn );
			setPrefs();
		}

		private void setPrefs() {
			ResultSet rs = null;
			PreparedStatement ps = null;
			StringBuffer sql = new StringBuffer( "SELECT P.CODMOEDA, MC.NOMEMUNIC, " );
			sql.append(	"M.SINGMOEDA, M.PLURMOEDA, M.DECSMOEDA, M.DECPMOEDA, ANOCENTROCUSTO, CODHISTPAG, LANCAFINCONTR ");
			sql.append( "FROM FNMOEDA M, SGPREFERE1 P, SGFILIAL F, SGMUNICIPIO MC " );
			sql.append( "WHERE M.CODMOEDA=P.CODMOEDA AND P.CODEMP=? AND P.CODFILIAL=? AND " );
			sql.append( "F.CODEMP=? AND F.CODFILIAL=? AND ");
			sql.append( "MC.CODPAIS=F.CODPAIS AND MC.SIGLAUF=F.SIGLAUF AND MC.CODMUNIC=F.CODMUNIC" );


			try {
				ps = con.prepareStatement( sql.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
				ps.setInt( 3, Aplicativo.iCodEmp );
				ps.setInt( 4, ListaCampos.getMasterFilial( "SGFILIAL" ) );
				rs = ps.executeQuery();
				if ( rs.next() ) {
					prefs.put( "CODMOEDA", rs.getString( "CODMOEDA" ).trim() );
					prefs.put( "NOMEMUNIC", rs.getString( "NOMEMUNIC" ).trim() );
					prefs.put( "SINGMOEDA", rs.getString( "PLURMOEDA" ).trim() );
					prefs.put( "PLURMOEDA", rs.getString( "PLURMOEDA" ).trim() );
					prefs.put( "DECSMOEDA", rs.getString( "DECSMOEDA" ).trim() );
					prefs.put( "DECPMOEDA", rs.getString( "DECPMOEDA" ).trim() );
					prefs.put( "ANOCENTROCUSTO", rs.getString( "ANOCENTROCUSTO" ).trim() );
					prefs.put( "CODHISTPAG", rs.getString( "CODHISTPAG" ).trim() );
					prefs.put( "LANCAFINCONTR", rs.getString( "LANCAFINCONTR" ) );

				}
				con.commit();
			} catch (SQLException e) {
				Funcoes.mensagemErro( this, "Erro consultando preferências!\n" + e.getMessage() );
			}
		}

		public void valorAlterado( TabelaEditEvent evt ) {

			if(carregandoTabela)
				return;
			
			if ( evt.getTabela() == tabPagar && 
					evt.getTabela().getColunaEditada() == COLS_PAG.SEL.ordinal() ) {
				calcTotaisPag();
			} else if ( evt.getTabela() == tabCheq && 
					evt.getTabela().getColunaEditada() == Cheque.COLS_CHEQ.VLRCHEQ.ordinal() ) {
				calcTotaisCheq();
			}

		}

		public void keyPressed( KeyEvent kevt ) {
			if ( kevt.getKeyCode() == KeyEvent.VK_ENTER ) {
				if ( kevt.getSource() == txtDatafimPagar ) {
					carregaTabPagar();
				} else if ( kevt.getSource() == txtSeqtalao ) {
					carregaTabCheq();
				} else if ( kevt.getSource() == tabPagar ) {
					// Gerar cheque quando pressionar <ENTER> sobre a lista de pagamentos.
					gerar();
				} else if ( kevt.getSource() == tabCheq ) {
					// Imprimir cheque quando pressionar <ENTER> sobre a lista de cheques.
					imprimir( TYPE_PRINT.PRINT);
				}
			}
		}

		private void editar() {

			PreparedStatement ps = null;
			StringBuffer sql = new StringBuffer();
			String[] sVals = null;
			String[] sRets = null;
			DLEditaPag dl = null;

			int iLin;
			String codhistpag = null;
			boolean lancafincontr = "S".equals( prefs.get("LANCAFINCONTR") );

			try {

				if ( tabPagar.getLinhaSel() > -1 ) {

					codhistpag = (String) prefs.get( "CODHISTPAG" );

					iLin = tabPagar.getLinhaSel();

					Integer iCodPag =  (Integer) tabPagar.getValor( iLin, COLS_PAG.CODPAG.ordinal() ) ;
					Integer iNParcPag = (Integer) tabPagar.getValor( iLin, COLS_PAG.NPARCPAG.ordinal() );

					sVals = new String[ EDIT_PAG_SETVALORES.values().length ];

					dl = new DLEditaPag( this, false, lancafincontr );

					sVals[ EDIT_PAG_SETVALORES.CODFOR.ordinal() ] = (String) tabPagar.getValor( iLin, COLS_PAG.CODFOR.ordinal() ).toString();
					sVals[ EDIT_PAG_SETVALORES.RAZFOR.ordinal() ] = (String) tabPagar.getValor( iLin, COLS_PAG.RAZFOR.ordinal() ).toString();

					sVals[ EDIT_PAG_SETVALORES.CODCONTA.ordinal() ] = (String) tabPagar.getValor( iLin, COLS_PAG.NUMCONTA.ordinal() ).toString();
					sVals[ EDIT_PAG_SETVALORES.CODPLAN.ordinal() ] = (String) tabPagar.getValor( iLin, COLS_PAG.CODPLAN.ordinal() ).toString();
					sVals[ EDIT_PAG_SETVALORES.CODCC.ordinal() ] = (String) tabPagar.getValor( iLin, COLS_PAG.CODCC.ordinal() ).toString();

					sVals[ EDIT_PAG_SETVALORES.DOC.ordinal() ] = (String) tabPagar.getValor( iLin, COLS_PAG.DOC.ordinal() ).toString();
					sVals[ EDIT_PAG_SETVALORES.DTEMIS.ordinal() ] = (String) tabPagar.getValor( iLin, COLS_PAG.DTITPAG.ordinal() ).toString();					
					sVals[ EDIT_PAG_SETVALORES.DTVENC.ordinal() ] = (String) tabPagar.getValor( iLin, COLS_PAG.DTVENCITPAG.ordinal() ).toString();

					sVals[ EDIT_PAG_SETVALORES.VLRPARC.ordinal() ] = 	( (StringDireita) tabPagar.getValor( iLin, COLS_PAG.VLRPARCITPAG.ordinal() )).toString();
					sVals[ EDIT_PAG_SETVALORES.VLRJUROS.ordinal() ] = 	( (StringDireita) tabPagar.getValor( iLin, COLS_PAG.VLRJUROSITPAG.ordinal() )).toString();
					sVals[ EDIT_PAG_SETVALORES.VLRDESC.ordinal() ] = 	( (StringDireita) tabPagar.getValor( iLin, COLS_PAG.VLRDESCITPAG.ordinal() )).toString(); 
					sVals[ EDIT_PAG_SETVALORES.VLRADIC.ordinal() ] = 	( (StringDireita) tabPagar.getValor( iLin, COLS_PAG.VLRADICITPAG.ordinal() )).toString(); 

					sVals[ EDIT_PAG_SETVALORES.OBS.ordinal() ] = (String) tabPagar.getValor( iLin, COLS_PAG.OBSITPAG.ordinal() ).toString();
					sVals[ EDIT_PAG_SETVALORES.CODTIPOCOB.ordinal() ] = (String) tabPagar.getValor( iLin, COLS_PAG.CODTIPOCOB.ordinal() ).toString();
					sVals[ EDIT_PAG_SETVALORES.VLRDEV.ordinal() ] = (String) tabPagar.getValor( iLin, COLS_PAG.VLRDEVITPAG.ordinal() ).toString();

					// Cod. pagar e nparc para carregar lista de cheques
					sVals[ EDIT_PAG_SETVALORES.CODPAG.ordinal() ] = (String) tabPagar.getValor( iLin, COLS_PAG.CODPAG.ordinal() ).toString();
					sVals[ EDIT_PAG_SETVALORES.NPARCPAG.ordinal() ] = (String) tabPagar.getValor( iLin, COLS_PAG.NPARCPAG.ordinal() ).toString();

					sVals[ EDIT_PAG_SETVALORES.CODCONTR.ordinal() ] = "";
					sVals[ EDIT_PAG_SETVALORES.CODITCONTR.ordinal() ] = "";

					// Se o doccompra estiver em branco getvalor(8) quer dizer que o lançamento foi feito pelo usuário.
					dl.setValores( sVals, "".equals( tabPagar.getValor( iLin, COLS_PAG.DOCCOMPRA.ordinal() ).toString().trim() ) );

					dl.setConexao( con );
					dl.setVisible( true );

					try {
						dl.salvaPag();
					} catch ( Exception err ) {
						Funcoes.mensagemErro( this, "Erro ao editar parcela!\n" + err.getMessage(), true, con, err );
						err.printStackTrace();
					}

					dl.dispose();
					carregaTabPagar();
				}
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}

		public void keyReleased( KeyEvent arg0 ) { }

		public void keyTyped( KeyEvent arg0 ) { }

}


