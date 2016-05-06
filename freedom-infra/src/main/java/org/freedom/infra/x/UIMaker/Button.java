package org.freedom.infra.x.UIMaker;

import javax.swing.Action;
import javax.swing.Icon;

import org.freedom.infra.x.swing.FDButton;

public class Button extends FDButton {

	private static final long serialVersionUID = 1l;

	public Button() {
		super();
		init();
	}

	public Button(Icon icon) {
		super(icon);
		init();
	}

	public Button(String text) {
		super(text);
		init();
	}

	public Button(Action a) {
		super(a);
		init();
	}

	public Button(String text, Icon icon) {
		super(text, icon);
		init();
	}

	private void init() {

		setContentAreaFilled(false);
		setFocusPainted(false);
	}

	@Override
	public void setDisabledIcon(Icon disabledIcon) {
		super.setDisabledIcon(disabledIcon);
		if (isBorderPainted()) {
			setBorderPainted(false);
		}
	}

	@Override
	public void setIcon(Icon defaultIcon) {
		super.setIcon(defaultIcon);
		if (isBorderPainted()) {
			setBorderPainted(false);
		}
	}

	@Override
	public void setPressedIcon(Icon pressedIcon) {
		super.setPressedIcon(pressedIcon);
		if (isBorderPainted() && getIcon() != null) {
			setBorderPainted(false);
		}
	}

}
