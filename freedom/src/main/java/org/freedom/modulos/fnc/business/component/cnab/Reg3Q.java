package org.freedom.modulos.fnc.business.component.cnab;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.business.exceptions.ExceptionCnab;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.ETipo;

public class Reg3Q extends Reg3 {

	private int tipoInscCli;

	private String cpfCnpjCli;

	private String razCli;

	private String endCli;

	private String bairCli;

	private String cepCli;

	// private String sufxCepCli;
	private String cidCli;

	private String ufCli;

	private int tipoInscAva;

	private String cpfCnpjAva;

	private String razAva;

	private int codCompensacao;

	private String nossoNumero;

	public Reg3Q() {

		super( 'Q' );
	}

	public Reg3Q( final String line ) throws ExceptionCnab {

		this();
		parseLine( line );
	}

	public String getBairCli() {

		return bairCli;
	}

	public void setBairCli( final String bairCli ) {

		this.bairCli = bairCli;
	}

	public String getCepCli() {

		return cepCli;
	}

	public void setCepCli( final String cepCli ) {

		this.cepCli = cepCli;
	}

	public String getCidCli() {

		return cidCli;
	}

	public void setCidCli( final String cidCli ) {

		this.cidCli = cidCli;
	}

	public int getCodCompensacao() {

		return codCompensacao;
	}

	/**
	 * Somente para troca de arquivos entre bancos.<br>
	 */
	private void setCodCompensacao( final int codCompensacao ) {

		this.codCompensacao = codCompensacao;
	}

	public String getCpfCnpjAva() {

		return cpfCnpjAva;
	}

	/**
	 * Inscrição. Conforme o tipo da inscrição.<br>
	 * 
	 * @see org.freedom.modulos.fnc.business.component.cnab.CnabUtil.Reg3Q#setTipoInscAva(int tipoInscEmp )
	 */
	public void setCpfCnpjAva( final String cpfCnpjAva ) {

		this.cpfCnpjAva = cpfCnpjAva;
	}

	public String getCpfCnpjCli() {

		return cpfCnpjCli;
	}

	/**
	 * Inscrição. Conforme o tipo da inscrição.<br>
	 * 
	 * @see org.freedom.modulos.fnc.business.component.cnab.CnabUtil.Reg3Q#setTipoInscCli(int tipoInscEmp )
	 */
	public void setCpfCnpjCli( final String cpfCnpjCli ) {

		this.cpfCnpjCli = cpfCnpjCli;
	}

	public String getEndCli() {

		return endCli;
	}

	public void setEndCli( final String endCli ) {

		this.endCli = endCli;
	}

	public String getNossoNumero() {

		return nossoNumero;
	}

	/**
	 * Somente para troca de arquivos entre bancos.<br>
	 */
	private void setNossoNumero( final String nossoNumero ) {

		this.nossoNumero = nossoNumero;
	}

	public String getRazAva() {

		return razAva;
	}

	/**
	 * Informação obrigatória quando se tratar de titulo negociado em nome de terceiros.<br>
	 */
	public void setRazAva( final String razAva ) {

		this.razAva = razAva;
	}

	public String getRazCli() {

		return razCli;
	}

	public void setRazCli( final String razCli ) {

		this.razCli = razCli;
	}

	public int getTipoInscAva() {

		return tipoInscAva;
	}

	/**
	 * Indica o tipo de inscrição da empresa.<br>
	 * 0 - Isento / Não informado.<br>
	 * 1 - CPF.<br>
	 * 2 - CNPJ.<br>
	 */
	public void setTipoInscAva( final int tipoInscAva ) {

		this.tipoInscAva = tipoInscAva;
	}

	public int getTipoInscCli() {

		return tipoInscCli;
	}

	/**
	 * Indica o tipo de inscrição da empresa.<br>
	 * 0 - Isento / Não informado.<br>
	 * 1 - CPF.<br>
	 * 2 - CNPJ.<br>
	 */
	public void setTipoInscCli( final int tipoInscCli ) {

		this.tipoInscCli = tipoInscCli;
	}

	public String getUfCli() {

		return ufCli;
	}

	public void setUfCli( final String ufCli ) {

		this.ufCli = ufCli;
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
			line.append( format( getTipoInscCli(), ETipo.$9, 1, 0 ) );
			line.append( format( getCpfCnpjCli(), ETipo.$9, 15, 0 ) );
			line.append( format( getRazCli(), ETipo.X, 40, 0 ) );
			line.append( format( getEndCli(), ETipo.X, 40, 0 ) );
			line.append( format( getBairCli(), ETipo.X, 15, 0 ) );
			line.append( format( getCepCli(), ETipo.$9, 8, 0 ) );
			line.append( format( getCidCli(), ETipo.X, 15, 0 ) );
			line.append( format( getUfCli(), ETipo.X, 2, 0 ) );
			line.append( format( getTipoInscAva(), ETipo.$9, 1, 0 ) );
			line.append( format( getCpfCnpjAva(), ETipo.$9, 15, 0 ) );
			line.append( format( getRazAva(), ETipo.X, 40, 0 ) );
			line.append( format( getCodCompensacao(), ETipo.$9, 3, 0 ) );
			line.append( format( getNossoNumero(), ETipo.X, 20, 0 ) );
			line.append( StringFunctions.replicate( " ", 8 ) );
			line.append( (char) 13 );
			line.append( (char) 10 );

		} catch ( Exception e ) {
			throw new ExceptionCnab( "CNAB registro 3 segmento Q.\nErro ao escrever registro.\n" + e.getMessage() );
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
				setTipoInscCli( line.substring( 17, 18 ).trim().length() > 0 ? Integer.parseInt( line.substring( 17, 18 ).trim() ) : 0 );
				setCpfCnpjCli( line.substring( 18, 33 ) );
				setRazCli( line.substring( 33, 73 ) );
				setEndCli( line.substring( 73, 113 ) );
				setBairCli( line.substring( 113, 128 ) );
				setCepCli( line.substring( 128, 136 ) );
				setCidCli( line.substring( 136, 151 ) );
				setUfCli( line.substring( 151, 153 ) );
				setTipoInscAva( line.substring( 153, 154 ).trim().length() > 0 ? Integer.parseInt( line.substring( 153, 154 ).trim() ) : 0 );
				setCpfCnpjAva( line.substring( 154, 169 ) );
				setRazAva( line.substring( 169, 209 ) );
				setCodCompensacao( line.substring( 209, 212 ).trim().length() > 0 ? Integer.parseInt( line.substring( 209, 212 ).trim() ) : 0 );
				setNossoNumero( line.substring( 212, 232 ) );
			}
		} catch ( Exception e ) {
			throw new ExceptionCnab( "CNAB registro 3 segmento Q.\nErro ao ler registro.\n" + e.getMessage() );
		}
	}
}