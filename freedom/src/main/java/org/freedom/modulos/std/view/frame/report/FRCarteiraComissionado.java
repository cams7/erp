/**
 * @version 07/10/2009 <BR>
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
 *                          Tela de filtros para o relatório ICMS por NCM e CFOP.
 * 
 */

package org.freedom.modulos.std.view.frame.report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

public class FRCarteiraComissionado extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodComiss = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeComiss = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private ListaCampos lcComiss = new ListaCampos( this );

	public FRCarteiraComissionado() {

		super( false );
		setTitulo( "Carteira de Clientes por Comissionado" );
		setAtribos( 50, 50, 355, 200 );

		montaListaCampos();
		montaTela();
	}

	private void montaListaCampos() {

		lcComiss.add( new GuardaCampo( txtCodComiss, "CodVend", "Cód.comiss.", ListaCampos.DB_PK, false ) );
		lcComiss.add( new GuardaCampo( txtNomeComiss, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, false ) );
		txtCodComiss.setTabelaExterna( lcComiss, null );
		txtCodComiss.setNomeCampo( "CodVend" );
		txtCodComiss.setFK( true );
		lcComiss.setReadOnly( true );
		lcComiss.montaSql( false, "VENDEDOR", "VD" );

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

		adic( new JLabelPad( "Cód.Comis." ), 7, 70, 90, 20 );
		adic( txtCodComiss, 7, 90, 90, 20 );

		adic( new JLabelPad( "Nome do comissionado" ), 100, 70, 227, 20 );
		adic( txtNomeComiss, 100, 90, 227, 20 );

		Calendar cPeriodo = Calendar.getInstance();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
		cPeriodo.set( Calendar.DAY_OF_MONTH, 1 );
		cPeriodo.set( Calendar.MONTH, 0 );
		cPeriodo.set( Calendar.YEAR, cPeriodo.get( Calendar.YEAR ) - 1 );
		txtDataini.setVlrDate( cPeriodo.getTime() );
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

		sCab.append( "Período de : " + Funcoes.dateToStrDate( txtDataini.getVlrDate() ) + "Até : " + Funcoes.dateToStrDate( txtDatafim.getVlrDate() ) );

		try {

			sql.append( "select V.codvend, V.nomevend, V.dddfonevend, V.fonevend, V.cidvend, C.codcli, C.razcli, " );
			sql.append( "C.endcli, C.numcli, C.dddcli, C.fonecli, COALESCE( M.nomemunic, C.cidcli ) cidcli, " );
			sql.append( "c.dddcelcli, c.celcli, c.dddfaxcli, c.faxcli, c.nomecli,c.emailcli " );
			sql.append( ", ( SELECT first 1 DTEMITVENDA FROM vdvenda VD, EQTIPOMOV TM " );
			sql.append( "WHERE VD.codvend = V.codvend AND VD.codfilialvd   = V.codfilial " );
			sql.append( " AND VD.codempvd = V.codemp" );
			sql.append( " AND VD.codcli = C.codcli" );
			sql.append( " AND VD.codfilialcl = C.codfilial" );
			sql.append( " AND VD.codempcl = C.codemp" );
			sql.append( " AND VD.codemptm = TM.codemp" );
			sql.append( " AND VD.codfilialtm = TM.codfilial" );
			sql.append( " AND VD.codtipomov = TM.codtipomov" );
			sql.append( " AND TM.fiscaltipomov = 'S'" );
			sql.append( " AND TM.somavdtipomov = 'S'" );
			sql.append( " AND substring(VD.statusvenda FROM 1 FOR 1 ) <> 'C' " );
			sql.append( " AND VD.DTEMITVENDA BETWEEN ? AND ? order by vd.dtemitvenda desc ) ultima_venda" );

			sql.append( ", ( SELECT first 1 vd.docvenda FROM vdvenda VD, EQTIPOMOV TM " );
			sql.append( "WHERE VD.codvend = V.codvend AND VD.codfilialvd   = V.codfilial " );
			sql.append( " AND VD.codempvd = V.codemp" );
			sql.append( " AND VD.codcli = C.codcli" );
			sql.append( " AND VD.codfilialcl = C.codfilial" );
			sql.append( " AND VD.codempcl = C.codemp" );
			sql.append( " AND VD.codemptm = TM.codemp" );
			sql.append( " AND VD.codfilialtm = TM.codfilial" );
			sql.append( " AND VD.codtipomov = TM.codtipomov" );
			sql.append( " AND TM.fiscaltipomov = 'S'" );
			sql.append( " AND TM.somavdtipomov = 'S'" );
			sql.append( " AND substring(VD.statusvenda FROM 1 FOR 1 ) <> 'C' " );
			sql.append( " AND VD.DTEMITVENDA BETWEEN ? AND ? order by vd.DTEMITVENDA desc ) doc_ultima_venda " );

			sql.append( " FROM sgmunicipio M, vdcliente C left outer join vdvendedor V on " );
			sql.append( " V.codvend = C.codvend AND V.codfilial = C.codfilialvd AND V.codemp = C.codempvd " );
			sql.append( " WHERE  C.codmunic = M.codmunic" );
			sql.append( " AND C.siglauf = M.siglauf AND C.codpais = M.codpais" );

			if ( txtCodComiss.getVlrInteger() > 0 ) {
				sql.append( " AND C.codempvd =? and C.codfilialvd = ? and C.codvend = ? " );
			}

			// sql.append( " order by 2, 13 DESC, 7 ASC" );
			sql.append( " order by 2 , 7" );

			ps = con.prepareStatement( sql.toString() );

			int param = 1;

			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

			if ( txtCodComiss.getVlrInteger() > 0 ) {
				ps.setInt( param++, lcComiss.getCodEmp() );
				ps.setInt( param++, lcComiss.getCodFilial() );
				ps.setInt( param++, txtCodComiss.getVlrInteger() );
			}

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
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "VDCLIENTE" ) );
		hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
		hParam.put( "FILTROS", sCab );

		dlGr = new FPrinterJob( "layout/rel/REL_CARTEIRA_COMISSIONADO.jasper", "Carteira de Clientes por Comissionado", sCab, rs, hParam, this );

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
		lcComiss.setConexao( con );
	}
}
