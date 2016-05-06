/**
 * @version 04/2007 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * @author Alex Rodrigues<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.rep <BR>
 *         Classe:
 * @(#)RelHistoricoCliente.java <BR>
 * 
 *                              Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                              modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                              na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                              Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                              sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                              Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                              Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                              de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                              Relatorio histórico de clientes.
 * 
 */

package org.freedom.modulos.rep;

import org.freedom.infra.model.jdbc.DbConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

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
import org.freedom.modulos.rep.RPPrefereGeral.EPrefere;

public class RelHistoricoCliente extends FRelatorio {

	private static final long serialVersionUID = 1;

	private final JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private final JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldFK txtRazFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private final JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldFK txtNomeVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCodMoeda = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private final JTextFieldFK txtNomeMoeda = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCodTpCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldFK txtDescTpCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtDtIni = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtDtFim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JRadioGroup<String, String> rgOrdem;

	private final ListaCampos lcCliente = new ListaCampos( this );

	private final ListaCampos lcFornecedor = new ListaCampos( this );

	private final ListaCampos lcVendedor = new ListaCampos( this );

	private final ListaCampos lcMoeda = new ListaCampos( this );

	private final ListaCampos lcTipoCliente = new ListaCampos( this );

	private List<Object> prefere = new ArrayList<Object>();

	public RelHistoricoCliente() {

		super( false );
		setTitulo( "Relatorio de Histórico de Clientes" );
		setAtribos( 100, 50, 325, 410 );

		montaRadioGrupos();
		montaListaCampos();
		montaTela();

		Calendar cal = Calendar.getInstance();
		txtDtFim.setVlrDate( cal.getTime() );
		cal.set( cal.get( Calendar.YEAR ), cal.get( Calendar.MONTH ) - 1, cal.get( Calendar.DATE ) );
		txtDtIni.setVlrDate( cal.getTime() );
	}

	private void montaRadioGrupos() {

		Vector<String> labs2 = new Vector<String>();
		labs2.add( "Código" );
		labs2.add( "Razão social" );
		Vector<String> vals2 = new Vector<String>();
		vals2.add( "P.CODCLI,C.RAZCLI" );
		vals2.add( "C.RAZCLI,P.CODCLI" );
		rgOrdem = new JRadioGroup<String, String>( 1, 2, labs2, vals2 );
	}

	private void montaListaCampos() {

		/***********
		 * CLIENTE *
		 ***********/

		lcCliente.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCliente.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCliente.montaSql( false, "CLIENTE", "RP" );
		lcCliente.setQueryCommit( false );
		lcCliente.setReadOnly( true );
		txtCodCli.setListaCampos( lcCliente );
		txtCodCli.setTabelaExterna( lcCliente, null );
		txtCodCli.setPK( true );
		txtCodCli.setNomeCampo( "CodCli" );

		/**************
		 * FORNECEDOR *
		 **************/

		lcFornecedor.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.for.", ListaCampos.DB_PK, false ) );
		lcFornecedor.add( new GuardaCampo( txtRazFor, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );
		lcFornecedor.montaSql( false, "FORNECEDOR", "RP" );
		lcFornecedor.setQueryCommit( false );
		lcFornecedor.setReadOnly( true );
		txtCodFor.setListaCampos( lcFornecedor );
		txtCodFor.setTabelaExterna( lcFornecedor, null );
		txtCodFor.setPK( true );
		txtCodFor.setNomeCampo( "CodFor" );

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

		/*********
		 * MOEDA *
		 *********/

		lcMoeda.add( new GuardaCampo( txtCodMoeda, "CodMoeda", "Cód.moeda", ListaCampos.DB_PK, true ) );
		lcMoeda.add( new GuardaCampo( txtNomeMoeda, "SingMoeda", "Descrição da moeda", ListaCampos.DB_SI, false ) );
		lcMoeda.montaSql( false, "MOEDA", "RP" );
		lcMoeda.setQueryCommit( false );
		lcMoeda.setReadOnly( true );
		txtCodMoeda.setListaCampos( lcMoeda );
		txtCodMoeda.setTabelaExterna( lcMoeda, null );
		txtCodMoeda.setPK( true );
		txtCodMoeda.setNomeCampo( "CodMoeda" );

		/*******************
		 * TIPO DE CLIENTE *
		 *******************/

		lcTipoCliente.add( new GuardaCampo( txtCodTpCli, "CodTipoCli", "Cód.tp.cli.", ListaCampos.DB_PK, false ) );
		lcTipoCliente.add( new GuardaCampo( txtDescTpCli, "DescTipoCli", "Descrição do tipo de cliente", ListaCampos.DB_SI, false ) );
		lcTipoCliente.montaSql( false, "TIPOCLI", "RP" );
		lcTipoCliente.setQueryCommit( false );
		lcTipoCliente.setReadOnly( true );
		txtCodTpCli.setListaCampos( lcTipoCliente );
		txtCodTpCli.setTabelaExterna( lcTipoCliente, null );
		txtCodTpCli.setPK( true );
		txtCodTpCli.setNomeCampo( "CodTipoCli" );
	}

	private void montaTela() {

		adic( new JLabel( "Ordem do relatorio :" ), 10, 10, 200, 20 );
		adic( rgOrdem, 10, 35, 290, 30 );

		JLabel periodo = new JLabel( "Periodo", SwingConstants.CENTER );
		periodo.setOpaque( true );
		adic( periodo, 25, 70, 60, 20 );

		JLabel borda = new JLabel();
		borda.setBorder( BorderFactory.createEtchedBorder() );
		adic( borda, 10, 80, 290, 45 );

		adic( txtDtIni, 25, 95, 110, 20 );
		adic( new JLabel( "até", SwingConstants.CENTER ), 135, 95, 40, 20 );
		adic( txtDtFim, 175, 95, 110, 20 );

		adic( new JLabel( "Cód.for." ), 10, 130, 77, 20 );
		adic( txtCodFor, 10, 150, 77, 20 );
		adic( new JLabel( "Razão social do fornecedor" ), 90, 130, 210, 20 );
		adic( txtRazFor, 90, 150, 210, 20 );

		adic( new JLabel( "Cód.vend." ), 10, 170, 77, 20 );
		adic( txtCodVend, 10, 190, 77, 20 );
		adic( new JLabel( "Nome do vendedor" ), 90, 170, 210, 20 );
		adic( txtNomeVend, 90, 190, 210, 20 );

		adic( new JLabel( "Cód.cli." ), 10, 210, 77, 20 );
		adic( txtCodCli, 10, 230, 77, 20 );
		adic( new JLabel( "Razão social do cliente" ), 90, 210, 210, 20 );
		adic( txtRazCli, 90, 230, 210, 20 );

		adic( new JLabel( "Cód.tp.cli" ), 10, 250, 77, 20 );
		adic( txtCodTpCli, 10, 270, 77, 20 );
		adic( new JLabel( "Descrição do tipo de cliente" ), 90, 250, 210, 20 );
		adic( txtDescTpCli, 90, 270, 210, 20 );

		adic( new JLabel( "Cód.moeda" ), 10, 290, 77, 20 );
		adic( txtCodMoeda, 10, 310, 77, 20 );
		adic( new JLabel( "Descrição da moeda" ), 90, 290, 210, 20 );
		adic( txtNomeMoeda, 90, 310, 210, 20 );
	}

	@ Override
	public void imprimir( TYPE_PRINT visualizar ) {

		if ( txtCodMoeda.getVlrString().trim().length() < 1 ) {
			Funcoes.mensagemInforma( this, "O campo \"Cód.moeda\" é requerido!" );
			return;
		}

		if ( txtDtIni.getVlrDate() != null && txtDtFim.getVlrDate() != null ) {
			if ( txtDtFim.getVlrDate().before( txtDtIni.getVlrDate() ) ) {
				Funcoes.mensagemInforma( this, "Data final inferior a inicial!" );
				return;
			}
		}

		try {

			String nomevend = null;
			String moeda = null;
			String tipocli = null;
			String razfor = null;
			Date dtini = txtDtIni.getVlrDate();
			Date dtfim = txtDtFim.getVlrDate();

			StringBuilder sql = new StringBuilder();

			sql.append( "SELECT P.CODPED,P.DATAPED,P.CODCLI,C.RAZCLI,P.CODVEND,V.NOMEVEND, " );
			sql.append( "P.CODFOR,F.RAZFOR,P.NUMPEDCLI,P.NUMPEDFOR,P.QTDTOTPED,P.VLRLIQPED, " );
			sql.append( "P.VLRDESCPED,P.VLRADICPED,P.VLRIPIPED, " );
			sql.append( "C.DDDCLI,C.FONECLI,C.FAXCLI " );
			sql.append( "FROM RPPEDIDO P, RPCLIENTE C,RPVENDEDOR V, RPFORNECEDOR F, RPMOEDA M, RPTIPOCLI T " );
			sql.append( "WHERE P.CODEMP=? AND P.CODFILIAL=? " );
			sql.append( "AND P.DATAPED BETWEEN ? AND ? " );
			sql.append( "AND C.CODEMP=P.CODEMPCL AND C.CODFILIAL=P.CODFILIALCL AND C.CODCLI=P.CODCLI " );
			sql.append( "AND V.CODEMP=P.CODEMPVD AND V.CODFILIAL=P.CODFILIALVD AND V.CODVEND=P.CODVEND " );
			sql.append( "AND F.CODEMP=P.CODEMPFO AND F.CODFILIAL=P.CODFILIALFO AND F.CODFOR=P.CODFOR " );
			sql.append( "AND M.CODEMP=P.CODEMPMO AND M.CODFILIAL=P.CODFILIALMO AND M.CODMOEDA=P.CODMOEDA " );
			sql.append( "AND T.CODEMP=C.CODEMPTC AND T.CODFILIAL=C.CODFILIALTC AND T.CODTIPOCLI=C.CODTIPOCLI " );

			if ( txtCodMoeda.getVlrString().trim().length() > 0 ) {
				sql.append( "AND M.CODMOEDA='" + txtCodMoeda.getVlrString() + "'" );
				moeda = txtNomeMoeda.getVlrString();
			}
			if ( txtCodCli.getVlrString().trim().length() > 0 ) {
				sql.append( "AND C.CODCLI=" + txtCodCli.getVlrInteger() );
			}
			if ( txtCodFor.getVlrString().trim().length() > 0 ) {
				sql.append( "AND F.CODFOR=" + txtCodFor.getVlrInteger() );
				razfor = txtRazFor.getVlrString();
			}
			if ( txtCodVend.getVlrString().trim().length() > 0 ) {
				sql.append( "AND V.CODVEND=" + txtCodVend.getVlrInteger().intValue() );
				nomevend = txtNomeVend.getVlrString();
			}
			if ( txtCodTpCli.getVlrString().trim().length() > 0 ) {
				sql.append( "AND T.CODTIPOCLI=" + txtCodTpCli.getVlrInteger() );
				tipocli = txtDescTpCli.getVlrString();
			}

			sql.append( " GROUP BY P.CODCLI,C.RAZCLI,P.CODPED,P.DATAPED,P.CODVEND,V.NOMEVEND, " );
			sql.append( " P.CODFOR,F.RAZFOR,P.NUMPEDCLI,P.NUMPEDFOR,P.QTDTOTPED,P.VLRLIQPED, " );
			sql.append( " P.VLRDESCPED,P.VLRADICPED,P.VLRIPIPED, " );
			sql.append( " C.DDDCLI,C.FONECLI,C.FAXCLI " );
			sql.append( " ORDER BY " + rgOrdem.getVlrString() );
			sql.append( " ,P.CODPED,P.DATAPED,P.CODVEND,V.NOMEVEND, " );
			sql.append( " P.CODFOR,F.RAZFOR,P.NUMPEDCLI,P.NUMPEDFOR,P.QTDTOTPED,P.VLRLIQPED, " );
			sql.append( " P.VLRDESCPED,P.VLRADICPED,P.VLRIPIPED, " );
			sql.append( " C.DDDCLI,C.FONECLI,C.FAXCLI " );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "RPPEDIDO" ) );
			ps.setDate( 3, Funcoes.dateToSQLDate( dtini ) );
			ps.setDate( 4, Funcoes.dateToSQLDate( dtfim ) );
			ResultSet rs = ps.executeQuery();

			HashMap<String, Object> hParam = new HashMap<String, Object>();

			hParam.put( "CODEMP", Aplicativo.iCodEmp );
			hParam.put( "REPORT_CONNECTION", con.getConnection() );
			hParam.put( "DTINI", dtini );
			hParam.put( "DTFIM", dtfim );
			hParam.put( "NOMEVEND", nomevend );
			hParam.put( "MOEDA", moeda );
			hParam.put( "RAZFOR", razfor );
			hParam.put( "TIPOCLI", tipocli );

			FPrinterJob dlGr = new FPrinterJob( "modulos/rep/relatorios/rphistoricoclientes.jasper", "HISTÓRICO DE CLIENTES", null, rs, hParam, this );

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

		lcCliente.setConexao( cn );
		lcFornecedor.setConexao( cn );
		lcVendedor.setConexao( cn );
		lcMoeda.setConexao( cn );
		lcTipoCliente.setConexao( cn );

		prefere = RPPrefereGeral.getPrefere( cn );

		txtCodMoeda.setVlrString( (String) prefere.get( EPrefere.CODMOEDA.ordinal() ) );
		lcMoeda.carregaDados();
	}

}
