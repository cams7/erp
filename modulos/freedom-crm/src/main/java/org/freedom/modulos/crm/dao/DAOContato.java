package org.freedom.modulos.crm.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.freedom.infra.dao.AbstractDAO;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;


public class DAOContato extends AbstractDAO {
	private Object prefs[] = null;
	public enum CONT_PREFS{ USACTOSEQ, LAYOUTFICHAAVAL };
	
	
	public DAOContato( DbConnection cn) {

		super( cn );
	}
	
	public Integer testaCodPK( String sTabela ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer retorno = new Integer( 0 );

		try {
			ps = getConn().prepareStatement( "SELECT ISEQ FROM SPGERANUM(?,?,?)" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "TKCONTATO" ) );
			ps.setString( 3, "CO" );

			rs = ps.executeQuery();
			rs.next();

			retorno = new Integer( rs.getString( 1 ) );

			rs.close();
			ps.close();

			getConn().commit();

		} catch ( SQLException err ) {
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
		}

		return retorno;

	}

	public StringBuilder getSqlFichaAval() {
		StringBuilder sql= new StringBuilder();
		
		sql.append("select f.razfilial, f.dddfilial, f.fonefilial ");
		sql.append(", f.endfilial, f.numfilial, f.siglauf siglauff ");
		sql.append(", f.bairfilial, f.cnpjfilial,f.emailfilial " );
		sql.append(", m.nomemunic nomemunicf ");
		sql.append(", c.razcto, c.endcto, c.numcto, c.baircto ");
		sql.append(", c.siglauf siglaufc, c.cpfcto, c.dddcto ");
		sql.append(", c.fonecto, c.cnpjcto, c.celcto ");
		sql.append(", c.contcto, mc.nomemunic nomemunicc, c.pessoacto, c.emailcto ");
		sql.append(", fa.seqfichaaval, fa.codmotaval, fa.dtfichaaval ");
		sql.append(", fa.localfichaaval, fa.finalifichaaval, fa.predentrfichaaval, fa.andarfichaaval ");
		sql.append(", fa.cobertfichaaval, fa.estrutfichaaval, fa.ocupadofichaaval, fa.mobilfichaaval, fa.janelafichaaval ");
		sql.append(", fa.sacadafichaaval, fa.outrosfichaaval, fa.obsfichaaval, fa.ocupadofichaaval ");
		sql.append("from sgfilial f ");
		sql.append("left outer join sgmunicipio m on ");
		sql.append("m.codmunic=f.codmunic and m.codpais=f.codpais ");
		sql.append("and m.siglauf=f.siglauf ");
		sql.append("inner join tkcontato c on ");
		sql.append("c.codemp=? and c.codfilial=? and c.codcto=? ");
		sql.append("left outer join sgmunicipio mc on ");
		sql.append("mc.codmunic=c.codmunic and mc.codpais=c.codpais ");
		sql.append("and mc.siglauf=c.siglauf ");
		sql.append("left outer join crfichaaval fa on ");
		sql.append("fa.codemp=? and fa.codfilial=? and fa.seqfichaaval=? ");
		sql.append("and fa.codempco = c.codemp and fa.codfilialco= c.codfilial and fa.codcto=c.codcto ");
		sql.append("left outer join critfichaaval itfa on ");
		sql.append("itfa.codemp = fa.codemp and itfa.codfilial= fa.codfilial and itfa.seqfichaaval= fa.seqfichaaval ");
		sql.append("where f.codemp=? and f.codfilial=? ");
 		
		return sql;
	}
	
	public void setPrefs(Integer codemp, Integer codfilial) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = null;
		
		prefs = new Object[ CONT_PREFS.values().length];
		
		try {
			sql = new StringBuilder("select p.usactoseq, p.layoutfichaaval " );
			sql.append( "from sgprefere3 p "); 
			sql.append( "where  p.codemp=? and p.codfilial=?" );
			
			ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( 1, codemp );
			ps.setInt( 2, codfilial );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				
				prefs[ CONT_PREFS.USACTOSEQ.ordinal() ] = rs.getString( CONT_PREFS.USACTOSEQ.toString() );
				prefs[ CONT_PREFS.LAYOUTFICHAAVAL.ordinal() ] = rs.getString( CONT_PREFS.LAYOUTFICHAAVAL.toString() );
				
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
	
	public Object[] getPrefs() {
		return this.prefs;
	}

}
