package org.freedom.modulos.fnc.business.component.siacc;

import java.math.BigDecimal;

import org.freedom.library.business.exceptions.ExceptionSiacc;
import org.freedom.library.functions.Funcoes;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.EColrec;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.ETipo;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.StuffRec;


public class RegE extends Reg {
	private final char COD_MOV = '0';

	private final String COD_MOEDA = "03";

	private static final char CODREG = 'E'; // registro E.01

	private String identCliEmp = null; // registro E.02

	private Integer agenciaDebCred = null; // registro E.03

	private String identCliBanco = null; // registro E.04

	private Integer dataVenc = null; // registro E.05

	private Long valorDebCred = null; // registro E.06

	private String codMoeda = null; // registro E.07

	private String usoEmp = null; // registro E.08

	private Integer numAgendCli = null; // registro E.09

	// private Integer E10 = null; // registro E.10
	private Integer seqRegistro = null; // registro E.11

	private Integer codMovimento = null; // registro E.12

	private BigDecimal vlrParc = null;

	private Integer codRec = null;

	private Integer nparcItRec = null;

	public RegE( final String line ) throws ExceptionSiacc {

		super( CODREG );
		parseLine( line );
	}

	public RegE( final char codreg, final StuffRec stfRec, final int numSeq, final int numAgenda ) {

		super( codreg );
		setCodRec( stfRec.getCodrec() );
		setNparcItRec( stfRec.getNParcitrec() );
		setUsoEmp( format( getCodRec(), ETipo.$9, 6, 0 ) + format( getNparcItRec(), ETipo.$9, 4, 0 ) );
		setVlrParc( new BigDecimal( stfRec.getArgs()[ EColrec.VLRAPAG.ordinal() ] ) );
		setIdentCliEmp( stfRec.getArgs()[ EColrec.PESSOACLI.ordinal() ], stfRec.getArgs()[ EColrec.CPFCLI.ordinal() ], stfRec.getArgs()[ EColrec.CNPJCLI.ordinal() ], stfRec.getArgs()[ EColrec.IDENTCLIBCO.ordinal() ],stfRec.getArgs()[ EColrec.CODCLI.ordinal() ]  );
		this.sbreg.append( getIdentCliEmp() );
		this.sbreg.append( format( stfRec.getArgs()[ EColrec.AGENCIACLI.ordinal() ], ETipo.$9, 4, 0 ) );
		this.sbreg.append( format( stfRec.getArgs()[ EColrec.IDENTCLI.ordinal() ], ETipo.X, 14, 0 ) );
		this.sbreg.append( format( stfRec.getArgs()[ EColrec.DTVENC.ordinal() ], ETipo.$9, 8, 0 ) );
		this.sbreg.append( format( Funcoes.transValor( vlrParc, 15, 2, true ), ETipo.$9, 15, 0 ) );
		this.sbreg.append( COD_MOEDA );
		this.sbreg.append( format( getUsoEmp(), ETipo.X, 60, 0 ) ); // Uso da empresa
		this.sbreg.append( format( numAgenda, ETipo.$9, 6, 0 ) );
		this.sbreg.append( format( "", ETipo.X, 8, 0 ) ); // Reservado para o futuro
		this.sbreg.append( format( numSeq, ETipo.$9, 6, 0 ) ); // Reservado para o futuro
		this.sbreg.append( stfRec.getArgs()[ EColrec.CODMOVIMENTO.ordinal() ] );
		this.sbreg.append( (char) 13 );
		this.sbreg.append( (char) 10 );
	}

	protected void parseLine( final String line ) throws ExceptionSiacc {

		try {
			setIdentCliEmp( line.substring( 1, 26 ) );
			setAgenciaDebCred( line.substring( 26, 30 ).trim().length() > 0 ? new Integer( line.substring( 26, 30 ) ) : null );
			setIdentCliBanco( line.substring( 30, 44 ) );
			setDataVenc( line.substring( 44, 52 ).trim().length() > 0 ? new Integer( line.substring( 44, 52 ) ) : null );
			setValorDebCred( line.substring( 52, 67 ).trim().length() > 0 ? new Long( line.substring( 52, 67 ) ) : null );
			setCodMoeda( line.substring( 67, 69 ) );
			setUsoEmp( line.substring( 69, 129 ) );
			setNumAgendCli( line.substring( 129, 135 ).trim().length() > 0 ? new Integer( line.substring( 129, 135 ) ) : null );
			// E10( line.substring( 135, 143 ) );
			setSeqRegistro( line.substring( 143, 149 ).trim().length() > 0 ? new Integer( line.substring( 143, 149 ) ) : null );
			setCodMovimento( line.substring( 149 ).trim().length() > 0 ? new Integer( line.substring( 149 ) ) : null );
		} catch ( Exception e ) {
			throw new ExceptionSiacc( "Erro na leitura do registro E!\n" + e.getMessage() );
		}
	}

	public Integer getAgenciaDebCred() {

		return agenciaDebCred;
	}

	public void setAgenciaDebCred( final Integer agenciaDebCred ) {

		this.agenciaDebCred = agenciaDebCred;
	}

	public String getCodMoeda() {

		return codMoeda;
	}

	public void setCodMoeda( final String codMoeda ) {

		this.codMoeda = codMoeda;
	}

	public Integer getCodMovimento() {

		return codMovimento;
	}

	public void setCodMovimento( final Integer codMovimento ) {

		this.codMovimento = codMovimento;
	}

	public Integer getDataVenc() {

		return dataVenc;
	}

	public void setDataVenc( final Integer dataVenc ) {

		this.dataVenc = dataVenc;
	}

	public String getIdentCliBanco() {

		return identCliBanco;
	}

	public void setIdentCliBanco( final String identCliBanco ) {

		this.identCliBanco = identCliBanco;
	}

	public String getIdentCliEmp() {

		return identCliEmp;
	}

	public void setIdentCliEmp( final String pessoaCli, final String cpfCli, final String cnpjCli, final String identCliBco, final String codcli ) {
		if("D".equals( identCliBco )){
			String tmpIdent = null;
			if ( "F".equals( pessoaCli ) ) {
				tmpIdent = format( cpfCli, ETipo.$9, 11, 0 );
			}
			else {
				tmpIdent = format( cnpjCli, ETipo.$9, 14, 0 );
			}
			tmpIdent += format( "0", ETipo.$9, 25 - tmpIdent.length(), 0 );
			setIdentCliEmp( tmpIdent );
		} else {
			
			setIdentCliEmp( format(  codcli, ETipo.X, 25, 0 ) );	
		}
	}

	public void setIdentCliEmp( final String identCliEmp ) {

		this.identCliEmp = identCliEmp;
	}

	public Integer getNumAgendCli() {

		return numAgendCli;
	}

	public void setNumAgendCli( final Integer numAgendCli ) {

		this.numAgendCli = numAgendCli;
	}

	public Integer getSeqRegistro() {

		return seqRegistro;
	}

	public void setSeqRegistro( final Integer seqRegistro ) {

		this.seqRegistro = seqRegistro;
	}

	public String getUsoEmp() {

		return usoEmp;
	}

	public void setUsoEmp( final String usoEmp ) {

		this.usoEmp = usoEmp;
		String codrec = this.usoEmp.substring( 0, 6 );
		String numparcrec = this.usoEmp.substring( 6, 10 );
		if ( codrec != null && codrec.trim().length() > 0 ) {
			setCodRec( Integer.parseInt( codrec.trim() ) );
		}
		if ( numparcrec != null && numparcrec.trim().length() > 0 ) {
			setNparcItRec( Integer.parseInt( numparcrec.trim() ) );
		}
	}

	public Long getValorDebCred() {

		return valorDebCred;
	}

	public void setValorDebCred( final Long valorDebCred ) {

		this.valorDebCred = valorDebCred;
	}

	public BigDecimal getVlrParc() {

		return vlrParc;
	}

	public void setVlrParc( BigDecimal vlrParc ) {

		this.vlrParc = vlrParc;
	}

	public Integer getCodRec() {

		return codRec;
	}

	public void setCodRec( final Integer codRec ) {

		this.codRec = codRec;
	}

	public Integer getNparcItRec() {

		return nparcItRec;
	}

	public void setNparcItRec( final Integer nparcItRec ) {

		this.nparcItRec = nparcItRec;
	}
}
