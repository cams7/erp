package org.freedom.infra.model.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Classe de conexão com o banco de dados<BR>
 * Projeto: freedom-infra <BR>
 * Pacote: org.freedom.infra.db <BR>
 * Classe: @(#)DbConnection.java <BR>
 * <BR>
 * Este programa é licenciado de acordo com a LGPL (Lesser General Public
 * License), <BR>
 * versão 2.1, Fevereiro de 1999 <BR>
 * A LGPL deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste
 * Programa. <BR>
 * Caso uma cópia da LGPL não esteja disponível junto com este Programa, você
 * pode contatar <BR>
 * o LICENCIADOR ou então pegar uma cópia em: <a
 * href=http://creativecommons.org/licenses/LGPL/2.1/legalcode.pt> Creative
 * Commons</a> <BR>
 * Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa é
 * preciso estar de acordo com os termos da LGPL. <BR>
 * <BR>
 * 
 * @author Robson Sanchez/Setpoint Informática Ltda. <BR>
 *         criada: 21/11/2006. <BR>
 * <BR>
 */

public class DbConnection {

	/** Log da classe. **/
	private static final Logger LOGGER = createLogger();

	private Connection conn;
	private boolean connected = false;
	private boolean transaction = false;
	private String userid;
	private String password;
	private String driver;
	private String urldb;
	private String schema;
	private Properties properties = new Properties();
	

	/**
	 * Cria uma instância do log4j da classe.
	 * 
	 * @return Retorna a instância do log da classe.
	 */
	private static Logger createLogger() {
		return Logger.getLogger("org.freedom.infra.db.DbConnection");
	}

	public DbConnection(String drv, String url, String usrid, String pwd) throws SQLException {
		executeConnection(drv, url, usrid, pwd);
	}

	public DbConnection(String url, Properties props) throws SQLException {
		conn = DriverManager.getConnection(url, props);
		setProperties(props);
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		
		PreparedStatement stmt = null;
		if (conn != null) {
			try {
				stmt = conn.prepareStatement(sql);
				setTransaction(true);
			}
			catch (SQLException e) {
				LOGGER.error(e);
				throw e;
			}
		}
		return stmt;
	}

	public Statement createStatement() throws SQLException {
		return conn.createStatement();
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		conn.setAutoCommit(autoCommit);
	}

	public void rollback() throws SQLException {
		setTransaction(false);
		if (!conn.getAutoCommit()) {
			conn.rollback();
		}
	}

	public ResultSet executeQuery(PreparedStatement stmt) {
		ResultSet rs = null;
		if (( conn != null ) && ( stmt != null )) {
			try {
				rs = stmt.executeQuery();
				setTransaction(true);
			}
			catch (SQLException e) {
				LOGGER.error(e);
			}
		}
		return rs;
	}

	// Setters
	public void setUserid(String usrid) {
		this.userid = usrid;
	}

	public void setPassword(String pwd) {
		this.password = pwd;
	}

	public void setUrldb(String url) {
		this.urldb = url;
	}

	public void setDriver(String drv) {
		this.driver = drv;
	}

	// Getters
	public String getDriver() {
		return driver;
	}

	public String getPassword() {
		return password;
	}

	public String getUrldb() {
		return urldb;
	}

	public String getUserid() {
		return userid;
	}

	public boolean executeConnection(String drv, String url, String usrid, String pwd) {
		boolean ret = true;
		setDriver(drv);
		setUrldb(url);
		setUserid(usrid);
		setPassword(pwd);
		try {
			Class.forName(drv);
			conn = DriverManager.getConnection(url, usrid, pwd);
		}
		catch (Exception e) {
			LOGGER.error(e);
			ret = false;

		}
		connected = ret;
		return ret;
	}

	public Connection getConnection() {
		return conn;
	}

	public boolean isConnected() {
		return connected;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public void commit() throws SQLException {
		setTransaction(false);
		if (!conn.getAutoCommit()) {
			conn.commit();
		}
		
	}

	public boolean isClosed() throws SQLException {
		return conn.isClosed();
	}

	public void close() throws SQLException {
		conn.close();
	}

	public synchronized boolean isTransaction() {
		return transaction;
	}

	private synchronized void setTransaction(boolean transaction) {
		this.transaction = transaction;
	}

}
