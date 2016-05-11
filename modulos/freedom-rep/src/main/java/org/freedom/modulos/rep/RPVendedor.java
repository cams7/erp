/**
 * @version 02/2007 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * @author Alex Rodrigues<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.rep <BR>
 *         Classe:
 * @(#)RPVendedor.java <BR>
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
 *                     Tela para cadastro de vendedores.
 * 
 */

package org.freedom.modulos.rep;

import java.awt.event.ActionListener;

import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;

public class RPVendedor extends FDados implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtNomeVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private final JTextFieldPad txtEndVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 45, 0 );

	// private final JTextFieldPad txtNumCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldPad txtCidVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private final JTextFieldPad txtBairVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private final JTextFieldPad txtCepVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private final JTextFieldPad txtUFVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private final JTextFieldPad txtDDDVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private final JTextFieldPad txtFoneVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private final JTextFieldPad txtFaxVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private final JTextFieldPad txtEmailVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtComisVend = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 5 );

	public RPVendedor() {

		super( false );
		setTitulo( "Cadastro de vendedor" );
		setAtribos( 50, 50, 435, 300 );

		montaTela();
		setListaCampos( true, "VENDEDOR", "RP" );

		txtCepVend.setMascara( JTextFieldPad.MC_CEP );
		txtFoneVend.setMascara( JTextFieldPad.MC_FONE );
		txtFaxVend.setMascara( JTextFieldPad.MC_FONE );
	}

	private void montaTela() {

		adicCampo( txtCodVend, 7, 30, 100, 20, "CodVend", "Cód.vend.", ListaCampos.DB_PK, true );
		adicCampo( txtNomeVend, 110, 30, 300, 20, "NomeVend", "Nome do vendedor", ListaCampos.DB_SI, true );

		adicCampo( txtEndVend, 7, 70, 403, 20, "EndVend", "Endereço", ListaCampos.DB_SI, false );

		adicCampo( txtCidVend, 7, 110, 132, 20, "CidVend", "Cidade", ListaCampos.DB_SI, false );
		adicCampo( txtBairVend, 142, 110, 132, 20, "BairVend", "Bairro", ListaCampos.DB_SI, false );
		adicCampo( txtCepVend, 277, 110, 80, 20, "CepVend", "Cep", ListaCampos.DB_SI, false );
		adicCampo( txtUFVend, 360, 110, 50, 20, "EstVend", "UF", ListaCampos.DB_SI, false );

		adicCampo( txtDDDVend, 7, 150, 52, 20, "DDDVend", "DDD", ListaCampos.DB_SI, false );
		adicCampo( txtFoneVend, 62, 150, 172, 20, "FoneVend", "Fone", ListaCampos.DB_SI, false );
		adicCampo( txtFaxVend, 237, 150, 172, 20, "FaxVend", "Fax", ListaCampos.DB_SI, false );

		adicCampo( txtEmailVend, 7, 190, 300, 20, "EmailVend", "E-mail", ListaCampos.DB_SI, false );
		adicCampo( txtComisVend, 310, 190, 100, 20, "PercComis", "% Comissão", ListaCampos.DB_SI, false );
	}
}
