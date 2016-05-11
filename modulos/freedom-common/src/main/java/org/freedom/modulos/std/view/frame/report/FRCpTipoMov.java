/**
 * @version 08/12/2000 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FRCpTipoMov.java <BR>
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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;

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

public class FRCpTipoMov extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtDescFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodPlanoPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtDescPlanoPag = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodTipoMov = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtDescTipoMov = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private ListaCampos lcFor = new ListaCampos( this );

	private ListaCampos lcPlanoPag = new ListaCampos( this );

	private ListaCampos lcTipoMov = new ListaCampos( this );

	private JRadioGroup<?, ?> rgTipo = null;

	public FRCpTipoMov() {

		setTitulo( "Compras por tipo de movimento" );
		setAtribos( 50, 50, 345, 310 );

		montaListaCampos();
		montaTela();

	}

	public void montaListaCampos() {

		lcFor.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.for.", ListaCampos.DB_PK, false ) );
		lcFor.add( new GuardaCampo( txtDescFor, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );
		txtCodFor.setTabelaExterna( lcFor, null );
		txtCodFor.setNomeCampo( "CodFor" );
		txtCodFor.setFK( true );
		lcFor.setReadOnly( true );
		lcFor.montaSql( false, "FORNECED", "CP" );

		lcPlanoPag.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.plano.pag.", ListaCampos.DB_PK, false ) );
		lcPlanoPag.add( new GuardaCampo( txtDescPlanoPag, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		txtCodPlanoPag.setTabelaExterna( lcPlanoPag, null );
		txtCodPlanoPag.setNomeCampo( "CodPlanoPag" );
		txtCodPlanoPag.setFK( true );
		lcPlanoPag.setReadOnly( true );
		lcPlanoPag.montaSql( false, "PLANOPAG", "FN" );

		lcTipoMov.add( new GuardaCampo( txtCodTipoMov, "CodTipoMov", "Cód.Mov.", ListaCampos.DB_PK, false ) );
		lcTipoMov.add( new GuardaCampo( txtDescTipoMov, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false ) );
		txtCodTipoMov.setTabelaExterna( lcTipoMov, null );
		txtCodTipoMov.setNomeCampo( "CodTipoMov" );
		txtCodTipoMov.setFK( true );
		lcTipoMov.setReadOnly( true );
		lcTipoMov.montaSql( false, "TIPOMOV", "EQ" );
	}

	public void montaTela() {

		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );
		JLabelPad lbPeriodo = new JLabelPad( "   	Período:" );
		lbPeriodo.setOpaque( true );

		Vector<String> vLabs = new Vector<String>();
		Vector<String> vVals = new Vector<String>();

		vLabs.addElement( "Gráfico" );
		vLabs.addElement( "Texto" );
		vVals.addElement( "G" );
		vVals.addElement( "T" );

		rgTipo = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgTipo.setVlrString( "T" );

		adic( lbPeriodo, 7, 1, 80, 20 );
		adic( lbLinha, 5, 10, 300, 45 );
		adic( new JLabelPad( "De:" ), 10, 25, 30, 20 );
		adic( txtDataini, 40, 25, 97, 20 );
		adic( new JLabelPad( "Até:" ), 152, 25, 37, 20 );
		adic( txtDatafim, 190, 25, 100, 20 );
		adic( new JLabelPad( "Cód.for." ), 7, 60, 80, 20 );
		adic( txtCodFor, 7, 80, 80, 20 );
		adic( new JLabelPad( "Descrição do fornecedor" ), 90, 60, 210, 20 );
		adic( txtDescFor, 90, 80, 215, 20 );
		adic( new JLabelPad( "Cód.pl.pag." ), 7, 100, 80, 20 );
		adic( txtCodPlanoPag, 7, 120, 80, 20 );
		adic( new JLabelPad( "Descrição do plano de pagamento" ), 90, 100, 200, 20 );
		adic( txtDescPlanoPag, 90, 120, 215, 20 );
		adic( new JLabelPad( "Cód.Tipo Mov." ), 7, 140, 100, 20 );
		adic( txtCodTipoMov, 7, 160, 80, 20 );
		adic( new JLabelPad( "Descrição do tipo de movimento" ), 90, 140, 200, 20 );
		adic( txtDescTipoMov, 90, 160, 215, 20 );
		adic( rgTipo, 7, 190, 300, 30 );

		Calendar cPeriodo = Calendar.getInstance();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataini.setVlrDate( cPeriodo.getTime() );
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcFor.setConexao( cn );
		lcPlanoPag.setConexao( cn );
		lcTipoMov.setConexao( cn );
	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sSQL = new StringBuilder();
		String sWhere = "";
		BigDecimal bTotal = null;
		StringBuilder sCab = new StringBuilder();

		try {

			bTotal = new BigDecimal( "0" );

			if ( txtCodFor.getVlrInteger().intValue() > 0 ) {

				sWhere += " AND C.CODFOR = " + txtCodFor.getVlrInteger().intValue();
				sCab.append( "FORNECEDOR : " + txtDescFor.getVlrString() );

			}
			if ( txtCodPlanoPag.getVlrInteger().intValue() > 0 ) {

				sWhere += " AND C.CODPLANOPAG = " + txtCodPlanoPag.getVlrInteger().intValue();
				sCab.append( "PLANO DE PAGAMENTO: " + txtDescPlanoPag.getVlrString() );

			}
			if ( txtCodTipoMov.getVlrInteger().intValue() > 0 ) {

				sWhere += " AND C.CODTIPOMOV = " + txtCodTipoMov.getVlrInteger().intValue();
				sCab.append( "TIPO DE MOVIMENTO: " + txtDescTipoMov.getVlrString() );

			}

			sSQL.append( "SELECT C.CODCOMPRA, C.DOCCOMPRA, C.DTEMITCOMPRA, C.DTENTCOMPRA, " );
			sSQL.append( "F.NOMEFOR, PG.DESCPLANOPAG, IT.CODITCOMPRA, IT.CODPROD, PD.DESCPROD, " );
			sSQL.append( "IT.CODLOTE, IT.QTDITCOMPRA," );

			if ( getPrefere() ) {
				sSQL.append( "IT.PRECOITCOMPRA AS PRECO, IT.VLRLIQITCOMPRA, C.VLRLIQCOMPRA " );
			}
			else {
				sSQL.append( "PD.PRECOBASEPROD AS PRECO,  " );
				sSQL.append( "(IT.QTDITCOMPRA*PD.PRECOBASEPROD) AS VLRLIQITCOMPRA, " );
				sSQL.append( "(SELECT SUM(PD2.PRECOBASEPROD*IT2.QTDITCOMPRA) " );
				sSQL.append( "FROM EQPRODUTO PD2, CPITCOMPRA IT2 " );
				sSQL.append( "WHERE PD2.CODEMP=IT2.CODEMPPD AND PD2.CODFILIAL=IT2.CODFILIALPD AND " );
				sSQL.append( "PD2.CODPROD=IT2.CODPROD AND IT2.CODEMP=C.CODEMP AND IT2.CODFILIAL=C.CODFILIAL AND " );
				sSQL.append( "IT2.CODCOMPRA=C.CODCOMPRA) VLRLIQCOMPRA " );
			}

			sSQL.append( "FROM CPCOMPRA C, CPITCOMPRA IT, CPFORNECED F, FNPLANOPAG PG, EQPRODUTO PD " );
			sSQL.append( "WHERE C.CODEMP=? AND C.CODFILIAL=? " );
			sSQL.append( "AND C.CODEMPFR=F.CODEMP AND C.CODFILIALFR=F.CODFILIAL AND C.CODFOR=F.CODFOR " );
			sSQL.append( "AND C.CODEMPPG=PG.CODEMP AND C.CODFILIALPG=PG.CODFILIAL AND C.CODPLANOPAG=PG.CODPLANOPAG " );
			sSQL.append( "AND C.CODEMP=IT.CODEMP AND C.CODFILIAL=IT.CODFILIAL AND C.CODCOMPRA=IT.CODCOMPRA " );
			sSQL.append( "AND IT.CODEMPPD=PD.CODEMP AND IT.CODFILIALPD=PD.CODFILIAL AND IT.CODPROD=PD.CODPROD " );
			sSQL.append( "AND C.DTENTCOMPRA BETWEEN ? AND ? " );
			sSQL.append( sWhere );
			sSQL.append( " ORDER BY C.CODCOMPRA, IT.CODITCOMPRA" );

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
			ps.setDate( 3, Funcoes.strDateToSqlDate( txtDataini.getVlrString() ) );
			ps.setDate( 4, Funcoes.strDateToSqlDate( txtDatafim.getVlrString() ) );
			rs = ps.executeQuery();

			if ( "G".equals( rgTipo.getVlrString() ) ) {
				imprimirGrafico( bVisualizar, rs, sCab.toString() );

			}
			else {
				imprimirTexto( bVisualizar, rs, sCab.toString() );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro consulta tabela de tipo de movimento!\n" + err.getMessage(), true, con, err );

		} finally {
			System.gc();
		}
	}

	private boolean getPrefere() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();

		boolean retorno = false;

		try {

			sSQL.append( "SELECT PRECOCPREL " );
			sSQL.append( "FROM SGPREFERE1 " );
			sSQL.append( "WHERE CODEMP=? AND CODFILIAL=?" );

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

			rs = ps.executeQuery();

			if ( rs.next() ) {

				if ( "S".equals( rs.getString( "PRECOCPREL" ) ) ) {

					retorno = true;
				}
				else {
					retorno = false;
				}
			}

		} catch ( Exception e ) {

			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar preferencias!" + e.getMessage() );
		}
		return retorno;
	}

	private void imprimirTexto( final TYPE_PRINT bVisualizar, final ResultSet rs, final String sCab ) {

		String sSQL = null;
		String sWhere = "";
		BigDecimal bTotal = new BigDecimal( 0 );
		BigDecimal bQtdtot = new BigDecimal( 0 );
		ImprimeOS imp = null;
		int linPag = 0;
		int iparam = 1;
		int iCab = 7;
		int iCodCompra = 0;

		try {

			imp = new ImprimeOS( "", con );
			linPag = imp.verifLinPag() - 1;
			imp.montaCab();
			imp.setTitulo( "Relatório de Compras" );
			imp.addSubTitulo( "RELATORIO DE COMPRAS " );
			imp.addSubTitulo( "PERIODO DE: " + txtDataini.getVlrString() + " Até: " + txtDatafim.getVlrString() );
			imp.limpaPags();

			while ( rs.next() ) {

				if ( imp.pRow() >= linPag ) {
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
					imp.eject();
					imp.incPags();
				}
				if ( imp.pRow() == 0 ) {
					imp.impCab( 136, true );
					imp.say( imp.pRow(), 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|" + StringFunctions.replicate( "=", 133 ) + "|" );
				}
				if ( iCodCompra != rs.getInt( "CODCOMPRA" ) ) {
					iCodCompra = rs.getInt( "CODCOMPRA" );
					if ( imp.pRow() > iCab ) {
						imp.say( imp.pRow() + 1, 0, imp.comprimido() );
						imp.say( imp.pRow(), 0, "|" + StringFunctions.replicate( "=", 133 ) + "|" );
					}
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "| N. Compra" );
					imp.say( imp.pRow(), 13, "| Doc" );
					imp.say( imp.pRow(), 22, "| Fornecedor" );
					imp.say( imp.pRow(), 58, "| Plano de Pagamento" );
					imp.say( imp.pRow(), 94, "| Valor" );
					imp.say( imp.pRow(), 109, "|  Emissao" );
					imp.say( imp.pRow(), 122, "|  Entrada" );
					imp.say( imp.pRow(), 135, "|" );
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "| " + ( rs.getString( "CODCOMPRA" ) != null ? rs.getString( "CODCOMPRA" ) : "" ) );
					imp.say( imp.pRow(), 13, "| " + ( rs.getString( "DOCCOMPRA" ) != null ? rs.getString( "DOCCOMPRA" ) : "" ) );
					imp.say( imp.pRow(), 22, "| " + ( rs.getString( "NOMEFOR" ) != null ? ( rs.getString( "NOMEFOR" ).length() > 34 ? rs.getString( "NOMEFOR" ).substring( 0, 34 ) : rs.getString( "NOMEFOR" ) ) : "" ) );
					imp.say( imp.pRow(), 58, "| " + ( rs.getString( "DESCPLANOPAG" ) != null ? ( rs.getString( "DESCPLANOPAG" ).length() > 34 ? rs.getString( "DESCPLANOPAG" ).substring( 0, 34 ) : rs.getString( "DESCPLANOPAG" ) ) : "" ) );
					imp.say( imp.pRow(), 94, "| " + Funcoes.strDecimalToStrCurrency( 10, 2, ( rs.getString( "VLRLIQCOMPRA" ) != null ? rs.getString( "VLRLIQCOMPRA" ) : "" ) ) );
					imp.say( imp.pRow(), 109, "| " + Funcoes.dateToStrDate( rs.getDate( "DTEMITCOMPRA" ) ) );
					imp.say( imp.pRow(), 122, "| " + Funcoes.dateToStrDate( rs.getDate( "DTENTCOMPRA" ) ) );
					imp.say( imp.pRow(), 135, "|" );
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "| Item" );
					imp.say( imp.pRow(), 8, "| Cod.prod." );
					imp.say( imp.pRow(), 22, "| Descricao do produto" );
					imp.say( imp.pRow(), 75, "| Lote" );
					imp.say( imp.pRow(), 91, "| Qtd" );
					imp.say( imp.pRow(), 102, "| Vlr.Unitario " );
					imp.say( imp.pRow(), 117, "| Vlr.Item" );
					imp.say( imp.pRow(), 135, "|" );
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
				}
				imp.say( imp.pRow() + 1, 0, imp.comprimido() );
				imp.say( imp.pRow(), 0, "| " + ( rs.getString( "CODITCOMPRA" ) != null ? rs.getString( "CODITCOMPRA" ) : "" ) );
				imp.say( imp.pRow(), 8, "| " + ( rs.getString( "CODPROD" ) != null ? rs.getString( "CODPROD" ) : "" ) );
				imp.say( imp.pRow(), 22, "| " + ( rs.getString( "DESCPROD" ) != null ? rs.getString( "DESCPROD" ) : "" ) );
				imp.say( imp.pRow(), 75, "| " + ( rs.getString( "CODLOTE" ) != null ? rs.getString( "CODLOTE" ) : "" ) );
				imp.say( imp.pRow(), 91, "| " + Funcoes.strDecimalToStrCurrency( 8, Aplicativo.casasDec, ( rs.getString( "QTDITCOMPRA" ) != null ? rs.getString( "QTDITCOMPRA" ) : "" ) ) );
				/*
				 * if( getPrefere() ){ imp.say(imp.pRow(), 102, "| " + (rs.getString("PRECOITCOMPRA") != null ? rs.getString("PRECOITCOMPRA") : "")); }else{
				 */
				imp.say( imp.pRow(), 102, "| " + ( rs.getString( "PRECO" ) != null ? rs.getString( "PRECO" ) : "" ) );
				/*
				 * }
				 */
				imp.say( imp.pRow(), 117, "| " + Funcoes.strDecimalToStrCurrency( 10, Aplicativo.casasDecPre, ( rs.getString( "VLRLIQITCOMPRA" ) != null ? rs.getString( "VLRLIQITCOMPRA" ) : "" ) ) );
				imp.say( imp.pRow(), 135, "|" );

				bTotal = bTotal.add( rs.getBigDecimal( "VLRLIQITCOMPRA" ) );
				bQtdtot = bQtdtot.add( rs.getBigDecimal( "QTDITCOMPRA" ) );
			}

			imp.say( imp.pRow() + 1, 0, imp.comprimido() );
			imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "=", 133 ) + "+" );
			imp.say( imp.pRow() + 1, 0, imp.comprimido() );
			imp.say( imp.pRow(), 0, "| " );
			imp.say( imp.pRow(), 44, "TOTAL" );
			imp.say( imp.pRow(), 91, "| " + Funcoes.strDecimalToStrCurrency( 12, Aplicativo.casasDec, bQtdtot.toString() ) );
			imp.say( imp.pRow(), 117, "| " + Funcoes.strDecimalToStrCurrency( 12, Aplicativo.casasDecPre, bTotal.toString() ) );
			imp.say( imp.pRow(), 135, "|" );
			imp.say( imp.pRow() + 1, 0, imp.comprimido() );
			imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "=", 133 ) + "+" );

			imp.eject();
			imp.fechaGravacao();

			if ( bVisualizar==TYPE_PRINT.VIEW )
				imp.preview( this );

			else
				imp.print();

		} catch ( Exception e ) {

			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao montar o relatorio!\n" + e.getMessage(), true, con, e );
		}
	}

	private void imprimirGrafico( final TYPE_PRINT bVisualizar, final ResultSet rs, final String sCab ) {

		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "CPCOMPRA" ) );
		hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
		hParam.put( "FILTROS", sCab );

		dlGr = new FPrinterJob( "relatorios/CpTipoMov.jasper", "Relatorio de compras por tipo de movimento", sCab, rs, hParam, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório de resumo diario!" + err.getMessage(), true, con, err );
			}
		}
	}
}
