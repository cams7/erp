/*
 * Projeto: Freedom Pacote: org.freedom.modules.std Classe: @(#)DLRetRemessa.java
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR> modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR> na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR> sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR> Veja a Licença Pública Geral GNU para maiores detalhes. <BR> Você deve ter recebido uma cópia da Licença Pública
 * Geral GNU junto com este programa, se não, <BR> escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA <BR> <BR>
 */

package org.freedom.modulos.std.view.dialog.report;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.functions.ConversionFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;

/**
 * Wizard para criar nota de retorno de nota de remessa.
 * 
 * @author Setpoint Informática Ltda./Alex Rodrigues
 * @version 31/08/2009
 */
public class DLRetRemessa extends FFDialogo implements CarregaListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad panelGeral = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad panelMaster = new JPanelPad( 700, 60 );

	private JPanelPad panelGrid = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad panelGridActions = new JPanelPad( 41, 200 );

	private JTextFieldPad txtCodVenda = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtTipoVenda = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldFK txtDocVenda = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 9, 0 );

	private JTextFieldFK txtDtEmitVenda = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldFK txtVlrLiqVenda = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JButtonPad btSelecionarTodos = new JButtonPad( Icone.novo( "btTudo.png" ) );

	private JButtonPad btSelecionarNenhum = new JButtonPad( Icone.novo( "btNada.png" ) );

	private JTextFieldPad txtTipoMov = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTablePad tabItens = new JTablePad();

	private ListaCampos lcVenda = new ListaCampos( this );

	private enum ITENS {
		SEL, ITEM, PRODUTO, DESCRICAO_PRODUTO, QUANTIDADE, QUANTIDADE_VENDA, SALDO, PRECO
		// , PRECO_VENDA, VENDA, ITEM_VENDA;
	}

	public final String tipoMovimento;

	public DLRetRemessa( String tipoMovimento ) {

		super();
		setTitulo( "Pesquisa de remessas" );
		setAtribos( 700, 320 );
		setResizable( true );

		txtTipoVenda.setVlrString( "V" );

		this.tipoMovimento = tipoMovimento;
		txtTipoMov.setVlrString( this.tipoMovimento );

		montaListaCampos();
		montaTela();

		lcVenda.addCarregaListener( this );

		btSelecionarTodos.addActionListener( this );
		btSelecionarNenhum.addActionListener( this );
	}

	private void montaListaCampos() {

		lcVenda.add( new GuardaCampo( txtCodVenda, "CodVenda", "N. pedido", ListaCampos.DB_PK, false ) );
		lcVenda.add( new GuardaCampo( txtDocVenda, "DocVenda", "N doc.", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtDtEmitVenda, "DtEmitVenda", "Data emis.", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtVlrLiqVenda, "VlrLiqVenda", "Valor", ListaCampos.DB_SI, false ) );
		txtCodVenda.setTabelaExterna( lcVenda, null );
		txtCodVenda.setNomeCampo( "CodVenda" );
		txtCodVenda.setFK( true );
		txtCodVenda.setPK( true );
		lcVenda.setDinWhereAdic( "TIPOVENDA = #S", txtTipoVenda );
		lcVenda.setDinWhereAdic( "EXISTS (SELECT TM.CODTIPOMOV FROM EQTIPOMOV TM " + "WHERE TM.CODEMP=V.CODEMPTM AND TM.CODFILIAL=V.CODFILIALTM AND " + "TM.CODTIPOMOV=V.CODTIPOMOV AND TM.TIPOMOV = #S ) ", txtTipoMov );
		lcVenda.montaSql( false, "VENDA V", "VD" );
		lcVenda.setReadOnly( true );
	}

	private void montaTela() {

		setPanel( panelGeral );
		panelGeral.add( panelMaster, BorderLayout.NORTH );

		panelMaster.adic( new JLabelPad( "Cód.venda" ), 7, 10, 100, 20 );
		panelMaster.adic( txtCodVenda, 7, 30, 100, 20 );
		panelMaster.adic( new JLabelPad( "Doc.venda" ), 110, 10, 100, 20 );
		panelMaster.adic( txtDocVenda, 110, 30, 100, 20 );
		panelMaster.adic( new JLabelPad( "Emissão" ), 213, 10, 100, 20 );
		panelMaster.adic( txtDtEmitVenda, 213, 30, 100, 20 );
		panelMaster.adic( new JLabelPad( "Valor líquido" ), 316, 10, 100, 20 );
		panelMaster.adic( txtVlrLiqVenda, 316, 30, 100, 20 );

		// ***** Grid

		panelGeral.add( panelGrid, BorderLayout.CENTER );
		panelGrid.setBorder( BorderFactory.createEtchedBorder() );

		tabItens.adicColuna( "" );
		tabItens.adicColuna( "Item" );
		tabItens.adicColuna( "Código" );
		tabItens.adicColuna( "Descrição do produto" );
		tabItens.adicColuna( "Qtd. remessa" );
		tabItens.adicColuna( "Qtd. vendida" );
		tabItens.adicColuna( "Saldo" );
		tabItens.adicColuna( "Preço" );
		// tabItens.adicColuna( "Preço venda" );
		// tabItens.adicColuna( "Cód.venda" );
		// tabItens.adicColuna( "Item venda" );

		tabItens.setTamColuna( 20, ITENS.SEL.ordinal() );
		tabItens.setTamColuna( 60, ITENS.ITEM.ordinal() );
		tabItens.setTamColuna( 80, ITENS.PRODUTO.ordinal() );
		tabItens.setTamColuna( 230, ITENS.DESCRICAO_PRODUTO.ordinal() );
		tabItens.setTamColuna( 80, ITENS.QUANTIDADE.ordinal() );
		tabItens.setTamColuna( 80, ITENS.QUANTIDADE_VENDA.ordinal() );
		tabItens.setTamColuna( 100, ITENS.SALDO.ordinal() );
		tabItens.setTamColuna( 80, ITENS.PRECO.ordinal() );
		// tabItens.setTamColuna( 80, ITENS.PRECO_VENDA.ordinal() );
		// tabItens.setTamColuna( 80, ITENS.VENDA.ordinal() );
		// tabItens.setTamColuna( 60, ITENS.ITEM_VENDA.ordinal() );

		tabItens.setColunaEditavel( ITENS.SEL.ordinal(), true );

		panelGrid.add( new JScrollPane( tabItens ), BorderLayout.CENTER );

		panelGridActions.adic( btSelecionarTodos, 3, 3, 30, 30 );
		panelGridActions.adic( btSelecionarNenhum, 3, 38, 30, 30 );
		panelGrid.add( panelGridActions, BorderLayout.EAST );
	}

	private void selecionarTodos() {

		for ( int row = 0; row < tabItens.getNumLinhas(); row++ ) {
			tabItens.setValor( new Boolean( true ), row, ITENS.SEL.ordinal() );
		}
	}

	private void selecionarNenhum() {

		for ( int row = 0; row < tabItens.getNumLinhas(); row++ ) {
			tabItens.setValor( new Boolean( false ), row, ITENS.SEL.ordinal() );
		}
	}

	private void carregaVendas() {

		try {

			StringBuilder selectVendas = new StringBuilder();
			selectVendas.append( "SELECT " );
			selectVendas.append( "R.CODVENDA, R.CODITVENDA, R.CODPROD, P.DESCPROD, R.QTDITVENDA QTDITVENDAVR, R.PRECOITVENDA PRECOITVENDAVR, " );
			selectVendas.append( "COALESCE((SELECT SUM(IR.QTDITVENDA) FROM VDITVENDA IR " );
			selectVendas.append( " WHERE IR.CODEMPVR=R.CODEMP AND IR.CODFILIALVR=R.CODFILIAL AND IR.TIPOVENDAVR=R.TIPOVENDA AND " );
			selectVendas.append( " IR.CODVENDAVR=R.CODVENDA AND IR.CODITVENDAVR=R.CODITVENDA), 0.0) QTDITVENDA " );
			selectVendas.append( "FROM VDITVENDA R, EQPRODUTO P " );
			selectVendas.append( "WHERE R.CODEMP=? AND R.CODFILIAL=? AND R.CODVENDA=? AND R.TIPOVENDA=? AND " );
			selectVendas.append( "P.CODEMP=R.CODEMPPD AND P.CODFILIAL=R.CODFILIALPD AND P.CODPROD=R.CODPROD " );
			selectVendas.append( "ORDER BY R.CODVENDA, R.CODITVENDA" );

			PreparedStatement ps = con.prepareStatement( selectVendas.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDITVENDA" ) );
			ps.setInt( 3, txtCodVenda.getVlrInteger() );
			ps.setString( 4, txtTipoVenda.getVlrString() );

			ResultSet rs = ps.executeQuery();

			tabItens.limpa();

			int row = 0;
			while ( rs.next() ) {

				tabItens.adicLinha();
				tabItens.setValor( new Boolean( true ), row, ITENS.SEL.ordinal() );
				tabItens.setValor( rs.getInt( "CODITVENDA" ), row, ITENS.ITEM.ordinal() );
				tabItens.setValor( rs.getInt( "CODPROD" ), row, ITENS.PRODUTO.ordinal() );
				tabItens.setValor( rs.getString( "DESCPROD" ), row, ITENS.DESCRICAO_PRODUTO.ordinal() );
				tabItens.setValor( Funcoes.bdToStr( rs.getBigDecimal( "QTDITVENDAVR" ) ), row, ITENS.QUANTIDADE.ordinal() );
				tabItens.setValor( Funcoes.bdToStr( rs.getBigDecimal( "QTDITVENDA" ) ), row, ITENS.QUANTIDADE_VENDA.ordinal() );
				tabItens.setValor( Funcoes.bdToStr( rs.getBigDecimal( "QTDITVENDAVR" ).subtract( rs.getBigDecimal( "QTDITVENDA" ) ) ), row, ITENS.SALDO.ordinal() );
				tabItens.setValor( Funcoes.bdToStr( rs.getBigDecimal( "PRECOITVENDAVR" ) ), row, ITENS.PRECO.ordinal() );
				// tabItens.setValor( Funcoes.bdToStr( rs.getBigDecimal( "PRECOITVENDA" ) ), row, ITENS.PRECO_VENDA.ordinal() );
				// tabItens.setValor( rs.getInt( "CODVENDA" ), row, ITENS.VENDA.ordinal() );
				// tabItens.setValor( rs.getInt( "CODITVENDA" ), row, ITENS.ITEM_VENDA.ordinal() );

				row++;
			}

			con.commit();

		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar nota de remessa.\n" + e.getMessage(), true, con, e );
		}
	}

	public List<GridBuscaRetorno> getGridBuscaRemessa() {

		List<GridBuscaRetorno> list = new ArrayList<GridBuscaRetorno>();

		for ( int row = 0; row < tabItens.getNumLinhas(); row++ ) {

			if ( (Boolean) tabItens.getValor( row, ITENS.SEL.ordinal() ) ) {

				GridBuscaRetorno gridBuscaRemessa = new GridBuscaRetorno();

				gridBuscaRemessa.setCodigoRemessa( txtCodVenda.getVlrInteger() );
				gridBuscaRemessa.setItemRemessa( (Integer) tabItens.getValor( row, ITENS.ITEM.ordinal() ) );
				gridBuscaRemessa.setCodigoProduto( (Integer) tabItens.getValor( row, ITENS.PRODUTO.ordinal() ) );
				gridBuscaRemessa.setDescricaoProduto( (String) tabItens.getValor( row, ITENS.DESCRICAO_PRODUTO.ordinal() ) );
				gridBuscaRemessa.setQuantidadeRemessa( ConversionFunctions.stringToBigDecimal( tabItens.getValor( row, ITENS.QUANTIDADE.ordinal() ) ) );
				gridBuscaRemessa.setPrecoRemessa( ConversionFunctions.stringToBigDecimal( tabItens.getValor( row, ITENS.PRECO.ordinal() ) ) );
				// gridBuscaRemessa.setCodigoVenda( (Integer)tabItens.getValor( row, ITENS.VENDA.ordinal() ) );
				// gridBuscaRemessa.setItemVenda( (Integer)tabItens.getValor( row, ITENS.ITEM_VENDA.ordinal() ) );
				// gridBuscaRemessa.setQuantidadeVenda( Funcoes.strToBd( tabItens.getValor( row, ITENS.QUANTIDADE_VENDA.ordinal() ) ) );
				// gridBuscaRemessa.setPrecoVenda( Funcoes.strToBd( tabItens.getValor( row, ITENS.PRECO_VENDA.ordinal() ) ) );
				gridBuscaRemessa.setSaldo( ConversionFunctions.stringToBigDecimal( tabItens.getValor( row, ITENS.SALDO.ordinal() ) ) );

				list.add( gridBuscaRemessa );
			}
		}

		return list;
	}

	@ Override
	public void actionPerformed( ActionEvent e ) {

		if ( e.getSource() == btSelecionarTodos ) {
			selecionarTodos();
		}
		else if ( e.getSource() == btSelecionarNenhum ) {
			selecionarNenhum();
		}
		else {
			super.actionPerformed( e );
		}
	}

	public void beforeCarrega( CarregaEvent e ) {

	}

	public void afterCarrega( CarregaEvent e ) {

		if ( e.getListaCampos() == lcVenda ) {
			carregaVendas();
		}
	}

	@ Override
	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcVenda.setConexao( cn );
	}

	public class GridBuscaRetorno {

		private Integer codigoRemessa;

		private Integer itemRemessa;

		private Integer codigoProduto;

		private String descricaoProduto;

		private BigDecimal quantidadeRemessa;

		private BigDecimal precoRemessa;

		private Integer codigoVenda;

		private Integer itemVenda;

		private BigDecimal quantidadeVenda;

		private BigDecimal precoVenda;

		private BigDecimal saldo;

		public GridBuscaRetorno() {

		}

		public Integer getCodigoRemessa() {

			return codigoRemessa;
		}

		public void setCodigoRemessa( Integer codigoVenda ) {

			this.codigoRemessa = codigoVenda;
		}

		public Integer getItemRemessa() {

			return itemRemessa;
		}

		public void setItemRemessa( Integer itemVenda ) {

			this.itemRemessa = itemVenda;
		}

		public Integer getCodigoProduto() {

			return codigoProduto;
		}

		public void setCodigoProduto( Integer codigoProduto ) {

			this.codigoProduto = codigoProduto;
		}

		public String getDescricaoProduto() {

			return descricaoProduto;
		}

		public void setDescricaoProduto( String descricaoProduto ) {

			this.descricaoProduto = descricaoProduto;
		}

		public BigDecimal getQuantidadeRemessa() {

			return quantidadeRemessa;
		}

		public void setQuantidadeRemessa( BigDecimal quantidade ) {

			this.quantidadeRemessa = quantidade;
		}

		public BigDecimal getPrecoRemessa() {

			return precoRemessa;
		}

		public void setPrecoRemessa( BigDecimal preco ) {

			this.precoRemessa = preco;
		}

		public Integer getCodigoVenda() {

			return codigoVenda;
		}

		public void setCodigoVenda( Integer codigoVenda ) {

			this.codigoVenda = codigoVenda;
		}

		public Integer getItemVenda() {

			return itemVenda;
		}

		public void setItemVenda( Integer itemVenda ) {

			this.itemVenda = itemVenda;
		}

		public BigDecimal getQuantidadeVenda() {

			return quantidadeVenda;
		}

		public void setQuantidadeVenda( BigDecimal quantidadeVenda ) {

			this.quantidadeVenda = quantidadeVenda;
		}

		public BigDecimal getPrecoVenda() {

			return precoVenda;
		}

		public void setPrecoVenda( BigDecimal precoVenda ) {

			this.precoVenda = precoVenda;
		}

		public BigDecimal getSaldo() {

			return saldo;
		}

		public void setSaldo( BigDecimal saldo ) {

			this.saldo = saldo;
		}
	}
}
