package org.freedom.infra.util.crypt;

/**
 * Projeto: <a
 * href="http://sourceforge.net/projects/freedom-erp/">Freedom-infra</a> <br>
 * Este programa é licenciado de acordo com a LPG-PC <br>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada
 * pela Fundação do Software Livre (FSF); <BR>
 * <br>
 * 
 * <code>SimpleCrypt</code> prove de forma simples, uma lógica para criptografia
 * e descriptografia de <code>strings</code>.
 * 
 * @author Robson Sanchez
 * @version 0.0.1 – 25/06/2008
 * 
 * @since 25/06/2008
 */
public class SimpleCrypt {

	/**
	 * @param text
	 *            String a ser criptografada.
	 * @return String já criptografada.
	 */
	public static String crypt(String text) {

		StringBuilder buffer = new StringBuilder();
		char character = ( char ) 0;
		if (text != null) {
			for (int i = 0; i < text.length(); i++) {
				character = ( char ) ( ( ( int ) text.charAt(i) ) * 2 - i );
				buffer.append(character);
			}
		}
		return buffer.toString();
	}

	/**
	 * @param text
	 *            String a ser descriptografada.
	 * @return String descriptografada.
	 */
	public static String decrypt(String text) {

		StringBuilder buffer = new StringBuilder();
		char character = ( char ) 0;
		if (text != null) {
			for (int i = 0; i < text.length(); i++) {
				character = ( char ) ( ( ( ( int ) text.charAt(i) ) + i ) / 2 );
				buffer.append(character);
			}
		}
		return buffer.toString();
	}
}
