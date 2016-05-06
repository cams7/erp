/**
 * @version 16/01/2008 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FRVendCliProd.java <BR>
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

package org.freedom.modulos.std.view.frame.report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
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
import org.freedom.library.type.TYPE_PRINT;

public class FRVendCliProd extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private ListaCampos lcCli = new ListaCampos( this, "CL" );

	private Vector<String> vLabs = new Vector<String>();

	private Vector<String> vVals = new Vector<String>();

	private JRadioGroup<?, ?> rgTipo = null;

	public FRVendCliProd() {

		setTitulo( "Ultimas Vendas de Cliente/Produto" );
		setAtribos( 50, 50, 340, 300 );

		montaTela();
		montaListaCampos();

	}

	private void montaTela() {

		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );
		JLabelPad lbPeriodo = new JLabelPad( "Periodo:", SwingConstants.CENTER );
		lbPeriodo.setOpaque( true );

		vLabs.addElement( "Texto" );
		vLabs.addElement( "Grafico" );
		vVals.addElement( "T" );
		vVals.addElement( "G" );

		rgTipo = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgTipo.setVlrString( "G" );

		adic( lbPeriodo, 15, 10, 80, 20 );
		adic( lbLinha, 7, 25, 295, 40 );

		adic( new JLabelPad( "De:", SwingConstants.CENTER ), 7, 35, 40, 20 );
		adic( txtDataini, 47, 35, 100, 20 );
		adic( new JLabelPad( "Até:", SwingConstants.CENTER ), 147, 35, 45, 20 );
		adic( txtDatafim, 193, 35, 100, 20 );
		adic( new JLabelPad( "Cód.Cli" ), 7, 75, 70, 20 );
		adic( txtCodCli, 7, 95, 70, 20 );
		adic( new JLabelPad( "Razão social do cliente" ), 80, 75, 170, 20 );
		adic( txtRazCli, 80, 95, 220, 20 );
		adic( rgTipo, 10, 170, 290, 30 );

		Calendar cPeriodo = Calendar.getInstance();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataini.setVlrDate( cPeriodo.getTime() );
	}

	private void montaListaCampos() {

		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		txtCodCli.setTabelaExterna( lcCli, null );
		txtCodCli.setNomeCampo( "CodCli" );
		txtCodCli.setFK( true );
		lcCli.setReadOnly( true );
		lcCli.montaSql( false, "CLIENTE", "VD" );
	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();
		StringBuffer sCab = new StringBuffer();
		StringBuffer sWhere = new StringBuffer();

		sCab.append( "de : " + txtDataini.getVlrDate() + "Até : " + txtDatafim.getVlrDate() );

		if ( txtRazCli.getVlrString().trim().length() > 0 ) {
			sWhere.append( "AND C.CODCLI=" + txtCodCli.getVlrInteger() );
		}

		try {

			sSQL.append( "SELECT C.RAZCLI, V.CODCLI, P.DESCPROD, IV.CODPROD, " );
			sSQL.append( "MAX(V.DTEMITVENDA) DTEMITVENDA, MAX(V.DOCVENDA) DOCVENDA, " );
			sSQL.append( "MAX(V.SERIE) SERIE, " );
			sSQL.append( "MAX (IV.VLRLIQITVENDA/(CASE WHEN IV.QTDITVENDA=0 THEN 1 ELSE IV.QTDITVENDA END)) PRECOVENDA " );
			sSQL.append( "FROM VDCLIENTE C, VDVENDA V, VDITVENDA IV, EQPRODUTO P " );
			sSQL.append( "WHERE C.CODEMP=V.CODEMPCL AND C.CODFILIAL=V.CODFILIALCL AND " );
			sSQL.append( "C.CODCLI=V.CODCLI AND C.CODEMP=? AND C.CODFILIAL=? AND " );
			sSQL.append( "IV.CODEMP=V.CODEMP AND IV.CODFILIAL=V.CODFILIAL AND " );
			sSQL.append( "IV.TIPOVENDA=V.TIPOVENDA AND IV.CODVENDA=V.CODVENDA AND " );
			sSQL.append( "P.CODEMP=IV.CODEMPPD AND P.CODFILIAL=IV.CODFILIALPD AND " );
			sSQL.append( "P.CODPROD=IV.CODPROD AND " );
			sSQL.append( "V.DTEMITVENDA BETWEEN ? AND ? " );
			sSQL.append( sWhere );
			sSQL.append( "GROUP BY C.RAZCLI, V.CODCLI, P.DESCPROD, IV.CODPROD " );

			ps = con.prepareStatement( sSQL.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
			ps.setDate( 3, Funcoes.strDateToSqlDate( txtDataini.getVlrString() ) );
			ps.setDate( 4, Funcoes.strDateToSqlDate( txtDatafim.getVlrString() ) );
			rs = ps.executeQuery();

			if ( "G".equals( rgTipo.getVlrString() ) ) {
				imprimiGrafico( bVisualizar, rs, sCab.toString() );
			}
			else {
				imprimiTexto( bVisualizar, rs, sCab.toString() );
			}

			con.commit();

		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemInforma( this, "Erro ao buscar dados da venda!" );
		}
	}

	public void imprimiTexto( final TYPE_PRINT bVisualizar, final ResultSet rs, final String sCab ) {

		PreparedStatement ps = null;
		StringBuffer sSQL = new StringBuffer();
		ImprimeOS imp = null;
		int linPag = 0;
		String sLinFina = StringFunctions.replicate( "-", 133 );
		String sLinDupla = StringFunctions.replicate( "=", 133 );

		try {

			imp = new ImprimeOS( "", con );
			linPag = imp.verifLinPag() - 1;
			imp.verifLinPag();
			imp.montaCab();
			imp.setTitulo( "Relatório de Vendas por cliente/produto" );
			imp.addSubTitulo( "RELATORIO DE VENDAS POR CLIENTE/PRODUTO   -   PERIODO DE :" + txtDataini.getVlrString() + " Até: " + txtDatafim.getVlrString() );
			imp.limpaPags();

			if ( sCab.length() > 0 ) {

				imp.addSubTitulo( sCab );
			}
			while ( rs.next() ) {

				if ( imp.pRow() >= linPag - 1 ) {
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "+" + sLinFina + "+" );
					imp.incPags();
					imp.eject();
				}

				if ( imp.pRow() == 0 ) {
					imp.impCab( 136, true );
					imp.pulaLinha( 0, imp.comprimido() );
					imp.say( 0, "|" + sLinFina + "|" );

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" );
					imp.say( 15, "CLIENTE" );
					imp.say( 37, "|" );
					imp.say( 50, "PRODUTO" );
					imp.say( 72, "|" );
					imp.say( 79, "VLR. UNIT." );
					imp.say( 94, "|" );
					imp.say( 100, "DT.ULT.COMPRA" );
					imp.say( 120, "|" );
					imp.say( 126, "DOC." );
					imp.say( 135, "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + sLinFina + "|" );
				}

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" );

				imp.say( 3, rs.getString( "RAZCLI" ) != null ? rs.getString( "RAZCLI" ) : "" );

			}
		} catch ( SQLException err ) {

			Funcoes.mensagemErro( this, "Erro ao imprmir relatório texto!\n" + err.getMessage(), true, con, err );
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {

			imp.preview( this );
		}
		else {

			imp.print();
		}
	}

	private void imprimiGrafico( final TYPE_PRINT bVisualizar, final ResultSet rs, final String sCab ) {

		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "VDVENDA" ) );
		hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
		hParam.put( "FILTROS", sCab );

		dlGr = new FPrinterJob( "relatorios/UltVendCli.jasper", "Ultimas Vendas por Cliente/Produto", sCab, rs, hParam, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório de Cliente por Produto!" + err.getMessage(), true, con, err );
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcCli.setConexao( con );
	}
}
