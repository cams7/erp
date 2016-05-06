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
public class SaidaVO {
	private final int tipoRegistro = 1;

	private Date dataLancamento;

	private int numeroInicial;

	private int numeroFinal;

	private Date dataEmissao;

	private int modelo;

	private String serie;

	private String subSerie;

	private int cfop;

	private int variacaoCfop;

	private int classificacao1;

	private int classificacao2;

	private String cnpjDestinatario;

	private String cpfDestinatario;

	private BigDecimal valorNota;

	private BigDecimal basePIS;

	private BigDecimal baseCOFINS;

	private BigDecimal baseCSLL;

	private BigDecimal baseIRPJ;

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

	private BigDecimal baseISS;

	private BigDecimal aliquotaISS;

	private BigDecimal valorISS;

	private BigDecimal valorISSIsentos;

	private BigDecimal valorIRRF;

	private String observacoesLivrosFiscais;

	private String especie;

	private String vendaAVista;

	private int cfopSubTributaria;

	private BigDecimal valorPISCOFINS;

	private int modalidadeFrete;

	private BigDecimal valorPIS;

	private BigDecimal valorCOFINS;

	private BigDecimal valorCSLL;

	private Date dataRecebimento;

	private int operacaoContabil;

	private BigDecimal valorMateriais;

	private BigDecimal valorSubEmpreitada;

	private int codigoServico;

	private int clifor;

	private String indentificadorExterior;

	private int sequencial;
	
	public SaidaVO() {
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

	public int getNumeroInicial() {
		return numeroInicial;
	}

	public void setNumeroInicial(int numeroInicial) {
		this.numeroInicial = numeroInicial;
	}

	public int getNumeroFinal() {
		return numeroFinal;
	}

	public void setNumeroFinal(int numeroFinal) {
		this.numeroFinal = numeroFinal;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public int getModelo() {
		return modelo;
	}

	public void setModelo(int modelo) {
		this.modelo = modelo;
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

	public int getClassificacao1() {
		return classificacao1;
	}

	public void setClassificacao1(int classificacao1) {
		this.classificacao1 = classificacao1;
	}

	public int getClassificacao2() {
		return classificacao2;
	}

	public void setClassificacao2(int classificacao2) {
		this.classificacao2 = classificacao2;
	}

	public String getCnpjDestinatario() {
		return cnpjDestinatario;
	}

	public void setCnpjDestinatario(String cnpjDestinatario) {
		this.cnpjDestinatario = cnpjDestinatario;
	}

	public String getCpfDestinatario() {
		return cpfDestinatario;
	}

	public void setCpfDestinatario(String cpfDestinatario) {
		this.cpfDestinatario = cpfDestinatario;
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

	public BigDecimal getBaseIRPJ() {
		return baseIRPJ;
	}

	public void setBaseIRPJ(BigDecimal baseIRPJ) {
		this.baseIRPJ = baseIRPJ;
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

	public BigDecimal getBaseISS() {
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

	public BigDecimal getValorISSIsentos() {
		return valorISSIsentos;
	}

	public void setValorISSIsentos(BigDecimal valorISSIsentos) {
		this.valorISSIsentos = valorISSIsentos;
	}

	public BigDecimal getValorIRRF() {
		return valorIRRF;
	}

	public void setValorIRRF(BigDecimal valorIRRF) {
		this.valorIRRF = valorIRRF;
	}

	public String getObservacoesLivrosFiscais() {
		return observacoesLivrosFiscais;
	}

	public void setObservacoesLivrosFiscais(String observacoesLivrosFiscais) {
		this.observacoesLivrosFiscais = observacoesLivrosFiscais;
	}

	public String getEspecie() {
		return especie;
	}

	public void setEspecie(String especie) {
		this.especie = especie;
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

	public BigDecimal getValorPISCOFINS() {
		return valorPISCOFINS;
	}

	public void setValorPISCOFINS(BigDecimal valorPISCOFINS) {
		this.valorPISCOFINS = valorPISCOFINS;
	}

	public int getModalidadeFrete() {
		return modalidadeFrete;
	}

	public void setModalidadeFrete(int modalidadeFrete) {
		this.modalidadeFrete = modalidadeFrete;
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

	public Date getDataRecebimento() {
		return dataRecebimento;
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	public int getOperacaoContabil() {
		return operacaoContabil;
	}

	public void setOperacaoContabil(int operacaoContabil) {
		this.operacaoContabil = operacaoContabil;
	}

	public BigDecimal getValorMateriais() {
		return valorMateriais;
	}

	public void setValorMateriais(BigDecimal valorMateriais) {
		this.valorMateriais = valorMateriais;
	}

	public BigDecimal getValorSubEmpreitada() {
		return valorSubEmpreitada;
	}

	public void setValorSubEmpreitada(BigDecimal valorSubEmpreitada) {
		this.valorSubEmpreitada = valorSubEmpreitada;
	}

	public int getCodigoServico() {
		return codigoServico;
	}

	public void setCodigoServico(int codigoServico) {
		this.codigoServico = codigoServico;
	}

	public int getClifor() {
		return clifor;
	}

	public void setClifor(int clifor) {
		this.clifor = clifor;
	}

	public String getIndentificadorExterior() {
		return indentificadorExterior;
	}

	public void setIndentificadorExterior(String indentificadorExterior) {
		this.indentificadorExterior = indentificadorExterior;
	}

	public int getSequencial() {
		return sequencial;
	}

	public void setSequencial(int sequencial) {
		this.sequencial = sequencial;
	}

	@Override
	public String toString() {

		StringBuilder saida = new StringBuilder(500);

		saida.append(getTipoRegistro());
		saida.append(EbsContabil.format(getDataLancamento()));
		saida.append(EbsContabil.format(getNumeroInicial(), 6));
		saida.append(EbsContabil.format(getNumeroFinal(), 6));
		saida.append(EbsContabil.format(getDataEmissao()));
		saida.append(EbsContabil.format(" ", 3));
		saida.append(EbsContabil.format(getModelo(), 2));
		saida.append(EbsContabil.format(getSerie(), 3));
		saida.append(EbsContabil.format(getSubSerie(), 3));
		saida.append(EbsContabil.format(getCfop(), 4));
		saida.append(EbsContabil.format(getVariacaoCfop(), 2));
		saida.append(EbsContabil.format(getClassificacao1(), 2));
		saida.append(EbsContabil.format(getClassificacao2(), 2));

		// saida.append( EbsContabil.format( Funcoes.setMascara( getCnpjDestinatario(),
		// "##.###.###/####-##" ), 18 ) );

		if (getCnpjDestinatario() != null) {
			saida.append(EbsContabil.format(Funcoes.setMascara(getCnpjDestinatario(), "##.###.###/####-##"), 18));
		}
		else {
			saida.append(EbsContabil.format(Funcoes.setMascara(getCpfDestinatario(), "###.###.###-##"), 18));
		}

		saida.append(EbsContabil.format(getValorNota(), 12, 2));
		saida.append(EbsContabil.format(getBasePIS(), 12, 2));
		saida.append(EbsContabil.format(getBaseCOFINS(), 12, 2));
		saida.append(EbsContabil.format(getBaseCSLL(), 12, 2));
		saida.append(EbsContabil.format(getBaseIRPJ(), 12, 2));
		saida.append(EbsContabil.format(" ", 8));
		saida.append(EbsContabil.format(getBaseICMSa(), 12, 2));
		saida.append(EbsContabil.format(getAliquotaICMSa(), 4, 2));
		saida.append(EbsContabil.format(getValorICMSa(), 12, 2));
		saida.append(EbsContabil.format(getBaseICMSb(), 12, 2));
		saida.append(EbsContabil.format(getAliquotaICMSb(), 4, 2));
		saida.append(EbsContabil.format(getValorICMSb(), 12, 2));
		saida.append(EbsContabil.format(getBaseICMSc(), 12, 2));
		saida.append(EbsContabil.format(getAliquotaICMSc(), 4, 2));
		saida.append(EbsContabil.format(getValorICMSc(), 12, 2));
		saida.append(EbsContabil.format(getBaseICMSd(), 12, 2));
		saida.append(EbsContabil.format(getAliquotaICMSd(), 4, 2));
		saida.append(EbsContabil.format(getValorICMSd(), 12, 2));
		saida.append(EbsContabil.format(getValorICMSIsentas(), 12, 2));
		saida.append(EbsContabil.format(getValorICMSOutras(), 12, 2));
		saida.append(EbsContabil.format(getBaseIPI(), 12, 2));
		saida.append(EbsContabil.format(getValorIPI(), 12, 2));
		saida.append(EbsContabil.format(getValorIPIIsentas(), 12, 2));
		saida.append(EbsContabil.format(getValorIPIOutras(), 12, 2));
		saida.append(EbsContabil.format(getValorSubTributaria(), 12, 2));
		saida.append(EbsContabil.format(getBaseSubTributaria(), 12, 2));
		saida.append(EbsContabil.format(getValorICMSSubTributaria(), 12, 2));
		saida.append(EbsContabil.format(getValorDiferidas(), 12, 2));
		saida.append(EbsContabil.format(getBaseISS(), 12, 2));
		saida.append(EbsContabil.format(getAliquotaISS(), 4, 2));
		saida.append(EbsContabil.format(getValorISS(), 12, 2));
		saida.append(EbsContabil.format(getValorISSIsentos(), 12, 2));
		saida.append(EbsContabil.format(getValorIRRF(), 12, 2));
		saida.append(EbsContabil.format(getObservacoesLivrosFiscais(), 50));
		saida.append(EbsContabil.format(getEspecie(), 5));
		saida.append(EbsContabil.format(getVendaAVista(), 1));
		saida.append(EbsContabil.format(getCfopSubTributaria(), 4));
		saida.append(EbsContabil.format(getValorPISCOFINS(), 8, 2));
		saida.append(EbsContabil.format(getModalidadeFrete(), 1));
		saida.append(EbsContabil.format(getValorPIS(), 12, 2));
		saida.append(EbsContabil.format(getValorCOFINS(), 12, 2));
		saida.append(EbsContabil.format(getValorCSLL(), 12, 2));
		saida.append(EbsContabil.format(getDataRecebimento()));
		saida.append(EbsContabil.format(getOperacaoContabil(), 4));
		saida.append(EbsContabil.format(getValorMateriais(), 12, 2));
		saida.append(EbsContabil.format(getValorSubEmpreitada(), 12, 2));
		saida.append(EbsContabil.format(getCodigoServico(), 4));
		saida.append(EbsContabil.format(getClifor(), 6));
		saida.append(EbsContabil.format(getIndentificadorExterior(), 18));
		saida.append(EbsContabil.format(" ", 5));
		saida.append(EbsContabil.format(getSequencial(), 6));

		return saida.toString();
	}
}
