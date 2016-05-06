package org.freedom.modulos.gms.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.modulos.gms.view.frame.crud.detail.FCompra;

public class FConsCompra extends FFilho implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCab = new JPanelPad( 0, 130 );

	private JPanelPad pnCli = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnRod = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTablePad tab = new JTablePad();

	private JScrollPane spnTab = new JScrollPane( tab );

	private JButtonPad btSair = new JButtonPad( "Sair", Icone.novo( "btSair.png" ) );

	private JButtonPad btBusca = new JButtonPad( "Buscar", Icone.novo( "btPesquisa.png" ) );

	private JTextFieldPad txtDtIni = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDtFim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private ListaCampos lcFor = new ListaCampos( this );

	private JRadioGroup<?, ?> rgFiltros = null;

	private Vector<String> vLabsFiltros = new Vector<String>();

	private Vector<String> vValsFiltros = new Vector<String>();

	public FConsCompra() {

		super( false );
		setTitulo( "Pesquisa Compra" );
		setAtribos( 10, 10, 770, 480 );

		montaTela();
		montaListaCampos();

	}

	private void montaTela() {

		Container c = getTela();
		c.add( pnRod, BorderLayout.SOUTH );
		c.add( pnCli, BorderLayout.CENTER );
		pnCli.add( pinCab, BorderLayout.NORTH );
		pnCli.add( spnTab, BorderLayout.CENTER );

		btBusca.addActionListener( this );
		btSair.addActionListener( this );

		txtDtIni.setRequerido( true );
		txtDtFim.setRequerido( true );

		txtDtIni.setVlrDate( new Date() );
		txtDtFim.setVlrDate( new Date() );

		btSair.setPreferredSize( new Dimension( 100, 30 ) );

		vValsFiltros.addElement( "CP" );
		vValsFiltros.addElement( "PC" );
		vValsFiltros.addElement( "A" );
		vLabsFiltros.addElement( "Compra" );
		vLabsFiltros.addElement( "Pedido de compra" );
		vLabsFiltros.addElement( "Ambos" );
		rgFiltros = new JRadioGroup<String, String>( 1, 3, vLabsFiltros, vValsFiltros );
		rgFiltros.setVlrString( "P" );

		pnRod.add( btSair, BorderLayout.EAST );

		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );
		JLabelPad lbLinha2 = new JLabelPad();
		lbLinha2.setBorder( BorderFactory.createEtchedBorder() );
		JLabelPad lbStatus = new JLabelPad( " Filtrar:" );
		lbStatus.setOpaque( true );

		pinCab.adic( new JLabelPad( "Período:" ), 7, 5, 50, 20 );
		pinCab.adic( txtDtIni, 7, 25, 95, 20 );
		pinCab.adic( new JLabelPad( "Até" ), 111, 25, 27, 20 );
		pinCab.adic( txtDtFim, 139, 25, 95, 20 );
		pinCab.adic( new JLabelPad( "Cód.For" ), 240, 5, 70, 20 );
		pinCab.adic( txtCodFor, 240, 25, 70, 20 );
		pinCab.adic( new JLabelPad( "Razão social do fornecedor" ), 313, 5, 250, 20 );
		pinCab.adic( txtDescFor, 313, 25, 340, 20 );
		pinCab.adic( rgFiltros, 7, 55, 480, 30 );

		pinCab.adic( btBusca, 490, 55, 160, 30 );

		tab.adicColuna( "Cód." );
		tab.adicColuna( "Data entrada" );
		tab.adicColuna( "Data de emis." );
		tab.adicColuna( "Valor desconto" );
		tab.adicColuna( "Valor compra" );
		tab.adicColuna( "Cód.For" );
		tab.adicColuna( "Razão do fornecedor" );

		tab.setTamColuna( 70, 0 );
		tab.setTamColuna( 85, 1 );
		tab.setTamColuna( 90, 2 );
		tab.setTamColuna( 85, 3 );
		tab.setTamColuna( 85, 4 );
		tab.setTamColuna( 85, 5 );
		tab.setTamColuna( 250, 6 );

		tab.addMouseListener( new MouseAdapter() {

			public void mouseClicked( MouseEvent mevt ) {

				if ( mevt.getSource() == tab && mevt.getClickCount() == 2 )
					;
				abrecompra();
			}
		} );

	}

	private void abrecompra() {

		int icodCompra = ( (Integer) tab.getValor( tab.getLinhaSel(), 0 ) ).intValue();
		if ( fPrim.temTela( "Compra" ) == false ) {
			FCompra tela = new FCompra();
			fPrim.criatela( "Compra", tela, con );
			tela.exec( icodCompra );
		}

	}

	private void montaListaCampos() {

		txtCodFor.setNomeCampo( "CodFor" );
		lcFor.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.for.", ListaCampos.DB_PK, false ) );
		lcFor.add( new GuardaCampo( txtDescFor, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );
		lcFor.montaSql( false, "FORNECED", "CP" );
		txtCodFor.setFK( true );
		lcFor.setQueryCommit( false );
		lcFor.setReadOnly( true );
		txtCodFor.setTabelaExterna( lcFor, null );
	}

	private void montaTabela() {

		StringBuffer sql = new StringBuffer();
		StringBuffer where = new StringBuffer();
		StringBuffer stipo = new StringBuffer();
		ResultSet rs = null;
		PreparedStatement ps = null;

		if ( !txtCodFor.getVlrString().equals( "" ) ) {
			where.append( "AND C.CODFOR=" + txtCodFor.getVlrString() );
		}
		if ( rgFiltros.getVlrString().equals( "PC" ) ) {
			stipo.append( "='PC'" );
		}
		else if ( rgFiltros.getVlrString().equals( "CP" ) ) {
			stipo.append( "='CP'" );
		}
		else {
			stipo.append( "IN('PC','CP')" );
		}

		sql.append( "SELECT C.CODCOMPRA, C.DTENTCOMPRA, C.DTEMITCOMPRA, C.VLRDESCCOMPRA, " );
		sql.append( "C.VLRLIQCOMPRA, C.CODFOR, F.RAZFOR FROM CPCOMPRA C, CPFORNECED F, EQTIPOMOV  M " );
		sql.append( "WHERE C.CODEMP=? AND C.CODFILIAL=? AND C.CODEMPFR=F.CODEMP AND C.CODFILIALFR=F.CODFILIAL " );
		sql.append( "AND C.CODFOR=F.CODFOR AND C.CODEMPTM=M.CODEMP AND C.CODFILIALTM=M.CODFILIAL AND C.CODTIPOMOV=M.CODTIPOMOV " );
		sql.append( "AND M.TIPOMOV " + stipo + " AND C.DTEMITCOMPRA BETWEEN ? AND ? " );
		sql.append( where.toString() );

		try {

			System.out.println( sql.toString() );
			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
			ps.setDate( 3, Funcoes.dateToSQLDate( txtDtIni.getVlrDate() ) );
			ps.setDate( 4, Funcoes.dateToSQLDate( txtDtFim.getVlrDate() ) );
			rs = ps.executeQuery();

			int iLin = 0;

			tab.limpa();

			while ( rs.next() ) {

				tab.adicLinha();

				tab.setValor( rs.getInt( "CODCOMPRA" ), iLin, 0 );
				tab.setValor( rs.getDate( "DTENTCOMPRA" ), iLin, 1 );
				tab.setValor( rs.getDate( "DTEMITCOMPRA" ), iLin, 2 );
				tab.setValor( rs.getBigDecimal( "VLRDESCCOMPRA" ), iLin, 3 );
				tab.setValor( rs.getBigDecimal( "VLRLIQCOMPRA" ), iLin, 4 );
				tab.setValor( rs.getInt( "CODFOR" ), iLin, 5 );
				tab.setValor( rs.getString( "RAZFOR" ), iLin, 6 );

				iLin++;

			}

			con.commit();

		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar informações da compra" );
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btSair ) {
			dispose();
		}
		if ( evt.getSource() == btBusca ) {
			montaTabela();
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcFor.setConexao( cn );

	}
}
