package org.freedom.infra.x.swing;

import javax.swing.JPasswordField;

import org.freedom.infra.beans.Component;
import org.freedom.infra.beans.Field;

public class FDPassaword extends JPasswordField implements Component {

	private static final long serialVersionUID = 1l;

	private final Field field;

	public FDPassaword() {

		this(null);
	}

	public FDPassaword(String text) {

		super();

		field = new Field();

		setText(text);
	}

	public Field getField() {
		return field;
	}

	public Object getValue() {
		return field.getValue();
	}

	public void setValue(Object obj) {
		field.setValue(obj);
		super.setText(obj == null ? "" : field.toString());
	}

	@Override
	public void setText(String t) {
		setValue(t);
	}
}
