package org.freedom.infra.x.swing;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

public class FDButton extends JButton {

	private static final long serialVersionUID = 1l;

	public FDButton() {
	}

	public FDButton(Icon icon) {
		super(icon);
	}

	public FDButton(String text) {
		super(text);
	}

	public FDButton(Action a) {
		super(a);
	}

	public FDButton(String text, Icon icon) {
		super(text, icon);
	}

}
