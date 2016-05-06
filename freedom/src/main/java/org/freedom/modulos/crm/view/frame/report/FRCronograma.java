/**
 * @version 28/09/2011 <BR>
 * @author Setpoint Informática Ltda. / Bruno Nascimento<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.tmk <BR>
 *         Classe: @(#)FRCronograma.java <BR>
 * 
 *         Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *         modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *         na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *         Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *         sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *         Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *         Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *         de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *         Relatório Cronograma Sintético.
 * 
 */

package org.freedom.modulos.crm.view.frame.report;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.infra.pojos.Constant;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.crm.dao.DAOGestaoProj;
import org.freedom.modulos.crm.view.frame.crud.detail.FContrato;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCliente;

public class FRCronograma extends FRelatorio implements CarregaListener{

	private static final long serialVersionUID = 1L;
	
	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );
	
	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtCodCli2 = new JTextFieldFK( JTextFieldFK.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtStatus = new JTextFieldPad( JTextFieldPad.TP_STRING, 100, 0 );
	
	private JTextFieldPad txtCodContr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtDescContr = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldFK txtContHSubContr = new JTextFieldFK( JTextFieldFK.TP_STRING, 2, 0 );
	
	private JTextFieldPad txtIndice = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );
	
	//Checkbox

	private final JCheckBoxPad cbTipoIC = new JCheckBoxPad( "Item ct.", "S", "N" );
	
	private final JCheckBoxPad cbTipoSC = new JCheckBoxPad( "Sub-ct.", "S", "N" );

	private final JCheckBoxPad cbTipoIS = new JCheckBoxPad( "Item s.c.", "S", "N" );

	private final JCheckBoxPad cbTipoTA = new JCheckBoxPad( "Tarefa", "S", "N" );
	
	private final JCheckBoxPad cbTipoST = new JCheckBoxPad( "Sub-tarefa", "S", "N" );
	
	private Vector<String> vLabsSaldo = new Vector<String>();
	
	private Vector<String> vValsSaldo = new Vector<String>();
	
	private JRadioGroup<String, String> rgSaldoHoras = null;

	private ListaCampos lcCli = new ListaCampos( this );
	
	private ListaCampos lcContr = new ListaCampos( this );
	
	private DAOGestaoProj daogestao = null;
	
	public FRCronograma() {		
		setTitulo( "Cronograma Sintético" );
		setAtribos( 80, 80, 410	, 280 );
		
		montaListaCampos();
		montaTela();
		
	}
	
	private void montaTela(){
		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );
		JLabelPad lbPeriodo = new JLabelPad( "Período:", SwingConstants.CENTER );
		lbPeriodo.setOpaque( true );

		adic( lbPeriodo, 7, 1, 80, 20 );
		adic( lbLinha, 5, 10, 300, 45 );
		
		adic( new JLabelPad( "De:" ), 15, 25, 25, 20 );
		adic( txtDataini, 42, 25, 95, 20 );
		adic( new JLabelPad( "Até:" ), 148, 25, 25, 20 );
		adic( txtDatafim, 178, 25, 95, 20 );
		
		adic( txtCodCli, 7, 80, 80, 20, "Cod.Cli" );
		adic( txtRazCli, 90, 80, 225, 20, "Razão social do cliente" );
		adic( txtCodContr, 7, 120, 80, 20, "Cod.Contr");
		adic( txtDescContr, 90, 120, 225, 20, "Descrição do Contrato" );
		adic( txtIndice, 7, 160, 80, 20, "Indice" );
		adic( cbTipoIC, 90, 160, 80,  20 );
		adic( cbTipoSC, 173, 160, 80, 20 );
		adic( cbTipoIS, 7, 180, 80, 20 );
		adic( cbTipoTA, 90, 180, 80, 20 );
		adic( cbTipoST, 173, 180, 90, 20 );
		
		Calendar cPeriodo = Calendar.getInstance();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
	
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataini.setVlrDate( cPeriodo.getTime() );
		
		cbTipoIC.setVlrString( "S" );
		cbTipoIS.setVlrString( "S" );
		cbTipoSC.setVlrString( "S" );
		cbTipoTA.setVlrString( "S" );
		cbTipoST.setVlrString( "S" );
	}
	
	
	private void montaListaCampos() {
		
		//cliente
		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, true ) );
		lcCli.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCli.montaSql( false, "CLIENTE", "VD" );
		lcCli.setReadOnly( true );
		txtCodCli.setTabelaExterna( lcCli, FCliente.class.getCanonicalName() );
		txtCodCli.setFK( true );
		txtCodCli.setNomeCampo( "CodCli" );
		
		// Contrato

		lcContr.add( new GuardaCampo( txtCodContr, "CodContr", "Cód.Contr.", ListaCampos.DB_PK, true ) );
		lcContr.add( new GuardaCampo( txtDescContr, "DescContr", "Descrição do contrato", ListaCampos.DB_SI, false ) );
		lcContr.add( new GuardaCampo( txtContHSubContr, "ContHSubContr", "Cont.HSubContr.", ListaCampos.DB_SI, false ) );
		lcContr.add( new GuardaCampo( txtCodCli2, "CodCli", "Cód.cli", ListaCampos.DB_SI, false ) );
		lcContr.setDinWhereAdic( "CodCli=#N", txtCodCli );
		lcContr.montaSql( false, "CONTRATO", "VD" );
		lcContr.setReadOnly( true );
	
		txtCodContr.setTabelaExterna( lcContr, FContrato.class.getCanonicalName() );
		txtCodContr.setFK( true );
		txtCodContr.setNomeCampo( "CodContr" );
		
		lcCli.addCarregaListener( this );
		lcContr.addCarregaListener( this );
		
		
	}

	public void imprimir( TYPE_PRINT bVisualizar ) {
		
		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data inicial maior que a data final!" );
			return;
		}
		
		String sCab = "";
		String sTitle = "";
		String Ordem = "";
		//StringBuilder sql = null;
		Blob fotoemp = null;

		try {
			PreparedStatement ps = con.prepareStatement( "SELECT FOTOEMP FROM SGEMPRESA WHERE CODEMP=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				fotoemp = rs.getBlob( "FOTOEMP" );
			}
			rs.close();
			ps.close();
			con.commit();

		} catch (Exception e) {
			Funcoes.mensagemErro( this, "Erro carregando logotipo.\n" + e.getMessage() );
			e.printStackTrace();
		}	
		
		sCab = txtCodCli.getVlrInteger().toString() + " - " + txtRazCli.getVlrString() + " \n" + "Período de " + txtDataini.getVlrString()  + " a " +  txtDatafim.getVlrString();
		sTitle = txtCodContr.getVlrInteger().toString() + " - " + txtDescContr.getVlrString();
		ResultSet rs = null;

		try{
			Constant[] filtroTipo = DAOGestaoProj.getFiltroTipo( "N", cbTipoIC.getVlrString(), cbTipoSC.getVlrString(), cbTipoIS.getVlrString(),
					cbTipoTA.getVlrString(), cbTipoST.getVlrString() );
			
			
			PreparedStatement ps = con.prepareStatement( daogestao.getQueryContr( txtContHSubContr.getVlrString(), txtIndice.getVlrString(), filtroTipo) );
			daogestao.setParamsQueryContr( ps, txtDataini.getVlrDate(),txtDatafim.getVlrDate(),Aplicativo.iCodEmp , 
					ListaCampos.getMasterFilial( "VDCONTRATO" ) , txtCodContr.getVlrInteger(), txtIndice.getVlrString(), filtroTipo);
			rs = ps.executeQuery();

		} catch (Exception err) {
			Funcoes.mensagemErro( this, "Erro consulta Cronograma Sintético\n" + err.getMessage(), true, con, err );
		}

		imprimiGrafico( bVisualizar, rs,  sCab, sTitle, fotoemp );

	}

	private void imprimiGrafico( TYPE_PRINT bVisualizar, ResultSet rs, String sCab, String sTitle, Blob fotoemp) {
		String report = "layout/rel/REL_CRONOGRAMA_01.jasper";
		String label = "Cronograma Sintético";
		
	    HashMap<String, Object> hParam = new HashMap<String, Object>();
		hParam.put( "SUBREPORT_DIR", "org/freedom/layout/rel/" );
		hParam.put( "CODEMP", new Integer(Aplicativo.iCodEmp) );
		hParam.put( "CODFILIAL", new Integer(ListaCampos.getMasterFilial( "VDCONTRATO" )) );
		hParam.put( "CODCONTR", txtCodContr.getVlrInteger() );
		hParam.put( "TITULO", sTitle );
		
	    try {
			hParam.put( "LOGOEMP",  new ImageIcon(fotoemp.getBytes(1, ( int ) fotoemp.length())).getImage() );
	
		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro carregando logotipo !\n" + e.getMessage()  );
			e.printStackTrace();
		}
		
		FPrinterJob dlGr = new FPrinterJob( report, label, sCab, rs, hParam , this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		} else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão do Cronograma Sintético!" + err.getMessage(), true, con, err );
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcCli.setConexao( cn );
		lcContr.setConexao( cn );
		
		daogestao = new DAOGestaoProj( cn );
	}

	public void afterCarrega( CarregaEvent cevt ) {
		if(cevt.getListaCampos() == lcContr ){
			if( txtCodContr.getVlrInteger() !=  txtCodCli2.getVlrInteger() ){
				txtCodCli.setVlrInteger( txtCodCli2.getVlrInteger() );
				lcCli.carregaDados();
			}	
		}
	}

	public void beforeCarrega( CarregaEvent cevt ) {

	}

}
