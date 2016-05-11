/**
 * @version 28/09/2011 <BR>
 * @author Setpoint Informática Ltda./ Bruno Nascimento<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.tmk <BR>
 *         Classe: @(#)FRSitContr.java <BR>
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
 *         Relatório Situação Projetos/Contratos.
 * 
 */

package org.freedom.modulos.crm.view.frame.report;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.atd.view.frame.crud.tabbed.FAtendente;

public class FRPremiacoes extends FRelatorio {

	private static final long serialVersionUID = 1L;
	
	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );
	
	private ListaCampos lcAtendente = new ListaCampos( this, "AE" );
	
	private JTextFieldPad txtCodAtend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeAtend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldPad txtMetaAtend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 15, 5 );

	
	public FRPremiacoes() {		
		setTitulo( "Relatório de premiação" );
		setAtribos( 80, 80, 410	, 300 );
		
		montaListaCampos();
		montaTela();
		
	}
	
	public void setParametros( Integer codcli, Date dtini, Date dtfim ) {

		txtDataini.setVlrDate( dtini );
		txtDatafim.setVlrDate( dtfim );
		
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
		
		adic( txtCodAtend, 7, 80, 80, 20, "Cod.Atend" );
		adic( txtNomeAtend, 90, 80, 215, 20, "Nome do atendente");
		
		Calendar cPeriodo = Calendar.getInstance();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
	
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataini.setVlrDate( cPeriodo.getTime() );

		txtDataini.setRequerido( true );
		txtDatafim.setRequerido( true );
	
	}
	
	private void montaListaCampos() {
		 
		//Atendente
		lcAtendente.add( new GuardaCampo( txtCodAtend, "CodAtend", "Cód.atend.", ListaCampos.DB_PK, true ) );
		lcAtendente.add( new GuardaCampo( txtNomeAtend, "NomeAtend", "Nome", ListaCampos.DB_SI, false ) );
		lcAtendente.add( new GuardaCampo( txtMetaAtend, "MetaAtend", "Meta Atendimento", ListaCampos.DB_SI, false ) );
		lcAtendente.montaSql( false, "ATENDENTE", "AT" );
		lcAtendente.setReadOnly( true );
		txtCodAtend.setTabelaExterna( lcAtendente, FAtendente.class.getCanonicalName() );
		txtCodAtend.setFK( true );
		txtCodAtend.setNomeCampo( "CodAtend" );	
	}
	
	public void imprimir( TYPE_PRINT bVisualizar ) {

		Blob fotoemp = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sCab = "";
		String Ordem = "";
		StringBuilder sql = new StringBuilder();
		
		if(txtCodAtend.getVlrInteger() == 0){
			Funcoes.mensagemInforma( this, "Selecione um atendente!" );
			return;
		}
		
		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data inicial maior que a data final!" );
			return;
		}
		
		try {
			ps = con.prepareStatement( "SELECT FOTOEMP FROM SGEMPRESA WHERE CODEMP=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );

			rs = ps.executeQuery();
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
		
		sCab = txtCodAtend.getVlrInteger().toString() + " - " + txtNomeAtend.getVlrString() + " - Período de " + txtDataini.getVlrString()  + " a " +  txtDatafim.getVlrString();
	
		sql.append( "select a.codemp, a.codfilial, a.codempct,  a.codfilialct, a.codcontr, c.desccontr, ");
		sql.append( "a.coditcontr, ic.descitcontr, a.codempae, a.codfilialae, a.codatend, a.nomeatend, ");
		sql.append( "a.perccomiespec perccomi, ((select sum(a2.totalgeral) from atatendimentovw02 a2 ");
		sql.append( "where a2.codempct=a.codempct and a2.codfilialct=a.codfilialct and a2.codcontr=a.codcontr and a2.coditcontr=a.coditcontr ) ) tothtrab, ");
		sql.append( "((select sum( a5.qtditvenda ) from atatendimentovw05 a5 where a5.codempct=a.codempct and a5.codfilialct=a.codfilialct and ");
		sql.append( "a5.codcontr=a.codcontr and a5.coditcontr=a.coditcontr)) qtdvd, ");
		sql.append( "((select sum( a5.vlrliqitvenda ) from atatendimentovw05 a5 ");
		sql.append( "where a5.codempct=a.codempct and a5.codfilialct=a.codfilialct and ");
		sql.append( "a5.codcontr=a.codcontr and a5.coditcontr=a.coditcontr)) vlrliqvd, ");
		sql.append( "sum(a.totalgeral) tothtrabgeral, ");
		sql.append( "sum(a.totalcomis) tothtrabatend from ");
		sql.append( "vdcontrato c, vditcontrato ic, atatendimentovw02 a, vdfincontr fn ");
		sql.append( "where a.codemp=? and a.codfilial=? and ");
		sql.append( "c.codemp=a.codempct and c.codfilial=a.codfilialct and ic.codemp=c.codemp and ");
		sql.append( "ic.codfilial=c.codfilial and ic.codcontr=c.codcontr and a.codempct=c.codemp and ");
		sql.append( "a.codfilialct=c.codfilial and a.codcontr=c.codcontr and a.coditcontr=ic.coditcontr and ");
		sql.append( "c.tpcobcontr='ES' and fn.codemp=a.codempct and fn.codfilial=a.codfilialct and ");
		sql.append( "fn.codcontr=a.codcontr and a.codempae=? and a.codfilialae=? and a.codatend=? and ");
		sql.append( "fn.dtfincontr between ? and ? and (  select sum( am.totalmeta ) ");
		sql.append( "from atatendimentovw02 am where am.codemp=a.codemp and am.codfilial=a.codfilial and ");
		sql.append( "am.dataatendo between ? and ? and am.codempae=a.codempae and ");
		sql.append( "am.codfilialae=a.codfilialae and am.codatend=a.codatend ) >= ? group by ");
		sql.append( "1,2,3,4,5,6,7,8,9,10,11,12,13 ");
		sql.append( "union all ");
		sql.append( "select a.codemp, a.codfilial, a.codempct,  a.codfilialct, a.codcontr, c.desccontr, ");
		sql.append( "a.coditcontr, ic.descitcontr, a.codempae, a.codfilialae, a.codatend, a.nomeatend, ");
		sql.append( "a.perccomiespec perccomi, ((select sum(a2.totalgeral) ");
		sql.append( "from atatendimentovw02 a2 where a2.codempct=a.codempct and ");
		sql.append( "a2.codfilialct=a.codfilialct and a2.codcontr=a.codcontr and a2.coditcontr=a.coditcontr and ");
		sql.append( "a2.dataatendo between ? and ? ) ) tothtrab, ");
		sql.append( "((select sum( a5.qtditvenda ) from atatendimentovw05 a5 where a5.codempct=a.codempct and ");
		sql.append( "a5.codfilialct=a.codfilialct and a5.codcontr=a.codcontr and a5.coditcontr=a.coditcontr and ");
		sql.append( "a5.dtfinapura between ? and ? )) qtdvd, ");
		sql.append( "((select sum( a5.vlrliqitvenda ) from atatendimentovw05 a5 ");
		sql.append( "where a5.codempct=a.codempct and a5.codfilialct=a.codfilialct and a5.codcontr=a.codcontr and ");
		sql.append( "a5.coditcontr=a.coditcontr and a5.dtfinapura between ? and ? )) vlrliqvd, ");
		sql.append( "sum(a.totalgeral) tothtrabgeral, ");
		sql.append( "sum(a.totalcomis) tothtrabatend from vdcontrato c, vditcontrato ic, atatendimentovw02 a ");
		sql.append( "where a.codemp=? and a.codfilial=? and c.codemp=a.codempct and c.codfilial=a.codfilialct and ");
		sql.append( "ic.codemp=c.codemp and ic.codfilial=c.codfilial and ic.codcontr=c.codcontr and ");
		sql.append( "a.codempct=c.codemp and a.codfilialct=c.codfilial and a.codcontr=c.codcontr and ");
		sql.append( "a.coditcontr=ic.coditcontr and c.tpcobcontr='ME' and ");
		sql.append( "a.codempae=? and a.codfilialae=? and a.codatend=? and ");
		sql.append( "a.dataatendo between ? and ? and ");
		sql.append( "(  select sum( am.totalmeta ) from atatendimentovw02 am ");
		sql.append( "where am.codemp=a.codemp and am.codfilial=a.codfilial and ");
		sql.append( "am.dataatendo between ? and ? and ");
		sql.append( "am.codempae=a.codempae and am.codfilialae=a.codfilialae and am.codatend=a.codatend) ");
		sql.append( ">= ? ");
		sql.append( "group by 1,2,3,4,5,6,7,8,9,10,11,12,13 ");
		sql.append( "order by 6, 8" );
		try{

			ps = con.prepareStatement( sql.toString() );
		
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "ATATENDIMENTO" ) );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "ATATENDENTE" ) );
			ps.setInt( 5, txtCodAtend.getVlrInteger().intValue());
			ps.setDate( 6, Funcoes.strDateToSqlDate( txtDataini.getVlrString() ) );
			ps.setDate( 7, Funcoes.strDateToSqlDate( txtDatafim.getVlrString() ) );	
			ps.setDate( 8, Funcoes.strDateToSqlDate( txtDataini.getVlrString() ) );
			ps.setDate( 9, Funcoes.strDateToSqlDate( txtDatafim.getVlrString() ) );
			ps.setBigDecimal( 10, txtMetaAtend.getVlrBigDecimal() ); // meta para premiação
			ps.setDate( 11, Funcoes.strDateToSqlDate( txtDataini.getVlrString() ) );
			ps.setDate( 12, Funcoes.strDateToSqlDate( txtDatafim.getVlrString() ) );
			ps.setDate( 13, Funcoes.strDateToSqlDate( txtDataini.getVlrString() ) );
			ps.setDate( 14, Funcoes.strDateToSqlDate( txtDatafim.getVlrString() ) );
			ps.setDate( 15, Funcoes.strDateToSqlDate( txtDataini.getVlrString() ) );
			ps.setDate( 16, Funcoes.strDateToSqlDate( txtDatafim.getVlrString() ) );
			ps.setInt( 17, Aplicativo.iCodEmp );
			ps.setInt( 18, ListaCampos.getMasterFilial( "ATATENDIMENTO" ) );

			ps.setInt( 19, Aplicativo.iCodEmp );
			ps.setInt( 20, ListaCampos.getMasterFilial( "ATATENDENTE" ) );
			ps.setInt( 21, txtCodAtend.getVlrInteger());
			ps.setDate( 22, Funcoes.strDateToSqlDate( txtDataini.getVlrString() ) );
			ps.setDate( 23, Funcoes.strDateToSqlDate( txtDatafim.getVlrString() ) );
			
			ps.setDate( 24, Funcoes.strDateToSqlDate( txtDataini.getVlrString() ) );
			ps.setDate( 25, Funcoes.strDateToSqlDate( txtDatafim.getVlrString() ) );

			ps.setBigDecimal( 26, txtMetaAtend.getVlrBigDecimal() );// meta para premiação
			
			rs = ps.executeQuery();

		} catch (Exception err) {
			Funcoes.mensagemErro( this, "Erro consulta Relatório de premiação!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
		
		imprimiGrafico( bVisualizar, rs,  sCab, fotoemp );

	}

	private void imprimiGrafico( TYPE_PRINT bVisualizar, ResultSet rs, String sCab, Blob fotoemp) {
		String report = "layout/rel/REL_PREMIACAO.jasper";
		String label = "Relatório de Premiação";
		
	    HashMap<String, Object> hParam = new HashMap<String, Object>();

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
				Funcoes.mensagemErro( this, "Erro na impressão de relatório de Premiação!" + err.getMessage(), true, con, err );
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcAtendente.setConexao( cn );
	}

}
