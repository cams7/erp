package org.freedom.library.business.componet.integration.ebs.vo;

import org.freedom.library.business.componet.integration.ebs.EbsContabil;
import org.freedom.library.functions.Funcoes;

/**
 * 
 * @author ffrizzo
 * since 11/12/2010
 */
public class EmitenteDestinatarioVO {

	private final int tipoRegistro = 4;

	private String cnpj;

	private String cpf;

	private String razaoSocial;

	private String nomeFantazia;

	private String estado;

	private String inscricao;

	private String endereco;

	private String bairro;

	private String cidade;

	private int cep;

	private int municipio;

	private int ddd;

	private int telefone;

	private int contaCliente;

	private int historicoCliente;

	private int contaFornecedor;

	private boolean produtor;

	private int historicoFornecedor;

	private String indentificacaoExterior;

	private int numero;

	private String complemento;

	private String suframa;

	private int pais;

	private int sequencial;
	
	private String ibge;
	
	public EmitenteDestinatarioVO() {
	}

	public int getTipoRegistro() {
		return tipoRegistro;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getNomeFantazia() {
		return nomeFantazia;
	}

	public void setNomeFantazia(String nomeFantazia) {
		this.nomeFantazia = nomeFantazia;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getInscricao() {
		return inscricao;
	}

	public void setInscricao(String inscricao) {
		this.inscricao = inscricao != null ? inscricao.replaceAll("\\D", "") : null;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public int getCep() {
		return cep;
	}

	public void setCep(int cep) {
		this.cep = cep;
	}

	public int getMunicipio() {
		return municipio;
	}

	public void setMunicipio(int municipio) {
		this.municipio = municipio;
	}

	public int getDdd() {
		return ddd;
	}

	public void setDdd(int ddd) {
		this.ddd = ddd;
	}

	public int getTelefone() {
		return telefone;
	}

	public void setTelefone(int telefone) {
		this.telefone = telefone;
	}

	public int getContaCliente() {
		return contaCliente;
	}

	public void setContaCliente(int contaCliente) {
		this.contaCliente = contaCliente;
	}

	public int getHistoricoCliente() {
		return historicoCliente;
	}

	public void setHistoricoCliente(int historicoCliente) {
		this.historicoCliente = historicoCliente;
	}

	public int getContaFornecedor() {
		return contaFornecedor;
	}

	public void setContaFornecedor(int contaFornecedor) {
		this.contaFornecedor = contaFornecedor;
	}

	public boolean isProdutor() {
		return produtor;
	}

	public void setProdutor(boolean produtor) {
		this.produtor = produtor;
	}

	public int getHistoricoFornecedor() {
		return historicoFornecedor;
	}

	public void setHistoricoFornecedor(int historicoFornecedor) {
		this.historicoFornecedor = historicoFornecedor;
	}

	public String getIndentificacaoExterior() {
		return indentificacaoExterior;
	}

	public void setIndentificacaoExterior(String indentificacaoExterior) {
		this.indentificacaoExterior = indentificacaoExterior;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getSuframa() {
		return suframa;
	}

	public void setSuframa(String suframa) {
		this.suframa = suframa;
	}

	public int getPais() {
		return pais;
	}

	public void setPais(int pais) {
		this.pais = pais;
	}

	public int getSequencial() {
		return sequencial;
	}

	public void setSequencial(int sequencial) {
		this.sequencial = sequencial;
	}
	
	public String getIbge() {
		return ibge;
	}

	public void setIbge(String ibge) {
		this.ibge = ibge;
	}

	@Override
	public String toString() {

		StringBuilder emitenteDestinatario = new StringBuilder(500);

		emitenteDestinatario.append(getTipoRegistro());
		if (getCnpj() != null) {
			emitenteDestinatario.append(EbsContabil.format(Funcoes.setMascara(getCnpj(), "##.###.###/####-##"), 18));
		}
		else {
			emitenteDestinatario.append(EbsContabil.format(Funcoes.setMascara(getCpf(), "###.###.###-##"), 18));
		}
		emitenteDestinatario.append(EbsContabil.format(getRazaoSocial(), 40));
		emitenteDestinatario.append(EbsContabil.format(getNomeFantazia(), 20));
		emitenteDestinatario.append(EbsContabil.format(getEstado(), 2));
		emitenteDestinatario.append(EbsContabil.format(getInscricao(), 20));
		emitenteDestinatario.append(EbsContabil.format(getEndereco(), 40));
		emitenteDestinatario.append(EbsContabil.format(getBairro(), 20));
		emitenteDestinatario.append(EbsContabil.format(getCidade(), 20));
		emitenteDestinatario.append(EbsContabil.format(getCep(), 8));
		emitenteDestinatario.append(EbsContabil.format(" ", 4));
		emitenteDestinatario.append(EbsContabil.format(getDdd(), 3));
		emitenteDestinatario.append(EbsContabil.format(getTelefone(), 10));
		emitenteDestinatario.append(EbsContabil.format(getContaCliente(), 6));
		emitenteDestinatario.append(EbsContabil.format(getHistoricoCliente(), 3));
		emitenteDestinatario.append(EbsContabil.format(getContaFornecedor(), 6));
		emitenteDestinatario.append(EbsContabil.format(getHistoricoFornecedor(), 3));
		emitenteDestinatario.append(isProdutor() ? "S" : "N");
		emitenteDestinatario.append(EbsContabil.format(getIndentificacaoExterior(), 18));
		emitenteDestinatario.append(EbsContabil.format(getNumero(), 5));
		emitenteDestinatario.append(EbsContabil.format(getComplemento(), 20));
		emitenteDestinatario.append(EbsContabil.format(getSuframa(), 9));
		emitenteDestinatario.append(EbsContabil.format(getPais(), 5));
		//IBGE
		emitenteDestinatario.append(" ");
		emitenteDestinatario.append(EbsContabil.format(getIbge(), 7));
		
		//emitenteDestinatario.append(EbsContabil.format(" ", 207));
		//emitenteDestinatario.append(EbsContabil.format(" ", 5));
		emitenteDestinatario.append(EbsContabil.format(" ", 199));
		emitenteDestinatario.append(EbsContabil.format(" ", 005));
		emitenteDestinatario.append(EbsContabil.format(getSequencial(), 6));

		return emitenteDestinatario.toString();
	}

}
