/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda. Robson Sanchez e Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FTipoMov.java <BR>
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
 *                   Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.gms.view.frame.crud.tabbed;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.CheckBoxEvent;
import org.freedom.acao.CheckBoxListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.JComboBoxEvent;
import org.freedom.acao.JComboBoxListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.RadioGroupEvent;
import org.freedom.acao.RadioGroupListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FTabDados;
import org.freedom.modulos.cfg.view.frame.crud.tabbed.FUsuario;
import org.freedom.modulos.gms.business.object.TipoMov;
import org.freedom.modulos.lvf.view.frame.crud.plain.FModDocFisc;
import org.freedom.modulos.std.view.frame.crud.detail.FPlanoPag;
import org.freedom.modulos.std.view.frame.crud.plain.FMensagem;
import org.freedom.modulos.std.view.frame.crud.plain.FModNota;
import org.freedom.modulos.std.view.frame.crud.plain.FSerie;
import org.freedom.modulos.std.view.frame.crud.plain.FTabPreco;
import org.freedom.modulos.std.view.frame.crud.tabbed.FTransp;
import org.freedom.modulos.std.view.frame.report.FRegraComiss;

public class FTipoMov extends FTabDados implements RadioGroupListener, CheckBoxListener, InsertListener, CarregaListener, JComboBoxListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pnGeral = new JPanelPad( 430, 460 );
	
	private JPanelPad pnComplementar = new JPanelPad();

	private JPanelPad pinRestricoes = new JPanelPad( 430, 460 );

	private JPanelPad pnRegrasVenda = new JPanelPad( "Regras para fechamento de venda", Color.BLUE );

	private JPanelPad pnRegrasGeral = new JPanelPad( "Regras para o tipo de movimento", Color.BLUE );

	private JPanelPad pnRegrasOutras = new JPanelPad( "Outras regras", Color.BLUE );

	private JPanelPad pinNavRestricoes = new JPanelPad( 680, 30 );

	private JPanelPad pinGeral = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnRestricoes = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinDetRestricoes = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTablePad tbRestricoes = new JTablePad();

	private JScrollPane spnRestricoes = new JScrollPane( tbRestricoes );

	private JTextFieldPad txtCodTipoMov = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodTipoMov2 = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodTipoMovTc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDescTipoMov = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );
	
	private JTextFieldPad txtCodMens = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtMens = new JTextFieldFK( JTextFieldFK.TP_STRING, 10000, 0 );

	private JTextFieldPad txtCodModNota = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtCodModDocFisc = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private JTextFieldPad txtCodSerie = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtCodTab = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtEspecieTipomov = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 9 );

	private JTextFieldPad txtIDUsu = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldFK txtDescModNota = new JTextFieldFK( JTextFieldPad.TP_STRING, 30, 0 );
	
	private JTextFieldFK txtDescModDocFisc = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldFK txtDescSerie = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTab = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtDescTipoMov2 = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );
	
	private JTextFieldFK txtDescTipoMovTc = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtNomeUsu = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodRegraComis = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescRegraComis = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodPlanoPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescPlanoPag = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldPad txtCodTran = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTran = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldPad txtDescNatCompl = new JTextFieldPad(JTextFieldPad.TP_STRING, 60, 0);
	
	private JRadioGroup<?, ?> rgOperTipoMov = null;

	private JRadioGroup<?, ?> rgESTipoMov = null;

	private JRadioGroup<?, ?> rgTipoFrete = null;

	private JComboBoxPad cbTipoMov = null;

	private JCheckBoxPad cbRestritoTipoMov = new JCheckBoxPad( "Permitir todos os usuários?", "S", "N" );

	private JCheckBoxPad cbFiscalTipoMov = new JCheckBoxPad( "Sim", "S", "N" );

	private JCheckBoxPad cbEstoqTipoMov = new JCheckBoxPad( "Sim", "S", "N" );

	private JCheckBoxPad cbSomaTipoMov = new JCheckBoxPad( "Sim", "S", "N" );

	private JCheckBoxPad chImpPedTipoMov = new JCheckBoxPad( "Sim", "S", "N" );

	private JCheckBoxPad cbImpNfTipoMov = new JCheckBoxPad( "Sim", "S", "N" );

	private JCheckBoxPad cbImpBolTipoMov = new JCheckBoxPad( "Sim", "S", "N" );

	private JCheckBoxPad cbImpRecTipoMov = new JCheckBoxPad( "Sim", "S", "N" );

	private JCheckBoxPad cbReImpNfTipoMov = new JCheckBoxPad( "Sim", "S", "N" );

	private JCheckBoxPad cbSeqNfTipoMov = new JCheckBoxPad( "Sim", "S", "N" );

	private JCheckBoxPad cbVlrMFinTipoMov = new JCheckBoxPad( "Permitir valor das parcelas menor que o total?", "S", "N" );

	private JCheckBoxPad cbMComisTipoMov = new JCheckBoxPad( "Múltiplos comissionados ?", "S", "N" );

	private JCheckBoxPad cbEmitNFCPMov = new JCheckBoxPad( "Emite nota de compra ?", "S", "N" );
	
	private JCheckBoxPad cbDesBloqCV = new JCheckBoxPad( "Desabilita Bloqueio Compra/Venda ?", "S", "N" );

	private Navegador navRestricoes = new Navegador( true );

	private ListaCampos lcRestricoes = new ListaCampos( this, "" );

	private ListaCampos lcUsu = new ListaCampos( this, "US" );

	private ListaCampos lcModNota = new ListaCampos( this, "MN" );
	
	private ListaCampos lcModDocFisc = new ListaCampos( this, "DF" );
	
	private ListaCampos lcPlanoPag = new ListaCampos( this, "PP" );

	private ListaCampos lcSerie = new ListaCampos( this, "SE" );

	private ListaCampos lcTab = new ListaCampos( this, "TB" );

	private ListaCampos lcTipoMov = new ListaCampos( this, "TM" );

	private ListaCampos lcTipoMovTc = new ListaCampos( this, "TC" );

	private ListaCampos lcTran = new ListaCampos( this, "TN" );

	private ListaCampos lcRegraComis = new ListaCampos( this, "RC" );

	private ListaCampos lcMens = new ListaCampos( this, "MC" );

	private boolean[] bPrefs = null;

	private JLabelPad separador1 = new JLabelPad();

	public FTipoMov() {

		super();

		nav.setNavigation( true );

		setTitulo( "Cadastro de Tipos de Movimento" );
		setAtribos( 50, 40, 720, 538 );

		lcRestricoes.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcRestricoes );
		lcRestricoes.setTabela( tbRestricoes );

		lcModNota.add( new GuardaCampo( txtCodModNota, "CodModNota", "Cód.mod.nota", ListaCampos.DB_PK, false ) );
		lcModNota.add( new GuardaCampo( txtDescModNota, "DescModNota", "Descrição do modelo de nota", ListaCampos.DB_SI, false ) );
		lcModNota.montaSql( false, "MODNOTA", "LF" );
		lcModNota.setQueryCommit( false );
		lcModNota.setReadOnly( true );
		txtCodModNota.setTabelaExterna( lcModNota, FModNota.class.getCanonicalName() );

		lcModDocFisc.add( new GuardaCampo( txtCodModDocFisc, "CodModDocFisc", "Cód.md.doc.fisc.", ListaCampos.DB_PK, false ) );
		lcModDocFisc.add( new GuardaCampo( txtDescModDocFisc, "DescModDocFisc", "Descrição do modelo de documento fiscal", ListaCampos.DB_SI, false ) );
		lcModDocFisc.montaSql( false, "MODDOCFISC", "LF" );
		lcModDocFisc.setQueryCommit( false );
		lcModDocFisc.setReadOnly( true );
		txtCodModDocFisc.setTabelaExterna( lcModDocFisc, FModDocFisc.class.getCanonicalName() );
		
		lcPlanoPag.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.Pl.Pag.", ListaCampos.DB_PK, false ) );
		lcPlanoPag.add( new GuardaCampo( txtDescPlanoPag, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPag.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag.setQueryCommit( false );
		lcPlanoPag.setReadOnly( true );
		txtCodPlanoPag.setTabelaExterna( lcPlanoPag, FPlanoPag.class.getCanonicalName() );

		lcSerie.add( new GuardaCampo( txtCodSerie, "Serie", "Cód.serie", ListaCampos.DB_PK, false ) );
		lcSerie.add( new GuardaCampo( txtDescSerie, "DocSerie", "Nº. doc", ListaCampos.DB_SI, false ) );
		lcSerie.montaSql( false, "SERIE", "LF" );
		lcSerie.setQueryCommit( false );
		lcSerie.setReadOnly( true );
		txtCodSerie.setTabelaExterna( lcSerie, FSerie.class.getCanonicalName() );

		lcTab.add( new GuardaCampo( txtCodTab, "CodTab", "Cód.tb.pc.", ListaCampos.DB_PK, false ) );
		lcTab.add( new GuardaCampo( txtDescTab, "DescTab", "Descrição da tabela de preço", ListaCampos.DB_SI, false ) );
		lcTab.montaSql( false, "TABPRECO", "VD" );
		lcTab.setQueryCommit( false );
		lcTab.setReadOnly( true );
		txtCodTab.setTabelaExterna( lcTab, FTabPreco.class.getCanonicalName() );

		lcTipoMov.add( new GuardaCampo( txtCodTipoMov2, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false ) );
		lcTipoMov.add( new GuardaCampo( txtDescTipoMov2, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false ) );
		lcTipoMov.montaSql( false, "TIPOMOV", "EQ" );
		lcTipoMov.setQueryCommit( false );
		lcTipoMov.setReadOnly( true );
		txtCodTipoMov2.setTabelaExterna( lcTipoMov, FTabPreco.class.getCanonicalName() );

		lcTipoMovTc.add( new GuardaCampo( txtCodTipoMovTc, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false ) );
		lcTipoMovTc.add( new GuardaCampo( txtDescTipoMovTc, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false ) );
		lcTipoMovTc.montaSql( false, "TIPOMOV", "EQ" );
		lcTipoMovTc.setQueryCommit( false );
		lcTipoMovTc.setReadOnly( true );
		txtCodTipoMovTc.setTabelaExterna( lcTipoMovTc, FTabPreco.class.getCanonicalName() );

		lcUsu.add( new GuardaCampo( txtIDUsu, "IDUsu", "ID", ListaCampos.DB_PK, txtNomeUsu, false ) );
		lcUsu.add( new GuardaCampo( txtNomeUsu, "NomeUsu", "Nome nome do usuário", ListaCampos.DB_SI, false ) );
		lcUsu.montaSql( false, "USUARIO", "SG" );
		lcUsu.setQueryCommit( false );
		lcUsu.setReadOnly( true );
		txtIDUsu.setFK( true );
		txtIDUsu.setNomeCampo( "IDUsu" );
		txtIDUsu.setTabelaExterna( lcUsu, FUsuario.class.getCanonicalName() );

		lcRegraComis.add( new GuardaCampo( txtCodRegraComis, "CodRegrComis", "Cód.rg.comis.", ListaCampos.DB_PK, txtDescRegraComis, false ) );
		lcRegraComis.add( new GuardaCampo( txtDescRegraComis, "DescRegrComis", "Descrição da regra do comissionado", ListaCampos.DB_SI, false ) );
		lcRegraComis.montaSql( false, "REGRACOMIS", "VD" );
		lcRegraComis.setQueryCommit( false );
		lcRegraComis.setReadOnly( true );
		txtCodRegraComis.setFK( true );
		txtCodRegraComis.setNomeCampo( "IDUsu" );
		txtCodRegraComis.setTabelaExterna( lcRegraComis, FRegraComiss.class.getCanonicalName() );

		lcTran.add( new GuardaCampo( txtCodTran, "CodTran", "Cód.tran.", ListaCampos.DB_PK, false ) );
		lcTran.add( new GuardaCampo( txtDescTran, "RazTran", "Descrição da transportadora", ListaCampos.DB_SI, false ) );
		lcTran.montaSql( false, "TRANSP", "VD" );
		lcTran.setQueryCommit( false );
		lcTran.setReadOnly( true );
		txtCodTran.setTabelaExterna( lcTran, FTransp.class.getCanonicalName() );

		cbTipoMov = new JComboBoxPad( null, null, JComboBoxPad.TP_STRING, 2, 0 );
		cbTipoMov.addComboBoxListener( this );
		
		lcMens.add( new GuardaCampo( txtCodMens, "CodMens", "Cód.mens.", ListaCampos.DB_PK, false ) );
		lcMens.add( new GuardaCampo( txtMens, "Mens", "Mensagem", ListaCampos.DB_SI, false ) );
		lcMens.montaSql( false, "MENSAGEM", "LF" );
		lcMens.setQueryCommit( false );
		lcMens.setReadOnly( true );
		txtCodMens.setTabelaExterna( lcMens, FMensagem.class.getCanonicalName() );

		
		Vector<String> vValsES = new Vector<String>();
		Vector<String> vLabsES = new Vector<String>();

		vLabsES.addElement( TipoMov.ENTRADA.getName() );
		vLabsES.addElement( TipoMov.SAIDA.getName() );
		vLabsES.addElement( TipoMov.INVENTARIO.getName() );

		vValsES.addElement( (String) TipoMov.ENTRADA.getValue() );
		vValsES.addElement( (String) TipoMov.SAIDA.getValue() );
		vValsES.addElement( (String) TipoMov.INVENTARIO.getValue() );

		rgESTipoMov = new JRadioGroup<String, String>( 1, 3, vLabsES, vValsES );
		rgESTipoMov.addRadioGroupListener( this );

		Vector<String> vValsTF = new Vector<String>();
		Vector<String> vLabsTF = new Vector<String>();
		vLabsTF.addElement( "CIF" );
		vLabsTF.addElement( "FOB" );
		vValsTF.addElement( "C" );
		vValsTF.addElement( "F" );
		rgTipoFrete = new JRadioGroup<String, String>( 2, 1, vLabsTF, vValsTF );
		rgTipoFrete.setAtivo( false );

		montaCbTipoMov( "E" );

		txtCodTipoMov2.setNomeCampo( "CodTipoMov" );

		Vector<String> vLabsOper = new Vector<String>();
		Vector<String> vValsOper = new Vector<String>();
		
		vLabsOper.addElement( "Conjugada" );
		vLabsOper.addElement( "Produto" );
		vLabsOper.addElement( "Serviço" );
		vValsOper.addElement( "C" );
		vValsOper.addElement( "P" );
		vValsOper.addElement( "S" );
		
		rgOperTipoMov = new JRadioGroup<String, String>( 1, 3, vLabsOper, vValsOper );
		
		pinGeral.setPreferredSize( new Dimension( 430, 560 ) );
		pinGeral.add( pnGeral, BorderLayout.CENTER );

		setPainel( pnGeral );

		adicTab( "Geral", pnGeral );

		adicCampo( txtCodTipoMov, 7, 20, 80, 20, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, true );
		adicCampo( txtDescTipoMov, 90, 20, 250, 20, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, true );

		adicCampo( txtCodSerie, 7, 105, 80, 20, "Serie", "Série", ListaCampos.DB_FK, txtDescSerie, true );
		adicDescFK( txtDescSerie, 90, 105, 250, 20, "DocSerie", "Documento atual" );
		adicCampo( txtCodModNota, 7, 145, 80, 20, "CodModNota", "Cód.mod.nota", ListaCampos.DB_FK, true );
		adicDescFK( txtDescModNota, 90, 145, 250, 20, "DescModNota", "Descrição do modelo de nota" );
		adicCampo( txtCodTipoMov2, 7, 185, 80, 20, "CodTipoMovTM", "Cód.mov.nf.", ListaCampos.DB_FK, txtDescTipoMov2, false );
		adicDescFK( txtDescTipoMov2, 90, 185, 250, 20, "DescTipoMov", "Descrição do movimento para nota." );
	
		adicCampo( txtCodTab, 7, 225, 80, 20, "CodTab", "Cód.tp.pc.", ListaCampos.DB_FK, txtDescTab, false );
		adicDescFK( txtDescTab, 90, 225, 250, 20, "DescTab", "Descrição da tab. de preços" );
		adicCampo( txtCodRegraComis, 7, 265, 80, 20, "CodRegrComis", "Cód.rg.comis.", ListaCampos.DB_FK, false );
		adicDescFK( txtDescRegraComis, 90, 265, 250, 20, "DescRegrComis", "Descrição da regra de comissionado" );
		adicCampo( txtCodTran, 7, 305, 80, 20, "CodTran", "Cód.tran.", ListaCampos.DB_FK, false );
		adicDescFK( txtDescTran, 90, 305, 250, 20, "DescTran", "Descrição da transportadora" );
		adicCampo( txtCodModDocFisc, 7, 345, 80, 20, "CodModDocFisc", "Cód.md.doc.fisc.", ListaCampos.DB_FK, false );
		adicDescFK( txtDescModDocFisc, 90, 345, 250, 20, "DescModDocFisc", "Descrição do modelo de documento fiscal " );
		adicCampo( txtCodPlanoPag, 7, 385, 80, 20, "CodPlanoPag", "Cód.Pl.Pag.", ListaCampos.DB_FK, false );
		adicDescFK( txtDescPlanoPag, 90, 385, 250, 20, "DescPlanoPag", "Descrição do plano de pagamento " );
		//adicCampo( txtDescNatCompl, 7, 465, 333, 20, "DescNatCompl", "Descrição de natureza de operação", ListaCampos.DB_SI, false );
		
		separador1.setBorder( BorderFactory.createEtchedBorder() );
		adic( separador1, 350, 4, 2, 415 );

		adicDB( rgESTipoMov, 360, 20, 330, 30, "ESTipoMov", "Fluxo", true );

		adicCampo( txtEspecieTipomov, 7, 60, 80, 24, "EspecieTipomov", "Espécie", ListaCampos.DB_SI, true );
		adicDB( cbTipoMov, 90, 60, 250, 24, "TipoMov", "Tipo de movimento", true );
		
		adic( pnRegrasGeral, 360, 64, 250, 70 );

		setPainel( pnRegrasGeral );

		adicDB( cbFiscalTipoMov, 5, 20, 60, 20, "FiscalTipoMov", "Fiscal", true );
		adicDB( cbEstoqTipoMov, 85, 20, 60, 20, "EstoqTipoMov", "Estoque", true );
		adicDB( cbSomaTipoMov, 165, 20, 70, 20, "SomaVdTipoMov", "Financeiro", true );

		setPainel( pnGeral );

		adicDB( rgTipoFrete, 613, 72, 76, 61, "CTipoFrete", "Tipo de frete", false );

		adicDB( rgOperTipoMov, 360, 150, 330, 30, "OperTipoMov", "Operações", true );
		
		adic( pnRegrasVenda, 360, 188, 330, 115 );

		setPainel( pnRegrasVenda );

		adicDB( chImpPedTipoMov, 5, 20, 105, 20, "ImpPedTipoMov", "Imprimir pedido", true );
		adicDB( cbImpNfTipoMov, 125, 20, 85, 20, "ImpNfTipoMov", "Imprimir NF", true );
		adicDB( cbImpBolTipoMov, 220, 20, 105, 20, "ImpBolTipoMov", "Imprimir boleto", true );

		adicDB( cbImpRecTipoMov, 5, 65, 105, 20, "ImpRecTipoMov", "Imprimir recibo", true );
		adicDB( cbReImpNfTipoMov, 125, 65, 85, 20, "ReImpNfTipoMov", "Reimprimir NF", true );
		adicDB( cbSeqNfTipoMov, 220, 65, 105, 20, "SeqNfTipoMov", "Aloca Nro. NF", true );

		txtCodRegraComis.setAtivo( false );

		cbEmitNFCPMov.setEnabled( false );

		setPainel( pnGeral );

		adic( pnRegrasOutras, 360, 308, 330, 130 );
		setPainel( pnRegrasOutras );

		adicDB( cbVlrMFinTipoMov, 5, 0, 300, 20, "VlrMFinTipoMov", "", true );
		adicDB( cbRestritoTipoMov, 5, 20, 240, 20, "TUSUTIPOMOV", "", true );
		adicDB( cbMComisTipoMov, 5, 40, 240, 20, "MComisTipoMov", "", true );
		adicDB( cbEmitNFCPMov, 5, 60, 240, 20, "EmitNfCpMov", "", true );
		adicDB( cbDesBloqCV, 5, 80, 240, 20, "DesBloqCV", "", true );
		
		
		cbRestritoTipoMov.addCheckBoxListener( this );
		cbMComisTipoMov.addCheckBoxListener( this );
		
		
		setPainel( pnComplementar );
		adicTab( "Complementar", pnComplementar );
		
		adicCampo( txtCodTipoMovTc, 7, 20, 80, 20, "CodTipoMovTC", "Cód.mov.nc.", ListaCampos.DB_FK, txtDescTipoMovTc, false );
		adicDescFK( txtDescTipoMovTc, 90, 20, 250, 20, "DescTipoMov", "Descrição do movimento para nota complementar." );
		adicCampo( txtCodMens, 7, 60, 80, 20, "CodMens", "Cód.mens.Compl", ListaCampos.DB_FK, txtMens, false );
		adicDescFK( txtMens, 90, 60, 250, 20, "Mens", "Mensagem" );
		adicCampo( txtDescNatCompl, 7, 100, 333, 20, "DescNatCompl", "Descrição de natureza de operação", ListaCampos.DB_SI, false );
		
		setListaCampos( true, "TIPOMOV", "EQ" );
		lcCampos.setQueryInsert( false );

		setPainel( pinDetRestricoes, pnRestricoes );

		pinDetRestricoes.setPreferredSize( new Dimension( 430, 80 ) );
		pinDetRestricoes.add( pinNavRestricoes, BorderLayout.SOUTH );
		pinDetRestricoes.add( pinRestricoes, BorderLayout.CENTER );

		setListaCampos( lcRestricoes );
		setNavegador( navRestricoes );

		pnRestricoes.add( pinDetRestricoes, BorderLayout.SOUTH );
		pnRestricoes.add( spnRestricoes, BorderLayout.CENTER );
		pinNavRestricoes.adic( navRestricoes, 0, 0, 270, 25 );

		setPainel( pinRestricoes );

		adicCampo( txtIDUsu, 7, 20, 80, 20, "IdUsu", "Id", ListaCampos.DB_PF, txtNomeUsu, true );
		adicDescFK( txtNomeUsu, 90, 20, 250, 20, "NomeUsu", " e nome do usuário" );

		setListaCampos( true, "TIPOMOVUSU", "EQ" );
		lcRestricoes.setQueryInsert( false );
		lcRestricoes.setQueryCommit( false );
		lcRestricoes.montaTab();

		txtCodTipoMov.setTabelaExterna( lcRestricoes, FTipoMov.class.getCanonicalName() );

		tbRestricoes.setTamColuna( 80, 0 );
		tbRestricoes.setTamColuna( 280, 1 );
		lcCampos.addInsertListener( this );

	}

	public void beforeInsert( InsertEvent ievt ) {

	}

	public void afterInsert( InsertEvent ievt ) {

		if ( !bPrefs[ 0 ] ) {
			cbEstoqTipoMov.setVlrString( "N" );
		}
		if ( bPrefs[ 1 ] ) {
			cbMComisTipoMov.setVlrString( "S" );
		}
		else {
			cbMComisTipoMov.setVlrString( "N" );
		}

	}

	public void beforePost( PostEvent pevt ) {

		if ( txtCodRegraComis.getAtivo() && txtCodRegraComis.getVlrInteger() == 0 ) {
			Funcoes.mensagemErro( this, "Campo Cód.rg.comis. é requerido!" );
			pevt.cancela();
		}

		super.beforePost( pevt );
	}

	private void montaCbTipoMov( String ES ) {

		cbTipoMov.limpa();

		cbTipoMov.setItens( TipoMov.getLabels( ES ), TipoMov.getValores( ES ) );

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		
		lcTipoMov.setConexao( cn );
		lcTipoMovTc.setConexao( cn );
		lcModNota.setConexao( cn );
		lcModDocFisc.setConexao( cn );		
		lcSerie.setConexao( cn );
		lcTab.setConexao( cn );
		lcRestricoes.setConexao( cn );
		lcUsu.setConexao( cn );
		lcRegraComis.setConexao( cn );
		lcTran.setConexao( cn );
		lcPlanoPag.setConexao( cn );
		lcMens.setConexao( cn );
		bPrefs = prefs();
		cbEstoqTipoMov.setEnabled( bPrefs[ 0 ] ); // Habilita controle de estoque de acordo com o preferências
	}

	public void valorAlterado( RadioGroupEvent evt ) {

		if ( rgESTipoMov.getVlrString().equals( "S" ) ) {
			rgTipoFrete.setAtivo( true );
		}
		else {
			rgTipoFrete.setAtivo( false );
		}

		montaCbTipoMov( rgESTipoMov.getVlrString() );

	}

	public void valorAlterado( JComboBoxEvent evt ) {

		if ( evt.getComboBoxPad() == cbTipoMov ) {
			
			if ( 		TipoMov.TM_COMPRA.getValue().equals( cbTipoMov.getVlrString() ) 
					|| 	TipoMov.TM_PEDIDO_COMPRA.getValue().equals( cbTipoMov.getVlrString() ) 
					|| 	TipoMov.TM_NOTA_FISCAL_IMPORTACAO.getValue().equals( cbTipoMov.getVlrString() )
					|| 	TipoMov.TM_DEVOLUCAO_REMESSA.getValue().equals( cbTipoMov.getVlrString() )  
					|| 	TipoMov.TM_DEVOLUCAO_VENDA.getValue().equals( cbTipoMov.getVlrString() ) 
				
				)
			
			{
				cbEmitNFCPMov.setEnabled( true );
			}
			else {
				cbEmitNFCPMov.setVlrString( "N" );
				cbEmitNFCPMov.setEnabled( false );
			}
		}

	}

	public void valorAlterado( CheckBoxEvent evt ) {

		if ( evt.getCheckBox() == cbRestritoTipoMov ) {
			if ( evt.getCheckBox().isSelected() ) {
				removeTab( "Restrições de Usuário", pnRestricoes );
			}
			else {
				adicTab( "Restrições de Usuário", pnRestricoes );
			}
		}
		else if ( evt.getCheckBox() == cbMComisTipoMov ) {

			if ( bPrefs[ 1 ] ) {
				cbMComisTipoMov.setEnabled( true );
				txtCodRegraComis.setRequerido( evt.getCheckBox().isSelected() );
				txtCodRegraComis.setAtivo( evt.getCheckBox().isSelected() );
				if ( !evt.getCheckBox().isSelected() ) {
					txtCodRegraComis.setVlrInteger( 0 );
					lcRegraComis.carregaDados();
				}
			}
			else {
				cbMComisTipoMov.setSelected( false );
				cbMComisTipoMov.setEnabled( false );
			}
		}

	}

	private boolean[] prefs() {

		boolean[] bRetorno = new boolean[ 2 ];
		String sSQL = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			sSQL = "SELECT CONTESTOQ, MULTICOMIS FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			rs = ps.executeQuery();
			bRetorno[ 0 ] = true;
			if ( rs.next() ) {
				bRetorno[ 0 ] = true;
				if ( "S".equals( rs.getString( "CONTESTOQ" ) ) ) {
					bRetorno[ 0 ] = true;
				}
				else {
					bRetorno[ 0 ] = false;
				}
				if ( "S".equals( rs.getString( "MULTICOMIS" ) ) ) {
					bRetorno[ 1 ] = true;
				}
				else {
					bRetorno[ 1 ] = false;
				}
			}
			rs.close();
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage(), true, con, err );
		} finally {
			rs = null;
			ps = null;
		}
		return bRetorno;
	}

	public void afterCarrega( CarregaEvent cevt ) {

		if ( cevt.getListaCampos() == lcCampos ) {

			if ( "CP".equals( cbTipoMov.getVlrString() ) || "PC".equals( cbTipoMov.getVlrString() ) ) {
				cbEmitNFCPMov.setEnabled( true );
			}
			else {
				cbEmitNFCPMov.setVlrString( "N" );
				cbEmitNFCPMov.setEnabled( false );
			}

		}

	}

	public void beforeCarrega( CarregaEvent cevt ) {

		// TODO Auto-generated method stub

	}

}
