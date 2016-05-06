/**
 * @version 22/03/2005 <BR>
 * @author Setpoint Informática Ltda / Robson Sanchez.
 * 
 * Projeto: Freedom <BR>
 * 
 * Pacote: org.freedom.componentes <BR>
 * Classe:
 * @(#)JTabbedPanePad.java <BR>
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
 * ? <BR>
 * 
 */

package org.freedom.library.swing.component;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTabbedPane;

public class JTabbedPanePad extends JTabbedPane implements FocusListener {

	private static final long serialVersionUID = 1L;
	private boolean initFirstFocus = true;
	private boolean controlFocus = true;

	// Flag que indica para o component procurar o primeiro campo para foco.
	/**
	 * 
	 */
	public JTabbedPanePad() {
		super();
		setChangedListener();
	}

	/**
	 * @param arg0
	 */
	public JTabbedPanePad(int arg0) {
		super(arg0);
		setChangedListener();
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public JTabbedPanePad(int arg0, int arg1) {
		super(arg0, arg1);
		setChangedListener();
	}

	private void setChangedListener() {
		addFocusListener(this);
	}

	public void focusGained(FocusEvent arg0) {
		// System.out.println("Ganhou foco no Tabbed");
		firstFocus(getSelectedComponent());
		controlFocus = false;
	}

	public void focusLost(FocusEvent arg0) {
		// System.out.println("Perdeu foco no Tabbed");
		// initFirstFocus = true;

	}

	public void setInitFirstFocus(boolean initFirstFocus) {
		this.initFirstFocus = initFirstFocus;
	}

	public boolean getInitFirstFocus() {
		return this.initFirstFocus;
	}

	public void firstFocus(Component c) {
		if (( initFirstFocus ) && ( c != null ) && ( controlFocus )) {
			if (c instanceof JPanelPad) {
				// System.out.println("Entrou no first focus do tabbed");
				if (!( ( JPanelPad ) c ).firstFocus())
					transferFocus();
			}
		}
	}

}
