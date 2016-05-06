package org.freedom.library.business.componet.integration.ebs.vo;

import java.math.BigDecimal;

import org.freedom.library.business.componet.integration.ebs.EbsContabil;

/**
 * 
 * @author ffrizzo
 * since 11/12/2010
 */
public class ItemSaidaVO {

	private final int tipoRegistro = 2;

	private int codigoItem;

	private BigDecimal quantidade;

	private BigDecimal valor;

	private BigDecimal quantidade2;

	private BigDecimal desconto;

	private BigDecimal baseICMS;

	private BigDecimal aliquotaICMS;

	private BigDecimal valorIPI;

	private BigDecimal baseICMSSubTributaria;

	private BigDecimal aliquotaIPI;

	private BigDecimal percentualReducaoBaseICMS;

	private int situacaoTributaria;

	private String indentificacao;

	private int situacaoTributariaIPI;

	private BigDecimal baseIPI;

	private int situacaoTributariaPIS;

	private BigDecimal basePIS;

	private BigDecimal aliquotaPIS;

	private BigDecimal quantidadeBasePIS;

	private BigDecimal valorAliquotaPIS;

	private BigDecimal valorPIS;

	private int situacaoTributariaCOFINS;

	private BigDecimal baseCOFINS;

	private BigDecimal valorAliquotaCOFINS;

	private BigDecimal aliquotaCOFINS;

	private BigDecimal quantidadeBaseCOFINS;

	private BigDecimal valorCOFINS;

	private BigDecimal valorICMSSubTributaria;
	
	private BigDecimal aliquotaICMSSubTributaria;
	
	private BigDecimal valorICMS;
	
	private String naturezaItem;
	
	private String unidade;
	
	private int codServTelecom;
	
	private int codRecTelecom;

	private int sequencial;
	
	public ItemSaidaVO() {
	}

	public int getCodigoItem() {
		return codigoItem;
	}

	public void setCodigoItem(int codigoItem) {
		this.codigoItem = codigoItem;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getQuantidade2() {
		return quantidade2;
	}

	public void setQuantidade2(BigDecimal quantidade2) {
		this.quantidade2 = quantidade2;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public BigDecimal getBaseICMS() {
		return baseICMS;
	}

	public void setBaseICMS(BigDecimal baseICMS) {
		this.baseICMS = baseICMS;
	}

	public BigDecimal getAliquotaICMS() {
		return aliquotaICMS;
	}

	public void setAliquotaICMS(BigDecimal aliquotaICMS) {
		this.aliquotaICMS = aliquotaICMS;
	}

	public BigDecimal getValorIPI() {
		return valorIPI;
	}

	public void setValorIPI(BigDecimal valorIPI) {
		this.valorIPI = valorIPI;
	}

	public BigDecimal getBaseICMSSubTributaria() {
		return baseICMSSubTributaria;
	}

	public void setBaseICMSSubTributaria(BigDecimal baseICMSSubTributaria) {
		this.baseICMSSubTributaria = baseICMSSubTributaria;
	}

	public BigDecimal getAliquotaIPI() {
		return aliquotaIPI;
	}

	public void setAliquotaIPI(BigDecimal aliquotaIPI) {
		this.aliquotaIPI = aliquotaIPI;
	}

	public BigDecimal getPercentualReducaoBaseICMS() {
		return percentualReducaoBaseICMS;
	}

	public void setPercentualReducaoBaseICMS(BigDecimal percentualReducaoBaseICMS) {
		this.percentualReducaoBaseICMS = percentualReducaoBaseICMS;
	}

	public int getSituacaoTributaria() {
		return situacaoTributaria;
	}

	public void setSituacaoTributaria(int situacaoTributaria) {
		this.situacaoTributaria = situacaoTributaria;
	}

	public String getIndentificacao() {
		return indentificacao;
	}

	public void setIndentificacao(String indentificacao) {
		this.indentificacao = indentificacao;
	}

	public int getSituacaoTributariaIPI() {
		return situacaoTributariaIPI;
	}

	public void setSituacaoTributariaIPI(int situacaoTributariaIPI) {
		this.situacaoTributariaIPI = situacaoTributariaIPI;
	}

	public BigDecimal getBaseIPI() {
		return baseIPI;
	}

	public void setBaseIPI(BigDecimal baseIPI) {
		this.baseIPI = baseIPI;
	}

	public int getSituacaoTributariaPIS() {
		return situacaoTributariaPIS;
	}

	public void setSituacaoTributariaPIS(int situacaoTributariaPIS) {
		this.situacaoTributariaPIS = situacaoTributariaPIS;
	}

	public BigDecimal getBasePIS() {
		return basePIS;
	}

	public void setBasePIS(BigDecimal basePIS) {
		this.basePIS = basePIS;
	}

	public BigDecimal getAliquotaPIS() {
		return aliquotaPIS;
	}

	public void setAliquotaPIS(BigDecimal aliquotaPIS) {
		this.aliquotaPIS = aliquotaPIS;
	}

	public BigDecimal getQuantidadeBasePIS() {
		return quantidadeBasePIS;
	}

	public void setQuantidadeBasePIS(BigDecimal quantidadeBasePIS) {
		this.quantidadeBasePIS = quantidadeBasePIS;
	}

	public BigDecimal getValorAliquotaPIS() {
		return valorAliquotaPIS;
	}

	public void setValorAliquotaPIS(BigDecimal valorAliquotaPIS) {
		this.valorAliquotaPIS = valorAliquotaPIS;
	}

	public BigDecimal getValorPIS() {
		return valorPIS;
	}

	public void setValorPIS(BigDecimal valorPIS) {
		this.valorPIS = valorPIS;
	}

	public int getSituacaoTributariaCOFINS() {
		return situacaoTributariaCOFINS;
	}

	public void setSituacaoTributariaCOFINS(int situacaoTributariaCOFINS) {
		this.situacaoTributariaCOFINS = situacaoTributariaCOFINS;
	}

	public BigDecimal getBaseCOFINS() {
		return baseCOFINS;
	}

	public void setBaseCOFINS(BigDecimal baseCOFINS) {
		this.baseCOFINS = baseCOFINS;
	}

	public BigDecimal getValorAliquotaCOFINS() {
		return valorAliquotaCOFINS;
	}

	public void setValorAliquotaCOFINS(BigDecimal aliquotaCOFINS) {
		this.valorAliquotaCOFINS = aliquotaCOFINS;
	}

	public BigDecimal getAliquotaCOFINS() {
		return aliquotaCOFINS;
	}

	public void setAliquotaCOFINS(BigDecimal aliquotaCOFINS) {
		this.aliquotaCOFINS = aliquotaCOFINS;
	}

	public BigDecimal getQuantidadeBaseCOFINS() {
		return quantidadeBaseCOFINS;
	}

	public void setQuantidadeBaseCOFINS(BigDecimal quantidadeBaseCOFINS) {
		this.quantidadeBaseCOFINS = quantidadeBaseCOFINS;
	}

	public BigDecimal getValorCOFINS() {
		return valorCOFINS;
	}

	public void setValorCOFINS(BigDecimal valorCOFINS) {
		this.valorCOFINS = valorCOFINS;
	}

	public BigDecimal getValorICMSSubTributaria() {
		return valorICMSSubTributaria;
	}

	public void setValorICMSSubTributaria(BigDecimal valorICMSSubTributaria) {
		this.valorICMSSubTributaria = valorICMSSubTributaria;
	}
	
	public BigDecimal getAliquotaICMSSubTributaria() {
		return aliquotaICMSSubTributaria;
	}

	public void setAliquotaICMSSubTributaria(BigDecimal aliquotaICMSSubTributaria) {
		this.aliquotaICMSSubTributaria = aliquotaICMSSubTributaria;
	}

	public BigDecimal getValorICMS() {
		return valorICMS;
	}

	public void setValorICMS(BigDecimal valorICMS) {
		this.valorICMS = valorICMS;
	}

	public void setNaturezaItem(String naturezaItem) {
		this.naturezaItem = naturezaItem;
	}

	public String getNaturezaItem() {
		return naturezaItem;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public String getUnidade() {
		return unidade;
	}


	public int getCodServTelecom() {
		return codServTelecom;
	}

	public void setCodServTelecom(int codServTelecom) {
		this.codServTelecom = codServTelecom;
	}

	public int getCodRecTelecom() {
		return codRecTelecom;
	}

	public void setCodRecTelecom(int codRecTelecom) {
		this.codRecTelecom = codRecTelecom;
	}

	public int getSequencial() {
		return sequencial;
	}

	public void setSequencial(int sequencial) {
		this.sequencial = sequencial;
	}

	public int getTipoRegistro() {
		return tipoRegistro;
	}

	@Override
	public String toString() {

		StringBuilder itemSaida = new StringBuilder(600);

		itemSaida.append(getTipoRegistro());
		itemSaida.append(EbsContabil.format(getCodigoItem(), 10));
		itemSaida.append(EbsContabil.format(getQuantidade(), 9, 3));
		itemSaida.append(EbsContabil.format(getValor(), 12, 2));
		itemSaida.append(EbsContabil.format(getQuantidade2(), 13, 3));
		itemSaida.append(EbsContabil.format(getDesconto(), 12, 2));
		itemSaida.append(EbsContabil.format(getBaseICMS(), 12, 2));
		itemSaida.append(EbsContabil.format(getAliquotaICMS(), 5, 2));
		itemSaida.append(EbsContabil.format(getValorIPI(), 12, 2));
		itemSaida.append(EbsContabil.format(getBaseICMSSubTributaria(), 12, 2));
		itemSaida.append(EbsContabil.format(getAliquotaIPI(), 5, 2));
		itemSaida.append(EbsContabil.format(getPercentualReducaoBaseICMS(), 5, 2));
		itemSaida.append(EbsContabil.format(getSituacaoTributaria(), 3));
		itemSaida.append(EbsContabil.format(getIndentificacao(), 15));
		itemSaida.append(EbsContabil.format(getSituacaoTributariaIPI(), 3));
		itemSaida.append(EbsContabil.format(getBaseIPI(), 12, 2));
		//itemSaida.append(EbsContabil.format(getSituacaoTributariaPIS(), 3));
		itemSaida.append(EbsContabil.format(getSituacaoTributariaPIS(), 2));
		itemSaida.append(EbsContabil.format(getBasePIS(), 12, 2));
		//itemSaida.append(EbsContabil.format(getAliquotaPIS(), 5, 2));
		itemSaida.append(EbsContabil.format(getAliquotaPIS(), 7, 4));
		//itemSaida.append(EbsContabil.format(getQuantidadeBasePIS(), 12, 2));
		itemSaida.append(EbsContabil.format(getQuantidadeBasePIS(), 13, 3));
		//itemSaida.append(EbsContabil.format(getValorAliquotaPIS(), 12, 2));
		itemSaida.append(EbsContabil.format(getValorAliquotaPIS(), 14, 4));
		itemSaida.append(EbsContabil.format(getValorPIS(), 12, 2));
		//itemSaida.append(EbsContabil.format(getSituacaoTributariaCOFINS(), 3));
		itemSaida.append(EbsContabil.format(getSituacaoTributariaCOFINS(), 2));
		itemSaida.append(EbsContabil.format(getBaseCOFINS(), 12, 2));
		//itemSaida.append(EbsContabil.format(getAliquotaCOFINS(), 5, 2));
		itemSaida.append(EbsContabil.format(getAliquotaCOFINS(), 7, 4));
		//itemSaida.append(EbsContabil.format(getQuantidadeBaseCOFINS(), 12, 2));
		itemSaida.append(EbsContabil.format(getQuantidadeBaseCOFINS(), 13, 3));
		//itemSaida.append(EbsContabil.format(getValorAliquotaCOFINS(), 12, 2));
		itemSaida.append(EbsContabil.format(getValorAliquotaCOFINS(), 14, 4));
		itemSaida.append(EbsContabil.format(getValorCOFINS(), 12, 2));
		itemSaida.append(EbsContabil.format(getValorICMSSubTributaria(), 12, 2));
		//Campo do novo layout EBS, informa o valor de icms retido por substituição tributária, caso não houver preencher com zeros.
		itemSaida.append(EbsContabil.format(getAliquotaICMSSubTributaria(), 5, 2));
		//Campo do novo layout EBS, informa o valor do ICMS, caso não houver preencher com zeros.
		itemSaida.append(EbsContabil.format(getValorICMS(), 12, 2));
		//Campo do novo layout EBS, informa o CFOP do item.
		itemSaida.append(EbsContabil.format(getNaturezaItem(), 4));
		//Campo do novo layout EBS, informa a unidade de medida do tem constante da nota fiscal.
		itemSaida.append(EbsContabil.format(getUnidade(), 6));
		//Campo alterado no novo layout EBS
		//itemSaida.append(EbsContabil.format(" ", 224));
		itemSaida.append(EbsContabil.format(getCodServTelecom(), 4));
		itemSaida.append(EbsContabil.format(getCodRecTelecom(), 1));
		itemSaida.append(EbsContabil.format(" ", 184));
		itemSaida.append(EbsContabil.format(" ", 5));
		itemSaida.append(EbsContabil.format(getSequencial(), 6));

		return itemSaida.toString();
	}
}
