/**
 * @version 12/07/2005 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.cfg <BR>
 *         Classe: @(#)FFluxo.java <BR>
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
 * 
 */
package org.freedom.modulos.cfg.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.Vector;

import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.frame.FFilho;

public class FVisual extends FFilho {

	private static final long serialVersionUID = 1L;

	private JComboBoxPad cbLookAndFeel = null;

	private Vector<String> vDescLookAndFeel = new Vector<String>();

	private Vector<?> vValLookAndFeel = new Vector<Object>();

	private JLabelPad lbLookAndFeel = new JLabelPad("Selecione o visual desejado");

	private JPanelPad pnCli = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());

	// public FVisual(boolean comScroll) {
	public FVisual() {

		super(true);
		setTitulo("Configuração de Visual");
		setAtribos(10, 10, 300, 200);

		Container c = getContentPane();

		c.setLayout(new BorderLayout());

		c.add(pnCli, BorderLayout.CENTER);

		cbLookAndFeel = new JComboBoxPad(vDescLookAndFeel, vValLookAndFeel, JComboBoxPad.TP_STRING, 100, 0);
		pnCli.adic(lbLookAndFeel, 30, 30, 100, 20);
		pnCli.adic(cbLookAndFeel, 60, 60, 100, 20);

	}

}
