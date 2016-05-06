package org.freedom.infra.pojos;

public class Constant implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Object value = null;

	private String name = null;

	public Constant(String name, Object value) {
		setName(name);
		setValue(value);
	}

	public Object getValue() {
		return value;
	}

	private void setValue(Object value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return getName();
	}

}
