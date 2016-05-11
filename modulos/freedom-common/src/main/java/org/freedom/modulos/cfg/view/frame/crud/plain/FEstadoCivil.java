/**
 * @version 28/01/2008 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.grh <BR>
 *         Classe: @(#)FArea.java <BR>
 * 
 *         Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *         modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *         na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *         Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *         sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *         Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *         Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *         escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA <BR>
 * <BR>
 * 
 *         Formulário para cadastro de estados civis.
 * 
 */

package org.freedom.modulos.cfg.view.frame.crud.plain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.type.TYPE_PRINT;

public class FEstadoCivil extends FDados implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodEstCivil = new JTextFieldPad(JTextFieldPad.TP_STRING, 2, 0);

	private JTextFieldPad txtDescEstCivil = new JTextFieldPad(JTextFieldPad.TP_STRING, 50, 0);

	public FEstadoCivil() {

		super();
		setTitulo("Cadastro de estados civis");
		setAtribos(50, 50, 380, 135);
		adicCampo(txtCodEstCivil, 7, 20, 70, 20, "CodEstCivil", "Cód.E.Civil", ListaCampos.DB_PK, true);
		adicCampo(txtDescEstCivil, 80, 20, 250, 20, "DescEstCivil", "Descrição do estado civil", ListaCampos.DB_SI,
				true);
		setListaCampos(false, "ESTCIVIL", "SG");
		btImp.addActionListener(this);
		btPrevimp.addActionListener(this);
		lcCampos.setQueryInsert(false);

		setImprimir(true);
	}

	public void actionPerformed(ActionEvent evt) {

		if (evt.getSource() == btPrevimp)
			imprimir(TYPE_PRINT.VIEW);
		else if (evt.getSource() == btImp)
			imprimir(TYPE_PRINT.PRINT);
		super.actionPerformed(evt);
	}

	private void imprimir(TYPE_PRINT bVisualizar) {

	}
}
