package org.freedom.library.business.componet.integration.ebs.vo;

import java.util.Date;

import org.freedom.library.business.componet.integration.ebs.EbsContabil;
import org.freedom.library.functions.Funcoes;

/**
 * 
 * @author ffrizzo
 * since 11/12/2010
 */
public class HeaderSaidaVO {
	
	private final int tipoRegistro = 0;

	private Date dataArquivo;

	private String cnpj;

	private int calculaBases;

	private int sequencial;
	
	public HeaderSaidaVO() {
	}

	public int getTipoRegistro() {
		return tipoRegistro;
	}

	public Date getDataArquivo() {
		return dataArquivo;
	}

	public void setDataArquivo(Date dataArquivo) {
		this.dataArquivo = dataArquivo;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getCpf() {
		return cnpj;
	}

	public int getCalculaBases() {
		return calculaBases;
	}

	/*
	 * public void setCalculaBases( int calculaBases ) { this.calculaBases =
	 * calculaBases; }
	 */

	public int getSequencial() {
		return sequencial;
	}

	public void setSequencial(int sequencial) {
		this.sequencial = sequencial;
	}

	@Override
	public String toString() {

		StringBuilder headerSaida = new StringBuilder(500);

		headerSaida.append(getTipoRegistro());
		headerSaida.append(EbsContabil.format(getDataArquivo()));
		// headerSaida.append( EbsContabil.format( Funcoes.setMascara( getCnpj(),
		// "##.###.###/####-##" ), 18 ) );c

		if (getCnpj() != null) {
			headerSaida.append(EbsContabil.format(Funcoes.setMascara(getCnpj(), "##.###.###/####-##"), 18));
		}
		else {
			headerSaida.append(EbsContabil.format(Funcoes.setMascara(getCpf(), "###.###.###-##"), 18));
		}

		headerSaida.append(EbsContabil.format(getCalculaBases(), 1));
		headerSaida.append(EbsContabil.format(" ", 3));
		headerSaida.append(EbsContabil.format(" ", 443));
		headerSaida.append(EbsContabil.format(" ", 20));
		headerSaida.append(EbsContabil.format(getSequencial(), 6));

		return headerSaida.toString();
	}
}
