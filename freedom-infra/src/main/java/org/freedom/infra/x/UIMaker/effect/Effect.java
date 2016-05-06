package org.freedom.infra.x.UIMaker.effect;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComponent;

public interface Effect extends ActionListener {

	void doStart();

	void doStop();

	void addComponent(JComponent component);

	void removeComponent(int indexComponent);

	void removeComponent(JComponent component);

	List<JComponent> getComponents();
}
