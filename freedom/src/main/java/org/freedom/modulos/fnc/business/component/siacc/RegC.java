package org.freedom.modulos.fnc.business.component.siacc;

import org.freedom.library.business.exceptions.ExceptionSiacc;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.EColcli;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.ETipo;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.StuffCli;


public class RegC extends Reg {
	private final char COD_MOV = '2';

	private static final char CODREG = 'C'; // registro C.01

	private String identCliEmp = null; // registro C.02

	private Integer agenciaDeb = null; // registro C.03

	private String identCliBanco = null; // registro C.04

	private String ocorrencia1 = null; // registro C.05

	private String ocorrencia2 = null; // registro C.06

	// private String C07 = null; // registro C.07
	private Integer seqRegistro = null; // registro C.08

	private Integer codMovimento = null; // registro C.09

	public RegC( final String line ) throws ExceptionSiacc {

		super( CODREG );
		parseLine( line );
	}

	public RegC( final char codreg, final StuffCli stfCli, final int numSeq ) {

		super( codreg );
		this.sbreg.append( format( stfCli.getCodigo(), ETipo.$9, 10, 0 ) );
		this.sbreg.append( format( "", ETipo.X, 15, 0 ) ); // Completar a identificação do cliente na empresa
		this.sbreg.append( format( stfCli.getArgs()[ EColcli.AGENCIACLI.ordinal() ], ETipo.$9, 4, 0 ) );
		this.sbreg.append( format( stfCli.getArgs()[ EColcli.IDENTCLI.ordinal() ], ETipo.X, 14, 0 ) );
		this.sbreg.append( format( "", ETipo.X, 40, 0 ) ); // Ocorrencia 1
		this.sbreg.append( format( "", ETipo.X, 40, 0 ) ); // Ocorrencia 2
		this.sbreg.append( format( "", ETipo.X, 19, 0 ) ); // Reservado para o futuro
		this.sbreg.append( format( numSeq, ETipo.$9, 6, 0 ) ); // Reservado para o futuro
		this.sbreg.append( COD_MOV );
		this.sbreg.append( (char) 13 );
		this.sbreg.append( (char) 10 );
	}

	protected void parseLine( final String line ) throws ExceptionSiacc {

		try {
			identCliEmp = line.substring( 1, 26 );
			agenciaDeb = line.substring( 26, 30 ).trim().length() > 0 ? new Integer( line.substring( 26, 30 ) ) : null;
			identCliBanco = line.substring( 30, 44 );
			ocorrencia1 = line.substring( 44, 84 );
			ocorrencia2 = line.substring( 84, 124 );
			// C07 = line.substring( 124, 143 );
			seqRegistro = line.substring( 143, 149 ).trim().length() > 0 ? new Integer( line.substring( 143, 149 ) ) : null;
			codMovimento = line.substring( 149 ).trim().length() > 0 ? new Integer( line.substring( 149 ) ) : null;
		} catch ( Exception e ) {
			throw new ExceptionSiacc( "Erro na leitura do registro C!\n" + e.getMessage() );
		}

	}

	public Integer getAgenciaDeb() {

		return agenciaDeb;
	}

	public void setAgenciaDeb( final Integer agenciaDeb ) {

		this.agenciaDeb = agenciaDeb;
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

	public String getIdentCliEmp() {

		return identCliEmp;
	}

	public void setIdentCliEmp( final String identCliEmp ) {

		this.identCliEmp = identCliEmp;
	}

	public String getOcorrencia1() {

		return ocorrencia1;
	}

	public void setOcorrencia1( final String ocorrencia1 ) {

		this.ocorrencia1 = ocorrencia1;
	}

	public String getOcorrencia2() {

		return ocorrencia2;
	}

	public void setOcorrencia2( final String ocorrencia2 ) {

		this.ocorrencia2 = ocorrencia2;
	}

	public Integer getSeqRegistro() {

		return seqRegistro;
	}

	public void setSeqRegistro( final Integer seqRegistro ) {

		this.seqRegistro = seqRegistro;
	}
}