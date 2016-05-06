/**
 * @version 28/05/2005 <BR>
 * @author Setpoint Informática Ltda / Robson Sanchez.
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)DLRCheque.java <BR>
 * 
 *                    Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                    modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                    na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                    Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                    sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                    Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                    Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                    de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                    ? <BR>
 * 
 */

package org.freedom.modulos.fnc.view.frame.crud.plain;

import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JScrollPane;

import org.freedom.acao.JComboBoxEvent;
import org.freedom.acao.JComboBoxListener;
import org.freedom.acao.PostEvent;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.fnc.view.dialog.report.DLRRetorno;
import org.freedom.modulos.fnc.view.dialog.report.DLRSCheque;

public class FNCRetorno extends FDados implements JComboBoxListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodBanco = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 7, 0 );

	private JTextFieldFK txtBanco = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNcheque = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtValor = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtData = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldFK txtCodCliente = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 7, 0 );

	private JTextFieldFK txtCliente = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtTsaida = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtDesc = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNsaida = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 3, 0 );

	private JComboBoxPad cbCodRetorno = null;

	private JTextAreaPad txtDescricao = new JTextAreaPad();

	private JScrollPane SCodRetorno = new JScrollPane( txtDescricao );

	private JLabelPad lbDescCod = new JLabelPad( "Descrição do Código de Retorno:" );

	private Vector<String> vStrTipo = new Vector<String>();

	private ListaCampos lcTBanco = new ListaCampos( this, "TR" );

	private ListaCampos lcTCheque = new ListaCampos( this, "TR" );

	public FNCRetorno() {

		setTitulo( "RETORNO DE CHEQUE" );
		setAtribos( 200, 60, 350, 340 );

		// / Prepara o combobox ////////////////////
		vStrTipo.addElement( "<Selecione>" );
		vStrTipo.addElement( "11" );
		vStrTipo.addElement( "12" );
		vStrTipo.addElement( "13" );
		vStrTipo.addElement( "14" );
		vStrTipo.addElement( "20" );
		vStrTipo.addElement( "21" );
		vStrTipo.addElement( "22" );
		vStrTipo.addElement( "23" );
		vStrTipo.addElement( "24" );
		vStrTipo.addElement( "25" );
		vStrTipo.addElement( "26" );
		vStrTipo.addElement( "27" );
		vStrTipo.addElement( "28" );
		vStrTipo.addElement( "29" );
		vStrTipo.addElement( "30" );
		vStrTipo.addElement( "31" );
		vStrTipo.addElement( "32" );
		vStrTipo.addElement( "33" );
		vStrTipo.addElement( "34" );
		vStrTipo.addElement( "35" );
		vStrTipo.addElement( "36" );
		vStrTipo.addElement( "37" );
		vStrTipo.addElement( "40" );
		vStrTipo.addElement( "41" );
		vStrTipo.addElement( "42" );
		vStrTipo.addElement( "43" );
		vStrTipo.addElement( "44" );
		vStrTipo.addElement( "45" );
		vStrTipo.addElement( "46" );
		vStrTipo.addElement( "47" );
		vStrTipo.addElement( "48" );
		vStrTipo.addElement( "49" );
		vStrTipo.addElement( "99" );

		cbCodRetorno = new JComboBoxPad( vStrTipo, vStrTipo, JComboBoxPad.TP_STRING, 6, 0 );

		// /////////////////////////////////////////

		lcTBanco.add( new GuardaCampo( txtCodBanco, "CodBanco", "Cód.Banco", ListaCampos.DB_PK, true ) );
		lcTBanco.add( new GuardaCampo( txtBanco, "NomeBanco", "Banco", ListaCampos.DB_SI, false ) );
		lcTBanco.montaSql( false, "BANCO", "FN" );
		lcTBanco.setQueryCommit( false );
		lcTBanco.setReadOnly( true );
		txtCodBanco.setTabelaExterna( lcTBanco, FBanco.class.getCanonicalName() );

		lcTCheque.add( new GuardaCampo( txtCodBanco, "CodBanco", "Cód.Banco", ListaCampos.DB_PK, true ) );
		lcTCheque.add( new GuardaCampo( txtNcheque, "NCheque", "N.Cheque", ListaCampos.DB_PK, true ) );
		lcTCheque.add( new GuardaCampo( txtData, "DTINS", "Data/Saída", ListaCampos.DB_SI, false ) );
		lcTCheque.add( new GuardaCampo( txtDesc, "DESCRICAO", "Descrição", ListaCampos.DB_SI, false ) );
		lcTCheque.montaSql( false, "SCHEQUE", "SG" );
		lcTCheque.setQueryCommit( false );
		lcTCheque.setReadOnly( true );
		txtNcheque.setTabelaExterna( lcTCheque, null );

		adicCampo( txtNsaida, 7, 20, 40, 20, "NSAIDA", "N.Saíd.", ListaCampos.DB_PK, null, true );
		adicCampo( txtCodBanco, 51, 20, 70, 20, "CodBanco", "Cód.Banc.", ListaCampos.DB_PK, true );
		adicDescFK( txtBanco, 125, 20, 204, 20, "NomeBanco", "Banco" );
		adicCampo( txtNcheque, 7, 60, 100, 20, "Ncheque", "N.Cheque", ListaCampos.DB_PK, true );
		adicDescFK( txtValor, 110, 60, 100, 20, "Valor", "Valor" );
		adicDescFK( txtData, 213, 60, 100, 20, "Datas", "Data/Saída" );
		adicDescFK( txtCodCliente, 7, 100, 100, 20, "codcli", "Cod.Cli" );
		adicDescFK( txtCliente, 110, 100, 219, 20, "Cliente", "Cliente" );
		adicDescFK( txtTsaida, 7, 140, 100, 20, "TSAIDA", "Tipo de Saída" );
		adicDescFK( txtDesc, 110, 140, 219, 20, "DESCRICAO", "Descrição" );
		adicDB( cbCodRetorno, 7, 180, 100, 25, "CODIGOR", "Código/Retorno", false );

		adic( lbDescCod, 7, 207, 200, 15 );
		adic( SCodRetorno, 7, 223, 320, 45 );
		setListaCampos( false, "RCHEQUE", "SG" );

		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		txtNcheque.addActionListener( this );
		cbCodRetorno.addComboBoxListener( this );
		txtDescricao.setEditable( false );
		setImprimir( true );

	}

	public void beforePost( PostEvent pevt ) {

		if ( txtNcheque.getText().trim().length() < 1 ) {
			pevt.cancela();
			Funcoes.mensagemInforma( this, "O Campo, Número do Cheque é requerido! ! !" );
			txtNcheque.requestFocus();
		}
		else if ( ( cbCodRetorno.getVlrString().length() < 1 ) | ( cbCodRetorno.getVlrString().equals( "<Selecione>" ) ) ) {
			pevt.cancela();
			Funcoes.mensagemInforma( this, " O campo, Código de Retorno é requerido! !" );
			txtNcheque.requestFocus();
		}
		else if ( pevt.getEstado() != ListaCampos.LCS_EDIT ) {

			if ( !ConfereSaida() ) {
				pevt.cancela();
				txtNsaida.requestFocus();
			}
			else
				MudaStatus();
		}
		else
			MudaStatus();

	}

	private void MudaStatus() {

		String sSQL = "UPDATE SGCHEQUE SET STATUS=? WHERE CODBANCO=? AND NCHEQUE=?";

		try {
			int iparam = 1;
			int valor = 0;

			PreparedStatement ps = con.prepareStatement( sSQL );

			ps.setInt( iparam++, valor );
			ps.setInt( iparam++, txtCodBanco.getVlrInteger().intValue() );
			ps.setInt( iparam++, txtNcheque.getVlrInteger().intValue() );

			ps.execute();

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao atualizar o status do cheque!\n" + err.getMessage() );
			err.printStackTrace();
		}

	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == txtNcheque ) {
			mostracampos();
		}

		if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
		else if ( evt.getSource() == btImp )
			imprimir( TYPE_PRINT.PRINT);

		super.actionPerformed( evt );
	}

	public void valorAlterado( JComboBoxEvent evt ) {

		if ( evt.getComboBoxPad() == cbCodRetorno )
			DescRetorno();

	}

	public boolean ConfereSaida() {

		boolean result = true;

		String sSQL = "SELECT COUNT(*) AS SOMA FROM SGRCHEQUE WHERE CODBANCO=? AND NCHEQUE=?";
		try {
			int iparam = 1;
			int qt = 0;
			PreparedStatement ps = con.prepareStatement( sSQL );

			ps.setInt( iparam++, txtCodBanco.getVlrInteger().intValue() );
			ps.setInt( iparam++, txtNcheque.getVlrInteger().intValue() );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() )
				qt = rs.getInt( "SOMA" ) + 1;

			if ( txtNsaida.getVlrInteger().intValue() != qt ) {
				Funcoes.mensagemErro( this, "O número correto de saída do cheque é : " + qt );
				result = false;
			}
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao contar o número de Retorno de cheques!\n" + err.getMessage() );
			err.printStackTrace();
		}

		return result;
	}

	public void mostracampos() {

		if ( txtNcheque.getVlrInteger().intValue() > 0 ) {
			String sSQL = "SELECT TSAIDA FROM SGSCHEQUE WHERE CODBANCO=? and NCHEQUE=? ";
			try {
				PreparedStatement ps = con.prepareStatement( sSQL );
				ps.setInt( 1, txtCodBanco.getVlrInteger().intValue() );
				ps.setInt( 2, txtNcheque.getVlrInteger().intValue() );

				ResultSet rs = ps.executeQuery();

				DLRSCheque dl = new DLRSCheque( this );

				if ( rs.next() )
					txtTsaida.setVlrString( dl.SetTipo( rs.getInt( "TSAIDA" ) ) );

				ps.close();

				con.commit();

			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao carregar a tabela de Entrada de Cheque!\n" + err.getMessage() );
				err.printStackTrace();
			}
		}

		if ( txtTsaida.getVlrString().length() > 0 ) {
			String sSQL = "SELECT CODCLI, VALOR FROM SGCHEQUE WHERE CODBANCO=? AND NCHEQUE=? ";
			try {
				PreparedStatement ps = con.prepareStatement( sSQL );
				ps.setInt( 1, txtCodBanco.getVlrInteger().intValue() );
				ps.setInt( 2, txtNcheque.getVlrInteger().intValue() );

				ResultSet rs = ps.executeQuery();

				if ( rs.next() ) {
					txtCodCliente.setVlrString( rs.getString( "CODCLI" ) );
					txtValor.setVlrString( rs.getString( "VALOR" ) );
				}

				ps.close();

				con.commit();

			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao carregar a tabela de Entrada de Cheque!\n" + err.getMessage() );
				err.printStackTrace();
			}
		}
		if ( txtCodCliente.getVlrInteger().intValue() > 0 ) {
			String sSQL = "SELECT NOMECLI FROM VDCLIENTE WHERE CODCLI=? ";
			try {
				PreparedStatement ps = con.prepareStatement( sSQL );
				ps.setInt( 1, txtCodCliente.getVlrInteger().intValue() );

				ResultSet rs = ps.executeQuery();

				if ( rs.next() )
					txtCliente.setVlrString( rs.getString( "NOMECLI" ) );

				ps.close();

				con.commit();

			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao carregar a tabela de Entrada de Cheque!\n" + err.getMessage() );
				err.printStackTrace();
			}
		}

	}

	public void DescRetorno() {

		if ( ( cbCodRetorno.getVlrString().length() > 1 ) && ! ( cbCodRetorno.getVlrString().equals( "<Selecione>" ) ) ) {

			String sSQL = "SELECT DESCRICAO FROM FNTABELAC WHERE CODIGO=? ";
			try {
				PreparedStatement ps = con.prepareStatement( sSQL );

				ps.setString( 1, cbCodRetorno.getVlrString() );

				ResultSet rs = ps.executeQuery();

				if ( rs.next() )
					txtDescricao.setVlrString( rs.getString( "DESCRICAO" ) );

				ps.close();

				con.commit();

			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao carregar a tabela de Código de Retorno!\n" + err.getMessage() );
				err.printStackTrace();
			}
		}

	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		String ordena, ConsNome, CodigoR;

		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		imp.montaCab();
		imp.setTitulo( "Relatório de RETORNO DE CHEQUE" );

		DLRRetorno dl = new DLRRetorno( this );
		dl.setVisible( true );

		if ( dl.OK == false ) {
			dl.dispose();
			return;
		}
		if ( ( dl.CompData() ) && ( dl.CompRetorno() ) ) {

			ordena = "sgrheque.DTINS";
			ordena = dl.getValor();
			ConsNome = "'" + dl.getCNome() + "%'";

			if ( dl.getRetorno().length() < 3 )
				CodigoR = " and sgrcheque.CODIGOR=" + "'" + dl.getRetorno() + "'";
			else
				CodigoR = "";

			String sSQL = " select vdcliente.Nomecli as nome, fnbanco.Nomebanco as banco, " + " sgcheque.NCHEQUE,  sgcheque.valor, " + " sgcheque.codbanco, sgcheque.codcli, sgrcheque.NCHEQUE, sgrcheque.CODIGOR, " + " sgrcheque.CODBANCO, sgrcheque.DTINS "
					+ " from vdcliente, fnbanco, sgcheque, sgrcheque where " + " vdcliente.CODCLI=sgcheque.CODCLI and " + " fnbanco.codbanco=sgcheque.codbanco and UPPER(vdcliente.Nomecli) LIKE UPPER( " + ConsNome + " ) "

					+ " and sgcheque.NCHEQUE=sgrcheque.NCHEQUE and sgcheque.CODBANCO=sgrcheque.CODBANCO " + CodigoR + " and sgrcheque.dtins BETWEEN ? AND ? ORDER BY " + ordena;

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
						imp.impCab( 111, false );
						imp.say( imp.pRow() + 0, 0, "" + imp.normal() );
						imp.say( imp.pRow() + 0, 0, "" );
						imp.say( imp.pRow() + 0, 3, "Data/Ret." );
						imp.say( imp.pRow() + 0, 4, "Cod.Ret." );
						imp.say( imp.pRow() + 0, 2, "Cliente" );
						imp.say( imp.pRow() + 0, 53, "N.Cheque" );
						imp.say( imp.pRow() + 0, 3, "Valor" );
						imp.say( imp.pRow() + 0, 18, "Banco" );
						imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
						imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "-", 110 ) );
					}
					imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
					imp.say( imp.pRow() + 0, 3, Funcoes.dateToStrDate( rs.getDate( "dtins" ) ) );
					imp.say( imp.pRow() + 0, 5, Funcoes.adicEspacosDireita( rs.getString( "CODIGOR" ), 5 ) );
					imp.say( imp.pRow() + 0, 3, rs.getString( "nome" ) );
					imp.say( imp.pRow() + 0, 2, StringFunctions.strZero( rs.getString( "NCheque" ), 8 ) );
					imp.say( imp.pRow() + 0, 3, "R$ " + Funcoes.adicEspacosDireita( rs.getString( "valor" ), 10 ) );
					imp.say( imp.pRow() + 0, 2, "| " + rs.getString( "banco" ) );
					if ( imp.pRow() >= linPag ) {
						imp.incPags();
						imp.eject();
					}
				}

				imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
				imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "=", 110 ) );
				imp.say( imp.pRow() + 1, 69, "  Total --->" );
				imp.say( imp.pRow() + 0, 1, "    R$ " + Funcoes.strDecimalToStrCurrency( 1, 2, ( total ) + "" ) );
				imp.eject();

				imp.fechaGravacao();

				con.commit();
				dl.dispose();

				rs.close();
				ps.close();
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro na consulta, na tabela de cheques " + err.getMessage() );
			}

			if ( bVisualizar==TYPE_PRINT.VIEW ) {
				imp.preview( this );
			}
			else {
				imp.print();
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcTBanco.setConexao( cn );
		lcTCheque.setConexao( cn );

	}

}
