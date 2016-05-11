/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)DLDataTransf.java <BR>
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

package org.freedom.modulos.std.view.dialog.utility;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Date;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;

public class DLDataTransf extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataTransf = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	public DLDataTransf( Component cOrig ) {

		super( cOrig );
		setTitulo( "Nova Data" );
		setAtribos( 100, 100, 250, 120 );

		adic( new JLabelPad( "Nova Data:" ), 7, 0, 110, 20 );
		adic( txtDataTransf, 7, 20, 100, 20 );
	}

	public Date getValor() {

		return txtDataTransf.getVlrDate();
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			if ( txtDataTransf.getText().length() < 10 ) {
				Funcoes.mensagemInforma( this, "Valor invalido para data!" );
				txtDataTransf.requestFocus();
				return;
			}
		}
		super.actionPerformed( evt );
	}

}
