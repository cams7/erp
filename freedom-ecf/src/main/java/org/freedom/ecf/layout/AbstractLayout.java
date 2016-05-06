
package org.freedom.ecf.layout;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.freedom.ecf.app.PrinterMemory;

/**
 * Classe base de layouts para impressoras não fiscais <BR>
 * Projeto: freedom-ecf <BR>
 * Pacote: org.freedom.ecf.layout <BR>
 * Classe: @(#)AbstractLayout.java <BR>
 * <BR>
 * Este programa é licenciado de acordo com a LGPL (Lesser General Public License), <BR>
 * versão 2.1, Fevereiro de 1999 <BR>
 * A LGPL deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste Programa. <BR>
 * Caso uma cópia da LGPL não esteja disponível junto com este Programa, você pode contatar <BR>
 * o LICENCIADOR ou então pegar uma cópia em: <a href=http://creativecommons.org/licenses/LGPL/2.1/legalcode.pt> Creative Commons</a> <BR>
 * Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa é preciso estar de acordo com os termos da LGPL. <BR>
 * <BR>
 * 
 * @author Robson Sanchez/Setpoint Informática Ltda. <BR>
 *         criada: 30/05/2009. <BR>
 * <BR>
 */
public abstract class AbstractLayout {

	protected PrinterMemory memory;

	private int offSet = 0;

	protected int siseLine = 48;

	private StringBuilder buffer;

	public PrinterMemory getMemory() {

		return memory;
	}

	public void setMemory( PrinterMemory memory ) {

		this.memory = memory;
	}

	private StringBuilder getBuffer() {

		if ( buffer == null ) {
			setBuffer( new StringBuilder() );
		}
		return buffer;
	}

	private void setBuffer( StringBuilder buffer ) {

		this.buffer = buffer;
	}

	public static String replicate( String texto, int times ) {

		StringBuffer sRetorno = new StringBuffer();
		sRetorno.append( "" );
		for ( int i = 0; i < times; i++ ) {
			sRetorno.append( texto );
		}
		return sRetorno.toString();
	}

	protected String formatZero( int value, int size ) {

		return formatZero( String.valueOf( value ), size );
	}

	protected String formatStringRigth( String value, int size ) {

		String str = null;
		if ( value == null ) {
			str = replicate( " ", size );
		}
		else {
			str = replicate( " ", size - value.trim().length() );
			str += value.trim();
		}
		return str;
	}

	protected String formatZero( String value, int size ) {

		String zero = null;
		if ( value == null ) {
			zero = replicate( "0", size );
		}
		else {
			zero = replicate( "0", size - value.trim().length() );
			zero += value.trim();
		}
		return zero;
	}

	protected String formatString( String value, int size ) {

		String str = value;
		if ( value != null && value.length() > size ) {
			str = str.substring( 0, size );
		}
		return str;
	}

	protected String formatDecimal( float value, int decimal ) {

		return formatDecimal( new BigDecimal( value ), decimal );
	}

	protected String formatDecimal( BigDecimal value, int decimal ) {

		if ( value == null ) {
			return null;
		}
		DecimalFormat format = new DecimalFormat( "0." + formatZero( null, decimal ) );
		return format.format( value.setScale( decimal, BigDecimal.ROUND_HALF_UP ) ).replace( '.', ',' );
	}

	protected String formatCurrency( float money ) {

		return formatCurrency( new BigDecimal( money ) );
	}

	protected String formatCurrency( BigDecimal money ) {

		if ( money == null ) {
			return null;
		}
		DecimalFormat format = new DecimalFormat( memory.getSimboloMoeda() + " 0.00" );
		return format.format( money ).replace( '.', ',' );
	}

	protected void print( String text ) {

		if ( text != null ) {
			offSet += text.length();
			if ( text.indexOf( "\n" ) > -1 || offSet == siseLine ) {
				offSet = 0;
			}
			else if ( offSet > siseLine ) {
				offSet = offSet - siseLine;
			}
			getBuffer().append( text );
		}
	}

	protected void println() {

		print( "\n" );
	}

	protected void println( String text ) {

		print( text );
		print( "\n" );
	}

	protected void printCenter( String text ) {

		println( alignCenter( text ) );
	}

	protected void printRight( String text ) {

		printRight( text, true );
	}

	protected void printRight( String text, boolean cr ) {

		print( alignRight( text ) );
		if ( cr ) {
			println();
		}
	}

	protected void printline( String marker ) {

		printline( marker, true );
	}

	protected void printline( String marker, boolean cr ) {

		print( repeat( marker, siseLine ) );
		if ( cr ) {
			print( "\n" );
		}
	}

	protected void printline() {

		printline( "-" );
	}

	protected void printline( boolean cr ) {

		printline( "-", false );
	}

	protected String printBuffer() {

		String strBuffer = getBuffer().toString();
		setBuffer( null );
		return strBuffer;
	}

	protected String repeat( String text, int times ) {

		StringBuilder str = new StringBuilder();
		int count = 0;
		while ( count++ < times ) {
			str.append( text );
		}
		return str.toString();
	}

	protected String alignCenter( String text ) {

		StringBuilder textLine = new StringBuilder();
		if ( text != null ) {
			int saldo = ( siseLine - text.trim().length() ) / 2;
			textLine.append( repeat( " ", saldo ) );
			textLine.append( text.trim() );
			textLine.append( repeat( " ", saldo ) );
			if ( ( ( text.trim().length() - siseLine ) % 2 ) != 0 ) {
				textLine.append( " " );
			}
		}
		return textLine.toString();
	}

	protected String alignRight( String text ) {

		StringBuilder textLine = new StringBuilder();
		if ( text != null ) {
			int saldo = ( siseLine - offSet ) - text.trim().length();
			textLine.append( repeat( " ", saldo ) );
			textLine.append( text.trim() );
		}
		return textLine.toString();
	}

	public abstract String resultAliquotas();

	public abstract String programaFormaPagamento( final String descricao );

	public abstract String alteraSimboloMoeda( String simbolo );

	public abstract String aberturaDeCupom();

	public abstract String aberturaDeCupom( String cnpj );

	public abstract String programaUnidadeMedida( String descUnid );

	public abstract String vendaItem( String codProd, String descProd, String aliquota, char tpqtd, float qtd, float valor, char tpdesc, float desconto );

	public abstract String iniciaFechamentoCupom( final char opt, final float valor );

	public abstract String efetuaFormaPagamento( final String indice, final float valor, final String descForma );

	public abstract String finalizaFechamentoCupom( final String mensagem );

	public abstract String comprovanteNFiscalNVinculado( String opt, float valor, String formaPag );

	public abstract String relatorioGerencial( String texto );

	public abstract String fechamentoRelatorioGerencial();
}
