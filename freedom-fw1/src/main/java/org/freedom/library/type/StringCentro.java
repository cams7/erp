/**
 * @version 18/05/2011 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 *
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.library.type <BR>
 * Classe: @(#)StringCentro.java <BR>
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
 * Classe para permitir a ordenação de textos centralizados no JTablePad.
 */

package org.freedom.library.type;

public class StringCentro implements Comparable<String> {
	private String sTexto = "";

	public StringCentro(String sTexto) {
		if (sTexto != null)
			this.sTexto = sTexto.trim();
	}

	public String toString() { 
		return sTexto;
	}

	public int compareTo(String arg0) {
		return sTexto.compareTo(arg0.toString());
	}
	
	public int compareTo(StringCentro arg0) {
		return sTexto.compareTo(arg0.toString());
	}
	
	
}
