package org.freedom.library.business.exceptions;

public class ExceptionSiacc extends Exception {

	private static final long serialVersionUID = 1L;

	public ExceptionSiacc() {
		super();
	}

	public ExceptionSiacc(String message, Throwable cause) {
		super(message, cause);
	}

	public ExceptionSiacc(String message) {
		super(message);
	}

	public ExceptionSiacc(Throwable cause) {
		super(cause);
	}

}
