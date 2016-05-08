/**
 * @version 19/09/2003 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.tmk <BR>
 *         Classe:
 * @(#)FreedomTMK.java <BR>
 * 
 *                     Este programa é licenciado de acordo com a LPG-PC (Licença Pública Geral para Programas de Computador), <BR>
 *                     modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                     A LPG-PC deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste Programa. <BR>
 *                     Caso uma cópia da LPG-PC não esteja disponível junto com este Programa, você pode contatar <BR>
 *                     sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                     Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                     Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa é preciso estar <BR>
 *                     de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                     Tela principal do módulo telemarketing.
 * 
 */

package org.freedom.modulos.crm;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.AplicativoPD;
import org.freedom.library.swing.frame.LoginPD;
import org.freedom.modulos.atd.view.frame.crud.plain.FTipoAtend;
import org.freedom.modulos.atd.view.frame.crud.tabbed.FAtendente;
import org.freedom.modulos.crm.agenda.FAgenda;
import org.freedom.modulos.crm.agenda.FTipoAgenda;
import org.freedom.modulos.crm.view.frame.crud.detail.FContrato;
import org.freedom.modulos.crm.view.frame.crud.detail.FDetOrc;
import org.freedom.modulos.crm.view.frame.crud.detail.FFichaAval;
import org.freedom.modulos.crm.view.frame.crud.detail.FFinalizaProjeto;
import org.freedom.modulos.crm.view.frame.crud.detail.FGrupoOrc;
import org.freedom.modulos.crm.view.frame.crud.detail.FSetorAtend;
import org.freedom.modulos.crm.view.frame.crud.detail.FTipoAtendo;
import org.freedom.modulos.crm.view.frame.crud.plain.FAmbienteAval;
import org.freedom.modulos.crm.view.frame.crud.plain.FAtividade;
import org.freedom.modulos.crm.view.frame.crud.plain.FChamado;
import org.freedom.modulos.crm.view.frame.crud.plain.FClasAtendo;
import org.freedom.modulos.crm.view.frame.crud.plain.FConfEmail;
import org.freedom.modulos.crm.view.frame.crud.plain.FEmail;
import org.freedom.modulos.crm.view.frame.crud.plain.FEspecAtend;
import org.freedom.modulos.crm.view.frame.crud.plain.FMarcador;
import org.freedom.modulos.crm.view.frame.crud.plain.FModAtendo;
import org.freedom.modulos.crm.view.frame.crud.plain.FModContr;
import org.freedom.modulos.crm.view.frame.crud.plain.FMotivoAval;
import org.freedom.modulos.crm.view.frame.crud.plain.FNovoAtend;
import org.freedom.modulos.crm.view.frame.crud.plain.FOrigContato;
import org.freedom.modulos.crm.view.frame.crud.plain.FQualificacao;
import org.freedom.modulos.crm.view.frame.crud.plain.FSitContr;
import org.freedom.modulos.crm.view.frame.crud.plain.FTarefa;
import org.freedom.modulos.crm.view.frame.crud.plain.FTipoChamado;
import org.freedom.modulos.crm.view.frame.crud.plain.FTipoCont;
import org.freedom.modulos.crm.view.frame.crud.plain.FTipoImp;
import org.freedom.modulos.crm.view.frame.crud.plain.FVendaContrato;
import org.freedom.modulos.crm.view.frame.crud.special.FHistorico;
import org.freedom.modulos.crm.view.frame.crud.tabbed.FCampanha;
import org.freedom.modulos.crm.view.frame.crud.tabbed.FContato;
import org.freedom.modulos.crm.view.frame.main.FPrincipalCRM;
import org.freedom.modulos.crm.view.frame.report.FRAcoesRealizadas;
import org.freedom.modulos.crm.view.frame.report.FRAnaliseAcoes;
import org.freedom.modulos.crm.view.frame.report.FRAtendimentos;
import org.freedom.modulos.crm.view.frame.report.FRBalancoProj;
import org.freedom.modulos.crm.view.frame.report.FRBhAtendente;
import org.freedom.modulos.crm.view.frame.report.FRCronograma;
import org.freedom.modulos.crm.view.frame.report.FRCustoProj;
import org.freedom.modulos.crm.view.frame.report.FRDiario;
import org.freedom.modulos.crm.view.frame.report.FRFaturamento;
import org.freedom.modulos.crm.view.frame.report.FRFeedback;
import org.freedom.modulos.crm.view.frame.report.FRFichaAvaliativa;
import org.freedom.modulos.crm.view.frame.report.FRPremiacoes;
import org.freedom.modulos.crm.view.frame.report.FRProjCon;
import org.freedom.modulos.crm.view.frame.report.FRResumoAtendente;
import org.freedom.modulos.crm.view.frame.report.FRSitContr;
import org.freedom.modulos.crm.view.frame.report.FRSobrHorario;
import org.freedom.modulos.crm.view.frame.utility.FCRM;
import org.freedom.modulos.crm.view.frame.utility.FConsisteCRM;
import org.freedom.modulos.crm.view.frame.utility.FConsultaCli;
import org.freedom.modulos.crm.view.frame.utility.FEnviaMail;
import org.freedom.modulos.crm.view.frame.utility.FGerencCampanhas;
import org.freedom.modulos.crm.view.frame.utility.FGestaoProj;
import org.freedom.modulos.crm.view.frame.utility.FImportaCto;
import org.freedom.modulos.gms.view.frame.crud.detail.FOrdemServico;
import org.freedom.modulos.gms.view.frame.utility.FControleServicos;
import org.freedom.modulos.std.view.frame.crud.detail.FModGrade;
import org.freedom.modulos.std.view.frame.crud.detail.FOrcamento;
import org.freedom.modulos.std.view.frame.crud.plain.FCategoriaImg;
import org.freedom.modulos.std.view.frame.crud.plain.FClasCli;
import org.freedom.modulos.std.view.frame.crud.plain.FImagem;
import org.freedom.modulos.std.view.frame.crud.plain.FSetor;
import org.freedom.modulos.std.view.frame.crud.plain.FTipoCli;
import org.freedom.modulos.std.view.frame.crud.plain.FVariantes;
import org.freedom.modulos.std.view.frame.crud.special.FGrade;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCliente;
import org.freedom.modulos.std.view.frame.crud.tabbed.FVendedor;
import org.freedom.modulos.std.view.frame.report.FRClientesSemVendas;
import org.freedom.modulos.std.view.frame.report.FRDesempVend;
import org.freedom.modulos.std.view.frame.report.FRDevolucao;
import org.freedom.modulos.std.view.frame.report.FREvoluVendas;
import org.freedom.modulos.std.view.frame.report.FRFechaDiario;
import org.freedom.modulos.std.view.frame.report.FRGerContas;
import org.freedom.modulos.std.view.frame.report.FRMediaItem;
import org.freedom.modulos.std.view.frame.report.FROrcamento;
import org.freedom.modulos.std.view.frame.report.FRResumoDiario;
import org.freedom.modulos.std.view.frame.report.FRUltimaVenda;
import org.freedom.modulos.std.view.frame.report.FRVendaSetor;
import org.freedom.modulos.std.view.frame.report.FRVendasCFOP;
import org.freedom.modulos.std.view.frame.report.FRVendasCanc;
import org.freedom.modulos.std.view.frame.report.FRVendasCli;
import org.freedom.modulos.std.view.frame.report.FRVendasCliProd;
import org.freedom.modulos.std.view.frame.report.FRVendasDet;
import org.freedom.modulos.std.view.frame.report.FRVendasFisico;
import org.freedom.modulos.std.view.frame.report.FRVendasGeral;
import org.freedom.modulos.std.view.frame.report.FRVendasItem;
import org.freedom.modulos.std.view.frame.report.FRVendasPlanoPag;
import org.freedom.modulos.std.view.frame.report.FRVendasTipoMov;
import org.freedom.modulos.std.view.frame.report.FRVendasVend;
import org.freedom.modulos.std.view.frame.utility.FAprovCancOrc;
import org.freedom.modulos.std.view.frame.utility.FConsPreco;
import org.freedom.modulos.std.view.frame.utility.FPesquisaOrc;

public class FreedomCRM extends AplicativoPD {

	private MenuItem miAtendimento = new MenuItem();

	public FreedomCRM() {

		super("iconcrm.png", "splashCRM.png", 1, "Freedom", 7, "CRM", null, new FPrincipalCRM(null, "bgFreedom2.jpg"),
				LoginPD.class);
		addOpcao(-1, TP_OPCAO_MENU, "Arquivo", "", 'A', 100000000, 0, false, null);

		addOpcao(100000000, TP_OPCAO_MENU, "Tabelas", "", 'T', 100100000, 1, false, null);

		addOpcao(100100000, TP_OPCAO_MENU, "Contato", "", 'C', 1001010000, 2, false, null);
		addOpcao(1001010000, TP_OPCAO_ITEM, "Atividade", "Atividade", 'A', 100101010, 3, true, FAtividade.class);
		addOpcao(1001010000, TP_OPCAO_ITEM, "Contato", "Contatos", 'C', 100101020, 3, true, FContato.class);
		addOpcao(1001010000, TP_OPCAO_ITEM, "Tipo de contato", "Tipo de contato", 'o', 100101030, 3, true,
				FTipoCont.class);

		addOpcao(100100000, TP_OPCAO_MENU, "Atendente", "", 'A', 100102000, 2, false, null);
		addOpcao(100102000, TP_OPCAO_ITEM, "Tipo de Atendente", "Tipo de atendente", 'T', 100102010, 3, true,
				FTipoAtend.class);
		addOpcao(100102000, TP_OPCAO_ITEM, "Atendentes", "Atendente", 'A', 100110020, 3, true, FAtendente.class);

		addOpcao(100100000, TP_OPCAO_MENU, "Cliente", "", 'C', 100104000, 2, false, null);
		addOpcao(100104000, TP_OPCAO_ITEM, "Setor do cliente", "Setor", 'S', 100104010, 3, true, FSetor.class);
		addOpcao(100104000, TP_OPCAO_ITEM, "Vendedor", "Vendedor", 'V', 100104020, 3, true, FVendedor.class);
		addOpcao(100104000, TP_OPCAO_ITEM, "Tipos de cliente", "Tipo de cliente", 'T', 100104030, 3, true,
				FTipoCli.class);
		addOpcao(100104000, TP_OPCAO_ITEM, "Classificação de cliente", "Classificação do cliente", 'l', 100104040, 3,
				true, FClasCli.class);
		addOpcao(100104000, TP_OPCAO_ITEM, "Cliente", "Clientes", 'C', 100104060, 3, true, FCliente.class);
		addSeparador(100200000);

		addOpcao(100100000, TP_OPCAO_ITEM, "Campanha", "Campanha", 'C', 100104070, 2, true, FCampanha.class);
		addOpcao(100100000, TP_OPCAO_ITEM, "Configuração de email", "Configuração de email", 'E', 100104080, 2, true,
				FConfEmail.class);
		addOpcao(100100000, TP_OPCAO_ITEM, "Origem do contato", "Origem do contato", 'O', 100104090, 2, true,
				FOrigContato.class);
		addOpcao(100100000, TP_OPCAO_ITEM, "Email", "Email", 'M', 100105000, 2, true, FEmail.class);
		addOpcao(100100000, TP_OPCAO_ITEM, "Tipo de agendamento", "Tipo de agendamento", 'g', 100106000, 2, true,
				FTipoAgenda.class);
		addSeparador(100100000);

		addOpcao(100100000, TP_OPCAO_MENU, "Grade de produtos", "", 'G', 100107000, 3, false, null);
		addOpcao(100107000, TP_OPCAO_ITEM, "Variantes", "Variantes", 'V', 100107010, 4, true, FVariantes.class);
		addOpcao(100107000, TP_OPCAO_ITEM, "Modelo", "Modelo de Grade", 'M', 100107020, 4, true, FModGrade.class);
		addOpcao(100107000, TP_OPCAO_ITEM, "Grade", "Grade", 'G', 100107030, 4, true, FGrade.class);

		addSeparador(100100000);
		addOpcao(100100000, TP_OPCAO_MENU, "Imagem", "", 'g', 100108000, 2, false, null);
		addOpcao(100108000, TP_OPCAO_ITEM, "Categoria", "Categoria de Imagens", 'C', 100108010, 2, true,
				FCategoriaImg.class);
		addOpcao(100108000, TP_OPCAO_ITEM, "Imagem", "Imagens", 'a', 100108020, 2, true, FImagem.class);

		addOpcao(100000000, TP_OPCAO_MENU, "Preferências", "", 'P', 100200000, 1, false, null);
		addOpcao(100200000, TP_OPCAO_ITEM, "Preferências CRM", "Preferências CRM", 'P', 100201000, 2, true,
				FPrefere.class);
		addOpcao(100000000, TP_OPCAO_MENU, "Ferramentas", "", 'F', 100300000, 1, false, null);
		addOpcao(100300000, TP_OPCAO_ITEM, "Importação de Contatos", "Importação de contatos", 'I', 100301000, 2, true,
				FImportaCto.class);
		addOpcao(100300000, TP_OPCAO_ITEM, "Cadastro de org.freedom.layout", "Cadastro de org.freedom.layout", 'C',
				100302000, 2, true, FTipoImp.class);
		addOpcao(100300000, TP_OPCAO_ITEM, "Envio de e-mail aos contatos", "Envia e-mail", 'E', 100303000, 2, true,
				FEnviaMail.class);
		addOpcao(100300000, TP_OPCAO_ITEM, "Gerenciamento de campanhas", "Gerenciamento de campanhas", 'G', 100304000,
				2, true, FGerencCampanhas.class);
		addOpcao(100300000, TP_OPCAO_ITEM, "Modelos de atendimento", "Modelos de atendimento", 'M', 100305000, 2, true,
				FModAtendo.class);

		addOpcao(-1, TP_OPCAO_MENU, "Contatos", "", 'C', 200000000, 0, false, null);
		addOpcao(200000000, TP_OPCAO_ITEM, "Histórico", "Historico", 'H', 200100000, 1, true, FHistorico.class);
		addOpcao(200000000, TP_OPCAO_ITEM, "Agenda", "Agenda", 'A', 200200000, 1, true, FAgenda.class);
		addOpcao(200000000, TP_OPCAO_MENU, "Listagens", "", 'L', 200300000, 1, false, null);
		addOpcao(200300000, TP_OPCAO_ITEM, "Relatório diário", "Relatório diário", 'R', 200301000, 1, true,
				FRDiario.class);
		addOpcao(200300000, TP_OPCAO_ITEM, "Relatório de feedback", "Relatório de feedback", 'f', 200302000, 1, true,
				FRFeedback.class);

		addOpcao(-1, TP_OPCAO_MENU, "Atendimento", "", 'A', 300000000, 0, false, null);
		addOpcao(300000000, TP_OPCAO_ITEM, "Gestão de relacionamento com clientes",
				"Gestão de relacionamento com clientes", 'G', 300100000, 1, true, FCRM.class);
		addSeparador(300000000);
		addOpcao(300000000, TP_OPCAO_ITEM, "Qualificações", "Qualificações", 'Q', 300400000, 1, true,
				FQualificacao.class);
		addOpcao(300000000, TP_OPCAO_ITEM, "Chamados", "Chamados", 'C', 300200000, 1, true, FChamado.class);

		addSeparador(300000000);

		addOpcao(300000000, TP_OPCAO_ITEM, "Tipo de atendimento", "Tipo de atendimento", 'T', 300200000, 1, true,
				FTipoAtendo.class);
		addOpcao(300000000, TP_OPCAO_ITEM, "Classificação de Atendimentos", "Classificação de Atendimento", 'i',
				300250000, 1, true, FClasAtendo.class);
		addOpcao(300000000, TP_OPCAO_ITEM, "Setor de Atendimentos", "Setor de Atendimento", 'S', 300260000, 1, true,
				FSetorAtend.class);
		addOpcao(300000000, TP_OPCAO_ITEM, "Tipo de chamado", "Tipo de chamado", 'h', 300270000, 1, true,
				FTipoChamado.class);
		addOpcao(300000000, TP_OPCAO_ITEM, "Especificação de atendimentos", "Especificação de atendimentos", 'E',
				300280000, 1, true, FEspecAtend.class);
		addOpcao(300000000, TP_OPCAO_ITEM, "Consistência/Ponto", "Consistência/Ponto", 'P', 300290000, 2, true,
				FConsisteCRM.class);

		addSeparador(300000000);

		addOpcao(300000000, TP_OPCAO_MENU, "Listagens", "", 'L', 300300000, 1, false, null);
		addOpcao(300300000, TP_OPCAO_ITEM, "Atendimentos/Cobrança", "Atendimentos/Cobrança", 'C', 300301000, 2, true,
				FRAtendimentos.class);
		addOpcao(300300000, TP_OPCAO_ITEM, "Atendimentos/Estatístico", "Atendimentos/Estatístico", 'E', 300302000, 2,
				true, FRResumoAtendente.class);
		addOpcao(300300000, TP_OPCAO_ITEM, "Banco de horas por atendente", "Banco de horas por atendente", 'B',
				300303000, 2, true, FRBhAtendente.class);
		addOpcao(300300000, TP_OPCAO_ITEM, "Sobreposição de horários", "Sobreposição de horários", 'S', 300304000, 2,
				true, FRSobrHorario.class);
		addOpcao(300300000, TP_OPCAO_ITEM, "Premiações", "Premiações", 'P', 300305000, 2, true, FRPremiacoes.class);
		addOpcao(300300000, TP_OPCAO_ITEM, "Análise de ações", "Análise de ações", 'A', 300306000, 2, true,
				FRAnaliseAcoes.class);
		addOpcao(300300000, TP_OPCAO_ITEM, "Previsão de faturamento", "Previsão de faturamento", 'A', 300307000, 2,
				true, FRFaturamento.class);

		addOpcao(-1, TP_OPCAO_MENU, "Saída", "", 'S', 400000000, 0, false, null);
		addOpcao(400000000, TP_OPCAO_ITEM, "Aprova orçamento", "Aprova Orçamento", 'A', 400100000, 1, true,
				FAprovCancOrc.class);

		addOpcao(400000000, TP_OPCAO_MENU, "Orçamento/Proposta", "", 'P', 400200000, 1, false, null);
		addOpcao(400200000, TP_OPCAO_ITEM, "Orçamento", "Orçamento", 'O', 400200100, 2, true, FOrcamento.class);
		addOpcao(400200000, TP_OPCAO_ITEM, "Agrupamento", "Agrupamento", 'A', 400200200, 2, true, FGrupoOrc.class);
		addOpcao(400200000, TP_OPCAO_ITEM, "Detalhamento", "Detalhamento", 'D', 400200300, 2, true, FDetOrc.class);
		addOpcao(400000000, TP_OPCAO_ITEM, "Pesquisa Orçamento", "Pesquisa Orçamento", 'P', 400300000, 1, true,
				FPesquisaOrc.class);
		addSeparador(400000000);
		addOpcao(400000000, TP_OPCAO_MENU, "Listagens", "", 's', 401000000, 1, false, null);
		addOpcao(401000000, TP_OPCAO_ITEM, "Resumo diário", "Resumo Diário", 'R', 401000100, 2, true,
				FRResumoDiario.class);
		addOpcao(401000000, TP_OPCAO_ITEM, "Vendas geral", "Vendas Geral", 'V', 401000200, 2, true,
				FRVendasGeral.class);
		addOpcao(401000000, TP_OPCAO_ITEM, "Vendas físico", "Físico de Vendas", 'd', 401000300, 2, true,
				FRVendasFisico.class);
		addOpcao(401000000, TP_OPCAO_ITEM, "Vendas detalhado", "Vendas Detalhadas", 'n', 401000400, 2, true,
				FRVendasDet.class);
		addOpcao(401000000, TP_OPCAO_ITEM, "Vendas por ítem", "Vendas por Item", 'e', 401000500, 2, true,
				FRVendasItem.class);
		addOpcao(401000000, TP_OPCAO_ITEM, "Média de vendas por ítem", "Media de vendas por item", 'o', 401000600, 2,
				true, FRMediaItem.class);
		addOpcao(401000000, TP_OPCAO_ITEM, "Ultimas Vendas por Cliente", "Ultimas Vendas por Cliente", 'U', 401000700,
				2, true, FRUltimaVenda.class);
		addOpcao(401000000, TP_OPCAO_ITEM, "Vendas por Cliente", "Vendas por Cliente", 'C', 401000800, 2, true,
				FRVendasCli.class);
		addOpcao(401000000, TP_OPCAO_ITEM, "Vendas por Setor", "Vendas por Setor", 't', 401000900, 2, true,
				FRVendaSetor.class);
		addOpcao(401000000, TP_OPCAO_ITEM, "Vendas por CFOP", "Vendas por CFOP", 'F', 401001000, 2, true,
				FRVendasCFOP.class);
		addOpcao(401000000, TP_OPCAO_ITEM, "Gerenciamento de contas", "Gerenciamento de contas", 'i', 401001100, 2,
				true, FRGerContas.class);
		addOpcao(401000000, TP_OPCAO_ITEM, "Fechamento diário", "Fechamento diário", 'i', 401001200, 2, true,
				FRFechaDiario.class);
		addOpcao(401000000, TP_OPCAO_ITEM, "Relatório de devolução", "Relatório de devolução", 'd', 401001300, 2, true,
				FRDevolucao.class);
		addOpcao(401000000, TP_OPCAO_ITEM, "Ultimas Vendas por Cli/Produto", "Ultimas Vendas por Cliente/Produto", 'd',
				401001400, 2, true, FRVendasCliProd.class);
		addOpcao(401000000, TP_OPCAO_ITEM, "Ultimas Vendas por Plano de Pagamento",
				"Ultimas Vendas por Plano de pagamento", 'd', 401001500, 2, true, FRVendasPlanoPag.class);
		addOpcao(401000000, TP_OPCAO_ITEM, "Desempenho por vendedor", "Desempenho por vendedor", 'v', 401001600, 2,
				true, FRDesempVend.class);
		addOpcao(401000000, TP_OPCAO_ITEM, "Vendas por vendedor", "Vendas por vendedor", 'v', 401001700, 2, true,
				FRVendasVend.class);
		addOpcao(401000000, TP_OPCAO_ITEM, "Vendas canceladas", "Vendas canceladas", 'v', 401001800, 2, true,
				FRVendasCanc.class);
		addOpcao(401000000, TP_OPCAO_ITEM, "Ultimas Vendas por Tipo de Movimento",
				"Ultimas Vendas por Tipo de Movimento", 'l', 401001900, 2, true, FRVendasTipoMov.class);
		addSeparador(401000000);
		addOpcao(401000000, TP_OPCAO_ITEM, "Orçamentos", "Orçamentos", 'O', 401001700, 2, true, FROrcamento.class);
		addSeparador(301000000);
		addOpcao(301000000, TP_OPCAO_ITEM, "Clientes sem movimento", "Clientes sem movimento", 'm', 301002000, 2, true,
				FRClientesSemVendas.class);

		addOpcao(400000000, TP_OPCAO_MENU, "Gráficos", "", 'G', 401100000, 1, false, null);
		addOpcao(401100000, TP_OPCAO_ITEM, "Evolução de vendas", "Evolução de vendas", 'E', 401100100, 2, true,
				FREvoluVendas.class);
		addSeparador(400000000);
		addOpcao(400000000, TP_OPCAO_MENU, "Consultas", "", 'n', 401200000, 1, false, null);
		addOpcao(401200000, TP_OPCAO_ITEM, "Preços", "Consulta de preços", 'P', 401200100, 2, true, FConsPreco.class);
		addOpcao(401200000, TP_OPCAO_ITEM, "Clientes", "Consulta de clientes", 'C', 301200200, 2, true,
				FConsultaCli.class);
		addOpcao(400000000, TP_OPCAO_ITEM, "Venda/Contrato", "Venda/Contrato", 'V', 401300000, 1, true,
				FVendaContrato.class);
		addSeparador(400000000);
		addOpcao(400000000, TP_OPCAO_MENU, "Ficha avaliativa", "", 'F', 401400000, 1, false, null);
		addOpcao(401400000, TP_OPCAO_ITEM, "Motivo", "Motivo da avaliação", 'M', 401400100, 2, true, FMotivoAval.class);
		addOpcao(401400000, TP_OPCAO_ITEM, "Ficha Avaliativa", "Ficha Avaliativa", 'L', 401400200, 2, true,
				FFichaAval.class);
		addOpcao(401400000, TP_OPCAO_ITEM, "Ambiente", "Ambiente", 'A', 401400300, 2, true, FAmbienteAval.class);
		addSeparador(401400000);
		addOpcao(401400000, TP_OPCAO_ITEM, "Rel. Ficha Avaliativa", "Rel.Ficha Avaliativa", 'R', 401400400, 2, true,
				FRFichaAvaliativa.class);

		addOpcao(-1, TP_OPCAO_MENU, "Projetos", "", 'S', 500000000, 0, false, null);
		addOpcao(500000000, TP_OPCAO_MENU, "Projetos/Contratos", "", 'P', 500100000, 1, false, null);
		addOpcao(500100000, TP_OPCAO_ITEM, "Projetos", "Projetos/Contratos", 'P', 500101000, 2, true, FContrato.class);
		addOpcao(500100000, TP_OPCAO_ITEM, "Finaliza Projetos/Contratos", "Finaliza Projetos/Contratos", 'P', 500102000,
				2, true, FFinalizaProjeto.class);
		addSeparador(500100000);
		addOpcao(500100000, TP_OPCAO_ITEM, "Marcador", "Marcador", 'P', 500103000, 2, true, FMarcador.class);
		addSeparador(500100000);
		addOpcao(500100000, TP_OPCAO_ITEM, "Tarefas/Subtarefas", "Tarefas/Subtarefas", 'T', 500104000, 2, true,
				FTarefa.class);
		addOpcao(500100000, TP_OPCAO_ITEM, "Situação Projeto/Contratos", "Situação Projeto/Contratos", 'S', 500105000,
				2, true, FSitContr.class);
		addOpcao(500100000, TP_OPCAO_ITEM, "Gestão de Projetos/Contratos", "Gestão de Projetos/Contratos", 'G',
				500106000, 2, true, FGestaoProj.class);
		addOpcao(500100000, TP_OPCAO_ITEM, "Modelo do Contrato", "Modelo do Contrato", 'M', 500107000, 2, true,
				FModContr.class);

		addOpcao(500000000, TP_OPCAO_MENU, "Listagens", "", 'L', 500200000, 1, false, null);
		addOpcao(500200000, TP_OPCAO_ITEM, "Detalhamento de custos", "Custos de Projeto", 'u', 500201000, 2, true,
				FRCustoProj.class);
		addOpcao(500200000, TP_OPCAO_ITEM, "Projetos / Contratos", "Projetos / Contratos", 'P', 500202000, 2, true,
				FRProjCon.class);
		addOpcao(500200000, TP_OPCAO_ITEM, "Situação Projetos / Contratos", "Situação Projetos / Contratos", 'S',
				500203000, 2, true, FRSitContr.class);
		addOpcao(500200000, TP_OPCAO_ITEM, "Cronograma Sintético", "Cronograma Sintético", 'C', 500204000, 2, true,
				FRCronograma.class);
		addOpcao(500200000, TP_OPCAO_ITEM, "Ações realizadas", "Ações realizadas", 'A', 500205000, 2, true,
				FRAcoesRealizadas.class);
		addOpcao(500200000, TP_OPCAO_ITEM, "Balanço de projeto", "Balanço de projeto", 'B', 500206000, 2, true,
				FRBalancoProj.class);

		addOpcao(-1, TP_OPCAO_MENU, "Serviços", "", 'S', 600000000, 0, false, null);
		addOpcao(600000000, TP_OPCAO_ITEM, "Painel de controle", "Painel de controle de Serviços", 'e', 600100000, 1,
				true, FControleServicos.class);
		addOpcao(600000000, TP_OPCAO_ITEM, "Ordem de Serviço", "Ordem de Serviço", 'e', 600200000, 1, true,
				FOrdemServico.class);

		addBotao("btCliente.png", "Cliente", "Clientes", 100104000, FCliente.class);
		addBotao("btAtendimentos.png", "Gestão de relacionamento com clientes", "Gestão de relacionamento com clientes",
				300100000, FCRM.class);
		addBotao("btChamado.png", "Chamados", "Chamados", 300020000, FChamado.class);
		addBotao("btCampanha.png", "Campanhas", "Campanhas", 100110020, FCampanha.class);
		addBotao("btEmail.gif", "Email", "Email", 100110020, FEmail.class);
		addBotao("btContato.png", "Contatos", "Contatos", 100110020, FContato.class);
		addBotao("btGerencCampanha.png", "Gerenciamento de campanhas", "Gerenciamento de campanhas", 100304000,
				FGerencCampanhas.class);
		addBotao("btProjeto.png", "Projetos", "Projetos/Contratos", 500100000, FContrato.class);
		addBotao("btOrcamento.png", "Orçamentos", "Orçamento", 400200000, FOrcamento.class);
		addBotao("btConsultaCli.png", "Consulta de Clientes", "Consulta de Clientes", 301200200, FConsultaCli.class);

		addBotao("btServico.png", "Ordem de Serviço", "Ordem de Serviço", 600200000, FOrdemServico.class);
		addBotao("btPainelServico.png", "Painel de Controle de Serviços", "Painel de Controle de Serviços", 600100000,
				FControleServicos.class);

		ajustaMenu();

		nomemodulo = "CRM";

		pm = new PopupMenu();

		// MenuItem mi = new MenuItem();

		miAtendimento.setName("miAtendimento");
		miAtendimento.setLabel("Atendimento");
		miAtendimento.addActionListener(this);

		pm.add(miAtendimento);

		telaPrincipal.setTrayIcon(pm);

	}

	public void actionPerformed(ActionEvent evt) {

		if (evt.getSource() == miAtendimento) {
			FNovoAtend dl = new FNovoAtend(0, null, telaPrincipal, false, con, 0, 0, "A", false, null, null, false);
			dl.setVisible(true);
			dl.dispose();
		}
		super.actionPerformed(evt);
	}

	public static void main(String sParams[]) {

		try {
			Aplicativo.setLookAndFeel("freedom.ini");
			FreedomCRM fFreedomtmk = new FreedomCRM();
			fFreedomtmk.show();
		} catch (Throwable e) {
			Funcoes.criaTelaErro("Erro de execução");
			e.printStackTrace();
		}
	}

}
