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


public class FRProducaoGrupoFSC extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private boolean comref = false;

	boolean cliente = false;

	boolean diario = false;
	
	private ListaCampos lcGrup = new ListaCampos( this );
	
	private JTextFieldPad txtCodGrup = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtDescGrup = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );
	
	private JCheckBoxPad cbPorFolha = new JCheckBoxPad( "Por Folhas (FSC)", "S", "N" );

	public FRProducaoGrupoFSC() {

	setTitulo( "Produção por grupo" );
		
		setAtribos( 120, 120, 370, 250 );

		lcGrup.add( new GuardaCampo( txtCodGrup, "CodGrup", "Cód.grupo", ListaCampos.DB_PK, false ) );
		lcGrup.add( new GuardaCampo( txtDescGrup, "DescGrup", "Descrição do grupo", ListaCampos.DB_SI, false ) );
		lcGrup.montaSql( false, "GRUPO", "EQ" );
		lcGrup.setReadOnly( true );
		txtCodGrup.setTabelaExterna( lcGrup, null );
		txtCodGrup.setFK( true );
		txtCodGrup.setNomeCampo( "CodGrup" );

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

		adic( pnFiltros, 4, 70, 335, 85 );

		pnFiltros.adic( txtCodGrup, 4, 25, 120, 20, "Cód.Grupo" );
		pnFiltros.adic( txtDescGrup, 127, 25, 185, 20, "Descrição do grupo" );

		adic(cbPorFolha, 7, 165, 200, 20);
		

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
			sql.append( "gp.codgrup, gp.descgrup, ");
			
			if("S".equals( cbPorFolha.getVlrString())) {
				
				sql.append( "coalesce(sum( ( ");
				sql.append( "select sum( op.qtdfinalprodop / cast( (pdo.nroplanos*pdo.qtdporplano) as decimal(15,4) ) * cast( coalesce(pdo.fatorfsc,1) as decimal(15,5)) ) ");
				sql.append( "from ppop op, eqproduto pdo ");
				sql.append( "where pdo.codemp=pd.codemp and pdo.codfilial=pd.codfilial and pdo.codprod=pd.codprod " );
				sql.append( "and op.codemppd=pdo.codemp and op.codfilialpd=pdo.codfilial and op.codprod=pdo.codprod ");
				sql.append( "and op.dtfabrop between ? and ? and op.sitop in ('FN') ");
				sql.append( ")),0) produzidas, "); 
								
				sql.append( "coalesce(sum ( ( ");
				sql.append( "select sum(iv.qtditvenda / cast( (pdo.nroplanos*pdo.qtdporplano) as decimal(15,4))  * cast( coalesce(pdo.fatorfsc,1) as decimal(15,5)) ) ");
				sql.append( "from vdvenda vd, vditvenda iv, eqproduto pdo ");
				sql.append( "where ");
				sql.append( "pdo.codemp=pd.codemp and pdo.codfilial=pd.codfilial and pdo.codprod=pd.codprod ");
				sql.append( "and vd.codemp=iv.codemp and vd.codfilial=iv.codfilial and vd.codvenda=iv.codvenda and vd.tipovenda=iv.tipovenda ");
				sql.append( "and vd.statusvenda in ('P3', 'V2', 'V3') and vd.dtsaidavenda between ? and ? ");
				sql.append( "and iv.codemppd=pdo.codemp and iv.codfilialpd=pdo.codfilial and iv.codprod=pdo.codprod ");
				sql.append( ")),0)  ");
				
				sql.append( " - ");

				
				sql.append( "coalesce(sum ( ( ");
				sql.append( "select sum(ic.qtditcompra / cast( (pdo.nroplanos*pdo.qtdporplano) as decimal(15,4))  * cast( coalesce(pdo.fatorfsc,1) as decimal(15,5)) ) ");
				sql.append( "from cpcompra cp, cpitcompra ic, eqproduto pdo ");
				sql.append( "where ");
				sql.append( "pdo.codemp=pd.codemp and pdo.codfilial=pd.codfilial and pdo.codprod=pd.codprod ");
				sql.append( "and cp.codemp=ic.codemp and cp.codfilial=ic.codfilial and cp.codcompra=ic.codcompra ");
				sql.append( "and cp.dtentcompra between ? and ? ");
				sql.append( "and ic.codemppd=pdo.codemp and ic.codfilialpd=pdo.codfilial and ic.codprod=pdo.codprod ");
				sql.append( ")),0)  ");
				
				sql.append("vendidas, "); 
		
			}
			else {

				sql.append( "coalesce(sum( ( ");
				sql.append( "select sum(op.qtdfinalprodop ) from ppop op where op.codemppd=pd.codemp and op.codfilialpd=pd.codfilial and op.codprod=pd.codprod and ");
				sql.append( "op.dtfabrop between ? and ? and op.sitop in ('FN') ");
				sql.append( ")),0) produzidas, ");
	
				sql.append( "coalesce(sum ( ( ");
				sql.append( "select sum(iv.qtditvenda) from vdvenda vd, vditvenda iv where ");
				sql.append( "vd.codemp=iv.codemp and vd.codfilial=iv.codfilial and vd.codvenda=iv.codvenda and vd.tipovenda=iv.tipovenda ");
				sql.append( "and vd.statusvenda in ('P3', 'V2', 'V3') and vd.dtsaidavenda between ? and ? ");
				sql.append( "and iv.codemppd=pd.codemp and iv.codfilialpd=pd.codfilial and iv.codprod=pd.codprod ");
				sql.append( ")),0) ");
				sql.append( " - ");
				sql.append( "coalesce(sum ( ( ");
				sql.append( "select sum(ic.qtditcompra) from cpcompra cp, cpitcompra ic where ");
				sql.append( "cp.codemp=ic.codemp and cp.codfilial=ic.codfilial and cp.codvenda=ic.codvenda ");
				sql.append( "and cp.dtentcompra between ? and ? ");
				sql.append( "and ic.codemppd=pd.codemp and ic.codfilialpd=pd.codfilial and ic.codprod=pd.codprod ");
				sql.append( ")),0) ");
				sql.append("vendidas, ");

			}
			
			sql.append( "coalesce(sum( ");
			sql.append( "(select first 1 ");
			sql.append( "m.sldmovprod ");
			if ("S".equals( cbPorFolha.getVlrString())) {
				sql.append( "/ cast( (ps.nroplanos*pe.qtdporplano) as decimal(15,4) )  * cast( coalesce(ps.fatorfsc,1) as decimal(15,5) )  ");
			}
			sql.append( "from eqmovprod m, eqproduto ps ");
			
			sql.append( "where m.codemppd=ps.codemp and ");
			sql.append( "m.codfilial=ps.codfilial and ");
			sql.append( "m.codprod=ps.codprod and ");
			sql.append( "m.dtmovprod<=? ");
			sql.append( "and ps.codemp=pd.codemp and ps.codfilial=pd.codfilial and ps.codprod=pd.codprod ");
			sql.append( "and ps.tipoprod in ('F','05','06') and ps.certfsc='S' ");
			sql.append( "order by m.codprod, m.dtmovprod desc, m.codmovprod desc "); 
			sql.append( " ) ");
			sql.append( " ),0) saldoanterior ");
			
			sql.append( "from eqproduto pd, eqgrupo gp ");
			
			sql.append( "where pd.codempgp=gp.codemp and pd.codfilialgp=gp.codfilial and pd.codgrup=gp.codgrup and pd.tipoprod='F' and pd.certfsc='S' ");
			sql.append( "and pd.codemp=? and pd.codfilial=? ");
			
			if ( !"".equals( txtCodGrup.getVlrString() ) ) {
				sql.append( "and pd.codempgp=? and pd.codfilialgp=? and pd.codgrup=? and pd.tipoprod='F' and pd.certfsc='S' " );
			}
			
			sql.append( "group by 1,2" ); 

			//if("S".equals( cbPorFolha.getVlrString())) {
				//sql.append( ",pd.nroplanos, pd.qtdporplano,pd.fatorfsc" );
			//}
			
			
			
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

			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			
			ps.setDate( param++, Funcoes.dateToSQLDate( cant.getTime()) );
			
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			
			sCab.append( "Período de " + Funcoes.dateToStrDate( txtDataini.getVlrDate() ) + " até " + Funcoes.dateToStrDate( txtDatafim.getVlrDate() ) );

			if ( !"".equals( txtCodGrup.getVlrString() ) ) {
				ps.setInt( param++, lcGrup.getCodEmp() );
				ps.setInt( param++, lcGrup.getCodFilial() );
				ps.setString( param++, txtCodGrup.getVlrString() );

				sCab2.append( "Grupo: " + txtDescGrup.getVlrString() );
			}
			

			rs = ps.executeQuery();

			imprimirGrafico( visualizar, rs, sCab.toString() + "\n" + sCab2.toString(), comref, "layout/rel/REL_FSC_PRODUCAO_GRUPO_01.jasper" );

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

	
		dlGr = new FPrinterJob( rel, "Relatório de produção por grupo ", sCab, rs, hParam, this );
		

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório de produção por grupo!" + err.getMessage(), true, con, err );
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

		lcGrup.setConexao( cn );

		comref = comRef();
	}
}
