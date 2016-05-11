/**
 * @version 24/03/2004 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FRGerContas.java <BR>
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
 *         Tela de opções para o relatório de gerenciamento de contas.
 * 
 */

package org.freedom.modulos.std.view.frame.report;

import org.freedom.infra.model.jdbc.DbConnection;
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Vector;

import net.sf.jasperreports.engine.JasperPrintManager;

public class FRGerContas extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private final int TAM_GRUPO = 14;

	private JTextFieldPad txtAno = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );

	private JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodGrup1 = new JTextFieldPad( JTextFieldPad.TP_STRING, TAM_GRUPO, 0 );

	private JTextFieldFK txtDescGrup1 = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodGrup2 = new JTextFieldPad( JTextFieldPad.TP_STRING, TAM_GRUPO, 0 );

	private JTextFieldFK txtDescGrup2 = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodMarca = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescMarca = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodTpCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTpCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtSiglaTpCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private JTextFieldPad txtPercFat = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 8, Aplicativo.casasDecFin );

	private JLabelPad lbAno = new JLabelPad( "Ano" );

	private JLabelPad lbPercFat = new JLabelPad( "% de relevância" );

	private JLabelPad lbCodVend = new JLabelPad( "Cód.comiss." );

	private JLabelPad lbDescVend = new JLabelPad( "Nome do comissionado" );

	private JLabelPad lbCodGrup1 = new JLabelPad( "Cód.grupo/somar" );

	private JLabelPad lbDescCodGrup1 = new JLabelPad( "Descrição do grupo/somar" );

	private JLabelPad lbCodGrup2 = new JLabelPad( "Cód.grupo/subtrair" );

	private JLabelPad lbDescCodGrup2 = new JLabelPad( "Descrição do grupo/subtrair" );

	private JLabelPad lbCodMarca = new JLabelPad( "Cód.marca" );

	private JLabelPad lbDescMarc = new JLabelPad( "Descrição da marca" );

	private JLabelPad lbCodTpCli = new JLabelPad( "Cód.tp.cli." );

	private JLabelPad lbDescTpCli = new JLabelPad( "Descrição do tipo de cliente" );

	private JCheckBoxPad cbVendas = new JCheckBoxPad( "Só vendas?", "S", "N" );

	private JCheckBoxPad cbCliPrinc = new JCheckBoxPad( "Mostrar no cliente principal?", "S", "N" );

	private JCheckBoxPad cbIncluiPed = new JCheckBoxPad( "Incluir pedidos não faturados?", "S", "N" );

	private JRadioGroup<?, ?> rgOrdemRel = null;

	private JRadioGroup<?, ?> rgOrdemRel1 = null;

	private JRadioGroup<?, ?> rgOrdemRel2 = null;

	private Vector<String> vLabOrdemRel = new Vector<String>();

	private Vector<String> vValOrdemRel = new Vector<String>();

	private ListaCampos lcGrup1 = new ListaCampos( this );

	private ListaCampos lcGrup2 = new ListaCampos( this );

	private ListaCampos lcVendedor = new ListaCampos( this );

	private ListaCampos lcMarca = new ListaCampos( this );

	private ListaCampos lcTpCli = new ListaCampos( this );

	private final int JAN = 0;

	private final int FEV = 1;

	private final int MAR = 2;

	private final int ABR = 3;

	private final int MAI = 4;

	private final int JUN = 5;

	private final int JUL = 6;

	private final int AGO = 7;

	private final int SET = 8;

	private final int OUT = 9;

	private final int NOV = 10;

	private final int DEZ = 11;

	double dbVendasGeral = 0.00;

	public FRGerContas() {

		setTitulo( "Gerenciamento de contas" );
		setAtribos( 80, 50, 695, 460 );

		txtAno.setRequerido( true );
		txtAno.setVlrInteger( new Integer( ( new GregorianCalendar() ).get( Calendar.YEAR ) ) );

		cbVendas.setVlrString( "S" );
		cbCliPrinc.setVlrString( "S" );
		cbIncluiPed.setVlrString( "N" );

		vLabOrdemRel.addElement( "Cód.cli." );
		vLabOrdemRel.addElement( "Razão" );
		vLabOrdemRel.addElement( "Categoria" );
		vLabOrdemRel.addElement( "Cidade" );
		vLabOrdemRel.addElement( "Classif." );
		vLabOrdemRel.addElement( "Valor" );

		vValOrdemRel.addElement( "C" );
		vValOrdemRel.addElement( "R" );
		vValOrdemRel.addElement( "T" );
		vValOrdemRel.addElement( "D" );
		vValOrdemRel.addElement( "S" );
		vValOrdemRel.addElement( "V" );

		rgOrdemRel = new JRadioGroup<String, String>( 1, 6, vLabOrdemRel, vValOrdemRel );
		rgOrdemRel.setVlrString( "T" );

		rgOrdemRel1 = new JRadioGroup<String, String>( 1, 6, vLabOrdemRel, vValOrdemRel );
		rgOrdemRel1.setVlrString( "S" );

		rgOrdemRel2 = new JRadioGroup<String, String>( 1, 6, vLabOrdemRel, vValOrdemRel );
		rgOrdemRel2.setVlrString( "V" );

		lcGrup1.add( new GuardaCampo( txtCodGrup1, "CodGrup", "Cód.grupo", ListaCampos.DB_PK, false ) );
		lcGrup1.add( new GuardaCampo( txtDescGrup1, "DescGrup", "Descrição do gurpo", ListaCampos.DB_SI, false ) );
		lcGrup1.montaSql( false, "GRUPO", "EQ" );
		lcGrup1.setReadOnly( true );
		txtCodGrup1.setTabelaExterna( lcGrup1, null );
		txtCodGrup1.setFK( true );
		txtCodGrup1.setNomeCampo( "CodGrup" );

		lcGrup2.add( new GuardaCampo( txtCodGrup2, "CodGrup", "Cód.grupo", ListaCampos.DB_PK, false ) );
		lcGrup2.add( new GuardaCampo( txtDescGrup2, "DescGrup", "Descrição do grupo", ListaCampos.DB_SI, false ) );
		lcGrup2.montaSql( false, "GRUPO", "EQ" );
		lcGrup2.setReadOnly( true );
		txtCodGrup2.setTabelaExterna( lcGrup2, null );
		txtCodGrup2.setFK( true );
		txtCodGrup2.setNomeCampo( "CodGrup" );

		lcVendedor.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.comiss.", ListaCampos.DB_PK, false ) );
		lcVendedor.add( new GuardaCampo( txtNomeVend, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, false ) );
		lcVendedor.montaSql( false, "VENDEDOR", "VD" );
		lcVendedor.setReadOnly( true );
		txtCodVend.setTabelaExterna( lcVendedor, null );
		txtCodVend.setFK( true );
		txtCodVend.setNomeCampo( "CodVend" );

		lcMarca.add( new GuardaCampo( txtCodMarca, "CodMarca", "Cód.marca", ListaCampos.DB_PK, false ) );
		lcMarca.add( new GuardaCampo( txtDescMarca, "DescMarca", "Descrição da marca", ListaCampos.DB_SI, false ) );
		lcMarca.montaSql( false, "MARCA", "EQ" );
		lcMarca.setReadOnly( true );
		txtCodMarca.setTabelaExterna( lcMarca, null );
		txtCodMarca.setFK( true );
		txtCodMarca.setNomeCampo( "CodMarca" );

		lcTpCli.add( new GuardaCampo( txtCodTpCli, "CodTipoCli", "Cód.tp.cli.", ListaCampos.DB_PK, false ) );
		lcTpCli.add( new GuardaCampo( txtDescTpCli, "DescTipoCli", "Descrição do tipo de cliente", ListaCampos.DB_SI, false ) );
		lcTpCli.add( new GuardaCampo( txtSiglaTpCli, "SiglaTipoCli", "Sigla", ListaCampos.DB_SI, false ) );
		lcTpCli.montaSql( false, "TIPOCLI", "VD" );
		lcTpCli.setReadOnly( true );
		txtCodTpCli.setTabelaExterna( lcTpCli, null );
		txtCodTpCli.setFK( true );
		txtCodTpCli.setNomeCampo( "CodTipoCli" );

		adic( lbAno, 7, 0, 100, 20 );
		adic( txtAno, 7, 20, 100, 20 );

		adic( new JLabelPad( "1º Ordem" ), 125, 0, 80, 20 );
		adic( rgOrdemRel, 120, 20, 520, 30 );
		adic( new JLabelPad( "2° Ordem" ), 125, 50, 80, 20 );
		adic( rgOrdemRel1, 120, 70, 520, 30 );
		adic( new JLabelPad( "3° Ordem" ), 125, 100, 80, 20 );
		adic( rgOrdemRel2, 120, 120, 520, 30 );

		adic( lbCodVend, 7, 150, 110, 20 );
		adic( txtCodVend, 7, 170, 110, 20 );
		adic( lbDescVend, 120, 150, 200, 20 );
		adic( txtNomeVend, 120, 170, 280, 20 );
		adic( lbCodGrup1, 7, 190, 110, 20 );
		adic( txtCodGrup1, 7, 210, 110, 20 );
		adic( lbDescCodGrup1, 120, 190, 200, 20 );
		adic( txtDescGrup1, 120, 210, 280, 20 );
		adic( lbCodGrup2, 7, 230, 110, 20 );
		adic( txtCodGrup2, 7, 250, 110, 20 );
		adic( lbDescCodGrup2, 120, 230, 200, 20 );
		adic( txtDescGrup2, 120, 250, 280, 20 );
		adic( lbCodMarca, 7, 270, 110, 20 );
		adic( txtCodMarca, 7, 290, 110, 20 );
		adic( lbDescMarc, 120, 270, 200, 20 );
		adic( txtDescMarca, 120, 290, 280, 20 );
		adic( lbCodTpCli, 7, 310, 110, 20 );
		adic( txtCodTpCli, 7, 330, 110, 20 );
		adic( lbDescTpCli, 120, 310, 200, 20 );
		adic( txtDescTpCli, 120, 330, 280, 20 );

		adic( lbPercFat, 7, 110, 100, 20 );
		adic( txtPercFat, 7, 130, 100, 20 );

		adic( cbVendas, 430, 170, 100, 20 );
		adic( cbCliPrinc, 430, 210, 250, 20 );
		adic( cbIncluiPed, 430, 250, 295, 20 );

	}

	private ResultSet rodaQuery() {

		String sSql = "";
		String sWhere = "";
		String sWhereCli = "";
		String sWhereTM = "";
		String sCodGrup1 = "";
		String sOrdemRel = "";
		String sOrdemRel1 = "";
		String sOrdemRel2 = "";
		String sOrderBy = "";
		String sOrderByTemp = "";
		String sCodGrup2 = "";
		String sFiltros1 = "";
		String sFiltros2 = "";
		double dbPercRel = 0.00;
		int iCodCli = 0;
		int iCodVend = 0;
		int iCodMarca = 0;
		int iCodTpCli = 0;
		int iParam = 1;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			sFiltros1 = "";
			sFiltros2 = "";
			sWhereTM += " AND TM.SOMAVDTIPOMOV='S' ";

			dbPercRel = txtPercFat.getVlrDouble().doubleValue();

			if ( cbCliPrinc.getVlrString().equals( "S" ) ) {
				sFiltros2 += ( !sFiltros2.equals( "" ) ? " / " : "" ) + "ADIC. CLIENTES PRINCIPAIS";
			}

			sCodGrup1 = txtCodGrup1.getVlrString().trim();
			sCodGrup2 = txtCodGrup2.getVlrString().trim();
			iCodVend = txtCodVend.getVlrInteger().intValue();
			iCodMarca = txtCodMarca.getVlrInteger().intValue();
			iCodTpCli = txtCodTpCli.getVlrInteger().intValue();
			sOrdemRel = rgOrdemRel.getVlrString();
			sOrdemRel1 = rgOrdemRel1.getVlrString();
			sOrdemRel2 = rgOrdemRel2.getVlrString();

			if ( !sCodGrup1.equals( "" ) ) {
				sWhere += "AND G.CODEMP=? AND G.CODFILIAL=? AND G.CODGRUP LIKE ? ";
				sFiltros1 += ( !sFiltros1.equals( "" ) ? " / " : "" ) + "G.: " + txtDescGrup1.getText().trim();
			}
			if ( !sCodGrup2.equals( "" ) ) {
				sWhere += "AND ( NOT P.CODGRUP=? ) ";
				sFiltros1 += ( !sFiltros1.equals( "" ) ? " / " : "" ) + " EXCL. G.: " + txtDescGrup2.getText().trim();
			}
			if ( iCodMarca != 0 ) {
				sWhere += " AND P.CODEMPMC=" + lcMarca.getCodEmp() + " AND P.CODFILIALMC=" + lcMarca.getCodFilial() + " AND P.CODMARCA=" + iCodMarca;
			}
			if ( iCodVend != 0 ) {
				sWhere += " AND V.CODVEND=? ";
				sWhereCli = " AND C.CODVEND=? ";
				sFiltros2 += ( !sFiltros2.equals( "" ) ? " / " : "" ) + " REPR.: " + iCodVend + "-" + txtNomeVend.getVlrString().trim();
			}

			if ( sOrdemRel.equals( "C" ) )
				sOrderBy = "1";
			else if ( sOrdemRel.equals( "R" ) )
				sOrderBy = "2";
			else if ( sOrdemRel.equals( "D" ) )
				sOrderBy = "3";
			else if ( sOrdemRel.equals( "T" ) )
				sOrderBy = "4";
			else if ( sOrdemRel.equals( "S" ) )
				sOrderBy = "5";
			else if ( sOrdemRel.equals( "V" ) )
				sOrderBy = "18";

			if ( ( sOrdemRel1.equals( "C" ) ) && ( !sOrdemRel1.equals( sOrdemRel ) ) )
				sOrderBy = sOrderBy + ",1";
			else if ( ( sOrdemRel1.equals( "R" ) ) && ( !sOrdemRel1.equals( sOrdemRel ) ) )
				sOrderBy = sOrderBy + ",2";
			else if ( ( sOrdemRel1.equals( "D" ) ) && ( !sOrdemRel1.equals( sOrdemRel ) ) )
				sOrderBy = sOrderBy + ",3";
			else if ( ( sOrdemRel1.equals( "T" ) ) && ( !sOrdemRel1.equals( sOrdemRel ) ) )
				sOrderBy = sOrderBy + ",4";
			else if ( ( sOrdemRel1.equals( "S" ) ) && ( !sOrdemRel1.equals( sOrdemRel ) ) )
				sOrderBy = sOrderBy + ",5";
			else if ( ( sOrdemRel1.equals( "V" ) ) && ( !sOrdemRel1.equals( sOrdemRel ) ) )
				sOrderBy = sOrderBy + ",18";

			if ( ( sOrdemRel2.equals( "C" ) ) && ( !sOrdemRel2.equals( sOrdemRel ) && !sOrdemRel2.equals( sOrdemRel1 ) ) ) {
				sOrderBy = sOrderBy + ",1";
				if ( sOrderBy.indexOf( ",18" ) == -1 )
					sOrderBy = sOrderBy + ",18";
				if ( sOrderBy.indexOf( ",2" ) == -1 )
					sOrderBy = sOrderBy + ",2";
			}
			else if ( ( sOrdemRel2.equals( "R" ) ) && ( !sOrdemRel2.equals( sOrdemRel ) && !sOrdemRel2.equals( sOrdemRel1 ) ) ) {
				sOrderBy = sOrderBy + ",2";
				if ( sOrderBy.indexOf( ",18" ) == -1 )
					sOrderBy = sOrderBy + ",18";
			}
			else if ( ( sOrdemRel2.equals( "D" ) ) && ( !sOrdemRel2.equals( sOrdemRel ) && !sOrdemRel2.equals( sOrdemRel1 ) ) ) {
				sOrderBy = sOrderBy + ",3";
				if ( sOrderBy.indexOf( ",18" ) == -1 )
					sOrderBy = sOrderBy + ",18";
				if ( sOrderBy.indexOf( ",2" ) == -1 )
					sOrderBy = sOrderBy + ",2";
			}
			else if ( ( sOrdemRel2.equals( "T" ) ) && ( !sOrdemRel2.equals( sOrdemRel ) && !sOrdemRel2.equals( sOrdemRel1 ) ) ) {
				sOrderBy = sOrderBy + ",4";
				if ( sOrderBy.indexOf( ",18" ) == -1 )
					sOrderBy = sOrderBy + ",18";
				if ( sOrderBy.indexOf( ",2" ) == -1 )
					sOrderBy = sOrderBy + ",2";
			}
			else if ( ( sOrdemRel2.equals( "S" ) ) && ( !sOrdemRel2.equals( sOrdemRel ) && !sOrdemRel2.equals( sOrdemRel1 ) ) ) {
				sOrderBy = sOrderBy + ",5";
				if ( sOrderBy.indexOf( ",18" ) == -1 )
					sOrderBy = sOrderBy + ",18";
				if ( sOrderBy.indexOf( ",2" ) == -1 )
					sOrderBy = sOrderBy + ",2";
			}
			else if ( ( sOrdemRel2.equals( "V" ) ) && ( !sOrdemRel2.equals( sOrdemRel ) && !sOrdemRel2.equals( sOrdemRel1 ) ) ) {
				sOrderBy = sOrderBy + ",18";
				if ( sOrderBy.indexOf( ",2" ) == -1 )
					sOrderBy = sOrderBy + ",2";
			}

			int iAno = txtAno.getVlrInteger().intValue();

			java.sql.Date dtIniJan = Funcoes.dateToSQLDate( Funcoes.getDataIniMes( JAN, iAno ) );
			java.sql.Date dtIniFev = Funcoes.dateToSQLDate( Funcoes.getDataIniMes( FEV, iAno ) );
			java.sql.Date dtIniMar = Funcoes.dateToSQLDate( Funcoes.getDataIniMes( MAR, iAno ) );
			java.sql.Date dtIniAbr = Funcoes.dateToSQLDate( Funcoes.getDataIniMes( ABR, iAno ) );
			java.sql.Date dtIniMai = Funcoes.dateToSQLDate( Funcoes.getDataIniMes( MAI, iAno ) );
			java.sql.Date dtIniJun = Funcoes.dateToSQLDate( Funcoes.getDataIniMes( JUN, iAno ) );
			java.sql.Date dtIniJul = Funcoes.dateToSQLDate( Funcoes.getDataIniMes( JUL, iAno ) );
			java.sql.Date dtIniAgo = Funcoes.dateToSQLDate( Funcoes.getDataIniMes( AGO, iAno ) );
			java.sql.Date dtIniSet = Funcoes.dateToSQLDate( Funcoes.getDataIniMes( SET, iAno ) );
			java.sql.Date dtIniOut = Funcoes.dateToSQLDate( Funcoes.getDataIniMes( OUT, iAno ) );
			java.sql.Date dtIniNov = Funcoes.dateToSQLDate( Funcoes.getDataIniMes( NOV, iAno ) );
			java.sql.Date dtIniDez = Funcoes.dateToSQLDate( Funcoes.getDataIniMes( DEZ, iAno ) );

			java.sql.Date dtFimJan = Funcoes.dateToSQLDate( Funcoes.getDataFimMes( JAN, iAno ) );
			java.sql.Date dtFimFev = Funcoes.dateToSQLDate( Funcoes.getDataFimMes( FEV, iAno ) );
			java.sql.Date dtFimMar = Funcoes.dateToSQLDate( Funcoes.getDataFimMes( MAR, iAno ) );
			java.sql.Date dtFimAbr = Funcoes.dateToSQLDate( Funcoes.getDataFimMes( ABR, iAno ) );
			java.sql.Date dtFimMai = Funcoes.dateToSQLDate( Funcoes.getDataFimMes( MAI, iAno ) );
			java.sql.Date dtFimJun = Funcoes.dateToSQLDate( Funcoes.getDataFimMes( JUN, iAno ) );
			java.sql.Date dtFimJul = Funcoes.dateToSQLDate( Funcoes.getDataFimMes( JUL, iAno ) );
			java.sql.Date dtFimAgo = Funcoes.dateToSQLDate( Funcoes.getDataFimMes( AGO, iAno ) );
			java.sql.Date dtFimSet = Funcoes.dateToSQLDate( Funcoes.getDataFimMes( SET, iAno ) );
			java.sql.Date dtFimOut = Funcoes.dateToSQLDate( Funcoes.getDataFimMes( OUT, iAno ) );
			java.sql.Date dtFimNov = Funcoes.dateToSQLDate( Funcoes.getDataFimMes( NOV, iAno ) );
			java.sql.Date dtFimDez = Funcoes.dateToSQLDate( Funcoes.getDataFimMes( DEZ, iAno ) );

			if ( dbPercRel > 0.00 ) {

				String sSqlGeral = "SELECT SUM(COALESCE(IV.VLRLIQITVENDA,0)) AS VENDASTOTAL " + "FROM VDVENDA V, VDITVENDA IV,EQPRODUTO P,EQGRUPO G,EQTIPOMOV TM " + "WHERE V.CODEMP=? AND V.CODFILIAL=? AND V.DTEMITVENDA BETWEEN ? AND ? "
						+ "AND  IV.CODEMP=V.CODEMP AND IV.CODFILIAL=V.CODFILIAL AND IV.CODVENDA=V.CODVENDA AND IV.TIPOVENDA=V.TIPOVENDA " + "AND P.CODEMP=IV.CODEMPPD AND P.CODFILIAL=IV.CODFILIALPD AND P.CODPROD=IV.CODPROD " + "AND G.CODEMP=P.CODEMPGP AND G.CODFILIAL=P.CODFILIALGP "
						+ "AND TM.CODEMP=V.CODEMPTM  AND TM.CODFILIAL=V.CODFILIALTM AND TM.CODTIPOMOV=V.CODTIPOMOV " + "AND ( NOT SUBSTR(V.STATUSVENDA,1,1)='C' ) " + sWhereTM + ( sCodGrup1.equals( "" ) ? " AND P.CODGRUP=G.CODGRUP " : " AND SUBSTR(P.CODGRUP,1," + sCodGrup1.length() + ")=G.CODGRUP " )
						+ sWhere;

				try {
					System.out.println( sSqlGeral );
					ps = con.prepareStatement( sSqlGeral );
					ps.setInt( iParam++, Aplicativo.iCodEmp );
					ps.setInt( iParam++, ListaCampos.getMasterFilial( "VDVENDA" ) );
					ps.setDate( iParam++, dtIniJan );
					ps.setDate( iParam++, dtFimDez );

					if ( !sCodGrup1.equals( "" ) ) {
						ps.setInt( iParam++, Aplicativo.iCodEmp );
						ps.setInt( iParam++, ListaCampos.getMasterFilial( "EQGRUPO" ) );
						ps.setString( iParam++, sCodGrup1 + ( sCodGrup1.length() < TAM_GRUPO ? "%" : "" ) );
					}
					if ( !sCodGrup2.equals( "" ) ) {
						ps.setString( iParam++, sCodGrup2 );
					}
					if ( iCodVend != 0 ) {
						ps.setInt( iParam++, iCodVend );
					}
					if ( iCodCli != 0 ) {
						ps.setInt( iParam++, iCodCli );
					}

					rs = ps.executeQuery();

					if ( rs.next() ) {
						dbVendasGeral = rs.getDouble( 1 );
						dbPercRel = dbPercRel / 100;

						System.out.println( "VALOR TOTAL DAS VENDAS:" + ( dbVendasGeral ) );
						dbVendasGeral = dbVendasGeral * dbPercRel;
						System.out.println( "VALOR A FILTRAR:" + dbVendasGeral );
					}
					int i = sOrderBy.indexOf( ",18" );
					sOrderByTemp = sOrderBy.substring( 0, i );
					if ( ( sOrderBy.length() - ( i + 3 ) ) > 1 )
						sOrderByTemp = sOrderByTemp + sOrderBy.substring( i + 3 );
					sOrderBy = "18," + sOrderByTemp;
				} catch ( Exception err ) {
					Funcoes.mensagemErro( this, "Erro consultando total de vendas.\n" + err.getMessage(), true, con, err );
					err.printStackTrace();
				} finally {
					rs = null;
					iParam = 1;
				}
			}
			else {
				dbVendasGeral = 0.00;
			}

			sSql = "SELECT C2.CODCLI,C2.RAZCLI,C2.CIDCLI,TI.SIGLATIPOCLI,CLA.SIGLACLASCLI," + "  SUM((SELECT (CAST((COALESCE(SUM(1),0)) AS INTEGER)) FROM TKHISTORICO HIS " + "WHERE HIS.CODEMPCL=C.CODEMP AND HIS.CODFILIALCL=C.CODFILIAL AND "
					+ "HIS.CODCLI=C.CODCLI AND HIS.TIPOHISTTK='V' AND HIS.DATAHISTTK BETWEEN ? AND ? )) AS JAN, " + "  SUM((SELECT (CAST((COALESCE(SUM(1),0)) AS INTEGER)) FROM TKHISTORICO HIS " + "WHERE HIS.CODEMPCL=C.CODEMP AND HIS.CODFILIALCL=C.CODFILIAL AND "
					+ "HIS.CODCLI=C.CODCLI AND HIS.TIPOHISTTK='V' AND HIS.DATAHISTTK BETWEEN ? AND ? )) AS FEV, " + "  SUM((SELECT (CAST((COALESCE(SUM(1),0)) AS INTEGER)) FROM TKHISTORICO HIS " + "WHERE HIS.CODEMPCL=C.CODEMP AND HIS.CODFILIALCL=C.CODFILIAL AND "
					+ "HIS.CODCLI=C.CODCLI AND HIS.TIPOHISTTK='V' AND HIS.DATAHISTTK BETWEEN ? AND ? )) AS MAR, " + "  SUM((SELECT (CAST((COALESCE(SUM(1),0)) AS INTEGER)) FROM TKHISTORICO HIS " + "WHERE HIS.CODEMPCL=C.CODEMP AND HIS.CODFILIALCL=C.CODFILIAL AND "
					+ "HIS.CODCLI=C.CODCLI AND HIS.TIPOHISTTK='V' AND HIS.DATAHISTTK BETWEEN ? AND ? )) AS ABR, " + "  SUM((SELECT (CAST((COALESCE(SUM(1),0)) AS INTEGER)) FROM TKHISTORICO HIS " + "WHERE HIS.CODEMPCL=C.CODEMP AND HIS.CODFILIALCL=C.CODFILIAL AND "
					+ "HIS.CODCLI=C.CODCLI AND HIS.TIPOHISTTK='V' AND HIS.DATAHISTTK BETWEEN ? AND ? )) AS MAI, " + "  SUM((SELECT (CAST((COALESCE(SUM(1),0)) AS INTEGER)) FROM TKHISTORICO HIS " + "WHERE HIS.CODEMPCL=C.CODEMP AND HIS.CODFILIALCL=C.CODFILIAL AND "
					+ "HIS.CODCLI=C.CODCLI AND HIS.TIPOHISTTK='V' AND HIS.DATAHISTTK BETWEEN ? AND ? )) AS JUN, " + "  SUM((SELECT (CAST((COALESCE(SUM(1),0)) AS INTEGER)) FROM TKHISTORICO HIS " + "WHERE HIS.CODEMPCL=C.CODEMP AND HIS.CODFILIALCL=C.CODFILIAL AND "
					+ "HIS.CODCLI=C.CODCLI AND HIS.TIPOHISTTK='V' AND HIS.DATAHISTTK BETWEEN ? AND ? )) AS JUL, " + "  SUM((SELECT (CAST((COALESCE(SUM(1),0)) AS INTEGER)) FROM TKHISTORICO HIS " + "WHERE HIS.CODEMPCL=C.CODEMP AND HIS.CODFILIALCL=C.CODFILIAL AND "
					+ "HIS.CODCLI=C.CODCLI AND HIS.TIPOHISTTK='V' AND HIS.DATAHISTTK BETWEEN ? AND ? )) AS AGO, " + "  SUM((SELECT (CAST((COALESCE(SUM(1),0)) AS INTEGER)) FROM TKHISTORICO HIS " + "WHERE HIS.CODEMPCL=C.CODEMP AND HIS.CODFILIALCL=C.CODFILIAL AND "
					+ "HIS.CODCLI=C.CODCLI AND HIS.TIPOHISTTK='V' AND HIS.DATAHISTTK BETWEEN ? AND ? )) AS SETE, " + "  SUM((SELECT (CAST((COALESCE(SUM(1),0)) AS INTEGER)) FROM TKHISTORICO HIS " + "WHERE HIS.CODEMPCL=C.CODEMP AND HIS.CODFILIALCL=C.CODFILIAL AND "
					+ "HIS.CODCLI=C.CODCLI AND HIS.TIPOHISTTK='V' AND HIS.DATAHISTTK BETWEEN ? AND ? )) AS OUT, " + "  SUM((SELECT (CAST((COALESCE(SUM(1),0)) AS INTEGER)) FROM TKHISTORICO HIS " + "WHERE HIS.CODEMPCL=C.CODEMP AND HIS.CODFILIALCL=C.CODFILIAL AND "
					+ "HIS.CODCLI=C.CODCLI AND HIS.TIPOHISTTK='V' AND HIS.DATAHISTTK BETWEEN ? AND ? )) AS NOV, "
					+ "  SUM((SELECT (CAST((COALESCE(SUM(1),0)) AS INTEGER)) FROM TKHISTORICO HIS "
					+ "WHERE HIS.CODEMPCL=C.CODEMP AND HIS.CODFILIALCL=C.CODFILIAL AND "
					+ "HIS.CODCLI=C.CODCLI AND HIS.TIPOHISTTK='V' AND HIS.DATAHISTTK BETWEEN ? AND ? )) AS DEZ, "

					// Vendas no ano

					+ " SUM((SELECT SUM(COALESCE(IV.VLRLIQITVENDA,0))" + " FROM VDVENDA V, VDITVENDA IV, VDVENDEDOR VD, EQPRODUTO P, EQGRUPO G,EQTIPOMOV TM" + " WHERE V.CODEMP=? AND V.CODFILIAL=? AND V.DTEMITVENDA BETWEEN ? AND ?"
					+ " AND V.CODEMPCL=C.CODEMP AND V.CODFILIALCL=C.CODFILIAL AND V.CODCLI=C.CODCLI" + " AND C2.CODEMP=C.CODEMPPQ AND C2.CODFILIAL=C.CODFILIALPQ AND C2.CODCLI=C.CODPESQ" + " AND  IV.CODEMP=V.CODEMP AND IV.CODFILIAL=V.CODFILIAL AND IV.CODVENDA=V.CODVENDA AND IV.TIPOVENDA=V.TIPOVENDA"
					+ " AND VD.CODEMP=V.CODEMPVD AND VD.CODFILIAL=V.CODFILIALVD AND VD.CODVEND=V.CODVEND AND VD.CODSETOR IS NOT NULL AND P.CODEMP=IV.CODEMPPD" + " AND P.CODFILIAL=IV.CODFILIALPD AND P.CODPROD=IV.CODPROD AND G.CODEMP=P.CODEMPGP AND G.CODFILIAL=P.CODFILIALGP "
					+ " AND TM.CODEMP=V.CODEMPTM  AND TM.CODFILIAL=V.CODFILIALTM AND TM.CODTIPOMOV=V.CODTIPOMOV AND ( NOT SUBSTR(V.STATUSVENDA,1,1)='C' )  AND TM.SOMAVDTIPOMOV='S'"
					+ sWhereTM
					+ ( sCodGrup1.equals( "" ) ? " AND P.CODGRUP=G.CODGRUP " : " AND SUBSTR(P.CODGRUP,1," + sCodGrup1.length() + ")=G.CODGRUP " )
					+ sWhere
					+ ") * -1) AS VENDASATUAL,"

					// Vendas no ano anterior 2

					+ " SUM((SELECT SUM(COALESCE(IV.VLRLIQITVENDA,0))"
					+ " FROM VDVENDA V, VDITVENDA IV, VDVENDEDOR VD, EQPRODUTO P, EQGRUPO G,EQTIPOMOV TM"
					+ " WHERE V.CODEMP=? AND V.CODFILIAL=? AND V.DTEMITVENDA BETWEEN ? AND ?"
					+ " AND V.CODEMPCL=C.CODEMP AND V.CODFILIALCL=C.CODFILIAL AND V.CODCLI=C.CODCLI"
					+ " AND C2.CODEMP=C.CODEMPPQ AND C2.CODFILIAL=C.CODFILIALPQ AND C2.CODCLI=C.CODPESQ"
					+ " AND  IV.CODEMP=V.CODEMP AND IV.CODFILIAL=V.CODFILIAL AND IV.CODVENDA=V.CODVENDA AND IV.TIPOVENDA=V.TIPOVENDA"
					+ " AND VD.CODEMP=V.CODEMPVD AND VD.CODFILIAL=V.CODFILIALVD AND VD.CODVEND=V.CODVEND AND VD.CODSETOR IS NOT NULL AND P.CODEMP=IV.CODEMPPD"
					+ " AND P.CODFILIAL=IV.CODFILIALPD AND P.CODPROD=IV.CODPROD AND G.CODEMP=P.CODEMPGP AND G.CODFILIAL=P.CODFILIALGP "
					+ " AND TM.CODEMP=V.CODEMPTM  AND TM.CODFILIAL=V.CODFILIALTM AND TM.CODTIPOMOV=V.CODTIPOMOV AND ( NOT SUBSTR(V.STATUSVENDA,1,1)='C' ) AND TM.SOMAVDTIPOMOV='S'"
					+ sWhereTM
					+ ( sCodGrup1.equals( "" ) ? " AND P.CODGRUP=G.CODGRUP " : " AND SUBSTR(P.CODGRUP,1," + sCodGrup1.length() + ")=G.CODGRUP " )
					+ sWhere
					+ ")) AS VENDASANTERIOR2,"

					// Vendas no ano anterior

					+ " SUM((SELECT SUM(COALESCE(IV.VLRLIQITVENDA,0))"
					+ " FROM VDVENDA V, VDITVENDA IV, VDVENDEDOR VD, EQPRODUTO P, EQGRUPO G,EQTIPOMOV TM "
					+ " WHERE V.CODEMP=? AND V.CODFILIAL=? AND V.DTEMITVENDA BETWEEN ? AND ?"
					+ " AND V.CODEMPCL=C.CODEMP AND V.CODFILIALCL=C.CODFILIAL AND V.CODCLI=C.CODCLI"
					+ " AND C2.CODEMP=C.CODEMPPQ AND C2.CODFILIAL=C.CODFILIALPQ AND C2.CODCLI=C.CODPESQ"
					+ " AND  IV.CODEMP=V.CODEMP AND IV.CODFILIAL=V.CODFILIAL AND IV.CODVENDA=V.CODVENDA AND IV.TIPOVENDA=V.TIPOVENDA"
					+ " AND VD.CODEMP=V.CODEMPVD AND VD.CODFILIAL=V.CODFILIALVD AND VD.CODVEND=V.CODVEND AND VD.CODSETOR IS NOT NULL AND P.CODEMP=IV.CODEMPPD"
					+ " AND P.CODFILIAL=IV.CODFILIALPD AND P.CODPROD=IV.CODPROD AND G.CODEMP=P.CODEMPGP AND G.CODFILIAL=P.CODFILIALGP "
					+ " AND TM.CODEMP=V.CODEMPTM  AND TM.CODFILIAL=V.CODFILIALTM AND TM.CODTIPOMOV=V.CODTIPOMOV AND ( NOT SUBSTR(V.STATUSVENDA,1,1)='C' )  AND TM.SOMAVDTIPOMOV='S'"
					+ sWhereTM
					+ ( sCodGrup1.equals( "" ) ? " AND P.CODGRUP=G.CODGRUP " : " AND SUBSTR(P.CODGRUP,1," + sCodGrup1.length() + ")=G.CODGRUP " )
					+ sWhere
					+ ")) AS VENDASANTERIOR,"

					// meta estimada para ano corrente
					+ " SUM((SELECT COALESCE(CM.VLRMETAVEND,0) FROM VDCLIMETAVEND CM WHERE CM.CODEMP=C.CODEMP AND"
					+ " CM.CODFILIAL=C.CODFILIAL AND CM.CODCLI=C.CODCLI AND"
					+ " ANOMETAVEND=("
					+ txtAno.getVlrInteger()
					+ "))) AS VENDASMETA"

					// From principal

					+ " FROM VDTIPOCLI TI, VDCLIENTE C, VDCLIENTE C2, VDCLASCLI CLA "

					// Where principal

					+ " WHERE CLA.CODEMP=C2.CODEMPCC AND CLA.CODFILIAL=C2.CODFILIALCC AND CLA.CODCLASCLI=C2.CODCLASCLI AND "
					+ ( cbCliPrinc.getVlrString().equals( "S" ) ? "C2.CODEMP=C.CODEMPPQ AND C2.CODFILIAL=C.CODFILIALPQ AND C2.CODCLI=C.CODPESQ AND " : "C2.CODEMP=C.CODEMP AND C2.CODFILIAL=C.CODFILIAL AND C2.CODCLI=C.CODCLI AND " )
					+ "TI.CODEMP=C2.CODEMPTI AND TI.CODFILIAL=C2.CODFILIALTI AND "
					+ "TI.CODTIPOCLI=C2.CODTIPOCLI " + ( iCodTpCli > 0 ? ( " AND TI.CODEMP=" + lcTpCli.getCodEmp() + " AND TI.CODFILIAL=" + lcTpCli.getCodFilial() + " AND TI.CODTIPOCLI=" + iCodTpCli ) : "" ) + sWhereCli

					+ " GROUP BY 1,2,3,4,5 " + "ORDER BY " + sOrderBy;

			try {
				ps = con.prepareStatement( sSql );
				ps.setDate( iParam++, dtIniJan );
				ps.setDate( iParam++, dtFimJan );
				ps.setDate( iParam++, dtIniFev );
				ps.setDate( iParam++, dtFimFev );
				ps.setDate( iParam++, dtIniMar );
				ps.setDate( iParam++, dtFimMar );
				ps.setDate( iParam++, dtIniAbr );
				ps.setDate( iParam++, dtFimAbr );
				ps.setDate( iParam++, dtIniMai );
				ps.setDate( iParam++, dtFimMai );
				ps.setDate( iParam++, dtIniJun );
				ps.setDate( iParam++, dtFimJun );
				ps.setDate( iParam++, dtIniJul );
				ps.setDate( iParam++, dtFimJul );
				ps.setDate( iParam++, dtIniAgo );
				ps.setDate( iParam++, dtFimAgo );
				ps.setDate( iParam++, dtIniSet );
				ps.setDate( iParam++, dtFimSet );
				ps.setDate( iParam++, dtIniOut );
				ps.setDate( iParam++, dtFimOut );
				ps.setDate( iParam++, dtIniNov );
				ps.setDate( iParam++, dtFimNov );
				ps.setDate( iParam++, dtIniDez );
				ps.setDate( iParam++, dtFimDez );

				ps.setInt( iParam++, Aplicativo.iCodEmp );
				ps.setInt( iParam++, ListaCampos.getMasterFilial( "VDVENDA" ) );

				ps.setDate( iParam++, Funcoes.dateToSQLDate( Funcoes.getDataIniMes( JAN, iAno ) ) );
				ps.setDate( iParam++, Funcoes.dateToSQLDate( Funcoes.getDataFimMes( DEZ, iAno ) ) );

				if ( !sCodGrup1.equals( "" ) ) {
					ps.setInt( iParam, Aplicativo.iCodEmp );
					ps.setInt( iParam + 1, ListaCampos.getMasterFilial( "EQGRUPO" ) );
					ps.setString( iParam + 2, sCodGrup1 + ( sCodGrup1.length() < TAM_GRUPO ? "%" : "" ) );
					iParam += 3;
				}
				if ( !sCodGrup2.equals( "" ) ) {
					ps.setString( iParam, sCodGrup2 );
					iParam += 1;
				}
				if ( iCodVend != 0 ) {
					ps.setInt( iParam, iCodVend );
					iParam += 1;
				}
				if ( iCodCli != 0 ) {
					ps.setInt( iParam, iCodCli );
					iParam += 1;
				}

				ps.setInt( iParam++, Aplicativo.iCodEmp );
				ps.setInt( iParam++, ListaCampos.getMasterFilial( "VDVENDA" ) );
				ps.setDate( iParam++, Funcoes.dateToSQLDate( Funcoes.getDataIniMes( JAN, iAno - 2 ) ) );
				ps.setDate( iParam++, Funcoes.dateToSQLDate( Funcoes.getDataFimMes( DEZ, iAno - 2 ) ) );

				if ( !sCodGrup1.equals( "" ) ) {
					ps.setInt( iParam, Aplicativo.iCodEmp );
					ps.setInt( iParam + 1, ListaCampos.getMasterFilial( "EQGRUPO" ) );
					ps.setString( iParam + 2, sCodGrup1 + ( sCodGrup1.length() < TAM_GRUPO ? "%" : "" ) );
					iParam += 3;
				}
				if ( !sCodGrup2.equals( "" ) ) {
					ps.setString( iParam, sCodGrup2 );
					iParam += 1;
				}

				if ( iCodVend != 0 ) {
					ps.setInt( iParam, iCodVend );
					iParam += 1;
				}
				if ( iCodCli != 0 ) {
					ps.setInt( iParam, iCodCli );
					iParam += 1;
				}

				ps.setInt( iParam++, Aplicativo.iCodEmp );
				ps.setInt( iParam++, ListaCampos.getMasterFilial( "VDVENDA" ) );
				ps.setDate( iParam++, Funcoes.dateToSQLDate( Funcoes.getDataIniMes( JAN, iAno - 1 ) ) );
				ps.setDate( iParam++, Funcoes.dateToSQLDate( Funcoes.getDataFimMes( DEZ, iAno - 1 ) ) );

				if ( !sCodGrup1.equals( "" ) ) {
					ps.setInt( iParam, Aplicativo.iCodEmp );
					ps.setInt( iParam + 1, ListaCampos.getMasterFilial( "EQGRUPO" ) );
					ps.setString( iParam + 2, sCodGrup1 + ( sCodGrup1.length() < TAM_GRUPO ? "%" : "" ) );
					iParam += 3;
				}
				if ( !sCodGrup2.equals( "" ) ) {
					ps.setString( iParam, sCodGrup2 );
					iParam += 1;
				}
				if ( iCodVend != 0 ) {
					ps.setInt( iParam, iCodVend );
					iParam += 1;
				}
				if ( iCodCli != 0 ) {
					ps.setInt( iParam, iCodCli );
					iParam += 1;
				}
				if ( iCodVend != 0 ) {
					ps.setInt( iParam, iCodVend );
					iParam += 1;
				}

				rs = ps.executeQuery();
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro executando a consulta.\n" + err.getMessage(), true, con, err );
				err.printStackTrace();
			}

		} finally {
			sSql = "";
			sWhere = "";
			sWhereCli = "";
			sWhereTM = "";
			sCodGrup1 = "";
			sOrdemRel = "";
			sOrdemRel1 = "";
			sOrdemRel2 = "";
			sOrderBy = "";
			sOrderByTemp = "";
			sCodGrup2 = "";
			sFiltros1 = "";
			sFiltros2 = "";
			ps = null;
		}
		return rs;
	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		FPrinterJob dlGr = null;
		ResultSet rsRel = rodaQuery();
		HashMap<String, Object> hParam = new HashMap<String, Object>();
		hParam.put( "VLRRELEV", new Double( dbVendasGeral ) );
		hParam.put( "ANO", txtAno.getVlrInteger() );
		hParam.put( "CODVEND", txtCodVend.getVlrInteger() );

		System.out.println( "Vai filtrar valor < que:" + dbVendasGeral );
		dbVendasGeral = 0.00;
		dlGr = new FPrinterJob( "relatorios/gercontas.jasper", "Gerenciamento de contas", "", rsRel, hParam, this );

		if ( bVisualizar==TYPE_PRINT.VIEW )
			dlGr.setVisible( true );
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório de gerenciamento de contas!" + err.getMessage(), true, con, err );
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcVendedor.setConexao( cn );
		lcGrup1.setConexao( cn );
		lcGrup2.setConexao( cn );
		lcMarca.setConexao( cn );
		lcTpCli.setConexao( cn );
	}
}
