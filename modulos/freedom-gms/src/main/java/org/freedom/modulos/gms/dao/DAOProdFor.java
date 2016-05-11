package org.freedom.modulos.gms.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import org.freedom.infra.dao.AbstractDAO;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.modulos.crm.business.object.ProdFor.EColProdFor;


public class DAOProdFor extends AbstractDAO {

	public DAOProdFor( DbConnection connection ) {
		super( connection );
		
	}
	
	public Vector<Vector<Object>> loadProdFor(Integer codemppd, Integer codfilialpd, Integer codprod,
			Integer codempfr, Integer codfilialfr, Integer codfor, Date dataini, Date datafim ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = null;
		Vector<Vector<Object >> result = new Vector<Vector<Object>>();
		Vector<Object> row = null;
		int param = 1;
		try{
			
		sql = new StringBuilder("Select ");
			sql.append( "p.descprod,  p.codprod, f.razfor, f.codfor " );
			sql.append( "from eqproduto p, cpforneced f, cpcompra c, cpitcompra ic " );
			sql.append( "where p.codemp=? and p.codfilial=? and " );
			if( codprod > 0 ){
				sql.append( "p.codprod=? and " );
			}
			if(codfor > 0){
				sql.append( "c.codempfr=? and c.codfilialfr=? and c.codfor=? and " );
			}
			sql.append( "ic.codemppd=p.codemp and ic.codfilialpd=p.codfilial and ic.codprod=p.codprod and " );
			sql.append( "c.codemp=ic.codemp and c.codfilial=ic.codfilial and " );
			sql.append( "c.codcompra=ic.codcompra and f.codemp=c.codempfr and " );
			sql.append( "c.codemp=ic.codemp and c.codfilial=ic.codfilial and " );
			sql.append( "c.codcompra=ic.codcompra and f.codemp=c.codempfr and " );
			sql.append( "f.codfilial=c.codfilialfr and f.codfor=c.codfor and c.dtemitcompra between ? and ? " );
			sql.append( "and not exists " );
			sql.append( "(select * from cpprodfor pf where pf.codemp=p.codemp  and pf.codfilial=p.codfilial " );
			sql.append( "and pf.codprod=p.codprod and pf.codempfr=f.codemp and pf.codfilialfr=f.codfilial and pf.codfor=f.codfor)" );
			sql.append( "group by p.descprod, p.codprod, f.razfor, f.codfor " );
			sql.append( " order by p.descprod, p.codprod, f.razfor, f.codfor " );
			
			ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( param++, codemppd );
			ps.setInt( param++, codfilialpd );
			if( codprod > 0 ){
				ps.setInt( param++, codprod );
			}		
			if(codfor > 0){
				ps.setInt( param++, codempfr );
				ps.setInt( param++, codfilialfr );
				ps.setInt( param++, codfor );
			}
			ps.setDate( param++, Funcoes.dateToSQLDate( dataini ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( datafim ) );
			rs = ps.executeQuery();
	
			while( rs.next() ){
			
				row = new Vector<Object>();
				
				row.addElement( getString(  rs.getString( EColProdFor.DESCPROD.toString() ) ) );
				row.addElement( new Integer( rs.getInt( EColProdFor.CODPROD.toString() ) ) );
				row.addElement( getString(  rs.getString( EColProdFor.RAZFOR.toString() ) ) );
				row.addElement( new Integer( rs.getInt( EColProdFor.CODFOR.toString() ) ) );
				
				result.addElement( row );
			}
			rs.close();
			ps.close();
			getConn().commit();
			
		} finally {
			ps = null;
			rs = null;
		}
		return result;	
	}
	
	
	
	public void insert( Integer codemppd, Integer codfilialpd, Integer codprod,
			Integer codempfr, Integer codfilialfr, Integer codfor, Date dataini, Date datafim ) throws SQLException {
		
		StringBuilder sql = new StringBuilder();
		int param = 1;

		sql.append( "insert into cpprodfor ( codemp , codfilial, codprod, codempfr, codfilialfr, codfor, refprodfor ) " );
		sql.append( "select p.codemp, p.codfilial, p.codprod ,f.codemp , f.codfilial, f.codfor, p.codfabprod  " );
		sql.append( "from eqproduto p, cpforneced f, cpcompra c, cpitcompra ic where p.codemp=? " );
		sql.append( "and p.codfilial=?  and " );
		if( codprod > 0 ){
			sql.append( "p.codprod=? and " );
		}
		if(codfor > 0){
			sql.append( "c.codempfr=? and c.codfilialfr=? and c.codfor=? and " );
		}
		sql.append( "ic.codemppd=p.codemp and " );
		sql.append( "ic.codfilialpd=p.codfilial and ic.codprod=p.codprod " );
		sql.append( "and c.codemp=ic.codemp and c.codfilial=ic.codfilial and " );
		sql.append( "c.codcompra=ic.codcompra and f.codemp=c.codempfr and " );
		sql.append( "f.codfilial=c.codfilialfr and f.codfor=c.codfor and c.dtemitcompra between ? and ? " );
		sql.append( "and not exists (select * from cpprodfor pf " );
		sql.append( "where pf.codemp=p.codemp and pf.codfilial=p.codfilial " );
		sql.append( "and pf.codprod=p.codprod and pf.codempfr=f.codemp and " );
		sql.append( "pf.codfilialfr=f.codfilial and pf.codfor=f.codfor) " );
				
		PreparedStatement ps = getConn().prepareStatement( sql.toString() );
		ps.setInt( param++, codemppd );
		ps.setInt( param++, codfilialpd );
		if( codprod > 0 ){
			ps.setInt( param++, codprod );
		}		
		if(codfor > 0){
			ps.setInt( param++, codempfr );
			ps.setInt( param++, codfilialfr );
			ps.setInt( param++, codfor );
		}
		ps.setDate( param++, Funcoes.dateToSQLDate( dataini ) );
		ps.setDate( param++, Funcoes.dateToSQLDate( datafim ) );
	
	
	ps.execute();
	ps.close();

	getConn().commit();

	}	
	
}
