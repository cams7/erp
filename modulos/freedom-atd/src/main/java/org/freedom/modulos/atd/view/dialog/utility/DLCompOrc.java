/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.atd <BR>
 *         Classe: @(#)DLCompOrc.java <BR>
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
 * 
 */

package org.freedom.modulos.atd.view.dialog.utility;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;

public class DLCompOrc extends FFDialogo implements FocusListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtPercDescOrc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, 2 );

	private JTextFieldPad txtVlrDescOrc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtPercAdicOrc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, 2 );

	private JTextFieldPad txtVlrAdicOrc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JLabelPad lbPercDescOrc = new JLabelPad( "% Desc." );

	private JLabelPad lbVlrDescOrc = new JLabelPad( "V Desc." );

	private JLabelPad lbPercAdicOrc = new JLabelPad( "% Adic." );

	private JLabelPad lbVlrAdicOrc = new JLabelPad( "V Adic." );

	private JLabelPad lbCodPlanoPag = new JLabelPad( "Código e Desc. do plano de pagto." );

	private JCheckBoxPad cbImpOrc = new JCheckBoxPad( "Imprime Orçamento?", "S", "N" );

	BigDecimal bValProd;

	boolean bDescIt = false;

	boolean bTestaAtend = false;

	JTextFieldPad txtAtend = null;

	JTextFieldPad txtPlano = null;

	public DLCompOrc( Component cOrig, boolean bDIt, BigDecimal bVP, BigDecimal bVD, BigDecimal bVA, JTextFieldPad txtCodPlanoPag, JTextFieldFK txtDescPlanoPag ) {

		super( cOrig );
		bDescIt = bDIt;
		bValProd = bVP;

		setTitulo( "Completar Orçamento" );
		setAtribos( 380, 200 );

		txtCodPlanoPag.setRequerido( true );

		txtVlrDescOrc.setVlrBigDecimal( bVD );
		txtVlrAdicOrc.setVlrBigDecimal( bVA );

		adic( lbCodPlanoPag, 7, 0, 270, 20 );
		adic( txtCodPlanoPag, 7, 20, 80, 20 );
		adic( txtDescPlanoPag, 90, 20, 260, 20 );
		adic( lbPercDescOrc, 7, 40, 80, 20 );
		adic( txtPercDescOrc, 7, 60, 80, 20 );
		adic( lbVlrDescOrc, 90, 40, 87, 20 );
		adic( txtVlrDescOrc, 90, 60, 87, 20 );
		adic( lbPercAdicOrc, 180, 40, 77, 20 );
		adic( txtPercAdicOrc, 180, 60, 77, 20 );
		adic( lbVlrAdicOrc, 260, 40, 90, 20 );
		adic( txtVlrAdicOrc, 260, 60, 90, 20 );
		adic( cbImpOrc, 7, 100, 150, 20 );

		if ( bDIt ) {
			txtPercDescOrc.setAtivo( false );
			txtVlrDescOrc.setAtivo( false );
		}

		txtPlano = txtCodPlanoPag;

		txtPercDescOrc.addFocusListener( this );
		txtVlrDescOrc.addFocusListener( this );
		txtPercAdicOrc.addFocusListener( this );
		txtVlrAdicOrc.addFocusListener( this );

		cbImpOrc.setVlrString( "N" );
	}

	public void setFKAtend( JTextFieldPad txtCodAtend, JTextFieldFK txtDescAtend ) {

		txtCodAtend.setRequerido( true );

		adic( new JLabelPad( "Código e descrição do atendente." ), 7, 40, 270, 20 );
		adic( txtCodAtend, 7, 60, 80, 20 );
		adic( txtDescAtend, 90, 60, 260, 20 );

		// Aumenta mais 40 pxs a janela.

		Rectangle tamAnt = getBounds();
		tamAnt.height += 40;
		setBounds( tamAnt );

		// Move todos os demais componentes 40pxs abaixo.

		lbPercDescOrc.setBounds( 7, 80, 80, 20 );
		txtPercDescOrc.setBounds( 7, 100, 80, 20 );
		lbVlrDescOrc.setBounds( 90, 80, 87, 20 );
		txtVlrDescOrc.setBounds( 90, 100, 87, 20 );
		lbPercAdicOrc.setBounds( 180, 80, 77, 20 );
		txtPercAdicOrc.setBounds( 180, 100, 77, 20 );
		lbVlrAdicOrc.setBounds( 260, 80, 90, 20 );
		txtVlrAdicOrc.setBounds( 260, 100, 90, 20 );
		cbImpOrc.setBounds( 7, 140, 150, 20 );

		txtAtend = txtCodAtend;
		bTestaAtend = true;
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			if ( txtPlano.getVlrInteger().intValue() == 0 ) {
				Funcoes.mensagemInforma( this, "O campo 'Código do plano de pagamento' é requerido!" );
				txtPlano.requestFocus();
				return;
			}
			else if ( bTestaAtend ) {
				if ( txtAtend.getVlrInteger().intValue() == 0 ) {
					Funcoes.mensagemInforma( this, "O campo 'Código do atendente' é requerido!" );
					txtAtend.requestFocus();
					return;
				}
			}
		}
		super.actionPerformed( evt );
	}

	public Object[] getValores() {

		Object[] bRetorno = new Object[ 3 ];
		bRetorno[ 0 ] = txtVlrDescOrc.getVlrBigDecimal();
		bRetorno[ 1 ] = txtVlrAdicOrc.getVlrBigDecimal();
		bRetorno[ 2 ] = cbImpOrc.getVlrString();
		return bRetorno;
	}

	public void focusLost( FocusEvent fevt ) {

		if ( fevt.getSource() == txtPercDescOrc ) {
			if ( txtPercDescOrc.getText().trim().length() < 1 ) {
				txtVlrDescOrc.setAtivo( true );
			}
			else {
				txtVlrDescOrc.setVlrBigDecimal( bValProd.multiply( txtPercDescOrc.getVlrBigDecimal() ).divide( new BigDecimal( "100" ), 3, BigDecimal.ROUND_HALF_UP ) );
				txtVlrDescOrc.setAtivo( false );
			}
		}
		if ( fevt.getSource() == txtPercAdicOrc ) {
			if ( txtPercAdicOrc.getText().trim().length() < 1 ) {
				txtVlrAdicOrc.setAtivo( true );
			}
			else {
				txtVlrAdicOrc.setVlrBigDecimal( bValProd.multiply( txtPercAdicOrc.getVlrBigDecimal() ).divide( new BigDecimal( "100" ), 3, BigDecimal.ROUND_HALF_UP ) );
				txtVlrAdicOrc.setAtivo( false );
			}
		}
	}

	public void focusGained( FocusEvent fevt ) {

	}
}
