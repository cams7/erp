/**
 * @version 16/01/2009 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 *         Pacote: org.freedom.tools <BR>
 *         Classe: @(#)ImportEmail.java <BR>
 * 
 *         Este programa é licenciado de acordo com a GPL (Licença Pública Geral para Programas de Computador), <BR>
 *         versão 2.0. <BR>
 *         A GPL deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste Programa. <BR>
 *         Caso uma cópia da GPL não esteja disponível junto com este Programa, você pode contatar <BR>
 *         sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *         Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *         Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *         de acordo com os termos da GPL <BR>
 * <BR>
 * 
 */

package org.freedom.modulos.crm.business.component;

import java.io.File;
import java.io.IOException;
import org.freedom.infra.model.jdbc.DbConnection;

public class ImportEmail {

	/**
	 * @param args
	 */
	public static void main( String[] args ) {

		byte[] password = new byte[ 20 ];
		if ( ( args == null ) || ( args.length < 3 ) ) {
			System.out.println( "Entre com os parametros URL/JDBC/Freedom, diretorio e extensao de arquivo" );
		}
		else {
			System.out.print( "Entre com a senha para usuário sysdba: " );
			try {
				System.in.read( password );
				importEmail( args[ 0 ], new String( password ).trim(), args[ 1 ], args[ 2 ] );
			} catch ( IOException e ) {
				System.out.println( "Erro lendo a senha\n" + e.getMessage() );
			}
		}
	}

	private static void importEmail( String URLJdbc, String password, String dir, String ext ) {

		String path = null;
		String[] list = null;
		System.out.println( "Executando a conexao..." );
		DbConnection con = getDbConnection( URLJdbc, password );
		if ( con != null ) {
			path = getDirectory( dir );
			if ( path != null ) {
				System.out.println( "Iniciando a importacao em " + path + " ..." );
				list = getListDir( path );
			}

		}
		else {
			System.out.println( "Nao foi possivel estabelecer a conexao com o banco de dados" );
		}
	}

	private static String[] getListDir( String path ) {

		File file = new File( path );
		return file.list();
	}

	private static String getDirectory( String dir ) {

		String pathv = null;
		File file = new File( dir );
		if ( file.exists() ) {
			pathv = file.getPath();
		}
		else {
			System.out.println( "Diretorio nao encontrado" );
		}
		return pathv;
	}

	private static DbConnection getDbConnection( String URLJdbc, String password ) {

		DbConnection con = null;

		try {
			con = new DbConnection( "org.firebirdsql.jdbc.FBDriver", URLJdbc, "sysdba", password );
			con.setAutoCommit( false );
		} catch ( java.sql.SQLException e ) {
			if ( e.getErrorCode() == 335544472 )
				System.out.println( "Nome do usuário ou senha inválidos ! ! !" );
			else
				System.out.println( "Não foi possível estabelecer conexão com o banco de dados.\n" + e.getMessage() );
			e.printStackTrace();
		}
		return con;
	}
}
