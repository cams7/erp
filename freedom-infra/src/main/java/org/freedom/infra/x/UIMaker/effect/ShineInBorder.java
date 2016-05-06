package org.freedom.infra.x.UIMaker.effect;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.Timer;
import javax.swing.border.Border;

public class ShineInBorder extends EffectStandard {

	public static final int SPEED_FAST = 50;

	public static final int SPEED_NORMAL = 100;

	public static final int SPEED_HARD = 300;

	private static final int SIZE = 4;

	private final Timer timer;

	private final Map<JComponent, Border> oldBorders;

	private Color[] colors = new Color[SIZE];

	private int countColor = 0;

	private int countEffect = SIZE;

	public ShineInBorder() {

		this(null);
	}

	public ShineInBorder(final JComponent component) {

		this(component, SPEED_FAST, null, null);
	}

	public ShineInBorder(final JComponent component, final int speed) {

		this(component, speed, null, null);
	}

	public ShineInBorder(final int speed, final Color colorBegin, final Color colorEnd) {

		oldBorders = new HashMap<JComponent, Border>();
		timer = new Timer(speed, this);

		setColors(colorBegin, colorEnd);
	}

	public ShineInBorder(final JComponent component, final int speed, final Color colorBegin, final Color colorEnd) {

		this(speed, colorBegin, colorEnd);

		addComponent(component);
	}

	public ShineInBorder(final List<JComponent> components, final int speed, final Color colorBegin, final Color colorEnd) {

		this(speed, colorBegin, colorEnd);

		if (components != null && components.size() > 0) {
			for (JComponent c : components) {
				addComponent(c);
			}
		}
	}

	public void setColors(final Color colorBegin, final Color colorEnd) {

		if (colorBegin != null && colorEnd != null) {
			int re = colorEnd.getRed();
			int ge = colorEnd.getGreen();
			int be = colorEnd.getBlue();
			int r = colorBegin.getRed() - re;
			int g = colorBegin.getGreen() - ge;
			int b = colorBegin.getBlue() - be;
			colors[0] = colorBegin;
			colors[1] = new Color(re + ( r / 3 * 2 ), ge + ( g / 3 * 2 ), be + ( b / 3 * 2 ));
			colors[2] = new Color(re + ( r / 3 ), ge + ( g / 3 ), be + ( b / 3 ));
			colors[3] = colorEnd;
		}
		else {
			for (int i = 0; i < SIZE; i++) {
				colors[i] = Color.WHITE;
			}
		}
	}

	private void effect() {
		if (getComponents().size() > 0) {
			if (countEffect > 0) {
				Color color = colors[countColor++];
				int ce = countEffect--;
				for (JComponent c : getComponents()) {
					c.setBorder(BorderFactory.createLineBorder(color, ce));
				}
			}
			else {
				countEffect = SIZE;
				countColor = 0;
				for (JComponent c : getComponents()) {
					c.setBorder(oldBorders.get(c));
				}
				doStop();
				System.gc();
			}
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
		}
	}

	public boolean isRunning() {
		return timer.isRunning();
	}

	@Override
	public void addComponent(JComponent component) {
		if (component != null) {
			components.add(component);
			oldBorders.put(component, component.getBorder());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == timer) {
			effect();
		}
	}

}
