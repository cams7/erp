/**
 * @version 16/50/2007 <BR>
 * @author Setpoint Informática Ltda./Alex Rodrigues <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FHistPad.java <BR>
 * 
 *                   Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                   modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                   na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                   Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                   sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                   Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                   Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                   de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                   Tela para cadastro de historicos padrão.
 * 
 */

package org.freedom.modulos.fnc.view.frame.crud.tabbed;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JScrollPane;

import org.freedom.acao.JComboBoxEvent;
import org.freedom.acao.JComboBoxListener;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;

public class FHistPad extends FDados implements ActionListener, JComboBoxListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodHist = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDescHist = new JTextFieldPad( JTextFieldPad.TP_STRING, 80, 0 );

	private final JTextAreaPad txaHistPad = new JTextAreaPad( 500 );

	private final JScrollPane spnHist = new JScrollPane( txaHistPad );

	private final JPanelPad panelCampos = new JPanelPad();

	private final JPanelPad pinCab = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JComboBoxPad cbCamposDin = null;

	public FHistPad() {

		super();
		setTitulo( "Cadastro de Históricos padrão" );
		setAtribos( 50, 50, 440, 380 );

		montaCombos();
		montaTela();

		cbCamposDin.addComboBoxListener( this );

	}

	private void montaTela() {

		txaHistPad.setFont( new Font( "Courier", Font.PLAIN, 11 ) );
		txaHistPad.setTabSize( 0 );

		panelCampos.setPreferredSize( new Dimension( 440, 90 ) );
		setPainel( panelCampos );

		adicCampo( txtCodHist, 7, 20, 70, 20, "CodHist", "Cód.hist.", ListaCampos.DB_PK, true );
		adicCampo( txtDescHist, 80, 20, 330, 20, "DescHist", "Descrição do histórico", ListaCampos.DB_SI, true );
		adicDBLiv( txaHistPad, "TxaHistPad", "Corpo", false );
		setListaCampos( true, "HISTPAD", "FN" );

		adic( new JLabelPad( "Campos de dados" ), 7, 40, 223, 20 );
		adic( cbCamposDin, 7, 60, 223, 20 );

		pinCab.add( panelCampos, BorderLayout.NORTH );
		pinCab.add( spnHist, BorderLayout.CENTER );

		this.add( pinCab );

	}

	private void montaCombos() {

		Vector<String> vLabs = new Vector<String>();
		vLabs.addElement( "<--Selecione-->" );
		vLabs.addElement( "Portador" );
		vLabs.addElement( "Valor" );
		vLabs.addElement( "Número do documento" );
		vLabs.addElement( "Data" );
		vLabs.addElement( "Histórico digitado" );

		Vector<String> vVals = new Vector<String>();
		vVals.addElement( "" );
		vVals.addElement( "<PORTADOR>" );
		vVals.addElement( "<VALOR>" );
		vVals.addElement( "<DOCUMENTO>" );
		vVals.addElement( "<DATA>" );
		vVals.addElement( "<HISTORICO>" );

		cbCamposDin = new JComboBoxPad( vLabs, vVals, JComboBoxPad.TP_STRING, 20, 0 );

	}

	public void valorAlterado( JComboBoxEvent evt ) {

		if ( evt.getComboBoxPad() == cbCamposDin ) {

			txaHistPad.insert( cbCamposDin.getVlrString(), txaHistPad.getCaretPosition() );
		}

	}

}
