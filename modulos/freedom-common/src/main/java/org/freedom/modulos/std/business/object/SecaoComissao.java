package org.freedom.modulos.std.business.object;

import java.math.BigDecimal;



public class SecaoComissao {

	private String codsecao;

	private BigDecimal perccomisgeral;
	
	private BigDecimal valorcomisgeral;
	
	public SecaoComissao() {
		
	}
	
	public String getCodsecao() {
	
		return codsecao;
	}

	
	public void setCodsecao( String codsecao ) {
	
		this.codsecao = codsecao;
	}

	
	public BigDecimal getPerccomisgeral() {
	
		return perccomisgeral;
	}

	
	public void setPerccomisgeral( BigDecimal perccomisgeral ) {
	
		this.perccomisgeral = perccomisgeral;
	}

	
	@ Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( codsecao == null ) ? 0 : codsecao.hashCode() );
		return result;
	}

	@ Override
	public boolean equals( Object obj ) {

		if ( this == obj )
			return true;
		if ( obj == null )
			return false;
		if ( getClass() != obj.getClass() )
			return false;
		SecaoComissao other = (SecaoComissao) obj;
		if ( codsecao == null ) {
			if ( other.codsecao != null )
				return false;
		}
		else if ( !codsecao.equals( other.codsecao ) )
			return false;
		return true;
	}

	
	public BigDecimal getValorcomisgeral() {
	
		return valorcomisgeral;
	}

	
	public void setValorcomisgeral( BigDecimal valorcomisgeral ) {
	
		this.valorcomisgeral = valorcomisgeral;
	}
	
	
}
