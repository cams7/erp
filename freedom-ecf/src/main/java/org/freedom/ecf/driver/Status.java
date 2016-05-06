
package org.freedom.ecf.driver;

public interface Status {

	final static int RELEVANC_ERRO = 1;

	final static int RELEVANC_WARNING = 2;

	final static int RELEVANC_MESSAGE = 3;

	String getMessage();

	int getCode();

	int getRelevanc();
}
