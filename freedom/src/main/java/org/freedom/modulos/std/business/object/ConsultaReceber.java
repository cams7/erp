package org.freedom.modulos.std.business.object;

import java.math.BigDecimal;
import java.util.Date;

public class ConsultaReceber {
	
	private BigDecimal vlrtotvendliq;
	
	private BigDecimal vlrtotpago;
	
	private BigDecimal vlrtotvendbrut;
	
	private BigDecimal vlrtotaberto;
	
	private Date primcompra;
	
	private Date ultcompra;
	
	private BigDecimal vlrmaxfat;
	
	private Date datamaxfat;
	
	private String datamaxacum;
	
	private BigDecimal vlrmaxacum;
	
	public BigDecimal getVlrtotvendliq() {
	
		return vlrtotvendliq;
	}
	
	public void setVlrtotvendliq( BigDecimal vlrtotvendliq ) {
	
		this.vlrtotvendliq = vlrtotvendliq;
	}
	
	public BigDecimal getVlrtotpago() {
	
		return vlrtotpago;
	}
	
	public void setVlrtotpago( BigDecimal vlrtotpago ) {
	
		this.vlrtotpago = vlrtotpago;
	}
	
	public BigDecimal getVlrtotvendbrut() {
	
		return vlrtotvendbrut;
	}
	
	public void setVlrtotvendbrut( BigDecimal vlrtotvendbrut ) {
	
		this.vlrtotvendbrut = vlrtotvendbrut;
	}
	
	public BigDecimal getVlrtotaberto() {
	
		return vlrtotaberto;
	}
	
	public void setVlrtotaberto( BigDecimal vlrtotaberto ) {
	
		this.vlrtotaberto = vlrtotaberto;
	}
	
	public Date getPrimcompra() {
	
		return primcompra;
	}
	
	public void setPrimcompra( Date primcompra ) {
	
		this.primcompra = primcompra;
	}
	
	public Date getUltcompra() {
	
		return ultcompra;
	}
	
	public void setUltcompra( Date ultcompra ) {
	
		this.ultcompra = ultcompra;
	}
	
	public BigDecimal getVlrmaxfat() {
	
		return vlrmaxfat;
	}
	
	public void setVlrmaxfat( BigDecimal vlrmaxfat ) {
	
		this.vlrmaxfat = vlrmaxfat;
	}
	
	public Date getDatamaxfat() {
	
		return datamaxfat;
	}
	
	public void setDatamaxfat( Date datamaxfat ) {
	
		this.datamaxfat = datamaxfat;
	}
	
	public String getDatamaxacum() {
	
		return datamaxacum;
	}
	
	public void setDatamaxacum( String datamaxacum ) {
	
		this.datamaxacum = datamaxacum;
	}
	
	public BigDecimal getVlrmaxacum() {
	
		return vlrmaxacum;
	}
	
	public void setVlrmaxacum( BigDecimal vlrmaxacum ) {
	
		this.vlrmaxacum = vlrmaxacum;
	}
}
