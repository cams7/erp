package org.freedom.library.business.component;

import java.math.BigDecimal;
import java.util.Date;

public class BancodoBrasil extends Banco {

	public String geraCodBar(final String codbanco, final String codmoeda, final String dvbanco, final Long fatvenc, final BigDecimal vlrtitulo, final String convenio, 
			final String tpnossonumero, final Long doc, final Long seq, final Long rec,
			final Long nparc, final Date dtemit, final String agencia, final String conta, final String carteira, final String modalidade) {

		final StringBuffer barcode = new StringBuffer();
		final StringBuffer parte1 = new StringBuffer();
		final StringBuffer parte2 = new StringBuffer();

		final String bufCodbanco = strZero(codbanco, 3);
		final String bufCodmoeda = strZero(codmoeda, 1);
		final String bufFatvenc = strZero(fatvenc.toString(), 4);
		final String bufVlrtitulo = geraVlrtitulo(vlrtitulo);
		final String bufConvenio = geraConvenio(convenio);
		final String bufModalidade = strZero(modalidade, 2);
		final String bufNossoNumero = geraNossoNumero(tpnossonumero, bufModalidade, bufConvenio, doc, seq, rec, nparc, dtemit, false);
		final String bufAgencia = strZero(getCodSig(agencia)[0], 4);
		final String bufConta = strZero(getCodSig(conta)[0], 8);
		final String bufCarteira = strZero(carteira, 2);

		parte1.append(bufCodbanco);
		parte1.append(bufCodmoeda);
		parte2.append(bufFatvenc);
		parte2.append(bufVlrtitulo);

		if ("21".equals(bufModalidade)) {
			// Formato do código de barras para convênios da carteira sem
			// registro
			// 16 e 18 - com nossó número livre de 17 posições
			parte2.append(bufConvenio);
			parte2.append(bufNossoNumero);
			parte2.append(bufModalidade);
		}
		else if (bufConvenio.length() >= 7) {
			// Código de barras bara convêncios acima de 1.000.000
			parte2.append("000000");
			parte2.append(bufNossoNumero);
			parte2.append(bufCarteira);
		}
		else {
			// Formato do código de barras para convênios com 4 ou 6 posições
			parte2.append(bufNossoNumero);
			parte2.append(bufAgencia);
			parte2.append(bufConta);
			parte2.append(bufCarteira);
		}

		barcode.append(parte1);
		barcode.append(digVerif(parte1.toString() + parte2.toString(), 11));
		barcode.append(parte2);

		return barcode.toString();
	}

	public String geraLinhaDig(final String codbar, final Long fatvenc, final BigDecimal vlrtitulo) {

		final StringBuffer linhadig = new StringBuffer();
		final StringBuffer campo1 = new StringBuffer();
		final StringBuffer campo2 = new StringBuffer();
		final StringBuffer campo3 = new StringBuffer();
		final StringBuffer campo4 = new StringBuffer();
		final StringBuffer campo5 = new StringBuffer();

		if (codbar != null) {

			final String bufCodbanco = codbar.substring(0, 3);
			final String bufCodmoeda = codbar.substring(3, 4);
			final String bufFatvenc = strZero(fatvenc.toString(), 4);
			final String bufVlrtitulo = geraVlrtitulo(vlrtitulo);

			campo1.append(bufCodbanco); // Código do banco
			campo1.append(bufCodmoeda); // Código da moeda

			// Formato da linha digitável para convênios da carteira sem
			// registro
			// 16 e 18 - com nossó número livre de 17 posições
			// Linha digitável para convênios de 4 ou 6 posições
			// Linha digitável para convêncios acima de 1.000.000

			campo1.append(codbar.substring(19, 24)); // Posição 20 a 24 do
			// código de barras
			campo1.append(digVerif(campo1.toString(), 10)); // Dígito
			// verificador do
			// campo 1
			campo2.append(codbar.substring(24, 34)); // Posição 25 a 34 do
			// código de barras
			campo2.append(digVerif(campo2.toString(), 10)); // DAC que amarra o
			// campo 2
			campo3.append(codbar.substring(34, 44)); // Posição 35 a 34 do
			// código de barras
			campo3.append(digVerif(campo3.toString(), 10)); // DAC que amarra o
			// campo 3
			campo4.append(codbar.substring(4, 5)); // Dígito verificador do
			// código de barras
			campo5.append(bufFatvenc); // Fator de vencimento
			campo5.append(bufVlrtitulo); // Valor do título
			linhadig.append(campo1);
			linhadig.append(campo2);
			linhadig.append(campo3);
			linhadig.append(campo4);
			linhadig.append(campo5);
		}

		return linhadig.toString();
	}

	public static String geraVlrtitulo(final BigDecimal vlrtitulo) {

		String retorno = null;
		retorno = transValor(vlrtitulo, 10, 2, true);

		return retorno;
	}

	public static String geraConvenio(final String convenio) {

		final StringBuffer retorno = new StringBuffer();
		final String bufConvenio;

		if (convenio == null) {
			bufConvenio = "000000";
		}
		else if (convenio.length() >= 7) {
			bufConvenio = convenio.substring(convenio.length() - 7);
		}
		else if (convenio.length() == 6) {
			bufConvenio = convenio.substring(convenio.length() - 6);
		}
		else {
			bufConvenio = convenio;
		}

		if (bufConvenio.length() <= 4) {
			retorno.append(strZero(bufConvenio, 4));
		}
		else {
			retorno.append(strZero(bufConvenio, 6));
		}

		return retorno.toString();
	}

	public String geraNossoNumero(final String tpnossonumero, final String modalidade, final String convenio, 
			final Long doc, final Long seq, final Long rec, final Long nparc, final Date dtemit) {
		return geraNossoNumero(tpnossonumero, modalidade, convenio, doc, seq, rec, nparc, dtemit, true);
	}

	public String geraNossoNumero(final String tpnossonumero, final String modalidade, final String convenio, 
			final Long doc, final Long seq, final Long rec, final Long nparc, final Date dtemit, final boolean comdigito) {
		return geraNossoNumero(tpnossonumero, modalidade, convenio, doc, seq, rec, nparc, dtemit, comdigito, false);
	}

	public String geraNossoNumero(final String tpnossonumero, final String modalidade, final String convenio, 
			final Long doc, final Long seq, final Long rec, 
			final Long nparc, final Date dtemit, final boolean comdigito, final boolean comtraco) {

		final StringBuffer retorno = new StringBuffer();

		if (!"21".equals(modalidade)) {
			retorno.append(convenio);
		}

		retorno.append(getNumCli(tpnossonumero, modalidade, convenio, doc, seq, rec, nparc));

		if (comdigito && ( convenio.length() == 4 || convenio.length() == 6 )) {

			if (comtraco) {
				retorno.append("-" + digVerif(retorno.toString(), 11, true));
			}
			else {
				retorno.append(digVerif(retorno.toString(), 11, true));
			}
		}

		return retorno.toString();
	}

	public String[] getCodSig(String codigo) {

		final String[] retorno = new String[2];
		final StringBuffer buffer = new StringBuffer();
		final String valido = "0123456789X";

		for (int i = 0; i < codigo.length(); i++) {

			if (valido.indexOf(codigo.charAt(i)) > -1) {
				buffer.append(codigo.charAt(i));
			}
			else if (codigo.charAt(i) == '-') {
				retorno[0] = buffer.toString();
				buffer.delete(0, buffer.length());
			}
		}
		if (retorno[0] == null) {
			retorno[0] = buffer.toString();
			retorno[1] = "";
		}
		else {
			retorno[1] = buffer.toString();
		}

		return retorno;
	}

	public String getNumCli(String tpnossonumero, String modalidade, String convenio, Long doc, Long seq, Long rec, Long nparc) {

		final StringBuffer retorno = new StringBuffer();

		if ("21".equals(modalidade)) {
			retorno.append(getNumCli(tpnossonumero, doc, seq, rec, nparc, 17));
		}
		else if (convenio.length() <= 4) {
			retorno.append(getNumCli(tpnossonumero, doc, seq, rec, nparc, 6));
		}
		else if (convenio.length() == 6) {
			retorno.append(getNumCli(tpnossonumero, doc, seq, rec, nparc, 5));
		}
		else {
			retorno.append(getNumCli(tpnossonumero, doc, seq, rec, nparc, 10));
		}

		return retorno.toString();
	}

	public String digVerif(final String codigo, final int modulo) {
		return digVerif(codigo, modulo, false);
	}

	public String digVerif(final String codigo, final int modulo, final boolean digx) {

		int[] peso;

		if (modulo == 10) {
			peso = new int[2];
			peso[0] = 2;
			peso[1] = 1;
		}
		else {
			peso = new int[8];
			for (int i = peso.length - 1; i > -1; i--) {
				peso[i] = peso.length - ( i - 1 );
			}
		}

		int soma = 0;
		int calc = 0;
		int posi = 0;
		int resto = 0;
		String str = "";
		String dig = null;

		for (int i = codigo.length() - 1; i > -1; i--) {
			if (modulo == 11) {
				calc = Integer.parseInt(codigo.substring(i, i + 1)) * ( peso[posi] );
			}
			else {
				// transforma o valor do produto em String
				str = String.valueOf(Integer.parseInt(codigo.substring(i, i + 1)) * ( peso[posi] ));
				// soma os valores. Exemplo: para resultado do produto = 14
				// (1+4=5).
				calc = 0;
				for (int s = 0; s < str.length(); s++) {
					calc += Integer.parseInt(str.substring(s, s + 1));
				}
			}
			soma += calc;
			posi++;
			if (posi >= peso.length) {
				posi = 0;
			}
		}

		resto = soma % modulo;

		if (modulo == 10) {
			dig = String.valueOf(modulo - resto);
		}
		else {
			dig = String.valueOf(resto);
		}
		if (modulo == 10 && "10".equals(dig)) {
			dig = "0";
		}
		else if (modulo == 11 && "10".equals(dig) && digx) {
			dig = "X";
		}
		else if (modulo == 11 && "0-1-10-11".indexOf(dig) > -1 && !digx) {
			dig = "1";
		}

		return dig;
	}

	@Override
	public String geraCodBar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String digVerif(String codigo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String formatNossonumero(String nossonumero) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPosto() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String setPosto(String posto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String formatConvenio(String convenio) {
		// TODO Auto-generated method stub
		return null;
	}


}
