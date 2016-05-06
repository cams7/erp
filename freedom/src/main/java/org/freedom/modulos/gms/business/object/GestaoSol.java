package org.freedom.modulos.gms.business.object;

import java.math.BigDecimal;


public class GestaoSol {
	public static enum GRID_SOL {   IMGCOLUNA, SITITSOL, SEL, CODPROD, REFPROD, DESCPROD, QTDITSOL, 
		QTDAPROVITSOL, CODSOL, CODITSOL, SLDPROD, CODCC, IDUSUITSOL, IDUSUAPROVITSOL }
	
	public class SelectedSol{
		String sititsol;
		Boolean sel;
		Integer codprod;
		String refprod;
		String descprod;
		BigDecimal qtditsol;
		BigDecimal qtdaprovitsol;
		Integer codsol;
		Integer coditsol;
		BigDecimal sldprod;
		String codcc;
		String idusuitsol;
		String idusuaprovitsol;
		
		public SelectedSol() {

			// TODO Auto-generated constructor stub
		}
		
		public String getSititsol() {
		
			return sititsol;
		}
		
		public void setSititsol( String sititsol ) {
		
			this.sititsol = sititsol;
		}
		
		public Boolean getSel() {
		
			return sel;
		}
		
		public void setSel( Boolean sel ) {
		
			this.sel = sel;
		}
		
		public Integer getCodprod() {
		
			return codprod;
		}
		
		public void setCodprod( Integer codprod ) {
		
			this.codprod = codprod;
		}
		
		public String getRefprod() {
		
			return refprod;
		}
		
		public void setRefprod( String refprod ) {
		
			this.refprod = refprod;
		}
		
		public String getDescprod() {
		
			return descprod;
		}
		
		public void setDescprod( String descprod ) {
		
			this.descprod = descprod;
		}
		
		public BigDecimal getQtditsol() {
		
			return qtditsol;
		}
		
		public void setQtditsol( BigDecimal qtditsol ) {
		
			this.qtditsol = qtditsol;
		}
		
		public BigDecimal getQtdaprovitsol() {
		
			return qtdaprovitsol;
		}
		
		public void setQtdaprovitsol( BigDecimal qtdaprovitsol ) {
		
			this.qtdaprovitsol = qtdaprovitsol;
		}
		
		public Integer getCodsol() {
		
			return codsol;
		}
		
		public void setCodsol( Integer codsol ) {
		
			this.codsol = codsol;
		}
		
		public Integer getCoditsol() {
		
			return coditsol;
		}
		
		public void setCoditsol( Integer coditsol ) {
		
			this.coditsol = coditsol;
		}
		
		public BigDecimal getSldprod() {
		
			return sldprod;
		}
		
		public void setSldprod( BigDecimal sldprod ) {
		
			this.sldprod = sldprod;
		}
		
		public String getCodcc() {
		
			return codcc;
		}
		
		public void setCodcc( String codcc ) {
		
			this.codcc = codcc;
		}
		
		public String getIdusuitsol() {
		
			return idusuitsol;
		}
		
		public void setIdusuitsol( String idusuitsol ) {
		
			this.idusuitsol = idusuitsol;
		}
		
		public String getIdusuaprovitsol() {
		
			return idusuaprovitsol;
		}
		
		public void setIdusuaprovitsol( String idusuaprovitsol ) {
		
			this.idusuaprovitsol = idusuaprovitsol;
		}
				
		
	}

}
