/**
 * @version 24/06/2009 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)DLCancItem.java <BR>
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
 *                     Comentários sobre a classe...
 */

package org.freedom.modulos.std.view.dialog.utility;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JScrollPane;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.dialog.FFDialogo;

public class DLCancItem extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private final JTextAreaPad txaObs = new JTextAreaPad( 250 );

	private JScrollPane spn = new JScrollPane( txaObs );

	public DLCancItem( Component cOrig ) {

		super( cOrig );
		setTitulo( "Motivo do Cancelamento" );
		setAtribos( 360, 250 );
		montaTela();

	}

	private void montaTela() {

		c.add( spn, BorderLayout.CENTER );
	}

	public String getValor() {

		return txaObs.getVlrString();
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK && "".equals( txaObs.getVlrString().trim() ) ) {
			Funcoes.mensagemInforma( this, "Motivo do cancelamento requerido!" );
		}
		else {
			super.actionPerformed( evt );
		}
	}

}
