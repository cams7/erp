package org.freedom.infra.x.UIMaker.effect;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.Timer;

public class Movie extends EffectStandard {

	public static final String START = "START";

	public static final String STOP = "STOP";

	public static final int UP = 11;

	public static final int DONW = 12;

	public static final int LEFT = 13;

	public static final int RIGHT = 14;

	private final List<ActionListener> actionListeners = new ArrayList<ActionListener>();

	private final Timer timer;

	private int direction;

	private int speed;

	private int jump;

	private int passage;

	private int indexPassage;

	public Movie() {

		this(10, 0, 0);
	}

	public Movie(final int jump, final int passage) {

		this(10, jump, passage);
	}

	public Movie(final int speed, final int jump, final int passage) {

		timer = new Timer(speed, this);

		this.speed = speed;
		this.jump = jump;
		this.passage = passage;
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

	public int getDirection() {
		return this.direction;
	}

	public void directionTo(int direction) {

		if (direction == UP || direction == DONW || direction == LEFT || direction == RIGHT) {
			this.direction = direction;
		}
		else {
			throw new IllegalArgumentException("Invalid direction!");
		}
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {

		if (speed > 0) {
			this.speed = speed;
			timer.setDelay(speed);
		}
	}

	public int getJump() {
		return jump;
	}

	public void setJump(int jump) {
		this.jump = jump;
	}

	public int getPassage() {
		return passage;
	}

	public void setPassage(int passage) {
		this.passage = passage;
	}

	public boolean isRunning() {
		return timer.isRunning();
	}

	private synchronized void effect() {

		if (indexPassage < passage) {
			int x, y;
			for (JComponent c : getComponents()) {
				x = c.getX();
				y = c.getY();

				switch (direction) {
				case UP:
					y -= jump;
					break;
				case DONW:
					y += jump;
					break;
				case LEFT:
					x -= jump;
					break;
				case RIGHT:
					x += jump;
					break;
				default:
					break;
				}

				c.setLocation(x, y);
			}
			indexPassage += jump;
		}
		else {
			doStop();
		}
	}

	public void doStart() {
		if (timer != null && !timer.isRunning()) {
			indexPassage = 0;

			fireActionListener(START);

			timer.start();
		}
	}

	public void doStop() {
		if (timer != null && timer.isRunning()) {

			timer.stop();

			fireActionListener(STOP);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == timer) {
			effect();
		}
	}

}
