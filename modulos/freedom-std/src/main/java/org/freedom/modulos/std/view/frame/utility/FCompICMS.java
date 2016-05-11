/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FCompICMS.java <BR>
 * 
 *         Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *         modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *         na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *         Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *         sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *         Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *         Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *         escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA <BR>
 * <BR>
 * 
 *         Comentários sobre a classe...
 */

package org.freedom.modulos.std.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.freedom.bmps.Icone;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.frame.FFilho;

public class FCompICMS extends FFilho implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCab = new JPanelPad( 400, 100 );

	private JPanelPad pinRod = new JPanelPad( 400, 120 );

	private JPanelPad pnRod = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JButtonPad btSair = new JButtonPad( Icone.novo( "btSair.png" ) );

	public FCompICMS() {

		super( false );
		setTitulo( "Compara ICMS" );
		setAtribos( 50, 50, 400, 400 );

		Container c = getContentPane();
		c.setLayout( new BorderLayout() );
		c.add( pinCab, BorderLayout.NORTH );

		btSair.setPreferredSize( new Dimension( 100, 30 ) );
		pnRod.setPreferredSize( new Dimension( 400, 30 ) );

		pnRod.add( pinRod, BorderLayout.NORTH );
		pnRod.add( btSair, BorderLayout.EAST );
		c.add( pnRod, BorderLayout.SOUTH );

	}

	public void actionPerformed( ActionEvent evt ) {

	}
}
