/**
 * @version 19/09/2011 <BR>
 * @author Setpoint Informática Ltda./Bruno Nascimento <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.tmk <BR>
 *         Classe: @(#)DLRegBatida.java <BR>
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
 *         Tela que registra Batida de ponto do empregado.
 * 
 */

package org.freedom.modulos.crm.view.dialog.utility;

import java.awt.event.ActionEvent;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.dialog.FFDialogo;

public class DLPesqObsAtendo extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JTextAreaPad txaPesqObsAtend = new JTextAreaPad();
	
	private String mensagem = null;
	
	public DLPesqObsAtendo(String filtroObs) {
		this();
		txaPesqObsAtend.setText( filtroObs );
	}
	public DLPesqObsAtendo() {

		super();
		setTitulo( "Pesquisa atendimento." );
		setAtribos( 50, 50, 430, 200 );
		montaTela();
	
	}
	
	public void montaTela(){

		adic( txaPesqObsAtend, 7, 25, 400, 80, "Descrição Atendimento" );
		
	}
	
	public void actionPerformed( ActionEvent evt ) {
		boolean result = false;
		if (evt.getSource()==btOK) {
            mensagem = txaPesqObsAtend.getVlrString();
		} 
		super.actionPerformed( evt );
	
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
	}

	
	public String getMensagem() {
	
		return mensagem;
	}

	
	public void setMensagem( String mensagem ) {
	
		this.mensagem = mensagem;
	}
}
