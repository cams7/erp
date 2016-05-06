package org.freedom.infra.x.swing;

import javax.swing.JTextField;
import javax.swing.text.Document;

import org.freedom.infra.beans.Component;
import org.freedom.infra.beans.Field;

public class FDTextField extends JTextField implements Component {

	private static final long serialVersionUID = 1l;

	private final Field field;

	public FDTextField() {

		this(null, null, 0);
	}

	public FDTextField(Document doc) {

		this(doc, null, 0);
	}

	public FDTextField(String text) {

		this(null, text, 0);
	}

	public FDTextField(int columns) {

		this(null, null, columns);
	}

	public FDTextField(String text, int columns) {

		this(null, text, columns);
	}

	public FDTextField(Document doc, String text, int columns) {

		super(doc, text, columns);

		field = new Field();

		if (doc != null && doc instanceof org.freedom.infra.util.text.Document) {
			field.setMask(( ( org.freedom.infra.util.text.Document ) doc ).getMask());
		}
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
