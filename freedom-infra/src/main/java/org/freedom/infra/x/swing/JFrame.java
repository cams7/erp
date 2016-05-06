package org.freedom.infra.x.swing;

import java.awt.Dimension;

/**
 * Classe de derivada de JFrame. <BR>
 * Projeto: freedom-infra <BR>
 * Pacote: org.freedom.infra.x.swing <BR>
 * Classe:
 * 
 * (#)JFrame.java <BR>
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
 * @see javax.swing.JFrame
 * 
 */
public class JFrame extends javax.swing.JFrame {

	private static final long serialVersionUID = 1l;

	/**
	 * This method initializes
	 * 
	 */
	public JFrame() {

		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {

		this.setSize(new Dimension(400, 200));

	}

} // @jve:decl-index=0:visual-constraint="10,10"
