package org.freedom.infra.x.swing;

import javax.swing.Icon;
import javax.swing.JLabel;

import org.freedom.infra.beans.Component;
import org.freedom.infra.beans.Field;

public class FDLabel extends JLabel implements Component {

	private static final long serialVersionUID = 1l;

	private Field field;

	public FDLabel() {
		this("", null, LEADING);
	}

	public FDLabel(String text) {
		this(text, null, LEADING);
	}

	public FDLabel(Icon image) {
		this("", image, LEADING);
	}

	public FDLabel(String text, int horizontalAlignment) {
		this(text, null, horizontalAlignment);
	}

	public FDLabel(Icon image, int horizontalAlignment) {
		this("", image, horizontalAlignment);
	}

	public FDLabel(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
	}

	public Field getField() {
		if (this.field == null) {
			this.field = new Field();
		}
		return this.field;
	}

	public Object getValue() {
		return getField().getValue();
	}

	public void setValue(Object value) {

		getField().setValue(value);

		super.setText(value != null ? value.toString() : null);
	}

	@Override
	public void setText(String text) {

		setValue(text);
	}
}
