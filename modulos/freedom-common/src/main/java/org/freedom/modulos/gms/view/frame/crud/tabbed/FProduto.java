/**
 * @version 08/12/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FProduto.java <BR>
 * 
 *                   Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                   modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                   na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                   Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                   sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                   Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                   Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                   de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                   Cadastro de produtos
 * 
 */

package org.freedom.modulos.gms.view.frame.crud.tabbed;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.CheckBoxEvent;
import org.freedom.acao.CheckBoxListener;
import org.freedom.acao.EditEvent;
import org.freedom.acao.EditListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.acao.RadioGroupEvent;
import org.freedom.acao.RadioGroupListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.object.CustosProd;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.EANFactory;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.component.PainelImagem;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FAndamento;
import org.freedom.library.swing.frame.FTabDados;
import org.freedom.library.swing.util.SwingParams;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.crm.view.frame.crud.plain.FTipoChamado;
import org.freedom.modulos.fnc.library.swing.component.JTextFieldPlan;
import org.freedom.modulos.gms.business.object.TipoProd;
import org.freedom.modulos.gms.view.dialog.report.DLRProduto;
import org.freedom.modulos.gms.view.frame.crud.plain.FSecaoProd;
import org.freedom.modulos.gms.view.frame.crud.special.FGrupoProd;
import org.freedom.modulos.lvf.view.frame.crud.detail.FCLFiscal;
import org.freedom.modulos.pcp.view.frame.crud.detail.FEstrutura;
import org.freedom.modulos.std.view.frame.crud.detail.FPlanoPag;
import org.freedom.modulos.std.view.frame.crud.plain.FAlmox;
import org.freedom.modulos.std.view.frame.crud.plain.FCaixa;
import org.freedom.modulos.std.view.frame.crud.plain.FClasCli;
import org.freedom.modulos.std.view.frame.crud.plain.FMarca;
import org.freedom.modulos.std.view.frame.crud.plain.FPrazoEnt;
import org.freedom.modulos.std.view.frame.crud.plain.FTabPreco;
import org.freedom.modulos.std.view.frame.crud.plain.FUnidade;
import org.freedom.modulos.std.view.frame.crud.special.FCentroCusto;
import org.freedom.modulos.std.view.frame.crud.special.FPlanejamento;
import org.freedom.modulos.std.view.frame.crud.tabbed.FFornecedor;
import org.freedom.modulos.std.view.frame.crud.tabbed.FMoeda;

public class FProduto extends FTabDados implements CheckBoxListener, EditListener, InsertListener, ChangeListener, ActionListener, CarregaListener, RadioGroupListener, PostListener {

	private static final long serialVersionUID = 1L;

	private int casasDec = Aplicativo.casasDec;

	private int casasDecFin = Aplicativo.casasDecFin;

	private int casasDecPre = Aplicativo.casasDecPre;
	
	private JPanelPad pinGeral = new JPanelPad();

	private JPanelPad pnFatConv = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnProdPlan = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnFor = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnCodAltProd = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnCodAcess = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnOutros = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );
	
	private JPanelPad pnFSC = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );
	
	private JPanelPad pnServico = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnLote = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnSerie = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnFoto = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnPreco = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );
	
	private JPanelPad pnMGProduto = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtRefProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCodMoeda = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtCodUnid = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtCodAltProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCodPA = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );// código de acesso

	private JTextFieldPad txtAnoCCPA = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );

	private JTextFieldPad txtCodCCPA = new JTextFieldPad( JTextFieldPad.TP_STRING, 19, 0 );

	private JTextFieldPad txtCodCaixa = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescCCPA = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtDescCaixa = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodFisc = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtCodMarca = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtCodSecao = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtCodGrup = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtCodAlmox = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodTpChamado = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtDescProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtDescAuxProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodBarProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtCodEANProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtCodFabProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private JTextFieldPad txtVlrDens = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtCubagem = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtVlrConcent = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtVlrCompri = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtVlrLarg = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtVlrEspess = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtQtdEmbalagem = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );
	
	private JTextFieldPad txtNroDiasValid = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );
	
	private JTextFieldPad txtNroPlanos = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );
	
	private JTextFieldPad txtQtdPorPlano = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );
	
	private JTextFieldPad txtFatorFSC = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, casasDec );

	private JTextFieldPad txtQtdHorasServ = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );
	
	private JTextFieldPad txtComisProd = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, casasDecFin );

	private JTextFieldPad txtPesoLiqProd = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtPesoBrutProd = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtQtdMinProd = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtQtdMaxProd = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtLocalProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private JTextFieldPad txtCustoMPMProd = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtCustoInfoProd = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtCustoPEPSProd = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtSldProd = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtCustoMPMAlmox = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtCustoPEPSAlmox = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtSldAlmox = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtDtUltEntrada = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, casasDec );
	
//	private JTextFieldDec txtTeste = new JTextFieldDec( JTextFieldPad.TP_DECIMAL, 10, casasDec );

	//private JTextFieldPad txtSldConsigProd = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	//private JTextFieldPad txtSldConsigAlmox = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	//private JTextFieldPad txtSldResProd = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	//private JTextFieldPad txtSldResAlmox = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	//private JTextFieldPad txtSldLiqProd = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	//private JTextFieldPad txtSldLiqAlmox = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtPrecoBaseProd = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );
	
	private JTextFieldPad txtPrecoComisProd = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtUnidFat = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtFatConv = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtCodLote = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtDiniLote = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtVenctoLote = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtNumSerie = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtDtFabricSerie = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDtValidSerie = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtSldLote = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtSldResLote = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtSldConsigLote = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 3 );

	private JTextFieldPad txtSldLiqLote = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtQtdProdLote = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtDiasAvisoLote = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtCodFotoProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDescFotoProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtLargFotoProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtAltFotoProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodPrecoProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodClasCliPreco = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodTabPreco = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodPlanoPagPreco = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtPrecoProd = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre ); 
	
	private JTextFieldPad txtTipoPrecoProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 1,0 ); 

	private JTextFieldPad txtDtAltPreco = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtHAltPreco = new JTextFieldPad( JTextFieldPad.TP_TIME, 10, 0 );
	
	private JTextFieldPad txtIdUsuAltPreco = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 ); 

	private JTextFieldPad txtDtInsPreco = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtHInsPreco = new JTextFieldPad( JTextFieldPad.TP_TIME, 10, 0 );

	private JTextFieldPad txtIdUsuInsPreco = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );
	
	private JTextFieldPad txtSeqPP = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPlan txtCodPlan = new JTextFieldPlan( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtAnoCC = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );

	private JTextFieldPad txtCodCC = new JTextFieldPad( JTextFieldPad.TP_STRING, 19, 0 );

	private JTextFieldPad txtPrazoEnt = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextAreaPad txaMGDescProd = new JTextAreaPad();
	
	private JTextAreaPad txaMGDescProdCompl = new JTextAreaPad();
	
	private JTextFieldFK txtDias = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldFK txtDescPrazoEnt = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtDescPlan = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtDescCC = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	//private JTextFieldFK txtAlmox = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtDescMoeda = new JTextFieldFK( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldFK txtDescUnid = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCodProdFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );
	
	private JTextFieldPad txtCodProdMG = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtRefProdFor = new JTextFieldPad( JTextFieldPad.TP_STRING, 18, 0 );

	private JTextFieldFK txtDescFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldFK txtDescFisc = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtDescMarca = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtDescSecao = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtDescGrup = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtDescAlmox = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );
	
	private JTextFieldFK txtDescTpChamado = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtDescUnidFat = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtDescClasCliPreco = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtDescTabPreco = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtDescPlanoPagPreco = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );
	
	private JTextFieldPad txtPrazoRepo = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtMediaVenda = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );
	
	private Vector<String> vLabsTipo = new Vector<String>();

	private Vector<String> vValsTipo = new Vector<String>();

	private Vector<String> vLabsCV = new Vector<String>();

	private Vector<String> vValsCV = new Vector<String>();

	private Vector<String> vLabsTF = new Vector<String>();

	private Vector<String> vValsTF = new Vector<String>();

	private Vector<String> vLabsTipoPP = new Vector<String>();

	private Vector<String> vValsTipoPP = new Vector<String>();

	private Vector<String> vValsTipoConv = new Vector<String>();

	private Vector<String> vLabsTipoConv = new Vector<String>();

	private Vector<String> vLabsPA = new Vector<String>();

	private Vector<String> vValsPA = new Vector<String>();

	private Vector<String> vLabsBCusto = new Vector<String>();

	private Vector<String> vValsBCusto = new Vector<String>();

	private JRadioGroup<?, ?> rgPA = null;

	private JRadioGroup<?, ?> rgTipoProd = null;

	private JRadioGroup<?, ?> rgCV = null;

	private JRadioGroup<?, ?> rgTF = null;

	private JRadioGroup<?, ?> rgTipoPP = null;

	private JRadioGroup<?, ?> rgAbaixCust = null;

	private JCheckBoxPad cbLote = null;
	
	private JCheckBoxPad cbDescCli = new JCheckBoxPad( "Permite desconto do cliente", "S", "N" );

	private JCheckBoxPad cbAtivoPrecoProd = new JCheckBoxPad( "Tabela de preço Ativa?", "S", "N" );
	
	private JCheckBoxPad cbReceita = null;

	private JCheckBoxPad cbSerie = null;

	private JCheckBoxPad cbGuiaTraf = null;

	private JCheckBoxPad cbAtivo = null;

	private JRadioGroup<?, ?> rgTipoConv = null;

	private JCheckBoxPad cbRMA = null;

	private JCheckBoxPad cbAdicPDV = null;

	private JTablePad tabFatConv = new JTablePad();

	private JScrollPane spnFatConv = new JScrollPane( tabFatConv );

	private JTablePad tabProdPlan = new JTablePad();

	private JScrollPane spnPlan = new JScrollPane( tabProdPlan );

	private JTablePad tabFor = new JTablePad();

	private JTablePad tabCodAltProd = new JTablePad();

	private JTablePad tabCodAcess = new JTablePad();

	private JScrollPane spnFor = new JScrollPane( tabFor );

	private JScrollPane spnCodAltProd = new JScrollPane( tabCodAltProd );

	private JScrollPane spnCodAcess = new JScrollPane( tabCodAcess );

	private JTablePad tabLote = new JTablePad();

	private JTablePad tabSerie = new JTablePad();

	private JScrollPane spnLote = new JScrollPane( tabLote );

	private JScrollPane spnSerie = new JScrollPane( tabSerie );

	private JTablePad tabFoto = new JTablePad();

	private JScrollPane spnFoto = new JScrollPane( tabFoto );

	private JTablePad tabPreco = new JTablePad();

	private JScrollPane spnPreco = new JScrollPane( tabPreco );

	private JPanelPad pinRodFatConv = new JPanelPad( 650, 120 );

	private JPanelPad pinRodProdPlan = new JPanelPad( 650, 120 );

	private JPanelPad pinRodFor = new JPanelPad( 650, 80 );

	private JPanelPad pinRodCodAltProd = new JPanelPad( 650, 80 );

	private JPanelPad pinRodCodAcess = new JPanelPad( 650, 120 );

	private JPanelPad pinOutros = new JPanelPad( 650, 120 );
	
	private JPanelPad pinFSC = new JPanelPad( 650, 120 );
	
	private JPanelPad pinServico = new JPanelPad( 650, 120 );

	private JPanelPad pinRodLote = new JPanelPad( 650, 120 );

	private JPanelPad pinRodSerie = new JPanelPad( 650, 165 );

	private JPanelPad pinRodFoto = new JPanelPad( 650, 170 );

	private JPanelPad pinRodPreco = new JPanelPad( 650, 120 );
	
	private JPanelPad pinMGProduto = new JPanelPad(650, 550);
	
	private JPanelPad pnDesc = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );
	
	private JPanelPad pnObsProd = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	private JTextAreaPad txaDescComp = new JTextAreaPad(500);
	
	private JTextAreaPad txaObsProd = new JTextAreaPad(10000);

	private JTextAreaPad txaObsSerie = new JTextAreaPad(10000);

	private JScrollPane spnDesc = new JScrollPane( txaDescComp );
	
	private JScrollPane spnObsProd = new JScrollPane( txaObsProd );
	
	
	private JScrollPane spnObsSerie = new JScrollPane( txaObsSerie );
	
/*	private JScrollPane spnMGDescProd = new JScrollPane( txaMGDescProd );
	
	private JScrollPane spnMGDescProdCompl = new JScrollPane( txaMGDescProdCompl );
*/
	private ListaCampos lcMoeda = new ListaCampos( this, "MA" );

	private ListaCampos lcUnid = new ListaCampos( this, "UD" );

	private ListaCampos lcFisc = new ListaCampos( this, "FC" );

	private ListaCampos lcMarca = new ListaCampos( this, "MC" );

	private ListaCampos lcSecao = new ListaCampos( this, "SC" );

	private ListaCampos lcGrup = new ListaCampos( this, "GP" );

	private ListaCampos lcAlmox = new ListaCampos( this, "AX" );
	
	private ListaCampos lcTipoChamado = new ListaCampos( this, "TC" );

	private ListaCampos lcPrazoEnt = new ListaCampos( this, "PE" );

	private ListaCampos lcFatConv = new ListaCampos( this, "" );

	private ListaCampos lcProdPlan = new ListaCampos( this );

	private ListaCampos lcProdFor = new ListaCampos( this, "PD" );

	private ListaCampos lcCodAltProd = new ListaCampos( this, "" );

	private ListaCampos lcProdAcesso = new ListaCampos( this );

	private ListaCampos lcUnidFat = new ListaCampos( this, "" );

	private ListaCampos lcPlan = new ListaCampos( this, "PN" );

	private ListaCampos lcCC = new ListaCampos( this, "CC" );

	private ListaCampos lcCCAcesso = new ListaCampos( this, "CC" );

	private ListaCampos lcCaixa = new ListaCampos( this, "CX" );

	private ListaCampos lcForFK = new ListaCampos( this, "FR" );

	private ListaCampos lcLote = new ListaCampos( this );

	private ListaCampos lcSerie = new ListaCampos( this );

	private ListaCampos lcFoto = new ListaCampos( this );

	private ListaCampos lcPreco = new ListaCampos( this );
	
	private ListaCampos lcMGProduto = new ListaCampos( this );

	private ListaCampos lcClasCliPreco = new ListaCampos( this, "CC" );

	private ListaCampos lcTabPreco = new ListaCampos( this, "TB" );

	private ListaCampos lcPlanoPagPreco = new ListaCampos( this, "PG" );

	private Navegador navFatConv = new Navegador( true );

	private Navegador navProdPlan = new Navegador( true );

	private Navegador navFor = new Navegador( true );

	private Navegador navLote = new Navegador( true );

	private Navegador navSerie = new Navegador( true );

	private Navegador navFoto = new Navegador( true );

	private Navegador navPreco = new Navegador( true );

	private Navegador navCodAltProd = new Navegador( true );

	private Navegador navCodAcess = new Navegador( true );
	
	private Navegador navMGProduto = new Navegador( false );

	private JButtonPad btCopiar = new JButtonPad( Icone.novo( "btCopiar.png" ) );

	private JButtonPad btCodBar = new JButtonPad( "", Icone.novo( "btCodBar.png" ) );

	private PainelImagem imFotoProd = new PainelImagem( 65000 );

	private String[] sPrefs = null;

	private JCheckBoxPad cbCpFatConv = new JCheckBoxPad( "", "S", "N" );
	
	private JCheckBoxPad cbCertFSC = new JCheckBoxPad( "Certificado FSC", "S", "N" );

	private enum eprefs {
		CODMOEDA, PEPSPROD, TIPOCODBAR, CODEANEMP, CODPAISEMP, TAMDESCPROD, CVPROD, VERIFPROD, RMAPROD, TIPOPROD
	};

	private JLabelPad lbUnidFat = null;

	private JLabelPad lbDescUnidFat = null;

	private JLabelPad lbFatConv = null;

	private JLabelPad lbCpFatConv = null;

	private JLabelPad lbCodProdEst = null;

	private JLabelPad lbDescProdEst = null;

	private JLabelPad lbSeqEst = null;

	private JTextFieldPad txtCodProdEst = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescProdEst = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtSeqEst = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private ListaCampos lcProdEstCod = new ListaCampos( this, "ET" );

	private JLabelPad separador_atividade = new JLabelPad();

	private JLabelPad separador_exigencia = new JLabelPad();

	private JLabelPad separador_adicional = new JLabelPad();

	private JLabelPad separador_radios = new JLabelPad();

	private JLabelPad separador_ultima_entrada = new JLabelPad();

	
	public FProduto() {

		super();

		nav.setNavigation( true );

		setTitulo( "Cadastro de Produtos" );
		setAtribos( 30, 10, 685, 660 );

		//Log de inserção adicionado como True para carregar no grid.
		lcPreco.setLoginstab( true );
		//lcMGProduto.setLoginstab( true );
		
		lcFatConv.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcFatConv );
		lcFatConv.setTabela( tabFatConv );

		lcProdPlan.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcProdPlan );
		lcProdPlan.setTabela( tabProdPlan );
		lcProdFor.setMaster( lcCampos );

		lcCodAltProd.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcProdFor );
		lcCampos.adicDetalhe( lcCodAltProd );
		lcProdFor.setTabela( tabFor );
		lcCodAltProd.setTabela( tabCodAltProd );

		lcProdAcesso.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcProdAcesso );
		lcProdAcesso.setTabela( tabCodAcess );

		lcLote.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcLote );
		lcLote.setTabela( tabLote );

		lcSerie.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcSerie );
		lcSerie.setTabela( tabSerie );

		lcFoto.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcFoto );
		lcFoto.setTabela( tabFoto );

		lcPreco.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcPreco );
		lcPreco.setTabela( tabPreco );
		
		lcMGProduto.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcMGProduto );

		lcCampos.addInsertListener( this );
		lcCampos.addCarregaListener( this );
		lcPreco.addCarregaListener( this );
		lcFoto.addEditListener( this );
		lcFoto.addInsertListener( this );
		
		lcMGProduto.addEditListener( this );
		lcMGProduto.addInsertListener( this );
		lcMGProduto.addCarregaListener( this );
		
		
		lcProdAcesso.addInsertListener( this );
		lcProdAcesso.addCarregaListener( this );

		setPainel( pinGeral );
		adicTab( "Geral", pinGeral );

		btCopiar.setToolTipText( "Copiar produto" );

		lcMoeda.add( new GuardaCampo( txtCodMoeda, "CodMoeda", "Cód. moeda", ListaCampos.DB_PK, true ) );
		lcMoeda.add( new GuardaCampo( txtDescMoeda, "SingMoeda", "Descrição da moeda", ListaCampos.DB_SI, false ) );
		lcMoeda.montaSql( false, "MOEDA", "FN" );
		lcMoeda.setReadOnly( true );
		lcMoeda.setQueryCommit( false );
		txtCodMoeda.setTabelaExterna( lcMoeda, FMoeda.class.getCanonicalName() );

		lcUnid.add( new GuardaCampo( txtCodUnid, "CodUnid", "Cód.unid.", ListaCampos.DB_PK, true ) );
		lcUnid.add( new GuardaCampo( txtDescUnid, "DescUnid", "Descrição da unidade", ListaCampos.DB_SI, false ) );
		lcUnid.montaSql( false, "UNIDADE", "EQ" );
		lcUnid.setReadOnly( true );
		lcUnid.setQueryCommit( false );
		txtCodUnid.setTabelaExterna( lcUnid, FUnidade.class.getCanonicalName() );

		lcMarca.add( new GuardaCampo( txtCodMarca, "CodMarca", "Cód.marca", ListaCampos.DB_PK, true ) );
		lcMarca.add( new GuardaCampo( txtDescMarca, "DescMarca", "Descrição da marca", ListaCampos.DB_SI, false ) );
		lcMarca.montaSql( false, "MARCA", "EQ" );
		lcMarca.setReadOnly( true );
		lcMarca.setQueryCommit( false );
		txtCodMarca.setTabelaExterna( lcMarca, FMarca.class.getCanonicalName() );

		lcSecao.add( new GuardaCampo( txtCodSecao, "CodSecao", "Cód.Seção", ListaCampos.DB_PK, false ) );
		lcSecao.add( new GuardaCampo( txtDescSecao, "DescSecao", "Descrição da seção", ListaCampos.DB_SI, false ) );
		lcSecao.montaSql( false, "SECAO", "EQ" );
		lcSecao.setReadOnly( true );
		lcSecao.setQueryCommit( false );
		txtCodSecao.setTabelaExterna( lcSecao, FSecaoProd.class.getCanonicalName() );

		lcFisc.add( new GuardaCampo( txtCodFisc, "CodFisc", "Cód.c.fisc.", ListaCampos.DB_PK, true ) );
		lcFisc.add( new GuardaCampo( txtDescFisc, "DescFisc", "Descrição da classificação fiscal", ListaCampos.DB_SI, false ) );
		lcFisc.montaSql( false, "CLFISCAL", "LF" );
		lcFisc.setReadOnly( true );
		lcFisc.setQueryCommit( false );
		txtCodFisc.setTabelaExterna( lcFisc, FCLFiscal.class.getCanonicalName() );

		lcGrup.add( new GuardaCampo( txtCodGrup, "CodGrup", "Cód.grupo", ListaCampos.DB_PK, true ) );
		lcGrup.add( new GuardaCampo( txtDescGrup, "DescGrup", "Descrição do grupo", ListaCampos.DB_SI, false ) );
		lcGrup.montaSql( false, "GRUPO", "EQ" );
		lcGrup.setReadOnly( true );
		lcGrup.setQueryCommit( false );
		txtCodGrup.setTabelaExterna( lcGrup, FGrupoProd.class.getCanonicalName() );

		lcAlmox.add( new GuardaCampo( txtCodAlmox, "CodAlmox", "Cód.almox.", ListaCampos.DB_PK, true ) );
		lcAlmox.add( new GuardaCampo( txtDescAlmox, "DescAlmox", "Descrição do almoxarifado", ListaCampos.DB_SI, false ) );
		lcAlmox.montaSql( false, "ALMOX", "EQ" );
		lcAlmox.setReadOnly( true );
		lcAlmox.setQueryCommit( false );
		txtCodAlmox.setTabelaExterna( lcAlmox, FAlmox.class.getCanonicalName() );

		lcPrazoEnt.add( new GuardaCampo( txtPrazoEnt, "CodPE", "Prazo para entrega", ListaCampos.DB_PK, false ) );
		lcPrazoEnt.add( new GuardaCampo( txtDescPrazoEnt, "DescPE", "Descrição do prazo de entrega", ListaCampos.DB_SI, false ) );
		lcPrazoEnt.add( new GuardaCampo( txtDias, "DiasPE", "Nº de dias", ListaCampos.DB_SI, false ) );
		lcPrazoEnt.montaSql( false, "PRAZOENT", "VD" );
		lcPrazoEnt.setReadOnly( true );
		lcPrazoEnt.setQueryCommit( false );
		txtPrazoEnt.setTabelaExterna( lcPrazoEnt, FPrazoEnt.class.getCanonicalName() );

		lcProdEstCod.add( new GuardaCampo( txtCodProdEst, "CodProd", "Cód.Prod.", ListaCampos.DB_PK, txtDescProdEst, false ) );
		lcProdEstCod.add( new GuardaCampo( txtSeqEst, "SeqEst", "Seq.Est.", ListaCampos.DB_PK, txtDescProdEst, false ) );
		lcProdEstCod.add( new GuardaCampo( txtDescProdEst, "DescEst", "Descrição da estrutura", ListaCampos.DB_SI, false ) );

		lcProdEstCod.setWhereAdic( "ATIVOEST='S'" );
		lcProdEstCod.montaSql( false, "ESTRUTURA", "PP" );
		lcProdEstCod.setQueryCommit( false );
		lcProdEstCod.setReadOnly( true );
		txtCodProdEst.setTabelaExterna( lcProdEstCod, FEstrutura.class.getCanonicalName() );
		txtSeqEst.setTabelaExterna( lcProdEstCod, FEstrutura.class.getCanonicalName() );
		txtCodProdEst.setNomeCampo( "codprodet" );
		
		lcTipoChamado.add( new GuardaCampo( txtCodTpChamado, "CodTpChamado", "Cód.Tp.Cham.", ListaCampos.DB_PK, true ) );
		lcTipoChamado.add( new GuardaCampo( txtDescTpChamado, "DescTpChamado", "Descrição do tipo de chamado", ListaCampos.DB_SI, false ) );
		lcTipoChamado.montaSql( false, "TIPOCHAMADO", "CR" );
		lcTipoChamado.setReadOnly( true );
		lcTipoChamado.setQueryCommit( false );
		txtCodTpChamado.setTabelaExterna( lcTipoChamado, FTipoChamado.class.getCanonicalName() );
/*
		vValsTipo.addElement( "P" );
		vValsTipo.addElement( "S" );
		vValsTipo.addElement( "E" );
		vValsTipo.addElement( "F" );
		vValsTipo.addElement( "M" );
		vValsTipo.addElement( "O" );
		vValsTipo.addElement( "C" );

		vLabsTipo.addElement( "Comércio" );
		vLabsTipo.addElement( "Serviço" );
		vLabsTipo.addElement( "Equipamento" );
		vLabsTipo.addElement( "Fabricação" );
		vLabsTipo.addElement( "Mat.prima" );
		vLabsTipo.addElement( "Patrimônio" );
		vLabsTipo.addElement( "Consumo" );
*/
	//	rgTipo = new JRadioGroup<String, String>( 3, 4, vLabsTipo, vValsTipo );
		
		rgTipoProd = new JRadioGroup<String, String>( 4, 3, TipoProd.getLabels(), TipoProd.getValores() );
		
		rgTipoProd.setFont( SwingParams.getFontboldmed() );
		
		rgTipoProd.setVlrString( "P" );

		vValsCV.addElement( "C" );
		vValsCV.addElement( "V" );
		vValsCV.addElement( "A" );
		vLabsCV.addElement( "Compra" );
		vLabsCV.addElement( "Venda" );
		vLabsCV.addElement( "Ambos" );
		rgCV = new JRadioGroup<String, String>( 3, 1, vLabsCV, vValsCV );
		rgCV.setVlrString( "V" );

		vValsTF.addElement( "P" );
		vValsTF.addElement( "M" );
		vValsTF.addElement( "N" );
		vValsTF.addElement( "G" );
		vLabsTF.addElement( "Pequena" );
		vLabsTF.addElement( "Média" );
		vLabsTF.addElement( "Natural" );
		vLabsTF.addElement( "Grande" );
		rgTF = new JRadioGroup<String, String>( 1, 4, vLabsTF, vValsTF );
		rgTF.setVlrString( "P" );

		vValsTipoPP.addElement( "R" );
		vValsTipoPP.addElement( "D" );
		vLabsTipoPP.addElement( "Receitas" );
		vLabsTipoPP.addElement( "Despesas" );
		rgTipoPP = new JRadioGroup<String, String>( 1, 2, vLabsTipoPP, vValsTipoPP );
		rgTipoPP.setVlrString( "R" );

		vValsTipoConv.addElement( "U" );
		vValsTipoConv.addElement( "P" );
		vLabsTipoConv.addElement( "Unidade" );
		vLabsTipoConv.addElement( "Produto" );
		rgTipoConv = new JRadioGroup<String, String>( 2, 1, vLabsTipoConv, vValsTipoConv );

		rgTipoConv.addRadioGroupListener( this );

		cbCpFatConv.setVlrString( "N" );

		vValsPA.addElement( "RMA" );
		vValsPA.addElement( "PDV" );
		vLabsPA.addElement( "RMA" );
		vLabsPA.addElement( "PDV" );
		rgPA = new JRadioGroup<String, String>( 1, 2, vLabsPA, vValsPA );
		rgPA.setVlrString( "R" );
		rgPA.addRadioGroupListener( this );

		cbLote = new JCheckBoxPad( "Exige", "S", "N" );
		cbLote.setVlrString( "N" );
		cbLote.addCheckBoxListener( this );

		cbAtivo = new JCheckBoxPad( "Sim", "S", "N" );
		cbAtivo.setVlrString( "S" );

		vValsBCusto.addElement( "N" );
		vValsBCusto.addElement( "S" );
		vValsBCusto.addElement( "L" );
		vLabsBCusto.addElement( "Bloqueado" );
		vLabsBCusto.addElement( "Senha" );
		vLabsBCusto.addElement( "Liberado" );
		rgAbaixCust = new JRadioGroup<String, String>( 3, 1, vLabsBCusto, vValsBCusto );
		rgAbaixCust.setVlrString( "N" );
		rgAbaixCust.addRadioGroupListener( this );

		cbRMA = new JCheckBoxPad( "Sim", "S", "N" );
		cbRMA.setVlrString( "S" );

		cbAdicPDV = new JCheckBoxPad( "Tela adicional", "S", "N" );
		cbAdicPDV.setVlrString( "N" );

		cbReceita = new JCheckBoxPad( "Exige", "S", "N" );
		cbReceita.setVlrString( "N" );

		cbGuiaTraf = new JCheckBoxPad( "Exige", "S", "N" );
		cbGuiaTraf.setVlrString( "N" );

		cbSerie = new JCheckBoxPad( "Exige", "S", "N" );
		cbSerie.setVlrString( "N" );

		txtCustoMPMProd.setSoLeitura( true );
		txtCustoPEPSProd.setSoLeitura( true );
		txtSldProd.setSoLeitura( true );
		//txtSldResProd.setSoLeitura( true );
		txtDtUltEntrada.setSoLeitura( true );
		//txtSldConsigProd.setSoLeitura( true );
		//txtSldLiqProd.setSoLeitura( true );

		//txtAlmox.setSoLeitura( true );
		txtCustoMPMAlmox.setSoLeitura( true );
		txtCustoPEPSAlmox.setSoLeitura( true );
		txtSldAlmox.setSoLeitura( true );
		//txtSldResAlmox.setSoLeitura( true );
		//txtSldConsigAlmox.setSoLeitura( true );
		//txtSldLiqAlmox.setSoLeitura( true );
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		btCodBar.addActionListener( this );
		tpn.addChangeListener( this );

		lcCampos.addPostListener( this );
		lcFatConv.addPostListener( this );

		setImprimir( true );

	}

	private void habAcesso( boolean hab, int tipo ) {

		if ( tipo == 0 ) {
			txtCodPA.setAtivo( hab );
			rgPA.setAtivo( hab );
		}
		if ( ( tipo == 0 ) || ( tipo == 1 ) ) {
			txtAnoCCPA.setAtivo( hab );
			txtCodCCPA.setAtivo( hab );
		}
		if ( ( tipo == 0 ) || ( tipo == 2 ) ) {
			txtCodCaixa.setAtivo( hab );
		}
	}

	private void montaTela() {

		adicCampo( txtCodProd, 7, 20, 60, 20, "CodProd", "Cód.prod.", ListaCampos.DB_PK, true );

		adicCampo( txtRefProd, 70, 20, 115, 20, "RefProd", "Referência", ListaCampos.DB_SI, true );

		adicCampo( txtDescProd, 188, 20, 360, 20, "DescProd", "Descrição do produto", ListaCampos.DB_SI, true );

		JPanelPad pnSaldo = new JPanelPad();
		pnSaldo.setBorder( SwingParams.getPanelLabel( "Saldo", Color.BLUE ) );
		
		adic( pnSaldo, 551, 00, 106, 84  );
		
		pnSaldo.adic( txtSldProd, 0, 0, 90, 45 );
		txtSldProd.setFont( SwingParams.getFontboldmax() );
		
//		adicCampo( txtSldProd, 638, 140, 76, 20, "SldProd", "Saldo", ListaCampos.DB_SI, false );
		
		adicCampoInvisivel( txtSldProd, "SldProd", "Saldo", ListaCampos.DB_SI, false );
		
//		adicDB( rgTipo, 540, 20, 115, 140, "TipoProd", "Tipo:", true );

		adicCampo( txtDescAuxProd, 7, 60, 320, 20, "DescAuxProd", "Descrição auxiliar", ListaCampos.DB_SI, false );
		
		adicCampo( txtPrazoRepo, 330, 60, 110, 20, "PrazoRepo", "Prazo repos.(dias)", ListaCampos.DB_SI, false );
		
		adicCampo( txtMediaVenda, 443, 60, 106, 20, "MediaVenda", "Média Venda", ListaCampos.DB_SI, false );

		adicCampo( txtCodBarProd, 7, 100, 225, 20, "CodBarProd", "Código de barras", ListaCampos.DB_SI, true );

		adic( btCodBar, 233, 100, 20, 20 );

		adicCampo( txtCodEANProd, 253, 100, 210, 20, "CodEANProd", "Código EAN", ListaCampos.DB_SI, false );

		adicCampo( txtCodFabProd, 466, 100, 182, 20, "CodFabProd", "Cód. fabricante", ListaCampos.DB_SI, true );

		adicCampo( txtPrecoBaseProd, 7, 140, 75, 20, "PrecoBaseProd", "Preço base", ListaCampos.DB_SI, true );
		
//		adic( txtTeste, 7,140, 75, 20, "Teste");

		adicCampo( txtCustoInfoProd, 85, 140, 70, 20, "CustoInfoProd", "Custo infor.", ListaCampos.DB_SI, false );
		
		adicCampo( txtPrecoComisProd, 158, 140, 75, 20, "PrecoComisProd", "Preço comis.", ListaCampos.DB_SI, false );

		adicCampo( txtComisProd, 236, 140, 50, 20, "ComisProd", "%Comis.", ListaCampos.DB_SI, true );

		adicCampo( txtQtdMinProd, 289, 140, 50, 20, "QtdMinProd", "Qtd.min.", ListaCampos.DB_SI, true );

		adicCampo( txtQtdMaxProd, 342, 140, 52, 20, "QtdMaxProd", "Qtd.máx.", ListaCampos.DB_SI, true );

		adic( separador_ultima_entrada, 400, 130, 2, 35);
		
		adic( txtDtUltEntrada, 404, 140, 85, 20, "Última entrada" );
		
//		adicCampo( txtLocalProd, 7, 180, 165, 20, "LocalProd", "Local armz.", ListaCampos.DB_SI, false );

		adic( txtCustoMPMProd, 492, 140, 78, 20, "Custo MPM" );
		
		adic( txtCustoPEPSProd, 573, 140, 78, 20, "Custo PEPS" ); // Sem inserir no lista campos
//		adicCampo( txtSldProd, 638, 140, 76, 20, "SldProd", "Saldo", ListaCampos.DB_SI, false );

		

		btCodBar.setToolTipText( "Gera cód. barras" );

		
		//adicCampo( txtSldResProd, 412, 180, 76, 20, "SldResProd", "Saldo res.", ListaCampos.DB_SI, false );
		//adicCampo( txtSldConsigProd, 491, 180, 76, 20, "SldConsigProd", "Saldo consig.", ListaCampos.DB_SI, false );
		//adicCampo( txtSldLiqProd, 570, 180, 76, 20, "SldLiqProd", "Saldo liq.", ListaCampos.DB_SI, false );

		//adic( new JLabelPad( "Almoxarifado" ), 7, 200, 87, 20 );
		//adic( txtAlmox, 7, 220, 76, 20 );
		
		
		//adic( new JLabelPad( "Custo MPM" ), 175, 200, 87, 20 );
		//adic( txtCustoMPMAlmox, 175, 220, 76, 20 );
		//adic( new JLabelPad( "Custo PEPS" ), 254, 200, 87, 20 );
		//adic( txtCustoPEPSAlmox, 254, 220, 76, 20 );
		//adic( new JLabelPad( "Saldo" ), 333, 200, 87, 20 );
		//adic( txtSldAlmox, 333, 220, 76, 20 );
		//adic( new JLabelPad( "Saldo res." ), 412, 200, 87, 20 );
		//adic( txtSldResAlmox, 412, 220, 76, 20 );
		//adic( new JLabelPad( "Saldo consig." ), 491, 200, 87, 20 );
		//adic( txtSldConsigAlmox, 491, 220, 76, 20 );
		//adic( new JLabelPad( "Saldo liq." ), 570, 200, 87, 20 );
		//adic( txtSldLiqAlmox, 570, 220, 76, 20 );

		JPanelPad pnControles = new JPanelPad();
		pnControles.setBorder( SwingParams.getPanelLabel( "Controles", Color.BLUE ) );

		adic( pnControles, 7, 295, 650, 110 );

		separador_atividade.setBorder( BorderFactory.createEtchedBorder() );
		separador_exigencia.setBorder( BorderFactory.createEtchedBorder() );
		separador_adicional.setBorder( BorderFactory.createEtchedBorder() );
		separador_radios.setBorder( BorderFactory.createEtchedBorder() );
		separador_ultima_entrada.setBorder( BorderFactory.createEtchedBorder() );

		setPainel( pnControles );

		// Atividade

		adicDB( cbAtivo, 10, 15, 50, 20, "AtivoProd", "Ativo", true );
		adicDB( cbRMA, 7, 60, 50, 20, "RMAProd", "RMA", true );

		pnControles.adic( separador_atividade, 60, 6, 2, 75 );

		// Exigencias
		adicDB( cbLote, 70, 15, 58, 20, "CLoteProd", "Lote", true );
		adicDB( cbSerie, 140, 15, 60, 20, "SerieProd", "Nro. Série", true );

		adicDB( cbReceita, 70, 60, 60, 20, "UsaReceitaProd", "Receita", true );
		adicDB( cbGuiaTraf, 140, 60, 90, 20, "GuiaTrafProd", "Guia de tráfego", true );

		pnControles.adic( separador_exigencia, 240, 6, 2, 75 );

		// PDV
		adicDB( cbAdicPDV, 250, 20, 110, 20, "UsaTelaAdicPDV", "PDV", true );

		pnControles.adic( separador_radios, 390, 6, 2, 75 );

		// Radios

		adicDB( rgCV, 398, 15, 115, 65, "CVProd", "Cadastro para:", true );
		adicDB( rgAbaixCust, 520, 15, 115, 65, "VERIFPROD", "Abaixo custo:", true );

		JPanelPad pnClassificacao = new JPanelPad();
		pnClassificacao.setBorder( SwingParams.getPanelLabel( "Classificação", Color.BLUE ) );

		setPainel( pinGeral );
		adic( pnClassificacao, 7, 170, 650, 120 );
		setPainel( pnClassificacao );
		
		adicDB( rgTipoProd, 7, 0, 620, 90, "TipoProd", "Tipo:", true );
		
		
		setPainel( pinGeral );

		adicCampo( txtCodAlmox, 7, 430, 80, 20, "CodAlmox", "Cód. almox.", ListaCampos.DB_FK, true );
		adicDescFK( txtDescAlmox, 90, 430, 170, 20, "DescAlmox", "Descrição do almoxarifado" );

		adicCampo( txtCodGrup, 263, 430, 80, 20, "CodGrup", "Cód. grupo", ListaCampos.DB_FK, txtDescGrup, true );
		adicDescFK( txtDescGrup, 346, 430, 303, 20, "DescGrup", "Descrição do grupo" );

		adicCampo( txtCodMoeda, 7, 470, 80, 20, "CodMoeda", "Cód.moeda", ListaCampos.DB_FK, true );
		adicDescFK( txtDescMoeda, 90, 470, 170, 20, "SingMoeda", "Descrição da moeda" );
		
		adicCampo( txtCodFisc, 263, 470, 80, 20, "CodFisc", "Cód. fiscal", ListaCampos.DB_FK, txtDescFisc, true );
		adicDescFK( txtDescFisc, 346, 470, 303, 20, "DescFisc", "Descrição da classificação fiscal" );

		adicCampo( txtCodUnid, 7, 510, 80, 20, "CodUnid", "Cód. unidade", ListaCampos.DB_FK, txtDescUnid, true );
		adicDescFK( txtDescUnid, 90, 510, 170, 20, "DescUnid", "Descrição da unidade" );

		adicCampo( txtCodMarca, 263, 510, 80, 20, "CodMarca", "Cód. marca", ListaCampos.DB_FK, txtDescMarca, true );
		adicDescFK( txtDescMarca, 346, 510, 303, 20, "DescMarca", "Descrição da marca" );

//		adicCampo( txtPrazoEnt, 263, 500, 80, 20, "CodPE", "Cód.p/entrega", ListaCampos.DB_FK, txtDescGrup, false );
//		adicDescFK( txtDescPrazoEnt, 346, 500, 190, 20, "DescPE", "Descrição do prazo de entrega" );
//		adicDescFK( txtDias, 539, 500, 110, 20, "DiasPE", "Dias p/entrega" );

		// adic( btDuplicar, 540, 500, 110, 30 );

		// pnRod.add( btDuplicar );
		// btCopiar.setPreferredSize( new Dimension(30,30) );

		btCopiar.setPreferredSize( new Dimension( 38, 26 ) );

		pnImp.add( btCopiar );

		// Decrição completa

		adicTab( "Descrição completa", pnDesc );
		adicDBLiv( txaDescComp, "DescCompProd", "Descrição completa", false );
		pnDesc.add( spnDesc );

		//setListaCampos( true, "PRODUTO", "EQ" );
		
		// OBSERVACAO PRODUTO
		adicTab( "Observação Produto", pnObsProd );
		adicDBLiv( txaObsProd, "ObsProd", "Observação Produto", false );
		pnObsProd.add( spnObsProd );

		//setListaCampos( true, "PRODUTO", "EQ" );

		// Outros

		setPainel( pinOutros, pnOutros );
		adicTab( "Outros", pnOutros );
		
		JPanelPad pnMedidas = new JPanelPad();
		pnMedidas.setBorder( SwingParams.getPanelLabel( "Medidas", Color.BLUE ) );
		
		pinOutros.adic( pnMedidas, 5, 5, 650, 160 );
		setPainel( pnMedidas );
		
		adicCampo( txtPesoBrutProd, 	7, 		20, 	110, 20, "PesoBrutProd", "Peso bruto", ListaCampos.DB_SI, true );
		adicCampo( txtPesoLiqProd, 		120, 	20, 	110, 20, "PesoLiqProd", "Peso líquido", ListaCampos.DB_SI, true );

		adicCampo( txtVlrDens, 			233, 	20, 	110, 20, "VlrDensidade", "Densidade", ListaCampos.DB_SI, false );
		adicCampo( txtVlrConcent, 		346, 	20, 	110, 20, "VlrConcent", "Concentração", ListaCampos.DB_SI, false );

		adicCampo( txtVlrCompri, 		7, 		60, 	110, 20, "Comprimento", "Comprimento(cm)", ListaCampos.DB_SI, false );
		adicCampo( txtVlrLarg, 			120, 	60, 	110, 20, "Largura", "Largura(cm)", ListaCampos.DB_SI, false );
		adicCampo( txtVlrEspess, 		233, 	60, 	110, 20, "Espessura", "Espessura(cm)", ListaCampos.DB_SI, false );

		adicCampo( txtQtdEmbalagem, 	7, 		100, 	110, 20, "QtdEmbalagem", "Qtd. Embalagem", ListaCampos.DB_SI, false );
		adicCampo( txtCubagem, 			120, 	100, 	110, 20, "Cubagem", "Cubagem (m3)", ListaCampos.DB_SI, false );		

		
		JPanelPad pnOutros = new JPanelPad();
		pnOutros.setBorder( SwingParams.getPanelLabel( "Outros", Color.BLUE ) );

		pinOutros.adic( pnOutros, 5, 180, 650, 200 );
		setPainel( pnOutros );

		
		adicCampo( txtPrazoEnt, 5, 20, 80, 20, "CodPE", "Cód.p/entrega", ListaCampos.DB_FK, txtDescGrup, false );
		adicDescFK( txtDescPrazoEnt, 88, 20, 190, 20, "DescPE", "Descrição do prazo de entrega" );
		adicDescFK( txtDias, 281, 20, 110, 20, "DiasPE", "Dias p/entrega" );
		
		adicCampo( txtCodSecao, 5, 60, 80, 20, "CodSecao", "Cód. seção", ListaCampos.DB_FK, txtDescSecao, false );
		adicDescFK( txtDescSecao, 88, 60, 303, 20, "DescSecao", "Descrição da seção" );

		adicCampo( txtLocalProd, 5, 100, 165, 20, "LocalProd", "Local de armazenamento", ListaCampos.DB_SI, false );
		adicDB( cbDescCli, 5, 140, 200, 20, "desccli", "", false );
		
		// Certificação FSC
		
		
		JPanelPad pnFSC = new JPanelPad();
		pnFSC.setBorder( SwingParams.getPanelLabel( "Certificação FSC (Forest Stewardship Council)", Color.BLUE ) );

		pinOutros.adic( pnFSC, 5, 390, 650, 150 );
		setPainel( pnFSC );

		adicDB( 	cbCertFSC		, 5		, 20	, 150	, 20	, "certfsc"		, ""						 , false );
		adicCampo( 	txtNroPlanos	, 158	, 20	, 110	, 20	, "NroPlanos"	, "Nro. de Planos"				 , ListaCampos.DB_SI, false );
		adicCampo( 	txtQtdPorPlano	, 271	, 20	, 110	, 20	, "QtdPorPlano"	, "Qtd. por Plano"			 , ListaCampos.DB_SI, false );
		adicCampo( 	txtFatorFSC		, 384	, 20	, 150	, 20	, "FatorFSC"	, "Equivalência de Unidades" , ListaCampos.DB_SI, false );

		//setListaCampos( true, "PRODUTO", "EQ" );

		// Serviçox

		setPainel( pinServico, pnServico );
		adicTab( "Serviço", pnServico );

		adicCampo( txtCodTpChamado, 7, 20, 110, 20, "CodTpChamado", "Cód.Tp.Cham.", ListaCampos.DB_FK, false );
		adicDescFK( txtDescTpChamado, 120, 20, 250, 20, "DescTpChamado", "Descrição do tipo de chamado" );

		adicCampo( txtQtdHorasServ, 7, 60, 110, 20, "QtdHorasServ", "Qtd. Horas", ListaCampos.DB_SI, false );
		
		setListaCampos( true, "PRODUTO", "EQ" );

		// Preço

		setPainel( pinRodPreco, pnPreco );
		adicTab( "Preços", pnPreco );
		setListaCampos( lcPreco );
		setNavegador( navPreco );
		
		pnPreco.add( pinRodPreco, BorderLayout.SOUTH );
		pnPreco.add( spnPreco, BorderLayout.CENTER );

		pinRodPreco.adic( navPreco, 0, 90, 270, 25 );

		lcClasCliPreco.add( new GuardaCampo( txtCodClasCliPreco, "CodClasCli", "Cód.c.cli.", ListaCampos.DB_PK, false ) );
		lcClasCliPreco.add( new GuardaCampo( txtDescClasCliPreco, "DescClasCli", "Descrição da classificação do cliente", ListaCampos.DB_SI, false ) );
		lcClasCliPreco.montaSql( false, "CLASCLI", "VD" );
		lcClasCliPreco.setQueryCommit( false );
		lcClasCliPreco.setReadOnly( true );
		txtDescClasCliPreco.setListaCampos( lcClasCliPreco );
		txtCodClasCliPreco.setTabelaExterna( lcClasCliPreco, FClasCli.class.getCanonicalName() );

		lcTabPreco.add( new GuardaCampo( txtCodTabPreco, "CodTab", "Cód.tab.pç.", ListaCampos.DB_PK, true ) );
		lcTabPreco.add( new GuardaCampo( txtDescTabPreco, "DescTab", "Descrição da tabela de preço", ListaCampos.DB_SI, false ) );
		lcTabPreco.montaSql( false, "TABPRECO", "VD" );
		lcTabPreco.setReadOnly( true );
		lcTabPreco.setQueryCommit( false );
		txtDescTabPreco.setListaCampos( lcTabPreco );
		txtCodTabPreco.setTabelaExterna( lcTabPreco, FTabPreco.class.getCanonicalName() );

		lcPlanoPagPreco.add( new GuardaCampo( txtCodPlanoPagPreco, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_PK, false ) );
		lcPlanoPagPreco.add( new GuardaCampo( txtDescPlanoPagPreco, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPagPreco.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPagPreco.setReadOnly( true );
		lcPlanoPagPreco.setQueryCommit( false );
		txtDescPlanoPagPreco.setListaCampos( lcPlanoPagPreco );
		txtCodPlanoPagPreco.setTabelaExterna( lcPlanoPagPreco, FPlanoPag.class.getCanonicalName() );

		adicCampo( txtCodPrecoProd, 7, 20, 80, 20, "CodPrecoProd", "Cód.pç.prod.", ListaCampos.DB_PK, true );
		adicCampo( txtCodClasCliPreco, 90, 20, 67, 20, "CodClasCli", "Cód.c.cli.", ListaCampos.DB_FK, txtDescClasCliPreco, false );
		adicDescFK( txtDescClasCliPreco, 160, 20, 217, 20, "DescClasCli", "Descrição da classificação do cliente" );
		adicCampo( txtCodTabPreco, 380, 20, 77, 20, "CodTab", "Cód.tab.pc.", ListaCampos.DB_FK, txtDescTabPreco, true );
		adicDescFK( txtDescTabPreco, 460, 20, 190, 20, "DescTab", "Descrição da tab. de preços" );
		adicCampo( txtCodPlanoPagPreco, 7, 60, 80, 20, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_FK, txtDescPlanoPagPreco, false );
		adicDescFK( txtDescPlanoPagPreco, 90, 60, 197, 20, "DescPlanoPag", "Descrição do plano de pagamento" );
		adicCampo( txtPrecoProd, 290, 60, 110, 20, "PrecoProd", "Preço", ListaCampos.DB_SI, true );
		adicCampoInvisivel( txtTipoPrecoProd,"TipoPrecoProd", "TipoPrecoProd", ListaCampos.DB_SI, false );
		adicDB( cbAtivoPrecoProd,403, 60, 200, 20, "AtivoPrecoProd", "", true);
/*		adicCampoInvisivel( txtIdUsuInsPreco, "IDUsuIns", "ID.Usu.Ins", ListaCampos.DB_SI, true);
		adicCampoInvisivel( txtDtInsPreco, "DtIns", "Dt.Ins.", ListaCampos.DB_SI, true);
		adicCampoInvisivel( txtHInsPreco, "HIns", "H.Ins", ListaCampos.DB_SI, true);
		adicCampoInvisivel( txtIdUsuAltPreco, "IDUsuAlt", "ID.Usu.Alt", ListaCampos.DB_SI, true);
		adicCampoInvisivel( txtDtAltPreco, "DtAlt", "Dt.Alt", ListaCampos.DB_SI, true);
		adicCampoInvisivel( txtHAltPreco, "HAlt", "H.Alt", ListaCampos.DB_SI, true);*/
		setListaCampos( true, "PRECOPROD", "VD" );
		lcPreco.setOrdem( "CodPrecoProd" );
		lcPreco.setQueryInsert( false );
		lcPreco.setQueryCommit( false );
		lcPreco.montaTab();
		tabPreco.setTamColuna( 65, 0 );
		tabPreco.setTamColuna( 60, 1 );
		tabPreco.setTamColuna( 110, 2 );
		tabPreco.setTamColuna( 60, 3 );
		tabPreco.setTamColuna( 110, 4 );
		tabPreco.setTamColuna( 60, 5 );
		tabPreco.setTamColuna( 110, 6 );
		tabPreco.setTamColuna( 75, 7 );
		tabPreco.setTamColuna( 30, 8 );
		tabPreco.setTamColuna( 30, 9 );
		//Campos adicionais para Log de inserção e alteração.

		
		/* Fatores de conversão */

		setPainel( pinRodFatConv, pnFatConv );
		adicTab( "Fatores de conversão", pnFatConv );
		setListaCampos( lcFatConv );

		setNavegador( navFatConv );
		pnFatConv.add( pinRodFatConv, BorderLayout.SOUTH );
		pnFatConv.add( spnFatConv, BorderLayout.CENTER );

		JPanelPad pnnav = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );
		pnnav.add( navFatConv, BorderLayout.WEST );
		pnnav.setBorder( BorderFactory.createEtchedBorder() );

		pinRodFatConv.adic( pnnav, 0, 86, 688, 30 );

		lcUnidFat.add( new GuardaCampo( txtUnidFat, "CodUnid", "Cód.unid.", ListaCampos.DB_PK, true ) );
		lcUnidFat.add( new GuardaCampo( txtDescUnidFat, "DescUnid", "Descrição da unidade", ListaCampos.DB_SI, false ) );
		lcUnidFat.montaSql( false, "UNIDADE", "EQ" );
		lcUnidFat.setReadOnly( true );
		lcUnidFat.setQueryCommit( false );
		txtDescUnidFat.setListaCampos( lcUnidFat );
		txtUnidFat.setTabelaExterna( lcUnidFat, FUnidade.class.getCanonicalName() );

		JLabelPad sepconv = new JLabelPad();
		sepconv.setBorder( BorderFactory.createEtchedBorder() );
		adic( sepconv, 128, 4, 2, 80 );

		lbUnidFat = adicCampo( txtUnidFat, 140, 20, 80, 20, "CodUnid", "Cód.unidade", ListaCampos.DB_PF, txtDescUnidFat, true );
		lbDescUnidFat = adicDescFK( txtDescUnidFat, 223, 20, 250, 20, "DescUnid", "Descrição da unidade" );
		lbFatConv = adicCampo( txtFatConv, 140, 60, 80, 20, "FatConv", "Fator", ListaCampos.DB_SI, false );
		lbCpFatConv = adicDB( cbCpFatConv, 476, 20, 120, 20, "CpFatConv", "Padrão p/compra", true );

		adicDB( rgTipoConv, 7, 20, 110, 60, "tipoconv", "Tipo de conversão", true );

		lbCodProdEst = adicCampo( txtCodProdEst, 140, 60, 80, 20, "CodProdEt", "Cód.Prod.", ListaCampos.DB_FK, txtDescProdEst, false );
		lbSeqEst = adicCampo( txtSeqEst, 223, 60, 80, 20, "SeqEst", "Seq.Est.", ListaCampos.DB_FK, txtDescProdEst, false );
		lbDescProdEst = adicDescFK( txtDescProdEst, 306, 60, 270, 20, "DescEst", "Descrição da estrutura" );

		rgTipoConv.setVlrString( "U" );

		setListaCampos( false, "FATCONV", "EQ" );

		lcFatConv.montaTab();
		lcFatConv.setQueryInsert( false );
		lcFatConv.setQueryCommit( false );

		tabFatConv.setTamColuna( 60, 1 );
		tabFatConv.setTamColuna( 50, 5 );
		tabFatConv.setTamColuna( 240, 6 );

		// *****************************************************************************/

		// Planejamento
		// lcPlan.setUsaME(false);
		lcPlan.add( new GuardaCampo( txtCodPlan, "CodPlan", "Cód.plan.", ListaCampos.DB_PK, true ) );
		lcPlan.add( new GuardaCampo( txtDescPlan, "DescPlan", "Descrição do planejamento", ListaCampos.DB_SI, false ) );
		lcPlan.montaSql( false, "PLANEJAMENTO", "FN" );
		lcPlan.setReadOnly( true );
		lcPlan.setQueryCommit( false );
		txtDescPlan.setListaCampos( lcPlan );
		txtCodPlan.setTabelaExterna( lcPlan, FPlanejamento.class.getCanonicalName() );

		// lcCC.setUsaME(false);
		lcCC.add( new GuardaCampo( txtAnoCC, "AnoCC", "Ano.cc.", ListaCampos.DB_PK, true ) );
		lcCC.add( new GuardaCampo( txtCodCC, "CodCC", "Cód.cc.", ListaCampos.DB_PK, true ) );
		lcCC.add( new GuardaCampo( txtDescCC, "DescCC", "Descrição do centro de custo", ListaCampos.DB_SI, false ) );

		lcCC.montaSql( false, "CC", "FN" );
		lcCC.setReadOnly( true );
		lcCC.setQueryCommit( false );
		txtDescCC.setListaCampos( lcCC );
		txtAnoCC.setTabelaExterna( lcCC, FCentroCusto.class.getCanonicalName() );
		txtCodCC.setTabelaExterna( lcCC, FCentroCusto.class.getCanonicalName() );

		// CC Acesso
		lcCCAcesso.add( new GuardaCampo( txtAnoCCPA, "AnoCC", "Ano.cc.", ListaCampos.DB_PK, true ) );
		lcCCAcesso.add( new GuardaCampo( txtCodCCPA, "CodCC", "Cód.cc.", ListaCampos.DB_PK, true ) );
		lcCCAcesso.add( new GuardaCampo( txtDescCCPA, "DescCC", "Descrição do C.C", ListaCampos.DB_SI, false ) );
		lcCCAcesso.montaSql( false, "CC", "FN" );
		lcCCAcesso.setReadOnly( true );
		lcCCAcesso.setQueryCommit( false );
		txtDescCCPA.setListaCampos( lcCCAcesso );
		txtAnoCCPA.setTabelaExterna( lcCCAcesso, FCentroCusto.class.getCanonicalName() );
		txtCodCCPA.setTabelaExterna( lcCCAcesso, FCentroCusto.class.getCanonicalName() );

		// Caixa
		lcCaixa.add( new GuardaCampo( txtCodCaixa, "CodCaixa", "Cód.caixa", ListaCampos.DB_PK, false ) );
		lcCaixa.add( new GuardaCampo( txtDescCaixa, "DescCaixa", "Descrição do Caixa", ListaCampos.DB_SI, false ) );
		lcCaixa.montaSql( false, "CAIXA", "PV" );
		lcCaixa.setReadOnly( true );
		lcCaixa.setQueryCommit( false );
		txtDescCaixa.setListaCampos( lcCaixa );
		txtCodCaixa.setTabelaExterna( lcCaixa, FCaixa.class.getCanonicalName() );

		setPainel( pinRodProdPlan, pnProdPlan );
		adicTab( "Planejamento", pnProdPlan );

		setListaCampos( lcProdPlan );
		setNavegador( navProdPlan );

		pnProdPlan.add( pinRodProdPlan, BorderLayout.SOUTH );
		pnProdPlan.add( spnPlan, BorderLayout.CENTER );

		pinRodProdPlan.adic( navProdPlan, 0, 90, 270, 25 );

		adicCampo( txtSeqPP, 7, 20, 80, 20, "SeqPP", "N.seq.", ListaCampos.DB_PK, true );
		adicCampo( txtCodPlan, 90, 20, 80, 20, "CodPlan", "Cód.plan.", ListaCampos.DB_PF, txtDescPlan, true );
		adicDescFK( txtDescPlan, 173, 20, 250, 20, "DescPlan", "Descrição do planejamento" );
		adicDB( rgTipoPP, 426, 20, 200, 30, "TipoPP", "Tipo", true );

		adicCampo( txtAnoCC, 7, 60, 80, 20, "AnoCC", "Ano.cc.", ListaCampos.DB_PF, txtDescCC, true );
		adicCampo( txtCodCC, 90, 60, 107, 20, "CodCC", "Cód.cc.", ListaCampos.DB_PF, true );
		adicDescFK( txtDescCC, 200, 60, 250, 20, "DescCC", "Descrição do centro de custo" );

		setListaCampos( true, "PRODPLAN", "EQ" );
		lcProdPlan.setOrdem( "SeqPP" );
		lcProdPlan.montaTab();
		lcProdPlan.setQueryCommit( false );
		tabProdPlan.setTamColuna( 50, 0 );
		tabProdPlan.setTamColuna( 100, 1 );
		tabProdPlan.setTamColuna( 250, 2 );
		tabProdPlan.setTamColuna( 50, 3 );
		tabProdPlan.setTamColuna( 50, 4 );
		tabProdPlan.setTamColuna( 100, 5 );
		tabProdPlan.setTamColuna( 250, 6 );

		// Fornecedor
		setPainel( pinRodFor, pnFor );
		adicTab( "Fornecedores", pnFor );
		setListaCampos( lcProdFor );

		navFor.setAtivo( 6, false );

		setNavegador( navFor );
		pnFor.add( pinRodFor, BorderLayout.SOUTH );
		pnFor.add( spnFor, BorderLayout.CENTER );

		pinRodFor.adic( navFor, 0, 50, 270, 25 );

		//lcForFK.setUsaME( false );
		lcForFK.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.for.", ListaCampos.DB_PK, true ) );
		lcForFK.add( new GuardaCampo( txtDescFor, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );
		lcForFK.montaSql( false, "FORNECED", "CP" );
		lcForFK.setReadOnly( true );
		lcForFK.setQueryCommit( false );
		txtCodFor.setListaCampos( lcForFK );
		txtCodFor.setTabelaExterna( lcForFK, FFornecedor.class.getCanonicalName() );

		adicCampo( txtCodFor, 7, 20, 80, 20, "CodFor", "Cód.for.", ListaCampos.DB_PF, txtDescFor, true );
		adicDescFK( txtDescFor, 90, 20, 300, 20, "RazFor", "Razão social do fornecedor" );
		adicCampo( txtRefProdFor, 400, 20, 105, 20, "RefProdFor", "Cód.prod.for.", ListaCampos.DB_SI, false );
		setListaCampos( false, "PRODFOR", "CP" );
		lcProdFor.montaTab();
		lcProdFor.setQueryInsert( false );
		lcProdFor.setQueryCommit( false );
		tabFor.setTamColuna( 250, 1 );

		// Lote
		setPainel( pinRodLote, pnLote );
		// adicTab( "Lotes", pnLote );
		setListaCampos( lcLote );
		setNavegador( navLote );
		pnLote.add( pinRodLote, BorderLayout.SOUTH );
		pnLote.add( spnLote, BorderLayout.CENTER );

		pinRodLote.adic( navLote, 0, 90, 270, 25 );

		txtSldLote.setSoLeitura( true );
		txtSldResLote.setSoLeitura( true );
		txtSldConsigLote.setSoLeitura( true );
		txtSldLiqLote.setSoLeitura( true );

		adicCampo( txtCodLote, 7, 20, 110, 20, "CodLote", "Cód.lote", ListaCampos.DB_PK, true );
		adicCampo( txtDiniLote, 120, 20, 100, 20, "DIniLote", "Data inicial", ListaCampos.DB_SI, false );
		adicCampo( txtVenctoLote, 223, 20, 100, 20, "VenctoLote", "Vencimento", ListaCampos.DB_SI, true );
		adicCampo( txtQtdProdLote, 326, 20, 100, 20, "QtdProdLote", "Qtd.Prod.Lote", ListaCampos.DB_SI, false );
		adicCampo( txtDiasAvisoLote, 429, 20, 100, 20, "DiasAvisoLote", "Dias para aviso", ListaCampos.DB_SI, true );
		adicCampo( txtSldLote, 7, 60, 80, 20, "SldLote", "Saldo", ListaCampos.DB_SI, false );
		adicCampo( txtSldResLote, 90, 60, 80, 20, "SldResLote", "Saldo res.", ListaCampos.DB_SI, false );
		adicCampo( txtSldConsigLote, 173, 60, 80, 20, "SldConsigLote", "Saldo consig.", ListaCampos.DB_SI, false );
		adicCampo( txtSldLiqLote, 256, 60, 80, 20, "SldLiqLote", "Saldo liq.", ListaCampos.DB_SI, false );

		setListaCampos( false, "LOTE", "EQ" );
		lcLote.setOrdem( "VenctoLote desc" );
		lcLote.setQueryInsert( false );
		lcLote.setQueryCommit( false );
		lcLote.montaTab();
		lcLote.setDinWhereAdic( "CODLOTE = #N", txtCodProd );
		tabLote.setTamColuna( 110, 0 );
		tabLote.setTamColuna( 100, 1 );
		tabLote.setTamColuna( 100, 2 );

		// Seriex
		setPainel( pinRodSerie, pnSerie );
		adicTab( "Séries", pnSerie );
		setListaCampos( lcSerie );
		setNavegador( navSerie );
		pnSerie.add( pinRodSerie, BorderLayout.SOUTH );
		pnSerie.add( spnSerie, BorderLayout.CENTER );

		pinRodSerie.adic( navSerie, 0, 135, 270, 25 );

		adicCampo( txtNumSerie, 7, 20, 250, 20, "NumSerie", "Num.Série", ListaCampos.DB_PK, true );
		adicCampo( txtDtFabricSerie, 260, 20, 100, 20, "DtFabricSerie", "Data fabricação", ListaCampos.DB_SI, false );
		adicCampo( txtDtValidSerie, 363, 20, 100, 20, "DtValidSerie", "Data de validade", ListaCampos.DB_SI, false );
		JLabelPad lbObsSerie = adicDBLiv( txaObsSerie, "ObsSerie", "Observações", false );
		adic( lbObsSerie, 7, 40, 200, 20 );
		adic( spnObsSerie, 7, 60, 458, 60 );

		setListaCampos( false, "SERIE", "EQ" );
		lcSerie.setOrdem( "DtValidSerie desc" );
		lcSerie.setQueryInsert( false );
		lcSerie.setQueryCommit( false );
		lcSerie.montaTab();
		lcSerie.setDinWhereAdic( "CODPROD = #N", txtCodProd );
		tabSerie.setTamColuna( 200, 0 );
		tabSerie.setTamColuna( 100, 1 );
		tabSerie.setTamColuna( 100, 2 );
		tabSerie.setTamColuna( 250, 3 );

		// Codigo alternativo

		setPainel( pinRodCodAltProd, pnCodAltProd );
		adicTab( "Cód.altern.", pnCodAltProd );
		setListaCampos( lcCodAltProd );
		setNavegador( navCodAltProd );
		pnCodAltProd.add( pinRodCodAltProd, BorderLayout.SOUTH );
		pnCodAltProd.add( spnCodAltProd, BorderLayout.CENTER );
		pinRodCodAltProd.adic( navCodAltProd, 0, 50, 270, 25 );
		navCodAltProd.setAtivo( 6, false );

		adicCampo( txtCodAltProd, 7, 20, 150, 20, "CodAltProd", "Código alternativo", ListaCampos.DB_PK, null, true );
		setListaCampos( false, "CODALTPROD", "EQ" );
		lcCodAltProd.setQueryInsert( false );
		lcCodAltProd.setQueryCommit( false );

		txtCodAltProd.setTabelaExterna( lcCodAltProd, null );
		txtCodAltProd.setEnterSai( false );
		lcCodAltProd.montaTab();
		tabCodAltProd.setTamColuna( 150, 0 );

		// Fotos

		setPainel( pinRodFoto, pnFoto );
		adicTab( "Fotos", pnFoto );
		setListaCampos( lcFoto );
		setNavegador( navFoto );
		pnFoto.add( pinRodFoto, BorderLayout.SOUTH );
		pnFoto.add( spnFoto, BorderLayout.CENTER );

		pinRodFoto.adic( navFoto, 0, 140, 270, 25 );

		txtAltFotoProd.setEnabled( false );
		txtLargFotoProd.setEnabled( false );

		adicCampo( txtCodFotoProd, 7, 20, 70, 20, "CodFotoProd", "Nº foto", ListaCampos.DB_PK, true );
		adicCampo( txtDescFotoProd, 80, 20, 250, 20, "DescFotoProd", "Descrição da foto", ListaCampos.DB_SI, true );
		adicDB( rgTF, 7, 60, 323, 30, "TipoFotoProd", "Tamanho:", true );
		adicCampo( txtLargFotoProd, 7, 110, 80, 20, "LargFotoProd", "Largura", ListaCampos.DB_SI, true );
		adicCampo( txtAltFotoProd, 90, 110, 77, 20, "AltFotoProd", "Altura", ListaCampos.DB_SI, true );
		adicDB( imFotoProd, 350, 20, 150, 140, "FotoProd", "Foto: (máx. 63K)", true );

		setListaCampos( true, "FOTOPROD", "VD" );
		lcFoto.setOrdem( "CodFotoProd" );
		lcFoto.setQueryInsert( false );
		lcFoto.setQueryCommit( false );
		lcFoto.montaTab();
		tabFoto.setTamColuna( 80, 0 );
		tabFoto.setTamColuna( 250, 1 );
		tabFoto.setTamColuna( 80, 2 );
		tabFoto.setTamColuna( 80, 3 );
		tabFoto.setTamColuna( 80, 4 );

		// Acesso

		setPainel( pinRodCodAcess, pnCodAcess );
		adicTab( "Acesso", pnCodAcess );
		setListaCampos( lcProdAcesso );
		setNavegador( navCodAcess );
		pnCodAcess.add( pinRodCodAcess, BorderLayout.SOUTH );
		pnCodAcess.add( spnCodAcess, BorderLayout.CENTER );
		pinRodCodAcess.adic( navCodAcess, 0, 90, 270, 25 );
		navCodAcess.setAtivo( 6, false );

		adicCampo( txtCodPA, 7, 20, 70, 20, "CodPA", "Cód.acess.", ListaCampos.DB_PK, null, true );
		adicDB( rgPA, 80, 20, 140, 30, "TipoPA", "Tipo", true );

		adicCampo( txtAnoCCPA, 223, 20, 80, 20, "AnoCC", "Ano CC.", ListaCampos.DB_FK, txtDescCCPA, false );
		adicCampo( txtCodCCPA, 306, 20, 150, 20, "CodCC", "Cód. CC.", ListaCampos.DB_FK, false );
		adicDescFK( txtDescCCPA, 459, 20, 180, 20, "DescCC", "Descrição do C.C." );

		adicCampo( txtCodCaixa, 223, 60, 80, 20, "CodCaixa", "Cód.caixa", ListaCampos.DB_FK, txtDescCaixa, false );
		adicDescFK( txtDescCaixa, 306, 60, 250, 20, "DescCaixa", "Descrição do caixa" );
		setListaCampos( true, "PRODACESSO", "EQ" );
		lcProdAcesso.setQueryInsert( false );
		lcProdAcesso.setQueryCommit( false );

		txtCodPA.setTabelaExterna( lcProdAcesso, null );
		txtCodPA.setEnterSai( false );
		lcProdAcesso.montaTab();
		tabCodAcess.setTamColuna( 90, 0 );
		tabCodAcess.setTamColuna( 50, 1 );
		tabCodAcess.setTamColuna( 70, 2 );
		tabCodAcess.setTamColuna( 120, 3 );
		tabCodAcess.setTamColuna( 80, 4 );
		
		
		setPainel( pinMGProduto, pnMGProduto );
		adicTab( "Magento", pnMGProduto );
		setListaCampos( lcMGProduto );
		setNavegador( navMGProduto );
		
		pnMGProduto.add( pinMGProduto, BorderLayout.SOUTH );
		navMGProduto.setAtivo( 0, false );
		navMGProduto.setAtivo( 1, false );

		adicDB(  txaMGDescProd, 7, 20, 600, 80, "DESCPROD", "Descrição do produto", ListaCampos.DB_SI, true );
		adicDB(  txaMGDescProdCompl, 7, 120, 600, 250, "DESCPRODCOMPL", "Descrição do produto completa", ListaCampos.DB_SI, false );
		pinMGProduto.adic( navMGProduto, 0, 500, 270, 25 );
		
		setListaCampos( false, "PRODUTO", "MG" );
		lcMGProduto.setMensInserir( false );
		lcMGProduto.setQueryCommit( false );
		
		txtCodProd.requestFocus();
		btCopiar.addActionListener( this );
		
		if(sPrefs!=null) {
			txtDescProd.iTamanho = Integer.parseInt( sPrefs[eprefs.TAMDESCPROD.ordinal()] );
		}		
	}

	private void buscaEstoque() {

		ResultSet rs = null;
		String sWhere = "";
		String sSQL = "";
		int iCodAlmox = 0;
		int iParam = 1;

		String sCodProd = null;
		String sFiltro = "";

		try {
			sCodProd = txtCodProd.getVlrString().trim();
			iCodAlmox = txtCodAlmox.getVlrInteger().intValue();

			if ( sCodProd.equals( "" ) ) {
				Funcoes.mensagemInforma( this, "Selecione um produto!" );
				txtCodProd.requestFocus();
				return;
			}

			sFiltro = "P.CODPROD=" + sCodProd;

			if ( iCodAlmox == 0 ) {
				sWhere = "SP.CODEMPAX = P.CODEMPAX AND SP.CODFILIALAX=P.CODFILIALAX AND " + "SP.CODALMOX = P.CODALMOX";
			}
			else {
				sWhere = "SP.CODEMPAX = ? AND SP.CODFILIALAX=? AND SP.CODALMOX = ?";
			}

			sSQL = "SELECT P.CODPROD,P.DESCPROD,P.SLDPROD, P.SLDRESPROD, " 
				+ "P.SLDCONSIGPROD,P.SLDLIQPROD,SP.SLDPROD SLDPRODAX, SP.SLDRESPROD SLDRESPRODAX, " 
				+ "SP.SLDCONSIGPROD SLDCONSIGPRODAX,SP.SLDLIQPROD SLDLIQPRODAX " 
				+ "FROM EQPRODUTO P, EQSALDOPROD SP "
				+ "WHERE SP.CODEMP=P.CODEMP AND SP.CODFILIAL=P.CODFILIAL AND SP.CODPROD = P.CODPROD AND " 
				+ "P.CODEMPGP=? AND P.CODFILIALGP=? AND " 
				+ sFiltro + " AND " + sWhere + " ORDER BY P.DESCPROD ";

			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( iParam++, Aplicativo.iCodEmp );
			ps.setInt( iParam++, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			if ( iCodAlmox != 0 ) {
				ps.setInt( iParam++, Aplicativo.iCodEmp );
				ps.setInt( iParam++, ListaCampos.getMasterFilial( "EQALMOX" ) );
				ps.setInt( iParam++, iCodAlmox );
			}
			rs = ps.executeQuery();

			if ( rs.next() ) {
				txtSldAlmox.setVlrBigDecimal( new BigDecimal( rs.getBigDecimal( iCodAlmox != 0 ? "SLDPRODAX" : "SLDPROD" ) + "" ) );
				//txtSldResAlmox.setVlrDouble( new Double( rs.getDouble( iCodAlmox != 0 ? "SLDRESPRODAX" : "SLDRESPROD" ) + "" ) );
				//txtSldConsigAlmox.setVlrDouble( new Double( rs.getDouble( iCodAlmox != 0 ? "SLDCONSIGPRODAX" : "SLDCONSIGPROD" ) + "" ) );
				//txtSldLiqAlmox.setVlrDouble( new Double( rs.getDouble( iCodAlmox != 0 ? "SLDLIQPRODAX" : "SLDLIQPROD" ) + "" ) );
			}
			else {
				txtSldAlmox.setVlrBigDecimal( new BigDecimal( 0 ) );
				//txtSldResAlmox.setVlrDouble( new Double( 0 ) );
				//txtSldConsigAlmox.setVlrDouble( new Double( 0 ) );
				//txtSldLiqAlmox.setVlrDouble( new Double( 0 ) );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar saldos por almoxarifado!\n" + err.getMessage() );
		} finally {
			sSQL = null;
		}

	}
	
	private void buscaUltimaEntrada() {

		ResultSet rs = null;
		String sWhere = "";
		StringBuilder sql = new StringBuilder();

		int iParam = 1;

		try {
			
			sql.append( "select first 1 mp.dtmovprod from eqmovprod mp " );
			sql.append( "where mp.tipomovprod='E' ");
			sql.append( "and codemppd=? and codfilialpd=? and codprod=? ");
			sql.append( "and codemp=? and codfilial=? ");
			sql.append( "order by mp.dtmovprod desc, mp.codmovprod desc ");

			PreparedStatement ps = con.prepareStatement( sql.toString() );

			ps.setInt( iParam++, Aplicativo.iCodEmp );
			ps.setInt( iParam++, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			ps.setInt( iParam++, txtCodProd.getVlrInteger() );
			ps.setInt( iParam++, Aplicativo.iCodEmp );
			ps.setInt( iParam++, ListaCampos.getMasterFilial( "EQMOVPROD" ) );
			
			rs = ps.executeQuery();

			if ( rs.next() ) {
				txtDtUltEntrada.setVlrDate( rs.getDate( "dtmovprod" ) );
			}
			else {
				txtDtUltEntrada.setVlrString( "" );
			}

			rs.close();
			ps.close();

			con.commit();

		} 
		catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar data da ultima entrada!\n" + err.getMessage() );
		} 
		finally {
			sql = null;
		}

	}

	private void duplicar() {

		if ( txtCodProd.getVlrInteger().intValue() == 0 || lcCampos.getStatus() != ListaCampos.LCS_SELECT ) {
			Funcoes.mensagemInforma( this, "Selecione um produto!" );
			return;
		}

		try {

			String sSQL = "SELECT ICOD FROM EQCOPIAPROD(?,?,?)";

			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, txtCodProd.getVlrInteger().intValue() );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, lcCampos.getCodFilial() );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				if ( Funcoes.mensagemConfirma( this, "Produto '" + rs.getInt( 1 ) + "' criado com sucesso!\n" + "Gostaria de edita-lo agora?" ) == JOptionPane.OK_OPTION ) {
					txtCodProd.setVlrInteger( new Integer( rs.getInt( 1 ) ) );
					lcCampos.carregaDados();
				}
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao duplicar o produto!\n" + err.getMessage() );
			err.printStackTrace();
		}

	}

	private String[] getPrefs() {

		String sRetorno[] = new String[ 10 ];
		String sSQL = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			sSQL = "SELECT P.CODMOEDA, P.PEPSPROD, P.TIPOCODBAR, E.CODEANEMP, PA.CODEANPAIS, P.TAMDESCPROD, " 
				 + "P.CVPROD, P.VERIFPROD, P.RMAPROD, P.TIPOPROD "	
				 + "FROM SGPREFERE1 P, SGEMPRESA E, SGFILIAL F left outer join SGPAIS PA "
				 + "on pa.codpais=f.codpais "				 
				 + "WHERE P.CODEMP=? AND P.CODFILIAL=? AND E.CODEMP=P.CODEMP AND " 
				 + "F.CODEMP=E.CODEMP AND F.CODFILIAL=?";

			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			ps.setInt( 3, Aplicativo.iCodFilial );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				sRetorno[ eprefs.CODMOEDA.ordinal() ] = rs.getString( "CODMOEDA" );
				sRetorno[ eprefs.PEPSPROD.ordinal() ] = rs.getString( "PEPSPROD" );
				sRetorno[ eprefs.TIPOCODBAR.ordinal() ] = rs.getString( "TIPOCODBAR" );
				sRetorno[ eprefs.CODEANEMP.ordinal() ] = rs.getString( "CODEANEMP" );
				sRetorno[ eprefs.CODPAISEMP.ordinal() ] = rs.getString( "CODEANPAIS" );
				sRetorno[ eprefs.TAMDESCPROD.ordinal() ] = rs.getString( "TAMDESCPROD" );
				sRetorno[ eprefs.CVPROD.ordinal() ] = rs.getString( "CVPROD" );
				sRetorno[ eprefs.VERIFPROD.ordinal() ] = rs.getString( "VERIFPROD" );
				sRetorno[ eprefs.RMAPROD.ordinal() ] = rs.getString( "RMAPROD" );
				sRetorno[ eprefs.TIPOPROD.ordinal() ] = rs.getString( "TIPOPROD" );
			}

			rs.close();
			ps.close();

			con.commit();
			
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage() );
			err.printStackTrace();
		} finally {
			rs = null;
			ps = null;
			sSQL = null;
		}

		return sRetorno;
	}
	
	private void setCadastroPadrao(){
		cbRMA.setVlrString( sPrefs[eprefs.RMAPROD.ordinal()] );
		
		if( sPrefs[eprefs.TIPOPROD.ordinal()] != null){
			rgTipoProd.setVlrString( sPrefs[eprefs.TIPOPROD.ordinal()] );
			rgCV.setVlrString( sPrefs[eprefs.CVPROD.ordinal()] );
			rgAbaixCust.setVlrString( sPrefs[eprefs.VERIFPROD.ordinal()] );
		}
	}
	
	private void carregaMoeda() {

		if ( sPrefs != null ) {
			txtCodMoeda.setVlrString( sPrefs[ 0 ] );
		}
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();
		StringBuffer sWhere = new StringBuffer();
		String[] sValores;
		ImprimeOS imp = new ImprimeOS( "", con );
		FAndamento And = null;
		int linPag = imp.verifLinPag() - 1;
		int iContaReg = 0;

		DLRProduto dl = new DLRProduto( con );
		dl.setVisible( true );
		if ( dl.OK == false ) {
			dl.dispose();
			return;
		}
		sValores = dl.getValores();
		dl.dispose();

		if ( sValores[ 1 ].trim().length() > 0 ) {
			sWhere.append( "DESCPROD >= '" + sValores[ 1 ] + "'" );
			imp.addSubTitulo( "PRODUTOS MAIORES QUE " + sValores[ 1 ].trim() );
		}
		if ( sValores[ 2 ].trim().length() > 0 ) {
			sWhere.append( sWhere.length() > 0 ? " AND " : "" );
			sWhere.append( "DESCPROD <= '" + sValores[ 2 ] + "'" );
			imp.addSubTitulo( "PRODUTOS MENORES QUE " + sValores[ 2 ].trim() );
		}
		if ( sValores[ 11 ].trim().length() > 0 ) {
			sWhere.append( sWhere.length() > 0 ? " AND " : "" );
			sWhere.append( "CODPROD >= '" + sValores[ 11 ] + "'" );
			imp.addSubTitulo( "PRODUTOS MAIORES QUE " + sValores[ 11 ].trim() );
		}
		if ( sValores[ 12 ].trim().length() > 0 ) {
			sWhere.append( sWhere.length() > 0 ? " AND " : "" );
			sWhere.append( "CODPROD <= '" + sValores[ 12 ] + "'" );
			imp.addSubTitulo( "PRODUTOS MENORES QUE " + sValores[ 12 ].trim() );
		}
		if ( sValores[ 3 ].equals( "S" ) ) {
			sWhere.append( sWhere.length() > 0 ? " AND " : "" );
			sWhere.append( "ATIVOPROD='S'" );
			imp.addSubTitulo( "PRODUTOS ATIVOS" );
		}
		if ( sValores[ 4 ].length() > 0 ) {
			sWhere.append( sWhere.length() > 0 ? " AND " : "" );
			sWhere.append( "CODPROD IN (SELECT CODPROD FROM CPPRODFOR WHERE CODFOR = " + sValores[ 4 ] + ")" );
			imp.addSubTitulo( "FORNECEDOR = " + sValores[ 4 ].trim() );
		}
		if ( sValores[ 7 ].length() > 0 ) {
			sWhere.append( sWhere.length() > 0 ? " AND " : "" );
			sWhere.append( "CODALMOX = " + sValores[ 7 ] );
			imp.addSubTitulo( "ALMOXARIFADO = " + sValores[ 8 ] );
		}

		if ( sValores[ 9 ].length() > 0 ) {
			sWhere.append( sWhere.length() > 0 ? " AND " : "" );
			sWhere.append( "CODMARCA = '" + sValores[ 9 ] + "'" );
			imp.addSubTitulo( "MARCA = " + sValores[ 10 ] );
		}

		if ( sValores[ 13 ].length() > 0 && !"T".equals( sValores[ 13 ] ) ) {
			sWhere.append( sWhere.length() > 0 ? " AND " : "" );
			sWhere.append( "TIPOPROD = '" + sValores[ 13 ] + "'" );
		}
		
		if ( sValores[ 14 ].length() > 0 ) {
			sWhere.append( sWhere.length() > 0 ? " AND " : "" );
			sWhere.append( "CODSECAO = '" + sValores[ 14 ] + "'" );
			imp.addSubTitulo( "SEÇÃO = " + sValores[ 15 ] );
		}

		if ( "C".equals( sValores[ 6 ] ) ) {

			sSQL.append( "SELECT CODPROD,REFPROD, CODALMOX, DESCPROD,CODUNID, CODMARCA,TIPOPROD,CODGRUP,CODBARPROD," );
			sSQL.append( "CODFABPROD, COMISPROD, PESOLIQPROD, PESOBRUTPROD, QTDMINPROD, QTDMAXPROD, CLOTEPROD, CUSTOMPMPROD," );
			sSQL.append( "CUSTOPEPSPROD, PRECOBASEPROD, SLDPROD, SLDRESPROD, SLDCONSIGPROD, SLDLIQPROD, DTULTCPPROD, QTDULTCPPROD " );
			sSQL.append( "FROM EQPRODUTO " );
			sSQL.append( sWhere.length() > 0 ? " WHERE " : "" );
			sSQL.append( sWhere );
			sSQL.append( " ORDER BY " + sValores[ 0 ] );

			try {

				ps = con.prepareStatement( "SELECT COUNT(*) FROM EQPRODUTO" + ( sWhere.length() > 0 ? " WHERE " : "" ) + sWhere.toString() );
				rs = ps.executeQuery();
				rs.next();

				And = new FAndamento( "Montando Relatório, Aguarde!", 0, rs.getInt( 1 ) - 1 );

				con.commit();

				ps = con.prepareStatement( sSQL.toString() );
				rs = ps.executeQuery();

				imp.limpaPags();
				imp.montaCab();
				imp.setTitulo( "Relatório de Produtos" );

				while ( rs.next() ) {
					String ref = null;

					if ( imp.pRow() >= linPag ) {
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
						imp.incPags();
						imp.eject();
					}

					if ( imp.pRow() == 0 ) {
						imp.impCab( 136, true );
						imp.say( 0, imp.comprimido() );
						imp.say( 0, "|" );
						imp.say( 135, "|" );
					}

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" );
					imp.say( 2, "Código:" );
					imp.say( 10, rs.getString( "CodProd" ) );
					imp.say( 19, "Ref.:" );
					
					ref = rs.getString("RefProd");
					
					while(ref.length() < 10	){
						ref = ref + " ";
					}
	
					imp.say( 25, ref != null ? ref.substring( 0, 10 ) : "" );
					imp.say( 36, "Cod.Bar.:" );
					imp.say( 47, rs.getString( "codBarProd" )!= null ? rs.getString( "codBarProd" ).substring( 0, 13 ) : "" );
					imp.say( 62, "Descrição:" );
					imp.say( 73, rs.getString( "DescProd" ) != null ? rs.getString( "Descprod" ).substring( 0, 60 ) : "" );
					imp.say( 135, "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" );
					imp.say( 2, "Cod.Fabr.:" );
					imp.say( 13, rs.getString( "CodFabProd" ) );
					imp.say( 27, "Grupo:" );
					imp.say( 34, rs.getString( "Codgrup" ) );
					imp.say( 48, "Custo:" );
					imp.say( 55, rs.getString( "custoMPMprod" ) );
					imp.say( 71, "Preço base:" ); 
					imp.say( 83, Funcoes.bdToStr( rs.getBigDecimal( "precobaseprod" ),Aplicativo.casasDecPre ).toString());
					imp.say( 99, "Saldo:" );
					imp.say( 106, rs.getString( "sldprod" ) );
					imp.say( 121, "Un.:" );
					imp.say( 126, rs.getString( "codunid" ) );
					imp.say( 135, "|" );

					And.atualiza( iContaReg++ );

				}

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );

				imp.eject();
				imp.fechaGravacao();

				rs.close();
				ps.close();

				con.commit();

				dl.dispose();
				And.dispose();

			} catch ( SQLException err ) {
				err.printStackTrace();
				Funcoes.mensagemErro( this, "Erro consulta tabela de produtos!" + err.getMessage() );
			}

		}
		else if ( "R".equals( sValores[ 6 ] ) ) {

			sSQL.append( "SELECT CODPROD,DESCPROD,CODUNID, SLDLIQPROD, PRECOBASEPROD, REFPROD FROM EQPRODUTO" );
			sSQL.append( sWhere.length() > 0 ? " WHERE " : "" );
			sSQL.append( sWhere );
			sSQL.append( " ORDER BY " + dl.getValores()[ 0 ] );

			try {

				ps = con.prepareStatement( "SELECT COUNT(*) FROM EQPRODUTO" + ( sWhere.length() > 0 ? " WHERE " : "" ) + sWhere.toString() );
				rs = ps.executeQuery();
				rs.next();

				And = new FAndamento( "Montando Relatório, Aguarde!", 0, rs.getInt( 1 ) - 1 );

				rs.close();
				ps.close();

				con.commit();

				ps = con.prepareStatement( sSQL.toString() );
				rs = ps.executeQuery();

				imp.limpaPags();
				imp.montaCab();
				imp.setTitulo( "Relatório de Produtos" );

				while ( rs.next() ) {

					if ( imp.pRow() >= linPag ) {
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
						imp.incPags();
						imp.eject();
					}

					if ( imp.pRow() == 0 ) {
						imp.impCab( 136, true );
						imp.say( 0, imp.comprimido() );
						imp.say( 0, "|" );
						imp.say( 135, "|" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|" );
						
						if("REFPROD".equals(sValores[0])) {
							imp.say( 3, "Refer.:" );
						}
						else {
							imp.say( 3, "Código:" );
						}
						imp.say( 12, "|" );
						imp.say( 13, "Descrição:" );
						imp.say( 70, "|" );
						imp.say( 72, "Unidade:" );
						imp.say( 95, "|" );
						imp.say( 97, "Saldo:" );
						imp.say( 117, "|" );
						imp.say( 120, "Preço Base:" );
						imp.say( 135, "|" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );

					}

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" );
					if("REFPROD".equals(sValores[0])) {
						imp.say( 4, (rs.getString( "RefProd" )).substring( 0,5 ) );
					}
					else {
						imp.say( 4, rs.getString( "CodProd" ) );
					}
					imp.say( 12, "|" );
					imp.say( 13, rs.getString( "DescProd" ) != null ? rs.getString( "Descprod" ).substring( 0, 50 ) : "" );
					imp.say( 70, "|" );
					imp.say( 72, rs.getString( "codunid" ) );
					imp.say( 95, "|" );
					imp.say( 97, Funcoes.bdToStr( rs.getBigDecimal( "sldliqprod" ),Aplicativo.casasDec ).toString());
					imp.say( 117, "|" );
					imp.say( 120, Funcoes.bdToStr( rs.getBigDecimal( "precobaseprod" ),Aplicativo.casasDecPre ).toString());
					imp.say( 135, "|" );

					And.atualiza( iContaReg++ );

				}

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );

				imp.eject();
				imp.fechaGravacao();

				rs.close();
				ps.close();

				con.commit();

				dl.dispose();
				And.dispose();

			} catch ( SQLException err ) {
				err.printStackTrace();
				Funcoes.mensagemErro( this, "Erro consulta tabela de produtos!" + err.getMessage() );
			}
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}

	}

	public void setCodBar() {

		EANFactory ean = new EANFactory();
		String codbarras = null;

		if ( sPrefs[ eprefs.CODPAISEMP.ordinal() ] == null && sPrefs[ eprefs.CODEANEMP.ordinal() ] == null ) {
			Funcoes.mensagemInforma( this, "Ajuste o cadastro da empresa.\nCodificação EAN requerida!" );
		}

		if ( sPrefs[ eprefs.TIPOCODBAR.ordinal() ].equals( "1" ) ) {

			codbarras = ean.novoEAN13( sPrefs[ eprefs.CODPAISEMP.ordinal() ], sPrefs[ eprefs.CODEANEMP.ordinal() ], txtCodProd.getVlrInteger().toString() );
			txtCodBarProd.setVlrString( codbarras );

		}
		else if ( sPrefs[ eprefs.TIPOCODBAR.ordinal() ].equals( "2" ) ) {

			codbarras = txtCodProd.getVlrString();
			txtCodBarProd.setVlrString( codbarras );
		}
	}

	public void exec( int iCodProduto ) {

		txtCodProd.setVlrInteger( new Integer( iCodProduto ) );
		lcCampos.carregaDados();
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
		else if ( evt.getSource() == btImp ) {
			imprimir( TYPE_PRINT.PRINT);
		}
		else if ( evt.getSource() == btCopiar ) {
			duplicar();
		}
		if ( evt.getSource() == btCodBar ) {
			setCodBar();
		}

		super.actionPerformed( evt );
	}

	public void stateChanged( ChangeEvent cevt ) {

		if ( cevt.getSource() == tpn ) {
			if ( tpn.getSelectedIndex() == 0 ) {
				txtCodProd.requestFocus();
			}
			else if ( tpn.getSelectedIndex() == 1 ) {
				txtUnidFat.requestFocus();
			}
			else if ( tpn.getSelectedIndex() == 2 ) {
				txtCodFor.requestFocus();
			}
			else if ( tpn.getSelectedIndex() == 3 ) {
				txtCodLote.requestFocus();
			}
			else if ( tpn.getSelectedIndex() == 4 ) {
				txtCodFotoProd.requestFocus();
			}
			else if ( tpn.getSelectedIndex() == 5 ) {
				txtCodPrecoProd.requestFocus();
			}
		}
	}

	public void edit( EditEvent eevt ) { }

	public void valorAlterado( CheckBoxEvent cbevt ) {

		if ( cbLote.getStatus() ) {
			txtCodLote.setEditable( true );
			txtDiniLote.setEditable( true );
			txtVenctoLote.setEditable( true );
			lcLote.setReadOnly( false );
		}
		else {
			txtCodLote.setEditable( false );
			txtDiniLote.setEditable( false );
			txtVenctoLote.setEditable( false );
			lcLote.setReadOnly( true );
		}
	}

	public void valorAlterado( RadioGroupEvent rgevt ) {

		if ( rgevt.getSource() == rgPA ) {
			if ( rgPA.getVlrString().equals( "RMA" ) ) {
				txtAnoCCPA.setAtivo( true );
				txtCodCCPA.setAtivo( true );
			}
			else {
				txtAnoCCPA.setAtivo( false );
				txtCodCCPA.setAtivo( false );
			}

			if ( rgPA.getVlrString().equals( "PDV" ) ) {
				txtCodCaixa.setAtivo( true );
			}
			else {
				txtCodCaixa.setAtivo( false );
			}
		}
		else if ( rgevt.getSource() == rgTipoConv ) {

			if ( "P".equals( rgTipoConv.getVlrString() ) ) {

				lbFatConv.setVisible( false );
				txtFatConv.setVisible( false );

				lbCodProdEst.setVisible( true );
				txtCodProdEst.setVisible( true );
				lbSeqEst.setVisible( true );
				txtSeqEst.setVisible( true );
				lbDescProdEst.setVisible( true );
				txtDescProdEst.setVisible( true );

				txtFatConv.setVlrBigDecimal( new BigDecimal( 1 ) );

				txtFatConv.setRequerido( false );
				txtCodProdEst.setRequerido( true );
				txtSeqEst.setRequerido( true );

			}
			else if ( "U".equals( rgTipoConv.getVlrString() ) ) {

				lbFatConv.setVisible( true );
				txtFatConv.setVisible( true );

				lbCodProdEst.setVisible( false );
				txtCodProdEst.setVisible( false );
				lbSeqEst.setVisible( false );
				txtSeqEst.setVisible( false );
				lbDescProdEst.setVisible( false );
				txtDescProdEst.setVisible( false );

				txtFatConv.setRequerido( true );
				txtCodProdEst.setRequerido( false );
				txtSeqEst.setRequerido( false );

			}
		}

	}
	
	public void beforeCarrega( CarregaEvent cevt ) {
		if (cevt.getListaCampos() == lcCampos) {
			if (lcMGProduto.getStatus()==ListaCampos.LCS_INSERT && 
					"".equals( txaMGDescProd.getVlrString().trim()) &&
					"".equals( txaMGDescProdCompl.getVlrString().trim()) ){
				lcMGProduto.cancelInsert();
			} /*else {
				lcMGProduto.carregaDados();
			}*/
		}
	}

	public void afterCarrega( CarregaEvent cevt ) {

		try {
			if ( cevt.getListaCampos() == lcCampos ) {
				if (verificaSeExisteMGProduto()) {
					lcMGProduto.carregaDados();
				} else {
					lcMGProduto.insert( false );
					txaMGDescProd.setVlrString( null );
					txaMGDescProdCompl.setVlrString( null );
				}

				buscaEstoque();
				CustosProd custos = new CustosProd( txtCodAlmox.getVlrInteger(), txtCodProd.getVlrInteger(), con );
				buscaUltimaEntrada();
				
				txtCustoPEPSProd.setVlrBigDecimal( custos.getCustoPEPSProd() );
				txtCustoMPMProd.setVlrBigDecimal( custos.getCustoMPMProd() );
				txtCustoPEPSAlmox.setVlrBigDecimal( custos.getCustoPEPSAlmox() );
				txtCustoMPMAlmox.setVlrBigDecimal( custos.getCustoMPMAlmox() );

				// Oculta aba Lote, caso produto não o utilize
				if ( "S".equals( cbLote.getVlrString() ) ) {
					adicTab( "Lotes", pnLote );
				}
				else {
					removeTab( "Lotes", pnLote );
				}
				// Oculta aba Serie, caso produto não o utilize
				if ( "S".equals( cbSerie.getVlrString() ) ) {
					adicTab( "Series", pnSerie );
				}
				else {
					removeTab( "Series", pnSerie );
				}

			}
/*			
			else if( cevt.getListaCampos() == lcPreco ) {
				for ( Vector<Object> row : tabPreco.getDataVector() ) {
					if(row.elementAt( 0 ).equals( txtCodTabPreco.getVlrInteger() ) ) {
						row.setElementAt( "teste", 9 );
					}
					
				}
			}*/
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	public void beforeEdit( EditEvent eevt ) { }

	public void afterEdit( EditEvent eevt ) {

		if ( imFotoProd.foiAlterado() ) {
			txtLargFotoProd.setVlrString( String.valueOf( imFotoProd.getLargura() ) );
			txtAltFotoProd.setVlrString( String.valueOf( imFotoProd.getAltura() ) );
		}
	}

	public void beforeInsert( InsertEvent evt ) { }

	public void afterInsert( InsertEvent ievt ) {

		if ( ievt.getListaCampos() == lcFoto && imFotoProd.foiAlterado() ) {
			txtLargFotoProd.setVlrString( "" + imFotoProd.getLargura() );
			txtAltFotoProd.setVlrString( "" + imFotoProd.getAltura() );
		}
		else if ( ievt.getListaCampos() == lcCampos ) {
			carregaMoeda();
			setCadastroPadrao();
			
			cbAtivo.setVlrString( "S" );
			txtRefProd.setVlrString( txtCodProd.getVlrString() );
			txtCodBarProd.setVlrString( txtCodProd.getVlrString() );
			txtCodFabProd.setVlrString( txtCodProd.getVlrString() );
			txtPesoBrutProd.setVlrBigDecimal( new BigDecimal(0) );
			txtPesoLiqProd.setVlrBigDecimal( new BigDecimal(0) );
		}
		else if ( ievt.getListaCampos() == lcProdAcesso ) {
			habAcesso( true, 0 );
		} /*else if (ievt.getListaCampos() == lcMGProduto) {
			txtCodProdMG.setVlrInteger( txtCodProd.getVlrInteger() );
		}*/
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		sPrefs = getPrefs();
		setCadastroPadrao();

		montaTela();

		lcLote.setConexao( cn );
		lcSerie.setConexao( cn );
		lcMoeda.setConexao( cn );
		lcUnid.setConexao( cn );
		lcFisc.setConexao( cn );
		lcMarca.setConexao( cn );
		lcSecao.setConexao( cn );
		lcGrup.setConexao( cn );
		lcAlmox.setConexao( cn );
		lcPrazoEnt.setConexao( cn );
		lcUnidFat.setConexao( cn );
		lcPlan.setConexao( cn );
		lcCC.setConexao( cn );
		lcCCAcesso.setConexao( cn );
		lcCaixa.setConexao( cn );
		lcForFK.setConexao( cn );
		lcFatConv.setConexao( cn );
		lcProdPlan.setConexao( cn );
		lcProdFor.setConexao( cn );
		lcFoto.setConexao( cn );
		lcPreco.setConexao( cn );
		lcClasCliPreco.setConexao( cn );
		lcTabPreco.setConexao( cn );
		lcPlanoPagPreco.setConexao( cn );
		lcCodAltProd.setConexao( cn );
		lcProdAcesso.setConexao( cn );
		lcProdEstCod.setConexao( cn );
		lcTipoChamado.setConexao( cn );
		lcMGProduto.setConexao( cn );
	}

	public void beforePost( PostEvent pevt ) {

		if ( pevt.getListaCampos() == lcCampos ) {
			BigDecimal qtdemb = txtQtdEmbalagem.getVlrBigDecimal();
			if ( ( qtdemb == null ) || ( qtdemb.compareTo( new BigDecimal( 0 ) ) < 1 ) ) {
				txtQtdEmbalagem.setVlrInteger( 1 );
			}
		}
		if ( pevt.getListaCampos() == lcFatConv ) {
			if ( "P".equals( rgTipoConv.getVlrString() ) ) {
				if ( !validaEstrutura() ) {
					Funcoes.mensagemInforma( this, "A estrutura selecionada não contem esse produto!\nSelecione outra estrutura para conversão." );
				}
			}
		}
	}

	private boolean validaEstrutura() {

		boolean ret = false;
		StringBuilder sql = new StringBuilder();

		try {

			sql.append( "select count(*) from ppitestrutura ie " );
			sql.append( "where ie.codemp=? and ie.codfilial=? and ie.codprod=? and ie.seqest=? " );
			sql.append( "and ie.codemppd=? and ie.codfilialpd=? and ie.codprodpd=? " );

			PreparedStatement ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, lcProdEstCod.getCodEmp() );
			ps.setInt( 2, lcProdEstCod.getCodFilial() );
			ps.setInt( 3, txtCodProdEst.getVlrInteger() );
			ps.setInt( 4, txtSeqEst.getVlrInteger() );
			ps.setInt( 5, lcCampos.getCodEmp() );
			ps.setInt( 6, lcCampos.getCodFilial() );
			ps.setInt( 7, txtCodProd.getVlrInteger() );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				ret = ( rs.getInt( 1 ) > 0 );
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return ret;
	}
	
	private boolean verificaSeExisteMGProduto() {

		boolean ret = false;
		StringBuilder sql = new StringBuilder();

		try {

			sql.append( "select codprod from mgproduto mg " );
			sql.append( "where mg.codemp=? and mg.codfilial=? and mg.codprod=? ");

			PreparedStatement ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "MGPRODUTO" ) );
			ps.setInt( 3, txtCodProd.getVlrInteger() );


			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				ret = true;
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return ret;
	}

}

