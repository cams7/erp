package org.freedom.modulos.std.view.dialog.utility;

import java.util.Vector;

import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.dialog.FFDialogo;

public class DLTranspFor extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JRadioGroup<?, ?> rgOrdem = null;
	
	private JRadioGroup<?, ?> rgTipo = null;

	private JLabelPad lbOrdem = new JLabelPad( "Ordenar por:" );
	
	private JLabelPad lbTipo = new JLabelPad( "Tipo:" );

	public DLTranspFor() {

		setTitulo( "Tipo/Ordem do Relatório" );
		setAtribos( 300, 200 );

		Vector<String> vLabs = new Vector<String>();
		Vector<String> vVals = new Vector<String>();
		vLabs.addElement( "Simples" );
		vLabs.addElement( "Detalhado" );
		vVals.addElement( "S" );
		vVals.addElement( "D" );
		rgTipo = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgTipo.setVlrString( "S" );
		adic( lbTipo, 7, 0, 80, 15 );
		adic( rgTipo, 7, 20, 270, 30 );
		
		vLabs = new Vector<String>();
		vVals = new Vector<String>();
		vLabs.addElement( "Código" );
		vLabs.addElement( "Descrição" );
		vVals.addElement( "C" );
		vVals.addElement( "D" );
		rgOrdem = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgOrdem.setVlrString( "C" );
		adic( lbOrdem, 7, 55, 80, 15 );
		adic( rgOrdem, 7, 75, 270, 30 );

		Vector<String> vLabs1 = new Vector<String>();
		Vector<String> vVals1 = new Vector<String>();

	}

	public String getValor() {
		String sRetorno = "";

		if ( "C".equals( rgOrdem.getVlrString() ) ) {
			sRetorno = "T.CODTRAN";
		} else if ( "D".equals( rgOrdem.getVlrString() ) ) {
			sRetorno = "T.NOMETRAN";
		}

		return sRetorno;
	}
	
	public String getTipo(){
		return rgTipo.getVlrString();
	}

}
