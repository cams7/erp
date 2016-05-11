/**
 * @version 12/08/2004 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FRPisCofins.java <BR>
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
 *         Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.lvf.view.frame.report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Vector;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.acao.RadioGroupEvent;
import org.freedom.acao.RadioGroupListener;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.component.ImprimeOS;
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

public class FRMovPisCofins extends FRelatorio implements RadioGroupListener  {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JCheckBoxPad cbSemMov = new JCheckBoxPad( "Ocultar produtos sem movimento", "S", "N" );

	private JRadioGroup<?, ?> rgPis = null;

	private Vector<String> vPisLab = new Vector<String>();

	private Vector<String> vPisVal = new Vector<String>();

	private Vector<String> vCofinsLab = new Vector<String>();

	private Vector<String> vCofinsVal = new Vector<String>();

	private Vector<String> tiporelLab = new Vector<String>();

	private Vector<String> tiporelVal = new Vector<String>();
	
	private JRadioGroup<?, ?> rgCofins = null;

	private JRadioGroup<?, ?> rgFinanceiro = null;
	
	private JRadioGroup<String, String> rgTiporel = null;

	public FRMovPisCofins() {

		setTitulo( "Pis e cofins" );
		setAtribos( 80, 30, 500, 380 );

		GregorianCalendar cal = new GregorianCalendar();
		cal.add( Calendar.DATE, -30 );
		txtDataini.setVlrDate( cal.getTime() );
		cal.add( Calendar.DATE, 30 );
		txtDatafim.setVlrDate( cal.getTime() );
		txtDataini.setRequerido( true );
		txtDatafim.setRequerido( true );

		vPisLab.addElement( "Tributado" );
		vPisLab.addElement( "Isento" );
		vPisLab.addElement( "Sub. Trib." );
		vPisLab.addElement( "Não Somar" );
		vPisVal.addElement( "T" );
		vPisVal.addElement( "I" );
		vPisVal.addElement( "S" );
		vPisVal.addElement( "N" );

		rgPis = new JRadioGroup<String, String>( 1, 4, vPisLab, vPisVal );

		vCofinsLab.addElement( "Tributado" );
		vCofinsLab.addElement( "Isento" );
		vCofinsLab.addElement( "Sub. Trib." );
		vCofinsLab.addElement( "Não Somar" );
		vCofinsVal.addElement( "T" );
		vCofinsVal.addElement( "I" );
		vCofinsVal.addElement( "S" );
		vCofinsVal.addElement( "N" );

		rgCofins = new JRadioGroup<String, String>( 1, 4, vCofinsLab, vCofinsVal );

		Vector<String> vFinLab = new Vector<String>();
		Vector<String> vFinVal = new Vector<String>();

		vFinLab.addElement( "Financeiro" );
		vFinLab.addElement( "Não Finaceiro" );
		vFinLab.addElement( "Ambos" );
		vFinVal.addElement( "S" );
		vFinVal.addElement( "N" );
		vFinVal.addElement( "A" );

		rgFinanceiro = new JRadioGroup<String, String>( 1, 3, vFinLab, vFinVal );
		rgFinanceiro.setVlrString( "S" );
		
		tiporelLab.addElement( "Gráfico" );
		tiporelLab.addElement( "Texto" );
		tiporelVal.addElement( "G" );
		tiporelVal.addElement( "T" );
		
		
		rgTiporel = new JRadioGroup<String, String>( 1, 2, tiporelLab, tiporelVal );
		

		adic( new JLabelPad( "Período:" ), 7, 0, 250, 20 );
		adic( txtDataini, 7, 20, 100, 20 );
		adic( txtDatafim, 110, 20, 100, 20 );

		adic( new JLabelPad( "Pis:" ), 7, 40, 250, 20 );
		adic( rgPis, 7, 60, 420, 30 );
		adic( new JLabelPad( "Cofins:" ), 7, 90, 250, 20 );
		adic( rgCofins, 7, 110, 420, 30 );
		adic( new JLabelPad( "Financeiro:" ), 7, 150, 250, 20 );
		adic( rgFinanceiro, 7, 170, 420, 30 );
		adic( new JLabelPad( "Tipo de impressão:" ), 7, 210, 250, 20 );
		adic( rgTiporel, 7, 230, 420, 30 );

		
		adic( cbSemMov, 7, 280, 420, 30 );

		cbSemMov.setVlrString( "S" );
		
		rgPis.setAtivo( false );
		rgCofins.setAtivo( false );
		cbSemMov.setEnabled( false );
		rgTiporel.addRadioGroupListener( this );


	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		String sPis = "";
		String sCofins = "";
		try {
			if ( ( txtDataini.getVlrString().length() < 10 ) || ( txtDatafim.getVlrString().length() < 10 ) ) {
				Funcoes.mensagemInforma( this, "Período inválido!" );
				return;
			}
			sPis = rgPis.getVlrString();
			sCofins = rgCofins.getVlrString();
			if ( ( sPis.equals( "N" ) ) && ( sCofins.equals( "N" ) ) ) {
				Funcoes.mensagemInforma( this, "Selecione PIS ou COFINS!" );
				return;
			}
			if ( "G".equals( rgTiporel.getVlrString() ) ) {
				imprimeGrafico( bVisualizar, sPis, sCofins );
			} else {
				imprimeTexto( bVisualizar, sPis, sCofins );
			}
		} finally {
			sPis = null;
			sCofins = null;
		}
	}


	private void imprimeGrafico(final TYPE_PRINT visualizar, final String pis, final String cofins ) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		StringBuilder where = new StringBuilder();
		StringBuilder wherefin = new StringBuilder();
		StringBuilder filtros = new StringBuilder();
		int param = 1;

		try {

			sql = getSqlDetalhado( pis, cofins, filtros, where, wherefin );
			ps = con.prepareStatement( sql.toString() );
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "VDVENDA" ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			rs = ps.executeQuery();
			
			String pathReportFile = "relatorios/MovPisCofins.jasper";

			FPrinterJob dlGr = null;
			HashMap<String, Object> hParam = new HashMap<String, Object>();

			hParam.put( "CODEMP", Aplicativo.iCodEmp );
			hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "VDVENDA" ) );
			hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
			hParam.put( "FILTROS", filtros.toString() );

			dlGr = new FPrinterJob( pathReportFile, "Relatório de PIS/COFINS ", filtros.toString(), rs, hParam, this );

			if ( visualizar==TYPE_PRINT.VIEW ) {
				dlGr.setVisible( true );
			}
			else {
				try {
					JasperPrintManager.printReport( dlGr.getRelatorio(), true );
				} catch ( Exception err ) {
					Funcoes.mensagemErro( this, "Erro na impressão do relatório!" + err.getMessage(), true, con, err );
				}
			}
		} catch (SQLException e) {
			Funcoes.mensagemErro( this, "Erro executando a consulta !\n"+e.getMessage() );
		} finally {
			where = null;
			sql = null;
			filtros = null;
			ps = null;
			rs = null;
			System.gc();
		}
	}
	
	private StringBuilder getSqlDetalhado(final String pis, final String cofins, StringBuilder filtros, StringBuilder where, StringBuilder wherefin ){

		/*
		if ( !pis.equals( "N" ) ) {
			filtros.append( "( PIS " );

			if ( pis.equals( "T" ) )
				filtros.append( "TRIBUTADO )" );
			else if ( pis.equals( "I" ) )
				filtros.append(  "ISENTO )" );
			else if ( pis.equals( "S" ) )
				filtros.append( "SUBSTITUICAO )" );

			where.append( " and ( itcf.sitpisfisc='" + pis + "'" );
		}
		if ( !cofins.equals( "N" ) ) {
			filtros.append( ( filtros.equals( "" ) ? "" : " + " ) + "( COFINS " );

			if ( cofins.equals( "T" ) )
				filtros.append(  "TRIBUTADO )" );
			else if ( cofins.equals( "I" ) )
				filtros.append( "ISENTO )" );
			else if ( cofins.equals( "S" ) )
				filtros.append( "SUBSTITUICAO )" );

			where.append( ( where.length()==0 ? " and ( " : " or " ) + " itcf.sitcofinsfisc='" + cofins + "'" );
		}
		if ( where.length()>0 )
			where.append( " ) ");
*/
		if ( "S".equals( rgFinanceiro.getVlrString() ) ) {
			wherefin.append(  " and tm.somavdtipomov='S' " );
		}
		else if ( "N".equals( rgFinanceiro.getVlrString() ) ) {
			wherefin.append( " and tm.somavdtipomov='N' " );
		}
		else if ( "A".equals( rgFinanceiro.getVlrString() ) ) {
			wherefin.append( " and tm.somavdtipomov in ('S','N') " );
		}

		StringBuilder sql = new StringBuilder();
		sql.append( " select ");
		sql.append( "     cast(pd.descprod as varchar(100)) descprod, ");
		sql.append( "     cast('E' as varchar(1)) tipo, ");
		sql.append( "     cast(ic.codfisc as char(13)) codfisc, cast(ic.coditfisc as integer) coditfisc, ");
		sql.append( "     cast(itcf.codsittribpis as char(2)) codsittribpis, cast(itcf.codsittribcof as char(2)) codsittribcof, ");
		sql.append( "     cast(sum(ic.vlrliqitcompra) as decimal(15,5)) vlrliq, ");
		sql.append( "     cast(sum(ic.vlrproditcompra) as decimal(15,5)) vlrtot, ");
		sql.append( "     cast(sum(coalesce(lfi.vlrbasepis,0)) as decimal(15,5)) vlrbasepis, ");
		sql.append( "     cast(sum(coalesce(lfi.vlrbasecofins,0)) as decimal(15,5))vlrbasecofins, ");
		sql.append( "     cast(sum(coalesce(lfi.vlrpis,0)) as decimal(15,5)) vlrpis, ");
		sql.append( "     cast(sum(coalesce(lfi.vlrcofins,0)*-1) as decimal(15,5)) vlrcofins ");
		sql.append( "   from cpcompra cp ");
		sql.append( "   left outer join  eqtipomov tm ");
		sql.append( "     on tm.codemp=cp.codemptm ");
		sql.append( "    and tm.codfilial=cp.codfilialtm ");
		sql.append( "    and tm.codtipomov=cp.codtipomov ");
		sql.append( "   inner join cpitcompra ic ");
		sql.append( "     on ic.codemp=cp.codemp ");
		sql.append( "    and ic.codfilial=cp.codfilial ");
		sql.append( "    and ic.codcompra=cp.codcompra ");
		sql.append( "   inner join eqproduto pd ");
		sql.append( "     on pd.codemp=ic.codemppd ");
		sql.append( "    and pd.codfilial=ic.codfilialpd ");
		sql.append( "    and pd.codprod=ic.codprod ");
		sql.append( "   left outer join lfitcompra lfi ");
		sql.append( "     on lfi.codemp=ic.codemp ");
		sql.append( "    and lfi.codfilial=ic.codfilial ");
		sql.append( "    and lfi.codcompra=ic.codcompra ");
		sql.append( "    and lfi.coditcompra=ic.coditcompra ");
		sql.append( "  left outer join lfitclfiscal itcf " );
		sql.append( "	  on itcf.codemp=ic.codempif ");
		sql.append( "    and itcf.codfilial=ic.codfilialif " );
		sql.append( "    and itcf.codfisc=ic.codfisc " );
		sql.append( "    and itcf.coditfisc=ic.coditfisc " );
		sql.append( "   inner join cpforneced cf ");
		sql.append( "     on cf.codfor = cp.codfor ");
		sql.append( "    and cf.codemp = cp.codempfr ");
		sql.append( "    and cf.codfilial = cp.codfilialfr ");
		sql.append( "  where cp.codemp=? ");
		sql.append( "    and cp.codfilial=? ");
		sql.append( "    and cp.dtemitcompra between ? AND ? ");
		sql.append( "    and tm.fiscaltipomov='S' ");
		sql.append( "    and cp.statuscompra<>'X' ");
		//sql.append( where );
		sql.append( wherefin);
		sql.append( "  group by 1, 2, 3, 4, 5, 6 ");
		
		sql.append( "union all " );
		
		sql.append( " select ");
		sql.append( "     cast(pd.descprod as varchar(100)) descprod, ");
		sql.append( "     cast('S' as varchar(1)) tipo, ");
		sql.append( "     cast(iv.codfisc as char(13)) codfisc, cast(iv.coditfisc as integer) coditfisc, ");
		sql.append( "     cast(itcf.codsittribpis as char(2)) codsittribpis, cast(itcf.codsittribcof as char(2)) codsittribcof, ");
		sql.append( "     cast(sum(iv.vlrliqitvenda) as decimal(15,5)) vlrliq, ");
		sql.append( "     cast(sum(iv.vlrproditvenda) as decimal(15,5)) vlrtot, ");
		sql.append( "     cast(sum(coalesce(lfi.vlrbasepis,0)) as decimal(15,5)) vlrbasepis, ");
		sql.append( "     cast(sum(coalesce(lfi.vlrbasecofins,0)) as decimal(15,5)) vlrbasecofins, ");
		sql.append( "     cast(sum(coalesce(lfi.vlrpis,0)) as decimal(15,5)) vlrpis, ");
		sql.append( "     cast(sum(coalesce(lfi.vlrcofins,0)) as decimal(15,5)) vlrcofins ");
		sql.append( " from vdvenda vd ");
		sql.append( " left outer join  eqtipomov tm ");
		sql.append( "     on tm.codemp=vd.codemptm ");
		sql.append( "    and tm.codfilial=vd.codfilialtm ");
		sql.append( "    and tm.codtipomov=vd.codtipomov ");
		sql.append( " inner join  vditvenda iv ");
		sql.append( "     on iv.codemp=vd.codemp and iv.codfilial=vd.codfilial ");
		sql.append( "    and iv.tipovenda=vd.tipovenda ");
		sql.append( "    and iv.codvenda=vd.codvenda ");
		sql.append( " inner join eqproduto pd ");
		sql.append( "     on pd.codemp=iv.codemppd and pd.codfilial=iv.codfilialpd ");
		sql.append( "    and pd.codprod=iv.codprod ");
		sql.append( " left outer join lfitclfiscal itcf " );
		sql.append( "	  on itcf.codemp=iv.codempif ");
		sql.append( "    and itcf.codfilial=iv.codfilialif " );
		sql.append( "    and itcf.codfisc=iv.codfisc " );
		sql.append( "    and itcf.coditfisc=iv.coditfisc " );
		sql.append( " left outer join lfitvenda lfi ");
		sql.append( "     on lfi.codemp=iv.codemp ");
		sql.append( "    and lfi.codfilial=iv.codfilial ");
		sql.append( "    and lfi.codvenda=iv.codvenda ");
		sql.append( "    and lfi.tipovenda=iv.tipovenda ");
		sql.append( "    and lfi.coditvenda=iv.coditvenda ");
		sql.append( "  inner join vdcliente vc ");
		sql.append( "     on vc.codcli = vd.codcli ");
		sql.append( "    and vc.codemp = vd.codempcl ");
		sql.append( "    and vc.codfilial = vd.codfilialcl ");
		sql.append( "  where vd.codemp=? ");
		sql.append( "    and vd.codfilial=? ");
		sql.append( "    and vd.dtemitvenda between ? AND ? ");
		sql.append( "    and tm.fiscaltipomov='S' ");
		sql.append( "    and vd.statusvenda<>'X' ");
		//sql.append( where );
		sql.append( wherefin);
		sql.append( "  group by 1, 2, 3, 4, 5, 6 ");
		sql.append( " order by 1, 2, 3, 4, 5, 6");

		
		return sql;
	}

	private void imprimeTexto( final TYPE_PRINT bVisualizar, String sPis, String sCofins ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		StringBuilder where = new StringBuilder();
		StringBuilder wherefin = new StringBuilder();
		String sFiltros1 = "";
		String sSemMov = "";
		ImprimeOS imp = null;
		int linPag = 0;
		double deVlrEntradas = 0;
		double deVlrSaidas = 0;

		try {

			imp = new ImprimeOS( "", con );
			linPag = imp.verifLinPag() - 1;

			sFiltros1 = "";

			if ( !sPis.equals( "N" ) ) {
				sFiltros1 = "( PIS ";

				if ( sPis.equals( "T" ) )
					sFiltros1 += "TRIBUTADO )";
				else if ( sPis.equals( "I" ) )
					sFiltros1 += "ISENTO )";
				else if ( sPis.equals( "S" ) )
					sFiltros1 += "SUBSTITUICAO )";

				where.append( " AND ( CF.SITPISFISC='" + sPis + "'" );
			}
			if ( !sCofins.equals( "N" ) ) {
				sFiltros1 += ( sFiltros1.equals( "" ) ? "" : " + " ) + "( COFINS ";

				if ( sCofins.equals( "T" ) )
					sFiltros1 += "TRIBUTADO )";
				else if ( sCofins.equals( "I" ) )
					sFiltros1 += "ISENTO )";
				else if ( sCofins.equals( "S" ) )
					sFiltros1 += "SUBSTITUICAO )";

				where.append( ( where.length()==0 ? " AND ( " : " OR " ) + " CF.SITCOFINSFISC='" + sCofins + "'" );
			}
			if ( where.length()>0 )
				where.append( " ) ");

			if ( "S".equals( rgFinanceiro.getVlrString() ) ) {
				wherefin.append(  " AND TM.SOMAVDTIPOMOV='S' " );
			}
			else if ( "N".equals( rgFinanceiro.getVlrString() ) ) {
				wherefin.append( " AND TM.SOMAVDTIPOMOV='N' " );
			}
			else if ( "A".equals( rgFinanceiro.getVlrString() ) ) {
				wherefin.append( " AND TM.SOMAVDTIPOMOV IN ('S','N') " );
			}

			sSemMov = cbSemMov.getVlrString();

			
			sql.append( "SELECT P.DESCPROD, P.CODFISC,(SELECT SUM( VLRLIQITCOMPRA ) " + "FROM CPITCOMPRA IC, CPCOMPRA C, EQTIPOMOV TM " + "WHERE C.CODCOMPRA = IC.CODCOMPRA AND C.CODEMP = IC.CODEMP AND " + "C.CODFILIAL = IC.CODFILIAL AND C.CODCOMPRA = IC.CODCOMPRA AND " );
			sql.append( "C.DTENTCOMPRA BETWEEN ? AND ? AND IC.CODEMPPD = P.CODEMP AND " + "IC.CODFILIALPD = P.CODFILIAL AND IC.CODPROD = P.CODPROD AND " + "C.CODEMPTM = TM.CODEMP AND C.CODFILIALTM = TM.CODFILIAL AND " + "C.CODTIPOMOV = TM.CODTIPOMOV AND TM.TIPOMOV = 'CP' " );
			sql.append( wherefin );
			sql.append( ") COMPRAS, ");
			sql.append( "( SELECT SUM( VLRLIQITVENDA ) FROM VDITVENDA IV, VDVENDA V, EQTIPOMOV TM " + "WHERE V.CODVENDA = IV.CODVENDA AND V.CODEMP = IV.CODEMP AND " + "V.CODFILIAL = IV.CODFILIAL AND V.TIPOVENDA = IV.TIPOVENDA AND " + "V.CODVENDA = IV.CODVENDA AND V.DTEMITVENDA BETWEEN ? AND ");
			sql.append( "? AND IV.CODEMPPD = P.CODEMP AND IV.CODFILIALPD = P.CODFILIAL AND " + "IV.CODPROD = P.CODPROD AND V.CODEMPTM = TM.CODEMP AND " + "V.CODFILIALTM = TM.CODFILIAL AND V.CODTIPOMOV = TM.CODTIPOMOV " );
			sql.append( wherefin );
			sql.append(" AND TM.TIPOMOV = 'VD' AND ( SUBSTRING( V.STATUSVENDA FROM 1 FOR 1 ) != 'C' OR " + "V.STATUSVENDA IS NULL )  ) VENDAS " );
					// "FROM EQPRODUTO P, LFITCLFISCAL CF " +
		    sql.append( "FROM EQPRODUTO P, LFCLFISCAL CF " + "WHERE P.CODEMPFC=CF.CODEMP AND P.CODFILIALFC=CF.CODFILIAL AND " );
					// "P.CODFISC=CF.CODFISC AND CF.GERALFISC='S' AND P.CODEMP=? AND P.CODFILIAL=? " + sWhere +
		    sql.append( "P.CODFISC=CF.CODFISC AND P.CODEMP=? AND P.CODFILIAL=? " );
		    sql.append( where );
		    sql.append( "ORDER BY P.DESCPROD " );

			System.out.println( sql.toString() );

			try {
				ps = con.prepareStatement( sql.toString() );
				ps.setDate( 1, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				ps.setDate( 2, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
				ps.setDate( 3, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				ps.setDate( 4, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
				ps.setInt( 5, Aplicativo.iCodEmp );
				ps.setInt( 6, ListaCampos.getMasterFilial( "EQPRODUTO" ) );

				rs = ps.executeQuery();

				imp.limpaPags();

				imp.montaCab();
				imp.setTitulo( "Relatório de entradas e saidas" );
				imp.addSubTitulo( "RELATORIO DE ENTRADAS E SAIDAS" );
				if ( !sFiltros1.equals( "" ) ) {
					imp.addSubTitulo( sFiltros1 );
				}

				while ( rs.next() ) {
					if ( imp.pRow() >= ( linPag - 1 ) ) {
						imp.say( imp.pRow() + 1, 0, imp.comprimido() );
						imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
						imp.incPags();
						imp.eject();
					}
					if ( imp.pRow() == 0 ) {
						imp.impCab( 136, true );
						imp.say( imp.pRow(), 0, imp.comprimido() );
						imp.say( imp.pRow(), 1, "|" );
						imp.say( imp.pRow(), 49, "PERIODO DE: " + txtDataini.getVlrString() + " ATE: " + txtDatafim.getVlrString() );
						imp.say( imp.pRow(), 136, "|" );
						imp.say( imp.pRow() + 1, 0, imp.comprimido() );
						imp.say( imp.pRow(), 1, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
						imp.say( imp.pRow() + 1, 0, imp.comprimido() );
						imp.say( imp.pRow(), 1, "| DESCRICAO DO PRODUTO" );
						imp.say( imp.pRow(), 60, "| CODIGO NBM" );
						imp.say( imp.pRow(), 89, "| ENTRADAS" );
						imp.say( imp.pRow(), 109, "| SAIDAS" );
						imp.say( imp.pRow(), 136, "|" );
						imp.say( imp.pRow() + 1, 0, imp.comprimido() );
						imp.say( imp.pRow(), 1, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
					}
					if ( ( sSemMov.equals( "N" ) ) || ( rs.getDouble( 3 ) != 0 ) || ( rs.getDouble( 4 ) != 0 ) ) {
						imp.say( imp.pRow() + 1, 0, imp.comprimido() );
						imp.say( imp.pRow(), 1, "| " + Funcoes.adicionaEspacos( rs.getString( 1 ), 50 ) );
						imp.say( imp.pRow(), 60, "| " + Funcoes.adicEspacosEsquerda( rs.getString( 2 ), 13 ) );
						imp.say( imp.pRow(), 89, "| " + Funcoes.strDecimalToStrCurrency( 15, 2, "" + rs.getDouble( 3 ) ) );
						imp.say( imp.pRow(), 109, "| " + Funcoes.strDecimalToStrCurrency( 15, 2, "" + rs.getDouble( 4 ) ) );
						imp.say( imp.pRow(), 136, "|" );
						deVlrEntradas += rs.getDouble( 3 );
						deVlrSaidas += rs.getDouble( 4 );
					}
				}

				// Fim da impressão do total por setor

				imp.say( imp.pRow() + 1, 0, imp.comprimido() );
				imp.say( imp.pRow(), 1, "|" + StringFunctions.replicate( "=", 133 ) + "|" );
				imp.say( imp.pRow() + 1, 0, imp.comprimido() );
				imp.say( imp.pRow(), 1, "| TOTAL" );
				imp.say( imp.pRow(), 89, "| " + Funcoes.strDecimalToStrCurrency( 10, 2, deVlrEntradas + "" ) );
				imp.say( imp.pRow(), 109, "| " + Funcoes.strDecimalToStrCurrency( 15, 2, deVlrSaidas + "" ) );
				imp.say( imp.pRow(), 136, "|" );
				imp.say( imp.pRow() + 1, 0, imp.comprimido() );
				imp.say( imp.pRow(), 1, "+" + StringFunctions.replicate( "=", 133 ) + "+" );
				imp.eject();
				imp.fechaGravacao();

				rs.close();
				ps.close();

				con.commit();

			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro executando a consulta.\n" + err.getMessage(), true, con, err );
				err.printStackTrace();
			}
			if ( bVisualizar==TYPE_PRINT.VIEW )
				imp.preview( this );
			else
				imp.print();

		} finally {
			where = null;
			sql = null;
			sFiltros1 = null;
			sPis = null;
			sCofins = null;
			imp = null;
			ps = null;
			rs = null;
			System.gc();
		}
	}

	public void valorAlterado( RadioGroupEvent evt ) {

		if (evt.getSource()==rgTiporel) {
			if ("G".equals( rgTiporel.getVlrString() )) {
				rgPis.setAtivo( false );
				rgCofins.setAtivo( false );
				cbSemMov.setEnabled( false );
				cbSemMov.setVlrString( "S" );
			} else {
				rgPis.setAtivo( true );
				rgCofins.setAtivo( true );
				cbSemMov.setEnabled( true );
			}
		} 
		
	}
}
