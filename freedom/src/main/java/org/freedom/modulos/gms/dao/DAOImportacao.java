package org.freedom.modulos.gms.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.freedom.infra.dao.AbstractDAO;
import org.freedom.infra.functions.ConversionFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.gms.view.frame.crud.detail.FImportacao;
import org.freedom.modulos.gms.view.frame.crud.detail.FImportacao.GRID_ADICAO;
import org.freedom.modulos.gms.view.frame.crud.plain.FBuscaCpCompl.enum_itcompra;


public class DAOImportacao extends AbstractDAO {
		
	public DAOImportacao( DbConnection cn ) {
		super(cn);		
	}
	
	public PreparedStatement getStatementItensImportacao(String utilizatbcalcca) {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;

		try {
			sql.append( "select " );

			sql.append( "ii.coditimp, ii.codemppd, ii.codfilialpd, ii.codprod, ii.refprod, ii.qtd, pd.codalmox, " );
			// Remoção do imposto de importação do valor do produto
			//sql.append( "(ii.vlrad + ii.vlrii + ii.vlripi + ii.vlrpis + ii.vlrcofins + ii.vlrtxsiscomex ) vlrliqitcompra, (ii.vlrad + ii.vlrii ) vlrproditcompra,   ( (ii.vlrad + ii.vlrii ) / qtd) precoitcompra, " );
			sql.append( "case when i.tipoimp='O' then ");
			sql.append( "cast(cast(cast(ii.vlrad + ii.vlrii + ii.vlripi + ii.vlrpis + ii.vlrcofins + ii.vlrtxsiscomex ");
			sql.append( " + (case when lf.adicicmstotnota='S' then ii.vlricms else 0.00 end)" );
			sql.append( "  as decimal(15,4) ) / ii.qtd as decimal(15,4)) * ii.qtd as decimal(15,4)) ");
			sql.append( " else cast( ii.vlrcompl/ii.qtd as decimal(15,4) ) * ii.qtd  end ");
			sql.append( " vlrliqitcompra ");
			// valor total dos produtos com cálculo para evitar dízima periódica
			sql.append( ", case when i.tipoimp='O' then ");
			sql.append("cast(cast( cast(ii.vlrad as decimal(15,2)) / cast(ii.qtd as decimal(15,5)) as decimal(15,4)) * cast(ii.qtd as decimal(15,4)) as decimal(15,2)) ");
			sql.append( " else cast( ii.vlrcompl/ii.qtd as decimal(15,4) ) * ii.qtd  end ");
			sql.append(" vlrproditcompra ");
			// preço do ítem para evitar dízima periódica
			sql.append(" , case when i.tipoimp='O' then "); 
			sql.append( " cast(cast( cast(ii.vlrad as decimal(15,2)) / cast(ii.qtd as decimal(15,5)) as decimal(15,4)) * cast(ii.qtd as decimal(15,4)) as decimal(15,2)) / cast(ii.qtd as decimal(15,5) ) ");
			sql.append( " else cast( cast( ii.vlrcompl/ii.qtd as decimal(15,4) ) * ii.qtd as decimal(15,5) ) / ii.qtd  end ");
			sql.append( " precoitcompra " );
			
			// Depois da inserção de parâmetro para adicionar ICMS no total da nota ajustar a linha abaixo.
			//sql.append( "(ii.vlrad + ii.vlrii + ii.vlripi + ii.vlrpis + ii.vlrcofins + ii.vlrtxsiscomex + ii.vlricms ) vlrliqitcompra, (ii.vlrad) vlrproditcompra,   ( (ii.vlrad ) / qtd) precoitcompra, " );
			sql.append( " , case when i.tipoimp='O' then ii.vlrbaseicms else cast(0 as decimal(15,5)) end "); 
			sql.append( " vlrbaseicms " );

			sql.append( ", case when i.tipoimp='O' then ii.aliqicmsuf else cast(0 as decimal(15,5)) end aliqicmsuf ");
			sql.append( ", case when i.tipoimp='O' then ii.vlricms - coalesce(ii.vlricmsdiferido,0) else cast(0 as decimal(15,5)) end vlricmsitcompra ");
			sql.append( ", case when i.tipoimp='O' then ii.vlrfrete  else cast(0 as decimal(15,5)) end vlrfreteitcompra " );
			sql.append( ", case when i.tipoimp='O' then (ii.vlrad + ii.vlrii) else cast(0 as decimal(15,5)) end vlrbaseipiitcompra" );
			sql.append( ", case when i.tipoimp='O' then ii.aliqipi else cast(0 as decimal(15,5)) end aliqipi ");
			sql.append( ", case when i.tipoimp='O' then ii.vlripi else cast(0 as decimal(15,5)) end vlripi ");
			sql.append( ", ii.codfisc, ii.coditfisc " );
			//sql.append( ", case when i.tipoimp='O' then ");
			sql.append( ",( select first 1 codadic from cpimportacaoadic where codemp=ii.codemp and codfilial=ii.codfilial and codimp=ii.codimp and codncm=ii.codncm ) nadicao ");
			sql.append(", ii.seqadic, " );
			sql.append( "case when i.tipoimp='O' then (ii.vlrpis + ii.vlrcofins + ii.vlrtxsiscomex) ");
			sql.append( "else cast( 0 as decimal(15,5)) end ");
			sql.append( "vlradicitcompra " );
			sql.append( ", case when i.tipoimp='O' then ii.aliqpis else cast(0 as decimal(15,5)) end aliqpis ");
			sql.append( ", case when i.tipoimp='O' then ii.vlrpis/(ii.aliqpis/100.00) else cast(0 as decimal(15,5)) end vlrbasepis	");
			sql.append( ", case when i.tipoimp='O' then ii.vlrpis else cast(0 as decimal(15,5)) end vlrpis " );
			sql.append( ", lf.codempsp	, lf.codfilialsp	, lf.codsittribpis	, lf.impsittribpis " );
			sql.append( ", case when i.tipoimp='O' then ii.aliqcofins else cast(0 as decimal(15,5)) end aliqcofins ");
			sql.append( ", case when i.tipoimp='O' then ii.vlrcofins/(ii.aliqcofins/100.00) else cast(0 as decimal(15,5)) end vlrbasecofins ");
			sql.append( ", case when i.tipoimp='O' then ii.vlrcofins else cast(0 as decimal(15,5)) end vlrcofins " );
			sql.append( ", lf.codempsc	, lf.codfilialsc	, lf.codsittribcof	, lf.impsittribcof " );
			sql.append( ", lf.modbcicms	, lf.redfisc		, lf.origfisc	, lf.codtrattrib, lf.codempsi, lf.codfilialsi, lf.codsittribipi, lf.impsittribipi " );
			sql.append( ", case when i.tipoimp='O' then ii.vlrad  else cast(0 as decimal(15,5)) end vlrbaseii ");
			sql.append( ", case when i.tipoimp='O' then ii.aliqii else cast(0 as decimal(15,5)) end aliqii ");
			sql.append( ", case when i.tipoimp='O' then ii.vlrii else cast(0 as decimal(15,5)) end vlrii " );
			sql.append( ", case when i.tipoimp='O' then ii.vlricmsdiferido else cast(0 as decimal(15,5)) end vlricmsdiferido ");
			sql.append( ", case when i.tipoimp='O' then ii.vlricmsrecolhimento else cast(0 as decimal(15,5)) end vlricmsrecolhimento ");
			sql.append( ", case when i.tipoimp='O' then ii.vlricmscredpresum else cast(0 as decimal(15,5)) end vlricmscredpresum ");
			
			// Colocar valor presumido
			sql.append(" , case when i.tipoimp='O' then "); 
			if("S".equals( utilizatbcalcca )){
				sql.append(" (select vlrcusto from lfcalccustosp01( lf.codempcc, lf.codfilialcc, lf.codcalc, ii.qtd, ii.vlrad, ii.vlricms" );
				sql.append(", ii.vlripi, ii.vlrpis, ii.vlrcofins, 0, 0, ii.vlrii, 0, ii.vlrtxsiscomex, ii.vlricmsdiferido, ii.vlricmscredpresum, ii.vlrcompl)) " );
			} else {
				sql.append(" ( (ii.vlrad + ii.vlrii + ii.vlripi + ii.vlrpis + ii.vlrcofins + ii.vlrtxsiscomex + ii.vlrcompl ) - ii.vlripi - (ii.vlricms - coalesce(ii.vlricmsdiferido,0) )  ) / (case when ii.qtd is null or ii.qtd=0 then 1 else ii.qtd end) " );
			}
			sql.append( " else cast( cast( ii.vlrcompl/ii.qtd as decimal(15,4) ) * ii.qtd as decimal(15,5) ) / ii.qtd  end custoitcompra ");
			sql.append( ", lf.adicicmstotnota ");
			sql.append( ", case when i.tipoimp='O' then ii.vlritdespad else cast(0 as decimal(15,5)) end vlritdespad ");
			sql.append( ", i.tipoimp, ii.vlrcompl, ii.vlrtxsiscomex ");
			sql.append( "from eqproduto pd,  cpimportacao i, cpitimportacao ii " );
			sql.append( "left outer join lfitclfiscal lf on lf.codemp=ii.codempcf and lf.codfilial=ii.codfilialcf and lf.codfisc=ii.codfisc and lf.coditfisc=ii.coditfisc " );
			sql.append( "where " );
			sql.append( "pd.codemp=ii.codemppd and pd.codfilial=ii.codfilialpd and pd.codprod=ii.codprod " );
			sql.append( "and i.codemp=ii.codemp and i.codfilial=ii.codfilial and i.codimp=ii.codimp " );
			sql.append( "and ii.codemp=? and ii.codfilial=? and ii.codimp=? " );
			sql.append( "order by ii.codimp, ii.coditimp ");

			ps = getConn().prepareStatement( sql.toString() );

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		System.out.println( "Query de itens de importação:" + sql.toString() );

		return ps;

	}
	
	public PreparedStatement getStatementInsertCPItCompra() {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;

		try {

			sql.append( "insert into cpitcompra (" );
			sql.append( "emmanut				, calccusto, " );
			sql.append( "codemp					, codfilial			, codcompra			, coditcompra		, " );
			sql.append( "codemppd				, codfilialpd		, codprod			, refprod			, " );
			sql.append( "codempnt				, codfilialnt		, codnat			, " );
			sql.append( "codempax				, codfilialax		, codalmox			, " );
			sql.append( "qtditcompra			, precoitcompra		, vlrliqitcompra	, vlrproditcompra	, " );
			sql.append( "vlrbaseicmsitcompra	, percicmsitcompra	, vlricmsitcompra	, vlrfreteitcompra	, " );
			sql.append( "vlrbaseipiitcompra		, percipiitcompra	, vlripiitcompra	, " );
			sql.append( "codempif				, codfilialif		, codfisc			, coditfisc			, " );
			sql.append( "nadicao				, seqadic			, vlradicitcompra   , custoitcompra 	, " );
			sql.append( "vlriiItcompra			, adicicmstotnota   , vlritoutrasdespitcompra, vlrtxsiscomexitcompra, tiponfitcompra " );
			sql.append( ")" );
			sql.append( "values (" );
			sql.append( " ?						, ? , " );
			sql.append( " ?						, ?					, ?					, ?					, " );
			sql.append( " ?						, ?					, ?					, ?					, " );
			sql.append( " ?						, ?					, ?										, " );
			sql.append( " ?						, ?					, ?										, " );
			sql.append( " ?						, ?					, ?					, ?					, " );
			sql.append( " ?						, ?					, ?					, ?					, " );
			sql.append( " ?						, ?					, ?										, " );
			sql.append( " ?						, ?					, ?					, ?					, " );
			sql.append( " ?						, ?					, ?					, ?				    , " );
			sql.append( " ? 					, ?                 , ? 				, ?					, ? " );
			sql.append( ")" );

			ps = getConn().prepareStatement( sql.toString() );

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		System.out.println( "Query de inserção itcompra:" + sql.toString() );

		return ps;

	}
	
	
	public PreparedStatement getStatementInsertLFItCompra() {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;

		try {

			sql.append( "insert into lfitcompra (" );
			sql.append( "codemp					, codfilial			, codcompra			, coditcompra		, " );
			sql.append( "codempsp				, codfilialsp		, codsittribpis		, impsittribpis		, vlrbasepis	, aliqpis		, vlrpis	, " );
			sql.append( "codempsc				, codfilialsc		, codsittribcof		, impsittribcof		, vlrbasecofins	, aliqcofins	, vlrcofins , " );
			sql.append( "codempsi				, codfilialsi		, codsittribipi		, impsittribipi		, " );
			sql.append( "modbcicms				, aliqredbcicms		, origfisc			, codtrattrib		, vlrbaseii		, aliqii		, vlrii,	  " );
			sql.append( "vlricmsdiferido		, vlricmsdevido		, vlricmscredpresum																	  " );

			sql.append( ")" );
			sql.append( "values (" );
			sql.append( " ?						, ?					, ?					, ?					, " );
			sql.append( " ?						, ?					, ?					, ?					, ?				, ?				, ? 		, " );
			sql.append( " ?						, ?					, ?					, ?					, ?				, ?				, ?			, " );
			sql.append( " ?						, ?					, ?					, ?					, " );
			sql.append( " ?						, ?					, ?					, ?					, ?				, ?				, ?  		, " );
			sql.append( " ?						, ?					, ?																					  " );
			sql.append( ")" );

			ps = getConn().prepareStatement( sql.toString() );

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		System.out.println( "Query de inserção de itens de tributação:" + sql.toString() );

		return ps;

	}
	
	public void geraItensCompras(Integer codemp, Integer codfilial, Integer codcompra,  Integer codimp, Integer codempfr, Integer codfilialfr, Integer codfor, Integer codTipoMov, String utilizatbcalcca) {

		PreparedStatement ps_imp = null;
		PreparedStatement ps_trib = null;
		PreparedStatement ps_comp = null;

		ResultSet rs1 = null;
		ResultSet rs2 = null;

		Integer iparam = null;
		
		String tipoimp = "O";

		try {

			// Query para percorrer os itens de importação e inserir na tabela de itens de compra

			ps_imp = getStatementItensImportacao(utilizatbcalcca);

			ps_imp.setInt( 1, Aplicativo.iCodEmp );
			ps_imp.setInt( 2, ListaCampos.getMasterFilial( "CPIMPORTACAO" ) );
			ps_imp.setInt( 3, codimp );

			rs1 = ps_imp.executeQuery();

			// Gerando statement para inserção na CPITCOMPRA;

			ps_comp = getStatementInsertCPItCompra();

			while ( rs1.next() ) {

				// Inserindo os ítens de importação

				iparam = 1;

				tipoimp = rs1.getString( "tipoimp" );
				
				ps_comp.setString( iparam++, "S" );

				ps_comp.setString( iparam++, "N" );

				ps_comp.setInt( iparam++, codemp);
				ps_comp.setInt( iparam++, codfilial );
				ps_comp.setInt( iparam++, codcompra);
				ps_comp.setInt( iparam++, rs1.getInt( "CODITIMP" ) );

				ps_comp.setInt( iparam++, rs1.getInt( "codemppd" ) );
				ps_comp.setInt( iparam++, rs1.getInt( "codfilialpd" ) );
				ps_comp.setInt( iparam++, rs1.getInt( "codprod" ) );
				ps_comp.setString( iparam++, rs1.getString( "refprod" ) );

				ps_comp.setInt( iparam++, Aplicativo.iCodEmp );
				ps_comp.setInt( iparam++, ListaCampos.getMasterFilial( "LFNATOPER" ) );

				String codnat = buscaCFOP( rs1.getInt( "codprod" ), codfor, codTipoMov, rs1.getInt( "coditfisc" ), getConn() );

				ps_comp.setString( iparam++, codnat );

				ps_comp.setInt( iparam++, Aplicativo.iCodEmp );
				ps_comp.setInt( iparam++, ListaCampos.getMasterFilial( "EQALMOX" ) );
				ps_comp.setInt( iparam++, rs1.getInt( "codalmox" ) );

				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "qtd" ) );
				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "precoitcompra" ) );
				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "vlrliqitcompra" ) );
				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "vlrproditcompra" ) );

				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "vlrbaseicms" ) );
				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "aliqicmsuf" ) );
				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "vlricmsitcompra" ) );
				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "vlrfreteitcompra" ) );

				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "vlrbaseipiitcompra" ) );
				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "aliqipi" ) );
				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "vlripi" ) );

				ps_comp.setInt( iparam++, Aplicativo.iCodEmp );
				ps_comp.setInt( iparam++, ListaCampos.getMasterFilial( "LFCLFISCAL" ) );
				ps_comp.setString( iparam++, rs1.getString( "codfisc" ) );
				ps_comp.setInt( iparam++, rs1.getInt( "coditfisc" ) );

				ps_comp.setInt( iparam++, rs1.getInt( "nadicao" ) );
				ps_comp.setInt( iparam++, rs1.getInt( "seqadic" ) );
				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "vlradicitcompra" ) );
				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "custoitcompra" ) );
				ps_comp.setBigDecimal( iparam++, rs1.getBigDecimal( "vlrii" ) );
				ps_comp.setString( iparam++, rs1.getString( "adicicmstotnota" ) );
				ps_comp.setString( iparam++, rs1.getString( "vlritdespad" ) );
				ps_comp.setString( iparam++, rs1.getString( "vlrtxsiscomex" ) );
				
				ps_comp.setString( iparam++, rs1.getString( "tipoimp") );

				ps_comp.execute();

			}

			getConn().commit();

			// Query para percorrer os itens de importação e inserir na tabela LFITCOMPRA

			ps_imp = getStatementItensImportacao(utilizatbcalcca);

			ps_imp.setInt( 1, Aplicativo.iCodEmp );
			ps_imp.setInt( 2, ListaCampos.getMasterFilial( "CPIMPORTACAO" ) );
			ps_imp.setInt( 3, codimp );

			rs2 = ps_imp.executeQuery();

			// Gerando statement para inserção na CPITCOMPRA;

			ps_trib = getStatementInsertLFItCompra();

			while ( rs2.next() ) {

				// Inserção da LFITCOMPRA

				iparam = 1;

				ps_trib.setInt( iparam++, codemp );
				ps_trib.setInt( iparam++, codfilial);
				ps_trib.setInt( iparam++, codcompra );
				ps_trib.setInt( iparam++, rs2.getInt( "CODITIMP" ) );

				ps_trib.setInt( iparam++, rs2.getInt( "codempsp" ) );
				ps_trib.setInt( iparam++, rs2.getInt( "codfilialsp" ) );
				ps_trib.setString( iparam++, rs2.getString( "codsittribpis" ) );
				ps_trib.setString( iparam++, rs2.getString( "impsittribpis" ) );
				ps_trib.setBigDecimal( iparam++, rs2.getBigDecimal( "vlrbasepis" ) );
				ps_trib.setBigDecimal( iparam++, rs2.getBigDecimal( "aliqpis" ) );
				ps_trib.setBigDecimal( iparam++, rs2.getBigDecimal( "vlrpis" ) );

				ps_trib.setInt( iparam++, rs2.getInt( "codempsc" ) );
				ps_trib.setInt( iparam++, rs2.getInt( "codfilialsc" ) );
				ps_trib.setString( iparam++, rs2.getString( "codsittribcof" ) );
				ps_trib.setString( iparam++, rs2.getString( "impsittribcof" ) );
				ps_trib.setBigDecimal( iparam++, rs2.getBigDecimal( "vlrbasecofins" ) );
				ps_trib.setBigDecimal( iparam++, rs2.getBigDecimal( "aliqcofins" ) );
				ps_trib.setBigDecimal( iparam++, rs2.getBigDecimal( "vlrcofins" ) );

				ps_trib.setInt( iparam++, rs2.getInt( "codempsi" ) );
				ps_trib.setInt( iparam++, rs2.getInt( "codfilialsi" ) );
				ps_trib.setString( iparam++, rs2.getString( "codsittribipi" ) );
				ps_trib.setString( iparam++, rs2.getString( "impsittribipi" ) );

				ps_trib.setInt( iparam++, rs2.getInt( "modbcicms" ) );
				ps_trib.setBigDecimal( iparam++, rs2.getBigDecimal( "redfisc" ) );
				ps_trib.setString( iparam++, rs2.getString( "origfisc" ) );
				ps_trib.setString( iparam++, rs2.getString( "codtrattrib" ) );

				ps_trib.setBigDecimal( iparam++, rs2.getBigDecimal( "vlrbaseii" ) );
				ps_trib.setBigDecimal( iparam++, rs2.getBigDecimal( "aliqii" ) );
				ps_trib.setBigDecimal( iparam++, rs2.getBigDecimal( "vlrii" ) );

				ps_trib.setBigDecimal( iparam++, rs2.getBigDecimal( "vlricmsdiferido" ) );
				ps_trib.setBigDecimal( iparam++, rs2.getBigDecimal( "vlricmsrecolhimento" ) );
				ps_trib.setBigDecimal( iparam++, rs2.getBigDecimal( "vlricmscredpresum" ) );

				ps_trib.execute();

			}

			getConn().commit();

			ps_comp.close();
			ps_trib.close();
			ps_imp.close();

			rs1.close();
			rs2.close();

			

		} catch ( Exception e ) {
			Funcoes.mensagemErro( null, "Erro ao gerar itens de compra!", false, e );
			e.printStackTrace();
		}

	}
	
	private static String buscaCFOP( Integer codprod, Integer codfor, Integer codtipomov, Integer coditfisc, DbConnection conex ) {

		String ret = null;
		String sSQL = "SELECT CODNAT FROM LFBUSCANATSP (?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try {

			PreparedStatement ps = conex.prepareStatement( sSQL );

			ps.setInt( 1, Aplicativo.iCodFilial );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			ps.setInt( 4, codprod );
			ps.setNull( 5, Types.INTEGER );
			ps.setNull( 6, Types.INTEGER );
			ps.setNull( 7, Types.INTEGER );
			ps.setInt( 8, Aplicativo.iCodEmp );
			ps.setInt( 9, ListaCampos.getMasterFilial( "CPFORNECED" ) );
			ps.setInt( 10, codfor );
			ps.setInt( 11, ListaCampos.getMasterFilial( "EQTIPOMOV" ) );
			ps.setInt( 12, codtipomov );

			// Incluido parametro com o código do item fiscal
			if ( null == coditfisc || coditfisc <= 0 ) {
				ps.setNull( 13, Types.INTEGER );
			}
			else {
				ps.setInt( 13, coditfisc );
			}

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				ret = rs.getString( "CODNAT" );

			}
			rs.close();
			ps.close();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( null, "Erro ao buscar natureza da operação!\n" + err.getMessage(), true, conex, err );
		}

		return ret;

	}
	
	
	public void execRateio(Integer codemp, Integer codfilial, Integer codimp, BigDecimal vlrfretemitot, BigDecimal vlrvmldmitot,
			BigDecimal pesoliq, BigDecimal pesoliqtot, BigDecimal vlrdespad, BigDecimal vlradmitot, BigDecimal vlrthcmitot) throws SQLException {
		
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		//try {
			
		sql.append( "update cpitimportacao set ");

		// Rateando o Frete pelo peso bruto
		
		sql.append( "vlrfretemi=( ( ? * (pesoliquido) ) / ? )," );

		// Rateando o THC pelo valor + frete
		sql.append( "vlrthcmi=( ( ? * (vmldmi) ) / ? ) " );
		
		sql.append( "where codemp=? and codfilial=? and codimp=?" );

		BigDecimal pesobrutoitem = null;
		
		ps = getConn().prepareStatement( sql.toString() );
		
		ps.setBigDecimal( 1, vlrfretemitot );
		
		//Condição para evitar divisão por 0
		if(pesoliqtot.compareTo( new BigDecimal(0)) > 0 ){
			ps.setBigDecimal( 2, pesoliqtot );
		} else {
			if(pesoliq.compareTo( new BigDecimal(0)) > 0 ){
				ps.setBigDecimal( 2, pesoliq );
			} else {
				ps.setBigDecimal( 2, new BigDecimal(1) );	
			}
		}
		
		
		ps.setBigDecimal( 3, vlrthcmitot );
		
		//Condição para evitar divisão por 0
		if(vlrvmldmitot.compareTo( new BigDecimal(0)) > 0 ){
			ps.setBigDecimal( 4, vlrvmldmitot );
		} else {
			ps.setBigDecimal( 4, new BigDecimal(1) );
		}
		
		ps.setInt( 5, codemp );
		ps.setInt( 6, codfilial );
		ps.setInt( 7, codimp );
		
		ps.execute();
		
		if( vlrdespad.compareTo( new BigDecimal( 0 ) ) > 0 ){
			execRateioDespAD(codemp, codfilial, codimp, vlrfretemitot, vlrvmldmitot, vlrdespad, vlradmitot, vlrthcmitot);
		} else {
			zeraVlrDesp(codimp);	
		}
			
		getConn().commit();
	
	/*	}
		catch (Exception e) {
			Funcoes.mensagemErro( null, "Erro ao realizar o rateio do frete.", false, e );
			e.printStackTrace();
		}*/
	}
	
	private void execRateioDespAD( Integer codemp, Integer codfilial, Integer codimp, BigDecimal vlrfretemitot, BigDecimal vlrvmldmitot,
			 BigDecimal vlrdespad, BigDecimal vlradmitot, BigDecimal vlrthcmitot) throws SQLException {
		
		BigDecimal vlrTotDesp = BigDecimal.ZERO;
		BigDecimal diferenca = BigDecimal.ZERO;	

		//atualizaDespAd();
		atualizaDespAd( codimp, vlradmitot, 
				vlrfretemitot, vlrthcmitot, vlrdespad );
		
		
		vlrTotDesp = getTotalDespAd( codemp,  codfilial, codimp  );
		
		diferenca = vlrdespad.subtract( vlrTotDesp );
		
		if( (diferenca.compareTo( BigDecimal.ZERO ) > 0) || (diferenca.compareTo( BigDecimal.ZERO )< 0) ) {
			atualizaDiferenca( codemp, codfilial, codimp, diferenca );
		}
	}

	public void atualizaDespAd(Integer codimp, BigDecimal vlradmittot, 
			BigDecimal vlrfreteittot, BigDecimal vlrthcmittot, BigDecimal vlrdespad) throws SQLException{
		
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		try {
			sql.append( "update cpitimportacao it set it.VLRITDESPAD = ");
	
			//Rateando desp.Aduaneiras pelo valor do produto + frete + thc
			sql.append("(((it.vlradmi + it.vlrfretemi + it.vlrthcmi ) / (? + ? + ? )) * ?)" );
			
			sql.append(" where it.codemp=? and it.CODFILIAL=? and it.codimp=? ");
			
			ps = getConn().prepareStatement( sql.toString() );
			int param = 1;
		
			//Totalizadores utilizado na contato
			ps.setBigDecimal( param++, vlradmittot );
			ps.setBigDecimal( param++, vlrfreteittot );
			ps.setBigDecimal( param++, vlrthcmittot );
			
			//Valor que será rateado
			ps.setBigDecimal( param++, vlrdespad );
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "CPIMPORTACAO" ) );
			ps.setInt( param++, codimp );
			ps.execute();
		} catch (SQLException e) {
			Funcoes.mensagemErro( null, "Erro ao ratear despesas da compra de importacao!\n" + e.getMessage(), true, getConn(), e );
		} finally {
			ps = null;
		}
	}
	
	
	public void execRateioVlrCompl( Integer codemp, Integer codfilial, Integer codimp, BigDecimal vlrcompl ) {
		
		BigDecimal vlrTotCompl = BigDecimal.ZERO;
		BigDecimal diferenca = BigDecimal.ZERO;	

		rateioVlrComplementar( codemp, codfilial, codimp );
		
		
		vlrTotCompl = getTotalDespCompl( codemp,  codfilial, codimp  );
		
		diferenca = vlrcompl.subtract( vlrTotCompl );
		
		if( (diferenca.compareTo( BigDecimal.ZERO ) > 0) || (diferenca.compareTo( BigDecimal.ZERO )< 0) ) {
			atualizaDiferencaCompl( codemp, codfilial, codimp, diferenca );
		}
	}
	

	public void rateioVlrComplementar(Integer codemp, Integer codfilial, Integer codimp ) {
		
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		try {
			sql.append( "update cpitimportacao it set it.vlrcompl = ");
	
			//Rateando desp.Aduaneiras pelo valor do produto + frete + thc
			//sql.append("(((it.vlradmi + it.vlrfretemi + it.vlrthcmi ) / (? + ? + ? )) * ?)" );
			
			
			sql.append("((it.vlradmi + it.vlrfretemi + it.vlrthcmi ) / ( (select vlradmi + vlrfretemi  + vlrthcmi  from cpimportacao where codemp=? and codfilial=? and codimp=?) ) * ");
			sql.append("(select vlrcompl from cpimportacao where codemp = ? and codfilial = ? and codimp = ?) ) ");
			
			sql.append(" where it.codemp=? and it.CODFILIAL=? and it.codimp=? ");
			
			ps = getConn().prepareStatement( sql.toString() );
			int param = 1;
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codimp );
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codimp );
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codimp );
			ps.execute();
		} catch (SQLException e) {
			Funcoes.mensagemErro( null, "Erro ao ratear despesas Complmentar da compra de importacao!\n" + e.getMessage(), true, getConn(), e );
		} finally {
			ps = null;
		}
	}
	
	
	
	
	public BigDecimal getTotalDespAd(Integer codemp, Integer codfilial, Integer codimp) {
		
		BigDecimal vlrTotDesp = BigDecimal.ZERO;
		PreparedStatement ps = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append( "select  SUM(it.vlritdespad) vlrtotdespad from cpitimportacao it where it.codemp=? and it.codfilial=? and it.codimp=? ");
		
		try{
			ps = getConn().prepareStatement( sql.toString() );
			int param = 1;
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codimp );
			
			rs = ps.executeQuery();
			
			if(rs.next()){
				vlrTotDesp = rs.getBigDecimal( "vlrtotdespad" );
			}
			rs.close();
			ps.close();
		
		} catch (SQLException e) {
			Funcoes.mensagemErro( null, "Erro ao buscar valor total das despesas da compra de importacao!\n" + e.getMessage(), true, getConn(), e );
		} finally {
			rs = null;
			ps = null;
		}
	
		return vlrTotDesp;
	
	}
	
	public BigDecimal getTotalDespCompl(Integer codemp, Integer codfilial, Integer codimp) {
		
		BigDecimal vlrTotCompl = BigDecimal.ZERO;
		PreparedStatement ps = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append( "select  SUM(it.vlrcompl) vlrtotcompl from cpitimportacao it where it.codemp=? and it.codfilial=? and it.codimp=? ");
		
		try{
			ps = getConn().prepareStatement( sql.toString() );
			int param = 1;
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codimp );
			
			rs = ps.executeQuery();
			
			if(rs.next()){
				vlrTotCompl = rs.getBigDecimal( "vlrtotcompl" );
			}
			rs.close();
			ps.close();
		
		} catch (SQLException e) {
			Funcoes.mensagemErro( null, "Erro ao buscar valor total das despesas complementar da compra de importacao!\n" + e.getMessage(), true, getConn(), e );
		} finally {
			rs = null;
			ps = null;
		}
	
		return vlrTotCompl;
	
	}

	public void zeraVlrDesp(Integer codimp){
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		try {
			sql.append( "update cpitimportacao it set it.VLRITDESPAD = 0 ");
			sql.append(" where it.codemp=? and it.CODFILIAL=? and it.codimp=? ");
			
			ps = getConn().prepareStatement( sql.toString() );
			int param = 1;
		
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "CPIMPORTACAO" ) );
			ps.setInt( param++, codimp );
			ps.execute();
		} catch (SQLException e) {
			Funcoes.mensagemErro( null, "Erro ao atualizar valor total das despesas da compra de importacao!\n" + e.getMessage(), true, getConn(), e );
		} finally {
			ps = null;
		}
	}
	
	public void rateioSiscomex(Vector<Vector<Object>> vector, Integer codimp ) {
		
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			

			sql.append( "update cpitimportacao set ");

			// Rateando o Frete pelo valor aduaneiro
			sql.append( "vlrtxsiscomex=( ( ? * (vlrad) ) / ? )" );				
			sql.append( "where codemp=? and codfilial=? and codimp=? and codncm=?" );

			ps = getConn().prepareStatement( sql.toString() );
			
			for ( Object row : vector ) {
				Vector<Object> rowVect = (Vector<Object>) row;
				BigDecimal siscomex = ConversionFunctions.stringCurrencyToBigDecimal( rowVect.elementAt( FImportacao.GRID_ADICAO.VLRTXSISCOMEXADIC.ordinal() ).toString() ) ;
				BigDecimal vlradadic = ConversionFunctions.stringCurrencyToBigDecimal(  rowVect.elementAt( FImportacao.GRID_ADICAO.VLRADUANEIRO.ordinal()).toString() );
				String ncm = rowVect.elementAt( GRID_ADICAO.CODNCM.ordinal()).toString() ;
				
				ps.setBigDecimal( 1, siscomex );
				ps.setBigDecimal( 2, vlradadic );
	
				ps.setInt( 3, Aplicativo.iCodEmp );
				ps.setInt( 4, ListaCampos.getMasterFilial( "CPIMPORTACAO" ) );
				ps.setInt( 5, codimp );
				ps.setString( 6, ncm );
				
				ps.execute();
			
			}
			
			getConn().commit();
			
			Funcoes.mensagemInforma( null, "Valores rateados entre os ítens!" );
			//lcCampos.carregaDados();
			
		}
		catch (Exception e) {
			Funcoes.mensagemErro( null, "Erro ao realizar o rateio do frete.", false, e );
			e.printStackTrace();
		}
	}
	
	
	public void atualizaDiferenca(Integer codemp, Integer codfilial, Integer codimp, BigDecimal diferenca){
		
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		sql.append( "update cpitimportacao it set it.VLRITDESPAD = it.VLRITDESPAD + ? where it.codemp=? and it.CODFILIAL=? and it.codimp=? and ");
		sql.append( " it.coditimp=( select first 1 itm.coditimp from cpitimportacao itm where itm.codemp=it.codemp and itm.CODFILIAL=it.codfilial and itm.codimp=it.codimp order by itm.vlritdespad desc ) ");
		
		try{
			ps = getConn().prepareStatement( sql.toString() );
			int param = 1;
			ps.setBigDecimal( param++, diferenca );
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codimp );
			ps.execute();

		} catch (SQLException e) {
			Funcoes.mensagemErro( null, "Erro ao atualizar diferança no maior termo da compra de importacao!\n" + e.getMessage(), true, getConn(), e );
		} finally {
			ps = null;
		}		
	}
	
	
	public void atualizaDiferencaCompl(Integer codemp, Integer codfilial, Integer codimp, BigDecimal diferenca){
		
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		sql.append( "update cpitimportacao it set it.vlrcompl = it.vlrcompl + ? where it.codemp=? and it.CODFILIAL=? and it.codimp=? and ");
		sql.append( " it.coditimp=( select first 1 itm.coditimp from cpitimportacao itm where itm.codemp=it.codemp and itm.CODFILIAL=it.codfilial and itm.codimp=it.codimp order by itm.vlrcompl desc ) ");
		
		try{
			ps = getConn().prepareStatement( sql.toString() );
			int param = 1;
			ps.setBigDecimal( param++, diferenca );
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codimp );
			ps.execute();

		} catch (SQLException e) {
			Funcoes.mensagemErro( null, "Erro ao atualizar diferança no maior termo da compra de importacao!\n" + e.getMessage(), true, getConn(), e );
		} finally {
			ps = null;
		}		
	}
	
	public ResultSet buscaAdicao(Integer codemp, Integer codfilial, Integer codimp){
		
		
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			sql.append( "select codncm, sum(vlrad) vlrad from cpitimportacao where codemp=? and codfilial=? and codimp=? group by codncm " );
			
			ps = getConn().prepareStatement( sql.toString() );
			
			ps.setInt( 1, codemp );
			ps.setInt( 2, codfilial);
			ps.setInt( 3, codimp );
			
			rs = ps.executeQuery();
		} catch (Exception e) {
			Funcoes.mensagemErro( null, "Erro ao buscar items para adição!", false, e );
			e.printStackTrace();
		}
		
		
		return rs;

	}
	
	public void excluiAdicoes(Integer codemp, Integer codfilial, Integer codimp) {
		
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		
		try {
			
			sql.append("delete from cpimportacaoadic where codemp=? and codfilial=? and codimp=? ");
			
			ps = getConn().prepareStatement( sql.toString() );
			int param = 1;
			
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codimp );
			
			ps.execute();
			
			getConn().commit();
			
		}
		catch (Exception e) {
			Funcoes.mensagemErro( null, "Erro ao excluir adições!", false, e );
			e.printStackTrace();
		}
		
	}
	
	
	public Integer buscaClassificacaoFiscal(String codfisc, Integer codpais) {
		
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer coditfisc = 0;
		
		try {
			
			sql.append( "select coditfisc from lfitclfiscal " );
			sql.append( "where " );
			sql.append( "codemp=? and codfilial=? and codfisc=? and tipousoitfisc='CP' and codpais=?" );

			ps = getConn().prepareStatement( sql.toString() );
			
			int param = 1;
			
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "LFITCLFISCAL" ) );
			ps.setString( param++, codfisc );
			ps.setInt( param++, codpais );
			
			rs = ps.executeQuery();
			
			if(rs.next()){
				coditfisc = rs.getInt( "coditfisc" );
			}
			
		} catch (Exception e) {
			Funcoes.mensagemErro( null, "Erro ao buscar item da classificação fiscal.", false, e );
			e.printStackTrace();
		}
		
		return coditfisc;
	}
	
	
	//Retorna o Código da importação gerada
	public Integer geraImportacao(Integer codemp, Integer codfilial, Integer codimp, BigDecimal vlrcompl) {
		
		int proxCodImp = 0;
		StringBuilder sql = new StringBuilder();
		StringBuilder sql2 = new StringBuilder();
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
//		ResultSet rs = null;

		try {			
			proxCodImp = getProxCodImp( codemp, codfilial );
			
			
			//query de insert do cabeçalho da importação		
			sql.append( "insert into cpimportacao ( emmanut, tipoimp, codemp, codfilial, codimp ");
			sql.append( ", codempmi, codfilialmi, codmoeda, cotacaomoeda ");
			sql.append( ", codemppg, codfilialpg, codplanopag, codempfr, codfilialfr, codfor ");
			sql.append( ", invoice, di, manifesto, certorigem, lacre, prescarga, identhouse ");
			sql.append( ", dta, conheccarga, identcontainer, tipomanifesto, dtimp ");
			sql.append( ", dtemb, dtchegada, dtdesembdi, dtregdi, localemb, recintoaduaneiro ");
			sql.append( ", codpaisdesembdi, siglaufdesembdi, locdesembdi, obs, veiculo, pesobruto ");
			sql.append( ", pesoliquido, vlrfretemi, vlrfrete, vmlemi, vmldmi, vmle, vmld, vlrseguromi ");
			sql.append( ", vlrseguro, vlrii, vlripi, vlrpis, vlrcofins, vlrdireitosad, vlrthc, vlrthcmi ");
			sql.append( ", vlrtxsiscomex, vlrad, vlradmi, vlrbaseicms, vlricms, vlricmsdiferido, vlricmsdevido ");
			sql.append( ", vlricmscredpresum, vlricmsrecolhimento, vlrdespad");
			sql.append( ", vlrcompl ");
			sql.append( ", codempoi, codfilialoi, codimpoi ) ");

			sql.append( "select 'S' emmanut, 'C' tipoimp, codemp, codfilial, ");
			sql.append( proxCodImp );
			sql.append( " codimp, codempmi, codfilialmi, codmoeda, cotacaomoeda ");
			sql.append( ", codemppg, codfilialpg, codplanopag, codempfr, codfilialfr, codfor ");
			sql.append( ", invoice, di, manifesto, certorigem, lacre, prescarga, identhouse ");
			sql.append( ", dta, conheccarga, identcontainer, tipomanifesto, dtimp ");
			sql.append( ", dtemb, dtchegada, dtdesembdi, dtregdi, localemb, recintoaduaneiro ");
			sql.append( ", codpaisdesembdi, siglaufdesembdi, locdesembdi, obs, veiculo, pesobruto ");
			sql.append( ", pesoliquido, vlrfretemi, vlrfrete, vmlemi, vmldmi, vmle, vmld, vlrseguromi ");
			sql.append( ", vlrseguro, vlrii, vlripi, vlrpis, vlrcofins, vlrdireitosad, vlrthc, vlrthcmi ");
			sql.append( ", vlrtxsiscomex, vlrad, vlradmi, vlrbaseicms, vlricms, vlricmsdiferido, vlricmsdevido ");
			sql.append( ", vlricmscredpresum, vlricmsrecolhimento, vlrdespad, ");
			sql.append( String.valueOf( vlrcompl ) );
			
			sql.append( " vlrcompl, codemp codempoi, codfilial codfilialoi, codimp codimpoi ");
			sql.append( "from cpimportacao i ");
			sql.append( "where codemp=? and codfilial=? and codimp=? ");
			
			ps = getConn().prepareStatement( sql.toString() );
			int param = 1;
			
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codimp );
			ps.execute();
			ps.close();
			
			geraItensImportacao( codemp, codfilial, codimp, proxCodImp );
			
			if ( vlrcompl.compareTo( BigDecimal.ZERO ) > 0 ) {
				execRateioVlrCompl( codemp, codfilial, proxCodImp, vlrcompl );
			}
			
			param = 1;
			sql2.append( "update cpitimportacao set emmanut='N' where codemp=? and codfilial=? and codimp=?" );
			ps2 = getConn().prepareStatement( sql2.toString() );
			ps2.setInt( param++, codemp );
			ps2.setInt( param++, codfilial );
			ps2.setInt( param++, proxCodImp );
			ps2.execute();
			ps2.close();
			
			
		}catch (SQLException e) {
			Funcoes.mensagemErro( null, "Erro ao gerar cabeçalho de importação.", false, e );
			e.printStackTrace();
		}
		
		return proxCodImp;

	}
	
	
	public ResultSet getValoresTot( Integer codemp, Integer codfilial, Integer codimp ) {
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null; 
		try {
			sql.append( "select  im.vlradmi , im.vlrfretemi , im.vlrthcmi from cpitimportacao im where im.codemp=? and im.codfilial=? and im.codimp=?  ");
			ps = getConn().prepareStatement( sql.toString() );
			int param = 1;
			
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codimp );
			
			rs = ps.executeQuery();
		}catch (Exception e) {
			Funcoes.mensagemErro( null, "Erro ao gerar valores para rateio do valor Complementar.", false, e );
			e.printStackTrace();
		}
		return rs;
	}
	
	public void geraItensImportacao(Integer codemp, Integer codfilial, Integer codimp, Integer novocodimp){
		
		StringBuilder sql = new StringBuilder();
		StringBuilder sql2 = new StringBuilder();
		PreparedStatement ps = null;
//		ResultSet rs = null;
		PreparedStatement ps2 = null;

		try{
			// insert dos itens de importação
			sql.append( "insert into cpitimportacao ( emmanut, codemp, codfilial, codimp, coditimp, codemppd, codfilialpd, codprod ");
			sql.append( ", refprod, qtd, codempun, codfilialun, codunid, pesoliquido ");
			sql.append( ", pesobruto, precomi, preco, vmlemi, vmldmi, vmle, vmld ");
			sql.append( ", vlrfretemi, vlrfrete, vlrseguromi, vlrseguro, vlrthcmi ");
			sql.append( ", vlrthc, vlradmi, vlrad, aliqicmsimp, aliqicmsuf, percdifericms ");
			sql.append( ", perccredpresimp, aliqipi, aliqpis, aliqcofins, aliqii ");
			sql.append( ", vlrii, vlripi, vlrpis, vlrcofins, vlrbaseicms, vlricms ");
			sql.append( ", vlricmsdiferido, vlricmsdevido, vlricmscredpresum, vlricmsrecolhimento ");
			sql.append( ", vlritdespad, vlrtxsiscomex, vlrvmcv, codempcf, codfilialcf, codfisc ");
			sql.append( ", coditfisc, codncm, seqadic) ");
			
			sql.append( "select 'S', codemp, codfilial, ");
			sql.append( novocodimp );
			sql.append( " codimp,  coditimp, codemppd, codfilialpd, codprod ");
			sql.append( ", refprod, qtd, codempun, codfilialun, codunid, pesoliquido ");
			sql.append( ", pesobruto, precomi, preco, vmlemi, vmldmi, vmle, vmld ");
			sql.append( ", vlrfretemi, vlrfrete, vlrseguromi, vlrseguro, vlrthcmi ");
			sql.append( ", vlrthc, vlradmi, vlrad, aliqicmsimp, aliqicmsuf, percdifericms ");
			sql.append( ", perccredpresimp, aliqipi, aliqpis, aliqcofins, aliqii ");
			sql.append( ", vlrii, vlripi, vlrpis, vlrcofins, vlrbaseicms, vlricms ");
			sql.append( ", vlricmsdiferido, vlricmsdevido, vlricmscredpresum, vlricmsrecolhimento ");
			sql.append( ", vlritdespad, vlrtxsiscomex, vlrvmcv, codempcf, codfilialcf, codfisc ");
			sql.append( ", coditfisc, codncm, seqadic from cpitimportacao i ");
			sql.append( "where codemp=? and codfilial=? and codimp=? ");
			
			ps = getConn().prepareStatement( sql.toString() );
			int param = 1;
			
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codimp );
			ps.execute();
			ps.close();
			
			sql2.append( "insert into cpimportacaoadic" );
			sql2.append( "(codemp, codfilial, codimp, codncm, codadic, vlrtxsiscomex) ");
			sql2.append( "select codemp, codfilial, ");
			sql2.append( novocodimp );
			sql2.append( " codimp, codncm, codadic, vlrtxsiscomex " );
			sql2.append( "from cpimportacaoadic ");
			sql2.append( "where codemp=? and codfilial=? and codimp=?");

			ps2 = getConn().prepareStatement( sql2.toString() );
			param = 1;
			
			ps2.setInt( param++, codemp );
			ps2.setInt( param++, codfilial );
			ps2.setInt( param++, codimp );
			ps2.execute();
			ps2.close();

		}catch (Exception e) {
			Funcoes.mensagemErro( null, "Erro ao gerar cabeçalho de importação.", false, e );
			e.printStackTrace();
		}

	}

	public boolean insereImportacaoCompl(int codemp, int codfilial, int codimp, Vector<Vector<Object>> datavector ) throws SQLException {
		boolean result = false;
		StringBuilder sql = new StringBuilder("insert into cpimportacaocompl ");
		sql.append(" (id, codemp, codfilial, codimp, descadic, vlrdespadic ) ");
		sql.append(" values (?, ?, ?, ?, ?, ?)");
		int col_desc = 0;
		int col_valor = 1;
		for (Vector<Object> row: datavector) {
			int param = 1;
			int id = geraSeqId("CPIMPOTACAOCOMPL");
			PreparedStatement ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( param++, id);
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codimp );
			ps.setString( param++, (String) row.elementAt( col_desc ));
			ps.setBigDecimal( param++, (BigDecimal) row.elementAt( col_valor ));
			result = ps.execute();

		}
		       
		return result;
	}
	
	public boolean insereItcompraItcompral(int codempco, int codfilialco, int codcompraco, int codcompra, Vector<Vector<Object>> datavector ) throws SQLException {
		boolean result = false;
		StringBuilder sql = new StringBuilder("insert into cpitcompraitcompra ");
		sql.append(" (id, codemp, codfilial, codcompra, coditcompra, codempco, codfilialco, codcompraco, coditcompraco, qtditcompra ) ");
		sql.append(" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		int coditcompra = 1; 
		for (Vector<Object> row: datavector) {
			if ( (Boolean) row.elementAt( enum_itcompra.SEL.ordinal() )) {
				int param = 1;
				int id = geraSeqId("CPITCOMPRAITCOMPRA");
				PreparedStatement ps = getConn().prepareStatement( sql.toString() );
				
				ps.setInt( param++, id);
				ps.setInt( param++, codempco );  // codemp nova compra o mesmo da original
				ps.setInt( param++, codfilialco ); // codfilial nova compra  o mesmo da original
				ps.setInt( param++, codcompra ); // codcompra nova compra recebido como parãmetro 
				ps.setInt( param++, coditcompra ); // coditcompra nova compra sequencial 
				ps.setInt( param++, codempco );  // codemp  compra original
				ps.setInt( param++, codfilialco ); // codfilial compraoriginal
				ps.setInt( param++, codcompraco ); // codcompra original ;
				ps.setInt( param++, (Integer) row.elementAt( enum_itcompra.CODITCOMPRA.ordinal() ) ); // codcompra original ;
				ps.setBigDecimal( param++, (BigDecimal) row.elementAt( enum_itcompra.QTDITCOMPRA.ordinal() ));
				
				result = ps.execute();
				ps.close();
				coditcompra ++;
			}

		}
		       
		return result;
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
	
	public void geraDiferencaImportacao(Integer codemp, Integer codfilial, Integer codimp, Integer novocodimp){
		
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null; 

		try{
		
			//insert dos itens de importação complementar - diferenças
			sql.append( "insert into cpitimportacao (codemp, codfilial, codimp, coditimp, codemppd, codfilialpd, codprod ");
			sql.append( ", refprod, qtd, codempun, codfilialun, codunid, pesoliquido ");
			sql.append( ", pesobruto, precomi, preco, vmlemi, vmldmi, vmle, vmld ");
			sql.append( ", vlrfretemi, vlrfrete, vlrseguromi, vlrseguro, vlrthcmi ");
			sql.append( ", vlrthc, vlradmi, vlrad, aliqicmsimp, aliqicmsuf, percdifericms ");
			sql.append( ", perccredpresimp, aliqipi, aliqpis, aliqcofins, aliqii ");
			sql.append( ", vlrii, vlripi, vlrpis, vlrcofins, vlrbaseicms, vlricms ");
			sql.append( ", vlricmsdiferido, vlricmsdevido, vlricmscredpresum, vlricmsrecolhimento ");
			sql.append( ", vlritdespad, vlrtxsiscomex, vlrvmcv, codempcf, codfilialcf, codfisc ");
			sql.append( ", coditfisc, codncm, seqadic) ");

			sql.append( "select ic.codemp, ic.codfilial, ");
			sql.append( novocodimp );
			sql.append( " codimp, ic.coditimp, ic.codemppd, ic.codfilialpd, ic.codprod  ");
			sql.append( ", ic.refprod, ic.qtd, ic.codempun, ic.codfilialun, ic.codunid, ic.pesoliquido, ic.pesobruto ");
			sql.append( ", coalesce(ic.precomi,0)-coalesce(io.precomi,0) precomi ");
			sql.append( ", coalesce(ic.preco,0)-coalesce(io.preco,0) preco ");
			sql.append( ", coalesce(ic.vmlemi,0)-coalesce(io.vmlemi,0) vmlemi ");
			sql.append( ", coalesce(ic.vmldmi,0)-coalesce(io.vmldmi,0) vmldmi ");
			sql.append( ", coalesce(ic.vmle,0)-coalesce(io.vmle,0) vmle ");
			sql.append( ", coalesce(ic.vmld,0)-coalesce(io.vmld,0) vmld ");
			sql.append( ", coalesce(ic.vlrfretemi,0)-coalesce(io.vlrfretemi,0) vlrfretemi ");
			sql.append( ", coalesce(ic.vlrfrete,0)-coalesce(io.vlrfrete,0) vlrfrete ");
			sql.append( ", coalesce(ic.vlrseguromi,0)-coalesce(io.vlrseguromi,0) vlrseguromi ");
			sql.append( ", coalesce(ic.vlrseguro,0)-coalesce(io.vlrseguro,0) vlrseguro ");
			sql.append( ", coalesce(ic.vlrthcmi,0)-coalesce(io.vlrthcmi,0) vlrthcmi ");
			sql.append( ", coalesce(ic.vlrthc,0)-coalesce(io.vlrthc,0) vlrthc ");
			sql.append( ", coalesce(ic.vlradmi,0)-coalesce(io.vlradmi,0) vlradmi ");
			sql.append( ", coalesce(ic.vlrad,0)-coalesce(io.vlrad,0) vlrad ");
			sql.append( ", ic.aliqicmsimp, ic.aliqicmsuf, ic.percdifericms, ic.perccredpresimp, ic.aliqipi, ic.aliqpis, ic.aliqcofins, ic.aliqii ");
			sql.append( ", coalesce(ic.vlrii,0)-coalesce(io.vlrii,0) vlrii ");
			sql.append( ", coalesce(ic.vlripi,0)-coalesce(io.vlripi,0) vlripi ");
			sql.append( ", coalesce(ic.vlrpis,0)-coalesce(io.vlrpis,0) vlrpis ");
			sql.append( ", coalesce(ic.vlrcofins,0)-coalesce(io.vlrcofins,0) vlrcofins ");
			sql.append( ", coalesce(ic.vlrbaseicms,0)-coalesce(io.vlrbaseicms,0) vlrbaseicms ");
			sql.append( ", coalesce(ic.vlricms,0)-coalesce(io.vlricms,0) vlricms ");
			sql.append( ", coalesce(ic.vlricmsdiferido,0)-coalesce(io.vlricmsdiferido,0) vlricmsdiferido ");
			sql.append( ", coalesce(ic.vlricmsdevido,0)-coalesce(io.vlricmsdevido,0) vlricmsdevido ");
			sql.append( ", coalesce(ic.vlricmscredpresum,0)-coalesce(io.vlricmscredpresum,0) vlricmscredpresum ");
			sql.append( ", coalesce(ic.vlricmsrecolhimento,0)-coalesce(io.vlricmsrecolhimento,0) vlricmsrecolhimento ");
			sql.append( ", coalesce(ic.vlritdespad,0)-coalesce(io.vlritdespad,0) vlritdespad ");
			sql.append( ", coalesce(ic.vlrtxsiscomex,0)-coalesce(io.vlrtxsiscomex,0) vlrtxsiscomex ");
			sql.append( ", coalesce(ic.vlrvmcv,0)-coalesce(io.vlrvmcv,0) vlrvmcv ");
			sql.append( ", ic.codempcf, ic.codfilialcf, ic.codfisc ");
			sql.append( ", ic.coditfisc, ic.codncm, ic.seqadic  ");
			//sql.append( ", coalesce(ic.custoitcompra,0)-coalesce(io.custoitcompra,0) custoitcompra ");
			sql.append( "from cpitimportacao io, cpitimportacao ic ");
			sql.append( "where io.codemp=? and io.codfilial=? and io.codimp=? ");
			sql.append( "and ic.codemp=? and ic.codfilial=? and ic.codimp=? ");
			sql.append( "and ic.coditimp=io.coditimp; ");
			
			
			ps = getConn().prepareStatement( sql.toString() );
			int param = 1;
			
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codimp );
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codimp );
			
			ps.execute();

			
		}catch (Exception e) {
			Funcoes.mensagemErro( null, "Erro ao gerar cabeçalho de importação.", false, e );
			e.printStackTrace();
		}

	}
		
	public Integer getProxCodImp( Integer codemp, Integer codfilial ) throws SQLException{
		StringBuilder sql = new StringBuilder();
		int codimp = 0;
		
		//query para buscar próxima importacao		
		sql.append( "select coalesce(max(codimp)+1,1) codimp from cpimportacao where codemp=? and codfilial=? ");
		PreparedStatement ps = getConn().prepareStatement( sql.toString() );
		int param = 1;
		
		ps.setInt( param++, codemp );
		ps.setInt( param++, codfilial );
		ResultSet rs = ps.executeQuery();
		
		if(rs.next()){
			codimp = rs.getInt( "codimp" );
		}

		return codimp;
	}
	
	public Map<String, String> getPrefere( Integer codemp, Integer codfilial ) {
		Map<String, String> prefs = new HashMap<String, String>();
		
		StringBuffer sql = new StringBuffer();
		try {

			sql.append( "SELECT P1.UTILIZATBCALCCA, P1.CODTIPOMOVIC " );
			sql.append( "FROM SGPREFERE1 P1  " );
			sql.append( "WHERE P1.CODEMP=? AND P1.CODFILIAL=?" );

			PreparedStatement ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( 1, codemp);
			ps.setInt( 2, codfilial);

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {

		
				prefs.put( "UTILIZATBCALCCA", rs.getString( "UTILIZATBCALCCA" ));
				prefs.put( "CODTIPOMOVIC", rs.getString( "CODTIPOMOVIC" ));

				
			}
			getConn().commit();
		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( null, "Erro ao carregar a tabela PREFERE1!\n" + e.getMessage(), true, getConn(), e );
		}
		
		return prefs;
	}
	
}
