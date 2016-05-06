
package org.freedom.ecf.app;

public enum EParametro {
	PARAM_CODIGO( "Código" ), PARAM_DESCRICAO( "Descrição" ), PARAM_ALIQUOTA( "Aliquota" ), PARAM_TIPO_QUANTIDADE( "Tipo da quantidade" ), PARAM_QUANTIDADE( "Quantidade" ), PARAM_CASAS_DECIMAIS( "Casas decimais" ), PARAM_VALOR_UNITARIO(
			"Valor unitário" ), PARAM_TIPO_DESCONTO( "Tipo de desconto" ), PARAM_DESCONTO( "Desconto" ), PARAM_ACRECIMO( "Acrécimo" ), PARAM_DEPARTAMENTO( "Departamento" ), PARAM_UNIDADE_MEDIDA( "Unidade de medida" ), PARAM_FORMA_PAGAMENTO(
			"Forma de pagamento" ), PARAM_DOCUMENTO( "Número do documento" );

	private final String name;

	EParametro( String name ) {

		this.name = name;
	}

	String getName() {

		return this.name;
	}
}
