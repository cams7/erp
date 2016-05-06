/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FRVendasItem.java <BR>
 * 
 *                       Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                       modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                       na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                       Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                       sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                       Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                       Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                       de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                       Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.gms.view.frame.report;

import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.border.TitledBorder;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.swing.util.SwingParams;
import org.freedom.library.type.TYPE_PRINT;

public class FRComisProd extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCodSecao = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldFK txtDescSecao = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JRadioGroup<?, ?> rgTipo = null;
	
	private ListaCampos lcSecao = new ListaCampos( this );

	private ListaCampos lcCliente = new ListaCampos( this );
	
	private JCheckBoxPad cbFinalizados = new JCheckBoxPad( "Apenas ítens finalizados", "S", "N" );
	
	private JCheckBoxPad cbAtivoComis = new JCheckBoxPad( "Apenas Técnicos Ativos", "S", "N" );

	private boolean comref = false;

	boolean cliente = false;

	boolean diario = false;

	public FRComisProd() {

		setTitulo( "Relatório de comissionamento/produtividade" );
		setAtribos( 80, 80, 380, 380 );

		txtDescSecao.setAtivo( false );
		txtRazCli.setAtivo( false );

		Vector<String> vLabs = new Vector<String>();
		Vector<String> vVals = new Vector<String>();

		vLabs.addElement( "Por Ítem" );
		vLabs.addElement( "Por Técnico" );
		vLabs.addElement( "Detalhado" );
		vVals.addElement( "I" );
		vVals.addElement( "T" );
		vVals.addElement( "D" );
		
		rgTipo = new JRadioGroup<String, String>( 1, 3, vLabs, vVals );
		rgTipo.setVlrString( "I" );

		lcSecao.add( new GuardaCampo( txtCodSecao, "CodSecao", "Cód.Seção", ListaCampos.DB_PK, false ) );
		lcSecao.add( new GuardaCampo( txtDescSecao, "DescSecao", "Descrição da seção", ListaCampos.DB_SI, false ) );
		txtCodSecao.setTabelaExterna( lcSecao, null );
		txtCodSecao.setNomeCampo( "CodSecao" );
		txtCodSecao.setFK( true );
		lcSecao.setReadOnly( true );
		lcSecao.montaSql( false, "SECAO", "EQ" );

		txtDataini.setVlrDate( new Date() );
		txtDatafim.setVlrDate( new Date() );

		lcCliente.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCliente.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		txtCodCli.setTabelaExterna( lcCliente, null );
		txtCodCli.setNomeCampo( "CodCli" );
		txtCodCli.setFK( true );
		lcCliente.setReadOnly( true );
		lcCliente.montaSql( false, "CLIENTE", "VD" );

		JPanelPad pnPeriodo = new JPanelPad();
		pnPeriodo.setBorder( SwingParams.getPanelLabel( "Período", Color.BLACK, TitledBorder.LEFT ) );

		adic( pnPeriodo, 4, 5, 325, 60 );

		pnPeriodo.adic( new JLabelPad( "De:" ), 5, 05, 30, 20 );
		pnPeriodo.adic( txtDataini, 35, 05, 90, 20 );
		pnPeriodo.adic( new JLabelPad( "Até:" ), 135, 05, 30, 20 );
		pnPeriodo.adic( txtDatafim, 170, 05, 90, 20 );

		JPanelPad pnTipo = new JPanelPad();
		pnTipo.setBorder( SwingParams.getPanelLabel( "Tipo", Color.BLACK, TitledBorder.LEFT ) );

		adic( pnTipo, 4, 70, 325, 70 );
		pnTipo.adic( rgTipo, 3, 3, 300, 30 );
		
		JPanelPad pnFiltros = new JPanelPad();
		pnFiltros.setBorder( SwingParams.getPanelLabel( "Filtros", Color.BLACK, TitledBorder.LEFT ) );

		adic( pnFiltros, 4, 145, 325, 170 );

		pnFiltros.adic( new JLabelPad( "Cód.Seção" ), 4, 5, 70, 20 );
		pnFiltros.adic( txtCodSecao, 4, 25, 70, 20 );

		pnFiltros.adic( new JLabelPad( "Descrição da seção de produção" ), 77, 5, 230, 20 );
		pnFiltros.adic( txtDescSecao, 77, 25, 230, 20 );

		pnFiltros.adic( new JLabelPad( "Cód.Cliente" ), 4, 45, 70, 20 );
		pnFiltros.adic( txtCodCli, 4, 65, 70, 20 );

		pnFiltros.adic( new JLabelPad( "Razão social do cliente" ), 77, 45, 230, 20 );
		pnFiltros.adic( txtRazCli, 77, 65, 230, 20 );
		
		pnFiltros.adic( cbFinalizados, 2, 95, 230, 20 );
		
		pnFiltros.adic( cbAtivoComis, 2, 115, 230, 20 );
		
		cbAtivoComis.setVlrString( "S" );

	}

	public void imprimir( TYPE_PRINT visualizar ) {

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}

		if( "I".equals( rgTipo.getVlrString() )){
			imprimirPorItem( visualizar  );
		}
		else if( "T".equals( rgTipo.getVlrString() )) {
			imprimirPorTecnico( visualizar  );
		}
		else if( "D".equals( rgTipo.getVlrString() )) {
			imprimirTecnicoDetalhado( visualizar  );
		}

	}

	public void imprimirPorItem( TYPE_PRINT visualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		StringBuffer sCab = new StringBuffer();
		StringBuffer sCab2 = new StringBuffer();

		int param = 1;

		try {

			sql.append( "select ");

			sql.append( "pd.codsecao, se.descsecao, sum(coalesce(en.qtdent, op.qtdfinalprodop)) qtdfinalprodop, pd.codprod, pd.refprod, ");
//			sql.append( "pd.codsecao, se.descsecao, op.qtdfinalprodop, pd.codprod, pd.refprod, ");
			sql.append( "pd.descprod, pd.precocomisprod, rc.perccomisgeral/100 perccomisgeral, case when coalesce(op.garantia,'N')='' then 'N' else op.garantia end garantia " );

			sql.append( "from eqproduto pd , vdregracomis rc, eqsecao se, ppop op ");

			sql.append( "left outer join eqrecmerc rm on ");
			sql.append( "rm.codemp=op.codempos and rm.codfilial=op.codfilialos and rm.ticket=op.ticket ");

			sql.append( "left outer join vdcliente cl on ");
			sql.append( "cl.codemp=rm.codempcl and cl.codfilial=rm.codfilialcl and cl.codcli=rm.codcli ");

			sql.append( "left outer join eqitrecmerc it on ");
			sql.append( "it.codemp=op.codempos and it.codfilial=op.codfilialos and it.ticket=op.ticket and it.coditrecmerc=op.coditrecmerc ");
			
			sql.append( "left outer join ppopentrada en on ");
			sql.append( "en.codemp=op.codemp and en.codfilial=op.codfilial and en.codop=op.codop and en.seqop=op.seqop ");

			sql.append( "where "); 

			sql.append( "pd.codemp=op.codemp and pd.codfilial=op.codfilial and pd.codprod=op.codprod ");
			sql.append( "and rc.codempsc=pd.codempsc and rc.codfilialsc=pd.codfilialsc and rc.codsecao=pd.codsecao ");
			sql.append( "and se.codemp=pd.codempsc and se.codfilial=pd.codfilialsc and se.codsecao=pd.codsecao ");
			sql.append( "and op.codemp=? and op.codfilial=? and coalesce(en.dtent,op.dtfabrop) between ? and ? ");
			sql.append( "and op.sitop<>'CA' " );
			
			if ( txtCodCli.getVlrInteger() > 0 ) {
				sql.append( "and rm.codempcl=? and rm.codfilialcl=? and rm.codcli=? " );
			}

			if ( !"".equals( txtCodSecao.getVlrString() ) ) {
				sql.append( "and pd.codempsc=? and pd.codfilialsc=? and pd.codsecao=? " );
			}
			
			if( "S".equals( cbFinalizados.getVlrString()) ) {
				sql.append( " and op.sitop = 'FN' " );
			}
			
			sql.append( "group by 1, 2, 4, 5, 6, 7, 8, 9 " );
			
			sql.append( "order by 1,9,5 " );
			
			System.out.println("SQL:" + sql.toString());

			ps = con.prepareStatement( sql.toString() );

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, Aplicativo.iCodFilial );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

			sCab.append( "Período de " + Funcoes.dateToStrDate( txtDataini.getVlrDate() ) + " até " + Funcoes.dateToStrDate( txtDatafim.getVlrDate() ) );

			if ( txtCodCli.getVlrInteger() > 0 ) {
				ps.setInt( param++, lcCliente.getCodEmp() );
				ps.setInt( param++, lcCliente.getCodFilial() );
				ps.setInt( param++, txtCodCli.getVlrInteger() );

				sCab2.append( "Cliente: " + txtRazCli.getVlrString() + "\n" );
			}

			if ( !"".equals( txtCodSecao.getVlrString() ) ) {
				ps.setInt( param++, lcSecao.getCodEmp() );
				ps.setInt( param++, lcSecao.getCodFilial() );
				ps.setString( param++, txtCodSecao.getVlrString() );

				sCab2.append( "Seção: " + txtDescSecao.getVlrString() );
			}

			rs = ps.executeQuery();

			imprimirGrafico( visualizar, rs, sCab.toString() + "\n" + sCab2.toString(), comref, "layout/rel/REL_COMIS_PROD_DET.jasper" );

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
	
	public void imprimirPorTecnico( TYPE_PRINT visualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		StringBuffer sCab = new StringBuffer();
		StringBuffer sCab2 = new StringBuffer();

		int param = 1;

		try { 

			sql.append( "select ");

			sql.append( "se.codsecao, se.descsecao, rc.perccomisgeral/100 perccomisgeral, ");

			sql.append( "sum( cast( case when op.garantia='S' then ((coalesce(en.qtdent,qtdfinalprodop) * pd.precocomisprod ) * (rc.perccomisgeral/100)) * -1 else ");
			sql.append( "((coalesce(qtdent,qtdfinalprodop) * pd.precocomisprod ) * (rc.perccomisgeral/100)) end as decimal(15,5) ) ) comissecao ");
			sql.append( ", vd.nomevend, irc.perccomisitrc/100 percomisvendedor ");

			sql.append( "from eqproduto pd , vdregracomis rc, vditregracomis irc, eqsecao se, ppop op ");

			sql.append( "left outer join eqrecmerc rm on ");
			sql.append( "rm.codemp=op.codempos and rm.codfilial=op.codfilialos and rm.ticket=op.ticket ");

			sql.append( "left outer join eqitrecmerc it on ");
			sql.append( "it.codemp=op.codempos and it.codfilial=op.codfilialos and it.ticket=op.ticket and it.coditrecmerc=op.coditrecmerc ");		
			
			sql.append( "left outer join ppopentrada en on ");
			sql.append( "en.codemp=op.codemp and en.codfilial=op.codfilial and en.codop=op.codop and en.seqop=op.seqop ");

			sql.append( "inner join vdvendedor vd on ");
			sql.append( "vd.codemp=irc.codempvd and vd.codfilial = irc.codfilialvd and vd.codvend=irc.codvend ");

			sql.append( "where ");

			sql.append( "pd.codemp=op.codemp and pd.codfilial=op.codfilial and pd.codprod=op.codprod ");
			sql.append( "and rc.codempsc=pd.codempsc and rc.codfilialsc=pd.codfilialsc and rc.codsecao=pd.codsecao ");
			sql.append( "and irc.codemp=rc.codemp and irc.codfilial=rc.codfilial and irc.codregrcomis=rc.codregrcomis ");
			sql.append( "and se.codemp=pd.codempsc and se.codfilial=pd.codfilialsc and se.codsecao=pd.codsecao ");
			sql.append( "and op.codemp=? and op.codfilial=? and coalesce(en.dtent,op.dtfabrop) between ? and ? ");
			sql.append( "and op.sitop<>'CA' ");

			if ( txtCodCli.getVlrInteger() > 0 ) {
				sql.append( "and rm.codempcl=? and rm.codfilialcl=? and rm.codcli=? " );
			}

			if ( !"".equals( txtCodSecao.getVlrString() ) ) {
				sql.append( "and pd.codempsc=? and pd.codfilialsc=? and pd.codsecao=? " );
			}
			
			if( "S".equals( cbFinalizados.getVlrString()) ) {
				sql.append( " and op.sitop = 'FN' " );
			}
			
			if ( "S".equals( cbAtivoComis.getVlrString() ) ) {
				sql.append( " and vd.ativocomis = 'S' " );
			}

			sql.append( "group by 1,2,3,5,6" );
			
//			sql.append( "order by pd.codsecao, op.dtfabrop " );

			ps = con.prepareStatement( sql.toString() );

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, Aplicativo.iCodFilial );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

			sCab.append( "Período de " + Funcoes.dateToStrDate( txtDataini.getVlrDate() ) + " até " + Funcoes.dateToStrDate( txtDatafim.getVlrDate() ) );

			if ( !"".equals( txtCodSecao.getVlrString() ) ) {
				ps.setInt( param++, lcSecao.getCodEmp() );
				ps.setInt( param++, lcSecao.getCodFilial() );
				ps.setString( param++, txtCodSecao.getVlrString() );

				sCab2.append( "Seção: " + txtDescSecao.getVlrString() );
			}

			rs = ps.executeQuery();

			imprimirGrafico( visualizar, rs, sCab.toString() + "\n" + sCab2.toString(), comref, "layout/rel/REL_COMIS_PROD_TEC.jasper" );

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
	
	public void imprimirTecnicoDetalhado( TYPE_PRINT visualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		StringBuffer sCab = new StringBuffer();
		StringBuffer sCab2 = new StringBuffer();

		int param = 1;

		try { 

			sql.append( "select ");
			sql.append( "vd.codvend, vd.nomevend, ");
			sql.append( "rc.perccomisgeral/100 perccomisgeral, ");
			sql.append( "se.codsecao, se.descsecao, ");
			sql.append( "sum( cast( case when op.garantia='S' then ((coalesce(en.qtdent,qtdfinalprodop) * pd.precocomisprod ) * (rc.perccomisgeral/100)) * -1 ");
			sql.append( "else ( (coalesce(en.qtdent,op.qtdfinalprodop) * pd.precocomisprod ) * (rc.perccomisgeral/100)) end as decimal(15,5) ) ) comissecao , ");
			sql.append( "irc.perccomisitrc/100 percomisvendedor, ");
			sql.append( "coalesce(vd.vlrabono,0.00) vlrabono, coalesce(vd.vlrdesconto,0.00) vlrdesconto ");

			sql.append( "from eqproduto pd , vdregracomis rc, vditregracomis irc, eqsecao se, ppop op ");
			sql.append( "left outer join eqrecmerc rm on rm.codemp=op.codempos and rm.codfilial=op.codfilialos and rm.ticket=op.ticket ");
			sql.append( "left outer join eqitrecmerc it on it.codemp=op.codempos and it.codfilial=op.codfilialos and it.ticket=op.ticket ");
			sql.append( "and it.coditrecmerc=op.coditrecmerc ");
			
			sql.append( "left outer join ppopentrada en on ");
			sql.append( "en.codemp=op.codemp and en.codfilial=op.codfilial and en.codop=op.codop and en.seqop=op.seqop ");
			
			sql.append( "inner join vdvendedor vd on vd.codemp=irc.codempvd and vd.codfilial = irc.codfilialvd ");
			sql.append( "and vd.codvend=irc.codvend where pd.codemp=op.codemp and pd.codfilial=op.codfilial ");
			sql.append( "and pd.codprod=op.codprod and rc.codempsc=pd.codempsc and rc.codfilialsc=pd.codfilialsc ");
			sql.append( "and rc.codsecao=pd.codsecao and irc.codemp=rc.codemp and irc.codfilial=rc.codfilial ");
			sql.append( "and irc.codregrcomis=rc.codregrcomis and se.codemp=pd.codempsc and se.codfilial=pd.codfilialsc ");
			sql.append( "and se.codsecao=pd.codsecao and op.codemp=? and op.codfilial=? and coalesce(en.dtent, op.dtfabrop) between ? and ? ");
			sql.append( "and op.sitop<>'CA' ");
			

			if ( txtCodCli.getVlrInteger() > 0 ) {
				sql.append( "and rm.codempcl=? and rm.codfilialcl=? and rm.codcli=? " );
			}

			if ( !"".equals( txtCodSecao.getVlrString() ) ) {
				sql.append( "and pd.codempsc=? and pd.codfilialsc=? and pd.codsecao=? " );
			}
			
			if( "S".equals( cbFinalizados.getVlrString()) ) {
				sql.append( " and op.sitop = 'FN' " );
			}
			
			if ( "S".equals( cbAtivoComis.getVlrString() ) ) {
				sql.append( " and vd.ativocomis = 'S' " );
			}

			sql.append( "group by 1,2,3,4,5,7,8,9 ");
			
			sql.append( "union ");
			sql.append( "select ");
			sql.append( "vd.codvend, vd.nomevend, ");
			sql.append( "cast(0.00 as decimal(15,5)) perccomisgeral, ");
			sql.append( "cast('' as varchar(13)) codsecao, cast('' as char(50)) descsecao, ");
			sql.append( "cast(0.00 as decimal(15,5)) comissecao, ");
			sql.append( "cast(0.00 as decimal(15,5)) percomisvendedor, ");
			sql.append( "cast(coalesce(vd.vlrabono,0.00) as decimal(15,5)) vlrabono, ");
			sql.append( "cast(coalesce(vd.vlrdesconto,0.00) as decimal(15,5)) vlrdesconto ");
			sql.append( "from vdvendedor vd ");
			sql.append( "where vd.vlrabono>0 and vd.codsecao is null ");
			
			sql.append( "order by 2"); 
			
			System.out.println( " SQL: " + sql.toString() );
			
			ps = con.prepareStatement( sql.toString() );

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, Aplicativo.iCodFilial );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

			sCab.append( "Período de " + Funcoes.dateToStrDate( txtDataini.getVlrDate() ) + " até " + Funcoes.dateToStrDate( txtDatafim.getVlrDate() ) );

			if ( !"".equals( txtCodSecao.getVlrString() ) ) {
				ps.setInt( param++, lcSecao.getCodEmp() );
				ps.setInt( param++, lcSecao.getCodFilial() );
				ps.setString( param++, txtCodSecao.getVlrString() );

				sCab2.append( "Seção: " + txtDescSecao.getVlrString() );
			}

			rs = ps.executeQuery();

			imprimirGrafico( visualizar, rs, sCab.toString() + "\n" + sCab2.toString(), comref, "layout/rel/REL_COMIS_PROD_TEC_DET.jasper" );

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
		hParam.put( "COMREF", bComRef ? "S" : "N" );

		FPrinterJob dlGr = null;

	
		dlGr = new FPrinterJob( rel, "Relatório de comississionamento por produção detalhado", sCab, rs, hParam, this );
		

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório de coletas!" + err.getMessage(), true, con, err );
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

		lcSecao.setConexao( cn );

		lcCliente.setConexao( cn );

		comref = comRef();
	}
}
