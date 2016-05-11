package org.freedom.modulos.fnc.business.component.cnab;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.business.exceptions.ExceptionCnab;
import org.freedom.library.functions.Funcoes;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil;

public class CnabUtil extends FbnUtil {

	/**
	 * Converte java.util.Date para um java.lang.String em formato DDMMAAAA.
	 * 
	 * @param arg
	 *            java.util.Date.
	 * @return java.lang.String em formato DDMMAAAA.
	 * @throws Exception
	 */
	public static String dateToString( final Date arg, String formato ) throws ExceptionCnab {

		String retorno = null;

		try {
			if ( arg != null ) {

				int[] args = Funcoes.decodeDate( arg );
				
				if( ("DDMMAAAA".equals( formato ) ) || ("DDMMAA".equals( formato )) ){
					retorno = StringFunctions.strZero( String.valueOf( args[ 2 ] ), 2 ) + StringFunctions.strZero( String.valueOf( args[ 1 ] ), 2 );
				} else if ( "AAAAMMDD".equals( formato ) ) {
					retorno = StringFunctions.strZero( String.valueOf( args[ 1 ] ), 2 ) + StringFunctions.strZero( String.valueOf( args[ 2 ] ), 2 );
				}
			
				if ( "DDMMAAAA".equals( formato ) || formato == null ) {
					retorno = retorno + StringFunctions.strZero( String.valueOf( args[ 0 ] ), 4 );
				}
				else if ( "DDMMAA".equals( formato ) ) {
					retorno = retorno + String.valueOf( args[ 0 ] ).substring( 2 );
				}
				else if ( "AAAAMMDD".equals( formato ) ) {
					retorno = StringFunctions.strZero( String.valueOf( args[ 0 ] ), 4 ) + retorno;
				}
			}
			else {
				retorno = "00000000";
			}
		} catch ( RuntimeException e ) {
			throw new ExceptionCnab( "Erro na função dateToString.\n" + e.getMessage() );
		}

		return retorno;
	}

	/**
	 * Converte um java.lang.String em formato AAAAMMDD para java.util.Date
	 * 
	 * @param arg
	 *            java.lang.String.
	 * @return java.util.Date.
	 * @throws Exception
	 */
	public static Date stringAAAAMMDDToDate( final String arg ) throws ExceptionCnab {

		Date retorno = null;

		try {
			if ( arg != null && arg.trim().length() > 7 ) {

				int dia = Integer.parseInt( arg.substring( 6 ) );
				int mes = Integer.parseInt( arg.substring( 4, 6 ) );
				int ano = Integer.parseInt( arg.substring( 0, 4 ) );

				if ( dia > 0 && mes > 0 && ano > 0 ) {
					retorno = Funcoes.encodeDate( ano, mes, dia );
				}
			}
		} catch ( NumberFormatException e ) {
			throw new ExceptionCnab( "Erro na função stringToDate.\n" + e.getMessage() );
		}

		return retorno;
	}

	/**
	 * Converte um java.lang.String em formato AAAAMMDD para java.util.Date
	 * 
	 * @param arg
	 *            java.lang.String.
	 * @return java.util.Date.
	 * @throws Exception
	 */
	public static Date stringDDMMAAAAToDate( final String arg ) throws ExceptionCnab {

		Date retorno = null;

		try {
			if ( arg != null && arg.trim().length() > 7 ) {

				int dia = Integer.parseInt( arg.substring( 0, 2 ) );
				int mes = Integer.parseInt( arg.substring( 2, 4 ) );
				int ano = Integer.parseInt( arg.substring( 4 ) );

				if ( dia > 0 && mes > 0 && ano > 0 ) {
					retorno = Funcoes.encodeDate( ano, mes, dia );
				}
			}
		} catch ( NumberFormatException e ) {
			throw new ExceptionCnab( "Erro na função stringToDate.\n" + e.getMessage() );
		}

		return retorno;
	}

	public static Date stringDDMMAAToDate( final String arg ) throws ExceptionCnab {

		Date retorno = null;
		Calendar cl = new GregorianCalendar();

		try {

			cl = Calendar.getInstance();

			if ( arg != null && arg.trim().length() > 5 ) {

				int dia = Integer.parseInt( arg.substring( 0, 2 ) );
				int mes = Integer.parseInt( arg.substring( 2, 4 ) );

				String anoatu = cl.get( Calendar.YEAR ) + "";

				int ano = Integer.parseInt( anoatu.substring( 0, 2 ) + arg.substring( 4 ) );

				if ( dia > 0 && mes > 0 && ano > 0 ) {
					retorno = Funcoes.encodeDate( ano, mes, dia );
				}
			}
		} catch ( NumberFormatException e ) {
			throw new ExceptionCnab( "Erro na função stringToDate.\n" + e.getMessage() );
		}

		return retorno;
	}

	/**
	 * Converte para java.math.BigDecimal um String de inteiros sem ponto ou virgula.
	 * 
	 * @param arg
	 *            String de inteiros sem ponto ou virgula.
	 * @return java.math.BigDecimal com escala de 2.
	 * @throws NumberFormatException
	 */
	public static BigDecimal strToBigDecimal( final String arg ) throws ExceptionCnab {

		BigDecimal bdReturn = null;

		try {
			if ( arg != null ) {
				StringBuilder value = new StringBuilder();
				char chars[] = arg.trim().toCharArray();
				if ( chars.length >= 3 ) {
					int indexDecimal = ( chars.length - 1 ) - 2;
					for ( int i = 0; i < chars.length; i++ ) {
						value.append( chars[ i ] );
						if ( i == indexDecimal ) {
							value.append( '.' );
						}
					}
					bdReturn = new BigDecimal( value.toString() );
				}
			}
		} catch ( RuntimeException e ) {
			throw new ExceptionCnab( "Erro na função strToBigDecimal.\n" + e.getMessage() );
		}

		return bdReturn;
	}

}
