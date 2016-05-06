/**
 * @version 22/02/2011 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.pcp <BR>
 *         Classe:
 * @(#)FRConsumoMat.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * Tela para filtros do relatório de consumo de matéria prima por seção.
 * 
 */

package org.freedom.modulos.pcp.view.frame.report.fsc;

import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.swing.border.TitledBorder;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.swing.util.SwingParams;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;


public class FRBalancoProdGrupoFSC extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private boolean comref = false;

	boolean cliente = false;

	boolean diario = false;
	
	private ListaCampos lcGrupo = new ListaCampos( this );
	
	private JTextFieldPad txtCodGrup = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtDescSecao = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );
	
	private JCheckBoxPad cbPorFolha = new JCheckBoxPad( "Por folhas (FSC)", "S", "N" );
	
	private ListaCampos lcProd = new ListaCampos( this, "" );
	
	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodFabProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );
	
	private JTextFieldPad txtRefProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	public FRBalancoProdGrupoFSC() {

	setTitulo( "Relatório de Balanço de produção por Grupo" );
		
		setAtribos( 200, 200, 370, 290 );

		lcGrupo.add( new GuardaCampo( txtCodGrup, "CodGrup", "Cód.Grupo", ListaCampos.DB_PK, false ) );
		lcGrupo.add( new GuardaCampo( txtDescSecao, "DescGrup", "Descrição do grupo", ListaCampos.DB_SI, false ) );
		lcGrupo.montaSql( false, "GRUPO", "EQ" );
		lcGrupo.setReadOnly( true );
		txtCodGrup.setTabelaExterna( lcGrupo, null );
		txtCodGrup.setFK( true );
		txtCodGrup.setNomeCampo( "CodGrup" );
		
		lcProd.add( new GuardaCampo( txtRefProd, "RefProd", "Referência do produto", ListaCampos.DB_PK, false ) );
		lcProd.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtCodFabProd, "codfabprod", "Cód.fab.prod.", ListaCampos.DB_SI, false ) );
		txtRefProd.setTabelaExterna( lcProd, FProduto.class.getCanonicalName() );
		txtRefProd.setNomeCampo( "RefProd" );
		txtRefProd.setFK( true );
		lcProd.setReadOnly( true );
		lcProd.montaSql( false, "PRODUTO", "EQ" );

		txtDataini.setVlrDate( new Date() );
		txtDatafim.setVlrDate( new Date() );

		cbPorFolha.setVlrString( "S" );
		
		JPanelPad pnPeriodo = new JPanelPad();
		pnPeriodo.setBorder( SwingParams.getPanelLabel( "Período", Color.BLACK, TitledBorder.LEFT ) );

		adic( pnPeriodo, 4, 5, 335, 60 );

		pnPeriodo.adic( new JLabelPad( "De:" ), 5, 05, 30, 20 );
		pnPeriodo.adic( txtDataini, 35, 05, 90, 20 );
		pnPeriodo.adic( new JLabelPad( "Até:" ), 135, 05, 30, 20 );
		pnPeriodo.adic( txtDatafim, 170, 05, 90, 20 );

		JPanelPad pnFiltros = new JPanelPad();
		pnFiltros.setBorder( SwingParams.getPanelLabel( "Filtros", Color.BLACK, TitledBorder.LEFT ) );

		adic( pnFiltros, 4, 70, 335, 120 );

		pnFiltros.adic( txtCodGrup, 4, 25, 120, 20, "Cód.Grupo" );
		pnFiltros.adic( txtDescSecao, 127, 25, 185, 20, "Descrição do grupo" );
		
		pnFiltros.adic( txtRefProd, 4, 65, 120, 20, "Ref.Prod." );
		pnFiltros.adic( txtDescProd, 127, 65, 185, 20, "Descrição do Produto" );


		adic(cbPorFolha, 7, 200, 200, 20);
		

	}

	public void imprimir( TYPE_PRINT visualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		StringBuffer sCab = new StringBuffer();
		StringBuffer sCab2 = new StringBuffer();

		int param = 1;

		try {

			if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
				Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
				return;
			}
		
			sql.append( "select ");
			sql.append( "sc.codgrup, sc.descgrup, ");
						
			sql.append( " coalesce(sum(( select sum(ir.qtdexpitrma) from ppop op, eqrma rm, eqitrma ir, eqproduto pd ");
			sql.append( "where ");
		
			sql.append( "rm.codempof=op.codemp and rm.codfilialof=op.codfilial and rm.codop=op.codop and rm.seqop=op.seqop and ");
			sql.append( "ir.codemp=rm.codemp and ir.codfilial=rm.codfilial and ir.codrma=rm.codrma and ");
			sql.append( "pd.codemp=ir.codemppd and pd.codfilial=ir.codfilialpd and pd.codprod=ir.codprod and ");
			sql.append( "pd.codempsc=pe.codempsc and pd.codfilialsc=pe.codfilialsc and pd.codsecao=pe.codsecao and ");
			sql.append( "pd.nroplanos is not null and pd.qtdporplano is not null and ");
			sql.append( "op.codemppd=pe.codemp and op.codfilialpd=pe.codfilial and op.codprod=pe.codprod and pd.certfsc='S' and ");
			sql.append( "op.dtfabrop between ? and ? and op.sitop in ('FN') )),0) consumidas, ");
						
			if("S".equals( cbPorFolha.getVlrString())) {

				sql.append( "coalesce(sum(( select sum( cast(coalesce(ope.qtdent, op.qtdfinalprodop) as decimal(15,4)) / cast(pd.nroplanos*pd.qtdporplano as decimal(15,4)) * cast(coalesce(pd.fatorfsc,1.00) as decimal(15,4)) ) ");
				sql.append( "from eqproduto pd, ppop op ");
				sql.append( "left outer join ppopentrada ope on ope.codemp=op.codemp and ope.codfilial=op.codfilial and ope.codop=op.codop ");
				sql.append( "and ope.seqop=op.seqop ");
				sql.append( "where pd.codprod=pe.codprod and pd.codfilial=pe.codfilial and pd.codprod=pe.codprod and pd.certfsc='S' and op.codemppd=pd.codemp "); 
				sql.append( "and op.codfilialpd=pd.codfilial and op.codprod=pd.codprod and op.dtfabrop between ? and ? and op.sitop in ('FN') ) ),0) produzidas,"); 

			}
			else {

				sql.append( "coalesce(sum(( select sum( cast(coalesce(ope.qtdent, op.qtdfinalprodop) as decimal(15,5))  ) from eqproduto pd, ppop op ");
				sql.append( "left outer join ppopentrada ope ");
				sql.append( "on ope.codemp=op.codemp and ope.codfilial=op.codfilial and ");
				sql.append( "ope.codop=op.codop and ope.seqop=op.seqop ");
				sql.append( "where op.codemppd=pe.codemp and op.codfilialpd=pe.codfilial and op.codprod=pe.codprod and ");
				sql.append( "pd.codprod=pe.codprod and pd.codfilial=pe.codfilial and pd.codprod=pe.codprod and pd.certfsc='S' ");
				sql.append( "and op.dtfabrop between ? and ? and op.sitop in ('FN') ");
				sql.append( ")),0) produzidas, ");

			}
			
			
			
			sql.append( "coalesce(sum( ");
			sql.append( "(select first 1 m.sldmovprod ");
			if ("S".equals( cbPorFolha.getVlrString())) {
				sql.append( "/ cast( ps.nroplanos*pe.qtdporplano as decimal(15,4) ) * cast(coalesce(ps.fatorfsc,1.00) as decimal(15,4)) ");
			}
			sql.append( "from eqmovprod m, eqproduto ps ");
			sql.append( "where m.codemppd=ps.codemp and ");
			sql.append( "m.codfilial=ps.codfilial and ");
			sql.append( "m.codprod=ps.codprod and ");
			sql.append( "m.dtmovprod<=? ");
			sql.append( "and ps.codemp=pe.codemp and ps.codfilial=pe.codfilial and ps.codprod=pe.codprod ");
			sql.append( "and ps.tipoprod in ('F','05','06') and ps.certfsc='S' ");
			sql.append( "order by m.codprod, m.dtmovprod desc, m.codmovprod desc "); 
			sql.append( " ) ");
			sql.append( " ),0) saldoanterior, ");
			
			if("S".equals( cbPorFolha.getVlrString())) {
			
				sql.append( "coalesce(sum( ( select sum( cast( iv.qtditvenda / cast( (pd.nroplanos*pd.qtdporplano ) as decimal(15,4)) * cast(coalesce(pd.fatorfsc,1) as decimal(15,5)) as decimal(15,5)) ) ");
			
			}
			else {
			
				sql.append( "coalesce(sum( (select sum(iv.qtditvenda) ");
			
			}
			sql.append( "from eqproduto pd, vditvenda iv, vdvenda v, eqtipomov tm ");
			sql.append( "where v.codemp=? and v.codfilial=? ");
			sql.append( "and v.dtemitvenda between ? and ? ");
			sql.append( "and iv.codemp=v.codemp and iv.codfilial=v.codfilial ");
			sql.append( "and iv.tipovenda=v.tipovenda and iv.codvenda=v.codvenda ");
			sql.append( "and iv.codemppd=pe.codemp and iv.codfilialpd=pe.codfilial and iv.codprod=pe.codprod ");
			sql.append( "and pd.codemp=iv.codemppd and pd.codfilial=iv.codfilialpd and pd.codprod=iv.codprod and pd.certfsc='S' ");
			sql.append( "and tm.codemp=v.codemptm and tm.codfilial=v.codfilialtm and tm.codtipomov=v.codtipomov and tm.estoqtipomov='S' " );

			sql.append( ")),0) ");
			sql.append( " - ");
			if("S".equals( cbPorFolha.getVlrString())) {
				
				sql.append( "coalesce(sum( ( select sum( cast( ic.qtditcompra / cast( (pd.nroplanos*pd.qtdporplano ) as decimal(15,4)) * cast(coalesce(pd.fatorfsc,1) as decimal(15,5)) as decimal(15,5)) ) ");
			
			}
			else {
			
				sql.append( "coalesce(sum( (select sum(iv.qtditcompra) ");
			
			}
			sql.append( "from eqproduto pd, cpitcompra ic, cpcompra c, eqtipomov tm ");
			sql.append( "where c.codemp=? and c.codfilial=? ");
			sql.append( "and c.dtentcompra between ? and ? ");
			sql.append( "and tm.codemp=c.codemptm and tm.codfilial=c.codfilialtm and tm.codtipomov=c.codtipomov ");
			sql.append( "and tm.estoqtipomov='S' ");
			sql.append( "and ic.codemp=c.codemp and ic.codfilial=c.codfilial ");
			sql.append( "and ic.codcompra=c.codcompra ");
			sql.append( "and ic.codemppd=pe.codemp and ic.codfilialpd=pe.codfilial and ic.codprod=pe.codprod ");
			sql.append( "and pd.codemp=ic.codemppd and pd.codfilial=ic.codfilialpd and pd.codprod=ic.codprod and pd.certfsc='S' ");
			sql.append( ")),0) ");
			
			sql.append("vendidas ");
			
			
			sql.append( "from eqgrupo sc, eqproduto pe ");
			sql.append( "where sc.codemp=pe.codempgp and sc.codfilial=pe.codfilialgp and sc.codgrup=pe.codgrup and pe.tipoprod='F' and pe.certfsc='S' ");
			
			if ( !"".equals( txtCodGrup.getVlrString() ) ) {
				sql.append( "and pe.codempgp=? and pe.codfilialgp=? and pe.codgrup=? " );
			}
			
			if ( !"".equals( txtRefProd.getVlrString() ) ) {
				sql.append( "and pe.codemp=? and pe.codfilial=? and pe.codprod=? " );
			}

			sql.append( "group by sc.codgrup, sc.descgrup ");
					
			if("S".equals( cbPorFolha.getVlrString())) {
				sql.append(",pe.nroplanos, pe.qtdporplano, pe.fatorfsc ");
			}						
			
			System.out.println("SQL:" + sql.toString());

			ps = con.prepareStatement( sql.toString() );

			Date dtant = txtDataini.getVlrDate();
			Calendar cant = new GregorianCalendar();
			cant.setTime( dtant );
			cant.add( Calendar.DAY_OF_YEAR, -1 ); 
			
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			
			ps.setDate( param++, Funcoes.dateToSQLDate( cant.getTime()) );

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "VDVENDA" ) );
			
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
			
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			
			sCab.append( "Período de " + Funcoes.dateToStrDate( txtDataini.getVlrDate() ) + " até " + Funcoes.dateToStrDate( txtDatafim.getVlrDate() ) );

			if ( !"".equals( txtCodGrup.getVlrString() ) ) {
				ps.setInt( param++, lcGrupo.getCodEmp() );
				ps.setInt( param++, lcGrupo.getCodFilial() );
				ps.setString( param++, txtCodGrup.getVlrString() );

				sCab2.append( "Seção: " + txtDescSecao.getVlrString() );
			}
			
			if ( !"".equals( txtRefProd.getVlrString() ) ) {
				
				ps.setInt( param++, lcProd.getCodEmp() );
				ps.setInt( param++, lcProd.getCodFilial() );
				ps.setInt( param++, txtCodProd.getVlrInteger() );
				
				sCab2.append( "Produto: " + txtDescProd.getVlrString() );
			}
			
			rs = ps.executeQuery();

			imprimirGrafico( visualizar, rs, sCab.toString() + "\n" + sCab2.toString(), comref, "layout/rel/REL_FSC_BALANCO_02.jasper" );

			rs.close();
			ps.close();

			con.commit();

		} catch ( Exception err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao montar relatorio!\n" + err.getMessage(), true, con, err );
		} finally {
			System.gc();
		}
	}
	
	public void imprimirGrafico( final TYPE_PRINT bVisualizar, final ResultSet rs, final String sCab, final boolean bComRef , String rel ) {

		HashMap<String, Object> hParam = new HashMap<String, Object>();
	//	hParam.put( "COMREF", bComRef ? "S" : "N" );

		FPrinterJob dlGr = null;
	
		dlGr = new FPrinterJob( rel, "Relatório Balanço de Produção por grupo", sCab, rs, hParam, this );
		

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório de consumo!" + err.getMessage(), true, con, err );
			}
		}
	}

	private boolean comRef() {

		boolean bRetorno = false;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = con.prepareStatement( "SELECT USAREFPROD FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			rs = ps.executeQuery();

			if ( rs.next() ) {
				bRetorno = "S".equals( rs.getString( "UsaRefProd" ) );
			}

			rs.close();
			ps.close();
			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage(), true, con, err );
		}
		return bRetorno;
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcGrupo.setConexao( cn );
		lcProd.setConexao( cn );

		comref = comRef();
	}
}
