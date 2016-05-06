package org.freedom.modulos.fnc.business.component.siacc;

import org.freedom.library.business.exceptions.ExceptionSiacc;
import org.freedom.library.functions.Funcoes;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.ETipo;


public abstract class Reg {

	private char tiporeg;

	protected StringBuilder sbreg = new StringBuilder();

	public Reg( char codreg ) {

		this.sbreg.append( codreg );
		this.tiporeg = codreg;
	}

	public String format( Object obj, ETipo tipo, int tam, int dec ) {

		String retorno = null;
		String str = null;
		// String formato = null;
		if ( obj == null ) {
			str = "";
		}
		else {
			str = obj.toString();
		}
		if ( tipo == ETipo.$9 ) {
			retorno = Funcoes.transValor( str, tam, dec, true );
		}
		else {
			retorno = Funcoes.adicionaEspacos( str, tam ); 
		}
		return retorno;
	}

	public String toString() {

		return sbreg.toString();
	}

	protected abstract void parseLine( String line ) throws ExceptionSiacc;

	public char getTiporeg() {

		return this.tiporeg;
	}
}
