/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)DLDescontItVenda.java <BR>
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
import java.awt.event.KeyEvent;
import java.math.BigDecimal;

import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;

public class DLDescontItVenda extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDesc1 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 4, 1 );

	private JTextFieldPad txtDesc2 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 4, 1 );

	private JTextFieldPad txtDesc3 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 4, 1 );

	private JTextFieldPad txtDesc4 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 4, 1 );

	private JTextFieldPad txtDesc5 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 4, 1 );

	private JTextFieldPad txtVlrDescTot = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 8, 2 );

	private JTextFieldPad txtVlrTot = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private double dVlr;

	private String sObs;

	public DLDescontItVenda( Component cOrig, double dVal, String[] sVals ) {

		super( cOrig );
		dVlr = dVal;
		setTitulo( "Descontos:" );
		setAtribos( 265, 225 );

		txtVlrDescTot.setAtivo( false );
		txtVlrTot.setAtivo( false );

		txtVlrDescTot.setVlrBigDecimal( new BigDecimal( 0 ) );
		txtVlrTot.setVlrBigDecimal( new BigDecimal( dVlr ) );

		adic( new JLabelPad( "1 :" ), 7, 10, 20, 20 );
		adic( txtDesc1, 30, 10, 77, 20 );
		adic( new JLabelPad( "2 :" ), 7, 35, 20, 20 );
		adic( txtDesc2, 30, 35, 77, 20 );
		adic( new JLabelPad( "3 :" ), 7, 60, 20, 20 );
		adic( txtDesc3, 30, 60, 77, 20 );
		adic( new JLabelPad( "4 :" ), 7, 85, 20, 20 );
		adic( txtDesc4, 30, 85, 77, 20 );
		adic( new JLabelPad( "5 :" ), 7, 110, 20, 20 );
		adic( txtDesc5, 30, 110, 77, 20 );
		adic( new JLabelPad( "Valor Desc.:" ), 120, 10, 120, 20 );
		adic( txtVlrDescTot, 120, 35, 120, 20 );
		adic( new JLabelPad( "Preço do Item:" ), 120, 60, 120, 20 );
		adic( txtVlrTot, 120, 85, 120, 20 );

		txtDesc1.setVlrString( sVals[ 0 ] );
		txtDesc2.setVlrString( sVals[ 1 ] );
		txtDesc3.setVlrString( sVals[ 2 ] );
		txtDesc4.setVlrString( sVals[ 3 ] );
		txtDesc5.setVlrString( sVals[ 4 ] );

		calc();
	}

	private void calc() {

		double dVlrTot = dVlr;
		double dVlrDescTot = 0;
		double dVlrTmpDesc = 0;
		double[] dSet = new double[ 5 ];
		String sSep = "";
		String sVal = "";
		sObs = "";
		dSet[ 0 ] = txtDesc1.getVlrDouble().doubleValue();
		dSet[ 1 ] = txtDesc2.getVlrDouble().doubleValue();
		dSet[ 2 ] = txtDesc3.getVlrDouble().doubleValue();
		dSet[ 3 ] = txtDesc4.getVlrDouble().doubleValue();
		dSet[ 4 ] = txtDesc5.getVlrDouble().doubleValue();
		for ( int i = 0; i < 5; i++ ) {
			if ( dSet[ i ] != 0 ) {
				dVlrTmpDesc = dVlrTot * ( dSet[ i ] / 100 );
				dVlrTot -= dVlrTmpDesc;
				dVlrDescTot += dVlrTmpDesc;
				sVal = ( dSet[ i ] - dSet[ i ] ) > 0 ? "" + dSet[ i ] : "" + (int) dSet[ i ];
				sObs += sSep + sVal;
				sSep = " + ";
			}
		}
		txtVlrDescTot.setVlrBigDecimal( new BigDecimal( dVlrDescTot ) );
		txtVlrTot.setVlrBigDecimal( new BigDecimal( dVlrTot ) );
	}

	public double getValor() {

		return txtVlrDescTot.getVlrDouble().doubleValue();
	}

	public String getObs() {

		return "Desc.: " + sObs;
	}

	public void keyPressed( KeyEvent kevt ) {

		if ( kevt.getKeyCode() == KeyEvent.VK_ENTER ) {
			if ( kevt.getSource() == txtDesc1 )
				txtDesc2.requestFocus();
			else if ( kevt.getSource() == txtDesc2 )
				txtDesc3.requestFocus();
			else if ( kevt.getSource() == txtDesc3 )
				txtDesc4.requestFocus();
			else if ( kevt.getSource() == txtDesc4 )
				txtDesc5.requestFocus();
			else if ( kevt.getSource() == txtDesc5 )
				btOK.requestFocus();
			calc();
		}
		super.keyPressed( kevt );
	}
}
