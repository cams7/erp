package org.freedom.modulos.fnc.business.component.cnab;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.business.exceptions.ExceptionCnab;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.ETipo;


public class Reg3S extends Reg3 {

	private int tipoImpressao;

	private int linhaImp;

	private String msgImp;

	private int tipoChar;

	private String msg5;

	private String msg6;

	private String msg7;

	private String msg8;

	private String msg9;

	public Reg3S() {

		super( 'S' );
	}

	public Reg3S( final String line ) throws ExceptionCnab {

		this();
		parseLine( line );
	}

	public int getLinhaImp() {

		return linhaImp;
	}

	/**
	 * Número da linha a ser impressa.<br>
	 * Frente : Poderá variar de 01 a 36.<br>
	 * Verso : Poderá variar de 01 a 24.<br>
	 * Zeros : Para envio de bloqueto por e-mail.<br>
	 */
	public void setLinhaImp( final int linhaImp ) {

		this.linhaImp = linhaImp;
	}

	public String getMsg5() {

		return msg5;
	}

	/**
	 * Menssagem livre a ser impressa no campo instruções<br>
	 * da ficha de compensação do bloqueto.<br>
	 */
	public void setMsg5( final String msg5 ) {

		this.msg5 = msg5;
	}

	public String getMsg6() {

		return msg6;
	}

	/**
	 * Menssagem livre a ser impressa no campo instruções<br>
	 * da ficha de compensação do bloqueto.<br>
	 */
	public void setMsg6( final String msg6 ) {

		this.msg6 = msg6;
	}

	public String getMsg7() {

		return msg7;
	}

	/**
	 * Menssagem livre a ser impressa no campo instruções<br>
	 * da ficha de compensação do bloqueto.<br>
	 */
	public void setMsg7( final String msg7 ) {

		this.msg7 = msg7;
	}

	public String getMsg8() {

		return msg8;
	}

	/**
	 * Menssagem livre a ser impressa no campo instruções<br>
	 * da ficha de compensação do bloqueto.<br>
	 */
	public void setMsg8( final String msg8 ) {

		this.msg8 = msg8;
	}

	public String getMsg9() {

		return msg9;
	}

	/**
	 * Menssagem livre a ser impressa no campo instruções<br>
	 * da ficha de compensação do bloqueto.<br>
	 */
	public void setMsg9( final String msg9 ) {

		this.msg9 = msg9;
	}

	public String getMsgImp() {

		return msgImp;
	}

	/**
	 * Está linha deverá ser enviada no formato imagem de impressão,<br>
	 * com tamanho maximo de 140 posições.<br>
	 * Para bloqueto por e-mail: os endereços de e-mail deverão ser separados<br>
	 * por ";" e sem espaços.<br>
	 */
	public void setMsgImp( final String msgImp ) {

		this.msgImp = msgImp;
	}

	public int getTipoChar() {

		return tipoChar;
	}

	/**
	 * Formato do caractere de impressão.<br>
	 * 01 - Normal.<br>
	 * 02 - Itálico.<br>
	 * Zeros para envio de bloqueto por e-mail.<br>
	 */
	public void setTipoChar( final int tipoChar ) {

		this.tipoChar = tipoChar;
	}

	public int getTipoImpressao() {

		return tipoImpressao;
	}

	/**
	 * Código de indentificação para impressão da menssagem.<br>
	 * 1 - Frente do bloqueto.<br>
	 * 2 - Verso do bloqueto.<br>
	 * 3 - Campo instruções da ficha de compensação do bloqueto.<br>
	 * 8 - Bloqueto por e-mail.<br>
	 */
	public void setTipoImpressao( final int tipoImpressao ) {

		this.tipoImpressao = tipoImpressao;
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

			line.append( super.getLineReg3( padraocnab ) );
			line.append( format( getTipoImpressao(), ETipo.$9, 1, 0 ) );

			if ( getTipoImpressao() == 1 || getTipoImpressao() == 2 ) {

				line.append( format( getLinhaImp(), ETipo.$9, 2, 0 ) );
				line.append( format( getMsgImp(), ETipo.X, 140, 0 ) );
				line.append( format( getTipoChar(), ETipo.$9, 2, 0 ) );
				line.append( StringFunctions.replicate( " ", 78 ) );
			}
			else if ( getTipoImpressao() == 3 ) {

				line.append( format( getMsg5(), ETipo.X, 40, 0 ) );
				line.append( format( getMsg6(), ETipo.X, 40, 0 ) );
				line.append( format( getMsg7(), ETipo.X, 40, 0 ) );
				line.append( format( getMsg8(), ETipo.X, 40, 0 ) );
				line.append( format( getMsg9(), ETipo.X, 40, 0 ) );
				line.append( StringFunctions.replicate( " ", 22 ) );
			}

			line.append( (char) 13 );
			line.append( (char) 10 );

		} catch ( Exception e ) {
			throw new ExceptionCnab( "CNAB registro 3 segmento S.\nErro ao escrever registro.\n" + e.getMessage() );
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

				super.parseLineReg3( line );
				setTipoImpressao( line.substring( 17, 18 ).trim().length() > 0 ? Integer.parseInt( line.substring( 17, 18 ).trim() ) : 0 );

				if ( getTipoImpressao() == 1 || getTipoImpressao() == 2 ) {

					setLinhaImp( line.substring( 18, 20 ).trim().length() > 0 ? Integer.parseInt( line.substring( 18, 20 ).trim() ) : 0 );
					setMsgImp( line.substring( 20, 160 ) );
					setTipoChar( line.substring( 160, 162 ).trim().length() > 0 ? Integer.parseInt( line.substring( 160, 162 ).trim() ) : 0 );
				}
				else if ( getTipoImpressao() == 3 ) {

					setMsg5( line.substring( 18, 58 ) );
					setMsg6( line.substring( 58, 98 ) );
					setMsg7( line.substring( 98, 138 ) );
					setMsg8( line.substring( 138, 178 ) );
					setMsg9( line.substring( 178, 218 ) );
				}
			}
		} catch ( Exception e ) {
			throw new ExceptionCnab( "CNAB registro 3 segmento S.\nErro ao ler registro.\n" + e.getMessage() );
		}
	}
}
