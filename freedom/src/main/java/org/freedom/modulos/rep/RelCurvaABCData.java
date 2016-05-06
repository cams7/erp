/**
 * @version 07/2009 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * @author Alex Rodrigues<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.rep <BR>
 *         Classe:
 * @(#)RelCurvaABCData.java <BR>
 * 
 *                          Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                          modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                          na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                          Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                          sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                          Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                          Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                          de acordo com os termos da LPG-PC <BR>
 * <BR>
 */

package org.freedom.modulos.rep;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

public class RelCurvaABCData extends FRelatorio {

	private static final long serialVersionUID = 1;

	private final JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldFK txtNomeVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCurvaA = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 2, 0 );

	private final JTextFieldPad txtCurvaB = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 2, 0 );

	private final JTextFieldPad txtCurvaC = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 2, 0 );

	private final JCheckBoxPad cbOrdem = new JCheckBoxPad( "Ordenar por data decrescente?", "S", "N" );

	private final ListaCampos lcFornecedor = new ListaCampos( this );

	private final ListaCampos lcVendedor = new ListaCampos( this );

	public RelCurvaABCData() {

		super( false );
		setTitulo( "Relatorio de Curva ABC por data" );
		setAtribos( 100, 50, 325, 200 );

		montaListaCampos();
		montaTela();

		txtCurvaA.setVlrInteger( 20 );
		txtCurvaB.setVlrInteger( 30 );
		txtCurvaC.setVlrInteger( 50 );

		cbOrdem.setVlrString( "N" );
	}

	private void montaListaCampos() {

		/************
		 * VENDEDOR *
		 ************/

		lcVendedor.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.vend.", ListaCampos.DB_PK, false ) );
		lcVendedor.add( new GuardaCampo( txtNomeVend, "NomeVend", "Nome do vendedor", ListaCampos.DB_SI, false ) );
		lcVendedor.montaSql( false, "VENDEDOR", "RP" );
		lcVendedor.setQueryCommit( false );
		lcVendedor.setReadOnly( true );
		txtCodVend.setListaCampos( lcVendedor );
		txtCodVend.setTabelaExterna( lcVendedor, null );
		txtCodVend.setPK( true );
		txtCodVend.setNomeCampo( "CodVend" );
	}

	private void montaTela() {

		adic( new JLabel( "Cód.vend." ), 10, 10, 77, 20 );
		adic( txtCodVend, 10, 30, 77, 20 );
		adic( new JLabel( "Nome do vendedor" ), 90, 10, 210, 20 );
		adic( txtNomeVend, 90, 30, 210, 20 );

		adic( new JLabel( "% A", SwingConstants.CENTER ), 20, 60, 30, 20 );
		adic( txtCurvaA, 50, 60, 50, 20 );
		adic( new JLabel( "% B", SwingConstants.CENTER ), 120, 60, 30, 20 );
		adic( txtCurvaB, 150, 60, 50, 20 );
		adic( new JLabel( "% C", SwingConstants.CENTER ), 220, 60, 30, 20 );
		adic( txtCurvaC, 250, 60, 50, 20 );

		adic( cbOrdem, 10, 90, 290, 20 );
	}

	@ Override
	public void imprimir( TYPE_PRINT visualizar ) {

		if ( ( txtCurvaA.getVlrInteger() + txtCurvaB.getVlrInteger() + txtCurvaC.getVlrInteger() ) != 100 ) {
			Funcoes.mensagemInforma( this, "O total da soma do valor das curvas deve ser 100 !" );
			return;
		}

		try {

			String nomevend = null;
			String razcli = null;
			String razfor = null;

			StringBuilder where = new StringBuilder();

			if ( txtCodVend.getVlrString().trim().length() > 0 ) {
				where.append( " AND C.CODVEND=" + txtCodVend.getVlrInteger().intValue() );
				nomevend = txtNomeVend.getVlrString();
			}

			StringBuilder sql = new StringBuilder();

			sql.append( "SELECT C.CODCLI, C.RAZCLI, C.DDDCLI, C.FONECLI, " );
			sql.append( "C.CODVEND, V.NOMEVEND, " );
			sql.append( "(SELECT FIRST 1 P.DATAPED FROM RPPEDIDO P " );
			sql.append( " WHERE P.CODEMPCL=C.CODEMP AND P.CODFILIALCL=C.CODFILIAL AND P.CODCLI=C.CODCLI " );
			sql.append( " order BY P.DATAPED DESC) ULTIMA_DATA, " );
			sql.append( "(SELECT FIRST 1 P.CODPED FROM RPPEDIDO P " );
			sql.append( " WHERE P.CODEMPCL=C.CODEMP AND P.CODFILIALCL=C.CODFILIAL AND P.CODCLI=C.CODCLI " );
			sql.append( " order BY P.DATAPED DESC) ULTIMO_PEDIDO, " );
			sql.append( "(SELECT FIRST 1 P.VLRLIQPED FROM RPPEDIDO P " );
			sql.append( " WHERE P.CODEMPCL=C.CODEMP AND P.CODFILIALCL=C.CODFILIAL AND P.CODCLI=C.CODCLI " );
			sql.append( " order BY P.DATAPED DESC) ULTIMO_VALOR " );
			sql.append( "FROM RPCLIENTE C, RPVENDEDOR V " );
			sql.append( "WHERE C.CODEMP=? AND C.CODFILIAL=? AND " );
			sql.append( "V.CODEMP=C.CODEMPVO AND V.CODFILIAL=C.CODFILIALVO AND V.CODVEND=C.CODVEND " );
			sql.append( where );
			sql.append( "ORDER BY 7 " + ( "S".equals( cbOrdem.getVlrString() ) ? "DESC" : "" ) + ", 9 DESC " );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "RPCLIENTE" ) );
			ResultSet rs = ps.executeQuery();

			int rows = 0;
			while ( rs.next() ) {
				rows++;
			}

			double porcentagemClientes = rows / 100d;
			int curvaA = (int) ( porcentagemClientes * txtCurvaA.getVlrInteger() );
			int curvaB = (int) ( porcentagemClientes * txtCurvaB.getVlrInteger() );
			int curvaC = (int) ( porcentagemClientes * txtCurvaC.getVlrInteger() );

			rs = ps.executeQuery();

			HashMap<String, Object> hParam = new HashMap<String, Object>();

			hParam.put( "CODEMP", Aplicativo.iCodEmp );
			hParam.put( "REPORT_CONNECTION", con.getConnection() );
			hParam.put( "NOMEVEND", nomevend );
			hParam.put( "CURVA_A", curvaA );
			hParam.put( "CURVA_B", curvaA + curvaB );
			hParam.put( "CURVA_C", curvaA + curvaB + curvaC );

			FPrinterJob dlGr = new FPrinterJob( "modulos/rep/relatorios/rpcurvaABCData.jasper", "CURVA ABC DE DATAS", null, rs, hParam, this );

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
		lcVendedor.setConexao( cn );
	}

}
