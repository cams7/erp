/*
 * Projeto: Freedom-nfe
 * Pacote: org.freedom.modules.nfe.event
 * Classe: @(#)NFEListener.java
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA <BR> <BR>
 */
package org.freedom.modules.nfe.event;

/**
 * Ouvinte de Evento de NF-e.<br>
 * 
 * Esta interface prepara a classe que a implementar, para receber ações do
 * evento de NF-e.<br>
 * 
 * @see org.freedom.modules.nfe.event.NFEEvent
 * 
 * @author Setpoint Informática Ltda./Robson Sanchez
 * @version 03/08/2009
 */
public interface NFEListener {

	public void beforeValidSend(NFEEvent e);

	public void afterValidSend(NFEEvent e);

	public void beforeRunSend(NFEEvent e);

	public void afterRunSend(NFEEvent e);
}
