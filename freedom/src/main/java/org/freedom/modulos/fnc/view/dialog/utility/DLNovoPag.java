/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)DLNovoPag.java <BR>
 * 
 *                    Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                    modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                    na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                    Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                    sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                    Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                    Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                    de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                    Tela para lançamento de contas a pagar.
 */

package org.freedom.modulos.fnc.view.dialog.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.object.Historico;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.util.SwingParams;
import org.freedom.modulos.fnc.library.swing.component.JTextFieldPlan;
import org.freedom.modulos.std.view.dialog.utility.DLFechaPag;

public class DLNovoPag extends FFDialogo implements PostListener, MouseListener, CarregaListener, FocusListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pnPag = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinCab = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinCampos = new JPanelPad( 580, 220 );

	private JTextFieldPad txtCodTipoFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDescTipoFor = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtRetencaoINSS = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtRetencaoOutros = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtRetencaoIRRF = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtPercBaseINSS = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 3, Aplicativo.casasDecFin );

	private JTextFieldPad txtPercRetOutros = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 3, Aplicativo.casasDecFin );

	private JTextFieldPad txtPercBaseIRRF = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 3, Aplicativo.casasDecFin );

	private JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtNroDependFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 3, 0 );

	private JTextFieldFK txtCNPJFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldFK txtDescFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodPlanoPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescPlanoPag = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private final JTextFieldPad txtCodTipoCob = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldFK txtDescTipoCob = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodBanco = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private JTextFieldFK txtDescBanco = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtNParcPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtVlrParcItPag = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtDtVencItPag = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtVlrParcPag = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtVlrDescPag = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtVlrAPagPag = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtDtEmisPag = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDocPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtObs = new JTextFieldPad( JTextFieldPad.TP_STRING, 250, 0 );

	private final JTextFieldPlan txtCodPlan = new JTextFieldPlan( JTextFieldPad.TP_STRING, 13, 0 );

	private final JTextFieldPad txtCodRedPlan = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldFK txtDescPlan = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCodTipoCobItPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldFK txtDescTipoCobItPag = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodConta = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldFK txtDescConta = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTablePad tabPag = new JTablePad();

	private JScrollPane spnTab = new JScrollPane( tabPag );

	private ListaCampos lcPagar = new ListaCampos( this );

	private final ListaCampos lcTipoCob = new ListaCampos( this, "TC" );

	private ListaCampos lcItPagar = new ListaCampos( this );

	private ListaCampos lcFor = new ListaCampos( this, "FR" );

	private ListaCampos lcTipoFor = new ListaCampos( this, "TF" );

	private ListaCampos lcPlanoPag = new ListaCampos( this, "PG" );

	private ListaCampos lcBanco = new ListaCampos( this, "BO" );

	private Navegador navPagar = new Navegador( false );

	private Navegador navItPagar = new Navegador( false );

	private final ListaCampos lcTipoCobItPag = new ListaCampos( this, "TC" );

	private ListaCampos lcConta = new ListaCampos( this, "CA" );

	public static final String HISTORICO_PADRAO = "PAGAMENTO REF. A COMPRA: <DOCUMENTO>";

	private Map<String, Integer> prefere = null;

	private Historico historico = null;

	private Component owner = null;

	private JTextFieldPad txtVlrBaseIRRF = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtVlrBaseINSS = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtVlrRetIRRF = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtVlrRetINSS = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JPanelPad pnRetencoes = new JPanelPad( 580, 75 );

	private JLabelPad lbObs = new JLabelPad( "Observações" );

	private int saltolinha = 0;

	private final JTextFieldPad txtCodCC = new JTextFieldPad( JTextFieldPad.TP_STRING, 19, 0 );

	private final JTextFieldPad txtAnoCC = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );

	private final JTextFieldFK txtSiglaCC = new JTextFieldFK( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldFK txtDescCC = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final ListaCampos lcCC = new ListaCampos( this, "CC" );

	private final ListaCampos lcPlan = new ListaCampos( this, "PN" );

	public DLNovoPag( Component cOrig ) {

		super( cOrig );
		this.owner = cOrig;

		setTitulo( "Novo título para pagamento" );
		setAtribos( 600, 450 );

		montaListaCampos();
		montaTela();

		lcPagar.addPostListener( this );
		tabPag.addMouseListener( this );
		lcFor.addCarregaListener( this );
		lcTipoFor.addCarregaListener( this );
		txtVlrParcPag.addFocusListener( this );

	}

	private void montaTela() {

		lcPagar.add( new GuardaCampo( txtCodPag, "CodPag", "Cód.pag.", ListaCampos.DB_PK, true ) );
		lcPagar.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.for.", ListaCampos.DB_FK, true ) );
		lcPagar.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.p.pg.", ListaCampos.DB_FK, true ) );
		lcPagar.add( new GuardaCampo( txtCodBanco, "CodBanco", "Cód.banco", ListaCampos.DB_FK, false ) );
		lcPagar.add( new GuardaCampo( txtCodTipoCob, "CodTipoCob", "Cód.tp.cob.", ListaCampos.DB_FK, false ) );
		lcPagar.add( new GuardaCampo( txtVlrParcPag, "VlrParcPag", "Valor da parc.", ListaCampos.DB_SI, false ) );
		lcPagar.add( new GuardaCampo( txtDtEmisPag, "DataPag", "Dt.emissão", ListaCampos.DB_SI, true ) );
		lcPagar.add( new GuardaCampo( txtDocPag, "DocPag", "N.documento", ListaCampos.DB_SI, true ) );
		lcPagar.add( new GuardaCampo( txtObs, "ObsPag", "Obs.", ListaCampos.DB_SI, false ) );
		lcPagar.add( new GuardaCampo( txtCodConta, "NumConta", "Cód.Conta", ListaCampos.DB_FK, txtDescConta, false ) );
		lcPagar.add( new GuardaCampo( txtVlrDescPag, "VlrDescPag", "Valor da parc.", ListaCampos.DB_SI, false ) );
		lcPagar.add( new GuardaCampo( txtVlrAPagPag, "VlrAPagPag", "Valor a pargar", ListaCampos.DB_SI, false ) );

		lcPagar.add( new GuardaCampo( txtVlrBaseIRRF, "VlrBaseIRRF", "Valor base IRRF", ListaCampos.DB_SI, false ) );
		lcPagar.add( new GuardaCampo( txtVlrBaseINSS, "VlrBaseINSS", "Valor base INSS", ListaCampos.DB_SI, false ) );
		lcPagar.add( new GuardaCampo( txtVlrRetIRRF, "VlrRetIRRF", "Valor ret. IRRF", ListaCampos.DB_SI, false ) );
		lcPagar.add( new GuardaCampo( txtVlrRetINSS, "VlrRetINSS", "Valor ret. INSS", ListaCampos.DB_SI, false ) );

		lcPagar.add( new GuardaCampo( txtCodPlan, "CodPlan", "Cód.Plan.", ListaCampos.DB_FK, txtDescPlan, false ) );
		lcPagar.add( new GuardaCampo( txtAnoCC, "AnoCC", "Ano.C.C.", ListaCampos.DB_SI, txtDescCC, false ) );
		lcPagar.add( new GuardaCampo( txtCodCC, "CodCC", "Cód.C.C.", ListaCampos.DB_FK, txtDescCC, false ) );

		lcPagar.montaSql( true, "PAGAR", "FN" );

		txtNParcPag.setNomeCampo( "NParcPag" );
		lcItPagar.add( new GuardaCampo( txtNParcPag, "NParcPag", "N.parc.", ListaCampos.DB_PK, false ) );
		lcItPagar.add( new GuardaCampo( txtVlrParcItPag, "VlrParcItPag", "Valor tot.", ListaCampos.DB_SI, false ) );
		lcItPagar.add( new GuardaCampo( txtDtVencItPag, "DtVencItPag", "Dt.vencto.", ListaCampos.DB_SI, false ) );
		lcItPagar.montaSql( false, "ITPAGAR", "FN" );
		lcItPagar.setQueryCommit( false );
		txtNParcPag.setListaCampos( lcItPagar );
		txtVlrParcItPag.setListaCampos( lcItPagar );
		txtDtVencItPag.setListaCampos( lcItPagar );

		lcItPagar.montaTab();
		// tabPag.addMouseListener( new HandlerMouseListenerPagamento() );

		c.add( pnPag );

		// pnPag.add( pinCab, BorderLayout.NORTH );
		// pnPag.add( spnTab, BorderLayout.CENTER );

		setPainel( pinCampos );

		adic( txtCodFor, 7, 20, 80, 20, "Cód.for." );
		adic( txtDescFor, 90, 20, 197, 20, "Razão social do fornecedor" );

		adic( txtCodPlanoPag, 290, 20, 80, 20, "Cód.p.pag." );
		adic( txtDescPlanoPag, 373, 20, 200, 20, "Descrição do plano de pagto." );

		adic( txtCodBanco, 7, 60, 80, 20, "Cód.banco" );
		adic( txtDescBanco, 90, 60, 197, 20, "Nome do banco" );

		adic( txtCodConta, 290, 60, 80, 20, "Nº Conta" );
		adic( txtDescConta, 373, 60, 197, 20, "Descrição da conta" );

		adic( txtCodTipoCob, 7, 100, 80, 20, "Cód.Tip.Cob." );
		adic( txtDescTipoCob, 90, 100, 197, 20, "Descrição do tipo de cobrança" );

		adic( txtCodCC, 290, 100, 80, 20, "Cód.c.c." );
		adic( txtDescCC, 373, 100, 200, 20, "Descrição do centro de custo" );

		adic( txtCodPlan, 7, 140, 80, 20, "Cód.categ." );
		adic( txtDescPlan, 90, 140, 197, 20, "Descrição da categoria" );

		adic( txtDtEmisPag, 290, 140, 80, 20, "Dt.Emissão" );

		adic( txtDocPag, 373, 140, 100, 20, "Documento" );
		adic( txtVlrParcPag, 476, 140, 97, 20, "Valor original" );

		adic( lbObs, 7, 160, 300, 20 );
		adic( txtObs, 7, 180, 565, 20 );

		pnRetencoes.setBorder( SwingParams.getPanelLabel( "Retenções", Color.RED ) );

		pnRetencoes.adic( txtVlrBaseIRRF, 7, 20, 90, 20, "Vlr.base IRRF" );
		pnRetencoes.adic( txtVlrBaseINSS, 100, 20, 90, 20, "Vlr.base INSS" );
		pnRetencoes.adic( txtVlrRetIRRF, 193, 20, 90, 20, "Valor IRRF" );
		pnRetencoes.adic( txtVlrRetINSS, 286, 20, 90, 20, "Valor INSS" );
		pnRetencoes.adic( txtVlrDescPag, 379, 20, 90, 20, "Tot.Desc." );
		pnRetencoes.adic( txtVlrAPagPag, 472, 20, 90, 20, "Tot.Líquido" );

		pnPag.add( pinCab, BorderLayout.NORTH );
		pnPag.add( spnTab, BorderLayout.CENTER );

		pinCab.add( pinCampos, BorderLayout.CENTER );
		pinCab.add( pnRetencoes, BorderLayout.SOUTH );

	}

	private void removeRetencoes( boolean remove ) {

		pinCab.remove( pnRetencoes );

		if ( !remove ) {
			pinCab.add( pnRetencoes, BorderLayout.SOUTH );
		}

		this.validate();
		this.repaint();

	}

	public static BigDecimal getVlrINSS( BigDecimal valororiginal, BigDecimal valorbase, BigDecimal peroutros, Integer nrodepend ) {

		BigDecimal ret = null;
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;

		BigDecimal aliquota = null;

		BigDecimal cem = new BigDecimal( 100 );

		try {

			sql.append( "select first 1 coalesce(inss.aliquota,0) aliquota " );
			sql.append( "from rhtabelainss inss " );
			sql.append( "where " );
			sql.append( "inss.teto >= ? " );
			sql.append( "order by inss.teto " );

			ps = Aplicativo.getInstace().getConexao().prepareStatement( sql.toString() );
			ps.setBigDecimal( 1, valororiginal );

			rs = ps.executeQuery();

			if ( rs.next() ) {

				aliquota = rs.getBigDecimal( "aliquota" );

			}
			else {
				return new BigDecimal( 0 );
			}

			System.out.println( "Aliquota INSS:" + aliquota );

			if ( ( peroutros != null ) && ( peroutros.compareTo( new BigDecimal( 0 ) ) > 0 ) ) {
				aliquota = aliquota.add( peroutros );
			}

			System.out.println( "Aliquota INSS+Outros:" + aliquota );

			ret = valorbase.multiply( aliquota.divide( cem ) );

			System.out.println( "Valor INSS:" + ret );

			ps.close();
			rs.close();

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return ret;
	}

	public static BigDecimal getVlrIRRF( BigDecimal valororiginal, BigDecimal valorbaseirrf, BigDecimal valorbaseinss, BigDecimal valorinss, BigDecimal vlrdep, Integer nrodepend ) {

		BigDecimal ret = null;
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;

		BigDecimal aliquota = null;
		BigDecimal reducaodependente = null;
		BigDecimal deducao = new BigDecimal( 0 );
		BigDecimal cem = new BigDecimal( 100 );

		try {

			sql.append( "select first 1 coalesce(ir.aliquota,0) aliquota, coalesce(ir.reducaodependente,0) reducao, coalesce(ir.deducao,0) deducao " );
			sql.append( "from rhtabelairrf ir " );
			sql.append( "where " );
			sql.append( "ir.teto >= ? " );
			sql.append( "order by ir.teto " );

			ps = Aplicativo.getInstace().getConexao().prepareStatement( sql.toString() );

			BigDecimal baseirrf_final = valorbaseirrf;

			baseirrf_final = baseirrf_final.subtract( valorinss );
			baseirrf_final = baseirrf_final.subtract( vlrdep );

			ps.setBigDecimal( 1, baseirrf_final );

			rs = ps.executeQuery();

			if ( rs.next() ) {

				deducao = rs.getBigDecimal( "deducao" );

				System.out.println( "Deducao IR:" + deducao );

				aliquota = rs.getBigDecimal( "aliquota" );

				System.out.println( "Aliquota IR:" + aliquota );

				reducaodependente = rs.getBigDecimal( "reducao" );

				System.out.println( "Reducao IR:" + reducaodependente );

			}
			else {
				return null;
			}

			/*** reduzindo a base ****/

			// Reduzindo INSS pagoc
			if ( valorbaseinss != null && valorbaseinss.compareTo( new BigDecimal( 0 ) ) > 0 ) {
				valorbaseirrf = valorbaseirrf.subtract( getVlrINSS( valororiginal, valorbaseinss, null, nrodepend ) );
			}

			// Reduzindo dependentes
			valorbaseirrf = valorbaseirrf.subtract( reducaodependente.multiply( new BigDecimal( nrodepend ) ) );

			// Calcula IRRF sem dedução da tabela
			ret = valorbaseirrf.multiply( aliquota.divide( cem ) );

			// Subtraindo dedução da tabela

			ret = ret.subtract( deducao );

			System.out.println( "Valor IRRF:" + ret );

			// Se for menor de 10 não deve realizar a retenção.
			if ( ret.floatValue() <= 10.00f ) {

				ret = new BigDecimal( 0 );

			}

			ps.close();
			rs.close();

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return ret;
	}

	private void calcRetencoes() {

		BigDecimal cem = new BigDecimal( 100 );

		BigDecimal percbaseinss = null;
		BigDecimal percbaseirrf = null;
		BigDecimal percoutros = null;
		BigDecimal vlrbaseinss = null;
		BigDecimal vlrbaseirrf = null;

		BigDecimal vlrinss = null;
		BigDecimal vlrirrf = null;
		BigDecimal vlroriginal = null;

		Boolean calcinss = false;
		Boolean calcirrf = false;
		Boolean calcoutros = false;

		Integer nrodepend = txtNroDependFor.getVlrInteger();

		try {

			calcinss = "S".equals( txtRetencaoINSS.getVlrString() );
			calcirrf = "S".equals( txtRetencaoIRRF.getVlrString() );
			calcoutros = "S".equals( txtRetencaoIRRF.getVlrString() );

			vlroriginal = txtVlrParcPag.getVlrBigDecimal();

			// Se deve calcular a retenção de INSS...
			if ( calcinss ) {

				percbaseinss = txtPercBaseINSS.getVlrBigDecimal();

				vlrbaseinss = ( vlroriginal.multiply( percbaseinss ) ).divide( cem );

				// Se deve calcular a retenção de outros tributos junto com o INSS
				if ( calcoutros ) {
					percoutros = txtPercRetOutros.getVlrBigDecimal();
				}

				vlrinss = getVlrINSS( vlroriginal, vlrbaseinss, percoutros, nrodepend );

				// Carregando campos...
				txtPercBaseINSS.setVlrBigDecimal( percbaseinss );
				txtVlrBaseINSS.setVlrBigDecimal( vlrbaseinss );
				txtVlrRetINSS.setVlrBigDecimal( vlrinss );

			}

			// Se deve calcular a retenção de INSS...
			if ( calcirrf ) {

				percbaseirrf = txtPercBaseIRRF.getVlrBigDecimal();
				vlrbaseirrf = ( vlroriginal.multiply( percbaseirrf ) ).divide( cem );

				// Valor colocado de forma fixa... deve ser substituido urgentemente!

				vlrirrf = getVlrIRRF( vlroriginal, vlrbaseirrf, vlrbaseinss, vlrinss, getReducaoDependente(), nrodepend );

				txtPercBaseIRRF.setVlrBigDecimal( percbaseirrf );
				txtVlrBaseIRRF.setVlrBigDecimal( vlrbaseirrf );
				txtVlrRetIRRF.setVlrBigDecimal( vlrirrf );

			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	public static BigDecimal getReducaoDependente() {

		BigDecimal ret = new BigDecimal( 0 );

		try {
			PreparedStatement ps = Aplicativo.getInstace().getConexao().prepareStatement( "SELECT FIRST 1 REDUCAODEPENDENTE FROM RHTABELAIRRF" );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				ret = rs.getBigDecimal( "REDUCAODEPENDENTE" );
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return ret;

	}

	private void calcTotLiquido() {

		BigDecimal vlrdescontos = null;
		BigDecimal vlrorig = null;
		BigDecimal vlrliquido = null;
		BigDecimal vlrirrf = null;
		BigDecimal vlrinss = null;

		try {

			vlrirrf = txtVlrRetIRRF.getVlrBigDecimal();
			vlrinss = txtVlrRetINSS.getVlrBigDecimal();

			vlrdescontos = vlrirrf.add( vlrinss );

			vlrorig = txtVlrParcPag.getVlrBigDecimal();

			if ( vlrdescontos == null ) {
				vlrdescontos = new BigDecimal( 0 );
			}
			if ( vlrorig == null ) {
				Funcoes.mensagemInforma( null, "Valor original inválido!" );
				return;
			}

			vlrliquido = vlrorig.subtract( vlrdescontos );

			txtVlrDescPag.setVlrBigDecimal( vlrdescontos );
			txtVlrAPagPag.setVlrBigDecimal( vlrliquido );

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private void montaListaCampos() {

		lcItPagar.setMaster( lcPagar );
		lcPagar.adicDetalhe( lcItPagar );
		lcItPagar.setTabela( tabPag );
		navPagar.setName( "Pagar" );
		lcPagar.setNavegador( navPagar );

		navItPagar.setName( "itpagar" );
		lcItPagar.setNavegador( navItPagar );

		lcFor.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.for.", ListaCampos.DB_PK, false ) );
		lcFor.add( new GuardaCampo( txtDescFor, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );
		lcFor.add( new GuardaCampo( txtCodTipoFor, "CodTipoFor", "Cód.for.", ListaCampos.DB_FK, false ) );
		lcFor.add( new GuardaCampo( txtCNPJFor, "CnpjFor", "CNPJ", ListaCampos.DB_SI, false ) );
		lcFor.add( new GuardaCampo( txtNroDependFor, "NroDependFor", "Nro.Depend.", ListaCampos.DB_SI, false ) );

		lcFor.montaSql( false, "FORNECED", "CP" );
		lcFor.setQueryCommit( false );
		lcFor.setReadOnly( true );
		txtCodFor.setTabelaExterna( lcFor, null );
		txtCodFor.setFK( true );
		txtCodFor.setNomeCampo( "CodFor" );

		lcPlanoPag.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.p.pg.", ListaCampos.DB_PK, false ) );
		lcPlanoPag.add( new GuardaCampo( txtDescPlanoPag, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPag.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag.setQueryCommit( false );
		lcPlanoPag.setReadOnly( true );
		txtCodPlanoPag.setTabelaExterna( lcPlanoPag, null );
		txtCodPlanoPag.setFK( true );
		txtCodPlanoPag.setNomeCampo( "CodPlanoPag" );

		lcBanco.add( new GuardaCampo( txtCodBanco, "CodBanco", "Cód.banco", ListaCampos.DB_PK, false ) );
		lcBanco.add( new GuardaCampo( txtDescBanco, "NomeBanco", "Nome do banco", ListaCampos.DB_SI, false ) );
		lcBanco.montaSql( false, "BANCO", "FN" );
		lcBanco.setQueryCommit( false );
		lcBanco.setReadOnly( true );
		txtCodBanco.setTabelaExterna( lcBanco, null );
		txtCodBanco.setFK( true );
		txtCodBanco.setNomeCampo( "CodBanco" );

		/**********************
		 * FNCONTA *
		 **********************/
		lcConta.add( new GuardaCampo( txtCodConta, "NumConta", "Nº Conta", ListaCampos.DB_PK, false ) );
		lcConta.add( new GuardaCampo( txtDescConta, "DescConta", "Descrição da conta", ListaCampos.DB_SI, false ) );
		lcConta.montaSql( false, "CONTA", "FN" );
		lcConta.setQueryCommit( false );
		lcConta.setReadOnly( true );
		txtCodConta.setTabelaExterna( lcConta, null );
		txtCodConta.setFK( true );
		txtCodConta.setNomeCampo( "NumConta" );
		txtDescConta.setTabelaExterna( lcConta, null );
		txtDescConta.setLabel( "Descrição da Conta" );

		/***************
		 * FNTIPOCOB *
		 ***************/
		lcTipoCob.add( new GuardaCampo( txtCodTipoCob, "CodTipoCob", "Cód.Tip.Cob", ListaCampos.DB_PK, false ) );
		lcTipoCob.add( new GuardaCampo( txtDescTipoCob, "DescTipoCob", "Descrição do tipo de cobrança", ListaCampos.DB_SI, false ) );
		lcTipoCob.montaSql( false, "TIPOCOB", "FN" );
		lcTipoCob.setQueryCommit( false );
		lcTipoCob.setReadOnly( true );
		txtCodTipoCob.setTabelaExterna( lcTipoCob, null );
		txtCodTipoCob.setFK( true );
		txtCodTipoCob.setNomeCampo( "CodTipoCob" );

		/***************
		 * CPTIPOFOR *
		 ***************/
		lcTipoFor.add( new GuardaCampo( txtCodTipoFor, "CodTipoFor", "Cód.tp.for.", ListaCampos.DB_PK, false ) );
		lcTipoFor.add( new GuardaCampo( txtDescTipoFor, "DescTipoFor", "Descrição do tipo de fornecedor", ListaCampos.DB_SI, false ) );
		lcTipoFor.add( new GuardaCampo( txtRetencaoINSS, "RetencaoINSS", "Ret.INSS", ListaCampos.DB_SI, false ) );
		lcTipoFor.add( new GuardaCampo( txtRetencaoIRRF, "RetencaoIRRF", "Ret.IRRF", ListaCampos.DB_SI, false ) );
		lcTipoFor.add( new GuardaCampo( txtRetencaoOutros, "RetencaoOutros", "Ret.Outros", ListaCampos.DB_SI, false ) );
		lcTipoFor.add( new GuardaCampo( txtPercBaseINSS, "PercBaseINSS", "%Base.INSS", ListaCampos.DB_SI, false ) );
		lcTipoFor.add( new GuardaCampo( txtPercBaseIRRF, "PercBaseIRRF", "%Base.IRRF", ListaCampos.DB_SI, false ) );
		lcTipoFor.add( new GuardaCampo( txtPercRetOutros, "PercRetOutros", "%Ret.Outros", ListaCampos.DB_SI, false ) );
		lcTipoFor.montaSql( false, "TIPOFOR", "CP" );
		lcTipoFor.setQueryCommit( false );
		lcTipoFor.setReadOnly( true );
		txtCodTipoFor.setTabelaExterna( lcTipoFor, null );
		txtCodTipoFor.setFK( true );
		txtCodTipoFor.setNomeCampo( "CodTipoFor" );

		/******************
		 * FNPLANEJAMENTO *
		 ******************/
		lcPlan.add( new GuardaCampo( txtCodPlan, "CodPlan", "Cód.plan.", ListaCampos.DB_PK, false ) );
		lcPlan.add( new GuardaCampo( txtDescPlan, "DescPlan", "Descrição do planejamento", ListaCampos.DB_SI, false ) );
		lcPlan.add( new GuardaCampo( txtCodRedPlan, "CodRedPlan", "Cód.Red.", ListaCampos.DB_SI, false ) );
		lcPlan.setWhereAdic( "TIPOPLAN = 'D' AND NIVELPLAN = 6" );
		lcPlan.montaSql( false, "PLANEJAMENTO", "FN" );
		lcPlan.setReadOnly( true );
		txtCodPlan.setTabelaExterna( lcPlan, null );
		txtCodPlan.setFK( true );
		txtCodPlan.setNomeCampo( "CodPlan" );

		/***************
		 * FNCC *
		 ***************/
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

	}

	public void setValues( Object[] values ) {

		txtCodFor.setVlrInteger( values[ 0 ] != null ? (Integer) values[ 0 ] : 0 );
		txtCodPlanoPag.setVlrInteger( values[ 1 ] != null ? (Integer) values[ 1 ] : 0 );
		txtCodBanco.setVlrString( values[ 2 ] != null ? (String) values[ 2 ] : "" );
		txtCodTipoCob.setVlrInteger( values[ 3 ] != null ? (Integer) values[ 3 ] : 0 );
		txtVlrParcPag.setVlrBigDecimal( values[ 4 ] != null ? (BigDecimal) values[ 4 ] : new BigDecimal( "0.00" ) );
		txtDtEmisPag.setVlrDate( values[ 5 ] != null ? (Date) values[ 5 ] : new Date() );
		txtDocPag.setVlrString( values[ 6 ] != null ? (String) values[ 6 ] : "" );
		txtObs.setVlrString( values[ 7 ] != null ? (String) values[ 7 ] : "" );

		lcTipoCob.carregaDados();
		lcBanco.carregaDados();
		lcPlanoPag.carregaDados();
		lcFor.carregaDados();

	}

	public int getCodigoPagamento() {

		return lcPagar.getStatus() == ListaCampos.LCS_SELECT ? txtCodPag.getVlrInteger() : 0;
	}

	private void testaCodPag() {

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT * FROM SPGERANUM(?,?,?)" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNPAGAR" ) );
			ps.setString( 3, "PA" );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				txtCodPag.setVlrString( rs.getString( 1 ) );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao confirmar código do contas a pagar!\n" + err.getMessage(), true, con, err );
		}
	}

	private boolean validaDocumento() {

		boolean ret = true;

		try {
			StringBuilder sql = new StringBuilder();
			sql.append( "select pg.codpag, pg.datapag, pg.codpag from fnpagar pg where pg.codemp=? and pg.codfilial=? and pg.docpag=? " );
			sql.append( "and pg.codempfr=? and pg.codfilialfr=? and pg.codfor=?" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNPAGAR" ) );
			ps.setInt( 3, txtDocPag.getVlrInteger() );
			ps.setInt( 4, Aplicativo.iCodEmp );
			ps.setInt( 5, ListaCampos.getMasterFilial( "CPFORNECED" ) );
			ps.setInt( 6, txtCodFor.getVlrInteger() );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				if ( Funcoes.mensagemConfirma( null, "Já existe um título lançado para o documento (" + txtDocPag.getVlrString() + " - Cod.Pag.: " + rs.getString( "codpag" ) + " de " + Funcoes.dateToStrDate( rs.getDate( "datapag" ) ) + ")" + " !\n" + "Confirma a gravação deste novo título?"

				) == JOptionPane.YES_OPTION ) {

					ret = true;

				}
				else {
					ret = false;
				}

			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao validar documento!\n" + err.getMessage(), true, con, err );
		}
		return ret;
	}

	public void beforePost( PostEvent e ) {

		if ( e.getListaCampos().equals( lcPagar ) && lcPagar.getStatus() == ListaCampos.LCS_INSERT ) {

			if ( !validaDocumento() ) {
				e.cancela();
			}

			testaCodPag();

			// Gerando histórico dinâmico

			Integer codhistpag = null;

			codhistpag = (Integer) prefere.get( "codhistpag" );

			if ( codhistpag != 0 ) {
				historico = new Historico( codhistpag, con );
				historico.setData( txtDtEmisPag.getVlrDate() );
				historico.setDocumento( txtDocPag.getVlrString() );
				historico.setPortador( txtDescFor.getVlrString() );
				historico.setValor( txtVlrParcPag.getVlrBigDecimal() );
				historico.setHistoricoant( txtObs.getVlrString() );

				txtObs.setVlrString( historico.getHistoricodecodificado() );
			}
			else {
				historico = new Historico();
				historico.setHistoricocodificado( HISTORICO_PADRAO );
				historico.setData( txtDtEmisPag.getVlrDate() );
				historico.setDocumento( txtDocPag.getVlrString() );
				historico.setPortador( txtDescFor.getVlrString() );
				historico.setValor( txtVlrParcPag.getVlrBigDecimal() );
				historico.setHistoricoant( txtObs.getVlrString() );

				if ( "".equals( txtObs.getVlrString() ) ) {
					txtObs.setVlrString( historico.getHistoricodecodificado() );
				}
				else {
					txtObs.setVlrString( txtObs.getVlrString() );
				}
			}

		}
	}

	public void afterPost( PostEvent e ) {

	}

	public void actionPerformed( ActionEvent e ) {

		if ( e.getSource() == btOK ) {

			if ( txtDtEmisPag.getVlrString().length() < 10 ) {
				Funcoes.mensagemInforma( this, "Data de emissão é requerido!" );
			}
			else {
				if ( lcPagar.getStatus() == ListaCampos.LCS_INSERT ) {
					lcPagar.post();
				}
				else {
					super.actionPerformed( e );
				}
			}
		}
		else {
			super.actionPerformed( e );
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcFor.setConexao( cn );
		lcTipoFor.setConexao( cn );
		lcPlanoPag.setConexao( cn );
		lcConta.setConexao( cn );
		lcTipoCob.setConexao( cn );
		lcPagar.setConexao( cn );
		lcItPagar.setConexao( cn );
		lcBanco.setConexao( cn );
		lcPlan.setConexao( cn );
		lcCC.setConexao( cn );

		lcPagar.insert( true );

		prefere = getPrefere();

	}

	private Map<String, Integer> getPrefere() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer anocc = null;
		Integer codhistpag = null;

		Map<String, Integer> retorno = new HashMap<String, Integer>();

		try {

			ps = con.prepareStatement( "SELECT ANOCENTROCUSTO,CODHISTPAG FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				anocc = rs.getInt( "ANOCENTROCUSTO" );
				codhistpag = rs.getInt( "CODHISTPAG" );
			}

			retorno.put( "codhistpag", codhistpag );
			retorno.put( "anocc", anocc );

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar o ano-base para o centro de custo.\n" + err.getMessage(), true, con, err );
		} finally {
			ps = null;
			rs = null;
		}
		return retorno;
	}

	private void alteraPag() {

		DLFechaPag dl = new DLFechaPag( this, txtVlrParcItPag.getVlrBigDecimal(), txtDtVencItPag.getVlrDate() );
		dl.setModalityType( ModalityType.APPLICATION_MODAL );
		dl.setVisible( true );

		// dl.execShow();

		try {

			if ( dl.OK ) {

				lcItPagar.edit();

				txtVlrParcItPag.setVlrBigDecimal( (BigDecimal) dl.getValores()[ 0 ] );
				txtDtVencItPag.setVlrDate( (Date) dl.getValores()[ 1 ] );
				lcItPagar.post();

				// Atualiza lcPagar

				if ( lcPagar.getStatus() == ListaCampos.LCS_EDIT ) {
					lcPagar.post(); // Caso o lcPagar estaja como edit executa o post que atualiza
				}
				else {
					lcPagar.carregaDados(); // Caso não, atualiza
				}
				dl.dispose();
			}
			else {
				dl.dispose();
				lcItPagar.cancel( false );
			}
			dl.dispose();
		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao atualizar parcelas.\n" + e.getMessage() );
			lcItPagar.cancel( true );
			lcPagar.cancel( true );
		}

	}

	/*
	 * private class HandlerMouseListenerPagamento extends MouseAdapter {
	 * 
	 * public void mouseClicked( MouseEvent mevt ) {
	 * 
	 * if ( mevt.getClickCount() == 2 && tabPag.getLinhaSel() >= 0 ) {
	 * 
	 * lcItPagar.edit();
	 * 
	 * // DLFechaPag dl = new DLFechaPag( DLNovoPag.this, txtVlrParcItPag.getVlrBigDecimal(), txtDtVencItPag.getVlrDate() ); DLFechaPag dl = new DLFechaPag( owner, txtVlrParcItPag.getVlrBigDecimal(), txtDtVencItPag.getVlrDate() ); dl.setVisible( true );
	 * 
	 * if ( dl.OK ) {
	 * 
	 * txtVlrParcItPag.setVlrBigDecimal( (BigDecimal) dl.getValores()[ 0 ] ); txtDtVencItPag.setVlrDate( (Date) dl.getValores()[ 1 ] ); lcItPagar.post();
	 * 
	 * // Atualiza lcPagar
	 * 
	 * if ( lcPagar.getStatus() == ListaCampos.LCS_EDIT ) { lcPagar.post(); // Caso o lcPagar estaja como edit executa o post que atualiza } else { lcPagar.carregaDados(); // Caso não, atualiza } dl.dispose(); } else { dl.dispose(); lcItPagar.cancel( false ); } dl.dispose(); } } }
	 */

	public void mouseClicked( MouseEvent mevt ) {

		if ( mevt.getSource() == tabPag ) {
			if ( ( mevt.getClickCount() == 2 ) & ( tabPag.getLinhaSel() >= 0 ) ) {
				alteraPag();
			}
		}
	}

	public void mouseEntered( MouseEvent e ) {

		// TODO Auto-generated method stub

	}

	public void mouseExited( MouseEvent e ) {

		// TODO Auto-generated method stub

	}

	public void mousePressed( MouseEvent e ) {

		// TODO Auto-generated method stub

	}

	public void mouseReleased( MouseEvent e ) {

		// TODO Auto-generated method stub

	}

	public void afterCarrega( CarregaEvent cevt ) {

		if ( cevt.getListaCampos() == lcFor ) {

			lcTipoFor.carregaDados();

		}
		if ( cevt.getListaCampos() == lcTipoFor ) {

			if ( ( "S".equals( txtRetencaoINSS.getVlrString() ) ) || ( "S".equals( txtRetencaoIRRF.getVlrString() ) ) ) {
				removeRetencoes( false );
			}
			else {
				removeRetencoes( true );
			}

		}

	}

	public void beforeCarrega( CarregaEvent cevt ) {

		// TODO Auto-generated method stub

	}

	public void focusGained( FocusEvent arg0 ) {

		// TODO Auto-generated method stub

	}

	public void focusLost( FocusEvent fevt ) {

		if ( fevt.getSource() == txtVlrParcPag ) {

			calcRetencoes();
			calcTotLiquido();

		}

	}
}
