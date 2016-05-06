/**
 * @version 03/12/2003 <BR>
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
 *                     Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.std.view.frame.report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

public class FRSaldoLote extends FRelatorio {

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

	private JLabelPad lbDescCodMarca = new JLabelPad( "Ddescrição da marca" );

	private ListaCampos lcGrup = new ListaCampos( this );

	private ListaCampos lcMarca = new ListaCampos( this );

	private JRadioGroup<?, ?> rgOrdem = null;

	private Vector<String> vLabs = new Vector<String>( 2 );

	private Vector<String> vVals = new Vector<String>( 2 );

	public FRSaldoLote() {

		setTitulo( "Relatório de Saldos de Lotes" );
		setAtribos( 80, 80, 350, 235 );

		lcGrup.add( new GuardaCampo( txtCodGrup, "CodGrup", "Cód.grupo", ListaCampos.DB_PK, false ) );
		lcGrup.add( new GuardaCampo( txtDescGrup, "DescGrup", "Descrição do grupo", ListaCampos.DB_SI, false ) );
		lcGrup.montaSql( false, "GRUPO", "EQ" );
		lcGrup.setReadOnly( true );
		txtCodGrup.setTabelaExterna( lcGrup, null );
		txtCodGrup.setFK( true );
		txtCodGrup.setNomeCampo( "CodGrup" );

		lcMarca.add( new GuardaCampo( txtCodMarca, "CodMarca", "Cód.marca", ListaCampos.DB_PK, false ) );
		lcMarca.add( new GuardaCampo( txtDescMarca, "DescMarca", "Descrição da marca", ListaCampos.DB_SI, false ) );
		lcMarca.add( new GuardaCampo( txtSiglaMarca, "SiglaMarca", "Sigla", ListaCampos.DB_SI, false ) );
		lcMarca.montaSql( false, "MARCA", "EQ" );
		lcMarca.setReadOnly( true );
		txtCodMarca.setTabelaExterna( lcMarca, null );
		txtCodMarca.setFK( true );
		txtCodMarca.setNomeCampo( "CodMarca" );

		cbLoteZerado = new JCheckBoxPad( "Exibir lotes com saldos zerados?", "S", "N" );
		cbLoteZerado.setVlrString( "N" );

		vLabs.addElement( "Código" );
		vLabs.addElement( "Descrição" );
		vVals.addElement( "C" );
		vVals.addElement( "D" );
		rgOrdem = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgOrdem.setVlrString( "D" );

		adic( lbCodGrup, 7, 0, 250, 20 );
		adic( txtCodGrup, 7, 20, 80, 20 );
		adic( lbDescCodGrup, 90, 0, 250, 20 );
		adic( txtDescGrup, 90, 20, 200, 20 );
		adic( lbCodMarca, 7, 40, 250, 20 );
		adic( txtCodMarca, 7, 60, 80, 20 );
		adic( lbDescCodMarca, 90, 40, 250, 20 );
		adic( txtDescMarca, 90, 60, 200, 20 );
		adic( new JLabelPad( "Ordem:" ), 7, 80, 250, 20 );
		adic( rgOrdem, 7, 100, 283, 30 );
		adic( cbLoteZerado, 7, 140, 250, 20 );
	}

	private boolean getUsaRef() {

		boolean bRetorno = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = "SELECT USAREFPROD FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
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
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage(), true, con, err );
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}
		return bRetorno;
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
		String sCab = "";
		String sCampo = null;
		String sOrdem = rgOrdem.getVlrString();
		String sOrdenado = null;
		ImprimeOS imp = null;
		int linPag = 0;
		int iCodProdAnt = -1;
		double dConta = 0;
		boolean bComRef = false;

		if ( txtCodGrup.getText().trim().length() > 0 ) {
			sWhere += " AND P.CODGRUP LIKE '" + txtCodGrup.getText().trim() + "%'";
			String sTmp = "GRUPO: " + txtDescGrup.getText().trim();
			sTmp = StringFunctions.replicate( " ", 67 - ( sTmp.length() / 2 ) ) + sTmp;
			sCab += sTmp + StringFunctions.replicate( " ", 133 - sTmp.length() ) + " |";
		}
		if ( txtCodMarca.getText().trim().length() > 0 ) {
			sWhere += " AND P.CODMARCA = '" + txtCodMarca.getText().trim() + "'";
			String sTmp = "MARCA: " + txtDescMarca.getText().trim();
			sTmp = StringFunctions.replicate( " ", 67 - ( sTmp.length() / 2 ) ) + sTmp;
			sCab += sTmp + StringFunctions.replicate( " ", 133 - sTmp.length() ) + " |";
		}
		if ( cbLoteZerado.getVlrString().equals( "N" ) ) {
			sWhere += " AND L.SLDLIQLOTE > 0";
			String sTmp = "LOTES COM SALDO";
			sTmp = StringFunctions.replicate( " ", 67 - ( sTmp.length() / 2 ) ) + sTmp;
			sCab += sTmp + StringFunctions.replicate( " ", 133 - sTmp.length() ) + " |";
		}
		if ( sOrdem.equals( "C" ) ) {
			if ( bComRef ) {
				sOrdem = "P.REFPROD";
				sOrdenado = "ORDENADO POR REFERENCIA";
			}
			else {
				sOrdem = "P.CODPROD";
				sOrdenado = "ORDENADO POR CODIGO";
			}
			sOrdenado = StringFunctions.replicate( " ", 67 - ( sOrdenado.length() / 2 ) ) + sOrdenado;
			sOrdenado += StringFunctions.replicate( " ", 133 - sOrdenado.length() ) + " |";
		}
		else {
			sOrdem = "P.DESCPROD";
			sOrdenado = "ORDENADO POR DESCRICAO";
			sOrdenado = StringFunctions.replicate( " ", 67 - ( sOrdenado.length() / 2 ) ) + sOrdenado;
			sOrdenado += StringFunctions.replicate( " ", 133 - sOrdenado.length() ) + " |";
		}

		bComRef = getUsaRef();
		sCampo = bComRef ? "REFPROD" : "CODPROD";

		try {
			imp = new ImprimeOS( "", con );
			linPag = imp.verifLinPag() - 1;
			imp.montaCab();
			imp.setTitulo( "Relatorio Saldos de Lotes" );
			if ( sCab.length() > 0 )
				imp.addSubTitulo( sCab );
			imp.addSubTitulo( sOrdenado );
			imp.limpaPags();

			sSQL = "SELECT P.CODPROD,P." + sCampo + ",P.DESCPROD,L.CODLOTE,L.VENCTOLOTE,L.SLDLIQLOTE " + "FROM EQPRODUTO P, EQLOTE L " + "WHERE L.CODPROD = P.CODPROD" + sWhere + " ORDER BY " + sOrdem;

			ps = con.prepareStatement( sSQL );
			rs = ps.executeQuery();
			while ( rs.next() ) {
				if ( imp.pRow() >= linPag ) {
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
					imp.incPags();
					imp.eject();
				}
				if ( imp.pRow() == 0 ) {
					imp.impCab( 136, true );
					imp.say( imp.pRow(), 0, imp.comprimido() );
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
				if ( rs.getInt( "CodProd" ) != iCodProdAnt && iCodProdAnt != -1 ) {
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|" );
					imp.say( imp.pRow(), 75, "Saldo do produto: " );
					imp.say( imp.pRow(), 98, "| " + Funcoes.strDecimalToStrCurrency( 15, 1, "" + dConta ) );
					imp.say( imp.pRow(), 135, "|" );
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
					dConta = 0;
				}
				imp.say( imp.pRow() + 1, 0, imp.comprimido() );
				imp.say( imp.pRow(), 0, "| " + ( sCampo.equals( "REFPROD" ) ? rs.getString( "REFPROD" ) : Funcoes.alinhaDir( rs.getInt( "CODPROD" ), 13 ) ) );
				imp.say( imp.pRow(), 16, "| " + rs.getString( "DESCPROD" ).substring( 0,50 ) );
				imp.say( imp.pRow(), 69, "| " + rs.getString( "CODLOTE" ) );
				imp.say( imp.pRow(), 85, "| " + StringFunctions.sqlDateToStrDate( rs.getDate( "VENCTOLOTE" ) ) );
				imp.say( imp.pRow(), 98, "| " + Funcoes.strDecimalToStrCurrency( 15, 1, rs.getString( "SLDLIQLOTE" ) ) );
				imp.say( imp.pRow(), 135, "|" );
				dConta += rs.getDouble( "SLDLIQLOTE" );
				iCodProdAnt = rs.getInt( "CodProd" );
			}

			imp.say( imp.pRow() + 1, 0, imp.comprimido() );
			imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "=", 133 ) + "+" );
			imp.say( imp.pRow() + 1, 0, imp.comprimido() );
			imp.say( imp.pRow(), 0, "|" );
			imp.say( imp.pRow(), 75, "Saldo do produto: " );
			imp.say( imp.pRow(), 98, "| " + Funcoes.strDecimalToStrCurrency( 15, 1, "" + dConta ) );
			imp.say( imp.pRow(), 135, "|" );
			imp.say( imp.pRow() + 1, 0, imp.comprimido() );
			imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "=", 133 ) + "+" );
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
			sCab = null;
			sCampo = null;
			sOrdem = null;
			sOrdenado = null;
		}

		if ( bVisualizar==TYPE_PRINT.VIEW )
			imp.preview( this );
		else
			imp.print();
	}
}
