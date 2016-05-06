package org.freedom.modulos.gms.business.object;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import org.freedom.infra.pojos.Constant;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;

public class TipoMov implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Tipo de movimento de entrada

	public static final Constant TM_ORCAMENTO_COMPRA = new Constant( "Orçamento", "OC" );

	public static final Constant TM_PEDIDO_COMPRA = new Constant( "Pedido", "PC" );

	public static final Constant TM_COMPRA = new Constant( "Compra", "CP" );

	public static final Constant TM_ORDEM_DE_PRODUCAO = new Constant( "Ordem de produção", "OP" );

	public static final Constant TM_DEVOLUCAO_VENDA = new Constant( "Devolução", "DV" );

	public static final Constant TM_DEVOLUCAO_REMESSA = new Constant( "Devolução de remessa", "DR" );

	public static final Constant TM_TRANSFERENCIA_ENTRADA = new Constant( "Transferência", "TR" );

	public static final Constant TM_CONHECIMENTO_FRETE_COMPRA = new Constant( "Conhecimento de frete", "CF" );

	public static final Constant TM_NOTA_FISCAL_COMPLEMENTAR_COMPRA = new Constant( "Nota fiscal complementar", "CO" );

	public static final Constant TM_NOTA_FISCAL_IMPORTACAO = new Constant( "Nota fiscal de importação", "DI" );

	// Tipo de movimento de saída

	public static final Constant TM_ORCAMENTO_VENDA = new Constant( "Orçamento", "OV" );

	public static final Constant TM_PEDIDO_VENDA = new Constant( "Pedido", "PV" );

	public static final Constant TM_VENDA = new Constant( "Venda comum", "VD" );

	public static final Constant TM_VENDA_ECF = new Constant( "Venda ECF", "VE" );

	public static final Constant TM_VENDA_TELEVENDAS = new Constant( "Venda Telemarketing", "VT" );

	public static final Constant TM_VENDA_SERVICO = new Constant( "Venda Serviço", "SE" );

	public static final Constant TM_BONIFICACAO_SAIDA = new Constant( "Bonificação", "BN" );

	public static final Constant TM_DEVOLUCAO_COMPRA = new Constant( "Devolução", "DV" );

	public static final Constant TM_TRANSFERENCIA_SAIDA = new Constant( "Transferência", "TR" );

	public static final Constant TM_PERDA_SAIDA = new Constant( "Perda", "PE" );

	public static final Constant TM_CONSIGNACAO_SAIDA = new Constant( "Consignação", "CS" );

	public static final Constant TM_DEVOLUCAO_CONSIGNACAO = new Constant( "Devolução de consignação", "CE" );

	public static final Constant TM_REQUISICAO_DE_MATERIAL = new Constant( "Requisição de material", "RM" );

	public static final Constant TM_NOTA_FISCAL_COMPLEMENTAR_SAIDA = new Constant( "Nota fiscal complementar", "CO" );

	public static final Constant TM_REMESSA_SAIDA = new Constant( "Remessa", "VR" );
	
	// Tipo de movimento de inventário

	public static final Constant TM_INVENTARIO = new Constant( "Inventário de estoque", "IV" );

	// Tipos de fluxo

	public static final Constant ENTRADA = new Constant( "Entrada", "E" );

	public static final Constant SAIDA = new Constant( "Saída", "S" );

	public static final Constant INVENTARIO = new Constant( "Inventário", "I" );

	public static Vector<String> getLabels( String tipo ) {

		Vector<String> ret = new Vector<String>();

		ret.addElement( "<--Selecione-->" );

		if ( ENTRADA.getValue().equals( tipo ) ) {

			ret.add( TM_ORCAMENTO_COMPRA.getName() );
			ret.add( TM_PEDIDO_COMPRA.getName() );
			ret.add( TM_COMPRA.getName() );
			ret.add( TM_ORDEM_DE_PRODUCAO.getName() );
			ret.add( TM_DEVOLUCAO_VENDA.getName() );
			ret.add( TM_DEVOLUCAO_REMESSA.getName() );
			ret.add( TM_CONHECIMENTO_FRETE_COMPRA.getName() );
			ret.add( TM_NOTA_FISCAL_COMPLEMENTAR_COMPRA.getName() );
			ret.add( TM_NOTA_FISCAL_IMPORTACAO.getName() );

		}
		else if ( SAIDA.getValue().equals( tipo ) ) {

			ret.add( TM_ORCAMENTO_VENDA.getName() );
			ret.add( TM_PEDIDO_VENDA.getName() );
			ret.add( TM_VENDA.getName() );
			ret.add( TM_VENDA_ECF.getName() );
			ret.add( TM_VENDA_TELEVENDAS.getName() );
			ret.add( TM_VENDA_SERVICO.getName() );
			ret.add( TM_BONIFICACAO_SAIDA.getName() );
			ret.add( TM_DEVOLUCAO_COMPRA.getName() );
			ret.add( TM_TRANSFERENCIA_SAIDA.getName() );
			ret.add( TM_PERDA_SAIDA.getName() );
			ret.add( TM_CONSIGNACAO_SAIDA.getName() );
			ret.add( TM_DEVOLUCAO_CONSIGNACAO.getName() );
			ret.add( TM_REQUISICAO_DE_MATERIAL.getName() );
			ret.add( TM_NOTA_FISCAL_COMPLEMENTAR_SAIDA.getName() );
			ret.add( TM_REMESSA_SAIDA.getName() );

		}
		else if ( INVENTARIO.getValue().equals( tipo ) ) {
			ret.add( TM_INVENTARIO.getName() );
		}

		return ret;

	}

	public static String getDescTipo(String tipo) {
		
		String ret = "";
		
		try {
		
			HashMap<String, Constant> constantes = getConstants();

			ret = constantes.get( tipo ).getName();
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
		
	}
	
	
	
	public static Vector<Object> getValores( String tipo ) {

		Vector<Object> ret = new Vector<Object>();

		ret.addElement( "" );

		if ( ENTRADA.getValue().equals( tipo ) ) {

			ret.add( TM_ORCAMENTO_COMPRA.getValue() );
			ret.add( TM_PEDIDO_COMPRA.getValue() );
			ret.add( TM_COMPRA.getValue() );
			ret.add( TM_ORDEM_DE_PRODUCAO.getValue() );
			ret.add( TM_DEVOLUCAO_VENDA.getValue() );
			ret.add( TM_DEVOLUCAO_REMESSA.getValue() );
			ret.add( TM_CONHECIMENTO_FRETE_COMPRA.getValue() );
			ret.add( TM_NOTA_FISCAL_COMPLEMENTAR_COMPRA.getValue() );
			ret.add( TM_NOTA_FISCAL_IMPORTACAO.getValue() );

		}
		else if ( SAIDA.getValue().equals( tipo ) ) {

			ret.add( TM_ORCAMENTO_VENDA.getValue() );
			ret.add( TM_PEDIDO_VENDA.getValue() );
			ret.add( TM_VENDA.getValue() );
			ret.add( TM_VENDA_ECF.getValue() );
			ret.add( TM_VENDA_TELEVENDAS.getValue() );
			ret.add( TM_VENDA_SERVICO.getValue() );
			ret.add( TM_BONIFICACAO_SAIDA.getValue() );
			ret.add( TM_DEVOLUCAO_COMPRA.getValue() );
			ret.add( TM_TRANSFERENCIA_SAIDA.getValue() );
			ret.add( TM_PERDA_SAIDA.getValue() );
			ret.add( TM_CONSIGNACAO_SAIDA.getValue() );
			ret.add( TM_DEVOLUCAO_CONSIGNACAO.getValue() );
			ret.add( TM_REQUISICAO_DE_MATERIAL.getValue() );
			ret.add( TM_NOTA_FISCAL_COMPLEMENTAR_SAIDA.getValue() );
			ret.add( TM_REMESSA_SAIDA.getValue() );

		}
		else if ( INVENTARIO.getValue().equals( tipo ) ) {
			ret.add( TM_INVENTARIO.getValue() );
		}

		return ret;

	}
	
	public static HashMap<String,Constant> getConstants( ) {

		HashMap<String, Constant> ret = new HashMap<String,Constant>();

		ret.put( TM_ORCAMENTO_COMPRA.getValue().toString(), TM_ORCAMENTO_COMPRA );
		ret.put( TM_PEDIDO_COMPRA.getValue().toString(), TM_PEDIDO_COMPRA );
		ret.put( TM_COMPRA.getValue().toString(), TM_COMPRA );
		ret.put( TM_ORDEM_DE_PRODUCAO.getValue().toString(), TM_ORDEM_DE_PRODUCAO );
		ret.put( TM_DEVOLUCAO_VENDA.getValue().toString(), TM_DEVOLUCAO_VENDA );
		ret.put( TM_DEVOLUCAO_REMESSA.getValue().toString(), TM_DEVOLUCAO_REMESSA );
		ret.put( TM_CONHECIMENTO_FRETE_COMPRA.getValue().toString(), TM_CONHECIMENTO_FRETE_COMPRA );
		ret.put( TM_NOTA_FISCAL_COMPLEMENTAR_COMPRA.getValue().toString(), TM_NOTA_FISCAL_COMPLEMENTAR_COMPRA );
		ret.put( TM_NOTA_FISCAL_IMPORTACAO.getValue().toString(), TM_NOTA_FISCAL_IMPORTACAO );
		ret.put( TM_ORCAMENTO_VENDA.getValue().toString(), TM_ORCAMENTO_VENDA );
		ret.put( TM_PEDIDO_VENDA.getValue().toString(), TM_PEDIDO_VENDA );
		ret.put( TM_VENDA.getValue().toString(), TM_VENDA );
		ret.put( TM_VENDA_ECF.getValue().toString(), TM_VENDA_ECF );
		ret.put( TM_VENDA_TELEVENDAS.getValue().toString(), TM_VENDA_TELEVENDAS );
		ret.put( TM_VENDA_SERVICO.getValue().toString(), TM_VENDA_SERVICO );
		ret.put( TM_BONIFICACAO_SAIDA.getValue().toString(), TM_BONIFICACAO_SAIDA );
		ret.put( TM_DEVOLUCAO_COMPRA.getValue().toString(), TM_DEVOLUCAO_COMPRA );
		ret.put( TM_TRANSFERENCIA_SAIDA.getValue().toString(), TM_TRANSFERENCIA_SAIDA );
		ret.put( TM_PERDA_SAIDA.getValue().toString(), TM_PERDA_SAIDA );
		ret.put( TM_CONSIGNACAO_SAIDA.getValue().toString(), TM_CONSIGNACAO_SAIDA );
		ret.put( TM_DEVOLUCAO_CONSIGNACAO.getValue().toString(), TM_DEVOLUCAO_CONSIGNACAO );
		ret.put( TM_REQUISICAO_DE_MATERIAL.getValue().toString(), TM_REQUISICAO_DE_MATERIAL );
		ret.put( TM_NOTA_FISCAL_COMPLEMENTAR_SAIDA.getValue().toString(), TM_NOTA_FISCAL_COMPLEMENTAR_SAIDA );
		ret.put( TM_REMESSA_SAIDA.getValue().toString(), TM_REMESSA_SAIDA );
		ret.put( TM_INVENTARIO.getValue().toString(), TM_INVENTARIO );

		return ret;

	}
	
	public static Integer getTipoMovFrete() {
		Integer ret = null;
		
		try {

			StringBuilder sql = new StringBuilder();
			sql.append( "select p.codtipomov9 from SGPREFERE1 p where p.codemp=? and p.codfilial=?" );

			PreparedStatement ps = Aplicativo.getInstace().getConexao().prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				ret = rs.getInt( "codtipomov9" );
				
			}

			rs.close();
			ps.close();

		} 
		catch ( SQLException e ) {
			e.printStackTrace();		
		}
		return ret;
	}
	
	public static String getSerieTipoMov(Integer codtipomov) {
		String ret = null;
		
		try {

			StringBuilder sql = new StringBuilder();
			sql.append( "select serie from eqtipomov where codemp=? and codfilial=? and codtipomov=? " );

			PreparedStatement ps = Aplicativo.getInstace().getConexao().prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQTIPOMOV" ) );
			ps.setInt( 3, codtipomov );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				ret = rs.getString( "serie" );
				
			}

			rs.close();
			ps.close();

		} 
		catch ( SQLException e ) {
			e.printStackTrace();		
		}
		return ret;
	}
	
	public static Integer getDocSerie(String serie) {
		Integer ret = null;
		
		try {

			StringBuilder sql = new StringBuilder();
			
			sql.append( "select coalesce(docserie,0) docserie from lfseqserie where " );			
			sql.append( "codemp=? and codfilial=? and serie=? and " );
			sql.append( "codempss=? and codfilialss=? and ativserie='S'" );
			
			PreparedStatement ps = Aplicativo.getInstace().getConexao().prepareStatement( sql.toString() );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "LFSERIE" ) );			
			ps.setString( 3, serie );
			
			ps.setInt( 4, Aplicativo.iCodEmp );
			ps.setInt( 5, ListaCampos.getMasterFilial( "LFSEQSERIE" ) );			

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				ret = rs.getInt( "docserie" );
				
			}
			else {
				ret = 1;
			}

			rs.close();
			ps.close();

		} 
		catch ( SQLException e ) {
			e.printStackTrace();		
		}
		return ret;
	}

	
}
