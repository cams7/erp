/**
 * @version 16/01/2008 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FRVendCliProd.java <BR>
 * 
 *                        Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                        modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                        na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                        Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                        sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                        Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                        Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                        de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                        Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.std.view.frame.report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

public class FRVendasCliProd extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCodComiss = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeComiss = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodTipoCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private Vector<String> vLabs = new Vector<String>();

	private Vector<String> vVals = new Vector<String>();

	private JRadioGroup<String, String> rgTipo = null;
	
	private Vector<String> vLabsRel = new Vector<String>();

	private Vector<String> vValsRel = new Vector<String>();

	private JRadioGroup<String, String> rgTipoDeRelatorio = null;

	private ListaCampos lcCli = new ListaCampos( this, "CL" );

	private ListaCampos lcComiss = new ListaCampos( this, "VD" );

	private ListaCampos lcTipoCli = new ListaCampos( this );
	
	private JCheckBoxPad cbObsItVenda = new JCheckBoxPad( "Imprimir Obs./ítens", "S", "N" );
	

	
	public FRVendasCliProd() {

		super( false );
		setTitulo( "Ultimas Vendas de Cliente/Produto" );
		setAtribos( 50, 50, 355, 380 );

		montaRadioGrupo();
		montaListaCampos();
		montaTela();
	}

	private void montaRadioGrupo() {

		vLabs.addElement( "Texto" );
		vLabs.addElement( "Grafico" );
		vVals.addElement( "T" );
		vVals.addElement( "G" );

		rgTipo = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgTipo.setVlrString( "G" );
		
		vLabsRel.addElement( "Retrato" );
		vLabsRel.addElement( "Paisagem" );
		vValsRel.addElement( "relatorios/UltVendCli.jasper" );
		vValsRel.addElement( "relatorios/UltVendCli_landscape.jasper" );

		rgTipoDeRelatorio = new JRadioGroup<String, String>( 1, 2, vLabsRel, vValsRel );
		rgTipoDeRelatorio.setVlrString( "relatorios/UltVendCli.jasper" );
		
	}

	private void montaListaCampos() {

		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		txtCodCli.setTabelaExterna( lcCli, null );
		txtCodCli.setNomeCampo( "CodCli" );
		txtCodCli.setFK( true );
		lcCli.setReadOnly( true );
		lcCli.montaSql( false, "CLIENTE", "VD" );

		lcComiss.add( new GuardaCampo( txtCodComiss, "CodVend", "Cód.comiss.", ListaCampos.DB_PK, false ) );
		lcComiss.add( new GuardaCampo( txtNomeComiss, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, false ) );
		txtCodComiss.setTabelaExterna( lcComiss, null );
		txtCodComiss.setNomeCampo( "CodVend" );
		txtCodComiss.setFK( true );
		lcComiss.setReadOnly( true );
		lcComiss.montaSql( false, "VENDEDOR", "VD" );

		lcTipoCli.add( new GuardaCampo( txtCodTipoCli, "CodTipoCli", "Cód.tp.cli.", ListaCampos.DB_PK, false ) );
		lcTipoCli.add( new GuardaCampo( txtDescTipoCli, "DescTipoCli", "Descrição do tipo de cliente", ListaCampos.DB_SI, false ) );
		txtCodTipoCli.setTabelaExterna( lcTipoCli, null );
		txtCodTipoCli.setNomeCampo( "CodTipoCli" );
		txtCodTipoCli.setFK( true );
		lcTipoCli.setReadOnly( true );
		lcTipoCli.montaSql( false, "TIPOCLI", "VD" );

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

		adic( new JLabelPad( "Cód.Tp.Cliente" ), 7, 70, 90, 20 );
		adic( txtCodTipoCli, 7, 90, 90, 20 );
		adic( new JLabelPad( "Descrição do tipo de cliente" ), 100, 70, 227, 20 );
		adic( txtDescTipoCli, 100, 90, 227, 20 );

		adic( new JLabelPad( "Cód.Cliente" ), 7, 110, 90, 20 );
		adic( txtCodCli, 7, 130, 90, 20 );
		adic( new JLabelPad( "Razão social do cliente" ), 100, 110, 227, 20 );
		adic( txtRazCli, 100, 130, 227, 20 );

		adic( new JLabelPad( "Cód.Comiss." ), 7, 150, 90, 20 );
		adic( txtCodComiss, 7, 170, 90, 20 );

		adic( new JLabelPad( "Nome do comissionado" ), 100, 150, 227, 20 );
		adic( txtNomeComiss, 100, 170, 227, 20 );

		adic( rgTipo, 7, 210, 320, 30 );
		
		adic( rgTipoDeRelatorio, 7, 250, 320, 30 );
		
		adic( cbObsItVenda,  	7,	290, 	200, 	20 );
		
		Calendar cPeriodo = Calendar.getInstance();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataini.setVlrDate( cPeriodo.getTime() );
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
		StringBuffer sWhereCli = new StringBuffer();
		StringBuffer sWhereComiss = new StringBuffer();

		sCab.append( "Período de : " + Funcoes.dateToStrDate( txtDataini.getVlrDate() ) + " Até : " + Funcoes.dateToStrDate( txtDatafim.getVlrDate() ) );

		try {

			sSQL.append( "select razcli_ret razcli, codcli_ret codcli, ");
			
			if( "N".equals( cbObsItVenda.getVlrString() )) {
				sSQL.append( "descprod_ret descprod, ");
			}
			else {
				sSQL.append( "coalesce(obsitvenda_ret,descprod_ret) as descprod, ");
			}
			
			sSQL.append( "codprod_ret codprod, " );
			sSQL.append( "dtemitvenda_ret dtemitvenda, docvenda_ret docvenda, serie_ret serie, precovenda_ret precovenda, refprod_ret refprod, qtdprod_ret qtditvenda " );
			sSQL.append( "from vdretultvdcliprod (?,?,?,?,?,?,?,?,?) " );

			ps = con.prepareStatement( sSQL.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );

			if ( txtRazCli.getVlrString().trim().length() > 0 ) {
				ps.setInt( 2, txtCodCli.getVlrInteger() );
			}
			else {
				ps.setNull( 2, Types.INTEGER );
			}

			ps.setInt( 3, ListaCampos.getMasterFilial( "VDVENDEDOR" ) );

			if ( txtNomeComiss.getVlrString().trim().length() > 0 ) {
				ps.setInt( 4, txtCodComiss.getVlrInteger() );
			}
			else {
				ps.setNull( 4, Types.INTEGER );
			}

			ps.setDate( 5, Funcoes.strDateToSqlDate( txtDataini.getVlrString() ) );
			ps.setDate( 6, Funcoes.strDateToSqlDate( txtDatafim.getVlrString() ) );

			if ( txtDescTipoCli.getVlrString().trim().length() > 0 ) {
				ps.setInt( 7, lcTipoCli.getCodEmp() );
				ps.setInt( 8, lcTipoCli.getCodFilial() );
				ps.setInt( 9, txtCodTipoCli.getVlrInteger() );
			}
			else {
				ps.setNull( 7, Types.INTEGER );
				ps.setNull( 8, Types.INTEGER );
				ps.setNull( 9, Types.INTEGER );
			}

			rs = ps.executeQuery();

			if ( "G".equals( rgTipo.getVlrString() ) ) {
				imprimiGrafico( bVisualizar, rs, sCab.toString() );
			}
			else {
				imprimiTexto( bVisualizar, rs );
			}

			con.commit();

		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemInforma( this, "Erro ao buscar dados da venda!" );
		}
	}

	public void imprimiTexto( final TYPE_PRINT bVisualizar, final ResultSet rs ) {

		PreparedStatement ps = null;
		StringBuffer sSQL = new StringBuffer();
		ImprimeOS imp = null;
		int linPag = 0;
		String sLinFina = StringFunctions.replicate( "-", 133 );
		String sLinDupla = StringFunctions.replicate( "=", 133 );
		int codcli = -1;
		boolean printCliente = true;

		try {
 
			imp = new ImprimeOS( "", con );
			linPag = imp.verifLinPag() - 1;
			imp.verifLinPag();
			imp.montaCab();
			imp.setTitulo( "Relatório de Vendas por cliente/produto" );
			imp.addSubTitulo( "PERIODO DE :" + txtDataini.getVlrString() + " ATÉ: " + txtDatafim.getVlrString() );
			imp.limpaPags();

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
					imp.say( 0, "|" + sLinDupla + "|" );

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" );
					imp.say( 3, "CLIENTE" );
					imp.say( 44, "|" );
					imp.say( 46, "PRODUTO" );
					imp.say( 88, "|" );
					imp.say( 93, "VLR. UNIT." );
					imp.say( 107, "|" );
					imp.say( 108, "DT.UT.COMPRA" );
					imp.say( 120, "|" );
					imp.say( 122, "DOC." );
					imp.say( 135, "|" );

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + sLinDupla + "|" );
				}

				if ( codcli == rs.getInt( "CODCLI" ) ) {
					printCliente = false;
				}
				else {
					if ( codcli != -1 ) { // -1 é o valor default, isso não imprime na primeira vez.
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|" + sLinFina + "|" );
					}
					codcli = rs.getInt( "CODCLI" );
					printCliente = true;
				}

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" );

				if ( printCliente ) {
					imp.say( 3, Funcoes.copy( rs.getString( "RAZCLI" ).trim(), 0, 40 ) );
				}

				imp.say( 44, "|" );
				imp.say( 46, Funcoes.copy( rs.getString( "DESCPROD" ).trim(), 0, 41 ) );
				imp.say( 88, "|" );
				imp.say( 90, rs.getBigDecimal( "PRECOVENDA" ) != null ? Funcoes.strDecimalToStrCurrency( 16, Aplicativo.casasDecPre, String.valueOf( rs.getBigDecimal( "PRECOVENDA" ) ) ) : "" );
				imp.say( 107, "|" );
				imp.say( 109, rs.getDate( "DTEMITVENDA" ) != null ? StringFunctions.sqlDateToStrDate( rs.getDate( "DTEMITVENDA" ) ) : "" );
				imp.say( 120, "|" );
				imp.say( 122, rs.getString( "DOCVENDA" ) != null ? Funcoes.copy( rs.getString( "DOCVENDA" ), 0, 12 ) : "" );
				imp.say( 135, "|" );

			}

			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "+" + sLinFina + "+" );
			imp.eject();
			imp.fechaGravacao();

		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao imprmir relatório texto!\n" + e.getMessage(), true, con, e );
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

		dlGr = new FPrinterJob( rgTipoDeRelatorio.getVlrString(), "Ultimas Vendas por Cliente/Produto", sCab, rs, hParam, this );

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
		lcCli.setConexao( cn );
		lcComiss.setConexao( cn );
		lcTipoCli.setConexao( cn );

	}
}
