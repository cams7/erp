/**
 * @version 07/05/2004 <BR>
 * @author Setpoint Informática Ltda./Marco Antonio Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)DLRClasFiscal.java <BR>
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

import java.awt.Component;
import java.util.Vector;

import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.dialog.FFDialogo;

public class DLRClasFiscal extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JRadioGroup<?, ?> rgOrdem = null;

	private JCheckBoxPad cbListaExec = new JCheckBoxPad( "Listar exeções ? ", "S", "N" );

	private JLabelPad lbOrdem = new JLabelPad( "Ordenar por:" );

	private Vector<String> vLabs = new Vector<String>();

	private Vector<String> vVals = new Vector<String>();

	public DLRClasFiscal( Component cOrig ) {

		super( cOrig );
		setTitulo( "Ordem do Relatório" );
		setAtribos( 300, 180 );
		vLabs.addElement( "Código" );
		vLabs.addElement( "Descrição" );
		vVals.addElement( "C" );
		vVals.addElement( "D" );
		rgOrdem = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgOrdem.setVlrString( "D" );
		adic( lbOrdem, 7, 5, 80, 15 );
		adic( rgOrdem, 7, 25, 280, 30 );
		adic( cbListaExec, 7, 60, 280, 30 );

		cbListaExec.setVlrString( "N" );

	}

	public String[] getValores() {

		String[] sRetorno = new String[ 2 ];
		sRetorno[ 0 ] = cbListaExec.getVlrString();
		sRetorno[ 1 ] = rgOrdem.getVlrString();

		return sRetorno;
	}

	public String getValor() {

		String sOrdem = "";
		if ( rgOrdem.getVlrString().compareTo( "C" ) == 0 )
			sOrdem = "CODFISC";
		else if ( rgOrdem.getVlrString().compareTo( "D" ) == 0 )
			sOrdem = "DESCFISC";
		return sOrdem;
	}
}
