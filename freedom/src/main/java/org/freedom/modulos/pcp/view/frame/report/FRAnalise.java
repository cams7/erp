/**
 * @version 05/08/2008 <BR>
 * @author Setpoint Informática Ltda.
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.pcp <BR>
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
 *                    Tela para cadastro de estruturas de produtos.
 * 
 */

package org.freedom.modulos.pcp.view.frame.report;

import org.freedom.library.type.TYPE_PRINT;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.pcp.view.frame.crud.detail.FOP;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import net.sf.jasperreports.engine.JasperPrintManager;

public class FRAnalise extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodOP = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDataOP = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldFK txtRefProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCodLote = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private ListaCampos lcProd = new ListaCampos( this, "" );

	private ListaCampos lcOP = new ListaCampos( this, "" );

	public FRAnalise() {

		super( false );
		setTitulo( "Análises" );
		setAtribos( 50, 50, 350, 220 );

		montaTela();
		montaListaCampos();
	}

	private void montaTela() {

		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );
		JLabelPad lbPeriodo = new JLabelPad( "Período:", SwingConstants.CENTER );
		lbPeriodo.setOpaque( true );

		adic( lbPeriodo, 7, 1, 80, 20 );
		adic( lbLinha, 5, 10, 300, 45 );

		adic( new JLabelPad( "De:" ), 10, 25, 30, 20 );
		adic( txtDataini, 40, 25, 97, 20 );
		adic( new JLabelPad( "Até:" ), 152, 25, 37, 20 );
		adic( txtDatafim, 190, 25, 100, 20 );
		adic( new JLabelPad( "Cód.Prod" ), 7, 55, 80, 20 );
		adic( txtCodProd, 7, 75, 70, 20 );
		adic( new JLabelPad( "Descrição do produto" ), 83, 55, 200, 20 );
		adic( txtDescProd, 83, 75, 220, 20 );

		adic( new JLabelPad( "Cód.OP" ), 7, 95, 80, 20 );
		adic( txtCodOP, 7, 115, 70, 20 );
		adic( new JLabelPad( "Data Fabr." ), 83, 95, 200, 20 );
		adic( txtDataOP, 83, 115, 75, 20 );

		Calendar cPeriodo = Calendar.getInstance();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataini.setVlrDate( cPeriodo.getTime() );
	}

	private void montaListaCampos() {

		/**************
		 * Produto *
		 **************/

		lcProd.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcProd.add( new GuardaCampo( txtRefProd, "RefProd", "Referência do produto", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		txtCodProd.setTabelaExterna( lcProd, FProduto.class.getCanonicalName() );
		txtCodProd.setNomeCampo( "CodProd" );
		txtCodProd.setFK( true );
		lcProd.setReadOnly( true );
		lcProd.montaSql( false, "PRODUTO", "EQ" );

		/**************
		 * OP *
		 **************/

		lcOP.add( new GuardaCampo( txtCodOP, "CodOP", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcOP.add( new GuardaCampo( txtDataOP, "DtFabrOP", "Data de Fabricação", ListaCampos.DB_SI, false ) );
		lcOP.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_FK, false ) );
		txtCodOP.setTabelaExterna( lcOP, FOP.class.getCanonicalName() );
		txtCodOP.setNomeCampo( "CodOp" );
		txtCodOP.setFK( true );
		lcOP.setReadOnly( true );
		lcOP.montaSql( false, "OP", "PP" );
	}

	public void imprimir( TYPE_PRINT b ) {

		StringBuffer sql = new StringBuffer();
		StringBuffer sWhere = new StringBuffer();
		StringBuffer sCab = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			if ( txtCodProd.getVlrInteger() > 0 ) {

				sWhere.append( "and op.codprod= " + txtCodProd.getVlrInteger() );
			}
			if ( txtCodOP.getVlrInteger() > 0 ) {
				sWhere.append( "and op.codop= " + txtCodOP.getVlrInteger() );
			}

			sql.append( "select op.codprod,pd.descprod,op.codlote,op.dtfabrop,op.dtvalidpdop, " );
			sql.append( "ta.desctpanalise,ea.vlrmin,ea.vlrmax,ea.especificacao,cq.vlrafer,cq.descafer,pr.imgassresp,cq.dtins, " );
			sql.append( "op.codop, eq.casasdec, eq.codunid, eq.descunid,cq.idusualt,pr.nomeresp, pr.nomerelanal, pr.cargoresp, pr.identprofresp " );
			sql.append( "from ppopcq cq, ppop op,ppestruanalise ea,pptipoanalise ta, sgprefere5 pr,eqproduto pd, equnidade eq " );
			sql.append( "where " );
			sql.append( "op.codemp = ? and op.codfilial=? and op.seqop=cq.seqop " );
			sql.append( "and cq.codemp=op.codemp and cq.codfilial=op.codfilial and cq.codop=op.codop and cq.seqop=op.seqop " );
			sql.append( "and pr.codemp=7 and pr.codfilial=1 " );
			sql.append( "and ea.codemp=op.codemppd and ea.codfilial=op.codfilialpd and ea.codprod=op.codprod " );
			sql.append( "and ea.seqest=op.seqest " );
			sql.append( "and ta.codemp=ea.codempta and ta.codfilial=ea.codfilialta and ta.codtpanalise=ea.codtpanalise " );
			sql.append( "and ea.codestanalise=cq.codestanalise " );
			sql.append( "and cq.status='AP' " );
			sql.append( "and pd.codemp=op.codemppd and pd.codfilial=op.codfilialpd and pd.codprod=op.codprod " );
			sql.append( "and eq.codemp=ta.codempud and eq.codfilial=ta.codfilialud and eq.codunid=ta.codunid " );
			sql.append( "and op.dtfabrop between ? and ? " );
			sql.append( sWhere.toString() );

			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PPOP" ) );
			ps.setDate( 3, Funcoes.strDateToSqlDate( txtDataini.getVlrString() ) );
			ps.setDate( 4, Funcoes.strDateToSqlDate( txtDatafim.getVlrString() ) );
			rs = ps.executeQuery();

			sCab.append( "Período: " + txtDataini.getVlrString() + " Até: " + txtDatafim.getVlrString() );

			imprimiGrafico( rs, b, sCab.toString() );

		} catch ( SQLException err ) {

			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar análises", true, con, err );
		}
	}

	private void imprimiGrafico( final ResultSet rs, final TYPE_PRINT bVisualizar, final String sCab ) {

		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "CPCOMPRA" ) );
		hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
		hParam.put( "FILTROS", sCab );
		hParam.put( "DESCPROD", txtDescProd.getVlrString() );
		hParam.put( "CODPROD", txtCodProd.getVlrInteger() == 0 ? null : txtCodProd.getVlrInteger() );

		dlGr = new FPrinterJob( "relatorios/RelAnalise.jasper", "Relatório de Análises", sCab, rs, hParam, this );

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
		lcProd.setConexao( con );
		lcOP.setConexao( con );
	}
}
