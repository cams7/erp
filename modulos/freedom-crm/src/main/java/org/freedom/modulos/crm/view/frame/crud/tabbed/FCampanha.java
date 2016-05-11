/**
 * @version 20/01/2009 <BR>
 * @author Setpoint Informática Ltda.
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.crm <BR>
 *         Classe: @(#)FCampanha.java <BR>
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
 * 
 */
package org.freedom.modulos.crm.view.frame.crud.tabbed;

import java.awt.BorderLayout;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.frame.FTabDados;
import org.freedom.modulos.crm.view.frame.crud.plain.FEmail;

import javax.swing.JScrollPane;

public class FCampanha extends FTabDados {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodCampanha = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtDescCampanha = new JTextFieldPad( JTextFieldPad.TP_STRING, 80, 0 );

	private JTextFieldPad txtCodEmail = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtDescEmail = new JTextFieldFK( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextAreaPad txaObsCampanha = new JTextAreaPad( 500 );

	private ListaCampos lcEmail = new ListaCampos( this, "EM" );

	private ListaCampos lcEmailFK = new ListaCampos( this, "EM" );

	private Navegador navEmail = new Navegador( true );

	private JTablePad tabEmail = new JTablePad();

	private JScrollPane spnGrupos = new JScrollPane( tabEmail );

	private JPanelPad pnEmail = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinCampanha = new JPanelPad();

	private JPanelPad pinEmails = new JPanelPad( 0, 80 );

	public FCampanha() {

		super();
		setTitulo( "Campanhas" );
		setAtribos( 15, 30, 450, 350 );

		lcEmail.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcEmail );
		lcEmail.setTabela( tabEmail );

		montaListaCampos();
		montaTela();

	}

	private void montaListaCampos() {

		lcEmailFK.add( new GuardaCampo( txtCodEmail, "CodEmail", "Cód.email.", ListaCampos.DB_PK, true ) );
		lcEmailFK.add( new GuardaCampo( txtDescEmail, "DescEmail", "Descrição do email", ListaCampos.DB_SI, false ) );
		lcEmailFK.montaSql( false, "EMAIL", "TK" );
		lcEmailFK.setReadOnly( true );
		lcEmailFK.setQueryCommit( false );
		txtCodEmail.setTabelaExterna( lcEmailFK, FEmail.class.getCanonicalName() );
	}

	private void montaTela() {

		pinCampanha = new JPanelPad( 500, 330 );
		setPainel( pinCampanha );
		adicTab( "Campanha", pinCampanha );

		adicCampo( txtCodCampanha, 7, 25, 70, 20, "CodCamp", "Cód.Camp.", ListaCampos.DB_PK, true );
		adicCampo( txtDescCampanha, 80, 25, 320, 20, "DescCamp", "Descrição da campanha", ListaCampos.DB_SI, true );
		adicDB( txaObsCampanha, 7, 65, 395, 180, "ObsCamp", "Observação", false );
		setListaCampos( false, "CAMPANHA", "TK" );

		setPainel( pinEmails, pnEmail );
		adicTab( "Emails", pnEmail );
		setListaCampos( lcEmail );
		setNavegador( navEmail );
		pnEmail.add( pinEmails, BorderLayout.SOUTH );
		pnEmail.add( spnGrupos, BorderLayout.CENTER );

		pinEmails.adic( navEmail, 0, 50, 270, 25 );

		adicCampo( txtCodEmail, 7, 20, 80, 20, "CodEmail", "Cód.Email.", ListaCampos.DB_PF, txtDescEmail, true );
		adicDescFK( txtDescEmail, 90, 20, 220, 20, "DescEmail", "Descrição do email" );
		setListaCampos( false, "CAMPANHAEMAIL", "TK" );

		lcEmail.montaTab();
		lcEmail.setQueryInsert( false );
		lcEmail.setQueryCommit( false );

		tabEmail.setTamColuna( 300, 1 );

		lcCampos.setQueryInsert( false );
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcEmail.setConexao( cn );
		lcEmailFK.setConexao( cn );

	}

}
