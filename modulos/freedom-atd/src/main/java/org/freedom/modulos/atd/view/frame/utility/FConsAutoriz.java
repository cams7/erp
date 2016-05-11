/**
 * @version 30/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.atd <BR>
 *         cbConveniado Classe:
 * @(#)FConsAutoriz.java <BR>
 * 
 *                       Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                       modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                       na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                       Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                       sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                       Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                       Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                       de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                       Formulário de consulta de orçamento.
 * 
 */

package org.freedom.modulos.atd.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.bmps.Icone;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.std.view.frame.crud.detail.FOrcamento;

public class FConsAutoriz extends FFilho implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCab = new JPanelPad( 0, 310 );

	private JPanelPad pnCli = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodConv = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeConv = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodEnc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtNomeEnc = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtDtIni = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDtFim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCid = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtCodTpConv = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoConv = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodGrupo = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldFK txtDescGrupo = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JCheckBoxPad cbVencidas = new JCheckBoxPad( "Vencidas", "S", "N" );

	private JCheckBoxPad cbCompleto = new JCheckBoxPad( "Completo", "S", "N" );

	private JCheckBoxPad cbLiberado = new JCheckBoxPad( "Liberado", "S", "N" );

	private JCheckBoxPad cbFaturadoParcial = new JCheckBoxPad( "Faturado Parcial", "S", "N" );

	private JCheckBoxPad cbFaturado = new JCheckBoxPad( "Faturado", "S", "N" );

	private JRadioGroup<?, ?> rgTipo = null;

	private JRadioGroup<?, ?> rgVenc;

	private JTablePad tab = new JTablePad();

	private JButtonPad btBusca = new JButtonPad( "Buscar", Icone.novo( "btPesquisa.png" ) );

	private JButtonPad btPrevimp = new JButtonPad( "Imprimir", Icone.novo( "btPrevimp.png" ) );

	private JScrollPane spnTab = new JScrollPane( tab );

	private ListaCampos lcConv = new ListaCampos( this, "PR" );

	private ListaCampos lcCli = new ListaCampos( this, "CL" );

	private ListaCampos lcEnc = new ListaCampos( this, "EC" );

	private ListaCampos lcTipoConv = new ListaCampos( this, "AT" );

	private ListaCampos lcGrupo = new ListaCampos( this );

	BigDecimal bTotalLiq = null;

	public FConsAutoriz() {

		super( false );
		setTitulo( "Pesquisa Autorização" );
		setAtribos( 10, 10, 605, 505 );
		cbVencidas.setVlrString( "S" );

		txtDtIni.setRequerido( true );
		txtDtFim.setRequerido( true );
		txtDtIni.setVlrDate( Calendar.getInstance().getTime() );
		txtDtFim.setVlrDate( Calendar.getInstance().getTime() );

		Vector<String> vVals = new Vector<String>();
		vVals.addElement( "D" );
		vVals.addElement( "V" );
		Vector<String> vLabs = new Vector<String>();
		vLabs.addElement( "Data de emissão" );
		vLabs.addElement( "Data de venc. da autorização" );
		rgVenc = new JRadioGroup<String, String>( 2, 1, vLabs, vVals );

		Vector<String> vLabs1 = new Vector<String>();
		Vector<String> vVals1 = new Vector<String>();
		vLabs1.addElement( "Texto" );
		vLabs1.addElement( "Grafico" );
		vVals1.addElement( "T" );
		vVals1.addElement( "G" );
		rgTipo = new JRadioGroup<String, String>( 2, 1, vLabs1, vVals1 );
		rgTipo.setVlrString( "G" );

		montaListaCampos();
		montaTela();
	}

	private void montaListaCampos() {

		lcConv.add( new GuardaCampo( txtCodConv, "CodConv", "Cód.conv", ListaCampos.DB_PK, false ) );
		lcConv.add( new GuardaCampo( txtNomeConv, "NomeConv", "Nome do conveniado", ListaCampos.DB_SI, false ) );
		txtCodConv.setTabelaExterna( lcConv, null );
		txtCodConv.setNomeCampo( "CodConv" );
		txtCodConv.setFK( true );
		lcConv.setReadOnly( true );
		lcConv.montaSql( false, "CONVENIADO", "AT" );

		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo( txtNomeCli, "NomeCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		txtCodCli.setTabelaExterna( lcCli, null );
		txtCodCli.setNomeCampo( "CodCli" );
		txtCodCli.setFK( true );
		lcCli.setReadOnly( true );
		lcCli.montaSql( false, "CLIENTE", "VD" );

		lcTipoConv.add( new GuardaCampo( txtCodTpConv, "CodTpConv", "Cód.tp.conv.", ListaCampos.DB_PK, false ) );
		lcTipoConv.add( new GuardaCampo( txtDescTipoConv, "DescTpConv", "Descrição do tipo de conveniado", ListaCampos.DB_SI, false ) );
		txtCodTpConv.setTabelaExterna( lcTipoConv, null );
		txtCodTpConv.setNomeCampo( "CodTpConv" );
		txtCodTpConv.setFK( true );
		lcTipoConv.setReadOnly( true );
		lcTipoConv.montaSql( false, "TIPOCONV", "AT" );

		lcEnc.add( new GuardaCampo( txtCodEnc, "CodEnc", "Cód.enc.", ListaCampos.DB_PK, false ) );
		lcEnc.add( new GuardaCampo( txtNomeEnc, "NomeEnc", "Nome do encaminhador", ListaCampos.DB_SI, false ) );
		txtCodEnc.setTabelaExterna( lcEnc, null );
		txtCodEnc.setNomeCampo( "CodEnc" );
		txtCodEnc.setFK( true );
		lcEnc.setReadOnly( true );
		lcEnc.montaSql( false, "ENCAMINHADOR", "AT" );

		lcGrupo.add( new GuardaCampo( txtCodGrupo, "CodGrup", "Cód.grupo", ListaCampos.DB_PK, false ) );
		lcGrupo.add( new GuardaCampo( txtDescGrupo, "DescGrup", "Descrição do grupo", ListaCampos.DB_SI, false ) );
		txtCodGrupo.setTabelaExterna( lcGrupo, null );
		txtCodGrupo.setNomeCampo( "CodEnc" );
		txtCodGrupo.setFK( true );
		lcGrupo.setReadOnly( true );
		lcGrupo.montaSql( false, "GRUPO", "EQ" );
	}

	private void montaTela() {

		getTela().add( pnCli, BorderLayout.CENTER );
		pnCli.add( pinCab, BorderLayout.NORTH );
		pnCli.add( spnTab, BorderLayout.CENTER );
		adicBotaoSair();

		pinCab.adic( new JLabelPad( "Cód.cli." ), 7, 0, 280, 20 );
		pinCab.adic( txtCodCli, 7, 20, 70, 20 );
		pinCab.adic( new JLabelPad( "Razão social do cliente" ), 80, 0, 280, 20 );
		pinCab.adic( txtNomeCli, 80, 20, 294, 20 );

		pinCab.adic( new JLabelPad( "Cód.conv." ), 7, 40, 280, 20 );
		pinCab.adic( txtCodConv, 7, 60, 70, 20 );
		pinCab.adic( new JLabelPad( "Nome do conveniado" ), 80, 40, 280, 20 );
		pinCab.adic( txtNomeConv, 80, 60, 294, 20 );

		pinCab.adic( new JLabelPad( "Cód.tp.conv." ), 7, 80, 250, 20 );
		pinCab.adic( txtCodTpConv, 7, 100, 70, 20 );
		pinCab.adic( new JLabelPad( "Descrição do tipo de conveniado" ), 80, 80, 250, 20 );
		pinCab.adic( txtDescTipoConv, 80, 100, 294, 20 );

		pinCab.adic( new JLabelPad( "Cód.enc." ), 7, 120, 250, 20 );
		pinCab.adic( txtCodEnc, 7, 140, 70, 20 );
		pinCab.adic( new JLabelPad( "Descrição do Encaminhador" ), 80, 120, 250, 20 );
		pinCab.adic( txtNomeEnc, 80, 140, 294, 20 );

		pinCab.adic( new JLabelPad( "Cód.grupo" ), 7, 160, 250, 20 );
		pinCab.adic( txtCodGrupo, 7, 180, 70, 20 );
		pinCab.adic( new JLabelPad( "Descrição do grupo de produtos" ), 80, 160, 250, 20 );
		pinCab.adic( txtDescGrupo, 80, 180, 294, 20 );

		pinCab.adic( new JLabelPad( "Período:" ), 380, 0, 90, 20 );
		pinCab.adic( txtDtIni, 380, 20, 87, 20 );
		pinCab.adic( new JLabelPad( "Até" ), 470, 20, 27, 20 );
		pinCab.adic( txtDtFim, 494, 20, 87, 20 );

		pinCab.adic( new JLabelPad( "Cidade" ), 380, 40, 50, 20 );
		pinCab.adic( txtCid, 380, 60, 200, 20 );

		pinCab.adic( btBusca, 440, 90, 140, 30 );
		pinCab.adic( btPrevimp, 440, 130, 140, 30 );

		JLabelPad lbBorda = new JLabelPad();
		lbBorda.setBorder( BorderFactory.createEtchedBorder() );

		pinCab.adic( new JLabelPad( "Status:" ), 7, 200, 90, 20 );
		pinCab.adic( lbBorda, 7, 220, 252, 80 );
		pinCab.adic( cbVencidas, 10, 230, 80, 20 );
		pinCab.adic( cbCompleto, 10, 250, 80, 20 );
		pinCab.adic( cbLiberado, 123, 230, 80, 20 );
		pinCab.adic( cbFaturadoParcial, 123, 250, 120, 20 );
		pinCab.adic( cbFaturado, 123, 270, 80, 20 );

		pinCab.adic( new JLabelPad( "Filtrar por:" ), 265, 200, 110, 20 );
		pinCab.adic( rgVenc, 265, 220, 220, 60 );
		pinCab.adic( rgTipo, 490, 220, 90, 60 );

		tab.adicColuna( "Cód.orc" );
		tab.adicColuna( "Emissão." );
		tab.adicColuna( "Vcto.Orc." );
		tab.adicColuna( "Valid.Autoriz." );
		tab.adicColuna( "N.Autorização" );
		tab.adicColuna( "Cod.Conv." );
		tab.adicColuna( "Nome do Conveniado" );
		tab.adicColuna( "End.Conveniado" );
		tab.adicColuna( "N.Resid." );
		tab.adicColuna( "Bairro" );
		tab.adicColuna( "Cidade" );
		tab.adicColuna( "DDD " );
		tab.adicColuna( "Fone " );
		tab.adicColuna( "Cod.Prod." );
		tab.adicColuna( "Cod.Barras" );
		tab.adicColuna( "Desc.Produto" );
		tab.adicColuna( "Qtd." );
		tab.adicColuna( "Vlr.Item" );

		tab.setTamColuna( 70, 0 );
		tab.setTamColuna( 70, 1 );
		tab.setTamColuna( 70, 2 );
		tab.setTamColuna( 70, 3 );
		tab.setTamColuna( 110, 4 );
		tab.setTamColuna( 70, 5 );
		tab.setTamColuna( 200, 6 );
		tab.setTamColuna( 200, 7 );
		tab.setTamColuna( 50, 8 );
		tab.setTamColuna( 100, 9 );
		tab.setTamColuna( 100, 10 );
		tab.setTamColuna( 50, 11 );
		tab.setTamColuna( 90, 12 );
		tab.setTamColuna( 70, 13 );
		tab.setTamColuna( 100, 14 );
		tab.setTamColuna( 250, 15 );
		tab.setTamColuna( 70, 16 );
		tab.setTamColuna( 70, 17 );

		btBusca.addActionListener( this );
		btPrevimp.addActionListener( this );

		tab.addMouseListener( new MouseAdapter() {

			public void mouseClicked( MouseEvent mevt ) {

				if ( mevt.getSource() == tab && mevt.getClickCount() == 2 )
					abreOrc();
			}
		} );
	}

	/**
	 * 
	 * Carrega os valores para a tabela de consulta. Este método é executado após carregar o ListaCampos da tabela.
	 * 
	 */
	private void carregaTabela() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;
		String sWhere = "";
		StringBuilder status = new StringBuilder();
		int iLin = 0;
		int iParam = 5;
		boolean bConv = ( txtCodConv.getVlrInteger().intValue() > 0 );
		boolean bCli = ( txtCodCli.getVlrInteger().intValue() > 0 );
		boolean bEnc = ( txtCodEnc.getVlrInteger().intValue() > 0 );

		String sCab = "";

		if ( cbVencidas.getVlrString().equals( "S" ) )
			sWhere = " AND IT.VENCAUTORIZORC < CAST ('today' AS DATE) ";

		if ( "S".equals( cbCompleto.getVlrString() ) ) {
			if ( status.length() > 0 ) {
				status.append( "," );
			}
			status.append( "'OC'" );
		}
		if ( "S".equals( cbLiberado.getVlrString() ) ) {
			if ( status.length() > 0 ) {
				status.append( "," );
			}
			status.append( "'OL'" );
		}
		if ( "S".equals( cbFaturadoParcial.getVlrString() ) ) {
			if ( status.length() > 0 ) {
				status.append( "," );
			}
			status.append( "'FP'" );
		}
		if ( "S".equals( cbFaturado.getVlrString() ) ) {
			if ( status.length() > 0 ) {
				status.append( "," );
			}
			status.append( "'OV'" );
		}

		if ( status.length() > 0 ) {
			sWhere += " AND O.STATUSORC IN (" + status.toString() + ") ";
		}

		if ( rgVenc.getVlrString().equals( "V" ) )
			sWhere += " AND IT.VENCAUTORIZORC BETWEEN ? AND ?";
		else
			sWhere += " AND O.DTORC BETWEEN ? AND ?";

		if ( bConv )
			sWhere += " AND O.CODCONV=? AND O.CODEMPCV=O.CODEMP AND O.CODFILIALCV=O.CODFILIAL ";
		if ( bCli )
			sWhere += " AND O.CODCLI=? AND O.CODEMPCV=O.CODEMP AND CODFILIALCV=O.CODFILIAL ";
		if ( bEnc )
			sWhere += " AND O.CODCONV=C.CODCONV AND O.CODEMPCV=C.CODEMP AND O.CODFILIALCV=C.CODFILIAL " + "AND C.CODENC=" + txtCodEnc.getVlrString() + " AND C.CODEMPEC=O.CODEMP AND C.CODFILIALEC=" + lcEnc.getCodFilial() + "";

		if ( !txtCid.getVlrString().equals( "" ) )
			sWhere += " AND C.CIDCONV  = '" + txtCid.getVlrString() + "'";

		if ( !txtCodTpConv.getVlrString().trim().equals( "" ) ) {
			sWhere += "AND EXISTS(SELECT T2.CODTPCONV FROM ATTIPOCONV T2, ATCONVENIADO C2 WHERE " + "T2.CODEMP=C2.CODEMPTC AND T2.CODFILIAL=C2.CODFILIALTC AND T2.CODTPCONV=C2.CODTPCONV AND " + "C2.CODEMP=O.CODEMPCV AND C2.CODFILIAL=O.CODFILIALCV AND C2.CODCONV=O.CODCONV AND " + "T2.CODEMP="
					+ Aplicativo.iCodEmp + " AND T2.CODFILIAL=" + ListaCampos.getMasterFilial( "ATTIPOCONV" ) + " AND " + "T2.CODTPCONV=" + txtCodTpConv.getVlrString().trim() + " ) ";
		}

		if ( !txtCodGrupo.getVlrString().trim().equals( "" ) ) {
			sWhere += "AND G.CODEMP=P.CODEMPGP AND G.CODFILIAL=P.CODFILIALGP AND G.CODGRUP=P.CODGRUP " + "AND G.CODGRUP LIKE '" + txtCodTpConv.getVlrString().trim() + "%' ";
		}
		try {

			sSQL = "SELECT  O.CODORC,O.DTORC,O.DTVENCORC,IT.VENCAUTORIZORC,IT.NUMAUTORIZORC," + "O.CODCONV,C.NOMECONV,c.endconv,C.NUMCONV, C.BAIRCONV, c.cidconv, C.DDDCONV, C.FONECONV , " + "IT.CODPROD, P.CODBARPROD,P.DESCPROD,it.qtditorc, it.vlrliqitorc,     "
					+ "(SELECT EC.NOMEENC FROM ATENCAMINHADOR EC WHERE EC.CODENC=C.CODENC AND " + "EC.CODEMP=C.CODEMPEC AND EC.CODFILIAL=C.CODFILIALEC) " + "FROM VDORCAMENTO O,VDCLIENTE CL," + "ATCONVENIADO C, EQPRODUTO P, VDITORCAMENTO IT "
					+ ( !txtCodGrupo.getVlrString().trim().equals( "" ) ? ", EQGRUPO G " : "" ) + "WHERE O.CODEMP=? AND O.CODFILIAL=? AND O.TIPOORC='O' " + "AND IT.CODORC=O.CODORC AND IT.CODEMP=O.CODEMP AND IT.CODFILIAL=O.CODFILIAL "
					+ "AND IT.TIPOORC=O.TIPOORC AND CL.CODEMP=O.CODEMPCL AND CL.CODFILIAL=O.CODFILIALCL " + "AND CL.CODCLI=O.CODCLI  AND C.CODEMP=O.CODEMPCV AND C.CODFILIAL=O.CODFILIALCV " + "AND O.CODCONV=C.CODCONV "
					+ "AND P.CODPROD=IT.CODPROD AND P.CODEMP=IT.CODEMPPD AND P.CODFILIAL=IT.CODFILIALPD " + sWhere + " ORDER BY O.CODORC";

			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDORCAMENTO" ) );
			ps.setDate( 3, Funcoes.dateToSQLDate( txtDtIni.getVlrDate() ) );
			ps.setDate( 4, Funcoes.dateToSQLDate( txtDtFim.getVlrDate() ) );
			if ( bConv )
				ps.setInt( iParam++, txtCodConv.getVlrInteger().intValue() );
			if ( bCli )
				ps.setInt( iParam, txtCodCli.getVlrInteger().intValue() );
			rs = ps.executeQuery();
			tab.limpa();
			bTotalLiq = new BigDecimal( "0" );
			while ( rs.next() ) {
				tab.adicLinha();
				tab.setValor( String.valueOf( rs.getInt( 1 ) ), iLin, 0 );
				tab.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( 2 ) ), iLin, 1 );
				tab.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( 3 ) ), iLin, 2 );
				tab.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( 4 ) ), iLin, 3 );
				tab.setValor( rs.getString( 5 ) != null ? rs.getString( 5 ) : "", iLin, 4 );
				tab.setValor( String.valueOf( rs.getInt( 6 ) ), iLin, 5 );
				tab.setValor( rs.getString( 7 ) != null ? rs.getString( 7 ) : "", iLin, 6 );
				tab.setValor( rs.getString( 8 ) != null ? rs.getString( 8 ) : "", iLin, 7 );
				tab.setValor( String.valueOf( rs.getInt( 9 ) ), iLin, 8 );
				tab.setValor( rs.getString( 10 ) != null ? rs.getString( 10 ) : "", iLin, 9 );
				tab.setValor( rs.getString( 11 ) != null ? rs.getString( 11 ) : "", iLin, 10 );
				tab.setValor( rs.getString( 12 ), iLin, 11 );
				tab.setValor( rs.getString( 13 ) != null ? Funcoes.setMascara( rs.getString( 13 ), "####-####" ) : "", iLin, 12 );
				tab.setValor( String.valueOf( rs.getInt( 14 ) ), iLin, 13 );
				tab.setValor( rs.getString( 15 ), iLin, 14 );
				tab.setValor( rs.getString( 16 ) != null ? rs.getString( 16 ) : "", iLin, 15 );
				tab.setValor( rs.getString( 17 ) != null ? rs.getString( 17 ) : "", iLin, 16 );
				tab.setValor( rs.getString( 18 ) != null ? rs.getString( 18 ) : "", iLin, 17 );

				bTotalLiq = bTotalLiq.add( new BigDecimal( rs.getString( "vlrliqitorc" ) ) );
				iLin++;

			}
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela VDORÇAMENTO!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	public void imprimir( final TYPE_PRINT bVisualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = "";
		String sWhere = "";
		String sStatusOrc = "";
		int iParam = 5;

		boolean bConv = ( txtCodConv.getVlrInteger().intValue() > 0 );
		boolean bCli = ( txtCodCli.getVlrInteger().intValue() > 0 );
		boolean bEnc = ( txtCodEnc.getVlrInteger().intValue() > 0 );

		if ( cbVencidas.getVlrString().equals( "S" ) )
			sWhere = " AND IT.VENCAUTORIZORC < CAST ('today' AS DATE) ";

		if ( cbCompleto.getVlrString().equals( "S" ) )
			sStatusOrc = "'OC'";
		if ( cbLiberado.getVlrString().equals( "S" ) )
			sStatusOrc += ( !sStatusOrc.equals( "" ) ? "," : "" ) + "'OL'";
		if ( cbFaturado.getVlrString().equals( "S" ) )
			sStatusOrc += ( !sStatusOrc.equals( "" ) ? "," : "" ) + "'OV'";
		if ( !sStatusOrc.equals( "" ) )
			sWhere += " AND O.STATUSORC IN (" + sStatusOrc + ") ";

		if ( rgVenc.getVlrString().equals( "V" ) )
			sWhere += " AND IT.VENCAUTORIZORC BETWEEN ? AND ?";
		else
			sWhere += " AND O.DTORC BETWEEN ? AND ?";

		if ( bConv )
			sWhere += " AND O.CODCONV=? AND O.CODEMPCV=O.CODEMP AND O.CODFILIALCV=O.CODFILIAL ";
		if ( bCli )
			sWhere += " AND O.CODCLI=? AND O.CODEMPCV=O.CODEMP AND CODFILIALCV=O.CODFILIAL ";
		if ( bEnc )
			sWhere += " AND O.CODCONV=C.CODCONV AND O.CODEMPCV=C.CODEMP AND O.CODFILIALCV=C.CODFILIAL " + "AND C.CODENC=" + txtCodEnc.getVlrString() + " AND C.CODEMPEC=O.CODEMP AND C.CODFILIALEC=" + lcEnc.getCodFilial() + "";

		if ( !txtCid.getVlrString().equals( "" ) )
			sWhere += " AND C.CIDCONV  = '" + txtCid.getVlrString() + "'";

		if ( !txtCodTpConv.getVlrString().trim().equals( "" ) ) {
			sWhere += "AND EXISTS(SELECT T2.CODTPCONV FROM ATTIPOCONV T2, ATCONVENIADO C2 WHERE " + "T2.CODEMP=C2.CODEMPTC AND T2.CODFILIAL=C2.CODFILIALTC AND T2.CODTPCONV=C2.CODTPCONV AND " +

			"C2.CODEMP=O.CODEMPCV AND C2.CODFILIAL=O.CODFILIALCV AND C2.CODCONV=O.CODCONV AND " + "T2.CODEMP=" + Aplicativo.iCodEmp + " AND T2.CODFILIAL=" + ListaCampos.getMasterFilial( "ATTIPOCONV" ) + " AND " + "T2.CODTPCONV=" + txtCodTpConv.getVlrString().trim() + " ) ";
		}

		try {

			sSQL = "SELECT  O.CODORC,O.DTORC,O.DTVENCORC,IT.VENCAUTORIZORC,IT.NUMAUTORIZORC," + "O.CODCONV,C.NOMECONV,c.endconv,C.NUMCONV, C.BAIRCONV, c.cidconv, C.DDDCONV, C.FONECONV , " + "IT.CODPROD, P.CODBARPROD,P.DESCPROD,it.qtditorc, it.vlrliqitorc,     "
					+ "(SELECT EC.NOMEENC FROM ATENCAMINHADOR EC WHERE EC.CODENC=C.CODENC AND " + "EC.CODEMP=C.CODEMPEC AND EC.CODFILIAL=C.CODFILIALEC) " + "FROM VDORCAMENTO O,VDCLIENTE CL," + "ATCONVENIADO C, EQPRODUTO P, VDITORCAMENTO IT WHERE O.CODEMP=? " + "AND O.CODFILIAL=? AND O.TIPOORC='O' "
					+ "AND IT.CODORC=O.CODORC AND IT.CODEMP=O.CODEMP AND IT.CODFILIAL=O.CODFILIAL " + "AND IT.TIPOORC=O.TIPOORC " + "AND CL.CODEMP=O.CODEMPCL AND CL.CODFILIAL=O.CODFILIALCL " + "AND CL.CODCLI=O.CODCLI  AND C.CODEMP=O.CODEMPCV AND C.CODFILIAL=O.CODFILIALCV "
					+ "AND O.CODCONV=C.CODCONV " + "AND P.CODPROD=IT.CODPROD AND P.CODEMP=IT.CODEMPPD AND " + "P.CODFILIAL=IT.CODFILIALPD " + sWhere + " ORDER BY O.CODORC";

			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDORCAMENTO" ) );
			ps.setDate( 3, Funcoes.dateToSQLDate( txtDtIni.getVlrDate() ) );
			ps.setDate( 4, Funcoes.dateToSQLDate( txtDtFim.getVlrDate() ) );

			if ( bConv )
				ps.setInt( iParam++, txtCodConv.getVlrInteger().intValue() );
			if ( bCli )
				ps.setInt( iParam, txtCodCli.getVlrInteger().intValue() );
			rs = ps.executeQuery();

		} catch ( SQLException err ) {

			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar dados!" + err.getMessage(), true, con, err );
		}

		if ( "T".equals( rgTipo.getVlrString() ) ) {
			imprimiTexto( rs, bVisualizar, "Periodo de: " + txtDtIni.getVlrDate() + " a: " + txtDtFim.getVlrDate() );
		}
		else {

			imprimiGrafico( rs, bVisualizar, "Periodo de: " + txtDtIni.getVlrString() + " a: " + txtDtFim.getVlrString() );
		}
	}

	private void imprimiTexto( final ResultSet rs, final TYPE_PRINT bVisualizar, final String sCab ) {

		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		imp.montaCab();
		imp.setTitulo( "Relatório de Orçamentos" );

		try {
			imp.limpaPags();

			for ( int iLin = 0; iLin < tab.getNumLinhas(); iLin++ ) {

				if ( imp.pRow() >= linPag ) {
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
					imp.incPags();
					imp.eject();
				}

				if ( imp.pRow() == 0 ) {
					imp.impCab( 136, true );
					imp.say( imp.pRow(), 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|" + StringFunctions.replicate( "=", 133 ) + "|" );
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|Cod.Orc." );
					imp.say( imp.pRow(), 05, "|Emissão" );
					imp.say( imp.pRow(), 14, "|Vcto.Orc." );
					imp.say( imp.pRow(), 24, "|Valid.Autoriz." );
					imp.say( imp.pRow(), 40, "|N.Autorização" );
					imp.say( imp.pRow(), 53, "|Cod.Conv." );
					imp.say( imp.pRow(), 64, "|Nome do Conveniado" );
					imp.say( imp.pRow(), 112, "|End.Conveniado" );
					imp.say( imp.pRow(), 124, "|N.Resid." );
					imp.say( imp.pRow(), 130, "|Bairro" );
					imp.say( imp.pRow(), 175, "|Cidade" );
					imp.say( imp.pRow(), 190, "|DDD" );
					imp.say( imp.pRow(), 200, "|Fone" );
					imp.say( imp.pRow(), 215, "|Cod.Prod." );
					imp.say( imp.pRow(), 235, "|Cod.Barras" );
					imp.say( imp.pRow(), 245, "|Desc.Prod." );
					imp.say( imp.pRow(), 300, "|Qtd." );
					imp.say( imp.pRow(), 315, "|Vlr.Item" );
					imp.say( imp.pRow(), 180, "|" );
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
				}
				imp.say( imp.pRow() + 1, 0, imp.comprimido() );
				imp.say( imp.pRow(), 0, "| " + tab.getValor( iLin, 0 ) );
				imp.say( imp.pRow(), 05, "| " + tab.getValor( iLin, 1 ) );
				imp.say( imp.pRow(), 14, "| " + tab.getValor( iLin, 2 ) );
				imp.say( imp.pRow(), 24, "| " + tab.getValor( iLin, 3 ) );
				imp.say( imp.pRow(), 40, "| " + Funcoes.copy( (String) tab.getValor( iLin, 4 ), 11 ) );
				imp.say( imp.pRow(), 53, "| " + Funcoes.copy( (String) tab.getValor( iLin, 5 ), 25 ) );
				imp.say( imp.pRow(), 64, "| " + Funcoes.copy( (String) tab.getValor( iLin, 6 ), 25 ) );
				imp.say( imp.pRow(), 112, "| " + Funcoes.copy( (String) tab.getValor( iLin, 7 ), 25 ) );
				imp.say( imp.pRow(), 124, "| " + tab.getValor( iLin, 8 ) );
				imp.say( imp.pRow(), 130, "| " + Funcoes.copy( (String) tab.getValor( iLin, 9 ), 15 ) );
				imp.say( imp.pRow(), 175, "| " + Funcoes.copy( (String) tab.getValor( iLin, 10 ), 15 ) );
				imp.say( imp.pRow(), 190, "| " + Funcoes.copy( (String) tab.getValor( iLin, 11 ), 10 ) );
				imp.say( imp.pRow(), 200, "| " + Funcoes.copy( (String) tab.getValor( iLin, 12 ), 15 ) );
				imp.say( imp.pRow(), 215, "| " + tab.getValor( iLin, 13 ) );
				imp.say( imp.pRow(), 235, "| " + tab.getValor( iLin, 14 ) );
				imp.say( imp.pRow(), 245, "| " + Funcoes.copy( (String) tab.getValor( iLin, 15 ), 25 ) );
				imp.say( imp.pRow(), 300, "| " + Funcoes.copy( (String) tab.getValor( iLin, 16 ), 10 ) );
				imp.say( imp.pRow(), 315, "| " + Funcoes.copy( (String) tab.getValor( iLin, 17 ), 10 ) );

				imp.say( imp.pRow(), 185, "|" );

			}
			imp.say( imp.pRow() + 1, 0, imp.comprimido() );
			imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "=", 133 ) + "+" );
			imp.say( imp.pRow() + 1, 103, " Total Geral | " + Funcoes.strDecimalToStrCurrency( 11, 2, "" + bTotalLiq ) + "      |" );
			imp.say( imp.pRow() + 1, 0, imp.comprimido() );
			imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "=", 133 ) + "+" );

			imp.eject();
			imp.fechaGravacao();

			con.commit();
		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemInforma( this, "Erro ao imprimir relatório" );
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
	}

	private void imprimiGrafico( final ResultSet rs, final TYPE_PRINT bVisualizar, final String sCab ) {

		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "CPCOMPRA" ) );
		hParam.put( "RAZAOEMP", Aplicativo.getMantenedor() );
		hParam.put( "FILTROS", sCab );

		dlGr = new FPrinterJob( "relatorios/FRConsAutoriz.jasper", "Relatório de Orçamentos por período", sCab, rs, hParam, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório Orçãmentos por periodo!" + err.getMessage(), true, con, err );
			}
		}
	}

	private void abreOrc() {

		int iCodOrc = Integer.parseInt( String.valueOf( tab.getValor( tab.getLinhaSel(), 0 ) ) );
		if ( fPrim.temTela( "Orcamento" ) == false ) {
			FOrcamento tela = new FOrcamento();
			fPrim.criatela( "Orcamento", tela, con );
			tela.exec( iCodOrc );
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btBusca ) {
			if ( txtDtIni.getVlrString().length() < 10 )
				Funcoes.mensagemInforma( this, "Digite a data inicial!" );
			else if ( txtDtFim.getVlrString().length() < 10 )
				Funcoes.mensagemInforma( this, "Digite a data final!" );
			else
				carregaTabela();
		}
		if ( evt.getSource() == btPrevimp )
			imprimir( TYPE_PRINT.VIEW );
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcConv.setConexao( cn );
		lcCli.setConexao( cn );
		lcEnc.setConexao( cn );
		lcTipoConv.setConexao( cn );
		lcGrupo.setConexao( cn );
	}
}
