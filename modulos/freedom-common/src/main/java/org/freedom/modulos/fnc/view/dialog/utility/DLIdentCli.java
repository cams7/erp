/**
 * @version 07/03/2007 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)DLIdentCli.java <BR>
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
 */

package org.freedom.modulos.fnc.view.dialog.utility;

import java.awt.Component;
import java.util.Vector;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;

public class DLIdentCli extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private final JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 9, 0 );

	private final JTextFieldPad txtRazCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 60, 0 );

	private final JTextFieldPad txtAgenciaCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 9, 0 );

	private final JTextFieldPad txtIdentCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private final JRadioGroup<?, ?> rgSubTipoFebraban;

	private static boolean MRET = false;

	public DLIdentCli( final Component cOrig ) {

		super( cOrig );
		setTitulo( "Identificação do cliente" );
		setAtribos( 395, 230 );

		Vector<String> vVals = new Vector<String>();
		Vector<String> vLabs = new Vector<String>();
		vLabs.add( "Débito em folha" );
		vLabs.add( "Débito em conta" );
		vVals.add( "01" );
		vVals.add( "02" );
		rgSubTipoFebraban = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgSubTipoFebraban.setVlrString( "02" );

		txtCodCli.setAtivo( false );
		txtRazCli.setAtivo( false );

		montaTela();
	}

	private void montaTela() {

		txtAgenciaCli.setRequerido( true );
		txtIdentCli.setRequerido( true );

		adic( new JLabelPad( "Cód.cli." ), 7, 5, 80, 20 );
		adic( txtCodCli, 7, 25, 80, 20 );
		adic( new JLabelPad( "Razão social do cliente" ), 90, 5, 280, 20 );
		adic( txtRazCli, 90, 25, 280, 20 );

		adic( new JLabelPad( "Agência" ), 7, 50, 80, 20 );
		adic( txtAgenciaCli, 7, 70, 80, 20 );
		adic( new JLabelPad( "Identificação" ), 90, 50, 100, 20 );
		adic( txtIdentCli, 90, 70, 100, 20 );

		adic( rgSubTipoFebraban, 7, 100, 363, 30 );
	}

	public static Object[] execIdentCli( final Component cOrig, final Integer codCli, final String razCli, final String agenciaCli, final String identCli, final String subTipo ) {

		Object[] retorno = { new Boolean( false ), "", "", "" };
		DLIdentCli dl = new DLIdentCli( cOrig );

		dl.txtCodCli.setVlrInteger( codCli );
		dl.txtRazCli.setVlrString( razCli );
		dl.txtAgenciaCli.setVlrString( agenciaCli );
		dl.txtIdentCli.setVlrString( identCli );
		dl.rgSubTipoFebraban.setVlrString( subTipo );
		dl.execShow();

		if ( MRET ) {
			retorno[ 0 ] = new Boolean( MRET );
			retorno[ 1 ] = dl.txtAgenciaCli.getVlrString();
			retorno[ 2 ] = dl.txtIdentCli.getVlrString();
			retorno[ 3 ] = dl.rgSubTipoFebraban.getVlrString();
		}

		return retorno;
	}

	@ Override
	public void ok() {

		if ( txtAgenciaCli.getVlrString().trim().equals( "" ) ) {
			Funcoes.mensagemInforma( this, "Campo agência é obrigatório!" );
			txtAgenciaCli.requestFocus();
			return;
		}
		else if ( txtIdentCli.getVlrString().trim().equals( "" ) ) {
			Funcoes.mensagemInforma( this, "Campo identificação é obrigatório!" );
			txtIdentCli.requestFocus();
			return;
		}

		MRET = true;

		super.ok();
	}

	@ Override
	public void cancel() {

		MRET = false;

		super.cancel();
	}

}
