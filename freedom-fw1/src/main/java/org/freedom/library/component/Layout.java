/**
 * @version 05/07/2005 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 *
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.layout <BR>
 * Classe: @(#)Layout.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA <BR> <BR>
 *
 * Comentários para a classe...
 */

package org.freedom.library.component;

import org.freedom.library.business.component.NF;
import org.freedom.library.swing.frame.Aplicativo;

public class Layout extends Object {
	// public boolean bEntrada = false;
	protected TabVector cab = null;
	protected TabVector itens = null;
	protected TabVector parc = null;
	protected TabVector adic = null;
	protected TabVector frete = null;
	protected int casasDec = Aplicativo.casasDec;
	protected int casasDecFin = Aplicativo.casasDecFin;
	protected int casasDecPre = Aplicativo.casasDecPre;

	public Layout() {
	}

	public boolean imprimir(NF nf, ImprimeOS imp) {
		cab = nf.getTabVector(NF.T_CAB);
		itens = nf.getTabVector(NF.T_ITENS);
		parc = nf.getTabVector(NF.T_PARC);
		adic = nf.getTabVector(NF.T_ADIC);
		frete = nf.getTabVector(NF.T_FRETE);
		casasDec = nf.getCasasDec();
		return false;
	}

}
