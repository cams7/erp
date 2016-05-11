package org.freedom.modulos.std.inter;

import java.math.BigDecimal;

public interface InterVenda {
	
	public int insertCabecalho(Integer codtipomov, Integer codcli, Integer codplanopag, Integer codvend, Integer codclcomis);
	
	public void insertItem(Integer codprod, String refProd, BigDecimal qtdprod, BigDecimal precoprod, BigDecimal percprod, String codlote );
	

}
