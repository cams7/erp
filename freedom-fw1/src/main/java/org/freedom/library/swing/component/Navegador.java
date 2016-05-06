/**
 * @version 23/01/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 *
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.componentes <BR>
 * Classe: @(#)Navegador.java <BR>
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

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Types;

import javax.swing.JPanel;

import org.freedom.bmps.Icone;
import org.freedom.library.persistence.ListaCampos;

public class Navegador extends JPanel implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	public JButtonPad btNovo = new JButtonPad(Icone.novo("btNovo.png"));
	public JButtonPad btExcluir = new JButtonPad(Icone.novo("btExcluir.png"));
	public JButtonPad btEditar = new JButtonPad(Icone.novo("btEditar.png"));
	public JButtonPad btSalvar = new JButtonPad(Icone.novo("btSalvar.png"));
	public JButtonPad btCancelar = new JButtonPad(Icone.novo("btCancelar.png"));

	public static int BT_PRIMEIRO = 0;
	public static int BT_ANTERIOR = 1;
	public static int BT_PROXIMO = 2;
	public static int BT_ULTIMO = 3;
	public static int BT_NOVO = 4;
	public static int BT_EXCLUIR = 5;
	public static int BT_EDITAR = 6;
	public static int BT_SALVAR = 7;
	public static int BT_CANCELAR = 8;

	public JButtonPad btPrim = null;
	public JButtonPad btAnt = null;
	public JButtonPad btProx = null;
	public JButtonPad btUlt = null;
	private ListaCampos lcNav = null;
	boolean Ctrl = false;
	public boolean bDet = false;
	boolean navigationOnly = false;
	boolean[] podeVer = new boolean[9];
	private boolean navigation = false;
	
	private int TIPO_PK = Types.INTEGER;

	public Navegador(boolean nav) {
		bDet = nav;

		for (int i = 0; i < 9; i++) {
			podeVer[i] = true;
		}
		btNovo.setToolTipText("Novo (Ctrl + N)");
		btExcluir.setToolTipText("Deletar (Ctrl + D)");
		btEditar.setToolTipText("Editar (Ctrl + E)");
		btSalvar.setToolTipText("Salvar (Ctrl + S)");
		btCancelar.setToolTipText("Cancelar (Ctrl + W)");

		btNovo.addActionListener(this);
		btExcluir.addActionListener(this);
		btEditar.addActionListener(this);
		btSalvar.addActionListener(this);
		btCancelar.addActionListener(this);

		setNavigation(nav);
	}

	// Feature não deve ser utilizada pois causa problemas na edição do registro
	// após status ser alterado par aditavel novamente (verificar futuramente)
	public void setReadOnly(boolean read_only) {

		try {

			btNovo.setEnabled(!read_only);
			btSalvar.setEnabled(!read_only);
			btEditar.setEnabled(!read_only);
			btCancelar.setEnabled(!read_only);
			btExcluir.setEnabled(!read_only);

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setActionDefault() {

		try {

			btNovo.setEnabled(true);
			btSalvar.setEnabled(false);
			btEditar.setEnabled(true);
			btCancelar.setEnabled(false);
			btExcluir.setEnabled(true);

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setNavigation(boolean nav) {
		setNavigation(nav, Types.INTEGER);
	}
	
	public void setNavigation(boolean nav, int tipo) {

		navigation = nav;
		TIPO_PK = tipo;
		
		
		removeAll();

		if (nav) {
			btPrim = new JButtonPad(Icone.novo("btPrim.png"));
			btAnt = new JButtonPad(Icone.novo("btAnt.png"));
			btProx = new JButtonPad(Icone.novo("btProx.png"));
			btUlt = new JButtonPad(Icone.novo("btUlt.png"));

			btPrim.setToolTipText("Primeiro (Ctrl + PageUp)");
			btAnt.setToolTipText("Anterior (PageUp)");
			btProx.setToolTipText("Próximo (PageDown)");
			btUlt.setToolTipText("Último (Ctrl + PageDow)");

			setLayout(new GridLayout(1, 9));
			setPreferredSize(new Dimension(270, 30));

			add(btPrim); // 0
			add(btAnt); // 1
			add(btProx); // 2
			add(btUlt); // 3

			btPrim.addActionListener(this);
			btAnt.addActionListener(this);
			btProx.addActionListener(this);
			btUlt.addActionListener(this);
		}
		else {
			setLayout(new GridLayout(1, 5));
			setPreferredSize(new Dimension(150, 30));

			if (btPrim != null)
				btPrim.removeActionListener(this);
			if (btAnt != null)
				btAnt.removeActionListener(this);
			if (btProx != null)
				btProx.removeActionListener(this);
			if (btUlt != null)
				btUlt.removeActionListener(this);

			btPrim = null;
			btAnt = null;
			btProx = null;
			btUlt = null;
		}

		add(btNovo); // 4
		add(btExcluir); // 5
		add(btEditar); // 6
		add(btSalvar); // 7
		add(btCancelar); // 8
	}

	public void setNavigationOnly() {
		navigationOnly = true;
		removeAll();

		btPrim = new JButtonPad(Icone.novo("btPrim.png"));
		btAnt = new JButtonPad(Icone.novo("btAnt.png"));
		btProx = new JButtonPad(Icone.novo("btProx.png"));
		btUlt = new JButtonPad(Icone.novo("btUlt.png"));

		btPrim.setToolTipText("Primeiro (Ctrl + PageUp)");
		btAnt.setToolTipText("Anterior (PageUp)");
		btProx.setToolTipText("Próximo (PageDown)");
		btUlt.setToolTipText("Último (Ctrl + PageDow)");

		setLayout(new GridLayout(1, 4));
		setPreferredSize(new Dimension(120, 30));

		add(btPrim); // 0
		add(btAnt); // 1
		add(btProx); // 2
		add(btUlt); // 3

		btCancelar.removeActionListener(this);
		btEditar.removeActionListener(this);
		btExcluir.removeActionListener(this);
		btNovo.removeActionListener(this);
		btSalvar.removeActionListener(this);

		btPrim.addActionListener(this);
		btAnt.addActionListener(this);
		btProx.addActionListener(this);
		btUlt.addActionListener(this);
	}

	public void setListaCampos(ListaCampos lc) {
		lcNav = lc;
	}

	public void keyPressed(KeyEvent kevt) {

		// Funcoes.mensagemInforma( null, "keyPressed");
		if (kevt.getKeyCode() == KeyEvent.VK_CONTROL)
			Ctrl = true;
		if (( btAnt != null ) & ( btProx != null ) & ( Ctrl == false )) {
			if (kevt.getKeyCode() == KeyEvent.VK_PAGE_UP)
				btAnt.doClick();
			else if (kevt.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
				btProx.doClick();
		}
		if (Ctrl) {
			if (kevt.getKeyCode() == KeyEvent.VK_N)
				btNovo.doClick();
			else if (kevt.getKeyCode() == KeyEvent.VK_D)
				btExcluir.doClick();
			else if (kevt.getKeyCode() == KeyEvent.VK_E)
				btEditar.doClick();
			else if (kevt.getKeyCode() == KeyEvent.VK_S)
				btSalvar.doClick();
			else if (kevt.getKeyCode() == KeyEvent.VK_W)
				btCancelar.doClick();
			if (( btPrim != null ) & ( btUlt != null )) {
				if (kevt.getKeyCode() == KeyEvent.VK_PAGE_UP)
					btPrim.doClick();
				else if (kevt.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
					btUlt.doClick();
			}
		}
	}

	public void visivel(String sVal, boolean bVal) {
		if (navigationOnly)
			return;
		int ind = 0;
		if (bDet || navigation) {
			if (sVal.compareTo("NEW") == 0)
				ind = 4;
			else if (sVal.compareTo("DELETE") == 0)
				ind = 5;
			else if (sVal.compareTo("EDIT") == 0)
				ind = 6;
			else if (sVal.compareTo("SAVE") == 0)
				ind = 7;
			else if (sVal.compareTo("CANCEL") == 0)
				ind = 8;
		}
		else {
			if (sVal.compareTo("NEW") == 0)
				ind = 0;
			else if (sVal.compareTo("DELETE") == 0)
				ind = 1;
			else if (sVal.compareTo("EDIT") == 0)
				ind = 2;
			else if (sVal.compareTo("SAVE") == 0)
				ind = 3;
			else if (sVal.compareTo("CANCEL") == 0)
				ind = 4;
		}
		if (bVal) {
			if (podeVer[ind]) {
				getComponent(ind).setEnabled(bVal);
			}
		}
		else {
			getComponent(ind).setEnabled(bVal);
		}
	}

	public void setAtivo(int ind, boolean b) {
		podeVer[ind] = b;
		getComponent(ind).setEnabled(b);
	}

	public void keyReleased(KeyEvent kevt) {
		// Funcoes.mensagemInforma( null, "keyRelease");
		if (kevt.getKeyCode() == KeyEvent.VK_CONTROL)
			Ctrl = false;
	}

	public void keyTyped(KeyEvent kevt) {
		// Funcoes.mensagemInforma( null, "keyTyped");
	}

	public void actionPerformed(ActionEvent evt) {
		if (lcNav != null) {
			if (evt.getSource() == btNovo) {
				lcNav.insert(true);
			}
			else if (evt.getSource() == btSalvar) {
				lcNav.post();
			}
			else if (evt.getSource() == btEditar) {
				lcNav.edit();
			}
			else if (evt.getSource() == btExcluir) {
				lcNav.delete();
			}
			else if (evt.getSource() == btCancelar) {
				lcNav.cancel(true);
			}
			if (bDet || navigation) {
				if (evt.getSource() == btPrim) {
					lcNav.first(TIPO_PK);
				}
				else if (evt.getSource() == btAnt) {
					lcNav.prior(TIPO_PK);
				}
				else if (evt.getSource() == btProx) {
					lcNav.next(TIPO_PK);
				}
				else if (evt.getSource() == btUlt) {
					lcNav.last(TIPO_PK);
				}
			}
		}
	}
}
