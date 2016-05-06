/**
 * @version 12/05/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.acao <BR>
 * Classe:
 * @(#)RadioGroupEvent.java <BR>
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

package org.freedom.acao;

import javax.swing.JRadioButton;

public class RadioGroupEvent {

	private Object source = null;

	private JRadioButton rButton = null;

	private int Indice = -1;

	public RadioGroupEvent(JRadioButton rb, int ind, Object source) {

		rButton = rb;
		Indice = ind;
		this.source = source;
	}

	public JRadioButton getRadioButton() {

		return rButton;
	}

	public int getIndice() {

		return Indice;
	}

	public Object getSource() {

		return source;
	}
}
