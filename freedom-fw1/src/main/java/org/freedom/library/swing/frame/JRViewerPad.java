package org.freedom.library.swing.frame;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;

import org.freedom.bmps.Icone;
import org.freedom.library.business.object.EmailBean;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.dialog.DLEnviarEmail;

public class JRViewerPad extends JRViewer {

	private static final long serialVersionUID = 1L;

	private final JPanelPad panelButtonsStp = new JPanelPad(JPanelPad.TP_JPANEL, new FlowLayout(FlowLayout.RIGHT, 3, 3));

	private JButtonPad btnEmail;

	private JasperPrint report = null;

	private EmailBean mail = Aplicativo.getEmailBean();
	
	private boolean imprimir = true;
	
	private boolean exportar = true;
	

	public JRViewerPad(JasperPrint arg0, EmailBean mail) {
		this(arg0, mail, true, true);
	}
	
	public JRViewerPad(JasperPrint arg0, EmailBean mail, boolean imprimir, boolean exportar) {
		super(arg0);
		report = arg0;
		this.imprimir = imprimir;
		this.exportar = exportar;
		
		init(mail, this.imprimir, this.exportar);
	}

	public JRViewerPad(JasperPrint arg0, Locale arg1, EmailBean mail) {

		super(arg0, arg1);
		init(mail, imprimir, exportar);
		report = arg0;
	}
	
	public JRViewerPad(JasperPrint arg0, Locale arg1, ResourceBundle arg2, EmailBean mail) {

		super(arg0, arg1, arg2);
		init(mail, imprimir, exportar);
		report = arg0;
	}

	private void init(EmailBean mail, boolean imprimir, boolean exportar) {

		tlbToolBar.add(panelButtonsStp);
		panelButtonsStp.setPreferredSize(new Dimension(115, 30));

		createButtonEmail(imprimir, exportar);

		if (mail != null) {
			setMail(mail);
		}

	}

	private void createButtonEmail(boolean imprimir, boolean exportar) {

		btnEmail = new JButtonPad("e-mail", Icone.novo("mail.png"));
		btnEmail.setToolTipText("Enviar arquivo por e-mail");
		btnEmail.setPreferredSize(new Dimension(100, 23));
		
		btnPrint.setEnabled(imprimir);
		btnSave.setEnabled(exportar);
		

		btnEmail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showDLEnviarEmail();
			}
		});

		panelButtonsStp.add(btnEmail);
	}

	private void showDLEnviarEmail() {

		DLEnviarEmail enviaemail = new DLEnviarEmail(this, getMail(), null);
		enviaemail.setReport(report);
		enviaemail.preparar();
		if (enviaemail.preparado()) {
			enviaemail.setVisible(true);
		}
		else {
			enviaemail.dispose();
		}
	}

	public EmailBean getMail() {
		return mail;
	}

	public void setMail(EmailBean mail) {
		this.mail = mail;
	}
	
	@Override
	protected void refreshPage() {
		super.refreshPage();
		btnPrint.setEnabled(imprimir);
		btnSave.setEnabled(exportar);
	}


}
