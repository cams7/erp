/**
 * @version 18/05/2005 <BR>
 * @author Setpoint Informática Ltda / Robson Sanchez.
 * 
 * Projeto: Freedom <BR>
 * 
 * Pacote: org.freedom.modulos.fnc.view.dialog.report <BR>
 * Classe:
 * @(#)DLRRemessa.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * Dialog para escolha do formato de impressão do relatório de remessa de cobrança.
 * 
 */

package org.freedom.modulos.fnc.view.dialog.report;

import java.awt.Component;
import java.util.Vector;

import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.dialog.FFDialogo;

public class DLRRemessa extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JRadioGroup<?, ?> rgFormato = null;

	private JLabelPad lbFormato = new JLabelPad( "Formato:" );

	private Vector<String> vLabs = new Vector<String>();

	private Vector<String> vVals = new Vector<String>();

	public DLRRemessa( Component cOrig ) {

		super( cOrig );
		
		setTitulo( "Relatório de Remessa" );
		setAtribos( 291, 160 );

		vLabs.addElement( "Retrato" );
		vLabs.addElement( "Paisagem" );

		vVals.addElement( "R" );
		vVals.addElement( "P" );
		
		rgFormato = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgFormato.setVlrString( "R" );
		
		adic( lbFormato, 7, 10, 80, 15 );
		adic( rgFormato, 7, 30, 264, 30 );

	}

	public String getFormato() {

		return rgFormato.getVlrString();
	}
	
}
