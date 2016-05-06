package org.freedom.library.swing.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Rectangle;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.freedom.library.swing.component.JTextFieldPad;

public class FChild extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel jPanel = null;
	private JScrollPane jScrollPane = null;
	private JPanel jPanel1 = null;
	private JTextFieldPad jTextField01 = null;

	/**
	 * This method initializes
	 * 
	 */
	public FChild() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setSize(new Dimension(491, 195));
		this.setContentPane(getJContentPane());
		this.setTitle("FChild");

	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJScrollPane(), BorderLayout.CENTER);
			jContentPane.add(getJPanel(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.setPreferredSize(new Dimension(30, 30));
		}
		return jPanel;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJPanel1());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.setLayout(null);
			jPanel1.add(getJTextField01(), null);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jTextField01
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextFieldPad getJTextField01() {
		if (jTextField01 == null) {
			jTextField01 = new JTextFieldPad(JTextFieldPad.TP_STRING, 10, 0);
			jTextField01.setBounds(new Rectangle(53, 29, 158, 25));
		}
		return jTextField01;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
