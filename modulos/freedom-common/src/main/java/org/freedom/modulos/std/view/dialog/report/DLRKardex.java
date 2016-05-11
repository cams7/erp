package org.freedom.modulos.std.view.dialog.report;

import java.util.Vector;

import java.awt.Component;

import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.dialog.FFDialogo;


public class DLRKardex extends FFDialogo {
	
	private static final long serialVersionUID = 1L;
	
	private JRadioGroup<?, ?> rgModo = null;
	
	private Vector<String> vLabsModo = new Vector<String>();

	private Vector<String> vValsModo = new Vector<String>();
	
	public DLRKardex ( Component cOrig ){
		
		super( cOrig );
		setTitulo( "Relatório Kardex de Produtos" );
		setAtribos( 300, 200 );
		setLocationRelativeTo( this );
		
		vLabsModo.addElement( "Gráfico" );
		vLabsModo.addElement( "Texto" );
		vValsModo.addElement( "G" );
		vValsModo.addElement( "T" );
		
		rgModo = new JRadioGroup<String, String>( 2, 3, vLabsModo, vValsModo );
		rgModo.setVlrString( "G" );
		
		adic( new JLabelPad( "Modelo do relatório:" ), 7, 20, 200, 20 );
		adic( rgModo, 7, 40, 150, 50 );
		
	}
	
	public String[] getValores() {
		
		String[] sRetorno = new String[ 1 ];
		
		sRetorno[ 0 ] = rgModo.getVlrString();
		
		return sRetorno;		
	}

}
