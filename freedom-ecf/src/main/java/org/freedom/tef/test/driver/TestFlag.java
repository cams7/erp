
package org.freedom.tef.test.driver;

import java.io.File;

import org.freedom.tef.driver.Flag;

import junit.framework.TestCase;

public class TestFlag extends TestCase {

	public TestFlag( String name ) {

		super( name );
	}

	// Teste de carregamento OK passando o objeto File
	public void testLoadTextTefFlagsMap1() {

		boolean ok = true;
		try {
			Flag.loadParametrosOfInitiation( new File( "C:\\bandeiras.ini" ) );
		}
		catch ( Exception e ) {
			e.printStackTrace();
			ok = false;
		}
		assertTrue( ok );
	}

	// Teste de carregamento OK passando o caminho do arquivo.
	public void testLoadTextTefFlagsMap2() {

		boolean ok = true;
		try {
			Flag.loadParametrosOfInitiation( "C:\\bandeiras.ini" );
		}
		catch ( Exception e ) {
			e.printStackTrace();
			ok = false;
		}
		assertTrue( ok );
	}

	// Teste de não carregamento passando o objeto file, pois o arquivo não existe.
	public void testLoadTextTefFlagsMap3() {

		boolean ok = false;
		try {
			Flag.loadParametrosOfInitiation( new File( "C:\\noarquivo.ini" ) );
		}
		catch ( Exception e ) {
			ok = true;
		}
		assertTrue( ok );
	}

	// Teste de não carregamento passando o caminho do arquivo, pois o arquivo não existe.
	public void testLoadTextTefFlagsMap4() {

		boolean ok = false;
		try {
			Flag.loadParametrosOfInitiation( "C:\\noarquivo.ini" );
		}
		catch ( Exception e ) {
			ok = true;
		}
		assertTrue( ok );
	}

	// Teste de verificação do estado do atributo de sinalização para carregamento correto ou não.
	public void testIsLoadTextTefFlagsMaps() {

		boolean A = false;
		boolean B = false;
		try {
			Flag.loadParametrosOfInitiation( "C:\\bandeiras.ini" );
			A = Flag.isLoadTextTefFlagsMaps();
			Flag.loadParametrosOfInitiation( "C:\\noarquivo.ini" );
		}
		catch ( Exception e ) {
			B = false;
		}
		assertTrue( A & !B );
	}
}
