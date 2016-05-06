/**		
 * Este programa é licenciado de acordo com a LGPL (Lesser General Public License), <br>
 * versão 2.1, Fevereiro de 1999 <br>
 * A LGPL deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste Programa. <br>
 * Caso uma cópia da LGPL não esteja disponível junto com este Programa, você pode contatar <br>
 * o LICENCIADOR ou então pegar uma cópia em: <a href=http://creativecommons.org/licenses/LGPL/2.1/legalcode.pt> Creative Commons</a> <br>
 * Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este programa é preciso estar de acordo com os termos da LGPL. <br>
 * <br>
 * <br>
 * Classe para armazenar as propriedades para criação de TextTef.<br>
 * Como extende de java.util.Properties implementas as funcionalidades da classe mãe,<br>
 * entretando, a org.freedom.tef.driver.text.TextTefProperties implementa uma lista de chaves únicas,
 * e implementa dos métodos novos para facilitar a chamada, {@link #get(String)} e {@link #set(String, String)}<br>
 * estes fazem uma validação ({@link #validateTextTefPropertie(String)}) para utilizar-se somente as propriedades já especificadas.<br> 
 * <br>
 * <br>
 * @author Setpoint Informática Ltda. / Alex Rodrigues <br>
 * @version 1.0 (beta), 05/03/2008 <br>
 * @see org.freedom.tef.text.TextTef <br>
 * <br>
 */

package org.freedom.tef.driver.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TextTefProperties extends Properties {

	private static final long serialVersionUID = 1l;

	private static final List<String> keysList = new ArrayList<String>();

	public static final String PATH_SEND = "PATH_SEND";

	public static final String PATH_RESPONSE = "PATH_RESPONSE";

	public static final String ARQ_TMP = "IntPos.tmp";

	public static final String ARQ_SEND = "IntPos.001";

	public static final String ARQ_RESPONSE = "IntPos.001";

	public static final String ARQ_ACTIVE = "Ativo.001";

	public static final String ARQ_STATUS = "IntPos.STS";

	public static final String HEADER = "000-000";

	public static final String INDENTIFICATION = "001-000";

	public static final String DOCUMENT = "002-000";

	public static final String VALUE = "003-000";

	public static final String STATE_TRANSACTION = "009-000";

	public static final String NET_NAME = "010-000";

	public static final String TYPE_TRANSACTION = "011-000";

	public static final String NSU = "012-000";

	public static final String AUTHORIZATION = "013-000";

	public static final String LOT_TRANSACTION = "014-000";

	public static final String TIM_SERVER = "015-000";

	public static final String TIM_PLACE = "016-000";

	public static final String DATE_VOUCHER = "022-000";

	public static final String HOUR_VOUCHER = "023-000";

	public static final String NSU_CANCEL = "025-000";

	public static final String FINISHING = "027-000";

	public static final String AMOUNT_LINES = "028-000";

	public static final String RESPONSE_TO_PRINT = "029-";

	public static final String MESSAGE_OPERATOR = "030-000";

	public static final String ADMINISTRATOR = "040-000";

	public static final String TRAILER = "999-999";
	{
		keysList.add( PATH_SEND );
		keysList.add( PATH_RESPONSE );
		keysList.add( ARQ_TMP );
		keysList.add( ARQ_SEND );
		keysList.add( ARQ_RESPONSE );
		keysList.add( ARQ_ACTIVE );
		keysList.add( ARQ_STATUS );
		keysList.add( HEADER );
		keysList.add( INDENTIFICATION );
		keysList.add( DOCUMENT );
		keysList.add( VALUE );
		keysList.add( STATE_TRANSACTION );
		keysList.add( NET_NAME );
		keysList.add( TYPE_TRANSACTION );
		keysList.add( NSU );
		keysList.add( AUTHORIZATION );
		keysList.add( LOT_TRANSACTION );
		keysList.add( TIM_SERVER );
		keysList.add( TIM_PLACE );
		keysList.add( DATE_VOUCHER );
		keysList.add( HOUR_VOUCHER );
		keysList.add( NSU_CANCEL );
		keysList.add( FINISHING );
		keysList.add( AMOUNT_LINES );
		keysList.add( RESPONSE_TO_PRINT );
		keysList.add( MESSAGE_OPERATOR );
		keysList.add( ADMINISTRATOR );
		keysList.add( TRAILER );
	}

	public TextTefProperties() {

		super();
		initializeTextTefProperties();
	}

	/**
	 * Inicializa ao objeto adcionando as chaves pré-definidas.<br>
	 */
	private void initializeTextTefProperties() {

		setProperty( PATH_SEND, "" );
		setProperty( PATH_RESPONSE, "" );
		setProperty( ARQ_TMP, "" );
		setProperty( ARQ_SEND, "" );
		setProperty( ARQ_RESPONSE, "" );
		setProperty( ARQ_ACTIVE, "" );
		setProperty( ARQ_STATUS, "" );
		setProperty( HEADER, "" );
		setProperty( INDENTIFICATION, "" );
		setProperty( DOCUMENT, "" );
		setProperty( VALUE, "" );
		setProperty( STATE_TRANSACTION, "" );
		setProperty( NET_NAME, "" );
		setProperty( TYPE_TRANSACTION, "" );
		setProperty( NSU, "" );
		setProperty( AUTHORIZATION, "" );
		setProperty( LOT_TRANSACTION, "" );
		setProperty( TIM_SERVER, "" );
		setProperty( TIM_PLACE, "" );
		setProperty( DATE_VOUCHER, "" );
		setProperty( HOUR_VOUCHER, "" );
		setProperty( NSU_CANCEL, "" );
		setProperty( FINISHING, "" );
		setProperty( AMOUNT_LINES, "" );
		setProperty( RESPONSE_TO_PRINT, "" );
		setProperty( MESSAGE_OPERATOR, "" );
		setProperty( ADMINISTRATOR, "" );
		setProperty( TRAILER, "" );
	}

	/**
	 * Efetua a validação da chave verificando se ele pertençe as chaves pré-definidas.<Br>
	 * 
	 * @param key
	 *            Chave a ser validada.
	 * @return Verdadeiro para a chave pré-definida e falso para chaves não especificadas.
	 */
	public boolean validateTextTefPropertie( final String key ) {

		boolean validate = false;
		if ( key != null && key.trim().length() > 0 ) {
			if ( key.trim().length() > 3 && RESPONSE_TO_PRINT.equals( key.substring( 0, 4 ) ) ) {
				validate = true;
			}
			for ( String k : keysList ) {
				if ( k.equals( key ) ) {
					validate = true;
					break;
				}
			}
		}
		return validate;
	}

	/**
	 * Efetua uma cópia da lista original de propriedades para ser retornada evitando acesso indevido a lista.
	 * 
	 * @return Lista de propriedades.
	 */
	@SuppressWarnings( "unchecked" )
	public static List<String> getKeyList() {

		List<String> keysListCopy = (List<String>) ( (ArrayList<String>) TextTefProperties.keysList ).clone();
		return keysListCopy;
	}

	/**
	 * Implementa uma chamada de método simplificada para {@link #getProperty(String)},<br>
	 * somente se a validação com {@link #validateTextTefPropertie(String)} for positiva.<br>
	 * 
	 * @param key
	 *            Chave de referencia ao objeto desejado.
	 * @return Objeto referente a chave informada.
	 * @see #getProperty(String)
	 * @see #validateTextTefPropertie(String)
	 */
	public String get( final String key ) {

		String stringReturn = null;
		if ( validateTextTefPropertie( key ) ) {
			stringReturn = super.getProperty( key );
		}
		else {
			throw new IllegalArgumentException( "Chave de propriedade inválida!" );
		}
		return stringReturn;
	}

	/**
	 * Implementa uma chamada de método simplificada para {@link #setProperty(String, String)},<br>
	 * somente se a validação com {@link #validateTextTefPropertie(String)} for positiva.<br>
	 * 
	 * @param key
	 *            Chave de referencia ao objeto desejado.
	 * @param value
	 *            Objeto a ser referenciado pela chave informada.
	 * @return Objeto referente a chave informada.
	 * @see #setProperty(String, String)
	 * @see #validateTextTefPropertie(String)
	 */
	public String set( final String key, final String value ) {

		if ( value == null ) {
			throw new NullPointerException( "Valor da propriedade não especificado!" );
		}
		if ( validateTextTefPropertie( key ) ) {
			super.setProperty( key, value );
		}
		else {
			throw new IllegalArgumentException( "Chave de propriedade inválida! [ " + key + " = " + value + " ]" );
		}
		return value;
	}

	/**
	 * Redireciona o processamento para {@link #get(String)}.<br>
	 * 
	 * @param key
	 *            Chave de referencia ao objeto desejado.
	 * @return Objeto referente a chave informada.
	 * @see #getProperty(String)
	 */
	@Override
	public String getProperty( String key ) {

		return get( key );
	}

	/**
	 * Redireciona o processamento para {@link #set(String, String)}.<br>
	 * 
	 * @param key
	 *            Chave de referencia ao objeto desejado.
	 * @param value
	 *            Objeto a ser referenciado pela chave informada.
	 * @return Objeto referente a chave informada.
	 * @see #setProperty(String, String)
	 */
	@Override
	public synchronized Object setProperty( String key, String value ) {

		return set( key, value );
	}
}
