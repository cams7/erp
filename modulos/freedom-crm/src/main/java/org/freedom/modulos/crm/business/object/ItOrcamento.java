package org.freedom.modulos.crm.business.object;

import java.math.BigDecimal;


public class ItOrcamento {
	
	public static enum INSERT_ITEM_ORC {
		NONE, CODEMP, CODFILIAL, TIPOORC, CODORC, CODITORC, CODEMPPD, CODFILIALPD, CODPROD,  
		CODEMPAX, CODFILIALAX, CODALMOX, QTDITORC, PRECOITORC
	}
	
	public static enum COLITORC {
		SEQITFICHAAVAL, CODAMBALVA, DESCAMBAVAL, DESCITFICHAAVAL, ALTITFICHAAVAL, COMPITFICHAAVAL, M2, CODPROD, DESCPROD, VLRTOTITFICHAAVAL, VLRUNITITFICHAAVAL,
		CODVARARG1, SEQITVARG1, CODVARARG2, SEQITVARG2, CODVARARG3, SEQITVARG3, CODVARARG4, SEQITVARG4, CODVARARG5, SEQITVARG5, CODVARARG6, SEQITVARG6, CODVARARG7, SEQITVARG7
	}
	
	private Integer codemp;
	
	private Integer codfilial;
	
	private String tipoorc;
	
	private Integer codorc;
	
	private Integer coditorc;
	
	private Integer codemppd;
	
	private Integer codfilialpd;
	
	private Integer codprod;
	
	private Integer codempax;
	
	private Integer codfilialax;
	
	private Integer codalmox;
	
	private BigDecimal qtditorc;
	
	private BigDecimal precoitorc;

	
	public Integer getCodemp() {
	
		return codemp;
	}

	
	public void setCodemp( Integer codemp ) {
	
		this.codemp = codemp;
	}

	
	public Integer getCodfilial() {
	
		return codfilial;
	}

	
	public void setCodfilial( Integer codfilial ) {
	
		this.codfilial = codfilial;
	}

	
	
	public String getTipoorc() {
	
		return tipoorc;
	}


	
	public void setTipoorc( String tipoorc ) {
	
		this.tipoorc = tipoorc;
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

	
	
	public Integer getCodempax() {
	
		return codempax;
	}


	
	public void setCodempax( Integer codempax ) {
	
		this.codempax = codempax;
	}


	
	public Integer getCodfilialax() {
	
		return codfilialax;
	}


	
	public void setCodfilialax( Integer codfilialax ) {
	
		this.codfilialax = codfilialax;
	}


	
	public Integer getCodalmox() {
	
		return codalmox;
	}


	
	public void setCodalmox( Integer codalmox ) {
	
		this.codalmox = codalmox;
	}


	public BigDecimal getQtditorc() {
	
		return qtditorc;
	}

	
	public void setQtditorc( BigDecimal qtditorc ) {
	
		this.qtditorc = qtditorc;
	}

	
	public BigDecimal getPrecoitorc() {
	
		return precoitorc;
	}

	
	public void setPrecoitorc( BigDecimal precoitorc ) {
	
		this.precoitorc = precoitorc;
	}
	
	
}
