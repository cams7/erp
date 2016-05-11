package org.freedom.modulos.gms.business.object;

import java.math.BigDecimal;
import java.util.Date;


public class CotacaoPrecos {
	public static enum SELECT_UPDATE {
		NONE, CODEMPFR, CODFILIALFR, CODFOR, CODEMPPG, CODFILIALPG, CODPLANPAG, DTCOT, DTVALIDCOT, CODEMPPD, CODFILIALPD, CODPROD, RENDA
	}
	public static enum UPDATE {
		NONE, PRECOCOT, APROVPRECO, VLRLIQITCOMPRA, CODEMPCP, CODFILIALCP, CODCOMPRA, CODITCOMPRA
	}
	//
	private Integer codempfr;
	
	private Integer codfilialfr;
	
	private Integer codfor;
	
	private Integer codemppg;
	
	private Integer codfilialpg;
	
	private Integer codplanpag;
	
	private Date dtcot;

	private Date dtvalidcot;
	
	private Integer codemppd;
	
	private Integer codfilialpd;
	
	private Integer codprod;
	
	private BigDecimal precocot;
	
	private String aprovpreco;
	
	private Integer codempcp;
	
	private Integer codfilialcp;
	
	private Integer codcompra;
	
	private Integer coditcompra;
	
	private String usarendacot;
	
	private Integer renda;
	
	
	public Integer getCodempfr() {
	
		return codempfr;
	}

	
	public void setCodempfr( Integer codempfr ) {
	
		this.codempfr = codempfr;
	}

	
	public Integer getCodfilialfr() {
	
		return codfilialfr;
	}

	
	public void setCodfilialfr( Integer codfilialfr ) {
	
		this.codfilialfr = codfilialfr;
	}

	
	public Integer getCodfor() {
	
		return codfor;
	}

	
	public void setCodfor( Integer codfor ) {
	
		this.codfor = codfor;
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

	
	
	public Integer getCodplanpag() {
	
		return codplanpag;
	}


	
	public void setCodplanpag( Integer codplanpag ) {
	
		this.codplanpag = codplanpag;
	}


	public Date getDtcot() {
		
		return dtcot;
	}


	
	public void setDtcot( Date dtcot ) {
	
		this.dtcot = dtcot;
	}


	
	public Date getDtvalidcot() {
	
		return dtvalidcot;
	}

	
	public void setDtvalidcot( Date dtvalidcot ) {
	
		this.dtvalidcot = dtvalidcot;
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

	
	public BigDecimal getPrecocot() {
	
		return precocot;
	}

	
	public void setPrecocot( BigDecimal precocot ) {
	
		this.precocot = precocot;
	}

	
	public String getAprovpreco() {
	
		return aprovpreco;
	}

	
	public void setAprovpreco( String aprovpreco ) {
	
		this.aprovpreco = aprovpreco;
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

	
	public Integer getCodcompra() {
	
		return codcompra;
	}

	
	public void setCodcompra( Integer codcompra ) {
	
		this.codcompra = codcompra;
	}

	
	public Integer getCoditcompra() {
	
		return coditcompra;
	}

	
	public void setCoditcompra( Integer coditcompra ) {
	
		this.coditcompra = coditcompra;
	}
	
	public String getUsarendacot() {
	
		return usarendacot;
	}


	
	public void setUsarendacot( String usarendacot ) {
	
		this.usarendacot = usarendacot;
	}


	
	public Integer getRenda() {
	
		return renda;
	}


	
	public void setRenda( Integer renda ) {
	
		this.renda = renda;
	}
}
