/**
 * @version 23/08/2007 <BR>
 * @author Setpoint Informática Ltda. <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FRCodbarProd.java <BR>
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
 *                       Comentários sobre a classe...
 */
package org.freedom.modulos.std.view.frame.report;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.EtiquetaPPLA;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.std.view.dialog.utility.DLEtiqCompra;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JScrollPane;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.bmps.Icone;

public class FRCodbarProd extends FRelatorio implements ActionListener, CarregaListener, KeyListener {

	private static final long serialVersionUID = 1L;

	private final JPanelPad pnCampos = new JPanelPad( 600, 95 );

	private final JPanelPad pnBotoesGrid = new JPanelPad( 35, 200 );

	private final JPanelPad pnGrid = new JPanelPad( 600, 200 );

	private final JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtRefProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCodBarProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtQtdPod = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JComboBox cbSel = null;

	private JComboBoxPad cbEtiquetas = null;

	private final JButtonPad btExecuta = new JButtonPad( Icone.novo( "btExecuta.png" ) );

	private final JButtonPad btSelectCompra = new JButtonPad( Icone.novo( "btPesquisa.png" ) );

	private final JButtonPad btExcluir = new JButtonPad( Icone.novo( "btCancelar.png" ) );

	private final JButtonPad btExcluirTudo = new JButtonPad( Icone.novo( "btNada.png" ) );

	private final JTablePad tabGrid = new JTablePad();

	private final JScrollPane spnGrid = new JScrollPane( tabGrid );

	private final ListaCampos lcProduto = new ListaCampos( this );

	private final JCheckBoxPad cbPreco = new JCheckBoxPad( "Mostra preço?", "S", "N" );

	public FRCodbarProd() {

		super( true );
		setTitulo( "Etiquetas de código de barras" );
		setAtribos( 80, 30, 520, 380 );

		montaTela();
		montaListaCampos();

	}

	private void montaListaCampos() {

		/*
		 * Produto
		 */

		lcProduto.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.produto", ListaCampos.DB_PK, true ) );
		lcProduto.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProduto.add( new GuardaCampo( txtRefProd, "RefProd", "Ref. produto", ListaCampos.DB_SI, false ) );
		lcProduto.add( new GuardaCampo( txtCodBarProd, "CodBarProd", "Cód. Barras", ListaCampos.DB_SI, false ) );
		txtCodProd.setTabelaExterna( lcProduto, null );
		txtCodProd.setNomeCampo( "CodProd" );
		txtCodProd.setFK( true );
		lcProduto.setReadOnly( true );
		lcProduto.montaSql( false, "PRODUTO", "EQ" );

	}

	private void montaTela() {

		Container c = getContentPane();

		c.add( pnCampos, BorderLayout.NORTH );
		c.add( pnGrid, BorderLayout.CENTER );
		c.add( pnBotoesGrid, BorderLayout.EAST );
		c.add( spnGrid );

		Vector<String> vLabsCtb = new Vector<String>();
		Vector<Integer> vValsCtb = new Vector<Integer>();
		vLabsCtb.addElement( "<--Selecione-->" );
		vLabsCtb.addElement( "Pimaco 6280" );
		vLabsCtb.addElement( "Argox OS-214" );
		vValsCtb.addElement( EEtiqueta.NONE.ordinal() );
		vValsCtb.addElement( EEtiqueta.PIMANCO_6280.ordinal() );
		vValsCtb.addElement( EEtiqueta.ARGOX_OS214.ordinal() );

		cbEtiquetas = new JComboBoxPad( vLabsCtb, vValsCtb, JComboBoxPad.TP_STRING, 2, 0 );

		pnCampos.adic( cbEtiquetas, 7, 60, 200, 20 );
		cbPreco.setVlrString( "S" );
		pnCampos.adic( cbPreco, 210, 60, 150, 20 );

		pnCampos.adic( new JLabelPad( "Cód. Produto" ), 07, 10, 100, 20 );
		pnCampos.adic( txtCodProd, 07, 30, 80, 20 );
		pnCampos.adic( new JLabelPad( "Descrição do produto" ), 93, 10, 200, 20 );
		pnCampos.adic( txtDescProd, 93, 30, 280, 20 );
		pnCampos.adic( new JLabelPad( "qtd." ), 375, 10, 60, 20 );
		pnCampos.adic( txtQtdPod, 375, 30, 60, 20 );
		pnCampos.adic( btExecuta, 445, 20, 50, 30 );
		pnCampos.adic( btSelectCompra, 445, 55, 50, 30 );
		pnBotoesGrid.adic( btExcluir, 0, 0, 30, 30 );
		pnBotoesGrid.adic( btExcluirTudo, 0, 30, 30, 30 );

		tabGrid.adicColuna( "Cód. prod" );
		tabGrid.adicColuna( "Descrição do produto" );
		tabGrid.adicColuna( "Qtd" );

		tabGrid.setTamColuna( 60, EProduto.CODPROD.ordinal() );
		tabGrid.setTamColuna( 330, EProduto.DESCPROD.ordinal() );
		tabGrid.setTamColuna( 60, EProduto.QTDPROD.ordinal() );

		lcProduto.addCarregaListener( this );

		btExecuta.addActionListener( this );
		btSelectCompra.addActionListener( this );
		btExcluir.addActionListener( this );
		btExcluirTudo.addActionListener( this );
		txtQtdPod.addKeyListener( this );

		btExecuta.setToolTipText( "Executar" );
		btSelectCompra.setToolTipText( "Selecionar produtos da compra" );
		btExcluir.setToolTipText( "Ecluir" );
		btExcluirTudo.setToolTipText( "Excluir tudo" );

	}

	private void adicLinha( BigDecimal qtd, int codprod, String descprod ) {

		int pos = -1;

		if ( codprod == 0 ) {

			Funcoes.mensagemInforma( this, "Produto não encontrado!" );
			txtCodProd.requestFocus();
			return;
		}

		for ( int i = 0; i < tabGrid.getNumLinhas(); i++ ) {

			if ( codprod == ( (Integer) tabGrid.getValor( i, EProduto.CODPROD.ordinal() ) ).intValue() ) {
				pos = i;
				qtd = qtd.add( (BigDecimal) tabGrid.getValor( i, EProduto.QTDPROD.ordinal() ) );
				break;
			}
		}

		if ( pos == -1 ) {

			tabGrid.adicLinha();
			pos = tabGrid.getNumLinhas() - 1;
		}

		tabGrid.setValor( codprod, pos, EProduto.CODPROD.ordinal() );
		tabGrid.setValor( descprod, pos, EProduto.DESCPROD.ordinal() );
		tabGrid.setValor( qtd, pos, EProduto.QTDPROD.ordinal() );

	}

	private void excluiLinha() {

		if ( tabGrid.getLinhaSel() != -1 ) {

			tabGrid.delLinha( tabGrid.getLinhaSel() );
		}
		else {

			Funcoes.mensagemInforma( this, "Selecione uma linha na lista!" );
		}
	}

	private void excluiTudo() {

		tabGrid.limpa();
	}

	private void selectCompra() {

		DLEtiqCompra dl = new DLEtiqCompra( this );
		dl.setConexao( con );

		dl.setVisible( true );

		int codcompra = dl.getCompra();

		if ( dl.OK && codcompra > 0 ) {

			// fazer select dos itens da compra e colocar na tabela com o
			// adiclinha( quantidade, codigo do produto, descrição do produto).

			StringBuilder sql = new StringBuilder();
			sql.append( "SELECT IT.CODPROD, IT.QTDITCOMPRA, PD.DESCPROD " );
			sql.append( "FROM CPITCOMPRA IT, EQPRODUTO PD, CPCOMPRA C " );
			sql.append( "WHERE C.CODEMP=? AND C.CODFILIAL=? AND C.CODCOMPRA=? AND " );
			sql.append( "IT.CODEMP=C.CODEMP AND IT.CODFILIAL=C.CODFILIAL AND IT.CODCOMPRA=C.CODCOMPRA AND " );
			sql.append( "PD.CODEMP=IT.CODEMPPD AND PD.CODFILIAL=IT.CODFILIALPD AND PD.CODPROD=IT.CODPROD " );

			try {

				PreparedStatement ps = con.prepareStatement( sql.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "CPITCOMPRA" ) );
				ps.setInt( 3, codcompra );

				ResultSet rs = ps.executeQuery();

				while ( rs.next() ) {

					adicLinha( rs.getBigDecimal( "QTDITCOMPRA" ).setScale( 0, BigDecimal.ROUND_HALF_UP ), rs.getInt( "CODPROD" ), rs.getString( "DESCPROD" ) );
				}

				rs.close();
				ps.close();

				con.commit();
			} catch ( SQLException e ) {
				e.printStackTrace();
				Funcoes.mensagemErro( this, "Erro ao carregar itens da compra!\n" + e.getMessage(), true, con, e );
			}
		}

		dl.dispose();
	}

	private int getNrConexao() {

		int conexao = -1;

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT CURRENT_CONNECTION FROM SGEMPRESA E WHERE E.CODEMP=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				conexao = rs.getInt( "CURRENT_CONNECTION" );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar número da conexão!\n" + e.getMessage(), true, con, e );
		}

		return conexao;
	}

	private boolean removeEtiquetas() {

		boolean retorno = false;

		try {

			PreparedStatement ps = con.prepareStatement( "DELETE FROM EQETIQPROD E WHERE E.NRCONEXAO=?" );
			ps.setInt( 1, getNrConexao() );

			int exec = ps.executeUpdate();

			ps.close();

			con.commit();

			if ( exec > -1 ) {
				retorno = true;
			}

		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao limpar tabela temporaria de etiquetas!\n" + e.getMessage(), true, con, e );
		}

		return retorno;
	}

	private boolean persistEtiquetas() {

		boolean retorno = false;

		int conexao = getNrConexao();

		StringBuilder sSql = new StringBuilder();

		sSql.append( "INSERT INTO EQETIQPROD " );
		sSql.append( "( NRCONEXAO, CODEMP, CODFILIAL, CODPROD ) " );
		sSql.append( "VALUES " );
		sSql.append( "( ?, ?, ?, ? )" );

		String sql = sSql.toString();

		int codprod = 0;
		int quantidade = 0;

		etiquetas : {

			for ( int i = 0; i < tabGrid.getNumLinhas(); i++ ) {

				codprod = (Integer) tabGrid.getValor( i, EProduto.CODPROD.ordinal() );
				quantidade = ( (BigDecimal) tabGrid.getValor( i, EProduto.QTDPROD.ordinal() ) ).intValue();

				for ( int j = 0; j < quantidade; j++ ) {
					if ( !insetEtiqueta( conexao, codprod, sql ) ) {
						break etiquetas;
					}
				}
			}

			retorno = true;
		}

		return retorno;
	}

	private boolean insetEtiqueta( int conexao, int codprod, String sql ) {

		boolean retorno = true;

		try {

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, conexao );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "EQETIQPROD" ) );
			ps.setInt( 4, codprod );

			ps.execute();

			con.commit();
		} catch ( SQLException e ) {
			retorno = false;
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao relacionar etiquetas!\n" + e.getMessage(), true, con, e );
		}

		return retorno;
	}

	private ResultSet getEtiquetas() {

		ResultSet rs = null;
		try {
			StringBuffer sSQL = new StringBuffer();
			sSQL.append( "SELECT E.CODPROD, P.DESCPROD, P.CODBARPROD, P.PRECOBASEPROD " );
			sSQL.append( "FROM EQETIQPROD E, EQPRODUTO P " );
			sSQL.append( "WHERE E.NRCONEXAO=? AND " );
			sSQL.append( "P.CODEMP=E.CODEMP AND P.CODFILIAL=E.CODFILIAL AND P.CODPROD=E.CODPROD " );

			PreparedStatement ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, getNrConexao() );

			rs = ps.executeQuery();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}

		return rs;
	}

	private Object[] montaEtiquetas() {

		Object[] buffer = new Object[ 3 ];
		StringBuilder bufferImprimir = new StringBuilder();
		ImprimeOS imp = new ImprimeOS( "", con );

		try {

			buffer[ 0 ] = imp;
			buffer[ 1 ] = bufferImprimir;

			ResultSet rs = getEtiquetas();

			EtiquetaPPLA etiqueta;

			while ( rs.next() ) {

				etiqueta = new EtiquetaPPLA();

				etiqueta.appendString( 100, 50, rs.getString( "DESCPROD" ) );

				bufferImprimir.append( etiqueta.command() );
			}

		} catch ( Exception e ) {
			e.printStackTrace();
			buffer = null;
		}

		return buffer;
	}

	private EEtiqueta getTpEtiquetas() {

		EEtiqueta etiqueta = EEtiqueta.NONE;

		if ( EEtiqueta.NONE.ordinal() == cbEtiquetas.getVlrInteger() ) {
			Funcoes.mensagemInforma( this, "Selecione um modelo de etiqueta!" );
		}
		else if ( EEtiqueta.PIMANCO_6280.ordinal() == cbEtiquetas.getVlrInteger() ) {
			etiqueta = EEtiqueta.PIMANCO_6280;
		}
		else if ( EEtiqueta.ARGOX_OS214.ordinal() == cbEtiquetas.getVlrInteger() ) {
			etiqueta = EEtiqueta.ARGOX_OS214;
		}

		return etiqueta;
	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		HashMap<String, Object> hParam = new HashMap<String, Object>();

		if ( removeEtiquetas() ) {

			if ( persistEtiquetas() ) {

				EEtiqueta etiqueta = getTpEtiquetas();

				if ( bVisualizar==TYPE_PRINT.VIEW ) {
					if ( etiqueta.tipo == EEtiqueta.JASPER ) {
						FPrinterJob dlGr = null;
						if ( !"".equals( getTpEtiquetas() ) ) {

							hParam.put( "MOSTRAPRECO", cbPreco.getVlrString() );
							dlGr = new FPrinterJob( etiqueta.local, "Etiquetas", null, getEtiquetas(), hParam, this );
							dlGr.setVisible( true );
						}
					}
				}
				else {

					if ( etiqueta.tipo == EEtiqueta.JASPER ) {
						try {
							FPrinterJob dlGr = null;
							hParam.put( "MOSTRAPRECO", cbPreco.getVlrString() );
							dlGr = new FPrinterJob( etiqueta.local, "Etiquetas", null, getEtiquetas(), hParam, this );
							JasperPrintManager.printReport( dlGr.getRelatorio(), true );
						} catch ( Exception err ) {
							Funcoes.mensagemErro( this, "Erro na impressão de Etiquetas!" + err.getMessage(), true, con, err );
						}
					}
					// impressora de etiquetas
					else {

						ImprimeOS imp = new ImprimeOS( "", con, "ET", false );
						Object[] etiquetas = montaEtiquetas();
						imp.gravaTexto( etiquetas[ 1 ].toString() );
						imp.fechaGravacao();
						imp.preview( this );
					}
				}
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcProduto.setConexao( cn );

	}

	public void afterCarrega( CarregaEvent cevt ) {

		if ( cevt.getListaCampos() == lcProduto ) {
			txtQtdPod.setVlrString( "1" );
		}
	}

	@ Override
	public void keyPressed( KeyEvent kevt ) {

		super.keyPressed( kevt );

		if ( kevt.getSource() == txtQtdPod ) {

			if ( kevt.getKeyCode() == KeyEvent.VK_ENTER ) {
				btExecuta.doClick();
			}
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		super.actionPerformed( evt );

		if ( evt.getSource() == btExecuta ) {
			adicLinha( new BigDecimal( txtQtdPod.getVlrString() ), txtCodProd.getVlrInteger().intValue(), txtDescProd.getVlrString() );
			txtCodProd.requestFocus();
		}
		else if ( evt.getSource() == btSelectCompra ) {
			selectCompra();
		}
		else if ( evt.getSource() == btExcluir ) {
			excluiLinha();
		}
		else if ( evt.getSource() == btExcluirTudo ) {
			excluiTudo();
			txtCodProd.requestFocus();
		}
	}

	public void beforeCarrega( CarregaEvent cevt ) {

	}

	private enum EProduto {

		CODPROD, DESCPROD, QTDPROD
	}

	private enum EEtiqueta {

		NONE( 0, "" ), PIMANCO_6280( 0, "relatorios/Pimaco6280.jasper" ), ARGOX_OS214( 1, "" );

		final static int JASPER = 0;

		final static int ETIQUETA = 1;

		int tipo;

		String local;

		EEtiqueta( int tipo, String local ) {

			this.tipo = tipo;
			this.local = local;
		}
	}
}
