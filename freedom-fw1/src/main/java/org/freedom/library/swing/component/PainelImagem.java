/**
 * @version 23/01/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 *
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.componentes <BR>
 * Classe: @(#)PainelImagem.java <BR>
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

package org.freedom.library.swing.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

import org.freedom.library.component.DadosImagem;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.FZoom;

public class PainelImagem extends JPanelPad implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;
	public static final int TP_NONE = -1;
	public static final int TP_BYTES = 11;
	private Image imImagem = null;
	private FileInputStream fisImagem = null;
	private byte[] bImagem = null;
	private ListaCampos lcPainel = null;
	private File fImagem = null;
	private DadosImagem diImagem;
	private Component cOrigem = null;
	public int iTipo = -1;
	public int iTam = 0;
	private boolean bAlt = false;
	private boolean bNulo = true;
	private JPopupMenu pm = new JPopupMenu();
	private JMenuItem selecMI = new JMenuItem();
	private JMenuItem excluirMI = new JMenuItem();
	private JMenuItem zoomMI = new JMenuItem();
	private JFrame jfPai = null;
	private int iDescBordA = 0;
	private int iDescBordL = 0;
	private int iPercAtual = 100;
	private int iMaxSize = 65000;
	private boolean bStretch = false;
	private boolean bEncaixa = true;
	private boolean bZoom = true;
	private boolean bMudaTamanho = false;
	private int L = 0;
	private int A = 0;

	public boolean ehNulo() {
		return bNulo;
	}

	public PainelImagem(int iTam) {
		this(null, iTam);
	}

	public PainelImagem(Component cOwner, int iTam) {
		this.iTam = iTam;
		this.iMaxSize = iTam;
		if (cOwner != null) {
			cOrigem = cOwner;
			bMudaTamanho = true;
		}
		else {
			cOrigem = this;
		}
		setBackground(Color.white);

		selecMI.setText("Selecionar Arquivo");
		selecMI.setMnemonic('S');
		selecMI.addActionListener(this);

		excluirMI.setText("Excluir");
		excluirMI.setMnemonic('E');
		excluirMI.addActionListener(this);

		zoomMI.setText("Zoom");
		zoomMI.setMnemonic('Z');
		zoomMI.addActionListener(this);

		pm.add(selecMI);
		pm.add(excluirMI);
		pm.add(zoomMI);

		setBorder(javax.swing.BorderFactory.createEtchedBorder());
		addMouseListener(this);
	}

	public void setMaxSize(int iVal) {
		iMaxSize = iVal;
	}

	public int getMaxSize() {
		return iMaxSize;
	}

	public int getZoom() {
		return iPercAtual;
	}

	public void setStretch(boolean b) {
		bStretch = b;
		bZoom = false;
	}

	public boolean getStretch() {
		return bStretch;
	}

	public void setEncaixa(boolean b) {
		bEncaixa = b;
		bZoom = false;
	}

	public boolean getEncaixa() {
		return bEncaixa;
	}

	public int getAltura() {
		int iRetorno = 0;
		if (imImagem != null)
			iRetorno = imImagem.getHeight(this);
		return iRetorno;
	}

	public int getLargura() {
		int iRetorno = 0;
		if (imImagem != null)
			iRetorno = imImagem.getWidth(this);
		return iRetorno;
	}

	public void setBorder(Border bor) {
		iDescBordA = 2;
		iDescBordL = 2;
		super.setBorder(bor);
	}

	public void setListaCampos(ListaCampos lc) {
		lcPainel = lc;
	}

	public void setVlrBytes(Image imF) {
		imImagem = imF;
		if (lcPainel != null) {
			lcPainel.edit();
		}
		bZoom = false;
	}

	public void setVlrBytes(byte[] bVals) {
		bImagem = bVals;
		imImagem = ( new ImageIcon(bImagem) ).getImage();
		if (diImagem == null) {
			diImagem = new DadosImagem(fisImagem, 0, bVals);
		}
		else {
			diImagem.setBytes(bVals);
		}
		bAlt = false;
		bZoom = false;
		repaint();
	}

	public void setVlrBytes(InputStream is) {
		if (is == null)
			return;
		byte[] bVals = new byte[iTam];
		BufferedInputStream bi = new BufferedInputStream(is);
		try {
			bi.read(bVals, 0, bVals.length);
		}
		catch (IOException err) {
			Funcoes.mensagemErro(null, "Erro ao carregar bytes!\n" + err.getMessage());
			err.printStackTrace();
		}
		bImagem = bVals;
		imImagem = ( new ImageIcon(bImagem) ).getImage();
		if (diImagem == null) {
			diImagem = new DadosImagem(fisImagem, 0, bVals);
		}
		else {
			diImagem.setBytes(bVals);
		}
		bAlt = false;
		bZoom = false;
		repaint();
		bNulo = false;
	}

	public int[] getValsEncaixa(Image im) {
		int[] iRetorno = new int[2];
		if (im != null) {

		}
		return iRetorno;
	}

	public boolean foiAlterado() {
		return bAlt;
	}

	public DadosImagem getVlrBytes() {
		bAlt = false;
		return diImagem;
	}

	/*
	 * public void update(Graphics g) { g.drawImage(imImagem,0,0,this); //
	 * super.update(g); }
	 */

	public void paint(Graphics Screen) {
		super.paint(Screen);
		if (imImagem != null) {
			if (bZoom) {
				paintZoom(Screen);
			}
			else {
				if (getStretch()) {
					bZoom = false;
					Screen.drawImage(imImagem, iDescBordL, iDescBordA, ( ( int ) cOrigem.getSize().getWidth() ) - ( iDescBordL + 1 ), ( ( int ) cOrigem.getSize().getHeight() ) - ( iDescBordA + 1 ),
							this);
				}
				else {
					if (getEncaixa()) {
						setZoom(getPercEncaixa());
						// System.out.println(getPercEncaixa());
						if (( L > A ) | ( L == A ))
							L = L > ( ( int ) cOrigem.getSize().getWidth() ) ? L - ( L - ( ( int ) cOrigem.getSize().getWidth() ) ) : L + ( ( ( int ) cOrigem.getSize().getWidth() ) - L );
						else if (A > L)
							A = A > ( ( int ) cOrigem.getSize().getHeight() ) ? A - ( A - ( ( int ) cOrigem.getSize().getHeight() ) ) : A + ( ( ( int ) cOrigem.getSize().getHeight() ) - A );
						showZoom(Screen);
					}
					else {
						bZoom = false;
						Screen.drawImage(imImagem, 0, 0, this);
						if (bMudaTamanho) {
							setPreferredSize(new Dimension(imImagem.getWidth(this), imImagem.getHeight(this)));
						}
					}
				}
			}
		}
	}

	public void setZoom(int iZ) {
		bZoom = true;
		iPercAtual = iZ;
		L = ( imImagem.getWidth(this) * iZ ) / 100;
		A = ( imImagem.getHeight(this) * iZ ) / 100;

	}

	public void showZoom(Graphics Screen) {
		Screen.drawImage(imImagem, iDescBordA, iDescBordA, L - ( iDescBordL + 1 ), A - ( iDescBordA + 1 ), this);
		if (bMudaTamanho) {
			setPreferredSize(new Dimension(L, A));
		}
	}

	public void paintZoom(Graphics Screen) {
		Screen.drawImage(imImagem, iDescBordA, iDescBordA, L - ( iDescBordL + 1 ), A - ( iDescBordA + 1 ), this);
		if (bMudaTamanho)
			setPreferredSize(new Dimension(L, A));
	}

	public int getPercEncaixa() {
		int iLargImagem = imImagem.getWidth(this);
		int iAltImagem = imImagem.getHeight(this);
		int iLargPainel = ( int ) cOrigem.getSize().getWidth();
		int iAltPainel = ( int ) cOrigem.getSize().getHeight();
		int iRetorno = 0;
		if (( iLargImagem > iAltImagem ) | ( iLargImagem == iAltImagem )) {
			if (iLargImagem > iLargPainel) {
				iRetorno = 100 - ( int ) ( ( ( double ) iLargImagem - iLargPainel ) * ( ( double ) 100 / iLargImagem ) );
			}
			else if (iLargImagem < iLargPainel) {
				iRetorno = 100 + ( int ) ( ( ( double ) iLargPainel - iLargImagem ) * ( ( double ) 100 / iLargImagem ) );
				// System.out.println("Retorno: "+iRetorno);
			}
			else if (iLargImagem == iLargPainel) {
				iRetorno = 100;
			}
		}
		else if (iAltImagem > iLargImagem) {
			if (iLargImagem > iLargPainel) {
				iRetorno = 100 - ( int ) ( ( ( double ) iAltImagem - iAltPainel ) * ( ( double ) 100 / iAltImagem ) );
			}
			else if (iLargImagem < iLargPainel) {
				iRetorno = 100 + ( int ) ( ( ( double ) iAltPainel - iAltImagem ) * ( ( double ) 100 / iAltImagem ) );
			}
			else if (iAltImagem == iAltPainel) {
				iRetorno = 100;
			}
		}
		return iRetorno;
	}

	private void excluir() {
		setVlrBytes(( new ImageIcon(new byte[0]) ).getImage());
		repaint();
	}

	private void selec() {
		// byte[] bRetorno = null;
		boolean bEncont = false;
		boolean bCancel = false;
		Component comp = null;
		String sFileName = "";
		FileDialog fdImagem = null;
		comp = getParent();
		while (true) {
			if (comp.getParent() == null) {
				break;
			}
			comp = comp.getParent();
			if (comp instanceof JFrame) {
				bEncont = true;
				break;
			}
		}
		if (bEncont) {
			jfPai = ( JFrame ) comp;
			// Funcoes.mensagemErro(null,"Encontrou!!:"+jfPai.getName());
		}
		if (jfPai != null) {
			try {
				fdImagem = new FileDialog(jfPai, "Abrir Imagem");
				fdImagem.setVisible(true);
				if (fdImagem.getFile() == null) {
					bCancel = true;
				}
				if (!bCancel) {
					sFileName = fdImagem.getDirectory() + fdImagem.getFile();
					// Funcoes.mensagemInforma(null,"Arquivo: "+sFileName);
					fImagem = new File(sFileName);
					if (( int ) fImagem.length() > iMaxSize) {
						Funcoes.mensagemErro(null, "O Tamanho do arquivo ultrapassa o limite máximo.\n" + "Tamanho máximo permitido: " + iMaxSize + " bytes.\n" + "Tamanho do arquivo: "
								+ fImagem.length() + " bytes.");
						bCancel = true;
						bNulo = true;
					}
					else {
						bNulo = false;
						fisImagem = new FileInputStream(fImagem);
						imImagem = ( new ImageIcon(sFileName) ).getImage();
						diImagem = new DadosImagem(fisImagem, ( int ) fImagem.length(), null);
					}
				}
			}
			catch (IOException err) {
				Funcoes.mensagemErro(null, "Erro ao carregar o arquivo de imagem!\n" + err.getMessage());
			}
		}
		if (!bCancel) {
			bAlt = true;
			setVlrBytes(imImagem);
			repaint();
		}
	}

	private void zoom() {
		FZoom zoom = null;
		if (imImagem != null) {
			zoom = new FZoom(imImagem, iTam);
			zoom.setVisible(true);
		}
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == excluirMI)
			excluir();
		else if (evt.getSource() == selecMI)
			selec();
		else if (evt.getSource() == zoomMI)
			zoom();
	}

	public void mousePressed(MouseEvent mevt) {
		if (mevt.getModifiers() == InputEvent.BUTTON3_MASK)
			pm.show(this, mevt.getX(), mevt.getY());
	}

	public void mouseClicked(MouseEvent mevt) {
	}

	public void mouseEntered(MouseEvent mevt) {
	}

	public void mouseExited(MouseEvent mevt) {
	}

	public void mouseReleased(MouseEvent mevt) {
	}
}
