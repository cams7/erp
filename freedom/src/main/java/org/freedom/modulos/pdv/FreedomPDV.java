/**
 * @version 10/06/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.pdv <BR>
 *         Classe:
 * @(#)FreedomPDV.java <BR>
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
 *                     Tela principal do múdulo ponto de venda.
 * 
 */

package org.freedom.modulos.pdv;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.frame.AplicativoPDV;
import org.freedom.modulos.fnc.view.frame.crud.plain.FBanco;
import org.freedom.modulos.gms.view.frame.crud.special.FGrupoProd;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.std.view.frame.crud.detail.FModGrade;
import org.freedom.modulos.std.view.frame.crud.detail.FPlanoPag;
import org.freedom.modulos.std.view.frame.crud.detail.FSimilar;
import org.freedom.modulos.std.view.frame.crud.plain.FAlmox;
import org.freedom.modulos.std.view.frame.crud.plain.FClasCli;
import org.freedom.modulos.std.view.frame.crud.plain.FMarca;
import org.freedom.modulos.std.view.frame.crud.plain.FTabPreco;
import org.freedom.modulos.std.view.frame.crud.plain.FTipoCli;
import org.freedom.modulos.std.view.frame.crud.plain.FTipoFisc;
import org.freedom.modulos.std.view.frame.crud.plain.FUnidade;
import org.freedom.modulos.std.view.frame.crud.plain.FVariantes;
import org.freedom.modulos.std.view.frame.crud.special.FGrade;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCliente;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCredCli;
import org.freedom.modulos.std.view.frame.crud.tabbed.FMoeda;
import org.freedom.modulos.std.view.frame.report.FRListaPreco;
import org.freedom.modulos.std.view.frame.utility.FCpProd;
import org.freedom.modulos.std.view.frame.utility.FManutPreco;

public class FreedomPDV extends AplicativoPDV {

	protected JButtonPad btVenda = null;

	public FreedomPDV() {

		super( "iconpdv.png", "splashPDV.png", 1, "Freedom", 3, "Ponto de Venda", null );

		addOpcao( -1, TP_OPCAO_MENU, "Arquivo", "", 'A', 100000000, 0, false, null );
		addOpcao( 100000000, TP_OPCAO_MENU, "Tabelas", "", 'T', 100100000, 1, false, null );
		addOpcao( 100100000, TP_OPCAO_MENU, "Cliente", "", 'C', 100101000, 2, false, null );
		addOpcao( 100101000, TP_OPCAO_ITEM, "Tipo de cliente...", "TipoCli", 'T', 100101010, 3, true, FTipoCli.class );
		addOpcao( 100101000, TP_OPCAO_ITEM, "Classificação de cliente...", "Classifição de Clientes", 'f', 100101020, 3, true, FClasCli.class );
		addOpcao( 100101000, TP_OPCAO_ITEM, "Cliente...", "Clientes", 'C', 100101030, 3, true, FCliente.class );
		addOpcao( 100101000, TP_OPCAO_ITEM, "Tipo fiscal de cliente...", "Tipo Fiscal de Cliente", 'p', 100101040, 3, true, FTipoFisc.class );
		addOpcao( 100101000, TP_OPCAO_ITEM, "Crédito por cliente...", "Crédito por cliente", 'r', 100101050, 3, true, FCredCli.class );
		addSeparador( 100100000 );
		addOpcao( 100100000, TP_OPCAO_ITEM, "Moeda", "Moeda", 'M', 100102000, 2, true, FMoeda.class );
		addOpcao( 100100000, TP_OPCAO_ITEM, "Banco", "Banco", 'B', 100103000, 2, true, FBanco.class );
		addOpcao( 100100000, TP_OPCAO_ITEM, "Plano de pagamento", "PlanoPag", 's', 100115000, 2, true, FPlanoPag.class );
		addSeparador( 100100000 );
		addOpcao( 100100000, TP_OPCAO_MENU, "Produto", "", 'u', 100104000, 2, false, null );
		addOpcao( 100104000, TP_OPCAO_ITEM, "Almoxarifado", "Almoxarifado", 'x', 100104030, 3, true, FAlmox.class );
		addOpcao( 100104000, TP_OPCAO_ITEM, "Grupo", "Grupos", 'r', 100104040, 3, true, FGrupoProd.class );
		addOpcao( 100104000, TP_OPCAO_ITEM, "Marca", "Marcas", 'c', 100104050, 3, true, FMarca.class );
		addOpcao( 100104000, TP_OPCAO_ITEM, "Unidade", "Unidades", 'U', 100104060, 3, true, FUnidade.class );
		addOpcao( 100104000, TP_OPCAO_ITEM, "Produto", "Produtos", 'P', 100104070, 3, true, FProduto.class );
		addSeparador( 100104000 );
		addOpcao( 100104000, TP_OPCAO_ITEM, "Similaridade", "Similar", 'S', 100104080, 3, true, FSimilar.class );
		addOpcao( 100104000, TP_OPCAO_MENU, "Grade de produtos", "", 'G', 100104090, 3, false, null );
		addOpcao( 100104090, TP_OPCAO_ITEM, "Variantes", "Variantes", 'V', 100104091, 4, true, FVariantes.class );
		addOpcao( 100104090, TP_OPCAO_ITEM, "Modelo", "Modelo de Grade", 'M', 100104092, 4, true, FModGrade.class );
		addOpcao( 100104090, TP_OPCAO_ITEM, "Grade", "Grade", 'r', 100104093, 4, true, FGrade.class );
		addSeparador( 100100000 );
		addOpcao( 100100000, TP_OPCAO_MENU, "Preço", "", 'ç', 100105000, 2, false, null );
		addOpcao( 100105000, TP_OPCAO_ITEM, "Manutenção de Preços", "Manutenção de Preços", 'M', 100105010, 3, true, FManutPreco.class );
		addOpcao( 100105000, TP_OPCAO_ITEM, "Copia preço", "Copia Precos", 'i', 100105020, 3, true, FCpProd.class );
		addOpcao( 100105000, TP_OPCAO_ITEM, "Tabela de preço", "Tabelas de Preços", 'a', 100105030, 3, true, FTabPreco.class );
		addOpcao( 100105000, TP_OPCAO_ITEM, "Lista de preço", "Lista de Preços", 'l', 100105040, 3, true, FRListaPreco.class );

		addOpcao( 100000000, TP_OPCAO_MENU, "Preferências", "", 'P', 100200000, 1, false, null );
		addOpcao( 100200000, TP_OPCAO_ITEM, "Preferências gerais", "Prefere Geral", 'g', 100201000, 2, true, FPreferePDV.class );
		addOpcao( 100200000, TP_OPCAO_ITEM, "Funções Administrativas TEF", "Admin TEF", 'A', 100202000, 2, true, FAdmTef.class );

		addOpcao( -1, TP_OPCAO_MENU, "PDV", "", 'P', 200000000, 1, false, null );
		addOpcao( 200000000, TP_OPCAO_ITEM, "Venda", "Venda", 'V', 200100000, 2, true, FVenda.class );
		addOpcao( 200000000, TP_OPCAO_ITEM, "Cancela venda", "Cancela Venda", 'C', 200200000, 2, true, DLCancCupom.class );
		addSeparador( 200000000 );
		addOpcao( 200000000, TP_OPCAO_ITEM, "Suprimento", "Suprimento de caixa", 'u', 200300000, 2, true, FSuprimento.class );
		addOpcao( 200000000, TP_OPCAO_ITEM, "Sangria", "Sangria de caixa", 'S', 200400000, 2, true, FSangria.class );
		addSeparador( 200000000 );
		addOpcao( 200000000, TP_OPCAO_ITEM, "Aliquota", "Inserir Aliquota", 'A', 200500000, 2, true, FAliquota.class );
		addOpcao( 200000000, TP_OPCAO_ITEM, "Ajusta moeda", "Grava Moeda", 'M', 200600000, 2, true, FGravaMoeda.class );
		addSeparador( 200000000 );
		addOpcao( 200000000, TP_OPCAO_ITEM, "Ler memória fiscal", "Le Fiscal", 'L', 200700000, 2, true, FLeFiscal.class );
		addOpcao( 200000000, TP_OPCAO_ITEM, "Leitura X", "Impressão de leitura X", 'X', 110800000, 2, true, FLeituraX.class );
		addSeparador( 200000000 );
		addOpcao( 200000000, TP_OPCAO_ITEM, "Fechamento de Caixa", "Efetua um fechamento de caixa", 'F', 110800001, 2, true, DLFechaDia.class );
		addOpcao( -1, TP_OPCAO_MENU, "Receber", "", 'R', 300000000, 1, false, null );
		addOpcao( 300000000, TP_OPCAO_ITEM, "Receber", "Receber", 'R', 300100000, 2, true, FManutRec.class );

		btVenda = addBotao( "barraVenda.gif", "Venda", "Venda", 200100000, FVenda.class );
		addBotao( "btExcluir.png", "Cancela venda", "Cancela Venda", 200200000, DLCancCupom.class );
		addBotao( "barraFornecedor.gif", "Suprimento", "Suprimento de caixa", 200300000, FSuprimento.class );
		addBotao( "btPdvSangria.gif", "Sangria", "Sangria", 200400000, FSangria.class );
		addBotao( "btPdvAliquota.gif", "Aliquota", "Inserir Aliquota", 200500000, FAliquota.class );
		addBotao( "btPdvGravaMoeda.png", "Ajusta moeda", "Grava Moeda", 200600000, FGravaMoeda.class );
		addBotao( "btPdvLeituraXPq.gif", "Ler memória fiscal", "Le Fiscal", 200700000, FLeFiscal.class );

		ajustaMenu();
		nomemodulo = "Ponto de Venda";

	}

	public static void main( String sParams[] ) {

		try {
			AplicativoPDV.setLookAndFeel( "freedom.ini" );
			FreedomPDV freedom = new FreedomPDV();
			freedom.setECF();
			FreedomPDV.loadEcflayout();
			freedom.show();
			freedom.btVenda.doClick();
		} catch ( Throwable e ) {
			Funcoes.criaTelaErro( "Erro de execução\n\n" + e.getMessage() );
			e.printStackTrace();
		}
	}
}
