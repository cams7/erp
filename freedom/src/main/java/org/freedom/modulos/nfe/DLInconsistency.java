/*
 * Projeto: Freedom Pacote: org.freedom.modulos.nfe Classe: @(#)DLInconsistency.java
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR> modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR> na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR> sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR> Veja a Licença Pública Geral GNU para maiores detalhes. <BR> Você deve ter recebido uma cópia da Licença Pública
 * Geral GNU junto com este programa, se não, <BR> escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA <BR> <BR>
 */
package org.freedom.modulos.nfe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;

import org.freedom.bmps.Icone;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.dialog.FDialogo;
import org.freedom.modules.nfe.bean.NFEInconsistency;

/**
 * Dialogo para exibição das inconsistências.
 * 
 * @author Setpoint Informática Ltda./Alex Rodrigues
 * @version 13/07/2009
 */
public class DLInconsistency extends FDialogo implements MouseListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad panelList = new JPanelPad( new BorderLayout() );

	private JLabelPad grupo = new JLabelPad( "    Inconsistências da nota fiscal" );

	private JTablePad tab = new JTablePad();

	private JPanelPad panelTextos = new JPanelPad( new GridLayout( 2, 1 ) );

	private JTextAreaPad descricao = new JTextAreaPad();

	private JTextAreaPad acaoCorretiva = new JTextAreaPad();

	private ImageIcon error = Icone.novo( "clVencido2.gif" );

	private List<NFEInconsistency> inconsistencyList;

	private enum Columns {
		ICON, DESCRIPTION, FIELD, VALUE_FIELD
	}

	public DLInconsistency( List<NFEInconsistency> inconsistencyList ) {

		super();
		setTitulo( "", this.getClass().getName() );
		setAtribos( 700, 500 );

		this.inconsistencyList = inconsistencyList;

		c.setLayout( new BorderLayout() );
		c.add( panelList, BorderLayout.CENTER );
		c.add( adicBotaoSair(), BorderLayout.SOUTH );

		grupo.setPreferredSize( new Dimension( 700, 30 ) );
		grupo.setForeground( Color.BLUE );
		panelList.add( grupo, BorderLayout.NORTH );

		tab.adicColuna( "" );
		tab.adicColuna( "inconsistência / mensagem" );
		tab.adicColuna( "campo" );
		tab.adicColuna( "valor" );

		tab.setTamColuna( 25, Columns.ICON.ordinal() );
		tab.setTamColuna( 420, Columns.DESCRIPTION.ordinal() );
		tab.setTamColuna( 120, Columns.FIELD.ordinal() );
		tab.setTamColuna( 120, Columns.VALUE_FIELD.ordinal() );

		panelList.add( new JScrollPane( tab ), BorderLayout.CENTER );

		descricao.setAtivo( false );
		descricao.setBorder( BorderFactory.createTitledBorder( "Descrição : " ) );
		panelTextos.add( descricao );

		acaoCorretiva.setAtivo( false );
		acaoCorretiva.setBorder( BorderFactory.createTitledBorder( "Ação corretiva / Observações : " ) );
		panelTextos.add( acaoCorretiva );

		panelTextos.setPreferredSize( new Dimension( 700, 200 ) );
		panelList.add( panelTextos, BorderLayout.SOUTH );

		tab.addMouseListener( this );

		listInconsistencys();
	}

	private void listInconsistencys() {

		int row = 0;
		for ( NFEInconsistency inconsistency : inconsistencyList ) {

			tab.adicLinha();

			tab.setValor( error, row, Columns.ICON.ordinal() );
			tab.setValor( inconsistency.getDescription(), row, Columns.DESCRIPTION.ordinal() );
			tab.setValor( inconsistency.getField(), row, Columns.FIELD.ordinal() );
			tab.setValor( inconsistency.getValueField(), row, Columns.VALUE_FIELD.ordinal() );

			row++;
		}
	}

	private void selecao() {

		NFEInconsistency inconsistency = inconsistencyList.get( tab.getLinhaSel() );
		descricao.setVlrString( inconsistency.getDescription() );
		acaoCorretiva.setVlrString( inconsistency.getCorrectiveAction() );
	}

	public void mouseClicked( MouseEvent e ) {

		if ( tab.equals( e.getSource() ) && tab.getLinhaSel() >= 0 ) {
			selecao();
		}
	}

	public void mouseEntered( MouseEvent e ) {

		if ( tab.equals( e.getSource() ) && tab.getLinhaSel() >= 0 ) {
			selecao();
		}
	}

	public void mouseExited( MouseEvent e ) {

	}

	public void mousePressed( MouseEvent e ) {

	}

	public void mouseReleased( MouseEvent e ) {

	}
}
