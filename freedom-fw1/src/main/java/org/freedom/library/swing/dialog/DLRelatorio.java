/**
 * @version 05/06/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 *
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.telas <BR>
 * Classe: @(#)DLRelatorio.java <BR>
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

package org.freedom.library.swing.dialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Container;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JDialog;

import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.type.TYPE_PRINT;

import javax.swing.BorderFactory;

import org.freedom.bmps.Icone;

public abstract class DLRelatorio extends JDialog implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	private JPanelPad pinCli = new JPanelPad(350, 170);
	private JPanelPad pnRod = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());
	private JPanelPad pnCentRod = new JPanelPad(JPanelPad.TP_JPANEL, new FlowLayout(FlowLayout.CENTER, 0, 0));
	private JPanelPad pnBotoes = new JPanelPad(JPanelPad.TP_JPANEL, new GridLayout(1, 2));
	private JButtonPad btImp = new JButtonPad(Icone.novo("btImprime.png"));
	private JButtonPad btPrevimp = new JButtonPad(Icone.novo("btPrevimp.png"));
	private JButtonPad btSair = new JButtonPad("Sair", Icone.novo("btSair.png"));
	boolean bSetArea = true;
	boolean bCtrl = false;
	public Container c = null;

	public DLRelatorio() {
		setTitulo("Requisiçao de Relatório");
		setAtribos(100, 100, 350, 200);
		c = getContentPane();
		c.setLayout(new BorderLayout());
		pnRod.setBorder(BorderFactory.createEtchedBorder());
		c.add(pnRod, BorderLayout.SOUTH);
		c.add(pinCli, BorderLayout.CENTER);
		pnRod.setPreferredSize(new Dimension(350, 32));
		btSair.setPreferredSize(new Dimension(100, 30));
		pnRod.add(btSair, BorderLayout.EAST);
		pnRod.add(pnCentRod, BorderLayout.CENTER);
		pnBotoes.setPreferredSize(new Dimension(80, 28));
		pnCentRod.add(pnBotoes);
		pnBotoes.add(btImp);
		pnBotoes.add(btPrevimp);

		btImp.setToolTipText("Imprimir (Ctrl + I)");
		btPrevimp.setToolTipText("Previsão da impressão (Ctrl + P)");
		btSair.setToolTipText("Sair (ESC)");

		btImp.addActionListener(this);
		btPrevimp.addActionListener(this);
		btSair.addActionListener(this);
		btImp.addKeyListener(this);
		btPrevimp.addKeyListener(this);
		btSair.addKeyListener(this);
	}

	public void setPainel(JPanelPad pin) {
		pinCli = pin;
		bSetArea = false;
	}

	public void setAreaComp() {
		pinCli = new JPanelPad(( int ) getSize().getWidth() - 10, ( int ) getSize().getHeight() - 45);
		c.add(pinCli, BorderLayout.CENTER);
		bSetArea = false;
	}

	public void adic(Component comp, int iX, int iY, int iLarg, int iAlt) {
		if (bSetArea)
			setAreaComp();
		comp.addKeyListener(this);
		pinCli.adic(comp, iX, iY, iLarg, iAlt);
	}

	public abstract void imprimir(TYPE_PRINT b);

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == btSair) {
			dispose();
		}
		else if (evt.getSource() == btImp) {
			imprimir(TYPE_PRINT.PRINT);
		}
		else if (evt.getSource() == btPrevimp) {
			imprimir(TYPE_PRINT.VIEW);
		}
	}

	public void keyPressed(KeyEvent kevt) {
		if (kevt.getKeyCode() == KeyEvent.VK_CONTROL) {
			bCtrl = true;
		}
		else if (kevt.getKeyCode() == KeyEvent.VK_I) {
			if (bCtrl)
				btImp.doClick();
		}
		else if (kevt.getKeyCode() == KeyEvent.VK_P) {
			if (bCtrl)
				btPrevimp.doClick();
		}
		else if (kevt.getKeyCode() == KeyEvent.VK_ESCAPE) {
			btSair.doClick();
		}
	}

	public void keyReleased(KeyEvent kevt) {
		if (kevt.getKeyCode() == KeyEvent.VK_CONTROL) {
			bCtrl = false;
		}
	}

	public void keyTyped(KeyEvent kevt) {
	}

	public void setTitulo(String sVal) {
		setTitle(sVal);
	}

	public void setAtribos(int iX, int iY, int iLarg, int iAlt) {
		setBounds(iX, iY, iLarg, iAlt);
	}

	public void setAtribos(int Larg, int Alt) {
		setSize(Larg, Alt);
		setLocationRelativeTo(this);
	}
}
