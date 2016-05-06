package org.freedom.infra.util.text;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.GapContent;

/**
 * Projeto: <a
 * href="http://sourceforge.net/projects/freedom-erp/">Freedom-infra</a> <br>
 * Este programa é licenciado de acordo com a LPG-PC <br>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada
 * pela Fundação do Software Livre (FSF); <BR>
 * <br>
 * 
 * O objetivo de DecimalDocument é possibilitar a aplicação de uma mascara para
 * números de ponto flutuante. Assim como em <code>IntegerDocument</code> esta,
 * somente tratará algarismos númericos, e além da desta validação e a de
 * negativo, valida-se também o número de casas decimais.
 * 
 * @see Document
 * @see org.freedom.infra.util.text.Mask
 * 
 * @author Alex Rodrigues
 * @version 0.0.2 – 16/05/2008
 * 
 * @since 13/05/2008
 */
public class DecimalDocument extends Document {

	private static final long serialVersionUID = 1l;

	private int precision = 0;

	private int indexDecimal = -1;

	public DecimalDocument() {
		this(new GapContent(), 15, 5);
	}

	public DecimalDocument(final Content c) {
		this(c, 15, 5);
	}

	public DecimalDocument(final int size, final int precision) {
		this(new GapContent(), size, precision);
	}

	public DecimalDocument(final Content c, final int size, final int precision) {
		super(c);
		setMask(Mask.createDecimal(size, precision));
		this.precision = precision;
	}

	/**
	 * Este metodo foi sobrescrito para que seja possivél, validar-se o texto a
	 * ser inserido no documento, e formata-lo conforme a mascara, antes do
	 * texto ser inserido no documento.
	 * 
	 * @see #validateMask(String, int)
	 * 
	 * @since 13/05/2008
	 */
	@Override
	public void insertString(final int offs, final String str, final AttributeSet a) throws BadLocationException {

		StringBuilder newstr = new StringBuilder(mask != null ? validateMask(str, offs) : str);
		super.insertString(offs, newstr.toString(), a);
	}

	/**
	 * Este metodo foi sobrescrito para que seja possivél atualizar o indice da
	 * posição na mascara.
	 * 
	 * @since 13/05/2008
	 */
	@Override
	public void remove(final int offs, final int len) throws BadLocationException {

		if (mask != null) {
			if (getText(offs, len).indexOf('-') > -1) {
				mask.setLength(mask.length() - 1);
				super.remove(offs, len);
			}
			else {
				super.remove(offs, len);
				index = getLength();
				if (getText(0, index).indexOf('-') > -1) {
					--index;
				}
			}
		}
		else {
			super.remove(offs, len);
		}
	}

	/**
	 * Inicia a validação, definindo variáveis e passando o fluxo para validação
	 * auxiliar.
	 * 
	 * @see #validateMaskAux(char, int, StringBuilder, int)
	 * 
	 * @since 13/05/2008
	 */
	private String validateMask(String str, int offs) throws BadLocationException {

		StringBuilder newstr = new StringBuilder();

		try {

			if (offs >= mask.length()) {
				return newstr.toString();
			}

			final char[] value = str.toCharArray();

			indexDecimal = getText(0, getLength()).indexOf(Mask.DECIMAL);
			int decimalCount = 0;

			if (indexDecimal > -1 && indexDecimal < ( mask.length() - 1 )) {
				String text = getText(0, getLength());
				decimalCount = text.substring(indexDecimal).length() - 1;
			}

			for (int i = 0; i < value.length; i++) {
				validateMaskAux(value[i], offs, newstr, decimalCount);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return newstr.toString();
	}

	/**
	 * Distribuio o fluxo para as verificações de negativo, inteiro e decimal.
	 * 
	 * @see #validateMaskAuxNegative(char, int, StringBuilder)
	 * @see #validateMaskAuxDecimal(char, int, StringBuilder, int)
	 * @see #validateMaskAuxDigit(char, int, StringBuilder, int)
	 * 
	 * @since 13/05/2008
	 */
	private void validateMaskAux(char value, int offs, StringBuilder str, int decimalCount) throws BadLocationException {

		if (index >= mask.length()) {
			if (!validateMaskAuxNegative(value, offs, str)) {
				return;
			}
		}
		else if (validateMaskAuxNegative(value, offs, str)) {
		}
		else if (index < mask.getChars().length && validateMaskAuxDecimal(value, offs, str, decimalCount)) {
		}
		else if (validateMaskAuxDigit(value, offs, str, decimalCount)) {
		}
	}

	/**
	 * Se o valor a ser inserido for negativo ( '-' ), este somente poderá ser
	 * feito se for o primeiro indice no buffer.
	 * 
	 * @since 13/05/2008
	 */
	private boolean validateMaskAuxNegative(char value, int offs, StringBuilder str) throws BadLocationException {

		if (value == Mask.NEGATIVE && offs == 0 && getText(0, getLength()).indexOf(Mask.NEGATIVE) == -1) {
			mask.setLength(mask.length() + 1);
			str.append(value);
			return true;
		}

		return false;
	}

	/**
	 * Validação para decimal.<br>
	 * Esta se sub-divide para duas outras validações diferenciadas, uma pelo
	 * valor e outra pela mascara.
	 * 
	 * @see #validateMaskAuxDecimalMask(char, int, StringBuilder, int)
	 * @see #validateMaskAuxDecimalValue(char, int, StringBuilder, int)
	 * 
	 * @since 13/05/2008
	 */
	private boolean validateMaskAuxDecimal(char value, int offs, StringBuilder str, int decimalCount) throws BadLocationException {

		boolean decimal = validateMaskAuxDecimalMask(value, offs, str, decimalCount) || validateMaskAuxDecimalValue(value, offs, str, decimalCount);
		return decimal;
	}

	/**
	 * Faz a validação de todo o contexto de mascara e valor, pois o indice da
	 * mascara aponta para a separação decimal.
	 * 
	 * @since 13/05/2008
	 */
	private boolean validateMaskAuxDecimalMask(char value, int offs, StringBuilder str, int decimalCount) throws BadLocationException {

		final char[] chars = mask.getChars();

		boolean decimal = false;

		if (Character.isDigit(value) && chars[index] == Mask.DECIMAL) {
			if (getText(0, getLength()).indexOf(Mask.DECIMAL) == -1) {
				str.append(Mask.DECIMAL);
				index++;
			}
			if (Character.isDigit(value)) {
				str.append(value);
				index++;
			}
			decimal = true;
		}

		return decimal;
	}

	/**
	 * Faz a validação de todo o contexto da mascara e valor, pois o valor a ser
	 * inserido no buffer é ',' ou '.', que caracterizam a separação decimal.
	 * 
	 * @since 13/05/2008
	 */
	private boolean validateMaskAuxDecimalValue(char value, int offs, StringBuilder str, int decimalCount) throws BadLocationException {

		boolean decimal = false;

		if (( value == Mask.DECIMAL || value == '.' ) && decimalCount < precision && indexDecimal == -1) {
			if (getLength() + str.length() == 0 || str.length() > 0 && str.charAt(0) == Mask.NEGATIVE) {
				str.append('0');
				index++;
			}
			str.append(Mask.DECIMAL);
			indexDecimal = index;
			index++;
			decimal = true;
		}

		return decimal;
	}

	/**
	 * Verifica se o valor a ser inserido ao buffer é um inteiro.
	 * 
	 * @since 13/05/2008
	 */
	private boolean validateMaskAuxDigit(char value, int offs, StringBuilder str, int decimalCount) throws BadLocationException {

		if (Character.isDigit(value)) {
			int size = getText(0, indexDecimal > -1 ? ( indexDecimal < getLength() ? indexDecimal : getLength() ) : 0).length() + precision + 1; // soma
			// um
			// por
			// conta
			// do
			// separador
			// decimal.

			if (( decimalCount < precision && offs > indexDecimal ) || ( offs <= indexDecimal && getLength() + str.length() < mask.length() && size < mask.length() )) {
				str.append(value);
				index++;
			}
			return true;
		}

		return false;
	}

}
