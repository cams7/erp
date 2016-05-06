package org.freedom.modulos.std.business.object;

import java.math.BigDecimal;


public class ComissionadoComissao {

	private String codsecao;

	private BigDecimal perccomis;

	private BigDecimal vlrcomis;

	private Integer codvend;
	
	public ComissionadoComissao() {

	}

	public String getCodsecao() {

		return codsecao;
	}



	public void setCodsecao( String codsecao ) {

		this.codsecao = codsecao;
	}



	public BigDecimal getPerccomis() {

		return perccomis;
	}



	public void setPerccomis( BigDecimal perccomis ) {

		this.perccomis = perccomis;
	}



	public BigDecimal getVlrcomis() {

		return vlrcomis;
	}



	public void setVlrcomis( BigDecimal vlrcomis ) {

		this.vlrcomis = vlrcomis;
	}



	public Integer getCodvend() {

		return codvend;
	}



	public void setCodvend( Integer codvend ) {

		this.codvend = codvend;
	}

	@ Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( codsecao == null ) ? 0 : codsecao.hashCode() );
		result = prime * result + ( ( codvend == null ) ? 0 : codvend.hashCode() );
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
		ComissionadoComissao other = (ComissionadoComissao) obj;
		if ( codsecao == null ) {
			if ( other.codsecao != null )
				return false;
		}
		else if ( !codsecao.equals( other.codsecao ) )
			return false;
		if ( codvend == null ) {
			if ( other.codvend != null )
				return false;
		}
		else if ( !codvend.equals( other.codvend ) )
			return false;
		return true;
	}

	


}
