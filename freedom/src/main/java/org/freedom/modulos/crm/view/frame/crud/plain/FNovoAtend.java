/*
 * Projeto: Freedom Pacote: org.freedom.modules.crm Classe: @(#)FAtendimento.java
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR> modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR> na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR> sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR> Veja a Licença Pública Geral GNU para maiores detalhes. <BR> Você deve ter recebido uma cópia da Licença Pública
 * Geral GNU junto com este programa, se não, <BR> escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA <BR>
 */

package org.freedom.modulos.crm.view.frame.crud.plain;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner.DateEditor;
import javax.swing.SpinnerDateModel;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.JComboBoxEvent;
import org.freedom.acao.JComboBoxListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.modulos.crm.agenda.DLNovoAgen;
import org.freedom.modulos.crm.business.component.Atendimento;
import org.freedom.modulos.crm.business.object.SaldoContrato;
import org.freedom.modulos.crm.dao.DAOAgenda;
import org.freedom.modulos.crm.dao.DAOAtendimento;
import org.freedom.modulos.crm.object.Agenda;
import org.freedom.modulos.crm.view.frame.utility.FCRM;

/**
 * 
 * @author Setpoint Informática Ltda./Alex Rodrigues
 * @version 10/10/2009 - Alex Rodrigues
 * @version 23/04/2010 - Anderson Sanchez
 * @version 28/10/2010 - Robson Sanchez e Bruno Nascimento
 */
public class FNovoAtend extends FFilho implements KeyListener, CarregaListener, ActionListener, JComboBoxListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pnCab = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );
	
	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodCli2 = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCodAtend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodChamado = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescChamado = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodEspec = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescEspec = new JTextFieldFK( JTextFieldPad.TP_STRING, 100, 0 );

	private JTextFieldPad txtCodAtendo = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeAtend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtDataAtendimento = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDataAtendimentoFin = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtHoraini = new JTextFieldPad( JTextFieldPad.TP_TIME, 5, 0 );

	private JTextFieldPad txtHorafim = new JTextFieldPad( JTextFieldPad.TP_TIME, 5, 0 );

	private JTextFieldPad txtHoraBloq = new JTextFieldPad( JTextFieldPad.TP_TIME, 5, 0 );

	private JTextFieldPad txtCodTpAtendo = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtDescTpAtendo = new JTextFieldFK( JTextFieldFK.TP_STRING, 50, 0 );

	//private JTextFieldPad txtContr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtCodContr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescContr = new JTextFieldFK( JTextFieldFK.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodItContr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtDescItContr = new JTextFieldFK( JTextFieldFK.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodContrCh = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodItContrCh = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtRecebContr = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtStatusAtendo = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );
	
	private JTextFieldPad txtSitAtendo = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtAtivoAtendo = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtExcedentecob = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtQtdhoras = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtQtditcontr = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtSaldo = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtCodsetat = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtDescSetor = new JTextFieldFK( JTextFieldFK.TP_STRING, 50, 0 );

	private JTextFieldFK txtObrigChamEspec = new JTextFieldFK( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldFK txtObrigProjEspec = new JTextFieldFK( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtCodTarefa= new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtDescTarefa = new JTextFieldFK( JTextFieldFK.TP_STRING, 100, 0 );

	private JTextFieldPad txtCobcliEspec= new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldFK txtTipoorc = new JTextFieldFK( JTextFieldFK.TP_STRING, 2, 0 );

	private JTextFieldPad txtCodorc= new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtDataOrc = new JTextFieldFK( JTextFieldFK.TP_DATE, 10, 0 );

	private JTextFieldFK txtCodCliOrc = new JTextFieldFK( JTextFieldFK.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtVlrOrc = new JTextFieldFK( JTextFieldFK.TP_DECIMAL, 10, Aplicativo.casasDec );

	private JTextAreaPad txaObsAtend = new JTextAreaPad();

	private JTextAreaPad txaObsInterno = new JTextAreaPad();

	private Vector<Integer> vValsTipo = new Vector<Integer>();

	private Vector<String> vLabsTipo = new Vector<String>();

	private Vector<Integer> vValsSetor = new Vector<Integer>();

	private Vector<String> vLabsSetor = new Vector<String>();
	
	private Vector<String> vValsStatus = new Vector<String>();
	
	private Vector<String> vLabsStatus = new Vector<String>();
	
	private JComboBoxPad cbStatus = new JComboBoxPad( vLabsTipo, vValsTipo, JComboBoxPad.TP_STRING, 2, 0 );
	
	private Vector<String> vValsSituacao = new Vector<String>();

	private Vector<String> vLabsSituacao = new Vector<String>();
	
	private JComboBoxPad cbSituacao = new JComboBoxPad( vLabsSituacao, vValsSituacao, JComboBoxPad.TP_STRING, 2, 0 );
	
	private JCheckBoxPad cbConcluiChamado = new JCheckBoxPad( "Conclui chamado?", "S", "N" );

	private ListaCampos lcAtend = new ListaCampos( this, "AE" );

	private ListaCampos lcEspec = new ListaCampos( this, "EA" );

	private ListaCampos lcChamado = new ListaCampos( this, "CH" );

	private ListaCampos lcCli = new ListaCampos( this, "CL" );

	private ListaCampos lcTpAtendo = new ListaCampos( this, "TO" ); 

	private ListaCampos lcSetor = new ListaCampos( this , "SA"); 

	private ListaCampos lcContrato = new ListaCampos( this , "CT"); 

	private ListaCampos lcItContrato = new ListaCampos( this , "CT"); 

	private ListaCampos lcTarefa = new ListaCampos( this , "TA"); 

	private ListaCampos lcOrc = new ListaCampos( this ); 

	private ListaCampos lcAtendimento = new ListaCampos( this );

	private JLabelPad lbImg = new JLabelPad( Icone.novo( "bannerAtendimento.png" ) );

	private JButtonPad btMedida = new JButtonPad( Icone.novo( "btMedida.png" ) );

	private JButtonPad btRun = new JButtonPad( Icone.novo( "btplay.png" ) );
	
	public JButtonPad btOK = new JButtonPad("OK", Icone.novo("btOk.png"));
	
	public JButtonPad btCancel = new JButtonPad("Cancelar", Icone.novo("btCancelar.png"));
	
	public JButtonPad btAgendar = new JButtonPad("Agendar", Icone.novo( "btAgenda.png"));

	private JLabelPad lbContador = new JLabelPad();

	private JPanelPad pnGeral = new JPanelPad( new BorderLayout() );

	private JPanelPad pnTela = new JPanelPad( new BorderLayout() );

	private JPanelPad pnCampos = new JPanelPad( 500, 340 );
	
	private JPanelPad pnBotoes =  new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );
	
	private JPanelPad pnAlinhaBotoes = new JPanelPad ( JPanelPad.TP_JPANEL, new GridLayout( 1, 2 ) );

	private JPanelPad pnTxa = new JPanelPad( new GridLayout( 2, 1 ) );

	private String tipoatendo = null;

	private Integer codrec = null;

	private Integer nparcitrec = null;

	private Integer codchamado_ant = null;

	private int iDoc = 0;

	private boolean financeiro = false;

	private boolean update = false;

	private boolean contando = false;

	private Thread contador = null;

	private Component corig = null;

	private DAOAtendimento daoatend = null;
	
	private String[] retornoAgenda = null; 

	private Object[] prefs;
	
	private DAOAgenda daoagenda;

	private Map<String, Object> agente;

	private Integer codagenda = 0; 
	
	private String agendado = null;
	
	public FNovoAtend( int iCodCli, Integer codchamado, Component cOrig, boolean isUpdate,
			DbConnection conn, int codatendo, int codatend, String tipoatendo, boolean financeirop, 
			String titulo, Integer codorc, boolean atendimentoBloqueado ) {
		this( iCodCli, codchamado, cOrig, conn, tipoatendo, isUpdate, financeirop, titulo);

		corig = cOrig;

		update = isUpdate;

		txtCodAtendo.setVlrInteger( codatendo );

		lcAtendimento.carregaDados();
		
		cbStatus.setVlrString( txtStatusAtendo.getVlrString() );
		
		txtCodChamado.setVlrInteger( codchamado );
		
		lcChamado.carregaDados();

		if( codorc > 0 ) {
			txtCodorc.setEnabled( false );
			txtCodorc.setVlrInteger( codorc );
		} else {
			txtCodorc.setEnabled( true );
			txtCodorc.setVlrInteger( codorc );
		}
		
		lcOrc.carregaDados();
		
		if ( update ) {
			pnCampos.adic( new JLabelPad( "Status" ), 510, 290, 120, 20 );
			pnCampos.adic( cbStatus, 510, 310, 100, 20 );
			//txtCoditContrato.setSize( 198, 20 );		
		}	
		montaComboStatus();
		cbSituacao.setVlrString( txtSitAtendo.getVlrString() );
		
		
		bloqueiaCampos( atendimentoBloqueado );

	}	
	
	public void bloqueiaCampos(boolean ativo) {
		txtCodCli.setAtivo( ativo );
		txtCodChamado.setAtivo( ativo );
		txtCodAtend.setAtivo( ativo );
		txtCodTpAtendo.setAtivo( ativo );
		txtCodsetat.setAtivo( ativo );
		txtCodContr.setAtivo( ativo );
		txtCodItContr.setAtivo( ativo );
		txtCodTarefa.setAtivo( ativo );
		txtCodEspec.setAtivo( ativo );
		txtDataAtendimento.setAtivo( ativo );
		txtHoraini.setAtivo( ativo );
		txtHorafim.setAtivo( ativo );
		txtCodorc.setAtivo( ativo );
		txaObsAtend.setAtivo( ativo );
		txaObsInterno.setAtivo( ativo );
		cbConcluiChamado.setEnabled( ativo );
		cbStatus.setEnabled( ativo );
		btRun.setEnabled( ativo ); 
		btOK.setEnabled( ativo );
		cbSituacao.setEnabled( ativo );
	}

	public void adicAtendimento( int codcli, Integer codchamado, Component cOrig,
			DbConnection conn, boolean isUpdate, String tipoatendo, boolean financeirop ){

		this.financeiro = financeirop;

		String horaini = null;

		update = isUpdate;
		this.tipoatendo = tipoatendo;

		//		montaListaCampos();

		txtCodCli.setVlrInteger( codcli );

		txtCodChamado.setVlrInteger( codchamado );

		if ( !update ) {

			txtCodAtend.setVlrInteger( Atendimento.buscaAtendente() );
			lcAtend.carregaDados();

			if ( getAutoDataHora() ) {

				txtHoraini.setVlrTime( new Date() );
				txtDataAtendimento.setVlrDate( new Date() );
				txtDataAtendimentoFin.setVlrDate( new Date() );

				try {

					horaini = daoatend.getHoraPrimUltLanca( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "ATATENDIMENTO" ), 
							new Date(), Funcoes.dateToStrTime(  new Date() ) , Funcoes.dateToStrTime(  new Date() ) ,
							Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "ATATENDENTE" ), txtCodAtend.getVlrInteger(), 
							"F" );
					if (horaini!=null) {
						txtHoraini.setVlrString( horaini );
					}
				} catch ( SQLException e ) {
					e.printStackTrace();
				}

				iniciaContagem();
			}
		}
	}

	public boolean isUpdate() {
		return update;
	}

	public void adicAtendimento( int iCodCli, Integer codchamado, Component cOrig, boolean isUpdate, 
			DbConnection conn, int codatendo, int codatend, String tipoatendo, boolean financeirop, 
			Integer codorc, boolean atendimentoBloqueado ) {

		adicAtendimento( iCodCli, codchamado, cOrig, conn, isUpdate, tipoatendo, financeirop );

		update = isUpdate;

		txtCodAtendo.setVlrInteger( codatendo );

		lcAtendimento.carregaDados();

		cbStatus.setVlrString( txtStatusAtendo.getVlrString() );
		
		txtCodChamado.setVlrInteger( codchamado );

		lcChamado.carregaDados();

		if( codorc > 0 ) {
			txtCodorc.setEnabled( false );
			txtCodorc.setVlrInteger( codorc );
		} else {
			txtCodorc.setEnabled( true );
			txtCodorc.setVlrInteger( codorc );
		}

		lcOrc.carregaDados();

		bloqueiaCampos( atendimentoBloqueado );
	}

	public FNovoAtend( int codcli, Integer codchamado, Component cOrig, DbConnection conn, boolean isUpdate, Integer codrec, Integer nparcitrec, String tipoatendo, boolean financeirop, String titulo ) {

		this( codcli, codchamado, cOrig, conn, tipoatendo, isUpdate, financeirop, titulo );

		this.codrec = codrec;
		this.nparcitrec = nparcitrec;

	}

	public FNovoAtend( Component cOrig, DbConnection conn, org.freedom.modulos.crm.business.object.Atendimento atd, String tipoatendo, String titulo ) {
		this( atd.getCodcli(), atd.getCodchamado(), cOrig, conn, tipoatendo, false, false, titulo );
		//txtCodAtend.setVlrInteger( atd.getCodatend() );
		txtCodTpAtendo.setVlrInteger( atd.getCodtpatendo() );
		lcTpAtendo.carregaDados();
		if (atd.getCodchamado()!=null) {
			txtCodChamado.setVlrInteger( atd.getCodchamado() );
			lcChamado.carregaDados();
		}
		if (txtCodsetat.getVlrInteger() != null) {
			txtCodsetat.setVlrInteger( atd.getCodsetat() ) ;
			lcSetor.carregaDados();
		} 
		if (atd.getCodcontr()!=null) {
			txtCodContr.setVlrInteger( atd.getCodcontr() );
			txtCodItContr.setVlrInteger( atd.getCoditcontr() );
			lcContrato.carregaDados();
			lcItContrato.carregaDados();
		}
		if (atd.getCodespec()!=null) {
			txtCodEspec.setVlrInteger( atd.getCodespec() );
			lcEspec.carregaDados();
		}
		if ( atd.getObsatendo()!=null ) {
			txaObsAtend.setVlrString( atd.getObsatendo() );
		}
		if ( atd.getObsinterno()!=null ) {
			txaObsInterno.setVlrString( atd.getObsinterno() );
		}
		if ( atd.getCodtarefa()!=null ) {
			txtCodTarefa.setVlrInteger( atd.getCodtarefa() );
			lcTarefa.carregaDados();
		}

		if ( atd.getCodorc()!=null ) {
			txtCodorc.setVlrInteger( atd.getCodorc() );
			lcOrc.carregaDados();
		}
		
	}

	public FNovoAtend( int codcli, Integer codchamado, Component cOrig, DbConnection conn, String tipoatendo, boolean isUpdate, boolean financeirop, String titulo ) {
		this(true);
		
		String horaini = null;
		corig = cOrig;

		this.financeiro = financeirop;
		this.update = isUpdate;
		this.tipoatendo = tipoatendo;
		
		montaTela(titulo);

		txtCodCli.setVlrInteger( codcli );

		txtCodChamado.setVlrInteger( codchamado );

		setConexao( conn );
		
		btAgendar.setEnabled( false );
		if ( !update ) {
			montaComboStatus();
			txtCodAtend.setVlrInteger( Atendimento.buscaAtendente() );
			lcAtend.carregaDados();

			if ( getAutoDataHora() ) {

				txtHoraini.setVlrTime( new Date() );
				txtDataAtendimento.setVlrDate( new Date() );
				txtDataAtendimentoFin.setVlrDate( new Date() );

				try {

					horaini = daoatend.getHoraPrimUltLanca( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "ATATENDIMENTO" ), 
							new Date(), Funcoes.dateToStrTime(  new Date() ) , Funcoes.dateToStrTime(  new Date() ) ,
							Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "ATATENDENTE" ), txtCodAtend.getVlrInteger(), 
							"F" );
					if (horaini!=null) {
						txtHoraini.setVlrString( horaini );
					}
				} catch ( SQLException e ) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				iniciaContagem();

			}
		}
	}

	public FNovoAtend( boolean scroll ) {
		super(scroll);
	}

	private void montaTela(String titulo) {

		if(titulo == null){
			setTitulo( "Novo atendimento" );
		} else {
			setTitulo(titulo);
		}
		setAtribos( 50,50, 640, 740 );

		montaListaCampos();
		
		btMedida.setPreferredSize( new Dimension( 30, 30 ) );

		pnCab.add( lbImg );
		
		add( pnCab, BorderLayout.NORTH );
		//add( pnGeral );

/*		c.add( pnCab, BorderLayout.NORTH );

		c.add( pnGeral );*/

		//pnCliente.add( pnCab, BorderLayout.NORTH );
		pnCliente.add( pnTela, BorderLayout.CENTER );

		pnCliente.add( pnCampos, BorderLayout.NORTH );

		pnCliente.add( pnTxa, BorderLayout.CENTER );
		
		
		
		JScrollPane spnDetalhamento = new JScrollPane( txaObsAtend );
		JScrollPane spnObsInterno = new JScrollPane( txaObsInterno );

		spnDetalhamento.setBorder( BorderFactory.createTitledBorder( "Detalhamento" ) );
		spnObsInterno.setBorder( BorderFactory.createTitledBorder( "Observações internas" ) );

		pnTxa.add( spnDetalhamento );
		pnTxa.add( spnObsInterno );

		txaObsAtend.setBorder( BorderFactory.createEtchedBorder( Color.RED, null ) );

		
		pnCampos.adic( txtCodCli, 7, 30, 80, 20, "Cód.cli." );
		pnCampos.adic( txtRazCli, 90, 30, 524, 20, "Razão social do cliente" );

		pnCampos.adic( txtCodChamado, 7, 70, 80, 20, "Cód.chamado" );
		pnCampos.adic( txtDescChamado, 90, 70, 524, 20, "Descrição do chamado" );

		pnCampos.adic( txtCodAtend, 7, 110, 80, 20, "Cód.atend." );
		pnCampos.adic( txtNomeAtend, 90, 110, 200, 20, "Nome do atendente" );

		pnCampos.adic( txtCodTpAtendo, 293, 110, 80, 20, "Cód.tp.at." );
		pnCampos.adic( txtDescTpAtendo, 376, 110, 237, 20, "Descrição do tipo de atendimento" );
		pnCampos.adic( txtCodsetat, 7, 150, 80, 20, "Cód.setor" );
		pnCampos.adic( txtDescSetor, 90, 150, 200, 20, "Descrição do setor" );

		pnCampos.adic( txtCodContr, 294, 150, 80, 20, "Cód.ctr./proj." );
		pnCampos.adic( txtDescContr, 377, 150, 237, 20, "Descrição do contrato/projeto" );
		pnCampos.adic( txtCodItContr, 7, 190, 80, 20, "Cód.item proj." );
		pnCampos.adic( txtDescItContr, 90, 190, 200, 20, "Descrição do item de contr./proj." );
		pnCampos.adic( txtCodTarefa, 294, 190, 80, 20, "Cód.Tarefa" );
		pnCampos.adic( txtDescTarefa, 377, 190, 237, 20, "Descrição da tarefa" );


		pnCampos.adic( txtDataAtendimento, 294, 230, 80, 20, "Início" );
		pnCampos.adic( txtHoraini, 377, 230, 53, 20 );
		pnCampos.adic( txtDataAtendimentoFin, 433, 230, 70, 20, "Final" );
		pnCampos.adic( txtHorafim, 506, 230, 53, 20 );
		pnCampos.adic( btRun, 559, 230, 19, 19 );
		
		
		pnCampos.adic( txtCodEspec, 7, 230, 80, 20, "Cód.espec." );
		pnCampos.adic( txtDescEspec, 90, 230, 200, 20, "Descrição da especificação do atendimento");

		pnCampos.adic( txtCodorc, 7, 270, 80, 20, "Cód.orc." );
		pnCampos.adic( txtVlrOrc, 90, 270, 90, 20, "Vlr. orçamento" );
		pnCampos.adic( txtDataOrc, 183, 270, 100, 20, "Data orçamento" );
		pnCampos.adic( cbSituacao, 286,270,150,20, "Situação");


		pnCampos.adic( new JLabelPad("Franquia"), 160, 290, 80, 20 );
		pnCampos.adic( txtQtditcontr, 160, 310, 80, 20 );

		pnCampos.adic( new JLabelPad("Tot.horas"), 243, 290, 80, 20 );
		pnCampos.adic( txtQtdhoras, 243, 310, 80, 20 );

		pnCampos.adic( new JLabelPad("Saldo"), 326, 290, 80, 20 );
		pnCampos.adic( txtSaldo, 326, 310, 80, 20 );

		pnCampos.adic( new JLabelPad("Excedente"), 409, 290, 80, 20 );
		pnCampos.adic( txtExcedentecob, 409, 310, 80, 20 );

		pnCampos.adic( cbConcluiChamado, 7, 300, 150, 20 );
		
		
		pnCampos.adic( btAgendar, 480,260, 120, 30 );
		
		
		add( pnBotoes, BorderLayout.SOUTH );
		pnBotoes.add( pnAlinhaBotoes, BorderLayout.EAST );
		
		pnAlinhaBotoes.setPreferredSize( new Dimension( 250, 30 ) );
		pnAlinhaBotoes.add( btOK);
		pnAlinhaBotoes.add( btCancel);
		
		txtDataAtendimento.setRequerido( true );
		txtDataAtendimentoFin.setRequerido( false );
		txtDataAtendimentoFin.setSoLeitura( true );
		txtQtditcontr.setSoLeitura( true );
		txtSaldo.setSoLeitura( true );
		txtExcedentecob.setSoLeitura( true );
		txtQtdhoras.setSoLeitura( true );
		txtDataAtendimento.addKeyListener( this );


		btMedida.addActionListener( this );
		btCancel.addActionListener( this );
		btOK.addActionListener( this );
		btAgendar.addActionListener( this );
		
		lcChamado.addCarregaListener( this );
		lcEspec.addCarregaListener( this );
		lcAtend.addCarregaListener( this );
		lcContrato.addCarregaListener( this );
		lcCli.addCarregaListener( this );
		lcOrc.addCarregaListener( this );

		txtCodCli.setRequerido( true );
		txtCodTpAtendo.setRequerido( true );
		txtCodsetat.setRequerido( true );
		

		btRun.addActionListener( this );
		

	}

	private void loadSaldoContrato() {
		Date dt = txtDataAtendimento.getVlrDate();
		if (dt!=null) {
			int mes = Funcoes.getMes( dt )-1;
			int ano = Funcoes.getAno( dt );
			Date dtini = Funcoes.getDataIniMes( mes, ano );
			Date dtfin = Funcoes.getDataFimMes( mes, ano );
			try {
				SaldoContrato sld = daoatend.loadSaldoContrato(
						Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "VDCLIENTE" ), txtCodCli.getVlrInteger()
						, Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "VDCONTRATO" ), txtCodContr.getVlrInteger(), 0
						, dtini, dtfin );
				if (sld==null) {
					txtExcedentecob.setVlrString("");
					txtSaldo.setVlrString( "" );
					txtQtditcontr.setVlrString( "" );
					txtQtdhoras.setVlrString( "" );
				} else {
					txtExcedentecob.setVlrBigDecimal( sld.getExcedentemescob() );
					txtSaldo.setVlrBigDecimal( sld.getSaldomes() );
					txtQtditcontr.setVlrBigDecimal( sld.getQtditcontr() );
					txtQtdhoras.setVlrBigDecimal( sld.getQtdhoras() );
				}

			} catch (SQLException e ) {
				try {
					con.rollback();
				} catch (SQLException err) {
					err.printStackTrace();
				}
				Funcoes.mensagemErro( this, "Erro carregando saldo de contrato !\n" + e.getMessage() );
				e.printStackTrace();
			}
		}

	}

	private void montaListaCampos() {

		txtCodCli.setTabelaExterna( lcCli, null );
		txtCodCli.setFK( true );
		txtCodCli.setNomeCampo( "CodCli" );
		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCli.montaSql( false, "CLIENTE", "VD" );
		lcCli.setReadOnly( true );

		txtCodAtend.setTabelaExterna( lcAtend, null );
		txtCodAtend.setFK( true );
		txtCodAtend.setNomeCampo( "CodAtend" );
		lcAtend.add( new GuardaCampo( txtCodAtend, "CodAtend", "Cód.atend.", ListaCampos.DB_PK, false ), "txtCodVendx" );
		lcAtend.add( new GuardaCampo( txtNomeAtend, "NomeAtend", "Nome", ListaCampos.DB_SI, false ), "txtCodVendx" );
		lcAtend.montaSql( false, "ATENDENTE", "AT" );
		lcAtend.setReadOnly( true );

		txtCodAtendo.setTabelaExterna( lcAtendimento, null );
		txtCodAtendo.setFK( true );
		txtCodAtendo.setNomeCampo( "CodAtendo" );
		lcAtendimento.add( new GuardaCampo( txtCodAtendo, "CodAtendo", "Cód.atendo", ListaCampos.DB_PK, false ) );
		lcAtendimento.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.Cli.", ListaCampos.DB_FK, false ) );
		lcAtendimento.add( new GuardaCampo( txtCodAtend, "CodAtend", "Cod.Atend.", ListaCampos.DB_FK, false ) );
		lcAtendimento.add( new GuardaCampo( txtCodChamado, "CodChamado", "Cód.Chamado", ListaCampos.DB_FK, false ) );
		lcAtendimento.add( new GuardaCampo( txtDataAtendimento, "dataAtendo", "Data atendimento", ListaCampos.DB_SI, false ) );
		lcAtendimento.add( new GuardaCampo( txtDataAtendimentoFin, "dataAtendoFin", "Data atendimento fin.", ListaCampos.DB_SI, false ) );
		lcAtendimento.add( new GuardaCampo( txtHoraini, "HoraAtendo", "Hora atendimento", ListaCampos.DB_SI, false ) );
		lcAtendimento.add( new GuardaCampo( txtHorafim, "HoraAtendoFin", "Hora atendimento fin.", ListaCampos.DB_SI, false ) );
		lcAtendimento.add( new GuardaCampo( txaObsAtend, "ObsAtendo", "Descrição", ListaCampos.DB_SI, false ) );
		lcAtendimento.add( new GuardaCampo( txtCodTpAtendo, "codtpatendo", "Tipo", ListaCampos.DB_FK, false ) );
		lcAtendimento.add( new GuardaCampo( txtCodsetat, "codsetat", "setor", ListaCampos.DB_FK, false ) );
		lcAtendimento.add( new GuardaCampo( txtCodContr, "codcontr", "Codcontrato", ListaCampos.DB_FK, false ) );
		lcAtendimento.add( new GuardaCampo( txtCodItContr, "coditcontr", "item do contrato", ListaCampos.DB_FK, false ) );
		lcAtendimento.add( new GuardaCampo( txtCodTarefa, "Codtarefa", "Cód.Tarefa", ListaCampos.DB_FK, false ) );
		lcAtendimento.add( new GuardaCampo( txtStatusAtendo, "statusatendo", "Status do atendimento", ListaCampos.DB_SI, false ) );
		lcAtendimento.add( new GuardaCampo( txtSitAtendo, "sitatendo", "Situação do atendimento", ListaCampos.DB_SI, false ) );
		lcAtendimento.add( new GuardaCampo( txaObsInterno, "obsinterno", "Observação interna", ListaCampos.DB_SI, false ) );
		lcAtendimento.add( new GuardaCampo( txtCodEspec, "codespec", "Cód.Espec.", ListaCampos.DB_FK, !financeiro ) );

		lcAtendimento.montaSql( false, "ATENDIMENTO", "AT" );
		lcAtendimento.setReadOnly( true );

		txtCodChamado.setTabelaExterna( lcChamado, null );
		txtCodChamado.setFK( true );
		txtCodChamado.setNomeCampo( "CodChamado" );
		lcChamado.add( new GuardaCampo( txtCodChamado, "CodChamado", "Cód.Chamado", ListaCampos.DB_PK, false ) );
		lcChamado.add( new GuardaCampo( txtDescChamado, "DescChamado", "Descrição do chamado", ListaCampos.DB_SI, false ) );
		lcChamado.add( new GuardaCampo( txtCodContrCh, "codcontr", "Codcontrato", ListaCampos.DB_SI, false ) );
		lcChamado.add( new GuardaCampo( txtCodItContrCh, "coditcontr", "item do contrato", ListaCampos.DB_SI, false ) );
		lcChamado.add( new GuardaCampo( txtCodCli2, "CodCli", "Cód.Cli.", ListaCampos.DB_SI, false ) );
		lcChamado.setDinWhereAdic( " STATUS NOT IN('CO','CA') AND CODCLI=#N", txtCodCli );
		lcChamado.montaSql( false, "CHAMADO", "CR" );
		lcChamado.setReadOnly( true );


		txtCodEspec.setTabelaExterna( lcEspec, null );
		txtCodEspec.setFK( true );
		txtCodEspec.setNomeCampo( "CodEspec" );
		lcEspec.add( new GuardaCampo( txtCodEspec, "CodEspec", "Cód.Espec.", ListaCampos.DB_PK, !financeiro ) );
		lcEspec.add( new GuardaCampo( txtDescEspec, "DescEspec", "Descrição da especificação", ListaCampos.DB_SI, false ) );
		lcEspec.add( new GuardaCampo( txtObrigChamEspec, "ObrigChamEspec", "Chamado Obrigatório no Atendimento", ListaCampos.DB_SI, false ) );
		lcEspec.add( new GuardaCampo( txtObrigProjEspec, "ObrigProjEspec", "Contrato/Projeto Obrigatório no Atendimento", ListaCampos.DB_SI, false ) );
		lcEspec.add( new GuardaCampo( txtCobcliEspec, "CobCliEspec", "Cobrável", ListaCampos.DB_SI, false ) );
		lcEspec.montaSql( false, "ESPECATEND", "AT" );
		lcEspec.setReadOnly( true );

		txtCodTpAtendo.setTabelaExterna( lcTpAtendo, null );
		txtCodTpAtendo.setNomeCampo( "CodTpAtendo" );
		lcTpAtendo.add( new GuardaCampo( txtCodTpAtendo, "CodTpAtendo", "Cód.tp.atendo.", ListaCampos.DB_PK, false) );
		lcTpAtendo.add( new GuardaCampo( txtDescTpAtendo, "DescTpAtendo", "Descrição do Tipo de Atendimento", ListaCampos.DB_SI, false ) );
		lcTpAtendo.add( new GuardaCampo( txtAtivoAtendo, "AtivoAtendo", "Atendimento Ativo", ListaCampos.DB_SI, false ) );
		lcTpAtendo.setWhereAdic( " ATIVOATENDO='S' AND TIPOATENDO='"+tipoatendo+"' ");
		//lcTpAtendo.setDinWhereAdic( " TIPOATENDO='"+tipoatendo+"'" );
		lcTpAtendo.montaSql( false, "TIPOATENDO", "AT" );
		lcTpAtendo.setReadOnly( true );

		txtCodsetat.setTabelaExterna( lcSetor, null );
		txtCodsetat.setFK( true );
		txtCodsetat.setNomeCampo( "CodSetAt" );
		lcSetor.add( new GuardaCampo( txtCodsetat, "CodSetAt", "Cód.Setor", ListaCampos.DB_PK, false) );
		lcSetor.add( new GuardaCampo( txtDescSetor, "DescSetAt", "Descrição do Setor", ListaCampos.DB_SI, false ) );
		lcSetor.montaSql( false, "SETOR", "AT" );
		//lcSetor.setDinWhereAdic( "CodTpAtendo=#N", txtCodTpAtendo );
		txtCodTpAtendo.setFK( true );
		lcSetor.setReadOnly( true );

		txtCodContr.setTabelaExterna( lcContrato, null );
		txtCodContr.setFK( true );
		txtCodContr.setNomeCampo( "CodContr" );
		lcContrato.add( new GuardaCampo( txtCodContr, "CodContr", "Cód.Contrato", ListaCampos.DB_PK, false ) );
		lcContrato.add( new GuardaCampo( txtDescContr, "DescContr", "Desc.Contr.", ListaCampos.DB_SI, false ) );
		lcContrato.add( new GuardaCampo( txtRecebContr, "RecebContr", "Recebível", ListaCampos.DB_SI, false ) );

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
		lcItContrato.setDinWhereAdic( " CodContr=#N", txtCodContr );
		lcItContrato.montaSql( false, "ITCONTRATO", "VD" );

		//lcItContrato.setQueryCommit( false );
		lcItContrato.setReadOnly( true );


		//FK Tarefa.

		txtCodTarefa.setTabelaExterna( lcTarefa, null );
		txtCodTarefa.setFK( true );
		txtCodTarefa.setNomeCampo( "CodTarefa" );
		lcTarefa.add( new GuardaCampo( txtCodTarefa, "CodTarefa", "Cód.Tarefa", ListaCampos.DB_PK, false ) );
		lcTarefa.add( new GuardaCampo( txtDescTarefa, "DescTarefa", "Descrição da tarefa", ListaCampos.DB_SI, false ) );
		lcTarefa.setWhereAdic( " LanctoTarefa='S' " );
		lcTarefa.setDinWhereAdic( " CodContr=#N", txtCodContr );
		lcTarefa.setDinWhereAdic( " CodItContr=#N", txtCodItContr);
		lcTarefa.montaSql( false, "TAREFA", "CR" );		
		lcTarefa.setReadOnly( true );

		//FK Orçamento.

		txtCodorc.setTabelaExterna( lcOrc, null );
		txtCodorc.setFK( true );
		txtCodorc.setNomeCampo( "CodOrc" );
		lcOrc.add( new GuardaCampo( txtCodorc, "CodOrc", "Cód.Orc", ListaCampos.DB_PK, false ) );
		lcOrc.add( new GuardaCampo( txtTipoorc, "Tipoorc", "Tipoorc", ListaCampos.DB_SI, false ) );
		lcOrc.add( new GuardaCampo( txtDataOrc, "dtorc", "dtorc", ListaCampos.DB_SI, false ) );
		lcOrc.add( new GuardaCampo( txtVlrOrc, "vlrliqorc", "vlrorc", ListaCampos.DB_SI, false ) );
		lcOrc.add( new GuardaCampo( txtCodCliOrc, "codcli", "codcli", ListaCampos.DB_SI, false ) );
		lcOrc.setDinWhereAdic( " CodCli=#N", txtCodCli);
		lcOrc.montaSql( false, "ORCAMENTO", "VD" );		
		lcOrc.setReadOnly( true );

	}

	private void montaComboStatus() {


		vValsStatus.addElement( "AA" );
		vValsStatus.addElement( "NC" );
		vLabsStatus.addElement( "Atendido" );
		vLabsStatus.addElement( "Não computado" );

		cbStatus.setItensGeneric( vLabsStatus, vValsStatus );
		
	
		vLabsSituacao.addElement( "<--Selecione-->" );
		vValsSituacao.addElement( "" );
		
		vLabsSituacao.addElement( "Rejeitado" );
		vValsSituacao.addElement( "RJ" );
		
		if ("AA".equals( txtSitAtendo.getVlrString())) {
			vLabsSituacao.addElement( "Agendado" );
			vValsSituacao.addElement( "AA" );		
		} else {
			vValsSituacao.addElement( "AG" );
			vLabsSituacao.addElement( "Agendar" );
		}
		
		vLabsSituacao.addElement( "Efetivado" );
		vValsSituacao.addElement( "EF" );
		cbSituacao.setItensGeneric( vLabsSituacao, vValsSituacao );
		
		cbSituacao.addComboBoxListener( this );
	}
	
	private boolean getAutoDataHora() {

		boolean autoDataHora = true;

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT AUTOHORATEND FROM SGPREFERE3 WHERE CODEMP=? AND CODFILIAL=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				autoDataHora = "S".equals( rs.getString( "AUTOHORATEND" ) );
			}

			rs.close();
			ps.close();
			con.commit();

		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao verificar preferências.\n" + e.getMessage() );
		}

		return autoDataHora;
	}

	private int getCodLev() {

		int iRet = 0;

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT ISEQ FROM SPGERANUM(?,?,?)" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			ps.setString( 3, "LV" );

			ResultSet rs = ps.executeQuery();
			if ( rs.next() ) {
				iRet = rs.getInt( 1 );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar novo código para levantamento.\n" + e.getMessage(), true, con, e );
		}

		return iRet;
	}

	private void insertIntervaloAtend(String horaini, String horafim) {
		try {
			daoatend.insertIntervaloAtend( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "ATATENDIMENTO" ), 
					txtDataAtendimento.getVlrDate(), txtDataAtendimentoFin.getVlrDate(),
					horaini, horafim, 
					Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "ATATENDENTE" ), txtCodAtend.getVlrInteger(), 
					Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "SGUSUARIO" ), Aplicativo.getUsuario().getIdusu() );

		}
		catch (Exception e) {
			Funcoes.mensagemErro( this, "Erro inserindo lançamento automatizado de intervalo !\n" + e.getMessage() );
			e.printStackTrace();
		}

	}

	private void verificaAtendimentoAnterior(Integer codatend, Date data, String hora) {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;

		String hora_ini_intervalo = null;

		Long diferenca_lanca = 0L;
		Long diferenca_turno = 0L;
		Long diferenca = 0L;

		String ini_turno = null;
		String fim_turno = null;

		String ini_inter = null;
		String fim_inter = null;

		hora = hora.substring( 0,5 );

		boolean turno = false;

		boolean tem_lancamento = false;

		boolean teste = false;

		try {

			sql.append( "select first 1 ");
			sql.append( "atd.dataatendofin, atd.horaatendofin ");
			sql.append( "from atatendimento atd ");
			sql.append( "where ");
			sql.append( "atd.codemp=? and atd.codfilial=? and atd.codempae=? and atd.codfilialae=? and atd.codatend=? and ");
			sql.append( "atd.dataatendofin = ? and atd.horaatendofin <= ? and ");
			sql.append( "atd.codatendo<>? ");
			sql.append( "order by dataatendofin desc, horaatendofin desc " );

			System.out.println("QUERY ULTIMO ATENDIMENTO:" + sql.toString());

			ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "ATATENDIMENTO" ) );

			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "ATATENDENTE" ) );
			ps.setInt( 5, codatend );

			ps.setDate( 6, Funcoes.dateToSQLDate( data ) );
			ps.setTime( 7, Funcoes.strTimeTosqlTime( hora )  );
			ps.setInt( 8, txtCodAtendo.getVlrInteger()  );


			rs = ps.executeQuery();

			if(rs.next()) {

				String horafinant = rs.getString( "horaatendofin" );
				horafinant = horafinant.substring( 0,5 );

				System.out.println( "Hora do último lançamento:" + horafinant.toString() );

				diferenca_lanca = Funcoes.subtraiTime( Funcoes.strTimeTosqlTime( horafinant), Funcoes.strTimeTosqlTime( hora ) );

				System.out.println( "Diferença:" + Funcoes.longTostrTime( diferenca_lanca ) );

				hora_ini_intervalo = horafinant;

				tem_lancamento = true;

			}

			con.commit();
			rs.close();


			// Verificação do enquadramento no turno

			sql = new StringBuilder();

			sql.append( "select ");
			sql.append( "tu.hiniturno, tu.hfimturno, tu.hiniintturno, tu.hfimintturno ");

			sql.append( "from ");
			sql.append( "atatendente ate ");
			sql.append( "left outer join rhempregado em on em.codemp=ate.codempep and em.codfilial=ate.codfilialep and em.matempr=ate.matempr ");
			sql.append( "left outer join rhturno tu on tu.codemp=em.codempto and tu.codfilial=em.codfilialto and tu.codturno=em.codturno ");
			sql.append( "where ate.codemp=? and ate.codfilial=? and ate.codatend=? " );

			ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "ATATENDENTE" ) );
			ps.setInt( 3, codatend );

			rs = ps.executeQuery();

			if(rs.next()) {

				ini_turno = rs.getString( "hiniturno" );
				fim_turno = rs.getString( "hfimturno" );

				ini_inter = rs.getString( "hiniintturno" );
				fim_inter = rs.getString( "hfimintturno" );

				if ( (fim_inter!=null ) && (ini_inter != null ) && (ini_turno != null  ) ) {
					// Verifica se o lançamento é após o fim do intervalo...


					diferenca_turno = Funcoes.subtraiTime( Funcoes.strTimeTosqlTime( fim_inter ), Funcoes.strTimeTosqlTime( hora ) );

					//hora_ini_intervalo =  fim_inter;

					if(diferenca_turno < 0 ) {

						// Se o lançamento é anterior ao fim do intervalo deve verificar se é anterior ao início do intervalo...
						diferenca_turno = Funcoes.subtraiTime( Funcoes.strTimeTosqlTime( ini_inter ), Funcoes.strTimeTosqlTime( hora ) );

						if(diferenca_turno < 0) {

							// Indica que o lançamento é anterior ao inicio do intervalo, ou seja deve obedecer o inicio do turno...							
							diferenca_turno = Funcoes.subtraiTime( Funcoes.strTimeTosqlTime( ini_turno ), Funcoes.strTimeTosqlTime( hora ) );

							//hora_ini_intervalo =  ini_turno;
						}						
					}	
				}

			}

			con.commit();
			rs.close();

			String texto_dif = "";

			if(tem_lancamento) { 

				//sobrepondo a diferença do turno
				if( ( diferenca_lanca > 1) && ( !"00:00:00".equals( Funcoes.longTostrTime(diferenca_lanca) ) ) ) {

					diferenca = diferenca_lanca;

					texto_dif = "seu último lançamento";

					turno = false;

				}

			}
			else {
				if( diferenca_turno >0 ) {

					diferenca = diferenca_turno;

					texto_dif = "o início do seu turno";

					turno = true;

				}
			}


			if(diferenca > 0) {

				StringBuilder mensagem = new StringBuilder();

				mensagem.append( "Existe um intervalo de " );
				mensagem.append( Funcoes.longTostrTime( diferenca ) );
				mensagem.append( " entre " );

				mensagem.append( texto_dif );

				mensagem.append( " e o lançamento atual.\n ");

				mensagem.append( "Gostaria de inserir o intervalo automaticamente?" );

				if( Funcoes.mensagemConfirma( null, mensagem.toString() ) == JOptionPane.YES_OPTION ) {

					// Inserir atendimento padrão para intervalos
					teste = true;
					//insertIntervalo( turno ? ini_turno : hora_ini_intervalo , txtHoraini.getVlrString() );


				}
				else {

					return;

				}

			}


			if(teste) {
				insertIntervaloAtend( turno ? ini_turno : hora_ini_intervalo , txtHoraini.getVlrString() );
			}


		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void insert() throws Exception {
		
		org.freedom.modulos.crm.business.object.Atendimento atd = new org.freedom.modulos.crm.business.object.Atendimento();

		atd.setCodemp( Aplicativo.iCodEmp );
		atd.setCodfilial( ListaCampos.getMasterFilial( "ATATENDIMENTO" ));

		atd.setCodempto( Aplicativo.iCodEmp );
		atd.setCodfilialto( ListaCampos.getMasterFilial( "ATTIPOATENDO" ));
		atd.setCodtpatendo( txtCodTpAtendo.getVlrInteger() );

		atd.setCodempca( Aplicativo.iCodEmp );
		atd.setCodfilialca( ListaCampos.getMasterFilial( "ATCLASATENDO" ));

		if (txtCodCli.getVlrInteger().intValue()!=0) {	
			atd.setCodempcl( Aplicativo.iCodEmp );
			atd.setCodfilialcl( ListaCampos.getMasterFilial( "VDCLIENTE" ));
			atd.setCodcli( txtCodCli.getVlrInteger() );
		}

		atd.setCodempcv( Aplicativo.iCodEmp );
		atd.setCodfilialcv( ListaCampos.getMasterFilial( "ATCONVENIADO" ));

		atd.setCodempae( Aplicativo.iCodEmp );
		atd.setCodfilialae( ListaCampos.getMasterFilial( "ATATENDENTE" ));
		atd.setCodatend( txtCodAtend.getVlrInteger() ); // Código do atendente logado

		atd.setCodempus( Aplicativo.iCodEmp );
		atd.setCodfilialus( ListaCampos.getMasterFilial( "SGUSUARIO" )); // Id do usuário logado
		atd.setIdusu( Aplicativo.getUsuario().getIdusu() );

		atd.setCodempsa( Aplicativo.iCodEmp );
		atd.setCodfilialsa( ListaCampos.getMasterFilial( "ATATENDENTE" ));
		atd.setCodsetat( txtCodsetat.getVlrInteger() ); // Setor de atendimento 

		atd.setDocatendo( String.valueOf( iDoc ) );
		atd.setCodatendo( txtCodAtendo.getVlrInteger() );
		atd.setDataatendo( txtDataAtendimento.getVlrDate() );
		atd.setDataatendofin( txtDataAtendimentoFin.getVlrDate() );
		atd.setHoraatendo( txtHoraini.getVlrString() );
		atd.setHoraatendofin( txtHorafim.getVlrString() );
		atd.setObsatendo( txaObsAtend.getVlrString() );
		atd.setObsinterno( txaObsInterno.getVlrString() );
		atd.setConcluichamado( cbConcluiChamado.getVlrString() );

		if (txtCodContr.getVlrInteger().intValue()!= 0) {		
			atd.setCodempct( Aplicativo.iCodEmp );
			atd.setCodfilialct( ListaCampos.getMasterFilial( "VDCONTRATO" ));
			atd.setCodcontr( txtCodContr.getVlrInteger() );
			atd.setCoditcontr( txtCodItContr.getVlrInteger() );
		}

		if (txtCodChamado.getVlrInteger().intValue()!=0) {
			atd.setCodempch( Aplicativo.iCodEmp );
			atd.setCodfilialch( ListaCampos.getMasterFilial( "CRCHAMADO" ));
			atd.setCodchamado( txtCodChamado.getVlrInteger() );
			//	atd.setCodcli( txtCodCli2.getVlrInteger()  );
		}

		if (txtCodEspec.getVlrInteger().intValue()!=0) {	
			atd.setCodempea( Aplicativo.iCodEmp );
			atd.setCodfilialea( ListaCampos.getMasterFilial( "ATESPECATEND" ));
			atd.setCodespec( txtCodEspec.getVlrInteger() );
		}
		if (txtCodTarefa.getVlrInteger().intValue()!=0) {	
			atd.setCodempta( Aplicativo.iCodEmp );
			atd.setCodfilialta( ListaCampos.getMasterFilial( "CRTAREFA" ));
			atd.setCodtarefa( txtCodTarefa.getVlrInteger() );
		}

		if (codrec!=null) {			
			atd.setCodempir( Aplicativo.iCodEmp );
			atd.setCodfilialir( ListaCampos.getMasterFilial( "FNRECEBER" ));
			atd.setCodrec( codrec );
			atd.setNparcitrec( nparcitrec );
		}

		if (txtCodorc.getVlrInteger().intValue()!=0) {			
			atd.setCodempoc( Aplicativo.iCodEmp );
			atd.setCodfilialoc( ListaCampos.getMasterFilial( "VDORCAMENTO" ));
			atd.setTipoorc( txtTipoorc.getVlrString() );
			atd.setCodorc( txtCodorc.getVlrInteger() );
		}

		if (!"".equals(cbSituacao.getVlrString())) {
			if ("AA".equals( agendado )) {
				atd.setSitatendo( "AA" );
			} else {
				atd.setSitatendo( cbSituacao.getVlrString() );
			}
		}
		
		if (codagenda > 0) {
			atd.setCodempag( Aplicativo.iCodEmp );
			atd.setCodfilialag( ListaCampos.getMasterFilial( "SGAGENDA" ));
			atd.setTipoage((String) agente.get("TipoAge"));
			atd.setCodage( (Integer) agente.get( "CodAge" ) );
			atd.setCodagd( codagenda );
		}
		
		daoatend.insert( atd );
		
		if(corig instanceof FCRM) {
			(( FCRM ) corig).carregaAtendimentos();	
		}

	}
	
	private Integer inseriAgenda() {
		agente = daoagenda.buscaAgente();
		
		Integer ret = null;
		Agenda agenda = new Agenda();
		agenda.setHini(retornoAgenda[1] + ":00");
		agenda.setHfim(retornoAgenda[3] + ":00");
		agenda.setAssunto(retornoAgenda[4]);
		agenda.setDescricao(retornoAgenda[5]);
		agenda.setCodfilialagd(retornoAgenda[6]);
		agenda.setTipoagd(retornoAgenda[7]);
		agenda.setPrioridade(retornoAgenda[8]);
		agenda.setCodagente(retornoAgenda[9]);
		agenda.setTipoagente(retornoAgenda[10]);
		agenda.setCodfilialagt((Integer) agente.get( "CodFilialAge" ));
		agenda.setCodagentee((Integer) agente.get("CodAge"));
		agenda.setTipoagentee((String) agente.get("TipoAge"));
		agenda.setControleacesso(retornoAgenda[11]);
		agenda.setStatus(retornoAgenda[12]);
		agenda.setMotivo(retornoAgenda[13]);
		agenda.setDtini(Funcoes.strDateToSqlDate(retornoAgenda[0]));
		agenda.setDtfim(Funcoes.strDateToSqlDate(retornoAgenda[2]));
		agenda.setRepete(false);
		agenda.setCont(0);
		agenda.setCodagdar(null);
		agenda.setDiatodo(retornoAgenda[14]);
		
		ret = daoagenda.insert(agenda);
		
		return ret;
	}

	private void update() throws Exception {
		org.freedom.modulos.crm.business.object.Atendimento atd = new org.freedom.modulos.crm.business.object.Atendimento();

		if( txtCodAtend.getVlrInteger() != null ){
			atd.setCodatend( txtCodAtend.getVlrInteger() );
		}
		atd.setCodemp( Aplicativo.iCodEmp );
		atd.setCodfilial( ListaCampos.getMasterFilial( "ATATENDIMENTO" ) );

		atd.setCodempto( Aplicativo.iCodEmp );
		atd.setCodfilialto( ListaCampos.getMasterFilial( "ATTIPOATENDO" ) );
		atd.setCodtpatendo( txtCodTpAtendo.getVlrInteger() );

		atd.setCodempca( Aplicativo.iCodEmp );
		atd.setCodfilialca( ListaCampos.getMasterFilial( "ATCLASATENDO" ));

		if (txtCodCli.getVlrInteger().intValue()!= 0) {	
			atd.setCodempcl( Aplicativo.iCodEmp );
			atd.setCodfilialcl( ListaCampos.getMasterFilial( "VDCLIENTE" ));
			atd.setCodcli( txtCodCli.getVlrInteger() );
		}

		atd.setCodempae( Aplicativo.iCodEmp );
		atd.setCodfilialae( ListaCampos.getMasterFilial( "ATATENDENTE" ));
		atd.setCodatend( txtCodAtend.getVlrInteger() ); // Código do atendente logado

		atd.setCodempcv( Aplicativo.iCodEmp );
		atd.setCodfilialcv( ListaCampos.getMasterFilial( "ATCONVENIADO" ));

		atd.setCodempus( Aplicativo.iCodEmp );
		atd.setCodfilialus( ListaCampos.getMasterFilial( "SGUSUARIO" )); // Id do usuário logado
		atd.setIdusu( Aplicativo.getUsuario().getIdusu() );

		atd.setCodempsa( Aplicativo.iCodEmp );
		atd.setCodfilialsa( ListaCampos.getMasterFilial( "ATSETOR" ) );
		atd.setCodsetat(  txtCodsetat.getVlrInteger() );

		atd.setCodatendo( txtCodAtendo.getVlrInteger() ); //Código do atendimento.

		atd.setDocatendo( String.valueOf( iDoc ) );
		atd.setDataatendo( txtDataAtendimento.getVlrDate() );
		atd.setDataatendofin( txtDataAtendimentoFin.getVlrDate() );
		atd.setHoraatendo( txtHoraini.getVlrString() );
		atd.setHoraatendofin( txtHorafim.getVlrString() );
		atd.setObsatendo( txaObsAtend.getVlrString() );
		atd.setObsinterno( txaObsInterno.getVlrString() ); //Observações internas
		atd.setConcluichamado( cbConcluiChamado.getVlrString() );

		if ( txtCodChamado.getVlrInteger().intValue()!= 0 ) {
			atd.setCodempch( Aplicativo.iCodEmp ); // Código da empresa do chamando
			atd.setCodfilialch( ListaCampos.getMasterFilial( "CRCHAMADO" )); // Código da filial da empresa do chamado
			atd.setCodchamado( txtCodChamado.getVlrInteger() ); // Código do chamado
			//	atd.setCodcli( txtCodCli2.getVlrInteger() );
		}

		if ( txtCodItContr.getVlrInteger().intValue()!= 0) {		
			atd.setCodempct( Aplicativo.iCodEmp );
			atd.setCodfilialct( ListaCampos.getMasterFilial( "VDCONTRATO" ));
			atd.setCodcontr( txtCodContr.getVlrInteger() ); // Código do Contrato
			atd.setCoditcontr( txtCodItContr.getVlrInteger() ); //Código do item do Contrato
		}

		atd.setStatusatendo( cbStatus.getVlrString() );

		if (txtCodEspec.getVlrInteger().intValue() != 0) {	
			atd.setCodempea( Aplicativo.iCodEmp );
			atd.setCodfilialea( ListaCampos.getMasterFilial( "ATESPECATEND" ));
			atd.setCodespec( txtCodEspec.getVlrInteger() );
		}

		if (txtCodTarefa.getVlrInteger().intValue()!=0) {	
			atd.setCodempta( Aplicativo.iCodEmp );
			atd.setCodfilialta( ListaCampos.getMasterFilial( "CRTAREFA" ));
			atd.setCodtarefa( txtCodTarefa.getVlrInteger() );
		}

		if (codrec!=null) {			
			atd.setCodempir( Aplicativo.iCodEmp );
			atd.setCodfilialir( ListaCampos.getMasterFilial( "FNRECEBER" ));
			atd.setCodrec( codrec );
			atd.setNparcitrec( nparcitrec );
		}

		if (txtCodorc.getVlrInteger().intValue() != 0)	{	
			atd.setCodempoc( Aplicativo.iCodEmp );
			atd.setCodfilialoc( ListaCampos.getMasterFilial( "VDORCAMENTO" ));
			atd.setTipoorc( txtTipoorc.getVlrString() );
			atd.setCodorc( txtCodorc.getVlrInteger() );
		}
		

		if (!"".equals(cbSituacao.getVlrString())) {
			if ("AA".equals( agendado )) {
				atd.setSitatendo( "AA" );
			} else {
				atd.setSitatendo( cbSituacao.getVlrString() );
			}
		}
		
		if (codagenda > 0) {
			atd.setCodempag( Aplicativo.iCodEmp );
			atd.setCodfilialag( ListaCampos.getMasterFilial( "SGAGENDA" ));
			atd.setTipoage((String) agente.get("TipoAge"));
			atd.setCodage( (Integer) agente.get( "CodAge" ) );
			atd.setCodagd( codagenda );
		}
		
		daoatend.update( atd );

		if(corig instanceof FCRM) {

			(( FCRM ) corig).carregaAtendimentos();	

		}

	}

	private void iniciaContagem() {

		if ( !contando ) {

			btRun.setIcon( Icone.novo( "btpause.png" ) );

			contador = new Thread( new Runnable() {

				public void run() {

					try {
						Calendar calini = null;

						while ( contando ) {
							try {
								Thread.sleep( 1000 );
							} catch ( Exception e ) {
								System.out.println( "Contagem interrompida" );
							}
							calini = new GregorianCalendar();
							txtHorafim.setVlrTime( calini.getTime() );
						}
					} catch ( Exception e ) {
						e.printStackTrace();
						Funcoes.mensagemInforma( null, "Erro na contagem!" );
					} finally {
						Calendar calini = null;
						SpinnerDateModel sdm = null;
						DateEditor de = null;
					}
				}
			} );
			try {
				contador.start();
				contando = true;
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}
		else {
			try {
				btRun.setIcon( Icone.novo( "btplay.png" ) );
				contador.interrupt();
			} catch ( Exception e ) {
				System.out.println( "Contagem interrompida" );
			} finally {
				contando = false;
			}
		}
	}
	private boolean consistForm(){
		boolean result = true;

		if ( txtDataAtendimento.getVlrDate().after( txtDataAtendimentoFin.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final menor que a data inicial!" );
			txtDataAtendimento.requestFocus();
			result = false;
		}
		else if ("".equals( txtCodCli.getText().trim() ) ){
			Funcoes.mensagemInforma( this, "Selecione o Cliente!" );
			txtCodCli.requestFocus();
			result = false;
		}

		else if ( "S".equals( txtObrigChamEspec.getVlrString() ) && ( "".equals(txtCodChamado.getText().trim() ) ) ) {
			Funcoes.mensagemInforma( this, "Código do Chamado não foi selecionado!" );
			txtCodChamado.requestFocus();
			result = false;
		}
		else if ( "".equals( txtCodAtend.getText().trim() ) ) {
			Funcoes.mensagemInforma( this, "Selecione o atendente!" );
			txtCodAtend.requestFocus();
			result = false;
		}
		else if ( "".equals( txtCodTpAtendo.getText().trim() ) ) {
			Funcoes.mensagemInforma( this, "O tipo de atendimento não foi selecionado!" );
			txtCodTpAtendo.requestFocus();
			result = false;
		}
		else if ( "".equals( txtCodsetat.getText().trim() ) ) {
			Funcoes.mensagemInforma( this, "O setor não foi selecionado!" );
			txtCodsetat.requestFocus();
			result = false;
		}
		else if ( "S".equals( txtObrigProjEspec.getVlrString() ) && 
				(( "".equals( txtCodContr.getText().trim() )) || ("".equals( txtCodItContr.getText().trim() ))) ){
			if ( "".equals( txtCodContr.getText().trim() ) ) {
				Funcoes.mensagemInforma( this, "Contrato/Projeto não foi selecionado!" );
				txtCodContr.requestFocus();
			}
			else if ( "".equals( txtCodItContr.getText().trim() ) ){
				Funcoes.mensagemInforma( this, "O item do Contrato/Projeto não foi selecionado!" );
				txtCodItContr.requestFocus();
			}
			result = false;
		}
		else if ( txtDataAtendimento.getVlrString().equals( "" ) ) {
			Funcoes.mensagemInforma( this, "Data inicial é requerida !" );
			txtDataAtendimento.requestFocus();
			result = false;
		}
		else if ( txtDataAtendimentoFin.getVlrString().equals( "" ) ) {
			Funcoes.mensagemInforma( this, "Data final é requerida !" );
			txtDataAtendimentoFin.requestFocus();
			result = false;
		}
		else if ( txtHoraini.getVlrString().equals( "" ) ) {
			Funcoes.mensagemInforma( this, "Hora inicial é requerida !" );
			txtHoraini.requestFocus();
			result = false;
		}
		else if ( txtHorafim.getVlrString().equals( "" ) ) {
			Funcoes.mensagemInforma( this, "Hora final é requerida !" );
			txtHorafim.requestFocus();
			result = false;
		}
		else if(txtHoraini.getVlrTime().compareTo( txtHorafim.getVlrTime() ) >= 0 )  {
			Funcoes.mensagemInforma( null, "Horário inicial deve ser menor que horário final !");
			result = false;
		}	
		else if( "".equals(txtCodEspec.getText().trim() ) && !financeiro) {
			Funcoes.mensagemInforma(null,"Informe a especificação do atendimento !");
			txtCodEspec.requestFocus();
			result = false;
		}
		else if ( txaObsAtend.getVlrString().equals( "" ) ) {
			Funcoes.mensagemInforma( this, "Não foi digitado nenhum procedimento !" );
			txaObsAtend.requestFocus();
			result = false;
		}
		else if ( "S".equals(txtCobcliEspec.getVlrString()) && !"S".equals( txtRecebContr.getVlrString() ) ) {
			Funcoes.mensagemInforma( this, "Esta especificação não pode ser utilizada para projeto interno !" );
			txtCodEspec.requestFocus();
			result = false;
		}
		else if ( cbSituacao.getVlrString().equals( "AG" ) && codagenda == 0 ) {
			Funcoes.mensagemInforma( this, "Agendamento é obrigatório!" );
			btAgendar.requestFocus();
			result = false;
		}
		
		return result;
	}

	private boolean gravaForm(){
		boolean result = true;
		if ( update ) { 
			try {

				update();

				verificaAtendimentoAnterior( txtCodAtend.getVlrInteger(), txtDataAtendimento.getVlrDate(), txtHoraini.getVlrString()+":01" );

			} 
			catch ( Exception e ) {
				Funcoes.mensagemInforma( this, "Erro ao gravar o atendimento!\n" + e.getMessage() );
				e.printStackTrace();
				result = false;
			}

		}
		else {
			try {

				insert();

				verificaAtendimentoAnterior( txtCodAtend.getVlrInteger(), txtDataAtendimento.getVlrDate(), txtHoraini.getVlrString()+":01"  );

			} 
			catch ( Exception e ) {
				e.printStackTrace();
				Funcoes.mensagemInforma( this, "Erro ao gravar o atendimento!\n" + e.getMessage() );
				result = false;
			}

		}

		sinalizaChamado( false, txtCodChamado.getVlrInteger() );	

		return result;
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			if( consistForm() ){
				try {
					Integer codorc = daoatend.getCodOrc( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "VDORCAMENTO" ), txtCodAtendo.getVlrInteger());

					if (codorc > 0) {
						Integer codcliorc = daoatend.getCodCliOrc( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "VDORCAMENTO" ),"O", codorc );

						if (! (codcliorc.compareTo( txtCodCli.getVlrInteger()) ==  0) )  {
							Funcoes.mensagemInforma( null, "Contato vinculado a um orçamento, não é possivel alterar o cliente!!!" );
							txtCodCli.setVlrInteger( codcliorc );
							txtCodorc.setVlrInteger( codorc );
							lcOrc.carregaDados();
							return;

						}
					}
				}catch (Exception e) {
					Funcoes.mensagemErro( null, "Erro ao carregar Código do cliente.");
				}
				try {
					Integer codatend = txtCodAtend.getVlrInteger();
					if(daoatend.bloquearAtendimentos( 0, txtDataAtendimento.getVlrString(), txtHoraini.getVlrString(), true, codatend, codatend  ) ) {
						Funcoes.mensagemInforma( null, "Lançamento fora do prazo de inserção/edição!!!");
					} else {
						if( gravaForm() ){
							setVisible(false);
						}
					}
			
				} catch (SQLException e) {
					Funcoes.mensagemErro( null, "Erro ao bloquear Atendimento!!!" );
					e.printStackTrace();
				}
			}
		}
		else if ( evt.getSource() == btRun ) {
			iniciaContagem();
		}
		else if ( evt.getSource() == btCancel ) {
			sinalizaChamado( false, txtCodChamado.getVlrInteger() );
			dispose();
		}
		else if (evt.getSource() == btAgendar) {
			if (txtDataAtendimento.getVlrDate() == null && txtDataAtendimentoFin.getVlrDate()== null) {
				Funcoes.mensagemInforma(this, "Selecione uma data!");
				return;
			}
			
			
			DLNovoAgen dl = new DLNovoAgen( Aplicativo.getUsuario().getIdusu(), Aplicativo.getUsuario().getIdusu(), 
					txtDataAtendimento.getVlrDate(), txtDataAtendimentoFin.getVlrDate(),  this, true );
			dl.setConexao( con );
			dl.setVisible( true );
			if (dl.OK) {
				retornoAgenda = dl.getValores();
				if (retornoAgenda != null) {
					codagenda = inseriAgenda();
				}
				agendado = "AA";
			}
			else 
				return;
		}
	}

	
	public void keyPressed( KeyEvent kevt ) {

		if ( kevt.getSource() == txtDataAtendimento ) {
			if ( kevt.getKeyCode() == KeyEvent.VK_ENTER ) {
				if ( !"".equals( txtDataAtendimento.getVlrString() ) ) {
					txtDataAtendimentoFin.setVlrDate( txtDataAtendimento.getVlrDate() );
				}
				else {
					Funcoes.mensagemInforma( this, "Digite a data do atendimento!" );
					txtDataAtendimento.requestFocus();
				}

			}
		}

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		//montaComboTipo();
		//montaComboSetor();
		

		lcAtendimento.setConexao( cn );
		
		lcAtend.setConexao( cn );

		lcCli.setConexao( cn );

		lcChamado.setConexao( cn );

		lcTpAtendo.setConexao( cn );

		lcSetor.setConexao( cn );

		lcContrato.setConexao( cn );

		lcItContrato.setConexao( cn );

		lcTarefa.setConexao( cn );

		lcEspec.setConexao( cn );

		lcOrc.setConexao( cn );
		lcAtendimento.carregaDados();
		lcChamado.carregaDados();
		lcCli.carregaDados();
		lcContrato.carregaDados();
		lcItContrato.carregaDados();

		daoatend = new DAOAtendimento( cn );
		daoagenda = new DAOAgenda( cn );
		try {
			daoatend.setPrefs( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "SGPREFERE3" ) );
			prefs = daoatend.getPrefs();
		} catch (SQLException e) {
			Funcoes.mensagemErro( this, "Erro carregando preferências !\b" + e.getMessage() );
		}
		
		
	}

	private void sinalizaChamado( boolean em_atendimento, Integer codchamado ) {

		StringBuilder sql = new StringBuilder(); 

		try {

			if ( codchamado != null ) {

				sql.append( "update crchamado set ematendimento=? " );
				sql.append( "where codemp=? and codfilial=? and codchamado=? " );

				PreparedStatement ps = con.prepareStatement( sql.toString() );

				ps.setString( 1, em_atendimento ? "S" : "N" );

				ps.setInt( 2, lcChamado.getCodEmp() );
				ps.setInt( 3, lcChamado.getCodFilial() );

				ps.setInt( 4, codchamado );

				ps.executeUpdate();

				con.commit();
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	public void setContratos(){

		txtCodContr.setVlrInteger( txtCodContrCh.getVlrInteger() ); 
		txtCodItContr.setVlrInteger(txtCodItContrCh.getVlrInteger());
		lcContrato.carregaDados();
		lcItContrato.carregaDados();
	}

	public void afterCarrega( CarregaEvent cevt ) {

		Integer codsetat = null;
		if ( cevt.getListaCampos() == lcChamado ) { 

			sinalizaChamado( true, txtCodChamado.getVlrInteger() );
			// Guardando o chamado sinalizado
			codchamado_ant = txtCodChamado.getVlrInteger();

			if(txtCodItContrCh.getVlrInteger() > 0){
				setContratos();
			}

		} else if (cevt.getListaCampos() == lcEspec ){
			if( "S".equals( txtObrigProjEspec.getVlrString() ) ){
				txtCodContr.setRequerido( true );
				txtCodItContr.setRequerido( true );
			} else {
				txtCodContr.setRequerido( false );
				txtCodItContr.setRequerido( false );
			}
			if( "S".equals( txtObrigChamEspec.getVlrString() ) ){
				txtCodChamado.setRequerido( true );
			} else {
				txtCodChamado.setRequerido( false );
			}
		} else if (cevt.getListaCampos() == lcAtend ) {
			if ( "".equals(txtCodsetat.getVlrString().trim()) ) {
				codsetat = daoatend.locateSetor( 
						Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "ATSETOR" ), 
						Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "ATATENDENTE" ), 
						txtCodAtend.getVlrInteger() );
				if (codsetat!=null) {
					txtCodsetat.setVlrInteger( codsetat );
					lcSetor.carregaDados();
				}
			}
		} else if (cevt.getListaCampos() == lcContrato ) {
			loadSaldoContrato();
		} else if (cevt.getListaCampos() == lcCli) {
			lcOrc.carregaDados();
		} else if (cevt.getListaCampos() == lcOrc) {
			if (txtCodorc.getVlrInteger() >= 1 && 
					(Boolean) prefs[org.freedom.modulos.crm.business.object.Atendimento.PREFS.AGENDAOBRIGORC.ordinal()]
							&& !"AA".equals( cbSituacao.getVlrString())) {
				cbSituacao.setVlrString( "AG" );
				cbSituacao.setEnabled(false);
			} else {
				cbSituacao.setEnabled(true);
			}
		}
	}

	public void beforeCarrega( CarregaEvent cevt ) {

		if ( cevt.getListaCampos() == lcChamado ) {

			sinalizaChamado( false, codchamado_ant );
		}
	}

	public void keyTyped( KeyEvent e ) {
	}

	public void keyReleased( KeyEvent e ) {
	}

	public void valorAlterado( JComboBoxEvent evt ) {
		if (evt.getComboBoxPad() == cbSituacao) {
			if ("AG".equals(cbSituacao.getVlrString())) {
				btAgendar.setEnabled( true );
			} else {
				btAgendar.setEnabled( false );	
			}
		}
	}

}
