package org.freedom.modulos.fnc.business.component.siacc;

import java.util.Date;

import org.freedom.library.business.exceptions.ExceptionSiacc;
import org.freedom.library.functions.Funcoes;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.EColcli;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.ETipo;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.StuffCli;


public class RegB extends Reg {
	private final char COD_MOV = '2';

	private static final char CODREG = 'B'; // registro B.01

	private String identCliEmp = null; // registro B.02

	private Integer agenciaDebt = null; // registro B.03

	private String identCliBco = null; // registro B.04

	private Integer dataOpcao = null; // registro B.05

	// private String B06 = null; // registro B.06
	private Integer codMovimento = null; // registro B.07

	// private Integer B08 = null; // registro B.08

	public RegB( final String line ) throws ExceptionSiacc {
	super( CODREG );
	parseLine( line );
}

public RegB( final char codreg, final StuffCli stfCli ) {

	super( codreg );
	this.sbreg.append( format( stfCli.getCodigo(), ETipo.$9, 10, 0 ) );
	this.sbreg.append( format( "", ETipo.X, 15, 0 ) ); // Completar a identificação do cliente na empresa
	this.sbreg.append( format( stfCli.getArgs()[ EColcli.AGENCIACLI.ordinal() ], ETipo.$9, 4, 0 ) );
	this.sbreg.append( format( stfCli.getArgs()[ EColcli.IDENTCLI.ordinal() ], ETipo.X, 14, 0 ) );
	this.sbreg.append( Funcoes.dataAAAAMMDD( new Date() ) );
	this.sbreg.append( format( "", ETipo.X, 96, 0 ) ); // Reservado para o futuro
	this.sbreg.append( COD_MOV );
	this.sbreg.append( format( "", ETipo.X, 1, 0 ) ); // Reservado para o futuro
	this.sbreg.append( (char) 13 );
	this.sbreg.append( (char) 10 );
}

protected void parseLine( final String line ) throws ExceptionSiacc {

	try {
		setIdentCliEmp( line.substring( 1, 26 ) );
		setAgenciaDebt( line.substring( 26, 30 ).trim().length() > 0 ? new Integer( line.substring( 26, 30 ).trim() ) : null );
		setIdentCliBanco( line.substring( 30, 44 ) );
		setDataOpcao( line.substring( 44, 52 ).trim().length() > 0 ? new Integer( line.substring( 44, 52 ).trim() ) : null );
		// B06 = line.substring( 52, 148 );
		setCodMovimento( line.substring( 148, 149 ).trim().length() > 0 ? new Integer( line.substring( 148, 149 ).trim() ) : null );
		// B08 = line.substring( 149 ).trim().length() > 0 ? new Integer( line.substring( 149 ).trim() ) : null;
	} catch ( Exception e ) {
		throw new ExceptionSiacc( "Erro na leitura do registro B!\n" + e.getMessage() );
	}
}

public Integer getAgenciaDebt() {

	return agenciaDebt;
}

public void setAgenciaDebt( final Integer agenciaDebt ) {

	this.agenciaDebt = agenciaDebt;
}

public Integer getCodMovimento() {

	return codMovimento;
}

public void setCodMovimento( final Integer codMovimento ) {

	this.codMovimento = codMovimento;
}

public java.util.Date getDataOpcao() throws Exception {

	return SiaccUtil.strToDate( String.valueOf( dataOpcao ) );
}

public void setDataOpcao( final Integer dataOpcao ) {

	this.dataOpcao = dataOpcao;
}

public String getIdentCliBco() {

	return identCliBco;
}

public void setIdentCliBanco( final String identCliBanco ) {

	this.identCliBco = identCliBanco;
}

public String getIdentCliEmp() {

	return identCliEmp;
}

public void setIdentCliEmp( final String identCliEmp ) {

	this.identCliEmp = identCliEmp;
}
}
