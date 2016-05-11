/**
 * @version 17/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.pdv <BR>
 *         Classe:
 * @(#)FLeFiscal.java <BR>
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

package org.freedom.modulos.pdv;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.freedom.bmps.Icone;
import org.freedom.ecf.app.ControllerECF;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.Campo;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.AplicativoPDV;
import org.freedom.library.swing.frame.FTabDados;

public class FLeFiscal extends FTabDados {

	private static final long serialVersionUID = 1L;

	private final JPanelPad pinCab = new JPanelPad( 0, 60 );

	private final JPanelPad pinGeral = new JPanelPad();

	private final JPanelPad pinAliq = new JPanelPad();

	private final JPanelPad pinTrib = new JPanelPad();

	private final JPanelPad pinTributado = new JPanelPad();

	private final JPanelPad pinIsen = new JPanelPad();

	private final JPanelPad pinIsento = new JPanelPad();

	private final JPanelPad pnMapa = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	private final JTextFieldPad txtDataX = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtNumTermX = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldFK txtDescTermX = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtAliq1 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 5, 2 );

	private final JTextFieldPad txtAliq2 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 5, 2 );

	private final JTextFieldPad txtAliq3 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 5, 2 );

	private final JTextFieldPad txtAliq4 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 5, 2 );

	private final JTextFieldPad txtAliq5 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 5, 2 );

	private final JTextFieldPad txtAliq6 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 5, 2 );

	private final JTextFieldPad txtAliq7 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 5, 2 );

	private final JTextFieldPad txtAliq8 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 5, 2 );

	private final JTextFieldPad txtAliq9 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 5, 2 );

	private final JTextFieldPad txtAliq10 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 5, 2 );

	private final JTextFieldPad txtAliq11 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 5, 2 );

	private final JTextFieldPad txtAliq12 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 5, 2 );

	private final JTextFieldPad txtAliq13 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 5, 2 );

	private final JTextFieldPad txtAliq14 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 5, 2 );

	private final JTextFieldPad txtAliq15 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 5, 2 );

	private final JTextFieldPad txtAliq16 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 5, 2 );

	private final JTextFieldPad txtT1 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtT2 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtT3 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtT4 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtT5 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtT6 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtT7 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtT8 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtT9 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtT10 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtT11 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtT12 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtT13 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtT14 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtT15 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtT16 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtTN1 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtTN2 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtTN3 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtTN4 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtTN5 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtTN6 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtTN7 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtTN8 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtTN9 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtTSangria = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtPrimCupom = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtTSuprimento = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtUltCupom = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtTotal = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtCanc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtVlrCanc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtRed = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtVlrDesc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtSequencia = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtVlrIsento = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 16, 2 );

	private final JTextFieldPad txtVlrNI = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 16, 2 );

	private final JTextFieldPad txtVlrSubst = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 16, 2 );

	private final JTextFieldPad txtVlrContabil = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 16, 2 );

	private final JButtonPad btExec = new JButtonPad( Icone.novo( "btExecuta.png" ) );

	private final JTablePad tab = new JTablePad();

	private final JScrollPane spnTab = new JScrollPane( tab );

	private final ListaCampos lcCaixa = new ListaCampos( this, "" );

	private final ControllerECF ecf;

	private boolean moduloPDV = false;

	public FLeFiscal() {

		super();
		setTitulo( "Leitura de memória fiscal" );
		setAtribos( 60, 60, 540, 470 );

		ecf = new ControllerECF( AplicativoPDV.getEcfdriver(), AplicativoPDV.getPortaECF(), AplicativoPDV.bModoDemo, AplicativoPDV.getEcflayout() );

		montaListaCampos();
		montaTela();
		montaTabela();

		moduloPDV = Aplicativo.nomemodulo.equals( "Ponto de Venda" );

		btExec.addActionListener( this );

		btExec.setVisible( moduloPDV );

		txtPrimCupom.setAtivo( !moduloPDV );
		txtRed.setAtivo( !moduloPDV );
	}

	private void montaListaCampos() {

		lcCaixa.add( new GuardaCampo( txtNumTermX, "CodCaixa", "Cód.caixa", ListaCampos.DB_PK, true ) );
		lcCaixa.add( new GuardaCampo( txtDescTermX, "DescCaixa", "Descrição do caixa", ListaCampos.DB_SI, false ) );
		lcCaixa.montaSql( false, "CAIXA", "PV" );
		lcCaixa.setQueryCommit( false );
		lcCaixa.setReadOnly( true );
		txtNumTermX.setTabelaExterna( lcCaixa, null );
	}

	private void montaTela() {

		pnCliente.add( pinCab, BorderLayout.NORTH );

		setPainel( pinCab );
		adicCampo( txtDataX, 7, 20, 100, 20, "Dtlx", "Data da leitura", ListaCampos.DB_PK, true );
		adicCampo( txtNumTermX, 110, 20, 77, 20, "CodCaixa", "Cód.caixa", ListaCampos.DB_PF, true );
		adicDescFK( txtDescTermX, 190, 20, 240, 20, "DescCaixa", "Descrição do caixa/máquina" );
		adic( btExec, 470, 10, 30, 30 );

		adicTab( "Geral", pinGeral );
		setPainel( pinGeral );

		JLabelPad lbAliq = new JLabelPad( "Aliquotas ICMS", SwingConstants.CENTER );
		lbAliq.setOpaque( true );

		adic( lbAliq, 15, 5, 90, 15 );
		adic( pinAliq, 7, 10, 423, 100 );

		setPainel( pinAliq );
		adicCampo( txtAliq1, 7, 25, 50, 20, "Aliq01", "T1", ListaCampos.DB_SI, false );
		adicCampo( txtAliq2, 60, 25, 47, 20, "Aliq02", "T2", ListaCampos.DB_SI, false );
		adicCampo( txtAliq3, 110, 25, 47, 20, "Aliq03", "T3", ListaCampos.DB_SI, false );
		adicCampo( txtAliq4, 160, 25, 47, 20, "Aliq04", "T4", ListaCampos.DB_SI, false );
		adicCampo( txtAliq5, 210, 25, 47, 20, "Aliq05", "T5", ListaCampos.DB_SI, false );
		adicCampo( txtAliq6, 260, 25, 47, 20, "Aliq06", "T6", ListaCampos.DB_SI, false );
		adicCampo( txtAliq7, 310, 25, 47, 20, "Aliq07", "T7", ListaCampos.DB_SI, false );
		adicCampo( txtAliq8, 360, 25, 50, 20, "Aliq08", "T8", ListaCampos.DB_SI, false );
		adicCampo( txtAliq9, 7, 65, 50, 20, "Aliq09", "T9", ListaCampos.DB_SI, false );
		adicCampo( txtAliq10, 60, 65, 47, 20, "Aliq10", "T10", ListaCampos.DB_SI, false );
		adicCampo( txtAliq11, 110, 65, 47, 20, "Aliq11", "T11", ListaCampos.DB_SI, false );
		adicCampo( txtAliq12, 160, 65, 47, 20, "Aliq12", "T12", ListaCampos.DB_SI, false );
		adicCampo( txtAliq13, 210, 65, 47, 20, "Aliq13", "T13", ListaCampos.DB_SI, false );
		adicCampo( txtAliq14, 260, 65, 47, 20, "Aliq14", "T14", ListaCampos.DB_SI, false );
		adicCampo( txtAliq15, 310, 65, 47, 20, "Aliq15", "T15", ListaCampos.DB_SI, false );
		adicCampo( txtAliq16, 360, 65, 50, 20, "Aliq16", "T16", ListaCampos.DB_SI, false );

		setPainel( pinGeral );

		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );

		JLabelPad lbContadores = new JLabelPad( "Contadores:", SwingConstants.CENTER );
		lbContadores.setOpaque( true );

		adic( lbContadores, 15, 125, 100, 15 );
		adic( lbLinha, 7, 130, 500, 2 );

		adicCampo( txtPrimCupom, 7, 160, 85, 20, "PrimCupomLX", "Prim.cupom", ListaCampos.DB_SI, false );
		adicCampo( txtUltCupom, 95, 160, 82, 20, "UltCupomLX", "Ult.cupom", ListaCampos.DB_SI, false );
		adicCampo( txtCanc, 180, 160, 82, 20, "NumCancLX", "Cancelados", ListaCampos.DB_SI, false );
		adicCampo( txtRed, 265, 160, 82, 20, "NumRedLX", "Reduções", ListaCampos.DB_SI, false );
		adicCampo( txtSequencia, 350, 160, 80, 20, "ContaLX", "Contador", ListaCampos.DB_SI, false );

		JLabelPad lbLinha2 = new JLabelPad();
		lbLinha2.setBorder( BorderFactory.createEtchedBorder() );

		JLabelPad lbContadores2 = new JLabelPad( "Totalizadores:", SwingConstants.CENTER );
		lbContadores2.setOpaque( true );

		adic( lbContadores2, 15, 195, 100, 15 );
		adic( lbLinha2, 7, 200, 500, 2 );

		adicCampo( txtTSangria, 7, 230, 105, 20, "TSangria", "Sangria", ListaCampos.DB_SI, false );
		adicCampo( txtTSuprimento, 115, 230, 102, 20, "TSuprimento", "Suprimento", ListaCampos.DB_SI, false );
		adicCampo( txtVlrCanc, 220, 230, 102, 20, "VlrCancLX", "Cancelamento", ListaCampos.DB_SI, false );
		adicCampo( txtVlrDesc, 325, 230, 105, 20, "VlrDescLX", "Desconto", ListaCampos.DB_SI, false );
		adicCampo( txtTotal, 7, 270, 105, 20, "TGTotal", "Grande total", ListaCampos.DB_SI, false );
		adicCampo( txtVlrContabil, 115, 270, 100, 20, "VlrContabilLX", "Valor Contabil", ListaCampos.DB_SI, false );

		adicTab( "Tributado", pinTributado );
		setPainel( pinTributado );

		JLabelPad lbTrib = new JLabelPad( "Valor dos totalizadores de ICMS.", SwingConstants.CENTER );
		lbTrib.setOpaque( true );

		adic( lbTrib, 15, 5, 200, 15 );
		adic( pinTrib, 7, 10, 423, 180 );

		setPainel( pinTrib );
		adicCampo( txtT1, 7, 25, 100, 20, "TT01", "T1", ListaCampos.DB_SI, false );
		adicCampo( txtT2, 110, 25, 97, 20, "TT02", "T2", ListaCampos.DB_SI, false );
		adicCampo( txtT3, 210, 25, 97, 20, "TT03", "T3", ListaCampos.DB_SI, false );
		adicCampo( txtT4, 310, 25, 100, 20, "TT04", "T4", ListaCampos.DB_SI, false );
		adicCampo( txtT5, 7, 65, 100, 20, "TT05", "T5", ListaCampos.DB_SI, false );
		adicCampo( txtT6, 110, 65, 97, 20, "TT06", "T6", ListaCampos.DB_SI, false );
		adicCampo( txtT7, 210, 65, 97, 20, "TT07", "T7", ListaCampos.DB_SI, false );
		adicCampo( txtT8, 310, 65, 100, 20, "TT08", "T8", ListaCampos.DB_SI, false );
		adicCampo( txtT9, 7, 105, 100, 20, "TT09", "T9", ListaCampos.DB_SI, false );
		adicCampo( txtT10, 110, 105, 97, 20, "TT10", "T10", ListaCampos.DB_SI, false );
		adicCampo( txtT11, 210, 105, 97, 20, "TT11", "T11", ListaCampos.DB_SI, false );
		adicCampo( txtT12, 310, 105, 100, 20, "TT12", "T12", ListaCampos.DB_SI, false );
		adicCampo( txtT13, 7, 145, 100, 20, "TT13", "T13", ListaCampos.DB_SI, false );
		adicCampo( txtT14, 110, 145, 97, 20, "TT14", "T14", ListaCampos.DB_SI, false );
		adicCampo( txtT15, 210, 145, 97, 20, "TT15", "T15", ListaCampos.DB_SI, false );
		adicCampo( txtT16, 310, 145, 100, 20, "TT16", "T16", ListaCampos.DB_SI, false );

		adicTab( "Isento", pinIsento );
		setPainel( pinIsento );

		JLabelPad lbIsento = new JLabelPad( "Valor dos totalizadores não sujeitos ao ICMS.", SwingConstants.CENTER );
		lbIsento.setOpaque( true );

		adic( lbIsento, 15, 5, 280, 15 );
		adic( pinIsen, 7, 10, 323, 140 );

		setPainel( pinIsen );
		adicCampo( txtTN1, 7, 25, 100, 20, "TN1", "T1", ListaCampos.DB_SI, false );
		adicCampo( txtTN2, 110, 25, 97, 20, "TN2", "T2", ListaCampos.DB_SI, false );
		adicCampo( txtTN3, 210, 25, 97, 20, "TN3", "T3", ListaCampos.DB_SI, false );
		adicCampo( txtTN4, 7, 65, 100, 20, "TN4", "T4", ListaCampos.DB_SI, false );
		adicCampo( txtTN5, 110, 65, 97, 20, "TN5", "T5", ListaCampos.DB_SI, false );
		adicCampo( txtTN6, 210, 65, 97, 20, "TN6", "T6", ListaCampos.DB_SI, false );
		adicCampo( txtTN7, 7, 105, 100, 20, "TN7", "T7", ListaCampos.DB_SI, false );
		adicCampo( txtTN8, 110, 105, 97, 20, "TN8", "T8", ListaCampos.DB_SI, false );
		adicCampo( txtTN9, 210, 105, 97, 20, "TN9", "T9", ListaCampos.DB_SI, false );

		setPainel( pinIsento );
		adicCampo( txtVlrIsento, 350, 45, 130, 20, "TIsencao", "Tot. isenção", ListaCampos.DB_SI, false );
		adicCampo( txtVlrNI, 350, 85, 130, 20, "TNIncidencia", "Tot. de não incidência", ListaCampos.DB_SI, false );
		adicCampo( txtVlrSubst, 350, 125, 130, 20, "TSubstituicao", "Tot. de substituição", ListaCampos.DB_SI, false );

		setListaCampos( false, "LEITURAX", "PV" );

		pnMapa.add( spnTab );
		adicTab( "Mapa fiscal", pnMapa );
	}

	private void montaTabela() {

		tab.adicColuna( "%Aliq" );
		tab.adicColuna( "Total isenção" );
		tab.adicColuna( "Total não incidência" );
		tab.adicColuna( "Total substituição" );
		tab.adicColuna( "Total sangria" );
		tab.adicColuna( "Total suprimento" );
		tab.adicColuna( "Grande total" );
		tab.adicColuna( "Valor contábil" );
		tab.adicColuna( "Base de cálculo" );
		tab.adicColuna( "Valor do imposto" );
		tab.adicColuna( "Total cancelamentos" );
		tab.adicColuna( "Total descontos" );
		tab.adicColuna( "Nº cancelamentos" );
		tab.adicColuna( "Nº reduções" );
		tab.adicColuna( "Contador" );
		tab.adicColuna( "Ultimo cupom" );

		tab.setTamColuna( 70, 0 );
		tab.setTamColuna( 100, 1 );
		tab.setTamColuna( 120, 2 );
		tab.setTamColuna( 100, 3 );
		tab.setTamColuna( 90, 4 );
		tab.setTamColuna( 100, 5 );
		tab.setTamColuna( 90, 6 );
		tab.setTamColuna( 100, 7 );
		tab.setTamColuna( 110, 8 );
		tab.setTamColuna( 120, 9 );
		tab.setTamColuna( 130, 10 );
		tab.setTamColuna( 110, 11 );
		tab.setTamColuna( 130, 12 );
		tab.setTamColuna( 100, 13 );
		tab.setTamColuna( 80, 14 );
		tab.setTamColuna( 100, 15 );
	}

	private void loadAll() {

		if ( moduloPDV ) {

			loadAliquotas();
			loadContadores();
			loadTotalizadoresFiscais();
			loadTotalizadoresNaoFiscais();
			loadTotalizadores();
		}
	}

	private void loadAliquotas() {

		if ( moduloPDV ) {

			List<String> aliquotas = ecf.getAliquotas();

			String aliquota = null;
			Campo field = null;
			int index = 1;

			for ( String str : aliquotas ) {
				aliquota = str.substring( 0, 2 ) + "." + str.substring( 2 );
				field = lcCampos.getCampo( "Aliq" + StringFunctions.strZero( String.valueOf( index ), 2 ) );
				if ( field != null ) {
					field.setVlrBigDecimal( new BigDecimal( aliquota ) );
				}
				index++;
			}
		}
	}

	private void loadContadores() {

		try {

			StringBuilder sql = new StringBuilder();
			sql.append( "SELECT FIRST 1 DOCVENDA AS PRIMEIRO FROM VDVENDA " );
			sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND " );
			sql.append( "CODEMPCX=? AND CODFILIALCX=? AND CODCAIXA=? AND " );
			sql.append( "DTEMITVENDA=? AND TIPOVENDA='E' " );
			sql.append( "ORDER BY DOCVENDA" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDVENDA" ) );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, Aplicativo.iCodFilial );
			ps.setInt( 5, AplicativoPDV.iCodCaixa );
			ps.setDate( 6, Funcoes.dateToSQLDate( txtDataX.getVlrDate() ) );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				txtPrimCupom.setVlrInteger( rs.getInt( "PRIMEIRO" ) );
			}
			else {
				txtPrimCupom.setVlrInteger( 0 );
			}

			rs.close();
			ps.close();

			sql = new StringBuilder();
			sql.append( "SELECT FIRST 1 DOCVENDA AS ULTIMO FROM VDVENDA " );
			sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND " );
			sql.append( "CODEMPCX=? AND CODFILIALCX=? AND CODCAIXA=? AND " );
			sql.append( "DTEMITVENDA=? AND TIPOVENDA='E' " );
			sql.append( "ORDER BY CODVENDA DESC" );

			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDVENDA" ) );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, Aplicativo.iCodFilial );
			ps.setInt( 5, AplicativoPDV.iCodCaixa );
			ps.setDate( 6, Funcoes.dateToSQLDate( txtDataX.getVlrDate() ) );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				txtUltCupom.setVlrInteger( rs.getInt( "ULTIMO" ) );
			}
			else {
				txtUltCupom.setVlrInteger( 0 );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar primeiro cupom!\n" + e.getMessage(), true, con, e );
		}

		if ( moduloPDV ) {

			txtRed.setVlrInteger( ecf.getNumeroReducoesZ() );
			txtCanc.setVlrInteger( ecf.getNumeroCancelamentos() );
			txtSequencia.setVlrInteger( ecf.getNumeroDocumento() );
		}
	}

	private void loadTotalizadores() {

		if ( moduloPDV ) {

			txtTSuprimento.setVlrBigDecimal( ecf.getTotalSuprimento() );
			txtTSangria.setVlrBigDecimal( ecf.getTotalSangria() );
			txtVlrCanc.setVlrBigDecimal( ecf.getTotalCancelamentos() );
			txtVlrDesc.setVlrBigDecimal( ecf.getTotalDescontos() );
			txtTotal.setVlrBigDecimal( ecf.getGrandeTotal() );
			txtVlrContabil.setVlrBigDecimal( new BigDecimal( txtVlrIsento.floatValue() + txtVlrNI.floatValue() + txtVlrSubst.floatValue() ) );
		}
	}

	private void loadTotalizadoresFiscais() {

		if ( moduloPDV ) {

			List<BigDecimal> totalizadores = ecf.getTotalizadoresFiscais();

			Campo field = null;
			int index = 1;

			for ( BigDecimal totalizador : totalizadores ) {
				field = lcCampos.getCampo( "TT" + StringFunctions.strZero( String.valueOf( index ), 2 ) );
				if ( field != null ) {
					field.setVlrBigDecimal( totalizador );
				}
				index++;
			}
		}
	}

	private void loadTotalizadoresNaoFiscais() {

		if ( moduloPDV ) {

			List<BigDecimal> totalizadores = ecf.getTotalizadoresNaoFiscais();

			Campo field = null;
			int index = 1;

			for ( BigDecimal totalizador : totalizadores ) {
				field = lcCampos.getCampo( "TN" + index );
				if ( field != null ) {
					field.setVlrBigDecimal( totalizador );
				}
				index++;
			}

			txtVlrIsento.setVlrBigDecimal( ecf.getTotalIsensao() );
			txtVlrNI.setVlrBigDecimal( ecf.getTotalNaoInsidencia() );
			txtVlrSubst.setVlrBigDecimal( ecf.getTotalSubstituicao() );
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( moduloPDV && evt.getSource() == btExec ) {
			loadAll();
		}

		super.actionPerformed( evt );
	}

	public boolean getReducaoZ( Date data, int codCaixa ) {

		if ( !moduloPDV ) {
			return false;
		}

		try {

			lcCampos.insert( false );
			txtDataX.setVlrDate( data );
			txtNumTermX.setVlrInteger( new Integer( codCaixa ) );
			lcCaixa.carregaDados();

			loadAll();

		} catch ( Exception e ) {
			Funcoes.mensagemErro( null, "Erro ao gravar Redução Z\n" + e.getMessage() );
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean salvaReducaoZ() {

		return moduloPDV ? lcCampos.post() : false;
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcCampos.setConexao( cn );
		lcCaixa.setConexao( cn );
	}
}
