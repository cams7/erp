package org.freedom.modulos.gms.business.object;

import java.util.Vector;

import org.freedom.infra.pojos.Constant;

public class TipoProd implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public static final Constant MERCADORIA_REVENDA = new Constant( "Mercadoria p/revenda", "P" );
	public static final Constant MERCADORIA_REVENDA_SPED = new Constant( MERCADORIA_REVENDA.getName(), "00" );	

	public static final Constant SERVICO = new Constant( "Serviço", "S" );
	public static final Constant SERVICO_SPED = new Constant( SERVICO.getName(), "09" );
	
	public static final Constant EQUIPAMENTO = new Constant( "Equipamento", "E" );
	public static final Constant EQUIPAMENTO_SPED = new Constant( EQUIPAMENTO.getName(), "08" );
	
	public static final Constant PRODUTO_ACABADO = new Constant( "Produto acabado", "F" );
	public static final Constant PRODUTO_ACABADO_SPED = new Constant( PRODUTO_ACABADO.getName(), "04" );
	
	public static final Constant MATERIA_PRIMA = new Constant( "Matéria prima", "M" );
	public static final Constant MATERIA_PRIMA_SPED = new Constant( MATERIA_PRIMA.getName(), "01" );

	public static final Constant ATIVO_IMOBILIZADO = new Constant( "Ativo imobilizado", "O" );
	public static final Constant ATIVO_IMOBILIZADO_SPED = new Constant( ATIVO_IMOBILIZADO.getName(), "08" );	
	
	public static final Constant MATERIAL_CONSUMO = new Constant( "Material de consumo", "C" );
	public static final Constant MATERIAL_CONSUMO_SPED = new Constant( MATERIAL_CONSUMO.getName(), "07" );
	
	public static final Constant EMBALAGEM = new Constant( "Embalagem", "02" );

	public static final Constant EM_PROCESSO = new Constant( "Em processo", "03" );

	public static final Constant SUB_PRODUTO = new Constant( "Subproduto", "05" );
	
	public static final Constant PRODUTO_INTERMEDIARIO = new Constant( "Produto intermediário", "06" );
	
	public static final Constant OUTROS_INSUMOS = new Constant( "Outros insumos", "10" );

	public static final Constant OUTRAS = new Constant( "Outros", "99" );

	public static Vector<String> getLabels() {

		Vector<String> ret = new Vector<String>();

		ret.add( MERCADORIA_REVENDA.getName() );
		ret.add( SERVICO.getName() );
		ret.add( EQUIPAMENTO.getName() );
		ret.add( PRODUTO_ACABADO.getName() );
		ret.add( MATERIA_PRIMA.getName() );
		ret.add( ATIVO_IMOBILIZADO.getName() );
		ret.add( MATERIAL_CONSUMO.getName() );
		ret.add( EMBALAGEM.getName() );
		ret.add( EM_PROCESSO.getName() );
		ret.add( SUB_PRODUTO.getName() );
		ret.add( PRODUTO_INTERMEDIARIO.getName() );
		ret.add( OUTROS_INSUMOS.getName() );
		ret.add( OUTRAS.getName() );

		
		
		return ret;

	}

	public static Vector<String> getValores() {

		Vector<String> ret = new Vector<String>();

		ret.add( MERCADORIA_REVENDA.getValue().toString() );
		ret.add( SERVICO.getValue().toString() );
		ret.add( EQUIPAMENTO.getValue().toString() );
		ret.add( PRODUTO_ACABADO.getValue().toString() );
		ret.add( MATERIA_PRIMA.getValue().toString() );
		ret.add( ATIVO_IMOBILIZADO.getValue().toString() );
		ret.add( MATERIAL_CONSUMO.getValue().toString() );
		ret.add( EMBALAGEM.getValue().toString() );
		ret.add( EM_PROCESSO.getValue().toString() );
		ret.add( SUB_PRODUTO.getValue().toString() );
		ret.add( PRODUTO_INTERMEDIARIO.getValue().toString() );
		ret.add( OUTROS_INSUMOS.getValue().toString() );
		ret.add( OUTRAS.getValue().toString() );
		return ret;

	}

	
	public static String getTipoItemSPED(String tipofreedom) {
	
		String ret = tipofreedom;
		
		try {

			if( MERCADORIA_REVENDA.getValue().equals( tipofreedom ) ) { 
				ret = MERCADORIA_REVENDA_SPED.getValue().toString();
			}
			else if( SERVICO.getValue().equals( tipofreedom ) ) { 
				ret = SERVICO_SPED.getValue().toString();
			}
			else if( EQUIPAMENTO.getValue().equals( tipofreedom ) ) { 
				ret = EQUIPAMENTO_SPED.getValue().toString();
			}
			else if( PRODUTO_ACABADO.getValue().equals( tipofreedom ) ) { 
				ret = PRODUTO_ACABADO_SPED.getValue().toString();
			}
			else if( MATERIA_PRIMA.getValue().equals( tipofreedom ) ) { 
				ret = MATERIA_PRIMA_SPED.getValue().toString();
			}
			else if( ATIVO_IMOBILIZADO.getValue().equals( tipofreedom ) ) { 
				ret = ATIVO_IMOBILIZADO_SPED.getValue().toString();
			}
			else if( MATERIAL_CONSUMO.getValue().equals( tipofreedom ) ) { 
				ret = MATERIAL_CONSUMO_SPED.getValue().toString();
			}			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;	
	
	}
	
}
