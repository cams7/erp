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

package org.freedom.modulos.gpe;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.AplicativoPD;
import org.freedom.library.swing.frame.FPrincipalPD;
import org.freedom.library.swing.frame.LoginPD;
import org.freedom.modulos.cfg.view.frame.crud.plain.FEstadoCivil;
import org.freedom.modulos.cfg.view.frame.crud.tabbed.FFeriados;
import org.freedom.modulos.gpe.view.frame.crud.plain.FBatida;
import org.freedom.modulos.gpe.view.frame.crud.plain.FFalta;
import org.freedom.modulos.gpe.view.frame.report.FRBatidas;
import org.freedom.modulos.gpe.view.frame.utility.FExcluLivroPonto;
import org.freedom.modulos.grh.view.frame.crud.plain.FBeneficio;
import org.freedom.modulos.grh.view.frame.crud.plain.FDepto;
import org.freedom.modulos.grh.view.frame.crud.plain.FFuncao;
import org.freedom.modulos.grh.view.frame.crud.plain.FTurnos;
import org.freedom.modulos.grh.view.frame.crud.tabbed.FEmpregado;
import org.freedom.modulos.grh.view.frame.crud.tabbed.FEmpregadores;

public class FreedomGPE extends AplicativoPD {

	public FreedomGPE() {

		super( "icongrh.png", "splashGRH.png", 1, "Freedom", 10, "Gestão de Ponto Eletrônico", null, new FPrincipalPD( null, "bgFreedom2.jpg" ), LoginPD.class );

		addOpcao( -1, TP_OPCAO_MENU, "Arquivo", "", 'A', 100000000, 0, false, null );

		addOpcao( 100000000, TP_OPCAO_MENU, "Tabelas", "", 'T', 100100000, 1, false, null );

		addOpcao( 100100000, TP_OPCAO_ITEM, "Turnos", "Turnos", 'T', 100100100, 2, true, FTurnos.class );
		addOpcao( 100100000, TP_OPCAO_ITEM, "Funções", "Funções", 'F', 100100200, 2, true, FFuncao.class );
		addOpcao( 100100000, TP_OPCAO_ITEM, "Departamentos", "Departamentos", 'D', 100100300, 2, true, FDepto.class );
		addOpcao( 100100000, TP_OPCAO_ITEM, "Empregados", "Empregados", 'E', 100100400, 2, true, FEmpregado.class );
		addOpcao( 100100000, TP_OPCAO_ITEM, "Empregadores", "Empregadores", 'p', 100100500, 2, true, FEmpregadores.class );
		addOpcao( 100100000, TP_OPCAO_ITEM, "Benefícios", "Cadastro de Benefícios", 'b', 100100600, 2, true, FBeneficio.class );

		addSeparador( 100100000 );
		addOpcao( 100100000, TP_OPCAO_ITEM, "Estados civis", "Estados civis", 's', 100100700, 2, true, FEstadoCivil.class );
		addSeparador( 100100000 );
		addOpcao( 100100000, TP_OPCAO_ITEM, "Feriados", "Cadastro de Feriados", 'r', 100100800, 2, true, FFeriados.class );

		addOpcao( -1, TP_OPCAO_MENU, "Ponto", "", 'P', 200000000, 0, false, null );
		addOpcao( 200000000, TP_OPCAO_ITEM, "Digitação de Livro Ponto", "Digitação de Livro Ponto", 'D', 200100000, 1, true, FBatida.class );

		addOpcao( 200000000, TP_OPCAO_ITEM, "Exclusão de Livro Ponto", "Exclusão de Livro Ponto", 'D', 200200000, 1, true, FExcluLivroPonto.class );

		addOpcao( 200000000, TP_OPCAO_ITEM, "Cadastro de Faltas", "Cadastro de Faltas", 'F',  200300000, 1, true, FFalta.class );

		addSeparador( 20000000 );
		addOpcao( 200000000, TP_OPCAO_MENU, "Listagens", "", 'L', 200400000, 1, false, null );
		addOpcao( 200400000, TP_OPCAO_ITEM, "Relatório de Batidas/Ponto", "Relatório de Batidas/Ponto", 'B', 200401000, 2, true, FRBatidas.class );	
		
		ajustaMenu();

		nomemodulo = "Ponto Eletrônico";

	}

	public static void main( String sParams[] ) {

		try {
			Aplicativo.setLookAndFeel( "freedom.ini" );
			FreedomGPE freedom = new FreedomGPE();
			freedom.show();
		} catch ( Throwable e ) {
			Funcoes.criaTelaErro( "Erro de execução\n\n" + e.getMessage() );
			e.printStackTrace();
		}
	}
}
