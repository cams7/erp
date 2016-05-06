/**
 * @version 01/03/2007 <BR>
 * @author Setpoint Informática Ltda./RobsonSanchez/Alex Rodrigues<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FRemFBN.java <BR>
 * 
 *                  Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                  modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                  na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                  Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                  sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                  Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                  Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                  de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                  Tela de remessa de arquivo, contendo os dados dos clientes e recebimentos, para o banco selecionado.
 * 
 */

package org.freedom.modulos.fnc.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.freedom.acao.RadioGroupEvent;
import org.freedom.acao.RadioGroupListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.functions.ConversionFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.component.Banco;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.library.type.StringDireita;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.EPrefs;
import org.freedom.modulos.fnc.view.dialog.utility.DLIdentCli;

public abstract class FRemFBN extends FFilho implements ActionListener, MouseListener, RadioGroupListener {

	private static final long serialVersionUID = 1L;

	protected static final String TIPO_FEBRABAN_SIACC = "01";

	protected static final String TIPO_FEBRABAN_CNAB = "02";

	protected final String TIPO_FEBRABAN;

	protected final JLabel lbValorSelecionado = new JLabel();

	private JPanelPad panelRodape = null;

	private final JPanelPad panelRemessa = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad panelFiltros = new JPanelPad();

	private final JPanelPad panelTabela = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad panelFuncoes = new JPanelPad();

	private final JPanelPad panelStatus = new JPanelPad( );

	private final JPanelPad panelImp = new JPanelPad( JPanelPad.TP_JPANEL, new FlowLayout( FlowLayout.CENTER, 0, 0 ) );

	private final JPanelPad pnImp = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 2 ) );

	protected final JTablePad tab = new JTablePad();

	protected final JTextFieldPad txtCodBanco = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	protected final JTextFieldFK txtNomeBanco = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	protected final JTextFieldPad txtDtIni = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	protected final JTextFieldPad txtDtFim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtCodCartCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private final JTextFieldFK txtDescCartCob = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private final JTextFieldFK txtConvCob = new JTextFieldFK( JTextFieldPad.TP_STRING, 15, 0 ); 

	protected JRadioGroup<String, String> rgData;

	protected JRadioGroup<String, String> rgSitRemessa;

	protected JRadioGroup<String, String> rgTipoRemessa;

	private final JButtonPad btCarrega = new JButtonPad( "Buscar", Icone.novo( "btExecuta.png" ) );

	private final JButtonPad btExporta = new JButtonPad( "Exportar", Icone.novo( "btSalvar.gif" ) );

	private final JButtonPad btSelTudo = new JButtonPad( Icone.novo( "btTudo.png" ) );

	private final JButtonPad btSelNada = new JButtonPad( Icone.novo( "btNada.png" ) );

	private final JButtonPad btImprime = new JButtonPad( Icone.novo( "btImprime.png" ) );

	private final JButtonPad btVisImp = new JButtonPad( Icone.novo( "btPrevimp.png" ) );

	protected final JLabel lbStatus = new JLabel();

	protected final ListaCampos lcBanco = new ListaCampos( this );

	protected final ListaCampos lcCarteira = new ListaCampos( this );

	public Map<Enum<EPrefs>, Object> prefs = new HashMap<Enum<EPrefs>, Object>();

	protected String where = "";

	protected BigDecimal vlrselecionado = null;

	public FRemFBN( final String tipofebraban ) {

		super( false );
		setTitulo( "Manutenção de contas a receber" );
		setAtribos( 10, 10, 780, 540 );

		this.TIPO_FEBRABAN = tipofebraban;

		montaRadioGrupos();
		montaListaCampos();
		montaTela();

		tab.adicColuna( "Sel." );
		tab.adicColuna( "Razão social do cliente" );
		tab.adicColuna( "Cód.cli." );
		tab.adicColuna( "Doc." );
		tab.adicColuna( "Seq.rec." );
		tab.adicColuna( "Cód.rec." );
		tab.adicColuna( "Nro.Parc." );
		tab.adicColuna( "Valor" );
		tab.adicColuna( "Emissão" );
		tab.adicColuna( "Vencimento" );
		tab.adicColuna( "Agência" );
		tab.adicColuna( "Indentificação" );
		tab.adicColuna( "Sit. rem." );
		tab.adicColuna( "Sit. ret." );
		tab.adicColuna( "Subtipo" );
		tab.adicColuna( "Tp.r.cli." );
		tab.adicColuna( "Pessoa" );
		tab.adicColuna( "C.P.F." );
		tab.adicColuna( "C.N.P.J." );
		tab.adicColuna( "Cart. cob." );
		tab.adicColuna( "Nosso numero" );
		tab.adicColuna( "Desc" );
		tab.adicColuna( "IdentCliBco" );
		tab.adicColuna( "Convênio" );
		tab.adicColuna(  "Desc.Pont." );

		tab.setTamColuna( 20, EColTab.COL_SEL.ordinal() );
		tab.setTamColuna( 150, EColTab.COL_RAZCLI.ordinal() );
		tab.setTamColuna( 70, EColTab.COL_CODCLI.ordinal() );
		tab.setTamColuna( 80, EColTab.COL_DOCREC.ordinal() );
		tab.setTamColuna( 80, EColTab.COL_SEQREC.ordinal() );
		tab.setTamColuna( 70, EColTab.COL_CODREC.ordinal() );
		tab.setTamColuna( 70, EColTab.COL_NRPARC.ordinal() );
		tab.setTamColuna( 70, EColTab.COL_VLRAPAG.ordinal() );
		tab.setTamColuna( 70, EColTab.COL_DTREC.ordinal() );
		tab.setTamColuna( 70, EColTab.COL_DTVENC.ordinal() );
		tab.setTamColuna( 100, EColTab.COL_AGENCIACLI.ordinal() );
		tab.setTamColuna( 100, EColTab.COL_IDENTCLI.ordinal() );
		tab.setTamColuna( 50, EColTab.COL_SITREM.ordinal() );
		tab.setTamColuna( 50, EColTab.COL_SITRET.ordinal() );
		tab.setTamColuna( 30, EColTab.COL_STIPOFEBRABAN.ordinal() );
		tab.setTamColuna( 30, EColTab.COL_TIPOREMCLI.ordinal() );
		tab.setTamColuna( 30, EColTab.COL_PESSOACLI.ordinal() );
		tab.setTamColuna( 80, EColTab.COL_CPFCLI.ordinal() );
		tab.setTamColuna( 80, EColTab.COL_CNPJCLI.ordinal() );
		tab.setTamColuna( 80, EColTab.COL_CARTEIRA.ordinal() );
		tab.setTamColuna( 150, EColTab.NOSSO_NUMERO.ordinal() );
		tab.setTamColuna( 150, EColTab.COL_VLRDESC.ordinal() );
		tab.setTamColuna( 30, EColTab.IDENTCLIBCO.ordinal() );
		tab.setTamColuna( 60, EColTab.CONVCOB.ordinal() );
		tab.setTamColuna( 30, EColTab.DESCPONT.ordinal() );
		
		tab.setColunaEditavel( EColTab.COL_SEL.ordinal(), true );
		
		tab.addMouseListener( this );

		btCarrega.addActionListener( this );
		btSelTudo.addActionListener( this );
		btSelNada.addActionListener( this );
		btExporta.addActionListener( this );
		btImprime.addActionListener( this );
		btVisImp.addActionListener( this );

		rgTipoRemessa.addRadioGroupListener( this );

		btSelTudo.setToolTipText( "Selecionar tudo" );
		btSelNada.setToolTipText( "Limpar seleção" );

		Calendar cal = Calendar.getInstance();
		txtDtFim.setVlrDate( cal.getTime() );
		cal.set( Calendar.MONTH, cal.get( Calendar.MONTH ) - 1 );
		txtDtIni.setVlrDate( cal.getTime() );

	}

	private void montaRadioGrupos() {

		Vector<String> vValsDate = new Vector<String>();

		Vector<String> vLabsDate = new Vector<String>();

		Vector<String> vValsRem = new Vector<String>();

		Vector<String> vLabsRem = new Vector<String>();

		Vector<String> vValsTipo = new Vector<String>();

		Vector<String> vLabsTipo = new Vector<String>();

		vValsDate.addElement( "E" );
		vValsDate.addElement( "V" );
		vLabsDate.addElement( "Emissão" );
		vLabsDate.addElement( "Vencimento" );
		rgData = new JRadioGroup<String, String>( 2, 1, vLabsDate, vValsDate );

		vValsRem.addElement( "00" );
		vValsRem.addElement( "01" );
		vValsRem.addElement( "02" );
		vValsRem.addElement( "99" );
		vLabsRem.addElement( "Não exportados" );
		vLabsRem.addElement( "Exportados" );
		vLabsRem.addElement( "Rejeitados" );
		vLabsRem.addElement( "Todos" );

		rgSitRemessa = new JRadioGroup<String, String>( 2, 2, vLabsRem, vValsRem );

		vValsTipo.addElement( "0" );
		vValsTipo.addElement( "1" );
		vLabsTipo.addElement( "Inclusão" );
		vLabsTipo.addElement( "Exclusão" );
		rgTipoRemessa = new JRadioGroup<String, String>( 2, 1, vLabsTipo, vValsTipo );
	}

	private void montaListaCampos() {

		/***************
		 * FNBANCO *
		 ***************/

		lcBanco.add( new GuardaCampo( txtCodBanco, "CodBanco", "Cód.banco", ListaCampos.DB_PK, true ) );
		lcBanco.add( new GuardaCampo( txtNomeBanco, "NomeBanco", "Nome do Banco", ListaCampos.DB_SI, false ) );
		lcBanco.montaSql( false, "BANCO", "FN" );
		lcBanco.setQueryCommit( false );
		lcBanco.setReadOnly( true );
		txtCodBanco.setNomeCampo( "CodBanco" );
		txtCodBanco.setTabelaExterna( lcBanco, null );
		txtCodBanco.setListaCampos( lcBanco );
		txtCodBanco.setFK( true );
		txtCodBanco.setRequerido( true );
		txtNomeBanco.setListaCampos( lcBanco );

		/***************
		 * CARTEIRA *
		 ***************/

		txtCodCartCob.setNomeCampo( "CodCartCob" );
		lcCarteira.add( new GuardaCampo( txtCodCartCob, "CodCartCob", "Cód.cart.cob", ListaCampos.DB_PK, true ) );
		lcCarteira.add( new GuardaCampo( txtDescCartCob, "DescCartCob", "Desc.Cart.Cob", ListaCampos.DB_SI, false ) );
		lcCarteira.add( new GuardaCampo( txtConvCob, "ConvCob", "Convênio", ListaCampos.DB_SI, false) );
		lcCarteira.setDinWhereAdic( "CODBANCO = #S", txtCodBanco );
		lcCarteira.montaSql( false, "CARTCOB", "FN" );
		lcCarteira.setQueryCommit( false );
		lcCarteira.setReadOnly( true );
		txtCodCartCob.setTabelaExterna( lcCarteira, null );
		txtCodCartCob.setListaCampos( lcCarteira );
		txtDescCartCob.setListaCampos( lcCarteira );
		txtCodCartCob.setFK( true );
	}

	private void montaTela() {

		pnCliente.add( panelRemessa, BorderLayout.CENTER );

		panelRemessa.add( panelFiltros, BorderLayout.NORTH );
		panelRemessa.add( panelTabela, BorderLayout.CENTER );
		panelRemessa.add( panelStatus, BorderLayout.SOUTH );

		JLabel bordaData = new JLabel();
		bordaData.setBorder( BorderFactory.createEtchedBorder() );
		JLabel periodo = new JLabel( "Periodo", SwingConstants.CENTER );
		periodo.setOpaque( true );

		panelFiltros.setPreferredSize( new Dimension( 300, 185 ) );
		panelFiltros.adic( new JLabel( "Cód.banco" ), 7, 10, 90, 20 );
		panelFiltros.adic( txtCodBanco, 7, 30, 90, 20 );
		panelFiltros.adic( new JLabel( "Nome do banco" ), 100, 10, 300, 20 );
		panelFiltros.adic( txtNomeBanco, 100, 30, 318, 20 );
		panelFiltros.adic( new JLabel( "Cód.Carteira" ), 7, 50, 90, 20 );
		panelFiltros.adic( txtCodCartCob, 7, 70, 90, 20 );
		panelFiltros.adic( new JLabel( "Descrição da carteira" ), 100, 50, 300, 20 );
		panelFiltros.adic( txtDescCartCob, 100, 70, 318, 20 );
		panelFiltros.adic( new JLabel( "Convênio" ), 423, 50, 100, 20 );
		panelFiltros.adic( txtConvCob, 423, 70, 100, 20 );

		panelFiltros.adic( periodo, 443, 3, 80, 20 );
		panelFiltros.adic( txtDtIni, 445, 23, 120, 20 );
		panelFiltros.adic( new JLabel( "até", SwingConstants.CENTER ), 565, 23, 50, 20 );
		panelFiltros.adic( txtDtFim, 615, 23, 120, 20 );
		panelFiltros.adic( bordaData, 433, 10, 317, 40 );

		panelFiltros.adic( new JLabel( "Tipo de remessa:" ), 7, 90, 150, 20 );
		panelFiltros.adic( rgTipoRemessa, 7, 110, 150, 70 );
		panelFiltros.adic( new JLabel( "filtro:" ), 170, 90, 250, 20 );
		panelFiltros.adic( rgSitRemessa, 170, 110, 250, 70 );
		panelFiltros.adic( new JLabel( "filtro:" ), 433, 90, 150, 20 );
		panelFiltros.adic( rgData, 433, 110, 150, 70 );

		panelFiltros.adic( btCarrega, 600, 100, 150, 30 );

		panelTabela.add( new JScrollPane( tab ), BorderLayout.CENTER );
		panelTabela.add( panelFuncoes, BorderLayout.EAST );

		panelFuncoes.setPreferredSize( new Dimension( 45, 100 ) );
		panelFuncoes.adic( btSelTudo, 5, 5, 30, 30 );
		panelFuncoes.adic( btSelNada, 5, 40, 30, 30 );

		lbStatus.setForeground( Color.BLUE );
		
		lbValorSelecionado.setForeground( Color.BLUE );

		panelStatus.setPreferredSize( new Dimension( 600, 30 ) );
//		panelStatus.add( lbStatus, BorderLayout.WEST );
		panelStatus.adic( lbStatus, 0, 0, 200, 20 );
		panelStatus.adic( lbValorSelecionado, 303, 0, 200, 20 );


		panelRodape = adicBotaoSair();
		panelRodape.setBorder( BorderFactory.createEtchedBorder() );
		panelRodape.setPreferredSize( new Dimension( 600, 32 ) );
		btExporta.setPreferredSize( new Dimension( 150, 30 ) );
		panelRodape.add( btExporta, BorderLayout.WEST );

		panelRodape.add( panelImp, BorderLayout.CENTER );
		panelImp.add( pnImp, BorderLayout.NORTH );
		pnImp.setPreferredSize( new Dimension( 60, 30 ) );
		pnImp.add( btImprime );
		pnImp.add( btVisImp );

	}

	protected String getMenssagemRet( final String codretorno ) {

		String msg = null;
		StringBuilder sSQL = new StringBuilder();
		PreparedStatement ps = null;

		try {

			sSQL.append( " SELECT DESCRET " );
			sSQL.append( " FROM FNFBNCODRET " );
			sSQL.append( " WHERE CODEMP=? AND CODFILIAL=?  AND CODEMPBO=? " );
			sSQL.append( " AND CODFILIALBO=?  AND CODRET=? AND TIPOFEBRABAN=? " );

			ps = con.prepareStatement( sSQL.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "FNBANCO" ) );
			ps.setString( 5, codretorno );
			ps.setString( 6, TIPO_FEBRABAN );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {

				msg = rs.getString( "DESCRET" );
			}
		} catch ( Exception e ) {
			Funcoes.mensagemInforma( this, "Erro ao montar grid. \n" + e.getMessage() );
			e.printStackTrace();
		}
		return msg;
	}

	protected boolean setPrefs(String convCob) {

		boolean retorno = false;
		Banco banco = FbnUtil.getBanco(txtCodBanco.getVlrString());
		try {

			StringBuilder sql = new StringBuilder();

			sql.append( "SELECT I.CODCONV, P.NOMEEMP, P.NOMEEMPCNAB, I.VERLAYOUT, I.IDENTSERV, I.CONTACOMPR, " );
			sql.append( "I.IDENTAMBCLI, I.IDENTAMBBCO, I.NROSEQ, " );
			sql.append( "I.NUMCONTA, C.AGENCIACONTA, C.POSTOCONTA, E.CNPJFILIAL, " );
			sql.append( "FORCADTIT, TIPODOC, IDENTEMITBOL , IDENTDISTBOL, ESPECTIT, CODJUROS, VLRPERCJUROS, " );
			sql.append( "CODDESC, VLRPERCDESC, CODPROT, DIASPROT, CODBAIXADEV, DIASBAIXADEV, I.MDECOB, I.CONVCOB, " );
			sql.append( " (CASE WHEN I.ACEITE='S' THEN 'A' ELSE 'N' END) ACEITE, I.PADRAOCNAB, P1.TPNOSSONUMERO, P1.IMPDOCBOL, " );
			
			sql.append( "I.CAMINHOREMESSA, I.CAMINHORETORNO, I.BACKUPREMESSA, I.BACKUPRETORNO, I.CODINSTR, I.CODOUTINSTR, ");
			sql.append( "coalesce(I.VLRPERCMULTA,0) VLRPERCMULTA " );
			
			sql.append( "FROM SGPREFERE1 P1, SGPREFERE6 P, SGFILIAL E, " );
			sql.append( "SGITPREFERE6 I LEFT OUTER JOIN FNCONTA C ON " );
			sql.append( "C.CODEMP=I.CODEMPCA AND C.CODFILIAL=I.CODFILIALCA AND C.NUMCONTA=I.NUMCONTA " );
			
			sql.append( "WHERE I.CODEMP=? AND I.CODFILIAL=? " );
			sql.append( "AND I.CODEMPBO=? AND I.CODFILIALBO=? AND I.CODBANCO=? AND I.TIPOFEBRABAN=? " );
			sql.append( "AND P.CODEMP=I.CODEMP AND P.CODFILIAL=I.CODFILIAL " );
			sql.append( "AND E.CODEMP=I.CODEMP AND E.CODFILIAL=I.CODFILIAL " );
			sql.append( "AND P1.CODEMP=E.CODEMP AND P1.CODFILIAL=? " );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGITPREFERE6" ) );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "FNBANCO" ) );
			ps.setString( 5, txtCodBanco.getVlrString() );
			ps.setString( 6, TIPO_FEBRABAN );
			ps.setInt( 7, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				if ( ( convCob==null ) || ( "".equals( convCob.trim() ) ) ) {
					prefs.put( EPrefs.CODCONV, rs.getString( EPrefs.CODCONV.toString() ) );
				} else {
					prefs.put( EPrefs.CODCONV, convCob);
				}
				prefs.put( EPrefs.NOMEEMP, rs.getString( EPrefs.NOMEEMP.toString() ) );
				prefs.put( EPrefs.NOMEEMPCNAB, rs.getString( EPrefs.NOMEEMPCNAB.toString() ) );
				prefs.put( EPrefs.VERLAYOUT, rs.getString( EPrefs.VERLAYOUT.toString() ) );
				prefs.put( EPrefs.CODBANCO, txtCodBanco.getVlrString() );
				prefs.put( EPrefs.NOMEBANCO, txtNomeBanco.getVlrString() );
				prefs.put( EPrefs.IDENTSERV, rs.getString( EPrefs.IDENTSERV.toString() ) );
				prefs.put( EPrefs.CONTACOMPR, rs.getString( EPrefs.CONTACOMPR.toString() ) );
				prefs.put( EPrefs.IDENTAMBCLI, rs.getString( EPrefs.IDENTAMBCLI.toString() ) );
				prefs.put( EPrefs.IDENTAMBBCO, rs.getString( EPrefs.IDENTAMBBCO.toString() ) );
				prefs.put( EPrefs.NROSEQ, new Integer( rs.getInt( EPrefs.NROSEQ.toString() ) ) );

				prefs.put( EPrefs.CAMINHOREMESSA, rs.getString( EPrefs.CAMINHOREMESSA.name() ) );
				prefs.put( EPrefs.CAMINHORETORNO, rs.getString( EPrefs.CAMINHORETORNO.name() ) );
				prefs.put( EPrefs.BACKUPREMESSA, rs.getString( EPrefs.BACKUPREMESSA.name() ) );
				prefs.put( EPrefs.BACKUPRETORNO, rs.getString( EPrefs.BACKUPRETORNO.name() ) );

				if ( rs.getString( "AGENCIACONTA" ) != null ) {
					String[] agencia = banco.getCodSig( rs.getString( "AGENCIACONTA" ) );
					prefs.put( EPrefs.AGENCIA, agencia[ 0 ] );
					prefs.put( EPrefs.DIGAGENCIA, agencia[ 1 ] );
				} else {
					prefs.put( EPrefs.AGENCIA, "" );
					prefs.put( EPrefs.DIGAGENCIA, "" );
				}
				if ( rs.getString( "POSTOCONTA" )!=null ) {
					prefs.put( EPrefs.POSTOCONTA, rs.getString( "POSTOCONTA" ) );
				} else {
					prefs.put( EPrefs.POSTOCONTA, "" );
				}

				if ( rs.getString( EPrefs.NUMCONTA.toString() ) != null ) {
					String[] conta = banco.getCodSig( rs.getString( EPrefs.NUMCONTA.toString() ) );
					prefs.put( EPrefs.NUMCONTA, conta[ 0 ] );
					prefs.put( EPrefs.DIGCONTA, conta[ 1 ] );
				} else {
					prefs.put( EPrefs.NUMCONTA, "" );
					prefs.put( EPrefs.DIGCONTA, "" );
				}

				prefs.put( EPrefs.DIGAGCONTA, null );
				prefs.put( EPrefs.CNPFEMP, rs.getString( "CNPJFILIAL" ) );

				prefs.put( EPrefs.FORCADTIT, rs.getInt( EPrefs.FORCADTIT.toString() ) );
				prefs.put( EPrefs.TIPODOC, rs.getInt( EPrefs.TIPODOC.toString() ) );
				prefs.put( EPrefs.IDENTEMITBOL, rs.getInt( EPrefs.IDENTEMITBOL.toString() ) );
				prefs.put( EPrefs.IDENTDISTBOL, rs.getInt( EPrefs.IDENTDISTBOL.toString() ) );
				prefs.put( EPrefs.ESPECTIT, rs.getInt( EPrefs.ESPECTIT.toString() ) );
				prefs.put( EPrefs.CODJUROS, rs.getInt( EPrefs.CODJUROS.toString() ) );
				prefs.put( EPrefs.VLRPERCJUROS, rs.getBigDecimal( EPrefs.VLRPERCJUROS.toString() ) );
				prefs.put( EPrefs.CODDESC, rs.getInt( EPrefs.CODDESC.toString() ) );
				prefs.put( EPrefs.VLRPERCDESC, rs.getBigDecimal( EPrefs.VLRPERCDESC.toString() ) );
				prefs.put( EPrefs.CODPROT, rs.getInt( EPrefs.CODPROT.toString() ) );
				prefs.put( EPrefs.DIASPROT, rs.getInt( EPrefs.DIASPROT.toString() ) );
				prefs.put( EPrefs.CODBAIXADEV, rs.getInt( EPrefs.CODBAIXADEV.toString() ) );
				prefs.put( EPrefs.DIASBAIXADEV, rs.getInt( EPrefs.DIASBAIXADEV.toString() ) );
				prefs.put( EPrefs.MDECOB, rs.getString( EPrefs.MDECOB.toString() ) );
				if ( ( convCob==null ) || ( "".equals( convCob.trim() ) ) ) {
					prefs.put( EPrefs.CONVCOB, rs.getString( EPrefs.CONVCOB.toString() ) );
				} else {
					prefs.put( EPrefs.CONVCOB, convCob );
				}
				prefs.put( EPrefs.ACEITE, rs.getString( EPrefs.ACEITE.toString() ) );
				prefs.put( EPrefs.PADRAOCNAB, rs.getString( EPrefs.PADRAOCNAB.toString() ) );
				prefs.put( EPrefs.TPNOSSONUMERO, rs.getString( EPrefs.TPNOSSONUMERO.toString() ) );
				prefs.put( EPrefs.IMPDOCBOL, rs.getString( EPrefs.IMPDOCBOL.toString() ) );
				prefs.put( EPrefs.CODINSTR, rs.getInt(  EPrefs.CODINSTR.toString() ) );
				prefs.put( EPrefs.CODOUTINSTR, rs.getInt( EPrefs.CODOUTINSTR.toString() ) );
				prefs.put( EPrefs.VLRPERCMULTA, rs.getBigDecimal( EPrefs.VLRPERCMULTA.toString() ) );

				retorno = true;
			} else {
				retorno = false;
				Funcoes.mensagemInforma( null, "Ajuste os parâmetros antes de executar!" );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( Exception e ) {
			retorno = false;
			Funcoes.mensagemErro( this, "Carregando parâmetros!\n" + e.getMessage() );
			e.printStackTrace();
			lbStatus.setText( "" );
		}

		return retorno;
	}

	protected void calcSelecionado() {

		try {

			vlrselecionado = new BigDecimal( 0 );

			for(int i=0; tab.getNumLinhas() > i; i++) {

				if((Boolean) tab.getValor( i, EColTab.COL_SEL.ordinal() )) {

					String strvalor = ((StringDireita) tab.getValor( i, EColTab.COL_VLRAPAG.ordinal())).toString() ;
					BigDecimal bdvalor = ConversionFunctions.stringToBigDecimal( strvalor );

					vlrselecionado = vlrselecionado.add(bdvalor);
				}

			}

			lbValorSelecionado.setText( Funcoes.bdToStr( vlrselecionado, Aplicativo.casasDecFin ).toString() );
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected ResultSet executeQuery(boolean exportados, String nomearquivo) throws SQLException {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sSQL = new StringBuilder();
		String sDtFiltro = "E".equals( rgData.getVlrString() ) ? "IR.DTITREC" : "IR.DTVENCITREC";

		StringBuilder sWhere = new StringBuilder();

		if( !exportados && nomearquivo==null ) {
			if ( "00".equals( rgSitRemessa.getVlrString() ) ) {
				sWhere.append( "AND ( FR.SITREMESSA IS NULL OR FR.SITREMESSA='00' ) AND ( FR.SITRETORNO IS NULL OR FR.SITRETORNO='00' ) " );
			} 
			else if ( "01".equals( rgSitRemessa.getVlrString() ) ) {
				sWhere.append( "AND ( FR.SITREMESSA='01' ) " );
			} 	
			else if ( "02".equals( rgSitRemessa.getVlrString() ) ) {
				sWhere.append( "AND ( FR.SITRETORNO IS NOT NULL AND FR.SITRETORNO<>'00' ) " );
			}
		}
		else {
			sWhere.append( "AND ( FR.SITREMESSA='01' AND FR.NOMEARQUIVO='"+ nomearquivo +"') " );
		}

		sSQL.append( "SELECT IR.CODREC, IR.NPARCITREC, R.DOCREC, IR.SEQNOSSONUMERO, R.CODCLI, C.RAZCLI, IR.DTITREC, IR.DTVENCITREC," );
		sSQL.append( "IR.VLRAPAGITREC, FC.AGENCIACLI, FC.IDENTCLI, COALESCE(FR.SITREMESSA,'00') SITREMESSA, " );
		sSQL.append( "FR.SITRETORNO, COALESCE(COALESCE(FR.STIPOFEBRABAN,FC.STIPOFEBRABAN),'02') STIPOFEBRABAN, " );
		sSQL.append( "COALESCE(FC.TIPOREMCLI,'B') TIPOREMCLI, C.PESSOACLI, C.CPFCLI, C.CNPJCLI, ir.nossonumero, IR.VLRDESCITREC, C.IDENTCLIBCO " );
		sSQL.append( ", coalesce(ccb.convcob,'') convcob, coalesce(IR.DESCPONT,'N') DESCPONT " );
		sSQL.append( "FROM VDCLIENTE C," );
		sSQL.append( "FNRECEBER R LEFT OUTER JOIN FNFBNCLI FC ON " );
		sSQL.append( "FC.CODEMP=R.CODEMPCL AND FC.CODFILIAL=R.CODFILIALCL AND FC.CODCLI=R.CODCLI " );

		sSQL.append( "and fc.codempbo=r.codempbo and fc.codfilialbo=r.codfilialbo and fc.codbanco=r.codbanco" );

		sSQL.append( ",FNITRECEBER IR LEFT OUTER JOIN FNFBNREC FR ON " );
		sSQL.append( "FR.CODEMP=IR.CODEMP AND FR.CODFILIAL=IR.CODFILIAL AND " );
		sSQL.append( "FR.CODREC=IR.CODREC AND FR.NPARCITREC=IR.NPARCITREC AND " );
		sSQL.append( "FR.CODEMPBO=IR.CODEMPBO AND FR.CODFILIALBO=IR.CODFILIALBO AND FR.CODBANCO=IR.CODBANCO " );
		sSQL.append( " left outer join fncartcob ccb on ");
		sSQL.append( "ccb.codemp=ir.codempcb and ccb.codfilial=ir.codfilialcb and ccb.codcartcob=ir.codcartcob and ccb.codbanco=ir.codbanco ");
		sSQL.append( "WHERE R.CODEMP=IR.CODEMP AND R.CODFILIAL=IR.CODFILIAL AND R.CODREC=IR.CODREC AND " );
		sSQL.append( "C.CODEMP=R.CODEMPCL AND C.CODFILIAL=R.CODFILIALCL AND C.CODCLI=R.CODCLI AND " );

		if ( !"".equals( txtCodCartCob.getVlrString() ) ) {
			sSQL.append( " IR.CODCARTCOB = '" + txtCodCartCob.getVlrString() + "' AND " );
		}

		sSQL.append( sDtFiltro );
		sSQL.append( " BETWEEN ? AND ? AND IR.STATUSITREC IN ('R1','RL','RR') AND " );
		sSQL.append( "IR.CODEMPBO=? AND IR.CODFILIALBO=? AND IR.CODBANCO=? " );
		sSQL.append( sWhere );
		sSQL.append( "ORDER BY C.RAZCLI, R.CODREC, IR.NPARCITREC " );

		System.out.println( "SQL:" + sSQL.toString() );

		ps = con.prepareStatement( sSQL.toString() );
		ps.setDate( 1, Funcoes.dateToSQLDate( txtDtIni.getVlrDate() ) );
		ps.setDate( 2, Funcoes.dateToSQLDate( txtDtFim.getVlrDate() ) );
		ps.setInt( 3, Aplicativo.iCodEmp );
		ps.setInt( 4, ListaCampos.getMasterFilial( "FNITRECEBER" ) );
		ps.setString( 5, txtCodBanco.getVlrString() );

		rs = ps.executeQuery();

		return rs;

	}

	protected void carregaTab() {

		if ( txtCodBanco.getVlrString().trim().length() < 1 ) {
			Funcoes.mensagemErro( this, "O código do banco é obrigatorio!" );
			txtCodBanco.requestFocus();
			return;
		}
		
		if ( txtCodCartCob.getVlrString().trim().length() < 1 ) {
			
			if( ! (Funcoes.mensagemConfirma( this, "O código da carteira de cobrança não foi informado!\nDeseja realizar a pesquisa mesmo assim?" ) == JOptionPane.YES_OPTION) ) {
				txtCodCartCob.requestFocus();
				return;
				
			}
			if ( ! "".equals( txtConvCob.getVlrString().trim() ) ) {
				prefs.put( EPrefs.CONVCOB, txtConvCob.getVlrString() );
			}
			
		}

		try {

			lbStatus.setText( "      carregando tabela ..." );

			tab.limpa();

			ResultSet rs = executeQuery(false, null);

			int i = 0;
			while ( rs.next() ) {

				tab.adicLinha();
				tab.setValor( new Boolean( true ), i, EColTab.COL_SEL.ordinal() );
				tab.setValor( rs.getString( "RAZCLI" ), i, EColTab.COL_RAZCLI.ordinal() );
				tab.setValor( new Integer( rs.getInt( "CODCLI" ) ), i, EColTab.COL_CODCLI.ordinal() );
				tab.setValor( new Integer( rs.getInt( "DOCREC" ) ), i, EColTab.COL_DOCREC.ordinal() );
				tab.setValor( new Integer( rs.getInt( "SEQNOSSONUMERO" ) ), i, EColTab.COL_SEQREC.ordinal() );
				tab.setValor( new Integer( rs.getInt( "CODREC" ) ), i, EColTab.COL_CODREC.ordinal() );
				tab.setValor( new Integer( rs.getInt( "NPARCITREC" ) ), i, EColTab.COL_NRPARC.ordinal() );
				tab.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRAPAGITREC" ) ), i, EColTab.COL_VLRAPAG.ordinal() );
				tab.setValor( rs.getDate( "DTITREC" ), i, EColTab.COL_DTREC.ordinal() );
				tab.setValor( rs.getDate( "DTVENCITREC" ), i, EColTab.COL_DTVENC.ordinal() );
				tab.setValor( rs.getString( "AGENCIACLI" ), i, EColTab.COL_AGENCIACLI.ordinal() );
				tab.setValor( rs.getString( "IDENTCLI" ), i, EColTab.COL_IDENTCLI.ordinal() );
				tab.setValor( rs.getString( "SITREMESSA" ), i, EColTab.COL_SITREM.ordinal() );
				tab.setValor( rs.getString( "SITRETORNO" ), i, EColTab.COL_SITRET.ordinal() );
				tab.setValor( rs.getString( "STIPOFEBRABAN" ), i, EColTab.COL_STIPOFEBRABAN.ordinal() );
				tab.setValor( rs.getString( "TIPOREMCLI" ), i, EColTab.COL_TIPOREMCLI.ordinal() );
				tab.setValor( rs.getString( "PESSOACLI" ), i, EColTab.COL_PESSOACLI.ordinal() );
				tab.setValor( rs.getString( "CPFCLI" ), i, EColTab.COL_CPFCLI.ordinal() );
				tab.setValor( rs.getString( "CNPJCLI" ), i, EColTab.COL_CNPJCLI.ordinal() );
				
				tab.setValor( rs.getString( "nossonumero" ), i, EColTab.NOSSO_NUMERO.ordinal() );
				tab.setValor( Funcoes.bdToStr( rs.getBigDecimal( "VLRDESCITREC" ) ), i, EColTab.COL_VLRDESC.ordinal() );
				tab.setValor( rs.getString( "IDENTCLIBCO" ), i, EColTab.IDENTCLIBCO.ordinal() );
				tab.setValor( rs.getString( "convcob" ), i, EColTab.CONVCOB.ordinal() );
				tab.setValor( rs.getString( "DESCPONT" ), i, EColTab.DESCPONT.ordinal() );
				
				i++;
			}

			rs.close();

			con.commit();

			if ( i > 0 ) {
				lbStatus.setText( "     tabela carregada com " + i + " itens..." );
			} else {
				lbStatus.setText( "" );
			}

			if ( "2".equals( rgTipoRemessa.getVlrString() ) ) {
				selecionaNada();
			}
			
			calcSelecionado();

		} catch ( Exception e ) {
			Funcoes.mensagemErro( this, "Erro ao busca dados!\n" + e.getMessage() );
			e.printStackTrace();
			lbStatus.setText( "" );
		} finally {
			System.gc();
		}

	}

	private void selecionaTudo() {
		for ( int i = 0; i < tab.getNumLinhas(); i++ ) {
			tab.setValor( new Boolean( true ), i, 0 );
		}
	}

	private void selecionaNada() {
		for ( int i = 0; i < tab.getNumLinhas(); i++ ) {
			tab.setValor( new Boolean( false ), i, 0 );
		}
	}

	protected boolean consisteExporta( HashSet<FbnUtil.StuffCli> hsCli, HashSet<FbnUtil.StuffRec> hsRec, boolean completartabela, String filename ) {

		boolean retorno = true;
		String convCob = null;
		Vector<?> vLinha = null;

		for ( int i = 0; i < tab.getNumLinhas(); i++ ) {

			vLinha = tab.getLinha( i );

			if ( (Boolean) vLinha.elementAt( EColTab.COL_SEL.ordinal() ) ) {
				if ( completartabela ) {
					if ( "".equals( (String) vLinha.elementAt( EColTab.COL_AGENCIACLI.ordinal() ) ) || 
							"".equals( (String) vLinha.elementAt( EColTab.COL_IDENTCLI.ordinal() ) ) ) {
						
						if ( !completaTabela( i, (Integer) vLinha.elementAt( EColTab.COL_CODCLI.ordinal() ), (String) vLinha.elementAt( EColTab.COL_RAZCLI.ordinal() ), 
								(String) vLinha.elementAt( EColTab.COL_AGENCIACLI.ordinal() ), (String) vLinha.elementAt( EColTab.COL_IDENTCLI.ordinal() ),
								(String) vLinha.elementAt( EColTab.COL_STIPOFEBRABAN.ordinal() ) ) ) {
							retorno = false;
							break;
						}
					}
				}
				
				if (convCob==null) {
					convCob = (String) vLinha.elementAt( EColTab.CONVCOB.ordinal());
				} else if (! convCob.equals( vLinha.elementAt( EColTab.CONVCOB.ordinal()) )) {
					Funcoes.mensagemInforma( this, "Registros de convênios diferentes não podem ser enviados no mesmo arquivo !" );
					retorno = false;
					break;
				}
				String[] stufCliArgs = new String[] { 
						txtCodBanco.getVlrString(), TIPO_FEBRABAN, (String) vLinha.elementAt( EColTab.COL_STIPOFEBRABAN.ordinal() ),
						(String) vLinha.elementAt( EColTab.COL_AGENCIACLI.ordinal() ), (String) vLinha.elementAt( EColTab.COL_IDENTCLI.ordinal() ), 
						(String) vLinha.elementAt( EColTab.COL_TIPOREMCLI.ordinal() ) };
				
				hsCli.add( new FbnUtil().new StuffCli( (Integer) vLinha.elementAt( EColTab.COL_CODCLI.ordinal() ),  stufCliArgs) );
				
				String[] stufRecArgs = new String[] { txtCodBanco.getVlrString(), TIPO_FEBRABAN, (String) vLinha.elementAt( EColTab.COL_STIPOFEBRABAN.ordinal() ), 
						(String) vLinha.elementAt( EColTab.COL_SITREM.ordinal() ), String.valueOf( (Integer) vLinha.elementAt( EColTab.COL_CODCLI.ordinal() ) ),
						(String) vLinha.elementAt( EColTab.COL_AGENCIACLI.ordinal() ), (String) vLinha.elementAt( EColTab.COL_IDENTCLI.ordinal() ), 
						Funcoes.dataAAAAMMDD( (Date) vLinha.elementAt( EColTab.COL_DTVENC.ordinal() ) ),
						ConversionFunctions.stringToBigDecimal( vLinha.elementAt( EColTab.COL_VLRAPAG.ordinal() ) ).toString(), 
						(String) vLinha.elementAt( EColTab.COL_PESSOACLI.ordinal() ), (String) vLinha.elementAt( EColTab.COL_CPFCLI.ordinal() ),
						(String) vLinha.elementAt( EColTab.COL_CNPJCLI.ordinal() ), rgTipoRemessa.getVlrString(), 
						String.valueOf( (Integer) vLinha.elementAt( EColTab.COL_DOCREC.ordinal() ) ), 
						Funcoes.dataAAAAMMDD( (Date) vLinha.elementAt( EColTab.COL_DTREC.ordinal() ) ),
						String.valueOf( vLinha.elementAt( EColTab.COL_NRPARC.ordinal() ) ),
						String.valueOf( vLinha.elementAt( EColTab.COL_SEQREC.ordinal() ) ), 
						ConversionFunctions.stringToBigDecimal( vLinha.elementAt( EColTab.COL_VLRDESC.ordinal() ) ).toString(), 
						(String) vLinha.elementAt( EColTab.IDENTCLIBCO.ordinal() ), 
						(String) vLinha.elementAt( EColTab.DESCPONT.ordinal() )};
				
				hsRec.add( new FbnUtil().new StuffRec(
						/* 0 */(Integer) vLinha.elementAt( EColTab.COL_CODREC.ordinal() ),
						/* 1 */(Integer) vLinha.elementAt( EColTab.COL_NRPARC.ordinal() ),
						/* 2 */stufRecArgs, 
						/* 3 */(String) vLinha.elementAt( EColTab.NOSSO_NUMERO.ordinal() ),
						/* 4 */(Date) vLinha.elementAt( EColTab.COL_DTREC.ordinal() )) );
			}
		}
		if ( retorno ) {
			retorno = persisteDados( hsCli, hsRec, filename );
		}

		return retorno;
	}

	protected boolean completaTabela( final int linha, final Integer codCli, final String razCli, final String agenciaCli, final String identCli, final String subTipo ) {

		boolean retorno = true;

		Object[] valores = DLIdentCli.execIdentCli( this, codCli, razCli, agenciaCli, identCli, subTipo );
		retorno = ( (Boolean) valores[ 0 ] ).booleanValue();

		if ( retorno ) {
			ajustaClientes( codCli, (String) valores[ 1 ], (String) valores[ 2 ], (String) valores[ 3 ] );
		} 
		else {
			tab.setValor( false, linha, EColTab.COL_SEL.ordinal() );
		}

		return retorno;
	}

	protected void ajustaClientes( final Integer codCli, final String agenciaCli, final String identCli, final String subTipo ) {

		for ( int i = 0; i < tab.getNumLinhas(); i++ ) {
			if ( ( (Boolean) tab.getValor( i, EColTab.COL_SEL.ordinal() ) ).booleanValue() && 
					codCli.equals( (Integer) tab.getValor( i, EColTab.COL_CODCLI.ordinal() ) ) ) {
				tab.setValor( agenciaCli, i, EColTab.COL_AGENCIACLI.ordinal() );
				tab.setValor( identCli, i, EColTab.COL_IDENTCLI.ordinal() );
				tab.setValor( subTipo, i, EColTab.COL_STIPOFEBRABAN.ordinal() );
			}
		}
	}
	
	abstract protected void ajustaNossoNumero();

	protected boolean persisteDados( final HashSet<FbnUtil.StuffCli> hsCli, final HashSet<FbnUtil.StuffRec> hsRec, String filename ) {

		boolean retorno = true;
		for ( FbnUtil.StuffCli stfCli : hsCli ) {
			retorno = updateCliente( stfCli.getCodigo(), stfCli.getArgs()[ FbnUtil.EColcli.CODBANCO.ordinal() ], stfCli.getArgs()[ FbnUtil.EColcli.TIPOFEBRABAN.ordinal() ], 
					stfCli.getArgs()[ FbnUtil.EColcli.STIPOFEBRABAN.ordinal() ], stfCli.getArgs()[ FbnUtil.EColcli.AGENCIACLI.ordinal() ], 
					stfCli .getArgs()[ FbnUtil.EColcli.IDENTCLI.ordinal() ], stfCli.getArgs()[ FbnUtil.EColcli.TIPOREMCLI.ordinal() ] );
			if ( !retorno ) {
				retorno = false;
				break;
			}
		}
		if ( retorno ) {
			for ( FbnUtil.StuffRec stfRec : hsRec ) {
				retorno = updateReceber( stfRec.getCodrec(), stfRec.getNParcitrec(), stfRec.getArgs()[ FbnUtil.EColrec.CODBANCO.ordinal() ], 
						stfRec.getArgs()[ FbnUtil.EColrec.TIPOFEBRABAN.ordinal() ], stfRec.getArgs()[ FbnUtil.EColrec.STIPOFEBRABAN.ordinal() ],
						stfRec.getArgs()[ FbnUtil.EColrec.SITREMESSA.ordinal() ], filename );
				if ( !retorno ) {
					retorno = false;
					break;
				}
			}
		}
		return retorno;
	}

	protected boolean updateCliente( int codCli, String codBanco, String tipoFebraban, String stipoFebraban, String agenciaCli, String identCli, String tipoRemCli ) {

		boolean retorno = false;

		try {

			StringBuilder sql = new StringBuilder();
			sql.append( "SELECT AGENCIACLI, IDENTCLI, STIPOFEBRABAN, TIPOREMCLI FROM FNFBNCLI " );
			sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODCLI=? " );
			sql.append( "AND CODEMPPF=? AND CODFILIALPF=? AND CODEMPBO=? AND CODFILIALBO=? AND CODBANCO=? AND TIPOFEBRABAN=?" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
			ps.setInt( 3, codCli );
			ps.setInt( 4, Aplicativo.iCodEmp );
			ps.setInt( 5, ListaCampos.getMasterFilial( "SGITPREFERE6" ) );
			ps.setInt( 6, Aplicativo.iCodEmp );
			ps.setInt( 7, ListaCampos.getMasterFilial( "FNBANCO" ) );
			ps.setString( 8, codBanco );
			ps.setString( 9, tipoFebraban );

			ResultSet rs = ps.executeQuery();
			if ( rs.next() ) {

				if (  !agenciaCli.equals( rs.getString( "AGENCIACLI" ) )  ||  !identCli.equals( rs.getString( "IDENTCLI" ) )  || 
						 !stipoFebraban.equals( rs.getString( "STIPOFEBRABAN" ) )  ||  !tipoRemCli.equals( rs.getString( "TIPOREMCLI" ) ) )  {

					StringBuilder sqlup = new StringBuilder();
					sqlup.append( "UPDATE FNFBNCLI SET AGENCIACLI=?, IDENTCLI=?, STIPOFEBRABAN=?, TIPOREMCLI=? " );
					sqlup.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODCLI=? " );
					sqlup.append( "AND CODEMPPF=? AND CODFILIALPF=? AND CODEMPBO=? AND CODFILIALBO=? AND CODBANCO=? AND TIPOFEBRABAN=?" );

					ps = con.prepareStatement( sqlup.toString() );
					ps.setString( 1, agenciaCli );
					ps.setString( 2, identCli );
					ps.setString( 3, stipoFebraban );
					ps.setString( 4, tipoRemCli );
					ps.setInt( 5, Aplicativo.iCodEmp );
					ps.setInt( 6, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
					ps.setInt( 7, codCli );
					ps.setInt( 8, Aplicativo.iCodEmp );
					ps.setInt( 9, ListaCampos.getMasterFilial( "SGITPREFERE6" ) );
					ps.setInt( 10, Aplicativo.iCodEmp );
					ps.setInt( 11, ListaCampos.getMasterFilial( "FNBANCO" ) );
					ps.setString( 12, codBanco );
					ps.setString( 13, tipoFebraban );
					ps.executeUpdate();
				}
			} else {

				StringBuilder sqlin = new StringBuilder();
				sqlin.append( "INSERT INTO FNFBNCLI (AGENCIACLI, IDENTCLI, CODEMP, CODFILIAL, " );
				sqlin.append( "CODCLI, CODEMPPF, CODFILIALPF, CODEMPBO, CODFILIALBO, CODBANCO, " );
				sqlin.append( "TIPOFEBRABAN, STIPOFEBRABAN, TIPOREMCLI) " );
				sqlin.append( "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)" );

				ps = con.prepareStatement( sqlin.toString() );
				ps.setString( 1, agenciaCli );
				ps.setString( 2, identCli );
				ps.setInt( 3, Aplicativo.iCodEmp );
				ps.setInt( 4, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
				ps.setInt( 5, codCli );
				ps.setInt( 6, Aplicativo.iCodEmp );
				ps.setInt( 7, ListaCampos.getMasterFilial( "SGITPREFERE6" ) );
				ps.setInt( 8, Aplicativo.iCodEmp );
				ps.setInt( 9, ListaCampos.getMasterFilial( "FNBANCO" ) );
				ps.setString( 10, codBanco );
				ps.setString( 11, tipoFebraban );
				ps.setString( 12, stipoFebraban );
				ps.setString( 13, tipoRemCli );

				ps.executeUpdate();
			}
			con.commit();

			retorno = true;

		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro atualizando cliente!\n" + e.getMessage() );
			e.printStackTrace();
		}

		return retorno;
	}

	protected boolean updateReceber( int codRec, int nParcitrec, String codBanco, String tipoFebraban, String stipoFebraban, String sitRemessa, String filename ) {

		boolean retorno = false;

		try {

			StringBuilder sql = new StringBuilder();
			sql.append( "SELECT CODBANCO, TIPOFEBRABAN, STIPOFEBRABAN, SITREMESSA, NOMEARQUIVO " );
			sql.append( "FROM FNFBNREC WHERE CODEMP=? AND CODFILIAL=? AND CODREC=? AND NPARCITREC=?" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNITRECEBER" ) );
			ps.setInt( 3, codRec );
			ps.setInt( 4, nParcitrec );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {

				if ( 	   !codBanco.equals( rs.getString( "CODBANCO" ) ) 
						|| !tipoFebraban.equals( rs.getString( "TIPOFEBRABAN" ) ) 
						|| !stipoFebraban.equals( rs.getString( "STIPOFEBRABAN" ) ) 
						|| !sitRemessa.equals( rs.getString( "SITREMESSA" )	)
						|| filename==null
						|| !filename.equals( rs.getString( "NOMEARQUIVO" )	)
						
				) {

					StringBuilder sqlup = new StringBuilder();
					sqlup.append( "UPDATE FNFBNREC SET CODBANCO=?, TIPOFEBRABAN=?, STIPOFEBRABAN=?, SITREMESSA=?, NOMEARQUIVO=? " );
					sqlup.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODREC=? AND NPARCITREC=?" );

					ps = con.prepareStatement( sqlup.toString() );
					ps.setString( 1, codBanco );
					ps.setString( 2, tipoFebraban );
					ps.setString( 3, stipoFebraban );
					ps.setString( 4, sitRemessa );
					ps.setString( 5, filename );
					
					ps.setInt( 6, Aplicativo.iCodEmp );
					ps.setInt( 7, ListaCampos.getMasterFilial( "FNITRECEBER" ) );
					ps.setInt( 8, codRec );
					ps.setInt( 9, nParcitrec );
					
					ps.executeUpdate();
				}
			} else {

				StringBuilder sqlin = new StringBuilder();
				sqlin.append( "INSERT INTO FNFBNREC (CODEMP, CODFILIAL, CODREC, NPARCITREC, " );
				sqlin.append( "CODEMPPF, CODFILIALPF, CODEMPBO, CODFILIALBO, CODBANCO, " );
				sqlin.append( "TIPOFEBRABAN, STIPOFEBRABAN, SITREMESSA, NOMEARQUIVO ) " );
				sqlin.append( "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )" );

				ps = con.prepareStatement( sqlin.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "FNFBNREC" ) );
				ps.setInt( 3, codRec );
				ps.setInt( 4, nParcitrec );
				ps.setInt( 5, Aplicativo.iCodEmp );
				ps.setInt( 6, ListaCampos.getMasterFilial( "SGITPREFERE6" ) );
				ps.setInt( 7, Aplicativo.iCodEmp );
				ps.setInt( 8, ListaCampos.getMasterFilial( "FNBANCO" ) );
				ps.setString( 9, codBanco );
				ps.setString( 10, tipoFebraban );
				ps.setString( 11, stipoFebraban );
				ps.setString( 12, sitRemessa );
				ps.setString( 13, filename );
				ps.executeUpdate();
			}

			con.commit();

			retorno = true;

		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro atualizando situação do contas a receber!\n" + e.getMessage() );
		}

		return retorno;
	}
	
	protected boolean updatePrefere() {

		boolean retorno = true;

		try {
			StringBuilder sql = new StringBuilder();
			sql.append( "UPDATE SGITPREFERE6 I SET NROSEQ=? " );
			sql.append( "WHERE I.CODEMP=? AND I.CODFILIAL=? " );
			sql.append( "AND I.CODEMPBO=? AND I.CODFILIALBO=? AND I.CODBANCO=? AND I.TIPOFEBRABAN=?" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, (Integer) prefs.get( FbnUtil.EPrefs.NROSEQ ) );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "SGITPREFERE6" ) );
			ps.setInt( 4, Aplicativo.iCodEmp );
			ps.setInt( 5, ListaCampos.getMasterFilial( "FNBANCO" ) );
			ps.setString( 6, (String) prefs.get( FbnUtil.EPrefs.CODBANCO ) );
			ps.setString( 7, TIPO_FEBRABAN );
			ps.executeUpdate();
			ps.close();
			con.commit();
		} catch ( SQLException e ) {
			retorno = false;
			Funcoes.mensagemErro( this, "Erro atualizando parâmetros!\n" + e.getMessage() );
		}

		return retorno;
	}
	
	abstract protected boolean execExporta(String convCob);

	abstract public void imprimir( TYPE_PRINT bVisualizar );

	public void valorAlterado( RadioGroupEvent e ) {

		if ( "1".equals( rgTipoRemessa.getVlrString() ) ) {
			selecionaTudo();
		}
		else if ( "2".equals( rgTipoRemessa.getVlrString() ) ) {
			selecionaNada();
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btCarrega ) {
			carregaTab();
		}
		else if ( evt.getSource() == btSelTudo ) {
			selecionaTudo();
			calcSelecionado();
		}
		else if ( evt.getSource() == btSelNada ) {
			selecionaNada();
			calcSelecionado();
		}
		else if ( evt.getSource() == btExporta ) {
			execExporta(txtConvCob.getVlrString());
		}
		else if ( evt.getSource() == btVisImp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
	}

	public void mouseClicked( MouseEvent e ) {

	}

	public void mouseEntered( MouseEvent e ) {

	}

	public void mouseExited( MouseEvent e ) {

	}

	public void mousePressed( MouseEvent e ) {

	}

	public void mouseReleased( MouseEvent e ) {

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcBanco.setConexao( cn );
		lcCarteira.setConexao( cn );
	}

	enum EColTab {

		COL_SEL, COL_RAZCLI, COL_CODCLI, COL_DOCREC, COL_SEQREC, COL_CODREC, COL_NRPARC, COL_VLRAPAG, COL_DTREC, COL_DTVENC, 
		COL_AGENCIACLI, COL_IDENTCLI, COL_SITREM, COL_SITRET, COL_STIPOFEBRABAN, COL_TIPOREMCLI, COL_PESSOACLI, COL_CPFCLI, 
		COL_CNPJCLI, COL_CARTEIRA, NOSSO_NUMERO, COL_VLRDESC, IDENTCLIBCO, CONVCOB, DESCPONT;
	}

}
