package org.freedom.modulos.fnc.business.component.siacc;

import org.freedom.library.business.exceptions.ExceptionSiacc;


public class RegH extends Reg {
	private static final char CODREG = 'H'; // registro H.01

	private String identCliEmpAnt = null; // registro H.02

	private Integer agencia = null; // registro H.03

	private String identCliBanco = null; // registro H.04

	private String identCliEmpAtual = null; // registro H.05

	private String ocorrencia = null; // registro H.06

	// private String H07 = null; // registro H.07
	private Integer codMovimento = null; // registro H.08

	public RegH( final String line ) throws ExceptionSiacc {

		super( CODREG );
		parseLine( line );
	}

	protected void parseLine( final String line ) throws ExceptionSiacc {

		try {
			setIdentCliEmpAnt( line.substring( 1, 26 ) );
			setAgencia( line.substring( 26, 30 ).trim().length() > 0 ? new Integer( line.substring( 26, 30 ) ) : null );
			setIdentCliBanco( line.substring( 30, 44 ) );
			setIdentCliEmpAtual( line.substring( 44, 69 ) );
			setOcorrencia( line.substring( 69, 127 ) );
			// H07( line.substring( 127, 149 ) );
			setCodMovimento( line.substring( 149 ).trim().length() > 0 ? new Integer( line.substring( 149 ) ) : null );
		} catch ( Exception e ) {
			throw new ExceptionSiacc( "Erro na leitura do registro H!/n" + e.getMessage() );
		}

	}

	public Integer getAgencia() {

		return agencia;
	}

	public void setAgencia( final Integer agencia ) {

		this.agencia = agencia;
	}

	public Integer getCodMovimento() {

		return codMovimento;
	}

	public void setCodMovimento( final Integer codMovimento ) {

		this.codMovimento = codMovimento;
	}

	public String getIdentCliBanco() {

		return identCliBanco;
	}

	public void setIdentCliBanco( final String identCliBanco ) {

		this.identCliBanco = identCliBanco;
	}

	public String getIdentCliEmpAnt() {

		return identCliEmpAnt;
	}

	public void setIdentCliEmpAnt( final String identCliEmpAnt ) {

		this.identCliEmpAnt = identCliEmpAnt;
	}

	public String getIdentCliEmpAtual() {

		return identCliEmpAtual;
	}

	public void setIdentCliEmpAtual( final String identCliEmpAtual ) {

		this.identCliEmpAtual = identCliEmpAtual;
	}

	public String getOcorrencia() {

		return ocorrencia;
	}

	public void setOcorrencia( final String ocorrencia ) {

		this.ocorrencia = ocorrencia;
	}
}
