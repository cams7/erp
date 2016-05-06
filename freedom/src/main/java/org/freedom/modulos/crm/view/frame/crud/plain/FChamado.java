/**
 * @version 16/50/2007 <BR>
 * @author Setpoint Informática Ltda./Alex Rodrigues <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FHistPad.java <BR>
 * 
 *                   Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                   modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                   na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                   Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                   sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                   Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                   Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                   de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                   Tela para cadastro de historicos padrão.
 * 
 */

package org.freedom.modulos.crm.view.frame.crud.plain;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.JComboBoxEvent;
import org.freedom.acao.JComboBoxListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.acao.Processo;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.component.ProcessoSec;
import org.freedom.library.business.object.EmailBean;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.DLLoading;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.swing.util.SwingParams;
import org.freedom.modulos.atd.view.frame.crud.tabbed.FAtendente;
import org.freedom.modulos.crm.business.object.Chamado;
import org.freedom.modulos.crm.business.object.Prioridade;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCliente;

public class FChamado extends FDados implements ActionListener, JComboBoxListener, InsertListener, CarregaListener, PostListener {

	private static final long serialVersionUID = 1L;

	private static final int notifica_tecnico = 0;
	
	private static final int notifica_cliente = 1;
	
	private static final int notifica_atendente = 2;
	
	private JTextFieldPad txtCodChamado = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDescChamado = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtNomeCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldPad txtEmailCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtContCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtSolicitante = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldPad txtEmailSolicitante = new JTextFieldPad( JTextFieldPad.TP_STRING, 60, 0 );
	
	private JTextFieldPad txtCodContr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtDescContr = new JTextFieldFK( JTextFieldFK.TP_STRING, 50, 0 );
	
	private JTextFieldPad txtCodItContr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtDescItContr = new JTextFieldFK( JTextFieldFK.TP_STRING, 50, 0 );

	private JTextFieldPad txtDtChamado = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDtPrevisao = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDtConclusao = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtQtdHorasPrev = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 4, Aplicativo.casasDec );

	private JTextFieldPad txtCodTpChamado = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );

	private JTextFieldPad txtCodAtend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtIdUsu = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtTicket = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );
	
	private JTextFieldPad txtCodQualific = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );
	
	private JTextFieldFK txtDescQualific = new JTextFieldFK( JTextFieldPad.TP_STRING, 100, 0 );

	private JTextFieldPad txtCodItRecMerc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodItOS = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeAtend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtEmailTecnico = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );
	
	private JTextFieldFK txtEmailAtendente = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private final JTextAreaPad txaDetChamado = new JTextAreaPad( 1000 );
	
	private final JTextAreaPad txaAmbiente = new JTextAreaPad( 1000 );
	
	private final JTextAreaPad txaFatoGerador = new JTextAreaPad( 1000 );

	private final JTextAreaPad txaObsChamado = new JTextAreaPad( 1000 );

	private final JScrollPane spnDetChamado = new JScrollPane( txaDetChamado );
	
	private final JScrollPane spnAmbiente = new JScrollPane( txaAmbiente );
	
	private final JScrollPane spnFatoGerador = new JScrollPane( txaFatoGerador );

	private final JScrollPane spnObsChamado = new JScrollPane( txaObsChamado );

	private JPanelPad panelGeral = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad panelCabecalho = new JPanelPad( 700, 260 );

	private final JPanelPad pinCab = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad panelDetalhamento = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 4, 1 ) );

	private JPanelPad panelTxa = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 2, 1 ) );

	private JComboBoxPad cbTpChamado = new JComboBoxPad( null, null, JComboBoxPad.TP_INTEGER, 4, 0 );

	private JComboBoxPad cbStatus = new JComboBoxPad( null, null, JComboBoxPad.TP_STRING, 2, 0 );
	
	private JComboBoxPad cbPrioridade = new JComboBoxPad( null, null, JComboBoxPad.TP_INTEGER, 4, 0 );
	
	private JCheckBoxPad cbNotificaTecnico = new JCheckBoxPad( "Técnico", "S", "N" );
	
	private JCheckBoxPad cbNotificaCliente = new JCheckBoxPad( "Cliente", "S", "N" );
	
	private JCheckBoxPad cbNotificaAtendente = new JCheckBoxPad( "Atendente", "S", "N" );
	
	private JTextFieldPad txtUsuIns = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );
	
	private JPanelPad pnNotificacoes = new JPanelPad();

	private JLabelPad lbBanner = new JLabelPad( Icone.novo( "bannerChamado.png" ) );

	private ListaCampos lcCli = new ListaCampos( this, "CL" );
	
	private ListaCampos lcQualificacao = new ListaCampos( this, "QL" );

	private ListaCampos lcTipoChamado = new ListaCampos( this, "TC" );
	
	private ListaCampos lcUsu = new ListaCampos( this );

	private ListaCampos lcAtend = new ListaCampos( this, "AE" );

	private ListaCampos lcItRecMercItOS = new ListaCampos( this, "OS" );
	
	private ListaCampos lcContrato = new ListaCampos( this, "CT" );
	
	private ListaCampos lcItContrato = new ListaCampos( this, "CT" );

	private FDados telanterior = null;

	public FChamado() {

		super();

		nav.setNavigation( true );

		setTitulo( "Cadastro de chamados" );
		setAtribos( 50, 50, 640, 800 );
		montaListaCampos();
		montaCombos();
		montaTela();

		cbTpChamado.addComboBoxListener( this );
		cbStatus.addComboBoxListener( this );

		lcCampos.addInsertListener( this );
		lcCli.addCarregaListener( this );

	}

	private void montaTela() {

		pinCab.add( lbBanner, BorderLayout.NORTH );
		pinCab.add( panelGeral, BorderLayout.CENTER );
		panelGeral.add( panelCabecalho, BorderLayout.NORTH );

		setPainel( panelCabecalho );

		adicCampo( txtCodChamado		, 7		, 20	, 80	, 20, "CodChamado", "Cód.Chamado", ListaCampos.DB_PK, true );
		adicCampo( txtDescChamado		, 90	, 20	, 340	, 20, "DescChamado", "Descrição do chamado", ListaCampos.DB_SI, true );

		adicCampoInvisivel( txtCodTpChamado, "CodTpChamado", "", ListaCampos.DB_FK, false );

		adicDB( cbTpChamado				, 433	, 20	, 190	, 20, "CodTpChamado", "Tipo de chamado", false );

		adicCampo( txtCodCli			, 7		, 60	, 80	, 20, "CodCli", "Cód.Cli.", ListaCampos.DB_FK, txtRazCli, true );
		adicDescFK( txtRazCli			, 90	, 60	, 280	, 20, "RazCli", "Razão social do cliente" );
		adicCampo( txtSolicitante		, 373	, 60	, 120	, 20, "Solicitante", "Solicitante", ListaCampos.DB_SI, true );
		adicCampo( txtEmailSolicitante	, 496	, 60	, 126	, 20, "EmailSolicitante", "Email", ListaCampos.DB_SI, false );
		
		adicCampo( txtCodContr			, 7		, 100	, 80	, 20, "CodContr", "Cód.Contr.", ListaCampos.DB_FK, txtDescContr, false );
		adicDescFK( txtDescContr		, 90	, 100	, 254	, 20, "DescContr", "Descrição do Contrato" );
		
		adicCampo( txtCodItContr		, 349	, 100	, 80	, 20, "CodItContr", "Cód.It.Contr.", ListaCampos.DB_FK, txtDescItContr, false );
		adicDescFK( txtDescItContr		, 432	, 100	, 190	, 20, "DescItContr", "Descrição do item  do contr." );

		adicCampo( txtDtChamado			, 7		, 140	, 80	, 20, "DtChamado", "Dt.Abertura", ListaCampos.DB_SI, true );
		adicCampo( txtDtPrevisao		, 90	, 140	, 80	, 20, "DtPrevisao", "Dt.Previsão", ListaCampos.DB_SI, true );
		adicCampo( txtQtdHorasPrev		, 173	, 140	, 60	, 20, "QtdHorasPrevisao", "Qtd.Prev.", ListaCampos.DB_SI, true );

		adicDB( cbPrioridade			, 236	, 140	, 110	, 20, "prioridade", "Prioridade", false );

		adicCampo( txtDtConclusao		, 349	, 140	, 80	, 20, "DtConclusao", "Dt.Conclusão", ListaCampos.DB_SI, false );

		adicDB( cbStatus				, 433	, 140	, 190	, 20, "Status", "Status", false );

		adicCampo( txtCodAtend			, 7		, 180	, 80	, 20, "CodAtend", "Cód.Atend.", ListaCampos.DB_FK, txtNomeAtend, false );
		adicDescFK( txtNomeAtend		, 90	, 180	, 256	, 20, "NomeAtend", "Nome do atendente designado" );

		adicCampo( txtCodQualific		, 349	, 180	, 80	, 20, "CodQualific", "Cód.Qualific.", ListaCampos.DB_FK, txtDescQualific, false );
		adicDescFK( txtDescQualific		, 432	, 180	, 190	, 20, "DescQualific", "Descrição da qualificação" );
		
		txtDtConclusao.setEditable( false );

		adicDBLiv( txaDetChamado, "DetChamado", "Detalhamamento", false );
		adicDBLiv( txaObsChamado, "ObsChamado", "Observações", false );
		adicDBLiv( txaAmbiente, "Ambiente", "Ambiente", false );
		adicDBLiv( txaFatoGerador, "FatoGerador", "Fato gerador", false );

		//		adicCampo( txtTicket, 432, 140, 80, 20, "Ticket", "Ticket", ListaCampos.DB_FK, false );
		//		adicCampo( txtCodItRecMerc, 515, 140, 80, 20, "CodItRecMerc", "Cod.It.Rec.Merc.", ListaCampos.DB_FK, false );
		//		adicCampo( txtCodItOS, 598, 140, 80, 20, "CodItOS", "Cod.It.OS", ListaCampos.DB_FK, false );

		adicCampoInvisivel( txtTicket, "Ticket", "Ticket", ListaCampos.DB_FK, false );
		adicCampoInvisivel( txtCodItRecMerc, "CodItRecMerc", "Cod.It.Rec.Merc.", ListaCampos.DB_FK, false );
		adicCampoInvisivel( txtCodItOS, "CodItOS", "Cod.It.OS", ListaCampos.DB_FK, false );
		txtUsuIns.setSoLeitura( true );
		adicCampoInvisivel( txtUsuIns, "idusuins", "IdUsuIns", ListaCampos.DB_SI, false );
		
		setListaCampos( true, "CHAMADO", "CR" );

		panelGeral.add( panelDetalhamento, BorderLayout.CENTER );
		panelDetalhamento.add( spnDetChamado );
		panelDetalhamento.add( spnFatoGerador );
		panelDetalhamento.add( spnAmbiente );
		panelDetalhamento.add( spnObsChamado );

		spnDetChamado.setBorder( BorderFactory.createTitledBorder( "Detalhamento" ) );
		spnFatoGerador.setBorder( BorderFactory.createTitledBorder( "Fato gerador" ) );
		spnAmbiente.setBorder( BorderFactory.createTitledBorder( "Ambiente" ) );
		spnObsChamado.setBorder( BorderFactory.createTitledBorder( "Observações" ) );

		this.add( pinCab );
		
		pnNotificacoes.setBorder( SwingParams.getPanelLabel( "Notificações", Color.RED ) );
		panelCabecalho.adic( pnNotificacoes, 5, 204, 344, 50 ); 
		
		pnNotificacoes.adic( cbNotificaTecnico	, 5		, 0	, 100	, 20 );
		pnNotificacoes.adic( cbNotificaCliente	, 115	, 0	, 100	, 20 );
		pnNotificacoes.adic( cbNotificaAtendente, 215	, 0	, 100	, 20 );
		
		
	}

	private void montaListaCampos() {

		// FK Cliente
		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtNomeCli, "NomeCli", "Nome do cliente", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtContCli, "ContCli", "Contato", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtEmailCli, "EmailCli", "Email", ListaCampos.DB_SI, false ) );

		lcCli.montaSql( false, "CLIENTE", "VD" );
		lcCli.setQueryCommit( false );
		lcCli.setReadOnly( true );
		txtCodCli.setTabelaExterna( lcCli, FCliente.class.getCanonicalName() );

		// FK Tipo de chamado
		lcTipoChamado.add( new GuardaCampo( txtCodTpChamado, "CodTpChamado", "Cód.Tp.Chamado", ListaCampos.DB_PK, false ) );

		lcTipoChamado.montaSql( false, "TIPOCHAMADO", "CR" );
		lcTipoChamado.setQueryCommit( false );
		lcTipoChamado.setReadOnly( true );
		txtCodTpChamado.setTabelaExterna( lcTipoChamado, FTipoChamado.class.getCanonicalName() );
		txtCodTpChamado.setFK( true );

		// FK para Atendente/Técnico designado
		txtCodAtend.setTabelaExterna( lcAtend, FAtendente.class.getCanonicalName() );
		txtCodAtend.setFK( true );
		txtCodAtend.setNomeCampo( "CodAtend" );
		lcAtend.add( new GuardaCampo( txtCodAtend, "CodAtend", "Cód.atend.", ListaCampos.DB_PK, false ) );
		lcAtend.add( new GuardaCampo( txtNomeAtend, "NomeAtend", "Nome", ListaCampos.DB_SI, false ));
		lcAtend.add( new GuardaCampo( txtEmailTecnico, "EmailAtend", "Email", ListaCampos.DB_SI, false ) );

		lcAtend.montaSql( false, "ATENDENTE", "AT" );
		lcAtend.setReadOnly( true );

		// FK para Item de Ordem de serviço
		txtTicket.setTabelaExterna( lcItRecMercItOS, FAtendente.class.getCanonicalName() );
		txtCodItRecMerc.setTabelaExterna( lcItRecMercItOS, FAtendente.class.getCanonicalName() );
		txtCodItOS.setTabelaExterna( lcItRecMercItOS, FAtendente.class.getCanonicalName() );

		txtTicket.setFK( true );
		txtCodItRecMerc.setFK( true );
		txtCodItOS.setFK( true );
		//		txtCodAtend.setNomeCampo( "CodAtend" );

		lcItRecMercItOS.add( new GuardaCampo( txtTicket, "Ticket", "Ticket", ListaCampos.DB_PK, false ) );
		lcItRecMercItOS.add( new GuardaCampo( txtCodItRecMerc, "CodItRecMerc", "Cod.it.rec.merc.", ListaCampos.DB_PK, false ) );
		lcItRecMercItOS.add( new GuardaCampo( txtCodItOS, "CodItOS", "Cod.it.OS", ListaCampos.DB_PK, false ) );
		lcItRecMercItOS.montaSql( false, "ITRECMERCITOS", "EQ" );
		lcItRecMercItOS.setReadOnly( true );
		
		// FK Qualificação
		lcQualificacao.add( new GuardaCampo( txtCodQualific, "CodQualific", "Cód.Qualific.", ListaCampos.DB_PK, false ) );
		lcQualificacao.add( new GuardaCampo( txtDescQualific, "DescQualific", "Descrição da qualificação", ListaCampos.DB_SI, false ) );

		lcQualificacao.montaSql( false, "QUALIFICACAO", "CR" );
		lcQualificacao.setQueryCommit( false );
		lcQualificacao.setReadOnly( true );
		txtCodQualific.setTabelaExterna( lcQualificacao, FQualificacao.class.getCanonicalName() );
		
		
		// FK Contrato
		txtCodContr.setTabelaExterna( lcContrato, null );
		txtCodContr.setFK( true );
		txtCodContr.setNomeCampo( "CodContr" );
		lcContrato.add( new GuardaCampo( txtCodContr, "CodContr", "Cód.Contrato", ListaCampos.DB_PK, false ) );
		lcContrato.add( new GuardaCampo( txtDescContr, "DescContr", "Desc.Contr.", ListaCampos.DB_SI, false ) );
		lcContrato.setDinWhereAdic( " CODCLI=#N", txtCodCli );
		lcContrato.montaSql( false, "CONTRATO", "VD" );

		//lcContrato.setQueryCommit( false );
		lcContrato.setReadOnly( true );

		txtCodItContr.setTabelaExterna( lcItContrato, null );
		txtCodItContr.setFK( true );
		txtCodItContr.setNomeCampo( "CodItContr" );
		lcItContrato.add( new GuardaCampo( txtCodItContr, "CodItContr", "Cód.It.Contr.", ListaCampos.DB_PK, false ) );
		lcItContrato.add( new GuardaCampo( txtCodContr, "CodContr", "Cód.Contrato", ListaCampos.DB_PK, false ) );
		lcItContrato.add( new GuardaCampo( txtDescItContr, "DescItContr", "Desc.It.Contr.", ListaCampos.DB_SI, false ) );
		lcItContrato.setDinWhereAdic( "CodContr=#N", txtCodContr );
		lcItContrato.montaSql( false, "ITCONTRATO", "VD" );
	
		//lcItContrato.setQueryCommit( false );
		lcItContrato.setReadOnly( true );
			

	}

	private void montaCombos() {

		cbTpChamado.setAutoSelect( "codtpchamado", "desctpchamado", "crtipochamado" );
		cbTpChamado.carregaValores();

		cbPrioridade.setItens( Prioridade.getLabels(), Prioridade.getValores() );

		cbStatus.setItens( Chamado.getLabels(), Chamado.getValores() );

	}



	public void exec( Integer codchamado ) {

		if ( codchamado != null ) {
			txtCodChamado.setVlrInteger( codchamado );
			lcCampos.carregaDados();
		}

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcCli.setConexao( cn );
		lcTipoChamado.setConexao( cn );
		lcAtend.setConexao( cn );
		lcItRecMercItOS.setConexao( cn );
		lcQualificacao.setConexao( cn );
		lcContrato.setConexao( cn );
		lcItContrato.setConexao( cn );

	}

	public void valorAlterado( JComboBoxEvent evt ) {

		if ( evt.getComboBoxPad() == cbTpChamado ) {
			if ( cbTpChamado.getVlrInteger() > 0 ) {
				txtCodTpChamado.setVlrInteger( cbTpChamado.getVlrInteger() );
				lcTipoChamado.carregaDados();

			}
			else {
				txtCodTpChamado.setVlrInteger( null );
			}
		}
		else if ( evt.getComboBoxPad() == cbStatus ) {
			if ( cbStatus.getVlrString().equals( Chamado.CHAMADO_CONCLUIDO.getValue() ) && txtDtConclusao.getVlrDate() == null ) {
				txtDtConclusao.setVlrDate( new Date() );
				txtDtConclusao.setEditable( true );
			}
		}

	}

	public void beforeInsert( InsertEvent ievt ) {
		
				
	}

	public void afterInsert( InsertEvent ievt ) {

		txtDtChamado.setVlrDate( new Date() );
		txtDtPrevisao.setVlrDate( new Date() );
		txtQtdHorasPrev.setVlrInteger( new Integer( 1 ) );

		cbStatus.setVlrString( "PE" );

	}

	private synchronized void notificar(int tipo) {

		DLLoading loading = new DLLoading(); 
		loading.setAlwaysOnTop( true );
		
		btSair.setEnabled( false );
		loading.start();

		
		EmailBean emailpad = null;
		EmailBean email = null;
		
		if( tipo == notifica_tecnico ){
			
			if(txtEmailTecnico.getVlrString()!=null && ! (txtEmailTecnico.getVlrString().equals( "" ))) {
				emailpad = createEmailBeanTecnico();
				email = emailpad.getClone();
				email.setPara( txtEmailTecnico.getVlrString() );
			}
			else {
				Funcoes.mensagemInforma( this, "Email do técnico, não informado!" );
			}
			
		}
		else if ( tipo == notifica_atendente ) {
				
			emailpad = createEmailBeanAtendente();
			
			if(emailpad!=null) {
			
				email = emailpad.getClone();
			}
			else {
				
				Funcoes.mensagemInforma( this, "Email do atendente, não informado!" );	
		
			}	
			
		}
		else if( tipo == notifica_cliente ) {
			
			if(txtEmailCli.getVlrString()!=null && ! (txtEmailCli.getVlrString().equals( "" ))) {
			
				emailpad = createEmailBeanCliente();
				email = emailpad.getClone();			
				email.setPara( txtEmailSolicitante.getVlrString() );
				
			}
			else {
			
				Funcoes.mensagemInforma( this, "Email do cliente, não informado!" );
				
			}
			
		}
		else {
			return;
		}
			
		try {

			
			email.createSession();
			
			Session session = email.getSession();

			try {

				MimeMessage msg = EmailBean.getMessage( session, email );

				String corpo = email.getCorpo();

				corpo = corpo.replaceAll( "#CODCHAMADO#"	, txtCodChamado.getVlrString() );
				corpo = corpo.replaceAll( "#DESCCHAMADO#"	, txtDescChamado.getVlrString() );
				corpo = corpo.replaceAll( "#DETCHAMADO#"	, txaDetChamado.getVlrString() );
				corpo = corpo.replaceAll( "#CODCLI#"		, txtCodCli.getVlrString() );
				corpo = corpo.replaceAll( "#RAZCLI#"		, txtRazCli.getVlrString() );
				corpo = corpo.replaceAll( "#PRIORIDADE#"	, cbPrioridade.getLabel() );
				
				corpo = corpo.replaceAll( "#DTABERTURA#"	, txtDtChamado.getVlrString() );
				corpo = corpo.replaceAll( "#DTPREVISAO#"	, txtDtPrevisao.getVlrString() );
				corpo = corpo.replaceAll( "#DTCONCLUSAO#"	, txtDtConclusao.getVlrString() );
				
				corpo = corpo.replaceAll( "#SOLICITANTE#"	, txtSolicitante.getVlrString() );
				
				if(txtUsuIns.getVlrString() == null || txtUsuIns.getVlrString().equals( "" )) {
					corpo = corpo.replaceAll( "#ATENDENTE#"		, Aplicativo.getUsuario().getIdusu() );
				}
				else {
					corpo = corpo.replaceAll( "#ATENDENTE#"		, txtUsuIns.getVlrString() );
				}
				
				corpo = corpo.replaceAll( "#DESIGNADO#"		, txtNomeAtend.getVlrString() );
				corpo = corpo.replaceAll( "#QTDHORASPREV#"	, txtQtdHorasPrev.getVlrString() );
				corpo = corpo.replaceAll( "#OBSCHAMADO#"	, txaObsChamado.getVlrString() );
				corpo = corpo.replaceAll( "#STATUSCHAMADO#"	, cbStatus.getLabel() );
				corpo = corpo.replaceAll( "#AMBIENTE#"		, txaAmbiente.getVlrString() );
				corpo = corpo.replaceAll( "#FATOGERADOR#"	, txaFatoGerador.getVlrString() );
		
				msg.setContent( corpo, email.getFormato() );

			
				try {

					email.send(msg);

				}
				catch (Exception e) {
					loading.stop();
					btSair.setEnabled( true );
					loading.dispose();
					Funcoes.mensagemErro(this, "Erro ao enviar pedido!\n" + e.getMessage(), true, con, e);
					e.printStackTrace();
				}
				finally {
					loading.stop();
					btSair.setEnabled( true );
					loading.dispose();

				}

			} 
			catch ( Exception e ) {
				e.printStackTrace();
			} 
			finally {
				System.gc();
			}


		} 
		catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private synchronized EmailBean createEmailBeanTecnico() {

		ResultSet rs = null;
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		EmailBean email = new EmailBean();
		
		sql.append( "SELECT CM.HOSTSMTP, CM.USAAUTSMTP, CM.USASSL, CM.PORTASMTP, CM.USUARIOREMET, CM.SENHAREMET, CM.CRIPTSENHA, CM.EMAILREMET, CM.EMAILRESP, " );
		sql.append( "EM.ASSUNTO, EM.CORPO, EM.FORMATO, EM.CHARSET " );
		
		sql.append( "FROM TKCONFEMAIL CM, TKEMAIL EM, SGPREFERE3 P3 " );
		
		sql.append( "WHERE ");
		
		sql.append( "CM.CODEMP=EM.CODEMP AND CM.CODFILIAL=EM.CODFILIAL AND CM.CODCONFEMAIL=EM.CODCONFEMAIL  " );
		
		sql.append( "AND EM.CODEMP=P3.CODEMPNC AND EM.CODFILIAL=P3.CODFILIALNC AND EM.CODEMAIL=P3.CODEMAILNC " );
	
		sql.append( "AND P3.CODEMP=? AND P3.CODFILIAL=? " );

		try {
			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "TKEMAIL" ) );


			rs = ps.executeQuery();
			if ( rs.next() ) {
				email.setHost( rs.getString( "HOSTSMTP" ) );
				email.setAutentica( rs.getString( "USAAUTSMTP" ) );
				email.setSsl( rs.getString( "USASSL" ) );
				email.setPorta( rs.getInt( "PORTASMTP" ) );
				email.setUsuario( rs.getString( "USUARIOREMET" ) );
				email.setSenha( rs.getString( "SENHAREMET" ), rs.getString( "CRIPTSENHA" ) );
				email.setDe( rs.getString( "EMAILREMET" ) );
				email.setEmailResp( rs.getString( "EMAILRESP" ) );
				email.setAssunto( rs.getString( "ASSUNTO" ) );
				email.setCorpo( rs.getString( "CORPO" ) );
				email.setFormato( rs.getString( "FORMATO" ) );
				email.setCharset( rs.getString( "CHARSET" ) );
			}
			rs.close();
			ps.close();
			con.commit();
		} catch ( SQLException e ) {
			Funcoes.mensagemErro( null, "Não foi possível carregar as informações para envio de email para técnico designado!\n" + e.getMessage() );
		}
		return email;

	}
	
	public EmailBean createEmailBeanCliente() {

		ResultSet rs = null;
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		EmailBean email = new EmailBean();
		
		sql.append( "SELECT CM.HOSTSMTP, CM.USAAUTSMTP, CM.USASSL, CM.PORTASMTP, CM.USUARIOREMET, CM.SENHAREMET, CM.CRIPTSENHA, CM.EMAILREMET, CM.EMAILRESP, " );
		sql.append( "EM.ASSUNTO, EM.CORPO, EM.FORMATO, EM.CHARSET " );
		
		sql.append( "FROM TKCONFEMAIL CM, TKEMAIL EM, TKEMAIL EA, SGPREFERE3 P3 " );
		
		sql.append( "WHERE ");
		
		sql.append( "CM.CODEMP=EM.CODEMP AND CM.CODFILIAL=EM.CODFILIAL AND CM.CODCONFEMAIL=EM.CODCONFEMAIL  " );
		
		sql.append( "AND EM.CODEMP=P3.CODEMPEC AND EM.CODFILIAL=P3.CODFILIALEC AND EM.CODEMAIL=P3.CODEMAILEC " );
	
		sql.append( "AND P3.CODEMP=? AND P3.CODFILIAL=? " );

		try {
			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "TKEMAIL" ) );


			rs = ps.executeQuery();
			if ( rs.next() ) {
				email.setHost( rs.getString( "HOSTSMTP" ) );
				email.setAutentica( rs.getString( "USAAUTSMTP" ) );
				email.setSsl( rs.getString( "USASSL" ) );
				email.setPorta( rs.getInt( "PORTASMTP" ) );
				email.setUsuario( rs.getString( "USUARIOREMET" ) );
				email.setSenha( rs.getString( "SENHAREMET" ), rs.getString( "CRIPTSENHA" ) );
				email.setDe( rs.getString( "EMAILREMET" ) );
				email.setEmailResp( rs.getString( "EMAILRESP" ) );
				email.setAssunto( rs.getString( "ASSUNTO" ) );
				email.setCorpo( rs.getString( "CORPO" ) );
				email.setFormato( rs.getString( "FORMATO" ) );
				email.setCharset( rs.getString( "CHARSET" ) );
			}
			rs.close();
			ps.close();
			con.commit();
		} catch ( SQLException e ) {
			Funcoes.mensagemErro( null, "Não foi possível carregar as informações para envio de email ao cliente!\n" + e.getMessage() );
		}
		return email;

	}
	
	public EmailBean createEmailBeanAtendente() {

		ResultSet rs = null;
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		EmailBean email = new EmailBean();
		
		sql.append( "SELECT CM.HOSTSMTP, CM.USAAUTSMTP, CM.USASSL, CM.PORTASMTP, CM.USUARIOREMET, CM.SENHAREMET, CM.CRIPTSENHA, CM.EMAILREMET, CM.EMAILRESP, " );
		sql.append( "EM.ASSUNTO, EM.CORPO, EM.FORMATO, EM.CHARSET, ce.emailremet emailatend " );
		
		sql.append( "FROM TKCONFEMAIL CM, TKEMAIL EM, SGPREFERE3 P3 " );
		
		sql.append( "left outer join sgusuario us on us.codemp=? and us.codfilial=? and us.idusu=? ");
		
		sql.append( "left outer join tkconfemail ce on ce.codemp=us.codempce and ce.codfilial=us.codfilialce and ce.codconfemail=us.codconfemail ");
		
		sql.append( "WHERE ");
		
		sql.append( "CM.CODEMP=EM.CODEMP AND CM.CODFILIAL=EM.CODFILIAL AND CM.CODCONFEMAIL=EM.CODCONFEMAIL  " );
		
		sql.append( "AND EM.CODEMP=P3.CODEMPEA AND EM.CODFILIAL=P3.CODFILIALEA AND EM.CODEMAIL=P3.CODEMAILEA " );
		
		sql.append( "AND P3.CODEMP=? AND P3.CODFILIAL=? " );

		try {
			ps = con.prepareStatement( sql.toString() );
		
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGUSUARIO" ) );
			
			if(txtUsuIns.getVlrString()!=null && !txtUsuIns.getVlrString().trim().equals( "" ) ) {
				
				ps.setString( 3, txtUsuIns.getVlrString().toLowerCase() );
				
			}
			else {
				
				ps.setString( 3, Aplicativo.getUsuario().getIdusu().toLowerCase() );
				
			}

			
			ps.setInt( 4, Aplicativo.iCodEmp );
			ps.setInt( 5, ListaCampos.getMasterFilial( "TKEMAIL" ) );


			rs = ps.executeQuery();
			if ( rs.next() ) {
				
				
				if(rs.getString( "emailatend" )!=null) {
				
					email.setPara( rs.getString( "emailatend" ) );
					
					email.setHost( rs.getString( "HOSTSMTP" ) );
					email.setAutentica( rs.getString( "USAAUTSMTP" ) );
					email.setSsl( rs.getString( "USASSL" ) );
					email.setPorta( rs.getInt( "PORTASMTP" ) );
					email.setUsuario( rs.getString( "USUARIOREMET" ) );
					email.setSenha( rs.getString( "SENHAREMET" ), rs.getString( "CRIPTSENHA" ) );
					email.setDe( rs.getString( "EMAILREMET" ) );
					email.setEmailResp( rs.getString( "EMAILRESP" ) );
					email.setAssunto( rs.getString( "ASSUNTO" ) );
					email.setCorpo( rs.getString( "CORPO" ) );
					email.setFormato( rs.getString( "FORMATO" ) );
					email.setCharset( rs.getString( "CHARSET" ) );
				}
				else {
					
					rs.close();
					ps.close();
					
					con.commit();
					
					return null;
					
				}
				
				
			}
			
			rs.close();
			ps.close();
			
			con.commit();
			
		} catch ( SQLException e ) {
			Funcoes.mensagemErro( null, "Não foi possível carregar as informações para envio de email ao atendente!\n" + e.getMessage() );
		}
		return email;

	}

	public void afterCarrega( CarregaEvent cevt ) {

		if ( cevt.getListaCampos() == lcCli && lcCampos.getStatus() == ListaCampos.LCS_INSERT ) {

			txtSolicitante.setVlrString( txtContCli.getVlrString() );
			txtEmailSolicitante.setVlrString( txtEmailCli.getVlrString() );

		}

	}

	public void beforeCarrega( CarregaEvent cevt ) {

		// TODO Auto-generated method stub

	}

	public void beforePost( PostEvent pevt ) {
		if ( lcCampos.getStatus() == ListaCampos.LCS_INSERT ) {
				txtCodChamado.setVlrInteger( Integer.parseInt( lcCampos.getNovoCodigo().trim()) );
		}
	}
	
	public void afterPost( PostEvent pevt ) {

		if(cbNotificaTecnico.getVlrString().equals( "S" ) 
				|| cbNotificaAtendente.getVlrString().equals( "S" ) 
				|| cbNotificaCliente.getVlrString().equals( "S" )) {
			
			ProcessoSec pSec = new ProcessoSec(001, new Processo() {

			public void run() {	}
				
			}, new Processo() {

				public void run() {
					if(cbNotificaTecnico.getVlrString().equals( "S" )) {
						notificar( notifica_tecnico );
					}
					if(cbNotificaAtendente.getVlrString().equals( "S" )) {
						notificar( notifica_atendente );
					}
					if(cbNotificaCliente.getVlrString().equals( "S" )) {
						notificar( notifica_cliente );
					}
					
				}
				
			});

			pSec.iniciar();

		}

	}

	public void novo() {
		lcCampos.insert( true );
	}

	public void setCodCli(Integer codcli) {
		txtCodCli.setVlrInteger( codcli );
		lcCli.carregaDados();
	}

	public void setDescChamado(String descchamado) {
		txtDescChamado.setVlrString( descchamado );
	}

	public void setDetChamado(String detchamado) {
		txaDetChamado.setVlrString( detchamado );		
	}

	public void setSolicitante(String solicitante) {
		txtSolicitante.setVlrString( solicitante );
	}
	
	public void setCodContr(Integer codcontr){
		txtCodContr.setVlrInteger( codcontr );
	}
	
	public void setCodItContr(Integer coditcontr){
		txtCodItContr.setVlrInteger( coditcontr );
	}
	
	public void setCodAtend(Integer codatend) {
		txtCodAtend.setVlrInteger( codatend );
		lcAtend.carregaDados();
	}

	public void setPrioridade(Integer prioridade) {
		cbPrioridade.setVlrInteger( prioridade );
	}

	public void setCodTpChamado(Integer codtpchamado) {
		cbTpChamado.setVlrInteger( codtpchamado );
		txtCodTpChamado.setVlrInteger( codtpchamado );

		lcTipoChamado.carregaDados();
	}

	public void setDtChamado(Date dtchamado) {
		txtDtChamado.setVlrDate( dtchamado );
	}

	public void setDtPrevisao(Date dtprevisao) {
		txtDtPrevisao.setVlrDate( dtprevisao );
	}

	public void setQtdHorasPrevisao(BigDecimal qtdhorasprevisao) {
		txtQtdHorasPrev.setVlrBigDecimal( qtdhorasprevisao );
	}

	public void setItOS(Integer ticket, Integer coditrecmerc, Integer coditos) {
		txtTicket.setVlrInteger( ticket );
		txtCodItRecMerc.setVlrInteger( coditrecmerc );
		txtCodItOS.setVlrInteger( coditos );
		lcItRecMercItOS.carregaDados();
	}

	public void setTelaAnterior(FDados telaant) {
		this.telanterior = telaant;
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btSair ) {
			if(telanterior!=null) {
				telanterior.lcCampos.carregaDados();
			}
		}

		super.actionPerformed( evt );

	}


}
