/**
 * @version 23/01/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 *
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.componentes <BR>
 * Classe: @(#)ProcessoSec.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Caso uma cópia da LPG-P
 * C não esteja disponível junto com este Programa, você pode contatar <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA <BR> <BR>
 *
 * Comentários da classe.....
 */

package org.freedom.library.business.component;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;

import org.freedom.acao.Processo;

public class ProcessoSec implements Processo {
	private Thread th = null;
	private Timer tim = null;
	private Processo pPros = this;
	private Processo pTimer = this;

	public ProcessoSec(int iTempo, Processo pTim, Processo proc) {
		pPros = proc;
		pTimer = pTim;
		tim = new Timer(iTempo, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pTimer.run();
			}
		});
		th = new Thread(proc);
	}

	public void iniciar() {
		if (th == null)
			th = new Thread(pPros);
		th.start();
		tim.start();
	}

	public void parar() {
		th.interrupt();
		th = null;
		tim.stop();
	}

	public int getTempo() {
		return tim.getDelay();
	}

	public void run() {
	}
}
