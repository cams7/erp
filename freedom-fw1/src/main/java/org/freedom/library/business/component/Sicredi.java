package org.freedom.library.business.component;

import java.math.BigDecimal;
import java.util.Date;

import org.freedom.library.functions.Funcoes;

public class Sicredi extends Banco {

	public String geraCodBar(final String codbanco, final String codmoeda, final String dvbanco
			, final Long fatvenc, final BigDecimal vlrtitulo, final String convenio
			, final String tpnossonumero, final Long doc, final Long seq, final Long rec
			,final Long nparc, final Date dtemit, final String agencia, final String conta, final String carteira, final String modalidade) {

		final StringBuffer barcode = new StringBuffer();
		final StringBuffer parte1a = new StringBuffer();
		final StringBuffer parte1b = new StringBuffer();
		final StringBuffer parte2 = new StringBuffer();

		final String bufCodbanco = strZero(codbanco, 3);
		final String bufCodmoeda = strZero(codmoeda, 1);
		final String bufFatvenc = strZero(fatvenc.toString(), 4);
		final String bufVlrtitulo = geraVlrtitulo(vlrtitulo);
		final String bufConvenio = geraConvenio(convenio);
		final String bufModalidade = strZero(modalidade, 2);
		final String bufNossoNumero = geraNossoNumero(tpnossonumero, bufModalidade, bufConvenio, doc, seq, rec, nparc, dtemit, true);
		//final String bufNossoNumero = "072000031";
		final String bufAgencia = bufConvenio.substring(0, 4);
				//strZero(getCodSig(agencia)[0], 4);
		
		final String bufPosto = bufConvenio.substring(4, 6);
		
		setPosto(bufPosto);

		final String bufConta = bufConvenio.substring(6, 11);
		
		
				//strZero(getCodSig(conta)[0], 8);
		//final String bufCarteira = strZero(carteira, 2);
		

		parte1a.append(bufCodbanco);
		parte1a.append(bufCodmoeda);
		parte1b.append(bufFatvenc);
		parte1b.append(bufVlrtitulo);
		// Numeéico correspondente ao tipo de cobrança: "1" - Com Registro "3" - Sem registro
		// Numérico correspondente ao tipo de carteira: "1" - Carteira simples
		
		parte2.append(bufModalidade);
		parte2.append(bufNossoNumero);
		parte2.append(bufAgencia);
		parte2.append(bufPosto);
		parte2.append(bufConta);
		parte2.append("1"); // 1 - Quando houver valor expresso no campo "valor do documento"
		parte2.append("0"); // Filler zeros
		parte2.append(digVerif(parte2.toString(), 11));

		barcode.append(parte1a);
		barcode.append(digVerifGeral(parte1a.toString()  + parte1b.toString() + parte2.toString()));
		barcode.append(parte1b);
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
			campo1.append(codbar.substring(19, 24 )); // 5 primeiras posições do campo livre
			//campo1.append(digVerif(campo1.toString(), 10)); // dígito verificador 
			campo1.append(calcDvMod10(campo1.toString())); // dígito verificador
			
			campo2.append(codbar.substring(24, 34)); // Posição 26 a 35 do código de barras - ou posição 6 a 15 do campo livre
			//campo2.append(digVerif(campo2.toString(), 10)); // DAC que amarra o
			campo2.append(calcDvMod10(campo2.toString())); // DAC que amarra o
			// campo 2
			campo3.append(codbar.substring(34, 44)); // Posição 35 a 34 do
			// código de barras
			//campo3.append(digVerif(campo3.toString(), 10)); // DAC que amarra o
			campo3.append(calcDvMod10(campo3.toString())); // DAC que amarra o
			
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
		return strZero(convenio, 11);
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
		final StringBuffer bufcalc = new StringBuffer();
		// Adicionar no convênio as informações 
		// aaaa = Agencia
		// pp = Posto cedente
		// ccccc = conta 
		// yy = ano
		// b = indicação de geração do nosso número ( 1=Cooperativa,  2 = Cedente)
		// nnnnn = número sequencial
		
		String numcli = getNumCli(tpnossonumero, modalidade, convenio, doc, seq, rec, nparc);
		String ano = strZero( String.valueOf( Funcoes.getAno(dtemit) ), 4).substring(2);
		String by = "2";
		
		bufcalc.append(convenio);
		bufcalc.append(ano); // Ano 2 dígitos "yy"
		bufcalc.append(by); // Indicação de geração do nosso número "b"
		bufcalc.append(numcli);
		
		retorno.append(ano);
		retorno.append(by); // Indicação de geração do nosso número "b"
		retorno.append(numcli);
		
	   if (comdigito) {
			retorno.append( digVerif(bufcalc.toString(), 11, false) );
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

		retorno.append(getNumCli(tpnossonumero, doc, seq, rec, nparc, 5));

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
			for (int i = 0; i<peso.length; i++ ) {
				peso[i] = i + 2 ;
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

		//if (modulo == 10) {
			dig = String.valueOf(modulo - resto);
		//}
		//else {
			//dig = String.valueOf(resto);
		//}
		if (modulo == 10 && "10".equals(dig)) {
			dig = "0";
		}
		else if ( (modulo == 11) && ( ("10".equals(dig) ) || ("11".equals(dig) ) ) ) {
			dig = "0";
		}

		return dig;
	}
	
	public String digVerifGeral(final String codigo) {
		int modulo = 11;
		int[] peso;

	
		peso = new int[8];
		for (int i = 7; i>=0; i-- ) {
			peso[i] = i + 2 ;
		}
		int soma = 0;
		int calc = 0;
		int posi = 0;
		int resto = 0;
		String dig = null;

		for (int i = codigo.length() - 1; i > -1; i--) {

			calc = Integer.parseInt(codigo.substring(i, i + 1)) * ( peso[posi] );
			soma += calc;
			posi++;
			if (posi >= peso.length) {
				posi = 0;
			}
		}

		resto = soma % modulo;

		dig = String.valueOf(modulo - resto);
		int digNum = Integer.parseInt(dig);
		
		if ( ( digNum == 0 ) || (digNum == 1 ) || ( digNum > 9 ) ) {
			dig = "1";
		}

		return dig;
	}
	
	public String calcDvMod10(final String codigo){
		
		int[] peso =  new int[2];
		peso[0] = 2;
		peso[1] = 1;
		
		int posi = 0;
		int resto = 0;
		String str = "";
		String dig = null;
		int contador = 0;
		
		for (int i = codigo.length() - 1; i > -1; i--) {
			
			str = String.valueOf(Integer.parseInt(codigo.substring(i, i + 1)) * ( peso[posi] ));
			
			if(Integer.parseInt(str) >= 10){
				str = String.valueOf((Integer.parseInt(str.substring(0,1)) ) + (Integer.parseInt(str.substring(1,2))));
			}
			
			posi++;
			if(posi == 2){
				posi = 0;
			}
			
			contador += Integer.parseInt(str);
			
		}
			
		resto = contador % 10;

		if (resto==0) {
			dig = "0";
		} else {
			dig = String.valueOf(10 - resto);
		}
		
		return dig;
	}


	@Override
	public String geraCodBar() {
		return null;
	}

	@Override
	public String digVerif(String codigo) {
		return null;
	}

	@Override
	public String formatNossonumero(String nossonumero) {
		StringBuilder result = new StringBuilder("");
		if (nossonumero!=null) {
			if (nossonumero.length()>2) {
				result.append(nossonumero.substring(0,2));
				result.append("/");
				result.append(nossonumero.substring(2, nossonumero.length()-1));
				result.append("-");
				result.append(nossonumero.substring(nossonumero.length()-1));
			} else {
				result.append(nossonumero);
			}
			
		}
		
		return result.toString();
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
		StringBuilder result = new StringBuilder("");
		if (convenio.length()>2) {
			// Agencia
			result.append(convenio.substring(0, 4));
			result.append(".");
			if (convenio.length()>4) {
				//Posto
				result.append(convenio.substring(4, 6));
				result.append(".");
				if (convenio.length()>9) {
					// Conta
					result.append( convenio.substring(6, 11) );
				}
			}
		}

		return result.toString();
	}

}
