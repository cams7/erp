/**
 * @version 09/08/2010 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.gms.view.frame.report <BR>
 *         Classe:
 * @(#)FRPrecoMedioRecMerc.java <BR>
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
 * Tela para filtros do relatório de preço médio por carga.
 * 
 */

package org.freedom.modulos.gms.view.frame.report;

import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
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
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.std.view.frame.crud.plain.FAlmox;

public class FRPrecoMedioRecMerc extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodTran = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeTran = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );
	
	private ListaCampos lcTransp = new ListaCampos( this );

	private ListaCampos lcForneced = new ListaCampos( this );
	
	private ListaCampos lcProduto = new ListaCampos( this );
	
	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private ListaCampos lcAlmox = new ListaCampos( this );
	
	private JTextFieldPad txtCodAlmox = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtDescAlmox = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );


	public FRPrecoMedioRecMerc() {

		setTitulo( "Relação de preço médio diário" );
		setAtribos( 80, 80, 380, 360 );

		txtDataini.setVlrDate( new Date() );
		txtDatafim.setVlrDate( new Date() );

		// Transportadora
		lcTransp.add( new GuardaCampo( txtCodTran, "CodTran", "Cód.Tran.", ListaCampos.DB_PK, false ) );
		lcTransp.add( new GuardaCampo( txtNomeTran, "NomeTran", "Nome do transportador", ListaCampos.DB_SI, false ) );
		txtCodTran.setTabelaExterna( lcTransp, null );
		txtCodTran.setNomeCampo( "CodTran" );
		txtCodTran.setFK( true );
		lcTransp.setReadOnly( true );
		lcTransp.montaSql( false, "TRANSP", "VD" );

		//Fornecedores
		lcForneced.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.For.", ListaCampos.DB_PK, false ) );
		lcForneced.add( new GuardaCampo( txtNomeFor, "NomeFor", "Nome do fornecedor", ListaCampos.DB_SI, false ) );
		txtCodFor.setTabelaExterna( lcForneced, null );
		txtCodFor.setNomeCampo( "CodFor" );
		txtCodFor.setFK( true );
		lcForneced.setReadOnly( true );
		lcForneced.montaSql( false, "FORNECED", "CP" );
		
		// * Almoxarifado (
		
		lcAlmox.add( new GuardaCampo( txtCodAlmox, "CodAlmox", "Cód.almox.", ListaCampos.DB_PK, false ) );
		lcAlmox.add( new GuardaCampo( txtDescAlmox, "DescAlmox", "Descrição do almoxarifado", ListaCampos.DB_SI, false ) );
		lcAlmox.montaSql( false, "ALMOX", "EQ" );
		lcAlmox.setReadOnly( true );
		lcAlmox.setQueryCommit( false );
		
		txtCodAlmox.setTabelaExterna( lcAlmox, FAlmox.class.getCanonicalName() );
		txtCodAlmox.setNomeCampo( "CodAlmox" );
		txtCodAlmox.setFK( true );
		
		// * Produto (

		lcProduto.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcProduto.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );

		txtCodProd.setTabelaExterna( lcProduto, FProduto.class.getCanonicalName() );
		txtCodProd.setNomeCampo( "CodProd" );
		txtCodProd.setFK( true );

		lcProduto.setReadOnly( true );
		lcProduto.montaSql( false, "PRODUTO", "EQ" );

		
		JPanelPad pnPeriodo = new JPanelPad();
		pnPeriodo.setBorder( SwingParams.getPanelLabel( "Período", Color.BLACK, TitledBorder.LEFT ) );

		adic( pnPeriodo, 4, 5, 325, 60 );

		pnPeriodo.adic( new JLabelPad( "De:" ), 5, 05, 30, 20 );
		pnPeriodo.adic( txtDataini, 35, 05, 90, 20 );
		pnPeriodo.adic( new JLabelPad( "Até:" ), 135, 05, 30, 20 );
		pnPeriodo.adic( txtDatafim, 170, 05, 90, 20 );
		
		JPanelPad pnFiltros = new JPanelPad();
		pnFiltros.setBorder( SwingParams.getPanelLabel( "Filtros", Color.BLACK, TitledBorder.LEFT ) );

		adic( pnFiltros, 4, 75, 325, 200 );

		pnFiltros.adic( new JLabelPad( "Cód.Tran." ), 4, 5, 70, 20 );
		pnFiltros.adic( txtCodTran, 4, 25, 70, 20 );

		pnFiltros.adic( new JLabelPad( "Nome do transportador" ), 77, 5, 230, 20 );
		pnFiltros.adic( txtNomeTran, 77, 25, 230, 20 );

		pnFiltros.adic( new JLabelPad( "Cód.For." ), 4, 45, 70, 20 );
		pnFiltros.adic( txtCodFor, 4, 65, 70, 20 );

		pnFiltros.adic( new JLabelPad( "Nome do fornecedor" ), 77, 45, 230, 20 );
		pnFiltros.adic( txtNomeFor, 77, 65, 230, 20 );
		
		pnFiltros.adic( new JLabelPad( "Cód.Prod." ), 4, 85, 70, 20 );
		pnFiltros.adic( txtCodProd, 4, 105, 70, 20 );

		pnFiltros.adic( new JLabelPad( "Descrição do produto" ), 77, 85, 230, 20 );
		pnFiltros.adic( txtDescProd, 77, 105, 230, 20 );
		
		pnFiltros.adic( new JLabelPad( "Cód.Almox." ), 4, 125, 70, 20 );
		pnFiltros.adic( txtCodAlmox, 4, 145, 70, 20 );

		pnFiltros.adic( new JLabelPad( "Descrição do almoxarifado" ), 77, 125, 230, 20 );
		pnFiltros.adic( txtDescAlmox, 77, 145, 230, 20 ); 
		

	}

	public void imprimir( TYPE_PRINT visualizar ) {

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		StringBuffer sCab = new StringBuffer();	

		int param = 1;

		sql.append( "select ");
		sql.append( "cp.dtentcompra, rm.dtent, cp.codfor, ");
		sql.append( "(select nomefor from cpforneced where codemp=cp.codempfr and codfilial=cp.codfilialfr and codfor=cp.codfor) nomefor , "); 
		sql.append( "br.nomebairro, al.descalmox, ic.qtditcompra, ");
		sql.append( "rm.rendaamostragem, ic.precoitcompra, ic.vlrproditcompra, rm.mediaamostragem ");
		sql.append( "from ");
		sql.append( "cpcompra cp ");
// Substituido por subselect por problemas de perfornce da query
//		left outer join cpforneced fr on ");
//		sql.append( "fr.codemp=cp.codempfr and fr.codfilial=cp.codfilialfr and fr.codfor=cp.codfor ");
		sql.append( "right outer join eqrecmerc rm on ");
		sql.append( "rm.codemp=cp.codemprm and rm.codfilial=cp.codfilialrm and rm.ticket=cp.ticket ");
		sql.append( "left outer join sgbairro br on ");
		sql.append( "br.codpais=rm.codpais and br.siglauf=rm.siglauf and br.codmunic=rm.codmunic and br.codbairro=rm.codbairro ");
		sql.append( "left outer join eqalmox al on ");
		sql.append( "al.codemp=rm.codemp and al.codfilial=rm.codfilial and al.codalmox=rm.codalmox ");
		sql.append( "left outer join cpitcompra ic on ");
		sql.append( "ic.codemp=cp.codemp and ic.codfilial=cp.codfilial and ic.codcompra=cp.codcompra ");
		sql.append( "where cp.codemp=? and cp.codfilial=? and rm.dtent between ? and ? ");

		if ( txtCodTran.getVlrInteger() > 0 ) {
			sql.append( "and cp.codemptn=? and cp.codfilialtn=? and cp.codtran=? " );
		}
		if ( txtCodFor.getVlrInteger() > 0 ) {
			sql.append( "and cp.codempfr=? and cp.codfilialfr=? and cp.codfor=? " );
		}
		if ( txtCodProd.getVlrInteger() > 0 ) {
			sql.append( "and rm.codemppd=? and rm.codfilialpd=? and rm.codprod=? and ic.codemppd=? and ic.codfilialpd=? and ic.codprod=? " );
		}
		if ( txtCodAlmox.getVlrInteger() > 0 ) {
			sql.append( "and rm.codempax=? and rm.codfilialax=? and rm.codalmox=? " );
		}


		sql.append( " order by rm.dtent,rm.ticket " );

		try {

			System.out.println( "SQL:" + sql.toString() );

			ps = con.prepareStatement( sql.toString() );

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "EQRECMERC" ) );
			
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

			sCab.append( "Período de " + Funcoes.dateToStrDate( txtDataini.getVlrDate() ) 
					+ " até " + Funcoes.dateToStrDate( txtDatafim.getVlrDate() )
					+ "\n" );
			
			if ( txtCodTran.getVlrInteger() > 0 ) {
				ps.setInt( param++, lcTransp.getCodEmp() );
				ps.setInt( param++, lcTransp.getCodFilial() );
				ps.setInt( param++, txtCodTran.getVlrInteger() );

				sCab.append( "Transportador: " + txtNomeTran.getVlrString() + "\n" );
			}

			if ( txtCodFor.getVlrInteger() > 0 ) {
				ps.setInt( param++, lcForneced.getCodEmp() );
				ps.setInt( param++, lcForneced.getCodFilial() );
				ps.setInt( param++, txtCodFor.getVlrInteger() );

				sCab.append( "Fornecedor: " + txtNomeFor.getVlrString() + "\n" );
			}
			if ( txtCodProd.getVlrInteger() > 0 ) {
				
				ps.setInt( param++, lcProduto.getCodEmp() );
				ps.setInt( param++, lcProduto.getCodFilial() );
				ps.setInt( param++, txtCodProd.getVlrInteger() );
				
				ps.setInt( param++, lcProduto.getCodEmp() );
				ps.setInt( param++, lcProduto.getCodFilial() );
				ps.setInt( param++, txtCodProd.getVlrInteger() );

				sCab.append( "Produto: " + txtDescProd.getVlrString() + "\n" );				
			}
			if ( txtCodAlmox.getVlrInteger() > 0 ) {
				
				ps.setInt( param++, lcAlmox.getCodEmp() );
				ps.setInt( param++, lcAlmox.getCodFilial() );
				ps.setInt( param++, txtCodAlmox.getVlrInteger() );
				
				sCab.append( "Almoxarifado: " + txtDescAlmox.getVlrString() + "\n" );				
			}
			
			
			System.out.println( "SQL:" + sql.toString() );

			rs = ps.executeQuery();

		} 
		catch ( SQLException err ) {

			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar dados das entradas." );

		}
 
		imprimirGrafico( visualizar, rs, sCab.toString() );

	}

	public void imprimirGrafico( final TYPE_PRINT bVisualizar, final ResultSet rs, final String sCab ) {

		HashMap<String, Object> hParam = new HashMap<String, Object>();

		FPrinterJob dlGr = null;

		dlGr = new FPrinterJob( "layout/rel/REL_RECMERC_02.jasper", "Relatório de preço médio/diário", sCab, rs, hParam, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório!" + err.getMessage(), true, con, err );
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcTransp.setConexao( cn );
		lcForneced.setConexao( cn );
		lcProduto.setConexao( cn );
		lcAlmox.setConexao( cn );

	}
}
