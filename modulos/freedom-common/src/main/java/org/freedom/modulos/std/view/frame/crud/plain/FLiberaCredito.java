/**
 * @version 11/02/2002 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FLiberaCredito.java <BR>
 * 
 *                         Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                         modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                         na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                         Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                         sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                         Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                         Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                         de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                         Formulário de liberação de crédito por pedido de venda.
 */

package org.freedom.modulos.std.view.frame.crud.plain;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDados;
import org.freedom.modulos.std.view.dialog.utility.DLConsultaObs;
import org.freedom.modulos.std.view.dialog.utility.DLConsultaVenda;
import org.freedom.modulos.std.view.frame.crud.detail.FVenda;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCliente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.JScrollPane;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.DeleteEvent;
import org.freedom.acao.DeleteListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.acao.RadioGroupEvent;
import org.freedom.acao.RadioGroupListener;

public class FLiberaCredito extends FDados implements RadioGroupListener, ActionListener, InsertListener, DeleteListener, CarregaListener, PostListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodLib = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodPesq = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtDtVencLCred = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodVenda = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtCodTpCred = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldFK txtCodTpCredP = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldFK txtDescTpCred = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtDescTpCredP = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtVlrLiqPed = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldFK txtVlrVendaCred = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldFK txtVlrTpCred = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldFK txtVlrTpCredP = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldFK txtVlrAberto = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldFK txtVlrLiberacao = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldFK txtVlrCredito = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtVlrALiberar = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	// private JTextFieldPad txtVlrALiberar2 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldFK txtVlrSaldo = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private ListaCampos lcCli = new ListaCampos( this, "CL" );

	private ListaCampos lcCliP = new ListaCampos( this, "CL" );

	private ListaCampos lcTipoCred = new ListaCampos( this, "TR" );

	private ListaCampos lcTipoCredP = new ListaCampos( this, "TR" );

	private JPanelPad pinCab = new JPanelPad( 0, 200 );

	private JPanelPad pinCli = new JPanelPad( new BorderLayout() );

	private JPanelPad pnTotais = new JPanelPad( 180, 200 );

	private JTablePad tab = new JTablePad();

	private JScrollPane spnTab = new JScrollPane( tab );

	private ListaCampos lcVenda = new ListaCampos( this, "VD" );

	public JTextFieldFK txtDocVenda = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 9, 0 );

	private JTextFieldFK txtDataVenda = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtTipoVenda = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JRadioGroup<?, ?> rgTipo = null;

	private Vector<String> vLabs1 = new Vector<String>();

	private Vector<String> vVals1 = new Vector<String>();

	private JLabelPad lbCredP = new JLabelPad( "Crédito pré-aprovado cliente principal" );

	public FLiberaCredito() {

		super();
		setTitulo( "Liberação de crédito" );
		setAtribos( 10, 10, 680, 480 );

		vLabs1.addElement( "Venda" );
		vLabs1.addElement( "ECF" );

		vVals1.addElement( "V" );
		vVals1.addElement( "E" );

		rgTipo = new JRadioGroup<String, String>( 1, 2, vLabs1, vVals1 );
		rgTipo.setVlrString( "V" );
		txtTipoVenda.setVlrString( "V" );

		rgTipo.addRadioGroupListener( this );

		// Mecanismo de busca e validação de clientes
		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, true ) );
		lcCli.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtCodTpCred, "CodTpCred", "Cód.tp.créd.", ListaCampos.DB_FK, txtDescTpCred, false ) );
		lcCli.add( new GuardaCampo( txtCodPesq, "CodPesq", "Cód.cli.p.", ListaCampos.DB_SI, false ) );
		lcCli.montaSql( false, "CLIENTE", "VD" );
		lcCli.setQueryCommit( false );
		lcCli.setReadOnly( true );
		txtCodCli.setTabelaExterna( lcCli, FCliente.class.getCanonicalName() );

		// Mecanismo de busca de cliente principal
		lcCliP.add( new GuardaCampo( txtCodPesq, "CodCli", "Cód.cli.", ListaCampos.DB_PK, true ) );
		lcCliP.add( new GuardaCampo( txtCodTpCredP, "CodTpCred", "Cód.tp.créd.", ListaCampos.DB_FK, txtDescTpCredP, false ) );
		lcCliP.montaSql( false, "CLIENTE", "VD" );
		lcCliP.setQueryCommit( false );
		lcCliP.setReadOnly( true );
		txtCodPesq.setTabelaExterna( lcCliP, FCliente.class.getCanonicalName() );

		// Mecanismo de busca de informações de tipo de crédito
		lcTipoCred.add( new GuardaCampo( txtCodTpCred, "CodTpCred", "Cód.tp.créd", ListaCampos.DB_PK, false ) );
		lcTipoCred.add( new GuardaCampo( txtDescTpCred, "DescTpCred", "Descrição do tipo de credito", ListaCampos.DB_SI, false ) );
		lcTipoCred.add( new GuardaCampo( txtVlrTpCred, "VlrTpCred", "Valor", ListaCampos.DB_SI, false ) );
		lcTipoCred.montaSql( false, "TIPOCRED", "FN" );
		lcTipoCred.setQueryCommit( false );
		lcTipoCred.setReadOnly( true );
		txtCodTpCred.setTabelaExterna( lcTipoCred, FTipoCred.class.getCanonicalName() );
		txtCodTpCred.setNomeCampo( "CodTpCred" );

		// Mecanismo de busca de informações de tipo de crédito do cliente principal
		lcTipoCredP.add( new GuardaCampo( txtCodTpCredP, "CodTpCred", "Cód.tp.créd", ListaCampos.DB_PK, false ) );
		lcTipoCredP.add( new GuardaCampo( txtDescTpCredP, "DescTpCred", "Descrição do tipo de credito", ListaCampos.DB_SI, false ) );
		lcTipoCredP.add( new GuardaCampo( txtVlrTpCredP, "VlrTpCred", "Valor", ListaCampos.DB_SI, false ) );
		lcTipoCredP.montaSql( false, "TIPOCRED", "FN" );
		lcTipoCredP.setQueryCommit( false );
		lcTipoCredP.setReadOnly( true );
		txtCodTpCredP.setTabelaExterna( lcTipoCredP, FTipoCred.class.getCanonicalName() );
		txtCodTpCredP.setNomeCampo( "CodTpCred" );

		lcVenda.add( new GuardaCampo( txtCodVenda, "CodVenda", "Cód.venda", ListaCampos.DB_PK, true ) );
		lcVenda.add( new GuardaCampo( txtDocVenda, "DocVenda", "Doc.", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtDataVenda, "DtEmitVenda", "Data", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_FK, true ) );
		// lcVenda.add( new GuardaCampo( txtTipoVenda, "TipoVenda", "Tipo Venda", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtVlrLiqPed, "VlrLiqVenda", "Valor do pedido", ListaCampos.DB_SI, false ) );

		lcVenda.setDinWhereAdic( "TIPOVENDA = #S", txtTipoVenda );

		lcVenda.setReadOnly( true );
		lcVenda.montaSql( false, "VENDA", "VD" );
		txtCodVenda.setTabelaExterna( lcVenda, FVenda.class.getCanonicalName() );
		txtCodVenda.setFK( true );
		txtCodVenda.setNomeCampo( "CodVenda" );

		// Adicionando elementos no painel superior da tela (Cabeçalho)
		setPainel( pinCab, pnCliente );

		adicDB( rgTipo, 7, 5, 300, 30, "TipoVenda", "", true );

		adicCampo( txtCodLib, 7, 60, 40, 20, "CodLCred", "Nº.lib.", ListaCampos.DB_PK, true );
		adicCampo( txtCodVenda, 50, 60, 70, 20, "CodVenda", "Pedido", ListaCampos.DB_FK, true );
		adicCampo( txtCodCli, 123, 60, 60, 20, "CodCli", "Cód.cli.", ListaCampos.DB_FK, true );
		adicDescFK( txtRazCli, 186, 60, 290, 20, "RazCli", "Razão social do cliente" );

		setPainel( pnTotais );
		adicCampo( txtVlrVendaCred, 7, 130, 80, 20, "VlrVendaCred", "Vlr.Anterior", ListaCampos.DB_SI, false );
		setPainel( pinCab, pnCliente );

		adic( new JLabelPad( "Crédito pré-aprovado sub-cliente" ), 7, 80, 300, 20 );
		adic( txtCodTpCred, 113, 100, 60, 20 );
		adicDescFK( txtDescTpCred, 176, 100, 300, 20, "DescTpCred", "" );
		adicDescFK( txtVlrTpCred, 7, 100, 103, 20, "VlrTpCred", "" );
		adic( new JLabelPad( "Duplicatas em aberto" ), 7, 173, 200, 20 );

		adic( lbCredP, 7, 120, 300, 20 );
		adic( txtCodTpCredP, 113, 140, 60, 20 );
		adicDescFK( txtDescTpCredP, 176, 140, 300, 20, "DescTpCred", "" );
		adicDescFK( txtVlrTpCredP, 7, 140, 103, 20, "VlrTpCred", "" );

		setPainel( pnTotais );
		adicCampo( txtVlrALiberar, 7, 170, 100, 20, "VlrAutorizLCred", "Valor a liberar", ListaCampos.DB_SI, true );

		// adicCampoInvisivel( txtVlrALiberar2, "VlrAutorizLCred", "Valor a liberar", ListaCampos.DB_SI, true );
		// adicCampoInvisivel( txtDtVencLCred, "DtaVctoLCred", "Vencimento", ListaCampos.DB_SI, true );

		setListaCampos( true, "LIBCRED", "FN" );

		pnTotais.adic( new JLabelPad( "Informações para análise" ), 7, 0, 200, 20 );
		pnTotais.adic( new JLabelPad( "Valor do crédito:" ), 7, 30, 120, 20 );
		pnTotais.adic( txtVlrCredito, 7, 50, 100, 20 );
		pnTotais.adic( new JLabelPad( "Valor em aberto:" ), 7, 70, 120, 20 );
		pnTotais.adic( txtVlrAberto, 7, 90, 100, 20 );
		pnTotais.adic( new JLabelPad( "Vlr.ped.atual:" ), 90, 110, 80, 20 );

		pnTotais.adic( txtVlrLiqPed, 90, 130, 80, 20 );

		// pnTotais.adic( new JLabelPad( "Valor a liberar:" ), 7, 150, 120, 20 );
		// pnTotais.adic( txtVlrALiberar, 7, 170, 100, 20 );

		pinCli.add( spnTab, BorderLayout.CENTER );
		// spnTab.add( pinCli, BorderLayout.CENTER );

		// Adicionando elementos no painel inferior da tela (Rodapé)

		pnCliente.add( pinCab, BorderLayout.NORTH );
		pnCliente.add( pinCli, BorderLayout.CENTER );
		pinCli.add( pnTotais, BorderLayout.EAST );

		tab.adicColuna( "Cod.cli." );
		tab.adicColuna( "Vencto." );
		tab.adicColuna( "Valor" );
		tab.adicColuna( "Série" );
		tab.adicColuna( "Doc" );
		tab.adicColuna( "Cód.venda" );
		tab.adicColuna( "Dt.venda." );
		tab.adicColuna( "Dt.pagto." );
		tab.adicColuna( "Valor pago." );
		tab.adicColuna( "Atraso" );
		tab.adicColuna( "Observações" );
		tab.adicColuna( "Banco" );
		tab.adicColuna( "TV" );

		tab.setTamColuna( 60, 0 );
		tab.setTamColuna( 90, 1 );
		tab.setTamColuna( 100, 2 );
		tab.setTamColuna( 50, 3 );
		tab.setTamColuna( 80, 4 );
		tab.setTamColuna( 90, 5 );
		tab.setTamColuna( 90, 6 );
		tab.setTamColuna( 90, 7 );
		tab.setTamColuna( 100, 8 );
		tab.setTamColuna( 60, 9 );
		tab.setTamColuna( 200, 10 );
		tab.setTamColuna( 200, 11 );
		tab.setTamColuna( 20, 12 );

		lcCampos.addInsertListener( this );
		lcCampos.addDeleteListener( this );
		lcCampos.addCarregaListener( this );
		lcCli.addCarregaListener( this );
		lcCliP.addCarregaListener( this );
		lcVenda.addCarregaListener( this );

		tab.addMouseListener( new MouseAdapter() {

			public void mouseClicked( MouseEvent mevt ) {

				if ( mevt.getSource() == tab ) {
					if ( mevt.getClickCount() == 2 ) {
						int iLin = tab.getLinhaSel();
						if ( iLin >= 0 ) {
							int iCodVenda = Integer.parseInt( (String) tab.getValor( iLin, 4 ) );
							String sTipoVenda = (String) tab.getValor( iLin, 11 );
							DLConsultaVenda dl = new DLConsultaVenda( FLiberaCredito.this, con, iCodVenda, sTipoVenda );
							dl.setVisible( true );
							dl.dispose();
						}
					}
				}
			}
		} );

	}

	private void carregaGridConsulta() {

		StringBuffer sql = new StringBuffer();
		tab.limpa();
		sql.append( "SELECT IT.DTVENCITREC,IT.VLRPARCITREC,V.SERIE,R.DOCREC,V.CODVENDA," );
		sql.append( "R.DATAREC,IT.DTPAGOITREC,IT.VLRPAGOITREC," );
		sql.append( "(CAST('today' AS DATE)-IT.DTVENCITREC) AS ATRASO," );
		sql.append( "R.OBSREC," );
		sql.append( "(SELECT B.NOMEBANCO FROM FNBANCO B WHERE B.CODBANCO = R.CODBANCO) AS NOMEBANCO," );
		sql.append( "R.CODREC,IT.NPARCITREC,R.TIPOVENDA,CL.CODCLI " );
		sql.append( "FROM FNRECEBER R, VDVENDA V, " );
		sql.append( "FNITRECEBER IT, VDCLIENTE CL WHERE " );
		sql.append( "((CL.CODCLI=? AND CL.CODEMP=? AND CL.CODFILIAL=?) OR " );
		sql.append( "(CL.CODPESQ=? AND CL.CODEMPPQ=? AND CL.CODFILIALPQ=?)) AND " );
		sql.append( "R.CODCLI=CL.CODCLI AND R.CODEMPCL=CL.CODEMP AND R.CODFILIALCL=CL.CODFILIAL AND " );
		sql.append( "V.CODVENDA=R.CODVENDA AND " );
		sql.append( "IT.STATUSITREC NOT IN ('RP') AND IT.CODREC = R.CODREC " );
		sql.append( "AND (V.CODEMP!=? OR V.CODFILIAL!=? OR V.TIPOVENDA!=? OR V.CODVENDA!=?) " );
		sql.append( "ORDER BY IT.DTVENCITREC,R.CODREC,IT.NPARCITREC" );

		try {
			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, txtCodCli.getVlrInteger().intValue() );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, lcCli.getCodFilial() );
			ps.setInt( 4, txtCodPesq.getVlrInteger().intValue() );
			ps.setInt( 5, Aplicativo.iCodEmp );
			ps.setInt( 6, lcCliP.getCodFilial() );
			ps.setInt( 7, Aplicativo.iCodEmp );
			ps.setInt( 8, lcVenda.getCodFilial() );
			ps.setString( 9, txtTipoVenda.getVlrString() );
			ps.setInt( 10, txtCodVenda.getVlrInteger() );

			System.out.println( "SQL:" + sql.toString() );

			ResultSet rs = ps.executeQuery();
			double dVal = 0;
			for ( int i = 0; rs.next(); i++ ) {
				tab.adicLinha();
				tab.setValor( rs.getString( "CODCLI" ), i, 0 );
				tab.setValor( ( rs.getDate( "DtVencItRec" ) != null ? StringFunctions.sqlDateToStrDate( rs.getDate( "DtVencItRec" ) ) : "" ), i, 1 );
				tab.setValor( Funcoes.strDecimalToStrCurrency( 15, 2, rs.getString( "VlrParcItRec" ) ), i, 2 );
				tab.setValor( ( rs.getString( "Serie" ) != null ? rs.getString( "Serie" ) : "" ), i, 3 );
				tab.setValor( ( rs.getString( "DocRec" ) != null ? rs.getString( "DocRec" ) : "" ), i, 4 );
				tab.setValor( String.valueOf( rs.getInt( "CodVenda" ) ), i, 5 );
				tab.setValor( ( rs.getDate( "DataRec" ) != null ? StringFunctions.sqlDateToStrDate( rs.getDate( "DataRec" ) ) : "" ), i, 6 );
				tab.setValor( ( rs.getDate( "DtPagoItRec" ) != null ? StringFunctions.sqlDateToStrDate( rs.getDate( "DtPagoItRec" ) ) : "" ), i, 7 );
				tab.setValor( Funcoes.strDecimalToStrCurrency( 15, 2, rs.getString( "VlrPagoItRec" ) ), i, 8 );
				tab.setValor( rs.getString( 9 ), i, 9 );
				tab.setValor( rs.getString( "ObsRec" ) != null ? rs.getString( "ObsRec" ) : "", i, 10 );
				tab.setValor( rs.getString( 11 ) != null ? rs.getString( 11 ) : "", i, 11 );
				tab.setValor( rs.getString( "TIPOVENDA" ), i, 12 );
				dVal += rs.getDouble( "VlrParcItRec" );
			}
			txtVlrAberto.setVlrString( Funcoes.strDecimalToStrCurrency( 2, "" + ( new BigDecimal( dVal ) ).setScale( 2, BigDecimal.ROUND_HALF_UP ) ) );
			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao montar a tabela de consulta!\n" + err.getMessage(), true, con, err );
		}
	}

	private void buscaVlrLib() {

		String sSQL = "SELECT SUM(VLRAUTORIZLCRED) FROM FNLIBCRED WHERE CODEMP=?" + " AND CODFILIAL=? AND CODCLI=?" + " AND CODEMPCL=? AND CODFILIALCL=?" + " AND DTAVCTOLCRED >= CAST('today' AS DATE)";
		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNLIBCRED" ) );
			ps.setInt( 3, txtCodCli.getVlrInteger().intValue() );
			ps.setInt( 4, Aplicativo.iCodEmp );
			ps.setInt( 5, lcCli.getCodFilial() );
			ResultSet rs = ps.executeQuery();
			if ( rs.next() && rs.getString( 1 ) != null ) {
				txtVlrLiberacao.setVlrString( Funcoes.strDecimalToStrCurrency( 2, rs.getString( 1 ) ) );
			}
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar valor liberado!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	private void calculaLiberacao() {

		BigDecimal vlrtpcred = new BigDecimal( 0 );
		BigDecimal vlrdeb = new BigDecimal( 0 );

		try {

			limpa();
			carregaGridConsulta();

			vlrtpcred = vlrtpcred.add( txtVlrTpCred.getVlrBigDecimal() );
			vlrdeb = txtVlrAberto.getVlrBigDecimal().add( txtVlrLiqPed.getVlrBigDecimal() );

			if ( !txtCodCli.getVlrInteger().equals( txtCodPesq.getVlrInteger() ) ) {
				vlrtpcred = vlrtpcred.add( txtVlrTpCredP.getVlrBigDecimal() );
				lbCredP.setVisible( true );
				txtCodTpCredP.setVisible( true );
				txtDescTpCredP.setVisible( true );
				txtVlrTpCredP.setVisible( true );

			}
			else {
				lbCredP.setVisible( false );
				txtCodTpCredP.setVisible( false );
				txtDescTpCredP.setVisible( false );
				txtVlrTpCredP.setVisible( false );
			}

			txtVlrCredito.setVlrString( Funcoes.strDecimalToStrCurrency( 2, String.valueOf( vlrtpcred ) ) );

			// if ( txtVlrALiberar.getVlrBigDecimal().compareTo( new BigDecimal( 0 ) ) == 0 ) {
			txtVlrALiberar.setVlrBigDecimal( vlrtpcred.subtract( vlrdeb ).multiply( new BigDecimal( -1 ) ) );
			// }

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private void limpa() {

		// txtVlrAberto.setVlrString(Funcoes.strDecimalToStrCurrency(2,"0.00"));
		// txtVlrLiberacao.setVlrString(Funcoes.strDecimalToStrCurrency(2,"0.00"));
		// txtVlrCredito.setVlrString(Funcoes.strDecimalToStrCurrency(2,"0.00"));
		// txtVlrALiberar.setVlrString(Funcoes.strDecimalToStrCurrency(2,"0.00"));
		// txtVlrSaldo.setVlrString(Funcoes.strDecimalToStrCurrency(2,"0.00"));
		tab.limpa();
	}

	private boolean buscaObs() {

		boolean continua = true;

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT CODCLI,RAZCLI,OBSCLI FROM VDCLIENTE " + "WHERE OBSCLI IS NOT NULL AND " + "((CODCLI=? AND CODEMP=? AND CODFILIAL=?) OR " + "(CODPESQ=? AND CODEMPPQ=? AND CODFILIALPQ=?))" );
			ps.setInt( 1, txtCodCli.getVlrInteger().intValue() );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, lcCli.getCodFilial() );
			ps.setInt( 4, txtCodCli.getVlrInteger().intValue() );
			ps.setInt( 5, Aplicativo.iCodEmp );
			ps.setInt( 6, lcCli.getCodFilial() );
			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				DLConsultaObs dl = new DLConsultaObs( this, rs, con );
				dl.setVisible( true );
				if ( !dl.OK ) {
					continua = false;
				}
				dl.dispose();
			}
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao verificar observações!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
		return continua;
	}

	public void open( String tipovenda, int pedido, int cliente, int item, BigDecimal valorItem ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			BigDecimal vlrItem = valorItem;

			if ( valorItem.compareTo( new BigDecimal( 0 ) ) > 0 ) {

				ps = con.prepareStatement( "SELECT I.VLRLIQITVENDA FROM VDITVENDA I " + "WHERE I.CODEMP=? AND I.CODFILIAL=? AND " + "I.TIPOVENDA=? AND I.CODVENDA=? AND I.CODITVENDA=?" );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "FNLIBCRED" ) );
				ps.setString( 3, tipovenda );
				ps.setInt( 4, pedido );
				ps.setInt( 5, item );
				rs = ps.executeQuery();

				if ( rs.next() ) {
					vlrItem = valorItem.subtract( rs.getBigDecimal( "VLRLIQITVENDA" ) );
				}

				rs.close();
				ps.close();

			}

			ps = con.prepareStatement( "SELECT L.CODLCRED FROM FNLIBCRED L " + "WHERE L.CODEMP=? AND L.CODFILIAL=? AND " + "L.TIPOVENDA=? AND L.CODVENDA=? AND L.CODCLI=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNLIBCRED" ) );
			ps.setString( 3, tipovenda );
			ps.setInt( 4, pedido );
			ps.setInt( 5, cliente );
			rs = ps.executeQuery();

			lcVenda.carregaDados();

			if ( rs.next() ) {
				rgTipo.setVlrString( tipovenda );
				txtCodLib.setVlrInteger( rs.getInt( "CODLCRED" ) );
				txtCodVenda.setVlrInteger( pedido );
				txtCodCli.setVlrInteger( cliente );
				lcCampos.carregaDados();
				txtVlrLiqPed.setVlrBigDecimal( txtVlrLiqPed.getVlrBigDecimal().add( vlrItem ) );
				txtVlrALiberar.setVlrBigDecimal( new BigDecimal( "0.00" ) );
				calculaLiberacao();
				lcCampos.edit();
			}
			else {
				lcCampos.insert( true );
				rgTipo.setVlrString( tipovenda );
				txtCodVenda.setVlrInteger( pedido );
				lcVenda.carregaDados();
				txtVlrLiqPed.setVlrBigDecimal( txtVlrLiqPed.getVlrBigDecimal().add( vlrItem ) );
				txtVlrALiberar.setVlrBigDecimal( new BigDecimal( "0.00" ) );
				calculaLiberacao();
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
		}
	}

	public void beforeCarrega( CarregaEvent cevt ) {

	}

	public void afterCarrega( CarregaEvent cevt ) {

		if ( cevt.getListaCampos() == lcCli ) {
			lcCliP.carregaDados();

		}
		else if ( cevt.getListaCampos() == lcVenda ) {
			calculaLiberacao();
		}
	}

	public void beforeInsert( InsertEvent ievt ) {

		txtVlrVendaCred.setVlrBigDecimal( txtVlrLiqPed.getVlrBigDecimal() );
	}

	public void afterInsert( InsertEvent ievt ) {

		if ( ievt.getListaCampos() == lcCampos ) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.set( Calendar.DATE, cal.get( Calendar.DATE ) + 7 ); // Coloca o vencimento padrao de 7 dias.
			txtDtVencLCred.setVlrDate( cal.getTime() );
			limpa();
		}
	}

	public void beforeDelete( DeleteEvent devt ) {

	}

	public void afterDelete( DeleteEvent devt ) {

		if ( devt.ok ) {
			limpa();
		}
	}

	public void beforePost( PostEvent pevt ) {

		boolean continua = true;

		try {

			continua = txtVlrALiberar.getVlrBigDecimal().compareTo( new BigDecimal( 0 ) ) > 0;

			if ( !continua ) {
				Funcoes.mensagemInforma( this, "O Pedido não necessita de liberação!\n Valor dentro do limite pré-estabelecido! " );
				pevt.cancela();
				return;
			}

			txtVlrVendaCred.setVlrBigDecimal( txtVlrLiqPed.getVlrBigDecimal() );
			continua = buscaObs();

			if ( !continua ) {
				pevt.cancela();
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	public void afterPost( PostEvent pevt ) {

	}

	public void valorAlterado( RadioGroupEvent evt ) {

		if ( evt.getSource() == rgTipo ) {
			txtTipoVenda.setVlrString( rgTipo.getVlrString() );
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcCli.setConexao( cn );
		lcCliP.setConexao( cn );
		lcTipoCred.setConexao( cn );
		lcTipoCredP.setConexao( cn );
		lcVenda.setConexao( cn );
	}
}
