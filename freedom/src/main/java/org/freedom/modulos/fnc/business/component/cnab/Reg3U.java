package org.freedom.modulos.fnc.business.component.cnab;

import java.math.BigDecimal;
import java.util.Date;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.business.exceptions.ExceptionCnab;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.ETipo;

public class Reg3U extends Reg3 {

	private BigDecimal vlrJurosMulta;

	private BigDecimal vlrDesc;

	private BigDecimal vrlAbatCancel;

	private BigDecimal vlrIOF;

	private BigDecimal vlrPago;

	private BigDecimal vlrLiqCred;

	private BigDecimal vlrOutrasDesp;

	private BigDecimal vlrOutrosCred;

	private BigDecimal vlrOcorrSac;

	private Date dataOcorr;

	private Date dataEfetvCred;

	private String codOcorrSac;

	private Date dataOcorrSac;

	private String compOcorrSac;

	private String codBancoCompens;

	private String nossoNrCompens;

	public Reg3U() {

		super( 'U' );
	}

	public Reg3U( final String line ) throws ExceptionCnab {

		this();
		parseLine( line );
	}

	public String getCodBancoCompens() {

		return codBancoCompens;
	}

	public void setCodBancoCompens( final String codBancoCompens ) {

		this.codBancoCompens = codBancoCompens;
	}

	public String getCodOcorrSac() {

		return codOcorrSac;
	}

	public void setCodOcorrSac( final String codOcorrSac ) {

		this.codOcorrSac = codOcorrSac;
	}

	public String getCompOcorrSac() {

		return compOcorrSac;
	}

	public void setCompOcorrSac( final String compOcorrSac ) {

		this.compOcorrSac = compOcorrSac;
	}

	public Date getDataEfetvCred() {

		return dataEfetvCred;
	}

	public void setDataEfetvCred( final Date dataEfetvCred ) {

		this.dataEfetvCred = dataEfetvCred;
	}

	public Date getDataOcorr() {

		return dataOcorr;
	}

	public void setDataOcorr( final Date dataOcorr ) {

		this.dataOcorr = dataOcorr;
	}

	public Date getDataOcorrSac() {

		return dataOcorrSac;
	}

	public void setDataOcorrSac( final Date dataOcorrSac ) {

		this.dataOcorrSac = dataOcorrSac;
	}

	public String getNossoNrCompens() {

		return nossoNrCompens;
	}

	/**
	 * Somente para troca de arquivo entre bancos.<br>
	 */
	public void setNossoNrCompens( final String nossoNrCompens ) {

		this.nossoNrCompens = nossoNrCompens;
	}

	public BigDecimal getVlrDesc() {

		return vlrDesc;
	}

	public void setVlrDesc( final BigDecimal vlrDesc ) {

		this.vlrDesc = vlrDesc;
	}

	public BigDecimal getVlrIOF() {

		return vlrIOF;
	}

	public void setVlrIOF( final BigDecimal vlrIOF ) {

		this.vlrIOF = vlrIOF;
	}

	public BigDecimal getVlrJurosMulta() {

		return vlrJurosMulta;
	}

	public void setVlrJurosMulta( final BigDecimal vlrJurosMulta ) {

		this.vlrJurosMulta = vlrJurosMulta;
	}

	public BigDecimal getVlrLiqCred() {

		return vlrLiqCred;
	}

	public void setVlrLiqCred( final BigDecimal vlrLiqCred ) {

		this.vlrLiqCred = vlrLiqCred;
	}

	public BigDecimal getVlrOcorrSac() {

		return vlrOcorrSac;
	}

	public void setVlrOcorrSac( final BigDecimal vlrOcorrSac ) {

		this.vlrOcorrSac = vlrOcorrSac;
	}

	public BigDecimal getVlrOutrasDesp() {

		return vlrOutrasDesp;
	}

	public void setVlrOutrasDesp( final BigDecimal vlrOutrasDesp ) {

		this.vlrOutrasDesp = vlrOutrasDesp;
	}

	public BigDecimal getVlrOutrosCred() {

		return vlrOutrosCred;
	}

	public void setVlrOutrosCred( final BigDecimal vlrOutrosCred ) {

		this.vlrOutrosCred = vlrOutrosCred;
	}

	public BigDecimal getVlrPago() {

		return vlrPago;
	}

	public void setVlrPago( final BigDecimal vlrPago ) {

		this.vlrPago = vlrPago;
	}

	public BigDecimal getVrlAbatCancel() {

		return vrlAbatCancel;
	}

	public void setVlrAbatCancel( final BigDecimal vrlAbatCancel ) {

		this.vrlAbatCancel = vrlAbatCancel;
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
			line.append( format( getVlrJurosMulta(), ETipo.$9, 15, 2 ) );
			line.append( format( getVlrDesc(), ETipo.$9, 15, 2 ) );
			line.append( format( getVrlAbatCancel(), ETipo.$9, 15, 2 ) );
			line.append( format( getVlrIOF(), ETipo.$9, 15, 2 ) );
			line.append( format( getVlrPago(), ETipo.$9, 15, 2 ) );
			line.append( format( getVlrLiqCred(), ETipo.$9, 15, 2 ) );
			line.append( format( getVlrOutrasDesp(), ETipo.$9, 15, 2 ) );
			line.append( format( getVlrOutrosCred(), ETipo.$9, 15, 2 ) );
			line.append( CnabUtil.dateToString( getDataOcorr(), null ) );
			line.append( CnabUtil.dateToString( getDataEfetvCred(), null ) );
			line.append( format( getCodOcorrSac(), ETipo.X, 4, 0 ) );
			line.append( CnabUtil.dateToString( getDataOcorrSac(), null ) );
			line.append( format( getVlrOcorrSac(), ETipo.$9, 15, 2 ) );
			line.append( format( getCompOcorrSac(), ETipo.X, 30, 0 ) );
			line.append( format( getCodBancoCompens(), ETipo.$9, 3, 0 ) );
			line.append( format( getNossoNrCompens(), ETipo.$9, 20, 0 ) );
			line.append( StringFunctions.replicate( " ", 7 ) );
			line.append( (char) 13 );
			line.append( (char) 10 );

		} catch ( Exception e ) {
			throw new ExceptionCnab( "CNAB registro 3 segmento T.\nErro ao escrever registro.\n" + e.getMessage() );
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
				setVlrJurosMulta( CnabUtil.strToBigDecimal( line.substring( 17, 32 ) ) );
				setVlrDesc( CnabUtil.strToBigDecimal( line.substring( 32, 47 ) ) );
				setVlrAbatCancel( CnabUtil.strToBigDecimal( line.substring( 47, 62 ) ) );
				setVlrIOF( CnabUtil.strToBigDecimal( line.substring( 62, 77 ) ) );
				setVlrPago( CnabUtil.strToBigDecimal( line.substring( 77, 92 ) ) );
				setVlrLiqCred( CnabUtil.strToBigDecimal( line.substring( 92, 107 ) ) );
				setVlrOutrasDesp( CnabUtil.strToBigDecimal( line.substring( 107, 122 ) ) );
				setVlrOutrosCred( CnabUtil.strToBigDecimal( line.substring( 122, 137 ) ) );
				setDataOcorr( CnabUtil.stringDDMMAAAAToDate( line.substring( 137, 145 ).trim() ) );
				setDataEfetvCred( CnabUtil.stringDDMMAAAAToDate( line.substring( 145, 153 ).trim() ) );
				setCodOcorrSac( line.substring( 153, 157 ) );
				setDataOcorrSac( CnabUtil.stringDDMMAAAAToDate( line.substring( 157, 165 ).trim() ) );
				setVlrOcorrSac( CnabUtil.strToBigDecimal( line.substring( 165, 180 ) ) );
				setCompOcorrSac( line.substring( 180, 210 ) );
				setCodBancoCompens( line.substring( 210, 213 ) );
				setNossoNrCompens( line.substring( 213, 233 ) );
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			throw new ExceptionCnab( "CNAB registro 3 segmento U.\nErro ao ler registro.\n" + e.getMessage() );
		}
	}
}