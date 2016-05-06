package org.freedom.library.business.component;

import java.math.BigDecimal;
import java.util.Date;

public abstract class Banco {

	public final static String BANCO_DO_BRASIL = "001";

	public final static String CAIXA_ECONOMICA = "104";

	public final static String BRADESCO = "237";
	
	public final static String ITAU = "341";
	
	public final static String SICRED = "748";
	
	public final static String HSBC = "399";
	
	public abstract String geraCodBar(final String codbanco, final String codmoeda, final String dvbanco, final Long fatvenc, final BigDecimal vlrtitulo, 
			final String convenio, final String tpnossonumero, final Long doc, final Long seq, final Long rec,
			final Long nparc, final Date dtemit, final String agencia, final String conta, final String carteira, final String modalidade);

	public abstract String geraCodBar();

	public abstract String geraLinhaDig(final String codbar, final Long fatvenc, final BigDecimal vlrtitulo);

	public abstract String geraNossoNumero(final String tpnossonumero, final String modalidade, final String convenio, final Long doc, 
			final Long seq, final Long rec, final Long nparc, final Date dtemit);

	public abstract String geraNossoNumero(final String tpnossonumero, final String modalidade, final String convenio, final Long doc, 
			final Long seq, final Long rec, final Long nparc, final Date dtemit, final boolean comdigito );

	public abstract String geraNossoNumero(final String tpnossonumero, final String modalidade, final String convenio, final Long doc, 
			final Long seq, final Long rec, final Long nparc, final Date dtemit, final boolean comdigito, final boolean comtraco);

	public abstract String[] getCodSig(String codigo);

	public abstract String getNumCli(String tpnossonumero, String modalidade, String convenio, Long doc, Long seq, Long rec, Long nparc);
	
	public abstract String digVerif(final String codigo);

	public abstract String digVerif(final String codigo, final int modulo);

	public abstract String digVerif(final String codigo, final int modulo, final boolean digx);

	public static String getIdentTitEmp(final Long rec, final Long nparc, final int tam) {
		final StringBuffer retorno = new StringBuffer();
		retorno.append( strZero(String.valueOf(rec),tam-3) );
		retorno.append( strZero(String.valueOf(nparc),3) );
		return retorno.toString();
	}
	
	public static String geraVlrtitulo(final BigDecimal vlrtitulo) {

		String retorno = null;
		retorno = transValor(vlrtitulo, 10, 2, true);
		return retorno;
	}

	public static String geraConvenio(final String convenio) {
		return geraConvenio(convenio, "999");
	}
	public static String geraConvenio(final String convenio , final String codbanco) {

		final StringBuffer retorno = new StringBuffer();
		final String bufConvenio;

		if (HSBC.equals(codbanco)) {
			if (convenio!=null) {
				if (convenio.trim().length()>7) {
					retorno.append(convenio.trim().substring(0,7));
				} else {
					retorno.append( strZero(convenio.trim(),7) );
				}
			}
		} else {
			if (convenio == null) {
				bufConvenio = "000000";
			} else if (convenio.trim().length() >= 7) {
				bufConvenio = convenio.trim().substring(convenio.trim().length() - 7);
			} else if (convenio.length() == 6) {
				bufConvenio = convenio.trim().substring(convenio.trim().length() - 6);
			} else {
				bufConvenio = convenio.trim();
			}
	
			if (bufConvenio.length() <= 4) {
				retorno.append(strZero(bufConvenio, 4));
			} else {
				retorno.append(strZero(bufConvenio, 6));
			}
		}
		return retorno.toString();
	}

	public static Long geraNumcli(final String tpnossonumero, final Long doc, final Long seq, final Long rec ) {
		Long numcli = null;
		if ( "D".equals(tpnossonumero) ) {  // Documento 
			numcli = doc;
		} else if ( "R".equals(tpnossonumero) ) { // Código do receber
			numcli = rec;
		} else if ( "S".equals(tpnossonumero) ) { // Sequencial
			numcli = seq;
		}
		return numcli;
	}
	
	public static String getNumCli(String tpnossonumero, Long doc, Long seq, Long rec, Long nparc, int tam) {

		final StringBuffer retorno = new StringBuffer();
		final Long numcli = geraNumcli(tpnossonumero, doc, seq, rec);
        int reduznparc = 2;
        if ( "S".equals(tpnossonumero) ) {
        	reduznparc = 0;
        }
        
		if (numcli == null) {
			retorno.append(strZero("0", tam - reduznparc));
		} else if (numcli.toString().length() > tam - reduznparc) {
			// Remover caracteres a mais da esquerda para direita
			retorno.append(numcli.toString().substring(numcli.toString().length() - ( tam - reduznparc )));
		} else {
			retorno.append(strZero(numcli.toString(), tam - reduznparc));
		}

		if (reduznparc==2) { 
			if (nparc == null) {
				retorno.append("00");
			} else {
				retorno.append(strZero(nparc.toString(), reduznparc));
			}
		}
		return retorno.toString();
	}
	
	public static String strZero(String val, int zeros) {

		String retorno = val;

		if (val == null) {
			return retorno;
		}
		retorno = replicate("0", zeros - val.trim().length());
		retorno += val.trim();

		return retorno;
	}

	public static String replicate(String texto, int Quant) {

		StringBuffer sRetorno = new StringBuffer();
		sRetorno.append("");
		for (int i = 0; i < Quant; i++) {
			sRetorno.append(texto);
		}
		return sRetorno.toString();
	}

	public static String transValor(final BigDecimal valor, int tam, int dec, boolean zerosEsq) {

		final String vlrcalc;
		String vlrdec = "";
		if (valor == null) {
			vlrcalc = new BigDecimal("0").toString();
		} else {
			vlrcalc = valor.toString();
		}
		
		String retorno = vlrcalc;
		for (int i = 0; i < vlrcalc.length(); i++) {
			if (( vlrcalc.substring(i, i + 1).equals(".") ) || ( vlrcalc.substring(i, i + 1).equals(",") )) {
				retorno = vlrcalc.substring(0, i);
				vlrdec = vlrcalc.substring(i + 1, vlrcalc.length());
				
				if (vlrdec.length() < dec) {
					vlrdec += replicate("0", dec - vlrdec.length());
				} else if (vlrdec.length() > dec) {
					vlrdec = vlrdec.substring(0, dec);
				}
				break;
			}
		}
		if (( vlrdec.trim().equals("") ) & ( dec > 0 )) {
			vlrdec = replicate("0", dec);
		}
		if (retorno.length() > ( tam - dec )) {
			retorno = retorno.substring(retorno.length() - ( tam - dec ) - 1, ( tam - dec ));
		}
		if (zerosEsq) {
			if (retorno.length() < ( tam - dec )) {
				retorno = replicate("0", ( tam - dec ) - retorno.length()) + retorno;
			}
		}
		return retorno + vlrdec;
	}

	public String getDigitoCampo(String campo, int mult) {

		// Esta rotina faz o calcula 212121

		int multiplicador = mult;
		int multiplicacao = 0;
		int soma_campo = 0;

		for (int i = 0; i < campo.length(); i++) {
			multiplicacao = Integer.parseInt(campo.substring(i, 1 + i)) * multiplicador;

			if (multiplicacao >= 10) {
				multiplicacao = Integer.parseInt(String.valueOf(multiplicacao).substring(0, 1)) + Integer.parseInt(String.valueOf(multiplicacao).substring(1));
			}

			soma_campo = soma_campo + multiplicacao;

			// ALTERADO POR VITOR MOTTA PARA SUBSTITUIR O COMENTARIO ABAIXO
			// valores assumidos: 212121...
			multiplicador = ( multiplicador % 2 ) + 1;

		}
		int dac = 10 - ( soma_campo % 10 );

		if (dac == 10) {
			dac = 0;
		}

		campo = campo + String.valueOf(dac);

		return campo;
	}

	public String getNossoNumeroFormatted() {
		return null;
	}

	public String getLinhaDigFormatted() {
		return null;
	}

	public abstract String formatNossonumero( String nossonumero );
	
	public abstract String getPosto();
	
	public abstract String setPosto(String posto);
	
	public abstract String formatConvenio(String convenio);
	
	public String getModulo11(String campo, int type) {

		// Modulo 11 - 234567 (type = 7)
		// Modulo 11 - 23456789 (type = 9)

		int multiplicador = 2;
		int multiplicacao = 0;
		int soma_campo = 0;

		for (int i = campo.length(); i > 0; i--) {
			multiplicacao = Integer.parseInt(campo.substring(i - 1, i)) * multiplicador;

			soma_campo = soma_campo + multiplicacao;

			multiplicador++;
			if (multiplicador > 7 && type == 7)
				multiplicador = 2;
			else if (multiplicador > 9 && type == 9)
				multiplicador = 2;
		}

		int dac = 11 - ( soma_campo % 11 );

		// codigo anterior... alterado segundo manual da caixa else if ((dac ==
		// 0 || dac == 1 || dac > 9) && type == 9)
		if (dac > 9) {
			dac = 0;
		}

		return ( ( Integer ) dac ).toString();
	}

	public String getSiglaCarteiraBanco() {
		return null;
	}

}
