package org.freedom.library.business.exceptions;

public class ExceptionCarregaItem extends Exception {

	private static final long serialVersionUID = 1L;

	public ExceptionCarregaItem() {
		super();
	}

	public ExceptionCarregaItem(String message, Throwable cause) {
		super(message, cause);
	}

	public ExceptionCarregaItem(String message) {
		super(message);
	}

	public ExceptionCarregaItem(Throwable cause) {
		super(cause);
	}

}
