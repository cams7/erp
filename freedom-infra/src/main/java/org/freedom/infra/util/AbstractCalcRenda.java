package org.freedom.infra.util;

import java.math.BigDecimal;

import org.freedom.infra.model.jdbc.DbConnection;

/*
 * Projeto: Freedom-nfe
 * Pacote: org.freedom.modules.nfe.control
 * Classe: @(#)AbstractNFEFactory.java
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

/**
 * Classe padrão para implementação de calculo de renda.
 * 
 * @author Setpoint Informática Ltda./Anderson Sanchez
 * @version 26/01/2010
 */

public abstract class AbstractCalcRenda {

	public abstract void inicializa(Integer ticket, Integer coditrecmerc, DbConnection con);

	public abstract Integer getRenda();

	public abstract BigDecimal getMedia();

	public abstract boolean isInicialized();

}
