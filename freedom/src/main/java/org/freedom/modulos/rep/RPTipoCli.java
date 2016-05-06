/**
 * @version 02/2007 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * @author Alex Rodrigues<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.rep <BR>
 *         Classe:
 * @(#)RPTipoCli.java <BR>
 * 
 *                    Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                    modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                    na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                    Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                    sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                    Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                    Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                    de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                    Tela para cadastro de tipos de clientes.
 * 
 */

package org.freedom.modulos.rep;

import java.awt.event.ActionListener;

import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;

public class RPTipoCli extends FDados implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodTipoCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtDescTipoCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtTipo = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	public RPTipoCli() {

		super( false );
		setTitulo( "Cadastro de tipos de clientes" );
		setAtribos( 50, 50, 410, 140 );

		montaTela();
		setListaCampos( true, "TIPOCLI", "RP" );
	}

	private void montaTela() {

		adicCampo( txtCodTipoCli, 7, 30, 70, 20, "CodTipoCli", "Cód.tp.cli.", ListaCampos.DB_PK, true );
		adicCampo( txtDescTipoCli, 80, 30, 250, 20, "DescTipoCli", "Descrição do tipo de cliente", ListaCampos.DB_SI, true );
		adicCampo( txtTipo, 333, 30, 50, 20, "TipoCli", "Tipo", ListaCampos.DB_SI, false );
	}
}
