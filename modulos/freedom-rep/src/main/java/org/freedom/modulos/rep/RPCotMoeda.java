/**
 * @version 02/2007 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * @author Alex Rodrigues<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.rep <BR>
 *         Classe:
 * @(#)RPCotMoeda.java <BR>
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
 *                     Tela de cadastros de cotações de valores das moedas cadastradas no sistema.
 * 
 */

package org.freedom.modulos.rep;

import java.awt.BorderLayout;
import java.awt.Dimension;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDados;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

import javax.swing.JScrollPane;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;

public class RPCotMoeda extends FDados implements InsertListener, CarregaListener {

	private static final long serialVersionUID = 1L;

	private final JPanelPad panelCotacao = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad panelCampos = new JPanelPad();

	private final JPanelPad panelTabela = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JTextFieldPad txtDataCot = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtValor = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private final JTextFieldPad txtCodMoeda = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private final JTextFieldFK txtSingMoeda = new JTextFieldFK( JTextFieldPad.TP_STRING, 30, 0 );

	private final JTablePad tabCotacao = new JTablePad();

	private final ListaCampos lcMoeda = new ListaCampos( this, "" );

	public RPCotMoeda() {

		super( false );
		setTitulo( "Cadastro de Moedas" );
		setAtribos( 50, 50, 425, 270 );

		montaListaCampos();

		montaTela();

		setListaCampos( false, "COTMOEDA", "RP" );

		lcCampos.addInsertListener( this );
		lcCampos.addCarregaListener( this );

		tabCotacao.adicColuna( "Data" );
		tabCotacao.adicColuna( "Valor" );
		tabCotacao.adicColuna( "Moeda" );

		tabCotacao.setTamColuna( 100, 0 );
		tabCotacao.setTamColuna( 200, 1 );
		tabCotacao.setTamColuna( 100, 2 );
	}

	private void montaListaCampos() {

		/*******************
		 * MOEDA *
		 *******************/

		lcMoeda.add( new GuardaCampo( txtCodMoeda, "CodMoeda", "Cód.moeda", ListaCampos.DB_PK, true ) );
		lcMoeda.add( new GuardaCampo( txtSingMoeda, "SingMoeda", "Descrição da moeda", ListaCampos.DB_SI, false ) );
		lcMoeda.montaSql( false, "MOEDA", "RP" );
		lcMoeda.setQueryCommit( false );
		lcMoeda.setReadOnly( true );
		txtCodMoeda.setTabelaExterna( lcMoeda, null );
	}

	private void montaTela() {

		panelCampos.setPreferredSize( new Dimension( 400, 110 ) );

		setPainel( panelCampos );

		adicCampo( txtDataCot, 7, 30, 100, 20, "DataCot", "Data", ListaCampos.DB_PK, true );
		adicCampo( txtValor, 110, 30, 150, 20, "ValorCot", "Valor", ListaCampos.DB_SI, true );

		adicCampo( txtCodMoeda, 7, 70, 100, 20, "CodMoeda", "Cód.moeda", ListaCampos.DB_FK, txtSingMoeda, true );
		adicDescFK( txtSingMoeda, 110, 70, 285, 20, "SingMoeda", "Nome no singular" );

		panelTabela.add( new JScrollPane( tabCotacao ), BorderLayout.CENTER );

		panelCotacao.add( panelCampos, BorderLayout.NORTH );
		panelCotacao.add( panelTabela, BorderLayout.CENTER );

		pnCliente.add( panelCotacao, BorderLayout.CENTER );
	}

	private void carregaTabela() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();

		try {

			tabCotacao.limpa();

			sql.append( "SELECT DATACOT, VALORCOT, CODMOEDA " );
			sql.append( "FROM RPCOTMOEDA " );
			sql.append( "WHERE CODEMP=? AND CODFILIAL=? " );
			sql.append( "ORDER BY DATACOT DESC, CODMOEDA " );

			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "RPCOTMOEDA" ) );
			rs = ps.executeQuery();

			for ( int i = 0; rs.next(); i++ ) {

				tabCotacao.adicLinha();
				tabCotacao.setValor( rs.getDate( "DATACOT" ), i, ECotacao.DATA.ordinal() );
				tabCotacao.setValor( rs.getBigDecimal( "VALORCOT" ), i, ECotacao.VALOR.ordinal() );
				tabCotacao.setValor( rs.getString( "CODMOEDA" ), i, ECotacao.MOEDA.ordinal() );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( Exception e ) {
			Funcoes.mensagemErro( this, "erro ao carregar tabela!" );
			e.printStackTrace();
		}

	}

	public void afterInsert( InsertEvent e ) {

		txtDataCot.setVlrDate( Calendar.getInstance().getTime() );
	}

	public void beforeInsert( InsertEvent e ) {

	}

	public void afterCarrega( CarregaEvent e ) {

		carregaTabela();
	}

	public void beforeCarrega( CarregaEvent e ) {

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcMoeda.setConexao( cn );

		carregaTabela();
	}

	private enum ECotacao {
		DATA, VALOR, MOEDA
	}
}
