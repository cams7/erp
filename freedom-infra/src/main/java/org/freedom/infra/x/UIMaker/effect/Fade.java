package org.freedom.infra.x.UIMaker.effect;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.Timer;

import org.freedom.infra.x.UIMaker.Panel;

public class Fade extends EffectStandard {

	public static final String COMMAND_IN = "fadeIN";

	public static final String COMMAND_OUT = "fadeOUT";

	public static final int ACTION_IN = 1000;

	public static final int ACTION_OUT = 2000;

	public static final int SPEED_DEFAULT = 40;

	public static final float ALFA_DEFAULT = 1.0f;

	public static final float ALFA_INCREMENT_DEFAULT = 0.1f;

	private final Timer timer;

	private final List<ActionListener> actionListeners;

	private Color color;

	private float alfa;

	private float maxAlfa;

	private float alfaIncrement;

	private boolean IN, OUT;

	public Fade() {

		this(( JComponent ) null, SPEED_DEFAULT, null, ALFA_DEFAULT, ALFA_INCREMENT_DEFAULT);
	}

	public Fade(final int speed) {

		this(( JComponent ) null, speed, null, ALFA_DEFAULT, ALFA_INCREMENT_DEFAULT);
	}

	public Fade(final int speed, float maxAlfa, float alfaIncrement) {

		this(( JComponent ) null, speed, null, maxAlfa, alfaIncrement);
	}

	public Fade(final int speed, final Color color, float maxAlfa, float alfaIncrement) {

		this(( JComponent ) null, speed, color, maxAlfa, alfaIncrement);
	}

	public Fade(final JComponent component) {

		this(component, SPEED_DEFAULT, null, ALFA_DEFAULT, ALFA_INCREMENT_DEFAULT);
	}

	public Fade(final JComponent component, final Color color) {

		this(component, SPEED_DEFAULT, color, ALFA_DEFAULT, ALFA_INCREMENT_DEFAULT);
	}

	public Fade(final JComponent component, final int speed) {

		this(component, speed, null, ALFA_DEFAULT, ALFA_INCREMENT_DEFAULT);
	}

	public Fade(final JComponent component, final int speed, float maxAlfa, float alfaIncrement) {

		this(component, speed, null, maxAlfa, alfaIncrement);
	}

	public Fade(final JComponent component, final int speed, final Color color, float maxAlfa, float alfaIncrement) {

		actionListeners = new ArrayList<ActionListener>();
		timer = new Timer(speed, this);

		this.color = color;
		this.maxAlfa = maxAlfa;
		this.alfaIncrement = alfaIncrement;

		addComponent(component);
	}

	public Fade(final List<JComponent> components, final int speed, final Color color, float maxAlfa, float alfaIncrement) {

		this(( JComponent ) null, speed, color, maxAlfa, alfaIncrement);

		if (components != null && components.size() > 0) {
			for (JComponent c : components) {
				addComponent(c);
			}
		}
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public float getMaxAlfa() {
		return maxAlfa;
	}

	public void setMaxAlfa(float maxAlfa) {
		this.maxAlfa = maxAlfa;
	}

	public float getAlfaIncrement() {
		return alfaIncrement;
	}

	public void setAlfaIncrement(float alfaIncrement) {
		this.alfaIncrement = alfaIncrement;
	}

	public void setAction(final int action) {

		if (action == ACTION_IN) {
			IN = true;
			OUT = false;
			alfa = 0.0f;
		}
		else if (action == ACTION_OUT) {
			IN = false;
			OUT = true;
			alfa = maxAlfa;
		}
	}

	private void effect() {

		if (IN) {
			alfa += alfaIncrement;
			if (alfa <= maxAlfa) {
				repaintPanels();
			}
			else {
				alfa = maxAlfa;
				repaintPanels();
				doStop();
				fireActionListener(COMMAND_IN);
			}
		}
		else if (OUT) {
			alfa -= alfaIncrement;
			if (alfa >= 0.0f) {
				repaintPanels();
			}
			else {
				alfa = 0.0f;
				repaintPanels();
				doStop();
				fireActionListener(COMMAND_OUT);
			}
		}
	}

	private void repaintPanels() {
		Panel p;
		for (JComponent c : components) {
			p = ( Panel ) c;
			p.setAlfa(alfa);

			Color oldColor = c.getBackground();
			if (oldColor != null ? !oldColor.equals(color) : color != null && !color.equals(oldColor)) {
				c.setBackground(color);
			}
			else {
				p.repaint();
			}
		}
	}

	public void addActionListener(ActionListener actionListener) {
		if (actionListener != null) {
			actionListeners.add(actionListener);
		}
	}

	public void removeActionListener(ActionListener actionListener) {
		if (actionListener != null) {
			actionListeners.remove(actionListener);
		}
	}

	private void fireActionListener(String command) {
		for (ActionListener a : actionListeners) {
			a.actionPerformed(new ActionEvent(this, 0, command));
		}
	}

	public void doStart() {
		if (timer != null && !timer.isRunning()) {
			timer.start();
		}
	}

	public void doStop() {
		if (timer != null && timer.isRunning()) {
			timer.stop();
			IN = false;
			OUT = false;
		}
	}

	@Override
	public void addComponent(JComponent component) {

		if (component instanceof Panel) {
			if (component != null) {
				components.add(component);
			}
		}
		else if (component != null) {
			throw new IllegalArgumentException("Efeito valido somente para org.freedom.infra.x.UIMaker.Panel !");
			// Implementar interface para alteração do alfa.
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == timer) {
			effect();
		}
	}

}
