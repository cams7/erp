/**
 * @version 30/07/2010 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.fnc.view.frame.crud.plain <BR>
 *         Classe: @(#)FTalaoCheq.java <BR>
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

package org.freedom.modulos.fnc.view.frame.crud.plain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.Date;

import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.fnc.view.frame.crud.tabbed.FConta;

public class FTalaoCheq extends FDados implements ActionListener, KeyListener, InsertListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtNumconta = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldFK txtDescconta = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtSeqtalao = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 6, 0 );
	
	private JTextFieldPad txtDttalao = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );
	
	private JCheckBoxPad cbxAtivotalao = new JCheckBoxPad( "Ativo", "S", "N" );
	
	private JTextFieldPad txtCheqinitalao = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtCheqfimtalao = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtCheqatualtalao = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private ListaCampos lcConta = new ListaCampos( this, "" );

	public FTalaoCheq() {

		super();
		setTitulo( "Cadastro de Talonário de Cheques" );
		setAtribos( 50, 50, 400, 210 );

		lcConta.add( new GuardaCampo( txtNumconta, "Numconta", "Número conta", ListaCampos.DB_PK, txtDescconta, true ) );
		lcConta.add( new GuardaCampo( txtDescconta, "Descconta", "Descriçao do conta", ListaCampos.DB_SI, null, false ) );
		lcConta.montaSql( false, "CONTA", "FN" );
		lcConta.setQueryCommit( false );
		lcConta.setReadOnly( true );
		txtNumconta.setTabelaExterna( lcConta, FConta.class.getCanonicalName() );

		adicCampo( txtNumconta, 7, 20, 80, 20, "Numconta", "Número conta", ListaCampos.DB_PF, true );
		adicDescFK( txtDescconta, 90, 20, 210, 20, "Descconta", "Descrição da conta" );
		adicCampo( txtSeqtalao, 310, 20, 60, 20, "Seqtalao", "Sequencia", ListaCampos.DB_PK, true);
		adicCampo( txtDttalao, 7, 60, 90, 20, "Dttalao", "Data", ListaCampos.DB_SI, true );
		adicDB( cbxAtivotalao, 110, 60, 70, 20, "AtivoTalao", "Ativo", true );
		
		adicCampo( txtCheqinitalao, 7, 110, 80, 20, "Cheqinitalao", "Cheque inicial", ListaCampos.DB_SI, false );
		adicCampo( txtCheqfimtalao, 90, 110, 80, 20, "Cheqfimtalao", "Cheque final", ListaCampos.DB_SI, false );
		adicCampo( txtCheqatualtalao, 173, 110, 80, 20, "Cheqatualtalao", "Número atual", ListaCampos.DB_SI, false );
		setListaCampos( false, "TALAOCHEQ", "FN" );
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		lcCampos.setQueryInsert( false );
		lcCampos.addInsertListener( this );
		setImprimir( true );
		
	}

	public void actionPerformed( ActionEvent evt ) {
		super.actionPerformed( evt );
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

	}

	public void setConexao( DbConnection cn ) {
		super.setConexao( cn );
		lcConta.setConexao( cn );
	}

	public void afterInsert( InsertEvent ievt ) {
		txtDttalao.setVlrDate( new Date()  );
		cbxAtivotalao.setVlrString( "S" );
		txtCheqinitalao.setVlrInteger( 1 );
		txtCheqfimtalao.setVlrInteger( 999999 );
		txtCheqatualtalao.setVlrInteger( 0 );
	}

	public void beforeInsert( InsertEvent ievt ) {

	}
}
