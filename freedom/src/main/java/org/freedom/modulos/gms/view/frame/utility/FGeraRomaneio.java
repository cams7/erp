/**
 * @version 15/07/2011 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.gms.view.frame.utility <BR>
 *         Classe: @(#)FGeraRomaneio.java <BR>
 * 
 *         Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *         modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *         na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *         Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *         sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *         Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *         Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *         escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA <BR>
 * 
 *         Tela para geração de romaneio de carga.
 * 
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

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
import org.freedom.modulos.gms.view.frame.crud.detail.FCompra;
import org.freedom.modulos.gms.view.frame.crud.detail.FCotacaoPrecos;
import org.freedom.modulos.gms.view.frame.crud.detail.FExpedicao;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.std.view.frame.crud.detail.FVenda;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCliente;
import org.freedom.modulos.std.view.frame.crud.tabbed.FTransp;

public class FGeraRomaneio extends FFilho implements ActionListener, TabelaSelListener, MouseListener, KeyListener, CarregaListener, TabelaEditListener, ChangeListener {

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

	private JPanelPad panelBotoesPesquisa = new JPanelPad( 37, 37 );

	private JPanelPad panelBotoesSelecionados = new JPanelPad( 37, 37 );

	private JPanelPad panelGridDet = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 2, 1 ) );

	private JPanelPad panelTabPesquisa = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad panelTabSelecionados = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTablePad tabPesquisa = null;

	private JTablePad tabSelecionados = null;

	// *** Labels

	private JLabelPad sepdet = new JLabelPad();

	// *** Geral

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldFK txtNomeCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtFoneCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 12, 0 );

	private JTextFieldFK txtCelCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 12, 0 );

	private JTextFieldFK txtContatoCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodTran = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeTran = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	// *** Listacampos

	private ListaCampos lcCliente = new ListaCampos( this );

	// *** Botões
	private JButtonPad btAtualiza = new JButtonPad( Icone.novo( "btAtualiza.png" ) );

	private JButtonPad btEditar = new JButtonPad( Icone.novo( "btEditar.gif" ) );

	private JButtonPad btBusca = new JButtonPad( "Buscar", Icone.novo( "btPesquisa.png" ) );

	private JButtonPad btAdicSel = new JButtonPad( Icone.novo( "btExecuta.png" ) );

	private JButtonPad btSelTudoPesq = new JButtonPad( Icone.novo( "btTudo.png" ) );

	private JButtonPad btSelNadaPesq = new JButtonPad( Icone.novo( "btNada.png" ) );

	private JButtonPad btSelTudoSel = new JButtonPad( Icone.novo( "btTudo.png" ) );

	private JButtonPad btSelNadaSel = new JButtonPad( Icone.novo( "btNada.png" ) );

	private JButtonPad btGeraRomaneio = new JButtonPad( Icone.novo( "btGerar.png" ) );

	private JButtonPad btSair = new JButtonPad( "Sair", Icone.novo( "btSair.png" ) );

	private JButtonPad btResetSel = new JButtonPad( Icone.novo( "btVassoura.png" ) );
	
	private JButtonPad btExcluiSel = new JButtonPad( Icone.novo( "btExcluir.png" ) );

	private ListaCampos lcProduto = new ListaCampos( this, "PD" );

	private ListaCampos lcTran = new ListaCampos( this, "TN" );

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private ImageIcon imgColuna = Icone.novo( "clAgdCanc.png" );

	private ImageIcon imgCancelada = Icone.novo( "clAgdCanc.png" );

	private ImageIcon imgEmitida = Icone.novo( "clAgdFin.png" );

	private ImageIcon imgAberto = Icone.novo( "clAgdPend.png" );

	private ImageIcon imgPedidoEmitido = Icone.novo( "clCheckYellow.png" );

	private ImageIcon imgPendente = Icone.novo( "clAgdPend.png" );

	private Date dataRoma = null;
	
	// Enums

	private enum enum_grid_pesquisa {
		SEL, STATUS, STATUSTXT, CODVENDA, TIPOVENDA, DOCVENDA, CODCLI, RAZCLI, CODTRAN, RAZTRAN, PLACA, VALOR, QTD, PESOLIQ, PESOBRUT;
	}

	private enum enum_grid_selecionados {
		SEL, CODVENDA, TIPOVENDA, DOCVENDA, CODCLI, RAZCLI, CODTRAN, RAZTRAN, PLACA, VALOR, QTD, PESOLIQ, PESOBRUT;
	}

	private Integer ticket = null;
	
	private FExpedicao expedicao = null;

	public FGeraRomaneio() {

		super( false );

		setTitulo( "Painel de geração de Romaneio de Carga", this.getClass().getName() );
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

		txtDataini.setVlrDate( new Date() );
		txtDatafim.setVlrDate( new Date() );

	}

	private void montaListaCampos() {

		lcCliente.add( new GuardaCampo( txtCodCli, "CodcLI", "Cód.Cli.", ListaCampos.DB_PK, false ) );
		lcCliente.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCliente.add( new GuardaCampo( txtNomeCli, "NomeCli", "Nome do cliente", ListaCampos.DB_SI, false ) );
		lcCliente.add( new GuardaCampo( txtContatoCli, "ContCli", "Contato", ListaCampos.DB_SI, false ) );
		lcCliente.add( new GuardaCampo( txtFoneCli, "FoneCli", "Telefone", ListaCampos.DB_SI, false ) );

		lcCliente.setWhereAdic( "ATIVOCLI='S'" );
		lcCliente.montaSql( false, "CLIENTE", "VD" );
		lcCliente.setReadOnly( true );

		txtCodCli.setTabelaExterna( lcCliente, FCliente.class.getCanonicalName() );
		txtCodCli.setFK( true );
		txtCodCli.setNomeCampo( "CodCli" );

		// * Produto (

		lcProduto.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcProduto.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );

		txtCodProd.setTabelaExterna( lcProduto, FProduto.class.getCanonicalName() );
		txtCodProd.setNomeCampo( "CodProd" );
		txtCodProd.setFK( true );

		lcProduto.setReadOnly( true );
		lcProduto.montaSql( false, "PRODUTO", "EQ" );

		// * Transportadora

		lcTran.add( new GuardaCampo( txtCodTran, "CodTran", "Cód.For.", ListaCampos.DB_PK, false ) );
		lcTran.add( new GuardaCampo( txtNomeTran, "NomeTran", "Nome da transportadora", ListaCampos.DB_SI, false ) );

		txtCodTran.setTabelaExterna( lcTran, FTransp.class.getCanonicalName() );
		txtCodTran.setNomeCampo( "CodTran" );
		txtCodTran.setFK( true );

		lcTran.setReadOnly( true );
		lcTran.montaSql( false, "TRANSP", "VD" );

	}

	private void adicToolTips() {

		btAtualiza.setToolTipText( "Executa pesquisa - (F5)" );
		btEditar.setToolTipText( "Abre venda - (ENTER/SPACE)" );

		btSelTudoPesq.setToolTipText( "Seleciona tudo" );
		btSelTudoSel.setToolTipText( "Seleciona tudo" );
		btAdicSel.setToolTipText( "Adiciona selecionados" );
		btResetSel.setToolTipText( "Limpar seleção" );
		btExcluiSel.setToolTipText( "Excluir item selecionado" );
		btGeraRomaneio.setToolTipText( "Gerar romaneio" );
		
		btSelTudoPesq.setToolTipText( "Desmarca tudo" );
		btSelTudoSel.setToolTipText( "Desmarca tudo" );

	}

	private void montaListeners() {

		btAtualiza.addActionListener( this );
		btEditar.addActionListener( this );

		btSelTudoPesq.addActionListener( this );
		btSelTudoSel.addActionListener( this );
		btSelNadaPesq.addActionListener( this );
		btSelNadaSel.addActionListener( this );
		btResetSel.addActionListener( this );
		btExcluiSel.addActionListener( this );
		btGeraRomaneio.addActionListener( this );
		btAdicSel.addActionListener( this );

		tabPesquisa.addTabelaSelListener( this );
		tabPesquisa.addMouseListener( this );
		tabPesquisa.addKeyListener( this );

		this.addKeyListener( this );

	}

	private void montaTela() {

		getTela().add( panelGeral, BorderLayout.CENTER );
		panelGeral.add( panelMaster, BorderLayout.NORTH );

		// ***** Cabeçalho

		panelMaster.adic( panelFiltros, 4, 0, 935, 120 );

		panelFiltros.adic( btAtualiza, 740, 0, 30, 89 );
		panelFiltros.adic( txtDataini, 7, 20, 70, 20, "Data Inicial" );
		panelFiltros.adic( txtDatafim, 80, 20, 70, 20, "Data Final" );
		panelFiltros.adic( txtCodCli, 153, 20, 70, 20, "Cód.Cli." );
		panelFiltros.adic( txtRazCli, 226, 20, 200, 20, "Razão social do cliente" );

		panelFiltros.adic( txtCodTran, 429, 20, 70, 20, "Cód.Tran." );
		panelFiltros.adic( txtNomeTran, 502, 20, 200, 20, "Nome do transportador" );

		panelFiltros.adic( txtCodProd, 153, 60, 70, 20, "Cód.Prod." );

		panelFiltros.adic( txtDescProd, 226, 60, 200, 20, "Descrição do produto" );

		// ***** Abas

		panelGeral.add( panelAbas, BorderLayout.CENTER );
		panelGeral.add( panelAbas );
		panelAbas.add( panelDet );

		// ***** Detalhamento

		panelDet.add( panelTabDet, BorderLayout.NORTH );
		panelDet.add( panelGridDet, BorderLayout.CENTER );

		panelGridDet.add( panelTabPesquisa );
		panelGridDet.add( panelTabSelecionados );

		JScrollPane spPesquisa = new JScrollPane( tabPesquisa );
		JScrollPane spSelecionados = new JScrollPane( tabSelecionados );

		panelBotoesPesquisa.adic( btSelTudoPesq, 1, 1, 30, 30 );
		panelBotoesPesquisa.adic( btSelNadaPesq, 1, 32, 30, 30 );
		panelBotoesPesquisa.adic( btAdicSel, 1, 63, 30, 30 );

		panelBotoesSelecionados.adic( btSelTudoSel, 1, 1, 30, 30 );
		panelBotoesSelecionados.adic( btSelNadaSel, 1, 32, 30, 30 );
		panelBotoesSelecionados.adic( btExcluiSel, 1, 63, 30, 30 );
		panelBotoesSelecionados.adic( btResetSel, 1, 94, 30, 30 );

		panelBotoesSelecionados.adic( btGeraRomaneio, 1, 125, 30, 30 );

		panelTabPesquisa.setBorder( SwingParams.getPanelLabel( "Seleção de vendas", SwingParams.COR_VERDE_FREEDOM ) );
		panelTabSelecionados.setBorder( SwingParams.getPanelLabel( "Vendas selecionadas", Color.BLUE ) );

		panelTabPesquisa.add( spPesquisa, BorderLayout.CENTER );
		panelTabPesquisa.add( panelBotoesPesquisa, BorderLayout.EAST );

		panelTabSelecionados.add( spSelecionados, BorderLayout.CENTER );
		panelTabSelecionados.add( panelBotoesSelecionados, BorderLayout.EAST );

		// ***** Rodapé

		Color statusColor = new Color( 111, 106, 177 );
		Font statusFont = SwingParams.getFontpadmed();

		panelLegenda.setBorder( null );

		panelGeral.add( panelSouth, BorderLayout.SOUTH );
		panelSouth.setBorder( BorderFactory.createEtchedBorder() );

		// panelNavegador.add( btExcluir );
		panelNavegador.add( btEditar );

		panelSouth.add( panelNavegador, BorderLayout.WEST );
		panelSouth.add( panelLegenda, BorderLayout.CENTER );
		panelSouth.add( adicBotaoSair(), BorderLayout.EAST );

	}

	private void criaTabelas() {

		// Tabela de pesquisa

		tabPesquisa = new JTablePad();
		tabPesquisa.setRowHeight( 21 );

		tabPesquisa.adicColuna( "" ); // Seleção
		tabPesquisa.adicColuna( "" ); // Status
		tabPesquisa.adicColuna( "" ); // Status em texto
		tabPesquisa.adicColuna( "Cod.Vend." ); // Codvenda
		tabPesquisa.adicColuna( "Tipo Vend." ); // Tipovenda
		
		tabPesquisa.adicColuna( "Doc." ); // DocVenda
		tabPesquisa.adicColuna( "Cód.Cli." ); // CodCli
		tabPesquisa.adicColuna( "Razão social do cliente" ); // RazCli
		tabPesquisa.adicColuna( "Cód.T." ); // CodTran
		tabPesquisa.adicColuna( "Transportadora" ); // RazTran
		tabPesquisa.adicColuna( "Placa" ); // Placa
		tabPesquisa.adicColuna( "Valor" ); // Valor
		tabPesquisa.adicColuna( "Qtd." ); // Qtd
		tabPesquisa.adicColuna( "Peso Liquido" ); // Pesoliq
		tabPesquisa.adicColuna( "Peso Bruto" ); // PesoBrut

		tabPesquisa.setTamColuna( 21, enum_grid_pesquisa.SEL.ordinal() );
		tabPesquisa.setTamColuna( 20, enum_grid_pesquisa.STATUS.ordinal() );
		tabPesquisa.setColunaInvisivel( enum_grid_pesquisa.STATUSTXT.ordinal() );
		tabPesquisa.setTamColuna( 50, enum_grid_pesquisa.CODVENDA.ordinal() );
		tabPesquisa.setColunaInvisivel( enum_grid_pesquisa.TIPOVENDA.ordinal() );
		tabPesquisa.setTamColuna( 50, enum_grid_pesquisa.DOCVENDA.ordinal() );
		tabPesquisa.setTamColuna( 60, enum_grid_pesquisa.CODCLI.ordinal() );
		tabPesquisa.setTamColuna( 150, enum_grid_pesquisa.RAZCLI.ordinal() );
		tabPesquisa.setTamColuna( 40, enum_grid_pesquisa.CODTRAN.ordinal() );
		tabPesquisa.setTamColuna( 150, enum_grid_pesquisa.RAZTRAN.ordinal() );
		tabPesquisa.setTamColuna( 60, enum_grid_pesquisa.PLACA.ordinal() );
		tabPesquisa.setTamColuna( 70, enum_grid_pesquisa.VALOR.ordinal() );
		tabPesquisa.setTamColuna( 70, enum_grid_pesquisa.QTD.ordinal() );
		tabPesquisa.setTamColuna( 70, enum_grid_pesquisa.PESOLIQ.ordinal() );
		tabPesquisa.setTamColuna( 70, enum_grid_pesquisa.PESOBRUT.ordinal() );

		tabPesquisa.setColunaEditavel( enum_grid_pesquisa.SEL.ordinal(), true );

		// Tabela de selecionados

		tabSelecionados = new JTablePad();
		tabSelecionados.setRowHeight( 21 );

		tabSelecionados.adicColuna( "" ); // Seleção
		tabSelecionados.adicColuna( "Cod.Vend." ); // Codvenda
		tabSelecionados.adicColuna( "Tipo Vend." ); // Codvenda
		
		tabSelecionados.adicColuna( "Doc." ); // DocVenda
		tabSelecionados.adicColuna( "Cód.Cli." ); // CodCli
		tabSelecionados.adicColuna( "Razão social do cliente" ); // RazCli
		tabSelecionados.adicColuna( "Cód.T." ); // CodTran
		tabSelecionados.adicColuna( "Transportadora" ); // RazTran
		tabSelecionados.adicColuna( "Placa" ); // Placa
		tabSelecionados.adicColuna( "Valor" ); // Valor
		tabSelecionados.adicColuna( "Qtd." ); // Qtd
		tabSelecionados.adicColuna( "Peso Liquido" ); // Pesoliq
		tabSelecionados.adicColuna( "Peso Bruto" ); // PesoBrut

		tabSelecionados.setTamColuna( 21, enum_grid_selecionados.SEL.ordinal() );
		tabSelecionados.setTamColuna( 50, enum_grid_selecionados.CODVENDA.ordinal() );
		tabSelecionados.setColunaInvisivel( enum_grid_selecionados.TIPOVENDA.ordinal() );
		tabSelecionados.setTamColuna( 50, enum_grid_selecionados.DOCVENDA.ordinal() );
		tabSelecionados.setTamColuna( 60, enum_grid_selecionados.CODCLI.ordinal() );
		tabSelecionados.setTamColuna( 150, enum_grid_selecionados.RAZCLI.ordinal() );
		tabSelecionados.setTamColuna( 40, enum_grid_selecionados.CODTRAN.ordinal() );
		tabSelecionados.setTamColuna( 150, enum_grid_selecionados.RAZTRAN.ordinal() );
		tabSelecionados.setTamColuna( 60, enum_grid_selecionados.PLACA.ordinal() );
		tabSelecionados.setTamColuna( 70, enum_grid_selecionados.VALOR.ordinal() );
		tabSelecionados.setTamColuna( 70, enum_grid_selecionados.QTD.ordinal() );
		tabSelecionados.setTamColuna( 70, enum_grid_selecionados.PESOLIQ.ordinal() );
		tabSelecionados.setTamColuna( 70, enum_grid_selecionados.PESOBRUT.ordinal() );

		tabSelecionados.setColunaEditavel( enum_grid_selecionados.SEL.ordinal(), true );

	}

	public void montaGrid() {

		try {

			StringBuilder sql = new StringBuilder();

			sql.append( "select " );
			sql.append( "vd.statusvenda, vd.codvenda, vd.tipovenda, vd.docvenda, vd.codcli, cl.razcli, fr.codtran, tr.raztran, fr.placafretevd, vd.vlrliqvenda, fr.qtdfretevd, fr.pesoliqvd, fr.pesobrutvd " );

			sql.append( "from vdvenda vd, vdcliente cl, vdfretevd fr, vdtransp tr " );

			sql.append( "where  " );

			sql.append( "cl.codemp=vd.codempcl and cl.codfilial=vd.codfilialcl and cl.codcli=vd.codcli and " );
			sql.append( "fr.codemp=vd.codemp and fr.codfilial=vd.codfilial and fr.codvenda=vd.codvenda and fr.tipovenda=vd.tipovenda and " );
			sql.append( "tr.codemp=fr.codemptn and tr.codfilial=fr.codfilialtn and tr.codtran=fr.codtran and " );
			sql.append( "vd.codemp=? and vd.codfilial=? and vd.dtsaidavenda between ? and ? " );
			sql.append( " and not exists ( select codvenda from vditromaneio ir where ir.codempva=vd.codemp and ir.codfilialva=vd.codfilial and ir.tipovenda=vd.tipovenda and ir.codvenda=vd.codvenda ) " );
			
			StringBuffer status = new StringBuffer( "" );

			if ( txtCodCli.getVlrInteger() > 0 ) {
				sql.append( " and vd.codempcl=? and vd.codfilialcl=? and vd.codcli=? " );
			}

			if ( txtCodProd.getVlrInteger() > 0 ) {
				sql.append( " and exists( select codprod from vditvenda iv where iv.codemp=vd.codemp and iv.codfilial=vd.codfilial and iv.codvenda=vd.codvenda and iv.tipovenda=vd.tipovenda and iv.codemppd=? and iv.codfilialpd=? and iv.codprod=? )" );
			}

			if ( txtCodTran.getVlrInteger() > 0 ) {
				sql.append( " and tr.codemp=? and tr.codfilial=? and tr.codtran=? " );
			}

			System.out.println( "SQL:" + sql.toString() );

			PreparedStatement ps = con.prepareStatement( sql.toString() );

			int iparam = 1;

			ps.setInt( iparam++, Aplicativo.iCodEmp );
			ps.setInt( iparam++, ListaCampos.getMasterFilial( "VDVENDA" ) );
			ps.setDate( iparam++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( iparam++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

			if ( txtCodCli.getVlrInteger() > 0 ) {
				ps.setInt( iparam++, lcCliente.getCodEmp() );
				ps.setInt( iparam++, lcCliente.getCodFilial() );
				ps.setInt( iparam++, txtCodCli.getVlrInteger() );
			}

			if ( txtCodProd.getVlrInteger() > 0 ) {
				ps.setInt( iparam++, lcProduto.getCodEmp() );
				ps.setInt( iparam++, lcProduto.getCodFilial() );
				ps.setInt( iparam++, txtCodProd.getVlrInteger() );
			}

			if ( txtCodTran.getVlrInteger() > 0 ) {
				ps.setInt( iparam++, lcTran.getCodEmp() );
				ps.setInt( iparam++, lcTran.getCodFilial() );
				ps.setInt( iparam++, txtCodTran.getVlrInteger() );
			}

			ResultSet rs = ps.executeQuery();

			tabPesquisa.limpa();

			int row = 0;

			BigDecimal pesoliquido = new BigDecimal( 0 );
			BigDecimal pesobruto = new BigDecimal( 0 );

			String status_venda = null;

			while ( rs.next() ) {

				tabPesquisa.adicLinha();

				pesoliquido = new BigDecimal( 0 );
				pesobruto = new BigDecimal( 0 );

				status_venda = rs.getString( "statusvenda" );

				if ( status_venda.length() > 0 && status_venda.substring( 0, 1 ).equals( "C" ) ) {
					imgColuna = imgCancelada;

				}
				else if ( status_venda.length() > 0 && ( status_venda.equals( "V2" ) || status_venda.equals( "V3" ) ) ) {
					imgColuna = imgEmitida;

				}
				else if ( status_venda.length() > 0 && status_venda.equals( "P2" ) ) {
					imgColuna = imgPendente;

				}
				else if ( status_venda.length() > 0 && status_venda.equals( "P3" ) ) {
					imgColuna = imgPedidoEmitido;

				}
				else if ( status_venda.equals( "P1" ) || status_venda.equals( "V1" ) ) {
					imgColuna = imgPendente;

				}
				else {
					imgColuna = imgPendente;

				}

				tabPesquisa.setValor( podeSel( status_venda ), row, enum_grid_pesquisa.SEL.ordinal() );

				tabPesquisa.setValor( imgColuna, row, enum_grid_pesquisa.STATUS.ordinal() );

				tabPesquisa.setValor( status_venda, row, enum_grid_pesquisa.STATUSTXT.ordinal() );

				tabPesquisa.setValor( rs.getInt( "CODVENDA" ), row, enum_grid_pesquisa.CODVENDA.ordinal() );
				tabPesquisa.setValor( rs.getInt( "DOCVENDA" ), row, enum_grid_pesquisa.DOCVENDA.ordinal() );
				tabPesquisa.setValor( rs.getString( "TIPOVENDA" ), row, enum_grid_pesquisa.TIPOVENDA.ordinal() );
				tabPesquisa.setValor( rs.getInt( "CODCLI" ), row, enum_grid_pesquisa.CODCLI.ordinal() );
				tabPesquisa.setValor( rs.getString( "RAZCLI" ).trim(), row, enum_grid_pesquisa.RAZCLI.ordinal() );
				tabPesquisa.setValor( rs.getInt( "CODTRAN" ), row, enum_grid_pesquisa.CODTRAN.ordinal() );
				tabPesquisa.setValor( rs.getString( "RAZTRAN" ).trim(), row, enum_grid_pesquisa.RAZTRAN.ordinal() );
				tabPesquisa.setValor( rs.getString( "PLACAFRETEVD" ), row, enum_grid_pesquisa.PLACA.ordinal() );
				tabPesquisa.setValor( rs.getBigDecimal( "VLRLIQVENDA" ), row, enum_grid_pesquisa.VALOR.ordinal() );
				tabPesquisa.setValor( rs.getBigDecimal( "QTDFRETEVD" ), row, enum_grid_pesquisa.QTD.ordinal() );
				tabPesquisa.setValor( rs.getBigDecimal( "PESOLIQVD" ), row, enum_grid_pesquisa.PESOLIQ.ordinal() );
				tabPesquisa.setValor( rs.getBigDecimal( "PESOBRUTVD" ), row, enum_grid_pesquisa.PESOBRUT.ordinal() );

				row++;

			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	private Boolean podeSel( String status_venda ) {

		if ( status_venda.length() > 0 && status_venda.substring( 0, 1 ).equals( "C" ) ) {
			return false;
		}
		else if ( status_venda.length() > 0 && ( status_venda.equals( "V2" ) || status_venda.equals( "V3" ) ) ) {
			return true;
		}
		else if ( status_venda.length() > 0 && status_venda.equals( "P2" ) ) {
			return false;
		}
		else if ( status_venda.length() > 0 && status_venda.equals( "P3" ) ) {
			return true;
		}
		else if ( status_venda.equals( "P1" ) || status_venda.equals( "V1" ) ) {
			return false;
		}
		else {
			return false;
		}

	}

	public void actionPerformed( ActionEvent e ) {

		if ( e.getSource() == btAtualiza ) {
			montaGrid();
		}
		else if ( e.getSource() == btEditar ) {
			abreVenda();
		}
		else if ( e.getSource() == btSelTudoPesq ) {
			selTudoPesq();
		}
		else if ( e.getSource() == btSelTudoSel ) {
			selTudoSel();
		}
		else if ( e.getSource() == btSelNadaPesq ) {
			selNada( tabPesquisa );
		}
		else if ( e.getSource() == btSelNadaSel ) {
			selNada( tabSelecionados );
		}
		else if ( e.getSource() == btAdicSel ) {
			adicSelecionados();
		}
		else if ( e.getSource() == btResetSel ) {
			tabSelecionados.limpa();
		}
		else if ( e.getSource() == btExcluiSel ) {
			removeSelecionados();
		}
		else if ( e.getSource() == btGeraRomaneio ) {
			geraRomaneio();
		}
		
	}

	private Integer getCodRoma() {
		
		Integer ret = 1;
		StringBuilder sql = new StringBuilder();
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		try {
			
			sql.append( "select coalesce( max(codroma), 0 ) + 1 from vdromaneio where codemp=? and codfilial=? " );
			
			ps = con.prepareStatement( sql.toString() );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDROMANEIO" ) );
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
			
				ret = rs.getInt( 1 );
				
			}
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	private void geraRomaneio() {
		
		StringBuilder sql = new StringBuilder();
		
		PreparedStatement ps = null;
		
		Integer codroma = null;
		
		try {
		
			if( tabSelecionados.getNumLinhas() > 0 ) {
			
				codroma = getCodRoma();
				
				sql.append( "insert into vdromaneio ( " );
				sql.append( "codemp, codfilial, codroma, " );
				sql.append( "dataroma, dtsaidaroma, dtprevroma, dtentregaroma, statusroma, " );
				sql.append( "codemptn, codfilialtn, codtran, " );
				sql.append( "codempex, codfilialex, ticket) " );
				
				sql.append( "values ( " );
				sql.append( "?,?,?,");
				sql.append( "?,?,?,?,?,");
				sql.append( "?,?,?,");
				sql.append( "?,?,? )");
				
				ps = con.prepareStatement( sql.toString() );
				
				int arg0 = 1;
				
				ps.setInt( arg0++, Aplicativo.iCodEmp );
				ps.setInt( arg0++, ListaCampos.getMasterFilial( "VDROMANEIO" ) );
				
				ps.setInt( arg0++, codroma );
				
				ps.setDate( arg0++, Funcoes.dateToSQLDate( dataRoma) );
				ps.setDate( arg0++, Funcoes.dateToSQLDate( dataRoma) );
				ps.setDate( arg0++, Funcoes.dateToSQLDate( dataRoma) );
				ps.setDate( arg0++, Funcoes.dateToSQLDate( dataRoma) );
				ps.setString( arg0++, "EX" );
				
				ps.setInt( arg0++, Aplicativo.iCodEmp );
				ps.setInt( arg0++, ListaCampos.getMasterFilial( "VDROMANEIO" ) );
				ps.setInt( arg0++, txtCodTran.getVlrInteger() );
				
				ps.setInt( arg0++, Aplicativo.iCodEmp );
				ps.setInt( arg0++, ListaCampos.getMasterFilial( "EQEXPEDICAO" ) );
				ps.setInt( arg0++, ticket );
				
				ps.execute();
	
				// Inseriu cabeçalho, agora deve inserir os ítens.
				
				sql = new StringBuilder();
				
				sql.append("insert into vditromaneio (codemp, codfilial, codroma, coditroma, codempva, codfilialva, tipovenda, codvenda, dtprevitroma) ");
				sql.append("values (?,?,?,?,?,?,?,?,?)");
				
				for(int i = 0; i < tabSelecionados.getNumLinhas(); i++) {
				
					ps = con.prepareStatement( sql.toString() );
					
					arg0 = 1;
					
					ps.setInt( arg0++, Aplicativo.iCodEmp );
					ps.setInt( arg0++, ListaCampos.getMasterFilial( "VDROMANEIO" ) );
					ps.setInt( arg0++, codroma );
					ps.setInt( arg0++, i + 1 );
					
					ps.setInt( arg0++, Aplicativo.iCodEmp );
					ps.setInt( arg0++, ListaCampos.getMasterFilial( "VDVENDA" ) );
					
					ps.setString( arg0++, (String) tabSelecionados.getValor( i, enum_grid_selecionados.TIPOVENDA.ordinal() ) );
					ps.setInt( arg0++, (Integer) tabSelecionados.getValor( i, enum_grid_selecionados.CODVENDA.ordinal() ) );
					
					ps.setDate( arg0++, Funcoes.dateToSQLDate( dataRoma ) );
					
					ps.execute();
					
				}
				
				Funcoes.mensagemInforma( this, "Romaneio ( " + codroma + " ) gerado com sucesso!" );
				
				tabSelecionados.limpa();
				
			}
			else {
				
				Funcoes.mensagemInforma( this, "Não há nenhuma nota para incluir em romaneio." );
				
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				con.commit();
				ps.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void removeSelecionados() {
		try {
			
			int numlinhas = tabSelecionados.getNumLinhas();
			
			for(int i = 0; i < numlinhas; i++ ) {
				
				if( (Boolean) tabSelecionados.getValor( i, enum_grid_selecionados.SEL.ordinal() )) {
					tabSelecionados.tiraLinha( i );
					  i--;
					  numlinhas --;
				}
				
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void selTudoPesq() {

		for ( int i = 0; i < tabPesquisa.getNumLinhas(); i++ ) {
			if ( podeSel( tabPesquisa.getValor( i, enum_grid_pesquisa.STATUSTXT.ordinal() ).toString() ) ) {
				tabPesquisa.setValor( new Boolean( true ), i, enum_grid_pesquisa.SEL.ordinal() );
			}
		}
	}

	private void selTudoSel() {

		for ( int i = 0; i < tabPesquisa.getNumLinhas(); i++ ) {
			tabSelecionados.setValor( new Boolean( true ), i, enum_grid_pesquisa.SEL.ordinal() );
		}
	}

	private void adicSelecionados() {

		try {

			for ( int row = 0; row < tabPesquisa.getNumLinhas(); row++ ) {

				Boolean selecao = (Boolean) tabPesquisa.getValor( row, enum_grid_pesquisa.SEL.ordinal() );

				if ( selecao ) {

					Integer codvenda = (Integer) tabPesquisa.getValor( row, enum_grid_pesquisa.CODVENDA.ordinal() );

					boolean selecionado = false;

					for ( int i = 0; i < tabSelecionados.getNumLinhas(); i++ ) {

						if ( codvenda.equals( tabSelecionados.getValor( i, enum_grid_selecionados.CODVENDA.ordinal() ) ) ) {
							selecionado = true;
							break; // Interrompe laço pois venda já foi selecionada.
						}

					}

					if ( selecionado ) {
						// continue; //Pula para proxima iteração porque a venda já foi selecionada.
					}
					else {

						tabSelecionados.adicLinha();

						tabSelecionados.setValor( new Boolean( false ), tabSelecionados.getNumLinhas() - 1, enum_grid_selecionados.SEL.ordinal() );

						tabSelecionados.setValor( codvenda, tabSelecionados.getNumLinhas() - 1, enum_grid_selecionados.CODVENDA.ordinal() );

						tabSelecionados.setValor( (String) tabPesquisa.getValor( row, enum_grid_pesquisa.TIPOVENDA.ordinal() ), tabSelecionados.getNumLinhas() - 1, enum_grid_selecionados.TIPOVENDA.ordinal() );
						
						tabSelecionados.setValor( (Integer) tabPesquisa.getValor( row, enum_grid_pesquisa.DOCVENDA.ordinal() ), tabSelecionados.getNumLinhas() - 1, enum_grid_selecionados.DOCVENDA.ordinal() );
						tabSelecionados.setValor( (Integer) tabPesquisa.getValor( row, enum_grid_pesquisa.CODCLI.ordinal() ), tabSelecionados.getNumLinhas() - 1, enum_grid_selecionados.CODCLI.ordinal() );
						tabSelecionados.setValor( (String) tabPesquisa.getValor( row, enum_grid_pesquisa.RAZCLI.ordinal() ), tabSelecionados.getNumLinhas() - 1, enum_grid_selecionados.RAZCLI.ordinal() );
						tabSelecionados.setValor( (Integer) tabPesquisa.getValor( row, enum_grid_pesquisa.CODTRAN.ordinal() ), tabSelecionados.getNumLinhas() - 1, enum_grid_selecionados.CODTRAN.ordinal() );
						tabSelecionados.setValor( (String) tabPesquisa.getValor( row, enum_grid_pesquisa.RAZTRAN.ordinal() ), tabSelecionados.getNumLinhas() - 1, enum_grid_selecionados.RAZTRAN.ordinal() );
						tabSelecionados.setValor( (String) tabPesquisa.getValor( row, enum_grid_pesquisa.PLACA.ordinal() ), tabSelecionados.getNumLinhas() - 1, enum_grid_selecionados.PLACA.ordinal() );
						tabSelecionados.setValor( (BigDecimal) tabPesquisa.getValor( row, enum_grid_pesquisa.VALOR.ordinal() ), tabSelecionados.getNumLinhas() - 1, enum_grid_selecionados.VALOR.ordinal() );
						tabSelecionados.setValor( (BigDecimal) tabPesquisa.getValor( row, enum_grid_pesquisa.QTD.ordinal() ), tabSelecionados.getNumLinhas() - 1, enum_grid_selecionados.QTD.ordinal() );
						tabSelecionados.setValor( (BigDecimal) tabPesquisa.getValor( row, enum_grid_pesquisa.PESOLIQ.ordinal() ), tabSelecionados.getNumLinhas() - 1, enum_grid_selecionados.PESOLIQ.ordinal() );
						tabSelecionados.setValor( (BigDecimal) tabPesquisa.getValor( row, enum_grid_pesquisa.PESOBRUT.ordinal() ), tabSelecionados.getNumLinhas() - 1, enum_grid_selecionados.PESOBRUT.ordinal() );

					}

				}
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	private void selNada( JTablePad tb ) {

		for ( int i = 0; i < tb.getNumLinhas(); i++ ) {
			tb.setValor( new Boolean( false ), i, 0 );
		}
	}

	public void valorAlterado( TabelaSelEvent e ) {

		/*
		 * if ( e.getTabela() == tabOrcamentos && tabOrcamentos.getLinhaSel() > -1 && !carregandoOrcamentos ) { buscaItensVenda( (Integer)tabOrcamentos.getValor( tabOrcamentos.getLinhaSel(), VENDAS.CODVENDA.ordinal() ), "V" ); }
		 */
	}

	private void abreVenda() {

		try {

			FVenda venda = null;

			if ( tabPesquisa.getLinhaSel() > -1 ) {

				if ( Aplicativo.telaPrincipal.temTela( FVenda.class.getName() ) ) {
					venda = (FVenda) Aplicativo.telaPrincipal.getTela( FVenda.class.getName() );
				}
				else {
					venda = new FVenda();
					Aplicativo.telaPrincipal.criatela( "Venda", venda, con );
				}

				int codvenda = (Integer) tabPesquisa.getValor( tabPesquisa.getLinhaSel(), enum_grid_pesquisa.CODVENDA.ordinal() );

				venda.exec( codvenda );

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
			if ( tabEv == tabPesquisa && tabEv.getLinhaSel() > -1 ) {

				abreVenda();

			}
		}
	}

	public void exec( Date data, Integer codtran, Integer codprod, Integer Ticket, FExpedicao pexpedicao ) {

		try {

			if ( codtran != null ) {
				txtCodTran.setVlrInteger( codtran );
				lcTran.carregaDados();
				txtCodTran.setEditable( false );
			}

			if ( data != null ) {
				txtDataini.setVlrDate( data );
				txtDatafim.setVlrDate( data );
				
				dataRoma = data;
				
			}

			ticket = Ticket;

			btAtualiza.doClick();
			
			this.expedicao = pexpedicao;

		} 
		catch ( Exception e ) {
			e.printStackTrace();
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
		else if ( e.getSource() == tabPesquisa ) {
			if ( e.getKeyCode() == KeyEvent.VK_SPACE ) {
				btEditar.doClick();
			}
			else if ( e.getKeyCode() == KeyEvent.VK_ENTER ) {
				btEditar.doClick();
			}
			else if ( e.getKeyCode() == KeyEvent.VK_F5 ) {
				btAtualiza.doClick();
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

		lcCliente.setConexao( con );
		lcProduto.setConexao( con );
		lcTran.setConexao( con );

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

			tela.abreCotacao( codsolicitacao, codfor, renda );
		}

	}
	
	public void dispose() {
		if(expedicao!=null) {
			expedicao.lcCampos.carregaDados();
		}
		super.dispose();
	}

}
