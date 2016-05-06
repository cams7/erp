
package org.freedom.ecf.driver;

public final class StatusStandard implements Status {

	public final static StatusStandard RETORNO_OK = new StatusStandard( 0, RELEVANC_MESSAGE, "RETORNO_OK" );

	public final static StatusStandard IMPRESSORA_OK = new StatusStandard( 100, RELEVANC_ERRO, "Impressora OK" );

	public final static StatusStandard RETORNO_INDEFINIDO = new StatusStandard( 101, RELEVANC_ERRO, "Retorno indefinido: " );

	public final static StatusStandard FUNCAO_NAO_IMPLEMENTADA = new StatusStandard( -1, RELEVANC_ERRO, "Comando não implementado pelo driver de comunicação." );

	public final static StatusStandard IMPRESSORA_SEM_COMUNICACAO = new StatusStandard( -1, RELEVANC_ERRO, "Impressora não responde." );

	private String message;

	private int code;

	private int relevanc;

	private StatusStandard( int code, int relevanc, String message ) {

		this.code = code;
		this.relevanc = relevanc;
		this.message = message;
	}

	public int getCode() {

		return code;
	}

	public String getMessage() {

		return message;
	}

	public int getRelevanc() {

		return relevanc;
	}
}
