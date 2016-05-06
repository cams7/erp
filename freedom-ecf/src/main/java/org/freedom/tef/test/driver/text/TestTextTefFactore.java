
package org.freedom.tef.test.driver.text;

import junit.framework.TestCase;

import org.freedom.tef.driver.Flag;
import org.freedom.tef.driver.text.TextTefFactory;
import org.freedom.tef.driver.text.TextTefProperties;

public class TestTextTefFactore extends TestCase {

	public TestTextTefFactore( String name ) {

		super( name );
	}

	// Teste de requisição de objeto TextTef, com parametros e bandeira válidos, e nome arquivo de iniciação invalido.
	public void testCreateTextTef1() {

		boolean ok = false;
		try {
			TextTefFactory.createTextTef( getTextTefProperties(), "VISA", "ARQUIVO INVALIDO" );
		}
		catch ( Exception e ) {
			System.out.println( "[CreateTextTef 1] " + e.getMessage() );
			ok = true;
		}
		assertTrue( ok );
	}

	// Teste de requisição de objeto TextTef, sem o mapeamento das bandeiras.
	public void testCreateTextTef2() {

		boolean ok = false;
		try {
			TextTefFactory.createTextTef( getTextTefProperties(), "VISA" );
		}
		catch ( Exception e ) {
			System.out.println( "[CreateTextTef 2] " + e.getMessage() );
			ok = true;
		}
		assertTrue( ok );
	}

	// Teste de requisição de objeto TextTef, com propriedades, bandeira e nome arquivo de iniciação validos.
	public void testCreateTextTef3() {

		boolean ok = true;
		try {
			TextTefFactory.createTextTef( getTextTefProperties(), "VISA", "C:\\bandeiras.ini" );
		}
		catch ( Exception e ) {
			System.out.println( "[CreateTextTef 3] " + e.getMessage() );
			ok = false;
		}
		assertTrue( ok );
	}

	// Teste de requisição de objeto TextTef, com parametros válidos.
	public void testCreateTextTef4() {

		boolean ok = true;
		try {
			Flag.loadParametrosOfInitiation( "C:\\bandeiras.ini" );
			TextTefFactory.createTextTef( getTextTefProperties(), "VISA" );
		}
		catch ( Exception e ) {
			System.out.println( "[CreateTextTef 4] " + e.getMessage() );
			ok = false;
		}
		assertTrue( ok );
	}

	// Teste de requisição de objeto TextTef, com propriedades validas e bandeira nula.
	public void testCreateTextTef5() {

		boolean ok = false;
		try {
			Flag.loadParametrosOfInitiation( "C:\\bandeiras.ini" );
			TextTefFactory.createTextTef( getTextTefProperties(), null );
		}
		catch ( Exception e ) {
			System.out.println( "[CreateTextTef 5] " + e.getMessage() );
			ok = true;
		}
		assertTrue( ok );
	}

	// Teste de requisição de objeto TextTef, com propriedades validas e bandeira inexistente.
	public void testCreateTextTef6() {

		boolean ok = false;
		try {
			Flag.loadParametrosOfInitiation( "C:\\bandeiras.ini" );
			TextTefFactory.createTextTef( getTextTefProperties(), "INEXISTENTE" );
		}
		catch ( Exception e ) {
			System.out.println( "[CreateTextTef 6] " + e.getMessage() );
			ok = true;
		}
		assertTrue( ok );
	}

	// Teste de requisição de objeto TextTef, com propriedades nula e bandeira válida.
	public void testCreateTextTef7() {

		boolean ok = false;
		try {
			Flag.loadParametrosOfInitiation( "C:\\bandeiras.ini" );
			TextTefFactory.createTextTef( null, "VISA" );
		}
		catch ( Exception e ) {
			System.out.println( "[CreateTextTef 7] " + e.getMessage() );
			ok = true;
		}
		assertTrue( ok );
	}

	private TextTefProperties getTextTefProperties() {

		final TextTefProperties textTefProperties = new TextTefProperties();
		textTefProperties.set( TextTefProperties.PATH_SEND, "C:/Client/Req" );
		textTefProperties.set( TextTefProperties.PATH_RESPONSE, "C:/Client/Resp" );
		return textTefProperties;
	}
}
