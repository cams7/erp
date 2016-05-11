package org.freedom.modulos.fnc.business.component.cnab;

import java.math.BigDecimal;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.business.exceptions.ExceptionCnab;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.ETipo;

public class Reg5 extends Reg {

	private String codBanco;

	private int loteServico;

	private int registroTrailer;

	private int qtdRegistros;

	private int qtdSimples;

	private BigDecimal vlrSimples;

	private int qtdVinculado;

	private BigDecimal vlrVinculado;

	private int qtdCalculado;

	private BigDecimal vlrCalculado;

	private int qtdDescontado;

	private BigDecimal vlrDescontado;

	private String avisoLanca;

	public Reg5() {

		setRegistroTrailer( 5 );
	}

	public Reg5( final String line ) throws ExceptionCnab {

		this();
		parseLine( line );
	}

	public String getAvisoLanca() {

		return avisoLanca;
	}

	/**
	 * Somente será utilizado para informação do arquivo de retorno.<br>
	 */
	public void setAvisoLanca( String avisoLanca ) {

		this.avisoLanca = avisoLanca;
	}

	public String getCodBanco() {

		return codBanco;
	}

	public void setCodBanco( String codBanco ) {

		this.codBanco = codBanco;
	}

	public int getLoteServico() {

		return loteServico;
	}

	/**
	 * Indentifica um Lote de Serviço.<br>
	 * Sequencial e nmão deve ser repetido dentro do arquivo.<br>
	 * As numerações 0000 e 9999 <br>
	 * são exclusivas para o Header e para o Trailer do arquivo respectivamente.<br>
	 */
	public void setLoteServico( int loteServico ) {

		this.loteServico = loteServico;
	}

	public int getQtdCalculado() {

		return qtdCalculado;
	}

	/**
	 * Somente será utilizado para informação do arquivo de retorno.<br>
	 */
	public void setQtdCalculado( int qtdCalculado ) {

		this.qtdCalculado = qtdCalculado;
	}

	public int getQtdDescontado() {

		return qtdDescontado;
	}

	/**
	 * Somente será utilizado para informação do arquivo de retorno.<br>
	 */
	public void setQtdDescontado( int qtdDescontado ) {

		this.qtdDescontado = qtdDescontado;
	}

	public int getQtdRegistros() {

		return qtdRegistros;
	}

	/**
	 * Quantidade de registros no lote, incluindo o Header e o Traler.<br>
	 */
	public void setQtdRegistros( int qtdRegistros ) {

		this.qtdRegistros = qtdRegistros;
	}

	public int getQtdSimples() {

		return qtdSimples;
	}

	/**
	 * Somente será utilizado para informação do arquivo de retorno.<br>
	 */
	public void setQtdSimples( int qtdSimples ) {

		this.qtdSimples = qtdSimples;
	}

	public int getQtdVinculado() {

		return qtdVinculado;
	}

	/**
	 * Somente será utilizado para informação do arquivo de retorno.<br>
	 */
	public void setQtdVinculado( int qtdVinculado ) {

		this.qtdVinculado = qtdVinculado;
	}

	public int getRegistroTrailer() {

		return registroTrailer;
	}

	private void setRegistroTrailer( int registroTraler ) {

		this.registroTrailer = registroTraler;
	}

	public BigDecimal getVlrCalculado() {

		return vlrCalculado;
	}

	/**
	 * Somente será utilizado para informação do arquivo de retorno.<br>
	 */
	public void setVlrCalculado( BigDecimal vlrCalculado ) {

		this.vlrCalculado = vlrCalculado;
	}

	public BigDecimal getVlrDescontado() {

		return vlrDescontado;
	}

	/**
	 * Somente será utilizado para informação do arquivo de retorno.<br>
	 */
	public void setVlrDescontado( BigDecimal vlrDescontado ) {

		this.vlrDescontado = vlrDescontado;
	}

	public BigDecimal getVlrSimples() {

		return vlrSimples;
	}

	/**
	 * Somente será utilizado para informação do arquivo de retorno.<br>
	 */
	public void setVlrSimples( BigDecimal vlrSimples ) {

		this.vlrSimples = vlrSimples;
	}

	public BigDecimal getVlrVinculado() {

		return vlrVinculado;
	}

	/**
	 * Somente será utilizado para informação do arquivo de retorno.<br>
	 */
	public void setVlrVinculado( BigDecimal vlrVinculado ) {

		this.vlrVinculado = vlrVinculado;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.freedom.modulos.fnc.CnabUtil.Reg#getLine()
	 */
	@ Override
	public String getLine( String padraocnab ) throws ExceptionCnab {

		StringBuilder line = new StringBuilder();

		try {
			line.append( format( getCodBanco(), ETipo.$9, 3, 0 ) );
			line.append( format( getLoteServico(), ETipo.$9, 4, 0 ) );
			line.append( format( getRegistroTrailer(), ETipo.$9, 1, 0 ) );
			line.append( StringFunctions.replicate( " ", 9 ) );
			line.append( format( getQtdRegistros(), ETipo.$9, 6, 0 ) );
			line.append( format( getQtdSimples(), ETipo.$9, 6, 0 ) );
			line.append( format( getVlrSimples(), ETipo.$9, 17, 2 ) );
			line.append( format( getQtdVinculado(), ETipo.$9, 6, 0 ) );
			line.append( format( getVlrVinculado(), ETipo.$9, 17, 2 ) );
			line.append( format( getQtdCalculado(), ETipo.$9, 6, 0 ) );
			line.append( format( getVlrCalculado(), ETipo.$9, 17, 2 ) );
			line.append( format( getQtdDescontado(), ETipo.$9, 6, 0 ) );
			line.append( format( getVlrDescontado(), ETipo.$9, 17, 2 ) );
			line.append( format( getAvisoLanca(), ETipo.X, 8, 0 ) );
			line.append( StringFunctions.replicate( " ", 117 ) );
			line.append( (char) 13 );
			line.append( (char) 10 );

		} catch ( Exception e ) {
			throw new ExceptionCnab( "CNAB registro 5.\nErro ao escrever registro.\n" + e.getMessage() );
		}

		return line.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.freedom.modulos.fnc.CnabUtil.Reg#parseLine(java.lang.String)
	 */
	@ Override
	public void parseLine( String line ) throws ExceptionCnab {

		try {

			if ( line == null ) {
				throw new ExceptionCnab( "Linha nula." );
			}
			else {

				setCodBanco( line.substring( 0, 3 ) );
				setLoteServico( line.substring( 3, 7 ).trim().length() > 0 ? Integer.parseInt( line.substring( 3, 7 ).trim() ) : 0 );
				setRegistroTrailer( line.substring( 7, 8 ).trim().length() > 0 ? Integer.parseInt( line.substring( 7, 8 ).trim() ) : 0 );
				setQtdRegistros( line.substring( 17, 23 ).trim().length() > 0 ? Integer.parseInt( line.substring( 17, 23 ).trim() ) : 0 );
				setQtdSimples( line.substring( 23, 29 ).trim().length() > 0 ? Integer.parseInt( line.substring( 23, 29 ).trim() ) : 0 );
				setVlrSimples( CnabUtil.strToBigDecimal( line.substring( 29, 46 ) ) );
				setQtdVinculado( line.substring( 46, 52 ).trim().length() > 0 ? Integer.parseInt( line.substring( 46, 52 ).trim() ) : 0 );
				setVlrVinculado( CnabUtil.strToBigDecimal( line.substring( 52, 69 ) ) );
				setQtdCalculado( line.substring( 69, 75 ).trim().length() > 0 ? Integer.parseInt( line.substring( 69, 75 ).trim() ) : 0 );
				setVlrCalculado( CnabUtil.strToBigDecimal( line.substring( 75, 92 ) ) );
				setQtdDescontado( line.substring( 92, 98 ).trim().length() > 0 ? Integer.parseInt( line.substring( 92, 98 ).trim() ) : 0 );
				setVlrDescontado( CnabUtil.strToBigDecimal( line.substring( 98, 115 ) ) );
				setAvisoLanca( line.substring( 115, 123 ) );
			}
		} catch ( Exception e ) {
			throw new ExceptionCnab( "CNAB registro 5.\nErro ao ler registro.\n" + e.getMessage() );
		}
	}
}