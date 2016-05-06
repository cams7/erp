/**
 * @version 30/09/2011 <BR>
 * @author Setpoint Informática Ltda./Bruno Nascimento <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.gpe.view.frame.crud.plain <BR>
 *         Classe:
 * @(#)FFALTA.java <BR>
 * 
 *                Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                Formulário para cadastro de Falta no sistema.
 * 
 */

package org.freedom.modulos.gpe.view.frame.crud.plain;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JScrollPane;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.acao.RadioGroupEvent;
import org.freedom.acao.RadioGroupListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.crm.business.object.Atendimento.PREFS;
import org.freedom.modulos.crm.dao.DAOAtendimento;
import org.freedom.modulos.grh.view.frame.crud.plain.FTurnos;
import org.freedom.modulos.grh.view.frame.crud.tabbed.FEmpregado;

public class FFalta extends FDados implements InsertListener, KeyListener, PostListener, FocusListener, CarregaListener, RadioGroupListener  {

	private static final long serialVersionUID = 1L;

	private final JTextFieldPad txtMatempr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 6, 0 );
	
	private final JTextFieldFK txtNomeempr = new JTextFieldFK( JTextFieldFK.TP_STRING, 50, 0);
	
	private final JTextFieldPad txtDtFalta = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtHIniFalta = new JTextFieldPad( JTextFieldPad.TP_TIME, 5, 0 );

	private final JTextFieldPad txtHFinFalta = new JTextFieldPad( JTextFieldPad.TP_TIME, 5, 0 );
	
	private final JTextFieldPad txtHIniIntFalta = new JTextFieldPad( JTextFieldPad.TP_TIME, 5, 0 );
	
	private final JTextFieldPad txtHFinIntFalta = new JTextFieldPad( JTextFieldPad.TP_TIME, 5, 0 );
	
	//FK
	private final JTextFieldFK txtCodTurno = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldFK txtDescTurno = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private final JTextFieldFK txtHIniTurno = new JTextFieldFK( JTextFieldPad.TP_TIME, 8, 0 );

	private final JTextFieldFK txtHFimTurno = new JTextFieldFK( JTextFieldPad.TP_TIME, 8, 0 );

	private final JTextFieldFK txtHIniIntTurno = new JTextFieldFK( JTextFieldPad.TP_TIME, 8, 0 );

	private final JTextFieldFK txtHFimIntTurno = new JTextFieldFK( JTextFieldPad.TP_TIME, 8, 0 );
	
	private JTextFieldPad txtCodAtend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtNomeAtend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldPad txtCodAtendo = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextAreaPad txaJustificativa = new JTextAreaPad( 2000 );
	private JScrollPane scrol = new JScrollPane( txaJustificativa );
	
	private final Vector<String> labels = new Vector<String>();
	
	private final Vector<String> values = new Vector<String>();

	private JRadioGroup<String, String>  rgPeriodo = null;
	
	private final Vector<String> vLabsTipo = new Vector<String>();
	
	private final Vector<String> vValsTipo = new Vector<String>();

	private JRadioGroup<String, String> rgTipoFalta = null;
	
	private final ListaCampos lcEmpr = new ListaCampos(this, "EP");
	
	private ListaCampos lcAtend = new ListaCampos( this, "EP" );
	
	private ListaCampos lcAtendimento = new ListaCampos( this, "AE" );
	
	private final ListaCampos lcTurno = new ListaCampos(this);
	
	private int LCS_STATUS = ListaCampos.LCS_NONE;
 	
	private DAOAtendimento daoatend;
	
	public FFalta() {

		super();
		setTitulo( "Cadastro de Falta" );
		setAtribos( 50, 50, 600, 350 );
		
		montaRadioGrupos();
		montaListaCampos();
		montaTela();


		
		setImprimir( true );
	}

	private void montaTela() {

		nav.setNavigation( false );
		txtCodTurno.setSoLeitura( true );
		txtDescTurno.setSoLeitura( true );
		txtHFimTurno.setSoLeitura( true );
		txtHIniTurno.setSoLeitura( true );
		txtHIniIntTurno.setSoLeitura( true );
		txtHFimIntTurno.setSoLeitura( true );
		
		adicCampo( txtDtFalta, 7, 20, 90, 20, "DtFalta", "Data Falta", ListaCampos.DB_PK, true );
		adicCampo( txtMatempr, 100, 20, 70, 20, "Matempr", "Matrícula", ListaCampos.DB_PF, txtNomeempr, true );
		adicDescFK( txtNomeempr, 173, 20, 343, 20, "Nomeempr", "Nome" );
		
		adic( txtCodTurno, 7, 63, 90, 20 , "Cód.Turno", false);
		adic( txtDescTurno, 100, 63, 230, 20, "Descrição do Turno", false);
		adic( txtHIniTurno, 333, 63, 90, 20, "Início Turno", false);
		adic( txtHFimTurno, 426, 63, 90, 20, "Final do Turno", false);
				
		adicDB( rgPeriodo, 7, 106, 250, 30, "Periodofalta", "Período", false );
		adicDB( rgTipoFalta, 265,106, 250, 30, "TipoFalta", "Tipo de batida", false );
		
		adicCampo( txtHIniFalta, 7, 160, 100, 20, "HIniFalta", "Horário de inicio", ListaCampos.DB_SI, true);
		adicCampo( txtHIniIntFalta, 143, 160, 100, 20, "HIniIntFalta", "Ini. Intervaldo", ListaCampos.DB_SI, true);
		adicCampo( txtHFinIntFalta, 276, 160, 100, 20, "HFinIntFalta", "Fin. Intervaldo", ListaCampos.DB_SI, true);
		adicCampo( txtHFinFalta, 409, 160, 100, 20, "HFinFalta", "Horário Final", ListaCampos.DB_SI, true);
		
		adicDB(txaJustificativa, 7, 203, 509, 50, "Justiffalta", "Justificativa",  false);
		
		setListaCampos( true, "FALTA", "PE" );
		lcCampos.setQueryInsert( false );
		
		adicListeners();
		
	}
	
	private void adicListeners() {
		
		lcCampos.addInsertListener( this );
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		rgPeriodo.addRadioGroupListener( this );
		
	}
	
	public void montaRadioGrupos(){
		
		labels.addElement( "Integral" );
		labels.addElement( "Meio" );
		values.addElement( "I" );
		values.addElement( "M" );
		rgPeriodo = new JRadioGroup<String, String>( 1, 2, labels, values );
		rgPeriodo.setEnabled( false );
		
		vLabsTipo.addElement( "Justificado" );
		vLabsTipo.addElement( "Injustificado" );
		vValsTipo.addElement("J");
		vValsTipo.addElement("I");
		rgTipoFalta = new JRadioGroup<String, String>( 1, 2,  vLabsTipo, vValsTipo );
		rgTipoFalta.setEnabled( false );
		
	}

	private void montaListaCampos() {
		
		/**********************
		 * EMPREGADO * *
		 *******************/
		
		lcEmpr.add( new GuardaCampo( txtMatempr, "Matempr", "Matrícula", ListaCampos.DB_PK, true ) );
		lcEmpr.add( new GuardaCampo( txtNomeempr, "Nomeempr", "Nome", ListaCampos.DB_SI, false ) );
		lcEmpr.add( new GuardaCampo( txtCodTurno, "Codturno", "Turno", ListaCampos.DB_FK, false ) );
		lcEmpr.montaSql( false, "EMPREGADO", "RH" );
		lcEmpr.setQueryCommit( false );
		lcEmpr.setReadOnly( true );
		txtMatempr.setTabelaExterna( lcEmpr, FEmpregado.class.getCanonicalName() );
		txtMatempr.setFK(true);
		
		/**********************
		 * TURNO * *
		 *******************/
		
		lcTurno.add( new GuardaCampo( txtCodTurno, "CodTurno", "Cód.Turno", ListaCampos.DB_PK, false ) );
		lcTurno.add( new GuardaCampo( txtDescTurno, "DescTurno", "Descrição do Turno", ListaCampos.DB_SI, false ) );
		lcTurno.add( new GuardaCampo( txtHIniTurno, "HIniTurno", "Início Turno", ListaCampos.DB_SI, false ) );
		lcTurno.add( new GuardaCampo( txtHIniIntTurno, "HIniIntTurno", "Início de intervalo", ListaCampos.DB_SI, false ) );
		lcTurno.add( new GuardaCampo( txtHFimIntTurno, "HFimIntTurno", "Final do Intervalor", ListaCampos.DB_SI, false ) );
		lcTurno.add( new GuardaCampo( txtHFimTurno, "HFimTurno", "Final do Turno", ListaCampos.DB_SI, false ) );
		lcTurno.montaSql( false, "TURNO", "RH" );
		lcTurno.setQueryCommit( false );
		lcTurno.setReadOnly( true );
		txtCodTurno.setTabelaExterna( lcTurno, FTurnos.class.getCanonicalName() );
		txtCodTurno.setFK( true );
		
		/**********************
		 * Atendente * *
		 *******************/
		
		
		lcAtend.add( new GuardaCampo( txtCodAtend, "CodAtend", "Cód.atend.", ListaCampos.DB_PK, false ) );
		lcAtend.add( new GuardaCampo( txtNomeAtend, "NomeAtend", "Nome", ListaCampos.DB_SI, false ) );
		lcAtend.add( new GuardaCampo( txtMatempr, "Matempr", "Matrícula", ListaCampos.DB_FK, false ) );
		lcAtend.montaSql( false, "ATENDENTE", "AT" );
		txtCodAtend.setTabelaExterna( lcAtend, null );
		lcAtend.setQueryCommit( false );
		lcAtend.setReadOnly( true );
		
		/*
		lcAtendimento.add( new GuardaCampo( txtCodAtendo, "CodAtendo", "Cód.atendo", ListaCampos.DB_PK, false ) );
		lcAtendimento.add( new GuardaCampo( txtCodAtend, "CodAtend", "Cod.Atend.", ListaCampos.DB_FK, false ) );
		lcAtendimento.montaSql( false, "ATENDIMENTO", "AT" );
		lcAtendimento.setReadOnly( true );
		txtCodAtendo.setNomeCampo( "CodAtendo" );
		txtCodAtendo.setFK( true );
		txtCodAtendo.setTabelaExterna( lcAtendimento, null );
		*/
		
	}
	private void imprimir( TYPE_PRINT bVisualizar ) {

	}

	public void afterInsert( InsertEvent ievt ) {

		if (ievt.getListaCampos()==lcCampos) {
			rgPeriodo.setVlrString( "I" );
			rgTipoFalta.setVlrString( "J" );
			loadRangeTurno();
			enableDisablePeriodo();
		}
		
	}

	private void loadRangeTurno() {
		txtHIniFalta.setVlrTime( txtHIniTurno.getVlrTime( ) );
		txtHIniIntFalta.setVlrTime( txtHIniIntTurno.getVlrTime() );
		if ( "M".equals(rgPeriodo.getVlrString())) {
			txtHFinIntFalta.setVlrTime( txtHFimTurno.getVlrTime() );
			txtHFinFalta.setVlrTime( txtHFimTurno.getVlrTime() );
		} else {
			txtHFinIntFalta.setVlrTime( txtHFimIntTurno.getVlrTime() );
			txtHFinFalta.setVlrTime( txtHFimTurno.getVlrTime() );
		}
	}
	
	public void beforeInsert( InsertEvent ievt ) {

	}
		
	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		
		lcEmpr.setConexao( cn );
		lcTurno.setConexao( cn );
		lcAtendimento.setConexao( cn );
		lcAtend.setConexao( cn );
		lcAtend.carregaDados();
		
		daoatend = new DAOAtendimento( cn );
		try {
			daoatend.setPrefs( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "SGPREFERE3" ) );
		} catch (SQLException e) {
			Funcoes.mensagemErro( this, "Erro carregando preferências !\b" + e.getMessage() );
		}
		
	}
	public void keyPressed( KeyEvent kevt ) {

		
	}
	
	private void insertFaltaInjustificada( String horaini, String horafin ) {
		txtCodAtend.setVlrInteger( daoatend.getAtendente( txtMatempr.getVlrInteger() ) );
		
		try{
			if( daoatend.getPrefs()[PREFS.CODMODELFI.ordinal()] != null ) {
				daoatend.insertFaltaInjustificada( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "ATATENDIMENTO" ), 
						txtDtFalta.getVlrDate(), txtDtFalta.getVlrDate(),
						horaini,  horafin, 
						Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "ATATENDENTE" ), txtCodAtend.getVlrInteger(), 
						Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "SGUSUARIO" ), Aplicativo.getUsuario().getIdusu() );
			}
		} catch (Exception e) {
			Funcoes.mensagemErro( this, "Erro inserindo lançamento automatizado de Falta injustificada !\n" + e.getMessage() );
			e.printStackTrace();
		}	

	}
	
	private void insertFaltaJustificada( String horaini, String horafin ) {
		
		txtCodAtend.setVlrInteger( daoatend.getAtendente( txtMatempr.getVlrInteger() ) );
		
		try {
				if ( daoatend.getPrefs()[PREFS.CODMODELFJ.ordinal()] != null )  {
				daoatend.insertFaltaJustificada( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "ATATENDIMENTO" ), 
						txtDtFalta.getVlrDate(), txtDtFalta.getVlrDate(),
						horaini,  horafin, 
						Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "ATATENDENTE" ), txtCodAtend.getVlrInteger(), 
						Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "SGUSUARIO" ), Aplicativo.getUsuario().getIdusu() );
				}	 
				
		} catch (Exception e) {
			Funcoes.mensagemErro( this, "Erro inserindo lançamento automatizado de intervalo !\n" + e.getMessage() );
			e.printStackTrace();
		}	

	}

	public void afterPost(PostEvent pevt){	
		if( pevt.getListaCampos() == lcCampos){
			if( LCS_STATUS == ListaCampos.LCS_INSERT ){
				if("J".equals( rgTipoFalta.getVlrString() ) ) { 
				   insertFaltaJustificada(  txtHIniFalta.getVlrString(), txtHIniIntFalta.getVlrString() );
				   if("I".equals( rgPeriodo.getVlrString() ) ) {
					   insertFaltaJustificada( txtHFinIntFalta.getVlrString(), txtHFinFalta.getVlrString() );
				   }
				}
				else if( "I".equals( rgTipoFalta.getVlrString() ) ) {
				   insertFaltaInjustificada( txtHIniFalta.getVlrString(), txtHIniIntFalta.getVlrString() );
				   if("I".equals( rgPeriodo.getVlrString() ) ) {
						insertFaltaInjustificada( txtHFinIntFalta.getVlrString(), txtHFinFalta.getVlrString() );
				   }
				}
			}
		}
	}
	
	public void beforePost(PostEvent bevt){
		
		if(bevt.getListaCampos() == lcCampos){
			
			LCS_STATUS = lcCampos.getStatus();
		}
	}
	
	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
		else if ( evt.getSource() == btImp ) {
			imprimir( TYPE_PRINT.PRINT);
		}
		super.actionPerformed( evt );
	}
	
	public void focusGained( FocusEvent arg0 ) {

	}
	
	public void valorAlterado( FocusEvent fevt ) {
		
	}
	
	public void focusLost( FocusEvent fevt ) {
		
	}

	public void beforeCarrega( CarregaEvent cevt ) {

		
	}

	public void afterCarrega( CarregaEvent cevt ) {
		if (cevt.getListaCampos()==lcCampos) {
			//enableDisablePeriodo();
		}
	}
	
	private void enableDisablePeriodo() {
		if ( "M".equals( rgPeriodo.getVlrString() ) ) {
			txtHFinIntFalta.setEnabled( false );
			txtHFinFalta.setEnabled( false );
		} else {
			txtHFinIntFalta.setEnabled( true );
			txtHFinFalta.setEnabled( true );
		}
	}

	public void valorAlterado( RadioGroupEvent evt ) {
		if ( evt.getSource()==rgPeriodo ) {
			loadRangeTurno();
			enableDisablePeriodo();
		}
		
	}
}
