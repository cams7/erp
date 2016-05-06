/**
 * @version 03/05/2010 <BR>
 * @author Setpoint Informática Ltda./Cristian Ribeiro Mietlicki<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std.view.frame.report <BR>
 *         Classe:
 * @(#)FRVendasIpi.java <BR>
 * 
 *                      Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                      modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                      na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                      Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                      sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                      Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                      Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                      de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                      Tela de filtros para o relatório IPI.
 * 
 */

package org.freedom.modulos.lvf.view.frame.report;

import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

public class FRPisCofins extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );
	
	private JCheckBoxPad cbEntradas = new JCheckBoxPad( "Entradas", "S", "N" );
	
	private JCheckBoxPad cbSaidas = new JCheckBoxPad( "Saídas", "S", "N" );
	
	private JRadioGroup<?, ?> rgFormato = null;

	public FRPisCofins() {

		super( false );
		setTitulo( "Relatório de PIS e COFINS" );
		setAtribos( 50, 50, 355, 200 );

		montaTela();
	}

	private void montaTela() {

		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );
		JLabelPad lbPeriodo = new JLabelPad( "Periodo:", SwingConstants.CENTER );
		lbPeriodo.setOpaque( true );
		
		
		Vector<String> vLabs1 = new Vector<String>();
		Vector<String> vVals1 = new Vector<String>();

		vLabs1.addElement( "Resumido" );
		vLabs1.addElement( "Detalhado" );
		vVals1.addElement( "R" );
		vVals1.addElement( "D" );
		rgFormato = new JRadioGroup<String, String>( 1, 2, vLabs1, vVals1 );
		rgFormato.setVlrString( "R" );

		adic( lbPeriodo, 15, 5, 80, 20 );
		adic( lbLinha, 7, 15, 320, 45 );

		adic( new JLabelPad( "De:", SwingConstants.CENTER ), 17, 30, 40, 20 );
		adic( txtDataini, 57, 30, 100, 20 );
		adic( new JLabelPad( "Até:", SwingConstants.CENTER ), 157, 30, 45, 20 );
		adic( txtDatafim, 202, 30, 100, 20 );
		
		adic( rgFormato, 7, 63, 320, 30 );
		
		adic( cbEntradas, 57, 95, 120, 20);
		adic( cbSaidas, 179,95,120,20);

		Calendar cPeriodo = Calendar.getInstance();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataini.setVlrDate( cPeriodo.getTime() );
		
		cbEntradas.setVlrString( "S" );
		cbSaidas.setVlrString( "S" );
	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		StringBuffer sCab = new StringBuffer();

		sCab.append( "Perído de : " + Funcoes.dateToStrDate( txtDataini.getVlrDate() ) + "Até : " + Funcoes.dateToStrDate( txtDatafim.getVlrDate() ) );

		try {

			if (rgFormato.getVlrString().equals( "D" )){
				sql = this.getSqlDetalhado();
			}
			else{
				sql = this.getSqlResumido();
			}
			
			ps = con.prepareStatement( sql.toString() );

			int param = 1;

			if("S".equals( cbSaidas.getVlrString())) {
				ps.setInt( param++, Aplicativo.iCodEmp );
			    ps.setInt( param++, Aplicativo.iCodFilial );

			    ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			    ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			}
			if("S".equals( cbEntradas.getVlrString())) {
				ps.setInt( param++, Aplicativo.iCodEmp );
			    ps.setInt( param++, Aplicativo.iCodFilial );

			    ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			    ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			}
			
			rs = ps.executeQuery();

			imprimiGrafico( bVisualizar, rs, sCab.toString() );

			con.commit();

		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemInforma( this, "Erro ao buscar dados do relatório!" );
		}
	}
	
	private StringBuffer getSqlResumido(){
		StringBuffer sql = new StringBuffer();
		if("S".equals( cbSaidas.getVlrString())) {
			sql.append( "select vd.dtemitvenda dtemit, 'S' tipo , sum(coalesce(lfi.vlrbasepis,0)) vlrbasepis, sum(coalesce(lfi.vlrbasecofins,0)) vlrbasecofins, " );
			sql.append( "sum(coalesce(lfi.vlrpis,0)) vlrpis, sum(coalesce(lfi.vlrcofins,0)) vlrcofins " );
			sql.append( "from " );
			sql.append( "vdvenda vd " );
			sql.append( "left outer join  eqtipomov tm on " );
			sql.append( "tm.codemp=vd.codemptm and tm.codfilial=vd.codfilialtm and tm.codtipomov=vd.codtipomov " );
			sql.append( "left outer join  vditvenda iv on " );
			sql.append( "iv.codemp=vd.codemp and iv.codfilial=vd.codfilial and iv.tipovenda=vd.tipovenda and iv.codvenda=vd.codvenda " );
			sql.append( "left outer join eqproduto pd on " );
			sql.append( "pd.codemp=iv.codemppd and pd.codfilial=iv.codfilialpd and pd.codprod=iv.codprod " );
			sql.append( "left outer join lfitvenda lfi on " );
			sql.append( "lfi.codemp=iv.codemp and lfi.codfilial=iv.codfilial and lfi.codvenda=iv.codvenda and lfi.tipovenda=iv.tipovenda and lfi.coditvenda=iv.coditvenda " );
			sql.append( "where " );
			sql.append( "vd.codemp=? and vd.codfilial=? and vd.dtemitvenda between ? AND ? " );
			sql.append( "and tm.fiscaltipomov='S' " );
			sql.append( "group by 1,2 " );				
		}
		
		if("S".equals( cbSaidas.getVlrString()) && "S".equals( cbEntradas.getVlrString())) {
			sql.append( "UNION ALL " );
		}
		
		if("S".equals( cbEntradas.getVlrString())) {
			sql.append( "select cp.dtemitcompra dtemit, 'E' tipo, sum(coalesce(lfi.vlrbasepis,0)) vlrbasepis, sum(coalesce(lfi.vlrbasecofins,0)) vlrbasecofins, " );
			sql.append( "sum(coalesce(lfi.vlrpis,0)) vlrpis, sum(coalesce(lfi.vlrcofins,0)*-1) vlrcofins " );
			sql.append( "from " );
			sql.append( "cpcompra cp " );
			sql.append( "left outer join  eqtipomov tm on " );
			sql.append( "tm.codemp=cp.codemptm and tm.codfilial=cp.codfilialtm and tm.codtipomov=cp.codtipomov " );
			sql.append( "left outer join  cpitcompra ic on " );
			sql.append( "ic.codemp=cp.codemp and ic.codfilial=cp.codfilial and ic.codcompra=cp.codcompra " );
			sql.append( "left outer join eqproduto pd on " );
			sql.append( "pd.codemp=ic.codemppd and pd.codfilial=ic.codfilialpd and pd.codprod=ic.codprod " );
			sql.append( "left outer join lfitcompra lfi on " );
			sql.append( "lfi.codemp=ic.codemp and lfi.codfilial=ic.codfilial and lfi.codcompra=ic.codcompra and lfi.coditcompra=ic.coditcompra " );
			sql.append( "where " );
			sql.append( "cp.codemp=? and cp.codfilial=? and cp.dtemitcompra between ? AND ? " );
			sql.append( "and tm.fiscaltipomov='S' " );

			sql.append( "group by 1,2 " );
		}
		return sql;
	}
	
	private StringBuffer getSqlDetalhado(){
		StringBuffer sql = new StringBuffer();
		if("S".equals( cbSaidas.getVlrString())) {
			sql.append( " select ");
			sql.append( "     CAST('S' AS VARCHAR(1)) AS tipo, ");
			sql.append( "     vd.codcli as codpessoa, ");
			sql.append( "     coalesce (vc.nomecli, vc.razcli) as nomepessoa, ");
			sql.append( "     vd.codvenda as codlanc, ");
			sql.append( "     vd.dtemitvenda dtemit, ");
			sql.append( "     vd.vlrliqvenda as vlrliq, ");
			sql.append( "     vd.vlrprodvenda as vlrtot, ");
			sql.append( "     sum(coalesce(lfi.vlrbasepis,0)) vlrbasepis, ");
			sql.append( "     sum(coalesce(lfi.vlrbasecofins,0)) vlrbasecofins, ");
			sql.append( "     sum(coalesce(lfi.vlrpis,0)) vlrpis, ");
			sql.append( "     sum(coalesce(lfi.vlrcofins,0)) vlrcofins ");
			sql.append( " from vdvenda vd ");
			sql.append( " left outer join  eqtipomov tm ");
			sql.append( "     on tm.codemp=vd.codemptm ");
			sql.append( "    and tm.codfilial=vd.codfilialtm ");
			sql.append( "    and tm.codtipomov=vd.codtipomov ");
			sql.append( " left outer join  vditvenda iv ");
			sql.append( "     on iv.codemp=vd.codemp and iv.codfilial=vd.codfilial ");
			sql.append( "    and iv.tipovenda=vd.tipovenda ");
			sql.append( "    and iv.codvenda=vd.codvenda ");
			sql.append( " left outer join eqproduto pd ");
			sql.append( "     on pd.codemp=iv.codemppd and pd.codfilial=iv.codfilialpd ");
			sql.append( "    and pd.codprod=iv.codprod ");
			sql.append( " left outer join lfitvenda lfi ");
			sql.append( "     on lfi.codemp=iv.codemp ");
			sql.append( "    and lfi.codfilial=iv.codfilial ");
			sql.append( "    and lfi.codvenda=iv.codvenda ");
			sql.append( "    and lfi.tipovenda=iv.tipovenda ");
			sql.append( "    and lfi.coditvenda=iv.coditvenda ");
			sql.append( "   left outer join vdcliente vc ");
			sql.append( "     on vc.codcli = vd.codcli ");
			sql.append( "    and vc.codemp = vd.codempcl ");
			sql.append( "    and vc.codfilial = vd.codfilialcl ");
			sql.append( "  where vd.codemp=? ");
			sql.append( "    and vd.codfilial=? ");
			sql.append( "    and vd.dtemitvenda between ? AND ? ");
			sql.append( "    and tm.fiscaltipomov='S' ");
			sql.append( "    and vd.statusvenda<>'X' ");
			sql.append( "  group by 1, 2, 3, 4, 5, 6, 7 ");
		}
		
		if("S".equals( cbSaidas.getVlrString()) && "S".equals( cbEntradas.getVlrString())) {
			sql.append( "UNION ALL " );
		}
		
		if("S".equals( cbEntradas.getVlrString())) {
			sql.append(" select ");
			sql.append("     CAST('E' AS VARCHAR(1)) AS tipo, ");
			sql.append("     cp.codfor as codpessoa, ");
			sql.append("     cf.nomefor as nomepessoa, ");
			sql.append("     cp.codcompra as codlanc, ");
			sql.append("     cp.dtemitcompra dtemit, ");
			sql.append("     cp.vlrliqcompra as vlrliq, ");
			sql.append("     cp.vlrprodcompra as vlrtot, ");
			sql.append("     sum(coalesce(lfi.vlrbasepis,0)) vlrbasepis, ");
			sql.append("     sum(coalesce(lfi.vlrbasecofins,0)) vlrbasecofins, ");
			sql.append("     sum(coalesce(lfi.vlrpis,0)) vlrpis, ");
			sql.append("     sum(coalesce(lfi.vlrcofins,0)*-1) vlrcofins ");
			sql.append("   from cpcompra cp ");
			sql.append("   left outer join  eqtipomov tm ");
			sql.append("     on tm.codemp=cp.codemptm ");
			sql.append("    and tm.codfilial=cp.codfilialtm ");
			sql.append("    and tm.codtipomov=cp.codtipomov ");
			sql.append("   left outer join  cpitcompra ic ");
			sql.append("     on ic.codemp=cp.codemp ");
			sql.append("    and ic.codfilial=cp.codfilial ");
			sql.append("    and ic.codcompra=cp.codcompra ");
			sql.append("   left outer join eqproduto pd ");
			sql.append("     on pd.codemp=ic.codemppd ");
			sql.append("    and pd.codfilial=ic.codfilialpd ");
			sql.append("    and pd.codprod=ic.codprod ");
			sql.append("   left outer join lfitcompra lfi ");
			sql.append("     on lfi.codemp=ic.codemp ");
			sql.append("    and lfi.codfilial=ic.codfilial ");
			sql.append("    and lfi.codcompra=ic.codcompra ");
			sql.append("    and lfi.coditcompra=ic.coditcompra ");
			sql.append("   left outer join cpforneced cf ");
			sql.append("     on cf.codfor = cp.codfor ");
			sql.append("    and cf.codemp = cp.codempfr ");
			sql.append("    and cf.codfilial = cp.codfilialfr ");
			sql.append("  where cp.codemp=? ");
			sql.append("    and cp.codfilial=? ");
			sql.append("    and cp.dtemitcompra between ? AND ? ");
			sql.append("    and tm.fiscaltipomov='S' ");
			sql.append( "    and cp.statuscompra<>'X' ");
			sql.append("  group by 1, 2, 3, 4, 5, 6, 7 ");
		}
		return sql;
	}

	private void imprimiGrafico( final TYPE_PRINT bVisualizar, final ResultSet rs, final String sCab ) {
		
		String pathReportFile = "layout/rel/REL_PIS_COFINS.jasper";
		if (rgFormato.getVlrString().equals( "D" )){
			pathReportFile = "layout/rel/REL_PIS_COFINS_DET.jasper";
		}

		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "VDVENDA" ) );
		hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
		hParam.put( "FILTROS", sCab );

		dlGr = new FPrinterJob( pathReportFile, "Relatório de PIS/COFINS ", sCab, rs, hParam, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão do relatório!" + err.getMessage(), true, con, err );
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
	}
}
