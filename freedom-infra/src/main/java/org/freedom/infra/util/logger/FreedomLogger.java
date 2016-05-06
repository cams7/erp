package org.freedom.infra.util.logger;

import java.util.Properties;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.freedom.infra.util.ini.ManagerIni;

/**
 * Projeto: <a
 * href="http://sourceforge.net/projects/freedom-erp/">Freedom-infra</a> <br>
 * Este programa é licenciado de acordo com a LPG-PC <br>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada
 * pela Fundação do Software Livre (FSF); <BR>
 * <br>
 * 
 * Sub-classe para personalização de logs utilizando a API Log4J.
 * 
 * @see FreedomLogger
 * 
 * @author Anderson Sanchez
 * @version 0.0.1 – 30/06/2008
 * 
 * @since 30/06/2008
 */
public class FreedomLogger extends Logger {

	private static String FQCN = FreedomLogger.class.getName() + ".";

	private static FreedomLoggerFactory myFactory = new FreedomLoggerFactory();

	private static String LAYOUT_DEFAULT = "%p >> %d{dd MMM yyyy HH:mm:ss:ms} %c - %m%n";

	public final static String LOGGER_JPA = "arqlog_jpa";

	public final static String LOGGER_SYS = "arqlog_sys";

	public final static String LOGGER_ECF = "arqlog_ecf";

	public FreedomLogger(String name) {

		super(name);
	}

	public static Logger getLogger(String name) {

		Logger c = Logger.getLogger(name, myFactory);
		Layout layout = new PatternLayout(LAYOUT_DEFAULT);
		c.addAppender(new ConsoleAppender(layout, ConsoleAppender.SYSTEM_OUT));
		return c;
	}

	public static Logger getLogger(final Class<?> cl, final String initFile, final String property) {

		Properties properties;
		Logger c = null;
		try {
			ManagerIni mi = initFile != null ? ManagerIni.createManagerIniFile(initFile) : ManagerIni.createManagerIniParameter();
			properties = mi.getSession("FreedomLogger");
			c = Logger.getLogger(cl.getCanonicalName(), myFactory);
			Layout layout = new PatternLayout(LAYOUT_DEFAULT);
			c.addAppender(new FileAppender(layout, properties.get(property).toString(), true));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}

	public static Logger getLogger(String name, String file) {

		Logger c = Logger.getLogger(name, myFactory);
		try {
			Layout layout = new PatternLayout(LAYOUT_DEFAULT);
			c.addAppender(new FileAppender(layout, file, true));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}

	public void log(int lv, String sMsg) {

		if (lv == Level.INFO_INT) {
			setLevel(Level.toLevel(lv));
			info(sMsg);
		}
		else if (lv == Level.WARN_INT) {
			setLevel(Level.toLevel(lv));
			warn(sMsg);
		}
		else if (lv == Level.ERROR_INT) {
			setLevel(Level.toLevel(lv));
			error(sMsg);
		}
		else if (lv == Level.TRACE_INT) {
			setLevel(Level.toLevel(lv));
			trace(sMsg);
		}
	}

	public void trace(Object message) {
		super.log(FQCN, XLevel.TRACE, message, null);
	}

	public static void setLayoutDefault(final String ld) {
		LAYOUT_DEFAULT = ld;
	}
}