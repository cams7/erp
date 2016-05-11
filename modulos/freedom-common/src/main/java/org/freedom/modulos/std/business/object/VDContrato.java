package org.freedom.modulos.std.business.object;

import java.util.Date;


public class VDContrato {
	
	private Integer codEmp; 
	private Integer codFilial; 
	private Integer codContr; 
	private String descContr; 
	private Integer codEmpCl; 
	private Integer codFilialCl; 
	private Integer codCli; 
	private Date dtInicio; 
	private Date dtFim; 
	private String tpCobContr; 
	private Integer diaVencContr; 
	private Integer diaFechContr; 
	private Integer indexContr;
	private String tpcontr;
	private Date dtPrevFin; 
	private String ativo;
	
	public Integer getCodEmp() {
	
		return codEmp;
	}
	
	public void setCodEmp( Integer codEmp ) {
	
		this.codEmp = codEmp;
	}
	
	public Integer getCodFilial() {
	
		return codFilial;
	}
	
	public void setCodFilial( Integer codFilial ) {
	
		this.codFilial = codFilial;
	}
	
	public Integer getCodContr() {
	
		return codContr;
	}
	
	public void setCodContr( Integer codContr ) {
	
		this.codContr = codContr;
	}
	
	public String getDescContr() {
	
		return descContr;
	}
	
	public void setDescContr( String descContr ) {
	
		this.descContr = descContr;
	}
	
	public Integer getCodEmpCl() {
	
		return codEmpCl;
	}
	
	public void setCodEmpCl( Integer codEmpCl ) {
	
		this.codEmpCl = codEmpCl;
	}
	
	public Integer getCodFilialCl() {
	
		return codFilialCl;
	}
	
	public void setCodFilialCl( Integer codFilialCl ) {
	
		this.codFilialCl = codFilialCl;
	}
	
	public Integer getCodCli() {
	
		return codCli;
	}
	
	public void setCodCli( Integer codCli ) {
	
		this.codCli = codCli;
	}
	
	public Date getDtInicio() {
	
		return dtInicio;
	}
	
	public void setDtInicio( Date dtInicio ) {
	
		this.dtInicio = dtInicio;
	}
	
	public Date getDtFim() {
	
		return dtFim;
	}
	
	public void setDtFim( Date dtFim ) {
	
		this.dtFim = dtFim;
	}
	
	public String getTpCobContr() {
	
		return tpCobContr;
	}
	
	public void setTpCobContr( String tpCobContr ) {
	
		this.tpCobContr = tpCobContr;
	}
	
	public Integer getDiaVencContr() {
	
		return diaVencContr;
	}
	
	public void setDiaVencContr( Integer diaVencContr ) {
	
		this.diaVencContr = diaVencContr;
	}
	
	public Integer getDiaFechContr() {
	
		return diaFechContr;
	}
	
	public void setDiaFechContr( Integer diaFechContr ) {
	
		this.diaFechContr = diaFechContr;
	}
	
	public Integer getIndexContr() {
	
		return indexContr;
	}
	
	public void setIndexContr( Integer indexContr ) {
	
		this.indexContr = indexContr;
	}
	
	public Date getDtPrevFin() {
	
		return dtPrevFin;
	}
	
	public void setDtPrevFin( Date dtPrevFin ) {
	
		this.dtPrevFin = dtPrevFin;
	}
	
	
	public String getTpcontr() {
	
		return tpcontr;
	}

	
	public void setTpcontr( String tpcontr ) {
	
		this.tpcontr = tpcontr;
	}

	public String getAtivo() {
	
		return ativo;
	}
	
	public void setAtivo( String ativo ) {
	
		this.ativo = ativo;
	} 
}
