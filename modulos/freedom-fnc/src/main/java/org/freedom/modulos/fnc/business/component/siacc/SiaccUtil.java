package org.freedom.modulos.fnc.business.component.siacc;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.freedom.modulos.fnc.library.business.compoent.FbnUtil;

public class SiaccUtil extends FbnUtil {

	/**
	 * Converte para java.math.BigDecimal um String de inteiros sem ponto ou virgula.
	 * 
	 * @param arg
	 *            String de inteiros sem ponto ou virgula.
	 * @return java.math.BigDecimal com escala de 2.
	 * @throws NumberFormatException
	 */
	public static BigDecimal strToBigDecimal( final String arg ) throws NumberFormatException {

		String value = null;
		if ( arg != null ) {
			char chars[] = arg.toCharArray();
			for ( int i = 0; i < chars.length; i++ ) {
				if ( '0' != chars[ i ] ) {
					value = arg.substring( i );
					break;
				}
			}
			if ( value != null ) {
				value = value.substring( 0, value.length() - 2 ) + "." + value.substring( value.length() - 2 );
			}
		}
		return new BigDecimal( value );
	}

	/**
	 * Converte para java.util.Date um String em formato AAAAMMDD.
	 * 
	 * @param arg
	 *            String de data no formato AAAAMMDD.
	 * @return java.util.Date.
	 * @throws Exception
	 */
	public static java.util.Date strToDate( final String arg ) throws Exception {

		Date retorno = null;
		if ( arg != null ) {
			Integer ano = Integer.parseInt( arg.substring( 0, 4 ) );
			Integer mes = Integer.parseInt( arg.substring( 4, 6 ) );
			Integer dia = Integer.parseInt( arg.substring( 6 ) );
			Calendar cal = Calendar.getInstance();
			cal.set( ano, mes - 1, dia );
			retorno = cal.getTime();
		}
		return retorno;
	}
}
