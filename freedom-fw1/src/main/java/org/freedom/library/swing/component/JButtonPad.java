/**
 * @version 04/06/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.componentes <BR>
 * Classe:
 * @(#)JButtonPad.java <BR>
 * 
 * Este programa é licenciado de acordo com a LPG-PC (Licença Pública Geral para
 * Programas de Computador), <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * A LPG-PC deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste
 * Programa. <BR>
 * Caso uma cópia da LPG-PC não esteja disponível junto com este Programa, você
 * pode contatar <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa é
 * preciso estar <BR>
 * de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * Comentários da classe.....
 */

package org.freedom.library.swing.component;

import java.awt.Cursor;

import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;

import org.freedom.library.swing.frame.IFilho;

public class JButtonPad extends JButton {

	private static final long serialVersionUID = 1L;

	private int iCodSys = 0;

	private int iCodMod = 0;

	private int iCodIt = 0;

	private Class<? extends IFilho> tela = null;

	private String titulo = "";

	public int getICodIt() {
		return iCodIt;
	}

	public int getICodMod() {
		return iCodMod;
	}

	public int getICodSys() {
		return iCodSys;
	}

	public Class<? extends IFilho> getTela() {
		return tela;
	}

	public String getTitulo() {
		return titulo;
	}

	// private int iCodNiv = 0;

	public JButtonPad(Icon icon) {
		this(null, icon);
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	public JButtonPad(String text, Icon icon) {
		// Create the model
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		setModel(new DefaultButtonModel());
		// initialize
		init(text, icon);
		
	}

	/**
	 * Construtor da classe JButtonPad(). <BR>
	 * 
	 */

	public JButtonPad() {
		this(0, 0, 0, null, "");
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	public JButtonPad(String texto) {
		this(0, 0, 0, null, texto);
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	/**
	 * Construtor da classe JButtonPad(). <BR>
	 * Construtor que ja ajusta os paramatros basicos do JButtonPad.
	 * 
	 * @param tela
	 *            Tela que receberá o botão.
	 * @param titulo
	 *            Título do botão.
	 * 
	 */

	public JButtonPad(int iCodSistema, int iCodModulo, int iCodItem, Class<? extends IFilho> tela, String titulo) {

		this(iCodSistema, iCodModulo, iCodItem, tela, titulo, true);

	}

	/**
	 * Construtor da classe JButtonPad(). <BR>
	 * Construtor que ja ajusta os paramatros basicos do JButtonPad.
	 * 
	 * @param tela
	 *            Tela que receberá o botão.
	 * @param titulo
	 *            Título do botão.
	 * 
	 */
	public JButtonPad(int iCodSistema, int iCodModulo, int iCodItem, Class<? extends IFilho> tela, String titulo, boolean blabelbotao) {

		iCodSys = iCodSistema;
		iCodMod = iCodModulo;
		iCodIt = iCodItem;

		this.tela = tela;
		this.titulo = titulo;

		if (titulo != null && !"".equals(titulo) && getIcon() == null && blabelbotao) {
			this.setText(titulo);
		}

		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		// iCodNiv = iCodNivel;

	}

	/**
	 * Ajusta o código do sistema. <BR>
	 * 
	 * @param iCod
	 *            - Código do sistema.
	 * @see #getCodSistema
	 * 
	 */

	public void setCodSistema(int iCod) {
		iCodSys = iCod;
	}

	/**
	 * Retorna o código do sistema. <BR>
	 * 
	 * @return Código do sistema ou zero se o código não foi definido.
	 * @see #setCodSistema
	 * 
	 */

	public int getCodSistema() {
		return iCodSys;
	}

	/**
	 * Ajusta o código do módulo do sistema. <BR>
	 * 
	 * @param iCod
	 *            - Código do módulo.
	 * @see #getCodModulo
	 * 
	 */

	public void setCodModulo(int iCod) {
		iCodMod = iCod;
	}

	/**
	 * Retorna o código do módulo do sistema. <BR>
	 * 
	 * @return Código do módulo ou zero se o código não foi definido.
	 * @see #setCodModulo
	 * 
	 */

	public int getCodModulo() {
		return iCodMod;
	}

	/**
	 * Ajusta o código do item. <BR>
	 * O código do item é composto da seguinte forma: <BR>
	 * 8 casas decimais (caso as casas da direita n&atildeo forem usadas colocar
	 * 0)<BR>
	 * e mais uma unidade para detalhamentos longos. agrupadas de dois em dois,
	 * ex: 19|26|03|17|8. <BR>
	 * <BR>
	 * No exemplo o código do menu principal é: 19 <BR>
	 * submenu1: 26 <BR>
	 * submenu2: 03 <BR>
	 * submenu3: 17 <BR>
	 * item: 8 <BR>
	 * 
	 * @param iItem
	 *            - Código do item.
	 * @see #getCodItem
	 * 
	 */

	public void setCodItem(int iCod) {
		iCodIt = iCod;
	}

	/**
	 * Retorna o código do item.
	 * 
	 * @return Código do item ou zero se o código não foi definido.
	 * @see #setCodItem
	 * 
	 */

	public int getCodItem() {
		return iCodIt;
	}

}