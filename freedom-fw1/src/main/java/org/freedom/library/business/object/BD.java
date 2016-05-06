/**
 * @version 23/01/2003 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 *
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.componentes <BR>
 * Classe: @(#)JMenuItemPad.java <BR>
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
 * Comentários da classe.....
 */

package org.freedom.library.business.object;

public class BD {
	private String sIDObj = "";
	private String sDescObj = "";
	private String sTipoObj = "";
	private String sComentObj = "";
	private String sUsomeObj = "";

	public BD(String sIDObj, String sDescObj, String sTipoObj, String sComentObj, String sUsomeObj) {
		this.sIDObj = sIDObj;
		this.sDescObj = sDescObj;
		this.sComentObj = sComentObj;
		this.sUsomeObj = sUsomeObj;
		this.sTipoObj = sTipoObj;

	}

	public String getIDObj() {
		return this.sIDObj;
	}

	public String getDescObj() {
		return this.sDescObj;
	}

	public String getTipoObj() {
		return this.sTipoObj;
	}

	public String setComentObj() {
		return this.sComentObj;
	}

	public String getUsomeObj() {
		return this.sUsomeObj;
	}

	public void setIDObj(String sIDObj) {
		this.sIDObj = sIDObj;
	}

	public void setDescObj(String sDescObj) {
		this.sDescObj = sDescObj;
	}

	public void setTipoObj(String sTipoObj) {
		this.sTipoObj = sTipoObj;
	}

	public void setComentObj(String sComentObj) {
		this.sComentObj = sComentObj;
	}

	public void setUsomeObj(String sUsomeObj) {
		this.sUsomeObj = sUsomeObj;
	}

}
