package org.freedom.modulos.pcp.business.object;

import java.awt.Color;
import java.util.Vector;

import javax.swing.ImageIcon;

import org.freedom.bmps.Icone;
import org.freedom.infra.pojos.Constant;

public class StatusOP implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final Constant OP_PENDENTE = new Constant( "Pendente", "PE" );

	public static final Constant OP_CANCELADA = new Constant( "Cancelada", "CA" );

	public static final Constant OP_FINALIZADA = new Constant( "Finalizada", "FN" );

	public static final Constant OP_BLOQUEADA = new Constant( "Bloqueada", "BL" );
	
	public static final Color COR_NAO_SALVO = Color.GRAY;

	public static final Color COR_PENDENTE = Color.ORANGE;
	
	public static final Color COR_CANCELADA = Color.DARK_GRAY;
	
	public static final Color COR_BLOQUEADA = Color.BLUE;
	
	public static final Color COR_FINALIZADA = new Color( 45, 190, 60 );
	
	public static String IMG_TAMANHO_M = "16x16";

	public static String IMG_TAMANHO_P = "10x10";

	public static ImageIcon getImagem( String status, String tamanho ) {

		ImageIcon img = null;

		ImageIcon IMG_OP_PENDENTE = Icone.novo( "op_pendente_" + tamanho + ".gif" );
		
		ImageIcon IMG_OP_CANCELADA = Icone.novo( "op_cancelada_" + tamanho + ".gif" ); 
		
		ImageIcon IMG_OP_FINALIZADA = Icone.novo( "op_finalizada_" + tamanho + ".gif" );
		
		ImageIcon IMG_OP_BLOQUEADA = Icone.novo( "op_bloqueada_" + tamanho + ".gif" );


		try {

			if ( status.equals( OP_PENDENTE.getValue() ) ) {
				return IMG_OP_PENDENTE;
			}
			else if ( status.equals( OP_CANCELADA.getValue() ) ) {
				return IMG_OP_CANCELADA;
			}
			else if ( status.equals( OP_FINALIZADA.getValue() ) ) {
				return IMG_OP_FINALIZADA;
			}
			else if ( status.equals( OP_BLOQUEADA.getValue() ) ) {
				return IMG_OP_BLOQUEADA;
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return img;
	}

	public static Vector<String> getLabels() {

		Vector<String> ret = new Vector<String>();

		ret.add( OP_PENDENTE.getName() );
		ret.add( OP_CANCELADA.getName() );
		ret.add( OP_FINALIZADA.getName() );
		ret.add( OP_BLOQUEADA.getName() );

		return ret;

	}

	public static Vector<Object> getValores() {

		Vector<Object> ret = new Vector<Object>();

		ret.add( OP_PENDENTE.getValue() );
		ret.add( OP_CANCELADA.getValue() );
		ret.add( OP_FINALIZADA.getValue() );
		ret.add( OP_BLOQUEADA.getValue() );

		return ret;

	}
	
}
