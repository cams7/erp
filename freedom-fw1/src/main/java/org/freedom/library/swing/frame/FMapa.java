/**
 * @version 16/04/2009 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 *
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.telas <BR>
 * Classe: @(#)FMapa.java <BR>
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
 * Tela de exibição de mapa de endereço...
 */

package org.freedom.library.swing.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import org.freedom.bmps.Icone;
import org.freedom.infra.x.swing.Mapa;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JPanelPad;
import org.jdesktop.swingx.JXMapViewer;

public final class FMapa extends FFilho implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	private JPanelPad pnMapa = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());
	private Mapa mapa = new Mapa();
	private JButtonPad btSair = new JButtonPad("Sair", Icone.novo("btSair.png"));
	private JButtonPad btImp = new JButtonPad(Icone.novo("btImprime.png"));
	private JButtonPad btPrevimp = new JButtonPad(Icone.novo("btPrevimp.png"));
	private JPanelPad pnImp = new JPanelPad(JPanelPad.TP_JPANEL);
	private JPanelPad pnGImp = new JPanelPad(JPanelPad.TP_JPANEL);
	private FlowLayout flImp = new FlowLayout(FlowLayout.CENTER, 0, 0);
	private GridLayout glImp = new GridLayout(1, 2);
	boolean Shift = false;
	boolean Ctrl = false;
	private Image img = null;

	public FMapa(boolean comScroll) {
		super(comScroll);
		setTitulo("Mapa");

		btSair.setToolTipText("Fecha a Tela (Shift + F4)");
		btImp.setToolTipText("Imprimir (Ctrl+P)");
		btPrevimp.setToolTipText("Visualizar Impressão (Ctrl+R)");

		setAtribos(0, 0, 1000, 700);
		c.add(pnMapa);
		mapa.setZoom(2);

		// JXMapViewer pm = new JXMapViewer();
		pnMapa.add(mapa, BorderLayout.CENTER);
		img = mapa.createImage(500, 500);

		pnImp.setLayout(flImp);
		pnGImp.setLayout(glImp);
		pnGImp.setPreferredSize(new Dimension(80, 26));
		pnGImp.add(btImp);
		pnGImp.add(btPrevimp);
		pnImp.add(pnGImp);

		btSair.addActionListener(this);

		btSair.addKeyListener(this);
		btImp.addKeyListener(this);
		btPrevimp.addKeyListener(this);
		btPrevimp.addActionListener(this);

		addKeyListener(this);
		addInternalFrameListener(this);

		pnRodape.add(btSair, BorderLayout.EAST);
		pnRodape.add(pnImp, BorderLayout.WEST);
		c.add(pnBordRod, BorderLayout.SOUTH);

	}

	public void setEndereco(final String rua, final Integer numero, final String cidade, final String uf) {
		mapa.buscaEndereco(rua, numero, cidade, uf);
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			super.finalize();
		}
		finally {
			c = null;
			mapa.removeAll();
			mapa = null;
			Runtime.getRuntime().gc();
			System.gc();
		}
	}

	/*
	 * private void close() throws Throwable { try { finalize(); } catch
	 * (Exception e) { e.printStackTrace(); }
	 * 
	 * }
	 */
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand() == "Sair")
			dispose();
		if (evt.getSource() == btPrevimp) {
			imprimir(true);
		}
		else if (evt.getSource() == btImp) {
			imprimir(false);
		}

	}

	private void imprimir(boolean visualizar) {
		try {

			// Testes para verificar modo de imprimir mapa.

			JXMapViewer mp2 = mapa.getMainMap();
			// Container cnt = mp2.getParent();

			Graphics gr = mp2.getGraphics();
			gr.drawRect(10, 10, 100, 100);

			HashMap<Object, Object> hParam = new HashMap<Object, Object>();
			hParam.put("MAPA", img);

			/*
			 * FPrinterJob dlGr = new FPrinterJob( "relatorios/Mapa.jasper",
			 * "Mapa", null, this, hParam, con );
			 * 
			 * if ( visualizar == TYPE_PRINT.VIEW ) { dlGr.setVisible( true ); } else {
			 * JasperPrintManager.printReport( dlGr.getRelatorio(), true ); }
			 */
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void keyPressed(KeyEvent kevt) {
		if (kevt.getKeyCode() == KeyEvent.VK_SHIFT)
			Shift = true;
		if (kevt.getKeyCode() == KeyEvent.VK_CONTROL)
			Ctrl = true;
		if (Shift) {
			if (kevt.getKeyCode() == KeyEvent.VK_F4)
				btSair.doClick();
		}
		if (Ctrl) {
			if (kevt.getKeyCode() == KeyEvent.VK_R)
				btPrevimp.doClick();
			else if (kevt.getKeyCode() == KeyEvent.VK_P)
				btImp.doClick();
		}
	}

	public void keyReleased(KeyEvent kevt) {
		if (kevt.getKeyCode() == KeyEvent.VK_SHIFT)
			Shift = false;
		else if (kevt.getKeyCode() == KeyEvent.VK_CONTROL)
			Ctrl = false;
	}

	public void keyTyped(KeyEvent kevt) {
	}

}
