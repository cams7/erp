package org.freedom.modulos.fnc.business.component.siacc;

import org.freedom.library.business.exceptions.ExceptionSiacc;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.ETipo;


public class RegZ extends Reg {
	private static final char CODREG = 'Z'; // registro Z.01

	private Integer totalRegistros = null; // registro Z.02

	private Long valorTotal = null; // registro Z.03

	// private String Z04 = null; // registro Z.04
	private Integer seqRegistro = null; // registro Z.05

	private Integer codMovimento = null; // registro Z.06

	public RegZ( final String line ) throws ExceptionSiacc {

		super( CODREG );
		parseLine( line );
	}

	public RegZ( final int totreg, final float vlrtotal, final int nroseq ) {

		super( 'Z' );
		this.sbreg.append( format( totreg, ETipo.$9, 6, 0 ) );
		this.sbreg.append( format( vlrtotal, ETipo.$9, 17, 2 ) );
		this.sbreg.append( format( "", ETipo.X, 119, 0 ) ); // Reservado para o futuro
		this.sbreg.append( format( nroseq, ETipo.$9, 6, 0 ) );
		this.sbreg.append( format( "", ETipo.$9, 1, 0 ) );
	}

	protected void parseLine( final String line ) throws ExceptionSiacc {

		try {
			setTotalRegistros( line.substring( 1, 7 ).trim().length() > 0 ? new Integer( line.substring( 1, 7 ) ) : null );
			setValorTotal( line.substring( 7, 24 ).trim().length() > 0 ? new Long( line.substring( 7, 24 ) ) : null );
			// Z04( line.substring( 24, 143 ) );
			setSeqRegistro( line.substring( 143, 149 ).trim().length() > 0 ? new Integer( line.substring( 143, 149 ) ) : null );
			setCodMovimento( line.substring( 149 ).trim().length() > 0 ? new Integer( line.substring( 149 ) ) : null );
		} catch ( Exception e ) {
			throw new ExceptionSiacc( "Erro na leitura do registro Z!\n" + e.getMessage() );
		}
	}

	public Integer getCodMovimento() {

		return codMovimento;
	}

	public void setCodMovimento( final Integer codMovimento ) {

		this.codMovimento = codMovimento;
	}

	public Integer getSeqRegistro() {

		return seqRegistro;
	}

	public void setSeqRegistro( final Integer seqRegistro ) {

		this.seqRegistro = seqRegistro;
	}

	public Integer getTotalRegistros() {

		return totalRegistros;
	}

	public void setTotalRegistros( final Integer totalRegistros ) {

		this.totalRegistros = totalRegistros;
	}

	public Long getValorTotal() {

		return valorTotal;
	}

	public void setValorTotal( final Long valorTotal ) {

		this.valorTotal = valorTotal;
	}
}
