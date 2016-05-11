/**
 * @version 20/07/2002 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FRVencLote.java <BR>
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
 *                     Comentários sobre a classe... Tela para verificação de validade dos Lotes
 * 
 */

package org.freedom.modulos.std.view.frame.report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

public class FRVencLote extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodGrup = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtDescGrup = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodMarca = new JTextFieldPad( JTextFieldPad.TP_STRING, 6, 0 );

	private JTextFieldPad txtDescMarca = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtSiglaMarca = new JTextFieldFK( JTextFieldPad.TP_STRING, 20, 0 );

	private JCheckBoxPad cbLoteZerado = null;

	private JLabelPad lbCodGrup = new JLabelPad( "Cód.grupo" );

	private JLabelPad lbDescCodGrup = new JLabelPad( "Descrição do grupo" );

	private JLabelPad lbCodMarca = new JLabelPad( "Cód.marca" );

	private JLabelPad lbDescCodMarca = new JLabelPad( "Descrição da marca" );

	private ListaCampos lcGrup = new ListaCampos( this );

	private ListaCampos lcMarca = new ListaCampos( this );

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	public FRVencLote() {

		setTitulo( "Relatório de Vencimentos de Lotes" );
		setAtribos( 80, 80, 310, 250 );

		lcGrup.add( new GuardaCampo( txtCodGrup, "CodGrup", "Cód.grupo", ListaCampos.DB_PK, false ) );
		lcGrup.add( new GuardaCampo( txtDescGrup, "DescGrup", "Descrição do gurpo", ListaCampos.DB_SI, false ) );
		lcGrup.montaSql( false, "GRUPO", "EQ" );
		lcGrup.setReadOnly( true );
		txtCodGrup.setTabelaExterna( lcGrup, null );
		txtCodGrup.setFK( true );
		txtCodGrup.setNomeCampo( "CodGrup" );

		lcMarca.add( new GuardaCampo( txtCodMarca, "CodMarca", "Cód.marca", ListaCampos.DB_PK, false ) );
		lcMarca.add( new GuardaCampo( txtDescMarca, "DescMarca", "Descrição da marca", ListaCampos.DB_SI, false ) );
		lcMarca.add( new GuardaCampo( txtSiglaMarca, "SiglaMarca", "Sigla", ListaCampos.DB_SI, false ), "txtSiglaMarca" );
		lcMarca.montaSql( false, "MARCA", "EQ" );
		lcMarca.setReadOnly( true );
		txtCodMarca.setTabelaExterna( lcMarca, null );
		txtCodMarca.setFK( true );
		txtCodMarca.setNomeCampo( "CodMarca" );

		cbLoteZerado = new JCheckBoxPad( "Exibir lotes com saldos zerados?", "S", "N" );
		cbLoteZerado.setVlrString( "N" );

		adic( new JLabelPad( "Período de vencimentos:" ), 7, 0, 250, 20 );
		adic( new JLabelPad( "De: " ), 7, 20, 40, 20 );
		adic( txtDataini, 50, 20, 97, 20 );
		adic( new JLabelPad( " até: " ), 150, 20, 37, 20 );
		adic( txtDatafim, 190, 20, 100, 20 );
		adic( cbLoteZerado, 7, 45, 250, 30 );
		adic( lbCodGrup, 7, 80, 250, 20 );
		adic( txtCodGrup, 7, 100, 80, 20 );
		adic( lbDescCodGrup, 90, 80, 250, 20 );
		adic( txtDescGrup, 90, 100, 200, 20 );
		adic( lbCodMarca, 7, 120, 250, 20 );
		adic( txtCodMarca, 7, 140, 80, 20 );
		adic( lbDescCodMarca, 90, 120, 250, 20 );
		adic( txtDescMarca, 90, 140, 200, 20 );

		GregorianCalendar cPeriodo = new GregorianCalendar();
		txtDataini.setVlrDate( cPeriodo.getTime() );
		cPeriodo.add( Calendar.MONTH, 3 );
		txtDatafim.setVlrDate( cPeriodo.getTime() );
	}

	private boolean comRef() {

		boolean bRetorno = false;
		String sSQL = "SELECT USAREFPROD FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			rs = ps.executeQuery();
			if ( rs.next() )
				if ( rs.getString( "UsaRefProd" ).trim().equals( "S" ) )
					bRetorno = true;

			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage() );
			err.printStackTrace();
		} finally {
			sSQL = null;
			ps = null;
			rs = null;
		}
		return bRetorno;
	}

	/**
	 * Impressão. <BR>
	 * Imprime um relatório para o usuário.
	 * 
	 */

	public void imprimir( TYPE_PRINT bVisualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;
		String sWhere = "";
		String sFiltros1 = "";
		String sFiltros2 = "";
		String sCodProd;
		ImprimeOS imp = null;
		int linPag = 0;

		if ( !txtCodGrup.getVlrString().trim().equals( "" ) ) {
			sWhere += " AND P.CODGRUP LIKE '" + txtCodGrup.getText().trim() + "%'";
			sFiltros1 = "GRUPO: " + txtDescGrup.getVlrString().trim();
		}
		if ( txtCodMarca.getText().trim().length() > 0 ) {
			sWhere += " AND P.CODMARCA = '" + txtCodMarca.getText().trim() + "'";
			sFiltros1 += ( sFiltros1.equals( "" ) ? "" : " / " ) + "MARCA: " + txtDescMarca.getVlrString().trim();
		}
		if ( cbLoteZerado.getVlrString().equals( "N" ) ) {
			sWhere += " AND L.SLDLIQLOTE >0 ";
			sFiltros1 += ( sFiltros1.equals( "" ) ? "" : " / " ) + "Produtos com saldos";
		}

		if ( !txtDataini.getText().trim().equals( "" ) && !txtDatafim.getText().trim().equals( "" ) ) {
			sWhere += " AND L.VENCTOLOTE BETWEEN '" + Funcoes.dateToStrDB( txtDataini.getVlrDate() ) + "' AND '" + Funcoes.dateToStrDB( txtDatafim.getVlrDate() ) + "' ";
			sFiltros2 = "PERIODO DE " + txtDataini.getVlrString() + " ATE " + txtDatafim.getVlrString();
		}

		if ( comRef() )
			sCodProd = "REFPROD";
		else
			sCodProd = "CODPROD";

		try {

			imp = new ImprimeOS( "", con );
			linPag = imp.verifLinPag() - 1;
			imp.montaCab();
			imp.setTitulo( "Relatorio de Vencimentos de Lotes" );
			imp.limpaPags();

			sSQL = "SELECT P." + sCodProd + ",P.DESCPROD,L.CODLOTE,L.VENCTOLOTE,L.SLDLIQLOTE " + "FROM EQPRODUTO P, EQLOTE L " + "WHERE L.CODEMP=? AND L.CODFILIAL=? " + "AND L.CODEMP=P.CODEMP AND L.CODFILIAL=P.CODFILIAL AND L.CODPROD=P.CODPROD " + sWhere + "ORDER BY VENCTOLOTE";

			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQLOTE" ) );
			rs = ps.executeQuery();
			while ( rs.next() ) {
				if ( imp.pRow() == 0 ) {
					imp.impCab( 136, true );
					imp.say( imp.pRow(), 0, imp.comprimido() );
					if ( !sFiltros1.trim().equals( "" ) ) {
						imp.say( imp.pRow(), 0, "|" );
						imp.say( imp.pRow(), 67 - ( sFiltros1.length() / 2 ), sFiltros1 );
						imp.say( imp.pRow(), 135, "|" );
						imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					}
					if ( !sFiltros2.trim().equals( "" ) ) {
						imp.say( imp.pRow(), 0, "|" );
						imp.say( imp.pRow(), 67 - ( sFiltros2.length() / 2 ), sFiltros2 );
						imp.say( imp.pRow(), 135, "|" );
						imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					}
					imp.say( imp.pRow(), 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "| Código" );
					imp.say( imp.pRow(), 16, "| Descrição" );
					imp.say( imp.pRow(), 69, "| Lote" );
					imp.say( imp.pRow(), 85, "| Vencimento" );
					imp.say( imp.pRow(), 98, "| Saldo" );
					imp.say( imp.pRow(), 135, "|" );
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
				}

				imp.say( imp.pRow() + 1, 0, imp.comprimido() );
				imp.say( imp.pRow(), 0, "| " + ( sCodProd.equals( "REFPROD" ) ? rs.getString( "REFPROD" ) : Funcoes.alinhaDir( rs.getInt( "CODPROD" ), 13 ) ) );
				imp.say( imp.pRow(), 16, "| " + Funcoes.copy( rs.getString( "DESCPROD" ), 50 ));
				imp.say( imp.pRow(), 69, "| " + rs.getString( "CODLOTE" ) );
				imp.say( imp.pRow(), 85, "| " + StringFunctions.sqlDateToStrDate( rs.getDate( "VENCTOLOTE" ) ) );
				imp.say( imp.pRow(), 98, "| " + Funcoes.strDecimalToStrCurrency( 15, 1, rs.getString( "SLDLIQLOTE" ) ) );
				imp.say( imp.pRow(), 135, "|" );

				if ( imp.pRow() >= linPag ) {
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
					imp.incPags();
					imp.eject();
				}
			}

			imp.say( imp.pRow() + 1, 0, imp.comprimido() );
			imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );

			imp.eject();
			imp.fechaGravacao();

			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao consultar a tabela PRODUTOS!\n" + err.getMessage(), true, con, err );
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
			sWhere = null;
			sFiltros1 = null;
			sFiltros2 = null;
			sCodProd = null;
			System.gc();
		}

		if ( bVisualizar==TYPE_PRINT.VIEW )
			imp.preview( this );
		else
			imp.print();
	}

	/**
	 * Ajusta conexão da tela. <BR>
	 * Adiciona a conexão vigente a este formulário.
	 * 
	 * @param cn
	 *            : Conexao valida e ativa que será repassada e esta tela.
	 * @see org.freedom.library.swing.frame.FFilho#setConexao
	 */
	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcGrup.setConexao( cn );
		lcMarca.setConexao( cn );
	}
}
