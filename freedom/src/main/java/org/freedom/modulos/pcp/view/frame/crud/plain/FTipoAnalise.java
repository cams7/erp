/**
 * @version 01/08/2008 <BR>
 * @author Setpoint Informática Ltda.
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FTipoAnalise.java <BR>
 * 
 *                       Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                       modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                       na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                       Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                       sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                       Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                       Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                       de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                       Tela para cadastro de tipos de clientes.
 * 
 */
package org.freedom.modulos.pcp.view.frame.crud.plain;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;
import org.freedom.modulos.pcp.view.frame.crud.tabbed.FMetodoAnalitico;
import org.freedom.modulos.std.view.frame.crud.plain.FUnidade;

import java.util.Vector;

public class FTipoAnalise extends FDados {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodTpAnalise = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtDescTpAnalise = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextAreaPad txaObsTpAnalise = new JTextAreaPad();

	private JTextFieldPad txtCodUnid = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldFK txtDescUnid = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCodMetodo = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldFK txtDescMetodo = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtTituloMet = new JTextFieldPad( JTextFieldPad.TP_STRING, 100, 0 );

	private Vector<String> vLabs1 = new Vector<String>();

	private Vector<String> vVals1 = new Vector<String>();

	private JRadioGroup<?, ?> rgTipo = null;

	ListaCampos lcUnid = new ListaCampos( this, "UD" );

	ListaCampos lcMetodo = new ListaCampos( this, "MA" );

	public FTipoAnalise() {

		setTitulo( "Tipos de Analise" );
		setAtribos( 50, 50, 435, 330 );
		montaListaCampos();
		montaTela();

	}

	private void montaTela() {

		vLabs1.addElement( "Mínimo e Máximo" );
		vLabs1.addElement( "Descritivo" );
		vVals1.addElement( "MM" );
		vVals1.addElement( "DT" );

		rgTipo = new JRadioGroup<String, String>( 1, 2, vLabs1, vVals1 );
		rgTipo.setVlrString( "MM" );

		adicCampo( txtCodTpAnalise, 7, 20, 100, 20, "CodTpAnalise", "Cód.Tp.An.", ListaCampos.DB_PK, true );
		adicCampo( txtDescTpAnalise, 110, 20, 300, 20, "DescTpAnalise", "Descrição do tipo de analise", ListaCampos.DB_SI, true );
		adicCampo( txtCodUnid, 7, 65, 100, 20, "CodUnid", "Cód.Unid", ListaCampos.DB_FK, txtDescUnid, false );
		adicDescFK( txtDescUnid, 110, 65, 300, 20, "DescUnid", "Descrição da Unidade" );
		adicCampo( txtCodMetodo, 7, 105, 100, 20, "CodMtAnalise", "Cód.Método", ListaCampos.DB_FK, txtDescMetodo, true );
		adicDescFK( txtDescMetodo, 110, 105, 300, 20, "DescMtAnalise", "Descrição do método analítico" );
		adicDB( txaObsTpAnalise, 7, 200, 402, 50, "ObsTpAnalise", "Observações", false );
		adicDB( rgTipo, 7, 150, 402, 30, "TipoExpec", "Tipo de expecificação", true );
		setListaCampos( true, "TIPOANALISE", "PP" );

	}

	private void montaListaCampos() {

		/*****************
		 * Unidade *
		 *****************/

		lcUnid.add( new GuardaCampo( txtCodUnid, "CodUnid", "Cód.Unidade", ListaCampos.DB_PK, null, false ) );
		lcUnid.add( new GuardaCampo( txtDescUnid, "DescUnid", "Descrição da unidade", ListaCampos.DB_SI, false ) );
		lcUnid.montaSql( false, "UNIDADE", "EQ" );
		lcUnid.setReadOnly( true );
		lcUnid.setQueryCommit( false );
		txtCodUnid.setListaCampos( lcUnid );
		txtCodUnid.setTabelaExterna( lcUnid, FUnidade.class.getCanonicalName() );

		/*****************
		 * Método *
		 *****************/

		lcMetodo.add( new GuardaCampo( txtCodMetodo, "CodMtAnalise", "Cód.Método", ListaCampos.DB_PK, null, false ) );
		lcMetodo.add( new GuardaCampo( txtDescMetodo, "DescMtAnalise", "Descrição do método analítico", ListaCampos.DB_SI, false ) );
		lcMetodo.add( new GuardaCampo( txtTituloMet, "TituloAnalise", "Titulo", ListaCampos.DB_SI, false ) );
		lcMetodo.montaSql( false, "METODOANALISE", "PP" );
		lcMetodo.setReadOnly( true );
		lcMetodo.setQueryCommit( false );
		txtCodMetodo.setListaCampos( lcMetodo );
		txtCodMetodo.setTabelaExterna( lcMetodo, FMetodoAnalitico.class.getCanonicalName() );
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcUnid.setConexao( cn );
		lcMetodo.setConexao( cn );
	}
}
