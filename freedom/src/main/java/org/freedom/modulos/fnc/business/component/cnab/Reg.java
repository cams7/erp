package org.freedom.modulos.fnc.business.component.cnab;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.business.exceptions.ExceptionCnab;
import org.freedom.library.functions.Funcoes;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.ETipo;


public abstract class Reg {
	
	public static final String CNAB_240 = "240";
	public static final String CNAB_400 = "400";

	protected final String LITERAL_REM = "REMESSA";
	protected final String LITERAL_SERV = "COBRANCA";
	protected final String LITERAL_SISTEMA = "MX";

	protected final String DATA_06 = "DDMMAA";
	protected final String DATA_08 = "DDMMAAAA";
	protected final String DATA_08_AAAAMMDD = "AAAAMMDD";

	public abstract void parseLine( String line ) throws ExceptionCnab;
	public abstract String getLine( String padraocnab ) throws ExceptionCnab;

	public String format( Object obj, ETipo tipo, int tam, int dec ) {

		String retorno = null;
		String str = null;

		if ( obj == null ) {
			str = "";
		} else {
			str = obj.toString();
		}

		if ( tipo == ETipo.$9 ) {

			if ( dec > 0 ) {
				retorno = Funcoes.transValor( str, tam, dec, true );
			} else {
				retorno = StringFunctions.strZero( str, tam );
			}
		} else {
			retorno = Funcoes.adicionaEspacos( str, tam );
		}

		return retorno;
	}

}
