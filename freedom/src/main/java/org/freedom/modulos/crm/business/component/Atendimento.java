package org.freedom.modulos.crm.business.component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.freedom.library.swing.frame.Aplicativo;

public class Atendimento {

	public static Integer buscaAtendente() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		Integer codatend = null;
		try {

			sql.append( "SELECT CODATEND FROM ATATENDENTE WHERE CODEMPUS=? AND CODFILIALUS=? AND IDUSU=? " );

			ps = Aplicativo.getInstace().con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			ps.setString( 3, Aplicativo.getUsuario().getIdusu() );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				codatend = rs.getInt( "CODATEND" );
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return codatend;
	}
}
