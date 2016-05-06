package org.freedom.library.business.componet.integration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.freedom.infra.functions.SystemFunctions;

public abstract class Contabil {

	public static final String FREEDOM_CONTABIL = "01";

	public static final String SAFE_CONTABIL = "02";

	public static final String EBS_CONTABIL = "03";

	protected static final int NUMERIC = 0;

	protected static final int CHAR = 1;

	protected static final int DATE = 2;

	protected static final String RETURN = String.valueOf(( char ) 13) + String.valueOf(( char ) 10);

	protected static final String SEPDIR = ( SystemFunctions.getOS() == SystemFunctions.OS_LINUX || SystemFunctions.getOS() == SystemFunctions.OS_OSX  ? "/" : "\\" );

	public static final String SET_SIZE_ROWS = "SET_SIZE_ROWS";

	public static final String PROGRESS_IN_ROWS = "PROGRESS_IN_ROWS";

	protected final List<String> readrows;

	protected final List<ActionListener> actionListeners = new ArrayList<ActionListener>();

	protected int sizeMax = 0;

	protected int progressInRows = 0;

	public Contabil() {
		this.readrows = new ArrayList<String>();
	}

	public abstract void createFile(File pathFile) throws Exception;

	public int getSizeMax() {
		return sizeMax;
	}

	public int getProgress() {
		return progressInRows;
	}

	public void addActionListener(ActionListener actionListener) {

		actionListeners.add(actionListener);
	}

	public void removeActionListener(ActionListener actionListener) {

		actionListeners.remove(actionListener);
	}

	protected void fireActionListenerForMaxSize() {

		for (ActionListener action : actionListeners) {
			action.actionPerformed(new ActionEvent(this, 0, SET_SIZE_ROWS));
		}
	}

	protected void fireActionListenerProgressInRows() {

		for (ActionListener action : actionListeners) {
			action.actionPerformed(new ActionEvent(this, 0, PROGRESS_IN_ROWS));
		}
	}
}
