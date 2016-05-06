/*
 * Created on 03/05/2005 to Window - Preferences - Java - Code Style - Code Templates
 */

package org.freedom.business.component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.freedom.library.swing.component.JTextFieldPad;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DataPump {

	private Banco banco = null;

	private String url = "jdbc:firebirdsql:localhost/3050:/opt/firebird/dados/clientes/rondobras/elog.fdb";

	private String driver = "org.firebirdsql.jdbc.FBDriver";

	// private String arquivo = "";

	// private String dados = "";

	private Document document = null;

	private Vector<Integer> size = new Vector<Integer>();

	// private Vector values = new Vector();

	private Vector<JTextFieldPad> fields = new Vector<JTextFieldPad>();

	private boolean windows = false;

	protected String extractParenthesis( String field ) {

		int start = 0, end = 0;
		start = field.indexOf( '(' );
		end = field.indexOf( ')' );
		if ( start == -1 && end == -1 ) {
			return "";
		}
		else {
			String result = field.substring( start + 1, end );
			return new Integer( Integer.parseInt( result ) ).toString();
		}
	}

	protected String extractType( String field ) {

		String result = field.substring( 0, field.indexOf( '(' ) );

		return result;
	}

	protected String extractSubType( String field ) {

		String result = null;
		if ( field.indexOf( ')' ) != field.length() - 1 )
			result = field.substring( field.indexOf( ')' ) + 1, field.length() );

		return result;
	}

	protected Integer getDecimal( String field ) {

		int result = 0;

		String decimal = extractSubType( field );
		if ( decimal != null && !decimal.equals( "" ) ) {
			String length = extractParenthesis( decimal );
			if ( !length.equals( "" ) )
				result += Integer.parseInt( extractParenthesis( length ) );
			else {
				if ( decimal.equalsIgnoreCase( "V99" ) )
					result += 2;
			}
		}

		return new Integer( result );
	}

	protected Integer getLength( String field ) {

		int result = 0;

		result += Integer.parseInt( extractParenthesis( field ) );

		return new Integer( result );
	}

	protected String createSQLColumns( Node node, String sql, String tableName ) {

		if ( node.hasChildNodes() ) {
			NodeList children = node.getChildNodes();

			for ( int i = 0; i < children.getLength(); i++ ) {
				Node child = children.item( i );
				if ( child.hasAttributes() ) {
					NamedNodeMap attr = child.getAttributes();

					if ( attr.getNamedItem( "picture" ) == null ) {
						if ( child.hasChildNodes() && attr.getNamedItem( "redefines" ) == null )
							sql = createSQLColumns( child, sql, tableName );
						continue;
					}

					String name = attr.getNamedItem( "name" ).getNodeValue().replace( '-', '_' );
					JTextFieldPad field = null;
					sql += name + " ";

					Integer length = getLength( attr.getNamedItem( "picture" ).getNodeValue() );
					Integer decimal = getDecimal( attr.getNamedItem( "picture" ).getNodeValue() );
					Integer fullLength = new Integer( length.intValue() + decimal.intValue() );

					size.addElement( fullLength );

					if ( attr.getNamedItem( "numeric" ) != null && attr.getNamedItem( "numeric" ).getNodeValue().equalsIgnoreCase( "true" ) ) {
						sql += "NUMERIC(" + length + "," + decimal + ")";
						field = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, length.intValue(), decimal.intValue() );
					}
					else {
						sql += "CHAR(" + length + ")";
						field = new JTextFieldPad( JTextFieldPad.TP_STRING, length.intValue(), 0 );
					}

					field.setNomeCampo( name );
					field.setName( name );
					fields.addElement( field );
					if ( child.getNextSibling() != null )
						sql += ", ";
				}
			}
		}
		else {
			Node child = node;
			if ( child.hasAttributes() ) {
				NamedNodeMap attr = child.getAttributes();

				if ( attr.getNamedItem( "picture" ) == null ) {
					if ( child.hasChildNodes() && attr.getNamedItem( "redefines" ) == null )
						sql = createSQLColumns( child, sql, tableName );
					return sql;
				}

				String name = attr.getNamedItem( "name" ).getNodeValue().replace( '-', '_' );

				sql += name + " ";

				Integer length = new Integer( extractParenthesis( attr.getNamedItem( "picture" ).getNodeValue() ) );

				size.addElement( length );
				JTextFieldPad field = null;

				if ( attr.getNamedItem( "numeric" ) != null && attr.getNamedItem( "numeric" ).getNodeValue().equalsIgnoreCase( "true" ) ) {
					sql += "NUMERIC(" + length + ",0)";
					field = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, length.intValue(), 0 );
				}
				else {
					sql += "CHAR(" + length + ")";
					field = new JTextFieldPad( JTextFieldPad.TP_STRING, length.intValue(), 0 );
				}

				field.setNomeCampo( name );
				field.setName( name );
				fields.addElement( field );
				if ( child.getNextSibling() != null )
					sql += ", ";
			}
		}
		return sql;
	}

	protected String leDados( FileReader leDados, String tableName ) {

		String result = "";
		String command = "INSERT INTO " + tableName + " ";
		String columns = "(";
		String values = "(";

		for ( int i = 0; i < fields.size(); i++ ) {
			int origSize = size.get( i ).intValue();
			char cbuf[] = new char[ origSize ];
			boolean parar = false;
			boolean fim = false;
			try {
				for ( int j = 0; j < origSize; j++ ) {
					int lebuf = leDados.read();
					if ( lebuf == -1 ) {
						parar = true;
						fim = true;
						break;
					}
					cbuf[ j ] = (char) lebuf;
					if ( windows ) {
						if ( cbuf[ j ] == '\r' ) {
							leDados.read();
							cbuf[ j ] = 20;
							parar = true;
							break;
						}
					}
					else {
						if ( cbuf[ j ] == '\n' ) {
							cbuf[ j ] = 20;
							parar = true;
							break;
						}
					}
				}

				String buf = new String( cbuf ).trim();
				buf = buf.replaceAll( "'", "\"" );
				if ( parar && fim && buf.equals( "" ) )
					return null;

				JTextFieldPad field = fields.get( i );

				columns += field.getNomeCampo();
				switch ( field.getTipo() ) {
					case JTextFieldPad.TP_STRING :
						field.setVlrString( buf );
						values += "'" + field.getVlrString() + "'";
						break;
					case JTextFieldPad.TP_DECIMAL :
						if ( field.iDecimal > 0 ) {
							String number = buf.substring( 0, buf.length() - field.iDecimal ) + "." + buf.substring( buf.length() - field.iDecimal, buf.length() );
							field.setVlrBigDecimal( new BigDecimal( Double.parseDouble( number ) ) );
						}
						else
							field.setVlrString( buf );
						values += field.getVlrBigDecimal();
					default :
				}

				if ( values.matches( "\'?+\'" ) )
					return null;

				if ( parar )
					break;

				if ( i < size.size() - 1 ) {
					columns += ", ";
					values += ", ";
				}

			} catch ( IOException e ) {
				break;
			}
		}

		columns += ")";
		values += ")";

		if ( columns.equals( "()" ) && values.equals( "()" ) )
			return null;
		else {
			result = command + columns + " VALUES " + values + ";";
			return result;
		}
	}

	public DataPump( String user, String pass, String arquivo, String dados, boolean windows ) {

		super();
		this.windows = windows;
		doImport( user, pass, arquivo, dados );
	}

	public void doImport( String user, String pass, String arquivo, String dados ) {

		banco = new Banco( url, driver, user, pass );

		if ( banco.getDbConnection() == null ) {
			System.err.println( "Não conectou no banco de dados" );
			System.exit( 2 );
		}

		// this.arquivo = arquivo;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.parse( new File( arquivo ) );

			String filename = document.getDocumentElement().getAttribute( "filename" ).replace( '.', '_' );
			String sql = "";

			if ( document.getDocumentElement().hasChildNodes() ) {

				sql = "CREATE TABLE " + filename + " (";
				String create = createSQLColumns( document.getDocumentElement(), sql, filename ).trim();
				if ( create.charAt( create.length() - 1 ) == ',' )
					create = create.substring( 0, create.length() - 1 );
				sql = create + ");\n";
			}

			/*
			 * try { System.out.println("Tentando dar DROP na tabela"); Statement stmt = banco.getStatement(); stmt.executeUpdate("DROP TABLE " + filename + ";\n"); } catch (SQLException e) { System.err.println("Tabela não encontrada"); }
			 */
			try {
				System.err.println( "Criando a tabela" );
				System.out.println( sql );
				Statement stmt = banco.getStatement();
				stmt.executeUpdate( sql );
			} catch ( SQLException e ) {
				System.err.println( "ERRO CRIANDO A TABLEA\n" );
				// e.printStackTrace();
				// System.exit(-1);
			}

			File arquivoDados = new File( dados );
			FileReader leDados = new FileReader( arquivoDados );

			String insertSQL = "";

			while ( insertSQL != null ) {
				insertSQL = leDados( leDados, filename );
				if ( insertSQL != null ) {
					try {
						System.out.println( insertSQL );
						Statement stmt = banco.getStatement();
						stmt.executeUpdate( insertSQL );
					} catch ( SQLException e ) {
						System.err.println( "ERRO NA INSERÇÃO\n" );
						e.printStackTrace();
						System.exit( -1 );
					}

				}
			}

		} catch ( SAXException sxe ) {
			Exception x = sxe;
			if ( sxe.getException() != null )
				x = sxe.getException();
			x.printStackTrace();

		} catch ( ParserConfigurationException pce ) {
			pce.printStackTrace();

		} catch ( IOException ioe ) {
			ioe.printStackTrace();
		}
	}

	public DataPump( String user, String pass, String arquivo, String dados ) {

		super();
		doImport( user, pass, arquivo, dados );

	}

	public static void main( String[] args ) {

		if ( args.length < 4 ) {
			System.err.println( "Uso: java DataPump arquivo_de_dados arquivo_de_definição usuario senha [/windows | /linux]" );
			System.exit( 1 );
		}

		if ( args.length >= 5 ) {
			// boolean windows = args[4].equalsIgnoreCase("/windows")|| args[4].equalsIgnoreCase("--windows")|| args[4].equalsIgnoreCase("-windows");
			// DataPump dataPump = new DataPump(args[2], args[3], args[1], args[0],windows);
		}
		else {
			// DataPump dataPump = new DataPump(args[2], args[3], args[1], args[0]);
		}
	}

}
