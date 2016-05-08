/**
 * @version 07/01/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.cfg <BR>
 *         Classe:
 * @(#)FreedomCFG.java <BR>
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
 *                     Tela principal do módulo configurador.
 * 
 */

package org.freedom.modulos.cfg;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.AplicativoPD;
import org.freedom.library.swing.frame.FPrincipalPD;
import org.freedom.library.swing.frame.LoginPD;
import org.freedom.modulos.cfg.view.frame.crud.detail.FFluxo;
import org.freedom.modulos.cfg.view.frame.crud.detail.FMenuObj;
import org.freedom.modulos.cfg.view.frame.crud.detail.FObjetoTb;
import org.freedom.modulos.cfg.view.frame.crud.detail.FProcesso;
import org.freedom.modulos.cfg.view.frame.crud.detail.FTabela;
import org.freedom.modulos.cfg.view.frame.crud.plain.FAnaliseLog;
import org.freedom.modulos.cfg.view.frame.crud.plain.FBairro;
import org.freedom.modulos.cfg.view.frame.crud.plain.FEstadoCivil;
import org.freedom.modulos.cfg.view.frame.crud.plain.FGrauInst;
import org.freedom.modulos.cfg.view.frame.crud.plain.FGrupoUsu;
import org.freedom.modulos.cfg.view.frame.crud.plain.FMunicipio;
import org.freedom.modulos.cfg.view.frame.crud.plain.FPais;
import org.freedom.modulos.cfg.view.frame.crud.plain.FUF;
import org.freedom.modulos.cfg.view.frame.crud.special.FAcesso;
import org.freedom.modulos.cfg.view.frame.crud.tabbed.FFeriados;
import org.freedom.modulos.cfg.view.frame.crud.tabbed.FUsuario;
import org.freedom.modulos.cfg.view.frame.utility.FAjustaSeq;
import org.freedom.modulos.cfg.view.frame.utility.FVisual;
import org.freedom.modulos.crm.view.frame.crud.plain.FConfEmail;
import org.freedom.modulos.pdv.FLeFiscal;

public class FreedomCFG extends AplicativoPD {

	public FreedomCFG() {

		super("iconcfg.png", "splashCFG.png", 1, "Freedom", 2, "Configurador do Sistema", null,
				new FPrincipalPD(null, "bgFreedom2.jpg"), LoginPD.class);

		addOpcao(-1, TP_OPCAO_MENU, "Arquivo", "", 'A', 100000000, 0, false, null);
		addOpcao(100000000, TP_OPCAO_MENU, "Tabelas", "", 'T', 100100000, 1, false, null);
		addOpcao(100100000, TP_OPCAO_MENU, "Acesso ao sistema", "", 'A', 100101000, 2, false, null);
		addOpcao(100101000, TP_OPCAO_ITEM, "Grupos", "Grupos", 'G', 100101010, 3, true, FGrupoUsu.class);
		addOpcao(100101000, TP_OPCAO_ITEM, "Usuarios", "Usuarios", 'U', 100101020, 3, true, FUsuario.class);
		addOpcao(100101000, TP_OPCAO_ITEM, "Acesso", "Acesso Menu", 'A', 100101030, 3, true, FAcesso.class);
		addOpcao(100101000, TP_OPCAO_ITEM, "Menu", "Menu X Objeto", 'M', 100101040, 3, true, FMenuObj.class);
		addOpcao(100100000, TP_OPCAO_MENU, "Tabelas Geográficas", "", 'C', 100102000, 2, false, null);
		addOpcao(100102000, TP_OPCAO_ITEM, "Paises", "Paises", 'P', 100102020, 3, true, FPais.class);
		addOpcao(100102000, TP_OPCAO_ITEM, "Cidades", "Cidades", 'd', 100102030, 3, true, FMunicipio.class);
		addOpcao(100102000, TP_OPCAO_ITEM, "Estados", "Estados", 'E', 100102040, 3, true, FUF.class);
		addOpcao(100102000, TP_OPCAO_ITEM, "Bairros", "Bairros", 'B', 100102050, 3, true, FBairro.class);
		addOpcao(100100000, TP_OPCAO_MENU, "Objetos", "", 'O', 100103000, 2, false, null);
		addOpcao(100103000, TP_OPCAO_ITEM, "Tabela", "Tabelas auxiliares", 'T', 100103010, 3, true, FTabela.class);
		addOpcao(100103000, TP_OPCAO_ITEM, "Objetos aux.", "Vinculo entre tabelas físicas e auxiliares", 'O', 100103020,
				3, true, FObjetoTb.class);
		addOpcao(100100000, TP_OPCAO_MENU, "Fluxos", "", 'F', 100104000, 2, false, null);
		addOpcao(100104000, TP_OPCAO_ITEM, "Processos", "Processos", 'P', 100104100, 3, true, FProcesso.class);
		addOpcao(100104000, TP_OPCAO_ITEM, "Fluxos", "Cadastro de fluxos", 'F', 100104110, 3, true, FFluxo.class);

		addOpcao(100100000, TP_OPCAO_MENU, "Outras tabelas genéricas", "", 's', 100105000, 2, false, null);
		addOpcao(100105000, TP_OPCAO_ITEM, "Estados civis", "Estados civis", 'i', 100105100, 3, true,
				FEstadoCivil.class);
		addOpcao(100105000, TP_OPCAO_ITEM, "Cadastro de Feriado", "Cadastro de Feriado", 'i', 100105200, 3, true,
				FFeriados.class);
		addOpcao(100105000, TP_OPCAO_ITEM, "Configuração de email", "Configuração de email", 'e', 100105300, 3, true,
				FConfEmail.class);
		addOpcao(100105000, TP_OPCAO_ITEM, "Grau de instrução", "Grau de Instrução", 'G', 100105410, 3, true,
				FGrauInst.class);

		addOpcao(100100000, TP_OPCAO_MENU, "Logs", "", 'l', 100106000, 2, false, null);
		addOpcao(100106000, TP_OPCAO_ITEM, "Análise de Logs", "Análise de Logs", 'L', 100106100, 3, true,
				FAnaliseLog.class);

		addOpcao(100000000, TP_OPCAO_MENU, "Ferramentas", "", 'e', 100200000, 1, false, null);
		addOpcao(100200000, TP_OPCAO_ITEM, "Ajuste de Sequencia", "Ajusta sequencia", 'A', 100201000, 2, true,
				FAjustaSeq.class);
		addOpcao(100200000, TP_OPCAO_ITEM, "Leitura Fiscal", "Leitura Fiscal", 'F', 100202000, 2, true,
				FLeFiscal.class);

		addOpcao(100000000, TP_OPCAO_MENU, "Preferências", "", 'P', 100300000, 1, false, null);
		addOpcao(100300000, TP_OPCAO_ITEM, "Visual", "Configuração de Visual", 'A', 100301000, 2, true, FVisual.class);

		addBotao("barraGrupo.gif", "Cadastro de Grupos", "Grupos", 100101010, FGrupoUsu.class);
		addBotao("barraUsuario.png", "Cadastro de Usuarios", "Usuarios", 100101020, FUsuario.class);
		addBotao("barraAcesso.gif", "Controle de Acessos", "Acesso Menu", 100101030, FAcesso.class);

		ajustaMenu();

		// Inicia a variável de conexão com o banco interno do interbase
		conIB = conexaoIB(getParameter("driver"), getParameter("bancocfg"));

		nomemodulo = "Configurador";

	}

	public static void main(String sParams[]) {

		try {
			Aplicativo.setLookAndFeel("freedom.ini");
			FreedomCFG freedom = new FreedomCFG();
			freedom.show();

		} catch (Throwable e) {
			Funcoes.criaTelaErro("Erro de execução");
			e.printStackTrace();
		}
	}
}
