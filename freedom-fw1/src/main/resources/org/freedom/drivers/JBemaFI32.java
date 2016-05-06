/**
 * @version 12/05/2003 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.drivers <BR>
 * Classe:
 * @(#)JBemaFI32.java <BR>
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
 * Comentários da classe.....
 */

package org.freedom.drivers;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.functions.Logger;

public class JBemaFI32 {

	/**
	 * Constructor for BemaFI32.
	 */

	public static String sMensErroLog = "";

	// Funções de Inicialização

	public native int bAlteraSimboloMoeda(String SimboloMoeda);

	public native int bProgramaAliquota(String Aliquota, int ICMS_ISS);

	public native int bProgramaHorarioVerao();

	public native int bNomeiaDepartamento(int Indice, String Departamento);

	public native int bNomeiaTotalizadorNaoSujeitoIcms(int Indice, String Totalizador);

	public native int bProgramaArredondamento();

	public native int bProgramaTruncamento();

	public native int bLinhasEntreCupons(int Linhas);

	public native int bEspacoEntreLinhas(int Dots);

	public native int bForcaImpactoAgulhas(int ForcaImpacto);

	// Funções do Cupom Fiscal

	public native int bAbreCupom(String CGC_CPF);

	public native int bVendeItem(String Codigo, String Descricao, String Aliquota, String TipoQuantidade, String Quantidade, int CasasDecimais, String ValorUnitario, String TipoDesconto,
			String Desconto);

	public native int bVendeItemDepartamento(String Codigo, String Descricao, String Aliquota, String ValorUnitario, String Quantidade, String Acrescimo, String Desconto, String IndiceDepartamento,
			String UnidadeMedida);

	public native int bCancelaItemAnterior();

	public native int bCancelaItemGenerico(String NumeroItem);

	public native int bCancelaCupom();

	public native int bFechaCupomResumido(String FormaPagamento, String Mensagem);

	public native int bFechaCupom(String FormaPagamento, String AcrescimoDesconto, String TipoAcrescimoDesconto, String ValorAcrescimoDesconto, String ValorPago, String Mensagem);

	public native int bResetaImpressora();

	public native int bIniciaFechamentoCupom(String AcrescimoDesconto, String TipoAcrescimoDesconto, String ValorAcrescimoDesconto);

	public native int bEfetuaFormaPagamento(String FormaPagamento, String ValorFormaPagamento);

	public native int bEfetuaFormaPagamentoDescricaoForma(String FormaPagamento, String ValorFormaPagamento, String DescricaoFormaPagto);

	public native int bTerminaFechamentoCupom(String Mensagem);

	public native int bEstornoFormasPagamento(String FormaOrigem, String FormaDestino, String Valor);

	public native int bUsaUnidadeMedida(String UnidadeMedida);

	public native int bAumentaDescricaoItem(String Descricao);

	// Funções dos Relatórios Fiscais

	public native int bleiturax();

	public native int bReducaoZ(String Data, String Hora);

	public native int bLeituraMemoriaFiscalData(String DataInicial, String DataFinal);

	public native int bLeituraMemoriaFiscalReducao(String ReducaoInicial, String ReducaoFinal);

	public native int bLeituraMemoriaFiscalSerialData(String DataInicial, String DataFinal);

	public native int bLeituraMemoriaFiscalSerialReducao(String ReducaoInicial, String ReducaoFinal);

	// Funções das Operações Não Fiscais

	public native int bRecebimentoNaoFiscal(String IndiceTotalizador, String Valor, String FormaPagamento);

	public native int bAbreComprovanteNaoFiscalVinculado(String FormaPagamento, String Valor, String NumeroCupom);

	public native int bUsaComprovanteNaoFiscalVinculadoTef(String Texto);

	public native int bUsaComprovanteNaoFiscalVinculado(String Texto);

	public native int bFechaComprovanteNaoFiscalVinculado();

	public native int bSangria(String Valor);

	public native int bSuprimento(String Valor, String FormaPagamento);

	public native int bRelatorioGerencial(String Texto);

	public native int bRelatorioGerencialTef(String Texto);

	public native int bFechaRelatorioGerencial();

	// Funções de Informações da Impressora

	public native int bNumeroSerie(String NumeroSerie);

	public native int bSubTotal(String SubTotal);

	public native String bNumeroCupom();

	public native int bLeituraXSerial();

	public native int bVersaoFirmware(String VersaoFirmware);

	public native int bCGC_IE(String CGC, String IE);

	public native String bGrandeTotal();

	public native String bCancelamentos();

	public native String bDescontos();

	public native int bNumeroOperacoesNaoFiscais(String NumeroOperacoes);

	public native String bNumeroCuponsCancelados();

	public native int bNumeroIntervencoes(String NumeroIntervencoes);

	public native String bNumeroReducoes();

	public native int bNumeroSubstituicoesProprietario(String NumeroSubstituicoes);

	public native int bUltimoItemVendido(String NumeroItem);

	public native int bClicheProprietario(String Cliche);

	public native int bNumeroCaixa(String NumeroCaixa);

	public native int bNumeroLoja(String NumeroLoja);

	public native int bSimboloMoeda(String SimboloMoeda);

	public native int bMinutosLigada(String Minutos);

	public native int bMinutosImprimindo(String Minutos);

	public native int bVerificaModoOperacao(String Modo);

	public native int bVerificaEpromConectada(String Flag);

	public native int bFlagsFiscais(int Flag);

	public native int bValorPagoUltimoCupom(String ValorCupom);

	public native int bDataHoraImpressora(String Data, String Hora);

	public native int bContadoresTotalizadoresNaoFiscais(String Contadores);

	public native int bVerificaTotalizadoresNaoFiscais(String Totalizadores);

	public native int bDataHoraReducao(String Data, String Hora);

	public native int bDataMovimento(String Data);

	public native int bVerificaTruncamento(String Flag);

	public native int bAcrescimos(String ValorAcrescimos);

	public native int bContadorBilhetePassagem(String ContadorPassagem);

	public native int bVerificaAliquotasIss(String Flag);

	public native int bVerificaFormasPagamento(String Formas);

	public native int bVerificaRecebimentoNaoFiscal(String Recebimentos);

	public native int bVerificaDepartamentos(String Departamentos);

	public native int bVerificaTipoImpressora(int TipoImpressora);

	public native String bVerificaTotalizadoresParciais();

	public native String bRetornoAliquotas();

	public native String bVerificaEstadoImpressora();

	public native int bDadosUltimaReducao(String DadosReducao);

	public native int bMonitoramentoPapel(int Linhas);

	public native int bVerificaIndiceAliquotasIss(String Flag);

	public native int bValorFormaPagamento(String FormaPagamento, String Valor);

	public native int bValorTotalizadorNaoFiscal(String Totalizador, String Valor);

	// Funções de Autenticação e Gaveta de Dinheiro

	public native int bAutenticacao();

	public native int bProgramaCaracterAutenticacao(String Parametros);

	public native int bAcionaGaveta();

	public native int bVerificaEstadoGaveta(int EstadoGaveta);

	// Funções para a Impressora Restaurante

	public native int bRAbreCupomRestaurante(String Mesa, String CGC_CPF);

	public native int bRRegistraVenda(String Mesa, String Codigo, String Descricao, String Aliquota, String Quantidade, String ValorUnitario, String FlagAcrescimoDesconto,
			String ValorAcrescimoDesconto);

	public native int bRCancelaVenda(String Mesa, String Codigo, String Descricao, String Aliquota, String Quantidade, String ValorUnitario, String FlagAcrescimoDesconto, String ValorAcrescimoDesconto);

	public native int bRConferenciaMesa(String Mesa, String FlagAcrescimoDesconto, String TipoAcrescimoDesconto, String ValorAcrescimoDesconto);

	public native int bRAbreConferenciaMesa(String Mesa);

	public native int bRFechaConferenciaMesa(String FlagAcrescimoDesconto, String TipoAcrescimoDesconto, String ValorAcrescimoDesconto);

	public native int bRTransferenciaMesa(String MesaOrigem, String MesaDestino);

	public native int bRContaDividida(String NumeroCupons, String ValorPago, String CGC_CPF);

	public native int bRFechaCupomContaDividida(String NumeroCupons, String FlagAcrescimoDesconto, String TipoAcrescimoDesconto, String ValorAcrescimoDesconto, String FormasPagamento,
			String ValorFormasPagamento, String ValorPagoCliente, String CGC_CPF);

	public native int bRTransferenciaItem(String MesaOrigem, String Codigo, String Descricao, String Aliquota, String Quantidade, String ValorUnitario, String FlagAcrescimoDesconto,
			String ValorAcrescimoDesconto, String MesaDestino);

	public native int bRRelatorioMesasAbertas(int TipoRelatorio);

	public native int bRImprimeCardapio();

	public native int bRRelatorioMesasAbertasSerial();

	public native int bRCardapioPelaSerial();

	public native int bRRegistroVendaSerial(String Mesa);

	public native int bRVerificaMemoriaLivre(String Bytes);

	public native int bRFechaCupomRestaurante(String FormaPagamento, String FlagAcrescimoDesconto, String TipoAcrescimoDesconto, String ValorAcrescimoDesconto, String ValorFormaPagto, String Mensagem);

	public native int bRFechaCupomResumidoRestaurante(String FormaPagamento, String Mensagem);

	// Função para a Impressora Bilhete de Passagem

	public native int bAbreBilhetePassagem(String ImprimeValorFinal, String ImprimeEnfatizado, String Embarque, String Destino, String Linha, String Prefixo, String Agente, String Agencia,
			String Data, String Hora, String Poltrona, String Plataforma);

	// Funções de Impressão de Cheques

	public native int bProgramaMoedaSingular(String MoedaSingular);

	public native int bProgramaMoedaPlural(String MoedaPlural);

	public native int bCancelaImpressaoCheque();

	public native int bVerificaStatusCheque(int StatusCheque);

	public native int bImprimeCheque(String Banco, String Valor, String Favorecido, String Cidade, String Data, String Mensagem);

	public native int bIncluiCidadeFavorecido(String Cidade, String Favorecido);

	public native int bImprimeCopiaCheque();

	// Funções para o TEF

	public native int bIniciaModoTEF();

	public native int bFinalizaModoTEF();

	/*
	 * public native int bTEFStatus(String Identificacao); public native int
	 * bTEFVendaCartao(String Identificacao, String ValorCompra); public native
	 * int bTEFConfirmaVenda(String Identificacao, String ValorCompra, String
	 * Header); public native int bTEFNaoConfirmaVendaImpressao(String
	 * Identificacao, String ValorCompra); public native int
	 * bTEFCancelaVendaCartao(String Identificacao, String ValorCompra, String
	 * Nsu, String NumeroCupom, String Hora, String Data, String Rede); public
	 * native int bTEFImprimeTEF(String Identificacao, String FormaPagamento,
	 * String ValorCompra); public native int bTEFImprimeRelatorio(); public
	 * native int bTEFADM(String Identificacao); public native int
	 * bTEFVendaCompleta(String Identificacao, String ValorCompra, String
	 * FormaPagamento, String Texto); public native int
	 * bTEFConfiguraDiretorioTef(String PathREQ, String PathRESP); public native
	 * int bTEFVendaCheque(String Identificacao, String ValorCompra); public
	 * native int bTEFApagaResiduos();
	 */
	// Outras Funções
	public native int bAbrePortaSerial();

	public native String bRetornoImpressora();

	public native int bFechaPortaSerial();

	public native int bMapaResumo();

	public native int bAberturaDoDia(String ValorCompra, String FormaPagamento);

	public native int bFechamentoDoDia();

	public native int bImprimeConfiguracoesImpressora();

	public native int bImprimeDepartamentos();

	public native int bRelatorioTipo60Analitico();

	public native int bRelatorioTipo60Mestre();

	public native int bVerificaImpressoraLigada();

	public boolean leituraX(String sUserID, boolean bModoDemo) {

		boolean bRetorno = false;
		if (bModoDemo) {
			bRetorno = true;
		}
		else {
			bRetorno = trataRetornoFuncao(bleiturax());
			if (!bRetorno) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_LEITRA_X, sMensErroLog);
			}
		}
		return bRetorno;
	}

	public boolean abreGaveta(String sUserID, boolean bModoDemo) {

		boolean bRetorno = false;
		if (bModoDemo) {
			bRetorno = true;
		}
		else {
			bRetorno = trataRetornoFuncao(bAcionaGaveta());
			if (!bRetorno) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_ABERT_GAVETA, sMensErroLog);
			}
		}
		return bRetorno;
	}

	public String retornaTotalizadores(String sUserID, boolean bModoDemo) {

		String sRetorno = StringFunctions.replicate(" ", 445);
		// int iRetorno = 0;
		if (!bModoDemo) {
			sRetorno = bVerificaTotalizadoresParciais();
			// iRetorno = Integer.parseInt(sRetorno.substring(0,10).trim());
			sRetorno = sRetorno.substring(10);
			if (sRetorno.length() < 445) {
				sRetorno = StringFunctions.replicate(" ", 445);
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_RET_TOTALIZ, sMensErroLog);
			}
			else {
				sRetorno = sRetorno.substring(0, 445);
			}
		}
		return sRetorno;
	}

	public boolean suprimento(String sUserID, BigDecimal bdValor, String sFormaPagto, boolean bModoDemo) {

		boolean bRetorno = false;
		if (!bModoDemo) {
			if (sFormaPagto.equals(""))
				sFormaPagto = " ";
			bRetorno = trataRetornoFuncao(bSuprimento(Funcoes.transValor(bdValor, 14, 2, true), sFormaPagto));
			if (!bRetorno) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_FAZ_SUPRIMENTO, "ERRO NO SUPRIMENTO-VALOR: " + bdValor + "-FORMA DE PAGTO. " + sFormaPagto + "-" + sMensErroLog);
			}
		}
		else
			bRetorno = true;
		return bRetorno;
	}

	public boolean programaMoeda(String sUserID, String sSing, String sPlur, boolean bModoDemo) {

		boolean bRetorno = false;
		if (!bModoDemo) {
			sSing = StringFunctions.clearAccents(sSing);
			sPlur = StringFunctions.clearAccents(sPlur);

			bRetorno = trataRetornoFuncao(bProgramaMoedaSingular(sSing));
			if (bRetorno) {
				bRetorno = trataRetornoFuncao(bProgramaMoedaPlural(sPlur));
				if (!bRetorno) {
					Logger.gravaLogTxt("", sUserID, Logger.LGEP_PROG_MOEDA, "Moeda Plural - " + sMensErroLog);
				}
			}
			else {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_PROG_MOEDA, "Moeda Singular - " + sMensErroLog);
			}
		}
		else
			bRetorno = true;
		return bRetorno;
	}

	public boolean programaAliquotas(String sUserID, String sVal, int iModoICMISS, boolean bModoDemo) {

		boolean bRetorno = false;
		if (!bModoDemo) {
			bRetorno = trataRetornoFuncao(bProgramaAliquota(sVal, iModoICMISS));
			if (!bRetorno) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_INCL_ALIQ, sMensErroLog);
			}
		}
		else
			bRetorno = true;
		return bRetorno;
	}

	public String retornaAliquotas(String sUserID, boolean bModoDemo) {

		String sRetorno = "";
		if (!bModoDemo) {
			sRetorno = bRetornoAliquotas();
			if (!trataRetornoFuncao(Integer.parseInt(sRetorno.substring(0, 10).trim()))) {
				sRetorno = "";
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_RET_ALIQ, sMensErroLog);
			}
			else {
				sRetorno = sRetorno.substring(10);
			}
		}
		return sRetorno;
	}

	public boolean trataRetornoFuncao(int iRetorno) {

		boolean bRetorno = false;
		String sMensagem;
		sMensagem = "";
		sMensErroLog = "";
		switch (iRetorno) {
		case 0:
			sMensagem = "Erro de comunicação física";
			break;
		case 1:
			sMensagem = "";
			break;
		case -2:
			sMensagem = "Parâmetro inválido na função.";
			break;
		case -3:
			sMensagem = "Aliquota não programada";
			break;
		case -4:
			sMensagem = "O arquivo de inicialização BEMAFI32.INI não foi encontrado no diretório de sistema do Windows";
			break;
		case -5:
			sMensagem = "Erro ao abrir a porta de comunicação";
			break;
		case -8:
			sMensagem = "Erro ao criar ou gravar no arquivo STATUS.TXT ou RETORNO.TXT";
			break;
		case -27:
			sMensagem = "Status da impressora diferente de 6,0,0 (ACK, ST1 e ST2)";
			break;
		case -30:
			sMensagem = "Função não compatível com a impressora YANCO";
			break;
		case -31:
			sMensagem = "Forma de pagamento não finalizada";
			break;
		default:
			sMensagem = "Retorno indefinido: " + iRetorno;
			break;
		}
		if (sMensagem.trim().equals(""))
			bRetorno = true;
		else {
			bRetorno = false;
			sMensErroLog = sMensagem + " - STATUS: " + iRetorno;
			Funcoes.mensagemErro(null, sMensagem);
		}
		return bRetorno;
	}

	public static String transStatus(char cStatus) {

		String sRet = "";
		switch (cStatus) {
		case 'A':
			sRet = "Aberto";
			break;
		case 'U':
			sRet = "Suprimento";
			break;
		case 'S':
			sRet = "Sangria";
			break;
		case 'V':
			sRet = "Venda";
			break;
		case 'Z':
			sRet = "Fechado (com LeituraZ)";
			break;
		default:
			sRet = "Não identificado";
		}
		return sRet;
	}

	public boolean[][] verificaEstadoImpressora(String sUserID, boolean bModoDemo) {

		boolean bRet[][] = null;
		String sRetorno = StringFunctions.replicate(" ", 40);
		int iRetorno = 0;
		if (!bModoDemo) {
			sRetorno = bVerificaEstadoImpressora();
			iRetorno = Integer.parseInt(sRetorno.substring(0, 10).trim());
			sRetorno = sRetorno.substring(10);
			if (!trataRetornoFuncao(iRetorno) || sRetorno.length() < 5) {
				sRetorno = StringFunctions.replicate(" ", 40);
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_STATUS_IMPRES, sMensErroLog);
			}
			else {
				String sStates[] = sRetorno.split("\\|");
				boolean bACK[] = iDecToBin(Integer.parseInt(sStates[0]));
				boolean bST1[] = iDecToBin(Integer.parseInt(sStates[1]));
				boolean bST2[] = iDecToBin(Integer.parseInt(sStates[2]));
				bRet = new boolean[][] { bACK, bST1, bST2 };
			}
		}
		return bRet;
	}

	public boolean[][] retornoImpressora(String sUserID, boolean bModoDemo) {

		boolean bRet[][] = null;
		String sRetorno = StringFunctions.replicate(" ", 40);
		int iRetorno = 0;
		if (!bModoDemo) {
			sRetorno = bRetornoImpressora();
			iRetorno = Integer.parseInt(sRetorno.substring(0, 10).trim());
			sRetorno = sRetorno.substring(10);
			if (!trataRetornoFuncao(iRetorno) || sRetorno.length() < 5) {
				sRetorno = StringFunctions.replicate(" ", 40);
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_STATUS_IMPRES, sMensErroLog);
			}
			else {
				String sStates[] = sRetorno.split("\\|");
				boolean bACK[] = iDecToBin(Integer.parseInt(sStates[0]));
				boolean bST1[] = iDecToBin(Integer.parseInt(sStates[1]));
				boolean bST2[] = iDecToBin(Integer.parseInt(sStates[2]));
				bRet = new boolean[][] { bACK, bST1, bST2 };
			}
		}
		return bRet;
	}

	public static boolean[] iDecToBin(int iNumero) {

		boolean[] cRetorno = { false, false, false, false, false, false, false, false };
		int[] iByte = { 1, 2, 4, 8, 16, 32, 64, 128 };
		int iRest = iNumero;
		int iMark = 0;

		while (iRest > 0) {
			for (int i = 0; i < 8; i++)
				if (iRest >= iByte[i])
					iMark = i;
			cRetorno[iMark] = true;
			iRest = iRest - iByte[iMark];
		}
		return cRetorno;
	}

	public String leStatus(String sUserID, boolean bModoDemo) {

		// int iAck = 0;
		// int iSt1 = 0;
		// int iSt2 = 0;
		boolean bState[][] = null;
		String sMensagem = "";
		String sSep = "";
		try {
			if (!bModoDemo) {
				bState = verificaEstadoImpressora(sUserID, bModoDemo);
				if (bState != null) {
					if (bState[1][7]) {
						sMensagem += "Fim de papel.";
						sSep = "\n";
					}
					if (bState[1][6]) {
						sMensagem += sSep + "Pouco papel.";
						sSep = "\n";
					}
					if (bState[1][5]) {
						sMensagem += sSep + "Erro no relógio.";
						sSep = "\n";
					}
					if (bState[1][4]) {
						sMensagem += sSep + "Impressora em erro.";
						sSep = "\n";
					}
					if (bState[1][3]) {
						sMensagem += sSep + "Primeiro dado de CMD não foi ESC( 1Bh).";
						sSep = "\n";
					}
					if (bState[1][2]) {
						sMensagem += sSep + "Comando inexistente.";
						sSep = "\n";
					}
					if (bState[1][1]) {
						sMensagem += sSep + "Cupom fiscal aberto.";
						sSep = "\n";
					}
					if (bState[1][0]) {
						sMensagem += sSep + "Número de parâmetro de CMD inválido.";
						sSep = "\n";
					}
					if (bState[2][7]) {
						sMensagem += sSep + "Tipo de parâmetro de CMD inválido.";
						sSep = "\n";
					}
					if (bState[2][6]) {
						sMensagem += sSep + "Memória fiscal lotada.";
						sSep = "\n";
					}
					if (bState[2][5]) {
						sMensagem += sSep + "Erro na memória RAM CMOS não volátil.";
						sSep = "\n";
					}
					if (bState[2][4]) {
						sMensagem += sSep + "Alíquota não programada.";
						sSep = "\n";
					}
					if (bState[2][3]) {
						sMensagem += sSep + "Capacidade de alíquotas esgotada.";
						sSep = "\n";
					}
					if (bState[2][2]) {
						sMensagem += sSep + "Cancelamento não permitido.";
						sSep = "\n";
					}
					if (bState[2][1]) {
						sMensagem += sSep + "CNPJ/IE do proprietário não programados.";
						sSep = "\n";
					}
					if (bState[2][0]) {
						sMensagem += sSep + "Comando não executado.";
						sSep = "\n";
					}
					if (!sMensagem.trim().equals("")) {
						Logger.gravaLogTxt("", sUserID, Logger.LGEP_STATUS_IMPRES, sMensagem + "-" + sMensErroLog);
					}
				}
				else {
					Logger.gravaLogTxt("", sUserID, Logger.LGEP_STATUS_IMPRES, sMensErroLog);
				}
			}
		}
		finally {
			bState = null;
		}

		return sMensagem;
	}

	public boolean ImpVerao(String sUserID, boolean bModoDemo) {

		boolean bRetorno = false;
		if (flagFiscal(sUserID, bModoDemo)[2])
			bRetorno = true;
		else
			bRetorno = false;
		return bRetorno;
	}

	public boolean[] flagFiscal(String sUserID, boolean bModoDemo) {

		boolean[] bRetorno = null;
		int iFlagFiscal = 0;
		if (!bModoDemo)
			if (!trataRetornoFuncao(bFlagsFiscais(iFlagFiscal)))
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_RET_FLAG, sMensErroLog);
		bRetorno = iDecToBin(iFlagFiscal);
		return bRetorno;
	}

	public boolean onOffVerao(String sUserID, boolean bModoDemo) {

		boolean bRetorno = false;
		if (!bModoDemo) {
			bRetorno = trataRetornoFuncao(bProgramaHorarioVerao());
			if (!bRetorno)
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_TROCA_VERAO, sMensErroLog);
		}
		else
			bRetorno = true;
		return bRetorno;
	}

	public String mp40fiCoord(int iPosVlrX, int iPosExt1X, int iPosExt2X, int iPosFavX, int iPosLocalX, int iPosDiaX, int iPosMesX, int iPosAnoX, int iPosVlrY, int iPosExt1Y, int iPosExt2Y,
			int iPosFavY, int iPosLocalY) {

		String sRetorno = "";
		if (( iPosVlrX == 0 ) || ( iPosExt1X == 0 ) || ( iPosExt2X == 0 ) || ( iPosFavX == 0 ) || ( iPosLocalX == 0 ) || ( iPosDiaX == 0 ) || ( iPosMesX == 0 ) || ( iPosAnoX == 0 )
				|| ( iPosVlrY == 0 ) || ( iPosExt1Y == 0 ) || ( iPosExt2Y == 0 ) || ( iPosFavY == 0 ) || ( iPosLocalY == 0 )) {
			sRetorno = "";
		}
		else
			sRetorno = StringFunctions.strZero("" + iPosVlrX, 2) + "|" + StringFunctions.strZero("" + iPosExt1X, 2) + "|" + StringFunctions.strZero("" + iPosExt2X, 2) + "|"
					+ StringFunctions.strZero("" + iPosFavX, 2) + "|" + StringFunctions.strZero("" + iPosLocalX, 2) + "|" + StringFunctions.strZero("" + iPosDiaX, 2) + "|"
					+ StringFunctions.strZero("" + iPosMesX, 2) + "|" + StringFunctions.strZero("" + iPosAnoX, 2) + "|" + StringFunctions.strZero("" + iPosVlrY, 2) + "|"
					+ StringFunctions.strZero("" + iPosExt1Y, 2) + "|" + StringFunctions.strZero("" + iPosExt2Y, 2) + "|" + StringFunctions.strZero("" + iPosFavY, 2) + "|"
					+ StringFunctions.strZero("" + iPosLocalY, 2);
		return sRetorno;
	}

	public boolean mp40fiCheque(String sUserID, String sCodbanc, String sNome, String sCidade, Date dtCheq, double deVlrCheque, boolean bModoDemo) {

		int i = 0;
		String sDataCheq = "";
		String sVlrCheq = "";
		boolean bRetorno = true;
		try {
			if (!bModoDemo) {
				sCodbanc = Funcoes.copy(sCodbanc, 1, 3);
				sVlrCheq = Funcoes.transValor(new BigDecimal(deVlrCheque), 14, 2, true);
				sNome = StringFunctions.clearAccents(Funcoes.copy(sNome, 1, 45));
				sCidade = StringFunctions.clearAccents(Funcoes.copy(sCidade, 1, 27)).trim();
				sDataCheq = Funcoes.dataDDMMAAAA(dtCheq);
				i = 1;

			}
			while (!trataRetornoFuncao(bImprimeCheque(sCodbanc, sVlrCheq, sNome, sCidade, sDataCheq, ""))) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_IMPRES_CHEQUE, "Banco: " + sCodbanc + "-Valor: " + sVlrCheq + "-Fav.: " + sNome + "-Cid: " + sCidade + "-Data: " + sDataCheq + "-"
						+ sMensErroLog);
				if (i >= 5) {
					bRetorno = false;
					break;
				}
				i++;
			}

		}
		finally {
			i = 0;
			sDataCheq = null;
			sVlrCheq = null;
		}
		return bRetorno;

	}

	public boolean verificaCupomAberto(String sUserID, boolean bModoDemo) {

		boolean bState[][] = null;
		boolean bRet = false;
		try {
			if (!bModoDemo) {
				bState = verificaEstadoImpressora(sUserID, bModoDemo);
				if (bState != null) {
					bRet = bState[1][1];
				}
				else {
					Logger.gravaLogTxt("", sUserID, Logger.LGEP_STATUS_IMPRES, sMensErroLog);
				}
			}
		}
		finally {
			bState = null;
		}
		return bRet;
	}

	public boolean autenticaDoc(String sUserID, boolean bModoDemo) {

		boolean bState[][] = null;
		long lSegIni = 0;
		long lSegFim = 0;
		long lSegDec = 0;
		boolean bRetorno = true;
		try {
			if (!bModoDemo) {
				lSegIni = Funcoes.getSeconds();
				bRetorno = trataRetornoFuncao(bAutenticacao());
				lSegFim = Funcoes.getSeconds();
				if (bRetorno) {
					lSegDec = lSegFim - lSegIni;
					if (lSegDec > 13) {
						Logger.gravaLogTxt("", sUserID, Logger.LGEP_AUT_DOC, "ERRO NA AUTENTICAÇÃO DE DOCUMENTO, TEMPO DE ESPERA DE " + lSegDec + " SEGUNDOS");
						bRetorno = false;
					}
					else {
						bState = retornoImpressora(sUserID, bModoDemo);
						if (bState != null) {
							if (bState[2][0]) {
								Logger.gravaLogTxt("", sUserID, Logger.LGEP_AUT_DOC, "COMANDO NÃO EXECUTADO");
								bRetorno = false;
							}
						}
						else {
							Logger.gravaLogTxt("", sUserID, Logger.LGEP_AUT_DOC, sMensErroLog);
							bRetorno = false;
						}
					}
				}
			}

		}
		finally {
			bState = null;
			lSegIni = 0;
			lSegFim = 0;
			lSegDec = 0;
		}
		return bRetorno;
	}

	public boolean cancelaItemAnterior(String sUserID, boolean bModoDemo) {

		boolean bRetorno = true;
		if (!bModoDemo) {
			Logger.gravaLogTxt("", sUserID, Logger.LGEP_CANC_ITEM, "TENTATIVA DE CANCELAMENTO DE ITEM ANTERIOR PELO OPERADOR " + sUserID);
			bRetorno = trataRetornoFuncao(bCancelaItemAnterior());
			if (!sMensErroLog.trim().equals("")) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_CANC_ITEM, sMensErroLog);
			}
		}
		return bRetorno;
	}

	public boolean cancelaItemGenerico(String sUserID, int iItem, boolean bModoDemo) {

		boolean bRetorno = true;
		if (!bModoDemo) {
			String sItem = StringFunctions.strZero("" + iItem, 3);
			Logger.gravaLogTxt("", sUserID, Logger.LGEP_CANC_ITEM, "CANCELAMENTO DE ITEM [" + sItem + "] PELO " + sUserID);
			bRetorno = trataRetornoFuncao(bCancelaItemGenerico(sItem));
			if (!sMensErroLog.trim().equals("")) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_CANC_ITEM, sMensErroLog);
			}
		}
		return bRetorno;
	}

	public boolean cancelaCupom(String sUserID, boolean bModoDemo) {

		boolean bRetorno = true;
		if (!bModoDemo) {
			bRetorno = trataRetornoFuncao(bCancelaCupom());
			if (!bRetorno)
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_CANC_CUPOM, sMensErroLog);
		}
		return bRetorno;
	}

	public boolean insereAliquota(String sUserID, double beAliquota, boolean bModoDemo) {

		String sAliquota = "";
		boolean bRetorno = true;
		try {
			if (!bModoDemo) {
				sAliquota = Funcoes.transValor(new BigDecimal(beAliquota), 4, 2, true);
				bRetorno = trataRetornoFuncao(bProgramaAliquota(sAliquota, 0));
				if (!bRetorno)
					Logger.gravaLogTxt("", sUserID, Logger.LGEP_INCL_ALIQ, sMensErroLog);
			}
		}
		finally {
			sAliquota = null;
		}
		return bRetorno;
	}

	public boolean terminaFechaCupom(String sUserID, String sMensagem, boolean bModoDemo) {

		boolean bRetorno = false;
		int i = 0;
		try {
			if (bModoDemo) {
				bRetorno = true;
			}
			else {
				if (sMensagem.trim().equals(""))
					sMensagem = ".";
				bRetorno = true;
				i = 1;
				while (!trataRetornoFuncao(bTerminaFechamentoCupom(sMensagem))) {
					Logger.gravaLogTxt("", sUserID, Logger.LGEP_FECHA_CUPOM, sMensErroLog + "-MENSAGEM DE FINAL DE CUPOM: " + sMensagem);
					Funcoes.espera(2);
					if (i >= 5) {
						bRetorno = false;
						break;
					}
					i++;
				}
			}
		}
		finally {
			i = 0;
		}
		return bRetorno;
	}

	public boolean formaPagtoCupom(String sUserID, String sFormapagto, double deValor, boolean bModoDemo) {

		boolean bRetorno = true;
		int i = 0;
		try {
			if (!bModoDemo) {
				sFormapagto = Funcoes.adicionaEspacos(sFormapagto, 16);
				i = 1;
				while (!trataRetornoFuncao(bEfetuaFormaPagamentoDescricaoForma(sFormapagto, Funcoes.transValor(deValor + "", 14, 2, true), ""))) {
					Logger.gravaLogTxt("", sUserID, Logger.LGEP_FECHA_CUPOM, "ERRO NA INCLUSÃO DE FORMA DE PAGAMENTO NO FECHAMENTO DO CUPOM. " + sMensErroLog + "-FORMA DE PAGAMENTO: " + sFormapagto);
					Funcoes.espera(2);
					if (i >= 5) {
						bRetorno = false;
						break;
					}
					i++;
				}
			}
		}
		finally {
			i = 0;
		}
		return bRetorno;
	}

	/*
	 * public native int bRecebimentoNaoFiscal(String IndiceTotalizador, String
	 * Valor, String FormaPagamento); public native int
	 * bAbreComprovanteNaoFiscalVinculado(String FormaPagamento, String Valor,
	 * String NumeroCupom); public native int
	 * bUsaComprovanteNaoFiscalVinculado(String Texto); public native int
	 * bFechaComprovanteNaoFiscalVinculado();
	 */

	public boolean abreComprovanteNaoFiscalVinculado(String sUserID, String sFormaPagto, boolean bModoDemo) {

		return abreComprovanteNaoFiscalVinculado(sUserID, sFormaPagto, null, 0, bModoDemo);
	}

	public boolean abreComprovanteNaoFiscalVinculado(String sUserID, String sFormaPagto, BigDecimal bdValor, int iNumCupom, boolean bModoDemo) {

		boolean bRetorno = false;
		String sValor;
		String sNumCupom;
		if (!bModoDemo) {
			if (sFormaPagto.trim().equals("")) {
				sFormaPagto = " ";
			}
			else {
				sFormaPagto = StringFunctions.clearAccents(Funcoes.adicionaEspacos(sFormaPagto, 16));
			}

			sValor = bdValor != null ? Funcoes.transValor(bdValor, 14, 2, true) : "";
			sNumCupom = iNumCupom > 0 ? iNumCupom + "" : "";

			bRetorno = trataRetornoFuncao(bAbreComprovanteNaoFiscalVinculado(sFormaPagto, sValor, sNumCupom));
			if (!bRetorno) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_ABRE_N_FISCAL_VIN, "ERRO NO COMPROVANTE NÃO FISCAL VINCULADO: " + sFormaPagto + "|" + bdValor + "|" + iNumCupom + "|" + sMensErroLog);
			}
		}
		else {
			bRetorno = true;
		}
		return bRetorno;
	}

	public boolean usaComprovanteNaoFiscalVinculadoTef(String sUserID, String sTexto, boolean bModoDemo) {

		boolean bRetorno = false;
		if (!bModoDemo) {
			if (sTexto.trim().equals(""))
				sTexto = " ";
			bRetorno = trataRetornoFuncao(bUsaComprovanteNaoFiscalVinculadoTef(sTexto));
			if (!bRetorno) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_USA_N_FISCAL_VIN, "ERRO NO COMPROVANTE NÃO FISCAL VINCULADO TEF: " + sTexto + "|" + sMensErroLog);
			}
		}
		else
			bRetorno = true;
		return bRetorno;
	}

	public boolean usaComprovanteNaoFiscalVinculado(String sUserID, String sTexto, boolean bModoDemo) {

		boolean bRetorno = false;
		if (!bModoDemo) {
			if (sTexto.trim().equals(""))
				sTexto = " ";
			bRetorno = trataRetornoFuncao(bUsaComprovanteNaoFiscalVinculado(sTexto));
			if (!bRetorno) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_USA_N_FISCAL_VIN, "ERRO NO COMPROVANTE NÃO FISCAL VINCULADO: " + sTexto + "|" + sMensErroLog);
			}
		}
		else
			bRetorno = true;
		return bRetorno;
	}

	public boolean relatorioGerencialTef(String sUserID, String sTexto, boolean bModoDemo) {

		boolean bRetorno = false;
		if (!bModoDemo) {
			if (sTexto.trim().equals(""))
				sTexto = " ";
			bRetorno = trataRetornoFuncao(bRelatorioGerencialTef(sTexto));
			if (!bRetorno) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_REL_GERENCIAL_TEF, "ERRO NO COMPROVANTE: " + sTexto + "|" + sMensErroLog);
			}
		}
		else
			bRetorno = true;
		return bRetorno;
	}

	public boolean fechaComprovanteNaoFiscalVinculado(String sUserID, boolean bModoDemo) {

		boolean bRetorno = false;
		if (!bModoDemo) {
			bRetorno = trataRetornoFuncao(bFechaComprovanteNaoFiscalVinculado());
			if (!bRetorno) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_FECHA_N_FISCAL_VIN, "ERRO NO COMPROVANTE NÃO FISCAL VINCULADO: " + sMensErroLog);
			}
		}
		else
			bRetorno = true;
		return bRetorno;
	}

	public boolean fechaRelatorioGerencial(String sUserID, boolean bModoDemo) {

		boolean bRetorno = false;
		if (!bModoDemo) {
			bRetorno = trataRetornoFuncao(bFechaRelatorioGerencial());
			if (!bRetorno) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_FECHA_REL_REGENCIAL, "ERRO NO COMPROVANTE: " + sMensErroLog);
			}
		}
		else
			bRetorno = true;
		return bRetorno;
	}

	public boolean iniciaModoTEF(String sUserID, boolean bModoDemo) {

		boolean bRetorno = false;
		if (!bModoDemo) {
			bRetorno = trataRetornoFuncao(bIniciaModoTEF());
			if (!bRetorno) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_INICIA_TEF, "ERRO NO INICIA MODO TEF: " + sMensErroLog);
			}
		}
		else
			bRetorno = true;
		return bRetorno;
	}

	public boolean finalizaModoTEF(String sUserID, boolean bModoDemo) {

		boolean bRetorno = false;
		if (!bModoDemo) {
			bRetorno = trataRetornoFuncao(bFinalizaModoTEF());
			if (!bRetorno) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_FINALIZA_TEF, "ERRO NO FINALIZA MODO TEF: " + sMensErroLog);
			}
		}
		else
			bRetorno = true;
		return bRetorno;
	}

	public boolean sangria(String sUserID, BigDecimal bdValor, boolean bModoDemo) {

		boolean bRetorno = false;
		if (!bModoDemo) {
			bRetorno = trataRetornoFuncao(bSangria(Funcoes.transValor(bdValor, 14, 2, true)));
			if (!bRetorno) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_FAZ_SANGRIA, "ERRO NA SANGRIA-VALOR: " + bdValor + "-" + sMensErroLog);
			}
		}
		else
			bRetorno = true;
		return bRetorno;
	}

	public boolean sangriax(String sUserID, double deValor, boolean bModoDemo) {

		boolean bRetorno = true;
		if (!bModoDemo) {
			bRetorno = trataRetornoFuncao(bSangria(Funcoes.transValor(deValor + "", 14, 2, true)));
			if (!bRetorno) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_FAZ_SANGRIA, "ERRO NA SANGRIA-VALOR: " + deValor + "|" + sMensErroLog);
			}
		}
		return bRetorno;
	}

	public boolean suprimento(String sUserID, double deValor, String sFormaPagto, boolean bModoDemo) {

		boolean bRetorno = true;
		if (!bModoDemo) {
			if (sFormaPagto.trim().equals(""))
				sFormaPagto = " ";
			bRetorno = trataRetornoFuncao(bSuprimento(Funcoes.transValor(deValor + "", 14, 2, true), sFormaPagto));
			if (!bRetorno) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_FAZ_SUPRIMENTO, "ERRO NO SUPRIMENTO-VALOR: " + deValor + "|FORMA DE PAGTO. " + sFormaPagto + "|" + sMensErroLog);
			}
		}
		return bRetorno;
	}

	public boolean abreCupom(String sCnpj, String sUserID, boolean bModoDemo) {

		boolean bRetorno = true;
		int i = 0;
		try {
			if (!bModoDemo) {
				i = 1;
				while (!trataRetornoFuncao(bAbreCupom(sCnpj))) {
					Logger.gravaLogTxt("", sUserID, Logger.LGEP_ABERT_CUPOM, sMensErroLog);
					Funcoes.espera(2);
					if (i >= 5) {
						bRetorno = false;
						break;
					}
					i++;
				}
			}
		}
		finally {
			i = 0;
		}
		return bRetorno;
	}

	public int numeroCupom(String sUserID, boolean bModoDemo) {

		String sNumero = "";
		int iRetorno = 0;
		try {
			if (!bModoDemo) {
				sNumero = bNumeroCupom();
				if (sNumero == null) {
					sNumero = "";
				}
				else {
					iRetorno = Integer.parseInt(sNumero.substring(0, 10).trim());
					sNumero = sNumero.substring(0, 10).trim();
				}
				if (!trataRetornoFuncao(iRetorno)) {
					if (sNumero.equals("")) {
						Funcoes.mensagemErro(null, "Número do cupom nulo!\n" + "Entre em contato com departamento técnico!!!");
						iRetorno = 999999;
					}
					else {
						sNumero = sNumero.trim();
						if (sNumero.equals(""))
							iRetorno = 0;
					}
				}
				else {
					if (Funcoes.ehInteiro(sNumero)) {
						iRetorno = Integer.parseInt(sNumero);
					}
					else {
						Funcoes.mensagemErro(null, "Número do cupom não é inteiro!\n" + "Entre em contato com departamento técnico!!!");
						iRetorno = 888888;
					}
				}
			}
			else {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_RET_NCUPOM, sMensErroLog);
			}
		}
		finally {
			sNumero = null;
		}
		return iRetorno;
	}

	public boolean vendaItem(String sUserID, int iCodprod, String sDescprod, String sTributo, double deQuant, double deValor, double deValDesc, boolean bModoDemo) {

		String sQuant = "";
		String sValor = "";
		String sValdesc = "";
		String sCodprod = "";
		int i = 0;
		boolean bRetorno = true;
		try {
			if (!bModoDemo) {
				sCodprod = "" + iCodprod;
				sDescprod = StringFunctions.clearAccents(Funcoes.adicionaEspacos(sDescprod, 29));
				sQuant = Funcoes.transValor(deQuant + "", 7, 3, true);
				sValor = Funcoes.transValor(deValor + "", 8, 2, true);
				sValdesc = Funcoes.transValor(deValDesc + "", 8, 2, true);
				i = 1;

				while (!trataRetornoFuncao(bVendeItem(sCodprod, sDescprod, sTributo, "F", sQuant, 2, sValor, "$", sValdesc))) {
					Logger.gravaLogTxt("", sUserID, Logger.LGEP_IMPRES_ITEM, "Codigo: " + sCodprod + "-Descrição: " + sDescprod + "-Qtd.: " + sQuant + "-Valor: " + sValor + "-Desconto: " + sValdesc
							+ "-Erro: " + sMensErroLog);
					Funcoes.espera(2);
					if (i++ >= 5) {
						bRetorno = false;
						break;
					}
				}
			}
		}
		finally {
			sQuant = null;
			sValor = null;
			sValdesc = null;
			sCodprod = null;
			i = 0;
		}
		return bRetorno;
	}

	public boolean fechaCupomFiscal(String sUserID, String sFormaPagto, String sAcreDesc, String sTipoAcreDesc, double deVlrAcreDesc, double deVlrPago, String sMensagem, boolean bModoDemo) {

		int i = 0;
		String sVlrAcreDesc = "";
		String sVlrPago = "";
		boolean bRetorno = true;

		try {
			if (!bModoDemo) {
				if (sMensagem.trim().equals("")) {
					sMensagem = ".";
				}

				sFormaPagto = Funcoes.adicionaEspacos(StringFunctions.clearAccents(sFormaPagto), 16);

				if (sAcreDesc.trim().equals("")) {
					sAcreDesc = "D";
				}
				if (sTipoAcreDesc.trim().equals("")) {
					sTipoAcreDesc = "$";
				}
				if (sTipoAcreDesc.trim().equals("$")) {
					sVlrAcreDesc = Funcoes.transValor("" + deVlrAcreDesc, 14, 2, true);
				}
				else {
					sVlrAcreDesc = Funcoes.transValor("" + deVlrAcreDesc, 4, 2, true);
				}

				sVlrPago = Funcoes.transValor("" + deVlrPago, 14, 2, true);

				i = 1;
				while (!trataRetornoFuncao(bFechaCupom(sFormaPagto, sAcreDesc, sTipoAcreDesc, sVlrAcreDesc, sVlrPago, sMensagem))) {
					Logger.gravaLogTxt("", sUserID, Logger.LGEP_FECHA_CUPOM, "Forma Pagto.: " + sFormaPagto + " - Valor Pago:" + sVlrPago + " - Msg. final de cupom: " + sMensagem + " - Erro: "
							+ sMensErroLog);
					Funcoes.espera(2);
					if (i >= 5) {
						bRetorno = false;
						break;
					}
					i++;
				}
			}
		}
		finally {
			i = 0;
			sVlrAcreDesc = "";
			sVlrPago = "";
		}
		return bRetorno;

	}

	public boolean inicioFechaCupom(String sUserID, boolean bModoDemo) {

		int i = 0;
		boolean bRetorno = true;
		try {
			if (!bModoDemo) {
				i = 1;
				while (!trataRetornoFuncao(bIniciaFechamentoCupom("D", "%", "0000"))) {
					Logger.gravaLogTxt("", sUserID, Logger.LGEP_FECHA_CUPOM, "ERRO NA FUNÇÃO INÍCIO DE FECHAMENTO DE CUPOM" + "-" + sMensErroLog);
					Funcoes.espera(2);
					if (i >= 5) {
						bRetorno = false;
						break;
					}
					i++;
				}
			}
		}
		finally {
			i = 0;
		}
		return bRetorno;
	}

	public boolean reducaoZ(String sUserID, boolean bModoDemo) {

		boolean bRetorno = true;
		if (!bModoDemo) {
			Date agora = new Date();
			bRetorno = trataRetornoFuncao(bReducaoZ(( new SimpleDateFormat("dd/MM/yyyy") ).format(agora), ( new SimpleDateFormat("HH:mm:ss") ).format(agora)));
			if (!bRetorno) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_REDUCAO_Z, sMensErroLog);
			}
		}
		return bRetorno;
	}

	public int numeroCancelados(String sUserID, boolean bModoDemo) {

		String sRetorno = "";
		int iRetorno = 0;
		int iErro = 0;
		try {
			if (!bModoDemo) {
				sRetorno = bNumeroCuponsCancelados();
				iErro = Integer.parseInt(sRetorno.substring(0, 10).trim());
				sRetorno = sRetorno.substring(10);
				if (!trataRetornoFuncao(iErro)) {
					sRetorno = "";
					Logger.gravaLogTxt("", sUserID, Logger.LGEP_RET_CANC, sMensErroLog);
				}
				if (!sRetorno.trim().equals(""))
					iRetorno = Integer.parseInt(sRetorno.trim());
			}
		}
		finally {
			sRetorno = null;
			iErro = 0;
		}
		return iRetorno;
	}

	public int numeroReducoes(String sUserID, boolean bModoDemo) {

		String sRetorno = "";
		int iRetorno = 0;
		int iErro = 0;
		try {
			if (!bModoDemo) {
				sRetorno = bNumeroReducoes();
				iErro = Integer.parseInt(sRetorno.substring(0, 10).trim());
				sRetorno = sRetorno.substring(10);
				if (trataRetornoFuncao(iErro)) {
					if (!sRetorno.trim().equals(""))
						iRetorno = Integer.parseInt(sRetorno.trim());
				}
				else {
					Logger.gravaLogTxt("", sUserID, Logger.LGEP_RET_RED, sMensErroLog);
				}
			}
		}
		finally {
			sRetorno = null;
			iErro = 0;
		}
		return iRetorno;

	}

	public double cancelamentos(String sUserID, boolean bModoDemo) {

		String sRetorno = "";
		double deRetorno = 0;
		int iRetorno = 0;
		try {
			if (!bModoDemo) {
				sRetorno = bCancelamentos();
				iRetorno = Integer.parseInt(sRetorno.substring(0, 10).trim());
				sRetorno = sRetorno.substring(10);
				if (trataRetornoFuncao(iRetorno)) {
					if (!sRetorno.trim().equals("")) {
						deRetorno = ( new Double(sRetorno.trim()) ).doubleValue() / 100;
					}
				}
				else {
					Logger.gravaLogTxt("", sUserID, Logger.LGEP_RET_VCANC, sMensErroLog);
				}
			}
		}
		finally {
			sRetorno = null;
		}
		return deRetorno;
	}

	public double descontos(String sUserID, boolean bModoDemo) {

		String sRetorno = "";
		double deRetorno = 0;
		int iRetorno = 0;
		try {
			if (!bModoDemo) {
				sRetorno = bDescontos();
				iRetorno = Integer.parseInt(sRetorno.substring(0, 10).trim());
				sRetorno = sRetorno.substring(10);
				if (trataRetornoFuncao(iRetorno)) {
					if (!sRetorno.trim().equals("")) {
						deRetorno = ( new Double(sRetorno.trim()) ).doubleValue() / 100;
					}
				}
				else {
					Logger.gravaLogTxt("", sUserID, Logger.LGEP_RET_VDESC, sMensErroLog);
				}
			}
		}
		finally {
			sRetorno = null;
		}
		return deRetorno;
	}

	public double grandeTotal(String sUserID, boolean bModoDemo) {

		String sRetorno = "";
		double deRetorno = 0;
		int iRetorno = 0;
		try {
			if (!bModoDemo) {
				sRetorno = bGrandeTotal();
				iRetorno = Integer.parseInt(sRetorno.substring(0, 10).trim());
				sRetorno = sRetorno.substring(10);
				if (trataRetornoFuncao(iRetorno)) {
					if (!sRetorno.trim().equals("")) {
						deRetorno = ( new Double(sRetorno.trim()) ).doubleValue() / 100;
					}
				}
				else {
					Logger.gravaLogTxt("", sUserID, Logger.LGEP_RET_GTOTAL, sMensErroLog);
				}
			}
		}
		finally {
			sRetorno = null;
		}
		return deRetorno;
	}

	public Date dataImpressora(String sUserID, String sTipo, boolean bModoDemo) {

		String sData = "";
		String sHora = "";
		Date dtRetorno = new Date();
		Date dtHora = null;
		Date dtData = null;
		int iDia = 0;
		int iMes = 0;
		int iAno = 0;
		int iHora = 0;
		int iMinutos = 0;
		int iSegundos = 0;
		int iMilesegundos = 0;
		// int iDiaAtu = 0;
		// int iMesAtu = 0;
		int iAnoAtu = 0;
		int[] iDataTmp = { 0, 0, 0 };

		try {
			if (!bModoDemo) {
				sData = StringFunctions.replicate(" ", 6);
				sHora = StringFunctions.replicate(" ", 6);
				if (trataRetornoFuncao(bDataHoraImpressora(sData, sHora))) {
					iDataTmp = Funcoes.decodeDate(new Date());
					iAnoAtu = iDataTmp[0];
					// iMesAtu = iDataTmp[1];
					// iDiaAtu = iDataTmp[2];
					iDia = Integer.parseInt(sData.substring(0, 2));
					iMes = Integer.parseInt(sData.substring(2, 4));
					iAno = Integer.parseInt(sData.substring(4, 6));
					if (StringFunctions.strZero("" + iAnoAtu, 4).substring(2, 4).equals(StringFunctions.strZero("" + iAno, 2)))
						iAno = iAnoAtu;
					iHora = Integer.parseInt(sHora.substring(0, 2));
					iMinutos = Integer.parseInt(sHora.substring(2, 4));
					iSegundos = Integer.parseInt(sHora.substring(4, 6));
					iMilesegundos = 0;
					dtData = Funcoes.encodeDate(iAno, iMes, iDia);
					dtHora = Funcoes.encodeTime(dtData, iHora, iMinutos, iSegundos, iMilesegundos);
					if (sTipo.toUpperCase().equals("D"))
						dtRetorno = dtData;
					else if (sTipo.toUpperCase().equals("H"))
						dtRetorno = dtHora;
					else if (sTipo.toUpperCase().equals("DH"))
						;
					dtRetorno = dtHora;
				}
				else {
					Logger.gravaLogTxt("", sUserID, Logger.LGEP_DATA_FISCALREDZ, "DATA FISCAL");
				}
			}
		}
		finally {
			sData = "";
			sHora = "";
			dtHora = null;
			dtData = null;
			iDia = 0;
			iMes = 0;
			iAno = 0;
			iHora = 0;
			iMinutos = 0;
			iSegundos = 0;
			iMilesegundos = 0;
			// iDiaAtu = 0;
			// iMesAtu = 0;
			iAnoAtu = 0;
			iDataTmp = null;
		}
		return dtRetorno;
	}

	public Date dataUltimaReducaoZ(String sUserID, String sTipo, boolean bModoDemo) {

		String sData = "";
		String sHora = "";
		Date dtRetorno = new Date();
		Date dtHora = null;
		Date dtData = null;
		int iDia = 0;
		int iMes = 0;
		int iAno = 0;
		int iHora = 0;
		int iMinutos = 0;
		int iSegundos = 0;
		int iMilesegundos = 0;
		// int iDiaAtu = 0;
		// int iMesAtu = 0;
		int iAnoAtu = 0;
		int[] iDataTmp = { 0, 0, 0 };
		try {
			if (!bModoDemo) {
				sData = StringFunctions.replicate(" ", 6);
				sHora = StringFunctions.replicate(" ", 6);
				if (trataRetornoFuncao(bDataHoraReducao(sData, sHora))) {
					iDataTmp = Funcoes.decodeDate(new Date());
					iAnoAtu = iDataTmp[0];
					// iMesAtu = iDataTmp[1];
					// iDiaAtu = iDataTmp[2];
					iDia = Integer.parseInt(sData.substring(0, 2));
					iMes = Integer.parseInt(sData.substring(2, 4));
					iAno = Integer.parseInt(sData.substring(4, 6));
					if (StringFunctions.strZero("" + iAnoAtu, 4).substring(2, 4).equals(StringFunctions.strZero("" + iAno, 2)))
						iAno = iAnoAtu;
					iHora = Integer.parseInt(sHora.substring(0, 2));
					iMinutos = Integer.parseInt(sHora.substring(2, 4));
					iSegundos = Integer.parseInt(sHora.substring(4, 6));
					iMilesegundos = 0;
					dtData = Funcoes.encodeDate(iAno, iMes, iDia);
					dtHora = Funcoes.encodeTime(dtData, iHora, iMinutos, iSegundos, iMilesegundos);
					if (sTipo.toUpperCase().equals("D"))
						dtRetorno = dtData;
					else if (sTipo.toUpperCase().equals("H"))
						dtRetorno = dtHora;
					else if (sTipo.toUpperCase().equals("DH"))
						;
					dtRetorno = dtHora;
				}
				else {
					Logger.gravaLogTxt("", sUserID, Logger.LGEP_DATA_FISCALREDZ, "DATA DA ÚLTIMA REDUÇÃO Z");
				}
			}
		}
		finally {
			sData = "";
			sHora = "";
			dtHora = null;
			dtData = null;
			iDia = 0;
			iMes = 0;
			iAno = 0;
			iHora = 0;
			iMinutos = 0;
			iSegundos = 0;
			iMilesegundos = 0;
			// iDiaAtu = 0;
			// iMesAtu = 0;
			iAnoAtu = 0;
			iDataTmp = null;
		}
		return dtRetorno;
	}

	static {
		System.loadLibrary("JBemaFI32");
	}
}
