/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Marco Antonio Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: projetos.freedom <BR>
 *         Classe: @(#)DLRConsProd.java <BR>
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

import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.dialog.FFDialogo;

public class DLRConsProd extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JRadioGroup<?, ?> rgOrdem = null;

	private Vector<String> vLabs = new Vector<String>();

	private Vector<String> vVals = new Vector<String>();

	private JLabelPad lbOrdem = new JLabelPad( "Listar:" );

	public DLRConsProd( Component cOrig ) {

		super( cOrig );
		setTitulo( "Relatório de Produtos  " );
		setAtribos( 250, 240 );

		vLabs.addElement( "Fornecedores" );
		vLabs.addElement( "Compras" );
		vLabs.addElement( "Vendas" );
		vVals.addElement( "F" );
		vVals.addElement( "C" );
		vVals.addElement( "V" );

		rgOrdem = new JRadioGroup<String, String>( 3, 1, vLabs, vVals );

		adic( lbOrdem, 7, 15, 150, 15 );
		adic( rgOrdem, 7, 40, 220, 100 );

	}

	public String getValores() {

		String sRetorno = "";

		sRetorno = rgOrdem.getVlrString();

		return sRetorno;

	}

}
