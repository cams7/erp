/**
 * @version 06/07/2011 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.crm.frame.utility <BR>
 *         Classe: @(#)FConsisteCRM.java <BR>
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
 *         Consistência e geração automatizada de lançamentos
 * 
 */

package org.freedom.modulos.crm.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import org.freedom.bmps.Icone;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
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
import org.freedom.modulos.crm.business.component.EstagioCheck;
import org.freedom.modulos.crm.business.object.Atendimento.EColAtend;
import org.freedom.modulos.crm.business.object.Atendimento.EColExped;
import org.freedom.modulos.crm.dao.DAOAtendimento;
import org.freedom.modulos.gpe.business.object.Batida;
import org.freedom.modulos.gpe.dao.DAOBatida;

public class FConsisteCRM extends FFilho implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCliente = new JPanelPad( 600, 123 );

	private JPanelPad pnGrid = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 2, 1 ) );

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 0, 10 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 0, 10 );

	private JCheckBoxPad cbEntrada = new JCheckBoxPad( "Entrada", "S", "N" );

	private JCheckBoxPad cbSaida = new JCheckBoxPad( "Saida", "S", "N" );

	private JTablePad tabexped = null;

	private JScrollPane spnTabexped = null;
	
	private JTablePad tabatend = null;

	private JScrollPane spnAtend = null;

	private JProgressBar pbAnd = new JProgressBar();

	private JButtonPad btVisual = new JButtonPad( Icone.novo( "btPesquisa.png" ) );

	private JButtonPad btChecar = new JButtonPad( Icone.novo( "btExecuta.png" ) );

	private JButtonPad btGerar = new JButtonPad( Icone.novo( "btGerar.png" ) );
	
	private ListaCampos lcAtend = new ListaCampos( this, "AE" );

	private JTextFieldPad txtCodAtend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtNomeAtend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private ListaCampos lcEmpregado = new ListaCampos( this, "EP" );
	
	private JTextFieldPad txtMatempr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeempr = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private ListaCampos lcTurno = new ListaCampos( this, "TO" );
	
	private JTextFieldPad txtCodTurno = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTurno = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtTotInconsistencia = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0);
	
	private Timer tim = null;

	private int andamento = 0;
	
	private int nbatidas = -1;
	
	private DAOAtendimento daoatend = null;
	
	DAOBatida daobatida = null;


	private JLabelPad lbAnd = new JLabelPad( "Aguardando:" );

	public FConsisteCRM() {
		super( false );
		montaListaCampos();
		montaTela();
	}

	private void montaTela() {
		// Carrega imagens para constantes de controle de estágio de consistência.
		try {
			for (EstagioCheck estagio: EstagioCheck.getListEstagio()) {
				estagio.setImg( Icone.novo(estagio.getValue()+".png") );
			}
		} catch (Exception e) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Não foi possível carregar imagens para as tabelas !\n" + e.getMessage() );
		}
		
		setTitulo( "Gerar Informações Fiscais" );
		setAtribos( 10, 10, 670, 500 );

		btVisual.setToolTipText( "Visualizar dados" );
		btChecar.setToolTipText( "Executar checagem" );
		btGerar.setToolTipText( "Gerar correções" );

		btChecar.setEnabled( false );
		btGerar.setEnabled( false );

		Container c = getContentPane();
		c.setLayout( new BorderLayout() );

		adicBotaoSair();

		c.add( pinCliente, BorderLayout.NORTH );
		c.add( pnGrid, BorderLayout.CENTER );

		txtDataini.setRequerido( true );
		txtDatafim.setRequerido( true );

		txtMatempr.setAtivo( false );
		txtCodTurno.setAtivo( false );
		txtTotInconsistencia.setAtivo( false );

		pinCliente.adic( new JLabelPad( "Início" ), 7, 0, 110, 25 );
		pinCliente.adic( txtDataini, 7, 20, 110, 20 );
		pinCliente.adic( new JLabelPad( "Fim" ), 120, 0, 107, 25 );
		pinCliente.adic( txtDatafim, 120, 20, 107, 20 );
		pinCliente.adic( new JLabelPad("Cód.atend."), 230, 0, 90, 25);
		pinCliente.adic( txtCodAtend, 230, 20, 90, 20 );
		pinCliente.adic( new JLabelPad("Nome do atendente"), 323, 0, 300, 25 );
		pinCliente.adic( txtNomeAtend, 323, 20, 300, 20 );
		pinCliente.adic( new JLabelPad("Matrícula"), 7, 40, 110, 25);
		pinCliente.adic( txtMatempr, 7, 60, 110, 20 );
		pinCliente.adic( new JLabelPad("Nome do colaborador"), 120, 40, 200, 25 );
		pinCliente.adic( txtNomeempr, 120, 60, 200, 20 );
		pinCliente.adic( new JLabelPad("Cód.turno"), 323, 40, 90, 25);
		pinCliente.adic( txtCodTurno, 323, 60, 90, 20 );
		pinCliente.adic( new JLabelPad("Descrição do turno"), 416, 40, 207, 25 );
		pinCliente.adic( txtDescTurno, 416, 60, 207, 20 );
		
		pinCliente.adic( btVisual, 7, 83, 30, 30 );
		pinCliente.adic( btChecar, 40, 83, 30, 30 );
		pinCliente.adic( btGerar, 73, 83, 30, 30 );
		
		pinCliente.adic( new JLabelPad("Inconsistências"), 120, 76, 100, 25 );
		pinCliente.adic( txtTotInconsistencia, 120, 96, 100, 20 );
		
		//pinCliente.adic( cbEntrada, 7, 50, 150, 20 );
		//pinCliente.adic( cbSaida, 170, 50, 150, 20 );
		//pinCliente.adic( lbAnd, 7, 80, 110, 20 );
		//pinCliente.adic( pbAnd, 120, 80, 210, 20 );


        prepTabexped(nbatidas);
        if (nbatidas==-1) {
        	nbatidas = 0;
        }
        prepTabatend();

        pnGrid.add( spnTabexped );
		pnGrid.add( spnAtend );
     
		colocaMes();
		btVisual.addActionListener( this );
		btChecar.addActionListener( this );
		btGerar.addActionListener( this );

		pbAnd.setStringPainted( true );
		
	}

	private void montaListaCampos() {

		txtCodAtend.setTabelaExterna( lcAtend, null );
		txtCodAtend.setFK( true );
		txtCodAtend.setNomeCampo( "CodAtend" );
		lcAtend.add( new GuardaCampo( txtCodAtend, "CodAtend", "Cód.atend.", ListaCampos.DB_PK, false ), "txtCodAtendx" );
		lcAtend.add( new GuardaCampo( txtNomeAtend, "NomeAtend", "Nome", ListaCampos.DB_SI, false ), "txtNomeAtendx" );
		lcAtend.add( new GuardaCampo( txtMatempr, "Matempr", "Matrícula", ListaCampos.DB_FK, false), "txtMatemprx" );
		lcAtend.montaSql( false, "ATENDENTE", "AT" );
		lcAtend.setReadOnly( true );

		txtMatempr.setTabelaExterna( lcEmpregado, null );
		txtMatempr.setFK( true );
		txtMatempr.setNomeCampo( "Matempr" );
		lcEmpregado.add( new GuardaCampo( txtMatempr, "Matempr", "Matrícula", ListaCampos.DB_PK, false ), "txtMatemprx" );
		lcEmpregado.add( new GuardaCampo( txtNomeempr, "Nomeempr", "Nome do colaborador", ListaCampos.DB_SI, false ), "txtNomeemprx" );
		lcEmpregado.add( new GuardaCampo( txtCodTurno, "Codturno", "Cód.turno", ListaCampos.DB_FK, false), "txtCodturnox" );
		lcEmpregado.montaSql( false, "EMPREGADO", "RH" );
		lcEmpregado.setReadOnly( true );
		
		txtCodTurno.setTabelaExterna( lcTurno, null );
		txtCodTurno.setFK( true );
		txtCodTurno.setNomeCampo( "CodTurno" );
		lcTurno.add( new GuardaCampo( txtCodTurno, "CodTurno", "Cód.turno", ListaCampos.DB_PK, false ), "txtCodTurnox" );
		lcTurno.add( new GuardaCampo( txtDescTurno, "DescTurno", "Descrição do turno", ListaCampos.DB_SI, false ), "txtDescTurnox" );
		lcTurno.montaSql( false, "TURNO", "RH" );
		lcTurno.setReadOnly( true );
		
	}

	public void setConexao( DbConnection cn ) {
		super.setConexao( cn );
		lcAtend.setConexao( cn );
		lcEmpregado.setConexao( cn );
		lcTurno.setConexao( cn );
						
		daoatend = new DAOAtendimento( cn );
		try {
			daoatend.setPrefs( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "SGPREFERE3" ) );
		} catch (SQLException e) {
			Funcoes.mensagemErro( this, "Erro carregando preferências !\b" + e.getMessage() );
		}

		daobatida = new DAOBatida( cn );
		try {
			daobatida.setPrefs( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "SGPREFERE3" ) );
		} catch (SQLException e) {
			Funcoes.mensagemErro( this, "Erro carregando preferências !\b" + e.getMessage() );
		}
		
	}
	
	private void colocaMes() {

		GregorianCalendar cData = new GregorianCalendar();
		GregorianCalendar cDataIni = new GregorianCalendar();
		GregorianCalendar cDataFim = new GregorianCalendar();
		cDataIni.set( Calendar.MONTH, cData.get( Calendar.MONTH ) - 1 );
		cDataIni.set( Calendar.DATE, 1 );
		cDataFim.set( Calendar.DATE, -1 );
		txtDataini.setVlrDate( cDataIni.getTime() );
		txtDatafim.setVlrDate( cDataFim.getTime() );

	}

	public void iniGerar() {

		Thread th = new Thread( new Runnable() {

			public void run() {

				gerar();
			}
		} );
		try {
			th.start();
		} catch ( Exception err ) {
			Funcoes.mensagemErro( this, "Não foi possível criar processo!\n" + err.getMessage(), true, con, err );
		}
	}

	private ImageIcon getImgSitrevatendo(String sitrevatendo) {
		ImageIcon result = null;
		result = EstagioCheck.getImg( sitrevatendo );
		return result;
	}
	
	private void prepTabexped(int nbatidas) {
		this.nbatidas = nbatidas;
		int numcols = 0;
		int qtdant = 0;
		if (nbatidas==-1) {
			tabexped =  new JTablePad();
			tabexped.addMouseListener( this );
			spnTabexped =  new JScrollPane( tabexped );

			tabexped.adicColuna( "Sit.img." );
			tabexped.adicColuna( "Sit.r." );
			tabexped.adicColuna( "Dt.exped." );
			tabexped.adicColuna( "C. horária" );
			tabexped.adicColuna(  "H.ini.turno" );
			tabexped.adicColuna( "H.ini.interv." );
			tabexped.adicColuna( "H.fim interv." );
			tabexped.adicColuna( "H.fim turno" );
			tabexped.setTamColuna( 30, EColExped.SITREVEXPEDIMG.ordinal() );
			tabexped.setTamColuna( 30, EColExped.SITREVEXPED.ordinal() );
			tabexped.setTamColuna( 70, EColExped.DTEXPED.ordinal() );
			tabexped.setTamColuna( 70, EColExped.HORASEXPED.ordinal() );
			tabexped.setTamColuna( 60, EColExped.HINITURNO.ordinal() );
			tabexped.setTamColuna( 60, EColExped.HINIINTTURNO.ordinal() );
			tabexped.setTamColuna( 60, EColExped.HFIMINTTURNO.ordinal() );
			tabexped.setTamColuna( 60, EColExped.HFIMTURNO.ordinal() );
		} else {
			tabexped.limpa();
			if ( (EColExped.HFIMTURNO.ordinal() + nbatidas ) > tabexped.getNumColunas() ) {
				numcols = EColExped.HFIMTURNO.ordinal() + nbatidas;
				qtdant = tabexped.getNumColunas();
				for (int i=qtdant; i<=numcols ; i++ ) {
	            	tabexped.adicColuna( "H.Ponto " + ( i - EColExped.HFIMTURNO.ordinal() ) );
	            	tabexped.setTamColuna( 60, EColExped.HFIMTURNO.ordinal() + i );
	            }
			}
			if (EColExped.HFIMTURNO.ordinal() + nbatidas + 4 > tabexped.getNumColunas() ) {
				qtdant = tabexped.getNumColunas();
				numcols = qtdant + 4;
				for (int i=qtdant; i<numcols ; i++ ) {
	            	tabexped.adicColuna( "H.Corrigir " + ( i - qtdant + 1 ) );
	            	tabexped.setTamColuna( 60, EColExped.HFIMTURNO.ordinal() + i );
	            }
			}
		}
	}

	private void prepTabatend() {

		tabatend =  new JTablePad();
		spnAtend =  new JScrollPane( tabatend );
        
		tabatend.adicColuna( "Cód.at." );
		tabatend.adicColuna( "Sit.img." );
		tabatend.adicColuna( "Sit.r." );
		tabatend.adicColuna( "Data" );
		tabatend.adicColuna( "H.início" );
		tabatend.adicColuna( "H.final" );
		tabatend.adicColuna( "H.bat" );
		tabatend.adicColuna( "I.F.turno" );
		tabatend.adicColuna( "Int.min." );
		tabatend.adicColuna( "Qtd.h." );
		tabatend.adicColuna( "Qtd.min." );
		tabatend.adicColuna( "Cód.espec." );
		tabatend.adicColuna( "Descrição da especificação" );
		tabatend.adicColuna( "Cód.model." );
		tabatend.adicColuna( "Descrição do modelo de atendimento" );
		tabatend.adicColuna( "H.iníc." );
		tabatend.adicColuna( "H.fin." );
		
		tabatend.setTamColuna( 50, EColAtend.CODATENDO.ordinal() );
		tabatend.setTamColuna( 30, EColAtend.SITREVATENDOIMG.ordinal() );
		tabatend.setTamColuna( 30, EColAtend.SITREVATENDO.ordinal() );
		tabatend.setTamColuna( 80, EColAtend.DATAATENDO.ordinal() );
		tabatend.setTamColuna( 70, EColAtend.HORAATENDO.ordinal() );
		tabatend.setTamColuna( 70, EColAtend.HORAATENDOFIN.ordinal() );
		tabatend.setTamColuna( 70, EColAtend.HORABATIDA.ordinal() );
		tabatend.setTamColuna( 70, EColAtend.INIFINTURNO.ordinal() );
		tabatend.setTamColuna( 70, EColAtend.INTERVATENDO.ordinal() );
		tabatend.setTamColuna( 70, EColAtend.TOTALGERAL.ordinal() );
		tabatend.setTamColuna( 70, EColAtend.TOTALMIN.ordinal() );
		tabatend.setTamColuna( 70, EColAtend.CODESPEC.ordinal() );
		tabatend.setTamColuna( 250, EColAtend.DESCESPEC.ordinal() );
		tabatend.setTamColuna( 70, EColAtend.CODMODEL.ordinal() );
		tabatend.setTamColuna( 250, EColAtend.DESCMODEL.ordinal() );
		tabatend.setTamColuna( 70, EColAtend.HORAINI.ordinal() );
		tabatend.setTamColuna( 70, EColAtend.HORAFIN.ordinal() );
		
	}
	
	private void visualizarExped() {
		StringBuffer sqlexped = new StringBuffer();
		StringBuffer sqlcount = new StringBuffer();
		StringBuffer sqlbatidas = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement psbat = null;
		ResultSet rsbat = null;
		int param = 1;
		int totexped = 0;
		int col = 0;
		try {
			
			sqlcount.append( "SELECT B.DTBAT, COUNT(*) QTD FROM PEBATIDA B "); 
			sqlcount.append( "WHERE B.CODEMP=? AND B.CODFILIAL=? AND ");
			sqlcount.append( "B.CODEMPEP=? AND B.CODFILIALEP=? AND B.MATEMPR=? AND ");
			sqlcount.append( "B.DTBAT BETWEEN ? AND ? ");
			sqlcount.append( "GROUP BY B.DTBAT ");
			sqlcount.append( "UNION ALL ");
			sqlcount.append( "SELECT DTFALTA DTBAT, COUNT(*) QTD ");
			sqlcount.append( "FROM PEFALTA F "); 
			sqlcount.append( "WHERE F.CODEMP=? AND F.CODFILIAL=? AND ");
			sqlcount.append( "F.CODEMPEP=? AND F.CODFILIALEP=? AND F.MATEMPR=? AND ");
			sqlcount.append( "F.DTFALTA BETWEEN ? AND ? ");
			sqlcount.append( "GROUP BY F.DTFALTA ");
			sqlcount.append( "UNION ALL ");
			sqlcount.append( "SELECT DTFALTA DTBAT, COUNT(*) QTD ");
			sqlcount.append( "FROM PEFALTA F "); 
			sqlcount.append( "WHERE F.CODEMP=? AND F.CODFILIAL=? AND ");
			sqlcount.append( "F.CODEMPEP=? AND F.CODFILIALEP=? AND F.MATEMPR=? AND ");
			sqlcount.append( "F.DTFALTA BETWEEN ? AND ? ");
			sqlcount.append( "GROUP BY F.DTFALTA ");
			sqlcount.append( "UNION ALL ");
			sqlcount.append( "SELECT DTFALTA DTBAT, COUNT(*) QTD ");
			sqlcount.append( "FROM PEFALTA F "); 
			sqlcount.append( "WHERE F.CODEMP=? AND F.CODFILIAL=? AND ");
			sqlcount.append( "F.CODEMPEP=? AND F.CODFILIALEP=? AND F.MATEMPR=? AND ");
			sqlcount.append( "F.DTFALTA BETWEEN ? AND ? AND ");
			sqlcount.append( "F.PERIODOFALTA='I' ");
			sqlcount.append( "GROUP BY F.DTFALTA ");
			sqlcount.append( "UNION ALL ");
			sqlcount.append( "SELECT DTFALTA DTBAT, COUNT(*) QTD ");
			sqlcount.append( "FROM PEFALTA F "); 
			sqlcount.append( "WHERE F.CODEMP=? AND F.CODFILIAL=? AND ");
			sqlcount.append( "F.CODEMPEP=? AND F.CODFILIALEP=? AND F.MATEMPR=? AND ");
			sqlcount.append( "F.DTFALTA BETWEEN ? AND ? AND ");
			sqlcount.append( "F.PERIODOFALTA='I' ");
			sqlcount.append( "GROUP BY F.DTFALTA ");
			sqlcount.append( "ORDER BY 2 DESC" );
 		    psbat = con.prepareStatement( sqlcount.toString() );
 		 	
 		    param = 1;
 		    psbat.setInt( param++, Aplicativo.iCodEmp );
 		    psbat.setInt( param++, ListaCampos.getMasterFilial( "PEBATIDA" ) );
 		    psbat.setInt( param++, Aplicativo.iCodEmp );
 		    psbat.setInt( param++, ListaCampos.getMasterFilial( "RHEMPREGADO" ) );
 		    psbat.setInt( param++, txtMatempr.getVlrInteger().intValue() );
			psbat.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			psbat.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

 		    psbat.setInt( param++, Aplicativo.iCodEmp );
 		    psbat.setInt( param++, ListaCampos.getMasterFilial( "PEFALTA" ) );
 		    psbat.setInt( param++, Aplicativo.iCodEmp );
 		    psbat.setInt( param++, ListaCampos.getMasterFilial( "RHEMPREGADO" ) );
 		    psbat.setInt( param++, txtMatempr.getVlrInteger().intValue() );
			psbat.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			psbat.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

 		    psbat.setInt( param++, Aplicativo.iCodEmp );
 		    psbat.setInt( param++, ListaCampos.getMasterFilial( "PEFALTA" ) );
 		    psbat.setInt( param++, Aplicativo.iCodEmp );
 		    psbat.setInt( param++, ListaCampos.getMasterFilial( "RHEMPREGADO" ) );
 		    psbat.setInt( param++, txtMatempr.getVlrInteger().intValue() );
			psbat.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			psbat.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

 		    psbat.setInt( param++, Aplicativo.iCodEmp );
 		    psbat.setInt( param++, ListaCampos.getMasterFilial( "PEFALTA" ) );
 		    psbat.setInt( param++, Aplicativo.iCodEmp );
 		    psbat.setInt( param++, ListaCampos.getMasterFilial( "RHEMPREGADO" ) );
 		    psbat.setInt( param++, txtMatempr.getVlrInteger().intValue() );
			psbat.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			psbat.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

 		    psbat.setInt( param++, Aplicativo.iCodEmp );
 		    psbat.setInt( param++, ListaCampos.getMasterFilial( "PEFALTA" ) );
 		    psbat.setInt( param++, Aplicativo.iCodEmp );
 		    psbat.setInt( param++, ListaCampos.getMasterFilial( "RHEMPREGADO" ) );
 		    psbat.setInt( param++, txtMatempr.getVlrInteger().intValue() );
			psbat.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			psbat.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			
			rsbat = psbat.executeQuery();
			Date dtbatcount=null;
			int countbat=0;
			while ( rsbat.next() ) {
				// Só ajustar a quantidade de batidas se a pesquisa retornar um valor maior
				if ( (dtbatcount==null) || (!dtbatcount.equals( rsbat.getDate( "dtbat" ) ) ) ) {
					countbat = 0;
					dtbatcount=rsbat.getDate( "dtbat" );
				}
				countbat += rsbat.getInt( "QTD" ); 
				if (countbat>nbatidas) {
					nbatidas = countbat;
				}
			}
			rsbat.close();
			psbat.close();
			con.commit();
			
			sqlexped.append("SELECT E.DTEXPED, E.HORASEXPED, T.HINITURNO, T.HINIINTTURNO, T.HFIMINTTURNO, T.HFIMTURNO ");
			sqlexped.append("FROM RHEXPEDIENTE E, RHTURNO T ");
			sqlexped.append("WHERE E.CODEMP=? AND E.CODFILIAL=? AND E.CODTURNO=? AND ");
			sqlexped.append("E.DTEXPED BETWEEN ? AND ? AND ");
			sqlexped.append( "T.CODEMP=E.CODEMP AND T.CODFILIAL=E.CODFILIAL AND T.CODTURNO=E.CODTURNO ");
			sqlexped.append("ORDER BY E.DTEXPED");
			
 		    ps = con.prepareStatement( sqlexped.toString() );
 	
 		    ps.setInt( 1, Aplicativo.iCodEmp );
 		    ps.setInt( 2, ListaCampos.getMasterFilial( "RHEXPEDIENTE" ) );
 		    ps.setInt( 3, txtCodTurno.getVlrInteger().intValue() );
			ps.setDate( 4, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( 5, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			rs = ps.executeQuery();
			prepTabexped( nbatidas );

			sqlbatidas.append( "SELECT B.DTBAT, B.HBAT, CAST('BT' AS CHAR(2)) TIPOBAT FROM PEBATIDA B "); 
			sqlbatidas.append( "WHERE B.CODEMP=? AND B.CODFILIAL=? AND ");
			sqlbatidas.append( "B.CODEMPEP=? AND B.CODFILIALEP=? AND B.MATEMPR=? AND ");
			sqlbatidas.append( "B.DTBAT = ? ");
			sqlbatidas.append( "UNION ALL ");
			sqlbatidas.append( "SELECT DTFALTA DTBAT, F.HINIFALTA HBAT, ");
			sqlbatidas.append( "CAST( 'F'||F.TIPOFALTA AS CHAR(2)) TIPOBAT FROM PEFALTA F "); 
			sqlbatidas.append( "WHERE F.CODEMP=? AND F.CODFILIAL=? AND ");
			sqlbatidas.append( "F.CODEMPEP=? AND F.CODFILIALEP=? AND F.MATEMPR=? AND ");
			sqlbatidas.append( "F.DTFALTA = ? ");
			sqlbatidas.append( "UNION ALL ");
			sqlbatidas.append( "SELECT DTFALTA DTBAT, F.HINIINTFALTA HBAT, ");
			sqlbatidas.append( "CAST( 'F'||F.TIPOFALTA AS CHAR(2)) TIPOBAT FROM PEFALTA F "); 
			sqlbatidas.append( "WHERE F.CODEMP=? AND F.CODFILIAL=? AND ");
			sqlbatidas.append( "F.CODEMPEP=? AND F.CODFILIALEP=? AND F.MATEMPR=? AND ");
			sqlbatidas.append( "F.DTFALTA = ? ");
			sqlbatidas.append( "UNION ALL ");
			sqlbatidas.append( "SELECT DTFALTA DTBAT, F.HFININTFALTA HBAT, ");
			sqlbatidas.append( "CAST( 'F'||F.TIPOFALTA AS CHAR(2)) TIPOBAT FROM PEFALTA F "); 
			sqlbatidas.append( "WHERE F.CODEMP=? AND F.CODFILIAL=? AND ");
			sqlbatidas.append( "F.CODEMPEP=? AND F.CODFILIALEP=? AND F.MATEMPR=? AND ");
			sqlbatidas.append( "F.DTFALTA = ? AND F.PERIODOFALTA='I' ");
			sqlbatidas.append( "UNION ALL ");
			sqlbatidas.append( "SELECT DTFALTA DTBAT, F.HFINFALTA HBAT, ");
			sqlbatidas.append( "CAST( 'F'||F.TIPOFALTA AS CHAR(2)) TIPOBAT FROM PEFALTA F "); 
			sqlbatidas.append( "WHERE F.CODEMP=? AND F.CODFILIAL=? AND ");
			sqlbatidas.append( "F.CODEMPEP=? AND F.CODFILIALEP=? AND F.MATEMPR=? AND ");
			sqlbatidas.append( "F.DTFALTA = ? AND F.PERIODOFALTA='I' ");
			sqlbatidas.append( "ORDER BY 1, 2" ); 
			
			while ( rs.next() ) {
				tabexped.adicLinha();
				tabexped.setValor( getImgSitrevatendo( (String) EstagioCheck.EPE.getValue() ), totexped, EColExped.SITREVEXPEDIMG.ordinal() );
				tabexped.setValor( ( (String) EstagioCheck.EPE.getValue()).substring(1) , totexped, EColExped.SITREVEXPED.ordinal() );
				tabexped.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( EColExped.DTEXPED.toString() ) ), totexped, EColExped.DTEXPED.ordinal() );
				tabexped.setValor( new Integer(rs.getInt( EColExped.HORASEXPED.toString() ) ), totexped, EColExped.HORASEXPED.ordinal() );
				tabexped.setValor( Funcoes.copy( rs.getTime( EColExped.HINITURNO.toString() ).toString() ,5 ) , totexped, EColExped.HINITURNO.ordinal() );
				tabexped.setValor( Funcoes.copy( rs.getTime( EColExped.HINIINTTURNO.toString() ).toString(),5 ) , totexped, EColExped.HINIINTTURNO.ordinal() );
				tabexped.setValor( Funcoes.copy( rs.getTime( EColExped.HFIMINTTURNO.toString() ).toString(), 5) , totexped, EColExped.HFIMINTTURNO.ordinal() );
				tabexped.setValor( Funcoes.copy( rs.getTime( EColExped.HFIMTURNO.toString().toString() ).toString(),5 ) , totexped, EColExped.HFIMTURNO.ordinal() );
	 		    psbat = con.prepareStatement( sqlbatidas.toString() );
	 		    param = 1;
	 		    psbat.setInt( param++, Aplicativo.iCodEmp );
	 		    psbat.setInt( param++, ListaCampos.getMasterFilial( "PEBATIDA" ) );
	 		    psbat.setInt( param++, Aplicativo.iCodEmp );
	 		    psbat.setInt( param++, ListaCampos.getMasterFilial( "RHEMPREGADO" ) );
	 		    psbat.setInt( param++, txtMatempr.getVlrInteger().intValue() );
				psbat.setDate( param++, rs.getDate(EColExped.DTEXPED.toString() ) );
				
	 		    psbat.setInt( param++, Aplicativo.iCodEmp );
	 		    psbat.setInt( param++, ListaCampos.getMasterFilial( "PEFALTA" ) );
	 		    psbat.setInt( param++, Aplicativo.iCodEmp );
	 		    psbat.setInt( param++, ListaCampos.getMasterFilial( "RHEMPREGADO" ) );
	 		    psbat.setInt( param++, txtMatempr.getVlrInteger().intValue() );
				psbat.setDate( param++, rs.getDate(EColExped.DTEXPED.toString() ) );
				
	 		    psbat.setInt( param++, Aplicativo.iCodEmp );
	 		    psbat.setInt( param++, ListaCampos.getMasterFilial( "PEFALTA" ) );
	 		    psbat.setInt( param++, Aplicativo.iCodEmp );
	 		    psbat.setInt( param++, ListaCampos.getMasterFilial( "RHEMPREGADO" ) );
	 		    psbat.setInt( param++, txtMatempr.getVlrInteger().intValue() );
				psbat.setDate( param++, rs.getDate(EColExped.DTEXPED.toString() ) );

	 		    psbat.setInt( param++, Aplicativo.iCodEmp );
	 		    psbat.setInt( param++, ListaCampos.getMasterFilial( "PEFALTA" ) );
	 		    psbat.setInt( param++, Aplicativo.iCodEmp );
	 		    psbat.setInt( param++, ListaCampos.getMasterFilial( "RHEMPREGADO" ) );
	 		    psbat.setInt( param++, txtMatempr.getVlrInteger().intValue() );
				psbat.setDate( param++, rs.getDate(EColExped.DTEXPED.toString() ) );
				
	 		    psbat.setInt( param++, Aplicativo.iCodEmp );
	 		    psbat.setInt( param++, ListaCampos.getMasterFilial( "PEFALTA" ) );
	 		    psbat.setInt( param++, Aplicativo.iCodEmp );
	 		    psbat.setInt( param++, ListaCampos.getMasterFilial( "RHEMPREGADO" ) );
	 		    psbat.setInt( param++, txtMatempr.getVlrInteger().intValue() );
				psbat.setDate( param++, rs.getDate(EColExped.DTEXPED.toString() ) );
				
				rsbat = psbat.executeQuery();
				col = EColExped.HFIMTURNO.ordinal() + 1;
				while ( rsbat.next() && col<tabexped.getNumColunas() ) {
					tabexped.setValor( Funcoes.copy( rsbat.getTime( "HBAT" ).toString(),5 )+
							rsbat.getString( "TIPOBAT" ) , totexped, col);
					col ++;
				}
				//Inserir string vazia nas colunas para evitar problemas.
				for (int i=col; i<tabexped.getNumColunas(); i++) {
					tabexped.setValor( "", totexped, i );
				}
				rsbat.close();
				psbat.close();
				totexped ++;
			}

		    rs.close();
		    ps.close();

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao realizar consulta de expediente !\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
			return;
		}
		
	}
	
    private void visualizarAtend() {
		StringBuffer sqlatend = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement psbat = null;
		ResultSet rsbat = null;
		int totatend = 0;

		try {
			
			sqlatend.append( "SELECT A.CODATENDO, A.SITREVATENDO, A.DATAATENDO, A.HORAATENDO, A.HORAATENDOFIN, A.TOTALGERAL, A.TOTALMIN, ");
			sqlatend.append( "CAST( COALESCE( ( A.HORAATENDO - ");
			sqlatend.append( "COALESCE( ( SELECT FIRST 1 A2.HORAATENDOFIN FROM ATATENDIMENTO A2 WHERE A2.CODEMP=A.CODEMP AND ");
			sqlatend.append( "A2.CODFILIAL=A.CODFILIAL AND A2.DATAATENDO=A.DATAATENDO AND A2.HORAATENDO<=A.HORAATENDO AND ");
			sqlatend.append( "A2.CODATENDO<>A.CODATENDO AND A2.CODEMPAE=A.CODEMPAE AND A2.CODFILIALAE=A.CODFILIALAE AND ");
			sqlatend.append( "A2.CODATEND=A.CODATEND ");
			sqlatend.append( "ORDER BY A2.DATAATENDO DESC, A2.HORAATENDO DESC, A2.HORAATENDOFIN DESC ");
			sqlatend.append( "), A.HORAATENDO) ");
			sqlatend.append( "), 0) / 60 AS INTEGER ) ");
			sqlatend.append( "INTERVATENDO, ");			
			sqlatend.append( "A.CODESPEC, A.DESCESPEC ");
			sqlatend.append( "FROM ATATENDIMENTOVW03 A "); 
			sqlatend.append( "WHERE ");
			sqlatend.append( "A.CODEMP=? AND A.CODFILIAL=? AND A.DATAATENDO BETWEEN ? AND ? AND ");
			sqlatend.append( "A.CODEMPAE=? AND A.CODFILIALAE=? AND A.CODATEND=? " );
			sqlatend.append( "ORDER BY DATAATENDO, HORAATENDO, HORAATENDOFIN, DESCESPEC ");
			
 		    ps = con.prepareStatement( sqlatend.toString() );
 	
 		    ps.setInt( 1, Aplicativo.iCodEmp );
 		    ps.setInt( 2, ListaCampos.getMasterFilial( "ATATENDIMENTO" ) );
			ps.setDate( 3, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( 4, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
 		    ps.setInt( 5, Aplicativo.iCodEmp );
 		    ps.setInt( 6, ListaCampos.getMasterFilial( "ATATENDENTE" ) );
			ps.setInt( 7, txtCodAtend.getVlrInteger().intValue() );
			rs = ps.executeQuery();
			tabatend.limpa();

			while ( rs.next() ) {
				tabatend.adicLinha();
				tabatend.setValor( new Integer(rs.getInt( EColAtend.CODATENDO.toString() ) ), totatend, EColAtend.CODATENDO.ordinal() );
				tabatend.setValor( getImgSitrevatendo( "E"+rs.getString( EColAtend.SITREVATENDO.toString() ) ), totatend, EColAtend.SITREVATENDOIMG.ordinal() );
				tabatend.setValor( rs.getString( EColAtend.SITREVATENDO.toString() ), totatend, EColAtend.SITREVATENDO.ordinal() );
				tabatend.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( EColAtend.DATAATENDO.toString() ) ),
						totatend, EColAtend.DATAATENDO.ordinal() );
				tabatend.setValor( Funcoes.copy( rs.getTime( EColAtend.HORAATENDO.toString() ).toString() ,5 ) , 
						totatend, EColAtend.HORAATENDO.ordinal() );
				tabatend.setValor( Funcoes.copy( rs.getTime( EColAtend.HORAATENDOFIN.toString() ).toString(),5 ) , 
						totatend, EColAtend.HORAATENDOFIN.ordinal() );
				tabatend.setValor( "", totatend, EColAtend.HORABATIDA.ordinal() );
				tabatend.setValor( "", totatend, EColAtend.INIFINTURNO.ordinal() );
				tabatend.setValor( new Integer(rs.getInt( EColAtend.INTERVATENDO.toString() ) ) , totatend, EColAtend.INTERVATENDO.ordinal() );
				tabatend.setValor( rs.getBigDecimal( EColAtend.TOTALGERAL.toString() ) , totatend, EColAtend.TOTALGERAL.ordinal() );
				tabatend.setValor( new Integer(rs.getInt( EColAtend.TOTALMIN.toString() ) ) , totatend, EColAtend.TOTALMIN.ordinal() );
				tabatend.setValor( new Integer(rs.getInt( EColAtend.CODESPEC.toString() ) ) , totatend, EColAtend.CODESPEC.ordinal() );
				tabatend.setValor( rs.getString( EColAtend.DESCESPEC.toString() ), totatend, EColAtend.DESCESPEC.ordinal() );
				
				totatend ++;
			}

		    rs.close();
		    ps.close();

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao realizar consulta de atendimentos !\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
			return;
		}
    	
    }
    
	private void visualizar() {
		if ( !valida() ) {
			return;
		}
		btChecar.setEnabled( false );
		btGerar.setEnabled( false );
	    visualizarExped();
	    visualizarAtend();
		btChecar.setEnabled( true );

	}

	private void gerarEstagio345() {
		try {
			daoatend.gerarEstagio345(tabatend.getDataVector(), 
				Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "ATATENDIMENTO" ),
				Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "ATMODATENDO" ),
				Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "ATATENDENTE" ), txtCodAtend.getVlrInteger(),
				Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "SGUSUARIO" ), Aplicativo.getUsuario().getIdusu()
			);
		} catch (SQLException e) {
			Funcoes.mensagemErro( this, "Erro gravando atendimento !\n" + e.getMessage() );
			e.printStackTrace();
		}
		btVisual.doClick();
	}
	
	private void gerarEstagio2() {
		Funcoes.mensagemInforma( this, "Estágio 2 deve ser corrigido manualmente !\nExistem um ou mais turnos que excedem o tempo máximo ! " );
	}
	
	private void gerarEstagio1() {
		Vector<Batida> batidas = daoatend.getRegistroBatidas(tabexped.getDataVector(), nbatidas);
		
		if (batidas.size()==0) {
			btGerar.setEnabled( false );
			return;
		}
		else {
			//pbAnd.setValue( batidas.size() );
			//andamento = 0;
			//tim.start();
			try {
				for (Batida batida: batidas) {
					batida.setCodemp( Aplicativo.iCodEmp );
					batida.setCodfilial( ListaCampos.getMasterFilial( "PEBATIDA" ) );
					batida.setCodempep( Aplicativo.iCodEmp );
					batida.setCodfilialep( ListaCampos.getMasterFilial( "RHEMPREGADO" ) );
					batida.setMatempr( txtMatempr.getVlrInteger() );
					daobatida.executeProcInsereBatida( batida );
					andamento ++;
				}
			} catch (Exception e) {
				Funcoes.mensagemErro( this, "Erro gravando batidas!\n" + e.getMessage() );
			}
			tabexped.limpa();
			btVisual.doClick();
		}
		//tim.stop();
		//andamento = 0;
		//pbAnd.setValue( andamento );
		//pbAnd.updateUI();
		lbAnd.setText( "Pronto." );
		btGerar.setEnabled( false );
	}
	
	private void gerar() {
		String sitrev = (String) EstagioCheck.EPE.getValue();
		sitrev = daoatend.checarSitrevEstagio1234( tabexped.getDataVector(), tabatend.getDataVector() );
		if (sitrev.equals( EstagioCheck.EPE.getValue() )) {
			Funcoes.mensagemInforma( this, "Não passou pelo primeiro estágio de checagem !" );
			return;
		} else if (sitrev.equals( EstagioCheck.E1I.getValue() )) {
			gerarEstagio1();
		} else if (sitrev.equals( EstagioCheck.E2I.getValue() )) {
			gerarEstagio2();
		} else if (sitrev.equals( EstagioCheck.E3I.getValue() )) {
			gerarEstagio345();
		} else if (sitrev.equals( EstagioCheck.E4I.getValue() )) {
			gerarEstagio345();
		} else if (sitrev.equals( EstagioCheck.E5I.getValue() )) {
			gerarEstagio345();
		}
	}

	private void checar() {
        // retorna true caso seja necessário aplicar correções
		boolean result = daoatend.checar(tabexped.getDataVector(), tabatend.getDataVector(), this.nbatidas);
		if (result) {
			txtTotInconsistencia.setVlrInteger( daoatend.getTotInconsistencia(tabexped.getDataVector(), tabatend.getDataVector()) );
			btGerar.setEnabled( true );
		} else {
			txtTotInconsistencia.setVlrInteger( 0 );
		}
	    tabexped.updateUI();
	    tabatend.updateUI();
	}

	private boolean valida() {

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return false;
		}

		if ( txtCodAtend.getVlrInteger().intValue()==0) {
			Funcoes.mensagemInforma( this, "Selecione o atendente!" );
			return false;
		}
		
		if (txtMatempr.getVlrInteger().intValue()==0) {
			Funcoes.mensagemInforma( this, "Atendente não possui vínculo com empregado!" );
			return false;
		}
		
		if (txtCodTurno.getVlrInteger().intValue()==0) {
			Funcoes.mensagemInforma( this, "Empregado não possui vínculo com turno!" );
			return false;
		}
		return true;
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == tim ) {
			// System.out.println("Atualizando\n");
			pbAnd.setValue( andamento + 1 );
			pbAnd.updateUI();
		}
		else if ( evt.getSource() == btGerar ) {
			iniGerar();
		}
		else if ( evt.getSource() == btChecar ) {
			checar();
		}
		else if ( evt.getSource() == btVisual ) {
			visualizar();
		}
	}

	public void mouseClicked( MouseEvent e ) {
		if (e.getSource()==tabexped) {
			int linha = tabexped.getLinhaSel();
			if (linha>-1) {
				posicionaTabatend(tabexped.getValor( linha, EColExped.DTEXPED.ordinal() ).toString());
			}
		}
		
	}

	private void posicionaTabatend(String dtexped) {
		int linha = tabatend.pesqLinha( EColAtend.DATAATENDO.ordinal(), dtexped );
		if (linha>-1) {
			if (linha!=tabatend.getLinhaSel()) {
				tabatend.setLinhaSel( linha );
			}
		}
	}
	
	public void mouseEntered( MouseEvent e ) {

		
	}

	public void mouseExited( MouseEvent e ) {

		
	}

	public void mousePressed( MouseEvent e ) {

	
	}

	public void mouseReleased( MouseEvent e ) {

		
	}
}
