package org.freedom.modulos.std.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.freedom.infra.dao.AbstractDAO;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.modulos.std.business.object.CabecalhoVenda;
import org.freedom.modulos.std.business.object.ItemVenda;
import org.freedom.modulos.std.business.object.UpdateVenda;
import org.freedom.modulos.std.business.object.VdItVendaItVenda;


public class DAOVenda extends AbstractDAO {
	
	public DAOVenda( DbConnection cn) {

		super( cn );

	}
	
	public CabecalhoVenda getCabecalhoVenda (Integer codemp, Integer codfilial, Integer codvenda) throws Exception {
		
		StringBuilder sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CabecalhoVenda cabecalho = null;
		
		try{
	
			sql = new StringBuilder();
			sql.append( "SELECT VD.CODTIPOMOV, VD.CODCLI, VD.CODPLANOPAG, VD.CODVEND, VD.CODCLCOMIS " );
			sql.append( "FROM VDVENDA VD " );
			sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODVENDA=? " );
			ps = getConn().prepareStatement( sql.toString() );
			int param = 1;
			
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codvenda );
			
			rs = ps.executeQuery();
			
			if (rs.next()) {
				
				cabecalho = new CabecalhoVenda();
				cabecalho.setCodtipomov( rs.getInt( "CODTIPOMOV" ) );
				cabecalho.setCodcli( rs.getInt( "CODCLI" ) );
				cabecalho.setCodplanopag( rs.getInt("CODPLANOPAG") );
				cabecalho.setCodvend( rs.getInt( "CODVEND" ) );
				cabecalho.setCodclcomis( rs.getInt( "CODCLCOMIS" ) );
				
			}
			
		}catch (SQLException e) {
			Funcoes.mensagemErro( null, "Não foi possivel encontrar Venda anterior!" );
			e.printStackTrace();
			throw e;
		}
	
		return cabecalho;
	}
	
	public ArrayList<ItemVenda> getItensVenda (Integer codemp, Integer codfilial, Integer codvenda) {
		ArrayList<ItemVenda> itens = new ArrayList<ItemVenda>();
		
		StringBuilder sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CabecalhoVenda cabecalho = null;
		
		try{
	
			sql = new StringBuilder();
			sql.append( "SELECT IV.CODPROD, IV.REFPROD, IV.PRECOITVENDA, IV.QTDITVENDA, IV.PERCDESCITVENDA, IV.CODLOTE " );
			sql.append( "FROM VDITVENDA IV " );
			sql.append( "WHERE IV.CODEMP=? AND IV.CODFILIAL=? AND IV.CODVENDA=? " );
			sql.append( "ORDER BY IV.CODITVENDA ");
			ps = getConn().prepareStatement( sql.toString() );
			int param = 1;
			
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codvenda );
			
			rs = ps.executeQuery();
			
			while (rs.next()) {
				
				ItemVenda item = new ItemVenda();
				item.setCodprod(  rs.getInt( "CODPROD" ) );
				item.setRefProd( getString( rs.getString( "REFPROD" ) ));
				item.setPrecoprod( getBigDecimal( rs.getBigDecimal( "PRECOITVENDA") ) );
				item.setQtdprod( getBigDecimal( rs.getBigDecimal( "QTDITVENDA" ) ) );
				item.setPercprod( getBigDecimal(  rs.getBigDecimal( "PERCDESCITVENDA" ) ) );
				item.setCodlote( getString(  rs.getString( "CODLOTE" ) ) );
				
				itens.add( item );
			}
			
		}catch (SQLException e) {
			Funcoes.mensagemErro( null, "Não foi possivel encontrar os itens da venda anterior!" );
			e.printStackTrace();
		}
		return itens;
	}
	
	public boolean insereItvendaItvenda(int codempvo, int codfilialvo, String tipovendavo, int codvendavo, int codvenda, int coditvenda, BigDecimal qtditvenda ) throws SQLException {
		boolean result = false;
		StringBuilder sql = new StringBuilder("insert into vditvendaitvenda ");
		sql.append(" (id, codemp, codfilial, tipovenda, codvenda, coditvenda, codempvo, codfilialvo, tipovendavo, codvendavo, coditvendavo, qtditvenda ) ");
		sql.append(" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		int param = 1;
		int id = geraSeqId("VDITVENDAITVENDA");
		PreparedStatement ps = getConn().prepareStatement( sql.toString() );
		
		ps.setInt( param++, id);
		ps.setInt( param++, codempvo );  // codemp nova venda o mesmo da original
		ps.setInt( param++, codfilialvo ); // codfilial nova venda  o mesmo da original
		ps.setString( param++, tipovendavo ); // tipo da nova venda o mesmo da original
		ps.setInt( param++, codvenda ); // codvenda da nova venda recebido como parãmetro 
		ps.setInt( param++, coditvenda ); // coditvenda nova venda sequencial 
		ps.setInt( param++, codempvo );  // codemp  venda original
		ps.setInt( param++, codfilialvo ); // codfilial venda original
		ps.setString( param++, tipovendavo ); // codfilial venda original
		ps.setInt( param++, codvendavo ); // codvenda original ;
		ps.setInt( param++, coditvenda ); // coditvenda original ;
		ps.setBigDecimal( param++, getBigDecimal( qtditvenda ));
		
		result = ps.execute();
		ps.close();
		       
		return result;
	}
	
	
	public VdItVendaItVenda getAmarracao(Integer codemp, Integer codfilial, String tipovenda,  Integer codvenda){
		
		StringBuilder sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		VdItVendaItVenda vendaitvenda = null;
		
		try{
	
			sql = new StringBuilder();
			sql.append( "SELECT VD.CODEMP, VD.CODFILIAL, VD.TIPOVENDA, VD.CODVENDA, " );
			sql.append( "VD.CODEMPVO, VD.CODFILIALVO, VD.TIPOVENDAVO, VD.CODVENDAVO " );
			sql.append( "FROM vditvendaitvenda VD " );
			sql.append( "WHERE VD.CODEMP=? AND VD.CODFILIAL=? AND VD.TIPOVENDA=? AND VD.CODVENDA=? " );
			ps = getConn().prepareStatement( sql.toString() );
			int param = 1;
			
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setString( param++, tipovenda);
			ps.setInt( param++, codvenda );
			rs = ps.executeQuery();
			
			if (rs.next()) {
				
				vendaitvenda = new VdItVendaItVenda();
				vendaitvenda.setCodemp( rs.getInt( "CODEMP" ) );
				vendaitvenda.setCodfilial( rs.getInt( "CODFILIAL" ) );
				vendaitvenda.setTipovenda( rs.getString( "TIPOVENDA" ) );
				vendaitvenda.setCodvenda( rs.getInt( "CODVENDA" ) );
				vendaitvenda.setCodempvo( rs.getInt( "CODEMPVO" ) );
				vendaitvenda.setCodfilialvo( rs.getInt( "CODFILIALVO" ) );
				vendaitvenda.setTipovendavo( rs.getString( "TIPOVENDAVO" ) );
				vendaitvenda.setCodvendavo( rs.getInt( "CODVENDAVO" ) );
			}
			
			rs.close();
			ps.close();
			
		}catch (SQLException e) {
			Funcoes.mensagemErro( null, "Não foi possivel encontrar Venda anterior!" );
			e.printStackTrace();
		}
	
		
		
		return vendaitvenda;
		
	}
	
	
	public UpdateVenda getValoresCabecalhoVenda( int codemp, int codfilial, String tipovenda, int codvenda ) {
		
		StringBuilder sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		UpdateVenda valores = null;

		try{
			sql = new StringBuilder();
			sql.append( "select v.vlrprodvenda  ");
			sql.append( ", v.vlrliqvenda ");
			sql.append( ", v.vlradicvenda ");
			sql.append( ", v.vlrdescvenda ");
			sql.append( "from  vdvenda v ");
			sql.append( "where v.codemp=? and v.codfilial=? "); 
			sql.append( "and v.tipovenda=? and v.codvenda=? ");
			ps = getConn().prepareStatement( sql.toString() );
			int param = 1;
			
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setString( param++, tipovenda );
			ps.setInt( param++, codvenda);
			
			rs = ps.executeQuery();
			
			if(rs.next()){
				valores = new UpdateVenda();
				valores.setVlradicvenda( rs.getBigDecimal( "vlradicvenda" ) );
				valores.setVlrdescvenda( rs.getBigDecimal( "vlrdescvenda" ) );
				valores.setVlrliqprodvenda( rs.getBigDecimal( "vlrliqvenda" ) );
				valores.setVlrprodvenda( rs.getBigDecimal( "vlrprodvenda" ) );
			}
			
			getConn().commit();
		
		} catch (SQLException e) {
			Funcoes.mensagemErro( null, "Erro ao realizar update da nota complementarss" );
			e.printStackTrace();
		}
		
		
		return valores;
	}

	
	
	public void updateNotaComplementar( int codempvo, int codfilialvo, String tipovendavo, int codvendavo, int codvenda ) {
	
		StringBuilder sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{
			sql = new StringBuilder();
			sql.append( "select v1.coditvenda coditvenda ");
			sql.append( ", coalesce(v1.qtditvenda,0)-coalesce(v2.qtditvenda,0) qtditvenda ");
			sql.append( ", coalesce(v1.precoitvenda,0)-coalesce(v2.precoitvenda,0) precoitvenda ");
			sql.append( ", coalesce(v1.vlrdescitvenda,0)-coalesce(v2.vlrdescitvenda,0) vlrdescitvenda ");
			sql.append( ", coalesce(v1.vlrbaseicmsitvenda,0)-coalesce(v2.vlrbaseicmsitvenda,0) vlrbaseicmsitvenda ");
			sql.append( ", coalesce(v1.vlricmsitvenda,0)-coalesce(v2.vlricmsitvenda,0) vlricmsitvenda ");
			sql.append( ", coalesce(v1.vlrbaseipiitvenda,0)-coalesce(v2.vlrbaseipiitvenda,0) vlrbaseipiitvenda ");
			sql.append( ", coalesce(v1.vlripiitvenda,0)-coalesce(v2.vlripiitvenda,0) vlripiitvenda ");
			sql.append( ", coalesce(v1.vlrliqitvenda,0)-coalesce(v2.vlrliqitvenda,0) vlrliqitvenda ");
			sql.append( ", coalesce(v1.vlrcomisitvenda,0)-coalesce(v2.vlrcomisitvenda,0) vlrcomisitvenda ");
			sql.append( ", coalesce(v1.vlradicitvenda,0)-coalesce(v2.vlradicitvenda,0) vlradicitvenda ");
			sql.append( ", coalesce(v1.vlrissitvenda,0)-coalesce(v2.vlrissitvenda,0) vlrissitvenda ");
			sql.append( ", coalesce(v1.vlrfreteitvenda,0)-coalesce(v2.vlrfreteitvenda,0) vlrfreteitvenda "); 
			sql.append( ", coalesce(v1.vlrproditvenda,0)-coalesce(v2.vlrproditvenda,0) vlrproditvenda ");
			sql.append( ", coalesce(v1.vlrisentasitvenda,0)-coalesce(v2.vlrisentasitvenda,0) vlrisentasitvenda ");
			sql.append( ", coalesce(v1.vlroutrasitvenda,0)-coalesce(v2.vlroutrasitvenda,0) vlroutrasitvenda ");
			sql.append( ", coalesce(v1.vlrbaseissitvenda,0)-coalesce(v2.vlrbaseissitvenda,0) vlrbaseissitvenda ");
			sql.append( ", coalesce(v1.vlrbaseicmsbrutitvenda,0)-coalesce(v2.vlrbaseicmsbrutitvenda,0) vlrbaseicmsbrutitvenda ");
			sql.append( ", coalesce(v1.vlrbaseicmsstitvenda,0)-coalesce(v2.vlrbaseicmsstitvenda,0) vlrbaseicmsstitvenda ");
			sql.append( ", coalesce(v1.vlricmsstitvenda,0)-coalesce(v2.vlricmsstitvenda,0) vlricmsstitvenda ");
			sql.append( ", coalesce(v1.vlrbasecomisitvenda,0)-coalesce(v2.vlrbasecomisitvenda,0) vlrbasecomisitvenda ");
			sql.append( "from vditvendaitvenda vv,  vditvenda v1, vditvenda v2 ");
			sql.append( "where v1.codemp=vv.codemp and v1.codfilial=vv.codfilial "); 
			sql.append( "and v1.tipovenda=vv.tipovenda and v1.codvenda=vv.codvenda and v1.coditvenda=vv.coditvenda ");
			sql.append( "and v2.codemp=vv.codempvo and v2.codfilial=vv.codfilialvo "); 
			sql.append( "and v2.tipovenda=vv.tipovendavo and v2.codvenda=vv.codvendavo and v2.coditvenda=vv.coditvendavo ");
			sql.append( "and vv.codemp=? and vv.codfilial=? and vv.tipovenda=? and vv.codvenda=? "); 
			sql.append( "and vv.codempvo=? and vv.codfilialvo=? and vv.tipovendavo=? and vv.codvendavo=? ");
			sql.append( "order by vv.codvenda, vv.coditvenda ");
			ps = getConn().prepareStatement( sql.toString() );
			int param = 1;
			
			ps.setInt( param++, codempvo );
			ps.setInt( param++, codfilialvo );
			ps.setString( param++, tipovendavo );
			ps.setInt( param++, codvenda);
			ps.setInt( param++, codempvo );
			ps.setInt( param++, codfilialvo );
			ps.setString( param++, tipovendavo );
			ps.setInt( param++, codvendavo);
			
			rs = ps.executeQuery();
			
			while(rs.next()){
				sql = new StringBuilder();
				sql.append( "update vditvenda v1 set ");  
				sql.append( "v1.qtditvenda=? ");
				sql.append( ", v1.precoitvenda=? ");
				sql.append( ", v1.vlrdescitvenda=? ");
				sql.append( ", v1.vlrbaseicmsitvenda=? ");  
				sql.append( ", v1.vlricmsitvenda=? ");  
				sql.append( ", v1.vlrbaseipiitvenda=? ");  
				sql.append( ", v1.vlripiitvenda=? ");  
				sql.append( ", v1.vlrliqitvenda=? ");  
				sql.append( ", v1.vlrcomisitvenda=? ");  
				sql.append( ", v1.vlradicitvenda=? ");  
				sql.append( ", v1.vlrissitvenda=? ");  
				sql.append( ", v1.vlrfreteitvenda=? ");  
				sql.append( ", v1.vlrproditvenda=? ");  
				sql.append( ", v1.vlrisentasitvenda=? ");  
				sql.append( ", v1.vlroutrasitvenda=? ");  
				sql.append( ", v1.vlrbaseissitvenda=? ");  
				sql.append( ", v1.vlrbaseicmsbrutitvenda=? ");  
				sql.append( ", v1.vlrbaseicmsstitvenda=? ");  
				sql.append( ", v1.vlricmsstitvenda=? ");  
				sql.append( ", v1.vlrbasecomisitvenda=? ");  
				sql.append( "where v1.codemp=? and v1.codfilial=? ");   
				sql.append( "and v1.tipovenda=? and v1.codvenda=? and v1.coditvenda=? ");
				PreparedStatement ps2 = getConn().prepareStatement( sql.toString() );
				int param2 = 1;
				
				ps2.setBigDecimal( param2++, rs.getBigDecimal( "qtditvenda" ) );
				ps2.setBigDecimal( param2++, rs.getBigDecimal( "precoitvenda" ) );
				ps2.setBigDecimal( param2++, rs.getBigDecimal( "vlrdescitvenda" ) );
				ps2.setBigDecimal( param2++, rs.getBigDecimal( "vlrbaseicmsitvenda" ) );
				ps2.setBigDecimal( param2++, rs.getBigDecimal( "vlricmsitvenda" ) );
				ps2.setBigDecimal( param2++, rs.getBigDecimal( "vlrbaseipiitvenda" ) );
				ps2.setBigDecimal( param2++, rs.getBigDecimal( "vlripiitvenda" ) );
				ps2.setBigDecimal( param2++, rs.getBigDecimal( "vlrliqitvenda" ) );
				ps2.setBigDecimal( param2++, rs.getBigDecimal( "vlrcomisitvenda" ) );
				ps2.setBigDecimal( param2++, rs.getBigDecimal( "vlradicitvenda" ) );
				ps2.setBigDecimal( param2++, rs.getBigDecimal( "vlrissitvenda" ) );
				ps2.setBigDecimal( param2++, rs.getBigDecimal( "vlrfreteitvenda" ) );
				ps2.setBigDecimal( param2++, rs.getBigDecimal( "vlrproditvenda" ) );
				ps2.setBigDecimal( param2++, rs.getBigDecimal( "vlrisentasitvenda" ) );
				ps2.setBigDecimal( param2++, rs.getBigDecimal( "vlroutrasitvenda" ) );
				ps2.setBigDecimal( param2++, rs.getBigDecimal( "vlrbaseissitvenda" ) );
				ps2.setBigDecimal( param2++, rs.getBigDecimal( "vlrbaseicmsbrutitvenda" ) );
				ps2.setBigDecimal( param2++, rs.getBigDecimal( "vlrbaseicmsstitvenda" ) );
				ps2.setBigDecimal( param2++, rs.getBigDecimal( "vlricmsstitvenda" ) );
				ps2.setBigDecimal( param2++, rs.getBigDecimal( "vlrbasecomisitvenda" ) );
				
				
				ps2.setInt( param2++, codempvo );
				ps2.setInt( param2++, codfilialvo );
				ps2.setString( param2++, tipovendavo );
				ps2.setInt( param2++, codvenda);
				ps2.setInt( param2++, rs.getInt( "coditvenda" )  );
				ps2.execute();
				
			}
			
			getConn().commit();
		
		} catch (SQLException e) {
			Funcoes.mensagemErro( null, "Erro ao realizar update da nota complementar" );
			e.printStackTrace();
		}
	}

	
	public Integer geraSeqId(String tabela) throws SQLException{
		
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null; 
		Integer id = 0;
		ps = getConn().prepareStatement( "select biseq from sgsequence_idsp(?)" );
		ps.setString( 1, tabela );
		
		rs = ps.executeQuery();
		if (rs.next()) {
			id = rs.getInt( "biseq" ); 
		}

		return id;
	}
	
}