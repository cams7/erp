/**
 * @version 11/02/2002 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * @version 28/11/2010 Anderson Sanchez <BR> 
 * 
 * Projeto: Freedom <BR>
 * 
 * Pacote: org.freedom.modulos.std <BR>
 * Classe:
 *         
 * @(#)FConta.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * Tela para cadastro e manutenção de contas.
 * 
 */

package org.freedom.modulos.fnc.view.frame.crud.tabbed;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Vector;

import javax.swing.JScrollPane;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.CheckBoxEvent;
import org.freedom.acao.CheckBoxListener;
import org.freedom.acao.RadioGroupEvent;
import org.freedom.acao.RadioGroupListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FTabDados;
import org.freedom.modulos.cfg.view.frame.crud.tabbed.FUsuario;
import org.freedom.modulos.fnc.library.swing.component.JTextFieldPlan;
import org.freedom.modulos.fnc.view.frame.crud.plain.FBanco;
import org.freedom.modulos.std.view.frame.crud.special.FPlanejamento;
import org.freedom.modulos.std.view.frame.crud.tabbed.FMoeda;

public class FConta extends FTabDados implements CheckBoxListener, RadioGroupListener, CarregaListener, MouseListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtAgConta = new JTextFieldPad( JTextFieldPad.TP_STRING, 6, 0 );

	private JTextFieldPad txtNumConta = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtDescConta = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNumContaCV = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldFK txtDescContaCV = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldPad txtCodBanco = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );
	
	private JTextFieldPad txtSaldoSL = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 12, Aplicativo.casasDecFin );

	private JTextFieldPad txtDataConta = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	// Posto - Informação necessária para boleto e CNAB Sicred.
	
	private JTextFieldPad txtPostoConta = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );
	
	private JTextFieldPad txtDataIniSaldo = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );
	
	private JTextFieldPad txtDataFimSaldo = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodMoeda = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );
	
	private JTextFieldPlan txtCodPlan = new JTextFieldPlan( JTextFieldPad.TP_STRING, 13, 0 );
	
	private JTextFieldFK txtDescBanco = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtDescMoeda = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtDescPlan = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JCheckBoxPad cbAtivaConta = new JCheckBoxPad( "Sim", "S", "N" );

	private JCheckBoxPad chbRestritoTipoMov = new JCheckBoxPad( "Todos os usuários?", "S", "N" );
	
	private JCheckBoxPad cbContaCheque = new JCheckBoxPad( "Conta p/cheques", "S", "N" );
	
	private JCheckBoxPad cbFechado = new JCheckBoxPad( "Fechado", "S", "N" );
	
	private JTextFieldPad txtDataSL = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private Vector<String> vValsTipo = new Vector<String>();

	private Vector<String> vLabsTipo = new Vector<String>();

	private JRadioGroup<?, ?> rgTipo = null;

	private Vector<String> vValsTipoCaixa = new Vector<String>();

	private Vector<String> vLabsTipoCaixa = new Vector<String>();

	private JRadioGroup<?, ?> rgTipoCaixa = null;

	private ListaCampos lcBanco = new ListaCampos( this, "BO" );

	private ListaCampos lcMoeda = new ListaCampos( this, "MA" );

	private ListaCampos lcPlan = new ListaCampos( this, "PN" );
	
	private JTextFieldPad txtCodPlanSL = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );
	
	private ListaCampos lcRestricoes = new ListaCampos( this, "" );
	
	private ListaCampos lcContaVinculada = new ListaCampos( this, "" );
	
	private ListaCampos lcContaCV = new ListaCampos( this, "CV" );

	private ListaCampos lcUsu = new ListaCampos( this, "US" );

	private ListaCampos lcHist = new ListaCampos( this, "HP" );

	private JPanelPad pinCamposGeral = new JPanelPad( 430, 400 );

	private JPanelPad pinCamposContabil = new JPanelPad( 430, 400 );
	
	private JPanelPad pinCamposRestricoes = new JPanelPad( 430, 400 );
	
	private JPanelPad pinCamposContasVinculadas = new JPanelPad( 430, 400 );
	
	private JPanelPad pinCamposFechamentoDet = new JPanelPad( 430, 400 );
	
	private JPanelPad pinCamposFechamentoCab = new JPanelPad( 430, 400 );

	private JPanelPad pnRestricoes = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );
	
	private JPanelPad pnContasVinculadas = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );
	
	private JPanelPad pnFechamento = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinDetRestricoes = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );
	
	private JPanelPad pinDetContasVinculadas = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );
	
	private JPanelPad pinDetFechamento = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinCabFechamento = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );
	
	private JTablePad tbRestricoes = new JTablePad();
	
	private JTablePad tbContasVinculadas = new JTablePad();
	
	private JTablePad tabSaldoLanca = new JTablePad();

	private JScrollPane spnRestricoes = new JScrollPane( tbRestricoes );
	
	private JScrollPane spnContasVinculadas = new JScrollPane( tbContasVinculadas );
	
	private JScrollPane spnSaldoLanca = new JScrollPane( tabSaldoLanca );

	private JPanelPad pinNavRestricoes = new JPanelPad( 680, 30 );
	
	private JPanelPad pinNavContasVinculadas = new JPanelPad( 680, 30 );
	
	private JPanelPad pinNavFechamento = new JPanelPad( 680, 30 );

	private JPanelPad pinNavCamposRestricoes = new JPanelPad( 430, 400 );

	private Navegador navRestricoes = new Navegador( true );
	
	private Navegador navContaVinculada = new Navegador( true );
	
	private JTextFieldPad txtIDUsu = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldFK txtNomeUsu = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodContDeb = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCodContCred = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCodForContab = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCodHistPad = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescHistPad = new JTextFieldFK( JTextFieldPad.TP_STRING, 80, 0 );

	private enum enum_tab_saldo_lanca { CODEMP, CODFILIAL, CODEMPPN, CODFILIALPN, CODPLAN, DATASL, SALDOSL, FECHADO  };
	
	private JButtonPad btCarregaSaldo = new JButtonPad( Icone.novo( "btExecuta.png" ) );
	
	private JButtonPad btSalvarFechamento = new JButtonPad( Icone.novo( "btSalvar.gif" ) );
	
	public FConta() {

		super( false );
		
		setTitulo( "Cadastro de Contas" );
		setAtribos( 50, 50, 420, 380 );
		
		lcRestricoes.setMaster( lcCampos );
		lcContaVinculada.setMaster( lcCampos );
		
		lcPlan.addCarregaListener( this );
		btCarregaSaldo.addActionListener( this );
		btSalvarFechamento.addActionListener( this );
		
		lcCampos.adicDetalhe( lcRestricoes );		
		lcCampos.adicDetalhe( lcContaVinculada );
		
		lcRestricoes.setTabela( tbRestricoes );
		lcContaVinculada.setTabela( tbContasVinculadas );

		lcBanco.add( new GuardaCampo( txtCodBanco, "CodBanco", "Cód.banco", ListaCampos.DB_PK, false ) );
		lcBanco.add( new GuardaCampo( txtDescBanco, "NomeBanco", "Nome do banco", ListaCampos.DB_SI, false ) );
		lcBanco.montaSql( false, "BANCO", "FN" );
		lcBanco.setQueryCommit( false );
		lcBanco.setReadOnly( true );
		txtCodBanco.setTabelaExterna( lcBanco, FBanco.class.getCanonicalName() );

		lcMoeda.add( new GuardaCampo( txtCodMoeda, "CodMoeda", "Cód.mda.", ListaCampos.DB_PK, true ) );
		lcMoeda.add( new GuardaCampo( txtDescMoeda, "SingMoeda", "Descrição da moeda", ListaCampos.DB_SI, false ) );
		lcMoeda.montaSql( false, "MOEDA", "FN" );
		lcMoeda.setQueryCommit( false );
		lcMoeda.setReadOnly( true );
		txtCodMoeda.setTabelaExterna( lcMoeda, FMoeda.class.getCanonicalName() );

		lcPlan.add( new GuardaCampo( txtCodPlan, "CodPlan", "Cód.planj.", ListaCampos.DB_PK, true ) );
		lcPlan.add( new GuardaCampo( txtDescPlan, "DescPlan", "Descrição do plano de contas", ListaCampos.DB_SI, false ) );
		lcPlan.montaSql( false, "PLANEJAMENTO", "FN" );
		lcPlan.setQueryCommit( false );
		lcPlan.setReadOnly( true );
		txtCodPlan.setTabelaExterna( lcPlan, FPlanejamento.class.getCanonicalName() );

		lcUsu.add( new GuardaCampo( txtIDUsu, "IDUsu", "ID", ListaCampos.DB_PK, txtNomeUsu, false ) );
		lcUsu.add( new GuardaCampo( txtNomeUsu, "NomeUsu", "Nome nome do usuário", ListaCampos.DB_SI, false ) );
		lcUsu.montaSql( false, "USUARIO", "SG" );
		lcUsu.setQueryCommit( false );
		lcUsu.setReadOnly( true );
		txtIDUsu.setFK( true );
		txtIDUsu.setNomeCampo( "IDUsu" );
		txtIDUsu.setTabelaExterna( lcUsu, FUsuario.class.getCanonicalName() );
		
		lcContaCV.add( new GuardaCampo( txtNumContaCV, "NumConta", "Num.Conta", ListaCampos.DB_PK, txtDescContaCV, false ) );
		lcContaCV.add( new GuardaCampo( txtDescContaCV, "DescConta", "Descrição da conta", ListaCampos.DB_SI, false ) );
		lcContaCV.montaSql( false, "CONTA", "FN" );
		lcContaCV.setQueryCommit( false );
		lcContaCV.setReadOnly( true );
		txtNumContaCV.setFK( true );
		txtNumContaCV.setNomeCampo( "numconta" );
		txtNumContaCV.setTabelaExterna( lcContaCV, null );
		
		
		lcHist.add( new GuardaCampo( txtCodHistPad, "CodHist", "Cód.hist.", ListaCampos.DB_PK, false ) );
		lcHist.add( new GuardaCampo( txtDescHistPad, "DescHist", "Descrição do historico padrão", ListaCampos.DB_SI, false ) );
		lcHist.montaSql( false, "HISTPAD", "FN" );
		lcHist.setQueryCommit( false );
		lcHist.setReadOnly( true );
		txtCodHistPad.setTabelaExterna( lcHist, FHistPad.class.getCanonicalName() );

		vValsTipo.addElement( "B" );
		vValsTipo.addElement( "C" );
		vLabsTipo.addElement( "Bancos" );
		vLabsTipo.addElement( "Caixa" );
		rgTipo = new JRadioGroup<String, String>( 1, 2, vLabsTipo, vValsTipo );

		vValsTipoCaixa.addElement( "P" );
		vValsTipoCaixa.addElement( "F" );
		vLabsTipoCaixa.addElement( "Previsão" );
		vLabsTipoCaixa.addElement( "Físico" );
		rgTipoCaixa = new JRadioGroup<String, String>( 1, 2, vLabsTipoCaixa, vValsTipoCaixa );

		rgTipoCaixa.setAtivo( false );

		rgTipo.addRadioGroupListener( this );

		/***************
		 * ABA GERAL *
		 ***************/
		setPainel( pinCamposGeral );

		adicTab( "Geral", pinCamposGeral );

		adicCampo( txtNumConta, 7, 20, 140, 20, "NumConta", "Nº da conta", ListaCampos.DB_PK, true );
		adicCampo( txtDescConta, 150, 20, 240, 20, "DescConta", "Descrição da conta", ListaCampos.DB_SI, true );
		adicCampo( txtAgConta, 7, 60, 75, 20, "AgenciaConta", "Agência", ListaCampos.DB_SI, false );
		adicCampo( txtCodBanco, 85, 60, 62, 20, "CodBanco", "Cód.banco", ListaCampos.DB_FK, false );
		adicDescFK( txtDescBanco, 150, 60, 240, 20, "NomeBanco", "Nome do banco" );
		adicCampo( txtPostoConta, 7,100, 50, 20 , "PostoConta", "Posto", ListaCampos.DB_SI, false);
		adicCampo( txtDataConta, 60, 100, 75, 20, "DataConta", "Data", ListaCampos.DB_SI, true );
		adicCampo( txtCodMoeda, 138, 100, 62, 20, "CodMoeda", "Cód.mda.", ListaCampos.DB_FK, true );
		adicDescFK( txtDescMoeda, 203, 100, 188, 20, "SingMoeda", "Descrição da moeda" );
		adicCampo( txtCodPlan, 7, 140, 140, 20, "CodPlan", "Cód.planejamento", ListaCampos.DB_FK, true );
		adicDescFK( txtDescPlan, 150, 140, 240, 20, "DescPlan", "Descrição do planejamento" );

		adicDB( rgTipo, 7, 180, 170, 30, "TipoConta", "Tipo", true );
		adicDB( rgTipoCaixa, 7, 235, 170, 30, "TipoCaixa", "Tipo de Caixa", true );

		adicDB( cbAtivaConta, 190, 185, 50, 20, "ativaconta", "Ativa", true );
		adicDB( chbRestritoTipoMov, 250, 185, 240, 20, "TUSUCONTA", "Permissão", true );

		/******************
		 * ABA CONTABIL *
		 ******************/
		setPainel( pinCamposContabil );

		adicTab( "Contábil", pinCamposContabil );

		adicCampo( txtCodContDeb, 7, 20, 150, 20, "CodContDeb", "Cód.cont.débito", ListaCampos.DB_SI, false );
		adicCampo( txtCodContCred, 160, 20, 150, 20, "CodContCred", "Cód.cont.crédito", ListaCampos.DB_SI, false );
		adicCampo( txtCodHistPad, 7, 60, 80, 20, "CodHist", "Cód.hist.", ListaCampos.DB_FK, txtDescHistPad, false );
		adicDescFK( txtDescHistPad, 90, 60, 300, 20, "DescHist", "Descrição do histórico padrão" );

		setListaCampos( false, "CONTA", "FN" );
		lcCampos.setQueryInsert( false );

		/********************
		 * ABA RESTRIÇÕES *
		 ********************/
		setPainel( pinDetRestricoes, pnRestricoes );

		pinDetRestricoes.setPreferredSize( new Dimension( 430, 80 ) );
		pinDetRestricoes.add( pinNavRestricoes, BorderLayout.SOUTH );
		pinDetRestricoes.add( pinCamposRestricoes, BorderLayout.CENTER );
		
		setListaCampos( lcRestricoes );
		setNavegador( navRestricoes );

		pnRestricoes.add( pinDetRestricoes, BorderLayout.SOUTH );
		pnRestricoes.add( spnRestricoes, BorderLayout.CENTER );
		pinNavRestricoes.adic( navRestricoes, 0, 0, 270, 25 );

		setPainel( pinCamposRestricoes );

		adicCampo( txtIDUsu, 7, 20, 80, 20, "IdUsu", "Id", ListaCampos.DB_PF, txtNomeUsu, true );
		adicDescFK( txtNomeUsu, 90, 20, 250, 20, "NomeUsu", " e nome do usuário" );

		setListaCampos( true, "CONTAUSU", "FN" );
		lcRestricoes.setQueryInsert( false );
		lcRestricoes.setQueryCommit( false );
		lcRestricoes.montaTab();

		txtNumConta.setTabelaExterna( lcRestricoes, null );

		tbRestricoes.setTamColuna( 80, 0 );
		tbRestricoes.setTamColuna( 280, 1 );

		chbRestritoTipoMov.addCheckBoxListener( this );
		
		
		/*********************
		 * Contas Vinculadas *
		 *********************/
		
		setPainel( pinDetContasVinculadas, pnContasVinculadas );
		adicTab( "Contas Vinculadas", pnContasVinculadas );
	
		pinDetContasVinculadas.setPreferredSize( new Dimension( 430, 80 ) );
		pinDetContasVinculadas.add( pinNavContasVinculadas, BorderLayout.SOUTH );
		pinDetContasVinculadas.add( pinCamposContasVinculadas, BorderLayout.CENTER );

		setListaCampos( lcContaVinculada );
		setNavegador( navContaVinculada );

		pnContasVinculadas.add( pinDetContasVinculadas, BorderLayout.SOUTH );
		pnContasVinculadas.add( spnContasVinculadas, BorderLayout.CENTER );
		pinNavContasVinculadas.adic( navContaVinculada, 0, 0, 270, 25 );

		setPainel( pinCamposContasVinculadas ); 

		adicCampo( txtNumContaCV, 7, 20, 80, 20, "NumContaCV", "Num.Conta", ListaCampos.DB_PF, txtDescContaCV, true );
		adicDescFK( txtDescContaCV, 90, 20, 150, 20, "DescConta", "Nome da conta vinculada" );
		adicDB( cbContaCheque, 243, 20, 120, 20, "contacheque", "", false);

		setListaCampos( false, "CONTAVINCULADA", "FN" );
		
		lcContaVinculada.setQueryInsert( false );
		
		lcContaVinculada.setQueryCommit( false );

		lcContaVinculada.montaTab();
		
		txtNumContaCV.setTabelaExterna( lcContaCV, null );
		txtNumContaCV.setNomeCampo( "NumConta" ); 
		
		tbContasVinculadas.setTamColuna( 80, 0 );
		tbContasVinculadas.setTamColuna( 280, 1 );
		
		
		/*********************
		 * Fechamento        *
		 *********************/
		
		setPainel( pinDetFechamento, pnFechamento );
		adicTab( "Fechamento", pnFechamento );
	
		pinDetFechamento.setPreferredSize( new Dimension( 430, 80 ) );
		pinCabFechamento.setPreferredSize( new Dimension( 430, 60 ) );
		
		pinDetFechamento.add( pinNavFechamento, BorderLayout.SOUTH );
		pinDetFechamento.add( pinCamposFechamentoDet, BorderLayout.CENTER );
		
		pinCabFechamento.add( pinCamposFechamentoCab, BorderLayout.CENTER );
		
		pnFechamento.add( pinCabFechamento, BorderLayout.NORTH );
		pnFechamento.add( pinDetFechamento, BorderLayout.SOUTH );
		pnFechamento.add( spnSaldoLanca, BorderLayout.CENTER );
		pinNavFechamento.adic( btSalvarFechamento, 0, 0, 25, 25 );

		setPainel( pinCamposFechamentoDet ); 

		txtDataSL.setSoLeitura( true );
		txtSaldoSL.setSoLeitura( true );
		
		txtDataIniSaldo.setVlrDate( new java.util.Date() );
		txtDataFimSaldo.setVlrDate( new java.util.Date() );
		
		pinCamposFechamentoCab.adic(txtDataIniSaldo, 7, 20, 100, 20, "Data Inicial");
		pinCamposFechamentoCab.adic(txtDataFimSaldo, 110, 20, 100, 20, "Data Final");
		
		pinCamposFechamentoCab.adic( btCarregaSaldo, 220, 15, 30, 30 );
		
		pinCamposFechamentoDet.adic( txtDataSL, 7, 20, 80, 20, "Data"); 		
		pinCamposFechamentoDet.adic( txtSaldoSL, 90, 20, 80, 20, "Saldo" );
		pinCamposFechamentoDet.adic( cbFechado, 173, 20, 120, 20, "" );
 
		montaTabSaldoLanca();
				
		tabSaldoLanca.addMouseListener( this );
		
		
	}

	private void montaTabSaldoLanca() {
				
		tabSaldoLanca.adicColuna( "Cod.Emp." );
		tabSaldoLanca.adicColuna( "Cod.Filial" );
		tabSaldoLanca.adicColuna( "Cod.Emp.PN." );
		tabSaldoLanca.adicColuna( "Cod.Filial PN." );
		tabSaldoLanca.adicColuna( "Cod.Plan." );
		tabSaldoLanca.adicColuna( "Data" );
		tabSaldoLanca.adicColuna( "Saldo" );
		tabSaldoLanca.adicColuna( "Fechado" );
		
		tabSaldoLanca.setColunaInvisivel( enum_tab_saldo_lanca.CODEMP.ordinal() );
		tabSaldoLanca.setColunaInvisivel( enum_tab_saldo_lanca.CODFILIAL.ordinal() );
		tabSaldoLanca.setColunaInvisivel( enum_tab_saldo_lanca.CODEMPPN.ordinal() );
		tabSaldoLanca.setColunaInvisivel( enum_tab_saldo_lanca.CODFILIALPN.ordinal() );
		tabSaldoLanca.setTamColuna( 120, enum_tab_saldo_lanca.CODPLAN.ordinal() );
		tabSaldoLanca.setTamColuna( 80, enum_tab_saldo_lanca.DATASL.ordinal() );
		tabSaldoLanca.setTamColuna( 80, enum_tab_saldo_lanca.SALDOSL.ordinal() );
		tabSaldoLanca.setTamColuna( 50, enum_tab_saldo_lanca.FECHADO.ordinal() );
	
		
	}
	
	private void carregaTabSaldoLanca () {
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			sql.append( "select codemp, codfilial, codemppn, codfilialpn, codplan, datasl, saldosl, fechado " );
			sql.append("from fnsaldolanca ");
			sql.append("where codemp=? and codfilial=? and codemppn=? and codfilialpn=? and codplan=? and datasl between ? and ? ");
			sql.append( "order by datasl" );
			
			ps = con.prepareStatement( sql.toString() );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNSALDOLANCA" ) );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "FNSALDOLANCA" ) );
			ps.setString( 5, txtCodPlan.getVlrString() );
			
			ps.setDate( 6, Funcoes.dateToSQLDate( txtDataIniSaldo.getVlrDate()) );
			ps.setDate( 7, Funcoes.dateToSQLDate( txtDataFimSaldo.getVlrDate()) );
			
			rs = ps.executeQuery( );
			
			int i = 0;
			
			tabSaldoLanca.limpa();
			
			while (rs.next()) {
				
				tabSaldoLanca.adicLinha();
				
				tabSaldoLanca.setValor( rs.getInt( enum_tab_saldo_lanca.CODEMP.name() ), i, enum_tab_saldo_lanca.CODEMP.ordinal() );
				tabSaldoLanca.setValor( rs.getInt( enum_tab_saldo_lanca.CODFILIAL.name() ), i, enum_tab_saldo_lanca.CODFILIAL.ordinal() );
				tabSaldoLanca.setValor( rs.getInt( enum_tab_saldo_lanca.CODEMPPN.name() ), i, enum_tab_saldo_lanca.CODEMPPN.ordinal() );				
				tabSaldoLanca.setValor( rs.getInt( enum_tab_saldo_lanca.CODFILIALPN.name() ), i, enum_tab_saldo_lanca.CODFILIALPN.ordinal() );
				tabSaldoLanca.setValor( rs.getString( enum_tab_saldo_lanca.CODPLAN.name() ), i, enum_tab_saldo_lanca.CODPLAN.ordinal() );
				Date datasl = rs.getDate( enum_tab_saldo_lanca.DATASL.name() );
				tabSaldoLanca.setValor( datasl , i ,enum_tab_saldo_lanca.DATASL.ordinal() );
				tabSaldoLanca.setValor( rs.getBigDecimal( enum_tab_saldo_lanca.SALDOSL.name() ), i, enum_tab_saldo_lanca.SALDOSL.ordinal() );
				tabSaldoLanca.setValor( rs.getString( enum_tab_saldo_lanca.FECHADO.name() ), i, enum_tab_saldo_lanca.FECHADO.ordinal() );
				
				i++;
				
			}
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void valorAlterado( CheckBoxEvent evt ) {

		if ( evt.getCheckBox() == chbRestritoTipoMov ) {
			if ( evt.getCheckBox().isSelected() ) {
				removeTab( "Restrições de Usuário", pnRestricoes );
			}
			else {
				adicTab( "Restrições de Usuário", pnRestricoes );
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		
		lcBanco.setConexao( cn );
		lcMoeda.setConexao( cn );
		lcPlan.setConexao( cn );
		lcRestricoes.setConexao( cn );
		lcUsu.setConexao( cn );
		lcHist.setConexao( cn );
		lcContaCV.setConexao( cn );
		lcContaVinculada.setConexao( cn );
		
	}

	public void valorAlterado( RadioGroupEvent evt ) {

		if ( evt.getSource() == rgTipo ) {
			if ( "C".equals( rgTipo.getVlrString() ) ) {
				rgTipoCaixa.setAtivo( true );
			}
			else {
				rgTipoCaixa.setVlrString( "F" );
				rgTipoCaixa.setAtivo( false );
			}

		}

	}

	public void afterCarrega( CarregaEvent cevt ) {
		if(cevt.getListaCampos()==lcPlan) {
			carregaTabSaldoLanca();
		}
	}

	public void beforeCarrega( CarregaEvent cevt ) {

		// TODO Auto-generated method stub
		
	}

	public void mouseClicked( MouseEvent mevt ) {

		if ( mevt.getSource() == tabSaldoLanca && mevt.getClickCount() == 2 ) {
			carregaSaldoLanca();
		}

		
	}
	
	private void carregaSaldoLanca() {
		
		try {
			
			txtDataSL.setVlrDate( (java.util.Date) tabSaldoLanca.getValor( tabSaldoLanca.getLinhaSel(), enum_tab_saldo_lanca.DATASL.ordinal() ) );
			txtCodPlanSL.setVlrString( (String) tabSaldoLanca.getValor( tabSaldoLanca.getLinhaSel(), enum_tab_saldo_lanca.CODPLAN.ordinal() ) );
			txtSaldoSL.setVlrBigDecimal( (BigDecimal) tabSaldoLanca.getValor( tabSaldoLanca.getLinhaSel(), enum_tab_saldo_lanca.SALDOSL.ordinal() ) );
			cbFechado.setVlrString( (String) tabSaldoLanca.getValor( tabSaldoLanca.getLinhaSel(), enum_tab_saldo_lanca.FECHADO.ordinal() ) );
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void atualizaFechamentoCaixa() {
		
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		
		try {
			
			sql.append( "update fnsaldolanca set fechado=? ");
			sql.append( "where codemp=? and codfilial=? and codemppn=? and codfilialpn=? and codplan=? and datasl");
			
			if("S".equals( cbFechado.getVlrString())) {
				sql.append( "<=? ");
			}
			else {
				sql.append( ">=? ");
			}
			
			sql.append( "and coalesce(fechado,'N')!=? ");
			
			System.out.println("sql:" + sql.toString());
			
			ps = con.prepareStatement( sql.toString() ); 
			
			ps.setString( 1, cbFechado.getVlrString() );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "FNSALDOLANCA" ) );
			ps.setInt( 4, Aplicativo.iCodEmp );
			ps.setInt( 5, lcPlan.getCodFilial() );
			ps.setString( 6, txtCodPlan.getVlrString() );
			
			ps.setDate( 7, Funcoes.dateToSQLDate( txtDataSL.getVlrDate() ) );
			ps.setString( 8, cbFechado.getVlrString() );
			
			ps.executeUpdate();
			
			con.commit();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		carregaTabSaldoLanca();
		
	}

	public void mouseEntered( MouseEvent arg0 ) {

		// TODO Auto-generated method stub
		
	}

	public void mouseExited( MouseEvent arg0 ) {

		// TODO Auto-generated method stub
		
	}

	public void mousePressed( MouseEvent arg0 ) {

		// TODO Auto-generated method stub
		
	}

	public void mouseReleased( MouseEvent arg0 ) {

		// TODO Auto-generated method stub
		
	}
	
	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btCarregaSaldo ) {
			carregaTabSaldoLanca();
		}
		else if ( evt.getSource() == btSalvarFechamento ) {
			atualizaFechamentoCaixa();
		}

		super.actionPerformed( evt );
	}
	
}


