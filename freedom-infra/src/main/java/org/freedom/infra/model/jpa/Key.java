package org.freedom.infra.model.jpa;

import java.io.Serializable;

public class Key implements Serializable {

	protected static final long serialVersionUID = 1L;

	public static final int COL_KEY = 0;

	public static final int COL_VALUE = 1;

	private Object[] keys = null;

	private String internalKey = null;

	public Key(Object... keys) {

		super();
		setKeys(keys);
	}

	public Object[] getKeys() {

		return keys;
	}

	public void setKeys(Object[] keys) {

		this.keys = keys;
		setInternalKey(encodeKey(keys));
	}

	public String encodeKey(Object[] values) {

		String encode = null;
		if (values != null) {
			StringBuilder buffer = new StringBuilder();
			for (Object value : values) {
				buffer.append(value.toString());
				buffer.append(" ");
			}
			encode = buffer.toString();
		}
		return encode;
	}

	public int hashCode() {

		int hashCode = 0;
		if (( keys != null ) && ( internalKey != null )) {
			hashCode = internalKey.hashCode();
		}
		return hashCode;
	}

	public String getInternalKey() {
		return internalKey;
	}

	public void setInternalKey(String internalKey) {
		this.internalKey = internalKey;
	}

	public boolean equals(Object o) {
		return ( ( o instanceof Key ) && ( internalKey.equals(( ( Key ) o ).getInternalKey()) ) );
	}
}
