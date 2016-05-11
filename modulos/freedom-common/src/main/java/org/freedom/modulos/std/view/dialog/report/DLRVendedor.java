/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)DLRCliente.java <BR>
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
 *         Tela de opções para o relatório de comissionados.
 */

package org.freedom.modulos.std.view.dialog.report;

import java.awt.Component;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;

import java.util.Vector;

public class DLRVendedor extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JRadioGroup<?, ?> rgOrdem = null;

	// private JPanelPad pnlbSelec = new JPanelPad(JPanelPad.TP_JPANEL,new GridLayout(1,1));
	// private JPanelPad pinSelec = new JPanelPad(400,90);
	private JTextFieldPad txtCid = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JLabelPad lbOrdem = new JLabelPad( "Ordenar por:" );

	private JLabelPad lbCid = new JLabelPad( "Cidade" );

	private Vector<String> vLabs = new Vector<String>();

	private Vector<String> vVals = new Vector<String>();

	private JLabelPad lbSetor = new JLabelPad( "Cód.setor" );

	private JLabelPad lbDescSetor = new JLabelPad( "Descrição do setor" );

	private JLabelPad lbCodFuncaoVend = new JLabelPad( "Cód.funcao" );

	private JLabelPad lbDescFuncaoVend = new JLabelPad( "Descrição do funcao do comissionado" );

	private JLabelPad lbVendedor = new JLabelPad( "Cód.comiss." );

	private JLabelPad lbNomeVendedor = new JLabelPad( "Nome do comissionado" );

	private JLabelPad lbCodClasComi = new JLabelPad( "Cód.cl.comis." );

	private JLabelPad lbDescClasComi = new JLabelPad( "Descrição da classificação de comissão" );

	private JTextFieldPad txtCodSetor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	// private JTextFieldPad txtCodFuncaoVend = new JTextFieldPad(JTextFieldPad.TP_INTEGER,8,0);
	private JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescSetor = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtNomeVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodFunc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescFunc = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private ListaCampos lcSetor = new ListaCampos( this );

	private ListaCampos lcVendedor = new ListaCampos( this );

	private ListaCampos lcFuncao = new ListaCampos( this, "FU" );

	private ListaCampos lcClComis = new ListaCampos( this, "CC" );

	private JTextFieldPad txtCodClComis = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescClComis = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	public DLRVendedor( Component cOrig, DbConnection cn ) {

		super( cOrig );
		setTitulo( "Relatório de Comissionados" );
		setAtribos( 460, 445 );
		vLabs.addElement( "Código" );
		vLabs.addElement( "Nome" );
		vVals.addElement( "C" );
		vVals.addElement( "N" );

		rgOrdem = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgOrdem.setVlrString( "C" );

		lcClComis.add( new GuardaCampo( txtCodClComis, "CodClComis", "Cód.cl.comis.", ListaCampos.DB_PK, txtDescClComis, false ) );
		lcClComis.add( new GuardaCampo( txtDescClComis, "DescClComis", "Descriçao da classificação da comissão", ListaCampos.DB_SI, false ) );
		lcClComis.montaSql( false, "CLCOMIS", "VD" );
		lcClComis.setQueryCommit( false );
		lcClComis.setReadOnly( true );
		txtCodClComis.setTabelaExterna( lcClComis, null );
		txtCodClComis.setFK( true );
		txtCodClComis.setNomeCampo( "CodClComis" );

		lcSetor.add( new GuardaCampo( txtCodSetor, "CodSetor", "Cód.setor", ListaCampos.DB_PK, false ) );
		lcSetor.add( new GuardaCampo( txtDescSetor, "DescSetor", "Descrição do setor", ListaCampos.DB_SI, false ) );
		lcSetor.montaSql( false, "SETOR", "VD" );
		lcSetor.setReadOnly( true );
		txtCodSetor.setTabelaExterna( lcSetor, null );
		txtCodSetor.setFK( true );
		txtCodSetor.setNomeCampo( "CodSetor" );

		lcFuncao.add( new GuardaCampo( txtCodFunc, "CodFunc", "Cód.função", ListaCampos.DB_PK, txtDescFunc, false ) );
		lcFuncao.add( new GuardaCampo( txtDescFunc, "DescFunc", "Descriçao da função", ListaCampos.DB_SI, false ) );
		lcFuncao.montaSql( false, "FUNCAO", "RH" );
		lcFuncao.setQueryCommit( false );
		lcFuncao.setReadOnly( true );
		txtCodFunc.setTabelaExterna( lcFuncao, null );
		txtCodFunc.setFK( true );
		txtCodFunc.setNomeCampo( "CodFunc" );

		lcVendedor.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.comiss.", ListaCampos.DB_PK, false ) );
		lcVendedor.add( new GuardaCampo( txtNomeVend, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, false ) );
		lcVendedor.montaSql( false, "VENDEDOR", "VD" );
		lcVendedor.setReadOnly( true );
		txtCodVend.setTabelaExterna( lcVendedor, null );
		txtCodVend.setFK( true );
		txtCodVend.setNomeCampo( "CodVend" );

		adic( lbOrdem, 7, 5, 180, 20 );
		adic( rgOrdem, 7, 25, 350, 30 );
		// adic(pnlbSelec,10,63,80,15);
		// adic(pinSelec,7,70,433,70);
		adic( lbCid, 7, 65, 140, 20 );
		adic( txtCid, 7, 85, 250, 20 );
		adic( lbSetor, 7, 105, 250, 20 );
		adic( txtCodSetor, 7, 125, 80, 20 );
		adic( lbDescSetor, 90, 105, 250, 20 );
		adic( txtDescSetor, 90, 125, 350, 20 );
		adic( lbVendedor, 7, 145, 300, 20 );
		adic( txtCodVend, 7, 165, 80, 20 );
		adic( lbNomeVendedor, 90, 145, 300, 20 );
		adic( txtNomeVend, 90, 165, 350, 20 );
		adic( lbCodFuncaoVend, 7, 185, 300, 20 );
		adic( txtCodFunc, 7, 205, 80, 20 );
		adic( lbDescFuncaoVend, 90, 185, 300, 20 );
		adic( txtDescFunc, 90, 205, 350, 20 );

		adic( lbCodClasComi, 7, 225, 300, 20 );
		adic( txtCodClComis, 7, 245, 80, 20 );
		adic( lbDescClasComi, 90, 225, 300, 20 );
		adic( txtDescClComis, 90, 245, 350, 20 );

		lcSetor.setConexao( cn );
		lcFuncao.setConexao( cn );
		lcVendedor.setConexao( cn );
		lcClComis.setConexao( cn );
	}

	public String[] getValores() {

		String[] sRetorno = new String[ 15 ];
		if ( rgOrdem.getVlrString().equals( "C" ) )
			sRetorno[ 0 ] = "VD.CODVEND, VD.NOMEVEND";
		else if ( rgOrdem.getVlrString().equals( "N" ) )
			sRetorno[ 0 ] = "VD.NOMEVEND,VD.CODVEND";

		sRetorno[ 1 ] = txtCid.getText();
		sRetorno[ 2 ] = txtCodClComis.getVlrString();
		sRetorno[ 3 ] = txtCodSetor.getVlrString();
		sRetorno[ 4 ] = txtCodFunc.getVlrString();
		sRetorno[ 5 ] = txtCodVend.getVlrString();

		return sRetorno;
	}
}
