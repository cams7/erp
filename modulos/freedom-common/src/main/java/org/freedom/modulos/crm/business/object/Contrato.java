package org.freedom.modulos.crm.business.object;

import java.awt.Color;
import java.util.Vector;

import org.freedom.modulos.crm.business.component.Constant;

public class Contrato {
	public static final Constant SIT_PROJ_NAO_SALVO = new Constant("Não salvo", "",  Color.WHITE, Color.BLACK);	
	public static final Constant SIT_PROJ_PENDENTE = new Constant("Pendente", "PE",  Color.YELLOW, Color.BLACK);
	public static final Constant SIT_PROJ_EM_PLANEJAMENTO = new Constant("Em planejamento", "PA", Color.GRAY, Color.WHITE);
	public static final Constant SIT_PROJ_PLANEJADO = new Constant("Planejado", "PF", Color.DARK_GRAY, Color.WHITE);
	public static final Constant SIT_PROJ_EM_EXECUCAO = new Constant("Em execução", "EE", Color.CYAN, Color.BLACK);
	public static final Constant SIT_PROJ_EXECUTADO = new Constant("Executado", "EX", Color.GREEN, Color.WHITE);
	public static final Constant SIT_PROJ_PARALIZADO = new Constant("Paralizado", "PO", Color.WHITE, Color.BLACK);
	public static final Constant SIT_PROJ_CANCELADO_CLIENTE = new Constant("Canc. cliente", "CC", Color.RED, Color.BLACK);
	public static final Constant SIT_PROJ_CANCELADO_PRESTADOR = new Constant("Canc. prestador ", "CP", Color.RED, Color.BLACK);
	public static final Constant SIT_PROJ_FINALIZADO = new Constant("Finalizado", "FN", Color.BLUE, Color.WHITE);
	
	public static final Constant TP_PROJ_CONTRATO = new Constant("Contrato", "C", Color.CYAN, Color.BLACK);
	public static final Constant TP_PROJ_PROJETO = new Constant("Projeto", "P", Color.BLUE, Color.WHITE);
	public static final Constant TP_PROJ_SUB_PROJETO = new Constant("Sub-Projeto", "S", Color.RED, Color.BLACK);

	private static Vector<Constant> listSitproj = null;
	
	private static Vector<Constant> listTpproj = null;
	
	public static Vector<Constant> getListSitproj() {
		if (listSitproj==null) {
			listSitproj = new Vector<Constant>();
			listSitproj.add( SIT_PROJ_PENDENTE );
			listSitproj.add( SIT_PROJ_EM_PLANEJAMENTO );
			listSitproj.add( SIT_PROJ_PLANEJADO );
			listSitproj.add( SIT_PROJ_EM_EXECUCAO );
			listSitproj.add( SIT_PROJ_EXECUTADO );
			listSitproj.add( SIT_PROJ_PARALIZADO );
			listSitproj.add( SIT_PROJ_CANCELADO_CLIENTE );
			listSitproj.add( SIT_PROJ_CANCELADO_PRESTADOR );
			listSitproj.add( SIT_PROJ_FINALIZADO );
		}
		return listSitproj;
	}
	
	public static Vector<Constant> getListTpproj() {
		if (listTpproj==null) {
			listTpproj = new Vector<Constant>();
			listTpproj.add( TP_PROJ_CONTRATO );
			listTpproj.add( TP_PROJ_PROJETO );
			listTpproj.add( TP_PROJ_SUB_PROJETO );
	
		}
		return listTpproj;
	}
	
	public static Vector<String> getSitprojName() {
		Vector<String> result = new Vector<String>();
		listSitproj = getListSitproj();
		for (int i=0; i<listSitproj.size(); i++) {
			result.add( listSitproj.elementAt( i ) .getName());
		}
		return result;
	}
	
	public static Vector<String> getTpprojName() {
		Vector<String> result = new Vector<String>();
		listTpproj= getListTpproj();
		for (int i=0; i<listTpproj.size(); i++) {
			result.add( listTpproj.elementAt( i ) .getName());
		}
		return result;
	}

	public static Vector<String> getSitprojValue() {
		Vector<String> result = new Vector<String>();
		listSitproj = getListSitproj();
		for (int i=0; i<listSitproj.size(); i++) {
			result.add( (String) listSitproj.elementAt( i ) .getValue());
		}
		return result;
	}
	
	public static Vector<String> getTpprojValue() {
		Vector<String> result = new Vector<String>();
		listTpproj = getListTpproj();
		for (int i=0; i<listTpproj.size(); i++) {
			result.add( (String) listTpproj.elementAt( i ) .getValue());
		}
		return result;
	}
	
}
