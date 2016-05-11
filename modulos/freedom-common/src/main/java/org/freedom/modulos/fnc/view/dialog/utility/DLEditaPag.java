/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)DLEditaPag.java <BR>
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
 */

package org.freedom.modulos.fnc.view.dialog.utility;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.infra.functions.ConversionFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.crm.view.frame.crud.detail.FContrato;
import org.freedom.modulos.fnc.business.object.Cheque;
import org.freedom.modulos.fnc.library.swing.component.JTextFieldPlan;
import org.freedom.modulos.fnc.view.frame.crud.detail.FCheque;

public class DLEditaPag extends FFDialogo implements CarregaListener {

	private static final long serialVersionUID = 1L;

	private final JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldPad txtRazFor = new JTextFieldPad( JTextFieldPad.TP_STRING, 60, 0 );

	private final JTextFieldPad txtCodConta = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldPad txtDescConta = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPlan txtCodPlan = new JTextFieldPlan( JTextFieldPad.TP_STRING, 13, 0 );

	private final JTextFieldPad txtDescPlan = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCodCC = new JTextFieldPad( JTextFieldPad.TP_STRING, 19, 0 );

	private final JTextFieldPad txtAnoCC = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );

	private final JTextFieldFK txtSiglaCC = new JTextFieldFK( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldFK txtDescCC = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtDoc = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldPad txtDtEmis = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtDtVenc = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtVlrParc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtVlrJuros = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtVlrDesc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtVlrDev = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private final JTextFieldPad txtVlrAdic = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtObs = new JTextFieldPad( JTextFieldPad.TP_STRING, 250, 0 );

	private final JTextFieldPad txtCodTipoCob = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldFK txtDescTipoCob = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodcontr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 30, 0 );

	private JTextFieldPad txtCoditcontr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDesccontr = new JTextFieldFK( JTextFieldPad.TP_STRING, 80, 0 );

	private JTextFieldFK txtDescitcontr = new JTextFieldFK( JTextFieldPad.TP_STRING, 80, 0 );
	
	private final ListaCampos lcConta = new ListaCampos( this );

	private final ListaCampos lcPlan = new ListaCampos( this );

	private final ListaCampos lcCC = new ListaCampos( this );

	private final ListaCampos lcTipoCob = new ListaCampos( this, "TC" );

	private ListaCampos lcContrato = new ListaCampos( this, "CT" );

	private ListaCampos lcItContrato = new ListaCampos( this, "CT" );
	
	private JTabbedPanePad tpn = new JTabbedPanePad();

	private JPanelPad pnGeral = new JPanelPad();

	private JPanelPad pnCheques = new JPanelPad( new BorderLayout() );

	private JTablePad tabCheques = new JTablePad();

	private JScrollPane spnCheques = new JScrollPane( tabCheques );

	private JTextFieldPad txtCodPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtNParcPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private boolean lancafincontr = false;

	private enum enum_grid_cheques {
		SEQCHEQ, NUMCHEQ, DTEMITCHEQ, DTVENCTOCHEQ, VLRCHEQ, SITCHEQ
	};

	public enum EDIT_PAG_SETVALORES { CODFOR, RAZFOR, CODCONTA, CODPLAN, CODCC, DOC, DTEMIS, DTVENC, VLRPARC, VLRJUROS, VLRDESC, VLRADIC, OBS, CODTIPOCOB, VLRDEV, CODPAG, NPARCPAG, CODCONTR, CODITCONTR }

	public enum EDIT_PAG_GETVALORES {CODCONTA, CODPLAN, CODCC, DOC, VLRPARC, VLRJUROS, VLRADIC, VLRDESC, DTVENC, OBS, CODTIPOCOB, VLRDEV, CODCONTR, CODITCONTR}

	public DLEditaPag( Component cOrig, boolean edita, boolean lancafincontr ) {

		super( cOrig );

		this.lancafincontr = lancafincontr;
		setTitulo( "Edição de contas a pagar" );
		setAtribos( 368, 545 );

		montaListaCampos();
		montaTela();
		montaGridCheques();

	}

	private void montaListaCampos() {

		lcConta.add( new GuardaCampo( txtCodConta, "NumConta", "Nº Conta", ListaCampos.DB_PK, false ) );
		lcConta.add( new GuardaCampo( txtDescConta, "DescConta", "Descrição da conta", ListaCampos.DB_SI, false ) );
		lcConta.montaSql( false, "CONTA", "FN" );
		lcConta.setReadOnly( true );
		txtCodConta.setTabelaExterna( lcConta, null );
		txtCodConta.setFK( true );
		txtCodConta.setNomeCampo( "NumConta" );

		lcPlan.add( new GuardaCampo( txtCodPlan, "CodPlan", "Cód.plan.", ListaCampos.DB_PK, false ) );
		lcPlan.add( new GuardaCampo( txtDescPlan, "DescPlan", "Descrição do planejamento", ListaCampos.DB_SI, false ) );
		lcPlan.setWhereAdic( "TIPOPLAN = 'D' AND NIVELPLAN = 6" );
		lcPlan.montaSql( false, "PLANEJAMENTO", "FN" );
		lcPlan.setReadOnly( true );
		txtCodPlan.setTabelaExterna( lcPlan, null );
		txtCodPlan.setFK( true );
		txtCodPlan.setNomeCampo( "CodPlan" );

		lcCC.add( new GuardaCampo( txtCodCC, "CodCC", "Cód.c.c.", ListaCampos.DB_PK, false ) );
		lcCC.add( new GuardaCampo( txtSiglaCC, "SiglaCC", "Sigla", ListaCampos.DB_SI, false ) );
		lcCC.add( new GuardaCampo( txtDescCC, "DescCC", "Descrição", ListaCampos.DB_SI, false ) );
		lcCC.add( new GuardaCampo( txtAnoCC, "AnoCC", "Ano-Base", ListaCampos.DB_PK, false ) );
		lcCC.setReadOnly( true );
		lcCC.setQueryCommit( false );
		lcCC.setWhereAdic( "NIVELCC=10" );
		lcCC.montaSql( false, "CC", "FN" );
		txtCodCC.setTabelaExterna( lcCC, null );
		txtCodCC.setFK( true );
		txtCodCC.setNomeCampo( "CodCC" );
		txtAnoCC.setTabelaExterna( lcCC, null );
		txtAnoCC.setFK( true );
		txtAnoCC.setNomeCampo( "AnoCC" );

		lcCC.addCarregaListener( this );

		txtCodTipoCob.setNomeCampo( "CodTipoCob" );
		lcTipoCob.add( new GuardaCampo( txtCodTipoCob, "CodTipoCob", "Cód.tp.cob.", ListaCampos.DB_PK, false ) );
		lcTipoCob.add( new GuardaCampo( txtDescTipoCob, "DescTipoCob", "Descrição do tipo de cobrança.", ListaCampos.DB_SI, false ) );
		lcTipoCob.montaSql( false, "TIPOCOB", "FN" );
		lcTipoCob.setQueryCommit( false );
		lcTipoCob.setReadOnly( true );
		txtCodTipoCob.setTabelaExterna( lcTipoCob, null );
		txtCodTipoCob.setListaCampos( lcTipoCob );
		txtDescTipoCob.setListaCampos( lcTipoCob );
		txtCodTipoCob.setFK( true );

		txtCodcontr.setNomeCampo( "codcontr" );
		lcContrato.add( new GuardaCampo( txtCodcontr, "CodContr", "Cód.Contrato", ListaCampos.DB_PK, true ) );
		lcContrato.add( new GuardaCampo( txtDesccontr, "DescContr", "Desc.Contr.", ListaCampos.DB_SI, false ) );
		lcContrato.montaSql( false, "CONTRATO", "VD" );
		lcContrato.setQueryCommit( false );
		lcContrato.setReadOnly( true );
		txtCodcontr.setTabelaExterna( lcContrato, FContrato.class.getCanonicalName() );
		txtCodcontr.setListaCampos( lcContrato );
		txtDesccontr.setListaCampos( lcContrato );
		txtCodcontr.setFK( true );

		txtCoditcontr.setNomeCampo( "coditcontr" );
		lcItContrato.add( new GuardaCampo( txtCoditcontr, "CodItContr", "Cód.It.Contr.", ListaCampos.DB_PK, true ) );
		lcItContrato.add( new GuardaCampo( txtCodcontr, "CodContr", "Cód.Contrato", ListaCampos.DB_PK, true ) );
		lcItContrato.add( new GuardaCampo( txtDescitcontr, "DescItContr", "Desc.It.Contr.", ListaCampos.DB_SI, false ) );
		lcItContrato.setDinWhereAdic( "CodContr=#N", txtCodcontr );
		txtCodcontr.setPK( true );
		lcItContrato.montaSql( false, "ITCONTRATO", "VD" );
		lcItContrato.setQueryCommit( false );
		lcItContrato.setReadOnly( true );
		txtCoditcontr.setTabelaExterna( lcItContrato, FContrato.class.getCanonicalName() );
		txtCoditcontr.setListaCampos( lcContrato );
		txtDescitcontr.setListaCampos( lcContrato );
		txtCoditcontr.setFK( true );
		
	}

	private void montaTela() {

		txtCodFor.setAtivo( false );
		txtRazFor.setAtivo( false );
		txtDescConta.setAtivo( false );
		txtDescPlan.setAtivo( false );
		txtDtEmis.setAtivo( false );

		Container c = getContentPane();
		c.setLayout( new BorderLayout() );
		c.add( pnBordRodape, BorderLayout.SOUTH );
		c.add( tpn, BorderLayout.CENTER );

		tpn.addTab( "Informações gerais", pnGeral );

		// ABA GERAL

		pnGeral.adic( new JLabelPad( "Cód.for." ), 7, 0, 250, 20 );

		pnGeral.adic( txtCodFor, 7, 20, 80, 20 );
		pnGeral.adic( new JLabelPad( "Razão social do fornecedor" ), 90, 0, 250, 20 );
		pnGeral.adic( txtRazFor, 90, 20, 250, 20 );
		pnGeral.adic( new JLabelPad( "Nº conta" ), 7, 40, 250, 20 );
		pnGeral.adic( txtCodConta, 7, 60, 80, 20 );
		pnGeral.adic( new JLabelPad( "Descrição da conta" ), 90, 40, 250, 20 );
		pnGeral.adic( txtDescConta, 90, 60, 250, 20 );
		pnGeral.adic( new JLabelPad( "Cód.catg." ), 7, 80, 250, 20 );
		pnGeral.adic( txtCodPlan, 7, 100, 100, 20 );
		pnGeral.adic( new JLabelPad( "Descrição da categoria" ), 110, 80, 250, 20 );
		pnGeral.adic( txtDescPlan, 110, 100, 230, 20 );
		pnGeral.adic( new JLabelPad( "Cód.c.c." ), 7, 120, 250, 20 );
		pnGeral.adic( txtCodCC, 7, 140, 100, 20 );
		pnGeral.adic( new JLabelPad( "Descrição do centro de custo" ), 110, 120, 250, 20 );
		pnGeral.adic( txtDescCC, 110, 140, 230, 20 );
		pnGeral.adic( new JLabelPad( "Cod.Tp.Cob" ), 7, 160, 80, 20 );
		pnGeral.adic( txtCodTipoCob, 7, 180, 80, 20 );
		pnGeral.adic( new JLabelPad( "Descrição do tipo de cobrança" ), 90, 160, 250, 20 );
		pnGeral.adic( txtDescTipoCob, 90, 180, 250, 20 );

		pnGeral.adic( new JLabelPad( "Doc." ), 7, 200, 81, 20 );
		pnGeral.adic( txtDoc, 7, 220, 81, 20 );
		pnGeral.adic( new JLabelPad( "Emissão" ), 91, 200, 81, 20 );
		pnGeral.adic( txtDtEmis, 91, 220, 81, 20 );
		pnGeral.adic( new JLabelPad( "Vencimento" ), 175, 200, 81, 20 );
		pnGeral.adic( txtDtVenc, 175, 220, 81, 20 );
		pnGeral.adic( new JLabelPad( "Vlr.parcela" ), 259, 200, 81, 20 );
		pnGeral.adic( txtVlrParc, 259, 220, 81, 20 );

		pnGeral.adic( new JLabelPad( "Vlr.desc." ), 7, 240, 81, 20 );
		pnGeral.adic( txtVlrDesc, 7, 260, 81, 20 );
		pnGeral.adic( new JLabelPad( "Vlr.juros." ), 91, 240, 81, 20 );
		pnGeral.adic( txtVlrJuros, 91, 260, 81, 20 );
		pnGeral.adic( new JLabelPad( "Vlr.devolução" ), 175, 240, 81, 20 );
		pnGeral.adic( txtVlrDev, 175, 260, 81, 20 );
		pnGeral.adic( new JLabelPad( "Vlr.adicional" ), 259, 240, 81, 20 );
		pnGeral.adic( txtVlrAdic, 259, 260, 81, 20 );

		pnGeral.adic( new JLabelPad( "Observações" ), 7, 280, 200, 20 );
		pnGeral.adic( txtObs, 7, 300, 333, 20 );

		pnGeral.adic( new JLabelPad("Cód.contr."), 7, 320, 80, 20 );
		pnGeral.adic( txtCodcontr, 7, 340, 80, 20 );
		pnGeral.adic( new JLabelPad("Descrição do contrato"), 90, 320, 250, 20 );
		pnGeral.adic( txtDesccontr, 90, 340, 250, 20 );
		pnGeral.adic( new JLabelPad("Cód.it.contr."), 7, 360, 80, 20 );
		pnGeral.adic( txtCoditcontr, 7, 380, 80, 20);
		pnGeral.adic( new JLabelPad("Descrição do item de contrato"), 90, 360, 250, 20 );
		pnGeral.adic( txtDescitcontr, 90, 380, 250, 20 );
		
		if ( ! lancafincontr) {
			txtCodcontr.setAtivo( false );
			txtCoditcontr.setAtivo( false );
		}

		// ABA CHEQUES

		//		tpn.addTab( "Cheques", pnCheques );

		pnCheques.add( spnCheques, BorderLayout.CENTER );

	}

	
	public void setValores( Object[] sVals, boolean bLancaUsu ) {

		txtCodFor.setVlrString( (String) sVals[ EDIT_PAG_SETVALORES.CODFOR.ordinal() ] );
		txtRazFor.setVlrString( (String) sVals[ EDIT_PAG_SETVALORES.RAZFOR.ordinal() ] );
		txtCodConta.setVlrString( (String) sVals[ EDIT_PAG_SETVALORES.CODCONTA.ordinal() ] );
		txtCodPlan.setVlrString( (String) sVals[ EDIT_PAG_SETVALORES.CODPLAN.ordinal() ] );
		txtCodCC.setVlrString( (String) sVals[ EDIT_PAG_SETVALORES.CODCC.ordinal() ] );
		txtDoc.setVlrString( (String) sVals[ EDIT_PAG_SETVALORES.DOC.ordinal() ] );
		txtDtEmis.setVlrString( (String) sVals[ EDIT_PAG_SETVALORES.DTEMIS.ordinal() ] );
		txtDtVenc.setVlrString( (String) sVals[ EDIT_PAG_SETVALORES.DTVENC.ordinal() ] );
		txtVlrParc.setVlrString( (String) sVals[ EDIT_PAG_SETVALORES.VLRPARC.ordinal() ] );
		txtVlrJuros.setVlrString( (String) sVals[ EDIT_PAG_SETVALORES.VLRJUROS.ordinal()] );
		txtVlrDesc.setVlrString( (String) sVals[ EDIT_PAG_SETVALORES.VLRDESC.ordinal() ] );
		txtVlrAdic.setVlrString( (String) sVals[ EDIT_PAG_SETVALORES.VLRADIC.ordinal() ] );
		txtObs.setVlrString( (String) sVals[ EDIT_PAG_SETVALORES.OBS.ordinal()] );
		txtCodTipoCob.setVlrString( (String) sVals[ EDIT_PAG_SETVALORES.CODTIPOCOB.ordinal() ] );
		txtVlrDev.setVlrString( (String) sVals[ EDIT_PAG_SETVALORES.VLRDEV.ordinal() ] );
		txtCodPag.setVlrInteger( (Integer) sVals[ EDIT_PAG_SETVALORES.CODPAG.ordinal() ] );
		txtNParcPag.setVlrInteger( (Integer) sVals[ EDIT_PAG_SETVALORES.NPARCPAG.ordinal() ] );
		if (! "".equals( sVals[ EDIT_PAG_SETVALORES.CODCONTR.ordinal() ] ) ) {
			txtCodcontr.setVlrString( (String) sVals[ EDIT_PAG_SETVALORES.CODCONTR.ordinal() ] );
		}
		if (! "".equals( sVals[ EDIT_PAG_SETVALORES.CODITCONTR.ordinal() ] ) ) {
			txtCoditcontr.setVlrString( (String) sVals[ EDIT_PAG_SETVALORES.CODITCONTR.ordinal() ] );
		}
		txtVlrParc.setAtivo( bLancaUsu );
	}

	public String[] getValores() {

		String[] sRetorno = new String[ EDIT_PAG_SETVALORES.values().length ];
		sRetorno[ EDIT_PAG_GETVALORES.CODCONTA.ordinal() ] = txtCodConta.getVlrString();
		sRetorno[ EDIT_PAG_GETVALORES.CODPLAN.ordinal() ] = txtCodPlan.getVlrString();
		sRetorno[ EDIT_PAG_GETVALORES.CODCC.ordinal() ] = txtCodCC.getVlrString();
		sRetorno[ EDIT_PAG_GETVALORES.DOC.ordinal() ] = txtDoc.getVlrString();
		sRetorno[ EDIT_PAG_GETVALORES.VLRPARC.ordinal() ] = txtVlrParc.getVlrString();
		sRetorno[ EDIT_PAG_GETVALORES.VLRJUROS.ordinal() ] = txtVlrJuros.getVlrString();
		sRetorno[ EDIT_PAG_GETVALORES.VLRADIC.ordinal() ] = txtVlrAdic.getVlrString();
		sRetorno[ EDIT_PAG_GETVALORES.VLRDESC.ordinal() ] = txtVlrDesc.getVlrString();
		sRetorno[ EDIT_PAG_GETVALORES.DTVENC.ordinal() ] = txtDtVenc.getVlrString();
		sRetorno[ EDIT_PAG_GETVALORES.OBS.ordinal() ] = txtObs.getVlrString();
		sRetorno[ EDIT_PAG_GETVALORES.CODTIPOCOB.ordinal() ] = txtCodTipoCob.getVlrString();
		sRetorno[ EDIT_PAG_GETVALORES.VLRDEV.ordinal() ] = txtVlrDev.getVlrString();
		sRetorno[ EDIT_PAG_GETVALORES.CODCONTR.ordinal() ] = txtCodcontr.getVlrString();
		sRetorno[ EDIT_PAG_GETVALORES.CODITCONTR.ordinal() ] = txtCoditcontr.getVlrString();
		return sRetorno;
	}

	public void salvaPag() {

		PreparedStatement ps = null;
		StringBuffer sql = new StringBuffer();
		String[] sVals = null;
		String[] sRets = null;
		DLEditaPag dl = null;
		ImageIcon imgStatusAt = null;
		int iLin;
		// ObjetoHistorico historico = null;
		Integer codhistpag = null;
		int param = 1;

		sRets = getValores();

		sql.append( "UPDATE FNITPAGAR SET " );
		sql.append( "NUMCONTA=?,CODEMPCA=?,CODFILIALCA=?,CODPLAN=?,CODEMPPN=? " );
		sql.append( ", CODFILIALPN=?,ANOCC=?,CODCC=?,CODEMPCC=?,CODFILIALCC=? " );
		sql.append( ", DOCLANCAITPAG=?,VLRPARCITPAG=?,VLRJUROSITPAG=?,VLRADICITPAG=? " );
		sql.append( ", VLRDESCITPAG=?,DTVENCITPAG=?,OBSITPAG=? " );
		sql.append( ", CODTIPOCOB=?,CODEMPTC=?,CODFILIALTC=?,VLRDEVITPAG=? , EDITITPAG='S' " );
		sql.append( ", CODCONTR=?, CODITCONTR=?, CODEMPCT=?, CODFILIALCT=? ");
		sql.append( "WHERE CODPAG=? AND NPARCPAG=? AND CODEMP=? AND CODFILIAL=?" );

		try {

			ps = con.prepareStatement( sql.toString() );

			if ( "".equals( sRets[ EDIT_PAG_GETVALORES.CODCONTA.ordinal() ].trim() ) ) {
				ps.setNull( param++, Types.CHAR );
				ps.setNull( param++, Types.INTEGER );
				ps.setNull( param++, Types.INTEGER );
			}
			else {
				ps.setString( param++, sRets[ EDIT_PAG_GETVALORES.CODCONTA.ordinal() ] );
				ps.setInt( param++, Aplicativo.iCodEmp );
				ps.setInt( param++, ListaCampos.getMasterFilial( "FNCONTA" ) );
			}
			if ( "".equals( sRets[ EDIT_PAG_GETVALORES.CODPLAN.ordinal() ].trim() ) ) {
				ps.setNull( param++, Types.CHAR );
				ps.setNull( param++, Types.INTEGER );
				ps.setNull( param++, Types.INTEGER );
			}
			else {
				ps.setString( param++, sRets[ EDIT_PAG_GETVALORES.CODPLAN.ordinal() ] );
				ps.setInt( param++, Aplicativo.iCodEmp );
				ps.setInt( param++, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
			}
			if ( "".equals( sRets[ EDIT_PAG_GETVALORES.CODCC.ordinal() ].trim() ) ) {
				ps.setNull( param++, Types.INTEGER );
				ps.setNull( param++, Types.CHAR );
				ps.setNull( param++, Types.INTEGER );
				ps.setNull( param++, Types.INTEGER );
			}
			else {
				ps.setInt( param++, txtAnoCC.getVlrInteger() );
				ps.setString( param++, sRets[ EDIT_PAG_GETVALORES.CODCC.ordinal() ] );
				ps.setInt( param++, Aplicativo.iCodEmp );
				ps.setInt( param++, ListaCampos.getMasterFilial( "FNCC" ) );
			}
			
			if ( "".equals( sRets[ EDIT_PAG_GETVALORES.DOC.ordinal() ].trim() ) ) {
				ps.setNull( param++, Types.CHAR );
			}
			else {
				ps.setString( param++, sRets[  EDIT_PAG_GETVALORES.DOC.ordinal() ] );
			}
			if ( "".equals( sRets[  EDIT_PAG_GETVALORES.VLRPARC.ordinal() ].trim() ) ) {
				ps.setNull( param++, Types.DECIMAL );
			}
			else {
				ps.setBigDecimal( param++, ConversionFunctions.stringCurrencyToBigDecimal( sRets[ EDIT_PAG_GETVALORES.VLRPARC.ordinal() ] ) );
			}
			if ( "".equals( sRets[ EDIT_PAG_GETVALORES.VLRJUROS.ordinal() ].trim() ) ) {
				ps.setNull( param++, Types.DECIMAL );
			}
			else {
				ps.setBigDecimal( param++, ConversionFunctions.stringCurrencyToBigDecimal( sRets[ EDIT_PAG_GETVALORES.VLRJUROS.ordinal() ] ) );
			}
			if ( "".equals( sRets[ EDIT_PAG_GETVALORES.VLRADIC.ordinal() ].trim() ) ) {
				ps.setNull( param++, Types.DECIMAL );
			}
			else {
				ps.setBigDecimal( param++, ConversionFunctions.stringCurrencyToBigDecimal( sRets[ EDIT_PAG_GETVALORES.VLRADIC.ordinal() ] ) );
			}
			if ( "".equals( sRets[ EDIT_PAG_GETVALORES.VLRDESC.ordinal() ].trim() ) ) {
				ps.setNull( param++, Types.DECIMAL );
			}
			else {
				ps.setBigDecimal( param++, ConversionFunctions.stringCurrencyToBigDecimal( sRets[ EDIT_PAG_GETVALORES.VLRDESC.ordinal() ] ) );
			}

			if ( "".equals( sRets[ EDIT_PAG_GETVALORES.DTVENC.ordinal() ].trim() ) ) {
				ps.setNull( param++, Types.DECIMAL );
			}
			else {
				ps.setDate( param++, Funcoes.strDateToSqlDate( sRets[  EDIT_PAG_GETVALORES.DTVENC.ordinal() ] ) );
			}
			if ( "".equals( sRets[  EDIT_PAG_GETVALORES.OBS.ordinal() ].trim() ) ) {
				ps.setNull( param++, Types.CHAR );
			}
			else {
				ps.setString( param++, sRets[  EDIT_PAG_GETVALORES.OBS.ordinal() ] );
			}
			if ( "".equals( sRets[  EDIT_PAG_GETVALORES.CODTIPOCOB.ordinal() ].trim() ) ) {
				ps.setNull( param++, Types.INTEGER );
				ps.setNull( param++, Types.INTEGER );
				ps.setNull( param++, Types.INTEGER );
			}
			else {
				ps.setInt( param++, Integer.parseInt( sRets[  EDIT_PAG_GETVALORES.CODTIPOCOB.ordinal() ] ) );
				ps.setInt( param++, Aplicativo.iCodEmp );
				ps.setInt( param++, ListaCampos.getMasterFilial( "FNTIPOCOB" ) );
			}
			if ( "".equals( sRets[  EDIT_PAG_GETVALORES.VLRDEV.ordinal() ].trim() ) ) {
				ps.setNull( param++, Types.DECIMAL );
			}
			else {
				ps.setBigDecimal( param++, ConversionFunctions.stringCurrencyToBigDecimal( sRets[ EDIT_PAG_GETVALORES.VLRDEV.ordinal()] ) );
			}
			if ( "".equals( sRets[  EDIT_PAG_GETVALORES.CODCONTR.ordinal() ].trim() ) ) {
				ps.setNull( param++, Types.INTEGER );
				ps.setNull( param++, Types.INTEGER );
				ps.setNull( param++, Types.INTEGER );
				ps.setNull( param++, Types.INTEGER );
			}
			else {
				ps.setInt( param++, Integer.parseInt( sRets[  EDIT_PAG_GETVALORES.CODCONTR.ordinal() ] ) );
				ps.setInt( param++, Integer.parseInt( sRets[  EDIT_PAG_GETVALORES.CODITCONTR.ordinal() ] ) );
				ps.setInt( param++, Aplicativo.iCodEmp );
				ps.setInt( param++, ListaCampos.getMasterFilial( "VDITCONTRATO" ) );
			}
			ps.setInt( param++, txtCodPag.getVlrInteger() );
			ps.setInt( param++, txtNParcPag.getVlrInteger() );
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "FNPAGAR" ) );

			ps.executeUpdate();

			con.commit();


		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK && txtDtVenc.getVlrString().length() < 10 ) {
			Funcoes.mensagemInforma( this, "Data do vencimento é requerido!" );
		}
		else {
			super.actionPerformed( evt );
		}
	}

	private int buscaAnoBaseCC() {

		int iRet = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = con.prepareStatement( "SELECT ANOCENTROCUSTO FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				iRet = rs.getInt( "ANOCENTROCUSTO" );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar lista de cheques\n" + err.getMessage(), true, con, err );
		}

		return iRet;
	}

	public static Vector<Cheque> buscaCheques(Integer codpag, Integer nparcpag, DbConnection conn) {

		Vector<Cheque> ret = new Vector<Cheque>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();

		try {

			sql.append( "select ch.vlrcheq, ch.numcheq, ch.sitcheq, ch.seqcheq, pc.codpag, ch.dtemitcheq, ch.dtvenctocheq ");
			sql.append( "from fnpagcheq pc, fncheque ch " );
			sql.append( "where ch.codemp=pc.codempch and ch.codfilial=pc.codfilialch and ch.seqcheq=pc.seqcheq " );
			sql.append( "and pc.codemp=? and pc.codfilial=? and pc.codpag=? and pc.nparcpag=?" );

			ps = conn.prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNITPAGAR" ) );
			ps.setInt( 3, codpag );
			ps.setInt( 4, nparcpag );

			rs = ps.executeQuery();


			while (rs.next()) {

				Cheque cheque = new Cheque();

				cheque.setVlrcheq( rs.getBigDecimal( "vlrcheq" ));
				cheque.setNumcheq( rs.getInt( "numcheq" ));
				cheque.setSitcheq( rs.getString( "sitcheq" ));
				cheque.setSeqcheq( rs.getInt( "seqcheq" ));
				cheque.setDtemitcheq( rs.getDate( "dtemitcheq" ));
				cheque.setDtvenctocheq( rs.getDate( "dtvenctocheq" ));

				ret.add( cheque );

			}
			
			rs.close();
			ps.close();
			
			} 
		catch ( Exception err ) {		
			err.printStackTrace();
			Funcoes.mensagemErro( null, "Erro ao buscar cheques para o pagmento.\n" + err.getMessage(), true, conn, err );
		}

		
		
		return ret;

	}


	private int carregaCheques() {

		int iRet = 0;

		try {


			Vector<Cheque> cheques = buscaCheques( txtCodPag.getVlrInteger(),  txtNParcPag.getVlrInteger(), con );

			tabCheques.limpa();

			for ( int i = 0; cheques.size() > i; i++ ) {

				tabCheques.adicLinha();
				Cheque cheque = (Cheque) cheques.get( i );

				tabCheques.setValor( cheque.getSeqcheq(), i, enum_grid_cheques.SEQCHEQ.ordinal() );
				tabCheques.setValor( cheque.getNumcheq(), i, enum_grid_cheques.NUMCHEQ.ordinal() );
				tabCheques.setValor( cheque.getDtemitcheq(), i, enum_grid_cheques.DTEMITCHEQ.ordinal() );
				tabCheques.setValor( cheque.getDtvenctocheq(), i, enum_grid_cheques.DTVENCTOCHEQ.ordinal() );
				tabCheques.setValor( cheque.getVlrcheq(), i, enum_grid_cheques.VLRCHEQ.ordinal() );
				tabCheques.setValor( cheque.getSitcheq(), i, enum_grid_cheques.SITCHEQ.ordinal() );

			}

			tpn.remove(pnCheques);

			if(tabCheques.getNumLinhas()>0) {
				tpn.addTab( "Cheques", pnCheques );
			}


		} 
		catch ( Exception err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar cheques do pagamento.\n" + err.getMessage(), true, con, err );
		}

		return iRet;
	}

	private void montaGridCheques() {

		tabCheques.addMouseListener( new MouseAdapter() {

			public void mouseClicked( MouseEvent mevt ) {

				if ( mevt.getSource() == tabCheques && mevt.getClickCount() == 2 )
					abreCheque();
			}
		} );

		tabCheques.adicColuna( "Seq." );
		tabCheques.adicColuna( "Número" );
		tabCheques.adicColuna( "Dt.Emissão" );
		tabCheques.adicColuna( "Dt.Vencto." );
		tabCheques.adicColuna( "Valor" );
		tabCheques.adicColuna( "Sit." );

		tabCheques.setColunaInvisivel( enum_grid_cheques.SEQCHEQ.ordinal() );
		tabCheques.setTamColuna( 60, enum_grid_cheques.NUMCHEQ.ordinal() );
		tabCheques.setTamColuna( 80, enum_grid_cheques.DTEMITCHEQ.ordinal() );
		tabCheques.setTamColuna( 80, enum_grid_cheques.DTVENCTOCHEQ.ordinal() );
		tabCheques.setTamColuna( 80, enum_grid_cheques.VLRCHEQ.ordinal() );
		tabCheques.setTamColuna( 20, enum_grid_cheques.SITCHEQ.ordinal() );

	}

	private void abreCheque() {

		if ( tabCheques.getLinhaSel() > -1 ) {
			FCheque tela = null;
			if ( Aplicativo.telaPrincipal.temTela( FCheque.class.getName() ) ) {
				tela = (FCheque) Aplicativo.telaPrincipal.getTela( FCheque.class.getName() );
			}
			else {
				tela = new FCheque();
				Aplicativo.telaPrincipal.criatela( "Cheque", tela, con );
			}
			tela.exec( (Integer) tabCheques.getValor( tabCheques.getLinhaSel(), enum_grid_cheques.SEQCHEQ.ordinal() ) );
		}

	}

	public void beforeCarrega( CarregaEvent cevt ) {

		if ( cevt.getListaCampos() == lcCC && txtAnoCC.getVlrInteger().intValue() == 0 ) {
			txtAnoCC.setVlrInteger( new Integer( buscaAnoBaseCC() ) );
		}
	}

	public void afterCarrega( CarregaEvent cevt ) {

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcConta.setConexao( cn );
		lcConta.carregaDados();
		lcPlan.setConexao( cn );
		lcPlan.carregaDados();
		lcTipoCob.setConexao( cn );
		lcTipoCob.carregaDados();
		lcCC.setConexao( cn );
		lcCC.carregaDados();
		lcContrato.setConexao( cn );
		lcContrato.carregaDados();
		lcItContrato.setConexao( cn );
		lcItContrato.carregaDados();
		carregaCheques();
	}
}
