package org.freedom.infra.util.db;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExtractDescription {

	/**
	 * @param args
	 */
    private static int ARG_JDBC_DRIVER = 0;
    private static int ARG_URL_DATABASE = 1;
    private static int ARG_EXPORT_FILE = 2;
    private static int ARG_USER = 3;
    private static int ARG_PASSWD = 4;
    private String exportfile = null;
    private Connection connection = null;
    private int result_java = 0;
    public static void main(String[] args) {

    	System.out.println(java.nio.charset.Charset.defaultCharset().name());
    	System.out.println(System.getProperty("file.encoding"));
    	
		if (args==null || args.length<=ARG_PASSWD) {
			System.out.println("Uso: java org.freedom.infra.util.db.ExtractDescription [jdbc_driver] [url_database] [export_file] [user_database] [passwd_database]");
		} else {
			System.out.println("JDBC_DRIVER: " + args[ARG_JDBC_DRIVER]);
			System.out.println("URL_DATABASE: " + args[ARG_URL_DATABASE]);
			System.out.println("EXPORT_FILE: " + args[ARG_EXPORT_FILE]);
			ExtractDescription extract = new ExtractDescription(args[ARG_JDBC_DRIVER], args[ARG_URL_DATABASE], args[ARG_EXPORT_FILE], args[ARG_USER], args[ARG_PASSWD]);
            extract.export();
		}
	}

	public ExtractDescription(String jdbc_driver, String url_database, String exportfile, String user_database, String passwd_database) {
        setExportfile(exportfile);
		try {
			Class.forName(jdbc_driver);
			setConnection(DriverManager.getConnection(url_database, user_database, passwd_database));
		} catch (ClassNotFoundException e) {
			result_java = 1;
			System.out.println("Erro carregando driver JDBC.\n" + e.getMessage());
		} catch (SQLException e) {
			result_java = 1;
			System.out.println("Erro conectando ao banco de dados.\n" + e.getMessage());
		}
	    
	}
	
	private void execQuery(final List<Entitie> entities, final String tablerelation, final String[] relationfields ) throws SQLException {
		StringBuffer sql = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String[] sourcefields = null;
		System.out.println("Pesquisando na tablea " + tablerelation );
        sql.append("SELECT ");
        for (int i=0; i<relationfields.length; i++) {
        	sql.append(relationfields[i]);
    		sql.append(", ");
        }
        sql.append("RDB$DESCRIPTION FROM ");
        sql.append(tablerelation);
        sql.append(" WHERE RDB$DESCRIPTION IS NOT NULL ORDER BY ");
        for (int i=0; i<relationfields.length; i++) {
            if (i>0) {
          		sql.append(", ");
            }
        	sql.append(relationfields[i]);
        }
    	ps = connection.prepareStatement(sql.toString());
    	rs = ps.executeQuery();
    	while (rs.next()) {
			sourcefields = new String[relationfields.length];
			for (int i=0; i<relationfields.length; i++) {
				sourcefields[i] = rs.getString(relationfields[i]);
			}
			entities.add(new Entitie(tablerelation, relationfields, 
					sourcefields, "RDB$DESCRIPTION", clearDescription( rs.getString("RDB$DESCRIPTION") )));
    	}
    	rs.close();
    	ps.close();
	}
	
	private String clearDescription(final String description) {
	   String result = new String(description.getBytes()); //, ISO8859_1);
	   //(description;
	   if (description.indexOf("'")>-1) {
//		   System.out.println(description);
		   result = new String(description.replaceAll("'", "\"").getBytes());//, ISO8859_1);
//		   System.out.println(result);
	   }
	   return result;
	}
	
	public void export() {
		final List<Entitie> entities = new ArrayList<Entitie>();
		File file = null;
		FileWriter wfile = null;
		BufferedWriter bwfile = null;
        StringBuffer buffer = new StringBuffer();			
        String[] relationfields = null;
		try {
			relationfields = new String[1];
			relationfields[0] = "RDB$RELATION_NAME";
			execQuery(entities, "RDB$RELATIONS", relationfields);
			
			relationfields = new String[2];
			relationfields[0] = "RDB$RELATION_NAME";
			relationfields[1] = "RDB$FIELD_NAME";
			execQuery(entities, "RDB$RELATION_FIELDS", relationfields);

			relationfields = new String[1];
			relationfields[0] = "RDB$PROCEDURE_NAME";
			execQuery(entities, "RDB$PROCEDURES", relationfields);
			
			relationfields = new String[2];
			relationfields[0] = "RDB$PROCEDURE_NAME";
			relationfields[1] = "RDB$PARAMETER_NAME";
			execQuery(entities, "RDB$PROCEDURE_PARAMETERS", relationfields); 
			
		} catch (SQLException e) {
			result_java = 1;
			System.out.println("Erro executando consulta.\n" + e.getMessage());
			return;
		}
		if ( entities.size() >0 ) {
			try {
				file = new File(getExportfile());
				if (file.exists()) {
					file.delete();
				}
				wfile = new FileWriter(file);
				bwfile = new BufferedWriter(wfile);
				for (Entitie entitie:entities) {
					if (buffer.length()>0) {
						buffer.delete(0, buffer.length());
					}
					buffer.append("UPDATE ");
					buffer.append(entitie.getTablerelation());
					buffer.append(" SET ");
					buffer.append(entitie.getFielddescription());
					buffer.append("='");
					buffer.append(entitie.getDescription());
					buffer.append("' WHERE ");
					for (int i=0; i<entitie.getFieldrelation().length; i++) {
						if (i>0) {
							buffer.append(" AND ");
						}
						buffer.append(entitie.getFieldrelation()[i]);
						buffer.append("='");
						buffer.append(entitie.getFieldsource()[i].trim());
						buffer.append("'");
					}
					buffer.append(";");
					//System.out.println(buffer.toString());
					//new String(description.getBytes(), Charset.forName("ISO-8859-1"));
					//bwfile.write(new String(buffer.toString().getBytes(), Charset.forName("ISO-8859-1") ) );
					//bwfile.write(new String(buffer.toString().getBytes(), ISO8859_1));
					bwfile.write(buffer.toString());
					bwfile.write("\n");
					//break;
				}
				bwfile.write("COMMIT WORK;\n");
				bwfile.flush();
				bwfile.close();
			} catch (IOException e) {
				result_java = 0;
                System.out.println("Erro criando arquivo.\n" + e.getMessage());
                return;
			}
		}
		if (result_java==0) {
			System.out.println("Fim da exportação");
		} else {
			System.out.println("Fim da exportação com erros");
		}
		System.exit(result_java); // Saída para o OS
	}

	public void setExportfile(String exportfile) {
		this.exportfile = exportfile;
	}

	public String getExportfile() {
		return exportfile;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
		try {
			this.connection.setAutoCommit(true);
		} catch (SQLException e) {
			System.out.println("Erro configurando auto commit.\n" + e.getMessage());
		}
	}

	public Connection getConnection() {
		return connection;
	}
	
	class Entitie {
		private String tablerelation;
		private String[] fieldrelation;
		private String[] fieldsource;
		private String fielddescription;
		private String description;		
		
		public Entitie( String tablerelation, String[] fieldrelation, String[] fieldsource, String fielddescription, String description) {
			setTablerelation(tablerelation);
			setFieldrelation(fieldrelation);
			setFieldsource(fieldsource);
			setFielddescription(fielddescription);
			setDescription(description);
		}
		
		public void setFieldrelation(String[] fieldrelation) {
			this.fieldrelation = fieldrelation;
		}
		public String[] getFieldrelation() {
			return fieldrelation;
		}
		public void setTablerelation(String tablerelation) {
			this.tablerelation = tablerelation;
		}
		public String getTablerelation() {
			return tablerelation;
		}
		public void setFieldsource(String[] fieldsource) {
			this.fieldsource = fieldsource;
		}
		public String[] getFieldsource() {
			return this.fieldsource;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getDescription() {
			return description;
		}

		public void setFielddescription(String fielddescription) {
			this.fielddescription = fielddescription;
		}

		public String getFielddescription() {
			return fielddescription;
		}
	}
}
