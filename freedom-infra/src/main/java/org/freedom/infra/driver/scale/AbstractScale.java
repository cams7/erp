package org.freedom.infra.driver.scale;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import java.util.EventListener;
import java.util.HashMap;

import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;

import org.freedom.infra.comm.AbstractPort;
import org.freedom.infra.comm.CtrlPort;
import org.freedom.infra.comm.SerialParams;

public abstract class AbstractScale implements SerialPortEventListener, Runnable {

	protected SerialParams serialParams = new SerialParams();

	protected static byte[] buffer = null;

	public abstract BigDecimal getWeight();

	public abstract Date getDate();

	public abstract Time getTime();

	protected abstract void readReturn();

	public boolean isRead = false;

	private boolean outputWrite;

	protected static int TIMEOUT_READ = 5000;

	public static int TIMEOUT_ACK = 1500;

	public static final Integer LOG_MESSAGE = 0;

	public static final Integer LOG_WARNING = 1;

	public static final Integer LOG_ERROR = 2;

	public static final String MESSAGE_UNSTABLE = "Escale is unstable, try again!";

	public static final String MESSAGE_NEGATIVE_VALUE = "Escale return a negative value, try again!";

	private ScaleResult scaleresult = null;
	
	public static StringBuilder scalebuffer = new StringBuilder();
	
	public volatile boolean readstring = false;

	protected boolean IS_BUFFERIZED = false;

	public Integer com = null;

	public boolean isBufferized() {
		return IS_BUFFERIZED;
	}

	private HashMap<Integer, String> log = new HashMap<Integer, String>();

	public abstract void initialize(Integer com, Integer timeout, Integer baundrate, Integer databits, Integer stopbits, Integer parity);

	public boolean activePort(final EventListener event) {

		boolean result = CtrlPort.getInstance().isActive();

		// if ( !result ) {
		result = CtrlPort.getInstance().activePort(com, serialParams, event == null ? this : event);
		// }

		return result;
	}

	protected void setLog(Integer typelog, String message) {
		log.put(typelog, message);
	}

	public HashMap<Integer, String> getLog() {
		return log;
	}

	public void inactivePort() {
		try {

			boolean result = CtrlPort.getInstance().isActive();

			if (result) {

				CtrlPort.getInstance().disablePort();

			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public abstract String getName();

	public void serialEvent(final SerialPortEvent event) {

		byte[] result = null;
		byte[] bufferTmp = null;
		byte[] tmp = null;

		InputStream input = null;

		input = CtrlPort.getInstance().getInput();

		try {
			if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {

				// result = new byte[ input.available() ];
				result = new byte[50];

				if (result != null) {

					input.read(result);

					if (buffer == null) {
						bufferTmp = result;
					}
					else {
						isRead = true;
						tmp = buffer;
						bufferTmp = new byte[tmp.length + result.length];

						for (int i = 0; i < bufferTmp.length; i++) {
							if (i < tmp.length) {
								bufferTmp[i] = tmp[i];
							}
							else {
								bufferTmp[i] = result[i - tmp.length];
							}
						}
					}
					buffer = bufferTmp;
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeOutput(final byte[] CMD) {

		try {
			final OutputStream output = CtrlPort.getInstance().getOutput();
			try {
				output.flush();
				output.write(CMD);
				closeOutput();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void closeOutput() {

		try {
			final OutputStream output = CtrlPort.getInstance().getOutput();
			output.close();
			outputWrite = true;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] sendCmd(final byte[] CMD, final int tamresult) {

		long tempo = 0;
		long tempoAtual = 0;
		boolean isserial = AbstractPort.isSerial(com);
		isRead = false;
		buffer = null;

		if (activePort(this)) {

			try {

				tempo = System.currentTimeMillis();
				outputWrite = false;

				Thread tee = new Thread(new Runnable() {
					public void run() {
						writeOutput(CMD);
					}
				});

				tee.start();
				tee.join(TIMEOUT_READ);

				if (!outputWrite) {
					tee.interrupt();
					return null;
				}

				if (isserial) {
					do {
						Thread.sleep(TIMEOUT_ACK);
						tempoAtual = System.currentTimeMillis();
					}
					while (( tempoAtual - tempo ) < ( TIMEOUT_READ ) && ( buffer == null || buffer.length < tamresult || !isRead ));
				}

			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return buffer;
	}

	public abstract void run();

	public abstract ScaleResult parseString();

	public void setScaleresult(ScaleResult scaleresult) {
		this.scaleresult = scaleresult;
	}

	public ScaleResult getScaleresult() {
		return scaleresult;
	}

}
