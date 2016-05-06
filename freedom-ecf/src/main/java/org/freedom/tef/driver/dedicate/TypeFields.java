
package org.freedom.tef.driver.dedicate;

public enum TypeFields {
	/**
	 * Buffer contém a primeira via do comprovante de pagamento (via do cliente) a ser impressa na impressora fiscal. Essa via, quando possível, é reduzida de forma a ocupar poucas linhas na impressora. Pode ser um comprovante de venda ou
	 * administrativo
	 */
	VOUCHER_TEF( 121 ),
	/**
	 * Buffer contém a segunda via do comprovante de pagamento (via do caixa) a ser impresso na impressora fiscal. Pode ser um comprovante de venda ou administrativo
	 */
	VOUCHER_TEF_TWO( 122 );

	private Integer code;

	private TypeFields( Integer code ) {

		this.code = code;
	}

	public Integer code() {

		return this.code;
	}
}
