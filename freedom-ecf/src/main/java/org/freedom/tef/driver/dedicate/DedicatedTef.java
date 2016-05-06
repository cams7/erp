
package org.freedom.tef.driver.dedicate;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import org.freedom.infra.util.ini.ManagerIni;

import softwareexpress.sitef.jCliSiTefI;

public class DedicatedTef {

	public static final String ENDERECO_TCP = "ENDERECO_TCP";

	public static final String EMPRESA = "EMPRESA";

	public static final String TERMINAL = "TERMINAL";

	public static final int VOUCHER_TEF = 121;

	private Properties properties;

	private jCliSiTefI clientesitef;

	private DedicatedTefListener dedicateTefListener;

	private static DedicatedTef instance;

	public static DedicatedTef getInstance( DedicatedTefListener dedicateTefListener ) throws Exception {

		if ( instance == null || instance.getDedicateTefListener() != dedicateTefListener ) {
			instance = new DedicatedTef( dedicateTefListener );
		}
		return instance;
	}

	public static DedicatedTef getInstance( String file, DedicatedTefListener dedicateTefListener ) throws Exception {

		return getInstance( new File( file ), dedicateTefListener );
	}

	public static DedicatedTef getInstance( File file, DedicatedTefListener dedicateTefListener ) throws Exception {

		if ( instance == null ) {
			instance = new DedicatedTef( file, dedicateTefListener );
		}
		else {
			instance = null;
		}
		return instance;
	}

	public DedicatedTefListener getDedicateTefListener() {

		return dedicateTefListener;
	}

	private DedicatedTef( DedicatedTefListener dedicateTefListener ) throws Exception {

		this( new File( System.getProperty( ManagerIni.FILE_INIT_DEFAULT ) ), dedicateTefListener );
	}

	private DedicatedTef( File file, DedicatedTefListener dedicateTefListener ) throws Exception {

		if ( file == null || !file.exists() ) {
			throw new IllegalArgumentException( "Arquivo de parametros inexistente." );
		}
		if ( dedicateTefListener == null ) {
			throw new IllegalArgumentException( "Ouvinte de eventos nulo." );
		}
		ManagerIni mi = ManagerIni.createManagerIniFile( file );
		properties = mi.getSession( "DedicateTef" );
		this.dedicateTefListener = dedicateTefListener;
		clientesitef = new jCliSiTefI();
		int r = clientesitef.ConfiguraIntSiTefInterativo( properties.getProperty( ENDERECO_TCP ), properties.getProperty( EMPRESA ), properties.getProperty( TERMINAL ) );
		checkConfiguraIntSiTefInterativo( r );
	}

	private synchronized boolean checkConfiguraIntSiTefInterativo( int result ) {

		String message = null;
		if ( result == 1 ) {
			message = "Endereço IP inválido ou não resolvido.";
		}
		else if ( result == 2 ) {
			message = "Código da loja inválido.";
		}
		else if ( result == 3 ) {
			message = "Código do terminal inválido.";
		}
		else if ( result == 6 ) {
			message = "Erro na inicialização TCP/IP.";
		}
		else if ( result == 7 ) {
			message = "Falta de memória.";
		}
		else if ( result == 8 ) {
			message = "Não encontrou a dll CliSiTef ou ela está com problemas.";
		}
		else if ( result == 10 ) {
			message = "O PinPad não está defidamente configurado no arquivo CliSiTef.ini.";
		}
		if ( message != null ) {
			dedicateTefListener.actionCommand( new DedicatedTefEvent( dedicateTefListener, DedicatedAction.ERRO, message ) );
			return false;
		}
		return true;
	}

	private synchronized boolean checkStandard( int result ) {

		String message = null;
		if ( result > 0 && result < 10000 ) {
			message = "Negada pelo autorizador.";
		}
		else if ( result == -1 ) {
			message = "Modulo não inicializado.";
		}
		else if ( result == -2 ) {
			message = "Operação cancelada pelo operador.";
		}
		else if ( result == -3 ) {
			message = "Fornecida uma modalidade inválida.";
		}
		else if ( result == -4 ) {
			message = "Falta de memória para rodar a função.";
		}
		else if ( result == -5 ) {
			message = "Sem comunicação com o SiTef.";
		}
		else if ( result < 0 ) {
			message = "Erro interno não mapeado.";
		}
		if ( message != null ) {
			dedicateTefListener.actionCommand( new DedicatedTefEvent( dedicateTefListener, DedicatedAction.ERRO, message ) );
			return false;
		}
		return true;
	}

	public synchronized boolean checkPinPad() {

		int result = clientesitef.VerificaPresencaPinPad();
		String message = null;
		if ( result == 0 ) {
			message = "Não existe um PinPad conectado ao micro.";
		}
		else if ( result == -1 ) {
			message = "Biblioteca de acesso ao PinPad não encontrada.";
		}
		if ( message != null ) {
			dedicateTefListener.actionCommand( new DedicatedTefEvent( dedicateTefListener, DedicatedAction.ERRO, message ) );
			return false;
		}
		return result == 1;
	}

	public synchronized boolean readYesNoCard( String message ) {

		message = message.replace( '\n', '|' );
		return 1 == clientesitef.LeSimNaoPinPad( message );
	}

	public synchronized boolean readCard( String message ) {

		message = message.replace( '\n', '|' );
		int result = clientesitef.LeCartaoDireto( message );
		System.out.println( message );
		System.out.println( result );
		System.out.println( clientesitef.GetBuffer() );
		return checkStandard( result );
	}

	public synchronized boolean requestSale( BigDecimal value, Integer docNumber, Date dateHour, String operator ) {

		boolean requestsale = false;
		DecimalFormat df = new DecimalFormat( "0.00" );
		SimpleDateFormat sdf1 = new SimpleDateFormat( "yyyyMMdd", Locale.getDefault() );
		SimpleDateFormat sdf2 = new SimpleDateFormat( "HHmmss", Locale.getDefault() );
		String date = sdf1.format( dateHour );
		String hour = sdf2.format( dateHour );
		int result = clientesitef.IniciaFuncaoSiTefInterativo( Modality.DEBITO.code(), df.format( value.doubleValue() ), String.valueOf( docNumber ), date, hour, operator, "" );
		if ( checkStandard( result ) ) {
			actionNextCommand();
		}
		return requestsale;
	}

	public synchronized void actionNextCommand() {

		clientesitef.SetBuffer( "" );
		clientesitef.SetContinuaNavegacao( 0 );
		boolean action = true;
		int result = 0;
		while ( action ) {
			result = clientesitef.ContinuaFuncaoSiTefInterativo();
			if ( result == 0 ) {
				finallySale();
				break;
			}
			else if ( checkStandard( result ) ) {
				action = actionCommand();
			}
			else {
				break;
			}
		}
	}

	private synchronized void finallySale() {

		boolean checkTicket = dedicateTefListener.actionCommand( new DedicatedTefEvent( dedicateTefListener, DedicatedAction.CHECK_TICKET ) );
		if ( checkTicket ) {
			boolean printTicket = dedicateTefListener.actionCommand( new DedicatedTefEvent( dedicateTefListener, DedicatedAction.PRINT_TICKET ) );
			clientesitef.SetConfirma( printTicket ? 1 : 0 );
			clientesitef.FinalizaTransacaoSiTefInterativo();
		}
	}

	private synchronized boolean actionCommand() {

		if ( dedicateTefListener != null ) {
			return dedicateTefListener.actionCommand( new DedicatedTefEvent( dedicateTefListener, DedicatedAction.getDedicatedAction( clientesitef.GetProximoComando() ), clientesitef.GetBuffer().trim() ) );
		}
		return false;
	}

	public synchronized String getBuffer() {

		return clientesitef.GetBuffer();
	}

	public synchronized int getTypeField() {

		return clientesitef.GetTipoCampo();
	}
}
