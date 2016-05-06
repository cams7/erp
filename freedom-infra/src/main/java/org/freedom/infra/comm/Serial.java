package org.freedom.infra.comm;

import java.io.IOException;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.TooManyListenersException;

import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;

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

public class Serial extends AbstractPort {

	private SerialPort portSerial = null;

	private static Serial instance = null;

	/*
	 * Construtor definido como private seguindo o padrão Singleton.
	 */
	private Serial() {

		super();

	}

	public SerialPort getSerialPort() {
		return portSerial;
	}

	public static Serial getInstance() {

		if (instance == null) {
			instance = new Serial();
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

				portSerial = ( SerialPort ) ips.open("SComm", serialParams.getTimeout());

				if (portSerial != null) {

					setInput(portSerial.getInputStream());
					setOutput(portSerial.getOutputStream());
					try {
						portSerial.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_OUT);
					}
					catch (UnsupportedCommOperationException e) {
						e.printStackTrace();
					}
					try {
						portSerial.setSerialPortParams(serialParams.getBauderate(), serialParams.getDatabits(), serialParams.getStopbits(), serialParams.getParity());
					}
					catch (UnsupportedCommOperationException e) {
						e.printStackTrace();
					}
					portSerial.setDTR(true);
					portSerial.setRTS(true);

					if (portSerial != null)
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

				portSerial.addEventListener(( SerialPortEventListener ) event);
				portSerial.notifyOnDataAvailable(true);

			}
			catch (TooManyListenersException e) {
				e.printStackTrace();
				result = false;
			}
		}

		return result;

	}

	public void disablePort() {

		if (portSerial != null) {
			portSerial.removeEventListener();
			portSerial.notifyOnDataAvailable(false);
			portSerial.close();
		}

		portSerial = null;
		setActived(false);
	}

}
