package org.freedom.drivers;

import java.math.BigDecimal;

import org.freedom.ecf.driver.AbstractECFDriver;
import org.freedom.ecf.driver.ECFBematech;
import org.freedom.ecf.driver.STResult;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.functions.Logger;
import org.freedom.library.swing.frame.AplicativoPDV;

public class ECFDriver {

	private JBemaFI32 bema = null;

	private boolean bModoDemo = AplicativoPDV.bModoDemo;

	private boolean dll;

	private AbstractECFDriver ecf = null;

	private String sUserID = AplicativoPDV.getUsuario().getIdusu();

	public static String sMensErroLog = "";

	public ECFDriver(final boolean arg0) {

		this.dll = arg0;

		if (this.dll) {
			bema = new JBemaFI32();
		}
		else {
			ecf = new ECFBematech(AplicativoPDV.getPortaECF());
		}

	}

	public int numeroReducoes() {

		int result = -1;

		if (dll) {
			result = bema.numeroReducoes(sUserID, bModoDemo);
		}
		else if (!bModoDemo && ecf != null) {

			result = Integer.parseInt(ecf.resultVariaveis(AbstractECFDriver.V_REDUCOES));

		}

		return result;

	}

	public int numeroCancelados() {

		int result = -1;

		if (dll) {
			result = bema.numeroCancelados(sUserID, bModoDemo);
		}
		else if (!bModoDemo && ecf != null) {

			result = Integer.parseInt(ecf.resultVariaveis(AbstractECFDriver.V_CUPONS_CANC));

		}

		return result;

	}

	public double cancelamentos() {

		double result = -1;

		if (dll) {
			result = bema.cancelamentos(sUserID, bModoDemo);
		}
		else if (!bModoDemo && ecf != null) {

			result = Double.parseDouble(ecf.resultVariaveis(AbstractECFDriver.V_CANCELAMENTOS));

		}

		return result;

	}

	public double descontos() {

		double result = -1;

		if (dll) {
			result = bema.descontos(sUserID, bModoDemo);
		}
		else if (!bModoDemo && ecf != null) {

			result = Double.parseDouble(ecf.resultVariaveis(AbstractECFDriver.V_CANCELAMENTOS));

		}

		return result;

	}

	public String resultTotalizadores() {

		String result = "";

		if (dll) {
			result = bema.retornaTotalizadores(sUserID, bModoDemo);
		}
		else if (!bModoDemo && ecf != null) {

			result = ecf.resultTotalizadoresParciais();

		}

		return result;

	}

	public boolean abreComprovanteNaoFiscalVinculado(final String sFormaPagto, final BigDecimal bdValor, final int iNumCupom) {

		boolean bresult = false;

		if (dll) {
			bresult = bema.abreComprovanteNaoFiscalVinculado(sUserID, sFormaPagto, bdValor, iNumCupom, bModoDemo);
		}
		else if (!bModoDemo && ecf != null) {

			bresult = trataresultFuncao(ecf.abreComprovanteNFiscalVinculado(sFormaPagto, bdValor.floatValue(), iNumCupom));

			if (!bresult) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_ABRE_N_FISCAL_VIN, "ERRO NO COMPROVANTE NÃO FISCAL VINCULADO: " + sFormaPagto + "|" + bdValor + "|" + iNumCupom + "|" + sMensErroLog);
			}

		}

		return bresult;

	}

	public boolean abreCupom() {

		return abreCupom("");

	}

	public boolean abreCupom(final String sCnpj) {

		boolean bresult = false;

		if (dll) {
			bresult = bema.abreCupom(sCnpj, sUserID, bModoDemo);
		}
		else if (!bModoDemo && ecf != null) {

			if ("".equals(sCnpj) || sCnpj == null) {
				int i = 1;
				while (!trataresultFuncao(ecf.aberturaDeCupom())) {
					Logger.gravaLogTxt("", sUserID, Logger.LGEP_ABERT_CUPOM, sMensErroLog);
					Funcoes.espera(2);
					if (i >= 5) {
						bresult = false;
						break;
					}
					i++;
				}
			}
			else {
				int i = 1;
				while (!trataresultFuncao(ecf.aberturaDeCupom(sCnpj))) {
					Logger.gravaLogTxt("", sUserID, Logger.LGEP_ABERT_CUPOM, sMensErroLog);
					Funcoes.espera(2);
					if (i >= 5) {
						bresult = false;
						break;
					}
					i++;
				}
			}

		}

		return bresult;

	}

	public boolean abreGaveta() {

		boolean bresult = false;

		if (dll) {
			bresult = bema.abreGaveta(sUserID, bModoDemo);
		}
		else {
			if (!bModoDemo) {

				bresult = trataresultFuncao(ecf.acionaGavetaDinheiro(205));

				if (!bresult) {

					Logger.gravaLogTxt("", sUserID, Logger.LGEP_ABERT_GAVETA, sMensErroLog);
				}
			}
		}

		return bresult;

	}

	public boolean cancelaCupom() {

		boolean bresult = false;

		if (dll) {
			bresult = bema.cancelaCupom(sUserID, bModoDemo);
		}
		else if (!bModoDemo && ecf != null) {

			bresult = trataresultFuncao(ecf.cancelaCupom());

			if (!bresult) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_CANC_CUPOM, sMensErroLog);
			}

		}

		return bresult;

	}

	public boolean cancelaItemAnterior() {

		boolean bresult = false;

		if (dll) {
			bresult = bema.cancelaItemAnterior(sUserID, bModoDemo);
		}
		else if (!bModoDemo && ecf != null) {

			Logger.gravaLogTxt("", sUserID, Logger.LGIF_CANC_ITEM, "TENTATIVA DE CANCELAMENTO DE ITEM ANTERIOR PELO OPERADOR " + sUserID);

			bresult = trataresultFuncao(ecf.cancelaItemAnterior());

			if (!sMensErroLog.trim().equals("")) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_CANC_ITEM, sMensErroLog);
			}
		}

		return bresult;

	}

	public boolean cancelaItemGenerico(final int item) {

		boolean bresult = false;

		if (dll) {
			bresult = bema.cancelaItemGenerico(sUserID, item, bModoDemo);
		}
		else if (!bModoDemo && ecf != null) {

			Logger.gravaLogTxt("", sUserID, Logger.LGIF_CANC_ITEM, "CANCELAMENTO DE ITEM [" + item + "] PELO " + sUserID);

			bresult = trataresultFuncao(ecf.cancelaItemGenerico(item));

			if (!sMensErroLog.trim().equals("")) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_CANC_ITEM, sMensErroLog);
			}
		}

		return bresult;

	}

	public boolean fechaComprovanteNaoFiscalVinculado() {

		boolean bresult = false;

		if (dll) {
			bresult = bema.fechaComprovanteNaoFiscalVinculado(sUserID, bModoDemo);
		}
		else if (!bModoDemo && ecf != null) {

			bresult = trataresultFuncao(ecf.fechamentoRelatorioGerencial());

			if (!bresult) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_FECHA_N_FISCAL_VIN, "ERRO NO COMPROVANTE NÃO FISCAL VINCULADO: " + sMensErroLog);
			}

		}

		return bresult;

	}

	public boolean fechaCupomFiscal(final String sFormaPagto, final String sAcreDesc, final String sTipoAcreDesc, final float fVlrAcreDesc, final float fVlrPago, final String sMensagem) {

		boolean bresult = false;

		if (dll) {
			bresult = bema.fechaCupomFiscal(sUserID, sFormaPagto, sAcreDesc, sTipoAcreDesc, fVlrAcreDesc, fVlrPago, sMensagem, bModoDemo);
		}
		else if (!bModoDemo && ecf != null) {

			if ("".equals(sAcreDesc) || "D".equals(sAcreDesc)) {
				if (sTipoAcreDesc.trim().equals("$")) {
					bresult = trataresultFuncao(ecf.iniciaFechamentoCupom(AbstractECFDriver.DESCONTO_VALOR, fVlrAcreDesc));
				}
				else {
					bresult = trataresultFuncao(ecf.iniciaFechamentoCupom(AbstractECFDriver.DESCONTO_PERC, fVlrAcreDesc));
				}
			}
			else {
				if (sTipoAcreDesc.trim().equals("$")) {
					bresult = trataresultFuncao(ecf.iniciaFechamentoCupom(AbstractECFDriver.ACRECIMO_VALOR, fVlrAcreDesc));
				}
				else {
					bresult = trataresultFuncao(ecf.iniciaFechamentoCupom(AbstractECFDriver.ACRECIMO_PERC, fVlrAcreDesc));
				}
			}

			if (bresult) {
				String formaPag = Funcoes.adicEspacosDireita(StringFunctions.clearAccents(sFormaPagto), 16);
				String sCodFormaPag = StringFunctions.strZero(ecf.programaFormaPagamento(formaPag), 2);

				bresult = trataresultFuncao(ecf.efetuaFormaPagamento(sCodFormaPag, fVlrPago, ""));
			}

			if (bresult) {
				ecf.finalizaFechamentoCupom(sMensagem);
			}

			if (!sMensErroLog.trim().equals("")) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_FECHA_CUPOM, "Forma Pagto.: " + sFormaPagto + " - Valor Pago:" + fVlrPago + " - Msg. final de cupom: " + sMensagem + " - Erro: "
						+ sMensErroLog);
			}

		}

		return bresult;

	}

	public boolean fechaRelatorioGerencial() {

		boolean bresult = false;

		if (dll) {
			bresult = bema.fechaRelatorioGerencial(sUserID, bModoDemo);
		}
		else if (!bModoDemo && ecf != null) {

			bresult = trataresultFuncao(ecf.fechamentoRelatorioGerencial());

			if (!bresult) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_FECHA_REL_REGENCIAL, "ERRO NO COMPROVANTE: " + sMensErroLog);
			}

		}

		return bresult;

	}

	public boolean finalizaModoTEF(String sUserID, boolean bModoDemo) {

		boolean bresult = false;

		if (dll) {
			bresult = bema.finalizaModoTEF(sUserID, bModoDemo);
		}/*
		 * else { if ( ! bModoDemo ) {
		 * 
		 * bresult = trataresultFuncao( ecf.acionaGavetaDinheiro( 1 ) );
		 * 
		 * if ( ! bresult ) { Logger.gravaLogTxt( "", sUserID,
		 * Logger.LGEP_ABERT_GAVETA, sMensErroLog ); } } }
		 */

		return bresult;

	}

	public boolean iniciaModoTEF(String sUserID, boolean bModoDemo) {

		boolean bresult = false;

		if (dll) {
			bresult = bema.iniciaModoTEF(sUserID, bModoDemo);
		}/*
		 * else { if ( ! bModoDemo ) {
		 * 
		 * bresult = trataresultFuncao( ecf.acionaGavetaDinheiro( 1 ) );
		 * 
		 * if ( ! bresult ) { Logger.gravaLogTxt( "", sUserID,
		 * Logger.LGEP_ABERT_GAVETA, sMensErroLog ); } } }
		 */

		return bresult;

	}

	public boolean leituraX() {

		return leituraX(false);

	}

	public boolean leituraX(final boolean saidaSerial) {

		boolean bresult = false;

		if (dll) {
			bresult = bema.leituraX(sUserID, bModoDemo);
		}
		else if (!bModoDemo && ecf != null) {

			if (saidaSerial) {
				bresult = trataresultFuncao(ecf.leituraXSerial());
			}
			else {
				bresult = trataresultFuncao(ecf.leituraX());
			}

			if (!bresult) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_LEITRA_X, sMensErroLog);
			}

		}

		return bresult;

	}

	public String leStatus() {

		String sMensagem = "";
		// String sSep = "";

		if (dll) {
			sMensagem = bema.leStatus(sUserID, bModoDemo);
		}
		else if (!bModoDemo && ecf != null) {

			/*
			 * String tmp = ecf.getStatus();
			 * 
			 * if( tmp != null && tmp.length() > 0 ) {
			 * 
			 * String[] status = tmp.split(",");
			 * 
			 * int st1 = Integer.parseInt( status[ 1 ] ); int st2 =
			 * Integer.parseInt( status[ 2 ] );
			 * 
			 * if ( st1 >= 128 ) { st1 -= 128; sMensagem += "Fim de papel.<BR>";
			 * } if ( st1 >= 64 ) { st1 -= 64; sMensagem += "Pouco papel.<BR>";
			 * } if ( st1 >= 32 ) { st1 -= 32; sMensagem +=
			 * "Erro no relógio.<BR>"; } if ( st1 >= 16 ) { st1 -= 16; sMensagem
			 * += "Impressora em erro.<BR>"; } if ( st1 >= 8 ) { st1 -= 8;
			 * sMensagem += "Primeiro dado de CMD não foi ESC( 1Bh).<BR>"; } if
			 * ( st1 >= 4 ) { st1 -= 4; sMensagem += "Comando inexistente.<BR>";
			 * } if ( st1 >= 2 ) { st1 -= 2; sMensagem +=
			 * "Cupom fiscal aberto.<BR>"; } if ( st1 >= 1 ) { st1 -= 1;
			 * sMensagem += "Número de parâmetro de CMD inválido.<BR>"; }
			 * 
			 * if ( sMensagem.length() > 0 ) { sSep = "\n"; }
			 * 
			 * if ( st2 >= 128 ) { st2 -= 128; sMensagem += sSep +
			 * "Tipo de parâmetro de CMD inválido.<BR>"; } if ( st2 >= 64 ) {
			 * st2 -= 64; sMensagem += sSep + "Memória fiscal lotada.<BR>"; } if
			 * ( st2 >= 32 ) { st2 -= 32; sMensagem += sSep +
			 * "Erro na memória RAM CMOS não volátil.<BR>"; } if ( st2 >= 16 ) {
			 * st2 -= 16; sMensagem += sSep + "Alíquota não programada.<BR>"; }
			 * if ( st2 >= 8 ) { st2 -= 8; sMensagem += sSep +
			 * "Capacidade de alíquotas esgotada.<BR>"; } if ( st2 >= 4 ) { st2
			 * -= 4; sMensagem += sSep + "Cancelamento não permitido.<BR>"; } if
			 * ( st2 >= 2 ) { st2 -= 2; sMensagem += sSep +
			 * "CNPJ/IE do proprietário não programados.<BR>"; } if ( st2 >= 1 )
			 * { st2 -= 1; sMensagem += sSep + "Comando não executado."; } }
			 */

		}

		return sMensagem;

	}

	public int numeroCupom() {

		int iresult = 0;

		if (dll) {
			iresult = bema.numeroCupom(sUserID, bModoDemo);
		}
		else if (!bModoDemo && ecf != null) {

			String sNumero = ecf.resultNumeroCupom();

			if (sNumero == null) {
				sNumero = "";
			}
			else {
				iresult = Integer.parseInt(sNumero);
			}
			/*
			 * if ( ! trataresultFuncao( iresult ) ) { if ( sNumero.equals( "" )
			 * ) { Funcoes.mensagemErro( null, "Número do cupom nulo!\n" +
			 * "Entre em contato com departamento técnico!!!" ); iresult =
			 * 999999; } else { sNumero = sNumero.trim(); if ( sNumero.equals(
			 * "" ) ) iresult = 0; } } else { if ( Funcoes.ehInteiro( sNumero )
			 * ) { iresult = Integer.parseInt( sNumero ); } else {
			 * Funcoes.mensagemErro( null, "Número do cupom não é inteiro!\n" +
			 * "Entre em contato com departamento técnico!!!" ); iresult =
			 * 888888; } }
			 */

		}

		return iresult;

	}

	public boolean programaAliquotas(final String sVal, final int iModoICMISS) {

		boolean bresult = false;

		if (dll) {
			bresult = bema.programaAliquotas(sUserID, sVal, iModoICMISS, bModoDemo);
		}
		else if (!bModoDemo && ecf != null) {

			char opt = iModoICMISS == 0 ? AbstractECFDriver.ICMS : AbstractECFDriver.ISS;
			bresult = trataresultFuncao(ecf.adicaoDeAliquotaTriburaria(sVal, opt));

			if (!bresult) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_INCL_ALIQ, sMensErroLog);
			}

		}

		return bresult;

	}

	public boolean programaMoeda(final String sSimb, final String sSing, final String sPlur) {

		boolean bresult = false;

		if (dll) {
			bresult = bema.programaMoeda(sUserID, sSing, sPlur, bModoDemo);
		}
		else if (!bModoDemo && ecf != null) {

			String simbolo = StringFunctions.clearAccents(sSimb);
			final String singular = StringFunctions.clearAccents(sSing);
			final String plural = StringFunctions.clearAccents(sPlur);

			if (simbolo.indexOf('$') > -1) {
				simbolo = " " + simbolo.replace('$', ' ');
			}

			System.out.println("\nAltera simbolo moeda:\n");

			bresult = trataresultFuncao(ecf.alteraSimboloMoeda(simbolo));

			System.out.println("\nDepois simbolo moeda:\n");

			if (bresult) {

				System.out.println("Programa moeda no singular:");

				bresult = trataresultFuncao(ecf.programaMoedaSingular(singular));

				if (bresult) {

					System.out.println("Programa moeda no plural:");

					bresult = trataresultFuncao(ecf.programaMoedaPlural(plural));

					if (!bresult) {
						Logger.gravaLogTxt("", sUserID, Logger.LGEP_PROG_MOEDA, "Moeda Plural - " + sMensErroLog);
					}

				}
				else {
					Logger.gravaLogTxt("", sUserID, Logger.LGEP_PROG_MOEDA, "Moeda Singular - " + sMensErroLog);
				}

			}
			else {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_ALT_SIMB_MOEDA, "Simbolo Moeda Corrente - " + sMensErroLog);
			}

		}

		return bresult;

	}

	public boolean reducaoZ() {

		boolean bresult = false;

		if (dll) {
			bresult = bema.reducaoZ(sUserID, bModoDemo);
		}
		else if (!bModoDemo && ecf != null) {

			bresult = trataresultFuncao(ecf.reducaoZ());

			if (!bresult) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_REDUCAO_Z, sMensErroLog);
			}

		}

		return bresult;

	}

	public boolean relatorioGerencialTef(final String comprovante) {

		boolean bresult = false;

		if (dll) {
			bresult = bema.relatorioGerencialTef(sUserID, comprovante, bModoDemo);
		}

		return bresult;

	}

	public String resultAliquotas() {

		String sresult = "";
		final int iNumCharAliq = 64;

		if (dll) {
			sresult = bema.retornaAliquotas(sUserID, bModoDemo);
		}
		else if (!bModoDemo && ecf != null) {

			try {

				String aliquotas = ecf.resultAliquotas();

				final int tamanho = Integer.parseInt(aliquotas.substring(0, aliquotas.length() - iNumCharAliq));

				sresult = aliquotas.substring(aliquotas.length() - iNumCharAliq, ( tamanho * 4 ) + 2);

			}
			catch (Exception e) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_RET_ALIQ, sMensErroLog);
			}

		}

		return sresult;

	}

	public boolean sangria(final BigDecimal valor) {

		boolean bresult = false;

		if (dll) {
			bresult = bema.sangria(sUserID, valor, bModoDemo);
		}
		else if (!bModoDemo && ecf != null && valor.floatValue() > 0) {

			bresult = trataresultFuncao(ecf.comprovanteNFiscalNVinculado("SA", valor.floatValue(), ""));

			if (!bresult) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_FAZ_SANGRIA, "ERRO NA SANGRIA-VALOR: " + valor.floatValue() + "|" + sMensErroLog);
			}

		}

		return bresult;

	}

	public boolean suprimento(final BigDecimal valor) {

		boolean bresult = false;

		if (dll) {
			bresult = bema.suprimento(sUserID, valor, "Dinheiro", bModoDemo);
		}
		else if (!bModoDemo && ecf != null && valor.floatValue() > 0) {

			bresult = trataresultFuncao(ecf.comprovanteNFiscalNVinculado("SU", valor.floatValue(), ""));

			if (!bresult) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_FAZ_SUPRIMENTO, "ERRO NO SUPRIMENTO-VALOR: " + valor.floatValue() + "-" + sMensErroLog);
			}

		}

		return bresult;

	}

	public boolean trataresultFuncao(final STResult result) {

		boolean bresult = true;

		String sMensagem = "";

		sMensErroLog = "";

		int iresult = result.getFirstCode();

		switch (iresult) {

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
			sMensagem = "Erro ao criar ou gravar no arquivo STATUS.TXT ou result.TXT";
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
			sMensagem = "result indefinido: " + iresult;
			break;

		}

		if (!"".equals(sMensagem.trim())) {

			bresult = false;

			sMensErroLog = sMensagem + " - STATUS: " + iresult;

			Funcoes.mensagemErro(null, sMensagem);

		}

		return bresult;
	}

	public boolean usaComprovanteNaoFiscalVinculadoTef(final String sTexto) {

		boolean bresult = false;

		if (dll) {
			bresult = bema.usaComprovanteNaoFiscalVinculadoTef(sUserID, sTexto, bModoDemo);
		}/*
		 * else { if ( ! bModoDemo ) {
		 * 
		 * bresult = trataresultFuncao( ecf.acionaGavetaDinheiro( 1 ) );
		 * 
		 * if ( ! bresult ) { Logger.gravaLogTxt( "", sUserID,
		 * Logger.LGEP_ABERT_GAVETA, sMensErroLog ); } } }
		 */

		return bresult;

	}

	public boolean vendaItem(final int iCodprod, final String sDescprod, final String sTributo, final BigDecimal bdQuant, final BigDecimal bdValor, final BigDecimal bdValDesc) {

		boolean bresult = false;

		if (dll) {
			bresult = bema.vendaItem(sUserID, iCodprod, sDescprod, sTributo, bdQuant.doubleValue(), bdValor.doubleValue(), bdValDesc.doubleValue(), bModoDemo);
		}
		else if (!bModoDemo && ecf != null) {

			String sCodprod = StringFunctions.strZero(String.valueOf(iCodprod), 29);
			String sDescricao = StringFunctions.clearAccents(Funcoes.adicionaEspacos(sDescprod, 29));
			int index = 1;

			while (!trataresultFuncao(ecf.vendaItem(sCodprod, sDescricao, sTributo, AbstractECFDriver.QTD_DECIMAL, bdQuant.floatValue(), bdValor.floatValue(), AbstractECFDriver.DESCONTO_VALOR,
					bdValDesc.floatValue()))) {

				Logger.gravaLogTxt("", sUserID, Logger.LGEP_IMPRES_ITEM, "Codigo: " + sCodprod + "-Descrição: " + sDescprod + "-Qtd.: " + bdQuant.floatValue() + "-Valor: " + bdValor.floatValue()
						+ "-Desconto: " + bdValDesc.floatValue() + "-Erro: " + sMensErroLog);

				Funcoes.espera(2);

				if (index++ >= 5) {
					bresult = false;
					break;
				}

			}

			if (!sMensErroLog.trim().equals("")) {
				Logger.gravaLogTxt("", sUserID, Logger.LGEP_CANC_ITEM, sMensErroLog);
			}
		}

		return bresult;

	}

	public boolean verificaCupomAberto() {

		boolean bresult = false;

		if (dll) {
			bresult = bema.verificaCupomAberto(sUserID, bModoDemo);
		}
		else if (!bModoDemo) {

			String tmp = leStatus();

			if (tmp.indexOf("aberto") > -1) {

				bresult = true;
			}
		}

		return bresult;

	}

	public boolean executadaReducaoZ() {

		boolean bresult = false;

		if (dll) {
			// bresult = bema.verificaCupomAberto( sUserID, bModoDemo );
		}
		else if (!bModoDemo) {

			int iret = Integer.parseInt(ecf.resultVariaveis(AbstractECFDriver.V_FLAG_FISCAL));
			if (iret >= 128) {
				iret -= 128;
			}
			if (iret >= 64) {
				iret -= 64;
			}
			if (iret >= 32) {
				iret -= 32;
			}
			if (iret >= 16) {
				iret -= 16;
			}
			if (iret >= 8) {
				iret -= 8;
				bresult = true;
			}
		}

		return bresult;
	}

	public String transStatus(char cStatus) {

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

}
