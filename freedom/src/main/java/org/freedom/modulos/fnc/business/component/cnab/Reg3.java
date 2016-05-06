package org.freedom.modulos.fnc.business.component.cnab;

import org.freedom.library.business.exceptions.ExceptionCnab;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.ETipo;

public abstract class Reg3 extends Reg {

	private String codBanco;

	private int loteServico;

	private int registroDetalhe;

	private int seqLote;

	private char segmento;

	private int codMovimento;

	public Reg3( final char segmento ) {

		setRegistroDetalhe( 3 );
		setSegmento( segmento );
	}

	public String getCodBanco() {

		return codBanco;
	}

	public void setCodBanco( final String codBanco ) {

		this.codBanco = codBanco;
	}

	public int getCodMovimento() {

		return codMovimento;
	}

	/**
	 * 01 - Entrada de titulos.<br>
	 * 02 - Pedido de baixa.<br>
	 * 04 - Concessão de abatimento.<br>
	 * 05 - Cancelamento de abatimento.<br>
	 * 06 - Alteração de vencimento.<br>
	 * 07 - Concessão de desconto.<br>
	 * 08 - Cancelamento de desconto.<br>
	 * 09 - Protestar.<br>
	 * 10 - Cancela/Sustação da instrução de protesto.<br>
	 * 30 - Recusa da alegação do sacado.<br>
	 * 31 - Alteração de outros dados.<br>
	 * 40 - Alteração da modalidade.<br>
	 */
	public void setCodMovimento( final int codMovimento ) {

		this.codMovimento = codMovimento;
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
	public void setLoteServico( final int loteServico ) {

		this.loteServico = loteServico;
	}

	public int getRegistroDetalhe() {

		return registroDetalhe;
	}

	/**
	 * Indica o tipo do registro.<br>
	 */
	private void setRegistroDetalhe( final int registroDetalhe ) {

		this.registroDetalhe = registroDetalhe;
	}

	public char getSegmento() {

		return segmento;
	}

	/**
	 * Indica o seguimento do registro.
	 */
	private void setSegmento( final char segmento ) {

		this.segmento = segmento;
	}

	public int getSeqLote() {

		return seqLote;
	}

	/**
	 * Número de sequência do registro no lote inicializado sempre em 1.<br>
	 */
	public void setSeqLote( final int seqLote ) {

		this.seqLote = seqLote;
	}

	public String getLineReg3( String padraocnab ) throws ExceptionCnab {

		StringBuilder line = new StringBuilder();

		try {
			line.append( format( getCodBanco(), ETipo.$9, 3, 0 ) );
			line.append( format( getLoteServico(), ETipo.$9, 4, 0 ) );
			line.append( getRegistroDetalhe() );
			line.append( format( getSeqLote(), ETipo.$9, 5, 0 ) );
			line.append( getSegmento() );
			line.append( ' ' );
			line.append( format( getCodMovimento(), ETipo.$9, 2, 0 ) );
		} catch ( Exception e ) {
			throw new ExceptionCnab( "CNAB registro 3.\nErro ao escrever registro.\n" + e.getMessage() );
		}

		return line.toString();
	}

	public void parseLineReg3( String line ) throws ExceptionCnab {

		try {

			if ( line == null ) {
				throw new ExceptionCnab( "Linha nula." );
			}
			else {

				setCodBanco( line.substring( 0, 3 ) );
				setLoteServico( line.substring( 3, 7 ).trim().length() > 0 ? Integer.parseInt( line.substring( 3, 7 ).trim() ) : 0 );
				setRegistroDetalhe( line.substring( 7, 8 ).trim().length() > 0 ? Integer.parseInt( line.substring( 7, 8 ).trim() ) : 0 );
				setSeqLote( line.substring( 8, 13 ).trim().length() > 0 ? Integer.parseInt( line.substring( 8, 13 ).trim() ) : 0 );
				setSegmento( line.substring( 13, 14 ).charAt( 0 ) );
				setCodMovimento( line.substring( 15, 17 ).trim().length() > 0 ? Integer.parseInt( line.substring( 15, 17 ).trim() ) : 0 );
			}
		} catch ( Exception e ) {
			throw new ExceptionCnab( "CNAB registro 3.\nErro ao ler registro.\n" + e.getMessage() );
		}
	}
}