package test;

import java.util.HashMap;
import java.util.Map;

public class TesteJPA {

	/**
	 * @param args
	 */

	// @KeyField(name="teste")
	// private String teste="Teste1";

	public static void main(String[] args) {
		Map<String, Object> m1 = new HashMap<String, Object>();
		Map<String, Object> m2 = new HashMap<String, Object>();

		m1.put("1", 1);
		m1.put("2", 2);

		m2.put("1", 1);
		m2.put("2", 2);

		// Object[][] i = {{"Teste","1"},{"Teste2","2"}};

		// TesteJPA.TesteKey key1 = new TesteJPA().new TesteKey( i );
		// TesteJPA.TesteKey key2 = new TesteJPA().new TesteKey( i );

		// System.out.println(key1.hashCode());
		// System.out.println(key2.hashCode());

		String st1 = new String("123");
		String st2 = new String("123");

		System.out.println(st1.hashCode());
		System.out.println(st2.hashCode());

		// System.out.println(key1.getInternalKey().hashCode());
		// System.out.println(key2.getInternalKey().hashCode());

	}

	/*
	 * class TesteKey extends Key { private String teste = null; private String
	 * teste2 = null;
	 * 
	 * // public TesteKey(Object[][] keys) { //super(TesteJPA.TesteKey.class,
	 * keys); //}
	 * 
	 * protected void setTeste(String teste) { this.teste = teste; } protected
	 * void setTeste2(String teste2) { this.teste2 = teste2; } }
	 */

}
