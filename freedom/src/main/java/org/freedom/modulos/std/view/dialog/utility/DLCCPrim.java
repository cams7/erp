/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)DLCCPrim.java <BR>
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;

public class DLCCPrim extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtDescCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtSigla = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JPanelPad pinCont = new JPanelPad( 450, 100 );

	private JLabelPad lbCod = new JLabelPad( "Código" );

	private JLabelPad lbDesc = new JLabelPad( "Descrição" );

	public DLCCPrim( Component cOrig, String sCod, String sDesc, String sSigla ) {

		super( cOrig );
		setTitulo( "Novo Item \"Nivel 1\"" );
		setAtribos( 450, 130 );
		Funcoes.setBordReq( txtDescCont );
		txtCodCont.setAtivo( false );
		txtCodCont.setVlrString( sCod );
		pinCont.adic( lbCod, 7, 5, 80, 20 );
		pinCont.adic( txtCodCont, 7, 25, 100, 20 );
		pinCont.adic( lbDesc, 110, 5, 150, 20 );
		pinCont.adic( txtDescCont, 110, 25, 217, 20 );
		pinCont.adic( new JLabelPad( "Sig." ), 330, 5, 100, 20 );
		pinCont.adic( txtSigla, 330, 25, 80, 20 );
		c.add( pinCont, BorderLayout.CENTER );
		if ( sDesc != null ) {
			setTitulo( "Edição de Conta de 1º Nivel" );
			txtDescCont.setVlrString( sDesc );
			txtSigla.setVlrString( sSigla );
		}
		txtDescCont.requestFocus();

		Funcoes.setBordReq( txtSigla );
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			if ( txtDescCont.getText().trim().length() == 0 ) {
				Funcoes.mensagemInforma( this, "O campo descrição está em branco! ! !" );
				txtDescCont.requestFocus();
				return;
			}
			else if ( txtSigla.getText().trim().length() == 0 ) {
				Funcoes.mensagemInforma( this, "O campo sigla está em branco! ! !" );
				txtSigla.requestFocus();
				return;
			}
		}
		super.actionPerformed( evt );
	}

	public String[] getValores() {

		String sRetorno[] = { txtDescCont.getVlrString(), txtSigla.getVlrString() };
		return sRetorno;
	}
}
