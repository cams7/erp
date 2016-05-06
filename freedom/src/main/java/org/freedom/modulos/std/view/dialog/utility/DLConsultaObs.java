/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)DLConsultaVenda.java <BR>
 * 
 *         Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *         modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *         na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *         Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *         sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *         Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *         Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *         escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA <BR>
 * <BR>
 * 
 *         Comentários sobre a classe...
 */

package org.freedom.modulos.std.view.dialog.utility;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JScrollPane;

import org.freedom.acao.TabelaSelEvent;
import org.freedom.acao.TabelaSelListener;

public class DLConsultaObs extends FFDialogo implements ActionListener, TabelaSelListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinConsulta = new JPanelPad( 500, 180 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextAreaPad txaObs = new JTextAreaPad();

	private JTablePad tabConsulta = new JTablePad();

	private JScrollPane spnTab = new JScrollPane( tabConsulta );

	private JScrollPane spnObs = new JScrollPane( txaObs );

	private ListaCampos lcCliente = new ListaCampos( this );

	public DLConsultaObs( Component cOrig, ResultSet rs, DbConnection cn ) {

		super( cOrig );
		setTitulo( "Consulta de Observções de clientes!" );
		setAtribos( 300, 350 );

		c.add( spnTab, BorderLayout.CENTER );
		c.add( pinConsulta, BorderLayout.NORTH );

		txtCodCli.setAtivo( false );
		txaObs.setEditable( false );

		pinConsulta.adic( new JLabelPad( "Cód.cli." ), 7, 0, 200, 20 );
		pinConsulta.adic( txtCodCli, 7, 20, 80, 20 );
		pinConsulta.adic( new JLabelPad( "Razão social do cliente" ), 90, 0, 200, 20 );
		pinConsulta.adic( txtRazCli, 90, 20, 187, 20 );
		pinConsulta.adic( new JLabelPad( "Observação:" ), 7, 40, 100, 20 );
		pinConsulta.adic( spnObs, 7, 60, 270, 80 );

		txtCodCli.setNomeCampo( "CodCli" );
		lcCliente.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCliente.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCliente.add( new GuardaCampo( txaObs, "ObsCli", "Observação", ListaCampos.DB_SI, false ) );
		txtRazCli.setListaCampos( lcCliente );
		lcCliente.montaSql( false, "CLIENTE", "VD" );
		lcCliente.setQueryCommit( false );
		lcCliente.setReadOnly( true );
		lcCliente.setConexao( cn );

		tabConsulta.adicColuna( "Código" );
		tabConsulta.adicColuna( "Razão" );

		tabConsulta.setTamColuna( 50, 0 );
		tabConsulta.setTamColuna( 220, 1 );

		tabConsulta.addTabelaSelListener( this );

		carregaGridConsulta( rs );
	}

	private void carregaGridConsulta( ResultSet rs ) {

		try {
			int i = -1; // Essa loucura ehh por que o rs jah foi dado next() fora desta tela.
			do {
				i++;
				tabConsulta.adicLinha();
				tabConsulta.setValor( "" + rs.getInt( "CodCli" ), i, 0 );
				tabConsulta.setValor( ( rs.getString( "RazCli" ) != null ? rs.getString( "RazCli" ) : "" ), i, 1 );
			} while ( rs.next() );
			rs.close();
			tabConsulta.setRowSelectionInterval( 0, 0 );
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao montar a tabela de consulta!\n" + err.getMessage(), true, con, err );
		}
	}

	public void valorAlterado( TabelaSelEvent tevt ) {

		if ( tevt.getTabela() == tabConsulta ) {
			txtCodCli.setVlrString( "" + tabConsulta.getValor( tabConsulta.getLinhaSel(), 0 ) );
			lcCliente.carregaDados();
		}
	}
}
