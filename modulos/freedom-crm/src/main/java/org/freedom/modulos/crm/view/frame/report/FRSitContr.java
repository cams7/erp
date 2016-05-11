/**
 * @version 28/09/2011 <BR>
 * @author Setpoint Informática Ltda./ Bruno Nascimento<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.tmk <BR>
 *         Classe: @(#)FRSitContr.java <BR>
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
 *         Relatório Situação Projetos/Contratos.
 * 
 */

package org.freedom.modulos.crm.view.frame.report;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.ImageIcon;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCliente;

public class FRSitContr extends FRelatorio {

	private static final long serialVersionUID = 1L;
	
	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtStatus = new JTextFieldPad( JTextFieldPad.TP_STRING, 100, 0 );
	
	private Vector<String> vLabsSaldo = new Vector<String>();
	private Vector<String> vValsSaldo = new Vector<String>();
	
	private JRadioGroup<String, String> rgSaldoHoras = null;

	
	private ListaCampos lcCli = new ListaCampos( this );
	
	public FRSitContr() {		
		setTitulo( "Relatório de situação Projetos/Contratos" );
		setAtribos( 80, 80, 410	, 300 );
		
		montaListaCampos();
		montaTela();
		
	}
	
	private void montaTela(){
		
		vLabsSaldo.addElement( "Positivo" );
		vLabsSaldo.addElement( "Negativo" );
		vLabsSaldo.addElement( "Ambos" );
		vValsSaldo.addElement( "P" );
		vValsSaldo.addElement( "N" );
		vValsSaldo.addElement( "A" );
		
		rgSaldoHoras = new JRadioGroup<String, String>( 1, 3, vLabsSaldo, vValsSaldo );
		rgSaldoHoras.setVlrString( "A" );
		
		
		adic( txtCodCli, 7, 20, 80, 20, "Cod.Cli" );
		adic( txtRazCli, 90, 20, 225, 20, "Razão social do cliente" );
		adic( rgSaldoHoras, 7, 60, 360, 30, "Saldo em Horas" );
		
		
	}
	
	private void montaListaCampos() {
		
		//cliente
		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCli.montaSql( false, "CLIENTE", "VD" );
		lcCli.setReadOnly( true );
		txtCodCli.setTabelaExterna( lcCli, FCliente.class.getCanonicalName() );
		txtCodCli.setFK( true );
		txtCodCli.setNomeCampo( "CodCli" );
		
	}
	
	public void imprimir( TYPE_PRINT bVisualizar ) {

		Blob fotoemp = null;
		
		try {
			PreparedStatement ps = con.prepareStatement( "SELECT FOTOEMP FROM SGEMPRESA WHERE CODEMP=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				fotoemp = rs.getBlob( "FOTOEMP" );
			}
			rs.close();
			ps.close();
			con.commit();

		} catch (Exception e) {
			Funcoes.mensagemErro( this, "Erro carregando logotipo.\n" + e.getMessage() );
			e.printStackTrace();
		}	

	
		String sCab = "";
		String Ordem = "";
		//StringBuffer sWhere = new StringBuffer();
		StringBuffer sql = new StringBuffer();
		
		sql.append( "select a.codempcl, a.codfilialcl, a.codcli, a.razcli, ");
		sql.append( "a.codempct, a.codfilialct, a.codcontr, a.desccontr, " );
		sql.append( "a.descsitcontr, a.sitcontr, a.tpcobcontr, ");
		sql.append( "a.qtdcontr, a.tothoras ");
		sql.append( "from atatendimentovw07 a ");
		sql.append( "where a.codempct=? and a.codfilialct=? and ");
		sql.append( "(a.tpcobcontr='ES' or a.tpcontr='S')" );
		sql.append( " order by a.codcli,  a.desccontr ");

		PreparedStatement ps = null;
		ResultSet rs = null;

		try{
			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDCONTRATO" ) );

			rs = ps.executeQuery();

		} catch (Exception err) {
			Funcoes.mensagemErro( this, "Erro consulta Situação de Projetos/Contratos!\n" + err.getMessage(), true, con, err );
		}
		
		imprimiGrafico( bVisualizar, rs,  sCab, fotoemp );

	}

	private void imprimiGrafico( TYPE_PRINT bVisualizar, ResultSet rs, String sCab, Blob fotoemp) {
		String report = "layout/rel/REL_SIT_PROJ_CONTR.jasper";
		String label = "Situação Projetos/Contratos";
		
	    HashMap<String, Object> hParam = new HashMap<String, Object>();

	    try {
			hParam.put( "LOGOEMP",  new ImageIcon(fotoemp.getBytes(1, ( int ) fotoemp.length())).getImage() );
		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro carregando logotipo !\n" + e.getMessage()  );
			e.printStackTrace();
		}
	
		FPrinterJob dlGr = new FPrinterJob( report, label, sCab, rs, hParam , this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		} else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório de Situação de Projeto/Contrato!" + err.getMessage(), true, con, err );
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcCli.setConexao( cn );
	}

}
