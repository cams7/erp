/**
 * @version 19/02/2008 <BR>
 * @author Setpoint Informática Ltda./Alex Rodrigues <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.grh <BR>
 *         Classe:
 * @(#)FEmpregado.java <BR>
 * 
 *                     Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                     modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                     na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                     Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                     sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                     Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                     Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                     de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                     Tela de cadastro de cadidatos.
 * 
 */

package org.freedom.modulos.grh.view.frame.crud.tabbed;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FTabDados;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.cfg.view.frame.crud.plain.FEstadoCivil;
import org.freedom.modulos.grh.view.frame.crud.plain.FCaracteristica;
import org.freedom.modulos.grh.view.frame.crud.plain.FCurso;
import org.freedom.modulos.grh.view.frame.crud.plain.FFuncao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.bmps.Icone;

public class FCandidato extends FTabDados implements CarregaListener, PostListener {

	private static final long serialVersionUID = 1L;

	private final JPanelPad panelGeral = new JPanelPad();

	private final JPanelPad panelOutrosEmpregos = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	private final JPanelPad panelOBS = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	private final JPanelPad panelCurso = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad panelCursoCampos = new JPanelPad( 0, 80 );

	private final JPanelPad panelCaracteristica = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad panelCaracteristicaCampos = new JPanelPad( 0, 80 );

	private final JPanelPad panelFuncao = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad panelFuncaoCampos = new JPanelPad( 0, 80 );

	private final JPanelPad panelHistorico = new JPanelPad( new BorderLayout() );

	private final JPanelPad pnHistorico = new JPanelPad( new BorderLayout() );

	private final JPanelPad pnStatus = new JPanelPad( 0, 500 );

	private JRadioGroup<String, String> rgStatus = null;

	// GERAL

	private final JTextFieldPad txtCodCand = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtNomeCand = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JRadioGroup<String, String> rgSexo = null;

	private final JTextFieldPad txtCPFCand = new JTextFieldPad( JTextFieldPad.TP_STRING, 11, 0 );

	private final JTextFieldPad txtRGCand = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldPad txtPISPasepCand = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private final JTextFieldPad txtCTPSCand = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private final JTextFieldPad txtSSPCand = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldPad txtTituloCand = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private final JTextFieldPad txtPretensaoCand = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private final JTextFieldPad txtCodEstCivilCand = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private final JTextFieldFK txtDescEstCivilCand = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtEndCand = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtNumCand = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtNroFilhos = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private final JTextFieldPad txtBairroCand = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private final JTextFieldPad txtCidCand = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private final JTextFieldPad txtUfCand = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private final JTextFieldPad txtCepCand = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private final JTextFieldPad txtDDDCand = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private final JTextFieldPad txtFoneCand = new JTextFieldPad( JTextFieldPad.TP_STRING, 12, 0 );

	private final JTextFieldPad txtCelCand = new JTextFieldPad( JTextFieldPad.TP_STRING, 12, 0 );

	private final JTextFieldPad txtNascimentoCand = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtEmailCand = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextAreaPad txaOutrosempregos = new JTextAreaPad( 1000 );

	private final JTextAreaPad txaOBS = new JTextAreaPad( 1000 );

	// FIM GERAL.

	// CURSOS

	private final JTextFieldPad txtCodCursoCand = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private final JTextFieldFK txtDescCursoCand = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	// FIM CURSOS

	// CARACTERISTICAS

	private final JTextFieldPad txtCodCaracCand = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private final JTextFieldFK txtDescCaracCand = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	// FIM CARACTERISTICAS

	// FUNÇÕES

	private final JTextFieldPad txtSeqFuncaoCand = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtCodFuncaoCand = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldFK txtDescFuncaoCand = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	// FIM FUNÇÕES.

	private final JTablePad tabCurso = new JTablePad();

	private final JTablePad tabCaracteristica = new JTablePad();

	private final JTablePad tabFuncao = new JTablePad();

	private final JTablePad tabHistorico = new JTablePad();

	private final ListaCampos lcEstadoCivil = new ListaCampos( this, "ES" );

	private final ListaCampos lcCurso = new ListaCampos( this, "CS" );

	private final ListaCampos lcCursoCand = new ListaCampos( this );

	private final ListaCampos lcCaracteristica = new ListaCampos( this, "CT" );

	private final ListaCampos lcCaracteristicaCand = new ListaCampos( this );

	private final ListaCampos lcFuncao = new ListaCampos( this, "FC" );

	private final ListaCampos lcFuncaoCand = new ListaCampos( this );

	private final Navegador navCurso = new Navegador( true );

	private final Navegador navCaracteristica = new Navegador( true );

	private final Navegador navFuncao = new Navegador( true );

	private final JCheckBoxPad cbIsencTransp = new JCheckBoxPad( "Possui isenção tarifaria", "S", "N" );

	private final ImageIcon imgEncaminhado = Icone.novo( "clEncaminhado.gif" );

	private final ImageIcon imgDisponivel = Icone.novo( "clDisponivel.gif" );

	private final ImageIcon imgEfetivado = Icone.novo( "clEfetivado.gif" );

	private final ImageIcon imgInativo = Icone.novo( "clInativo.gif" );

	private final ImageIcon imgEntrevistado = Icone.novo( "clEntrevistado.gif" );

	private final ImageIcon imgDesligado = Icone.novo( "clDesligado.gif" );

	public FCandidato() {

		super( true );
		setTitulo( "Cadastro de Candidatos" );
		setAtribos( 50, 50, 560, 400 );

		cbIsencTransp.setVlrString( "N" );

		lcCursoCand.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcCursoCand );
		lcCursoCand.setTabela( tabCurso );

		lcCaracteristicaCand.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcCaracteristicaCand );
		lcCaracteristicaCand.setTabela( tabCaracteristica );

		lcFuncaoCand.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcFuncaoCand );
		lcFuncaoCand.setTabela( tabFuncao );

		montaRadioGroups();
		montaRadioStatus();
		montaListaCampos();
		montaTela();

		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );

		setImprimir( true );

		lcCampos.addCarregaListener( this );
		lcCampos.addPostListener( this );
	}

	private void montaRadioGroups() {

		Vector<String> vLabs = new Vector<String>();
		Vector<String> vVals = new Vector<String>();

		vLabs.addElement( "Masculino" );
		vLabs.addElement( "Feminino" );
		vVals.addElement( "M" );
		vVals.addElement( "F" );

		rgSexo = new JRadioGroup<String, String>( 2, 1, vLabs, vVals );
		rgSexo.setVlrString( "M" );
	}

	private void montaRadioStatus() {

		Vector<String> vLabs = new Vector<String>();
		Vector<String> vVals = new Vector<String>();

		vLabs.addElement( "Inativo" );
		vLabs.addElement( "Disponivel" );
		vLabs.addElement( "Encaminhado" );
		vLabs.addElement( "Efetivado" );
		vLabs.addElement( "Eliminado de seleção" );
		vLabs.addElement( "Empregado" );
		vLabs.addElement( "Entrevistado" );
		vLabs.addElement( "Desligado" );
		vVals.addElement( "IN" );
		vVals.addElement( "DI" );
		vVals.addElement( "EN" );
		vVals.addElement( "EF" );
		vVals.addElement( "EL" );
		vVals.addElement( "EM" );
		vVals.addElement( "EV" );
		vVals.addElement( "DL" );

		rgStatus = new JRadioGroup<String, String>( 2, 4, vLabs, vVals );
		rgStatus.setVlrString( "DI" );
	}

	private void montaListaCampos() {

		lcEstadoCivil.add( new GuardaCampo( txtCodEstCivilCand, "CodEstCivil", "Cód.est.cívil", ListaCampos.DB_PK, true ) );
		lcEstadoCivil.add( new GuardaCampo( txtDescEstCivilCand, "DescEstCivil", "Descrição do estado cívil", ListaCampos.DB_SI, false ) );
		lcEstadoCivil.montaSql( false, "ESTCIVIL", "SG" );
		lcEstadoCivil.setQueryCommit( false );
		lcEstadoCivil.setReadOnly( true );
		txtCodEstCivilCand.setTabelaExterna( lcEstadoCivil, FEstadoCivil.class.getCanonicalName() );

		lcCurso.add( new GuardaCampo( txtCodCursoCand, "CodCurso", "Cód.curso", ListaCampos.DB_PK, txtDescCursoCand, false ) );
		lcCurso.add( new GuardaCampo( txtDescCursoCand, "DescCurso", "Descrição do curso", ListaCampos.DB_SI, false ) );
		lcCurso.montaSql( false, "CURSO", "RH" );
		lcCurso.setReadOnly( true );
		lcCurso.setQueryCommit( false );
		txtCodCursoCand.setTabelaExterna( lcCurso, FCurso.class.getCanonicalName() );
		txtCodCursoCand.setFK( true );
		txtCodCursoCand.setListaCampos( lcCurso );
		txtDescCursoCand.setListaCampos( lcCurso );

		lcCaracteristica.add( new GuardaCampo( txtCodCaracCand, "CodCarac", "Cód.Carac.", ListaCampos.DB_PK, txtDescCaracCand, false ) );
		lcCaracteristica.add( new GuardaCampo( txtDescCaracCand, "DescCarac", "Descrição da característica", ListaCampos.DB_SI, false ) );
		lcCaracteristica.montaSql( false, "Caracteristica", "RH" );
		lcCaracteristica.setReadOnly( true );
		lcCaracteristica.setQueryCommit( false );
		txtCodCaracCand.setTabelaExterna( lcCaracteristica, FCaracteristica.class.getCanonicalName() );
		txtCodCaracCand.setFK( true );
		txtCodCaracCand.setListaCampos( lcCaracteristica );
		txtDescCaracCand.setListaCampos( lcCaracteristica );

		lcFuncao.add( new GuardaCampo( txtCodFuncaoCand, "CodFunc", "Cód.função", ListaCampos.DB_PK, txtDescFuncaoCand, false ) );
		lcFuncao.add( new GuardaCampo( txtDescFuncaoCand, "DescFunc", "Descrição da função", ListaCampos.DB_SI, false ) );
		lcFuncao.montaSql( true, "FUNCAO", "RH" );
		lcFuncao.setReadOnly( true );
		lcFuncao.setQueryCommit( false );
		txtCodFuncaoCand.setTabelaExterna( lcFuncao, FFuncao.class.getCanonicalName() );
		txtCodFuncaoCand.setFK( true );
		txtCodFuncaoCand.setListaCampos( lcFuncao );
		txtDescFuncaoCand.setListaCampos( lcFuncao );
	}

	private void montaTela() {

		txtCPFCand.setMascara( JTextFieldPad.MC_CPF );
		txtRGCand.setMascara( JTextFieldPad.MC_RG );
		txtCepCand.setMascara( JTextFieldPad.MC_CEP );
		txtFoneCand.setMascara( JTextFieldPad.MC_FONE );
		txtCelCand.setMascara( JTextFieldPad.MC_CELULAR );

		// Aba geral

		adicTab( "Geral", panelGeral );
		setPainel( panelGeral );

		adicCampo( txtCodCand, 7, 20, 70, 20, "CodCand", "Cód.Cand.", ListaCampos.DB_PK, true );
		adicCampo( txtNomeCand, 80, 20, 310, 20, "NomeCand", "Nome do candidato", ListaCampos.DB_SI, true );

		adicDB( rgSexo, 394, 20, 122, 61, "SexoCand", "Sexo", false );

		setPainel( pnStatus );
		adicDB( rgStatus, 7, 25, 520, 60, "STCAND", "Situação atual do candidato", false );

		setPainel( panelGeral );
		adicCampo( txtCodEstCivilCand, 7, 60, 70, 20, "CodEstCivil", "Cód.Civil", ListaCampos.DB_FK, txtDescEstCivilCand, false );
		adicDescFK( txtDescEstCivilCand, 80, 60, 310, 20, "DescEstCivil", "Descrição do estado civil" );

		adicCampo( txtRGCand, 7, 100, 140, 20, "RgCand", "RG", ListaCampos.DB_SI, false );
		adicCampo( txtSSPCand, 150, 100, 70, 20, "SSPCand", "SSP", ListaCampos.DB_SI, false );

		adicCampo( txtCPFCand, 223, 100, 140, 20, "CpfCand", "CPF", ListaCampos.DB_SI, false );
		adicCampo( txtTituloCand, 366, 100, 150, 20, "TitEleitCand", "Titulo de eleitor", ListaCampos.DB_SI, false );

		adicCampo( txtPISPasepCand, 7, 140, 140, 20, "PISPasepCand", "PIS/Pasep", ListaCampos.DB_SI, false );
		adicCampo( txtCTPSCand, 150, 140, 205, 20, "CTPSCand", "CTPS", ListaCampos.DB_SI, false );

		adicDB( cbIsencTransp, 358, 140, 200, 20, "IsencTransp", "Transporte", false );

		adicCampo( txtNascimentoCand, 7, 180, 100, 20, "DtNascCand", "Dt. Nascimento", ListaCampos.DB_SI, false );
		adicCampo( txtPretensaoCand, 110, 180, 100, 20, "PretensaoSal", "Pret. salarial", ListaCampos.DB_SI, false );
		adicCampo( txtNroFilhos, 213, 180, 80, 20, "NroFilhos", "Nro. Filhos", ListaCampos.DB_SI, false );

		adicCampo( txtDDDCand, 296, 180, 35, 20, "DDDCand", "DDD", ListaCampos.DB_SI, false );
		adicCampo( txtFoneCand, 334, 180, 89, 20, "FoneCand", "Fone", ListaCampos.DB_SI, false );
		adicCampo( txtCelCand, 426, 180, 90, 20, "CelCand", "Celular", ListaCampos.DB_SI, false );

		adicCampo( txtEndCand, 7, 220, 311, 20, "EndCand", "Endereço", ListaCampos.DB_SI, false );
		adicCampo( txtNumCand, 321, 220, 47, 20, "NumCand", "Número", ListaCampos.DB_SI, false );
		adicCampo( txtBairroCand, 371, 220, 145, 20, "BairCand", "Bairro", ListaCampos.DB_SI, false );

		adicCampo( txtCidCand, 7, 260, 145, 20, "CidCand", "Cidade", ListaCampos.DB_SI, false );
		adicCampo( txtUfCand, 155, 260, 27, 20, "UfCand", "UF", ListaCampos.DB_SI, false );
		adicCampo( txtCepCand, 185, 260, 67, 20, "CepCand", "CEP", ListaCampos.DB_SI, false );
		adicCampo( txtEmailCand, 255, 260, 261, 20, "EmailCand", "e-mail", ListaCampos.DB_SI, false );

		// Fim da aba geral

		// Aba outros empregos

		adicTab( "Empregos", panelOutrosEmpregos );
		setPainel( panelOutrosEmpregos );

		adicDBLiv( txaOutrosempregos, "OutrosEmpregos", "Outros empregos", false );
		panelOutrosEmpregos.add( new JScrollPane( txaOutrosempregos ) );

		setListaCampos( true, "CANDIDATO", "RH" );
		lcCampos.setQueryInsert( false );

		// Fim da aba outros empregos

		// Aba outros OBS

		adicTab( "Observações", panelOBS );
		setPainel( panelOBS );

		adicDBLiv( txaOBS, "OBSCAND", "Observações", false );
		panelOBS.add( new JScrollPane( txaOBS ) );

		setListaCampos( true, "CANDIDATO", "RH" );
		lcCampos.setQueryInsert( false );

		// Fim da aba outros empregos

		// Aba cursos

		adicTab( "Cursos", panelCurso );
		setPainel( panelCurso );

		setListaCampos( lcCursoCand );
		setNavegador( navCurso );
		navCurso.setAtivo( 6, false );

		panelCurso.add( new JScrollPane( tabCurso ), BorderLayout.CENTER );
		panelCurso.add( panelCursoCampos, BorderLayout.SOUTH );

		setPainel( panelCursoCampos );

		adicCampo( txtCodCursoCand, 7, 20, 90, 20, "CodCurso", "Cód.curso", ListaCampos.DB_PF, txtDescCursoCand, false );
		adicDescFK( txtDescCursoCand, 100, 20, 300, 20, "DescCurso", "Descrição do curso" );
		adic( navCurso, 0, 50, 270, 25 );
		setListaCampos( false, "CANDIDATOCURSO", "RH" );
		lcCursoCand.setQueryInsert( false );
		lcCursoCand.setQueryCommit( false );
		lcCursoCand.montaTab();

		tabCurso.setTamColuna( 335, 1 );

		// Fim da aba cursos

		// Aba Características

		adicTab( "Características", panelCaracteristica );

		setListaCampos( lcCaracteristicaCand );
		setNavegador( navCaracteristica );
		navCaracteristica.setAtivo( 6, false );

		panelCaracteristica.add( new JScrollPane( tabCaracteristica ), BorderLayout.CENTER );
		panelCaracteristica.add( panelCaracteristicaCampos, BorderLayout.SOUTH );

		setPainel( panelCaracteristicaCampos );

		adicCampo( txtCodCaracCand, 7, 20, 90, 20, "CodCarac", "Cód.Carac.", ListaCampos.DB_PF, txtDescCaracCand, false );
		adicDescFK( txtDescCaracCand, 100, 20, 400, 20, "DescCarac", "Descrição da característica" );
		adic( navCaracteristica, 0, 50, 270, 25 );
		setListaCampos( false, "CANDIDATOCARAC", "RH" );
		lcCaracteristicaCand.setQueryInsert( false );
		lcCaracteristicaCand.setQueryCommit( false );
		lcCaracteristicaCand.montaTab();

		tabCaracteristica.setTamColuna( 335, 1 );

		// Fim da aba características

		// Aba funções

		adicTab( "Funções", panelFuncao );

		setListaCampos( lcFuncaoCand );
		setNavegador( navFuncao );
		navFuncao.setAtivo( 6, false );

		panelFuncao.add( new JScrollPane( tabFuncao ), BorderLayout.CENTER );
		panelFuncao.add( panelFuncaoCampos, BorderLayout.SOUTH );

		setPainel( panelFuncaoCampos );

		adicCampo( txtSeqFuncaoCand, 7, 20, 40, 20, "SeqCandFunc", "Seq.", ListaCampos.DB_PK, true );
		adicCampo( txtCodFuncaoCand, 50, 20, 90, 20, "CodFunc", "Cód.função", ListaCampos.DB_PF, txtDescFuncaoCand, true );
		adicDescFK( txtDescFuncaoCand, 143, 20, 357, 20, "DescFunc", "Descrição da função" );
		adic( navFuncao, 0, 50, 270, 25 );
		setListaCampos( false, "CANDIDATOFUNC", "RH" );
		lcFuncaoCand.setQueryInsert( true );
		lcFuncaoCand.setPodeIns( true );
		lcFuncaoCand.setQueryCommit( false );
		lcFuncaoCand.montaTab();

		tabFuncao.setTamColuna( 35, 0 );
		tabFuncao.setTamColuna( 300, 2 );

		// Fim da aba funções

		// Aba Histórico

		adicTab( "Histórico", panelHistorico );

		panelHistorico.add( pnHistorico, BorderLayout.NORTH );
		setPainel( pnHistorico );
		pnHistorico.setPreferredSize( new Dimension( 0, 205 ) );

		tabHistorico.adicColuna( "" );
		tabHistorico.adicColuna( "Data" );
		tabHistorico.adicColuna( "Função" );
		tabHistorico.adicColuna( "Empresa" );

		tabHistorico.setTamColuna( 30, 0 );
		tabHistorico.setTamColuna( 70, 1 );
		tabHistorico.setTamColuna( 200, 2 );
		tabHistorico.setTamColuna( 220, 3 );

		pnHistorico.add( new JScrollPane( tabHistorico ) );

		setPainel( pnStatus );
		panelHistorico.add( pnStatus, BorderLayout.CENTER );

		// Fim da aba histórico

	}

	private void montaTabHistorico() {

		StringBuffer sql = new StringBuffer();

		try {

			sql.append( "SELECT CS.stcand, CS.dtstatus, coalesce(fu.descfunc,'') as descfunc," );
			sql.append( "coalesce(EM.nomeempr,'') as nomeempr " );
			sql.append( "FROM rhcandidatostatus CS full outer join rhvagacandidato vc on " );
			sql.append( "VC.codemp=CS.codempvg AND VC.codfilial=CS.codfilialvg AND VC.codvaga=CS.codvaga " );
			sql.append( "AND VC.codempca=CS.codemp AND VC.codfilialca=CS.codfilial AND VC.codcand=CS.codcand " );
			sql.append( "left outer join rhvaga vg on " );
			sql.append( "VG.codemp=CS.codempvg AND VG.codfilial=CS.codfilialvg AND VG.codvaga=CS.codvaga " );
			sql.append( "left outer join rhempregador em on " );
			sql.append( "EM.codemp=VG.codempem AND EM.codfilial=VG.codfilialem AND EM.codempr=VG.codempr " );
			sql.append( "left outer join rhfuncao fu on " );
			sql.append( "FU.codemp=VG.codempfc AND FU.codfilial=VG.codfilialfc AND FU.codfunc=VG.codfunc " );
			sql.append( "where CS.codemp=? AND CS.codfilial=? AND CS.codcand=?" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "RHCANDIDATOSTATUS" ) );
			ps.setInt( 3, txtCodCand.getVlrInteger().intValue() );

			ResultSet rs = ps.executeQuery();

			tabHistorico.limpa();

			while ( rs.next() ) {
				Vector<Object> vVals = new Vector<Object>();

				if ( rs.getString( "STCAND" ).equals( "EN" ) ) {
					vVals.addElement( imgEncaminhado );
				}
				else if ( rs.getString( "STCAND" ).equals( "EF" ) ) {
					vVals.addElement( imgEfetivado );
				}
				else if ( rs.getString( "STCAND" ).equals( "IN" ) ) {
					vVals.addElement( imgInativo );
				}
				else if ( rs.getString( "STCAND" ).equals( "EV" ) ) {
					vVals.addElement( imgEntrevistado );
				}
				else if ( rs.getString( "STCAND" ).equals( "DL" ) ) {
					vVals.addElement( imgDesligado );
				}
				else {
					vVals.addElement( imgDisponivel );
				}

				vVals.addElement( rs.getDate( "DTSTATUS" ) );
				vVals.addElement( rs.getString( "DESCFUNC" ) == null ? "" : rs.getString( "DESCFUNC" ) );
				vVals.addElement( rs.getString( "NOMEEMPR" ) == null ? "" : rs.getString( "NOMEEMPR" ) );

				tabHistorico.adicLinha( vVals );
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private void bloqueiaStatus() {

		if ( rgStatus.getVlrString().equals( "DI" ) ) {
			rgStatus.setAtivo( true );
		}
		else if ( rgStatus.getVlrString().equals( "DL" ) ) {
			rgStatus.setAtivo( false );
			rgStatus.setAtivo( 0, true );
			rgStatus.setAtivo( 6, true );
			rgStatus.setAtivo( 1, true );

		}
		else {
			rgStatus.setAtivo( false );
			rgStatus.setAtivo( 0, true );
			rgStatus.setAtivo( 4, true );
			rgStatus.setAtivo( 6, true );
			rgStatus.setAtivo( 7, true );
		}
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		try {
			DLRCandidato dl = new DLRCandidato( this, con );
			dl.setVisible( true );

			if ( dl.OK ) {

				HashMap<String, Object> hParam = dl.getParams();
				hParam.put( "CODEMP", Aplicativo.iCodEmp );
				hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "RHCANDIDATO" ) );

				String dea = null;
				String de = (String) hParam.get( "DE" );
				String a = (String) hParam.get( "A" );

				if ( de != null && de.trim().length() > 0 && a != null && a.trim().length() > 0 ) {
					dea = "AND C.NOMECAND BETWEEN '" + de + "' AND '" + a + "' ";
				}

				String strCaracteristicas = (String) hParam.get( "CARACTERISTICAS" );

				StringBuffer sql = new StringBuffer();
				sql.append( "SELECT C.CODCAND, C.NOMECAND, C.DDDCAND, C.FONECAND, C.SEXOCAND, C.DTNASCCAND, C.PRETENSAOSAL " );
				sql.append( "FROM RHCANDIDATO C " );
				sql.append( "WHERE C.CODEMP=? AND C.CODFILIAL=? " );
				if ( dea != null ) {
					sql.append( dea );
				}
				if ( "".equals( strCaracteristicas ) ) {
					sql.append( "AND NOT EXISTS (" );
					sql.append( "SELECT CC.CODCAND FROM RHCANDIDATOCARAC CC " );
					sql.append( "WHERE C.CODFILIAL=CC.CODFILIAL AND C.CODCAND=CC.CODCAND AND C.CODCAND=CC.CODCAND ) " );
				}
				else {
					sql.append( "AND EXISTS (" );
					sql.append( "SELECT CC.CODCAND FROM RHCANDIDATOCARAC CC " );
					sql.append( "WHERE C.CODFILIAL=CC.CODFILIAL AND C.CODCAND=CC.CODCAND AND C.CODCAND=CC.CODCAND AND " );
					sql.append( "C.CODEMP=CC.CODEMPCT AND C.CODFILIAL=CC.CODFILIALCT AND CC.CODCARAC IN (" + strCaracteristicas + ") ) " );
				}

				sql.append( "ORDER BY " + hParam.get( "ORDEM" ) );

				PreparedStatement ps = con.prepareStatement( sql.toString() );

				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "RHCANDIDATO" ) );

				ResultSet rs = ps.executeQuery();

				FPrinterJob dlGr = new FPrinterJob( "relatorios/grhCandidato.jasper", "Lista de Candidatos", null, rs, hParam, this );

				if ( bVisualizar==TYPE_PRINT.VIEW ) {
					dlGr.setVisible( true );
				}
				else {
					try {
						JasperPrintManager.printReport( dlGr.getRelatorio(), true );
					} catch ( Exception e ) {
						e.printStackTrace();
						Funcoes.mensagemErro( this, "Erro na geração do relátorio!" + e.getMessage(), true, con, e );
					}
				}
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	@ Override
	public void actionPerformed( ActionEvent e ) {

		super.actionPerformed( e );

		if ( e.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
		else if ( e.getSource() == btImp ) {
			imprimir( TYPE_PRINT.PRINT);
		}
	}

	public void afterCarrega( CarregaEvent e ) {

		if ( e.getListaCampos() == lcCampos ) {
			montaTabHistorico();
			bloqueiaStatus();
		}
	}

	public void beforeCarrega( CarregaEvent e ) {

	}

	public void afterPost( PostEvent e ) {

		if ( e.getListaCampos() == lcCampos ) {
			lcCampos.carregaDados();
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcEstadoCivil.setConexao( cn );
		lcCurso.setConexao( cn );
		lcCaracteristica.setConexao( cn );
		lcFuncao.setConexao( cn );
		lcCursoCand.setConexao( cn );
		lcCaracteristicaCand.setConexao( cn );
		lcFuncaoCand.setConexao( cn );
	}

	private class DLRCandidato extends FFDialogo {

		private static final long serialVersionUID = 1l;

		private final JPanelPad panelCaracteristicas = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

		private final JPanelPad panelItensCaracteristicas = new JPanelPad();

		private final JTextFieldPad txtCodCand = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

		private final JTextFieldPad txtNomeCand = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

		private final JTextFieldPad txtNomeDe = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

		private final JTextFieldPad txtNomeA = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

		private JRadioGroup<String, String> rgOrdem = null;

		private HashMap<String, JCheckBoxPad> caracteristicas = new HashMap<String, JCheckBoxPad>();

		DLRCandidato( Component cOrig, DbConnection cn ) {

			super( cOrig );
			setTitulo( "Relatório de Candidatos" );
			setAtribos( 355, 350 );
			setConexao( cn );

			adic( new JLabelPad( "Ordem :" ), 10, 10, 320, 20 );

			Vector<String> vLabs = new Vector<String>();
			Vector<String> vVals = new Vector<String>();

			vLabs.addElement( "Nome" );
			vLabs.addElement( "Código" );
			vVals.addElement( "C.NOMECAND" );
			vVals.addElement( "C.CODCAND" );
			rgOrdem = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
			rgOrdem.setVlrString( "N" );

			adic( rgOrdem, 10, 30, 320, 30 );

			JLabelPad nome = new JLabelPad( "Nome:", SwingConstants.CENTER );
			nome.setOpaque( true );
			JLabelPad linha = new JLabelPad();
			linha.setBorder( BorderFactory.createEtchedBorder() );

			adic( nome, 20, 60, 60, 20 );
			adic( linha, 10, 70, 320, 50 );
			adic( new JLabelPad( "de", SwingConstants.CENTER ), 20, 90, 30, 20 );
			adic( txtNomeDe, 50, 90, 120, 20 );
			adic( new JLabelPad( "a", SwingConstants.CENTER ), 170, 90, 30, 20 );
			adic( txtNomeA, 200, 90, 120, 20 );

			panelCaracteristicas.setBorder( BorderFactory.createTitledBorder( "Caracteristicas" ) );
			panelCaracteristicas.add( new JScrollPane( panelItensCaracteristicas ), BorderLayout.CENTER );
			panelItensCaracteristicas.tiraBorda();
			adic( panelCaracteristicas, 10, 130, 320, 120 );

			montaCaracteristicas();
		}

		private void montaCaracteristicas() {

			for ( JCheckBoxPad cb : caracteristicas.values() ) {
				panelItensCaracteristicas.remove( cb );
			}

			carregaCaracteristicas();

			if ( caracteristicas.isEmpty() ) {
				return;
			}

			int y = 5;
			for ( JCheckBoxPad cb : caracteristicas.values() ) {
				panelItensCaracteristicas.adic( cb, 5, y, 290, 20 );
				y += 20;
				cb.setSelected( true );
			}

			panelItensCaracteristicas.setPreferredSize( new Dimension( 290, y + 5 ) );
			panelCaracteristicas.setVisible( true );
		}

		private void carregaCaracteristicas() {

			try {

				caracteristicas.clear();

				StringBuilder sql = new StringBuilder();
				sql.append( "SELECT C.CODCARAC, C.DESCCARAC " );
				sql.append( "FROM RHCARACTERISTICA C " );
				sql.append( "WHERE C.CODEMP=? AND C.CODFILIAL=? " );
				sql.append( "ORDER BY DESCCARAC " );

				PreparedStatement ps = con.prepareStatement( sql.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "RHCARACTERISTICA" ) );

				ResultSet rs = ps.executeQuery();

				while ( rs.next() ) {
					caracteristicas.put( rs.getString( "CODCARAC" ).trim(), new JCheckBoxPad( rs.getString( "DESCCARAC" ).trim()/* + "(" + rs.getString( "CODCARAC" ).trim() + ")" */, "S", "N" ) );
				}

				rs.close();
				ps.close();

				con.commit();
			} catch ( SQLException err ) {
				err.printStackTrace();
				Funcoes.mensagemErro( this, "Erro ao carregar caracteristicas!\n" + err.getMessage(), true, con, err );
			}
		}

		public HashMap<String, Object> getParams() {

			HashMap<String, Object> params = new HashMap<String, Object>();

			String strCaracteristicas = "";
			for ( Entry<String, JCheckBoxPad> ek : caracteristicas.entrySet() ) {
				JCheckBoxPad cb = ek.getValue();
				if ( "S".equals( cb.getVlrString() ) ) {
					if ( strCaracteristicas.trim().length() > 0 ) {
						strCaracteristicas += ",";
					}
					strCaracteristicas += "'" + ek.getKey() + "'";
				}
			}

			params.put( "ORDEM", rgOrdem.getVlrString() );
			params.put( "DE", txtNomeDe.getVlrString() );
			params.put( "A", txtNomeA.getVlrString() );
			params.put( "CARACTERISTICAS", strCaracteristicas );

			return params;
		}
	}

}
