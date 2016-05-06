/**
 * @version 14/07/2003 <BR>
 * @version 03/07/2013 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)DLBaixaPag.java <BR>
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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.object.Historico;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.crm.view.frame.crud.detail.FContrato;
import org.freedom.modulos.fnc.library.swing.component.JTextFieldPlan;
import org.freedom.modulos.std.view.frame.crud.special.FPlanejamento;

public class DLBaixaPag extends FFDialogo implements CarregaListener {

	private static final long serialVersionUID = 1L;

	private final JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldPad txtRazFor = new JTextFieldPad( JTextFieldPad.TP_STRING, 60, 0 );

	private final JTextFieldPad txtCodConta = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldPad txtDescConta = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPlan txtCodPlan = new JTextFieldPlan( JTextFieldPad.TP_STRING, 13, 0 );
	
	private final JTextFieldPad txtCodRedPlan = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtDescPlan = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCodCC = new JTextFieldPad( JTextFieldPad.TP_STRING, 19, 0 );

	private final JTextFieldPad txtAnoCC = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );

	private final JTextFieldFK txtSiglaCC = new JTextFieldFK( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldFK txtDescCC = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtDoc = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldPad txtDtEmis = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtDtVenc = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtVlrParc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private final JTextFieldPad txtDtPagto = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtVlrPago = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

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
	
	private boolean multiBaixa;
	
	private boolean categoriaRequerida = true;

	private Integer codhistpag = null;
	
	private Integer anoBaseCC = null;

	private boolean lancafincontr = false;

	public static enum RET_BAIXA_PAG {CODCONTA, CODPLAN, DOC, DTPAGTO, VLRPAGO, CODCC, CODTIPOCOB, OBS, CODCONTR, CODITCONTR}

	public static enum VAL_BAIXAMANUT {CODFOR, RAZFOR, CODCONTA, CODPLAN, DOC, DTEMIS, DTVENC, VLRPARC, DTPAGTO, VLRPAGO, CODCC, CODTIPOCOB, OBS, CODCONTR, CODITCONTR}
	
	public DLBaixaPag( Component cOrig, boolean lancafincontr, boolean categoriaRequerida ) {

		super( cOrig );
		this.lancafincontr = lancafincontr;
		this.categoriaRequerida = categoriaRequerida;
		setTitulo( "Baixa" );
		setAtribos( 360, 520 );
		
		montaListaCampos();
		montaTela();
	}
	
	
	public DLBaixaPag(Component cOrig, boolean multibaixa, boolean categoriaRequerida, boolean lancafincontr){
		this( cOrig, lancafincontr, categoriaRequerida);
		this.multiBaixa = multibaixa;
		
	}
	

	private void montaListaCampos() {

		lcConta.add( new GuardaCampo( txtCodConta, "NumConta", "Nº Conta", ListaCampos.DB_PK, false ) );
		lcConta.add( new GuardaCampo( txtDescConta, "DescConta", "Descrição", ListaCampos.DB_SI, false ) );
		lcConta.montaSql( false, "CONTA", "FN" );
		lcConta.setReadOnly( true );
		txtCodConta.setTabelaExterna( lcConta, null );
		txtCodConta.setFK( true );
		txtCodConta.setNomeCampo( "NumConta" );

		lcCC.add( new GuardaCampo( txtCodCC, "CodCC", "Código", ListaCampos.DB_PK, false ) );
		lcCC.add( new GuardaCampo( txtSiglaCC, "SiglaCC", "Sigla", ListaCampos.DB_SI, false ) );
		lcCC.add( new GuardaCampo( txtDescCC, "DescCC", "Descrição", ListaCampos.DB_SI, false ) );
		lcCC.add( new GuardaCampo( txtAnoCC, "AnoCC", "Ano-Base", ListaCampos.DB_PK, false ) );
		lcCC.setReadOnly( true );
		lcCC.setQueryCommit( false );
		lcCC.setWhereAdic( "NIVELCC=10 " );
		lcCC.setDinWhereAdic( "ANOCC = #N ", txtAnoCC );
		lcCC.montaSql( false, "CC", "FN" );
		txtCodCC.setTabelaExterna( lcCC, null );
		txtCodCC.setFK( true );
		txtCodCC.setNomeCampo( "CodCC" );
		txtAnoCC.setTabelaExterna( lcCC, null );
		txtAnoCC.setFK( true );
		txtAnoCC.setNomeCampo( "AnoCC" );

		lcPlan.add( new GuardaCampo( txtCodPlan, "CodPlan", "Cód. Plan.", ListaCampos.DB_PK, false ) );
		lcPlan.add( new GuardaCampo( txtDescPlan, "DescPlan", "Descrição", ListaCampos.DB_SI, false ) );
		lcPlan.add( new GuardaCampo( txtCodRedPlan, "CodRedPlan", "Cód.Reduz.", ListaCampos.DB_SI, false ) );
		
		lcPlan.setWhereAdic( "TIPOPLAN = 'D' AND NIVELPLAN = 6" );
		lcPlan.montaSql( false, "PLANEJAMENTO", "FN" );
		lcPlan.setReadOnly( true );
		txtCodPlan.setTabelaExterna( lcPlan, FPlanejamento.class.getCanonicalName() );
		txtCodPlan.setFK( true );
		txtCodPlan.setNomeCampo( "CodPlan" );
		txtCodPlan.setRequerido( categoriaRequerida );

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
		
		if(categoriaRequerida) {
			Funcoes.setBordReq( txtCodPlan );
		}
		
		Funcoes.setBordReq( txtCodConta );
		Funcoes.setBordReq( txtDoc );
		Funcoes.setBordReq( txtDtPagto );
		Funcoes.setBordReq( txtVlrPago );
		Funcoes.setBordReq( txtObs );

		txtCodFor.setAtivo( false );
		txtRazFor.setAtivo( false );
		txtDescConta.setAtivo( false );
		txtDescPlan.setAtivo( false );
		txtDtEmis.setAtivo( false );
		txtDtVenc.setAtivo( false );
		txtVlrParc.setAtivo( false );

		adic( txtCodFor			, 7		, 20	, 80	, 20, "Cód.for." );
		adic( txtRazFor			, 90	, 20	, 250	, 20, "Razão do fornecedor" );
		adic( txtCodConta		, 7		, 60	, 80	, 20, "Cód.conta" );
		adic( txtDescConta		, 90	, 60	, 250	, 20, "Descrição da conta" );
		adic( txtCodPlan		, 7		, 100	, 100	, 20, "Cód.categoria" );
		adic( txtDescPlan		, 110	, 100	, 230	, 20, "Descrição da categoria" );
		adic( txtCodCC			, 7		, 140	, 100	, 20, "Cód.C.C." );
		adic( txtDescCC			, 110	, 140	, 230	, 20, "Descrição do centro de custo" );

		adic( txtCodTipoCob		, 7		, 180	, 80	, 20, "Cod.Tp.Cob" );
		adic( txtDescTipoCob	, 90	, 180	, 250	, 20, "Descrição do tipo de cobrança" );

		adic( txtDoc			, 7		, 220	, 110	, 20, "Doc." );
		adic( txtDtEmis			, 120	, 220	, 107	, 20, "Emissão" );
		adic( txtDtVenc			, 230	, 220	, 110	, 20, "Vencimento" );
		adic( txtVlrParc		, 7		, 260	, 110	, 20, "Vlr. Parc." );
		adic( txtDtPagto		, 120	, 260	, 107	, 20, "Dt. Pagto." );
		adic( txtVlrPago		, 230	, 260	, 110	, 20, "Vlr. Pago" );
		adic( txtObs			, 7		, 300	, 333	, 20, "Observações"  );
		adic( txtCodcontr, 7, 340, 80, 20, "Cód.contr."  );
		adic( txtDesccontr, 90, 340, 250, 20, "Descrição do contrato" );
		adic( txtCoditcontr, 7, 380, 80, 20, "Cód.it.contr.");
		adic( txtDescitcontr, 90, 380, 250, 20, "Descrição do item de contrato" );
		
		if ( ! lancafincontr) {
			txtCodcontr.setAtivo( false );
			txtCoditcontr.setAtivo( false );
		}

		
		lcCC.addCarregaListener( this );

	}

	public void setValores( String[] sVals ) {

		if (multiBaixa) {
			txtVlrPago.setAtivo( false );
			txtRazFor.setVlrString( "PGTOS MULTIPLOS" );
		} else {
			txtCodFor.setVlrString( sVals[ VAL_BAIXAMANUT.CODFOR.ordinal() ] );
			txtRazFor.setVlrString( sVals[ VAL_BAIXAMANUT.RAZFOR.ordinal() ] );
			txtCodConta.setVlrString( sVals[ VAL_BAIXAMANUT.CODCONTA.ordinal() ] );
			txtCodPlan.setVlrString( sVals[ VAL_BAIXAMANUT.CODPLAN.ordinal() ] );
			txtDoc.setVlrString( sVals[ VAL_BAIXAMANUT.DOC.ordinal() ] );
			txtDtEmis.setVlrString( sVals[ VAL_BAIXAMANUT.DTEMIS.ordinal() ] );
			txtDtVenc.setVlrString( sVals[ VAL_BAIXAMANUT.DTVENC.ordinal() ] );
			txtObs.setVlrString( sVals[ VAL_BAIXAMANUT.OBS.ordinal() ] );
			txtCodCC.setVlrString( sVals[ VAL_BAIXAMANUT.CODCC.ordinal() ] );
			txtCodTipoCob.setVlrString( sVals[ VAL_BAIXAMANUT.CODTIPOCOB.ordinal() ] );
		}
		
		txtVlrParc.setVlrString( sVals[ VAL_BAIXAMANUT.VLRPARC.ordinal() ] );
		txtDtPagto.setVlrString( sVals[ VAL_BAIXAMANUT.DTPAGTO.ordinal() ] );
		txtVlrPago.setVlrString( sVals[ VAL_BAIXAMANUT.VLRPAGO.ordinal() ] );
		if (!"".equals( sVals[ VAL_BAIXAMANUT.CODCONTR.ordinal()])) {
			txtCodcontr.setVlrString(sVals[ VAL_BAIXAMANUT.CODCONTR.ordinal() ] );
		}
		if (!"".equals( sVals[ VAL_BAIXAMANUT.CODITCONTR.ordinal()])) {
			txtCoditcontr.setVlrString(sVals[ VAL_BAIXAMANUT.CODITCONTR.ordinal() ] );
		}
	}

	public String[] getValores() {

		String[] sRetorno = new String[ RET_BAIXA_PAG.values().length ];

		sRetorno[ RET_BAIXA_PAG.CODCONTA.ordinal() ] = txtCodConta.getVlrString();
		sRetorno[ RET_BAIXA_PAG.CODPLAN.ordinal() ] = txtCodPlan.getVlrString();
		sRetorno[ RET_BAIXA_PAG.DOC.ordinal() ] = txtDoc.getVlrString();
		sRetorno[ RET_BAIXA_PAG.DTPAGTO.ordinal() ] = txtDtPagto.getVlrString();
		sRetorno[ RET_BAIXA_PAG.VLRPAGO.ordinal() ] = txtVlrPago.getVlrString();
		sRetorno[ RET_BAIXA_PAG.CODCC.ordinal() ] = txtCodCC.getVlrString();
		sRetorno[ RET_BAIXA_PAG.CODTIPOCOB.ordinal() ] = txtCodTipoCob.getVlrString();
		sRetorno[ RET_BAIXA_PAG.OBS.ordinal() ] = txtObs.getVlrString();
		sRetorno[ RET_BAIXA_PAG.CODCONTR.ordinal() ] = txtCodcontr.getVlrString();
		sRetorno[ RET_BAIXA_PAG.CODITCONTR.ordinal() ] = txtCoditcontr.getVlrString();

		return sRetorno;

	}

	public void actionPerformed( ActionEvent evt ) {

		if (evt.getSource() == btOK) {

			if (txtCodConta.getVlrString().length() < 1) {
				Funcoes.mensagemInforma( this, "Número da conta é requerido!" );
			} else if (txtCodPlan.getVlrString().length() < 13 && txtCodPlan.isRequerido()) {
				Funcoes.mensagemInforma( this, "Código da categoria é requerido!" );
			} else if (txtDtPagto.getVlrString().length() < 10) {
				Funcoes.mensagemInforma( this, "Data do pagamento é requerido!" );
			} else if (txtVlrPago.getVlrString().length() < 4) {
				Funcoes.mensagemInforma( this, "Valor pago é requerido!" );
			} else if ( txtVlrPago.getVlrDouble().doubleValue() <= 0.0) {
				Funcoes.mensagemInforma( this, "Valor pago deve ser maior que zero!" );
			} else {
				super.actionPerformed( evt );
			}
			
			if (txtObs.getVlrString()==null || txtObs.getVlrString().trim().equals( "" )) {
			
				Historico historico = null;
				
				if (codhistpag!=null  && codhistpag != 0) {
					historico = new Historico( codhistpag, con );
				} else {
					historico = new Historico();
					historico.setHistoricocodificado( DLNovoPag.HISTORICO_PADRAO );
				}
	
				historico.setData( txtDtEmis.getVlrDate() );
				historico.setDocumento( txtDoc.getVlrString() );
				historico.setPortador( txtRazFor.getVlrString() );
				historico.setValor( txtVlrParc.getVlrBigDecimal() );
//				historico.setHistoricoant( txtObs.getVlrString() );
				txtObs.setVlrString( historico.getHistoricodecodificado() );
			}
			
		} else {
			super.actionPerformed( evt );
		}
	}

	private int buscaAnoBaseCC() {
		int iRet = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement( "SELECT ANOCENTROCUSTO, CODHISTPAG FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				iRet = rs.getInt( "ANOCENTROCUSTO" );
				codhistpag = rs.getInt( "CODHISTPAG" );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar o ano-base para o centro de custo.\n" + err.getMessage(), true, con, err );
		} finally {
			ps = null;
			rs = null;
		}
		return iRet;
	}

	public void beforeCarrega( CarregaEvent cevt ) {
		if ( cevt.getListaCampos() == lcCC && txtAnoCC.getVlrInteger().intValue() == 0 ) {
			txtAnoCC.setVlrInteger(  anoBaseCC  );
		}
	}

	public void afterCarrega( CarregaEvent cevt ) { }

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcConta.setConexao( cn );
		lcPlan.setConexao( cn );
		lcCC.setConexao( cn );
		lcTipoCob.setConexao( cn );
		
		anoBaseCC = buscaAnoBaseCC();
		txtAnoCC.setVlrInteger( anoBaseCC );
		
		lcConta.carregaDados();
		lcCC.carregaDados();
		lcTipoCob.carregaDados();
		lcPlan.carregaDados();
		
		lcContrato.setConexao( cn );
		lcContrato.carregaDados();
		lcItContrato.setConexao( cn );
		lcItContrato.carregaDados();

	}

}
