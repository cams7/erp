/**
 * @version 29/10/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FItNatOper.java <BR>
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
 *         Detalhe para natureza da operação...
 * 
 */

package org.freedom.modulos.std.view.frame.crud.detail;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDetalhe;
import org.freedom.modulos.lvf.view.frame.crud.plain.FTabICMS;

public class FItNatoper extends FDetalhe {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCab = new JPanelPad();

	private JPanelPad pinDet = new JPanelPad();

	private JTextFieldPad txtCodNat = new JTextFieldPad( JTextFieldPad.TP_STRING, 5, 0 );

	private JTextFieldPad txtDescNat = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodItNatoper = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtUFTabICMS = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtAliqTabICMS = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 9, 2 );

	private ListaCampos lcTabICMS = new ListaCampos( this, "TI" );

	public FItNatoper() {

		setTitulo( "Cadastro de alíquotas por estado" );
		setAtribos( 50, 50, 430, 300 );

		txtCodNat.setAtivo( false );
		txtDescNat.setAtivo( false );

		setListaCampos( lcCampos );
		setAltCab( 85 );
		setPainel( pinCab, pnCliCab );
		adicCampo( txtCodNat, 7, 20, 80, 20, "CodNat", "Cód.nat.op.", ListaCampos.DB_PK, true );
		adicCampo( txtDescNat, 90, 20, 220, 20, "DescNat", "Descrição da natureza da operação", ListaCampos.DB_SI, true );
		setListaCampos( true, "NATOPER", "LF" );
		lcCampos.setReadOnly( true );
		txtCodNat.setStrMascara( "#.###" );

		lcTabICMS.add( new GuardaCampo( txtUFTabICMS, "UFTI", "Cód.alq.", ListaCampos.DB_PK, true ) );
		lcTabICMS.add( new GuardaCampo( txtAliqTabICMS, "ALIQTI", "Alíquota", ListaCampos.DB_SI, false ) );
		lcTabICMS.montaSql( false, "TABICMS", "LF" );
		lcTabICMS.setQueryCommit( false );
		lcTabICMS.setReadOnly( true );
		txtUFTabICMS.setTabelaExterna( lcTabICMS, FTabICMS.class.getCanonicalName() );

		setAltDet( 60 );
		setPainel( pinDet, pnDet );
		setListaCampos( lcDet );
		setNavegador( navRod );
		adicCampo( txtCodItNatoper, 7, 20, 60, 20, "CodItNatoper", "Item", ListaCampos.DB_PK, true );
		adicCampo( txtUFTabICMS, 70, 20, 77, 20, "UFTI", "UF", ListaCampos.DB_FK, txtAliqTabICMS, true );
		adicDescFK( txtAliqTabICMS, 150, 20, 150, 20, "ALIQTI", "Alíquota" );
		setListaCampos( true, "ITNATOPER", "LF" );
		lcDet.setQueryInsert( false );

		montaTab();

	}

	public void exec( String sCodFisc ) {

		txtCodNat.setVlrString( sCodFisc );
		lcCampos.carregaDados();
	}

	public void setConexao( DbConnection cn ) {

		lcTabICMS.setConexao( cn );
		super.setConexao( cn );
	}
}
