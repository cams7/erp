package org.freedom.modulos.pcp.business.object;

import java.math.BigDecimal;
import java.util.Date;



public class PPGeraOP {

	public enum PROCEDUREOP {
		NONE, TIPOPROCESS, CODEMPOP, CODFILIALOP, CODOP, SEQOP, CODEMPPD, CODFILIALPD, CODPROD, CODEMPOC, CODFILIALOC, CODORC, TIPOORC, CODITORC,
		QTDSUGPRODOP, DTFABROP, SEQEST, CODEMPET, CODFILIALET, CODEST, AGRUPDATAAPROV, AGRUPDTFABROP, AGRUPCODCLI, CODEMPCL, CODFILIALCL, CODCLI, 
		DATAAPROV, CODEMPCP, CODFILIALCP, CODCOMPRA, CODITCOMPRA, JUSTFICQTDPROD, CODEMPPDENTRADA, CODFILIALPDENTRADA, CODPRODENTRADA, QTDENTRADA
	}

	private String tipoprocess;
	private Integer codempop;
	private Integer codfilialop;
	private Integer codop;
	private Integer seqop;
	private Integer codemppd;
	private Integer codfilialpd;
	private Integer codprod;
	private Integer codempoc;
	private Integer codfilialoc;
	private Integer codorc;
	private Integer coditorc;
	private String tipoorc;
	private BigDecimal qtdSugProdOp;
	private Date dtFabOp;
	private Integer seqest;
	private Integer codempet;
	private Integer codfilialet;
	private Integer codest;
	private String agrupdataaprov;
	private String agrupdtfabrop;
	private String agrupcodcli;
	private Integer codempcl;
	private Integer codfilialcl;
	private Integer codcli;
	private Date dataaprov;
	private Integer codempcp;
	private Integer codfilialcp;
	private String justficqtdprod;
	private Integer codemppdentrada;
	private Integer codfilialpdentrada;
	private Integer codprodentrada;
	private BigDecimal qtdentrada;
	
	public String getTipoprocess() {
	
		return tipoprocess;
	}
	
	public void setTipoprocess( String tipoprocess ) {
	
		this.tipoprocess = tipoprocess;
	}
	
	public Integer getCodempop() {
	
		return codempop;
	}
	
	public void setCodempop( Integer codempop ) {
	
		this.codempop = codempop;
	}
	
	public Integer getCodfilialop() {
	
		return codfilialop;
	}
	
	public void setCodfilialop( Integer codfilialop ) {
	
		this.codfilialop = codfilialop;
	}
	
	
	public Integer getCodop() {
	
		return codop;
	}

	
	public void setCodop( Integer codop ) {
	
		this.codop = codop;
	}

	
	public Integer getSeqop() {
	
		return seqop;
	}

	
	public void setSeqop( Integer seqop ) {
	
		this.seqop = seqop;
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
	
	public Integer getCodempoc() {
	
		return codempoc;
	}
	
	public void setCodempoc( Integer codempoc ) {
	
		this.codempoc = codempoc;
	}
	
	public Integer getCodfilialoc() {
	
		return codfilialoc;
	}
	
	public void setCodfilialoc( Integer codfilialoc ) {
	
		this.codfilialoc = codfilialoc;
	}
	
	public Integer getCodorc() {
	
		return codorc;
	}
	
	public void setCodorc( Integer codorc ) {
	
		this.codorc = codorc;
	}
	
	public Integer getCoditorc() {
	
		return coditorc;
	}
	
	public void setCoditorc( Integer coditorc ) {
	
		this.coditorc = coditorc;
	}
	
	public String getTipoorc() {
	
		return tipoorc;
	}
	
	public void setTipoorc( String tipoorc ) {
	
		this.tipoorc = tipoorc;
	}
	
	public BigDecimal getQtdSugProdOp() {
	
		return qtdSugProdOp;
	}
	
	public void setQtdSugProdOp( BigDecimal qtdSugProdOp ) {
	
		this.qtdSugProdOp = qtdSugProdOp;
	}
	
	public Date getDtFabOp() {
	
		return dtFabOp;
	}
	
	public void setDtFabOp( Date dtFabOp ) {
	
		this.dtFabOp = dtFabOp;
	}
	
	public Integer getSeqest() {
	
		return seqest;
	}
	
	public void setSeqest( Integer seqest ) {
	
		this.seqest = seqest;
	}
	
	public Integer getCodempet() {
	
		return codempet;
	}
	
	public void setCodempet( Integer codempet ) {
	
		this.codempet = codempet;
	}
	
	public Integer getCodfilialet() {
	
		return codfilialet;
	}
	
	public void setCodfilialet( Integer codfilialet ) {
	
		this.codfilialet = codfilialet;
	}
	
	public Integer getCodest() {
	
		return codest;
	}
	
	public void setCodest( Integer codest ) {
	
		this.codest = codest;
	}
	
	public String getAgrupdataaprov() {
	
		return agrupdataaprov;
	}
	
	public void setAgrupdataaprov( String agrupdataaprov ) {
	
		this.agrupdataaprov = agrupdataaprov;
	}
	
	public String getAgrupdtfabrop() {
	
		return agrupdtfabrop;
	}
	
	public void setAgrupdtfabrop( String agrupdtfabrop ) {
	
		this.agrupdtfabrop = agrupdtfabrop;
	}
	
	public String getAgrupcodcli() {
	
		return agrupcodcli;
	}
	
	public void setAgrupcodcli( String agrupcodcli ) {
	
		this.agrupcodcli = agrupcodcli;
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

	public Date getDataaprov() {
	
		return dataaprov;
	}
	
	public void setDataaprov( Date dataaprov ) {
	
		this.dataaprov = dataaprov;
	}
	
	public Integer getCodempcp() {
	
		return codempcp;
	}
	
	public void setCodempcp( Integer codempcp ) {
	
		this.codempcp = codempcp;
	}
	
	public Integer getCodfilialcp() {
	
		return codfilialcp;
	}
	
	public void setCodfilialcp( Integer codfilialcp ) {
	
		this.codfilialcp = codfilialcp;
	}
	
	public String getJustficqtdprod() {
	
		return justficqtdprod;
	}
	
	public void setJustficqtdprod( String justficqtdprod ) {
	
		this.justficqtdprod = justficqtdprod;
	}
	
	public Integer getCodemppdentrada() {
	
		return codemppdentrada;
	}
	
	public void setCodemppdentrada( Integer codemppdentrada ) {
	
		this.codemppdentrada = codemppdentrada;
	}
	
	public Integer getCodfilialpdentrada() {
	
		return codfilialpdentrada;
	}
	
	public void setCodfilialpdentrada( Integer codfilialpdentrada ) {
	
		this.codfilialpdentrada = codfilialpdentrada;
	}
	
	public Integer getCodprodentrada() {
	
		return codprodentrada;
	}
	
	public void setCodprodentrada( Integer codprodentrada ) {
	
		this.codprodentrada = codprodentrada;
	}
	
	public BigDecimal getQtdentrada() {
	
		return qtdentrada;
	}
	
	public void setQtdentrada( BigDecimal qtdentrada ) {
	
		this.qtdentrada = qtdentrada;
	}
	
	
	
}
