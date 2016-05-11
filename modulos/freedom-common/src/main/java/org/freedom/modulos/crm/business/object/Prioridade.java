package org.freedom.modulos.crm.business.object;

import java.util.Vector;

import org.freedom.infra.pojos.Constant;

public class Prioridade implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public static final Constant MUITO_ALTA = new Constant( "Muito Alta", new Integer( 1 ) );

	public static final Constant ALTA = new Constant( "Alta", new Integer( 2 ) );

	public static final Constant MEDIA = new Constant( "Média", new Integer( 3 ) );

	public static final Constant BAIXA = new Constant( "Baixa", new Integer( 4 ) );

	public static final Constant MUITO_BAIXA = new Constant( "Muito Baixa", new Integer( 5 ) );

	public static Vector<String> getLabels() {

		Vector<String> ret = new Vector<String>();

		ret.addElement( "<--Selecione-->" );

		ret.add( MUITO_ALTA.getName() );
		ret.add( ALTA.getName() );
		ret.add( MEDIA.getName() );
		ret.add( BAIXA.getName() );
		ret.add( MUITO_BAIXA.getName() );

		return ret;

	}

	public static Vector<Object> getValores() {

		Vector<Object> ret = new Vector<Object>();

		ret.addElement( "" );

		ret.add( (Integer) MUITO_ALTA.getValue() );
		ret.add( (Integer) ALTA.getValue() );
		ret.add( (Integer) MEDIA.getValue() );
		ret.add( (Integer) BAIXA.getValue() );
		ret.add( (Integer) MUITO_BAIXA.getValue() );

		return ret;

	}

}
