/**
 * @version 03/2007 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * @author Alex Rodrigues<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.rep <BR>
 *         Classe:
 * @(#)RelProduto.java <BR>
 * 
 *                     Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                     modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                     na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                     Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                     sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                     Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                     Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                     de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                     Relatorio de produtos, em dois modos: produtos e preços.
 * 
 */

package org.freedom.modulos.rep;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JLabel;

import net.sf.jasperreports.engine.JasperPrintManager;

public class RelProduto extends FRelatorio {

	private static final long serialVersionUID = 1;

	private final JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldFK txtRazFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JRadioGroup<String, String> rgModo;

	private JRadioGroup<String, String> rgOrdem;

	private final ListaCampos lcFor = new ListaCampos( this );

	public RelProduto() {

		super( false );
		setTitulo( "Relatorio de produtos" );
		setAtribos( 50, 50, 325, 250 );

		lcFor.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.for.", ListaCampos.DB_PK, false ) );
		lcFor.add( new GuardaCampo( txtRazFor, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );
		lcFor.montaSql( false, "FORNECEDOR", "RP" );
		lcFor.setQueryCommit( false );
		lcFor.setReadOnly( true );
		txtCodFor.setListaCampos( lcFor );
		txtCodFor.setTabelaExterna( lcFor, null );
		txtCodFor.setPK( true );
		txtCodFor.setNomeCampo( "CodFor" );

		Vector<String> labs = new Vector<String>();
		labs.add( "produtos" );
		labs.add( "preços" );
		Vector<String> vals = new Vector<String>();
		vals.add( "P" );
		vals.add( "R" );
		rgModo = new JRadioGroup<String, String>( 1, 2, labs, vals );

		Vector<String> labs1 = new Vector<String>();
		labs1.add( "Código" );
		labs1.add( "Descrição" );
		Vector<String> vals1 = new Vector<String>();
		vals1.add( "CODPROD" );
		vals1.add( "DESCPROD" );
		rgOrdem = new JRadioGroup<String, String>( 1, 2, labs1, vals1 );

		adic( new JLabel( "Modo :" ), 10, 10, 200, 20 );
		adic( rgModo, 10, 35, 290, 30 );
		adic( new JLabel( "Ordem do relatorio :" ), 10, 70, 200, 20 );
		adic( rgOrdem, 10, 95, 290, 30 );

		adic( new JLabel( "Cód.for." ), 10, 130, 77, 20 );
		adic( txtCodFor, 10, 150, 77, 20 );
		adic( new JLabel( "Razão social do fornecedor" ), 90, 130, 210, 20 );
		adic( txtRazFor, 90, 150, 210, 20 );
	}

	@ Override
	public void imprimir( TYPE_PRINT visualizar ) {

		try {

			String relatorio = "P".equals( rgModo.getVlrString() ) ? "rpproduto.jasper" : "rpprodutopreco.jasper";
			String modo = "P".equals( rgModo.getVlrString() ) ? "" : " ( preços )";
			String filtro = null;

			StringBuilder sql = new StringBuilder();

			sql.append( "SELECT P.CODPROD, P.REFPROD, P.DESCPROD, G.DESCGRUP, F.RAZFOR, P.REFPRODFOR, " );
			sql.append( "P.CODBARPROD, P.EMBALAPROD, P.PRECOPROD1, P.PRECOPROD2, P.PRECOPROD3 " );
			sql.append( "FROM RPPRODUTO P, RPGRUPO G, RPFORNECEDOR F " );
			sql.append( "WHERE P.CODEMP=? AND P.CODFILIAL=? " );
			sql.append( "AND P.CODEMPFO=F.CODEMP AND P.CODFILIALFO=F.CODFILIAL AND P.CODFOR=F.CODFOR " );
			sql.append( "AND P.CODEMPGP=G.CODEMP AND P.CODFILIALGP=G.CODFILIAL AND P.CODGRUP=G.CODGRUP " );
			if ( txtRazFor.getVlrString().trim().length() > 0 ) {
				sql.append( "AND F.CODFOR=" + txtCodFor.getVlrInteger().intValue() );
				filtro = "Fornecedor : " + txtRazFor.getVlrString().trim();
			}
			sql.append( "ORDER BY " + rgOrdem.getVlrString() );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "RPPRODUTO" ) );
			ResultSet rs = ps.executeQuery();

			HashMap<String, Object> hParam = new HashMap<String, Object>();

			hParam.put( "CODEMP", Aplicativo.iCodEmp );
			hParam.put( "REPORT_CONNECTION", con.getConnection() );

			FPrinterJob dlGr = new FPrinterJob( "modulos/rep/relatorios/" + relatorio, "PRODUTOS" + modo, filtro, rs, hParam, this );

			if ( visualizar == TYPE_PRINT.VIEW ) {
				dlGr.setVisible( true );
			}
			else {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			}
		} catch ( Exception e ) {
			Funcoes.mensagemErro( this, "Erro ao montar relatorio!\n" + e.getMessage() );
			e.printStackTrace();
		}

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcFor.setConexao( cn );
	}

}
