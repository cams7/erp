/**
 * @version 03/2008 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * @author Alex Rodrigues
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.rep <BR>
 *         Classe:
 * @(#)FExpImpEstoq.java <BR>
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
 *                       Formulário de exportação e importação de saldo de estoque.
 * 
 */

package org.freedom.modulos.std.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.component.ProcessoSec;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.freedom.acao.Processo;
import org.freedom.acao.RadioGroupEvent;
import org.freedom.acao.RadioGroupListener;
import org.freedom.bmps.Icone;

public class FExpImpEstoq extends FFilho implements ActionListener, RadioGroupListener {

	private static final long serialVersionUID = 1L;

	private static final int EXPORTAR = 0;

	private static final int IMPORTAR = 1;

	private final JPanelPad panelImportacao = new JPanelPad();

	private final JPanelPad panelRodape = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad panelBotoes = new JPanelPad( JPanelPad.TP_JPANEL, new FlowLayout( FlowLayout.CENTER, 6, 4 ) );

	private final JPanelPad panelSair = new JPanelPad( JPanelPad.TP_JPANEL, new FlowLayout( FlowLayout.CENTER, 6, 4 ) );

	private final JTextFieldPad txtDiretorio = new JTextFieldPad( JTextFieldPad.TP_STRING, 100, 0 );

	private JRadioGroup<String, Integer> rgModo;

	private JRadioGroup<String, String> rgFiltro1;

	private JRadioGroup<String, String> rgFiltro2;

	private final JTablePad tabProdutos = new JTablePad();

	private final JButtonPad btBuscarProdutos = new JButtonPad( "Buscar Produtos" );

	private final JButtonPad btExeportar = new JButtonPad( "Exportar" );

	private final JButtonPad btImportar = new JButtonPad( "Importar" );

	private final JButtonPad btInventario = new JButtonPad( "Executar Inventário" );

	private final JButtonPad btProdutosInvalidos = new JButtonPad( "Listar inválidos" );

	private final JButtonPad btDirtorio = new JButtonPad( "..." );

	private final JButtonPad btSair = new JButtonPad( "Sair", Icone.novo( "btSair.png" ) );

	private final JProgressBar status = new JProgressBar();

	private List<String> produtos = new ArrayList<String>();

	private List<Object[]> produtosInvalidos = new ArrayList<Object[]>();

	public FExpImpEstoq() {

		super( false );
		setAtribos( 50, 50, 520, 450 );

		montaRadioGrupos();
		montaTela();

		btBuscarProdutos.addActionListener( this );
		btExeportar.addActionListener( this );
		btImportar.addActionListener( this );
		btInventario.addActionListener( this );
		btProdutosInvalidos.addActionListener( this );
		btDirtorio.addActionListener( this );
		btSair.addActionListener( this );

		rgModo.addRadioGroupListener( this );
		rgFiltro1.addRadioGroupListener( this );
		rgFiltro2.addRadioGroupListener( this );

		status.setStringPainted( true );
		status.setString( "Selecione a local do arquivo e exportação ..." );

		btBuscarProdutos.setEnabled( false );
		btExeportar.setEnabled( false );
		btImportar.setEnabled( false );
		btInventario.setEnabled( false );

		btBuscarProdutos.setVisible( true );
		btExeportar.setVisible( true );
		btImportar.setVisible( false );
		btInventario.setVisible( false );
		btProdutosInvalidos.setVisible( false );
	}

	private void montaRadioGrupos() {

		Vector<String> labs = new Vector<String>();
		labs.add( "Exportar" );
		labs.add( "Importar" );
		Vector<Integer> vals = new Vector<Integer>();
		vals.add( EXPORTAR );
		vals.add( IMPORTAR );
		rgModo = new JRadioGroup<String, Integer>( 1, 2, labs, vals );

		Vector<String> labs1 = new Vector<String>();
		labs1.add( "Ativos" );
		labs1.add( "Inativos" );
		labs1.add( "Ambos" );
		Vector<String> vals1 = new Vector<String>();
		vals1.add( "A" );
		vals1.add( "I" );
		vals1.add( "T" );
		rgFiltro1 = new JRadioGroup<String, String>( 1, 3, labs1, vals1 );

		Vector<String> labs2 = new Vector<String>();
		labs2.add( "Compra" );
		labs2.add( "Venda" );
		labs2.add( "Ambos" );
		Vector<String> vals2 = new Vector<String>();
		vals2.add( "C" );
		vals2.add( "V" );
		vals2.add( "T" );
		rgFiltro2 = new JRadioGroup<String, String>( 1, 3, labs2, vals2 );

		rgModo.setVlrInteger( EXPORTAR );
		rgFiltro1.setVlrString( "A" );
		rgFiltro2.setVlrString( "T" );
	}

	private void montaTela() {

		getContentPane().setLayout( new BorderLayout() );

		JLabel filtros = new JLabel( "Filtros de produtos", SwingConstants.CENTER );
		JLabel linha = new JLabel();

		// ----- Parametros

		filtros.setOpaque( true );
		linha.setBorder( BorderFactory.createEtchedBorder() );

		rgFiltro1.setBorder( BorderFactory.createEmptyBorder() );
		rgFiltro2.setBorder( BorderFactory.createEmptyBorder() );

		tabProdutos.adicColuna( "Código" );
		tabProdutos.adicColuna( "Descrição" );
		tabProdutos.adicColuna( "Preço/Custo" );
		tabProdutos.adicColuna( "Saldo" );
		tabProdutos.setTamColuna( 70, 0 );
		tabProdutos.setTamColuna( 250, 1 );
		tabProdutos.setTamColuna( 70, 0 );
		tabProdutos.setTamColuna( 70, 0 );

		btExeportar.setPreferredSize( new Dimension( 120, 30 ) );
		btBuscarProdutos.setPreferredSize( new Dimension( 150, 30 ) );
		btImportar.setPreferredSize( new Dimension( 120, 30 ) );
		btInventario.setPreferredSize( new Dimension( 150, 30 ) );
		btProdutosInvalidos.setPreferredSize( new Dimension( 120, 30 ) );

		panelRodape.setPreferredSize( new Dimension( 100, 44 ) );
		panelRodape.setBorder( BorderFactory.createEtchedBorder() );

		// ----- Fim dos parametros

		panelImportacao.adic( rgModo, 7, 10, 490, 30 );

		panelImportacao.adic( new JLabel( "Local da arquivo" ), 7, 45, 350, 20 );
		panelImportacao.adic( txtDiretorio, 7, 65, 460, 20 );
		panelImportacao.adic( btDirtorio, 477, 65, 20, 20 );

		panelImportacao.adic( filtros, 17, 85, 120, 20 );
		panelImportacao.adic( linha, 7, 95, 490, 75 );
		panelImportacao.adic( rgFiltro1, 37, 105, 470, 30 );
		panelImportacao.adic( rgFiltro2, 37, 135, 470, 30 );

		panelImportacao.adic( new JScrollPane( tabProdutos ), 7, 180, 490, 150 );
		panelImportacao.adic( status, 7, 340, 490, 20 );

		panelBotoes.add( btBuscarProdutos );
		panelBotoes.add( btExeportar );
		panelBotoes.add( btImportar );
		panelBotoes.add( btInventario );
		panelBotoes.add( btProdutosInvalidos );

		panelSair.add( btSair );

		panelRodape.add( panelBotoes, BorderLayout.WEST );
		panelRodape.add( panelSair, BorderLayout.EAST );

		getContentPane().add( panelImportacao, BorderLayout.CENTER );
		getContentPane().add( panelRodape, BorderLayout.SOUTH );
	}

	private void getDiretorio() {

		try {

			FileDialog fileDialog = new FileDialog( Aplicativo.telaPrincipal, "Selecionar diretorio." );
			fileDialog.setFile( "*.txt" );
			fileDialog.setVisible( true );

			if ( fileDialog.getDirectory() != null ) {

				if ( EXPORTAR == rgModo.getVlrInteger() ) {
					txtDiretorio.setVlrString( fileDialog.getDirectory() + fileDialog.getFile() );
					status.setString( "Buscar produtos para exportação ..." );
					btBuscarProdutos.setEnabled( true );
					btBuscarProdutos.requestFocus();
				}
				else if ( IMPORTAR == rgModo.getVlrInteger() ) {
					txtDiretorio.setVlrString( fileDialog.getDirectory() + fileDialog.getFile() );
					status.setString( "Importar produtos do arquivo " + fileDialog.getFile() + " ..." );
					btImportar.setEnabled( true );
					btImportar.requestFocus();
				}
			}
			else {
				txtDiretorio.setVlrString( "" );
				btDirtorio.requestFocus();
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private void carregaProdutosExportacao() {

		produtos = new ArrayList<String>();

		try {

			status.setString( "Carregando produtos para exportação ..." );

			String filtro1 = "";
			String filtro2 = "";

			if ( "A".equals( rgFiltro1.getVlrString() ) ) {
				filtro1 = " AND P.ATIVOPROD='S' ";
			}
			else if ( "I".equals( rgFiltro1.getVlrString() ) ) {
				filtro1 = " AND P.ATIVOPROD='N' ";
			}

			if ( "C".equals( rgFiltro2.getVlrString() ) ) {
				filtro2 = "AND P.CVPROD='C' ";
			}
			else if ( "V".equals( rgFiltro2.getVlrString() ) ) {
				filtro2 = "AND P.CVPROD='V' ";
			}

			StringBuilder sql = new StringBuilder();
			sql.append( "SELECT P.CODPROD, P.DESCPROD, P.PRECOBASEPROD, P.SLDLIQPROD " );
			sql.append( "FROM EQPRODUTO P " );
			sql.append( "WHERE P.CODEMP=? AND P.CODFILIAL=? AND P.CLOTEPROD='N' " );
			sql.append( filtro1 );
			sql.append( filtro2 );
			sql.append( "ORDER BY P.CODPROD" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQPRODUTO" ) );

			ResultSet rs = ps.executeQuery();

			StringBuilder tmp = new StringBuilder();
			tabProdutos.limpa();

			DecimalFormat df1 = new DecimalFormat( "00000000" );
			DecimalFormat df2 = new DecimalFormat( "00000.00" );

			while ( rs.next() ) {

				tmp.delete( 0, tmp.length() );
				tmp.append( df1.format( rs.getInt( "CODPROD" ) ) + ";" );
				tmp.append( df2.format( rs.getBigDecimal( "PRECOBASEPROD" ) ).replace( ',', '.' ) + ";" );
				tmp.append( Funcoes.copy( rs.getString( "DESCPROD" ).trim(), 30 ) + ";" );
				tmp.append( df2.format( rs.getBigDecimal( "SLDLIQPROD" ) ).replace( ',', '.' ) );

				tabProdutos.adicLinha( new Object[] { rs.getInt( "CODPROD" ), rs.getString( "DESCPROD" ).trim(), rs.getBigDecimal( "PRECOBASEPROD" ).setScale( 2, BigDecimal.ROUND_HALF_UP ), rs.getBigDecimal( "SLDLIQPROD" ).setScale( 2, BigDecimal.ROUND_HALF_UP ) } );

				produtos.add( tmp.toString() );
			}

			status.setString( "Carregandos " + produtos.size() + " produtos para exportação..." );

			if ( produtos.size() > 0 ) {
				txtDiretorio.setEnabled( false );
				btDirtorio.setEnabled( false );
				btBuscarProdutos.setEnabled( false );
				btExeportar.setEnabled( true );
				btExeportar.requestFocus();
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar produtos!\n" + e.getMessage(), true, con, e );
		}
	}

	private void exportar() {

		if ( produtos == null || produtos.size() == 0 ) {
			Funcoes.mensagemInforma( this, "Não a produtos a exportar!" );
			return;
		}

		try {

			File file = new File( txtDiretorio.getVlrString().trim() );
			file.createNewFile();
			FileWriter fw = new FileWriter( file );
			BufferedWriter bw = new BufferedWriter( fw );

			int indice = 1;
			status.setString( "Exportando ..." );
			status.setMaximum( produtos.size() );

			for ( String linha : produtos ) {
				bw.write( linha + "\n" );
				status.setValue( indice++ );
			}

			bw.flush();
			bw.close();

			status.setString( "Concluido !" );
			status.setMaximum( 0 );

			btExeportar.setEnabled( false );

		} catch ( IOException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao exportar produtos!\n" + e.getMessage(), true, con, e );
		}
	}

	private void importar() {

		try {

			File file = new File( txtDiretorio.getVlrString().trim() );

			if ( file.exists() ) {

				txtDiretorio.setEnabled( false );
				btDirtorio.setEnabled( false );
				btImportar.setEnabled( false );

				FileReader fileReader = new FileReader( file );

				if ( fileReader == null ) {
					Funcoes.mensagemInforma( this, "Arquivo não encontrado" );
					return;
				}

				BufferedReader bufferedReader = new BufferedReader( fileReader );
				List<String> saldoImportacao = new ArrayList<String>();
				String tmp = null;

				while ( ( tmp = bufferedReader.readLine() ) != null ) {
					// if ( tmp.length() >= 57 ) { correção, alguns produtos com descrição menor não estavam sendo carregados.
					saldoImportacao.add( tmp );
					// }
				}

				bufferedReader.close();

				produtosInvalidos = new ArrayList<Object[]>();
				Object[] elementos = new Object[ 4 ];
				String[] campos = null;

				tabProdutos.limpa();

				status.setString( "Carregando saldos de produtos ..." );
				status.setMaximum( saldoImportacao.size() );
				int indice = 1;
				Integer codprod = null;
				BigDecimal custo = null;

				for ( String linha : saldoImportacao ) {

					/*
					 * codprod = Integer.parseInt( linha.substring( 0, 8 ) ); custo = getCusto( codprod ); elementos[ 0 ] = codprod; elementos[ 1 ] = linha.substring( 18, 48 ); elementos[ 2 ] = custo; elementos[ 3 ] = new BigDecimal( linha.substring( 49, 57 ).trim() ).setScale( Aplicativo.casasDec,
					 * BigDecimal.ROUND_HALF_UP );
					 */

					campos = linha.split( ";" );

					codprod = Integer.parseInt( campos[ 0 ] );
					custo = getCusto( codprod );

					elementos = new Object[ 4 ];
					elementos[ 0 ] = codprod;
					elementos[ 1 ] = campos[ 2 ];
					elementos[ 2 ] = custo;
					elementos[ 3 ] = new BigDecimal( campos[ 3 ] ).setScale( Aplicativo.casasDec, BigDecimal.ROUND_HALF_UP );

					if ( custo != null ) {
						tabProdutos.adicLinha( elementos );
					}
					else {
						produtosInvalidos.add( elementos );
					}

					status.setValue( indice++ );
				}

				status.setString( tabProdutos.getNumLinhas() + " de produtos carragados ..." );

				if ( tabProdutos.getNumLinhas() > 0 ) {
					btInventario.setEnabled( true );
					btInventario.requestFocus();
				}

				if ( produtosInvalidos.size() > 0 ) {
					btProdutosInvalidos.setVisible( true );
				}
			}
			else {
				Funcoes.mensagemErro( this, "Arquivo não encontrado!" );
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao importar arquivo!\n" + e.getMessage(), true, con, e );
		}
	}

	private BigDecimal getCusto( final Integer codprod ) throws Exception {

		BigDecimal custo = new BigDecimal( "0.00" );

		StringBuilder sql = new StringBuilder();
		sql.append( "SELECT NCUSTOMPMAX FROM EQPRODUTOSP01(?,?,?,?,?,?)" );

		PreparedStatement ps = con.prepareStatement( sql.toString() );
		ps.setInt( 1, Aplicativo.iCodEmp );
		ps.setInt( 2, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
		ps.setInt( 3, codprod );
		ps.setInt( 4, Aplicativo.iCodEmp );
		ps.setInt( 5, ListaCampos.getMasterFilial( "EQALMOX" ) );
		ps.setInt( 6, getAlmoxarifado( codprod ) );

		ResultSet rs = ps.executeQuery();

		if ( rs.next() ) {
			custo = rs.getBigDecimal( "NCUSTOMPMAX" ) != null ? rs.getBigDecimal( "NCUSTOMPMAX" ) : custo;
		}

		rs.close();
		ps.close();

		con.commit();

		return custo.setScale( Aplicativo.casasDecPre, BigDecimal.ROUND_HALF_UP );
	}

	private Integer getAlmoxarifado( final Integer codprod ) throws Exception {

		Integer almoxarifado = null;

		StringBuilder sql = new StringBuilder();
		sql.append( "SELECT CODALMOX FROM EQPRODUTO WHERE CODEMP=? AND CODFILIAL=? AND CODPROD=?" );

		PreparedStatement ps = con.prepareStatement( sql.toString() );
		ps.setInt( 1, Aplicativo.iCodEmp );
		ps.setInt( 2, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
		ps.setInt( 3, codprod );

		ResultSet rs = ps.executeQuery();

		if ( rs.next() ) {
			almoxarifado = rs.getInt( "CODALMOX" );
		}

		rs.close();
		ps.close();

		con.commit();

		return almoxarifado;
	}

	private void criarInventario() {

		try {

			// para evitar de dar o commit no meio do loop e o rollback de erro não adiantar.
			final boolean commit = con.getConnection().getAutoCommit();
			con.setAutoCommit( false );

			int tamanhoTabela = tabProdutos.getNumLinhas();

			status.setString( "Realizando inventarios ..." );
			status.setMaximum( tamanhoTabela );

			StringBuilder sql = new StringBuilder();
			sql.append( "INSERT INTO EQINVPROD " );
			sql.append( "(CODEMP, CODFILIAL, CODINVPROD, " );
			sql.append( "CODEMPPD, CODFILIALPD, CODPROD, " );
			sql.append( "CODEMPTM, CODFILIALTM, CODTIPOMOV, " );
			sql.append( "DATAINVP, QTDINVP, SLDATUALINVP, SLDDIGINVP, PRECOINVP, " );
			sql.append( "CODEMPAX, CODFILIALAX, CODALMOX) " );
			sql.append( "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) " );

			final Integer tipoMovimentoInventario = getTipoMovimentoInventario();
			final Date hoje = Funcoes.dateToSQLDate( Calendar.getInstance().getTime() );
			Integer codprod = null;
			BigDecimal saldoAtual = null;
			BigDecimal novoSaldo = null;
			BigDecimal quantidade = null;
			BigDecimal custo = null;

			Integer codInventario = null;
			Integer codAlmoarifado = null;

			PreparedStatement ps = null;

			for ( int i = 0; i < tamanhoTabela; i++ ) {

				codprod = (Integer) tabProdutos.getValor( i, 0 );
				saldoAtual = getSaldoAtual( codprod );
				novoSaldo = (BigDecimal) tabProdutos.getValor( i, 3 );
				quantidade = novoSaldo.subtract( saldoAtual );
				custo = (BigDecimal) tabProdutos.getValor( i, 2 );

				codInventario = getCodInventario();
				codAlmoarifado = getAlmoxarifado( codprod );

				ps = con.prepareStatement( sql.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "EQINVPROD" ) );
				ps.setInt( 3, codInventario );
				ps.setInt( 4, Aplicativo.iCodEmp );
				ps.setInt( 5, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
				ps.setInt( 6, codprod ); // código do produto.
				ps.setInt( 7, Aplicativo.iCodEmp );
				ps.setInt( 8, ListaCampos.getMasterFilial( "EQTIPOMOV" ) );
				ps.setInt( 9, tipoMovimentoInventario ); // código do tipo de movimento de inventário.
				ps.setDate( 10, hoje );
				ps.setBigDecimal( 11, quantidade );
				ps.setBigDecimal( 12, saldoAtual );
				ps.setBigDecimal( 13, novoSaldo );
				ps.setBigDecimal( 14, custo );
				ps.setInt( 15, Aplicativo.iCodEmp );
				ps.setInt( 16, ListaCampos.getMasterFilial( "EQALMOX" ) );
				ps.setInt( 17, codAlmoarifado );
				ps.execute();

				status.setValue( i + 1 );
			}

			status.setString( "Concluido !" );
			btInventario.setEnabled( false );

			con.setAutoCommit( commit );

			con.commit();

		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao realizar inventáros!\n" + e.getMessage(), true, con, e );
			try {
				con.rollback();
				rgModo.setVlrInteger( IMPORTAR );
			} catch ( SQLException e1 ) {
				e1.printStackTrace();
			}
		}
	}

	private Integer getTipoMovimentoInventario() throws Exception {

		Integer tipoMovimentoInventario = null;

		StringBuilder sql = new StringBuilder();
		sql.append( "SELECT CODTIPOMOV6 FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?" );

		PreparedStatement ps = con.prepareStatement( sql.toString() );
		ps.setInt( 1, Aplicativo.iCodEmp );
		ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

		ResultSet rs = ps.executeQuery();

		if ( rs.next() ) {
			tipoMovimentoInventario = rs.getInt( "CODTIPOMOV6" );
		}

		rs.close();
		ps.close();

		con.commit();

		return tipoMovimentoInventario;
	}

	private Integer getCodInventario() throws Exception {

		Integer codInventario = null;

		StringBuilder sql = new StringBuilder();
		sql.append( "SELECT MAX(CODINVPROD)+1 FROM EQINVPROD WHERE CODEMP=? AND CODFILIAL=?" );

		PreparedStatement ps = con.prepareStatement( sql.toString() );
		ps.setInt( 1, Aplicativo.iCodEmp );
		ps.setInt( 2, ListaCampos.getMasterFilial( "EQINVPROD" ) );

		ResultSet rs = ps.executeQuery();

		if ( rs.next() ) {
			codInventario = rs.getInt( 1 );
		}

		rs.close();
		ps.close();

		con.commit();

		return codInventario;
	}

	private BigDecimal getSaldoAtual( final Integer codprod ) throws Exception {

		BigDecimal saldoAtual = new BigDecimal( "0.00" );

		StringBuilder sql = new StringBuilder();
		sql.append( "SELECT NSALDOAX FROM EQPRODUTOSP01(?,?,?,?,?,?)" );

		PreparedStatement ps = con.prepareStatement( sql.toString() );
		ps.setInt( 1, Aplicativo.iCodEmp );
		ps.setInt( 2, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
		ps.setInt( 3, codprod );
		ps.setInt( 4, Aplicativo.iCodEmp );
		ps.setInt( 5, ListaCampos.getMasterFilial( "EQALMOX" ) );
		ps.setInt( 6, getAlmoxarifado( codprod ) );

		ResultSet rs = ps.executeQuery();

		if ( rs.next() ) {
			saldoAtual = rs.getBigDecimal( "NSALDOAX" ) != null ? rs.getBigDecimal( "NSALDOAX" ) : saldoAtual;
		}

		rs.close();
		ps.close();

		con.commit();

		return saldoAtual.setScale( Aplicativo.casasDec, BigDecimal.ROUND_HALF_UP );
	}

	private void showInvalidos() {

		if ( produtosInvalidos != null && produtosInvalidos.size() > 0 ) {

			Invalidos invalidos = new Invalidos( this, produtosInvalidos );
			invalidos.montaTabela();
			invalidos.setVisible( true );
		}
	}

	public void actionPerformed( ActionEvent e ) {

		if ( e.getSource() == btSair ) {
			dispose();
		}
		else if ( e.getSource() == btDirtorio ) {
			getDiretorio();
		}
		else if ( e.getSource() == btBuscarProdutos ) {
			carregaProdutosExportacao();
		}
		else if ( e.getSource() == btExeportar ) {
			ProcessoSec pSec = new ProcessoSec( 500, new Processo() {

				public void run() {

					status.updateUI();
				}
			}, new Processo() {

				public void run() {

					exportar();
				}
			} );
			pSec.iniciar();
		}
		else if ( e.getSource() == btImportar ) {
			ProcessoSec pSec = new ProcessoSec( 500, new Processo() {

				public void run() {

					status.updateUI();
				}
			}, new Processo() {

				public void run() {

					importar();
				}
			} );
			pSec.iniciar();
		}
		else if ( e.getSource() == btInventario ) {
			ProcessoSec pSec = new ProcessoSec( 500, new Processo() {

				public void run() {

					status.updateUI();
				}
			}, new Processo() {

				public void run() {

					criarInventario();
				}
			} );
			pSec.iniciar();
		}
		else if ( e.getSource() == btProdutosInvalidos ) {
			showInvalidos();
		}
	}

	public void valorAlterado( RadioGroupEvent e ) {

		if ( e.getSource() == rgModo ) {

			produtos = null;
			produtosInvalidos = null;

			tabProdutos.limpa();

			txtDiretorio.setVlrString( "" );
			txtDiretorio.setEnabled( true );
			btDirtorio.setEnabled( true );

			btBuscarProdutos.setEnabled( false );
			btExeportar.setEnabled( false );
			btImportar.setEnabled( false );
			btInventario.setEnabled( false );
			btProdutosInvalidos.setVisible( false );

			if ( e.getIndice() == 0 ) {
				status.setString( "Selecione a local do arquivo e exportação ..." );
				btBuscarProdutos.setVisible( true );
				btExeportar.setVisible( true );
				btImportar.setVisible( false );
				btInventario.setVisible( false );
			}
			else if ( e.getIndice() == 1 ) {
				status.setString( "Selecione a local do arquivo e importação ..." );
				btBuscarProdutos.setVisible( false );
				btExeportar.setVisible( false );
				btImportar.setVisible( true );
				btInventario.setVisible( true );
			}
		}
		else if ( ( e.getSource() == rgFiltro1 || e.getSource() == rgFiltro2 ) && !txtDiretorio.isEnabled() && EXPORTAR == rgModo.getVlrInteger() ) {
			tabProdutos.limpa();
			btExeportar.setEnabled( false );
			btBuscarProdutos.setEnabled( true );
			btBuscarProdutos.requestFocus();
			status.setString( "Buscar produtos para exportação ..." );
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
	}

	private class Invalidos extends FFDialogo {

		private static final long serialVersionUID = 1l;

		private final JPanelPad panelInvalidos = new JPanelPad();

		private final JPanelPad panelBotaoTXT = new JPanelPad( JPanelPad.TP_JPANEL, new FlowLayout( FlowLayout.CENTER, 6, 4 ) );

		private final JTablePad tabInvalidos = new JTablePad();

		private final JButtonPad btTexto = new JButtonPad( "Exportar para TXT", Icone.novo( "btTXT.gif" ) );

		private final List<Object[]> invalidos;

		Invalidos( final Component cOrig, final List<Object[]> invalidos ) {

			super( cOrig );
			setTitulo( "Itens Inválidos" );
			setAtribos( 520, 250 );
			setLocationRelativeTo( cOrig );
			setDefaultCloseOperation( JInternalFrame.DISPOSE_ON_CLOSE );

			this.invalidos = invalidos;

			montaTela();

			btTexto.addActionListener( this );
		}

		private void montaTela() {

			getContentPane().setLayout( new BorderLayout() );

			tabInvalidos.adicColuna( "Código" );
			tabInvalidos.adicColuna( "Descrição" );
			tabInvalidos.adicColuna( "Custo" );
			tabInvalidos.adicColuna( "Saldo" );
			tabInvalidos.setTamColuna( 70, 0 );
			tabInvalidos.setTamColuna( 250, 1 );
			tabInvalidos.setTamColuna( 70, 0 );
			tabInvalidos.setTamColuna( 70, 0 );

			panelInvalidos.adic( new JScrollPane( tabInvalidos ), 7, 10, 490, 150 );

			btTexto.setPreferredSize( new Dimension( 200, 30 ) );

			panelBotaoTXT.setPreferredSize( new Dimension( 520, 40 ) );
			panelBotaoTXT.add( btTexto );

			getContentPane().add( panelInvalidos, BorderLayout.CENTER );
			getContentPane().add( panelBotaoTXT, BorderLayout.SOUTH );
		}

		private void montaTabela() {

			if ( invalidos != null && invalidos.size() > 0 ) {

				tabInvalidos.limpa();

				for ( Object[] line : invalidos ) {
					tabInvalidos.adicLinha( line );
				}
			}
		}

		private void exportar() {

			if ( invalidos == null ) {
				return;
			}

			try {

				String localFile = null;

				FileDialog fileDialog = new FileDialog( Aplicativo.telaPrincipal, "Selecionar diretorio." );
				fileDialog.setFile( "*.txt" );
				fileDialog.setVisible( true );

				if ( fileDialog.getDirectory() != null ) {

					localFile = fileDialog.getDirectory() + fileDialog.getFile();
				}

				File file = new File( localFile );
				file.createNewFile();
				FileWriter fw = new FileWriter( file );
				BufferedWriter bw = new BufferedWriter( fw );

				for ( Object[] campos : invalidos ) {
					bw.write( String.valueOf( campos[ 0 ] ) + ";" );
					bw.write( String.valueOf( campos[ 2 ] ) + ";" );
					bw.write( String.valueOf( campos[ 1 ] ) + ";" );
					bw.write( campos[ 3 ] + "\n" );
				}

				bw.flush();
				bw.close();

				Funcoes.mensagemInforma( this, "Itens inválidos salvos." );

				this.dispose();

			} catch ( IOException e ) {
				e.printStackTrace();
				Funcoes.mensagemErro( this, "Erro ao exportar produtos!\n" + e.getMessage(), true, con, e );
			}
		}

		public void actionPerformed( ActionEvent e ) {

			if ( e.getSource() == btTexto ) {
				exportar();
			}
		}
	}
}
