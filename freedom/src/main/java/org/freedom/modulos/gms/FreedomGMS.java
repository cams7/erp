/**
 * @version 30/01/2004 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.gms <BR>
 *         Classe:
 * @(#)FreedomGMS.java <BR>
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
 *                     Tela principal para o módulo de gestão de materiais.
 * 
 */

package org.freedom.modulos.gms;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.AplicativoPD;
import org.freedom.library.swing.frame.FPrincipalPD;
import org.freedom.library.swing.frame.LoginPD;
import org.freedom.modulos.atd.view.frame.crud.plain.FTipoAtend;
import org.freedom.modulos.atd.view.frame.crud.tabbed.FAtendente;
import org.freedom.modulos.cfg.view.frame.crud.plain.FBairro;
import org.freedom.modulos.cfg.view.frame.crud.plain.FMunicipio;
import org.freedom.modulos.cfg.view.frame.crud.plain.FPais;
import org.freedom.modulos.cfg.view.frame.crud.plain.FUF;
import org.freedom.modulos.crm.view.frame.crud.plain.FChamado;
import org.freedom.modulos.crm.view.frame.utility.FCRM;
import org.freedom.modulos.gms.view.frame.crud.detail.FColeta;
import org.freedom.modulos.gms.view.frame.crud.detail.FCompra;
import org.freedom.modulos.gms.view.frame.crud.detail.FConhecFrete;
import org.freedom.modulos.gms.view.frame.crud.detail.FCotacaoItens;
import org.freedom.modulos.gms.view.frame.crud.detail.FCotacaoPrecos;
import org.freedom.modulos.gms.view.frame.crud.detail.FExpedicao;
import org.freedom.modulos.gms.view.frame.crud.detail.FImportacao;
import org.freedom.modulos.gms.view.frame.crud.detail.FOrdemCompra;
import org.freedom.modulos.gms.view.frame.crud.detail.FOrdemServico;
import org.freedom.modulos.gms.view.frame.crud.detail.FRecMerc;
import org.freedom.modulos.gms.view.frame.crud.detail.FRma;
import org.freedom.modulos.gms.view.frame.crud.detail.FSolicitacaoCompra;
import org.freedom.modulos.gms.view.frame.crud.detail.FTipoExpedicao;
import org.freedom.modulos.gms.view.frame.crud.detail.FTipoRecMerc;
import org.freedom.modulos.gms.view.frame.crud.plain.FAtribuicao;
import org.freedom.modulos.gms.view.frame.crud.plain.FHomologFor;
import org.freedom.modulos.gms.view.frame.crud.plain.FSecaoProd;
import org.freedom.modulos.gms.view.frame.crud.special.FAtribUsu;
import org.freedom.modulos.gms.view.frame.crud.special.FGrupoProd;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FPrefereGMS;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FTipoMov;
import org.freedom.modulos.gms.view.frame.report.FRColetas;
import org.freedom.modulos.gms.view.frame.report.FRComisProd;
import org.freedom.modulos.gms.view.frame.report.FRFreteExpedicao;
import org.freedom.modulos.gms.view.frame.report.FRFreteRecMerc;
import org.freedom.modulos.gms.view.frame.report.FRMatPrimaFor;
import org.freedom.modulos.gms.view.frame.report.FROCEntregaPrevista;
import org.freedom.modulos.gms.view.frame.report.FRPesoRecMerc;
import org.freedom.modulos.gms.view.frame.report.FRPrecoMedioRecMerc;
import org.freedom.modulos.gms.view.frame.report.FRValorEstoque;
import org.freedom.modulos.gms.view.frame.utility.FAtualizaForneced;
import org.freedom.modulos.gms.view.frame.utility.FConsCompra;
import org.freedom.modulos.gms.view.frame.utility.FConsRMA;
import org.freedom.modulos.gms.view.frame.utility.FConsRmaItem;
import org.freedom.modulos.gms.view.frame.utility.FConsSol;
import org.freedom.modulos.gms.view.frame.utility.FControleExpedicao;
import org.freedom.modulos.gms.view.frame.utility.FControleRecMerc;
import org.freedom.modulos.gms.view.frame.utility.FControleServicos;
import org.freedom.modulos.gms.view.frame.utility.FGestaoSol;
import org.freedom.modulos.gms.view.frame.utility.FMovSerie;
import org.freedom.modulos.pcp.view.frame.crud.detail.FContraProva;
import org.freedom.modulos.pcp.view.frame.crud.detail.FOP;
import org.freedom.modulos.pcp.view.frame.crud.plain.FSimulaOP;
import org.freedom.modulos.pcp.view.frame.report.FRAnalise;
import org.freedom.modulos.pcp.view.frame.report.FRCertAnalise;
import org.freedom.modulos.pcp.view.frame.report.FRContraProva;
import org.freedom.modulos.pcp.view.frame.report.FREstruturaItem;
import org.freedom.modulos.pcp.view.frame.report.FRProducao;
import org.freedom.modulos.pcp.view.frame.utility.FAcompanhaProd;
import org.freedom.modulos.pcp.view.frame.utility.FBaixaRMACodBar;
import org.freedom.modulos.pcp.view.frame.utility.FPMP_Pull;
import org.freedom.modulos.pcp.view.frame.utility.FPMP_Push;
import org.freedom.modulos.std.FPrefereGeral;
import org.freedom.modulos.std.view.frame.crud.detail.FModGrade;
import org.freedom.modulos.std.view.frame.crud.detail.FOrcamento;
import org.freedom.modulos.std.view.frame.crud.detail.FPlanoPag;
import org.freedom.modulos.std.view.frame.crud.detail.FSimilar;
import org.freedom.modulos.std.view.frame.crud.detail.FVenda;
import org.freedom.modulos.std.view.frame.crud.plain.FAlmox;
import org.freedom.modulos.std.view.frame.crud.plain.FEntrega;
import org.freedom.modulos.std.view.frame.crud.plain.FEstacao;
import org.freedom.modulos.std.view.frame.crud.plain.FFrete;
import org.freedom.modulos.std.view.frame.crud.plain.FImpressora;
import org.freedom.modulos.std.view.frame.crud.plain.FInventario;
import org.freedom.modulos.std.view.frame.crud.plain.FMarca;
import org.freedom.modulos.std.view.frame.crud.plain.FModEtiqueta;
import org.freedom.modulos.std.view.frame.crud.plain.FModNota;
import org.freedom.modulos.std.view.frame.crud.plain.FMotorista;
import org.freedom.modulos.std.view.frame.crud.plain.FNatoPer;
import org.freedom.modulos.std.view.frame.crud.plain.FPapel;
import org.freedom.modulos.std.view.frame.crud.plain.FSerie;
import org.freedom.modulos.std.view.frame.crud.plain.FTipoCob;
import org.freedom.modulos.std.view.frame.crud.plain.FTipoFor;
import org.freedom.modulos.std.view.frame.crud.plain.FUnidade;
import org.freedom.modulos.std.view.frame.crud.plain.FVariantes;
import org.freedom.modulos.std.view.frame.crud.plain.FVeiculo;
import org.freedom.modulos.std.view.frame.crud.special.FDevolucao;
import org.freedom.modulos.std.view.frame.crud.special.FGrade;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCliente;
import org.freedom.modulos.std.view.frame.crud.tabbed.FFornecedor;
import org.freedom.modulos.std.view.frame.crud.tabbed.FTransp;
import org.freedom.modulos.std.view.frame.report.FRCodbarProd;
import org.freedom.modulos.std.view.frame.report.FRCodficProd;
import org.freedom.modulos.std.view.frame.report.FRCompras;
import org.freedom.modulos.std.view.frame.report.FRComprasFor;
import org.freedom.modulos.std.view.frame.report.FRConfEstoq;
import org.freedom.modulos.std.view.frame.report.FRContaEstoque;
import org.freedom.modulos.std.view.frame.report.FRCpItem;
import org.freedom.modulos.std.view.frame.report.FRCpMunicipio;
import org.freedom.modulos.std.view.frame.report.FRCpProd;
import org.freedom.modulos.std.view.frame.report.FRCpTipoMov;
import org.freedom.modulos.std.view.frame.report.FRDemanda;
import org.freedom.modulos.std.view.frame.report.FREstoqueLiquido;
import org.freedom.modulos.std.view.frame.report.FREstoqueMin;
import org.freedom.modulos.std.view.frame.report.FREtiqueta;
import org.freedom.modulos.std.view.frame.report.FREvoluVendas;
import org.freedom.modulos.std.view.frame.report.FRGiroEstoque;
import org.freedom.modulos.std.view.frame.report.FRInvPeps;
import org.freedom.modulos.std.view.frame.report.FRMediaItem;
import org.freedom.modulos.std.view.frame.report.FRMovProd;
import org.freedom.modulos.std.view.frame.report.FRMovProdCont;
import org.freedom.modulos.std.view.frame.report.FRProdGrup;
import org.freedom.modulos.std.view.frame.report.FRResumoDiario;
import org.freedom.modulos.std.view.frame.report.FRSaldoLote;
import org.freedom.modulos.std.view.frame.report.FRUltimaVenda;
import org.freedom.modulos.std.view.frame.report.FRVencLote;
import org.freedom.modulos.std.view.frame.report.FRVendaSetor;
import org.freedom.modulos.std.view.frame.report.FRVendasDet;
import org.freedom.modulos.std.view.frame.report.FRVendasFisico;
import org.freedom.modulos.std.view.frame.report.FRVendasGeral;
import org.freedom.modulos.std.view.frame.report.FRVendasItem;
import org.freedom.modulos.std.view.frame.report.FRomaneio;
import org.freedom.modulos.std.view.frame.utility.FAprovCancOrc;
import org.freedom.modulos.std.view.frame.utility.FCancVenda;
import org.freedom.modulos.std.view.frame.utility.FConsEstoque;
import org.freedom.modulos.std.view.frame.utility.FConsPreco;
import org.freedom.modulos.std.view.frame.utility.FImpTabFor;
import org.freedom.modulos.std.view.frame.utility.FKardex;
import org.freedom.modulos.std.view.frame.utility.FPesquisaOrc;
import org.freedom.modulos.std.view.frame.utility.FProcessaEQ;
import org.freedom.modulos.std.view.frame.utility.FStatusItOrc;

public class FreedomGMS extends AplicativoPD {

	public FreedomGMS() {

		super( "icongms.png", "splashGMS.png", 1, "Freedom", 8, "Gestão de Materiais e Serviços", null, new FPrincipalPD( null, "bgFreedom2.jpg" ), LoginPD.class );

		addOpcao( -1, TP_OPCAO_MENU, "Arquivo", "", 'A', 100000000, 0, false, null );
		addOpcao( 100000000, TP_OPCAO_MENU, "Cadastros", "", 'T', 100100000, 1, false, null );
		addOpcao( 100100000, TP_OPCAO_MENU, "Clientes", "", 'C', 100101000, 2, false, null );
		addOpcao( 100101000, TP_OPCAO_ITEM, "Clientes", "Clientes", 'C', 100101010, 3, true, FCliente.class );
		addSeparador( 100100000 );
		addOpcao( 100100000, TP_OPCAO_MENU, "Fornecedores", "", 'C', 100102000, 2, false, null );
		addOpcao( 100102000, TP_OPCAO_ITEM, "Tipos de fornecedores", "TipoFor", 'e', 100102010, 3, true, FTipoFor.class );
		addOpcao( 100102000, TP_OPCAO_ITEM, "Fornecedores", "Fornecedor", 'r', 100102020, 3, true, FFornecedor.class );
		addSeparador( 100102000 );
		addOpcao( 100100000, TP_OPCAO_MENU, "Produtos", "", 'u', 100103000, 2, false, null );
		addOpcao( 100103000, TP_OPCAO_ITEM, "Almoxarifados", "Almoxarifado", 'x', 100103030, 3, true, FAlmox.class );
		addOpcao( 100103000, TP_OPCAO_ITEM, "Grupos", "Grupos", 'r', 100103040, 3, true, FGrupoProd.class );
		addOpcao( 100103000, TP_OPCAO_ITEM, "Seções", "Seções", 'e', 100103091, 3, true, FSecaoProd.class );
		addOpcao( 100103000, TP_OPCAO_ITEM, "Marcas", "Marcas", 'c', 100103050, 3, true, FMarca.class );
		addOpcao( 100103000, TP_OPCAO_ITEM, "Unidades", "Unidades", 'U', 100103060, 3, true, FUnidade.class );
		addOpcao( 100103000, TP_OPCAO_ITEM, "Kits de produtos", "Kits de produtos", 'K', 100103070, 3, true, FGrupoProd.class );
		addOpcao( 100103000, TP_OPCAO_ITEM, "Similaridade", "Similar", 'S', 100103080, 3, true, FSimilar.class );
		addOpcao( 100103000, TP_OPCAO_ITEM, "Produtos", "Produtos", 'P', 100103090, 3, true, FProduto.class );
		addSeparador( 100100000 );
		addOpcao( 100100000, TP_OPCAO_MENU, "Grade de produtos", "", 'G', 100103100, 3, false, null );
		addOpcao( 100103100, TP_OPCAO_ITEM, "Variantes", "Variantes", 'V', 100103101, 4, true, FVariantes.class );
		addOpcao( 100103100, TP_OPCAO_ITEM, "Modelos", "Modelo de Grade", 'M', 100103102, 4, true, FModGrade.class );
		addOpcao( 100103100, TP_OPCAO_ITEM, "Grades", "Grade", 'r', 100103103, 4, true, FGrade.class );
		addSeparador( 100100000 );
		addOpcao( 100100000, TP_OPCAO_MENU, "Outros cadastros", "", 'C', 100104000, 2, false, null );
		addOpcao( 100104000, TP_OPCAO_ITEM, "Transportadoras", "Transportadora", 'p', 100104010, 3, true, FTransp.class );

		addOpcao( 100104000, TP_OPCAO_ITEM, "Veículos", "Veículos", 'v', 100104011, 3, true, FVeiculo.class );
		addOpcao( 100104000, TP_OPCAO_ITEM, "Motoristas", "Motoristas", 'M', 100104012, 3, true, FMotorista.class );

		addSeparador( 100104000 );
		addOpcao( 100104000, TP_OPCAO_ITEM, "Tipo de cobrança", "TipoCob", 'o', 100104020, 3, true, FTipoCob.class );
		addOpcao( 100104000, TP_OPCAO_ITEM, "Plano de pagamento", "PlanoPag", 's', 100104030, 3, true, FPlanoPag.class );
		addSeparador( 100104000 );
		addOpcao( 100104000, TP_OPCAO_ITEM, "Natureza de operação", "Naturezas", 'z', 100104040, 3, true, FNatoPer.class );
		addSeparador( 100104000 );
		addOpcao( 100100000, TP_OPCAO_MENU, "Atribuições", "", 't', 100105000, 3, false, null );
		addOpcao( 100105000, TP_OPCAO_ITEM, "Atribuições", "Atribuição", 'r', 100105010, 4, true, FAtribuicao.class );
		addOpcao( 100105000, TP_OPCAO_ITEM, "Atribuições por usuário", "Atribuição por usuário", 'u', 100105020, 4, true, FAtribUsu.class );

		addOpcao( 100100000, TP_OPCAO_MENU, "Tabelas Geográficas", "", 'C', 100106000, 2, false, null );
		addOpcao( 100106000, TP_OPCAO_ITEM, "Paises", "Paises", 'P', 100106010, 3, true, FPais.class );
		addOpcao( 100106000, TP_OPCAO_ITEM, "Cidades", "Cidades", 'd', 100106020, 3, true, FMunicipio.class );
		addOpcao( 100106000, TP_OPCAO_ITEM, "Estados", "Estados", 'E', 100106030, 3, true, FUF.class );
		addOpcao( 100106000, TP_OPCAO_ITEM, "Bairros", "Bairros", 'B', 100106040, 3, true, FBairro.class );

		addOpcao( 100000000, TP_OPCAO_MENU, "Ferramentas", "", 'F', 100200000, 1, false, null );
		addOpcao( 100200000, TP_OPCAO_MENU, "Etiquetas", "", 't', 100201000, 2, false, null );
		addOpcao( 100201000, TP_OPCAO_ITEM, "Modelo", "Modelo de etiquetas", 'M', 100201010, 3, true, FModEtiqueta.class );
		addOpcao( 100201000, TP_OPCAO_ITEM, "Imprimir", "Etiquetas", 'I', 100201020, 3, true, FREtiqueta.class ); // LOM
		addSeparador( 100200000 );
		addOpcao( 100200000, TP_OPCAO_ITEM, "Imp. tabelas de fornecedores", "Imp. tabelas de fornecedores", 'I', 100202000, 2, true, FImpTabFor.class );
		addSeparador( 100200000 );
		addOpcao( 100200000, TP_OPCAO_ITEM, "Ajuste do item do orçamento", "Ajuste do item do orçamento", 'A', 100203000, 2, true, FStatusItOrc.class );
		addSeparador( 100200000 );
		addOpcao( 100200000, TP_OPCAO_ITEM, "Fornecedores homologados", "Fornecedores homologados", 'H', 100204000, 2, true, FHomologFor.class );

		addOpcao( 100000000, TP_OPCAO_MENU, "Configurações", "", 'C', 100300000, 1, false, null );
		addOpcao( 100300000, TP_OPCAO_ITEM, "Impressora", "Impressoras", 'I', 100301000, 2, true, FImpressora.class );
		addOpcao( 100300000, TP_OPCAO_ITEM, "Papel", "Papeis", 'P', 100302000, 2, true, FPapel.class );
		addOpcao( 100300000, TP_OPCAO_ITEM, "Estação", "FEstacao", 'E', 100303000, 2, true, FEstacao.class );// lom
		addSeparador( 100300000 );
		addOpcao( 100300000, TP_OPCAO_MENU, "Preferências", "", 'P', 100304000, 2, false, null );
		addOpcao( 100304000, TP_OPCAO_ITEM, "Preferências Gerais", "Preferências Gerais", 'G', 100304010, 3, true, FPrefereGeral.class );
		addOpcao( 100304000, TP_OPCAO_ITEM, "Preferências GMS", "Preferências GMS", 'S', 100304020, 3, true, FPrefereGMS.class );
		addOpcao( 100304000, TP_OPCAO_ITEM, "Série de NFs", "", 'N', 100304030, 3, true, FSerie.class );
		addOpcao( 100304000, TP_OPCAO_ITEM, "Modelo de NFs", "Modelo NF", 'M', 100304040, 3, true, FModNota.class );

		addOpcao( -1, TP_OPCAO_MENU, "Entrada", "", 'E', 200000000, 0, false, null );
		addOpcao( 200000000, TP_OPCAO_ITEM, "Solicitação de Compra", "Solicitação de Compra", 'S', 200100000, 1, true, FSolicitacaoCompra.class );
		addOpcao( 200000000, TP_OPCAO_ITEM, "Pesquisa Solicitações de Compra", "Pesquisa Solicitações de Compra", 'P', 200300000, 1, true, FConsSol.class );
		addOpcao( 200000000, TP_OPCAO_ITEM, "Pesquisa Compra", "Pesquisa Compra", 'P', 200400000, 1, true, FConsCompra.class );
		addSeparador( 200000000 );

		addOpcao( 200000000, TP_OPCAO_ITEM, "Ordem de Compra", "Ordem de Compra", 'O', 200500000, 1, true, FOrdemCompra.class );

		addSeparador( 200000000 );
		addOpcao( 200000000, TP_OPCAO_ITEM, "Gestão de Solicitações de Compra", "Gestão de Solicitações de Compra", 'M', 200300010, 1, true, FGestaoSol.class );
		addOpcao( 200000000, TP_OPCAO_ITEM, "Cotação Sumarizada de Preços", "Cotação Sumarizada de Preços", 'Z', 200300020, 1, true, FCotacaoItens.class );
		addSeparador( 200000000 );
		addOpcao( 200000000, TP_OPCAO_ITEM, "Cotação de Preços", "Cotação de Preços", 'T', 200400000, 1, true, FCotacaoPrecos.class );
		addSeparador( 200000000 );
		addOpcao( 200000000, TP_OPCAO_ITEM, "Compra", "Compra", 'C', 200600000, 1, true, FCompra.class );
		addOpcao( 200000000, TP_OPCAO_ITEM, "Conhecimento de Frete", "Conhecimento de Frete", 'F', 200800000, 1, true, FConhecFrete.class );
		addOpcao( 200000000, TP_OPCAO_ITEM, "Compra (Importação)", "Compra (Importação)", 'F', 200900000, 1, true, FImportacao.class );

		addOpcao( 200000000, TP_OPCAO_MENU, "Listagens", "", 'L', 200700000, 1, false, null );
		addOpcao( 200700000, TP_OPCAO_ITEM, "Compras por fornecedor", "Compras por Fornecedor", 'F', 200701000, 2, true, FRComprasFor.class );
		addOpcao( 200700000, TP_OPCAO_ITEM, "Compras geral", "Compras geral", 'p', 200702000, 2, true, FRCompras.class );
		addOpcao( 200700000, TP_OPCAO_ITEM, "Compras por tipo de movimento ", "Compras por tipo de movimento", 'p', 200703000, 2, true, FRCpTipoMov.class );
		addOpcao( 200700000, TP_OPCAO_ITEM, "Compras por ítem ", "Compras por ítem", 'p', 200704000, 2, true, FRCpItem.class );
		addOpcao( 200700000, TP_OPCAO_ITEM, "Compras por município", "Compras por município", 'm', 200705000, 2, true, FRCpMunicipio.class );
		addSeparador( 200700000 );
		addOpcao( 200700000, TP_OPCAO_ITEM, "Fretes de Rec.Merc.", "Fretes de Rec.Merc.", 'c', 200705000, 2, true, FRFreteRecMerc.class );
		addOpcao( 200700000, TP_OPCAO_ITEM, "Fretes de Expedição", "Fretes de Expedição", 'x', 200706000, 2, true, FRFreteExpedicao.class );
		addSeparador( 200700000 );
		addOpcao( 200700000, TP_OPCAO_ITEM, "Mat.prima por Fornecedor", "Mat.prima por Fornecedor", 'f', 200707000, 2, true, FRMatPrimaFor.class );
		addOpcao( 200000000, TP_OPCAO_ITEM, "Atualiza Fornecedor/Produto", "Atualiza Fornecedor/Produto", 'A', 200800000, 1, true, FAtualizaForneced.class );
		addSeparador( 200700000 );
		addOpcao( 200700000, TP_OPCAO_ITEM, "Ordens de compra pendentes", "Ordens de compra pendentes", 's', 200708000, 2, true, FROCEntregaPrevista.class );

		addOpcao( -1, TP_OPCAO_MENU, "Saída", "", 'S', 300000000, 0, false, null );
		addOpcao( 300000000, TP_OPCAO_ITEM, "Venda", "Venda", 'V', 300100000, 1, true, FVenda.class );
		addOpcao( 300000000, TP_OPCAO_ITEM, "Cancela venda", "Cancelamento", 'C', 300200000, 1, true, FCancVenda.class );
		addOpcao( 300000000, TP_OPCAO_ITEM, "Devolução de vendas", "Devolução de vendas", 'D', 300300000, 1, true, FDevolucao.class );
		addOpcao( 300000000, TP_OPCAO_ITEM, "Lançamento de Frete", "Lançamento de Frete", 'L', 300400000, 1, true, FFrete.class );
		addOpcao( 300000000, TP_OPCAO_ITEM, "Lançamento canhotos de entrega", "Lançamento de canhotos de entrega", 'h', 301500000, 1, true, FEntrega.class );
		addSeparador( 300000000 );
		addOpcao( 300000000, TP_OPCAO_ITEM, "Aprova orçamento", "Aprova Orçamento", 'A', 300500000, 1, true, FAprovCancOrc.class );
		addOpcao( 300000000, TP_OPCAO_ITEM, "Orçamento", "Orçamento", 'O', 300600000, 1, true, FOrcamento.class );
		addOpcao( 300000000, TP_OPCAO_ITEM, "Pesquisa Orçamento", "Pesquisa Orçamento", 'P', 300700000, 1, true, FPesquisaOrc.class );
		addSeparador( 300000000 );
		addOpcao( 300000000, TP_OPCAO_ITEM, "Romaneio", "Romaneio", 'R', 300800000, 1, true, FRomaneio.class );
		addSeparador( 300000000 );
		addOpcao( 300000000, TP_OPCAO_ITEM, "Requisição de material", "Requisição de material", 'm', 300900000, 1, true, FRma.class );
		addOpcao( 300000000, TP_OPCAO_ITEM, "Pesquisa requisição de material", "Pesquisa requisição de material", 'm', 301300000, 1, true, FConsRMA.class );
		addOpcao( 300000000, TP_OPCAO_ITEM, "Pesquisa item de Rma", "Pesquisa item de requisição de material", 'i', 301400000, 1, true, FConsRmaItem.class );
		addSeparador( 300000000 );
		addOpcao( 300000000, TP_OPCAO_MENU, "Listagens", "", 's', 301000000, 1, false, null );
		addOpcao( 301000000, TP_OPCAO_ITEM, "Resumo diário", "Resumo Diário", 'R', 301001000, 2, true, FRResumoDiario.class );
		addOpcao( 301000000, TP_OPCAO_ITEM, "Vendas geral", "Vendas em Geral", 'V', 301002000, 2, true, FRVendasGeral.class );
		addOpcao( 301000000, TP_OPCAO_ITEM, "Vendas físico", "Físico de Vendas", 'd', 301003000, 2, true, FRVendasFisico.class );
		addOpcao( 301000000, TP_OPCAO_ITEM, "Vendas detalhado", "Vendas Detalhadas", 'n', 301004000, 2, true, FRVendasDet.class );
		addOpcao( 301000000, TP_OPCAO_ITEM, "Vendas por ítem", "Vendas por Item", 'e', 301005000, 2, true, FRVendasItem.class );
		addOpcao( 301000000, TP_OPCAO_ITEM, "Média de vendas por ítem", "Media de vendas por item", 'o', 301006000, 2, true, FRMediaItem.class );
		addOpcao( 301000000, TP_OPCAO_ITEM, "Ultimas Vendas por Cliente", "", 'C', 301007000, 2, true, FRUltimaVenda.class );
		addOpcao( 301000000, TP_OPCAO_ITEM, "Vendas por Setor", "Vendas por Setor", 't', 301008000, 2, true, FRVendaSetor.class );
		addOpcao( 300000000, TP_OPCAO_MENU, "Gráficos", "", 'G', 301100000, 1, false, null );
		addOpcao( 301100000, TP_OPCAO_ITEM, "Evolução de vendas", "Evolução de vendas", 'E', 301101000, 2, true, FREvoluVendas.class );
		addSeparador( 300000000 );
		addOpcao( 300000000, TP_OPCAO_MENU, "Consultas", "", 'n', 301200000, 1, false, null );
		addOpcao( 301200000, TP_OPCAO_ITEM, "Preços", "Consulta de preços", 'P', 301201000, 2, true, FConsPreco.class );

		addOpcao( -1, TP_OPCAO_MENU, "Estoque", "", 'E', 400000000, 0, false, null );
		addOpcao( 400000000, TP_OPCAO_ITEM, "Kardex", "Kardex", 'K', 400100000, 1, true, FKardex.class );
		addOpcao( 400000000, TP_OPCAO_ITEM, "Mov. Numero de Série", "Mov. Numero de Série", 'M', 400200000, 1, true, FMovSerie.class );
		addOpcao( 400000000, TP_OPCAO_ITEM, "Inventário", "Inventário", 'I', 400300000, 1, true, FInventario.class );
		addOpcao( 400000000, TP_OPCAO_ITEM, "Consulta estoque", "Consulta", 'C', 400400000, 1, true, FConsEstoque.class );
		addOpcao( 400000000, TP_OPCAO_ITEM, "Consulta preço", "Consulta preço", 'P', 400500000, 1, true, FConsPreco.class );
		addOpcao( 400000000, TP_OPCAO_ITEM, "Tipos de movimentos", "Tipo de Movimento", 'T', 400600000, 1, true, FTipoMov.class );
		addSeparador( 400000000 );
		addOpcao( 400000000, TP_OPCAO_ITEM, "Reprocessa estoque", "Reprocessa estoque", 'R', 400700000, 1, true, FProcessaEQ.class );
		addSeparador( 400000000 );
		addOpcao( 400000000, TP_OPCAO_MENU, "Listagens", "", 'L', 400800000, 1, false, null );
		addOpcao( 400800000, TP_OPCAO_ITEM, "Estoque mínimo", "Estoque Mínimo", 's', 400801000, 2, true, FREstoqueMin.class );
		addOpcao( 400800000, TP_OPCAO_ITEM, "Produtos/Movimentos", "Listagem de Produtos", 'P', 400802000, 2, true, FRMovProd.class );
		addOpcao( 400800000, TP_OPCAO_ITEM, "Vencimentos de lote", "Vencimento Lote", 'V', 400803000, 2, true, FRVencLote.class );
		addOpcao( 400800000, TP_OPCAO_ITEM, "Saldos de lote", "Saldos de Lote", 'l', 400804000, 2, true, FRSaldoLote.class );
		addOpcao( 400800000, TP_OPCAO_ITEM, "Demanda", "Demanda", 'D', 400805000, 2, true, FRDemanda.class );
		addOpcao( 400800000, TP_OPCAO_ITEM, "Conferência", "Conferência de Estoque", 'C', 400806000, 2, true, FRConfEstoq.class );
		addOpcao( 400800000, TP_OPCAO_ITEM, "Inventário", "Inventário", 'I', 400807000, 2, true, FRInvPeps.class );

		addOpcao( 400800000, TP_OPCAO_ITEM, "Codifição de produto", "Codificação de produto", 'P', 400800800, 2, true, FRCodficProd.class );
		addOpcao( 400800000, TP_OPCAO_ITEM, "Etiquetas de código de barras", "Etiquetas de código de barras", 'E', 400800900, 2, true, FRCodbarProd.class );
		addOpcao( 400800000, TP_OPCAO_ITEM, "Ultimas compras/produto", "Ultimas compras/produto", 'E', 400801000, 2, true, FRCpProd.class );
		addOpcao( 400800000, TP_OPCAO_ITEM, "Movimentação de Produto Controlado", "Movimentação de Produto Controlado", 'M', 400802000, 2, true, FRMovProdCont.class );
		addOpcao( 400800000, TP_OPCAO_ITEM, "Estoque liquido", "Estoque liquido", 'L', 400803000, 2, true, FREstoqueLiquido.class );
		addOpcao( 400800000, TP_OPCAO_ITEM, "Produtos por grupo", "Produtos pro grupo", 'G', 400804000, 2, true, FRProdGrup.class );
		addOpcao( 400800000, TP_OPCAO_ITEM, "Giro de estoque", "Giro de estoque", 'i', 400805000, 2, true, FRGiroEstoque.class );
		addOpcao( 400800000, TP_OPCAO_ITEM, "Contagem de estoque", "Contagem de estoque", 's', 700706000, 2, true, FRContaEstoque.class );
		addOpcao( 400800000, TP_OPCAO_ITEM, "Valor em estoque", "Valor em estoque", 'v', 400807000, 2, true, FRValorEstoque.class );

		addOpcao( -1, TP_OPCAO_MENU, "Recepção", "", 'R', 500000000, 0, false, null );
		addOpcao( 500000000, TP_OPCAO_ITEM, "Painel de controle", "Painel de Controle", 'P', 500100000, 1, true, FControleRecMerc.class );
		addSeparador( 500000000 );
		addOpcao( 500000000, TP_OPCAO_ITEM, "Recepção de matéria prima", "Recepção de matéria prima", 'R', 500200000, 1, true, FRecMerc.class );
		addOpcao( 500000000, TP_OPCAO_ITEM, "Coleta de materiais", "Coleta de materiais", 'C', 500400000, 1, true, FColeta.class );
		addSeparador( 500000000 );
		addOpcao( 500000000, TP_OPCAO_ITEM, "Tipos de Recepção", "Cadastro de tipos de recepção de mercadorias", 'T', 500300000, 1, true, FTipoRecMerc.class );
		addSeparador( 500000000 );
		addOpcao( 500000000, TP_OPCAO_MENU, "Listagens", "", 'L', 500500000, 1, false, null );
		addOpcao( 500500000, TP_OPCAO_ITEM, "Coletas por dia", "Coletas por dia", 'o', 500501000, 2, true, FRColetas.class );
		addOpcao( 500500000, TP_OPCAO_ITEM, "Entradas/peso", "Relação de entradas/peso", 'e', 500501100, 2, true, FRPesoRecMerc.class );
		addOpcao( 500500000, TP_OPCAO_ITEM, "Preço médio/diário", "Preço médio/diário", 'P', 500501200, 2, true, FRPrecoMedioRecMerc.class );

		addOpcao( -1, TP_OPCAO_MENU, "Expedição", "", 'x', 600000000, 0, false, null );

		addOpcao( 600000000, TP_OPCAO_ITEM, "Painel de controle", "Painel de Controle", 'P', 600100000, 1, true, FControleExpedicao.class );
		addSeparador( 600000000 );
		addOpcao( 600000000, TP_OPCAO_ITEM, "Expedição de produto acabado", "Expedição de produto acabado", 'R', 600200000, 1, true, FExpedicao.class );
		addOpcao( 600000000, TP_OPCAO_ITEM, "Tipos de Expedição", "Cadastro de tipos de expedição de produtos", 'T', 600300000, 1, true, FTipoExpedicao.class );
		addSeparador( 600000000 );

		addOpcao( 600000000, TP_OPCAO_ITEM, "Pesquisa requisição de material", "Pesquisa requisição de material", 'm', 600100000, 1, true, FConsRMA.class );
		addOpcao( 600000000, TP_OPCAO_ITEM, "Pesquisa item de Rma", "Pesquisa item de requisição de material", 'i', 600200000, 1, true, FConsRmaItem.class );
		addOpcao( 600000000, TP_OPCAO_ITEM, "Baixa RMA via Cód.Barras", "Baixa RMA", 'B', 600300000, 1, true, FBaixaRMACodBar.class );

		addSeparador( 600000000 );

		addOpcao( 600000000, TP_OPCAO_MENU, "Listagens", "", 'L', 600400000, 1, false, null );

		addOpcao( -1, TP_OPCAO_MENU, "Serviços", "", 'S', 700000000, 0, false, null );
		addOpcao( 700000000, TP_OPCAO_ITEM, "Painel de controle", "Painel de controle de Serviços", 'e', 700100000, 1, true, FControleServicos.class );
		addOpcao( 700000000, TP_OPCAO_ITEM, "Ordem de Serviço", "Ordem de Serviço", 'e', 700200000, 1, true, FOrdemServico.class );
		addSeparador( 700000000 );
		addOpcao( 700000000, TP_OPCAO_ITEM, "Tipo de atendente", "Tipo de atendente", 'T', 700300000, 1, true, FTipoAtend.class );
		addOpcao( 700000000, TP_OPCAO_ITEM, "Atendente", "Atendente", 'd', 700400000, 1, true, FAtendente.class );

		addSeparador( 700000000 );
		addOpcao( 700000000, TP_OPCAO_ITEM, "Gestão de relacionamento com clientes", "Gestão de relacionamento com clientes", 'A', 700500000, 1, true, FCRM.class );
		addSeparador( 700000000 );
		addOpcao( 700000000, TP_OPCAO_ITEM, "Chamados", "Chamados", 'A', 700600000, 1, true, FChamado.class );
		addSeparador( 700000000 );
		addOpcao( 700000000, TP_OPCAO_MENU, "Listagens", "", 'L', 700700000, 1, false, null );

		addOpcao( -1, TP_OPCAO_MENU, "Produção", "", 'P', 800000000, 0, false, null );
		addOpcao( 800000000, TP_OPCAO_ITEM, "Ordens de produção", "Ordens de produção", 'O', 800100000, 1, true, FOP.class );
		addOpcao( 800000000, TP_OPCAO_ITEM, "Simulação de OP", "Simulação de OP", 'S', 800200000, 1, true, FSimulaOP.class );
		addOpcao( 800000000, TP_OPCAO_ITEM, "Acompanhamento da produção", "Acompanhamento da produção", 'o', 800300000, 1, true, FAcompanhaProd.class );
		addOpcao( 800000000, TP_OPCAO_ITEM, "Planejamento mestre da produção (Pull)", "Planejamento mestre da produção (Pull)", 'P', 800400000, 1, true, FPMP_Pull.class );
		addOpcao( 800000000, TP_OPCAO_ITEM, "Planejamento mestre da produção (Push)", "Planejamento mestre da produção (Push)", 'P', 800500000, 1, true, FPMP_Push.class );
		addSeparador( 800000000 );
		addOpcao( 800000000, TP_OPCAO_ITEM, "Contra Prova", "Contra prova", 'P', 800400000, 1, true, FContraProva.class );
		addSeparador( 800000000 );
		addOpcao( 800000000, TP_OPCAO_ITEM, "Requisição de material", "Requisição de material", 'R', 800500000, 1, true, FRma.class );
		addOpcao( 800000000, TP_OPCAO_ITEM, "Pesquisa requisição de material", "Pesquisa requisição de material", 'P', 800600000, 1, true, FConsRMA.class );
		addOpcao( 800000000, TP_OPCAO_ITEM, "Pesquisa RMA por item", "Pesquisa RMA por item", 'i', 800700000, 1, true, FConsRmaItem.class );
		addOpcao( 800000000, TP_OPCAO_ITEM, "Baixa RMA via Cód.Barras", "Baixa RMA", 'B', 800800000, 1, true, FBaixaRMACodBar.class );
		// addOpcao( 800000000, TP_OPCAO_ITEM, "Teste Java", "Teste Java", 'T', 800800200, 1, true, FTesteJava.class );

		addSeparador( 800000000 );
		addOpcao( 800000000, TP_OPCAO_MENU, "Listagens", "", 'L', 800900000, 1, false, null );
		addOpcao( 800900000, TP_OPCAO_ITEM, "Certificação de análise", "Certificação de análise", 'C', 800900100, 1, true, FRCertAnalise.class );
		addOpcao( 800900000, TP_OPCAO_ITEM, "Análises", "Análises", 'C', 800900200, 1, true, FRAnalise.class );
		addOpcao( 800900000, TP_OPCAO_ITEM, "Contra-Provas", "Contra-Provas", 'C', 800900300, 1, true, FRContraProva.class );
		addOpcao( 800900000, TP_OPCAO_ITEM, "Produção", "Produção", 'P', 800900400, 1, true, FRProducao.class );
		addOpcao( 800900000, TP_OPCAO_ITEM, "Estruturas por item", "Estruturas por item", 'z', 800900500, 1, true, FREstruturaItem.class );
		addOpcao( 800900000, TP_OPCAO_ITEM, "Comissionamento/produtividade", "Comissionamento/produtividade", 'C', 800900600, 1, true, FRComisProd.class );

		addBotao( "btPrefere.png", "Preferências gerais", "Preferências Gerais", 100304010, FPrefereGeral.class );
		addBotao( "btCliente.png", "Cliente", "Clientes", 100101010, FCliente.class );
		addBotao( "btForneced.png", "Fornecedor", "Fornecedor", 100102020, FFornecedor.class );
		addBotao( "btRma.png", "Requisição de material", "Requisição de material", 300900000, FRma.class );
		addBotao( "btOP.png", "Ordens de Produção", "Ordens de Produção", 800100000, FOP.class );
		addBotao( "btsoliccp.png", "Solicitação de Compra", "Solicitação de Compra", 200100000, FSolicitacaoCompra.class );

		addBotao( "btEntrada.png", "Compra", "Compras", 200600000, FCompra.class );

		addBotao( "btTransp.png", "Transportadora", "Transportadora", 100117000, FTransp.class );
		addBotao( "btConFrete.png", "Conhecimento de Frete", "Conhecimento de Frete", 200300000, FConhecFrete.class );

		addBotao( "btEstoque.png", "Consulta estoque", "Consulta", 400300000, FConsEstoque.class );
		addBotao( "btProduto.png", "Cadastro de produtos", "Produtos", 100103090, FProduto.class );
		addBotao( "btSimilar.png", "Cadastro de similaridades", "Similaridade", 100103080, FSimilar.class );
		addBotao( "btOrcamento.png", "Orçamento", "Orcamento", 300600000, FOrcamento.class );
		addBotao( "btConsOrcamento.png", "Pesquisa Orçamentos", "Pesquisa Orcamentos", 300700000, FPesquisaOrc.class );
		addBotao( "btAprovaOrc.png", "Aprovações de Orçamentos", "Aprova Orcamento", 300500000, FAprovCancOrc.class );

		addBotao( "btAtendimentos.png", "Gestão de relacionamento com clientes", "Gestão de relacionamento com clientes", 700500000, FCRM.class );
		addBotao( "btChamado.png", "Chamados", "Chamados", 700600000, FChamado.class );

		addBotao( "btColeta.png", "Coleta de materiais", "Coleta de Materiais", 500400000, FColeta.class );
		addBotao( "btRecMatPrim.png", "Painel de Controle", "Painel de Controle", 500100000, FControleRecMerc.class );

		addBotao( "btServico.png", "Ordem de Serviço", "Ordem de Serviço", 700200000, FOrdemServico.class );
		addBotao( "btPainelServico.png", "Painel de Controle de Serviços", "Painel de Controle de Serviços", 700100000, FControleServicos.class );

		ajustaMenu();

		nomemodulo = "Gestão de Materiais e Serviços";

	}

	public static void main( String sParams[] ) {

		try {
			Aplicativo.setLookAndFeel( "freedom.ini" );
			FreedomGMS freedom = new FreedomGMS();
			freedom.show();
		} catch ( Throwable e ) {
			Funcoes.criaTelaErro( "Erro de execução" );
			e.printStackTrace();
		}
	}
}
