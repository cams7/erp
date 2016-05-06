package org.freedom.modulos.pcp.business.object;

import java.math.BigDecimal;
import java.util.Date;


public class Agrupamento {

	public enum AGRUPAMENTO {
		MARCACAO, STATUS, DATAAPROV, DTFABROP, CODEMPCL, CODFILIALCL, CODCLI, RAZCLI, CODEMPPD, CODFILIALPD, CODPROD, SEQEST, DESCPROD, QTDESTOQUE, QTDRESERVADO, QTDEMPROD, QTDAPROD
	}
	
	private boolean marcacao;
	private boolean status;
	private Date dataaprov;
	private Date dtfabrop;
	private Integer codempcl;
	private Integer codfilialcl;
	private Integer codcli;
	private String razcli;
	private Integer codemppd;
	private Integer codfilialpd;
	private Integer codprod;
	private Integer seqest;
	private String descprod;
	private BigDecimal qtdestoque;
	private BigDecimal qtdreservado;
	private BigDecimal qtdemprod;
	private BigDecimal qtdaprod;
	
	public boolean isMarcacao() {
	
		return marcacao;
	}
	
	public void setMarcacao( boolean marcacao ) {
	
		this.marcacao = marcacao;
	}
	
	public boolean isStatus() {
	
		return status;
	}
	
	public void setStatus( boolean status ) {
	
		this.status = status;
	}
	
	public Date getDataaprov() {
	
		return dataaprov;
	}
	
	public void setDataaprov( Date dataaprov ) {
	
		this.dataaprov = dataaprov;
	}
	
	public Date getDtfabrop() {
	
		return dtfabrop;
	}
	
	public void setDtfabrop( Date dtfabrop ) {
	
		this.dtfabrop = dtfabrop;
	}
	
	public Integer getCodempcl() {
	
		return codempcl;
	}
	
	public void setCodempcl( Integer codempcl ) {
	
		this.codempcl = codempcl;
	}
	
	public Integer getCodfilialcl() {
	
		return codfilialcl;
	}
	
	public void setCodfilialcl( Integer codfilialcl ) {
	
		this.codfilialcl = codfilialcl;
	}
	
	public Integer getCodcli() {
	
		return codcli;
	}
	
	public void setCodcli( Integer codcli ) {
	
		this.codcli = codcli;
	}
	
	public String getRazcli() {
	
		return razcli;
	}
	
	public void setRazcli( String razcli ) {
	
		this.razcli = razcli;
	}
	
	public Integer getCodemppd() {
	
		return codemppd;
	}
	
	public void setCodemppd( Integer codemppd ) {
	
		this.codemppd = codemppd;
	}
	
	public Integer getCodfilialpd() {
	
		return codfilialpd;
	}
	
	public void setCodfilialpd( Integer codfilialpd ) {
	
		this.codfilialpd = codfilialpd;
	}
	
	public Integer getCodprod() {
	
		return codprod;
	}
	
	public void setCodprod( Integer codprod ) {
	
		this.codprod = codprod;
	}
	
	public Integer getSeqest() {
	
		return seqest;
	}
	
	public void setSeqest( Integer seqest ) {
	
		this.seqest = seqest;
	}
	
	public String getDescprod() {
	
		return descprod;
	}
	
	public void setDescprod( String descprod ) {
	
		this.descprod = descprod;
	}
	
	public BigDecimal getQtdestoque() {
	
		return qtdestoque;
	}
	
	public void setQtdestoque( BigDecimal qtdestoque ) {
	
		this.qtdestoque = qtdestoque;
	}
	
	
	public BigDecimal getQtdreservado() {
	
		return qtdreservado;
	}

	
	public void setQtdreservado( BigDecimal qtdreservado ) {
	
		this.qtdreservado = qtdreservado;
	}

	public BigDecimal getQtdemprod() {
	
		return qtdemprod;
	}
	
	public void setQtdemprod( BigDecimal qtdemprod ) {
	
		this.qtdemprod = qtdemprod;
	}
	
	public BigDecimal getQtdaprod() {
	
		return qtdaprod;
	}
	
	public void setQtdaprod( BigDecimal qtdaprod ) {
	
		this.qtdaprod = qtdaprod;
	}
	
}
