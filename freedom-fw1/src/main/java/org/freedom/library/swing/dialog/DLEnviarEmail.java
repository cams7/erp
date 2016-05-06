/**
 * @version 05/2007 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * @author Alex Rodrigues
 * 
 * Projeto: Freedom <BR>
 * 
 * Pacote: org.freedom.modulos.rep <BR>
 * Classe:
 * @(#)DLEnviaPedido.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * Formulário para envio do pedido por e-mail.
 */

package org.freedom.library.swing.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import org.freedom.infra.model.jdbc.DbConnection;
import java.util.Calendar;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.freedom.acao.Processo;
import org.freedom.bmps.Icone;
import org.freedom.library.business.component.ProcessoSec;
import org.freedom.library.business.object.EmailBean;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JPasswordFieldPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.rep.RPPrefereGeral;

public class DLEnviarEmail extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JPanelPad panelRodape = null;

	private final JTextFieldPad txtHost = new JTextFieldPad(JTextFieldPad.TP_STRING, 100, 0);

	private final JTextFieldPad txtPort = new JTextFieldPad(JTextFieldPad.TP_STRING, 100, 0);

	private final JTextFieldPad txtFrom = new JTextFieldPad(JTextFieldPad.TP_STRING, 100, 0);

	private final JTextFieldPad txtTo = new JTextFieldPad(JTextFieldPad.TP_STRING, 100, 0);

	private final JTextFieldPad txtAssunto = new JTextFieldPad(JTextFieldPad.TP_STRING, 120, 0);

	private final JTextFieldPad txtUser = new JTextFieldPad(JTextFieldPad.TP_STRING, 50, 0);

	private final JPasswordFieldPad txtPassword = new JPasswordFieldPad(30);

	private final JTextAreaPad txtMessage = new JTextAreaPad(1000);

	private final JButtonPad btEnviar = new JButtonPad("Enviar", Icone.novo("btEnviarMail.png"));

	private final JLabel status = new JLabel();
	
	private static final String UTF8 = "UTF-8";

	private JasperPrint report = null;

	private EmailBean mail = null;

	private boolean preparado = false;

	private final JCheckBoxPad cbComCopia = new JCheckBoxPad("Com cópia?", "S", "N");

	private String anexo = null;

	public DLEnviarEmail(final Component cOrig, final EmailBean mail, final String anexo) {

		super(cOrig);
		setTitulo("Enviar por e-mail");
		setAtribos(405, 470);
		setResizable(false);

		this.mail = mail;
		this.anexo = anexo;

		montaTela();

		btEnviar.addActionListener(this);
	}

	private void montaTela() {

		adic(new JLabel("De:"), 10, 10, 265, 20);
		adic(txtUser, 10, 30, 265, 20);

		adic(new JLabel("Senha"), 280, 10, 100, 20);
		adic(txtPassword, 280, 30, 100, 20);

		adic(new JLabel("Para:"), 10, 50, 265, 20);
		adic(txtTo, 10, 70, 265, 20);

		adic(cbComCopia, 276, 68, 150, 20);

		adic(new JLabel("Assunto:"), 10, 90, 370, 20);
		adic(txtAssunto, 10, 110, 370, 20);

		adic(new JLabel("Mensagem:"), 10, 130, 370, 20);
		adic(new JScrollPane(txtMessage), 10, 150, 370, 240);

		adic(status, 10, 300, 370, 20);

		status.setForeground(Color.BLUE);

		panelRodape = adicBotaoSair();

		btEnviar.setPreferredSize(new Dimension(100, 26));
		panelRodape.add(btEnviar, BorderLayout.WEST);
	}

	public void setReport(final JasperPrint report) {

		this.report = report;
	}

	private void setStatus(final String msg) {

		status.setText(msg != null ? msg : "");
	}

	public void preparar() {

		while (!validaEmailBean(mail)) {

			// mail = getEmailBean( mail );

			if (mail == null) {
				break;
			}
		}

		if (mail != null) {

			txtHost.setVlrString(mail.getHost());
			txtPort.setVlrInteger(mail.getPorta());
			txtUser.setVlrString(mail.getUsuario());
			txtPassword.setVlrString(mail.getSenha());
			txtFrom.setVlrString(mail.getDe());
			txtTo.setVlrString(mail.getPara());
			txtAssunto.setVlrString(mail.getAssunto());

			StringBuilder msg = new StringBuilder();

			if (mail.getCorpo() != null) {
				msg.append(mail.getCorpo());
			}
			if (mail.getAssinatura() != null) {
				msg.append("\n");
				msg.append(mail.getAssinatura());
			}

			txtMessage.setVlrString(msg.toString());

			preparado = true;
		}
	}

	/*
	 * private EmailBean getEmailBean( EmailBean mail ) {
	 * 
	 * DLEmailBean dlemail = new DLEmailBean( mail ); dlemail.setVisible( true
	 * ); mail = dlemail.getEmailBean();
	 * 
	 * if ( mail != null ) {
	 * 
	 * Aplicativo.getInstace().updateEmailBean( mail ); }
	 * 
	 * return mail; }
	 */
	private boolean validaEmailBean(EmailBean mail) {

		boolean ok = false;

		if (mail != null) {

			String host = mail.getHost();
			String from = mail.getDe();
			String autentica = mail.getAutentica();
			String ssl = mail.getSsl();
			// String assinatura = mail.getAssinatura();
			int porta = mail.getPorta();

			if (( host != null && host.trim().length() > 0 ) && ( from != null && from.trim().length() > 0 ) && ( autentica != null && autentica.trim().length() > 0 )
					&& ( ssl != null && ssl.trim().length() > 0 ) && ( porta > 0 )) {
				ok = true;
			}
		}

		return ok;
	}

	public boolean preparado() {
		return preparado;
	}

	private boolean validaEnviar() {

		boolean retorno = false;

		validar: {

			if (txtHost.getVlrString() == null || txtHost.getVlrString().trim().length() == 0) {
				Funcoes.mensagemErro(this, "Servidor SMTP inválido!\nVerifique as preferências do sistema.");
				dispose();
				break validar;
			}
			if (txtTo.getVlrString() == null || txtTo.getVlrString().trim().length() == 0) {
				Funcoes.mensagemErro(this, "Email da filial inválido!\nVerifique o cadastro da filial.");
				dispose();
				break validar;
			}
			if (txtFrom.getVlrString() == null || txtFrom.getVlrString().trim().length() == 0) {
				Funcoes.mensagemErro(this, "E-mail não informado!");
				break validar;
			}
			if (txtUser.getVlrString() == null || txtUser.getVlrString().trim().length() == 0) {
				Funcoes.mensagemErro(this, "Usuário não informado!");
				break validar;
			}
			if (txtPassword.getVlrString() == null || txtPassword.getVlrString().trim().length() == 0) {
				Funcoes.mensagemErro(this, "Senha não informada!");
				break validar;
			}

			retorno = true;
		}

		return retorno;
	}

	private void enviar() {

		boolean enviado = false;

		if (validaEnviar()) {

			DLLoading loading = new DLLoading();

			try {

				loading.start();
				mail.createSession();
				MimeMessage msg = getMessage(mail.getSession());

				if (msg != null) {
					setStatus("Enviando e-mail...");
					mail.send(msg);
					enviado = true;
				}


			}
			catch (Exception e) {
				loading.stop();
				Funcoes.mensagemErro(this, "Erro ao enviar pedido!\n" + e.getMessage(), true, con, e);
				e.printStackTrace();
			}
			finally {
				loading.stop();
				if (enviado) {
					Funcoes.mensagemInforma(this, "E-mail enviado com sucesso.");
				}
			}

			setStatus(null);
		}
	}

	private MimeMessage getMessage(final Session session) throws Exception {

		InternetAddress from[] = new InternetAddress[1];
		MimeMessage msg = null;
		String sDestinatario = null;

		try {

			Multipart mp = new MimeMultipart();

			from[0] = new InternetAddress(txtFrom.getVlrString());
			msg = new MimeMessage(session);
			sDestinatario = txtTo.getVlrString();
			String[] sDestinatarios = Funcoes.strToStrArray(sDestinatario, ",");
			InternetAddress[] iDestinatarios = new InternetAddress[sDestinatarios.length];

			msg.setFrom(from[0]);
			msg.setReplyTo(from);

			for (int i = 0; i < sDestinatarios.length; i++) {
				iDestinatarios[i] = new InternetAddress(sDestinatarios[i]);
			}

			msg.setRecipients(Message.RecipientType.TO, iDestinatarios);

			if ("S".equals(cbComCopia.getVlrString())) {
				msg.setRecipient(Message.RecipientType.CC, from[0]);
			}

			msg.setSubject(txtAssunto.getVlrString(), UTF8);

			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setText(txtMessage.getVlrString(), UTF8);

			mp.addBodyPart(mbp1);

			if (report != null) {
				setStatus("Criando arquivo de anexo em " + Aplicativo.strTemp);

				String filename = Aplicativo.strTemp + Calendar.getInstance().getTimeInMillis() + ".pdf";
				JasperExportManager.exportReportToPdfFile(report, filename);
				File file = new File(filename);

				if (!file.exists()) {
					Funcoes.mensagemErro(this, "Anexo não foi criado.\nVerifique o parametro 'temp' no arquivo de parametros.");
					return null;
				}

				setStatus("Anexando arquivo...");

				FileDataSource fds = new FileDataSource(filename);
				MimeBodyPart mbp2 = new MimeBodyPart();
				mbp2.setDataHandler(new DataHandler(fds));
				mbp2.setFileName(fds.getName());

				mp.addBodyPart(mbp2);
			}
			else if (anexo != null) {
				setStatus("Criando arquivo de anexo em " + Aplicativo.strTemp);

				File file = new File(anexo);

				if (!file.exists()) {
					Funcoes.mensagemErro(this, "Anexo não foi criado.\nVerifique o parametro 'temp' no arquivo de parametros.");
					return null;
				}

				setStatus("Anexando arquivo...");

				FileDataSource fds = new FileDataSource(anexo);
				MimeBodyPart mbp2 = new MimeBodyPart();
				mbp2.setDataHandler(new DataHandler(fds));
				mbp2.setFileName(fds.getName());

				mp.addBodyPart(mbp2);

			}

			msg.setContent(mp);
			msg.setSentDate(Calendar.getInstance().getTime());

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return msg;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		super.actionPerformed(e);

		if (e.getSource() == btEnviar) {

			ProcessoSec pSec = new ProcessoSec(500, new Processo() {

				public void run() {

					status.updateUI();
				}
			}, new Processo() {

				public void run() {

					enviar();
				}
			});

			pSec.iniciar();
		}
	}

	@Override
	public void setConexao(DbConnection conn) {

		super.setConexao(conn);

		if (con != null) {

			List<Object> prefere = RPPrefereGeral.getPrefere(con);
			if ("S".equals(prefere.get(RPPrefereGeral.EPrefere.ENVIACOPIA.ordinal()))) {
				cbComCopia.setVlrString("S");
			}
			else {
				cbComCopia.setVlrString("N");
			}
		}
	}

	class SMTPAuthenticator extends Authenticator {

		private final String username;

		private final String password;

		SMTPAuthenticator(String username, String password) {

			this.username = username;
			this.password = password;
		}

		public PasswordAuthentication getPasswordAuthentication() {

			return new PasswordAuthentication(username, password);
		}
	}
}
