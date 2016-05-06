package org.freedom.infra.x.UIMaker;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import org.freedom.infra.x.swing.FDPanel;

public class Panel extends FDPanel {

	private static final long serialVersionUID = 1l;

	public static final float ALFA_NONE = -1f;

	private Image wallpaper;

	private float alfa = ALFA_NONE;

	private int arcWidth;

	private int arcHeight;

	public Panel() {
		super();
	}

	public Panel(final LayoutManager layout) {
		super(layout);
	}

	public Panel(final float alfa) {

		this();
		setAlfa(alfa);
	}

	public Panel(final float alfa, final Color color) {

		this();
		setAlfaColor(alfa, color);
	}

	public Image getWallpaper() {
		return wallpaper;
	}

	public void setWallpaper(Image wallpaper) {
		this.wallpaper = wallpaper;
	}

	public float getAlfa() {

		return alfa;
	}

	public void setAlfa(final float alfa) {

		this.alfa = alfa >= 0 ? alfa : ALFA_NONE;
	}

	public void setAlfaColor(final float alfa, final Color color) {

		setAlfa(alfa);
		setBackground(color);
	}

	public int getArcWidth() {
		return arcWidth;
	}

	public void setArcWidth(int arcWidth) {
		this.arcWidth = arcWidth;
	}

	public int getArcHeight() {
		return arcHeight;
	}

	public void setArcHeight(int arcHeight) {
		this.arcHeight = arcHeight;
	}

	public void setArc(int arcWidth, int arcHeight) {
		setArcWidth(arcWidth);
		setArcHeight(arcHeight);
	}

	@Override
	public void paintComponent(Graphics g) {

		paintWallpaper(g);

		if (alfa > ALFA_NONE) {
			paintAlfa(g);
		}
		else if (( arcWidth > 0 || arcHeight > 0 ) && isOpaque()) {
			paintArc(g);
		}

		super.paintComponent(g);
	}

	private void paintWallpaper(Graphics g) {

		if (wallpaper == null) {
			return;
		}

		g.drawImage(wallpaper, 0, 0, getWidth(), getHeight(), this);
	}

	private void paintAlfa(Graphics g) {

		setOpaque(false);

		Graphics2D g2d = ( Graphics2D ) g;

		Composite oldComposite = g2d.getComposite();

		AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alfa);
		g2d.setComposite(alphaComposite);

		Rectangle r = g.getClipBounds();
		Color c = getBackground();
		if (c == null) {
			c = Color.lightGray;
		}
		g.setColor(c);
		if (r != null) {
			g.fillRoundRect(r.x, r.y, r.width, r.height, arcWidth >= 0 ? arcWidth : 0, arcHeight >= 0 ? arcHeight : 0);
		}
		else {
			g.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth >= 0 ? arcWidth : 0, arcHeight >= 0 ? arcHeight : 0);
		}

		g2d.setComposite(oldComposite);
	}

	private void paintArc(Graphics g) {

		setOpaque(false);

		Rectangle r = g.getClipBounds();
		Color c = getBackground();
		if (c == null) {
			c = Color.lightGray;
		}
		g.setColor(c);
		if (r != null) {
			g.fillRoundRect(r.x, r.y, r.width, r.height, arcWidth >= 0 ? arcWidth : 0, arcHeight >= 0 ? arcHeight : 0);
		}
		else {
			g.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth >= 0 ? arcWidth : 0, arcHeight >= 0 ? arcHeight : 0);
		}

		setOpaque(false);
	}

	public void setBackground(Color color, boolean opaque) {
		super.setBackground(color);
		super.setOpaque(opaque);
	}
}
