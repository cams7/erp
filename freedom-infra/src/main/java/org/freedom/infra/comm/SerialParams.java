package org.freedom.infra.comm;

import javax.comm.SerialPort;

public class SerialParams {

	private int timeout = 150;
	private int bauderate = 9600;
	private int databits = SerialPort.DATABITS_8;
	private int stopbits = SerialPort.STOPBITS_1;
	private int parity = SerialPort.PARITY_NONE;

	public SerialParams() {
		super();
	}

	public SerialParams(final int tout, final int brate, final int dbits, final int sbits, final int par) {
		super();
		this.timeout = tout;
		this.bauderate = brate;
		this.databits = dbits;
		this.stopbits = sbits;
		this.parity = par;

	}

	public int getTimeout() {

		return timeout;
	}

	public void setTimeout(int timeout) {

		this.timeout = timeout;
	}

	public int getBauderate() {

		return bauderate;
	}

	public void setBauderate(int bauderate) {

		this.bauderate = bauderate;
	}

	public int getDatabits() {

		return databits;
	}

	public void setDatabits(int databits) {

		this.databits = databits;
	}

	public int getStopbits() {

		return stopbits;
	}

	public void setStopbits(int stopbits) {

		this.stopbits = stopbits;
	}

	public int getParity() {

		return parity;
	}

	public void setParity(int parity) {

		this.parity = parity;
	}

}
