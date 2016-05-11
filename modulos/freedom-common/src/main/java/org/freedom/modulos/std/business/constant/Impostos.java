package org.freedom.modulos.std.business.constant;

import java.util.ArrayList;
import java.util.List;



public enum Impostos {
	
	IPI("IPI", "Imposto sobre Produto Industrializado"), 
	PIS("PIS", "Programa de integração social "),
	COFINS("COFINS", "Contribuição Social para o Financiamento da Seguridade Social"),
	ICMS("ICMS", "Imposto sobre Circulação de Mercadorias e Serviços"),
	ISS("ISS","Impostos sobre Serviços de Qualquer Natureza"),
	FUNRURAL("FUNRURAL","Contribuição ao Funrural "),
	IR("IR","Imposto sobre a Renda e proventos de qualquer natureza"),
	II("II", "Imposto sobre a importação de produtos estrangeiros"),
	TXSISCOMEX("TXSISCOMEX", "Taxa Siscomex"),
	ICMSDIF("ICMSDIF", "Imposto sobre Circulação de Mercadorias e Serviços Diferido"),
	ICMSPRES("ICMSPRES", "Imposto sobre Circulação de Mercadorias e Serviços Presumido"),
	COMPL("COMPL", "Valor complementar de importação")
	;
	
	private String value;
	
	private String desc;

	private Impostos(String value, String desc) {
		this.value = value;
		this.desc = desc;
	}
	
	
	public String getDesc() {
	
		return desc;
	}

	public void setDesc( String desc ) {
	
		this.desc = desc;
	}

	public String getValue() {
	
		return value;
	}

	public void setValue( String value ) {
	
		this.value = value;
	}
	
	public static List<Impostos> getImpostos() {
		List<Impostos> listaImp = new ArrayList<Impostos>();
		
		for (Impostos imp: Impostos.values()) {
			listaImp.add( imp );
		}
	
		return listaImp;
	}
	
}
