package org.freedom.infra.functions;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SMTPAuthenticator extends Authenticator {

	private final String username;

	private final String password;

	public SMTPAuthenticator(String username, String password) {

		this.username = username;
		this.password = password;

	}

	public PasswordAuthentication getPasswordAuthentication() {

		return new PasswordAuthentication(username, password);
	}
}
