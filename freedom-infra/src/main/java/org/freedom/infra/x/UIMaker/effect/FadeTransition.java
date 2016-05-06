package org.freedom.infra.x.UIMaker.effect;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.Timer;

import org.freedom.infra.x.UIMaker.Panel;

public class FadeTransition extends EffectStandard {

	private final Timer timer;

	private final Timer timerSleep;

	private List<Image> images;

	private float alfa1;

	private float alfa2;

	private float alfaIncrement = 0.1f;

	private int index1 = 0;

	private int index2 = -1;

	private boolean firstSleep = true;

	private FadePanel fadePanel = null;

	public FadeTransition(final List<Image> images, final int speed, final int speedSleep, final float alfaIncrement) {

		this.images = images;
		this.timer = new Timer(speed, this);
		this.timerSleep = new Timer(speedSleep, this);
		this.alfaIncrement = alfaIncrement;
	}

	public FadeTransition(final JComponent component, final List<Image> images, final int speed, final int speedSleep, final float alfaIncrement) {

		this(images, speed, speedSleep, alfaIncrement);
		addComponent(component);
	}

	public FadeTransition(final boolean createFadePanel, final List<Image> images, final int speed, final int speedSleep, final float alfaIncrement) {

		this(images, speed, speedSleep, alfaIncrement);

		if (createFadePanel) {
			fadePanel = new FadePanel();
		}
	}

	public Panel getFadePanel() {
		return fadePanel;
	}

	private void effect() {

		if (images != null && images.size() > 0) {

			alfa1 += alfaIncrement;
			alfa2 -= alfaIncrement;

			if (alfa1 <= 1.0f) {

				if (fadePanel != null) {
					fadePanel.repaint();
				}

				if (getComponents().size() > 0) {
					for (JComponent component : getComponents()) {
						fade(component);
					}
				}
				else if (fadePanel == null) {
					doStop();
				}
			}
			else {

				timer.stop();
				timerSleep.start();

				alfa1 = 0.0f;
				alfa2 = 1.0f - alfaIncrement;

				index1++;
				if (index1 == images.size()) {
					index1 = 0;
				}
				index2++;
				if (index2 == images.size()) {
					index2 = 0;
				}
			}
		}
	}

	public void fade(final JComponent component) {

		if (component != null) {

			int w = component.getWidth();
			int h = component.getHeight();

			Graphics2D g2d = ( Graphics2D ) component.getGraphics();

			AlphaComposite a1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alfa1);
			g2d.setComposite(a1);
			g2d.drawImage(images.get(index1), 0, 0, w, h, component);

			if (index2 >= 0) {
				AlphaComposite a2 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alfa2 > 0 ? alfa2 : 0);
				g2d.setComposite(a2);
				g2d.drawImage(images.get(index2), 0, 0, w, h, component);
			}
		}
	}

	public void doStart() {
		if (timer != null && !timer.isRunning()) {
			alfa1 = 0.0f;
			alfa2 = 1.0f - alfaIncrement;
			index1 = 0;
			index2 = -1;
			timer.start();
		}
	}

	public void doStop() {
		if (timer != null && timer.isRunning()) {
			timer.stop();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == timer) {
			effect();
		}
		else if (e.getSource() == timerSleep) {
			if (!firstSleep) {
				this.timerSleep.stop();
				this.timer.start();
			}
			else {
				firstSleep = false;
			}
		}
	}

	private final class FadePanel extends Panel {

		private static final long serialVersionUID = 1l;

		@Override
		public void paint(Graphics g) {

			int w = getWidth();
			int h = getHeight();

			Graphics2D g2d = ( Graphics2D ) g;

			AlphaComposite a1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alfa1);
			g2d.setComposite(a1);
			g2d.drawImage(images.get(index1), 0, 0, w, h, this);

			if (index2 >= 0) {
				AlphaComposite a2 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alfa2 > 0 ? alfa2 : 0);
				g2d.setComposite(a2);
				g2d.drawImage(images.get(index2), 0, 0, w, h, this);
			}
		}
	}
}
