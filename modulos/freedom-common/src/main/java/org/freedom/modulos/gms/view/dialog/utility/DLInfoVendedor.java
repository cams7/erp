/**
 * @version 04/06/2010 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.gms <BR>
 *         Classe:
 * @(#)DLInfoVendedor.java <BR>
 * 
 *                         Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                         modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                         na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                         Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                         sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                         Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                         Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                         de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                         Dialog para entrada de código do vendedor...
 */

package org.freedom.modulos.gms.view.dialog.utility;

import java.awt.Component;
import java.awt.event.ActionEvent;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;

public class DLInfoVendedor extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JLabelPad lbCodVend = new JLabelPad( "Cód.Comis." );

	private JLabelPad lbNomeComis = new JLabelPad( "Nome do comissionado" );

	private ListaCampos lcComis = new ListaCampos( this, "VD" );

	public DLInfoVendedor( Component cOrig, DbConnection cn ) {

		super( cOrig );

		setConexao( cn );
		setTitulo( "Informe o comissionado" );
		setAtribos( 370, 140 );

		montaListaCampos();

		txtCodVend.setRequerido( true );

		adic( lbCodVend, 7, 0, 80, 20 );
		adic( txtNomeVend, 7, 20, 80, 20 );

		adic( lbNomeComis, 90, 0, 250, 20 );
		adic( txtNomeVend, 90, 20, 250, 20 );

		txtCodVend.requestFocus();
	}

	private void montaListaCampos() {

		lcComis.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.comis.", ListaCampos.DB_PK, txtNomeVend, true ) );
		lcComis.add( new GuardaCampo( txtNomeVend, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, false ) );
		lcComis.setWhereAdic( "ATIVOCOMIS='S' " );
		lcComis.montaSql( false, "VENDEDOR", "VD" );
		lcComis.setQueryCommit( false );
		lcComis.setReadOnly( true );
		txtCodVend.setTabelaExterna( lcComis, null );
		txtCodVend.setFK( true );
		txtCodVend.setNomeCampo( "codvend" );

	}

	public Integer getValor() {

		return txtCodVend.getVlrInteger();
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			if ( txtCodVend.getText().trim().length() == 0 ) {
				Funcoes.mensagemInforma( this, "Informe o comissionado!" );
				txtCodVend.requestFocus();

			}
			else {
				super.actionPerformed( evt );
			}
		}
		else if ( evt.getSource() == btCancel ) {
			super.actionPerformed( evt );
		}

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcComis.setConexao( cn );
	}

}
