/**
 * @version 18/05/2005 <BR>
 * @author Setpoint Informática Ltda.
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)DLRCheque.java <BR>
 * 
 *                    Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                    modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                    na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                    Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                    sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                    Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                    Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                    de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                    ? <BR>
 * 
 */

package org.freedom.modulos.fnc.view.dialog.report;

import java.awt.Component;
import java.util.Vector;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;

public class DLRCheque extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JRadioGroup<?, ?> rgOrdem = null;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtNome = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JLabelPad lbOrdem = new JLabelPad( "Ordenar por:" );

	private Vector<String> vLabs = new Vector<String>();

	private Vector<String> vVals = new Vector<String>();

	public DLRCheque( Component cOrig ) {

		super( cOrig );
		setTitulo( "Relatório de Entrada de Cheque" );
		setAtribos( 291, 234 );

		adic( new JLabelPad( "Periodo:" ), 7, 5, 125, 20 );
		adic( new JLabelPad( "De:" ), 7, 25, 35, 20 );
		adic( txtDataini, 32, 25, 100, 20 );
		adic( new JLabelPad( "Até:" ), 140, 25, 25, 20 );
		adic( txtDatafim, 170, 25, 100, 20 );

		vLabs.addElement( "Data" );
		vLabs.addElement( "Nome" );
		vVals.addElement( "D" );
		vVals.addElement( "N" );
		rgOrdem = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgOrdem.setVlrString( "D" );
		adic( lbOrdem, 7, 50, 80, 15 );
		adic( rgOrdem, 7, 70, 264, 30 );

		adic( new JLabelPad( "Cliente" ), 7, 100, 75, 20 );
		adic( txtNome, 7, 120, 264, 20 );
	}

	public JTextFieldPad GetDataini() {

		return txtDataini;
	}

	public String getCNome() {

		return txtNome.getVlrString();
	}

	public JTextFieldPad GetDatafim() {

		return txtDatafim;
	}

	public boolean CompData() {

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return false;
		}
		else
			return true;
	}

	public String getValor() {

		String sRetorno = "";
		if ( rgOrdem.getVlrString().compareTo( "D" ) == 0 )
			sRetorno = "sgcheque.DTINS";
		else if ( rgOrdem.getVlrString().compareTo( "N" ) == 0 )
			sRetorno = "vdcliente.NOMECLI";
		return sRetorno;
	}

}
