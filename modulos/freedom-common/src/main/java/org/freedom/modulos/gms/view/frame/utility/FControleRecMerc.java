/*
 * Projeto: Freedom Pacote: org.freedom.modules.crm Classe: @(#)FPCP.java
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR> modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR> na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR> sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR> Veja a Licença Pública Geral GNU para maiores detalhes. <BR> Você deve ter recebido uma cópia da Licença Pública
 * Geral GNU junto com este programa, se não, <BR> escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA <BR>
 */

package org.freedom.modulos.gms.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.TabelaEditEvent;
import org.freedom.acao.TabelaEditListener;
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
import org.freedom.library.swing.util.SwingParams;
import org.freedom.modulos.fnc.view.dialog.utility.DLInfoPlanoPag;
import org.freedom.modulos.gms.dao.DAORecMerc;
import org.freedom.modulos.gms.view.frame.crud.detail.FCompra;
import org.freedom.modulos.gms.view.frame.crud.detail.FCotacaoPrecos;
import org.freedom.modulos.gms.view.frame.crud.detail.FRecMerc;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.std.view.frame.crud.tabbed.FFornecedor;

/**
 * Painel de controle para recepção de materias primas
 * 
 * @author Setpoint Informática Ltda./Anderson Sanchez
 * @version 08/01/2010
 */

public class FControleRecMerc extends FFilho implements ActionListener, TabelaSelListener, MouseListener, KeyListener, CarregaListener, TabelaEditListener, ChangeListener {

	private static final long serialVersionUID = 1L;

	private static final Color GREEN = new Color( 45, 190, 64 );

	// *** Paineis tela

	private JPanelPad panelGeral = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad panelMaster = new JPanelPad( 700, 130 );

	private JPanelPad panelAbas = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	private JTabbedPanePad tabbedAbas = new JTabbedPanePad();

	private JPanelPad panelSouth = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad panelLegenda = new JPanelPad();

	private JPanelPad panelNavegador = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	private JPanelPad panelFiltros = new JPanelPad( "Filtros", Color.BLUE );

	// *** Paineis Detalhamento

	private JPanelPad panelDet = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad panelTabDet = new JPanelPad( 700, 0 );

	private JPanelPad panelGridDet = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	private JPanelPad panelTabDetItens = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	private JTablePad tabDet = null;

	// *** Labels

	private JLabelPad sepdet = new JLabelPad();

	// *** Geral

	private JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRazFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldFK txtNomeFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtFoneFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 12, 0 );

	private JTextFieldFK txtCelFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 12, 0 );

	private JTextFieldFK txtContatoFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	// *** Listacampos

	private ListaCampos lcForneced = new ListaCampos( this );

	// *** Botões
	private JButtonPad btAtualiza = new JButtonPad( Icone.novo( "btAtualiza.png" ) );

	private JButtonPad btNovo = new JButtonPad( Icone.novo( "btNovo.png" ) );

	// private JButtonPad btExcluir = new JButtonPad( Icone.novo( "btExcluir.png" ) );
	private JButtonPad btEditar = new JButtonPad( Icone.novo( "btEditar.gif" ) );

	private JButtonPad btCotacao = new JButtonPad( Icone.novo( "btCotacao.png" ) );
	
	private JButtonPad btCompra = new JButtonPad( Icone.novo( "btEntrada.png" ) );

	private JTablePad tabstatus = new JTablePad();

	private JScrollPane scpStatus = new JScrollPane( tabstatus );

	private ImageIcon imgColuna = Icone.novo( "clAgdCanc.png" );
	
	private ListaCampos lcProduto = new ListaCampos( this, "PD" );
	
	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );


	// Enums

	private enum DETALHAMENTO {
		STATUS, STATUSTXT, TICKET, CODTIPORECMERC, DATA, HORA, PLACA, CODTRAN, NOMETRAN, CODFOR, NOMEFOR, PESOLIQUIDO, RENDA, CODCOMPRA;
	}

	public FControleRecMerc() {

		super( false );

		setTitulo( "Painel de recepção de matéria prima", this.getClass().getName() );
		setAtribos( 20, 20, 960, 600 );

		int x = (int) ( Aplicativo.telaPrincipal.dpArea.getSize().getWidth() - getWidth() ) / 2;
		int y = (int) ( Aplicativo.telaPrincipal.dpArea.getSize().getHeight() - getHeight() ) / 2;

		setLocation( x, y );
		setValoresPadrao();
		montaListaCampos();
		criaTabelas();
		montaTela();
		montaListeners();
		adicToolTips();

	}

	private void setValoresPadrao() {

		txtDataini.setVlrDate( Funcoes.getDataIniMes( Funcoes.getMes( new Date() ) - 1, Funcoes.getAno( new Date() ) ) );
		txtDatafim.setVlrDate( Funcoes.getDataFimMes( Funcoes.getMes( new Date() ) - 1, Funcoes.getAno( new Date() ) ) );

	}

	private void carregaStatus() {

		Vector<Object> valores = DAORecMerc.getValores();
		Vector<String> labels = DAORecMerc.getLabels();
		Vector<ImageIcon> icones = new Vector<ImageIcon>();

		Vector<Object> item = null;

		for ( int i = 0; i < valores.size(); i++ ) {

			item = new Vector<Object>();

			String valor = valores.elementAt( i ).toString();
			String label = labels.elementAt( i );
			ImageIcon icon = DAORecMerc.getImagem( valor, DAORecMerc.IMG_TAMANHO_P );

			if ( DAORecMerc.STATUS_NOTA_ENTRADA_EMITIDA.getValue().equals( valor ) || DAORecMerc.STATUS_PEDIDO_COMPRA_EMITIDO.getValue().equals( valor ) ) {
				item.addElement( new Boolean( false ) );
			}
			else {
				item.addElement( new Boolean( true ) );
			}

			item.addElement( valor );
			item.addElement( icon );
			item.addElement( label );

			tabstatus.adicLinha( item );

		}

	}

	private void montaGridStatus() {

		tabstatus.adicColuna( "" ); // Selecao
		tabstatus.adicColuna( "Cod." ); // Codigo
		tabstatus.adicColuna( "" ); // Imagem
		tabstatus.adicColuna( "Status" ); // Descrição

		tabstatus.setTamColuna( 10, 0 );

		tabstatus.setColunaInvisivel( 1 );

		tabstatus.setTamColuna( 10, 2 );
		tabstatus.setTamColuna( 100, 3 );

		tabstatus.setRowHeight( 12 );

		tabstatus.setColunaEditavel( 0, new Boolean( true ) );

	}

	private void montaListaCampos() {

		lcForneced.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.for.", ListaCampos.DB_PK, false ) );
		lcForneced.add( new GuardaCampo( txtRazFor, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );
		lcForneced.add( new GuardaCampo( txtNomeFor, "NomeFor", "Nome do fornecedor", ListaCampos.DB_SI, false ) );
		lcForneced.add( new GuardaCampo( txtContatoFor, "ContFor", "Contato", ListaCampos.DB_SI, false ) );
		lcForneced.add( new GuardaCampo( txtFoneFor, "FoneFor", "Telefone", ListaCampos.DB_SI, false ) );

		lcForneced.setWhereAdic( "ATIVOFOR='S'" );
		lcForneced.montaSql( false, "FORNECED", "CP" );
		lcForneced.setReadOnly( true );
		txtCodFor.setTabelaExterna( lcForneced, FFornecedor.class.getCanonicalName() );
		txtCodFor.setFK( true );
		txtCodFor.setNomeCampo( "CodFor" );
		
		
		// * Produto (

		lcProduto.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcProduto.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );

		txtCodProd.setTabelaExterna( lcProduto, FProduto.class.getCanonicalName() );
		txtCodProd.setNomeCampo( "CodProd" );
		txtCodProd.setFK( true );

		lcProduto.setReadOnly( true );
		lcProduto.montaSql( false, "PRODUTO", "EQ" );


	}

	private void adicToolTips() {

		btAtualiza.setToolTipText( "Executa pesquisa - (F5)" );
		btEditar.setToolTipText( "Abre recepção de mercadorias - (ENTER/SPACE)" );
		btNovo.setToolTipText( "Nova recepção - (F12)" );
		btCompra.setToolTipText( "Gerar pedido de compra - (F11)" );
		btCotacao.setToolTipText( "Gerar cotação" );
	}

	private void montaListeners() {

		btAtualiza.addActionListener( this );
		btNovo.addActionListener( this );

		btEditar.addActionListener( this );
		btCompra.addActionListener( this );
		btCotacao.addActionListener( this );

		tabDet.addTabelaSelListener( this );
		tabDet.addMouseListener( this );
		tabDet.addKeyListener( this );

		this.addKeyListener( this );

	}

	private void montaTela() {

		getTela().add( panelGeral, BorderLayout.CENTER );
		panelGeral.add( panelMaster, BorderLayout.NORTH );

		// ***** Cabeçalho

		panelMaster.adic( panelFiltros, 4, 0, 935, 120 );

		panelFiltros.adic( scpStatus, 587, 0, 150, 90 );
		panelFiltros.adic( btAtualiza, 740, 0, 30, 89 );

		panelFiltros.adic( new JLabelPad( "Data Inicial" ), 7, 0, 70, 20 );
		panelFiltros.adic( txtDataini, 7, 20, 70, 20 );

		panelFiltros.adic( new JLabelPad( "Data Final" ), 80, 0, 70, 20 );
		panelFiltros.adic( txtDatafim, 80, 20, 70, 20 );

		panelFiltros.adic( new JLabelPad( "Cód.For." ), 153, 0, 70, 20 );
		panelFiltros.adic( txtCodFor, 153, 20, 70, 20 );

		panelFiltros.adic( new JLabelPad( "Razão social do fornecedor" ), 226, 0, 180, 20 );
		panelFiltros.adic( txtRazFor, 226, 20, 320, 20 );
		
		panelFiltros.adic( new JLabelPad( "Cód.Prod." ), 153, 40, 70, 20 );
		panelFiltros.adic( txtCodProd, 153, 60, 70, 20 );

		panelFiltros.adic( new JLabelPad( "Descrição do produto" ), 226, 40, 180, 20 );
		panelFiltros.adic( txtDescProd, 226, 60, 320, 20 );

		

		// ***** Abas

		panelGeral.add( panelAbas, BorderLayout.CENTER );
		panelGeral.add( panelAbas );
		panelAbas.add( tabbedAbas );

		tabbedAbas.addTab( "Detalhamento", panelDet );

		tabbedAbas.addChangeListener( this );

		// ***** Detalhamento

		panelDet.add( panelTabDet, BorderLayout.NORTH );
		panelDet.add( panelGridDet, BorderLayout.CENTER );
		panelGridDet.add( panelTabDetItens );

		panelTabDetItens.add( new JScrollPane( tabDet ) );

		// ***** Rodapé

		Color statusColor = new Color( 111, 106, 177 );
		Font statusFont = SwingParams.getFontpadmed();

		panelLegenda.setBorder( null );

		panelGeral.add( panelSouth, BorderLayout.SOUTH );
		panelSouth.setBorder( BorderFactory.createEtchedBorder() );

		panelNavegador.add( btNovo );
		// panelNavegador.add( btExcluir );
		panelNavegador.add( btEditar );
		panelNavegador.add( btCotacao );
		panelNavegador.add( btCompra );

		panelSouth.add( panelNavegador, BorderLayout.WEST );
		panelSouth.add( panelLegenda, BorderLayout.CENTER );
		panelSouth.add( adicBotaoSair(), BorderLayout.EAST );

		montaGridStatus();
		carregaStatus();

	}

	private void criaTabelas() {

		// Tabela de detalhamento

		tabDet = new JTablePad();
		tabDet.setRowHeight( 21 );

		tabDet.adicColuna( "" );
		tabDet.adicColuna( "" );
		tabDet.adicColuna( "Ticket" );
		tabDet.adicColuna( "Cód.Tipo.Rec.Merc." );

		tabDet.adicColuna( "Data" );
		tabDet.adicColuna( "Hora" );
		tabDet.adicColuna( "Placa" );
		tabDet.adicColuna( "Cod.T." );
		tabDet.adicColuna( "Transportador" );
		tabDet.adicColuna( "Cod.F." );
		tabDet.adicColuna( "Fornecedor" );
		tabDet.adicColuna( "Peso Liquido" );
		tabDet.adicColuna( "Renda" );
		tabDet.adicColuna( "Cód.ped." );

		tabDet.setTamColuna( 21, DETALHAMENTO.STATUS.ordinal() );
		tabDet.setColunaInvisivel( DETALHAMENTO.STATUSTXT.ordinal() );
		tabDet.setTamColuna( 60, DETALHAMENTO.TICKET.ordinal() );
		tabDet.setColunaInvisivel( DETALHAMENTO.CODTIPORECMERC.ordinal() );
		tabDet.setTamColuna( 60, DETALHAMENTO.DATA.ordinal() );
		tabDet.setTamColuna( 50, DETALHAMENTO.HORA.ordinal() );
		tabDet.setTamColuna( 60, DETALHAMENTO.PLACA.ordinal() );
		tabDet.setTamColuna( 40, DETALHAMENTO.CODTRAN.ordinal() );
		tabDet.setTamColuna( 240, DETALHAMENTO.NOMETRAN.ordinal() );
		tabDet.setTamColuna( 40, DETALHAMENTO.CODFOR.ordinal() );
		tabDet.setTamColuna( 200, DETALHAMENTO.NOMEFOR.ordinal() );
		tabDet.setTamColuna( 100, DETALHAMENTO.PESOLIQUIDO.ordinal() );
		tabDet.setTamColuna( 50, DETALHAMENTO.RENDA.ordinal() );
//		tabDet.setTamColuna( 50, DETALHAMENTO.CODCOMPRA.ordinal() );
		tabDet.setColunaInvisivel( DETALHAMENTO.CODCOMPRA.ordinal() );
		// tabDet.setColunaInvisivel( 2 );

	}

	public void montaGrid() {

		try {

			StringBuilder sql = new StringBuilder();

			sql.append( "select " );
			sql.append( "rm.ticket, rm.codtiporecmerc, rm.status, rm.dtins data, rm.hins hora, rm.placaveiculo placa, rm.codtran, tr.nometran, rm.codfor, fr.nomefor, " );

			sql.append( "(select first 1 cast(ic.qtditcompra as decimal(15,0)) from eqitrecmercitcp irc, cpitcompra ic ");
			sql.append( "where irc.codemp=rm.codemp and irc.codfilial=rm.codfilial and irc.ticket=rm.ticket ");
			sql.append( "and ic.codemp=irc.codempcp and ic.codfilial=irc.codfilialcp and ic.codcompra=irc.codcompra and ic.coditcompra=irc.coditcompra) qtditcompra, ");
					
			sql.append( "rm.rendaamostragem renda, ");
			
			sql.append( "(select first 1 irc.codcompra from eqitrecmercitcp irc ");
			sql.append( "where irc.codemp=rm.codemp and irc.codfilial=rm.codfilial and irc.ticket=rm.ticket) codcompra ");

			sql.append( "from eqrecmerc rm, vdtransp tr, cpforneced fr " );
			sql.append( "where tr.codemp=rm.codemptr and tr.codfilial=rm.codfilialtr and tr.codtran=rm.codtran " );
			sql.append( "and fr.codemp=rm.codempfr and fr.codfilial=rm.codfilialfr and fr.codfor=rm.codfor ");
			sql.append( "and rm.codemp=? and rm.codfilial=? " );
			sql.append( "and rm.dtins between ? and ? " );

			StringBuffer status = new StringBuffer( "" );

			boolean primeiro = true;

			for ( int i = 0; i < tabstatus.getNumLinhas(); i++ ) {

				if ( (Boolean) tabstatus.getValor( i, 0 ) ) {

					if ( primeiro ) {
						sql.append( " and rm.status in (" );
					}
					else {
						sql.append( "," );
					}

					sql.append( "'" + tabstatus.getValor( i, 1 ) + "'" );

					primeiro = false;
				}

				if ( i == tabstatus.getNumLinhas() - 1 && !primeiro ) {
					sql.append( " ) " );
				}

			}

			if ( status.length() > 0 ) {
				sql.append( " and rm.status in (" );
				sql.append( status );
				sql.append( ") " );
			}

			if ( txtCodFor.getVlrInteger() > 0 ) {
				sql.append( " and rm.codempfr=? and rm.codfilialfr=? and rm.codfor=? " );
			}
			
			if ( txtCodProd.getVlrInteger() > 0 ) {
				sql.append( " and rm.codemppd=? and rm.codfilialpd=? and rm.codprod=? " );
			}

			System.out.println( "SQL:" + sql.toString() );

			PreparedStatement ps = con.prepareStatement( sql.toString() );

			int iparam = 1;

			ps.setInt( iparam++, Aplicativo.iCodEmp );
			ps.setInt( iparam++, ListaCampos.getMasterFilial( "EQRECMERC" ) );
			ps.setDate( iparam++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( iparam++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

			if ( txtCodFor.getVlrInteger() > 0 ) {
				ps.setInt( iparam++, lcForneced.getCodEmp() );
				ps.setInt( iparam++, lcForneced.getCodFilial() );
				ps.setInt( iparam++, txtCodFor.getVlrInteger() );
			}
			
			if ( txtCodProd.getVlrInteger() > 0 ) {
				ps.setInt( iparam++, lcProduto.getCodEmp() );
				ps.setInt( iparam++, lcProduto.getCodFilial() );
				ps.setInt( iparam++, txtCodProd.getVlrInteger() );				
			}

			ResultSet rs = ps.executeQuery();

			tabDet.limpa();

			int row = 0;
			
			DAORecMerc recmerc = null;
			BigDecimal peso1 = new BigDecimal(0);
			BigDecimal peso2 = new BigDecimal(0);
			BigDecimal pesoliquido = new BigDecimal(0);
			String status_recmerc = null;

			while ( rs.next() ) {

				tabDet.adicLinha();
				
				peso1 = new BigDecimal(0);
				peso2 = new BigDecimal(0);
				pesoliquido = new BigDecimal(0);

				status_recmerc = rs.getString( "status" );
				
				imgColuna = DAORecMerc.getImagem( rs.getString( "status" ), DAORecMerc.IMG_TAMANHO_M );

				tabDet.setValor( imgColuna, row, DETALHAMENTO.STATUS.ordinal() );
				
				tabDet.setValor( status_recmerc, row, DETALHAMENTO.STATUSTXT.ordinal() );
				tabDet.setValor( rs.getInt( DETALHAMENTO.TICKET.toString().trim() ), row, DETALHAMENTO.TICKET.ordinal() );
				tabDet.setValor( rs.getInt( DETALHAMENTO.CODTIPORECMERC.toString().trim() ), row, DETALHAMENTO.CODTIPORECMERC.ordinal() );
				tabDet.setValor( Funcoes.dateToStrDate( rs.getDate( DETALHAMENTO.DATA.toString() ) ), row, DETALHAMENTO.DATA.ordinal() );
				tabDet.setValor( rs.getString( DETALHAMENTO.HORA.toString().trim() ), row, DETALHAMENTO.HORA.ordinal() );
				tabDet.setValor( rs.getString( DETALHAMENTO.PLACA.toString().trim() ), row, DETALHAMENTO.PLACA.ordinal() );
				tabDet.setValor( rs.getInt( DETALHAMENTO.CODTRAN.toString().trim() ), row, DETALHAMENTO.CODTRAN.ordinal() );
				tabDet.setValor( rs.getString( DETALHAMENTO.NOMETRAN.toString().trim() ), row, DETALHAMENTO.NOMETRAN.ordinal() );
				tabDet.setValor( rs.getInt( DETALHAMENTO.CODFOR.toString().trim() ), row, DETALHAMENTO.CODFOR.ordinal() );
				tabDet.setValor( rs.getString( DETALHAMENTO.NOMEFOR.toString().trim() ), row, DETALHAMENTO.NOMEFOR.ordinal() );
				
				if(status_recmerc.equals( DAORecMerc.STATUS_PEDIDO_COMPRA_EMITIDO.getValue() )) {
					
					pesoliquido = rs.getBigDecimal( "qtditcompra" );
					
				}
				else if(status_recmerc.equals( DAORecMerc.STATUS_RECEBIMENTO_FINALIZADO.getValue() )){
				
				
					recmerc = new DAORecMerc( null, rs.getInt( DETALHAMENTO.TICKET.toString().trim() ), con );
					
					HashMap<String, Object> p1 = recmerc.getPrimeirapesagem();
	
					peso1 = (BigDecimal) p1.get( "peso" );
	
					HashMap<String, Object> p2 = recmerc.getSegundapesagem();
	
					peso2 = (BigDecimal) p2.get( "peso" );
	
					if(peso2!=null && peso1!=null) {
						
						pesoliquido = peso1.subtract( peso2 ).setScale( 0 );
						BigDecimal desconto = recmerc.getDesconto();
						 
						if(desconto!=null && desconto.floatValue()>0) {
							BigDecimal pesodesc = pesoliquido.multiply( desconto.divide( new BigDecimal(100) ) );
							pesoliquido = pesoliquido.subtract( pesodesc );
							System.out.println("Aplicado desconto no peso de :" + pesodesc.toString());
						}
						
					}
										
				}
				
				Integer renda = rs.getInt( DETALHAMENTO.RENDA.toString().trim() );
				
				tabDet.setValor( pesoliquido, row, DETALHAMENTO.PESOLIQUIDO.ordinal() );
				tabDet.setValor( renda > 0 ? renda : 0, row, DETALHAMENTO.RENDA.ordinal() );
				
				tabDet.setValor( rs.getInt( DETALHAMENTO.CODCOMPRA.toString().trim() ), row, DETALHAMENTO.CODCOMPRA.ordinal() );

				row++;

			}

			if ( tabDet.getRowCount() > 0 ) {
				tabDet.setLinhaSel( 0 );
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	public void actionPerformed( ActionEvent e ) {

		if ( e.getSource() == btAtualiza ) {
			montaGrid();
		}
		else if ( e.getSource() == btNovo ) {
			novoRecebimento();
		}
		else if ( e.getSource() == btEditar ) {
			abreRecMerc();
		}
		else if ( e.getSource() == btCompra ) {
			geraCompra();
		}
		else if ( e.getSource() == btCotacao ) {
			geraCotacao();
		}


	}

	private void novoRecebimento() {

		FRecMerc recebimento = new FRecMerc( true );

		try {

			Aplicativo.telaPrincipal.criatela( "Recepção de mercadorias", recebimento, con );
			recebimento.setTelaMae( this );

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	public void valorAlterado( TabelaSelEvent e ) {

		/*
		 * if ( e.getTabela() == tabOrcamentos && tabOrcamentos.getLinhaSel() > -1 && !carregandoOrcamentos ) { buscaItensVenda( (Integer)tabOrcamentos.getValor( tabOrcamentos.getLinhaSel(), VENDAS.CODVENDA.ordinal() ), "V" ); }
		 */
	}

	private void abreRecMerc() {

		FRecMerc recmerc = null;

		try {

			if ( tabDet.getLinhaSel() > -1 ) {

				if ( Aplicativo.telaPrincipal.temTela( FRecMerc.class.getName() ) ) {
					recmerc = (FRecMerc) Aplicativo.telaPrincipal.getTela( FRecMerc.class.getName() );
				}
				else {
					recmerc = new FRecMerc( false );
					Aplicativo.telaPrincipal.criatela( "Recepção de mercadorias", recmerc, con );
				}

				int ticket = (Integer) tabDet.getValor( tabDet.getLinhaSel(), DETALHAMENTO.TICKET.ordinal() );
				int codtiporecmerc = (Integer) tabDet.getValor( tabDet.getLinhaSel(), DETALHAMENTO.CODTIPORECMERC.ordinal() );

				recmerc.exec( ticket, codtiporecmerc, this );
			}
			else {
				Funcoes.mensagemInforma( this, "Não há nenhum registro selecionado para edição!" );
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	public void mouseClicked( MouseEvent mevt ) {

		JTablePad tabEv = (JTablePad) mevt.getSource();

		if ( mevt.getClickCount() == 2 ) {
			if ( tabEv == tabDet && tabEv.getLinhaSel() > -1 ) {

				abreRecMerc();

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

		if ( e.getSource() == btAtualiza && e.getKeyCode() == KeyEvent.VK_ENTER ) {
			btAtualiza.doClick();
		}
		else if ( e.getSource() == tabDet ) {
			if ( e.getKeyCode() == KeyEvent.VK_SPACE ) {
				btEditar.doClick();
			}
			else if ( e.getKeyCode() == KeyEvent.VK_ENTER ) {
				btEditar.doClick();
			}
			else if ( e.getKeyCode() == KeyEvent.VK_F5 ) {
				btAtualiza.doClick();
			}
			else if ( e.getKeyCode() == KeyEvent.VK_F12 ) {
				btNovo.doClick();
			}
			else if ( e.getKeyCode() == KeyEvent.VK_F11 ) {
				btCompra.doClick();
			}
		}

	}

	public void keyReleased( KeyEvent e ) {

	}

	public void keyTyped( KeyEvent e ) {

	}

	public void beforeCarrega( CarregaEvent e ) {

	}

	public void afterCarrega( CarregaEvent e ) {

		montaGrid();

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		montaGrid();
		lcForneced.setConexao( con );
		lcProduto.setConexao( con );

	}

	public void valorAlterado( TabelaEditEvent evt ) {

	}

	private void selectAll( JTablePad tab ) {

		for ( int i = 0; i < tab.getNumLinhas(); i++ ) {
			tab.setValor( new Boolean( true ), i, 0 );
		}
	}

	private void limpaNaoSelecionados( JTablePad tab ) {

		int linhas = tab.getNumLinhas();
		int pos = 0;
		try {
			for ( int i = 0; i < linhas; i++ ) {
				if ( tab.getValor( i, 0 ) != null && ! ( (Boolean) tab.getValor( i, 0 ) ).booleanValue() ) { // xxx
					tab.tiraLinha( i );
					i--;
				}
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	public void stateChanged( ChangeEvent cevt ) {

		if ( cevt.getSource() == tabbedAbas ) {
			if ( tabbedAbas.getSelectedIndex() == 1 ) {
				// geraTabTemp();
			}
		}
	}

	private Integer getPlanoPag() {

		Integer codplanopag = null;

		try {

			DLInfoPlanoPag dl = new DLInfoPlanoPag( this, con );
			dl.setVisible( true );

			if ( dl.OK ) {
				codplanopag = dl.getValor();
				dl.dispose();
			}
			else {
				dl.dispose();
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return codplanopag;
	}

	private void abrecompra( Integer codcompra ) {

		if ( fPrim.temTela( "Compra" ) == false ) {
			FCompra tela = new FCompra();
			fPrim.criatela( "Compra", tela, con );
			tela.exec( codcompra );
		}

	}

	private void abreSolicitacao( Integer codsolicitacao, Integer codfor, Integer renda ) {
		
		if ( fPrim.temTela( "Compra" ) == false ) {
			FCotacaoPrecos tela = new FCotacaoPrecos();
			fPrim.criatela( "Cotações de preços ", tela, con );
			
			tela.abreCotacao( codsolicitacao, codfor, renda  );
		}

	}

	
	private void geraCompra() {

		StringBuilder sql = new StringBuilder();

		Integer ticket = null;
		BigDecimal pesoliq = null;
		BigDecimal peso1 = null;
		BigDecimal peso2 = null;
		String unid = null;
		PreparedStatement ps = null;

		DAORecMerc recmerc = null;
		
		Integer codcompra = null;

		try {

			if ( tabDet.getLinhaSel() > -1 ) {

				ticket = (Integer) tabDet.getValor( tabDet.getLinhaSel(), DETALHAMENTO.TICKET.ordinal() );
				codcompra = (Integer) tabDet.getValor( tabDet.getLinhaSel(), DETALHAMENTO.CODCOMPRA.ordinal() );

				recmerc = new DAORecMerc( this, ticket, con );
				String statustxt = (String) tabDet.getValor( tabDet.getLinhaSel(), DETALHAMENTO.STATUSTXT.ordinal() ); 
				if ( statustxt.equals( DAORecMerc.STATUS_RECEBIMENTO_FINALIZADO.getValue() ) || statustxt.equals( DAORecMerc.STATUS_PEDIDO_COMPRA_EMITIDO.getValue() ) || statustxt.equals( DAORecMerc.STATUS_NOTA_ENTRADA_EMITIDA.getValue() ) ) {

					if(codcompra!=null && codcompra>0) {
						abrecompra( codcompra );
					}
					else {
					
						if ( Funcoes.mensagemConfirma( this, "Confirma a geração do pedido de compra para o ticket nro.:" + ticket.toString() + " ?" ) == JOptionPane.YES_OPTION ) {

							codcompra = recmerc.geraCompra(false);

							if ( codcompra != null && codcompra > 0 ) {

								abrecompra( codcompra );

							}
						}
					}

				}
				else {
					Funcoes.mensagemInforma( this, "O recebimento selecionado ainda não foi finalizado!" );
				}

			}
			else {
				Funcoes.mensagemInforma( this, "Selecione um ticket no grid!" );
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}
	
	private void geraCotacao() {

		StringBuilder sql = new StringBuilder();

		Integer forneced = null;

		DAORecMerc recmerc = null;
		
		Integer renda = null;

		try {

			if ( tabDet.getLinhaSel() > -1 ) {

				

				if ( tabDet.getValor( tabDet.getLinhaSel(), DETALHAMENTO.STATUSTXT.ordinal() ).equals( DAORecMerc.STATUS_RECEBIMENTO_FINALIZADO.getValue() ) ) {

					forneced = (Integer) tabDet.getValor( tabDet.getLinhaSel(), DETALHAMENTO.CODFOR.ordinal() );
					
					if ( Funcoes.mensagemConfirma( this, "Confirma a geração de cotação para o ticket nro.:" + forneced.toString() + " ?" ) == JOptionPane.YES_OPTION ) {

						Integer codsolicitacao = DAORecMerc.getSolicitacao(this);
						
						renda = (Integer) tabDet.getValor( tabDet.getLinhaSel(), DETALHAMENTO.RENDA.ordinal() );
						
						if ( codsolicitacao != null && codsolicitacao > 0 ) {

							abreSolicitacao( codsolicitacao, forneced, renda );

						}
					}

				}
				else {
					Funcoes.mensagemInforma( this, "O recebimento selecionado ainda não foi finalizado!" );
				}

			}
			else {
				Funcoes.mensagemInforma( this, "Selecione um ticket no grid!" );
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

}
