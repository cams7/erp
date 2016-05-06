/**
 * @version 29/12/2003 <BR>
 * @author Setpoint Informática Ltda.
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.fnc <BR>
 *         Classe:
 * @(#)FreedomFNC.java <BR>
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
 *                     Tela principal do módulo financeiro.
 * 
 */

package org.freedom.modulos.lvf;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.AplicativoPD;
import org.freedom.library.swing.frame.FPrincipalPD;
import org.freedom.library.swing.frame.LoginPD;
import org.freedom.modulos.lvf.view.frame.crud.plain.FModDocFisc;
import org.freedom.modulos.lvf.view.frame.crud.plain.FTabICMS;
import org.freedom.modulos.lvf.view.frame.report.FRIcms;
import org.freedom.modulos.lvf.view.frame.report.FRIcmsNcm;
import org.freedom.modulos.lvf.view.frame.report.FRIpi;
import org.freedom.modulos.lvf.view.frame.report.FRMovPisCofins;
import org.freedom.modulos.lvf.view.frame.report.FRPisCofins;
import org.freedom.modulos.lvf.view.frame.report.FRRegitroEntrada;
import org.freedom.modulos.lvf.view.frame.report.FRRegitroInventario;
import org.freedom.modulos.lvf.view.frame.report.FRRegitroSaida;
import org.freedom.modulos.lvf.view.frame.utility.FSintegra;
import org.freedom.modulos.std.view.frame.crud.detail.FEmpresa;
import org.freedom.modulos.std.view.frame.crud.plain.FClasCli;
import org.freedom.modulos.std.view.frame.crud.plain.FEstacao;
import org.freedom.modulos.std.view.frame.crud.plain.FImpressora;
import org.freedom.modulos.std.view.frame.crud.plain.FMensagem;
import org.freedom.modulos.std.view.frame.crud.plain.FModNota;
import org.freedom.modulos.std.view.frame.crud.plain.FPapel;
import org.freedom.modulos.std.view.frame.crud.plain.FSetor;
import org.freedom.modulos.std.view.frame.crud.plain.FTipoCli;
import org.freedom.modulos.std.view.frame.crud.plain.FTipoFor;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCliente;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCredCli;
import org.freedom.modulos.std.view.frame.crud.tabbed.FFornecedor;
import org.freedom.modulos.std.view.frame.crud.tabbed.FMoeda;
import org.freedom.modulos.std.view.frame.crud.tabbed.FVendedor;
import org.freedom.modulos.std.view.frame.report.FRImpServ;
import org.freedom.modulos.std.view.frame.report.FRRegDuplicatas;
import org.freedom.modulos.std.view.frame.report.FRegraFiscal;
import org.freedom.modulos.std.view.frame.utility.FGeraFiscal;

public class FreedomLVF extends AplicativoPD {

	public FreedomLVF() {

		super( "iconlvf.png", "splashLVF.png", 1, "Freedom", 10, "Livros Fiscais", null, new FPrincipalPD( null, "bgFreedom2.jpg" ), LoginPD.class );

		addOpcao( -1, TP_OPCAO_MENU, "Arquivo", "", 'A', 100000000, 0, false, null );
		addOpcao( 100000000, TP_OPCAO_MENU, "Tabelas", "", 'T', 100100000, 1, false, null );
		addOpcao( 100100000, TP_OPCAO_MENU, "Cliente", "", 'C', 100101000, 2, false, null );
		addOpcao( 100101000, TP_OPCAO_ITEM, "Setor", "Setor", 'S', 100101010, 3, true, FSetor.class );
		addOpcao( 100101000, TP_OPCAO_ITEM, "Comissionado", "Comissionado", 's', 100101020, 3, true, FVendedor.class );
		addOpcao( 100101000, TP_OPCAO_ITEM, "Tipo de cliente", "TipoCli", 'T', 100101030, 3, true, FTipoCli.class );
		addOpcao( 100101000, TP_OPCAO_ITEM, "Classificação de cliente", "Classifição de Clientes", 'f', 100101040, 3, true, FClasCli.class );
		addOpcao( 100101000, TP_OPCAO_ITEM, "Cliente", "Clientes", 'C', 100101050, 3, true, FCliente.class );
		addOpcao( 100101000, TP_OPCAO_ITEM, "Crédito por cliente", "Crédito por cliente", 'r', 100101060, 3, true, FCredCli.class );
		addSeparador( 100100000 );
		addOpcao( 100100000, TP_OPCAO_ITEM, "Moeda", "Moeda", 'M', 100102000, 2, true, FMoeda.class );
		addSeparador( 100100000 );
		addOpcao( 100100000, TP_OPCAO_ITEM, "Tipo de fornecedor", "TipoFor", 'i', 100107000, 2, true, FTipoFor.class );
		addOpcao( 100100000, TP_OPCAO_ITEM, "Fornecedor", "Fornecedor", 'F', 100108000, 2, true, FFornecedor.class );
		addSeparador( 100100000 );
		addOpcao( 100000000, TP_OPCAO_MENU, "Ferramentas", "", 'F', 100200000, 1, false, null );
		addOpcao( 100000000, TP_OPCAO_MENU, "Preferências", "", 'P', 100300000, 1, false, null );
		addOpcao( 100000000, TP_OPCAO_MENU, "Configurações", "", 'C', 100400000, 1, false, null );
		addOpcao( 100400000, TP_OPCAO_ITEM, "Estação de trabalho", "Estações de trabalho", 't', 100401000, 2, true, FEstacao.class );
		addOpcao( 100400000, TP_OPCAO_ITEM, "Impressora", "Impressoras", 'I', 100402000, 2, true, FImpressora.class );
		addOpcao( 100400000, TP_OPCAO_ITEM, "Papel", "Papeis", 'P', 100403000, 2, true, FPapel.class );
		addSeparador( 100400000 );
		addOpcao( 100400000, TP_OPCAO_ITEM, "Empresa", "Empresa", 'E', 100404000, 2, true, FEmpresa.class );

		addOpcao( -1, TP_OPCAO_MENU, "Fiscal", "", 'F', 200000000, 0, false, null );
		addOpcao( 200000000, TP_OPCAO_ITEM, "Gerar", "Gera Fiscal", 'G', 200100000, 1, true, FGeraFiscal.class );
		addOpcao( 200000000, TP_OPCAO_ITEM, "Regras fiscais", "Regras Fiscais", 'R', 200200000, 1, true, FRegraFiscal.class );
		addOpcao( 200000000, TP_OPCAO_ITEM, "Sintegra", "Gera Arquivo Sintegra", 'S', 200300000, 1, true, FSintegra.class );
		addOpcao( 200000000, TP_OPCAO_ITEM, "Tabela de Alíquotas", "Tabela de alíquotas", 'T', 200400000, 1, true, FTabICMS.class );
		addOpcao( 200000000, TP_OPCAO_ITEM, "Mensagens", "Mensagens", 'M', 200500000, 1, true, FMensagem.class );
		addOpcao( 200000000, TP_OPCAO_MENU, "Listagens", "", 'L', 200600000, 1, false, null );
		addOpcao( 200600000, TP_OPCAO_ITEM, "ICMS", "ICMS", 'I', 200601000, 2, true, FRIcms.class );
		addOpcao( 200600000, TP_OPCAO_ITEM, "ICMS por NCM/CFOP", "ICMS por NCM/CFOP", 'N', 200604000, 2, true, FRIcmsNcm.class );
		addOpcao( 200600000, TP_OPCAO_ITEM, "IPI", "IPI", 'P', 200606000, 2, true, FRIpi.class );
		addOpcao( 200600000, TP_OPCAO_ITEM, "Impostos sobre serviços", "Impostos sobre serviços", 'S', 200602000, 2, true, FRImpServ.class );
		addOpcao( 800600000, TP_OPCAO_ITEM, "PIS/COFINS", "Mov. PIS/COFINS", 'P', 200605000, 2, true, FRPisCofins.class );
		addOpcao( 200600000, TP_OPCAO_ITEM, "Movimentação com PIS e COFINS", "Movimentação com PIS e COFINS", 'P', 200603000, 2, true, FRMovPisCofins.class );

		addOpcao( 200600000, TP_OPCAO_ITEM, "Modelo de NFs", "Modelo de NFs", 'M', 200607000, 2, true, FModNota.class );
		addOpcao( 200600000, TP_OPCAO_ITEM, "Modelo de Doc. Fiscais", "Modelo de Doc. Fiscais", 'i', 200608000, 2, true, FModDocFisc.class );
		
		addOpcao( -1, TP_OPCAO_MENU, "Entrada", "", 'E', 300000000, 0, false, null );
		addOpcao( 300000000, TP_OPCAO_ITEM, "Registro de Entrada", "Registro de Entrada", 'E', 300100000, 1, true, FRRegitroEntrada.class );

		addOpcao( -1, TP_OPCAO_MENU, "Saida", "", 'S', 400000000, 0, false, null );
		addOpcao( 400000000, TP_OPCAO_ITEM, "Registro de Saida", "Registro de Saida", 'S', 400100000, 1, true, FRRegitroSaida.class );
		addOpcao( 400000000, TP_OPCAO_ITEM, "Registro de Duplicatas", "Registro de Duplicatas", 'S', 400200000, 1, true, FRRegDuplicatas.class);

		addOpcao( -1, TP_OPCAO_MENU, "Estoque", "", 't', 500000000, 0, false, null );
		addOpcao( 500000000, TP_OPCAO_ITEM, "Registro de Inventário", "Registro de Inventário", 'E', 500100000, 1, true, FRRegitroInventario.class );

		addBotao( "barraUsuario.png", "Cliente", "Clientes", 100101050, FCliente.class );
		addBotao( "btForneced.png", "Fornecedor", "Fornecedor", 100108000, FFornecedor.class );

		ajustaMenu();

		nomemodulo = "Livros fiscais";

	}

	public static void main( String sParams[] ) {

		try {
			Aplicativo.setLookAndFeel( "freedom.ini" );
			FreedomLVF freedom = new FreedomLVF();
			freedom.show();
		} catch ( Throwable e ) {
			Funcoes.criaTelaErro( "Erro de execução\n\n\n" + e.getMessage() );
			e.printStackTrace();
		}
	}
}
