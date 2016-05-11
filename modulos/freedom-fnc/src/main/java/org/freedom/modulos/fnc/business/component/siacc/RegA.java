package org.freedom.modulos.fnc.business.component.siacc;

import java.util.Date;
import java.util.Map;

import org.freedom.library.business.exceptions.ExceptionSiacc;
import org.freedom.library.functions.Funcoes;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.EPrefs;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.ETipo;


public class RegA extends Reg {
	
	public static final char CODREG = 'A'; // registro A.01

	private Integer codRemessa = null; // registro A.02

	private String codConvenio = null; // registro A.03

	private String nomeEmpresa = null; // registro A.04

	private Integer codBanco = null; // registro A.05

	private String nomeBanco = null; // registro A.06

	private Integer dataMovimento = null; // registro A.07

	private Integer seqArquivo = null; // registro A.08

	private Integer versao = null; // registro A.09

	private String identServ = null; // registro A.10

	private String contaComp = null; // registro A.11

	private String identAmbCli = null; // registro A.12

	private String identAmbBco = null; // registro A.13

	// private String A14 = null; // registro A.14
	private Integer seqRegistro = null; // registro A.15

	// private String A16 = null; // registro A.16

	public RegA( final String line ) throws ExceptionSiacc {

		super( CODREG );
		parseLine( line );
	}

	// @SuppressWarnings("unchecked")
	public RegA( final char codrem, final Map<Enum<EPrefs>, Object> prefs, final int numReg ) {

		super( CODREG );
		this.sbreg.append( codrem );
		this.sbreg.append( format( prefs.get( EPrefs.CODCONV ), ETipo.X, 20, 0 ) );
		this.sbreg.append( format( prefs.get( EPrefs.NOMEEMP ), ETipo.X, 20, 0 ) );
		this.sbreg.append( format( prefs.get( EPrefs.CODBANCO ), ETipo.$9, 3, 0 ) );
		this.sbreg.append( format( prefs.get( EPrefs.NOMEBANCO ), ETipo.X, 20, 0 ) );
		this.sbreg.append( Funcoes.dataAAAAMMDD( new Date() ) );
		this.sbreg.append( format( prefs.get( EPrefs.NROSEQ ), ETipo.$9, 6, 0 ) );
		this.sbreg.append( format( prefs.get( EPrefs.VERLAYOUT ), ETipo.$9, 2, 0 ) );
		this.sbreg.append( format( prefs.get( EPrefs.IDENTSERV ), ETipo.X, 17, 0 ) );
		this.sbreg.append( format( prefs.get( EPrefs.CONTACOMPR ), ETipo.$9, 16, 0 ) );
		this.sbreg.append( format( prefs.get( EPrefs.IDENTAMBCLI ), ETipo.X, 1, 0 ) );
		this.sbreg.append( format( prefs.get( EPrefs.IDENTAMBBCO ), ETipo.X, 1, 0 ) );
		this.sbreg.append( format( "", ETipo.X, 27, 0 ) ); // Reservado para o futuro
		this.sbreg.append( format( numReg, ETipo.$9, 6, 0 ) ); // Número sequencial do registro
		this.sbreg.append( format( "", ETipo.X, 1, 0 ) ); // Reservado para o futuro
		this.sbreg.append( (char) 13 );
		this.sbreg.append( (char) 10 );
	}

	protected void parseLine( final String line ) throws ExceptionSiacc {

		try {
			setCodRemessa( line.substring( 1, 2 ).trim().length() > 0 ? new Integer( line.substring( 1, 2 ).trim() ) : null );
			setCodConvenio( line.substring( 2, 22 ) );
			setNomeEmpresa( line.substring( 22, 42 ) );
			setCodBanco( line.substring( 42, 45 ).trim().length() > 0 ? new Integer( line.substring( 42, 45 ).trim() ) : null );
			setNomeBanco( line.substring( 45, 65 ) );
			setDataMovimento( line.substring( 65, 73 ).trim().length() > 0 ? new Integer( line.substring( 65, 73 ).trim() ) : null );
			setSeqArquivo( line.substring( 73, 79 ).trim().length() > 0 ? new Integer( line.substring( 73, 79 ).trim() ) : null );
			setVersao( line.substring( 79, 81 ).trim().length() > 0 ? new Integer( line.substring( 79, 81 ).trim() ) : null );
			setIdentServ( line.substring( 81, 98 ) );
			setContaComp( line.substring( 98, 114 ) );
			setIdentAmbCli( line.substring( 114, 115 ) );
			setIdentAmbBco( line.substring( 115, 116 ) );
			// A14 = line.substring( 116, 143 );
			setSeqRegistro( line.substring( 143, 149 ).trim().length() > 0 ? new Integer( line.substring( 143, 149 ).trim() ) : null );
			// A16 = line.substring( 149 );
		} catch ( Exception e ) {
			throw new ExceptionSiacc( "Erro no registro tipo A!\n" + e.getMessage() );
		}
	}

	public Integer getCodBanco() {

		return codBanco;
	}

	public void setCodBanco( final Integer codBanco ) {

		this.codBanco = codBanco;
	}

	public String getCodConvenio() {

		return codConvenio;
	}

	public void setCodConvenio( final String codConvenio ) {

		this.codConvenio = codConvenio;
	}

	public Integer getCodRemessa() {

		return codRemessa;
	}

	public void setCodRemessa( final Integer codRemessa ) {

		this.codRemessa = codRemessa;
	}

	public String getContaComp() {

		return contaComp;
	}

	public void setContaComp( final String contaComp ) {

		this.contaComp = contaComp;
	}

	public Integer getDataMovimento() {

		return dataMovimento;
	}

	public void setDataMovimento( final Integer dataMovimento ) {

		this.dataMovimento = dataMovimento;
	}

	public String getIdentAmbCaixa() {

		return identAmbBco;
	}

	public void setIdentAmbBco( final String identAmbCaixa ) {

		this.identAmbBco = identAmbCaixa;
	}

	public String getIdentAmbCli() {

		return identAmbCli;
	}

	public void setIdentAmbCli( final String identAmbCli ) {

		this.identAmbCli = identAmbCli;
	}

	public String getIdentServ() {

		return identServ;
	}

	public void setIdentServ( final String identServ ) {

		this.identServ = identServ;
	}

	public String getNomeBanco() {

		return nomeBanco;
	}

	public void setNomeBanco( final String nomeBanco ) {

		this.nomeBanco = nomeBanco;
	}

	public String getNomeEmpresa() {

		return nomeEmpresa;
	}

	public void setNomeEmpresa( final String nomeEmpresa ) {

		this.nomeEmpresa = nomeEmpresa;
	}

	public Integer getSeqArquivo() {

		return seqArquivo;
	}

	public void setSeqArquivo( final Integer seqArquivo ) {

		this.seqArquivo = seqArquivo;
	}

	public Integer getSeqRegistro() {

		return seqRegistro;
	}

	public void setSeqRegistro( final Integer seqRegistro ) {

		this.seqRegistro = seqRegistro;
	}

	public Integer getVersao() {

		return versao;
	}

	public void setVersao( final Integer versao ) {

		this.versao = versao;
	}
}
