/**
 * @version 06/10/2009 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FRVolVendasProd.java <BR>
 * 
 *                          Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                          modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                          na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                          Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                          sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                          Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                          Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                          de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                          Tela de filtros para o relatório de volume de vendas por produto.
 * 
 */

package org.freedom.modulos.std.view.frame.report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import net.sf.jasperreports.engine.JasperPrintManager;

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
import org.freedom.library.type.TYPE_PRINT;

public class FRVolVendasProd extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private ListaCampos lcCli = new ListaCampos( this, "CL" );

	private ListaCampos lcComiss = new ListaCampos( this, "VD" );

	private JTextFieldPad txtCodGrupo = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldFK txtDescGrupo = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private final ListaCampos lcProduto = new ListaCampos( this );

	private final JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private ListaCampos lcGrupo = new ListaCampos( this );

	public FRVolVendasProd() {

		super( false );
		setTitulo( "Volume de vendas por produto" );
		setAtribos( 50, 50, 355, 290 );

		montaListaCampos();
		montaTela();
	}

	private void montaListaCampos() {

		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		txtCodCli.setTabelaExterna( lcCli, null );
		txtCodCli.setNomeCampo( "CodCli" );
		txtCodCli.setFK( true );
		lcCli.setReadOnly( true );
		lcCli.montaSql( false, "CLIENTE", "VD" );

		/*
		 * lcComiss.add( new GuardaCampo( txtCodComiss, "CodVend", "Cód.comiss.", ListaCampos.DB_PK, false ) ); lcComiss.add( new GuardaCampo( txtNomeComiss, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, false ) ); txtCodComiss.setTabelaExterna( lcComiss ); txtCodComiss.setNomeCampo(
		 * "CodVend" ); txtCodComiss.setFK( true ); lcComiss.setReadOnly( true ); lcComiss.montaSql( false, "VENDEDOR", "VD" );
		 */

		lcGrupo.add( new GuardaCampo( txtCodGrupo, "CodGrup", "Cód.grupo", ListaCampos.DB_PK, false ) );
		lcGrupo.add( new GuardaCampo( txtDescGrupo, "DescGrup", "Descrição do grupo", ListaCampos.DB_SI, false ) );
		lcGrupo.montaSql( false, "GRUPO", "EQ" );
		lcGrupo.setReadOnly( true );
		txtCodGrupo.setTabelaExterna( lcGrupo, null );
		txtCodGrupo.setFK( true );
		txtCodGrupo.setNomeCampo( "CodGrup" );

		lcProduto.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.produto", ListaCampos.DB_PK, false ) );
		lcProduto.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		txtCodProd.setTabelaExterna( lcProduto, null );
		txtCodProd.setNomeCampo( "CodProd" );
		txtCodProd.setFK( true );
		lcProduto.setReadOnly( true );
		lcProduto.montaSql( false, "PRODUTO", "EQ" );

	}

	private void montaTela() {

		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );
		JLabelPad lbPeriodo = new JLabelPad( "Periodo:", SwingConstants.CENTER );
		lbPeriodo.setOpaque( true );

		adic( lbPeriodo, 15, 5, 80, 20 );
		adic( lbLinha, 7, 15, 320, 45 );

		adic( new JLabelPad( "De:", SwingConstants.CENTER ), 17, 30, 40, 20 );
		adic( txtDataini, 57, 30, 100, 20 );
		adic( new JLabelPad( "Até:", SwingConstants.CENTER ), 157, 30, 45, 20 );
		adic( txtDatafim, 202, 30, 100, 20 );

		adic( new JLabelPad( "Cód.Cli" ), 7, 70, 90, 20 );
		adic( txtCodCli, 7, 90, 90, 20 );
		adic( new JLabelPad( "Razão social do cliente" ), 100, 70, 227, 20 );
		adic( txtRazCli, 100, 90, 227, 20 );

		adic( new JLabelPad( "Cód.Grupo" ), 7, 110, 90, 20 );
		adic( txtCodGrupo, 7, 130, 90, 20 );
		adic( new JLabelPad( "Descrição do grupo" ), 100, 110, 227, 20 );
		adic( txtDescGrupo, 100, 130, 227, 20 );

		adic( new JLabelPad( "Cód.Prod." ), 7, 150, 90, 20 );
		adic( txtCodProd, 7, 170, 90, 20 );
		adic( new JLabelPad( "Descrição do produto" ), 100, 150, 227, 20 );
		adic( txtDescProd, 100, 170, 227, 20 );

		Calendar cPeriodo = Calendar.getInstance();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataini.setVlrDate( cPeriodo.getTime() );
	}
	
	private boolean comRef() {

		boolean bRetorno = false;
		String sSQL = "SELECT USAREFPROD FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				if ( rs.getString( "UsaRefProd" ).trim().equals( "S" ) )
					bRetorno = true;
			}
			rs.close();
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage(), true, con, err );
		}
		return bRetorno;
	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		StringBuffer sCab = new StringBuffer();
		String usaRefProd = "N";
		String sOrdem;
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		if ( comRef() ) {
			usaRefProd = "S";
		}
		
		if ( "S".equals( usaRefProd ) ) {
			sOrdem = "IV.REFPROD";
			//sOrdenado = "ORDENADO POR REFERÊNCIA";
		}
		else {
			sOrdem =  "IV.CODPROD";
			//sOrdenado = "ORDENADO POR CODIGO";
		}

		sCab.append( "Perído de : " + Funcoes.dateToStrDate( txtDataini.getVlrDate() ) + "Até : " + Funcoes.dateToStrDate( txtDatafim.getVlrDate() ) );

		try {
			
			sql.append( "select iv.codprod, pd.descprod, pd.refprod,  sum(iv.qtditvenda) as qtd " );
			sql.append( "from vdvenda vd, vditvenda iv, eqproduto pd, eqtipomov tm " );
			sql.append( "where " );
			sql.append( "iv.codemp=vd.codemp and iv.codfilial=vd.codfilial and iv.codvenda=vd.codvenda and iv.tipovenda=vd.tipovenda and " );
			sql.append( "tm.codemp=vd.codemptm and tm.codfilial=vd.codfilialtm and tm.codtipomov=vd.codtipomov and " );
			sql.append( "pd.codemp=iv.codemppd and pd.codfilial=iv.codfilialpd and pd.codprod=iv.codprod and " );
			sql.append( "vd.dtemitvenda between ? and ? and substring(vd.statusvenda from 1 for 1)!='C' and vd.codemp=? and vd.codfilial=? and " );
			sql.append( "tm.SomaVdTipoMov='S' " );

			if ( !"".equals( txtCodGrupo.getVlrString() ) ) {
				if ( txtCodGrupo.getVlrString().trim().length() == 12 ) {
					sql.append( " and pd.codgrup = '" + txtCodGrupo.getVlrString() + "'" );
				}
				else {
					sql.append( " and pd.codgrup like '" + txtCodGrupo.getVlrString() + "%'" );
				}

				sCab.append( "\n Grupo:" + txtCodGrupo.getVlrString().trim() + "-" + txtDescGrupo.getVlrString().trim() );
			}

			if ( txtCodCli.getVlrInteger() > 0 ) {
				sql.append( " and vd.codempcl=? and vd.codfilialcl=? and vd.codcli=? " );
				sCab.append( "\n Cliente:" + txtCodCli.getVlrString().trim() + "-" + txtRazCli.getVlrString().trim() );
			}

			if ( txtCodProd.getVlrInteger() > 0 ) {
				sql.append( " and iv.codemppd=? and iv.codfilialpd=? and iv.codprod=? " );
				sCab.append( "\n Produto:" + txtCodProd.getVlrString().trim() + "-" + txtDescProd.getVlrString().trim() );
			}

			sql.append( "group by 1,2,3 " );
			sql.append( "order by iv.CodProd" );
			
			params.put( "USAREFPROD", usaRefProd );
			ps = con.prepareStatement( sql.toString() );

			int param = 1;

			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );

			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

			ps.setInt( param++, Aplicativo.iCodEmp );

			ps.setInt( param++, Aplicativo.iCodFilial );

			if ( txtCodCli.getVlrInteger() > 0 ) {
				ps.setInt( param++, lcCli.getCodEmp() );
				ps.setInt( param++, lcCli.getCodFilial() );
				ps.setInt( param++, txtCodCli.getVlrInteger() );
			}

			if ( txtCodProd.getVlrInteger() > 0 ) {
				ps.setInt( param++, lcProduto.getCodEmp() );
				ps.setInt( param++, lcProduto.getCodFilial() );
				ps.setInt( param++, txtCodProd.getVlrInteger() );
			}
			
			rs = ps.executeQuery();

			imprimiGrafico( bVisualizar, rs, sCab.toString(), usaRefProd.toString() );

			con.commit();

		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemInforma( this, "Erro ao buscar dados do relatório!" );
		}
	}

	private void imprimiGrafico( final TYPE_PRINT bVisualizar, final ResultSet rs, final String sCab, String usaRefProd ) {

		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "VDVENDA" ) );
		hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
		hParam.put( "FILTROS", sCab );
		hParam.put( "USAREFPROD", usaRefProd );

		dlGr = new FPrinterJob( "layout/rel/REL_VENDAS_PROD_01.jasper", "Volume de vendas por produto", sCab, rs, hParam, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório de Volume de vendas por produto!" + err.getMessage(), true, con, err );
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcCli.setConexao( con );
		lcComiss.setConexao( con );
		lcGrupo.setConexao( con );
		lcProduto.setConexao( con );
	}
}
