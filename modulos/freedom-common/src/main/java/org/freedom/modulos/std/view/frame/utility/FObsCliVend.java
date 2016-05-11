/**
 * @version 06/09/2005 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FObsCliVend.java <BR>
 * 
 *                      Este programa é licenciado de acordo com a LPG-PC (Licença Pública Geral para Programas de Computador), <BR>
 *                      modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                      A LPG-PC deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste Programa. <BR>
 *                      Caso uma cópia da LPG-PC não esteja disponível junto com este Programa, você pode contatar <BR>
 *                      sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                      Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                      Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa é preciso estar <BR>
 *                      de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                      Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.std.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JScrollPane;

import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.dialog.FFDialogo;

public class FObsCliVend extends FFDialogo implements KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JTextAreaPad txaObs = new JTextAreaPad();

	private JScrollPane spObs = new JScrollPane( txaObs );
	
	private static FObsCliVend tela = null;
	
	
	public FObsCliVend() {

		super();
		txaObs.setEditable( false );
		c.add( spObs, BorderLayout.CENTER );
		
		txaObs.addKeyListener( this );
		btCancel.setVisible( false );

		setModal( false );
		
		addKeyListener( this );
	}

	public static void showVend( int x, int y, int larg, int alt, String sObsCli ) {

		if(tela == null) {
			tela = new FObsCliVend();
			tela.setAtribos( x, y, larg, alt + 50 );
		}
		
		
		tela.txaObs.setText( sObsCli );
		tela.setVisible( true );
		tela.btOK.requestFocus();

	}

	public void keyPressed( KeyEvent kevt ) {

		if ( kevt.getKeyCode() == KeyEvent.VK_SPACE )
			btOK.doClick();
		else
			super.keyPressed( kevt );
	}

	public void keyReleased( KeyEvent kevt ) {

		super.keyReleased( kevt );
	}

	public void keyTyped( KeyEvent kevt ) {

		super.keyTyped( kevt );
	}


	

}
