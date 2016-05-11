/**
 * @version 17/05/2005 <BR>
 * @author Setpoint Informática Ltda / Robson Sanchez.
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.fnc <BR>
 *         Classe:
 * @(#)FVendaConsig.java <BR>
 * 
 *                       Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                       modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                       na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                       Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                       sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                       Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                       Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                       de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                       ? <BR>
 * 
 */

package org.freedom.modulos.fnc.view.frame.crud.plain;

import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.fnc.view.dialog.report.DLRCheque;

public class Entrada extends FDados implements PostListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 3, 0 );

	private JTextFieldFK txtNomeCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodBanco = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldFK txtBanco = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNCheque = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtData = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtValor = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private ListaCampos lcTpRecp = new ListaCampos( this, "MB" );

	public Entrada() {

		setTitulo( "CADASTRO DE CHEQUE" );
		setAtribos( 200, 100, 400, 200 );

		lcTpRecp.add( new GuardaCampo( txtCodBanco, "CodBanco", "Cód.Banco", ListaCampos.DB_PK, null, true ) );
		lcTpRecp.add( new GuardaCampo( txtBanco, "NomeBanco", "Banco", ListaCampos.DB_SI, null, false ) );
		lcTpRecp.montaSql( false, "BANCO", "FN" );
		lcTpRecp.setQueryCommit( false );
		lcTpRecp.setReadOnly( true );
		txtCodBanco.setTabelaExterna( lcTpRecp, null );

		adicCampo( txtCodCli, 7, 100, 60, 20, "CodCli", "Cód.Cli.", ListaCampos.DB_SI, false );
		adicDescFK( txtNomeCli, 70, 100, 312, 20, "NOMECLI", "Nome do Cliente" );
		adicCampo( txtCodBanco, 7, 20, 60, 20, "CodBanco", "Cód.Banc.", ListaCampos.DB_PK, null, true );
		adicDescFK( txtBanco, 70, 20, 312, 20, "NomeBanco", "Banco" );
		adicCampo( txtNCheque, 7, 60, 100, 20, "NCheque", "N.Cheque", ListaCampos.DB_PK, true );
		adicCampo( txtValor, 110, 60, 100, 20, "Valor", "Valor", ListaCampos.DB_SI, false );
		adicCampo( txtData, 210, 60, 100, 20, "preData", "Predatado Para", ListaCampos.DB_SI, false );
		setListaCampos( false, "CHEQUE", "SG" );
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		txtCodCli.addActionListener( this );
		setImprimir( true );
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp )
			imprimir( TYPE_PRINT.VIEW );

		else if ( evt.getSource() == btImp )
			imprimir( TYPE_PRINT.PRINT);

		else if ( evt.getSource() == txtCodCli )
			mostracliente();

		super.actionPerformed( evt );
	}

	public void mostracliente() {

		if ( txtCodCli.getVlrInteger().intValue() > 0 ) {
			String sSQL = "SELECT NOMECLI FROM VDCLIENTE WHERE CODCLI=?";
			try {
				PreparedStatement ps = con.prepareStatement( sSQL );
				ps.setInt( 1, txtCodCli.getVlrInteger().intValue() );

				ResultSet rs = ps.executeQuery();

				if ( rs.next() )
					txtNomeCli.setVlrString( rs.getString( "NOMECLI" ) );

				ps.close();

				con.commit();

			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao carregar a tabela de clientes!\n" + err.getMessage() );
				err.printStackTrace();
			}
		}

	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		String ordena, ConsNome;

		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		imp.montaCab();
		imp.setTitulo( "Relatório de ENTRADA DE CHEQUES" );

		DLRCheque dl = new DLRCheque( this );
		dl.setVisible( true );

		if ( dl.OK == false ) {
			dl.dispose();
			return;
		}
		if ( dl.CompData() ) {

			ordena = "sgcheque.DTINS";
			ordena = dl.getValor();
			ConsNome = "'" + dl.getCNome() + "%'";

			String sSQL = "select vdcliente.Nomecli as nome, fnbanco.Nomebanco as banco, sgcheque.NCHEQUE, sgcheque.predata,sgcheque.Dtins, sgcheque.valor " + "from vdcliente, fnbanco, sgcheque where "
					+ "vdcliente.CODCLI=sgcheque.CODCLI and fnbanco.codbanco=sgcheque.codbanco and UPPER(vdcliente.Nomecli) LIKE UPPER( " + ConsNome + " ) " + " and sgcheque.dtins BETWEEN ? AND ? ORDER BY " + ordena;

			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				int iparam = 1;
				double total = 0.00;

				ps = con.prepareStatement( sSQL );

				ps.setDate( iparam++, Funcoes.dateToSQLDate( dl.GetDataini().getVlrDate() ) );
				ps.setDate( iparam++, Funcoes.dateToSQLDate( dl.GetDatafim().getVlrDate() ) );

				rs = ps.executeQuery();
				imp.limpaPags();

				while ( rs.next() ) {

					total += rs.getFloat( "valor" );

					if ( imp.pRow() == 0 ) {
						imp.impCab( 110, false );
						imp.say( imp.pRow() + 0, 0, "" + imp.normal() );
						imp.say( imp.pRow() + 0, 0, "" );
						imp.say( imp.pRow() + 0, 4, "Data" );
						imp.say( imp.pRow() + 0, 15, "Cliente" );
						imp.say( imp.pRow() + 0, 65, "N.Chque" );
						imp.say( imp.pRow() + 0, 3, "Para" );
						imp.say( imp.pRow() + 0, 15, "Valor" );
						imp.say( imp.pRow() + 0, 11, "Banco" );
						imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
						imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "-", 109 ) );
					}
					imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
					imp.say( imp.pRow() + 0, 3, Funcoes.dateToStrDate( rs.getDate( "dtins" ) ) );
					imp.say( imp.pRow() + 0, 3, rs.getString( "nome" ) );
					imp.say( imp.pRow() + 0, 1, StringFunctions.strZero( rs.getString( "NCheque" ), 8 ) );
					imp.say( imp.pRow() + 0, 2, Funcoes.dateToStrDate( rs.getDate( "predata" ) ) );
					imp.say( imp.pRow() + 0, 3, "R$ " + Funcoes.adicEspacosDireita( rs.getString( "valor" ), 10 ) );
					imp.say( imp.pRow() + 0, 2, "| " + rs.getString( "banco" ) );
					if ( imp.pRow() >= linPag ) {
						imp.incPags();
						imp.eject();
					}
				}

				imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
				imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "=", 109 ) );
				imp.say( imp.pRow() + 1, 70, "  Total --->" );
				imp.say( imp.pRow() + 0, 1, "    R$ " + Funcoes.strDecimalToStrCurrency( 1, 2, ( total ) + "" ) );
				imp.eject();

				imp.fechaGravacao();

				con.commit();
				dl.dispose();

				rs.close();
				ps.close();
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro na consulta da tabela de cheques " + err.getMessage() );
			}

			if ( bVisualizar==TYPE_PRINT.VIEW ) {
				imp.preview( this );
			}
			else {
				imp.print();
			}
		}
	}

	public void beforePost( PostEvent pevt ) {

		if ( txtNCheque.getText().trim().length() < 1 ) {
			pevt.cancela();
			Funcoes.mensagemInforma( this, "O Campo, Número do Cheque é requerido! ! !" );
			txtNCheque.requestFocus();
		}
		else if ( txtData.getText().trim().length() < 1 ) {
			pevt.cancela();
			Funcoes.mensagemInforma( this, "Informe para qual data é o cheque; se é à vista, informe a data de hoje." );
			txtData.requestFocus();
		}
		else if ( txtCodCli.getText().trim().length() < 1 ) {
			pevt.cancela();
			Funcoes.mensagemInforma( this, "O Campo,Código do cliente é requerido! ! !" );
			txtNCheque.requestFocus();
		}
		else if ( txtCodBanco.getText().trim().length() < 1 ) {
			pevt.cancela();
			Funcoes.mensagemInforma( this, "O Campo,Código do Banco é requerido! ! !" );
			txtNCheque.requestFocus();
		}

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcTpRecp.setConexao( cn );

	}
}
