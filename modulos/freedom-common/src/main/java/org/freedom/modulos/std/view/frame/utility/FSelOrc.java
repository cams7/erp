/**
 * @version 13/05/2008 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FSelOrc.java <BR>
 * 
 *                  Este programa ï¿½ licenciado de acordo com a LPG-PC (Licenï¿½a Pï¿½blica Geral para Programas de Computador), <BR>
 *                  versï¿½o 2.1.0 ou qualquer versï¿½o posterior. <BR>
 *                  A LPG-PC deve acompanhar todas PUBLICAï¿½ï¿½ES, DISTRIBUIï¿½ï¿½ES e REPRODUï¿½ï¿½ES deste Programa. <BR>
 *                  Caso uma cï¿½pia da LPG-PC nï¿½o esteja disponï¿½vel junto com este Programa, vocï¿½ pode contatar <BR>
 *                  o LICENCIADOR ou entï¿½o pegar uma cï¿½pia em: <BR>
 *                  Licenï¿½a: http://www.lpg.adv.br/licencas/lpgpc.rtf <BR>
 *                  Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa ï¿½ preciso estar <BR>
 *                  de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * 
 * 
 */

package org.freedom.modulos.std.view.frame.utility;

import java.util.Vector;

import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.dialog.FFDialogo;

public class FSelOrc extends FFDialogo {

	private static final long serialVersionUID = 1L;

	String retorno = null;

	JRadioGroup<?, ?> Rgrel = null;

	Vector<String> vLabs = new Vector<String>();

	Vector<String> vVals = new Vector<String>();

	int tamanho = 0;


	public FSelOrc() {
		
		setTitulo( "Layouts", "Layouts" );
		setAtribos( 300, 220 );
	}

	
	public String seleciona( String[] arg, String[] desc ) {

		for ( int i = 0; i < arg.length; i++ ) {
			if ( i < desc.length ) {
				vLabs.addElement( desc[ i ].trim() );
			}
			else {
				vLabs.addElement( arg[ i ].trim() );
			}
			vVals.addElement( arg[ i ].trim() );
		}
		Rgrel = new JRadioGroup<String, String>( arg.length, 1, vLabs, vVals );
		tamanho = arg.length;
		montaTela();
		setVisible( true );

		if ( OK ) {
			retorno = Rgrel.getVlrString();
		}

		return retorno;
	}

	public void montaTela() {

		adic( new JLabelPad( "Escolha o Layout: " ), 7, 5, 150, 10 );
		adic( Rgrel, 7, 25, 250, tamanho + 75 );
	}
}
