/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)DLPlanSin.java <BR>
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
 *         Tela de cadastro de planejamento financeiro (Contas sintéticas).
 */

package org.freedom.modulos.std.view.dialog.utility;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Vector;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;

public class DLPlanSin extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodPai = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDescPai = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodSin = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDescSin = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JLabelPad lbCodPai = new JLabelPad( "Cód.origem" );

	private JLabelPad lbDescPai = new JLabelPad( "Descrição da origem" );

	private JLabelPad lbCodSin = new JLabelPad( "Código" );

	private JLabelPad lbDescSin = new JLabelPad( "Descrição" );

	private Vector<String> vVals = new Vector<String>();

	private Vector<String> vLabs = new Vector<String>();

	private JRadioGroup<?, ?> rgTipo = null;

	private String sCodPai = "";

	private String sDesc = "";

	public DLPlanSin( Component cOrig, String sCodP, String sDescPai, String sCod, String sD, String sTipo ) {

		super( cOrig );
		setTitulo( "Planejamento financeiro (Conta Sintética)" );
		setAtribos( 310, 250 );
		sCodPai = sCodP;
		sDesc = sD;
		cancText( txtCodPai );
		cancText( txtDescPai );
		cancText( txtCodSin );
		Funcoes.setBordReq( txtDescSin );
		txtCodPai.setVlrString( sCodPai );
		txtDescPai.setVlrString( sDescPai );
		txtCodSin.setVlrString( sCod );
		vVals.addElement( "B" );
		vVals.addElement( "C" );
		vVals.addElement( "D" );
		vVals.addElement( "R" );
		vLabs.addElement( "Bancos" );
		vLabs.addElement( "Caixa" );
		vLabs.addElement( "Despesas" );
		vLabs.addElement( "Receitas" );
		rgTipo = new JRadioGroup<String, String>( 2, 2, vLabs, vVals );
		setTipo( sTipo );
		adic( lbCodPai, 7, 0, 80, 20 );
		adic( txtCodPai, 7, 20, 80, 20 );
		adic( lbDescPai, 90, 0, 200, 20 );
		adic( txtDescPai, 90, 20, 200, 20 );
		adic( lbCodSin, 7, 40, 80, 20 );
		adic( txtCodSin, 7, 60, 80, 20 );
		adic( lbDescSin, 90, 40, 100, 20 );
		adic( txtDescSin, 90, 60, 200, 20 );
		adic( rgTipo, 7, 90, 283, 60 );
		if ( sDesc != null ) {
			setTitulo( "Edição de Conta Sintética" );
			txtDescSin.setVlrString( sDesc );
			txtDescSin.selectAll();
		}
		txtDescSin.requestFocus();
	}

	private void cancText( JTextFieldPad txt ) {

		txt.setBackground( Color.lightGray );
		txt.setFont( new Font( "Dialog", Font.BOLD, 12 ) );
		txt.setEditable( false );
		txt.setForeground( new Color( 118, 89, 170 ) );
	}

	private void setTipo( String sTipo ) {

		if ( ( sDesc == null ) & ( sCodPai.trim().length() == 1 ) & ( sTipo.compareTo( "B" ) == 0 ) ) {
			rgTipo.setVlrString( "B" );
			rgTipo.setAtivo( 0, true );
			rgTipo.setAtivo( 1, true );
			rgTipo.setAtivo( 2, false );
			rgTipo.setAtivo( 3, false );
		}
		else {
			rgTipo.setVlrString( sTipo );
			rgTipo.setAtivo( 0, false );
			rgTipo.setAtivo( 1, false );
			rgTipo.setAtivo( 2, false );
			rgTipo.setAtivo( 3, false );
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			if ( txtDescSin.getText().trim().length() == 0 ) {
				Funcoes.mensagemInforma( this, "O campo descrição está em branco! ! !" );
				txtDescSin.requestFocus();
				return;
			}
		}
		super.actionPerformed( evt );
	}

	public String[] getValores() {

		String[] sRetorno = { txtDescSin.getText(), rgTipo.getVlrString() };
		return sRetorno;
	}
}
