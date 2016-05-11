package org.freedom.modulos.fnc.business.component.siacc;

import org.freedom.library.business.exceptions.ExceptionSiacc;


public class RegX extends Reg {
	private static final char CODREG = 'X'; // registro X.01

	private String codAgencia = null; // registro X.02

	private String nomeAgencia = null; // registro X.03

	private String endAgencia = null; // registro X.04

	private String numAgencia = null; // registro X.05

	private String cepAgencia = null; // registro X.06

	private String sufixoCepAgencia = null; // registro X.07

	private String cidadeAgencia = null; // registro X.08

	private String ufAgencia = null; // registro X.09

	private String sitAgencia = null; // registro X.10

	// private String X11 = null; // registro X.11

	public RegX( final String line ) throws ExceptionSiacc {

		super( CODREG );
		parseLine( line );
	}

	protected void parseLine( final String line ) throws ExceptionSiacc {

		try {
			setCodAgencia( line.substring( 1, 5 ) );
			setNomeAgencia( line.substring( 5, 35 ) );
			setEndAgencia( line.substring( 35, 65 ) );
			setNumAgencia( line.substring( 65, 70 ) );
			setCepAgencia( line.substring( 70, 75 ) );
			setSufixoCepAgencia( line.substring( 75, 78 ) );
			setCidadeAgencia( line.substring( 78, 98 ) );
			setUfAgencia( line.substring( 98, 100 ) );
			setSitAgencia( line.substring( 100, 101 ) );
			// X11( line.substring( 101 ) );
		} catch ( Exception e ) {
			throw new ExceptionSiacc( "Erro na leitura do registro X!\n" + e.getMessage() );
		}
	}

	public String getCodAgencia() {

		return codAgencia;
	}

	public void setCodAgencia( final String agencia ) {

		this.codAgencia = agencia;
	}

	public String getCepAgencia() {

		return cepAgencia;
	}

	public void setCepAgencia( final String cepAgencia ) {

		this.cepAgencia = cepAgencia;
	}

	public String getCidadeAgencia() {

		return cidadeAgencia;
	}

	public void setCidadeAgencia( final String cidadeAgencia ) {

		this.cidadeAgencia = cidadeAgencia;
	}

	public String getEndAgencia() {

		return endAgencia;
	}

	public void setEndAgencia( final String endAgencia ) {

		this.endAgencia = endAgencia;
	}

	public String getNomeAgencia() {

		return nomeAgencia;
	}

	public void setNomeAgencia( final String nomeAgencia ) {

		this.nomeAgencia = nomeAgencia;
	}

	public String getNumAgencia() {

		return numAgencia;
	}

	public void setNumAgencia( final String numAgencia ) {

		this.numAgencia = numAgencia;
	}

	public String getSitAgencia() {

		return sitAgencia;
	}

	public void setSitAgencia( final String sitAgencia ) {

		this.sitAgencia = sitAgencia;
	}

	public String getSufixoCepAgencia() {

		return sufixoCepAgencia;
	}

	public void setSufixoCepAgencia( final String sufixoCepAgencia ) {

		this.sufixoCepAgencia = sufixoCepAgencia;
	}

	public String getUfAgencia() {

		return ufAgencia;
	}

	public void setUfAgencia( final String ufAgencia ) {

		this.ufAgencia = ufAgencia;
	}
}
