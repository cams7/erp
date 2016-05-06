package org.freedom.infra.comm;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.EventListener;

public class CtrlPort {

	private static CtrlPort instance = null;

	private static boolean active = false;

	private int portnSel = -1;

	private static AbstractPort port = null;

	public static CtrlPort getInstance() {

		if (instance == null) {
			instance = new CtrlPort();
		}

		return instance;
	}

	/**
	 * 
	 * @return Verdadeiro caso a porta esteja ativada. <BR>
	 */
	public boolean isActive() {

		return active;
	}

	public InputStream getInput() {
		InputStream result = null;
		if (port != null)
			result = port.getInput();
		return result;
	}

	public OutputStream getOutput() {
		OutputStream result = null;
		if (port != null)
			result = port.getOutput();
		return result;
	}

	public static AbstractPort getPort() {
		return port;
	}

	public int getPortnsel() {
		return this.portnSel;
	}

	/**
	 * Ativa a porta serial.<BR>
	 * 
	 * @param portn
	 * @param serialParams
	 * @param event
	 *            Objeto ouvinte do evento de leitura dos bytes recebidos.<BR>
	 * @return Verdadeiro caso a porta esteja ativada. <BR>
	 */
	public boolean activePort(final int portn, final SerialParams serialParams, final EventListener event) {

		boolean result = true;

		if (portn != portnSel || port == null) {

			portnSel = portn;
			if (AbstractPort.isSerial(portnSel)) {
				port = Serial.getInstance();
			}
			else {
				port = Parallel.getInstance();
			}
			if (port == null) {
				result = false;
			}
			else {
				result = port.activePort(portn, serialParams, event);
			}

			active = result;

		}
		// result = port.activePort( portn, serialParams, event );
		return result;
	}

	public void setActive(boolean status) {
		active = status;
	}

	/**
	 * Desativa a porta serial.<BR>
	 * 
	 */
	public void disablePort() {

		if (port != null) {
			port.disablePort();
			active = false;
			port = null;
		}

	}

	public AbstractPort activePort(final String porta) {
		AbstractPort prt = null;

		return prt;
	}

}
