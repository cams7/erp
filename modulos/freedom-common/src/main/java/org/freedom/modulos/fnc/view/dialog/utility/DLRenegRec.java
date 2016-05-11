/*
 * Projeto: Freedom Pacote: org.freedom.modules.fnc Classe: @(#)DLRenegRec.java
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR> modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR> na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR> sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR> Veja a Licença Pública Geral GNU para maiores detalhes. <BR> Você deve ter recebido uma cópia da Licença Pública
 * Geral GNU junto com este programa, se não, <BR> escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA <BR> <BR>
 */
package org.freedom.modulos.fnc.view.dialog.utility;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.object.Historico;
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
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.fnc.library.swing.component.JTextFieldPlan;
import org.freedom.modulos.fnc.view.dialog.utility.DLEditaRec.EColEdit;
import org.freedom.modulos.fnc.view.dialog.utility.DLEditaRec.EColRet;

/**
 * Wizard para renegociação de títulos.
 * 
 * @author Fabiano Frizzo
 * @version 20/12/2010
 */
public class DLRenegRec extends FFDialogo implements FocusListener, CarregaListener, PostListener, ChangeListener, MouseListener{
	private static final long serialVersionUID = 1L;
	
	private enum RECEBER {
		SEL, STATUS, DTVENC, CODREC, NPARCITREC, DOCLANCA, CODCLI, RAZCLI, OBS, DOCVENDA, VLRPARC, 
		DTPAGTO, VLRPAGO, VLRDESC, VLRJUROS, NUMCONTA, DESCCONTA, CODPLAN, DESCPLAN, CODBANCO, NOMEBANCO;
	}
	
	public enum EFields {
		VALOR, DATA, DESCONTO, TIPOCOB, BANCO, CARTCOB, DESCPONT, DATAPREV, OBSITREC
	}
	
	private static final Integer SELECIONA = 0;
	private static final Integer GERA = 1;
	
	private JTabbedPanePad tpn = new JTabbedPanePad();
	private JTablePad tabSeleciona = new JTablePad();
	private JScrollPane spnSeleciona = new JScrollPane( tabSeleciona );
	private JPanelPad pnSeleciona = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );
	private JTablePad tabGera = new JTablePad();
	private JScrollPane spnGera = new JScrollPane( tabGera );
	private JPanelPad pnGera = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );
	
	//Images
	private ImageIcon imgVencido = Icone.novo( "clVencido.gif" );
	private ImageIcon imgVencido2 = Icone.novo( "clVencido2.gif" );
	private ImageIcon imgPago = Icone.novo( "clPago.gif" );
	private ImageIcon imgPagoParcial = Icone.novo( "clPagoParcial.gif" );
	private ImageIcon imgNaoVencido = Icone.novo( "clNaoVencido.gif" );
	private ImageIcon imgCancelado = Icone.novo( "clCancelado.gif" );
	private ImageIcon imgRenegociado = Icone.novo( "clRenegociado.png" );
	private ImageIcon imgEmRenegociacao = Icone.novo( "btRenegRec.png" );
	private ImageIcon imgColuna = null;
	
	//Components Seleciona
	private JPanelPad panelSelecionaActions = new JPanelPad( 42, 200 );
	private JPanelPad panelSelecionaRod = new JPanelPad( 610, 42 );
	private JButtonPad btSelecionarTodos = new JButtonPad( Icone.novo( "btTudo.png" ) );
	private JButtonPad btSelecionarNenhum = new JButtonPad( Icone.novo( "btNada.png" ) );
	private JButtonPad btGerarRenegociaco = new JButtonPad( "Gerar Negociação", Icone.novo( "btExecuta.png" ) );
	private JLabelPad lbTotalSelecionado = new JLabelPad("Total Selecionado", SwingConstants.LEFT);
	private JTextFieldPad txtTotalSelecionado = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );
	
	//Components Gera
	private JPanelPad pnGeraRec = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );
	private JPanelPad pnGeraRecDados = new JPanelPad( 610, 280 );
	private JPanelPad pnGeraRecRod = new JPanelPad( 610, 42 );
	
	private Navegador navRec = new Navegador( false );
	private Navegador navItRec = new Navegador( false );
	
	private Map<String, Integer> prefere = null;
	private Historico historico = null;
	public static final String HISTORICO_PADRAO = "RECEBIMENTO REF. A RENEGOCIACAO DO(S) TITULO(S): <TITULOS>";
	
	public JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	private JTextFieldFK txtCNPJCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 14, 0 );
	private JTextFieldFK txtDescCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldPad txtCodPlanoPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	private JTextFieldPad txtParcPlanoPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	private JTextFieldFK txtDescPlanoPag = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );
	private JTextFieldPad txtCodTipoCob = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	private JTextFieldFK txtDescTipoCob = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );
	private JTextFieldPad txtObrigCart = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );
	private JTextFieldPad txtCodBanco = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );
	private JTextFieldFK txtDescBanco = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );
	private JTextFieldPad txtCodCartCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 3	, 0 );
	private JTextFieldFK txtDescCartCob = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	private JTextFieldPad txtCodTipoCobItRec = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	private JTextFieldFK txtDescTipoCobItRec = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );
	private JTextFieldPad txtCodBancoItRec = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );
	private JTextFieldFK txtDescBancoItRec = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );
	private JTextFieldPad txtCodCartCobItRec = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );
	private JTextFieldFK txtDescCartCobItRec = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );
	private JTextFieldPad txtDtPrevItRec = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );
	private JTextFieldPad txtCodRec = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	private JTextFieldPad txtNParcRec = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	private JTextFieldPad txtVlrParcItRec = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );
	private JTextFieldPad txtVlrDescItRec = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );
	private JTextFieldPad txtDtVencItRec = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );
	//private JTextFieldPad txtVlrParcRec = new JTextFieldPad( JTextFieldPad.TP_NUMERIC, 15, 2 );
	private JTextFieldPlan txtCodPlan = new JTextFieldPlan( JTextFieldPad.TP_STRING, 13, 0 );
	private JTextFieldFK txtDescPlan = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	private JTextFieldPad txtDtReneg = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );
	private JTextFieldPad txtDocRec = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );
	private JTextFieldPad txtObs = new JTextFieldPad( JTextFieldPad.TP_STRING, 250, 0 );
	private JTextFieldPad txtStatus = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );
	private JTextFieldPad txtCodConta = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );
	private JTextFieldPad txtCodCC = new JTextFieldPad( JTextFieldPad.TP_STRING, 19, 0 );
	private JTextFieldPad txtAnoCC = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );
	private JTextFieldFK txtSiglaCC = new JTextFieldFK( JTextFieldPad.TP_STRING, 10, 0 );
	private JTextFieldFK txtDescCC = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	private JTextFieldFK txtDescConta = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldPad txtCodEmpRengRec = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );
	private JTextFieldPad txtCodFilialRenegRec = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );
	private JTextFieldPad txtCodRenegReg = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );
	
	private JTextFieldPad txtValorTotalOriginal = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );
	private JTextFieldPad txtValorDesconto = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );
	private JTextFieldPad txtValorAdicional = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );
	private JTextFieldPad txtValorJuros = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );
	private JTextFieldPad txtValorTotalRenegociado = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );
	
	//ListaCampos
	private ListaCampos lcReceber = new ListaCampos( this );
	private ListaCampos lcItReceber = new ListaCampos( this );
	private ListaCampos lcCli = new ListaCampos( this, "CL" );
	private ListaCampos lcPlanoPag = new ListaCampos( this, "PG" );
	private ListaCampos lcTipoCob = new ListaCampos( this, "TC" );
	private ListaCampos lcBanco = new ListaCampos( this, "BO" );
	private ListaCampos lcCartCob = new ListaCampos( this, "CB" );
	private ListaCampos lcBancoItRec = new ListaCampos( this, "BO" );
	private ListaCampos lcCartCobItRec = new ListaCampos( this, "CB" );
	private ListaCampos lcTipoCobItRec = new ListaCampos( this, "TC" );
	private ListaCampos lcPlan = new ListaCampos( this, "PN" );
	private ListaCampos lcCC = new ListaCampos( this, "CC" );
	private ListaCampos lcConta = new ListaCampos( this, "CA" );
	
	public DLRenegRec() {
		super();
		setTitulo( "Renegociação de Títulos" );
		setAtribos( 610, 600 );
		
		Container c = getContentPane();
		c.setLayout( new BorderLayout() );
		c.add( tpn, BorderLayout.CENTER );
		
		tpn.addTab( "Seleciona Titulos", pnSeleciona );
		pnSeleciona.add( spnSeleciona, BorderLayout.CENTER );
		pnSeleciona.add( panelSelecionaActions, BorderLayout.EAST );
		pnSeleciona.add( panelSelecionaRod, BorderLayout.SOUTH);
		
		tpn.addTab( "Gera Renegociação", pnGera );
		tpn.setEnabledAt( GERA, false );
		pnGera.add( pnGeraRec, BorderLayout.CENTER);
		
		adicBotaoSair();
		adicionaListeners();
		montaTabSeleciona();
		montaTabGera();
	}
	
	private void adicionaListeners(){
		tpn.addChangeListener( this );
		
		btSelecionarNenhum.addActionListener( this );
		btSelecionarTodos.addActionListener( this );
		btGerarRenegociaco.addActionListener( this );
		tabGera.addMouseListener( this );
	}
	
	private void montaTabSeleciona(){
		tabSeleciona.adicColuna( "" );
		tabSeleciona.adicColuna( "" );
		tabSeleciona.adicColuna( "Vencto." );
		tabSeleciona.adicColuna( "Código" );
		tabSeleciona.adicColuna( "Parc." );
		tabSeleciona.adicColuna( "Doc." );
		tabSeleciona.adicColuna( "Cód.cli." );
		tabSeleciona.adicColuna( "Razão social do cliente" );
		tabSeleciona.adicColuna( "Observação" );
		tabSeleciona.adicColuna( "Doc. venda" );
		tabSeleciona.adicColuna( "Valor" );
		tabSeleciona.adicColuna( "Pagamento" );
		tabSeleciona.adicColuna( "Valor pago" );
		tabSeleciona.adicColuna( "Valor desc." );
		tabSeleciona.adicColuna( "Valor juros" );
		tabSeleciona.adicColuna( "Conta" );
		tabSeleciona.adicColuna( "Descrição da conta" );
		tabSeleciona.adicColuna( "Cód.planejamento" );
		tabSeleciona.adicColuna( "Descrição do planejamento" );
		tabSeleciona.adicColuna( "Cód.banco" );
		tabSeleciona.adicColuna( "Nome do banco" );
		
		tabSeleciona.setTamColuna( 20, RECEBER.SEL.ordinal() );
		tabSeleciona.setTamColuna( 20, RECEBER.STATUS.ordinal() );
		tabSeleciona.setTamColuna( 60, RECEBER.DTVENC.ordinal() );
		tabSeleciona.setTamColuna( 60, RECEBER.CODREC.ordinal() );
		tabSeleciona.setTamColuna( 40, RECEBER.NPARCITREC.ordinal() );
		tabSeleciona.setTamColuna( 50, RECEBER.DOCLANCA.ordinal() );
		tabSeleciona.setTamColuna( 50, RECEBER.CODCLI.ordinal() );
		tabSeleciona.setTamColuna( 140, RECEBER.RAZCLI.ordinal() );
		tabSeleciona.setTamColuna( 150, RECEBER.OBS.ordinal() );
		tabSeleciona.setTamColuna( 70, RECEBER.DOCVENDA.ordinal() );
		tabSeleciona.setTamColuna( 80, RECEBER.VLRPARC.ordinal() );
		tabSeleciona.setTamColuna( 80, RECEBER.DTPAGTO.ordinal() );
		tabSeleciona.setTamColuna( 80, RECEBER.VLRPAGO.ordinal() );
		tabSeleciona.setTamColuna( 80, RECEBER.VLRDESC.ordinal() );
		tabSeleciona.setTamColuna( 80, RECEBER.VLRJUROS.ordinal() );
		tabSeleciona.setTamColuna( 60, RECEBER.NUMCONTA.ordinal() );
		tabSeleciona.setTamColuna( 130, RECEBER.DESCCONTA.ordinal() );
		tabSeleciona.setTamColuna( 100, RECEBER.CODPLAN.ordinal() );
		tabSeleciona.setTamColuna( 150, RECEBER.DESCPLAN.ordinal() );
		tabSeleciona.setTamColuna( 60, RECEBER.CODBANCO.ordinal() );
		tabSeleciona.setTamColuna( 150, RECEBER.NOMEBANCO.ordinal() );
		
		tabSeleciona.setColunaEditavel( RECEBER.SEL.ordinal(), true );
		tabSeleciona.addMouseListener( new MouseAdapter() {
			public void mouseClicked( MouseEvent mevt ) {
				if ( mevt.getClickCount() == 1 ) {
					calculaSelecionados();
				}
			}
		});
		
		panelSelecionaActions.adic( btSelecionarTodos, 3, 3, 30, 30 );
		panelSelecionaActions.adic( btSelecionarNenhum, 3, 38, 30, 30 );
		
		txtTotalSelecionado.setSoLeitura( true );
		panelSelecionaRod.adic( lbTotalSelecionado, 310, 1, 110, 17 );
		panelSelecionaRod.adic( txtTotalSelecionado, 310, 18, 110, 18 );
		panelSelecionaRod.adic( btGerarRenegociaco, 430, 7 ,150, 30 );
	}
	
	private void montaTabGera(){
		lcItReceber.setMaster( lcReceber );
		lcReceber.adicDetalhe( lcItReceber );
		lcItReceber.setTabela( tabGera );

		navRec.setName( "Receber" );
		lcReceber.setNavegador( navRec );
		navItRec.setName( "itpagar" );
		lcItReceber.setNavegador( navItRec );
		
		montaListaCampos();
		
		lcItReceber.montaTab();
		
		pnGeraRec.add( pnGeraRecDados, BorderLayout.NORTH );
		pnGeraRec.add( spnGera, BorderLayout.CENTER );
		pnGeraRec.add( pnGeraRecRod, BorderLayout.SOUTH );
		
		pnGeraRecRod.adic( btOK, 480, 7 ,100, 30 );

		pnGeraRecDados.adic( new JLabelPad( "Cód.p.pag." ), 7, 0, 250, 20 );
		pnGeraRecDados.adic( txtCodPlanoPag, 7, 20, 80, 20 );
		pnGeraRecDados.adic( new JLabelPad( "Descrição do plano de pagto." ), 90, 0, 250, 20 );
		pnGeraRecDados.adic( txtDescPlanoPag, 90, 20, 197, 20 );

		pnGeraRecDados.adic( new JLabelPad( "Cod.Tip.Cob." ), 290, 0, 250, 20 );
		pnGeraRecDados.adic( txtCodTipoCob, 290, 20, 80, 20 );
		pnGeraRecDados.adic( new JLabelPad( "Descrição Tipo Cobrança" ), 373, 0, 250, 20 );
		pnGeraRecDados.adic( txtDescTipoCob, 373, 20, 200, 20 );

		pnGeraRecDados.adic( new JLabelPad( "Cod.Cart.Cob." ), 7, 40, 250, 20 );
		pnGeraRecDados.adic( txtCodCartCob, 7, 60, 80, 20 );
		pnGeraRecDados.adic( new JLabelPad( "Descrição da Carteira de Cob." ), 90, 40, 200, 20 );
		pnGeraRecDados.adic( txtDescCartCob, 90, 60, 197, 20 );

		pnGeraRecDados.adic( new JLabelPad( "Cód.catg." ), 290, 40, 250, 20 );
		pnGeraRecDados.adic( txtCodPlan, 290, 60, 80, 20 );
		pnGeraRecDados.adic( new JLabelPad( "Descrição da categoria" ), 373, 40, 200, 20 );
		pnGeraRecDados.adic( txtDescPlan, 373, 60, 200, 20 );

		pnGeraRecDados.adic( new JLabelPad( "Cód.c.c." ), 7, 80, 80, 20 );
		pnGeraRecDados.adic( txtCodCC, 7, 100, 80, 20 );
		pnGeraRecDados.adic( new JLabelPad( "Descrição do centro de custo" ), 90, 80, 250, 20 );
		pnGeraRecDados.adic( txtDescCC, 90, 100, 197, 20 );
		
		pnGeraRecDados.adic( new JLabelPad( "Cód.banco" ), 290, 80, 80, 20 );
		pnGeraRecDados.adic( txtCodBanco, 290, 100, 80, 20 );
		pnGeraRecDados.adic( new JLabelPad( "Descriçao do banco" ), 373, 80, 200, 20 );
		pnGeraRecDados.adic( txtDescBanco, 373, 100, 200, 20 );

		pnGeraRecDados.adic( new JLabelPad( "Nº Conta" ), 7, 120, 250, 20 );
		pnGeraRecDados.adic( txtCodConta, 7, 140, 80, 20 );
		pnGeraRecDados.adic( new JLabelPad( "Descrição da conta" ), 90, 120, 200, 20 );
		pnGeraRecDados.adic( txtDescConta, 90, 140, 197, 20 );
		
		pnGeraRecDados.adic( new JLabelPad( "Doc." ), 290, 120, 114, 20 );
		pnGeraRecDados.adic( txtDocRec, 290, 140, 277, 20 );

		pnGeraRecDados.adic( new JLabelPad( "Dt. Renegociacao" ), 7, 160, 80, 20 );
		pnGeraRecDados.adic( txtDtReneg, 7, 180, 80, 20 );
		pnGeraRecDados.adic( new JLabelPad("Total Reneg."), 90, 160, 80, 20 );
		pnGeraRecDados.adic( txtValorTotalOriginal, 90, 180, 80, 20 );
		pnGeraRecDados.adic( new JLabelPad("Desconto"), 173, 160, 80, 20 );
		pnGeraRecDados.adic( txtValorDesconto, 173, 180, 80, 20 );
		pnGeraRecDados.adic( new JLabelPad("Juros"), 256, 160, 80, 20 );
		pnGeraRecDados.adic( txtValorJuros, 256, 180, 80, 20 );
		pnGeraRecDados.adic( new JLabelPad("Adicional"), 339, 160, 80, 20 );
		pnGeraRecDados.adic( txtValorAdicional, 339, 180, 80, 20 );
		pnGeraRecDados.adic( new JLabelPad("Total Liq. Reneg."), 423, 160, 80, 20 );
		pnGeraRecDados.adic( txtValorTotalRenegociado, 423, 180, 80, 20 );

		pnGeraRecDados.adic( new JLabelPad( "Observações" ), 7, 200, 400, 20 );
		pnGeraRecDados.adic( txtObs, 7, 220, 500, 40 );
		
		txtValorTotalOriginal.setEditable( false );
		txtValorTotalRenegociado.setEditable( false );
		txtValorAdicional.addFocusListener( this );
		txtValorDesconto.addFocusListener( this );
		txtValorJuros.addFocusListener( this );
		
		txtDtReneg.setVlrDate( new Date() );
		
		tabGera.setTamColuna( 40, 0 ); // Número da parcela
		tabGera.setTamColuna( 60, 1 ); // Valor total da parcela
		tabGera.setTamColuna( 65, 2 ); // Vencimento
		tabGera.setTamColuna( 65, 3 ); // Data prevista
		tabGera.setTamColuna( 60, 4 ); // Valor do desconto
		tabGera.setTamColuna( 40, 5 ); // Cód. tipo de cobrança
		tabGera.setTamColuna( 100, 6 ); // Descrição do tipo de cobrança.
		tabGera.setTamColuna( 40, 7 ); // Código do banco.
		tabGera.setTamColuna( 100, 8 ); // Descrição do nome do banco
		tabGera.setTamColuna( 40, 9 ); // Código da carteira de cobrança.
		tabGera.setTamColuna( 100, 10 ); // Descrição da carteira de cobranaça.
		
		lcReceber.addPostListener( this );
		lcItReceber.addCarregaListener( this );
		lcTipoCob.addCarregaListener( this );
		txtCodTipoCob.addFocusListener( this );
	}
	
	private void montaListaCampos(){
		/***************
		 * FNCLIENTE *
		 ***************/
		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo( txtDescCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtCNPJCli, "CnpjCli", "CNPJ", ListaCampos.DB_SI, false ) );
		lcCli.montaSql( false, "CLIENTE", "VD" );
		lcCli.setQueryCommit( false );
		lcCli.setReadOnly( true );
		txtCodCli.setTabelaExterna( lcCli, null );
		txtCodCli.setFK( true );
		txtCodCli.setNomeCampo( "CodCli" );

		/****************
		 * FNPLANOPAG *
		 ****************/
		lcPlanoPag.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_PK, false ) );
		lcPlanoPag.add( new GuardaCampo( txtDescPlanoPag, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPag.add( new GuardaCampo( txtParcPlanoPag, "ParcPlanoPag", "Nro. parcelas", ListaCampos.DB_SI, false ) );
		lcPlanoPag.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag.setQueryCommit( false );
		lcPlanoPag.setReadOnly( true );
		txtCodPlanoPag.setTabelaExterna( lcPlanoPag, null );
		txtCodPlanoPag.setFK( true );
		txtCodPlanoPag.setNomeCampo( "CodPlanoPag" );

		/***************
		 * FNTIPOCOB *
		 ***************/
		lcTipoCob.add( new GuardaCampo( txtCodTipoCob, "CodTipoCob", "Cód.Tip.Cob", ListaCampos.DB_PK, false ) );
		lcTipoCob.add( new GuardaCampo( txtDescTipoCob, "DescTipoCob", "Descrição do tipo de cobrança", ListaCampos.DB_SI, false ) );
		lcTipoCob.add( new GuardaCampo( txtObrigCart, "ObrigCartCob", "Obriga cart.cob.", ListaCampos.DB_SI, false ) );
		lcTipoCob.montaSql( false, "TIPOCOB", "FN" );
		lcTipoCob.setQueryCommit( false );
		lcTipoCob.setReadOnly( true );
		txtCodTipoCob.setTabelaExterna( lcTipoCob, null );
		txtCodTipoCob.setFK( true );
		txtCodTipoCob.setNomeCampo( "CodTipoCob" );

		/***************
		 * FNCARTCOB *
		 ***************/
		lcCartCob.add( new GuardaCampo( txtCodCartCob, "CodCartCob", "Cód.Cart.Cob.", ListaCampos.DB_PK, false ) );
		// lcCartCob.add( new GuardaCampo( txtCodBanco, "CodBanco", "Cód.banco", ListaCampos.DB_PF, false ) );
		lcCartCob.add( new GuardaCampo( txtDescCartCob, "DescCartCob", "Descrição da carteira de cobrança", ListaCampos.DB_SI, false ) );
		lcCartCob.montaSql( false, "CARTCOB", "FN" );
		lcCartCob.setQueryCommit( false );
		lcCartCob.setReadOnly( true );
		txtCodCartCob.setTabelaExterna( lcCartCob, null );
		txtCodCartCob.setFK( true );
		txtCodCartCob.setNomeCampo( "CodCartCob" );

		/*************
		 * FNBANCO *
		 *************/
		lcBanco.add( new GuardaCampo( txtCodBanco, "CodBanco", "Cód.banco", ListaCampos.DB_PK, false ) );
		lcBanco.add( new GuardaCampo( txtDescBanco, "NomeBanco", "Nome do banco", ListaCampos.DB_SI, false ) );
		lcBanco.montaSql( false, "BANCO", "FN" );
		lcBanco.setQueryCommit( false );
		lcBanco.setReadOnly( true );
		txtCodBanco.setTabelaExterna( lcBanco, null );
		txtCodBanco.setFK( true );
		txtCodBanco.setNomeCampo( "CodBanco" );

		/**********************
		 * FNCONTA *
		 **********************/
		lcConta.add( new GuardaCampo( txtCodConta, "NumConta", "Nº Conta", ListaCampos.DB_PK, false ) );
		lcConta.add( new GuardaCampo( txtDescConta, "DescConta", "Descrição da conta", ListaCampos.DB_SI, false ) );
		lcConta.montaSql( false, "CONTA", "FN" );
		lcConta.setQueryCommit( false );
		lcConta.setReadOnly( true );
		txtCodConta.setTabelaExterna( lcConta, null );
		txtCodConta.setFK( true );
		txtCodConta.setNomeCampo( "NumConta" );
		txtDescConta.setTabelaExterna( lcConta, null );
		txtDescConta.setLabel( "Descrição da Conta" );
		
		/************************
		 * FNPLANEJAMENTO       *
		 ************************/
		lcPlan.add( new GuardaCampo( txtCodPlan, "CodPlan", "Cód.plan.", ListaCampos.DB_PK, false ) );
		lcPlan.add( new GuardaCampo( txtDescPlan, "DescPlan", "Descrição do planejamento", ListaCampos.DB_SI, false ) );
		lcPlan.setWhereAdic( "TIPOPLAN = 'R' AND NIVELPLAN = 6" );
		lcPlan.montaSql( false, "PLANEJAMENTO", "FN" );
		lcPlan.setReadOnly( true );
		txtCodPlan.setTabelaExterna( lcPlan, null );
		txtCodPlan.setFK( true );
		txtCodPlan.setNomeCampo( "CodPlan" );
		
		/***************
		 * FNCC        *
		 ***************/
		lcCC.add( new GuardaCampo( txtCodCC, "CodCC", "Cód.c.c.", ListaCampos.DB_PK, false ) );
		lcCC.add( new GuardaCampo( txtSiglaCC, "SiglaCC", "Sigla", ListaCampos.DB_SI, false ) );
		lcCC.add( new GuardaCampo( txtDescCC, "DescCC", "Descrição", ListaCampos.DB_SI, false ) );
		lcCC.add( new GuardaCampo( txtAnoCC, "AnoCC", "Ano-Base", ListaCampos.DB_PK, false ) );
		lcCC.setReadOnly( true );
		lcCC.setQueryCommit( false );
		lcCC.setWhereAdic( "NIVELCC=10" );
		lcCC.montaSql( false, "CC", "FN" );
		txtCodCC.setTabelaExterna( lcCC, null );
		txtCodCC.setFK( true );
		txtCodCC.setNomeCampo( "CodCC" );
		txtAnoCC.setTabelaExterna( lcCC, null );
		txtAnoCC.setFK( true );
		txtAnoCC.setNomeCampo( "AnoCC" );

		lcCC.addCarregaListener( this );

		
		/***************
		 * FNRECEBER *
		 ***************/
		lcReceber.add( new GuardaCampo( txtCodRec, "CodRec", "Cód.rec.", ListaCampos.DB_PK, true ) );
		lcReceber.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_FK, true ) );
		lcReceber.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_FK, true ) );
		lcReceber.add( new GuardaCampo( txtCodBanco, "CodBanco", "Cód.banco", ListaCampos.DB_FK, false ) );
		lcReceber.add( new GuardaCampo( txtCodTipoCob, "CodTipoCob", "Cód.tp.cob.", ListaCampos.DB_FK, false ) );
		/*lcReceber.add( new GuardaCampo( txtVlrParcRec, "VlrParcRec", "Valor parc.", ListaCampos.DB_SI, true ) );
		lcReceber.add( new GuardaCampo( txtVlrParcRec, "VlrAPagRec", "Valor a rec.", ListaCampos.DB_SI, true ) );
		lcReceber.add( new GuardaCampo( txtVlrParcRec, "VlrRec", "Valor tot.", ListaCampos.DB_SI, true ) );*/
		lcReceber.add( new GuardaCampo( txtValorTotalRenegociado, "VlrParcRec", "Valor parc.", ListaCampos.DB_SI, true ) );
		lcReceber.add( new GuardaCampo( txtValorTotalRenegociado, "VlrAPagRec", "Valor a rec.", ListaCampos.DB_SI, true ) );
		lcReceber.add( new GuardaCampo( txtValorTotalRenegociado, "VlrRec", "Valor tot.", ListaCampos.DB_SI, true ) );
		lcReceber.add( new GuardaCampo( txtDtReneg, "DataRec", "Dt.emissão", ListaCampos.DB_SI, true ) );
		lcReceber.add( new GuardaCampo( txtDocRec, "DocRec", "N.doc.", ListaCampos.DB_SI, true ) );
		lcReceber.add( new GuardaCampo( txtObs, "ObsRec", "Obs.", ListaCampos.DB_SI, false ) );
		lcReceber.add( new GuardaCampo( txtStatus, "StatusRec", "Status", ListaCampos.DB_SI, false ) );
		lcReceber.add( new GuardaCampo( txtCodConta, "NumConta", "Cód.Conta", ListaCampos.DB_FK, txtDescConta, false ) );
		
		lcReceber.add( new GuardaCampo( txtCodEmpRengRec, "CodEmpRR", "Emp. Reneg. Rec.", ListaCampos.DB_SI, false ) );
		lcReceber.add( new GuardaCampo( txtCodFilialRenegRec, "CodFilialRR", "Filial Reneg. Rec.", ListaCampos.DB_SI, false ) );
		lcReceber.add( new GuardaCampo( txtCodRenegReg, "CodRenegRec", "Status", ListaCampos.DB_SI, false ) );

		lcReceber.add( new GuardaCampo( txtCodPlan, "CodPlan", "Cód.Plan.", ListaCampos.DB_FK, txtDescPlan, false ) ); 
		lcReceber.add( new GuardaCampo( txtAnoCC, "AnoCC", "Ano.C.C.", ListaCampos.DB_SI, txtDescCC, false ) );
		lcReceber.add( new GuardaCampo( txtCodCC, "CodCC", "Cód.C.C.", ListaCampos.DB_FK, txtDescCC, false ) );

		lcReceber.montaSql( true, "RECEBER", "FN" );

		/************************
		 * FNTIPOCOB - DETALHE *
		 ************************/
		lcTipoCobItRec.add( new GuardaCampo( txtCodTipoCobItRec, "CodTipoCob", "Cód.Tip.Cob", ListaCampos.DB_PK, false ) );
		lcTipoCobItRec.add( new GuardaCampo( txtDescTipoCobItRec, "DescTipoCob", "Descrição tipo de cobrança", ListaCampos.DB_SI, false ) );
		lcTipoCobItRec.montaSql( false, "TIPOCOB", "FN" );
		lcTipoCobItRec.setQueryCommit( false );
		lcTipoCobItRec.setReadOnly( true );
		txtCodTipoCobItRec.setTabelaExterna( lcTipoCobItRec, null );
		txtCodTipoCobItRec.setFK( true );
		txtCodTipoCobItRec.setNomeCampo( "CodTipoCob" );
		txtDescTipoCobItRec.setTabelaExterna( lcTipoCobItRec, null );
		txtDescTipoCobItRec.setLabel( "Descrição do tipo de cobrança" );

		/***************
		 * FNCARTCOB *
		 ***************/
		txtCodCartCobItRec.setNomeCampo( "CodCartCob" );
		lcCartCobItRec.add( new GuardaCampo( txtCodCartCobItRec, "CodCartCob", "Cód.Cart.Cob.", ListaCampos.DB_PK, false ) );
		lcCartCobItRec.add( new GuardaCampo( txtDescCartCobItRec, "DescCartCob", "Descrição da carteira de cobrança", ListaCampos.DB_SI, false ) );
		lcCartCobItRec.setWhereAdicSubSel( "CODBANCO=master.CODBANCO" );
		lcCartCobItRec.montaSql( false, "CARTCOB", "FN" );
		lcCartCobItRec.setQueryCommit( false );
		lcCartCobItRec.setReadOnly( true );
		txtCodCartCobItRec.setTabelaExterna( lcCartCobItRec, null );
		txtCodCartCobItRec.setListaCampos( lcCartCobItRec );
		txtDescCartCobItRec.setListaCampos( lcCartCobItRec );
		txtDescCartCobItRec.setLabel( "Descrição da carteira de cobrança" );
		txtCodCartCobItRec.setFK( true );

		/**********************
		 * FNBANCO - DETALE *
		 **********************/
		lcBancoItRec.add( new GuardaCampo( txtCodBancoItRec, "CodBanco", "Cód.Tip.Cob", ListaCampos.DB_PK, false ) );
		lcBancoItRec.add( new GuardaCampo( txtDescBancoItRec, "NomeBanco", "Descrição tipo de cobrança", ListaCampos.DB_SI, false ) );
		lcBancoItRec.montaSql( false, "BANCO", "FN" );
		lcBancoItRec.setQueryCommit( false );
		lcBancoItRec.setReadOnly( true );
		txtCodBancoItRec.setTabelaExterna( lcBancoItRec, null );
		txtCodBancoItRec.setFK( true );
		txtCodBancoItRec.setNomeCampo( "CodBanco" );
		txtDescBancoItRec.setTabelaExterna( lcBancoItRec, null );
		txtDescBancoItRec.setLabel( "Descrição do Banco" );

		/*****************
		 * FNITRECEBER *
		 *****************/
		txtNParcRec.setNomeCampo( "NParcRec" );
		lcItReceber.add( new GuardaCampo( txtNParcRec, "NParcItRec", "N.parc.", ListaCampos.DB_PK, false ) );
		lcItReceber.add( new GuardaCampo( txtVlrParcItRec, "VlrParcItRec", "Valor tot.", ListaCampos.DB_SI, false ) );
		lcItReceber.add( new GuardaCampo( txtDtVencItRec, "DtVencItRec", "Dt.vencto.", ListaCampos.DB_SI, false ) );
		lcItReceber.add( new GuardaCampo( txtDtPrevItRec, "DtPrevItRec", "Dt.Prev.", ListaCampos.DB_SI, false ) );
		lcItReceber.add( new GuardaCampo( txtVlrDescItRec, "VlrDescItRec", "Valor desc.", ListaCampos.DB_SI, false ) );
		lcItReceber.add( new GuardaCampo( txtCodTipoCobItRec, "CodTipoCob", "Cód.Tipo.Cob", ListaCampos.DB_FK, txtDescTipoCobItRec, false ) );
		lcItReceber.add( new GuardaCampo( txtCodBancoItRec, "CodBanco", "Cód.Banco", ListaCampos.DB_FK, txtDescBancoItRec, false ) );
		lcItReceber.add( new GuardaCampo( txtCodCartCobItRec, "CodCartCob", "Cód.Cart.Cob.", ListaCampos.DB_FK, txtDescCartCobItRec, false ) );

		lcItReceber.montaSql( false, "ITRECEBER", "FN" );
		lcItReceber.setQueryCommit( false );
		txtNParcRec.setListaCampos( lcItReceber );
		txtVlrParcItRec.setListaCampos( lcItReceber );
		txtVlrDescItRec.setListaCampos( lcItReceber );
		txtDtVencItRec.setListaCampos( lcItReceber );
		txtCodTipoCobItRec.setListaCampos( lcItReceber );
		txtCodBancoItRec.setListaCampos( lcItReceber );
		txtCodCartCobItRec.setListaCampos( lcItReceber );
	}
	
	public void carregaGrid( List<GridRenegRec> grid ) {
		if ( grid != null ) {
			tabSeleciona.limpa();

			int row = 0;
			for ( GridRenegRec g : grid ) {
				tabSeleciona.adicLinha();

				if ( "CR".equals( g.getStatus() ) ) {
					imgColuna = imgCancelado;
				}else if ( "RR".equals( g.getStatus() ) ) {
					imgColuna = imgEmRenegociacao;
				}else if ( "RN".equals( g.getStatus() ) ) {
					imgColuna = imgRenegociado;
				}else if ( "RP".equals( g.getStatus() ) && g.getValorAReceber().doubleValue() == 0 ) {
					imgColuna = imgPago;
				}else if ( g.getValorPago().doubleValue() > 0 ) {
					imgColuna = imgPagoParcial;
				}else if ( g.getDataVencimento().before( Calendar.getInstance().getTime() ) ) {
					imgColuna = imgVencido;
				}else if ( g.getDataVencimento().after( Calendar.getInstance().getTime() ) ) {
					imgColuna = imgNaoVencido;
				}

				tabSeleciona.setValor( new Boolean( false ), 			row, RECEBER.SEL.ordinal() );
				tabSeleciona.setValor( imgColuna, 					row, RECEBER.STATUS.ordinal() );
				tabSeleciona.setValor( g.getDataVencimento(), 		row, RECEBER.DTVENC.ordinal() );
				tabSeleciona.setValor( g.getCodigoReceber(), 			row, RECEBER.CODREC.ordinal() );
				tabSeleciona.setValor( g.getParcela(), 				row, RECEBER.NPARCITREC.ordinal() );
				tabSeleciona.setValor( g.getDocumentoLancamento(), 	row, RECEBER.DOCLANCA.ordinal() );
				tabSeleciona.setValor( g.getCodigoCliente(), 			row, RECEBER.CODCLI.ordinal() );
				tabSeleciona.setValor( g.getRazaoCliente(), 			row, RECEBER.RAZCLI.ordinal() );
				tabSeleciona.setValor( g.getDocumentoVenda(), 		row, RECEBER.DOCVENDA.ordinal() );
				//tabSeleciona.setValor( g.getValorParcela(), 			row, RECEBER.VLRPARC.ordinal() );
				tabSeleciona.setValor( g.getValorAReceber(), 			row, RECEBER.VLRPARC.ordinal() );
				tabSeleciona.setValor( g.getDataPagamento(), 			row, RECEBER.DTPAGTO.ordinal() );
				tabSeleciona.setValor( g.getValorPago(), 				row, RECEBER.VLRPAGO.ordinal() );
				tabSeleciona.setValor( g.getValorDesconto(), 			row, RECEBER.VLRDESC.ordinal() );
				tabSeleciona.setValor( g.getValorJuros(), 			row, RECEBER.VLRJUROS.ordinal() );
				tabSeleciona.setValor( g.getConta(), 					row, RECEBER.NUMCONTA.ordinal() );
				tabSeleciona.setValor( g.getDescricaoConta(), 		row, RECEBER.DESCCONTA.ordinal() );
				tabSeleciona.setValor( g.getPlanejamento(), 			row, RECEBER.CODPLAN.ordinal() );
				tabSeleciona.setValor( g.getDescricaoPlanejamento(), 	row, RECEBER.DESCPLAN.ordinal() );
				tabSeleciona.setValor( g.getBanco(), 					row, RECEBER.CODBANCO.ordinal() );
				tabSeleciona.setValor( g.getNomeBanco(), 				row, RECEBER.NOMEBANCO.ordinal() );
				tabSeleciona.setValor( g.getObservacao(), 			row, RECEBER.OBS.ordinal() );
				row++;
			}
		}
	}

	private void selecionarTodos() {
		for ( int row = 0; row < tabSeleciona.getNumLinhas(); row++ ) {
			tabSeleciona.setValor( new Boolean( true ), row, RECEBER.SEL.ordinal() );
		}
		
		calculaSelecionados();
	}

	private void selecionarNenhum() {
		for ( int row = 0; row < tabSeleciona.getNumLinhas(); row++ ) {
			tabSeleciona.setValor( new Boolean( false ), row, RECEBER.SEL.ordinal() );
		}
		
		calculaSelecionados();
	}
	
	private void calculaSelecionados(){
		BigDecimal totalSelecionado = new BigDecimal( 0 );
		for ( int row = 0; row < tabSeleciona.getNumLinhas(); row++ ) {
			Boolean selecionado = (Boolean)tabSeleciona.getValor( row, RECEBER.SEL.ordinal() ) ;
			if (selecionado){
				totalSelecionado = totalSelecionado.add( (BigDecimal) tabSeleciona.getValor( row, RECEBER.VLRPARC.ordinal() ) );
			}
		}
		txtTotalSelecionado.setVlrBigDecimal( totalSelecionado );
	}
	
	private Integer getCodigos(Integer codFilial, String siglaTabela){
		PreparedStatement ps = null;
		ResultSet rs = null;

		Integer retValue = 0;
		try {

			ps = con.prepareStatement( "SELECT * FROM SPGERANUM(?,?,?)" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2,  codFilial);
			ps.setString( 3,  siglaTabela);
			rs = ps.executeQuery();

			if ( rs.next() ) {
				retValue = rs.getInt(  1 );
			}

			rs.close();
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao recuperar código.\n" + err.getMessage(), true, con, err );
		}
		
		return retValue;
	}
	
	private void testaCodRec() { // Traz o verdadeiro número do codrec
		txtCodRec.setVlrString( getCodigos( ListaCampos.getMasterFilial( "FNRECEBER" ), "RC" ).toString() );
	}

	private void setCarteira() {

		if ( ( txtCodBanco.getVlrString() != null && txtCodBanco.getVlrString().trim().length() > 0 ) && 
				( txtCodCartCob.getVlrString() != null && txtCodCartCob.getVlrString().trim().length() > 0 ) ) {

			try {

				String sql = "UPDATE FNITRECEBER SET CODCARTCOB=?, CODEMPCB=?, CODFILIALCB=? WHERE CODEMP=? AND CODFILIAL=? AND CODREC=?";

				PreparedStatement ps = con.prepareStatement( sql );
				ps.setString( 1, txtCodCartCob.getVlrString() );
				ps.setInt( 2, Aplicativo.iCodEmp );
				ps.setInt( 3, ListaCampos.getMasterFilial( "FNITRECEBER" ) );
				ps.setInt( 4, Aplicativo.iCodEmp );
				ps.setInt( 5, ListaCampos.getMasterFilial( "FNRECEBER" ) );
				ps.setInt( 6, txtCodRec.getVlrInteger() );
				ps.executeUpdate();

				ps.close();

				con.commit();

				lcReceber.carregaDados();
			} catch ( Exception e ) {
				e.printStackTrace();
				Funcoes.mensagemErro( this, "Erro ao definir carteira de cobrança.\n" + e.getMessage() );
			}
		}
	}

	private void gerarRenegociacao(){
		if(lcReceber.getStatus() != ListaCampos.LCS_INSERT ){
			dispose();
			return;
		}
		
		if(!isValido())
			return;
		
		StringBuilder sqlRenegRec = new StringBuilder();
		sqlRenegRec.append( "INSERT INTO FNRENEGREC (CODEMP, CODFILIAL, CODRENEGREC, DTRENEGREC, VLRORIGINAL, VLRDESCONTO, VLRADICIONAL, VLRJUROS, VLRLIQRENEGREC, ");
		sqlRenegRec.append( "CODEMPPG, CODFILIALPG, CODPLANOPAG, OBSRENEGREC)");
		sqlRenegRec.append( "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)" );
		Integer codRenegRec = getCodigos( Aplicativo.iCodFilial, "RR" );
		
		try {
			PreparedStatement psRenegRec = con.prepareStatement( sqlRenegRec.toString() );
			psRenegRec.setInt( 1, Aplicativo.iCodEmp );
			psRenegRec.setInt( 2, Aplicativo.iCodFilial );
			psRenegRec.setInt( 3, codRenegRec );
			psRenegRec.setDate( 4, Funcoes.dateToSQLDate( txtDtReneg.getVlrDate() ) );
			psRenegRec.setBigDecimal( 5, txtValorTotalOriginal.getVlrBigDecimal() );
			psRenegRec.setBigDecimal( 6, txtValorDesconto.getVlrBigDecimal() );
			psRenegRec.setBigDecimal( 7, txtValorAdicional.getVlrBigDecimal() );
			psRenegRec.setBigDecimal( 8, txtValorJuros.getVlrBigDecimal() );
			psRenegRec.setBigDecimal( 9, txtValorTotalRenegociado.getVlrBigDecimal() );
			psRenegRec.setInt( 10, lcPlanoPag.getCodEmp() );
			psRenegRec.setInt( 11, lcPlanoPag.getCodFilial() );
			psRenegRec.setInt( 12, txtCodPlanoPag.getVlrInteger() );
			psRenegRec.setString( 13, txtObs.getVlrString() );
			
			psRenegRec.executeUpdate();
			psRenegRec.close();
			con.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		
		StringBuilder sqlItRenegRec = new StringBuilder();
		sqlItRenegRec.append( "INSERT INTO FNITRENEGREC (CODEMP, CODFILIAL, CODRENEGREC, CODITRENEGREC, CODEMPIR, CODFILIALIR, CODREC, NPARCITREC)  ");
		sqlItRenegRec.append( "VALUES (?,?,?,?,?,?,?,?)" );
		for ( int row = 0; row < tabSeleciona.getNumLinhas(); row++ ) {
			if ( !(Boolean)tabSeleciona.getValor( row, RECEBER.SEL.ordinal() ) )
				continue;
			
			try {
				PreparedStatement psItRenegRec = con.prepareStatement( sqlItRenegRec.toString() );
				psItRenegRec.setInt( 1, Aplicativo.iCodEmp );
				psItRenegRec.setInt( 2, Aplicativo.iCodFilial );
				psItRenegRec.setInt( 3, codRenegRec );
				psItRenegRec.setInt( 4, getCodigos( Aplicativo.iCodFilial, "RI" ) );
				psItRenegRec.setInt( 5, Aplicativo.iCodEmp );
				psItRenegRec.setInt( 6, ListaCampos.getMasterFilial( "FNITRECEBER" ) );
				psItRenegRec.setInt( 7, (Integer) tabSeleciona.getValor( row, RECEBER.CODREC.ordinal() ) );
				psItRenegRec.setInt( 8, (Integer) tabSeleciona.getValor( row, RECEBER.NPARCITREC.ordinal() ) );
				
				psItRenegRec.executeUpdate();
				psItRenegRec.close();
				con.commit();
			} catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		
		txtCodEmpRengRec.setVlrInteger( Aplicativo.iCodEmp );
		txtCodFilialRenegRec.setVlrInteger( Aplicativo.iCodFilial );
		txtCodRenegReg.setVlrInteger( codRenegRec );
		
		if ( lcReceber.post() ) {
			setCarteira();
			
			try {
				PreparedStatement psItReceber = con.prepareStatement( "update fnitreceber set STATUSITREC = 'RR' where codemp = ? and codfilial = ? and codrec = ? " );
				psItReceber.setInt( 1, Aplicativo.iCodEmp );
				psItReceber.setInt( 2, Aplicativo.iCodFilial );
				psItReceber.setInt( 3, txtCodRec.getVlrInteger() );
				psItReceber.executeUpdate();
				con.commit();
			} catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
	}

	private boolean isValido() {

		if ( txtCodCli.getVlrString().trim().length() == 0 ) {
			Funcoes.mensagemErro( this, "Cód.cli. é requerido!" );
			txtCodCli.requestFocus();
			return false;
		}
		if ( txtCodPlanoPag.getVlrString().trim().length() == 0 ) {
			Funcoes.mensagemErro( this, "Cód.p.pag. é requerido!" );
			txtCodPlanoPag.requestFocus();
			return false;
		}
		if ( "S".equalsIgnoreCase( txtObrigCart.getVlrString() ) ) {
			if ( txtCodBanco.getVlrString().trim().length() == 0 ) {
				Funcoes.mensagemErro( this, "Cód.banco é requerido!" );
				txtCodBanco.requestFocus();
				return false;
			}
			else if ( txtCodCartCob.getVlrString().trim().length() == 0 ) {
				Funcoes.mensagemErro( this, "Cód.cart.cob. é requerido!" );
				txtCodCartCob.requestFocus();
				return false;
			}
		}
		if ( txtDtReneg.getVlrString().length() < 10 ) {
			Funcoes.mensagemErro( this, "Data de emissão é requerido!" );
			txtDtReneg.requestFocus();
			return false;
		}
		if ( txtDocRec.getVlrString().trim().length() == 0 ) {
			Funcoes.mensagemErro( this, "Doc. é requerido!" );
			txtDocRec.requestFocus();
			return false;
		}
		return true;
	}
	
	private Map<String, Integer> getPrefere() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer anocc = null;
		Integer codhistrec = null;

		Map<String, Integer> retorno = new HashMap<String, Integer>();

		try {

			ps = con.prepareStatement( "SELECT ANOCENTROCUSTO,CODHISTREC FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				anocc = rs.getInt( "ANOCENTROCUSTO" );
				codhistrec = rs.getInt( "CODHISTREC" );
			}

			retorno.put( "codhistrec", codhistrec );
			retorno.put( "anocc", anocc );

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar o ano-base para o centro de custo.\n" + err.getMessage(), true, con, err );
		} finally {
			ps = null;
			rs = null;
		}
		return retorno;
	}
	
	private void editar() {

		PreparedStatement ps = null;
		StringBuffer sSQL = new StringBuffer();
		Object[] sVals = new Object[ 18 ];
		Object[] oRets = null;
		DLEditaRec dl = null;
		int iCodRec = 0;
		int iNParcItRec = 0;

		try {
			int iLin = tabGera.getLinhaSel();

			if ( tabGera.getLinhaSel() > -1 ) {
				iCodRec = txtCodRec.getVlrInteger();
				iNParcItRec = (Integer) tabGera.getValor( iLin, 0 );

				dl = new DLEditaRec( this, true );

				sVals = getTabManutValores();

				dl.setConexao( con );
				dl.setValores( sVals );
				dl.setVisible( true );

				if ( dl.OK ) {

					oRets = dl.getValores();

					sSQL.append( "UPDATE FNITRECEBER SET " );
					sSQL.append( "NUMCONTA=?,CODEMPCA=?,CODFILIALCA=?,CODPLAN=?,CODEMPPN=?,CODFILIALPN=?," );
					sSQL.append( "ANOCC=?,CODCC=?,CODEMPCC=?,CODFILIALCC=?,DOCLANCAITREC=?,VLRJUROSITREC=?,VLRDEVITREC=?," );
					sSQL.append( "VLRDESCITREC=?,DTVENCITREC=?,OBSITREC=?,CODEMPBO=?,CODFILIALBO=?,CODBANCO=?," );
					sSQL.append( "CODEMPTC=?,CODFILIALTC=?,CODTIPOCOB=?," );
					sSQL.append( "CODEMPCB=?,CODFILIALCB=?,CODCARTCOB=?, DESCPONT=?, DTPREVITREC=?, VLRPARCITREC=? " );
					sSQL.append( "WHERE CODREC=? AND NPARCITREC=? AND CODEMP=? AND CODFILIAL=?" );

					try {
						ps = con.prepareStatement( sSQL.toString() );

						if ( "".equals( oRets[ EColRet.NUMCONTA.ordinal() ] ) ) {
							ps.setNull( 1, Types.CHAR );
							ps.setNull( 2, Types.INTEGER );
							ps.setNull( 3, Types.INTEGER );
						} else {
							ps.setString( 1, (String) oRets[ EColRet.NUMCONTA.ordinal() ] );
							ps.setInt( 2, Aplicativo.iCodEmp );
							ps.setInt( 3, ListaCampos.getMasterFilial( "FNCONTA" ) );
						}

						if ( "".equals( String.valueOf( oRets[ EColRet.CODPLAN.ordinal() ] ).trim() ) ) {
							ps.setNull( 4, Types.CHAR );
							ps.setNull( 5, Types.INTEGER );
							ps.setNull( 6, Types.INTEGER );
						} else {
							ps.setString( 4, (String) oRets[ EColRet.CODPLAN.ordinal() ] );
							ps.setInt( 5, Aplicativo.iCodEmp );
							ps.setInt( 6, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
						}

						if ( "".equals( String.valueOf( oRets[ EColRet.CODCC.ordinal() ] ).trim() ) ) {
							ps.setNull( 7, Types.INTEGER );
							ps.setNull( 8, Types.CHAR );
							ps.setNull( 9, Types.INTEGER );
							ps.setNull( 10, Types.INTEGER );
						} else {
							ps.setInt( 7, 0 );
							ps.setString( 8, (String) oRets[ EColRet.CODCC.ordinal() ] );
							ps.setInt( 9, Aplicativo.iCodEmp );
							ps.setInt( 10, ListaCampos.getMasterFilial( "FNCC" ) );
						}

						if ( "".equals( String.valueOf( oRets[ EColRet.DOC.ordinal() ] ).trim() ) ) {
							ps.setNull( 11, Types.CHAR );
						} else {
							ps.setString( 11, (String) oRets[ EColRet.DOC.ordinal() ] );
						}

						if ( "".equals( oRets[ EColRet.VLRJUROS.ordinal() ] ) ) {
							ps.setNull( 12, Types.DECIMAL );
						} else {
							ps.setBigDecimal( 12, (BigDecimal) oRets[ EColRet.VLRJUROS.ordinal() ] );
						}

						if ( "".equals( oRets[ EColRet.VLRDEVOLUCAO.ordinal() ] ) ) {
							ps.setNull( 13, Types.DECIMAL );
						} else {
							ps.setBigDecimal( 13, (BigDecimal) oRets[ EColRet.VLRDEVOLUCAO.ordinal() ] );
						}

						if ( "".equals( oRets[ EColRet.VLRDESC.ordinal() ] ) ) {
							ps.setNull( 14, Types.DECIMAL );
						} else {
							ps.setBigDecimal( 14, (BigDecimal) ( oRets[ EColRet.VLRDESC.ordinal() ] ) );
						}

						if ( "".equals( oRets[ EColRet.DTVENC.ordinal() ] ) ) {
							ps.setNull( 15, Types.DECIMAL );
						} else {
							ps.setDate( 15, Funcoes.dateToSQLDate( (java.util.Date) oRets[ EColRet.DTVENC.ordinal() ] ) );
						}

						if ( "".equals( oRets[ EColRet.OBS.ordinal() ] ) ) {
							ps.setNull( 16, Types.CHAR );
						} else {
							ps.setString( 16, (String) oRets[ EColRet.OBS.ordinal() ] );
						}

						if ( "".equals( oRets[ EColRet.CODBANCO.ordinal() ] ) ) {
							ps.setNull( 17, Types.INTEGER );
							ps.setNull( 18, Types.INTEGER );
							ps.setNull( 19, Types.CHAR );
						} else {
							ps.setInt( 17, Aplicativo.iCodEmp );
							ps.setInt( 18, ListaCampos.getMasterFilial( "FNBANCO" ) );
							ps.setString( 19, (String) oRets[ EColRet.CODBANCO.ordinal() ] );
						}

						if ( "".equals( oRets[ EColRet.CODTPCOB.ordinal() ] ) ) {
							ps.setNull( 20, Types.INTEGER );
							ps.setNull( 21, Types.INTEGER );
							ps.setNull( 22, Types.INTEGER );
						} else {
							ps.setInt( 20, Aplicativo.iCodEmp );
							ps.setInt( 21, ListaCampos.getMasterFilial( "FNTIPOCOB" ) );
							ps.setInt( 22, Integer.parseInt( (String) oRets[ EColRet.CODTPCOB.ordinal() ] ) );
						}

						if ( "".equals( oRets[ EColRet.CODCARTCOB.ordinal() ] ) ) {
							ps.setNull( 23, Types.INTEGER );
							ps.setNull( 24, Types.INTEGER );
							ps.setNull( 25, Types.CHAR );
						} else {
							ps.setInt( 23, Aplicativo.iCodEmp );
							ps.setInt( 24, ListaCampos.getMasterFilial( "FNCARTCOB" ) );
							ps.setString( 25, ( (String) oRets[ EColRet.CODCARTCOB.ordinal() ] ) );
						}
						if ( "".equals( oRets[ EColRet.DESCPONT.ordinal() ] ) ) {
							ps.setNull( 26, Types.CHAR );
						} else {
							ps.setString( 26, ( (String) oRets[ EColRet.DESCPONT.ordinal() ] ) );
						}
						if ( oRets[ EColRet.DTPREV.ordinal() ] == null || "".equals( oRets[ EColRet.DTPREV.ordinal() ] ) ) {
							ps.setNull( 27, Types.DECIMAL );
						} else {
							ps.setDate( 27, Funcoes.dateToSQLDate( (java.util.Date) oRets[ EColRet.DTPREV.ordinal() ] ) );
						}
						
						ps.setBigDecimal( 28, (BigDecimal) ( oRets[ EColRet.VLRPARC.ordinal() ] ) );
						ps.setInt( 29, iCodRec );
						ps.setInt( 30, iNParcItRec );
						ps.setInt( 31, Aplicativo.iCodEmp );
						ps.setInt( 32, ListaCampos.getMasterFilial( "FNRECEBER" ) );
						ps.executeUpdate();
						con.commit();

					} catch ( SQLException err ) {
						Funcoes.mensagemErro( this, "Erro ao editar parcela!\n" + err.getMessage(), true, con, err );
						err.printStackTrace();
					}
				}

				dl.dispose();	
				lcReceber.carregaDados();
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			ps = null;
			sSQL = null;
			sVals = new String[ 13 ];
			oRets = null;
			dl = null;
		}
	}
	
	private Object[] getTabManutValores() {

		Object[] sRet = new Object[ EColEdit.values().length ];
		Integer iCodRec;
		Integer iNParcItRec;
		Integer codhistrec = null;

		try {
			int iLin = tabGera.getLinhaSel();

			sRet[ EColEdit.CODCLI.ordinal() ] = txtCodCli.getVlrInteger();
			sRet[ EColEdit.RAZCLI.ordinal() ] = txtDescCli.getVlrString();
			sRet[ EColEdit.NUMCONTA.ordinal() ] = txtCodConta.getVlrString();
			sRet[ EColEdit.CODPLAN.ordinal() ] = txtCodPlan.getVlrString();
			sRet[ EColEdit.CODCC.ordinal() ] = txtCodCC.getVlrString();
			
//			if ( "".equals( tabManut.getValor( iLin, EColTabManut.DOCLANCA.ordinal() ) ) ) {
//				sRet[ EColEdit.DOC.ordinal() ] = String.valueOf( tabManut.getValor( iLin, EColTabManut.DOCVENDA.ordinal() ) );
//			} else {
//				sRet[ EColEdit.DOC.ordinal() ] = tabManut.getValor( iLin, EColTabManut.DOCLANCA.ordinal() );
//			}
			
			sRet[ EColEdit.DOC.ordinal() ] = txtDocRec.getVlrString();
			sRet[ EColEdit.DTEMIS.ordinal() ] = txtDtReneg.getVlrDate();// Funcoes.strDateToDate( tabManut.getValor( iLin, EColTabManut.DTEMIT.ordinal() ).toString() );
			sRet[ EColEdit.DTVENC.ordinal() ] = Funcoes.strDateToDate( tabGera.getValor( iLin, 3 ).toString() );
			sRet[ EColEdit.VLRJUROS.ordinal() ] = txtValorJuros.getVlrBigDecimal();
			sRet[ EColEdit.VLRDEVOLUCAO.ordinal() ] = new BigDecimal(0);
			sRet[ EColEdit.VLRDESC.ordinal() ] = txtValorDesconto.getVlrBigDecimal();
			sRet[ EColEdit.VLRPARC.ordinal() ] = txtVlrParcItRec.getVlrBigDecimal();
			sRet[ EColEdit.NPARCITREC.ordinal() ] = tabGera.getValor( iLin, 0 );

			sRet[ EColEdit.CODREC.ordinal() ] = txtCodRec.getVlrInteger();

			sRet[ EColEdit.OBS.ordinal() ] = txtObs.getVlrString();

			sRet[ EColEdit.CODBANCO.ordinal() ] = tabGera.getValor( iLin, 8 );
			sRet[ EColEdit.CODTPCOB.ordinal() ] = String.valueOf( tabGera.getValor( iLin, 6 ) );
			sRet[ EColEdit.DESCTPCOB.ordinal() ] = String.valueOf( tabGera.getValor( iLin, 7 ) );
			sRet[ EColEdit.CODCARTCOB.ordinal() ] = String.valueOf( tabGera.getValor( iLin, 10 ) );
			sRet[ EColEdit.DESCCARTCOB.ordinal() ] = String.valueOf( tabGera.getValor( iLin, 11 ) );
			sRet[ EColEdit.DESCPONT.ordinal() ] = "";
//			sRet[ EColEdit.DTPREV.ordinal() ] = Funcoes.strDateToDate( tabManut.getValor( iLin, EColTabManut.DTPREV.ordinal() ).toString() );

		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return sRet;
	}
	
	public void setConexao( DbConnection cn ) {
		super.setConexao( cn );
		lcCli.setConexao( cn );
		lcPlanoPag.setConexao( cn );
		lcReceber.setConexao( cn );
		lcItReceber.setConexao( cn );
		lcTipoCob.setConexao( cn );
		lcBanco.setConexao( cn );
		lcCartCob.setConexao( cn );
		lcTipoCobItRec.setConexao( cn );
		lcBancoItRec.setConexao( cn );
		lcCartCobItRec.setConexao( cn );
		lcConta.setConexao( cn );
		lcPlan.setConexao( cn );
		lcCC.setConexao( cn );
		
		lcReceber.insert( true );

		prefere = getPrefere();
	}
	
	@ Override
	public void actionPerformed( ActionEvent evt ) {
		if(evt.getSource() == btSelecionarTodos){
			selecionarTodos();
		}else if(evt.getSource() == btSelecionarNenhum){
			selecionarNenhum();
		}else if(evt.getSource() == btGerarRenegociaco){
			boolean selecionado = false;
			for ( int row = 0; row < tabSeleciona.getNumLinhas(); row++ ) {
				if ((Boolean)tabSeleciona.getValor( row, RECEBER.SEL.ordinal() )){
					selecionado = true;
				}
			}
			
			if(selecionado){
				tpn.setEnabledAt( GERA, true );
				tpn.setEnabledAt( SELECIONA, false );
				tpn.setSelectedIndex( GERA );
				
				txtValorTotalOriginal.setVlrBigDecimal( txtTotalSelecionado.getVlrBigDecimal() );
				txtValorTotalRenegociado.setVlrBigDecimal( txtValorTotalOriginal.getVlrBigDecimal() );
				txtDtReneg.setVlrDate( new Date() );
			}else{
				Funcoes.mensagemErro(this, "Selecione um ou mais titulos para renegociação");
			}
		}else if(evt.getSource() == btOK){
			gerarRenegociacao();
		}
	}
	
	public void beforePost( PostEvent evt ) {
		if ( ( evt.getListaCampos().equals( lcReceber ) ) & ( lcReceber.getStatus() == ListaCampos.LCS_INSERT ) ) {

			testaCodRec();
			txtStatus.setVlrString( "RR" );

			// Gerando histórico dinâmico
			Integer codhistrec = null;
			codhistrec = (Integer) prefere.get( "codhistrec" );

			if ( codhistrec != 0 ) {
				historico = new Historico( codhistrec, con );
			}
			else {
				historico = new Historico();
				
				String historicoStr = "";
				for ( int row = 0; row < tabSeleciona.getNumLinhas(); row++ ) {
					Boolean selecionado = (Boolean)tabSeleciona.getValor( row, RECEBER.SEL.ordinal() ) ;
					if (selecionado){
						historicoStr +=  tabSeleciona.getValor( row, RECEBER.DOCLANCA.ordinal() ).toString();
						historicoStr += ", ";
					}
				}		
				historicoStr = historicoStr.substring( 0, historicoStr.length() - 2 );
				
				historico.setHistoricocodificado( HISTORICO_PADRAO );
				historico.setHistoricocodificado( historico.getHistoricocodificado().replaceAll( "<TITULOS>", historicoStr ) );
			}

			historico.setData( txtDtReneg.getVlrDate() );
			historico.setDocumento( txtDocRec.getVlrString() );
			historico.setPortador( txtDescCli.getVlrString() );
			historico.setValor( txtValorTotalRenegociado.getVlrBigDecimal() );
			historico.setHistoricoant( txtObs.getVlrString() );

			txtObs.setVlrString( historico.getHistoricodecodificado() );
		}
	}

	public void afterPost( PostEvent evt ) { }

	public void beforeCarrega( CarregaEvent evt ) { }

	public void afterCarrega( CarregaEvent evt ) {
		if ( evt.getListaCampos() == lcTipoCob && "S".equalsIgnoreCase( txtObrigCart.getVlrString() ) ) {
			txtCodBanco.setRequerido( true );
			txtCodCartCob.setRequerido( true );
		}
	}

	public void focusGained( FocusEvent evt ) { }

	public void focusLost( FocusEvent evt ) {
		if ( evt.getSource() == txtCodTipoCob ) {
			lcTipoCob.carregaDados();

			if ( !"S".equalsIgnoreCase( txtObrigCart.getVlrString() ) ) {
				txtCodBanco.setRequerido( false );
				txtCodCartCob.setRequerido( false );
			}
		}else if(evt.getSource() == txtValorAdicional ||
				evt.getSource() == txtValorDesconto ||
				evt.getSource() == txtValorJuros){
			BigDecimal valorTotal = txtValorTotalOriginal.getVlrBigDecimal();
			valorTotal = valorTotal.subtract( txtValorDesconto.getVlrBigDecimal() );
			valorTotal = valorTotal.add( txtValorJuros.getVlrBigDecimal() );
			valorTotal = valorTotal.add( txtValorAdicional.getVlrBigDecimal() );
			txtValorTotalRenegociado.setVlrBigDecimal( valorTotal );
		}
	}
	
	public void stateChanged( ChangeEvent evt ) { }
	
	public void mouseClicked( MouseEvent mevt ) {
		if ( mevt.getSource() == tabGera && mevt.getClickCount() == 2 ) {
			editar();
		}
	}
	
	public void mouseEntered( MouseEvent arg0 ) { }

	public void mouseExited( MouseEvent arg0 ) { }

	public void mousePressed( MouseEvent arg0 ) { }

	public void mouseReleased( MouseEvent arg0 ) { }
	
	public class GridRenegRec {

		private String status;
		private Date dataVencimento;
		private int codigoReceber;
		private int parcela;
		private String documentoLancamento;
		private int codigoCliente;
		private String razaoCliente;
		private String documentoVenda;
		private BigDecimal valorParcela;
		private Date dataPagamento;
		private BigDecimal valorPago;
		private BigDecimal valorDesconto;
		private BigDecimal valorJuros;
		private BigDecimal valorAReceber;
		private String conta;
		private String descricaoConta;
		private String planejamento;
		private String descricaoPlanejamento;
		private String banco;
		private String nomeBanco;
		private String observacao;

		public GridRenegRec() { }
		
		public String getStatus() {
			return status;
		}
		
		public void setStatus( String status ) {
			this.status = status;
		}
		
		public Date getDataVencimento() {
			return dataVencimento;
		}
		
		public void setDataVencimento( Date dataVencimento ) {
			this.dataVencimento = dataVencimento;
		}
		
		public int getCodigoReceber() {
			return codigoReceber;
		}
		
		public void setCodigoReceber( int codigoReceber ) {
			this.codigoReceber = codigoReceber;
		}
		
		public int getParcela() {
			return parcela;
		}
		
		public void setParcela( int parcela ) {
			this.parcela = parcela;
		}
		
		public String getDocumentoLancamento() {
			return documentoLancamento;
		}
		
		public void setDocumentoLancamento( String documentoLancamento ) {
			this.documentoLancamento = documentoLancamento;
		}
		
		public int getCodigoCliente() {
			return codigoCliente;
		}
		
		public void setCodigoCliente( int codigoCliente ) {
			this.codigoCliente = codigoCliente;
		}
		
		public String getRazaoCliente() {
			return razaoCliente;
		}
		
		public void setRazaoCliente( String razaoCliente ) {
			this.razaoCliente = razaoCliente;
		}
		
		public String getDocumentoVenda() {
			return documentoVenda;
		}
		
		public void setDocumentoVenda( String documentoVenda ) {
			this.documentoVenda = documentoVenda;
		}
		
		public BigDecimal getValorParcela() {
			return valorParcela;
		}
		
		public void setValorParcela( BigDecimal valorParcela ) {
			this.valorParcela = valorParcela;
		}
		
		public Date getDataPagamento() {
			return dataPagamento;
		}
		
		public void setDataPagamento( Date dataPagamento ) {
			this.dataPagamento = dataPagamento;
		}
		
		public BigDecimal getValorPago() {
			return valorPago;
		}
		
		public void setValorPago( BigDecimal valorPago ) {
			this.valorPago = valorPago;
		}
		
		public BigDecimal getValorDesconto() {
			return valorDesconto;
		}
		
		public void setValorDesconto( BigDecimal valorDesconto ) {
			this.valorDesconto = valorDesconto;
		}
		
		public BigDecimal getValorJuros() {
			return valorJuros;
		}
		
		public void setValorJuros( BigDecimal valorJuros ) {
			this.valorJuros = valorJuros;
		}
		
		public BigDecimal getValorAReceber() {
			return valorAReceber;
		}
		
		public void setValorAReceber( BigDecimal valorAReceber ) {
			this.valorAReceber = valorAReceber;
		}
		
		public String getConta() {
			return conta;
		}
		
		public void setConta( String conta ) {
			this.conta = conta;
		}
		
		public String getDescricaoConta() {
			return descricaoConta;
		}
		
		public void setDescricaoConta( String descricaoConta ) {
			this.descricaoConta = descricaoConta;
		}
		
		public String getPlanejamento() {
			return planejamento;
		}
		
		public void setPlanejamento( String planejamento ) {
			this.planejamento = planejamento;
		}
		
		public String getDescricaoPlanejamento() {
			return descricaoPlanejamento;
		}
		
		public void setDescricaoPlanejamento( String descricaoPlanejamento ) {
			this.descricaoPlanejamento = descricaoPlanejamento;
		}
		
		public String getBanco() {
			return banco;
		}
		
		public void setBanco( String banco ) {
			this.banco = banco;
		}
		
		public String getNomeBanco() {
			return nomeBanco;
		}
		
		public void setNomeBanco( String nomeBanco ) {
			this.nomeBanco = nomeBanco;
		}
		
		public String getObservacao() {
			return observacao;
		}
		
		public void setObservacao( String observacao ) {
			this.observacao = observacao;
		}
	}

}
