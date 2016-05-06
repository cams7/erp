/**
 * @version 14/11/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.telas <BR>
 * Classe:
 * @(#)FObservacao.java <BR>
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
 * Classe para implementação de observações..<BR>
 * ATENÇÂO!! ESTA CLASSE É DERIVADA DE FDialogo E NÂO FFDialogo.
 */
package org.freedom.library.swing.frame;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;

import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.dialog.FDialogo;

public class FObservacao extends FDialogo {

	private static final long serialVersionUID = 1L;

	protected JPanelPad pn = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());

	public JTextAreaPad txa = new JTextAreaPad();

	private JScrollPane spn = new JScrollPane(txa);

	/**
	 * 
	 * Construtor sem titulo.
	 * 
	 * @param sPad
	 *            - Texto inicial.
	 */
	public FObservacao(String sPad) {

		this(null, sPad, 0);
	}

	/**
	 * 
	 * Construtor com titulo.
	 * 
	 * @param sTit
	 *            - Título da janela.
	 * @param sPad
	 *            - Texto inicial.
	 */
	public FObservacao(String sTit, String sPad) {

		this(sTit, sPad, 0);
	}

	public FObservacao(String sTit, String sPad, int iTam) {

		if (sTit != null) {

			setTitulo(sTit, this.getClass().getName());
		}
		else {

			setTitulo("Observação", this.getClass().getName());
		}

		setAtribos(400, 200);

		pn.add(spn, BorderLayout.CENTER);

		c.add(pn);

		txa.setText(sPad);

		if (iTam > 0) {

			txa.iTamanho = iTam;
		}
	}

	/**
	 * 
	 * Retorna o texto digitado na dialog de observação.
	 * 
	 * @return sTexto - Texto digitado.
	 * 
	 */
	public String getTexto() {

		String sTexto = txa.getText();
		return sTexto;
	}
}
