/**
 * @version 08/12/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FConsProd.java <BR>
 * 
 *         Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *         modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *         na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *         Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *         sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *         Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *         Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *         escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA <BR>
 * <BR>
 * 
 *         Tela de consulta de produtos. Reune informações de últimas compras, vendas, kardex, similaridades etc...
 * 
 */

package org.freedom.modulos.std.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.gms.view.frame.crud.detail.FCompra;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.std.view.dialog.report.DLRConsProd;
import org.freedom.modulos.std.view.dialog.utility.DLBuscaProd;
import org.freedom.modulos.std.view.frame.crud.detail.FVenda;

public class FConsProd extends FRelatorio implements ActionListener, ChangeListener, CarregaListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCab = new JPanelPad( 0, 150 );

	private JPanelPad pinPeriodoCompra = new JPanelPad( 0, 60 );

	private JPanelPad pinPeriodoVenda = new JPanelPad( 0, 60 );

	private JPanelPad pinPeriodoCompraCab = new JPanelPad( 0, 50 );

	private JPanelPad pinPeriodoVendaCab = new JPanelPad( 0, 50 );

	private JPanelPad pnGeral = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnForneced = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnCompras = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnVendas = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnRod = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	// private JPanelPad pnProduto = new JPanelPad(JPanelPad.TP_JPANEL,new BorderLayout());
	private JButtonPad btExecCompra = new JButtonPad( Icone.novo( "btExecuta.png" ) );

	private JButtonPad btExecVenda = new JButtonPad( Icone.novo( "btExecuta.png" ) );

	// private JLabelPad lbA = new JLabelPad("à");
	private JPanelPad pinLbPeriodoCompra = new JPanelPad( 53, 15 );

	private JPanelPad pinLbPeriodoVenda = new JPanelPad( 53, 15 );

	private JTablePad tab = new JTablePad();

	private JTablePad tabFor = new JTablePad();

	private JTablePad tabCompras = new JTablePad();

	private JTablePad tabVendas = new JTablePad();

	private JScrollPane spnFor = new JScrollPane( tabFor );

	private JScrollPane spnCompras = new JScrollPane( tabCompras );

	private JScrollPane spnVendas = new JScrollPane( tabVendas );

	private JTabbedPanePad tpn = new JTabbedPanePad();

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtRefProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldFK txtSldProd = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, 3 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtPrecoBaseProd = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtLocalProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodUnid = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldFK txtDescUnid = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCodMarca = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldFK txtDescMarca = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodGrup = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldFK txtDescGrup = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodFisc = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldFK txtDescFisc = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtDtUltCp = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldFK txtQtUltCp = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, 2 );
	
	private JTextFieldFK txtVlrUltCp = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtDtCpIni = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDtCpFim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDtVdIni = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDtVdFim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private ListaCampos lcFisc = new ListaCampos( this, "FC" );

	private ListaCampos lcUnid = new ListaCampos( this, "UD" );

	private ListaCampos lcProd = new ListaCampos( this, "PR" );

	private ListaCampos lcProd2 = new ListaCampos( this, "PR" );

	private ListaCampos lcMarca = new ListaCampos( this, "MC" );

	private ListaCampos lcGrup = new ListaCampos( this, "GP" );
	
	private JCheckBoxPad cbPorConserto = new JCheckBoxPad( "Por item de O.S", "S", "N" );

	private boolean bPrefs[] = null;

	public FConsProd() {

		setTitulo( "Consulta produtos" );
		setAtribos( 50, 25, 650, 470 );

		txtLocalProd.setAtivo( false );
		txtCodUnid.setAtivo( false );
		txtCodMarca.setAtivo( false );
		txtCodGrup.setAtivo( false );
		txtCodFisc.setAtivo( false );

		setPanel( pnGeral );

		pnRod.setPreferredSize( new Dimension( 0, 230 ) );
		pnRod.add( tpn, BorderLayout.CENTER );

		pnGeral.add( pinCab, BorderLayout.NORTH );
		pnGeral.add( pnRod, BorderLayout.CENTER );

		tpn.setTabLayoutPolicy( JTabbedPanePad.SCROLL_TAB_LAYOUT );
		tpn.setPreferredSize( new Dimension( 600, 30 ) );

		txtCodProd.setRequerido( true );
		lcProd.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcProd.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtSldProd, "SldProd", "Saldo", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtPrecoBaseProd, "PrecoBaseProd", "Preço base", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtLocalProd, "LocalProd", "Local armz.", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtCodUnid, "CodUnid", "Cód.und.", ListaCampos.DB_FK, false ) );
		lcProd.add( new GuardaCampo( txtCodMarca, "CodMarca", "Cód.marca.", ListaCampos.DB_FK, false ) );
		lcProd.add( new GuardaCampo( txtCodGrup, "CodGrup", "Cód.grupo", ListaCampos.DB_FK, false ) );
		lcProd.add( new GuardaCampo( txtCodFisc, "CodFisc", "Cód.fisc.", ListaCampos.DB_FK, false ) );
		lcProd.add( new GuardaCampo( txtDtUltCp, "DtUltCpProd", "Dt.cp.", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtQtUltCp, "QtdUltCpProd", "Qt.cp.", ListaCampos.DB_SI, false ) );
		//lcProd.add( new GuardaCampo( txtVlrUltCp, "VlrUltCpProd", "Qt.cp.", ListaCampos.DB_SI, false ) );

		txtCodProd.setTabelaExterna( lcProd, FProduto.class.getCanonicalName() );
		txtCodProd.setNomeCampo( "CodProd" );
		txtCodProd.setFK( true );
		lcProd.setReadOnly( true );
		lcProd.montaSql( false, "PRODUTO", "EQ" );

		lcProd2.add( new GuardaCampo( txtRefProd, "RefProd", "Ref.prod.", ListaCampos.DB_PK, true ) );
		lcProd2.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtSldProd, "SldProd", "Saldo", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtPrecoBaseProd, "PrecoBaseProd", "Preço base", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtLocalProd, "LocalProd", "Local armz.", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtCodUnid, "CodUnid", "Cód.und.", ListaCampos.DB_FK, false ) );
		lcProd2.add( new GuardaCampo( txtCodMarca, "CodMarca", "Cód.marca.", ListaCampos.DB_FK, false ) );
		lcProd2.add( new GuardaCampo( txtCodGrup, "CodGrup", "Cód.grupo", ListaCampos.DB_FK, false ) );
		lcProd2.add( new GuardaCampo( txtCodFisc, "CodFisc", "Cód.fisc.", ListaCampos.DB_FK, false ) );
		lcProd2.add( new GuardaCampo( txtDtUltCp, "DtUltCpProd", "Dt.cp.", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtQtUltCp, "QtdUltCpProd", "Qt.cp.", ListaCampos.DB_SI, false ) );
		//lcProd2.add( new GuardaCampo( txtVlrUltCp, "VlrUltCpProd", "Vlr.cp.", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_SI, false ) );
		txtRefProd.setTabelaExterna( lcProd2, FProduto.class.getCanonicalName() );
		txtRefProd.setNomeCampo( "RefProd" );
		txtRefProd.setFK( true );
		lcProd2.setReadOnly( true );
		lcProd2.montaSql( false, "PRODUTO", "EQ" );

		lcUnid.add( new GuardaCampo( txtCodUnid, "CodUnid", "Cód.und.", ListaCampos.DB_PK, false ) );
		lcUnid.add( new GuardaCampo( txtDescUnid, "DescUnid", "Descrição da unidade", ListaCampos.DB_SI, false ) );
		lcUnid.montaSql( false, "UNIDADE", "EQ" );
		lcUnid.setReadOnly( true );
		lcUnid.setQueryCommit( false );
		txtCodUnid.setTabelaExterna( lcUnid, null );

		lcMarca.add( new GuardaCampo( txtCodMarca, "CodMarca", "Cód.marc.", ListaCampos.DB_PK, false ) );
		lcMarca.add( new GuardaCampo( txtDescMarca, "DescMarca", "Descrição da marca", ListaCampos.DB_SI, false ) );
		lcMarca.montaSql( false, "MARCA", "EQ" );
		lcMarca.setReadOnly( true );
		lcMarca.setQueryCommit( false );
		txtCodMarca.setTabelaExterna( lcMarca, null );

		lcGrup.add( new GuardaCampo( txtCodGrup, "CodGrup", "Cód.grupo", ListaCampos.DB_PK, false ) );
		lcGrup.add( new GuardaCampo( txtDescGrup, "DescGrup", "Descrição do grupo", ListaCampos.DB_SI, false ) );
		lcGrup.montaSql( false, "GRUPO", "EQ" );
		lcGrup.setReadOnly( true );
		lcGrup.setQueryCommit( false );
		txtCodGrup.setTabelaExterna( lcGrup, null );

		lcFisc.add( new GuardaCampo( txtCodFisc, "CodFisc", "Cód.fisc", ListaCampos.DB_PK, false ) );
		lcFisc.add( new GuardaCampo( txtDescFisc, "DescFisc", "Descrição fiscal", ListaCampos.DB_SI, false ) );
		lcFisc.montaSql( false, "CLFISCAL", "LF" );
		lcFisc.setReadOnly( true );
		lcFisc.setQueryCommit( false );
		txtCodFisc.setTabelaExterna( lcFisc, null );

		pinCab.adic( txtDescProd, 80, 20, 180, 20 );
		pinCab.adic( new JLabelPad( "Saldo" ), 260, 0, 60, 20 );
		pinCab.adic( txtSldProd, 260, 20, 60, 20 );
		pinCab.adic( new JLabelPad( "Preço base" ), 325, 0, 70, 20 );
		pinCab.adic( txtPrecoBaseProd, 325, 20, 70, 20 );
		pinCab.adic( new JLabelPad( "Dt.ult.comp." ), 400, 0, 80, 20 );
		pinCab.adic( txtDtUltCp, 400, 20, 80, 20 );
		pinCab.adic( new JLabelPad( "Qt.ult.comp." ), 480, 0, 60, 20 );
		pinCab.adic( txtQtUltCp, 480, 20, 60, 20 );
		pinCab.adic( new JLabelPad( "Vlr.ult.comp." ), 550, 0, 80, 20 );
		pinCab.adic( txtVlrUltCp, 550, 20, 80, 20 );
		
		pinCab.adic( new JLabelPad( "Local armz." ), 7, 40, 100, 20 );
		pinCab.adic( txtLocalProd, 7, 60, 100, 20 );
		pinCab.adic( new JLabelPad( "Cód.unid." ), 110, 40, 200, 20 );
		pinCab.adic( txtCodUnid, 110, 60, 67, 20 );
		pinCab.adic( new JLabelPad( "Descrição da unidade" ), 180, 40, 147, 20 );
		pinCab.adic( txtDescUnid, 180, 60, 147, 20 );
		pinCab.adic( new JLabelPad( "Cód.mac." ), 330, 40, 230, 20 );
		pinCab.adic( txtCodMarca, 330, 60, 67, 20 );
		pinCab.adic( new JLabelPad( "Descrição da marca" ), 400, 40, 230, 20 );
		pinCab.adic( txtDescMarca, 400, 60, 230, 20 );

		pinCab.adic( new JLabelPad( "Cód.desc.grupo" ), 7, 80, 220, 20 );
		pinCab.adic( txtCodGrup, 7, 100, 100, 20 );
		pinCab.adic( new JLabelPad( "Descrição do grupo" ), 110, 80, 210, 20 );
		pinCab.adic( txtDescGrup, 110, 100, 210, 20 );
		pinCab.adic( new JLabelPad( "Cód.d.cl.fisc." ), 323, 80, 180, 20 );
		pinCab.adic( txtCodFisc, 323, 100, 87, 20 );
		pinCab.adic( new JLabelPad( "Declaração da classe fiscal" ), 413, 80, 180, 20 );
		pinCab.adic( txtDescFisc, 413, 100, 215, 20 );

		btExecCompra.addActionListener( this );
		btExecVenda.addActionListener( this );

		tpn.setTabPlacement( SwingConstants.BOTTOM );

		tab.adicColuna( "" );
		tab.adicColuna( "" );
		tab.adicColuna( "" );
		tab.adicColuna( "" );
		tab.adicColuna( "" );
		tab.adicColuna( "" );

		tab.setTamColuna( 105, 0 );
		tab.setTamColuna( 105, 1 );
		tab.setTamColuna( 105, 2 );
		tab.setTamColuna( 105, 3 );
		tab.setTamColuna( 105, 4 );
		tab.setTamColuna( 105, 5 );

		// Tab Fornecedores

		tpn.addTab( "Fornecedores", pnForneced );
		pnForneced.add( spnFor, BorderLayout.CENTER );

		tabFor.adicColuna( "Cód.for." );
		tabFor.adicColuna( "Fornecedor" );
		tabFor.adicColuna( "Ref.prod." );

		tabFor.setTamColuna( 80, 0 );
		tabFor.setTamColuna( 400, 1 );
		tabFor.setTamColuna( 130, 2 );

		// Tab Compras

		tpn.addTab( "Compras", pnCompras );
		pinPeriodoCompraCab.adic( txtDtCpIni, 7, 10, 97, 20 );
		pinPeriodoCompraCab.adic( new JLabelPad( "à" ), 107, 10, 7, 20 );
		pinPeriodoCompraCab.adic( txtDtCpFim, 117, 10, 97, 20 );
		pinPeriodoCompraCab.adic( btExecCompra, 220, 5, 30, 30 );
		pinLbPeriodoCompra.adic( new JLabelPad( " Periodo" ), 0, 0, 51, 15 );
		pinLbPeriodoCompra.tiraBorda();
		pinPeriodoCompra.adic( pinLbPeriodoCompra, 10, 2, 51, 15 );
		pinPeriodoCompra.adic( pinPeriodoCompraCab, 7, 10, 260, 44 );
		pnCompras.add( pinPeriodoCompra, BorderLayout.NORTH );
		pnCompras.add( spnCompras, BorderLayout.CENTER );

		Calendar cPeriodoCp = Calendar.getInstance();
		txtDtCpFim.setVlrDate( cPeriodoCp.getTime() );
		cPeriodoCp.set( Calendar.DAY_OF_MONTH, cPeriodoCp.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDtCpIni.setVlrDate( cPeriodoCp.getTime() );

		tabCompras.adicColuna( "Cód.for." );
		tabCompras.adicColuna( "NF." );
		tabCompras.adicColuna( "Fornecedor" );
		tabCompras.adicColuna( "Item" );
		tabCompras.adicColuna( "Data" );
		tabCompras.adicColuna( "Qtd.It." );
		tabCompras.adicColuna( "Vlr.Compra" );
		tabCompras.adicColuna( "Vlr.prod." );
		tabCompras.adicColuna( "Vlr.desc." );
		tabCompras.adicColuna( "Vlr.adic." );
		tabCompras.adicColuna( "Vlr.liq." );
		tabCompras.adicColuna( "Vlr.Icms." );
		tabCompras.adicColuna( "Vlr.IPI." );

		tabCompras.setTamColuna( 50, 0 );
		tabCompras.setTamColuna( 50, 1 );
		tabCompras.setTamColuna( 200, 2 );
		tabCompras.setTamColuna( 40, 3 );
		tabCompras.setTamColuna( 95, 4 );
		tabCompras.setTamColuna( 75, 5 );
		tabCompras.setTamColuna( 75, 6 );
		tabCompras.setTamColuna( 75, 7 );
		tabCompras.setTamColuna( 75, 8 );
		tabCompras.setTamColuna( 75, 9 );
		tabCompras.setTamColuna( 75, 10 );
		tabCompras.setTamColuna( 75, 11 );
		tabCompras.setTamColuna( 75, 12 );

		// Tab Vendas

		tpn.addTab( "Vendas", pnVendas );
		pinPeriodoVendaCab.adic( txtDtVdIni, 7, 10, 97, 20 );
		pinPeriodoVendaCab.adic( new JLabelPad( "à" ), 107, 10, 7, 20 );
		pinPeriodoVendaCab.adic( txtDtVdFim, 117, 10, 97, 20 );
		pinPeriodoVendaCab.adic( btExecVenda, 220, 5, 30, 30 );
		pinLbPeriodoVenda.adic( new JLabelPad( " Periodo" ), 0, 0, 51, 15 );
		pinLbPeriodoVenda.tiraBorda();
		pinPeriodoVenda.adic( pinLbPeriodoVenda, 10, 2, 51, 15 );
		pinPeriodoVenda.adic( pinPeriodoVendaCab, 7, 10, 260, 44 );
		
		pinPeriodoVenda.adic(cbPorConserto, 300, 20, 120, 20);
		
		pnVendas.add( pinPeriodoVenda, BorderLayout.NORTH );
		pnVendas.add( spnVendas, BorderLayout.CENTER );

		Calendar cPeriodoVd = Calendar.getInstance();
		txtDtVdFim.setVlrDate( cPeriodoVd.getTime() );
		cPeriodoVd.set( Calendar.DAY_OF_MONTH, cPeriodoVd.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDtVdIni.setVlrDate( cPeriodoVd.getTime() );

		tabVendas.adicColuna( "N.pedido" );
		tabVendas.adicColuna( "NF." );
		tabVendas.adicColuna( "Cód.cli." );
		tabVendas.adicColuna( "Razão social do cliente" );
		tabVendas.adicColuna( "Item" );
		tabVendas.adicColuna( "Data" );
		tabVendas.adicColuna( "Qtd.It." );
		tabVendas.adicColuna( "Vlr.Venda" );
		tabVendas.adicColuna( "Vlr.prod." );
		tabVendas.adicColuna( "Vlr.desc." );
		tabVendas.adicColuna( "Vlr.adic." );
		tabVendas.adicColuna( "Vlr.liq." );
		tabVendas.adicColuna( "Vlr.Icms" );
		tabVendas.adicColuna( "Vlr.IPI." );
		tabVendas.adicColuna( "TipoVenda" );

		tabVendas.setTamColuna( 50, 0 );
		tabVendas.setTamColuna( 50, 1 );
		tabVendas.setTamColuna( 50, 2 );
		tabVendas.setTamColuna( 200, 3 );
		tabVendas.setTamColuna( 40, 4 );
		tabVendas.setTamColuna( 95, 5 );
		tabVendas.setTamColuna( 75, 6 );
		tabVendas.setTamColuna( 75, 7 );
		tabVendas.setTamColuna( 75, 8 );
		tabVendas.setTamColuna( 75, 9 );
		tabVendas.setTamColuna( 75, 10 );
		tabVendas.setTamColuna( 75, 11 );
		tabVendas.setTamColuna( 75, 12 );
		tabVendas.setTamColuna( 75, 13 );
		tabVendas.setColunaInvisivel( 14 );

		tpn.addChangeListener( this );
		lcProd2.addCarregaListener( this );
		
		txtCodProd.addKeyListener( new KeyAdapter() {

			public void keyPressed( KeyEvent kevt ) {

				if ( kevt.getKeyCode() == KeyEvent.VK_ENTER ) {
					carregaProdFor();
					// carregaSimilares();
				}
			}
		} );

		txtRefProd.addKeyListener( new KeyAdapter() {

			public void keyPressed( KeyEvent kevt ) {

				if ( kevt.getKeyCode() == KeyEvent.VK_ENTER ) {
					carregaProdFor();
					// carregaSimilares();
				}
			}
		} );

		tabCompras.addMouseListener( new MouseAdapter() {

			public void mouseClicked( MouseEvent mevt ) {

				if ( mevt.getSource() == tabCompras && mevt.getClickCount() == 2 )
					abreCompra();
			}
		} );

		tabVendas.addMouseListener( new MouseAdapter() {

			public void mouseClicked( MouseEvent mevt ) {

				if ( mevt.getSource() == tabVendas && mevt.getClickCount() == 2 )
					abreVenda();
			}
		} );

	}

	private void carregaProdFor() {

		String sSQL1 = "SELECT PF.CODFOR,PF.REFPRODFOR,F.RAZFOR FROM CPPRODFOR PF,CPFORNECED F ";
		sSQL1 += "WHERE PF.CODEMP=? AND PF.CODFILIAL = ? AND PF.CODPROD=? ";
		sSQL1 += "AND F.CODFOR = PF.CODFOR AND F.CODEMP=PF.CODEMP AND F.CODFILIAL=PF.CODFILIAL ";
		//System.out.println( sSQL1 );
		try {
			tabFor.limpa();
			PreparedStatement ps = con.prepareStatement( sSQL1 );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			ps.setInt( 3, txtCodProd.getVlrInteger().intValue() );
			ResultSet rs = ps.executeQuery();

			for ( int i = 0; rs.next(); i++ ) {
				tabFor.adicLinha();

				tabFor.setValor( rs.getString( "CODFOR" ), i, 0 );
				tabFor.setValor( rs.getString( "RAZFOR" ), i, 1 );
				tabFor.setValor( rs.getString( "REFPRODFOR" ), i, 2 );
			}
			rs.close();
			ps.close();
			con.commit();

		} catch ( SQLException err ) {
			System.out.println( "Erro ao montar a tabela de produtos/fornecedor!\n" + err.getMessage() );
			err.printStackTrace();
		}

	}

	
	public void carregaVendas( ) {
		
		if("S".equals( cbPorConserto.getVlrString() )) {
			carregaVendasPorConserto();
		}
		else {
			carregaVendasPorVenda();
		}
		
	}

	
	private void carregaVendasPorVenda() {

		if ( validaPeriodoVenda() ) {
			String sSQL2 = "SELECT IT.CODVENDA,IT.CODITVENDA,V.DTEMITVENDA,IT.PRECOITVENDA,IT.VLRICMSITVENDA,";
			sSQL2 += "IT.VLRIPIITVENDA,IT.VLRLIQITVENDA,IT.VLRDESCITVENDA,IT.CODPROD,C.CODCLI,C.RAZCLI,V.DOCVENDA,";
			sSQL2 += "IT.VLRPRODITVENDA,IT.VLRADICITVENDA,IT.QTDITVENDA, IT.TIPOVENDA ";
			sSQL2 += "FROM VDVENDA V,VDITVENDA IT,VDCLIENTE C " + "WHERE IT.CODEMP=? AND IT.CODFILIAL=? AND IT.CODPROD=? AND V.DTEMITVENDA BETWEEN ? AND ? AND ";
			sSQL2 += "V.CODVENDA=IT.CODVENDA AND V.CODEMP=IT.CODEMP AND V.CODFILIAL=IT.CODFILIAL AND V.TIPOVENDA=IT.TIPOVENDA AND C.CODCLI=V.CODCLI AND C.CODEMP=V.CODEMPCL ";
			sSQL2 += "AND C.CODFILIAL=V.CODFILIALCL " + "ORDER BY V.DTEMITVENDA DESC";

			System.out.println( sSQL2 );
			try {
				tabVendas.limpa();
				PreparedStatement ps = con.prepareStatement( sSQL2 );
				ps.setInt( 1, Aplicativo.iCodEmp );

				// Alterado o parâmetro do ListaCampos.getMasterFilial de "EQPRODUTO" para "VDVENDA"
				// pois não se trata de uma tabela compartilhada entre filiais, gerando como retorno o ID da filial
				// que está em uso, sendo usado na clausula where da instrução de pesquisa dos registros de compra e venda
				ps.setInt( 2, ListaCampos.getMasterFilial( "VDVENDA" ) );

				ps.setInt( 3, txtCodProd.getVlrInteger().intValue() );
				ps.setDate( 4, Funcoes.dateToSQLDate( txtDtVdIni.getVlrDate() ) );
				ps.setDate( 5, Funcoes.dateToSQLDate( txtDtVdFim.getVlrDate() ) );
				ResultSet rs = ps.executeQuery();

				for ( int i = 0; rs.next(); i++ ) {
					tabVendas.adicLinha();

					tabVendas.setValor( new Integer( rs.getInt( "CODVENDA" ) ), i, 0 );
					tabVendas.setValor( rs.getString( "DOCVENDA" ), i, 1 );
					tabVendas.setValor( rs.getString( "CODCLI" ), i, 2 );
					tabVendas.setValor( rs.getString( "RAZCLI" ), i, 3 );
					tabVendas.setValor( new Integer( rs.getInt( "CODITVENDA" ) ), i, 4 );

					tabVendas.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "DTEMITVENDA" ) ), i, 5 );
					tabVendas.setValor( Funcoes.strDecimalToStrCurrency( 8, 2, rs.getString( "QTDITVENDA" ) ), i, 6 );
					tabVendas.setValor( Funcoes.strDecimalToStrCurrency( 8, 2, rs.getString( "PRECOITVENDA" ) ), i, 7 );
					tabVendas.setValor( Funcoes.strDecimalToStrCurrency( 8, 2, rs.getString( "VLRPRODITVENDA" ) ), i, 8 );
					tabVendas.setValor( Funcoes.strDecimalToStrCurrency( 8, 2, rs.getString( "VLRDESCITVENDA" ) ), i, 9 );
					tabVendas.setValor( Funcoes.strDecimalToStrCurrency( 8, 2, rs.getString( "VLRADICITVENDA" ) ), i, 10 );
					tabVendas.setValor( Funcoes.strDecimalToStrCurrency( 8, 2, rs.getString( "VLRLIQITVENDA" ) ), i, 11 );
					tabVendas.setValor( Funcoes.strDecimalToStrCurrency( 8, 2, rs.getString( "VLRICMSITVENDA" ) ), i, 12 );
					tabVendas.setValor( Funcoes.strDecimalToStrCurrency( 8, 2, rs.getString( "VLRIPIITVENDA" ) ), i, 13 );
					tabVendas.setValor( rs.getString( "TIPOVENDA" ), i, 14 );

				}
				rs.close();
				ps.close();
				con.commit();

			} catch ( SQLException err ) {
				System.out.println( "Erro ao montar a tabela de vendas/produto!\n" + err.getMessage() );
				err.printStackTrace();
			}
		}
	}
	
	private void carregaVendasPorConserto() {

		StringBuilder sSQL = new StringBuilder("");
		
		if ( validaPeriodoVenda() ) {
			
			sSQL.append( "select iv.codvenda, iv.coditvenda, vd.dtemitvenda, iv.precoitvenda, iv.vlricmsitvenda, " );
			sSQL.append( "iv.vlripiitvenda, iv.vlrliqitvenda, iv.vlrdescitvenda, coalesce(pd2.codprod,pd1.codprod) codprod, ");
			sSQL.append( "coalesce(pd2.refprod, pd1.refprod) refprod, c.codcli, c.razcli, vd.docvenda, iv.vlrproditvenda, iv.vlradicitvenda, iv.qtditvenda, iv.tipovenda ");
			
			sSQL.append( "from vdcliente c, vdvenda vd, vditvenda iv "); 
			sSQL.append( "left outer join eqproduto pd1 on ");
			sSQL.append( "pd1.codemp=iv.codemppd and pd1.codfilial=iv.codfilialpd and pd1.codprod=iv.codprod ");
			sSQL.append( "left outer join eqgrupo g1 on ");
			sSQL.append( "g1.codemp=pd1.codempgp and g1.codfilial=pd1.codfilialgp and g1.codgrup=pd1.codgrup ");
			sSQL.append( "left outer join vdvendaorc vo on ");
			sSQL.append( "vo.codemp=iv.codemp and vo.codfilial=iv.codfilial and vo.codvenda=iv.codvenda and vo.tipovenda=iv.tipovenda and vo.coditvenda=iv.coditvenda ");
			sSQL.append( "left outer join eqitrecmercitositorc iro on ");
			sSQL.append( "iro.codempoc=vo.codempor and iro.codfilialoc=vo.codfilialor and iro.codorc=vo.codorc and iro.tipoorc=vo.tipoorc and iro.coditorc=vo.coditorc ");
			sSQL.append( "left outer join eqitrecmerc ir on ");
			sSQL.append( "ir.codemp=iro.codemp and ir.codfilial=iro.codfilial and ir.ticket=iro.ticket and ir.coditrecmerc=iro.coditrecmerc ");
			sSQL.append( "left outer join eqproduto pd2 on ");
			sSQL.append( "pd2.codemp=ir.codemppd and pd2.codfilial=ir.codfilialpd and pd2.codprod=ir.codprod ");
			sSQL.append( "left outer join eqgrupo g2 on ");
			sSQL.append( "g2.codemp=pd2.codempgp and g2.codfilial=pd2.codfilialgp and g2.codgrup=pd2.codgrup " );
			
			sSQL.append( "WHERE Iv.CODEMP=? AND Iv.CODFILIAL=? ");
			sSQL.append( "AND coalesce(pd2.codprod,pd1.codprod) = ? ");				
			
			sSQL.append( "AND Vd.DTEMITVENDA BETWEEN ? AND ? ");
			sSQL.append( "AND Vd.CODVENDA=Iv.CODVENDA AND Vd.CODEMP=Iv.CODEMP AND Vd.CODFILIAL=Iv.CODFILIAL AND Vd.TIPOVENDA=Iv.TIPOVENDA ");
			sSQL.append( "AND C.CODCLI=Vd.CODCLI AND C.CODEMP=Vd.CODEMPCL AND C.CODFILIAL=Vd.CODFILIALCL ");
			
			sSQL.append( " ORDER BY Vd.DTEMITVENDA DESC" );
			
			
			System.out.println( sSQL.toString() );
			try {
				tabVendas.limpa();
				PreparedStatement ps = con.prepareStatement( sSQL.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );

				// Alterado o parâmetro do ListaCampos.getMasterFilial de "EQPRODUTO" para "VDVENDA"
				// pois não se trata de uma tabela compartilhada entre filiais, gerando como retorno o ID da filial
				// que está em uso, sendo usado na clausula where da instrução de pesquisa dos registros de compra e venda
				ps.setInt( 2, ListaCampos.getMasterFilial( "VDVENDA" ) );

				ps.setInt( 3, txtCodProd.getVlrInteger().intValue() );
				ps.setDate( 4, Funcoes.dateToSQLDate( txtDtVdIni.getVlrDate() ) );
				ps.setDate( 5, Funcoes.dateToSQLDate( txtDtVdFim.getVlrDate() ) );
				ResultSet rs = ps.executeQuery();

				for ( int i = 0; rs.next(); i++ ) {
					tabVendas.adicLinha();

					tabVendas.setValor( new Integer( rs.getInt( "CODVENDA" ) ), i, 0 );
					tabVendas.setValor( rs.getString( "DOCVENDA" ), i, 1 );
					tabVendas.setValor( rs.getString( "CODCLI" ), i, 2 );
					tabVendas.setValor( rs.getString( "RAZCLI" ), i, 3 );
					tabVendas.setValor( new Integer( rs.getInt( "CODITVENDA" ) ), i, 4 );

					tabVendas.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "DTEMITVENDA" ) ), i, 5 );
					tabVendas.setValor( Funcoes.strDecimalToStrCurrency( 8, 2, rs.getString( "QTDITVENDA" ) ), i, 6 );
					tabVendas.setValor( Funcoes.strDecimalToStrCurrency( 8, 2, rs.getString( "VLRPRODITVENDA" ) ), i, 7 );
					tabVendas.setValor( Funcoes.strDecimalToStrCurrency( 8, 2, rs.getString( "VLRDESCITVENDA" ) ), i, 8 );
					tabVendas.setValor( Funcoes.strDecimalToStrCurrency( 8, 2, rs.getString( "VLRADICITVENDA" ) ), i, 9 );
					tabVendas.setValor( Funcoes.strDecimalToStrCurrency( 8, 2, rs.getString( "VLRLIQITVENDA" ) ), i, 10 );
					tabVendas.setValor( Funcoes.strDecimalToStrCurrency( 8, 2, rs.getString( "VLRICMSITVENDA" ) ), i, 11 );
					tabVendas.setValor( Funcoes.strDecimalToStrCurrency( 8, 2, rs.getString( "VLRIPIITVENDA" ) ), i, 12 );
					tabVendas.setValor( rs.getString( "TIPOVENDA" ), i, 13 );

				}
				rs.close();
				ps.close();
				con.commit();

			} catch ( SQLException err ) {
				System.out.println( "Erro ao montar a tabela de vendas/produto!\n" + err.getMessage() );
				err.printStackTrace();
			}
		}
	}

	private void carregaCompras() {

		if ( validaPeriodoCompra() ) {
			String sSQL3 = "SELECT IT.CODCOMPRA,IT.CODITCOMPRA,C.DTENTCOMPRA,IT.PRECOITCOMPRA,IT.VLRICMSITCOMPRA,";
			sSQL3 += "IT.VLRIPIITCOMPRA,IT.VLRLIQITCOMPRA,IT.VLRDESCITCOMPRA,IT.CODPROD,IT.VLRPRODITCOMPRA,IT.VLRADICITCOMPRA,IT.QTDITCOMPRA,";
			sSQL3 += "C.DOCCOMPRA,F.RAZFOR  FROM CPCOMPRA C,CPITCOMPRA IT,CPFORNECED F ";
			sSQL3 += "WHERE IT.CODEMP=? AND IT.CODFILIAL=? AND IT.CODPROD=? AND C.DTENTCOMPRA BETWEEN ? AND ? AND ";
			sSQL3 += "C.CODCOMPRA=IT.CODCOMPRA AND C.CODEMP=IT.CODEMP AND C.CODFILIAL=IT.CODFILIAL AND F.CODFOR=C.CODFOR AND F.CODEMP=C.CODEMPFR AND F.CODFILIAL=C.CODFILIALFR ";
			sSQL3 += "ORDER BY C.DTENTCOMPRA DESC";
			
			System.out.println( sSQL3 );
			try {
				tabCompras.limpa();
				PreparedStatement ps = con.prepareStatement( sSQL3 );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "VDVENDA" ) );
				ps.setInt( 3, txtCodProd.getVlrInteger().intValue() );
				ps.setDate( 4, Funcoes.dateToSQLDate( txtDtCpIni.getVlrDate() ) );
				ps.setDate( 5, Funcoes.dateToSQLDate( txtDtCpFim.getVlrDate() ) );
				ResultSet rs = ps.executeQuery();

				for ( int i = 0; rs.next(); i++ ) {
					tabCompras.adicLinha();

					tabCompras.setValor( new Integer( rs.getString( "CODCOMPRA" ) ), i, 0 );
					tabCompras.setValor( rs.getString( "DOCCOMPRA" ), i, 1 );
					tabCompras.setValor( rs.getString( "RAZFOR" ), i, 2 );
					tabCompras.setValor( rs.getString( "CODITCOMPRA" ), i, 3 );
					tabCompras.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "DTENTCOMPRA" ) ), i, 4 );

					tabCompras.setValor( Funcoes.strDecimalToStrCurrency( 8, 2, rs.getString( "QTDITCOMPRA" ) ), i, 5 );
					tabCompras.setValor( Funcoes.strDecimalToStrCurrency( 8, 2, rs.getString( "PRECOITCOMPRA" ) ), i, 6 );
					tabCompras.setValor( Funcoes.strDecimalToStrCurrency( 8, 2, rs.getString( "VLRPRODITCOMPRA" ) ), i, 7 );
					tabCompras.setValor( Funcoes.strDecimalToStrCurrency( 8, 2, rs.getString( "VLRDESCITCOMPRA" ) ), i, 8 );
					tabCompras.setValor( Funcoes.strDecimalToStrCurrency( 8, 2, rs.getString( "VLRADICITCOMPRA" ) ), i, 9 );
					tabCompras.setValor( Funcoes.strDecimalToStrCurrency( 8, 2, rs.getString( "VLRLIQITCOMPRA" ) ), i, 10 );
					tabCompras.setValor( Funcoes.strDecimalToStrCurrency( 8, 2, rs.getString( "VLRICMSITCOMPRA" ) ), i, 11 );
					tabCompras.setValor( Funcoes.strDecimalToStrCurrency( 8, 2, rs.getString( "VLRIPIITCOMPRA" ) ), i, 12 );

				}
				rs.close();
				ps.close();
				con.commit();

			} catch ( SQLException err ) {
				System.out.println( "Erro ao montar a tabela de compras/produto!\n" + err.getMessage() );
				err.printStackTrace();
			}
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btExecCompra ) {
			carregaCompras();
		}
		else if ( evt.getSource() == btExecVenda ) {
			carregaVendas();
		}
		super.actionPerformed( evt );
	}
	
	public void imprimir( TYPE_PRINT bVisualizar ) {

		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;

		String sRets;
		DLRConsProd dl = new DLRConsProd( this );
		dl.setVisible( true );
		if ( dl.OK == false ) {
			dl.dispose();
			return;
		}
		sRets = dl.getValores();

		try {

			imp.limpaPags();

			if ( sRets.equals( "F" ) ) {
				imp.montaCab();
				imp.setTitulo( "Relatorio de Consulta de Produtos" );
				imp.addSubTitulo( "Fornecedor" );
				for ( int i = 0; i < tabFor.getNumLinhas(); i++ ) {
					if ( imp.pRow() >= linPag ) {
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
						imp.incPags();
						imp.eject();
					}
					if ( imp.pRow() == 0 ) {
						imp.impCab( 136, true );

						imp.say( imp.pRow() + 0, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "|" );
						imp.say( imp.pRow() + 0, 135, "|" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "|" );
						imp.say( imp.pRow() + 0, 2, "Cód.prod.:" );
						imp.say( imp.pRow() + 0, 15, txtCodProd.getVlrString() );
						imp.say( imp.pRow() + 0, 25, "Ref.prod.:" );
						imp.say( imp.pRow() + 0, 40, txtRefProd.getVlrString() );
						imp.say( imp.pRow() + 0, 51, "Descrição :" );
						imp.say( imp.pRow() + 0, 63, txtDescProd.getVlrString() );
						imp.say( imp.pRow() + 0, 108, "Saldo :" );
						imp.say( imp.pRow() + 0, 119, txtSldProd.getVlrString() );
						imp.say( imp.pRow() + 0, 135, "|" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "|" );
						imp.say( imp.pRow() + 0, 2, "Preço Base" );
						imp.say( imp.pRow() + 0, 15, txtPrecoBaseProd.getVlrString() );
						imp.say( imp.pRow() + 0, 25, "Dt.ut.cp.:" );
						imp.say( imp.pRow() + 0, 38, txtDtUltCp.getVlrString() );
						imp.say( imp.pRow() + 0, 51, "Qtd.ult.cp.: " );
						imp.say( imp.pRow() + 0, 68, txtQtUltCp.getVlrString() );
						imp.say( imp.pRow() + 0, 80, "Loc.armaz.:" );
						imp.say( imp.pRow() + 0, 94, txtLocalProd.getVlrString() );
						imp.say( imp.pRow() + 0, 135, "|" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "|" );
						imp.say( imp.pRow() + 0, 2, "Un.:" );
						imp.say( imp.pRow() + 0, 5, txtDescUnid.getVlrString() );
						imp.say( imp.pRow() + 0, 42, "Marca :" );
						imp.say( imp.pRow() + 0, 50, Funcoes.copy( "" + txtDescMarca.getVlrString(), 7 ).trim() );
						imp.say( imp.pRow() + 0, 135, "|" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "|" );
						imp.say( imp.pRow() + 0, 2, "Cod.gp:" );
						imp.say( imp.pRow() + 0, 10, txtCodGrup.getVlrString() );
						imp.say( imp.pRow() + 0, 25, "Desc.gp:" );
						imp.say( imp.pRow() + 0, 34, txtDescGrup.getVlrString() );
						imp.say( imp.pRow() + 0, 75, "Cod.clas:" );
						imp.say( imp.pRow() + 0, 85, txtCodFisc.getVlrString() );
						imp.say( imp.pRow() + 0, 99, "Desc.cl.fisc:" );
						imp.say( imp.pRow() + 0, 114, Funcoes.copy( "" + txtDescFisc.getVlrString(), 20 ).trim() );
						imp.say( imp.pRow() + 0, 135, "|" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "|" );
						imp.say( imp.pRow() + 0, 135, "|" );
					}
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "|" );
					imp.say( imp.pRow() + 0, 2, "Cod.forn.:" );
					imp.say( imp.pRow() + 0, 15, "|" );
					imp.say( imp.pRow() + 0, 16, "Desc.forn." );
					imp.say( imp.pRow() + 0, 135, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "| " + tabFor.getValor( i, 0 ) );
					imp.say( imp.pRow() + 0, 15, "" + tabFor.getValor( i, 1 ) );

					if ( tabFor.getValor( i, 2 ).equals( "" ) ) {
						imp.say( imp.pRow() + 0, 70, "" );
					}
					else {
						imp.say( imp.pRow() + 0, 70, "Ref.prod.:." );
					}

					imp.say( imp.pRow() + 0, 90, "" + tabFor.getValor( i, 2 ) );
					imp.say( imp.pRow() + 0, 135, "|" );

				}
			}
			if ( sRets.equals( "C" ) ) {
				imp.montaCab();
				imp.setTitulo( "Relatorio de Consulta de Produtos" );
				imp.addSubTitulo( "Compras" );
				for ( int i = 0; i < tabFor.getNumLinhas(); i++ ) {
					if ( imp.pRow() >= linPag ) {
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
						imp.incPags();
						imp.eject();
					}
					if ( imp.pRow() == 0 ) {
						imp.impCab( 136, true );

						imp.say( imp.pRow() + 0, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "|" );
						imp.say( imp.pRow() + 0, 135, "|" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "|" );
						imp.say( imp.pRow() + 0, 2, "Cód.prod.:" );
						imp.say( imp.pRow() + 0, 15, txtCodProd.getVlrString() );
						imp.say( imp.pRow() + 0, 25, "Ref.prod.:" );
						imp.say( imp.pRow() + 0, 40, txtRefProd.getVlrString() );
						imp.say( imp.pRow() + 0, 51, "Descrição :" );
						imp.say( imp.pRow() + 0, 63, txtDescProd.getVlrString() );
						imp.say( imp.pRow() + 0, 108, "Saldo :" );
						imp.say( imp.pRow() + 0, 119, txtSldProd.getVlrString() );
						imp.say( imp.pRow() + 0, 135, "|" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "|" );
						imp.say( imp.pRow() + 0, 2, "Preço Base" );
						imp.say( imp.pRow() + 0, 15, txtPrecoBaseProd.getVlrString() );
						imp.say( imp.pRow() + 0, 25, "Dt.ut.cp.:" );
						imp.say( imp.pRow() + 0, 38, txtDtUltCp.getVlrString() );
						imp.say( imp.pRow() + 0, 51, "Qtd.ult.cp.: " );
						imp.say( imp.pRow() + 0, 68, txtQtUltCp.getVlrString() );
						imp.say( imp.pRow() + 0, 80, "Loc.armaz.:" );
						imp.say( imp.pRow() + 0, 94, txtLocalProd.getVlrString() );
						imp.say( imp.pRow() + 0, 135, "|" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "|" );
						imp.say( imp.pRow() + 0, 2, "Un.:" );
						imp.say( imp.pRow() + 0, 5, txtDescUnid.getVlrString() );
						imp.say( imp.pRow() + 0, 42, "Marca :" );
						imp.say( imp.pRow() + 0, 50, Funcoes.copy( "" + txtDescMarca.getVlrString(), 7 ).trim() );
						imp.say( imp.pRow() + 0, 135, "|" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "|" );
						imp.say( imp.pRow() + 0, 2, "Cod.gp:" );
						imp.say( imp.pRow() + 0, 10, txtCodGrup.getVlrString() );
						imp.say( imp.pRow() + 0, 25, "Desc.gp:" );
						imp.say( imp.pRow() + 0, 34, txtDescGrup.getVlrString() );
						imp.say( imp.pRow() + 0, 75, "Cod.clas:" );
						imp.say( imp.pRow() + 0, 85, txtCodFisc.getVlrString() );
						imp.say( imp.pRow() + 0, 99, "Desc.cl.fisc:" );
						imp.say( imp.pRow() + 0, 114, Funcoes.copy( "" + txtDescFisc.getVlrString(), 20 ).trim() );
						imp.say( imp.pRow() + 0, 135, "|" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "|" );
						imp.say( imp.pRow() + 0, 135, "|" );
					}
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "|" );
					imp.say( imp.pRow() + 0, 2, "Cod.forn.:" );
					imp.say( imp.pRow() + 0, 15, "|" );
					imp.say( imp.pRow() + 0, 16, "Desc.forn." );
					imp.say( imp.pRow() + 0, 135, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "| " + tabFor.getValor( i, 0 ) );
					imp.say( imp.pRow() + 0, 15, "" + tabFor.getValor( i, 1 ) );
					if ( tabFor.getValor( i, 2 ).equals( "" ) ) {
						imp.say( imp.pRow() + 0, 70, "" );
					}
					else {
						imp.say( imp.pRow() + 0, 70, "Ref.prod.:." );
					}
					imp.say( imp.pRow() + 0, 90, "" + tabFor.getValor( i, 2 ) );
					imp.say( imp.pRow() + 0, 135, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "|" );
					imp.say( imp.pRow() + 0, 135, "|" );
					// imp.say(imp.pRow()+0,0,"+"+StringFunctions.replicate("-",133)+"+");
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "|" );
					imp.say( imp.pRow() + 0, 2, "Cod.cP.:" );
					imp.say( imp.pRow() + 0, 12, "" + tabCompras.getValor( i, 0 ) );
					imp.say( imp.pRow() + 0, 24, "Doc.cP.:" );
					imp.say( imp.pRow() + 0, 33, "" + tabCompras.getValor( i, 1 ) );
					imp.say( imp.pRow() + 0, 43, "Cod.it.cp.:" );
					imp.say( imp.pRow() + 0, 56, "" + tabCompras.getValor( i, 3 ) );
					imp.say( imp.pRow() + 0, 65, "Dt.ent.: " );
					imp.say( imp.pRow() + 0, 73, "" + tabCompras.getValor( i, 4 ) );
					imp.say( imp.pRow() + 0, 90, "Qtd.cp.: " );
					imp.say( imp.pRow() + 0, 100, "" + tabCompras.getValor( i, 5 ) );
					imp.say( imp.pRow() + 0, 110, "Vlr.prod.: " );
					imp.say( imp.pRow() + 0, 124, "" + tabCompras.getValor( i, 6 ) );
					imp.say( imp.pRow() + 0, 135, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "|" );
					imp.say( imp.pRow() + 0, 135, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "|" );
					imp.say( imp.pRow() + 0, 2, "Vlr.desc.: " );
					imp.say( imp.pRow() + 0, 15, "" + tabCompras.getValor( i, 7 ) );
					imp.say( imp.pRow() + 0, 26, "Vlr.adic.:" );
					imp.say( imp.pRow() + 0, 40, "" + tabCompras.getValor( i, 8 ) );
					imp.say( imp.pRow() + 0, 56, "Vlr.liq.:" );
					imp.say( imp.pRow() + 0, 68, "" + tabCompras.getValor( i, 9 ) );
					imp.say( imp.pRow() + 0, 80, "Vlr.Icms.:" );
					imp.say( imp.pRow() + 0, 93, "" + tabCompras.getValor( i, 10 ) );
					imp.say( imp.pRow() + 0, 115, "Vlr.IPI.:" );
					imp.say( imp.pRow() + 0, 124, "" + tabCompras.getValor( i, 11 ) );
					imp.say( imp.pRow() + 0, 135, "|" );

				}
			}

			if ( sRets.equals( "V" ) ) {
				imp.montaCab();
				imp.setTitulo( "Relatorio de Consulta de Produtos" );
				imp.addSubTitulo( "Vendas" );
				for ( int i = 0; i < tabVendas.getNumLinhas(); i++ ) {
					if ( imp.pRow() >= linPag ) {
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
						imp.incPags();
						imp.eject();
					}
					if ( imp.pRow() == 0 ) {
						imp.impCab( 136, true );

						imp.say( imp.pRow() + 0, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "|" );
						imp.say( imp.pRow() + 0, 135, "|" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "|" );
						imp.say( imp.pRow() + 0, 2, "Cód.prod.:" );
						imp.say( imp.pRow() + 0, 15, txtCodProd.getVlrString() );
						imp.say( imp.pRow() + 0, 25, "Ref.prod.:" );
						imp.say( imp.pRow() + 0, 40, txtRefProd.getVlrString() );
						imp.say( imp.pRow() + 0, 51, "Descrição :" );
						imp.say( imp.pRow() + 0, 63, txtDescProd.getVlrString() );
						imp.say( imp.pRow() + 0, 108, "Saldo :" );
						imp.say( imp.pRow() + 0, 119, txtSldProd.getVlrString() );
						imp.say( imp.pRow() + 0, 135, "|" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "|" );
						imp.say( imp.pRow() + 0, 2, "Preço Base" );
						imp.say( imp.pRow() + 0, 15, txtPrecoBaseProd.getVlrString() );
						imp.say( imp.pRow() + 0, 25, "Dt.ut.cp.:" );
						imp.say( imp.pRow() + 0, 38, txtDtUltCp.getVlrString() );
						imp.say( imp.pRow() + 0, 51, "Qtd.ult.cp.: " );
						imp.say( imp.pRow() + 0, 68, txtQtUltCp.getVlrString() );
						imp.say( imp.pRow() + 0, 80, "Loc.armaz.:" );
						imp.say( imp.pRow() + 0, 94, txtLocalProd.getVlrString() );
						imp.say( imp.pRow() + 0, 135, "|" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "|" );
						imp.say( imp.pRow() + 0, 2, "Un.:" );
						imp.say( imp.pRow() + 0, 5, txtDescUnid.getVlrString() );
						imp.say( imp.pRow() + 0, 42, "Marca :" );
						imp.say( imp.pRow() + 0, 50, Funcoes.copy( "" + txtDescMarca.getVlrString(), 7 ).trim() );
						imp.say( imp.pRow() + 0, 135, "|" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "|" );
						imp.say( imp.pRow() + 0, 2, "Cod.gp:" );
						imp.say( imp.pRow() + 0, 10, txtCodGrup.getVlrString() );
						imp.say( imp.pRow() + 0, 25, "Desc.gp:" );
						imp.say( imp.pRow() + 0, 34, txtDescGrup.getVlrString() );
						imp.say( imp.pRow() + 0, 75, "Cod.clas:" );
						imp.say( imp.pRow() + 0, 85, txtCodFisc.getVlrString() );
						imp.say( imp.pRow() + 0, 99, "Desc.cl.fisc:" );
						imp.say( imp.pRow() + 0, 114, Funcoes.copy( "" + txtDescFisc.getVlrString(), 20 ).trim() );
						imp.say( imp.pRow() + 0, 135, "|" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, "|" );
						imp.say( imp.pRow() + 0, 135, "|" );
					}
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "|" );
					imp.say( imp.pRow() + 0, 2, "Cod.venda:" );
					imp.say( imp.pRow() + 0, 16, "" + tabVendas.getValor( i, 0 ) );
					imp.say( imp.pRow() + 0, 29, "Doc.venda : " );
					imp.say( imp.pRow() + 0, 40, "" + tabVendas.getValor( i, 1 ) );
					imp.say( imp.pRow() + 0, 53, "Cliente:" );
					imp.say( imp.pRow() + 0, 66, "" + tabVendas.getValor( i, 3 ) );
					imp.say( imp.pRow() + 0, 135, "|" );

					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "|" );
					imp.say( imp.pRow() + 0, 135, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );

					imp.say( imp.pRow() + 0, 0, "|" );
					imp.say( imp.pRow() + 0, 02, "Cod.it.vd.:" );
					imp.say( imp.pRow() + 0, 16, "" + tabVendas.getValor( i, 4 ) );
					imp.say( imp.pRow() + 0, 35, "Dt.venda.: " );
					imp.say( imp.pRow() + 0, 50, "" + tabVendas.getValor( i, 5 ) );
					imp.say( imp.pRow() + 0, 69, "Qtd.it.vd.: " );
					imp.say( imp.pRow() + 0, 80, "" + tabVendas.getValor( i, 6 ) );
					imp.say( imp.pRow() + 0, 95, "Vlr.prod.: " );
					imp.say( imp.pRow() + 0, 115, "" + tabVendas.getValor( i, 7 ) );
					imp.say( imp.pRow() + 0, 135, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "|" );
					imp.say( imp.pRow() + 0, 135, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "|" );
					imp.say( imp.pRow() + 0, 2, "Vlr.desc.: " );
					imp.say( imp.pRow() + 0, 15, "" + tabVendas.getValor( i, 8 ) );
					imp.say( imp.pRow() + 0, 26, "Vlr.adic.:" );
					imp.say( imp.pRow() + 0, 37, "" + tabVendas.getValor( i, 9 ) );
					imp.say( imp.pRow() + 0, 53, "Vlr.liq.:" );
					imp.say( imp.pRow() + 0, 65, "" + tabVendas.getValor( i, 10 ) );
					imp.say( imp.pRow() + 0, 82, "Vlr.Icms.:" );
					imp.say( imp.pRow() + 0, 95, "" + tabVendas.getValor( i, 11 ) );
					imp.say( imp.pRow() + 0, 115, "Vlr.IPI.:" );
					imp.say( imp.pRow() + 0, 124, "" + tabVendas.getValor( i, 12 ) );
					imp.say( imp.pRow() + 0, 135, "|" );

				}
			}
			imp.fechaGravacao();

			// rs.close();
			// ps.close();
			con.commit();
			dl.dispose();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro na consulta da tabela de Produtos!\n" + err.getMessage(), true, con, err );
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
	}

	public void stateChanged( ChangeEvent cevt ) {

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcProd.setConexao( con );
		lcUnid.setConexao( con );
		lcMarca.setConexao( con );
		lcGrup.setConexao( con );
		lcFisc.setConexao( con );
		lcProd2.setConexao( con );
		bPrefs = prefs();
		montaTela();
	}

	private boolean validaPeriodoCompra() {

		boolean bRetorno = false;
		if ( txtDtCpIni.getText().trim().length() == 0 ) {
		}
		else if ( txtDtCpIni.getText().trim().length() < 10 ) {
			Funcoes.mensagemInforma( this, "Data inicial inválida!" );
		}
		else if ( txtDtCpFim.getText().trim().length() < 10 ) {
			Funcoes.mensagemInforma( this, "Data final inválida!" );
		}
		else if ( txtDtCpFim.getVlrDate().before( txtDtCpIni.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final inicial que a data final!" );
		}
		else {
			bRetorno = true;
		}
		return bRetorno;
	}

	private void abreCompra() {

		int iCodCompra = ( (Integer) tabCompras.getValor( tabCompras.getLinhaSel(), 0 ) ).intValue();
		if ( fPrim.temTela( "Compra" ) == false ) {
			FCompra tela = new FCompra();
			fPrim.criatela( "Compra", tela, con );
			tela.exec( iCodCompra );
		}
	}

	private void abreVenda() {

		int codvenda = ( (Integer) tabVendas.getValor( tabVendas.getLinhaSel(), 0 ) ).intValue();
		int coditvenda = ( (Integer) tabVendas.getValor( tabVendas.getLinhaSel(), 4 ) ).intValue();
		String tipovenda = ( (String) tabVendas.getValor( tabVendas.getLinhaSel(), 13 ) );
		if ( fPrim.temTela( "Venda" ) == false ) {
			FVenda tela = new FVenda();
			fPrim.criatela( "Venda", tela, con );
			tela.exec( codvenda, coditvenda, tipovenda );
		}
	}

	private void montaTela() {

		pinCab.adic( new JLabelPad( "Descrição do produto" ), 80, 0, 200, 20 );

		if ( bPrefs[ 0 ] ) {
			txtRefProd.setBuscaAdic( new DLBuscaProd( con, "REFPROD", lcProd2.getWhereAdic() ) );
			pinCab.adic( txtRefProd, 7, 20, 70, 20, "Ref.Prod." );
			txtRefProd.requestFocus();
		}
		else {
			txtCodProd.setBuscaAdic( new DLBuscaProd( con, "CODPROD", lcProd.getWhereAdic() ) );
			pinCab.adic( txtCodProd, 7, 20, 70, 20,"Cód.Prod." );
			txtCodProd.requestFocus();
		}

	}

	private boolean[] prefs() {

		boolean[] bRetorno = new boolean[ 1 ];
		String sSQL = "SELECT USAREFPROD FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			bRetorno[ 0 ] = false;
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				bRetorno[ 0 ] = rs.getString( "UsaRefProd" ).trim().equals( "S" );
			}
			// rs.close();
			// ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage(), true, con, err );
		}
		return bRetorno;
	}

	private boolean validaPeriodoVenda() {

		boolean bRetorno = false;
		if ( txtDtVdIni.getText().trim().length() == 0 ) {
		}
		else if ( txtDtVdIni.getText().trim().length() < 10 ) {
			Funcoes.mensagemInforma( this, "Data inicial inválida!" );
		}
		else if ( txtDtVdFim.getText().trim().length() < 10 ) {
			Funcoes.mensagemInforma( this, "Data final inválida!" );
		}
		else if ( txtDtVdFim.getVlrDate().before( txtDtVdIni.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final inicial que a data final!" );
		}
		else {
			bRetorno = true;
		}
		return bRetorno;
	}

	public void beforeCarrega( CarregaEvent cevt ) {
	}

	public void afterCarrega( CarregaEvent cevt ) {
		this.carregaDadosUltimaEntrada();
	}
	
	private void carregaDadosUltimaEntrada(){
		ResultSet rs = null;
		String sWhere = "";
		StringBuilder sql = new StringBuilder();

		int iParam = 1;

		try {
			
			sql.append( "select first 1 mp.dtmovprod, mp.qtdmovprod, mp.precomovprod from eqmovprod mp " );
			sql.append( "where mp.tipomovprod='E' ");
			sql.append( "and codemppd=? and codfilialpd=? and codprod=? ");
			sql.append( "and codemp=? and codfilial=? ");
			sql.append( "order by mp.dtmovprod desc, mp.codmovprod desc ");

			PreparedStatement ps = con.prepareStatement( sql.toString() );

			ps.setInt( iParam++, Aplicativo.iCodEmp );
			ps.setInt( iParam++, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			ps.setInt( iParam++, txtCodProd.getVlrInteger() );
			ps.setInt( iParam++, Aplicativo.iCodEmp );
			ps.setInt( iParam++, ListaCampos.getMasterFilial( "EQMOVPROD" ) );
			
			rs = ps.executeQuery();

			if ( rs.next() ) {
				txtDtUltCp.setVlrDate( rs.getDate( "dtmovprod" ) );
				txtQtUltCp.setVlrBigDecimal( rs.getBigDecimal( "qtdmovprod" ) );
				txtVlrUltCp.setVlrBigDecimal( rs.getBigDecimal( "precomovprod" ) );
			}else {
				txtDtUltCp.setVlrString( "" );
				txtQtUltCp.setVlrString( "" );
				txtVlrUltCp.setVlrString( "" );
			}

			rs.close();
			ps.close();

			con.commit();

		} 
		catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar dados da ultima entrada!\n" + err.getMessage() );
		} 
		finally {
			sql = null;
		}
	}

}
