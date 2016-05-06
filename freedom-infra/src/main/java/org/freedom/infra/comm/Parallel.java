package org.freedom.infra.comm;

import java.io.IOException;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.TooManyListenersException;

import javax.comm.CommPortIdentifier;
import javax.comm.ParallelPort;
import javax.comm.ParallelPortEventListener;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;

/**
 * Classe para acesso e controle a porta serial <BR>
 * Projeto: freedom-ecf <BR>
 * Pacote: org.freedom.ecf.com <BR>
 * Classe: @(#)Serial.java <BR>
 * <BR>
 * Este programa é licenciado de acordo com a LGPL (Lesser General Public
 * License), <BR>
 * versão 2.1, Fevereiro de 1999 <BR>
 * A LGPL deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste
 * Programa. <BR>
 * Caso uma cópia da LGPL não esteja disponível junto com este Programa, você
 * pode contatar <BR>
 * o LICENCIADOR ou então pegar uma cópia em: <a
 * href=http://creativecommons.org/licenses/LGPL/2.1/legalcode.pt> Creative
 * Commons</a> <BR>
 * Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa é
 * preciso estar de acordo com os termos da LGPL. <BR>
 * <BR>
 * 
 * @author Robson Sanchez/Setpoint Informática Ltda. <BR>
 *         criada: 19/08/2006. <BR>
 * @version 1.0.0 - 05/04/2006 <BR>
 */

public class Parallel extends AbstractPort {

	private ParallelPort portParallel = null;

	public static final int TIMEOUT = 150;

	public static final int BAUDRATE = 9600;

	public static final int DATABITS = SerialPort.DATABITS_8;

	public static final int STOPBITS = SerialPort.STOPBITS_1;

	public static final int PARITY = SerialPort.PARITY_NONE;

	private static Parallel instance = null;

	/*
	 * Construtor definido como private seguindo o padrão Singleton.
	 */
	private Parallel() {

		super();

	}

	public static Parallel getInstance() {

		if (instance == null) {
			instance = new Parallel();
		}

		return instance;
	}

	/**
	 * Captura a porta aberta de define os objetos de entrada e saida.<BR>
	 * 
	 * @param portn
	 * @param serialParams
	 * @param event
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean activePort(final int portn, final SerialParams serialParams, final EventListener event) {

		boolean result = false;
		String portstr = convPort(portn);
		Enumeration<CommPortIdentifier> listaPortas = null;
		CommPortIdentifier ips = null;
		listaPortas = ( Enumeration<CommPortIdentifier> ) CommPortIdentifier.getPortIdentifiers();

		while (listaPortas.hasMoreElements()) {

			ips = listaPortas.nextElement();

			if (ips.getName().equalsIgnoreCase(portstr)) {
				break;
			}
			else {
				ips = null;
			}

		}

		if (ips != null) {

			try {

				portParallel = ( ParallelPort ) ips.open("SComm", TIMEOUT);

				if (portParallel != null) {

					// setInput( portParallel.getInputStream() );
					setOutput(portParallel.getOutputStream());
					/*
					 * try { portParallel.setFlowControlMode(
					 * SerialPort.FLOWCONTROL_RTSCTS_OUT ); } catch (
					 * UnsupportedCommOperationException e ) {
					 * e.printStackTrace(); } try {
					 * portParallel.setSerialPortParams( Serial.BAUDRATE,
					 * Serial.DATABITS, Serial.STOPBITS, Serial.PARITY ); }
					 * catch ( UnsupportedCommOperationException e ) {
					 * e.printStackTrace(); } portParallel.setDTR( true );
					 * portParallel.setRTS( true );
					 */

					if (portParallel != null)
						result = true;
				}

			}
			catch (PortInUseException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
				// portaSerial = null;
			}

			try {
				portParallel.addEventListener(( ParallelPortEventListener ) event);
				portParallel.notifyOnBuffer(true);
			}
			catch (TooManyListenersException e) {
				e.printStackTrace();
				result = false;
			}
		}

		return result;

	}

	public void disablePort() {

		if (portParallel != null) {
			portParallel.close();
		}

		portParallel = null;
		setActived(false);
	}

}
