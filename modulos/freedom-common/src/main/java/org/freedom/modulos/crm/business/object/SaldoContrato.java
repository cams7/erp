package org.freedom.modulos.crm.business.object;

import java.math.BigDecimal;


public class SaldoContrato implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private BigDecimal qtditcontr = null;
	private BigDecimal qtdhoras = null;
	private BigDecimal saldomes = null;
	private int mes = 0;
	private int ano = 0;
	private BigDecimal excedentemescob = null;
	private BigDecimal excedentemes = null;
	
	public BigDecimal getQtditcontr() {
	
		return qtditcontr;
	}
	
	public BigDecimal getQtdhoras() {
	
		return qtdhoras;
	}
	
	public BigDecimal getSaldomes() {
	
		return saldomes;
	}
	
	public int getMes() {
	
		return mes;
	}
	
	public int getAno() {
	
		return ano;
	}
	
	public BigDecimal getExcedentemescob() {
	
		return excedentemescob;
	}
	
	public BigDecimal getExcedentemes() {
	
		return excedentemes;
	}
	
	public void setQtditcontr( BigDecimal qtditcontr ) {
	
		this.qtditcontr = qtditcontr;
	}
	
	public void setQtdhoras( BigDecimal qtdhoras ) {
	
		this.qtdhoras = qtdhoras;
	}
	
	public void setSaldomes( BigDecimal saldomes ) {
	
		this.saldomes = saldomes;
	}
	
	public void setMes( int mes ) {
	
		this.mes = mes;
	}
	
	public void setAno( int ano ) {
	
		this.ano = ano;
	}
	
	public void setExcedentemescob( BigDecimal excedentemescob ) {
	
		this.excedentemescob = excedentemescob;
	}
	
	public void setExcedentemes( BigDecimal excedentemes ) {
	
		this.excedentemes = excedentemes;
	}



}
