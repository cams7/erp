/**
 * @version 05/06/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.telas <BR>
 * Classe:
 * @(#)FFDialogo.java <BR>
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
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrincipal;
import org.freedom.library.swing.frame.IFilho;

public class FFDialogo extends JDialog implements ActionListener, KeyListener, IFilho {

	private static final long serialVersionUID = 1L;

	protected FPrincipal fPrim;

	private boolean comScroll = false;

	private Component firstFocus = null;

	private boolean initFirstFocus = true;

	public DbConnection con = null;

	public JButtonPad btCancel = new JButtonPad("Cancelar", Icone.novo("btCancelar.png"));

	public JButtonPad btOK = new JButtonPad("OK", Icone.novo("btOk.png"));

	private JPanelPad pnBox = new JPanelPad(JPanelPad.TP_JPANEL);

	public JPanelPad pnRodape = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());

	protected JPanelPad pnBotoes = new JPanelPad(JPanelPad.TP_JPANEL, new FlowLayout(FlowLayout.CENTER, 3, 3));

	protected JPanelPad pnGrid = new JPanelPad(JPanelPad.TP_JPANEL, new GridLayout(1, 2));

	private boolean fechaJanela = true;

	protected JPanelPad pnBordRodape = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());

	protected JPanelPad pin = new JPanelPad();

	public Container c = getContentPane();

	private Border br = BorderFactory.createEtchedBorder();

	protected Component cPai = null;

	boolean setArea = true;

	boolean bUltimo = false;
	
	protected JPanelPad pnPrincipal = new JPanelPad(JPanelPad.TP_JPANEL);

	private BorderLayout blPrincipal = new BorderLayout();

	private JScrollPane spPrincipal = new JScrollPane(pnPrincipal, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	public boolean OK = false;

	public boolean bAguardando = false;

	public JComponent pnPai = null;

	public FFDialogo() {
		this(Aplicativo.telaPrincipal, true);
		setLocationRelativeTo(Aplicativo.telaPrincipal);
	}

	public FFDialogo(Component cOrig) {
		this(cOrig, false);
	}

	public FFDialogo(Component cOrig, boolean comScroll) {
		
		this(cOrig instanceof JFrame ? ( JFrame ) cOrig : Aplicativo.telaPrincipal, true, comScroll);

		cPai = cOrig;

		// Gambs para tornar o form uma modal:

		if (cOrig instanceof JFrame)
			pnPai = ( ( JFrame ) cOrig ).getRootPane();
		else if (cOrig instanceof JDialog)
			pnPai = ( ( JDialog ) cOrig ).getRootPane();
		else if (cOrig instanceof JInternalFrame)
			pnPai = ( ( JInternalFrame ) cOrig ).getDesktopPane();
		else {
			pnPai = JOptionPane.getDesktopPaneForComponent(cOrig);
			if (pnPai == null) {
				Window win = SwingUtilities.getWindowAncestor(cOrig);
				if (win instanceof JDialog)
					pnPai = ( ( JDialog ) win ).getRootPane();
				else if (win instanceof JFrame)
					pnPai = ( ( JFrame ) win ).getRootPane();
			}
		}
		
	}

	public FFDialogo(Frame fOrig, boolean bModal ) {
		this(fOrig, bModal, false);
	}
	
	public FFDialogo(Frame fOrig, boolean bModal, boolean comScroll) {
		super(fOrig, "Dialogo", bModal);
		setComScroll(comScroll);
/*		if (comScroll) {
			spPrincipal = new JScrollPane(pin, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		}
	*/	
		if (pnPai == null) {
			pnPai = Aplicativo.telaPrincipal.dpArea;
		}
		setTitle("Dialogo");
		setAtribos(50, 50, 500, 300);
		c.setLayout(new BorderLayout());

		pnPrincipal.setLayout(blPrincipal);
		
		// pnGrid.setPreferredSize(new Dimension(220,30));
		btOK.setPreferredSize(new Dimension(110, 30));
		btCancel.setPreferredSize(new Dimension(110, 30));
		pnGrid.add(btOK);
		pnGrid.add(btCancel);
		pnBotoes.add(pnGrid);

		pnBox.add(pnBotoes);

		pnRodape.add(pnBox, BorderLayout.EAST);

		// pnBordRodape.setPreferredSize(new Dimension(250,30));
		pnBordRodape.setBorder(br);
		pnBordRodape.add(pnRodape);

		c.add(pnBordRodape, BorderLayout.SOUTH);

		btCancel.addActionListener(this);
		btCancel.addKeyListener(this);
		btOK.addActionListener(this);
		btOK.addKeyListener(this);
		addKeyListener(this);
	}
	
	public void setPrimeiroFoco(final JComponent comp) {
		addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent wevt) {
				comp.requestFocusInWindow();
			}
		});
	}

	public void setPainel(JPanelPad p) {
		pin = p;
		setArea = false;
	}

	public void setPanel(JComponent p) {
		c.add(p, BorderLayout.CENTER);
		setArea = false;
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == btOK)
			ok();
		else if (evt.getSource() == btCancel)
			cancel();
	}

	public void ok() {
		if (fechaJanela) {
			OK = true;
			setVisible(false);
		}
	}

	public void cancel() {
		OK = false;
		setVisible(false);
	}

	public void setFechaJanela(boolean fecha) {
		this.fechaJanela = fecha;
	}

	public boolean getFechaJanela() {
		return this.fechaJanela;
	}

	public void setTitulo(String tit) {
		setTitulo(tit, this.getClass().getName());

	}

	public void setTitulo(String tit, String name) {
		if (getName() == null) {
			setName(name);
		}
		setTitle(tit);
	}

	public void setAtribos(int X, int Y, int Larg, int Alt) {
		setBounds(X, Y, Larg, Alt);
	}

	public void setAtribos(int Larg, int Alt) {
		setBounds(( pnPai.getSize().width - Larg ) / 2, ( pnPai.getSize().height - Alt ) / 2, Larg, Alt);
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
		if (comScroll) {
			pnPrincipal.add(pin);
			pnPrincipal.setPreferredSize(new Dimension(( int ) getSize().getWidth(), ( int ) getSize().getHeight()));
			c.add(spPrincipal, BorderLayout.CENTER);
		} else {
			c.add(pin, BorderLayout.CENTER);
		}
		setArea = false;
	}

	public void adic(Component comp, int X, int Y, int Larg, int Alt) {
		if (setArea)
			setAreaComp();
		comp.addKeyListener(this);
		pin.adic(comp, X, Y, Larg, Alt);
	}

	public JLabelPad adic(Component comp, int X, int Y, int Larg, int Alt, String label) {
		JLabelPad lbTmp = new JLabelPad(label);
		adic(lbTmp, X, Y - 20, Larg, 20);
		adic(comp, X, Y, Larg, Alt);
		return lbTmp;

	}


	public void adicInvisivel(Component comp) {
		comp.addKeyListener(this);
	}

	public void keyPressed(KeyEvent kevt) {
		if (( bUltimo ) & ( kevt.getKeyCode() == KeyEvent.VK_ENTER ) & ( btOK.isEnabled() ))
			btOK.doClick();
		else if (kevt.getKeyCode() == KeyEvent.VK_ESCAPE)
			btCancel.doClick();
	}

	public void keyTyped(KeyEvent kevt) {
	}

	public void keyReleased(KeyEvent kevt) {
	}

	public void setTela(Container c) {
		setContentPane(c);
	}

	public Container getTela() {
		Container tela = getContentPane();
		tela.setLayout(new BorderLayout());
		return tela;
	}

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

	public void setFirstFocus(Component firstFocus) {
		this.firstFocus = firstFocus;
	}

	public void firstFocus() {
		if (( firstFocus != null ) && ( !firstFocus.isFocusOwner() ) && ( initFirstFocus )){
			SwingUtilities.invokeLater(new Runnable() {
			    public void run() {
			    	firstFocus.requestFocus();
			    }
			});
		}

		/*
		 * if (firstFocus!=null) { if (firstFocus.hasFocus())
		 * firstFocus.requestFocus(); else loadFirstFocus(); } else
		 * loadFirstFocus();
		 */
	}

	public void setConexao(DbConnection cn) {
		con = cn;
	}

	public void execShow() {
		setVisible(true);
	}

	public boolean getInitFirstFocus() {
		return initFirstFocus;
	}

	public void setInitFirstFocus(boolean initFirstFocus) {
		this.initFirstFocus = initFirstFocus;
	}

	public void setTelaPrim(FPrincipal fP) {
		fPrim = fP;
	}

	public void dispose() {
		System.gc();
		super.dispose();
	}

	protected JRootPane createRootPane() {

		// Definindo o ActionListener

		ActionListener actionListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				setVisible(false);

			}

		};

		// Definindo o KeyStroke

		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);

		// Criando uma instancia de JRootPane

		JRootPane rootPane = new JRootPane();

		// Registrando o KeyStroke enquanto o JDialog estiver em foco

		rootPane.registerKeyboardAction( actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW );

		// Retornando o novo e modificado JRootPane

		return rootPane;

	}

	public boolean isComScroll() {
		return comScroll;
	}

	public void setComScroll(boolean comScroll) {
		this.comScroll = comScroll;
	}


}