/**
 * @version 05/06/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 *
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.telas <BR>
 * Classe: @(#)FAndamento.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA <BR> <BR>
 *
 * Comentários da classe.....
 */

package org.freedom.library.swing.frame;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;

public class FAndamento extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanelPad pin = new JPanelPad(310, 150);
	private JProgressBar pb = new JProgressBar();
	private JLabelPad lbAnd = new JLabelPad("");

	public FAndamento(String sLabel, int iMin, int iMax) {
		setBounds(100, 100, 310, 150);
		pb.setStringPainted(true);
		pb.setMaximum(iMax);
		pb.setMinimum(iMin);
		setTitle("Andamento");
		getContentPane().setLayout(new GridLayout(1, 1));
		lbAnd.setText(sLabel);
		pin.adic(lbAnd, 7, 20, 200, 20);
		pin.adic(pb, 7, 60, 280, 20);
		getContentPane().add(pin);
	}

	public synchronized void atualiza(int iVal) {
		pb.setValue(iVal);
		pb.updateUI();
	}

	public void setLabel(String sLabel) {
		lbAnd.setText(sLabel);
		lbAnd.updateUI();
	}
}
