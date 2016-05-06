package org.freedom.modulos.std.business.object;

import java.math.BigDecimal;


public class UpdateVenda {

	private BigDecimal vlrprodvenda;
	private BigDecimal vlrliqprodvenda;
	private BigDecimal vlradicvenda;
	private BigDecimal Vlrdescvenda;
	
	public BigDecimal getVlrprodvenda() {
	
		return vlrprodvenda;
	}
	
	public BigDecimal getVlrliqprodvenda() {
	
		return vlrliqprodvenda;
	}
	
	public BigDecimal getVlradicvenda() {
	
		return vlradicvenda;
	}
	
	public BigDecimal getVlrdescvenda() {
	
		return Vlrdescvenda;
	}
	
	public void setVlrprodvenda( BigDecimal vlrprodvenda ) {
	
		this.vlrprodvenda = vlrprodvenda;
	}
	
	public void setVlrliqprodvenda( BigDecimal vlrliqprodvenda ) {
	
		this.vlrliqprodvenda = vlrliqprodvenda;
	}
	
	public void setVlradicvenda( BigDecimal vlradicvenda ) {
	
		this.vlradicvenda = vlradicvenda;
	}
	
	public void setVlrdescvenda( BigDecimal vlrdescvenda ) {
	
		Vlrdescvenda = vlrdescvenda;
	}

}
