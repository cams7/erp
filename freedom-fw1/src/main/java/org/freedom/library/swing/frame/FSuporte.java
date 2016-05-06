/**
 * @version 25/09/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 *
 * Projeto: Freedom <BR>
 *  
 * Pacote: org.freedom.modulos.tmk <BR>
 * Classe: @(#)FSuporte.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA <BR> <BR>
 *
 * Comentários sobre a classe...
 * 
 */

package org.freedom.library.swing.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

import org.freedom.acao.Processo;
import org.freedom.bmps.Icone;
import org.freedom.infra.functions.SystemFunctions;
import org.freedom.library.business.component.ProcessoSec;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;

/**
 * @author robson
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class FSuporte extends FFDialogo implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtArqMen = new JTextFieldPad(JTextFieldPad.TP_STRING, 30, 0);
	private JTextFieldPad txtAssunto = new JTextFieldPad(JTextFieldPad.TP_STRING, 40, 0);
	// private JTextFieldPad txtEmail = new
	// JTextFieldPad(JTextFieldPad.TP_STRING,60,0);
	private JTextFieldPad txtDe = new JTextFieldPad(JTextFieldPad.TP_STRING, 40, 0);
	private JButtonPad btBuscaArq = new JButtonPad(Icone.novo("btAbrirPeq.png"));
	private JButtonPad btEnviar = new JButtonPad(Icone.novo("btEnviarMail.png"));
	private JTextAreaPad txaMen = new JTextAreaPad();
	private JScrollPane spnMen = new JScrollPane(txaMen);
	// private JPanelPad pinGeral = new JPanelPad(0, 135);
	private JPanelPad pinArq = new JPanelPad(0, 50);
	private JPanelPad pinRod = new JPanelPad(0, 0);
	private JPanelPad pnCenter = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());
	private JProgressBar pbAnd = new JProgressBar();
	private JLabelPad lbStatus = new JLabelPad("Pronto.");
	String sSMTP = null;
	String sUser = null;
	String sPass = null;
	File fArq = null;
	boolean bEnvia = false;

	public FSuporte() {
		setTitulo("Envio de pedido de suporte");
		setAtribos(100, 100, 375, 430);
		pnBordRodape.setVisible(false);
		lbStatus.setPreferredSize(new Dimension(0, 20));
		lbStatus.setForeground(Color.BLUE);
		pinRod.tiraBorda();

		txaMen.setVlrString("Descreva aqui seu problema, dúvida ou sugestão...");
		Vector<String> vVals = new Vector<String>();
		vVals.add("A");
		vVals.add("C");
		Vector<String> vLabs = new Vector<String>();
		vLabs.add("Arquivo no anexo.");
		vLabs.add("Arquivo no corpo.");

		lbStatus.setBorder(BorderFactory.createEtchedBorder());

		c.setLayout(new BorderLayout());
		// c.add(pinGeral, BorderLayout.NORTH);
		c.add(pnCenter, BorderLayout.CENTER);
		c.add(pinRod, BorderLayout.SOUTH);

		adicBotaoSair().add(pinRod, BorderLayout.CENTER);

		pnCenter.add(pinArq, BorderLayout.NORTH);
		pnCenter.add(spnMen, BorderLayout.CENTER);
		pnCenter.add(lbStatus, BorderLayout.SOUTH);

		txtAssunto.setVlrString("Pedido de suporte - " + Aplicativo.sNomeFilial.trim() + " - " + Aplicativo.getUsuario().getIdusu());
		pinArq.adic(new JLabelPad("Anexe um arquivo, caso necessário."), 7, 0, 250, 20);
		pinArq.adic(txtArqMen, 7, 20, 313, 20);
		pinArq.adic(btBuscaArq, 320, 20, 20, 20);

		pinRod.adic(btEnviar, 7, 0, 30, 30);
		pinRod.adic(pbAnd, 45, 5, 205, 20);

		pbAnd.setStringPainted(true);
		pbAnd.setMinimum(0);

		btBuscaArq.addActionListener(this);
		btEnviar.addActionListener(this);
	}

	public void setMensagem(String sMensagem) {
		txaMen.setVlrString(sMensagem);
	}

	private String buscaEmailFilial() {
		String sRet = "";
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sSQL = "SELECT EMAILFILIAL FROM SGFILIAL FL WHERE CODEMP=? AND CODFILIAL=?";
		try {
			ps = con.prepareStatement(sSQL);
			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, Aplicativo.iCodFilial);
			rs = ps.executeQuery();
			while (rs.next()) {
				sRet = rs.getString(1);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		return sRet;
	}

	private String buscaArq() {
		String sRetorno = "";
		JFileChooser fcImagem = new JFileChooser();
		if (fcImagem.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			sRetorno = fcImagem.getSelectedFile().getPath();
		}
		return sRetorno;
	}

	private boolean verifSmtp() {
		boolean bRet = false;
		String sSQL = "SELECT SMTPMAIL,USERMAIL,PASSMAIL FROM SGPREFERE3 WHERE CODEMP=? AND CODFILIAL=?";
		try {
			PreparedStatement ps = con.prepareStatement(sSQL);
			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, Aplicativo.iCodFilial);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				sSMTP = rs.getString("SMTPMail") != null ? rs.getString("SMTPMail").trim() : null;
				sUser = rs.getString("UserMail") != null ? rs.getString("UserMail").trim() : null;
				sPass = rs.getString("PassMail") != null ? rs.getString("PassMail").trim() : null;
			}
			if (sSMTP == null) {
				Funcoes.mensagemInforma(null, "Não foi cadastrado o servidor de SMTP!\nUtilize a tela de preferências gerais para configurar.");
			}
			else
				bRet = true;
		}
		catch (SQLException err) {
			Funcoes.mensagemErro(this, "Erro ao buscar o as informações de SMTP!\n" + err.getMessage());
			err.printStackTrace();
		}
		return bRet;
	}

	private boolean verifCab() {
		boolean bRet = false;
		/*
		 * if (txtEmail == null) { Funcoes.mensagemInforma(this,
		 * "Não foi preenchido o campo 'E-Mail:'!"); }
		 */
		if (txtDe == null) {
			Funcoes.mensagemInforma(this, "Não foi preenchido o campo 'De:'!");
		}
		else if (txtAssunto == null) {
			Funcoes.mensagemInforma(this, "Não foi preenchido o campo 'Assunto:'!");
		}
		else
			bRet = true;
		return bRet;
	}

	private boolean verifCorpo() {
		boolean bRet = false;
		if (txaMen.getVlrString().equals("") && txtArqMen.getVlrString().equals(""))
			Funcoes.mensagemInforma(this, "Nada a ser enviado!");
		else
			bRet = true;
		return bRet;
	}

	private void enviar() {
		int iConta = 0;
		if (!verifSmtp())
			return;
		else if (!verifCab())
			return;
		else if (!verifCorpo())
			return;
		Properties pp = new Properties();
		pp.put("mail.smtp.host", sSMTP);
		Session se = Session.getDefaultInstance(pp);

		pbAnd.setMaximum(1);
		pbAnd.setValue(iConta = 0);

		fArq = null;
		if (!txtArqMen.getVlrString().equals("")) {
			fArq = new File(txtArqMen.getVlrString());
			if (!fArq.exists()) {
				Funcoes.mensagemErro(this, "Arquivo não foi encotrado!");
				return;
			}
		}

		bEnvia = true;
		String sEmail = Aplicativo.getEmailSuporte();
		if (!bEnvia)
			return;
		if (sEmail != null) {
			sEmail = sEmail.trim();
		}
		mandaMail(sEmail, se);
		pbAnd.setValue(++iConta);
	}

	private void mandaMail(String sTo, Session se) {
		state("Preparando envio para: '" + sTo + "'");
		try {

			txtDe.setVlrString(buscaEmailFilial());

			MimeMessage mes = new MimeMessage(se);
			mes.setFrom(new InternetAddress(txtDe.getVlrString()));
			mes.setSubject(txtAssunto.getVlrString());
			mes.setSentDate(new Date());
			mes.addRecipient(RecipientType.TO, new InternetAddress(sTo));

			BodyPart parte = new MimeBodyPart();

			parte.setText(txaMen.getVlrString());

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(parte);

			String sTextoAdic = "";

			try {

				Date datacompilacao = SystemFunctions.getClassDateCompilation(this.getClass());
				sTextoAdic += "Versão:" + Funcoes.dateToStrDataHora(datacompilacao) + "\n";

			}
			catch (Exception e) {
				e.printStackTrace();
			}
			sTextoAdic += "\nMensagem:\n" + txaMen.getVlrString();
			parte.setText(sTextoAdic);

			if (fArq != null) {
				parte = new MimeBodyPart();
				FileDataSource orig = new FileDataSource(fArq);
				parte.setDataHandler(new DataHandler(orig));
				parte.setFileName(fArq.getName());
				// parte.setDisposition(rgTipo.getVlrString().equals("A") ?
				// Part.ATTACHMENT : Part.INLINE);

				parte.setDisposition(Part.ATTACHMENT);

				multipart.addBodyPart(parte);
			}
			mes.setContent(multipart);
			state("Enviando dados...");
			Transport.send(mes);
			state("Envio OK...");
		}
		catch (MessagingException err) {
			Funcoes.mensagemErro(this, "Erro ao enviar mensagem para: " + sTo + "\n" + err.getMessage());
			err.getStackTrace();
			state("Aguardando reenvio.");
		}
	}

	/*
	 * private void mandaMail(String sTo, Session se) {
	 * state("Preparando envio para: '" + sTo + "'"); try { MimeMessage mes =
	 * new MimeMessage(se); txtDe.setVlrString(buscaEmailFilial());
	 * if(txtDe.getVlrString()==null) { newException(
	 * "Não foi possível enviar mensagem!\nCadestre um email válido para a filial."
	 * ); } mes.setFrom(new InternetAddress(txtDe.getVlrString()));
	 * mes.setSubject(txtAssunto.getVlrString()); mes.setSentDate(new Date());
	 * try { mes.addRecipient(RecipientType.TO, new
	 * InternetAddress(sTo,"suporte")); } catch(UnsupportedEncodingException e){
	 * e.printStackTrace(); } BodyPart parte = new MimeBodyPart();
	 * 
	 * String sTextoAdic =
	 * "Novo pedido de suporte da empresa:"+Aplicativo.sNomeFilial+"\n";
	 * sTextoAdic += "Usuário:"+Aplicativo.getUsuario().getIdusu() + "\n"; sTextoAdic +=
	 * "Sistema:"+Aplicativo.getNomeSis() + "\n"; sTextoAdic +=
	 * "Módulo:"+Aplicativo.nomemodulo + "\n";
	 * 
	 * try { URL uPath = getClass().getResource("FSuporte.class");
	 * JarURLConnection juc = (JarURLConnection)uPath.openConnection();
	 * sTextoAdic += "Versão:"+Funcoes.dateToStrDataHora(new
	 * Date(juc.getJarEntry().getTime()))+"\n"; } catch(Exception e){
	 * e.printStackTrace(); } sTextoAdic +=
	 * "\nMensagem:\n"+txaMen.getVlrString(); parte.setText(sTextoAdic);
	 * 
	 * Multipart multipart = new MimeMultipart(); multipart.addBodyPart(parte);
	 * 
	 * if (fArq != null) { parte = new MimeBodyPart(); FileDataSource orig = new
	 * FileDataSource(fArq); parte.setDataHandler(new DataHandler(orig));
	 * parte.setFileName(fArq.getName()); parte.setDisposition(Part.ATTACHMENT);
	 * multipart.addBodyPart(parte); } mes.setContent(multipart);
	 * state("Enviando dados..."); Transport.send(mes); state("Envio OK..."); }
	 * catch (MessagingException err) { Funcoes.mensagemErro(this,
	 * "Erro ao enviar mensagem para: " + sTo + "\n" + err.getMessage());
	 * err.getStackTrace(); state("Aguardando reenvio."); } }
	 */
	public void actionPerformed(ActionEvent evt) {
		String sArq = "";
		if (evt.getSource() == btBuscaArq) {
			sArq = buscaArq();
			if (!sArq.equals("")) {
				txtArqMen.setText(sArq);
			}
		}
		else if (evt.getSource() == btEnviar) {
			ProcessoSec pSec = new ProcessoSec(500, new Processo() {
				public void run() {
					lbStatus.updateUI();
					pbAnd.updateUI();
				}
			}, new Processo() {
				public void run() {
					enviar();
				}
			});
			pSec.iniciar();
		}
	}

	public void state(String sStatus) {
		lbStatus.setText(sStatus);
	}
}