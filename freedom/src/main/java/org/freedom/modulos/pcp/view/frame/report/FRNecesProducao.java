/**
 * @version 03/06/2013 <BR>
 * @author Robson Sanchez / Setpoint Informática Ltda.
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.pcp.view.frame.report <BR>
 *         Classe:
 * @(#)FRAnalise.java <BR>
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
 *                    Relatório de necessidades de produção.
 * 
 */

package org.freedom.modulos.pcp.view.frame.report;

import org.freedom.library.type.TYPE_PRINT;
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
import org.freedom.modulos.gms.business.object.TipoProd;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import net.sf.jasperreports.engine.JasperPrintManager;

public class FRNecesProducao extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodProdIni = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescProdIni = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtRefProdIni = new JTextFieldFK( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCodProdFim = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescProdFim = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtRefProdFim = new JTextFieldFK( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCodGrupo = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldFK txtDescGrupo = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodLote = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );
	
	private Vector<String> vLbOrdem = new Vector<String>();

	private Vector<String> vVlrOrdem = new Vector<String>();

	private JRadioGroup<?, ?> rgOrdem = null;

	private Vector<String> vLbTipoOrdem = new Vector<String>();

	private Vector<String> vVlrTipoOrdem = new Vector<String>();

	private JRadioGroup<?, ?> rgTipoOrdem = null;

	private Vector<String> vLbTipoFiltro = new Vector<String>();

	private Vector<String> vVlrTipoFiltro = new Vector<String>();

	private JRadioGroup<?, ?> rgTipoFiltro = null;

	private ListaCampos lcProdIni = new ListaCampos( this, "" );

	private ListaCampos lcProdFim = new ListaCampos( this, "" );

	private ListaCampos lcGrupo = new ListaCampos( this );

	public FRNecesProducao() {
		super( false );
		setAtribos( 50, 50, 420, 440 );

		montaTela();
		montaListaCampos();
	}

	private void montaTela() {

		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );
		JLabelPad lbPeriodo = new JLabelPad( "Período:", SwingConstants.CENTER );
		lbPeriodo.setOpaque( true );

		vLbOrdem.addElement( "Código" );
		vLbOrdem.addElement( "Descrição" );
		vLbOrdem.addElement( "Necessidade" );
		vVlrOrdem.addElement( "C" );
		vVlrOrdem.addElement( "D" );
		vVlrOrdem.addElement( "N" );
		rgOrdem = new JRadioGroup<String, String>( 3, 1, vLbOrdem, vVlrOrdem );
		rgOrdem.setVlrString( "C" );

		vLbTipoOrdem.addElement( "Crescente" );
		vLbTipoOrdem.addElement( "Decrescente" );
		vVlrTipoOrdem.addElement( "C" );
		vVlrTipoOrdem.addElement( "D" );
		rgTipoOrdem = new JRadioGroup<String, String>( 2, 1, vLbTipoOrdem, vVlrTipoOrdem );
		rgTipoOrdem.setVlrString( "C" );

		vLbTipoFiltro.addElement( "Todos os itens" );
		vLbTipoFiltro.addElement( "Estoq. menor que mín." );
		vVlrTipoFiltro.addElement( "T" );
		vVlrTipoFiltro.addElement( "E" );
		rgTipoFiltro = new JRadioGroup<String, String>( 1, 2, vLbTipoFiltro, vVlrTipoFiltro );
		rgTipoFiltro.setVlrString( "E" );

		adic( lbPeriodo, 7, 1, 80, 20 );
		adic( lbLinha, 5, 10, 355, 45 );

		adic( new JLabelPad( "De:" ), 10, 25, 30, 20 );
		adic( txtDataini, 40, 25, 97, 20 );
		adic( new JLabelPad( "Até:" ), 152, 25, 37, 20 );
		adic( txtDatafim, 190, 25, 100, 20 );
		adic( new JLabelPad( "Cód.prod.i." ), 7, 55, 80, 20 );
		adic( txtCodProdIni, 7, 75, 70, 20 );
		adic( new JLabelPad( "Descrição do produto inicial" ), 83, 55, 280, 20 );
		adic( txtDescProdIni, 83, 75, 280, 20 );

		adic( new JLabelPad( "Cód.prod.f." ), 7, 95, 80, 20 );
		adic( txtCodProdFim, 7, 115, 70, 20 );
		adic( new JLabelPad( "Descrição do produto final" ), 83, 95, 280, 20 );
		adic( txtDescProdFim, 83, 115, 280, 20 );

		adic( new JLabelPad( "Cód.grupo" ), 7, 135, 80, 20 );
		adic( txtCodGrupo, 7, 155, 70, 20 );
		adic( new JLabelPad( "Descrição do grupo" ), 83, 135, 280, 20 );
		adic( txtDescGrupo, 83, 155, 280, 20 );

		adic( new JLabelPad("Ordem:"), 7, 175, 150, 20 );
		adic( rgOrdem, 7, 195, 173, 75 );
		adic( rgTipoOrdem, 193, 195, 173, 75);

		adic( new JLabelPad("Tipo de filtro:"), 7, 275, 400, 20 );
		adic( rgTipoFiltro, 7, 295, 360, 30 );
		
		Calendar cPeriodo = Calendar.getInstance();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataini.setVlrDate( cPeriodo.getTime() );
	}

	private void montaListaCampos() {

		/*******************
		 * Produto inicial *
		 *******************/

		lcProdIni.add( new GuardaCampo( txtCodProdIni, "CodProd", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcProdIni.add( new GuardaCampo( txtRefProdIni, "RefProd", "Referência do produto", ListaCampos.DB_SI, false ) );
		lcProdIni.add( new GuardaCampo( txtDescProdIni, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		txtCodProdIni.setTabelaExterna( lcProdIni, FProduto.class.getCanonicalName() );
		txtCodProdIni.setNomeCampo( "CodProd" );
		txtCodProdIni.setFK( true );
		lcProdIni.setReadOnly( true );
		lcProdIni.montaSql( false, "PRODUTO", "EQ" );

		/*******************
		 * Produto final *
		 *******************/

		lcProdFim.add( new GuardaCampo( txtCodProdFim, "CodProd", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcProdFim.add( new GuardaCampo( txtRefProdFim, "RefProd", "Referência do produto", ListaCampos.DB_SI, false ) );
		lcProdFim.add( new GuardaCampo( txtDescProdFim, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		txtCodProdFim.setTabelaExterna( lcProdFim, FProduto.class.getCanonicalName() );
		txtCodProdFim.setNomeCampo( "CodProd" );
		txtCodProdFim.setFK( true );
		lcProdFim.setReadOnly( true );
		lcProdFim.montaSql( false, "PRODUTO", "EQ" );

		/**********
		 * Grupo *
		 **********/
		lcGrupo.add( new GuardaCampo( txtCodGrupo, "CodGrup", "Cód.grupo", ListaCampos.DB_PK, false ) );
		lcGrupo.add( new GuardaCampo( txtDescGrupo, "DescGrup", "Descrição do grupo", ListaCampos.DB_SI, false ) );
		lcGrupo.montaSql( false, "GRUPO", "EQ" );
		lcGrupo.setReadOnly( true );
		txtCodGrupo.setTabelaExterna( lcGrupo, null );
		txtCodGrupo.setFK( true );
		txtCodGrupo.setNomeCampo( "CodGrup" );
	}

	private int getNumdias(Date dataini, Date datafim) {
		int result;
		float tmp = 0f;
		float intervalo = datafim.getTime()-dataini.getTime();
		tmp = ( intervalo / 1000f / 60f / 60f / 24f ) + 1 ;
		result = (int) tmp;
		return result;
	}
	
	public void imprimir( TYPE_PRINT b ) {

		StringBuffer sql = new StringBuffer();
		StringBuffer cab = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer codprodini = null;
		Integer codprodfim = null;
		String codgrup = txtCodGrupo.getVlrString().trim();
		String ordem = rgOrdem.getVlrString();
		String tipoordem = rgTipoOrdem.getVlrString();
		String tipofiltro = rgTipoFiltro.getVlrString();
		int numdias = getNumdias( txtDataini.getVlrDate(), txtDatafim.getVlrDate() );

		try {
			
			cab.append( " ( período: " );
			cab.append( txtDataini.getVlrString() );
			cab.append( " até: " );
			cab.append( txtDatafim.getVlrString() );
			cab.append( " ) ");
			
			if (!"".equals( txtCodProdIni.getVlrString().trim() ) ) {
				codprodini = txtCodProdIni.getVlrInteger();
			}
			if (!"".equals( txtCodProdFim.getVlrString().trim() ) ) {
				codprodfim = txtCodProdFim.getVlrInteger();
			}
			sql.append( "select p.codprod, p.descprod, p.codunid, p.sldliqprod, p.qtdminprod " );
			sql.append( " , cast(coalesce(sum(iv.qtditvenda),0) as decimal(15,2)) qtditvenda " );
			sql.append( " , cast(coalesce(sum(iv.qtditvenda) / ");
			sql.append(numdias);
			sql.append(" * 30,0) as decimal(15,2)) qtditvenda_mes " );
			sql.append( " , cast(case when p.sldliqprod>p.qtdminprod then 0 else p.qtdminprod-p.sldliqprod end as decimal(15,2)) qtdnecesprod " );
			sql.append( " from eqproduto p ");
			sql.append( " left outer join  vditvenda iv on " );
			sql.append( " iv.codemppd=p.codemp and iv.codfilialpd=p.codfilial and iv.codprod=p.codprod " );
			sql.append( " left outer join vdvenda v on ");
			sql.append( " v.codemp=iv.codemp and v.codfilial=iv.codfilial " );
			sql.append( " and v.tipovenda=iv.tipovenda and v.codvenda=iv.codvenda " );
			sql.append( " and v.dtemitvenda between ? and ? " );
			sql.append( " where p.codemp=? and p.codfilial=? " );
			if ( codprodini!=null && codprodfim!=null ) {
				sql.append(" and p.codprod between ? and ? ");
				cab.append( " ( faixa de produtos: " );
				cab.append( codprodini );
				cab.append( " a " );
				cab.append( codprodfim);
				cab.append( " ) ");
			} else if ( codprodini!=null && codprodfim==null ) {
				sql.append(" and p.codprod=? ");
				cab.append( " ( produto: " );
				cab.append( codprodini );
				cab.append( " - " );
				cab.append( txtDescProdIni.getVlrString().trim() );
				cab.append( " ) ");
			}
			if ( !"".equals( codgrup )) {
				sql.append( " and p.codempgp=? and p.codfilialgp=? and p.codgrup like ? " );
				cab.append( " ( grupo: " );
				cab.append( codgrup );
				cab.append( " - " );
				cab.append( txtDescGrupo.getVlrString().trim() );
				cab.append( " ) ");
			}
			if ( "E".equals(tipofiltro) ) {
				sql.append( " and p.sldliqprod<p.qtdminprod ");
				cab.append( " ( estoque abaixo do mínimo ) " );
			}
			sql.append( " and p.ativoprod = 'S' ");
			sql.append( " and p.tipoprod in ('" );
			sql.append( TipoProd.PRODUTO_ACABADO.getValue() );
			sql.append( "','" );
			sql.append( TipoProd.PRODUTO_INTERMEDIARIO.getValue() );
			sql.append( "','" );
			sql.append( TipoProd.EM_PROCESSO.getValue() );
			sql.append( "') " );
			sql.append( " group by 1,2,3,4,5 " );
			sql.append( " order by ");
			if ( "C".equals( ordem ) ) {
				sql.append( " p.codprod " );
			} else if ( "D".equals( ordem ) ) {
				sql.append( " p.descprod ");
			} else {
				// Necessidade de produção
				sql.append( " 8 ");
			}
			if ( "D".equals(tipoordem) ) {
				sql.append( " desc " );
			}
			ps = con.prepareStatement( sql.toString() );
			
			int param = 1;
			ps.setDate( param++, Funcoes.strDateToSqlDate( txtDataini.getVlrString() ) );
			ps.setDate( param++, Funcoes.strDateToSqlDate( txtDatafim.getVlrString() ) );
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			if ( codprodini!=null && codprodfim!=null ) {
				ps.setInt( param++, codprodini.intValue() );
				ps.setInt( param++, codprodfim.intValue() );
			} else if ( codprodini!=null && codprodfim==null ) {
				ps.setInt( param++, codprodini.intValue() );
			}
			if ( !"".equals( codgrup )) {
				ps.setInt( param++, Aplicativo.iCodEmp );
				ps.setInt( param++, ListaCampos.getMasterFilial( "EQGRUPO" ) );
				ps.setString( param++, codgrup+"%" );
			}

			rs = ps.executeQuery();

			imprimiGrafico( rs, b, cab.toString() );

		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao consultar banco de dados !", true, con, err );
		}
	}

	private void imprimiGrafico( final ResultSet rs, final TYPE_PRINT bVisualizar, final String sCab ) {

		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "CPCOMPRA" ) );
		hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
		hParam.put( "FILTROS", sCab );
		hParam.put( "DESCPROD", txtDescProdIni.getVlrString() );
		hParam.put( "CODPROD", txtCodProdIni.getVlrInteger() == 0 ? null : txtCodProdIni.getVlrInteger() );

		dlGr = new FPrinterJob( "relatorios/NecesProducao.jasper", "Relatório de necessidade de produção", sCab, rs, hParam, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de Relatório de Análises!" + err.getMessage(), true, con, err );
			}
		}
	}

	public void setConexao( DbConnection con ) {

		super.setConexao( con );
		lcProdIni.setConexao( con );
		lcProdFim.setConexao( con );
		lcGrupo.setConexao( con );
	}
}
