/**
 * @version 02/08/2003 <BR>
 * @author Setpoint Informática Ltda./Alex Rodrigues <BR>
 *         Projeto: Freedom <BR>
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FConsSolItem.java <BR>
 *                       Este programa é licenciado de acordo com a LPG-PC (Licença Pública Geral para Programas de Computador), <BR>
 *                       modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                       A LPG-PC deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste Programa. <BR>
 *                       Caso uma cópia da LPG-PC não esteja disponível junto com este Programa, você pode contatar <BR>
 *                       sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                       Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                       Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa é preciso estar <BR>
 *                       de acordo com os termos da LPG-PC <BR>
 * <BR>
 *                       Formulário de consulta de RMA.
 */

package org.freedom.modulos.gms.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.modulos.gms.business.object.GestaoSol.GRID_SOL;
import org.freedom.modulos.gms.business.object.GestaoSol.SelectedSol;
import org.freedom.modulos.gms.dao.DAOGestaoSol;

public class FGestaoSol extends FFilho implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JTabbedPanePad tabGestao = new JTabbedPanePad();

	private JPanelPad pinCab = new JPanelPad( 0, 130);
	
	private JPanelPad pinFiltros = new JPanelPad( 0, 130);
		
	private JPanelPad pinBarraFerramentas = new JPanelPad( 40, 0 );
	
	private JPanelPad pinNavegacao = new JPanelPad( 40, 0 );
	
	private JPanelPad pnCli = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );
	
	private JPanelPad pnDetalhe = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnRod = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnLegenda = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 0, 5 ) );

	private JTextFieldPad txtDtIni = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDtFim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodUsu = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldFK txtNomeUsu = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodCC = new JTextFieldPad( JTextFieldPad.TP_STRING, 19, 0 );

	private JTextFieldPad txtAnoCC = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );

	private JTextFieldFK txtDescCC = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodAlmoxarife = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtDescAlmoxarife = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtRefProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTablePad tab = new JTablePad();

	private ImageIcon imgCancelada = Icone.novo( "clVencido.gif" );

	private ImageIcon imgExpedida = Icone.novo( "clPago.gif" );

	private ImageIcon imgAprovada = Icone.novo( "clPagoParcial.gif" );

	private ImageIcon imgPendente = Icone.novo( "clNaoVencido.gif" );

	private ImageIcon imgColuna = null;
	
	private JButtonPad btTudo = new JButtonPad( Icone.novo( "btTudo.png" ) );
	
	private JButtonPad btNada = new JButtonPad( Icone.novo( "btNada.png" ) );

	private JButtonPad btGerarSol = new JButtonPad( Icone.novo( "btGerar.png" ) );

	private JButtonPad btBusca = new JButtonPad( "Buscar", Icone.novo( "btPesquisa.png" ) );

	private JButtonPad btSair = new JButtonPad( "Sair", Icone.novo( "btSair.png" ) );
	
	private JButtonPad btAprova = new JButtonPad( Icone.novo( "btOk.png" ) );

	private JScrollPane spnTab = new JScrollPane( tab );

	private ListaCampos lcAlmox = new ListaCampos( this, "AM" );

	private ListaCampos lcUsuario = new ListaCampos( this, "" );

	private ListaCampos lcCC = new ListaCampos( this, "CC" );

	private ListaCampos lcProd = new ListaCampos( this, "PD" );
	
	private Vector<String> lSituacaoItem = new Vector<String>();
	
	private Vector<Object> vSituacaoItem = new Vector<Object>();
	
	private Vector<String> lSituacaoCompra = new Vector<String>();
	
	private Vector<Object> vSituacaoCompra = new Vector<Object>();

	private Vector<String> lSituacaoSol = new Vector<String>();
	
	private Vector<Object> vSituacaoSol = new Vector<Object>();
	
	private JComboBoxPad cbSituacaoItem = new JComboBoxPad( lSituacaoItem, vSituacaoItem, JComboBoxPad.TP_STRING, 2, 0 );
	
	private JComboBoxPad cbSituacaoCompra = new JComboBoxPad( lSituacaoCompra, vSituacaoCompra, JComboBoxPad.TP_STRING, 2, 0 );
	
	private JComboBoxPad cbSituacaoSol = new JComboBoxPad( lSituacaoSol, vSituacaoSol, JComboBoxPad.TP_STRING, 2, 0 );
	
	Map<String, Object> params = null;

	boolean bAprovaParcial = false;

	boolean bExpede = false;

	String aprovasol = null;

	private Vector<?> vSitSol = new Vector<Object>();
	
	private DAOGestaoSol daocons = null;

	public FGestaoSol() {

		super( false );
		setTitulo( "Gestão de Solicitações de Compra" );
		setAtribos( 10, 10, 795, 480 );
		
		montaComboBox();
		montaListaCampos();
		montaListener();
		montaTela();
		
		txtDtIni.setRequerido( true );
		txtDtFim.setRequerido( true );

	}
	
	private void montaGrid(){
		
		tab.adicColuna( "" );// IMGCOLUNA
		tab.adicColuna( "Sit." );// STATUS DA SOLICITAÇÃO
		tab.adicColuna( "Sel." );// SEL
		tab.adicColuna( "Cód.prod." );// CODPROD
		tab.adicColuna( "Ref.prod" );// REFPROD
		tab.adicColuna( "Descrição do produto" );// DESCPROD
		tab.adicColuna( "Qt. requerida" );// QTDITSOL
		tab.adicColuna( "Qt. aprovada" );// QTDAPROVITSOL
		tab.adicColuna( "Cód.sol." ); // CODSOL
		tab.adicColuna( "Item sol." ); // Iten da solicitação
		tab.adicColuna( "Saldo" );// SLDPROD
		tab.adicColuna( "Cód.c.c" );// Centro de custo
		tab.adicColuna( "Usu.Sol" ); // Usuário que solicitou
		tab.adicColuna( "Usu.Aprov" ); // Usuário que aprovou


		tab.setTamColuna( 12, GRID_SOL.IMGCOLUNA.ordinal() );
		tab.setTamColuna( 35, GRID_SOL.SITITSOL.ordinal() );
		tab.setTamColuna( 35, GRID_SOL.SEL.ordinal() );
		tab.setTamColuna( 70, GRID_SOL.CODPROD.ordinal() );
		tab.setTamColuna( 70, GRID_SOL.REFPROD.ordinal() );
		tab.setTamColuna( 280, GRID_SOL.DESCPROD.ordinal() );
		tab.setTamColuna( 90, GRID_SOL.QTDITSOL.ordinal() );
		tab.setTamColuna( 90, GRID_SOL.QTDAPROVITSOL.ordinal() );
		tab.setTamColuna( 70, GRID_SOL.CODSOL.ordinal() );
		tab.setTamColuna( 70, GRID_SOL.CODITSOL.ordinal() );
		tab.setTamColuna( 80, GRID_SOL.SLDPROD.ordinal() );
		tab.setTamColuna( 80, GRID_SOL.CODCC.ordinal() );
		tab.setTamColuna( 80, GRID_SOL.IDUSUITSOL.ordinal() );
		tab.setTamColuna( 80, GRID_SOL.IDUSUAPROVITSOL.ordinal() );

		tab.setColunaEditavel( GRID_SOL.SEL.ordinal() , true );
		
	}
	
	public void montaTela(){
		
		Container c = getTela();
		c.add( pnCli, BorderLayout.NORTH );
		c.add( pnDetalhe, BorderLayout.CENTER );
		c.add( pnRod, BorderLayout.SOUTH );

		/*
		 * Cria tab Gestão com as Abas Gestão e Filtros.
		 */
		pnCli.setPreferredSize( new Dimension( 480,160) );
		pnCli.add( tabGestao );
		tabGestao.add( "Gestão", pinCab );
		tabGestao.add("Filtros", pinFiltros); 
		
		
		pinCab.adic( txtDtIni, 7, 20, 95, 20, "Data inicial" );
		pinCab.adic( txtDtFim, 106, 20, 95, 20, "Data final" );

		pinCab.adic( txtCodUsu, 204, 20, 80, 20, "Cód.usu." );
		pinCab.adic( txtNomeUsu, 287, 20, 309, 20, "Nome do usuário" );
		
		pinCab.adic( txtCodProd, 7, 60, 80, 20, "Cód.prod." );
		pinCab.adic( txtDescProd, 90, 60, 210, 20, "Descrição do Produto" );
		pinCab.adic( txtCodCC, 303, 60, 140, 20, "Cód.c.c" );
		pinCab.adic( txtDescCC, 446, 60, 150, 20, "Centro de custo" );

		pinCab.adic( cbSituacaoItem , 7, 100, 150, 20, "Situação do Item");
		pinCab.adic( cbSituacaoCompra , 160, 100, 150, 20, "Situação da compra");
		pinCab.adic( cbSituacaoSol , 313, 100, 150, 20, "Situação da Solicitação");

		pinCab.adic( btBusca, 466, 90, 130, 30 );
	
		//Acaba Cabeçalho	
		txtDtIni.setVlrDate( new Date() );
		txtDtFim.setVlrDate( new Date() );
		
		/*
		 * Monta Barra de Ferramentas lateral
		 */
		pnDetalhe.add( spnTab, BorderLayout.CENTER );
		pnDetalhe.setPreferredSize( new Dimension( 40,280) );
		pnDetalhe.add( pinBarraFerramentas, BorderLayout.EAST );
		//pnDetalhe.add( pinNavegacao, BorderLayout.EAST);
		
		pinBarraFerramentas.adic( btTudo,  3, 10, 30, 30 );
		pinBarraFerramentas.adic( btNada,  3, 40, 30, 30 );
		btAprova.setPreferredSize( new Dimension ( 30, 30 ) );
		pinBarraFerramentas.adic( btAprova, 3, 190, 30, 30);
		btGerarSol.setPreferredSize( new Dimension ( 30, 30 ) );
		pinBarraFerramentas.adic( btGerarSol,  3, 220, 30, 30 );
		
		
		/*
		 * Monta Barra de notificação. 
		 */
		btSair.setPreferredSize( new Dimension( 100, 30 ) );

		pnLegenda.add( new JLabelPad( "Cancelada", imgCancelada, SwingConstants.CENTER ) );
		pnLegenda.add( new JLabelPad( "Aprovada", imgAprovada, SwingConstants.CENTER ) );
		pnLegenda.add( new JLabelPad( "Em Cotação", imgExpedida, SwingConstants.CENTER ) );
		pnLegenda.add( new JLabelPad( "Pendente", imgPendente, SwingConstants.CENTER ) );

		pnRod.add( pnLegenda );
		pnRod.add( btSair, BorderLayout.EAST );

		montaGrid();
		

		
	}
	
	public void montaListaCampos(){
		
		txtCodAlmoxarife.setNomeCampo( "CodAlmox" );
		txtCodAlmoxarife.setFK( true );

		lcAlmox.add( new GuardaCampo( txtCodAlmoxarife, "CodAlmox", "Cód.almox.", ListaCampos.DB_PK, null, false ) );
		lcAlmox.add( new GuardaCampo( txtDescAlmoxarife, "DescAlmox", "Desc.almox;", ListaCampos.DB_SI, null, false ) );
		lcAlmox.setQueryCommit( false );
		lcAlmox.setReadOnly( true );

		txtDescAlmoxarife.setSoLeitura( true );
		txtCodAlmoxarife.setTabelaExterna( lcAlmox, null );
		lcAlmox.montaSql( false, "ALMOX", "EQ" );

		txtCodProd.setNomeCampo( "CodProd" );
		txtCodProd.setFK( true );

		lcProd.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_PK, null, false ) );
		lcProd.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, null, false ) );
		lcProd.add( new GuardaCampo( txtRefProd, "RefProd", "Referência", ListaCampos.DB_SI, null, false ) );
		lcProd.setQueryCommit( false );
		lcProd.setReadOnly( true );

		txtDescProd.setSoLeitura( true );
		txtRefProd.setSoLeitura( true );
		txtCodProd.setTabelaExterna( lcProd, null );
		lcProd.montaSql( false, "PRODUTO", "EQ" );

		txtCodUsu.setNomeCampo( "IDUSU" );
		txtCodUsu.setFK( true );

		lcUsuario.add( new GuardaCampo( txtCodUsu, "IDUSU", "ID usuario", ListaCampos.DB_PK, null, false ) );
		lcUsuario.add( new GuardaCampo( txtNomeUsu, "NOMEUSU", "Nome do usuario", ListaCampos.DB_SI, null, false ) );
		lcUsuario.setQueryCommit( false );
		lcUsuario.setReadOnly( true );

		txtNomeUsu.setSoLeitura( true );
		txtCodUsu.setTabelaExterna( lcUsuario, null );
		lcUsuario.montaSql( false, "USUARIO", "SG" );

		lcCC.add( new GuardaCampo( txtCodCC, "CodCC", "Cód.cc.", ListaCampos.DB_PK, false ) );
		lcCC.add( new GuardaCampo( txtAnoCC, "AnoCC", "Ano.cc.", ListaCampos.DB_PK, false ) );
		lcCC.add( new GuardaCampo( txtDescCC, "DescCC", "Descrição do centro de custo", ListaCampos.DB_SI, false ) );
		lcCC.setReadOnly( true );
		lcCC.setQueryCommit( false );
		lcCC.montaSql( false, "CC", "FN" );
		txtCodCC.setTabelaExterna( lcCC, null );
		txtCodCC.setFK( true );
		txtCodCC.setNomeCampo( "CodCC" );
	
	}
	
	
	private void montaComboBox(){

		// ComboBox da Situação da aprovação do Item.
		lSituacaoItem.addElement( "Todos" );
		lSituacaoItem.addElement( "Pendente" );
		lSituacaoItem.addElement( "Aprovado parcial" );
		lSituacaoItem.addElement( "Aprovado total" );
		lSituacaoItem.addElement( "Não aprovado" );
		
		vSituacaoItem.addElement( "TD" );
		vSituacaoItem.addElement( "PE" );
		vSituacaoItem.addElement( "AP" );
		vSituacaoItem.addElement( "AT" );
		vSituacaoItem.addElement( "NA" );

		cbSituacaoItem.setItens( lSituacaoItem, vSituacaoItem );
		
		// ComboBox da Situação da Compra.
		lSituacaoCompra.addElement( "Todos" );
		lSituacaoCompra.addElement( "Pendente" );
		lSituacaoCompra.addElement( "Compra parcial" );
		lSituacaoCompra.addElement( "Compra total" );

		vSituacaoCompra.addElement( "TD" );
		vSituacaoCompra.addElement( "PE" );
		vSituacaoCompra.addElement( "CP" );
		vSituacaoCompra.addElement( "CT" );

		cbSituacaoCompra.setItens( lSituacaoCompra, vSituacaoCompra );
		
		
		// ComboBox da Situação do item da solicitação.
		lSituacaoSol.addElement( "Todos" );
		lSituacaoSol.addElement( "Pendente" );
		lSituacaoSol.addElement( "Solicitação finalizada" );
		lSituacaoSol.addElement( "Cancelada" );
		lSituacaoSol.addElement( "Em andamento" );
		
		vSituacaoSol.addElement( "TD" );
		vSituacaoSol.addElement( "PE" );
		vSituacaoSol.addElement( "FN" );
		vSituacaoSol.addElement( "CA" );
		vSituacaoSol.addElement( "EA" );
		
		cbSituacaoSol.setItens( lSituacaoSol, vSituacaoSol );			
		
	}
	
	private void montaListener()	{
		
		btGerarSol.setToolTipText( "Criar solicitação sumarizada" );
		btAprova.setToolTipText( "Aprova Solicitações" );
		btTudo.setToolTipText( "Selecionar tudo" );
		btNada.setToolTipText( "Limpar seleção" );
		btGerarSol.addActionListener( this );
		btTudo.addActionListener( this );
		btNada.addActionListener( this );
		btAprova.addActionListener( this );
		btBusca.addActionListener( this );
		btSair.addActionListener( this );

	}
	
	private void execLoadSolicitacao() throws SQLException{
		
		Vector<Vector<Object>> datavector = daocons.loadSolicitacao( imgColuna, imgAprovada, txtCodProd.getVlrInteger(), 
				txtDtIni.getVlrDate(), 	txtDtFim.getVlrDate(),
				Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "EQALMOX" ), txtCodAlmoxarife.getVlrInteger(),
				Aplicativo.iCodEmp,	ListaCampos.getMasterFilial( "FNCC" ), txtAnoCC.getVlrInteger(), txtCodCC.getVlrString(), 
				Aplicativo.iCodEmp, ListaCampos.getMasterFilial("SGUSUARIO" ), txtCodUsu.getVlrString(), 
				cbSituacaoItem.getVlrString(), cbSituacaoCompra.getVlrString(), cbSituacaoSol.getVlrString());
		
		
		tab.setDataVector( datavector );
	}
	
	/**
	 * Carrega os valores para a tabela de consulta. Este método é executado após carregar o ListaCampos da tabela.
	 */
	private void loadSolicitacao() {
			
		if ( txtDtIni.getVlrString().length() < 10 ){
			Funcoes.mensagemInforma( this, "Digite a data inicial!" );
			return;
		}
		else if ( txtDtFim.getVlrString().length() < 10 ) {
			Funcoes.mensagemInforma( this, "Digite a data final!" );
			return;
		} 
							
		try {
			execLoadSolicitacao();
			
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro carregando grid de contratos !\b" + err.getMessage() );
			err.printStackTrace();
		}
	}
	

	private void habilitaAprov( boolean bHab ) {

		btAprova.setEnabled( bHab );
	}
	
	private void carregaTudo( JTablePad tb ) {

		for ( int i = 0; i < tb.getNumLinhas(); i++ ) {
			tb.setValor( new Boolean( true ), i, GRID_SOL.SEL.ordinal() );
		}
	}

	private void carregaNada( JTablePad tb ) {

		for ( int i = 0; i < tb.getNumLinhas(); i++ ) {
			tb.setValor( new Boolean( false ), i, GRID_SOL.SEL.ordinal() );
		}
	}

	public void createSol() {
		Vector<Integer> solSel = new Vector<Integer>();
		if ( tab.getRowCount() <= 0 ) {
			Funcoes.mensagemInforma( this, "Não há nenhum ítem para sumarização !" );
			return;
		} 
		if (daocons.getSelecionados( tab.getDataVector() ).size() == 0 ) {
			Funcoes.mensagemInforma( this, "Selecione as solicitações a sumarizar !" );
			return;
		}		
	}
	
	public void aprovaSolicitacao()	{
	
		try{
			if ( tab.getRowCount() <= 0 ) {
				Funcoes.mensagemInforma( this, "Não há nenhum ítem para aprovar !" );
				return;
			} 
			
			Vector<SelectedSol> selecionados = daocons.getSelecionados( tab.getDataVector() );
			
			if( selecionados.size() == 0 ) {
				Funcoes.mensagemInforma( this, "Selecione a(s) solicitação(ões) a aprovar !" );
				return;		
			} else {
				if ( Funcoes.mensagemConfirma( this, "Deseja realmente aprovar os itens selecionados?" ) == JOptionPane.NO_OPTION ) {
					return;
				}
				
				if (daocons.itensAprovZero( selecionados )) {
					if ( Funcoes.mensagemConfirma( this, "Existe(m) item(ns) com quantidade a aprovar ZERADA, Deseja realmente aprova-lo(s) ?" ) == JOptionPane.NO_OPTION ) {
						return;
					}
				}
				int result = daocons.aprovaSolicitacao( selecionados );	
				execLoadSolicitacao();
				Funcoes.mensagemInforma( this, "Número de itens aprovados: " + result );
				
			}
			
		}catch (Exception e) {
			Funcoes.mensagemErro( this, "Erro carregando informações do usuário !\b" + e.getMessage() );
		}
		
	}
	
	public void actionPerformed( ActionEvent evt ) {
		 
		if ( evt.getSource() == btBusca ) {
			loadSolicitacao();
		}
		else if ( evt.getSource() == btGerarSol ) {
			createSol();
		}
		else if ( evt.getSource() == btAprova ){
			aprovaSolicitacao();
		} 
		else if ( evt.getSource() == btTudo ) {
			carregaTudo( tab );
		}
		else if ( evt.getSource() == btNada ) {
			carregaNada( tab );
		} 
		else if ( evt.getSource() == btSair ) {
			dispose();
		}
	}

	private int buscaVlrPadrao() {

		int iRet = 0;
		String sSQL = "SELECT ANOCENTROCUSTO FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			ResultSet rs = ps.executeQuery();
			if ( rs.next() )
				iRet = rs.getInt( "ANOCENTROCUSTO" );
			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar o ano-base para o centro de custo.\n" + err.getMessage() );
		}

		return iRet;
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcAlmox.setConexao( cn );
		lcProd.setConexao( cn );
		lcUsuario.setConexao( cn );
		lcCC.setConexao( cn );
		lcCC.setWhereAdic( "NIVELCC=10 AND ANOCC=" + buscaVlrPadrao() );
	
		daocons = new DAOGestaoSol(cn);
		
		try{
		    params = daocons.getAnocc( "" , Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "SGUSUARIO" ),  Aplicativo.getUsuario().getIdusu()  );
		    aprovasol = (String) params.get( "aprovasol");
		} catch (SQLException e) {
			Funcoes.mensagemErro( this, "Erro carregando informações do usuário !\b" + e.getMessage() );
		}
	    
	    habilitaAprov( !"ND".equals(aprovasol) );
	}

}
