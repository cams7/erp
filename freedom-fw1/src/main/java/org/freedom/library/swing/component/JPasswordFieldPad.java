/**
 * @version 08/01/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 *
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.componentes <BR>
 * Classe: @(#)JPasswordFieldPad.java <BR>
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

package org.freedom.library.swing.component;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JPasswordField;

import org.freedom.library.persistence.ListaCampos;

public class JPasswordFieldPad extends JPasswordField {

	private static final long serialVersionUID = 1L;
	private int iTam = 0;
	private ListaCampos lcTxt = null;
	private boolean bEnterPula = true;

	/**
	 * Construtor da classe mesmo que JPasswordFieldPad(0). <BR>
	 * 
	 */

	public JPasswordFieldPad() {
		this(0);
	}

	/**
	 * Construtor da classe com tamanho do campo pré-estabelecido. <BR>
	 * Carrega as heranças e adiciona keyListener e focusListener.
	 * 
	 */

	public JPasswordFieldPad(int iTamanho) {
		iTam = iTamanho;
		addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent kevt) {
				if (kevt.getKeyChar() == KeyEvent.VK_BACK_SPACE)
					return;

				if (kevt.getKeyChar() == KeyEvent.VK_ENTER && bEnterPula) {
					transferFocus();
					return;
				}
				if (getPassword().length + 1 > iTam && ( getSelectedText() == null )) { // +1
					// pq
					// o
					// keyTyped
					// vem
					// depois
					// que
					// o
					// char
					// foi
					// impresso.
					kevt.setKeyChar(( char ) KeyEvent.VK_UNDEFINED);
				}
				if (validaChar(kevt.getKeyChar()) && ( lcTxt != null )) {
					lcTxt.edit();
				}
			}
		}

		);
		addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent fevt) {
				selectAll();
			}
		}

		);

	}

	/**
	 * Ajusta o tamanho maximo do campo. <BR>
	 * 
	 * @param iTamanho
	 *            - Tamanho do campo.
	 * @see #getTamanho
	 * 
	 */

	public void setTamanho(int iTamanho) {
		iTam = iTamanho;
	}

	/**
	 * Retorna o tamanho maximo do campo. <BR>
	 * 
	 * @return Tamanho do campo ou zero se o tamanho não foi definido.
	 * @see #setTamanho
	 * 
	 */

	public int getTamanho() {
		return iTam;
	}

	/**
	 * Adiciona um ListaCampos a este campo. <BR>
	 * O ListaCampos aqui adionado será informado sobre os status de edição.
	 * 
	 * @param lcCampos
	 *            - ListaCampos a ser adicionado.
	 * @see #getListaCampos
	 * 
	 */

	public void setListaCampos(ListaCampos lcCampos) {
		lcTxt = lcCampos;
	}

	/**
	 * Retorna o ListaCampos que foi adicionado neste campo. <BR>
	 * 
	 * @return ListaCampos do password ou null.
	 * @see #setListaCampos
	 * 
	 */

	public ListaCampos getListaCampos() {
		return lcTxt;
	}

	/**
	 * Ajusta o campo para pular automaticamente para o proximo foco<BR>
	 * quando a tecla enter é pressionada.
	 * 
	 * @param bValEnterPula
	 *            - Se verdadeiro o enter troca de foco, se falso não troca.
	 * @see #getEnterPula
	 * 
	 */

	public void setEnterPula(boolean bValEnterPula) {
		bEnterPula = bValEnterPula;
	}

	/**
	 * Retorna se o campo troca de foco quando a tecla enter é pressionada.
	 * 
	 * @return Se verdadeiro o enter troca de foco, se falso não troca.
	 * @see #setEnterPula
	 * 
	 */

	public boolean getEnterPula() {
		return bEnterPula;
	}

	/**
	 * Insere um valor string no campo.<BR>
	 * 
	 * @param sVal
	 *            - String a ser inserida.
	 * @see #getVlrString
	 * 
	 */

	public void setVlrString(String sVal) {
		if (sVal != null)
			setText(sVal.trim());
		else
			setText("");
	}

	/**
	 * Retorna um valor string com a senha.
	 * 
	 * @return String com a senha.
	 * @see #setVlrString
	 * 
	 */

	public String getVlrString() {
		return new String(getPassword());
	}

	private boolean validaChar(char cVal) {
		boolean bValido = false;

		if (( ( cVal >= KeyEvent.VK_COMMA ) && ( cVal <= KeyEvent.VK_DIVIDE ) ) || ( ( cVal >= KeyEvent.VK_AMPERSAND ) && ( cVal <= KeyEvent.VK_BRACERIGHT ) )
				|| ( ( cVal >= KeyEvent.VK_AT ) && ( cVal <= KeyEvent.VK_UNDERSCORE ) )) {
			bValido = true;
		} // Campos imprimíveis!

		return bValido;
	}
}
