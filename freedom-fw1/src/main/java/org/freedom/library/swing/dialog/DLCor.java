package org.freedom.library.swing.dialog;

import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DLCor extends FDialogo implements ChangeListener {
	
	private static final long serialVersionUID = 1L;
	
	private JColorChooser tcc = new JColorChooser();
	
	private Color cor = null;
	
	public DLCor() {
		
		super();
		setAtribos( 400, 400 );
		setTitulo( "Selecione uma cor" );

		add(tcc);
		
		tcc.getSelectionModel().addChangeListener( this );
		
		
	}
	
	public void stateChanged( ChangeEvent e ) {

		cor = tcc.getColor();
		
	}
	
	public Color getCor() {
		
		return cor;
		
	}
	
	public void setCor( Color corp ) {
		
		this.cor = corp;
		tcc.setColor(cor);
		tcc.repaint();
		tcc.revalidate();
		
	}

	

}
