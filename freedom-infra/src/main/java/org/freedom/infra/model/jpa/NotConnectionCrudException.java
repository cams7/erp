package org.freedom.infra.model.jpa;

public class NotConnectionCrudException extends Exception {

	private static final long serialVersionUID = -2465550210342268934L;

	public NotConnectionCrudException() {
	}

	public NotConnectionCrudException(String message) {
		super(message);
	}

	public NotConnectionCrudException(Throwable cause) {
		super(cause);
	}

	public NotConnectionCrudException(String message, Throwable cause) {
		super(message, cause);
	}
}
