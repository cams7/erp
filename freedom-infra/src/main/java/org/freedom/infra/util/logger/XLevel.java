package org.freedom.infra.util.logger;

import org.apache.log4j.Level;

/**
 * Projeto: <a
 * href="http://sourceforge.net/projects/freedom-erp/">Freedom-infra</a> <br>
 * Este programa é licenciado de acordo com a LPG-PC <br>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada
 * pela Fundação do Software Livre (FSF); <BR>
 * <br>
 * 
 * Classe para personalização de níveis de log.
 * 
 * @see FreedomLogger
 * 
 * @author Anderson Sanchez
 * @version 0.0.1 – 30/06/2008
 * 
 * @since 30/06/2008
 */
public class XLevel extends Level {

	private static final long serialVersionUID = 1L;

	private static String TRACE_STR = "TRACE";

	private static String LETHAL_STR = "LETHAL";

	public static final int TRACE_INT = Level.DEBUG_INT - 1;

	public static final int LETHAL_INT = Level.FATAL_INT + 1;

	public static final XLevel TRACE = new XLevel(TRACE_INT, TRACE_STR, 7);

	public static final XLevel LETHAL = new XLevel(LETHAL_INT, LETHAL_STR, 0);

	protected XLevel(int level, String strLevel, int syslogEquiv) {
		super(level, strLevel, syslogEquiv);
	}

	public static Level toLevel(String sArg) {
		return ( Level ) toLevel(sArg, XLevel.TRACE);
	}

	public static Level toLevel(String sArg, Level defaultValue) {

		if (sArg == null) {
			return defaultValue;
		}
		String stringVal = sArg.toUpperCase();
		if (stringVal.equals(TRACE_STR)) {
			return XLevel.TRACE;
		}
		else if (stringVal.equals(LETHAL_STR)) {
			return XLevel.LETHAL;
		}

		return Level.toLevel(sArg, ( Level ) defaultValue);
	}

	public static Level toLevel(int i) throws IllegalArgumentException {

		switch (i) {
		case TRACE_INT:
			return XLevel.TRACE;
		case LETHAL_INT:
			return XLevel.LETHAL;
		}
		return Level.toLevel(i);
	}
}
