/**
 * @version 01/07/2011 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.crm.view.frame.report <BR>
 *         Classe: @(#)FRSobreHorario.java <BR>
 * 
 *         Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *         modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *         na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *         Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *         sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *         Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *         Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *         de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *         Relatório de banco de horas por atendente
 * 
 */

package org.freedom.modulos.gpe.view.frame.report;

import org.freedom.library.type.TYPE_PRINT;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
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
import org.freedom.modulos.atd.view.frame.crud.tabbed.FAtendente;

public class FRBatidas extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );
	
	private ListaCampos lcEmpr = new ListaCampos( this, "AE" );
	
	private JTextFieldPad txtMatempr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeempr = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	
	public FRBatidas() {

		setTitulo( "Sobreposição de horários" );
		
		setAtribos( 80, 80, 450	, 250 );

		montaListaCampos();
		montaTela();

	}

	public void setParametros( Integer codcli, Date dtini, Date dtfim ) {

		txtDataini.setVlrDate( dtini );
		txtDatafim.setVlrDate( dtfim );
		
	}

	private void montaTela() {

		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );
		JLabelPad lbPeriodo = new JLabelPad( "Período:", SwingConstants.CENTER );
		lbPeriodo.setOpaque( true );

		adic( lbPeriodo, 7, 1, 80, 20 );
		adic( lbLinha, 5, 10, 300, 45 );
		
		adic( txtDataini, 38, 25, 95, 20 );		
		adic( txtDatafim, 178, 25, 95, 20 );	
		
		adic (txtMatempr, 7, 85, 80, 20, "Matrícula");
		adic (txtNomeempr, 90, 85, 215, 20, "Nome do empregado");
		
		Calendar cPeriodo = Calendar.getInstance();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
	
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataini.setVlrDate( cPeriodo.getTime() );

	}

	private void montaListaCampos() {
		
		txtMatempr.setTabelaExterna( lcEmpr, FAtendente.class.getCanonicalName() );
		txtMatempr.setFK( true );
		txtMatempr.setNomeCampo( "Matempr" );
		lcEmpr.add( new GuardaCampo( txtMatempr, "MatEmpr", "Matrícula", ListaCampos.DB_PK, false ) );
		lcEmpr.add( new GuardaCampo( txtNomeempr, "NomeEmpr", "Nome", ListaCampos.DB_SI, false ) );
		lcEmpr.montaSql( false, "EMPREGADO", "RH" );
		lcEmpr.setReadOnly( true );

	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		int iparam = 1;

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}
		
		sql.append( "SELECT B.DTBAT, B.HBAT, B.MATEMPR, E.NOMEEMPR, " );
		sql.append( "(CASE WHEN B.TIPOBAT='M' THEN 'MANUAL' ELSE 'ELETRÔNICA' END) TIPOBAT ");
		sql.append( "FROM PEBATIDA B, RHEMPREGADO E ");
		sql.append( "WHERE B.CODEMP=? AND B.CODFILIAL=? AND B.DTBAT BETWEEN ? AND ? AND ");
		sql.append( "E.CODEMP=B.CODEMPEP AND E.CODFILIAL=B.CODFILIALEP AND E.MATEMPR=B.MATEMPR ");
		if(txtMatempr.getVlrInteger()>0) {
			sql.append( "AND B.CODEMPEP=? AND B.CODFILIALEP=? AND B.MATEMPR=? " );
		}
		sql.append( "ORDER BY B.DTBAT, E.NOMEEMPR, B.HBAT ");
		
		System.out.println( "SQL:" + sql.toString() );

		try {

			ps = con.prepareStatement( sql.toString() );
			ps.setInt( iparam++, Aplicativo.iCodEmp );
			ps.setInt( iparam++, ListaCampos.getMasterFilial( "PEBATIDA" ) );
			ps.setDate( iparam++, Funcoes.strDateToSqlDate( txtDataini.getVlrString() ) );
			ps.setDate( iparam++, Funcoes.strDateToSqlDate( txtDatafim.getVlrString() ) );
			
			if(txtMatempr.getVlrInteger()>0) {
				
				ps.setInt( iparam++, Aplicativo.iCodEmp );
				ps.setInt( iparam++, ListaCampos.getMasterFilial( "RHEMPREGADO" ) );
				ps.setInt( iparam++, txtMatempr.getVlrInteger() );
				
			}
			
			rs = ps.executeQuery();
		
		} 
		catch ( SQLException err ) {

			err.printStackTrace();
			Funcoes.mensagemErro( this, " Erro na consulta de atendimentos!" );
			
		}

		imprimiGrafico( rs, bVisualizar );

	}

	private void imprimiGrafico( final ResultSet rs, final TYPE_PRINT bVisualizar ) {

		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "ATATENDIMENTO" ) );
		hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
		hParam.put( "SUBREPORT_DIR", "org/freedom/relatorios/" );
		hParam.put( "DTINI", txtDataini.getVlrDate() );
		hParam.put( "DTFIM", txtDatafim.getVlrDate() );
		hParam.put( "CONEXAO", con.getConnection() );

		dlGr = new FPrinterJob( "layout/rel/REL_GPE_BATIDAS.jasper", "Relatório de Batidas/Ponto", "", rs, hParam, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de Relatório de Batidas/Ponto !" + err.getMessage(), true, con, err );
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		
		lcEmpr.setConexao( cn );
		
	}

}
