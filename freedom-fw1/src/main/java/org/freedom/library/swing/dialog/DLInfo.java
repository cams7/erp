/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 * Projeto: Freedom <BR>
 * 
 * Pacote: org.freedom.modulos.std <BR>
 * Classe:
 * @(#)DLFechaPag.java <BR>
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
 * Comentários sobre a classe...
 */

package org.freedom.library.swing.dialog;

import java.util.Date;

import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;

public class DLInfo extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JTextFieldFK txtDtins = new JTextFieldFK(JTextFieldPad.TP_DATE, 10, 0);

	private JTextFieldFK txtHins = new JTextFieldFK(JTextFieldPad.TP_TIME, 10, 0);

	private JTextFieldFK txtUsuIns = new JTextFieldFK(JTextFieldPad.TP_STRING, 20, 0);

	private JTextFieldFK txtDtAlt = new JTextFieldFK(JTextFieldPad.TP_DATE, 10, 0);

	private JTextFieldFK txtHAlt = new JTextFieldFK(JTextFieldPad.TP_TIME, 10, 0);

	private JTextFieldFK txtUsuAlt = new JTextFieldFK(JTextFieldPad.TP_STRING, 20, 0);

	private FDados telaorig = null;

	public DLInfo(FDados cOrig, Date dtins, Date dtalt, Date hins, Date halt, String idins, String idalt) {

		super(cOrig);

		setTitulo("Informações do registro");

		setAtribos(270, 180);

		txtDtins.setVlrDate(dtins);
		txtHins.setVlrTime(hins);

		txtDtAlt.setVlrDate(dtalt);
		txtHAlt.setVlrTime(halt);

		txtUsuIns.setVlrString(idins);
		txtUsuAlt.setVlrString(idalt);

		adic(new JLabelPad("Data e hora da inserção"), 7, 0, 200, 20);
		adic(new JLabelPad("Usuário"), 150, 0, 100, 20);

		adic(txtDtins, 7, 20, 70, 20);
		adic(txtHins, 77, 20, 70, 20);
		adic(txtUsuIns, 150, 20, 100, 20);

		adic(new JLabelPad("Data e hora da alteração"), 7, 40, 200, 20);
		adic(new JLabelPad("Usuário"), 150, 40, 100, 20);

		adic(txtDtAlt, 7, 60, 70, 20);
		adic(txtHAlt, 77, 60, 70, 20);
		adic(txtUsuAlt, 150, 60, 100, 20);

		telaorig = ( FDados ) telaorig;
	}

}
