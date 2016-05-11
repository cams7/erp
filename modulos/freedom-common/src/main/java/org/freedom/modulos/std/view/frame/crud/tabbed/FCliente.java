/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FCliente.java <BR>
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
 *                   Tela de cadastro de clientes.
 */

package org.freedom.modulos.std.view.frame.crud.tabbed;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Calendar;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.acao.RadioGroupEvent;
import org.freedom.acao.RadioGroupListener;
import org.freedom.acao.TabelaSelEvent;
import org.freedom.acao.TabelaSelListener;
import org.freedom.bmps.Icone;
import org.freedom.business.webservice.WSCep;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.object.Endereco;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.component.PainelImagem;
import org.freedom.library.swing.dialog.DLInputText;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FAndamento;
import org.freedom.library.swing.frame.FMapa;
import org.freedom.library.swing.frame.FTabDados;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.atd.view.frame.crud.tabbed.FConveniado;
import org.freedom.modulos.cfg.view.frame.crud.plain.FMunicipio;
import org.freedom.modulos.cfg.view.frame.crud.plain.FPais;
import org.freedom.modulos.cfg.view.frame.crud.plain.FUF;
import org.freedom.modulos.crm.business.object.Atendimento;
import org.freedom.modulos.crm.business.object.Atendimento.PREFS;
import org.freedom.modulos.crm.dao.DAOAtendimento;
import org.freedom.modulos.crm.view.frame.crud.plain.FNovoAtend;
import org.freedom.modulos.crm.view.frame.utility.FCRM.COL_ATENDIMENTO;
import org.freedom.modulos.fnc.library.swing.component.JTextFieldPlan;
import org.freedom.modulos.fnc.view.frame.crud.plain.FBanco;
import org.freedom.modulos.fnc.view.frame.crud.plain.FCartCob;
import org.freedom.modulos.fnc.view.frame.crud.tabbed.FHistPad;
import org.freedom.modulos.std.dao.DAOCliente;
import org.freedom.modulos.std.view.dialog.report.DLRCliente;
import org.freedom.modulos.std.view.dialog.report.DLRCliente.RET_DLRCLIENTE;
import org.freedom.modulos.std.view.dialog.utility.DLCopiaCliente;
import org.freedom.modulos.std.view.dialog.utility.DLGrpCli;
import org.freedom.modulos.std.view.frame.crud.detail.FPlanoPag;
import org.freedom.modulos.std.view.frame.crud.plain.FClasCli;
import org.freedom.modulos.std.view.frame.crud.plain.FSetor;
import org.freedom.modulos.std.view.frame.crud.plain.FTipoCli;
import org.freedom.modulos.std.view.frame.crud.plain.FTipoCob;
import org.freedom.modulos.std.view.frame.crud.plain.FTipoFisc;

public class FCliente extends FTabDados implements RadioGroupListener, PostListener, ActionListener, TabelaSelListener, ChangeListener, CarregaListener, InsertListener, FocusListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinEnt = new JPanelPad();

	private JPanelPad pinVend = new JPanelPad();

	private JPanelPad pinCob = new JPanelPad();

	private JPanelPad pinMapa = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnObs1 = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() ); // JPanelPad de observações com 2 linha e 1 coluna

	private JPanelPad pnObs1_1 = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() ); // JPanelPad de observações gerais

	private JPanelPad pnObs1_2 = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() ); // JPanelPad principal de observações por data

	private JPanelPad pnObs1_2_1 = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnObs1_2_2 = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() ); // Pinel para observações e outros

	private JPanelPad pnObs1_2_2_1 = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnObs1_2_2_2 = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinObs1_2_1_1 = new JPanelPad( 200, 200 );

	private JPanelPad pinObs1_2_2_2_1 = new JPanelPad( 0, 30 );

	private JPanelPad pinCli = new JPanelPad();

	private JPanelPad pnFor = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnCliFor = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinFor = new JPanelPad( 0, 80 );

	private JPanelPad pinCliFor = new JPanelPad( 0, 80 );

	private JPanelPad pinTesteFor = new JPanelPad( 0, 30 );

	private JPanelPad pnCto = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnCont = new JPanelPad( new Dimension( 600, 400 ) );

	private JPanelPad pinContatos = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinAtendimento = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinAtdBt = new JPanelPad(0, 32);

	//private JPanelPad pinHistorico = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	//private JPanelPad pinHistbt = new JPanelPad( 0, 32 );

	private JPanelPad pinMetaVend = new JPanelPad( 0, 160 );

	private JPanelPad pnMetaVend = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinImages = new JPanelPad();

	private JPanelPad pinMes1 = new JPanelPad();

	private JPanelPad pinMes2 = new JPanelPad();

	private JPanelPad pinMes3 = new JPanelPad();

	private JPanelPad pinMes4 = new JPanelPad();

	private JPanelPad pinMes5 = new JPanelPad();

	private JPanelPad pinMes6 = new JPanelPad();

	private JPanelPad pinMes7 = new JPanelPad();

	private JPanelPad pinMes8 = new JPanelPad();

	private JPanelPad pinMes9 = new JPanelPad();

	private JPanelPad pinMes10 = new JPanelPad();

	private JPanelPad pinMes11 = new JPanelPad();

	private JPanelPad pinMes12 = new JPanelPad();

	private JTablePad tbObsData = new JTablePad();

	private JTablePad tabMetaVend = new JTablePad();

	private JTablePad tabFor = new JTablePad();

	private JTablePad tabCliFor = new JTablePad();

	//private JTablePad tabHist = new JTablePad();
	private JTablePad tabatd = new JTablePad();

	private PainelImagem fotoCli = new PainelImagem( 65000 );

	private PainelImagem imgAssCli = new PainelImagem( 65000 );

	private JTabbedPanePad tpnCont = new JTabbedPanePad();

	private JTextFieldPad txtAno = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );

	private JTextFieldPad txtAntQtdContJan = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtAntQtdContFev = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtAntQtdContMar = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtAntQtdContAbr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtAntQtdContMai = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtAntQtdContJun = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtAntQtdContJul = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtAntQtdContAgo = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtAntQtdContSet = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtAntQtdContOut = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtAntQtdContNov = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtAntQtdContDez = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtNovaQtdContJan = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtNovaQtdContFev = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtNovaQtdContMar = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtNovaQtdContAbr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtNovaQtdContMai = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtNovaQtdContJun = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtNovaQtdContJul = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtNovaQtdContAgo = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtNovaQtdContSet = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtNovaQtdContOut = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtNovaQtdContNov = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtNovaQtdContDez = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtRazCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtNomeCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtContCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtContCliCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtContCliEnt = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodTipoCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodCnae = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private JTextFieldFK txtDescCnae = new JTextFieldFK( JTextFieldPad.TP_STRING, 100, 0 );

	private JTextFieldPad txtCnpjCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtInscCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCpfCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 11, 0 );

	private JTextFieldPad txtRgCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtSSPCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtEndCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNumCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtComplCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtBairCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtSuframaCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 9, 0 );

	private JTextFieldPad txtCidCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtUFCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtCepCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtDDDCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtFoneCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 12, 0 );

	private JTextFieldPad txtRamalCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 6, 0 );

	private JTextFieldPad txtDDDFaxCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtFaxCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtEmailCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtEmailNfeCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtEmailEnt = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtEmailCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtSiteCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtEdificioCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtDtNascCli = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0);

	private JTextFieldPad txtIncraCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private JTextFieldPad txtEndCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNumCob = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtComplCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtBairCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtCidCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtCepCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtUFCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtDDDFoneCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtFoneCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 12, 0 );

	private JTextFieldPad txtDDDFaxCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtFaxCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtEndEnt = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNumEnt = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtComplEnt = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtBairEnt = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtCidEnt = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtCepEnt = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtUFEnt = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtDDDFoneEnt = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtFoneEnt = new JTextFieldPad( JTextFieldPad.TP_STRING, 12, 0 );

	private JTextFieldPad txtDDDFaxEnt = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtFaxEnt = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtDDDCelEnt = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtCelEnt = new JTextFieldPad( JTextFieldPad.TP_STRING, 9, 0 );

	private JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodFiscCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescFiscCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodPlanoPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescPlanoPag = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodTran = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTran = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodTipoCob = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoCob = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodBanco = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private JTextFieldPad txtAgenciaCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 6, 0 );

	private JTextFieldPad txtNContaBcoCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldFK txtNomeBanco = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodSetor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescSetor = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodPais = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescPais = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodPaisEnt = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescPaisEnt = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodPaisCob = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescPaisCob = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodMunic = new JTextFieldPad( JTextFieldPad.TP_STRING, 7, 0 );

	private JTextFieldFK txtDescMun = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtDDDMun = new JTextFieldFK( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldFK txtDDDMunCob = new JTextFieldFK( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldFK txtDDDMunEnt = new JTextFieldFK( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtCodMunicEnt = new JTextFieldPad( JTextFieldPad.TP_STRING, 7, 0 );

	private JTextFieldFK txtDescMunEnt = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodMunicCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 7, 0 );

	private JTextFieldFK txtDescMunCob = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtSiglaUF = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtNomeUF = new JTextFieldFK( JTextFieldPad.TP_STRING, 80, 0 );

	private JTextFieldPad txtSiglaUFCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtNomeUFCob = new JTextFieldFK( JTextFieldPad.TP_STRING, 80, 0 );

	private JTextFieldPad txtSiglaUFEnt = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtPercDescCli = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 4, 2 );

	private JTextFieldFK txtNomeUFEnt = new JTextFieldFK( JTextFieldPad.TP_STRING, 80, 0 );

	private JTextFieldPad txtCodPesq = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescPesq = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodClas = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescClas = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtDDDCelCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtCelCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodForCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeForCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtcnpjForCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldFK txtcpfForCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 11, 0 );

	private JTextFieldFK txtBairForCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldFK txtEndForCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtNumForCli = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtinscForCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtInscMunCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldFK txtCnpjForCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 15, 0 );

	private JTextFieldFK txtrgForCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtCodCliFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodCpCliFor = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldFK txtDescFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtDescForCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtAnoMetaVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtVlrMetaVend = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtCodContDeb = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCodContCred = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCodCliContab = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCodHistPad = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescHistPad = new JTextFieldFK( JTextFieldPad.TP_STRING, 80, 0 );

	private JTextFieldPad txtCodCartCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private JTextFieldFK txtDescCartCob = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	/*private JTextFieldPad txtCodAtendAtendo = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeAtendAtendo = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodAtendenteChamado = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeAtendenteChamado = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtAcesAtdoLerOutAtendo = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtAcesAtdoAltOutAtendo = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtAcesAtdoDelOutAtendo = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtAcesAtdoDelLanAtendo = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );
	 */
	private JTextAreaPad txaObs = new JTextAreaPad();

	private JTextAreaPad txaTxtObsCli = new JTextAreaPad(); // Campo memo para observações por data

	private JTextAreaPad txaObsMetaVend = new JTextAreaPad();

	private JRadioGroup<String, String> rgPessoa = null;

	private JRadioGroup<String, String> rgIdentCliBco = null;

	private JCheckBoxPad cbAtivo = new JCheckBoxPad( "Ativo", "S", "N" );

	private JCheckBoxPad cbSimples = new JCheckBoxPad( "Simples", "S", "N" );

	private JCheckBoxPad cbProdRural = new JCheckBoxPad( "Rural", "S", "N" );

	private JCheckBoxPad cbContato = new JCheckBoxPad( "Contato", "O", "C" );

	private JButtonPad btAtEntrega = new JButtonPad( Icone.novo( "btReset.png" ) );

	private JButtonPad btMapa = new JButtonPad( Icone.novo( "btMapa.png" ) );

	private JButtonPad btAtCobranca = new JButtonPad( Icone.novo( "btReset.png" ) );

	private JButtonPad btNovaObs = new JButtonPad( Icone.novo( "btNovo.png" ) );

	private JButtonPad btExclObs = new JButtonPad( Icone.novo( "btExcluir.png" ) );

	private JButtonPad btEditObs = new JButtonPad( Icone.novo( "btEditar.gif" ) );

	private JButtonPad btGrpCli = new JButtonPad( Icone.novo( "btCliente.png" ) );

	private JButtonPad btSetaQtdJan = new JButtonPad( Icone.novo( "btExecuta2.gif" ) );

	private JButtonPad btSetaQtdFev = new JButtonPad( Icone.novo( "btExecuta2.gif" ) );

	private JButtonPad btSetaQtdMar = new JButtonPad( Icone.novo( "btExecuta2.gif" ) );

	private JButtonPad btSetaQtdAbr = new JButtonPad( Icone.novo( "btExecuta2.gif" ) );

	private JButtonPad btSetaQtdMai = new JButtonPad( Icone.novo( "btExecuta2.gif" ) );

	private JButtonPad btSetaQtdJun = new JButtonPad( Icone.novo( "btExecuta2.gif" ) );

	private JButtonPad btSetaQtdJul = new JButtonPad( Icone.novo( "btExecuta2.gif" ) );

	private JButtonPad btSetaQtdAgo = new JButtonPad( Icone.novo( "btExecuta2.gif" ) );

	private JButtonPad btSetaQtdSet = new JButtonPad( Icone.novo( "btExecuta2.gif" ) );

	private JButtonPad btSetaQtdOut = new JButtonPad( Icone.novo( "btExecuta2.gif" ) );

	private JButtonPad btSetaQtdNov = new JButtonPad( Icone.novo( "btExecuta2.gif" ) );

	private JButtonPad btSetaQtdDez = new JButtonPad( Icone.novo( "btExecuta2.gif" ) );

	private JButtonPad btMudaTudo = new JButtonPad( "Alterar todos", Icone.novo( "btExecuta.png" ) );

	private JButtonPad btNovoAtd = new JButtonPad( Icone.novo( "btNovo.png" ) );

	private JButtonPad btExcluirAtd = new JButtonPad( Icone.novo( "btExcluir.png" ) );

	/*private JButtonPad btNovoHist = new JButtonPad( Icone.novo( "btNovo.png" ) );

	private JButtonPad btExcluiHist = new JButtonPad( Icone.novo( "btExcluir.png" ) );
	 */
	private JButtonPad btFirefox = new JButtonPad( Icone.novo( "chrome.png" ) );

	private JButtonPad btBuscaEnd = new JButtonPad( Icone.novo( "btBuscacep.png" ) );

	private JButtonPad btBuscaFor = new JButtonPad( Icone.novo( "btPesquisa.png" ) );

	private JButtonPad btCopiar = new JButtonPad( Icone.novo( "btCopiar.png" ) );

	private ListaCampos lcTipoCli = new ListaCampos( this, "TI" );

	private ListaCampos lcCnae = new ListaCampos( this, "" );

	private ListaCampos lcTipoFiscCli = new ListaCampos( this, "FC" );

	private ListaCampos lcVend = new ListaCampos( this, "VD" );

	private ListaCampos lcPlanoPag = new ListaCampos( this, "PG" );

	private ListaCampos lcTran = new ListaCampos( this, "TN" );

	private ListaCampos lcTipoCob = new ListaCampos( this, "TC" );

	private ListaCampos lcBanco = new ListaCampos( this, "BO" );

	private ListaCampos lcSetor = new ListaCampos( this, "SR" );

	private ListaCampos lcClas = new ListaCampos( this, "CC" );

	private ListaCampos lcPesq = new ListaCampos( this, "PQ" );

	private ListaCampos lcCliFor = new ListaCampos( this );

	private ListaCampos lcClixFor = new ListaCampos( this );

	private ListaCampos lcMetaVend = new ListaCampos( this );

	private ListaCampos lcFor = new ListaCampos( this, "FR" );

	private ListaCampos lcForCli = new ListaCampos( this, "FR" );

	private ListaCampos lcPais = new ListaCampos( this, "" );

	private ListaCampos lcPaisEnt = new ListaCampos( this, "" );

	private ListaCampos lcPaisCob = new ListaCampos( this, "" );

	private ListaCampos lcHistorico = new ListaCampos( this, "HP" );

	private ListaCampos lcCartCob = new ListaCampos( this, "CB" );

	private ListaCampos lcAtendenteAtendimento = new ListaCampos( this, "AE" );

	private ListaCampos lcUF = new ListaCampos( this );

	private ListaCampos lcUFEnt = new ListaCampos( this );

	private ListaCampos lcUFCob = new ListaCampos( this );

	private ListaCampos lcMunic = new ListaCampos( this );

	private ListaCampos lcMunicEnt = new ListaCampos( this );

	private ListaCampos lcMunicCob = new ListaCampos( this );

	private Navegador navFor = new Navegador( true );

	private Navegador navClixFor = new Navegador( true );

	private Navegador navMetaVend = new Navegador( false );

	private FConveniado telaConv;

	private Map<String, Object> bPref = null;

	private boolean bExecCargaObs = false;

	private String sURLBanco = null;

	private Integer codatend_atual = null;

	private Integer codvend_atual = null;

	private JCheckBoxPad cbDescIpi = new JCheckBoxPad( "Habilita desconto do IPI", "S", "N" );

	private DAOCliente daocli;

	private DAOAtendimento daoatendo;

	private Map<String, Object> atendente = null;

	private boolean acesatdoaltout = false;

	private boolean acesatdolerout = false;

	private boolean acesatdodellan = false;

	private boolean acesatdodelout = false;

	private boolean acestrocomis = false;

	private boolean acestrocomisout = false;

	public FCliente() {

		super();

		setTitulo( "Cadastro de Clientes" );
		setAtribos( 50, 20, 545, 720 );

		lcCliFor.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcCliFor );
		lcClixFor.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcClixFor );
		lcCliFor.setTabela( tabFor );
		lcClixFor.setTabela( tabCliFor );
		lcMetaVend.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcMetaVend );
		lcMetaVend.setTabela( tabMetaVend );

		pinCli = new JPanelPad( 500, 330 );
		setPainel( pinCli );
		setImprimir( true );
	}

	private void montaTela() {

		nav.setNavigation( true );

		adicTab( "Cliente", pinCli );

		lcCampos.addPostListener( this );
		lcCampos.addInsertListener( this );

		lcTipoCli.add( new GuardaCampo( txtCodTipoCli, "CodTipoCli", "Cód.tp.cli.", ListaCampos.DB_PK, true ) );
		lcTipoCli.add( new GuardaCampo( txtDescTipoCli, "DescTipoCli", "Descrição do tipo de cliente", ListaCampos.DB_SI, false ) );
		lcTipoCli.montaSql( false, "TIPOCLI", "VD" );
		lcTipoCli.setQueryCommit( false );
		lcTipoCli.setReadOnly( true );
		txtCodTipoCli.setTabelaExterna( lcTipoCli, FTipoCli.class.getCanonicalName() );

		lcCnae.setUsaME( false );
		lcCnae.add( new GuardaCampo( txtCodCnae, "CodCnae", "Cód.CNAE", ListaCampos.DB_PK, false ) );
		lcCnae.add( new GuardaCampo( txtDescCnae, "DescCnae", "Descrição da atividade principal (CNAE)", ListaCampos.DB_SI, false ) );
		lcCnae.montaSql( false, "CNAE", "SG" );
		lcCnae.setQueryCommit( false );
		lcCnae.setReadOnly( true );
		txtCodCnae.setTabelaExterna( lcCnae, null );

		lcVend.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.comiss.", ListaCampos.DB_PK, false ) );
		lcVend.add( new GuardaCampo( txtDescVend, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, false ) );
		lcVend.montaSql( false, "VENDEDOR", "VD" );
		lcVend.setQueryCommit( false );
		lcVend.setReadOnly( true );
		txtCodVend.setTabelaExterna( lcVend, FVendedor.class.getCanonicalName() );
		lcVend.addCarregaListener( this );

		lcPlanoPag.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_PK, false ) );
		lcPlanoPag.add( new GuardaCampo( txtDescPlanoPag, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPag.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag.setQueryCommit( false );
		lcPlanoPag.setReadOnly( true );
		txtCodPlanoPag.setTabelaExterna( lcPlanoPag, FPlanoPag.class.getCanonicalName() );

		lcTran.add( new GuardaCampo( txtCodTran, "CodTran", "Cód.tran.", ListaCampos.DB_PK, false ) );
		lcTran.add( new GuardaCampo( txtDescTran, "NomeTran", "Razão social da transportadora", ListaCampos.DB_SI, false ) );
		lcTran.montaSql( false, "TRANSP", "VD" );
		lcTran.setQueryCommit( false );
		lcTran.setReadOnly( true );
		txtCodTran.setTabelaExterna( lcTran, FTransp.class.getCanonicalName() );

		lcPais.setUsaME( false );
		lcPais.add( new GuardaCampo( txtCodPais, "CodPais", "Cod.país.", ListaCampos.DB_PK, true ) );
		lcPais.add( new GuardaCampo( txtDescPais, "NomePais", "Nome", ListaCampos.DB_SI, false ) );
		lcPais.montaSql( false, "PAIS", "SG" );
		lcPais.setQueryCommit( false );
		lcPais.setReadOnly( true );
		txtCodPais.setTabelaExterna( lcPais, FPais.class.getCanonicalName() );

		lcPaisEnt.setUsaME( false );
		lcPaisEnt.add( new GuardaCampo( txtCodPaisEnt, "CodPais", "Cod.país.", ListaCampos.DB_PK, false ) );
		lcPaisEnt.add( new GuardaCampo( txtDescPaisEnt, "NomePais", "Nome", ListaCampos.DB_SI, false ) );
		lcPaisEnt.montaSql( false, "PAIS", "SG" );
		lcPaisEnt.setQueryCommit( false );
		lcPaisEnt.setReadOnly( true );
		txtCodPaisEnt.setTabelaExterna( lcPaisEnt, FPais.class.getCanonicalName() );

		lcPaisCob.setUsaME( false );
		lcPaisCob.add( new GuardaCampo( txtCodPaisCob, "CodPais", "Cod.país.", ListaCampos.DB_PK, false ) );
		lcPaisCob.add( new GuardaCampo( txtDescPaisCob, "NomePais", "Nome", ListaCampos.DB_SI, false ) );
		lcPaisCob.montaSql( false, "PAIS", "SG" );
		lcPaisCob.setQueryCommit( false );
		lcPaisCob.setReadOnly( true );
		txtCodPaisCob.setTabelaExterna( lcPaisCob, FPais.class.getCanonicalName() );

		lcTipoCob.add( new GuardaCampo( txtCodTipoCob, "CodTipoCob", "Cód.tp.cob.", ListaCampos.DB_PK, false ) );
		lcTipoCob.add( new GuardaCampo( txtDescTipoCob, "DescTipoCob", "Descrição do tipo de cobrança", ListaCampos.DB_SI, false ) );
		lcTipoCob.montaSql( false, "TIPOCOB", "FN" );
		lcTipoCob.setQueryCommit( false );
		lcTipoCob.setReadOnly( true );
		txtCodTipoCob.setTabelaExterna( lcTipoCob, FTipoCob.class.getCanonicalName() );

		lcTipoFiscCli.add( new GuardaCampo( txtCodFiscCli, "CodFiscCli", "Cód.tp.fisc.", ListaCampos.DB_PK, false ) );
		lcTipoFiscCli.add( new GuardaCampo( txtDescFiscCli, "DescFiscCli", "Descrição do tipo fiscal", ListaCampos.DB_SI, false ) );
		lcTipoFiscCli.montaSql( false, "TIPOFISCCLI", "LF" );
		lcTipoFiscCli.setQueryCommit( false );
		lcTipoFiscCli.setReadOnly( true );
		txtCodFiscCli.setTabelaExterna( lcTipoFiscCli, FTipoFisc.class.getCanonicalName() );

		lcBanco.add( new GuardaCampo( txtCodBanco, "CodBanco", "Cód.banco", ListaCampos.DB_PK, false ) );
		lcBanco.add( new GuardaCampo( txtNomeBanco, "NomeBanco", "Nome do banco", ListaCampos.DB_SI, false ) );
		lcBanco.montaSql( false, "BANCO", "FN" );
		lcBanco.setQueryCommit( false );
		lcBanco.setReadOnly( true );
		txtCodBanco.setTabelaExterna( lcBanco, FBanco.class.getCanonicalName() );

		/************************
		 * CARTEIRA DE COBRANÇA *
		 ************************/

		txtCodCartCob.setNomeCampo( "CodCartCob" );
		lcCartCob.add( new GuardaCampo( txtCodCartCob, "CodCartCob", "Cód.cart.cob", ListaCampos.DB_PK, false ) );
		lcCartCob.add( new GuardaCampo( txtCodBanco, "CodBanco", "Cód.banco", ListaCampos.DB_PF, false ) );
		lcCartCob.add( new GuardaCampo( txtDescCartCob, "DescCartCob", "Desc.Cart.Cob", ListaCampos.DB_SI, false ) );
		lcCartCob.setDinWhereAdic( "CODBANCO = #S", txtCodBanco );
		lcCartCob.montaSql( false, "CARTCOB", "FN" );
		lcCartCob.setQueryCommit( false );
		lcCartCob.setReadOnly( true );
		txtCodCartCob.setTabelaExterna( lcCartCob, FCartCob.class.getCanonicalName() );
		txtCodCartCob.setListaCampos( lcCartCob );
		// txtDescCartCob.setListaCampos( lcCartCob );
		txtCodCartCob.setFK( true );

		/***************
		 * SETOR *
		 **************/

		txtCodSetor.setNomeCampo( "codsetor" );
		lcSetor.add( new GuardaCampo( txtCodSetor, "CodSetor", "Cód.setor", ListaCampos.DB_PK, txtDescSetor, false ) );
		lcSetor.add( new GuardaCampo( txtDescSetor, "DescSetor", "Descrição do setor", ListaCampos.DB_SI, false ) );
		lcSetor.montaSql( false, "SETOR", "VD" );
		lcSetor.setReadOnly( true );
		lcSetor.setQueryCommit( false );
		txtCodSetor.setTabelaExterna( lcSetor, FSetor.class.getCanonicalName() );
		txtCodSetor.setListaCampos( lcSetor );
		txtDescSetor.setListaCampos( lcSetor );

		/***************
		 * MUNICIPIO *
		 **************/

		lcMunic.setUsaME( false );
		lcMunic.add( new GuardaCampo( txtCodMunic, "CodMunic", "Cód.Munic.", ListaCampos.DB_PK, true ) );
		lcMunic.add( new GuardaCampo( txtDescMun, "NomeMunic", "Nome Munic.", ListaCampos.DB_SI, false ) );
		lcMunic.add( new GuardaCampo( txtDDDMun, "DDDMunic", "DDD Munic.", ListaCampos.DB_SI, false ) );
		lcMunic.setDinWhereAdic( "SIGLAUF = #S", txtSiglaUF );
		lcMunic.montaSql( false, "MUNICIPIO", "SG" );
		lcMunic.setQueryCommit( false );
		lcMunic.setReadOnly( true );
		txtCodMunic.setTabelaExterna( lcMunic, FMunicipio.class.getCanonicalName() );

		/***************
		 * UF *
		 **************/

		lcUF.setUsaME( false );
		lcUF.add( new GuardaCampo( txtSiglaUF, "SiglaUf", "Sigla", ListaCampos.DB_PK, true ) );
		lcUF.add( new GuardaCampo( txtNomeUF, "NomeUf", "Nome", ListaCampos.DB_SI, false ) );
		lcMunic.setDinWhereAdic( "CODPAIS = #S", txtCodPais );
		lcUF.montaSql( false, "UF", "SG" );
		lcUF.setQueryCommit( false );
		lcUF.setReadOnly( true );
		txtSiglaUF.setTabelaExterna( lcUF, FUF.class.getCanonicalName() );

		/******************
		 * MUNICIPIO ENT *
		 ******************/

		lcMunicEnt.setUsaME( false );
		lcMunicEnt.add( new GuardaCampo( txtCodMunicEnt, "CodMunic", "Cód.Muni", ListaCampos.DB_PK, false ) );
		lcMunicEnt.add( new GuardaCampo( txtDescMunEnt, "NomeMunic", "Nome Muni.", ListaCampos.DB_SI, false ) );
		lcMunicEnt.add( new GuardaCampo( txtDDDMunEnt, "DDDMunic", "DDD Munic.", ListaCampos.DB_SI, false ) );
		lcMunicEnt.setDinWhereAdic( "SIGLAUF = #S", txtSiglaUFEnt );
		lcMunicEnt.montaSql( false, "MUNICIPIO", "SG" );
		lcMunicEnt.setQueryCommit( false );
		lcMunicEnt.setReadOnly( true );
		txtCodMunicEnt.setTabelaExterna( lcMunicEnt, FMunicipio.class.getCanonicalName() );

		/***************
		 * UF ENT *
		 **************/

		lcUFEnt.setUsaME( false );
		lcUFEnt.add( new GuardaCampo( txtSiglaUFEnt, "SiglaUf", "Sigla", ListaCampos.DB_PK, false ) );
		lcUFEnt.add( new GuardaCampo( txtNomeUFEnt, "NomeUf", "Nome", ListaCampos.DB_SI, false ) );
		lcUFEnt.setDinWhereAdic( "CODPAIS = #S", txtCodPais );
		lcUFEnt.montaSql( false, "UF", "SG" );
		lcUFEnt.setQueryCommit( false );
		lcUFEnt.setReadOnly( true );
		txtSiglaUFEnt.setTabelaExterna( lcUFEnt, FUF.class.getCanonicalName() );

		/******************
		 * MUNICIPIO COB *
		 ******************/

		lcMunicCob.setUsaME( false );
		lcMunicCob.add( new GuardaCampo( txtCodMunicCob, "CodMunic", "Cód.Muni", ListaCampos.DB_PK, false ) );
		lcMunicCob.add( new GuardaCampo( txtDescMunCob, "NomeMunic", "Nome Muni.", ListaCampos.DB_SI, false ) );
		lcMunicCob.add( new GuardaCampo( txtDDDMunCob, "DDDMunic", "DDD Munic.", ListaCampos.DB_SI, false ) );
		lcMunicCob.setDinWhereAdic( "SIGLAUF = #S", txtSiglaUFCob );
		lcMunicCob.montaSql( false, "MUNICIPIO", "SG" );
		lcMunicCob.setQueryCommit( false );
		lcMunicCob.setReadOnly( true );
		txtCodMunicCob.setTabelaExterna( lcMunicCob, FMunicipio.class.getCanonicalName() );

		/***************
		 * UF COB *
		 **************/

		lcUFCob.setUsaME( false );
		lcUFCob.add( new GuardaCampo( txtSiglaUFCob, "SiglaUf", "Sigla", ListaCampos.DB_PK, false ) );
		lcUFCob.add( new GuardaCampo( txtNomeUFCob, "NomeUf", "Nome", ListaCampos.DB_SI, false ) );
		lcUFCob.setDinWhereAdic( "CODPAIS = #S", txtCodPais );
		lcUFCob.montaSql( false, "UF", "SG" );
		lcUFCob.setQueryCommit( false );
		lcUFCob.setReadOnly( true );
		txtSiglaUFCob.setTabelaExterna( lcUFCob, FUF.class.getCanonicalName() );

		lcPesq.add( new GuardaCampo( txtCodPesq, "CodCli", "Cód.cli.p.", ListaCampos.DB_PK, false ) );
		lcPesq.add( new GuardaCampo( txtDescPesq, "RazCli", "Razão social do cliente pricipal", ListaCampos.DB_SI, false ) );
		lcPesq.montaSql( false, "CLIENTE", "VD" );
		lcPesq.setQueryCommit( false );
		lcPesq.setReadOnly( true );
		txtCodPesq.setTabelaExterna( lcPesq, null );

		lcClas.add( new GuardaCampo( txtCodClas, "CodClasCli", "Cód.c.cli.", ListaCampos.DB_PK, true ) );
		lcClas.add( new GuardaCampo( txtDescClas, "DescClasCli", "Descrição da classificação do cliente", ListaCampos.DB_SI, false ) );
		lcClas.montaSql( false, "CLASCLI", "VD" );
		lcClas.setQueryCommit( false );
		lcClas.setReadOnly( true );
		txtCodClas.setTabelaExterna( lcClas, FClasCli.class.getCanonicalName() );

		lcHistorico.add( new GuardaCampo( txtCodHistPad, "CodHist", "Cód.hist.", ListaCampos.DB_PK, false ) );
		lcHistorico.add( new GuardaCampo( txtDescHistPad, "DescHist", "Descrição do historico padrão", ListaCampos.DB_SI, false ) );
		lcHistorico.montaSql( false, "HISTPAD", "FN" );
		lcHistorico.setQueryCommit( false );
		lcHistorico.setReadOnly( true );
		txtCodHistPad.setTabelaExterna( lcHistorico, FHistPad.class.getCanonicalName() );

		/*

		txtCodAtendAtendo.setTabelaExterna( lcAtendenteAtendimento, FAtendente.class.getCanonicalName() );
		txtCodAtendAtendo.setFK( true );
		txtCodAtendAtendo.setNomeCampo( "CodAtend" );
		lcAtendenteAtendimento.add( new GuardaCampo( txtCodAtendAtendo, "CodAtend", "Cód.atend.", ListaCampos.DB_PK, false ) );
		lcAtendenteAtendimento.add( new GuardaCampo( txtNomeAtendAtendo, "NomeAtend", "Nome", ListaCampos.DB_SI, false ) );
		lcAtendenteAtendimento.add( new GuardaCampo( txtAcesAtdoLerOutAtendo, "AcesAtdoLerOut", "Acesso leitura", ListaCampos.DB_SI, false ) );
		lcAtendenteAtendimento.add( new GuardaCampo( txtAcesAtdoAltOutAtendo, "AcesAtdoAltOut", "Acesso alteração", ListaCampos.DB_SI, false ) );
		lcAtendenteAtendimento.add( new GuardaCampo( txtAcesAtdoDelLanAtendo, "AcesAtdoDelLan", "Acesso exclusão", ListaCampos.DB_SI, false ) );
		lcAtendenteAtendimento.add( new GuardaCampo( txtAcesAtdoDelOutAtendo, "AcesAtdoDelOut", "Acesso exclusão", ListaCampos.DB_SI, false ) );

		lcAtendenteAtendimento.montaSql( false, "ATENDENTE", "AT" );
		lcAtendenteAtendimento.setReadOnly( true );

		 */		adicCampo( txtCodCli, 7, 20, 80, 20, "CodCli", "Cód.cli.", ListaCampos.DB_PK, true );
		 adicCampo( txtRazCli, 90, 20, 322, 20, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, true );
		 adicCampo( txtNomeCli, 90, 60, 322, 20, "NomeCli", "Nome", ListaCampos.DB_SI, true );

		 Vector<String> vPessoaLab = new Vector<String>();
		 Vector<String> vPessoaVal = new Vector<String>();

		 vPessoaLab.addElement( "Jurídica" );
		 vPessoaLab.addElement( "Física" );
		 vPessoaVal.addElement( "J" );
		 vPessoaVal.addElement( "F" );
		 rgPessoa = new JRadioGroup<String, String>( 2, 1, vPessoaLab, vPessoaVal );
		 rgPessoa.addRadioGroupListener( this );

		 adicDB( rgPessoa, 415, 20, 100, 60, "PessoaCli", "Pessoa", true );
		 rgPessoa.setVlrString( "J" );

		 cbAtivo.setEnabled( (Boolean) bPref.get( "HABATIVCLI" ) );

		 adicDB( cbAtivo, 7, 60, 70, 20, "AtivoCli", "Ativo", true );
		 adicCampo( txtCodTipoCli, 7, 100, 80, 20, "CodTipoCli", "Cód.tp.cli.", ListaCampos.DB_FK, txtDescTipoCli, true );
		 adicDescFK( txtDescTipoCli, 90, 100, 325, 20, "DescTipoCli", "Descrição do tipo de cliente" );

		 adicDB( cbSimples, 425, 100, 80, 20, "SimplesCli", "", true );
		 adicDB( cbProdRural, 425, 120, 80, 20, "ProdRuralCli", "", true );
		 adicDB( cbContato, 425, 140, 80, 20, "CtoCli", "", true );

		 adicCampo( txtCodClas, 7, 140, 80, 20, "CodClasCli", "Cód.c.cli", ListaCampos.DB_FK, txtDescClas, true );
		 adicDescFK( txtDescClas, 90, 140, 325, 20, "DescClasCli", "Descrição da classificação do cliente" );

		 adicCampo( txtCodCnae, 7, 180, 80, 20, "CodCnae", "Cód.CNAE", ListaCampos.DB_FK, txtDescCnae, false );
		 adicDescFK( txtDescCnae, 90, 180, 325, 20, "DescCnae", "Descrição da atividade principal (CNAE)" );

		 adicCampo( txtCnpjCli, 7, 220, 120, 20, "CnpjCli", "CNPJ", ListaCampos.DB_SI, false );
		 adicCampo( txtInscCli, 130, 220, 107, 20, "InscCli", "Inscrição Estadual", ListaCampos.DB_SI, false );
		 adicCampo( txtCpfCli, 240, 220, 120, 20, "CpfCli", "CPF", ListaCampos.DB_SI, false );
		 adicCampo( txtRgCli, 363, 220, 80, 20, "RgCli", "RG", ListaCampos.DB_SI, false );
		 adicCampo( txtSSPCli, 446, 220, 70, 20, "SSPCli", "Orgão exp.", ListaCampos.DB_SI, false );
		 adicCampo( txtCepCli, 7, 260, 90, 20, "CepCli", "Cep", ListaCampos.DB_SI, false );
		 adic( btBuscaEnd, 100, 260, 20, 20 );
		 adicCampo( txtEndCli, 125, 260, 315, 20, "EndCli", "Endereço", ListaCampos.DB_SI, false );
		 adicCampo( txtNumCli, 443, 260, 73, 20, "NumCli", "Num.", ListaCampos.DB_SI, false );
		 adicCampo( txtComplCli, 7, 300, 166, 20, "ComplCli", "Compl.", ListaCampos.DB_SI, false );
		 adicCampo( txtBairCli, 176, 300, 200, 20, "BairCli", "Bairro", ListaCampos.DB_SI, false );
		 adicCampo( txtSuframaCli, 379, 300, 137, 20, "SuframaCli", "SUFRAMA", ListaCampos.DB_SI, false );

		 adicCampo( txtDDDCli, 7, 340, 40, 20, "DDDCli", "DDD", ListaCampos.DB_SI, false );
		 adicCampo( txtFoneCli, 50, 340, 97, 20, "FoneCli", "Telefone", ListaCampos.DB_SI, false );
		 adicCampo( txtRamalCli, 150, 340, 72, 20, "RamalCli", "Ramal", ListaCampos.DB_SI, false );

		 adicCampo( txtDDDFaxCli, 225, 340, 40, 20, "DDDFaxCli", "DDD", ListaCampos.DB_SI, false );
		 adicCampo( txtFaxCli, 268, 340, 107, 20, "FaxCli", "Fax", ListaCampos.DB_SI, false );

		 adicCampo( txtDDDCelCli, 378, 340, 40, 20, "DDDCelCli", "DDD", ListaCampos.DB_SI, false );
		 adicCampo( txtCelCli, 421, 340, 95, 20, "CelCli", "Celular", ListaCampos.DB_SI, false );

		 adicCampo( txtEdificioCli, 7, 380, 120, 20, "EdificioCli", "Edifício", ListaCampos.DB_SI, false );
		 adicCampo( txtSiteCli, 130, 380, 210, 20, "SiteCli", "Site", ListaCampos.DB_SI, false );
		 adicCampo( txtIncraCli, 363, 380, 153, 20, "IncraCli", "Incra", ListaCampos.DB_SI, false );
		 adic( btFirefox, 340, 380, 20, 20 );
		 adicCampo( txtEmailCli, 7, 420, 245, 20, "EmailCli", "E-Mail", ListaCampos.DB_SI, false );

		 adicCampo( txtContCli, 256, 420, 260, 20, "ContCli", "Contato", ListaCampos.DB_SI, false );
		 // Bairro, Telefone fixo, Telefone celular, contato e e-mail.
		 if ( (Boolean) bPref.get( "ENDERECOOBRIGCLI" ) ) {
			 txtDDDCli.setRequerido( true );
			 txtFoneCli.setRequerido( true );
			 txtDDDCelCli.setRequerido( true );
			 txtCelCli.setRequerido( true );
			 txtContCli.setRequerido( true );
			 txtEmailCli.setRequerido( true );

		 }


		 if ( (Boolean) bPref.get( "USAIBGECLI" ) ) {

			 adicCampo( txtEmailNfeCli, 7, 460, 245, 20, "EmailNfeCli", "E-Mail para envio de Nfe", ListaCampos.DB_SI, false );
			 adicCampo( txtInscMunCli, 256, 460, 260, 20, "InscMunCli", "Inscrição Municipal", ListaCampos.DB_SI, false );

			 adicCampo( txtCodPais, 7, 500, 70, 20, "CodPais", "Cod.país", ListaCampos.DB_FK, txtDescPais, true );
			 adicDescFK( txtDescPais, 80, 500, 217, 20, "NomePais", "Nome do país" );

			 adicCampo( txtSiglaUF, 300, 500, 50, 20, "SiglaUf", "Sigla UF", ListaCampos.DB_FK, txtNomeUF, true );
			 adicDescFK( txtNomeUF, 353, 500, 162, 20, "NomeUF", "Nome UF" );

			 adicCampo( txtCodMunic, 7, 540, 70, 20, "CodMunic", "Cod.munic.", ListaCampos.DB_FK, txtDescMun, false );
			 adicDescFK( txtDescMun, 80, 540, 217, 20, "NomeMunic", "Nome do municipio" );

		 }
		 else {

			 adicCampo( txtCodPais, 7, 460, 70, 20, "CodPais", "Cod.país", ListaCampos.DB_FK, txtDescPais, true );
			 adicDescFK( txtDescPais, 80, 460, 217, 20, "NomePais", "Nome do país" );
			 adicCampo( txtCidCli, 300, 460, 162, 20, "CidCli", "Cidade", ListaCampos.DB_SI, false );
			 adicCampo( txtUFCli, 465, 460, 50, 20, "UFCli", "UF", ListaCampos.DB_SI, true );
		 }

		 adicCampo( txtDtNascCli, 7, 580, 100, 20, "DtNascCli", "Dt.Nascimento", ListaCampos.DB_SI, false);

		 if ( (Boolean) bPref.get( "BUSCACEP" ) ) {
			 btBuscaEnd.setEnabled( true );
		 }
		 else {
			 btBuscaEnd.setEnabled( false );
		 }

		 txtCpfCli.setMascara( JTextFieldPad.MC_CPF );
		 txtCnpjCli.setMascara( JTextFieldPad.MC_CNPJ );
		 txtCepCli.setMascara( JTextFieldPad.MC_CEP );
		 txtFoneCli.setMascara( JTextFieldPad.MC_FONE );
		 txtCelCli.setMascara( JTextFieldPad.MC_CELULAR);
		 txtFaxCli.setMascara( JTextFieldPad.MC_FONE );

		 pinEnt = new JPanelPad( 500, 290 );
		 setPainel( pinEnt );

		 adicTab( "Entrega", pinEnt );

		 btAtEntrega.setPreferredSize( new Dimension( 30, 30 ) );
		 btAtEntrega.setToolTipText( "Atualiza endereço de entrega." );
		 btAtEntrega.addActionListener( this );
		 btFirefox.addActionListener( this );
		 btFirefox.setToolTipText( "Acessar Site" );

		 btBuscaEnd.addActionListener( this );
		 btBuscaEnd.setToolTipText( "Busca Endereço a partir do CEP" );

		 adicCampo( txtEndEnt, 7, 20, 260, 20, "EndEnt", "Endereço", ListaCampos.DB_SI, false );
		 adicCampo( txtNumEnt, 270, 20, 50, 20, "NumEnt", "Num.", ListaCampos.DB_SI, false );
		 adicCampo( txtComplEnt, 323, 20, 150, 20, "ComplEnt", "Compl.", ListaCampos.DB_SI, false );
		 adicCampo( txtBairEnt, 7, 60, 260, 20, "BairEnt", "Bairro", ListaCampos.DB_SI, false );
		 adicCampo( txtCepEnt, 270, 60, 100, 20, "CepEnt", "Cep", ListaCampos.DB_SI, false );
		 txtCepEnt.setMascara( JTextFieldPad.MC_CEP );

		 adicCampo( txtDDDFoneEnt, 7, 100, 30, 20, "DDDFoneEnt", "DDD", ListaCampos.DB_SI, false );
		 adicCampo( txtFoneEnt, 40, 100, 80, 20, "FoneEnt", "Telefone", ListaCampos.DB_SI, false );
		 txtFoneEnt.setMascara( JTextFieldPad.MC_FONE );
		 adicCampo( txtDDDFaxEnt, 123, 100, 30, 20, "DDDFaxEnt", "DDD", ListaCampos.DB_SI, false );
		 adicCampo( txtFaxEnt, 156, 100, 80, 20, "FaxEnt", "Fax", ListaCampos.DB_SI, false );
		 adicCampo( txtDDDCelEnt, 239, 100, 30, 20, "DDDCelEnt", "DDD", ListaCampos.DB_SI, false );
		 adicCampo( txtCelEnt, 272, 100, 80, 20, "CelEnt", "Cel", ListaCampos.DB_SI, false );
		 txtCelEnt.setMascara( JTextFieldPlan.MC_CELULAR );
		 adicCampo( txtEmailEnt, 7, 140, 350, 20, "EmailEnt", "Email", ListaCampos.DB_SI, false );

		 Vector<String> vIdentCliLab = new Vector<String>();
		 Vector<String> vIdentCliVal = new Vector<String>();

		 vIdentCliLab.addElement( "Doc - CPF/CNPJ" );
		 vIdentCliLab.addElement( "Código" );
		 vIdentCliVal.addElement( "D" );
		 vIdentCliVal.addElement( "C" );
		 rgIdentCliBco = new JRadioGroup<String, String>( 1, 2, vIdentCliLab, vIdentCliVal );
		 rgIdentCliBco.addRadioGroupListener( this );


		 txtFaxEnt.setMascara( JTextFieldPad.MC_FONE );

		 adic( btAtEntrega, 476	, 15, 30, 30 );

		 if ( (Boolean) bPref.get( "USAIBGECLI" ) ) {

			 adicCampo( txtCodPaisEnt, 7, 180, 70, 20, "CodPaisEnt", "Cod.país.Ent", ListaCampos.DB_FK, txtDescPaisEnt, false );
			 adicDescFK( txtDescPaisEnt, 80, 180, 350, 20, "NomePais", "Nome do país" );
			 adicCampo( txtSiglaUFEnt, 7, 220, 70, 20, "SiglaUfEnt", "Sigla UF", ListaCampos.DB_FK, txtNomeUFEnt, false );
			 adicDescFK( txtNomeUFEnt, 80, 220, 350, 20, "NomeUFEnt", "Nome UF" );
			 adicCampo( txtCodMunicEnt, 7, 260, 70, 20, "CodMunicEnt", "Cod.munic.", ListaCampos.DB_FK, txtDescMunEnt, false );
			 adicDescFK( txtDescMunEnt, 80, 260, 350, 20, "NomeMunicEnt", "Nome do municipio" );
			 adicCampo( txtContCliEnt, 7, 300, 300, 20, "ContCliEnt", "Contato para entrega", ListaCampos.DB_SI, false );

		 }
		 else {
			 adicCampo( txtCodPaisEnt, 7, 180, 70, 20, "CodPaisEnt", "Cod.país", ListaCampos.DB_FK, txtDescPaisEnt, false );
			 adicDescFK( txtDescPaisEnt, 80, 180, 350, 20, "NomePais", "Nome do país" );
			 adicCampo( txtCidEnt, 7, 220, 120, 20, "CidEnt", "Cidade", ListaCampos.DB_SI, false );
			 adicCampo( txtUFEnt, 130, 220, 36, 20, "UFEnt", "UF", ListaCampos.DB_SI, false );

			 adicCampo( txtContCliEnt, 7, 260, 260, 20, "ContCliEnt", "Contato para entrega", ListaCampos.DB_SI, false );
		 }

		 if ( (Boolean) bPref.get( "ENTREGAOBRIGCLI" ) ) {
			 txtEndEnt.setRequerido( true );
			 txtNumEnt.setRequerido( true );
			 txtComplEnt.setRequerido( true );
			 txtBairEnt.setRequerido( true );
			 txtCepEnt.setRequerido( true );
			 txtDDDFoneEnt.setRequerido( true );
			 txtFoneEnt.setRequerido( true );
			 txtDDDFaxEnt.setRequerido( true );
			 txtFaxEnt.setRequerido( true );
			 txtDDDCelEnt.setRequerido( true );
			 txtCelEnt.setRequerido( true );
			 txtEmailEnt.setRequerido( true );
			 txtCodPaisEnt.setRequerido( true );
			 txtSiglaUFEnt.setRequerido( true );
			 txtCodMunicEnt.setRequerido( true );
			 txtContCliEnt.setRequerido( true );
		 }

		 pinCob = new JPanelPad( 500, 290 );
		 setPainel( pinCob );
		 adicTab( "Cobrança", pinCob );

		 btAtCobranca.setPreferredSize( new Dimension( 30, 30 ) );
		 btAtCobranca.setToolTipText( "Atualiza endereço de cobrança." );
		 btAtCobranca.addActionListener( this );

		 adicCampo( txtEndCob, 7, 20, 260, 20, "EndCob", "Endereço", ListaCampos.DB_SI, false );
		 adicCampo( txtNumCob, 270, 20, 50, 20, "NumCob", "Num.", ListaCampos.DB_SI, false );
		 adicCampo( txtComplCob, 323, 20, 150, 20, "ComplCob", "Compl.", ListaCampos.DB_SI, false );
		 adicCampo( txtBairCob, 7, 60, 260, 20, "BairCob", "Bairro", ListaCampos.DB_SI, false );
		 adicCampo( txtCepCob, 270, 60, 100, 20, "CepCob", "Cep", ListaCampos.DB_SI, false );
		 txtCepCob.setMascara( JTextFieldPad.MC_CEP );
		 adicCampo( txtDDDFoneCob, 7, 100, 30, 20, "DDDFoneCob", "DDD", ListaCampos.DB_SI, false );
		 adicCampo( txtFoneCob, 40, 100, 70, 20, "FoneCob", "Telefone", ListaCampos.DB_SI, false );
		 txtFoneCob.setMascara( JTextFieldPad.MC_FONE );
		 adicCampo( txtDDDFaxCob, 113, 100, 30, 20, "DDDFaxCob", "DDD", ListaCampos.DB_SI, false );
		 adicCampo( txtFaxCob, 146, 100, 70, 20, "FaxCob", "Fax", ListaCampos.DB_SI, false );
		 txtFaxCob.setMascara( JTextFieldPad.MC_FONE );
		 adicCampo( txtEmailCob, 219, 100, 150, 20, "EmailCob", "Email", ListaCampos.DB_SI, false );

		 adicDB( rgIdentCliBco, 7, 140, 367, 30, "IdentCliBco", "Identificação do Cliente Banco- (SIACC)", false );
		 adicCampo( txtCodTipoCob, 7, 190, 80, 20, "CodTipoCob", "Cód.t.cob.", ListaCampos.DB_FK, txtDescTipoCob, false );
		 adicDescFK( txtDescTipoCob, 90, 190, 280, 20, "DescTipoCob", "Descrição do tipo de cobrança" );
		 adicCampo( txtCodBanco, 7, 230, 80, 20, "CodBanco", "Cód.banco", ListaCampos.DB_FK, txtNomeBanco, false );
		 adicDescFK( txtNomeBanco, 90, 230, 280, 20, "NomeBanco", "Nome do banco" );

		 adicCampo( txtAgenciaCli, 7, 270, 100,20, "AgenciaCli", "Agencia Cli.", ListaCampos.DB_SI, false );
		 adicCampo( txtNContaBcoCli, 110, 270, 150,20, "NContaBcoCli", "Num. Conta Cobrança Cli.", ListaCampos.DB_SI, false );

		 adicCampo( txtCodCartCob, 7, 310, 80, 20, "CodCartCob", "Cód.cart.cob.", ListaCampos.DB_FK, txtDescCartCob, false );
		 adicDescFK( txtDescCartCob, 90, 310, 280, 20, "DescCartCob", "Descrição da carteira de cobrança" );

		 adic( btAtCobranca, 476, 15, 30, 30 );

		 if ( (Boolean) bPref.get( "USAIBGECLI" ) ) {

			 adicCampo( txtCodPaisCob, 7, 350, 70, 20, "CodPaisCob", "Cod.país", ListaCampos.DB_FK, txtDescPaisCob, false );
			 adicDescFK( txtDescPaisCob, 80, 350, 290, 20, "NomePais", "Nome do país" );
			 adicCampo( txtSiglaUFCob, 7, 390, 70, 20, "SiglaUfCob", "Sigla UF", ListaCampos.DB_FK, txtNomeUFCob, false );
			 adicDescFK( txtNomeUFCob, 80, 390, 290, 20, "NomeUFCob", "Nome UF" );
			 adicCampo( txtCodMunicCob, 7, 430, 70, 20, "CodMunicCob", "Cod.munic.", ListaCampos.DB_FK, txtDescMunCob, false );
			 adicDescFK( txtDescMunCob, 80, 430, 290, 20, "NomeMunicCob", "Nome do municipio" );
			 adicCampo( txtContCliCob, 7, 470, 260, 20, "ContCliCob", "Contato para cobrança", ListaCampos.DB_SI, false );

		 }
		 else {
			 adicCampo( txtCodPaisCob, 7, 350, 70, 20, "CodPaisCob", "Cod.país", ListaCampos.DB_FK, txtDescPaisCob, false );
			 adicDescFK( txtDescPaisCob, 80, 350, 290, 20, "NomePais", "Nome do país" );
			 adicCampo( txtCidCob, 7, 390, 120, 20, "CidCob", "Cidade", ListaCampos.DB_SI, false );
			 adicCampo( txtUFCob, 130, 390, 36, 20, "UFCob", "UF", ListaCampos.DB_SI, false );
		 }

		 // Venda:

		 pinVend = new JPanelPad( 500, 290 );
		 setPainel( pinVend );
		 adicTab( "Venda", pinVend );
		 adicCampo( txtCodVend, 7, 20, 80, 20, "CodVend", "Cód.comiss.", ListaCampos.DB_FK, txtDescVend, false );
		 adicDescFK( txtDescVend, 90, 20, 240, 20, "NomeVend", "Nome do comissionado" );
		 adicCampo( txtCodPlanoPag, 7, 60, 80, 20, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_FK, txtDescPlanoPag, false );
		 adicDescFK( txtDescPlanoPag, 90, 60, 240, 20, "DescPlanoPag", "Descrição do plano de pagamento" );
		 adicCampo( txtCodTran, 7, 100, 80, 20, "CodTran", "Cód.tran.", ListaCampos.DB_FK, txtDescTran, false );
		 adicDescFK( txtDescTran, 90, 100, 240, 20, "NomeTran", "Nome ou razão social do transportador" );


		 adicCampo( txtCodPesq, 7, 140, 80, 20, "CodPesq", "Cód.cli.p.", ListaCampos.DB_FK, txtDescPesq, false );
		 adicDescFK( txtDescPesq, 90, 140, 240, 20, "RazCli", "Razão social do cliente principal" );
		 adicCampo( txtCodFiscCli, 7, 180, 80, 20, "CodFiscCli", "Cód.tp.fisc.", ListaCampos.DB_FK, txtDescFiscCli, false );
		 adicDescFK( txtDescFiscCli, 90, 180, 240, 20, "DescFiscCli", "Descrição do tipo fiscal" );
		 adicCampo( txtCodCliContab, 7, 220, 160, 20, "CodCliContab", "Cód.cli.contábil", ListaCampos.DB_SI, false );
		 adicCampo( txtCodContDeb, 7, 260, 160, 20, "CodContDeb", "Cód.cont.débito", ListaCampos.DB_SI, false );
		 adicCampo( txtCodContCred, 170, 260, 160, 20, "CodContCred", "Cód.cont.crédito", ListaCampos.DB_SI, false );
		 adicCampo( txtCodHistPad, 7, 300, 80, 20, "CodHist", "Cód.hist.", ListaCampos.DB_FK, txtDescHistPad, false );
		 adicDescFK( txtDescHistPad, 90, 300, 240, 20, "DescHist", "Descrição do historico padrão" );

		 adicCampo( txtPercDescCli, 7, 340, 80, 20, "PercDescCli", "% Desconto", ListaCampos.DB_SI, false );

		 adicCampo( txtCodSetor, 7, 380, 80, 20, "CodSetor", "Cód.setor", ListaCampos.DB_FK, txtDescSetor, false );
		 adicDescFK( txtDescSetor, 90, 380, 237, 20, "DescSetor", "Descrição do setor" );

		 adicDB( cbDescIpi, 7, 420, 200, 20, "DescIpi", "", false );

		 // Adicionar botão para agrupamento de clientes

		 btGrpCli.setToolTipText( "Clientes agrupados" );
		 btGrpCli.setPreferredSize( new Dimension( 38, 26 ) );

		 btMapa.setToolTipText( "Exibição de mapa" );
		 btMapa.setPreferredSize( new Dimension( 38, 26 ) );

		 btCopiar.setToolTipText( "Copiar Cliente" );
		 btCopiar.setPreferredSize( new Dimension( 38, 26 ) );

		 pnGImp.add( btMapa );
		 pnGImp.add( btGrpCli );
		 pnGImp.add( btCopiar );

		 pnGImp.setPreferredSize( new Dimension( 150, 26 ) );

		 btGrpCli.addActionListener( this );

		 adicTab( "Observações", pnObs1 );
		 adicDBLiv( txaObs, "ObsCli", "Observações", false );

		 txaTxtObsCli.setEditable( false );
		 tbObsData.adicColuna( "Data" );
		 tbObsData.adicColuna( "Hora" );
		 tbObsData.adicColuna( "Seq." );
		 tbObsData.setTamColuna( 80, 0 );
		 tbObsData.setTamColuna( 80, 0 );
		 tbObsData.setTamColuna( 40, 1 );

		 btNovaObs.setToolTipText( "Nova observação por data." );
		 btExclObs.setToolTipText( "Exclui observação selecionada." );
		 btEditObs.setToolTipText( "Edita observação selecionanda." );

		 btNovaObs.addActionListener( this );
		 btExclObs.addActionListener( this );
		 btEditObs.addActionListener( this );

		 pinObs1_2_2_2_1.adic( btNovaObs, 0, 0, 30, 26 );
		 pinObs1_2_2_2_1.adic( btExclObs, 31, 0, 30, 26 );
		 pinObs1_2_2_2_1.adic( btEditObs, 62, 0, 30, 26 );
		 pnObs1_2_2_1.add( new JScrollPane( txaTxtObsCli ) );
		 pnObs1_2_2_2.add( pinObs1_2_2_2_1 );
		 pinObs1_2_1_1.adic( new JScrollPane( tbObsData ), 0, 0, 200, 200 );
		 pnObs1_2_1.add( pinObs1_2_1_1 ); // adiciona o scrool pane da tabela de datas no painel da esquerda
		 pnObs1_2_2.add( pnObs1_2_2_1, BorderLayout.CENTER ); // adiciona memo de observações no painel da direita
		 pnObs1_2_2.add( pnObs1_2_2_2, BorderLayout.SOUTH );
		 pnObs1_1.add( new JScrollPane( txaObs ), BorderLayout.CENTER ); // adiciona as observações gerais no painel
		 pnObs1_2.add( pnObs1_2_1, BorderLayout.WEST );
		 pnObs1_2.add( pnObs1_2_2, BorderLayout.CENTER );
		 pnObs1.add( pnObs1_1, BorderLayout.CENTER );
		 pnObs1.add( pnObs1_2, BorderLayout.SOUTH );

		 adicTab( "Imagens", pinImages );
		 setPainel( pinImages );
		 adicDB( fotoCli, 10, 30, 200, 270, "FotoCli", "Foto ( 200 pixel X 270 pixel )", false );
		 adicDB( imgAssCli, 10, 330, 340, 85, "ImgAssCli", "Assinatura ( 340 pixel X 85 pixel )", false );

		 setListaCampos( true, "CLIENTE", "VD" );
		 lcCampos.setValidarcpf( (Boolean) bPref.get( "CONSISTCPFCLI" ) );

		 // Fornecedor:

		 setPainel( pinFor, pnFor );
		 adicTab( "Codificação no fornecedor", pnFor );
		 setListaCampos( lcCliFor );

		 navFor.setAtivo( 6, false );

		 setNavegador( navFor );
		 pnFor.add( pinFor, BorderLayout.SOUTH );
		 pnFor.add( new JScrollPane( tabFor ), BorderLayout.CENTER );

		 pinFor.adic( navFor, 0, 50, 270, 25 );

		 lcFor.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.for.", ListaCampos.DB_PK, null, true ) );
		 lcFor.add( new GuardaCampo( txtDescFor, "RazFor", "Razão social do fronecedor", ListaCampos.DB_SI, false ) );
		 lcFor.montaSql( false, "FORNECED", "CP" );
		 lcFor.setReadOnly( true );
		 lcFor.setQueryCommit( false );
		 txtCodFor.setListaCampos( lcFor );
		 txtCodFor.setTabelaExterna( lcFor, FFornecedor.class.getCanonicalName() );

		 adicCampo( txtCodFor, 7, 20, 80, 20, "CodFor", "Cód.forn.", ListaCampos.DB_PF, txtDescFor, true );
		 adicDescFK( txtDescFor, 90, 20, 257, 20, "RazFor", "Razão social do fornecedor" );
		 adicCampo( txtCodCliFor, 350, 20, 80, 20, "CodCliFor", "Cód.cli.for.", ListaCampos.DB_SI, false );
		 adicCampo( txtCodCpCliFor, 433, 20, 77, 20, "CodCpCliFor", "Cód.compl.", ListaCampos.DB_SI, false );
		 setListaCampos( false, "CLIENTEFOR", "VD" );
		 lcCliFor.montaTab();
		 lcCliFor.setQueryInsert( false );
		 lcCliFor.setQueryCommit( false );
		 tabFor.setTamColuna( 250, 1 );

		 txtCodPesq.setNomeCampo( "CodCli" );

		 btImp.addActionListener( this );
		 btPrevimp.addActionListener( this );
		 tpn.addChangeListener( this );
		 lcCampos.setQueryInsert( false );

		 // Contatos
		 /*
		tabHist.adicColuna( "Ind." );
		tabHist.adicColuna( "Sit." );
		tabHist.adicColuna( "tipo" );
		tabHist.adicColuna( "Contato" );
		tabHist.adicColuna( "Atendente" );
		tabHist.adicColuna( "Data" );
		tabHist.adicColuna( "Histórico" );
		tabHist.adicColuna( "Usuário" );
		tabHist.adicColuna( "Hora" );

		tabHist.setTamColuna( 40, 0 );
		tabHist.setTamColuna( 30, 1 );
		tabHist.setTamColuna( 30, 2 );
		tabHist.setTamColuna( 70, 3 );
		tabHist.setTamColuna( 70, 4 );
		tabHist.setTamColuna( 75, 5 );
		tabHist.setTamColuna( 200, 6 );
		tabHist.setTamColuna( 100, 7 );
		tabHist.setTamColuna( 70, 8 );

		if ( mevt.getSource() == tabatd && mevt.getClickCount() == 2 && mevt.getModifiers() == MouseEvent.BUTTON1_MASK ) {
			visualizaAtend();
		}

		tabHist.addMouseListener( new MouseAdapter() {

			public void mouseClicked( MouseEvent mevt ) {

				if ( mevt.getClickCount() == 2 ) {
					editaHist();
				}
			}
		} );
		  */
		 montaGridAtend();
		 tpnCont.setTabPlacement( SwingConstants.BOTTOM );
		 //tpnCont.add( "Historico", pinHistorico );
		 tpnCont.add("Atendimentos", pinAtendimento);
		 //tpnCont.add( "Lançamento de Contatos", pinContatos );
		 tpnCont.addChangeListener( this );

		 setPainel( pinContatos );
		 adicTab( "Contatos", pnCto );
		 pnCto.add( tpnCont );

		 pinAtendimento.add( new JScrollPane( tabatd ), BorderLayout.CENTER );
		 pinAtendimento.add( pinAtdBt, BorderLayout.EAST);
		 pinAtdBt.setPreferredSize( new Dimension( 37, 36 ) );
		 pinAtdBt.adic( btNovoAtd, 1, 1, 30, 30 );
		 pinAtdBt.adic( btExcluirAtd, 1, 32, 30, 30 );
		 btNovoAtd.addActionListener( this );
		 btExcluirAtd.addActionListener( this );

		 //pinHistorico.add( pinHistbt, BorderLayout.EAST );
		 /*pinHistorico.add( new JScrollPane( tabHist ), BorderLayout.CENTER );
		pinHistorico.add( pinHistbt, BorderLayout.EAST );

		pinHistbt.setPreferredSize( new Dimension( 37, 36 ) );
		pinHistbt.adic( btNovoHist, 1, 1, 30, 30 );
		pinHistbt.adic( btExcluiHist, 1, 32, 30, 30 );
		btNovoHist.addActionListener( this );
		btExcluiHist.addActionListener( this );
		  */
		 //pinContatos.add( pnCont, BorderLayout.CENTER );

		 /*pnCont.adic( new JLabelPad( "Ano" ), 7, 0, 80, 20 );
		pnCont.adic( txtAno, 7, 20, 80, 20 );
		pnCont.adic( btMudaTudo, 347, 15, 150, 30 );

		txtAno.addFocusListener( this );
		txtAno.setVlrInteger( new Integer( Calendar.getInstance().get( Calendar.YEAR ) ) );

		JLabelPad lbMes1 = new JLabelPad( "   Janeiro" );
		lbMes1.setOpaque( true );
		pnCont.adic( lbMes1, 17, 55, 80, 15 );
		pnCont.adic( pinMes1, 7, 60, 160, 70 );
		pinMes1.adic( new JLabelPad( "Contatos" ), 7, 10, 60, 20 );
		pinMes1.adic( txtAntQtdContJan, 7, 30, 60, 20 );
		txtAntQtdContJan.setAtivo( false );
		pinMes1.adic( new JLabelPad( "Nova qtd." ), 70, 10, 60, 20 );
		pinMes1.adic( txtNovaQtdContJan, 70, 30, 60, 20 );
		pinMes1.adic( btSetaQtdJan, 133, 30, 20, 20 );
		btSetaQtdJan.setBorder( null );
		btSetaQtdJan.setToolTipText( "Gera contatos" );
		pinMes1.setBorder( BorderFactory.createEtchedBorder() );

		JLabelPad lbMes2 = new JLabelPad( "   Fevereiro" );
		lbMes2.setOpaque( true );
		pnCont.adic( lbMes2, 192, 55, 80, 15 );
		pnCont.adic( pinMes2, 172, 60, 160, 70 );
		pinMes2.adic( new JLabelPad( "Contatos" ), 7, 10, 60, 20 );
		pinMes2.adic( txtAntQtdContFev, 7, 30, 60, 20 );
		txtAntQtdContFev.setAtivo( false );
		pinMes2.adic( new JLabelPad( "Nova qtd." ), 70, 10, 60, 20 );
		pinMes2.adic( txtNovaQtdContFev, 70, 30, 60, 20 );
		pinMes2.adic( btSetaQtdFev, 133, 30, 20, 20 );
		btSetaQtdFev.setBorder( null );
		btSetaQtdFev.setToolTipText( "Gera contatos" );

		pinMes2.setBorder( BorderFactory.createEtchedBorder() );
		JLabelPad lbMes3 = new JLabelPad( "   Março" );
		lbMes3.setOpaque( true );
		pnCont.adic( lbMes3, 367, 55, 80, 15 );
		pnCont.adic( pinMes3, 337, 60, 160, 70 );
		pinMes3.setBorder( BorderFactory.createEtchedBorder() );
		pinMes3.adic( new JLabelPad( "Contatos" ), 7, 10, 60, 20 );
		pinMes3.adic( txtAntQtdContMar, 7, 30, 60, 20 );
		txtAntQtdContMar.setAtivo( false );
		pinMes3.adic( new JLabelPad( "Nova qtd." ), 70, 10, 60, 20 );
		pinMes3.adic( txtNovaQtdContMar, 70, 30, 60, 20 );
		pinMes3.adic( btSetaQtdMar, 133, 30, 20, 20 );
		btSetaQtdMar.setBorder( null );
		btSetaQtdMar.setToolTipText( "Gera contatos" );

		JLabelPad lbMes4 = new JLabelPad( "   Abril" );
		lbMes4.setOpaque( true );
		pnCont.adic( lbMes4, 17, 135, 80, 15 );
		pnCont.adic( pinMes4, 7, 140, 160, 70 );
		pinMes4.setBorder( BorderFactory.createEtchedBorder() );
		pinMes4.adic( new JLabelPad( "Contatos" ), 7, 10, 60, 20 );
		pinMes4.adic( txtAntQtdContAbr, 7, 30, 60, 20 );
		txtAntQtdContAbr.setAtivo( false );
		pinMes4.adic( new JLabelPad( "Nova qtd." ), 70, 10, 60, 20 );
		pinMes4.adic( txtNovaQtdContAbr, 70, 30, 60, 20 );
		pinMes4.adic( btSetaQtdAbr, 133, 30, 20, 20 );
		btSetaQtdAbr.setBorder( null );
		btSetaQtdAbr.setToolTipText( "Gera contatos" );

		JLabelPad lbMes5 = new JLabelPad( "   Maio" );
		lbMes5.setOpaque( true );
		pnCont.adic( lbMes5, 192, 135, 80, 15 );
		pnCont.adic( pinMes5, 172, 140, 160, 70 );
		pinMes5.setBorder( BorderFactory.createEtchedBorder() );
		pinMes5.adic( new JLabelPad( "Contatos" ), 7, 10, 60, 20 );
		pinMes5.adic( txtAntQtdContMai, 7, 30, 60, 20 );
		txtAntQtdContMai.setAtivo( false );
		pinMes5.adic( new JLabelPad( "Nova qtd." ), 70, 10, 60, 20 );
		pinMes5.adic( txtNovaQtdContMai, 70, 30, 60, 20 );
		pinMes5.adic( btSetaQtdMai, 133, 30, 20, 20 );
		btSetaQtdMai.setBorder( null );
		btSetaQtdMai.setToolTipText( "Gera contatos" );

		JLabelPad lbMes6 = new JLabelPad( "   Junho" );
		lbMes6.setOpaque( true );
		pnCont.adic( lbMes6, 367, 135, 80, 15 );
		pnCont.adic( pinMes6, 337, 140, 160, 70 );
		pinMes6.setBorder( BorderFactory.createEtchedBorder() );
		pinMes6.adic( new JLabelPad( "Contatos" ), 7, 10, 60, 20 );
		pinMes6.adic( txtAntQtdContJun, 7, 30, 60, 20 );
		txtAntQtdContJun.setAtivo( false );
		pinMes6.adic( new JLabelPad( "Nova qtd." ), 70, 10, 60, 20 );
		pinMes6.adic( txtNovaQtdContJun, 70, 30, 60, 20 );
		pinMes6.adic( btSetaQtdJun, 133, 30, 20, 20 );
		btSetaQtdJun.setBorder( null );
		btSetaQtdJun.setToolTipText( "Gera contatos" );

		JLabelPad lbMes7 = new JLabelPad( "   Julho" );
		lbMes7.setOpaque( true );
		pnCont.adic( lbMes7, 17, 215, 80, 15 );
		pnCont.adic( pinMes7, 7, 220, 160, 70 );
		pinMes7.setBorder( BorderFactory.createEtchedBorder() );
		pinMes7.adic( new JLabelPad( "Contatos" ), 7, 10, 60, 20 );
		pinMes7.adic( txtAntQtdContJul, 7, 30, 60, 20 );
		txtAntQtdContJul.setAtivo( false );
		pinMes7.adic( new JLabelPad( "Nova qtd." ), 70, 10, 60, 20 );
		pinMes7.adic( txtNovaQtdContJul, 70, 30, 60, 20 );
		pinMes7.adic( btSetaQtdJul, 133, 30, 20, 20 );
		btSetaQtdJul.setBorder( null );
		btSetaQtdJul.setToolTipText( "Gera contatos" );

		JLabelPad lbMes8 = new JLabelPad( "   Agosto" );
		lbMes8.setOpaque( true );
		pnCont.adic( lbMes8, 192, 215, 80, 15 );
		pnCont.adic( pinMes8, 172, 220, 160, 70 );
		pinMes8.setBorder( BorderFactory.createEtchedBorder() );
		pinMes8.adic( new JLabelPad( "Contatos" ), 7, 10, 60, 20 );
		pinMes8.adic( txtAntQtdContAgo, 7, 30, 60, 20 );
		txtAntQtdContAgo.setAtivo( false );
		pinMes8.adic( new JLabelPad( "Nova qtd." ), 70, 10, 60, 20 );
		pinMes8.adic( txtNovaQtdContAgo, 70, 30, 60, 20 );
		pinMes8.adic( btSetaQtdAgo, 133, 30, 20, 20 );
		btSetaQtdAgo.setBorder( null );
		btSetaQtdAgo.setToolTipText( "Gera contatos" );

		JLabelPad lbMes9 = new JLabelPad( "   Setembro" );
		lbMes9.setOpaque( true );
		pnCont.adic( lbMes9, 367, 215, 80, 15 );
		pnCont.adic( pinMes9, 337, 220, 160, 70 );
		pinMes9.setBorder( BorderFactory.createEtchedBorder() );
		pinMes9.adic( new JLabelPad( "Contatos" ), 7, 10, 60, 20 );
		pinMes9.adic( txtAntQtdContSet, 7, 30, 60, 20 );
		txtAntQtdContSet.setAtivo( false );
		pinMes9.adic( new JLabelPad( "Nova qtd." ), 70, 10, 60, 20 );
		pinMes9.adic( txtNovaQtdContSet, 70, 30, 60, 20 );
		pinMes9.adic( btSetaQtdSet, 133, 30, 20, 20 );
		btSetaQtdSet.setBorder( null );
		btSetaQtdSet.setToolTipText( "Gera contatos" );

		JLabelPad lbMes10 = new JLabelPad( "   Outubro" );
		lbMes10.setOpaque( true );
		pnCont.adic( lbMes10, 17, 295, 80, 15 );
		pnCont.adic( pinMes10, 7, 300, 160, 70 );
		pinMes10.setBorder( BorderFactory.createEtchedBorder() );
		pinMes10.adic( new JLabelPad( "Cntatos" ), 7, 10, 60, 20 );
		pinMes10.adic( txtAntQtdContOut, 7, 30, 60, 20 );
		txtAntQtdContOut.setAtivo( false );
		pinMes10.adic( new JLabelPad( "Nova qtd." ), 70, 10, 60, 20 );
		pinMes10.adic( txtNovaQtdContOut, 70, 30, 60, 20 );
		pinMes10.adic( btSetaQtdOut, 133, 30, 20, 20 );
		btSetaQtdOut.setBorder( null );
		btSetaQtdOut.setToolTipText( "Gera contatos" );

		JLabelPad lbMes11 = new JLabelPad( "   Novembro" );
		lbMes11.setOpaque( true );
		pnCont.adic( lbMes11, 192, 295, 80, 15 );
		pnCont.adic( pinMes11, 172, 300, 160, 70 );
		pinMes11.setBorder( BorderFactory.createEtchedBorder() );
		pinMes11.adic( new JLabelPad( "Contatos" ), 7, 10, 60, 20 );
		pinMes11.adic( txtAntQtdContNov, 7, 30, 60, 20 );
		txtAntQtdContNov.setAtivo( false );
		pinMes11.adic( new JLabelPad( "Nova qtd." ), 70, 10, 60, 20 );
		pinMes11.adic( txtNovaQtdContNov, 70, 30, 60, 20 );
		pinMes11.adic( btSetaQtdNov, 133, 30, 20, 20 );
		btSetaQtdNov.setBorder( null );
		btSetaQtdNov.setToolTipText( "Gera contatos" );

		JLabelPad lbMes12 = new JLabelPad( "   Dezembro" );
		lbMes12.setOpaque( true );
		pnCont.adic( lbMes12, 367, 295, 80, 15 );
		pnCont.adic( pinMes12, 337, 300, 160, 70 );
		pinMes12.setBorder( BorderFactory.createEtchedBorder() );
		pinMes12.adic( new JLabelPad( "Contatos" ), 7, 10, 60, 20 );
		pinMes12.adic( txtAntQtdContDez, 7, 30, 60, 20 );
		txtAntQtdContDez.setAtivo( false );
		pinMes12.adic( new JLabelPad( "Nova qtd." ), 70, 10, 60, 20 );
		pinMes12.adic( txtNovaQtdContDez, 70, 30, 60, 20 );
		pinMes12.adic( btSetaQtdDez, 133, 30, 20, 20 );
		btSetaQtdDez.setBorder( null );
		btSetaQtdDez.setToolTipText( "Gera contatos" );
		  */
		 // AnotaMetaVend
		 setPainel( pinMetaVend, pnMetaVend );
		 adicTab( "Meta de Vendas", pnMetaVend );
		 setListaCampos( lcMetaVend );
		 setNavegador( navMetaVend );

		 pnMetaVend.add( pinMetaVend, BorderLayout.SOUTH );
		 pnMetaVend.add( new JScrollPane( tabMetaVend ), BorderLayout.CENTER );

		 pinMetaVend.adic( navMetaVend, 0, 130, 150, 25 );

		 adicCampo( txtAnoMetaVend, 7, 20, 100, 20, "AnoMetaVend", "Ano", ListaCampos.DB_PK, null, true );
		 adicCampo( txtVlrMetaVend, 110, 20, 120, 20, "VlrMetaVend", "Valor da meta", ListaCampos.DB_SI, true );
		 adicDBLiv( txaObsMetaVend, 7, 60, 500, 60, "ObsMetaVend", "Observações", false );
		 setListaCampos( false, "CLIMETAVEND", "VD" );
		 lcMetaVend.montaTab();
		 lcMetaVend.setQueryInsert( false );
		 lcMetaVend.setQueryCommit( false );
		 tabMetaVend.setTamColuna( 150, 1 );

		 btSetaQtdJan.addActionListener( this );
		 btSetaQtdFev.addActionListener( this );
		 btSetaQtdMar.addActionListener( this );
		 btSetaQtdAbr.addActionListener( this );
		 btSetaQtdMai.addActionListener( this );
		 btSetaQtdJun.addActionListener( this );
		 btSetaQtdJul.addActionListener( this );
		 btSetaQtdAgo.addActionListener( this );
		 btSetaQtdSet.addActionListener( this );
		 btSetaQtdOut.addActionListener( this );
		 btSetaQtdNov.addActionListener( this );
		 btSetaQtdDez.addActionListener( this );
		 btMudaTudo.addActionListener( this );

		 setPainel( pinCliFor, pnCliFor );

		 adicTab( "Cliente x Fornecedor", pnCliFor );
		 setListaCampos( lcClixFor );

		 navClixFor.setAtivo( 6, false );

		 setNavegador( navClixFor );
		 pnCliFor.add( pinCliFor, BorderLayout.CENTER );
		 pnCliFor.add( pinTesteFor, BorderLayout.SOUTH );
		 pinTesteFor.adic( navClixFor, 0, 0, 270, 25 );

		 navClixFor.setAtivo( 0, false );
		 navClixFor.setAtivo( 1, false );
		 navClixFor.setAtivo( 2, false );
		 navClixFor.setAtivo( 3, false );
		 navClixFor.setAtivo( 4, false );
		 navClixFor.setAtivo( 8, true );

		 lcForCli.add( new GuardaCampo( txtCodForCli, "CodFor", "Cód.for.", ListaCampos.DB_PK, null, true ) );
		 lcForCli.add( new GuardaCampo( txtDescForCli, "RazFor", "Razão social do fronecedor", ListaCampos.DB_SI, false ) );
		 lcForCli.add( new GuardaCampo( txtNomeForCli, "NomeFor", "Nome do Fornecedor", ListaCampos.DB_SI, false ) );
		 lcForCli.add( new GuardaCampo( txtcpfForCli, "CpfFor", "CPF", ListaCampos.DB_SI, false ) );
		 lcForCli.add( new GuardaCampo( txtinscForCli, "InscFor", "Inscrição Estadual", ListaCampos.DB_SI, false ) );
		 lcForCli.add( new GuardaCampo( txtrgForCli, "RgFor", "RG", ListaCampos.DB_SI, false ) );
		 lcForCli.add( new GuardaCampo( txtCnpjForCli, "CnpjFor", "CNPJ", ListaCampos.DB_SI, false ) );
		 lcForCli.add( new GuardaCampo( txtEndForCli, "EndFor", "Endereço do fornecedor", ListaCampos.DB_SI, false ) );
		 lcForCli.add( new GuardaCampo( txtNumForCli, "NumFor", "Nº Fornecedor", ListaCampos.DB_SI, false ) );
		 lcForCli.add( new GuardaCampo( txtBairForCli, "BairFor", "Bairro", ListaCampos.DB_SI, false ) );
		 lcForCli.montaSql( false, "FORNECED", "CP" );
		 lcForCli.setReadOnly( true );
		 lcForCli.setQueryCommit( false );
		 txtCodForCli.setListaCampos( lcForCli );
		 txtCodForCli.setTabelaExterna( lcForCli, FFornecedor.class.getCanonicalName() );

		 adic( btBuscaFor, 7, 7, 30, 30 );
		 btBuscaFor.setToolTipText( "Buscar fornecedor" );
		 adicCampo( txtCodForCli, 50, 20, 55, 20, "CodFor", "Cód.For", ListaCampos.DB_PF, txtDescForCli, true );
		 adicDescFK( txtDescForCli, 110, 20, 200, 20, "RazFor", "Razão social do Fornecedor" );
		 setListaCampos( false, "CLIFOR", "EQ" );
		 lcClixFor.montaTab();
		 lcClixFor.setQueryInsert( false );
		 lcClixFor.setQueryCommit( false );

		 adic( new JLabelPad( "Nome" ), 313, 0, 200, 20 );
		 adic( txtNomeForCli, 313, 20, 180, 20 );
		 adic( new JLabelPad( "Endereço" ), 7, 40, 200, 20 );
		 adic( txtEndForCli, 7, 60, 305, 20 );
		 adic( new JLabelPad( "Bairro" ), 315, 40, 120, 20 );
		 adic( txtBairForCli, 315, 60, 131, 20 );
		 adic( new JLabelPad( "Nº" ), 450, 40, 200, 20 );
		 adic( txtNumForCli, 450, 60, 45, 20 );
		 adic( new JLabelPad( "CNPJ" ), 7, 80, 160, 20 );
		 adic( txtCnpjForCli, 7, 100, 160, 20 );
		 adic( new JLabelPad( "Inscrição Estadual" ), 170, 80, 200, 20 );
		 adic( txtinscForCli, 170, 100, 142, 20 );
		 adic( new JLabelPad( "CPF" ), 315, 80, 200, 20 );
		 adic( txtcpfForCli, 315, 100, 180, 20 );
		 txtcpfForCli.setMascara( JTextFieldPad.MC_CPF );
		 txtCnpjForCli.setMascara( JTextFieldPad.MC_CNPJ );

		 btImp.addActionListener( this );
		 btPrevimp.addActionListener( this );
		 btBuscaFor.addActionListener( this );
		 btMapa.addActionListener( this );
		 btCopiar.addActionListener( this );
		 tpn.addChangeListener( this );
		 lcCampos.setQueryInsert( false );

		 lcCampos.addCarregaListener( this );
		 tbObsData.addTabelaSelListener( this );
		 lcMunic.addCarregaListener( this );

	}

	private void montaGridAtend() {

		tabatd.adicColuna( "Doc." ); // Documento do atendimento
		tabatd.adicColuna( "Status" ); // Status do atendimento
		tabatd.adicColuna( "Data" ); // Data inicio atendimento
		tabatd.adicColuna( "Cód.atd." ); // Código do atendimento
		tabatd.adicColuna( "Cód.orc." ); // Código do orçamento
		tabatd.adicColuna( "Data fim" ); // Data final atendimento
		tabatd.adicColuna( "Cliente" ); // Nome do cliente
		tabatd.adicColuna( "Atendimento" ); // Observações do atendimento
		tabatd.adicColuna( " Cód. Atend." );// Código do atendente
		tabatd.adicColuna( "Atendente" ); // Código do atendente
		tabatd.adicColuna( "Inicio" ); // Hora inicial
		tabatd.adicColuna( "Fim" ); // Hora final
		tabatd.adicColuna( "Tempo" ); // Tempo de atendimento
		tabatd.adicColuna( "Cobrança" ); // Tempo de atendimento
		tabatd.adicColuna( "Cham." ); // Código do chamado
		tabatd.adicColuna( "Cod.Cli." ); // Código do cliente
		tabatd.adicColuna( "Cód.Esp." ); // Código da especificação
		tabatd.adicColuna( "Descrição da especificação" ); // Descrição da especificação

		tabatd.setTamColuna( 45, COL_ATENDIMENTO.CODATENDO.ordinal() );
		tabatd.setTamColuna( 45, COL_ATENDIMENTO.CODORC.ordinal() );
		tabatd.setTamColuna( 150, COL_ATENDIMENTO.NOMECLI.ordinal() );
		tabatd.setTamColuna( 250, COL_ATENDIMENTO.OBSATENDO.ordinal() );
		tabatd.setTamColuna( 45, COL_ATENDIMENTO.HORAATENDO.ordinal() );
		tabatd.setTamColuna( 45, COL_ATENDIMENTO.HORAATENDOFIN.ordinal() );
		tabatd.setTamColuna( 45, COL_ATENDIMENTO.TEMPO.ordinal() );
		tabatd.setTamColuna( 45, COL_ATENDIMENTO.TEMPOCOB.ordinal() );
		tabatd.setTamColuna( 45, COL_ATENDIMENTO.CODCHAMADO.ordinal() );
		tabatd.setTamColuna( 150, COL_ATENDIMENTO.DESCESPEC.ordinal() );
		// tabatd.setColunaInvisivel( COL_ATENDIMENTO.CODATENDO.ordinal() );
		tabatd.setTamColuna(45, COL_ATENDIMENTO.DOCATENDO.ordinal() );
		tabatd.setTamColuna(45, COL_ATENDIMENTO.STATUSATENDO.ordinal() );
		tabatd.setTamColuna(60, COL_ATENDIMENTO.DATAATENDOFIN.ordinal() );
		tabatd.setTamColuna(45, COL_ATENDIMENTO.CODATEND.ordinal() );
		tabatd.setTamColuna(45, COL_ATENDIMENTO.CODCLI.ordinal() );
		tabatd.setTamColuna(45, COL_ATENDIMENTO.CODESPEC.ordinal() );

		tabatd.setRowHeight( 20 );



		tabatd.addMouseListener( new MouseAdapter() {

			public void mouseClicked( MouseEvent mevt ) {

				if ( mevt.getSource() == tabatd && mevt.getClickCount() == 2 && mevt.getModifiers() == MouseEvent.BUTTON1_MASK ) {
					visualizaAtend();
				}
			}
		} );

	}

	private void setValores() {

		txtNomeForCli.setVlrString( txtNomeCli.getVlrString() );
		txtcpfForCli.setVlrInteger( txtCpfCli.getVlrInteger() );
		txtinscForCli.setVlrString( txtInscCli.getVlrString() );
		txtrgForCli.setVlrInteger( txtRgCli.getVlrInteger() );
		txtCnpjForCli.setVlrInteger( txtCnpjCli.getVlrInteger() );
		txtEndForCli.setVlrString( txtEndCli.getVlrString() );
		txtNumForCli.setVlrInteger( txtNumCli.getVlrInteger() );
		txtBairForCli.setVlrString( txtBairCli.getVlrString() );

	}

	private void buscaFornecedor() {

		String sCodCli = txtCodCli.getVlrString();
		int codFor = 0;

		if ( "".equals( sCodCli ) ) {

			Funcoes.mensagemInforma( this, "Selecione um cliente! " );
		}

		codFor = pesqFor();

		if ( codFor != 0 ) {

			txtCodForCli.setVlrInteger( codFor );
			setValores();
			lcForCli.carregaDados();
			lcClixFor.carregaDados();

		}
		else {

			if ( Funcoes.mensagemConfirma( this, "Não foi encontrado nenhum fornecedor equivalente!\n" + "Deseja replicar os dados do cliente automaticamente?" ) == JOptionPane.YES_OPTION ) {
				codFor = inserirFor();

				if ( codFor != 0 ) {

					txtCodForCli.setVlrInteger( codFor );
					lcForCli.carregaDados();
					lcClixFor.carregaDados();
					Funcoes.mensagemInforma( this, "Fornecedor inserido com sucesso!" );
					setValores();

				}
			}
		}
	}

	private int getCodFor() {

		int codigo = 0;
		StringBuilder sSQL = new StringBuilder();
		PreparedStatement ps = null;

		sSQL.append( "SELECT MAX( F.CODFOR ) FROM CPFORNECED F WHERE F.CODEMP=? AND F.CODFILIAL=?" );

		try {

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "CPFORNECED" ) );
			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {

				codigo = rs.getInt( 1 ) + 1;
			}

		} catch ( SQLException err ) {

			err.printStackTrace();
			Funcoes.mensagemInforma( this, "Erro ao buscar último fornecedor!" + err.getMessage() );
		}

		return codigo;
	}

	private int inserirFor() {
		int codfor = 0;

		try {

			codfor = daocli.insereFor( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "CPFORNECED"), Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "VDCLIENTE" ), 
					txtCodCli.getVlrInteger(), Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "CPTIPOFOR" ),(Integer) bPref.get( "CODTIPOFOR" ));

			daocli.getConn().commit();

		} catch ( SQLException e ) {
			try {
				daocli.getConn().rollback();
			} catch ( SQLException e1 ) {
				e1.printStackTrace();
			}

			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao inserir Fornecedor" + e.getMessage() );
		}

		return codfor;
	}

	private int pesqFor() {

		int codfor = 0;


		try{
			codfor = daocli.pesquisaFor( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "VDCLIENTE" ), txtCodCli.getVlrInteger() );
		}catch (Exception e) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Não foi possível pesquisar se existe fornecedor.\n" + e.getMessage(), true, con, e );	
		} 

		return codfor;

	}

	private void carregaTabelaObs() {

		int iCodCli = 0;
		ResultSet rs = null;
		PreparedStatement ps = null;
		StringBuffer sSql = new StringBuffer();

		Object[] oLinha = null;

		try {

			iCodCli = txtCodCli.getVlrInteger().intValue();
			txaTxtObsCli.setText( "" );
			bExecCargaObs = true;
			tbObsData.limpa();

			if ( iCodCli != 0 ) {

				sSql.append( "SELECT OC.DTOBSCLI, OC.HOBSCLI, OC.SEQOBSCLI FROM VDOBSCLI OC " );
				sSql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODCLI=? " );
				sSql.append( "ORDER BY OC.DTOBSCLI DESC, OC.HOBSCLI DESC, OC.SEQOBSCLI DESC" );

				ps = con.prepareStatement( sSql.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "VDOBSCLI" ) );
				ps.setInt( 3, iCodCli );
				rs = ps.executeQuery();

				while ( rs.next() ) {
					oLinha = new Object[ 3 ];
					oLinha[ 0 ] = rs.getString( "DTOBSCLI" );
					oLinha[ 1 ] = rs.getString( "HOBSCLI" );
					oLinha[ 2 ] = rs.getString( "SEQOBSCLI" );
					tbObsData.adicLinha( oLinha );
				}

				rs.close();
				ps.close();

				con.commit();

			}

		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Não foi possível carregar dados da tabela observações de cliente (VDOBSCLI).\n" + err.getMessage(), true, con, err );
		} finally {
			iCodCli = 0;
			rs = null;
			ps = null;
			sSql = null;
			oLinha = null;
			bExecCargaObs = false;
			if ( tbObsData.getNumLinhas() > 0 )
				tbObsData.setLinhaSel( 0 );
		}
	}

	private void carregaTabAtendo() {
		try {
			tabatd.setDataVector( daoatendo.carregaGridPorCliente( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "ATATENDIMENTO" ), 
					txtCodCli.getVlrInteger(), codatend_atual, acesatdolerout));
		} catch (SQLException e) {
			Funcoes.mensagemErro( this, "Erro ao carregar grid de atendimento!!!" );
		}
	}
	/*	private void carregaTabHist() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSql = new StringBuffer();
		int iLinha = 0;

		try {

			sSql.append( "select h.codhisttk, coalesce(h.sithisttk,'') sithisttk, coalesce(h.tipohisttk,'') tipohisttk," );
			sSql.append( "coalesce(h.datahisttk,cast('today' as date)) datahisttk, coalesce(h.deschisttk,'') deschisttk," );
			sSql.append( "coalesce(h.codcto,'') codcto, coalesce(h.codatend,'') codatend, coalesce(a.nomeatend,'') nomeatend," );
			sSql.append( "coalesce(h.horahisttk,'') horahisttk " );
			sSql.append( "FROM TKHISTORICO H, ATATENDENTE A " );
			sSql.append( "WHERE H.CODCLI=? AND H.CODEMPCL=? AND H.CODFILIALCL=? " );
			sSql.append( "AND A.CODATEND=H.CODATEND AND A.CODEMP=H.CODEMPAE AND A.CODFILIAL=H.CODFILIALAE " );
			sSql.append( "ORDER BY H.DATAHISTTK DESC,H.HORAHISTTK DESC,H.CODHISTTK" );

			ps = con.prepareStatement( sSql.toString() );
			ps.setInt( 1, txtCodCli.getVlrInteger().intValue() );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, Aplicativo.iCodFilial );
			rs = ps.executeQuery();

			tabHist.limpa();

			while ( rs.next() ) {

				tabHist.adicLinha();
				tabHist.setValor( rs.getString( "CodHistTK" ), iLinha, 0 );
				tabHist.setValor( rs.getString( "SitHistTK" ), iLinha, 1 );
				tabHist.setValor( rs.getString( "TipoHistTK" ), iLinha, 2 );
				tabHist.setValor( rs.getString( "CodCto" ), iLinha, 3 );
				tabHist.setValor( rs.getString( "CodAtend" ), iLinha, 4 );
				tabHist.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "DataHistTK" ) ), iLinha, 5 );
				tabHist.setValor( rs.getString( "DescHistTK" ), iLinha, 6 );
				tabHist.setValor( rs.getString( "NomeAtend" ), iLinha, 7 );
				tabHist.setValor( rs.getString( "HoraHistTK" ), iLinha, 8 );
				iLinha++;

			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( Exception err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao carregar tabela de históricos!\n" + err.getMessage(), true, con, err );
		} finally {
			rs = null;
			ps = null;
			sSql = null;
		}
	}
	 */
	private void carregaObs() {

		int iCodCli = 0;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sSql = null;

		try {

			if ( !bExecCargaObs ) {

				if ( tbObsData.getSelectedRow() > -1 ) {

					iCodCli = txtCodCli.getVlrInteger().intValue();

					if ( iCodCli != 0 ) {

						sSql = "SELECT OC.TXTOBSCLI FROM VDOBSCLI OC WHERE CODEMP=? AND CODFILIAL=? AND CODCLI=? AND SEQOBSCLI=? ";

						ps = con.prepareStatement( sSql );
						ps.setInt( 1, Aplicativo.iCodEmp );
						ps.setInt( 2, ListaCampos.getMasterFilial( "VDOBSCLI" ) );
						ps.setInt( 3, iCodCli );
						ps.setInt( 4, Integer.parseInt( tbObsData.getValor( tbObsData.getSelectedRow(), 2 ).toString() ) );
						rs = ps.executeQuery();

						if ( rs.next() ) {
							txaTxtObsCli.setVlrString( rs.getString( "TXTOBSCLI" ) );
						}

						rs.close();
						ps.close();

						con.commit();
					}

				}

			}

		} catch ( Exception err ) {
			Funcoes.mensagemErro( this, "Não foi possível carregar dados da tabela " + "observações de cliente (VDOBSCLI).\n" + err.getMessage(), true, con, err );
		} finally {
			rs = null;
			ps = null;
			sSql = null;
		}
	}

	private boolean duploCNPJ() {

		boolean bRetorno = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = "SELECT CNPJCLI FROM VDCLIENTE WHERE CODEMP=? AND CODFILIAL=? AND CNPJCLI=?";

		try {

			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "CPCLIENTE" ) );
			ps.setString( 3, txtCnpjCli.getVlrString() );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				bRetorno = true;
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( Exception err ) {
			Funcoes.mensagemErro( this, "Erro ao checar CNPJ.\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}
		return bRetorno;
	}

	private void editaAtendimento() {
		int linhasel = tabatd.getLinhaSel();

		if ( linhasel > -1 ) {
			visualizaAtend();
		}
		else {
			Funcoes.mensagemInforma( this, "Nenhum atendimento selecionado!" );
		}
	}


	private void visualizaAtend() {

		if (codatend_atual == null) {
			Funcoes.mensagemInforma( this, "Não existe atendente vinculado a este usuário, verifique!!!" );
			return;
		}

		Integer codatendo = (Integer) tabatd.getValor( tabatd.getLinhaSel(), COL_ATENDIMENTO.CODATENDO.ordinal() );
		Integer codatend = (Integer) tabatd.getValor( tabatd.getLinhaSel(), COL_ATENDIMENTO.CODATEND.ordinal());
		boolean atendimentoBloqueado = false;

		int icodAtend = codatend;
		int icodAtendo = codatendo;

		Integer codchamado = (Integer) tabatd.getValor( tabatd.getLinhaSel(), COL_ATENDIMENTO.CODCHAMADO.ordinal() );

		try {

			FNovoAtend dl = new FNovoAtend( true );
			atendimentoBloqueado = !daoatendo.bloquearAtendimentos( codatendo, (String) tabatd.getValor( tabatd.getLinhaSel(), COL_ATENDIMENTO.DATAATENDOFIN.ordinal() ), 
					(String) tabatd.getValor( tabatd.getLinhaSel(), COL_ATENDIMENTO.HORAATENDOFIN.ordinal() ), acesatdoaltout, codatend_atual, codatend );

			if ( dl != null && dl.isUpdate() ) {
				dl.adicAtendimento( txtCodCli.getVlrInteger(), codchamado, this, true, con, icodAtendo, icodAtend, "A", false, (Integer) tabatd.getValor( tabatd.getLinhaSel(), COL_ATENDIMENTO.CODORC.ordinal() ), atendimentoBloqueado );
			}
			else {
				dl = new FNovoAtend( txtCodCli.getVlrInteger(), codchamado, this, true, con, icodAtendo, icodAtend, "A", false, null, (Integer) tabatd.getValor( tabatd.getLinhaSel(), COL_ATENDIMENTO.CODORC.ordinal() ), atendimentoBloqueado );
			}
			if ( fPrim.temTela( "Edição de Atendimento: " + icodAtendo ) == false ) {
				fPrim.criatela( "Edição de Atendimento: " + icodAtendo, dl, con );
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao carregar campos!" );
		}

	}
	/*	private void editaHist() {

		int iLin = 0;
		int iCod = 0;
		Object oRets[];

		if ( ( iLin = tabHist.getLinhaSel() ) < 0 ) {
			Funcoes.mensagemInforma( this, "Não ha nenhum histórico selecionado!" );
			return;
		}
		iCod = txtCodCli.getVlrInteger().intValue();

		DLNovoHist dl = new DLNovoHist( iCod, 1, this );
		dl.setConexao( con );
		dl.setValores( new Object[] { (String) tabHist.getValor( iLin, 6 ), (String) tabHist.getValor( iLin, 4 ), (String) tabHist.getValor( iLin, 1 ), (String) tabHist.getValor( iLin, 2 ), (Date) Funcoes.strDateToSqlDate( (String) tabHist.getValor( iLin, 5 ) ) } );
		dl.setVisible( true );

		if ( dl.OK ) {

			oRets = dl.getValores();
			PreparedStatement ps = null;
			String sSQL = null;

			try {

				sSQL = "EXECUTE PROCEDURE TKSETHISTSP(?,?,?,?,?,?,?,?,?,?,?,?)";
				ps = con.prepareStatement( sSQL );
				ps.setInt( 1, Integer.parseInt( (String) tabHist.getValor( iLin, 0 ) ) );
				ps.setInt( 2, Aplicativo.iCodEmp );
				ps.setNull( 3, Types.INTEGER );
				ps.setNull( 4, Types.INTEGER );
				ps.setInt( 5, lcCampos.getCodFilial() );
				ps.setInt( 6, txtCodCli.getVlrInteger().intValue() );
				ps.setString( 7, (String) oRets[ 0 ] );// Descrição do historico
				ps.setInt( 8, ListaCampos.getMasterFilial( "ATATENDENTE" ) );// Filial do atendete
				ps.setString( 9, (String) oRets[ 1 ] );// codígo atendente
				ps.setString( 10, (String) oRets[ 2 ] );// status do historico
				ps.setDate( 11, (Date) oRets[ 4 ] );// data do historico
				ps.setString( 12, (String) oRets[ 3 ] );// status do historico
				ps.execute();
				ps.close();

				con.commit();

			} catch ( Exception err ) {
				err.printStackTrace();
				Funcoes.mensagemErro( this, "Erro ao salvar o histórico!\n" + err.getMessage(), true, con, err );
			} finally {
				ps = null;
				sSQL = null;
			}
		}
		dl.dispose();
		carregaTabHist();
	}
	 */
	private void editObs() {

		int iCodCli = 0;
		PreparedStatement ps = null;
		StringBuffer sSql = new StringBuffer();
		DLInputText dl = null;
		iCodCli = txtCodCli.getVlrInteger().intValue();

		if ( ( tbObsData.getSelectedRow() > -1 ) && ( iCodCli != 0 ) ) {

			try {

				dl = new DLInputText( this, "Observação", false );
				dl.setTexto( txaTxtObsCli.getText() );
				dl.setVisible( true );

				if ( dl.OK ) {

					try {

						sSql.append( "UPDATE VDOBSCLI SET DTOBSCLI=?, HOBSCLI=? , TXTOBSCLI=? " );
						sSql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODCLI=? AND SEQOBSCLI=?" );

						ps = con.prepareStatement( sSql.toString() );
						ps.setDate( 1, new Date( Calendar.getInstance().getTime().getTime() ) );
						ps.setTime( 2, new Time( Calendar.getInstance().getTime().getTime() ) );
						ps.setString( 3, dl.getTexto() );
						ps.setInt( 4, Aplicativo.iCodEmp );
						ps.setInt( 5, ListaCampos.getMasterFilial( "VDOBSCLI" ) );
						ps.setInt( 6, iCodCli );
						ps.setInt( 7, Integer.parseInt( tbObsData.getValor( tbObsData.getSelectedRow(), 2 ).toString() ) );
						ps.executeUpdate();
						ps.close();

						con.commit();

					} catch ( SQLException err ) {
						Funcoes.mensagemErro( this, "Não foi possível alterar a observação.\n" + err.getMessage(), true, con, err );
					}
				}

				carregaTabelaObs();

			} finally {
				ps = null;
				sSql = null;
				dl = null;
			}
		}
	}

	private void exclObs() {

		int iCodCli = 0;
		PreparedStatement ps = null;
		String sSql = null;
		iCodCli = txtCodCli.getVlrInteger().intValue();

		if ( ( tbObsData.getSelectedRow() > -1 ) && ( iCodCli != 0 ) ) {

			if ( Funcoes.mensagemConfirma( this, "Confirma exclusão da mensagem?" ) == JOptionPane.YES_OPTION ) {

				try {

					sSql = "DELETE FROM VDOBSCLI WHERE CODEMP=? AND CODFILIAL=? AND CODCLI=? AND SEQOBSCLI=?";
					ps = con.prepareStatement( sSql );
					ps.setInt( 1, Aplicativo.iCodEmp );
					ps.setInt( 2, ListaCampos.getMasterFilial( "VDOBSCLI" ) );
					ps.setInt( 3, iCodCli );
					ps.setInt( 4, Integer.parseInt( tbObsData.getValor( tbObsData.getSelectedRow(), 2 ).toString() ) );
					ps.executeUpdate();
					ps.close();

					con.commit();

					carregaTabelaObs();

				} catch ( SQLException err ) {
					Funcoes.mensagemErro( this, "Não foi possível excluir a observação.\n" + err.getMessage(), true, con, err );
					err.printStackTrace();
				} finally {
					ps = null;
					iCodCli = 0;
					sSql = null;
				}

			}

		}
	}

	/*private void excluiHist() {

		if ( tabHist.getLinhaSel() == -1 ) {
			Funcoes.mensagemInforma( this, "Selecione um item na lista!" );
			return;
		}
		else if ( Funcoes.mensagemConfirma( this, "Deseja relamente excluir o histórico '" + tabHist.getValor( tabHist.getLinhaSel(), 0 ) + "'?" ) != JOptionPane.YES_OPTION ) {
			return;
		}

		PreparedStatement ps = null;
		String sSQL = null;

		try {

			sSQL = "DELETE FROM TKHISTORICO WHERE CODHISTTK=? AND CODEMP=? AND CODFILIAL=?";
			ps = con.prepareStatement( sSQL );
			ps.setString( 1, (String) tabHist.getValor( tabHist.getLinhaSel(), 0 ) );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "TKHISTORICO" ) );
			ps.execute();
			ps.close();

			con.commit();

			carregaTabHist();

		} catch ( Exception err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao excluir o histórico!\n" + err.getMessage(), true, con, err );
		}
	}
	 */
	public void exec( int codigoCliente ) {

		txtCodCli.setVlrInteger( codigoCliente );
		lcCampos.carregaDados();
	}

	/*private void geraHistorico( Integer iMes ) {

		PreparedStatement ps = null;
		String sSQL = null;
		Integer iCodAtende = getAtendente();

		if ( iCodAtende.compareTo( new Integer( 0 ) ) > 0 ) {

			try {

				sSQL = "EXECUTE PROCEDURE TKSETHISTSP(0,?,?,?,?,?,?,?,?,?,'" + iMes + "-01-" + txtAno.getVlrInteger() + "','V')";
				ps = con.prepareStatement( sSQL );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setNull( 2, Types.INTEGER );
				ps.setNull( 3, Types.INTEGER );
				ps.setInt( 4, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
				ps.setInt( 5, txtCodCli.getVlrInteger().intValue() );
				ps.setString( 6, "CONTATO" );// Descrição do historico
				ps.setInt( 7, ListaCampos.getMasterFilial( "ATATENDENTE" ) );// Filial do atendete
				ps.setInt( 8, iCodAtende.intValue() );// codígo atendente
				ps.setString( 9, "EF" );// status do historico
				ps.execute();
				ps.close();

				con.commit();

			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro ao inserir historicos para o cliente.\n" + err.getMessage(), true, con, err );
				err.printStackTrace();
			} finally {
				ps = null;
				sSQL = null;
			}
		}
		else {
			Funcoes.mensagemInforma( this, "Não é possivel gerar contatos para esse cliente, pois não existe um atendente\n" + "vinculado ao vendedor padrão!" );
		}

		carregaTabHist();
	}
	 */
	/*private void geraHistoricos( Integer iMes ) {

		Integer iCodAtende = getAtendente();

		if ( iCodAtende.compareTo( new Integer( 0 ) ) > 0 ) {

			HashMap<String, HashMap<String, Integer>> hmMeses = new HashMap<String, HashMap<String, Integer>>();
			HashMap<String, Integer> hmJan = new HashMap<String, Integer>();
			HashMap<String, Integer> hmFev = new HashMap<String, Integer>();
			HashMap<String, Integer> hmMar = new HashMap<String, Integer>();
			HashMap<String, Integer> hmAbr = new HashMap<String, Integer>();
			HashMap<String, Integer> hmMai = new HashMap<String, Integer>();
			HashMap<String, Integer> hmJun = new HashMap<String, Integer>();
			HashMap<String, Integer> hmJul = new HashMap<String, Integer>();
			HashMap<String, Integer> hmAgo = new HashMap<String, Integer>();
			HashMap<String, Integer> hmSet = new HashMap<String, Integer>();
			HashMap<String, Integer> hmOut = new HashMap<String, Integer>();
			HashMap<String, Integer> hmNov = new HashMap<String, Integer>();
			HashMap<String, Integer> hmDez = new HashMap<String, Integer>();

			hmJan.put( "ANT", txtAntQtdContJan.getVlrInteger() );
			hmJan.put( "NOVO", txtNovaQtdContJan.getVlrInteger() );
			hmFev.put( "ANT", txtAntQtdContFev.getVlrInteger() );
			hmFev.put( "NOVO", txtNovaQtdContFev.getVlrInteger() );
			hmMar.put( "ANT", txtAntQtdContMar.getVlrInteger() );
			hmMar.put( "NOVO", txtNovaQtdContMar.getVlrInteger() );
			hmAbr.put( "ANT", txtAntQtdContAbr.getVlrInteger() );
			hmAbr.put( "NOVO", txtNovaQtdContAbr.getVlrInteger() );
			hmMai.put( "ANT", txtAntQtdContMai.getVlrInteger() );
			hmMai.put( "NOVO", txtNovaQtdContMai.getVlrInteger() );
			hmJun.put( "ANT", txtAntQtdContJun.getVlrInteger() );
			hmJun.put( "NOVO", txtNovaQtdContJun.getVlrInteger() );
			hmJul.put( "ANT", txtAntQtdContJul.getVlrInteger() );
			hmJul.put( "NOVO", txtNovaQtdContJul.getVlrInteger() );
			hmAgo.put( "ANT", txtAntQtdContAgo.getVlrInteger() );
			hmAgo.put( "NOVO", txtNovaQtdContAgo.getVlrInteger() );
			hmSet.put( "ANT", txtAntQtdContSet.getVlrInteger() );
			hmSet.put( "NOVO", txtNovaQtdContSet.getVlrInteger() );
			hmOut.put( "ANT", txtAntQtdContOut.getVlrInteger() );
			hmOut.put( "NOVO", txtNovaQtdContOut.getVlrInteger() );
			hmNov.put( "ANT", txtAntQtdContNov.getVlrInteger() );
			hmNov.put( "NOVO", txtNovaQtdContNov.getVlrInteger() );
			hmDez.put( "ANT", txtAntQtdContDez.getVlrInteger() );
			hmDez.put( "NOVO", txtNovaQtdContDez.getVlrInteger() );

			hmMeses.put( "1", hmJan );
			hmMeses.put( "2", hmFev );
			hmMeses.put( "3", hmMar );
			hmMeses.put( "4", hmAbr );
			hmMeses.put( "5", hmMai );
			hmMeses.put( "6", hmJun );
			hmMeses.put( "7", hmJul );
			hmMeses.put( "8", hmAgo );
			hmMeses.put( "9", hmSet );
			hmMeses.put( "10", hmOut );
			hmMeses.put( "11", hmNov );
			hmMeses.put( "12", hmDez );

			if ( iMes == null ) {
				for ( int iM = 1; iM < 13; iM++ ) {
					int iQtdAnt = ( (Integer) ( hmMeses.get( String.valueOf( iM ) ) ).get( "ANT" ) ).intValue();
					int iQtdNov = ( (Integer) ( hmMeses.get( String.valueOf( iM ) ) ).get( "NOVO" ) ).intValue();

					if ( iQtdNov > 0 ) {
						if ( iQtdAnt > iQtdNov ) {
							Funcoes.mensagemInforma( this, "A nova quantidade informada é menor ou igual a quantidade atual, " + "\n você deve excluir os contatos manualmente." );
						}
						else if ( iQtdNov > iQtdAnt ) {
							for ( int i = 0; ( iQtdNov - iQtdAnt ) > i; i++ ) {
								geraHistorico( new Integer( iM ) );
							}
						}
					}
				}
			}
			else {
				int iQtdAnt = ( (Integer) ( hmMeses.get( String.valueOf( iMes ) ) ).get( "ANT" ) ).intValue();
				int iQtdNov = ( (Integer) ( hmMeses.get( String.valueOf( iMes ) ) ).get( "NOVO" ) ).intValue();
				if ( iQtdNov > 0 ) {
					if ( iQtdAnt > iQtdNov ) {
						Funcoes.mensagemInforma( this, "A nova quantidade informada é menor ou igual a quantidade atual, " + "\n você deve excluir os contatos manualmente." );
					}
					else if ( iQtdNov > iQtdAnt ) {
						for ( int i = 0; ( iQtdNov - iQtdAnt ) > i; i++ ) {
							geraHistorico( iMes );
						}
					}
				}
			}
			getContatos();
			carregaTabHist();
		}
		else {
			Funcoes.mensagemInforma( this, "Não é possivel gerar contatos para esse cliente, pois não existe um atendente\n" + "vinculado ao vendedor padrão!" );
		}
	}*/

	private Object[] getAgente() {

		Object[] oRet = new Object[ 2 ];
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;

		try {

			sSQL = "SELECT U.CODAGE,U.TIPOAGE FROM SGUSUARIO U WHERE CODEMP=? AND CODFILIAL=? AND IDUSU=?";

			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			ps.setString( 3, Aplicativo.getUsuario().getIdusu() );
			rs = ps.executeQuery();

			while ( rs.next() ) {
				oRet[ 0 ] = new Integer( rs.getInt( 1 ) );
				oRet[ 1 ] = rs.getString( 2 );
			}

			con.commit();
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}

		return oRet;
	}

	private Integer getAtendente() {

		Integer iRet = new Integer( 0 );
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSql = null;

		try {

			sSql = "SELECT CODATEND FROM ATATENDENTE WHERE CODEMPVE=? AND CODFILIALVE=? AND CODVEND=?";

			ps = con.prepareStatement( sSql );
			ps.setInt( 1, lcVend.getCodEmp() );
			ps.setInt( 2, lcVend.getCodFilial() );
			ps.setInt( 3, txtCodVend.getVlrInteger().intValue() );
			rs = ps.executeQuery();

			if ( rs.next() ) {
				iRet = new Integer( rs.getInt( 1 ) );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( Exception err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro na busca de atendente vinculado ao vendedor.\n" + err.getMessage(), true, con, err );
		} finally {
			ps = null;
			rs = null;
			sSql = null;
		}
		return iRet;
	}

	private void getContatos() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSql = new StringBuffer();
		int iMes = 0;
		int iQtd = 0;

		try {

			txtAntQtdContJan.setVlrString( "" );
			txtNovaQtdContJan.setVlrString( "" );
			txtAntQtdContFev.setVlrString( "" );
			txtNovaQtdContFev.setVlrString( "" );
			txtAntQtdContMar.setVlrString( "" );
			txtNovaQtdContMar.setVlrString( "" );
			txtAntQtdContAbr.setVlrString( "" );
			txtNovaQtdContAbr.setVlrString( "" );
			txtAntQtdContMai.setVlrString( "" );
			txtNovaQtdContMai.setVlrString( "" );
			txtAntQtdContJun.setVlrString( "" );
			txtNovaQtdContJun.setVlrString( "" );
			txtAntQtdContJul.setVlrString( "" );
			txtNovaQtdContJul.setVlrString( "" );
			txtAntQtdContAgo.setVlrString( "" );
			txtNovaQtdContAgo.setVlrString( "" );
			txtAntQtdContSet.setVlrString( "" );
			txtNovaQtdContSet.setVlrString( "" );
			txtAntQtdContOut.setVlrString( "" );
			txtNovaQtdContOut.setVlrString( "" );
			txtAntQtdContNov.setVlrString( "" );
			txtNovaQtdContNov.setVlrString( "" );
			txtAntQtdContDez.setVlrString( "" );
			txtNovaQtdContDez.setVlrString( "" );

			sSql.append( "SELECT EXTRACT(MONTH FROM TK.DATAHISTTK),COUNT(1) FROM TKHISTORICO TK " );
			sSql.append( "WHERE TK.CODEMP=? AND TK.CODFILIAL=? AND EXTRACT(YEAR FROM TK.DATAHISTTK)=? " );
			sSql.append( "AND CODEMPCL=? AND CODFILIALCL=? AND CODCLI=? " );
			sSql.append( "GROUP BY 1 ORDER BY 1" );

			ps = con.prepareStatement( sSql.toString() );
			ps.setInt( 1, lcCampos.getCodEmp() );
			ps.setInt( 2, lcCampos.getCodFilial() );
			ps.setInt( 3, txtAno.getVlrInteger().intValue() );
			ps.setInt( 4, lcCampos.getCodEmp() );
			ps.setInt( 5, lcCampos.getCodFilial() );
			ps.setInt( 6, txtCodCli.getVlrInteger().intValue() );

			rs = ps.executeQuery();

			while ( rs.next() ) {

				iMes = rs.getInt( 1 );
				iQtd = rs.getInt( 2 );

				if ( iMes == 1 ) {
					txtAntQtdContJan.setVlrInteger( new Integer( iQtd ) );
					txtNovaQtdContJan.setVlrString( "" );
				}
				else if ( iMes == 2 ) {
					txtAntQtdContFev.setVlrInteger( new Integer( iQtd ) );
					txtNovaQtdContFev.setVlrString( "" );
				}
				else if ( iMes == 3 ) {
					txtAntQtdContMar.setVlrInteger( new Integer( iQtd ) );
					txtNovaQtdContMar.setVlrString( "" );
				}
				else if ( iMes == 4 ) {
					txtAntQtdContAbr.setVlrInteger( new Integer( iQtd ) );
					txtNovaQtdContAbr.setVlrString( "" );
				}
				else if ( iMes == 5 ) {
					txtAntQtdContMai.setVlrInteger( new Integer( iQtd ) );
					txtNovaQtdContMai.setVlrString( "" );
				}
				else if ( iMes == 6 ) {
					txtAntQtdContJun.setVlrInteger( new Integer( iQtd ) );
					txtNovaQtdContJun.setVlrString( "" );
				}
				else if ( iMes == 7 ) {
					txtAntQtdContJul.setVlrInteger( new Integer( iQtd ) );
					txtNovaQtdContJul.setVlrString( "" );
				}
				else if ( iMes == 8 ) {
					txtAntQtdContAgo.setVlrInteger( new Integer( iQtd ) );
					txtNovaQtdContAgo.setVlrString( "" );
				}
				else if ( iMes == 9 ) {
					txtAntQtdContSet.setVlrInteger( new Integer( iQtd ) );
					txtNovaQtdContSet.setVlrString( "" );
				}
				else if ( iMes == 10 ) {
					txtAntQtdContOut.setVlrInteger( new Integer( iQtd ) );
					txtNovaQtdContOut.setVlrString( "" );
				}
				else if ( iMes == 11 ) {
					txtAntQtdContNov.setVlrInteger( new Integer( iQtd ) );
					txtNovaQtdContNov.setVlrString( "" );
				}
				else if ( iMes == 12 ) {
					txtAntQtdContDez.setVlrInteger( new Integer( iQtd ) );
					txtNovaQtdContDez.setVlrString( "" );
				}

			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( Exception err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro na busca de atendente vinculado ao vendedor.\n" + err.getMessage(), true, con, err );
		} finally {
			ps = null;
			rs = null;
			sSql = null;
		}
	}


	/**
	 * private boolean[] getPrefere() {
	 * 
	 * boolean[] bRet = new boolean[ 8 ]; String sSQL = null; PreparedStatement ps = null; ResultSet rs = null;
	 * 
	 * try {
	 * 
	 * sSQL = "SELECT P.SETORVENDA, P.RGCLIOBRIG, P.CLIMESMOCNPJ, P.CNPJOBRIGCLI, P.CODTIPOFORCLI," + "P.CONSISTEIECLI, P.CONSISTCPFCLI, " + "(CASE WHEN P.USUATIVCLI='N' THEN 'S' " + "WHEN P.USUATIVCLI='S' AND U.ATIVCLI='S' THEN 'S' " + "ELSE 'N' " + "END) HABATIVCLI  " +
	 * "FROM SGPREFERE1 P LEFT OUTER JOIN SGUSUARIO U " + "ON U.CODEMP=? AND U.CODFILIAL=? AND U.IDUSU=? "+ "WHERE P.CODEMP=? AND P.CODFILIAL=?";
	 * 
	 * try { ps = con.prepareStatement( sSQL ); ps.setInt( 1, Aplicativo.iCodEmp ); ps.setInt( 2, ListaCampos.getMasterFilial( "SGUSUARIO" ) ); ps.setString( 3, Aplicativo.getUsuario().getIdusu().toLowerCase() ); ps.setInt( 4, Aplicativo.iCodEmp ); ps.setInt( 5, ListaCampos.getMasterFilial( "SGPREFERE1" ) ); rs
	 * = ps.executeQuery();
	 * 
	 * if ( rs.next() ) {
	 * 
	 * bRet[ 0 ] = "CA".indexOf( rs.getString( "SetorVenda" ) ) >= 0; bRet[ 1 ] = "S".equals( rs.getString( "RGCLIOBRIG" ) ); bRet[ 2 ] = "S".equals( rs.getString( "CLIMESMOCNPJ" ) ); bRet[ 3 ] = "S".equals( rs.getString( "CLIMESMOCNPJ" ) ); bRet[ 4 ] = "S".equals( rs.getString( "CONSISTEIECLI" ) );
	 * bRet[ 5 ] = "S".equals( rs.getString( "CONSISTCPFCLI" ) ); bRet[ 6 ] = "S".equals( rs.getString( "HABATIVCLI" ) ); bRet[ 7 ] = rs.getString( "CODTIPOFORCLI" ) ; }
	 * 
	 * rs.close(); ps.close();
	 * 
	 * if ( !con.getAutoCommit() ) { con.commit(); } } catch ( SQLException err ) { Funcoes.mensagemErro( this, "Erro ao verificar preferências!\n" + err.getMessage(), true, con, err ); err.printStackTrace(); } } finally { sSQL = null; ps = null; rs = null; } return bRet; }
	 * 
	 * 
	 */

	private void grpCli() {

		DLGrpCli dl = null;
		int iCodCli = 0;
		int iCodPesq = txtCodPesq.getVlrInteger().intValue();
		try {
			if ( iCodPesq != 0 ) {
				dl = new DLGrpCli( this );
				dl.carregaClientes( con, iCodPesq, txtDescPesq.getVlrString() );
				dl.setVisible( true );
				if ( dl.OK ) {
					iCodCli = dl.getCodCli();
					if ( iCodCli != 0 ) {
						txtCodCli.setVlrInteger( new Integer( iCodCli ) );
						lcCampos.carregaDados();
					}
				}
			}
		} finally {
			dl = null;
			iCodPesq = 0;
			iCodCli = 0;
		}
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		ImprimeOS imp = new ImprimeOS( "", con );
		String[] sValores;

		DLRCliente dl = new DLRCliente( this, con );
		dl.setVisible( true );
		if ( dl.OK == false ) {
			dl.dispose();
			return;
		}

		sValores = dl.getValores();

		if ( "1".equals( sValores[ RET_DLRCLIENTE.MODO.ordinal() ] ) ) {
			imprimeResumido1( imp, sValores );
		}
		else if ( "2".equals( sValores[ RET_DLRCLIENTE.MODO.ordinal() ] ) ) {
			imprimeResumido2( imp, sValores );
		}
		else if ( "3".equals( sValores[ RET_DLRCLIENTE.MODO.ordinal() ] ) ) {
			imprimeResumido3( imp, sValores );
		}
		else if ( "C".equals( sValores[ RET_DLRCLIENTE.MODO.ordinal() ] ) ) {
			imprimeCompleto( imp, sValores );
		}
		else if ( "A".equals( sValores[ RET_DLRCLIENTE.MODO.ordinal() ] ) ) {
			imprimeAlinhaFilial( imp, sValores );
		}

		dl.dispose();

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}

	}

	private void imprimeResumido1( final ImprimeOS imp, final String[] sValores ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();
		String sObs = "";
		StringBuffer sWhere = new StringBuffer();
		String sFrom = "";
		String linhaFina = StringFunctions.replicate( "-", 133 );
		Vector<String> vObs = null;
		FAndamento And = null;
		int linPag = imp.verifLinPag() - 1;
		int iContaReg = 0;

		if ( sValores[ RET_DLRCLIENTE.OBS.ordinal() ].equals( "S" ) ) {
			sObs = ",C1.OBSCLI ";
		}

		if ( sValores[ RET_DLRCLIENTE.DE.ordinal() ].trim().length() > 0 ) {
			// sWhere.append( " AND C1.RAZCLI >='" + sValores[ 2 ] + "'" );
			// imp.addSubTitulo( "RAZAO MAIORES QUE " + sValores[ 2 ].trim() );

			sWhere.append( " AND C1.CODCLI >='" + sValores[ RET_DLRCLIENTE.DE.ordinal() ] + "'" );
			imp.addSubTitulo( "CODIGOS CLIENTES MAIORES QUE " + sValores[ 2 ].trim() );
		}
		if ( sValores[ RET_DLRCLIENTE.A.ordinal() ].trim().length() > 0 ) {
			// sWhere.append( " AND C1.RAZCLI <='" + sValores[ 3 ] + "'" );
			// imp.addSubTitulo( "RAZAO MENORES QUE " + sValores[ 3 ].trim() );

			sWhere.append( " AND C1.CODCLI <='" + sValores[ RET_DLRCLIENTE.A.ordinal() ] + "'" );
			imp.addSubTitulo( "CODIGOS CLIENTES MENORES QUE " + sValores[ RET_DLRCLIENTE.A.ordinal() ].trim() );
		}
		if ( sValores[ RET_DLRCLIENTE.FIS.ordinal() ].equals( "N" ) ) {
			sWhere.append( " AND C1.PESSOACLI <> 'F'" );
			imp.addSubTitulo( "PESSOAS JURIDICAS" );
		}
		if ( sValores[ RET_DLRCLIENTE.CODMUNIC.ordinal() ].length() > 0 ) {
			if ( "A".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				//sWhere.append( " AND C1.CIDCLI=" );
				sWhere.append( " AND C1.CODMUNIC=" );
			}
			else if ( "E".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				//sWhere.append( " AND C1.CIDENT=" );
				sWhere.append( " AND C1.CODMUNICENT=" );
			}
			else if ( "C".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				//sWhere.append( " AND C1.CIDCOB=" );
				sWhere.append( " AND C1.CODMUNICCOB=" );
			}
			sWhere.append( "'" + sValores[ RET_DLRCLIENTE.CODMUNIC.ordinal() ] + "'" );
			imp.addSubTitulo( "CIDADE = " + sValores[ RET_DLRCLIENTE.CODMUNIC.ordinal() ].trim() );
		}
		if ( sValores[ RET_DLRCLIENTE.JUR.ordinal() ].equals( "N" ) ) {
			sWhere.append( " AND C1.PESSOACLI <> 'J'" );
			imp.addSubTitulo( "PESSOAS FISICA" );
		}
		if ( !sValores[ RET_DLRCLIENTE.CODVEND.ordinal() ].trim().equals( "" ) ) {
			sWhere.append( " AND C1.CODVEND =" + sValores[ RET_DLRCLIENTE.CODVEND.ordinal() ] );
			imp.addSubTitulo( "REPRES. = " + sValores[ RET_DLRCLIENTE.CODVEND.ordinal() ] + "-" + sValores[ RET_DLRCLIENTE.NOMEVEND.ordinal() ] );
		}
		if ( sValores[ RET_DLRCLIENTE.CODSETOR.ordinal() ].length() > 0 ) {
			if ( !(Boolean) bPref.get( "SetorVenda" ) ) {
				sFrom = ",VDVENDEDOR V ";
				sWhere.append( " AND C1.CODEMPVD=V.CODEMP AND C1.CODFILIALVD=V.CODFILIAL AND C1.CODVEND=V.CODVEND AND V.CODSETOR = " + sValores[ RET_DLRCLIENTE.CODSETOR.ordinal() ] );
				;
			}
			else {
				sWhere.append( " AND C1.CODSETOR = " + sValores[ RET_DLRCLIENTE.CODSETOR.ordinal() ] );
			}
			imp.addSubTitulo( "SETOR = " + sValores[ RET_DLRCLIENTE.DESCSETOR.ordinal() ] );
		}
		if ( sValores[ RET_DLRCLIENTE.CODTIPOCLI.ordinal() ].length() > 0 ) {
			sWhere.append( " AND C1.CODTIPOCLI=" + sValores[ RET_DLRCLIENTE.CODTIPOCLI.ordinal() ] );
			imp.addSubTitulo( "TIPO DE CLIENTE=" + sValores[ RET_DLRCLIENTE.DESCTIPOCLI.ordinal() ] );
		}
		if ( sValores[ RET_DLRCLIENTE.CODCLASCLI.ordinal() ].length() > 0 ) {
			sWhere.append( " AND C1.CODCLASCLI=" + sValores[ RET_DLRCLIENTE.CODCLASCLI.ordinal() ] );
			imp.addSubTitulo( "CLASSIFICACAO DO CLIENTE = " + sValores[ RET_DLRCLIENTE.DESCCLASCLI.ordinal() ] );
		}
		if ( sValores[ RET_DLRCLIENTE.BAIRRO.ordinal() ].length() > 0 ) {
			if ( "A".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.BAIRCLI=" );
			}
			else if ( "E".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.BAIRENT=" );
			}
			else if ( "C".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.BAIRCOB=" );
			}
			sWhere.append( "'" + sValores[ RET_DLRCLIENTE.BAIRRO.ordinal() ] + "'" );
			imp.addSubTitulo( "BAIRRO = " + sValores[ RET_DLRCLIENTE.BAIRRO.ordinal() ] );
		}

		if ( !sValores[ RET_DLRCLIENTE.SIGLAUF.ordinal() ].equals( "" ) ) {
			if ( "A".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.UFCLI=" );
			}
			else if ( "E".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.UFENT=" );
			}
			else if ( "C".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.UFCOB=" );
			}
			sWhere.append( "'" + sValores[ RET_DLRCLIENTE.SIGLAUF.ordinal() ] + "'" );
			imp.addSubTitulo( "ESTADO = " + sValores[ RET_DLRCLIENTE.SIGLAUF.ordinal() ] );
		}

		if ( sValores[ RET_DLRCLIENTE.ATIV.ordinal() ].equals( "S" ) ) {
			sWhere.append( " AND C1.ATIVOCLI='S' " );
		}

		if ( sValores[ RET_DLRCLIENTE.INATIV.ordinal() ].equals( "S" ) ) {
			sWhere.append( " AND C1.ATIVOCLI='N' " );
		}

		try {

			imp.limpaPags();
			imp.montaCab();
			imp.setTitulo( "Relatório de Clientes" );

			ps = con.prepareStatement( "SELECT COUNT(*) FROM VDCLIENTE C1" + sFrom + " WHERE C1.CODEMP=? AND C1.CODFILIAL=? " + sWhere.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
			rs = ps.executeQuery();
			rs.next();

			And = new FAndamento( "Montando Relatório, Aguarde!", 0, rs.getInt( 1 ) - 1 );
			And.setVisible( true );

			con.commit();

			sSQL.append( "SELECT C1.CODCLI,C1.RAZCLI," );
			if ( "A".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sSQL.append( "C1.ENDCLI,C1.NUMCLI,C1.CIDCLI," );
			}
			else if ( "E".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sSQL.append( "C1.ENDENT AS ENDCLI,C1.CIDENT AS CIDCLI," );
			}
			else if ( "C".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sSQL.append( "C1.ENDCOB AS ENDCLI,C1.CIDCOB AS CIDCLI," );
			}
			sSQL.append( "C1.FONECLI,C1.DDDCLI,C1.CODPESQ " );
			sSQL.append( sObs );
			sSQL.append( "FROM VDCLIENTE C1 " );
			sSQL.append( sFrom );
			sSQL.append( "WHERE C1.CODEMP=? AND C1.CODFILIAL=? " );
			sSQL.append( sWhere );
			sSQL.append( " ORDER BY " + sValores[ RET_DLRCLIENTE.ORDEM.ordinal() ] );

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
			rs = ps.executeQuery();

			while ( rs.next() ) {

				if ( imp.pRow() >= linPag ) {

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "+" + linhaFina + "+" );
					imp.incPags();
					imp.eject();

				}

				if ( imp.pRow() == 0 ) {

					imp.impCab( 136, true );
					imp.pulaLinha( 0, imp.comprimido() );
					imp.say( 0, "|" + linhaFina + "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|  Código" );
					imp.say( 12, "|  Razão:" );
					imp.say( 46, "|  Matriz" );
					imp.say( 57, "|  Endereço:" );
					imp.say( 93, "|  Cidade:" );
					imp.say( 117, "|  Tel:" );
					imp.say( 135, "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + linhaFina + "|" );

				}

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" );
				imp.say( 4, rs.getString( "CodCli" ) );
				imp.say( 12, "|" );
				imp.say( 14, rs.getString( "RazCli" ) != null ? rs.getString( "RazCli" ).substring( 0, 30 ) : "" );
				imp.say( 46, "|" );

				if ( rs.getString( "CodPesq" ) != null && !rs.getString( "CodPesq" ).equals( rs.getString( "CodCLi" ) ) ) {
					imp.say( 49, String.valueOf( rs.getString( "CodPesq" ) ) );
				}

				imp.say( 57, "|" );
				imp.say( 59, ( rs.getString( "EndCli" ) != null ?  rs.getString( "EndCli" ).substring( 0, 27 ) : "" ).trim() + ", "  + ( rs.getString( "NumCli" ) != null ? rs.getString( "NumCli" ) : "" ) );
				imp.say( 93, "|" );
				imp.say( 96, rs.getString( "CidCli" ) != null ? rs.getString( "CidCli" ).substring( 0, 20 ) : "" );
				imp.say( 117, "|" );
				imp.say( 120, ( rs.getString( "DDDCli" ) != null ? "(" + rs.getString( "DDDCli" ) + ")" : "" ) + ( rs.getString( "FoneCli" ) != null ? Funcoes.setMascara( rs.getString( "FoneCli" ).trim(), "####-####" ) : "" ).trim() );
				imp.say( 135, "|" );

				if ( !sObs.equals( "" ) ) {

					vObs = Funcoes.quebraLinha( Funcoes.stringToVector( rs.getString( "ObsCli" ) ), 101 );

					for ( int i = 0; i < vObs.size(); i++ ) {

						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|" );
						imp.say( 14, vObs.elementAt( i ).toString() );
						imp.say( 117, "|" );
						imp.say( 135, "|" );

						if ( imp.pRow() >= linPag ) {

							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( 0, "+" + linhaFina + "+" );
							imp.incPags();
							imp.eject();

						}

					}

				}

				And.atualiza( iContaReg );
				iContaReg++;

			}

			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "+" + linhaFina + "+" );

			imp.eject();
			imp.fechaGravacao();

			con.commit();

			And.dispose();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela de contatos!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	private void imprimeResumido2( final ImprimeOS imp, final String[] sValores ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();
		String sObs = "";
		StringBuffer sWhere = new StringBuffer();
		String sFrom = "";
		String linhaFina = StringFunctions.replicate( "-", 133 );
		Vector<String> vObs = null;
		FAndamento And = null;
		int linPag = imp.verifLinPag() - 1;
		int iContaReg = 0;

		if ( sValores[ RET_DLRCLIENTE.OBS.ordinal() ].equals( "S" ) ) {
			sObs = ",C1.OBSCLI ";
		}

		if ( sValores[ RET_DLRCLIENTE.DE.ordinal() ].trim().length() > 0 ) {
			sWhere.append( " AND C1.RAZCLI >='" + sValores[ RET_DLRCLIENTE.DE.ordinal() ] + "'" );
			imp.addSubTitulo( "RAZAO MAIORES QUE " + sValores[ RET_DLRCLIENTE.DE.ordinal() ].trim() );
		}
		if ( sValores[ RET_DLRCLIENTE.A.ordinal() ].trim().length() > 0 ) {
			sWhere.append( " AND C1.RAZCLI <='" + sValores[ RET_DLRCLIENTE.A.ordinal() ] + "'" );
			imp.addSubTitulo( "RAZAO MENORES QUE " + sValores[ RET_DLRCLIENTE.A.ordinal() ].trim() );
		}
		if ( sValores[ RET_DLRCLIENTE.FIS.ordinal() ].equals( "N" ) ) {
			sWhere.append( " AND C1.PESSOACLI <> 'F'" );
			imp.addSubTitulo( "PESSOAS JURIDICAS" );
		}
		if ( sValores[ RET_DLRCLIENTE.CODMUNIC.ordinal() ].length() > 0 ) {
			if ( "A".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				//sWhere.append( " AND C1.CIDCLI=" );
				sWhere.append( " AND C1.CODMUNIC=" );
			}
			else if ( "E".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				//sWhere.append( " AND C1.CIDENT=" );
				sWhere.append( " AND C1.CODMUNICENT=" );
			}
			else if ( "C".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				//sWhere.append( " AND C1.CIDCOB=" );
				sWhere.append( " AND C1.CODMUNICCOB=" );
			}
			sWhere.append( "'" + sValores[ 5 ] + "'" );
			imp.addSubTitulo( "CIDADE = " + sValores[ 5 ].trim() );
		}
		if ( sValores[ 6 ].equals( "N" ) ) {
			sWhere.append( " AND C1.PESSOACLI <> 'J'" );
			imp.addSubTitulo( "PESSOAS FISICA" );
		}
		if ( !sValores[ 13 ].trim().equals( "" ) ) {
			sWhere.append( " AND C1.CODVEND =" + sValores[ 13 ] );
			imp.addSubTitulo( "REPRES. = " + sValores[ 13 ] + "-" + sValores[ 14 ] );
		}
		if ( sValores[ 8 ].length() > 0 ) {
			if ( !(Boolean) bPref.get( "SetorVenda" ) ) {
				sFrom = ",VDVENDEDOR V ";
				sWhere.append( " AND C1.CODEMPVD=V.CODEMP AND C1.CODFILIALVD=V.CODFILIAL AND C1.CODVEND=V.CODVEND AND V.CODSETOR = " + sValores[ 8 ] );
			}
			else {
				sWhere.append( " AND C1.CODSETOR = " + sValores[ 8 ] );
			}
			imp.addSubTitulo( "SETOR = " + sValores[ 9 ] );
		}
		if ( sValores[ 10 ].length() > 0 ) {
			sWhere.append( " AND C1.CODTIPOCLI=" + sValores[ 10 ] );
			imp.addSubTitulo( "TIPO DE CLIENTE=" + sValores[ 11 ] );
		}
		if ( sValores[ 15 ].length() > 0 ) {
			sWhere.append( " AND C1.CODCLASCLI=" + sValores[ 15 ] );
			imp.addSubTitulo( "CLASSIFICACAO DO CLIENTE = " + sValores[ 16 ] );
		}
		if ( sValores[ 18 ].length() > 0 ) {
			if ( "A".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.BAIRCLI=" );
			}
			else if ( "E".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.BAIRENT=" );
			}
			else if ( "C".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.BAIRCOB=" );
			}
			sWhere.append( "'" + sValores[ 18 ] + "'" );
			imp.addSubTitulo( "BAIRRO = " + sValores[ 18 ] );
		}

		if ( !sValores[ 19 ].equals( "" ) ) {
			if ( "A".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.UFCLI=" );
			}
			else if ( "E".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.UFENT=" );
			}
			else if ( "C".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.UFCOB=" );
			}
			sWhere.append( "'" + sValores[ 19 ] + "'" );
			imp.addSubTitulo( "ESTADO = " + sValores[ 19 ] );
		}

		if ( sValores[ 20 ].equals( "S" ) ) {
			sWhere.append( " AND C1.ATIVOCLI='S' " );
		}
		if ( sValores[ 21 ].equals( "S" ) ) {
			sWhere.append( " AND C1.ATIVOCLI='N' " );
		}

		try {

			imp.limpaPags();
			imp.montaCab();
			imp.setTitulo( "Relatório de Clientes" );

			ps = con.prepareStatement( "SELECT COUNT(*) FROM VDCLIENTE C1" + sFrom + " WHERE C1.CODEMP=? AND C1.CODFILIAL=? " + sWhere.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
			rs = ps.executeQuery();
			rs.next();

			And = new FAndamento( "Montando Relatório, Aguarde!", 0, rs.getInt( 1 ) - 1 );
			And.setVisible( true );

			con.commit();

			sSQL.append( "SELECT C1.CODCLI,C1.RAZCLI,C1.NOMECLI,C1.FONECLI," );
			if ( "A".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sSQL.append( "C1.ENDCLI,C1.CIDCLI,C1.NUMCLI," );
			}
			else if ( "E".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sSQL.append( "C1.ENDENT AS ENDCLI,C1.CIDENT AS CIDCLI,C1.NUMENT AS NUMCLI," );
			}
			else if ( "C".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sSQL.append( "C1.ENDCOB AS ENDCLI,C1.CIDCOB AS CIDCLI,C1.NUMCOB AS NUMCLI," );
			}
			sSQL.append( "C1.FAXCLI,C1.DDDCLI,C1.CONTCLI,C1.EMAILCLI,C1.SITECLI " );
			sSQL.append( sObs );
			sSQL.append( "FROM VDCLIENTE C1 " );
			sSQL.append( sFrom );
			sSQL.append( "WHERE C1.CODEMP=? AND C1.CODFILIAL=? " );
			sSQL.append( sWhere );
			sSQL.append( " ORDER BY " + sValores[ 0 ] );

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
			rs = ps.executeQuery();

			while ( rs.next() ) {

				if ( imp.pRow() >= linPag ) {

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "+" + linhaFina + "+" );
					imp.incPags();
					imp.eject();

				}

				if ( imp.pRow() == 0 ) {

					imp.impCab( 136, true );
					imp.pulaLinha( 0, imp.comprimido() );
					imp.say( 0, "|" + linhaFina + "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|  Código" );
					imp.say( 10, "|  Razão Social / Nome Fantasia" );
					imp.say( 42, "|  Contato / E-Mail" );
					imp.say( 74, "|  Endereço / Web Site" );
					imp.say( 117, "|   Tel / Fax" );
					imp.say( 135, "|" );

				}

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" + linhaFina + "|" );
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" );
				imp.say( 2, rs.getString( "CodCli" ) );
				imp.say( 10, "|" );
				imp.say( 11, rs.getString( "RazCli" ) != null ? rs.getString( "RazCli" ).substring( 0, 30 ) : "" );
				imp.say( 42, "|" );
				imp.say( 43, rs.getString( "ContCli" ) != null ? rs.getString( "ContCli" ).substring( 0, 30 ) : "" );
				imp.say( 74, "|" );
				imp.say( 76, ( rs.getString( "EndCli" ) != null ? rs.getString( "EndCli" ).substring( 0, 30 ) : "" ).trim() + ", " + ( rs.getString( "NumCli" ) != null ? rs.getString( "NumCli" ) : "" ) );
				imp.say( 117, "|" );
				imp.say( 119, ( rs.getString( "FoneCli" ) != null ? ( ( rs.getString( "DDDCli" ) != null ? "(" + rs.getString( "DDDCli" ) + ")" : "" ) + Funcoes.setMascara( rs.getString( "FoneCli" ).trim(), "####-####" ) ) : "" ).trim() );
				imp.say( 135, "|" );
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" );
				imp.say( 10, "|" );
				imp.say( 11, rs.getString( "NomeCli" ) != null ? rs.getString( "NomeCli" ).substring( 0, 30 ) : "" );
				imp.say( 42, "|" );
				imp.say( 43, rs.getString( "EmailCli" ) != null ? rs.getString( "EmailCli" ).substring( 0, 30 ) : "" );
				imp.say( 74, "|" );
				imp.say( 76, ( rs.getString( "SiteCli" ) != null ? rs.getString( "SiteCli" ).substring( 0, 30 ) : "" ) );
				imp.say( 117, "|" );
				imp.say( 119, ( rs.getString( "FaxCli" ) != null ? ( ( rs.getString( "DDDCli" ) != null ? "(" + rs.getString( "DDDCli" ) + ")" : "" ) + Funcoes.setMascara( rs.getString( "FaxCli" ).trim(), "####-####" ) ) : "" ).trim() );
				imp.say( 135, "|" );

				if ( !sObs.equals( "" ) ) {

					vObs = Funcoes.quebraLinha( Funcoes.stringToVector( rs.getString( "ObsCli" ) ), 101 );

					for ( int i = 0; i < vObs.size(); i++ ) {

						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|" );
						imp.say( 14, vObs.elementAt( i ).toString() );
						imp.say( 117, "|" );
						imp.say( 135, "|" );

						if ( imp.pRow() >= linPag ) {
							imp.incPags();
							imp.eject();
						}

					}

				}

				And.atualiza( iContaReg );
				iContaReg++;

			}

			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "+" + linhaFina + "+" );

			imp.eject();
			imp.fechaGravacao();

			con.commit();

			And.dispose();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela de contatos!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	private void imprimeResumido3( final ImprimeOS imp, final String[] sValores ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();
		String sObs = "";
		StringBuffer sWhere = new StringBuffer();
		String sFrom = "";
		String linhaFina = StringFunctions.replicate( "-", 133 );
		Vector<String> vObs = null;
		FAndamento And = null;
		int linPag = imp.verifLinPag() - 1;
		int iContaReg = 0;

		if ( sValores[ 1 ].equals( "S" ) ) {
			sObs = ",C1.OBSCLI ";
		}

		if ( sValores[ 2 ].trim().length() > 0 ) {
			sWhere.append( " AND C1.NOMECLI >='" + sValores[ 2 ] + "'" );
			imp.addSubTitulo( "NOMES MAIORES QUE " + sValores[ 2 ].trim() );
		}
		if ( sValores[ 3 ].trim().length() > 0 ) {
			sWhere.append( " AND C1.NOMECLI <='" + sValores[ 3 ] + "'" );
			imp.addSubTitulo( "NOMES MENORES QUE " + sValores[ 3 ].trim() );
		}
		if ( sValores[ 4 ].equals( "N" ) ) {
			sWhere.append( " AND C1.PESSOACLI <> 'F'" );
			imp.addSubTitulo( "PESSOAS JURIDICAS" );
		}
		if ( sValores[ 5 ].length() > 0 ) {
			if ( "A".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.CIDCLI=" );
			}
			else if ( "E".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.CIDENT=" );
			}
			else if ( "C".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.CIDCOB=" );
			}
			sWhere.append( "'" + sValores[ 5 ] + "'" );
			imp.addSubTitulo( "CIDADE = " + sValores[ 5 ].trim() );
		}
		if ( sValores[ 6 ].equals( "N" ) ) {
			sWhere.append( " AND C1.PESSOACLI <> 'J'" );
			imp.addSubTitulo( "PESSOAS FISICA" );
		}
		if ( !sValores[ 13 ].trim().equals( "" ) ) {
			sWhere.append( " AND C1.CODVEND =" + sValores[ 13 ] );
			imp.addSubTitulo( "REPRESENTANTE = " + sValores[ 13 ] + "-" + sValores[ 14 ] );
		}
		if ( sValores[ 8 ].length() > 0 ) {
			if ( !(Boolean) bPref.get( "SetorVenda" ) ) {
				sFrom = ",VDVENDEDOR V ";
				sWhere.append( " AND C1.CODEMPVD=V.CODEMP AND C1.CODFILIALVD=V.CODFILIAL AND C1.CODVEND=V.CODVEND AND V.CODSETOR = " + sValores[ 8 ] );
				;
			}
			else {
				sWhere.append( " AND C1.CODSETOR = " + sValores[ 8 ] );
			}
			imp.addSubTitulo( "SETOR = " + sValores[ 9 ] );
		}
		if ( sValores[ 10 ].length() > 0 ) {
			sWhere.append( " AND C1.CODTIPOCLI=" + sValores[ 10 ] );
			imp.addSubTitulo( "TIPO DE CLIENTE=" + sValores[ 11 ] );
		}
		if ( sValores[ 15 ].length() > 0 ) {
			sWhere.append( " AND C1.CODCLASCLI=" + sValores[ 15 ] );
			imp.addSubTitulo( "CLASSIFICACAO DO CLIENTE = " + sValores[ 16 ] );
		}
		if ( sValores[ 18 ].length() > 0 ) {
			if ( "A".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.BAIRCLI=" );
			}
			else if ( "E".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.BAIRENT=" );
			}
			else if ( "C".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.BAIRCOB=" );
			}
			sWhere.append( "'" + sValores[ 18 ] + "'" );
			imp.addSubTitulo( "BAIRRO = " + sValores[ 18 ] );
		}

		if ( !sValores[ 19 ].equals( "" ) ) {
			if ( "A".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.UFCLI=" );
			}
			else if ( "E".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.UFENT=" );
			}
			else if ( "C".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.UFCOB=" );
			}
			sWhere.append( "'" + sValores[ 19 ] + "'" );
			imp.addSubTitulo( "ESTADO = " + sValores[ 19 ] );
		}

		if ( sValores[ 20 ].equals( "S" ) ) {
			sWhere.append( " AND C1.ATIVOCLI='S' " );
		}
		if ( sValores[ 21 ].equals( "S" ) ) {
			sWhere.append( " AND C1.ATIVOCLI='N' " );
		}

		try {

			imp.limpaPags();
			imp.montaCab();
			imp.setTitulo( "Relatório de Clientes" );

			ps = con.prepareStatement( "SELECT COUNT(*) FROM VDCLIENTE C1" + sFrom + " WHERE C1.CODEMP=? AND C1.CODFILIAL=? " + sWhere.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
			rs = ps.executeQuery();
			rs.next();

			And = new FAndamento( "Montando Relatório, Aguarde!", 0, rs.getInt( 1 ) - 1 );
			And.setVisible( true );

			con.commit();

			sSQL.append( "SELECT C1.CODCLI,C1.NOMECLI," );
			if ( "A".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sSQL.append( "C1.ENDCLI,C1.NUMCLI,C1.BAIRCLI,C1.CIDCLI,C1.COMPLCLI,C1.UFCLI," );
			}
			else if ( "E".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sSQL.append( "C1.ENDENT AS ENDCLI,C1.NUMENT AS NUMCLI,C1.BAIRENT AS BAIRCLI," );
				sSQL.append( "C1.CIDENT AS CIDCLI,C1.COMPLENT AS COMPLCLI,C1.UFENT AS UFCLI," );
			}
			else if ( "C".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sSQL.append( "C1.ENDCOB AS ENDCLI,C1.NUMCOB AS NUMCLI,C1.BAIRCOB AS BAIRCLI," );
				sSQL.append( "C1.CIDCOB AS CIDCLI,C1.COMPLCOB AS COMPLCLI,C1.UFCOB AS UFCLI," );
			}
			sSQL.append( "C1.FONECLI,C1.DDDCLI " );
			sSQL.append( sObs );
			sSQL.append( "FROM VDCLIENTE C1 " );
			sSQL.append( sFrom );
			sSQL.append( "WHERE C1.CODEMP=? AND C1.CODFILIAL=? " );
			sSQL.append( sWhere );
			sSQL.append( " ORDER BY " + sValores[ 0 ] );

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
			rs = ps.executeQuery();

			while ( rs.next() ) {

				if ( imp.pRow() >= linPag ) {

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "+" + linhaFina + "+" );
					imp.incPags();
					imp.eject();

				}

				if ( imp.pRow() == 0 ) {

					imp.impCab( 136, true );
					imp.say( 0, "|" + linhaFina + "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|  Código" );
					imp.say( 9, "|  Nome fantazia:" );
					imp.say( 40, "|  Endereço:" );
					imp.say( 76, "|  Bairro:" );
					imp.say( 96, "|  Cidade:" );
					imp.say( 116, "|UF" );
					imp.say( 119, "|     Fone" );
					imp.say( 135, "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + linhaFina + "|" );

				}

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" + Funcoes.alinhaDir( rs.getString( "CODCLI" ), 8 ) );
				imp.say( 9, "|" + ( rs.getString( "NOMECLI" ) != null ? Funcoes.copy( rs.getString( "NOMECLI" ), 29 ) : "" ) );
				imp.say( 40, "|" + ( rs.getString( "ENDCLI" ) != null ? Funcoes.copy( rs.getString( "ENDCLI" ), 30 ) : "" ) );
				imp.say( 71, rs.getString( "NUMCLI" ) != null ? Funcoes.alinhaDir( rs.getString( "NUMCLI" ), 5 ) : "" );
				imp.say( 76, "|" + ( rs.getString( "BAIRCLI" ) != null ? Funcoes.copy( rs.getString( "BAIRCLI" ), 19 ) : "" ) );
				imp.say( 96, "|" + ( rs.getString( "CIDCLI" ) != null ? Funcoes.copy( rs.getString( "CIDCLI" ), 19 ) : "" ) );
				imp.say( 116, "|" + ( rs.getString( "UFCLI" ) != null ? Funcoes.copy( rs.getString( "UFCLI" ), 2 ) : "" ) );
				imp.say( 119, "|" + ( rs.getString( "DDDCLI" ) != null ? ( "(" + rs.getString( "DDDCLI" ) + ")" ) : "" ) + ( rs.getString( "FONECLI" ) != null ? Funcoes.setMascara( rs.getString( "FONECLI" ).trim(), "####-####" ) : "" ).trim() );
				imp.say( 135, "|" );

				if ( "S".equals( sValores[ 1 ] ) ) {

					vObs = Funcoes.quebraLinha( Funcoes.stringToVector( rs.getString( "ObsCli" ) ), 100 );

					for ( int i = 0; i < vObs.size(); i++ ) {

						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|      Obs: " + vObs.elementAt( i ).toString() );
						imp.say( 119, "|" );
						imp.say( 135, "|" );

						if ( imp.pRow() >= linPag ) {

							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( 0, "+" + linhaFina + "+" );
							imp.incPags();
							imp.eject();

						}

					}

				}

				And.atualiza( iContaReg );
				iContaReg++;

			}

			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "+" + linhaFina + "+" );

			imp.eject();
			imp.fechaGravacao();

			con.commit();

			And.dispose();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela de contatos!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	private void imprimeCompleto( final ImprimeOS imp, final String[] sValores ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();
		String sObs = "";
		StringBuffer sWhere = new StringBuffer();
		String sFrom = "";
		String linhaFina = StringFunctions.replicate( "-", 133 );
		FAndamento And = null;
		int linPag = imp.verifLinPag() - 1;
		int iContaReg = 0;

		if ( sValores[ 1 ].equals( "S" ) ) {
			sObs = ",C1.OBSCLI ";
		}

		if ( sValores[ 2 ].trim().length() > 0 ) {
			sWhere.append( " AND C1.RAZCLI >='" + sValores[ 2 ] + "'" );
			imp.addSubTitulo( "RAZAO MAIORES QUE " + sValores[ 2 ].trim() );
		}
		if ( sValores[ 3 ].trim().length() > 0 ) {
			sWhere.append( " AND C1.RAZCLI <='" + sValores[ 3 ] + "'" );
			imp.addSubTitulo( "RAZAO MENORES QUE " + sValores[ 3 ].trim() );
		}
		if ( sValores[ 4 ].equals( "N" ) ) {
			sWhere.append( " AND C1.PESSOACLI <> 'F'" );
			imp.addSubTitulo( "PESSOAS JURIDICAS" );
		}
		if ( sValores[ 5 ].length() > 0 ) {
			if ( "A".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.CIDCLI=" );
			}
			else if ( "E".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.CIDENT=" );
			}
			else if ( "C".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.CIDCOB=" );
			}
			sWhere.append( "'" + sValores[ 5 ] + "'" );
			imp.addSubTitulo( "CIDADE = " + sValores[ 5 ].trim() );
		}
		if ( sValores[ 6 ].equals( "N" ) ) {
			sWhere.append( " AND C1.PESSOACLI <> 'J'" );
			imp.addSubTitulo( "PESSOAS FISICA" );
		}
		if ( !sValores[ 13 ].trim().equals( "" ) ) {
			sWhere.append( " AND C1.CODVEND =" + sValores[ 13 ] );
			imp.addSubTitulo( "REPRES. = " + sValores[ 13 ] + "-" + sValores[ 14 ] );
		}
		if ( sValores[ 8 ].length() > 0 ) {
			if ( !(Boolean) bPref.get( "SetorVenda" ) ) {
				sFrom = ",VDVENDEDOR V ";
				sWhere.append( " AND C1.CODEMPVD=V.CODEMP AND C1.CODFILIALVD=V.CODFILIAL AND C1.CODVEND=V.CODVEND AND V.CODSETOR = " + sValores[ 8 ] );
			}
			else {
				sWhere.append( " AND C1.CODSETOR = " + sValores[ 8 ] );
			}
			imp.addSubTitulo( "SETOR = " + sValores[ 9 ] );
		}
		if ( sValores[ 10 ].length() > 0 ) {
			sWhere.append( " AND C1.CODTIPOCLI=" + sValores[ 10 ] );
			imp.addSubTitulo( "TIPO DE CLIENTE=" + sValores[ 11 ] );
		}
		if ( sValores[ 15 ].length() > 0 ) {
			sWhere.append( " AND C1.CODCLASCLI=" + sValores[ 15 ] );
			imp.addSubTitulo( "CLASSIFICACAO DO CLIENTE = " + sValores[ 16 ] );
		}
		if ( sValores[ 18 ].length() > 0 ) {
			if ( "A".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.BAIRCLI=" );
			}
			else if ( "E".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.BAIRENT=" );
			}
			else if ( "C".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.BAIRCOB=" );
			}
			sWhere.append( "'" + sValores[ 18 ] + "'" );
			imp.addSubTitulo( "BAIRRO = " + sValores[ 18 ] );
		}

		if ( !sValores[ 19 ].equals( "" ) ) {
			if ( "A".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.UFCLI=" );
			}
			else if ( "E".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.UFENT=" );
			}
			else if ( "C".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.UFCOB=" );
			}
			sWhere.append( "'" + sValores[ 19 ] + "'" );
			imp.addSubTitulo( "ESTADO = " + sValores[ 19 ] );
		}

		if ( sValores[ 20 ].equals( "S" ) ) {
			sWhere.append( " AND C1.ATIVOCLI='S' " );
		}
		if ( sValores[ 21 ].equals( "S" ) ) {
			sWhere.append( " AND C1.ATIVOCLI='N' " );
		}

		try {

			imp.limpaPags();
			imp.montaCab();
			imp.setTitulo( "Relatório de Clientes" );

			ps = con.prepareStatement( "SELECT COUNT(*) FROM VDCLIENTE C1" + sFrom + " WHERE C1.CODEMP=? AND C1.CODFILIAL=? " + sWhere.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
			rs = ps.executeQuery();
			rs.next();

			And = new FAndamento( "Montando Relatório, Aguarde!", 0, rs.getInt( 1 ) - 1 );
			And.setVisible( true );

			con.commit();

			sSQL.append( "SELECT C1.CODCLI,C1.RAZCLI,C1.PESSOACLI,C1.NOMECLI,C1.CONTCLI," );
			sSQL.append( "C1.CNPJCLI,C1.INSCCLI,C1.CPFCLI," );
			sSQL.append( "C1.RGCLI,C1.FONECLI,C1.DDDCLI,C1.FAXCLI,C1.EMAILCLI,C1.CODPESQ, " );
			if ( "A".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sSQL.append( "C1.ENDCLI,C1.NUMCLI,C1.BAIRCLI,C1.CIDCLI,C1.COMPLCLI,C1.UFCLI,C1.CEPCLI " );
			}
			else if ( "E".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sSQL.append( "C1.ENDENT AS ENDCLI,C1.NUMENT AS NUMCLI,C1.BAIRENT AS BAIRCLI," );
				sSQL.append( "C1.CIDENT AS CIDCLI,C1.COMPLENT AS COMPLCLI,C1.UFENT AS UFCLI,C1.CEPENT AS CEPCLI " );
			}
			else if ( "C".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sSQL.append( "C1.ENDCOB AS ENDCLI,C1.NUMCOB AS NUMCLI,C1.BAIRCOB AS BAIRCLI," );
				sSQL.append( "C1.CIDCOB AS CIDCLI,C1.COMPLCOB AS COMPLCLI,C1.UFCOB AS UFCLI,C1.CEPCOB AS CEPCLI " );
			}
			sSQL.append( sObs );
			sSQL.append( "FROM VDCLIENTE C1 " );
			sSQL.append( sFrom );
			sSQL.append( "WHERE C1.CODEMP=? AND C1.CODFILIAL=? " );
			sSQL.append( sWhere );
			sSQL.append( " ORDER BY " + sValores[ 0 ] );

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
			rs = ps.executeQuery();

			while ( rs.next() ) {

				if ( imp.pRow() == 0 ) {
					imp.impCab( 136, true );
					imp.pulaLinha( 0, imp.comprimido() );
					imp.say( 0, "|" );
					imp.say( 135, "|" );
				}

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" + linhaFina + "|" );
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "| Código:" );
				imp.say( 10, rs.getString( "CodCli" ) );
				imp.say( 20, "Razão:" );
				imp.say( 27, rs.getString( "RazCli" ) != null ? rs.getString( "RazCli" ).substring( 0, 30 ) : "" );

				if ( rs.getString( "CodPesq" ) != null && !rs.getString( "CodPesq" ).equals( rs.getString( "CodCLi" ) ) ) {
					imp.say( 57, "Cod.Matriz : " + rs.getString( "CodPesq" ) );
				}

				imp.say( 127, "Tipo:" );
				imp.say( 133, rs.getString( "PessoaCli" ) );
				imp.say( 135, "|" );
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "| Nome:" );
				imp.say( 8, rs.getString( "NomeCli" ) );
				imp.say( 60, "Contato:" );
				imp.say( 70, rs.getString( "ContCli" ) );
				imp.say( 135, "|" );
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "| Endereço:" );
				imp.say( 12, rs.getString( "EndCli" ) );
				imp.say( 63, "N.:" );
				imp.say( 67, String.valueOf( rs.getInt( "NumCli" ) ) );
				imp.say( 76, "Compl.:" );
				imp.say( 85, rs.getString( "ComplCli" ) != null ? rs.getString( "ComplCli" ).trim() : "" );
				imp.say( 94, "Bairro:" );
				imp.say( 103, rs.getString( "BairCli" ) != null ? rs.getString( "BairCli" ).trim() : "" );
				imp.say( 135, "|" );
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "| Cidade:" );
				imp.say( 10, rs.getString( "CidCli" ) );
				imp.say( 88, "UF:" );
				imp.say( 93, rs.getString( "UfCli" ) );
				imp.say( 120, "CEP:" );
				imp.say( 125, rs.getString( "CepCli" ) != null ? Funcoes.setMascara( rs.getString( "CepCli" ), "#####-###" ) : "" );
				imp.say( 135, "|" );
				imp.pulaLinha( 1, imp.comprimido() );

				if ( ( rs.getString( "CnpjCli" ) ) != null && ( rs.getString( "InscCli" ) != null ) ) {
					imp.say( 0, "| CNPJ:" );
					imp.say( 7, Funcoes.setMascara( rs.getString( "CnpjCli" ), "##.###.###/####-##" ) );
					imp.say( 50, "IE:" );
					if ( !rs.getString( "InscCli" ).trim().toUpperCase().equals( "ISENTO" ) && rs.getString( "UFCli" ) != null ) {
						// Funcoes.validaIE( rs.getString( "InscCli" ), rs.getString( "UFCli" ) );
						imp.say( 55, Funcoes.formataIE( rs.getString( "InscCli" ), rs.getString( "UFCli" ) ) );
					}
				}
				else {
					imp.say( 0, "| CPF:" );
					imp.say( 6, Funcoes.setMascara( rs.getString( "CPFCli" ), "###.###.###-##" ) );
					imp.say( 50, "RG:" );
					imp.say( 55, rs.getString( "RgCli" ) );
				}

				imp.say( 80, "Tel:" );
				imp.say( 86, ( rs.getString( "DDDCli" ) != null ? rs.getString( "DDDCli" ) + "-" : "" ) + ( rs.getString( "FoneCli" ) != null ? Funcoes.setMascara( rs.getString( "FoneCli" ).trim(), "####-####" ) : "" ).trim() );
				imp.say( 115, "Fax:" );
				imp.say( 122, rs.getString( "FaxCli" ) != null ? Funcoes.setMascara( rs.getString( "FaxCli" ), "####-####" ) : "" );
				imp.say( 135, "|" );
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "| Contato:" );
				imp.say( 12, rs.getString( "ContCli" ) );
				imp.say( 72, "E-mail:" );
				imp.say( 79, rs.getString( "EmailCli" ) );
				imp.say( 135, "|" );

				if ( sObs.length() > 0 ) {
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|Obs:" );
					imp.say( 6, rs.getString( "ObsCli" ) );
					imp.say( 135, "|" );
				}

				if ( imp.pRow() >= linPag ) {
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "+" + linhaFina + "+" );
					imp.incPags();
					imp.eject();
				}

				And.atualiza( iContaReg++ );

			}

			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "+" + linhaFina + "+" );

			imp.eject();
			imp.fechaGravacao();

			con.commit();

			And.dispose();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela de clientes!" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}

	}

	private void imprimeAlinhaFilial( final ImprimeOS imp, final String[] sValores ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();
		StringBuffer sWhere = new StringBuffer();
		StringBuffer sWhere2 = new StringBuffer();
		String sCodpesq = "";
		String sCodpesqant = "";
		String sOrdem = "";
		String sFrom = "";
		String linhaFina = StringFunctions.replicate( "-", 133 );
		FAndamento And = null;
		int linPag = imp.verifLinPag() - 1;
		int iContaReg = 0;

		if ( sValores[ 12 ].equals( "C" ) ) {
			sOrdem = "1,3,4";
		}
		else {
			sOrdem = "2,3,5";
		}

		if ( sValores[ 2 ].trim().length() > 0 ) {
			// sWhere.append( " AND C1.RAZCLI >='" + sValores[ 2 ] + "'" );
			// sWhere2.append( " AND C2.RAZCLI >='" + sValores[ 2 ] + "'" );
			// imp.addSubTitulo( "RAZAO MAIORES QUE " + sValores[ 2 ].trim() );

			sWhere.append( " AND C1.CODCLI >='" + sValores[ 2 ] + "'" );
			sWhere2.append( " AND C2.CODCLI >='" + sValores[ 2 ] + "'" );
			imp.addSubTitulo( "CODIGOS CLIENTES MAIORES QUE " + sValores[ 2 ].trim() );
		}
		if ( sValores[ 3 ].trim().length() > 0 ) {
			// sWhere.append( " AND C1.RAZCLI <='" + sValores[ 3 ] + "'" );
			// sWhere2.append( " AND C2.RAZCLI <='" + sValores[ 3 ] + "'" );
			// imp.addSubTitulo( "RAZAO MENORES QUE " + sValores[ 3 ].trim() );

			sWhere.append( " AND C1.CODCLI <='" + sValores[ 3 ] + "'" );
			sWhere2.append( " AND C2.CODCLI <='" + sValores[ 3 ] + "'" );
			imp.addSubTitulo( "CODIGOS CLIENTES MENORES QUE " + sValores[ 3 ].trim() );
		}
		if ( sValores[ 4 ].equals( "N" ) ) {
			sWhere.append( " AND C1.PESSOACLI <> 'F'" );
			sWhere2.append( " AND C2.PESSOACLI <> 'F'" );
			imp.addSubTitulo( "PESSOAS JURIDICAS" );
		}
		if ( sValores[ 5 ].length() > 0 ) {
			sWhere.append( " AND C1.CIDCLI ='" + sValores[ 5 ] + "'" );
			sWhere2.append( " AND C2.CIDCLI ='" + sValores[ 5 ] + "'" );
			imp.addSubTitulo( "CIDADE : " + sValores[ 5 ].trim() );
		}
		if ( sValores[ 6 ].equals( "N" ) ) {
			sWhere.append( " AND C1.PESSOACLI <> 'J'" );
			sWhere2.append( " AND C2.PESSOACLI <> 'J'" );
			imp.addSubTitulo( "PESSOAS FISICA" );
		}
		if ( !sValores[ 13 ].trim().equals( "" ) ) {
			sWhere.append( " AND C1.CODVEND =" + sValores[ 13 ] );
			sWhere2.append( " AND C2.CODVEND =" + sValores[ 13 ] );
			imp.addSubTitulo( "REPRES. : " + sValores[ 13 ] + "-" + sValores[ 14 ] );
		}
		if ( sValores[ 8 ].length() > 0 ) {
			if ( !(Boolean) bPref.get( "SetorVenda" ) ) {
				sFrom = ",VDVENDEDOR V ";
				sWhere.append( " AND C1.CODEMPVD=V.CODEMP AND C1.CODFILIALVD=V.CODFILIAL AND C1.CODVEND=V.CODVEND AND V.CODSETOR = " + sValores[ 8 ] );
				sWhere2.append( " AND C2.CODEMPVD=V.CODEMP AND C2.CODFILIALVD=V.CODFILIAL AND C2.CODVEND=V.CODVEND AND V.CODSETOR = " + sValores[ 8 ] );
			}
			else {
				sWhere.append( " AND C1.CODSETOR = " + sValores[ 8 ] );
				sWhere2.append( " AND C2.CODSETOR = " + sValores[ 8 ] );
			}
			imp.addSubTitulo( "SETOR : " + sValores[ 9 ] );
		}
		if ( sValores[ 10 ].length() > 0 ) {
			sWhere.append( " AND C1.CODTIPOCLI=" + sValores[ 10 ] );
			sWhere2.append( " AND C2.CODTIPOCLI=" + sValores[ 10 ] );
			imp.addSubTitulo( "TIPO DE CLIENTE=" + sValores[ 11 ] );
		}
		if ( sValores[ 15 ].length() > 0 ) {
			sWhere.append( " AND C1.CODCLASCLI=" + sValores[ 15 ] );
			sWhere2.append( " AND C2.CODCLASCLI=" + sValores[ 15 ] );
			imp.addSubTitulo( "CLASSIFICACAO DO CLIENTE = " + sValores[ 16 ] );
		}
		if ( sValores[ 18 ].length() > 0 ) {
			sWhere.append( " AND C1.BAIRCLI='" + sValores[ 18 ] + "'" );
			sWhere2.append( " AND C2.BAIRCLI='" + sValores[ 18 ] + "'" );
			imp.addSubTitulo( "BAIRRO : " + sValores[ 18 ] );
		}

		if ( !sValores[ 19 ].equals( "" ) ) {
			if ( "A".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.UFCLI=" );
				sWhere2.append( " AND C2.UFCLI=" );
			}
			else if ( "E".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.UFENT=" );
				sWhere2.append( " AND C2.UFENT=" );
			}
			else if ( "C".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sWhere.append( " AND C1.UFCOB=" );
				sWhere2.append( " AND C2.UFCOB=" );
			}
			sWhere.append( "'" + sValores[ 19 ] + "'" );
			sWhere2.append( "'" + sValores[ 19 ] + "'" );
			imp.addSubTitulo( "ESTADO = " + sValores[ 19 ] );
		}

		try {

			imp.limpaPags();
			imp.montaCab();
			imp.setTitulo( "Relatório de Clientes" );

			ps = con.prepareStatement( "SELECT COUNT(*) FROM VDCLIENTE C1" + sFrom + " WHERE C1.CODEMP=? AND C1.CODFILIAL=? " + sWhere.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
			rs = ps.executeQuery();
			rs.next();

			And = new FAndamento( "Montando Relatório, Aguarde!", 0, rs.getInt( 1 ) - 1 );
			And.setVisible( true );

			con.commit();

			sSQL.append( "SELECT C1.CODPESQ,C1.RAZCLI RAZMATRIZ,'A' TIPO,C1.CODCLI,C1.RAZCLI,C1.DDDCLI,C1.FONECLI," );
			if ( "A".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sSQL.append( "C1.ENDCLI,C1.CIDCLI " );
			}
			else if ( "E".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sSQL.append( "C1.ENDENT AS ENDCLI,C1.CIDENT AS CIDCLI " );
			}
			else if ( "C".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sSQL.append( "C1.ENDCOB AS ENDCLI,C1.CIDCOB AS CIDCLI " );
			}
			sSQL.append( "FROM VDCLIENTE C1" );
			sSQL.append( sFrom );
			sSQL.append( " WHERE C1.CODCLI=C1.CODPESQ " );
			sSQL.append( " AND C1.CODEMP=? AND C1.CODFILIAL=? " );
			sSQL.append( sWhere );
			sSQL.append( " UNION ALL SELECT C2.CODPESQ," );
			sSQL.append( "(SELECT C3.RAZCLI FROM VDCLIENTE C3 WHERE C3.CODCLI=C2.CODPESQ) AS RAZMATRIZ," );
			sSQL.append( "'B' TIPO,C2.CODCLI,C2.RAZCLI,C2.DDDCLI,C2.FONECLI," );
			if ( "A".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sSQL.append( "C2.ENDCLI,C2.CIDCLI " );
			}
			else if ( "E".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sSQL.append( "C2.ENDENT AS ENDCLI,C2.CIDENT AS CIDCLI " );
			}
			else if ( "C".equals( sValores[ RET_DLRCLIENTE.END.ordinal() ] ) ) {
				sSQL.append( "C2.ENDCOB AS ENDCLI,C2.CIDCOB AS CIDCLI " );
			}
			sSQL.append( "FROM VDCLIENTE C2" );
			sSQL.append( sFrom );
			sSQL.append( " WHERE NOT C2.CODCLI=C2.CODPESQ AND " );
			sSQL.append( "C2.CODEMP=? AND C2.CODFILIAL=? " );
			sSQL.append( sWhere2 );
			sSQL.append( " ORDER BY " + sOrdem );

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
			rs = ps.executeQuery();

			while ( rs.next() ) {

				sCodpesq = String.valueOf( rs.getInt( 1 ) );

				if ( sCodpesqant.equals( "" ) ) {
					sCodpesqant = sCodpesq;
				}

				if ( imp.pRow() >= linPag ) {
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "+" + linhaFina + "+" );
					imp.incPags();
					imp.eject();
				}

				if ( imp.pRow() == 0 ) {

					imp.impCab( 136, true );
					imp.pulaLinha( 0, imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, "+" + linhaFina + "+" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "| Codigo" );
					imp.say( 12, "| Nome" );
					imp.say( 58, "| Endereco" );
					imp.say( 101, "| Cidade" );
					imp.say( 115, "|  Telefone" );
					imp.say( 135, "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + linhaFina + "|" );

				}
				else {
					if ( !sCodpesq.equals( sCodpesqant ) ) {
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|" + linhaFina + "|" );
					}
				}

				if ( rs.getString( 3 ).equals( "A" ) ) {

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" );
					imp.say( 2, rs.getString( "CodCli" ) );
					imp.say( 12, "|" );
					imp.say( 14, rs.getString( "RazCli" ) != null ? rs.getString( "RazCli" ).substring( 0, 30 ) : "" );
					imp.say( 47, "( M )" );
					imp.say( 58, "|" );
					imp.say( 59, rs.getString( "EndCli" ) != null ? rs.getString( "EndCli" ).substring( 0, 30 ) : "" );
					imp.say( 101, "|" );
					imp.say( 103, rs.getString( "CidCli" ) != null ? rs.getString( "CidCli" ).substring( 0, 12 ) : "" );
					imp.say( 115, "|" );
					imp.say( 120, ( rs.getString( "DDDCli" ) != null ? "(" + rs.getString( "DDDCli" ) + ")" : "" ) + ( rs.getString( "FoneCli" ) != null ? Funcoes.setMascara( rs.getString( "FoneCli" ).trim(), "####-####" ) : "" ).trim() );
					imp.say( 135, "|" );

				}
				else if ( !rs.getString( 1 ).equals( rs.getString( 4 ) ) ) {

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" );
					imp.say( 2, rs.getString( "CodCli" ) );
					imp.say( 12, "|" );
					imp.say( 15, rs.getString( "RazCli" ) != null ? rs.getString( "RazCli" ).substring( 0, 30 ) : "" );
					imp.say( 47, "( F )" );
					imp.say( 58, "|" );
					imp.say( 59, rs.getString( "EndCli" ) != null ? rs.getString( "EndCli" ).substring( 0, 30 ) : "" );
					imp.say( 101, "|" );
					imp.say( 103, rs.getString( "CidCli" ) != null ? rs.getString( "CidCli" ).substring( 0, 12 ) : "" );
					imp.say( 115, "|" );
					imp.say( 120, ( rs.getString( "DDDCli" ) != null ? "(" + rs.getString( "DDDCli" ) + ")" : "" ) + ( rs.getString( "FoneCli" ) != null ? Funcoes.setMascara( rs.getString( "FoneCli" ), "####-####" ) : "" ).trim() );
					imp.say( 135, "|" );

				}

				And.atualiza( iContaReg++ );
				sCodpesqant = sCodpesq;

			}

			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "+" + linhaFina + "+" );
			imp.eject();
			imp.fechaGravacao();

			con.commit();

			And.dispose();

		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro consulta tabela de clientes!\n" + err.getMessage(), true, con, err );
		}

	}

	/*private void novoHist() {

		PreparedStatement ps = null;
		int iCod = 0;

		try {

			Object oRets[];

			if ( txtCodCli.getVlrInteger().intValue() == 0 ) {
				Funcoes.mensagemInforma( this, "Não ha nenhum cliente selecionado!" );
				txtCodCli.requestFocus();
				return;
			}
			else {
				iCod = txtCodCli.getVlrInteger().intValue();
			}

			DLNovoHist dl = new DLNovoHist( iCod, 1, this );
			dl.setConexao( con );
			dl.setVisible( true );

			if ( dl.OK ) {

				oRets = dl.getValores();

				try {
					StringBuilder sql = new StringBuilder( "EXECUTE PROCEDURE TKSETHISTSP(0,?,?,?,?,?,?,?,?,?,?,?)" );
					ps = con.prepareStatement( sql.toString() );
					ps.setInt( 1, Aplicativo.iCodEmp );
					ps.setNull( 2, Types.INTEGER );
					ps.setNull( 3, Types.INTEGER );
					ps.setInt( 4, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
					ps.setInt( 5, txtCodCli.getVlrInteger().intValue() );
					ps.setString( 6, (String) oRets[ 0 ] );// Descrição do historico
					ps.setInt( 7, ListaCampos.getMasterFilial( "ATATENDENTE" ) );// Filial do atendete
					ps.setString( 8, (String) oRets[ 1 ] );// codígo atendente
					ps.setString( 9, (String) oRets[ 2 ] );// status do historico
					ps.setDate( 10, (Date) oRets[ 4 ] );// data do historico
					ps.setString( 11, (String) oRets[ 3 ] );// tipo do historico
					ps.execute();
					ps.close();

					con.commit();

					if ( oRets[ 5 ] != null ) { // CODAGD
						Object[] agente = getAgente();
						StringBuilder agenda_sql = new StringBuilder( "EXECUTE PROCEDURE SGSETAGENDASP(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" );
						ps = con.prepareStatement( agenda_sql.toString() );

						ps.setInt( 1, (Integer) oRets[ 5 ] );// CODAGD
						ps.setInt( 2, (Integer) oRets[ 6 ] );// código da empresa
						ps.setDate( 3, Funcoes.strDateToSqlDate( (String) oRets[ 7 ] ) );// data inicial
						ps.setString( 4, oRets[ 8 ] + ":00" );// hora inicial
						ps.setDate( 5, Funcoes.strDateToSqlDate( (String) oRets[ 9 ] ) );// data final
						ps.setString( 6, oRets[ 10 ] + ":00" );// hora final
						ps.setString( 7, (String) oRets[ 11 ] );// assunto
						ps.setString( 8, (String) oRets[ 12 ] );// descrição da atividade
						ps.setInt( 9, ( (Integer) oRets[ 13 ] ).intValue() );// filial do tipo de agendamento
						ps.setString( 10, (String) oRets[ 14 ] );// tipo do agendamento
						ps.setString( 11, (String) oRets[ 15 ] );// prioridade
						ps.setInt( 12, (Integer) oRets[ 16 ] );// código do agente
						ps.setString( 13, (String) oRets[ 17 ] );// tipo do agente
						ps.setInt( 14, ( (Integer) oRets[ 18 ] ).intValue() );// filial do agente emitente
						ps.setInt( 15, ( (Integer) oRets[ 19 ] ).intValue() );// código do agente emitente
						ps.setString( 16, (String) oRets[ 20 ] );// tipo do agente emitente
						ps.setString( 17, (String) oRets[ 21 ] );// controle de acesso
						ps.setString( 18, (String) oRets[ 22 ] );// status
						ps.setString( 19, (String) oRets[ 23 ] );// Motivo / resolução
						if ( (Integer) oRets[ 24 ] != null ) {
							ps.setInt( 20, (Integer) oRets[ 24 ] );// Código Agendamento( repetitivo )
						}
						else {
							ps.setNull( 20, Types.INTEGER );
						}
						ps.setString( 21, (String) oRets[ 25 ] ); // Todo o dia?

						ps.execute();
						ps.close();

						con.commit();
					}
				} catch ( Exception err ) {
					Funcoes.mensagemErro( this, "Erro ao salvar o histórico!\n" + err.getMessage(), true, con, err );
					err.printStackTrace();
				}
				dl.dispose();
			}
			carregaTabHist();
		} finally {
			ps = null;
		}
	}*/

	private void novaObs() {

		String sSql = null;
		int iCodCli = 0;
		PreparedStatement ps = null;
		DLInputText dl = null;
		iCodCli = txtCodCli.getVlrInteger().intValue();
		java.util.Date dtHoje = null;

		if ( iCodCli != 0 ) {
			try {
				dl = new DLInputText( this, "Observação", false );
				dl.setTexto( "" );
				dl.setVisible( true );

				if ( ( dl.OK ) && ( !dl.getTexto().trim().equals( "" ) ) ) {

					try {
						dtHoje = new java.util.Date();
						sSql = "INSERT INTO VDOBSCLI (SEQOBSCLI, CODEMP, CODFILIAL, CODCLI, DTOBSCLI, HOBSCLI, TXTOBSCLI) " + "VALUES ((COALESCE((SELECT MAX(OC.SEQOBSCLI) " + "FROM VDOBSCLI OC " + "WHERE OC.CODEMP=? AND OC.CODFILIAL=? AND OC.CODCLI=?)+1,1 ))," + "?,?,?,?,?,?)";
						ps = con.prepareStatement( sSql );
						ps.setInt( 1, Aplicativo.iCodEmp );
						ps.setInt( 2, ListaCampos.getMasterFilial( "VDOBSCLI" ) );
						ps.setInt( 3, iCodCli );
						ps.setInt( 4, Aplicativo.iCodEmp );
						ps.setInt( 5, ListaCampos.getMasterFilial( "VDOBSCLI" ) );
						ps.setInt( 6, iCodCli );
						ps.setDate( 7, new Date( dtHoje.getTime() ) );
						ps.setTime( 8, new Time( dtHoje.getTime() ) );
						ps.setString( 9, dl.getTexto() );
						ps.executeUpdate();
						ps.close();
						con.commit();
					} catch ( SQLException err ) {
						err.printStackTrace();
						Funcoes.mensagemErro( this, "Não foi possível inserir a observação.\n" + err.getMessage(), true, con, err );
					}
				}
				carregaTabelaObs();
			} finally {
				ps = null;
				sSql = null;
				dl = null;
				dtHoje = null;
			}
		}
	}

	public void setConveniado( FConveniado telaConv ) {

		this.telaConv = telaConv;
	}

	public void setVlrConveniado( String sRazcli, String sNomecli, String sEndcli, Integer iNumcli, String sComplcli, String sBaircli, String sCidcli, String sCepcli, String sUFcli, String sRgcli, String sCpfcli, String sFonecli, String sFaxcli, String sEmailcli, Integer iCodTipoCli,
			Integer iCodClasCli ) {

		rgPessoa.setVlrString( "F" );
		txtRazCli.setVlrString( sRazcli );
		txtNomeCli.setVlrString( sNomecli );
		txtEndCli.setVlrString( sEndcli );
		txtNumCli.setVlrInteger( iNumcli );
		txtComplCli.setVlrString( sComplcli );
		txtBairCli.setVlrString( sBaircli );
		txtCidCli.setVlrString( sCidcli );
		txtCepCli.setVlrString( sCepcli );
		txtUFCli.setVlrString( sUFcli );
		txtRgCli.setVlrString( sRgcli );
		txtCpfCli.setVlrString( sCpfcli );
		txtFoneCli.setVlrString( sFonecli );
		txtFaxCli.setVlrString( sFaxcli );
		txtEmailCli.setVlrString( sEmailcli );
		if ( iCodTipoCli != null ) {
			txtCodTipoCli.setVlrInteger( iCodTipoCli );
			lcTipoCli.carregaDados();
		}
		if ( iCodClasCli != null ) {
			txtCodClas.setVlrInteger( iCodClasCli );
			lcClas.carregaDados();
		}
	}

	public void copiarCliente() {

		if ( txtCodCli.getVlrInteger().intValue() == 0 || lcCampos.getStatus() != ListaCampos.LCS_SELECT ) {
			Funcoes.mensagemInforma( this, "Selecione um cliente!" );
			return;
		}

		DLCopiaCliente dlCopiaCliente = new DLCopiaCliente( rgPessoa.getVlrString(), txtCnpjCli.getText(), (Boolean) bPref.get( "CLIMESMOCNPJ" ) );
		dlCopiaCliente.setConexao( con );
		dlCopiaCliente.setVisible( true );

		try {

			if ( dlCopiaCliente.OK ) {
				String iDocumento = dlCopiaCliente.getDocumento(); 

				if ("".equals( iDocumento)) {
					iDocumento = null;
				}

				String sSQL = "SELECT ICOD FROM VDCOPIACLIENTE(?, ?, ?, ?)";

				PreparedStatement ps = con.prepareStatement( sSQL );
				ps.setInt( 1, txtCodCli.getVlrInteger() );
				ps.setString( 2, iDocumento );
				ps.setInt( 3, Aplicativo.iCodEmp );
				ps.setInt( 4, Aplicativo.iCodFilial );

				ResultSet rs = ps.executeQuery();

				if ( rs.next() ) {
					if ( Funcoes.mensagemConfirma( this, "Cliente '" + rs.getInt( 1 ) + "' criado com sucesso!\n" + "Gostaria de edita-lo agora?" ) == JOptionPane.OK_OPTION ) {
						txtCodCli.setVlrInteger( new Integer( rs.getInt( 1 ) ) );
						lcCampos.carregaDados();
					}
				}

				rs.close();
				ps.close();

				con.commit();
			}

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao duplicar o cliente!\n" + err.getMessage() );
			err.printStackTrace();
		} finally {
			dlCopiaCliente.dispose();
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
		else if ( evt.getSource() == btImp ) {
			imprimir( TYPE_PRINT.PRINT);
		}
		else if ( evt.getSource() == btAtEntrega ) {
			if ( lcCampos.getStatus() != ListaCampos.LCS_EDIT ) {
				lcCampos.edit();
			}
			txtEndEnt.setVlrString( txtEndCli.getVlrString() );
			txtNumEnt.setVlrString( txtNumCli.getVlrString() );
			txtComplEnt.setVlrString( txtComplCli.getVlrString() );
			txtBairEnt.setVlrString( txtBairCli.getVlrString() );
			txtCepEnt.setVlrString( txtCepCli.getVlrString() );
			txtDDDFoneEnt.setVlrString( txtDDDCli.getVlrString() );
			txtFoneEnt.setVlrString( txtFoneCli.getVlrString() );
			txtDDDFaxEnt.setVlrString( txtDDDFaxCli.getVlrString() );
			txtFaxEnt.setVlrString( txtFaxCli.getVlrString() );
			txtDDDCelEnt.setVlrString( txtDDDCelCli.getVlrString() );
			txtCelEnt.setVlrString( txtCelCli.getVlrString() );
			txtComplEnt.setVlrString( txtComplCli.getVlrString() );

			if ( (Boolean) bPref.get( "USAIBGECLI" ) ) {
				txtCodPaisEnt.setVlrInteger( txtCodPais.getVlrInteger() );
				txtSiglaUFEnt.setVlrString( txtSiglaUF.getVlrString() );
				txtCodMunicEnt.setVlrString( txtCodMunic.getVlrString() );
				lcPaisEnt.carregaDados();
				lcUFEnt.carregaDados();
				lcMunicEnt.carregaDados();
			}
			else {
				txtCidEnt.setVlrString( txtCidCli.getVlrString() );
				txtUFEnt.setVlrString( txtUFCli.getVlrString() );
			}
		}
		else if ( evt.getSource() == btAtCobranca ) {
			if ( lcCampos.getStatus() != ListaCampos.LCS_EDIT ) {
				lcCampos.edit();
			}
			txtEndCob.setVlrString( txtEndCli.getVlrString() );
			txtNumCob.setVlrString( txtNumCli.getVlrString() );
			txtComplCob.setVlrString( txtComplCli.getVlrString() );
			txtBairCob.setVlrString( txtBairCli.getVlrString() );
			txtCepCob.setVlrString( txtCepCli.getVlrString() );
			txtDDDFoneCob.setVlrString( txtDDDCli.getVlrString() );
			txtFoneCob.setVlrString( txtFoneCli.getVlrString() );
			txtDDDFaxCob.setVlrString( txtDDDFaxCli.getVlrString() );
			txtFaxCob.setVlrString( txtFaxCli.getVlrString() );

			if ( (Boolean) bPref.get( "USAIBGECLI" ) ) {

				txtCodPaisCob.setVlrInteger( txtCodPais.getVlrInteger() );
				txtSiglaUFCob.setVlrString( txtSiglaUF.getVlrString() );
				txtCodMunicCob.setVlrString( txtCodMunic.getVlrString() );
				lcPaisCob.carregaDados();
				lcUFCob.carregaDados();
				lcMunicCob.carregaDados();

			}
			else {

				txtCidCob.setVlrString( txtCidCli.getVlrString() );
				txtUFCob.setVlrString( txtUFCli.getVlrString() );
			}
		}
		else if ( evt.getSource() == btExclObs ) {
			exclObs();
		}
		else if ( evt.getSource() == btEditObs ) {
			editObs();
		}
		else if ( evt.getSource() == btNovaObs ) {
			novaObs();
		}
		else if ( evt.getSource() == btGrpCli ) {
			grpCli();
		}
		/*else if ( evt.getSource() == btSetaQtdJan ) {
			geraHistoricos( new Integer( 1 ) );
		}
		else if ( evt.getSource() == btSetaQtdFev ) {
			geraHistoricos( new Integer( 2 ) );
		}
		else if ( evt.getSource() == btSetaQtdMar ) {
			geraHistoricos( new Integer( 3 ) );
		}
		else if ( evt.getSource() == btSetaQtdAbr ) {
			geraHistoricos( new Integer( 4 ) );
		}
		else if ( evt.getSource() == btSetaQtdMai ) {
			geraHistoricos( new Integer( 5 ) );
		}
		else if ( evt.getSource() == btSetaQtdJun ) {
			geraHistoricos( new Integer( 6 ) );
		}
		else if ( evt.getSource() == btSetaQtdJul ) {
			geraHistoricos( new Integer( 7 ) );
		}
		else if ( evt.getSource() == btSetaQtdAgo ) {
			geraHistoricos( new Integer( 8 ) );
		}
		else if ( evt.getSource() == btSetaQtdSet ) {
			geraHistoricos( new Integer( 9 ) );
		}
		else if ( evt.getSource() == btSetaQtdOut ) {
			geraHistoricos( new Integer( 10 ) );
		}
		else if ( evt.getSource() == btSetaQtdNov ) {
			geraHistoricos( new Integer( 11 ) );
		}
		else if ( evt.getSource() == btSetaQtdDez ) {
			geraHistoricos( new Integer( 12 ) );
		}
		else if ( evt.getSource() == btMudaTudo ) {
			geraHistoricos( null );
		}
		else if ( evt.getSource() == btNovoHist ) {
			novoHist();
		}
		else if ( evt.getSource() == btExcluiHist ) {
			excluiHist();
		}*/

		else if ( evt.getSource() == btNovoAtd ) {
			novoAtendimento();
		}
		else if ( evt.getSource() == btExcluirAtd ) {
			excluiAtend();
		}
		else if ( evt.getSource() == btBuscaFor ) {
			buscaFornecedor();
		}
		else if ( evt.getSource() == btMapa ) {
			FMapa tela = new FMapa( false );
			if ( tpn.getSelectedIndex() == 2 ) {
				tela.setEndereco( txtEndCob.getVlrString(), txtNumCob.getVlrInteger(), txtCidCob.getVlrString().equals( "" ) ? txtDescMunCob.getVlrString() : txtCidCob.getVlrString(), txtUFCob.getVlrString().equals( "" ) ? txtSiglaUFCob.getVlrString() : txtUFCob.getVlrString() );
			}
			else if ( tpn.getSelectedIndex() == 3 ) {
				tela.setEndereco( txtEndEnt.getVlrString(), txtNumEnt.getVlrInteger(), txtCidEnt.getVlrString().equals( "" ) ? txtDescMunEnt.getVlrString() : txtCidEnt.getVlrString(), txtUFEnt.getVlrString().equals( "" ) ? txtSiglaUFEnt.getVlrString() : txtUFEnt.getVlrString() );
			}
			else {
				tela.setEndereco( txtEndCli.getVlrString(), txtNumCli.getVlrInteger(), txtCidCli.getVlrString().equals( "" ) ? txtDescMun.getVlrString() : txtCidCli.getVlrString(), txtUFCli.getVlrString().equals( "" ) ? txtSiglaUF.getVlrString() : txtUFCli.getVlrString() );
			}

			tela.setTelaPrim( Aplicativo.telaPrincipal );
			Aplicativo.telaPrincipal.criatela( "Mapa", tela, con );
		}
		else if ( evt.getSource() == btCopiar ) {
			copiarCliente();
		}
		super.actionPerformed( evt );

		if ( evt.getSource() == btFirefox ) {

			if ( !txtSiteCli.getVlrString().equals( "" ) ) {

				sURLBanco = txtSiteCli.getVlrString();
				Funcoes.executeURL( Aplicativo.strOS, Aplicativo.strBrowser, sURLBanco );
			}
			else
				Funcoes.mensagemInforma( this, "Informe o Site do Cliente! " );
		}
		else if ( evt.getSource() == btBuscaEnd ) {
			buscaEndereco();
		}
	}

	private void novoAtendimento() {
		if (txtCodCli.getVlrInteger() <= 0) {
			Funcoes.mensagemInforma( this, "Cliente não selecionado, verifique!!!" );
			return;
		}

		Integer codmodel = (Integer) ((Object[]) bPref.get( "prefAtendo" ))[Atendimento.PREFS.CODMODELMC.ordinal()] ;
		if (codmodel <= 0) {
			Funcoes.mensagemInforma( this, "Nenhum modelo de atendimento cadastrado, verifique!!!" );
			return;
		}

		
		if (codatend_atual == null) {
			Funcoes.mensagemInforma( this, "Não existe atendente vinculado a este usuário, verifique!!!" );
			return;
		}


		Atendimento atd = new org.freedom.modulos.crm.business.object.Atendimento();
		
		FNovoAtend dl = null;
		
		
		if ( codmodel != null ) {
			try {
				atd = daoatendo.loadModelAtend( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "ATATENDIMENTO" ), Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "ATMODATENDO" ), codmodel );
				atd.setCodempcl( Aplicativo.iCodEmp );
				atd.setCodfilialcl( ListaCampos.getMasterFilial( "VDCLIENTE" ));
				atd.setCodcli( txtCodCli.getVlrInteger() );

				dl = new FNovoAtend( this, con, atd, "A", "Novo atendimento a partir de modelo" );

				if ( fPrim.temTela( "Novo Atendimento" ) == false ) {
					fPrim.criatela( "Novo Atendimento", dl, con );
				}

			} catch ( SQLException e ) {
				Funcoes.mensagemErro( this, "Erro carregando modelo de atendimento!\n" + e.getMessage() );
				e.printStackTrace();
			}
		}
	}

	private void excluiAtend() {

		StringBuilder sql = new StringBuilder();
		int linhaSel = tabatd.getLinhaSel();
		
		if (codatend_atual == null) {
			Funcoes.mensagemInforma( this, "Não existe atendente vinculado a este usuário, verifique!!!" );
			return;
		}

		if ( linhaSel == -1 ) {
			Funcoes.mensagemInforma( this, "Selecione um item na lista!" );
			return;
		}

		if ( ( (Integer) tabatd.getValor( linhaSel, COL_ATENDIMENTO.CODORC.ordinal() ) ) > 0 ) {
			Funcoes.mensagemInforma( this, "Não é possivel excluir um atendimento vinculado a um orçamento!" );
			return;
		}

		Integer codatend = (Integer) tabatd.getValor( linhaSel, COL_ATENDIMENTO.CODATEND.ordinal() );

		if ( ( !acesatdodelout ) &&  ( ! (codatend_atual).equals( codatend ) ) ) {
			Funcoes.mensagemInforma( this, "Não é permitido excluir lançamentos de outro atendente !" );
			return;
		} else if ( ( !acesatdodellan) && (  (codatend_atual).equals( codatend ) ) ) {
			Funcoes.mensagemInforma( this, "Não é permitido excluir atendimentos !" );
			return;
		}

		if ( Funcoes.mensagemConfirma( this, "Confirma a exclusão deste atendimento?" ) == JOptionPane.YES_OPTION ) {
			try {
				daoatendo.excluirAtendimento( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "ATATENDIMENTO" ), 
						(Integer) tabatd.getValor( tabatd.getLinhaSel(), COL_ATENDIMENTO.CODATENDO.ordinal() ) );
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao excluir atendimento!\n" + err.getMessage(), true, con, err );
			}
		}

		carregaTabAtendo();
	}


	public void focusGained( FocusEvent fevt ) {

	}

	// Copia a descrição o planejamento para a descrição da conta:
	public void focusLost( FocusEvent fevt ) {

		if ( fevt.getSource() == txtAno ) {
			if ( txtAno.getVlrInteger().intValue() > 0 ) {
				getContatos();
			}
		}
	}

	public void stateChanged( ChangeEvent cevt ) {

		if ( cevt.getSource() == tpn ) {
			if ( tpn.getSelectedIndex() == 0 ) {
				txtCodCli.requestFocus();
			}
			else if ( tpn.getSelectedIndex() == 1 ) {
				txtEndEnt.requestFocus();
			}
			else if ( tpn.getSelectedIndex() == 2 ) {
				txtEndCob.requestFocus();
			}
			else if ( tpn.getSelectedIndex() == 3 ) {
				txtCodVend.requestFocus();
			}
			else if ( tpn.getSelectedIndex() == 4 ) {
				txaObs.requestFocus();
			}
			else if ( tpn.getSelectedIndex() == 6 ) {
				getContatos();
			}
		}
		else if ( cevt.getSource() == tpnCont ) {
			getContatos();
		}
	}

	public void valorAlterado( RadioGroupEvent rgevt ) {

		if ( rgPessoa.getVlrString().compareTo( "J" ) == 0 ) {
			txtCnpjCli.setEnabled( true );
			txtInscCli.setEnabled( true );
			if ( (Boolean) bPref.get( "CLIMESMOCNPJ" ) ) {
				setBordaReq( txtCnpjCli );
				setBordaReq( txtInscCli );
			}
			else {
				setBordaPad( txtCnpjCli );
				setBordaPad( txtInscCli );
			}
			txtCpfCli.setEnabled( false );
			setBordaPad( txtCpfCli );
			txtRgCli.setEnabled( false );
			setBordaPad( txtRgCli );
			txtSSPCli.setEnabled( false );
			setBordaPad( txtSSPCli );
		}
		else if ( rgPessoa.getVlrString().compareTo( "F" ) == 0 ) {
			txtCnpjCli.setEnabled( false );
			setBordaPad( txtCnpjCli );
			txtCpfCli.setEnabled( true );
			setBordaReq( txtCpfCli );
			txtRgCli.setEnabled( true );
			setBordaReq( txtRgCli );
			txtSSPCli.setEnabled( true );
			setBordaReq( txtSSPCli );
		}
	}

	public void valorAlterado( TabelaSelEvent evt ) {

		if ( evt.getTabela() == tbObsData ) {
			carregaObs();
		}
	}

	public void beforeCarrega( CarregaEvent cevt ) {

	}

	private void habilitaDesabilitaCodvend() {
		if ( ( !acestrocomisout ) &&  ( !txtCodVend.getVlrInteger().equals( codvend_atual ) && txtCodVend.getVlrInteger().intValue() != 0)) {
			txtCodVend.setEnabled( false );
		} else if ( ( !acestrocomis) && (  txtCodVend.getVlrInteger().equals( codvend_atual ) ) ) {
			txtCodVend.setEnabled( false );
		} else {
			txtCodVend.setEnabled( true );
		}
	}
	
	public void afterCarrega( CarregaEvent cevt ) {

		if ( cevt.getListaCampos() == lcCampos ) {
			carregaTabelaObs();
			txtAno.setVlrInteger( new Integer( Calendar.getInstance().get( Calendar.YEAR ) ) );
			getContatos();
			if (codatend_atual != null) {
				carregaTabAtendo();
			}
			habilitaDesabilitaCodvend();
		}
		else if ( cevt.getListaCampos() == lcMunic ) {
			if ( "".equals( txtDDDCli.getVlrString() ) ) {
				txtDDDCli.setVlrString( txtDDDMun.getVlrString() );
			}
			if ( "".equals( txtDDDFaxCli.getVlrString() ) ) {
				txtDDDFaxCli.setVlrString( txtDDDMun.getVlrString() );
			}
			if ( "".equals( txtDDDCelCli.getVlrString() ) ) {
				txtDDDCelCli.setVlrString( txtDDDMun.getVlrString() );
			}
		}
		else if ( cevt.getListaCampos() == lcMunicCob ) {
			if ( "".equals( txtDDDFoneCob.getVlrString() ) ) {
				txtDDDFoneCob.setVlrString( txtDDDMunCob.getVlrString() );
			}
			if ( "".equals( txtDDDFaxCob.getVlrString() ) ) {
				txtDDDFaxCob.setVlrString( txtDDDMunCob.getVlrString() );
			}

		}
		else if ( cevt.getListaCampos() == lcMunicEnt ) {
			if ( "".equals( txtDDDFoneEnt.getVlrString() ) ) {
				txtDDDFoneEnt.setVlrString( txtDDDMunEnt.getVlrString() );
			}
			if ( "".equals( txtDDDFaxEnt.getVlrString() ) ) {
				txtDDDFaxEnt.setVlrString( txtDDDMunEnt.getVlrString() );
			}
		}
		
	}

	public void beforeInsert( InsertEvent ievt ) {
		
	}

	public void afterInsert( InsertEvent ievt ) {

		if ( ievt.getListaCampos() == lcCampos ) {
			if ( (Boolean) bPref.get( "HABATIVCLI" ) ) {
				cbAtivo.setVlrString( "S" );
			}
			else {
				cbAtivo.setVlrString( "N" );
			}
			if ( (Boolean) bPref.get("USACLISEQ") ) {
				txtCodCli.setVlrInteger( daocli.testaCodPK( "VDCLIENTE" ) );
			}
			txtCodVend.setEnabled( true );
		} 
	}

	public void beforePost( PostEvent pevt ) {

		if ( pevt.getListaCampos() == lcCampos ) {

			if ((Boolean) bPref.get("ENDERECOOBRIGCLI")) {
				if ( txtDDDCli.getText().trim().length() < 2 ) {
					pevt.cancela();
					Funcoes.mensagemInforma( this, "Campo DDD é requerido! ! !" );
					txtDDDCli.requestFocus();
					return;
				}

				if ( txtFoneCli.getText().trim().length() < 8 ) {
					pevt.cancela();
					Funcoes.mensagemInforma( this, "Campo Fone é requerido! ! !" );
					txtFoneCli.requestFocus();
					return;
				}

				if ( txtDDDCelCli.getText().trim().length() < 2 ) {
					pevt.cancela();
					Funcoes.mensagemInforma( this, "Campo DDD Cel. é requerido! ! !" );
					txtDDDCelCli.requestFocus();
					return;
				}

				if ( txtCelCli.getText().trim().length() < 8 ) {
					pevt.cancela();
					Funcoes.mensagemInforma( this, "Campo Celular é requerido! ! !" );
					txtCelCli.requestFocus();
					return;
				}

				if ( txtContCli.getText().trim().length() < 1) {
					pevt.cancela();
					Funcoes.mensagemInforma( this, "Campo Contato é requerido! ! !" );
					txtFoneCli.requestFocus();
					return;
				}

				if ( txtEmailCli.getText().trim().length() < 1 ) {
					pevt.cancela();
					Funcoes.mensagemInforma( this, "Campo Email é requerido! ! !" );
					txtEmailCli.requestFocus();
					return;
				}
			}

			if ( (Boolean) bPref.get( "ENTREGAOBRIGCLI" ) ) { 
				if ( txtEndEnt.getText().trim().length()< 1 || txtNumEnt.getText().trim().length()< 1 || txtComplEnt.getText().trim().length()< 1 || txtBairEnt.getText().trim().length()< 1 || txtCepEnt.getText().trim().length()< 1 || txtDDDFoneEnt.getText().trim().length()< 1 
						|| txtFoneEnt.getText().trim().length()< 1 || txtDDDFaxEnt.getText().trim().length()< 1 || txtFaxEnt.getText().trim().length()< 1 || txtDDDCelEnt.getText().trim().length()< 1 || txtCelEnt.getText().trim().length()< 1 ||  txtEmailEnt.getText().trim().length()< 1 || txtCodPaisEnt.getText().trim().length()< 1 || txtSiglaUFEnt.getText().trim().length()< 1 || 
						txtCodMunicEnt.getText().trim().length()< 1 || txtContCliEnt.getText().trim().length()< 1 ) {
					pevt.cancela();
					Funcoes.mensagemInforma( this, "Todos os campos da aba Entrega são Obrigatórios ! ! !" );
					return;
				}

			}

			if ( !"".equals( txtEmailCli.getVlrString().trim() ) && !Funcoes.validaEmail( txtEmailCli.getVlrString().trim() ) ) {
				pevt.cancela();
				Funcoes.mensagemInforma( this, "Endereço de e-mail inválido !\nO registro não foi salvo. ! ! !" );
				return;
			}

			if ( !"".equals( txtEmailNfeCli.getVlrString().trim() ) && !Funcoes.validaEmail( txtEmailNfeCli.getVlrString().trim() ) ) {
				pevt.cancela();
				Funcoes.mensagemInforma( this, "Endereço de e-mail inválido !\nO registro não foi salvo. ! ! !" );
				return;
			}

			if (  (Boolean) bPref.get( "OBRIGTIPOFISC" ) && "".equals( txtCodFiscCli.getVlrString() ) ) {
				pevt.cancela();
				Funcoes.mensagemInforma( this, "Campo tipo fiscal do cliente é obrigatório (Aba venda) !" );
				if (txtCodFiscCli.isFocusable()) {
					txtCodFiscCli.requestFocus();
				}
				return;

			}

			if ( ( (Boolean) bPref.get( "CONSISTEIEPF" ) ) && ( (Boolean) bPref.get( "CONSISTEIECLI" ) ) ) {

				String sUF = "";

				if ( (Boolean) bPref.get( "USAIBGECLI" ) ) {
					sUF = txtSiglaUF.getText();
				}
				else {
					sUF = txtUFCli.getText();
				}

				if ( !Funcoes.validaIE( txtInscCli.getText(), sUF ) ) {
					if ( !txtInscCli.getText().trim().equals( "" ) ) {
						pevt.cancela();
						Funcoes.mensagemInforma( this, "Inscrição Estadual Inválida ! ! !" );
						txtInscCli.requestFocus();
						return;
					}
				}

				if ( ( !"".equals( txtInscCli.getVlrString() ) ) && ( !"ISENTO".equals( txtInscCli.getText().trim() ) ) ) {
					txtInscCli.setVlrString( Funcoes.formataIE( txtInscCli.getVlrString(), sUF ) );
				}
				else if ( "".equals( txtInscCli.getVlrString() ) ) {
					if ( Funcoes.mensagemConfirma( this, "Inscrição Estadual em branco! Inserir ISENTO?" ) == JOptionPane.OK_OPTION ) {
						txtInscCli.setVlrString( "ISENTO" );
					}
				}

			}

			if ( rgPessoa.getVlrString().compareTo( "F" ) == 0 ) {
				return;
			}

			if ( ( "".equals(txtCnpjCli.getText().trim()) ) && ( (Boolean) bPref.get( "CNPJOBRIGCLI" ) ) ) {
				pevt.cancela();
				Funcoes.mensagemInforma( this, "Campo CNPJ é requerido! ! !" );
				txtCnpjCli.requestFocus();
				return;
			}

			if ( ( lcCampos.getStatus() == ListaCampos.LCS_INSERT ) && ( duploCNPJ() ) ) {
				if ( (Boolean) bPref.get( "CLIMESMOCNPJ" ) ) {
					if ( Funcoes.mensagemConfirma( this, "Este CNPJ já está cadastrado! Salvar mesmo assim?" ) != JOptionPane.OK_OPTION ) {
						pevt.cancela();
						txtCnpjCli.requestFocus();
						return;
					}
				}
				else {
					pevt.cancela();
					Funcoes.mensagemInforma( this, "Este CNPJ já está cadastrado!" );
					txtCnpjCli.requestFocus();
					return;
				}
			}

			if ( txtInscCli.getText().trim().toUpperCase().compareTo( "ISENTO" ) == 0 ) {
				return;
			}

			if ( txtCodMunic.getText().trim().length() < 5 ) {
				pevt.cancela();
				Funcoes.mensagemInforma( this, "Código do Município é requerido! ! !" );
				txtCodMunic.requestFocus();
				return;
			}

			if ( !(Boolean) bPref.get( "USAIBGECLI" ) ) {
				if ( txtUFCli.getText().trim().length() < 2 ) {
					pevt.cancela();
					Funcoes.mensagemInforma( this, "Campo UF é requerido! ! !" );
					txtUFCli.requestFocus();
					return;
				}

			}



		}

		/*
		 * if ( (Boolean)bPref.get( "CONSISTEIECLI" ) ) { String sUF = ""; if ( (Boolean)bPref.get( "USAIBGECLI" )) {
		 * 
		 * sUF = txtSiglaUF.getText(); }else{ sUF = txtUFCli.getText(); }
		 * 
		 * 
		 * if ( ! Funcoes.vIE( txtInscCli.getText(), sUF ) ) { if ( ! txtInscCli.getText().trim().equals( "" ) ) { pevt.cancela(); Funcoes.mensagemInforma( this, "Inscrição Estadual Inválida ! ! !" ); txtInscCli.requestFocus(); return; } }
		 * 
		 * if ( ! txtInscCli.getText().trim().equals( "" ) ) { txtInscCli.setVlrString( Funcoes.sIEValida ); } }
		 */
	}

	public void afterPost( PostEvent pevt ) {

		if (pevt.getListaCampos()==lcCampos) {
			if ( ( rgPessoa.getVlrString().equals( "F" ) ) && ( pevt.ok ) ) {
				if ( telaConv != null ) {
					telaConv.setCodcli( txtCodCli.getVlrString(), txtRazCli.getVlrString() );
					this.btSair.doClick();
				}
			}
			habilitaDesabilitaCodvend();
		}
	}

	private void buscaEndereco() {

		if ( !"".equals( txtCepCli.getVlrString() ) ) {

			txtEndCli.setEnabled( false );
			txtComplCli.setEnabled( false );
			txtBairCli.setEnabled( false );
			txtCidCli.setEnabled( false );
			txtUFCli.setEnabled( false );
			txtCodPais.setEnabled( false );
			txtSiglaUF.setEnabled( false );
			txtCodMunic.setEnabled( false );
			txtDDDCli.setEnabled( false );
			txtDDDFaxCli.setEnabled( false );
			txtDDDCelCli.setEnabled( false );

			Thread th = new Thread( new Runnable() {

				public void run() {

					try {
						WSCep cep = new WSCep();
						cep.setCon( con );
						cep.setCep( txtCepCli.getVlrString() );
						cep.busca();
						Endereco endereco = cep.getEndereco();

						txtEndCli.setVlrString( endereco.getTipo() + " " + endereco.getLogradouro() );
						txtComplCli.setVlrString( endereco.getComplemento() );
						txtBairCli.setVlrString( endereco.getBairro() );
						txtCidCli.setVlrString( endereco.getCidade() );
						txtUFCli.setVlrString( endereco.getSiglauf() );
						txtCodPais.setVlrInteger( endereco.getCodpais() );
						txtSiglaUF.setVlrString( endereco.getSiglauf() );
						txtCodMunic.setVlrString( endereco.getCodmunic() );

						lcPais.carregaDados();
						lcUF.carregaDados();
						lcMunic.carregaDados();

						txtNumCli.requestFocus();
					} catch ( Exception e ) {
						e.printStackTrace();
						Funcoes.mensagemInforma( null, "Não foi encontrado o endereço para o CEP informado!" );
					} finally {
						txtEndCli.setEnabled( true );
						txtComplCli.setEnabled( true );
						txtBairCli.setEnabled( true );
						txtCidCli.setEnabled( true );
						txtUFCli.setEnabled( true );
						txtCodPais.setEnabled( true );
						txtSiglaUF.setEnabled( true );
						txtCodMunic.setEnabled( true );
						txtDDDCli.setEnabled( true );
						txtDDDFaxCli.setEnabled( true );
						txtDDDCelCli.setEnabled( true );
					}
				}
			} );
			try {
				th.start();
			} catch ( Exception err ) {
				Funcoes.mensagemInforma( null, "Não foi encontrado o endereço para o CEP informado!" );
				txtCepCli.requestFocus();
			}
		}
		else {
			Funcoes.mensagemInforma( null, "Digite um CEP para busca!" );
			txtCepCli.requestFocus();
		}

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		daocli = new DAOCliente( cn );

		try {
			bPref = daocli.getPrefere( Aplicativo.iCodEmp,  ListaCampos.getMasterFilial( "SGUSUARIO" )
					, Aplicativo.getUsuario().getIdusu().toLowerCase(), Aplicativo.iCodEmp,  ListaCampos.getMasterFilial( "SGPREFERE1" ) );
		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao carregar preferência geral" + e.getMessage() );
		}


		daoatendo= new DAOAtendimento( cn );
		try {
			daoatendo.setPrefs( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "SGPREFERE3" ));
			bPref.put( "prefAtendo", daoatendo.getPrefs());

		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro carregando preferências !\b" + e.getMessage() );
		}

		//Se não controlar o acesso pelo atendente deixa liberado para ler,gravar e etc.
		if ((Boolean) ((Object[]) bPref.get( "prefAtendo" ))[PREFS.CONTROLEACESATEND.ordinal()]) {
			try {
				atendente = daoatendo.paramAtendente( Aplicativo.iCodEmp, ListaCampos.getMasterFilial("ATATENDENTE") );
			} catch (SQLException e) {
				Funcoes.mensagemErro( this, "Erro carregando dados do atendente !\b" + e.getMessage() );
			}
	
			if (atendente != null) {	
				codatend_atual = (Integer) atendente.get("codatend");
				codvend_atual = (Integer) atendente.get( "codvend" );
				acesatdoaltout = (Boolean) atendente.get("acesatdoaltout");
				acesatdolerout = (Boolean) atendente.get("acesatdolerout");
				acesatdodellan = (Boolean) atendente.get("acesatdodellan");
				acesatdodelout = (Boolean) atendente.get("acesatdodelout");
				acestrocomis = (Boolean) atendente.get("acestrocomis");
				acestrocomisout = (Boolean) atendente.get("acestrocomisout");
			}
		} else {
			codatend_atual = org.freedom.modulos.crm.business.component.Atendimento.buscaAtendente();
			acesatdoaltout = true;
			acesatdolerout = true;
			acesatdodellan = true;
			acesatdodelout = true;
			acestrocomis = true;		
			acestrocomisout = true;
		}

		montaTela();

		lcTipoCli.setConexao( cn );
		lcCnae.setConexao( cn );
		lcTipoFiscCli.setConexao( cn );
		lcClas.setConexao( cn );
		lcVend.setConexao( cn );
		lcPlanoPag.setConexao( cn );
		lcTran.setConexao( cn );
		lcTipoCob.setConexao( cn );
		lcBanco.setConexao( cn );
		lcPesq.setConexao( cn );
		lcFor.setConexao( con );
		lcCliFor.setConexao( con );
		lcPais.setConexao( con );
		lcMetaVend.setConexao( con );
		lcHistorico.setConexao( con );
		lcCartCob.setConexao( con );
		lcForCli.setConexao( con );
		lcClixFor.setConexao( con );
		lcUF.setConexao( con );
		lcMunic.setConexao( con );
		lcUFEnt.setConexao( con );
		lcMunicEnt.setConexao( con );
		lcUFCob.setConexao( con );
		lcMunicCob.setConexao( con );
		lcPaisCob.setConexao( con );
		lcPaisEnt.setConexao( con );

		if ( lcSetor != null ) {
			lcSetor.setConexao( con );
		}
		if (bPref!=null && (Boolean) bPref.get( "OBRIGTIPOFISC" )) {
			txtCodFiscCli.setRequerido( true );
		}
		
		if ( !acesatdodellan && !acesatdodelout ) {
			btExcluirAtd.setEnabled( false );
		}
		else {
			btExcluirAtd.setEnabled( true );
		}

		//	txtCodAtendAtendo.setSoLeitura( !(Boolean) atendente.get( "acesatdolerout") );

	}

}
