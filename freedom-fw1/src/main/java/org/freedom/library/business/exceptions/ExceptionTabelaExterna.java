package org.freedom.library.business.exceptions;

public class ExceptionTabelaExterna extends Exception {

	private static final long serialVersionUID = 1L;

	public ExceptionTabelaExterna() {
		super();
	}

	public ExceptionTabelaExterna(String message, Throwable cause) {
		super(message, cause);
	}

	public ExceptionTabelaExterna(String message) {
		super(message);
	}

	public ExceptionTabelaExterna(Throwable cause) {
		super(cause);
	}

}
