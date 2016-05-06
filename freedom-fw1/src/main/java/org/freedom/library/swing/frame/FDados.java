/**
 * @version 05/06/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 *
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.telas <BR>
 * Classe: @(#)FDados.java <BR>
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.PreparedStatement;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.exceptions.ExceptionSetConexao;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JPasswordFieldPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.component.PainelImagem;
import org.freedom.library.swing.dialog.DLInfo;

public class FDados extends FFilho implements ActionListener, KeyListener, InternalFrameListener, PostListener {// ,
	// FocusListener
	// {

	private static final long serialVersionUID = 1L;

	public PreparedStatement atualiza = null;

	public PreparedStatement insere = null;

	public PreparedStatement deleta = null;

	public ListaCampos lcCampos = new ListaCampos(this);

	public ListaCampos lcSeq = null;

	private GridLayout glImp = new GridLayout(1, 6);

	public Navegador nav = new Navegador(false);

	public Navegador navSeq = new Navegador(false);

	public JPanelPad pnImp = new JPanelPad(new BorderLayout());

	public JPanelPad pnGImp = new JPanelPad(JPanelPad.TP_JPANEL);

	public JPanelPad pinDados = new JPanelPad();

	public JButtonPad btSair = new JButtonPad("Sair", Icone.novo("btSair.png"));

	public JButtonPad btImp = new JButtonPad(Icone.novo("btImprime.png"));

	public JButtonPad btPrevimp = new JButtonPad(Icone.novo("btPrevimp.png"));

	public JButtonPad btInfo = new JButtonPad(Icone.novo("btInfo.png"));

	public JComponent primeiroCompo = null;

	boolean Shift = false;

	boolean Ctrl = false;

	boolean setArea = true;

	boolean bMostrar = false;

	private DLInfo dlinfo = null;

	private JTextFieldFK txtDtins = new JTextFieldFK(JTextFieldPad.TP_DATE, 10, 0);

	private JTextFieldFK txtHins = new JTextFieldFK(JTextFieldPad.TP_TIME, 8, 0);

	private JTextFieldFK txtUsuIns = new JTextFieldFK(JTextFieldPad.TP_STRING, 20, 0);

	private JTextFieldPad txtDtAlt = new JTextFieldPad(JTextFieldPad.TP_DATE, 10, 0);

	private JTextFieldPad txtHAlt = new JTextFieldPad(JTextFieldPad.TP_TIME, 8, 0);

	private JTextFieldPad txtUsuAlt = new JTextFieldPad(JTextFieldPad.TP_STRING, 20, 0);

	public FDados() {
		this(true);
	}

	public FDados(boolean comScroll) {
		super(comScroll);
		btImp.setVisible(false);
		btPrevimp.setVisible(false);
		// setTitulo("Formulário de dados", this.getClass().getName());

		lcSeq = lcCampos;
		navSeq = nav;

		btSair.setToolTipText("Fecha a Tela (Shift + F4)");
		btImp.setToolTipText("Imprimir (Ctrl+P)");
		btPrevimp.setToolTipText("Visualizar Impressão (Ctrl+R)");
		btInfo.setToolTipText("Informações sobre o registro");

		pnGImp.setLayout(glImp);
		pnGImp.setPreferredSize(new Dimension(120, 26));

		pnGImp.add(btImp);
		pnGImp.add(btPrevimp);
		pnGImp.add(btInfo);

		pnImp.add(pnGImp, BorderLayout.WEST);

		btSair.addActionListener(this);
		btInfo.addActionListener(this);

		btSair.addKeyListener(this);
		btImp.addKeyListener(this);
		btPrevimp.addKeyListener(this);

		addKeyListener(this);
		addInternalFrameListener(this);

		lcCampos.addPostListener(this);

		pnRodape.add(btSair, BorderLayout.EAST);
		pnRodape.add(nav, BorderLayout.WEST);

		pnRodape.add(pnImp, BorderLayout.CENTER);

		c.add(pnBordRod, BorderLayout.SOUTH);

		txtDtins.setSoLeitura(true);
		txtHins.setSoLeitura(true);
		txtUsuIns.setSoLeitura(true);
		txtDtAlt.setSoLeitura(true);
		txtHAlt.setSoLeitura(true);
		txtUsuAlt.setSoLeitura(true);

	}

	private void adicCamposInfo() {
		try {

			adicCampoInvisivel(txtDtins, "dtins", "Dt.ins.", ListaCampos.DB_SI, false);
			adicCampoInvisivel(txtHins, "hins", "Hr.ins.", ListaCampos.DB_SI, false);
			adicCampoInvisivel(txtUsuIns, "idusuins", "id.ins.", ListaCampos.DB_SI, false);

			adicCampoInvisivel(txtDtAlt, "dtalt", "Dt.alt.", ListaCampos.DB_SI, false);
			adicCampoInvisivel(txtHAlt, "halt", "hr.alt.", ListaCampos.DB_SI, false);
			adicCampoInvisivel(txtUsuAlt, "idusualt", "id.alt.", ListaCampos.DB_SI, false);

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setImprimir(boolean bImp) {
		btImp.setVisible(bImp);
		btPrevimp.setVisible(bImp);
	}

	public void setPKFoco() {
		for (int i = 0; i < lcCampos.getComponentCount(); i++) {
			if (( ( GuardaCampo ) lcCampos.getComponent(i) ).ehPK())
				( ( GuardaCampo ) lcCampos.getComponent(i) ).getComponente().requestFocus();
		}
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand() == "Sair") {
			dispose();
		}
		else if (evt.getSource() == btInfo) {
			getInfo();
		}
	}

	private void getInfo() {

		if (lcCampos.getStatus() == ListaCampos.LCS_NONE) {
			Funcoes.mensagemInforma(this, "Selecione um registro!");
		}
		else {
			dlinfo = new DLInfo(this, getDtins(), getDtAlt(), getHins(), getHAlt(), getUsuIns(), getUsuAlt());
			dlinfo.setVisible(true);
		}

	}

	public Date getDtins() {
		return txtDtins.getVlrDate();
	}

	public Date getHins() {
		return txtHins.getVlrTime();
	}

	public String getUsuIns() {
		return txtUsuIns.getVlrString();
	}

	public Date getDtAlt() {
		return txtDtAlt.getVlrDate();
	}

	public Date getHAlt() {
		return txtHAlt.getVlrTime();
	}

	public String getUsuAlt() {
		return txtUsuAlt.getVlrString();
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
		// Funcoes.mensagemErro( null, "keyRelease");
		if (kevt.getKeyCode() == KeyEvent.VK_SHIFT)
			Shift = false;
		else if (kevt.getKeyCode() == KeyEvent.VK_CONTROL)
			Ctrl = false;
	}

	public void keyTyped(KeyEvent kevt) {
	}

	public void setPainel(JPanelPad pin, Container pn) {
		pinDados = pin;
		pn.add(pinDados);
		setArea = false;
	}

	public void setPainel(JPanelPad pin) {
		pinDados = pin;
		setArea = false;
	}

	public Dimension getAreaComp() {
		int iLarg = ( int ) getSize().getWidth();
		int iAlt = ( int ) getSize().getHeight();
		iLarg -= 10;
		iAlt -= 35;
		Dimension dm = new Dimension(iLarg, iAlt);
		return dm;
	}

	public void setAreaComp() {
		pinDados = new JPanelPad(( int ) getSize().getWidth() - 10, ( int ) getSize().getHeight() - 65);
		pnCliente.add(pinDados, BorderLayout.CENTER);
		setArea = false;
	}

	public void adic(Component comp, int X, int Y, int Larg, int Alt) {
		if (setArea)
			setAreaComp();
		comp.addKeyListener(this);
		if (navSeq != null) {
			comp.addKeyListener(navSeq);
		}
		try {
			JTextFieldPad txt = ( JTextFieldPad ) comp;
			txt.addFocusListener(txt);
			comp = txt;
		}
		catch (Exception e) {
		}
		pinDados.adic(comp, X, Y, Larg, Alt);
	}

	public JLabelPad adicCampo(JTextFieldPad comp, int X, int Y, int Larg, int Alt, String nome, String label, byte key, JTextFieldFK txtDescFK, boolean req) {
		comp.setName("txt" + nome);
		comp.setNomeCampo(nome);
		comp.setListaCampos(lcSeq);
		comp.setChave(key);
		if (txtDescFK == null)
			lcSeq.add(new GuardaCampo(comp, nome, label, key, req));
		else
			lcSeq.add(new GuardaCampo(comp, nome, label, key, txtDescFK, req));
		if (navSeq != null) {
			navSeq.setListaCampos(lcSeq);
			lcSeq.setNavegador(navSeq);
		}
		lcSeq.setState(ListaCampos.LCS_NONE);
		JLabelPad lbTmp = new JLabelPad(label);
		adic(lbTmp, X, Y - 20, Larg, 20);
		adic(comp, X, Y, Larg, Alt);
		return lbTmp;
	}

	public JLabelPad adicCampo(JTextFieldPad comp, int X, int Y, int Larg, int Alt, String nome, String label, byte key, boolean req) {
		return adicCampo(comp, X, Y, Larg, Alt, nome, label, key, null, req);
	}

	public void adicCampo(JPasswordFieldPad comp, int X, int Y, int Larg, int Alt, String nome, String label, byte key, boolean req) {
		comp.setName("txp" + nome);
		comp.setListaCampos(lcSeq);
		lcSeq.add(new GuardaCampo(comp, nome, label, key, req), "txt" + nome);
		if (navSeq != null) {
			navSeq.setListaCampos(lcSeq);
			lcSeq.setNavegador(navSeq);
		}
		lcSeq.setState(ListaCampos.LCS_NONE);
		adic(new JLabelPad(label), X, Y - 20, Larg, 20);
		adic(comp, X, Y, Larg, Alt);
	}

	public void adicCampoInvisivel(JTextFieldPad comp, String nome, String label, byte key, JTextFieldFK txtDescFK, boolean req) {
		comp.addKeyListener(this);
		if (navSeq != null) {
			comp.addKeyListener(navSeq);
		}
		comp.setName("txt" + nome);
		comp.setNomeCampo(nome);
		comp.setListaCampos(lcSeq);
		lcSeq.add(new GuardaCampo(comp, nome, label, key, txtDescFK, req, false));
		if (navSeq != null) {
			navSeq.setListaCampos(lcSeq);
			lcSeq.setNavegador(navSeq);
		}
		lcSeq.setState(ListaCampos.LCS_NONE);
	}

	public JLabelPad adicCampoInvisivel(JTextFieldPad comp, String nome, String label, byte key, boolean req) {
		adicCampoInvisivel(comp, nome, label, key, null, req);
		return new JLabelPad(label);
	}

	public JLabelPad adicDescFK(JTextFieldFK comp, int X, int Y, int Larg, int Alt, String nome, String label) {
		comp.setNomeCampo(nome);
		comp.addKeyListener(this);
		if (navSeq != null) {
			comp.addKeyListener(navSeq);
		}
		comp.setLabel(label);
		JLabelPad lbTmp = new JLabelPad(label);
		adic(lbTmp, X, Y - 20, Larg, 20);
		adic(comp, X, Y, Larg, Alt);
		return lbTmp;
	}

	public JLabelPad adicDescFKInvisivel(JTextFieldFK comp, String nome, String label) {
		comp.setNomeCampo(nome);
		comp.addKeyListener(this);
		if (navSeq != null) {
			comp.addKeyListener(navSeq);
		}
		comp.setLabel(label);
		JLabelPad lbTmp = new JLabelPad(label);
		return lbTmp;
	}

	public JLabelPad adicDBLiv(Component comp, String nome, String label, boolean req) {
		comp.setName(nome);
		comp.addKeyListener(this);
		if (navSeq != null) {
			comp.addKeyListener(navSeq);
		}
		if (comp instanceof JRadioGroup<?, ?>)
			( ( JRadioGroup<?, ?> ) comp ).setListaCampos(lcSeq);
		else if (comp instanceof JCheckBoxPad)
			( ( JCheckBoxPad ) comp ).setListaCampos(lcSeq);
		else if (comp instanceof JTextAreaPad)
			( ( JTextAreaPad ) comp ).setListaCampos(lcSeq);
		lcSeq.add(new GuardaCampo(comp, nome, label, ListaCampos.DB_SI, req));

		return new JLabelPad(label);

	}

	public void adicDBLiv(Component comp, int X, int Y, int Larg, int Alt, String nome, String label, boolean req) {
		comp.setName(nome);
		comp.addKeyListener(this);
		
		
		if (comp instanceof JRadioGroup<?, ?>)
			( ( JRadioGroup<?, ?> ) comp ).setListaCampos(lcSeq);
		else if (comp instanceof JCheckBoxPad)
			( ( JCheckBoxPad ) comp ).setListaCampos(lcSeq);
		else if (comp instanceof JTextAreaPad) {
			( ( JTextAreaPad ) comp ).setListaCampos(lcSeq);
			comp.setName("txp" + nome);
			
			adic(new JLabelPad(label), X, Y - 20, Larg, 20);
			adic(new JScrollPane(comp), X, Y, Larg, Alt);
		}
		lcSeq.add(new GuardaCampo(comp, nome, label, ListaCampos.DB_SI, req));

		lcSeq.setState(ListaCampos.LCS_NONE);
	}

	public JLabelPad adicDB(Component comp, int X, int Y, int Larg, int Alt, String nome, String label, byte key, boolean req) {
		boolean bScroll = false;
		JLabelPad lbTmp = null;
		comp.setName(nome);
		if (comp instanceof JRadioGroup<?, ?>)
			( ( JRadioGroup<?, ?> ) comp ).setListaCampos(lcSeq);
		else if (comp instanceof JCheckBoxPad)
			( ( JCheckBoxPad ) comp ).setListaCampos(lcSeq);
		else if (comp instanceof JComboBoxPad)
			( ( JComboBoxPad ) comp ).setListaCampos(lcSeq);
		else if (comp instanceof PainelImagem)
			( ( PainelImagem ) comp ).setListaCampos(lcSeq);
		else if (comp instanceof JTextAreaPad) {
			( ( JTextAreaPad ) comp ).setListaCampos(lcSeq);
			bScroll = true;
		}
		lcSeq.add(new GuardaCampo(comp, nome, label, key, req));
		if (comp instanceof JTextAreaPad) {
			if (navSeq != null) {
				navSeq.setListaCampos(lcSeq);
				lcSeq.setNavegador(navSeq);
			}
		}
		lbTmp = new JLabelPad(label);
		adic(lbTmp, X, Y - 20, Larg, 20);
		if (bScroll)
			adic(new JScrollPane(comp), X, Y, Larg, Alt);
		else
			adic(comp, X, Y, Larg, Alt);

		return lbTmp;
		
	}
		
	public JLabelPad adicDB(Component comp, int X, int Y, int Larg, int Alt, String nome, String label, boolean req) {
		return this.adicDB(comp, X, Y, Larg, Alt, nome, label, ListaCampos.DB_SI, req);
	}
	

	public JLabelPad adic(Component comp, int X, int Y, int Larg, int Alt, String label, boolean scroll) {
		
		JLabelPad lbTmp = null;
		
		if(!scroll) {
			
			lbTmp = new JLabelPad(label);
			adic(lbTmp, X, Y - 20, Larg, 20);
			adic(comp, X, Y, Larg, Alt);
		}
		else {
			lbTmp = adic( comp, X, Y, Larg, Alt, label );
		}
		

		return lbTmp;
	}
	
	public JLabelPad adic(Component comp, int X, int Y, int Larg, int Alt, String label) {
		JLabelPad lbTmp = null;
		
		lbTmp = new JLabelPad(label);
		adic(lbTmp, X, Y - 20, Larg, 20);
	
		adic(comp, X, Y, Larg, Alt);

		return lbTmp;
	}

	public JLabelPad adicDBInvisivel(Component comp, String nome, String label, boolean req) {

		JLabelPad lbTmp = null;
		comp.setName(nome);
		if (comp instanceof JRadioGroup<?, ?>)
			( ( JRadioGroup<?, ?> ) comp ).setListaCampos(lcSeq);
		else if (comp instanceof JCheckBoxPad)
			( ( JCheckBoxPad ) comp ).setListaCampos(lcSeq);
		else if (comp instanceof JComboBoxPad)
			( ( JComboBoxPad ) comp ).setListaCampos(lcSeq);
		else if (comp instanceof PainelImagem)
			( ( PainelImagem ) comp ).setListaCampos(lcSeq);
		else if (comp instanceof JTextAreaPad) {
			( ( JTextAreaPad ) comp ).setListaCampos(lcSeq);
		}

		lcSeq.add(new GuardaCampo(comp, nome, label, ListaCampos.DB_SI, req));
		lbTmp = new JLabelPad(label);
		return lbTmp;

	}

	public void setNavegador(Navegador nv) {
		navSeq = nv;
	}

	public void setListaCampos(ListaCampos lc) {
		lcSeq = lc;
	}

	public void setListaCampos(boolean bAuto, String sTab, String sSigla) {
		if (lcCampos == lcSeq) {
			adicCamposInfo();
		}
		lcSeq.montaSql(bAuto, sTab, sSigla);
	}

	public void setBordaReq(JComponent comp) {
		comp.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red), BorderFactory.createEtchedBorder()));
	}

	public void setBordaPad(JComponent comp) {
		comp.setBorder(BorderFactory.createEtchedBorder());
	}

	public synchronized void setConexao(DbConnection cn) { // throws
		// ExceptionSetConexao
		// {
		try {
			super.setConexao(cn);

			lcCampos.setConexao(con);
			lcSeq.setConexao(con);
			setPKFoco();
		}
		catch (Exception e) {
			new ExceptionSetConexao("Erro ao setar a conexão");
		}

	}

	public void beforePost(PostEvent pevt) {
		setPKFoco();
	}

	public void afterPost(PostEvent pevt) {
	}

	public void internalFrameDeactivated(InternalFrameEvent ifevt) {
		super.internalFrameDeactivated(ifevt);
	}

	public void internalFrameDeiconified(InternalFrameEvent ifevt) {
		super.internalFrameDeiconified(ifevt);
	}

	public void internalFrameIconified(InternalFrameEvent ifevt) {
		super.internalFrameIconified(ifevt);
	}

	public void internalFrameOpened(InternalFrameEvent ifevt) {
		super.internalFrameOpened(ifevt);
	}
}
