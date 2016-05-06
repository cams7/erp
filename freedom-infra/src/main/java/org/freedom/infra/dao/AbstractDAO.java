/**
 * @version 02/08/2011 <BR>
 * @author Setpoint Tecnologia em Informática Ltda./Robson Sanchez <BR>
 *
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.infra.dao <BR>
 * Classe: @(#)AbstractDAO.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA <BR> <BR>
 *
 * Classe base para implementações de métodos de acesso a dados
 */

package org.freedom.infra.dao;

import java.math.BigDecimal;

import org.freedom.infra.model.jdbc.DbConnection;

public abstract class AbstractDAO {
	private DbConnection conn;
	Integer codemp = null; 
	Integer codfilial = null;
	
	public AbstractDAO(DbConnection connection) {
		setConn(connection);
	}
	
	public AbstractDAO(DbConnection connection, Integer codemp, Integer codfilial) {
		this(connection);
		setCodemp(codemp);
		setCodfilial(codfilial);
	}
	
	public DbConnection getConn() {
		return conn;
	}

	public void setConn(DbConnection conn) {
		this.conn = conn;
	}

	public Integer getCodemp() {
		return codemp;
	}

	public void setCodemp(Integer codemp) {
		this.codemp = codemp;
	}

	public Integer getCodfilial() {
		return codfilial;
	}

	public void setCodfilial(Integer codfilial) {
		this.codfilial = codfilial;
	}	
	
	protected String getString( String value ){
		String result = null;

		if (value == null){
			result = "";
		} else {
			result = value;
		}
		return result;
	}	

	protected Integer getInteger( Integer value ) {
		Integer result = null;

		if (value == null){
			result = new Integer( 0 );
		} else {
			result = value;
		}
		return result;
	}

	protected BigDecimal getBigDecimal( BigDecimal value ) {
		BigDecimal result = null;

		if (value == null){
			result = BigDecimal.ZERO;
		} else {
			result = value;
		}
		return result;
	}

}
