/**
 * @version 14/03/2004 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 *
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.funcoes <BR>
 * Classe: @(#)Extenso.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA <BR> <BR>
 *
 * Comentários da classe.....
 */

package org.freedom.library.functions;

import org.freedom.infra.functions.StringFunctions;

/**
 * @author robson
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class Extenso {

	public static void main(String[] args) {
		System.out.println(extenso(132.98, "real", "reais", "centavo", "centavos"));
	}

	public static String extenso(double dExValor, String sExMoedaS, String sExMoedaP, String sExDecS, String sExDecP) {

		String sExTr = "";
		String sExBi = "";
		String sExMi = "";
		String sExMil = "";
		String sExCe = "";
		String sExDe = "";
		String sExValor2 = "";
		String sExValor3 = "";
		int iTamEx = 0;

		sExValor2 = Funcoes.transValor(dExValor + "", 17, 2, false);
		sExValor2 = StringFunctions.replicate(" ", 17 - sExValor2.length()) + sExValor2;
		// System.out.println(sExValor2.length());

		iTamEx = sExValor2.trim().length();
		sExDe = dezenas(sExValor2.substring(15, 17));

		// System.out.println(sExValor2.length());

		if (iTamEx > 2)
			sExCe = centenas(sExValor2.substring(12, 15));

		if (iTamEx > 5) {
			sExMil = centenas(sExValor2.substring(9, 12));
			if (!sExMil.trim().equals(""))
				sExMil += " mil" + ( !sExCe.trim().equals("") ? ( iTamEx > 7 ? ", " : " e " ) : "" );
			/* sExDe */
		}

		if (iTamEx > 8) {
			sExMi = centenas(sExValor2.substring(6, 9));
			if (!sExMi.trim().equals(""))
				sExMi += ( sExMi.equals("um") ? " milhao" : " milhoes" ) + ( ( sExMil + sExCe ).trim().equals("") ? " de" : ", " );
		}

		if (iTamEx > 11) {
			sExBi = centenas(sExValor2.substring(3, 6));
			if (!sExBi.trim().equals(""))
				sExBi += ( sExBi.equals("um") ? " bilhao" : " bilhoes" ) + ( ( sExMil + sExCe + sExMi ).trim().equals("") ? " de" : ", " );
		}

		if (iTamEx > 14) {
			sExTr = centenas(sExValor2.substring(0, 3));
			if (!sExTr.trim().equals(""))
				sExTr += ( sExTr.equals("um") ? " trilhao" : " trilhoes" ) + ( ( sExMil + sExCe + sExMi + sExBi ).trim().equals("") ? " de" : ", " );
		}

		sExValor3 = sExTr + sExBi + sExMi + sExMil + sExCe;

		if (sExValor2.substring(14).trim().equals("1"))
			sExValor3 += " " + ( sExValor3.trim().equals("") ? " " : sExMoedaS );
		else
			sExValor3 += " " + ( sExValor3.trim().equals("") ? " " : sExMoedaP );

		if (!sExDe.trim().equals("")) {
			if (sExValor2.substring(16).trim().equals("1"))
				sExValor3 += ( !sExValor3.trim().equals("") ? " e " : " " ) + sExDe + " " + sExDecS;
			else
				sExValor3 += ( !sExValor3.trim().equals("") ? " e " : " " ) + sExDe + " " + sExDecP;
		}

		return sExValor3.trim();
	}

	private static String centenas(String sCeValor) {
		String sCeValor2 = "   ";
		if (sCeValor.substring(0, 1).equals("1")) {
			if (sCeValor.equals("100"))
				sCeValor2 = "cem";
			else
				sCeValor2 = "cento";
		}
		else if (sCeValor.substring(0, 1).equals("2"))
			sCeValor2 = "duzentos";
		else if (sCeValor.substring(0, 1).equals("3"))
			sCeValor2 = "trezentos";
		else if (sCeValor.substring(0, 1).equals("4"))
			sCeValor2 = "quatrocentos";
		else if (sCeValor.substring(0, 1).equals("5"))
			sCeValor2 = "quinhentos";
		else if (sCeValor.substring(0, 1).equals("6"))
			sCeValor2 = "seiscentos";
		else if (sCeValor.substring(0, 1).equals("7"))
			sCeValor2 = "setecentos";
		else if (sCeValor.substring(0, 1).equals("8"))
			sCeValor2 = "oitocentos";
		else if (sCeValor.substring(0, 1).equals("9"))
			sCeValor2 = "novecentos";

		if (!sCeValor.substring(1, 3).equals("00")) {
			if (!sCeValor2.trim().equals(""))
				sCeValor2 += " e " + dezenas(sCeValor.substring(1, 3));
			else
				sCeValor2 = dezenas(sCeValor.substring(1, 3));
		}

		return sCeValor2.trim();
	}

	private static String dezenas(String sDeValor) {
		String sDeValor2 = " ";
		if (sDeValor.equals("10"))
			sDeValor2 = "dez";
		else if (sDeValor.equals("11"))
			sDeValor2 = "onze";
		else if (sDeValor.equals("12"))
			sDeValor2 = "doze";
		else if (sDeValor.equals("13"))
			sDeValor2 = "treze";
		else if (sDeValor.equals("14"))
			sDeValor2 = "quatorze";
		else if (sDeValor.equals("15"))
			sDeValor2 = "quinze";
		else if (sDeValor.equals("16"))
			sDeValor2 = "dezesseis";
		else if (sDeValor.equals("17"))
			sDeValor2 = "dezesete";
		else if (sDeValor.equals("18"))
			sDeValor2 = "dezoito";
		else if (sDeValor.equals("19"))
			sDeValor2 = "dezenove";
		else if (sDeValor.substring(0, 1).equals("2"))
			sDeValor2 = "vinte";
		else if (sDeValor.substring(0, 1).equals("3"))
			sDeValor2 = "trinta";
		else if (sDeValor.substring(0, 1).equals("4"))
			sDeValor2 = "quarenta";
		else if (sDeValor.substring(0, 1).equals("5"))
			sDeValor2 = "cincoenta";
		else if (sDeValor.substring(0, 1).equals("6"))
			sDeValor2 = "sessenta";
		else if (sDeValor.substring(0, 1).equals("7"))
			sDeValor2 = "setenta";
		else if (sDeValor.substring(0, 1).equals("8"))
			sDeValor2 = "oitenta";
		else if (sDeValor.substring(0, 1).equals("9"))
			sDeValor2 = "noventa";

		if (( !sDeValor.substring(1, 2).equals("0") ) && ( !sDeValor.substring(0, 1).equals("1") )) {
			if (!sDeValor2.trim().equals(""))
				sDeValor2 += " e " + unidades(sDeValor.substring(1, 2));
			else
				sDeValor2 = unidades(sDeValor.substring(1, 2));
		}

		return sDeValor2.trim();

	}

	private static String unidades(String sUnValor) {
		String sUnValor2 = " ";
		if (sUnValor.equals("1"))
			sUnValor2 = "um";
		else if (sUnValor.equals("2"))
			sUnValor2 = "dois";
		else if (sUnValor.equals("3"))
			sUnValor2 = "tres";
		else if (sUnValor.equals("4"))
			sUnValor2 = "quatro";
		else if (sUnValor.equals("5"))
			sUnValor2 = "cinco";
		else if (sUnValor.equals("6"))
			sUnValor2 = "seis";
		else if (sUnValor.equals("7"))
			sUnValor2 = "sete";
		else if (sUnValor.equals("8"))
			sUnValor2 = "oito";
		else if (sUnValor.equals("9"))
			sUnValor2 = "nove";

		return sUnValor2.trim();
	}

}
