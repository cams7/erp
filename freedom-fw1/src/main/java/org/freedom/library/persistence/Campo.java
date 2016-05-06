package org.freedom.library.persistence;

import java.math.BigDecimal;

public interface Campo {
	public void setVlrString(String vlr);
	public String getVlrString();
	public ListaCampos getTabelaExterna();
	public String getStrMascara();
	public int getTamanho();
	public void setTamanho(int tam);
	public void setTipoCampo(int tipoCampo);
	public int getTipoCampo();
	public int getDecimal();
	public int getIMascara();
	public void setIMascara(int iMasc);
	public ListaCampos getListaCampos();
	public void cancelaDLF2();
	public String getText();
	public int getMascara();
	public void requestFocus();
	public Integer getVlrInteger();
	public void setVlrInteger(Integer vlr);
	public void setVlrBigDecimal(BigDecimal vlr);
	
}
