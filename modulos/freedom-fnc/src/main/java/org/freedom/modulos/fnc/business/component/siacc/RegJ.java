package org.freedom.modulos.fnc.business.component.siacc;

import org.freedom.library.business.exceptions.ExceptionSiacc;


public class RegJ extends Reg {
	private static final char CODREG = 'J'; // registro J.01

	private String menssagemInfo = null; // registro J.02

	private String filler = null; // registro J.03

	public RegJ( final String line ) throws ExceptionSiacc {

		super( CODREG );
		parseLine( line );
	}

	protected void parseLine( final String line ) throws ExceptionSiacc {

		try {
			setMenssagemInfo( line.substring( 1, 27 ) );
			setFiller( line.substring( 27 ) );
		} catch ( Exception e ) {
			throw new ExceptionSiacc( "Erro na leitura do registro J!" + e.getMessage() );
		}
	}

	public String getFiller() {

		return filler;
	}

	public void setFiller( final String filler ) {

		this.filler = filler;
	}

	public String getMenssagemInfo() {

		return menssagemInfo;
	}

	public void setMenssagemInfo( final String menssagemInfo ) {

		this.menssagemInfo = menssagemInfo;
	}
}
