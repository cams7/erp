/**
 * @version 29/12/2004 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.funcoes <BR>
 * Classe:
 * @(#)Logger.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * Esta classe está responsável por guardar as constantes para criação do logs.
 */

package org.freedom.library.functions;

import java.util.Date;

import org.freedom.infra.functions.StringFunctions;

public class Logger {

	// Erros de Comunição:
	public static final int idErroCom = 1;

	public static final LoggerItem LGEC_COM = new LoggerItem(idErroCom, 1, "Erro de comunicação com o Banco de Dados");

	// Erros internos de banco de dados:
	public static final int idErroBD = 2;

	public static final LoggerItem LGEB_BD = new LoggerItem(idErroBD, 1, "Erro interno de Banco de Dados");

	// Informações de PDV:
	public static final int idInfoPDV = 3;

	public static final LoggerItem LGIF_ABERT_CAIXA = new LoggerItem(idInfoPDV, 1, "Abertura de caixa");

	public static final LoggerItem LGIF_FECHA_CAIXA = new LoggerItem(idInfoPDV, 2, "Fechamento de caixa");

	public static final LoggerItem LGIF_ABERT_VENDA = new LoggerItem(idInfoPDV, 3, "Abertura de tela de vendas");

	public static final LoggerItem LGIF_FECHA_VENDA = new LoggerItem(idInfoPDV, 4, "Fechamento de tela de vendas");

	public static final LoggerItem LGIF_LIB_CUPOM = new LoggerItem(idInfoPDV, 5, "Liberação de cupom");

	public static final LoggerItem LGIF_CANC_ITEM = new LoggerItem(idInfoPDV, 6, "Cancelamento de ítem de cupom fical");

	// Erros de PDV:
	public static final int idErroPDV = 4;

	public static final LoggerItem LGEP_ABERTFECHA_CAIXA = new LoggerItem(idErroPDV, 1, "Erro na abertura/fechamento caixa");

	public static final LoggerItem LGEP_STATUS_IMPRES = new LoggerItem(idErroPDV, 2, "Leitura de status da impressora");

	public static final LoggerItem LGEP_ABERT_CUPOM = new LoggerItem(idErroPDV, 3, "Erro na abertura do cupom fiscal");

	public static final LoggerItem LGEP_TROCA_VERAO = new LoggerItem(idErroPDV, 4, "Erro na troca do horário de verão");

	public static final LoggerItem LGEP_ABERT_GAVETA = new LoggerItem(idErroPDV, 5, "Erro na abertura da gaveta");

	public static final LoggerItem LGEP_PROG_MOEDA = new LoggerItem(idErroPDV, 6, "Erro na programação da moeda");

	public static final LoggerItem LGEP_IMPRES_CHEQUE = new LoggerItem(idErroPDV, 7, "Erro na impressão do cheque");

	public static final LoggerItem LGEP_AUT_DOC = new LoggerItem(idErroPDV, 8, "Erro na autenticação do documento");

	public static final LoggerItem LGEP_CANC_ITEM = new LoggerItem(idErroPDV, 9, "Erro no cancelamento do item");

	public static final LoggerItem LGEP_CANC_CUPOM = new LoggerItem(idErroPDV, 10, "Erro no cancelamento do cupom");

	public static final LoggerItem LGEP_INCL_ALIQ = new LoggerItem(idErroPDV, 11, "Erro na inclusão da alíquota");

	public static final LoggerItem LGEP_FECHA_CUPOM = new LoggerItem(idErroPDV, 12, "Erro no fechamento do cupom");

	public static final LoggerItem LGEP_FAZ_SANGRIA = new LoggerItem(idErroPDV, 13, "Erro na sangria");

	public static final LoggerItem LGEP_FAZ_SUPRIMENTO = new LoggerItem(idErroPDV, 14, "Erro no suprimento");

	public static final LoggerItem LGEP_RET_NCUPOM = new LoggerItem(idErroPDV, 15, "Erro no retorno do número do cupom");

	public static final LoggerItem LGEP_IMPRES_ITEM = new LoggerItem(idErroPDV, 16, "Erro na impressão do ítem de venda");

	public static final LoggerItem LGEP_LEITRA_X = new LoggerItem(idErroPDV, 17, "Erro na leitura 'X'");

	public static final LoggerItem LGEP_REDUCAO_Z = new LoggerItem(idErroPDV, 18, "Erro na redução 'Z'");

	public static final LoggerItem LGEP_RET_ALIQ = new LoggerItem(idErroPDV, 19, "Erro na função de retorno das alíquotas");

	public static final LoggerItem LGEP_RET_TOTALIZ = new LoggerItem(idErroPDV, 20, "Erro na função de retorno dos totalizadores");

	public static final LoggerItem LGEP_RET_CANC = new LoggerItem(idErroPDV, 21, "Erro na função de retorno dos cancelamentos");

	public static final LoggerItem LGEP_RET_RED = new LoggerItem(idErroPDV, 22, "Erro na função de retorno das reduções");

	public static final LoggerItem LGEP_DATA_FISCALREDZ = new LoggerItem(idErroPDV, 23, "Erro na função de retorno da data fiscal/redução'Z'");

	public static final LoggerItem LGEP_RET_VCANC = new LoggerItem(idErroPDV, 24, "Erro na função de retorno do valor de cancelamentos");

	public static final LoggerItem LGEP_RET_VDESC = new LoggerItem(idErroPDV, 25, "Erro na função de retorno do valor de descontos");

	public static final LoggerItem LGEP_RET_FLAG = new LoggerItem(idErroPDV, 26, "Erro na função de retorno do flag fiscal");

	public static final LoggerItem LGEP_RET_GTOTAL = new LoggerItem(idErroPDV, 27, "Erro na função de retorno do grande total");

	public static final LoggerItem LGEP_ABRE_N_FISCAL_VIN = new LoggerItem(idErroPDV, 28, "Erro na função abrir comprovante não fiscal vinculado");

	public static final LoggerItem LGEP_USA_N_FISCAL_VIN = new LoggerItem(idErroPDV, 29, "Erro na função usar comprovante não fiscal vinculado");

	public static final LoggerItem LGEP_FECHA_N_FISCAL_VIN = new LoggerItem(idErroPDV, 30, "Erro na função fechar comprovante não fiscal vinculado");

	public static final LoggerItem LGEP_INICIA_TEF = new LoggerItem(idErroPDV, 31, "Erro na função de inicialização da TEF");

	public static final LoggerItem LGEP_FINALIZA_TEF = new LoggerItem(idErroPDV, 32, "Erro na função de finalização de TEF");

	public static final LoggerItem LGEP_REL_GERENCIAL_TEF = new LoggerItem(idErroPDV, 33, "Erro na função usar relatorio gerencial TEF");

	public static final LoggerItem LGEP_FECHA_REL_REGENCIAL = new LoggerItem(idErroPDV, 34, "Erro na função fechar relatorio gerencial");

	public static final LoggerItem LGEP_ALT_SIMB_MOEDA = new LoggerItem(idErroPDV, 35, "Erro na função alterar simbolo da moeda corrente");

	// Logs de PDV:
	public static boolean gravaLogTxt(String sEmp, String sUserID, LoggerItem lg, String sTextoAdic) {

		Date dtLog = new Date();

		StringBuffer buf = new StringBuffer();

		buf.append("LOG: ");
		buf.append(sEmp);
		buf.append("|");
		buf.append(sUserID);
		buf.append("|");
		buf.append(StringFunctions.strZero(String.valueOf(lg.idpai), 2));
		buf.append("|");
		buf.append(StringFunctions.strZero(String.valueOf(lg.id), 2));
		buf.append("|");
		buf.append(lg.desc);
		buf.append("|");
		buf.append(sTextoAdic);
		buf.append("|");
		buf.append(dtLog);

		System.out.println(buf.toString());

		return true;
	}
}

class LoggerItem {

	public int id = 0;

	public int idpai = 0;

	public String desc = "";

	public LoggerItem(int idpai, int id, String desc) {

		this.id = id;
		this.idpai = idpai;
		this.desc = desc;
	}
}
