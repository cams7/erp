package org.freedom.plugin;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractControle {

	private Map<String, Object> objects;

	public void addAttribute(String name, Object obj) {

		if (objects == null) {
			objects = new HashMap<String, Object>();
		}

		objects.put(name, obj);
	}

	public Object getAttribute(String name) {

		Object ret = null;

		if (objects != null) {
			ret = objects.get(name);
		}

		return ret;
	}

	public abstract void inicializa();

}
