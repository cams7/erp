/**
 * @version 20/01/2009 <BR>
 * @author Setpoint Informática Ltda.
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.crm <BR>
 *         Classe: @(#)FConfEmail.java <BR>
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

package org.freedom.modulos.crm.view.frame.crud.plain;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.PostEvent;
import org.freedom.infra.util.crypt.SimpleCrypt;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JPasswordFieldPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;

public class FConfEmail extends FDados implements CarregaListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodConfEmail = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 15, 0 );

	private JTextFieldPad txtEmailRemet = new JTextFieldPad( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtNomeRemet = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtUsuarioRemet = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JPasswordFieldPad txtSenhaRemet = new JPasswordFieldPad( 40 );

	private JTextFieldPad txtEmailResp = new JTextFieldPad( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtHostSmpt = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtUsaSSL = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtUsaAutSMTP = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtPortaSMTP = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 15, 0 );

	private final JCheckBoxPad cbUsaSSL = new JCheckBoxPad( "Usa SSL ?", "S", "N" );

	private final JCheckBoxPad cbUsaAutSmtp = new JCheckBoxPad( "Autentica ?", "S", "N" );
	
	private final JCheckBoxPad cbCriptSenha = new JCheckBoxPad( "Criptografar senha ?", "S", "N");

	private JTextAreaPad txaAssinatRemet = new JTextAreaPad( 1000 );

	public FConfEmail() {

		super();
		setTitulo( "Configuração de email" );
		setAtribos( 15, 30, 433, 460 );
		montaTela();

	}

	private void montaTela() {

		adicCampo( txtCodConfEmail, 7, 25, 70, 20, "CodConfEmail", "Cód.Conf.", ListaCampos.DB_PK, true );
		adicCampo( txtNomeRemet, 80, 25, 330, 20, "NomeRemet", "Nome remetente", ListaCampos.DB_SI, false );
		adicCampo( txtEmailRemet, 7, 65, 200, 20, "EmailRemet", "Email remetente", ListaCampos.DB_SI, false );
		adicCampo( txtEmailResp, 210, 65, 200, 20, "EmailResp", "Email resposta", ListaCampos.DB_SI, false );
		adicCampo( txtHostSmpt, 7, 105, 300, 20, "HostSmtp", "Host SMTP", ListaCampos.DB_SI, false );
		adicCampo( txtPortaSMTP, 310, 105, 100, 20, "PortaSmtp", "Porta SMTP", ListaCampos.DB_SI, false );
		adicCampo( txtUsuarioRemet, 7, 145, 100, 20, "UsuarioRemet", "Usuário", ListaCampos.DB_SI, false );
		adicCampo( txtSenhaRemet, 110, 145, 95, 20, "SenhaRemet", "Senha", ListaCampos.DB_SI, false );
		adicDB( cbCriptSenha, 7, 165, 402, 20, "CriptSenha", "", true );
		adicDB( txaAssinatRemet, 7, 205, 422, 155, "AssinatRemet", "Assinatura", false );

		adicDB( cbUsaSSL, 208, 145, 95, 20, "UsaSSL", "", false );
		adicDB( cbUsaAutSmtp, 300, 145, 130, 20, "UsaAutSMTP", "", false );
		setListaCampos( true, "CONFEMAIL", "TK" );
		lcCampos.addCarregaListener( this );
	
	}

	public void afterCarrega( CarregaEvent cevt ) {

		if (cevt.getListaCampos()==lcCampos) {
			if ("S".equals( cbCriptSenha.getVlrString() )) {
				txtSenhaRemet.setVlrString( SimpleCrypt.decrypt( txtSenhaRemet.getVlrString() ) );
			}
		}
		
	}

	public void beforeCarrega( CarregaEvent cevt ) {

		
	}

	public void beforePost(PostEvent pevt) {
		super.beforePost( pevt);
		if (pevt.getListaCampos()==lcCampos) {
			if ("S".equals( cbCriptSenha.getVlrString() )) {
				txtSenhaRemet.setVlrString( SimpleCrypt.crypt( txtSenhaRemet.getVlrString() ) );
			}
		}
	}

	public void afterPost(PostEvent pevt) {
		super.afterPost( pevt );
		
	}
	
	
}
