package org.freedom.modulos.pdv;

/**
 * @version 01/11/2005 <BR>
 * @author Setpoint Informática Ltda./Alex Rodrigues <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.pdv <BR>
 *         Classe:
 * @(#)FFechaVenda.java <BR>
 * 
 *                      Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                      modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                      na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                      Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                      sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                      Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                      Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                      de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                      Tela de fechamento de venda no PDV.
 * 
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.ecf.app.ControllerECF;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.functions.Logger;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.AplicativoPDV;
import org.freedom.library.swing.frame.FPassword;
import org.freedom.modulos.std.view.dialog.utility.DLFechaParcela;
import org.freedom.tef.app.ControllerTef;
import org.freedom.tef.app.ControllerTefEvent;
import org.freedom.tef.app.ControllerTefListener;
import org.freedom.tef.driver.text.TextTefAction;

public class DLFechaVenda extends FFDialogo implements ControllerTefListener, CarregaListener, MouseListener, FocusListener {

	private static final long serialVersionUID = 1L;

	private static final int CASAS_DEC = Aplicativo.casasDec;

	private static final int CASAS_DEC_FIN = Aplicativo.casasDecFin;

	private final JTextFieldPad txtCodVenda = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtTipoVenda = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private final JTextFieldPad txtCodPlanoPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldFK txtDescPlanoPag = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldFK txtNomeVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCodClComis = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldFK txtDescClComis = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldFK txtVlrCupom = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 12, CASAS_DEC_FIN );

	private final JTextFieldPad txtVlrDinheiro = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 12, CASAS_DEC_FIN );

	private final JTextFieldPad txtVlrCheque = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 12, CASAS_DEC_FIN );

	private final JTextFieldPad txtVlrTef = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 12, CASAS_DEC_FIN );

	private final JTextFieldFK txtVlrPago = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 12, CASAS_DEC_FIN );

	private final JTextFieldFK txtVlrTroco = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 12, CASAS_DEC_FIN );

	private final JTextFieldPad txtCodTran = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldFK txtDescTran = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtPlacaFreteVD = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldPad txtUFFreteVD = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private final JTextFieldPad txtVlrFreteVD = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtConhecFreteVD = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private final JTextFieldPad txtQtdFreteVD = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, CASAS_DEC );

	private final JTextFieldPad txtPesoBrutVD = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, CASAS_DEC );

	private final JTextFieldPad txtPesoLiqVD = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, CASAS_DEC );

	private final JTextFieldPad txtEspFreteVD = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldPad txtMarcaFreteVD = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldPad txtCodAuxV = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private final JTextFieldPad txtCPFCliAuxV = new JTextFieldPad( JTextFieldPad.TP_STRING, 11, 0 );

	private final JTextFieldPad txtNomeCliAuxV = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtEndCliAuxV = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtNumCliAuxV = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtCidCliAuxV = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private final JTextFieldPad txtUFCliAuxV = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private final JTextFieldPad txtCodRec = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtVlrParcRec = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, CASAS_DEC_FIN );

	private final JTextFieldPad txtAltUsuRec = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private final JTextFieldPad txtNParcItRec = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtVlrParcItRec = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, CASAS_DEC_FIN );

	private final JTextFieldPad txtVlrDescItRec = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, CASAS_DEC_FIN );

	private final JTextFieldPad txtDtVencItRec = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtCodTipoCobItRec = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldFK txtDescTipoCobItRec = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private final JTextFieldPad txtCodBancoItRec = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private final JTextFieldFK txtDescBancoItRec = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private final JTextFieldPad txtCodCartCobItRec = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private final JTextFieldFK txtDescCartCobItRec = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final Vector<String> vVals = new Vector<String>();

	private final Vector<String> vLabs = new Vector<String>();

	private JRadioGroup<String, String> rgFreteVD = null;

	private final JTabbedPanePad tpn = new JTabbedPanePad();

	private final JPanelPad pnVenda = new JPanelPad( 400, 300 );

	private final JPanelPad pnAdic = new JPanelPad( 400, 300 );

	private final JPanelPad pnFrete = new JPanelPad( 400, 300 );

	private final JPanelPad pnReceber = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final ListaCampos lcVendedor = new ListaCampos( this, "VD" );

	private final ListaCampos lcClComis = new ListaCampos( this, "CM" );

	private final ListaCampos lcPlanoPag = new ListaCampos( this, "PG" );

	private final ListaCampos lcTran = new ListaCampos( this, "TN" );

	private final ListaCampos lcAuxVenda = new ListaCampos( this );

	private final ListaCampos lcFreteVD = new ListaCampos( this );

	private final ListaCampos lcReceber = new ListaCampos( this );

	private final ListaCampos lcItReceber = new ListaCampos( this );

	private final ListaCampos lcTipoCobItRec = new ListaCampos( this, "TC" );

	private final ListaCampos lcBancoItRec = new ListaCampos( this, "BO" );

	private final ListaCampos lcCartCobItRec = new ListaCampos( this, "CB" );

	private final Navegador navItRec = new Navegador( false );

	private final JTablePad tabRec = new JTablePad();

	private String sTipoVenda = null;

	private int iCodVenda = 0;

	private int iNumCupom = 0;

	private JLabelPad lbChequeElet;

	private boolean bFrete = false;

	private boolean bReceber = false;

	private boolean bUserRec = false;

	private boolean trocouCli = false;

	private boolean impMens = false;

	private BigDecimal vlrTotTrib = new BigDecimal( 0 );
	
	private BigDecimal aliqTotTrib = new BigDecimal( 0 );
	
	private BigDecimal pesoBrutFrete = new BigDecimal( 0 );

	private BigDecimal pesoLiqFrete = new BigDecimal( 0 );

	private BigDecimal vlrFrete = new BigDecimal( 0 );

	private Object[] param;

	private final ControllerECF ecf;

	private ControllerTef tef;

	private StringBuilder comprovanteTef = new StringBuilder();

	private boolean vendaFechada = false;

	public DLFechaVenda( Object[] args ) {

		setTitulo( "Fechamento de venda" );
		setAtribos( 330, 385 );

		param = args;
		iCodVenda = ( (Integer) param[ 0 ] ).intValue();
		sTipoVenda = (String) param[ 1 ];
		iNumCupom = ( (Integer) param[ 3 ] ).intValue();
		txtCodPlanoPag.setVlrInteger( (Integer) param[ 4 ] );
		trocouCli = ( (Boolean) param[ 7 ] ).booleanValue();
		txtCodVend.setVlrInteger( (Integer) param[ 12 ] );

		txtVlrCupom.setVlrBigDecimal( (BigDecimal) param[ 2 ] );
		txtVlrTef.setAtivo( false );

		vVals.addElement( "C" );
		vVals.addElement( "F" );
		vLabs.addElement( "CIF" );
		vLabs.addElement( "FOB" );
		rgFreteVD = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );

		ecf = new ControllerECF( AplicativoPDV.getEcfdriver(), AplicativoPDV.getPortaECF(), AplicativoPDV.bModoDemo, AplicativoPDV.getEcflayout() );

		if ( AplicativoPDV.bTEFTerm ) {
			try {
				tef = AplicativoPDV.getControllerTef();
				tef.setControllerMessageListener( this );
				txtVlrTef.setAtivo( tef.standardManagerActive() );
			} catch ( Exception e ) {
				e.printStackTrace();
				Funcoes.mensagemErro( this, e.getMessage() );
			}
		}

		montaListaCampos();
		montaTela();
		setConexao( (DbConnection) param[ 5 ] );

		if ( bFrete ) {
			if ( param[ 8 ] instanceof BigDecimal ) {
				pesoBrutFrete = (BigDecimal) param[ 8 ];
				pesoLiqFrete = (BigDecimal) param[ 9 ];
				vlrFrete = (BigDecimal) param[ 10 ];
				tpn.setSelectedIndex( 2 );
			}
			else if ( param[ 8 ] instanceof Boolean ) {
				tpn.setEnabledAt( 1, false );
				tpn.setEnabledAt( 2, false );
			}
		}
		else {
			tpn.setEnabledAt( 1, false );
			tpn.setEnabledAt( 2, false );
		}

		tpn.setEnabledAt( 3, false );

		int iCodAux = getCodAux();
		if ( iCodAux > 0 ) {
			txtCodAuxV.setVlrInteger( new Integer( iCodAux ) );
			lcAuxVenda.carregaDados();
		}
		else {
			txtCodAuxV.setVlrInteger( new Integer( 1 ) );
		}

		// Não pode commitar enquanto todo o processo tive OK:

		lcPlanoPag.setPodeCommit( false );

		txtVlrDinheiro.addFocusListener( this );
		txtVlrCheque.addFocusListener( this );
		txtVlrTef.addFocusListener( this );

		lcTran.addCarregaListener( this );
		lcVendedor.addCarregaListener( this );

		tabRec.addMouseListener( this );

		lcItReceber.montaTab();

	}

	private void montaListaCampos() {

		lcItReceber.setMaster( lcReceber );
		lcReceber.adicDetalhe( lcItReceber );
		lcItReceber.setTabela( tabRec );

		navItRec.setName( "ItReceber" );
		lcItReceber.setNavegador( navItRec );

		/*
		 * PLANO DE PAGAMENTO
		 */
		lcPlanoPag.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_PK, true ) );
		lcPlanoPag.add( new GuardaCampo( txtDescPlanoPag, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPag.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag.setReadOnly( true );
		txtCodPlanoPag.setTabelaExterna( lcPlanoPag, null );
		txtCodPlanoPag.setNomeCampo( "CodPlanoPag" );
		txtCodPlanoPag.setFK( true );

		/*
		 * COMISSIONADO
		 */
		lcVendedor.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.comis.", ListaCampos.DB_PK, false ) );
		lcVendedor.add( new GuardaCampo( txtNomeVend, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, false ) );
		lcVendedor.add( new GuardaCampo( txtCodClComis, "CodClComis", "Cód.cl.comis.", ListaCampos.DB_FK, false ) );
		lcVendedor.montaSql( false, "VENDEDOR", "VD" );
		lcVendedor.setReadOnly( true );
		txtCodVend.setTabelaExterna( lcVendedor, null );
		txtCodVend.setFK( true );
		txtCodVend.setNomeCampo( "CodVend" );
		txtCodVend.setAtivo( ! ( (Boolean) param[ 11 ] ).booleanValue() );

		/*
		 * CLASSIFICAÇÃO DE COMISSÃO
		 */
		lcClComis.add( new GuardaCampo( txtCodClComis, "CodClComis", "Cód.cl.comis.", ListaCampos.DB_PK, false ) );
		lcClComis.add( new GuardaCampo( txtDescClComis, "DescClComis", "Descrição da classificação da comissão", ListaCampos.DB_SI, false ) );
		lcClComis.montaSql( false, "CLCOMIS", "VD" );
		lcClComis.setReadOnly( true );
		txtCodClComis.setTabelaExterna( lcClComis, null );
		txtCodClComis.setFK( true );
		txtCodClComis.setNomeCampo( "CodClComis" );

		/*
		 * TRANSPORTADORA
		 */
		lcTran.add( new GuardaCampo( txtCodTran, "CodTran", "Cód.tran.", ListaCampos.DB_PK, false ) );
		lcTran.add( new GuardaCampo( txtDescTran, "RazTran", "Nome do transportador", ListaCampos.DB_SI, false ) );
		txtDescTran.setListaCampos( lcTran );
		txtCodTran.setTabelaExterna( lcTran, null );
		txtCodTran.setFK( true );
		lcTran.montaSql( false, "TRANSP", "VD" );
		lcTran.setQueryCommit( false );
		lcTran.setReadOnly( true );

		/*
		 * FRETE
		 */
		lcFreteVD.add( new GuardaCampo( txtTipoVenda, "TipoVenda", "Tipo", ListaCampos.DB_PK, false ) );
		lcFreteVD.add( new GuardaCampo( txtCodVenda, "CodVenda", "N.pedido", ListaCampos.DB_PK, false ) );
		lcFreteVD.add( new GuardaCampo( txtCodTran, "CodTran", "Cód.tran.", ListaCampos.DB_FK, txtDescTran, true ) );
		lcFreteVD.add( new GuardaCampo( rgFreteVD, "TipoFreteVD", "Tipo", ListaCampos.DB_SI, true ) );
		lcFreteVD.add( new GuardaCampo( txtConhecFreteVD, "ConhecFreteVD", "Conhec.", ListaCampos.DB_SI, false ) );
		lcFreteVD.add( new GuardaCampo( txtPlacaFreteVD, "PlacaFreteVD", "Placa", ListaCampos.DB_SI, true ) );
		lcFreteVD.add( new GuardaCampo( txtUFFreteVD, "UFFreteVD", "Placa", ListaCampos.DB_SI, true ) );
		lcFreteVD.add( new GuardaCampo( txtVlrFreteVD, "VlrFreteVD", "Valor", ListaCampos.DB_SI, true ) );
		lcFreteVD.add( new GuardaCampo( txtQtdFreteVD, "QtdFreteVD", "Qtd.", ListaCampos.DB_SI, true ) );
		lcFreteVD.add( new GuardaCampo( txtPesoBrutVD, "PesoBrutVD", "Peso bruto", ListaCampos.DB_SI, true ) );
		lcFreteVD.add( new GuardaCampo( txtPesoLiqVD, "PesoLiqVD", "Peso liq.", ListaCampos.DB_SI, true ) );
		lcFreteVD.add( new GuardaCampo( txtEspFreteVD, "EspFreteVD", "Esp.fiscal", ListaCampos.DB_SI, true ) );
		lcFreteVD.add( new GuardaCampo( txtMarcaFreteVD, "MarcaFreteVD", "Marca", ListaCampos.DB_SI, true ) );
		lcFreteVD.montaSql( false, "FRETEVD", "VD" );
		rgFreteVD.setListaCampos( lcFreteVD );
		txtPlacaFreteVD.setListaCampos( lcFreteVD );
		txtUFFreteVD.setListaCampos( lcFreteVD );
		txtVlrFreteVD.setListaCampos( lcFreteVD );
		txtQtdFreteVD.setListaCampos( lcFreteVD );
		txtPesoBrutVD.setListaCampos( lcFreteVD );
		txtPesoLiqVD.setListaCampos( lcFreteVD );
		txtEspFreteVD.setListaCampos( lcFreteVD );
		txtMarcaFreteVD.setListaCampos( lcFreteVD );
		txtConhecFreteVD.setListaCampos( lcFreteVD );
		txtCodTran.setListaCampos( lcFreteVD );

		/*
		 * AUXILIAR DE NOTA FISCAL
		 */
		lcAuxVenda.add( new GuardaCampo( txtTipoVenda, "TipoVenda", "Tp.venda", ListaCampos.DB_PK, true ) );
		lcAuxVenda.add( new GuardaCampo( txtCodVenda, "CodVenda", "N.pedido", ListaCampos.DB_PK, true ) );
		lcAuxVenda.add( new GuardaCampo( txtCodAuxV, "CodAuxV", "Cód.aux.", ListaCampos.DB_PK, true ) );
		lcAuxVenda.add( new GuardaCampo( txtCPFCliAuxV, "CPFCliAuxV", "CPF", ListaCampos.DB_SI, true ) );
		lcAuxVenda.add( new GuardaCampo( txtNomeCliAuxV, "NomeCliAuxV", "Nome", ListaCampos.DB_SI, true ) );
		lcAuxVenda.add( new GuardaCampo( txtEndCliAuxV, "EndCliAuxV", "Endereco", ListaCampos.DB_SI, true ) );
		lcAuxVenda.add( new GuardaCampo( txtNumCliAuxV, "NumCliAuxV", "Numero", ListaCampos.DB_SI, true ) );
		lcAuxVenda.add( new GuardaCampo( txtCidCliAuxV, "CidCliAuxV", "Cidade", ListaCampos.DB_SI, true ) );
		lcAuxVenda.add( new GuardaCampo( txtUFCliAuxV, "UFCliAuxV", "UF", ListaCampos.DB_SI, true ) );
		lcAuxVenda.montaSql( false, "AUXVENDA", "VD" );
		txtCodAuxV.setListaCampos( lcAuxVenda );
		txtCPFCliAuxV.setListaCampos( lcAuxVenda );
		txtNomeCliAuxV.setListaCampos( lcAuxVenda );
		txtEndCliAuxV.setListaCampos( lcAuxVenda );
		txtNumCliAuxV.setListaCampos( lcAuxVenda );
		txtCidCliAuxV.setListaCampos( lcAuxVenda );
		txtUFCliAuxV.setListaCampos( lcAuxVenda );
		txtCPFCliAuxV.setMascara( JTextFieldPad.MC_CPF );

		/*
		 * TIPO DE COBRANÇA
		 */
		txtCodTipoCobItRec.setNomeCampo( "CodTipoCob" );
		lcTipoCobItRec.add( new GuardaCampo( txtCodTipoCobItRec, "CodTipoCob", "Cód.tp.cob.", ListaCampos.DB_PK, false ) );
		lcTipoCobItRec.add( new GuardaCampo( txtDescTipoCobItRec, "DescTipoCob", "Descrição do tipo de cobrança.", ListaCampos.DB_SI, false ) );
		lcTipoCobItRec.montaSql( false, "TIPOCOB", "FN" );
		lcTipoCobItRec.setQueryCommit( false );
		lcTipoCobItRec.setReadOnly( true );
		txtCodTipoCobItRec.setTabelaExterna( lcTipoCobItRec, null );
		txtCodTipoCobItRec.setListaCampos( lcTipoCobItRec );
		txtDescTipoCobItRec.setListaCampos( lcTipoCobItRec );
		txtCodTipoCobItRec.setFK( true );

		/*
		 * BANCO DA PARCELA
		 */
		txtCodBancoItRec.setNomeCampo( "CodBanco" );
		lcBancoItRec.add( new GuardaCampo( txtCodBancoItRec, "CodBanco", "Cód.banco", ListaCampos.DB_PK, false ) );
		lcBancoItRec.add( new GuardaCampo( txtDescBancoItRec, "NomeBanco", "Nome do banco", ListaCampos.DB_SI, false ) );
		lcBancoItRec.montaSql( false, "BANCO", "FN" );
		lcBancoItRec.setQueryCommit( false );
		lcBancoItRec.setReadOnly( true );
		txtCodBancoItRec.setTabelaExterna( lcBancoItRec, null );
		txtCodBancoItRec.setListaCampos( lcBancoItRec );
		txtDescBancoItRec.setListaCampos( lcBancoItRec );
		txtCodBancoItRec.setFK( true );

		/*
		 * TIPO DE COBRANÇA DA PARCELA
		 */
		txtCodCartCobItRec.setNomeCampo( "CodCartCob" );
		lcCartCobItRec.add( new GuardaCampo( txtCodCartCobItRec, "CodCartCob", "Cód.Cart.Cob.", ListaCampos.DB_PK, false ) );
		lcCartCobItRec.add( new GuardaCampo( txtCodBancoItRec, "CodBanco", "Cód.Banco.", ListaCampos.DB_PK, false ) );
		lcCartCobItRec.add( new GuardaCampo( txtDescCartCobItRec, "DescCartCob", "Descrição da carteira de cobrança", ListaCampos.DB_SI, false ) );
		lcCartCobItRec.montaSql( false, "CARTCOB", "FN" );
		lcCartCobItRec.setQueryCommit( false );
		lcCartCobItRec.setReadOnly( true );
		txtCodCartCobItRec.setTabelaExterna( lcCartCobItRec, null );
		txtCodCartCobItRec.setListaCampos( lcBancoItRec );
		txtDescCartCobItRec.setListaCampos( lcBancoItRec );
		txtCodCartCobItRec.setFK( true );

		/*
		 * RECEBER
		 */
		txtCodRec.setNomeCampo( "CodRec" );
		lcReceber.add( new GuardaCampo( txtCodRec, "CodRec", "Cód.rec.", ListaCampos.DB_PK, false ) );
		lcReceber.add( new GuardaCampo( txtVlrParcRec, "VlrParcRec", "Valor tot.", ListaCampos.DB_SI, false ) );
		lcReceber.add( new GuardaCampo( txtTipoVenda, "TipoVenda", "Tp.venda", ListaCampos.DB_SI, false ) );
		lcReceber.add( new GuardaCampo( txtAltUsuRec, "AltUsuRec", "Usu.alt.", ListaCampos.DB_SI, false ) );
		lcReceber.montaSql( false, "RECEBER", "FN" );
		txtCodRec.setListaCampos( lcReceber );
		txtVlrParcRec.setListaCampos( lcReceber );
		txtTipoVenda.setListaCampos( lcReceber );
		txtAltUsuRec.setListaCampos( lcReceber );

		/*
		 * PARCELA DO RECEBER
		 */
		txtNParcItRec.setNomeCampo( "NParcItRec" );
		lcItReceber.add( new GuardaCampo( txtNParcItRec, "NParcItRec", "N.parc.", ListaCampos.DB_PK, false ) );
		lcItReceber.add( new GuardaCampo( txtVlrParcItRec, "VlrParcItRec", "Valor tot.", ListaCampos.DB_SI, false ) );
		lcItReceber.add( new GuardaCampo( txtDtVencItRec, "DtVencItRec", "Dt. vencto.", ListaCampos.DB_SI, false ) );
		lcItReceber.add( new GuardaCampo( txtVlrDescItRec, "VlrDescItRec", "Valor desc.", ListaCampos.DB_SI, false ) );
		lcItReceber.add( new GuardaCampo( txtCodTipoCobItRec, "CodTipoCob", "Cod.tp.cob.", ListaCampos.DB_FK, txtDescTipoCobItRec, false ) );
		lcItReceber.add( new GuardaCampo( txtCodBancoItRec, "CodBanco", "Cód.banco", ListaCampos.DB_FK, txtDescBancoItRec, false ) );
		lcItReceber.add( new GuardaCampo( txtCodCartCobItRec, "CodCartCob", "Cód.Cart.Cob.", ListaCampos.DB_FK, txtDescCartCobItRec, false ) );
		lcItReceber.montaSql( false, "ITRECEBER", "FN" );
		lcItReceber.setQueryCommit( false );
		txtNParcItRec.setListaCampos( lcItReceber );
		txtVlrParcItRec.setListaCampos( lcItReceber );
		txtVlrDescItRec.setListaCampos( lcItReceber );
		txtDtVencItRec.setListaCampos( lcItReceber );
		txtCodTipoCobItRec.setListaCampos( lcItReceber );
		txtCodBancoItRec.setListaCampos( lcItReceber );
		txtCodCartCobItRec.setListaCampos( lcItReceber );
		txtDescTipoCobItRec.setLabel( "Descrição do tipo de cobrança" );
		txtDescBancoItRec.setLabel( "Nome do banco" );
		txtDescCartCobItRec.setLabel( "Descrição da carteira de cobrança" );
	}

	private void montaTela() {

		c.add( tpn );

		tpn.add( "Fechamento", pnVenda );
		tpn.add( "Adicionais", pnAdic );
		tpn.add( "Frete", pnFrete );
		tpn.add( "Receber", pnReceber );

		/*
		 * ABA FECHAMENTO
		 */

		setPainel( pnVenda );

		adic( new JLabelPad( "Cód.comis." ), 7, 5, 250, 15 );
		adic( txtCodVend, 7, 20, 80, 20 );
		adic( new JLabelPad( "Nome do comissionado" ), 90, 5, 250, 15 );
		adic( txtNomeVend, 90, 20, 200, 20 );
		adic( new JLabelPad( "Cód.p.pag." ), 7, 45, 250, 15 );
		adic( txtCodPlanoPag, 7, 60, 80, 20 );
		adic( new JLabelPad( "Descrição da forma de pagamento" ), 90, 45, 250, 15 );
		adic( txtDescPlanoPag, 90, 60, 200, 20 );

		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );

		adic( lbLinha, 7, 90, 300, 2 );

		adic( new JLabelPad( "Valor total do cupom: " ), 7, 100, 150, 20 );
		adic( txtVlrCupom, 170, 100, 120, 20 );
		adic( new JLabelPad( "Valor em dinheiro: " ), 7, 125, 150, 20 );
		adic( txtVlrDinheiro, 170, 125, 120, 20 );
		adic( new JLabelPad( "Valor em cheque: " ), 7, 150, 150, 20 );
		adic( txtVlrCheque, 170, 150, 120, 20 );
		adic( ( lbChequeElet = new JLabelPad( "Valor em ch. elet./cartão: " ) ), 7, 175, 150, 20 );
		adic( txtVlrTef, 170, 175, 120, 20 );
		adic( new JLabelPad( "Valor pago: " ), 7, 200, 150, 20 );
		adic( txtVlrPago, 170, 200, 120, 20 );
		adic( new JLabelPad( "Valor troco: " ), 7, 225, 150, 20 );
		adic( txtVlrTroco, 170, 225, 120, 20 );

		/*
		 * ABA AUXILIAR
		 */

		setPainel( pnAdic );

		adic( new JLabelPad( "Nome" ), 7, 0, 240, 20 );
		adic( txtNomeCliAuxV, 7, 20, 285, 20 );
		adic( new JLabelPad( "Endereço" ), 7, 40, 240, 20 );
		adic( txtEndCliAuxV, 7, 60, 231, 20 );
		adic( new JLabelPad( "Num." ), 241, 40, 50, 20 );
		adic( txtNumCliAuxV, 241, 60, 53, 20 );
		adic( new JLabelPad( "CPF" ), 7, 80, 120, 20 );
		adic( txtCPFCliAuxV, 7, 100, 120, 20 );
		adic( new JLabelPad( "Cidade" ), 130, 80, 100, 20 );
		adic( txtCidCliAuxV, 130, 100, 120, 20 );
		adic( new JLabelPad( "UF" ), 253, 80, 40, 20 );
		adic( txtUFCliAuxV, 253, 100, 40, 20 );

		/*
		 * ABA FRETE
		 */

		setPainel( pnFrete );

		adic( new JLabelPad( "Cód.tran." ), 7, 0, 80, 20 );
		adic( txtCodTran, 7, 20, 80, 20 );
		adic( new JLabelPad( "Nome do transportador" ), 90, 0, 210, 20 );
		adic( txtDescTran, 90, 20, 210, 20 );
		adic( new JLabelPad( "Tipo" ), 7, 40, 170, 20 );
		adic( rgFreteVD, 7, 60, 130, 30 );
		adic( new JLabelPad( "Conhec." ), 140, 50, 77, 20 );
		adic( txtConhecFreteVD, 140, 70, 77, 20 );
		adic( new JLabelPad( "Placa" ), 220, 50, 80, 20 );
		adic( txtPlacaFreteVD, 220, 70, 80, 20 );
		adic( new JLabelPad( "Valor" ), 7, 90, 120, 20 );
		adic( txtVlrFreteVD, 7, 110, 120, 20 );
		adic( new JLabelPad( "Volumes" ), 130, 90, 77, 20 );
		adic( txtQtdFreteVD, 130, 110, 120, 20 );
		adic( new JLabelPad( "UF" ), 253, 90, 40, 20 );
		adic( txtUFFreteVD, 253, 110, 45, 20 );
		adic( new JLabelPad( "Peso B." ), 7, 130, 120, 20 );
		adic( txtPesoBrutVD, 7, 150, 120, 20 );
		adic( new JLabelPad( "Peso L." ), 130, 130, 120, 20 );
		adic( txtPesoLiqVD, 130, 150, 120, 20 );
		adic( new JLabelPad( "Espec." ), 7, 170, 120, 20 );
		adic( txtEspFreteVD, 7, 190, 120, 20 );
		adic( new JLabelPad( "Marca" ), 130, 170, 120, 20 );
		adic( txtMarcaFreteVD, 130, 190, 120, 20 );

		/*
		 * ABA RECEBER
		 */

		setPainel( pnReceber );

		JPanelPad pinTopRec = new JPanelPad( 400, 60 );
		pinTopRec.setPreferredSize( new Dimension( 400, 60 ) );

		pinTopRec.adic( new JLabelPad( "Valor Tot." ), 7, 0, 130, 20 );
		pinTopRec.adic( txtVlrParcRec, 7, 20, 130, 20 );

		txtVlrParcRec.setAtivo( false );

		pnReceber.add( pinTopRec, BorderLayout.NORTH );
		pnReceber.add( new JScrollPane( tabRec ), BorderLayout.CENTER );
	}

	private synchronized boolean fechaVenda() {

		boolean fechavenda = false;

		lcVendedor.carregaDados();
		lcClComis.carregaDados();

		fechamento : if ( validaFechamento() && fechamento() ) {

			if ( !ecf.iniciaFechamentoCupom() ) {
				break fechamento;
			}

			if ( txtVlrDinheiro.floatValue() > 0f && !ecf.efetuaFormaPagamento( "Dinheiro", txtVlrDinheiro.getVlrBigDecimal() ) ) {
				Funcoes.mensagemErro( this, ecf.getMessageLog() );
				break fechamento;
			}
			if ( txtVlrCheque.floatValue() > 0f && !ecf.efetuaFormaPagamento( "Cheque", txtVlrCheque.getVlrBigDecimal() ) ) {
				Funcoes.mensagemErro( this, ecf.getMessageLog() );
				break fechamento;
			}
			if ( txtVlrTef.floatValue() > 0f && !ecf.efetuaFormaPagamento( "Cartão", txtVlrTef.getVlrBigDecimal() ) ) {
				Funcoes.mensagemErro( this, ecf.getMessageLog() );
				break fechamento;
			}

			if ( ecf.finalizaFechamentoCupom( getMenssage() ) ) {
				if ( tef() ) {
					btCancel.setEnabled( false );
					finalizaVenda();
					gravaTroco();
					ecf.abrirGaveta();
				}
			}
			else {
				Funcoes.mensagemErro( this, ecf.getMessageLog() );
				break fechamento;
			}

			fechavenda = true;
		}

		return fechavenda;
	}

	private boolean validaFechamento() {

		if ( txtCodVend.getVlrInteger().intValue() == 0 ) {
			Funcoes.mensagemInforma( this, "Digite o código do comissionado!" );
			txtCodVend.requestFocus();
			return false;
		}
		else if ( txtCodPlanoPag.getVlrInteger().intValue() == 0 ) {
			Funcoes.mensagemInforma( this, "Digite o código da forma de pagamento!" );
			txtCodPlanoPag.requestFocus();
			return false;
		}
		else if ( txtVlrPago.floatValue() == 0 ) {
			Funcoes.mensagemInforma( this, "Digite o valor pago!" );
			txtVlrDinheiro.requestFocus();
			return false;
		}
		else if ( txtVlrPago.floatValue() < txtVlrCupom.floatValue() ) {
			Funcoes.mensagemInforma( this, "Valor pago menor que o valor da venda!" );
			txtVlrDinheiro.requestFocus();
			return false;
		}

		return true;
	}

	private boolean fechamento() {

		if ( bFrete ) {
			if ( lcFreteVD.getStatus() == ListaCampos.LCS_EDIT || lcFreteVD.getStatus() == ListaCampos.LCS_INSERT ) {
				if ( !lcFreteVD.post() ) {
					Funcoes.mensagemInforma( this, "Não foi possivel salvar frete!" );
					return false;
				}
				else {
					impMens = true;
				}
			}
			if ( lcAuxVenda.getStatus() == ListaCampos.LCS_EDIT || lcAuxVenda.getStatus() == ListaCampos.LCS_INSERT ) {
				if ( !lcAuxVenda.post() ) {
					return false;
				}
			}
		}

		if ( !gravaVenda() ) {
			return false;
		}

		// carrega o receber
		txtCodRec.setVlrInteger( getCodRec() );
		lcReceber.carregaDados();
		lcItReceber.carregaDados();
		lcTipoCobItRec.carregaDados();
		lcBancoItRec.carregaDados();
		lcCartCobItRec.carregaDados();

		try {
			// Fecha a venda:
			String sSQL = "EXECUTE PROCEDURE PVFECHAVENDASP(?,?,?,?,?,?)";
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PVMOVCAIXA" ) );
			ps.setObject( 3, txtVlrCupom.getVlrBigDecimal() );
			ps.setInt( 4, AplicativoPDV.iCodCaixa );
			ps.setDate( 5, Funcoes.dateToSQLDate( new Date() ) );
			ps.setString( 6, Aplicativo.getUsuario().getIdusu() );
			ps.execute();
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao executar fechamento!\n" + err.getMessage(), true, con, err );
			Logger.gravaLogTxt( "", Aplicativo.getUsuario().getIdusu(), Logger.LGEB_BD, "Erro ao executar fechamento." );
			return false;
		}

		return true;
	}

	private boolean gravaVenda() {

		try {
			StringBuilder sql = new StringBuilder();
			sql.append( "UPDATE VDVENDA SET " );
			sql.append( "STATUSVENDA='V2', " );
			sql.append( "CODVEND=?, " );
			sql.append( "CODEMPVD=?, " );
			sql.append( "CODFILIALVD=?, " );
			sql.append( "CODCLCOMIS=?, " );
			sql.append( "CODEMPCM=?, " );
			sql.append( "CODFILIALCM=?, " );
			sql.append( "CODPLANOPAG=?, " );
			sql.append( "CODEMPPG=?, " );
			sql.append( "CODFILIALPG=?, " );
			sql.append( "IMPNOTAVENDA='S' " );
			sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODVENDA=? AND TIPOVENDA='E'" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, txtCodVend.getVlrInteger().intValue() );
			ps.setInt( 2, lcVendedor.getCodEmp() );
			ps.setInt( 3, lcVendedor.getCodFilial() );
			ps.setInt( 4, txtCodClComis.getVlrInteger().intValue() );
			ps.setInt( 5, lcClComis.getCodEmp() );
			ps.setInt( 6, lcClComis.getCodFilial() );
			ps.setInt( 7, txtCodPlanoPag.getVlrInteger().intValue() );
			ps.setInt( 8, lcPlanoPag.getCodEmp() );
			ps.setInt( 9, lcPlanoPag.getCodFilial() );
			ps.setInt( 10, Aplicativo.iCodEmp );
			ps.setInt( 11, ListaCampos.getMasterFilial( "VDVENDA" ) );
			ps.setInt( 12, iCodVenda );
			ps.executeUpdate();
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
			Logger.gravaLogTxt( "", Aplicativo.getUsuario().getIdusu(), Logger.LGEB_BD, "Erro ao gravar a venda: " + err.getMessage() );
			return false;
		}

		return true;
	}

	private boolean finalizaVenda() {

		try {
			PreparedStatement ps = con.prepareStatement( "UPDATE VDVENDA SET STATUSVENDA='V3' WHERE CODEMP=? AND CODFILIAL=? AND CODVENDA=? AND TIPOVENDA='E'" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDVENDA" ) );
			ps.setInt( 3, iCodVenda );
			ps.executeUpdate();
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( null, "Não foi possível finalizar a venda!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
			return false;
		}

		return true;
	}

	private boolean gravaTroco() {

		if ( txtVlrTroco.floatValue() > 0 ) {
			return false;
		}

		try {
			PreparedStatement ps = con.prepareStatement( "EXECUTE PROCEDURE PVSANGRIASP(?,?,?,?,?,?)" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PVMOVCAIXA" ) );
			ps.setBigDecimal( 3, txtVlrTroco.getVlrBigDecimal() );
			ps.setInt( 4, AplicativoPDV.iCodCaixa );
			ps.setDate( 5, Funcoes.dateToSQLDate( new Date() ) );
			ps.setString( 6, Aplicativo.getUsuario().getIdusu() );
			ps.execute();
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao executar o troco!\n" + err.getMessage(), true, con, err );
			Logger.gravaLogTxt( "", Aplicativo.getUsuario().getIdusu(), Logger.LGEB_BD, "Erro ao executar o troco." );
			return false;
		}

		return true;
	}

	private boolean tef() {

		boolean btef = true;

		if ( AplicativoPDV.bTEFTerm ) {
			if ( txtVlrTef.floatValue() > 0 && tef != null ) {
				try {
					btef = tef.requestSale( iNumCupom, txtVlrTef.getVlrBigDecimal(), "RedeCard" );
				} catch ( RuntimeException e ) {
					e.printStackTrace();
					Funcoes.mensagemErro( this, e.getMessage(), true, con, e );
					btef = false;
				}
			}
		}

		return btef;
	}

	private void vinculaTef() {

		try {
			PreparedStatement ps = con.prepareStatement( "INSERT INTO VDTEF (CODEMP,CODFILIAL,TIPOVENDA,CODVENDA,NSUTEF,REDETEF,DTTRANSTEF,VLRTEF) VALUES (?,?,?,?,?,?,?,?)" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDTEF" ) );
			ps.setString( 3, "E" );
			ps.setInt( 4, iCodVenda );
			/*
			 * ps.setString( 5, Tef.retNsu( prop ) ); ps.setString( 6, Tef.retRede( prop ) ); ps.setDate( 7, Funcoes.dateToSQLDate( Tef.retData( prop ) ) ); ps.setBigDecimal( 8, Tef.retValor( prop ) );*
			 */
			ps.executeUpdate();
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
			Logger.gravaLogTxt( "", Aplicativo.getUsuario().getIdusu(), Logger.LGEB_BD, "Erro ao gravar tef vinculado no banco: " + err.getMessage() );
		}
	}

	private void calculaValorPago() {

		txtVlrPago.setVlrBigDecimal( txtVlrDinheiro.getVlrBigDecimal().add( txtVlrCheque.getVlrBigDecimal().add( txtVlrTef.getVlrBigDecimal() ) ) );
		txtVlrTroco.setVlrBigDecimal( txtVlrPago.getVlrBigDecimal().subtract( txtVlrCupom.getVlrBigDecimal() ) );
	}

	private void alteraRec() {

		if ( bUserRec ) {
			FPassword fpw = new FPassword( this, FPassword.ALT_PARC_VENDA, null, con );
			fpw.execShow();
			if ( !fpw.OK ) {
				return;
			}
			fpw.dispose();
		}

		lcItReceber.edit();

		DLFechaParcela dl = new DLFechaParcela( this, con );

		Object[] valores = new Object[] { txtVlrParcItRec.getVlrBigDecimal(), txtDtVencItRec.getVlrDate(), txtVlrDescItRec.getVlrBigDecimal(), txtCodTipoCobItRec.getVlrInteger(), txtCodBancoItRec.getVlrString(), txtCodCartCobItRec.getVlrString(), "N" };

		dl.setValores( valores );
		dl.setVisible( true );

		if ( dl.OK ) {

			valores = dl.getValores();

			txtVlrParcItRec.setVlrBigDecimal( (BigDecimal) valores[ DLFechaParcela.EFields.VALOR.ordinal() ] );
			txtDtVencItRec.setVlrDate( (Date) valores[ DLFechaParcela.EFields.DATA.ordinal() ] );
			txtVlrDescItRec.setVlrBigDecimal( (BigDecimal) valores[ DLFechaParcela.EFields.DESCONTO.ordinal() ] );
			txtCodTipoCobItRec.setVlrString( (String) valores[ DLFechaParcela.EFields.TIPOCOB.ordinal() ] );
			txtCodBancoItRec.setVlrString( (String) valores[ DLFechaParcela.EFields.BANCO.ordinal() ] );
			txtCodCartCobItRec.setVlrString( (String) valores[ DLFechaParcela.EFields.CARTCOB.ordinal() ] );
			txtAltUsuRec.setVlrString( "S" );
			lcItReceber.post();
			txtAltUsuRec.setVlrString( "N" );

			// Atualiza lcReceber
			if ( lcReceber.getStatus() == ListaCampos.LCS_EDIT ) {
				lcReceber.post(); // Caso o lcReceber estaja como edit executa o post que atualiza
			}
			else {
				lcReceber.carregaDados(); // Caso não, atualiza
			}
		}
		else {
			lcItReceber.cancel( true );
		}

		dl.dispose();
	}

	private int getCodAux() {

		int iRet = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;

		try {

			sSQL = "SELECT CODAUXV FROM VDAUXVENDA WHERE CODEMP=? AND CODFILIAL=? AND CODVENDA=? AND TIPOVENDA='E'";
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDAUXVENDA" ) );
			ps.setInt( 3, txtCodVenda.getVlrInteger().intValue() );
			rs = ps.executeQuery();

			if ( rs.next() ) {
				iRet = rs.getInt( "CodAuxV" );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar codaux.\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}

		return iRet;
	}

	private int getCodRec() {

		int iRetorno = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = con.prepareStatement( "SELECT CODREC FROM FNRECEBER WHERE TIPOVENDA='E' AND CODVENDA=? AND CODEMP=? AND CODFILIAL=?" );
			ps.setInt( 1, iCodVenda );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "FNRECEBER" ) );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				iRetorno = rs.getInt( "CodRec" );
			}

			ps.close();

			con.commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar o código da conta a receber!\n" + err.getMessage(), true, con, err );
		} finally {
			ps = null;
			rs = null;
		}

		return iRetorno;
	}

	private String getMenssage() {
		getDestaqueImposto();
		
		String sMenssage = "";
		
		if (vlrTotTrib != null && vlrTotTrib.compareTo( new BigDecimal(0)) == 1 ) {
			sMenssage = " Total Impostos Pagos R$"+ Funcoes.strDecimalToStrCurrency(2, String.valueOf(vlrTotTrib)).trim() + "(" + Funcoes.strDecimalToStrCurrency(2, String.valueOf(aliqTotTrib)).trim()
			+"%)Fonte:IBPT";
		}
		
		if ( trocouCli && impMens ) {

			String[] dadosCli = (String[]) param[ 6 ];

			sMenssage = ( dadosCli[ 0 ] != null ? dadosCli[ 0 ].trim() : "" ) + " - " + ( dadosCli[ 1 ] != null ? dadosCli[ 1 ].trim() : "" ) + "\n" + ( dadosCli[ 2 ] != null ? dadosCli[ 2 ].trim() : "" ) + " , " + ( dadosCli[ 3 ] != null ? dadosCli[ 3 ].trim() : "" ) + " - "
					+ ( dadosCli[ 4 ] != null ? dadosCli[ 4 ].trim() : "" ) + "/" + ( dadosCli[ 5 ] != null ? dadosCli[ 5 ].trim() : "" ) + "\n" + txtDescTran.getVlrString().trim() + " - " + txtPlacaFreteVD.getVlrString().trim();

		}
		else if ( txtNomeCliAuxV.getVlrString().trim().length() > 0 ) {

			sMenssage = txtNomeCliAuxV.getVlrString().trim() + " - " + txtCPFCliAuxV.getVlrString().trim() + "\n" + txtEndCliAuxV.getVlrString().trim() + " , " + txtNumCliAuxV.getVlrString().trim() + " - " + txtCidCliAuxV.getVlrString().trim() + "/" + txtUFCliAuxV.getVlrString().trim() + "\n"
					+ txtDescTran.getVlrString().trim() + " - " + txtPlacaFreteVD.getVlrString().trim();
		}

		if ( sMenssage.length() > 300 ) {
			sMenssage = sMenssage.substring( 0, 300 );
		}

		return sMenssage;
	}

	private void getDestaqueImposto() {
		//
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select (case when coalesce(pf.leitransp,'N') = 'C' then ");
			sql.append("(case when coalesce(tf.leitransp,'N') = 'S' then ");
			sql.append("(coalesce(vd.vlricmsvenda,0 ) ");
			sql.append("+ coalesce(vd.vlripivenda,0) ");
			sql.append("+ coalesce(vd.vlrpisvenda,0)");
			sql.append("+ coalesce(vd.vlricmsstvenda,0)");
			sql.append("+ coalesce(vd.vlrcofinsvenda,0)) else null end) " );
			sql.append(" when coalesce(pf.leitransp,'N') = 'I' then  ");
			sql.append("(case when coalesce(tf.leitransp,'N') = 'S' then ");
			sql.append("(select sum(lfi.vlrnacncm) from lfitvenda lfi where lfi.codemp=vd.codemp and lfi.codfilial=vd.codfilial and lfi.codvenda=vd.codvenda and lfi.tipovenda=vd.tipovenda)  ");
			sql.append(" +  (select sum(lfi.vlrimpncm) from lfitvenda lfi where lfi.codemp=vd.codemp and lfi.codfilial=vd.codfilial and lfi.codvenda=vd.codvenda and lfi.tipovenda=vd.tipovenda) else null end) "); 
			sql.append(" else null end) vTotTrib  ");
			sql.append("from ");
			sql.append("vdvenda vd  ");
			sql.append("left outer join sgfilial fi on fi.codemp=vd.codemp and fi.codfilial=vd.codfilial ");
			sql.append("left outer join sgprefere1 pf on fi.codemp=vd.codemp and pf.codfilial=fi.codfilial ");
			sql.append("left outer join vdcliente cl on ");
			sql.append("cl.codemp=vd.codempcl and cl.codfilial=vd.codfilialcl and cl.codcli=vd.codcli ");
			sql.append("left outer join lftipofisccli tf on tf.codemp=cl.codempfc and tf.codfilial=cl.codfilialfc and tf.codfisccli=cl.codfisccli ");
			
			sql.append("where ");
			sql.append("vd.codemp=? and vd.codfilial=? and vd.codvenda=? and vd.tipovenda=? ");
			
			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "lfitvenda" ) );
			ps.setInt( 3, txtCodVenda.getVlrInteger() );
			ps.setString( 4, txtTipoVenda.getVlrString() );
			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				vlrTotTrib = rs.getBigDecimal( "vTotTrib" );
			}

			rs.close();
			ps.close();
			con.commit();
			
			StringBuilder sqlAliq = new StringBuilder();
			sqlAliq.append("select case when lf.vlrnacncm<=0 then lf.aliqimpncm else lf.aliqnacncm ");
			sqlAliq.append("end aliq from lfitvenda lf where lf.codemp=? and lf.codfilial=? and lf.codvenda=? and lf.tipovenda=? ");
			
			PreparedStatement psAliq = con.prepareStatement( sqlAliq.toString() );
			psAliq.setInt( 1, Aplicativo.iCodEmp );
			psAliq.setInt( 2, ListaCampos.getMasterFilial( "lfitvenda" ) );
			psAliq.setInt( 3, txtCodVenda.getVlrInteger() );
			psAliq.setString( 4, txtTipoVenda.getVlrString() );
			ResultSet rsAliq = psAliq.executeQuery();

			if ( rsAliq.next() ) {
				aliqTotTrib = rsAliq.getBigDecimal( "aliq" );
			}

			psAliq.close();
			rsAliq.close();
			con.commit();
		
		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela valor tributário!\n" + err.getMessage(), true, con, err );
		}
	}
	
	private void getPreferencias() {

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT ADICPDV, HABRECEBER FROM SGPREFERE4 WHERE CODEMP=? AND CODFILIAL=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE4" ) );
			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				bFrete = "S".equals( rs.getString( "ADICPDV" ) );
				bReceber = "S".equals( rs.getString( "HABRECEBER" ) );
			}

			rs.close();
			ps.close();
			con.commit();

			ps = con.prepareStatement( "SELECT VERIFALTPARCVENDA FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				bUserRec = "S".equals( rs.getString( "VerifAltParcVenda" ) );
			}

			rs.close();
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE4!\n" + err.getMessage(), true, con, err );
		}
	}

	public boolean actionTef( final ControllerTefEvent e ) {

		boolean actionTef = false;

		if ( e.getAction() == TextTefAction.WARNING ) {
			Funcoes.mensagemInforma( this, e.getMessage() );
			actionTef = true;
		}
		else if ( e.getAction() == TextTefAction.ERROR ) {
			Funcoes.mensagemErro( this, e.getMessage() );
			actionTef = true;
		}
		else if ( e.getAction() == TextTefAction.BEGIN_PRINT ) {
			actionTef = ecf.abreComprovanteNaoFiscalVinculado( "Cartão", txtVlrTef.getVlrBigDecimal(), iNumCupom );
			comprovanteTef.delete( 0, comprovanteTef.length() );
		}
		else if ( e.getAction() == TextTefAction.PRINT ) {
			System.out.println( StringFunctions.clearAccents( e.getMessage().replace( "\n", "" ) ) + "]" );
			comprovanteTef.append( StringFunctions.clearAccents( e.getMessage() ) );
			actionTef = true;
		}
		else if ( e.getAction() == TextTefAction.END_PRINT ) {
			actionTef = ecf.usaComprovanteNaoFiscalVinculado( comprovanteTef.toString() );
			ecf.fecharRelatorioGerencial();
		}
		else if ( e.getAction() == TextTefAction.RE_PRINT ) {
			actionTef = ecf.fecharRelatorioGerencial();
			if ( actionTef ) {
				actionTef = ecf.abreComprovanteNaoFiscalVinculado( "Cartão", txtVlrTef.getVlrBigDecimal(), iNumCupom );
			}
		}
		else if ( e.getAction() == TextTefAction.CONFIRM ) {
			actionTef = Funcoes.mensagemConfirma( this, e.getMessage() ) == JOptionPane.YES_OPTION;
		}

		return actionTef;
	}

	public void actionPerformed( ActionEvent evt ) {

		boolean bRet = false;

		if ( evt.getSource() == btOK && !vendaFechada ) {
			if ( !vendaFechada ) {
				vendaFechada = fechaVenda();
				System.out.println( "\nFECHOU!\n" );
				if ( bReceber ) {
					tpn.setEnabledAt( 3, true );
					tpn.setSelectedIndex( 3 );
					bReceber = false;
				}
				else {
					super.actionPerformed( evt );
				}
			}
		}
		else {
			super.actionPerformed( evt );
		}
	}

	public void keyPressed( KeyEvent kevt ) {

		if ( kevt.getSource() == btOK ) {
			btOK.doClick();
		}
		super.keyPressed( kevt );
	}

	public void mouseClicked( MouseEvent e ) {

		if ( e.getClickCount() == 2 ) {
			if ( e.getSource() == tabRec && tabRec.getLinhaSel() >= 0 ) {
				alteraRec();
			}
		}
	}

	public void mouseEntered( MouseEvent e ) {

	}

	public void mouseExited( MouseEvent e ) {

	}

	public void mousePressed( MouseEvent e ) {

	}

	public void mouseReleased( MouseEvent e ) {

	}

	public void focusGained( FocusEvent arg0 ) {

	}

	public void focusLost( FocusEvent fevt ) {

		if ( fevt.getSource() == txtVlrDinheiro || fevt.getSource() == txtVlrCheque || fevt.getSource() == txtVlrTef ) {
			calculaValorPago();
		}
	}

	public void beforeCarrega( CarregaEvent e ) {

		if ( e.getListaCampos() == lcTran ) {
			txtPesoBrutVD.setVlrBigDecimal( pesoBrutFrete );
			txtPesoLiqVD.setVlrBigDecimal( pesoLiqFrete );
			txtVlrFreteVD.setVlrBigDecimal( vlrFrete );
		}
	}

	public void afterCarrega( CarregaEvent e ) {

		if ( e.getListaCampos() == lcVendedor ) {
			lcClComis.carregaDados();
		}
	}

	@ Override
	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcPlanoPag.setConexao( cn );
		lcVendedor.setConexao( cn );
		lcClComis.setConexao( cn );
		lcTran.setConexao( cn );
		lcFreteVD.setConexao( cn );
		lcAuxVenda.setConexao( cn );
		lcReceber.setConexao( cn );
		lcItReceber.setConexao( cn );
		lcTipoCobItRec.setConexao( cn );
		lcBancoItRec.setConexao( cn );
		lcCartCobItRec.setConexao( cn );

		txtCodVenda.setVlrInteger( new Integer( iCodVenda ) );
		txtTipoVenda.setVlrString( sTipoVenda );

		lcPlanoPag.carregaDados();
		lcVendedor.carregaDados();
		lcClComis.carregaDados();

		getPreferencias();
	}
}
