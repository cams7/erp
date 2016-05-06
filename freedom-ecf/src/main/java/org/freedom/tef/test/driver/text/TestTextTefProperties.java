
package org.freedom.tef.test.driver.text;

import java.util.List;

import org.freedom.tef.driver.text.TextTefProperties;

import junit.framework.TestCase;

public class TestTextTefProperties extends TestCase {

	public TestTextTefProperties( String name ) {

		super( name );
	}

	// Teste de instanciação do objeto.
	public void testeTextTefProperties() {

		TextTefProperties textTefProperties = new TextTefProperties();
		assertFalse( textTefProperties == null );
	}

	// Teste de validação de chave, com parâmetro de chave válida.
	public void testValidateTextTefPropertie1() {

		TextTefProperties textTefProperties = new TextTefProperties();
		assertTrue( textTefProperties.validateTextTefPropertie( TextTefProperties.HEADER ) );
	}

	// Teste de validação de chave, com parâmetro de chave inválida.
	public void testValidateTextTefPropertie2() {

		TextTefProperties textTefProperties = new TextTefProperties();
		assertFalse( textTefProperties.validateTextTefPropertie( "CHAVE INVÁLIDA!" ) );
	}

	// Teste de validação de chave, com parâmetro de chave nulo.
	public void testValidateTextTefPropertie3() {

		TextTefProperties textTefProperties = new TextTefProperties();
		assertFalse( textTefProperties.validateTextTefPropertie( null ) );
	}

	// Teste de validação de chave, com parâmetro de chave vazia ou com espaços ( "" || "  " ).
	public void testValidateTextTefPropertie4() {

		TextTefProperties textTefProperties = new TextTefProperties();
		assertFalse( textTefProperties.validateTextTefPropertie( " " ) );
	}

	// Teste de validação de chave, com parâmetro de chave variado de RESPONSE_TO_PRINT, válida.
	public void testValidateTextTefPropertie5() {

		TextTefProperties textTefProperties = new TextTefProperties();
		assertTrue( textTefProperties.validateTextTefPropertie( "029-001" ) );
	}

	// Teste de retorno de propriedade, para chave válida.
	public void testGet1() {

		// TextTefProperties.HEADER é definido com "" em initializeTextTefProperties();
		TextTefProperties textTefProperties = new TextTefProperties();
		assertFalse( textTefProperties.getProperty( TextTefProperties.HEADER ) == null );
	}

	// Teste de retorno de propriedade, para chave inválida.
	public void testGet2() {

		try {
			TextTefProperties textTefProperties = new TextTefProperties();
			textTefProperties.getProperty( "CHAVE INVÁLIDA!" );
		}
		catch ( Exception e ) {
			assertTrue( true );
		}
	}

	// Teste de definição de propriedade, para chave válida e valor válido.
	public void testSet1() {

		// TextTefProperties.HEADER é definido com "" em initializeTextTefProperties();
		TextTefProperties textTefProperties = new TextTefProperties();
		assertTrue( "TESTE".equals( textTefProperties.setProperty( TextTefProperties.HEADER, "TESTE" ) ) );
	}

	// Teste de definição de propriedade, para chave inválida e valor válido.
	public void testSet2() {

		try {
			TextTefProperties textTefProperties = new TextTefProperties();
			textTefProperties.setProperty( "CHAVE INVÁLIDA!", "TESTE" );
		}
		catch ( Exception e ) {
			assertTrue( true );
		}
	}

	// Teste de definição de propriedade, para chave válida e valor inválido.
	public void testSet3() {

		try {
			TextTefProperties textTefProperties = new TextTefProperties();
			textTefProperties.setProperty( TextTefProperties.HEADER, null );
		}
		catch ( Exception e ) {
			assertTrue( true );
		}
	}

	// Teste de cópia da lista de propriedades.
	public void testKeysListCopy() {

		try {
			List<String> keysListCopy = TextTefProperties.getKeyList();
			assertTrue( keysListCopy != null );
		}
		catch ( Exception e ) {
			assertTrue( false );
		}
	}
}
