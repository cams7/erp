/**
 * @version 03/05/2010 <BR>
 * @author Setpoint Informática Ltda./Cristian Ribeiro Mietlicki<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std.view.frame.report <BR>
 *         Classe:
 * @(#)FRVendasIpi.java <BR>
 * 
 *                      Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                      modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                      na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                      Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                      sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                      Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                      Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                      de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                      Tela de filtros para o relatório IPI.
 * 
 */

package org.freedom.modulos.lvf.view.frame.report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

public class FRIpi extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );
	
	private JCheckBoxPad cbEntradas = new JCheckBoxPad( "Entradas", "S", "N" );
	
	private JCheckBoxPad cbSaidas = new JCheckBoxPad( "Saídas", "S", "N" );

	public FRIpi() {

		super( false );
		setTitulo( "Relatório de IPI" );
		setAtribos( 50, 50, 355, 200 );

		montaTela();
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
		
		adic( cbEntradas, 57, 80, 120, 20);
		adic( cbSaidas, 179,80,120,20);

		Calendar cPeriodo = Calendar.getInstance();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataini.setVlrDate( cPeriodo.getTime() );
		
		cbEntradas.setVlrString( "S" );
		cbSaidas.setVlrString( "S" );
		
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

		sCab.append( "Perído de : " + Funcoes.dateToStrDate( txtDataini.getVlrDate() ) + "Até : " + Funcoes.dateToStrDate( txtDatafim.getVlrDate() ) );

		try {

			if("S".equals( cbSaidas.getVlrString())) {
			
				sql.append( "SELECT V.DTEMITVENDA ,T.ESTIPOMOV,SUM(V.VLRLIQVENDA), SUM(V.VLRBASEIPIVENDA) AS VALORBASEIPI,SUM(V.VLRIPIVENDA)AS VALORIPIVENDA " );
				sql.append( "FROM VDVENDA V,EQTIPOMOV T " );
				sql.append( "WHERE " );
				sql.append( "V.DTEMITVENDA BETWEEN ? AND ? AND " );
				sql.append( "V.CODTIPOMOV=T.CODTIPOMOV AND T.FISCALTIPOMOV='S' " );
				sql.append( "AND (NOT SUBSTR(V.STATUSVENDA,1,1)='C') GROUP BY V.DTEMITVENDA,T.ESTIPOMOV " );
				
			}
			
			if("S".equals( cbSaidas.getVlrString()) && "S".equals( cbEntradas.getVlrString())) {
				
				sql.append( "UNION ALL " );
			
			}
			
			if("S".equals( cbEntradas.getVlrString())) {
				
				sql.append( "SELECT C.DTENTCOMPRA DTEMITVENDA,T1.ESTIPOMOV,SUM(C.VLRLIQCOMPRA), " );
				sql.append( "SUM(C.VLRBASEIPICOMPRA) VALORBASEIPI,SUM(C.VLRIPICOMPRA*-1) VALORIPIVENDA " );
				sql.append( "FROM CPCOMPRA C,EQTIPOMOV T1 " );
				sql.append( "WHERE C.DTENTCOMPRA BETWEEN ? AND ? AND " );
				sql.append( "C.CODTIPOMOV=T1.CODTIPOMOV AND T1.FISCALTIPOMOV='S' " );
				sql.append( "GROUP BY C.DTENTCOMPRA,T1.ESTIPOMOV ORDER BY 1,3" );
				
			}

			/*
			 * if(! "".equals( txtCodGrupo.getVlrString())) { if(txtCodGrupo.getVlrString().trim().length()==12) { sql.append( " and pd.codgrup = '" + txtCodGrupo.getVlrString() + "'" ); } else { sql.append( " and pd.codgrup like '" + txtCodGrupo.getVlrString() + "%'" ); }
			 * 
			 * sCab.append( "\n Grupo:" + txtCodGrupo.getVlrString().trim() + "-" + txtDescGrupo.getVlrString().trim() ); }
			 * 
			 * if(txtCodCli.getVlrInteger()>0) { sql.append( " and vd.codempcl=? and vd.codfilialcl=? and vd.codcli=? " ); sCab.append( "\n Cliente:" + txtCodCli.getVlrString().trim() + "-" + txtRazCli.getVlrString().trim() ); }
			 * 
			 * if(txtCodProd.getVlrInteger()>0) { sql.append( " and iv.codemppd=? and iv.codfilialpd=? and iv.codprod=? " ); sCab.append( "\n Produto:" + txtCodProd.getVlrString().trim() + "-" + txtDescProd.getVlrString().trim() ); }
			 */

			ps = con.prepareStatement( sql.toString() );

			int param = 1;

			if("S".equals( cbSaidas.getVlrString())) {
				ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			}
			if("S".equals( cbEntradas.getVlrString())) {
				ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			}

			/*
			 * if ( txtCodCli.getVlrInteger() > 0 ) { ps.setInt( param++, lcCli.getCodEmp() ); ps.setInt( param++, lcCli.getCodFilial() ); ps.setInt( param++, txtCodCli.getVlrInteger() ); }
			 * 
			 * if ( txtCodProd.getVlrInteger() > 0 ) { ps.setInt( param++, lcProduto.getCodEmp() ); ps.setInt( param++, lcProduto.getCodFilial() ); ps.setInt( param++, txtCodProd.getVlrInteger() ); }
			 */
			rs = ps.executeQuery();

			imprimiGrafico( bVisualizar, rs, sCab.toString() );

			con.commit();

		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemInforma( this, "Erro ao buscar dados do relatório!" );
		}
	}

	private void imprimiGrafico( final TYPE_PRINT bVisualizar, final ResultSet rs, final String sCab ) {

		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "VDVENDA" ) );
		hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
		hParam.put( "FILTROS", sCab );

		dlGr = new FPrinterJob( "layout/rel/REL_VENDAS_IPI.jasper", "Relatório de IPI ", sCab, rs, hParam, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão do relatório!" + err.getMessage(), true, con, err );
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		// lcCli.setConexao( con );
		// lcComiss.setConexao( con );
		// lcGrupo.setConexao( con );
		// lcProduto.setConexao( con );
	}
}
