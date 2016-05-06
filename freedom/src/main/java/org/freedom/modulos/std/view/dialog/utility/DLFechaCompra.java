/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)DLFechaCompra.java <BR>
 * 
 *                        Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                        modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                        na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                        Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                        sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                        Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                        Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                        de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                        Comentários sobre a classe...
 */

package org.freedom.modulos.std.view.dialog.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
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
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPassword;
import org.freedom.library.swing.util.SwingParams;
import org.freedom.modulos.fnc.library.swing.component.JTextFieldPlan;
import org.freedom.modulos.fnc.view.dialog.utility.DLNovoPag;
import org.freedom.modulos.gms.view.frame.crud.detail.FCompra;
import org.freedom.modulos.std.view.frame.crud.tabbed.FFornecedor;

public class DLFechaCompra extends FFDialogo implements FocusListener, MouseListener, KeyListener, CarregaListener {

	private static final long serialVersionUID = 1L;

	private JTabbedPanePad tpn = new JTabbedPanePad();

	private JPanelPad pinFecha = new JPanelPad( 420, 300 );

	private JPanelPad pnPagar = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTablePad tabPag = new JTablePad();

	private JTextFieldPad txtCodCompra = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtVlrDescItCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtPercDescCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, 2 );

	private JTextFieldPad txtVlrDescCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtVlrBaseICMSST = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtVlrICMSST = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtPercAdicCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, 2 );

	private JTextFieldPad txtVlrLiqCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtQtdFreteCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private JTextFieldPad txtVlrAdicCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtVlrProdCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtVlrFreteCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtVlrICMSCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtVlrIPICompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );
	
	private JTextFieldPad txtVlrTotCompra = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtVlrBaseICMS = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );
	
	private JTextFieldPad txtVlrISS = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );
	
	private JTextFieldPad txtVlrFunRural = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );
	
	private JTextFieldPad txtVlrOutrasDesp = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtCodPlanoPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtDocCompra = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 9, 0 );
	
	private JTextFieldPad txtDtEmitCompra = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDtEntCompra = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtCodTipoCob = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodBanco = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private JTextFieldPad txtNParcPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtVlrParcItPag = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtVlrParcPag = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtDtVencItPag = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtStatusCompra = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtDescPlanoPag = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtDescTipoCob = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtDescBanco = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JCheckBoxPad cbEmitePedido = new JCheckBoxPad( "Emite Pedido?", "S", "N" );

	private JCheckBoxPad cbFinalizar = new JCheckBoxPad( "Finalizar?", "S", "N" );

	private JCheckBoxPad cbEmiteNota = new JCheckBoxPad( "Emite Nota Fiscal?", "S", "N" );

	private JCheckBoxPad cbAdicFreteCusto = new JCheckBoxPad( "Soma Valor do frete ao custo dos produtos.", "S", "N" );

	private JCheckBoxPad cbAdicAdicCusto = new JCheckBoxPad( "Soma Valor adicional ao custo dos produtos.", "S", "N" );
	
//	private JCheckBoxPad cbOutrasDespCusto = new JCheckBoxPad( "Soma Outras desp. ao custo dos produtos.", "S", "N" );

	private JCheckBoxPad cbAdicIPIBase = new JCheckBoxPad( "Soma IPI à base de cálculo do ICMS.", "S", "N" );

	private JCheckBoxPad cbAdicFreteBase = new JCheckBoxPad( "Soma Frete à base de cálculo do ICMS.", "S", "N" );

	private JCheckBoxPad cbAdicAdicBase = new JCheckBoxPad( "Soma Vlr. Adicionais à base de cálculo do ICMS.", "S", "N" );
	
	private JTextFieldPad txtCodImp = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JRadioGroup<?, ?> rgFreteVD = null;

	private ListaCampos lcCompra = new ListaCampos( this );

	private ListaCampos lcPlanoPag = new ListaCampos( this, "PG" );

	private ListaCampos lcTipoCob = new ListaCampos( this, "TC" );
	
	private ListaCampos lcPagar = new ListaCampos( this );

	private ListaCampos lcBanco = new ListaCampos( this, "BO" );

	private ListaCampos lcItPagar = new ListaCampos( this );

	private Vector<String> vVals = new Vector<String>();

	private Vector<String> vLabs = new Vector<String>();

	public JButtonPad btFechar = new JButtonPad( "Fechar Compra", Icone.novo( "btOk.png" ) );

	private int iCodCompraFecha = 0;

	private boolean bPodeSair = false;

	BigDecimal volumes = null;

	private JPanelPad pinLbCusto = new JPanelPad();

	private JPanelPad pinCusto = new JPanelPad();

	private JPanelPad pinLbValores = new JPanelPad();

	private JPanelPad pinValores = new JPanelPad();

	private JPanelPad pinLbFrete = new JPanelPad();

	private JPanelPad pinFrete = new JPanelPad();

	private JPanelPad pinLbImp = new JPanelPad();

	private JPanelPad pinImp = new JPanelPad();

	private JPanelPad pinLBOutras = new JPanelPad();

	private JPanelPad pinOutras = new JPanelPad();
	
	private JPanelPad pinLbTrib = new JPanelPad();
	
	private JPanelPad pinTrib = new JPanelPad();

	private JPanelPad pinLbICMS = new JPanelPad();

	private JPanelPad pinICMS = new JPanelPad();
	
	private ListaCampos lcConta = new ListaCampos( this, "CT" );
	
	private JTextFieldPad txtNumConta = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldFK txtDescConta = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private final ListaCampos lcPlan = new ListaCampos( this, "PN" );
	
	private final ListaCampos lcCC = new ListaCampos( this, "CC" );
	
	private final JTextFieldPlan txtCodPlan = new JTextFieldPlan( JTextFieldPad.TP_STRING, 13, 0 );

	private final JTextFieldFK txtDescPlan = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private final JTextFieldPad txtCodCC = new JTextFieldPad( JTextFieldPad.TP_STRING, 19, 0 );

	private final JTextFieldPad txtAnoCC = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );

	private final JTextFieldFK txtSiglaCC = new JTextFieldFK( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldFK txtDescCC = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private Map<String, Integer> prefere = null;
	
	private Historico historico = null;
	
	private JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtDescFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private ListaCampos lcFor = new ListaCampos( this, "FR" );
	
	private final JTextFieldPad txtObspag = new JTextFieldPad( JTextFieldPad.TP_STRING, 250, 0 );
	
	private Component cOrig2 = null;

	public DLFechaCompra( DbConnection cn, Integer iCodCompra, Component cOrig, BigDecimal volumes, boolean NFe ) {

		super( cOrig );
		cOrig2 = cOrig;
		
		setConexao( cn );
		
		prefere = getPrefere();
		
		txtAnoCC.setVlrInteger( prefere.get( "anocc" ) );

		if ( NFe ) {
			cbEmiteNota.setText( "Emite NFE?" );
		}

		iCodCompraFecha = iCodCompra.intValue();
		setTitulo( "Fechar Compra" );
		setAtribos( 570, 600 );

		this.volumes = volumes;

		lcItPagar.setMaster( lcPagar );
		lcPagar.adicDetalhe( lcItPagar );
		lcItPagar.setTabela( tabPag );

		c.add( tpn );

		tpn.add( "Fechamento", pinFecha );
		tpn.add( "Pagar", pnPagar );

		vVals.addElement( "C" );
		vVals.addElement( "F" );
		vLabs.addElement( "CIF" );
		vLabs.addElement( "FOB" );
		rgFreteVD = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );

		txtCodPlanoPag.setNomeCampo( "CodPlanoPag" );
		lcPlanoPag.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.p.pg.", ListaCampos.DB_PK, false ) );
		lcPlanoPag.add( new GuardaCampo( txtDescPlanoPag, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		txtCodPlanoPag.setTabelaExterna( lcPlanoPag, null );
		txtCodPlanoPag.setFK( true );
		txtDescPlanoPag.setListaCampos( lcPlanoPag );
		lcPlanoPag.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag.setQueryCommit( false );
		lcPlanoPag.setReadOnly( true );
		lcPlanoPag.setConexao( cn );

		txtCodTipoCob.setNomeCampo( "CodTipoCob" );
		lcTipoCob.add( new GuardaCampo( txtCodTipoCob, "CodTipoCob", "Cód.t.cob.", ListaCampos.DB_PK, false ) );
		lcTipoCob.add( new GuardaCampo( txtDescTipoCob, "DescTipoCob", "Descrição do tipo de cobrança", ListaCampos.DB_SI, false ) );
		txtCodTipoCob.setTabelaExterna( lcTipoCob, null );
		txtCodTipoCob.setFK( true );
		txtDescTipoCob.setListaCampos( lcTipoCob );
		lcTipoCob.montaSql( false, "TIPOCOB", "FN" );
		lcTipoCob.setQueryCommit( false );
		lcTipoCob.setReadOnly( true );
		lcTipoCob.setConexao( cn );
		txtCodTipoCob.setTabelaExterna( lcTipoCob, null );

		txtCodBanco.setNomeCampo( "CodBanco" );
		lcBanco.add( new GuardaCampo( txtCodBanco, "CodBanco", "Cód.banco", ListaCampos.DB_PK, false ) );
		lcBanco.add( new GuardaCampo( txtDescBanco, "NomeBanco", "Nome do banco", ListaCampos.DB_SI, false ) );
		txtDescBanco.setListaCampos( lcBanco );
		lcBanco.montaSql( false, "BANCO", "FN" );
		lcBanco.setQueryCommit( false );
		lcBanco.setReadOnly( true );
		lcBanco.setConexao( cn );
		txtCodBanco.setTabelaExterna( lcBanco, null );
		txtCodBanco.setFK( true );
		
		lcConta.add( new GuardaCampo( txtNumConta, "NumConta", "Num.Conta", ListaCampos.DB_PK, txtDescConta, false ) );
		lcConta.add( new GuardaCampo( txtDescConta, "DescConta", "Descrição da conta", ListaCampos.DB_SI, false ) );
		lcConta.montaSql( false, "CONTA", "FN" );
		lcConta.setQueryCommit( false );
		lcConta.setReadOnly( true );
		txtNumConta.setFK( true );
		txtNumConta.setNomeCampo( "numconta" );
		txtNumConta.setTabelaExterna( lcConta, null );	
		lcConta.setConexao( cn );
		
		
		/************************
		 * FNPLANEJAMENTO       *
		 ************************/
		lcPlan.add( new GuardaCampo( txtCodPlan, "CodPlan", "Cód.plan.", ListaCampos.DB_PK, false ) );
		lcPlan.add( new GuardaCampo( txtDescPlan, "DescPlan", "Descrição do planejamento", ListaCampos.DB_SI, false ) );
		lcPlan.setWhereAdic( "TIPOPLAN = 'D' AND NIVELPLAN = 6" );
		lcPlan.montaSql( false, "PLANEJAMENTO", "FN" );
		lcPlan.setReadOnly( true );
		txtCodPlan.setTabelaExterna( lcPlan, null );
		txtCodPlan.setFK( true );
		txtCodPlan.setNomeCampo( "CodPlan" );
		lcPlan.setConexao( cn );
		
		lcFor.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.for.", ListaCampos.DB_PK, false ) );
		lcFor.add( new GuardaCampo( txtDescFor, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );

		lcFor.montaSql( false, "FORNECED", "CP" );
		lcFor.setQueryCommit( false );
		lcFor.setReadOnly( true );
		txtCodFor.setTabelaExterna( lcFor, FFornecedor.class.getCanonicalName() );
		
		lcFor.setConexao( cn );

		
		/***************
		 * FNCC        *
		 ***************/
		lcCC.add( new GuardaCampo( txtCodCC, "CodCC", "Cód.c.c.", ListaCampos.DB_PK, false ) );
		lcCC.add( new GuardaCampo( txtSiglaCC, "SiglaCC", "Sigla", ListaCampos.DB_SI, false ) );
		lcCC.add( new GuardaCampo( txtDescCC, "DescCC", "Descrição", ListaCampos.DB_SI, false ) );
		lcCC.add( new GuardaCampo( txtAnoCC, "AnoCC", "Ano-Base", ListaCampos.DB_PK, false ) );
		lcCC.setReadOnly( true );
		lcCC.setQueryCommit( false );
		lcCC.setWhereAdic( "NIVELCC=10 AND ANOCC=" + prefere.get( "anocc" ) );
		lcCC.montaSql( false, "CC", "FN" );
		txtCodCC.setTabelaExterna( lcCC, null );
		txtCodCC.setFK( true );
		txtCodCC.setNomeCampo( "CodCC" );
		txtAnoCC.setTabelaExterna( lcCC, null );
		txtAnoCC.setFK( true );
		txtAnoCC.setNomeCampo( "AnoCC" );
		lcCC.setConexao( cn );

		txtCodCompra.setNomeCampo( "CodCompra" );
		
		txtCodPlanoPag.setTabelaExterna( lcPlanoPag, null );
		
		lcCompra.add( new GuardaCampo( txtCodCompra			, "CodCompra"				, "N.pedido"					, ListaCampos.DB_PK, false ) );
		lcCompra.add( new GuardaCampo( txtCodPlanoPag		, "CodPlanoPag"				, "Cod.p.pg."					, ListaCampos.DB_FK, txtDescPlanoPag, false ) );
		lcCompra.add( new GuardaCampo( txtVlrLiqCompra		, "VlrLiqCompra"			, "V.compra"					, ListaCampos.DB_SI, false ) );
		lcCompra.add( new GuardaCampo( txtVlrICMSCompra		, "VlrICMSCompra"			, "V.ICMS"						, ListaCampos.DB_SI, false ) );
		lcCompra.add( new GuardaCampo( txtVlrICMSST			, "VlrICMSSTCompra"			, "V.ICMS ST"					, ListaCampos.DB_SI, false ) );
		lcCompra.add( new GuardaCampo( txtVlrIPICompra		, "VlrIPICompra"			, "V.IPI"						, ListaCampos.DB_SI, false ) );
		lcCompra.add( new GuardaCampo( txtVlrDescItCompra	, "VlrDescItCompra"			, "% Desc.it."					, ListaCampos.DB_SI, false ) );
		lcCompra.add( new GuardaCampo( txtVlrDescCompra		, "VlrDescCompra"			, "% Desc.it."					, ListaCampos.DB_SI, false ) );
		lcCompra.add( new GuardaCampo( txtVlrAdicCompra		, "VlrAdicCompra"			, "V.adic."						, ListaCampos.DB_SI, false ) );
		lcCompra.add( new GuardaCampo( txtVlrProdCompra		, "VlrProdCompra"			, "V.prod."						, ListaCampos.DB_SI, false ) );
		lcCompra.add( new GuardaCampo( txtVlrFreteCompra	, "VlrFreteCompra"			, "V.prod."						, ListaCampos.DB_SI, false ) );
		lcCompra.add( new GuardaCampo( txtStatusCompra		, "StatusCompra"			, "Status"						, ListaCampos.DB_SI, false ) );
		lcCompra.add( new GuardaCampo( txtCodTipoCob		, "CodTipoCob"				, "Cod.t.cob."					, ListaCampos.DB_FK, txtDescTipoCob, false ) );
		lcCompra.add( new GuardaCampo( txtCodBanco			, "CodBanco"				, "CodBanco"					, ListaCampos.DB_FK, txtDescBanco, false ) );
		lcCompra.add( new GuardaCampo( rgFreteVD			, "TipoFreteCompra"			, "Tipo do frete"				, ListaCampos.DB_SI, false ) );
		lcCompra.add( new GuardaCampo( cbAdicFreteCusto		, "AdicFreteCompra"			, "frete na campra"				, ListaCampos.DB_SI, false ) );
		lcCompra.add( new GuardaCampo( cbAdicAdicCusto		, "AdicAdicCompra"			, "Vlr Adicional na campra"		, ListaCampos.DB_SI, false ) );
		lcCompra.add( new GuardaCampo( txtQtdFreteCompra	, "QtdFreteCompra"			, "Qtd. de volumes na compra"	, ListaCampos.DB_SI, false ) );
		lcCompra.add( new GuardaCampo( txtVlrBaseICMS		, "VlrBaseICMSCompra"		, "Vlr. Base do ICMS"			, ListaCampos.DB_SI, false ) );
		lcCompra.add( new GuardaCampo( txtVlrBaseICMSST		, "VlrBaseICMSSTCompra"		, "Vlr. Base do ICMS ST"		, ListaCampos.DB_SI, false ) );
		lcCompra.add( new GuardaCampo( txtNumConta			, "NumConta"				, "Nro.Conta"					, ListaCampos.DB_FK, txtDescConta, false ) );
		lcCompra.add( new GuardaCampo( txtCodPlan			, "CodPlan"					, "Cód.Plan."					, ListaCampos.DB_FK, txtDescPlan, false ) ); 
		lcCompra.add( new GuardaCampo( txtAnoCC				, "AnoCC"					, "Ano.C.C."					, ListaCampos.DB_SI, txtDescCC, false ) );
		lcCompra.add( new GuardaCampo( txtCodCC				, "CodCC"					, "Cód.C.C."					, ListaCampos.DB_FK, txtDescCC, false ) );
		lcCompra.add( new GuardaCampo( txtCodImp			, "CodImp"					, "Cód.Imp"						, ListaCampos.DB_SI, null, false ) );

		lcCompra.add( new GuardaCampo( txtCodFor			, "CodFor"					, "Cód.for."					, ListaCampos.DB_FK, txtDescFor, false ));
		lcCompra.add( new GuardaCampo( txtDocCompra			, "DocCompra"				, "Documento"					, ListaCampos.DB_SI, false ));
		lcCompra.add( new GuardaCampo( txtDtEmitCompra		, "DtEmitCompra"			, "Dt.emissão"					, ListaCampos.DB_SI, false ));
		lcCompra.add( new GuardaCampo( txtDtEntCompra		, "DtEntCompra"				, "Dt.entrada"					, ListaCampos.DB_SI, false ));
		lcCompra.add( new GuardaCampo( txtObspag			, "ObsPag"					, "Obs.Pag"						, ListaCampos.DB_SI, false ));
		lcCompra.add( new GuardaCampo( txtVlrISS			, "VlrISSCompra"			, "Vlr. ISS"					, ListaCampos.DB_SI, false ) );
		lcCompra.add( new GuardaCampo( txtVlrFunRural		, "VlrFunRuralCompra"		, "Vlr. Funrural"				, ListaCampos.DB_SI, false ) );		
		lcCompra.add( new GuardaCampo( txtVlrOutrasDesp		, "VlrOutrasDesp"			, "Vlr.Outras Desp."			, ListaCampos.DB_SI, false ) );		
		
		lcCompra.montaSql( false, "COMPRA", "CP" );
		lcCompra.setConexao( cn );
		txtVlrLiqCompra.setListaCampos( lcCompra );
		txtVlrICMSCompra.setListaCampos( lcCompra );
		txtVlrIPICompra.setListaCampos( lcCompra );
		txtVlrAdicCompra.setListaCampos( lcCompra );
		txtPercAdicCompra.setListaCampos( lcCompra );
		txtVlrDescCompra.setListaCampos( lcCompra );
		txtVlrFreteCompra.setListaCampos( lcCompra );
		txtQtdFreteCompra.setListaCampos( lcCompra );
		txtPercDescCompra.setListaCampos( lcCompra );
		txtStatusCompra.setListaCampos( lcCompra );
		txtCodPlanoPag.setListaCampos( lcCompra );
		txtCodTipoCob.setListaCampos( lcCompra );

		JPanelPad pinTopPag = new JPanelPad( 400, 60 );
		pinTopPag.setPreferredSize( new Dimension( 400, 60 ) );
		pnPagar.add( pinTopPag, BorderLayout.NORTH );
		JScrollPane spnTabPag = new JScrollPane( tabPag );
		pnPagar.add( spnTabPag, BorderLayout.CENTER );

		txtVlrParcPag.setAtivo( false );

		pinTopPag.adic( new JLabelPad( "Valor Tot." ), 7, 0, 130, 20 );
		pinTopPag.adic( txtVlrParcPag, 7, 20, 130, 20 );

		txtCodPag.setNomeCampo( "CodPag" );
		
		lcPagar.add( new GuardaCampo( txtCodPag			, "CodPag"		, "Cód.pgto."	, ListaCampos.DB_PK, false ) );
		lcPagar.add( new GuardaCampo( txtVlrParcPag		, "VlrParcPag"	, "Valor tot."	, ListaCampos.DB_SI, false ) );

		lcPagar.montaSql( false, "PAGAR", "FN" );
		lcPagar.setConexao( cn );
		txtCodPag.setListaCampos( lcPagar );
		txtVlrParcPag.setListaCampos( lcPagar );
		txtCodBanco.setListaCampos( lcPagar );

		txtNParcPag.setNomeCampo( "NParcPag" );
		
		lcItPagar.add( new GuardaCampo( txtNParcPag		, "NParcPag"	, "N.parc."		, ListaCampos.DB_PK, false ) );
		lcItPagar.add( new GuardaCampo( txtVlrParcItPag	, "VlrParcItPag", "Valor tot."	, ListaCampos.DB_SI, false ) );
		lcItPagar.add( new GuardaCampo( txtDtVencItPag	, "DtVencItPag"	, "Valor tot."	, ListaCampos.DB_SI, false ) );
		
		lcItPagar.montaSql( false, "ITPAGAR", "FN" );
		lcItPagar.setConexao( cn );
		
		txtNParcPag.setListaCampos( lcItPagar );
		txtVlrParcItPag.setListaCampos( lcItPagar );
		txtDtVencItPag.setListaCampos( lcItPagar );

		lcItPagar.montaTab();
		tabPag.addMouseListener( this );

		txtCodCompra.setVlrInteger( iCodCompra );
		
		lcCompra.addCarregaListener( this );
		
		lcCompra.carregaDados();

		setPainel( pinFecha );

		adic( txtCodPlanoPag, 		7,  20,  60, 20, "Cód.pag." );
		adic( txtDescPlanoPag, 	   70,  20, 200, 20, "Descrição do plano de pagto." );

		adic( txtCodTipoCob, 	  273,  20,  60, 20, "Cód.t.cob." );
		adic( txtDescTipoCob, 	  336,  20, 200, 20, "Descrição do tipo de cobrança" );

		adic( txtCodBanco, 			7,  60,  60, 20, "Cód.banc." );
		adic( txtDescBanco, 	   70,  60, 200, 20, "Descrição do Banco" );

		adic( txtNumConta, 		  273,  60,  60, 20, "Conta" );
		adic( txtDescConta, 	  336,  60, 200, 20, "Descrição da conta" );
		
		adic( txtCodPlan, 			7, 100,  60, 20, "Cód.Cat." );
		adic( txtDescPlan, 		   70, 100, 200, 20, "Descrição da categoria" );

		adic( txtCodCC, 		  273, 100,  60, 20, "Cod.CC." );
		adic( txtDescCC, 		  336, 100, 200, 20, "Descrição do centro de custo" );

		/**********************
		 * ADICIONANDO QUADROS
		 **********************/
		
		adic( pinValores,	 	    7, 125, 326, 110 );
		adic( pinFrete, 		  336, 125, 200, 110 );
		adic( pinOutras, 			7, 238, 326, 70 );
		adic( pinTrib, 			    7, 311, 326, 70 );
		adic( pinICMS, 		      336, 255, 200, 110 );
		adic( pinCusto, 		    7, 390, 326, 90 );
		adic( pinImp, 			  336, 390, 200, 90 );
		
		
		/**********************
		 * Quadro de valores
		 **********************/

		pinValores.setBorder( SwingParams.getPanelLabel( "Valores", Color.BLACK ) );
		
		pinValores.adic( txtPercDescCompra,    7, 20, 60, 20, "% Desc." 	);
		pinValores.adic( txtVlrDescCompra, 	  70, 20, 80, 20, "Vlr.Desc." 	);
		pinValores.adic( txtVlrFreteCompra,	 153, 20, 80, 20, "Vlr.Frete" 	);
		pinValores.adic( txtVlrLiqCompra, 	 236, 20, 77, 20, "Vlr.Compra"	);
		pinValores.adic( txtPercAdicCompra,	   7, 60, 60, 20, "% Adic." 	);
		pinValores.adic( txtVlrAdicCompra,	  70, 60, 80, 20, "Vlr.Adic." 	);
		pinValores.adic( txtVlrIPICompra, 	 153, 60, 80, 20, "Vlr.IPI" 	);
		pinValores.adic( txtVlrTotCompra, 	 236, 60, 77, 20, "Vlr.Tot." 	);
		
		txtVlrTotCompra.setAtivo( false );
		txtVlrIPICompra.setAtivo( false );
		
		/**********************
		 * Quadro Frete
		 **********************/

		pinFrete.setBorder( SwingParams.getPanelLabel( "Frete", Color.BLACK ) );
		
		pinFrete.adic( rgFreteVD, 				7, 10, 178, 30 );
		pinFrete.adic( txtQtdFreteCompra, 		7, 60, 90, 20, "Volumes" );
		
		
		/**********************
		 * Quadro Outras Dispesas
		 **********************/

		pinOutras.setBorder( SwingParams.getPanelLabel( "Outras despesas", Color.BLACK ) );
		
		pinOutras.adic( txtVlrOutrasDesp			, 7		, 20	, 90	, 20, "Vlr.Outras Desp." );

		/**********************
		 * Quadro Tributação
		 **********************/

		pinTrib.setBorder( SwingParams.getPanelLabel( "Outros tributos", Color.BLACK ) );
		
		pinTrib.adic( txtVlrISS			, 7		, 20	, 90	, 20, "Vlr.ISS" );
		pinTrib.adic( txtVlrFunRural	, 100	, 20	, 90	, 20, "Vlr.FunRural" );
		
		/**********************
		 * ICMS
		 **********************/

		pinICMS.setBorder( SwingParams.getPanelLabel( "ICMS", Color.BLACK ) );
		
		pinICMS.adic( txtVlrBaseICMS	,   	7, 20, 90, 20, "Base calc." 	);
		pinICMS.adic( txtVlrBaseICMSST	, 	  100, 20, 80, 20, "Base calc. ST" 	);

		pinICMS.adic( txtVlrICMSCompra	,   	7, 60, 90, 20, "Vlr.ICMS" 		);
		pinICMS.adic( txtVlrICMSST		, 	  100, 60, 80, 20, "Vlr.ICMS ST" 	);
		
		txtVlrBaseICMS.setAtivo( false );
		txtVlrBaseICMSST.setAtivo( false );
		txtVlrICMSCompra.setAtivo( false );
		txtVlrICMSST.setAtivo( false );
		
		/******************************
		 * Quadro Composição do custo
		 ******************************/

		pinCusto.setBorder( SwingParams.getPanelLabel( "Composição do custo", Color.BLACK ) );
		
		pinCusto.adic( cbAdicFreteCusto	, 		7, 0, 280, 20 );
		pinCusto.adic( cbAdicAdicCusto	,	 	7, 20, 280, 20 );
		//pinCusto.adic( cbOutrasDespCusto,	 	7, 40, 280, 20 );

		/**********************
		 * Quadro Emissão
		 **********************/

		pinImp.setBorder( SwingParams.getPanelLabel( "Emissão", Color.BLACK ) );
		
		pinImp.adic( cbEmitePedido				, 7, 0, 180, 20 );
		pinImp.adic( cbEmiteNota				, 7, 20, 180, 20 );
		pinImp.adic( cbFinalizar				, 7, 40, 180, 20 );

		/********** FIM DOS QUADROS ***********/

		if ( txtVlrDescItCompra.getVlrBigDecimal().compareTo( new BigDecimal( 0 ) ) != 0 ) {
			txtPercDescCompra.setAtivo( false );
			txtVlrDescCompra.setAtivo( false );
		}

		tpn.setEnabledAt( 1, false );
		Funcoes.transValor( new BigDecimal( 0 ), 10, 2, false );
		btFechar.addActionListener( this );

		txtPercDescCompra.addFocusListener( this );
		txtVlrDescCompra.addFocusListener( this );
		txtVlrDescCompra.addKeyListener( this );
		txtPercAdicCompra.addFocusListener( this );
		txtVlrAdicCompra.addFocusListener( this );

		
		txtQtdFreteCompra.setVlrBigDecimal( volumes );

		lcCompra.edit();
		
	}

	private void adicVlrFrete() {

		if ( txtVlrFreteCompra.getVlrBigDecimal().intValue() > 0 ) {

			BigDecimal bdVlrFrete = txtVlrFreteCompra.getVlrBigDecimal();
			BigDecimal bdVlrCompra = txtVlrLiqCompra.getVlrBigDecimal();

			if ( Funcoes.mensagemConfirma( null, "Deseja adicionar o valor do frete no valor total?" ) == JOptionPane.YES_OPTION ) {

				txtVlrLiqCompra.setVlrBigDecimal( bdVlrCompra.add( bdVlrFrete ) );
				 
			}
		}
	}

	private void geraHistoricoPag() {

		// Gerando histórico dinâmico

		try {

			Integer codhistpag = prefere.get( "codhistpag" );

			if ( codhistpag!=null && codhistpag != 0 ) {
			
				historico = new Historico( codhistpag, con );
			
			}
			else {
			
				historico = new Historico();
				historico.setHistoricocodificado( DLNovoPag.HISTORICO_PADRAO );

			}

			
			historico.setData( txtDtEmitCompra.getVlrDate() );
			historico.setDocumento( txtDocCompra.getVlrString() );
			historico.setPortador( txtDescFor.getVlrString() );
			historico.setValor( txtVlrLiqCompra.getVlrBigDecimal() );
			historico.setHistoricoant( "" );
			
			txtObspag.setVlrString( historico.getHistoricodecodificado() );


		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	
	private void alteraParc() {

		lcItPagar.edit();
		DLFechaPag dl = new DLFechaPag( DLFechaCompra.this, txtVlrParcItPag.getVlrBigDecimal(), txtDtVencItPag.getVlrDate() );
		dl.setVisible( true );
		
		if ( dl.OK ) {
			txtVlrParcItPag.setVlrBigDecimal( (BigDecimal) dl.getValores()[ 0 ] );
			txtDtVencItPag.setVlrDate( (Date) dl.getValores()[ 1 ] );
			lcItPagar.post();
			// Atualiza lcPagar
			if ( lcPagar.getStatus() == ListaCampos.LCS_EDIT ) {
				lcPagar.post(); // Caso o lcPagar estaja como edit executa o post que atualiza
			}
			else {
				lcPagar.carregaDados(); // Caso não, atualiza
			}
		}
		else {
			dl.dispose();
			lcItPagar.cancel( false );
		}
		dl.dispose();
	}

	private int getCodPag() {

		int iRetorno = 0;
		String sSQL = "SELECT CODPAG FROM FNPAGAR WHERE CODCOMPRA=? AND CODEMPCP=? AND CODFILIALCP=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, iCodCompraFecha );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				iRetorno = rs.getInt( "CodPag" );
			}

			rs.close();
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar o código da conta a Pagar!\n" + err.getMessage(), true, con, err );
		}
		return iRetorno;
	}
	
	private Map<String, Integer> getPrefere() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer anocc = null;
		Integer codhistpag = null;

		Map<String, Integer> retorno = new HashMap<String, Integer>();

		try {

			ps = con.prepareStatement( "SELECT ANOCENTROCUSTO,CODHISTPAG FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				anocc = rs.getInt( "ANOCENTROCUSTO" );
				codhistpag = rs.getInt( "CODHISTPAG" );
			}

			retorno.put( "codhistpag", codhistpag );
			retorno.put( "anocc", anocc );

			rs.close();
			ps.close();

			con.commit();
		} 
		catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar o ano-base para o centro de custo.\n" + err.getMessage(), true, con, err );
		} 
		finally {
			ps = null;
			rs = null;
		}
		return retorno;
	}

	private boolean getVerificaUsu() {

		boolean bRetorno = false;
		String sSQL = "SELECT VERIFALTPARCVENDA FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			rs = ps.executeQuery();

			if ( rs.next() ) {
				if ( rs.getString( "VerifAltParcVenda" ).trim().equals( "S" ) ) {
					bRetorno = true;
				}
			}

			rs.close();
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage(), true, con, err );
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}
		return bRetorno;
	}
	
	
	public void rateiaOutrasDesp(){
		BigDecimal vlrLiqCompra = BigDecimal.ZERO;
		BigDecimal vlrTotDesp = BigDecimal.ZERO;
		BigDecimal diferenca = BigDecimal.ZERO;
		
		vlrLiqCompra = getValorLiqCompra( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "CPCOMPRA" ), txtCodCompra.getVlrInteger() );
		
		atualizaOutrasDesp( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "CPCOMPRA" ), txtCodCompra.getVlrInteger(), vlrLiqCompra );
		
		vlrTotDesp = getTotalDesp( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "CPCOMPRA" ), txtCodCompra.getVlrInteger()  );
		
		diferenca = txtVlrOutrasDesp.getVlrBigDecimal().subtract( vlrTotDesp );
		
		if( (diferenca.compareTo( BigDecimal.ZERO ) > 0) || (diferenca.compareTo( BigDecimal.ZERO )< 0) ) {
			atualizaDiferenca( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "CPCOMPRA" ), txtCodCompra.getVlrInteger(), diferenca );
		}
		
		
	}
	
	public void atualizaDiferenca(Integer codemp, Integer codfilial, Integer codcompra, BigDecimal diferenca){
		
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		sql.append( "update cpitcompra it set it.vlritoutrasdespitcompra = coalesce(it.vlritoutrasdespitcompra,0) + ? where it.codemp=? and it.CODFILIAL=? and it.codcompra=? and ");
		sql.append( " it.coditcompra=( select first 1 itm.coditcompra from cpitcompra itm where itm.codemp=it.codemp and itm.CODFILIAL=it.codfilial and itm.codcompra=it.codcompra order by itm.vlrproditcompra desc ) ");
		
		try{
			ps = con.prepareStatement( sql.toString() );
			int param = 1;
			ps.setBigDecimal( param++, diferenca );
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codcompra );
			ps.execute();
			
			con.commit();
		} catch (SQLException e) {
			Funcoes.mensagemErro( this, "Erro ao atualizar diferança no maior termo da compra!\n" + e.getMessage(), true, con, e );
		} finally {
			ps = null;
		}		
	}
	
	public BigDecimal getTotalDesp(Integer codemp, Integer codfilial, Integer codcompra) {
		
		BigDecimal vlrTotDesp = BigDecimal.ZERO;
		PreparedStatement ps = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append( "select  SUM(it.vlritoutrasdespitcompra) vlritoutrasdespitcompra from cpitcompra it where it.codemp=? and it.CODFILIAL=? and it.codcompra=? ");
		
		try{
			ps = con.prepareStatement( sql.toString() );
			int param = 1;
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codcompra );
			
			rs = ps.executeQuery();
			
			if(rs.next()){
				vlrTotDesp = rs.getBigDecimal( "vlritoutrasdespitcompra" );
			}
			rs.close();
			ps.close();
		
		} catch (SQLException e) {
			Funcoes.mensagemErro( this, "Erro ao buscar valor total das despesas da compra!\n" + e.getMessage(), true, con, e );
		} finally {
			rs = null;
			ps = null;
		}
	
		return vlrTotDesp;
		
	}
	
	public void atualizaOutrasDesp(Integer codemp, Integer codfilial, Integer codcompra, BigDecimal vlrliqcompra){
		
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		sql.append( " update cpitcompra it set it.vlritoutrasdespitcompra = ((it.VLRLIQITCOMPRA/?)*?) where it.codemp=? and it.CODFILIAL=? and it.codcompra=? ");
		
		try{
			ps = con.prepareStatement( sql.toString() );
			int param = 1;
			ps.setBigDecimal( param++, vlrliqcompra );
			ps.setBigDecimal( param++, txtVlrOutrasDesp.getVlrBigDecimal() );
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codcompra );
			ps.execute();
			
			con.commit();
		} catch (SQLException e) {
			Funcoes.mensagemErro( this, "Erro ao atualizar vlrOutrasDesp valor liquido da compra!\n" + e.getMessage(), true, con, e );
		} finally {
			ps = null;
		}		
	}
	
	public BigDecimal getValorLiqCompra(Integer codemp, Integer codfilial, Integer codcompra){
		BigDecimal vlrLiqCompra = BigDecimal.ZERO;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		//em implementação.
		StringBuilder sql = new StringBuilder();
		sql.append( " select cp.vlrliqcompra from cpcompra cp where cp.codemp=? and cp.CODFILIAL=? and cp.codcompra=? ");
		
		try{
			ps = con.prepareStatement( sql.toString() );
			int param = 1;
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codcompra );
			
			rs = ps.executeQuery();
			
			if(rs.next()){
				vlrLiqCompra = rs.getBigDecimal( "vlrliqcompra" );
			}
			rs.close();
			ps.close();
		
		} catch (SQLException e) {
			Funcoes.mensagemErro( this, "Erro ao buscar valor liquido da compra!\n" + e.getMessage(), true, con, e );
		} finally {
			rs = null;
			ps = null;
		}
	
		return vlrLiqCompra;
	}
	

	public String[] getValores() {

		String[] sRetorno = new String[ 8 ];

		sRetorno[ 0 ] = txtCodPlanoPag.getVlrString();
		sRetorno[ 1 ] = txtVlrDescCompra.getVlrString();
		sRetorno[ 2 ] = txtVlrAdicCompra.getVlrString();
		sRetorno[ 3 ] = cbEmitePedido.getVlrString();
		sRetorno[ 4 ] = cbEmiteNota.getVlrString();
		sRetorno[ 5 ] = txtVlrLiqCompra.getVlrString();
		sRetorno[ 6 ] = txtQtdFreteCompra.getVlrString();
		sRetorno[ 7 ] = cbFinalizar.getVlrString();

		return sRetorno;

	}

	private void fecharCompra() {

		lcCompra.edit();
		if ( txtStatusCompra.getVlrString().trim().equals( "P1" ) ) {
			txtStatusCompra.setVlrString( "P2" );
		}
		if ( txtStatusCompra.getVlrString().trim().equals( "C1" ) ) {
			txtStatusCompra.setVlrString( "C2" );
		}
		
		geraHistoricoPag();
		
		lcCompra.post();
		int iCodPag = getCodPag();
		if ( iCodPag > 0 ) {
			txtCodPag.setVlrInteger( new Integer( iCodPag ) );
			lcPagar.carregaDados();
		}
		bPodeSair = true;
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btCancel ) {
			super.actionPerformed( evt );
		}
		else if ( evt.getSource() == btOK ) {
			if ( bPodeSair ) {
				if ( lcPagar.getStatus() == ListaCampos.LCS_EDIT ) {
					lcPagar.post();
				}
				if ( cbEmitePedido.getVlrString().trim().equals( "S" ) || (cbFinalizar.equals( "S" )) || (cbEmiteNota.getVlrString().trim().equals( "N" ))  ) {
					lcCompra.edit();
					txtStatusCompra.setVlrString( "P3" );
					if ( !lcCompra.post() ) {
						cbEmitePedido.setVlrString( "N" );
					}
				}
				if ( cbEmiteNota.getVlrString().trim().equals( "S" ) ) {
					lcCompra.edit();
					txtStatusCompra.setVlrString( "C3" );
					if ( !lcCompra.post() ) {
						cbEmiteNota.setVlrString( "N" );
					}
				}
				if( txtVlrOutrasDesp.getVlrBigDecimal().compareTo( BigDecimal.ZERO) >0 ){
					rateiaOutrasDesp();
				}
				super.actionPerformed( evt );
			}
			else {
				if ( tpn.getSelectedIndex() == 0 ) {
					fecharCompra();
					tpn.setEnabledAt( 1, true );
					tpn.setSelectedIndex( 1 );
				}
			}
		}
		else if ( evt.getSource() == btFechar ) {
			fecharCompra();
			btOK.doClick();
		}
	}

	public void focusLost( FocusEvent fevt ) {

		if ( fevt.getSource() == txtPercDescCompra ) {
			if ( txtPercDescCompra.getText().trim().length() < 1 ) {
				txtVlrDescCompra.setAtivo( true );
			}
			else {
				txtVlrDescCompra.setVlrBigDecimal( txtVlrProdCompra.getVlrBigDecimal().multiply( txtPercDescCompra.getVlrBigDecimal() ).divide( new BigDecimal( "100" ), 2, BigDecimal.ROUND_HALF_UP ) );
				txtVlrDescCompra.setAtivo( false );
			}
		}
		if ( fevt.getSource() == txtPercAdicCompra ) {
			if ( txtPercAdicCompra.getText().trim().length() < 1 ) {
				txtVlrAdicCompra.setAtivo( true );
			}
			else {
				txtVlrAdicCompra.setVlrBigDecimal( txtVlrProdCompra.getVlrBigDecimal().multiply( txtPercAdicCompra.getVlrBigDecimal() ).divide( new BigDecimal( "100" ), 2, BigDecimal.ROUND_HALF_UP ) );
				txtVlrAdicCompra.setAtivo( false );
			}
		}
	}

	public void focusGained( FocusEvent fevt ) {

		
	}

	public void keyPressed( KeyEvent kevt ) {

		if ( kevt.getSource() == txtVlrFreteCompra ) {
			if ( kevt.getKeyCode() == KeyEvent.VK_ENTER ) {
				adicVlrFrete();
			}
		}
		
		if ( kevt.getSource() == txtVlrDescCompra ) {
			if ( kevt.getKeyCode() == KeyEvent.VK_ENTER ) {
				BigDecimal brut = txtVlrProdCompra.getVlrBigDecimal();
				BigDecimal des = txtVlrDescCompra.getVlrBigDecimal();
				BigDecimal liq = brut.subtract( des );
				txtVlrLiqCompra.setVlrBigDecimal( liq );
			}
		}

		super.keyPressed( kevt );
	}

	public void mouseClicked( MouseEvent mevt ) {

		if ( ( mevt.getClickCount() == 2 ) && ( tabPag.getLinhaSel() >= 0 ) ) {
			if ( getVerificaUsu() ) {
				FPassword fpw = new FPassword( this, FPassword.ALT_PARC_VENDA, null, con );
				fpw.execShow();
				if ( fpw.OK ) {
					alteraParc();
				}
				fpw.dispose();
			}
			else {
				alteraParc();
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

	public void afterCarrega( CarregaEvent cevt ) {

		if(cevt.getListaCampos() == lcCompra ) {
			if(txtCodImp.getVlrInteger() > 0 ) {
				txtVlrFreteCompra.setAtivo( false );
			}
			else {
				txtVlrFreteCompra.setAtivo( true );				
			}
		
			txtVlrTotCompra.setVlrBigDecimal( ((FCompra) cOrig2).getTotalNota()  );
			
		}
		
	}

	public void beforeCarrega( CarregaEvent cevt ) {

		// TODO Auto-generated method stub
		
	}
}
