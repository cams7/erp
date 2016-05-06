/**
 * @version 11/02/2002 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FContComis.java <BR>
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

package org.freedom.modulos.std.view.frame.crud.special;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.modulos.fnc.view.dialog.utility.DLNovoPag;
import org.freedom.modulos.gms.view.frame.crud.detail.FConhecFrete;
import org.freedom.modulos.std.view.frame.crud.tabbed.FTransp;

public class FManutConFrete extends FFilho implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinPeriodo = new JPanelPad( 275, 65 );

	private JPanelPad pinLabel = new JPanelPad( 50, 20 );

	private JTextFieldPad txtDataIni = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDataFim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodTran = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtNomeTran = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtValorSelecionados = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 10, Aplicativo.casasDecFin );

	private JTextFieldPad txtTotalPendente = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 10, Aplicativo.casasDecFin );

	private JTextFieldPad txtTotalEmPagamento = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 10, Aplicativo.casasDecFin );

	private JTextFieldPad txtTotalPago = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 10, Aplicativo.casasDecFin );

	private JCheckBoxPad cbPendentes = new JCheckBoxPad( "Pendentes", "S", "N" );

	private JCheckBoxPad cbEmPagamento = new JCheckBoxPad( "Em pagamento", "S", "N" );

	private JCheckBoxPad cbPagos = new JCheckBoxPad( "Pagas", "S", "N" );

	private JButtonPad btPesquisar = new JButtonPad( "Pesquisar", Icone.novo( "btPesquisa.png" ) );

	private JButtonPad btSelecionarTodos = new JButtonPad( Icone.novo( "btTudo.png" ) );

	private JButtonPad btSelecionarNenhum = new JButtonPad( Icone.novo( "btNada.png" ) );

	private JButtonPad btGerarPagamentos = new JButtonPad( Icone.novo( "btGerar.png" ) );

	private JButtonPad btExcluirPagamentos = new JButtonPad( Icone.novo( "btCancelar.png" ) );

	private JButtonPad btSair = new JButtonPad( "Sair", Icone.novo( "btSair.png" ) );

	private ImageIcon imgPendente = Icone.novo( "clVencido.gif" );

	private ImageIcon imgEmPagamento = Icone.novo( "clPagoParcial.gif" );

	private ImageIcon imgPago = Icone.novo( "clPago.gif" );

	private ImageIcon imgColuna = null;

	private JTablePad tab = new JTablePad();

	private JScrollPane spnTab = new JScrollPane( tab );

	private ListaCampos lcTransp = new ListaCampos( this );

	private BigDecimal valorSelecionados = new BigDecimal( "0.00" );

	private BigDecimal valorPendente = new BigDecimal( "0.00" );

	private BigDecimal valorEmPagamento = new BigDecimal( "0.00" );

	private BigDecimal valorPago = new BigDecimal( "0.00" );

	private HandlerSelecaoGrid handlerSelecaoGrid = null;

	private enum EManutencaoFrete {
		SEL, STATUS, CODIGO, DOCUMENTO, DOC_VENDAS, EMISSAO, PAGAMENTO, CODTRANS, RAZTRANS, CODREMT, DESCREMT, CODDEST, DESCDEST, VLRFRETE, VLRMERCADORIAS, VOLUMES;
	}

	public FManutConFrete() {

		super( false );

		setTitulo( "Pagamento de Frete" );
		setAtribos( 50, 50, 740, 430 );

		lcTransp.add( new GuardaCampo( txtCodTran, "CodTran", "Cód.Transp.", ListaCampos.DB_PK, txtNomeTran, true ) );
		lcTransp.add( new GuardaCampo( txtNomeTran, "NomeTran", "Nome do transportador", ListaCampos.DB_SI, false ) );
		lcTransp.add( new GuardaCampo( txtCodFor, "CodFor", "Código do fornecedor vinculado", ListaCampos.DB_SI, false ) );
		lcTransp.montaSql( false, "TRANSP", "VD" );
		lcTransp.setReadOnly( true );
		txtCodTran.setPK( true );
		txtCodTran.setTabelaExterna( lcTransp, FTransp.class.getCanonicalName() );
		txtCodTran.setListaCampos( lcTransp );
		txtCodTran.setNomeCampo( "CodTran" );

		montaTela();

		btPesquisar.addActionListener( this );
		btSelecionarTodos.addActionListener( this );
		btSelecionarNenhum.addActionListener( this );
		btGerarPagamentos.addActionListener( this );
		btExcluirPagamentos.addActionListener( this );
		btSair.addActionListener( this );
		tab.addMouseListener( this );

		Calendar cPeriodo = Calendar.getInstance();
		txtDataFim.setVlrDate( cPeriodo.getTime() );
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataIni.setVlrDate( cPeriodo.getTime() );

		cbPendentes.setVlrString( "S" );
		cbEmPagamento.setVlrString( "N" );
		cbPagos.setVlrString( "N" );
		
		
		
	}

	private void montaTela() {

		btPesquisar.setToolTipText( "Pesquisar conhecimentos de fretes" );
		btSelecionarTodos.setToolTipText( "Selecionar todos da lista" );
		btSelecionarNenhum.setToolTipText( "Desfaser seleção de todos da lista" );
		btGerarPagamentos.setToolTipText( "Criar pagamentos" );
		btExcluirPagamentos.setToolTipText( "Desfazer registro de pagamento" );

		Container c = getContentPane();
		c.setLayout( new BorderLayout() );

		JPanelPad pinTop = new JPanelPad( 600, 120 );

		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );
		JLabelPad lbPeriodo = new JLabelPad( "Periodo:", SwingConstants.CENTER );
		lbPeriodo.setOpaque( true );

		pinTop.adic( lbPeriodo, 17, 10, 80, 20 );
		pinTop.adic( lbLinha, 7, 20, 250, 40 );
		pinTop.adic( txtDataIni, 17, 30, 100, 20 );
		pinTop.adic( new JLabel( "à", SwingConstants.CENTER ), 117, 30, 30, 20 );
		pinTop.adic( txtDataFim, 147, 30, 100, 20 );

		pinTop.adic( new JLabelPad( "Cód.trans." ), 270, 10, 80, 20 );
		pinTop.adic( txtCodTran, 270, 30, 90, 20 );
		pinTop.adic( new JLabelPad( "Nome do transportador" ), 363, 10, 300, 20 );
		pinTop.adic( txtNomeTran, 363, 30, 337, 20 );

		JLabelPad lbLinha2 = new JLabelPad();
		lbLinha2.setBorder( BorderFactory.createEtchedBorder() );
		pinTop.adic( lbLinha2, 7, 70, 353, 30 );
		pinTop.adic( cbPendentes, 25, 75, 90, 20 );
		pinTop.adic( cbEmPagamento, 130, 75, 120, 20 );
		pinTop.adic( cbPagos, 270, 75, 65, 20 );

		pinTop.adic( btPesquisar, 580, 70, 120, 30 );

		JPanelPad pinCentro = new JPanelPad( new BorderLayout() );
		JPanelPad pinCentroBotoes = new JPanelPad( 45, 200 );

		pinCentroBotoes.adic( btSelecionarTodos, 5, 5, 30, 30 );
		pinCentroBotoes.adic( btSelecionarNenhum, 5, 40, 30, 30 );

		pinCentro.add( spnTab, BorderLayout.CENTER );
		pinCentro.add( pinCentroBotoes, BorderLayout.EAST );

		JPanelPad pinRod = new JPanelPad( new BorderLayout() );
		JPanelPad pinRodBotoes = new JPanelPad( 90, 50 );
		JPanelPad pinRodStatus = new JPanelPad( 100, 50 );
		JPanelPad pinRodSair = new JPanelPad( new FlowLayout( FlowLayout.RIGHT, 10, 10 ) );

		pinRodBotoes.tiraBorda();
		pinRodBotoes.adic( btGerarPagamentos, 10, 10, 30, 30 );
		pinRodBotoes.adic( btExcluirPagamentos, 50, 10, 30, 30 );

		pinRodStatus.tiraBorda();
		pinRodStatus.adic( new JLabelPad( "Selecionados" ), 7, 0, 80, 20 );
		pinRodStatus.adic( txtValorSelecionados, 7, 20, 80, 20 );
		pinRodStatus.adic( new JLabelPad( "Pendente" ), 90, 0, 80, 20 );
		pinRodStatus.adic( txtTotalPendente, 90, 20, 80, 20 );
		pinRodStatus.adic( new JLabelPad( "Em pagamento" ), 173, 0, 90, 20 );
		pinRodStatus.adic( txtTotalEmPagamento, 173, 20, 90, 20 );
		pinRodStatus.adic( new JLabelPad( "Pago" ), 266, 0, 80, 20 );
		pinRodStatus.adic( txtTotalPago, 266, 20, 80, 20 );

		pinRodStatus.adic( new JLabelPad( imgPendente ), 360, 0, 20, 20 );
		JLabelPad pendentes = new JLabelPad( "pendentes" );
		pendentes.setForeground( new Color( 111, 106, 177 ) );
		pendentes.setFont( new Font( "Tomoha", Font.PLAIN, 11 ) );
		pinRodStatus.adic( pendentes, 380, 0, 200, 20 );

		pinRodStatus.adic( new JLabelPad( imgEmPagamento ), 360, 15, 20, 20 );
		JLabelPad emPagamento = new JLabelPad( "em processo de pagamento" );
		emPagamento.setForeground( new Color( 111, 106, 177 ) );
		emPagamento.setFont( new Font( "Tomoha", Font.PLAIN, 11 ) );
		pinRodStatus.adic( emPagamento, 380, 15, 200, 20 );

		pinRodStatus.adic( new JLabelPad( imgPago ), 360, 30, 20, 20 );
		JLabelPad pago = new JLabelPad( "pago" );
		pago.setForeground( new Color( 111, 106, 177 ) );
		pago.setFont( new Font( "Tomoha", Font.PLAIN, 11 ) );
		pinRodStatus.adic( pago, 380, 30, 200, 20 );

		txtValorSelecionados.setAtivo( false );
		txtTotalPendente.setAtivo( false );
		txtTotalEmPagamento.setAtivo( false );
		txtTotalPago.setAtivo( false );

		pinRodSair.add( btSair );

		pinRod.add( pinRodBotoes, BorderLayout.WEST );
		pinRod.add( pinRodStatus, BorderLayout.CENTER );
		pinRod.add( pinRodSair, BorderLayout.EAST );

		c.add( pinTop, BorderLayout.NORTH );
		c.add( pinCentro, BorderLayout.CENTER );
		c.add( pinRod, BorderLayout.SOUTH );

		tab.adicColuna( "" ); // 0
		tab.adicColuna( "" ); // 1
		tab.adicColuna( "Código" ); // 2
		tab.adicColuna( "Documento" ); // 3
		tab.adicColuna( "Vendas" ); // 4
		tab.adicColuna( "Emissão" ); // 5
		tab.adicColuna( "Pagamento" ); // 6
		tab.adicColuna( "Cód.transp." ); // 7
		tab.adicColuna( "Razão social da transportadora" ); // 8
		tab.adicColuna( "Cód.dest." ); // 9
		tab.adicColuna( "Descrição do remetente" ); // 10
		tab.adicColuna( "Cód.dest." ); // 11
		tab.adicColuna( "Descrição do destinatário" ); // 12
		tab.adicColuna( "Vlr. Frete" ); // 13
		tab.adicColuna( "Vlr. Mercadorias" ); // 14
		tab.adicColuna( "Volumes" ); // 15

		tab.setTamColuna( 20, EManutencaoFrete.SEL.ordinal() );
		tab.setTamColuna( 20, EManutencaoFrete.STATUS.ordinal() );
		tab.setTamColuna( 70, EManutencaoFrete.CODIGO.ordinal() );
		tab.setTamColuna( 70, EManutencaoFrete.DOCUMENTO.ordinal() );
		tab.setTamColuna( 100, EManutencaoFrete.DOC_VENDAS.ordinal() );
		tab.setTamColuna( 70, EManutencaoFrete.EMISSAO.ordinal() );
		tab.setTamColuna( 70, EManutencaoFrete.PAGAMENTO.ordinal() );
		tab.setTamColuna( 70, EManutencaoFrete.CODTRANS.ordinal() );
		tab.setTamColuna( 170, EManutencaoFrete.RAZTRANS.ordinal() );
		tab.setTamColuna( 70, EManutencaoFrete.CODREMT.ordinal() );
		tab.setTamColuna( 170, EManutencaoFrete.DESCREMT.ordinal() );
		tab.setTamColuna( 70, EManutencaoFrete.CODDEST.ordinal() );
		tab.setTamColuna( 170, EManutencaoFrete.DESCDEST.ordinal() );
		tab.setTamColuna( 100, EManutencaoFrete.VLRFRETE.ordinal() );
		tab.setTamColuna( 100, EManutencaoFrete.VLRMERCADORIAS.ordinal() );
		tab.setTamColuna( 80, EManutencaoFrete.VOLUMES.ordinal() );
		tab.setColunaEditavel( 0, true );
	}

	private void montarGrid( boolean avisaSobreNaoTer ) {

		tab.limpa();
		valorSelecionados = new BigDecimal( "0.00" );
		valorPendente = new BigDecimal( "0.00" );
		valorEmPagamento = new BigDecimal( "0.00" );
		valorPago = new BigDecimal( "0.00" );

		txtValorSelecionados.setVlrBigDecimal( valorSelecionados );
		txtTotalPendente.setVlrBigDecimal( valorPendente );
		txtTotalEmPagamento.setVlrBigDecimal( valorEmPagamento );
		txtTotalPago.setVlrBigDecimal( valorPago );

		if ( txtCodFor.getVlrInteger() == 0 ) {
			Funcoes.mensagemInforma( this, "Selecione um transportador com fornecedor vinculado!" );
			txtCodTran.requestFocus();
			return;
		}
		if ( "N".equals( cbPendentes.getVlrString() ) && "N".equals( cbEmPagamento.getVlrString() ) && "N".equals( cbPagos.getVlrString() ) ) {
			Funcoes.mensagemInforma( this, "Selecione as opções de filtro!" );
			return;
		}

		StringBuilder sql = new StringBuilder();
		sql.append( "select" );
		sql.append( " cf.codfrete, cf.docfrete, cf.dtemitfrete, cf.codtran, t.raztran," );
		sql.append( " cf.codremet, rm.descunifcod as descremet," );
		sql.append( " cf.coddestinat, ds.descunifcod as descdestinat," );
		sql.append( " cf.vlrfrete, cf.vlrmercadoria, cf.qtdfrete, cf.codpag," );
		sql.append( " (select p.vlrpagopag from FNPAGAR p" );
		sql.append( "  where p.codemp=cf.codemppa and p.codfilial=cf.codfilialpa and p.codpag=cf.codpag and" );
		sql.append( "  (select sum(ip1.nparcpag) from fnitpagar ip1" );
		sql.append( "   where ip1.codemp=p.codemp and ip1.codfilial=p.codfilial and ip1.codpag=p.codpag) =" );
		sql.append( "  (select sum(ip1.nparcpag) from fnitpagar ip1" );
		sql.append( "   where ip1.codemp=p.codemp and ip1.codfilial=p.codfilial and ip1.codpag=p.codpag and" );
		sql.append( "   ip1.statusitpag='PP')) as pago," );
		sql.append( " (select p.vlrpagopag from FNPAGAR p" );
		sql.append( "  where p.codemp=cf.codemppa and p.codfilial=cf.codfilialpa and p.codpag=cf.codpag) as emPagamento" );
		sql.append( " from LFFRETE cf, VDTRANSP t, SGUNIFCOD rm, SGUNIFCOD ds" );
		sql.append( " where cf.codemp=? and cf.codfilial=? and cf.dtemitfrete between ? and ? and" );
		sql.append( " t.codfor=(select f.codfor from CPFORNECED f where f.codemp=t.codempfr and f.codfilial=t.codfilialfr and f.codfor=t.codfor and f.codfor=?) and" );
		sql.append( " t.codemp=cf.codemptm and t.codfilial=cf.codfilialtm and t.codtran=cf.codtran and" );
		sql.append( " rm.codemp=cf.codempre and rm.codfilial=cf.codfilialre and rm.codunifcod=cf.codremet and" );
		sql.append( " ds.codemp=cf.codempde and ds.codfilial=cf.codfilialde and ds.codunifcod=cf.coddestinat and" );

		StringBuilder where = new StringBuilder();
		if ( "S".equals( cbPendentes.getVlrString() ) ) {
			where.append( " cf.codpag is null" );
		}
		if ( "S".equals( cbPagos.getVlrString() ) ) {
			if ( where.length() > 0 ) {
				where.append( " or" );
			}
			where.append( " (select p.codpag from FNPAGAR p" );
			where.append( "  where p.codemp=cf.codemppa and p.codfilial=cf.codfilialpa and p.codpag=cf.codpag and" );
			where.append( "  (select sum(ip1.nparcpag) from fnitpagar ip1" );
			where.append( "   where ip1.codemp=p.codemp and ip1.codfilial=p.codfilial and ip1.codpag=p.codpag) =" );
			where.append( "  (select sum(ip1.nparcpag) from fnitpagar ip1" );
			where.append( "   where ip1.codemp=p.codemp and ip1.codfilial=p.codfilial and ip1.codpag=p.codpag and" );
			where.append( "   ip1.statusitpag='PP')) is not null " );
		}
		if ( "S".equals( cbEmPagamento.getVlrString() ) ) {
			if ( where.length() > 0 ) {
				where.append( " or" );
			}
			where.append( " (cf.codpag is not null and" );
			where.append( " (select p.codpag from FNPAGAR p" );
			where.append( "  where p.codemp=cf.codemppa and p.codfilial=cf.codfilialpa and p.codpag=cf.codpag and" );
			where.append( "  (select sum(ip1.nparcpag) from fnitpagar ip1" );
			where.append( "   where ip1.codemp=p.codemp and ip1.codfilial=p.codfilial and ip1.codpag=p.codpag) =" );
			where.append( "  (select sum(ip1.nparcpag) from fnitpagar ip1" );
			where.append( "   where ip1.codemp=p.codemp and ip1.codfilial=p.codfilial and ip1.codpag=p.codpag and" );
			where.append( "   ip1.statusitpag='PP')) is null) " );
		}

		sql.append( where.length() > 0 ? ( "(" + where.toString() + ")" ) : "" );
		sql.append( " order by cf.dtemitfrete, cf.codfrete" );

		try {

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "LFFRETE" ) );
			ps.setDate( 3, Funcoes.dateToSQLDate( txtDataIni.getVlrDate() ) );
			ps.setDate( 4, Funcoes.dateToSQLDate( txtDataFim.getVlrDate() ) );
			ps.setInt( 5, txtCodFor.getVlrInteger() );

			ResultSet rs = ps.executeQuery();

			int row = 0;
			for ( ; rs.next(); row++ ) {

				tab.adicLinha();

				if ( rs.getBigDecimal( "pago" ) != null ) {
					imgColuna = imgPago;
					valorPago = valorPago.add( rs.getBigDecimal( "pago" ) );
				}
				else if ( rs.getBigDecimal( "emPagamento" ) != null ) {
					imgColuna = imgEmPagamento;
					valorEmPagamento = valorEmPagamento.add( rs.getBigDecimal( "vlrfrete" ) );
				}
				else {
					imgColuna = imgPendente;
					valorPendente = valorPendente.add( rs.getBigDecimal( "vlrfrete" ) );
				}

				tab.setValor( new Boolean( false ), row, EManutencaoFrete.SEL.ordinal() );
				tab.setValor( imgColuna, row, EManutencaoFrete.STATUS.ordinal() );
				tab.setValor( rs.getInt( "codfrete" ), row, EManutencaoFrete.CODIGO.ordinal() );
				tab.setValor( rs.getString( "docfrete" ), row, EManutencaoFrete.DOCUMENTO.ordinal() );
				tab.setValor( Funcoes.dateToSQLDate( rs.getDate( "dtemitfrete" ) ), row, EManutencaoFrete.EMISSAO.ordinal() );
				tab.setValor( rs.getInt( "codpag" ), row, EManutencaoFrete.PAGAMENTO.ordinal() );
				tab.setValor( rs.getInt( "codtran" ), row, EManutencaoFrete.CODTRANS.ordinal() );
				tab.setValor( rs.getString( "raztran" ), row, EManutencaoFrete.RAZTRANS.ordinal() );
				tab.setValor( rs.getInt( "codremet" ), row, EManutencaoFrete.CODREMT.ordinal() );
				tab.setValor( rs.getString( "descremet" ), row, EManutencaoFrete.DESCREMT.ordinal() );
				tab.setValor( rs.getInt( "coddestinat" ), row, EManutencaoFrete.CODDEST.ordinal() );
				tab.setValor( rs.getString( "descdestinat" ), row, EManutencaoFrete.DESCDEST.ordinal() );
				tab.setValor( rs.getBigDecimal( "vlrfrete" ).setScale( Aplicativo.casasDecFin, BigDecimal.ROUND_HALF_UP ), row, EManutencaoFrete.VLRFRETE.ordinal() );
				tab.setValor( rs.getBigDecimal( "vlrmercadoria" ).setScale( Aplicativo.casasDecFin, BigDecimal.ROUND_HALF_UP ), row, EManutencaoFrete.VLRMERCADORIAS.ordinal() );
				tab.setValor( rs.getBigDecimal( "qtdfrete" ).setScale( Aplicativo.casasDec, BigDecimal.ROUND_HALF_UP ), row, EManutencaoFrete.VOLUMES.ordinal() );

				if ( handlerSelecaoGrid == null ) {
					tab.getCellEditor( row, 0 ).addCellEditorListener( handlerSelecaoGrid = new HandlerSelecaoGrid() );
				}
			}

			if ( row == 0 && avisaSobreNaoTer ) {
				Funcoes.mensagemInforma( this, "Nenhum conhecimento de frete encontrado." );
			}

			txtTotalPendente.setVlrBigDecimal( valorPendente );
			txtTotalEmPagamento.setVlrBigDecimal( valorEmPagamento );
			txtTotalPago.setVlrBigDecimal( valorPago );

			rs.close();
			ps.close();

			con.commit();

			carregaDocumentosVendas();

		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao carregar conhecimentos de frete", true, con, e );
		}
	}

	private void carregaDocumentosVendas() {

		StringBuilder sql = new StringBuilder();
		sql.append( "select v.docvenda from vdvenda v, lffretevenda fv, lffrete cf " );
		sql.append( "where v.codemp=fv.codemp and v.codfilial=fv.codfilial and v.tipovenda=fv.tipovenda and v.codvenda=fv.codvenda and " );
		sql.append( "cf.codemp=fv.codemp and cf.codfilial=fv.codfilial and cf.codfrete=fv.codfrete and " );
		sql.append( "cf.codemp=? and cf.codfilial=? and cf.codfrete=? " );
		sql.append( "order by fv.codvenda" );

		try {

			for ( int row = 0; row < tab.getNumLinhas(); row++ ) {

				PreparedStatement ps = con.prepareStatement( sql.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "LFFTRE" ) );
				ps.setInt( 3, (Integer) tab.getValor( row, EManutencaoFrete.CODIGO.ordinal() ) );

				ResultSet rs = ps.executeQuery();
				StringBuilder vendas = new StringBuilder();

				while ( rs.next() ) {
					if ( vendas.length() > 0 ) {
						vendas.append( "," );
					}
					vendas.append( rs.getString( "docvenda" ) );
				}

				tab.setValor( vendas, row, EManutencaoFrete.DOC_VENDAS.ordinal() );

				rs.close();
				ps.close();

				con.commit();
			}

		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao carregar conhecimentos de frete", true, con, e );
		}
	}

	private void selecionaTodos() {

		for ( int row = 0; row < tab.getNumLinhas(); row++ ) {
			tab.setValor( new Boolean( true ), row, 0 );
		}
		calcValorSelecionado();
	}

	private void selecionaNenhum() {

		for ( int row = 0; row < tab.getNumLinhas(); row++ ) {
			tab.setValor( new Boolean( false ), row, 0 );
		}
		calcValorSelecionado();
	}

	private void calcValorSelecionado() {

		valorSelecionados = new BigDecimal( "0.00" );
		for ( int row = 0; row < tab.getNumLinhas(); row++ ) {
			if ( (Boolean) tab.getValor( row, 0 ) ) {
				valorSelecionados = valorSelecionados.add( (BigDecimal) tab.getValor( row, EManutencaoFrete.VLRFRETE.ordinal() ) );
			}
		}
		txtValorSelecionados.setVlrBigDecimal( valorSelecionados );
	}

	private void gerarPagamentos() {

		if ( valorSelecionados.doubleValue() > 0.00 ) {

			DLNovoPag dl = new DLNovoPag( this );
			dl.setConexao( con );

			Object[] values = new Object[ 8 ];

			values[ 0 ] = txtCodFor.getVlrInteger();
			values[ 1 ] = null; // plano de pagamento
			values[ 2 ] = null; // banco
			values[ 3 ] = null; // tipo de cobrança
			values[ 4 ] = txtValorSelecionados.getVlrBigDecimal();
			values[ 5 ] = new Date();
			values[ 6 ] = null; // documento
			values[ 7 ] = "PGTO.FRETE";

			dl.setValues( values );
			dl.setVisible( true );

			if ( dl.OK ) {

				int codigoPagamento = dl.getCodigoPagamento();

				if ( codigoPagamento > 0 ) {

					StringBuilder conhecimentos = new StringBuilder();

					for ( int row = 0; row < tab.getNumLinhas(); row++ ) {
						if ( (Boolean) tab.getValor( row, 0 ) ) {
							if ( conhecimentos.length() > 0 ) {
								conhecimentos.append( "," );
							}
							conhecimentos.append( (Integer) tab.getValor( row, EManutencaoFrete.CODIGO.ordinal() ) );
						}
					}

					StringBuilder sql = new StringBuilder();
					sql.append( "update LFFRETE set codemppa=?, codfilialpa=?, codpag=? " );
					sql.append( "where codemp=? and codfilial=? and " );
					sql.append( "codfrete in (" + conhecimentos.toString() + ")" );

					try {

						PreparedStatement ps = con.prepareStatement( sql.toString() );
						ps.setInt( 1, Aplicativo.iCodEmp );
						ps.setInt( 2, ListaCampos.getMasterFilial( "FNPAGAR" ) );
						ps.setInt( 3, codigoPagamento );
						ps.setInt( 4, Aplicativo.iCodEmp );
						ps.setInt( 5, ListaCampos.getMasterFilial( "LFFRETE" ) );

						ps.executeUpdate();
						ps.close();

						con.commit();

						Funcoes.mensagemInforma( this, "Registro de pagamento criado para os conhecimentos de fretes selecionados." );

					} catch ( SQLException e ) {
						e.printStackTrace();
						Funcoes.mensagemErro( this, "Erro ao atualizar conhecimentos de frete", true, con, e );
					}

					montarGrid( false );
				}
			}

			dl.dispose();
		}
		else {
			Funcoes.mensagemInforma( this, "Selecione na listagem, os conhecimentos de fretes desejados." );
		}
	}

	private void excluirPagamentos() {

		Set<Integer> pagamentosSet = new HashSet<Integer>();
		;

		for ( int row = 0; row < tab.getNumLinhas(); row++ ) {
			if ( (Boolean) tab.getValor( row, 0 ) && !imgPago.equals( tab.getValor( row, 1 ) ) && ( (Integer) tab.getValor( row, EManutencaoFrete.PAGAMENTO.ordinal() ) ) > 0 ) {
				pagamentosSet.add( (Integer) tab.getValor( row, EManutencaoFrete.PAGAMENTO.ordinal() ) );
			}
		}

		if ( pagamentosSet.size() == 0 ) {
			Funcoes.mensagemInforma( this, "Selecione os conhecimento de frete para desfazer registro de pagamento." );
			return;
		}

		Funcoes.mensagemInforma( this, "ATENÇÃO!\n" + "Os pagamentos com parcelas pagas não poderam ser desfeitos \n" + "antes do estorno destes pagamentos." );

		StringBuilder msg = new StringBuilder();
		StringBuilder pagamentos = new StringBuilder();
		Object[] tmp = pagamentosSet.toArray();

		for ( int i = 0; i < tmp.length; i++ ) {
			if ( i > 0 ) {
				msg.append( "," );
				pagamentos.append( "," );
			}
			if ( ( i % 10 ) == 0 ) {
				msg.append( "\n" );
			}
			msg.append( String.valueOf( tmp[ i ] ) );
			pagamentos.append( String.valueOf( tmp[ i ] ) );
		}

		Funcoes.mensagemInforma( this, "Seram excluidos os pagamentos:" + msg.toString() + "\n" + "e todos os conhecimentos vinculados a estes ficaram pendentes." );

		StringBuilder update = new StringBuilder();
		update.append( "update LFFRETE cf set cf.codemppa=null, cf.codfilialpa=null, cf.codpag=null " );
		update.append( "where cf.codemp=? and cf.codfilial=? and " );
		update.append( "cf.codpag in (" + pagamentos.toString() + ") and " );
		update.append( "not exists (select p.statusitpag from FNITPAGAR p " );
		update.append( "		    where p.codemp=cf.codemppa and p.codfilial=cf.codfilialpa and p.codpag=cf.codpag and p.statusitpag<>'P1' ) " );

		StringBuilder delete = new StringBuilder();
		delete.append( "delete from FNPAGAR p " );
		delete.append( "where p.codemp=? and p.codfilial=? and " );
		delete.append( "p.codpag in (" + pagamentos.toString() + ") and " );
		delete.append( "not exists (select i.statusitpag from FNITPAGAR i " );
		delete.append( "		     where i.codemp=p.codemp and i.codfilial=p.codfilial and i.codpag=p.codpag and i.statusitpag<>'P1' ) " );

		try {

			PreparedStatement ps = con.prepareStatement( update.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNPAGAR" ) );

			ps.executeUpdate();

			ps = con.prepareStatement( delete.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNPAGAR" ) );

			ps.executeUpdate();
			ps.close();

			con.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao atualizar excluir registros de pagamento.\n" + e.getMessage(), true, con, e );
		}

		montarGrid( false );

	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPesquisar ) {
			montarGrid( true );
		}
		else if ( evt.getSource() == btSelecionarTodos ) {
			selecionaTodos();
		}
		else if ( evt.getSource() == btSelecionarNenhum ) {
			selecionaNenhum();
		}
		else if ( evt.getSource() == btGerarPagamentos ) {
			gerarPagamentos();
		}
		else if ( evt.getSource() == btExcluirPagamentos ) {
			excluirPagamentos();
		}
		else if ( evt.getSource() == btSair ) {
			dispose();
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcTransp.setConexao( cn );

	}

	private class HandlerSelecaoGrid implements CellEditorListener {

		public void editingCanceled( ChangeEvent e ) {

		}

		public void editingStopped( ChangeEvent e ) {

			calcValorSelecionado();
		}
	}
	
	public void mouseClicked( MouseEvent e ) {

		if ( e.getClickCount() == 2 ) {
			if ( e.getSource() == tab && tab.getLinhaSel() > -1 ) {
				FConhecFrete frete = null;
				if ( Aplicativo.telaPrincipal.temTela( FConhecFrete.class.getName() ) ) {
					frete = (FConhecFrete) Aplicativo.telaPrincipal.getTela( FConhecFrete.class.getName() );
				}
				else {
					frete = new FConhecFrete();
					Aplicativo.telaPrincipal.criatela( "Conhecimento de frete", frete, con );
				}
				frete.exec( (Integer) tab.getValor( tab.getLinhaSel(), 2 ) );
			}
		}
	}

	public void mouseEntered( MouseEvent arg0 ) {

		// TODO Auto-generated method stub
		
	}

	public void mouseExited( MouseEvent arg0 ) {

		// TODO Auto-generated method stub
		
	}

	public void mousePressed( MouseEvent arg0 ) {

		// TODO Auto-generated method stub
		
	}

	public void mouseReleased( MouseEvent arg0 ) {

		// TODO Auto-generated method stub
		
	}
	
}
