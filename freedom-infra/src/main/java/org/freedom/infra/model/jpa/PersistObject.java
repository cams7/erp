package org.freedom.infra.model.jpa;

public abstract class PersistObject {

	public abstract void setKey(Key key);

	public abstract Key getKey();

	public int hashCode() {

		return getKey().hashCode();
	}

	public boolean equals(Object object) {

		return ( ( object instanceof PersistObject ) && ( this.getKey().equals(( ( PersistObject ) object ).getKey()) ) );
	}
}
