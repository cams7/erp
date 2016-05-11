package org.freedom.modulos.std.view.dialog.utility;

import javax.swing.JScrollPane;

import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.dialog.FDialogo;

public class DLBuscaDescProd extends FDialogo {

	private static final long serialVersionUID = 1l;

	private JPanelPad pn = new JPanelPad();

	private JTextAreaPad txa = new JTextAreaPad();

	public DLBuscaDescProd( final String sDesc ) {

		super();

		setTitle( "Descrição completa" );
		setAtribos( 400, 200 );

		txa.setVlrString( sDesc );
		pn.adic( new JScrollPane( txa ), 0, 0, 385, 133 );
		c.add( pn );
	}

	public String getTexto() {

		return txa.getVlrString();
	}
	public void setEditable(boolean editable) {
		txa.setEditable( editable );
	}
}
