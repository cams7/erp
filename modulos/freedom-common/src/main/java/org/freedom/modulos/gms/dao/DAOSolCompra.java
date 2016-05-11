package org.freedom.modulos.gms.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.freedom.infra.dao.AbstractDAO;
import org.freedom.infra.model.jdbc.DbConnection;


public class DAOSolCompra extends AbstractDAO {
	
	Object prefs = null;

	public DAOSolCompra( DbConnection cn ) {

		super( cn );
		
	}

	public Integer setPrefs( Integer codemp, Integer codfilial ) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer anocc = 0;
		StringBuilder sql = null;
		try {
			sql = new StringBuilder();
			sql.append( " SELECT ANOCENTROCUSTO FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=? " );
			
			
			ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( 1, codemp );
			ps.setInt( 2, codfilial );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				anocc = rs.getInt( "ANOCENTROCUSTO" );
			}
			rs.close();
			ps.close();
		} finally {
			rs = null;
			ps = null;
			sql = null;
		}

		return anocc;
	}
	
	public Map<String, Object> getAnocc(boolean bAprovaCab, String codcc, Integer codemp, Integer codfilial, String idusu) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = null;
		String sAprova = null;
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			sql = new StringBuilder("SELECT ");
			sql.append( "ANOCC,CODCC,CODEMPCC,CODFILIALCC,APROVCPSOLICITACAOUSU " );
			sql.append( "FROM SGUSUARIO " );
			sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND IDUsu=? " );
			
			ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( 1, codemp );
			ps.setInt( 2, codfilial );
			ps.setString( 3, idusu );
			rs = ps.executeQuery();
			
			if ( rs.next() ) {
				sAprova = rs.getString( "APROVCPSOLICITACAOUSU" );
				if ( sAprova != null ) {
					if ( !sAprova.equals( "ND" ) ) {
						if ( sAprova.equals( "TD" ) )
							bAprovaCab = true;
						else if ( ( codcc.trim().equals( rs.getString( "CODCC" ).trim() ) ) 
								&& ( codemp == rs.getInt( "CODEMPCC" ) ) && ( codfilial == rs.getInt( "CODFILIALCC" ) ) 
								&& ( sAprova.equals( "CC" ) ) ) {
							bAprovaCab = true;
						}
						else {
							bAprovaCab = false;
						}
					}
				}
				result.put( "anocc", new Integer(rs.getInt( "ANOCC" ) ) );
				result.put( "codcc", getString( rs.getString( "CODCC" ) ) );
				result.put( "baprova", bAprovaCab );
				
			}

			getConn().commit();
		} finally {
			rs = null;
			ps = null;
			sql = null;
		}
		return result;
	}

}
	