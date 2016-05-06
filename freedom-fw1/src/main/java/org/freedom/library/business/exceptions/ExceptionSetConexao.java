package org.freedom.library.business.exceptions;

public class ExceptionSetConexao extends Exception {

	private static final long serialVersionUID = 1L;

	public ExceptionSetConexao() {
		super();
	}

	public ExceptionSetConexao(String message, Throwable cause) {
		super(message, cause);
	}

	public ExceptionSetConexao(String message) {
		super(message);
	}

	public ExceptionSetConexao(Throwable cause) {
		super(cause);
	}

}
