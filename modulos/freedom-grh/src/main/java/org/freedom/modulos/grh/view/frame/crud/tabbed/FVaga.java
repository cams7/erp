/**
 * @version 21/02/2008 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.grh <BR>
 *         Classe:
 * @(#)FVaga.java <BR>
 * 
 *                Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                Tela de cadastro de vagas.
 * 
 */

package org.freedom.modulos.grh.view.frame.crud.tabbed;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FTabDados;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.grh.view.frame.crud.plain.FCaracteristica;
import org.freedom.modulos.grh.view.frame.crud.plain.FCurso;
import org.freedom.modulos.grh.view.frame.crud.plain.FFuncao;
import org.freedom.modulos.grh.view.frame.crud.plain.FTurnos;

import java.util.HashMap;

import javax.swing.JScrollPane;

import net.sf.jasperreports.engine.JasperPrintManager;

public class FVaga extends FTabDados {

	private static final long serialVersionUID = 1L;

	private final JPanelPad panelGeral = new JPanelPad();

	private final JPanelPad panelCurso = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad panelCursoCampos = new JPanelPad( 0, 80 );

	private final JPanelPad panelCaracQuali = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad panelCaracQualiCampos = new JPanelPad( 0, 80 );

	private final JPanelPad panelCaracRestr = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad panelCaracRestrCampos = new JPanelPad( 0, 80 );

	// GERAL

	private final JTextFieldPad txtCodVaga = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtCodEmpr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldFK txtNomeEmpr = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private final JTextFieldPad txtCodTurnoVaga = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldFK txtDescTurnoVaga = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private final JTextFieldPad txtFaixaSalIni = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private final JTextFieldPad txtFaixaSalFim = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private final JTextFieldPad txtCodCursoVaga = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private final JTextFieldFK txtDescCursoVaga = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCodCaracVagaQ = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private final JTextFieldFK txtDescCaracVagaQ = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCodCaracVagaR = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private final JTextFieldFK txtDescCaracVagaR = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCodFuncaoVaga = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private final JTextFieldFK txtDescFuncaoVaga = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTablePad tabCurso = new JTablePad();

	private final JTablePad tabCaracteristicaQ = new JTablePad();

	private final JTablePad tabCaracteristicaR = new JTablePad();

	private final JTablePad tabFuncao = new JTablePad();

	private final ListaCampos lcEmpregador = new ListaCampos( this, "EM" );

	private final ListaCampos lcCurso = new ListaCampos( this, "CS" );

	private final ListaCampos lcVagaCurso = new ListaCampos( this );

	private final ListaCampos lcTurno = new ListaCampos( this, "TN" );

	private final ListaCampos lcVagaCaracteristicaQ = new ListaCampos( this );

	private final ListaCampos lcVagaCaracteristicaR = new ListaCampos( this );

	private final ListaCampos lcCaracteristicaQ = new ListaCampos( this, "CT" );

	private final ListaCampos lcCaracteristicaR = new ListaCampos( this, "CT" );

	private final ListaCampos lcFuncao = new ListaCampos( this, "FC" );

	private final Navegador navCurso = new Navegador( true );

	private final Navegador navCaracteristicaQ = new Navegador( true );

	private final Navegador navCaracteristicaR = new Navegador( true );

	private final Navegador navFuncao = new Navegador( true );

	public FVaga() {

		super( false );
		setTitulo( "Cadastro de Vagas" );
		setAtribos( 50, 50, 580, 270 );

		lcVagaCaracteristicaQ.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcVagaCaracteristicaQ );
		lcVagaCaracteristicaQ.setTabela( tabCaracteristicaQ );

		lcVagaCaracteristicaR.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcVagaCaracteristicaR );
		lcVagaCaracteristicaR.setTabela( tabCaracteristicaR );

		lcVagaCurso.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcVagaCurso );
		lcVagaCurso.setTabela( tabCurso );

		montaListaCampos();
		montaTela();

		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );

		setImprimir( true );
	}

	private void montaListaCampos() {

		lcEmpregador.add( new GuardaCampo( txtCodEmpr, "CodEmpr", "Cód.Empreg.", ListaCampos.DB_PK, true ) );
		lcEmpregador.add( new GuardaCampo( txtNomeEmpr, "NomeEmpr", "Nome do empregador", ListaCampos.DB_SI, false ) );
		lcEmpregador.montaSql( false, "EMPREGADOR", "RH" );
		lcEmpregador.setQueryCommit( false );
		lcEmpregador.setReadOnly( true );
		txtCodEmpr.setTabelaExterna( lcEmpregador, FEmpregadores.class.getCanonicalName() );

		lcTurno.add( new GuardaCampo( txtCodTurnoVaga, "CodTurno", "Cód.Turno", ListaCampos.DB_PK, null, false ) );
		lcTurno.add( new GuardaCampo( txtDescTurnoVaga, "DescTurno", "Descrição do Turno", ListaCampos.DB_SI, false ) );
		lcTurno.montaSql( false, "TURNO", "RH" );
		lcTurno.setQueryCommit( false );
		lcTurno.setReadOnly( true );
		txtCodTurnoVaga.setTabelaExterna( lcTurno, FTurnos.class.getCanonicalName() );

		lcFuncao.add( new GuardaCampo( txtCodFuncaoVaga, "CodFunc", "Cód.função", ListaCampos.DB_PK, null, false ) );
		lcFuncao.add( new GuardaCampo( txtDescFuncaoVaga, "DescFunc", "Descrição da função", ListaCampos.DB_SI, false ) );
		lcFuncao.montaSql( false, "FUNCAO", "RH" );
		lcFuncao.setReadOnly( true );
		lcFuncao.setQueryCommit( false );
		txtCodFuncaoVaga.setListaCampos( lcFuncao );
		txtCodFuncaoVaga.setTabelaExterna( lcFuncao, FFuncao.class.getCanonicalName() );

		lcCurso.add( new GuardaCampo( txtCodCursoVaga, "CodCurso", "Cód.curso", ListaCampos.DB_PK, txtDescCursoVaga, false ) );
		lcCurso.add( new GuardaCampo( txtDescCursoVaga, "DescCurso", "Descrição do curso", ListaCampos.DB_SI, false ) );
		lcCurso.montaSql( false, "CURSO", "RH" );
		lcCurso.setReadOnly( true );
		lcCurso.setQueryCommit( false );
		txtCodCursoVaga.setTabelaExterna( lcCurso, FCurso.class.getCanonicalName() );
		txtCodCursoVaga.setFK( true );
		txtCodCursoVaga.setListaCampos( lcCurso );
		txtDescCursoVaga.setListaCampos( lcCurso );

		lcCaracteristicaQ.add( new GuardaCampo( txtCodCaracVagaQ, "CodCarac", "Cód.Carac.", ListaCampos.DB_PK, null, false ) );
		lcCaracteristicaQ.add( new GuardaCampo( txtDescCaracVagaQ, "DescCarac", "Descrição da característica", ListaCampos.DB_SI, false ) );
		lcCaracteristicaQ.montaSql( false, "Caracteristica", "RH" );
		lcCaracteristicaQ.setReadOnly( true );
		lcCaracteristicaQ.setQueryCommit( false );
		txtCodCaracVagaQ.setTabelaExterna( lcCaracteristicaQ, FCaracteristica.class.getCanonicalName() );
		txtCodCaracVagaQ.setFK( true );
		txtCodCaracVagaQ.setListaCampos( lcCaracteristicaQ );
		txtDescCaracVagaQ.setListaCampos( lcCaracteristicaQ );

		lcCaracteristicaR.add( new GuardaCampo( txtCodCaracVagaR, "CodCarac", "Cód.Carac.", ListaCampos.DB_PK, null, false ) );
		lcCaracteristicaR.add( new GuardaCampo( txtDescCaracVagaR, "DescCarac", "Descrição da característica", ListaCampos.DB_SI, false ) );
		lcCaracteristicaR.montaSql( false, "Caracteristica", "RH" );
		lcCaracteristicaR.setReadOnly( true );
		lcCaracteristicaR.setQueryCommit( false );
		txtCodCaracVagaR.setListaCampos( lcCaracteristicaR );
		txtCodCaracVagaR.setTabelaExterna( lcCaracteristicaR, FCaracteristica.class.getCanonicalName() );
		txtCodCaracVagaR.setFK( true );
		txtCodCaracVagaR.setListaCampos( lcCaracteristicaR );
		txtDescCaracVagaR.setListaCampos( lcCaracteristicaR );

	}

	private void montaTela() {

		// Aba geral

		adicTab( "Geral", panelGeral );
		setPainel( panelGeral );

		adicCampo( txtCodVaga, 7, 20, 90, 20, "CodVaga", "Cód.Vaga", ListaCampos.DB_PK, true );

		adicCampo( txtCodEmpr, 100, 20, 87, 20, "CodEmpr", "Cód.Empreg.", ListaCampos.DB_FK, txtNomeEmpr, true );
		adicDescFK( txtNomeEmpr, 190, 20, 330, 20, "NomeEmpr", "Nome do empregador" );

		adicCampo( txtCodFuncaoVaga, 7, 60, 90, 20, "CodFunc", "Cód.Função", ListaCampos.DB_FK, txtDescFuncaoVaga, true );
		adicDescFK( txtDescFuncaoVaga, 100, 60, 420, 20, "DescFunc", "Nome da função" );

		adicCampo( txtCodTurnoVaga, 7, 100, 90, 20, "CodTurno", "Cód.Turno", ListaCampos.DB_FK, txtDescTurnoVaga, true );
		adicDescFK( txtDescTurnoVaga, 100, 100, 420, 20, "DescTurno", "Descrição do turno" );

		adicCampo( txtFaixaSalIni, 7, 140, 150, 20, "FaixaSalIni", "Salário inicial", ListaCampos.DB_SI, false );
		adicCampo( txtFaixaSalFim, 160, 140, 150, 20, "FaixaSalFim", "Salário final", ListaCampos.DB_SI, false );

		// Fim da aba geral

		setListaCampos( true, "VAGA", "RH" );
		lcCampos.setQueryInsert( false );

		// Aba características Qualificativas

		adicTab( " Características Qualificativas", panelCaracQuali );

		setListaCampos( lcVagaCaracteristicaQ );
		setNavegador( navCaracteristicaQ );
		// navCaracteristicaQ.setAtivo( 6, false );

		panelCaracQuali.add( new JScrollPane( tabCaracteristicaQ ), BorderLayout.CENTER );
		panelCaracQuali.add( panelCaracQualiCampos, BorderLayout.SOUTH );

		setPainel( panelCaracQualiCampos );

		adicCampo( txtCodCaracVagaQ, 7, 20, 90, 20, "CodCarac", "Cód.atrib.", ListaCampos.DB_PF, txtDescCaracVagaQ, false );
		adicDescFK( txtDescCaracVagaQ, 100, 20, 300, 20, "DescCarac", "Descrição da característica" );
		adic( navCaracteristicaQ, 0, 50, 270, 25 );
		setListaCampos( false, "VAGACARACQUALI", "RH" );
		lcVagaCaracteristicaQ.setQueryInsert( false );
		lcVagaCaracteristicaQ.setQueryCommit( false );
		lcVagaCaracteristicaQ.montaTab();

		tabCaracteristicaQ.setTamColuna( 335, 1 );

		// Fim da aba características qualificativas

		// Aba características Restritivas

		adicTab( "Características Restritivas", panelCaracRestr );

		setListaCampos( lcVagaCaracteristicaR );
		setNavegador( navCaracteristicaR );
		// navCaracteristicaR.setAtivo( 6, false );

		panelCaracRestr.add( new JScrollPane( tabCaracteristicaR ), BorderLayout.CENTER );
		panelCaracRestr.add( panelCaracRestrCampos, BorderLayout.SOUTH );

		setPainel( panelCaracRestrCampos );

		adicCampo( txtCodCaracVagaR, 7, 20, 90, 20, "CodCarac", "Cód.atrib.", ListaCampos.DB_PF, txtDescCaracVagaR, false );
		adicDescFK( txtDescCaracVagaR, 100, 20, 300, 20, "DescCarac", "Descrição da característica" );
		adic( navCaracteristicaR, 0, 50, 270, 25 );
		setListaCampos( false, "VAGACARACREST", "RH" );
		lcVagaCaracteristicaR.setQueryInsert( false );
		lcVagaCaracteristicaR.setQueryCommit( false );
		lcVagaCaracteristicaR.montaTab();

		tabCaracteristicaR.setTamColuna( 335, 1 );

		// Fim da aba características restritivas

		// Aba cursos

		adicTab( "Cursos requeridos", panelCurso );
		setListaCampos( lcVagaCurso );
		setNavegador( navCurso );
		// navCaracteristicaR.setAtivo( 6, false );

		panelCurso.add( new JScrollPane( tabCurso ), BorderLayout.CENTER );
		panelCurso.add( panelCursoCampos, BorderLayout.SOUTH );

		setPainel( panelCursoCampos );

		adicCampo( txtCodCursoVaga, 7, 20, 90, 20, "CodCurso", "Cód.Curso", ListaCampos.DB_PF, txtDescCursoVaga, false );
		adicDescFK( txtDescCursoVaga, 100, 20, 300, 20, "DescCurso", "Descrição do Curso" );
		adic( navCurso, 0, 50, 270, 25 );
		setListaCampos( false, "VAGACURSO", "RH" );
		lcVagaCurso.setQueryInsert( false );
		lcVagaCurso.setQueryCommit( false );
		lcVagaCurso.montaTab();

		tabCurso.setTamColuna( 335, 1 );

		// Fim da aba cursos

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

	private void imprimir( TYPE_PRINT bVisualizar ) {

		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "RHVAGA" ) );

		dlGr = new FPrinterJob( "relatorios/grhVagas.jasper", "Lista de Vagas", "", this, hParam, con, null, false );

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

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcEmpregador.setConexao( cn );
		lcCurso.setConexao( cn );
		lcVagaCurso.setConexao( cn );
		lcTurno.setConexao( cn );
		lcVagaCaracteristicaQ.setConexao( cn );
		lcVagaCaracteristicaR.setConexao( cn );
		lcCaracteristicaQ.setConexao( cn );
		lcCaracteristicaR.setConexao( cn );
		lcFuncao.setConexao( cn );
	}
}
