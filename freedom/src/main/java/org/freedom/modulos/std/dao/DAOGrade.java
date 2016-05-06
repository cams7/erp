package org.freedom.modulos.std.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JProgressBar;

import org.freedom.infra.dao.AbstractDAO;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.std.business.object.UpdateVenda;
import org.freedom.modulos.std.view.frame.crud.special.FGrade.TAB_GRADE;


public class DAOGrade extends AbstractDAO {
	
	public DAOGrade( DbConnection cn) {
		super( cn );
	}
	
	public String executeProcedure ( int codemppd, int codfilialpd, int codprod, int codempmg, int codfilialmg, int codmodg
			, JTablePad tab, JProgressBar pbGrade) throws SQLException {

		StringBuilder sql =  new StringBuilder("EXECUTE PROCEDURE EQADICPRODUTOSP(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		PreparedStatement ps = null;
		String erros = "";
		for (int i = 0; i < tab.getNumLinhas(); i++) {
			ps = getConn().prepareStatement( sql.toString() );
			if (((Boolean) tab.getValor(i, TAB_GRADE.ADICPROD.ordinal())).booleanValue()) {
				int param = 1;
				ps.setInt( param++, codemppd );
				ps.setInt( param++, codfilialpd );
				ps.setInt( param++, codprod );
				ps.setString( param++, ( (String) tab.getValor( i, TAB_GRADE.DESCPROD.ordinal() ) ).trim() );
				ps.setString( param++, "" );
				ps.setString( param++, ( (String) tab.getValor( i, TAB_GRADE.REFPROD.ordinal() ) ).trim() );
				ps.setString( param++, ( (String) tab.getValor( i, TAB_GRADE.CODFABPROD.ordinal() ) ).trim() );
				ps.setString( param++, ( (String) tab.getValor( i, TAB_GRADE.CODBARPROD.ordinal() ) ).trim() );
				ps.setInt( param++, codempmg);
				ps.setInt( param++, codfilialmg );
				ps.setInt( param++, codmodg );
				ps.setString( param++, ( (String) tab.getValor( i, TAB_GRADE.DESCCOMPL.ordinal() ) ).trim() );
				ps.setBigDecimal( param++, ((BigDecimal) tab.getValor( i, TAB_GRADE.PRECOBASE.ordinal() ) ) );
				ps.setString( param++, ((Boolean) tab.getValor( i, TAB_GRADE.ATUALIZAPROD.ordinal() ) == true ? "S" : "N" ) );
				try {
					ps.execute();
				} catch (SQLException exception) {
					erros = erros + "Desc.:" + tab.getValor( i, 1 ) + " Ref.:" + tab.getValor( i, 2 ) + "\n" + exception.getMessage() + "\n";
				}
				pbGrade.setValue( i + 1 );
				getConn().commit();
			}
			getConn().commit();
		}
		return erros;
	}

	public ResultSet getMontaTab( Integer codemp, Integer codfilial, Integer codmodg ) throws SQLException {

		StringBuilder sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		UpdateVenda valores = null;

		sql = new StringBuilder();
		sql.append("SELECT M.CODPROD,I.CODMODG,I.CODITMODG,I.CODVARG,V.DESCVARG,");
		sql.append("I.DESCITMODG,I.REFITMODG,I.CODFABITMODG,I.CODBARITMODG, M.DESCCOMPPRODMODG, I.DESCCOMPITMODG, I.PRECOITVARG ");
		sql.append("FROM EQITMODGRADE I, EQVARGRADE V, EQMODGRADE M WHERE ");
		sql.append("M.CODEMP = ? AND M.CODFILIAL = ? AND I.CODMODG=?");
		sql.append(" AND V.CODVARG = I.CODVARG AND M.CODMODG=I.CODMODG ");
		sql.append("ORDER BY I.CODMODG,I.ORDEMITMODG,I.CODVARG ");
		ps = getConn().prepareStatement( sql.toString() );
		int param = 1;

		ps.setInt( param++, codemp );
		ps.setInt( param++, codfilial );
		ps.setInt( param++, codmodg );

		rs = ps.executeQuery();

		return rs;
	}
	
	//Método com o intuito de buscar o preço base antigo do produto.
	public Map<String, Object> getPrecoBaseAnt( Integer codemp, Integer codfilial, String refprod ) throws SQLException {

		StringBuilder sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		BigDecimal precoBaseAnt = new BigDecimal( 0 );
		Map<String, Object > result = new HashMap<String, Object>();
		result.put("CADASTRADO", false);
		result.put("PRECOBASEPROD", precoBaseAnt);
		
		
		sql = new StringBuilder();
		sql.append("SELECT P.PRECOBASEPROD ");
		sql.append("FROM EQPRODUTO P WHERE ");
		sql.append("P.CODEMP = ? AND P.CODFILIAL = ? AND P.REFPROD = ?");
		ps = getConn().prepareStatement( sql.toString() );
		int param = 1;

		ps.setInt( param++, codemp );
		ps.setInt( param++, codfilial );
		ps.setString( param++, refprod );
		rs = ps.executeQuery();

		if (rs.next()) {
			result.put("CADASTRADO", true);
			result.put("PRECOBASEPROD", getBigDecimal(rs.getBigDecimal( "PRECOBASEPROD")));
			//precoBaseAnt = getBigDecimal( rs.getBigDecimal( "PRECOBASEPROD" ) );
		}
		
		rs.close();
		ps.close();
		
		return result;
	}

	public Integer copiaModGrade( int codemp, int codfilial, Integer codmodg ) {
		int param;
		StringBuilder sql;
		StringBuilder sqlItens;
		PreparedStatement ps;
		int novoCodModG = getMaxId();
		
		try {
			param = 1;
			sql = new StringBuilder();
			sql.append( "insert into eqmodgrade ( codemp, codfilial, codmodg,  codemppd, codfilialpd, codprod, "); 
			sql.append( "descmodg, descprodmodg, desccompprodmodg, refmodg, codfabmodg, codbarmodg) ");
			sql.append( "select codemp, codfilial, ");
			sql.append( novoCodModG );
			sql.append( ",codemppd, codfilialpd, codprod, descmodg, "); 
			sql.append( "descprodmodg, desccompprodmodg, refmodg, codfabmodg, codbarmodg ");
			sql.append( "from eqmodgrade where codemp=? and codfilial=? and codmodg=?");
			ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codmodg );
			
			ps.execute();
			ps.close();
		} catch (Exception e) {
			Funcoes.mensagemErro( null, "Erro ao copiar cabeçalho do modelo de grade!!!" );
			e.printStackTrace();
		}
		
		try {
			param = 1;
			sqlItens = new StringBuilder();
			sqlItens.append( "insert into eqitmodgrade (codemp, codfilial, codmodg, coditmodg, ordemitmodg, refitmodg, codempvg ");
			sqlItens.append( ", codfilialvg, codvarg, codfabitmodg, codbaritmodg, descitmodg, desccompitmodg) ");
			sqlItens.append( "select codemp, codfilial, ");
			sqlItens.append( novoCodModG );
			sqlItens.append(", coditmodg, ordemitmodg, refitmodg, codempvg ");
			sqlItens.append( ", codfilialvg, codvarg, codfabitmodg, codbaritmodg, descitmodg, desccompitmodg ");
			sqlItens.append( "from eqitmodgrade where codemp=? and codfilial=? and codmodg=?");
			ps = getConn().prepareStatement( sqlItens.toString() );
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codmodg );
			
			ps.execute();
			ps.close();
		} catch (Exception e) {
			Funcoes.mensagemErro( null, "Erro ao copiar itens do modelo de grade!!!" );
			e.printStackTrace();
		}	
		
		return novoCodModG;
	}
	public int getMaxId() {
		String sql;
		PreparedStatement ps;
		ResultSet rs;
		int novoCodModG = 0;
		try {
			sql = "select count(*) codmod from eqmodgrade";
			ps = getConn().prepareStatement( sql );
			rs = ps.executeQuery();
			
			if (rs.next()) {
				novoCodModG = rs.getInt( "codmod" ) + 1;
			}
			
		} catch (Exception e) {
			Funcoes.mensagemErro( null, "Erro buscar max id!!!" );
			e.printStackTrace();
		}	
		return novoCodModG;
	}
	
	
	//Busca dados preferênciais.
	public Map<String, Object> getPref() {
		StringBuilder sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			sql = new StringBuilder();
			sql.append( "select calcprecog from sgprefere1 where codemp=? and codfilial=?" );
			ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			rs = ps.executeQuery();
			
			if (rs.next()) {
				result.put( "calcprecog", "S".equals(rs.getString( "calcprecog" )) ? true : false );
			}

		} catch (SQLException e) {
			Funcoes.mensagemErro( null, "Erro ao buscar informações preferênciais." );
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int calculaCombinacoes(int codemp, int codfilial, int codmodg) {
          int result = 1;
          StringBuilder sql = new StringBuilder();
          try {
	          sql.append( "select codvarg, count(*) qtd from eqitmodgrade " );
	          sql.append( "where codemp=? and codfilial=? and codmodg=? ");
	          sql.append( "group by codvarg ");
	          int param = 1;
	          PreparedStatement ps = getConn().prepareStatement( sql.toString() );
	          ps.setInt( param++, codemp );
	          ps.setInt( param++, codfilial );  
	          ps.setInt( param++, codmodg );  
	          
	          ResultSet rs = ps.executeQuery();
	          while ( rs.next() ) {
	                  result *= rs.getInt( "qtd" ); 
	          }         
	          rs.close();
	          ps.close();
	          getConn().commit();
          } catch (Exception e) {
  			Funcoes.mensagemErro( null, "Erro ao verificar Possibilidades!!!" );
  			e.printStackTrace();
  		}	
        return result;
          
	}
}


