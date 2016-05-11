/**
 * @version 25/03/2004 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.pcp <BR>
 *         Classe: @(#)DLRRecursos.java <BR>
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
 *         Tela de dialogo para relação de recursos de produção...
 */

package org.freedom.modulos.pcp.view.dialog.report;

import java.awt.Component;
import java.util.Vector;

import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.dialog.FFDialogo;

public class DLRRecursos extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JRadioGroup<?, ?> rgOrdem = null;

	private JRadioGroup<?, ?> rgTipo = null;

	private JLabelPad lbOrdem = new JLabelPad( "Ordenar por:" );

	private JLabelPad lbTipo = new JLabelPad( "Tipo de impressão:" );

	private Vector<String> vLabsOrd = new Vector<String>();

	private Vector<String> vValsOrd = new Vector<String>();

	private Vector<String> vLabsTipo = new Vector<String>();

	private Vector<String> vValsTipo = new Vector<String>();

	public DLRRecursos( Component cOrig ) {

		super( cOrig );
		setTitulo( "Ordem do Relatório" );
		setAtribos( 300, 220 );
		vLabsOrd.addElement( "Código" );
		vLabsOrd.addElement( "Nome" );
		vValsOrd.addElement( "C" );
		vValsOrd.addElement( "N" );
		rgOrdem = new JRadioGroup<String, String>( 1, 2, vLabsOrd, vValsOrd );
		rgOrdem.setVlrString( "N" );

		vLabsTipo.addElement( "Texto" );
		vLabsTipo.addElement( "Gráfica" );
		vValsTipo.addElement( "T" );
		vValsTipo.addElement( "G" );
		rgTipo = new JRadioGroup<String, String>( 1, 2, vLabsTipo, vValsTipo );
		rgTipo.setVlrString( "G" );

		adic( lbOrdem, 7, 5, 80, 15 );
		adic( rgOrdem, 7, 25, 275, 30 );

		adic( lbTipo, 7, 65, 265, 15 );
		adic( rgTipo, 7, 85, 275, 30 );

	}

	public Vector<String> getValores() {

		Vector<String> vRet = new Vector<String>();

		if ( rgOrdem.getVlrString().compareTo( "C" ) == 0 )
			vRet.addElement( "CODRECP" );
		else if ( rgOrdem.getVlrString().compareTo( "N" ) == 0 )
			vRet.addElement( "DESCRECP" );
		if ( rgTipo.getVlrString().compareTo( "G" ) == 0 )
			vRet.addElement( "G" );
		else if ( rgTipo.getVlrString().compareTo( "T" ) == 0 )
			vRet.addElement( "T" );
		return vRet;
	}
}
