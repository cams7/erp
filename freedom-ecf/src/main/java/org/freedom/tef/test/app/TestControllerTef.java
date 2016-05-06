
package org.freedom.tef.test.app;

import java.io.File;
import java.math.BigDecimal;

import javax.swing.JOptionPane;

import junit.framework.TestCase;

import org.freedom.tef.app.ControllerTef;
import org.freedom.tef.app.ControllerTefEvent;
import org.freedom.tef.app.ControllerTefListener;
import org.freedom.tef.driver.dedicate.DedicatedAction;
import org.freedom.tef.driver.text.TextTefAction;
import org.freedom.tef.driver.text.TextTefProperties;

public class TestControllerTef extends TestCase implements ControllerTefListener {

	// private int countRowPrint = 0;
	public TestControllerTef( String name ) {

		super( name );
	}

	public void testActive() {

		try {
			ControllerTef controllerTef = ControllerTef.getControllerTextTef( getTextTefProperties(), new File( "C:\\bandeiras.ini" ), ControllerTef.TEF_TEXT );
			assertTrue( controllerTef.standardManagerActive() );
		}
		catch ( Exception e ) {
		}
	}

	public void testSale() {

		boolean ok = true;
		try {
			ControllerTef controllerTef = ControllerTef.getControllerTextTef( getTextTefProperties(), new File( "C:\\bandeiras.ini" ), ControllerTef.TEF_TEXT );
			controllerTef.setControllerMessageListener( this );
			ok = controllerTef.requestSale( 2, new BigDecimal( "9.99" ), "VISA" );
		}
		catch ( Exception e ) {
			e.printStackTrace();
			ok = false;
		}
		assertTrue( ok );
	}

	public void testAdministrator() {

		boolean ok = true;
		try {
			ControllerTef controllerTef = ControllerTef.getControllerTextTef( getTextTefProperties(), new File( "C:\\bandeiras.ini" ), ControllerTef.TEF_TEXT );
			controllerTef.setControllerMessageListener( this );
			ok = controllerTef.requestAdministrator( "VISA" );
		}
		catch ( Exception e ) {
			e.printStackTrace();
			ok = false;
		}
		assertTrue( ok );
	}

	private TextTefProperties getTextTefProperties() {

		final TextTefProperties textTefProperties = new TextTefProperties();
		textTefProperties.set( TextTefProperties.PATH_SEND, "C:/Client/Req" );
		textTefProperties.set( TextTefProperties.PATH_RESPONSE, "C:/Client/Resp" );
		return textTefProperties;
	}

	public boolean actionTef( ControllerTefEvent event ) {

		boolean actionTef = false;
		if ( event.getAction() == TextTefAction.WARNING ) {
			System.out.println( "\n[ WARNING ] " + event.getMessage() + "\n" );
			actionTef = true;
		}
		else if ( event.getAction() == TextTefAction.ERROR ) {
			System.out.println( "\n[  ERROR  ] " + event.getMessage() + "\n" );
			actionTef = true;
		}
		else if ( event.getAction() == TextTefAction.CONFIRM ) {
			int option = JOptionPane.showConfirmDialog( null, event.getMessage(), "CONFIM", JOptionPane.YES_NO_OPTION, JOptionPane.CANCEL_OPTION );
			System.out.println( "[ option ] " + ( option == JOptionPane.YES_OPTION ) );
			actionTef = option == JOptionPane.YES_OPTION;
		}
		else if ( event.getAction() == TextTefAction.BEGIN_PRINT ) {
			System.out.println( "[ Início da impressão do comprovante ] ...\nAbrir Comprovante não Fiscal Vinculado." );
			actionTef = true;
		}
		else if ( event.getAction() == TextTefAction.PRINT ) {
			System.out.println( event.getMessage() );
			actionTef = true;
			/*
			 * actionTef = countRowPrint++ < 9 ; if ( ! actionTef ) { countRowPrint = 0; }
			 */
		}
		else if ( event.getAction().equals( TextTefAction.END_PRINT ) ) {
			System.out.println( "[ Término da impressão do comprovante ] ...\nFechar Comprovante não Fiscal Vinculado." );
			actionTef = true;
		}
		else if ( event.getAction().equals( DedicatedAction.ERRO ) ) {
			JOptionPane.showMessageDialog( null, event.getMessage() );
			actionTef = false;
		}
		return actionTef;
	}
}
