package org.freedom.modulos.gms.business.object;

import java.util.Vector;

import org.freedom.infra.pojos.Constant;

public class TipoExpedicao implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Tipo de recebimento de expedição de produtos

	public static final Constant TIPO_EXPEDICAO_PESAGEM = new Constant( "Expedição com pesagem", "EP" );

	public static final Constant TIPO_EXPEDICAO_SEM_PESAGEM = new Constant( "Expedição sem pesagem", "SP" );

	// Processos

	public static final Constant PROCESSO_PESAGEM_INICIAL = new Constant( "Pesagem inicial", "PI" );

	public static final Constant PROCESSO_PESAGEM_FINAL = new Constant( "Pesagem final", "PF" );
	
	public static final Constant PROCESSO_ROMANEIO = new Constant( "Romaneio", "RO" );

	public static Vector<String> getLabelsTipoRecMerc() {

		Vector<String> ret = new Vector<String>();

		ret.addElement( "<--Selecione-->" );

		ret.add( TIPO_EXPEDICAO_PESAGEM.getName() );
		ret.add( TIPO_EXPEDICAO_SEM_PESAGEM.getName() );

		return ret;

	}

	public static Vector<String> getValoresTipoRecMerc() {

		Vector<String> ret = new Vector<String>();

		ret.addElement( "" );

		ret.add( (String) TIPO_EXPEDICAO_PESAGEM.getValue() );
		ret.add( (String) TIPO_EXPEDICAO_SEM_PESAGEM.getValue() );

		return ret;

	}

	public static Vector<String> getLabelsProcesso( String tipo ) {

		Vector<String> ret = new Vector<String>();

		ret.addElement( "<--Selecione-->" );

		if ( TIPO_EXPEDICAO_PESAGEM.getValue().equals( tipo ) ) {
			ret.add( PROCESSO_PESAGEM_INICIAL.getName() );
			ret.add( PROCESSO_ROMANEIO.getName() );
			ret.add( PROCESSO_PESAGEM_FINAL.getName() );
		}
		else if ( TIPO_EXPEDICAO_SEM_PESAGEM.getValue().equals( tipo ) ) {
			ret.add( PROCESSO_ROMANEIO.getName() );
		}

		return ret;

	}

	public static Vector<Object> getValoresProcesso( String tipo ) {

		Vector<Object> ret = new Vector<Object>();

		ret.addElement( "" );

		if ( TIPO_EXPEDICAO_PESAGEM.getValue().equals( tipo ) ) {
			ret.add( PROCESSO_PESAGEM_INICIAL.getValue() );
			ret.add( PROCESSO_ROMANEIO.getValue() );
			ret.add( PROCESSO_PESAGEM_FINAL.getValue() );
		}
		else if ( TIPO_EXPEDICAO_SEM_PESAGEM.getValue().equals( tipo ) ) {
			ret.add( PROCESSO_ROMANEIO.getValue() );
		}
		return ret;

	}

}
