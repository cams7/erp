package org.freedom.modulos.lvf.business.object;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;

public class SeqSerie {

	private static SeqSerie instance = null;

	private Integer codtipomovtc;

	private String serie;

	private Integer docserie;

	public SeqSerie() {

		super();

	}

	public SeqSerie( String serie ) {

		setSerie( serie );
		carregaSeqSerie();
	}

	public void carregaSeqSerie() {

		StringBuilder sql = new StringBuilder();

		sql.append( "select coalesce(docserie,0) docserie from lfseqserie " );
		sql.append( "where codemp=? and codfilial=? and serie=? and codempss=? and codfilialss=? and ativserie='S' " );

		try {

			DbConnection con = Aplicativo.getInstace().getConexao();

			PreparedStatement ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "LFSERIE" ) );
			ps.setString( 3, getSerie() );

			ps.setInt( 4, Aplicativo.iCodEmp );
			ps.setInt( 5, ListaCampos.getMasterFilial( "LFSEQSERIE" ) );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				setDocserie( rs.getInt( "docserie" ) );
			}
			else {
				setDocserie( 0 );
			}

			con.commit();

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	public String getSerie() {

		return serie;
	}

	public void setSerie( String serie ) {

		this.serie = serie;
	}

	public Integer getDocserie() {

		return docserie;
	}

	public void setDocserie( Integer docserie ) {

		this.docserie = docserie;
	}

}
