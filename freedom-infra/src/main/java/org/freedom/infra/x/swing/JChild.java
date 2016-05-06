package org.freedom.infra.x.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Classe base para telas internas padrão MDI. <BR>
 * Projeto: freedom-infra <BR>
 * Pacote: org.freedom.infra.x.swing <BR>
 * Classe:
 * 
 * (#)JChild.java <BR>
 * <BR>
 * Este programa é licenciado de acordo com a LGPL (Lesser General Public
 * License), <BR>
 * versão 2.1, Fevereiro de 1999 <BR>
 * A LGPL deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste
 * Programa. <BR>
 * Caso uma cópia da LGPL não esteja disponível junto com este Programa, você
 * pode contatar <BR>
 * o LICENCIADOR ou então pegar uma cópia em: <a
 * href=http://creativecommons.org/licenses/LGPL/2.1/legalcode.pt> Creative
 * Commons</a> <BR>
 * Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa é
 * preciso estar de acordo com os termos da LGPL. <BR>
 * <BR>
 * 
 * @author Robson Sanchez/Setpoint Informática Ltda. <BR>
 *         criada: 21/11/2006. <BR>
 * <BR>
 * @see javax.swing.JInternalFrame
 */
public class JChild extends JInternalFrame {

	private static final long serialVersionUID = 1l;

	private JPanel jpnContainer = null;

	private JPanel jpnTop = null;

	private JPanel jpnClient = null;

	private JPanel jpnSouth = null;

	private JScrollPane jspClient = null;

	/**
	 * This method initializes
	 * 
	 */
	public JChild() {

		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {

		this.setSize(new Dimension(470, 280));
		this.setContentPane(getJpnContainer());

	}

	/**
	 * This method initializes jpnContainer
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJpnContainer() {

		if (jpnContainer == null) {
			jpnContainer = new JPanel();
			jpnContainer.setLayout(new BorderLayout());
			jpnContainer.add(getJpnTop(), BorderLayout.NORTH);
			jpnContainer.add(getJpnClient(), BorderLayout.CENTER);
			jpnContainer.add(getJpnSouth(), BorderLayout.SOUTH);
		}
		return jpnContainer;
	}

	/**
	 * This method initializes jpnTop
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJpnTop() {

		if (jpnTop == null) {
			jpnTop = new JPanel();
			jpnTop.setLayout(new GridBagLayout());
		}
		return jpnTop;
	}

	/**
	 * This method initializes jpnClient
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJpnClient() {

		if (jpnClient == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.weightx = 1.0;
			jpnClient = new JPanel();
			jpnClient.setLayout(new GridBagLayout());
			jpnClient.add(getJspClient(), gridBagConstraints);
		}
		return jpnClient;
	}

	/**
	 * This method initializes jpnSouth
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJpnSouth() {

		if (jpnSouth == null) {
			jpnSouth = new JPanel();
			jpnSouth.setLayout(new GridBagLayout());
		}
		return jpnSouth;
	}

	/**
	 * This method initializes jspClient
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJspClient() {

		if (jspClient == null) {
			jspClient = new JScrollPane();
			jspClient.setBorder(null);
		}
		return jspClient;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
