package org.freedom.library.business.componet.integration.ebs.vo;

import java.math.BigDecimal;

import org.freedom.library.business.componet.integration.ebs.EbsContabil;

/**
 * 
 * @author ffrizzo
 * since 11/12/2010
 */
public class TraillerEntradaVO {

	private final int tipoRegistro = 3;

	private BigDecimal valorNota;

	private BigDecimal basePIS;

	private BigDecimal baseCOFINS;

	private BigDecimal baseContribuicaoSocial;

	private BigDecimal baseImpostoRenda;

	private BigDecimal baseICMSa;

	private BigDecimal valorICMSa;

	private BigDecimal baseICMSb;

	private BigDecimal valorICMSb;

	private BigDecimal baseICMSc;

	private BigDecimal valorICMSc;

	private BigDecimal baseICMSd;

	private BigDecimal valorICMSd;

	private BigDecimal valorICMSIsentas;

	private BigDecimal valorICMSOutras;

	private BigDecimal baseIPI;

	private BigDecimal valorIPI;

	private BigDecimal valorIPIIsentas;

	private BigDecimal valorIPIOutras;

	private BigDecimal valorSubTributaria;

	private BigDecimal baseSubTriburaria;

	private BigDecimal valorICMSSubTributaria;

	private BigDecimal valorDiferidas;

	private int sequencial;
	
	public TraillerEntradaVO() {
	}

	public int getTipoRegistro() {
		return tipoRegistro;
	}

	public BigDecimal getValorNota() {
		return valorNota;
	}

	public void setValorNota(BigDecimal valorNota) {
		this.valorNota = valorNota;
	}

	public BigDecimal getBasePIS() {
		return basePIS;
	}

	public void setBasePIS(BigDecimal basePIS) {
		this.basePIS = basePIS;
	}

	public BigDecimal getBaseCOFINS() {
		return baseCOFINS;
	}

	public void setBaseCOFINS(BigDecimal baseCOFINS) {
		this.baseCOFINS = baseCOFINS;
	}

	public BigDecimal getBaseContribuicaoSocial() {
		return baseContribuicaoSocial;
	}

	public void setBaseContribuicaoSocial(BigDecimal baseContribuicaoSocial) {
		this.baseContribuicaoSocial = baseContribuicaoSocial;
	}

	public BigDecimal getBaseImpostoRenda() {
		return baseImpostoRenda;
	}

	public void setBaseImpostoRenda(BigDecimal baseImpostoRenda) {
		this.baseImpostoRenda = baseImpostoRenda;
	}

	public BigDecimal getBaseICMSa() {
		return baseICMSa;
	}

	public void setBaseICMSa(BigDecimal baseICMSa) {
		this.baseICMSa = baseICMSa;
	}

	public BigDecimal getValorICMSa() {
		return valorICMSa;
	}

	public void setValorICMSa(BigDecimal valorICMSa) {
		this.valorICMSa = valorICMSa;
	}

	public BigDecimal getBaseICMSb() {
		return baseICMSb;
	}

	public void setBaseICMSb(BigDecimal baseICMSb) {
		this.baseICMSb = baseICMSb;
	}

	public BigDecimal getValorICMSb() {
		return valorICMSb;
	}

	public void setValorICMSb(BigDecimal valorICMSb) {
		this.valorICMSb = valorICMSb;
	}

	public BigDecimal getBaseICMSc() {
		return baseICMSc;
	}

	public void setBaseICMSc(BigDecimal baseICMSc) {
		this.baseICMSc = baseICMSc;
	}

	public BigDecimal getValorICMSc() {
		return valorICMSc;
	}

	public void setValorICMSc(BigDecimal valorICMSc) {
		this.valorICMSc = valorICMSc;
	}

	public BigDecimal getBaseICMSd() {
		return baseICMSd;
	}

	public void setBaseICMSd(BigDecimal baseICMSd) {
		this.baseICMSd = baseICMSd;
	}

	public BigDecimal getValorICMSd() {
		return valorICMSd;
	}

	public void setValorICMSd(BigDecimal valorICMSd) {
		this.valorICMSd = valorICMSd;
	}

	public BigDecimal getValorICMSIsentas() {
		return valorICMSIsentas;
	}

	public void setValorICMSIsentas(BigDecimal valorICMSIsentas) {
		this.valorICMSIsentas = valorICMSIsentas;
	}

	public BigDecimal getValorICMSOutras() {
		return valorICMSOutras;
	}

	public void setValorICMSOutras(BigDecimal valorICMSOutras) {
		this.valorICMSOutras = valorICMSOutras;
	}

	public BigDecimal getBaseIPI() {
		return baseIPI;
	}

	public void setBaseIPI(BigDecimal baseIPI) {
		this.baseIPI = baseIPI;
	}

	public BigDecimal getValorIPI() {
		return valorIPI;
	}

	public void setValorIPI(BigDecimal valorIPI) {
		this.valorIPI = valorIPI;
	}

	public BigDecimal getValorIPIIsentas() {
		return valorIPIIsentas;
	}

	public void setValorIPIIsentas(BigDecimal valorIPIIsentas) {
		this.valorIPIIsentas = valorIPIIsentas;
	}

	public BigDecimal getValorIPIOutras() {
		return valorIPIOutras;
	}

	public void setValorIPIOutras(BigDecimal valorIPIOutras) {
		this.valorIPIOutras = valorIPIOutras;
	}

	public BigDecimal getValorSubTributaria() {
		return valorSubTributaria;
	}

	public void setValorSubTributaria(BigDecimal valorSubTributaria) {
		this.valorSubTributaria = valorSubTributaria;
	}

	public BigDecimal getBaseSubTriburaria() {
		return baseSubTriburaria;
	}

	public void setBaseSubTriburaria(BigDecimal baseSubTriburaria) {
		this.baseSubTriburaria = baseSubTriburaria;
	}

	public BigDecimal getValorICMSSubTributaria() {
		return valorICMSSubTributaria;
	}

	public void setValorICMSSubTributaria(BigDecimal valorICMSSubTributaria) {
		this.valorICMSSubTributaria = valorICMSSubTributaria;
	}

	public BigDecimal getValorDiferidas() {
		return valorDiferidas;
	}

	public void setValorDiferidas(BigDecimal valorDiferidas) {
		this.valorDiferidas = valorDiferidas;
	}

	public int getSequencial() {
		return sequencial;
	}

	public void setSequencial(int sequencial) {
		this.sequencial = sequencial;
	}

	@Override
	public String toString() {

		StringBuilder trallerEntrada = new StringBuilder(500);

		trallerEntrada.append(getTipoRegistro());
		trallerEntrada.append(EbsContabil.format(getValorNota(), 12, 2));
		trallerEntrada.append(EbsContabil.format(getBasePIS(), 12, 2));
		trallerEntrada.append(EbsContabil.format(getBaseCOFINS(), 12, 2));
		trallerEntrada.append(EbsContabil.format(getBaseContribuicaoSocial(), 12, 2));
		trallerEntrada.append(EbsContabil.format(getBaseImpostoRenda(), 12, 2));
		trallerEntrada.append(EbsContabil.format(getBaseICMSa(), 12, 2));
		trallerEntrada.append(EbsContabil.format(getValorICMSa(), 12, 2));
		trallerEntrada.append(EbsContabil.format(getBaseICMSb(), 12, 2));
		trallerEntrada.append(EbsContabil.format(getValorICMSb(), 12, 2));
		trallerEntrada.append(EbsContabil.format(getBaseICMSc(), 12, 2));
		trallerEntrada.append(EbsContabil.format(getValorICMSc(), 12, 2));
		trallerEntrada.append(EbsContabil.format(getBaseICMSd(), 12, 2));
		trallerEntrada.append(EbsContabil.format(getValorICMSd(), 12, 2));
		trallerEntrada.append(EbsContabil.format(getValorICMSIsentas(), 12, 2));
		trallerEntrada.append(EbsContabil.format(getValorICMSOutras(), 12, 2));
		trallerEntrada.append(EbsContabil.format(getBaseIPI(), 12, 2));
		trallerEntrada.append(EbsContabil.format(getValorIPI(), 12, 2));
		trallerEntrada.append(EbsContabil.format(getValorIPIIsentas(), 12, 2));
		trallerEntrada.append(EbsContabil.format(getValorIPIOutras(), 12, 2));
		trallerEntrada.append(EbsContabil.format(getValorSubTributaria(), 12, 2));
		trallerEntrada.append(EbsContabil.format(getBaseSubTriburaria(), 12, 2));
		trallerEntrada.append(EbsContabil.format(getValorICMSSubTributaria(), 12, 2));
		trallerEntrada.append(EbsContabil.format(getValorDiferidas(), 12, 2));
		trallerEntrada.append(EbsContabil.format(" ", 217));
		trallerEntrada.append(EbsContabil.format(getSequencial(), 6));

		return trallerEntrada.toString();
	}
}
