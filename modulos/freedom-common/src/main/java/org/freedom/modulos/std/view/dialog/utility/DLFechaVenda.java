/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)DLFechaVenda.java <BR>
 * 
 *                       Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                       modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                       na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                       Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                       sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                       Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                       Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                       de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                       Comentários sobre a classe...
 */

package org.freedom.modulos.std.view.dialog.utility;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JScrollPane;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.CheckBoxEvent;
import org.freedom.acao.CheckBoxListener;
import org.freedom.acao.RadioGroupEvent;
import org.freedom.acao.RadioGroupListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.object.Historico;
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
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPassword;
import org.freedom.modulos.fnc.view.dialog.utility.DLNovoRec;
import org.freedom.modulos.gms.view.frame.crud.detail.FConhecFrete;
import org.freedom.modulos.std.business.component.ComissaoEspecial;
import org.freedom.modulos.std.business.object.UpdateVenda;
import org.freedom.modulos.std.business.object.VdItVendaItVenda;
import org.freedom.modulos.std.dao.DAOVenda;

public class DLFechaVenda extends FFDialogo implements FocusListener, MouseListener, CheckBoxListener, RadioGroupListener, CarregaListener {

	private static final long serialVersionUID = 1L;

	private final int ABA_FECHAMENTO = 0;

	private final int ABA_FRETE = 1;

	private final int ABA_ADIC = 2;

	private final int ABA_RECEBER = 3;

	private final int ABA_COMISSAO = 4;

	private final JTabbedPanePad tpn = new JTabbedPanePad();

	private final JPanelPad pinFecha = new JPanelPad( 400, 300 );

	private final JPanelPad pinFrete = new JPanelPad( 400, 300 );

	private final JPanelPad pnReceber = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad pnComis = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	private final JPanelPad pinInfEspec = new JPanelPad( 0, 0 );

	private final JTextFieldPad txtCodVenda = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtCodVendaDoc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtVlrDescItVenda = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtPercDescVenda = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, 2 );

	private final JTextFieldPad txtVlrDescVenda = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtPercAdicVenda = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, 2 );

	private final JTextFieldPad txtVlrAdicVenda = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtVlrProdVenda = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtCodPlanoPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtCodTran = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtPlacaFreteVD = new JTextFieldPad( JTextFieldPad.TP_STRING, 7, 0 );

	private final JTextFieldPad txtUFFreteVD = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private final JTextFieldPad txtVlrFreteVD = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtVlrSegFreteVD = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtConhecFreteVD = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private final JTextFieldPad txtQtdFreteVD = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private final JTextFieldPad txtPesoBrutVD = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private final JTextFieldPad txtPesoLiqVD = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private final JTextFieldPad txtEspFreteVD = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldPad txtMarcaFreteVD = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldFK txtVlrIcmsFreteVD = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );
	
	private final JTextFieldPad txtRNTCVD = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	// private final JTextFieldPad txtPercIcmsFreteVD = new JTextFieldPad( JTextFieldPad.TP_NUMERIC, 15, Aplicativo.casasDecFin );

	private final JTextFieldPad txtCodAuxV = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private final JTextFieldPad txtCPFCliAuxV = new JTextFieldPad( JTextFieldPad.TP_STRING, 11, 0 );

	private final JTextFieldPad txtNomeCliAuxV = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCidCliAuxV = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private final JTextFieldPad txtUFCliAuxV = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private final JTextFieldPad txtCodRec = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtCodBanco = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private final JTextFieldPad txtCodBancoItRec = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private final JTextFieldPad txtCodCartCobItRec = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private final JTextFieldPad txtCodModBol = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldFK txtDescModBol = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtNParcItRec = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtVlrParcItRec = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtVlrDescItRec = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtVlrParcRec = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtDtVencItRec = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtDtPrevItRec = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtCodComi = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtVlrComi = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtDtVencComi = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtStatusVenda = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private final JTextFieldPad txtTipoVenda = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );
	
	private final JTextFieldPad txtSubTipoVenda = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );
	
	private final JTextFieldPad txtSitComplVenda = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private final JTextFieldPad txtTipoVendaDoc = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private final JTextFieldPad txtAltUsuRec = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private final JTextFieldPad txtDtEmisRec = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtDocRec = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldFK txtDescCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCodTipoCob = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtCodTipoCobItRec = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtCodCartCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private final JTextFieldFK txtDescCartCob = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldFK txtDescPlanoPag = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private final JTextFieldFK txtDescTran = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldFK txtDescBanco = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private final JTextFieldFK txtDescBancoItRec = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private final JTextFieldFK txtDescTipoCob = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private final JTextFieldFK txtDescTipoCobItRec = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private final JTextFieldFK txtDescCartCobItRec = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodTipoMov = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodTranMov = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDescTipoMov = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private final JCheckBoxPad cbImpPed = new JCheckBoxPad( "Imprime Pedido?", "S", "N" );

	private JCheckBoxPad cbEmiteNota = new JCheckBoxPad( "", "S", "N" );

	private final JCheckBoxPad cbEmiteBoleto = new JCheckBoxPad( "Emite Boleto?", "S", "N" );

	private final JCheckBoxPad cbEmiteRecibo = new JCheckBoxPad( "Emite Recibo?", "S", "N" );

	private final JCheckBoxPad cbImpReciboItRec = new JCheckBoxPad( "Imp.rec.", "S", "N" );

	private final JCheckBoxPad cbReEmiteNota = new JCheckBoxPad( "", "S", "N" );

	private final JCheckBoxPad cbAdicFrete = new JCheckBoxPad( "adiciona valor do frete na nota?", "S", "N" );

	private final JCheckBoxPad cbAdicICMSFrete = new JCheckBoxPad( "adiciona valor do frete na base de ICMS?", "S", "N" );

	private JButtonPad btGerarConhecimento = new JButtonPad( "Conhecimento Frete", Icone.novo( "btGerar.png" ) );

	private final JRadioGroup<?, ?> rgFreteVD;

	private final ListaCampos lcVenda = new ListaCampos( this );

	private final ListaCampos lcVendaDoc = new ListaCampos( this );

	private final ListaCampos lcPlanoPag = new ListaCampos( this, "PG" );

	private final ListaCampos lcTran = new ListaCampos( this, "TN" );

	private final ListaCampos lcAuxVenda = new ListaCampos( this );

	private final ListaCampos lcFreteVD = new ListaCampos( this );

	private final ListaCampos lcReceber = new ListaCampos( this );

	private final ListaCampos lcBanco = new ListaCampos( this, "BO" );

	private final ListaCampos lcTipoCob = new ListaCampos( this, "TC" );

	private final ListaCampos lcBancoItRec = new ListaCampos( this, "BO" );

	private final ListaCampos lcCartCobItRec = new ListaCampos( this, "CB" );

	private final ListaCampos lcTipoCobItRec = new ListaCampos( this, "TC" );

	private final ListaCampos lcCartCob = new ListaCampos( this, "CB" );

	private final ListaCampos lcItReceber = new ListaCampos( this );

	private final ListaCampos lcComis = new ListaCampos( this );

	private ListaCampos lcModBol = new ListaCampos( this );

	private final Navegador navItRec = new Navegador( false );

	private final Navegador navRec = new Navegador( false );

	private final Navegador navComis = new Navegador( false );

	private final JTablePad tabRec = new JTablePad();

	private final JTablePad tabComis = new JTablePad();

	private int iCodVendaFecha = 0;

	private boolean bCarregaReceber = true;

	private boolean bCarFrete = false;

	private Object oPrefs[] = null;

	private JCheckBoxPad cbDescPont = new JCheckBoxPad( "Desconto pontualidade?", "S", "N" );

	private JTextAreaPad txaObsItRec = new JTextAreaPad( 250 );

	private int icodTran = 0;

	private String modeloBoleto = "";

	private String modeloBoleto1 = "";

	private JTextFieldPad txtNumContaitrec = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldFK txtDescContaitrec = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNumConta = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtNumContaPag = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldFK txtDescConta = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private ListaCampos lcContaitrec = new ListaCampos( this, "CA" );

	private ListaCampos lcConta = new ListaCampos( this, "CA" );

	private Historico historico = null;

	private JTextFieldPad txtDocVenda = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 9, 0 );

	private JTextFieldPad txtDocVendaDoc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 9, 0 );

	private final JTextFieldPad txtObsrec = new JTextFieldPad( JTextFieldPad.TP_STRING, 250, 0 );

	private JTextFieldPad txtVlrLiqVenda = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );
	

	public static enum COL_RETDFV {
		CODPLANOPAG, VLRDESCVENDA, VLRADICVENDA, IMPPED, IMPNOTA, MODBOL1, IMPREC, MODBOL2, REIMPNOTA, IMPBOL, NUMCONTA
	};

	private BigDecimal volumes = null;

	private String codmarca = "";
	
	DAOVenda daovenda = null;
	
	public DLFechaVenda( DbConnection cn, Integer iCodVenda, Component cOrig, String impPed, String impNf, String impBol
			, String impRec, String reImpNf, Integer codtran, String tpFrete, BigDecimal volumes, boolean NFe, String codmarca ) {

		super( cOrig );

		setConexao( cn );
		daovenda = new DAOVenda( cn );

		if ( NFe ) {
			cbEmiteNota.setText( "Emite NFE?" );
			cbReEmiteNota.setText( "Consulta NFE?" );
		}
		else {
			cbEmiteNota.setText( "Imprime Nota?" );
			cbReEmiteNota.setText( "Reimprime Nota?" );
		}

		iCodVendaFecha = iCodVenda.intValue();
		icodTran = codtran.intValue();

		this.volumes = volumes;
		this.codmarca = codmarca;

		setTitulo( "Fechar Venda" );
		setAtribos( 405, 490 );

		oPrefs = prefs();
		
		
		// Desabilita o desconto no fechamento na venda caso o flag DesabDescFechaVD no Preferências Gerais seja verdadeiro.
		if ( (Boolean) oPrefs[ 7 ] ) {
			txtPercDescVenda.setSoLeitura(true);
			txtVlrDescVenda.setSoLeitura(true);				
		} else {
			txtPercDescVenda.setSoLeitura(false);
			txtVlrDescVenda.setSoLeitura(false);
		}

		lcItReceber.setMaster( lcReceber );
		lcReceber.adicDetalhe( lcItReceber );
		lcItReceber.setTabela( tabRec );
		lcComis.setMaster( lcReceber );
		lcReceber.adicDetalhe( lcComis );
		lcComis.setTabela( tabComis );

		navItRec.setName( "ItReceber" );
		lcItReceber.setNavegador( navItRec );
		navRec.setName( "Receber" );
		lcReceber.setNavegador( navRec );
		navComis.setName( "Comissão" );
		lcComis.setNavegador( navComis );

		c.add( tpn );

		pnComis.add( new JScrollPane( tabComis ) );

		tpn.add( "Fechamento", pinFecha );
		tpn.add( "Frete", pinFrete );
		tpn.add( "Inf. específicas", pinInfEspec );
		tpn.add( "Receber", pnReceber );
		tpn.add( "Comissão", pnComis );

		Vector<String> vVals = new Vector<String>();
		Vector<String> vLabs = new Vector<String>();
		vVals.addElement( "C" );
		vVals.addElement( "F" );
		vLabs.addElement( "CIF" );
		vLabs.addElement( "FOB" );

		rgFreteVD = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgFreteVD.setVlrString( tpFrete );

		txtCodPlanoPag.setNomeCampo( "CodPlanoPag" );
		lcPlanoPag.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_PK, false ) );
		lcPlanoPag.add( new GuardaCampo( txtDescPlanoPag, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPag.add( new GuardaCampo( txtNumContaPag, "Numconta", "Número da conta", ListaCampos.DB_SI, false ) );
		txtCodPlanoPag.setTabelaExterna( lcPlanoPag, null );
		txtCodPlanoPag.setFK( true );
		txtDescPlanoPag.setListaCampos( lcPlanoPag );
		lcPlanoPag.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag.setQueryCommit( false );
		lcPlanoPag.setReadOnly( true );
		lcPlanoPag.setConexao( cn );

		txtCodTran.setNomeCampo( "CodTran" );
		lcTran.add( new GuardaCampo( txtCodTran, "CodTran", "Cód.tran.", ListaCampos.DB_PK, false ) );
		lcTran.add( new GuardaCampo( txtDescTran, "RazTran", "Nome do transportador", ListaCampos.DB_SI, false ) );
		lcTran.add( new GuardaCampo( txtPlacaFreteVD, "PlacaTran", "Placa do veículo", ListaCampos.DB_SI, false ) );
		lcTran.add( new GuardaCampo( txtUFFreteVD, "SiglaUF", "UF", ListaCampos.DB_SI, false ) );
		lcTran.add( new GuardaCampo( txtRNTCVD, "RNTCTRAN", "RNTC", ListaCampos.DB_SI, false ) );

		txtDescTran.setListaCampos( lcTran );
		txtCodTran.setTabelaExterna( lcTran, null );
		txtCodTran.setFK( true );
		lcTran.montaSql( false, "TRANSP", "VD" );
		lcTran.setQueryCommit( false );
		lcTran.setReadOnly( true );
		lcTran.setConexao( cn );

		txtCodBanco.setNomeCampo( "CodBanco" );
		lcBanco.add( new GuardaCampo( txtCodBanco, "CodBanco", "Cód.banco", ListaCampos.DB_PK, false ) );
		lcBanco.add( new GuardaCampo( txtDescBanco, "NomeBanco", "Nome do banco", ListaCampos.DB_SI, false ) );
		lcBanco.add( new GuardaCampo( txtCodModBol, "CodModBol", "Cód.m.bl/rc.", ListaCampos.DB_SI, false ) );
		lcBanco.montaSql( false, "BANCO", "FN" );
		lcBanco.setQueryCommit( false );
		lcBanco.setReadOnly( true );
		lcBanco.setConexao( cn );
		txtCodBanco.setTabelaExterna( lcBanco, null );
		txtCodBanco.setListaCampos( lcBanco );
		txtDescBanco.setListaCampos( lcBanco );
		txtCodBanco.setFK( true );

		txtCodCartCob.setNomeCampo( "CodCartCob" );
		lcCartCob.add( new GuardaCampo( txtCodCartCob, "CodCartCob", "Cód.cart.cob", ListaCampos.DB_PK, false ) );
		lcCartCob.add( new GuardaCampo( txtDescCartCob, "DescCartCob", "Desc.Cart.Cob", ListaCampos.DB_SI, false ) );
		lcCartCob.setDinWhereAdic( "CODBANCO = #S", txtCodBanco );
		lcCartCob.montaSql( false, "CARTCOB", "FN" );
		lcCartCob.setQueryCommit( false );
		lcCartCob.setReadOnly( true );
		lcCartCob.setConexao( cn );
		txtCodCartCob.setTabelaExterna( lcCartCob, null );
		txtCodCartCob.setListaCampos( lcCartCob );
		txtDescCartCob.setListaCampos( lcCartCob );
		txtCodCartCob.setFK( true );

		txtCodTipoCob.setNomeCampo( "CodTipoCob" );
		lcTipoCob.add( new GuardaCampo( txtCodTipoCob, "CodTipoCob", "Cód.tp.cob.", ListaCampos.DB_PK, false ) );
		lcTipoCob.add( new GuardaCampo( txtDescTipoCob, "DescTipoCob", "Descrição do tipo de cobrança.", ListaCampos.DB_SI, false ) );
		lcTipoCob.montaSql( false, "TIPOCOB", "FN" );
		lcTipoCob.setQueryCommit( false );
		lcTipoCob.setReadOnly( true );
		lcTipoCob.setConexao( cn );
		txtCodTipoCob.setTabelaExterna( lcTipoCob, null );
		txtCodTipoCob.setListaCampos( lcTipoCob );
		txtDescTipoCob.setListaCampos( lcTipoCob );
		txtCodTipoCob.setFK( true );

		txtCodTipoCobItRec.setNomeCampo( "CodTipoCob" );
		lcTipoCobItRec.add( new GuardaCampo( txtCodTipoCobItRec, "CodTipoCob", "Cód.tp.cob.", ListaCampos.DB_PK, false ) );
		lcTipoCobItRec.add( new GuardaCampo( txtDescTipoCobItRec, "DescTipoCob", "Descrição do tipo de cobrança.", ListaCampos.DB_SI, false ) );
		lcTipoCobItRec.montaSql( false, "TIPOCOB", "FN" );
		lcTipoCobItRec.setQueryCommit( false );
		lcTipoCobItRec.setReadOnly( true );
		lcTipoCobItRec.setConexao( cn );
		txtCodTipoCobItRec.setTabelaExterna( lcTipoCobItRec, null );
		txtCodTipoCobItRec.setListaCampos( lcTipoCobItRec );
		txtDescTipoCobItRec.setListaCampos( lcTipoCobItRec );
		txtCodTipoCobItRec.setFK( true );

		txtCodBancoItRec.setNomeCampo( "CodBanco" );
		lcBancoItRec.add( new GuardaCampo( txtCodBancoItRec, "CodBanco", "Cód.banco", ListaCampos.DB_PK, false ) );
		lcBancoItRec.add( new GuardaCampo( txtDescBancoItRec, "NomeBanco", "Nome do banco", ListaCampos.DB_SI, false ) );
		lcBancoItRec.montaSql( false, "BANCO", "FN" );
		lcBancoItRec.setQueryCommit( false );
		lcBancoItRec.setReadOnly( true );
		lcBancoItRec.setConexao( cn );
		txtCodBancoItRec.setTabelaExterna( lcBancoItRec, null );
		txtCodBancoItRec.setListaCampos( lcBancoItRec );
		txtDescBancoItRec.setListaCampos( lcBancoItRec );
		txtCodBancoItRec.setFK( true );

		txtCodCartCobItRec.setNomeCampo( "CodCartCob" );
		lcCartCobItRec.add( new GuardaCampo( txtCodCartCobItRec, "CodCartCob", "Cód.Cart.Cob.", ListaCampos.DB_PK, false ) );
		lcCartCobItRec.add( new GuardaCampo( txtDescCartCobItRec, "DescCartCob", "Descrição da carteira de cobrança", ListaCampos.DB_SI, false ) );
		lcCartCobItRec.setWhereAdicSubSel( "CODBANCO=master.CODBANCO" );
		lcCartCobItRec.montaSql( false, "CARTCOB", "FN" );
		lcCartCobItRec.setQueryCommit( false );
		lcCartCobItRec.setReadOnly( true );
		lcCartCobItRec.setConexao( cn );
		txtCodCartCobItRec.setTabelaExterna( lcCartCobItRec, null );
		txtCodCartCobItRec.setListaCampos( lcCartCobItRec );
		txtDescCartCobItRec.setListaCampos( lcCartCobItRec );
		txtCodCartCobItRec.setFK( true );

		lcContaitrec.add( new GuardaCampo( txtNumContaitrec, "NumConta", "Nº Conta", ListaCampos.DB_PK, false ) );
		lcContaitrec.add( new GuardaCampo( txtDescContaitrec, "DescConta", "Descrição da conta", ListaCampos.DB_SI, false ) );
		lcContaitrec.montaSql( false, "CONTA", "FN" );
		lcContaitrec.setReadOnly( true );
		lcContaitrec.setConexao( cn );
		txtNumContaitrec.setTabelaExterna( lcContaitrec, null );
		txtNumContaitrec.setFK( true );
		txtNumContaitrec.setNomeCampo( "NumConta" );

		lcConta.add( new GuardaCampo( txtNumConta, "NumConta", "Nº Conta", ListaCampos.DB_PK, false ) );
		lcConta.add( new GuardaCampo( txtDescConta, "DescConta", "Descrição da conta", ListaCampos.DB_SI, false ) );
		lcConta.montaSql( false, "CONTA", "FN" );
		lcConta.setReadOnly( true );
		lcConta.setConexao( cn );
		txtNumConta.setTabelaExterna( lcConta, null );
		txtNumConta.setFK( true );
		txtNumConta.setNomeCampo( "NumConta" );

		txtCodPlanoPag.setTabelaExterna( lcPlanoPag, null );

		lcVenda.add( new GuardaCampo( txtTipoVenda, "TipoVenda", "Tp.venda", ListaCampos.DB_PK, false ) );
		lcVenda.add( new GuardaCampo( txtCodVenda, "CodVenda", "N.pedido", ListaCampos.DB_PK, false ) );
		lcVenda.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cod.p.pag.", ListaCampos.DB_FK, txtDescPlanoPag, false ) );
		lcVenda.add( new GuardaCampo( txtVlrDescItVenda, "VlrDescItVenda", "% Desc it.", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtVlrDescVenda, "VlrDescVenda", "% Desc it.", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtVlrAdicVenda, "VlrAdicVenda", "% Adic.", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtVlrProdVenda, "VlrProdVenda", "V.prod.", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtVlrLiqVenda, "VlrLiqVenda", "V.liq.", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtCodTipoCob, "CodTipoCob", "Cod.tp.cob.", ListaCampos.DB_FK, txtDescTipoCob, false ) );
		lcVenda.add( new GuardaCampo( txtCodBanco, "CodBanco", "Cód.banco", ListaCampos.DB_FK, txtDescBanco, false ) );
		lcVenda.add( new GuardaCampo( txtCodCartCob, "CodCartCob", "Cód.cart.cob", ListaCampos.DB_FK, txtDescCartCob, false ) );
		lcVenda.add( new GuardaCampo( txtStatusVenda, "StatusVenda", "Status", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtNumConta, "NumConta", "Cod.Conta", ListaCampos.DB_FK, txtDescConta, false ) );
		lcVenda.add( new GuardaCampo( txtObsrec, "obsrec", "Obs.rec.", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtDocVenda, "DocVenda", "N doc.", ListaCampos.DB_SI, false ) );
		
		lcVenda.add( new GuardaCampo( txtSubTipoVenda, "SubTipoVenda", "subtipovenda.", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtSitComplVenda, "SitComplVenda", "sitcomplvenda.", ListaCampos.DB_SI, false ) );
		
		

		lcVenda.montaSql( false, "VENDA", "VD" );
		lcVenda.setConexao( cn );
		txtCodVenda.setNomeCampo( "CodVenda" );
		txtVlrAdicVenda.setListaCampos( lcVenda );
		txtPercAdicVenda.setListaCampos( lcVenda );
		txtVlrDescVenda.setListaCampos( lcVenda );
		txtPercDescVenda.setListaCampos( lcVenda );
		txtStatusVenda.setListaCampos( lcVenda );
		txtCodPlanoPag.setListaCampos( lcVenda );
		txtNumConta.setListaCampos( lcVenda );

		// Lista Campos Auxiliar para atualização do numero do documento
		lcVendaDoc.add( new GuardaCampo( txtTipoVendaDoc, "TipoVenda", "Tp.venda", ListaCampos.DB_PK, false ) );
		lcVendaDoc.add( new GuardaCampo( txtCodVendaDoc, "CodVenda", "N.pedido", ListaCampos.DB_PK, false ) );
		lcVendaDoc.add( new GuardaCampo( txtDocVendaDoc, "DocVenda", "N doc.", ListaCampos.DB_SI, false ) );

		lcVendaDoc.montaSql( false, "VENDA", "VD" );
		lcVendaDoc.setConexao( cn );
		txtCodVendaDoc.setNomeCampo( "CodVenda" );

		lcFreteVD.add( new GuardaCampo( txtTipoVenda, "TipoVenda", "Tipo", ListaCampos.DB_PK, false ) );
		lcFreteVD.add( new GuardaCampo( txtCodVenda, "CodVenda", "N.pedido", ListaCampos.DB_PK, false ) );
		lcFreteVD.add( new GuardaCampo( rgFreteVD, "TipoFreteVD", "Tipo", ListaCampos.DB_SI, true ) );
		lcFreteVD.add( new GuardaCampo( txtConhecFreteVD, "ConhecFreteVD", "Conhec.", ListaCampos.DB_SI, false ) );
		lcFreteVD.add( new GuardaCampo( txtPlacaFreteVD, "PlacaFreteVD", "Placa", ListaCampos.DB_SI, false ) );
		lcFreteVD.add( new GuardaCampo( txtUFFreteVD, "UFFreteVD", "UF", ListaCampos.DB_SI, true ) );
		lcFreteVD.add( new GuardaCampo( txtVlrFreteVD, "VlrFreteVD", "Valor", ListaCampos.DB_SI, true ) );
		lcFreteVD.add( new GuardaCampo( txtVlrSegFreteVD, "vlrsegfretevd", "Vlr.Seguro", ListaCampos.DB_SI, true ) );
		lcFreteVD.add( new GuardaCampo( txtQtdFreteVD, "QtdFreteVD", "Qtd.", ListaCampos.DB_SI, true ) );
		lcFreteVD.add( new GuardaCampo( txtPesoBrutVD, "PesoBrutVD", "Peso bruto", ListaCampos.DB_SI, true ) );
		lcFreteVD.add( new GuardaCampo( txtPesoLiqVD, "PesoLiqVD", "Peso liq.", ListaCampos.DB_SI, true ) );
		lcFreteVD.add( new GuardaCampo( txtEspFreteVD, "EspFreteVD", "Esp.fiscal", ListaCampos.DB_SI, true ) );
		lcFreteVD.add( new GuardaCampo( txtMarcaFreteVD, "MarcaFreteVD", "Marca", ListaCampos.DB_SI, true ) );
		lcFreteVD.add( new GuardaCampo( cbAdicFrete, "AdicFreteVD", "frete na nota", ListaCampos.DB_SI, true ) );
		lcFreteVD.add( new GuardaCampo( cbAdicICMSFrete, "AdicFreteBaseICM", "frete no icms", ListaCampos.DB_SI, true ) );
		// lcFreteVD.add( new GuardaCampo( txtPercIcmsFreteVD, "AliqICMSFreteVD", "aliquota", ListaCampos.DB_SI, false ) );
		lcFreteVD.add( new GuardaCampo( txtVlrIcmsFreteVD, "VlrIcmsFreteVD", "valor icms", ListaCampos.DB_SI, false ) );
		lcFreteVD.add( new GuardaCampo( txtCodTran, "codtran", "Cód.Transp.", ListaCampos.DB_FK, false ) );
		lcFreteVD.add( new GuardaCampo( txtRNTCVD, "RNTCVD", "RNTC", ListaCampos.DB_SI, false ) );

		lcFreteVD.montaSql( false, "FRETEVD", "VD" );
		lcFreteVD.setConexao( cn );
		rgFreteVD.setListaCampos( lcFreteVD );
		txtPlacaFreteVD.setListaCampos( lcFreteVD );
		txtUFFreteVD.setListaCampos( lcFreteVD );
		txtVlrFreteVD.setListaCampos( lcFreteVD );
		txtVlrSegFreteVD.setListaCampos( lcFreteVD );
		txtQtdFreteVD.setListaCampos( lcFreteVD );
		txtPesoBrutVD.setListaCampos( lcFreteVD );
		txtPesoLiqVD.setListaCampos( lcFreteVD );
		txtEspFreteVD.setListaCampos( lcFreteVD );
		txtMarcaFreteVD.setListaCampos( lcFreteVD );
		txtRNTCVD.setListaCampos( lcFreteVD );
		txtConhecFreteVD.setListaCampos( lcFreteVD );
		cbAdicFrete.setListaCampos( lcFreteVD );
		cbAdicICMSFrete.setListaCampos( lcFreteVD );
		// txtPercIcmsFreteVD.setListaCampos( lcFreteVD );
		txtVlrIcmsFreteVD.setListaCampos( lcFreteVD );
		txtPlacaFreteVD.setStrMascara( "###-####" );

		cbAdicFrete.setVlrString( "S" );
		cbAdicICMSFrete.setVlrString( "N" );

		lcAuxVenda.add( new GuardaCampo( txtTipoVenda, "TipoVenda", "Tp.venda", ListaCampos.DB_PK, false ) );
		lcAuxVenda.add( new GuardaCampo( txtCodVenda, "CodVenda", "N.pedido", ListaCampos.DB_PK, false ) );
		lcAuxVenda.add( new GuardaCampo( txtCodAuxV, "CodAuxV", "Cód.aux.", ListaCampos.DB_PK, false ) );
		lcAuxVenda.add( new GuardaCampo( txtCPFCliAuxV, "CPFCliAuxV", "CPF", ListaCampos.DB_SI, false ) );
		lcAuxVenda.add( new GuardaCampo( txtNomeCliAuxV, "NomeCliAuxV", "Nome", ListaCampos.DB_SI, false ) );
		lcAuxVenda.add( new GuardaCampo( txtCidCliAuxV, "CidCliAuxV", "Cidade", ListaCampos.DB_SI, false ) );
		lcAuxVenda.add( new GuardaCampo( txtUFCliAuxV, "UFCliAuxV", "UF", ListaCampos.DB_SI, false ) );
		lcAuxVenda.montaSql( false, "AUXVENDA", "VD" );
		lcAuxVenda.setConexao( cn );
		txtCodAuxV.setListaCampos( lcAuxVenda );
		txtCPFCliAuxV.setListaCampos( lcAuxVenda );
		txtNomeCliAuxV.setListaCampos( lcAuxVenda );
		txtCidCliAuxV.setListaCampos( lcAuxVenda );
		txtUFCliAuxV.setListaCampos( lcAuxVenda );
		txtCPFCliAuxV.setMascara( JTextFieldPad.MC_CPF );

		JPanelPad pinTopRec = new JPanelPad( 400, 60 );
		pinTopRec.setPreferredSize( new Dimension( 400, 60 ) );
		pnReceber.add( pinTopRec, BorderLayout.NORTH );
		JScrollPane spnTabRec = new JScrollPane( tabRec );
		pnReceber.add( spnTabRec, BorderLayout.CENTER );

		txtVlrParcRec.setAtivo( false );

		pinTopRec.adic( new JLabelPad( "Valor Tot." ), 7, 0, 130, 20 );
		pinTopRec.adic( txtVlrParcRec, 7, 20, 130, 20 );

		txtCodRec.setNomeCampo( "CodRec" );
		lcReceber.add( new GuardaCampo( txtCodRec, "CodRec", "Cód.rec.", ListaCampos.DB_PK, false ) );
		lcReceber.add( new GuardaCampo( txtVlrParcRec, "VlrParcRec", "Valor tot.", ListaCampos.DB_SI, false ) );
		lcReceber.add( new GuardaCampo( txtTipoVenda, "TipoVenda", "Tp.venda", ListaCampos.DB_SI, false ) );
		lcReceber.add( new GuardaCampo( txtAltUsuRec, "AltUsuRec", "Usu.alt.", ListaCampos.DB_SI, false ) );
		lcReceber.add( new GuardaCampo( txtDtEmisRec, "DataRec", "Dt.emissão", ListaCampos.DB_SI, true ) );
		lcReceber.add( new GuardaCampo( txtDocRec, "DocRec", "N.doc.", ListaCampos.DB_SI, true ) );
		lcReceber.add( new GuardaCampo( txtObsrec, "obsrec", "Obs.", ListaCampos.DB_SI, false ) );

		lcReceber.setQueryCommit( true );
		lcReceber.montaSql( false, "RECEBER", "FN" );
		lcReceber.setConexao( cn );
		txtCodRec.setListaCampos( lcReceber );
		txtVlrParcRec.setListaCampos( lcReceber );
		txtTipoVenda.setListaCampos( lcReceber );
		txtAltUsuRec.setListaCampos( lcReceber );

		txtNParcItRec.setNomeCampo( "NParcItRec" );
		lcItReceber.add( new GuardaCampo( txtNParcItRec, "NParcItRec", "N.parc.", ListaCampos.DB_PK, false ) );
		lcItReceber.add( new GuardaCampo( cbImpReciboItRec, "ImpReciboItRec", "Imp.rec.", ListaCampos.DB_SI, false ) );
		lcItReceber.add( new GuardaCampo( txtVlrParcItRec, "VlrParcItRec", "Valor tot.", ListaCampos.DB_SI, false ) );
		lcItReceber.add( new GuardaCampo( txtDtVencItRec, "DtVencItRec", "Dt. vencto.", ListaCampos.DB_SI, false ) );
		lcItReceber.add( new GuardaCampo( txtDtPrevItRec, "DtPrevItRec", "Dt. prev.", ListaCampos.DB_SI, false ) );
		lcItReceber.add( new GuardaCampo( txtVlrDescItRec, "VlrDescItRec", "Valor desc.", ListaCampos.DB_SI, false ) );
		lcItReceber.add( new GuardaCampo( txtCodTipoCobItRec, "CodTipoCob", "Cod.tp.cob.", ListaCampos.DB_FK, txtDescTipoCobItRec, false ) );
		lcItReceber.add( new GuardaCampo( txtCodBancoItRec, "CodBanco", "Cód.banco", ListaCampos.DB_FK, txtDescBancoItRec, false ) );
		lcItReceber.add( new GuardaCampo( txtCodCartCobItRec, "CodCartCob", "Cód.Cart.Cob.", ListaCampos.DB_FK, txtDescCartCobItRec, false ) );
		lcItReceber.add( new GuardaCampo( txtNumContaitrec, "NumConta", "Cód.Conta", ListaCampos.DB_FK, txtDescContaitrec, false ) );
		lcItReceber.add( new GuardaCampo( cbDescPont, "DescPont", "Desc.Pont", ListaCampos.DB_SI, false ) );
		lcItReceber.add( new GuardaCampo( txaObsItRec, "ObsItRec", "Obs.", ListaCampos.DB_SI, false ) );

		lcItReceber.montaSql( false, "ITRECEBER", "FN" );
		lcItReceber.setQueryCommit( false );
		txtNParcItRec.setListaCampos( lcItReceber );
		txtVlrParcItRec.setListaCampos( lcItReceber );
		txtVlrDescItRec.setListaCampos( lcItReceber );
		txtDtVencItRec.setListaCampos( lcItReceber );
		txtCodTipoCobItRec.setListaCampos( lcItReceber );
		txtCodBancoItRec.setListaCampos( lcItReceber );
		txtCodCartCobItRec.setListaCampos( lcItReceber );
		txtNumContaitrec.setListaCampos( lcItReceber );
		txtDescTipoCobItRec.setLabel( "Descrição do tipo de cobrança" );
		txtDescBancoItRec.setLabel( "Nome do banco" );
		txtDescCartCobItRec.setLabel( "Descrição da carteira de cobrança" );
		lcItReceber.montaTab();
		lcItReceber.setConexao( cn );
		tabRec.setColunaEditavel( 1, true );
		tabRec.addMouseListener( this );

		txtCodComi.setNomeCampo( "CodComi" );
		lcComis.add( new GuardaCampo( txtCodComi, "CodComi", "Cód.comis.", ListaCampos.DB_PK, false ) );
		lcComis.add( new GuardaCampo( txtVlrComi, "VlrComi", "Valor da comissão", ListaCampos.DB_SI, false ) );
		lcComis.add( new GuardaCampo( txtDtVencComi, "DtVencComi", "Dt.vencto.", ListaCampos.DB_SI, false ) );
		lcComis.montaSql( false, "COMISSAO", "VD" );
		lcComis.setQueryCommit( false );
		lcComis.montaTab();
		lcComis.setConexao( cn );

		lcModBol.add( new GuardaCampo( txtCodModBol, "CodModBol", "Cód.mod.", ListaCampos.DB_PK, false ) );
		lcModBol.add( new GuardaCampo( txtDescModBol, "DescModBol", "Descrição do modelo de boleto", ListaCampos.DB_SI, false ) );
		lcModBol.setReadOnly( true );
		lcModBol.montaSql( false, "MODBOLETO", "FN" );
		txtCodModBol.setTabelaExterna( lcModBol, null );
		txtCodModBol.setFK( true );
		txtCodModBol.setNomeCampo( "CodModBol" );
		lcModBol.setConexao( cn );

		tabComis.addMouseListener( this );

		txtTipoVenda.setVlrString( "V" );
		txtCodVenda.setVlrInteger( iCodVenda );

		lcVenda.carregaDados();
		
		//System.out.println("Numconta: "+txtNumConta.getVlrString());

		txtTipoVendaDoc.setVlrString( "V" );
		txtCodVendaDoc.setVlrInteger( iCodVenda );

		lcVendaDoc.carregaDados();
		
		getDadosCli();
		
		cbEmiteNota.addCheckBoxListener( this );
		cbImpPed.addCheckBoxListener( this );
		cbEmiteBoleto.addCheckBoxListener( this );
		cbEmiteRecibo.addCheckBoxListener( this );
		cbReEmiteNota.addCheckBoxListener( this );
		cbAdicICMSFrete.addCheckBoxListener( this );

		// Carrega o frete

		lcFreteVD.setReadOnly( true );

		if ( lcFreteVD.carregaDados() ) {
			lcFreteVD.setReadOnly( false );
			lcFreteVD.setState( ListaCampos.LCS_SELECT );
			if (txtPesoBrutVD.getVlrBigDecimal().doubleValue()==0 && txtPesoLiqVD.getVlrBigDecimal().doubleValue()==0 ) {
				bCarFrete = false;
			} else {
				bCarFrete = true;
			}
		}
		else {
			lcFreteVD.setReadOnly( false );
		}
		/*
		 * if ( ( txtCodTran.getVlrInteger() == null ) || ( txtCodTran.getVlrInteger().intValue() == 0 ) ) { txtCodTran.setVlrInteger( icodTran ); } lcTran.carregaDados();
		 */
		// Carrega o aux
		int iCodAux = getCodAux();
		if ( iCodAux > 0 ) {
			txtCodAuxV.setVlrInteger( new Integer( iCodAux ) );
			lcAuxVenda.carregaDados();
		}
		else {
			txtCodAuxV.setVlrInteger( new Integer( 1 ) );
		}

		setPainel( pinFecha );
		adic( new JLabelPad( "Cód.p.pg." ), 7, 0, 100, 20 );
		adic( txtCodPlanoPag, 7, 20, 100, 20 );
		adic( new JLabelPad( "Descrição do plano de pagamento" ), 110, 0, 270, 20 );
		adic( txtDescPlanoPag, 110, 20, 250, 20 );

		adic( new JLabelPad( "Cód.tp.cob." ), 7, 40, 100, 20 );
		adic( txtCodTipoCob, 7, 60, 100, 20 );
		adic( new JLabelPad( "Descrição do Tipo de cobrança" ), 110, 40, 250, 20 );
		adic( txtDescTipoCob, 110, 60, 250, 20 );

		adic( new JLabelPad( "Cód.banco" ), 7, 80, 100, 20 );
		adic( txtCodBanco, 7, 100, 100, 20 );
		adic( new JLabelPad( "Descrição do Banco" ), 110, 80, 250, 20 );
		adic( txtDescBanco, 110, 100, 250, 20 );

		adic( new JLabelPad( "Cód. Cart. cob" ), 7, 120, 100, 20 );
		adic( txtCodCartCob, 7, 140, 100, 20 );
		adic( new JLabelPad( "Descriçao da Carteira de cobrança" ), 110, 120, 250, 20 );
		adic( txtDescCartCob, 110, 140, 250, 20 );

		adic( new JLabelPad( "Nºconta" ), 7, 160, 250, 20 );
		adic( txtNumConta, 7, 180, 100, 20 );
		adic( new JLabelPad( "Descrição da conta" ), 110, 160, 250, 20 );
		adic( txtDescConta, 110, 180, 250, 20 );

		adic( new JLabel( "Cód.Modelo" ), 7, 200, 100, 20 );
		adic( txtCodModBol, 7, 220, 100, 20 );
		adic( new JLabel( "Descrição do modelo de recibo" ), 110, 200, 250, 20 );
		adic( txtDescModBol, 110, 220, 250, 20 );

		adic( new JLabel( "% Desc." ), 7, 240, 100, 20 );
		adic( txtPercDescVenda, 7, 260, 100, 20 );
		adic( new JLabelPad( "V Desc." ), 110, 240, 100, 20 );
		adic( txtVlrDescVenda, 110, 260, 100, 20 );

		adic( new JLabelPad( "% Adic." ), 7, 280, 100, 20 );
		adic( txtPercAdicVenda, 7, 300, 100, 20 );
		adic( new JLabelPad( "V Adic." ), 110, 280, 100, 20 );
		adic( txtVlrAdicVenda, 110, 300, 100, 20 );

		adic( cbImpPed, 230, 250, 150, 20 );
		adic( cbEmiteNota, 230, 270, 150, 20 );
		adic( cbEmiteBoleto, 230, 290, 150, 20 );
		adic( cbEmiteRecibo, 230, 310, 150, 20 );
		adic( cbReEmiteNota, 230, 330, 150, 20 );
		// adic( cbDescPont, 7, 250, 180, 20 );

		setPainel( pinFrete );

		adic( txtCodTran, 7, 20, 80, 20, "Cód.tran." );
		adic( txtDescTran, 90, 20, 270, 20, "Nome do transportador" );
		adic( rgFreteVD, 7, 60, 130, 30, "Tipo" );
		adic( cbAdicFrete, 150, 60, 220, 30 );

		adic( txtConhecFreteVD, 7, 110, 175, 20, "Conhec." );
		adic( txtPlacaFreteVD, 185, 110, 86, 20, "Placa" );
		adic( txtUFFreteVD, 274, 110, 86, 20, "UF" );

		adic( txtVlrFreteVD, 7, 150, 56, 20, "Valor" );

		adic( txtVlrSegFreteVD, 67, 150, 56, 20, "Seguro" );

		adic( txtQtdFreteVD, 126, 150, 56, 20, "Volumes" );

		adic( txtPesoBrutVD, 185, 150, 86, 20, "Peso B." );
		adic( txtPesoLiqVD, 274, 150, 86, 20, "Peso L." );
		adic( txtEspFreteVD, 7, 190, 175, 20, "Espec." );
		adic( txtMarcaFreteVD, 185, 190, 175, 20, "Marca" );
		adic( txtRNTCVD, 7, 230, 175, 20, "RNTC (ANTT)" );
		
		if ( (Boolean) oPrefs[ 3 ] ) {
			adic( cbAdicICMSFrete, 7, 220, 300, 20 );
			adic( new JLabelPad( "% icms" ), 7, 240, 70, 20 );
			// adic( txtPercIcmsFreteVD, 7, 260, 70, 20 );
			adic( new JLabelPad( "Valor do icms" ), 80, 240, 90, 20 );
			adic( txtVlrIcmsFreteVD, 80, 260, 90, 20 );
		}

		adic( btGerarConhecimento, 185, 255, 175, 30 );

		Funcoes.setBordReq( rgFreteVD );
		Funcoes.setBordReq( txtPlacaFreteVD );
		Funcoes.setBordReq( txtUFFreteVD );
		Funcoes.setBordReq( txtVlrFreteVD );
		Funcoes.setBordReq( txtVlrSegFreteVD );
		Funcoes.setBordReq( txtQtdFreteVD );
		Funcoes.setBordReq( txtPesoBrutVD );
		Funcoes.setBordReq( txtPesoLiqVD );
		Funcoes.setBordReq( txtEspFreteVD );
		Funcoes.setBordReq( txtMarcaFreteVD );
		Funcoes.setBordReq( txtRNTCVD );
		
		rgFreteVD.addRadioGroupListener( this );

		setPainel( pinInfEspec );

		adic( new JLabelPad( "Nome" ), 7, 0, 240, 20 );
		adic( txtNomeCliAuxV, 7, 20, 240, 20 );
		adic( new JLabelPad( "CPF" ), 250, 0, 100, 20 );
		adic( txtCPFCliAuxV, 250, 20, 100, 20 );
		adic( new JLabelPad( "Cidade" ), 7, 40, 300, 20 );
		adic( txtCidCliAuxV, 7, 60, 300, 20 );
		adic( new JLabelPad( "UF" ), 310, 40, 40, 20 );
		adic( txtUFCliAuxV, 310, 60, 40, 20 );

		if ( txtVlrDescItVenda.getVlrBigDecimal().doubleValue() > 0 ) {
			txtPercDescVenda.setAtivo( false );
			txtVlrDescVenda.setAtivo( false );
		}

		tpn.setEnabledAt( 1, false );
		tpn.setEnabledAt( 2, false );
		tpn.setEnabledAt( 3, false );
		tpn.setEnabledAt( 4, false );

		btGerarConhecimento.addActionListener( this );

		txtPercDescVenda.addFocusListener( this );
		txtVlrDescVenda.addFocusListener( this );
		txtPercAdicVenda.addFocusListener( this );
		txtVlrAdicVenda.addFocusListener( this );
		txtVlrFreteVD.addFocusListener( this );

		cbImpPed.setVlrString( impPed );
		cbEmiteNota.setVlrString( impNf );
		cbEmiteBoleto.setVlrString( impBol );
		cbEmiteRecibo.setVlrString( impRec );
		cbReEmiteNota.setVlrString( reImpNf );

		lcVenda.edit();
		lcFreteVD.addCarregaListener( this );
		lcTran.addCarregaListener( this );
	}

	private void calcPeso() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();

		BigDecimal bLiq = new BigDecimal( "0" );
		BigDecimal bBrut = new BigDecimal( "0" );

		try {

			sSQL.append( "SELECT SUM(I.QTDITVENDA * P.PESOLIQPROD) AS TOTPESOLIQ, " );
			sSQL.append( "SUM(I.QTDITVENDA * P.PESOBRUTPROD) AS TOTPESOBRUT " );
			sSQL.append( "FROM VDITVENDA I,EQPRODUTO P " );
			sSQL.append( "WHERE I.CODVENDA=? AND I.CODEMP=? AND I.CODFILIAL=? AND I.TIPOVENDA=? ");
			sSQL.append( " AND P.CODEMP=I.CODEMPPD AND P.CODFILIAL=I.CODFILIALPD AND P.CODPROD=I.CODPROD" );

			lcFreteVD.edit();

			if ( icodTran == 0 ) { // se não tiver transportadora no tipo de movimento, pega do cliente.

				txtCodTran.setVlrInteger( new Integer( getCodTran() ) );
				lcTran.carregaDados();
			}

			if ( txtPlacaFreteVD.getVlrString() == null || "".equals( txtPlacaFreteVD.getVlrString() ) ) {
				txtPlacaFreteVD.setVlrString( "*******" );
			}

			if ( txtUFFreteVD.getVlrString() == null || "".equals( txtUFFreteVD.getVlrString() ) ) {
				txtUFFreteVD.setVlrString( "**" );
			}

			if ( (Boolean) oPrefs[ 4 ] ) {
				txtQtdFreteVD.setVlrBigDecimal( this.volumes );
			}
			else {
				txtQtdFreteVD.setVlrBigDecimal( new BigDecimal( "0" ) );
			}

			if ( txtVlrFreteVD.getText()==null || "".equals( txtVlrFreteVD.getText().trim() ) ) {
				txtVlrFreteVD.setVlrBigDecimal( new BigDecimal( "0" ) );
			}

			txtVlrSegFreteVD.setVlrBigDecimal( new BigDecimal( "0" ) );

			txtEspFreteVD.setVlrString( "Volume" );

			if ( this.codmarca != null ) {
				txtMarcaFreteVD.setVlrString( this.codmarca );
			}
			else {
				txtMarcaFreteVD.setVlrString( "**********" );
			}

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, iCodVendaFecha );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "VDITVENDA" ) );
			ps.setString( 4, txtTipoVenda.getVlrString() );

			rs = ps.executeQuery();

			if ( rs.next() ) {

				bLiq = new BigDecimal( rs.getString( "TOTPESOLIQ" ) != null ? rs.getString( "TOTPESOLIQ" ) : "0" );
				bBrut = new BigDecimal( rs.getString( "TOTPESOBRUT" ) != null ? rs.getString( "TOTPESOBRUT" ) : "0" );
				bLiq = bLiq.setScale( Aplicativo.casasDec, BigDecimal.ROUND_HALF_UP );
				bBrut = bBrut.setScale( Aplicativo.casasDec, BigDecimal.ROUND_HALF_UP );
				txtPesoLiqVD.setVlrBigDecimal( bLiq );
				txtPesoBrutVD.setVlrBigDecimal( bBrut );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao calcular o peso!\n" + err.getMessage(), true, con, err );
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
			bLiq = null;
			bBrut = null;
		}
	}

	private void geraHistoricoRec() {

		// Gerando histórico dinâmico

		try {

			lcVendaDoc.carregaDados();

			Integer codhistrec = null;

			codhistrec = (Integer) oPrefs[ 5 ];

			if ( codhistrec != 0 ) {
				historico = new Historico( codhistrec, con );
			}
			else {
				historico = new Historico();
				historico.setHistoricocodificado( DLNovoRec.HISTORICO_PADRAO );
			}

			historico.setData( txtDtEmisRec.getVlrDate() );
			historico.setDocumento( txtDocVendaDoc.getVlrString() );
			historico.setPortador( txtDescCli.getVlrString() );
			historico.setValor( txtVlrLiqVenda.getVlrBigDecimal() );
			historico.setHistoricoant( "" );

			txtObsrec.setVlrString( historico.getHistoricodecodificado() );
			txaObsItRec.setVlrString( historico.getHistoricodecodificado() );

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	private boolean finaliza() {

		boolean bRet = false;

		if ( lcReceber.getStatus() == ListaCampos.LCS_EDIT ) {

			lcReceber.post();
		}
		
		if ( "N".equals( cbReEmiteNota.getVlrString() ) ) {

			lcVenda.edit();

			// geraHistoricoRec();

			if ( "V2".equals( txtStatusVenda.getVlrString() ) ) {

				txtStatusVenda.setVlrString( "V3" );
			}
			else if ( "P2".equals( txtStatusVenda.getVlrString() ) ) {

				txtStatusVenda.setVlrString( "P3" );
			}

			bRet = lcVenda.post();
		}
		else {

			bRet = true;
		}

		return bRet;
	}

	private void gravaVenda() {

		try {
			
			if("NC".equals( txtSubTipoVenda.getVlrString() ) && "I".equals( txtSitComplVenda.getVlrString() )) {
		
				VdItVendaItVenda vendaitvenda = daovenda.getAmarracao( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "VDVENDA" ), txtTipoVenda.getVlrString(), txtCodVenda.getVlrInteger() );
				lcVenda.cancelCarrega();
				daovenda.updateNotaComplementar( vendaitvenda.getCodempvo(), vendaitvenda.getCodfilialvo(), vendaitvenda.
						getTipovendavo(), vendaitvenda.getCodvendavo(), vendaitvenda.getCodvenda() );
				UpdateVenda valores = daovenda.getValoresCabecalhoVenda( vendaitvenda.getCodemp(), vendaitvenda.getCodfilial(), vendaitvenda.
						getTipovenda(), vendaitvenda.getCodvenda() );
				
				txtVlrLiqVenda.setVlrBigDecimal( valores.getVlrliqprodvenda() );
				txtVlrProdVenda.setVlrBigDecimal( valores.getVlrprodvenda() );
				txtVlrAdicVenda.setVlrBigDecimal( valores.getVlradicvenda() );
				txtVlrDescVenda.setVlrBigDecimal( valores.getVlrdescvenda() );
				// Define o padrão da situação da nota complementar como C = processo Completo.
				txtSitComplVenda.setVlrString( "C" );
				
				//daovenda.updateCabecalhoNotaComplementar(vendaitvenda.getCodempvo(), vendaitvenda.getCodfilialvo(), vendaitvenda.
				//		getTipovendavo(), vendaitvenda.getCodvendavo(), vendaitvenda.getCodvenda() );
				
				}
		
			
			if ( "N".equals( cbReEmiteNota.getVlrString() ) ) {

				txtPlacaFreteVD.getVlrString();
				txtRNTCVD.getVlrString();
				if ( lcFreteVD.getStatus() == ListaCampos.LCS_EDIT || lcFreteVD.getStatus() == ListaCampos.LCS_INSERT ) {
					lcFreteVD.post();
				}
				//System.out.println(txtNumConta.getVlrString());
			
				lcVenda.edit();
					
				if ( "S".equals( cbEmiteNota.getVlrString() ) ) {
					txtStatusVenda.setVlrString( "V2" );
				}
				else if ( "P".equals( txtStatusVenda.getVlrString().substring( 0, 1 ) ) ) {
					txtStatusVenda.setVlrString( "P2" );
				}
				else if ( "V".equals( txtStatusVenda.getVlrString().substring( 0, 1 ) ) ) {
					txtStatusVenda.setVlrString( "V2" );
				}

				// geraHistoricoRec();

				lcVenda.post();
				if ( lcAuxVenda.getStatus() == ListaCampos.LCS_EDIT || lcAuxVenda.getStatus() == ListaCampos.LCS_INSERT ) {
					lcAuxVenda.post();
				}

			}
			
			int iCodRec = getCodRec();

			if ( iCodRec > 0 ) {
				txtCodRec.setVlrInteger( new Integer( iCodRec ) );
				if ( "S".equals( cbEmiteRecibo.getVlrString() ) ) {
					gravaImpRecibo( iCodRec, 1, new Boolean( true ) );
				}

				lcReceber.carregaDados();
				lcReceber.edit();

				if ( lcReceber.getStatus() == ListaCampos.LCS_EDIT ) {

					geraHistoricoRec();
					if ( "".equals(txtNumContaitrec.getVlrString())) {
						txtNumContaitrec.setVlrString( txtNumConta.getVlrString() );
					}

					lcReceber.post(); // Caso o lcReceber estaja como edit executa o post que atualiza
					lcReceber.carregaDados();
					lcItReceber.carregaDados();

				}

			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private void alteraReceber() {

		lcItReceber.edit();

		DLFechaParcela dl = new DLFechaParcela( this, con );

		Object[] valores = new Object[] { txtVlrParcItRec.getVlrBigDecimal(), txtDtVencItRec.getVlrDate(), txtVlrDescItRec.getVlrBigDecimal(), txtCodTipoCobItRec.getVlrInteger(), txtCodBancoItRec.getVlrString(), txtCodCartCobItRec.getVlrString(), cbDescPont.getVlrString(),
				txtDtPrevItRec.getVlrDate(), txaObsItRec.getVlrString()

		};

		dl.setValores( valores );
		dl.setVisible( true );

		if ( dl.OK ) {

			valores = dl.getValores();

			txtVlrParcItRec.setVlrBigDecimal( (BigDecimal) valores[ DLFechaParcela.EFields.VALOR.ordinal() ] );
			txtDtVencItRec.setVlrDate( (Date) valores[ DLFechaParcela.EFields.DATA.ordinal() ] );
			txtDtPrevItRec.setVlrDate( (Date) valores[ DLFechaParcela.EFields.DATAPREV.ordinal() ] );
			txtVlrDescItRec.setVlrBigDecimal( (BigDecimal) valores[ DLFechaParcela.EFields.DESCONTO.ordinal() ] );
			txtCodTipoCobItRec.setVlrString( (String) valores[ DLFechaParcela.EFields.TIPOCOB.ordinal() ] );
			txtCodBancoItRec.setVlrString( (String) valores[ DLFechaParcela.EFields.BANCO.ordinal() ] );
			txtCodCartCobItRec.setVlrString( (String) valores[ DLFechaParcela.EFields.CARTCOB.ordinal() ] );
			cbDescPont.setVlrString( (String) valores[ DLFechaParcela.EFields.DESCPONT.ordinal() ] );
			txaObsItRec.setVlrString( (String) valores[ DLFechaParcela.EFields.OBSITREC.ordinal() ] );

			txtAltUsuRec.setVlrString( "S" );

			lcItReceber.post();

			txtAltUsuRec.setVlrString( "N" );

			// Atualiza lcReceber
			if ( lcReceber.getStatus() == ListaCampos.LCS_EDIT ) {

				geraHistoricoRec();

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

	private void gravaImpRecibo( int codrec, int nparcitrec, boolean imprecibo ) {

		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		sql.append( "UPDATE FNITRECEBER IR SET IMPRECIBOITREC=? " );
		sql.append( "WHERE IR.CODEMP=? AND IR.CODFILIAL=? AND IR.CODREC=? AND IR.NPARCITREC=?" );
		try {
			ps = con.prepareStatement( sql.toString() );
			if ( imprecibo ) {
				ps.setString( 1, "S" );
			}
			else {
				ps.setString( 1, "N" );
			}
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "FNITRECEBER" ) );
			ps.setInt( 4, codrec );
			ps.setInt( 5, nparcitrec );
			ps.executeUpdate();
			ps.close();
			con.commit();
		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Não foi possível gravar informações do recibo!\n" + e.getMessage() );
		}

	}

	private void alteraComis() {

		lcComis.edit();

		DLFechaParcela dl = new DLFechaParcela( this, con );

		Object[] valores = new Object[] { txtVlrComi.getVlrBigDecimal(), txtDtVencComi.getVlrDate(), txtVlrDescItRec.getVlrBigDecimal(), txtCodTipoCob.getVlrInteger(), txtCodBanco.getVlrString(), "" // para evitar erro na leitura do array
		};

		dl.setValores( valores );
		dl.setVisible( true );

		if ( dl.OK ) {

			valores = dl.getValores();

			txtVlrComi.setVlrBigDecimal( (BigDecimal) valores[ DLFechaParcela.EFields.VALOR.ordinal() ] );
			txtDtVencComi.setVlrDate( (Date) valores[ DLFechaParcela.EFields.DATA.ordinal() ] );
			lcComis.post();
		}
		else {

			lcComis.cancel( false );
		}

		dl.dispose();
	}

	private boolean prox() {

		boolean bRet = false;
		
		switch ( tpn.getSelectedIndex() ) {

			case ABA_FECHAMENTO :

				txtCodVenda.setVlrInteger( new Integer( iCodVendaFecha ) );
				txtTipoVenda.setVlrString( "V" );

				if ( ! ( txtCodTran.getVlrInteger() > 0 ) ) {

					lcFreteVD.insert( false );
					txtCodTran.setVlrInteger( icodTran );
					lcTran.carregaDados();

				}

				if ( cbEmiteRecibo.getVlrString().equals( "S" ) ) {
					if ( txtCodModBol.getVlrString().equals( "" ) ) {
						Funcoes.mensagemInforma( this, "Selecione o modelo de recibo!" );
						break;
					}
					modeloBoleto = txtCodModBol.getVlrString();
				}

				if ( cbEmiteBoleto.getVlrString().equals( "S" ) ) {
					if ( txtCodModBol.getVlrString().equals( "" ) ) {
						Funcoes.mensagemInforma( this, "Selecione o modelo de boleto!" );
						break;
					}
					modeloBoleto1 = txtCodModBol.getVlrString();
				}
			
				// frete
				if ( (Boolean) oPrefs[ 0 ] ) {
					if ( !bCarFrete ) {
						calcPeso();
						getTipoFrete();
					}
					tpn.setEnabledAt( 0, false );
					tpn.setEnabledAt( 1, true );
					tpn.setEnabledAt( 2, false );
					tpn.setEnabledAt( 3, false );
					tpn.setEnabledAt( 4, false );
					tpn.setSelectedIndex( 1 );

				}
				// adicional
				else if ( (Boolean) oPrefs[ 1 ] ) {
					tpn.setEnabledAt( 0, false );
					tpn.setEnabledAt( 1, false );
					tpn.setEnabledAt( 2, true );
					tpn.setEnabledAt( 3, false );
					tpn.setEnabledAt( 4, false );
					tpn.setSelectedIndex( 2 );
				}
				else {
					gravaVenda();
					tpn.setEnabledAt( 0, false );
					tpn.setEnabledAt( 1, false );
					tpn.setEnabledAt( 2, false );
					tpn.setEnabledAt( 3, true );
					tpn.setEnabledAt( 4, true );
					tpn.setSelectedIndex( 3 );
					btCancel.setEnabled( false );
				}

				break;
			case ABA_FRETE :

				if ( (Boolean) oPrefs[ 0 ] ) {
					lcFreteVD.edit();
				}
				// adicional

				if ( txtPlacaFreteVD.getVlrString() == null || "".equals( txtPlacaFreteVD.getVlrString() ) ) {
					txtPlacaFreteVD.setVlrString( "*******" );
				}

				if ( (Boolean) oPrefs[ 1 ] ) {
					tpn.setEnabledAt( 0, false );
					tpn.setEnabledAt( 1, false );
					tpn.setEnabledAt( 2, true );
					tpn.setEnabledAt( 3, false );
					tpn.setEnabledAt( 4, false );
					tpn.setSelectedIndex( 2 );
				}
				else {
					gravaVenda();
					tpn.setEnabledAt( 0, false );
					tpn.setEnabledAt( 1, false );
					tpn.setEnabledAt( 2, false );
					tpn.setEnabledAt( 3, true );
					tpn.setEnabledAt( 4, true );
					tpn.setSelectedIndex( 3 );
					btCancel.setEnabled( false );
				}

				break;
			case ABA_ADIC :

				gravaVenda();
				tpn.setEnabledAt( 0, false );
				tpn.setEnabledAt( 1, false );
				tpn.setEnabledAt( 2, false );
				tpn.setEnabledAt( 3, true );
				tpn.setEnabledAt( 4, true );
				tpn.setSelectedIndex( 3 );
				btCancel.setEnabled( false );

				break;
			default :
				
				gravaVenda();

				if ( (Boolean) oPrefs[ 6 ] ) {

					/************* COMISSIONAMENTO ESPECIAL *****************/

					ComissaoEspecial comissao_especial = new ComissaoEspecial( txtCodVenda.getVlrInteger(), txtTipoVenda.getVlrString(), txtVlrLiqVenda.getVlrBigDecimal(), txtDtEmisRec.getVlrDate(), txtDtEmisRec.getVlrDate() );

					comissao_especial.processaComissao();

					/******************************************************/
				}

				bRet = finaliza();
		}

		return bRet;
	}

	private Object[] prefs() {

		Object[] ret = new Object[ 8 ];
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = con.prepareStatement( "SELECT TABFRETEVD,TABADICVD,VERIFALTPARCVENDA,ADICFRETEBASEICM, SOMAVOLUMES, CODHISTREC, COALESCE(ESPECIALCOMIS,'N') ESPECIALCOMIS, DESABDESCFECHAVD FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

			rs = ps.executeQuery();

			if ( rs.next() ) {

				ret[ 0 ] = new Boolean( rs.getString( "TABFRETEVD" ).trim().equals( "S" ) );
				ret[ 1 ] = new Boolean( rs.getString( "TABADICVD" ).trim().equals( "S" ) );
				ret[ 2 ] = new Boolean( rs.getString( "VERIFALTPARCVENDA" ).trim().equals( "S" ) );
				ret[ 3 ] = new Boolean( rs.getString( "ADICFRETEBASEICM" ).trim().equals( "S" ) );
				ret[ 4 ] = new Boolean( rs.getString( "SOMAVOLUMES" ).trim().equals( "S" ) );
				ret[ 5 ] = new Integer( rs.getInt( "CODHISTREC" ) );
				ret[ 6 ] = new Boolean( rs.getString( "ESPECIALCOMIS" ).trim().equals( "S" ) );
				ret[ 7 ] = new Boolean( rs.getString( "DESABDESCFECHAVD" ).trim().equals( "S" ) );
				

			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage(), true, con, err );
		} finally {
			ps = null;
			rs = null;
		}

		return ret;
	}

	private int getCodRec() {

		int iRetorno = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = con.prepareStatement( "SELECT CODREC FROM FNRECEBER WHERE TIPOVENDA='V' AND CODVENDA=? AND CODEMP=? AND CODFILIAL=?" );
			ps.setInt( 1, iCodVendaFecha );
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

	private int getCodAux() {

		int iRet = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = con.prepareStatement( "SELECT CODAUXV FROM VDAUXVENDA WHERE CODEMP=? AND CODFILIAL=? AND CODVENDA=? AND TIPOVENDA='V'" );
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
		}

		return iRet;
	}

	private int getCodTran() {

		int iRetorno = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;

		try {

			sSQL = "SELECT C.CODTRAN " + "FROM VDCLIENTE C, VDVENDA V " + "WHERE C.CODCLI=V.CODCLI AND C.CODEMP=V.CODEMPCL " + "AND V.CODVENDA=? AND TIPOVENDA='V' AND V.CODEMP=? AND V.CODFILIAL=?";

			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, iCodVendaFecha );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "VDVENDA" ) );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				iRetorno = rs.getInt( "CodTran" );
			}

			if ( iRetorno == 0 ) {

				sSQL = "SELECT CODTRAN FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=? ";

				ps = con.prepareStatement( sSQL );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, Aplicativo.iCodFilial );

				rs = ps.executeQuery();

				if ( rs.next() ) {
					iRetorno = rs.getInt( "CodTran" );
				}
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar o código da Transportadora do cliente!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}

		return iRetorno;
	}

	private void getTipoFrete() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();

		try {

			sSQL.append( "SELECT T.CTIPOFRETE FROM EQTIPOMOV T, VDVENDA V " );
			sSQL.append( "WHERE T.CODEMP=V.CODEMPTM AND T.CODFILIAL=V.CODFILIALTM AND T.CODTIPOMOV=V.CODTIPOMOV " );
			sSQL.append( "AND V.CODEMP=? AND V.CODFILIAL=? AND V.CODVENDA=? AND V.TIPOVENDA='V'" );

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQTIPOMOV" ) );
			ps.setInt( 3, iCodVendaFecha );
			rs = ps.executeQuery();

			if ( rs.next() ) {
				if ( !"".equals( rs.getString( 1 ) ) && rs.getString( 1 ) != null ) {
					rgFreteVD.setVlrString( rs.getString( 1 ).equals( "F" ) ? "F" : "C" );
				}
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( Exception err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar o código da conta a receber!\n" + err.getMessage(), true, con, err );
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}
	}

	public void getDadosCli() {

		StringBuilder sSQL = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;

		if ( "P1".equals( txtStatusVenda.getVlrString() ) || "V1".equals( txtStatusVenda.getVlrString() ) ) {
			if ( "".equals(txtNumConta.getText().trim() ) && ( ! ("".equals(txtNumContaPag.getText().trim() ) ) ) ) {
				txtNumConta.setVlrString( txtNumContaPag.getVlrString() );
				lcConta.carregaDados();
			}
			try {

				sSQL.append( "SELECT C.CODTIPOCOB, C.CODBANCO, C.CODCARTCOB, C.RAZCLI " );
				sSQL.append( "FROM VDCLIENTE C, VDVENDA V " );
				sSQL.append( "WHERE C.CODEMP=V.CODEMPCL AND C.CODFILIAL=V.CODFILIALCL AND C.CODCLI=V.CODCLI " );
				sSQL.append( "AND V.CODEMP=? AND V.CODFILIAL=? AND CODVENDA=? AND TIPOVENDA='V'" );

				ps = con.prepareStatement( sSQL.toString() );

				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
				ps.setInt( 3, txtCodVenda.getVlrInteger() );
				rs = ps.executeQuery();

				if ( rs.next() ) {

					txtCodBanco.setVlrString( rs.getString( "CODBANCO" ) );
					txtCodTipoCob.setVlrString( rs.getString( "CODTIPOCOB" ) );
					txtCodCartCob.setVlrString( rs.getString( "CODCARTCOB" ) );
					txtDescCli.setVlrString( rs.getString( "RAZCLI" ) );

					lcBanco.carregaDados();
					lcTipoCob.carregaDados();
					lcCartCob.carregaDados();

				}
			} catch ( Exception e ) {

				Funcoes.mensagemErro( this, "Erro ao carregar dados do cliente \n" + e.getMessage() );
				e.printStackTrace();

			}
			
		}
	}

	public List<Integer> getParcRecibo() {

		List<Integer> lsRet = new ArrayList<Integer>();
		Boolean sel = new Boolean( false );
		for ( int i = 0; i < tabRec.getNumLinhas(); i++ ) {
			sel = (Boolean) tabRec.getValor( i, 1 );
			if ( sel ) {
				lsRet.add( (Integer) tabRec.getValor( i, 0 ) ); // Coluna da parcela
			}
		}
		return lsRet;
	}

	public String[] getValores() {

		String[] sRetorno = new String[ 11 ];
		sRetorno[ COL_RETDFV.CODPLANOPAG.ordinal() ] = txtCodPlanoPag.getVlrString();
		sRetorno[ COL_RETDFV.VLRDESCVENDA.ordinal() ] = txtVlrDescVenda.getVlrString();
		sRetorno[ COL_RETDFV.VLRADICVENDA.ordinal() ] = txtVlrAdicVenda.getVlrString();
		sRetorno[ COL_RETDFV.IMPPED.ordinal() ] = cbImpPed.getVlrString();
		sRetorno[ COL_RETDFV.IMPNOTA.ordinal() ] = cbEmiteNota.getVlrString();
		sRetorno[ COL_RETDFV.MODBOL1.ordinal() ] = modeloBoleto1;
		sRetorno[ COL_RETDFV.IMPREC.ordinal() ] = cbEmiteRecibo.getVlrString();
		sRetorno[ COL_RETDFV.MODBOL2.ordinal() ] = modeloBoleto;
		sRetorno[ COL_RETDFV.REIMPNOTA.ordinal() ] = cbReEmiteNota.getVlrString();
		sRetorno[ COL_RETDFV.IMPBOL.ordinal() ] = cbEmiteBoleto.getVlrString();
		sRetorno[ COL_RETDFV.NUMCONTA.ordinal() ] = txtNumConta.getVlrString();
		return sRetorno;
	}

	/*
	 * private void calculaIcmsFrete() { if ( "S".equals( cbAdicICMSFrete.getVlrString() ) ) { BigDecimal icms = txtVlrFreteVD.getVlrBigDecimal().divide( new BigDecimal( "100.00" ) ).multiply( txtPercIcmsFreteVD.getVlrBigDecimal() ); txtVlrIcmsFreteVD.setVlrBigDecimal( icms ); } }
	 */

	private void gerarConhecimentoFrete() {

		prox();

		FConhecFrete tela = null;

		if ( Aplicativo.telaPrincipal.temTela( FConhecFrete.class.getName() ) ) {
			tela = (FConhecFrete) Aplicativo.telaPrincipal.getTela( FConhecFrete.class.getName() );
		}
		else {
			tela = new FConhecFrete();
			Aplicativo.telaPrincipal.criatela( "Conhecimento de Frete", tela, con );
		}

		tela.gerarConhecimentoFrete( txtCodVenda.getVlrInteger(), txtTipoVenda.getVlrString() );
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			if ( prox() ) {
				super.actionPerformed( evt );
			}
		}
		else if ( evt.getSource() == btGerarConhecimento ) {
			gerarConhecimentoFrete();
		}
		else {
			super.actionPerformed( evt );
		}
	}

	public void focusLost( FocusEvent fevt ) {

		if ( fevt.getSource() == txtPercDescVenda ) {
			if ( txtPercDescVenda.getText().trim().length() < 1 ) {
				txtVlrDescVenda.setAtivo( true );
			}
			else {
				txtVlrDescVenda.setVlrBigDecimal( txtVlrProdVenda.getVlrBigDecimal().multiply( txtPercDescVenda.getVlrBigDecimal() ).divide( new BigDecimal( "100" ), 3, BigDecimal.ROUND_HALF_UP ) );
				txtVlrDescVenda.setAtivo( false );
			}
		}
		else if ( fevt.getSource() == txtPercAdicVenda ) {
			if ( txtPercAdicVenda.getText().trim().length() < 1 ) {
				txtVlrAdicVenda.setAtivo( true );
			}
			else {
				txtVlrAdicVenda.setVlrBigDecimal( txtVlrProdVenda.getVlrBigDecimal().multiply( txtPercAdicVenda.getVlrBigDecimal() ).divide( new BigDecimal( "100" ), 3, BigDecimal.ROUND_HALF_UP ) );
				txtVlrAdicVenda.setAtivo( false );
			}
		}
		/*
		 * else if ( fevt.getSource() == txtVlrFreteVD ) { calculaIcmsFrete(); }
		 */
	}

	public void focusGained( FocusEvent fevt ) {

	}

	public void mouseClicked( MouseEvent mevt ) {

		JTablePad tab = (JTablePad) mevt.getSource();
		String imprecibo = "N";
		if ( mevt.getClickCount() == 1 ) {
			gravaImpRecibo( txtCodRec.getVlrInteger(), (Integer) tabRec.getValor( tabRec.getLinhaSel(), 0 ), (Boolean) tabRec.getValor( tabRec.getLinhaSel(), 1 ) );
		}
		else if ( mevt.getClickCount() == 2 ) {
			if ( tab == tabRec && tabRec.getLinhaSel() >= 0 ) {
				if ( (Boolean) oPrefs[ 2 ] ) {
					FPassword fpw = new FPassword( this, FPassword.ALT_PARC_VENDA, null, con );
					fpw.execShow();
					if ( fpw.OK ) {
						alteraReceber();
					}
					fpw.dispose();
				}
				else {
					alteraReceber();
				}
			}
			else if ( tab == tabComis && tabComis.getLinhaSel() >= 0 ) {
				alteraComis();
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

	public void valorAlterado( CheckBoxEvent evt ) {

		if ( evt.getCheckBox() == cbReEmiteNota ) {
			if ( cbReEmiteNota.getVlrString().equals( "S" ) ) {
				cbEmiteBoleto.setVlrString( "N" );
				cbEmiteRecibo.setVlrString( "N" );
				cbEmiteNota.setVlrString( "N" );
				//cbImpPed.setVlrString( "N" );
				txtCodModBol.setSoLeitura( true );
				txtCodModBol.setRequerido( false );
			}
		}
		else if ( ( evt.getCheckBox() == cbEmiteNota ) || ( evt.getCheckBox() == cbEmiteBoleto ) || ( evt.getCheckBox() == cbEmiteRecibo ) || ( evt.getCheckBox() == cbImpPed ) ) {
			if ( ( (JCheckBoxPad) evt.getCheckBox() ).getVlrString().equals( "S" ) ) {
				cbReEmiteNota.setVlrString( "N" );
			}
		}
		// else if ( evt.getCheckBox() == cbAdicICMSFrete ) {
		// txtPercIcmsFreteVD.setEditable( "S".equals( cbAdicICMSFrete.getVlrString() ) );
		// if ( "N".equals( cbAdicICMSFrete.getVlrString() ) ) {
		// txtPercIcmsFreteVD.setVlrString( "" );
		// txtVlrIcmsFreteVD.setVlrString( "" );
		// }
		// }
		if ( evt.getCheckBox() == cbEmiteRecibo || evt.getCheckBox() == cbEmiteBoleto ) {
			if ( cbEmiteRecibo.getVlrString().equals( "S" ) || cbEmiteBoleto.getVlrString().equals( "S" ) ) {
				txtCodModBol.setRequerido( true );
				txtCodModBol.setSoLeitura( false );
			}
			else {
				txtCodModBol.setSoLeitura( true );
				txtCodModBol.setRequerido( false );
				txtCodModBol.setVlrString( "" );
				txtDescModBol.setVlrString( "" );
			}
		}
	}

	public void valorAlterado( RadioGroupEvent evt ) {

		if ( rgFreteVD.getVlrString().equals( "C" ) ) {
			cbAdicFrete.setVlrString( "S" );
		}
		else if ( rgFreteVD.getVlrString().equals( "F" ) ) {
			cbAdicFrete.setVlrString( "N" );
		}
	}

	@ Override
	public void keyPressed( KeyEvent e ) {

		/*
		 * if ( e.getKeyCode() == KeyEvent.VK_ENTER && e.getSource() == txtPercIcmsFreteVD && txtVlrFreteVD.getVlrBigDecimal() != null && txtVlrFreteVD.getVlrBigDecimal().floatValue() > 0 ) { calculaIcmsFrete(); } else { super.keyPressed( e ); }
		 */
	}

	public void afterCarrega( CarregaEvent cevt ) {

		if ( txtPlacaFreteVD.getVlrString() == null || "".equals( txtPlacaFreteVD.getVlrString() ) ) {
			txtPlacaFreteVD.setVlrString( "*******" );
		}
	}

	public void beforeCarrega( CarregaEvent cevt ) {

	}
}
