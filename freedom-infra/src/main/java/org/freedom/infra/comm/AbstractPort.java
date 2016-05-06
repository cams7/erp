package org.freedom.infra.comm;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.EventListener;

import org.freedom.infra.functions.SystemFunctions;

public abstract class AbstractPort {

	protected static int sistema = -1;

	public static final int OS_NONE = -1;

	public static final int OS_LINUX = 0;

	public static final int OS_WINDOWS = 1;

	public static final int COM1 = 0;
	public static final int COM2 = 1;
	public static final int COM3 = 2;
	public static final int COM4 = 3;
	public static final int COM5 = 4;
	public static final int COM6 = 5;
	public static final int COM7 = 6;
	public static final int COM8 = 7;
	public static final int COM9 = 8;
	public static final int COM10 = 9;
	public static final int COM11 = 10;
	public static final int COM12 = 11;
	public static final int COM13 = 12;
	public static final int COM14 = 13;
	public static final int COM15 = 14;
	public static final int COM16 = 15;
	public static final int COM17 = 16;
	public static final int COM18 = 17;
	public static final int COM19 = 18;
	public static final int COM20 = 19;

	public static final int COM_USB1 = 20;
	public static final int COM_USB2 = 21;
	public static final int COM_USB3 = 22;
	public static final int COM_USB4 = 23;
	public static final int COM_USB5 = 24;
	public static final int COM_USB6 = 25;
	public static final int COM_USB7 = 26;
	public static final int COM_USB8 = 27;
	public static final int COM_USB9 = 28;
	public static final int COM_USB10 = 29;
	public static final int COM_USB11 = 30;
	public static final int COM_USB12 = 31;
	public static final int COM_USB13 = 32;
	public static final int COM_USB14 = 33;
	public static final int COM_USB15 = 34;
	public static final int COM_USB16 = 35;
	public static final int COM_USB17 = 36;
	public static final int COM_USB18 = 37;
	public static final int COM_USB19 = 38;
	public static final int COM_USB20 = 39;

	public static final int LPT1 = 40;
	public static final int LPT2 = 41;
	public static final int LPT3 = 42;
	public static final int LPT4 = 43;
	public static final int LPT5 = 44;
	public static final int LPT6 = 45;
	public static final int LPT7 = 46;
	public static final int LPT8 = 47;
	public static final int LPT9 = 48;
	public static final int LPT10 = 49;
	public static final int LPT11 = 50;
	public static final int LPT12 = 51;
	public static final int LPT13 = 52;
	public static final int LPT14 = 53;
	public static final int LPT15 = 54;
	public static final int LPT16 = 55;
	public static final int LPT17 = 56;
	public static final int LPT18 = 57;
	public static final int LPT19 = 58;
	public static final int LPT20 = 59;

	public static final int LPT_USB1 = 60;
	public static final int LPT_USB2 = 61;
	public static final int LPT_USB3 = 62;
	public static final int LPT_USB4 = 63;
	public static final int LPT_USB5 = 64;
	public static final int LPT_USB6 = 65;
	public static final int LPT_USB7 = 66;
	public static final int LPT_USB8 = 67;
	public static final int LPT_USB9 = 68;
	public static final int LPT_USB10 = 69;
	public static final int LPT_USB11 = 70;
	public static final int LPT_USB12 = 71;
	public static final int LPT_USB13 = 72;
	public static final int LPT_USB14 = 73;
	public static final int LPT_USB15 = 74;
	public static final int LPT_USB16 = 75;
	public static final int LPT_USB17 = 76;
	public static final int LPT_USB18 = 77;
	public static final int LPT_USB19 = 78;
	public static final int LPT_USB20 = 79;

	public static final int MIN_COM = COM1;
	public static final int MAX_COM = COM20;
	public static final int MIN_COM_USB = COM_USB1;
	public static final int MAX_COM_USB = COM_USB20;
	public static final int MIN_LPT = LPT1;
	public static final int MAX_LPT = LPT20;
	public static final int MIN_LPT_USB = LPT_USB1;
	public static final int MAX_LPT_USB = LPT_USB20;

	private boolean actived = false;

	private InputStream input = null;

	private OutputStream output = null;

	public void setActived(boolean actived) {

		this.actived = actived;
	}

	public boolean isActived() {

		return actived;
	}

	public InputStream getInput() {

		return input;
	}

	public void setInput(InputStream inp) {

		this.input = inp;
	}

	public OutputStream getOutput() {

		return output;
	}

	public void setOutput(OutputStream out) {

		this.output = out;
	}

	public static int convPorta(String com) {

		int porta = COM1;
		int portaParcial = 0;
		final String[] portas = { "COM_USB", "LPT_USB", "COM", "LPT" };

		if (com != null) {
			for (int i = 0; i < portas.length; i++) {
				if (com.length() > portas[i].length()) {
					if (com.substring(0, portas[i].length()).equalsIgnoreCase(portas[i])) {
						portaParcial = Integer.parseInt(com.substring(portas[i].length()));
						if (i == 0)
							porta = MIN_COM_USB;
						else if (i == 1)
							porta = MIN_LPT_USB;
						else if (i == 2)
							porta = MIN_COM;
						else
							porta = MIN_LPT;
						porta = porta + portaParcial - 1;
						break;
					}
				}
			}
		}
		return porta;
	}

	abstract boolean activePort(final int portn, final SerialParams serialParams, final EventListener event);

	abstract void disablePort();

	public static boolean isSerial(final int portn) {
		boolean result = false;
		if (( portn >= MIN_COM && portn <= MAX_COM ) || ( portn >= MIN_COM_USB && portn <= MAX_COM_USB ))
			result = true;
		return result;
	}

	public static String convPort(final int portn) {

		final StringBuffer porta = new StringBuffer();

		if (SystemFunctions.getOS() == OS_WINDOWS) {
			if (portn <= MAX_COM) {
				porta.append("COM");
				porta.append(portn + 1);
			}
			else if (( portn >= MIN_LPT ) && ( portn <= MAX_LPT )) {
				porta.append("LPT");
				porta.append(portn - MIN_LPT + 1);
			}
		}
		else {
			if (portn <= MAX_COM) {
				porta.append("/dev/ttyS");
				porta.append(portn);
			}
			else if (( portn >= MIN_COM_USB ) && ( portn <= MAX_COM_USB )) {
				porta.append("/dev/ttyUSB");
				porta.append(portn - MIN_COM_USB);
			}
			else if (( portn >= MIN_LPT ) && ( portn <= MAX_LPT )) {
				porta.append("/dev/lp");
				porta.append(portn - MIN_LPT);
			}
			else if (( portn >= MIN_LPT_USB ) && ( portn <= MAX_LPT_USB )) {
				porta.append("/dev/usb/lp");
				porta.append(portn - MIN_LPT_USB);
			}
		}
		return porta.toString();
	}

}
