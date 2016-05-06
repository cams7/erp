package org.freedom.infra.util.ini;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Projeto: <a
 * href="http://sourceforge.net/projects/freedom-erp/">Freedom-infra</a> <br>
 * Este programa é licenciado de acordo com a LPG-PC <br>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada
 * pela Fundação do Software Livre (FSF); <BR>
 * <br>
 * 
 * Gerenciador para fluxo em arquivo de parametros.<br>
 * 
 * Esta classe gerencia o fluxo de parametros em arquivo, lendo o arquivo e
 * organizando os parametros em sessões, conforme descrito no próprio arquivo.<br>
 * A sessão deve ser disposta da seguinte maneira: <br>
 * <br>
 * <blockquote> [nome da sessão]<br>
 * parametro = valor do parametro<br>
 * ...<br>
 * [outra sessão]<br>
 * parametro = valor do parametro<br>
 * ...<br>
 * </blockquote> <br>
 * Desta forma o gerenciador mantera um mapa das sessões, e o acesso a estas se
 * da através dos metodos {@link #getSession(String)} e
 * {@link #setSession(String, Properties)}.<br>
 * A atualização do conteudo do arquivo é efetuada pelo metodo
 * {@link #postProperties()}, reescreve as sessões e seus parametros, também as
 * sessões incluidas posteriormente a leitura do arquivo, atualizando o estado
 * dos paramentros. <br>
 * 
 * @see java.util.Map
 * @see java.util.Properties
 * 
 * @author Alex Rodrigues
 * @version 0.0.2 – 07/07/2008
 * 
 * @since 04/07/2008
 */
public class ManagerIni {

	/**
	 * Constante padrão, para nome de parametro a ser resgatado com
	 * java.lang.System.getProperty(String, String), parametro este que indicara
	 * o arquivo de parametros para leitura.
	 */
	public static final String FILE_INIT_DEFAULT = "INITFILE";

	/**
	 * Nome padrão para sessão, caso o arquivo não possua uma sessão definida.
	 */
	private static final String PROPERTIES_DEFAULT = "PROPERTIES_DEFAULT";

	/**
	 * Mapa das sessões.
	 */
	private Map<String, Properties> sessions = new HashMap<String, Properties>();

	/**
	 * Arquivo de parametros.
	 */
	private File file;

	private Properties properties;

	/**
	 * @return Um novo gerenciador a partir do nome do parametro padrão.
	 * @throws IOException
	 * 
	 * @since 04/07/2008
	 */
	public static ManagerIni createManagerIniParameter() throws IOException {
		return new ManagerIni(new File(System.getProperty(FILE_INIT_DEFAULT, "")));
	}

	/**
	 * @param initFileName
	 *            Nome do parametro
	 * @return Um novo gerenciador a partir do nome do parametro informado.
	 * @throws IOException
	 * 
	 * @since 04/07/2008
	 */
	public static ManagerIni createManagerIniParameter(String initFileName) throws IOException {
		return new ManagerIni(new File(System.getProperty(initFileName, "")));
	}

	/**
	 * @param file
	 *            Arquivo de parametros
	 * @return Um novo gerenciador a partir do arquivo informado.
	 * @throws IOException
	 * 
	 * @since 04/07/2008
	 */
	public static ManagerIni createManagerIniFile(File file) throws IOException {
		return new ManagerIni(file);
	}

	/**
	 * @param fileName
	 *            Nome do arquivo de parametros
	 * @return Um novo gerenciador a partir do arquivo criado com o nome
	 *         informado.
	 * @throws IOException
	 * 
	 * @since 04/07/2008
	 */
	public static ManagerIni createManagerIniFile(String fileName) throws IOException {
		return new ManagerIni(new File(fileName));
	}

	/**
	 * Contrutor que recebe arquivo de parametros, verifica se a referência está
	 * nula ou se arquivo é inexistente, do contrário executa a leitura do
	 * arquivo e criação do mapa de sessões.
	 * 
	 * @see #readFile()
	 * 
	 * @param initFile
	 *            Arquivo de parametros.
	 * 
	 * @throws IOException
	 *             Repassada por readFile()
	 * 
	 * @since 04/07/2008
	 */
	private ManagerIni(File initFile) throws IOException {

		if (initFile != null && initFile.exists()) {
			file = initFile;
			readFile();
		}
	}

	/**
	 * Executa a leitura do arquivo de parametros, a criação das sessões e
	 * montagem do mapa.<br>
	 * <blockquote> Quanto a leitura, ela é feita linha a linha, no entanto são
	 * ignoradas as linhas identificadas como linhas de comentários.<br>
	 * A linha de comentário é identificada das seguintes formas:<br>
	 * # Com o caracter # no íncio da linha. Ou <br>
	 * // Com dois caracters / no ínicio da linha. </blockquote>
	 * 
	 * @see java.io.FileReader
	 * @see java.io.BufferedReader
	 * 
	 * @throws IOException
	 *             Esta pode provir das classes FileReader ou BufferedReader,
	 *             utilizadas para a leitura do arquivo.
	 * 
	 * @since 04/07/2008
	 */
	private void readFile() throws IOException {

		if (file == null || !file.exists()) {
			return;
		}

		FileReader reader = new FileReader(file);
		BufferedReader buffered = new BufferedReader(reader);

		sessions = new HashMap<String, Properties>();
		properties = null;

		if (buffered != null) {

			String line = "";
			String name = null;
			String value = null;
			int ivl = -1;

			while (( line = buffered.readLine() ) != null) {

				line = line.trim();

				if (line.length() == 0) {
					continue;
				}
				else if ('#' == line.charAt(0)) {
					continue;
				}
				else if (line.length() > 1 && ( '/' == line.charAt(0) && '/' == line.charAt(1) )) {
					continue;
				}
				else if (ckeckNewSession(line)) {
					continue;
				}

				if (properties == null) {
					properties = new Properties();
					sessions.put(PROPERTIES_DEFAULT, properties);
				}

				ivl = line.indexOf('=');

				if (ivl > -1) {
					name = line.substring(0, ivl);
					value = line.substring(ivl + 1);
					properties.put(name, value);
				}
			}
		}
	}

	/**
	 * Re-escreve o arquivo de parametros.<br>
	 * <blockquote> O arquivo será completamento re-escrito com base no estado
	 * atual das sessões, incluindo sessões e parametros adicionáis e eliminando
	 * os parametros ou sessões retiradas de suas sessões ou mapa
	 * respectivamente. e o estado antigo do arquivo será perdido. </blockquote>
	 * 
	 * @see java.io.PrintWriter
	 * 
	 * @throws IOException
	 * 
	 * @since 04/07/2008
	 */
	public void postProperties() throws IOException {

		PrintWriter printWriter = new PrintWriter(file);

		if (printWriter != null) {

			Properties p;
			Object[] pk;

			for (String nameSession : sessions.keySet()) {

				p = sessions.get(nameSession);
				if (p.size() > 0) {

					printWriter.println("[" + nameSession + "]");
					pk = p.keySet().toArray();
					Arrays.sort(pk);

					for (Object k : pk) {
						printWriter.println(k + "=" + p.getProperty(( String ) k));
					}

					printWriter.println();
				}
			}

			if (sessions.size() > 0) {
				printWriter.flush();
				printWriter.close();
			}
		}
	}

	/**
	 * Verifica o texto da linha informada, para identificação de nome de
	 * sessão. <blockquote> Esta identificação se da através dos caracters [ e
	 * ], a string encontrada entre estes dois caracteres será identificada como
	 * abertura de uma nova sessão. </blockquote>
	 * 
	 * @param line
	 *            String contendo a linha a ser analisada.
	 * @return Verdadeiro para identificação de um nome de sessão e Falso para a
	 *         não identificação.
	 * 
	 * @since 04/07/2008
	 */
	private boolean ckeckNewSession(String line) {

		int ik1 = line.indexOf('[');
		int ik2 = line.indexOf(']');

		if (ik1 > -1 && ik2 > -1 && ik2 > ik1 + 1) {

			properties = new Properties();
			sessions.put(line.substring(ik1 + 1, ik2), properties);

			return true;
		}

		return false;
	}

	public Properties getSession(String session) {
		return sessions.get(session);
	}

	public void setSession(String sessionName, Properties session) {
		sessions.put(sessionName, session);
	}

	/**
	 * Retorna o valor do parametro da sessão, ambos informados por parametro.
	 * 
	 * @param session
	 *            Nome da sessão de parametros
	 * @param key
	 *            Nome do parametro
	 * @return Valor do parametro
	 * 
	 * @since 04/07/2008
	 */
	public String getProperty(String session, String key) {

		String value = null;

		Properties p = sessions.get(session);
		if (p != null && key != null) {
			value = p.getProperty(key);
		}

		return value;
	}

	/**
	 * Define um novo valor para o parametro da sessão. <blockquote> Caso a
	 * referência ao parametro seja igual a <code>null</code> ou se a sessão não
	 * for encontrada no mapa, o valor não será atribuido ao parametro.
	 * </blockquote>
	 * 
	 * @see java.util.Map#get(Object)
	 * @see java.util.Properties#setProperty(String, String)
	 * 
	 * @param session
	 *            Nome da sessão
	 * @param key
	 *            Nome do parametro
	 * @param value
	 *            Novo valor para o parametro
	 * 
	 * @since 04/07/2008
	 */
	public void setProperty(String session, String key, String value) {

		Properties p = sessions.get(session);
		if (p != null && key != null && value != null) {
			p.setProperty(key, value);
		}
	}
}
