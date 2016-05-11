/**
 * @version 11/03/2009 <BR>
 * @author Setpoint Informática Ltda./Vinícius Cintra Domingos <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FROrcamento.java <BR>
 * 
 *                      Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                      modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                      na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                      Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                      sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                      Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                      Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                      de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                      Tela de dialogo para impressão de relatório de orçamentos.
 * 
 */

package org.freedom.modulos.std.view.frame.report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.acao.CheckBoxEvent;
import org.freedom.acao.CheckBoxListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

public class FROrcamentoCliente extends FRelatorio implements CheckBoxListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );
	
	private JRadioGroup<String, String> rgFiltrarPor = null;

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCnpjCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );
	
	private JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtDescVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );
	
	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private ListaCampos lcCli = new ListaCampos( this );
	
	private ListaCampos lcVend = new ListaCampos( this );
	
	private ListaCampos lcProduto = new ListaCampos( this );
	
	private JComboBoxPad cbSaldo = null;

	public FROrcamentoCliente() {

		super( false );
		setTitulo( "Relatório de Orçamentos - Resumo por Clientes" );
		setAtribos( 130, 130, 380, 380 );

		montaListaCampos();
		montaRadioGroup();
		montaTela();
		
		Calendar cPeriodo = Calendar.getInstance();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataini.setVlrDate( cPeriodo.getTime() );
	}

	private void montaListaCampos() {

		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtCnpjCli, "CnpjCli", "CNPJ", ListaCampos.DB_SI, false ) );

		lcCli.montaSql( false, "CLIENTE", "VD" );
		lcCli.setReadOnly( true );
		txtCodCli.setTabelaExterna( lcCli, null );
		txtCodCli.setFK( true );
		txtCodCli.setNomeCampo( "CodCli" );
		
		lcVend.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.comiss.", ListaCampos.DB_PK, false ) );
		lcVend.add( new GuardaCampo( txtDescVend, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, false ) );
		txtCodVend.setTabelaExterna( lcVend, null );
		txtCodVend.setNomeCampo( "CodVend" );
		txtCodVend.setFK( true );
		lcVend.setReadOnly( true );
		lcVend.montaSql( false, "VENDEDOR", "VD" );
		
		lcProduto.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.produto", ListaCampos.DB_PK, false ) );
		lcProduto.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		txtCodProd.setTabelaExterna( lcProduto, null );
		txtCodProd.setNomeCampo( "CodProd" );
		txtCodProd.setFK( true );
		lcProduto.setReadOnly( true );
		lcProduto.montaSql( false, "PRODUTO", "EQ" );
		
	}

	private void montaRadioGroup() {

		Vector<String> vVals1 = new Vector<String>();
		Vector<String> vLabs1 = new Vector<String>();
		vLabs1.addElement( "Emissão" );
		vLabs1.addElement( "Vencimento" );
		vVals1.addElement( "E" );
		vVals1.addElement( "V" );
		rgFiltrarPor = new JRadioGroup<String, String>( 1, 2, vLabs1, vVals1 );
		rgFiltrarPor.setVlrString( "E" );
		
		Vector<String> lSaldo = new Vector<String>();
		Vector<String> vSaldo = new Vector<String>();
		lSaldo.addElement( "TODOS" );
		lSaldo.addElement( "  > 0" );
		lSaldo.addElement( "  < 0" );
		lSaldo.addElement( ">= 0" );
		lSaldo.addElement( "<= 0" );
		lSaldo.addElement( "  = 0" );
		lSaldo.addElement( "<> 0" );
		vSaldo.addElement( "T" );
		vSaldo.addElement( "> 0" );
		vSaldo.addElement( "< 0" );
		vSaldo.addElement( ">= 0" );
		vSaldo.addElement( "<= 0" );
		vSaldo.addElement( "= 0" );
		vSaldo.addElement( "<> 0" );
		
		cbSaldo = new JComboBoxPad( lSaldo, vSaldo, JComboBoxPad.TP_INTEGER, 5, 0);
		cbSaldo.setVlrString( "T" );
	}

	private void montaTela() {
		
		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );
		JLabelPad lbPeriodo = new JLabelPad( "[ Período ]", SwingConstants.CENTER );
		lbPeriodo.setOpaque( true );

		adic( lbPeriodo, 17, 10, 80, 20 );
		adic( lbLinha, 7, 20, 300, 95 );
		adic( rgFiltrarPor, 17, 35, 285, 30 );
		adic( txtDataini, 17, 75, 125, 20 );
		adic( new JLabelPad( "à", SwingConstants.CENTER ), 142, 75, 30, 20 );
		adic( txtDatafim, 172, 75, 125, 20 );
		
		adic( new JLabelPad( "Cód.cli." ), 7, 115, 80, 20 );
		adic( txtCodCli, 7, 135, 80, 20 );
		adic( new JLabelPad( "Razão social do cliente" ), 90, 115, 210, 20 );
		adic( txtRazCli, 90, 135, 210, 20 );
		
		adic( new JLabelPad( "Cód.vend." ), 7, 160, 80, 20 );
		adic( txtCodVend, 7, 180, 80, 20 );
		adic( new JLabelPad( "Nome do vendedor" ), 90, 160, 210, 20 );
		adic( txtDescVend, 90, 180, 210, 20 );
		
		adic( new JLabelPad( "Cód. Produto" ), 7, 210, 80, 20 );
		adic( txtCodProd, 7, 230, 80, 20 );
		adic( new JLabelPad( "Descrição do produto" ), 90, 210, 210, 20 );
		adic( txtDescProd, 90, 230, 210, 20 );
		
		adic( new JLabelPad( "Listar Saldo" ), 7, 260, 100, 20 );
		adic( cbSaldo, 7, 280, 100, 20 );
		
	}

	public void imprimir( TYPE_PRINT bVisualizar ) {
		
		StringBuilder sql = new StringBuilder();
		StringBuilder filtros = new StringBuilder();
		StringBuilder sWhere = new StringBuilder();
		String sPeriodo = null;
		
		try {

			if ( txtCodCli.getVlrInteger() > 0 ) {
				
				sWhere.append( " and O.CODCLI = " + txtCodCli.getVlrInteger() );
				filtros.append( " Cliente : "+ txtCodCli.getVlrInteger() + " - " + txtRazCli.getVlrString() );
			}		
			
			if ( txtCodProd.getVlrInteger() > 0){
				
				sWhere.append( " and I.CODPRO = " + txtCodProd.getVlrInteger() );
				filtros.append( " Produto : "+ txtCodProd.getVlrInteger() + " - " + txtDescProd.getVlrString() );
			}
			
			if ( txtCodVend.getVlrInteger() > 0){
				
				sWhere.append( " and O.CODVEND = " + txtCodVend.getVlrInteger() );
				filtros.append( " Vendedor : "+ txtCodVend.getVlrInteger() + " - " + txtDescVend.getVlrString() );
			}

			sql.append( "select O.CODORC, O.CODCLI, C.RAZCLI, O.DTORC, O.DTVENCORC, I.CODPROD, P.DESCPROD, I.QTDITORC, " );
			sql.append( "I.QTDFATITORC, I.QTDAFATITORC, F.DESCPLANOPAG, V.NOMEVEND " );
			sql.append( "from VDORCAMENTO O " );
			sql.append( "inner join VDITORCAMENTO I on (I.CODEMP = O.CODEMP and I.CODFILIAL = O.CODFILIAL and I.CODORC = O.CODORC) " );
			sql.append( "inner join EQPRODUTO P on (P.CODEMP = I.CODEMPPD and P.CODFILIAL = I.CODFILIALPD and P.CODPROD = I.CODPROD) " );
			sql.append( "inner join VDCLIENTE C on (C.CODEMP = O.CODEMPCL and C.CODFILIAL = O.CODFILIALCL and C.CODCLI = O.CODCLI) " );
			sql.append( "inner join FNPLANOPAG F on (F.CODEMP = O.CODEMPPG and F.CODFILIAL = O.CODFILIALPG and F.CODPLANOPAG = O.CODPLANOPAG) " );
			sql.append( "inner join VDVENDEDOR V on (V.CODEMP = O.CODEMPVD and V.CODFILIAL = O.CODFILIALVD and V.CODVEND = O.CODVEND) " );
			sql.append( "where O.STATUSORC <> 'CA' and O.CODEMP=? and O.CODFILIAL=? and" );
			
			if ( rgFiltrarPor.getVlrString().equals( "E" )){
				sql.append( " O.DTORC between ? and ? " );
			}else
			{
				sql.append( " O.DTVENCORC between ? and ? " );
			}
			
			sPeriodo = "De: " +txtDataini.getVlrString() + " à " + txtDatafim.getVlrString();
			
			if ( sWhere.length() > 0){
				
				sql.append( sWhere );
			}
			
			if ( !cbSaldo.getVlrString().equals( "T" ) ) {
				sql.append( " and (I.QTDITORC-I.QTDFATITORC) " + cbSaldo.getVlrString() );
			}
			
			sql.append( " order by O.CODCLI, C.RAZCLI, O.CODORC" );
			
			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDORCAMENTO" ) );
			ps.setDate( 3, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( 4, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

			ResultSet rs = ps.executeQuery();

			HashMap<String, Object> hParam = new HashMap<String, Object>();
			hParam.put( "FILTROS", filtros + ".");
			hParam.put( "PERIODO", sPeriodo);

			FPrinterJob dlGr = new FPrinterJob( "layout/rel/REL_ORC_QTD_CLIENTES.jasper", "Relatório de orçamentos", "", rs, hParam, this );

			if ( bVisualizar==TYPE_PRINT.VIEW ) {
				dlGr.setVisible( true );
			}
			else {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			}

		} catch ( Exception err ) {
			Funcoes.mensagemErro( this, "Erro consultar orçamentos!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}
	
	public void valorAlterado( CheckBoxEvent evt ) {

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcCli.setConexao( cn );
		lcVend.setConexao( cn );
		lcProduto.setConexao( cn );
	}
}
