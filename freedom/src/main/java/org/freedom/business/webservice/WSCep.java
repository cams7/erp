package org.freedom.business.webservice;

import java.util.HashMap;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.object.Endereco;
import org.freedom.library.functions.Funcoes;

public class WSCep {

	private String cep = null;

	private DbConnection con = null;

	private HashMap<String, String> endereco = new HashMap<String, String>();

	public void setCep( String cep ) {

		this.cep = cep;
	}

	public void setCon( DbConnection con ) {

		this.con = con;
	}

	public Endereco getEndereco() {

		return new Endereco( cep, endereco, con );
	}

	public void busca() throws Exception {
		
		WebServiceCep.setUrl( Funcoes.getURLWsCep( con ) );
		WebServiceCep webServiceCep = WebServiceCep.searchCep( cep );

		if ( webServiceCep.wasSuccessful() ) {
			System.out.println( "Cep: " + webServiceCep.getCep() );
			System.out.println( "Logradouro: " + webServiceCep.getLogradouroFull() );
			System.out.println( "Bairro: " + webServiceCep.getBairro() );
			System.out.println( "Cidade: " + webServiceCep.getCidade() + "/" + webServiceCep.getUf() );

			endereco.put( "tipo", webServiceCep.getLogradouroType() );
			endereco.put( "logradouro", webServiceCep.getLogradouro() );
			// endereco.put( "complemento", webserv);
			endereco.put( "bairro", webServiceCep.getBairro() );
			endereco.put( "siglauf", webServiceCep.getUf() );
			endereco.put( "cidade", webServiceCep.getCidade() );

		}
		else {
			new Exception();
		}
	}

}
