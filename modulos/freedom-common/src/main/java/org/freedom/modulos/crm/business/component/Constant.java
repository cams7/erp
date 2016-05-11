package org.freedom.modulos.crm.business.component;

import java.awt.Color;


public class Constant extends org.freedom.infra.pojos.Constant {
	
	private static final long serialVersionUID = 1L;
	private Color bgcolor = null;
	private Color fgcolor = null;

	public Constant( String name, Object value ) {

		super( name, value );
		
	}
	
	public Constant( String name, Object value, Color bgcolor, Color fgcolor ) {
		super( name, value);
		setBgcolor( bgcolor );
		setFgcolor( fgcolor );
	}
	
	public Color getBgcolor() {
	
		return bgcolor;
	}

	
	public void setBgcolor( Color bgcolor ) {
	
		this.bgcolor = bgcolor;
	}

	
	public Color getFgcolor() {
	
		return fgcolor;
	}

	
	public void setFgcolor( Color fgcolor ) {
	
		this.fgcolor = fgcolor;
	}


}
