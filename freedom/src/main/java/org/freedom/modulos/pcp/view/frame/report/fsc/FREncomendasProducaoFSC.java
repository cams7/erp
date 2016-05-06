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


public class FREncomendasProducaoFSC extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private boolean comref = false;

	boolean cliente = false;

	boolean diario = false;
	
	private ListaCampos lcSecao = new ListaCampos( this );
	
	private JTextFieldPad txtCodSecao = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtDescSecao = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );
	
	private JCheckBoxPad cbPorFolha = new JCheckBoxPad( "Por Folhas (FSC)", "S", "N" );
	
	private JCheckBoxPad cbOpsProdInter = new JCheckBoxPad( "OP's de produtos intermediários", "S", "N" );

	private JCheckBoxPad cbOpsProdAcab = new JCheckBoxPad( "OP's de produtos acabados", "S", "N" );

	public FREncomendasProducaoFSC() {

	setTitulo( "Relatório de encomendas de produção" );
		
		setAtribos( 40, 40, 370, 350 );

		lcSecao.add( new GuardaCampo( txtCodSecao, "CodSecao", "Cód.Seção", ListaCampos.DB_PK, false ) );
		lcSecao.add( new GuardaCampo( txtDescSecao, "DescSecao", "Descrição da seção", ListaCampos.DB_SI, false ) );
		lcSecao.montaSql( false, "SECAO", "EQ" );
		lcSecao.setReadOnly( true );
		txtCodSecao.setTabelaExterna( lcSecao, null );
		txtCodSecao.setFK( true );
		txtCodSecao.setNomeCampo( "CodSecao" );

		txtDataini.setVlrDate( new Date() );
		txtDatafim.setVlrDate( new Date() );
		
		cbPorFolha.setVlrString( "S" );
		cbOpsProdAcab.setVlrString( "S" );
		cbOpsProdInter.setVlrString( "S" );

		JPanelPad pnPeriodo = new JPanelPad();
		pnPeriodo.setBorder( SwingParams.getPanelLabel( "Período", Color.BLACK, TitledBorder.LEFT ) );

		adic( pnPeriodo, 4, 5, 335, 60 );

		pnPeriodo.adic( new JLabelPad( "De:" ), 5, 05, 30, 20 );
		pnPeriodo.adic( txtDataini, 35, 05, 90, 20 );
		pnPeriodo.adic( new JLabelPad( "Até:" ), 135, 05, 30, 20 );
		pnPeriodo.adic( txtDatafim, 170, 05, 90, 20 );

		JPanelPad pnFiltros = new JPanelPad();
		pnFiltros.setBorder( SwingParams.getPanelLabel( "Filtros", Color.BLACK, TitledBorder.LEFT ) );

		adic( pnFiltros, 4, 70, 335, 85 );

		pnFiltros.adic( txtCodSecao, 4, 25, 120, 20, "Cód.Seção" );
		pnFiltros.adic( txtDescSecao, 127, 25, 185, 20, "Descrição da seção" );

		adic(cbPorFolha, 7, 165, 300, 20);
		adic(cbOpsProdAcab, 7, 185, 300, 20);
		adic(cbOpsProdInter, 7, 205, 300, 20);
		
		
		

	}

	public void imprimir( TYPE_PRINT visualizar ) {

		if ( "N".equals( cbOpsProdInter.getVlrString() ) &&  "N".equals( cbOpsProdAcab.getVlrString() ) ) {
			Funcoes.mensagemInforma( this, "Selecione um tipo de produto !" );
			return;
		}
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
			sql.append( "op.codop, op.refprod, pd.descprod, pd.codgrup, pd.tipoprod" );
			// sql.append( ", ( case when pd.tipoprod='06' then null else ");
			sql.append( ", coalesce(sum(( ");
			sql.append( "select sum(ir.qtdexpitrma) from eqrma rm, eqitrma ir, eqproduto pe ");
			sql.append( "where rm.codempof=op.codemp and rm.codfilialof=op.codfilial and rm.codop=op.codop and rm.seqop=op.seqop ");
			sql.append( "and ir.codemp=rm.codemp and ir.codfilial=rm.codfilial and ir.codrma=rm.codrma ");
			sql.append( "and pe.codemp=ir.codemppd and pe.codfilial=ir.codfilialpd and pe.codprod=ir.codprod ");
			sql.append( "and pe.nroplanos is not null and pe.qtdporplano is not null and pe.certfsc='S' ");
			sql.append( ")),0) ");
			//sql.append( "end) ") ;
			sql.append( "consumidas ");
			//sql.append( ", (case when pd.tipoprod='06' then null else " );
			if("S".equals( cbPorFolha.getVlrString())) {
				sql.append( ", coalesce(sum( case when pd.certfsc='S' then (coalesce( ope.qtdent, op.qtdfinalprodop ) / ( pd.nroplanos * pd.qtdporplano ) * coalesce(pd.fatorfsc,1.00)) else 0 end ),0) ");
			}
			else {
				sql.append( ", coalesce(sum( case when pd.certfsc='S' then (coalesce( ope.qtdent, op.qtdfinalprodop ) ) else 0 end  ),0) ");
			}
			// sql.append( " end ) "); 
			sql.append( "produzidas ");
			
			sql.append( ", coalesce(sum((select ");
			if("S".equals( cbPorFolha.getVlrString())) {
				sql.append( "coalesce(sum( case when psp.certfsc='S' then (coalesce( sp.qtditsp, 0 ) / ( psp.nroplanos * psp.qtdporplano ) * coalesce(psp.fatorfsc,1.00)) else 0 end ),0) ");
			}
			else {
				sql.append( "coalesce(sum( case when psp.certfsc='S' then (coalesce( sp.qtditsp, 0) ) else 0 end  ),0)  ");
			}
			sql.append( "from ppopsubprod sp, eqproduto psp where sp.codemp=op.codemp and sp.codfilial=op.codfilial and  ");
			sql.append( "sp.codop=op.codop and sp.seqop=op.seqop and " );
			sql.append( " psp.codemp=sp.codemppd and psp.codfilial=sp.codfilialpd and psp.codprod=sp.codprod)),0) desperdicio  ");
			
			sql.append( "from ");
			sql.append( "ppop op ");
			sql.append( "left outer join ppopentrada ope on ope.codemp=op.codemp and ope.codfilial=op.codfilial and ope.codop=op.codop and ope.seqop=op.seqop ");
			sql.append( "left outer join eqproduto pd on pd.codemp=op.codemppd and pd.codfilial=op.codfilialpd and pd.codprod=op.codprod ");
			sql.append( "left outer join eqsecao sc on sc.codemp=pd.codempsc and sc.codfilial=pd.codfilialsc and sc.codsecao=pd.codsecao ");

			sql.append( "where ");
			
			sql.append( "op.dtfabrop between ? and ? and op.codemp=? and op.codfilial=? and op.sitop='FN' and pd.certfsc='S' and pd.tipoprod in (" );
			// Inclusão de produtos intermédiarios na query
            if ("S".equals(cbOpsProdAcab.getVlrString())) {
            	sql.append( " 'F' ");
            }
            if ("S".equals(cbOpsProdInter.getVlrString())) {
                if ("S".equals(cbOpsProdAcab.getVlrString())) {
                	sql.append( "," );
                }
            	sql.append( " '06' ");
            }
			sql.append( ") ");
			
			if ( !"".equals( txtCodSecao.getVlrString() ) ) {
				sql.append( " and pd.codempsc=? and pd.codfilialsc=? and pd.codsecao=? " );
			}
			
			sql.append( "group by 1,2,3,4,5 ");
			
			//if("S".equals( cbPorFolha.getVlrString())) {
				//sql.append(",pd.nroplanos, pd.qtdporplano, pd.fatorfsc ");
			//}
			
			System.out.println("SQL:" + sql.toString());

			ps = con.prepareStatement( sql.toString() );

			Date dtant = txtDataini.getVlrDate();
			Calendar cant = new GregorianCalendar();
			cant.setTime( dtant );
			cant.add( Calendar.DAY_OF_YEAR, -1 ); 
				
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "PPOP" ) );
			


			sCab.append( "Período de " + Funcoes.dateToStrDate( txtDataini.getVlrDate() ) + " até " + Funcoes.dateToStrDate( txtDatafim.getVlrDate() ) );

			if ( !"".equals( txtCodSecao.getVlrString() ) ) {
				ps.setInt( param++, lcSecao.getCodEmp() );
				ps.setInt( param++, lcSecao.getCodFilial() );
				ps.setString( param++, txtCodSecao.getVlrString() );

				sCab2.append( "Grupo: " + txtDescSecao.getVlrString() );
			}
			
			

			rs = ps.executeQuery();

			imprimirGrafico( visualizar, rs, sCab.toString() + "\n" + sCab2.toString(), comref, "layout/rel/REL_FSC_ENCOMENDAS_PRODUCAO_01.jasper" );

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

	
		dlGr = new FPrinterJob( rel, "Relatório de Ordens de Produção ", sCab, rs, hParam, this );
		

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório de ordens de produção!" + err.getMessage(), true, con, err );
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
