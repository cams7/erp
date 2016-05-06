/**
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 *         Projeto: Freedom <BR>
 *         Pacote: org.freedom.modulos.gms <BR>
 *         Classe:
 * @(#)FRma.java <BR>
 *               Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *               modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *               na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *               Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *               sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *               Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *               Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *               de acordo com os termos da LPG-PC <BR>
 * <BR>
 *               Tela para cadastro de Requisições de material ao almoxarifado.
 */

package org.freedom.modulos.gms.view.frame.crud.detail;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.JComboBoxEvent;
import org.freedom.acao.JComboBoxListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.object.CustosProd;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.functions.FuncoesCRM;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDetalhe;
import org.freedom.library.swing.frame.FObservacao;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.cfg.view.frame.crud.tabbed.FUsuario;
import org.freedom.modulos.crm.view.frame.crud.detail.FContrato;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FTipoMov;
import org.freedom.modulos.pcp.view.frame.crud.detail.FOP;
import org.freedom.modulos.pcp.view.frame.crud.detail.FOPFase;
import org.freedom.modulos.std.DLBuscaEstoq;
import org.freedom.modulos.std.DLCodProd;
import org.freedom.modulos.std.view.dialog.report.DLRPedido;
import org.freedom.modulos.std.view.dialog.utility.DLBuscaProd;
import org.freedom.modulos.std.view.frame.crud.plain.FAlmox;
import org.freedom.modulos.std.view.frame.crud.plain.FUnidade;
import org.freedom.modulos.std.view.frame.crud.special.FCentroCusto;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCliente;

public class FRma extends FDetalhe implements PostListener, CarregaListener, FocusListener, ActionListener, InsertListener, JComboBoxListener {

	private static final long serialVersionUID = 1L;

	private int casasDec = Aplicativo.casasDec;

	private int casasDecFin = Aplicativo.casasDecFin;

	private int casasDecPre = Aplicativo.casasDecPre;
	
	private JPanelPad pinCab = new JPanelPad( 740, 242 );

	private JPanelPad pinBotCab = new JPanelPad( 104, 92 );

	private JPanelPad pinBotDet = new JPanelPad( 104, 63 );

	private JPanelPad pinDet = new JPanelPad();

	private JPanelPad pinLb = new JPanelPad();

	private JLabelPad lSitItRma = null;

	private JButtonPad btAprovaRMA = new JButtonPad( "Aprovar", Icone.novo( "btTudo.png" ) );

	private JButtonPad btFinAprovRMA = new JButtonPad( "Finaliz. aprov.", Icone.novo( "btFechaVenda.png" ) );

	private JButtonPad btExpedirRMA = new JButtonPad( "Expedir", Icone.novo( "btMedida.png" ) );
	
	private JButtonPad btExpedirItemRMA = new JButtonPad( "Expedir", Icone.novo( "btMedida.png" ) );

	private JButtonPad btFinExpRMA = new JButtonPad( "Finaliz. exp.", Icone.novo( "btFechaVenda.png" ) );

	private JButtonPad btCancelaRMA = new JButtonPad( "Cancelar", Icone.novo( "btRetorno.png" ) );

	private JButtonPad btCancelaItem = new JButtonPad( "Cancelar", Icone.novo( "btRetorno.png" ) );

	private JButtonPad btMotivoCancelaRMA = new JButtonPad( "Mot.Can", Icone.novo( "btObs1.png" ) );

	private JButtonPad btMotivoCancelaItem = new JButtonPad( "Mot.Can", Icone.novo( "btObs1.png" ) );

	private JButtonPad btMotivoPrior = new JButtonPad( "Mot.Prior", Icone.novo( "btObs1.png" ) );

	private JTextFieldPad txtCodRma = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodItRma = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtQtdItRma = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtCustoMPMProd = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtQtdAprovRma = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtQtdExpRma = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtPrecoItRma = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtCodOP = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtSeqOP = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtSeqOF = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtTicket = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodItRecMerc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtRefProdItRecMerc = new JTextFieldFK( JTextFieldPad.TP_STRING, 20, 0 );
	
	private JTextFieldFK txtDescProdItRecMerc = new JTextFieldFK( JTextFieldPad.TP_STRING, 100, 0 );

	private JTextFieldFK txtDescFase = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtCLoteProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtRefProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCodCC = new JTextFieldPad( JTextFieldPad.TP_STRING, 19, 0 );

	private JTextFieldFK txtDescCC = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtAnoCC = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodAlmox = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtCodTpMov = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldFK txtDescAlmox = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtIDUsu = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtNomeUsu = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodCCUsu = new JTextFieldPad( JTextFieldPad.TP_STRING, 19, 0 );

	private JTextFieldPad txtDtaReqRma = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldFK txtDescTipoMov = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextAreaPad txaMotivoRma = new JTextAreaPad();

	private JTextAreaPad txaMotivoCancRma = new JTextAreaPad();

	private JTextAreaPad txaMotivoCancItem = new JTextAreaPad();

	private JTextAreaPad txaMotivoPrior = new JTextAreaPad();

	private JTextFieldPad txtSitItRma = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtSitAprovItRma = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtSitExpItRma = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtSitRma = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtSitAprovRma = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtSitExpRma = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtCodLote = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldFK txtDescLote = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtSldLiqProd = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtCodFabProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtCodUnid = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldFK txtDescUnid = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JRadioGroup<?, ?> rgPriod = null;

	private Vector<String> vLabsTipo = new Vector<String>();

	private Vector<String> vValsTipo = new Vector<String>();

	private JScrollPane spnMotivo = new JScrollPane( txaMotivoRma );

	private ListaCampos lcAlmox = new ListaCampos( this, "AX" );

	private ListaCampos lcProd = new ListaCampos( this, "PD" );

	private ListaCampos lcProd2 = new ListaCampos( this, "PD" );

	private ListaCampos lcCC = new ListaCampos( this, "CC" );

	private ListaCampos lcOP = new ListaCampos( this, "OF" );

	private ListaCampos lcSeqOP = new ListaCampos( this, "OF" );

	private ListaCampos lcLote = new ListaCampos( this, "LE" );

	private ListaCampos lcUsu = new ListaCampos( this, "UU" );

	private ListaCampos lcTipoMov = new ListaCampos( this, "TM" );

	private ListaCampos lcUnid = new ListaCampos( this, "UD" );

	private ListaCampos lcContrato = new ListaCampos( this, "" );

	private ListaCampos lcItContrato = new ListaCampos( this, "CT" );

	private String sSitItRma = txtSitItRma.getVlrString();

	private String sOrdRMA = "";

	private Integer anoCC = null;

	private Integer iCodTpMov = null;

	private String codCC = null;

	private boolean bAprovaParcial = false;

	private String SitRma = "";

	private HashMap<String, Object> prefere = new HashMap<String, Object>();

	private boolean bAprovaCab = false;

	private boolean bExpede = false;

	private int cont = 0;

	private Vector<String> vItem = new Vector<String>();

	private Vector<String> vProdCan = new Vector<String>();

	private Vector<String> vMotivoCan = new Vector<String>();

	private String sSitItExp;

	private String siStItAprov;

	private String sSitRma;

	private JPanelPad pnEnquadramento = new JPanelPad();

	private Vector<Integer> vValsContr = new Vector<Integer>();

	private Vector<String> vLabsContr = new Vector<String>();

	private JComboBoxPad cbContr = new JComboBoxPad( vLabsContr, vValsContr, JComboBoxPad.TP_INTEGER, 8, 0 );

	private Vector<Integer> vValsitContr = new Vector<Integer>();

	private Vector<String> vLabsitContr = new Vector<String>();

	private JComboBoxPad cbitContr = new JComboBoxPad( vLabsitContr, vValsitContr, JComboBoxPad.TP_INTEGER, 8, 0 );

	private JTabbedPanePad tpnCab = new JTabbedPanePad();

	private JPanelPad pinCabRma = new JPanelPad();

	private JPanelPad pinCabEnquadramento = new JPanelPad();

	private ListaCampos lcCli = new ListaCampos( this, "CL" );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtCodContr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodItContr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	public FRma() {

		setTitulo( "RMA" );
		setAtribos( 15, 10, 772, 580 );

		nav.setNavigation( true );

		lcItContrato.setMaster( lcContrato );

		pnMaster.remove( 2 );
		pnGImp.removeAll();
		pnGImp.setLayout( new GridLayout( 1, 3 ) );
		pnGImp.setPreferredSize( new Dimension( 220, 26 ) );
		pnGImp.add( btPrevimp );
		pnGImp.add( btImp );

		pnMaster.add( spTab, BorderLayout.CENTER );

		pnCliCab.add( tpnCab );
		tpnCab.addTab( "Rma", pinCabRma );

		lcTipoMov.add( new GuardaCampo( txtCodTpMov, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false ) );
		lcTipoMov.add( new GuardaCampo( txtDescTipoMov, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false ) );
		lcTipoMov.setWhereAdic( "((ESTIPOMOV = 'S') AND TIPOMOV='RM' AND" + " ( TUSUTIPOMOV='S' OR	EXISTS (SELECT * FROM EQTIPOMOVUSU TU " + "WHERE TU.CODEMP=EQTIPOMOV.CODEMP AND TU.CODFILIAL=EQTIPOMOV.CODFILIAL AND " + "TU.CODTIPOMOV=EQTIPOMOV.CODTIPOMOV AND TU.CODEMPUS=" + Aplicativo.iCodEmp
				+ " AND " + "TU.CODFILIALUS=" + ListaCampos.getMasterFilial( "SGUSUARIO" ) + " AND TU.IDUSU='" + Aplicativo.getUsuario().getIdusu() + "') ) " + ")" );
		lcTipoMov.montaSql( false, "TIPOMOV", "EQ" );
		lcTipoMov.setQueryCommit( false );
		lcTipoMov.setReadOnly( true );
		txtCodTpMov.setTabelaExterna( lcTipoMov, FTipoMov.class.getCanonicalName() );

		String sWhereAdicProd = "ATIVOPROD='S' AND RMAPROD='S' AND ((SELECT ANOCCUSU||CODCCUSU FROM sgretinfousu("+Aplicativo.iCodEmp+",'" + Aplicativo.getUsuario().getIdusu() + "')) IN " 
				+ "(SELECT ANOCC||CODCC FROM EQPRODACESSO PA WHERE TIPOPA='RMA' AND PA.codemp=EQPRODUTO.CODEMP AND "
				+ "PA.CODFILIAL=EQPRODUTO.CODFILIAL AND PA.CODPROD=EQPRODUTO.CODPROD) " + "OR " 
				+ "((SELECT coalesce(COUNT(1),0) FROM EQPRODACESSO PA WHERE TIPOPA='RMA' AND PA.codemp=EQPRODUTO.CODEMP AND " 
				+ "PA.CODFILIAL=EQPRODUTO.CODFILIAL AND PA.CODPROD=EQPRODUTO.CODPROD)=0) " + "OR "
				+ "((SELECT ALMOXARIFE FROM sgretinfousu("+Aplicativo.iCodEmp+",'" + Aplicativo.getUsuario().getIdusu() + "'))='S') " + "OR " 
				+ "((SELECT APROVARMA FROM sgretinfousu("+Aplicativo.iCodEmp+",'" + Aplicativo.getUsuario().getIdusu() + "'))='TD') " + ") ";

		lcProd.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcProd.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtRefProd, "RefProd", "Referência", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtCodFabProd, "CodFabProd", "Código do fabricante", ListaCampos.DB_SI, true ) );
		lcProd.add( new GuardaCampo( txtCustoMPMProd, "CustoMPMProd", "Custo MPM", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtCLoteProd, "CLoteProd", "C/Lote", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtCodUnid, "CodUnid", "Cód.und.", ListaCampos.DB_SI, txtDescUnid, false ) );
		lcProd.setWhereAdic( sWhereAdicProd );
		lcProd.montaSql( false, "PRODUTO", "EQ" );
		lcProd.setReadOnly( true );
		txtCodProd.setTabelaExterna( lcProd, FProduto.class.getCanonicalName() );

		lcProd2.add( new GuardaCampo( txtRefProd, "RefProd", "Referência", ListaCampos.DB_PK, false ) );
		lcProd2.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.rod.", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtCodFabProd, "CodFabProd", "Código do fabricante", ListaCampos.DB_SI, true ) );
		lcProd2.add( new GuardaCampo( txtCustoMPMProd, "CustoMPMProd", "Custo MPM", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtCLoteProd, "CLoteProd", "C/Lote", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtCodUnid, "CodUnid", "Cód.und.", ListaCampos.DB_SI, txtDescUnid, false ) );
		txtRefProd.setNomeCampo( "RefProd" );
		txtRefProd.setListaCampos( lcDet );
		lcProd2.setWhereAdic( sWhereAdicProd );
		lcProd2.montaSql( false, "PRODUTO", "EQ" );
		lcProd2.setQueryCommit( false );
		lcProd2.setReadOnly( true );
		txtRefProd.setTabelaExterna( lcProd2, FProduto.class.getCanonicalName() );

		lcAlmox.add( new GuardaCampo( txtCodAlmox, "CodAlmox", "Cod.almox.", ListaCampos.DB_PK, txtDescAlmox, false ) );
		lcAlmox.add( new GuardaCampo( txtDescAlmox, "DescAlmox", "Descrição do almoxarifado;", ListaCampos.DB_SI, false ) );
		lcAlmox.montaSql( false, "ALMOX", "EQ" );
		lcAlmox.setQueryCommit( false );
		lcAlmox.setReadOnly( true );
		txtDescAlmox.setSoLeitura( true );
		txtCodAlmox.setTabelaExterna( lcAlmox, FAlmox.class.getCanonicalName() );

		lcCC.add( new GuardaCampo( txtCodCC, "CodCC", "Cód.c.c.", ListaCampos.DB_PK, false ) );
		lcCC.add( new GuardaCampo( txtAnoCC, "AnoCC", "Ano c.c.", ListaCampos.DB_PK, false ) );
		lcCC.add( new GuardaCampo( txtDescCC, "DescCC", "Descrição do centro de custo", ListaCampos.DB_SI, false ) );
		lcCC.montaSql( false, "CC", "FN" );
		lcCC.setQueryCommit( false );
		lcCC.setReadOnly( true );
		txtCodCC.setTabelaExterna( lcCC, FCentroCusto.class.getCanonicalName() );
		txtAnoCC.setTabelaExterna( lcCC, FCentroCusto.class.getCanonicalName() );

		lcLote.add( new GuardaCampo( txtCodLote, "CodLote", "Lote", ListaCampos.DB_PK, txtDescLote, false ) );
		lcLote.add( new GuardaCampo( txtDescLote, "VenctoLote", "Dt.vencto.", ListaCampos.DB_SI, false ) );
		lcLote.add( new GuardaCampo( txtSldLiqProd, "SldLiqLote", "Saldo", ListaCampos.DB_SI, false ) );
		lcLote.setDinWhereAdic( "CODPROD=#N AND VENCTOLOTE >= #D ", txtCodProd );
		lcLote.setDinWhereAdic( "", txtDtaReqRma );
		lcLote.montaSql( false, "LOTE", "EQ" );
		lcLote.setQueryCommit( false );
		lcLote.setReadOnly( true );
		txtCodLote.setTabelaExterna( lcLote, null );
		txtDescLote.setListaCampos( lcLote );
		txtDescLote.setNomeCampo( "VenctoLote" );
		txtDescLote.setLabel( "Vencimento" );

		lcUsu.add( new GuardaCampo( txtIDUsu, "idusu", "Id.Usu.", ListaCampos.DB_PK, false ) );
		lcUsu.add( new GuardaCampo( txtNomeUsu, "nomeusu", "Nome do usuário", ListaCampos.DB_SI, false ) );
		lcUsu.add( new GuardaCampo( txtCodCCUsu, "codcc", "C.Custo Usuário", ListaCampos.DB_SI, false ) );
		lcUsu.montaSql( false, "USUARIO", "SG" );
		lcUsu.setQueryCommit( false );
		lcUsu.setReadOnly( true );
		txtIDUsu.setTabelaExterna( lcUsu, FUsuario.class.getCanonicalName() );

		lcOP.add( new GuardaCampo( txtCodOP, "CodOP", "Cód. OP.", ListaCampos.DB_PK, false ) );
		lcOP.add( new GuardaCampo( txtSeqOF, "SeqOF", "Sequencia da fase", ListaCampos.DB_PK, false ) );
		lcOP.montaSql( false, "OPFASE", "PP" );
		lcOP.setQueryCommit( false );
		lcOP.setReadOnly( true );
		txtCodOP.setTabelaExterna( lcOP, FOPFase.class.getCanonicalName() );
		txtSeqOF.setTabelaExterna( lcOP, FOPFase.class.getCanonicalName() );

		lcSeqOP.add( new GuardaCampo( txtSeqOP, "SeqOP", "Seq. OP.", ListaCampos.DB_PK, null, false ) );
		lcSeqOP.setQueryCommit( false );
		lcSeqOP.setReadOnly( true );

		txtSeqOP.setTabelaExterna( lcSeqOP, FOP.class.getCanonicalName() );
		lcSeqOP.montaSql( false, "OP", "PP" );

		lcUnid.add( new GuardaCampo( txtCodUnid, "CodUnid", "Cód.unid.", ListaCampos.DB_PK, true ) );
		lcUnid.add( new GuardaCampo( txtDescUnid, "DescUnid", "Unidade", ListaCampos.DB_SI, false ) );
		lcUnid.montaSql( false, "UNIDADE", "EQ" );
		lcUnid.setReadOnly( true );
		lcUnid.setQueryCommit( false );
		txtCodUnid.setTabelaExterna( lcUnid, FUnidade.class.getCanonicalName() );

		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		txtCodCli.setTabelaExterna( lcCli, FCliente.class.getCanonicalName() );
		txtCodCli.setNomeCampo( "CodCli" );
		txtCodCli.setFK( true );
		lcCli.setReadOnly( true );
		lcCli.montaSql( false, "CLIENTE", "VD" );

		lcContrato.add( new GuardaCampo( txtCodContr, "CodContr", "Cód.Contr.", ListaCampos.DB_PK, false ) );
		lcContrato.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.Cli.", ListaCampos.DB_FK, false ) );
		lcContrato.setReadOnly( true );
		lcContrato.setQueryCommit( false );
		lcContrato.montaSql( false, "CONTRATO", "VD" );
		txtCodContr.setTabelaExterna( lcContrato, FContrato.class.getCanonicalName() );

		lcItContrato.add( new GuardaCampo( txtCodContr, "CodContr", "Cód.It.Contr.", ListaCampos.DB_PF, false ) );
		lcItContrato.add( new GuardaCampo( txtCodItContr, "CodItContr", "Cód.It.Contr.", ListaCampos.DB_PK, false ) );
		lcItContrato.setReadOnly( true );
		lcItContrato.setQueryCommit( false );
		lcItContrato.montaSql( false, "ITCONTRATO", "VD" );
		txtCodItContr.setTabelaExterna( lcItContrato, null );

		vValsTipo.addElement( "M" );
		vValsTipo.addElement( "A" );
		vLabsTipo.addElement( "Normal" );
		vLabsTipo.addElement( "Urgente" );
		rgPriod = new JRadioGroup<String, String>( 2, 1, vLabsTipo, vValsTipo );
		rgPriod.setVlrString( "B" );

		setListaCampos( lcCampos );

		setAltCab( 260 );

		// setPainel( pinCab, pnCliCab );

		setPainel( pinCabRma );

		adicCampo( txtCodRma, 7, 20, 110, 20, "CodRma", "Cód.Rma", ListaCampos.DB_PK, true );
		adicCampo( txtCodTpMov, 120, 20, 90, 20, "CodTipoMov", "Cód.Tp.Mov.", ListaCampos.DB_FK, txtDescTipoMov, true );
		adicDescFK( txtDescTipoMov, 213, 20, 200, 20, "DescTipoMov", "Descrição do tipo de movimento" );
		adicCampo( txtIDUsu, 416, 20, 120, 20, "IdUsu", "Id do usuário", ListaCampos.DB_FK, true );
		adicCampo( txtDtaReqRma, 539, 20, 86, 20, "DtaReqRma", "Data da Rma", ListaCampos.DB_SI, true );
		adicCampo( txtCodOP, 7, 60, 60, 20, "CodOP", "Cód.OP", ListaCampos.DB_FK, false );
		adicCampo( txtSeqOP, 70, 60, 47, 20, "SeqOP", "Sq.OP", ListaCampos.DB_FK, false );
		adicCampo( txtSeqOF, 120, 60, 37, 20, "SeqOF", "Sq.FS", ListaCampos.DB_FK, txtDescFase, false );
		adicDescFKInvisivel( txtDescCC, "DescCC", "Descrição do centro de custos" );
		adicCampo( txtAnoCC, 160, 60, 50, 20, "AnoCC", "Ano CC.", ListaCampos.DB_FK, true );
		adicCampo( txtCodCC, 213, 60, 200, 20, "CodCC", "Cód.CC.", ListaCampos.DB_FK, txtDescCC, true );
		adicDescFK( txtDescCC, 416, 60, 207, 20, "DescCC", "Descrição do centro de custos" );

		adicCampoInvisivel( txtSitRma, "sitrma", "Sit.Rma.", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtSitAprovRma, "sitaprovrma", "Sit.Ap.Rma.", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtSitExpRma, "sitexprma", "Sit.Exp.Rma.", ListaCampos.DB_SI, false );
		adicDBLiv( txaMotivoCancRma, "motivocancrma", "Motivo do cancelamento", false );
		adicDBLiv( txaMotivoRma, "MotivoRma", "Observações", false );
		
		adicCampo( txtTicket, 7, 100, 60, 20, "Ticket", "Ticket", ListaCampos.DB_SI, false );
		adicCampo( txtCodItRecMerc, 70, 100, 47, 20, "CodItRecMerc", "It.Rec.", ListaCampos.DB_SI, false );		
		
		pinCabRma.adic( new JLabelPad("Ref.Prod."), 120, 80, 90, 20 );
		pinCabRma.adic( txtRefProdItRecMerc, 120, 100, 90, 20 );
		
		pinCabRma.adic( new JLabelPad("Descrição do produto"), 213, 80, 200, 20 );
		pinCabRma.adic( txtDescProdItRecMerc, 213, 100, 200, 20 );
		
		pinCabRma.adic( new JLabelPad("Cliente"), 416, 80, 200, 20 );
		pinCabRma.adic( txtRazCli, 416, 100, 200, 20 );
		
		adic( new JLabelPad( "Observações" ), 7, 120, 100, 20 );
		adic( spnMotivo, 7, 140, 617, 50 );

		adicCampoInvisivel( txtCodContr, "CodContr", "Cod.Contr.", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtCodItContr, "CodItContr", "Cod.It.Contr.", ListaCampos.DB_FK, false );

		txtIDUsu.setNaoEditavel( true );
		txtDtaReqRma.setNaoEditavel( true );
		txtCodCC.setNaoEditavel( true );
		txtAnoCC.setNaoEditavel( true );
		txtCodTpMov.setNaoEditavel( true );

		setListaCampos( true, "RMA", "EQ" );
		lcCampos.setQueryInsert( false );

		txtQtdItRma.addFocusListener( this );
		lcCampos.addPostListener( this );
		lcCampos.addCarregaListener( this );
		lcProd.addCarregaListener( this );
		lcProd2.addCarregaListener( this );
		lcDet.addPostListener( this );
		lcDet.addCarregaListener( this );
		lcDet.addInsertListener( this );
		lcCampos.addInsertListener( this );
		lcUsu.addCarregaListener( this );
		lcCli.addCarregaListener( this );
		cbContr.addComboBoxListener( this );
		cbitContr.addComboBoxListener( this );

		btAprovaRMA.setToolTipText( "Aprovar todos os ítens." );
		btFinAprovRMA.setToolTipText( "Finaliza Aprovação." );
		btFinExpRMA.setToolTipText( "Finaliza Expedição." );
		btCancelaRMA.setToolTipText( "Cancelar todos os ítens." );
		btCancelaItem.setToolTipText( "Cancelar ítem." );
		btExpedirRMA.setToolTipText( "Expedir todos os ítens." );
		btExpedirItemRMA.setToolTipText( "Expedir ítem." );
		btMotivoCancelaRMA.setToolTipText( "Motivo do cancelamento da RMA." );
		btMotivoCancelaItem.setToolTipText( "Motivo do cancelamento do ítem." );
		btMotivoPrior.setToolTipText( "Motivo da prioridade do ítem." );

		pinCabRma.adic( pinBotCab, 630, 1, 114, 190 );
		pinBotCab.adic( btAprovaRMA, 0, 0, 110, 30 );
		pinBotCab.adic( btFinAprovRMA, 0, 31, 110, 30 );
		pinBotCab.adic( btCancelaRMA, 0, 62, 110, 30 );
		pinBotCab.adic( btMotivoCancelaRMA, 0, 93, 110, 30 );
		pinBotCab.adic( btExpedirRMA, 0, 124, 110, 30 );
		pinBotCab.adic( btFinExpRMA, 0, 155, 110, 30 );

		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		btAprovaRMA.addActionListener( this );
		btCancelaRMA.addActionListener( this );
		btCancelaItem.addActionListener( this );
		btExpedirRMA.addActionListener( this );
		btExpedirItemRMA.addActionListener( this );
		btMotivoCancelaRMA.addActionListener( this );
		btMotivoCancelaItem.addActionListener( this );
		btMotivoPrior.addActionListener( this );
		btFinAprovRMA.addActionListener( this );
		btFinExpRMA.addActionListener( this );

		setImprimir( true );

		desabAprov( true );
		desabExp( true );
	}

	private void montaDetalhe() {

		setAltDet( 130 );
		pinDet = new JPanelPad( 740, 122 );
		setPainel( pinDet, pnDet );
		setListaCampos( lcDet );
		setNavegador( navRod );

		adicCampo( txtCodItRma, 7, 20, 30, 20, "CodItRma", "Item", ListaCampos.DB_PK, true );

		if ( comRef() ) {

			adicCampo( txtRefProd, 40, 20, 87, 20, "RefProd", "Referência", ListaCampos.DB_FK, txtDescProd, true );
			adicCampoInvisivel( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_SI, false );

			txtRefProd.setBuscaAdic( new DLBuscaProd( con, "REFPROD", lcProd2.getWhereAdic() ) );

			if ( buscaGen() ) {
				txtRefProd.setBuscaGenProd( new DLCodProd( con, null, null ) );
			}

			txtQtdItRma.setBuscaAdic( new DLBuscaEstoq( lcDet, lcAlmox, lcProd2, con, "qtditrma" ) );
		}
		else {
			adicCampo( txtCodProd, 40, 20, 87, 20, "CodProd", "Cód.prod.", ListaCampos.DB_FK, txtDescProd, true );

			if ( buscaGen() ) {
				txtCodProd.setBuscaGenProd( new DLCodProd( con, null, null ) );
			}

			adicCampoInvisivel( txtRefProd, "RefProd", "Referência", ListaCampos.DB_SI, false );
			txtCodProd.setBuscaAdic( new DLBuscaProd( con, "CODPROD", lcProd.getWhereAdic() ) );
			txtQtdItRma.setBuscaAdic( new DLBuscaEstoq( lcDet, lcAlmox, lcProd, con, "qtditrma" ) );
		}

		adicDescFK( txtDescProd, 130, 20, 297, 20, "DescProd", "Descrição do produto" );
		adic( txtDescUnid, 513, 20, 100, 20 );
		adicDB( rgPriod, 513, 60, 100, 50, "PriorItRma", "Prioridade:", true );
		adicCampo( txtQtdItRma, 430, 20, 80, 20, "QtdItRma", "Qtd.solic.", ListaCampos.DB_SI, true );
		adicCampo( txtQtdAprovRma, 260, 60, 80, 20, "QtdAprovItRma", "Qtd.aprov.", ListaCampos.DB_SI, false );
		adicCampo( txtQtdExpRma, 343, 60, 80, 20, "QtdExpItRma", "Qtd.exp.", ListaCampos.DB_SI, false );
		adicCampo( txtCodLote, 426, 60, 80, 20, "CodLote", "Lote", ListaCampos.DB_FK, false );
		adicCampoInvisivel( txtPrecoItRma, "PrecoItRma", "Preço", ListaCampos.DB_SI, true );

		txtCodAlmox.setNaoEditavel( true );
		txtPrecoItRma.setNaoEditavel( true );

		adicCampoInvisivel( txtCodAlmox, "CodAlmox", "Cód.Almox.", ListaCampos.DB_FK, txtDescAlmox, false );
		adicDescFK( txtDescAlmox, 7, 60, 250, 20, "DescAlmox", "Descrição do almoxarifado" );
		adicCampoInvisivel( txtSitItRma, "sititrma", "Sit.It.Rma.", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtSitAprovItRma, "sitaprovitrma", "Sit.Ap.It.Rma.", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtSitExpItRma, "sitexpitrma", "Sit.Exp.It.Rma.", ListaCampos.DB_SI, false );
		adicDBLiv( txaMotivoCancItem, "motivocancitrma", "Motivo do cancelamento", false );
		adicDBLiv( txaMotivoPrior, "MotivoPriorItRma", "Motivo da Prioridade", false );

		/*
		 * txtRefProd.addKeyListener(new KeyAdapter() { public void keyPressed(KeyEvent kevt) { lcDet.edit(); } });
		 */

		setListaCampos( true, "ITRMA", "EQ" );
		lcDet.setQueryInsert( false );
		montaTab();

		tab.setTamColuna( 30, 0 );
		tab.setTamColuna( 70, 1 );
		tab.setTamColuna( 250, 2 );
		tab.setTamColuna( 70, 3 );
		tab.setTamColuna( 70, 4 );
		tab.setTamColuna( 70, 5 );
		tab.setTamColuna( 70, 6 );
		tab.setTamColuna( 70, 7 );
		tab.setTamColuna( 250, 8 );

		btMotivoPrior.setEnabled( false );

		pinBotDet.adic( btCancelaItem, 0, 0, 110, 28 );
		pinBotDet.adic( btMotivoCancelaItem, 0, 29, 110, 28 );
		pinBotDet.adic( btMotivoPrior, 0, 58, 110, 28 );
		pinBotDet.adic( btExpedirItemRMA, 0, 87, 110, 28 );
		
		pinDet.adic( pinBotDet, 630, 1, 115, 120 );
		
		
		lSitItRma = new JLabelPad();
		lSitItRma.setForeground( Color.WHITE );
		pinLb.adic( lSitItRma, 31, 0, 110, 20 );
		

		JPanelPad navEast = new JPanelPad();
		navEast.setPreferredSize( new Dimension( 118, 30 ) );
		navEast.adic( pinLb, 0, 0 , 118, 24 );

		navEast.tiraBorda();
		pnNavCab.add( navEast, BorderLayout.EAST );

	}

	private void buscaInfoUsuAtual() {

		String sSQL = "SELECT ANOCC,CODCC,CODEMPCC,CODFILIALCC,APROVRMAUSU,ALMOXARIFEUSU,RMAOUTCC " + "FROM SGUSUARIO WHERE CODEMP=? AND CODFILIAL=? " + "AND IDUSU=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGUSUARIO" ) );
			ps.setString( 3, Aplicativo.getUsuario().getIdusu() );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				String sAprova = rs.getString( "APROVRMAUSU" );
				String sExpede = rs.getString( "ALMOXARIFEUSU" );
				String sOutCC = rs.getString( "RMAOUTCC" );
				if ( sAprova != null ) {
					if ( !sAprova.equals( "ND" ) ) {
						if ( sAprova.equals( "TD" ) )
							bAprovaCab = true;
						else if ( ( txtCodCC.getVlrString().trim().equals( rs.getString( "CODCC" ).trim() ) ) && ( lcCC.getCodEmp() == rs.getInt( "CODEMPCC" ) ) && ( lcCC.getCodFilial() == rs.getInt( "CODFILIALCC" ) ) && ( sAprova.equals( "CC" ) ) ) {
							bAprovaCab = true;
						}
						else {
							bAprovaCab = false;
						}

					}
				}
				if ( sExpede != null ) {
					if ( sExpede.equals( "S" ) )
						bExpede = true;
					else
						bExpede = false;
				}
				if ( "S".equals( sOutCC ) ) {
					txtCodCC.setNaoEditavel( false );
				}

			}
			con.commit();

		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela SGUSUARIO!\n" + err.getMessage() );
		}
	}

	public void focusGained( FocusEvent fevt ) {

	}

	public void focusLost( FocusEvent fevt ) {

	}

	public void beforeCarrega( CarregaEvent cevt ) {

	}

	public void afterPost( PostEvent pevt ) {

		if ( pevt.getListaCampos() == lcCampos ) {
			lcCampos.carregaDados();
		}
		if ( pevt.getListaCampos() == lcDet ) {
			lcCampos.carregaDados();
		}
	}

	private void desabCampos( boolean bHab ) {

		txtCodProd.setNaoEditavel( bHab );
		txtRefProd.setNaoEditavel( bHab );
		txtQtdItRma.setNaoEditavel( bHab );
		txaMotivoRma.setEnabled( !bHab );
		rgPriod.setAtivo( !bHab );
		txtCodOP.setAtivo( !bHab );
		txtSeqOP.setAtivo( !bHab );
		txtSeqOF.setAtivo( !bHab );
		
	}

	private void desabAprov( boolean bHab ) {

		if ( txtSitAprovRma.getVlrString().equals( "AT" ) ) {
			btAprovaRMA.setEnabled( false );
			if ( !txtSitRma.getVlrString().equals( "AF" ) )
				btFinAprovRMA.setEnabled( true );
			else {
				btFinAprovRMA.setEnabled( false );
			}
		}
		else {
			btAprovaRMA.setEnabled( !bHab );
		}
		if ( txtSitRma.getVlrString().equals( "CA" ) ) {
			btMotivoCancelaRMA.setEnabled( true );
		}
		if ( txtSitItRma.getVlrString().equals( "CA" ) || txtSitExpItRma.getVlrString().equals( "CA" ) ) {
			btMotivoCancelaItem.setEnabled( true );
		}
		else {
			btMotivoCancelaRMA.setEnabled( !bHab );
			btMotivoCancelaItem.setEnabled( !bHab );
		}

		btFinAprovRMA.setEnabled( !bHab );
		btCancelaRMA.setEnabled( !bHab );
		btCancelaItem.setEnabled( !bHab || ( bExpede && txtSitRma.getVlrString().equals( "AF" ) ) );
		txtQtdAprovRma.setNaoEditavel( bHab );
	}

	private void desabExp( boolean bHab ) {

		if ( txtSitExpRma.getVlrString().equals( "ET" ) ) {
			btExpedirRMA.setEnabled( false );
			btExpedirItemRMA.setEnabled( false );
			btFinExpRMA.setEnabled( !bHab );
		}
		else if ( bHab ) {
			btFinExpRMA.setEnabled( !bHab );
		}

		btExpedirRMA.setEnabled( !bHab );
		btExpedirItemRMA.setEnabled( !bHab );
		txtQtdExpRma.setNaoEditavel( bHab );

	}

	public void carregaWhereAdic() {

		buscaInfoUsuAtual();
		if ( ( bAprovaCab ) || ( bExpede ) ) {
			if ( bAprovaParcial ) {
				lcCampos.setWhereAdic( "CODCC='" + Aplicativo.getUsuario().getCodcc() + "' AND ANOCC=" + Aplicativo.getUsuario().getAnocc() );
			}
		}
		else {
			lcCampos.setWhereAdic( "IDUSU='" + Aplicativo.getUsuario().getIdusu() + "'" );
		}
	}

	public void afterCarrega( CarregaEvent cevt ) {

		buscaInfoUsuAtual();

		sSitRma = txtSitRma.getVlrString();
		siStItAprov = txtSitAprovItRma.getVlrString();
		sSitItExp = txtSitExpItRma.getVlrString();
		sSitItRma = txtSitItRma.getVlrString();
		
		boolean bStatusTravaTudo = ( ( sSitItRma.equals( "AF" ) ) || ( sSitItRma.equals( "EF" ) ) || ( sSitItRma.equals( "CA" ) ) );
		boolean bStatusTravaExp = ( !sSitItRma.equals( "AF" ) || sSitItExp.equals( "CA" ) );

		if ( rgPriod.getVlrString().equals( "A" ) && sSitRma.equals( "PE" ) ) {
			btMotivoPrior.setEnabled( true );
		}
		else
			btMotivoPrior.setEnabled( false );

		if ( sSitRma.equals( "CA" ) )
			btMotivoCancelaRMA.setEnabled( true );
		else
			btMotivoCancelaRMA.setEnabled( false );

		if ( ! ( txtIDUsu.getVlrString().equals( Aplicativo.getUsuario().getIdusu() ) ) || ( bStatusTravaTudo ) )
			desabCampos( true );
		else
			desabCampos( false );

		if ( !bAprovaCab || bStatusTravaTudo ) {
			desabAprov( true );
			txaMotivoCancRma.setEnabled( false );
		}
		else {
			if ( !bStatusTravaTudo )
				txaMotivoCancRma.setEnabled( true );
			desabAprov( false );
		}

		if ( !bExpede || bStatusTravaExp )
			desabExp( true );
		else {
			desabExp( false );
			if ( txtCLoteProd.getVlrString().equals( "N" ) )
				txtCodLote.setAtivo( false );
			else
				txtCodLote.setAtivo( true );
		}

		if ( sSitItRma.equals( "CA" ) || sSitItExp.equals( "CA" ) ) {
			SitRma = "Cancelada";
			lSitItRma.setText( SitRma );
			pinLb.setBackground( cor( 250, 50, 50 ) );
		}
		else if ( sSitItRma.equals( "PE" ) ) {
			SitRma = "Pendente";
			lSitItRma.setText( SitRma );
			pinLb.setBackground( cor( 255, 204, 51 ) );
		}
		else if ( sSitItExp.equals( "ET" ) || sSitItExp.equals( "EP" ) ) {
			SitRma = "Expedida";
			lSitItRma.setText( SitRma );
			pinLb.setBackground( cor( 0, 170, 30 ) );
		}
		else if ( siStItAprov.equals( "AT" ) || siStItAprov.equals( "AP" ) ) {
			SitRma = "Aprovada";
			lSitItRma.setText( SitRma );
			pinLb.setBackground( cor( 26, 140, 255 ) );
		}

		if ( ( ( cevt.getListaCampos() == lcProd ) || ( cevt.getListaCampos() == lcProd2 ) ) && ( ( lcDet.getStatus() == ListaCampos.LCS_EDIT ) || ( ( lcDet.getStatus() == ListaCampos.LCS_INSERT ) ) ) ) {
			CustosProd custo = new CustosProd( txtCodAlmox.getVlrInteger(), txtCodProd.getVlrInteger(), con );
			txtPrecoItRma.setVlrBigDecimal( custo.getCustoMPMProd() );
		}

		if ( ( cevt.getListaCampos() == lcProd ) || ( cevt.getListaCampos() == lcProd2 ) ) {
			txtCodUnid.atualizaFK();
			if ( txtCLoteProd.getVlrString().equals( "N" ) )
				txtCodLote.setAtivo( false );
			else
				txtCodLote.setAtivo( true );
		}

		if ( cevt.getListaCampos() == lcDet ) {
			if ( sSitItRma.equals( "CA" ) ) {
				desabCampos( true );
				btMotivoCancelaItem.setEnabled( true );
			}
			if ( txtQtdAprovRma.isEditable() ) {
				if ( txtQtdAprovRma.floatValue() == 0 )
					txtQtdAprovRma.setVlrBigDecimal( new BigDecimal( txtQtdItRma.floatValue() ) );
			}
			if ( txtQtdExpRma.isEditable() ) {
				if ( txtQtdExpRma.floatValue() == 0 )
					txtQtdExpRma.setVlrBigDecimal( new BigDecimal( txtQtdAprovRma.floatValue() ) );
			}
		}

		if ( cevt.getListaCampos() == lcCli ) {
			HashMap<String, Vector<Object>> vals = FuncoesCRM.montaComboContr( con, txtCodCli.getVlrInteger(), "<Não selecionado>", true );
			cbContr.setItensGeneric( (Vector<?>) vals.get( "LAB" ), (Vector<?>) vals.get( "VAL" ) );
		}

		if ( cevt.getListaCampos() == lcCampos ) {
			cbContr.setVlrInteger( txtCodContr.getVlrInteger() );
			cbitContr.setVlrInteger( txtCodItContr.getVlrInteger() );
			
			if(txtTicket.getVlrInteger()>0) {
				carregaInfoOS();
			}
			
		}

	}

	private HashMap<String, Object> getPrefere() {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		String sSQL = "SELECT USAREFPROD, LANCARMACONTR, BUSCACODPRODGEN FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			rs = ps.executeQuery();

			if ( rs.next() ) {
				ret.put( "COMREF", rs.getString( "UsaRefProd" ) );
				ret.put( "LANCARMACONTR", rs.getString( "LANCARMACONTR" ) );
				ret.put( "BUSCACODPRODGEN", "S".equals( rs.getString( "BUSCACODPRODGEN" ) ) );

			}

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage(), true, con, err );
		} finally {
			rs = null;
			ps = null;
			sSQL = null;
		}
		return ret;
	}

	private boolean dialogObsCab() {

		boolean bRet = false;
		FObservacao obs = new FObservacao( "Cancelando RMA", txaMotivoCancRma.getVlrString() );
		if ( obs != null ) {
			if ( ( !bAprovaCab ) || ( sSitItRma.equals( "CA" ) ) )
				obs.txa.setEnabled( false );
			obs.setVisible( true );
			if ( obs.OK ) {
				txaMotivoCancRma.setVlrString( obs.getTexto() );
				bRet = true;
			}
		}
		obs.dispose();
		return bRet;
	}

	private boolean dialogObsDet() {

		boolean bRet = false;
		FObservacao obs = new FObservacao( "Cancelando item", txaMotivoCancItem.getVlrString() );
		if ( obs != null ) {
			if ( ( !bAprovaCab ) || ( sSitItRma.equals( "CA" ) || sSitItExp.equals( "CA" ) ) )
				obs.txa.setEnabled( false );
			obs.setVisible( true );
			if ( obs.OK ) {
				txaMotivoCancItem.setVlrString( obs.getTexto() );
				bRet = true;
			}
		}
		obs.dispose();
		return bRet;
	}

	private boolean dialogObsPrior() {

		boolean bRet = false;
		FObservacao obs = new FObservacao( "Motivo da prioridade", txaMotivoPrior.getVlrString() );
		if ( obs != null ) {
			if ( ( rgPriod.getVlrString().equals( "A" ) ) && ( txtIDUsu.getVlrString().equals( Aplicativo.getUsuario().getIdusu() ) ) ) {
				obs.txa.setEnabled( true );
			}
			else
				obs.txa.setEnabled( false );
			obs.setVisible( true );
			if ( obs.OK ) {
				txaMotivoPrior.setVlrString( obs.getTexto() );
				bRet = true;
			}
		}
		obs.dispose();
		return bRet;
	}

	private void carregaInfoOS() {
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			sql.append( "select rm.ticket, ir.coditrecmerc, ir.refprod, cl.razcli, pd.descprod " );
			sql.append( "from eqrecmerc rm, eqitrecmerc ir, vdcliente cl, eqproduto pd " );
			sql.append( "where " );
			sql.append( "rm.codemp=ir.codemp and rm.codfilial=ir.codfilial and rm.ticket=ir.ticket and ");
			sql.append( "cl.codemp=rm.codempcl and cl.codfilial=rm.codfilialcl and cl.codcli=rm.codcli and ");
			sql.append( "pd.codemp=ir.codemppd and pd.codfilial=ir.codfilialpd and pd.codprod=ir.codprod and " );
			sql.append( "ir.codemp=? and ir.codfilial=? and ir.ticket=? and ir.coditrecmerc=? " );
			
			ps = con.prepareStatement( sql.toString() );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			ps.setInt( 3, txtTicket.getVlrInteger() );
			ps.setInt( 4, txtCodItRecMerc.getVlrInteger());
			
			rs = ps.executeQuery();
			
			if(rs.next()) {

				txtRefProdItRecMerc.setVlrString( rs.getString( "refprod" ) );
				txtRazCli.setVlrString( rs.getString( "razcli" ) );
				txtDescProdItRecMerc.setVlrString( rs.getString( "descprod" ) );
				
				txtTicket.setEditable( false );
				txtCodItRecMerc.setEditable( false );
				
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp )
			imprimir( TYPE_PRINT.VIEW,txtCodRma.getVlrInteger().intValue() );
		else if ( evt.getSource() == btImp )
			imprimir( TYPE_PRINT.PRINT, txtCodRma.getVlrInteger().intValue() );
		else if ( evt.getSource() == btMotivoCancelaRMA ) {
			dialogObsCab();
		}
		else if ( evt.getSource() == btMotivoCancelaItem ) {
			dialogObsDet();
		}
		else if ( evt.getSource() == btMotivoPrior ) {
			dialogObsPrior();
		}
		else if ( evt.getSource() == btCancelaRMA ) {
			lcCampos.setState( ListaCampos.LCS_EDIT );
			if ( Funcoes.mensagemConfirma( null, "Deseja cancelar a RMA e todos os ítens?" ) == JOptionPane.YES_OPTION ) {
				if ( dialogObsCab() ) {
					txtSitRma.setVlrString( "CA" );
					lcCampos.post();
				}
			}
		}
		else if ( evt.getSource() == btCancelaItem ) {
			lcDet.setState( ListaCampos.LCS_EDIT );
			if ( Funcoes.mensagemConfirma( null, "Deseja cancelar ítem da RMA?" ) == JOptionPane.YES_OPTION ) {
				if ( dialogObsDet() ) {
					if ( ! ( txtSitRma.getVlrString().equals( "AF" ) || txtSitRma.getVlrString().equals( "AT" ) ) ) {
						txtSitItRma.setVlrString( "CA" );
					}
					else {
						txtSitExpItRma.setVlrString( "CA" );
						txtQtdExpRma.setVlrBigDecimal( new BigDecimal( 0 ) );
					}
					lcDet.post();
				}
			}
		}
		else if ( evt.getSource() == btAprovaRMA ) {
			lcCampos.setState( ListaCampos.LCS_EDIT );
			if ( Funcoes.mensagemConfirma( null, "Deseja Aprovar todos os ítens da RMA?\n " + "Caso você não tenha informado as quantidades\n a serem aprovadas" + " estará aprovando as quantidades requeridas!" ) == JOptionPane.OK_OPTION ) {
				txtSitAprovRma.setVlrString( "AT" );
				nav.btSalvar.doClick();
			}
		}
		else if ( evt.getSource() == btFinAprovRMA ) {
			lcCampos.setState( ListaCampos.LCS_EDIT );
			if ( Funcoes.mensagemConfirma( null, "Deseja finalizar o processo de aprovação da RMA?\n Após este procedimento a RMA não poderá mais ser alterada\n" + "e estará disponível para expedição!" ) == JOptionPane.OK_OPTION ) {
				;
				txtSitRma.setVlrString( "AF" );
				nav.btSalvar.doClick();
			}
		}
		else if ( evt.getSource() == btExpedirRMA ) {
			lcCampos.setState( ListaCampos.LCS_EDIT );
			if ( Funcoes.mensagemConfirma( null, "Deseja Expedir todos os ítens da RMA?\n Caso você não tenha informado as quantidades\n a serem expedidas" + " estará expedindo as quantidades aprovadas!" ) == JOptionPane.OK_OPTION ) {
				txtSitExpRma.setVlrString( "ET" );
				nav.btSalvar.doClick();
			}
		}
		else if ( evt.getSource() == btFinExpRMA ) {
			lcCampos.setState( ListaCampos.LCS_EDIT );
			if ( Funcoes.mensagemConfirma( null, "Deseja finalizar o processo de expedição da RMA?\n Após este procedimento os itens da RMA terão seus \n" + "saldos em estoque alterados!" ) == JOptionPane.OK_OPTION ) {
				;
				txtSitRma.setVlrString( "EF" );
				nav.btSalvar.doClick();
			}
		}
		else if ( evt.getSource() == btExpedirItemRMA ) {
			lcDet.setState( ListaCampos.LCS_EDIT );
			if ( Funcoes.mensagemConfirma( null, "Confirma a expedição do ítem selecionado?!" ) == JOptionPane.OK_OPTION ) {
				txtSitExpItRma.setVlrString( "ET" );
				navRod.btSalvar.doClick();
			}
		}


		super.actionPerformed( evt );
	}

	private void imprimir( TYPE_PRINT bVisualizar, int iCodSol ) {

		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		PreparedStatement ps = null;
		ResultSet rs = null;
		DLRPedido dl = new DLRPedido( sOrdRMA, "I.CODITRMA", true );
		
		dl.setTipo( "G" );
		
		dl.setConexao( con );
		dl.setVisible( true );

		if ( dl.OK == false ) {
			dl.dispose();
			return;
		}

		String ordem = dl.getValor();
		if (! "I.CODITRMA".equalsIgnoreCase( ordem )) {
			ordem = "P."+ordem;
		}
		
		StringBuilder sql = new StringBuilder();

		try {

			sql.append( " SELECT" );

			sql.append( " (SELECT COUNT(IT.CODITRMA) FROM EQITRMA IT WHERE IT.CODEMP=R.CODEMP AND IT.CODFILIAL = R.CODFILIAL AND IT.CODRMA=R.CODRMA)," );

			sql.append( " R.CODRMA,R.DTINS,R.SITRMA,R.MOTIVORMA,R.IDUSU,R.IDUSUAPROV,R.IDUSUEXP,R.DTAAPROVRMA,R.DTAEXPRMA,R.MOTIVOCANCRMA," );

			sql.append( " I.CODPROD, I.QTDITRMA, I.QTDAPROVITRMA, I.QTDEXPITRMA, I.SITITRMA,I.SITITRMA,I.SITAPROVITRMA,I.SITEXPITRMA,I.CODITRMA," );

			sql.append( " P.REFPROD,P.DESCPROD, P.CODUNID, UND.DESCUNID,A.CODALMOX, A.DESCALMOX, CC.CODCC, CC.ANOCC, CC.DESCCC," );

			sql.append( " (SELECT U.CODCC FROM SGUSUARIO U WHERE U.IDUSU=R.IDUSUAPROV AND U.CODEMP=R.CODEMPUA AND U.CODFILIAL=R.CODFILIALUA)," );

			sql.append( " (SELECT C.DESCCC FROM FNCC C, SGUSUARIO U WHERE C.CODEMP=U.CODEMPCC AND C.CODFILIAL=U.CODEMPCC AND C.ANOCC=U.ANOCC  AND C.CODCC=U.CODCC AND U.IDUSU=R.IDUSUAPROV)," );

			sql.append( " (SELECT C.DESCCC FROM FNCC C, SGUSUARIO U WHERE C.CODEMP=U.CODEMPCC AND C.CODFILIAL=U.CODEMPCC AND C.ANOCC=U.ANOCC  AND C.CODCC=U.CODCC AND U.IDUSU=R.IDUSUEXP)," );

			sql.append( " (SELECT U.CODCC FROM SGUSUARIO U WHERE U.IDUSU=R.IDUSUEXP AND U.CODEMP=R.CODEMPUE AND U.CODFILIAL=R.CODFILIALUE)," );

			sql.append( " I.MOTIVOCANCITRMA, I.CODPROD , R.CODOP, R.SEQOP" );
			
			sql.append( " FROM EQRMA R, EQITRMA I, EQALMOX A, FNCC CC, EQPRODUTO P, EQUNIDADE UND " );
			
//			sql.append( " LEFT OUTER JOIN EQRECMERC RM ON RM.CODEMP=R.CODEMPOS AND RM.CODFILIAL=R.CODFILIALOS AND RM.TICKET=R.TICKET " );
			
//			sql.append( " LEFT OUTER JOIN VDCLIENTE CL ON CL.CODEMP=RM.CODEMPCL AND CL.CODFILIAL=RM.CODFILIALCL AND CL.CODCLI=RM.CODCLI " );
			
			sql.append( " WHERE" );

			sql.append( " R.CODEMP=? AND R.CODFILIAL=? AND R.CODRMA=?" );
			sql.append( " AND P.CODEMP=I.CODEMPPD AND P.CODFILIAL=I.CODFILIALPD AND P.CODPROD=I.CODPROD" );
			sql.append( " AND I.CODEMP=R.CODEMP AND I.CODFILIAL=R.CODFILIAL AND I.CODRMA=R.CODRMA" );
			sql.append( " AND CC.CODEMP=R.CODEMPCC AND CC.CODFILIAL=R.CODFILIALCC AND CC.CODCC=R.CODCC AND CC.anocc=R.anocc" );
			sql.append( " AND A.CODEMP=I.CODEMPAX AND A.CODFILIAL=I.CODFILIALAX AND A.CODALMOX=I.CODALMOX" );
			sql.append( " AND UND.CODEMP = P.codempud AND UND.CODFILIAL=P.codfilialud AND UND.codunid=P.codunid" );

			sql.append( " ORDER BY R.CODRMA " );

			sql.append( ", " + ordem );

			System.out.println( "SQL:" + sql.toString() );

			ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, lcCampos.getCodEmp() );
			ps.setInt( 2, lcCampos.getCodFilial() );
			ps.setInt( 3, txtCodRma.getVlrInteger().intValue() );

			rs = ps.executeQuery();

			if ( "G".equals( dl.getTipo() ) ) {
				imprimirGrafico( bVisualizar, rs, "", dl.ehResumido() );

			}
			else {
				imprimirTexto( bVisualizar, rs, "", dl.ehResumido() );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( Exception e ) {

			e.printStackTrace();
		}

	}

	private void imprimirTexto( final TYPE_PRINT bVisualizar, final ResultSet rs, final String sCab, boolean isResum ) {

		ImprimeOS imp = null;
		int linPag = 0;
		
		vProdCan = new Vector<String>();
		vMotivoCan = new Vector<String>();

		try {

			imp = new ImprimeOS( "", con );
			linPag = imp.verifLinPag() - 1;
			imp.montaCab();
			imp.setTitulo( "Impressão de RMA" );
			imp.limpaPags();

			while ( rs.next() ) {

				if ( imp.pRow() >= ( linPag - 1 ) ) {
					imp.eject();
					imp.incPags();
				}
				if ( imp.pRow() == 0 ) {
					imp.impCab( 136, true );
					imp.say( imp.pRow() + 0, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "|" + StringFunctions.replicate( "=", 133 ) + "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 4, "R.M.A.   No.: " );
					imp.say( imp.pRow() + 0, 19, rs.getString( "CODRMA" ) );
					imp.say( imp.pRow() + 0, 136, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 4, "Requisitante: " );
					imp.say( imp.pRow() + 0, 19, rs.getString( "IDUSU" ) );
					imp.say( imp.pRow() + 0, 30, "- C.C.: " );
					imp.say( imp.pRow() + 0, 38, ( rs.getString( "CODCC" ) != null ? rs.getString( "CODCC" ).trim() : "" ) );
					imp.say( imp.pRow() + 0, 62, "-" + ( rs.getString( "DESCCC" ) != null ? rs.getString( "DESCCC" ).trim() : "" ) );
					imp.say( imp.pRow() + 0, 113, "- Data : " );
					imp.say( imp.pRow() + 0, 123, StringFunctions.sqlDateToStrDate( rs.getDate( "DTINS" ) ) );
					imp.say( imp.pRow() + 0, 136, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 4, "Aprovação   : " );
					imp.say( imp.pRow() + 0, 19, rs.getString( "IDUSUAPROV" ) );
					imp.say( imp.pRow() + 0, 30, "- C.C.: " );
					imp.say( imp.pRow() + 0, 38, ( rs.getString( 29 ) != null ? rs.getString( 29 ).trim() : "" ) );
					imp.say( imp.pRow() + 0, 62, "-" + ( rs.getString( 30 ) != null ? rs.getString( 30 ).trim() : "" ) );
					imp.say( imp.pRow() + 0, 113, "- Data : " );
					imp.say( imp.pRow() + 0, 123, StringFunctions.sqlDateToStrDate( rs.getDate( "DTAAPROVRMA" ) ) );
					imp.say( imp.pRow() + 0, 136, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 4, "Expedição   : " );
					imp.say( imp.pRow() + 0, 19, rs.getString( "IDUSUEXP" ) );
					imp.say( imp.pRow() + 0, 30, "- C.C.: " );
					imp.say( imp.pRow() + 0, 38, ( rs.getString( 31 ) != null ? rs.getString( 31 ).trim() : "" ) );
					imp.say( imp.pRow() + 0, 62, "-" + ( rs.getString( 32 ) != null ? rs.getString( 32 ).trim() : "" ) );
					imp.say( imp.pRow() + 0, 113, "- Data : " );
					imp.say( imp.pRow() + 0, 123, StringFunctions.sqlDateToStrDate( rs.getDate( "DTAEXPRMA" ) ) );
					imp.say( imp.pRow() + 0, 136, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 4, "O.P/OS.:" );
					imp.say( imp.pRow() + 0, 13, rs.getString( "CodOP" ) != null ? rs.getString( "CodOP" ).trim() : "" );
					imp.say( imp.pRow() + 0, 25, "Seq. O.P.:" );
					imp.say( imp.pRow() + 0, 37, rs.getString( "SeqOP" ) != null ? rs.getString( "SeqOP" ).trim() : "" );
					imp.say( imp.pRow() + 0, 136, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "|" + StringFunctions.replicate( "=", 133 ) + "|" );

					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 57, "DADOS DO(S) PRODUTO(S)" );
					imp.say( imp.pRow() + 0, 136, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 136, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 2, "Item" );
					imp.say( imp.pRow() + 0, 8, "Referencia" );
					imp.say( imp.pRow() + 0, 22, "Descrição dos produtos" );

					if ( isResum ) {
						imp.say( imp.pRow() + 0, 60, "Qtd.aprov." );
						imp.say( imp.pRow() + 0, 75, "Qtd.exp." );
						imp.say( imp.pRow() + 0, 90, "Unid" );
					}

					else {
						imp.say( imp.pRow() + 0, 60, "Qtd.req." );
						imp.say( imp.pRow() + 0, 75, "Qtd.aprov." );
						imp.say( imp.pRow() + 0, 90, "Qtd.exp." );
						imp.say( imp.pRow() + 0, 100, "Sit.item" );
						imp.say( imp.pRow() + 0, 110, "Sit.aprov." );
						imp.say( imp.pRow() + 0, 122, "Sit.exp." );
					}
					imp.say( imp.pRow() + 0, 136, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 136, "|" );
				}
				imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
				imp.say( imp.pRow() + 0, 1, "|" );
				imp.say( imp.pRow() + 0, 2, rs.getString( "CODITRMA" ) );
				imp.say( imp.pRow() + 0, 8, rs.getString( "REFPROD" ) );
				imp.say( imp.pRow() + 0, 22, rs.getString( "DESCPROD" ).substring( 0, 37 ) );
				if ( isResum ) {
					imp.say( imp.pRow() + 0, 60, "" + rs.getDouble( "QTDAPROVITRMA" ) );
					imp.say( imp.pRow() + 0, 75, "" + rs.getDouble( "QTDEXPITRMA" ) );
					imp.say( imp.pRow() + 0, 90, "" + rs.getString( "DESCUNID" ) );
				}
				else {
					imp.say( imp.pRow() + 0, 60, "" + rs.getDouble( "QTDITRMA" ) );
					imp.say( imp.pRow() + 0, 75, "" + rs.getDouble( "QTDAPROVITRMA" ) );
					imp.say( imp.pRow() + 0, 90, "" + rs.getDouble( "QTDEXPITRMA" ) );
					if ( !rs.getString( "SITITRMA" ).equals( "CA" ) )
						imp.say( imp.pRow() + 0, 105, "" + rs.getString( "SITITRMA" ) );
					if ( !rs.getString( "SITAPROVITRMA" ).equals( "NA" ) )
						imp.say( imp.pRow() + 0, 115, "" + rs.getString( "SITAPROVITRMA" ) );
					if ( !rs.getString( "SITEXPITRMA" ).equals( "NE" ) )
						imp.say( imp.pRow() + 0, 125, "" + rs.getString( "SITEXPITRMA" ) );
				}
				imp.say( imp.pRow() + 0, 136, "|" );

				if ( ( rs.getString( "SITITRMA" ).equals( "CA" ) ) || ( rs.getString( "SITAPROVITRMA" ).equals( "NA" ) ) || ( rs.getString( "SITEXPITRMA" ).equals( "NE" ) ) ) {
					if ( comRef() )
						vProdCan.addElement( rs.getString( "REFPROD" ) );
					else
						vProdCan.addElement( rs.getString( "CODPROD" ) );
					vItem.addElement( rs.getString( "CODITRMA" ) );
					vMotivoCan.addElement( rs.getString( "MOTIVOCANCRMA" ) != null ? rs.getString( "MOTIVOCANCRMA" ) : "" );
					cont++;
				}

			}
			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 0, 0, "|" + StringFunctions.replicate( "=", 133 ) + "|" );
			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 0, 1, "|" );
			imp.say( imp.pRow() + 0, 57, "INFORMAÇÕES ADICIONAIS" );
			imp.say( imp.pRow() + 0, 136, "|" );
			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 0, 1, "|" );
			imp.say( imp.pRow() + 0, 136, "|" );
			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 0, 1, "|" );
			imp.say( imp.pRow() + 0, 2, "MOTIVO DA REQUISIÇÃO: " );
			String sMotivoRMA = ( rs.getString( "MOTIVORMA" ) != null ? rs.getString( "MOTIVORMA" ) : "" ).trim();
			imp.say( imp.pRow() + 0, 26, sMotivoRMA.substring( 0, sMotivoRMA.length() > 109 ? 109 : sMotivoRMA.length() ) );
			imp.say( imp.pRow() + 0, 136, "|" );
			if ( cont > 0 ) {
				imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
				imp.say( imp.pRow() + 0, 1, "|" );
				imp.say( imp.pRow() + 0, 4, "ITENS NÃO EXPEDIDOS:" );
				imp.say( imp.pRow() + 0, 136, "|" );

				for ( int i = 0; vProdCan.size() > i; i++ ) {
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 1, "|" );
					imp.say( imp.pRow() + 0, 4, vItem.elementAt( i ).toString() );
					imp.say( imp.pRow() + 0, 9, vProdCan.elementAt( i ).toString() );
					String sMotivoCanc = vMotivoCan.elementAt( i ).toString();

					imp.say( imp.pRow() + 0, 25, "- " + sMotivoCanc.substring( 0, sMotivoCanc.length() > 108 ? 108 : sMotivoCanc.length() ) );
					imp.say( imp.pRow() + 0, 136, "|" );
				}
			}

			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 0, 0, "+" + StringFunctions.replicate( "=", 133 ) + "+" );
			imp.say( imp.pRow() + 2, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 0, 52, StringFunctions.replicate( "_", 41 ) );
			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 0, 62, "Ass. do requisitante" );

			imp.eject();
			imp.fechaGravacao();

			if ( bVisualizar==TYPE_PRINT.VIEW )
				imp.preview( this );

			else
				imp.print();

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	private void imprimirGrafico( final TYPE_PRINT bVisualizar, final ResultSet rs, final String sCab, boolean isResum ) {

		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "EQRMA" ) );
		hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
		hParam.put( "FILTROS", sCab );
		hParam.put( "RESUMIDO", new Boolean( isResum ) );

		if(txtTicket.getVlrInteger()>0) {
			
			hParam.put( "TICKET", txtTicket.getVlrInteger() );
			hParam.put( "REFPRODITRECMERC", txtRefProdItRecMerc.getVlrString() );
			hParam.put( "DESCPRODITRECMERC", txtDescProdItRecMerc.getVlrString() );
			hParam.put( "RAZCLIOS", txtRazCli.getVlrString() );
			
		}
		

		dlGr = new FPrinterJob( "relatorios/FRRma.jasper", "Requisição de material", sCab, rs, hParam, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de Requisição de material!" + err.getMessage(), true, con, err );
			}
		}
	}

	private boolean comRef() {

		return "S".equals( prefere.get( "COMREF" ) );
	}

	private boolean buscaGen() {

		return (Boolean) prefere.get( "BUSCACODPRODGEN" );
	}

	public void keyTyped( KeyEvent kevt ) {

		super.keyTyped( kevt );
	}

	public void keyReleased( KeyEvent kevt ) {

		super.keyReleased( kevt );
	}

	public void beforePost( PostEvent pevt ) {

		String sMotvProir = rgPriod.getVlrString();
		if ( pevt.getListaCampos() == lcDet ) {
			// Validação desnecessária
			//if ( txtQtdAprovRma.floatValue() > txtQtdItRma.floatValue() ) {
				//Funcoes.mensagemInforma( null, "Quantidade aprovada maior que a requerida!" );
				//pevt.getListaCampos().cancelPost();
			//}
			if ( txtQtdExpRma.floatValue() > txtQtdAprovRma.floatValue() ) {
				Funcoes.mensagemInforma( null, "Quantidade expedida maior que a aprovada!" );
				pevt.getListaCampos().cancelPost();
			}
			if ( txtSitItRma.getVlrString().equals( "" ) ) {
				txtSitItRma.setVlrString( "PE" );
			}
			if ( txtSitAprovItRma.getVlrString().equals( "" ) ) {
				txtSitAprovItRma.setVlrString( "PE" );
			}
			if ( txtSitExpItRma.getVlrString().equals( "" ) ) {
				txtSitExpItRma.setVlrString( "PE" );
			}
			if ( txtQtdAprovRma.getVlrString().equals( "" ) ) {
				txtQtdAprovRma.setVlrBigDecimal( new BigDecimal( 0 ) );
			}
			if ( txtQtdExpRma.getVlrString().equals( "" ) ) {
				txtQtdExpRma.setVlrBigDecimal( new BigDecimal( 0 ) );
			}
			if ( sMotvProir.equals( "A" ) ) {
				dialogObsPrior();
			}
		}
		else if ( pevt.getListaCampos() == lcCampos ) {
			if ( txtSitRma.getVlrString().equals( "" ) ) {
				txtSitRma.setVlrString( "PE" );
			}
			if ( txtSitAprovRma.getVlrString().equals( "" ) ) {
				txtSitAprovRma.setVlrString( "PE" );
			}
			if ( txtSitExpRma.getVlrString().equals( "" ) ) {
				txtSitExpRma.setVlrString( "PE" );
			}
		}
	}

	public void beforeInsert( InsertEvent ievt ) {

	}

	public void afterInsert( InsertEvent ievt ) {

		if ( ievt.getListaCampos() == lcCampos ) {
			txtAnoCC.setVlrInteger( anoCC );
			txtCodCC.setVlrString( codCC );
			lcCC.carregaDados();
			txtIDUsu.setVlrString( Aplicativo.getUsuario().getIdusu() );
			txtDtaReqRma.setVlrDate( new Date() );
			txtCodTpMov.setVlrInteger( iCodTpMov );
			lcTipoMov.carregaDados();
			lcCampos.carregaDados();
		}
	}

	public void exec( int iCodRma ) {

		txtCodRma.setVlrString( iCodRma + "" );
		lcCampos.carregaDados();
	}

	public void execDev( int iCodFor, int iCodTipoMov, String sSerie, int iDoc ) {

		lcCampos.insert( true );
	}

	private int buscaVlrPadrao() {

		int iRet = 0;
		String sSQL = "SELECT ANOCENTROCUSTO FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			ResultSet rs = ps.executeQuery();
			if ( rs.next() )
				iRet = rs.getInt( "ANOCENTROCUSTO" );
			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar o ano-base para o centro de custo.\n" + err.getMessage() );
		}

		if ( txtCodTpMov.getVlrString().equals( "" ) ) {
			sSQL = "SELECT CODTIPOMOV8 FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
			try {
				PreparedStatement ps = con.prepareStatement( sSQL );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
				ResultSet rs = ps.executeQuery();
				if ( rs.next() ) {
					if ( rs.getString( 1 ) != null ) {
						iCodTpMov = new Integer( rs.getInt( 1 ) );
					}
					else {
						iCodTpMov = new Integer( 0 );
						Funcoes.mensagemInforma( null, "Não existe um tipo de movimento padrão para RMA definido nas preferências!" );
					}
				}
				rs.close();
				ps.close();
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao buscar o ano-base para o centro de custo.\n" + err.getMessage() );
			}
		}

		return iRet;
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcCli.setConexao( cn );
		lcContrato.setConexao( cn );
		lcItContrato.setConexao( cn );

		prefere = getPrefere();

		if ( "S".equals( (String) prefere.get( "LANCARMACONTR" ) ) ) {
			adicInfoContrato();
		}

		montaDetalhe();

		lcUnid.setConexao( cn );
		lcTipoMov.setConexao( cn );
		lcProd.setConexao( cn );
		lcProd2.setConexao( cn );
		lcLote.setConexao( cn );
		lcCC.setConexao( cn );
		lcCC.setWhereAdic( "NIVELCC=10 AND ANOCC=" + buscaVlrPadrao() );
		lcAlmox.setConexao( cn );
		lcUsu.setConexao( cn );
		lcOP.setConexao( cn );
		lcSeqOP.setConexao( cn );
		String sSQL = "SELECT anoCC, codCC, codAlmox, aprovCPSolicitacaoUsu FROM SGUSUARIO WHERE CODEMP=? AND CODFILIAL=? AND IDUsu=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			ps.setString( 3, Aplicativo.getUsuario().getIdusu() );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				anoCC = new Integer( rs.getInt( "anoCC" ) );
				if ( anoCC.intValue() == 0 )
					anoCC = new Integer( buscaVlrPadrao() );
				codCC = rs.getString( "codCC" );
			}

			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage() );
		}

		carregaWhereAdic();
	}

	public Color cor( int r, int g, int b ) {

		Color color = new Color( r, g, b );
		return color;
	}

	private void adicInfoContrato() {

		try {
			tpnCab.addTab( "Contrato/Projeto", pinCabEnquadramento );

			setPainel( pinCabEnquadramento );

			adic( new JLabelPad( "Cód.Cliente" ), 7, 10, 90, 20 );
			adic( txtCodCli, 7, 30, 90, 20 );
			adic( new JLabelPad( "Razão Social do Cliente." ), 100, 10, 510, 20 );
			adic( txtRazCli, 100, 30, 510, 20 );

			adic( new JLabelPad( "Contrato/Projeto" ), 7, 50, 284, 20 );
			adic( cbContr, 7, 70, 284, 20 );
			adic( new JLabelPad( "Item" ), 298, 50, 320, 20 );
			adic( cbitContr, 298, 70, 310, 20 );

		} catch ( Exception e ) {
			e.printStackTrace();
		}
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







