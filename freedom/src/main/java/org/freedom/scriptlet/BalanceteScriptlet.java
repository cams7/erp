package org.freedom.scriptlet;

import net.sf.jasperreports.engine.JRDefaultScriptlet;


public class BalanceteScriptlet extends JRDefaultScriptlet {
	public String replicate(String charac, int count) {
		StringBuffer result = new StringBuffer();
		if (charac!=null) {
			for (int i=0; i<count; i++) {
				result.append( charac );
			}
		}
		return result.toString();
	}
}
