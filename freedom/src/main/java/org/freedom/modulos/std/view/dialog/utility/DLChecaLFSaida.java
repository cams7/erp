/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)DLChecaLFSaida.java <BR>
 * 
 *                         Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                         modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                         na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                         Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                         sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                         Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                         Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                         de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                         Comentários sobre a classe...
 */

package org.freedom.modulos.std.view.dialog.utility;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;

import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.dialog.DLRelatorio;
import org.freedom.library.type.TYPE_PRINT;

public class DLChecaLFSaida extends DLRelatorio {

	private static final long serialVersionUID = 1L;

	public JTablePad tab = new JTablePad();

	private JScrollPane spnTab = new JScrollPane( tab );

	private JPanelPad pnCliente = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	public DLChecaLFSaida() {

		setTitulo( "Inconsistências de Vendas" );
		setAtribos( 600, 320 );

		c.add( pnCliente, BorderLayout.CENTER );
		pnCliente.add( spnTab, BorderLayout.CENTER );
		tab.adicColuna( "Pedido" );
		tab.adicColuna( "Série" );
		tab.adicColuna( "Nota" );
		tab.adicColuna( "Emissão" );
		tab.adicColuna( "Inconsistência" );

		tab.setTamColuna( 80, 0 );
		tab.setTamColuna( 40, 1 );
		tab.setTamColuna( 80, 2 );
		tab.setTamColuna( 100, 3 );
		tab.setTamColuna( 200, 4 );

	}

	public void imprimir( TYPE_PRINT bVal ) {

		if ( bVal==TYPE_PRINT.VIEW ) {
			System.out.println( "imprimiu" );
		}
	}

};
