/**
 * @version 02/2007 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * @author Alex Rodrigues<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.rep <BR>
 *         Classe:
 * @(#)RPContatos.java <BR>
 * 
 *                     Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                     modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                     na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                     Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                     sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                     Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                     Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                     de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                     Tela para cadastro de contatos.
 * 
 */

package org.freedom.modulos.rep;

import java.awt.event.ActionListener;

import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;

public class RPContato extends FDados implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodCont = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtNomeCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private final JTextFieldPad txtEndCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 45, 0 );

	// private final JTextFieldPad txtNumCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldPad txtCidCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private final JTextFieldPad txtBairCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private final JTextFieldPad txtCepCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private final JTextFieldPad txtUFCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private final JTextFieldPad txtDDDCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private final JTextFieldPad txtFoneCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private final JTextFieldPad txtFaxCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private final JTextFieldPad txtEmailCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtNascCont = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	public RPContato() {

		super( false );
		setTitulo( "Cadastro de contato" );
		setAtribos( 50, 50, 435, 300 );

		montaTela();
		setListaCampos( true, "CONTATO", "RP" );

		txtCepCont.setMascara( JTextFieldPad.MC_CEP );
		txtFoneCont.setMascara( JTextFieldPad.MC_FONE );
		txtFaxCont.setMascara( JTextFieldPad.MC_FONE );
	}

	private void montaTela() {

		adicCampo( txtCodCont, 7, 30, 100, 20, "CodCont", "Cód.cont.", ListaCampos.DB_PK, true );
		adicCampo( txtNomeCont, 110, 30, 300, 20, "NomeCont", "Nome do contato", ListaCampos.DB_SI, true );

		adicCampo( txtEndCont, 7, 70, 403, 20, "EndCont", "Endereço", ListaCampos.DB_SI, false );

		adicCampo( txtCidCont, 7, 110, 132, 20, "CidCont", "Cidade", ListaCampos.DB_SI, false );
		adicCampo( txtBairCont, 142, 110, 132, 20, "BairCont", "Bairro", ListaCampos.DB_SI, false );
		adicCampo( txtCepCont, 277, 110, 80, 20, "CepCont", "Cep", ListaCampos.DB_SI, false );
		adicCampo( txtUFCont, 360, 110, 50, 20, "EstCont", "UF", ListaCampos.DB_SI, false );

		adicCampo( txtDDDCont, 7, 150, 52, 20, "DDDCont", "DDD", ListaCampos.DB_SI, false );
		adicCampo( txtFoneCont, 62, 150, 172, 20, "FoneCont", "Fone", ListaCampos.DB_SI, false );
		adicCampo( txtFaxCont, 237, 150, 172, 20, "FaxCont", "Fax", ListaCampos.DB_SI, false );

		adicCampo( txtEmailCont, 7, 190, 300, 20, "EmailCont", "E-mail", ListaCampos.DB_SI, false );
		adicCampo( txtNascCont, 310, 190, 100, 20, "NascCont", "Nascimento", ListaCampos.DB_SI, false );
	}
}
