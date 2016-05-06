/**
 * @version 05/06/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 *
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.telas <BR>
 * Classe: @(#)DLPrinterJob.java <BR>
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;

import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.freedom.bmps.Icone;
import org.freedom.bmps.Imagem;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.ImprimeLayout;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldPad;

public class DLPrinterJob extends FFDialogo implements ActionListener, KeyListener {
	private static final long serialVersionUID = 1L;

	private JPanelPad pnCli = new JPanelPad(JPanelPad.TP_JPANEL, new GridLayout(1, 1));
	private JPanelPad pnCab = new JPanelPad(JPanelPad.TP_JPANEL, new FlowLayout(FlowLayout.CENTER, 0, 0));
	private ImprimeLayout impLay = new ImprimeLayout();
	private JButtonPad btSair = new JButtonPad("Sair", Icone.novo("btSair.png"));
	private JButtonPad btImp = new JButtonPad("Imprimir", Icone.novo("btImprime.png"));
	private JButtonPad btProx = new JButtonPad(Icone.novo("btProx.png"));
	private JButtonPad btAnt = new JButtonPad(Icone.novo("btAnt.png"));
	private JButtonPad btPrim = new JButtonPad(Icone.novo("btPrim.png"));
	private JButtonPad btUlt = new JButtonPad(Icone.novo("btUlt.png"));
	private JLabelPad lbPag = new JLabelPad("1 de 1");

	private JPanelPad pinCab = new JPanelPad(232, 45);
	private JScrollPane spn = new JScrollPane();
	private JButtonPad btZoom100 = new JButtonPad(Icone.novo("btZoom100.gif"));
	private JButtonPad btZoomIn = new JButtonPad(Icone.novo("btZoomIn.gif"));
	private JButtonPad btZoomPag = new JButtonPad(Icone.novo("btZoomPag.gif"));
	private JTextFieldPad txtZoom = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 3, 0);
	private JButtonPad btMais = new JButtonPad(Icone.novo("btZoomMais.gif"));
	private JButtonPad btMenos = new JButtonPad(Icone.novo("btZoomMenos.gif"));
	private PageFormat pag = null;

	public String strTemp = "";
	private int iZoomAtual = 100;
	boolean bVisualiza = false;

	public DLPrinterJob(ImprimeLayout impL, Component cOrig) {
		super(cOrig);
		impLay = impL;

		setTitulo("Visualizar Impressão Gráfica");
		setAtribos(pnPai.getSize().width, pnPai.getSize().height);

		txtZoom.setEnterSai(false);

		c.add(pnCab, BorderLayout.NORTH);
		btSair.setPreferredSize(new Dimension(80, 30));
		pnCab.add(pinCab);
		pinCab.adic(btZoom100, 7, 5, 30, 30);
		pinCab.adic(btZoomIn, 40, 5, 30, 30);
		pinCab.adic(btZoomPag, 73, 5, 30, 30);
		pinCab.adic(txtZoom, 106, 5, 50, 30);
		pinCab.adic(btMais, 159, 5, 30, 30);
		pinCab.adic(btMenos, 192, 5, 30, 30);

		// monta a area de visualização:

		spn.setViewportView(impLay);
		pnCli.add(spn);

		c.add(pnCli, BorderLayout.CENTER);

		// desmonta tela antiga de dialogo:

		c.remove(0);

		// monta tela atual:

		JPanelPad pnRod = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());
		JPanelPad pnFCenter = new JPanelPad(JPanelPad.TP_JPANEL, new FlowLayout(FlowLayout.CENTER, 0, 0));

		JPanelPad pnDir = new JPanelPad(JPanelPad.TP_JPANEL, new GridLayout(1, 2));
		pnDir.add(btImp);
		pnDir.add(btSair);
		pnRod.add(pnDir, BorderLayout.EAST);

		JPanelPad pnCenter = new JPanelPad(JPanelPad.TP_JPANEL, new GridLayout(1, 5));

		pnFCenter.add(pnCenter);

		pnCenter.add(btPrim);
		pnCenter.add(btAnt);
		pnCenter.add(lbPag);
		pnCenter.add(btProx);
		pnCenter.add(btUlt);
		pnRod.add(pnFCenter, BorderLayout.CENTER);

		c.add(pnRod, BorderLayout.SOUTH);

		// Configura os Listeners e Componentes

		lbPag.setHorizontalAlignment(SwingConstants.CENTER);
		btSair.addActionListener(this);
		btImp.addActionListener(this);
		btProx.addActionListener(this);
		btAnt.addActionListener(this);
		btPrim.addActionListener(this);
		btUlt.addActionListener(this);

		btZoom100.addActionListener(this);
		btZoomIn.addActionListener(this);
		btZoomPag.addActionListener(this);
		btMais.addActionListener(this);
		btMenos.addActionListener(this);
		btSair.addActionListener(this);
		txtZoom.addKeyListener(this);

		impLay.montaG();
		impLay.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent mevt) {
				if (mevt.getButton() == 1) {
					if (iZoomAtual < 990) {
						setZoom(iZoomAtual + 10);
						impLay.repaint();
					}
				}
				else {
					if (iZoomAtual > 10) {
						setZoom(iZoomAtual - 10);
						impLay.repaint();
					}
				}
				txtZoom.setVlrString("" + iZoomAtual);
			}
		});

		impLay.setCursor(getToolkit().createCustomCursor(Imagem.novo("curZoom.gif"), new Point(5, 5), "Zoom"));

		upContaPag(impLay.getPagAtual(), impLay.getNumPags());
		pag = impLay.getPFPad();
		txtZoom.setVlrString("100");

		setVisible(true);
	}

	public void setEnabledBotaoImp( boolean enabledBotaoImp ) {
		btImp.setEnabled( enabledBotaoImp );
	}
	
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == btSair)
			setVisible(false);
		else if (evt.getSource() == btImp) {
			OK = impLay.imprimir(false);
		}
		else if (evt.getSource() == btAnt)
			impLay.toPagina(impLay.getPagAtual() - 1);
		else if (evt.getSource() == btProx)
			impLay.toPagina(impLay.getPagAtual() + 1);
		else if (evt.getSource() == btPrim)
			impLay.toPagina(1);
		else if (evt.getSource() == btUlt)
			impLay.toPagina(impLay.getNumPags());
		else if (evt.getSource() == btZoom100) {
			setZoom(100);
			impLay.repaint();
		}
		else if (evt.getSource() == btZoomIn) {
			setZoom(getPercEncaixaY());
			impLay.repaint();
		}
		else if (evt.getSource() == btZoomPag) {
			setZoom(getPercEncaixaX());
			impLay.repaint();
		}
		else if (evt.getSource() == btMais) {
			if (iZoomAtual < 990) {
				setZoom(iZoomAtual + 10);
				impLay.repaint();
			}
		}
		else if (evt.getSource() == btMenos) {
			if (iZoomAtual > 10) {
				setZoom(iZoomAtual - 10);
				impLay.repaint();
			}
		}
		txtZoom.setVlrString("" + iZoomAtual);

		upContaPag(impLay.getPagAtual(), impLay.getNumPags());
	}

	public void keyPressed(KeyEvent kevt) {
		if (kevt.getKeyCode() == KeyEvent.VK_ENTER) {
			if (txtZoom.getText().trim().length() == 0) {
				txtZoom.setVlrString("" + iZoomAtual);
			}
			else {
				setZoom(txtZoom.getVlrInteger().intValue());
				pnCli.updateUI();
				impLay.repaint();
			}
		}
	}

	private void setZoom(int iVal) {
		if (iVal == 0) {
			Funcoes.mensagemErro(this, "Não é poissível ajustar o zoom para 0%!");
			return;
		}

		// double dX = (double)iVal/100.0;
		double dX = iVal / 100.0;
		// double dY = (double)iVal/100.0;
		double dY = iVal / 100.0;
		Dimension dAnt = new Dimension(( int ) pag.getWidth(), ( int ) pag.getHeight());
		impLay.setEscala(dX, dY);
		dAnt.setSize(dAnt.getWidth() * dX, dAnt.getHeight() * dY);
		impLay.setPreferredSize(dAnt);
		impLay.revalidate();
		iZoomAtual = iVal;
	}

	private int getPercEncaixaX() {
		double dLargPag = pag.getWidth();
		double dLarg = impLay.getVisibleRect().getWidth();
		double dFat = ( dLarg / dLargPag );
		return ( int ) ( dFat * 100.0 );
	}

	private int getPercEncaixaY() {
		double dAltPag = pag.getHeight();
		double dAlt = impLay.getVisibleRect().getHeight();
		double dFat = ( dAlt / dAltPag );
		return ( int ) ( dFat * 100.0 );
	}

	public void upContaPag(int Atual, int Num) {
		lbPag.setText(Atual + " de " + Num);
	}

	public void keyTyped(KeyEvent kevt) {
	}

	public void keyReleased(KeyEvent kevt) {
	}
}
