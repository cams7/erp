/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)DLPlanPrim.java <BR>
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
 *         Tela de cadastro de planejamento financeiro (Nível 1).
 */

package org.freedom.modulos.std.view.dialog.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Vector;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;

public class DLPlanPrim extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtDescCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JPanelPad pinCont = new JPanelPad( 360, 220 );

	private Vector<String> vVals = new Vector<String>();

	private Vector<String> vLabs = new Vector<String>();

	private JRadioGroup<?, ?> rgTipo = null;

	private JTabbedPanePad ptb = new JTabbedPanePad();

	private JLabelPad lbCod = new JLabelPad( "Código" );

	private JLabelPad lbDesc = new JLabelPad( "Descrição" );

	public DLPlanPrim( Component cOrig, String sCod, String sDesc, String sTipo ) {

		super( cOrig );
		setTitulo( "Planejamento financeiro (Nível 1)" );
		setAtribos( 360, 220 );
		Funcoes.setBordReq( txtDescCont );
		txtCodCont.setBackground( Color.lightGray );
		txtCodCont.setFont( new Font( "Dialog", Font.BOLD, 12 ) );
		txtCodCont.setEditable( false );
		txtCodCont.setForeground( new Color( 118, 89, 170 ) );
		txtCodCont.setVlrString( sCod );
		vVals.addElement( "B" );
		vVals.addElement( "R" );
		vVals.addElement( "D" );
		vLabs.addElement( "Caixa e Bancos" );
		vLabs.addElement( "Receita" );
		vLabs.addElement( "Despesa" );
		rgTipo = new JRadioGroup<String, String>( 1, 3, vLabs, vVals );
		rgTipo.setVlrString( "D" );
		pinCont.adic( lbCod, 7, 0, 80, 20 );
		pinCont.adic( txtCodCont, 7, 20, 100, 20 );
		pinCont.adic( lbDesc, 110, 0, 150, 20 );
		pinCont.adic( txtDescCont, 110, 20, 220, 20 );
		pinCont.adic( rgTipo, 7, 50, 323, 25 );
		ptb.add( "1º Nivel", pinCont );
		c.add( ptb, BorderLayout.CENTER );
		if ( sDesc != null ) {
			setTitulo( "Edição de Conta de 1º Nivel" );
			rgTipo.setVlrString( sTipo );
			txtDescCont.setVlrString( sDesc );
			txtDescCont.selectAll();
			rgTipo.setVlrString( sTipo );
			rgTipo.setAtivo( 0, false );
			rgTipo.setAtivo( 1, false );
			rgTipo.setAtivo( 2, false );
		}
		txtDescCont.requestFocus();
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			if ( txtDescCont.getText().trim().length() == 0 ) {
				Funcoes.mensagemInforma( this, "O campo descrição está em branco! ! !" );
				txtDescCont.requestFocus();
				return;
			}
		}
		super.actionPerformed( evt );
	}

	public String[] getValores() {

		String[] sRetorno = { txtDescCont.getText(), rgTipo.getVlrString() };
		return sRetorno;
	}
}
