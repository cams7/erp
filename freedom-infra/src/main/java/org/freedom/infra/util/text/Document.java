package org.freedom.infra.util.text;

import javax.swing.text.PlainDocument;

/**
 * Projeto: <a
 * href="http://sourceforge.net/projects/freedom-erp/">Freedom-infra</a> <br>
 * Este programa é licenciado de acordo com a LPG-PC <br>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada
 * pela Fundação do Software Livre (FSF); <BR>
 * <br>
 * 
 * Document extende de <code>javax.swing.text.PlainDocument</code> para prover o
 * vínculo básico para a utilização da máscara com a classe <code>Mask</code>
 * 
 * @see javax.swing.text.PlainDocument
 * @see org.freedom.infra.util.text.Mask
 * 
 * @author Alex Rodrigues
 * @version 0.0.1 – 16/05/2008
 * 
 * @since 16/05/2008
 */
public abstract class Document extends PlainDocument {

	private static final long serialVersionUID = 1l;

	/**
	 * Máscara a ser utilizada.
	 */
	protected Mask mask;

	/**
	 * Índice para encontrar o caracter correspondente na máscara.
	 */
	protected int index = 0;

	public Document() {
		super();
	}

	public Document(final Content c) {
		super(c);
	}

	public Mask getMask() {
		return mask;
	}

	public void setMask(final Mask mask) {
		this.mask = mask;
	}
}
