/**
 * @version 26/09/2011 <BR>
 * @author Setpoint Informática Ltda./Bruno Nascimento <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.crm <BR>
 *         Classe: @(#)FTarefa.java <BR>
 * 
 *         Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *         modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *         na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *         Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *         sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *         Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *         Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *         escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA <BR>
 * <BR>
 * 
 *         Tela de cadastro de Tarefas/subtarefas.
 * 
 */

package org.freedom.modulos.crm.view.frame.crud.plain;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import javax.swing.JScrollPane;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.RadioGroupEvent;
import org.freedom.acao.RadioGroupListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FTabDados;
import org.freedom.modulos.crm.dao.DAOGestaoProj;
import org.freedom.modulos.crm.view.frame.crud.detail.FContrato;

public class FTarefa extends FTabDados implements RadioGroupListener, InsertListener , CarregaListener, FocusListener{

	private static final long serialVersionUID = 1L;
	
	//paineis
	
	private JPanelPad pinTarefa = new JPanelPad();
	
	private JPanelPad pinPrevi = new JPanelPad(650, 90);
	
	private JPanelPad pnPrevi = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );
	
	private JTablePad tabPeriodo = new JTablePad();
	
	private JScrollPane scpPeriodo = new JScrollPane( tabPeriodo );
	
	private JPanelPad pnPreviCab = new JPanelPad( 650, 100 );
	
	private JPanelPad pnPreviCampos = new JPanelPad();
	
	private Navegador navPrevi = new Navegador( true );
	
	private JTextFieldPad txtCodTarefa= new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );
	
	private JTextFieldPad txtDescTarefa = new JTextFieldPad( JTextFieldPad.TP_STRING, 100, 0 );
	
	private JTextFieldPad txtCodTarefaPrinc= new JTextFieldPad( JTextFieldPad.TP_INTEGER, 6, 0 );
	
	private JTextFieldFK txtDescTarefaPrinc = new JTextFieldFK( JTextFieldPad.TP_STRING, 100, 0 );
	
	private JTextFieldPad txtTempoDecTarefa = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 10, 2 );
	
	private JTextFieldPad txtTempoEstTarefa = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );
	
	private JTextFieldPad txtIndexTarefa = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );
	
	private JCheckBoxPad cbLanctoTarefa = new JCheckBoxPad( "Lançamentos na tarefa?", "S", "N" );
	
	private JTextFieldPad txtCodTarefaPer = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );
	
	private JTextFieldPad txtAnoTPer = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );
	
	private JTextFieldPad txtMesTPer = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 2, 0 );
	
	private JTextFieldPad txtDtIniPer = new JTextFieldPad( JTextFieldPad.TP_DATE , 10, 0 );
	
	private JTextFieldPad txtDtFimPer = new JTextFieldPad( JTextFieldPad.TP_DATE , 10, 0 );
	
	private JTextFieldPad txtTempoDecTarefaPer = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 10, 2 );
	
	private JTextFieldPad txtCampoSomatorio = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 10, 2 );
	
	//FK
	
	private JTextFieldPad txtCodContr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 6, 0 );
	
	private JTextFieldFK txtDescContr = new JTextFieldFK( JTextFieldPad.TP_STRING, 100, 0 );
	
	private JTextFieldPad txtCodChamado = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 6, 0 );
	
	private JTextFieldFK txtDescChamado = new JTextFieldFK( JTextFieldPad.TP_STRING, 100, 0 );
	
	private JTextFieldPad txtCodItContr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 6, 0 );
	
	private JTextFieldFK txtDescItContr = new JTextFieldFK( JTextFieldPad.TP_STRING, 100, 0 );
	
	private JTextFieldPad txtCodMarcor= new JTextFieldPad( JTextFieldPad.TP_INTEGER, 6, 0 );

	private JTextFieldFK txtDescMarcor = new JTextFieldFK( JTextFieldPad.TP_STRING, 100, 0 );
	
	// Campos FK na aba Previsionamento.
	
	private JTextFieldFK txtCodTarefaPrev= new JTextFieldFK( JTextFieldFK.TP_INTEGER, 10, 0 );
	
	private JTextFieldFK txtDescTarefaPrev = new JTextFieldFK( JTextFieldFK.TP_STRING, 100, 0 );
	
	private JTextFieldFK txtTempoDecTarefaPrev = new JTextFieldFK( JTextFieldFK.TP_DECIMAL, 10, 2 );
	
	private JTextFieldFK txtTempoEstTarefaPrev = new JTextFieldFK( JTextFieldFK.TP_STRING, 10, 0 );
	
	private JTextFieldFK txtIndexTarefaPrev = new JTextFieldFK( JTextFieldFK.TP_INTEGER, 10, 0 );
	
	private JTextFieldFK txtTipoTarefa = new JTextFieldFK( JTextFieldFK.TP_STRING, 1, 0 );
	
	private JTextFieldFK txtLanctoTarefa = new JTextFieldFK( JTextFieldFK.TP_STRING, 1, 0 );
		
	//Lista Campos.
	
	private ListaCampos lcMarc = new ListaCampos( this, "MO" );
	
	private ListaCampos lcChamado = new ListaCampos( this, "CH" );
	
	private ListaCampos lcContrato = new ListaCampos( this, "CT" );
	
	private ListaCampos lcItContrato = new ListaCampos( this, "CT" );
	
	private ListaCampos lcSuperTarefa = new ListaCampos( this, "TA" );
	
	private ListaCampos lcTarefaPer = new ListaCampos( this );
	
	private ListaCampos lcTarefaPrev = new ListaCampos( this );
	                                                      
	private JTextAreaPad txaDescDetTarefa = new JTextAreaPad( 2000 );
	
	private JScrollPane scrol = new JScrollPane( txaDescDetTarefa );
	
	private JTextAreaPad txaNotasTarefa = new JTextAreaPad( 2000 );
	
	private JScrollPane scrol2 = new JScrollPane( txaNotasTarefa );
	
	private JRadioGroup<?, ?> rgTipoTarefa = null;
	
	private DAOGestaoProj daogestao = null;
	
	private Integer mes = null;
	
	private Integer ano = null;
		
	
	
	public FTarefa( ) {

		super();
		setTitulo( "Tarefas/SubTarefas" );
		setAtribos( 10, 50, 650, 560 );
		nav.setNavigation( true );
		
		lcTarefaPer.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcCampos );
		lcCampos.adicDetalhe( lcTarefaPer );
		lcTarefaPer.setTabela( tabPeriodo );
		carregaListener();
		montaListaCampos();
		montaTela();
	

	}
	
	public void getSaldo(){


	}
	
	public FTarefa(DbConnection cn, Integer codtarefa, Integer mes, Integer ano) {

		this();
		setConexao( cn );		
		txtCodTarefa.setVlrInteger( codtarefa );
		lcCampos.carregaDados();
		this.mes = mes;
		this.ano = ano;
	
	
	}
	
	public void loadPeriodoPrevi(){
		
		if( (txtTempoDecTarefa.getVlrBigDecimal().compareTo( new BigDecimal(0) ) == 0 )) {
			tpn.setSelectedIndex( 0 );
			txtTempoDecTarefa.requestFocus();
		} else {
			tpn.setSelectedIndex( 1 );
			txtMesTPer.setVlrInteger( mes );
			txtAnoTPer.setVlrInteger( ano );
			lcTarefaPer.carregaDados();
		}
	}

	private void montaListaCampos()  {	
		
		/**********************
		 * Marcador * *
		 *******************/
		txtCodMarcor.setTabelaExterna( lcMarc, null );
		txtCodMarcor.setFK( true );
		txtCodMarcor.setNomeCampo( "CodChamado" );
		lcMarc.add( new GuardaCampo( txtCodMarcor, "CodMarcor", "Cód.Marcador", ListaCampos.DB_PK, false ) );
		lcMarc.add( new GuardaCampo( txtDescMarcor, "DescMarcor", "Descrição do Marcador", ListaCampos.DB_SI, false ) );
		lcMarc.montaSql( false, "MARCADOR", "CR" );
		lcMarc.setReadOnly( true );
		
		/**********************
		 * Chamado   * *
		 *******************/
		txtCodChamado.setTabelaExterna( lcChamado, null );
		txtCodChamado.setFK( true );
		txtCodChamado.setNomeCampo( "CodChamado" );
		lcChamado.add( new GuardaCampo( txtCodChamado, "CodChamado", "Cód.Chamado", ListaCampos.DB_PK, false ) );
		lcChamado.add( new GuardaCampo( txtDescChamado, "DescChamado", "Descrição do chamado", ListaCampos.DB_SI, false ) );
		lcChamado.montaSql( false, "CHAMADO", "CR" );
		lcChamado.setReadOnly( true );
		
		/**********************
		 * Contrato   * *
		 *******************/
		lcContrato.add( new GuardaCampo( txtCodContr, "CodContr", "Cód.Contrato", ListaCampos.DB_PK, true ) );
		lcContrato.add( new GuardaCampo( txtDescContr, "DescContr", "Desc.Contr.", ListaCampos.DB_SI, false ) );
		lcContrato.montaSql( false, "CONTRATO", "VD" );
		lcContrato.setQueryCommit( false );
		lcContrato.setReadOnly( true );
		txtCodContr.setTabelaExterna( lcContrato, FContrato.class.getCanonicalName() );
		
		/**********************
		 * Item  Contrato   * *
		 *******************/
		
		lcItContrato.add( new GuardaCampo( txtCodItContr, "CodItContr", "Cód.It.Contr.", ListaCampos.DB_PK, true ) );
		lcItContrato.add( new GuardaCampo( txtCodContr, "CodContr", "Cód.Contrato", ListaCampos.DB_PK, true ) );
		lcItContrato.add( new GuardaCampo( txtDescItContr, "DescItContr", "Desc.It.Contr.", ListaCampos.DB_SI, false ) );
		lcItContrato.setDinWhereAdic( "CodContr=#N", txtCodContr );
		txtCodContr.setPK( true );
		lcItContrato.montaSql( false, "ITCONTRATO", "VD" );
		lcItContrato.setQueryCommit( false );
		lcItContrato.setReadOnly( true );
		txtCodItContr.setTabelaExterna( lcItContrato, FContrato.class.getCanonicalName() );		
			

		/**********************
		 * Tarefa PAI   * *
		 *******************/
		lcSuperTarefa.setQueryCommit( false );
		lcSuperTarefa.setReadOnly( true );
		lcSuperTarefa.add( new GuardaCampo( txtCodTarefaPrinc, "CodTarefa", "Cód.Tarefa", ListaCampos.DB_PK, txtDescTarefaPrinc, false ) );
		lcSuperTarefa.add( new GuardaCampo( txtDescTarefaPrinc, "DescTarefa" , "Descrição da tarefa", ListaCampos.DB_SI, false ) );
		txtCodTarefaPrinc.setNomeCampo( "CODTAREFA" );
		lcSuperTarefa.montaSql( false, "TAREFA", "CR" );
		txtCodTarefaPrinc.setTabelaExterna( lcSuperTarefa, null );
		txtCodTarefaPrinc.setFK( true );
		
		
		/**********************
		 * Tarefa Prev  * *
		 *******************/
		
		lcTarefaPrev.setMaster( lcCampos );
		lcTarefaPrev.add( new GuardaCampo( txtCodTarefaPrev, "CodTarefa", "Cód.Tarefa.", ListaCampos.DB_PK, txtDescTarefaPrev, false ) );
		lcTarefaPrev.add( new GuardaCampo( txtDescTarefaPrev, "DescTarefa" , "Descrição da tarefa", ListaCampos.DB_SI, false ) );
		lcTarefaPrev.add( new GuardaCampo( txtIndexTarefaPrev, "IndexTarefa" , "Índice da tarefa", ListaCampos.DB_SI, false ) );
		lcTarefaPrev.add( new GuardaCampo( txtTipoTarefa, "TipoTarefa" , "Tipo da tarefa", ListaCampos.DB_SI, false ) );
		lcTarefaPrev.add( new GuardaCampo( txtTempoDecTarefaPrev , "TempoDecTarefa" , "Tempo em decimal tarefa", ListaCampos.DB_SI, false ) );
		lcTarefaPrev.add( new GuardaCampo( txtLanctoTarefa, "LanctoTarefa" , "lancamento na tarefa?", ListaCampos.DB_SI, false ) );
		lcTarefaPrev.montaSql( false, "TAREFA", "CR" );
		lcTarefaPrev.setQueryCommit( false );
		lcTarefaPrev.setReadOnly( true );
		
	}
	
	private void montaTela(){
		

		setPainel( pinTarefa );
		adicTab( "Tarefa", pinTarefa );
		
		montaGrupoRadio();

		txtTempoEstTarefa.setEditable( false );
		txtTempoEstTarefa.setFocusable( false );
		txtCodTarefaPrinc.setEnabled( false );
		
		adicCampo( txtCodTarefa, 7, 20, 80, 20, "CodTarefa", "Cód.tarefa.", ListaCampos.DB_PK, true );
		adicCampo( txtDescTarefa, 90, 20, 520, 20, "DescTarefa", "Descrição da Tarefa", ListaCampos.DB_SI, true );
		
		adicDB( rgTipoTarefa, 7, 63, 200, 25, "TipoTarefa", "Tipo de tarefa",true );
		adicCampo( txtCodTarefaPrinc , 210, 63, 80, 20, "CodTarefata", "Cód.tarefa.Princ", ListaCampos.DB_FK, txtDescTarefaPrinc, false );
		adicDescFK( txtDescTarefaPrinc, 293, 63, 317, 20, "DescTarefa", "Descrição do projeto principal" );
		
		adicCampo( txtCodContr, 7, 110, 80, 20, "CodContr", "Cód.Contrato", ListaCampos.DB_FK, true );
		adicDescFK( txtDescContr, 90, 110, 520, 20, "DescContr", "Descrião do contrato" );
		adicCampo( txtCodItContr, 7, 150, 80, 20, "CodItContr", "Cód.It.Contr.", ListaCampos.DB_FK, txtDescItContr, true );
		adicDescFK( txtDescItContr, 90, 150, 467, 20, "DescItContr", "Descrição do item de contrato" );
		adicCampo( txtIndexTarefa, 560, 150, 50, 20, "IndexTarefa", "Índice", ListaCampos.DB_SI, true );
		adicCampo( txtCodChamado, 7, 190, 80, 20, "Codchamado", "Cód.Chamado", ListaCampos.DB_FK, txtDescChamado, false  );
		adicDescFK( txtDescChamado, 90, 190, 520, 20, "Descchamado", "Descrição do chamado" );
					
		adicCampo( txtCodMarcor, 7, 230, 80, 20, "CodMarcor", "Cód.Marcador", ListaCampos.DB_FK, txtDescMarcor, false  );
		adicDescFK( txtDescMarcor, 90, 230, 310, 20, "Descmarcor", "Descrição do marcador" );
		adicCampo( txtTempoDecTarefa, 403, 230, 102, 20, "TempoDecTarefa", "Tp.estimado dec.", ListaCampos.DB_SI, true );
		adicCampo( txtTempoEstTarefa, 508, 230, 102, 20, "TempoEstTarefa", "Tp.estimado", ListaCampos.DB_SI, false );
		//adicCampo( txtCampoSomatorio, 403, 260, 102, 20, "TempoDecTarefa", "Tp.estimado dec.", ListaCampos., false );
		adic( txtCampoSomatorio, 403, 270, 102, 20,"Saldo tarefa" );
		
		adicDB( cbLanctoTarefa, 7, 255, 300, 20, "LanctoTarefa", "", false );
		adicDB(txaDescDetTarefa, 7, 300, 603, 80, "DescDetTarefa", "Descrição Detalhada da tarefa", true);
		adicDB(txaNotasTarefa, 7, 400, 603, 80, "NotasTarefa", "Notas da tarefa", false);
		setListaCampos( lcCampos );
		setListaCampos( true, "TAREFA", "CR" );
		
		//Monta Grid de Previsionamento.		
		setPainel( pinPrevi, pnPrevi );
		adicTab( "Previsionamento", pnPrevi );
		setListaCampos( lcTarefaPer );
		
		pnPrevi.add( pnPreviCab, BorderLayout.NORTH );
		pnPrevi.add( pinPrevi, BorderLayout.SOUTH );
		pnPrevi.add( scpPeriodo, BorderLayout.CENTER );
		setNavegador( navPrevi );
		pinPrevi.adic( navPrevi, 0, 60, 270, 25 );
		
		//adicCampo( txtCodTarefaPer,  7, 20, 80, 20, "CodTarefa","CodTarefa",  ListaCampos.DB_PK, true );
		adicCampo( txtMesTPer, 7, 20,80, 20, "MesTPer", "Mês", ListaCampos.DB_PK, true );
		adicCampo( txtAnoTPer, 90, 20, 80, 20, "AnoTper", "Ano", ListaCampos.DB_PK, true );
		adicCampo( txtDtIniPer, 173, 20, 80, 20, "DtIniPer", "Data inicial", ListaCampos.DB_SI, true );
		adicCampo( txtDtFimPer, 256, 20, 80, 20, "DtFimPer", "Data final", ListaCampos.DB_SI, true );
		adicCampo( txtTempoDecTarefaPer, 339, 20, 80, 20, "TempoDecTarefa", "Tp.estimado dec.", ListaCampos.DB_SI, true );
		setListaCampos( true, "TAREFAPER", "CR" );
		lcTarefaPer.setOrdem( "AnoTPer" );
		lcTarefaPer.setQueryInsert( false );
		lcTarefaPer.setQueryCommit( false );
		lcTarefaPer.montaTab();
		tabPeriodo.setTamColuna( 70, 0 );
		tabPeriodo.setTamColuna( 70, 0 );
		tabPeriodo.setTamColuna( 70, 0 );
		tabPeriodo.setTamColuna( 70, 0 );		
		tabPeriodo.setTamColuna( 70, 0 );
		
		setPainel( pnPreviCab );
		
		adicDescFK( txtCodTarefaPrev, 7, 20, 80, 20, "CodTarefa", "Cód.tarefa" );
		adicDescFK( txtDescTarefaPrev, 90, 20, 520, 20, "DescTarefa", "Descrição da tarefa" );
		adicDescFK( txtTipoTarefa, 7, 60,80, 20, "TipoTarefa", "Tp.tarefa" );
		adicDescFK( txtTempoDecTarefaPrev, 90, 60, 80, 20, "TempoDecTarefa", "Tp.estimado Dec." );
		adicDescFK( txtIndexTarefaPrev, 173, 60, 80, 20, "IndexTarefa", "Index" );
		adicDescFK( txtLanctoTarefa, 256, 60, 80, 20, "LanctoTarefa", "Lancto.Tarefa" );
		
	}

	private void carregaListener(){
	
		lcCampos.setQueryInsert( false );
		lcCampos.addCarregaListener( this );
		lcItContrato.addCarregaListener( this );
		lcSuperTarefa.addCarregaListener( this );
		lcTarefaPer.addInsertListener( this );
		lcSuperTarefa.addInsertListener( this );
		lcCampos.addInsertListener( this );
		lcCampos.setQueryInsert( false );
		txtTempoDecTarefa.addFocusListener( this );
	
	}
	
	private void montaGrupoRadio(){
		
		Vector<String> vValsTipo = new Vector<String>();
		Vector<String> vLabsTipo = new Vector<String>();
		vValsTipo.addElement( "T" );
		vValsTipo.addElement( "S" );
		vLabsTipo.addElement( "Tarefa" );
		vLabsTipo.addElement( "Sub-tarefa" );
		rgTipoTarefa = new JRadioGroup<String, String>( 1, 2, vLabsTipo, vValsTipo );
		rgTipoTarefa.setVlrString( "T" );
		rgTipoTarefa.addRadioGroupListener( this );

	}

	public void valorAlterado(RadioGroupEvent evt){
		if (evt.getSource() == rgTipoTarefa){
			visualizarSuperProjeto("S".equals( rgTipoTarefa.getVlrString()));
		}
	}
	
	private void visualizarSuperProjeto(boolean subtarefa){
		this.txtCodTarefaPrinc.setEnabled( subtarefa );
		this.txtCodContr.setEnabled( !subtarefa );
		this.txtCodItContr.setEnabled( !subtarefa );
		this.cbLanctoTarefa.setEnabled( !subtarefa );
		if (!subtarefa){
			this.txtCodTarefaPrinc.setVlrString( "" );
			this.txtDescTarefaPrinc.setVlrString( "" );
		}
	}
	
	private void setSeqIndice(){
		try {
			if("T".equals( rgTipoTarefa.getVlrString() ) ) {
				txtIndexTarefa.setVlrInteger( daogestao.getNewIndiceItemTarefa( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "VDCONTRATO" ), txtCodContr.getVlrInteger(), txtCodItContr.getVlrInteger() ) );
			} else {
				txtIndexTarefa.setVlrInteger( daogestao.getNewIndiceItemSubTarefa( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "CRTAREFA" ), txtCodTarefaPrinc.getVlrInteger() ) );
			}
		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro ao buscar Indice do item do contrato!\n" + e.getMessage() );	
				e.printStackTrace();
		}
	}
	
	private void setContratos(){
		try {
			txtCodContr.setVlrInteger( daogestao.getCodContr( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "CRTAREFA" ), txtCodTarefaPrinc.getVlrInteger().intValue() ) );
			txtCodItContr.setVlrInteger( daogestao.getCodItContr( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "CRTAREFA" ), txtCodTarefaPrinc.getVlrInteger().intValue() ) );
			lcContrato.carregaDados();
			lcItContrato.carregaDados();
		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro ao buscar Código do Contrato!\n" + e.getMessage() );	
			e.printStackTrace();
		}
	}
	
	private void setPeriodoPrev(){
		
		Date dataini = null;
		Date datafim = null;
		dataini = Funcoes.getDataIniMes( ( txtMesTPer.getVlrInteger() ) -1, txtAnoTPer.getVlrInteger() );
		datafim =  Funcoes.getDataFimMes( ( txtMesTPer.getVlrInteger() ) -1, txtAnoTPer.getVlrInteger() );
		txtDtIniPer.setVlrDate( dataini );
		txtDtFimPer.setVlrDate( datafim );
		lcTarefaPrev.carregaDados();

	}
	
	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcCampos.setConexao( cn );
		lcSuperTarefa.setConexao( cn );
		lcChamado.setConexao( cn );
		lcContrato.setConexao( cn );
		lcItContrato.setConexao( cn );
		lcMarc.setConexao( cn );
		lcTarefaPer.setConexao( cn );
		lcTarefaPrev.setConexao( cn );
		
		daogestao = new DAOGestaoProj( cn );
	}
	
	public void afterCarrega( CarregaEvent cevt ) {

		if (cevt.getListaCampos()== lcItContrato) {
			if (lcCampos.getStatus()==ListaCampos.LCS_INSERT) { 
				setSeqIndice();
			}
		} else if (cevt.getListaCampos() == lcSuperTarefa){
			if ( (lcCampos.getStatus()==ListaCampos.LCS_INSERT) ||(lcCampos.getStatus() == ListaCampos.LCS_EDIT) ) { 
				setContratos();
			}
		} else if (cevt.getListaCampos() == lcCampos){
			visualizarSuperProjeto( "S".equals( rgTipoTarefa.getVlrString() ) );
			txtCodTarefaPrev.setVlrInteger( txtCodTarefa.getVlrInteger() );
			lcTarefaPrev.carregaDados();
		}
	
		
	}

	public void afterInsert( InsertEvent ievt ) {
		if(ievt.getListaCampos() == lcCampos){
			
			if (lcCampos.getStatus()==ListaCampos.LCS_INSERT) { 
				cbLanctoTarefa.setVlrString( "S" );
			}
		} else if ( ievt.getListaCampos() == lcTarefaPer) {
		
			if( txtAnoTPer.getVlrInteger() > 0 && txtMesTPer.getVlrInteger() > 0 ) {
					setPeriodoPrev();
			}
		}
	}


	public void beforeInsert( InsertEvent ievt ) {

	}


	public void beforeCarrega( CarregaEvent cevt ) {
		
	}


	public void focusGained( FocusEvent arg0 ) {

	}


	public void focusLost( FocusEvent arg0 ) {
		if (arg0.getSource()==txtTempoDecTarefa) {
			if (txtTempoDecTarefa.getVlrBigDecimal().doubleValue()>0) {
				txtTempoEstTarefa.setVlrString( Funcoes.convertBigDecimalStrTime( txtTempoDecTarefa.getVlrBigDecimal() ) );
			}
		}

		
	}
	
}