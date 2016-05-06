/**
 * @version 04/02/2004 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FModEtiqueta.java <BR>
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
 *         Tela para cadastro dos modelos padrões para geração automatica de lotes.
 * 
 */

package org.freedom.modulos.pcp.view.frame.crud.plain;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Vector;

import javax.swing.JScrollPane;

import org.freedom.acao.JComboBoxEvent;
import org.freedom.acao.JComboBoxListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.pcp.business.object.ModLote;

public class FModLote extends FDados implements ActionListener, JComboBoxListener, PostListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCab = new JPanelPad( 0, 100 );

	private JTextFieldPad txtCodModLote = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDescModLote = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextAreaPad txaModLote = new JTextAreaPad( 350 );

	private JScrollPane spnCli = new JScrollPane( txaModLote );

	private JButtonPad btAdic = new JButtonPad( Icone.novo( "btOk.png" ) );

	private JComboBoxPad cbCampos = null;

	private Vector<?> vTamanhos = new Vector<Object>();

	private Vector<String> vLabs = new Vector<String>();

	private Vector<?> vVals = new Vector<Object>();

	private ModLote objModLote = new ModLote();

	public FModLote() {

		super();
		setTitulo( "Modelos de lote" );
		setAtribos( 20, 100, 430, 200 );
		setImprimir( true );
		pnCliente.add( pinCab, BorderLayout.NORTH );
		pnCliente.add( spnCli );

		setPainel( pinCab );

		txaModLote.setFont( new Font( "Courier", Font.PLAIN, 11 ) );

		adicCampo( txtCodModLote, 7, 20, 90, 20, "CodModLote", "Cód.mod.lote", ListaCampos.DB_PK, true );
		adicCampo( txtDescModLote, 100, 20, 280, 20, "DescModLote", "Descrição do modelo de lote", ListaCampos.DB_SI, true );
		adicDBLiv( txaModLote, "TxaModLote", "Corpo", true );

		setListaCampos( true, "MODLOTE", "EQ" );

		vLabs = new Vector<String>();
		vVals = new Vector<Object>();
		vTamanhos = new Vector<Object>();

		vLabs = objModLote.getLabels();
		vVals = objModLote.getValores();
		vTamanhos = objModLote.getTams();

		cbCampos = new JComboBoxPad( vLabs, vVals, JComboBoxPad.TP_STRING, 50, 0 );

		adic( new JLabelPad( "Campos dinâmicos" ), 7, 40, 220, 20 );
		adic( cbCampos, 7, 60, 220, 20 );
		adic( btAdic, 230, 60, 30, 30 );
		txaModLote.setTabSize( 0 );

		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );

		cbCampos.addComboBoxListener( this );
		btAdic.addActionListener( this );
	}

	private void adicionaCampo() {

		int iTam = Integer.parseInt( vTamanhos.elementAt( cbCampos.getSelectedIndex() ).toString() );
		txaModLote.insert( "[" + cbCampos.getVlrString() + StringFunctions.replicate( "-", iTam ) + "]", txaModLote.getCaretPosition() );
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		ImprimeOS imp = new ImprimeOS( "", con );
		objModLote.setTexto( txaModLote.getVlrString() );
		imp.verifLinPag();
		imp.setTitulo( "Teste de Modelo de Lote" );
		imp.limpaPags();
		String sTexto = "É necessário selecionar um modelo de lote válido!";

		if ( objModLote.getLote( new Integer( 30 ), new Date(), con ) != null ) {
			sTexto = objModLote.getLote( new Integer( 30 ), new Date(), con );
		}

		imp.say( imp.pRow() + 1, 0, sTexto );

		imp.eject();
		imp.fechaGravacao();
		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
	}

	public void valorAlterado( JComboBoxEvent evt ) {

		if ( evt.getComboBoxPad() == cbCampos ) {
			adicionaCampo();
		}
	}

	public void beforePost( PostEvent pevt ) {

		int iMax = 13;
		objModLote.setTexto( txaModLote.getVlrString() );// carrega o texto criado para o objeto
		Vector<?> vTemp = new Vector<Object>();
		String sTexto = txaModLote.getVlrString();
		String sTmp = sTexto;

		vTemp = objModLote.getValoresAdic();

		for ( int i = 0; vTemp.size() > i; i++ ) {
			sTmp = sTmp.replaceAll( "\\" + vTemp.elementAt( i ).toString(), "" );
			sTmp = sTmp.replaceAll( "\\[", "" );
			sTmp = sTmp.replaceAll( "\\]", "" );
		}

		int iTam = sTmp.length();

		if ( iTam > iMax ) {
			pevt.cancela();
			Funcoes.mensagemErro( this, "Texto muito grande para o lote (" + iTam + " caracteres).\n " + "O código do lote deve conter no máximo 13 caracteres." );
		}

	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btAdic ) {
			adicionaCampo();
		}
		else if ( evt.getSource() == btImp ) {
			imprimir( TYPE_PRINT.PRINT);
		}
		else if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
		super.actionPerformed( evt );
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
	}
}
