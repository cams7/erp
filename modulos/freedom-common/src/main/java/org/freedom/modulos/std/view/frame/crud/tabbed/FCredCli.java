/**
 * @version 21/11/2003 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FCredCli.java <BR>
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
 *                   Tela ficha cadastral e crédito de cliente.
 * 
 */

package org.freedom.modulos.std.view.frame.crud.tabbed;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FTabDados;
import org.freedom.modulos.fnc.view.frame.crud.plain.FBanco;
import org.freedom.modulos.std.view.frame.crud.plain.FTipoCli;
import org.freedom.modulos.std.view.frame.crud.plain.FTipoCob;
import org.freedom.modulos.std.view.frame.crud.plain.FTipoCred;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.EditEvent;
import org.freedom.acao.EditListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;

public class FCredCli extends FTabDados implements ActionListener, CarregaListener, PostListener, EditListener, InsertListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinGeral = new JPanelPad( 680, 200 );

	private JPanelPad pnFicha = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinDetRefP = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinDetAutP = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinDetVeic = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinDetImov = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinDetTerras = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinDetBancos = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinDetSocios = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinDetRefC = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTablePad tbRefP = new JTablePad();

	private JTablePad tbAutP = new JTablePad();

	private JTablePad tbVeic = new JTablePad();

	private JTablePad tbImov = new JTablePad();

	private JTablePad tbTerras = new JTablePad();

	private JTablePad tbBancos = new JTablePad();

	private JTablePad tbSocios = new JTablePad();

	private JTablePad tbRefC = new JTablePad();

	private JScrollPane spnRefP = new JScrollPane( tbRefP ); // Scrool pane para

	// grid de ref. pess.

	private JScrollPane spnAutP = new JScrollPane( tbAutP ); // Scrool pane para

	// grid de aut. pess.

	private JScrollPane spnVeic = new JScrollPane( tbVeic ); // Scrool pane para

	// grid de Veiculos.

	private JScrollPane spnImov = new JScrollPane( tbImov ); // Scrool pane para

	// grid de Imoveis.

	private JScrollPane spnTerras = new JScrollPane( tbTerras ); // Scrool pane

	// para grid de
	// Terras.

	private JScrollPane spnBancos = new JScrollPane( tbBancos ); // Scrool pane

	// para grid de
	// Bancos.

	private JScrollPane spnSocios = new JScrollPane( tbSocios ); // Scrool pane

	// para grid de
	// Socios.

	private JScrollPane spnRefC = new JScrollPane( tbRefC ); // Scrool pane para

	// grid de ref com.

	private JPanelPad pinFiliacao = new JPanelPad( 680, 200 );

	private JPanelPad pinTrabalho = new JPanelPad( 680, 200 );

	private JPanelPad pinConjuge = new JPanelPad( 680, 200 );

	private JPanelPad pinCamposRefP = new JPanelPad( 680, 200 );

	private JPanelPad pinCamposAutP = new JPanelPad( 680, 200 );

	private JPanelPad pinCamposVeic = new JPanelPad( 680, 200 );

	private JPanelPad pinCamposImov = new JPanelPad( 680, 200 );

	private JPanelPad pinCamposTerras = new JPanelPad( 680, 200 );

	private JPanelPad pinCamposBancos = new JPanelPad( 680, 200 );

	private JPanelPad pinCamposSocios = new JPanelPad( 680, 200 );

	private JPanelPad pinCamposRefC = new JPanelPad( 680, 200 );

	private JPanelPad pinNavRefP = new JPanelPad( 680, 30 );

	private JPanelPad pinNavAutP = new JPanelPad( 680, 30 );

	private JPanelPad pinNavVeic = new JPanelPad( 680, 30 );

	private JPanelPad pinNavImov = new JPanelPad( 680, 30 );

	private JPanelPad pinNavTerras = new JPanelPad( 680, 30 );

	private JPanelPad pinNavBancos = new JPanelPad( 680, 30 );

	private JPanelPad pinNavSocios = new JPanelPad( 680, 30 );

	private JPanelPad pinNavRefC = new JPanelPad( 680, 30 );

	private JPanelPad pnRefP = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnAutP = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnVeic = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnImov = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnTerras = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnBancos = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnSocios = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnRefC = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinAvalista = new JPanelPad( 680, 200 );

	private JPanelPad pinJuridica = new JPanelPad( 680, 200 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDataCli = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodTipoCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldFK txtDescTipoCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodTpCred = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldFK txtDescTpCred = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtVlrTpCred = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtDtIniTr = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDtVencto = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtRazCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtEndCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNumCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtComplCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtBairCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtCidCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtUFCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtCepCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtFoneCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 12, 0 );

	private JTextFieldPad txtRamalCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtFaxCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtCelCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 9, 0 );

	private JTextFieldPad txtNatCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtApelidoCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtUFNatCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtTempoResCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtDDDCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtDDDFaxCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtDDDCelCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtPaiCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtRgPaiCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtSSPPaiCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtMaeCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtRgMaeCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtSSPMaeCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtEmpTrabCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtFuncaoTrabCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtDtAdmTrabCli = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtRendaTrabCli = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtDDDTrabCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtFoneTrabCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtRamalTrabCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtEndTrabCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNumTrabCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtComplTrabCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtBairTrabCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtCidTrabCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtUfTrabCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtCepTrabCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtOutRendaCli = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtFontRendaCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNomeConjCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtDtNascConjCli = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtRgConjCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtSSPConjCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtCPFConjCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 11, 0 );

	private JTextFieldPad txtNatConjCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtUfNatConjCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtEmpTrabConjCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtFuncaoConjCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtDtAdmTrabConjCli = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtRendaConjCli = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtNomeAvalCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtDtNascAvalCli = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtRgAvalCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtSSPAvalCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtCPFAvalCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 11, 0 );

	private JTextFieldPad txtEmpTrabAvalCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtFuncaoAvalCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtDtAdmTrabAvalCli = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtRamoAtivCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtRendaAvalCli = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtDtFundacCli = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtFaturCli = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtNumFiliaisCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtNumFuncCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JCheckBoxPad cbCompraReqCli = new JCheckBoxPad( "Compra com Requisição", "S", "N" );

	private JTextFieldPad txtCodEmpTb = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtCodFilialTb = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtCodTb = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtCodTipoCob = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoCob = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

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

	private JTextFieldPad txtCodRefP = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtNomeRefP = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtEndRefP = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNumRefP = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtComplRefP = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtBairRefP = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtCidRefP = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtCepRefP = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtUfRefP = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtDDDRefP = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtFoneRefP = new JTextFieldPad( JTextFieldPad.TP_STRING, 12, 0 );

	private JTextFieldPad txtCodAutP = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtNomeAutP = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtEndAutP = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNumAutP = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtComplAutP = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtBairAutP = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtCidAutP = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtCepAutP = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtUfAutP = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtDDDAutP = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtFoneAutP = new JTextFieldPad( JTextFieldPad.TP_STRING, 12, 0 );

	private JTextFieldPad txtRGAutP = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtSSPAutP = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtCPFAutP = new JTextFieldPad( JTextFieldPad.TP_STRING, 11, 0 );

	private JTextFieldPad txtFuncaoAutP = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtPlacaVeic = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtModeloVeic = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtAlienadoVeic = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtAnoVeic = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );

	private JTextFieldPad txtValorVeic = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtCodImov = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtTipoImov = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtConstrImov = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtAreaTerImov = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtValorImov = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtCodTerra = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtEndTerra = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNumTerra = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtBairTerra = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtAreaTerra = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtQtdCafeTerra = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtQtdGadoTerra = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextAreaPad txaObsTerra = new JTextAreaPad();

	private JTextFieldPad txtConta = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCodBanco = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private JTextFieldPad txtAgencia = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtDtAbConta = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldFK txtNomeBanco = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodRefC = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtNomeEmpRefC = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtDDDRefC = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtFoneRefC = new JTextFieldPad( JTextFieldPad.TP_STRING, 12, 0 );

	private JTextFieldPad txtDtMaiorCpRefC = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtVlrMaiorCpRefC = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtNroParcMaiorCpRefC = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtDtUltCpRefC = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtVlrUltCpRefC = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtNroParcUltCpRefC = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JCheckBoxPad cbPontualRefC = new JCheckBoxPad( "Sim", "S", "N" );

	private JTextFieldPad txtMediaAtrasoRefC = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtConceitoRefC = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JCheckBoxPad cbAvalistaRefC = new JCheckBoxPad( "Sim", "S", "N" );

	private JTextFieldPad txtInformanteRefC = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextAreaPad txaObsRefC = new JTextAreaPad();

	private JTextFieldPad txtNomeSocio = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtDtNascSocio = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtRgSocio = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtSSPSocio = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtCPFSocio = new JTextFieldPad( JTextFieldPad.TP_STRING, 11, 0 );

	private JTextFieldPad txtPercCotasSocio = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 2, 0 );

	private JTablePad tbFicha = new JTablePad();

	private JLabelPad lbNatCli = null;

	private JLabelPad lbApelidoCli = null;

	private JLabelPad lbUfNatCli = null;

	private JLabelPad lbTempoResCli = null;

	private JLabelPad lbEstadoCivil = null;

	private JLabelPad lbPaiCli = null;

	private JLabelPad lbMaeCli = null;

	private JLabelPad lbRgPaiCli = null;

	private JLabelPad lbSSPPaiCli = null;

	private JLabelPad lbSSPMaeCli = null;

	private JLabelPad lbRgMaeCli = null;

	private JLabelPad lbEmpTrabCli = null;

	private JLabelPad lbFuncaoTrabCli = null;

	private JLabelPad lbDtAdmTrabCli = null;

	private JLabelPad lbRendaTrabCli = null;

	private JLabelPad lbEndTrabCli = null;

	private JLabelPad lbNumTrabCli = null;

	private JLabelPad lbComplTrabCli = null;

	private JLabelPad lbBairTrabCli = null;

	private JLabelPad lbCidTrabCli = null;

	private JLabelPad lbCepTrabCli = null;

	private JLabelPad lbUfTrabCli = null;

	private JLabelPad lbDDDTrabCli = null;

	private JLabelPad lbFoneTrabCli = null;

	private JLabelPad lbRamalTrabCli = null;

	private JLabelPad lbOutRendaCli = null;

	private JLabelPad lbFontRendaCli = null;

	private JLabelPad lbFuncaoAutP = null;

	private ListaCampos lcTipoCred = new ListaCampos( this, "TR" );

	private ListaCampos lcTipoCli = new ListaCampos( this, "TI" );

	private ListaCampos lcFicha = new ListaCampos( this, "CC" );

	private ListaCampos lcTipoCob = new ListaCampos( this, "TC" );

	private ListaCampos lcRefP = new ListaCampos( this, "RP" );

	private ListaCampos lcAutP = new ListaCampos( this, "AP" );

	private ListaCampos lcVeic = new ListaCampos( this, "VE" );

	private ListaCampos lcImov = new ListaCampos( this, "IM" );

	private ListaCampos lcTerras = new ListaCampos( this, "TE" );

	private ListaCampos lcBancos = new ListaCampos( this );

	private ListaCampos lcSocios = new ListaCampos( this, "SO" );

	private ListaCampos lcRefC = new ListaCampos( this, "RC" );

	private ListaCampos lcBanco = new ListaCampos( this, "BC" );

	private Navegador navFicha = new Navegador( false );

	private Navegador navRefP = new Navegador( true );

	private Navegador navAutP = new Navegador( true );

	private Navegador navVeic = new Navegador( true );

	private Navegador navImov = new Navegador( true );

	private Navegador navTerras = new Navegador( true );

	private Navegador navBancos = new Navegador( true );

	private Navegador navSocios = new Navegador( true );

	private Navegador navRefC = new Navegador( true );

	private boolean bFisTipoCli = false;

	private boolean bJurTipoCli = false;

	private boolean bFilTipoCli = false;

	private boolean bLocTrabTipoCli = false;

	private boolean bRefPesTipoCli = false;

	private boolean bConjTipoCli = false;

	private boolean bVeicTipoCli = false;

	private boolean bImovTipoCli = false;

	private boolean bTerraTipoCli = false;

	private boolean bPessAutTipoCli = false;

	private boolean bAvalTipoCli = false;

	private boolean bAchou = true;

	private JTabbedPanePad tpn2 = new JTabbedPanePad();

	private JComboBoxPad cbEstCivCli = null;

	public FCredCli() {

		super();
		setTitulo( "Ficha cadastral/Crédito por cliente" );
		setAtribos( 10, 10, 650, 540 );

		txtFoneCli.setMascara( JTextFieldPad.MC_FONE );
		txtFoneCob.setMascara( JTextFieldPad.MC_FONE );
		txtFoneTrabCli.setMascara( JTextFieldPad.MC_FONE );
		txtFoneRefP.setMascara( JTextFieldPad.MC_FONE );
		txtFoneAutP.setMascara( JTextFieldPad.MC_FONE );

		txtFaxCli.setMascara( JTextFieldPad.MC_FONE );
		txtFaxCob.setMascara( JTextFieldPad.MC_FONE );
		txtFoneRefC.setMascara( JTextFieldPad.MC_FONE );

		txtCepCli.setMascara( JTextFieldPad.MC_CEP );
		txtCepCob.setMascara( JTextFieldPad.MC_CEP );
		txtCepTrabCli.setMascara( JTextFieldPad.MC_CEP );
		txtCepRefP.setMascara( JTextFieldPad.MC_CEP );
		txtCepAutP.setMascara( JTextFieldPad.MC_CEP );

		txtCPFAvalCli.setMascara( JTextFieldPad.MC_CPF );
		txtCPFConjCli.setMascara( JTextFieldPad.MC_CPF );
		txtCPFAutP.setMascara( JTextFieldPad.MC_CPF );
		txtCPFSocio.setMascara( JTextFieldPad.MC_CPF );

		txtRgAvalCli.setMascara( JTextFieldPad.MC_RG );
		txtRgConjCli.setMascara( JTextFieldPad.MC_RG );
		txtRgMaeCli.setMascara( JTextFieldPad.MC_RG );
		txtRgPaiCli.setMascara( JTextFieldPad.MC_RG );
		txtRGAutP.setMascara( JTextFieldPad.MC_RG );
		txtRgSocio.setMascara( JTextFieldPad.MC_RG );

		lcFicha.setMaster( lcCampos );
		lcRefP.setMaster( lcCampos );
		lcAutP.setMaster( lcCampos );
		lcVeic.setMaster( lcCampos );
		lcImov.setMaster( lcCampos );
		lcTerras.setMaster( lcCampos );
		lcBancos.setMaster( lcCampos );
		lcSocios.setMaster( lcCampos );
		lcRefC.setMaster( lcCampos );

		lcCampos.adicDetalhe( lcFicha );
		lcCampos.adicDetalhe( lcRefP );
		lcCampos.adicDetalhe( lcAutP );
		lcCampos.adicDetalhe( lcVeic );
		lcCampos.adicDetalhe( lcImov );
		lcCampos.adicDetalhe( lcTerras );
		lcCampos.adicDetalhe( lcBancos );
		lcCampos.adicDetalhe( lcSocios );
		lcCampos.adicDetalhe( lcRefC );

		lcFicha.setTabela( tbFicha );
		lcRefP.setTabela( tbRefP );
		lcAutP.setTabela( tbAutP );
		lcVeic.setTabela( tbVeic );
		lcImov.setTabela( tbImov );
		lcTerras.setTabela( tbTerras );
		lcBancos.setTabela( tbBancos );
		lcSocios.setTabela( tbSocios );
		lcRefC.setTabela( tbRefC );

		navFicha.btNovo.setVisible( false );
		navFicha.btExcluir.setVisible( false );

		lcTipoCli.add( new GuardaCampo( txtCodTipoCli, "CodTipoCli", "Cód.tp.cli.", ListaCampos.DB_PK, true ) );
		lcTipoCli.add( new GuardaCampo( txtDescTipoCli, "DescTipoCli", "Descrição do tipo de cliente", ListaCampos.DB_SI, false ) );
		lcTipoCli.montaSql( false, "TIPOCLI", "VD" );
		lcTipoCli.setQueryCommit( false );
		lcTipoCli.setReadOnly( true );
		txtCodTipoCli.setTabelaExterna( lcTipoCli, FTipoCli.class.getCanonicalName() );

		lcTipoCred.add( new GuardaCampo( txtCodTpCred, "CodTpCred", "Cód.tp.cred.", ListaCampos.DB_PK, true ) );
		lcTipoCred.add( new GuardaCampo( txtDescTpCred, "DescTpCred", "Descrição do tipo de crédito", ListaCampos.DB_SI, false ) );
		lcTipoCred.add( new GuardaCampo( txtVlrTpCred, "VlrTpCred", "Valor", ListaCampos.DB_SI, false ) );
		lcTipoCred.montaSql( false, "TIPOCRED", "FN" );
		lcTipoCred.setQueryCommit( false );
		lcTipoCred.setReadOnly( true );
		txtCodTpCred.setTabelaExterna( lcTipoCred, FTipoCred.class.getCanonicalName() );

		lcTipoCob.add( new GuardaCampo( txtCodTipoCob, "CodTipoCob", "Cód.tp.cob.", ListaCampos.DB_PK, false ) );
		lcTipoCob.add( new GuardaCampo( txtDescTipoCob, "DescTipoCob", "Descrição do tipo de cobrança", ListaCampos.DB_SI, false ) );
		lcTipoCob.montaSql( false, "TIPOCOB", "FN" );
		lcTipoCob.setQueryCommit( false );
		lcTipoCob.setReadOnly( true );
		txtCodTipoCob.setTabelaExterna( lcTipoCob, FTipoCob.class.getCanonicalName() );

		lcBanco.add( new GuardaCampo( txtCodBanco, "CodBanco", "Cód.Bco.", ListaCampos.DB_PK, false ) );
		lcBanco.add( new GuardaCampo( txtNomeBanco, "nomebanco", "Nome Banco", ListaCampos.DB_SI, false ) );
		lcBanco.montaSql( false, "BANCO", "FN" );
		lcBanco.setQueryCommit( false );
		lcBanco.setReadOnly( true );
		txtCodBanco.setTabelaExterna( lcBanco, FBanco.class.getCanonicalName() );

		setPainel( pinGeral );

		lcCampos.addCarregaListener( this );
		lcCampos.addPostListener( this );
		lcFicha.addInsertListener( this );
		lcFicha.addEditListener( this );

		adicCampo( txtCodCli, 7, 20, 70, 20, "CodCli", "Cód.cli.", ListaCampos.DB_PK, true );
		adicCampo( txtRazCli, 80, 20, 257, 20, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtCodTipoCli, "CodTipoCli", "Cód.tp.cli", ListaCampos.DB_FK, txtDescTipoCli, false );
		adicDescFK( txtDescTipoCli, 340, 20, 130, 20, "DescTipoCli", "Desc. tipo de cliente" );
		adicCampo( txtDataCli, 473, 20, 95, 20, "DataCli", "Cadastro", ListaCampos.DB_SI, false );
		adicCampo( txtCodTpCred, 7, 60, 70, 20, "CodTpCred", "Cód.tp.cred", ListaCampos.DB_FK, txtDescTpCred, true );
		adicDescFK( txtDescTpCred, 80, 60, 212, 20, "DescTpCred", "Descrição do crédito" );
		adicDescFK( txtVlrTpCred, 295, 60, 107, 20, "VlrTpCred", "Valor" );
		adicCampo( txtDtIniTr, 405, 60, 80, 20, "DtIniTr", "Dt.ab.créd.", ListaCampos.DB_SI, true );
		adicCampo( txtDtVencto, 488, 60, 80, 20, "DtVenctoTr", "Vencimento", ListaCampos.DB_SI, true );
		adicCampo( txtEndCli, 7, 100, 330, 20, "EndCli", "Endereço", ListaCampos.DB_SI, false );
		adicCampo( txtNumCli, 340, 100, 77, 20, "NumCli", "Num.", ListaCampos.DB_SI, false );
		adicCampo( txtComplCli, 420, 100, 149, 20, "ComplCli", "Compl.", ListaCampos.DB_SI, false );
		adicCampo( txtBairCli, 7, 140, 210, 20, "BairCli", "Bairro", ListaCampos.DB_SI, false );
		adicCampo( txtCidCli, 220, 140, 210, 20, "CidCli", "Cidade", ListaCampos.DB_SI, false );
		adicCampo( txtCepCli, 433, 140, 80, 20, "CepCli", "Cep", ListaCampos.DB_SI, false );
		adicCampo( txtUFCli, 516, 140, 52, 20, "UFCli", "UF", ListaCampos.DB_SI, false );
		adicCampo( txtDDDCli, 7, 180, 60, 20, "DDDCli", "DDD", ListaCampos.DB_SI, false );
		adicCampo( txtFoneCli, 70, 180, 97, 20, "FoneCli", "Telefone", ListaCampos.DB_SI, false );
		adicCampo( txtRamalCli, 170, 180, 60, 20, "RamalCli", "Ramal", ListaCampos.DB_SI, false );
		adicCampo( txtDDDFaxCli, 233, 180, 60, 20, "DDDFaxCli", "DDD", ListaCampos.DB_SI, false );
		adicCampo( txtFaxCli, 296, 180, 97, 20, "FaxCli", "Fax", ListaCampos.DB_SI, false );
		adicCampo( txtDDDCelCli, 396, 180, 60, 20, "DDDCelCli", "DDD", ListaCampos.DB_SI, false );
		adicCampo( txtCelCli, 459, 180, 110, 20, "CelCli", "Celular", ListaCampos.DB_SI, false );

		adicCampo( txtEndCob, 7, 220, 330, 20, "EndCob", "Endereço de cobrança", ListaCampos.DB_SI, false );
		adicCampo( txtNumCob, 340, 220, 77, 20, "NumCob", "Num. cob.", ListaCampos.DB_SI, false );
		adicCampo( txtComplCob, 420, 220, 149, 20, "ComplCob", "Compl. cobrança", ListaCampos.DB_SI, false );
		adicCampo( txtBairCob, 7, 260, 210, 20, "BairCob", "Bairro cobrança", ListaCampos.DB_SI, false );
		adicCampo( txtCidCob, 220, 260, 210, 20, "CidCob", "Cidade cobrança", ListaCampos.DB_SI, false );
		adicCampo( txtCepCob, 433, 260, 80, 20, "CepCob", "Cep cobrança", ListaCampos.DB_SI, false );
		adicCampo( txtUFCob, 516, 260, 52, 20, "UFCob", "UF cob.", ListaCampos.DB_SI, false );
		adicCampo( txtDDDFoneCob, 7, 300, 40, 20, "DDDFoneCob", "DDD", ListaCampos.DB_SI, false );
		adicCampo( txtFoneCob, 50, 300, 97, 20, "FoneCob", "Telefone cob.", ListaCampos.DB_SI, false );
		adicCampo( txtDDDFaxCob, 150, 300, 40, 20, "DDDFaxCob", "DDD", ListaCampos.DB_SI, false );
		adicCampo( txtFaxCob, 193, 300, 97, 20, "FaxCob", "Fax cob.", ListaCampos.DB_SI, false );
		adicCampo( txtCodTipoCob, 293, 300, 60, 20, "CodTipoCob", "Cód.t.cob.", ListaCampos.DB_FK, txtDescTipoCob, false );
		adicDescFK( txtDescTipoCob, 356, 300, 211, 20, "DescTipoCob", "Descrição do tipo de cobrança" );

		lbApelidoCli = adicCampo( txtApelidoCli, 7, 340, 170, 20, "ApelidoCli", "Apelido", ListaCampos.DB_SI, false );
		lbNatCli = adicCampo( txtNatCli, 180, 340, 165, 20, "NatCli", "Naturalidade", ListaCampos.DB_SI, false );
		lbUfNatCli = adicCampo( txtUFNatCli, 348, 340, 52, 20, "UfNatCli", "Uf Natur.", ListaCampos.DB_SI, false );
		lbTempoResCli = adicCampo( txtTempoResCli, 403, 340, 165, 20, "TempoResCli", "Tempo de residência.", ListaCampos.DB_SI, false );

		adicCampoInvisivel( txtCodEmpTb, "codempec", "cod. emp.", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtCodFilialTb, "codfilialec", "cod. filial", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtCodTb, "codtbec", "cod. emp.", ListaCampos.DB_SI, false );

		cbEstCivCli = new JComboBoxPad( new Vector<String>(), new Vector<Object>(), JComboBoxPad.TP_INTEGER, 5, 0 );
		lbEstadoCivil = adicDB( cbEstCivCli, 7, 380, 200, 25, "codittbec", "Estado civil", false );
		cbEstCivCli.setZeroNulo();
		setListaCampos( true, "CLIENTE", "VD" );
		lcCampos.setAutoLimpaPK( true );
		lcCampos.setPodeIns( false );
		lcCampos.setPodeExc( false );
		lcCampos.setQueryInsert( false );

		setListaCampos( lcFicha );

		tpn2.setTabPlacement( SwingConstants.LEFT );
		tpn.setPreferredSize( new Dimension( 50, 500 ) );

		pnFicha.add( tpn2, BorderLayout.WEST );

		setPainel( pinFiliacao );

		lbPaiCli = adicCampo( txtPaiCli, 7, 20, 315, 20, "PaiCli", "Nome do pai", ListaCampos.DB_SI, false );
		lbRgPaiCli = adicCampo( txtRgPaiCli, 325, 20, 130, 20, "RgPaiCli", "Rg", ListaCampos.DB_SI, false );
		lbSSPPaiCli = adicCampo( txtSSPPaiCli, 458, 20, 100, 20, "SSPPaiCli", "Orgão exp.", ListaCampos.DB_SI, false );
		lbMaeCli = adicCampo( txtMaeCli, 7, 60, 315, 20, "MaeCli", "Nome da mãe", ListaCampos.DB_SI, false );
		lbRgMaeCli = adicCampo( txtRgMaeCli, 325, 60, 130, 20, "RgMaeCli", "Rg", ListaCampos.DB_SI, false );
		lbSSPMaeCli = adicCampo( txtSSPMaeCli, 458, 60, 100, 20, "SSPMaeCli", "Orgão exp.", ListaCampos.DB_SI, false );

		setPainel( pinTrabalho );

		lbEmpTrabCli = adicCampo( txtEmpTrabCli, 7, 20, 310, 20, "EmpTrabCli", "Empresa onde trabalha", ListaCampos.DB_SI, false );
		lbFuncaoTrabCli = adicCampo( txtFuncaoTrabCli, 320, 20, 165, 20, "CargoTrabCli", "Funcao", ListaCampos.DB_SI, false );
		lbDtAdmTrabCli = adicCampo( txtDtAdmTrabCli, 488, 20, 81, 20, "DtAdmTrabCli", "Dt.Admis.", ListaCampos.DB_SI, false );
		lbEndTrabCli = adicCampo( txtEndTrabCli, 7, 60, 330, 20, "EndTrabCli", "Endereço", ListaCampos.DB_SI, false );
		lbNumTrabCli = adicCampo( txtNumTrabCli, 340, 60, 77, 20, "NumTrabCli", "Num.", ListaCampos.DB_SI, false );
		lbComplTrabCli = adicCampo( txtComplTrabCli, 420, 60, 149, 20, "ComplTrabCli", "Compl.", ListaCampos.DB_SI, false );
		lbBairTrabCli = adicCampo( txtBairTrabCli, 7, 100, 210, 20, "BairTrabCli", "Bairro", ListaCampos.DB_SI, false );
		lbCidTrabCli = adicCampo( txtCidTrabCli, 220, 100, 210, 20, "CidTrabCli", "Cidade", ListaCampos.DB_SI, false );
		lbCepTrabCli = adicCampo( txtCepTrabCli, 433, 100, 80, 20, "CepTrabCli", "Cep", ListaCampos.DB_SI, false );
		lbUfTrabCli = adicCampo( txtUfTrabCli, 516, 100, 53, 20, "UFTrabCli", "UF", ListaCampos.DB_SI, false );
		lbDDDTrabCli = adicCampo( txtDDDTrabCli, 7, 140, 40, 20, "DDDTrabCli", "DDD", ListaCampos.DB_SI, false );
		lbFoneTrabCli = adicCampo( txtFoneTrabCli, 50, 140, 97, 20, "FoneTrabCli", "Telefone", ListaCampos.DB_SI, false );
		lbRamalTrabCli = adicCampo( txtRamalTrabCli, 150, 140, 47, 20, "RamalTrabCli", "Ramal", ListaCampos.DB_SI, false );
		lbRendaTrabCli = adicCampo( txtRendaTrabCli, 200, 140, 90, 20, "RendaTrabCli", "Renda", ListaCampos.DB_SI, false );
		lbOutRendaCli = adicCampo( txtOutRendaCli, 293, 140, 90, 20, "OutRendaCli", "Outras rendas", ListaCampos.DB_SI, false );
		lbFontRendaCli = adicCampo( txtFontRendaCli, 386, 140, 184, 20, "FontRendaCli", "Fonte de outras rendas", ListaCampos.DB_SI, false );

		setPainel( pinConjuge );

		adicCampo( txtNomeConjCli, 7, 20, 300, 20, "NomeConjCli", "Nome do cônjuge", ListaCampos.DB_SI, false );
		adicCampo( txtDtNascConjCli, 310, 20, 85, 20, "DtNascConjCli", "Dt.nasc.conj.", ListaCampos.DB_SI, false );
		adicCampo( txtRgConjCli, 398, 20, 120, 20, "RgConjCli", "Rg", ListaCampos.DB_SI, false );
		adicCampo( txtSSPConjCli, 521, 20, 120, 20, "SSPConjCli", "Orgão exp.", ListaCampos.DB_SI, false );
		adicCampo( txtCPFConjCli, 7, 60, 140, 20, "CPFConjCli", "CPF", ListaCampos.DB_SI, false );
		adicCampo( txtNatConjCli, 150, 60, 140, 20, "NatConjCli", "Naturalidade", ListaCampos.DB_SI, false );
		adicCampo( txtUfNatConjCli, 293, 60, 30, 20, "UfNatConjCli", "UF", ListaCampos.DB_SI, false );
		adicCampo( txtEmpTrabConjCli, 326, 60, 315, 20, "EmpTrabConjCli", "Empresa onde trabalha", ListaCampos.DB_SI, false );
		adicCampo( txtFuncaoConjCli, 7, 100, 150, 20, "CargoConjCli", "Funcao", ListaCampos.DB_SI, false );
		adicCampo( txtDtAdmTrabConjCli, 160, 100, 90, 20, "DtAdmTrabConjCli", "Dt.admissao", ListaCampos.DB_SI, false );
		adicCampo( txtRendaConjCli, 253, 100, 90, 20, "RendaConjCli", "Renda", ListaCampos.DB_SI, false );

		setPainel( pinAvalista );

		adicCampo( txtNomeAvalCli, 7, 20, 300, 20, "NomeAvalCli", "Nome do avalista", ListaCampos.DB_SI, false );
		adicCampo( txtDtNascAvalCli, 310, 20, 85, 20, "DtNascAvalCli", "Dt.nasc.Aval.", ListaCampos.DB_SI, false );
		adicCampo( txtRgAvalCli, 398, 20, 120, 20, "RgAvalCli", "Rg", ListaCampos.DB_SI, false );
		adicCampo( txtSSPAvalCli, 521, 20, 120, 20, "SSPAvalCli", "Orgão exp.", ListaCampos.DB_SI, false );
		adicCampo( txtCPFAvalCli, 7, 60, 140, 20, "CPFAvalCli", "CPF", ListaCampos.DB_SI, false );
		adicCampo( txtEmpTrabAvalCli, 150, 60, 315, 20, "EmpTrabAvalCli", "Empresa onde trabalha", ListaCampos.DB_SI, false );
		adicCampo( txtFuncaoAvalCli, 468, 60, 173, 20, "CargoAvalCli", "Funcao", ListaCampos.DB_SI, false );
		adicCampo( txtDtAdmTrabAvalCli, 7, 100, 90, 20, "DtAdmTrabAvalCli", "Dt.admissao", ListaCampos.DB_SI, false );
		adicCampo( txtRendaAvalCli, 100, 100, 90, 20, "RendaAvalCli", "Renda", ListaCampos.DB_SI, false );

		// ************************* Juridica

		setPainel( pinJuridica );

		adicCampo( txtDtFundacCli, 7, 20, 90, 20, "DtFundacCli", "Dat. Fundação", ListaCampos.DB_SI, false );
		adicCampo( txtRamoAtivCli, 100, 20, 330, 20, "RamoAtivCli", "Ramo de Atividade", ListaCampos.DB_SI, false );
		adicCampo( txtFaturCli, 7, 60, 80, 20, "FaturCli", "Faturamento", ListaCampos.DB_SI, false );
		adicCampo( txtNumFiliaisCli, 90, 60, 90, 20, "NumFiliaisCli", "Núm. Filiais", ListaCampos.DB_SI, false );
		adicCampo( txtNumFuncCli, 187, 60, 90, 20, "NumFuncCli", "Núm. Func.", ListaCampos.DB_SI, false );
		adicDB( cbCompraReqCli, 7, 85, 180, 25, "CompraReqCli", "", false );

		setListaCampos( false, "CLICOMPL", "VD" );
		lcFicha.setQueryInsert( false );
		lcFicha.setQueryCommit( false );
		lcFicha.montaTab();

		// *************************

		// *************************Veiculos

		setPainel( pinDetVeic, pnVeic );

		pinDetVeic.setPreferredSize( new Dimension( 600, 130 ) );
		pinDetVeic.add( pinNavVeic, BorderLayout.SOUTH );
		pinDetVeic.add( pinCamposVeic, BorderLayout.CENTER );
		setListaCampos( lcVeic );
		setNavegador( navVeic );

		pnVeic.add( pinDetVeic, BorderLayout.SOUTH );
		pnVeic.add( spnVeic, BorderLayout.CENTER );

		pinNavVeic.adic( navVeic, 0, 0, 270, 25 );

		setPainel( pinCamposVeic );

		adicCampo( txtPlacaVeic, 7, 20, 80, 20, "PlacaVeic", "Placa", ListaCampos.DB_PK, null, true );
		adicCampo( txtModeloVeic, 90, 20, 250, 20, "ModeloVeic", "Modelo", ListaCampos.DB_SI, null, false );
		adicCampo( txtAlienadoVeic, 343, 20, 150, 20, "AlienadoVeic", "Alienado", ListaCampos.DB_SI, null, false );
		adicCampo( txtAnoVeic, 7, 60, 80, 20, "AnoVeic", "Ano", ListaCampos.DB_SI, null, false );
		adicCampo( txtValorVeic, 90, 60, 100, 20, "ValorVeic", "Valor", ListaCampos.DB_SI, null, false );

		setListaCampos( false, "CLIVEIC", "VD" );
		lcVeic.setQueryInsert( false );
		lcVeic.setQueryCommit( false );
		lcVeic.montaTab();
		tbVeic.setTamColuna( 60, 0 );
		tbVeic.setTamColuna( 200, 1 );
		tbVeic.setTamColuna( 150, 2 );
		tbVeic.setTamColuna( 80, 3 );
		tbVeic.setTamColuna( 110, 4 );

		// **************************

		// *************************Imoveis

		setPainel( pinDetImov, pnImov );

		pinDetImov.setPreferredSize( new Dimension( 600, 80 ) );
		pinDetImov.add( pinNavImov, BorderLayout.SOUTH );
		pinDetImov.add( pinCamposImov, BorderLayout.CENTER );
		setListaCampos( lcImov );
		setNavegador( navImov );

		pnImov.add( pinDetImov, BorderLayout.SOUTH );
		pnImov.add( spnImov, BorderLayout.CENTER );

		pinNavImov.adic( navImov, 0, 0, 270, 25 );

		setPainel( pinCamposImov );

		adicCampo( txtCodImov, 7, 20, 50, 20, "CodImov", "Seq.", ListaCampos.DB_PK, null, true );
		adicCampo( txtTipoImov, 60, 20, 120, 20, "TipoImov", "Propria/Aluguel", ListaCampos.DB_SI, null, false );
		adicCampo( txtConstrImov, 183, 20, 120, 20, "ConstrImov", "Tipo construção", ListaCampos.DB_SI, null, false );
		adicCampo( txtAreaTerImov, 306, 20, 100, 20, "AreaTerImov", "Area terreno", ListaCampos.DB_SI, null, false );
		adicCampo( txtValorImov, 409, 20, 100, 20, "ValorImov", "Valor", ListaCampos.DB_SI, null, false );

		setListaCampos( true, "CLIIMOV", "VD" );
		lcImov.setQueryInsert( false );
		lcImov.setQueryCommit( false );
		lcImov.montaTab();
		tbImov.setTamColuna( 30, 0 );
		tbImov.setTamColuna( 150, 1 );
		tbImov.setTamColuna( 150, 2 );
		tbImov.setTamColuna( 120, 3 );
		tbImov.setTamColuna( 120, 4 );

		// **************************

		// *************************Terras

		setPainel( pinDetTerras, pnTerras );

		pinDetTerras.setPreferredSize( new Dimension( 600, 230 ) );
		pinDetTerras.add( pinNavTerras, BorderLayout.SOUTH );
		pinDetTerras.add( pinCamposTerras, BorderLayout.CENTER );
		setListaCampos( lcTerras );
		setNavegador( navTerras );

		pnTerras.add( pinDetTerras, BorderLayout.SOUTH );
		pnTerras.add( spnTerras, BorderLayout.CENTER );

		pinNavTerras.adic( navTerras, 0, 0, 270, 25 );

		setPainel( pinCamposTerras );

		adicCampo( txtCodTerra, 7, 20, 50, 20, "CodTerra", "Seq.", ListaCampos.DB_PK, null, true );
		adicCampo( txtEndTerra, 60, 20, 250, 20, "EndTerra", "Endereço", ListaCampos.DB_SI, null, false );
		adicCampo( txtNumTerra, 313, 20, 90, 20, "NumTerra", "Num.", ListaCampos.DB_SI, null, false );
		adicCampo( txtBairTerra, 406, 20, 150, 20, "BairTerra", "Bairro", ListaCampos.DB_SI, null, false );
		adicCampo( txtAreaTerra, 559, 20, 100, 20, "AreaTerra", "Area total", ListaCampos.DB_SI, null, false );
		adicCampo( txtQtdCafeTerra, 7, 60, 100, 20, "QtdCafeTerra", "Qtd.Café", ListaCampos.DB_SI, null, false );
		adicCampo( txtQtdGadoTerra, 110, 60, 100, 20, "QtdGadoTerra", "Qtd.Gado", ListaCampos.DB_SI, null, false );
		adicDBLiv( txaObsTerra, 7, 100, 300, 80, "ObsTerra", "Observações", false );

		setListaCampos( true, "CLITERRA", "VD" );
		lcTerras.setQueryInsert( false );
		lcTerras.setQueryCommit( false );
		lcTerras.montaTab();
		tbTerras.setTamColuna( 30, 0 );
		tbTerras.setTamColuna( 200, 1 );
		tbTerras.setTamColuna( 150, 2 );
		tbTerras.setTamColuna( 120, 3 );
		tbTerras.setTamColuna( 120, 4 );
		tbTerras.setTamColuna( 120, 5 );
		tbTerras.setTamColuna( 200, 6 );

		// **************************

		// *************************Dados bancários

		setPainel( pinDetBancos, pnBancos );

		pinDetBancos.setPreferredSize( new Dimension( 600, 80 ) );
		pinDetBancos.add( pinNavBancos, BorderLayout.SOUTH );
		pinDetBancos.add( pinCamposBancos, BorderLayout.CENTER );
		setListaCampos( lcBancos );
		setNavegador( navBancos );

		pnBancos.add( pinDetBancos, BorderLayout.SOUTH );
		pnBancos.add( spnBancos, BorderLayout.CENTER );

		pinNavBancos.adic( navBancos, 0, 0, 270, 25 );

		setPainel( pinCamposBancos );

		adicCampo( txtConta, 7, 20, 90, 20, "conta", "Conta", ListaCampos.DB_PK, null, false );
		adicCampo( txtCodBanco, 100, 20, 60, 20, "CodBanco", "Cd.Bco.", ListaCampos.DB_FK, txtNomeBanco, true );
		adicDescFK( txtNomeBanco, 163, 20, 150, 20, "CodBanco", "Cd.Bco." );
		adicCampo( txtAgencia, 316, 20, 120, 20, "Agencia", "Agencia", ListaCampos.DB_SI, null, false );
		adicCampo( txtDtAbConta, 439, 20, 100, 20, "DtAbConta", "Dt. Abetura", ListaCampos.DB_SI, null, false );

		setListaCampos( false, "CLICONTAS", "VD" );
		lcBancos.setQueryInsert( false );
		lcBancos.setQueryCommit( false );
		lcBancos.montaTab();
		tbBancos.setTamColuna( 90, 0 );
		tbBancos.setTamColuna( 90, 1 );
		tbBancos.setTamColuna( 150, 2 );
		tbBancos.setTamColuna( 100, 3 );

		// **************************

		// *************************Referencias Comerciais

		setPainel( pinDetRefC, pnRefC );

		pinDetRefC.setPreferredSize( new Dimension( 600, 265 ) );
		pinDetRefC.add( pinNavRefC, BorderLayout.SOUTH );
		pinDetRefC.add( pinCamposRefC, BorderLayout.CENTER );
		setListaCampos( lcRefC );
		setNavegador( navRefC );

		pnRefC.add( pinDetRefC, BorderLayout.SOUTH );
		pnRefC.add( spnRefC, BorderLayout.CENTER );

		pinNavRefC.adic( navRefC, 0, 0, 270, 25 );

		setPainel( pinCamposRefC );

		adicCampo( txtCodRefC, 7, 20, 50, 20, "CodRefC", "Seq.", ListaCampos.DB_PK, null, true );
		adicCampo( txtNomeEmpRefC, 60, 20, 370, 20, "NomeEmpRefC", "Empresa", ListaCampos.DB_SI, null, true );
		adicCampo( txtDDDRefC, 433, 20, 40, 20, "DDDRefC", "DDD", ListaCampos.DB_SI, false );
		adicCampo( txtFoneRefC, 476, 20, 97, 20, "FoneRefC", "Telefone", ListaCampos.DB_SI, false );
		adicCampo( txtDtMaiorCpRefC, 576, 20, 100, 20, "DtMaiorCp", "Dt.Maior Cp.", ListaCampos.DB_SI, false );
		adicCampo( txtVlrMaiorCpRefC, 7, 60, 100, 20, "VlrMaiorCp", "Vlr.Maior Cp.", ListaCampos.DB_SI, false );
		adicCampo( txtNroParcMaiorCpRefC, 110, 60, 100, 20, "NroParcMaiorCp", "Nro.Parc.Maior Cp.", ListaCampos.DB_SI, false );
		adicCampo( txtDtUltCpRefC, 213, 60, 100, 20, "DtUltCp", "Dt.Ult.Cp.", ListaCampos.DB_SI, false );
		adicCampo( txtVlrUltCpRefC, 316, 60, 100, 20, "VlrUltCp", "Vlr.Ult.Cp.", ListaCampos.DB_SI, false );
		adicCampo( txtNroParcUltCpRefC, 419, 60, 100, 20, "NroParcUltCp", "Nro.Parc.Ult.Cp.", ListaCampos.DB_SI, false );
		adicDB( cbPontualRefC, 521, 60, 70, 20, "Pontual", "Pontual", true );
		adicCampo( txtMediaAtrasoRefC, 7, 100, 100, 20, "MediaAtraso", "Media atraso", ListaCampos.DB_SI, false );
		adicCampo( txtConceitoRefC, 110, 100, 150, 20, "Conceito", "Conceito", ListaCampos.DB_SI, false );
		adicDB( cbAvalistaRefC, 263, 100, 70, 20, "Avalista", "Avalista", true );
		adicCampo( txtInformanteRefC, 336, 100, 150, 20, "Informante", "Informante", ListaCampos.DB_SI, false );
		adicDBLiv( txaObsRefC, 7, 140, 300, 80, "ObsRefC", "Observações", false );

		setListaCampos( true, "CLIREFC", "VD" );
		lcRefC.setQueryInsert( false );
		lcRefC.setQueryCommit( false );
		lcRefC.montaTab();
		tbRefC.setTamColuna( 30, 0 ); // Seq.
		tbRefC.setTamColuna( 200, 1 ); // NomeRefC

		// **************************

		// *************************Socios

		setPainel( pinDetSocios, pnSocios );

		pinDetSocios.setPreferredSize( new Dimension( 600, 120 ) );
		pinDetSocios.add( pinNavSocios, BorderLayout.SOUTH );
		pinDetSocios.add( pinCamposSocios, BorderLayout.CENTER );
		setListaCampos( lcSocios );
		setNavegador( navSocios );

		pnSocios.add( pinDetSocios, BorderLayout.SOUTH );
		pnSocios.add( spnSocios, BorderLayout.CENTER );

		pinNavSocios.adic( navSocios, 0, 0, 270, 25 );

		setPainel( pinCamposSocios );

		adicCampo( txtNomeSocio, 7, 20, 250, 20, "NomeSocio", "Nome", ListaCampos.DB_PK, null, true );
		adicCampo( txtDtNascSocio, 260, 20, 90, 20, "DtNascSocio", "Dt.Nasc.", ListaCampos.DB_SI, false );
		adicCampo( txtRgSocio, 353, 20, 120, 20, "RgSocio", "Rg", ListaCampos.DB_SI, null, false );
		adicCampo( txtSSPSocio, 476, 20, 120, 20, "SSPSocio", "Orgão exp.", ListaCampos.DB_SI, null, false );
		adicCampo( txtCPFSocio, 7, 60, 130, 20, "CPFSocio", "CPF", ListaCampos.DB_SI, null, false );
		adicCampo( txtPercCotasSocio, 140, 60, 50, 20, "PercCotasSocio", "% Cotas", ListaCampos.DB_SI, null, false );

		setListaCampos( false, "CLISOCIOS", "VD" );
		lcSocios.setQueryInsert( false );
		lcSocios.setQueryCommit( false );
		lcSocios.montaTab();
		tbSocios.setTamColuna( 200, 0 );
		tbSocios.setTamColuna( 100, 1 );
		tbSocios.setTamColuna( 120, 2 );
		tbSocios.setTamColuna( 120, 3 );

		// **************************

		// *************************Referencias Pessoais

		setPainel( pinDetRefP, pnRefP );

		pinDetRefP.setPreferredSize( new Dimension( 600, 170 ) );
		pinDetRefP.add( pinNavRefP, BorderLayout.SOUTH );
		pinDetRefP.add( pinCamposRefP, BorderLayout.CENTER );
		setListaCampos( lcRefP );
		setNavegador( navRefP );

		pnRefP.add( pinDetRefP, BorderLayout.SOUTH );
		pnRefP.add( spnRefP, BorderLayout.CENTER );

		pinNavRefP.adic( navRefP, 0, 0, 270, 25 );

		setPainel( pinCamposRefP );

		adicCampo( txtCodRefP, 7, 20, 50, 20, "CodRefP", "Seq.", ListaCampos.DB_PK, null, true );
		adicCampo( txtNomeRefP, 60, 20, 370, 20, "NomeRefP", "Nome", ListaCampos.DB_SI, null, true );
		adicCampo( txtDDDRefP, 433, 20, 40, 20, "DDDRefP", "DDD", ListaCampos.DB_SI, false );
		adicCampo( txtFoneRefP, 476, 20, 97, 20, "FoneRefP", "Telefone", ListaCampos.DB_SI, false );
		adicCampo( txtEndRefP, 7, 60, 330, 20, "EndRefP", "Endereço", ListaCampos.DB_SI, false );
		adicCampo( txtNumRefP, 340, 60, 77, 20, "NumRefP", "Num.", ListaCampos.DB_SI, false );
		adicCampo( txtComplRefP, 420, 60, 149, 20, "ComplRefP", "Compl.", ListaCampos.DB_SI, false );
		adicCampo( txtBairRefP, 7, 100, 210, 20, "BairRefP", "Bairro", ListaCampos.DB_SI, false );
		adicCampo( txtCidRefP, 220, 100, 210, 20, "CidRefP", "Cidade", ListaCampos.DB_SI, false );
		adicCampo( txtUfRefP, 433, 100, 53, 20, "UFRefP", "UF", ListaCampos.DB_SI, false );
		adicCampo( txtCepRefP, 486, 100, 80, 20, "CepRefP", "Cep", ListaCampos.DB_SI, false );

		setListaCampos( true, "CLIREFP", "VD" );
		lcRefP.setQueryInsert( false );
		lcRefP.setQueryCommit( false );
		lcRefP.montaTab();
		tbRefP.setTamColuna( 30, 0 ); // Seq.
		tbRefP.setTamColuna( 200, 1 ); // NomeRefP
		tbRefP.setTamColuna( 50, 2 ); // DDD
		tbRefP.setTamColuna( 200, 4 ); // Endereco
		tbRefP.setTamColuna( 50, 5 ); // Numero
		tbRefP.setTamColuna( 100, 6 ); // Complemento
		tbRefP.setTamColuna( 100, 7 ); // Bairro
		tbRefP.setTamColuna( 100, 8 ); // Cidade
		tbRefP.setTamColuna( 30, 9 ); // UF

		// **************************

		// *************************Pessoas autorizadas a comprar

		setPainel( pinDetAutP, pnAutP );

		pinDetAutP.setPreferredSize( new Dimension( 600, 200 ) );
		pinDetAutP.add( pinNavAutP, BorderLayout.SOUTH );
		pinDetAutP.add( pinCamposAutP, BorderLayout.CENTER );
		setListaCampos( lcAutP );
		setNavegador( navAutP );

		pnAutP.add( pinDetAutP, BorderLayout.SOUTH );
		pnAutP.add( spnAutP, BorderLayout.CENTER );

		pinNavAutP.adic( navAutP, 0, 0, 270, 25 );

		setPainel( pinCamposAutP );

		adicCampo( txtCodAutP, 7, 20, 50, 20, "CodAutP", "Seq.", ListaCampos.DB_PK, null, true );
		adicCampo( txtNomeAutP, 60, 20, 200, 20, "NomeAutP", "Nome", ListaCampos.DB_SI, null, true );
		adicCampo( txtRGAutP, 263, 20, 100, 20, "RGAutP", "RG", ListaCampos.DB_SI, null, false );
		adicCampo( txtSSPAutP, 366, 20, 100, 20, "SSPAutP", "Orgão exp.", ListaCampos.DB_SI, null, false );
		adicCampo( txtCPFAutP, 469, 20, 100, 20, "CPFAutP", "CPF", ListaCampos.DB_SI, null, false );
		adicCampo( txtEndAutP, 7, 60, 330, 20, "EndAutP", "Endereço", ListaCampos.DB_SI, false );
		adicCampo( txtNumAutP, 340, 60, 77, 20, "NumAutP", "Num.", ListaCampos.DB_SI, false );
		adicCampo( txtComplAutP, 420, 60, 149, 20, "ComplAutP", "Compl.", ListaCampos.DB_SI, false );
		adicCampo( txtBairAutP, 7, 100, 210, 20, "BairAutP", "Bairro", ListaCampos.DB_SI, false );
		adicCampo( txtCidAutP, 220, 100, 210, 20, "CidAutP", "Cidade", ListaCampos.DB_SI, false );
		adicCampo( txtUfAutP, 433, 100, 53, 20, "UFAutP", "UF", ListaCampos.DB_SI, false );
		adicCampo( txtCepAutP, 486, 100, 80, 20, "CepAutP", "Cep", ListaCampos.DB_SI, false );
		adicCampo( txtDDDAutP, 7, 140, 40, 20, "DDDAutP", "DDD", ListaCampos.DB_SI, false );
		adicCampo( txtFoneAutP, 50, 140, 97, 20, "FoneAutP", "Telefone", ListaCampos.DB_SI, false );
		lbFuncaoAutP = adicCampo( txtFuncaoAutP, 157, 140, 220, 20, "FuncAutP", "Função", ListaCampos.DB_SI, false );
		setListaCampos( true, "CLIAUTP", "VD" );
		lcAutP.setQueryInsert( false );
		lcAutP.setQueryCommit( false );
		lcAutP.montaTab();
		tbAutP.setTamColuna( 30, 0 ); // Seq.
		tbAutP.setTamColuna( 200, 1 ); // NomeRefP
		tbAutP.setTamColuna( 100, 2 ); // RG
		tbAutP.setTamColuna( 100, 3 ); // SSP
		tbAutP.setTamColuna( 110, 4 ); // CPF
		tbAutP.setTamColuna( 200, 5 ); // Endereco
		tbAutP.setTamColuna( 100, 7 ); // Complemento
		tbAutP.setTamColuna( 100, 8 ); // Bairro
		tbAutP.setTamColuna( 100, 9 ); // Cidade
		tbAutP.setTamColuna( 30, 10 ); // UF
		tbAutP.setTamColuna( 50, 12 ); // DDD
		tbAutP.setTamColuna( 50, 13 ); // Fone
		tbAutP.setTamColuna( 220, 14 ); // Função

		// **************************

		nav.setListaCampos( lcCampos );
		completaTela();
	}

	private void completaTela() {

		habCampos();
		habAbas();
	}

	private void habAbas() {

		tpn2.removeAll();
		tpn.removeAll();

		adicTab( "Geral", pinGeral );

		if ( bFisTipoCli ) {
			adicTab( "Complemento", pnFicha );
			navFicha.setListaCampos( lcFicha );
			lcFicha.setNavegador( navFicha );
		}
		if ( bJurTipoCli ) {
			adicTab( "Jurídica", pinJuridica );
		}
		if ( bVeicTipoCli ) {
			adicTab( "Veículos", pnVeic );
		}
		if ( bImovTipoCli ) {
			adicTab( "Imóveis", pnImov );
		}
		if ( bTerraTipoCli ) {
			adicTab( "Terras", pnTerras );
		}
		if ( bJurTipoCli ) {
			adicTab( "Bancos", pnBancos );
			adicTab( "Ref.Com.", pnRefC );
			adicTab( "Sócios", pnSocios );
		}
		if ( bRefPesTipoCli ) {
			adicTab( "Ref.Pes.", pnRefP );
		}
		if ( bPessAutTipoCli ) {
			adicTab( "Pess.Autoriz.", pnAutP );
		}
		if ( bFilTipoCli ) {
			tpn2.addTab( "Filiação", pinFiliacao );
		}
		if ( bLocTrabTipoCli ) {
			tpn2.addTab( "Trabalho", pinTrabalho );
		}
		if ( bConjTipoCli ) {
			tpn2.addTab( "Cônjuge", pinConjuge );
		}
		if ( bAvalTipoCli ) {
			tpn2.addTab( "Avalista", pinAvalista );
		}
	}

	private void setaFoco() {

		if ( bAchou ) {

			txtRazCli.requestFocus( true );
		}
		else {

			txtCodCli.requestFocus( false );
		}
	}

	private void habCampos() {

		// Pessoa Jurídica
		if ( bJurTipoCli ) {
			txtFuncaoAutP.setVisible( true );
			lbFuncaoAutP.setVisible( true );
		}
		else {
			txtFuncaoAutP.setVisible( false );
			lbFuncaoAutP.setVisible( false );
		}

		// Pessoa Física
		txtNatCli.setVisible( bFisTipoCli );
		txtUFNatCli.setVisible( bFisTipoCli );
		txtTempoResCli.setVisible( bFisTipoCli );
		cbEstCivCli.setVisible( bFisTipoCli );
		txtApelidoCli.setVisible( bFisTipoCli );
		lbNatCli.setVisible( bFisTipoCli );
		lbUfNatCli.setVisible( bFisTipoCli );
		lbTempoResCli.setVisible( bFisTipoCli );
		lbEstadoCivil.setVisible( bFisTipoCli );
		lbApelidoCli.setVisible( bFisTipoCli );

		// Filiação
		lbPaiCli.setVisible( bFilTipoCli );
		lbMaeCli.setVisible( bFilTipoCli );
		lbRgPaiCli.setVisible( bFilTipoCli );
		lbSSPPaiCli.setVisible( bFilTipoCli );
		lbSSPMaeCli.setVisible( bFilTipoCli );
		lbRgMaeCli.setVisible( bFilTipoCli );
		txtPaiCli.setVisible( bFilTipoCli );
		txtMaeCli.setVisible( bFilTipoCli );
		txtRgPaiCli.setVisible( bFilTipoCli );
		txtRgMaeCli.setVisible( bFilTipoCli );
		txtSSPPaiCli.setVisible( bFilTipoCli );
		txtSSPMaeCli.setVisible( bFilTipoCli );
		pinFiliacao.setEnabled( bFilTipoCli );
		// Trabalho
		lbEmpTrabCli.setVisible( bLocTrabTipoCli );
		lbFuncaoTrabCli.setVisible( bLocTrabTipoCli );
		lbDtAdmTrabCli.setVisible( bLocTrabTipoCli );
		lbEndTrabCli.setVisible( bLocTrabTipoCli );
		lbNumTrabCli.setVisible( bLocTrabTipoCli );
		lbComplTrabCli.setVisible( bLocTrabTipoCli );
		lbBairTrabCli.setVisible( bLocTrabTipoCli );
		lbCidTrabCli.setVisible( bLocTrabTipoCli );
		lbCepTrabCli.setVisible( bLocTrabTipoCli );
		lbUfTrabCli.setVisible( bLocTrabTipoCli );
		lbDDDTrabCli.setVisible( bLocTrabTipoCli );
		lbFoneTrabCli.setVisible( bLocTrabTipoCli );
		lbRamalTrabCli.setVisible( bLocTrabTipoCli );
		lbRendaTrabCli.setVisible( bLocTrabTipoCli );
		lbOutRendaCli.setVisible( bLocTrabTipoCli );
		lbFontRendaCli.setVisible( bLocTrabTipoCli );

		txtEmpTrabCli.setVisible( bLocTrabTipoCli );
		txtFuncaoTrabCli.setVisible( bLocTrabTipoCli );
		txtDtAdmTrabCli.setVisible( bLocTrabTipoCli );
		txtEndTrabCli.setVisible( bLocTrabTipoCli );
		txtNumTrabCli.setVisible( bLocTrabTipoCli );
		txtComplTrabCli.setVisible( bLocTrabTipoCli );
		txtBairTrabCli.setVisible( bLocTrabTipoCli );
		txtCidTrabCli.setVisible( bLocTrabTipoCli );
		txtCepTrabCli.setVisible( bLocTrabTipoCli );
		txtUfTrabCli.setVisible( bLocTrabTipoCli );
		txtDDDTrabCli.setVisible( bLocTrabTipoCli );
		txtFoneTrabCli.setVisible( bLocTrabTipoCli );
		txtRamalTrabCli.setVisible( bLocTrabTipoCli );
		txtRendaTrabCli.setVisible( bLocTrabTipoCli );
		txtOutRendaCli.setVisible( bLocTrabTipoCli );
		txtFontRendaCli.setVisible( bLocTrabTipoCli );
		pinTrabalho.setEnabled( false );

	}

	public void afterCarrega( CarregaEvent cevt ) {

		if ( txtDtIniTr.getVlrString().equals( "" ) ) {

			txtDtIniTr.setVlrDate( new Date() );
		}

		buscaAdicionais();
		setaFoco();
	}

	private void buscaEstadoCivil() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();

		Vector<String> vDesc = new Vector<String>();
		Vector<Integer> vCod = new Vector<Integer>();

		try {

			sSQL.append( "SELECT IT.CODTB,IT.CODITTB,IT.DESCITTB " );
			sSQL.append( "FROM SGITTABELA IT, SGTABELA TB " );
			sSQL.append( "WHERE TB.SIGLATB='EST_CIVIL' AND TB.CODEMP=? AND TB.CODFILIAL=? AND " );
			sSQL.append( "IT.CODEMP=TB.CODEMP AND IT.CODFILIAL=TB.CODFILIAL AND IT.CODTB=TB.CODTB ORDER BY IT.DESCITTB" );

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGTABELA" ) );

			rs = ps.executeQuery();

			int i = 0;

			vCod.addElement( new Integer( 0 ) );
			vDesc.addElement( "<--Selecione-->" );

			while ( rs.next() ) {

				vCod.addElement( new Integer( rs.getInt( 2 ) ) );
				vDesc.addElement( rs.getString( 3 ) );

				if ( i == 0 ) {
					txtCodTb.setVlrInteger( new Integer( rs.getInt( 1 ) ) );
				}

				i++;
			}

			if ( rs != null ) {
				txtCodEmpTb.setVlrInteger( new Integer( Aplicativo.iCodEmp ) );
				txtCodFilialTb.setVlrInteger( new Integer( ListaCampos.getMasterFilial( "SGTABELA" ) ) );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Não foi possível carregar estado civil!\n" + err.getMessage(), true, con, err );
		} finally {
			cbEstCivCli.setItensGeneric( vDesc, vCod );
			rs = null;
			ps = null;
			sSQL = null;
		}

	}

	private void buscaAdicionais() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();

		try {

			sSQL.append( "SELECT TC.FISTIPOCLI,TC.JURTIPOCLI,TC.CHEQTIPOCLI,TC.FILTIPOCLI,TC.LOCTRABTIPOCLI," );
			sSQL.append( "TC.REFCOMLTIPOCLI,TC.BANCTIPOCLI,TC.REFPESTIPOCLI,TC.CONJTIPOCLI,TC.VEICTIPOCLI," );
			sSQL.append( "TC.IMOVTIPOCLI,TC.TERRATIPOCLI,TC.PESAUTCPTIPOCLI,TC.AVALTIPOCLI,TC.SOCIOTIPOCLI " );
			sSQL.append( "FROM VDTIPOCLI TC,VDCLIENTE CLI " );
			sSQL.append( "WHERE TC.CODEMP=? AND TC.CODFILIAL=? AND CLI.CODCLI=? " );
			sSQL.append( "AND CLI.CODEMPTI=TC.CODEMP AND CLI.CODFILIALTI=TC.CODFILIAL AND TC.CODTIPOCLI=CLI.CODTIPOCLI" );

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
			ps.setInt( 3, txtCodCli.getVlrInteger().intValue() );

			rs = ps.executeQuery();

			if ( rs.next() ) {

				bFisTipoCli = "S".equals( rs.getString( 1 ) );
				bJurTipoCli = "S".equals( rs.getString( 2 ) );
				bFilTipoCli = "S".equals( rs.getString( 4 ) );
				bLocTrabTipoCli = "S".equals( rs.getString( 5 ) );
				bRefPesTipoCli = "S".equals( rs.getString( 8 ) );
				bConjTipoCli = "S".equals( rs.getString( 9 ) );
				bVeicTipoCli = "S".equals( rs.getString( 10 ) );
				bImovTipoCli = "S".equals( rs.getString( 11 ) );
				bTerraTipoCli = "S".equals( rs.getString( 12 ) );
				bPessAutTipoCli = "S".equals( rs.getString( 13 ) );
				bAvalTipoCli = "S".equals( rs.getString( 14 ) );

				bAchou = true;
			}
			else {

				bAchou = false;
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Não foi possível carregar pessoa cliente!\n" + err.getMessage(), true, con, err );
		} finally {
			rs = null;
			ps = null;
			sSQL = null;
		}
		habCampos();
		habAbas();
		setaFoco();

	}

	public void beforeCarrega( CarregaEvent cevt ) {

	}

	private boolean postDet( ListaCampos lc ) {

		boolean bRet = true;
		if ( lc == lcCampos ) {
			if ( ( lcFicha.getStatus() == ListaCampos.LCS_EDIT ) || ( lcFicha.getStatus() == ListaCampos.LCS_INSERT ) )
				bRet = lcFicha.post();
		}
		return bRet;
	}

	public void afterPost( PostEvent pevt ) {

	}

	public void beforePost( PostEvent pevt ) {

		boolean bRet = true;
		bRet = postDet( pevt.getListaCampos() );
		if ( !bRet ) {
			pevt.cancela();
		}
		else {
			if ( cbEstCivCli.getSelectedItem().equals( "<--Selecione-->" ) ) {
				txtCodEmpTb.setVlrString( "" );
				txtCodFilialTb.setVlrString( "" );
				cbEstCivCli.setVlrInteger( null );
				txtCodTb.setVlrString( "" );
			}
		}
	}

	private void setEditInsert( ListaCampos lc ) {

		if ( lc == lcFicha ) {
			if ( ! ( lcCampos.getStatus() == ListaCampos.LCS_EDIT || lcCampos.getStatus() == ListaCampos.LCS_INSERT ) ) {
				lcCampos.edit();
			}
		}
	}

	public void afterEdit( EditEvent eevt ) {

		setEditInsert( eevt.getListaCampos() );

	}

	public void beforeEdit( EditEvent eevt ) {

	}

	public void edit( EditEvent eevt ) {

	}

	public void afterInsert( InsertEvent ievt ) {

		setEditInsert( ievt.getListaCampos() );
	}

	public void beforeInsert( InsertEvent ievt ) {

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcFicha.setConexao( cn );
		lcTipoCli.setConexao( cn );
		lcTipoCred.setConexao( cn );
		lcTipoCob.setConexao( cn );
		lcRefP.setConexao( cn );
		lcAutP.setConexao( cn );
		lcVeic.setConexao( cn );
		lcImov.setConexao( cn );
		lcTerras.setConexao( cn );
		lcBancos.setConexao( cn );
		lcBanco.setConexao( cn );
		lcSocios.setConexao( cn );
		lcRefC.setConexao( cn );
		buscaEstadoCivil();
	}
}
