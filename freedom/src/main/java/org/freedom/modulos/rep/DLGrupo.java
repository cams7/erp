/**
 * @version 05/03/2007 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * @author Alex Rodrigues
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.rep <BR>
 *         Classe:
 * @(#)DLGrupo.java <BR>
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
 *                  Tela auxiliar de cadastro de grupos.
 */

package org.freedom.modulos.rep;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JLabel;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;

public class DLGrupo extends FFDialogo {

	private static final long serialVersionUID = 1L;

	public static final int GRUPO = 1;

	public static final int SUB_GRUPO = 2;

	private final JTextFieldPad txtCodGrupo = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private final JTextFieldPad txtDescGrupo = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtSiglaGrupo = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldPad txtCodSubGrupo = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private final JTextFieldPad txtDescSubGrupo = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtSiglaSubGrupo = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	public DLGrupo( final Component cOrig ) {

		super( cOrig );
		setTitulo( "Grupo" );
		setAtribos( 400, 150 );
	}

	private void montaTela( final int tipo ) {

		adic( new JLabel( "Cód.grupo" ), 7, 10, 100, 20 );
		adic( txtCodGrupo, 7, 30, 100, 20 );
		adic( new JLabel( "Descrição do grupo" ), 110, 10, 200, 20 );
		adic( txtDescGrupo, 110, 30, 200, 20 );
		adic( new JLabel( "Sigla" ), 313, 10, 60, 20 );
		adic( txtSiglaGrupo, 313, 30, 60, 20 );

		if ( tipo == SUB_GRUPO ) {

			adic( new JLabel( "Cód.sub.grupo" ), 7, 50, 100, 20 );
			adic( txtCodSubGrupo, 7, 70, 100, 20 );
			adic( new JLabel( "Descrição do grupo" ), 110, 50, 200, 20 );
			adic( txtDescSubGrupo, 110, 70, 200, 20 );
			adic( new JLabel( "Sigla" ), 313, 50, 60, 20 );
			adic( txtSiglaSubGrupo, 313, 70, 60, 20 );

			setAtribos( 400, 200 );
		}
	}

	private String getCodGrupo() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		String retorno = "";

		try {

			sql = "SELECT COUNT(*) FROM RPGRUPO WHERE CODEMP=? AND CODFILIAL=? AND CODEMPSG=? AND CODFILIALSG=? AND CODSUBGRUP=?";
			ps = con.prepareStatement( sql );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "RPGRUPO" ) );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "RPGRUPO" ) );
			ps.setString( 5, txtCodGrupo.getVlrString().trim() );
			rs = ps.executeQuery();

			if ( rs.next() ) {

				retorno = txtCodGrupo.getVlrString().trim() + StringFunctions.strZero( String.valueOf( ( rs.getInt( 1 ) + 1 ) ), 2 );
			}

			ps.close();

			con.commit();

		} catch ( Exception e ) {
			Funcoes.mensagemErro( this, "Erro ao buscar grupo\n" + e.getMessage() );
			e.printStackTrace();
		}

		return retorno;
	}

	public void setParams( final String[] grupo, final String[] subgrupo, final int tipo ) {

		montaTela( tipo );

		if ( grupo != null ) {

			txtCodGrupo.setVlrString( grupo[ 0 ] );
			txtDescGrupo.setVlrString( grupo[ 1 ] );
			txtSiglaGrupo.setVlrString( grupo[ 2 ] );

			txtCodGrupo.setAtivo( false );
		}
		if ( tipo == SUB_GRUPO ) {

			if ( subgrupo != null ) {

				txtCodSubGrupo.setVlrString( subgrupo[ 0 ] );
				txtDescSubGrupo.setVlrString( subgrupo[ 1 ] );
				txtSiglaSubGrupo.setVlrString( subgrupo[ 2 ] );
			}
			else {
				txtCodSubGrupo.setVlrString( getCodGrupo() );
			}

			txtCodGrupo.setAtivo( false );
			txtDescGrupo.setAtivo( false );
			txtSiglaGrupo.setAtivo( false );

			txtCodSubGrupo.setAtivo( false );
		}
	}

	public String[] getGrupo() {

		String[] sRetorno = new String[ 3 ];
		sRetorno[ 0 ] = txtCodGrupo.getText();
		sRetorno[ 1 ] = txtDescGrupo.getText();
		sRetorno[ 2 ] = txtSiglaGrupo.getText();
		return sRetorno;
	}

	public String[] getSubGrupo() {

		String[] sRetorno = new String[ 3 ];
		sRetorno[ 0 ] = txtCodSubGrupo.getText();
		sRetorno[ 1 ] = txtDescSubGrupo.getText();
		sRetorno[ 2 ] = txtSiglaSubGrupo.getText();
		return sRetorno;
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			if ( txtCodGrupo.getText().trim().length() == 0 ) {
				Funcoes.mensagemInforma( this, "Código em branco! ! ! " );
				txtCodGrupo.requestFocus();
				return;
			}
			else if ( txtDescGrupo.getText().trim().length() == 0 ) {
				Funcoes.mensagemInforma( this, "Descrição em branco! ! !" );
				txtDescGrupo.requestFocus();
				return;
			}
		}
		super.actionPerformed( evt );
	}
}
