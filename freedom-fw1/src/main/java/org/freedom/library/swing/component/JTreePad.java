/**
 * @version 12/05/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 *
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.componentes <BR>
 * Classe: @(#)Arvore.java <BR>
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

import java.awt.Component;
import javax.swing.JTree;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.freedom.acao.ArvoreFace;

public class JTreePad extends JTree implements ArvoreFace {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArvoreFace face = this;

	public JTreePad() {
		setCellRenderer(new NohImg(this));
	}

	public void setImgTrat(ArvoreFace imgT) {
		face = imgT;
	}

	public ImageIcon getImagem(int iLinha, boolean bNoh, Object src) {
		return null;
	}

	public ImageIcon getImg(int iLinha, boolean bNoh, Object src) {
		return face.getImagem(iLinha, bNoh, src);
	}
}

class NohImg extends DefaultTreeCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTreePad pai = null;

	public NohImg(JTreePad p) {
		pai = p;
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		ImageIcon img = pai.getImg(row, leaf, value);
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		if (img != null)
			setIcon(img);
		return this;
	}

}
