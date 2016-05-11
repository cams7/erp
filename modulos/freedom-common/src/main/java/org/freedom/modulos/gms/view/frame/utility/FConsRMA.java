/**
 * @version 02/08/2003 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 *         Projeto: Freedom <BR>
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FConsRMA.java <BR>
 *                   Este programa é licenciado de acordo com a LPG-PC (Licença Pública Geral para Programas de Computador), <BR>
 *                   modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                   A LPG-PC deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste Programa. <BR>
 *                   Caso uma cópia da LPG-PC não esteja disponível junto com este Programa, você pode contatar <BR>
 *                   sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                   Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                   Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa é preciso estar <BR>
 *                   de acordo com os termos da LPG-PC <BR>
 * <BR>
 *                   Formulário de consulta de RMA.
 */

package org.freedom.modulos.gms.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

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
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.gms.view.frame.crud.detail.FRma;

public class FConsRMA extends FFilho implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCab = new JPanelPad( 0, 185 );

	private JPanelPad pnCli = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnRod = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnLegenda = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 0, 4 ) );

	private JTextFieldPad txtDtIni = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDtFim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JCheckBoxPad cbPendentes = new JCheckBoxPad( "Requisições pendentes", "S", "N" );

	private JCheckBoxPad cbAprovadas = new JCheckBoxPad( "Requisições aprovadas", "S", "N" );

	private JCheckBoxPad cbExpedidas = new JCheckBoxPad( "Requisições expedidas", "S", "N" );

	private JCheckBoxPad cbCanceladas = new JCheckBoxPad( "Requisições canceladas", "S", "N" );

	private JTextFieldPad txtCodUsu = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldFK txtNomeUsu = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodCC = new JTextFieldPad( JTextFieldPad.TP_STRING, 19, 0 );

	private JTextFieldPad txtAnoCC = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );

	private JTextFieldFK txtDescCC = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodAlmoxarife = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtDescAlmoxarife = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodOP = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtSeqOP = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDtEmiOP = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDtFabOP = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodProdOP = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtRefProdOP = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTablePad tab = new JTablePad();

	private ImageIcon imgCancelada = Icone.novo( "clVencido.gif" );

	private ImageIcon imgExpedida = Icone.novo( "clPago.gif" );

	private ImageIcon imgAprovada = Icone.novo( "clPagoParcial.gif" );

	private ImageIcon imgPendente = Icone.novo( "clNaoVencido.gif" );

	private ImageIcon imgColuna = null;

	private JButtonPad btBusca = new JButtonPad( "Buscar", Icone.novo( "btPesquisa.png" ) );

	private JButtonPad btPrevimp = new JButtonPad( "Imprimir", Icone.novo( "btPrevimp.png" ) );

	private JButtonPad btSair = new JButtonPad( "Sair", Icone.novo( "btSair.png" ) );

	private JScrollPane spnTab = new JScrollPane( tab );

	private ListaCampos lcAlmox = new ListaCampos( this, "AM" );

	private ListaCampos lcUsuario = new ListaCampos( this, "" );

	private ListaCampos lcOP = new ListaCampos( this, "OF" );

	private ListaCampos lcSeqOP = new ListaCampos( this, "OF" );

	private ListaCampos lcCC = new ListaCampos( this, "CC" );

	boolean bAprovaParcial = false;

	boolean bExpede = false;

	boolean bAprova = false;

	private Vector<String> vSitRMA = new Vector<String>();

	public FConsRMA() {

		super( false );
		setTitulo( "Pesquisa Requisições de material" );
		setAtribos( 10, 10, 735, 480 );

		txtDtIni.setRequerido( true );
		txtDtFim.setRequerido( true );

		txtCodAlmoxarife.setNomeCampo( "CodAlmox" );
		txtCodAlmoxarife.setFK( true );

		lcAlmox.add( new GuardaCampo( txtCodAlmoxarife, "CodAlmox", "Cód.almox.", ListaCampos.DB_PK, null, false ) );
		lcAlmox.add( new GuardaCampo( txtDescAlmoxarife, "DescAlmox", "Desc.almox;", ListaCampos.DB_SI, null, false ) );
		lcAlmox.setQueryCommit( false );
		lcAlmox.setReadOnly( true );

		txtDescAlmoxarife.setSoLeitura( true );
		txtCodAlmoxarife.setTabelaExterna( lcAlmox, null );
		lcAlmox.montaSql( false, "ALMOX", "EQ" );

		txtCodOP.setNomeCampo( "CodOP" );
		txtCodOP.setFK( true );

		lcOP.add( new GuardaCampo( txtCodOP, "CodOP", "Cód. OP.", ListaCampos.DB_PK, null, false ) );
		lcOP.add( new GuardaCampo( txtDtEmiOP, "DTEMITOP", "Data de emissão", ListaCampos.DB_SI, null, false ) );
		lcOP.add( new GuardaCampo( txtDtFabOP, "DTFABROP", "Data de fabricação", ListaCampos.DB_SI, null, false ) );
		lcOP.add( new GuardaCampo( txtCodProdOP, "CodProd", "Cód.prod.", ListaCampos.DB_SI, null, false ) );
		lcOP.add( new GuardaCampo( txtRefProdOP, "RefProd", "Referência", ListaCampos.DB_SI, null, false ) );
		lcOP.setQueryCommit( false );
		lcOP.setReadOnly( true );

		txtDtEmiOP.setSoLeitura( true );
		txtDtFabOP.setSoLeitura( true );
		txtCodProdOP.setSoLeitura( true );
		txtCodOP.setTabelaExterna( lcOP, null );
		lcOP.montaSql( false, "OP", "PP" );

		txtSeqOP.setNomeCampo( "SeqOP" );
		txtSeqOP.setFK( true );

		lcSeqOP.add( new GuardaCampo( txtSeqOP, "SeqOP", "Seq. OP.", ListaCampos.DB_PK, null, false ) );
		lcSeqOP.setQueryCommit( false );
		lcSeqOP.setReadOnly( true );

		txtSeqOP.setTabelaExterna( lcSeqOP, null );
		lcSeqOP.montaSql( false, "OP", "PP" );

		txtCodUsu.setNomeCampo( "IDUSU" );
		txtCodUsu.setFK( true );

		lcUsuario.add( new GuardaCampo( txtCodUsu, "IDUSU", "ID usuario", ListaCampos.DB_PK, null, false ) );
		lcUsuario.add( new GuardaCampo( txtNomeUsu, "NOMEUSU", "Nome do usuario", ListaCampos.DB_SI, null, false ) );
		lcUsuario.setQueryCommit( false );
		lcUsuario.setReadOnly( true );

		txtNomeUsu.setSoLeitura( true );
		txtCodUsu.setTabelaExterna( lcUsuario, null );
		lcUsuario.montaSql( false, "USUARIO", "SG" );

		lcCC.add( new GuardaCampo( txtCodCC, "CodCC", "Cód.cc.", ListaCampos.DB_PK, false ) );
		lcCC.add( new GuardaCampo( txtAnoCC, "AnoCC", "Ano.cc.", ListaCampos.DB_PK, false ) );
		lcCC.add( new GuardaCampo( txtDescCC, "DescCC", "Descrição do centro de custo", ListaCampos.DB_SI, false ) );
		lcCC.setReadOnly( true );
		lcCC.setQueryCommit( false );
		lcCC.montaSql( false, "CC", "FN" );
		txtCodCC.setTabelaExterna( lcCC, null );
		txtCodCC.setFK( true );
		txtCodCC.setNomeCampo( "CodCC" );

		Container c = getTela();
		c.add( pnRod, BorderLayout.SOUTH );
		c.add( pnCli, BorderLayout.CENTER );
		pnCli.add( pinCab, BorderLayout.NORTH );
		pnCli.add( spnTab, BorderLayout.CENTER );

		btSair.setPreferredSize( new Dimension( 100, 30 ) );

		pnLegenda.add( new JLabelPad( "Cancelada", imgCancelada, SwingConstants.CENTER ) );
		pnLegenda.add( new JLabelPad( "Aprovada", imgAprovada, SwingConstants.CENTER ) );
		pnLegenda.add( new JLabelPad( "Expedida", imgExpedida, SwingConstants.CENTER ) );
		pnLegenda.add( new JLabelPad( "Pendente", imgPendente, SwingConstants.CENTER ) );

		pnRod.add( pnLegenda, BorderLayout.WEST );
		pnRod.add( btSair, BorderLayout.EAST );

		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );
		JLabelPad lbLinha2 = new JLabelPad();
		lbLinha2.setBorder( BorderFactory.createEtchedBorder() );
		JLabelPad lbStatus = new JLabelPad( "Filtrar:", SwingConstants.CENTER );
		lbStatus.setOpaque( true );

		JLabelPad lbLin = new JLabelPad();
		lbLin.setBorder( BorderFactory.createEtchedBorder() );
		JLabelPad lbPeriodo = new JLabelPad( "Periodo:", SwingConstants.CENTER );
		lbPeriodo.setOpaque( true );

		pinCab.adic( lbPeriodo, 10, 5, 80, 18 );
		pinCab.adic( lbLin, 7, 10, 280, 50 );

		pinCab.adic( new JLabelPad( "De" ), 15, 25, 27, 20 );
		pinCab.adic( txtDtIni, 35, 25, 95, 20 );
		pinCab.adic( new JLabelPad( "Até" ), 135, 25, 27, 20 );
		pinCab.adic( txtDtFim, 160, 25, 95, 20 );

		pinCab.adic( new JLabelPad( "Cód.c.c." ), 290, 5, 70, 20 );
		pinCab.adic( txtCodCC, 290, 25, 130, 20 );
		pinCab.adic( new JLabelPad( "Centro de custo" ), 427, 5, 410, 20 );
		pinCab.adic( txtDescCC, 427, 25, 210, 20 );

		pinCab.adic( new JLabelPad( "Cód.usu." ), 7, 58, 70, 20 );
		pinCab.adic( txtCodUsu, 7, 80, 70, 20 );
		pinCab.adic( new JLabelPad( "Nome do usuário" ), 80, 58, 153, 20 );
		pinCab.adic( txtNomeUsu, 80, 80, 206, 20 );
		pinCab.adic( new JLabelPad( "Cód. OP." ), 290, 58, 153, 20 );
		pinCab.adic( txtCodOP, 290, 80, 100, 20 );
		pinCab.adic( new JLabelPad( "Seq. OP." ), 393, 58, 153, 20 );
		pinCab.adic( txtSeqOP, 393, 80, 100, 20 );

		pinCab.adic( lbStatus, 15, 100, 50, 18 );
		pinCab.adic( lbLinha2, 7, 110, 373, 66 );
		pinCab.adic( cbPendentes, 15, 122, 170, 20 );
		pinCab.adic( cbAprovadas, 15, 147, 170, 20 );
		pinCab.adic( cbExpedidas, 195, 122, 180, 20 );
		pinCab.adic( cbCanceladas, 195, 147, 180, 20 );

		pinCab.adic( btBusca, 382, 110, 110, 30 );
		pinCab.adic( btPrevimp, 382, 145, 110, 30 );

		txtDtIni.setVlrDate( new Date() );
		txtDtFim.setVlrDate( new Date() );

		tab.adicColuna( "" );
		tab.adicColuna( "Rma." );
		tab.adicColuna( "Data" );
		tab.adicColuna( "Usuário" );
		tab.adicColuna( "CC" );
		tab.adicColuna( "Motivo" );
		tab.adicColuna( "Projeto/Contrato" );

		tab.setTamColuna( 12, 0 );
		tab.setTamColuna( 40, 1 );
		tab.setTamColuna( 70, 2 );
		tab.setTamColuna( 50, 3 );
		tab.setTamColuna( 180, 4 );
		tab.setTamColuna( 200, 5 );
		tab.setTamColuna( 150, 6 );

		btBusca.addActionListener( this );
		btPrevimp.addActionListener( this );

		tab.addMouseListener( new MouseAdapter() {

			public void mouseClicked( MouseEvent mevt ) {

				if ( mevt.getSource() == tab && mevt.getClickCount() == 2 )
					abreRma();
			}
		} );
		btSair.addActionListener( this );

	}

	private void habCampos() {

		getAprova();
		if ( !bExpede ) {
			if ( bAprova ) {
				if ( bAprovaParcial ) {
					txtCodCC.setVlrString( Aplicativo.getUsuario().getCodcc() );
					txtAnoCC.setVlrInteger( Aplicativo.getUsuario().getAnocc() );
					txtCodCC.setNaoEditavel( true );
					lcUsuario.setWhereAdic( "CODCC='" + Aplicativo.getUsuario().getCodcc() + "' AND ANOCC=" + Aplicativo.getUsuario().getAnocc() );
				}
				else {
					txtCodCC.setNaoEditavel( false );

				}
				txtCodUsu.setNaoEditavel( false );
			}
			else {
				txtCodUsu.setVlrString( Aplicativo.getUsuario().getIdusu() );
				txtCodCC.setVlrString( Aplicativo.getUsuario().getCodcc() );
				txtAnoCC.setVlrInteger( Aplicativo.getUsuario().getAnocc() );

				txtCodUsu.setNaoEditavel( true );
				txtCodCC.setNaoEditavel( true );
				lcUsuario.carregaDados();
				lcCC.carregaDados();
			}
		}
	}

	/**
	 * Carrega os valores para a tabela de consulta. Este método é executado após carregar o ListaCampos da tabela.
	 */
	private void carregaTabela() {

		String where = "";
		boolean usaOr = false;
		boolean usaWhere = false;
		boolean usuario = ( !txtCodUsu.getVlrString().trim().equals( "" ) );
		boolean almoxarifado = false;
		boolean CC = ( !txtCodCC.getVlrString().trim().equals( "" ) );
		String sCodOp = txtCodOP.getVlrString();
		String sSeqOp = txtSeqOP.getVlrString();

		if ( cbPendentes.getVlrString().equals( "S" ) ) {
			usaWhere = true;
			where = " SitRma ='PE'";
		}
		if ( cbAprovadas.getVlrString().equals( "S" ) ) {
			if ( where.trim().equals( "" ) ) {
				where = " SitRma ='AF'";
			}
			else {
				where = where + " OR SitRma ='AF'";
				usaOr = true;
			}
			usaWhere = true;
		}
		if ( cbExpedidas.getVlrString().equals( "S" ) ) {
			if ( where.trim().equals( "" ) ) {
				where = " SitRma ='EF'";
			}
			else {
				where = where + " OR SitRma ='EF'";
				usaOr = true;
			}
			usaWhere = true;
		}
		if ( cbCanceladas.getVlrString().equals( "S" ) ) {
			if ( where.trim().equals( "" ) ) {
				where = " SitRma ='CA'";
			}
			else {
				where = where + " OR SitRma ='CA'";
				usaOr = true;
			}
			usaWhere = true;
		}

		if ( usaWhere && usaOr )
			where = " AND (" + where + ")";
		else if ( usaWhere )
			where = " AND " + where;
		else
			where = " AND SitRma='PE'";

		if ( sCodOp.length() > 0 )
			where += " AND R.CODOP = '" + sCodOp + "'";

		if ( sSeqOp.length() > 0 )
			where += " AND R.SEQOP = '" + sSeqOp + "'";

		if ( almoxarifado )
			where += " AND IT.CODALMOX=? AND IT.CODEMPAM=? AND IT.CODFILIALAM=? ";

		if ( CC )
			where += " AND R.ANOCC=? AND R.CODCC=? AND R.CODEMPCC=? AND R.CODFILIALCC=? ";

		if ( usuario )
			where += " AND (R.IDUSU=?) ";
		StringBuilder sql = new StringBuilder();

		sql.append( "select r.sitrma, r.codrma, r.dtareqrma, r.idusu, fn.desccc, r.motivorma, " );
		sql.append( "ct.codcontr, ct.desccontr " );
		sql.append( "from  eqrma r " );
		sql.append( "left outer join vdcontrato ct on " );
		sql.append( "ct.codemp=r.codempct and ct.codfilial=r.codfilialct and ct.codcontr=r.codcontr, " );
		sql.append( " eqitrma it, fncc fn " );
		sql.append( "where r.codemp=? and r.codfilial=? and it.codrma=r.codrma and it.codemp=r.codemp and it.codfilial=r.codfilial " );
		sql.append( "and r.anocc=fn.anocc and r.codcc=fn.codcc " );
		sql.append( "and ((it.dtaprovitrma between ? and ? ) or  (r.dtareqrma between ? and ?)) " );
		sql.append( where );
		sql.append( " group by r.codrma, r.sitrma, r.dtareqrma, r.idusu, fn.desccc, r.motivorma, ct.codcontr, ct.desccontr  " );

		System.out.println( sql.toString() );

		try {

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			int param = 1;
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "EQRMA" ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDtIni.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDtFim.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDtIni.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDtFim.getVlrDate() ) );

			if ( almoxarifado ) {
				ps.setInt( param++, txtCodAlmoxarife.getVlrInteger().intValue() );
				ps.setInt( param++, Aplicativo.iCodEmp );
				ps.setInt( param++, Aplicativo.iCodFilial );
			}

			if ( CC ) {
				ps.setInt( param++, txtAnoCC.getVlrInteger().intValue() );
				ps.setString( param++, txtCodCC.getVlrString() );
				ps.setInt( param++, Aplicativo.iCodEmp );
				ps.setInt( param++, Aplicativo.iCodFilial );
			}

			if ( usuario ) {
				ps.setString( param++, txtCodUsu.getVlrString() );
			}

			ResultSet rs = ps.executeQuery();

			int iLin = 0;

			tab.limpa();
			vSitRMA = new Vector<String>();
			while ( rs.next() ) {
				tab.adicLinha();

				String sitRMA = rs.getString( 1 );
				if ( sitRMA.equalsIgnoreCase( "PE" ) ) {
					imgColuna = imgPendente;
					vSitRMA.addElement( "Pendente" );
				}
				else if ( sitRMA.equalsIgnoreCase( "CA" ) ) {
					imgColuna = imgCancelada;
					vSitRMA.addElement( "Cancelada" );
				}
				else if ( sitRMA.equalsIgnoreCase( "EF" ) ) {
					imgColuna = imgExpedida;
					vSitRMA.addElement( "Expedida" );
				}
				else if ( sitRMA.equalsIgnoreCase( "AF" ) ) {
					imgColuna = imgAprovada;
					vSitRMA.addElement( "Aprovada" );
				}

				tab.setValor( imgColuna, iLin, 0 );
				tab.setValor( new Integer( rs.getInt( 2 ) ), iLin, 1 );
				tab.setValor( rs.getString( 3 ) == null ? "-" : StringFunctions.sqlDateToStrDate( rs.getDate( 3 ) ) + "", iLin, 2 );
				tab.setValor( rs.getString( 4 ) == null ? "-" : rs.getString( 4 ) + "", iLin, 3 );
				tab.setValor( rs.getString( 5 ) == null ? "-" : rs.getString( 5 ) + "", iLin, 4 );
				tab.setValor( rs.getString( 6 ) == null ? "-" : rs.getString( 6 ) + "", iLin, 5 );
				tab.setValor( rs.getString( "desccontr" ) == null ? "" : rs.getString( "desccontr" ), iLin, 6 );

				iLin++;
			}

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela EQRMA!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		BigDecimal bTotalLiq = new BigDecimal( "0" );
		boolean bImpCot = false;

		/*
		 * bImpCot = Funcoes.mensagemConfirma(this, "Deseja imprimir informações de cotações de preço?") == 0 ? true : false;
		 */

		try {
			imp.limpaPags();
			for ( int iLin = 0; iLin < tab.getNumLinhas(); iLin++ ) {
				if ( imp.pRow() == 0 ) {
					imp.montaCab();
					imp.setTitulo( "Relatório de Requisições de material" );
					imp.addSubTitulo( "Relatório de Requisições de material" );
					imp.impCab( 136, true );
					imp.say( imp.pRow() + 0, 0, "| Rma." );
					imp.say( imp.pRow() + 0, 15, "| Emissão" );
					imp.say( imp.pRow() + 0, 29, "| Situação" );
					imp.say( imp.pRow() + 0, 45, "| Usuário" );
					imp.say( imp.pRow() + 0, 65, "| Motivo" );
					imp.say( imp.pRow() + 0, 135, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );

					if ( bImpCot ) {
						imp.say( imp.pRow() + 0, 0, "| Nro. Pedido" );
						imp.say( imp.pRow() + 0, 15, "| Nro. Nota" );
						imp.say( imp.pRow() + 0, 29, "| Data Fat." );
						imp.say( imp.pRow() + 0, 41, "| " );
						imp.say( imp.pRow() + 0, 56, "| " );
						imp.say( imp.pRow() + 0, 87, "| Vlr. Item Fat." );
						imp.say( imp.pRow() + 0, 105, "| " );
						imp.say( imp.pRow() + 0, 124, "| " );
						imp.say( imp.pRow() + 0, 135, "|" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );

					}

					imp.say( imp.pRow() + 0, 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );

				}

				imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
				imp.say( imp.pRow() + 0, 0, "|" + tab.getValor( iLin, 1 ) );
				imp.say( imp.pRow() + 0, 15, "| " + tab.getValor( iLin, 2 ) );
				imp.say( imp.pRow() + 0, 29, "| " + vSitRMA.elementAt( iLin ).toString() );
				String sMotivo = "" + tab.getValor( iLin, 3 );
				imp.say( imp.pRow() + 0, 45, "| " + sMotivo.substring( 0, sMotivo.length() > 89 ? 89 : sMotivo.length() ).trim() );
				imp.say( imp.pRow() + 0, 135, "| " );

				if ( bImpCot ) {
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 2, "|" + tab.getValor( iLin, 2 ) );
					imp.say( imp.pRow() + 0, 15, "|" + tab.getValor( iLin, 3 ) );
					imp.say( imp.pRow() + 0, 29, "|" );
					imp.say( imp.pRow() + 0, 41, "|" );
					imp.say( imp.pRow() + 0, 56, "|" );
					imp.say( imp.pRow() + 0, 87, "|" + tab.getValor( iLin, 12 ) );
					imp.say( imp.pRow() + 0, 105, "|" );
					imp.say( imp.pRow() + 0, 124, "|" );
					imp.say( imp.pRow() + 0, 135, "|" );
				}

				if ( tab.getValor( iLin, 9 ) != null ) {
					bTotalLiq = bTotalLiq.add( new BigDecimal( Funcoes.strCurrencyToDouble( "" + tab.getValor( iLin, 9 ) ) ) );
				}

				if ( imp.pRow() >= linPag ) {
					imp.incPags();
					imp.eject();
				}
			}

			imp.say( imp.pRow() + 1, 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
			imp.eject();

			imp.fechaGravacao();

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela de orçamentos!\n" + err.getMessage(), true, con, err );
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
	}

	private void abreRma() {

		int iRma = ( (Integer) tab.getValor( tab.getLinhaSel(), 1 ) ).intValue();
		if ( fPrim.temTela( "Requisição de material" ) == false ) {
			FRma tela = new FRma();
			fPrim.criatela( "Requisição de material", tela, con );
			tela.exec( iRma );
		}
	}

	private void getAprova() {

		String sSQL = "SELECT ANOCC,CODCC,CODEMPCC,CODFILIALCC,APROVRMAUSU,ALMOXARIFEUSU " + "FROM SGUSUARIO WHERE CODEMP=? AND CODFILIAL=? " + "AND IDUSU=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGUSUARIO" ) );
			ps.setString( 3, Aplicativo.getUsuario().getIdusu() );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				String sAprova = rs.getString( "APROVRMAUSU" );
				String sExpede = rs.getString( "ALMOXARIFEUSU" );
				if ( sAprova != null ) {
					if ( !sAprova.equals( "ND" ) ) {
						if ( sAprova.equals( "TD" ) )
							bAprova = true;
						else if ( ( Aplicativo.getUsuario().getCodcc().equals( rs.getString( "CODCC" ) ) ) && ( Aplicativo.iCodEmp == rs.getInt( "CODEMPCC" ) ) && ( ListaCampos.getMasterFilial( "FNCC" ) == rs.getInt( "CODFILIALCC" ) ) && ( sAprova.equals( "CC" ) ) ) {
							bAprova = true;
							bAprovaParcial = true;
						}
					}
				}
				if ( sExpede != null ) {
					if ( sExpede.equals( "S" ) ) {
						bExpede = true;
					}
					else {
						bExpede = false;
					}
				}
			}
			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage(), true, con, err );
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btSair ) {
			dispose();
		}
		if ( evt.getSource() == btBusca ) {
			if ( txtDtIni.getVlrString().length() < 10 )
				Funcoes.mensagemInforma( this, "Digite a data inicial!" );
			else if ( txtDtFim.getVlrString().length() < 10 )
				Funcoes.mensagemInforma( this, "Digite a data final!" );
			else
				carregaTabela();
			if ( evt.getSource() == btPrevimp ) {
				imprimir( TYPE_PRINT.VIEW );
			}
		}
		if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}

	}

	private int buscaVlrPadrao() {

		int iRet = 0;
		String sSQL = "SELECT ANOCENTROCUSTO FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			ResultSet rs = ps.executeQuery();
			if ( rs.next() )
				iRet = rs.getInt( "ANOCENTROCUSTO" );
			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar o ano-base para o centro de custo.\n" + err.getMessage() );
		}

		return iRet;
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcAlmox.setConexao( cn );
		lcOP.setConexao( cn );
		lcSeqOP.setConexao( cn );
		lcUsuario.setConexao( cn );
		lcCC.setConexao( cn );
		lcCC.setWhereAdic( "NIVELCC=10 AND ANOCC=" + buscaVlrPadrao() );
		habCampos();
	}
}
