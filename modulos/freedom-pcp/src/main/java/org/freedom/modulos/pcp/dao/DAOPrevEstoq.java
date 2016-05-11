/**
 * @version 22/04/2013 <BR>
 * @author Setpoint Informática Ltda./Bruno Nascimento<BR>
 *  
 *          Projeto: Freedom <BR>
 * 
 *          Pacote: org.freedom.modulos.pcp.dao <BR>
 *          Classe: @(#)DAOPull.java <BR>
 * 
 *                  Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                  modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                  na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                  Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                  sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                  Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                  Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                  de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                  DAO que possui a responsabilidade sobre o Sistema de produção Puxada ( Pull System ).
 * 
 */


package org.freedom.modulos.pcp.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import org.freedom.infra.dao.AbstractDAO;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.exceptions.ExceptionCarregaDados;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.pcp.business.object.PrevEstoqLog;

import com.sun.org.apache.bcel.internal.generic.Type;

public class DAOPrevEstoq extends AbstractDAO {

	public DAOPrevEstoq( DbConnection cn) {
		super( cn );
	}

	public boolean comRef() throws SQLException {

		boolean bRetorno = false;
		String sSQL = "SELECT USAREFPROD FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
		PreparedStatement ps = null;
		ResultSet rs = null;


		ps = Aplicativo.getInstace().getConexao().prepareStatement( sSQL );

		ps.setInt( 1, Aplicativo.iCodEmp );
		ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

		rs = ps.executeQuery();

		if ( rs.next() )
			if ( rs.getString( "UsaRefProd" ).trim().equals( "S" ) )
				bRetorno = true;

		ps.close();
		rs.close();
		return bRetorno;
	}

	public String formatFiltros(ArrayList<String> filtros) {
		String filtroFormatado = null;
		if (filtros != null) {
			filtroFormatado = "and pd.tipoprod in (";

			for ( int i = 0; i < filtros.size(); i++ ) {
				filtroFormatado += "'" + filtros.get(i) + "'";
				if (i != filtros.size() - 1) 
					filtroFormatado += ",";	
			}

			filtroFormatado += ") ";
		}

		return filtroFormatado;
	}

	public Vector<Vector<Object>> carregar(Integer codemp, Integer codfilial, Date dataini, Date datafim, String prodSemMovimento, ArrayList<String> filtros ) throws ExceptionCarregaDados{

		PreparedStatement ps = null;
		ResultSet rs = null;
		String mensagemErro = "";
		StringBuilder sql = null;
		String sWhere = null;
		Vector<Object> vVals = null;
		boolean bOrc = false;
		boolean bConv = false;
		int iCod = -1;
		Vector<Vector<Object>> vector = null;
		int param = 1;
		String sfiltros = formatFiltros( filtros );
		System.out.println(sfiltros);

		try {

			sql = new StringBuilder();

			sql.append("select pd.codemp, pd.codfilial, pd.codprod, pd.refprod, pd.descprod ");
			sql.append(", sum(iv.qtditvenda) / 150 mediavendas ");
			sql.append(", pd.prazorepo, pd.precobaseprod, pd.sldprod, pd.qtdminprod, pd.qtdmaxprod ");
			sql.append("from eqproduto pd ");
			sql.append("inner join vditvenda iv on ");
			sql.append("iv.codemppd=pd.codemp and iv.codfilialpd=pd.codfilial and iv.codprod=pd.codprod ");
			if ("S".equals(prodSemMovimento))
				sql.append("left outer join vdvenda v on ");
			else
				sql.append("inner join vdvenda v on ");

			sql.append("v.codemp=iv.codemp and v.codfilial=iv.codfilial and v.tipovenda=iv.tipovenda and v.codvenda=iv.codvenda ");
			sql.append("and v.dtemitvenda between ? and ? ");
			sql.append("where pd.codemp=? and pd.codfilial=? ");

			if (sfiltros != null)
				sql.append( sfiltros );

			sql.append(" group by pd.codemp, pd.codfilial, pd.codprod, pd.refprod, pd.descprod ");
			sql.append(", pd.prazorepo, pd.precobaseprod, pd.sldprod, pd.qtdminprod, pd.qtdmaxprod ");

			ps = getConn().prepareStatement( sql.toString() );
			ps.setDate( param++, Funcoes.dateToSQLDate(dataini));
			ps.setDate( param++, Funcoes.dateToSQLDate(datafim));
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial);
			rs = ps.executeQuery();

			vector =  new Vector<Vector<Object>>();

			while (rs.next()) {
				vVals = new Vector<Object>();
				vVals.addElement(new Boolean( true ) );
				vVals.addElement(getInteger(rs.getInt("codprod")));
				vVals.addElement(getString(rs.getString("refprod")));
				vVals.addElement(getString(rs.getString("descprod")));
				vVals.addElement(getBigDecimal(rs.getBigDecimal("mediavendas")));
				vVals.addElement(getInteger(rs.getInt("prazorepo")));
				vVals.addElement(getBigDecimal(rs.getBigDecimal("precobaseprod")));
				vVals.addElement(getBigDecimal(rs.getBigDecimal("sldprod")));
				vVals.addElement(getBigDecimal(rs.getBigDecimal("qtdminprod")));
				vVals.addElement(getBigDecimal(rs.getBigDecimal("qtdmaxprod")));
				vVals.addElement(new BigDecimal(0));
				vVals.addElement(new BigDecimal(0));
				vVals.addElement(new BigDecimal(0));
				vector.add(vVals);
			}

			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			mensagemErro = "Erro ao carregar previsão de estoque!\n";
			vector = null;
			throw new ExceptionCarregaDados(mensagemErro);
		}

		ps = null;
		rs = null;
		sql = null;
		sWhere = null;
		vVals = null;

		return vector;
	}

	public void gravaLog(PrevEstoqLog log) throws SQLException {
		
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		int param = 1;
		int id = geraSeqId("EQPREVESTOQLOG");
		
		sql.append( "insert into eqprevestoqlog (id, codemp, codfilial, dtini, dtfim, codempgp, codfilialgp, codgrup, codemppd, codfilialpd, codprod, ordem, mercadoria_revenda," );
		sql.append( "materia_prima, em_processo, outros, sub_produto, equipamento, material_consumo, produto_intermed, produto_acabado, embalagem, outros_insumos" );
		sql.append( ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," );
		sql.append( "?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " );
		
		ps = getConn().prepareStatement( sql.toString() );
		ps.setInt( param++, id);
		ps.setInt( param++, log.getCodemp());
		ps.setInt( param++, log.getCodfilial());
		ps.setDate( param++, Funcoes.dateToSQLDate(log.getDtini()));
		ps.setDate( param++, Funcoes.dateToSQLDate(log.getDtfim()));

		if (log.getCodgrup().length() > 0) {
			ps.setInt( param++, log.getCodempgp());
			ps.setInt( param++, log.getCodfilialgp());
			ps.setString( param++, log.getCodgrup());
		} else {
			ps.setNull( param++, Types.INTEGER );
			ps.setNull( param++, Types.INTEGER );
			ps.setNull( param++, Types.CHAR );
		}
		
		if (log.getCodprod() > 0) {
			ps.setInt( param++, log.getCodemppd());
			ps.setInt( param++, log.getCodfilialpd());
			ps.setInt( param++, log.getCodprod());
		} else {
			ps.setNull( param++, Types.INTEGER );
			ps.setNull( param++, Types.INTEGER );
			ps.setNull( param++, Types.INTEGER );
		}
		
		ps.setString( param++, log.getOrdem() );
		ps.setString( param++, log.getMercadoria_revenda());
		ps.setString( param++, log.getMateria_prima());
		ps.setString( param++, log.getEm_processo());
		ps.setString( param++, log.getOutros());
		ps.setString( param++, log.getSub_produto());
		ps.setString( param++, log.getEquipamento());
		ps.setString( param++, log.getMaterial_consumo());
		ps.setString( param++, log.getProduto_intermed());
		ps.setString( param++, log.getProduto_acabado());
		ps.setString( param++, log.getEmbalagem());
		ps.setString( param++, log.getOutros_insumos());
		ps.execute();
		
		getConn().commit();
	}
	
	
	

	public Vector<Vector<Object>> carregarLog(Integer codemp, Integer codfilial) throws ExceptionCarregaDados {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String mensagemErro = "";
		StringBuilder sql = null;
		String sWhere = null;
		Vector<Object> vVals = null;
		boolean bOrc = false;
		boolean bConv = false;
		int iCod = -1;
		Vector<Vector<Object>> vector = null;
		int param = 1;

		try {

			sql = new StringBuilder();

			sql.append("select dtini, dtfim, codgrup, codprod, ordem, mercadoria_revenda, materia_prima, em_processo, outros, ");
			sql.append("sub_produto, equipamento, material_consumo, produto_intermed, produto_acabado, embalagem, outros_insumos, idusuins ");
			sql.append("from eqprevestoqlog where codemp=? and codfilial=? ");

			ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial);
			rs = ps.executeQuery();

			vector =  new Vector<Vector<Object>>();

			while (rs.next()) {
				vVals = new Vector<Object>();
				vVals.addElement(Funcoes.sqlDateToDate(rs.getDate("DTINI")));
				vVals.addElement(Funcoes.sqlDateToDate(rs.getDate("DTFIM")));
				vVals.addElement(getInteger(rs.getInt("codgrup")));
				vVals.addElement(getInteger(rs.getInt("codprod")));
				vVals.addElement(new Boolean( "S".equals(rs.getString("ordem"))));
				vVals.addElement(new Boolean( "S".equals(rs.getString("mercadoria_revenda"))));
				vVals.addElement(new Boolean( "S".equals(rs.getString("materia_prima"))));
				vVals.addElement(new Boolean( "S".equals(rs.getString("em_processo"))));
				vVals.addElement(new Boolean( "S".equals(rs.getString("outros"))));
				vVals.addElement(new Boolean( "S".equals(rs.getString("sub_produto"))));
				vVals.addElement(new Boolean( "S".equals(rs.getString("equipamento"))));
				vVals.addElement(new Boolean( "S".equals(rs.getString("material_consumo"))));
				vVals.addElement(new Boolean( "S".equals(rs.getString("produto_intermed"))));
				vVals.addElement(new Boolean( "S".equals(rs.getString("produto_acabado"))));
				vVals.addElement(new Boolean( "S".equals(rs.getString("embalagem"))));
				vVals.addElement(new Boolean( "S".equals(rs.getString("outros_insumos"))));
				vVals.addElement(getString(rs.getString("idusuins")));
				vector.add(vVals);
			}

			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			mensagemErro = "Erro ao carregar previsão de estoque!\n";
			vector = null;
			throw new ExceptionCarregaDados(mensagemErro);
		}

		ps = null;
		rs = null;
		sql = null;
		sWhere = null;
		vVals = null;

		return vector;
	}
	
	public Integer geraSeqId (String tabela) throws SQLException{

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


