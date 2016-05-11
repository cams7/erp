package org.freedom.modulos.fnc.business.component.siacc;

import java.math.BigDecimal;

import org.freedom.library.business.exceptions.ExceptionSiacc;


public class RegF extends Reg {
	private static final char CODREG = 'F'; // registro F.01

	private String identCliEmp = null; // registro F.02

	private Integer agencia = null; // registro F.03

	private String identCliBanco = null; // registro F.04

	private Integer dataVenc = null; // registro F.05

	private String valorDebCred = null; // registro F.06

	private String codRetorno = null; // registro F.07

	private String usoEmp = null; // registro F.08

	// private String F09 = null; // registro F.09
	private Integer codMovimento = null; // registro F.10

	private Integer codRec = null;

	private Integer nparcItRec = null;

	public RegF( final String line ) throws ExceptionSiacc {

		super( CODREG );
		parseLine( line );
	}

	protected void parseLine( final String line ) throws ExceptionSiacc {

		try {
			setIdentCliEmp( line.substring( 1, 26 ) );
			setAgencia( line.substring( 26, 30 ).trim().length() > 0 ? new Integer( line.substring( 26, 30 ) ) : null );
			setIdentCliBanco( line.substring( 30, 44 ) );
			setDataVenc( line.substring( 44, 52 ).trim().length() > 0 ? new Integer( line.substring( 44, 52 ) ) : null );
			setValorDebCred( line.substring( 52, 67 ) );
			setCodRetorno( line.substring( 67, 69 ) );
			setUsoEmp( line.substring( 69, 129 ) );
			// F09 line.substring( 129, 149 ) );
			setCodMovimento( line.substring( 149 ).trim().length() > 0 ? new Integer( line.substring( 149 ) ) : null );
		} catch ( Exception e ) {
			throw new ExceptionSiacc( "Erro na leitura do registro F!\n" + e.getMessage() );
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

	public String getCodRetorno() {

		return codRetorno;
	}

	public void setCodRetorno( final String codRet ) {

		this.codRetorno = codRet;
	}

	public java.util.Date getDataVenc() throws Exception {

		return SiaccUtil.strToDate( String.valueOf( dataVenc ) );
	}

	public void setDataVenc( final Integer dataVenc ) {

		this.dataVenc = dataVenc;
	}

	public String getIdentCliBanco() {

		return identCliBanco;
	}

	public void setIdentCliBanco( final String identCliBanco ) {

		this.identCliBanco = identCliBanco;
	}

	public String getIdentCliEmp() {

		return identCliEmp;
	}

	public void setIdentCliEmp( final String identCliEmp ) {

		this.identCliEmp = identCliEmp;
	}

	public String getUsoEmp() {

		return usoEmp;
	}

	public void setUsoEmp( final String usoEmp ) {

		this.usoEmp = usoEmp;
		String codrec = this.usoEmp.substring( 0, 6 );
		String numparcrec = this.usoEmp.substring( 6, 10 );
		if ( codrec != null && codrec.trim().length() > 0 ) {
			setCodRec( Integer.parseInt( codrec.trim() ) );
		}
		if ( numparcrec != null && numparcrec.trim().length() > 0 ) {
			setNparcItRec( Integer.parseInt( numparcrec.trim() ) );
		}
	}

	public BigDecimal getValorDebCred() {

		return SiaccUtil.strToBigDecimal( valorDebCred );
	}

	public void setValorDebCred( final String valor ) {

		this.valorDebCred = valor;
	}

	public Integer getCodRec() {

		return codRec;
	}

	public void setCodRec( final Integer codRec ) {

		this.codRec = codRec;
	}

	public Integer getNparcItRec() {

		return nparcItRec;
	}

	public void setNparcItRec( final Integer nparcItRec ) {

		this.nparcItRec = nparcItRec;
	}
}
