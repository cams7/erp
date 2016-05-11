/**
 * @version 30/05/2003 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: projetos.freeedomcfg <BR>
 *         Classe: @(#)FTabela.java <BR>
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

package org.freedom.modulos.cfg.view.frame.crud.detail;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDetalhe;

public class FTabela extends FDetalhe implements InsertListener, ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCab = new JPanelPad();

	private JPanelPad pinDet = new JPanelPad();

	private JTextFieldPad txtCodTb = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtDescTb = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtSiglaTb = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtCodItTb = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtDescItTb = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JCheckBoxPad cbPadrao = null;

	public FTabela() {

		setTitulo( "Cadastro de Tabelas auxiliares" );
		setAtribos( 50, 20, 500, 350 );
		setAltCab( 90 );
		pinCab = new JPanelPad( 500, 90 );
		setListaCampos( lcCampos );
		setPainel( pinCab, pnCliCab );

		adicCampo( txtCodTb, 7, 20, 70, 20, "CodTb", "Cód.tab.", ListaCampos.DB_PK, true );
		adicCampo( txtDescTb, 80, 20, 297, 20, "DescTb", "Descrição da tabela", ListaCampos.DB_SI, true );
		adicCampo( txtSiglaTb, 380, 20, 80, 20, "SiglaTb", "Sigla", ListaCampos.DB_SI, true );

		setListaCampos( true, "TABELA", "SG" );
		lcCampos.setQueryInsert( false );
		setAltDet( 60 );
		pinDet = new JPanelPad( 590, 110 );
		setPainel( pinDet, pnDet );
		setListaCampos( lcDet );
		setNavegador( navRod );

		cbPadrao = new JCheckBoxPad( "Padrão", "S", "N" );
		cbPadrao.setVlrString( "N" );

		adicCampo( txtCodItTb, 7, 20, 70, 20, "CodItTb", "Item", ListaCampos.DB_PK, true );
		adicCampo( txtDescItTb, 80, 20, 280, 20, "DescItTb", "Descrição do item", ListaCampos.DB_SI, true );
		adicDB( cbPadrao, 365, 20, 90, 20, "PadraoIttb", "", true );

		setListaCampos( true, "ITTABELA", "SG" );
		lcCampos.setQueryInsert( false );
		montaTab();

		tab.setTamColuna( 70, 0 );
		tab.setTamColuna( 395, 1 );
		tab.setTamColuna( 20, 2 );

		lcCampos.addInsertListener( this );
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
	}

	public void actionPerformed( ActionEvent evt ) {

		super.actionPerformed( evt );
	}

	public void afterInsert( InsertEvent ievt ) {

	}

	public void beforeInsert( InsertEvent ievt ) {

	}

}
