package org.freedom.modulos.fnc.business.component.cnab;

import java.math.BigDecimal;
import java.util.Date;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.business.exceptions.ExceptionCnab;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.ETipo;

public class Reg3T extends Reg3 {

	private String agencia;

	private String digAgencia;

	private String conta;

	private String digConta;

	private String digAgConta;

	private String identTitBanco;

	private int carteira;

	private String docCob;

	private Date dataVencTit;

	private BigDecimal vlrTitulo;

	private String codBancoCob;

	private String agenciaCob;

	private String digAgenciaCob;

	private String identTitEmp;

	private int codMoeda;

	private int tipoInscCli;

	private String cpfCnpjCli;

	private String razCli;

	private String contratoCred;

	private BigDecimal vlrTarifa;

	private String codRejeicoes;
	
	private int diasProtesto;

	public Reg3T() {

		super( 'T' );
	}

	public Reg3T( final String line ) throws ExceptionCnab {

		this();
		parseLine( line );
	}

	public String getAgencia() {

		return agencia;
	}

	public void setAgencia( final String agencia ) {

		this.agencia = agencia;
	}

	public String getAgenciaCob() {

		return agenciaCob;
	}

	public void setAgenciaCob( final String agenciaCob ) {

		this.agenciaCob = agenciaCob;
	}

	public int getCarteira() {

		return carteira;
	}

	/**
	 * Carteira.<br>
	 * 1 - Cobrança simpres.<br>
	 * 2 - Cobrança vinculada.<br>
	 * 3 - Cobrança caucionada.<br>
	 * 4 - Cobrança descontada.<br>
	 * 7 - Cobrança direta especial / carteira 17.
	 */
	public void setCarteira( final int carteira ) {

		this.carteira = carteira;
	}

	public String getCodBancoCob() {

		return codBancoCob;
	}

	/**
	 * Agencia cobradora/recebedora.<br>
	 */
	public void setCodBancoCob( final String codBanco ) {

		this.codBancoCob = codBanco;
	}

	public int getCodMoeda() {

		return codMoeda;
	}

	/**
	 * Código da moeda.<br>
	 * 01 - Reservado para uso futuro.<br>
	 * 02 - Dolar americano comercial/venda.<br>
	 * 03 - Dolar americano turismo/venda.<br>
	 * 04 - ITRD.<br>
	 * 05 - IDTR.<br>
	 * 06 - UFIR diária.<br>
	 * 07 - UFIR mensal.<br>
	 * 08 - FAJ-TR.<br>
	 * 09 - Real.<br>
	 */
	public void setCodMoeda( final int codMoeda ) {

		this.codMoeda = codMoeda;
	}

	public String getCodRejeicoes() {

		return codRejeicoes;
	}

	public void setCodRejeicoes( final String codRejeicoes ) {

		this.codRejeicoes = codRejeicoes;
	}

	public String getConta() {

		return conta;
	}

	public void setConta( final String conta ) {

		this.conta = conta;
	}

	public String getContratoCred() {

		return contratoCred;
	}

	public void setContratoCred( final String contratoCred ) {

		this.contratoCred = contratoCred;
	}

	public String getCpfCnpjCli() {

		return cpfCnpjCli;
	}

	public void setCpfCnpjCli( final String cpfCnpjCli ) {

		this.cpfCnpjCli = cpfCnpjCli;
	}

	public Date getDataVencTit() {

		return dataVencTit;
	}

	public void setDataVencTit( final Date dataVencTit ) {

		this.dataVencTit = dataVencTit;
	}

	public String getDigAgConta() {

		return digAgConta;
	}

	public void setDigAgConta( final String digAgConta ) {

		this.digAgConta = digAgConta;
	}

	public String getDigAgencia() {

		return digAgencia;
	}

	public void setDigAgencia( final String digAgencia ) {

		this.digAgencia = digAgencia;
	}

	public String getDigAgenciaCob() {

		return digAgenciaCob;
	}

	public void setDigAgenciaCob( final String digAgenciaCob ) {

		this.digAgenciaCob = digAgenciaCob;
	}

	public String getDigConta() {

		return digConta;
	}

	public void setDigConta( final String digConta ) {

		this.digConta = digConta;
	}

	public String getDocCob() {

		return docCob;
	}

	public void setDocCob( final String docCob ) {

		this.docCob = docCob;
	}

	public String getIdentTitBanco() {

		return identTitBanco;
	}

	public void setIdentTitBanco( final String identTitBanco ) {

		this.identTitBanco = identTitBanco;
	}

	public String getIdentTitEmp() {

		return identTitEmp;
	}

	public void setIdentTitEmp( final String identTitEmp ) {

		this.identTitEmp = identTitEmp;
	}

	public String getRazCli() {

		return razCli;
	}

	public void setRazCli( final String razCli ) {

		this.razCli = razCli;
	}

	public int getTipoInscCli() {

		return tipoInscCli;
	}

	public void setTipoInscCli( final int tipoInscCli ) {

		this.tipoInscCli = tipoInscCli;
	}

	public BigDecimal getVlrTarifa() {

		return vlrTarifa;
	}

	public void setVlrTarifa( final BigDecimal vlrTarifa ) {

		this.vlrTarifa = vlrTarifa;
	}

	public BigDecimal getVlrTitulo() {

		return vlrTitulo;
	}

	public void setVlrTitulo( final BigDecimal vlrTitulo ) {

		this.vlrTitulo = vlrTitulo;
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
			line.append( format( getAgencia(), ETipo.$9, 5, 0 ) );
			line.append( format( getDigAgencia(), ETipo.$9, 1, 0 ) );
			line.append( format( getConta(), ETipo.$9, 12, 0 ) );
			line.append( format( getDigConta(), ETipo.$9, 1, 0 ) );
			line.append( format( getDigAgConta(), ETipo.$9, 1, 0 ) );
			line.append( format( getIdentTitBanco(), ETipo.X, 20, 0 ) );
			line.append( format( getCarteira(), ETipo.$9, 1, 0 ) );
			line.append( format( getDocCob(), ETipo.X, 15, 0 ) );
			line.append( CnabUtil.dateToString( getDataVencTit(), null ) );
			line.append( format( getVlrTitulo(), ETipo.$9, 15, 2 ) );
			line.append( format( getCodBancoCob(), ETipo.$9, 3, 0 ) );
			line.append( format( getAgenciaCob(), ETipo.$9, 5, 0 ) );
			line.append( format( getDigAgenciaCob(), ETipo.$9, 1, 0 ) );
			line.append( format( getIdentTitEmp(), ETipo.X, 25, 0 ) );
			line.append( format( getCodMoeda(), ETipo.$9, 2, 0 ) );
			line.append( format( getTipoInscCli(), ETipo.$9, 1, 0 ) );
			line.append( format( getCpfCnpjCli(), ETipo.$9, 15, 0 ) );
			line.append( format( getRazCli(), ETipo.X, 40, 0 ) );
			line.append( format( getContratoCred(), ETipo.$9, 10, 0 ) );
			line.append( format( getVlrTarifa(), ETipo.$9, 15, 2 ) );
			line.append( format( getCodRejeicoes(), ETipo.$9, 10, 0 ) );
			line.append( StringFunctions.replicate( " ", 17 ) );
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
				setAgencia( line.substring( 17, 22 ) );
				setDigAgencia( line.substring( 22, 23 ) );
				setConta( line.substring( 23, 35 ) );
				setDigConta( line.substring( 35, 36 ) );
				setDigAgConta( line.substring( 36, 37 ) );
				setIdentTitBanco( line.substring( 37, 57 ) );
				setCarteira( line.substring( 57, 58 ).trim().length() > 0 ? Integer.parseInt( line.substring( 57, 58 ).trim() ) : 0 );
				setDocCob( line.substring( 58, 73 ) );
				setDataVencTit( CnabUtil.stringDDMMAAAAToDate( line.substring( 73, 81 ).trim() ) );
				setVlrTitulo( CnabUtil.strToBigDecimal( line.substring( 81, 96 ) ) );
				setCodBancoCob( line.substring( 96, 99 ) );
				setAgenciaCob( line.substring( 99, 104 ) );
				setDigAgenciaCob( line.substring( 104, 105 ) );
				setIdentTitEmp( line.substring( 105, 130 ) );
				setCodMoeda( line.substring( 130, 132 ).trim().length() > 0 ? Integer.parseInt( line.substring( 130, 132 ).trim() ) : 0 );
				setTipoInscCli( line.substring( 132, 133 ).trim().length() > 0 ? Integer.parseInt( line.substring( 132, 133 ).trim() ) : 0 );
				setCpfCnpjCli( line.substring( 133, 148 ) );
				setRazCli( line.substring( 148, 188 ) );
				setContratoCred( line.substring( 188, 198 ) );
				setVlrTarifa( CnabUtil.strToBigDecimal( line.substring( 198, 213 ) ) );
				setCodRejeicoes( line.substring( 213, 223 ) );
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			throw new ExceptionCnab( "CNAB registro 3 segmento T.\nErro ao ler registro.\n" + e.getMessage() );
		}
	}
}