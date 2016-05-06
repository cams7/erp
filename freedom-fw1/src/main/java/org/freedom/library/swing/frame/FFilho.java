/**
 * @version 14/11/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.telas <BR>
 * Classe:
 * @(#)FFilho.java <BR>
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
 * Classe com funcoes basicas para controle do um InternalFrame.
 */

package org.freedom.library.swing.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.exceptions.ExceptionSetConexao;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JPanelPad;

public class FFilho extends JInternalFrame implements InternalFrameListener, IFilho {

	private static final long serialVersionUID = 1L;

	protected FPrincipal fPrim;

	private Component firstFocus = null;

	private boolean initFirstFocus = true;

	private int largAnt = 0;
	private int altAnt = 0;

	public boolean primShow = true;

	private BorderLayout blCliente = new BorderLayout();
	private BorderLayout blPrincipal = new BorderLayout();
	private BorderLayout blRodape = new BorderLayout();
	private BorderLayout blDados = new BorderLayout();

	protected Container c = null;

	protected Border br = BorderFactory.createEtchedBorder();

	protected JPanelPad pnPrincipal = new JPanelPad(JPanelPad.TP_JPANEL);

	protected JPanelPad pnCliente = new JPanelPad(JPanelPad.TP_JPANEL);

	// protected JPanelPad pnRodape = new JPanelPad(JPanelPad.TP_JPANEL);

	protected JPanelPad pnRodape = new JPanelPad(new BorderLayout());

	public JPanelPad pnBordRod = new JPanelPad(JPanelPad.TP_JPANEL);

	protected JScrollPane spPrincipal = new JScrollPane(pnPrincipal, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	protected JPanelPad pnRod = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());

	public String strTemp = "";

	public DbConnection con = null;
	
	public JButtonPad btSair = new JButtonPad("Sair", Icone.novo("btSair.png"));

	public FFilho(boolean comScroll) {
		/* Construtor da classe. */
		this("Filho01", true, true, true, true, comScroll);

	}

	public FFilho(String arg01, boolean arg02, boolean arg03, boolean arg04, boolean arg05, boolean comScroll) {
		super(arg01, arg02, arg03, arg04, arg05);

		c = getContentPane();
		c.setLayout(blDados);
		addInternalFrameListener(this);
		setClosable(true);

		pnBordRod.setLayout(new GridLayout(1, 1));
		pnBordRod.setPreferredSize(new Dimension(450, 30));
		pnBordRod.add(pnRodape);
		pnBordRod.setBorder(br);

		// pnCliente.setAutoscrolls(false);
		pnPrincipal.setLayout(blPrincipal);
		pnCliente.setLayout(blCliente);
		pnRodape.setLayout(blRodape);
		pnPrincipal.add(pnCliente, BorderLayout.CENTER);

		if (comScroll) {
			c.add(spPrincipal, BorderLayout.CENTER);
		}
		else {
			c.add(pnPrincipal, BorderLayout.CENTER);
		}

	}

	public void doDefaultCloseAction() {
		// este método e reconstruido para evitar o fechamento da janela,
		// caso queiramos impedir o fechamento.
		// Para isso basta reconstruir o método validaFechamento() nas tela
		// subsequentes.
		// System.out.println("doDefaultCloseAction");
		if (validaFechamento()) {
			super.doDefaultCloseAction();
		}
		//
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

	// protected void ativaScroll() {

	// }
	public void setAtribos(int esq, int topo, int larg, int alt) {
		int altPrinc = 0;
		int largPrinc = 0;

		altAnt = alt; // Guarda a altura anterior para acertar o painel
		// principal
		largAnt = larg; // Guarda a largura anteriror para acertar o painel
		// principal

		if (Aplicativo.telaPrincipal != null) {
			altPrinc = ( int ) Aplicativo.telaPrincipal.dpArea.getSize().getHeight();
			largPrinc = ( int ) Aplicativo.telaPrincipal.dpArea.getSize().getWidth();
		}
		if (( altPrinc < ( topo + alt ) ) || ( largPrinc < ( esq + larg ) )) {
			esq = 0;
			topo = 0;
			larg = largPrinc;
			alt = altPrinc;
		}
		// spCliente.setBounds(0,0, larg, alt);
		this.setBounds(esq, topo, larg - 3, alt - 3);
		// this.pnCliente.setBounds(0,0,100,100);
		// this.spCliente.setVisible(true);
		// this.pnCliente.setBounds(0,0,larg-20,alt-20);
		// this.setSize(larg+50,alt+50);
		// this.show();
		// this.spCliente.setBounds(0, 0, 400, 250);
	}

	public void setTela(Container c) {
		setContentPane(c);
	}

	public Container getTela() {
		Container tela = getContentPane();
		return tela;
	}

	public JPanelPad adicBotaoSair() {
		Container c = getContentPane();
		pnRod = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());
		pnRod.setPreferredSize(new Dimension(110, 30));
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

	/**
	 * Ajusta conexão da tela. <BR>
	 * Adiciona a conexão vigente a este formulário. Esta função é geramente
	 * chamada da classe criadora da tela, <BR>
	 * esta função será sobrescrita em cada classe filha para serem <BR>
	 * devidamente ajustada as conexões necessárias na tela.
	 * 
	 * @param cn
	 *            : Conexao valida e ativa que será repassada e esta tela.
	 */
	public void internalFrameActivated(InternalFrameEvent e) {
		// System.out.println("Teste 1");
	}

	public void internalFrameClosed(InternalFrameEvent e) {
		// System.out.println("Closed");
	}

	protected boolean validaFechamento() {
		return true;
	}

	/*
	 * public boolean isClosable() { return true; }
	 */

	public void internalFrameClosing(InternalFrameEvent e) {

		// System.out.println("Clossing");
	}

	public void internalFrameDeactivated(InternalFrameEvent e) {
		// System.out.println("Teste 4");
	}

	public void internalFrameDeiconified(InternalFrameEvent e) {
		// System.out.println("Teste 5");
	}

	public void internalFrameIconified(InternalFrameEvent e) {
		// System.out.println("Teste 6");
	}

	public synchronized void setFirstFocus(Component firstFocus) {
		this.firstFocus = firstFocus;
	}

	public void setPrimeiroFoco(final JComponent comp) {
		addInternalFrameListener(new InternalFrameAdapter() {
			public void internalFrameActivated(InternalFrameEvent ievt) {
				comp.requestFocusInWindow();
			}
		});
	}

	public synchronized void firstFocus() {
		if (( firstFocus != null ) && ( firstFocus.hasFocus() ) && ( initFirstFocus ))
			firstFocus.requestFocus();

		/*
		 * if (firstFocus!=null) { if (firstFocus.hasFocus())
		 * firstFocus.requestFocus(); else loadFirstFocus(); } else
		 * loadFirstFocus();
		 */
	}

	/*
	 * public synchronized void loadFirstFocus() { Component cOpened = null; if
	 * (contentFirstFocus == null) contentFirstFocus = getContentPane();
	 * //String nome = cpOpened.getName(); if (contentFirstFocus != null) { for
	 * (int i=0 ; i <contentFirstFocus.getComponentCount() ; i++) { cOpened =
	 * contentFirstFocus.getComponent(i); if (cOpened!=null) { if
	 * (cOpened.hasFocus()) { cOpened.nextFocus(); cOpened.requestFocus();
	 * break; } } } } }
	 */

	/*
	 * public synchronized void setContentFirstFocus(Container
	 * contentFirstFocus) { this.contentFirstFocus = contentFirstFocus; }
	 */
	public void internalFrameOpened(InternalFrameEvent e) {
		firstFocus();
	}

	public void setConexao(DbConnection cn) { // throws ExceptionSetConexao {

		try {
			con = cn;
		}
		catch (Exception e) {
			new ExceptionSetConexao("Erro ao setar a conexão");
		}
	}

	public void show() {
		super.show();
		if (primShow) {
			ajustaScroll();
		}
	}

	public void ajustaScroll() {
		this.setSize(this.getWidth() + 3, this.getHeight() + 3);
		// pnCliente.setPreferredSize(new Dimension(spPrincipal.getWidth()-3,
		// spPrincipal.getHeight()-pnBordRod.getHeight()-3));
		if (( largAnt != 0 ) && ( altAnt != 0 ))
			pnCliente.setPreferredSize(new Dimension(largAnt - 30, altAnt - pnBordRod.getHeight() - 36));
		spPrincipal.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		spPrincipal.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// spCliente.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		// spCliente.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		primShow = false;
	}

	public synchronized void execShow() {
		show();
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

}