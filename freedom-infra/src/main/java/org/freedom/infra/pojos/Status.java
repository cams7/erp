package org.freedom.infra.pojos;

import java.awt.Color;

import javax.swing.ImageIcon;

public class Status implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Object value = null;

	private String name = null;
	
	private String detail = null;
	
	private Color color = null;
	
	private ImageIcon image = null;
	
	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Status(String name, Object value, String details, Color color, ImageIcon image) {
		setName(name);
		setValue(value);
		setColor(color);
		setDetail(detail);
		setImage(image);
	}
	
	public Status(String name, Object value, Color color) {
		setName(name);
		setValue(value);
		setColor(color);
	}
	
	public Status(String name, Object value ) {
		setName(name);
		setValue(value);
	}

	public Object getValue() {
		return value;
	}

	private void setValue(Object value) {
		this.value = value;
	}

	public ImageIcon getImage() {
		return image;
	}

	public void setImage(ImageIcon image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return getName();
	}

}
