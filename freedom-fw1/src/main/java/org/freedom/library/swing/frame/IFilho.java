/**
 * @version 18/04/2005 <BR>
 * @author Setpoint Informática Ltda / Alexandre Marcondes.
 * 
 * Projeto: Freedom <BR>
 * 
 * Pacote: org.freedom.telas <BR>
 * Classe:
 * @(#)IFilho.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * ? <BR>
 * 
 */
package org.freedom.library.swing.frame;

import java.awt.Component;
import java.awt.Container;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.exceptions.ExceptionSetConexao;
import org.freedom.library.swing.component.JPanelPad;

public interface IFilho {
	public abstract void setTitulo(String tit, String name);

	public abstract void setAtribos(int Esq, int Topo, int Larg, int Alt);

	public abstract void setTela(Container c);

	public abstract Container getTela();

	public abstract JPanelPad adicBotaoSair();

	public abstract void setFirstFocus(Component firstFocus);

	public abstract void firstFocus();

	public abstract void setConexao(DbConnection cn) throws ExceptionSetConexao;

	public abstract void execShow();

	public abstract boolean getInitFirstFocus();

	public abstract void setInitFirstFocus(boolean initFirstFocus);

	public abstract void setTelaPrim(FPrincipal fP);
}