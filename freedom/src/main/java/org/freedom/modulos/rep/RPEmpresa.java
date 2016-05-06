/**
 * @version 02/2007 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * @author Alex Rodrigues<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)RPEmpresa.java <BR>
 * 
 *                    Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                    modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                    na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                    Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                    sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                    Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                    Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                    de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                    Tela de cadastro dos dados da empresa.
 * 
 */

package org.freedom.modulos.rep;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.component.PainelImagem;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FTabDados;

public class RPEmpresa extends FTabDados {

	private static final long serialVersionUID = 1L;

	private final JPanelPad panelEmpresa = new JPanelPad();

	private final JPanelPad panelFilial = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad panelCamposFilial = new JPanelPad();

	private final JPanelPad panelTabFilial = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad panelNavegadorFilial = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTextFieldPad txtCodEmp = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtRazEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 45, 0 );

	private JTextFieldPad txtNomeEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 45, 0 );

	private JTextFieldPad txtEndEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 45, 0 );

	private JTextFieldPad txtNumEmp = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtCnpjEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtInscEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private JTextFieldPad txtCidEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtBairEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtCepEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtDDDEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtFoneEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 12, 0 );

	private JTextFieldPad txtFaxEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtUFEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtEmailEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	// private JTextFieldPad txtCodMoeda = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	// private JTextFieldFK txtDescMoeda = new JTextFieldFK( JTextFieldPad.TP_STRING, 45, 0 );

	private PainelImagem imLogoEmp = new PainelImagem( 65000 );

	private JTextFieldPad txtCodFilial = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtRazFilial = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNomeFilial = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtEndFilial = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNumFilial = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtCnpjFilial = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtInscFilial = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private JTextFieldPad txtCidFilial = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtBairFilial = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtCepFilial = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtDDDFilial = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtFoneFilial = new JTextFieldPad( JTextFieldPad.TP_STRING, 12, 0 );

	private JTextFieldPad txtFaxFilial = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtUFFilial = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtEmailFilial = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JCheckBoxPad cbMatriz = new JCheckBoxPad( "     Filial Matriz", "S", "N" );

	private JTablePad tabFilial = new JTablePad();

	private ListaCampos lcFilial = new ListaCampos( this );

	// private ListaCampos lcMoeda = new ListaCampos( this, "MO" );

	private Navegador navFilial = new Navegador( true );

	private JButtonPad btCopiarEmp = new JButtonPad( "copiar dados", Icone.novo( "btReset.png" ) );

	public RPEmpresa() {

		super( false );
		setTitulo( "Cadastro da Empresa" );
		setAtribos( 50, 50, 550, 480 );

		lcCampos.setUsaME( false );
		lcFilial.setUsaME( false );

		lcFilial.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcFilial );

		lcFilial.setTabela( tabFilial );

		montaAbaEmpresa();
		setListaCampos( false, "EMPRESA", "SG" );

		txtCnpjEmp.setMascara( JTextFieldPad.MC_CNPJ );
		txtCepEmp.setMascara( JTextFieldPad.MC_CEP );
		txtFoneEmp.setMascara( JTextFieldPad.MC_FONE );
		txtFaxEmp.setMascara( JTextFieldPad.MC_FONE );

		setListaCampos( lcFilial );
		setNavegador( navFilial );

		montaAbaFilial();
		setListaCampos( true, "FILIAL", "SG" );
		lcFilial.setOrdem( "RazFilial" );
		lcFilial.montaTab();

		tabFilial.setTamColuna( 80, 0 );
		tabFilial.setTamColuna( 205, 1 );
		tabFilial.setTamColuna( 205, 2 );
		tabFilial.setTamColuna( 40, 3 );
		tabFilial.setTamColuna( 120, 4 );
		tabFilial.setTamColuna( 120, 5 );

		tabFilial.setTamColuna( 200, 6 );
		tabFilial.setTamColuna( 100, 7 );
		tabFilial.setTamColuna( 100, 8 );
		tabFilial.setTamColuna( 100, 9 );
		tabFilial.setTamColuna( 50, 10 );
		tabFilial.setTamColuna( 50, 11 );
		tabFilial.setTamColuna( 100, 12 );
		tabFilial.setTamColuna( 100, 13 );
		tabFilial.setTamColuna( 200, 14 );

		txtCnpjFilial.setMascara( JTextFieldPad.MC_CNPJ );
		txtCepFilial.setMascara( JTextFieldPad.MC_CEP );
		txtFoneFilial.setMascara( JTextFieldPad.MC_FONE );
		txtFaxFilial.setMascara( JTextFieldPad.MC_FONE );

		btCopiarEmp.addActionListener( this );

	}

	private void montaAbaEmpresa() {

		/*******************
		 * EMPRESA *
		 *******************/

		adicTab( "Empresa", panelEmpresa );
		setPainel( panelEmpresa );

		adicCampo( txtCodEmp, 7, 30, 100, 20, "CodEmp", "Cód.emp.", ListaCampos.DB_PK, true );
		adicCampo( txtRazEmp, 110, 30, 260, 20, "RazEmp", "Razão social da empresa", ListaCampos.DB_SI, true );

		adicDB( imLogoEmp, 380, 30, 140, 140, "FotoEmp", "Logomarca", true );

		adicCampo( txtNomeEmp, 7, 70, 363, 20, "NomeEmp", "Nome fantasia", ListaCampos.DB_SI, true );

		adicCampo( txtCnpjEmp, 7, 110, 180, 20, "CnpjEmp", "Cnpj", ListaCampos.DB_SI, true );
		adicCampo( txtInscEmp, 190, 110, 180, 20, "InscEmp", "Inscrição Estadual", ListaCampos.DB_SI, true );

		adicCampo( txtEndEmp, 7, 150, 280, 20, "EndEmp", "Endereço", ListaCampos.DB_SI, false );
		adicCampo( txtNumEmp, 290, 150, 80, 20, "NumEmp", "Número", ListaCampos.DB_SI, false );

		adicCampo( txtCidEmp, 7, 190, 112, 20, "CidEmp", "Cidade", ListaCampos.DB_SI, false );
		adicCampo( txtBairEmp, 122, 190, 112, 20, "BairEmp", "Bairro", ListaCampos.DB_SI, false );
		adicCampo( txtCepEmp, 237, 190, 80, 20, "CepEmp", "Cep", ListaCampos.DB_SI, false );
		adicCampo( txtUFEmp, 320, 190, 50, 20, "UFEmp", "UF", ListaCampos.DB_SI, false );

		adicCampo( txtDDDEmp, 7, 230, 52, 20, "DDDEmp", "DDD", ListaCampos.DB_SI, false );
		adicCampo( txtFoneEmp, 62, 230, 152, 20, "FoneEmp", "Telefone", ListaCampos.DB_SI, false );
		adicCampo( txtFaxEmp, 217, 230, 152, 20, "FaxEmp", "Fax", ListaCampos.DB_SI, false );

		adicCampo( txtEmailEmp, 7, 270, 363, 20, "EmailEmp", "E-Mail", ListaCampos.DB_SI, false );

		// adicCampo( txtCodMoeda, 7, 310, 100, 20, "CodMoeda", "Cód.moeda", ListaCampos.DB_PK, txtDescMoeda, false );
		// adicDescFK( txtDescMoeda, 110, 310, 260, 20, "DescMoeda", "Descrição da moeda" );
	}

	private void montaAbaFilial() {

		/*******************
		 * FILIAL *
		 *******************/

		adicTab( "Filial", panelFilial );
		setPainel( panelCamposFilial, panelFilial );

		panelFilial.add( panelTabFilial, BorderLayout.NORTH );
		panelFilial.add( panelCamposFilial, BorderLayout.CENTER );
		panelFilial.add( panelNavegadorFilial, BorderLayout.SOUTH );

		JScrollPane scrollTab = new JScrollPane( tabFilial );
		scrollTab.setPreferredSize( new Dimension( 550, 80 ) );
		panelTabFilial.add( scrollTab, BorderLayout.CENTER );

		adicCampo( txtCodFilial, 7, 30, 100, 20, "CodFilial", "Cód.filial.", ListaCampos.DB_PK, true );
		adicCampo( txtRazFilial, 110, 30, 260, 20, "RazFilial", "Razão social da Filial", ListaCampos.DB_SI, true );

		adicCampo( txtNomeFilial, 7, 70, 363, 20, "NomeFilial", "Nome fantasia", ListaCampos.DB_SI, true );

		adicDB( cbMatriz, 400, 30, 120, 20, "MzFilial", "", false );

		adicCampo( txtCnpjFilial, 373, 70, 147, 20, "CnpjFilial", "Cnpj", ListaCampos.DB_SI, true );

		adicCampo( txtInscFilial, 373, 110, 147, 20, "InscFilial", "Inscrição Estadual", ListaCampos.DB_SI, true );

		adicCampo( txtEndFilial, 7, 110, 280, 20, "EndFilial", "Endereço", ListaCampos.DB_SI, false );
		adicCampo( txtNumFilial, 290, 110, 80, 20, "NumFilial", "Número", ListaCampos.DB_SI, false );

		adicCampo( txtCidFilial, 7, 150, 112, 20, "CidFilial", "Cidade", ListaCampos.DB_SI, false );
		adicCampo( txtBairFilial, 122, 150, 112, 20, "BairFilial", "Bairro", ListaCampos.DB_SI, false );
		adicCampo( txtCepFilial, 237, 150, 80, 20, "CepFilial", "Cep", ListaCampos.DB_SI, false );
		adicCampo( txtUFFilial, 320, 150, 50, 20, "UFFilial", "UF", ListaCampos.DB_SI, false );

		adicCampo( txtDDDFilial, 7, 190, 52, 20, "DDDFilial", "DDD", ListaCampos.DB_SI, false );
		adicCampo( txtFoneFilial, 62, 190, 152, 20, "FoneFilial", "Telefone", ListaCampos.DB_SI, false );
		adicCampo( txtFaxFilial, 217, 190, 152, 20, "FaxFilial", "Fax", ListaCampos.DB_SI, false );

		adicCampo( txtEmailFilial, 7, 230, 363, 20, "EmailFilial", "E-Mail", ListaCampos.DB_SI, false );

		adic( btCopiarEmp, 380, 225, 135, 30 );

		navFilial.setPreferredSize( new Dimension( 290, 27 ) );
		panelNavegadorFilial.add( navFilial, BorderLayout.WEST );
		panelNavegadorFilial.setBorder( BorderFactory.createEtchedBorder() );
	}

	private void copiarEmpresa() {

		if ( Funcoes.mensagemConfirma( this, "Confirma a cópia dos dados da empresa?" ) == JOptionPane.YES_OPTION ) {

			lcFilial.edit();

			txtRazFilial.setVlrString( txtRazEmp.getVlrString() );
			txtNomeFilial.setVlrString( txtNomeEmp.getVlrString() );
			txtEndFilial.setVlrString( txtEndEmp.getVlrString() );
			txtNumFilial.setVlrString( txtNumEmp.getVlrString() );
			txtCnpjFilial.setVlrString( txtCnpjEmp.getVlrString() );
			txtInscFilial.setVlrString( txtInscEmp.getVlrString() );
			txtCidFilial.setVlrString( txtCidEmp.getVlrString() );
			txtBairFilial.setVlrString( txtBairEmp.getVlrString() );
			txtCepFilial.setVlrString( txtCepEmp.getVlrString() );
			txtDDDFilial.setVlrString( txtDDDEmp.getVlrString() );
			txtFoneFilial.setVlrString( txtFoneEmp.getVlrString() );
			txtFaxFilial.setVlrString( txtFaxEmp.getVlrString() );
			txtUFFilial.setVlrString( txtUFEmp.getVlrString() );
			txtEmailFilial.setVlrString( txtEmailEmp.getVlrString() );
		}
	}

	public void actionPerformed( ActionEvent e ) {

		if ( e.getSource() == btCopiarEmp ) {
			copiarEmpresa();
		}

		super.actionPerformed( e );
	}

	@ Override
	public synchronized void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		txtCodEmp.setVlrInteger( new Integer( Aplicativo.iCodEmp ) );
		lcCampos.carregaDados();
	}
}
