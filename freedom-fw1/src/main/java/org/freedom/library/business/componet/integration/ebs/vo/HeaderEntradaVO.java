package org.freedom.library.business.componet.integration.ebs.vo;

import java.util.Date;

import org.freedom.library.business.componet.integration.ebs.EbsContabil;
import org.freedom.library.functions.Funcoes;

/**
 * 
 * @author ffrizzo
 * since 11/12/2010
 */
public class HeaderEntradaVO {
	private final int tipoRegistro = 0;

	private Date dataArquivo;

	private String cnpj;

	private int calculaBases;

	private int sequencial;

	public HeaderEntradaVO() {
		setCalculaBases(1);
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

	public int getCalculaBases() {
		return calculaBases;
	}

	public void setCalculaBases(int calculaBases) {
		this.calculaBases = calculaBases;
	}

	public int getSequencial() {
		return sequencial;
	}

	public void setSequencial(int sequencial) {
		this.sequencial = sequencial;
	}

	@Override
	public String toString() {

		StringBuilder headerEntrada = new StringBuilder(500);

		headerEntrada.append(getTipoRegistro());
		headerEntrada.append(EbsContabil.format(getDataArquivo()));
		headerEntrada.append(EbsContabil.format(Funcoes.setMascara(getCnpj(), "##.###.###/####-##"), 18));
		headerEntrada.append(getCalculaBases());
		headerEntrada.append(EbsContabil.format(" ", 3));
		headerEntrada.append(EbsContabil.format(" ", 443));
		headerEntrada.append(EbsContabil.format(" ", 20));
		headerEntrada.append(EbsContabil.format(getSequencial(), 6));

		return headerEntrada.toString();
	}
}
