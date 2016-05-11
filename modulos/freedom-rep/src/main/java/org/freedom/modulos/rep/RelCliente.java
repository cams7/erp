/**
 * @version 03/2007 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * @author Alex Rodrigues<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.rep <BR>
 *         Classe:
 * @(#)RelCliente.java <BR>
 * 
 *                     Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                     modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                     na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                     Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                     sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                     Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                     Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                     de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                     Relatorio de clientes, em dois modos: completo e resumido.
 * 
 */

package org.freedom.modulos.rep;

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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JLabel;

import net.sf.jasperreports.engine.JasperPrintManager;

public class RelCliente extends FRelatorio {

	private static final long serialVersionUID = 1;

	private final JTextFieldPad txtCodTpCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldFK txtDescTpCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldFK txtNomeVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCidade = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JRadioGroup<String, String> rgModo;

	private JRadioGroup<String, String> rgOrdem;

	private final ListaCampos lcTipoCli = new ListaCampos( this );

	private final ListaCampos lcVend = new ListaCampos( this );

	public RelCliente() {

		super( false );
		setTitulo( "Relatorio de clientes" );
		setAtribos( 50, 50, 325, 330 );

		montaRadioGrupos();
		montaListaCampos();
		montaTela();
	}

	private void montaRadioGrupos() {

		Vector<String> labs = new Vector<String>();
		labs.add( "completo" );
		labs.add( "resumido" );
		Vector<String> vals = new Vector<String>();
		vals.add( "C" );
		vals.add( "R" );
		rgModo = new JRadioGroup<String, String>( 1, 3, labs, vals );

		Vector<String> labs1 = new Vector<String>();
		labs1.add( "Código" );
		labs1.add( "Nome" );
		labs1.add( "Bairro" );
		
		Vector<String> vals1 = new Vector<String>();
		vals1.add( "CODCLI" );
		vals1.add( "RAZCLI" );
		vals1.add( "BAIRCLI" );
		rgOrdem = new JRadioGroup<String, String>( 1, 3, labs1, vals1 );
	}

	private void montaListaCampos() {

		lcTipoCli.add( new GuardaCampo( txtCodTpCli, "CodTipoCli", "Cód.tp.cli.", ListaCampos.DB_PK, false ) );
		lcTipoCli.add( new GuardaCampo( txtDescTpCli, "DescTipoCli", "Descrição do tipo de cliente", ListaCampos.DB_SI, false ) );
		lcTipoCli.montaSql( false, "TIPOCLI", "RP" );
		lcTipoCli.setQueryCommit( false );
		lcTipoCli.setReadOnly( true );
		txtCodTpCli.setListaCampos( lcTipoCli );
		txtCodTpCli.setTabelaExterna( lcTipoCli, null );
		txtCodTpCli.setPK( true );
		txtCodTpCli.setNomeCampo( "CodTipoCli" );

		lcVend.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.vend.", ListaCampos.DB_PK, false ) );
		lcVend.add( new GuardaCampo( txtNomeVend, "NomeVend", "Nome do vendedor", ListaCampos.DB_SI, false ) );
		lcVend.montaSql( false, "VENDEDOR", "RP" );
		lcVend.setQueryCommit( false );
		lcVend.setReadOnly( true );
		txtCodVend.setListaCampos( lcVend );
		txtCodVend.setTabelaExterna( lcVend, null );
		txtCodVend.setPK( true );
		txtCodVend.setNomeCampo( "CodVend" );
	}

	private void montaTela() {

		adic( new JLabel( "Modo :" ), 10, 10, 200, 20 );
		adic( rgModo, 10, 35, 290, 30 );
		adic( new JLabel( "Ordem do relatorio :" ), 10, 70, 200, 20 );
		adic( rgOrdem, 10, 95, 290, 30 );

		adic( new JLabel( "Cód.tp.cli." ), 10, 130, 77, 20 );
		adic( txtCodTpCli, 10, 150, 77, 20 );
		adic( new JLabel( "Descrição do tipo de cliente" ), 90, 130, 210, 20 );
		adic( txtDescTpCli, 90, 150, 210, 20 );

		adic( new JLabel( "Cód.vend." ), 10, 170, 77, 20 );
		adic( txtCodVend, 10, 190, 77, 20 );
		adic( new JLabel( "Nome do vendedor" ), 90, 170, 210, 20 );
		adic( txtNomeVend, 90, 190, 210, 20 );

		adic( new JLabel( "Cidade" ), 10, 210, 290, 20 );
		adic( txtCidade, 10, 230, 290, 20 );
	}

	@ Override
	public void imprimir( TYPE_PRINT visualizar ) {

		try {

			String relatorio = "C".equals( rgModo.getVlrString() ) ? "rpclientecomp.jasper" : "rpclienteresum.jasper";
			String modo = "C".equals( rgModo.getVlrString() ) ? "( completo )" : "( resumido )";
			String filtro = "";

			StringBuilder sql = new StringBuilder();

			sql.append( "SELECT CODCLI,RAZCLI,NOMECLI,CNPJCLI,INSCCLI, " );
			sql.append( "ENDCLI,CIDCLI,ESTCLI,CEPCLI,BAIRCLI,DDDCLI, " );
			sql.append( "FONECLI,FAXCLI,EMAILCLI,CONTATOCLI,CELCONTATOCLI " );
			sql.append( "FROM RPCLIENTE " );
			sql.append( "WHERE CODEMP=? AND CODFILIAL=? " );
			if ( txtCodTpCli.getVlrString().trim().length() > 0 ) {
				sql.append( "AND CODTIPOCLI=" + txtCodTpCli.getVlrInteger().intValue() );
				filtro = "Tipo de cliente : " + txtDescTpCli.getVlrString().trim();
			}
			if ( txtNomeVend.getVlrString().trim().length() > 0 ) {
				sql.append( "AND CODVEND=" + txtCodVend.getVlrInteger().intValue() );
				filtro += "  Vendedor : " + txtNomeVend.getVlrString().trim();
			}
			if ( txtCidade.getVlrString().trim().length() > 0 ) {
				sql.append( "AND CIDCLI LIKE '%" + txtCidade.getVlrString() + "%' " );
			}
			sql.append( "ORDER BY " + rgOrdem.getVlrString() );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "RPCLIENTE" ) );
			ResultSet rs = ps.executeQuery();

			HashMap<String, Object> hParam = new HashMap<String, Object>();

			hParam.put( "CODEMP", Aplicativo.iCodEmp );
			hParam.put( "REPORT_CONNECTION", con.getConnection() );

			FPrinterJob dlGr = new FPrinterJob( "modulos/rep/relatorios/" + relatorio, "CLIENTES " + modo, filtro, rs, hParam, this );

			if ( visualizar == TYPE_PRINT.VIEW ) {
				dlGr.setVisible( true );
			}
			else {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			}

		} catch ( Exception e ) {
			Funcoes.mensagemErro( this, "Erro ao montar relatorio!\n" + e.getMessage() );
			e.printStackTrace();
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcTipoCli.setConexao( cn );
		lcVend.setConexao( cn );
	}

}
