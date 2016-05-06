/**
 * @version 10/08/2003 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.relatorios <BR>
 *         Classe: @(#)DLExibePizza.java <BR>
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
 *         Tela de exibição de graficos, permite a utilização dos gráficos pizza 3D rotatórios.
 * 
 */

package org.freedom.layout.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.frame.FFilho;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

public class DLExibePizza extends FFilho implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pnCli = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	private JPanelPad pnCab = new JPanelPad( JPanelPad.TP_JPANEL, new FlowLayout( FlowLayout.CENTER ) );

	private JPanelPad pnRod = new JPanelPad( JPanelPad.TP_JPANEL, new FlowLayout( FlowLayout.CENTER ) );

	public DLExibePizza( JFreeChart ch, int larg, int alt, String sTitulo, String sVlr ) {

		super( false );
		setAtribos( 0, 0, larg, alt );
		setTitulo( "Visualização de gráfico" );
		ChartPanel chartPanel = new ChartPanel( ch );
		Container c = getContentPane();
		pnCli.add( chartPanel );

		pnCab.setPreferredSize( new Dimension( 100, 20 ) );
		pnCab.add( new JLabelPad( sTitulo ) );
		pnCab.setBackground( new Color( 255, 255, 255 ) );

		pnRod.setPreferredSize( new Dimension( 100, 20 ) );
		pnRod.add( new JLabelPad( sVlr ) );
		pnRod.setBackground( new Color( 255, 255, 255 ) );

		c.add( pnCab, BorderLayout.NORTH );
		c.add( pnCli, BorderLayout.CENTER );
		c.add( pnRod, BorderLayout.SOUTH );

	}

	public void actionPerformed( ActionEvent evt ) {

	}
}
