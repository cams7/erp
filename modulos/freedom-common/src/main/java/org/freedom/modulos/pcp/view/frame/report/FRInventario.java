/**
 * @version 27/09/2007 <BR>
 * @author Setpoint Informática Ltda./Reginado Garcia Heua <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FRCpProd.java <BR>
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
package org.freedom.modulos.pcp.view.frame.report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

public class FRInventario extends FRelatorio  {

	private static final long serialVersionUID = 1L;
	
	private JTextFieldPad txtDtInventario = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );
	
	private JTextFieldPad txtCodGrupo = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldFK txtDescGrupo = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodMarca = new JTextFieldPad( JTextFieldPad.TP_STRING, 6, 0 );

	private JTextFieldFK txtDescMarca = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JCheckBoxPad cbProdSaldo = new JCheckBoxPad( "Somente produtos com estoque", "S", "N" );

	private JCheckBoxPad cbExibirLotesZ = new JCheckBoxPad( "Exibir lotes com saldos zerados", "S", "N" );

	private JCheckBoxPad cbMostraStatusOP = new JCheckBoxPad( "Mostra Status OP", "S", "N" );

	private ListaCampos lcGrupo = new ListaCampos( this );

	private ListaCampos lcMarca = new ListaCampos( this );

	private JRadioGroup<?, ?> rgOrdem = null;

	private Vector<String> vLabs = new Vector<String>();

	private Vector<String> vVals = new Vector<String>();

	public FRInventario() {
		super( false );
		setTitulo( "Relatório de Inventário + OP" );
		setAtribos( 50, 50, 345, 340 );
		
		montaTela();
		montaListaCampos();
		txtDtInventario.setVlrDate( new Date() );

	}

	public void montaTela() {

		vLabs.addElement( "Código" );
		vLabs.addElement( "Descrição" );
		vLabs.addElement( "Grupo" );
		vVals.addElement( "CODPROD" );
		vVals.addElement( "DESCPROD" );
		vVals.addElement( "CODGRUP, CODPROD" );

		rgOrdem = new JRadioGroup<String, String>( 1, 3, vLabs, vVals );
		rgOrdem.setVlrString( "CODPROD" );

		
		JLabel bordaData = new JLabel();
		bordaData.setBorder( BorderFactory.createEtchedBorder() );
		JLabel periodo = new JLabel( "Compra anterior a: ", SwingConstants.LEFT );
		periodo.setOpaque( true );

		
		adic( txtDtInventario, 7, 20, 100, 20, "Data Inventário" );
		adic( txtCodGrupo, 7, 60, 70, 20, "Cód.grupo" );
		adic( txtDescGrupo, 80, 60, 225, 20, "Descrição do Grupo" );
		adic( txtCodMarca, 7, 100, 70, 20, "Cód.marca" );
		adic( txtDescMarca, 80, 100, 225, 20, "Descrição da Marca" );
		adic( rgOrdem, 7, 140, 300, 35, "Ordenar por:" );
		adic( cbProdSaldo, 7, 178, 300, 20, "" );
		adic( cbExibirLotesZ, 7, 198, 300, 20, "" );
		adic( cbMostraStatusOP, 7, 218, 300, 20, "" );
		
		btExportXLS.setEnabled( true );
		
	}

	public void montaListaCampos() {

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

		/***********
		 * Marca *
		 ***********/
		lcMarca.add( new GuardaCampo( txtCodMarca, "CodMarca", "Cód.marca", ListaCampos.DB_PK, false ) );
		lcMarca.add( new GuardaCampo( txtDescMarca, "DescMarca", "Descrição da marca", ListaCampos.DB_SI, false ) );
		txtCodMarca.setTabelaExterna( lcMarca, null );
		txtCodMarca.setNomeCampo( "CodMarca" );
		txtCodMarca.setFK( true );
		lcMarca.setReadOnly( true );
		lcMarca.montaSql( false, "MARCA", "EQ" );

	}

	@ Override
	public void imprimir( TYPE_PRINT bVisualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		StringBuilder sFiltro = new StringBuilder();
		StringBuilder sCab = new StringBuilder();
		int param = 1;

		if ( txtDtInventario.getVlrDate() != null) {
			sCab.append( " Data do Inventário: " + txtDtInventario.getVlrString() );

		}
		
		if ( txtCodGrupo.getVlrString() != null && txtCodGrupo.getVlrString().trim().length() > 0 ) {
			sFiltro.append( " AND CODGRUP='" + txtCodGrupo.getVlrString() + "'" );
			sCab.append( " Grupo: " + txtDescGrupo.getVlrString() );

		}

		if ( txtCodMarca.getVlrString() != null && txtCodMarca.getVlrString().trim().length() > 0 ) {
			sFiltro.append( " AND CODMARCA='" + txtCodMarca.getVlrString() + "'" );
			sCab.append( " Marca: " + txtDescMarca.getVlrString() );

		}
		
		/*
		if ("N".equals( cbExibirLotesZ.getVlrString() )) {
			sFiltro.append( " AND SLDLOTE <> 0" );
			Parâmetros da procedure
			

		}*/
		
		
		sql.append("select refprod, descprod, sldprod, custounit, custotot ");  
		sql.append(", coalesce(codfabprod,0) codfabprod, coalesce(codbarprod,0) codbarprod, ativoprod, codlote, venctolote, codunid, codgrup, sldlote, siglagrup ");
		sql.append(", f.razfilial, f.dddfilial, f.fonefilial ");
		sql.append(", f.endfilial, f.numfilial, f.siglauf siglauff ");
		sql.append(", f.bairfilial, f.cnpjfilial,f.emailfilial ");
		sql.append(", f.unidfranqueada, f.wwwfranqueadora, f.marcafranqueadora "); 
		sql.append("from sgfilial f, eqrelpepssp(?,?,?,?,?,?,?,?,?,");
		sql.append("null,null,null,null,?,?)  where f.codemp=? and f.codfilial=? and SLDPROD!=0  AND ATIVOPROD IN ('S')");
		sql.append(" order by " + rgOrdem.getVlrString() );
		
		
		System.out.println( "SQL:" + sql.toString() );

		try {

			/*			    icodemp integer,
    			scodfilial smallint,
    			dtestoq date,
    			icodempmc integer,
    			scodfilialmc smallint,
    			ccodmarca char(6),
    			icodempgp integer,
    			scodfilialgp smallint,
    			ccodgrup varchar(14),
    			ctipocusto char(1),
    			icodempax integer,
    			scodfilialax smallint,
    			icodalmox integer,
    			cloteprod char(1),
    			soprodsaldo char(1))
*/
			ps = con.prepareStatement( sql.toString() );
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDtInventario.getVlrDate() ));
			if ( txtCodMarca.getVlrString() != null && txtCodMarca.getVlrString().trim().length() > 0) {
				ps.setInt( param++, Aplicativo.iCodEmp );
				ps.setInt( param++, ListaCampos.getMasterFilial( "EQMARCA" ) );
				ps.setString( param++, txtCodMarca.getVlrString() );
			}
			else {
				ps.setNull( param++, Types.INTEGER );
				ps.setNull( param++, Types.SMALLINT );
				ps.setNull( param++, Types.CHAR );
			}
			
			if (txtCodGrupo.getVlrString() != null && txtCodGrupo.getVlrString().trim().length() > 0) {
				ps.setInt( param++, Aplicativo.iCodEmp );
				ps.setInt( param++, ListaCampos.getMasterFilial( "EQGRUPO" ) );
				ps.setString( param++, txtCodGrupo.getVlrString() );
			}
			else {
				ps.setNull( param++, Types.INTEGER );
				ps.setNull( param++, Types.SMALLINT );
				ps.setNull( param++, Types.CHAR );
			}
			
			ps.setString( param++, cbExibirLotesZ.getVlrString());
			ps.setString( param++, cbProdSaldo.getVlrString());
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "SGFILIAL" ));
			rs = ps.executeQuery();

		} catch ( Exception e ) {

			Funcoes.mensagemErro( this, "Erro ao buscar dados do produto !\n" + e.getMessage() );
			e.printStackTrace();
		}

		if (bVisualizar==TYPE_PRINT.EXPORT) {
			if (btExportXLS.execute(rs, getTitle())) {
				Funcoes.mensagemInforma( this, "Arquivo exportado com sucesso !" );
			}
			try {
				rs.close();
				con.commit();
			} catch ( SQLException e ) {
				e.printStackTrace();
			}
		} else {
			imprimeGrafico( rs, bVisualizar, sCab.toString() );
		}
		
	}
	
	
	private String montaSubRelatorio(Integer codemp, Integer codfilial) {
		String query = "select op.sitop, op.qtdprevprodop, op.codop, op.seqop " +
				",f.dddfilial, f.fonefilial, f.endfilial, f.numfilial, f.siglauf siglauff, " +
				"f.bairfilial, f.cnpjfilial,f.emailfilial, f.unidfranqueada, f.wwwfranqueadora, " +
				"f.marcafranqueadora " +
				"from sgfilial f , ppop op where f.codemp=$CODEMP and op.codfilial=$CODFILIAL " +
				" and op.codemp=f.codemp and op.codfilial=f.codfilial and op.sitop not in ('FN','CA')  order by op.codop, op.seqop";
		query = query.replace( "$CODEMP", ""+codemp);
		query = query.replace( "$CODFILIAL", ""+codfilial);
		
		StringBuilder sql = new StringBuilder(query);
		return sql.toString();

	}

	

	public void imprimeGrafico( final ResultSet rs, final TYPE_PRINT bVisualizar, final String sCab ) {

		HashMap<String, Object> hParam = new HashMap<String, Object>();

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "EQPRODUTO" ) );
		hParam.put( "FILTROS", sCab );
		hParam.put( "SUBREPORT_DIR", "org/freedom/relatorios/");
		hParam.put( "SUBREPORT", montaSubRelatorio( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "EQPRODUTO" ) ) );
		hParam.put( "CONEXAO", con.getConnection() );
		hParam.put( "MOSTRAOP", cbMostraStatusOP.getVlrString() );

		FPrinterJob dlGr = new FPrinterJob( "relatorios/inventario.jasper", "Relatório de Inventário", null, rs, hParam, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {

			dlGr.setVisible( true );

		}
		else {
			try {

				JasperPrintManager.printReport( dlGr.getRelatorio(), true );

			} catch ( Exception err ) {

				Funcoes.mensagemErro( this, "Erro na impressão do relatório de Últimas compras/produto!\n" + err.getMessage(), true, con, err );
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcGrupo.setConexao( cn );
		lcMarca.setConexao( cn );
	}
}
