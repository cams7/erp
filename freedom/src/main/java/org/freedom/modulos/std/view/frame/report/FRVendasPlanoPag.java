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

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

public class FRVendasPlanoPag extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JCheckBoxPad cbDesc = new JCheckBoxPad( "Ordenar decrescente ?", "DESC", "" );

	private JRadioGroup<String, String> rgOrdem = null;

	private JRadioGroup<String, String> rgFaturados = null;

	private JRadioGroup<String, String> rgFinanceiro = null;
	
	private JRadioGroup<?, ?> rgEmitidos = null;
	
	private Vector<String> vLabsEmit = new Vector<String>();

	private Vector<String> vValsEmit = new Vector<String>();


	public FRVendasPlanoPag() {

		super( false );
		setTitulo( "Vendas por plano de pagamento" );
		setAtribos( 80, 80, 330, 380 );

		montaRadioGrups();
		montaTela();

		GregorianCalendar cPeriodo = new GregorianCalendar();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataini.setVlrDate( cPeriodo.getTime() );

		cbDesc.setVlrString( "DESC" );
	}

	private void montaRadioGrups() {

		Vector<String> vLabs = new Vector<String>();
		Vector<String> vVals = new Vector<String>();

		vLabs.addElement( "Plano de pagamento" );
		vLabs.addElement( "Valor" );
		vVals.addElement( "P.CODPLANOPAG" );
		vVals.addElement( "3" );

		rgOrdem = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgOrdem.setVlrString( "3" );

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

		
		
	}

	private void montaTela() {

		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );
		JLabelPad lbPeriodo = new JLabelPad( "Periodo:", SwingConstants.CENTER );
		lbPeriodo.setOpaque( true );

		adic( lbPeriodo, 17, 10, 80, 20 );
		adic( lbLinha, 7, 20, 300, 45 );

		adic( txtDataini, 17, 35, 125, 20 );
		adic( new JLabelPad( "à", SwingConstants.CENTER ), 142, 35, 30, 20 );
		adic( txtDatafim, 172, 35, 125, 20 );
		adic( new JLabelPad( "Ordem" ), 7, 70, 50, 20 );
		adic( rgOrdem, 7, 90, 300, 30 );
		adic( cbDesc, 7, 125, 300, 20 );
		adic( rgFaturados, 7, 160, 145, 70 );
		adic( rgFinanceiro, 160, 160, 145, 70 );
		adic( rgEmitidos, 7, 240, 145, 70 );
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
		String sWhere1 = null;
		String sWhere2 = null;
		String sWhere3 = "";

		try {

			if ( rgFaturados.getVlrString().equals( "S" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV='S' ";
				sCab.append( "FATURADO" );
			}
			else if ( rgFaturados.getVlrString().equals( "N" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV='N' ";
				if ( sCab.length() > 0 ) {
					sCab.append( " - " );
				}
				sCab.append( "NAO FATURADO" );
			}
			else if ( rgFaturados.getVlrString().equals( "A" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV IN ('S','N') ";
			}

			if ( rgFinanceiro.getVlrString().equals( "S" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV='S' ";
				if ( sCab.length() > 0 ) {
					sCab.append( " - " );
				}
				sCab.append( "FINANCEIRO" );
			}
			else if ( rgFinanceiro.getVlrString().equals( "N" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV='N' ";
				if ( sCab.length() > 0 ) {
					sCab.append( " - " );
				}
				sCab.append( "NAO FINANCEIRO" );
			}
			else if ( rgFinanceiro.getVlrString().equals( "A" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV IN ('S','N') ";
			}
			
			if ( rgEmitidos.getVlrString().equals( "S" ) ) {
				sWhere3 = " AND V.STATUSVENDA IN ('V2','V3','P3') " ;
				sCab.append(" EMITIDOS " );
			}
			else if ( rgEmitidos.getVlrString().equals( "N" ) ) {
				sWhere3 = " AND V.STATUSVENDA NOT IN ('V2','V3','P3') ";
				sCab.append( "NAO EMITIDOS" );
			}


			sSQL.append( "SELECT P.CODPLANOPAG, P.DESCPLANOPAG, SUM( V.VLRLIQVENDA ) AS VALORVD, " );
			sSQL.append( "SUM( V.vlrprodvenda ) AS valorbruto, SUM( V.vlrdescvenda ) AS vlrdescvenda, SUM( V.vlrdescitvenda ) AS vlrdescitvenda ");
			sSQL.append( "FROM FNPLANOPAG P, VDVENDA V, EQTIPOMOV TM " );
			sSQL.append( "WHERE V.CODEMP=? AND V.CODFILIAL=? AND P.CODPLANOPAG=V.CODPLANOPAG AND " );
			sSQL.append( "V.CODEMPPG=P.CODEMP AND V.CODFILIALPG=P.CODFILIAL AND " );
			sSQL.append( "V.CODEMPTM=TM.CODEMP AND V.CODFILIALTM=TM.CODFILIAL AND V.CODTIPOMOV=TM.CODTIPOMOV AND " );
			sSQL.append( "NOT SUBSTR(V.STATUSVENDA,1,1)='C' AND " );
			sSQL.append( "V.DTEMITVENDA BETWEEN ? AND ? " );
			sSQL.append( sWhere1 );
			sSQL.append( sWhere2 );
			sSQL.append( sWhere3 );
			sSQL.append( "GROUP BY P.CODPLANOPAG, P.DESCPLANOPAG " );
			sSQL.append( "ORDER BY " );
			sSQL.append( rgOrdem.getVlrString() + " " + cbDesc.getVlrString() );

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNPLANOPAG" ) );
			ps.setDate( 3, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( 4, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			rs = ps.executeQuery();

			imprimirGrafico( bVisualizar, rs, sCab.toString() );

			rs.close();
			ps.close();

			con.commit();

		} catch ( Exception err ) {
			Funcoes.mensagemErro( this, "Erro ao montar relatório de plano de pagamento!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	public void imprimirGrafico( final TYPE_PRINT bVisualizar, final ResultSet rs, final String sCab ) {

		FPrinterJob dlGr = new FPrinterJob( "relatorios/VendasPlanoPag.jasper", "Vendas por plano de pagamento", sCab, rs, null, this );

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
	}
}
