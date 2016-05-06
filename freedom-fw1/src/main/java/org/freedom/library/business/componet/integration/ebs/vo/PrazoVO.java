package org.freedom.library.business.componet.integration.ebs.vo;

import java.math.BigDecimal;
import java.util.Date;

import org.freedom.library.business.componet.integration.ebs.EbsContabil;

public class PrazoVO {
	
	private final int tipoRegistro = 9;
	
	private String tipoParcela;
	
	private int nrFatura;
	
	private String tipoTitulo;

	private Date dtVencimento;
	
	private BigDecimal vlrParcela;
	
	private int sequencial;

	public String getTipoParcela() {
		return tipoParcela;
	}

	public void setTipoParcela(String tipoParcela) {
		this.tipoParcela = tipoParcela;
	}

	public int getNrFatura() {
		return nrFatura;
	}

	public void setNrFatura(int nrFatura) {
		this.nrFatura = nrFatura;
	}

	public String getTipoTitulo() {
		return tipoTitulo;
	}

	public void setTipoTitulo(String tipoTitulo) {
		this.tipoTitulo = tipoTitulo;
	}

	public Date getDtVencimento() {
		return dtVencimento;
	}

	public void setDtVencimento(Date dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public BigDecimal getVlrParcela() {
		return vlrParcela;
	}

	public void setVlrParcela(BigDecimal vlrParcela) {
		this.vlrParcela = vlrParcela;
	}

	public int getSequencial() {
		return sequencial;
	}

	public void setSequencial(int sequencial) {
		this.sequencial = sequencial;
	}

	public int getTipoRegistro() {
		return tipoRegistro;
	}
	
	@Override
	public String toString() {

		StringBuilder prazo = new StringBuilder();
		
		prazo.append(getTipoRegistro());
		prazo.append(EbsContabil.format(getTipoParcela(),1));
		prazo.append(EbsContabil.format(getNrFatura(), 9));
		prazo.append(EbsContabil.format(getTipoTitulo(),2 ));
		prazo.append(EbsContabil.format(getDtVencimento() ));
		prazo.append(EbsContabil.format(getVlrParcela(), 12, 2 ));
		prazo.append(EbsContabil.format(" ", 456));
		prazo.append(EbsContabil.format(" ", 5));
		prazo.append(EbsContabil.format(getSequencial(), 6));

		return prazo.toString();		
	}
	
}
