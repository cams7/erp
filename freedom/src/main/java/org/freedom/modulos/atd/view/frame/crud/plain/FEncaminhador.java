/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Carlos Eduardo Caetano David <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.atd <BR>
 *         Classe: @(#)FEncaminhador.java <BR>
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

package org.freedom.modulos.atd.view.frame.crud.plain;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;

import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;

public class FEncaminhador extends FDados implements PostListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodEnc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtNomeEnc = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtEndEnc = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNumEnc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtBairEnc = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtComplEnc = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCidEnc = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtCepEnc = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtFoneEnc = new JTextFieldPad( JTextFieldPad.TP_STRING, 12, 0 );

	private JTextFieldPad txtFaxEnc = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtUFEnc = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	public FEncaminhador() {

		super();

		nav.setNavigation( true );

		setTitulo( "Cadastro de Encaminhador" );
		setAtribos( 50, 50, 400, 250 );

		adicCampo( txtCodEnc, 7, 20, 50, 20, "CodEnc", "Cód.enc.", ListaCampos.DB_PK, true );
		adicCampo( txtNomeEnc, 60, 20, 312, 20, "NomeEnc", "Descrição do encaminhador", ListaCampos.DB_SI, true );
		adicCampo( txtEndEnc, 7, 60, 260, 20, "EndEnc", "Endereço", ListaCampos.DB_SI, false );
		adicCampo( txtNumEnc, 270, 60, 50, 20, "NumEnc", "Num.", ListaCampos.DB_SI, false );
		adicCampo( txtComplEnc, 323, 60, 49, 20, "ComplEnc", "Compl.", ListaCampos.DB_SI, false );
		adicCampo( txtBairEnc, 7, 100, 120, 20, "BairEnc", "Bairro", ListaCampos.DB_SI, false );
		adicCampo( txtCidEnc, 130, 100, 120, 20, "CidEnc", "Cidade", ListaCampos.DB_SI, false );
		adicCampo( txtCepEnc, 253, 100, 80, 20, "CepEnc", "Cep", ListaCampos.DB_SI, false );
		adicCampo( txtUFEnc, 336, 100, 36, 20, "UFEnc", "UF", ListaCampos.DB_SI, false );
		adicCampo( txtFoneEnc, 7, 140, 120, 20, "FoneEnc", "Telefone", ListaCampos.DB_SI, false );
		adicCampo( txtFaxEnc, 130, 140, 120, 20, "FaxEnc", "Fax", ListaCampos.DB_SI, false );

		txtFoneEnc.setMascara( JTextFieldPad.MC_FONEDDD );
		txtFaxEnc.setMascara( JTextFieldPad.MC_FONE );
		lcCampos.addPostListener( this );
		lcCampos.setQueryInsert( false );
	}

	public void beforePost( PostEvent pevt ) {

		if ( txtUFEnc.getText().trim().length() < 2 ) {
			pevt.cancela();
			Funcoes.mensagemInforma( this, "Campo UF é requerido! ! !" );
			txtUFEnc.requestFocus();
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		setListaCampos( true, "ENCAMINHADOR", "AT" );
	}
}
