package org.freedom.modulos.crm.business.object;

import java.sql.Types;
import java.util.Vector;

import javax.swing.ImageIcon;

import org.freedom.bmps.Icone;
import org.freedom.infra.beans.Field;
import org.freedom.infra.pojos.Constant;

public class Chamado {

	private static final long serialVersionUID = 1L;

	public enum Campos {
		CODEMP, CODFILIAL, CODCHAMADO, DESCCHAMADO, DETCHAMADO, OBSCHAMADO, CODEMPCL, CODFILIALCL, CODCLI, SOLICITANTE, PRIORIDADE, CODEMPTC, CODFILIALTC, CODTPCHAMADO, STATUS, DTCHAMADO, DTPREVISAO, QTDHORASPREVISAO, DTCONCLUSAO, DTINS, HINS, IDUSUINS, DTALT, HALT, IDUSUALT
	}

	public static final Constant CHAMADO_PENDENTE = new Constant( "Pendente", "PE" );

	public static final Constant CHAMADO_ANALISE = new Constant( "Em analise", "AN" );

	public static final Constant CHAMADO_EM_ANDAMENTO = new Constant( "Em andamento", "EA" );

	public static final Constant CHAMADO_CANCELADO = new Constant( "Cancelado", "CA" );

	public static final Constant CHAMADO_CONCLUIDO = new Constant( "Concluído", "CO" );

	public static String IMG_TAMANHO_M = "16x16";

	public static String IMG_TAMANHO_P = "10x10";

	private Field codchamado = new Field( "codchamado", null, Types.INTEGER );

	private Field deschamado = new Field( "descchamado", null, Types.CHAR );

	private Field detchamado = new Field( "detchamado", null, Types.CHAR );

	private Field obschamado = new Field( "obschamado", null, Types.CHAR );

	private Field solicitante = new Field( "solicitante", null, Types.CHAR );

	private Field prioridade = new Field( "prioridade", null, Types.INTEGER );

	private Field codtpchamado = new Field( "codtpchamado", null, Types.INTEGER );

	private Field status = new Field( "status", null, Types.CHAR );

	private Field dtchamado = new Field( "dtchamado", null, Types.DATE );

	private Field dtprevisao = new Field( "dtprevisao", null, Types.DATE );

	private Field qtdhorasprevisao = new Field( "qtdhorasprevisao", null, Types.DECIMAL );

	private Field dtconclusao = new Field( "dtconclusao", null, Types.DATE );

	public static ImageIcon getImagem( String status, String tamanho ) {

		ImageIcon img = null;

		ImageIcon IMG_CHAMADO_PENDENTE = Icone.novo( "chamado_pendente_" + tamanho + ".png" );

		ImageIcon IMG_CHAMADO_ANDAMENTO = Icone.novo( "chamado_em_andamento_" + tamanho + ".png" );

		ImageIcon IMG_CHAMADO_ANALISE = Icone.novo( "chamado_em_analise_" + tamanho + ".png" );

		ImageIcon IMG_CHAMADO_CONCLUIDO = Icone.novo( "chamado_concluido_" + tamanho + ".png" );

		ImageIcon IMG_CHAMADO_CANCELADO = Icone.novo( "chamado_cancelado_" + tamanho + ".png" );

		try {

			if ( status.equals( CHAMADO_PENDENTE.getValue() ) ) {
				return IMG_CHAMADO_PENDENTE;
			}
			else if ( status.equals( CHAMADO_ANALISE.getValue() ) ) {
				return IMG_CHAMADO_ANALISE;
			}
			else if ( status.equals( CHAMADO_EM_ANDAMENTO.getValue() ) ) {
				return IMG_CHAMADO_ANDAMENTO;
			}
			else if ( status.equals( CHAMADO_CANCELADO.getValue() ) ) {
				return IMG_CHAMADO_CANCELADO;
			}
			else if ( status.equals( CHAMADO_CONCLUIDO.getValue() ) ) {
				return IMG_CHAMADO_CONCLUIDO;
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return img;
	}

	public static Vector<String> getLabels() {

		Vector<String> ret = new Vector<String>();

		ret.addElement( "<--Selecione-->" );

		ret.add( CHAMADO_PENDENTE.getName() );
		ret.add( CHAMADO_ANALISE.getName() );
		ret.add( CHAMADO_EM_ANDAMENTO.getName() );
		ret.add( CHAMADO_CANCELADO.getName() );
		ret.add( CHAMADO_CONCLUIDO.getName() );

		return ret;

	}

	public static Vector<Object> getValores() {

		Vector<Object> ret = new Vector<Object>();

		ret.addElement( "" );

		ret.add( (String) CHAMADO_PENDENTE.getValue() );
		ret.add( (String) CHAMADO_ANALISE.getValue() );
		ret.add( (String) CHAMADO_EM_ANDAMENTO.getValue() );
		ret.add( (String) CHAMADO_CANCELADO.getValue() );
		ret.add( (String) CHAMADO_CONCLUIDO.getValue() );

		return ret;

	}

}
