package org.freedom.modulos.grh.view.frame.crud.plain;

import java.awt.event.ActionListener;

import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;

/**
 * Classe para cadastro de Benefícios <BR>
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.modulos.grh <BR>
 * Classe:
 * 
 * @FBeneficio.java <BR>
 * <BR>
 *                  Este programa é licenciado de acordo com a LGPL (Lesser General Public License), <BR>
 *                  versão 2.1, Fevereiro de 1999 <BR>
 *                  A LGPL deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste Programa. <BR>
 *                  Caso uma cópia da LGPL não esteja disponível junto com este Programa, você pode contatar <BR>
 *                  o LICENCIADOR ou então pegar uma cópia em: <a href=http://creativecommons.org/licenses/LGPL/2.1/legalcode.pt> Creative Commons</a> <BR>
 *                  Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa é preciso estar de acordo com os termos da LGPL. <BR>
 * <BR>
 * @author Setpoint Informática Ltda. <BR>
 * @version 1.0.0 - 03/10/2008 <BR>
 * @since 03/10/2008. <BR>
 * 
 */

public class FBeneficio extends FDados implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final JTextFieldPad txtCodBenef = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private final JTextFieldPad txtDescBenef = new JTextFieldPad( JTextFieldPad.TP_STRING, 60, 0 );

	private final JTextFieldPad txtVlrBenef = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	public FBeneficio() {

		super();
		setTitulo( "Cadastro de Benefícios" );
		setAtribos( 50, 50, 450, 125 );

		montaTela();
	}

	private void montaTela() {

		nav.setNavigation( true );

		adicCampo( txtCodBenef, 7, 20, 70, 20, "CodBenef", "Cód.benef.", ListaCampos.DB_PK, true );
		adicCampo( txtDescBenef, 80, 20, 260, 20, "DescBenef", "Descrição do benefício", ListaCampos.DB_SI, true );
		adicCampo( txtVlrBenef, 345, 20, 80, 20, "ValorBenef", "Valor( R$ )", ListaCampos.DB_SI, false );
		setListaCampos( true, "BENEFICIO", "RH" );
		lcCampos.setQueryInsert( false );
	}

}
