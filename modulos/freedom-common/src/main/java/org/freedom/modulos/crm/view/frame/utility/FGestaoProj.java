/**
 * @version 09/11/2011 <BR>
 * @author Setpoint Informática Ltda./Bruno Nascimento <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.crm.view.frame.utility; <BR>
 *         Classe: @(#)FGestaoProj.java <BR>
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
 *         Tela para gestão de projetos.
 * 
 */

package org.freedom.modulos.crm.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.library.swing.util.SwingParams;
import org.freedom.modulos.crm.business.component.Constant;
import org.freedom.modulos.crm.business.object.Contrato;
import org.freedom.modulos.crm.business.object.ContratoVW.EColContr;
import org.freedom.modulos.crm.dao.DAOGestaoProj;
import org.freedom.modulos.crm.view.frame.crud.detail.FContrato;
import org.freedom.modulos.crm.view.frame.crud.plain.FTarefa;

public class FGestaoProj extends FFilho implements CarregaListener, ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	
	//Paineis
	
	private JPanelPad pnDetail = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );
	
	private JPanelPad pnContr = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );
	
	private JTabbedPanePad tabAbas = new JTabbedPanePad();
	
	private JPanelPad pinCab = new JPanelPad( 700, 150 );
	
	private JPanelPad pinNav = new JPanelPad (  JPanelPad.TP_JPANEL, new GridLayout( 1, 2 ) );
	
	//Geral
	
	private JTablePad tabContr = new JTablePad();
	
	private JScrollPane scpContr = new JScrollPane( tabContr );
	
	private JPanelPad pnGridAtd = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JLabelPad lbStatus = new JLabelPad();
	
	private JLabelPad lbTpProj = new JLabelPad(); 
	
	private JTextFieldPad txtCodContr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );
	
	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 0, 10 );
	
	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 0, 10 );

	private JTextFieldFK txtDescContr = new JTextFieldFK( JTextFieldFK.TP_STRING, 80, 0 );
	
	private JTextFieldFK txtCodCli = new JTextFieldFK( JTextFieldFK.TP_INTEGER, 5, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldFK.TP_STRING, 60, 0 );

	private JTextFieldFK txtDtInicio = new JTextFieldFK( JTextFieldFK.TP_DATE, 10, 0 );

	private JTextFieldFK txtDtFim = new JTextFieldFK( JTextFieldFK.TP_DATE, 10, 0 );
	
	private JTextFieldFK txtTpContr = new JTextFieldFK( JTextFieldFK.TP_STRING, 1, 0 );
	
	private JTextFieldFK txtSitContr = new JTextFieldFK( JTextFieldFK.TP_STRING, 2, 0 );
	
	private JTextFieldFK txtContHSubContr = new JTextFieldFK( JTextFieldFK.TP_STRING, 2, 0 );
	
	private JTextFieldPad txtIndice = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );
		
	//CheckBox
	
	private final JCheckBoxPad cbTipoIC = new JCheckBoxPad( "Item ct.", "S", "N" );
	
	private final JCheckBoxPad cbTipoSC = new JCheckBoxPad( "Sub-ct.", "S", "N" );

	private final JCheckBoxPad cbTipoIS = new JCheckBoxPad( "Item s.c.", "S", "N" );

	private final JCheckBoxPad cbTipoTA = new JCheckBoxPad( "Tarefa", "S", "N" );
	
	private final JCheckBoxPad cbTipoST = new JCheckBoxPad( "Sub-tarefa", "S", "N" );
	
	//Botões

	private JButtonPad btEditar = new JButtonPad( Icone.novo( "btEditar.gif" ) );
	
	private JButtonPad btSair = new JButtonPad( "Sair", Icone.novo( "btSair.png" ) );

	private JButtonPad btImprimir = new JButtonPad( Icone.novo( "btPrevimp.png" ) );
	
	private JButtonPad  btPrimeiro = new JButtonPad( Icone.novo( "btPrim.png" ) );
	
	private JButtonPad 	btAnterior = new JButtonPad( Icone.novo( "btAnt.png" ) );
	
	private JButtonPad  btProximo= new JButtonPad( Icone.novo( "btProx.png" ) );
	
	private JButtonPad  btUltimo = new JButtonPad( Icone.novo( "btUlt.png" ) );
	
	private JButtonPad  btGerar = new JButtonPad( Icone.novo( "btGerar.png" ) );
	
	//Lista Campos
	
	private ListaCampos lcCliente = new ListaCampos( this, "CL" );
	
	private ListaCampos lcContrato = new ListaCampos( this );
	
	//DAOGestaoProj
	
	private DAOGestaoProj daogestao = null;
	
	private FContrato contr = null;
	
	private FTarefa tarefa = null;
	
	public FGestaoProj() {

		super( false );

		setTitulo( "Gestão de Projetos" );
		setAtribos( 20, 20, 780, 650 );
		montaListaCampos();
		montaTela();
		carregaListener();
		txtDatafim.setRequerido( true );
		txtDataini.setRequerido( true );
	}
	
	private void montaTela(){
		

		
		getTela().add( pnCliente , BorderLayout.CENTER );
		pnCliente.add( pinCab, BorderLayout.NORTH );
	
		
		// ***** Cabeçalho
		pinCab.adic( txtDataini, 7, 20, 80, 20, "Data Inicial" );
		pinCab.adic( txtDatafim, 90, 20, 80, 20, "Data Final");
		pinCab.adic( txtCodContr, 173, 20, 80, 20, "Cód.proj" );
		txtCodContr.setFK( true );
		txtCodContr.setNomeCampo( "CodContr" );
		pinCab.adic( txtDescContr, 256, 20, 490, 20, "Descrição do contrato/projeto" );
		pinCab.adic( txtCodCli, 7,60 , 80, 20, "Cód.cli."  );
		pinCab.adic( txtRazCli, 90, 60, 492, 20, "Descrição do cliente" );
		pinCab.adic( txtDtInicio, 585, 60, 80, 20, "Dt.ini." );
		pinCab.adic( txtDtFim, 668, 60, 80, 20, "Dt.fin." );
		pinCab.adic( lbTpProj, 7, 100, 100, 20, "Tipo do projeto" );
		pinCab.adic( lbStatus, 110, 100, 100, 20, "Status");
		pinCab.adic( txtContHSubContr, 213, 100, 200, 20,"Contabiliza horas no sub-contratos"  );
		pinCab.adic( txtIndice, 416, 100, 80,20, "Indice" );
		pinCab.adic( cbTipoIC, 7, 123, 80, 20);
		pinCab.adic( cbTipoSC, 90, 123, 80, 20);
		pinCab.adic( cbTipoIS, 173, 123, 80, 20);
		pinCab.adic( cbTipoTA, 256, 123, 80, 20);
		pinCab.adic( cbTipoST, 339, 123, 90, 20);
		
		pinCab.adic( btGerar, 716, 110, 30, 30 );

		// ***** Grid

		pnCliente.add( pnDetail, BorderLayout.CENTER );
		pnDetail.add( tabAbas );
		tabAbas.addTab( "Contratos", pnContr );
		// tabbedDetail.addTab( "Receber", panelReceber );
		// tabbedDetail.addTab( "Histórico", panelHistorico );

		// ***** Venda
		montaGridContr();
		
		
		pnContr.add( scpContr, BorderLayout.CENTER );
		
		// ***** Rodapé
		
		pnCliente.add( pnRodape, BorderLayout.SOUTH );
		adicNavegador();
		pnRodape.add( adicBotaoSair() );
		setNaoSalvo();
		
		colocaMes();
		
		cbTipoIC.setVlrString( "S" );
		cbTipoIS.setVlrString( "S" );
		cbTipoSC.setVlrString( "S" );
		cbTipoTA.setVlrString( "S" );
		cbTipoST.setVlrString( "S" );
		
	}
	

	
	private void montaGridContr(){
		
		tabContr.adicColuna( "Tipo" );
		tabContr.adicColuna( "Indice" );
		tabContr.adicColuna( "Descrição" );
		tabContr.adicColuna( "Chamado" );
		tabContr.adicColuna( "Prev.Total" );
		tabContr.adicColuna( "Real.ant." );
		tabContr.adicColuna( "Saldo.ant." );
		tabContr.adicColuna( "Previsão" );
		tabContr.adicColuna( "Realizado" );
		tabContr.adicColuna( "Saldo" );
		tabContr.adicColuna( "Idx" );
		tabContr.adicColuna( "Cód.contr." );
		tabContr.adicColuna( "Cód.sub-contr." );
		tabContr.adicColuna( "Cod.it.contr." );
		tabContr.adicColuna( "Cód.tarefa" );
		tabContr.adicColuna( "Cód.sub-tarefa" );
 
		tabContr.setTamColuna( 30, EColContr.TIPO.ordinal() );
		tabContr.setTamColuna( 70, EColContr.INDICE.ordinal() );
		tabContr.setTamColuna( 400, EColContr.DESCRICAO.ordinal() );
		tabContr.setTamColuna( 60, EColContr.CHAMADO.ordinal() );
		tabContr.setTamColuna( 60, EColContr.TOTALPREVGERAL.ordinal() );
		tabContr.setTamColuna( 60, EColContr. TOTALCOBCLIANT.ordinal() );
		tabContr.setTamColuna( 60, EColContr.SALDOANT.ordinal() );
		tabContr.setTamColuna( 60, EColContr.TOTALPREVPER.ordinal() );
		tabContr.setTamColuna( 60, EColContr.TOTALCOBCLIPER.ordinal() );
		tabContr.setTamColuna( 60, EColContr.SALDOPER.ordinal() );
		tabContr.setTamColuna( 30, EColContr.IDX.ordinal() );
		tabContr.setTamColuna( 70, EColContr.CODCONTR.ordinal() );
		tabContr.setTamColuna( 70, EColContr.CODCONTRSC.ordinal() );
		tabContr.setTamColuna( 70, EColContr.CODITCONTR.ordinal() );
		tabContr.setTamColuna( 70, EColContr.CODTAREFA.ordinal() );
		tabContr.setTamColuna( 70, EColContr.CODTAREFAST.ordinal() );
		
	}
	
	private void montaListaCampos(){
		
		
		/*************
		 * CLIENTE * *
		 **********/
		lcCliente.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCliente.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		txtCodCli.setTabelaExterna( lcCliente, null );
		txtCodCli.setNomeCampo( "CodCli" );
		txtCodCli.setFK( true );
		lcCliente.setReadOnly( true );
		lcCliente.montaSql( false, "CLIENTE", "VD" );
		
		/***************
		 * 	CONTRATO  * *
		 **************/

		lcContrato.add( new GuardaCampo( txtCodContr, "CodContr", "Cód.Contr.", ListaCampos.DB_PK, true ) );
		lcContrato.add( new GuardaCampo( txtDescContr, "DescContr", "Descrição do contrato", ListaCampos.DB_SI, false ) );
		lcContrato.add( new GuardaCampo( txtDtInicio, "DtInicio", "Dt.Ini.", ListaCampos.DB_SI, false ) );
		lcContrato.add( new GuardaCampo( txtDtFim, "DtFim", "Dt.Fim", ListaCampos.DB_SI, false ) );
		lcContrato.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.Cli.", ListaCampos.DB_FK, txtRazCli, false ) );
		lcContrato.add( new GuardaCampo( txtTpContr, "TpContr", "Tp.Contr.", ListaCampos.DB_SI, false ) );
		lcContrato.add( new GuardaCampo( txtSitContr, "SitContr", "Sit.Contr.", ListaCampos.DB_SI, false ) );
		lcContrato.add( new GuardaCampo( txtContHSubContr, "ContHSubContr", "Cont.HSubContr.", ListaCampos.DB_SI, false ) );
		lcContrato.setDinWhereAdic( " TPCONTR NOT IN('S') ", txtTpContr);
		txtCodContr.setTabelaExterna( lcContrato, FContrato.class.getCanonicalName() );
		lcContrato.setReadOnly( true );
		lcContrato.montaSql( false, "CONTRATO", "VD" );
		
	}
	
	private void setNaoSalvo(){
		
		lbStatus.setForeground( Color.WHITE );
		lbStatus.setBackground( Color.BLACK );
		lbStatus.setFont( SwingParams.getFontboldmed() );
		lbStatus.setHorizontalAlignment( SwingConstants.CENTER );
		lbStatus.setOpaque( true );
		lbStatus.setText( "NÃO SALVO" );
		
		lbTpProj.setForeground( Color.WHITE );
		lbTpProj.setBackground( Color.BLACK );
		lbTpProj.setFont( SwingParams.getFontboldmed() );
		lbTpProj.setHorizontalAlignment( SwingConstants.CENTER );
		lbTpProj.setOpaque( true );
		lbTpProj.setText( "NÃO SALVO" );
	}
	
	private void carregaListener(){
		
		lcContrato.addCarregaListener( this );
		btGerar.addActionListener( this );
		btAnterior.addActionListener( this );
		btPrimeiro.addActionListener( this );
		btProximo.addActionListener( this );
		btUltimo.addActionListener( this );
		btEditar.addActionListener( this );
		tabContr.addKeyListener(this);
	}

	private void adicNavegador(){
	
		pnRodape.add( pinNav, BorderLayout.WEST );
		
		pinNav.setPreferredSize( new Dimension( 200, 30 ) );
		pinNav.add( btPrimeiro );
		pinNav.add( btAnterior );
		pinNav.add( btProximo );
		pinNav.add( btUltimo );
		pinNav.add( btEditar );
		pinNav.add( btImprimir );

		btEditar.setToolTipText( "Editar" );
		btImprimir.setToolTipText( "Imprimir" );
	}
	
	private void colocaMes() {

		Date cData = new Date();
		GregorianCalendar cDataIni = new GregorianCalendar();
		GregorianCalendar cDataFim = new GregorianCalendar();
		cDataIni.set( Calendar.DATE, 1 );
		cDataFim.set( Calendar.MONTH, cDataIni.MONTH + 2 );
		cDataFim.set( Calendar.DATE, 0 );
		

		txtDataini.setVlrDate( cDataIni.getTime() );
		txtDatafim.setVlrDate( cDataFim.getTime() );

	}

	
	private void setSitcontr() {
		String statusProj = txtSitContr.getVlrString().trim();
		Vector<Constant> listaSit = Contrato.getListSitproj();
		Constant item = null;
		for (int i=0; i<listaSit.size(); i++) {
			item = listaSit.elementAt( i );
			if (statusProj.equals( item.getValue())) {
				lbStatus.setBackground( item.getBgcolor() );
				lbStatus.setForeground( item.getFgcolor() );
				lbStatus.setText( item.getName() );
				break;
			}
		}
	}
	
	private void setTpProjcontr() {
		String statusProj = txtTpContr.getVlrString().trim();
		Vector<Constant> listaSit = Contrato.getListTpproj();
		Constant item = null;
		for (int i=0; i<listaSit.size(); i++) {
			item = listaSit.elementAt( i );
			if (statusProj.equals( item.getValue())) {
				lbTpProj.setBackground( item.getBgcolor() );
				lbTpProj.setForeground( item.getFgcolor() );
				lbTpProj.setText( item.getName() );
				break;
			}
		}
	}
	
	private void primeiro(JTablePad panel) {

		if ( ( panel != null ) & ( panel.getNumLinhas() > 0 ) )
			panel.setLinhaSel( 0 );
	}

	private void anterior(JTablePad panel) {

		int iLin = 0;
		if ( ( panel != null ) & ( panel.getNumLinhas() > 0 ) ) {
			iLin = panel.getLinhaSel();
			if ( iLin > 0 )
				panel.setLinhaSel( iLin - 1 );
		}
	}

	private void proximo(JTablePad panel) {

		int iLin = 0;
		if ( ( panel != null ) & ( panel.getNumLinhas() > 0 ) ) {
			iLin = panel.getLinhaSel();
			if ( iLin < ( panel.getNumLinhas() - 1 ) )
				panel.setLinhaSel( iLin + 1 );
		}
	}

	private void ultimo(JTablePad panel) {

		if ( ( panel != null ) & ( panel.getNumLinhas() > 0 ) )
			panel.setLinhaSel( panel.getNumLinhas() - 1 );
	}
	

	
	public void setConexao( DbConnection cn ) {
		
		super.setConexao( cn );
		lcCliente.setConexao( cn );
		lcContrato.setConexao( cn );
		
		daogestao = new DAOGestaoProj( cn );
	
	}
	
	public void afterCarrega( CarregaEvent cevt ) {
		
		if (cevt.getListaCampos()== lcContrato) {
			if("".equals( txtCodContr.getText() ) ) {
				setNaoSalvo();
				
			} else {
				setSitcontr();
				setTpProjcontr();
				tabContr.limpa();
			}
		}
	}

	public void beforeCarrega( CarregaEvent cevt ) {
		
	}
	
	private void loadContr(){
		try {
		/*
			Vector<Vector<Object>> datavector = daogestao.loadContr( txtDataini.getVlrDate(), txtDatafim.getVlrDate(), 
					Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "VDCONTRATO" ), 
					txtCodContr.getVlrInteger(), txtContHSubContr.getVlrString() );
			tabContr.limpa();
			
			for(Vector<Object> row : datavector){
				tabContr.adicLinha( row );
			}
		*/
		org.freedom.infra.pojos.Constant[] filtroTipo = DAOGestaoProj.getFiltroTipo( "N", cbTipoIC.getVlrString(), cbTipoSC.getVlrString(), 
					cbTipoIS.getVlrString(), cbTipoTA.getVlrString(), cbTipoST.getVlrString() );
	
			
		Vector<Vector<Object>> datavector = daogestao.loadContr( txtDataini.getVlrDate(), txtDatafim.getVlrDate(), 
				Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "VDCONTRATO" ), 
				txtCodContr.getVlrInteger(), txtContHSubContr.getVlrString(), txtIndice.getVlrString(), filtroTipo );
		
		tabContr.setDataVector( datavector );	
			
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro carregando grid de contratos !\b" + err.getMessage() );
			err.printStackTrace();
		}
	}
	
	private void abreContr() {
		Integer codcontr =  ( (Integer) tabContr.getValor( tabContr.getLinhaSel(), EColContr.CODCONTRSC.ordinal() ) );
		contr = new FContrato( con, codcontr );
		fPrim.criatela( "Projetos/Contratos", contr , con );
	}
	
	private void abreItContr(String tipo) {
		Integer codcontr = null;
		Integer coditcontr = null; 
		if("IC".equals( tipo ) ){
			codcontr =  ( (Integer) tabContr.getValor( tabContr.getLinhaSel(), EColContr.CODCONTR.ordinal() ) );
			coditcontr =  ( (Integer) tabContr.getValor( tabContr.getLinhaSel(), EColContr.CODITCONTR.ordinal() ) );
		} else if("IS".equals( tipo ) ){
			codcontr =  ( (Integer) tabContr.getValor( tabContr.getLinhaSel(), EColContr.CODCONTRSC.ordinal() ) );
			coditcontr =  ( (Integer) tabContr.getValor( tabContr.getLinhaSel(), EColContr.CODITCONTR.ordinal() ) );
		}
		contr = new FContrato( con, codcontr, coditcontr );
		fPrim.criatela( "Projetos/Contratos", contr , con );
	}
	
	private void abreTarefa(String tipo){
		Integer codtarefa = null;
		if("TA".equals( tipo ) ){
			codtarefa =  ( (Integer) tabContr.getValor( tabContr.getLinhaSel(), EColContr.CODTAREFA.ordinal() ) );
		} else if("ST".equals( tipo )) {
			codtarefa =  ( (Integer) tabContr.getValor( tabContr.getLinhaSel(), EColContr.CODTAREFAST.ordinal() ) );
		}
		tarefa = new FTarefa( con, codtarefa,  Funcoes.getMes( txtDataini.getVlrDate() ),Funcoes.getAno( txtDataini.getVlrDate() ) );
		fPrim.criatela( "Tarefa", tarefa , con );
		tarefa.loadPeriodoPrevi();
	}
	
	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btGerar ) {
			if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
				Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			}
			loadContr();
		}
		else if ( evt.getSource() == btPrimeiro ) {
    		primeiro(tabContr);
    	}
		else if ( evt.getSource() == btAnterior ) {
			anterior(tabContr);
		}
		else if ( evt.getSource() == btProximo ) {
			proximo(tabContr);
		}
		else if ( evt.getSource() == btUltimo ) {
			ultimo(tabContr);
		}
		else if ( evt.getSource() == btEditar ) {
			String tipo =( (String) tabContr.getValor( tabContr.getLinhaSel(), EColContr.TIPO.ordinal() ) ); 
			
			if( "SC".equals(tipo) ){
				abreContr();
			} else if ( ( "IS".equals( tipo ) ) || "IC".equals( tipo ) ) {
				abreItContr(tipo);
			} else if ( ( "TA".equals( tipo ) ) || ( "ST".equals( tipo ) ) ){
				abreTarefa(tipo);
			} 
	
		}
	}

	public void keyTyped( KeyEvent kevt ) {

		
	}

	public void keyPressed( KeyEvent kevt ) {

		if ( kevt.getKeyCode() == KeyEvent.VK_SPACE) {
			if ( kevt.getSource() == tabContr ) {
				String tipo =( (String) tabContr.getValor( tabContr.getLinhaSel(), EColContr.TIPO.ordinal() ) ); 
				
				if( "SC".equals(tipo) ){
					abreContr();
				} else if ( ( "IS".equals( tipo ) ) || "IC".equals( tipo ) ) {
					abreItContr(tipo);
				} else if ( ( "TA".equals( tipo ) ) || ( "ST".equals( tipo ) ) ){
					abreTarefa(tipo);
				}
			}
		}
		
	}

	public void keyReleased( KeyEvent e ) {

		
	}
	
}
