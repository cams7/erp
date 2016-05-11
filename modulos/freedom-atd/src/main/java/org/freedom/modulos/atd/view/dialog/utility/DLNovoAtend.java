/**
 * @version 23/06/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.atd <BR>
 *         Classe: @(#)DLNovoAtend.java <BR>
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

package org.freedom.modulos.atd.view.dialog.utility;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import org.freedom.acao.JComboBoxEvent;
import org.freedom.acao.JComboBoxListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.LeiauteGR;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.DLPrinterJob;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;

public class DLNovoAtend extends FFDialogo implements JComboBoxListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pnCab = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	private JPanelPad pnBotoes = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTextFieldPad txtCodConv = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeConv = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodAtend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeAtend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextAreaPad txaDescAtend = new JTextAreaPad();

	private Vector<Object> vValsTipo = new Vector<Object>();

	private Vector<String> vLabsTipo = new Vector<String>();

	private JComboBoxPad cbTipo = new JComboBoxPad( vLabsTipo, vValsTipo, JComboBoxPad.TP_INTEGER, 8, 0 );

	private Vector<Integer> vValsSetor = new Vector<Integer>();

	private Vector<String> vLabsSetor = new Vector<String>();

	private JComboBoxPad cbSetor = new JComboBoxPad( vLabsSetor, vValsSetor, JComboBoxPad.TP_INTEGER, 8, 0 );

	private ListaCampos lcConv = new ListaCampos( this );

	private ListaCampos lcAtend = new ListaCampos( this );

	private JScrollPane spnDesc = new JScrollPane( txaDescAtend );

	private JLabelPad lbImg = new JLabelPad( Icone.novo( "bannerATD.jpg" ) );

	private JButtonPad btMedida = new JButtonPad( Icone.novo( "btMedida.png" ) );

	private String sPrefs[] = null;

	private int iDoc = 0;

	public DLNovoAtend( int iCodConv, Component cOrig ) {

		super( cOrig );
		setTitulo( "Novo atendimento" );
		setAtribos( 505, 470 );

		lcConv.add( new GuardaCampo( txtCodConv, "CodConv", "Cód.conv.", ListaCampos.DB_PK, false ), "txtCodVendx" );
		lcConv.add( new GuardaCampo( txtNomeConv, "NomeConv", "Nome", ListaCampos.DB_SI, false ), "txtCodVendx" );
		lcConv.montaSql( false, "CONVENIADO", "AT" );
		lcConv.setReadOnly( true );
		txtCodConv.setTabelaExterna( lcConv, null );
		txtCodConv.setFK( true );
		txtCodConv.setNomeCampo( "CodConv" );

		lcAtend.add( new GuardaCampo( txtCodAtend, "CodAtend", "Cód.atend.", ListaCampos.DB_PK, false ), "txtCodVendx" );
		lcAtend.add( new GuardaCampo( txtNomeAtend, "NomeAtend", "Nome", ListaCampos.DB_SI, false ), "txtCodVendx" );
		lcAtend.montaSql( false, "ATENDENTE", "AT" );
		lcAtend.setReadOnly( true );
		txtCodAtend.setTabelaExterna( lcAtend, null );
		txtCodAtend.setFK( true );
		txtCodAtend.setNomeCampo( "CodAtend" );

		btMedida.setPreferredSize( new Dimension( 30, 30 ) );

		pnCab.setPreferredSize( new Dimension( 500, 60 ) );
		pnCab.add( lbImg );
		c.add( pnCab, BorderLayout.NORTH );
		pnBotoes.setPreferredSize( new Dimension( 35, 60 ) );
		pnBotoes.add( btMedida, BorderLayout.NORTH );
		pnBotoes.setToolTipText( "Ficha de medidas" );
		c.add( pnBotoes, BorderLayout.EAST );

		adic( new JLabelPad( "Cód.conv." ), 7, 5, 200, 20 );
		adic( txtCodConv, 7, 25, 80, 20 );
		adic( new JLabelPad( "Nome do conveniado" ), 90, 5, 200, 20 );
		adic( txtNomeConv, 90, 25, 197, 20 );
		adic( new JLabelPad( "Tipo de atendimento" ), 290, 5, 150, 20 );
		adic( cbTipo, 290, 25, 150, 20 );
		adic( new JLabelPad( "Cód.atd" ), 7, 45, 200, 20 );
		adic( txtCodAtend, 7, 65, 80, 20 );
		adic( new JLabelPad( "Nome do atendente" ), 90, 45, 200, 20 );
		adic( txtNomeAtend, 90, 65, 197, 20 );
		adic( new JLabelPad( "Setor" ), 290, 45, 150, 20 );
		adic( cbSetor, 290, 65, 150, 20 );

		JPanelPad pnLbAtend = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );
		pnLbAtend.add( new JLabelPad( "   Atendimento" ) );
		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );

		btMedida.addActionListener( this );
		cbTipo.addComboBoxListener( this );

		adic( pnLbAtend, 20, 90, 90, 20 );
		adic( lbLinha, 7, 100, 433, 2 );
		adic( spnDesc, 7, 115, 433, 200 );

		txtCodConv.setVlrInteger( new Integer( iCodConv ) );
		txtCodConv.setAtivo( false );
	}

	private void montaComboTipo() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = "SELECT CODTPATENDO,DESCTPATENDO FROM ATTIPOATENDO WHERE CODEMP=? AND CODFILIAL=?";
		try {
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "ATTIPOATENDO" ) );
			rs = ps.executeQuery();
			vValsTipo.clear();
			vLabsTipo.clear();
			vValsTipo.addElement( "" );
			vLabsTipo.addElement( "<Selecione>" );
			while ( rs.next() ) {
				vValsTipo.addElement( new Integer( rs.getInt( "CodTpAtendo" ) ) );
				vLabsTipo.addElement( rs.getString( "DescTpAtendo" ) );
			}
			cbTipo.setItens( vLabsTipo, vValsTipo );
			rs.close();
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar os tipos de atendimento!\n" + err.getMessage(), true, con, err );
		} finally {
			ps = null;
			rs = null;
		}
	}

	private void montaComboSetor() {

		Integer iTipo = cbTipo.getVlrInteger();
		if ( ( iTipo == null ) || ( iTipo.intValue() == 0 ) )
			return;

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = "SELECT S.CODSETAT,S.DESCSETAT FROM ATSETOR S, ATTIPOATENDOSETOR TS" + " WHERE S.CODEMP=TS.CODEMPST AND S.CODFILIAL=TS.CODFILIAL AND S.CODSETAT=TS.CODSETAT" + " AND TS.CODEMP=? AND TS.CODFILIAL=? AND TS.CODTPATENDO=?";

		try {
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "ATTIPOATENDO" ) );
			ps.setInt( 3, iTipo.intValue() );
			rs = ps.executeQuery();
			vValsSetor.clear();
			vLabsSetor.clear();
			vValsTipo.addElement( "" );
			vLabsTipo.addElement( "<Selecione>" );
			while ( rs.next() ) {
				vValsSetor.addElement( new Integer( rs.getInt( "CodSetAt" ) ) );
				vLabsSetor.addElement( rs.getString( "DescSetAt" ) );
			}
			cbSetor.setItensGeneric( vLabsSetor, vValsSetor );
			rs.close();
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar os setores!\n" + err.getMessage(), true, con, err );
		} finally {
			ps = null;
			rs = null;
		}

		if ( vValsSetor.size() <= 0 )
			Funcoes.mensagemInforma( this, "Não existe setor cadastrado para este tipo de atendimento." );
		else if ( vValsSetor.size() == 1 )
			cbSetor.setEnabled( false );
		else
			cbSetor.setEnabled( true );
	}

	private String[] getPref() {

		String sRets[] = { "", "" };
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = "SELECT CODTPATENDO,CLASSMEDIDA FROM SGPREFERE2 WHERE CODEMP=? AND CODFILIAL=?";
		try {
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				sRets[ 0 ] = rs.getString( "CodTpAtendo" ) != null ? rs.getString( "CodTpAtendo" ) : "";
				sRets[ 1 ] = rs.getString( "ClassMedida" );
			}
			rs.close();
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao verificar levantamento. " );
		} finally {
			ps = null;
			rs = null;
		}
		return sRets;
	}

	private int getCodLev() {

		int iRet = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = "SELECT ISEQ FROM SPGERANUM(?,?,?)";
		try {
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			ps.setString( 3, "LV" );
			rs = ps.executeQuery();
			if ( rs.next() )
				iRet = rs.getInt( 1 );

			rs.close();
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar novo código para levantamento.\n" + err.getMessage(), true, con, err );
		} finally {
			ps = null;
			rs = null;
		}
		return iRet;
	}

	public String[] getValores() {

		String[] sVal = new String[ 5 ];
		sVal[ 0 ] = "" + cbTipo.getVlrInteger();
		sVal[ 1 ] = txtCodAtend.getVlrString();
		sVal[ 2 ] = "" + cbSetor.getVlrInteger();
		sVal[ 3 ] = txaDescAtend.getVlrString();
		sVal[ 4 ] = "" + iDoc;
		return sVal;
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		montaComboTipo();
		montaComboSetor();
		lcAtend.setConexao( cn );
		lcConv.setConexao( cn );
		lcConv.carregaDados();
		sPrefs = getPref();
	}

	public void valorAlterado( JComboBoxEvent evt ) {

		if ( evt.getComboBoxPad() == cbTipo ) {
			montaComboSetor();
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			if ( cbTipo.getVlrInteger().equals( "" ) ) {
				Funcoes.mensagemInforma( this, "O tipo de atendimento não foi selecionado!" );
				return;
			}
			else if ( txtCodAtend.getVlrString().equals( "" ) ) {
				Funcoes.mensagemInforma( this, "Código do atendente inválido!" );
				return;
			}
			else if ( cbSetor.getVlrInteger().equals( "" ) ) {
				Funcoes.mensagemInforma( this, "O setor não foi selecionado!" );
				return;
			}
			else if ( txaDescAtend.getVlrString().equals( "" ) ) {
				Funcoes.mensagemInforma( this, "Não foi digitado nenhum procedimento!" );
				return;
			}
		}
		else if ( evt.getSource() == btMedida ) {
			if ( sPrefs[ 1 ] == null ) {
				Funcoes.mensagemInforma( this, "Não foi possível encontrar um org.freedom.layout de levantamento." );
				return;
			}
			try {
				iDoc = getCodLev();
				Vector<Object> vParam = new Vector<Object>();
				LeiauteGR lei = (LeiauteGR) Class.forName( "org.freedom.layout.atd." + sPrefs[ 1 ].trim() ).newInstance();
				lei.setConexao( con );
				vParam.addElement( txtCodConv.getVlrInteger() );
				vParam.addElement( txtNomeAtend.getVlrString() );
				vParam.addElement( "" + iDoc );
				lei.setParam( vParam );
				DLPrinterJob dl = new DLPrinterJob( lei, this );
				dl.dispose();
			} catch ( Exception err ) {
				Funcoes.mensagemInforma( this, "Não foi possível carregar org.freedom.layout de levantamento!\n" + err.getMessage() );
				err.printStackTrace();
			}
		}
		super.actionPerformed( evt );
	}
}
