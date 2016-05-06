/**		
 * Este programa é licenciado de acordo com a LGPL (Lesser General Public License), <br>
 * versão 2.1, Fevereiro de 1999 <br>
 * A LGPL deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste Programa. <br>
 * Caso uma cópia da LGPL não esteja disponível junto com este Programa, você pode contatar <br>
 * o LICENCIADOR ou então pegar uma cópia em: <a href=http://creativecommons.org/licenses/LGPL/2.1/legalcode.pt> Creative Commons</a> <br>
 * Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este programa é preciso estar de acordo com os termos da LGPL. <br>
 * <br>
 * <br>
 * A classe TextTef fornecerá implementações em comum para as classe de implementação de funções de TEF<br>
 * por meio de comunicação com troca de arquivos texto, que serão implementadas por bandeiras.<br>
 * <br>
 * <br>
 * @author Setpoint Informática Ltda. / Alex Rodrigues <br>
 * @version 1.0 (beta), 05/03/2008 <br>
 */

package org.freedom.tef.driver.text;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public abstract class TextTef {

	public static final String ATV = "ATV";

	public static final String ADM = "ADM";

	public static final String ADR = "ADR";

	public static final String CHQ = "CHQ";

	public static final String CRT = "CRT";

	public static final String CNC = "CNC";

	public static final String CNF = "CNF";

	public static final String PRE = "PRE";

	public static final String NCN = "NCN";

	private TextTefProperties textTefProperties;

	protected File fileTemp;

	protected File fileSend;

	protected File fileResponse;

	protected File fileStatus;

	protected File fileActive;

	public TextTef() {

	}

	public TextTefProperties getTextTefProperties() {

		return textTefProperties;
	}

	public void setTextTefProperties( final TextTefProperties textTefProperties ) throws Exception {

		if ( textTefProperties == null ) {
			throw new NullPointerException( "Properties para TEF não especificadas!" );
		}
		this.textTefProperties = textTefProperties;
	}

	public String get( String key ) throws Exception {

		return getTextTefProperties() != null ? getTextTefProperties().getProperty( key ) : null;
	}

	public String get( String key, String valueDefault ) throws Exception {

		return getTextTefProperties() != null ? getTextTefProperties().getProperty( key, valueDefault ) : null;
	}

	public String set( String key, String value ) throws Exception {

		return getTextTefProperties() != null ? getTextTefProperties().set( key, value ) : null;
	}

	/**
	 * Padroniza a inicialização dos parametros necessessários para o correto funcionamento do Objeto TEF.
	 * 
	 * @throws Exception
	 */
	abstract public void initializeTextTef() throws Exception;

	/**
	 * Agiliza o processo de instanciação do Objeto TEF,<br>
	 * definindo as propriedades, e também, invocando a inicialização do objeto com {@link #initializeTextTef()}
	 * 
	 * @see #setTextTefProperties(TextTefProperties)
	 * @see TextTefProperties
	 * 
	 * @param textTefProperties
	 *            Lista de propriedades
	 * 
	 * @throws Exception
	 */
	public void initializeTextTef( final TextTefProperties textTefProperties ) throws Exception {

		setTextTefProperties( textTefProperties );
		initializeTextTef();
	}

	/**
	 * Verifica se o gerenciador padrão está ativo.
	 * 
	 * @return Verdadeiro para gerenciador padrão ativo.
	 * 
	 * @throws Exception
	 */
	abstract public boolean standardManagerActive() throws Exception;

	/**
	 * Executa a requisição de venda.
	 * 
	 * @param numberDoc
	 *            Número do documento fiscal vinculado.
	 * @param value
	 *            Valor da transação.
	 * 
	 * @return Verdadeiro para sucesso na requisição.
	 * 
	 * @throws Exception
	 */
	abstract public boolean requestSale( final Integer numberDoc, final BigDecimal value ) throws Exception;

	/**
	 * Executa o acionamento das funções administrativas do gerenciador padrão.
	 * 
	 * @return Verdadeiro para sucesso no acionamento.
	 * 
	 * @throws Exception
	 */
	abstract public boolean requestAdministrator() throws Exception;

	/**
	 * Executa a requisição do cancelamento de transação TEF.
	 * 
	 * @param rede
	 *            Nome da rede
	 * @param nsu
	 *            Número da transação ( Número Sequencial Único )
	 * @param data
	 *            Data da transação
	 * @param hora
	 *            Hora da transação
	 * @param value
	 *            Valor da trasação
	 * 
	 * @return Verdadeiro para o sucesso no cancelamento.
	 * 
	 * @throws Exception
	 */
	abstract public boolean requestCancel( final String rede, final String nsu, final Date data, final String hora, final BigDecimal value ) throws Exception;

	/**
	 * Faz a leitura do arquivo de resposta.
	 * 
	 * @param header
	 *            Valor do campo header para comparação do arquivo a qual a resposta se refere.
	 * 
	 * @return Verdadeiro para correta leitura do arquivo.
	 * 
	 * @throws Exception
	 */
	abstract public boolean readResponse( final String header ) throws Exception;

	/**
	 * Monta uma lista, com as linhas, a serem impressas no comprovante TEF ( campos 029- ... ) .<br>
	 * 
	 * @return Lista com comprovante TEF.
	 * 
	 * @throws Exception
	 */
	abstract public List<String> getResponseToPrint() throws Exception;

	/**
	 * Executa a requisição de confirmação da impressão do comprovante TEF.
	 * 
	 * @return Verdadeiro para sucesso na confimação.
	 * 
	 * @throws Exception
	 */
	abstract public boolean confirmation() throws Exception;

	/**
	 * Executa a requisição da não confirmação da impressão do comprovante TEF.
	 * 
	 * @return Verdadeiro para sucesso da não confirmação.
	 * 
	 * @throws Exception
	 */
	abstract public boolean noConfirmation() throws Exception;
}
