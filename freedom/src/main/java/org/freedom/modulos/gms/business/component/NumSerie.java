package org.freedom.modulos.gms.business.component;

import java.awt.Component;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.gms.view.dialog.utility.DLSerie;
import org.freedom.modulos.gms.view.dialog.utility.DLSerieGrid;

public class NumSerie {

	private String numserie = null;

	private ListaCampos lcDet = null;

	private Integer codprod = null;

	private Integer cod = null;

	private Integer itcod = null;

	private String descprod = null;

	private boolean unico = false;

	private Component corig = null;

	public NumSerie( Component corig, ListaCampos lcDet, Integer cod, Integer itcod, Integer codprod, String descprod, String numserie, boolean unico ) {

		setCod( cod );
		setItcod( itcod );
		setCodprod( codprod );
		setDescprod( descprod );
		setNumserie( numserie );
		setUnico( unico );
		setCorig( corig );
		setLcDet( lcDet );
	}

	public boolean isUnico() {

		return unico;
	}

	public void setUnico( boolean unico ) {

		this.unico = unico;
	}

	public boolean testaNumSerie( int tipo ) {

		boolean bRetorno = false;
		boolean bValido = false;

		// Validação e abertura da tela para cadastramento da serie unitária
		if ( unico ) {

			String sSQL = "SELECT COUNT(*) FROM EQSERIE WHERE NUMSERIE=? AND CODPROD=? AND CODEMP=? AND CODFILIAL=?";

			PreparedStatement ps = null;
			ResultSet rs = null;

			try {

				ps = Aplicativo.getInstace().con.prepareStatement( sSQL );
				ps.setString( 1, numserie );
				ps.setInt( 2, codprod );
				ps.setInt( 3, Aplicativo.iCodEmp );
				ps.setInt( 4, ListaCampos.getMasterFilial( "EQSERIE" ) );

				rs = ps.executeQuery();

				if ( rs.next() ) {
					if ( rs.getInt( 1 ) > 0 ) {
						bValido = true;
					}
				}

				rs.close();
				ps.close();

			} catch ( SQLException err ) {
				Funcoes.mensagemErro( null, "Erro ao consultar a tabela EQSERIE!\n" + err.getMessage(), true, Aplicativo.getInstace().con, err );
			}
			if ( !bValido ) {

				DLSerie dl = new DLSerie( getCorig(), getNumserie(), getCodprod(), getDescprod() );

				dl.setVisible( true );

				if ( dl.OK ) {
					bRetorno = true;
					setNumserie( dl.getNumSerie() );

				}
				dl.dispose();
			}
			else {
				bRetorno = true;
			}
		}
		// Tela para cadastramento da série para quantidade maior que 1
		else {

			abreDlSerieMuitiplos( tipo );

		}

		return bRetorno;
	}

	public String getNumserie() {

		return numserie;
	}

	public void setNumserie( String numserie ) {

		this.numserie = numserie;
	}

	private void abreDlSerieMuitiplos( int tipo ) {

		DLSerieGrid dl = new DLSerieGrid();
		dl.setCodemp( Aplicativo.iCodEmp );
		dl.setCodfilial( getLcDet().getCodFilial() );

		dl.setCod( getCod() );
		dl.setCodit( getItcod() );
		dl.setTipo( tipo );

		dl.setCodemppd( Aplicativo.iCodEmp );
		dl.setCodfilialpd( ListaCampos.getMasterFilial( "EQPRODUTO" ) );
		dl.setCodprod( getCodprod() );
		dl.setDescprod( getDescprod() );

		dl.setConexao( Aplicativo.getInstace().getConexao() );
		dl.setVisible( true );
		HashMap<String, Integer> retorno = null;

		if ( dl.OK ) {
			// IMplementar
		}
		else {
			// Implementar
		}

		dl.dispose();
	}

	public static boolean isGarantia( Date validade, Date entrada ) {

		if ( validade != null && entrada != null ) {

			if ( Funcoes.getNumDias( validade, entrada ) >= 0 ) {

				return true;
			}

		}

		return false;

	}

	public ListaCampos getLcDet() {

		return lcDet;
	}

	public void setLcDet( ListaCampos lcDet ) {

		this.lcDet = lcDet;
	}

	public Integer getCodprod() {

		return codprod;
	}

	public void setCodprod( Integer codprod ) {

		this.codprod = codprod;
	}

	public Integer getCod() {

		return cod;
	}

	public void setCod( Integer cod ) {

		this.cod = cod;
	}

	public Integer getItcod() {

		return itcod;
	}

	public void setItcod( Integer itcod ) {

		this.itcod = itcod;
	}

	public String getDescprod() {

		return descprod;
	}

	public void setDescprod( String descprod ) {

		this.descprod = descprod;
	}

	public Component getCorig() {

		return corig;
	}

	public void setCorig( Component corig ) {

		this.corig = corig;
	}

}
