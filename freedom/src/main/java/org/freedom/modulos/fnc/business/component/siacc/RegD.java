package org.freedom.modulos.fnc.business.component.siacc;

import org.freedom.library.business.exceptions.ExceptionSiacc;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.EColcli;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.ETipo;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.StuffCli;


public class RegD extends Reg {
	private final char COD_MOV = '0';

	private static final char CODREG = 'D'; // registro D.01

	private String identCliEmpAnt = null; // registro D.02

	private Integer agenciaDeb = null; // registro D.03

	private String identCliBanco = null; // registro D.04

	private String idetCliEmpAtual = null; // registro D.05

	private String ocorrencia = null; // registro D.06

	// private String C07 = null; // registro D.07
	private Integer seqRegistro = null; // registro D.08

	private Integer codMovimento = null; // registro D.09

	RegD( final String line ) throws ExceptionSiacc {

		super( CODREG );
		parseLine( line );
	}

	RegD( final char codreg, final StuffCli stfCli, final int numSeq ) {

		super( codreg );
		this.sbreg.append( format( stfCli.getCodigo(), ETipo.$9, 10, 0 ) );
		this.sbreg.append( format( "", ETipo.X, 15, 0 ) ); // Completar a identificação do cliente na empresa
		this.sbreg.append( format( stfCli.getArgs()[ EColcli.AGENCIACLI.ordinal() ], ETipo.$9, 4, 0 ) );
		this.sbreg.append( format( stfCli.getArgs()[ EColcli.IDENTCLI.ordinal() ], ETipo.X, 14, 0 ) );
		this.sbreg.append( format( stfCli.getCodigo(), ETipo.$9, 10, 0 ) );
		this.sbreg.append( format( "", ETipo.X, 15, 0 ) ); // Completar a identificação do cliente na empresa
		this.sbreg.append( format( "", ETipo.X, 60, 0 ) ); // Ocorrencia
		this.sbreg.append( format( "", ETipo.X, 14, 0 ) ); // Reservado para o futuro
		this.sbreg.append( format( numSeq, ETipo.$9, 6, 0 ) ); // Reservado para o futuro
		this.sbreg.append( COD_MOV );
		this.sbreg.append( (char) 13 );
		this.sbreg.append( (char) 10 );
	}

	protected void parseLine( final String line ) throws ExceptionSiacc {

		try {
			setIdentCliEmpAnt( line.substring( 1, 26 ) );
			setAgenciaDeb( line.substring( 26, 30 ).trim().length() > 0 ? new Integer( line.substring( 26, 30 ) ) : null );
			setIdentCliBanco( line.substring( 30, 44 ) );
			setIdetCliEmpAtual( line.substring( 44, 69 ) );
			setOcorrencia( line.substring( 69, 129 ) );
			// C07 = line.substring( 124, 143 );
			setSeqRegistro( line.substring( 143, 149 ).trim().length() > 0 ? new Integer( line.substring( 143, 149 ) ) : null );
			setCodMovimento( line.substring( 149 ).trim().length() > 0 ? new Integer( line.substring( 149 ) ) : null );
		} catch ( Exception e ) {
			throw new ExceptionSiacc( "Erro na leitura do registro D!\n " + e.getMessage() );
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

	public String getIdentCliEmpAnt() {

		return identCliEmpAnt;
	}

	public void setIdentCliEmpAnt( final String identCliEmpAnt ) {

		this.identCliEmpAnt = identCliEmpAnt;
	}

	public String getIdetCliEmpAtual() {

		return idetCliEmpAtual;
	}

	public void setIdetCliEmpAtual( final String idetCliEmpAtual ) {

		this.idetCliEmpAtual = idetCliEmpAtual;
	}

	public String getOcorrencia() {

		return ocorrencia;
	}

	public void setOcorrencia( final String ocorrencia ) {

		this.ocorrencia = ocorrencia;
	}

	public Integer getSeqRegistro() {

		return seqRegistro;
	}

	public void setSeqRegistro( final Integer seqRegistro ) {

		this.seqRegistro = seqRegistro;
	}
}
