/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)DLAnalBanc.java <BR>
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
 */

package org.freedom.modulos.std.view.dialog.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Date;
import java.util.Vector;

import javax.swing.JTextField;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;

public class DLAnalBanc extends FFDialogo implements FocusListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodPai = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDescPai = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodAnal = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDescAnal = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtAgCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 6, 0 );

	private JTextFieldPad txtNumCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtDescCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodBanco = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private JTextFieldFK txtDescBanco = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtDataCont = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodMoeda = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldFK txtDescMoeda = new JTextFieldFK( JTextFieldPad.TP_STRING, 20, 0 );

	private JTabbedPanePad tbp = new JTabbedPanePad();

	private JPanelPad pinGeral = new JPanelPad( 370, 240 );

	private JPanelPad pinDet = new JPanelPad( 370, 240 );

	private Vector<String> vVals = new Vector<String>();

	private Vector<String> vLabs = new Vector<String>();

	private JRadioGroup<?, ?> rgTipo = null;

	private Vector<String> vValsCont = new Vector<String>();

	private Vector<String> vLabsCont = new Vector<String>();

	private JRadioGroup<?, ?> rgTipoCont = null;

	private ListaCampos lcBanc = new ListaCampos( this, "BO" );

	private ListaCampos lcMoeda = new ListaCampos( this, "MA" );

	public DLAnalBanc( Component cOrig, String sCodPai, String sDescPai, String sCod, String sDesc, String sTipo, String[] ContVals ) {

		super( cOrig );
		
		setTitulo( "Nova Conta Analítica" );
		setAtribos( 380, 330 );
		
		// Monta a tab Geral:
		
		cancText( txtCodPai ); // Por ser análitica são desabilitados estes campos:
		cancText( txtDescPai );
		cancText( txtCodAnal );
		
		txtCodPai.setVlrString( sCodPai ); // Eles Receber valor padrão:
		txtDescPai.setVlrString( sDescPai );
		txtCodAnal.setVlrString( sCod );
		
		vVals.addElement( "B" ); // Aqui é montado o JRadioGroup<?, ?> do tipo de conta:
		vVals.addElement( "C" );
		vVals.addElement( "D" );
		vVals.addElement( "R" );
		
		vLabs.addElement( "Bancos" );
		vLabs.addElement( "Caixa" );
		vLabs.addElement( "Despesas" );
		vLabs.addElement( "Receitas" );
		
		rgTipo = new JRadioGroup<String, String>( 2, 2, vLabs, vVals );
		rgTipo.setVlrString( sTipo );
		rgTipo.setAtivo( 0, false );
		rgTipo.setAtivo( 1, false );
		rgTipo.setAtivo( 2, false );
		rgTipo.setAtivo( 3, false );
		
		lcMoeda.add( new GuardaCampo( txtCodMoeda, "CodMoeda", "Código", ListaCampos.DB_PK, true ) );
		lcMoeda.add( new GuardaCampo( txtDescMoeda, "SingMoeda", "Descrição", ListaCampos.DB_SI, false ) );
		lcMoeda.montaSql( false, "MOEDA", "FN" );
		lcMoeda.setReadOnly( true );
		
		txtCodMoeda.setTabelaExterna( lcMoeda, null ); // Tabela da Foreign Key
		txtCodMoeda.setFK( true );
		txtCodMoeda.setNomeCampo( "CodMoeda" );
		
		vValsCont.addElement( "B" ); // Aqui é montado o JRadioGroup<?, ?> do tipo de entrada:
		vValsCont.addElement( "C" );
		vLabsCont.addElement( "Bancos" );
		vLabsCont.addElement( "Caixa" );
		
		rgTipoCont = new JRadioGroup<String, String>( 1, 2, vLabsCont, vValsCont );
	
		Funcoes.setBordReq( txtDescAnal );
		Funcoes.setBordReq( txtNumCont );
		Funcoes.setBordReq( txtDescCont );
		Funcoes.setBordReq( txtDataCont );
		Funcoes.setBordReq( txtCodMoeda );

		pinGeral.adic( txtCodPai, 7, 20, 80, 20, "Código" );
		pinGeral.adic( txtDescPai, 90, 20, 240, 20, "e descrição da origem" );
		pinGeral.adic( txtCodAnal, 7, 60, 110, 20, "Código" );
		pinGeral.adic( txtDescAnal, 120, 60, 210, 20, "Descrição" );
		pinGeral.adic( rgTipo, 7, 90, 323, 60 );

		pinDet.adic( txtAgCont, 7, 20, 80, 20, "Agência" );
		pinDet.adic( txtNumCont, 90, 20, 240, 20, "Número" );
		pinDet.adic( txtDescCont, 7, 60, 323, 20, "Descrição" );
		pinDet.adic( txtCodBanco, 7, 100, 80, 20, "Código" );
		pinDet.adic( txtDescBanco, 90, 100, 240, 20, "e nome do banco" );
		pinDet.adic( txtDataCont, 7, 140, 90, 20, "Data Inicial" );
		pinDet.adic( rgTipoCont, 100, 130, 223, 30 );
		pinDet.adic( txtCodMoeda, 7, 180, 80, 20, "Código" );
		pinDet.adic( txtDescMoeda, 90, 180, 240, 20, "e descrição da moeda" );

		// Monta a tab Detalhe:
		lcBanc.add( new GuardaCampo( txtCodBanco, "CodBanco", "Cód.banco", ListaCampos.DB_PK, false ) );
		lcBanc.add( new GuardaCampo( txtDescBanco, "NomeBanco", "Descrição do banco", ListaCampos.DB_SI, false ) );
		lcBanc.montaSql( false, "BANCO", "FN" );
		lcBanc.setReadOnly( true );
		txtCodBanco.setTabelaExterna( lcBanc, null );
		txtCodBanco.setFK( true );
		txtCodBanco.setNomeCampo( "CodBanco" );
		
		// Se for Novo:
		if ( sTipo.compareTo( "B" ) == 0 ) {// Para cada entrada muda-se os Valores e Campos abilitados:
			rgTipoCont.setVlrString( sTipo );
			rgTipoCont.setAtivo( 0, false );
			rgTipoCont.setAtivo( 1, false );
		}
		else if ( sTipo.compareTo( "C" ) == 0 ) {
			cancText( txtAgCont );
			txtCodBanco.setEnabled( false );
			rgTipoCont.setVlrString( sTipo );
			rgTipoCont.setAtivo( 0, false );
			rgTipoCont.setAtivo( 1, false );
		}

		// Se for Editar:
		if ( sDesc != null ) {
			setTitulo( "Edição de Conta Analítica" );
			txtDescAnal.setVlrString( sDesc );
			txtDescAnal.selectAll();
			txtDescAnal.requestFocus();
			txtAgCont.setVlrString( ContVals[ 0 ] );
			txtNumCont.setVlrString( ContVals[ 1 ] );
			txtDescCont.setVlrString( ContVals[ 2 ] );
			txtCodBanco.setVlrString( ContVals[ 3 ] );
			txtDataCont.setVlrString( ContVals[ 4 ] );
			txtCodMoeda.setVlrString( ContVals[ 5 ] );
		}
		
		txtDescAnal.addFocusListener( this );
		txtDescAnal.requestFocus();
		
		tbp.add( "Geral", pinGeral );
		tbp.add( "Detalhe", pinDet );
		
		c.add( tbp, BorderLayout.CENTER );
	}

	public void focusGained( FocusEvent fevt ) {

	}

	public void focusLost( FocusEvent fevt ) {// Copia a descrição o planejamento para a descrição da conta:

		if ( txtDescCont.getText().length() == 0 )
			txtDescCont.setVlrString( txtDescAnal.getText() );
	}

	public void setConexao( DbConnection cn ) {// Seta as devidas conexões nos listacampos de Foreign Key

		lcBanc.setConexao( cn );
		lcMoeda.setConexao( cn );
	}

	private void cancText( JTextField txt ) {// Desabilita TextFields

		txt.setBackground( Color.lightGray );
		txt.setFont( new Font( "Dialog", Font.BOLD, 12 ) );
		txt.setEditable( false );
		txt.setForeground( new Color( 118, 89, 170 ) );
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) { // Valida os valores:
			if ( txtDescAnal.getText().trim().length() == 0 ) {
				Funcoes.mensagemInforma( this, "O campo descrição está em branco! ! !" );
				txtDescAnal.requestFocus();
				return;
			}
			else if ( txtNumCont.getText().trim().length() == 0 ) {
				Funcoes.mensagemInforma( this, "O campo número da conta está em branco! ! !" );
				txtNumCont.requestFocus();
				return;
			}
			else if ( txtDescCont.getText().trim().length() == 0 ) {
				Funcoes.mensagemInforma( this, "O campo descrição da conta está em branco! ! !" );
				txtDescCont.requestFocus();
				return;
			}
			else if ( txtDataCont.getText().trim().length() == 0 ) {
				Funcoes.mensagemInforma( this, "O campo data da conta está em branco! ! !" );
				txtDataCont.requestFocus();
				return;
			}
			else if ( txtCodMoeda.getText().trim().length() == 0 ) {
				Funcoes.mensagemInforma( this, "O campo código da moeda está em branco! ! !" );
				txtCodMoeda.requestFocus();
				return;
			}
		}
		super.actionPerformed( evt );
	}

	public String[] getValores() { // Devolve os valores a Tela de planejamento:

		String[] sVals = new String[ 7 ];
		sVals[ 0 ] = txtDescAnal.getText();
		sVals[ 1 ] = txtAgCont.getText();
		sVals[ 2 ] = txtNumCont.getText();
		sVals[ 3 ] = txtDescCont.getText();
		sVals[ 4 ] = txtCodBanco.getText();
		sVals[ 5 ] = Funcoes.dateToStrDate( txtDataCont.getVlrDate() );
		sVals[ 6 ] = txtCodMoeda.getText();
		return sVals;
	}

	public Date getData() {

		return txtDataCont.getVlrDate();
	}
}
