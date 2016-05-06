package org.freedom.library.functions;

import org.freedom.infra.functions.StringFunctions;

public class EANFactory {

	public String novoEAN13(String codpais, String codemp, String codigo) {

		StringBuilder codbar = new StringBuilder();
		int tamcod = 0;

		try {

			codbar.append(codpais.trim());
			codbar.append(codemp.trim());

			tamcod = 12 - codbar.length();
			codbar.append(StringFunctions.strZero(codigo, tamcod));
			codbar.append(calcDigEAN13(codbar.toString()));

		}
		catch (Exception e) {

			e.printStackTrace();
		}

		return codbar.toString();
	}

	public boolean isPar(int val) {
		boolean retorno = ( ( val % 2 ) == 0 );
		return retorno;
	}

	public String calcDigEAN13(String codbar) {

		int[] digitos = new int[12];
		int qtdpar = 0;
		int qtdimpar = 0;
		int total = 0;
		String calc = null;
		int dig = 0;
		StringBuilder codigo = new StringBuilder();
		if (codbar == null) {
			codigo.append(StringFunctions.replicate("0", 12));
		}
		else if (codbar.length() >= 12) {
			codigo.append(codbar.substring(0, 12));
		}
		else {
			codigo.append(codbar);
			codigo.append(StringFunctions.replicate("0", 12 - codbar.length()));
		}
		for (int i = 11; i > -1; i--) {
			digitos[11 - i] = Integer.parseInt(codigo.substring(i, i + 1));
			if (isPar(i + 1)) {
				qtdpar += digitos[11 - i];
			}
			else {
				qtdimpar += digitos[11 - i];
			}
		}

		qtdpar *= 3;

		total = qtdpar + qtdimpar;

		calc = String.valueOf(total);

		dig = 10 - Integer.parseInt(calc.substring(calc.length() - 1));
		if (dig == 10) {
			dig = 0;
		}

		return String.valueOf(dig);
	}
}
