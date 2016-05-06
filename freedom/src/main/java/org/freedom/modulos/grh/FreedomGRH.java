/**
 * @version 30/01/2004 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.grh <BR>
 *         Classe:
 * @(#)FreedomGRH.java <BR>
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
 *                     Tela principal para o módulo de gestão de recursos humanos.
 * 
 */

package org.freedom.modulos.grh;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.AplicativoPD;
import org.freedom.library.swing.frame.FPrincipalPD;
import org.freedom.library.swing.frame.LoginPD;
import org.freedom.modulos.cfg.view.frame.crud.plain.FEstadoCivil;
import org.freedom.modulos.cfg.view.frame.crud.tabbed.FFeriados;
import org.freedom.modulos.grh.view.frame.crud.plain.FArea;
import org.freedom.modulos.grh.view.frame.crud.plain.FBeneficio;
import org.freedom.modulos.grh.view.frame.crud.plain.FCaracteristica;
import org.freedom.modulos.grh.view.frame.crud.plain.FCodGPS;
import org.freedom.modulos.grh.view.frame.crud.plain.FCurso;
import org.freedom.modulos.grh.view.frame.crud.plain.FDepto;
import org.freedom.modulos.grh.view.frame.crud.plain.FFuncao;
import org.freedom.modulos.grh.view.frame.crud.plain.FNivelCurso;
import org.freedom.modulos.grh.view.frame.crud.plain.FTurnos;
import org.freedom.modulos.grh.view.frame.crud.special.FTabelaINSS;
import org.freedom.modulos.grh.view.frame.crud.special.FTabelaIRRF;
import org.freedom.modulos.grh.view.frame.crud.tabbed.FCandidato;
import org.freedom.modulos.grh.view.frame.crud.tabbed.FEmpregado;
import org.freedom.modulos.grh.view.frame.crud.tabbed.FEmpregadores;
import org.freedom.modulos.grh.view.frame.crud.tabbed.FVaga;
import org.freedom.modulos.grh.view.frame.report.FRAtividade;
import org.freedom.modulos.grh.view.frame.report.FRRelAtiv;
import org.freedom.modulos.grh.view.frame.report.FRVagas;
import org.freedom.modulos.grh.view.frame.utility.FGerencVagas;

public class FreedomGRH extends AplicativoPD {

	public FreedomGRH() {

		super( "icongrh.png", "splashGRH.png", 1, "Freedom", 9, "Gestão de Recursos Humanos", null, new FPrincipalPD( null, "bgFreedom2.jpg" ), LoginPD.class );

		addOpcao( -1, TP_OPCAO_MENU, "Arquivo", "", 'A', 100000000, 0, false, null );

		addOpcao( 100000000, TP_OPCAO_MENU, "Tabelas", "", 'T', 100100000, 1, false, null );

		addOpcao( 100100000, TP_OPCAO_ITEM, "Candidatos", "Candidatos", 'C', 100100100, 2, true, FCandidato.class );

		addSeparador( 100100000 );

		addOpcao( 100100000, TP_OPCAO_ITEM, "Turnos", "Turnos", 'T', 100100200, 2, true, FTurnos.class );
		addOpcao( 100100000, TP_OPCAO_ITEM, "Funções", "Funções", 'F', 100100300, 2, true, FFuncao.class );
		addOpcao( 100100000, TP_OPCAO_ITEM, "Departamentos", "Departamentos", 'D', 100100400, 2, true, FDepto.class );
		addOpcao( 100100000, TP_OPCAO_ITEM, "Empregados", "Empregados", 'E', 100100500, 2, true, FEmpregado.class );
		addOpcao( 100100000, TP_OPCAO_ITEM, "Empregadores", "Empregadores", 'p', 100100600, 2, true, FEmpregadores.class );
		addOpcao( 100100000, TP_OPCAO_ITEM, "Benefícios", "Cadastro de Benefícios", 'b', 100100700, 2, true, FBeneficio.class );

		addSeparador( 100100000 );

		addOpcao( 100100000, TP_OPCAO_ITEM, "Areas", "Areas", 'A', 100100700, 2, true, FArea.class );
		addOpcao( 100100000, TP_OPCAO_ITEM, "Níveis/Cursos", "Níveis/Cursos", 'N', 100100800, 2, true, FNivelCurso.class );
		addOpcao( 100100000, TP_OPCAO_ITEM, "Cursos", "Cursos", 'u', 100100900, 2, true, FCurso.class );
		addOpcao( 100100000, TP_OPCAO_ITEM, "Características", "Características", 's', 100101000, 2, true, FCaracteristica.class );
		addSeparador( 100100000 );
		addOpcao( 100100000, TP_OPCAO_ITEM, "Estados civis", "Estados civis", 's', 100101100, 2, true, FEstadoCivil.class );
		addSeparador( 100100000 );
		addOpcao( 100100000, TP_OPCAO_ITEM, "Códigos GPS/INSS", "Códigos GPS/INSS", 'g', 100101200, 2, true, FCodGPS.class );
		addSeparador( 100100000 );
		addOpcao( 100100000, TP_OPCAO_ITEM, "Tabela de IRRF", "Tabela de IRRF", 'I', 100101300, 1, true, FTabelaIRRF.class );
		addOpcao( 100100000, TP_OPCAO_ITEM, "Tabela de INSS", "Tabela de INSS", 'S', 100101400, 1, true, FTabelaINSS.class );
		addOpcao( 100100000, TP_OPCAO_ITEM, "Feriados", "Cadastro de Feriados", 'r', 100101500, 2, true, FFeriados.class );
		
		addOpcao( -1, TP_OPCAO_MENU, "Vagas", "", 'V', 200000000, 0, false, null );
		addOpcao( 200000000, TP_OPCAO_ITEM, "Cadastro de vagas", "Cadastro de vagas", 'V', 200100000, 1, true, FVaga.class );
		addSeparador( 200000000 );
		addOpcao( 200000000, TP_OPCAO_ITEM, "Gerenciamento de vagas", "Gerenciamento de Vagas", 'T', 200200000, 1, true, FGerencVagas.class );
		addSeparador( 200000000 );
		addOpcao( 200000000, TP_OPCAO_MENU, "Relatórios", "Relatórios", 'T', 200300000, 1, false, null );
		addOpcao( 200300000, TP_OPCAO_ITEM, "Relatório de atividade por vaga", "Relatório de atividade por vaga", 'a', 200301000, 1, true, FRRelAtiv.class );
		addOpcao( 200300000, TP_OPCAO_ITEM, "Relatório de Vagas", "Relatório de Vagas", 'a', 200302000, 1, true, FRVagas.class );
		addOpcao( 200300000, TP_OPCAO_ITEM, "Relatório de atividade", "Relatório de atividade", 'e', 200303000, 1, true, FRAtividade.class );
		
		addBotao( "btForneced.png", "Empregadores", "Empregadores", 100100600, FEmpregadores.class );
		addBotao( "btMedida.png", "Características", "Características", 100101000, FCaracteristica.class );
		addBotao( "barraConveniados.png", "Empregados", "Empregados", 100100500, FEmpregado.class );
		addBotao( "barraGrupo.gif", "Candidatos", "Candidatos", 100100100, FCandidato.class );
		addBotao( "btTarefas.gif", "Cursos", "Cursos", 100100900, FCurso.class );
		addBotao( "btNovo.png", "Cadastro de vagas", "Cadastro de vagas", 100101100, FVaga.class );
		addBotao( "btPesquisa.png", "Gerenciamento de vagas", "Gerenciamento de vagas", 200100000, FGerencVagas.class );

		ajustaMenu();

		nomemodulo = "Recursos Humanos";

	}

	public static void main( String sParams[] ) {

		try {
			Aplicativo.setLookAndFeel( "freedom.ini" );
			FreedomGRH freedom = new FreedomGRH();
			freedom.show();
		} catch ( Throwable e ) {
			Funcoes.criaTelaErro( "Erro de execução\n\n" + e.getMessage() );
			e.printStackTrace();
		}
	}
}
