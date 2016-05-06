/**
 * @version 15/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.pdv <BR>
 *         Classe:
 * @(#)FGravaMoeda.java <BR>
 * 
 *                      Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                      modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                      na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                      Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                      sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                      Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                      Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                      de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                      Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.pdv;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.AplicativoPDV;

import org.freedom.ecf.app.ControllerECF;

public class FGravaMoeda extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private final JTextFieldPad txtCodMoeda = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private final JTextFieldFK txtSingMoeda = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private final JTextFieldFK txtPlurMoeda = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private final ControllerECF ecf;

	private ListaCampos lcMoeda = new ListaCampos( this, "" );

	public FGravaMoeda() {

		setTitulo( "Ajusta moeda na impressora." );
		setAtribos( 385, 180 );

		ecf = new ControllerECF( AplicativoPDV.getEcfdriver(), AplicativoPDV.getPortaECF(), AplicativoPDV.bModoDemo, AplicativoPDV.getEcflayout() );

		montaListaCampos();
		montaTela();
	}

	private void montaListaCampos() {

		txtCodMoeda.setTipo( JTextFieldPad.TP_STRING, 4, 0 );
		lcMoeda.add( new GuardaCampo( txtCodMoeda, "CodMoeda", "Cód.moeda", ListaCampos.DB_PK, true ) );
		lcMoeda.add( new GuardaCampo( txtSingMoeda, "SingMoeda", "Descrição da moeda", ListaCampos.DB_SI, false ) );
		lcMoeda.add( new GuardaCampo( txtPlurMoeda, "PlurMoeda", "Descrição do plural da moeda", ListaCampos.DB_SI, false ) );
		lcMoeda.montaSql( false, "MOEDA", "FN" );
		lcMoeda.setQueryCommit( false );
		lcMoeda.setReadOnly( true );
		txtCodMoeda.setFK( true );
		txtCodMoeda.setNomeCampo( "CodMoeda" );
		txtCodMoeda.setTabelaExterna( lcMoeda, null );
	}

	private void montaTela() {

		adic( new JLabelPad( "Sigla" ), 7, 5, 50, 15 );
		adic( txtCodMoeda, 7, 20, 50, 20 );
		adic( new JLabelPad( "Nome no singular" ), 60, 5, 147, 15 );
		adic( txtSingMoeda, 60, 20, 147, 20 );
		adic( new JLabelPad( "Nome no plural" ), 210, 5, 150, 15 );
		adic( txtPlurMoeda, 210, 20, 150, 20 );
		adic( new JLabelPad( "<HTML>" + "Este comando so será executado<BR>" + "se não tiver havido movimentação no dia." + "</HTML>" ), 7, 45, 400, 40 );
	}

	private void gravaMoeda() {

		if ( !ecf.programaMoeda( txtCodMoeda.getVlrString(), txtSingMoeda.getVlrString(), txtPlurMoeda.getVlrString() ) ) {
			Funcoes.mensagemErro( this, ecf.getMessageLog() );
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			gravaMoeda();
		}
		super.actionPerformed( evt );
	}

	@ Override
	public void keyPressed( KeyEvent e ) {

		if ( e.getSource() == btOK && e.getKeyCode() == KeyEvent.VK_ENTER ) {
			btOK.doClick();
		}
		else if ( e.getSource() == btCancel && e.getKeyCode() == KeyEvent.VK_ENTER ) {
			btCancel.doClick();
		}
		else {
			super.keyPressed( e );
		}
	}

	public void setConexao( DbConnection cn ) {

		lcMoeda.setConexao( cn );
	}
}
