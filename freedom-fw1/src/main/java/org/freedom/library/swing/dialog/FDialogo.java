/**
 * @version 05/06/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.telas <BR>
 * Classe:
 * @(#)FDialogo.java <BR>
 * 
 * Este programa é licenciado de acordo com a LPG-PC (Licença Pública Geral para
 * Programas de Computador), <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * A LPG-PC deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste
 * Programa. <BR>
 * Caso uma cópia da LPG-PC não esteja disponível junto com este Programa, você
 * pode contatar <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa é
 * preciso estar <BR>
 * de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * Comentários da classe.....
 */

package org.freedom.library.swing.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrincipal;
import org.freedom.library.swing.frame.IFilho;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.border.Border;

import org.freedom.bmps.Icone;

public class FDialogo extends JDialog implements ActionListener, KeyListener, WindowListener, IFilho {

	private static final long serialVersionUID = 1L;

	public JButtonPad btCancel = new JButtonPad("Cancelar", Icone.novo("btCancelar.png"));

	public JButtonPad btOK = new JButtonPad("OK", Icone.novo("btOk.png"));

	public JPanelPad pnRodape = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());

	private JPanelPad pnGrid = new JPanelPad(JPanelPad.TP_JPANEL, new GridLayout(1, 2));

	private JPanelPad pnBordRodape = new JPanelPad(JPanelPad.TP_JPANEL, new GridLayout(1, 1));

	private JPanelPad pin = new JPanelPad();

	public Container c = getContentPane();

	private Border br = BorderFactory.createEtchedBorder();

	boolean setArea = true;

	boolean bUltimo = false;

	private boolean initFirstFocus = true;

	private Component firstFocus = null;

	public boolean OK = false;

	public DbConnection con = null;

	public FDialogo() {
		super(Aplicativo.telaPrincipal, "Dialogo", true);
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		setTitle("Dialogo");
		setModal(true);
		setSize(250, 100);
		setLocationRelativeTo(this);

		c.setLayout(new BorderLayout());

		pnGrid.setPreferredSize(new Dimension(220, 30));
		pnGrid.add(btOK);
		pnGrid.add(btCancel);

		pnRodape.add(pnGrid, BorderLayout.EAST);

		pnBordRodape.setPreferredSize(new Dimension(250, 30));
		pnBordRodape.setBorder(br);
		pnBordRodape.add(pnRodape);

		c.add(pnBordRodape, BorderLayout.SOUTH);

		btCancel.addActionListener(this);
		btOK.addActionListener(this);
		addKeyListener(this);

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
	}
	
	// private FPrincipal fPrim;

	public JPanelPad adicBotaoSair() {
		Container c = getContentPane();
		JButtonPad btSair = new JButtonPad("Sair", Icone.novo("btSair.png"));
		JPanelPad pnRod = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());
		pnRod.setPreferredSize(new Dimension(200, 30));
		btSair.setPreferredSize(new Dimension(110, 30));
		pnRod.add(btSair, BorderLayout.EAST);
		c.add(pnRod, BorderLayout.SOUTH);
		btSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				dispose();
			}
		});
		return pnRod;
	}

	public void dispose() {
		System.gc();
		super.dispose();
	}
	
	public void execShow() {
		setVisible(true);
	}

	public Container getTela() {
		Container tela = getContentPane();
		tela.setLayout(new BorderLayout());
		return tela;
	}

	public void setTela(Container c) {
		setContentPane(c);
	}

	public void setTelaPrim(FPrincipal fP) {
		// fPrim = fP;
	}
	
	public void setConexao(DbConnection cn) {
		con = cn;
	}

	public void setPainel(JPanelPad p) {
		pin = p;
		setArea = false;
	}

	public void setPanel(JPanelPad p) {
		c.add(p, BorderLayout.CENTER);
		setArea = false;
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == btOK) {
			OK = true;
			setVisible(false);
		}
		else if (evt.getSource() == btCancel) {
			OK = false;
			setVisible(false);
		}
	}

	public void setTitulo(String tit) {
		setTitulo(tit, this.getClass().getName());
	}

	public void setTitulo(String tit, String name) {
		setTitle(tit);
		if (getName() == null) {
			setName(name);
		}
	}

	public void setAtribos(int X, int Y, int Larg, int Alt) {
		setBounds(X, Y, Larg, Alt);
	}

	public void setAtribos(int Larg, int Alt) {
		setSize(Larg, Alt);
		setLocationRelativeTo(this);
	}

	public void setPrimeiroFoco(final JComponent comp) {
		addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent wevt) {
				comp.requestFocusInWindow();
			}
		});
	}

	public void eUltimo() {
		bUltimo = true;
	}

	public void setToFrameLayout() {
		pnRodape.remove(0);
		pnGrid = new JPanelPad(JPanelPad.TP_JPANEL, new GridLayout(1, 1));
		pnGrid.setPreferredSize(new Dimension(100, 30));
		JButtonPad btSair = new JButtonPad("Sair", Icone.novo("btSair.png"));
		btSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setVisible(false);
			}
		});
		pnGrid.add(btSair);
		pnRodape.add(pnGrid, BorderLayout.EAST);
	}

	public void setAreaComp() {
		pin = new JPanelPad(( int ) getSize().getWidth(), ( int ) getSize().getHeight());
		c.add(pin, BorderLayout.CENTER);
		setArea = false;
	}

	public void adic(Component comp, int X, int Y, int Larg, int Alt) {
		if (setArea)
			setAreaComp();
		comp.addKeyListener(this);
		pin.adic(comp, X, Y, Larg, Alt);
	}

	public void adicInvisivel(Component comp) {
		comp.addKeyListener(this);
	}

	public void keyPressed(KeyEvent kevt) {
		if (( bUltimo ) & ( kevt.getKeyCode() == KeyEvent.VK_ENTER ))
			btOK.doClick();
		else if (kevt.getKeyCode() == KeyEvent.VK_ESCAPE)
			btCancel.doClick();
	}

	public void keyTyped(KeyEvent kevt) {
	}

	public void keyReleased(KeyEvent kevt) {
	}

	public void setFirstFocus(Component firstFocus) {
		this.firstFocus = firstFocus;
	}

	public void setInitFirstFocus(boolean initFirstFocus) {
		this.initFirstFocus = initFirstFocus;
	}

	public boolean getInitFirstFocus() {
		return initFirstFocus;
	}

	public void firstFocus() {
		if (( firstFocus != null ) && ( firstFocus.hasFocus() ) && ( initFirstFocus ))
			firstFocus.requestFocus();

		/*
		 * if (firstFocus!=null) { if (firstFocus.hasFocus())
		 * firstFocus.requestFocus(); else loadFirstFocus(); } else
		 * loadFirstFocus();
		 */
	}

	public void windowActivated(WindowEvent arg0) {
		firstFocus();
	}

	public void windowClosed(WindowEvent arg0) {
	}

	public void windowClosing(WindowEvent arg0) {
	}

	public void windowDeactivated(WindowEvent arg0) {
	}

	public void windowDeiconified(WindowEvent arg0) {
	}

	public void windowIconified(WindowEvent arg0) {
	}

	public void windowOpened(WindowEvent arg0) {
		firstFocus();

	}
}