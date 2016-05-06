package org.freedom.library.business.componet.integration.ebs.vo;

import java.math.BigDecimal;
import java.util.Date;

import org.freedom.library.business.componet.integration.ebs.EbsContabil;
import org.freedom.library.functions.Funcoes;

/**
 * 
 * @author ffrizzo
 * since 11/12/2010
 */
public class EntradaVO {

	private final int tipoRegistro = 1;

	private Date dataLancamento;

	private int nota;

	private Date dataEmissao;

	private int modeloNota;

	private String serie;

	private String subSerie;

	private int cfop;

	private int variacaoCfop;

	private int classificacaoIntegracao;

	private int classificacaoIntegracao2;

	private String cnpjFornecedor;

	private BigDecimal valorNota;

	private BigDecimal basePIS;

	private BigDecimal baseCOFINS;

	private BigDecimal baseCSLL;

	private BigDecimal baseIR;

	private BigDecimal baseICMSa;

	private BigDecimal aliquotaICMSa;

	private BigDecimal valorICMSa;

	private BigDecimal baseICMSb;

	private BigDecimal aliquotaICMSb;

	private BigDecimal valorICMSb;

	private BigDecimal baseICMSc;

	private BigDecimal aliquotaICMSc;

	private BigDecimal valorICMSc;

	private BigDecimal baseICMSd;

	private BigDecimal aliquotaICMSd;

	private BigDecimal valorICMSd;

	private BigDecimal valorICMSIsentas;

	private BigDecimal valorICMSOutras;

	private BigDecimal baseIPI;

	private BigDecimal valorIPI;

	private BigDecimal valorIPIIsentas;

	private BigDecimal valorIPIOutras;

	private BigDecimal valorSubTributaria;

	private BigDecimal baseSubTributaria;

	private BigDecimal valorICMSSubTributaria;

	private BigDecimal valorDiferidas;

	private String observacaoLivroFiscal;

	private String especieNota;

	private String vendaAVista;

	private int cfopSubTributaria;

	private BigDecimal basePISCOFINSSubTributaria;

	private BigDecimal baseISS;

	private BigDecimal aliquotaISS;

	private BigDecimal valorISS;

	private BigDecimal valorISSIsentas;

	private BigDecimal valorIRRF;

	private BigDecimal valorPIS;

	private BigDecimal valorCOFINS;

	private BigDecimal valorCSLL;

	private Date dataPagamento;

	private int codigoOperacaoContabil;

	private String indentificacaoExterior;

	private BigDecimal valorINSS;

	private BigDecimal valorFUNRURAL;

	private int codigoItemServico;

	private int sequencial;

	private String issRetido;
	
	private String issDevido;
	
	private String ufPrestacao;
	
	private String tipoEmissao;
	
	public EntradaVO() {
	}

	public int getTipoRegistro() {
		return tipoRegistro;
	}

	public Date getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public int getNota() {
		return nota;
	}

	public void setNota(int nota) {
		this.nota = nota;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public int getModeloNota() {
		return modeloNota;
	}

	public void setModeloNota(int modeloNota) {
		this.modeloNota = modeloNota;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getSubSerie() {
		return subSerie;
	}

	public void setSubSerie(String subSerie) {
		this.subSerie = subSerie;
	}

	public int getCfop() {
		return cfop;
	}

	public void setCfop(int cfop) {
		this.cfop = cfop;
	}

	public int getVariacaoCfop() {
		return variacaoCfop;
	}

	public void setVariacaoCfop(int variacaoCfop) {
		this.variacaoCfop = variacaoCfop;
	}

	public int getClassificacaoIntegracao() {
		return classificacaoIntegracao;
	}

	public void setClassificacaoIntegracao(int classificacaoIntegracao) {
		this.classificacaoIntegracao = classificacaoIntegracao;
	}

	public int getClassificacaoIntegracao2() {
		return classificacaoIntegracao2;
	}

	public void setClassificacaoIntegracao2(int classificacaoIntegracao2) {
		this.classificacaoIntegracao2 = classificacaoIntegracao2;
	}

	public String getCnpjFornecedor() {
		return cnpjFornecedor;
	}

	public void setCnpjFornecedor(String cnpjFornecedor) {
		this.cnpjFornecedor = cnpjFornecedor;
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

	public BigDecimal getBaseCSLL() {
		return baseCSLL;
	}

	public void setBaseCSLL(BigDecimal baseCSLL) {
		this.baseCSLL = baseCSLL;
	}

	public BigDecimal getBaseIR() {
		return baseIR;
	}

	public void setBaseIR(BigDecimal baseIR) {
		this.baseIR = baseIR;
	}

	public BigDecimal getBaseICMSa() {
		return baseICMSa;
	}

	public void setBaseICMSa(BigDecimal baseICMSa) {
		this.baseICMSa = baseICMSa;
	}

	public BigDecimal getAliquotaICMSa() {
		return aliquotaICMSa;
	}

	public void setAliquotaICMSa(BigDecimal aliquotaICMSa) {
		this.aliquotaICMSa = aliquotaICMSa;
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

	public BigDecimal getAliquotaICMSb() {
		return aliquotaICMSb;
	}

	public void setAliquotaICMSb(BigDecimal aliquotaICMSb) {
		this.aliquotaICMSb = aliquotaICMSb;
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

	public BigDecimal getAliquotaICMSc() {
		return aliquotaICMSc;
	}

	public void setAliquotaICMSc(BigDecimal aliquotaICMSc) {
		this.aliquotaICMSc = aliquotaICMSc;
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

	public BigDecimal getAliquotaICMSd() {
		return aliquotaICMSd;
	}

	public void setAliquotaICMSd(BigDecimal aliquotaICMSd) {
		this.aliquotaICMSd = aliquotaICMSd;
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

	public BigDecimal getBaseSubTributaria() {
		return baseSubTributaria;
	}

	public void setBaseSubTributaria(BigDecimal baseSubTributaria) {
		this.baseSubTributaria = baseSubTributaria;
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

	public String getObservacaoLivroFiscal() {
		return observacaoLivroFiscal;
	}

	public void setObservacaoLivroFiscal(String observacaoLivroFiscal) {
		this.observacaoLivroFiscal = observacaoLivroFiscal;
	}

	public String getEspecieNota() {
		return especieNota;
	}

	public void setEspecieNota(String especieNota) {
		this.especieNota = especieNota;
	}

	public String getVendaAVista() {
		return vendaAVista;
	}

	public void setVendaAVista(String vendaAVista) {
		this.vendaAVista = vendaAVista;
	}

	public int getCfopSubTributaria() {
		return cfopSubTributaria;
	}

	public void setCfopSubTributaria(int cfopSubTributaria) {
		this.cfopSubTributaria = cfopSubTributaria;
	}

	public BigDecimal getBasePISCOFINSSubTributaria() {
		return basePISCOFINSSubTributaria;
	}

	public void setBasePISCOFINSSubTributaria(BigDecimal basePISCOFINSSubTributaria) {
		this.basePISCOFINSSubTributaria = basePISCOFINSSubTributaria;
	}

	private BigDecimal getBaseISS() {
		return baseISS;
	}

	public void setBaseISS(BigDecimal baseISS) {
		this.baseISS = baseISS;
	}

	public BigDecimal getAliquotaISS() {
		return aliquotaISS;
	}

	public void setAliquotaISS(BigDecimal aliquotaISS) {
		this.aliquotaISS = aliquotaISS;
	}

	public BigDecimal getValorISS() {
		return valorISS;
	}

	public void setValorISS(BigDecimal valorISS) {
		this.valorISS = valorISS;
	}

	public BigDecimal getValorISSIsentas() {
		return valorISSIsentas;
	}

	public void setValorISSIsentas(BigDecimal valorISSIsentas) {
		this.valorISSIsentas = valorISSIsentas;
	}

	public BigDecimal getValorIRRF() {
		return valorIRRF;
	}

	public void setValorIRRF(BigDecimal valorIRRF) {
		this.valorIRRF = valorIRRF;
	}

	public BigDecimal getValorPIS() {
		return valorPIS;
	}

	public void setValorPIS(BigDecimal valorPIS) {
		this.valorPIS = valorPIS;
	}

	public BigDecimal getValorCOFINS() {
		return valorCOFINS;
	}

	public void setValorCOFINS(BigDecimal valorCOFINS) {
		this.valorCOFINS = valorCOFINS;
	}

	public BigDecimal getValorCSLL() {
		return valorCSLL;
	}

	public void setValorCSLL(BigDecimal valorCSLL) {
		this.valorCSLL = valorCSLL;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public int getCodigoOperacaoContabil() {
		return codigoOperacaoContabil;
	}

	public void setCodigoOperacaoContabil(int codigoOperacaoContabil) {
		this.codigoOperacaoContabil = codigoOperacaoContabil;
	}

	public String getIndentificacaoExterior() {
		return indentificacaoExterior;
	}

	public void setIndentificacaoExterior(String indentificacaoExterior) {
		this.indentificacaoExterior = indentificacaoExterior;
	}

	public BigDecimal getValorINSS() {
		return valorINSS;
	}

	public void setValorINSS(BigDecimal valorINSS) {
		this.valorINSS = valorINSS;
	}

	public BigDecimal getValorFUNRURAL() {
		return valorFUNRURAL;
	}

	public void setValorFUNRURAL(BigDecimal valorFUNRURAL) {
		this.valorFUNRURAL = valorFUNRURAL;
	}

	public int getCodigoItemServico() {
		return codigoItemServico;
	}

	public void setCodigoItemServico(int codigoItemServico) {
		this.codigoItemServico = codigoItemServico;
	}

	public int getSequencial() {
		return sequencial;
	}

	public void setSequencial(int sequencial) {
		this.sequencial = sequencial;
	}

	public String getIssRetido() {
		return issRetido;
	}

	public void setIssRetido(String issRetido) {
		this.issRetido = issRetido;
	}

	public String getIssDevido() {
		return issDevido;
	}

	public void setIssDevido(String issDevido) {
		this.issDevido = issDevido;
	}

	public String getUfPrestacao() {
		return ufPrestacao;
	}

	public void setUfPrestacao(String ufPrestacao) {
		this.ufPrestacao = ufPrestacao;
	}

	public String getTipoEmissao() {
		return tipoEmissao;
	}

	public void setTipoEmissao(String tipoEmissao) {
		this.tipoEmissao = tipoEmissao;
	}

	@Override
	public String toString() {

		StringBuilder entrada = new StringBuilder(500);

		entrada.append(getTipoRegistro());
		entrada.append(EbsContabil.format(getDataLancamento()));
		entrada.append(EbsContabil.format(getNota(), 6));
		entrada.append(EbsContabil.format(getDataEmissao()));
		entrada.append(EbsContabil.format(getModeloNota(), 2));
		entrada.append(EbsContabil.format(getSerie(), 3));
		entrada.append(EbsContabil.format(getSubSerie(), 3));
		entrada.append(EbsContabil.format(getCfop(), 4));
		entrada.append(EbsContabil.format(getVariacaoCfop(), 2));
		entrada.append(EbsContabil.format(getClassificacaoIntegracao(), 2));
		entrada.append(EbsContabil.format(getClassificacaoIntegracao2(), 2));
		entrada.append(EbsContabil.format(Funcoes.setMascara(getCnpjFornecedor(), "##.###.###/####-##"), 18));
		entrada.append(EbsContabil.format(getValorNota(), 12, 2));
		entrada.append(EbsContabil.format(getBasePIS(), 12, 2));
		entrada.append(EbsContabil.format(getBaseCOFINS(), 12, 2));
		entrada.append(EbsContabil.format(getBaseCSLL(), 12, 2));
		entrada.append(EbsContabil.format(getBaseIR(), 12, 2));
		entrada.append(EbsContabil.format(getBaseICMSa(), 12, 2));
		entrada.append(EbsContabil.format(getAliquotaICMSa(), 4, 2));
		entrada.append(EbsContabil.format(getValorICMSa(), 12, 2));
		entrada.append(EbsContabil.format(getBaseICMSb(), 12, 2));
		entrada.append(EbsContabil.format(getAliquotaICMSb(), 4, 2));
		entrada.append(EbsContabil.format(getValorICMSb(), 12, 2));
		entrada.append(EbsContabil.format(getBaseICMSc(), 12, 2));
		entrada.append(EbsContabil.format(getAliquotaICMSc(), 4, 2));
		entrada.append(EbsContabil.format(getValorICMSc(), 12, 2));
		entrada.append(EbsContabil.format(getBaseICMSd(), 12, 2));
		entrada.append(EbsContabil.format(getAliquotaICMSd(), 4, 2));
		entrada.append(EbsContabil.format(getValorICMSd(), 12, 2));
		entrada.append(EbsContabil.format(getValorICMSIsentas(), 12, 2));
		entrada.append(EbsContabil.format(getValorICMSOutras(), 12, 2));
		entrada.append(EbsContabil.format(getBaseIPI(), 12, 2));
		entrada.append(EbsContabil.format(getValorIPI(), 12, 2));
		entrada.append(EbsContabil.format(getValorIPIIsentas(), 12, 2));
		entrada.append(EbsContabil.format(getValorIPIOutras(), 12, 2));
		entrada.append(EbsContabil.format(getValorSubTributaria(), 12, 2));
		entrada.append(EbsContabil.format(getBaseSubTributaria(), 12, 2));
		entrada.append(EbsContabil.format(getValorICMSSubTributaria(), 12, 2));
		entrada.append(EbsContabil.format(getValorDiferidas(), 12, 2));
		entrada.append(EbsContabil.format(getObservacaoLivroFiscal(), 50));
		entrada.append(EbsContabil.format(getEspecieNota(), 5));
		entrada.append(EbsContabil.format(getVendaAVista(), 1));
		entrada.append(EbsContabil.format(getCfopSubTributaria(), 4));
		entrada.append(EbsContabil.format(getBasePISCOFINSSubTributaria(), 8, 2));
		entrada.append(EbsContabil.format(getBaseISS(), 12, 2));
		entrada.append(EbsContabil.format(getAliquotaISS(), 4, 2));
		entrada.append(EbsContabil.format(getValorISS(), 12, 2));
		entrada.append(EbsContabil.format(getValorISSIsentas(), 12, 2));
		entrada.append(EbsContabil.format(getValorIRRF(), 12, 2));
		entrada.append(EbsContabil.format(getValorPIS(), 12, 2));
		entrada.append(EbsContabil.format(getValorCOFINS(), 12, 2));
		entrada.append(EbsContabil.format(getValorCSLL(), 12, 2));
		entrada.append(EbsContabil.format(getDataPagamento()));
		entrada.append(EbsContabil.format(getCodigoOperacaoContabil(), 4));
		entrada.append(EbsContabil.format(" ", 6));
		entrada.append(EbsContabil.format(getIndentificacaoExterior(), 18));
		entrada.append(EbsContabil.format(getValorINSS(), 12, 2));
		entrada.append(EbsContabil.format(getValorFUNRURAL(), 12, 2));
		entrada.append(EbsContabil.format(getCodigoItemServico(), 4));

		//entrada.append(EbsContabil.format(" ", 18));
		//entrada.append(EbsContabil.format(" ", 5));
		// Novas implementações
		// ISS RETIDO S/N
		// ISS DEVIDO PRESTAÇÃO S/N
		entrada.append(EbsContabil.format(getIssRetido(), 1));
		entrada.append(EbsContabil.format(getIssDevido(), 1));
		// UF PRESTACAO
		entrada.append(EbsContabil.format(getUfPrestacao(), 2));
		// Municipio da prestação
		entrada.append(EbsContabil.format(" ", 7));
		// Tipo de emissão T - Terceiros ou P para própria
		entrada.append(EbsContabil.format(getTipoEmissao(), 1));
		entrada.append(EbsContabil.format(" ", 6));
		entrada.append(EbsContabil.format(" ", 5));
				
		entrada.append(EbsContabil.format(getSequencial(), 6));

		return entrada.toString();
	}
}
