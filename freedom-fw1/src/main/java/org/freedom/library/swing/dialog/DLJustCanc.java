/**
 * @author Setpoint Informática Ltda.
 * 
 * Projeto: Freedom <BR>
 * 
 * Pacote: org.freedom.telas <BR>
 * Classe:
 * @(#)DLJustCanc.java <BR>
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
 */
package org.freedom.library.swing.dialog;

import javax.swing.JScrollPane;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JTextAreaPad;

public class DLJustCanc extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JTextAreaPad txaJustCanc = new JTextAreaPad();

	public DLJustCanc() {

		setTitulo("Justificativa do cancelamento");
		setAtribos(330, 190);

		txaJustCanc.requestFocus();
		adic(new JScrollPane(txaJustCanc), 7, 7, 300, 70);

	}

	public String getValor() {

		String sRet = "";

		if (txaJustCanc.getVlrString().equals("")) {
			sRet = "";
		}
		else {
			sRet = txaJustCanc.getVlrString();
		}
		return sRet;
	}

	public void ok() {
		if (( txaJustCanc.getVlrString().equals("") )) {
			Funcoes.mensagemInforma(this, "Informe o motivo do cancelamento!");
			return;
		}
		else {
			super.ok();
		}
	}
}
