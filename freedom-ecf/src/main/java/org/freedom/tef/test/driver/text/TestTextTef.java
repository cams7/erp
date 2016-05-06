
package org.freedom.tef.test.driver.text;

import junit.framework.TestCase;

import org.freedom.tef.driver.Flag;
import org.freedom.tef.driver.text.TextTef;
import org.freedom.tef.driver.text.TextTefProperties;

public class TestTextTef extends TestCase {

	public TestTextTef( String name ) {

		super( name );
	}

	// Teste de instanciação, inicialização de propriedades.
	public void testTextTef1() {

		boolean ok = true;
		try {
			Flag.loadParametrosOfInitiation( "C:\\bandeiras.ini" );
			Class<TextTef> classTextTef = Flag.getTextTefFlagsMap().get( "VISA" );
			TextTef textTef = classTextTef.newInstance();
			ok = ( textTef != null );
		}
		catch ( Exception e ) {
			e.printStackTrace();
			ok = false;
		}
		assertTrue( ok );
	}

	// Teste de instanciação, com inicialização de propriedades.
	public void testTextTef2() {

		boolean ok = true;
		try {
			Flag.loadParametrosOfInitiation( "C:\\bandeiras.ini" );
			Class<TextTef> classTextTef = Flag.getTextTefFlagsMap().get( "VISA" );
			TextTef textTef = classTextTef.newInstance();
			TextTefProperties textTefProperties = new TextTefProperties();
			textTefProperties.set( TextTefProperties.PATH_SEND, "C:/Client/Req" );
			textTefProperties.set( TextTefProperties.PATH_RESPONSE, "C:/Client/Resp" );
			if ( textTef != null ) {
				textTef.initializeTextTef( textTefProperties );
			}
		}
		catch ( Exception e ) {
			e.printStackTrace();
			ok = false;
		}
		assertTrue( ok );
	}

	// Teste de instanciação, com inicialização de propriedades inválida.
	public void testTextTef3() {

		boolean ok = false;
		try {
			Flag.loadParametrosOfInitiation( "C:\\bandeiras.ini" );
			Class<TextTef> classTextTef = Flag.getTextTefFlagsMap().get( "VISA" );
			TextTef textTef = classTextTef.newInstance();
			textTef.initializeTextTef( null );
		}
		catch ( Exception e ) {
			ok = true;
		}
		assertTrue( ok );
	}

	// Teste de retorno dos membros da lista textTefProperties, para get(String).
	public void testGetString1() {

		boolean A = false;
		boolean B = false;
		try {
			Flag.loadParametrosOfInitiation( "C:\\bandeiras.ini" );
			Class<TextTef> classTextTef = Flag.getTextTefFlagsMap().get( "VISA" );
			TextTef textTef = classTextTef.newInstance();
			TextTefProperties textTefProperties = new TextTefProperties();
			textTefProperties.set( TextTefProperties.PATH_SEND, "C:/Client/Req" );
			textTefProperties.set( TextTefProperties.PATH_RESPONSE, "C:/Client/Resp" );
			if ( textTef != null ) {
				textTef.initializeTextTef( textTefProperties );
			}
			A = "C:/Client/Req".equals( textTef.get( TextTefProperties.PATH_SEND ) );
			B = "".equals( textTef.get( "TESTE", "" ) );
		}
		catch ( Exception e ) {
			B = true;
		}
		assertTrue( A & B );
	}

	// Teste de retorno dos membros da lista textTefProperties, para get(String,String).
	public void testGetStringString1() {

		boolean A = false;
		boolean B = false;
		boolean C = false;
		try {
			Flag.loadParametrosOfInitiation( "C:\\bandeiras.ini" );
			Class<TextTef> classTextTef = Flag.getTextTefFlagsMap().get( "VISA" );
			TextTef textTef = classTextTef.newInstance();
			TextTefProperties textTefProperties = new TextTefProperties();
			textTefProperties.set( TextTefProperties.PATH_SEND, "C:/Client/Req" );
			textTefProperties.set( TextTefProperties.PATH_RESPONSE, "C:/Client/Resp" );
			if ( textTef != null ) {
				textTef.initializeTextTef( textTefProperties );
			}
			A = "C:/Client/Req".equals( textTef.get( TextTefProperties.PATH_SEND, "" ) );
			B = "".equals( textTef.get( TextTefProperties.MESSAGE_OPERATOR, "" ) );
			C = "".equals( textTef.get( "TESTE", "" ) );
		}
		catch ( Exception e ) {
			C = true;
		}
		assertTrue( A & B & C );
	}

	// Teste de retorno dos membros da lista textTefProperties, para get(String), mas sem textTefProperties definida.
	public void testGetString2() {

		boolean ok = false;
		try {
			Flag.loadParametrosOfInitiation( "C:\\bandeiras.ini" );
			Class<TextTef> classTextTef = Flag.getTextTefFlagsMap().get( "VISA" );
			TextTef textTef = classTextTef.newInstance();
			ok = textTef.get( TextTefProperties.PATH_SEND ) == null;
		}
		catch ( Exception e ) {
			ok = false;
		}
		assertTrue( ok );
	}

	// Teste de retorno dos membros da lista textTefProperties, para get(String,String), mas sem textTefProperties definida.
	public void testGetStringString2() {

		boolean ok = false;
		try {
			Flag.loadParametrosOfInitiation( "C:\\bandeiras.ini" );
			Class<TextTef> classTextTef = Flag.getTextTefFlagsMap().get( "VISA" );
			TextTef textTef = classTextTef.newInstance();
			ok = textTef.get( TextTefProperties.PATH_SEND, "" ) == null;
		}
		catch ( Exception e ) {
			ok = false;
		}
		assertTrue( ok );
	}

	// Teste de definição de membro da lista textTefProperties.
	public void testSetString1() {

		boolean ok = false;
		try {
			Flag.loadParametrosOfInitiation( "C:\\bandeiras.ini" );
			Class<TextTef> classTextTef = Flag.getTextTefFlagsMap().get( "VISA" );
			TextTef textTef = classTextTef.newInstance();
			TextTefProperties textTefProperties = new TextTefProperties();
			textTefProperties.set( TextTefProperties.PATH_SEND, "C:/Client/Req" );
			textTefProperties.set( TextTefProperties.PATH_RESPONSE, "C:/Client/Resp" );
			if ( textTef != null ) {
				textTef.initializeTextTef( textTefProperties );
			}
			ok = textTef.set( TextTefProperties.MESSAGE_OPERATOR, "TESTE" ) != null;
		}
		catch ( Exception e ) {
			ok = false;
		}
		assertTrue( ok );
	}

	// Teste de definição de membro da lista textTefProperties, mas chave inválida.
	public void testSetString2() {

		boolean ok = false;
		try {
			Flag.loadParametrosOfInitiation( "C:\\bandeiras.ini" );
			Class<TextTef> classTextTef = Flag.getTextTefFlagsMap().get( "VISA" );
			TextTef textTef = classTextTef.newInstance();
			TextTefProperties textTefProperties = new TextTefProperties();
			textTefProperties.set( TextTefProperties.PATH_SEND, "C:/Client/Req" );
			textTefProperties.set( TextTefProperties.PATH_RESPONSE, "C:/Client/Resp" );
			if ( textTef != null ) {
				textTef.initializeTextTef( textTefProperties );
			}
			textTef.set( "TESTE", "TESTE" );
		}
		catch ( Exception e ) {
			ok = true;
		}
		assertTrue( ok );
	}

	// Teste de definição de membro da lista textTefProperties, mas com textTefProperties não definida.
	public void testSetString3() {

		boolean ok = false;
		try {
			Flag.loadParametrosOfInitiation( "C:\\bandeiras.ini" );
			Class<TextTef> classTextTef = Flag.getTextTefFlagsMap().get( "VISA" );
			TextTef textTef = classTextTef.newInstance();
			ok = textTef.set( TextTefProperties.MESSAGE_OPERATOR, "TESTE" ) == null;
		}
		catch ( Exception e ) {
			ok = false;
		}
		assertTrue( ok );
	}
}
