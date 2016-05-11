/**
 * @version 25/02/2008 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.pdv <BR>
 *         Classe:
 * @(#)FManutRec.java <BR>
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
 *                    Tela de fechamento de venda no PDV.
 * 
 */

package org.freedom.modulos.pdv;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.TabelaSelEvent;
import org.freedom.acao.TabelaSelListener;
import org.freedom.bmps.Icone;
import org.freedom.ecf.app.ControllerECF;
import org.freedom.infra.functions.ConversionFunctions;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
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
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.AplicativoPDV;
import org.freedom.modulos.fnc.view.dialog.utility.DLBaixaRec;

public class FManutRec extends FFDialogo implements CarregaListener, TabelaSelListener {

	private static final long serialVersionUID = 1L;

	private final ControllerECF ecf;

	private JTablePad tabBaixa = new JTablePad();

	private JScrollPane spnBaixa = new JScrollPane( tabBaixa );

	private JTabbedPanePad tpn = new JTabbedPanePad();

	private JPanelPad pinBaixa = new JPanelPad( 500, 140 );

	private JPanelPad pnTabBaixa = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinBotoesBaixa = new JPanelPad( 40, 100 );

	private JPanelPad pnBaixa = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnRodape = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnBotoes = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnBtOk = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnBtCancel = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTextFieldPad txtCodRecBaixa = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDoc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtSerie = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtCodVendaBaixa = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodCliBaixa = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRazCliBaixa = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldFK txtCPFCliBaixa = new JTextFieldFK( JTextFieldPad.TP_STRING, 11, 0 );

	private JTextFieldFK txtDescBancoBaixa = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtDtEmisBaixa = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtTotRecBaixa = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtTotPagoBaixa = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtTotAbertoBaixa = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtJurosBaixa = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtCodBancoBaixa = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private JTextFieldPad txtPrimCompr = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtUltCompr = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDataMaxFat = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtVlrMaxFat = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtVlrTotCompr = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtVlrTotPago = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtVlrTotAberto = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtDataMaxAcum = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtVlrMaxAcum = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JButtonPad btCarregaBaixas = new JButtonPad( Icone.novo( "btConsBaixa.png" ) );

	private JButtonPad btBaixa = new JButtonPad( Icone.novo( "btOk.png" ) );

	private JButtonPad btApaga = new JButtonPad( Icone.novo( "btNada.png" ) );

	private ListaCampos lcRecBaixa = new ListaCampos( this );

	private ListaCampos lcVendaBaixa = new ListaCampos( this );

	private ListaCampos lcCliBaixa = new ListaCampos( this );

	private ListaCampos lcBancoBaixa = new ListaCampos( this, "BC" );

	private ImageIcon imgVencido = Icone.novo( "clVencido.gif" );

	private ImageIcon imgPago = Icone.novo( "clPago.gif" );

	private ImageIcon imgPagoParcial = Icone.novo( "clPagoParcial.gif" );

	private ImageIcon imgNaoVencido = Icone.novo( "clNaoVencido.gif" );

	private JPanelPad pnLegenda = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 0, 4 ) );

	private ImageIcon imgColuna = null;

	private int anoBase;

	private boolean carregavel = true;

	private boolean porCliente = false;

	public FManutRec() {

		super( Aplicativo.telaPrincipal );
		setTitulo( "Receber" );
		setAtribos( 792, 480 );

		ecf = new ControllerECF( AplicativoPDV.getEcfdriver(), AplicativoPDV.getPortaECF(), AplicativoPDV.bModoDemo, AplicativoPDV.getEcflayout() );

		montaListaCampos();
		montaTela();
		montaTabela();

		btCarregaBaixas.addActionListener( this );
		btBaixa.addActionListener( this );
		btApaga.addActionListener( this );

		txtCodRecBaixa.addKeyListener( this );
		txtCodCliBaixa.addKeyListener( this );
		tabBaixa.addKeyListener( this );

		lcRecBaixa.addCarregaListener( this );
		lcCliBaixa.addCarregaListener( this );

		tabBaixa.addTabelaSelListener( this );
	}

	private void montaListaCampos() {

		lcRecBaixa.add( new GuardaCampo( txtCodRecBaixa, "CodRec", "Cód.rec.", ListaCampos.DB_PK, false ) );
		lcRecBaixa.add( new GuardaCampo( txtCodVendaBaixa, "CodVenda", "Cód.venda", ListaCampos.DB_FK, false ) );
		lcRecBaixa.add( new GuardaCampo( txtDoc, "DocRec", "Doc", ListaCampos.DB_SI, false ) );
		lcRecBaixa.add( new GuardaCampo( txtTotRecBaixa, "VlrRec", "Tot.rec.", ListaCampos.DB_SI, false ) );
		lcRecBaixa.add( new GuardaCampo( txtCodCliBaixa, "CodCli", "Cód.cli.", ListaCampos.DB_FK, false ) );
		lcRecBaixa.add( new GuardaCampo( txtDtEmisBaixa, "DataRec", "Data emissão", ListaCampos.DB_SI, false ) );
		lcRecBaixa.add( new GuardaCampo( txtCodBancoBaixa, "CodBanco", "Cód.banco", ListaCampos.DB_FK, false ) );
		lcRecBaixa.add( new GuardaCampo( txtTotAbertoBaixa, "VlrApagRec", "Total aberto", ListaCampos.DB_SI, false ) );
		lcRecBaixa.add( new GuardaCampo( txtTotPagoBaixa, "VlrPagoRec", "Total pago", ListaCampos.DB_SI, false ) );
		lcRecBaixa.add( new GuardaCampo( txtJurosBaixa, "VlrJurosRec", "Total juros", ListaCampos.DB_SI, false ) );
		lcRecBaixa.montaSql( false, "RECEBER", "FN" );
		lcRecBaixa.setQueryCommit( false );
		lcRecBaixa.setReadOnly( true );
		txtCodRecBaixa.setTabelaExterna( lcRecBaixa, null );
		txtCodRecBaixa.setFK( true );
		txtCodRecBaixa.setNomeCampo( "CodRec" );

		lcCliBaixa.add( new GuardaCampo( txtCodCliBaixa, "CodCli", "Cód.cli", ListaCampos.DB_PK, false ) );
		lcCliBaixa.add( new GuardaCampo( txtRazCliBaixa, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCliBaixa.add( new GuardaCampo( txtCPFCliBaixa, "CPFCli", "CPF", ListaCampos.DB_SI, false ) );
		lcCliBaixa.montaSql( false, "CLIENTE", "VD" );
		lcCliBaixa.setQueryCommit( false );
		lcCliBaixa.setReadOnly( true );
		txtCodCliBaixa.setTabelaExterna( lcCliBaixa, null );
		txtCodCliBaixa.setFK( true );
		txtCodCliBaixa.setNomeCampo( "CodCli" );
		txtCPFCliBaixa.setMascara( JTextFieldPad.MC_CPF );

		lcVendaBaixa.add( new GuardaCampo( txtCodVendaBaixa, "CodVenda", "Cód.venda", ListaCampos.DB_PK, false ) );
		lcVendaBaixa.add( new GuardaCampo( txtSerie, "Serie", "Série", ListaCampos.DB_SI, false ) );
		lcVendaBaixa.montaSql( false, "VENDA", "VD" );
		lcVendaBaixa.setQueryCommit( false );
		lcVendaBaixa.setReadOnly( true );
		txtCodVendaBaixa.setTabelaExterna( lcVendaBaixa, null );
		txtCodVendaBaixa.setFK( true );
		txtCodVendaBaixa.setNomeCampo( "CodVenda" );

		lcBancoBaixa.add( new GuardaCampo( txtCodBancoBaixa, "CodBanco", "Cód.banco", ListaCampos.DB_PK, false ) );
		lcBancoBaixa.add( new GuardaCampo( txtDescBancoBaixa, "NomeBanco", "Nome do banco", ListaCampos.DB_SI, false ) );
		lcBancoBaixa.montaSql( false, "BANCO", "FN" );
		lcBancoBaixa.setQueryCommit( false );
		lcBancoBaixa.setReadOnly( true );
		txtCodBancoBaixa.setTabelaExterna( lcBancoBaixa, null );
		txtCodBancoBaixa.setFK( true );
		txtCodBancoBaixa.setNomeCampo( "CodBanco" );
	}

	private void montaTela() {

		Container c = getContentPane();
		c.setLayout( new BorderLayout() );
		c.add( pnRodape, BorderLayout.SOUTH );
		c.add( tpn, BorderLayout.CENTER );

		tpn.addTab( "Baixa", pnBaixa );

		pnBaixa.add( pinBaixa, BorderLayout.NORTH );

		pnTabBaixa.add( pinBotoesBaixa, BorderLayout.EAST );
		pnTabBaixa.add( spnBaixa, BorderLayout.CENTER );
		pnTabBaixa.add( pnBotoes, BorderLayout.SOUTH );
		pnBotoes.add( pnBtOk, BorderLayout.EAST );
		pnBtOk.add( pnBtCancel, BorderLayout.EAST );
		pnBotoes.add( pnLegenda, BorderLayout.WEST );

		pnLegenda.add( new JLabelPad( "Vencido", imgVencido, SwingConstants.CENTER ) );
		pnLegenda.add( new JLabelPad( "Parcial", imgPagoParcial, SwingConstants.CENTER ) );
		pnLegenda.add( new JLabelPad( "Pago", imgPago, SwingConstants.CENTER ) );
		pnLegenda.add( new JLabelPad( "À vencer", imgNaoVencido, SwingConstants.CENTER ) );

		pnBtOk.add( btOK );
		pnBtCancel.add( btCancel );

		pnBaixa.add( pnTabBaixa, BorderLayout.CENTER );

		txtDoc.setAtivo( false );
		txtCodVendaBaixa.setAtivo( false );
		txtSerie.setAtivo( false );
		txtDtEmisBaixa.setAtivo( false );
		txtCodBancoBaixa.setAtivo( false );
		txtTotRecBaixa.setAtivo( false );
		txtTotAbertoBaixa.setAtivo( false );
		txtTotPagoBaixa.setAtivo( false );
		txtJurosBaixa.setAtivo( false );

		btApaga.setToolTipText( "Limpar consulta." );

		pinBotoesBaixa.adic( btBaixa, 3, 10, 30, 30 );
		pinBaixa.adic( btApaga, 540, 90, 30, 30 );

		pinBaixa.adic( new JLabelPad( "Cód.rec" ), 7, 0, 100, 20 );
		pinBaixa.adic( txtCodRecBaixa, 7, 20, 100, 20 );
		pinBaixa.adic( new JLabelPad( "Doc." ), 110, 0, 100, 20 );
		pinBaixa.adic( txtDoc, 110, 20, 100, 20 );
		pinBaixa.adic( new JLabelPad( "Série" ), 213, 0, 50, 20 );
		pinBaixa.adic( txtSerie, 213, 20, 50, 20 );
		pinBaixa.adic( new JLabelPad( "Pedido" ), 266, 0, 100, 20 );
		pinBaixa.adic( txtCodVendaBaixa, 266, 20, 100, 20 );

		pinBaixa.adic( new JLabelPad( "Cód.cli." ), 7, 40, 100, 20 );
		pinBaixa.adic( txtCodCliBaixa, 7, 60, 100, 20 );
		pinBaixa.adic( new JLabelPad( "Descrição do cliente" ), 110, 40, 256, 20 );
		pinBaixa.adic( txtRazCliBaixa, 110, 60, 256, 20 );
		pinBaixa.adic( new JLabelPad( "Cód.banco" ), 369, 40, 100, 20 );
		pinBaixa.adic( txtCodBancoBaixa, 369, 60, 100, 20 );
		pinBaixa.adic( new JLabelPad( "Descrição do banco" ), 471, 40, 256, 20 );
		pinBaixa.adic( txtDescBancoBaixa, 471, 60, 256, 20 );

		pinBaixa.adic( new JLabelPad( "Data de emissão" ), 7, 80, 100, 20 );
		pinBaixa.adic( txtDtEmisBaixa, 7, 100, 120, 20 );
		pinBaixa.adic( new JLabelPad( "Total a pagar" ), 130, 80, 97, 20 );
		pinBaixa.adic( txtTotRecBaixa, 130, 100, 97, 20 );
		pinBaixa.adic( new JLabelPad( "Total pago" ), 230, 80, 97, 20 );
		pinBaixa.adic( txtTotPagoBaixa, 230, 100, 97, 20 );
		pinBaixa.adic( new JLabelPad( "Total em aberto" ), 330, 80, 107, 20 );
		pinBaixa.adic( txtTotAbertoBaixa, 330, 100, 107, 20 );
		pinBaixa.adic( new JLabelPad( "Juros" ), 440, 80, 80, 20 );
		pinBaixa.adic( txtJurosBaixa, 440, 100, 90, 20 );
	}

	private void montaTabela() {

		tabBaixa.adicColuna( "" );// 0
		tabBaixa.adicColuna( "Vencimento" ); // 1
		tabBaixa.adicColuna( "Cód.rec." ); // 2
		tabBaixa.adicColuna( "Nro.parc." ); // 3
		tabBaixa.adicColuna( "Doc." ); // 4
		tabBaixa.adicColuna( "Pedido" ); // 5
		tabBaixa.adicColuna( "Valor parcela" ); // 6
		tabBaixa.adicColuna( "Data Pagamento" ); // 7
		tabBaixa.adicColuna( "Valor pago" ); // 8
		tabBaixa.adicColuna( "Valor desc." ); // 9
		tabBaixa.adicColuna( "Valor juros" ); // 10
		tabBaixa.adicColuna( "Valor aberto" ); // 11
		tabBaixa.adicColuna( "Nro.Conta" ); // 12
		tabBaixa.adicColuna( "Descrição conta" ); // 13
		tabBaixa.adicColuna( "Cód.planej." ); // 14
		tabBaixa.adicColuna( "Descrição planej." ); // 15
		tabBaixa.adicColuna( "Cód.c.c." ); // 16
		tabBaixa.adicColuna( "Descrição c.c." ); // 17
		tabBaixa.adicColuna( "Observação" ); // 18

		tabBaixa.setTamColuna( 0, EColTabBaixa.IMGSTATUS.ordinal() );
		tabBaixa.setTamColuna( 100, EColTabBaixa.DTVENC.ordinal() );
		tabBaixa.setTamColuna( 100, EColTabBaixa.CODREC.ordinal() );
		tabBaixa.setTamColuna( 100, EColTabBaixa.NPARCITREC.ordinal() );
		tabBaixa.setTamColuna( 70, EColTabBaixa.DOC.ordinal() );
		tabBaixa.setTamColuna( 70, EColTabBaixa.CODVENDA.ordinal() );
		tabBaixa.setTamColuna( 100, EColTabBaixa.VLRPARC.ordinal() );
		tabBaixa.setTamColuna( 120, EColTabBaixa.DTPAGTO.ordinal() );
		tabBaixa.setTamColuna( 100, EColTabBaixa.VLRPAGO.ordinal() );
		tabBaixa.setTamColuna( 100, EColTabBaixa.VLRDESC.ordinal() );
		tabBaixa.setTamColuna( 100, EColTabBaixa.VLRJUROS.ordinal() );
		tabBaixa.setTamColuna( 100, EColTabBaixa.VLRAPAG.ordinal() );
		tabBaixa.setTamColuna( 80, EColTabBaixa.NUMCONTA.ordinal() );
		tabBaixa.setTamColuna( 180, EColTabBaixa.DESCCONTA.ordinal() );
		tabBaixa.setTamColuna( 80, EColTabBaixa.CODPLAN.ordinal() );
		tabBaixa.setTamColuna( 180, EColTabBaixa.DESCPLAN.ordinal() );
		tabBaixa.setTamColuna( 80, EColTabBaixa.CODCC.ordinal() );
		tabBaixa.setTamColuna( 180, EColTabBaixa.DESCCC.ordinal() );
		tabBaixa.setTamColuna( 200, EColTabBaixa.OBS.ordinal() );
	}

	private void limpaConsulta() {

		carregavel = true;

		txtDoc.setVlrString( "" );
		txtSerie.setVlrString( "" );
		txtCodVendaBaixa.setVlrString( "" );
		txtDtEmisBaixa.setVlrString( "" );
		txtTotAbertoBaixa.setVlrString( "" );
		txtTotPagoBaixa.setVlrString( "" );
		txtTotRecBaixa.setVlrString( "" );
		txtCodRecBaixa.setVlrString( "" );
		txtDescBancoBaixa.setVlrString( "" );
		txtCodCliBaixa.setVlrString( "" );
		txtRazCliBaixa.setVlrString( "" );
		txtJurosBaixa.setVlrString( "" );

		lcRecBaixa.limpaCampos( true );
		lcVendaBaixa.limpaCampos( true );
		lcCliBaixa.limpaCampos( true );
		lcBancoBaixa.limpaCampos( true );

		txtCodRecBaixa.setSoLeitura( false );
		txtCodCliBaixa.setSoLeitura( false );

		tabBaixa.limpa();

		txtCodRecBaixa.requestFocus();
	}

	private boolean carregaGridBaixa() {

		if ( !carregavel ) {
			return false;
		}

		boolean actionReturn = false;

		float bdVlrAReceber = 0.0f;
		float bdVlrPago = 0.0f;

		try {

			tabBaixa.limpa();

			StringBuffer sSQL = new StringBuffer();
			sSQL.append( "SELECT IR.DTVENCITREC,IR.STATUSITREC,R.CODREC,IR.DOCLANCAITREC," );
			sSQL.append( "R.CODVENDA,IR.VLRPARCITREC,IR.DTPAGOITREC,IR.VLRPAGOITREC," );
			sSQL.append( "IR.VLRAPAGITREC,IR.NUMCONTA,IR.VLRDESCITREC," );
			sSQL.append( "(SELECT C.DESCCONTA FROM FNCONTA C WHERE C.NUMCONTA=IR.NUMCONTA " );
			sSQL.append( "AND C.CODEMP=IR.CODEMPCA AND C.CODFILIAL=IR.CODFILIALCA) DESCCONTA,IR.CODPLAN," );
			sSQL.append( "(SELECT P.DESCPLAN FROM FNPLANEJAMENTO P " );
			sSQL.append( "WHERE P.CODPLAN=IR.CODPLAN AND P.CODEMP=IR.CODEMPPN AND P.CODFILIAL=IR.CODFILIALPN) DESCPLAN," );
			sSQL.append( "IR.CODCC," );
			sSQL.append( "(SELECT CC.DESCCC FROM FNCC CC " );
			sSQL.append( "WHERE CC.CODCC=IR.CODCC AND CC.CODEMP=IR.CODEMPCC AND CC.CODFILIAL=IR.CODFILIALCC) DESCCC," );
			sSQL.append( "IR.OBSITREC,IR.NPARCITREC,IR.VLRJUROSITREC,IR.DTITREC," );
			sSQL.append( "(SELECT V.DOCVENDA FROM VDVENDA V WHERE V.CODEMP=R.CODEMPVA " );
			sSQL.append( "AND V.CODFILIAL=R.CODFILIALVA AND V.TIPOVENDA=R.TIPOVENDA AND V.CODVENDA=R.CODVENDA) DOCVENDA," );
			sSQL.append( "IR.CODBANCO " );
			sSQL.append( "FROM FNITRECEBER IR,FNRECEBER R " );
			sSQL.append( "WHERE IR.CODREC=R.CODREC AND IR.CODEMP=R.CODEMP AND IR.CODFILIAL=R.CODFILIAL AND " );
			sSQL.append( "NOT IR.STATUSITREC='RP' AND NOT IR.VLRAPAGITREC=0 AND " );
			if ( porCliente ) {
				sSQL.append( "R.CODCLI=? AND R.CODVENDA IS NOT NULL AND " );
			}
			else {
				sSQL.append( "R.CODREC=? AND " );
			}
			sSQL.append( "R.CODEMP=? AND R.CODFILIAL=? " );
			sSQL.append( "ORDER BY IR.DTVENCITREC,IR.STATUSITREC" );

			PreparedStatement ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, porCliente ? txtCodCliBaixa.getVlrInteger() : txtCodRecBaixa.getVlrInteger() );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "FNRECEBER" ) );

			ResultSet rs = ps.executeQuery();

			for ( int i = 0; rs.next(); i++ ) {

				if ( txtCodVendaBaixa.getVlrInteger() == 0 && !porCliente ) {
					Funcoes.mensagemInforma( this, "Este recebimento não é originário de uma venda\n" + "e não poderá ser recebido por este modulo." );
					limpaConsulta();
					return false;
				}

				bdVlrAReceber = Funcoes.strDecimalToBigDecimal( 2, rs.getString( "VlrApagItRec" ) ).floatValue();
				bdVlrPago = Funcoes.strDecimalToBigDecimal( 2, rs.getString( "VlrPagoItRec" ) ).floatValue();

				if ( rs.getString( "StatusItRec" ).equals( "RP" ) && bdVlrAReceber == 0.0f ) {
					imgColuna = imgPago;
				}
				else if ( bdVlrPago > 0 ) {
					imgColuna = imgPagoParcial;
				}
				else if ( rs.getDate( "DtVencItRec" ).before( Calendar.getInstance().getTime() ) ) {
					imgColuna = imgVencido;
				}
				else if ( rs.getDate( "DtVencItRec" ).after( Calendar.getInstance().getTime() ) ) {
					imgColuna = imgNaoVencido;
				}

				tabBaixa.adicLinha();
				tabBaixa.setValor( imgColuna, i, EColTabBaixa.IMGSTATUS.ordinal() );
				tabBaixa.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "DTVENCITREC" ) ), i, EColTabBaixa.DTVENC.ordinal() );
				tabBaixa.setValor( rs.getInt( "CODREC" ), i, EColTabBaixa.CODREC.ordinal() );
				tabBaixa.setValor( rs.getInt( "NPARCITREC" ), i, EColTabBaixa.NPARCITREC.ordinal() );
				tabBaixa.setValor( ( rs.getString( "DOCLANCAITREC" ) != null ? rs.getString( "DOCLANCAITREC" ) : String.valueOf( rs.getInt( "DOCVENDA" ) ) ), i, EColTabBaixa.DOC.ordinal() );
				tabBaixa.setValor( rs.getInt( "CODVENDA" ), i, EColTabBaixa.CODVENDA.ordinal() );
				tabBaixa.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRPARCITREC" ) ), i, EColTabBaixa.VLRPARC.ordinal() );
				tabBaixa.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "DTPAGOITREC" ) ), i, EColTabBaixa.DTPAGTO.ordinal() );
				tabBaixa.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRPAGOITREC" ) ), i, EColTabBaixa.VLRPAGO.ordinal() );
				tabBaixa.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRDESCITREC" ) ), i, EColTabBaixa.VLRDESC.ordinal() );
				tabBaixa.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRJUROSITREC" ) ), i, EColTabBaixa.VLRJUROS.ordinal() );
				tabBaixa.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRAPAGITREC" ) ), i, EColTabBaixa.VLRAPAG.ordinal() );
				tabBaixa.setValor( rs.getString( "NUMCONTA" ) != null ? rs.getString( "NUMCONTA" ) : "", i, EColTabBaixa.NUMCONTA.ordinal() );
				tabBaixa.setValor( rs.getString( "DESCCONTA" ) != null ? rs.getString( "DESCCONTA" ) : "", i, EColTabBaixa.DESCCONTA.ordinal() );
				tabBaixa.setValor( rs.getString( "CODCC" ) != null ? rs.getString( "CODCC" ) : "", i, EColTabBaixa.CODCC.ordinal() );
				tabBaixa.setValor( rs.getString( "DESCCC" ) != null ? rs.getString( "DESCCC" ) : "", i, EColTabBaixa.DESCCC.ordinal() );
				tabBaixa.setValor( rs.getInt( "NPARCITREC" ), i, EColTabBaixa.NPARCITREC.ordinal() );
				tabBaixa.setValor( rs.getString( "OBSITREC" ) != null ? rs.getString( "OBSITREC" ) : "", i, EColTabBaixa.OBS.ordinal() );

				txtCodBancoBaixa.setVlrString( rs.getString( "CODBANCO" ) );
			}

			lcBancoBaixa.carregaDados();

			if ( porCliente && tabBaixa.getNumLinhas() == 0 ) {
				Funcoes.mensagemInforma( this, "Não há pagamentos pendentes para este cliente!" );
				limpaConsulta();
				return false;
			}
			else if ( tabBaixa.getNumLinhas() == 0 ) {
				rs.close();
				ps.close();

				con.commit();

				sSQL = new StringBuffer();
				sSQL.append( "SELECT IR.STATUSITREC, IR.VLRAPAGITREC " );
				sSQL.append( "FROM FNITRECEBER IR,FNRECEBER R " );
				sSQL.append( "WHERE IR.CODREC=R.CODREC AND IR.CODEMP=R.CODEMP AND IR.CODFILIAL=R.CODFILIAL AND " );
				sSQL.append( "R.CODREC=? AND R.CODEMP=? AND R.CODFILIAL=? " );
				sSQL.append( "ORDER BY IR.DTVENCITREC,IR.STATUSITREC" );

				ps = con.prepareStatement( sSQL.toString() );
				ps.setInt( 1, porCliente ? txtCodCliBaixa.getVlrInteger() : txtCodRecBaixa.getVlrInteger() );
				ps.setInt( 2, Aplicativo.iCodEmp );
				ps.setInt( 3, ListaCampos.getMasterFilial( "FNRECEBER" ) );

				rs = ps.executeQuery();

				if ( rs.next() ) {
					if ( "RP".equals( rs.getString( "STATUSITREC" ) ) && 0.0f == ( rs.getBigDecimal( "VLRAPAGITREC" ) != null ? rs.getBigDecimal( "VLRAPAGITREC" ).floatValue() : 1f ) ) {
						Funcoes.mensagemInforma( this, "Parcela já foi paga!" );
						limpaConsulta();
						return false;
					}
				}
			}

			rs.close();
			ps.close();

			con.commit();

			actionReturn = tabBaixa.getNumLinhas() > 0;

		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao montar a tabela de baixa!\n" + err.getMessage(), true, con, err );
		} finally {
			System.gc();
		}

		return actionReturn;
	}

	private void baixar() {

		PreparedStatement ps = null;
		StringBuffer sSQL = new StringBuffer();
		DLBaixaRec dl = null;
		ImageIcon imgStatusAt = null;
		int iCodRec = 0;
		int iNParcItRec = 0;

		if ( tabBaixa.getLinhaSel() > -1 ) {

			imgStatusAt = ( (ImageIcon) tabBaixa.getValor( tabBaixa.getLinhaSel(), 0 ) );

			if ( imgStatusAt == imgPago ) {
				Funcoes.mensagemInforma( this, "Parcela já foi paga!" );
				return;
			}

			int iLin = tabBaixa.getLinhaSel();

			iCodRec = (Integer) tabBaixa.getValor( iLin, EColTabBaixa.CODREC.ordinal() );
			iNParcItRec = (Integer) tabBaixa.getValor( iLin, EColTabBaixa.NPARCITREC.ordinal() );

			dl = new DLBaixaRec( AplicativoPDV.telaPrincipal );
			DLBaixaRec.BaixaRecBean baixaRecBean = dl.new BaixaRecBean();

			baixaRecBean.setCliente( txtCodCliBaixa.getVlrInteger() );
			baixaRecBean.setRazaoSocialCliente( txtRazCliBaixa.getVlrString() );
			baixaRecBean.setConta( (String) tabBaixa.getValor( iLin, EColTabBaixa.NUMCONTA.ordinal() ) );
			baixaRecBean.setPlanejamento( (String) tabBaixa.getValor( iLin, EColTabBaixa.CODPLAN.ordinal() ) );
			baixaRecBean.setDocumento( (String) tabBaixa.getValor( iLin, EColTabBaixa.DOC.ordinal() ) );
			baixaRecBean.setDataEmissao( txtDtEmisBaixa.getVlrDate() );
			baixaRecBean.setDataVencimento( Funcoes.strDateToDate( (String) tabBaixa.getValor( iLin, EColTabBaixa.DTVENC.ordinal() ) ) );
			baixaRecBean.setValorParcela( ConversionFunctions.stringToBigDecimal( tabBaixa.getValor( iLin, EColTabBaixa.VLRPARC.ordinal() ) ) );
			baixaRecBean.setValorDesconto( ConversionFunctions.stringToBigDecimal( tabBaixa.getValor( iLin, EColTabBaixa.VLRDESC.ordinal() ) ) );
			baixaRecBean.setValorJuros( ConversionFunctions.stringToBigDecimal( tabBaixa.getValor( iLin, EColTabBaixa.VLRJUROS.ordinal() ) ) );
			baixaRecBean.setValorAPagar( ConversionFunctions.stringToBigDecimal( tabBaixa.getValor( iLin, EColTabBaixa.VLRAPAG.ordinal() ) ) );
			baixaRecBean.setCentroCusto( (String) tabBaixa.getValor( iLin, EColTabBaixa.CODCC.ordinal() ) );

			if ( "".equals( tabBaixa.getValor( iLin, EColTabBaixa.DTPAGTO.ordinal() ) ) ) { // Data de pagamento branco
				baixaRecBean.setDataPagamento( new Date() );
				baixaRecBean.setValorPago( ConversionFunctions.stringToBigDecimal( tabBaixa.getValor( iLin, EColTabBaixa.VLRPAGO.ordinal() ) ) );
			}
			else {
				baixaRecBean.setDataPagamento( Funcoes.strDateToDate( (String) tabBaixa.getValor( iLin, EColTabBaixa.DTPAGTO.ordinal() ) ) );
				baixaRecBean.setValorPago( ConversionFunctions.stringToBigDecimal( tabBaixa.getValor( iLin, EColTabBaixa.VLRPAGO.ordinal() ) ) );
			}
			if ( "".equals( ( (String) tabBaixa.getValor( iLin, EColTabBaixa.OBS.ordinal() ) ).trim() ) ) {
				baixaRecBean.setObservacao( "RECEBIMENTO REF. AO PED.: " + txtCodVendaBaixa.getVlrString() );
			}
			else {
				baixaRecBean.setObservacao( (String) tabBaixa.getValor( iLin, EColTabBaixa.OBS.ordinal() ) );
			}

			dl.setValores( baixaRecBean );
			dl.setConexao( con );
			dl.setVisible( true );

			if ( dl.OK ) {

				baixaRecBean = dl.getValores();

				sSQL.append( "UPDATE FNITRECEBER SET NUMCONTA=?,CODEMPCA=?,CODFILIALCA=?,CODPLAN=?,CODEMPPN=?,CODFILIALPN=?," );
				sSQL.append( "ANOCC=?,CODCC=?,CODEMPCC=?,CODFILIALCC=?,DOCLANCAITREC=?,DTPAGOITREC=?,VLRPAGOITREC=VLRPAGOITREC+?," );
				sSQL.append( "VLRDESCITREC=?,VLRJUROSITREC=?,OBSITREC=?,STATUSITREC='RP',PDVITREC='S' " );
				sSQL.append( "WHERE CODREC=? AND NPARCITREC=? AND CODEMP=? AND CODFILIAL=?" );

				try {

					ps = con.prepareStatement( sSQL.toString() );
					ps.setString( 1, baixaRecBean.getConta() );
					ps.setInt( 2, Aplicativo.iCodEmp );
					ps.setInt( 3, ListaCampos.getMasterFilial( "FNCONTA" ) );
					ps.setString( 4, baixaRecBean.getPlanejamento() );
					ps.setInt( 5, Aplicativo.iCodEmp );
					ps.setInt( 6, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );

					if ( "".equals( baixaRecBean.getCentroCusto() ) ) {
						ps.setNull( 7, Types.INTEGER );
						ps.setNull( 8, Types.CHAR );
						ps.setNull( 9, Types.INTEGER );
						ps.setNull( 10, Types.INTEGER );
					}
					else {
						ps.setInt( 7, anoBase );
						ps.setString( 8, baixaRecBean.getCentroCusto() );
						ps.setInt( 9, Aplicativo.iCodEmp );
						ps.setInt( 10, ListaCampos.getMasterFilial( "FNCC" ) );
					}

					ps.setString( 11, baixaRecBean.getDocumento() );
					ps.setDate( 12, Funcoes.dateToSQLDate( baixaRecBean.getDataPagamento() ) );
					ps.setBigDecimal( 13, baixaRecBean.getValorPago() );
					ps.setBigDecimal( 14, baixaRecBean.getValorDesconto() );
					ps.setBigDecimal( 15, baixaRecBean.getValorJuros() );
					ps.setString( 16, baixaRecBean.getObservacao() );
					ps.setInt( 17, iCodRec );
					ps.setInt( 18, iNParcItRec );
					ps.setInt( 19, Aplicativo.iCodEmp );
					ps.setInt( 20, ListaCampos.getMasterFilial( "FNRECEBER" ) );

					ps.executeUpdate();

					con.commit();

					if ( ecf.suprimento( baixaRecBean.getValorPago(), "Recebimento" ) ) {
						// validar aqui se autentica documento... no lugar do true ai de baixo...
						// if ( true ) {
						// if ( ! ecf.autenticarDocumento() ) {
						// Funcoes.mensagemErro( this, ecf.getMessageLog() );
						// }
						// }
					}
					else {
						Funcoes.mensagemErro( this, ecf.getMessageLog() );
					}

				} catch ( Exception err ) {
					Funcoes.mensagemErro( this, "Erro ao baixar parcela!\n" + err.getMessage(), true, con, err );
					err.printStackTrace();
				}

			}

			dl.dispose();
			carregaGridBaixa();
		}
	}

	private int getAnoBaseCC() {

		int anobase = 0;

		try {
			PreparedStatement ps = con.prepareStatement( "SELECT ANOCENTROCUSTO FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			ResultSet rs = ps.executeQuery();
			if ( rs.next() ) {
				anobase = rs.getInt( "ANOCENTROCUSTO" );
			}
			rs.close();
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar o ano-base para o centro de custo.\n" + err.getMessage(), true, con, err );
		}

		return anobase;
	}

	public void beforeCarrega( CarregaEvent e ) {

	}

	public void afterCarrega( CarregaEvent e ) {

		if ( e.getListaCampos() == lcRecBaixa || e.getListaCampos() == lcCliBaixa ) {

		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btBaixa ) {
			baixar();
		}
		else if ( evt.getSource() == btApaga ) {
			limpaConsulta();
		}

		super.actionPerformed( evt );
	}

	public void keyPressed( KeyEvent e ) {

		if ( e.getKeyCode() == KeyEvent.VK_ENTER ) {
			if ( e.getSource() == tabBaixa ) {
				tabBaixa.removeTabelaSelListener( this );
				baixar();
				tabBaixa.addTabelaSelListener( this );
			}
			else if ( e.getSource() == txtCodRecBaixa || e.getSource() == txtCodCliBaixa ) {
				if ( tabBaixa.getNumLinhas() == 0 ) {
					porCliente = txtCodCliBaixa.getVlrInteger() > 0 && txtCodRecBaixa.getVlrInteger() == 0;
					if ( carregaGridBaixa() ) {
						txtCodRecBaixa.setSoLeitura( true );
						txtCodCliBaixa.setSoLeitura( true );
						tabBaixa.requestFocus();
					}
					else {
						limpaConsulta();
					}
				}
			}
		}
		else if ( e.getKeyCode() == KeyEvent.VK_ESCAPE ) {
			if ( e.getSource() == tabBaixa ) {
				limpaConsulta();
			}
		}
	}

	public void valorAlterado( TabelaSelEvent e ) {

		if ( e.getTabela() == tabBaixa ) {
			if ( tabBaixa.getNumLinhas() > 0 ) {
				carregavel = false;
				txtCodRecBaixa.setVlrInteger( (Integer) tabBaixa.getValueAt( tabBaixa.getLinhaSel(), EColTabBaixa.CODREC.ordinal() ) );
				lcRecBaixa.carregaDados();
				carregavel = true;
			}
		}
	}

	public void setVisible( boolean arg0 ) {

		if ( FreedomPDV.bECFTerm ) {

			if ( arg0 ) {

				int result = FreedomPDV.abreCaixa( con, ecf );

				if ( result == -1 ) {
					FreedomPDV.killProg( 5, "Caixa não foi aberto. A aplicação será fechada!" );
				}
				else if ( result == 3 ) {
					dispose();
				}
				else {
					if ( AplicativoPDV.caixaAberto( con ) || FreedomPDV.pegaValorINI( con ) ) {
						super.setVisible( arg0 );
					}
					else {
						super.setVisible( !arg0 );
					}
				}
			}
			else {
				super.setVisible( arg0 );
			}
		}
		else {
			FreedomPDV.killProg( 1, "Esta estação de trabalho não é um PDV!\n" + "Verifique o cadastro desta estação de trabalho através do modulo Standard." );
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcCliBaixa.setConexao( cn );
		lcVendaBaixa.setConexao( cn );
		lcBancoBaixa.setConexao( cn );
		lcRecBaixa.setConexao( cn );

		anoBase = getAnoBaseCC();
	}

	private enum EColTabBaixa {
		IMGSTATUS, DTVENC, CODREC, NPARCITREC, DOC, CODVENDA, VLRPARC, DTPAGTO, VLRPAGO, VLRDESC, VLRJUROS, VLRAPAG, NUMCONTA, DESCCONTA, CODPLAN, DESCPLAN, CODCC, DESCCC, OBS
	}

}
