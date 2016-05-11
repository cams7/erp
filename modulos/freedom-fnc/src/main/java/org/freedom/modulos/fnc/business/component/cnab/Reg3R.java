package org.freedom.modulos.fnc.business.component.cnab;

import java.math.BigDecimal;
import java.util.Date;

import org.freedom.library.business.exceptions.ExceptionCnab;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.ETipo;


public class Reg3R extends Reg3 {

	private int codDesc2;

	private Date dataDesc2;

	private BigDecimal vlrPercConced2;

	private int codDesc3;

	private Date dataDesc3;

	private BigDecimal vlrPercConced3;

	private int codMulta;

	private Date dataMulta;

	private BigDecimal vlrPercMulta;

	private String msgSacado;

	private String msg3;

	private String msg4;

	private String codBancoDeb;

	private String agenciaDeb;

	private String contaDeb;

	private int codOcorrSacado;

	public Reg3R() {

		super( 'R' );
	}

	public Reg3R( final String line ) throws ExceptionCnab {

		this();
		parseLine( line );
	}

	public String getAgenciaDeb() {

		return agenciaDeb;
	}

	public void setAgenciaDeb( final String agenciaDeb ) {

		this.agenciaDeb = agenciaDeb;
	}

	public String getCodBancoDeb() {

		return codBancoDeb;
	}

	public void setCodBancoDeb( final String codBancoDeb ) {

		this.codBancoDeb = codBancoDeb;
	}

	public int getCodDesc2() {

		return codDesc2;
	}

	/**
	 * Código do desconto.<br>
	 * 1 - Valor fixo até a data informada.<br>
	 * 2 - Percentual até a data informada.<br>
	 * 3 - Valor por antecipação por dia corrido.<br>
	 * 4 - Valor por antecipação por dia util.<br>
	 * 5 - Percentual sobre o valor nominal dia corrido.<br>
	 * 6 - Percentual sobre o valor nominal dia util.<br>
	 * Obs.: Para as opções 1 e 2 será obrigatório a informação da data.<br>
	 */
	public void setCodDesc2( final int codDesc2 ) {

		this.codDesc2 = codDesc2;
	}

	public int getCodDesc3() {

		return codDesc3;
	}

	/**
	 * Código do desconto.<br>
	 * 1 - Valor fixo até a data informada.<br>
	 * 2 - Percentual até a data informada.<br>
	 * 3 - Valor por antecipação por dia corrido.<br>
	 * 4 - Valor por antecipação por dia util.<br>
	 * 5 - Percentual sobre o valor nominal dia corrido.<br>
	 * 6 - Percentual sobre o valor nominal dia util.<br>
	 * Obs.: Para as opções 1 e 2 será obrigatório a informação da data.<br>
	 */
	public void setCodDesc3( final int codDesc3 ) {

		this.codDesc3 = codDesc3;
	}

	public int getCodMulta() {

		return codMulta;
	}

	/**
	 * Código da multa.<br>
	 * 1 - Valor fixo.<br>
	 * 2 - Percentual.<br>
	 */
	public void setCodMulta( final int codMulta ) {

		this.codMulta = codMulta;
	}

	public int getCodOcorrSacado() {

		return codOcorrSacado;
	}

	/**
	 * Deverá conter o(s) código(s) da(s) ocorrência(s) do sacado<br>
	 * a(s) qual(is) o cedente não concorda.<br>
	 * Somente será utilizado para o código de movimento 30.<br>
	 */
	public void setCodOcorrSacado( final int codOcorrSacado ) {

		this.codOcorrSacado = codOcorrSacado;
	}

	public String getContaDeb() {

		return contaDeb;
	}

	public void setContaDeb( final String contaDeb ) {

		this.contaDeb = contaDeb;
	}

	public Date getDataDesc2() {

		return dataDesc2;
	}

	public void setDataDesc2( final Date dataDesc2 ) {

		this.dataDesc2 = dataDesc2;
	}

	public Date getDataDesc3() {

		return dataDesc3;
	}

	public void setDataDesc3( final Date dataDesc3 ) {

		this.dataDesc3 = dataDesc3;
	}

	public Date getDataMulta() {

		return dataMulta;
	}

	public void setDataMulta( final Date dataMulta ) {

		this.dataMulta = dataMulta;
	}

	public String getMsg3() {

		return msg3;
	}

	/**
	 * Menssagem livre a ser impressa no campo instruções<br>
	 * da ficha de compensação do bloqueto.<br>
	 */
	public void setMsg3( final String msg3 ) {

		this.msg3 = msg3;
	}

	/**
	 * Menssagem livre a ser impressa no campo instruções<br>
	 * da ficha de compensação do bloqueto.<br>
	 */
	public String getMsg4() {

		return msg4;
	}

	public void setMsg4( final String msg4 ) {

		this.msg4 = msg4;
	}

	public String getMsgSacado() {

		return msgSacado;
	}

	/**
	 * Este campo só poderá ser utilizado,<br>
	 * cazo haja troca de arquivos magnéticos entre o banco e o sacado.<br>
	 */
	public void setMsgSacado( final String msgSacado ) {

		this.msgSacado = msgSacado;
	}

	public BigDecimal getVlrPercConced2() {

		return vlrPercConced2;
	}

	public void setVlrPercConced2( final BigDecimal vlrPercConced2 ) {

		this.vlrPercConced2 = vlrPercConced2;
	}

	public BigDecimal getVlrPercConced3() {

		return vlrPercConced3;
	}

	public void setVlrPercConced3( final BigDecimal vlrPercConced3 ) {

		this.vlrPercConced3 = vlrPercConced3;
	}

	public BigDecimal getVlrPercMulta() {

		return vlrPercMulta;
	}

	public void setVlrPercMulta( final BigDecimal vlrPercMulta ) {

		this.vlrPercMulta = vlrPercMulta;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.freedom.modulos.fnc.CnabUtil.Reg3#getLine()
	 */
	@ Override
	public String getLine( String padraocnab ) throws ExceptionCnab {

		StringBuilder line = new StringBuilder();

		try {
			line.append( super.getLineReg3( padraocnab ) );
			line.append( format( getCodDesc2(), ETipo.$9, 1, 0 ) );
			line.append( CnabUtil.dateToString( getDataDesc2(), null ) );
			line.append( format( getVlrPercConced2(), ETipo.$9, 15, 2 ) );
			line.append( format( getCodDesc3(), ETipo.$9, 1, 0 ) );
			line.append( CnabUtil.dateToString( getDataDesc3(), null ) );
			line.append( format( getVlrPercConced3(), ETipo.$9, 15, 2 ) );
			line.append( format( getCodMulta(), ETipo.$9, 1, 0 ) );
			line.append( CnabUtil.dateToString( getDataMulta(), null ) );
			line.append( format( getVlrPercMulta(), ETipo.$9, 15, 2 ) );
			line.append( format( getMsgSacado(), ETipo.X, 10, 0 ) );
			line.append( format( getMsg3(), ETipo.X, 40, 0 ) );
			line.append( format( getMsg4(), ETipo.X, 40, 0 ) );
			line.append( format( getCodBancoDeb(), ETipo.$9, 3, 0 ) );
			line.append( format( getAgenciaDeb(), ETipo.$9, 4, 0 ) );
			line.append( format( getContaDeb(), ETipo.$9, 13, 0 ) );
			line.append( format( getCodOcorrSacado(), ETipo.$9, 8, 0 ) );
			line.append( (char) 13 );
			line.append( (char) 10 );
		} catch ( Exception e ) {
			throw new ExceptionCnab( "CNAB registro 3 segmento R.\nErro ao escrever registro.\n" + e.getMessage() );
		}

		return line.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.freedom.modulos.fnc.CnabUtil.Reg3#parseLine(java.lang.String)
	 */
	@ Override
	public void parseLine( String line ) throws ExceptionCnab {

		try {

			if ( line == null ) {
				throw new ExceptionCnab( "Linha nula." );
			}
			else {

				super.parseLineReg3( line );
				setCodDesc2( line.substring( 17, 18 ).trim().length() > 0 ? Integer.parseInt( line.substring( 17, 18 ).trim() ) : 0 );
				setDataDesc2( CnabUtil.stringDDMMAAAAToDate( line.substring( 18, 26 ).trim() ) );
				setVlrPercConced2( CnabUtil.strToBigDecimal( line.substring( 26, 41 ) ) );
				setCodDesc3( line.substring( 41, 42 ).trim().length() > 0 ? Integer.parseInt( line.substring( 41, 42 ).trim() ) : 0 );
				setDataDesc3( CnabUtil.stringDDMMAAAAToDate( line.substring( 42, 50 ).trim() ) );
				setVlrPercConced3( CnabUtil.strToBigDecimal( line.substring( 50, 65 ) ) );
				setCodMulta( line.substring( 65, 66 ).trim().length() > 0 ? Integer.parseInt( line.substring( 65, 66 ).trim() ) : 0 );
				setDataMulta( CnabUtil.stringDDMMAAAAToDate( line.substring( 66, 74 ).trim() ) );
				setVlrPercMulta( CnabUtil.strToBigDecimal( line.substring( 74, 89 ) ) );
				setMsgSacado( line.substring( 89, 99 ) );
				setMsg3( line.substring( 99, 139 ) );
				setMsg4( line.substring( 139, 179 ) );
				setCodBancoDeb( line.substring( 179, 182 ) );
				setAgenciaDeb( line.substring( 182, 186 ) );
				setContaDeb( line.substring( 186, 199 ) );
				setCodOcorrSacado( line.substring( 199, 207 ).trim().length() > 0 ? Integer.parseInt( line.substring( 199, 207 ).trim() ) : 0 );
			}
		} catch ( Exception e ) {
			throw new ExceptionCnab( "CNAB registro 3 segmento R.\nErro ao ler registro.\n" + e.getMessage() );
		}
	}
}