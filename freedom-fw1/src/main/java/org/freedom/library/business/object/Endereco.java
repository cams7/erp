package org.freedom.library.business.object;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;

import java.util.HashMap;

public class Endereco {
	private String tipo = "";
	private String logradouro = "";
	private String complemento = "";
	private String bairro = "";
	private String cidade = "";
	private String siglauf = "";
	private String cep = "";
	private String codmunic = "";
	private Integer codpais = null;

	public Endereco(final String cep, final HashMap<?, ?> endereco, DbConnection con) {
		setCep(cep);
		setTipo(endereco.get("tipo").toString());
		setLogradouro(endereco.get("logradouro").toString());
		setBairro(endereco.get("bairro").toString());
		setCidade(endereco.get("cidade").toString());
		setSiglauf(endereco.get("siglauf").toString());

		HashMap<String, Object> hsCidade = Funcoes.getCodMunic(getCidade(), getSiglauf(), con);

		setCodpais(( Integer ) hsCidade.get("CODPAIS"));
		setSiglauf(( String ) hsCidade.get("SIGLAUF"));
		setCodmunic(( String ) hsCidade.get("CODMUNIC"));
	}

	public String getCep() {

		return cep;
	}

	public void setCep(String cep) {

		this.cep = cep;
	}

	public String getCidade() {

		return cidade;
	}

	public void setCidade(String cidade) {

		this.cidade = cidade;
	}

	public String getComplemento() {

		return complemento;
	}

	public void setComplemento(String complemento) {

		this.complemento = complemento;
	}

	public String getLogradouro() {

		return logradouro;
	}

	public void setLogradouro(String logradouro) {

		this.logradouro = logradouro;
	}

	public String getTipo() {

		return tipo;
	}

	public void setTipo(String tipo) {

		this.tipo = tipo;
	}

	public String getCodmunic() {

		return codmunic;
	}

	public void setCodmunic(String codmunic) {

		this.codmunic = codmunic;
	}

	public Integer getCodpais() {

		return codpais;
	}

	public void setCodpais(Integer codpais) {

		this.codpais = codpais;
	}

	public String getSiglauf() {

		return siglauf;
	}

	public void setSiglauf(String siglauf) {

		this.siglauf = siglauf;
	}

	public String getBairro() {

		return bairro;
	}

	public void setBairro(String bairro) {

		this.bairro = bairro;
	}
}
