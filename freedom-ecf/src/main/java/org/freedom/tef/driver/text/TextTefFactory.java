/**		
 * Este programa é licenciado de acordo com a LGPL (Lesser General Public License), <br>
 * versão 2.1, Fevereiro de 1999 <br>
 * A LGPL deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste Programa. <br>
 * Caso uma cópia da LGPL não esteja disponível junto com este Programa, você pode contatar <br>
 * o LICENCIADOR ou então pegar uma cópia em: <a href=http://creativecommons.org/licenses/LGPL/2.1/legalcode.pt> Creative Commons</a> <br>
 * Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este programa é preciso estar de acordo com os termos da LGPL. <br>
 * <br>
 * <br>
 * Classe para obtenção de objetos TextTef especifico da Bandeira.<br>
 * <br>
 * <br>
 * @author Setpoint Informática Ltda. / Alex Rodrigues <br>
 * @version 1.0 (beta), 05/03/2008 <br>
 * <br>
 * @see org.freedom.tef.text.TextTef <br>
 * @see org.freedom.tef.text.TestTextTefProperties <br>
 * @see org.freedom.tef.Flag <br>
 */

package org.freedom.tef.driver.text;

import java.io.File;

import org.freedom.tef.driver.Flag;

public final class TextTefFactory {

	/**
	 * Sobrecarrega {@link #createTextTef(TextTefProperties, String, File)} <br>
	 * Deve ser efetuado o mapeamento das Flags anteriormente. <br>
	 * 
	 * @see #createTextTef(TextTefProperties, String, File)
	 * 
	 * @param textTefProperties
	 *            Parametros para a classe de função TEF.
	 * @param flagName
	 *            Bandeira
	 * @return Classe de funções TEF especifica da Bandeira.
	 * @throws Exception
	 *             Repassa qualquer exceção para possibilitar tratamento pela aplicação.
	 */
	public static TextTef createTextTef( final TextTefProperties textTefProperties, final String flagName ) throws Exception {

		return createTextTef( textTefProperties, flagName, (File) null );
	}

	/**
	 * Sobrecarrega {@link #createTextTef(TextTefProperties, String, File)} <br>
	 * 
	 * @see #createTextTef(TextTefProperties, String, File)
	 * 
	 * @param textTefProperties
	 *            Parametros para a classe de função TEF.
	 * @param flagName
	 *            Bandeira
	 * @param fileParametrosOfInitiation
	 *            Caminho para o arquivo para iniciação do mapa de Bandeiras X Classe de função TEF,<br>
	 *            caso ainda não se tenha mapeado.
	 * @return Classe de funções TEF especifica da Bandeira.
	 * @throws Exception
	 *             Repassa qualquer exceção para possibilitar tratamento pela aplicação.
	 */
	public static TextTef createTextTef( final TextTefProperties textTefProperties, final String flagName, final String fileParametrosOfInitiation ) throws Exception {

		return createTextTef( textTefProperties, flagName, new File( fileParametrosOfInitiation ) );
	}

	/**
	 * Cria um objeto de funções TEF especifico para a Bandeira.
	 * 
	 * @param textTefProperties
	 *            Parametros para a classe de função TEF.
	 * @param flagName
	 *            Bandeira
	 * @param fileParametrosOfInitiation
	 *            Arquivo para iniciação do mapa de Bandeiras X Classe de função TEF,<br>
	 *            caso ainda não se tenha mapeado.
	 * @return Classe de funções TEF especifica da Bandeira.
	 * @throws Exception
	 *             Repassa qualquer exceção para possibilitar tratamento pela aplicação.
	 */
	public static TextTef createTextTef( final TextTefProperties textTefProperties, final String flagName, final File fileParametrosOfInitiation ) throws Exception {

		TextTef textTef = null;
		if ( !Flag.isLoadTextTefFlagsMaps() ) {
			// Não é necessário tratar fileParametrosOfInitiation,
			// pois Flag.loadParametrosOfInitiation já o faz.
			Flag.loadParametrosOfInitiation( fileParametrosOfInitiation );
		}
		Class<TextTef> classTextTef = Flag.getTextTefFlagsMap().get( flagName );
		if ( classTextTef != null ) {
			try {
				textTef = classTextTef.newInstance();
				if ( textTef != null ) {
					textTef.initializeTextTef( textTefProperties );
				}
			}
			catch ( ClassCastException e ) {
				System.out.println( "[  ERROR  ] " + e.getMessage() );
				Flag.getTextTefFlagsMap().remove( flagName );
			}
			catch ( Exception e ) {
				System.out.println( "[  ERROR  ] " + e.getMessage() );
				throw new Exception( "Não foi possivél criar objeto para TEF!\n" + e.getMessage(), e );
			}
		}
		else {
			throw new Exception( "Classe do objeto para TEF para a bandeira " + flagName + " não encontrada!" );
		}
		return textTef;
	}
}
