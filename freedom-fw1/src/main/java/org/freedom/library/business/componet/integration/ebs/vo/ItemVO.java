package org.freedom.library.business.componet.integration.ebs.vo;

import java.math.BigDecimal;

import org.freedom.library.business.componet.integration.ebs.EbsContabil;

/**
 * 
 * @author ffrizzo
 * since 11/12/2010
 */
public class ItemVO {
	
	private int codigo;

	private String descricao;

	private String ncm;

	private String unidade;

	private BigDecimal peso;

	private String referencia;

	private int tipoProduto;

	private int sequencial;
	
	private int cstIcmsEntrada;
		
	private int cstIpiEntrada;
	
	private int cstIcmsSaida;
	
    private int cstIpiSaida;
    
    private String unidade2;
    
    private BigDecimal aliquotaICMS;
    
    private BigDecimal aliquotaIPI;
	
	public ItemVO() {
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNcm() {
		return ncm;
	}

	public void setNcm(String ncm) {
		this.ncm = ncm;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public int getTipoProduto() {
		return tipoProduto;
	}

	public void setTipoProduto(int tipoProduto) {
		this.tipoProduto = tipoProduto;
	}

	public int getCstIcmsEntrada() {
		return cstIcmsEntrada;
	}

	public void setCstIcmsEntrada(int cstIcmsEntrada) {
		this.cstIcmsEntrada = cstIcmsEntrada;
	}

	public int getCstIpiEntrada() {
		return cstIpiEntrada;
	}

	public void setCstIpiEntrada(int cstIpiEntrada) {
		this.cstIpiEntrada = cstIpiEntrada;
	}

	public int getCstIcmsSaida() {
		return cstIcmsSaida;
	}

	public void setCstIcmsSaida(int cstIcmsSaida) {
		this.cstIcmsSaida = cstIcmsSaida;
	}

	public int getCstIpiSaida() {
		return cstIpiSaida;
	}

	public void setCstIpiSaida(int cstIpiSaida) {
		this.cstIpiSaida = cstIpiSaida;
	}

	public String getUnidade2() {
		return unidade2;
	}

	public void setUnidade2(String unidade2) {
		this.unidade2 = unidade2;
	}

	public BigDecimal getAliquotaICMS() {
		return aliquotaICMS;
	}

	public void setAliquotaICMS(BigDecimal aliquotaICMS) {
		this.aliquotaICMS = aliquotaICMS;
	}

	public BigDecimal getAliquotaIPI() {
		return aliquotaIPI;
	}

	public void setAliquotaIPI(BigDecimal aliquotaIPI) {
		this.aliquotaIPI = aliquotaIPI;
	}

	public int getSequencial() {
		return sequencial;
	}

	public void setSequencial(int sequencial) {
		this.sequencial = sequencial;
	}

	@Override
	public String toString() {

		StringBuilder item = new StringBuilder(100);

		item.append(EbsContabil.format(getCodigo(), 10));
		item.append(EbsContabil.format(getDescricao(), 40));
		item.append(EbsContabil.format(getNcm(), 8));
		item.append(EbsContabil.format(getUnidade(), 4));
		item.append(EbsContabil.format(getPeso(), 9, 3));
		item.append(EbsContabil.format(getReferencia(), 15));
		item.append(EbsContabil.format(getTipoProduto(), 2));
		//novo leiaute importação de produtos
		
		//item.append(EbsContabil.format(" ", 6));
		item.append(EbsContabil.format(" ", 12));
		item.append(EbsContabil.format(getCstIcmsEntrada(), 1));
		item.append(EbsContabil.format(getCstIpiEntrada(), 1));
		item.append(EbsContabil.format(getCstIcmsSaida(), 1));
		item.append(EbsContabil.format(getCstIpiSaida(), 1));
		item.append(EbsContabil.format(getUnidade2(), 6));
		item.append(EbsContabil.format(getAliquotaICMS(), 5, 2));
		item.append(EbsContabil.format(getAliquotaIPI(), 5, 2));
		item.append(EbsContabil.format(" ", 365));
		item.append(EbsContabil.format(2, 1));
		item.append(EbsContabil.format(getSequencial(), 6));
		
		
		
		
		return item.toString();
	}
}
