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
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.crm.view.frame.crud.detail.FContrato;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCliente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

public class FRFaturamento extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodContr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtCodItContr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldFK txtDescContr = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldFK txtDescItContr = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

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

	private ListaCampos lcContr = new ListaCampos( this, "" );
	
	private ListaCampos lcItContr = new ListaCampos( this );

	private boolean bComp = false;

	private Component tela = null;

	private ListaCampos lcAtend = new ListaCampos( this, "AE" );

	public FRFaturamento() {
		super(false);

		setTitulo( "Relatório de previsão de faturamento" );
		setAtribos( 80, 80, 350	, 280 );

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
		adic( lbLinha, 5, 10, 310, 45 );
		
		adic( txtDataini, 38, 25, 95, 20, "De:" );		
		adic( txtDatafim, 178, 25, 95, 20, "Até:" );		

		adic( txtCodCli, 7, 75, 80, 20, "Cód.Cli" );		
		adic( txtRazCli, 90, 75, 225, 20, "Razão social do cliente" );

		adic( txtCodContr, 7, 115, 80, 20, "Cód.Contr." );
		adic( txtDescContr, 90, 115, 225, 20, "Descrição do contrato" );
		
		adic( txtCodItContr, 7, 155, 80, 20, "Cód.It.Contr." );
		adic( txtDescItContr, 90, 155, 225, 20, "Descrição do ítem de contrato" );

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
		txtCodCli.setTabelaExterna( lcCli, FCliente.class.getCanonicalName() );
		txtCodCli.setFK( true );
		txtCodCli.setNomeCampo( "CodCli" );

		// Contrato

		lcContr.add( new GuardaCampo( txtCodContr, "CodContr", "Cód.Contr.", ListaCampos.DB_PK, false ) );
		lcContr.add( new GuardaCampo( txtDescContr, "DescContr", "Descrição do contrato", ListaCampos.DB_SI, false ) );
		lcContr.montaSql( false, "CONTRATO", "VD" );
		lcContr.setReadOnly( true );
		lcContr.setDinWhereAdic( "CODCLI=#N ", txtCodCli );
		txtCodContr.setTabelaExterna( lcContr, FContrato.class.getCanonicalName() );
		txtCodContr.setFK( true );
		txtCodContr.setNomeCampo( "CodContr" );
		
		
		// Item de Contrato

		lcItContr.add( new GuardaCampo( txtCodItContr, "CodItContr", "Cód.It.Contr.", ListaCampos.DB_PK, false ) );
		lcItContr.add( new GuardaCampo( txtCodContr, "CodContr", "Cód.Contr.", ListaCampos.DB_PF, false ) );
		lcItContr.add( new GuardaCampo( txtDescItContr, "DescItContr", "Descrição do ítem de contrato", ListaCampos.DB_SI, false ) );
		lcItContr.montaSql( false, "ITCONTRATO", "VD" );
		lcItContr.setReadOnly( true );
		lcItContr.setDinWhereAdic( "CODCONTR=#N ", txtCodContr );
		txtCodItContr.setTabelaExterna( lcItContr, FContrato.class.getCanonicalName() );
		txtCodItContr.setFK( true );
		txtCodItContr.setNomeCampo( "CodItContr" );


	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		StringBuilder sql = new StringBuilder();
		StringBuilder sqlcontrato = new StringBuilder();
		StringBuilder filtros = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		int iparam = 1;
		int acumuloitcontr = 0;
		Date dtinicontr = new Date();
		try {

			if (txtCodItContr.getVlrInteger().intValue()!=0) {
				sqlcontrato.append("select i.acumuloitcontr, c.dtinicio from vditcontrato i, vdcontrato c ");
				sqlcontrato.append("where i.codemp=? and i.codfilial=? and i.codcontr=? and i.coditcontr=? and ");
				sqlcontrato.append("c.codemp=i.codemp and c.codfilial=i.codfilial and c.codcontr=i.codcontr ");
				ps = con.prepareStatement( sqlcontrato.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "VDITCONTRATO" ) );
				ps.setInt( 3, txtCodContr.getVlrInteger().intValue() );
				ps.setInt( 4, txtCodItContr.getVlrInteger().intValue() );
				rs = ps.executeQuery();
				if (rs.next()) {
					acumuloitcontr = rs.getInt( "acumuloitcontr" );
					dtinicontr = rs.getDate( "dtinicio" );
				}
				rs.close();
				ps.close();
				con.commit();
			}
			
			if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
				Funcoes.mensagemInforma( tela, "Data final maior que a data inicial!" );
				return;
			}
			Date dtiniac = txtDataini.getVlrDate();
			if (acumuloitcontr!=0) {
				dtiniac = Funcoes.somaMes( dtiniac, -1*acumuloitcontr ); 
				// Verificar se o contrato é novo para não calcular acumulo.
				if (dtinicontr.compareTo( dtiniac )>0) {
					dtiniac = dtinicontr;
					acumuloitcontr = Funcoes.contaMeses( dtiniac, txtDataini.getVlrDate() );
				}
			}

		
			sql.append( "select a.codempcl, a.codfilialcl, a.codcli, a.razcli, a.codempct , a.codfilialcl, a.codcontr, a.desccontr,a.vlrhora , a.vlrhoraexced, a.qtditcontr,");
			sql.append( "coalesce(a.vlrcobtot,0.00) vlrcobtot, coalesce(a.vlrcob, 0.00) vlrcob, coalesce(a.vlrcobexced, 0.00) vlrcobexced, a.qtdhoras, a.saldomes, a.mes, a.ano, coalesce(a.excedentemescob, 0.00) excedentemescob, coalesce(a.excedentemes,0.00) excedentemes "); 
			sql.append( "from atresumoatendosp02(?,?,?,?,?,?,?,?,?) a order by  a.razcli, a.codcli, a.desccontr, a.codcontr, a.coditcontr, a.mes desc" );
			System.out.println( "SQL:" + sql.toString() );

			ps = con.prepareStatement( sql.toString() );
			
			ps.setInt( iparam++, lcCli.getCodEmp() );
			ps.setInt( iparam++, lcCli.getCodFilial() );
			
			if ( txtCodCli.getVlrInteger().intValue() > 0 ) {
				ps.setInt( iparam++, txtCodCli.getVlrInteger() );
				filtros.append( "Cliente" );
			} else {
				ps.setInt( iparam++, 0 );
			}
			
			ps.setInt( iparam++, lcContr.getCodEmp() );
			ps.setInt( iparam++, lcContr.getCodFilial() );
			if ( txtCodContr.getVlrInteger().intValue() > 0 ) {
				ps.setInt( iparam++, txtCodContr.getVlrInteger() );
				filtros.append( "Contrato " );
			} else {
				ps.setInt( iparam++, 0 );
			}
			if ( txtCodItContr.getVlrInteger().intValue() > 0 ) {
				ps.setInt( iparam++, txtCodItContr.getVlrInteger() );
				filtros.append( "Item do Contrato " );
			} else {
				ps.setInt( iparam++, 0 );
			}
		
			//ps.setDate( iparam++, Funcoes.strDateToSqlDate( txtDataini.getVlrString() ) );
			ps.setDate( iparam++,  Funcoes.strDateToSqlDate( txtDataini.getVlrString() ) );
			ps.setDate( iparam++, Funcoes.strDateToSqlDate( txtDatafim.getVlrString() ) );
			
			rs = ps.executeQuery();

		} catch ( SQLException err ) {

			err.printStackTrace();
			Funcoes.mensagemErro( this, " Erro na consulta da tabela de atendimentos" );
		}

		imprimiGrafico( rs, bVisualizar, filtros.toString() );

	}

	private void imprimiGrafico( final ResultSet rs, final TYPE_PRINT bVisualizar, String filtros) {

		FPrinterJob dlGr = null;
		int mescob = 0;
		HashMap<String, Object> hParam = new HashMap<String, Object>();
		Date dtinip = txtDataini.getVlrDate();
		Date dtfimp = txtDataini.getVlrDate();
			
		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "CPCOMPRA" ) );
		hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
		hParam.put( "CODCLI", txtCodCli.getVlrInteger() );
		hParam.put( "FILTROS", filtros);
		hParam.put( "DTFIM", txtDatafim.getVlrDate() );
		hParam.put( "DTINIP", dtinip );
		hParam.put( "CODCONTR", txtCodContr.getVlrInteger() );
		hParam.put( "CODITCONTR", txtCodItContr.getVlrInteger() );
		hParam.put( "CONEXAO", con.getConnection() );
		hParam.put( "CLIENTE", txtCodCli.getVlrString().trim() + "-" + txtRazCli.getVlrString().trim() );
		
		dlGr = new FPrinterJob( "relatorios/prev_faturamento.jasper", "RELATÓRIO DE PREVISÂO DE FATURAMENTO", "", rs, hParam, this );
	
		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( tela, "Erro na impressão de relatório Compras Geral!" + err.getMessage(), true, con, err );
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcCli.setConexao( cn );
		lcContr.setConexao( cn );
		lcItContr.setConexao( cn );
		lcAtend.setConexao( cn );
	}

}
