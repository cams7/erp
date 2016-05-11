/**
 * @version 08/12/2000 <BR>

 * @author Setpoint Informática Ltda./Alex Rodrigues <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FRVendasCli <BR>
 * 
 *                 Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                 modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                 na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                 Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                 sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                 Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                 Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                 de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                 Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.std.view.frame.report;
	
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

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

public class FRVendasCli extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );
	
	private JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );
	
	private ListaCampos lcVendedor = new ListaCampos( this );
	
	private JCheckBoxPad cbListaFilial = new JCheckBoxPad( "Listar vendas das filiais ?", "S", "N" );

	private JCheckBoxPad cbDesc = new JCheckBoxPad( "Ordenar decrescente ?", "S", "N" );

	private JRadioGroup<?, ?> rgTipo = null;

	private JRadioGroup<?, ?> rgOrdem = null;

	private JRadioGroup<?, ?> rgFaturados = null;

	private JRadioGroup<?, ?> rgFinanceiro = null;
	
	private JRadioGroup<?, ?> rgEmitidos = null;
	
	private Vector<String> vLabsEmit = new Vector<String>();

	private Vector<String> vValsEmit = new Vector<String>();

	public FRVendasCli() {

		setTitulo( "Vendas por Cliente" );
		setAtribos( 80, 80, 340, 480 );

		GregorianCalendar cPeriodo = new GregorianCalendar();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataini.setVlrDate( cPeriodo.getTime() );


		Vector<String> vLabs = new Vector<String>();
		Vector<String> vVals = new Vector<String>();

		vLabs.addElement( "Grafico" );
		vLabs.addElement( "Texto" );
		vVals.addElement( "G" );
		vVals.addElement( "T" );
		rgTipo = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgTipo.setVlrString( "T" );

		Vector<String> vLabs1 = new Vector<String>();
		Vector<String> vVals1 = new Vector<String>();

		vLabs1.addElement( "Código" );
		vLabs1.addElement( "Razão" );
		vLabs1.addElement( "Valor" );
		vVals1.addElement( "C" );
		vVals1.addElement( "R" );
		vVals1.addElement( "V" );
		rgOrdem = new JRadioGroup<String, String>( 1, 3, vLabs1, vVals1 );
		rgOrdem.setVlrString( "V" );

		Vector<String> vLabs2 = new Vector<String>();
		Vector<String> vVals2 = new Vector<String>();

		vLabs2.addElement( "Faturado" );
		vLabs2.addElement( "Não Faturado" );
		vLabs2.addElement( "Ambos" );
		vVals2.addElement( "S" );
		vVals2.addElement( "N" );
		vVals2.addElement( "A" );
		rgFaturados = new JRadioGroup<String, String>( 3, 1, vLabs2, vVals2 );
		rgFaturados.setVlrString( "S" );

		Vector<String> vLabs3 = new Vector<String>();
		Vector<String> vVals3 = new Vector<String>();

		vLabs3.addElement( "Financeiro" );
		vLabs3.addElement( "Não Finaceiro" );
		vLabs3.addElement( "Ambos" );
		vVals3.addElement( "S" );
		vVals3.addElement( "N" );
		vVals3.addElement( "A" );
		rgFinanceiro = new JRadioGroup<String, String>( 3, 1, vLabs3, vVals3 );
		rgFinanceiro.setVlrString( "S" );
		
		vLabsEmit.addElement( "Emitidos" );
		vLabsEmit.addElement( "Não emitidos" );
		vLabsEmit.addElement( "Ambos" );
		vValsEmit.addElement( "S" );
		vValsEmit.addElement( "N" );
		vValsEmit.addElement( "A" );
		rgEmitidos = new JRadioGroup<String, String>( 3, 1, vLabsEmit, vValsEmit );
		rgEmitidos.setVlrString( "A" );

		cbDesc.setVlrString( "S" );

		adic( new JLabelPad( "Periodo:" ), 7, 10, 120, 20 );
		adic( new JLabelPad( "De:" ), 7, 30, 30, 20 );
		adic( txtDataini, 37, 30, 90, 20 );
		adic( new JLabelPad( "Até:" ), 140, 30, 30, 20 );
		adic( txtDatafim, 175, 30, 90, 20 );
		
		adic( new JLabelPad( "Cód." ), 7, 60, 70, 20 );
		adic( txtCodVend, 7, 80, 70, 20 );
		adic( new JLabelPad( "Nome Vendedor" ), 80, 60, 190, 20 );
		adic( txtNomeVend, 80, 80, 190, 20 );
		
		adic( rgTipo, 7, 120, 261, 30 );
		adic( new JLabelPad( "Ordem" ), 7, 150, 50, 20 );
		adic( rgOrdem, 7, 170, 261, 30 );
		adic( cbDesc, 7, 205, 250, 20 );
		adic( cbListaFilial, 7, 225, 200, 20 );
		adic( rgFaturados, 7, 250, 120, 70 );
		adic( rgFinanceiro, 148, 250, 120, 70 );
		adic( rgEmitidos, 7, 330, 120, 70 );
		
		lcVendedor.add( new GuardaCampo( txtCodVend, "CodVend", "Cód. Vend.", ListaCampos.DB_PK, false ) );
		lcVendedor.add( new GuardaCampo( txtNomeVend, "NomeVend", "Nome do Vendedor", ListaCampos.DB_SI, false ) );
		lcVendedor.montaSql( false, "VENDEDOR", "VD" );
		lcVendedor.setReadOnly( true );
		txtCodVend.setTabelaExterna( lcVendedor, null );
		txtCodVend.setFK( true );
		txtCodVend.setNomeCampo( "CodVend" );
	}

	public void imprimir( TYPE_PRINT bVisualizar ) {
		
		int param = 1;
		
		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();
		StringBuffer sCab = new StringBuffer();
		String sOrdem = "";
		String sWhere1 = "";
		String sWhere2 = "";

		try {

			if ( rgFaturados.getVlrString().equals( "S" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV='S' ";
				sCab.append( "FATURADO " );
			}
			else if ( rgFaturados.getVlrString().equals( "N" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV='N' ";
				if ( sCab.length() > 0 ) {
					sCab.append( " - " );
				}
				sCab.append( "NAO FATURADO " );
			}
		/*	else if ( rgFaturados.getVlrString().equals( "A" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV IN ('S','N') ";
			}*/

			if ( rgFinanceiro.getVlrString().equals( "S" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV='S' ";
				if ( sCab.length() > 0 ) {
					sCab.append( " - " );
				}
				sCab.append( "FINANCEIRO " );
			}
			else if ( rgFinanceiro.getVlrString().equals( "N" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV='N' ";
				if ( sCab.length() > 0 ) {
					sCab.append( " - " );
				}
				sCab.append( "NAO FINANCEIRO " );
			}
	/*		else if ( rgFinanceiro.getVlrString().equals( "A" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV IN ('S','N') ";
			}*/
			
			if ( rgEmitidos.getVlrString().equals( "S" ) ) {
				sWhere2 = " AND V.STATUSVENDA IN ('V2','V3','P3') ";
				sCab.append( " SO EMITIDOS ");
			}
			else if ( rgEmitidos.getVlrString().equals( "N" ) ) {
				sWhere2 = " AND V.STATUSVENDA NOT IN ('V2','V3','P3') ";
				sCab.append( " NAO EMITIDOS " );
			}

			if ( rgOrdem.getVlrString().equals( "C" ) ) {
				sOrdem = "S".equals( cbDesc.getVlrString() ) ? "V.CODCLI DESC, C.RAZCLI DESC, C.FONECLI DESC" : "V.CODCLI, C.RAZCLI, C.FONECLI";
			}
			else if ( rgOrdem.getVlrString().equals( "R" ) ) {
				sOrdem = "S".equals( cbDesc.getVlrString() ) ? "C.RAZCLI DESC, V.CODCLI DESC, C.FONECLI DESC" : "C.RAZCLI, V.CODCLI, C.FONECLI";
			}
			else if ( rgOrdem.getVlrString().equals( "V" ) ) {
				sOrdem = "S".equals( cbDesc.getVlrString() ) ? "5 DESC" : "5";
			}
			
			sSQL.append( "SELECT V.CODCLI, C.RAZCLI, C.DDDCLI, C.FONECLI, SUM(V.VLRLIQVENDA) AS VALOR " );
			sSQL.append( "FROM VDVENDA V, VDCLIENTE C, EQTIPOMOV TM " );
			sSQL.append( "WHERE V.CODEMP=? AND V.CODFILIAL=? " );
			sSQL.append( "AND V.CODEMPCL=C.CODEMP AND V.CODFILIALCL=C.CODFILIAL AND V.CODCLI=C.CODCLI " );
			sSQL.append( "AND V.CODEMPTM=TM.CODEMP AND V.CODFILIALTM=TM.CODFILIAL AND V.CODTIPOMOV=TM.CODTIPOMOV " );
			sSQL.append( "AND NOT SUBSTR(V.STATUSVENDA,1,1)='C' " );
			
			if ( txtCodVend.getVlrInteger() > 0 ){
				sSQL.append( "AND V.CODVEND=? " );
				sCab.append( "VENDEDOR: " );
				sCab.append(txtCodVend.getVlrInteger() ); 
				sCab.append( " - " ); 
				sCab.append(txtNomeVend.getVlrString() );
			}
			
			sSQL.append( "AND V.DTEMITVENDA BETWEEN ? AND ? " );
			sSQL.append( sWhere1 );
			sSQL.append( sWhere2 );
			sSQL.append( " GROUP BY V.CODCLI, C.RAZCLI, C.DDDCLI, C.FONECLI" );
			sSQL.append( " ORDER BY " + sOrdem );
			
			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
			
			if ( txtCodVend.getVlrInteger() > 0 ){
				ps.setInt( param++, txtCodVend.getVlrInteger() );
			}
			
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );	

			rs = ps.executeQuery();
			
			if ( "T".equals( rgTipo.getVlrString() ) ) {
				imprimirTexto( bVisualizar, rs, sCab.toString() );
			}
			else if ( "G".equals( rgTipo.getVlrString() ) ) {
				imprimirGrafico( bVisualizar, rs, sCab.toString() );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( Exception err ) {
			Funcoes.mensagemErro( this, "Erro ao montar relatório de vendas!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sCab = null;
			sOrdem = null;
			sWhere1 = null;
			sWhere2 = null;
			sSQL = null;
			System.gc();
		}
	}

	private void imprimirTexto( final TYPE_PRINT bVisualizar, final ResultSet rs, final String sCab ) {

		String sLinhaFina = StringFunctions.replicate( "-", 133 );
		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		int count = 1;

		try {

			linPag = imp.verifLinPag() - 1;
			imp.limpaPags();
			imp.montaCab();
			imp.setTitulo( "Relatório de Vendas por Cliente" );
			
			if ( txtCodVend.getVlrInteger() > 0 )
			{
				imp.addSubTitulo( "VENDAS REALIZADAS POR : " + txtNomeVend.getVlrString() + " -  PERIODO DE :" + txtDataini.getVlrString() + " ATE: " + txtDatafim.getVlrString() );
			}
			else
			{
				imp.addSubTitulo( "VENDAS -  PERIODO DE :" + txtDataini.getVlrString() + " ATE: " + txtDatafim.getVlrString() );
			}

			imp.addSubTitulo( sCab.toString() );

			while ( rs.next() ) {

				if ( imp.pRow() == linPag ) {
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "+" + sLinhaFina + "+" );
					imp.eject();
					imp.incPags();
				}

				if ( imp.pRow() == 0 ) {
					imp.impCab( 136, true );
					imp.say( 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|" + sLinhaFina + "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" );
					imp.say( 10, "|  Cod.cli" );
					imp.say( 23, "|  Razao Social" );
					imp.say( 75, "|  Telefone" );
					imp.say( 95, "|  Valor Total" );
					imp.say( 135, "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + sLinhaFina + "|" );
				}

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "| " + Funcoes.alinhaDir( count++, 6 ) );
				imp.say( 10, "| " + Funcoes.alinhaDir( rs.getInt( "CODCLI" ), 10 ) );
				imp.say( 23, "| " + rs.getString( "RAZCLI" ) );
				imp.say( 75, "| " + ( rs.getString( "DDDCli" ) != null ? "(" + rs.getString( "DDDCli" ) + ")" : "" ) + ( rs.getString( "FoneCli" ) != null ? Funcoes.setMascara( rs.getString( "FoneCli" ).trim(), "####-####" ) : "" ) );
				imp.say( 95, "| " + Funcoes.strDecimalToStrCurrency( 18, 2, rs.getString( 5 ) ) );
				imp.say( 135, "|" );
			}

			if ( count > 1 ) {
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "+" + sLinhaFina + "+" );
			}

			imp.eject();
			imp.fechaGravacao();

			if ( bVisualizar==TYPE_PRINT.VIEW ) {
				imp.preview( this );
			}
			else {
				imp.print();
			}

		} catch ( Exception err ) {
			Funcoes.mensagemErro( this, "Erro ao montar relatório de vendas!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	public void imprimirGrafico( final TYPE_PRINT bVisualizar, final ResultSet rs, final String sCab ) {

		FPrinterJob dlGr = new FPrinterJob( "relatorios/VendasCliente.jasper", "Vendas por Cliente", sCab, rs, null, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório de vendas por cliente!" + err.getMessage(), true, con, err );
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcVendedor.setConexao( cn );
	}
}
