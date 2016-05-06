package org.freedom.scriptlet;

import java.awt.Color;
import java.util.Vector;

import org.freedom.modulos.crm.business.component.Constant;
import org.freedom.modulos.crm.business.object.Contrato;

import net.sf.jasperreports.engine.JRDefaultScriptlet;


public class SitContrScriptlet extends JRDefaultScriptlet {
	
	private Vector<Constant> listSitProj = Contrato.getListSitproj();
	
	public Constant getSitcontr(String sitcontr) {
		Constant result = null;

	    for (int i=0; i<listSitProj.size(); i++) {
	    	if (sitcontr.equals( listSitProj.elementAt( i ).getValue() )) {
	    		result = listSitProj.elementAt( i );
	    		break;
	    	}
	    }
		
		if (result==null) {
			result = new Constant( "Pendente", "PE", Color.YELLOW, Color.BLACK );
		}
		return result;
	}
}
