/**
 * @version 05/01/2004 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.tmk <BR>
 *         Classe: @(#)FHistorico.java <BR>
 * 
 *         Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *         modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *         na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *         Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *         sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *         Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *         Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *         de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *         Classe para gravação de histórico de contatos realizados.
 * 
 */

package org.freedom.modulos.crm.view.frame.crud.special;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.modulos.crm.view.dialog.utility.DLNovoHist;
import org.freedom.modulos.crm.view.frame.crud.tabbed.FContato;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCliente;

public class FHistorico extends FFilho implements CarregaListener, ActionListener, ChangeListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCabCont = new JPanelPad( 530, 200 );

	private JPanelPad pinCabCli = new JPanelPad( 530, 200 );

	private JPanelPad pnCabCont = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnCabCli = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnCont = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnCli = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnRodCont = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnBotCont = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 2 ) );

	private JTabbedPanePad tpnHist = new JTabbedPanePad();

	private JTablePad tabCont = new JTablePad();

	private JTablePad tabCli = new JTablePad();

	private JScrollPane spnCont = new JScrollPane( tabCont );

	private JScrollPane spnCli = new JScrollPane( tabCli );

	private JTextFieldPad txtCodCont = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeCont = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtNomeCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtDDDCont = new JTextFieldFK( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldFK txtDDDCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldFK txtTelCont = new JTextFieldFK( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldFK txtTelCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldFK txtFaxCont = new JTextFieldFK( JTextFieldPad.TP_STRING, 9, 0 );

	private JTextFieldFK txtFaxCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 9, 0 );

	private JTextFieldFK txtEmpCont = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtEmpCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldFK txtEmailCont = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtEmailCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtEndCont = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtEndCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtNumCont = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNumCli = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtComplCont = new JTextFieldFK( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldFK txtComplCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldFK txtBairCont = new JTextFieldFK( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldFK txtBairCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldFK txtCidCont = new JTextFieldFK( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldFK txtCidCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldFK txtUfCont = new JTextFieldFK( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtUfCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtContCont = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtContCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JButtonPad btNovo = new JButtonPad( Icone.novo( "btNovo.png" ) );

	private JButtonPad btExcluir = new JButtonPad( Icone.novo( "btExcluir.png" ) );

	private JButtonPad btSair = new JButtonPad( "Sair", Icone.novo( "btSair.png" ) );

	private ListaCampos lcCont = new ListaCampos( this );

	private ListaCampos lcCli = new ListaCampos( this );

	private Vector<String> vCodHists = new Vector<String>();

	private Vector<String> vCodAtends = new Vector<String>();

	private JTablePad tabTemp = null;

	public FHistorico() {

		super( false );
		setTitulo( "Histórico de contatos" );
		setAtribos( 20, 20, 597, 505 );

		lcCont.add( new GuardaCampo( txtCodCont, "CodCto", "Cód.cto.", ListaCampos.DB_PK, txtNomeCont, false ) );
		lcCont.add( new GuardaCampo( txtNomeCont, "NomeCto", "Nome do contato", ListaCampos.DB_SI, false ) );
		lcCont.add( new GuardaCampo( txtDDDCont, "dddCto", "DDD", ListaCampos.DB_SI, false ) );
		lcCont.add( new GuardaCampo( txtTelCont, "FoneCto", "Fone.", ListaCampos.DB_SI, false ) );
		lcCont.add( new GuardaCampo( txtFaxCont, "FaxCto", "Fax.", ListaCampos.DB_SI, false ) );
		lcCont.add( new GuardaCampo( txtEmpCont, "RazCto", "Empresa", ListaCampos.DB_SI, false ) );
		lcCont.add( new GuardaCampo( txtEmailCont, "EmailCto", "E-Mail", ListaCampos.DB_SI, false ) );
		lcCont.add( new GuardaCampo( txtContCont, "ContCto", "Contato", ListaCampos.DB_SI, false ) );
		lcCont.add( new GuardaCampo( txtEndCont, "EndCto", "Endereco", ListaCampos.DB_SI, false ) );
		lcCont.add( new GuardaCampo( txtNumCont, "NumCto", "Numero", ListaCampos.DB_SI, false ) );
		lcCont.add( new GuardaCampo( txtComplCont, "ComplCto", "Compl.", ListaCampos.DB_SI, false ) );
		lcCont.add( new GuardaCampo( txtBairCont, "BairCto", "Bairro", ListaCampos.DB_SI, false ) );
		lcCont.add( new GuardaCampo( txtCidCont, "CidCto", "Cidade", ListaCampos.DB_SI, false ) );
		lcCont.add( new GuardaCampo( txtUfCont, "UfCto", "UF", ListaCampos.DB_SI, false ) );
		lcCont.montaSql( false, "CONTATO", "TK" );
		lcCont.setReadOnly( true );
		txtCodCont.setTabelaExterna( lcCont, FContato.class.getCanonicalName() );
		txtCodCont.setFK( true );
		txtCodCont.setNomeCampo( "CodCto" );
		txtTelCont.setMascara( JTextFieldPad.MC_FONE );
		txtFaxCont.setMascara( JTextFieldPad.MC_FONE );

		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, txtNomeCli, false ) );
		lcCli.add( new GuardaCampo( txtNomeCli, "NomeCli", "Nome do cliente", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtTelCli, "FoneCli", "Fone.", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtFaxCli, "FaxCli", "Fax.", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtDDDCli, "DDDCli", "DDD", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtEmpCli, "RazCli", "Empresa", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtEmailCli, "EmailCli", "E-Mail", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtEndCli, "EndCli", "Endereco", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtNumCli, "NumCli", "Numero", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtComplCli, "ComplCli", "Compl.", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtBairCli, "BairCli", "Bairro", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtCidCli, "CidCli", "Cidade", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtUfCli, "UfCli", "UF", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtContCli, "ContCli", "Contato", ListaCampos.DB_SI, false ) );
		lcCli.montaSql( false, "CLIENTE", "VD" );
		lcCli.setReadOnly( true );
		txtCodCli.setTabelaExterna( lcCli, FCliente.class.getCanonicalName() );
		txtCodCli.setFK( true );
		txtCodCli.setNomeCampo( "CodCli" );
		txtTelCli.setMascara( JTextFieldPad.MC_FONE );
		txtFaxCli.setMascara( JTextFieldPad.MC_FONE );

		tpnHist.add( "Contato", pnCont );
		tpnHist.add( "Cliente", pnCli );

		pnCabCont.setPreferredSize( new Dimension( 500, 200 ) );
		pnCabCont.add( pinCabCont, BorderLayout.CENTER );
		pnCont.setPreferredSize( new Dimension( 500, 310 ) );
		pnCont.add( pnCabCont, BorderLayout.NORTH );
		pnCont.add( spnCont, BorderLayout.CENTER );

		pnCabCli.setPreferredSize( new Dimension( 500, 200 ) );
		pnCabCli.add( pinCabCli, BorderLayout.CENTER );
		pnCli.setPreferredSize( new Dimension( 500, 310 ) );
		pnCli.add( pnCabCli, BorderLayout.NORTH );
		pnCli.add( spnCli, BorderLayout.CENTER );

		getTela().add( tpnHist, BorderLayout.CENTER );

		pinCabCont.adic( new JLabelPad( "Cod.cto." ), 7, 10, 250, 20 );
		pinCabCont.adic( txtCodCont, 7, 30, 80, 20 );
		pinCabCont.adic( new JLabelPad( "Nome do contato" ), 90, 10, 250, 20 );
		pinCabCont.adic( txtNomeCont, 90, 30, 197, 20 );
		pinCabCont.adic( new JLabelPad( "DDD" ), 290, 10, 27, 20 );
		pinCabCont.adic( txtDDDCont, 290, 30, 27, 20 );
		pinCabCont.adic( new JLabelPad( "Tel." ), 320, 10, 97, 20 );
		pinCabCont.adic( txtTelCont, 320, 30, 97, 20 );
		pinCabCont.adic( new JLabelPad( "Fax" ), 420, 10, 97, 20 );
		pinCabCont.adic( txtFaxCont, 420, 30, 97, 20 );
		pinCabCont.adic( new JLabelPad( "Empresa" ), 7, 50, 270, 20 );
		pinCabCont.adic( txtEmpCont, 7, 70, 270, 20 );
		pinCabCont.adic( new JLabelPad( "E-mail" ), 280, 50, 207, 20 );
		pinCabCont.adic( txtEmailCont, 280, 70, 237, 20 );
		pinCabCont.adic( new JLabelPad( "Endereco" ), 7, 90, 200, 20 );
		pinCabCont.adic( txtEndCont, 7, 110, 200, 20 );
		pinCabCont.adic( new JLabelPad( "Num." ), 210, 90, 67, 20 );
		pinCabCont.adic( txtNumCont, 210, 110, 67, 20 );
		pinCabCont.adic( new JLabelPad( "Compl." ), 280, 90, 47, 20 );
		pinCabCont.adic( txtComplCont, 280, 110, 47, 20 );
		pinCabCont.adic( new JLabelPad( "Bairro." ), 330, 90, 157, 20 );
		pinCabCont.adic( txtBairCont, 330, 110, 187, 20 );
		pinCabCont.adic( new JLabelPad( "Cidade." ), 7, 130, 180, 20 );
		pinCabCont.adic( txtCidCont, 7, 150, 180, 20 );
		pinCabCont.adic( new JLabelPad( "UF" ), 190, 130, 30, 20 );
		pinCabCont.adic( txtUfCont, 190, 150, 30, 20 );
		pinCabCont.adic( new JLabelPad( "Falar com:" ), 223, 130, 100, 20 );
		pinCabCont.adic( txtContCont, 223, 150, 295, 20 );

		pinCabCli.adic( new JLabelPad( "Cod.cli." ), 7, 10, 250, 20 );
		pinCabCli.adic( txtCodCli, 7, 30, 80, 20 );
		pinCabCli.adic( new JLabelPad( "Nome do cliente" ), 90, 10, 250, 20 );
		pinCabCli.adic( txtNomeCli, 90, 30, 197, 20 );
		pinCabCli.adic( new JLabelPad( "DDD" ), 290, 10, 27, 20 );
		pinCabCli.adic( txtDDDCli, 290, 30, 27, 20 );
		pinCabCli.adic( new JLabelPad( "Tel." ), 320, 10, 97, 20 );
		pinCabCli.adic( txtTelCli, 320, 30, 97, 20 );
		pinCabCli.adic( new JLabelPad( "Fax" ), 420, 10, 97, 20 );
		pinCabCli.adic( txtFaxCli, 420, 30, 97, 20 );
		pinCabCli.adic( new JLabelPad( "Empresa" ), 7, 50, 270, 20 );
		pinCabCli.adic( txtEmpCli, 7, 70, 270, 20 );
		pinCabCli.adic( new JLabelPad( "E-mail" ), 280, 50, 237, 20 );
		pinCabCli.adic( txtEmailCli, 280, 70, 237, 20 );
		pinCabCli.adic( new JLabelPad( "Endereco" ), 7, 90, 200, 20 );
		pinCabCli.adic( txtEndCli, 7, 110, 200, 20 );
		pinCabCli.adic( new JLabelPad( "Num." ), 210, 90, 67, 20 );
		pinCabCli.adic( txtNumCli, 210, 110, 67, 20 );
		pinCabCli.adic( new JLabelPad( "Compl." ), 280, 90, 47, 20 );
		pinCabCli.adic( txtComplCli, 280, 110, 47, 20 );
		pinCabCli.adic( new JLabelPad( "Bairro." ), 330, 90, 157, 20 );
		pinCabCli.adic( txtBairCli, 330, 110, 187, 20 );
		pinCabCli.adic( new JLabelPad( "Cidade." ), 7, 130, 180, 20 );
		pinCabCli.adic( txtCidCli, 7, 150, 180, 20 );
		pinCabCli.adic( new JLabelPad( "UF" ), 190, 130, 30, 20 );
		pinCabCli.adic( txtUfCli, 190, 150, 30, 20 );
		pinCabCli.adic( new JLabelPad( "Contato" ), 223, 130, 100, 20 );
		pinCabCli.adic( txtContCli, 223, 150, 295, 20 );

		tabCont.adicColuna( "Ind." );
		tabCont.adicColuna( "Sit." );
		tabCont.adicColuna( "Tipo" );
		tabCont.adicColuna( "Data" );
		tabCont.adicColuna( "Histórico" );
		tabCont.adicColuna( "Usuário" );
		tabCont.adicColuna( "Hora" );

		tabCont.setTamColuna( 40, 0 );
		tabCont.setTamColuna( 30, 1 );
		tabCont.setTamColuna( 30, 2 );
		tabCont.setTamColuna( 90, 3 );
		tabCont.setTamColuna( 200, 4 );
		tabCont.setTamColuna( 130, 5 );
		tabCont.setTamColuna( 75, 6 );

		tabCli.adicColuna( "Ind." );
		tabCli.adicColuna( "Sit." );
		tabCli.adicColuna( "tipo" );
		tabCli.adicColuna( "Data" );
		tabCli.adicColuna( "Histórico" );
		tabCli.adicColuna( "Usuário" );
		tabCli.adicColuna( "Hora" );

		tabCli.setTamColuna( 40, 0 );
		tabCli.setTamColuna( 30, 1 );
		tabCli.setTamColuna( 30, 2 );
		tabCli.setTamColuna( 90, 3 );
		tabCli.setTamColuna( 200, 4 );
		tabCli.setTamColuna( 130, 5 );
		tabCli.setTamColuna( 75, 6 );

		pnBotCont.setPreferredSize( new Dimension( 60, 30 ) );
		pnBotCont.add( btNovo );
		pnBotCont.add( btExcluir );

		pnRodCont.add( pnBotCont, BorderLayout.WEST );

		btSair.setPreferredSize( new Dimension( 110, 30 ) );
		pnRodCont.add( btSair, BorderLayout.EAST );

		pnRodCont.setBorder( BorderFactory.createEtchedBorder() );
		getTela().add( pnRodCont, BorderLayout.SOUTH );

		tabCont.addMouseListener( new MouseAdapter() {

			public void mouseClicked( MouseEvent mevt ) {

				if ( mevt.getClickCount() == 2 ) {
					editaHist();
				}
			}
		} );

		tabCli.addMouseListener( new MouseAdapter() {

			public void mouseClicked( MouseEvent mevt ) {

				if ( mevt.getClickCount() == 2 ) {
					editaHist();
				}
			}
		} );

		btNovo.addActionListener( this );
		btExcluir.addActionListener( this );
		btSair.addActionListener( this );
		lcCont.addCarregaListener( this );
		lcCli.addCarregaListener( this );
		tpnHist.addChangeListener( this );

	}

	private void carregaTabCont() {

		String sSQL = "SELECT H.CODHISTTK,H.SITHISTTK,H.TIPOHISTTK,H.DATAHISTTK,H.DESCHISTTK,A.NOMEATEND,H.HORAHISTTK,H.CODATEND" + " FROM TKHISTORICO H, ATATENDENTE A" + " WHERE H.CODCTO=? AND H.CODEMPCO=? AND H.CODFILIALCO=?"
				+ " AND A.CODATEND=H.CODATEND AND A.CODEMP=H.CODEMPAE AND A.CODFILIAL=H.CODFILIALAE " + " ORDER BY H.DATAHISTTK DESC,H.HORAHISTTK DESC";
		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, txtCodCont.getVlrInteger().intValue() );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, lcCont.getCodFilial() );
			ResultSet rs = ps.executeQuery();
			tabCont.limpa();
			vCodHists.clear();
			vCodAtends.clear();
			for ( int i = 0; rs.next(); i++ ) {
				vCodHists.addElement( rs.getString( "CodHistTK" ) );
				vCodAtends.addElement( rs.getString( "CodAtend" ) );
				tabCont.adicLinha();
				tabCont.setValor( rs.getString( "CodHistTK" ), i, 0 );
				tabCont.setValor( rs.getString( "SitHistTK" ), i, 1 );
				tabCont.setValor( rs.getString( "TipoHistTK" ), i, 2 );
				tabCont.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "DataHistTK" ) ), i, 3 );
				tabCont.setValor( rs.getString( "DescHistTK" ), i, 4 );
				tabCont.setValor( rs.getString( "NomeAtend" ), i, 5 );
				tabCont.setValor( rs.getString( "HoraHistTK" ), i, 6 );
			}

			tabTemp = tabCont;

			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar tabela de históricos!\n" + err.getMessage(), true, con, err );
		}
	}

	private void carregaTabCli() {

		String sSQL = "SELECT H.CODHISTTK,H.SITHISTTK,H.TIPOHISTTK,H.DATAHISTTK,H.DESCHISTTK,A.NOMEATEND,H.HORAHISTTK,H.CODATEND" + " FROM TKHISTORICO H, ATATENDENTE A" + " WHERE H.CODCLI=? AND H.CODEMPCL=? AND H.CODFILIALCL=?"
				+ " AND A.CODATEND=H.CODATEND AND A.CODEMP=H.CODEMPAE AND A.CODFILIAL=H.CODFILIALAE " + " ORDER BY H.DATAHISTTK DESC,H.HORAHISTTK DESC,H.CODHISTTK";
		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, txtCodCli.getVlrInteger().intValue() );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, lcCli.getCodFilial() );
			ResultSet rs = ps.executeQuery();
			tabCli.limpa();
			vCodHists.clear();
			vCodAtends.clear();
			for ( int i = 0; rs.next(); i++ ) {
				vCodHists.addElement( rs.getString( "CodHistTK" ) );
				vCodAtends.addElement( rs.getString( "CodAtend" ) );
				tabCli.adicLinha();
				tabCli.setValor( rs.getString( "CodHistTK" ), i, 0 );
				tabCli.setValor( rs.getString( "SitHistTK" ), i, 1 );
				tabCli.setValor( rs.getString( "TipoHistTK" ), i, 2 );
				tabCli.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "DataHistTK" ) ), i, 3 );
				tabCli.setValor( rs.getString( "DescHistTK" ), i, 4 );
				tabCli.setValor( rs.getString( "NomeAtend" ), i, 5 );
				tabCli.setValor( rs.getString( "HoraHistTK" ), i, 6 );
			}

			tabTemp = tabCli;

			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar tabela de históricos!\n" + err.getMessage(), true, con, err );
		}
	}

	private void novoHist() {

		PreparedStatement ps = null;
		int iCod = 0;
		try {

			Object oRets[];

			if ( tabTemp == tabCont ) {
				if ( txtCodCont.getVlrInteger().intValue() == 0 ) {
					Funcoes.mensagemInforma( this, "Não ha nenhum contato selecionado!" );
					txtCodCont.requestFocus();
					return;
				}
				else
					iCod = txtCodCont.getVlrInteger().intValue();
			}
			else if ( tabTemp == tabCli ) {
				if ( txtCodCli.getVlrInteger().intValue() == 0 ) {
					Funcoes.mensagemInforma( this, "Não ha nenhum cliente selecionado!" );
					txtCodCli.requestFocus();
					return;
				}
				else
					iCod = txtCodCli.getVlrInteger().intValue();
			}

			DLNovoHist dl = new DLNovoHist( iCod, tpnHist.getSelectedIndex(), this );
			dl.setConexao( con );
			dl.setVisible( true );
			if ( dl.OK ) {
				oRets = dl.getValores();
				try {
					String sSQL = "EXECUTE PROCEDURE TKSETHISTSP(0,?,?,?,?,?,?,?,?,?,?,?)";
					ps = con.prepareStatement( sSQL );
					ps.setInt( 1, Aplicativo.iCodEmp );
					if ( txtCodCont.getVlrInteger().intValue() == 0 ) {// Filial e código do contato
						ps.setNull( 2, Types.INTEGER );
						ps.setNull( 3, Types.INTEGER );
					}
					else {
						ps.setInt( 2, lcCont.getCodFilial() );
						ps.setInt( 3, txtCodCont.getVlrInteger().intValue() );
					}
					if ( txtCodCli.getVlrInteger().intValue() == 0 ) {
						ps.setNull( 4, Types.INTEGER );
						ps.setNull( 5, Types.INTEGER );
					}
					else {
						ps.setInt( 4, lcCli.getCodFilial() );
						ps.setInt( 5, txtCodCli.getVlrInteger().intValue() );
					}
					ps.setString( 6, (String) oRets[ 0 ] );// Descrição do historico
					ps.setInt( 7, ListaCampos.getMasterFilial( "ATATENDENTE" ) );// Filial do atendete
					ps.setString( 8, (String) oRets[ 1 ] );// codígo atendente
					ps.setString( 9, (String) oRets[ 2 ] );// status do historico
					ps.setDate( 10, (Date) oRets[ 4 ] );// data do historico
					ps.setString( 11, (String) oRets[ 3 ] );// status do historico
					ps.execute();
					ps.close();

					con.commit();

					if ( oRets[ 5 ] != null ) {
						sSQL = "EXECUTE PROCEDURE SGSETAGENDASP(0,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'PU')";
						ps = con.prepareStatement( sSQL );
						ps.setInt( 1, Aplicativo.iCodEmp );
						// ps.setDate(2,(Date)oRets[5]);
						ps.setDate( 2, Funcoes.strDateToSqlDate( oRets[ 5 ].toString() ) );
						ps.setString( 3, oRets[ 6 ] + ":00" );
						ps.setDate( 4, Funcoes.strDateToSqlDate( oRets[ 7 ].toString() ) );
						ps.setString( 5, (String) oRets[ 8 ] + ":00" );
						ps.setString( 6, (String) oRets[ 9 ] );
						ps.setString( 7, (String) oRets[ 10 ] );
						ps.setInt( 8, Aplicativo.iCodFilialPad );
						ps.setString( 9, (String) oRets[ 11 ] );
						ps.setInt( 10, 5 );
						ps.setInt( 11, Aplicativo.iCodFilialPad );
						ps.setString( 12, Aplicativo.getUsuario().getIdusu() );
						ps.setString( 13, (String) oRets[ 12 ] );
						ps.setString( 14, (String) oRets[ 13 ] );
						ps.setInt( 15, ( (Integer) buscaAgente( 0 ) ).intValue() );
						ps.setString( 16, (String) buscaAgente( 1 ) );
						ps.execute();
						ps.close();
						con.commit();
					}
				} catch ( SQLException err ) {
					Funcoes.mensagemErro( this, "SQL Erro ao salvar o histórico!\n" + err.getMessage(), true, con, err );
					err.printStackTrace();
				} catch ( Exception err ) {
					Funcoes.mensagemErro( this, "Erro ao salvar o histórico!\n" + err.getMessage(), true, con, err );
					err.printStackTrace();
				}
				if ( tabTemp == tabCont )
					carregaTabCont();
				else if ( tabTemp == tabCli )
					carregaTabCli();
			}
			dl.dispose();
		} finally {
			ps = null;
			iCod = 0;
		}
	}

	private void editaHist() {

		int iLin = 0;
		int iCod = 0;
		try {
			if ( ( iLin = tabTemp.getLinhaSel() ) < 0 ) {
				Funcoes.mensagemInforma( this, "Não ha nenhum histórico selecionado!" );
				return;
			}
			Object oRets[];

			if ( tabTemp == tabCont )
				iCod = txtCodCont.getVlrInteger().intValue();
			else if ( tabTemp == tabCli )
				iCod = txtCodCli.getVlrInteger().intValue();

			DLNovoHist dl = new DLNovoHist( iCod, tpnHist.getSelectedIndex(), this );
			dl.setConexao( con );
			dl.setValores( new Object[] { (String) tabTemp.getValor( iLin, 4 ), vCodAtends.elementAt( iLin ), (String) tabTemp.getValor( iLin, 1 ), (String) tabTemp.getValor( iLin, 2 ), (Date) Funcoes.strDateToSqlDate( (String) tabTemp.getValor( iLin, 3 ) ) } );
			dl.setVisible( true );
			if ( dl.OK ) {
				oRets = dl.getValores();
				try {
					String sSQL = "EXECUTE PROCEDURE TKSETHISTSP(?,?,?,?,?,?,?,?,?,?,?,'')";
					PreparedStatement ps = con.prepareStatement( sSQL );
					ps.setInt( 1, Integer.parseInt( (String) tabTemp.getValor( iLin, 0 ) ) );
					ps.setInt( 2, Aplicativo.iCodEmp );
					if ( txtCodCont.getVlrInteger().intValue() == 0 ) {// Filial e código do contato
						ps.setNull( 3, Types.INTEGER );
						ps.setNull( 4, Types.INTEGER );
					}
					else {
						ps.setInt( 3, lcCont.getCodFilial() );
						ps.setInt( 4, txtCodCont.getVlrInteger().intValue() );
					}
					if ( txtCodCli.getVlrInteger().intValue() == 0 ) {
						ps.setNull( 5, Types.INTEGER );
						ps.setNull( 6, Types.INTEGER );
					}
					else {
						ps.setInt( 5, lcCli.getCodFilial() );
						ps.setInt( 6, txtCodCli.getVlrInteger().intValue() );
					}
					ps.setString( 7, (String) oRets[ 0 ] );// Descrição do historico
					ps.setInt( 8, ListaCampos.getMasterFilial( "ATATENDENTE" ) );// Filial do atendete
					ps.setString( 9, (String) oRets[ 1 ] );// codígo atendente
					ps.setString( 10, (String) oRets[ 2 ] );// status do historico
					ps.setDate( 11, (Date) oRets[ 4 ] );// data do historico

					ps.execute();
					ps.close();
					con.commit();
				} catch ( SQLException err ) {
					Funcoes.mensagemErro( this, "Erro ao salvar o histórico!\n" + err.getMessage(), true, con, err );
				}
				if ( tabTemp == tabCont )
					carregaTabCont();
				else if ( tabTemp == tabCli )
					carregaTabCli();
			}
			dl.dispose();
		} finally {
			iLin = 0;
			iCod = 0;
		}
	}

	private void excluiHist() {

		if ( tabTemp.getLinhaSel() == -1 ) {
			Funcoes.mensagemInforma( this, "Selecione um item na lista!" );
			return;
		}
		else if ( Funcoes.mensagemConfirma( this, "Deseja relamente excluir o histórico '" + vCodHists.elementAt( tabTemp.getLinhaSel() ) + "'?" ) != JOptionPane.YES_OPTION ) {
			return;
		}
		try {
			String sSQL = "DELETE FROM TKHISTORICO WHERE CODHISTTK=? AND CODEMP=? AND CODFILIAL=?";
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setString( 1, "" + vCodHists.elementAt( tabTemp.getLinhaSel() ) );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "TKHISTORICO" ) );
			ps.execute();
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao excluir o histórico!\n" + err.getMessage(), true, con, err );
		}
		if ( tabTemp == tabCont )
			carregaTabCont();
		else if ( tabTemp == tabCli )
			carregaTabCli();
	}

	private Object buscaAgente( int index ) {

		Object[] oRet = new Object[ 2 ];
		String sSQL = "SELECT U.CODAGE,U.TIPOAGE FROM SGUSUARIO U WHERE CODEMP=? AND CODFILIAL=? " + "AND IDUSU=?";
		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			ps.setString( 3, Aplicativo.getUsuario().getIdusu() );

			ResultSet rs = ps.executeQuery();
			while ( rs.next() ) {
				oRet[ 0 ] = new Integer( rs.getInt( 1 ) );
				oRet[ 1 ] = rs.getString( 2 );
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			sSQL = null;
		}

		return oRet[ index ];
	}

	private void zeraCampos() {

		if ( tpnHist.getSelectedIndex() == 1 ) {
			txtCodCont.setVlrString( "" );
			txtNomeCont.setVlrString( "" );
			txtTelCont.setVlrString( "" );
			txtFaxCont.setVlrString( "" );
			txtEmpCont.setVlrString( "" );
			txtEmailCont.setVlrString( "" );
			txtEndCont.setVlrString( "" );
			txtNumCont.setVlrString( "" );
			txtComplCont.setVlrString( "" );
			txtBairCont.setVlrString( "" );
			txtCidCont.setVlrString( "" );
			txtUfCont.setVlrString( "" );
			txtDDDCont.setVlrString( "" );
			txtContCont.setVlrString( "" );
		}
		else if ( tpnHist.getSelectedIndex() == 0 ) {
			txtCodCli.setVlrString( "" );
			txtNomeCli.setVlrString( "" );
			txtTelCli.setVlrString( "" );
			txtFaxCli.setVlrString( "" );
			txtEmpCli.setVlrString( "" );
			txtEmailCli.setVlrString( "" );
			txtEndCli.setVlrString( "" );
			txtNumCli.setVlrString( "" );
			txtComplCli.setVlrString( "" );
			txtBairCli.setVlrString( "" );
			txtCidCli.setVlrString( "" );
			txtUfCli.setVlrString( "" );
			txtDDDCli.setVlrString( "" );
			txtContCli.setVlrString( "" );
		}

	}

	public void afterCarrega( CarregaEvent cevt ) {

	}

	public void beforeCarrega( CarregaEvent cevt ) {

		if ( cevt.getListaCampos() == lcCont ) {
			carregaTabCont();
		}
		if ( cevt.getListaCampos() == lcCli ) {
			carregaTabCli();
		}
	}

	public void stateChanged( ChangeEvent evt ) {

		if ( evt.getSource() == tpnHist ) {
			zeraCampos();
			if ( tpnHist.getSelectedIndex() == 0 ) {
				txtCodCont.requestFocus();
				carregaTabCont();
			}
			else if ( tpnHist.getSelectedIndex() == 1 ) {
				txtCodCli.requestFocus();
				carregaTabCli();
			}
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btSair ) {
			dispose();
		}
		else if ( evt.getSource() == btNovo ) {
			novoHist();
		}
		else if ( evt.getSource() == btExcluir ) {
			excluiHist();
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcCont.setConexao( cn );
		lcCli.setConexao( cn );
	}
}
