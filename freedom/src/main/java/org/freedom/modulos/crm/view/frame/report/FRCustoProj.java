/**
 * @version 23/01/2009 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.tmk <BR>
 *         Classe: @(#)FRDiario.java <BR>
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
 *         Relatório diário de ligações.
 * 
 */

package org.freedom.modulos.crm.view.frame.report;

import java.awt.Component;
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
import java.util.Date;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

public class FRCustoProj extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodContr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldFK txtDescContr = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtNomeCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtDDDCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldFK txtFoneCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 12, 0 );

	private JTextFieldFK txtEmailCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtContatoCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtEndCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtCidCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldFK txtUfCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtNumCli = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 10, 0 );

	private ListaCampos lcCli = new ListaCampos( this );

	private ListaCampos lcContr = new ListaCampos( this );

	private boolean bComp = false;

	private Component tela = null;

	public FRCustoProj() {

		setTitulo( "Custos de Projeto" );
		setAtribos( 80, 80, 350, 240 );

		montaListaCampos();
		montaTela();
		tela = this;

	}

	public void setParametros( Integer codcli, Date dtini, Date dtfim ) {

		txtCodCli.setVlrInteger( codcli );
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
		
		adic( new JLabelPad( "De:" ), 15, 25, 25, 20 );
		adic( txtDataini, 42, 25, 95, 20 );
		adic( new JLabelPad( "Até:" ), 148, 25, 25, 20 );
		adic( txtDatafim, 178, 25, 95, 20 );
		adic( new JLabelPad( "Cód.Cli" ), 7, 60, 80, 20 );
		adic( txtCodCli, 7, 80, 80, 20 );
		adic( new JLabelPad( "Razão social do cliente" ), 90, 60, 250, 20 );
		adic( txtRazCli, 90, 80, 225, 20 );

		adic( new JLabelPad( "Cód.Contr." ), 7, 110, 80, 20 );
		adic( txtCodContr, 7, 130, 80, 20 );
		adic( new JLabelPad( "Descrição do contrato" ), 90, 110, 250, 20 );
		adic( txtDescContr, 90, 130, 225, 20 );

		Calendar cPeriodo = Calendar.getInstance();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataini.setVlrDate( cPeriodo.getTime() );

	}

	private void montaListaCampos() {

		// Cliente
		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCli.montaSql( false, "CLIENTE", "VD" );
		lcCli.setReadOnly( true );
		txtCodCli.setTabelaExterna( lcCli, null );
		txtCodCli.setFK( true );
		txtCodCli.setNomeCampo( "CodCli" );

		// Contrato

		lcContr.add( new GuardaCampo( txtCodContr, "CodContr", "Cód.Contr.", ListaCampos.DB_PK, false ) );
		lcContr.add( new GuardaCampo( txtDescContr, "DescContr", "Descrição do contrato", ListaCampos.DB_SI, false ) );
		lcContr.montaSql( false, "CONTRATO", "VD" );
		lcContr.setReadOnly( true );
		lcContr.setDinWhereAdic( "CODCLI=#N ", txtCodCli );
		txtCodContr.setTabelaExterna( lcContr, null );
		txtCodContr.setFK( true );
		txtCodContr.setNomeCampo( "CodContr" );

	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		int iparam = 1;

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( tela, "Data final maior que a data inicial!" );
			return;
		}

		sql.append( "SELECT CL.CODCLI, CL.RAZCLI, CP.CODCONTR, CP.CODITCONTR, CT.DESCCONTR, IC.DESCITCONTR ,DESCCUSTO, QTDCUSTO,VLRCUSTO,TIPOCUSTO, QTDCUSTO * VLRCUSTO AS TOTCUSTO, DATA " );
		sql.append( "FROM VWCUSTOPROJ01 CP, VDCONTRATO CT, VDITCONTRATO IC, VDCLIENTE CL " );
		sql.append( "WHERE CT.CODEMP=CP.CODEMP AND CT.CODFILIAL=CP.CODFILIAL AND CT.CODCONTR=CP.CODCONTR " );
		sql.append( "AND IC.CODEMP=CT.CODEMP AND IC.CODFILIAL=CT.CODFILIAL AND IC.CODCONTR=CT.CODCONTR " );
		sql.append( "AND CL.CODEMP=CT.CODEMPCL AND CL.CODFILIAL=CT.CODFILIALCL AND CL.CODCLI=CT.CODCLI " );
		sql.append( "AND IC.CODITCONTR=CP.CODITCONTR AND CP.CODEMP=? AND CP.CODFILIAL=? " );

		if ( txtCodContr.getVlrInteger().intValue() > 0 ) {
			sql.append( "AND CP.CODCONTR=? " );
		}
		if ( txtCodCli.getVlrInteger().intValue() > 0 ) {
			sql.append( "AND CP.CODCLI=? " );
		}
		if ( txtDataini.getVlrDate() != null && txtDatafim.getVlrDate() != null ) {
			sql.append( "AND CP.DATA BETWEEN ? AND ? " );
		}

		sql.append( "ORDER BY CP.CODCONTR, CP.CODITCONTR, TIPOCUSTO, DATA" );

		try {

			ps = con.prepareStatement( sql.toString() );
			ps.setInt( iparam++, Aplicativo.iCodEmp );
			ps.setInt( iparam++, ListaCampos.getMasterFilial( "VDCONTRATO" ) );

			if ( txtCodContr.getVlrInteger().intValue() > 0 ) {
				ps.setInt( iparam++, txtCodContr.getVlrInteger() );
			}
			if ( txtCodCli.getVlrInteger().intValue() > 0 ) {
				ps.setInt( iparam++, txtCodCli.getVlrInteger() );
			}

			if ( txtDataini.getVlrDate() != null && txtDatafim.getVlrDate() != null ) {
				ps.setDate( iparam++, Funcoes.strDateToSqlDate( txtDataini.getVlrString() ) );
				ps.setDate( iparam++, Funcoes.strDateToSqlDate( txtDatafim.getVlrString() ) );
			}

			rs = ps.executeQuery();

		} catch ( SQLException err ) {

			err.printStackTrace();
			Funcoes.mensagemErro( this, " Erro na consulta da tabela de atendimentos" );
		}

		imprimiGrafico( rs, bVisualizar );

	}

	private void imprimiGrafico( final ResultSet rs, final TYPE_PRINT bVisualizar ) {

		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "CPCOMPRA" ) );
		hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
		hParam.put( "SUBREPORT_DIR", "org/freedom/relatorios/" );

		dlGr = new FPrinterJob( "layout/rel/REL_FIN_PROJ_01.jasper", "Relatório de custos de projeto/contrato", "", rs, hParam, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( tela, "Erro na impressão de relatório custos de projetos/contratos!" + err.getMessage(), true, con, err );
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcCli.setConexao( cn );
		lcContr.setConexao( cn );
	}

}
