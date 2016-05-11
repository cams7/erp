package org.freedom.modulos.gms.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.freedom.infra.dao.AbstractDAO;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.gms.business.object.Coleta;
import org.freedom.modulos.gms.business.object.Coleta.PREFS;

public class DAOColeta extends AbstractDAO {

	private Object prefs[] = null;
	
	
	public DAOColeta( DbConnection cn ) {

		super( cn );
		
	}
	
	public void setPrefs(Integer codemp, Integer codfilial) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = null;
		
		prefs = new Object[ Coleta.PREFS.values().length];
		
		try {
			sql = new StringBuilder("select pf1.usarefprod, " );
			sql.append( "coalesce(pf8.codtiporecmerccm,0) codtiporecmerc, " );
			sql.append( "pf8.codplanopag, pf8.codtran, pf8.permitdoccoldupl " );
			sql.append( "from sgprefere1 pf1 left outer join sgprefere8 pf8 " );
			sql.append( "on pf8.codemp=pf1.codemp and pf8.codfilial=pf1.codfilial " );
			sql.append( "where pf1.codemp=? and pf1.codfilial=? " );

			ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp);
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			rs = ps.executeQuery();
			
			if ( rs.next() ) {
				
				prefs[ PREFS.USAREFPROD.ordinal() ] = new Boolean( "S".equals( rs.getString( PREFS.USAREFPROD.toString() ) ) );
				//prefs[ PREFS.CODEMPTR.ordinal() ] = new Integer(rs.getInt( PREFS.CODEMPTR.toString() ));
				//prefs[ PREFS.CODFILIALTR.ordinal() ] = new Integer(rs.getInt( PREFS.CODFILIALTR.toString() ));
				prefs[ PREFS.CODTIPORECMERC.ordinal() ] = new Integer(rs.getInt( PREFS.CODTIPORECMERC.toString() ));
				prefs[ PREFS.CODPLANOPAG.ordinal() ] = new Integer(rs.getInt( PREFS.CODPLANOPAG.toString() ));
				prefs[ PREFS.CODTRAN.ordinal() ] = new Integer(rs.getInt( PREFS.CODTRAN.toString() ));
				prefs[ PREFS.PERMITDOCCOLDUPL.ordinal() ] = rs.getString( PREFS.PERMITDOCCOLDUPL.toString() );

			}
			rs.close();
			ps.close();
			getConn().commit();
		} finally {
			ps = null;
			rs = null;
			sql = null;
		}
	}
	
	public Integer loadCompra(Integer codemp, Integer codfilial, Integer ticket) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = null;
		Integer result = null;
		try {
			sql = new StringBuilder("select codcompra from cpcompra where codemprm=? and codfilialrm=? and ticket=? " );

			ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( 1, codemp );
			ps.setInt( 2, codfilial );
			ps.setInt( 3, ticket );
			rs = ps.executeQuery();
			
			if ( rs.next() ) {
				result = rs.getInt( "codcompra" );
			}
			rs.close();
			ps.close();
			getConn().commit();
		} finally {
			ps = null;
			rs = null;
			sql = null;
		}
		return result;
	}
	
	public Object[] getPrefs() {
		return this.prefs;
	}
	
	public Integer consistDocColeta(Integer codemp, Integer codfilial, Integer ticket, Integer docrecmerc) throws SQLException {
		Integer result = null;
		if ( "N".equals( getPrefs()[PREFS.PERMITDOCCOLDUPL.ordinal()]) ) {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT RM.TICKET FROM EQRECMERC RM WHERE CODEMP=? AND CODFILIAL=? AND TICKET<>? AND DOCRECMERC=?");
			PreparedStatement ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( 1, codemp );
			ps.setInt( 2, codfilial );
			ps.setInt( 3, ticket );
			ps.setInt( 4, docrecmerc );
			
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				result = new Integer(rs.getInt( "TICKET" ) );
			}
			rs.close();
			ps.close();
			getConn().commit();
		}
		return result;
		
	}
	
	public Integer loadCodfor(Integer codemp, Integer codfilial, Integer codcli) throws SQLException {
		Integer result = null;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT CF.CODFOR FROM EQCLIFOR CF WHERE CODEMP=? AND CODFILIAL=? AND CODCLI=?");
		PreparedStatement ps = getConn().prepareStatement( sql.toString() );
		ps.setInt( 1, codemp );
		ps.setInt( 2, codfilial );
		ps.setInt( 3, codcli );
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			result = rs.getInt( "CODFOR" );
		}
		rs.close();
		ps.close();
		getConn().commit();
		return result;
	}

}
