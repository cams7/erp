/**
 * @version 02/2007 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * @author Alex Rodrigues<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.rep <BR>
 *         Classe:
 * @(#)RPMoeda.java <BR>
 * 
 *                  Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                  modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                  na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                  Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                  sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                  Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                  Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                  de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                  Tela de cadastros de moedas.
 * 
 */

package org.freedom.modulos.rep;

import java.util.Vector;

import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;

public class RPMoeda extends FDados {

	private static final long serialVersionUID = 1L;

	private final JRadioGroup<String, String> rgTipo;

	private final JTextFieldPad txtCodMoeda = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private final JTextFieldPad txtSingMoeda = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private final JTextFieldPad txtPlurMoeda = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private final JTextFieldPad txtDecsMoeda = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private final JTextFieldPad txtDecpMoeda = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	public RPMoeda() {

		super( false );
		setTitulo( "Cadastro de Moedas" );
		setAtribos( 50, 50, 425, 270 );

		Vector<String> vValsTipo = new Vector<String>();
		Vector<String> vLabsTipo = new Vector<String>();

		vValsTipo.addElement( "C" );
		vValsTipo.addElement( "I" );
		vLabsTipo.addElement( "Moeda corrente" );
		vLabsTipo.addElement( "Indice de valores" );
		rgTipo = new JRadioGroup<String, String>( 2, 1, vLabsTipo, vValsTipo );
		rgTipo.setVlrString( "C" );

		montaTela();

		setListaCampos( false, "MOEDA", "RP" );
		lcCampos.setQueryInsert( false );

	}

	private void montaTela() {

		adicCampo( txtCodMoeda, 7, 30, 80, 20, "CodMoeda", "Cód.moeda", ListaCampos.DB_PK, true );
		adicCampo( txtSingMoeda, 90, 30, 305, 20, "SingMoeda", "Nome no singular", ListaCampos.DB_SI, true );

		adicCampo( txtPlurMoeda, 7, 80, 200, 20, "PlurMoeda", "Nome no plural", ListaCampos.DB_SI, true );
		adicCampo( txtDecsMoeda, 7, 120, 200, 20, "DecsMoeda", "Decimal no singular", ListaCampos.DB_SI, true );
		adicCampo( txtDecpMoeda, 7, 160, 200, 20, "DecpMoeda", "Decimal no plural", ListaCampos.DB_SI, true );
		adicDB( rgTipo, 220, 80, 175, 50, "TipoMoeda", "Tipo", false );
	}
}
