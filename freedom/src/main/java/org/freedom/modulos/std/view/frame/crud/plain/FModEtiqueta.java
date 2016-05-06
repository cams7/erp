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
 *         Monta o org.freedom.layout para etiquetas.
 * 
 */

package org.freedom.modulos.std.view.frame.crud.plain;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JScrollPane;

import org.freedom.acao.CheckBoxEvent;
import org.freedom.acao.CheckBoxListener;
import org.freedom.acao.JComboBoxEvent;
import org.freedom.acao.JComboBoxListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.bmps.Icone;
import org.freedom.business.object.EtiquetaCli;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.type.TYPE_PRINT;

public class FModEtiqueta extends FDados implements ActionListener, JComboBoxListener, PostListener, CheckBoxListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCab = new JPanelPad( 0, 150 );

	private JTextFieldPad txtCodModEtiq = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtDescModEtiq = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtNColModEtiq = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtCodpapel = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtEECModEtiq = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 2, 0 );

	private JTextFieldFK txtDescpapel = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtColPapel = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextAreaPad txaEtiqueta = new JTextAreaPad( 500 );

	private JTextFieldPad txtLinPapel = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JScrollPane spnCli = new JScrollPane( txaEtiqueta );

	private JButtonPad btAdic = new JButtonPad( Icone.novo( "btOk.png" ) );

	private JComboBoxPad cbCampos = null;

	private JComboBoxPad cbEtiquetas = null;

	private ListaCampos lcPapel = new ListaCampos( this, "PL" );

	private Vector<?> vTamanhos = new Vector<Object>();

	private JCheckBoxPad cbComprimido = new JCheckBoxPad( "Imprime Comprimido", "S", "N" );

	EtiquetaCli objEtiqCli = new EtiquetaCli();

	private final JCheckBoxPad cbPosScript = new JCheckBoxPad( "Gráfico?", "S", "N" );

	private JLabelPad labelEtiq = new JLabelPad( "Etiqueta" );

	public FModEtiqueta() {

		super();
		setTitulo( "Modelo de etiqueta" );
		setAtribos( 20, 100, 750, 400 );

		pnCliente.add( pinCab, BorderLayout.NORTH );
		pnCliente.add( spnCli );

		setPainel( pinCab );

		lcPapel.add( new GuardaCampo( txtCodpapel, "Codpapel", "Cod.papel", ListaCampos.DB_PK, false ) );
		lcPapel.add( new GuardaCampo( txtDescpapel, "Descpapel", "Descrição do papel", ListaCampos.DB_SI, false ) );
		lcPapel.add( new GuardaCampo( txtColPapel, "Colpapel", "Num. colunas", ListaCampos.DB_SI, false ) );
		lcPapel.add( new GuardaCampo( txtLinPapel, "LinPapel", "Num. Linhas", ListaCampos.DB_SI, false ) );
		lcPapel.montaSql( false, "PAPEL", "SG" );
		lcPapel.setQueryCommit( false );
		lcPapel.setReadOnly( true );
		txtCodpapel.setTabelaExterna( lcPapel, FPapel.class.getCanonicalName() );

		txaEtiqueta.setFont( new Font( "Courier", Font.PLAIN, 11 ) );

		Vector<String> vLabs1 = new Vector<String>();
		Vector<String> vVals1 = new Vector<String>();

		vLabs1.addElement( "" );
		vLabs1.addElement( "pimaco 6180" );
		vVals1.addElement( "" );
		vVals1.addElement( "pimaco6180.jasper" );

		cbEtiquetas = new JComboBoxPad( vLabs1, vVals1, JComboBoxPad.TP_STRING, 50, 0 );

		adic( labelEtiq, 270, 80, 200, 20 );

		adicCampo( txtCodModEtiq, 7, 20, 90, 20, "CodModEtiq", "Cód.mod.etiq.", ListaCampos.DB_PK, true );
		adicCampo( txtDescModEtiq, 100, 20, 237, 20, "DescModEtiq", "Descrição do modelo de etiqueta", ListaCampos.DB_SI, true );
		adicCampo( txtNColModEtiq, 340, 20, 60, 20, "NColModEtiq", "Colunas", ListaCampos.DB_SI, false );
		adicCampo( txtEECModEtiq, 403, 20, 60, 20, "EECModEtiq", "Entre col.", ListaCampos.DB_SI, false );
		adicDB( cbComprimido, 466, 20, 150, 20, "Comprimido", "Imp.comprimido", false );
		adicDB( cbPosScript, 466, 40, 150, 20, "PosScript", "Gráfico", false );
		adicDB( cbEtiquetas, 270, 100, 200, 20, "ModEtiq", "", false );

		txtNColModEtiq.setRequerido( true );
		txtEECModEtiq.setRequerido( true );
		txtCodpapel.setRequerido( true );

		cbPosScript.setVlrString( "N" );
		cbPosScript.addCheckBoxListener( this );

		adicDBLiv( txaEtiqueta, "TxaModEtiq", "Corpo", false );

		adicCampo( txtCodpapel, 7, 60, 90, 20, "Codpapel", "Cód.papel", ListaCampos.DB_FK, txtDescpapel, false );
		adicDescFK( txtDescpapel, 100, 60, 360, 20, "Descpapel", "Descrição do papel" );

		setListaCampos( false, "MODETIQUETA", "SG" );

		Vector<String> vLabs = objEtiqCli.getLabels();
		Vector<?> vVals = objEtiqCli.getValores();
		vTamanhos = objEtiqCli.getTams();

		cbCampos = new JComboBoxPad( vLabs, vVals, JComboBoxPad.TP_STRING, 50, 0 );

		adic( new JLabelPad( "Campos dinâmicos" ), 7, 80, 220, 20 );
		adic( cbCampos, 7, 100, 220, 20 );
		adic( btAdic, 230, 100, 30, 30 );

		txaEtiqueta.setTabSize( 0 );

		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );

		cbCampos.addComboBoxListener( this );
		btAdic.addActionListener( this );

	}

	public String getEtiqueta() {

		String retorno = cbEtiquetas.getVlrString();

		return retorno;
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcPapel.setConexao( cn );
	}

	public void beforePost( PostEvent pevt ) {

		if ( cbPosScript.getVlrString().equals( "N" ) ) {

			Vector<?> vLinhas = null;
			vLinhas = Funcoes.stringToVector( txaEtiqueta.getVlrString() );
			int iColsPapel = txtColPapel.getVlrInteger().intValue();
			int iColsModel = txtNColModEtiq.getVlrInteger().intValue();
			int iEspacoCol = txtEECModEtiq.getVlrInteger().intValue();
			int iMax = 0;

			if ( vLinhas.size() > 0 ) {
				objEtiqCli.setTexto( txaEtiqueta.getVlrString() );// carrega o texto criado para o objeto
				Vector<?> vValoresAdic = objEtiqCli.getValoresAdic(); // busca quais as palavras chaves adicionadas.
				for ( int i = 0; vLinhas.size() > i; i++ ) { // Loop para percorrer todas as linhas do modelo de etiquetas.
					String sTmp = vLinhas.elementAt( i ).toString(); // Carrega string com a linha atual.
					for ( int i2 = 0; vValoresAdic.size() > i2; i2++ ) { // Loop para percorrer todos os elementos adicionados para elimina-los da string medida
						sTmp = sTmp.replaceAll( "\\" + vValoresAdic.elementAt( i2 ).toString(), "" ); // Limpando string retirando as palavras chave para medi-lo corretamente
						sTmp = sTmp.replaceAll( "\\[", "" );
						sTmp = sTmp.replaceAll( "\\]", "" );
					}

					if ( sTmp.length() > iMax ) {
						iMax = sTmp.length();
					}
				}
			}
			int iTamMax = ( ( ( iColsPapel ) - ( iEspacoCol * ( iColsModel - 1 ) ) ) / iColsModel );
			if ( iMax > iTamMax ) {
				pevt.cancela();
				Funcoes.mensagemErro( this, "Texto muito grande (" + iMax + " caracteres.) por coluna.\n Diminua o número de colunas" + " ou reduza o tamanho do texto (" + iTamMax + " caracteres)." );
			}
		}
	}

	public void valorAlterado( JComboBoxEvent evt ) {

		if ( evt.getComboBoxPad() == cbCampos ) {
			adicionaCampo();
		}
	}

	private void adicionaCampo() {

		int iTam = Integer.parseInt( vTamanhos.elementAt( cbCampos.getSelectedIndex() ).toString() );
		txaEtiqueta.insert( "[" + cbCampos.getVlrString() + StringFunctions.replicate( "-", iTam ) + "]", txaEtiqueta.getCaretPosition() );
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btAdic ) {
			adicionaCampo();
		}
		else if ( evt.getSource() == btImp )
			imprimir( TYPE_PRINT.PRINT);
		else if ( evt.getSource() == btPrevimp )
			imprimir( TYPE_PRINT.VIEW );
		super.actionPerformed( evt );
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		ImprimeOS imp = new ImprimeOS( "", con );
		imp.verifLinPag();
		imp.setTitulo( "Teste de etiqueta" );
		imp.limpaPags();
		String[] sLinhas = txaEtiqueta.getText().split( "\n" );
		for ( int i = 0; i < sLinhas.length; i++ ) {
			imp.say( imp.pRow() + 1, 0, sLinhas[ i ] );
		}
		imp.eject();
		imp.fechaGravacao();
		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
	}

	public void valorAlterado( CheckBoxEvent evt ) {

		if ( evt.getCheckBox() == cbPosScript ) {

			if ( cbPosScript.getVlrString().equals( "S" ) ) {
				txtNColModEtiq.setRequerido( false );
				txtEECModEtiq.setRequerido( false );
				txtEECModEtiq.setEditable( false );
				txtNColModEtiq.setEditable( false );
				txtCodpapel.setEditable( false );
				cbEtiquetas.setVisible( true );
				txtCodpapel.setRequerido( false );
				labelEtiq.setVisible( true );
			}
			else {
				txtEECModEtiq.setEditable( true );
				txtNColModEtiq.setEditable( true );
				txtCodpapel.setEditable( true );
				txtNColModEtiq.setRequerido( true );
				txtEECModEtiq.setRequerido( true );
				cbEtiquetas.setVisible( false );
				txtCodpapel.setRequerido( true );
				labelEtiq.setVisible( false );
			}
		}
	}
}
