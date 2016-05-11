/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Marco Antonio Sanchez<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FFrete.java <BR>
 * 
 *                 Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                 modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                 na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                 Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                 sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                 Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                 Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                 de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                 Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.std.view.frame.crud.plain;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.BorderFactory;

import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.pdv.FVenda;
import org.freedom.modulos.std.view.dialog.report.DLRFrete;
import org.freedom.modulos.std.view.frame.crud.tabbed.FTransp;

public class FFrete extends FDados implements InsertListener, FocusListener {

	private static final long serialVersionUID = 1L;

	private final JPanelPad panelIcms = new JPanelPad( 390, 100 );

	private final JTextFieldPad txtCodVenda = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 9, 0 );

	private final JTextFieldFK txtDocVenda = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 9, 0 );

	private final JTextFieldPad txtVlrLiqVenda = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private final JTextFieldPad txtSeries = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private final JTextFieldPad txtConhecFreteVD = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private final JTextFieldPad txtPercVendaFreteVD = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 9, Aplicativo.casasDec );

	private final JTextFieldFK txtDescTran = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private final JTextFieldPad txtCodTran = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtPlacaFreteVD = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldPad txtUFFreteVD = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private final JTextFieldPad txtVlrFreteVD = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private final JTextFieldPad txtVlrSegFreteVD = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );
	
	private final JTextFieldPad txtQtdFreteVD = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private final JTextFieldPad txtPesoBrutVD = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private final JTextFieldPad txtPesoLiqVD = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private final JTextFieldPad txtEspFreteVD = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldPad txtMarcaFreteVD = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldFK txtVlrIcmsFreteVD = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private final JTextFieldPad txtPercIcmsFreteVD = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private final JTextFieldPad txtRNTCVD = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );
	
	private final JCheckBoxPad cbAdicICMSFrete = new JCheckBoxPad( "adiciona valor do frete na base de ICMS?", "S", "N" );

	private JRadioGroup<String, String> rgFreteVD = null;

	private final Vector<String> vVals = new Vector<String>();

	private final Vector<String> vLabs = new Vector<String>();

	private final ListaCampos lcTran = new ListaCampos( this, "TN" );

	private final ListaCampos lcVenda = new ListaCampos( this, "" );

	public FFrete() {

		super( false );

		nav.setNavigation( true );

		setTitulo( "Lançamento de Fretes" );
		setAtribos( 50, 50, 390, 360 );

		lcCampos.addPostListener( this );

		txtCodTran.setNomeCampo( "CodTran" );

		lcTran.add( new GuardaCampo( txtCodTran, "CodTran", "Cód.tran.", ListaCampos.DB_PK, false ) );
		lcTran.add( new GuardaCampo( txtDescTran, "NomeTran", "Descrição da transporatadora", ListaCampos.DB_SI, false ) );
		txtDescTran.setListaCampos( lcTran );
		txtCodTran.setTabelaExterna( lcTran, FTransp.class.getCanonicalName() );
		txtCodTran.setFK( true );
		lcTran.montaSql( false, "TRANSP", "VD" );
		lcTran.setQueryCommit( false );
		lcTran.setReadOnly( true );

		txtCodVenda.setNomeCampo( "CodVenda" );

		lcVenda.add( new GuardaCampo( txtCodVenda, "CodVenda", "N.pedido", ListaCampos.DB_PK, false ) );
		lcVenda.add( new GuardaCampo( txtDocVenda, "DocVenda", "N.doc.", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtSeries, "Serie", "Serie", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtVlrLiqVenda, "VlrLiqVenda", "V.liq.", ListaCampos.DB_SI, false ) );
		txtDocVenda.setListaCampos( lcVenda );
		txtCodVenda.setTabelaExterna( lcVenda, FVenda.class.getCanonicalName() );
		txtCodVenda.setFK( true );
		lcVenda.montaSql( false, "VENDA", "VD" );
		lcVenda.setQueryCommit( false );
		lcVenda.setReadOnly( true );

		vVals.addElement( "C" );
		vVals.addElement( "F" );
		vLabs.addElement( "CIF" );
		vLabs.addElement( "FOB" );

		rgFreteVD = new JRadioGroup<String, String>( 2, 1, vLabs, vVals );

		txtPercVendaFreteVD.setAtivo( false );

		adicCampo( txtCodVenda, 7, 20, 110, 20, "CodVenda", "Nº pedido", ListaCampos.DB_PF, txtDocVenda, true );
		adicDescFK( txtDocVenda, 120, 20, 110, 20, "DocVenda", "Nº Doc" );
		adicDB( rgFreteVD, 233, 20, 125, 60, "TipoFreteVd", "Tipo", true );
		adicCampo( txtMarcaFreteVD, 7, 60, 110, 20, "MarcaFreteVd", "Marca", ListaCampos.DB_SI, true );
		adicCampo( txtPercVendaFreteVD, 120, 60, 110, 20, "PercVendaFreteVd", "Perc.vd.", ListaCampos.DB_SI, false );
		adicCampo( txtCodTran, 7, 100, 80, 20, "CodTran", "Cód.tran.", ListaCampos.DB_FK, true );
		adicDescFK( txtDescTran, 91, 100, 267, 20, "NomeTran", "Descrição da tansportador" );
		adicCampo( txtConhecFreteVD, 7, 140, 90, 20, "ConhecFreteVd", "Conhec.frete", ListaCampos.DB_SI, false );
		adicCampo( txtPlacaFreteVD, 99, 140, 100, 20, "PlacaFreteVd", "Placa", ListaCampos.DB_SI, true );
		adicCampo( txtUFFreteVD, 202, 140, 44, 20, "UfFreteVd", "UF.", ListaCampos.DB_SI, true );
		adicCampo( txtVlrFreteVD, 248, 140, 56, 20, "VlrFreteVd", "Valor", ListaCampos.DB_SI, true );		
		adicCampo( txtVlrSegFreteVD, 307, 140, 51, 20, "VlrSegFreteVd", "Seguro", ListaCampos.DB_SI, true );	 	
		adicCampo( txtQtdFreteVD, 7, 180, 90, 20, "QtdFreteVd", "Volumes", ListaCampos.DB_SI, true );
		adicCampo( txtPesoBrutVD, 100, 180, 77, 20, "PesoBrutVd", "P.bruto", ListaCampos.DB_SI, true );
		adicCampo( txtPesoLiqVD, 180, 180, 77, 20, "PesoLiqVd", "P.liq.", ListaCampos.DB_SI, true );
		adicCampo( txtEspFreteVD, 260, 180, 98, 20, "EspFreteVd", "Especie", ListaCampos.DB_SI, true );
		adicCampo( txtRNTCVD, 7, 220, 175, 20, "RNTCVD", "RNTC (ANTT)", ListaCampos.DB_SI, false );

		adic( panelIcms, -1, 210, 390, 100 );
		panelIcms.setBorder( BorderFactory.createEmptyBorder() );
		setPainel( panelIcms );

		adicDB( cbAdicICMSFrete, 7, 0, 300, 30, "AdicFreteBaseICM", "", true );
		// adicCampo( txtPercIcmsFreteVD, 7, 50, 90, 20, "AliqICMSFreteVD", "% icms", ListaCampos.DB_SI, false );
		adicCampo( txtVlrIcmsFreteVD, 100, 50, 150, 20, "VlrIcmsFreteVD", "Valor do icms do frete", ListaCampos.DB_SI, false );

		txtVlrIcmsFreteVD.setNaoEditavel( true );
		txtPercIcmsFreteVD.setNaoEditavel( true );

		txtPlacaFreteVD.setStrMascara( "###-####" );

		setListaCampos( true, "FRETEVD", "VD" );

		lcCampos.addInsertListener( this );
		txtVlrFreteVD.addFocusListener( this );

		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );

		setImprimir( true );
	}

	private void usaIcmsFreteVenda() {

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT ADICFRETEBASEICM FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

			ResultSet rs = ps.executeQuery();
			boolean adicionaIcmsVenda = false;

			if ( rs.next() ) {
				adicionaIcmsVenda = rs.getString( "ADICFRETEBASEICM" ).trim().equals( "S" );
			}

			rs.close();
			ps.close();

			con.commit();

			panelIcms.setVisible( adicionaIcmsVenda );

		} catch ( SQLException err ) {
			err.printStackTrace();
		}
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		ImprimeOS imp = new ImprimeOS( "", con );
		String[] sValores;
		String sWhere = "";

		String sAnd = " AND ";
		int linPag = imp.verifLinPag() - 1;

		DLRFrete dl = new DLRFrete();
		dl.setVisible( true );

		if ( dl.OK == false ) {
			dl.dispose();
			return;
		}

		sValores = dl.getValores();
		dl.dispose();

		if ( sValores[ 1 ].trim().length() > 0 ) {
			sWhere = sWhere + sAnd + "VD.DtEmitVenda >= '" + Funcoes.strDateToStrDB( sValores[ 1 ] ) + "'";
			sAnd = " AND ";
		}
		if ( sValores[ 2 ].trim().length() > 0 ) {
			sWhere = sWhere + sAnd + "VD.DtEmitVenda <= '" + Funcoes.strDateToStrDB( sValores[ 2 ] ) + "'";
			sAnd = " AND ";
		}
		String sDataini = sValores[ 1 ];
		String sDatafim = sValores[ 2 ];

		try {

			StringBuilder sql = new StringBuilder();
			sql.append( "SELECT F.CODVENDA,F.TIPOFRETEVD,F.PLACAFRETEVD,F.UFFRETEVD,F.VLRFRETEVD,F.QTDFRETEVD," );
			sql.append( "F.PESOLIQVD,F.PESOBRUTVD,F.ESPFRETEVD,F.MARCAFRETEVD, T.CODTRAN,T.NOMETRAN," );
			sql.append( "VD.DOCVENDA,F.PERCVENDAFRETEVD,F.CONHECFRETEVD,VD.DTEMITVENDA FROM VDVENDA VD, VDTRANSP T,VDFRETEVD F  WHERE T.CODTRAN=F.CODTRAN  " );
			sql.append( "AND T.CODEMP=F.CODEMPTN AND T.CODFILIAL=F.CODFILIALTN AND F.CODVENDA=VD.CODVENDA AND " );
			sql.append( "VD.CODEMP=F.CODEMP AND VD.CODFILIAL=F.CODFILIAL" );
			sql.append( sWhere );
			sql.append( " ORDER BY " + sValores[ 0 ] + ",VD.DTEMITVENDA" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ResultSet rs = ps.executeQuery();

			imp.limpaPags();
			imp.montaCab();
			imp.setTitulo( "Relatório de Lancamentos de Fretes" );
			imp.addSubTitulo( "RELATÓRIO DE LANÇAMENTO DE FRETES   -   PERIODO DE :" + sDataini + " Até: " + sDatafim );

			String linha = StringFunctions.replicate( "-", 133 );

			while ( rs.next() ) {

				if ( imp.pRow() == 0 ) {

					imp.impCab( 136, true );

					imp.say( 0, imp.comprimido() );
					imp.say( 0, "|" + linha + "|" );

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" );
					imp.say( 2, "Tipo frete  " );
					imp.say( 14, "Pedido" );
					imp.say( 23, "Doc.vd." );
					imp.say( 34, "Cod.tran." );
					imp.say( 54, "Nome transportadora" );
					imp.say( 92, "Placa" );
					imp.say( 102, "UF" );
					imp.say( 135, "|" );

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" );
					imp.say( 2, "Conhec." );
					imp.say( 14, "Qtd." );
					imp.say( 27, "Valor." );
					imp.say( 38, "% Ft.vd." );
					imp.say( 51, "Especie." );
					imp.say( 64, "Marca." );
					imp.say( 79, "P.bruto :" );
					imp.say( 95, "P.liq.:" );
					imp.say( 110, "Dt.emit.vd" );
					imp.say( 135, "|" );

				}

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" + linha + "|" );

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" );
				imp.say( 2, rs.getString( "TipoFreteVd" ).equals( "C" ) ? "CIF" : "FOB" );
				imp.say( 14, rs.getString( "CodVenda" ) );
				imp.say( 23, rs.getString( "DocVenda" ) );
				imp.say( 34, rs.getString( "CodTran" ) );
				imp.say( 54, Funcoes.copy( rs.getString( "NomeTran" ), 20 ) );
				imp.say( 92, Funcoes.setMascara( rs.getString( "PlacaFreteVd" ), "###-####" ) );
				imp.say( 102, rs.getString( "UFFRETEVD" ) );
				imp.say( 135, "|" );

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" );
				imp.say( 2, Funcoes.copy( rs.getString( "ConhecFreteVd" ), 8 ) );
				imp.say( 14, String.valueOf( rs.getDouble( "QtdFreteVd" ) ) );
				imp.say( 25, Funcoes.strDecimalToStrCurrency( 9, 2, "" + rs.getString( "VlrFreteVd" ) ) );
				imp.say( 38, String.valueOf( rs.getDouble( "PercVendaFreteVd" ) ) );
				imp.say( 51, Funcoes.copy( rs.getString( "EspFreteVd" ), 10 ) );
				imp.say( 64, Funcoes.copy( rs.getString( "MarcaFreteVd" ), 10 ) );
				imp.say( 79, Funcoes.strDecimalToStrCurrency( 9, Aplicativo.casasDec, rs.getString( "PesoBrutVd" ) ) );
				imp.say( 95, Funcoes.strDecimalToStrCurrency( 9, Aplicativo.casasDec, rs.getString( "PesoLiqVd" ) ) );
				imp.say( 110, Funcoes.dateToStrDate( rs.getDate( "DTEMITVENDA" ) ) );
				imp.say( 135, "|" );

				if ( imp.pRow() >= linPag ) {
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "+" + linha + "+" );
					imp.incPags();
					imp.eject();
				}
			}
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "+" + linha + "+" );

			imp.eject();
			imp.fechaGravacao();

			rs.close();
			ps.close();
			con.commit();

			dl.dispose();

		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao consultar a tabela de lancameto de fretes!\n" + err.getMessage(), true, con, err );
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
	}

	private void calcPerc() {

		if ( txtVlrLiqVenda.getVlrDouble().doubleValue() > 0 ) {
			txtPercVendaFreteVD.setVlrBigDecimal( txtVlrFreteVD.getVlrBigDecimal().divide( txtVlrLiqVenda.getVlrBigDecimal(), 2, BigDecimal.ROUND_HALF_UP ).multiply( new BigDecimal( 100 ) ) );
		}
	}

	private void calculaIcmsFrete() {

		/*
		 * if ( "S".equals( cbAdicICMSFrete.getVlrString() ) ) { BigDecimal icms = txtVlrFreteVD.getVlrBigDecimal().divide( new BigDecimal( "100.00" ) ).multiply( txtPercIcmsFreteVD.getVlrBigDecimal() ); txtVlrIcmsFreteVD.setVlrBigDecimal( icms ); }
		 */
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
		else if ( evt.getSource() == btImp ) {
			imprimir( TYPE_PRINT.PRINT);
		}

		super.actionPerformed( evt );
	}

	public void keyPressed( KeyEvent e ) {

		/*
		 * if ( e.getKeyCode() == KeyEvent.VK_ENTER && e.getSource() == txtPercIcmsFreteVD && txtVlrFreteVD.getVlrBigDecimal() != null && txtVlrFreteVD.getVlrBigDecimal().floatValue() > 0 ) { calculaIcmsFrete(); } else { super.keyPressed( e ); }
		 */

		super.keyPressed( e );
	}

	public void afterInsert( InsertEvent ievt ) {

		txtPlacaFreteVD.setVlrString( "*******" );
		txtUFFreteVD.setVlrString( "**" );
		txtEspFreteVD.setVlrString( "Volume" );
		txtMarcaFreteVD.setVlrString( "**********" );
		txtPesoBrutVD.setVlrBigDecimal( new BigDecimal( "1" ) );
		txtPesoLiqVD.setVlrBigDecimal( new BigDecimal( "1" ) );
	}

	public void beforeInsert( InsertEvent ievt ) {

	}

	public void focusLost( FocusEvent fevt ) {

		if ( fevt.getSource() == txtVlrFreteVD ) {
			calcPerc();
			calculaIcmsFrete();
		}
	}

	public void focusGained( FocusEvent e ) {

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcTran.setConexao( cn );
		lcVenda.setConexao( cn );
		usaIcmsFreteVenda();
	}
}
