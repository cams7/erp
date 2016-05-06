/**
 * @version 05/03/2007 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * @author Alex Rodrigues
 * 
 * Projeto: Freedom <BR>
 * 
 * Pacote: org.freedom.modulos.rep <BR>
 * Classe:
 * @(#)DLLoading.java <BR>
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
 * Display de espera.
 */

package org.freedom.library.swing.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import org.freedom.bmps.Icone;

public class DLLoading extends JWindow implements ActionListener {
//public class DLLoading extends FFDialogo implements ActionListener {	

	private static final long serialVersionUID = 1;

	private static final int NUM_TRANSICOES = 29;

	private final Color azul_claro = new Color(0.9f, 0.93f, 1f);

	private final Color azul = new Color(0.5f, 0.62f, 0.95f);

	private final JLabel aguarde = new JLabel("Aguarde ...", SwingConstants.CENTER);

	private final JLabel loading = new JLabel("", SwingConstants.CENTER);

	private final JPanel mensagens = new JPanel();

	private final JPanel panel = new JPanel();

	private ImageIcon[] img = new ImageIcon[NUM_TRANSICOES];

	private Timer timer = new Timer(50, this);

	private int index = 0;
	
	public DLLoading() {

		setSize(200, 100);
		setLocationRelativeTo(null);
		setBackground(azul_claro); 

		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.setBackground(azul_claro);

		mensagens.setLayout(new BorderLayout());
		mensagens.setBackground(azul_claro);

		loadImages();

		panel.setLayout(new BorderLayout());
		panel.setBackground(azul_claro);
		panel.setBorder(BorderFactory.createEtchedBorder(2, azul, azul));
		c.add(panel, BorderLayout.CENTER);

		loading.setIcon(img[index]);
		panel.add(loading, BorderLayout.CENTER);

		aguarde.setFont(new Font("Heveltica", Font.BOLD, 18));
		aguarde.setForeground(azul);
		aguarde.setPreferredSize(new Dimension(200, 30));

		mensagens.add(aguarde, BorderLayout.CENTER);
		panel.add(mensagens, BorderLayout.SOUTH);
		
	 
		
		
	}

	private void loadImages() {

		for (int i = 0; i < img.length; i++) {
			img[i] = Icone.novo("loading_" + i + ".png");
		}
	}

	public void start() {

		if (!timer.isRunning()) {
			timer.start();
			setVisible(true);
		}
	}

	public void stop() {

		if (timer.isRunning()) {
			timer.stop();
		}
		dispose(); 
	}

	public void actionPerformed(ActionEvent e) {

		if (index == NUM_TRANSICOES) {
			index = 0;
		}

		loading.setIcon(img[index++]);
		loading.updateUI();
		panel.updateUI();
	}

	public static void main(String[] args) {

		DLLoading load = new DLLoading();
		load.start();
	}
}
