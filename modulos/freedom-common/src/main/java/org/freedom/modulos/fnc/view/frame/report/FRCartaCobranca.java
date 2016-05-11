/**
 * @version 16/04/2010 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FRReceber.java <BR>
 * 
 *                    Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                    modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                    na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                    Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                    sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                    Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                    Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                    de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                    Tela de filtros par ao relatório de carta de cobrança.
 * 
 */

package org.freedom.modulos.fnc.view.frame.report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.acao.RadioGroupEvent;
import org.freedom.acao.RadioGroupListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.object.EmailBean;
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
import org.freedom.modulos.fnc.business.component.Juros;

public class FRCartaCobranca extends FRelatorio implements RadioGroupListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCnpjCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtDiasAtrazo = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 3, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldFK txtEmailCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private ListaCampos lcCli = new ListaCampos( this );

	private ListaCampos lcTipoCli = new ListaCampos( this );

	public FRCartaCobranca() {

		super( false );

		setTitulo( "Carta de cobrança" );
		setAtribos( 40, 50, 420, 130 );

		txtDiasAtrazo.setVlrInteger( new Integer( 15 ) );

		montaListaCampos();
		montaRadioGroups();
		montaTela();

	}

	private void montaListaCampos() {

		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtEmailCli, "EmailCli", "Email", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtCnpjCli, "CnpjCli", "CNPJ", ListaCampos.DB_SI, false ) );

		lcCli.montaSql( false, "CLIENTE", "VD" );
		lcCli.setReadOnly( true );
		txtCodCli.setTabelaExterna( lcCli, null );
		txtCodCli.setFK( true );
		txtCodCli.setNomeCampo( "CodCli" );

	}

	private void montaRadioGroups() {

	}

	private void montaTela() {

		adic( new JLabelPad( "Cód.cli." ), 7, 0, 80, 20 );
		adic( txtCodCli, 7, 20, 80, 20 );
		adic( new JLabelPad( "Razão social do cliente" ), 90, 0, 210, 20 );
		adic( txtRazCli, 90, 20, 210, 20 );

		adic( new JLabelPad( "Dias de atrazo" ), 303, 0, 90, 20 );
		adic( txtDiasAtrazo, 303, 20, 90, 20 );

	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();

		try {

			sql.append( "select " );
			sql.append( "cl.codcli, cl.razcli, " );
			sql.append( "rc.datarec emissao, ir.dtvencitrec vencimento, ir.dtpagoitrec recebimento, " );
			sql.append( "rc.docrec doc, ir.vlrparcitrec, ir.vlrpagoitrec recebido, ir.vlrapagitrec aberto " );

			sql.append( "from fnitreceber ir, fnreceber rc, vdcliente cl " );

			sql.append( "where " );
			sql.append( "ir.codemp=rc.codemp and ir.codfilial=rc.codfilial and ir.codrec=rc.codrec " );
			sql.append( "and cl.codemp=rc.codempcl and cl.codfilial=rc.codfilialcl and cl.codcli=rc.codcli " );
			sql.append( "and ir.statusitrec not in('RP','CR') " );
			sql.append( "and ir.dtvencitrec <= cast('today' as date) " );
			sql.append( "and rc.codemp=? and rc.codfilial=? " );

			if ( txtCodCli.getVlrInteger() > 0 ) {
				sql.append( " and rc.codempcl=? and rc.codfilialcl=? and rc.codcli=? " );
			}

			if ( txtDiasAtrazo.getVlrInteger() > 0 ) {
				sql.append( " and ( ( ( cast('today' as date) ) - ir.dtvencitrec ) ) >= " + txtDiasAtrazo.getVlrInteger() );
			}

			sql.append( " order by 1, 3 desc" );

			int param = 1;

			System.out.println( "Query:" + sql.toString() );

			ps = con.prepareStatement( sql.toString() );

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "FNRECEBER" ) );

			if ( txtCodCli.getVlrInteger() > 0 ) {
				ps.setInt( param++, lcCli.getCodEmp() );
				ps.setInt( param++, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
				ps.setInt( param++, txtCodCli.getVlrInteger() );

			}

			rs = ps.executeQuery();

			imprimirGrafico( bVisualizar, rs, "" );

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela de contas a receber!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	private void imprimirGrafico( final TYPE_PRINT bVisualizar, final ResultSet rs, final String sCab ) {

		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "FNPAGAR" ) );
		hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
		hParam.put( "FILTROS", sCab );
		hParam.put( "ENDEMP", Aplicativo.empresa.getEnderecoCompleto() );
		hParam.put( "CIDFILIAL", Aplicativo.empresa.getCidFilial() );

		Juros calcjuros = new Juros(); 

		hParam.put( "CALCJUROS", calcjuros ); 

		EmailBean mail = Aplicativo.getEmailBean();
		mail.setPara( txtEmailCli.getVlrString() );

		dlGr = new FPrinterJob( "layout/rel/REL_CARTA_COB_01.jasper", "Carta de cobrança", sCab, rs, hParam, this, mail );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão do relatório de carta de cobrança!" + err.getMessage(), true, con, err );				
			}
		}
		
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcCli.setConexao( cn );
		lcTipoCli.setConexao( cn );

	}

	public void valorAlterado( RadioGroupEvent evt ) {

	}
}
