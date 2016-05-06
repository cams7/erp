/*
 * Projeto: Freedom Pacote: org.freedom.modules.crm Classe: @(#)FConsultaCli.java
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR> modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR> na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR> sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR> Veja a Licença Pública Geral GNU para maiores detalhes. <BR> Você deve ter recebido uma cópia da Licença Pública
 * Geral GNU junto com este programa, se não, <BR> escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA <BR>
 */

package org.freedom.modulos.rep;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.TabelaSelEvent;
import org.freedom.acao.TabelaSelListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.std.view.frame.crud.detail.FVenda;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCliente;

/**
 * Consulta de informações referente a clientes/Produtos.
 * 
 * @author Setpoint Informática Ltda./Anderson Sanchez
 * @version 15/11/2009
 */
public class FConsultaCliProd extends FFilho implements ActionListener, TabelaSelListener, MouseListener, KeyListener, CarregaListener {

	private static final long serialVersionUID = 1L;

	private static final Color GREEN = new Color( 45, 190, 60 );

	private JPanelPad panelGeral = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad panelMaster = new JPanelPad( 700, 100 );

	private JPanelPad panelDetail = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	private JTabbedPanePad tabbedDetail = new JTabbedPanePad();

	private JPanelPad panelVendas = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad panelTabVendas = new JPanelPad( 700, 60 );

	private JPanelPad panelGridVendas = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 2, 1 ) );

	private JPanelPad panelTabVendasNotas = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	private JPanelPad panelTabItensVendas = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	private JPanelPad panelReceber = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad panelHistorico = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad panelSouth = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1, 10, 10 ) );

	// *** Geral

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtRefProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private final JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtAtivoCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JLabelPad lbAtivoCli = new JLabelPad( "Ativo", SwingConstants.CENTER );

	private JButtonPad btBuscar = new JButtonPad( "Buscar vendas", Icone.novo( "btExecuta.png" ) );

	// *** Vendas

	private JTextFieldFK txtUltimaCompra = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldFK txtVlrUltimaCompra = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 12, Aplicativo.casasDecFin );

	private JTextFieldFK txtTotalCompras = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 12, Aplicativo.casasDecFin );

	private JTablePad tabVendas = new JTablePad();

	private JTablePad tabItensVendas = new JTablePad();

	private ImageIcon imgPedido = Icone.novo( "clPagoParcial.gif" );

	private ImageIcon imgColuna = null;

	// *** Listacampos

	private ListaCampos lcCliente = new ListaCampos( this, "CL" );

	private boolean carregandoVendas = false;

	private final ListaCampos lcProduto = new ListaCampos( this, "PD" );

	private enum VENDAS {
		STATUS, CODPED, NUMPEDCLI, DATA, PAGAMENTO, VENDEDOR, VALOR_PRODUTOS, VALOR_DESCONTO, VALOR_ADICIONAL, VALOR_LIQUIDO;
	}

	private enum ITEMVENDAS {
		ITEM, CODPROD, DESCPROD, QUANTIDADE, PRECO, PERCDESCITPED, DESCONTO, TOTAL, ;
	}

	public FConsultaCliProd() {

		super( false );
		setTitulo( "Consulta de clientes", this.getClass().getName() );
		setAtribos( 50, 50, 730, 540 );
		int x = (int) ( Aplicativo.telaPrincipal.dpArea.getSize().getWidth() - getWidth() ) / 2;
		int y = (int) ( Aplicativo.telaPrincipal.dpArea.getSize().getHeight() - getHeight() ) / 2;
		setLocation( x, y );

		montaListaCampos();
		montaTela();

		lcCliente.addCarregaListener( this );
		lcProduto.addCarregaListener( this );
		btBuscar.addActionListener( this );
		tabVendas.addTabelaSelListener( this );
		tabVendas.addMouseListener( this );
		tabItensVendas.addMouseListener( this );
		btBuscar.addKeyListener( this );

		Calendar periodo = Calendar.getInstance();
		txtDatafim.setVlrDate( periodo.getTime() );
		periodo.set( Calendar.YEAR, periodo.get( Calendar.YEAR ) - 1 );
		txtDataini.setVlrDate( periodo.getTime() );
	}

	private void montaListaCampos() {

		lcCliente.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCliente.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCliente.add( new GuardaCampo( txtAtivoCli, "AtivCli", "ativo", ListaCampos.DB_SI, false ) );
		txtCodCli.setTabelaExterna( lcCliente, FCliente.class.getCanonicalName() );
		txtCodCli.setNomeCampo( "CodCli" );
		txtCodCli.setFK( true );
		lcCliente.setReadOnly( true );
		lcCliente.montaSql( false, "CLIENTE", "RP" );

		/***********
		 * PRODUTO *
		 ***********/

		lcProduto.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcProduto.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProduto.add( new GuardaCampo( txtRefProd, "RefProd", "Referência do produto", ListaCampos.DB_SI, false ) );
		lcProduto.montaSql( false, "PRODUTO", "RP" );
		lcProduto.setQueryCommit( false );
		lcProduto.setReadOnly( true );
		txtCodProd.setListaCampos( lcProduto );
		txtCodProd.setTabelaExterna( lcProduto, FProduto.class.getCanonicalName() );
		txtCodProd.setPK( true );
		txtCodProd.setNomeCampo( "CodProd" );

	}

	private void montaTela() {

		getTela().add( panelGeral, BorderLayout.CENTER );
		panelGeral.add( panelMaster, BorderLayout.NORTH );

		// ***** Cabeçalho

		JLabel periodo = new JLabel( "Período", SwingConstants.CENTER );
		periodo.setOpaque( true );
		JLabel borda = new JLabel();
		borda.setBorder( BorderFactory.createEtchedBorder() );

		panelMaster.adic( new JLabelPad( "Cód.Cli" ), 7, 5, 60, 20 );
		panelMaster.adic( txtCodCli, 7, 25, 60, 20 );

		panelMaster.adic( new JLabelPad( "Razão social do cliente" ), 70, 5, 320, 20 );
		panelMaster.adic( txtRazCli, 70, 25, 320, 20 );

		panelMaster.adic( lbAtivoCli, 393, 25, 70, 20 );

		panelMaster.adic( new JLabelPad( "Cód.Prod." ), 7, 45, 60, 20 );
		panelMaster.adic( txtCodProd, 7, 65, 60, 20 );

		panelMaster.adic( new JLabelPad( "Razão social do cliente" ), 70, 45, 320, 20 );
		panelMaster.adic( txtDescProd, 70, 65, 320, 20 );

		panelMaster.adic( periodo, 480, 0, 60, 20 );
		panelMaster.adic( borda, 470, 10, 220, 45 );
		panelMaster.adic( txtDataini, 480, 25, 80, 20 );
		panelMaster.adic( new JLabel( "até", SwingConstants.CENTER ), 560, 25, 40, 20 );
		panelMaster.adic( txtDatafim, 600, 25, 80, 20 );

		panelMaster.adic( btBuscar, 470, 60, 220, 30 );

		lbAtivoCli.setOpaque( true );
		lbAtivoCli.setFont( new Font( "Arial", Font.BOLD, 13 ) );
		lbAtivoCli.setBackground( GREEN );
		lbAtivoCli.setForeground( Color.WHITE );

		// ***** Detalhamento (abas)

		panelGeral.add( panelDetail, BorderLayout.CENTER );
		panelDetail.add( tabbedDetail );
		tabbedDetail.addTab( "Vendas", panelVendas );

		// ***** Vendas

		panelVendas.add( panelTabVendas, BorderLayout.NORTH );
		panelVendas.add( panelGridVendas, BorderLayout.CENTER );
		panelGridVendas.add( panelTabVendasNotas );
		panelGridVendas.add( panelTabItensVendas );

		panelTabVendasNotas.setBorder( BorderFactory.createTitledBorder( "Vendas" ) );
		panelTabItensVendas.setBorder( BorderFactory.createTitledBorder( "Itens de vendas" ) );
		panelTabItensVendas.setPreferredSize( new Dimension( 700, 120 ) );

		panelTabVendas.adic( new JLabelPad( "Última Compra" ), 10, 10, 100, 20 );
		panelTabVendas.adic( txtUltimaCompra, 10, 30, 100, 20 );
		panelTabVendas.adic( new JLabelPad( "Valor última compra" ), 113, 10, 120, 20 );
		panelTabVendas.adic( txtVlrUltimaCompra, 113, 30, 120, 20 );
		panelTabVendas.adic( new JLabelPad( "Total de compras" ), 236, 10, 120, 20 );
		panelTabVendas.adic( txtTotalCompras, 236, 30, 120, 20 );

		Color statusColor = new Color( 111, 106, 177 );
		Font statusFont = new Font( "Tomoha", Font.PLAIN, 11 );

		panelTabVendas.adic( new JLabelPad( imgPedido ), 600, 20, 20, 15 );
		JLabelPad pedidos = new JLabelPad( "pedidos" );
		pedidos.setForeground( statusColor );
		pedidos.setFont( statusFont );
		panelTabVendas.adic( pedidos, 620, 20, 100, 15 );

		tabVendas.adicColuna( "" );
		tabVendas.adicColuna( "Código" );
		tabVendas.adicColuna( "Ped.Cli." );
		tabVendas.adicColuna( "Data" );
		tabVendas.adicColuna( "Plano de pagamento" );
		tabVendas.adicColuna( "Vendedor" );
		tabVendas.adicColuna( "V.Prod." );
		tabVendas.adicColuna( "V.Desc." );
		tabVendas.adicColuna( "V.Adic." );
		tabVendas.adicColuna( "V.Líq." );

		tabVendas.setTamColuna( 20, VENDAS.STATUS.ordinal() );
		tabVendas.setTamColuna( 55, VENDAS.CODPED.ordinal() );
		tabVendas.setTamColuna( 45, VENDAS.NUMPEDCLI.ordinal() );
		tabVendas.setTamColuna( 70, VENDAS.DATA.ordinal() );
		tabVendas.setTamColuna( 110, VENDAS.PAGAMENTO.ordinal() );
		tabVendas.setTamColuna( 90, VENDAS.VENDEDOR.ordinal() );
		tabVendas.setTamColuna( 80, VENDAS.VALOR_PRODUTOS.ordinal() );
		tabVendas.setTamColuna( 60, VENDAS.VALOR_DESCONTO.ordinal() );
		tabVendas.setTamColuna( 60, VENDAS.VALOR_ADICIONAL.ordinal() );
		tabVendas.setTamColuna( 90, VENDAS.VALOR_LIQUIDO.ordinal() );

		panelTabVendasNotas.add( new JScrollPane( tabVendas ) );

		tabItensVendas.adicColuna( "Item" );
		tabItensVendas.adicColuna( "Código" );
		tabItensVendas.adicColuna( "Descrição do produto" );
		tabItensVendas.adicColuna( "Quantidade" );
		tabItensVendas.adicColuna( "Preço" );
		tabItensVendas.adicColuna( "% Desc." );
		tabItensVendas.adicColuna( "V.Desc." );
		tabItensVendas.adicColuna( "V.Líquido" );

		tabItensVendas.setTamColuna( 30, ITEMVENDAS.ITEM.ordinal() );
		tabItensVendas.setTamColuna( 55, ITEMVENDAS.CODPROD.ordinal() );
		tabItensVendas.setTamColuna( 250, ITEMVENDAS.DESCPROD.ordinal() );
		tabItensVendas.setTamColuna( 50, ITEMVENDAS.QUANTIDADE.ordinal() );
		tabItensVendas.setTamColuna( 70, ITEMVENDAS.PRECO.ordinal() );
		tabItensVendas.setTamColuna( 70, ITEMVENDAS.PERCDESCITPED.ordinal() );
		tabItensVendas.setTamColuna( 70, ITEMVENDAS.DESCONTO.ordinal() );
		tabItensVendas.setTamColuna( 90, ITEMVENDAS.TOTAL.ordinal() );

		panelTabItensVendas.add( new JScrollPane( tabItensVendas ) );

		// ***** Rodapé

		panelGeral.add( panelSouth, BorderLayout.SOUTH );
		panelSouth.setBorder( BorderFactory.createEtchedBorder() );
		panelSouth.add( adicBotaoSair() );
	}

	private void buscaVendas() {

		if ( txtCodCli.getVlrInteger() == 0 && txtCodProd.getVlrInteger() == 0 ) {
			Funcoes.mensagemInforma( this, "Selecione um cliente ou produto!" );
			txtCodCli.requestFocus();
			return;
		}

		try {

			StringBuilder sql = new StringBuilder();
			sql.append( "select pd.codped, pd.numpedcli, pd.dataped, pd.codplanopag, " );
			sql.append( "pp.descplanopag, pd.codvend, vd.nomevend, pd.vlrtotped, pd.vlrdescped, " );
			sql.append( "pd.vlradicped, pd.vlrliqped " );
			sql.append( "from rppedido pd, rpplanopag pp, rpvendedor vd " );
			sql.append( "where pd.codemp=? and pd.codfilial=? and pd.dataped between ? and ? and " );
			sql.append( "pp.codemp=pd.codemppg and pp.codfilial=pd.codfilialpg and pp.codplanopag=pd.codplanopag and " );
			sql.append( "vd.codemp=pd.codempvd and vd.codfilial=pd.codfilialvd and vd.codvend=pd.codvend" );

			if ( txtCodCli.getVlrInteger() > 0 ) {
				sql.append( " and pd.codcli=?" );
			}

			if ( txtCodProd.getVlrInteger() > 0 ) {
				sql.append( " and exists (" );
				sql.append( "select codprod from rpitpedido ip where " );
				sql.append( "ip.codemp=pd.codemp and ip.codfilial=pd.codfilial and ip.codped=pd.codped " );
				sql.append( "and ip.codemppd=? and ip.codfilialpd=? and ip.codprod=?)" );
			}

			sql.append( " order by pd.dataped desc " );

			PreparedStatement ps = con.prepareStatement( sql.toString() );

			int iparam = 1;

			ps.setInt( iparam++, Aplicativo.iCodEmp );
			ps.setInt( iparam++, ListaCampos.getMasterFilial( "RPPEDIDO" ) );
			ps.setDate( iparam++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( iparam++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

			if ( txtCodCli.getVlrInteger() > 0 ) {
				ps.setInt( iparam++, txtCodCli.getVlrInteger() );
			}

			if ( txtCodProd.getVlrInteger() > 0 ) {
				ps.setInt( iparam++, lcProduto.getCodEmp() );
				ps.setInt( iparam++, lcProduto.getCodFilial() );
				ps.setInt( iparam++, txtCodProd.getVlrInteger() );
			}

			ResultSet rs = ps.executeQuery();

			carregandoVendas = true;
			tabVendas.limpa();
			tabItensVendas.limpa();

			int row = 0;
			while ( rs.next() ) {

				tabVendas.adicLinha();

				imgColuna = imgPedido;

				tabVendas.setValor( imgColuna, row, VENDAS.STATUS.ordinal() );
				tabVendas.setValor( rs.getInt( "CODPED" ), row, VENDAS.CODPED.ordinal() );
				tabVendas.setValor( rs.getString( "NUMPEDCLI" ), row, VENDAS.NUMPEDCLI.ordinal() );
				tabVendas.setValor( Funcoes.dateToStrDate( rs.getDate( "DATAPED" ) ), row, VENDAS.DATA.ordinal() );
				tabVendas.setValor( rs.getString( "DESCPLANOPAG" ), row, VENDAS.PAGAMENTO.ordinal() );
				tabVendas.setValor( rs.getString( "NOMEVEND" ), row, VENDAS.VENDEDOR.ordinal() );
				tabVendas.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRTOTPED" ) ), row, VENDAS.VALOR_PRODUTOS.ordinal() );
				tabVendas.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRDESCPED" ) ), row, VENDAS.VALOR_DESCONTO.ordinal() );
				tabVendas.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRADICPED" ) ), row, VENDAS.VALOR_ADICIONAL.ordinal() );
				tabVendas.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRLIQPED" ) ), row, VENDAS.VALOR_LIQUIDO.ordinal() );

				row++;
			}

			sql = new StringBuilder();
			sql.append( "SELECT FIRST 1 P.DATAPED, P.VLRLIQPED, P.CODPED " );
			sql.append( "FROM RPPEDIDO P " );
			sql.append( "WHERE P.CODEMP=? AND P.CODFILIAL=? AND P.DATAPED BETWEEN ? AND ? " );
			sql.append( "ORDER BY P.DATAPED DESC" );

			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "RPPEDIDO" ) );
			ps.setDate( 3, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( 4, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				txtUltimaCompra.setVlrDate( Funcoes.sqlDateToDate( rs.getDate( "DATAPED" ) ) );
				txtVlrUltimaCompra.setVlrBigDecimal( rs.getBigDecimal( "VLRLIQPED" ) );
			}

			sql = new StringBuilder();
			sql.append( "SELECT SUM(P.VLRLIQPED) TOTAL " );
			sql.append( "FROM RPPEDIDO P " );
			sql.append( "WHERE P.CODEMP=? AND P.CODFILIAL=? AND P.DATAPED BETWEEN ? AND ? " );
			sql.append( "GROUP BY P.CODCLI " );

			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDVENDA" ) );
			ps.setDate( 3, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( 4, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				txtTotalCompras.setVlrBigDecimal( rs.getBigDecimal( "TOTAL" ) );

				// Selecionando o primeiro pedido
				tabVendas.setLinhaSel( 0 );
				buscaItensVenda( (Integer) tabVendas.getValor( 0, VENDAS.CODPED.ordinal() ) );

			}

			rs.close();
			ps.close();
			con.commit();

			carregandoVendas = false;
			tabVendas.requestFocus();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private void buscaItensVenda( int codped ) {

		try {
			StringBuilder sql = new StringBuilder();
			sql.append( "SELECT I.CODITPED, I.CODPROD, P.DESCPROD, " );
			sql.append( "I.QTDITPED, I.PRECOITPED, I.VLRDESCITPED, I.VLRLIQITPED, I.PERCDESCITPED " );
			sql.append( "FROM RPITPEDIDO I, RPPRODUTO P " );
			sql.append( "WHERE I.CODEMP=? AND I.CODFILIAL=? AND I.CODPED=? AND " );
			sql.append( "P.CODEMP=I.CODEMPPD AND P.CODFILIAL=I.CODFILIALPD AND P.CODPROD=I.CODPROD " );

			if ( txtCodProd.getVlrInteger() > 0 ) {
				sql.append( "AND I.CODPROD=? " );
			}

			sql.append( "ORDER BY I.CODITPED" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );

			int iparam = 1;

			ps.setInt( iparam++, Aplicativo.iCodEmp );
			ps.setInt( iparam++, ListaCampos.getMasterFilial( "VDITVENDA" ) );
			ps.setInt( iparam++, codped );

			if ( txtCodProd.getVlrInteger() > 0 ) {
				ps.setInt( iparam++, txtCodProd.getVlrInteger() );
			}

			ResultSet rs = ps.executeQuery();

			tabItensVendas.limpa();

			int row = 0;
			while ( rs.next() ) {

				tabItensVendas.adicLinha();

				tabItensVendas.setValor( rs.getInt( "CODITPED" ), row, ITEMVENDAS.ITEM.ordinal() );
				tabItensVendas.setValor( rs.getInt( "CODPROD" ), row, ITEMVENDAS.CODPROD.ordinal() );
				tabItensVendas.setValor( rs.getString( "DESCPROD" ), row, ITEMVENDAS.DESCPROD.ordinal() );
				tabItensVendas.setValor( Funcoes.bdToStr( rs.getBigDecimal( "QTDITPED" ) ), row, ITEMVENDAS.QUANTIDADE.ordinal() );
				tabItensVendas.setValor( Funcoes.bdToStr( rs.getBigDecimal( "PRECOITPED" ) ), row, ITEMVENDAS.PRECO.ordinal() );
				tabItensVendas.setValor( Funcoes.bdToStr( rs.getBigDecimal( "PERCDESCITPED" ) ), row, ITEMVENDAS.PERCDESCITPED.ordinal() );
				tabItensVendas.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRDESCITPED" ) ), row, ITEMVENDAS.DESCONTO.ordinal() );
				tabItensVendas.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRLIQITPED" ) ), row, ITEMVENDAS.TOTAL.ordinal() );

				row++;
			}

			rs.close();
			ps.close();
			con.commit();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	public void actionPerformed( ActionEvent e ) {

		if ( e.getSource() == btBuscar ) {
			buscaVendas();
		}
	}

	public void valorAlterado( TabelaSelEvent e ) {

		if ( e.getTabela() == tabVendas && tabVendas.getLinhaSel() > -1 && !carregandoVendas ) {
			buscaItensVenda( (Integer) tabVendas.getValor( tabVendas.getLinhaSel(), VENDAS.CODPED.ordinal() ) );
		}
	}

	public void mouseClicked( MouseEvent e ) {

		if ( e.getClickCount() == 2 ) {
			if ( e.getSource() == tabVendas && tabVendas.getLinhaSel() > -1 ) {
				RPPedido pedido = null;
				if ( Aplicativo.telaPrincipal.temTela( FVenda.class.getName() ) ) {
					pedido = (RPPedido) Aplicativo.telaPrincipal.getTela( RPPedido.class.getName() );
				}
				else {
					pedido = new RPPedido();
					Aplicativo.telaPrincipal.criatela( "Pedido", pedido, con );
				}
				pedido.executar( (Integer) tabVendas.getValor( tabVendas.getLinhaSel(), VENDAS.CODPED.ordinal() ) );
			}
			else if ( e.getSource() == tabItensVendas && tabItensVendas.getLinhaSel() > -1 ) {
				RPPedido pedido = null;
				if ( Aplicativo.telaPrincipal.temTela( RPPedido.class.getName() ) ) {
					pedido = (RPPedido) Aplicativo.telaPrincipal.getTela( RPPedido.class.getName() );
				}
				else {
					pedido = new RPPedido();
					Aplicativo.telaPrincipal.criatela( "Pedido", pedido, con );
				}
				pedido.executar( (Integer) tabVendas.getValor( tabVendas.getLinhaSel(), VENDAS.CODPED.ordinal() ) );
			}
		}
	}

	public void mouseEntered( MouseEvent e ) {

	}

	public void mouseExited( MouseEvent e ) {

	}

	public void mousePressed( MouseEvent e ) {

	}

	public void mouseReleased( MouseEvent e ) {

	}

	public void keyPressed( KeyEvent e ) {

		if ( e.getSource() == btBuscar && e.getKeyCode() == KeyEvent.VK_ENTER ) {
			btBuscar.doClick();
		}
	}

	public void keyReleased( KeyEvent e ) {

	}

	public void keyTyped( KeyEvent e ) {

	}

	public void beforeCarrega( CarregaEvent e ) {

	}

	public void afterCarrega( CarregaEvent e ) {

		if ( lcCliente == e.getListaCampos() || lcProduto == e.getListaCampos() ) {
			if ( "S".equals( txtAtivoCli.getVlrString().trim() ) ) {
				lbAtivoCli.setText( "Ativo" );
				lbAtivoCli.setBackground( GREEN );
			}
			else {
				lbAtivoCli.setText( "Inativo" );
				lbAtivoCli.setBackground( Color.RED );
			}
			buscaVendas();
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcCliente.setConexao( con );
		lcProduto.setConexao( con );
	}
}
