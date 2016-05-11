package org.freedom.modulos.gms.business.object;

import java.awt.Color;
import java.awt.Font;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import org.freedom.bmps.Icone;
import org.freedom.infra.pojos.Constant;
import org.freedom.library.swing.component.JLabelPad;

public class StatusOS implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public static final Constant OS_PENDENTE = new Constant( "Pendente", "PE" );

	public static final Constant OS_ANALISE = new Constant( "Em analise", "AN" );

	public static final Constant OS_ORCAMENTO = new Constant( "Em orçamento", "EO" );

	public static final Constant OS_APROVADA = new Constant( "Aprovada", "OA" );

	public static final Constant OS_CANCELADA = new Constant( "Cancelada", "CA" );

	public static final Constant OS_ENCAMINHADO = new Constant( "Encaminhado", "EC" );
	
	public static final Constant OS_ANDAMENTO = new Constant( "Em andamento", "EA" );

	public static final Constant OS_PRONTO = new Constant( "Pronto", "PT" );

	public static final Constant OS_FINALIZADA = new Constant( "Finalizada", "FN" );

	public static final Constant IT_OS_NAO_SALVO = new Constant( "Não Salvo", "" );
	
	public static final Constant IT_OS_PENDENTE = new Constant( "Pendente", "PE" );
	
	public static final Constant IT_OS_ENCAMINHADO = new Constant( "Encaminhado", "EC" );

	public static final Constant IT_OS_EM_ANDAMENTO = new Constant( "Em andamento", "EA" );

	public static final Constant IT_OS_CONCLUIDO = new Constant( "Concluído", "CO" );
	
	public static final Color COR_NAO_SALVO = Color.GRAY;

	public static final Color COR_PENDENTE = Color.ORANGE;
	
	public static final Color COR_EM_DESENVOLVIMENTO = Color.BLUE;
	
	public static final Color COR_CONCLUIDO = new Color( 45, 190, 60 );
	
	public static String IMG_TAMANHO_M = "16x16";

	public static String IMG_TAMANHO_P = "10x10";

	public static ImageIcon getImagem( String status, String tamanho ) {

		ImageIcon img = null;

		ImageIcon IMG_OS_PENDENTE = Icone.novo( "os_pendente_" + tamanho + ".png" );

		ImageIcon IMG_OS_ANDAMENTO = Icone.novo( "os_em_andamento_" + tamanho + ".png" );
		
		ImageIcon IMG_OS_ENCAMINHADO = Icone.novo( "os_em_andamento_" + tamanho + ".png" );

		ImageIcon IMG_OS_ANALISE = Icone.novo( "os_em_analise_" + tamanho + ".png" );

		ImageIcon IMG_OS_PRONTO = Icone.novo( "os_pronta_" + tamanho + ".png" );

		ImageIcon IMG_OS_FINALIZADA = Icone.novo( "os_finalizada_" + tamanho + ".png" );

		ImageIcon IMG_OS_ORCAMENTO = Icone.novo( "os_orcamento_" + tamanho + ".png" );

		ImageIcon IMG_OS_APROVADA = Icone.novo( "os_aprovada_" + tamanho + ".png" );

		ImageIcon IMG_OS_CANCELADA = Icone.novo( "os_cancelada_" + tamanho + ".png" );

		try {

			if ( status.equals( OS_PENDENTE.getValue() ) ) {
				return IMG_OS_PENDENTE;
			}
			else if ( status.equals( OS_ANALISE.getValue() ) ) {
				return IMG_OS_ANALISE;
			}
			else if ( status.equals( OS_ORCAMENTO.getValue() ) ) {
				return IMG_OS_ORCAMENTO;
			}
			else if ( status.equals( OS_APROVADA.getValue() ) ) {
				return IMG_OS_APROVADA;
			}
			else if ( status.equals( OS_CANCELADA.getValue() ) ) {
				return IMG_OS_CANCELADA;
			}
			else if ( status.equals( OS_ENCAMINHADO.getValue() ) ) {
				return IMG_OS_ENCAMINHADO;
			}
			else if ( status.equals( OS_ANDAMENTO.getValue() ) ) {
				return IMG_OS_ANDAMENTO;
			}
			else if ( status.equals( OS_PRONTO.getValue() ) ) {
				return IMG_OS_PRONTO;
			}
			else if ( status.equals( OS_FINALIZADA.getValue() ) ) {
				return IMG_OS_FINALIZADA;
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return img;
	}

	public static Vector<String> getLabels() {

		Vector<String> ret = new Vector<String>();

		ret.add( OS_PENDENTE.getName() );
		ret.add( OS_ANALISE.getName() );
		ret.add( OS_ORCAMENTO.getName() );
		ret.add( OS_APROVADA.getName() );
		ret.add( OS_CANCELADA.getName() );
		ret.add( OS_ENCAMINHADO.getName() );
		ret.add( OS_ANDAMENTO.getName() );
		ret.add( OS_PRONTO.getName() );
		ret.add( OS_FINALIZADA.getName() );

		return ret;

	}

	public static Vector<Object> getValores() {

		Vector<Object> ret = new Vector<Object>();

		ret.add( OS_PENDENTE.getValue() );
		ret.add( OS_ANALISE.getValue() );
		ret.add( OS_ORCAMENTO.getValue() );
		ret.add( OS_APROVADA.getValue() );
		ret.add( OS_CANCELADA.getValue() );
		ret.add( OS_ENCAMINHADO.getValue() );
		ret.add( OS_ANDAMENTO.getValue() );
		ret.add( OS_PRONTO.getValue() );
		ret.add( OS_FINALIZADA.getValue() );

		return ret;

	}
	
	public static Vector<String> getLabelsItOS() {

		Vector<String> ret = new Vector<String>();

		ret.add( IT_OS_NAO_SALVO.getName() );
		ret.add( IT_OS_PENDENTE.getName() );
		ret.add( IT_OS_ENCAMINHADO.getName() );
		ret.add( IT_OS_EM_ANDAMENTO.getName() );
		ret.add( IT_OS_CONCLUIDO.getName() );

		return ret;

	}

	public static Vector<Object> getValoresItOS() {

		Vector<Object> ret = new Vector<Object>();

		ret.add( IT_OS_NAO_SALVO.getValue() );
		ret.add( IT_OS_PENDENTE.getValue() );
		ret.add( IT_OS_ENCAMINHADO.getValue() );
		ret.add( IT_OS_EM_ANDAMENTO.getValue() );
		ret.add( IT_OS_CONCLUIDO.getValue() );

		return ret;

	}
	
	
	
	public static void atualizaStatusItOS( String status, JLabelPad lbstatus ) {

		lbstatus.setForeground( Color.WHITE );
		lbstatus.setFont( new Font( "Arial", Font.BOLD, 13 ) );
		lbstatus.setOpaque( true );
		lbstatus.setHorizontalAlignment( SwingConstants.CENTER );

		if ( IT_OS_NAO_SALVO.getValue().equals(status) ) {
			lbstatus.setText( IT_OS_NAO_SALVO.getName() );
			lbstatus.setBackground( COR_NAO_SALVO );
		}
		else if ( IT_OS_PENDENTE.getValue().equals( status ) ) {
			lbstatus.setText( IT_OS_PENDENTE.getName() );
			lbstatus.setBackground( COR_PENDENTE );
		}
		else if ( IT_OS_ENCAMINHADO.getValue().equals( status ) ) {
			lbstatus.setText( IT_OS_ENCAMINHADO.getName() );
			lbstatus.setBackground( COR_EM_DESENVOLVIMENTO );
		}
		else if ( IT_OS_EM_ANDAMENTO.getValue().equals( status ) ) {
			lbstatus.setText( IT_OS_EM_ANDAMENTO.getName() );
			lbstatus.setBackground( COR_EM_DESENVOLVIMENTO );
		}
		else if ( IT_OS_CONCLUIDO.getValue().equals( status ) ) {
			lbstatus.setText( IT_OS_CONCLUIDO.getName() );
			lbstatus.setBackground( COR_CONCLUIDO );
		}
		

	}

}
