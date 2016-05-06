package org.freedom.modulos.gms.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.ImageIcon;

import org.freedom.infra.dao.AbstractDAO;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.gms.business.object.GestaoSol;
import org.freedom.modulos.gms.business.object.GestaoSol.GRID_SOL;
import org.freedom.modulos.gms.business.object.GestaoSol.SelectedSol;


public class DAOGestaoSol extends AbstractDAO {

	public DAOGestaoSol( DbConnection cn ) {
		super(cn);		
	}
	public Vector<Vector<Object>> loadSolicitacao(ImageIcon imgColuna, ImageIcon imgAprovada, Integer codprod, Date dataini, 
			Date datafim, Integer codempam, Integer codfilialam, Integer codalmox, Integer codempcc, Integer codfilialcc, 
			Integer anocc, String codcc, Integer codempuu, Integer codfilialuu, String idusu,
			String sitItem, String sitCompra, String sitSol) throws SQLException{
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Vector<Vector<Object >> result = new Vector<Vector<Object>>();
		Vector<Object> row = null;
		StringBuilder sql = null;
		StringBuilder where = new StringBuilder();
		
		boolean usuario = ( !idusu.trim().equals( "" ) );
		boolean almoxarifado = false;
		boolean CC = ( !codcc.trim().equals( "" ) );
		
		// Define Situaçao do Item.
		if( "AT".equals( sitItem )){
			where.append( " AND IT.SitAprovItSol ='AT'  " );
		} else if( "AP".equals( sitItem )){
			where.append( " AND IT.SitAprovItSol ='AP' ");
		} else if( "PE".equals( sitItem )){
			where.append( " AND IT.SitAprovItSol ='PE' ");
		} else if( "NA".equals( sitItem )){
			where.append( " AND IT.SitAprovItSol ='NA' ");
		}
				
		//Define Situação da Compra.
		if( "PE".equals( sitCompra )){
			where.append( " AND IT.SitCompItSol ='PE' " );
		} else if( "CP".equals( sitCompra )){
			where.append( " AND IT.SitCompItSol ='CP' ");
		} else if( "CT".equals( sitCompra )){
			where.append( " AND IT.SitCompItSol ='CT' ");
		}
		
		//Define Situação da Solicitação.
		if( "PE".equals( sitSol )){
			where.append( " AND (IT.SITITSOL ='PE' ) " );
		} else if( "FN".equals( sitSol )){
			where.append( " AND (IT.SITITSOL ='FN') ");
		} else if( "CA".equals( sitSol )){
			where.append( " AND (IT.SITITSOL ='CA') ");
		} else if( "EA".equals( sitSol )){
			where.append( " AND (IT.SITITSOL ='EA') ");		
		}
			
		if ( codprod > 0 )
			where.append(" AND IT.CODPROD = '" + codprod + "' ");

		if ( almoxarifado )
			where.append(" AND IT.CODALMOX=? AND IT.CODEMPAM=? AND IT.CODFILIALAM=? ");

		if ( CC )
			where.append( " AND O.ANOCC=? AND O.CODCC=? AND O.CODEMPCC=? AND O.CODFILIALCC=? " );

		if ( usuario )
			where.append(" AND (O.CODEMPUU=? AND O.CODFILIALUU=? AND O.IDUSU=?) " );
		
		try{
			sql = new StringBuilder();
			sql.append( "SELECT IT.SITITSOL, IT.CODPROD,IT.REFPROD, PD.DESCPROD, " );
			sql.append( "IT.QTDITSOL, IT.QTDAPROVITSOL, IT.CODSOL, IT.CODITSOL, PD.SLDPROD, O.CODCC, IT.IDUSUITSOL, IT.IDUSUAPROVITSOL " );
			sql.append( "FROM CPSOLICITACAO O, CPITSOLICITACAO IT, EQPRODUTO PD " );
			sql.append( "WHERE O.CODEMP=IT.CODEMP AND O.CODFILIAL=IT.CODFILIAL AND O.CODSOL=IT.CODSOL " );
			sql.append( "AND PD.CODEMP=IT.CODEMP AND PD.CODFILIAL=IT.CODFILIAL AND PD.CODPROD=IT.CODPROD " );
			sql.append( "AND ((IT.DTAPROVITSOL BETWEEN ? AND ?) OR  (O.DTEMITSOL BETWEEN ? AND ?)) " );
			sql.append( where );
			//sql.append( "GROUP BY IT.CODPROD,IT.REFPROD, PD.DESCPROD, PD.SLDPROD " );
			sql.append( "ORDER BY PD.DESCPROD, IT.CODPROD, IT.REFPROD, IT.CODSOL, IT.CODITSOL, PD.SLDPROD " );
			
			ps = getConn().prepareStatement( sql.toString() );
			System.out.println(sql.toString());
			
			int param = 1;
			ps.setDate( param++, Funcoes.dateToSQLDate( dataini ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( datafim ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( dataini ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( datafim ) );
		
			if ( almoxarifado ) {
				ps.setInt( param++, codalmox );
				ps.setInt( param++, codempam );
				ps.setInt( param++, codfilialam );
			}

			if ( CC ) {
				ps.setInt( param++, anocc );
				ps.setString( param++, codcc );
				ps.setInt( param++, codempcc );
				ps.setInt( param++, codfilialcc );
			}

			if ( usuario ) {
				ps.setInt( param++, codempuu );
				ps.setInt( param++, codfilialuu );
				ps.setString( param++, idusu );
			}
			rs = ps.executeQuery();
			
			while( rs.next() ){
				imgColuna = imgAprovada;
				
				row = new Vector<Object>();
				row.addElement( imgColuna );
				row.addElement( getString( rs.getString( GRID_SOL.SITITSOL.toString() ) ));
				row.addElement( new Boolean(false) );
				row.addElement( new Integer(rs.getInt( GRID_SOL.CODPROD.toString() ) ) );
				row.addElement( getString( rs.getString( GRID_SOL.REFPROD.toString() ) ) );
				row.addElement( getString( rs.getString( GRID_SOL.DESCPROD.toString() ) ) );
				row.addElement( getBigDecimal( rs.getBigDecimal( GRID_SOL.QTDITSOL.toString() ) ) );
				row.addElement( getBigDecimal( rs.getBigDecimal( GRID_SOL.QTDAPROVITSOL.toString() ) ) );
				row.addElement( new Integer(rs.getInt( GRID_SOL.CODSOL.toString() ) ) );
				row.addElement( new Integer(rs.getInt( GRID_SOL.CODITSOL.toString() ) ) );
				row.addElement( getBigDecimal( rs.getBigDecimal( GRID_SOL.SLDPROD.toString() ) ) );
				row.addElement( getString( rs.getString( GRID_SOL.CODCC.toString() ) ) );
				row.addElement( getString(  rs.getString(  GRID_SOL.IDUSUITSOL.toString() ) ) );
				row.addElement( getString( rs.getString( GRID_SOL.IDUSUAPROVITSOL.toString() ) ) );
				
				result.addElement( row );
			
			}
		} finally {
			ps = null;
			rs = null;
		}
		return result;		
	}
	
	public void criaCotacao( int codprod ) {
		
		
		String sSQLautoincrement = "SELECT coalesce(MAX(SS.CODSUMSOL) + 1, 1) AS CODSUMSOL FROM cpsumsol SS WHERE SS.CODEMP=? AND SS.CODFILIAL=?";

		String sSQLsumsol = "INSERT INTO CPSUMSOL " + "(CODEMP, CODFILIAL, CODSUMSOL, CODEMPPD, CODFILIALPD, CODPROD, REFPROD, QTDITSOL, QTDAPROVITSOL, SITITSOL, SITAPROVITSOL) " + "SELECT ?, ?, ?, " + "IT.CODEMPPD, IT.CODFILIALPD, IT.CODPROD,IT.REFPROD, "
				+ "SUM(IT.QTDITSOL), SUM(IT.QTDAPROVITSOL), 'PE', 'PE' " + "FROM CPSOLICITACAO O, CPITSOLICITACAO IT " + "WHERE O.CODEMP=IT.CODEMP AND O.CODFILIAL=IT.CODFILIAL AND O.CODSOL=IT.CODSOL " + "AND CODEMPPD = ? AND CODFILIALPD = ? AND CODPROD = ? "
				+ "group BY IT.CODEMPPD, IT.CODFILIALPD, IT.CODPROD, IT.REFPROD";

		String sSQLitsumsol = "INSERT INTO CPITSUMSOL " + "(CODEMP, CODFILIAL, CODSUMSOL, CODEMPSC, CODFILIALSC, CODSOL, CODITSOL) " + "SELECT ?, ?, ?, IT.CODEMP, IT.CODFILIAL, IT.CODSOL, IT.CODITSOL " + "FROM CPSOLICITACAO O, CPITSOLICITACAO IT "
				+ "WHERE O.CODEMP=IT.CODEMP AND O.CODFILIAL=IT.CODFILIAL AND O.CODSOL=IT.CODSOL " + "AND CODEMPPD = ? AND CODFILIALPD = ? AND CODPROD = ?";

		int codsumsol = 0;
		try {
			PreparedStatement ps = getConn().prepareStatement( sSQLautoincrement );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				codsumsol = rs.getInt( "CODSUMSOL" );
			}
		} catch ( SQLException err ) {
			err.printStackTrace();
		}

		try {
			PreparedStatement ps2 = getConn().prepareStatement( sSQLsumsol );

			ps2.setInt( 1, Aplicativo.iCodEmp );
			ps2.setInt( 2, Aplicativo.iCodFilial );
			ps2.setInt( 3, codsumsol );
			ps2.setInt( 4, Aplicativo.iCodEmp );
			ps2.setInt( 5, Aplicativo.iCodFilial );
			ps2.setInt( 6, codprod );

			ps2.execute();

			getConn().commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
		}

		try {
			PreparedStatement ps3 = getConn().prepareStatement( sSQLitsumsol );

			ps3.setInt( 1, Aplicativo.iCodEmp );
			ps3.setInt( 2, Aplicativo.iCodFilial );
			ps3.setInt( 3, codsumsol );
			ps3.setInt( 4, Aplicativo.iCodEmp );
			ps3.setInt( 5, Aplicativo.iCodFilial );
			ps3.setInt( 6, codprod );

			ps3.execute();

			getConn().commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
		}
	}
	
	public void updateQtd(BigDecimal qtditSol, int codemp, int codfilial, int codsol, int coditsol) throws SQLException{
		StringBuilder sql = null;
		PreparedStatement ps = null;
		try {
			sql = new StringBuilder("UPDATE ");
			sql.append( "CPITSOLICITACAO SET QTDAPROVITSOL=? where CODEMP =? AND CODFILIAL =? AND CODSOL =? AND CODITSOL =?" );
			
			ps = getConn().prepareStatement( sql.toString() );
			int params = 1;
			ps.setBigDecimal( params++, qtditSol);
			ps.setInt( params++, codemp );
			ps.setInt( params++, codfilial );
			ps.setInt( params++, codsol );
			ps.setInt( params++, coditsol );
			
			ps.executeUpdate();

			ps.close();
			getConn().commit();
			
		} finally {
			ps = null;
			sql = null;
		}
	}
	
	public int aprovaSolicitacao(   Vector<SelectedSol> dataVector ) throws SQLException{
		int result = 0;
		
		for (SelectedSol row: dataVector ) {
			if( (!row.getQtditsol().equals( row.getQtdaprovitsol() )) && (!"CA".equals( row.getSititsol() ) ) ) {
				updateQtd(  row.getQtditsol(), Aplicativo.iCodEmp, 
						ListaCampos.getMasterFilial( "CPITSOLICITACAO" ),row.getCodsol(), 
						row.getCoditsol() );
				result++;
			}
		}
		
		return result;
	}
	
	public boolean itensAprovZero(Vector<SelectedSol> dataVector) {
		boolean result = false;
		for (SelectedSol row: dataVector) {
			if (BigDecimal.ZERO.equals( row.getQtdaprovitsol() ) ) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	public Vector<SelectedSol> getSelecionados( Vector<Vector<Object>> dataVector ) {
		Vector<SelectedSol> solSel = new Vector<SelectedSol>();
		
		for (Vector<Object> row: dataVector ) {
			
			if ( (Boolean) row.elementAt(GRID_SOL.SEL.ordinal()) ) {
			
				GestaoSol.SelectedSol selected = new GestaoSol().new SelectedSol();
				selected.setSel( (Boolean) row.get( GRID_SOL.SEL.ordinal() ) );
				selected.setCodsol(  (Integer) row.get( GRID_SOL.CODSOL.ordinal() ) );
				selected.setCoditsol( (Integer) row.get( GRID_SOL.CODITSOL.ordinal() ) );
				selected.setSititsol(  (String) row.get( GRID_SOL.SITITSOL.ordinal() ) );
				selected.setCodcc( (String) row.get( GRID_SOL.CODCC.ordinal() ) );
				selected.setQtditsol( (BigDecimal) row.get( GRID_SOL.QTDITSOL.ordinal() ) );
				selected.setQtdaprovitsol( (BigDecimal) row.get( GRID_SOL.QTDAPROVITSOL.ordinal() ) );
				
				solSel.addElement(selected ); 
			}
		}
		return solSel;		
	}
	
	public Map<String, Object> getAnocc(String codcc, Integer codemp, Integer codfilial, String idusu) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = null;
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
				result.put( "codempcc", new Integer(rs.getInt( "CODEMPCC" ) ) );
				result.put( "codfilialcc", new Integer(rs.getInt( "CODFILIALCC" ) ) );
				result.put( "anocc", new Integer(rs.getInt( "ANOCC" ) ) );
				result.put( "codcc", getString( rs.getString( "CODCC" ) ) );
				result.put( "aprovasol", getString( rs.getString( "APROVCPSOLICITACAOUSU") ) );
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
