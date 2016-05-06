package org.freedom.infra.x.UIMaker.effect;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

public abstract class EffectStandard implements Effect {

	protected final List<JComponent> components = new ArrayList<JComponent>();

	public void addComponent(JComponent component) {
		if (component != null) {
			components.add(component);
		}
	}

	public List<JComponent> getComponents() {
		return components;
	}

	public void removeComponent(int indexComponent) {
		components.remove(indexComponent);
	}

	public void removeComponent(JComponent component) {
		components.remove(component);
	}

	public void clear() {
		components.clear();
	}

	public void actionPerformed(ActionEvent e) {
	}

	@Override
	protected void finalize() throws Throwable {

		doStop();
		super.finalize();
	}

}
