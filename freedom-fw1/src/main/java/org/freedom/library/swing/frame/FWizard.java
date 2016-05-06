/**
 * @version 27/04/2004 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.telas <BR>
 * Classe:
 * @(#)FFWizard.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * Comentários da classe.....
 */

package org.freedom.library.swing.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;

import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.dialog.FFDialogo;

import javax.swing.border.EtchedBorder;

public class FWizard extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCorpo = new JPanelPad();

	private JPanelPad pnTit = new JPanelPad(JPanelPad.TP_JPANEL, new FlowLayout(FlowLayout.LEFT, 15, 10));

	private JPanelPad pnCorpo = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());

	private JPanelPad pnRod = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());

	private JPanelPad pnBotoes = new JPanelPad(JPanelPad.TP_JPANEL, new FlowLayout(FlowLayout.RIGHT, 5, 3));

	private JButtonPad btVoltar = new JButtonPad("< Voltar");

	private JButtonPad btProximo = new JButtonPad("Próximo >");

	private JButtonPad btFinalizar = new JButtonPad("Finalizar");

	private JButtonPad btCancelar = new JButtonPad("Cancelar");

	private JLabelPad lbTit = new JLabelPad("Título");

	Dimension dimBotao = new Dimension(100, 30);

	Object oInfoCache[] = null;

	protected boolean podeVoltar = false;

	protected boolean podeProximo = true;

	protected boolean podeFinalizar = false;

	protected boolean podeCancelar = true;

	String sNivel = "1";

	public FWizard(Component cPai) {

		super(cPai);

		// Reconstruindo a tela

		c.removeAll();

		pnTit.setBorder(new EtchedBorder());
		pnCorpo.setBorder(new EtchedBorder());
		pnRod.setBorder(new EtchedBorder());

		c.add(pnTit, BorderLayout.NORTH);
		c.add(pnCorpo, BorderLayout.CENTER);
		c.add(pnRod, BorderLayout.SOUTH);

		// Montando painel de botoes

		btVoltar.setPreferredSize(dimBotao);
		btProximo.setPreferredSize(dimBotao);
		btFinalizar.setPreferredSize(dimBotao);
		btCancelar.setPreferredSize(dimBotao);

		JPanelPad pnNav = new JPanelPad(JPanelPad.TP_JPANEL, new GridLayout(1, 2));
		pnNav.add(btVoltar);
		pnNav.add(btProximo);
		pnBotoes.add(pnNav);
		pnBotoes.add(btFinalizar);
		pnBotoes.add(btCancelar);
		pnRod.add(pnBotoes, BorderLayout.EAST);

		// Montando o tit
		lbTit.setFont(lbTit.getFont().deriveFont(( float ) 14.0));
		lbTit.setForeground(Color.BLUE);
		pnTit.add(lbTit);

		// Montando o corpo
		pinCorpo.setBorder(BorderFactory.createEmptyBorder());
		pnCorpo.add(pinCorpo, BorderLayout.CENTER);

		btVoltar.setEnabled(false);
		btFinalizar.setEnabled(false);

		btVoltar.setMnemonic('V');
		btProximo.setMnemonic('P');
		btFinalizar.setMnemonic('F');

		btCancelar.setMnemonic('C');

		btVoltar.addActionListener(this);
		btProximo.addActionListener(this);
		btFinalizar.addActionListener(this);
		btCancelar.addActionListener(this);
	}

	public void upBotoes() {

		btVoltar.setEnabled(podeVoltar);
		btProximo.setEnabled(podeProximo);
		btFinalizar.setEnabled(podeFinalizar);
		btCancelar.setEnabled(podeCancelar);
	}

	public void setCabecalho(String sCab) {

		lbTit.setText(sCab);
	}

	public void setPanel(JPanelPad pn) {

		pnCorpo.add(pn, BorderLayout.CENTER);
	}

	public void setPainel(JPanelPad pin) {

		pnCorpo.remove(pinCorpo);
		pin.tiraBorda();
		pnCorpo.add(pin, BorderLayout.CENTER);
		pinCorpo = pin;
		pnCorpo.updateUI();
	}

	public void adic(Component comp, int x, int y, int larg, int alt) {

		pinCorpo.adic(comp, x, y, larg, alt);
		comp.addKeyListener(this);
	}

	public String getNivel() {

		return sNivel;
	}

	public void voltar() {

		return;
	}

	public String proximo() {

		return "1";
	}

	public void finalizar() {

		ok();
	}

	public void cancelar() {

		cancel();
	}

	public void actionPerformed(ActionEvent evt) {

		if (evt.getSource() == btVoltar) {
			sNivel = sNivel.length() > 0 ? sNivel.substring(0, sNivel.length() - 1) : sNivel;
			voltar();
		}
		else if (evt.getSource() == btProximo) {
			sNivel = sNivel + proximo();
		}
		else if (evt.getSource() == btFinalizar)
			finalizar();
		else if (evt.getSource() == btCancelar)
			cancelar();
		upBotoes();
		super.actionPerformed(evt);
	}
}
