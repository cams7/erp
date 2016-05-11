/**
 * @version 25/07/2005 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.pdv <BR>
 *         Classe:
 * @(#)FLeituraX.java <BR>
 * 
 *                    Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                    modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                    na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                    Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                    sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                    Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                    Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                    de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                    Tela para impressão de leitura X
 * 
 */

package org.freedom.modulos.pdv;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLayeredPane;

import org.freedom.ecf.app.ControllerECF;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.AplicativoPDV;

public class FLeituraX extends FFDialogo implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final JLayeredPane pn = new JLayeredPane();

	private final ControllerECF ecf;

	public FLeituraX() {

		super();

		setAtribos( 400, 200 );

		ecf = new ControllerECF( AplicativoPDV.getEcfdriver(), AplicativoPDV.getPortaECF(), AplicativoPDV.bModoDemo, AplicativoPDV.getEcflayout() );

		StringBuffer texto = new StringBuffer();
		texto.append( "<HTML>" );
		texto.append( "Impressão de leitura X.<BR><BR>" );
		texto.append( "Pressione [ OK ] para confirmar a impressão.<BR>" );
		texto.append( "Pressione [ CANCELAR ] para sair!<BR>" );
		texto.append( "</HTML>" );

		JLabelPad label = new JLabelPad( texto.toString() );
		label.setFont( new Font( "Arial", Font.PLAIN, 14 ) );
		label.setForeground( Color.BLUE );
		label.setBounds( 40, 10, 300, 100 );

		pn.add( label );

		c.add( pn );

		eUltimo();
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			if ( !ecf.leituraX() ) {
				Funcoes.mensagemErro( this, ecf.getMessageLog() );
			}
		}

		super.actionPerformed( evt );
	}
}
