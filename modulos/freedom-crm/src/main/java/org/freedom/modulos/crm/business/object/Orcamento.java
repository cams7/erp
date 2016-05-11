package org.freedom.modulos.crm.business.object;

import java.math.BigDecimal;
import java.util.Date;


public class Orcamento {
	
	public static enum GET_ORC { 
		CODORC, CODCLI, DTEMISSAO, DTVENC, CODPAG, VLRPRODORC, TIPOORC, QTDITENS
	}
	
	public static enum INSERT_ORC {
		NONE, CODEMP, CODFILIAL, TIPOORC, CODORC, DTORC, DTVENCORC, CODEMPCL, CODFILIALCL, CODCLI, CODEMPVD, 
		CODFILIALVD, CODVEND, CODEMPPG, CODFILIALPG, CODPLANOPAG, CODEMPTN, CODFILIALTN, CODTRAN, STATUSORC, VLRPRODORC
	}
	
	
	private Integer codemp;
	
	private Integer codfilial;
	
	private Integer codorc;
	
	private Integer coditorc;
	
	private BigDecimal qtditorc;
	
	private BigDecimal precoitorc;
	
	private String tipoorc;
	
	private Integer codempcl;
	
	private Integer codfilialcl;
	
	private Integer codcli;
	
	private Date dtorc;
	
	private Date dtvencorc;
	
	private Integer codempvd;
	
	private Integer codfilialvd;
	
	private Integer codvend;
	
	private Integer codemppg;
	
	private Integer codfilialpg;
	
	private Integer codplanopag;
	
	private Integer codemptn;
	
	private Integer codfilialtn;
	
	private Integer codtran;
	
	private String statusorc;
	
	private BigDecimal vlrprodorc;
	
	private Integer qtditens;

	
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


	
	public String getTipoorc() {
	
		return tipoorc;
	}


	
	public void setTipoorc( String tipoorc ) {
	
		this.tipoorc = tipoorc;
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

	
	public Date getDtorc() {
	
		return dtorc;
	}

	
	public void setDtorc( Date dtorc ) {
	
		this.dtorc = dtorc;
	}

	
	public Date getDtvencorc() {
	
		return dtvencorc;
	}

	
	public void setDtvencorc( Date dtvencorc ) {
	
		this.dtvencorc = dtvencorc;
	}

	
	public Integer getCodempvd() {
	
		return codempvd;
	}

	
	public void setCodempvd( Integer codempvd ) {
	
		this.codempvd = codempvd;
	}

	
	public Integer getCodfilialvd() {
	
		return codfilialvd;
	}

	
	public void setCodfilialvd( Integer codfilialvd ) {
	
		this.codfilialvd = codfilialvd;
	}

	
	public Integer getCodvend() {
	
		return codvend;
	}

	
	public void setCodvend( Integer codvend ) {
	
		this.codvend = codvend;
	}

	
	public Integer getCodemppg() {
	
		return codemppg;
	}

	
	public void setCodemppg( Integer codemppg ) {
	
		this.codemppg = codemppg;
	}

	
	public Integer getCodfilialpg() {
	
		return codfilialpg;
	}

	
	public void setCodfilialpg( Integer codfilialpg ) {
	
		this.codfilialpg = codfilialpg;
	}

	
	public Integer getCodplanopag() {
	
		return codplanopag;
	}

	
	public void setCodplanopag( Integer codplanopag ) {
	
		this.codplanopag = codplanopag;
	}

	
	public Integer getCodemptn() {
	
		return codemptn;
	}

	
	public void setCodemptn( Integer codemptn ) {
	
		this.codemptn = codemptn;
	}

	
	public Integer getCodfilialtn() {
	
		return codfilialtn;
	}

	
	public void setCodfilialtn( Integer codfilialtn ) {
	
		this.codfilialtn = codfilialtn;
	}

	
	public Integer getCodtran() {
	
		return codtran;
	}

	
	public void setCodtran( Integer codtran ) {
	
		this.codtran = codtran;
	}


	
	public String getStatusorc() {
	
		return statusorc;
	}


	
	public void setStatusorc( String statusorc ) {
	
		this.statusorc = statusorc;
	}


	
	public BigDecimal getVlrprodorc() {
	
		return vlrprodorc;
	}


	
	public void setVlrprodorc( BigDecimal vlrprodorc ) {
	
		this.vlrprodorc = vlrprodorc;
	}


	
	public Integer getQtditens() {
	
		return qtditens;
	}


	
	public void setQtditens( Integer qtditens ) {
	
		this.qtditens = qtditens;
	}
	
}
