/**
 * @version 08/12/2000 <BR>
 * @author Setpoint Informática Ltda. <BR>
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;

import net.sf.jasperreports.engine.JasperPrintManager;

public class FRCpItem extends FRelatorio {

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

	public FRCpItem() {

		setTitulo( "Compras por ítem" );
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

			/*
			 * sSQL.append( "SELECT it.precoitcompra as preco, IT.CODPROD, PD.DESCPROD, SUM(IT.QTDITCOMPRA)," ); sSQL.append( "SUM(IT.VLRLIQITCOMPRA) " );
			 * 
			 * sSQL.append( "FROM CPCOMPRA C, CPITCOMPRA IT, CPFORNECED F, FNPLANOPAG PG, EQPRODUTO PD "); sSQL.append( "WHERE C.CODEMP=? AND C.CODFILIAL=? "); sSQL.append( "AND C.CODEMPFR=F.CODEMP AND C.CODFILIALFR=F.CODFILIAL AND C.CODFOR=F.CODFOR "); sSQL.append(
			 * "AND C.CODEMPPG=PG.CODEMP AND C.CODFILIALPG=PG.CODFILIAL AND C.CODPLANOPAG=PG.CODPLANOPAG "); sSQL.append( "AND C.CODEMP=IT.CODEMP AND C.CODFILIAL=IT.CODFILIAL AND C.CODCOMPRA=IT.CODCOMPRA "); sSQL.append(
			 * "AND IT.CODEMPPD=PD.CODEMP AND IT.CODFILIALPD=PD.CODFILIAL AND IT.CODPROD=PD.CODPROD "); sSQL.append( "AND C.DTENTCOMPRA BETWEEN ? AND ? "); sSQL.append( sWhere ); sSQL.append( "GROUP BY IT.CODPROD,PD.DESCPROD " ); sSQL.append( " ORDER BY IT.CODPROD" );
			 */

			sSQL.append( "SELECT IT.CODPROD, PD.DESCPROD, " );
			sSQL.append( "SUM(IT.QTDITCOMPRA) QTDITCOMPRA, " );
			sSQL.append( "SUM(IT.VLRLIQITCOMPRA) VLRLIQITCOMPRA " );
			sSQL.append( "FROM CPCOMPRA C, CPITCOMPRA IT, " );
			sSQL.append( "CPFORNECED F, FNPLANOPAG PG, EQPRODUTO PD WHERE C.CODEMP=? AND " );
			sSQL.append( "C.CODFILIAL=? AND C.CODEMPFR=F.CODEMP AND C.CODFILIALFR=F.CODFILIAL AND " );
			sSQL.append( "C.CODFOR=F.CODFOR AND C.CODEMPPG=PG.CODEMP AND C.CODFILIALPG=PG.CODFILIAL AND " );
			sSQL.append( "C.CODPLANOPAG=PG.CODPLANOPAG AND C.CODEMP=IT.CODEMP AND C.CODFILIAL=IT.CODFILIAL AND " );
			sSQL.append( "C.CODCOMPRA=IT.CODCOMPRA AND IT.CODEMPPD=PD.CODEMP AND IT.CODFILIALPD=PD.CODFILIAL AND " );
			sSQL.append( "IT.CODPROD=PD.CODPROD AND C.DTENTCOMPRA BETWEEN ? AND ? " );
			sSQL.append( sWhere );
			sSQL.append( "GROUP BY 1,2 " );

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
			ps.setDate( 3, Funcoes.strDateToSqlDate( txtDataini.getVlrString() ) );
			ps.setDate( 4, Funcoes.strDateToSqlDate( txtDatafim.getVlrString() ) );

			rs = ps.executeQuery();

			imprimirGrafico( bVisualizar, rs, sCab.toString() );

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

	private void imprimirGrafico( final TYPE_PRINT bVisualizar, final ResultSet rs, final String sCab ) {

		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "CPCOMPRA" ) );
		hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
		hParam.put( "FILTROS", sCab );

		dlGr = new FPrinterJob( "relatorios/CpItem.jasper", "Relatorio de compras por ítem", sCab, rs, hParam, this );

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
