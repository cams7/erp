package org.freedom.modulos.rep;

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
import org.freedom.modulos.std.view.frame.crud.tabbed.FCliente;

public class RPCopiaPed extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private ListaCampos lcCli = new ListaCampos( this, "" );

	public RPCopiaPed( Component cOrig ) {

		super( cOrig );
		setTitulo( "Cópia de orçamento" );
		setAtribos( 320, 150 );

		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, true ) );
		lcCli.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCli.montaSql( false, "CLIENTE", "RP" );
		lcCli.setReadOnly( true );
		txtCodCli.setTabelaExterna( lcCli, FCliente.class.getCanonicalName() );
		txtCodCli.setFK( true );
		txtCodCli.setNomeCampo( "CodCli" );

		adic( new JLabelPad( "Cód.cli." ), 7, 5, 200, 20 );
		adic( txtCodCli, 7, 25, 80, 20 );
		adic( new JLabelPad( "Razão social do cliente" ), 90, 5, 200, 20 );
		adic( txtRazCli, 90, 25, 200, 20 );

	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			if ( txtCodCli.getText().trim().length() == 0 ) {
				Funcoes.mensagemInforma( this, "O campo cliente está em branco! ! !" );
				txtCodCli.requestFocus();
				return;
			}
		}
		super.actionPerformed( evt );
	}

	public void setConexao( DbConnection cn ) {

		lcCli.setConexao( cn );
	}

	public int[] getValores() {

		int iRet[] = { txtCodCli.getVlrInteger().intValue(), lcCli.getCodFilial() };
		return iRet;
	}
}
