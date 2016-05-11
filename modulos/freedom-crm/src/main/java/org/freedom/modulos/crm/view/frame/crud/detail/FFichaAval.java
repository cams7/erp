/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.atd <BR>
 *         Classe: @(#)FTipoAtendo.java <BR>
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
 *         Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.crm.view.frame.crud.detail;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.CheckBoxEvent;
import org.freedom.acao.CheckBoxListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.JComboBoxEvent;
import org.freedom.acao.JComboBoxListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
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
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDetalhe;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.modulos.crm.business.object.FichaOrc;
import org.freedom.modulos.crm.business.object.Orcamento;
import org.freedom.modulos.crm.dao.DAOFicha;
import org.freedom.modulos.crm.view.dialog.utility.DLContToCli;
import org.freedom.modulos.crm.view.frame.crud.plain.FAmbienteAval;
import org.freedom.modulos.crm.view.frame.crud.plain.FMotivoAval;
import org.freedom.modulos.crm.view.frame.crud.tabbed.FContato;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.std.view.frame.crud.detail.FOrcamento;
import org.freedom.modulos.std.view.frame.crud.detail.FVenda;
import org.freedom.modulos.std.view.frame.crud.plain.FVariantes;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCliente;

public class FFichaAval extends FDetalhe implements InsertListener, CarregaListener, FocusListener, JComboBoxListener, PostListener, CheckBoxListener, KeyListener{

	private static final long serialVersionUID = 1L;
	
	private JTabbedPanePad tpnCab = new JTabbedPanePad();
	
	private JPanelPad pinFichaAval = new JPanelPad();

	private JPanelPad pinCabInfCompl = new JPanelPad();
	
	private JPanelPad pinCabOrcamento = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );
	
	private JTablePad tabOrcamento = new JTablePad();

	private JScrollPane spOrcamento = new JScrollPane( tabOrcamento );
	
	private JPanelPad pinObs = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );
	
	private JPanelPad pinCab = new JPanelPad();

	private JPanelPad pinDet = new JPanelPad();
	
	private JPanelPad pinImp = new JPanelPad(JPanelPad.TP_JPANEL);

	//FICHAAVAL
	
	private JTextFieldPad txtDtFichaAval = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );
	
	private JTextFieldPad txtDtVisitaFichaAval = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );
	
	private JTextFieldPad txtHVisitaFichaAval = new JTextFieldPad( JTextFieldPad.TP_TIME, 8, 0 );
	
	private JTextFieldPad txtSeqFichaAval = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtCodCont = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldFK txtRazCont = new JTextFieldFK( JTextFieldFK.TP_STRING, 60, 0 );

	private JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldPad txtCodSetor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtCodTipoCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );
	
	private JTextFieldPad txtCodMotAval= new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescMotAval = new JTextFieldFK( JTextFieldFK.TP_STRING, 50, 0 );

	private JTextFieldPad txtAndarFichaAval = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtPontoRefFichaAval= new JTextFieldPad( JTextFieldPad.TP_STRING, 100, 0 );

	private JTextAreaPad txaObsFichaAval = new JTextAreaPad();
	
	private JScrollPane spnObs = new JScrollPane( txaObsFichaAval );
	
	private JCheckBoxPad cbFinaliCriFichaAval = new JCheckBoxPad( " Criança ?", "S", "N" );
	
	private JCheckBoxPad cbFinaliAniFichaAval = new JCheckBoxPad( " Animal ?", "S", "N" );
	
	private JCheckBoxPad cbFinaliOutFichaAval = new JCheckBoxPad( " Outros ?", "S", "N" );
	
	//private JCheckBoxPad cbCobertFichaAval = new JCheckBoxPad( " INDICA SE É COBERTURA ?", "S", "N" );
	
	private JLabelPad lbCobertFichaAval = new JLabelPad( "INDICA SE É COBERTURA ?" );
	
	private JTextFieldPad txtCobertFichaAval = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );
	
	//private JCheckBoxPad cbEstrutFichaAval = new JCheckBoxPad( "HÁ NECESSIDADE DE ESTRUTURA ?", "S", "N" );
	private JLabelPad lbEstrutFichaAval = new JLabelPad( "HÁ NECESSIDADE DE ESTRUTURA ?" );
	
	private JTextFieldPad txtEstrutFichaAval = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );
	
	//private JCheckBoxPad cbOcupadoFichaAval = new JCheckBoxPad( "IMÓVEL OCUPADO ?", "S", "N" );
		
	private JLabelPad lbOcupadoFichaAval = new JLabelPad( "IMÓVEL OCUPADO ?" );
	
	private JTextFieldPad txtOcupadoFichaAval = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );
	
	private JCheckBoxPad cbJanelaFichaAval = new JCheckBoxPad( "JANELAS ?", "S", "N" );
	
	private JLabelPad lbQtdJanelaFichaAval = new JLabelPad( "Qtd. Janela: " );
		
	private JTextFieldPad txtQtdJanelaFichaAval = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );
	
	private JCheckBoxPad cbSacadaFichaAval = new JCheckBoxPad( "SACADAS ?", "S", "N" );
	
	private JLabelPad lbQtdSacadaFichaAval = new JLabelPad( "Qtd. Sacada: " );
	
	private JTextFieldPad txtQtdSacadaFichaAval = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );
	
	private JCheckBoxPad cbOutrosFichaAval = new JCheckBoxPad( "OUTROS ?", "S", "N" );
	
	private JTextFieldPad txtDescOutrosFichaAval = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );
	
	//ITFICHAAVAL
	
	private JTextFieldPad txtSeqItFichaAval = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );
	
	private JTextFieldPad txtDescItFichaAval = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldPad txtCodAmbAval = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 ); 
	
	private JTextFieldFK txtDescAmbAval = new JTextFieldFK( JTextFieldFK.TP_STRING, 50, 0 ); 
	
	private JTextFieldFK txtSiglaAmbAval = new JTextFieldFK( JTextFieldFK.TP_STRING, 10, 0 ); 
	
	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );
	
	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JLabelPad lbCodVar1 = new JLabelPad( "" );
	
	private JLabelPad lbCodVar2 = new JLabelPad( "" );
	
	private JLabelPad lbCodVar3 = new JLabelPad( "" );
	
	private JLabelPad lbCodVar4 = new JLabelPad( "" );
	
	private JLabelPad lbCodVar5 = new JLabelPad( "" );
	
	private JLabelPad lbCodVar6 = new JLabelPad( "" );
	
	private JLabelPad lbCodVar7 = new JLabelPad( "" );
	
	private JLabelPad lbCodVar8 = new JLabelPad( "" );
	
//	private JTextFieldPad txtMatItFichaAval = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );
	
//	private JTextFieldPad txtMalhaItFichaAval = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );
	
//	private JTextFieldPad txtCorItFichaAval = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );
	
	private JTextFieldPad txtTirItFichaAval = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );
	
	private JTextFieldPad txtAltItFichaAval = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

//	private JTextFieldPad txtAltInfItFichaAval = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );
	
//	private JTextFieldPad txtCompEsqItFichaAval = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );
	
	private JTextFieldPad txtCompItFichaAval = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

//	private JTextFieldPad txtCompDirItFichaAval = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );
	
	private JTextFieldPad txtEleFixItFichaAval = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldFK txtM2ItFichaAval = new JTextFieldFK( JTextFieldFK.TP_DECIMAL, 15, Aplicativo.casasDec );
		
//	private JTextFieldPad txtDnmItFichaAval = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );
	
	private JTextFieldFK txtVlrUnitItFichaAval = new JTextFieldFK( JTextFieldFK.TP_DECIMAL, 15, Aplicativo.casasDec );
	
	private JTextFieldPad txtVlrTotItFichaAval = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );
	
	private JTextFieldPad txtCodVarG1 = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8,0);
	
	private JTextFieldPad txtSeqItVarG1 = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8,0);
	
	private JTextFieldPad txtCodVarG2 = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8,0);
	
	private JTextFieldPad txtSeqItVarG2 = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8,0);
	
	private JTextFieldPad txtCodVarG3 = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8,0);
	
	private JTextFieldPad txtSeqItVarG3 = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8,0);
	
	private JTextFieldPad txtCodVarG4 = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8,0);
	
	private JTextFieldPad txtSeqItVarG4 = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8,0);
	
	private JTextFieldPad txtCodVarG5 = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8,0);
	
	private JTextFieldPad txtSeqItVarG5 = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8,0);
	
	private JTextFieldPad txtCodVarG6 = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8,0);
	
	private JTextFieldPad txtSeqItVarG6 = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8,0);
	
	private JTextFieldPad txtCodVarG7 = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8,0);
	
	private JTextFieldPad txtSeqItVarG7 = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8,0);
	
	private JTextFieldPad txtCodVarG8 = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8,0);
	
	private JTextFieldPad txtSeqItVarG8 = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8,0);
	
	// 1 Variante apontada no preferências gerais CRM.
	
	private Vector<Integer> vValsVarG1 = new Vector<Integer>();

	private Vector<String> vLabsVarG1 = new Vector<String>();
	
	private JComboBoxPad cbVarG1 = new JComboBoxPad( vLabsVarG1, vValsVarG1, JComboBoxPad.TP_INTEGER, 8, 0 );
	
	// 2 Variante apontada no preferências gerais CRM.
	
	private Vector<Integer> vValsVarG2 = new Vector<Integer>();

	private Vector<String> vLabsVarG2 = new Vector<String>();
	
	private JComboBoxPad cbVarG2 = new JComboBoxPad( vLabsVarG2, vValsVarG2, JComboBoxPad.TP_INTEGER, 8, 0 );
	
	// 3 Variante apontada no preferências gerais CRM.
	
	private Vector<Integer> vValsVarG3 = new Vector<Integer>();

	private Vector<String> vLabsVarG3 = new Vector<String>();
	
	private JComboBoxPad cbVarG3 = new JComboBoxPad( vLabsVarG3, vValsVarG3, JComboBoxPad.TP_INTEGER, 8, 0 );
	
	// 4 Variante apontada no preferências gerais CRM.
	
	private Vector<Integer> vValsVarG4 = new Vector<Integer>();

	private Vector<String> vLabsVarG4 = new Vector<String>();
	
	private JComboBoxPad cbVarG4 = new JComboBoxPad( vLabsVarG4, vValsVarG4, JComboBoxPad.TP_INTEGER, 8, 0 );
	
	// 5 Variante apontada no preferências gerais CRM.
	
	private Vector<Integer> vValsVarG5 = new Vector<Integer>();

	private Vector<String> vLabsVarG5 = new Vector<String>();
	
	private JComboBoxPad cbVarG5 = new JComboBoxPad( vLabsVarG5, vValsVarG5, JComboBoxPad.TP_INTEGER, 8, 0 );
	
	
	// 6 Variante apontada no preferências gerais CRM.
	
	private Vector<Integer> vValsVarG6 = new Vector<Integer>();

	private Vector<String> vLabsVarG6 = new Vector<String>();
	
	private JComboBoxPad cbVarG6 = new JComboBoxPad( vLabsVarG6, vValsVarG6, JComboBoxPad.TP_INTEGER, 8, 0 );
	
	// 7 Variante apontada no preferências gerais CRM.
	
	private Vector<Integer> vValsVarG7 = new Vector<Integer>();

	private Vector<String> vLabsVarG7 = new Vector<String>();
	
	private JComboBoxPad cbVarG7 = new JComboBoxPad( vLabsVarG7, vValsVarG7, JComboBoxPad.TP_INTEGER, 8, 0 );
	
	// 6 Variante apontada no preferências gerais CRM.
	
	private Vector<Integer> vValsVarG8 = new Vector<Integer>();

	private Vector<String> vLabsVarG8 = new Vector<String>();
	
	private JComboBoxPad cbVarG8 = new JComboBoxPad( vLabsVarG8, vValsVarG8, JComboBoxPad.TP_INTEGER, 8, 0 );

	private JRadioGroup<?, ?> rgLocalFichaAval = null;

	//private JRadioGroup<?, ?> rgFinaliFichaAval = null;
	
	private JRadioGroup<?, ?> rgMobilFichaAval = null;
	
	private JCheckBoxPad cbPredentrfichaAval = new JCheckBoxPad( "PREDIO/CASA ESTÁ SENDO ENTREGUE AGORA?", "S", "N" );
	
	private ListaCampos lcContato = new ListaCampos( this, "CO" );
	
	private ListaCampos lcMotAval = new ListaCampos( this, "MA" );
	
	private ListaCampos lcComis = new ListaCampos( this, "VD" );
	
	private ListaCampos lcProduto = new ListaCampos( this, "PD");
	
	private ListaCampos lcAmbAval = new ListaCampos( this, "AM");
	
	private ListaCampos lcVariante1 = new ListaCampos( this, "V1");
	
	private ListaCampos lcVariante2 = new ListaCampos( this, "V2");

	private ListaCampos lcVariante3 = new ListaCampos( this, "V3");
	
	private ListaCampos lcVariante4 = new ListaCampos( this, "V4");
	
	private ListaCampos lcVariante5 = new ListaCampos( this, "V5");
	
	private ListaCampos lcVariante6 = new ListaCampos( this, "V6");
	
	private ListaCampos lcVariante7 = new ListaCampos( this, "V7");
	
	private ListaCampos lcVariante8 = new ListaCampos( this, "V8");
	
	private JButtonPad btExportCli = new JButtonPad( Icone.novo( "btExportaCli.png" ) );
	
	private JButtonPad btGeraOrc = new JButtonPad( Icone.novo( "btGerar.png" ) );
	
	private DAOFicha daoficha = null;
	
	String descVar1 = "";
	String descVar2 = "";
	String descVar3 = "";
	String descVar4 = "";
	String descVar5 = "";
	String descVar6 = "";
	String descVar7 = "";
	String descVar8 = "";

	public FFichaAval() {

		nav.setNavigation( true );
		//nav.add( pinImp );
		//pinImp.add( btPrevimp );
		setTitulo( "Ficha Avaliativa" );
	
		setAtribos( 50, 50, 750, 600 );
		montaListaCampos();
		montaTela();
		montaListeners();
		
		btGeraOrc.setToolTipText( "Gerar Orçamento a partir da ficha avaliativa" );
		btPrevimp.setToolTipText( "Previsão da ficha avaliativa" );
		btExportCli.setToolTipText( "Transforma contato em cliente" );

	}
	
	public FFichaAval(DbConnection cn, int codCto){
		this();
		setConexao( cn );	
		lcCampos.insert( true );
		txtCodCont.setVlrInteger(codCto);
		lcContato.carregaDados();
		txtDtFichaAval.setVlrDate( new Date() );
		
	}
	
	public void montaListeners(){
		
		btPrevimp.addActionListener( this );
		btExportCli.addActionListener( this );
		btGeraOrc.addActionListener( this );
		lcCampos.addCarregaListener( this );
		lcDet.addCarregaListener( this );
		txtCompItFichaAval.addFocusListener( this );
		txtCodProd.addFocusListener( this );
		txtVlrTotItFichaAval.addFocusListener( this );
		cbVarG1.addComboBoxListener( this );
		cbVarG2.addComboBoxListener( this );
		cbVarG3.addComboBoxListener( this );
		cbVarG4.addComboBoxListener( this );
		cbVarG5.addComboBoxListener( this );
		cbVarG6.addComboBoxListener( this );
		cbVarG7.addComboBoxListener( this );
		cbVarG8.addComboBoxListener( this );
		cbJanelaFichaAval.addCheckBoxListener( this );
		cbSacadaFichaAval.addCheckBoxListener( this );
		cbOutrosFichaAval.addCheckBoxListener( this );
		lcCampos.addCarregaListener( this );
		lcCampos.addPostListener( this );
		lcDet.addPostListener( this );
		lcDet.addInsertListener( this );
		cbFinaliOutFichaAval.addKeyListener( this );
		
	}
	
	public void montaListaCampos(){
		
		// FK Contato
		txtCodCont.setTabelaExterna( lcContato, FContato.class.getCanonicalName());
		txtCodCont.setFK( true );
		txtCodCont.setNomeCampo( "CodContr" );
		lcContato.add( new GuardaCampo( txtCodCont, "CodCto", "Cód.Contato", ListaCampos.DB_PK, false ) );
		lcContato.add( new GuardaCampo( txtRazCont, "RazCto", "Razão do contato.", ListaCampos.DB_SI, false ) );
		lcContato.add( new GuardaCampo( txtCodSetor, "CodSetor", "Cód.setor", ListaCampos.DB_SI, false ) );
		lcContato.add( new GuardaCampo( txtCodTipoCli, "CodTipoCli", "Cód.Tipo Cli.", ListaCampos.DB_SI, false ) );
		lcContato.montaSql( false, "CONTATO", "TK" );
		lcContato.setReadOnly( true );
		lcContato.setQueryCommit( false );
		
		// FK Motivo Aval.
		txtCodMotAval.setTabelaExterna( lcMotAval, FMotivoAval.class.getCanonicalName());
		txtCodMotAval.setFK( true );
		txtCodMotAval.setNomeCampo( "MotAval" );
		lcMotAval.add( new GuardaCampo( txtCodMotAval, "CodMotAval", "Cód.Motivo", ListaCampos.DB_PK, false ) );
		lcMotAval.add( new GuardaCampo( txtDescMotAval, "DescMotAval", "Descrição do motivo da avaliação.", ListaCampos.DB_SI, false ) );
		lcMotAval.montaSql( false, "MotivoAval", "CR" );
		lcMotAval.setReadOnly( true );
		lcMotAval.setQueryCommit( false );
		
		
		// FK VENDEDOR
		lcComis.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.comis.", ListaCampos.DB_PK, txtNomeVend, false ) );
		lcComis.add( new GuardaCampo( txtNomeVend, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, false ) );
		lcComis.setWhereAdic( "ATIVOCOMIS='S' " );
		lcComis.montaSql( false, "VENDEDOR", "VD" );
		lcComis.setQueryCommit( false );
		lcComis.setReadOnly( true );
		txtCodVend.setTabelaExterna( lcComis, null );
		txtCodVend.setFK( true );
		txtCodVend.setNomeCampo( "codvend" );
		
		// FK Produto

		txtCodProd.setTabelaExterna( lcProduto, FProduto.class.getCanonicalName() );
		txtCodProd.setFK( true );
		txtCodProd.setNomeCampo( "CodProd" );
		lcProduto.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcProduto.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProduto.montaSql( false, "PRODUTO", "EQ" );
		lcProduto.setReadOnly( true );
		lcProduto.setQueryCommit( false );
		
		// FK Ambiente Aval.
		txtCodAmbAval.setTabelaExterna( lcAmbAval, FAmbienteAval.class.getCanonicalName());
		txtCodAmbAval.setFK( true );
		txtCodAmbAval.setNomeCampo( "CodAmbAval" );
		lcAmbAval.add( new GuardaCampo( txtCodAmbAval, "CodAmbAval", "Cód.Ambiente", ListaCampos.DB_PK, false ) );
		lcAmbAval.add( new GuardaCampo( txtDescAmbAval, "DescAmbAval", "Descrição do ambiente.", ListaCampos.DB_SI, false ) );
		lcAmbAval.add( new GuardaCampo( txtSiglaAmbAval, "SiglaAmbAval", "Sigla.Amb.", ListaCampos.DB_SI, false ) );
		lcAmbAval.montaSql( false, "AmbienteAval", "CR" );
		lcAmbAval.setReadOnly( true );
		lcAmbAval.setQueryCommit( false );
		
		// Ficha Avaliativa.
		txtCodVarG1.setTabelaExterna(lcVariante1, FVariantes.class.getCanonicalName());
		txtSeqItVarG1.setTabelaExterna(lcVariante1, FVariantes.class.getCanonicalName());
		txtCodVarG1.setFK( true );
		txtSeqItVarG1.setFK( true );
		lcVariante1.add( new GuardaCampo( txtCodVarG1, "CodVarG", "Cód.var.1", ListaCampos.DB_PK, false ) );
		lcVariante1.add( new GuardaCampo( txtSeqItVarG1, "SeqItVarG", "Sequência da Variante 1", ListaCampos.DB_PK,  false ) );
		lcVariante1.montaSql( false, "ITVARGRADE", "EQ" );
		lcVariante1.setQueryCommit( false );
		lcVariante1.setReadOnly( true );
		txtCodVarG1.setListaCampos( lcVariante1 );
		
		
		txtCodVarG2.setTabelaExterna(lcVariante2,  FVariantes.class.getCanonicalName());
		txtSeqItVarG2.setTabelaExterna(lcVariante2, FVariantes.class.getCanonicalName());
		txtCodVarG2.setFK( true );
		txtSeqItVarG2.setFK( true );
		lcVariante2.add( new GuardaCampo( txtCodVarG2, "CodVarG", "Cód.var.2", ListaCampos.DB_PK, false ) );
		lcVariante2.add( new GuardaCampo( txtSeqItVarG2, "SeqItVarG", "Sequência da Variante 2", ListaCampos.DB_PK,  false ) );
		lcVariante2.montaSql( false, "ITVARGRADE", "EQ" );
		lcVariante2.setQueryCommit( false );
		lcVariante2.setReadOnly( true );
		txtCodVarG2.setListaCampos( lcVariante2 );
		
		
		txtCodVarG3.setTabelaExterna(lcVariante3, FVariantes.class.getCanonicalName());
		txtSeqItVarG3.setTabelaExterna(lcVariante3, FVariantes.class.getCanonicalName());
		txtCodVarG3.setFK( true );
		txtSeqItVarG3.setFK( true );
		lcVariante3.add( new GuardaCampo( txtCodVarG3, "CodVarG", "Cód.var.3", ListaCampos.DB_PK, false));
		lcVariante3.add( new GuardaCampo( txtSeqItVarG3, "SeqItVarG", "Sequência da Variante 3", ListaCampos.DB_PK, false));
		lcVariante3.montaSql( false,"ITVARGRADE", "EQ");
		lcVariante3.setQueryCommit( false);
		lcVariante3.setReadOnly( true );
		txtCodVarG3.setListaCampos( lcVariante3 );
		
		
		txtCodVarG4.setTabelaExterna(lcVariante4,  FVariantes.class.getCanonicalName());
		txtSeqItVarG4.setTabelaExterna(lcVariante4,  FVariantes.class.getCanonicalName());
		txtCodVarG4.setFK( true );
		txtSeqItVarG4.setFK( true );
		lcVariante4.add( new GuardaCampo( txtCodVarG4, "CodVarG", "Cód.var.4", ListaCampos.DB_PK, false));
		lcVariante4.add( new GuardaCampo( txtSeqItVarG4, "SeqItVarG", "Sequência da Variante 4", ListaCampos.DB_PK, false));
		lcVariante4.montaSql( false,"ITVARGRADE", "EQ");
		lcVariante4.setQueryCommit( false);
		lcVariante4.setReadOnly( true );
		txtCodVarG4.setListaCampos( lcVariante4 );
		
		txtCodVarG5.setTabelaExterna(lcVariante5,  FVariantes.class.getCanonicalName());
		txtSeqItVarG5.setTabelaExterna(lcVariante5,  FVariantes.class.getCanonicalName());
		txtCodVarG5.setFK( true );
		txtSeqItVarG5.setFK( true );
		lcVariante5.add( new GuardaCampo( txtCodVarG5, "CodVarG", "Cód.var.5", ListaCampos.DB_PK, false));
		lcVariante5.add( new GuardaCampo( txtSeqItVarG5, "SeqItVarG", "Sequência da Variante 5", ListaCampos.DB_PK, false));
		lcVariante5.montaSql( false,"ITVARGRADE", "EQ");
		lcVariante5.setQueryCommit( false);
		lcVariante5.setReadOnly( true );
		txtCodVarG5.setListaCampos( lcVariante5 );
		
		txtCodVarG6.setTabelaExterna(lcVariante6,  FVariantes.class.getCanonicalName());
		txtSeqItVarG6.setTabelaExterna(lcVariante6,  FVariantes.class.getCanonicalName());
		txtCodVarG6.setFK( true );
		txtSeqItVarG6.setFK( true );
		lcVariante6.add( new GuardaCampo( txtCodVarG6, "CodVarG", "Cód.var.6", ListaCampos.DB_PK, false));
		lcVariante6.add( new GuardaCampo( txtSeqItVarG6, "SeqItVarG", "Sequência da Variante 6", ListaCampos.DB_PK, false));
		lcVariante6.montaSql( false,"ITVARGRADE", "EQ");
		lcVariante6.setQueryCommit( false);
		lcVariante6.setReadOnly( true );
		txtCodVarG6.setListaCampos( lcVariante6 );
		
		txtCodVarG7.setTabelaExterna(lcVariante7,  FVariantes.class.getCanonicalName());
		txtSeqItVarG7.setTabelaExterna(lcVariante7,  FVariantes.class.getCanonicalName());
		txtCodVarG7.setFK( true );
		txtSeqItVarG7.setFK( true );
		lcVariante7.add( new GuardaCampo( txtCodVarG7, "CodVarG", "Cód.var.7", ListaCampos.DB_PK, false));
		lcVariante7.add( new GuardaCampo( txtSeqItVarG7, "SeqItVarG", "Sequência da Variante 7", ListaCampos.DB_PK, false));
		lcVariante7.montaSql( false,"ITVARGRADE", "EQ");
		lcVariante7.setQueryCommit( false);
		lcVariante7.setReadOnly( true );
		txtCodVarG7.setListaCampos( lcVariante7 );
		
		txtCodVarG8.setTabelaExterna(lcVariante8,  FVariantes.class.getCanonicalName());
		txtSeqItVarG8.setTabelaExterna(lcVariante8,  FVariantes.class.getCanonicalName());
		txtCodVarG8.setFK( true );
		txtSeqItVarG8.setFK( true );
		lcVariante8.add( new GuardaCampo( txtCodVarG8, "CodVarG", "Cód.var.8", ListaCampos.DB_PK, false));
		lcVariante8.add( new GuardaCampo( txtSeqItVarG8, "SeqItVarG", "Sequência da Variante 8", ListaCampos.DB_PK, false));
		lcVariante8.montaSql( false,"ITVARGRADE", "EQ");
		lcVariante8.setQueryCommit( false);
		lcVariante8.setReadOnly( true );
		txtCodVarG8.setListaCampos( lcVariante8 );

	}
	
	public void montaTela(){
		montaGrupoRadio();

		adicAbas();
		
		montaCombos();
		
		setListaCampos( lcCampos );
		setAltCab( 210 );
		setPainel( pinFichaAval );
			
		adicCampo( txtSeqFichaAval, 7, 20, 80, 20, "SeqFichaAval", "Seq.Ficha", ListaCampos.DB_PK, true );
		adicCampo( txtCodCont, 90, 20, 80, 20, "CodCto", "Cód.Contato", ListaCampos.DB_FK, txtRazCont, true );
		adicDescFK( txtRazCont, 173, 20, 270, 20, "RazCto", "Razão do contato" );
		adicCampo( txtDtFichaAval, 446, 20, 80, 20, "DtFichaAval", "Dt.ficha aval.", ListaCampos.DB_SI, true );
		adicCampo( txtDtVisitaFichaAval, 529, 20, 80, 20, "DtVisitaFichaAval", "Dt.visita", ListaCampos.DB_SI, false );
		adicCampo( txtHVisitaFichaAval, 612, 20, 80, 20, "HVisitaFichaAval", "H.visita", ListaCampos.DB_SI, false );
		
		
		adicCampo( txtCodMotAval, 7, 60, 80, 20, "CodMotAval", "Cód.Motivo", ListaCampos.DB_FK ,txtDescMotAval, true );
		adicDescFK( txtDescMotAval, 90, 60, 250, 20, "DescMotAval", "Descrição do motivo da avaliação" );
		adicCampo( txtAndarFichaAval, 343, 60, 80, 20, "AndarFichaAval", "Andar", ListaCampos.DB_SI , false );
		adicCampo( txtCodVend, 426, 60, 80, 20, "CodVend", "Cód.comis", ListaCampos.DB_FK, txtNomeVend, false );
		adicDescFK( txtNomeVend, 509, 60, 180, 20, "NomeVend", "Razão do comissionado" );
		
		
		adicCampo( txtPontoRefFichaAval, 7, 100, 633, 20, "PontoRefFichaAval", "Ponto de referência", ListaCampos.DB_SI, false);
				
	//	adicDB( rgFinaliFichaAval, 330, 100, 320, 30, "FinaliFichaAval", "Finalidade Ficha Avaliativa", false );
		adicDB( cbPredentrfichaAval, 7, 120, 300, 30, "PredentrfichaAval", "", false );
		adicDB( cbFinaliCriFichaAval, 310, 120, 85, 30, "FinaliCriFichaAval", "", false );
		adicDB( cbFinaliAniFichaAval, 397, 120, 85, 30, "FinaliAniFichaAval", "", false );
		adicDB( cbFinaliOutFichaAval, 485, 120, 85, 30, "FinaliOutFichaAval", "", false );
		//cbPredentrfichaAval.setoValorNeutro( new String( "X" ) );
		adicDBLiv( txaObsFichaAval, "ObsFichaAval", "Observações ficha aval", false );
		setPainel( pinCabInfCompl );

		adicDB( rgLocalFichaAval, 7, 20, 345, 30, "LocalFichaAval", "Edificação", false );
		adicDB( rgMobilFichaAval, 355, 20, 345, 30, "MobilFichaAval", "Imóvel", false );
		//adicDB( cbCobertFichaAval, 7, 50, 250, 20, "CobertFichaAval", "", true );
		//adicDB( cbEstrutFichaAval, 355, 50, 250, 20, "EstrutFichaAval", "", true );
		adic( lbCobertFichaAval, 7, 55, 200, 20 );
		adicCampo( txtCobertFichaAval, 208, 55, 40, 20, "CobertFichaAval", "", ListaCampos.DB_SI, true );
		adic( lbEstrutFichaAval, 355, 55, 200, 20 );
		adicCampo( txtEstrutFichaAval, 585, 55, 40, 20, "EstrutFichaAval", "", ListaCampos.DB_SI, true );
		
		
		//adicDB( cbOcupadoFichaAval, 7, 95, 300, 20, "OcupadoFichaAval", "", true );
		adic( lbOcupadoFichaAval, 7, 80, 200, 20 );
		adicCampo( txtOcupadoFichaAval, 208, 80, 40, 20, "OcupadoFichaAval", "", ListaCampos.DB_SI, true );
		
		adicDB( cbJanelaFichaAval, 355, 80, 90, 20, "JanelaFichaAval", "", true );
		adic( lbQtdJanelaFichaAval,500,80,80,20 );
		adicCampo( txtQtdJanelaFichaAval, 585, 80, 80, 20, "QtdJanelaFichaAval", "", ListaCampos.DB_SI, false );
		
		adicDB( cbSacadaFichaAval, 7, 105, 90, 20, "SacadaFichaAval", "", true );
		adic( lbQtdSacadaFichaAval,125, 105, 80, 20 );
		adicCampo( txtQtdSacadaFichaAval,208 , 105, 80, 20, "QtdSacadaFichaAval", "", ListaCampos.DB_SI, false );
		
		adicDB( cbOutrosFichaAval, 355, 105, 90, 20, "OutrosFichaAval", "", true );
	
		adicCampo( txtDescOutrosFichaAval,500 , 105, 187, 20, "DescOutrosFichaAval", "", ListaCampos.DB_SI, false );
		
		
		setListaCampos( true, "FICHAAVAL", "CR" );
		lcCampos.setQueryInsert( false );
		txtQtdJanelaFichaAval.setEnabled( false );
		txtQtdSacadaFichaAval.setEnabled( false );
		txtDescOutrosFichaAval.setEnabled( false );
		
		tpnCab.addTab( "Orçamento", pinCabOrcamento );

		pinCabOrcamento.add( spOrcamento, BorderLayout.CENTER );

		tabOrcamento.adicColuna( "Cód.Orc" );
		tabOrcamento.adicColuna( "Cód.cli." );
		tabOrcamento.adicColuna( "Emissão" );
		tabOrcamento.adicColuna( "Vencimento" );
		tabOrcamento.adicColuna( "Cód.pag." );
		tabOrcamento.adicColuna( "Vlr.Orc" );
		tabOrcamento.adicColuna( "Tipo Orc" );
		tabOrcamento.adicColuna( "Qtd.Itens" );

		tabOrcamento.setTamColuna( 80, Orcamento.GET_ORC.CODORC.ordinal() );
		tabOrcamento.setTamColuna( 80, Orcamento.GET_ORC.CODCLI.ordinal() );
		tabOrcamento.setTamColuna( 80, Orcamento.GET_ORC.DTEMISSAO.ordinal() );
		tabOrcamento.setTamColuna( 80, Orcamento.GET_ORC.DTVENC.ordinal() );
		tabOrcamento.setTamColuna( 80, Orcamento.GET_ORC.CODPAG.ordinal() );
		tabOrcamento.setTamColuna( 80, Orcamento.GET_ORC.VLRPRODORC.ordinal() );
		tabOrcamento.setColunaInvisivel( Orcamento.GET_ORC.TIPOORC.ordinal() );
		tabOrcamento.setTamColuna( 80, Orcamento.GET_ORC.QTDITENS.ordinal() );
	

		tabOrcamento.addMouseListener( new MouseAdapter() {

			@ Override
			public void mouseClicked( MouseEvent e ) {

				if ( e.getClickCount() == 2 ) {
					if ( e.getSource() == tabOrcamento ) {
						abreOrcamento();
					}
				}
			}
		} );
		
		montaDetalhe();
	
	}
	
	public void montaDetalhe(){
		
		setAltDet( 210 );
		pinDet = new JPanelPad( 600, 80 );
		setPainel( pinDet, pnDet );
		setListaCampos( lcDet );
		lcDet.setMaster( lcCampos );
		setNavegador( navRod );
		
		adicCampo( txtSeqItFichaAval, 7, 25, 60, 20, "SeqItFichaAval", "Seq.item", ListaCampos.DB_PK, true );
		adicCampo( txtCodAmbAval, 70, 25, 60, 20, "CodAmbAval", "Cód.Amb.", ListaCampos.DB_FK, txtDescAmbAval, true );
		adicDescFK( txtSiglaAmbAval, 133, 25, 60, 20, "SiglaAmbAval", "Sigla.Amb.");
		adicCampo( txtDescItFichaAval, 196, 25, 195, 20, "DescItFichaAval", "Descrição", ListaCampos.DB_SI, true );
		adicCampo( txtTirItFichaAval, 394, 25, 60, 20, "TIRITFICHAAVAL", "Tirante", ListaCampos.DB_SI, true );
		adicCampo( txtAltItFichaAval, 457, 25, 60, 20, "AltItFichaAval", "Altura", ListaCampos.DB_SI, true );
		adicCampo( txtCompItFichaAval, 521, 25, 60, 20, "CompItFichaAval", "Comprimento", ListaCampos.DB_SI, true );
		adicCampo( txtM2ItFichaAval, 584, 25, 60, 20, "M2ItFichaAval", "M²", ListaCampos.DB_SI, true );
//		adicCampo( txtMatItFichaAval, 394, 25, 80, 20, "MaterialItFichaAval", "Material", ListaCampos.DB_SI, true );
//		adicCampo( txtMalhaItFichaAval, 477, 25, 80, 20, "MalhaItFichaAval", "Malha", ListaCampos.DB_SI, true );
//		adicCampo( txtCorItFichaAval, 560, 25, 80, 20, "CorItFichaAval", "Cor", ListaCampos.DB_SI, true );
		adicCampo( txtCodProd, 7, 65, 60, 20, "CodProd", "Cód.prod.", ListaCampos.DB_FK, txtDescProd, true );
		adicDescFK( txtDescProd, 70, 65, 400, 20, "DescProd", "Descrição do produto/serviço" );
//		adicCampo( txtEleFixItFichaAval, 473, 65, 80, 20, "EleFixItFichaAval", "Elem.Fixação", ListaCampos.DB_SI, true );
		adicCampo( txtVlrTotItFichaAval, 477, 65, 80, 20, "VlrTotItFichaAval", "Valor Tot.", ListaCampos.DB_SI, true );	
		adicCampo( txtVlrUnitItFichaAval, 560, 65, 80, 20, "VlrUnitItFichaAval", "Valor Unit.", ListaCampos.DB_SI, true );	
//		adicCampo( txtAltSupItFichaAval, 394, 65, 80, 20, "AltSupItFichaAval", "Alt.sup.", ListaCampos.DB_SI, true );
//		adicCampo( txtAltInfItFichaAval, 560, 65, 80, 20, "AltInfItFichaAval", "Alt.inf.", ListaCampos.DB_SI, true );
//		adicCampo( txtCompEsqItFichaAval, 7, 105, 80, 20, "CompEsqItFichaAval", "Comp.esq.", ListaCampos.DB_SI, true );		
//		adicCampo( txtCompDirItFichaAval, 173, 105, 80, 20, "CompDirItFichaAval", "Comp.dir.", ListaCampos.DB_SI, true );
		
		JLabel bordaData = new JLabel();
		bordaData.setBorder( BorderFactory.createEtchedBorder() );
		JLabel detalhamento = new JLabel( "Detalhamento", SwingConstants.CENTER );
		detalhamento.setOpaque( true );
		
		adic( detalhamento, 7, 87, 100, 20 );
		adic( lbCodVar1, 17, 107, 150, 20 );
		adic( cbVarG1, 17, 125, 150, 20, descVar1 );
		adic( lbCodVar2, 170, 107, 150, 20 );
		adic( cbVarG2, 170, 125, 150, 20, descVar2 );
		adic( lbCodVar3, 323, 107, 150, 20 );
		adic( cbVarG3, 323, 125, 150, 20, descVar3 );
		adic( lbCodVar4, 476, 107, 150, 20 );
		adic( cbVarG4, 476, 125, 150, 20, descVar4 );
		adic( lbCodVar5, 17, 147, 150, 20 );
		adic( cbVarG5, 17, 165, 150, 20, descVar5 );
		adic( lbCodVar6, 170, 147, 150, 20 );
		adic( cbVarG6, 170, 165, 150, 20, descVar6 );
		adic( lbCodVar7, 323, 147, 150, 20 );
		adic( cbVarG7, 323, 165, 150, 20, descVar7 );
		adic( lbCodVar8, 476, 147, 150, 20 );
		adic( cbVarG8, 476, 165, 150, 20, descVar8 );
		adic( bordaData, 7, 97, 635, 100 );
		
		
		adicCampoInvisivel( txtCodVarG1, "codvarg1", "Cód.var.1", ListaCampos.DB_FK, false);
		adicCampoInvisivel( txtSeqItVarG1, "Seqitvarg1", "Seq.it.var.1", ListaCampos.DB_SI, false);
		txtCodVarG1.setName( "CodVarG" );
		txtSeqItVarG1.setName( "SeqItVarG" );
		
		adicCampoInvisivel( txtCodVarG2, "codvarg2", "Cód.var.2", ListaCampos.DB_FK, false);
		adicCampoInvisivel( txtSeqItVarG2, "Seqitvarg2", "Seq.it.var.2", ListaCampos.DB_SI, false);
		txtCodVarG2.setName( "CodVarG" );
		txtSeqItVarG2.setName( "SeqItVarG" );

		adicCampoInvisivel( txtCodVarG3, "codvarg3", "Cód.var.3", ListaCampos.DB_FK,  false);
		adicCampoInvisivel( txtSeqItVarG3, "Seqitvarg3", "Seq.it.var.3", ListaCampos.DB_SI, false);
		txtCodVarG3.setName( "CodVarG" );
		txtSeqItVarG3.setName( "SeqItVarG" );
		
		adicCampoInvisivel( txtCodVarG4, "codvarg4", "Cód.var.4", ListaCampos.DB_FK,  false);
		adicCampoInvisivel( txtSeqItVarG4, "Seqitvarg4", "Seq.it.var.4", ListaCampos.DB_SI, false);
		txtCodVarG4.setName( "CodVarG" );
		txtSeqItVarG4.setName( "SeqItVarG" );
		
		adicCampoInvisivel( txtCodVarG5, "codvarg5", "Cód.var.5", ListaCampos.DB_FK,  false);
		adicCampoInvisivel( txtSeqItVarG5, "Seqitvarg5", "Seq.it.var.5", ListaCampos.DB_SI, false);
		txtCodVarG5.setName( "CodVarG" );
		txtSeqItVarG5.setName( "SeqItVarG" );
		
		adicCampoInvisivel( txtCodVarG6, "codvarg6", "Cód.var.6", ListaCampos.DB_FK,  false);
		adicCampoInvisivel( txtSeqItVarG6, "Seqitvarg6", "Seq.it.var.6", ListaCampos.DB_SI, false);
		txtCodVarG6.setName( "CodVarG" );
		txtSeqItVarG6.setName( "SeqItVarG" );
		
		adicCampoInvisivel( txtCodVarG7, "codvarg7", "Cód.var.7", ListaCampos.DB_FK,  false);
		adicCampoInvisivel( txtSeqItVarG7, "Seqitvarg7", "Seq.it.var.7", ListaCampos.DB_SI, false);
		txtCodVarG7.setName( "CodVarG" );
		txtSeqItVarG7.setName( "SeqItVarG" );
		
		adicCampoInvisivel( txtCodVarG8, "codvarg8", "Cód.var.8", ListaCampos.DB_FK,  false);
		adicCampoInvisivel( txtSeqItVarG8, "Seqitvarg8", "Seq.it.var.8", ListaCampos.DB_SI, false);
		txtCodVarG8.setName( "CodVarG" );
		txtSeqItVarG8.setName( "SeqItVarG" );
		
		//adicDB( cbVarG2, 206, 60, 314, 20, "SEQITVARG2", "",false );
		
		setListaCampos( true, "ITFICHAAVAL", "CR" );
		lcDet.setQueryInsert( false );
		
		montaTab();
		
		pnGImp.removeAll();
		pnGImp.setLayout( new GridLayout( 1, 2 ) );
		pnGImp.setPreferredSize( new Dimension( 93, 26 ) );
		pnGImp.add( btExportCli );
		pnGImp.add( btGeraOrc );
		pnGImp.add( btPrevimp );
		setImprimir( true );
		lcCampos.addInsertListener( this );

	}
	
	public void montaCombos(){
		
		vValsVarG1.addElement( -1 );
		vLabsVarG1.addElement( "<Não Selecionado>" );
		cbVarG1.setItensGeneric( vLabsVarG1, vValsVarG1 );
		
		vValsVarG2.addElement(  -1 );
		vLabsVarG2.addElement( "<Não Selecionado>" );
		cbVarG2.setItensGeneric( vLabsVarG2, vValsVarG2 );
		
		vValsVarG3.addElement( -1 );
		vLabsVarG3.addElement( "<Não Selecionado>" );
		cbVarG3.setItensGeneric( vLabsVarG3, vValsVarG3 );
		
		vValsVarG4.addElement( -1 );
		vLabsVarG4.addElement( "<Não Selecionado>" );
		cbVarG4.setItensGeneric( vLabsVarG4, vValsVarG4 );
		
		vValsVarG5.addElement( -1 );
		vLabsVarG5.addElement( "<Não Selecionado>" );
		cbVarG5.setItensGeneric( vLabsVarG5, vValsVarG5 );
		
		vValsVarG6.addElement( -1 );
		vLabsVarG6.addElement( "<Não Selecionado>" );
		cbVarG6.setItensGeneric( vLabsVarG6, vValsVarG6 );
		
		vValsVarG7.addElement( -1 );
		vLabsVarG7.addElement( "<Não Selecionado>" );
		cbVarG7.setItensGeneric( vLabsVarG7, vValsVarG7 );
		
		vValsVarG8.addElement( -1 );
		vLabsVarG8.addElement( "<Não Selecionado>" );
		cbVarG8.setItensGeneric( vLabsVarG8, vValsVarG8 );
		
	}
	
	public void abreOrcamento(){
		if ( tabOrcamento.getLinhaSel() > -1 ) {
			FOrcamento tela = null;
			if ( Aplicativo.telaPrincipal.temTela( FVenda.class.getName() ) ) {
				tela = (FOrcamento) Aplicativo.telaPrincipal.getTela( FOrcamento.class.getName() );
			} else {
				tela = new FOrcamento();
				Aplicativo.telaPrincipal.criatela( "Orçamento", tela, con );
			}
			tela.exec( (Integer) tabOrcamento.getValor( tabOrcamento.getLinhaSel(), Orcamento.GET_ORC.CODORC.ordinal() ));
		}
	}
	
	private void carregaOrcamentos() {

		tabOrcamento.limpa();
		ResultSet rs = null;
		try {

			List<Orcamento> orcs =	daoficha.loadOrcamento( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "CRFICHAORC" ), txtSeqFichaAval.getVlrInteger() );
			int row = 0;
			for ( Orcamento o : orcs) {
				
				tabOrcamento.adicLinha();
				tabOrcamento.setValor( o.getCodorc(), row, Orcamento.GET_ORC.CODORC.ordinal() );
				tabOrcamento.setValor( o.getCodcli(), row, Orcamento.GET_ORC.CODCLI.ordinal() );
				tabOrcamento.setValor( o.getDtorc() , row, Orcamento.GET_ORC.DTEMISSAO.ordinal() );
				tabOrcamento.setValor( o.getDtvencorc() , row, Orcamento.GET_ORC.DTVENC.ordinal() );
				tabOrcamento.setValor( o.getCodplanopag(), row, Orcamento.GET_ORC.CODPAG.ordinal() );
				tabOrcamento.setValor( o.getPrecoitorc() ,row, Orcamento.GET_ORC.VLRPRODORC.ordinal() );
				tabOrcamento.setValor( o.getTipoorc(), row, Orcamento.GET_ORC.TIPOORC.ordinal() );
				tabOrcamento.setValor( o.getQtditens(), row, Orcamento.GET_ORC.QTDITENS.ordinal() );
				row++;
			}
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao consultar orcamento!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}
	
	public void setImprimir(boolean bImp) {
		btImp.setVisible(bImp);
		btPrevimp.setVisible(bImp);
	}
	
	private void montaGrupoRadio(){
		
		Vector<String> vValsLocal = new Vector<String>();
		Vector<String> vLabsLocal = new Vector<String>();
		vLabsLocal.addElement( "Apartamento" );
		vLabsLocal.addElement( "Casa" );
		vLabsLocal.addElement( "Empresa" );
		vValsLocal.addElement( "A" );
		vValsLocal.addElement( "C" );
		vValsLocal.addElement( "E" );
		rgLocalFichaAval = new JRadioGroup<String, String>( 1, 3, vLabsLocal, vValsLocal );
		rgLocalFichaAval.setVlrString( "A" );

		/*Vector<String> vValsFinali = new Vector<String>();
		Vector<String> vLabsFinali = new Vector<String>();
		vLabsFinali.addElement( "Criança" );
		vLabsFinali.addElement( "Animal" );
		vLabsFinali.addElement( "Outros" );
		vValsFinali.addElement( "C" );
		vValsFinali.addElement( "A" );
		vValsFinali.addElement( "O" );
		//rgFinaliFichaAval = new JRadioGroup<String, String>( 1, 3, vLabsFinali, vValsFinali );
		//rgFinaliFichaAval.setVlrString( "C" );
		*/
		Vector<String> vValsMobilidade = new Vector<String>();
		Vector<String> vLabsMobilidade = new Vector<String>();
		vLabsMobilidade.addElement( "Mobiliado" );
		vLabsMobilidade.addElement( "Semi-Mobiliado" );
		vLabsMobilidade.addElement( "Vazio" );
		vValsMobilidade.addElement( "M" );
		vValsMobilidade.addElement( "S" );
		vValsMobilidade.addElement( "V" );
		rgMobilFichaAval = new JRadioGroup<String, String>( 1, 3, vLabsMobilidade, vValsMobilidade );
		rgMobilFichaAval.setVlrString( "M" );

	}
	
	private void adicAbas() {

		pnCliCab.add( tpnCab );
		tpnCab.addTab( "Ficha Avaliativa", pinFichaAval );
		tpnCab.addTab( "Inf.Complementares", pinCabInfCompl );
		pinObs.add( spnObs );
		tpnCab.addTab( "Observações", pinObs );

	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp ) {
			impFichaAval( txtCodCont.getVlrInteger(), txtSeqFichaAval.getVlrInteger() );
	
		}		else if ( evt.getSource() == btGeraOrc ) {
			geraOrcamento();
			
		} else if (evt.getSource() == btExportCli){
			exportaCli(true);
		}
		super.actionPerformed( evt );
	}

	public void impFichaAval(final int codcont, final int seqficha){
		
		Blob fotoemp = FPrinterJob.getLogo( con );

		boolean temitens = tab.getNumLinhas() > 0 ;
		
		String layoutfichaaval = null;
		
		if (temitens) {
			layoutfichaaval = (String) daoficha.getPrefs()[FichaOrc.PREFS.LAYOUTPREFICHAAVAL.ordinal()];
		} else {
			layoutfichaaval = (String) daoficha.getPrefs()[FichaOrc.PREFS.LAYOUTFICHAAVAL.ordinal()];
		}

		if (layoutfichaaval==null) {
			Funcoes.mensagemInforma( this, "Layout's para ficha avaliativa não foram definidos !");
			return;
		}
		
		StringBuilder sql = daoficha.getSqlFichaAval(temitens);

		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement( sql.toString() );
			int param = 1;
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "TKCONTATO" ) );
			ps.setInt( param++, codcont );
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "CRFICHAAVAL" ) );
			ps.setInt( param++, seqficha );
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "SGFILIAL" ) );
			
			rs = ps.executeQuery();
		} catch (SQLException e) {
			Funcoes.mensagemErro( this, "Erro executando consulta: \n" + e.getMessage() );
			e.printStackTrace();
		}
		

		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "SGFILIAL" ) );
		hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
		hParam.put( "SUBREPORT_DIR", "org/freedom/relatorios/");
		hParam.put( "CODEMPCT", Aplicativo.iCodEmp );
		hParam.put( "CODFILIALCT", ListaCampos.getMasterFilial( "TKCONTATO" ) );
		hParam.put( "CODCTO", codcont );
		hParam.put( "CODEMPFA", Aplicativo.iCodEmp );
		hParam.put( "CODFILIALFA", ListaCampos.getMasterFilial( "CRFICHAAVAL" ) );
		hParam.put( "SEQFICHAAVAL", seqficha );
		hParam.put( "CONEXAO", con.getConnection() );
		
		
		try {
			hParam.put( "LOGOEMP", new ImageIcon(fotoemp.getBytes(1, ( int ) fotoemp.length())).getImage() );
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		
		
		dlGr = new FPrinterJob( layoutfichaaval, "Ficha avaliativa", "", rs, hParam, this );
		
		dlGr.setVisible( true );
		
	}
	
	private void exportaCli(boolean mostradl) {

		if ( txtCodCont.getVlrInteger().intValue() == 0 || lcCampos.getStatus() != ListaCampos.LCS_SELECT ) {
			Funcoes.mensagemInforma( this, "Selecione um contato cadastrado antes!" );
			return;
		}

		DLContToCli dl = new DLContToCli( this, txtCodSetor.getVlrInteger() , txtCodTipoCli.getVlrInteger() );
		dl.setConexao( con );
		dl.setVisible( true );

		if ( !dl.OK ) {
			dl.dispose();
			return;
		}

		DLContToCli.ContatoClienteBean contatoClienteBean = dl.getValores();

		dl.dispose();

		try {

			PreparedStatement ps = con.prepareStatement( daoficha.geraCli());
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, lcCampos.getCodFilial() );
			ps.setInt( 3, txtCodCont.getVlrInteger().intValue() );
			ps.setInt( 4, lcCampos.getCodFilial() );
			ps.setInt( 5, contatoClienteBean.getTipo() );
			ps.setInt( 6, lcCampos.getCodFilial() );
			ps.setInt( 7, contatoClienteBean.getClassificacao() );
			ps.setInt( 8, lcCampos.getCodFilial() );
			ps.setInt( 9, contatoClienteBean.getSetor() );

			ResultSet rs = ps.executeQuery();

		
			if ( rs.next() ) {
				if(mostradl){
					if ( Funcoes.mensagemConfirma( this, "Cliente '" + rs.getInt( 1 ) + "' criado com sucesso!\nGostaria de editá-lo agora?" ) == JOptionPane.OK_OPTION ) {
						abreCli( rs.getInt( 1 ) );
					}
				}
			}
		
			rs.close();
			ps.close();
			con.commit();

		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao criar cliente!\n" + err.getMessage(), true, con, err );
		}
	}
	
	private void geraOrcamento(){
		
	
		if ( Funcoes.mensagemConfirma( this, "Deseja realmente gerar orçamento a partir da ficha avaliativa?" ) == JOptionPane.OK_OPTION ) {
			Integer codorc = null;
			Integer codalmox = 1;
			Integer codcli = null;
			
			
			
			
			try {
				
				codcli = daoficha.getCliente( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "VDORCAMENTO" ), txtCodCont.getVlrInteger() );
				if(codcli==null){
					exportaCli(false);
					codcli = daoficha.getCliente( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "VDORCAMENTO" ), txtCodCont.getVlrInteger() );
				}
				
				//for(row = 0; row < tab.getNumLinhas(); row++){
				//	if(bPrim){
				codorc = daoficha.gravaCabOrc( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "VDORCAMENTO" ), txtSeqFichaAval.getVlrInteger(), txtCodCont.getVlrInteger(), new Date(), 
						new Date(), Integer.valueOf( daoficha.getPrefs()[FichaOrc.PREFS.CODPLANOPAG.ordinal()].toString()), Integer.valueOf( daoficha.getPrefs()[FichaOrc.PREFS.CODTRAN.ordinal()].toString()),
						Integer.valueOf( daoficha.getPrefs()[FichaOrc.PREFS.CODVEND.ordinal()].toString()), codcli,  Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "VDVENDEDOR" ), txtCodVend.getVlrInteger() );
				
				daoficha.insert_item_orc( codorc, Aplicativo.iCodEmp ,ListaCampos.getMasterFilial( "CRITFICHAAVAL" ), txtSeqFichaAval.getVlrInteger() );
				
				daoficha.insert_fichaorc( "O", codorc,  Aplicativo.iCodEmp ,ListaCampos.getMasterFilial( "CRITFICHAAVAL" ), txtSeqFichaAval.getVlrInteger() );
				
				con.commit();
				
				lcCampos.carregaDados();
				//	}
				/*	Integer numseq = (Integer) tab.getValor( row, ItOrcamento.COLITORC.SEQITFICHAAVAL.ordinal() ) ;
					
					ItOrcamento item = new ItOrcamento();
					item.setCodemp( Aplicativo.iCodEmp );
					item.setCodfilial( ListaCampos.getMasterFilial( "VDITORCAMENTO" ) );
					item.setTipoorc( "O" );
					item.setCodorc( codorc );
					item.setCoditorc( numseq );
					item.setCodemppd( Aplicativo.iCodEmp );
					item.setCodfilialpd( ListaCampos.getMasterFilial( "EQPRODUTO" ) );
					item.setCodprod(  (Integer) tab.getValor( row, ItOrcamento.COLITORC.CODPROD.ordinal() )  );
					item.setCodempax( Aplicativo.iCodEmp );
					item.setCodfilialax( ListaCampos.getMasterFilial( "EQALMOX" ) );
					item.setCodalmox( 1 );
					item.setQtditorc( Funcoes.strDecimalToBigDecimal( Aplicativo.casasDec, tab.getValor( row, ItOrcamento.COLITORC.M2.ordinal() ).toString().trim() ) );
					item.setPrecoitorc(	Funcoes.strDecimalToBigDecimal( Aplicativo.casasDec, tab.getValor( row, ItOrcamento.COLITORC.VLRUNITITFICHAAVAL.ordinal() ).toString().trim() ) );
					daoficha.insert_item_orc( item );
				*/	
					
					/*
					FichaOrc ficha = new FichaOrc();
					ficha.setCodemp( Aplicativo.iCodEmp );
					ficha.setCodfilial( ListaCampos.getMasterFilial( "CRFICHAAVAL" ) );
					ficha.setSeqfichaaval( txtSeqFichaAval.getVlrInteger() );
					ficha.setSeqitfichaaval( numseq );
					ficha.setCodempor( Aplicativo.iCodEmp );
					ficha.setCodfilialor( ListaCampos.getMasterFilial( "VDITORCAMENTO" ) );
					ficha.setTipoorc( "O" );
					ficha.setCodorc( codorc );
					ficha.setCoditorc( (Integer) tab.getValor( row, ItOrcamento.COLITORC.SEQITFICHAAVAL.ordinal() ) );					
					*/
					
				if ( Funcoes.mensagemConfirma( this, "Orçamento: '" + codorc + "' gerado com sucesso!\nGostaria de edita-lo agora?" ) == JOptionPane.OK_OPTION ) {
					abreOrc( codorc );
				}
			} catch ( SQLException e ) {
				Funcoes.mensagemErro( this, "O orçamento não pode ser gerado!" );
				e.printStackTrace();
				try {
					con.rollback();
				} catch ( SQLException e1 ) {
					e1.printStackTrace();
				}
			}

		}
	}
	
	private void abreCli( int codigoCliente ) {

		FCliente cliente = null;
		if ( Aplicativo.telaPrincipal.temTela( FCliente.class.getName() ) ) {
			cliente = (FCliente) Aplicativo.telaPrincipal.getTela( FCliente.class.getName() );
		}
		else {
			cliente = new FCliente();
			Aplicativo.telaPrincipal.criatela( "Cliente", cliente, con );
		}

		cliente.exec( codigoCliente );
	}
	
	
	
	private void abreOrc( int codigoOrcamento ) {

		FOrcamento orcamento = null;
		if ( Aplicativo.telaPrincipal.temTela( FOrcamento.class.getName() ) ) {
			orcamento = (FOrcamento) Aplicativo.telaPrincipal.getTela( FOrcamento.class.getName() );
		}
		else {
			orcamento = new FOrcamento();
			Aplicativo.telaPrincipal.criatela( "Orçamento", orcamento, con );
		}

		orcamento.exec( codigoOrcamento );
	}


	public void beforeInsert( InsertEvent ievt ) {

	}

	public void afterInsert( InsertEvent ievt ) {
		if(ievt.getListaCampos() == lcCampos) {
			txtQtdJanelaFichaAval.setVlrInteger( 0 );
			txtQtdSacadaFichaAval.setVlrInteger( 0 );
			txtAndarFichaAval.setVlrInteger( 0 );
			txtDtFichaAval.setVlrDate( new Date() );
			txtQtdJanelaFichaAval.setEnabled( false );
			txtQtdSacadaFichaAval.setEnabled( false );
			cbPredentrfichaAval.setVlrString( "X" );
		}	
		if(ievt.getListaCampos() == lcDet) {
			cbVarG1.limpa();
			cbVarG2.limpa();
			cbVarG3.limpa();
			cbVarG4.limpa();
			cbVarG5.limpa();
			cbVarG6.limpa();
			cbVarG7.limpa();
			cbVarG8.limpa();
		}	
	}
	
	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcContato.setConexao( cn );
		lcMotAval.setConexao( cn );
		lcComis.setConexao( cn );
		lcProduto.setConexao( cn );
		lcAmbAval.setConexao( cn );
		lcVariante1.setConexao( cn );
		lcVariante2.setConexao( cn );
		lcVariante3.setConexao( cn );
		lcVariante4.setConexao( cn );
		lcVariante5.setConexao( cn );
		lcVariante6.setConexao( cn );
		lcVariante7.setConexao( cn );
		lcVariante8.setConexao( cn );
		
		daoficha = new DAOFicha( cn );
		try{
		daoficha.setPrefs( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "SGPREFERE3" ) );
		}catch (SQLException e) {
			Funcoes.mensagemErro( this, "Erro carregando preferências !\b" + e.getMessage() );
			e.printStackTrace();
		}
		
		carregaCombo();
	}

	public void beforeCarrega( CarregaEvent cevt ) {
		
		
	}
	
	public void beforePost( PostEvent e ) {
		
		if ( e.getListaCampos() == lcCampos ) {
			if( ( lcCampos.getStatus() == ListaCampos.LCS_INSERT ) || ( lcCampos.getStatus() == ListaCampos.LCS_EDIT) ) {
				if( ("N".equals( cbJanelaFichaAval.getVlrString() )  ) ||  ("N".equals( cbSacadaFichaAval.getVlrString() ) ) ) { 
						//&&  ("N".equals( cbOutrosFichaAval.getVlrString() )  ) ) {
					Funcoes.mensagemInforma( this, "Preencha as informações complementares!!" );
					tpnCab.setSelectedIndex( 1 );
					tpnCab.doLayout();
					e.cancela();
					
				}
				else if( (!"S".equalsIgnoreCase( txtCobertFichaAval.getVlrString() )) && (!"N".equalsIgnoreCase( txtCobertFichaAval.getVlrString())) 
						|| (!"S".equalsIgnoreCase( txtEstrutFichaAval.getVlrString() )) && (!"N".equalsIgnoreCase( txtEstrutFichaAval.getVlrString()))
						|| (!"S".equalsIgnoreCase( txtOcupadoFichaAval.getVlrString() )) && (!"N".equalsIgnoreCase( txtOcupadoFichaAval.getVlrString()))
						) {
					Funcoes.mensagemInforma( this, "Informe S/N para os campos cobertura , estrutura e imóvel ocupado!!!" );
					tpnCab.setSelectedIndex( 1 );
					tpnCab.doLayout();
					e.cancela();
				} else {
					txtCobertFichaAval.setVlrString( txtCobertFichaAval.getVlrString().toUpperCase() );
					txtEstrutFichaAval.setVlrString( txtEstrutFichaAval.getVlrString().toUpperCase() );
					txtOcupadoFichaAval.setVlrString( txtOcupadoFichaAval.getVlrString().toUpperCase() );
				}
				System.out.println(cbPredentrfichaAval.getVlrString());
			}
		} else 	if ( e.getListaCampos() == lcDet ) {
			try {
				calcValores( null );
				
			} catch ( SQLException e1 ) {
				Funcoes.mensagemErro( this, "Erro ao calcular valores !!!" );
				e1.printStackTrace();
			}
			
			
			if( ( cbVarG1.getVlrInteger() <= 0) || ( cbVarG2.getVlrInteger() <= 0) || ( cbVarG3.getVlrInteger() <= 0) || ( cbVarG4.getVlrInteger() <= 0) || ( cbVarG5.getVlrInteger() <= 0) ||
					( cbVarG6.getVlrInteger() <= 0) || ( cbVarG7.getVlrInteger() <= 0) || ( cbVarG8.getVlrInteger() <= 0)  ){
				Funcoes.mensagemInforma( this, "Preencha todos os itens do detalhamento!!" );
				e.cancela();
			}
		}
	}
	
	public void carregaCombo() {
		
		txtCodVarG1.setVlrInteger( new Integer(daoficha.getPrefs()[FichaOrc.PREFS.CODVARG1.ordinal()].toString()));
		txtCodVarG2.setVlrInteger( new Integer(daoficha.getPrefs()[FichaOrc.PREFS.CODVARG2.ordinal()].toString()));
		txtCodVarG3.setVlrInteger( new Integer(daoficha.getPrefs()[FichaOrc.PREFS.CODVARG3.ordinal()].toString()));
		txtCodVarG4.setVlrInteger( new Integer(daoficha.getPrefs()[FichaOrc.PREFS.CODVARG4.ordinal()].toString()));
		txtCodVarG5.setVlrInteger( new Integer(daoficha.getPrefs()[FichaOrc.PREFS.CODVARG5.ordinal()].toString()));
		txtCodVarG6.setVlrInteger( new Integer(daoficha.getPrefs()[FichaOrc.PREFS.CODVARG6.ordinal()].toString()));
		txtCodVarG7.setVlrInteger( new Integer(daoficha.getPrefs()[FichaOrc.PREFS.CODVARG7.ordinal()].toString()));
		txtCodVarG8.setVlrInteger( new Integer(daoficha.getPrefs()[FichaOrc.PREFS.CODVARG8.ordinal()].toString()));
		
		
		descVar1 = daoficha.buscaDesc( txtCodVarG1.getVlrInteger() );
		lbCodVar1.setText( descVar1 );
		descVar2 = daoficha.buscaDesc( txtCodVarG2.getVlrInteger() );
		lbCodVar2.setText( descVar2 );
		descVar3 = daoficha.buscaDesc( txtCodVarG3.getVlrInteger() );
		lbCodVar3.setText( descVar3 );
		descVar4 = daoficha.buscaDesc( txtCodVarG4.getVlrInteger() );
		lbCodVar4.setText( descVar4 );
		descVar5 = daoficha.buscaDesc( txtCodVarG5.getVlrInteger() );
		lbCodVar5.setText( descVar5 );
		descVar6 = daoficha.buscaDesc( txtCodVarG6.getVlrInteger() );
		lbCodVar6.setText( descVar6 );
		descVar7 = daoficha.buscaDesc( txtCodVarG7.getVlrInteger() );
		lbCodVar7.setText( descVar7 );
		descVar8 = daoficha.buscaDesc( txtCodVarG8.getVlrInteger() );
		lbCodVar8.setText( descVar8 );

		HashMap<String, Vector<Object>> vals1 = daoficha.montaComboFicha( txtCodVarG1.getVlrInteger() , "Não Selecionado" );
		cbVarG1.setItensGeneric( (Vector<?>) vals1.get( "LAB" ), (Vector<?>) vals1.get( "VAL" ) );
		
		HashMap<String, Vector<Object>> vals2 = daoficha.montaComboFicha( txtCodVarG2.getVlrInteger() , "Não Selecionado" );
		cbVarG2.setItensGeneric( (Vector<?>) vals2.get( "LAB" ), (Vector<?>) vals2.get( "VAL" ) );
		
		HashMap<String, Vector<Object>> vals3 = daoficha.montaComboFicha( txtCodVarG3.getVlrInteger(), "Não Selecionado" );
		cbVarG3.setItensGeneric( (Vector<?>) vals3.get( "LAB" ), (Vector<?>) vals3.get( "VAL" ) );
		
		HashMap<String, Vector<Object>> vals4 = daoficha.montaComboFicha( txtCodVarG4.getVlrInteger() , "Não Selecionado" );
		cbVarG4.setItensGeneric( (Vector<?>) vals4.get( "LAB" ), (Vector<?>) vals4.get( "VAL" ) );
		
		HashMap<String, Vector<Object>> vals5 = daoficha.montaComboFicha( txtCodVarG5.getVlrInteger() , "Não Selecionado" );
		cbVarG5.setItensGeneric( (Vector<?>) vals5.get( "LAB" ), (Vector<?>) vals5.get( "VAL" ) );
		
		HashMap<String, Vector<Object>> vals6 = daoficha.montaComboFicha( txtCodVarG6.getVlrInteger() , "Não Selecionado" );
		cbVarG6.setItensGeneric( (Vector<?>) vals6.get( "LAB" ), (Vector<?>) vals6.get( "VAL" ) );
		
		HashMap<String, Vector<Object>> vals7 = daoficha.montaComboFicha( txtCodVarG7.getVlrInteger() , "Não Selecionado" );
		cbVarG7.setItensGeneric( (Vector<?>) vals7.get( "LAB" ), (Vector<?>) vals7.get( "VAL" ) );
	
		HashMap<String, Vector<Object>> vals8 = daoficha.montaComboFicha( txtCodVarG8.getVlrInteger() , "Não Selecionado" );
		cbVarG8.setItensGeneric( (Vector<?>) vals8.get( "LAB" ), (Vector<?>) vals8.get( "VAL" ) );
	
	}

	public void afterCarrega( CarregaEvent cevt ) {
		 if ( cevt.getListaCampos() == lcCampos ) {
			 carregaOrcamentos();	
					 
		 }		
		 
		 if( cevt.getListaCampos() == lcDet){
			 
			cbVarG1.setVlrInteger(txtSeqItVarG1.getVlrInteger());
			cbVarG2.setVlrInteger(txtSeqItVarG2.getVlrInteger());
			cbVarG3.setVlrInteger(txtSeqItVarG3.getVlrInteger());
			cbVarG4.setVlrInteger(txtSeqItVarG4.getVlrInteger());
			cbVarG5.setVlrInteger(txtSeqItVarG5.getVlrInteger());
			cbVarG6.setVlrInteger(txtSeqItVarG6.getVlrInteger());
			cbVarG7.setVlrInteger(txtSeqItVarG7.getVlrInteger());
			cbVarG8.setVlrInteger(txtSeqItVarG8.getVlrInteger());
		 }
	}	
	
	public void calcM2(){

		BigDecimal vlrm2 = txtAltItFichaAval.getVlrBigDecimal().multiply( txtCompItFichaAval.getVlrBigDecimal() );

		txtM2ItFichaAval.setVlrBigDecimal( vlrm2 );

	}

	public void focusGained( FocusEvent e ) {
		
	}

	public void focusLost( FocusEvent fevt ) {
		if( fevt.getSource() == txtCompItFichaAval ){
			if(( txtAltItFichaAval.getVlrBigDecimal().floatValue() > 0 ) && ( txtCompItFichaAval.getVlrBigDecimal().floatValue() > 0 ) ) {
				calcM2();
			}
			else {
				if(txtAltItFichaAval.getVlrBigDecimal().floatValue() <= 0) {
					Funcoes.mensagemInforma( this, "Informe a altura para o calculo do m²!!!" );
				} else {
					Funcoes.mensagemInforma( this, "Informe o comprimento para o calculo do m²!!!" );
				}
				txtM2ItFichaAval.setVlrBigDecimal( new BigDecimal(0) );
			}
		}
		
		if( fevt.getSource() == txtCodProd ){
			try {
				if(txtCodProd.getVlrInteger() > 0){
					calcValores( txtCodProd.getVlrInteger() );
				}
			} catch ( SQLException e ) {
				Funcoes.mensagemErro( this, "Erro ao buscar preço base do produto." );
				e.printStackTrace();
			}
		}
		if( fevt.getSource() == txtVlrTotItFichaAval ){
			try {
				calcValores( null );
			} catch ( SQLException e ) {
				Funcoes.mensagemErro( this, "Erro ao atualizar Valores..." );
				e.printStackTrace();
			}
		}
	}
	
	public void calcValores(Integer codprod) throws SQLException {
		
			MathContext mcPerc= new MathContext( 5, RoundingMode.HALF_EVEN   );
			
			if(codprod != null){
				BigDecimal precoUnit = daoficha.getPrecoBase( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "EQPRODUTO" ), codprod );
				txtVlrTotItFichaAval.setVlrBigDecimal( precoUnit.multiply( txtM2ItFichaAval.getVlrBigDecimal() ) );
			}
			
			txtVlrUnitItFichaAval.setVlrBigDecimal( txtVlrTotItFichaAval.getVlrBigDecimal().divide( txtM2ItFichaAval.getVlrBigDecimal(), mcPerc ) );
			
	}
	
	
	public void insertValorCombo(Integer codvarg, JTextFieldPad txtcodvarg, JComboBoxPad combo, JTextFieldPad txtseqitvarg){
		if( codvarg != null ){
			txtcodvarg.setVlrInteger( codvarg );		
		} else {
			txtcodvarg.setVlrInteger( 0 );
		}
		if(combo!= null){
			txtseqitvarg.setVlrInteger( combo.getVlrInteger() );		
		} else {
			txtseqitvarg.setVlrInteger( 0 );
		
		}
	}

	public void valorAlterado( JComboBoxEvent evt ) {
			
		if ( evt.getComboBoxPad() == cbVarG1 ) {
			if(cbVarG1.getVlrInteger() > 0 ){
				insertValorCombo(  new Integer(daoficha.getPrefs()[FichaOrc.PREFS.CODVARG1.ordinal()].toString()), txtCodVarG1, cbVarG1, txtSeqItVarG1 );
				//txtCodVarG1.setVlrInteger( new Integer(daoficha.getPrefs()[FichaOrc.PREFS.CODVARG1.ordinal()].toString()));
				//txtSeqItVarG1.setVlrInteger( cbVarG1.getVlrInteger() );
			//	if(lcDet.isEditable()){
			//		lcDet.edit();
			//	}
			} else {
			}
			
		} else if ( evt.getComboBoxPad() == cbVarG2 ) {
			if(cbVarG2.getVlrInteger() > 0 ){
				insertValorCombo(  new Integer(daoficha.getPrefs()[FichaOrc.PREFS.CODVARG2.ordinal()].toString()), txtCodVarG2, cbVarG2, txtSeqItVarG2 );
			} else {
			}

		} else if ( evt.getComboBoxPad() == cbVarG3 ) {
			if(cbVarG3.getVlrInteger() > 0){
				insertValorCombo(  new Integer(daoficha.getPrefs()[FichaOrc.PREFS.CODVARG3.ordinal()].toString()), txtCodVarG3, cbVarG3, txtSeqItVarG3 );	
			} else {
				}
			
		} else if ( evt.getComboBoxPad() == cbVarG4 ) {
			if(cbVarG4.getVlrInteger() > 0){
				insertValorCombo(  new Integer(daoficha.getPrefs()[FichaOrc.PREFS.CODVARG4.ordinal()].toString()), txtCodVarG4, cbVarG4, txtSeqItVarG4 );
			} else {
				
			}
			
		} else if ( evt.getComboBoxPad() == cbVarG5 ) {
			if(cbVarG5.getVlrInteger() > 0){
				insertValorCombo(  new Integer(daoficha.getPrefs()[FichaOrc.PREFS.CODVARG5.ordinal()].toString()), txtCodVarG5, cbVarG5, txtSeqItVarG5 );	
			} else {
				
			}
				
		} else if ( evt.getComboBoxPad() == cbVarG6 ) {
			if(cbVarG6.getVlrInteger() > 0){
				insertValorCombo(  new Integer(daoficha.getPrefs()[FichaOrc.PREFS.CODVARG6.ordinal()].toString()), txtCodVarG6, cbVarG6, txtSeqItVarG6 );
			} else {

			}
			
		} else if ( evt.getComboBoxPad() == cbVarG7 ) {
			if(cbVarG7.getVlrInteger() > 0){
				insertValorCombo(  new Integer(daoficha.getPrefs()[FichaOrc.PREFS.CODVARG7.ordinal()].toString()), txtCodVarG7, cbVarG7, txtSeqItVarG7 );				
			} else {
				
			}
			
		} else if ( evt.getComboBoxPad() == cbVarG8 ) {
			if(cbVarG8.getVlrInteger() > 0){
				insertValorCombo(  new Integer(daoficha.getPrefs()[FichaOrc.PREFS.CODVARG8.ordinal()].toString()), txtCodVarG8, cbVarG8, txtSeqItVarG8 );				
			} else {
				
			}
			
		}
		
	}
	
	public void keyPressed( KeyEvent kevt ) {
		if ( kevt.getKeyCode() == KeyEvent.VK_ENTER ) {
			if ( kevt.getSource() == cbFinaliOutFichaAval ) {
				// ultimo campo da aba de Ficha Avaliativa
				// então abre a tab informações complementares.
				tpnCab.setSelectedIndex( 1 );
				tpnCab.doLayout();
				rgLocalFichaAval.requestFocus();
			}
		}
	
	}

	public void valorAlterado( CheckBoxEvent evt ) {
		if( evt.getCheckBox() == cbJanelaFichaAval) {
				
			if("S".equals( cbJanelaFichaAval.getVlrString() )){
				 txtQtdJanelaFichaAval.setEnabled( true );
			} else {
				txtQtdJanelaFichaAval.setEnabled( false );
			}
		} else if( evt.getCheckBox() == cbSacadaFichaAval ) {
			if("S".equals( cbSacadaFichaAval.getVlrString() )){
				txtQtdSacadaFichaAval.setEnabled( true );
			} else {
				txtQtdSacadaFichaAval.setEnabled( false );
			}
		}
		else if( evt.getCheckBox() == cbOutrosFichaAval ) {
			if("S".equals( cbOutrosFichaAval.getVlrString() )){
				txtDescOutrosFichaAval.setEnabled( true );
			} else {
				txtDescOutrosFichaAval.setEnabled( false );
			}
		}
		
	}
}
