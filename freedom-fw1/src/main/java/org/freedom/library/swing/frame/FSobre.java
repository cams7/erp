/**
 * @version 14/11/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.telas <BR>
 * Classe:
 * @(#)FLogin.java <BR>
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
 * Comentários para a classe...
 */

package org.freedom.library.swing.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;

import org.freedom.bmps.Icone;
import org.freedom.infra.functions.SystemFunctions;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.util.SwingParams;

public class FSobre extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JButtonPad btReciclarMemoria = new JButtonPad("Reciclar memória", Icone.novo("btAtualiza.png"));

	private JTabbedPanePad tpnSobre = new JTabbedPanePad();

	private JPanelPad pnSobre = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());

	private JPanelPad pnEquipe = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());

	private JPanelPad pnSistema = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());

	private JPanelPad pnNotas = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());

	private ImageIcon img = Icone.novo("freedom_real_500x450.png");

	private long lMemLivre;

	private long lMemTotal;

	private long lMemUtilizada;

	private long lMemMaxima;

	private JLabelPad lbRodapeVersao = new JLabelPad();
	private JPanelPad pnRodapeVersao = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());
	private JScrollPane spRodapeVersao = new JScrollPane(lbRodapeVersao);

	private JLabelPad lbRodapeEquipe = new JLabelPad();
	private JPanelPad pnRodapeEquipe = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());
	private JScrollPane spRodapeEquipe = new JScrollPane(lbRodapeEquipe);

	private JLabelPad lbRodapeSistema = new JLabelPad();
	private JPanelPad pnRodapeSistema = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());
	private JScrollPane spRodapeSistema = new JScrollPane(lbRodapeSistema);

	private JLabelPad lbRodapeNotas = new JLabelPad();
	private JPanelPad pnRodapeNotas = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());
	private JScrollPane spRodapeNotas = new JScrollPane(lbRodapeNotas);

	private Font font = SwingParams.getFontboldmed();

	public FSobre() {

		super(Aplicativo.telaPrincipal);

		setTitulo("Freedom-ERP");
		setAtribos(920, 550);

		setToFrameLayout();
		adicListeners();
		setFonteLabels();
		setColorPanels();

		pnRodape.add(btReciclarMemoria);

		c.add(tpnSobre, BorderLayout.CENTER);

		/******************************************
		 * 
		 * ABA SOBRE
		 * 
		 * *
		 ***************************************/

		tpnSobre.addTab("Sobre", pnSobre);

		pnSobre.add(spRodapeVersao, BorderLayout.CENTER);

		lbRodapeVersao.setText(getHtmlVersao());

		/******************************************
		 * 
		 * ABA EQUIPE
		 * 
		 * *
		 ***************************************/

		tpnSobre.addTab("Equipe", pnEquipe);

		pnEquipe.add(pnRodapeEquipe, BorderLayout.CENTER);

		lbRodapeEquipe.setText(getHtmlEquipe());

		/******************************************
		 * 
		 * ABA SISTEMA
		 * 
		 * *
		 ***************************************/

		tpnSobre.addTab("Sistema", pnSistema);

		pnSistema.add(pnRodapeSistema, BorderLayout.CENTER);

		lbRodapeSistema.setText(getHtmlSistema());

		/******************************************
		 * 
		 * ABA NOTAS DA VERSÃO
		 * 
		 * *
		 ***************************************/

		tpnSobre.addTab("Notas da versão", pnNotas);

		pnNotas.add(pnRodapeNotas, BorderLayout.CENTER);

//		lbRodapeNotas.setText(getHtmlNotasPorVersao());
		lbRodapeNotas.setText(getHtmlNotasGeral());

	}

	private void adicListeners() {

		btReciclarMemoria.addActionListener(this);

	}

	private void setColorPanels() {

		/******************************************
		 * 
		 * ABA SOBRE
		 * 
		 * *
		 ***************************************/

		pnSobre.setBackground(Color.BLACK);
		lbRodapeVersao.setForeground(Color.WHITE);
		lbRodapeVersao.setBackground(Color.BLACK);

		spRodapeVersao.setBorder(null);
		lbRodapeVersao.setOpaque(true);
		lbRodapeVersao.setIcon(img);

		pnRodapeVersao.add(spRodapeVersao, BorderLayout.CENTER);

		/******************************************
		 * 
		 * ABA EQUIPE
		 * 
		 * *
		 ***************************************/

		pnEquipe.setBackground(Color.BLACK);
		lbRodapeEquipe.setForeground(Color.WHITE);
		lbRodapeEquipe.setBackground(Color.BLACK);

		spRodapeEquipe.setBorder(null);
		lbRodapeEquipe.setOpaque(true);
		lbRodapeEquipe.setIcon(img);

		pnRodapeEquipe.add(spRodapeEquipe, BorderLayout.CENTER);

		/******************************************
		 * 
		 * ABA SISTEMA
		 * 
		 * *
		 ***************************************/

		pnSistema.setBackground(Color.BLACK);
		lbRodapeSistema.setForeground(Color.WHITE);
		lbRodapeSistema.setBackground(Color.BLACK);

		spRodapeSistema.setBorder(null);
		lbRodapeSistema.setOpaque(true);
		lbRodapeSistema.setIcon(img);

		pnRodapeSistema.add(spRodapeSistema, BorderLayout.CENTER);

		/******************************************
		 * 
		 * ABA NOTAS DA VERSAO
		 * 
		 * *
		 ***************************************/

		pnNotas.setBackground(Color.LIGHT_GRAY);
		lbRodapeNotas.setForeground(Color.BLACK);
		lbRodapeNotas.setBackground(Color.LIGHT_GRAY);

		spRodapeNotas.setBorder(null);

		pnRodapeNotas.add(spRodapeNotas, BorderLayout.CENTER);
	}

	private void setFonteLabels() {

		lbRodapeVersao.setFont(font);
		lbRodapeSistema.setFont(font);
		lbRodapeEquipe.setFont(font);
		lbRodapeNotas.setFont(new Font(SwingParams.FONT_PAD, SwingParams.FONT_STYLE_PAD, SwingParams.FONT_SIZE_MIN + 1));

	}

	private String getHtmlEquipe() {

		StringBuffer html = new StringBuffer();

		html.append("<HTML>");
		html.append("<table style=\"width: 370;\" border=\"0\">");

		Vector<String> equipesis = Aplicativo.getEquipeSis();

		for (int i = 0; equipesis.size() > i; i++) {

			html.append("<TR><CENTER>");
			html.append(equipesis.elementAt(i).toString());
			html.append("</CENTER></TR>");
		}

		html.append("</TABLE></HTML>");

		return html.toString();

	}

	private String getHtmlNotasGeral() {
		String ret = "";
		String file = "";
		String extensao = ".html";

		try {

			file = "release" + extensao;
			
			InputStream is = this.getClass().getResourceAsStream("/org/freedom/doc/release_notes/" + file);

			byte buf[] = new byte[is.available()];

			StringBuffer notas = new StringBuffer();

			while (is.read(buf) > 0) {
				notas.append(new String(buf, Charset.forName("ISO-8859-1")));
			}

			ret = "<html>" + notas + "</html>";


		}
		catch (Exception e) {
			e.printStackTrace();
			ret = "<html>" + "Não foi possível localizar as notas da versão.\n" + file + "</html>";

		}

		return ret;

	}
	/*
	private String getHtmlNotasPorVersao() {
		String ret = "";
		String file = "";
		String versao = "";
		String extensao = ".html";

		try {

			versao = SystemFunctions.getVersionSis(this.getClass());

			if ("Indefinida".equals(versao)) {
				file = versao + extensao;
			}
			else {
				file = versao.substring(0, 7) + extensao;
			}

			InputStream is = this.getClass().getResourceAsStream("/org/freedom/doc/release_notes/" + file);

			byte buf[] = new byte[is.available()];

			StringBuffer notas = new StringBuffer();

			while (is.read(buf) > 0) {
				notas.append(new String(buf, Charset.forName("ISO-8859-1")));
			}

			ret = "<html>" + notas + "</html>";
			// ret = notas.toString();

		}
		catch (Exception e) {
			e.printStackTrace();
			ret = "<html>" + "Não foi possível localizar as notas da versão.\n" + file + "</html>";

		}

		return ret;

	}
*/
	private String getHtmlVersao() {
		String ret = "";

		try {

			String sdatacompilacao = "";
			StringBuilder html = new StringBuilder();

			Date datacompilacao = SystemFunctions.getClassDateCompilation(this.getClass());

			sdatacompilacao = Funcoes.dateToStrDataHora(datacompilacao);

			html.append("<HTML>");
			html.append("<table style=\"width: 370;\" border=\"0\">");
			html.append("<tr>");
			html.append("<td>");

			html.append("Versão: ");

			html.append("</td>");
			html.append("<td>");

			html.append(SystemFunctions.getVersionSis(this.getClass()));

			html.append("</td>");
			html.append("</tr>");

			html.append("<tr>");
			html.append("<td>");
			html.append("Compilação:");
			html.append("</td>");
			html.append("<td>");
			html.append(sdatacompilacao);
			html.append("</td>");

			html.append("</tr>");

			html.append("</table>");

			html.append("</HTML>");

			ret = html.toString();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

	private void limpaMemoria() {
		try {
			Runtime.getRuntime().gc();
			System.gc();
			lbRodapeSistema.setText(getHtmlSistema());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getHtmlSistema() {
		String ret = "";

		try {

			lMemLivre = ( ( Runtime.getRuntime().freeMemory() / 1024 ) / 1024 );
			lMemTotal = ( ( Runtime.getRuntime().totalMemory() / 1024 ) / 1024 );
			lMemUtilizada = lMemTotal - lMemLivre;
			lMemMaxima = ( ( Runtime.getRuntime().maxMemory() / 1024 ) / 1024 );

			StringBuffer html = new StringBuffer();

			html.append("<HTML>");

			html.append("<table style=\"width: 370;\" border=\"0\">");
			html.append("<TR>");
			html.append("<TD>Memória maxima:</TD>");
			html.append("<TD>" + lMemMaxima + " MB" + "</TD>");
			html.append("</TR>");
			html.append("<TR>");
			html.append("<TD>Memória total:</TD>");
			html.append("<TD>" + lMemTotal + " MB" + "</TD>");
			html.append("</TR>");
			html.append("<TR>");
			html.append("<TD>Memória utilizada:</TD>");
			html.append("<TD>" + lMemUtilizada + " MB" + "</TD>");
			html.append("</TR>");
			html.append("<TR>");
			html.append("<TD>Memória livre:</TD>");
			html.append("<TD>" + lMemLivre + " MB" + "</TD>");
			html.append("</TR>");
			html.append("</CENTER></TABLE>");

			html.append("</HTML>");

			ret = html.toString();

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return ret;

	}

	public void actionPerformed(ActionEvent evt) {

		if (evt.getSource() == btReciclarMemoria) {

			limpaMemoria();
		}

		super.actionPerformed(evt);
	}

}
