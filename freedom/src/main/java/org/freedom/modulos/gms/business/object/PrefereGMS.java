package org.freedom.modulos.gms.business.object;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.freedom.library.swing.frame.Aplicativo;

public class PrefereGMS {

	private static PrefereGMS instance = null;

	private final String nometabela = "SGPREFERE8";

	public static enum CAMPOSPREFERE8 {
		CODTIPOMOVTC, SINCTICKET
	}

	private Integer codtipomovtc;
	
	private String sincticket;

	/*
	 * Construtor definido como private seguindo o padrão Singleton.
	 */
	private PrefereGMS() {

		super();

		StringBuilder sql = new StringBuilder();

		sql.append( "select " );

		for ( CAMPOSPREFERE8 campo : CAMPOSPREFERE8.values() ) {

			if ( campo.ordinal() > 0 ) {
				sql.append( "," );
			}

			sql.append( campo.name() );

		}

		sql.append( " from " + nometabela );
		sql.append( " where codemp=? and codfilial=?" );

		try {

			PreparedStatement ps = Aplicativo.getInstace().con.prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {

				setCodtipomovtc( rs.getInt( CAMPOSPREFERE8.CODTIPOMOVTC.name() ) );
				setSincticket( rs.getString( CAMPOSPREFERE8.SINCTICKET.name())==null ? "N" : rs.getString( CAMPOSPREFERE8.SINCTICKET.name()) );

			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	public static PrefereGMS getInstance() {

		if ( instance == null ) {
			instance = new PrefereGMS();
		}

		return instance;
	}

	public Integer getCodtipomovtc() {

		return codtipomovtc;
	}

	public void setCodtipomovtc( Integer codtipomovtc ) {

		this.codtipomovtc = codtipomovtc;
	}

	
	public String getSincticket() {
	
		return sincticket;
	}

	
	public void setSincticket( String sincticket ) {
	
		this.sincticket = sincticket;
	}

}
