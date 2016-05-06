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
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.swing.util.SwingParams;
import org.freedom.library.type.TYPE_PRINT;

public class FRConsumoMatFSC extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodSecao = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldFK txtDescSecao = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private ListaCampos lcSecao = new ListaCampos( this );

	private boolean comref = false;

	boolean cliente = false;

	boolean diario = false;

	public FRConsumoMatFSC() {

		setTitulo( "Relatório de consumo de matéria prima" );
		
		setAtribos( 80, 80, 370, 250 );

		txtDescSecao.setAtivo( false );

		lcSecao.add( new GuardaCampo( txtCodSecao, "CodSecao", "Cód.Seção", ListaCampos.DB_PK, false ) );
		lcSecao.add( new GuardaCampo( txtDescSecao, "DescSecao", "Descrição da seção", ListaCampos.DB_SI, false ) );
		txtCodSecao.setTabelaExterna( lcSecao, null );
		txtCodSecao.setNomeCampo( "CodSecao" );
		txtCodSecao.setFK( true );
		lcSecao.setReadOnly( true );
		lcSecao.montaSql( false, "SECAO", "EQ" );

		txtDataini.setVlrDate( new Date() );
		txtDatafim.setVlrDate( new Date() );

		JPanelPad pnPeriodo = new JPanelPad();
		pnPeriodo.setBorder( SwingParams.getPanelLabel( "Período", Color.BLACK, TitledBorder.LEFT ) );

		adic( pnPeriodo, 4, 5, 325, 60 );

		pnPeriodo.adic( new JLabelPad( "De:" ), 5, 05, 30, 20 );
		pnPeriodo.adic( txtDataini, 35, 05, 90, 20 );
		pnPeriodo.adic( new JLabelPad( "Até:" ), 135, 05, 30, 20 );
		pnPeriodo.adic( txtDatafim, 170, 05, 90, 20 );

		JPanelPad pnFiltros = new JPanelPad();
		pnFiltros.setBorder( SwingParams.getPanelLabel( "Filtros", Color.BLACK, TitledBorder.LEFT ) );

		adic( pnFiltros, 4, 70, 325, 85 );

		pnFiltros.adic( new JLabelPad( "Cód.Seção" ), 4, 5, 70, 20 );
		pnFiltros.adic( txtCodSecao, 4, 25, 70, 20 );

		pnFiltros.adic( new JLabelPad( "Descrição da seção de produção" ), 77, 5, 230, 20 );
		pnFiltros.adic( txtDescSecao, 77, 25, 230, 20 );

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
			
			sql.append( "pd.codsecao, sc.descsecao, ");
			
			//INICIO DAS COMPRAS			
			sql.append( "coalesce(sum(( ");
			sql.append( "select ");
			sql.append( "sum(ic.qtditcompra) from cpitcompra ic, cpcompra cp, eqtipomov tm ");
			sql.append( "where ");
			
			sql.append( "cp.codemp=ic.codemp and cp.codfilial=ic.codfilial and cp.codcompra=ic.codcompra and cp.dtentcompra between ? and ? ");
			sql.append( "and ic.codemppd=pd.codemp and ic.codfilialpd=pd.codfilial and ic.codprod=pd.codprod ");
			sql.append( "and cp.codemptm=tm.codemp and cp.codfilialtm=tm.codfilial and cp.codtipomov=tm.codtipomov and tm.estoqtipomov='S' ");
			sql.append( "and cp.codemp=? and cp.codfilial=? and cp.statuscompra in ('P2','P3','C2','C3','EP','ET') ");
			
			sql.append( ")),0) recepcionadas ");
			// FIM DAS COMPRAS
		
			sql.append( " , ");
			
			//INICIO DO CONSUMO
			
			sql.append( "coalesce(sum(( ");
			sql.append( "select ");
			sql.append( "sum(ir.qtdexpitrma) ");
			sql.append( "from ");
			sql.append( "eqitrma ir, eqproduto pe ");
			sql.append( "where ");
			sql.append( "ir.codemp=? and ir.codfilial=? and ir.dtaexpitrma between ? and ? ");
			sql.append( "and pe.codemp=ir.codemppd and pe.codfilial=ir.codfilialpd and pe.codprod=ir.codprod ");
			sql.append( "and pe.codemp=pd.codemp and pe.codfilial=pd.codfilial and pe.codprod=pd.codprod ");
			
			sql.append( ")),0) consumidas ");

			// FIM DO CONSUMO 
			
			sql.append( ", ");
			
			// ESTOQUE ANTERIOR
			
			sql.append( "coalesce(sum(( ");
			sql.append( "select sldprod from eqcustoprodsp(pd.codemp, pd.codfilial, pd.codprod, ?, 'P', null, null, null, 'S') ");
			sql.append( ")),0) saldoanterior ");

			// FIM ESTOQUE ANTERIOR
			
			sql.append( "from eqproduto pd ");
			sql.append( "inner join eqsecao sc on sc.codemp=pd.codempsc and sc.codfilial=pd.codfilialsc and sc.codsecao=pd.codsecao ");
			sql.append( "and pd.tipoprod='M' and pd.certfsc='S' ");

			if( ! "".equals( txtCodSecao.getVlrString()) ) {
				sql.append( "and pd.codempsc=? and pd.codfilialsc=? and pd.codsecao=? " );
			}
			
			sql.append( " group by 1,2 " );
		
			System.out.println("SQL:" + sql.toString());

			ps = con.prepareStatement( sql.toString() );

			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, Aplicativo.iCodFilial );
			
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, Aplicativo.iCodFilial );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

			Date dtant = txtDataini.getVlrDate();
			Calendar cant = new GregorianCalendar();
			cant.setTime( dtant );
			cant.add( Calendar.DAY_OF_YEAR, -1 ); 
			
			ps.setDate( param++, Funcoes.dateToSQLDate( cant.getTime() ) );


			sCab.append( "Período de " + Funcoes.dateToStrDate( txtDataini.getVlrDate() ) + " até " + Funcoes.dateToStrDate( txtDatafim.getVlrDate() ) );

			if ( !"".equals( txtCodSecao.getVlrString() ) ) {
				ps.setInt( param++, lcSecao.getCodEmp() );
				ps.setInt( param++, lcSecao.getCodFilial() );
				ps.setString( param++, txtCodSecao.getVlrString() );

				sCab2.append( "Seção: " + txtDescSecao.getVlrString() );
			}
			
			rs = ps.executeQuery();

			imprimirGrafico( visualizar, rs, sCab.toString() + "\n" + sCab2.toString(), comref, "layout/rel/REL_FSC_CONSUMO_MATERIAL_01.jasper" );

			rs.close();
			ps.close();

			con.commit();

		} 
		catch ( Exception err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao montar relatorio!\n" + err.getMessage(), true, con, err );
		} 
		finally {
			System.gc();
		}
	}
	
	public void imprimirGrafico( final TYPE_PRINT bVisualizar, final ResultSet rs, final String sCab, final boolean bComRef , String rel ) {

		HashMap<String, Object> hParam = new HashMap<String, Object>();

		FPrinterJob dlGr = null;

	
		dlGr = new FPrinterJob( rel, "Relatório de consumo de matéria prima ", sCab, rs, hParam, this );
		

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

		lcSecao.setConexao( cn );

		comref = comRef();
	}
}
