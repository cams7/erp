package org.freedom.modulos.crm.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import javax.swing.ImageIcon;

import org.freedom.infra.dao.AbstractDAO;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.modulos.crm.business.object.Campanha.EColCampanha;


public class DAOCampanha extends AbstractDAO {

	public DAOCampanha( DbConnection connection ) {

		super( connection );

	}
	
	public Integer loadCodEmail(String codcamp) throws SQLException {
		StringBuilder sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer codemail = null;
		if(codcamp!= null){
			sql = new StringBuilder();
			sql.append( "select codemail " );
			sql.append( "from tkcampanhaemail " );
			sql.append( "where codcamp=? " );
			
			ps = getConn().prepareStatement( sql.toString() );
			ps.setString( 1, codcamp );
			rs = ps.executeQuery();
	
			if( rs.next() ){
				codemail = new Integer( rs.getInt( "codemail" ) );
			}
		}
		return codemail;
	}

	
	
	public Vector<Vector<Object>> loadContcli( String tipocto, 
			Integer codempca, Integer codfilialca
			, Integer codempco,Integer codfilialco
			, Integer codempcl, Integer codfilialcl
			, String emailvalido
			, Vector<String> vCampFiltroPart, Vector<String> vCampFiltroNPart
			, ImageIcon imagem, String filtraperiodo, Date dtini, Date dtfim
			, Integer codempto, Integer codfilialto, Integer codtipocont
			, Integer codempsr, Integer codfilialsr, Integer codsetor
			, Integer codempoc, Integer codfilialoc, Integer codorigcont
			, Integer codempti, Integer codfilialti, Integer codtipocli
			, Integer codempcc, Integer codfilialcc, Integer codclascli
			, String ordem, String ativo
			)throws SQLException{

		StringBuffer sql = null;
		Vector<Vector<Object>> result = new Vector<Vector<Object>>();
		Vector<Object> row = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int param = 1;

		try {
			sql = new StringBuffer("select co.codemp, co.codfilial, co.tipocto, co.codcto, ");
			sql.append( "co.razcto, co.nomecto, co.contcto, co.emailcto, ");
			sql.append( "co.obscto, co.dtins, co.dtalt, co.dtinscc, co.dtaltcc ");
			sql.append( "from tkcontclivw03 co ");
			sql.append( "where ( ( co.tipocto='O' and co.codemp=? and co.codfilial=? ) or ");
			sql.append( "( co.tipocto='C' and co.codemp=? and co.codfilial=? ) ) and "	);
			sql.append( "co.tipocto in (?,?) " );
			if ( ! "N".equals( filtraperiodo )) {
				if ("C".equals(filtraperiodo)) {
					sql.append( " and co.dtinscc between ? and ? " );
				} else if ("A".equals( filtraperiodo )) {
					sql.append( " and co.dtins between ? and ? " );
				}
			}
			if ( "S".equals( emailvalido ) ) {
				sql.append("and co.emailcto is not null ");
			}
			if (vCampFiltroPart.size()>0) {
				String sIN = Funcoes.vectorToString( vCampFiltroPart, "','" );
				sIN = "'" + sIN + "'";
				sql.append( " and exists (select * from tkcampanhacto cc " );
				sql.append( " where cc.codemp=" );
				sql.append( codempca );
				sql.append( " and cc.codfilial=" );
				sql.append( codfilialca );
				sql.append( " and ( (co.tipocto='O' and cc.codempco=co.codemp and cc.codfilialco=co.codfilial and cc.codcto=co.codcto ) or  " );
				sql.append( " (co.tipocto='C' and cc.codempcl=co.codemp and cc.codfilialcl=co.codfilial and cc.codcli=co.codcto ) )  " );
				sql.append( " and cc.codcamp in (" + sIN + ")) " );				
			}
			if (vCampFiltroNPart.size()>0) {
				String sIN = Funcoes.vectorToString( vCampFiltroNPart, "','" );
				sIN = "'" + sIN + "'";
				sql.append( " and not exists (select * from tkcampanhacto cc " );
				sql.append( " where cc.codemp=" );
				sql.append( codempca );
				sql.append( " and cc.codfilial=" );
				sql.append( codfilialca );
				sql.append( " and ( (co.tipocto='O' and cc.codempco=co.codemp and cc.codfilialco=co.codfilial and cc.codcto=co.codcto ) or  " );
				sql.append( " (co.tipocto='C' and cc.codempcl=co.codemp and cc.codfilialcl=co.codfilial and cc.codcli=co.codcto ) )  " );
				sql.append( " and cc.codcamp in (" + sIN + ")) " );				
			}
			if (codtipocont.intValue()!=0) {
				sql.append( " and codempto=? and codfilialto=? and codtipocont=? ");				
			}
			if (codsetor.intValue()!=0) {
				sql.append( " and codempsr=? and codfilialsr=? and codsetor=? ");				
			}
			if (codorigcont.intValue()!=0) {
				sql.append( " and codempoc=? and codfilialoc=? and codorigcont=? ");				
			}
			if (codtipocli.intValue()!=0) {
				sql.append( " and codempti=? and codfilialti=? and codtipocli=? ");				
			}
			if (codclascli.intValue()!=0) {
				sql.append( " and codempcc=? and codfilialcc=? and codclascli=? ");				
			}
			sql.append( " and ativo in (?,?) ");				

		//	sql.append(" order by co.razcto, co.nomecto, co.contcto, co.emailcto");
			sql.append(" order by ");
			sql.append( ordem );
			ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( param++, codempco );
			ps.setInt( param++, codfilialco );
			ps.setInt( param++, codempcl );
			ps.setInt( param++, codfilialcl );
			if ("A".equals(tipocto)) {
				ps.setString( param++, "O" );
				ps.setString( param++, "C" );
			} else {
				ps.setString( param++, tipocto );
				ps.setString( param++, tipocto );
			}
			if ( ! "N".equals( filtraperiodo )) {
				ps.setDate( param++, Funcoes.dateToSQLDate( dtini ) );
				ps.setDate( param++, Funcoes.dateToSQLDate( dtfim ) );
			}
			if (codtipocont.intValue()!=0) {
				ps.setInt( param++, codempto );
				ps.setInt( param++, codfilialto );
				ps.setInt( param++, codtipocont );
			}
			if (codsetor.intValue()!=0) {
				ps.setInt( param++, codempsr );
				ps.setInt( param++, codfilialsr );
				ps.setInt( param++, codsetor );
			}
			if (codorigcont.intValue()!=0) {
				ps.setInt( param++, codempoc );
				ps.setInt( param++, codfilialoc );
				ps.setInt( param++, codorigcont );
			}
			if (codtipocli.intValue()!=0) {
				ps.setInt( param++, codempti );
				ps.setInt( param++, codfilialti );
				ps.setInt( param++, codtipocli );
			}
			if (codclascli.intValue()!=0) {
				ps.setInt( param++, codempcc );
				ps.setInt( param++, codfilialcc );
				ps.setInt( param++, codclascli );
			}
			if ( "S".equalsIgnoreCase( ativo ) ) {
				ps.setString( param++, "S" );
				ps.setString( param++, "S" );
			} else if ( "N".equalsIgnoreCase( ativo ) ) {
				ps.setString( param++, "N" );
				ps.setString( param++, "N" );
			} else {
				ps.setString( param++, "S" );
				ps.setString( param++, "N" );
			}

			rs = ps.executeQuery();
			while( rs.next() ){
				if ( !"S".equals( emailvalido ) || Funcoes.validaEmail( getString( rs.getString( EColCampanha.EMAILCTO.toString() ) ) ) ) {
					row = new Vector<Object>();
					row.addElement( new Boolean( false ) );
					row.addElement( imagem );
					row.addElement( "" );
					row.addElement( new Integer( rs.getInt( EColCampanha.CODEMP.toString() ) ) );
					row.addElement( new Integer( rs.getInt( EColCampanha.CODFILIAL.toString() ) ) );
					row.addElement( getString( rs.getString( EColCampanha.TIPOCTO.toString() ) ) );
					row.addElement( new Integer( rs.getInt( EColCampanha.CODCTO.toString() ) ) );
					row.addElement( getString( rs.getString( EColCampanha.RAZCTO.toString() ) ) );
					row.addElement( getString( rs.getString( EColCampanha.NOMECTO.toString() ) ) );
					row.addElement(	getString( rs.getString( EColCampanha.EMAILCTO.toString() ) ) );
					row.addElement( getString( rs.getString( EColCampanha.CONTCTO.toString() ) ) );
					row.addElement( getString( rs.getString( EColCampanha.OBSCTO.toString() ) ) );
					row.addElement( getString( Funcoes.dateToStrDate( rs.getDate( EColCampanha.DTINS.toString() ) ) ) );
					row.addElement( getString( Funcoes.dateToStrDate( rs.getDate( EColCampanha.DTALT.toString() ) ) ) );
					row.addElement( getString( Funcoes.dateToStrDate( rs.getDate( EColCampanha.DTINSCC.toString() ) ) ) );
					row.addElement( getString( Funcoes.dateToStrDate( rs.getDate( EColCampanha.DTALTCC.toString() ) ) ) );
					result.addElement( row );
					
				}

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
	
	public void efetivarCampanha( String tipocto, 
			Integer codempca, Integer codfilialca, String codcamp,
			int codempcto, int codfilialcto, int codcto,
			Integer codempat, Integer codfilialat, Integer codativ,
			String tipo,  String deschist ) throws SQLException {

		PreparedStatement ps = null;
		String sithist = "EF";
		int param = 1;

		try {

			if ( "TE".equals( tipo ) )
				sithist = "RJ";

			ps = getConn().prepareStatement( "EXECUTE PROCEDURE TKGERACAMPANHACTO(?,?,?,?,?,?,?,?,?,?,?,?)" );
			ps.setString( param++, tipocto );
			ps.setInt( param++, codempca );
			ps.setInt( param++, codfilialca );
			ps.setString( param++, codcamp );

			ps.setInt( param++, codempcto );
			ps.setInt( param++, codfilialcto );
			ps.setInt( param++, codcto );

			ps.setInt( param++, codempat );
			ps.setInt( param++, codfilialat );
			ps.setInt( param++, codativ );

			ps.setString( param++, sithist );
			ps.setString( param++, deschist );

			ps.execute();
			ps.close();

			getConn().commit();

		} catch ( Exception e ) {
			e.printStackTrace();
			getConn().rollback();
			throw new SQLException(e);
		}
	}
	
}
