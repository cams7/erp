/**
 * @version 04/03/2013 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std.view.dialog.utility <BR>
 *         Classe:
 * @(#)DLSelLayout.java <BR>
 * 
 *                     Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                     modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                     na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                     Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                     sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                     Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                     Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                     de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                     Classe de seleção de layouts
 */

package org.freedom.modulos.std.view.dialog.utility;

import java.awt.Component;
import java.util.Vector;

import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.dialog.FFDialogo;

public class DLSelLayout extends FFDialogo {

	private Component owner = null;

	private static final long serialVersionUID = 1L;

	private JRadioGroup<String, String> rgLayout = null;
	
	private Vector<String> labLayout = new Vector<String>();
	private Vector<String> valLayout = new Vector<String>();

	public DLSelLayout( Component cOrig, String class01, String class02 ) {
		super( cOrig );
		setTitulo( "Seleção de layout" );
		setAtribos( 250, 180 );
		labLayout.addElement( class01 );
		labLayout.addElement( class02 );
		valLayout.addElement( class01 );
		valLayout.addElement( class02 );
		rgLayout = new JRadioGroup<String, String>( 2, 1, labLayout, valLayout );
		adic( rgLayout, 7, 7, 200, 60 );
	}

	public String getLayoutSel() {
		return rgLayout.getVlrString();
	}
}
