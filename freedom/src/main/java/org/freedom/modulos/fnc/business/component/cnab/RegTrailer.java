package org.freedom.modulos.fnc.business.component.cnab;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.business.component.Banco;
import org.freedom.library.business.exceptions.ExceptionCnab;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.ETipo;


public class RegTrailer extends Reg {

	private String codBanco;

	private String loteServico;

	private String registroTrailer;
	
	private String conta;

	private int qtdLotes;

	private int qtdRegistros;

	private int qtdConsilacoes;

	private int seqregistro;

	public RegTrailer() {

		setLoteServico( "9999" );
		setRegistroTrailer( "9" );
	}

	public int getSeqregistro() {

		return seqregistro;
	}

	public void setSeqregistro( int seqregistro ) {

		this.seqregistro = seqregistro;
	}

	public String getCodBanco() {

		return codBanco;
	}

	public void setCodBanco( final String codBanco ) {

		this.codBanco = codBanco;
	}

	public String getLoteServico() {

		return loteServico;
	}

	private void setLoteServico( final String loteServico ) {

		this.loteServico = loteServico;
	}

	public int getQtdConsilacoes() {

		return qtdConsilacoes;
	}

	public void setQtdConsilacoes( final int qtdConsilacoes ) {

		this.qtdConsilacoes = qtdConsilacoes;
	}

	public int getQtdLotes() {

		return qtdLotes;
	}

	public void setQtdLotes( final int qtdLotes ) {

		this.qtdLotes = qtdLotes;
	}

	public int getQtdRegistros() {

		return qtdRegistros;
	}

	public void setQtdRegistros( final int qtdRegistros ) {

		this.qtdRegistros = qtdRegistros;
	}

	public String getRegistroTrailer() {

		return registroTrailer;
	}

	private void setRegistroTrailer( final String regTrailer ) {

		this.registroTrailer = regTrailer;
	}

	
	public String getConta() {
	
		return conta;
	}

	
	public void setConta( String codConta ) {
	
		this.conta = codConta;
	}

	@ Override
	public String getLine( String padraocnab ) throws ExceptionCnab {

		StringBuilder line = new StringBuilder();

		try {
			if ( padraocnab.equals( CNAB_240 ) ) {

				line.append( format( getCodBanco(), ETipo.$9, 3, 0 ) );
				line.append( format( getLoteServico(), ETipo.$9, 4, 0 ) );
				line.append( format( getRegistroTrailer(), ETipo.$9, 1, 0 ) );
				line.append( StringFunctions.replicate( " ", 9 ) );
				line.append( format( getQtdLotes(), ETipo.$9, 6, 0 ) );
				line.append( format( getQtdRegistros(), ETipo.$9, 6, 0 ) );
				line.append( format( getQtdConsilacoes(), ETipo.$9, 6, 0 ) );
				line.append( StringFunctions.replicate( " ", 205 ) );
			}
			else if ( padraocnab.equals( CNAB_400 ) ) {
				line.append( StringFunctions.replicate( "9", 1 ) ); // Posição 001 a 001 - Identificação do registro
				
				if( getCodBanco().equals( Banco.SICRED )){
					line.append( "1" );
					line.append( getCodBanco() );
					line.append( format( getConta(), ETipo.$9, 5, 0 ) );
					line.append( StringFunctions.replicate( " ", 384 ) );
					
				} else {
					line.append( StringFunctions.replicate( " ", 393 ) ); // Posição 002 a 394 - Branco		
				}
				line.append( format( seqregistro, ETipo.$9, 6, 0 ) ); // Posição 395 a 400 - Nro Sequancial do ultimo registro

			}

		} catch ( Exception e ) {
			throw new ExceptionCnab( "CNAB registro trailer.\nErro ao escrever registro.\n" + e.getMessage() );
		}

		line.append( (char) 13 );
		line.append( (char) 10 );
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
				setLoteServico( line.substring( 3, 7 ) );
				setRegistroTrailer( line.substring( 7, 8 ) );
				setQtdRegistros( line.substring( 17, 23 ).trim().length() > 0 ? Integer.parseInt( line.substring( 17, 23 ).trim() ) : 0 );
				setQtdLotes( line.substring( 23, 29 ).trim().length() > 0 ? Integer.parseInt( line.substring( 23, 29 ).trim() ) : 0 );
				setQtdRegistros( line.substring( 29, 35 ).trim().length() > 0 ? Integer.parseInt( line.substring( 29, 35 ).trim() ) : 0 );
			}
		} catch ( Exception e ) {
			throw new ExceptionCnab( "CNAB registro trailer.\nErro ao ler registro.\n" + e.getMessage() );
		}
	}
	
}
