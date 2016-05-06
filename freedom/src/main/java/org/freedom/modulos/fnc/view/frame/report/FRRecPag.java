/**
 * @version 07/04/2010 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FRReceber.java <BR>
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
 *                    Tela para filtros do relatório de contas a receber/pagar
 * 
 */

package org.freedom.modulos.fnc.view.frame.report;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.acao.RadioGroupEvent;
import org.freedom.acao.RadioGroupListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

public class FRRecPag extends FRelatorio implements RadioGroupListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodBanco = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private JTextFieldFK txtDescBanco = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private ListaCampos lcBanco = new ListaCampos( this );
	
	private JRadioGroup<?, ?> rgTipo = null;

	private boolean bPref = false;
	
	private Vector<String> vValsVenc = new Vector<String>();

	private Vector<String> vLabsVenc = new Vector<String>();


	public FRRecPag() {

		super( false );
		setTitulo( "Receber/Pagar" );
		setAtribos( 40, 50, 328, 225 );

		txtDataini.setVlrDate( new Date() );
		txtDatafim.setVlrDate( new Date() );

		vValsVenc.addElement( "D" );
		vValsVenc.addElement( "S" );
		vLabsVenc.addElement( "Por dia" );
		vLabsVenc.addElement( "Por semana" );
		
		
		rgTipo = new JRadioGroup<String, String>( 1, 2, vLabsVenc, vValsVenc );
		rgTipo.setVlrString( "S" );
		
		montaListaCampos();
		montaRadioGroups();
		montaTela();
		
		
	}

	private void montaListaCampos() {

		lcBanco.add( new GuardaCampo( txtCodBanco, "CodBanco", "Cód.banco.", ListaCampos.DB_PK, false ) );
		lcBanco.add( new GuardaCampo( txtDescBanco, "NomeBanco", "Nome do banco", ListaCampos.DB_SI, false ) );
		lcBanco.montaSql( false, "BANCO", "FN" );
		lcBanco.setReadOnly( true );
		txtCodBanco.setTabelaExterna( lcBanco, null );
		txtCodBanco.setFK( true );
		txtCodBanco.setNomeCampo( "CodBanco" );

	}

	private void montaRadioGroups() {

	}

	private void montaTela() {

		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );

		adic( new JLabelPad( "Periodo:" ), 7, 0, 80, 20 );
		adic( lbLinha, 7, 20, 300, 40 );

		adic( new JLabelPad( "De:", SwingConstants.CENTER ), 17, 30, 30, 20 );
		adic( txtDataini, 47, 30, 80, 20 );

		adic( new JLabelPad( "Até:", SwingConstants.CENTER ), 127, 30, 30, 20 );
		adic( txtDatafim, 157, 30, 80, 20 );

		adic( new JLabelPad( "Cód.banco" ), 7, 65, 80, 20 );
		adic( txtCodBanco, 7, 85, 80, 20 );

		adic( new JLabelPad( "Descrição do banco" ), 90, 65, 215, 20 );
		adic( txtDescBanco, 90, 85, 215, 20 );
		
		adic( new JLabelPad( "Agrupamento" ), 7, 105, 150, 20 );
		adic( rgTipo, 7, 125, 300, 25 );


	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		StringBuilder filtros = new StringBuilder();

		String titulo = null;

		try {

			if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
				Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
				return;
			}

			filtros.append( "Período de " + txtDataini.getVlrString() + " até " + txtDatafim.getVlrString() );

			titulo = "Contas a Receber/Pagar";

			sql.append( "select ip.codemp, ip.codfilial, ip.dtvencitpag vencimento, " );
			sql.append( "cast(sum(ip.vlrapagitpag) as numeric(15,2)) vlrapagar, cast(0 as decimal(15,2)) vlrreceber " );
			sql.append( "from fnitpagar ip, fnpagar pg " );
			sql.append( "where " );
			sql.append( "ip.codemp = ? and ip.codfilial = ? and ip.dtvencitpag between ? and ? " );
			sql.append( "and pg.codemp = ip.codemp and pg.codfilial = ip.codfilial and pg.codpag = ip.codpag " );
			sql.append( "and ip.statusitpag != 'PP' and cast(ip.vlrapagitpag as numeric(15,2)) > 0 " );

			if ( txtCodBanco.getVlrString().length() > 0 ) {
				sql.append( " and pg.codempbo=? and pg.codfilialbo=? and pg.codbanco=? " );
				filtros.append( "\nBanco.: " + txtCodBanco.getVlrString() + " - " + txtDescBanco.getVlrString().trim() );
			}

			sql.append( "group by 1 , 2, 3 " );

			sql.append( "union all " );

			sql.append( "select ir.codemp, ir.codfilial, coalesce(ir.dtprevitrec,ir.dtvencitrec) vencimento, " );
			sql.append( "cast(0 as decimal(15,2)) vlrapagar, " );
			sql.append( "cast(sum(ir.vlrapagitrec) as numeric(15,2)) vlrareceber " );
			sql.append( "from fnitreceber ir " );
			sql.append( "where " );
			sql.append( "ir.codemp = ? and ir.codfilial = ? and ir.dtvencitrec between ? and ? " );
			sql.append( "and ir.statusitrec != 'RP' and cast(ir.vlrapagitrec as numeric(15,2)) > 0 " );

			if ( txtCodBanco.getVlrString().length() > 0 ) {
				sql.append( " and ir.codempbo=? AND ir.codfilialbo=? and ir.codbanco=? " );
			}

			sql.append( "group by 1 , 2, 3 " );

			sql.append( "order by 1, 3, 3 " );

			int iparam = 1;
			ps = con.prepareStatement( sql.toString() );

			ps.setInt( iparam++, Aplicativo.iCodEmp );
			ps.setInt( iparam++, Aplicativo.iCodFilial );

			ps.setDate( iparam++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( iparam++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

			if ( txtCodBanco.getVlrString().length() > 0 ) {
				ps.setInt( iparam++, Aplicativo.iCodEmp );
				ps.setInt( iparam++, ListaCampos.getMasterFilial( "FNBANCO" ) );
				ps.setString( iparam++, txtCodBanco.getVlrString() );
			}

			ps.setInt( iparam++, Aplicativo.iCodEmp );
			ps.setInt( iparam++, Aplicativo.iCodFilial );

			ps.setDate( iparam++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( iparam++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

			if ( txtCodBanco.getVlrString().length() > 0 ) {
				ps.setInt( iparam++, Aplicativo.iCodEmp );
				ps.setInt( iparam++, ListaCampos.getMasterFilial( "FNBANCO" ) );
				ps.setString( iparam++, txtCodBanco.getVlrString() );
			}

			rs = ps.executeQuery();

			imprimirGrafico( bVisualizar, rs, filtros.toString() );

			rs.close();
			ps.close();
			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela de contas a receber/pagar!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	private void imprimirGrafico( final TYPE_PRINT bVisualizar, final ResultSet rs, final String sCab ) {

		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();
		BigDecimal SaldoComposto = null;
		
		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "FNPAGAR" ) );
		hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
		hParam.put( "FILTROS", sCab );
		
		if("S".equals(rgTipo.getVlrString())) {
			hParam.put( "SALDO_COMPOSTO", getSaldoComposto( txtDataini.getVlrDate() ) );
		}
		
		if("S".equals(rgTipo.getVlrString())) {
			hParam.put( "PAGAR_ATRASADO", getPagamentosAtrasados( txtDataini.getVlrDate() ) );
		}

		if("S".equals(rgTipo.getVlrString())) {
			hParam.put( "RECEBER_ATRASADO", getRecebimentosAtrasados( txtDataini.getVlrDate() ) );
		}


		if("S".equals(rgTipo.getVlrString())) {
			dlGr = new FPrinterJob( "layout/rel/REL_REC_PAG_SEMANA.jasper", "Contas a receber/pagar (semanal)", sCab, rs, hParam, this );
		}
		else {
			dlGr = new FPrinterJob( "layout/rel/REL_REC_PAG_01.jasper", "Contas a receber/pagar (diário)", sCab, rs, hParam, this );			
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão do relatório contas a receber/pagar!" + err.getMessage(), true, con, err );
			}
		}
	}
	
	private BigDecimal getSaldoComposto(Date dataini) {
		BigDecimal ret = new BigDecimal(0);
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			sql.append("select coalesce(sum(s.saldosl),0.00) saldo_composto ");
			sql.append("from fnsaldolanca s, fnconta ct ");
			sql.append("where ct.codemppn=s.codemppn and ct.codfilialpn=s.codfilialpn and ct.codplan=s.codplan ");
			sql.append("and s.codemp=? and s.codfilial=? and s.datasl= ");
			sql.append("(select max(s1.datasl) ");
			sql.append("from fnsaldolanca s1 where s1.datasl <= ? and s1.codplan=s.codplan ");
			sql.append("and s1.codemp=s.codemp and s1.codfilial=s.codfilial and s1.codemppn=s.codemppn and s1.codfilialpn=s.codfilialpn and ct.ativaconta='S' ) ");
			
			ps=con.prepareStatement( sql.toString() );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
			ps.setDate( 3, Funcoes.dateToSQLDate( dataini ) );
			
			rs = ps.executeQuery();
			
			if(rs.next()){
				
				ret = rs.getBigDecimal( "saldo_composto" );
				
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	private BigDecimal getPagamentosAtrasados(Date dataini) {
		
		BigDecimal ret = new BigDecimal(0);
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			sql.append( "select ");
			sql.append( "coalesce(sum(it.vlrapagitpag), 0.00) pagar ");
			sql.append( "from fnitpagar it ");
			sql.append( "where ");
			sql.append( "it.codemp=? and it.codfilial=? and it.statusitpag in ('P1','PL') ");
			sql.append( "and it.dtvencitpag < ? ");
			
			ps=con.prepareStatement( sql.toString() );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNITPAGAR" ) );
			ps.setDate( 3, Funcoes.dateToSQLDate( dataini ) );
			
			rs = ps.executeQuery();
			
			if(rs.next()){
				
				ret = rs.getBigDecimal( "pagar" );
				
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	private BigDecimal getRecebimentosAtrasados(Date dataini) {
		
		BigDecimal ret = new BigDecimal(0);
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			sql.append( "select ");
			sql.append( "coalesce(sum(ir.vlritrec), 0.00) receber ");
			sql.append( "from fnitreceber ir ");
			sql.append( "where ");
			sql.append( "ir.codemp=99 and ir.codfilial=1 and ir.statusitrec in ('R1','RL') ");
			sql.append( "and ir.dtvencitrec < ? ");
			
			ps=con.prepareStatement( sql.toString() );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNITPAGAR" ) );
			ps.setDate( 3, Funcoes.dateToSQLDate( dataini ) );
			
			rs = ps.executeQuery();
			
			if(rs.next()){
				
				ret = rs.getBigDecimal( "pagar" );
				
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	
	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcBanco.setConexao( cn );

	}

	public void valorAlterado( RadioGroupEvent evt ) {

	}
}
