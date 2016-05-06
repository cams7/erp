package org.freedom.library.business.componet.integration.ebs.vo;

import java.math.BigDecimal;

import org.freedom.library.business.componet.integration.ebs.EbsContabil;
import org.freedom.library.functions.Funcoes;

/**
 * 
 * @author ffrizzo
 * since 11/12/2010
 */
public class NotaItemVO {
	
	private String tipoNota;
	private Integer numeroNota;
	private String modelo;
	private String serie;
	private String cfop;
	private String cpfCnpj;
	private Integer codigo;
	private BigDecimal valor;
	private BigDecimal quantidade;
	private BigDecimal desconto;
	private BigDecimal baseICMS;
	private BigDecimal aliquotaICMS;
	private BigDecimal valorIPI;
	private BigDecimal baseICMSSubTributaria;
	private BigDecimal aliquotaIPI;
	private BigDecimal percRedBaseIcms;
	private Integer sitTributaria;
	private String identificacao;
	private Integer sitTributariaIPI;
	private BigDecimal baseIPI;
	private Integer sitTributariaPIS;
	private BigDecimal basePIS;
	private BigDecimal aliquotaPISPercento;
	private BigDecimal qtdeBaseCalculoPIS;
	private BigDecimal aliquotaPISValor;
	private BigDecimal valorPIS;
	private Integer sitTributariaCofins;
	private BigDecimal baseCofins;
	private BigDecimal aliquotaCofinsPercento;
	private BigDecimal qtdeBaseCalcCofins;
	private BigDecimal aliquotaCofinsValor;
	private BigDecimal valorCofins;
	private BigDecimal valorICMSSubTributaria;
	private BigDecimal aliquotaICMSSubTributaria;
	
	private Integer sequencial;
	
	public NotaItemVO() {
	}

	public String getTipoNota() {
		return tipoNota;
	}

	public void setTipoNota(String tipoNota) {
		this.tipoNota = tipoNota;
	}

	public Integer getNumeroNota() {
		return numeroNota;
	}

	public void setNumeroNota(Integer numeroNota) {
		this.numeroNota = numeroNota;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getCfop() {
		return cfop;
	}

	public void setCfop(String cfop) {
		this.cfop = cfop;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
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

	public BigDecimal getAliquotaIPI() {
		return aliquotaIPI;
	}

	public void setAliquotaIPI(BigDecimal aliquotaIPI) {
		this.aliquotaIPI = aliquotaIPI;
	}

	public BigDecimal getPercRedBaseIcms() {
		return percRedBaseIcms;
	}

	public void setPercRedBaseIcms(BigDecimal percRedBaseIcms) {
		this.percRedBaseIcms = percRedBaseIcms;
	}

	public Integer getSitTributaria() {
		return sitTributaria;
	}

	public void setSitTributaria(Integer sitTributaria) {
		this.sitTributaria = sitTributaria;
	}

	public String getIdentificacao() {
		return identificacao;
	}

	public void setIdentificacao(String identificacao) {
		this.identificacao = identificacao;
	}

	public Integer getSitTributariaIPI() {
		return sitTributariaIPI;
	}

	public void setSitTributariaIPI(Integer sitTributariaIPI) {
		this.sitTributariaIPI = sitTributariaIPI;
	}

	public BigDecimal getBaseIPI() {
		return baseIPI;
	}

	public void setBaseIPI(BigDecimal baseIPI) {
		this.baseIPI = baseIPI;
	}

	public Integer getSitTributariaPIS() {
		return sitTributariaPIS;
	}

	public void setSitTributariaPIS(Integer sitTributariaPIS) {
		this.sitTributariaPIS = sitTributariaPIS;
	}

	public BigDecimal getBasePIS() {
		return basePIS;
	}

	public void setBasePIS(BigDecimal basePIS) {
		this.basePIS = basePIS;
	}

	public BigDecimal getAliquotaPISPercento() {
		return aliquotaPISPercento;
	}

	public void setAliquotaPISPercento(BigDecimal aliquotaPISPercento) {
		this.aliquotaPISPercento = aliquotaPISPercento;
	}

	public BigDecimal getQtdeBaseCalculoPIS() {
		return qtdeBaseCalculoPIS;
	}

	public void setQtdeBaseCalculoPIS(BigDecimal qtdeBaseCalculoPIS) {
		this.qtdeBaseCalculoPIS = qtdeBaseCalculoPIS;
	}

	public BigDecimal getAliquotaPISValor() {
		return aliquotaPISValor;
	}

	public void setAliquotaPISValor(BigDecimal aliquotaPISValor) {
		this.aliquotaPISValor = aliquotaPISValor;
	}

	public BigDecimal getValorPIS() {
		return valorPIS;
	}

	public void setValorPIS(BigDecimal valorPIS) {
		this.valorPIS = valorPIS;
	}

	public Integer getSitTributariaCofins() {
		return sitTributariaCofins;
	}

	public void setSitTributariaCofins(Integer sitTributariaCofins) {
		this.sitTributariaCofins = sitTributariaCofins;
	}

	public BigDecimal getBaseCofins() {
		return baseCofins;
	}

	public void setBaseCofins(BigDecimal baseCofins) {
		this.baseCofins = baseCofins;
	}

	public BigDecimal getAliquotaCofinsPercento() {
		return aliquotaCofinsPercento;
	}

	public void setAliquotaCofinsPercento(BigDecimal aliquotaCofinsPercento) {
		this.aliquotaCofinsPercento = aliquotaCofinsPercento;
	}

	public BigDecimal getQtdeBaseCalcCofins() {
		return qtdeBaseCalcCofins;
	}

	public void setQtdeBaseCalcCofins(BigDecimal qtdeBaseCalcCofins) {
		this.qtdeBaseCalcCofins = qtdeBaseCalcCofins;
	}

	public BigDecimal getAliquotaCofinsValor() {
		return aliquotaCofinsValor;
	}

	public void setAliquotaCofinsValor(BigDecimal aliquotaCofinsValor) {
		this.aliquotaCofinsValor = aliquotaCofinsValor;
	}

	public BigDecimal getValorCofins() {
		return valorCofins;
	}

	public void setValorCofins(BigDecimal valorCofins) {
		this.valorCofins = valorCofins;
	}

	public Integer getSequencial() {
		return sequencial;
	}

	public void setSequencial(Integer sequencial) {
		this.sequencial = sequencial;
	}
	
	public BigDecimal getBaseICMSSubTributaria() {
		return baseICMSSubTributaria;
	}

	public void setBaseICMSSubTributaria(BigDecimal baseICMSSubTributaria) {
		this.baseICMSSubTributaria = baseICMSSubTributaria;
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(getTipoNota());
		sb.append(EbsContabil.format(getNumeroNota(), 6));
		sb.append(EbsContabil.format(getModelo(), 2));
		sb.append(EbsContabil.format(getSerie(), 3));
		sb.append(EbsContabil.format(getCfop(), 4));
		
		if(getCpfCnpj() == null || getCpfCnpj().trim().length() == 0){
			sb.append(EbsContabil.format("", 18));
		}else if (getCpfCnpj().length() == 14) {
			sb.append(EbsContabil.format(Funcoes.setMascara(getCpfCnpj(), "##.###.###/####-##"), 18));
		}else if (getCpfCnpj().length() == 11) {
			sb.append(EbsContabil.format(Funcoes.setMascara(getCpfCnpj(), "###.###.###-##"), 18));
		}
		
		sb.append(EbsContabil.format(getCodigo(),10));
		sb.append(EbsContabil.format(getValor(), 12 , 2));
		sb.append(EbsContabil.format(getQuantidade(), 13, 3));
		sb.append(EbsContabil.format(getDesconto(), 12, 2));
		sb.append(EbsContabil.format(getBaseICMS(), 12, 2));
		sb.append(EbsContabil.format(getAliquotaICMS(), 5, 2));
		sb.append(EbsContabil.format(getValorIPI(), 12, 2));
		sb.append(EbsContabil.format(getBaseICMSSubTributaria(), 12, 2));
		sb.append(EbsContabil.format(getAliquotaIPI(), 5, 2));
		sb.append(EbsContabil.format(getPercRedBaseIcms(), 5, 2));
		sb.append(EbsContabil.format(getSitTributaria(), 3));
		sb.append(EbsContabil.format(getIdentificacao(), 15));
		sb.append(EbsContabil.format(getSitTributariaIPI(), 3));
		sb.append(EbsContabil.format(getBaseIPI(), 12, 2));
		sb.append(EbsContabil.format(getSitTributariaPIS(), 3));
		sb.append(EbsContabil.format(getBasePIS(), 12, 2));
		sb.append(EbsContabil.format(getAliquotaPISPercento(), 5, 2));
		sb.append(EbsContabil.format(getQtdeBaseCalculoPIS(), 12, 2));
		sb.append(EbsContabil.format(getAliquotaPISValor(), 12, 2));
		sb.append(EbsContabil.format(getValorPIS(), 12, 2));
		sb.append(EbsContabil.format(getSitTributariaCofins(), 3));
		sb.append(EbsContabil.format(getBaseCofins(), 12, 2));
		sb.append(EbsContabil.format(getAliquotaCofinsPercento(), 5, 2));
		sb.append(EbsContabil.format(getQtdeBaseCalcCofins(), 12, 2));
		sb.append(EbsContabil.format(getAliquotaCofinsValor(), 12, 2));
		sb.append(EbsContabil.format(getValorCofins(), 12, 2));
		sb.append(EbsContabil.format(getValorICMSSubTributaria(), 12, 2));
		sb.append(EbsContabil.format(getAliquotaICMSSubTributaria(), 5, 2));
		sb.append(EbsContabil.format("", 195));
		sb.append(EbsContabil.format("", 5));
		sb.append(EbsContabil.format(getSequencial(), 6));
		
		
		return sb.toString();
	}


}
