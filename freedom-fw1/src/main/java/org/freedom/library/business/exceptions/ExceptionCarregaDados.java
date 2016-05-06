package org.freedom.library.business.exceptions;

public class ExceptionCarregaDados extends Exception {

	private static final long serialVersionUID = 1L;

	public ExceptionCarregaDados() {
		super();
	}

	public ExceptionCarregaDados(String message, Throwable cause) {
		super(message, cause);
	}

	public ExceptionCarregaDados(String message) {
		super(message);
	}

	public ExceptionCarregaDados(Throwable cause) {
		super(cause);
	}

}
