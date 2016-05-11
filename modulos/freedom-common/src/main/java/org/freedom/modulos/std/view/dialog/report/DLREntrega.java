/**
 * @version 21/07/2003 <BR>
 * @author Setpoint Informática Ltda./Marco Antonio Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)DLRFrete <BR>
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

package org.freedom.modulos.std.view.dialog.report;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

public class DLREntrega extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JRadioGroup<?, ?> rgOrdem = null;

	private JLabelPad lbOrdem = new JLabelPad( "Ordenar por:" );

	private Vector<String> vLabs = new Vector<String>();

	private Vector<String> vVals = new Vector<String>();

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	public DLREntrega() {

		setTitulo( "Ordem do Relatório" );
		setAtribos( 280, 200 );

		GregorianCalendar cPeriodo = new GregorianCalendar();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataini.setVlrDate( cPeriodo.getTime() );

		vLabs.addElement( "Documento" );
		vLabs.addElement( "Data entrega" );
		vVals.addElement( "V" );
		vVals.addElement( "D" );
		
		rgOrdem = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgOrdem.setVlrString( "D" );
		
		adic( new JLabelPad( "Periodo:" ), 7, 5, 120, 20 );
		adic( new JLabelPad( "De:" ), 7, 27, 30, 20 );
	
		adic( txtDataini, 37, 27, 90, 20 );
		
		adic( new JLabelPad( "Até:" ), 140, 27, 30, 20 );
		adic( txtDatafim, 175, 27, 90, 20 );

		adic( lbOrdem, 7, 60, 80, 15 );
		adic( rgOrdem, 7, 80, 255, 28 );
	}

	public String[] getValores() {

		String[] sRetorno = new String[ 11 ];
		
		if ( rgOrdem.getVlrString().compareTo( "V" ) == 0 ) {
		
			sRetorno[ 0 ] = "v.docvenda";
		
		}
		else if ( rgOrdem.getVlrString().compareTo( "D" ) == 0 ) {
		
			sRetorno[ 0 ] = "e.dtentrega";
			
		}
		
		sRetorno[ 1 ] = txtDataini.getVlrString();
		
		sRetorno[ 2 ] = txtDatafim.getVlrString();

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );

		}
		return sRetorno;

	}
}
