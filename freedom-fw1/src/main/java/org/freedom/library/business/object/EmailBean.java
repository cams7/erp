package org.freedom.library.business.object;

import java.util.Calendar;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.freedom.infra.functions.SMTPAuthenticator;
import org.freedom.infra.util.crypt.SimpleCrypt;

public class EmailBean {

	private String assunto;
	private String host;
	private int porta;
	private String usuario;
	private String senha;
	private String criptSenha;
	private String de;
	private String para;
	private String copia;
	private String autentica;
	private String ssl;
	private String emailresp;
	private String corpo;
	private String formato;
	private String charset;
	private String assinatura;
	private Session session;

	public String getAssinatura() {

		return assinatura;
	}

	public void setAssinatura(String assinatura) {

		this.assinatura = assinatura;
	}

	public String getCopia() {
		return copia;
	}

	public void setCopia(String copia) {
		this.copia = copia;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public String getCharset() {

		return charset;
	}

	public void setCharset(String charset) {

		this.charset = charset;
	}

	public EmailBean() {
	}

	public String getCorpo() {
		return corpo;
	}

	public void setCorpo(String corpo) {
		this.corpo = corpo;
	}

	public String getEmailResp() {
		return emailresp;
	}

	public void setEmailResp(String emailresp) {
		this.emailresp = emailresp;
	}

	public String getAutentica() {
		return autentica;
	}

	public boolean getBooleanAutentica() {
		return "S".equals(getAutentica());
	}

	public void setAutentica(String autentica) {
		this.autentica = autentica;
	}

	public String getDe() {
		return de;
	}

	public void setDe(String from) {
		this.de = from;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String passord) {
		this.senha = passord;
	}

	public int getPorta() {
		return porta;
	}

	public void setPorta(int port) {
		this.porta = port;
	}

	public String getSsl() {
		return ssl;
	}

	public boolean getBooleanSsl() {
		return "S".equals(getSsl());
	}

	public void setSsl(String ssl) {
		this.ssl = ssl;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String titulo) {
		this.assunto = titulo;
	}

	public String getPara() {
		return para;
	}

	public void setPara(String to) {
		this.para = to;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String user) {
		this.usuario = user;
	}

	public EmailBean getClone() {

		EmailBean clone = new EmailBean();

		clone.setAssunto(getAssunto());
		clone.setHost(getHost());
		clone.setPorta(getPorta());
		clone.setUsuario(getUsuario());
		clone.setSenha(getSenha());
		clone.setCriptSenha(getCriptSenha());
		clone.setDe(getDe());
		clone.setEmailResp(getEmailResp());
		clone.setPara(getPara());
		clone.setFormato(getFormato());
		clone.setAutentica(getAutentica());
		clone.setSsl(getSsl());
		clone.setCorpo(getCorpo());
		clone.setAssinatura(getAssinatura());

		return clone;
	}


	public synchronized static MimeMessage getMessage(final Session session, EmailBean email) throws Exception {

		MimeMessage msg = null;
		try {
			InternetAddress from[] = { new InternetAddress(email.getDe().trim()) };
			InternetAddress resp[] = { new InternetAddress(email.getEmailResp().trim()) };
			msg = new MimeMessage(session);
			msg.setFrom(from[0]);
			msg.setReplyTo(resp);

			InternetAddress[] address = null;

			if(email.getCopia()!=null && !email.getCopia().trim().equals("")) {
			
				address = new InternetAddress[] { new InternetAddress(email.getPara().trim()), new InternetAddress(email.getCopia().trim()) };

			}
			else {
				address = new InternetAddress[] { new InternetAddress(email.getPara().trim()) };

			}
				
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject(email.getAssunto().trim());

			MimeBodyPart mbp = new MimeBodyPart();

			mbp.setContent(email.getCorpo().trim(), email.getFormato());
			mbp.setHeader("MIME-Version", "1.0");
			mbp.setHeader("Content-Type", email.getFormato() + ";charset=\"" + email.getCharset() + "\"");

			MimeMultipart content = new MimeMultipart("alternative");
			content.addBodyPart(mbp);

			msg.setContent(content);

			msg.setHeader("MIME-Version", "1.0");
			msg.setHeader("Content-Type", content.getContentType());
			msg.setHeader("X-Mailer", "Java-Mailer");

			msg.setSentDate(Calendar.getInstance().getTime());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return msg;
	}

	public synchronized MimeMessage getMessage() throws Exception {

		MimeMessage msg = null;
		try {
			InternetAddress from[] = { new InternetAddress(getDe().trim()) };
			InternetAddress resp[] = { new InternetAddress(getEmailResp().trim()) };
			msg = new MimeMessage(getSession());
			msg.setFrom(from[0]);
			msg.setReplyTo(resp);

			InternetAddress[] address = null;

			address = new InternetAddress[] { new InternetAddress(getPara().trim()) };

			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject(getAssunto().trim());

			MimeBodyPart mbp = new MimeBodyPart();

			mbp.setContent(getCorpo().trim(), getFormato());
			mbp.setHeader("MIME-Version", "1.0");
			mbp.setHeader("Content-Type", getFormato() + ";charset=\"" + getCharset() + "\"");

			MimeMultipart content = new MimeMultipart("alternative");
			content.addBodyPart(mbp);

			msg.setContent(content);

			msg.setHeader("MIME-Version", "1.0");
			msg.setHeader("Content-Type", content.getContentType());
			msg.setHeader("X-Mailer", "Java-Mailer");

			msg.setSentDate(Calendar.getInstance().getTime());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return msg;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session sessao) {
		this.session = sessao;
	}

	public void createSession() {

		Session result = null;

		Properties props = new Properties();
		
		SMTPAuthenticator authenticator = new SMTPAuthenticator(getUsuario().trim(), getSenha().trim());

		try {
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.host", getHost().trim() );
			props.put("mail.smtp.port", String.valueOf( getPorta() ) );
			if ("S".equals(getSsl())) {
				props.put("mail.smtp.socketFactory.port", String.valueOf( getPorta() ) );
				props.put("mail.smtp.socketFactory.class",
						"javax.net.ssl.SSLSocketFactory");
			} /*else {
				props.put("mail.smtp.socketFactory.class", "javax.net.SocketFactory");
			}*/


			if ("S".equals(getAutentica())) {

				props.put("mail.smtp.auth", "true");
				// Se for autenticado e não for SSL, se torna necessário iniciar TLS 
				if ( ! "S".equals(getSsl())) {
				//	props.put("mail.smtp.starttls.enable", "true");
				}
				 
				result = Session.getDefaultInstance(props, authenticator); 
				
			} 
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		setSession(result);

	}

	public void setCriptSenha(String criptSenha) {
		this.criptSenha = criptSenha;
	}

	public void setSenha(String senha, String criptSenha) {
		this.criptSenha = criptSenha;
		if ("S".equals(criptSenha)) {
			this.senha = SimpleCrypt.decrypt(senha.trim());
		} else {
			this.senha = senha;
		}
	}
	
	public void send(Message msg) throws MessagingException {
		if ( msg != null ) {
			Transport.send( msg );
		}
	}
	
	public String getCriptSenha() {
		return criptSenha;
	}

}
