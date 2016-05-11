/**
 * @version 04/2007 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * @author Alex Rodrigues<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.rep <BR>
 *         Classe:
 * @(#)RPConsPedido.java <BR>
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
 *                       Tela de consulta de pedidos.
 * 
 */

package org.freedom.modulos.rep;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;

public class RPConsPedido extends FFilho implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final JPanelPad panelConsulta = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad panelCampos = new JPanelPad();

	private final JPanelPad panelTabela = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private final JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldFK txtRazFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private final JTextFieldPad txtDtIni = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtDtFim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JButtonPad btPesquisar = new JButtonPad( "Pesquisar...", Icone.novo( "btObs1.png" ) );

	private final JTablePad tabConsulta = new JTablePad();

	private final ListaCampos lcCliente = new ListaCampos( this, "" );

	private final ListaCampos lcFornecedor = new ListaCampos( this, "" );

	public RPConsPedido() {

		super( false );
		setTitulo( "Consulta de Pedidos" );
		setAtribos( 100, 70, 395, 400 );

		montaListaCampos();
		montaTela();
		adicBotaoSair();

		Calendar cal = Calendar.getInstance();
		txtDtFim.setVlrDate( cal.getTime() );
		cal.set( cal.get( Calendar.YEAR ) - 2, cal.get( Calendar.MONTH ), cal.get( Calendar.DATE ) );
		txtDtIni.setVlrDate( cal.getTime() );

		btPesquisar.addActionListener( this );

		tabConsulta.addMouseListener( new MouseAdapter() {

			@ Override
			public void mouseClicked( MouseEvent e ) {

				if ( e.getSource() == tabConsulta && tabConsulta.getLinhaSel() > -1 ) {
					openPedido( (Integer) tabConsulta.getValor( tabConsulta.getLinhaSel(), EConsulta.PEDIDO.ordinal() ) );
				}
			}
		} );
	}

	private void montaListaCampos() {

		/***********
		 * CLIENTE *
		 ***********/

		lcCliente.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCliente.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCliente.montaSql( false, "CLIENTE", "RP" );
		lcCliente.setQueryCommit( false );
		lcCliente.setReadOnly( true );
		txtCodCli.setListaCampos( lcCliente );
		txtCodCli.setTabelaExterna( lcCliente, null );
		txtCodCli.setPK( true );
		txtCodCli.setNomeCampo( "CodCli" );

		/**************
		 * FORNECEDOR *
		 **************/

		lcFornecedor.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.for.", ListaCampos.DB_PK, false ) );
		lcFornecedor.add( new GuardaCampo( txtRazFor, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );
		lcFornecedor.montaSql( false, "FORNECEDOR", "RP" );
		lcFornecedor.setQueryCommit( false );
		lcFornecedor.setReadOnly( true );
		txtCodFor.setListaCampos( lcFornecedor );
		txtCodFor.setTabelaExterna( lcFornecedor, null );
		txtCodFor.setPK( true );
		txtCodFor.setNomeCampo( "CodFor" );
	}

	private void montaTela() {

		panelCampos.setPreferredSize( new Dimension( 400, 160 ) );

		panelCampos.adic( new JLabel( "Cód.cli." ), 7, 10, 100, 20 );
		panelCampos.adic( txtCodCli, 7, 30, 100, 20 );
		panelCampos.adic( new JLabel( "Razão social do cliente" ), 110, 10, 260, 20 );
		panelCampos.adic( txtRazCli, 110, 30, 260, 20 );
		panelCampos.adic( new JLabel( "Cód.for." ), 7, 50, 100, 20 );
		panelCampos.adic( txtCodFor, 7, 70, 100, 20 );
		panelCampos.adic( new JLabel( "Razão social do fornecedor" ), 110, 50, 260, 20 );
		panelCampos.adic( txtRazFor, 110, 70, 260, 20 );

		JLabel periodo = new JLabel( "Periodo", SwingConstants.CENTER );
		periodo.setOpaque( true );
		panelCampos.adic( periodo, 20, 95, 60, 20 );

		JLabel borda = new JLabel();
		borda.setBorder( BorderFactory.createEtchedBorder() );
		panelCampos.adic( borda, 7, 105, 226, 45 );

		panelCampos.adic( txtDtIni, 20, 120, 80, 20 );
		panelCampos.adic( new JLabel( "até", SwingConstants.CENTER ), 100, 120, 40, 20 );
		panelCampos.adic( txtDtFim, 140, 120, 80, 20 );

		panelCampos.adic( btPesquisar, 240, 115, 130, 30 );

		panelTabela.add( new JScrollPane( tabConsulta ), BorderLayout.CENTER );

		panelConsulta.add( panelCampos, BorderLayout.NORTH );
		panelConsulta.add( panelTabela, BorderLayout.CENTER );

		pnCliente.add( panelConsulta, BorderLayout.CENTER );

		tabConsulta.adicColuna( "Pedido" );
		tabConsulta.adicColuna( "Emissão" );
		tabConsulta.adicColuna( "Valor pedido" );

		tabConsulta.setTamColuna( 80, EConsulta.PEDIDO.ordinal() );
		tabConsulta.setTamColuna( 120, EConsulta.EMISSAO.ordinal() );
		tabConsulta.setTamColuna( 120, EConsulta.VALOR.ordinal() );
	}

	private void carregaTabela() {

		if ( txtDtIni.getVlrDate() != null && txtDtFim.getVlrDate() != null ) {
			if ( txtDtFim.getVlrDate().before( txtDtIni.getVlrDate() ) ) {
				Funcoes.mensagemInforma( this, "Data final inferior a inicial!" );
				return;
			}
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		StringBuilder where = new StringBuilder();

		try {

			tabConsulta.limpa();

			if ( txtCodCli.getVlrInteger() != null && txtCodCli.getVlrInteger() > 0 ) {
				where.append( " AND P.CODEMPCL=" );
				where.append( Aplicativo.iCodEmp );
				where.append( " AND P.CODFILIALCL=" );
				where.append( ListaCampos.getMasterFilial( "RPCLIENTE" ) );
				where.append( " AND P.CODCLI=" );
				where.append( txtCodCli.getVlrInteger() );
			}
			if ( txtCodFor.getVlrInteger() != null && txtCodFor.getVlrInteger() > 0 ) {
				where.append( " AND P.CODEMPFO=" );
				where.append( Aplicativo.iCodEmp );
				where.append( " AND P.CODFILIALFO=" );
				where.append( ListaCampos.getMasterFilial( "RPFORNECEDOR" ) );
				where.append( " AND P.CODFOR=" );
				where.append( txtCodFor.getVlrInteger() );
			}

			sql.append( "SELECT P.CODPED, P.DATAPED, P.VLRLIQPED " );
			sql.append( "FROM RPPEDIDO P " );
			sql.append( "WHERE P.CODEMP=? AND P.CODFILIAL=? AND P.DATAPED BETWEEN ? AND ? " );
			sql.append( where );
			sql.append( " ORDER BY DATAPED, CODPED, VLRLIQPED DESC " );

			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "RPCOTMOEDA" ) );
			ps.setDate( 3, Funcoes.dateToSQLDate( txtDtIni.getVlrDate() ) );
			ps.setDate( 4, Funcoes.dateToSQLDate( txtDtFim.getVlrDate() ) );
			rs = ps.executeQuery();

			for ( int i = 0; rs.next(); i++ ) {

				tabConsulta.adicLinha();
				tabConsulta.setValor( rs.getInt( "CODPED" ), i, EConsulta.PEDIDO.ordinal() );
				tabConsulta.setValor( rs.getDate( "DATAPED" ), i, EConsulta.EMISSAO.ordinal() );
				tabConsulta.setValor( rs.getBigDecimal( "VLRLIQPED" ), i, EConsulta.VALOR.ordinal() );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( Exception e ) {
			Funcoes.mensagemErro( this, "erro ao carregar tabela!", true, con, e );
			e.printStackTrace();
		}
	}

	private void openPedido( final Integer codped ) {

		RPPedido pedido = new RPPedido();

		if ( fPrim.temTela( "Pedidos" ) ) {

			pedido = (RPPedido) fPrim.getTela( "Pedidos" );
		}
		else {

			fPrim.criatela( "Pedidos", pedido, con );
		}

		pedido.executar( codped );
		pedido.toFront();
	}

	public void actionPerformed( ActionEvent e ) {

		if ( e.getSource() == btPesquisar ) {

			carregaTabela();
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcCliente.setConexao( cn );
		lcFornecedor.setConexao( cn );
	}

	private enum EConsulta {
		PEDIDO, EMISSAO, VALOR,
	}
}
