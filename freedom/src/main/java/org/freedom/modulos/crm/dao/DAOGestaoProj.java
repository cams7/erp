package org.freedom.modulos.crm.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.freedom.infra.dao.AbstractDAO;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.infra.pojos.Constant;
import org.freedom.library.functions.Funcoes;
import org.freedom.modulos.crm.business.object.ContratoVW.EColContr;


public class DAOGestaoProj extends AbstractDAO {
	
	private Object prefs[] = null;

	public static Constant TIPO_CT = new Constant( "Contrato", "CT" );
	public static Constant TIPO_IC = new Constant( "Item de contrato", "IC" );
	public static Constant TIPO_SC = new Constant( "Sub.-contrato", "SC" );
	public static Constant TIPO_IS = new Constant( "Item sub-contrato", "IS" );
	public static Constant TIPO_TA = new Constant( "Tarefa" ,"TA" );
	public static Constant TIPO_ST = new Constant( "Sub-tarefa", "ST" );
	
    private static Constant[] LIST_TIPO = {TIPO_CT, TIPO_IC, TIPO_SC, TIPO_IS, TIPO_TA, TIPO_ST};
	
	public DAOGestaoProj( DbConnection cn) {

		super( cn );

	}
	
	public String getQueryAcao( String conthsubcontr, Integer codsubcontr, Integer codtarefa, Integer codatend ) {
		StringBuilder sql = new StringBuilder();
		sql.append( "select a.dataatendo, a.nomeatend, ct.indice " );
		sql.append( ", a.codchamado, a.horaatendo, a.horaatendofin " );
		sql.append( ", a.totalgeral, a.totalcobcli " );
		sql.append( ", a.obsatendo " );
		sql.append( ", ct.tipo " );
		sql.append( ", ( case " );
		sql.append( " when idx=1 and ct.tipo in ('SC','SP') then desccontrsc " );
		sql.append( " when idx=1 and ct.tipo in ('CT','PJ') then desccontr " );
		sql.append( " when idx=2 then ct.descitcontr " );
		sql.append( " when idx=3 then ct.desctarefa " );
		sql.append( " when idx=4 then ct.desctarefast " );
		sql.append( "end ) descricao " );
		sql.append( ", ct.idx, ct.codcontr, ct.codcontrsc " ); 
		sql.append( ", ct.coditcontr, ct.codtarefa, ct.codtarefast " );
		sql.append( ", ct.idx01, ct.idx02, ct.idx03, ct.idx04, ct.idx05 " );
		sql.append( "from vdcontratovw01 ct " );
		sql.append( "inner join atatendimentovw02 a " );
		sql.append( "on a.codempct=coalesce(ct.codempsc, ct.codempct) " );
		sql.append( "and a.codfilialct=coalesce(ct.codfilialsc, ct.codfilialct) " );
		sql.append( "and a.codcontr=coalesce(ct.codcontrsc, ct.codcontr) " );
		sql.append( "and a.coditcontr=ct.coditcontr " );
		sql.append( "and a.codempta=coalesce(ct.codempst, ct.codempta) " );
		sql.append( "and a.codfilialta=coalesce(ct.codfilialst, ct.codfilialta) " );
		sql.append( "and a.codtarefa=coalesce(ct.codtarefast, ct.codtarefa) " );
		sql.append( "where ct.codempct=? and ct.codfilialct=? and ct.codcontr=? " );
		if (codsubcontr > 0 ){
			sql.append( "and ct.codcontrsc = ? " );
		} 
		if (codtarefa > 0 ) {
			sql.append( "and ct.codtarefa = ? " );
		} 
		if (codatend > 0 ) {
		sql.append( " and  a.codempae=ct.codempct and a.codfilialae=ct.codfilialct and a.codatend=? ");
		}
		sql.append( "and a.dataatendo between ? and ? " );

		if ("S".equals(conthsubcontr)) {
			sql.append( "and ct.codcontrsc is not null " );
		} else {
			sql.append( "and ct.codcontrsc is null ");
		}
		sql.append( "order by a.dataatendo, a.nomeatend, a.horaatendo, a.horaatendofin " );
		return sql.toString();
	}
	
	public void setParamsQueryAcao( PreparedStatement ps, Integer codempct, 
			Integer codfilialct, Integer codcontr, Date dataini, Date datafim, Integer codsubcontr, Integer codtarefa, Integer codatend ) throws SQLException {
		int param = 1;
		ps.setInt( param++, codempct );
		ps.setInt( param++, codfilialct );
		ps.setInt( param++, codcontr );
		if (codsubcontr > 0 ) {
			ps.setInt( param++, codsubcontr );
		} 
		if (codtarefa > 0 ) {
			ps.setInt( param++, codtarefa );
		} 
		if (codatend > 0 ) {
			ps.setInt( param++, codatend );
		}
		ps.setDate( param++, Funcoes.dateToSQLDate( dataini ) );
		ps.setDate( param++, Funcoes.dateToSQLDate( datafim ) );

	}
	
	public String getTotaisAcao( String conthsubcontr, Integer codsubcontr, Integer codtarefa, Integer codatend ){
		StringBuilder sql = new StringBuilder();
		sql.append( "select sum( a.totalgeral ) totgeral " );
		sql.append( ", sum(a.totalcobcli) totcob " );
		sql.append( "from vdcontratovw01 ct " );
		sql.append( "inner join atatendimentovw02 a " );
		sql.append( "on a.codempct=coalesce(ct.codempsc, ct.codempct) " );
		sql.append( "and a.codfilialct=coalesce(ct.codfilialsc, ct.codfilialct) " );
		sql.append( "and a.codcontr=coalesce(ct.codcontrsc, ct.codcontr) " );
		sql.append( "and a.coditcontr=ct.coditcontr " );
		sql.append( "and a.codempta=coalesce(ct.codempst, ct.codempta) " );
		sql.append( "and a.codfilialta=coalesce(ct.codfilialst, ct.codfilialta) " );
		sql.append( "and a.codtarefa=coalesce(ct.codtarefast, ct.codtarefa) " );
		sql.append( "where ct.codempct=? and ct.codfilialct=? and ct.codcontr=? " );
		if (codsubcontr > 0 ){
			sql.append( "and ct.codcontrsc = ? " );
		} if (codtarefa > 0 ) {
			sql.append( "and ct.codtarefa = ? " );
		} 
		if (codatend > 0 ) {
			sql.append( " and a.codempae=ct.codempct and a.codfilialae=ct.codfilialct and a.codatend=? ");
		}
		sql.append( "and a.dataatendo between ? and ? " );

		if ("S".equals(conthsubcontr)) {
			sql.append( "and ct.codcontrsc is not null " );
		} else {
			sql.append( "and ct.codcontrsc is null ");
		}
		
		return sql.toString();	
	}
	
	
	
	public String getQueryContr( String conthsubcontr, String indice, Constant[] filtroTipo ){
		StringBuilder sql = null;
		sql = new StringBuilder( "select ct.tipo, ct.indice, " );
		sql.append( "( case " );
		sql.append( "when idx=1 and ct.tipo in ('SC','SP') then desccontrsc " );
		sql.append( "when idx=1 and ct.tipo in ('CT','PJ') then desccontr " );
		sql.append( "when idx=2 then ct.descitcontr " );
		sql.append( "when idx=3 then ct.desctarefa " );
		sql.append( "when idx=4 then ct.desctarefast " );
		sql.append( "end ) descricao, " );
		sql.append( "t.totalprevgeral, t.totalcobcliant, t.saldoant, t.totalprevper, t.totalcobcliper, t.saldoper, " );
		sql.append( "ct.idx, ct.codcontr, ct.codcontrsc, ct.coditcontr, ct.codtarefa, ct.codtarefast, " );
		sql.append( "ct.idx01, ct.idx02, ct.idx03, ct.idx04, ct.idx05 " );
		sql.append( "from vdcontratovw01 ct, vdcontratototsp(ct.codempct, ct.codfilialct, ct.codcontr, ct.coditcontr, " );
		sql.append( "ct.codempsc, ct.codfilialsc, ct.codcontrsc, " );
		sql.append( "ct.codempta, ct.codfilialta, ct.codtarefa, " );
		sql.append( "ct.codempst, ct.codfilialst, ct.codtarefast, " );
		sql.append( "?, ? ) t " );
		sql.append( "where ct.codempct=? and ct.codfilialct=? and ct.codcontr=? " );
		if ("S".equals(conthsubcontr)) {
			sql.append( "and ct.codcontrsc is not null " );
		} else {
			sql.append( "and ct.codcontrsc is null ");
		}
		if ( indice!=null &&  ! "".equals(indice) ) {
			sql.append( "and ct.indice like ?" );
		}
		if (filtroTipo!=null && filtroTipo.length>0 ) {
			sql.append( "and ct.tipo in ( ");
			for ( int i=0; i<filtroTipo.length; i++ ) {
				if (i>0) {
					sql.append(", ");
				}
				sql.append(" ? ");
			}
			sql.append( ") ");
		}
			
		sql.append( "order by idx01, idx02, idx03, idx04, idx05 " );
		
		return sql.toString();
	}
	
	public Constant getTipo( final String tipo ) {
		Constant result = null;
		if (tipo!=null) {
			for ( int i=0; i<LIST_TIPO.length; i++ ) {
				if ( LIST_TIPO[i].getValue().equals( tipo ) ) {
					result = LIST_TIPO[i]; 
					break;
				}
			}
		}
		return result;
	}
	
	public static Constant[] getFiltroTipo(final String tipoct, final String tipoic, final String tiposc, 
			final String tipois, final String tipota, final String tipost) {
		Constant[] result = null;
		
	    List<Constant> list = new ArrayList<Constant>();
	    if ("S".equals( tipoct )) {
	    	list.add( TIPO_CT );
	    }
	    if ("S".equals( tipoic )) {
	    	list.add( TIPO_IC );
	    }
	    if ("S".equals( tiposc )) {
	    	list.add( TIPO_SC );
	    }
	    if ("S".equals( tipois )) {
	    	list.add( TIPO_IS );
	    }
	    if ("S".equals( tipota )) {
	    	list.add( TIPO_TA );
	    }
	    if ("S".equals( tipost )) {
	    	list.add( TIPO_ST );
	    }
	    if ( list.size()>0 ) {
	    	result = new Constant[list.size()];
	    	for (int i=0; i<result.length; i++) {
	    		result[i] = list.get( i );
	    	}
	    }
        
		return result;
	}
	
	
	public void setParamsQueryContr( PreparedStatement ps, Date dataini, Date datafim, Integer codempct, 
			Integer codfilialct, Integer codcontr, String indice, final Constant[] filtroTipo ) throws SQLException {
		int param = 1;
		ps.setDate( param++, Funcoes.dateToSQLDate( dataini ) );
		ps.setDate( param++, Funcoes.dateToSQLDate( datafim ) );
		ps.setInt( param++, codempct );
		ps.setInt( param++, codfilialct );
		ps.setInt( param++, codcontr );
		if ( indice!=null &&  ! "".equals(indice) ) {
			ps.setString( param++, indice+"%" );
		}
		if (filtroTipo!=null && filtroTipo.length>0 ) {
			for ( int i=0; i<filtroTipo.length; i++) {
				ps.setString( param++, (String) filtroTipo[i].getValue() );
			}
		}

	}
	
	public Vector<Vector<Object>> loadContr( Date dataini, Date datafim, Integer codempct , Integer codfilialct, Integer codcontr, 
			String conthsubcontr, final String indice, final Constant[] filtroTipo ) throws SQLException{
		PreparedStatement ps = null;
		ResultSet rs = null;
		Vector<Vector<Object >> result = new Vector<Vector<Object>>();
		
		Vector<Object> row = null;
	
			try{
				
				ps = getConn().prepareStatement( getQueryContr( conthsubcontr, indice, filtroTipo ) );
				setParamsQueryContr( ps, dataini, datafim, codempct, codfilialct, codcontr, indice, filtroTipo );
				rs = ps.executeQuery();
		
				while( rs.next() ){
					
					row = new Vector<Object>();
					row.addElement( getString(  rs.getString( EColContr.TIPO.toString() ) ) );
					row.addElement( getString( rs.getString( EColContr.INDICE.toString() ) ) );
					row.addElement( getString( rs.getString( EColContr.DESCRICAO.toString() ) ) );
					//ponto para colocar o chamado.
					row.addElement( getString( "" ) );
					row.addElement( getBigDecimal( rs.getBigDecimal(  EColContr.TOTALPREVGERAL.toString() ) ) );
					row.addElement( getBigDecimal( rs.getBigDecimal( EColContr.TOTALCOBCLIANT.toString() ) ) );
					row.addElement( getBigDecimal( rs.getBigDecimal( EColContr.SALDOANT.toString() ) ) );
					row.addElement( getBigDecimal( rs.getBigDecimal( EColContr.TOTALPREVPER.toString() ) ) );
					row.addElement( getBigDecimal( rs.getBigDecimal( EColContr.TOTALCOBCLIPER.toString() ) ) );
					row.addElement( getBigDecimal( rs.getBigDecimal( EColContr.SALDOPER.toString() ) ) );
					row.addElement( new Integer(rs.getInt( EColContr.IDX.toString() ) ) );
					row.addElement( new Integer(rs.getInt( EColContr.CODCONTR.toString() ) ) );
					row.addElement( new Integer(rs.getInt( EColContr.CODCONTRSC.toString() ) ) );
					row.addElement( new Integer(rs.getInt( EColContr.CODITCONTR.toString() ) ) );
					row.addElement( new Integer(rs.getInt( EColContr.CODTAREFA.toString() ) ) );
					row.addElement( new Integer(rs.getInt( EColContr.CODTAREFAST.toString() ) ) );
					result.addElement( row );
				}
				rs.close();
				ps.close();
				getConn().commit();
			} finally {
				ps = null;
				rs = null;
			}
			return result;
		}	
	
public Integer getNewIndiceContr(Integer codemp, Integer codfilial, Integer codcli) throws SQLException	{
		
		Integer result = null;
		StringBuilder sql = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		try{
			sql = new StringBuilder();
			sql.append( "SELECT COALESCE(MAX(CO.INDEXCONTR),0)+1 INDEXCONTR " );
			sql.append( "FROM VDCONTRATO CO " );
			sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODCLI=?" );

			ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( 1, codemp );
			ps.setInt( 2, codfilial );
			ps.setInt( 3, codcli );
			rs = ps.executeQuery();
			
			if( rs.next() ){
				result = new Integer( rs.getInt( "INDEXCONTR" ) );		
			} else {
				result = 1;
			}
			
			rs.close();
			ps.close();
			getConn().commit();
			
		} finally {
			ps = null;
			rs = null;
			sql = null;
		}
		
		
		
		return result;
	}
	
	public Integer getNewIndiceItemContr(Integer codemp, Integer codfilial, Integer codcontr) throws SQLException	{
		
		Integer result = null;
		StringBuilder sql = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		try{
			sql = new StringBuilder();
			sql.append( "SELECT COALESCE(MAX(IC.INDEXITCONTR),0)+1 INDEXITCONTR " );
			sql.append( "FROM VDITCONTRATO IC " );
			sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODCONTR=?" );

			ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( 1, codemp );
			ps.setInt( 2, codfilial );
			ps.setInt( 3, codcontr );
			rs = ps.executeQuery();
			
			if( rs.next() ){
				result = new Integer( rs.getInt( "INDEXITCONTR" ) );		
			} else {
				result = 1;
			}
			
			rs.close();
			ps.close();
			getConn().commit();
			
		} finally {
			ps = null;
			rs = null;
			sql = null;
		}
		
		return result;
	}
	
	
	public Integer getNewIndiceItemTarefa(Integer codempct, Integer codfilialct, Integer codcontr, Integer coditcontr) throws SQLException	{
		
		Integer result = null;
		StringBuilder sql = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		try{

			sql = new StringBuilder();
			sql.append( "SELECT COALESCE(MAX(TA.INDEXTAREFA),0)+1 INDEXTAREFA " );
			sql.append( "FROM CRTAREFA TA " );
			sql.append( "WHERE CODEMPCT=? AND CODFILIALCT=? AND CODCONTR=?  AND CODITCONTR=? AND TIPOTAREFA=?" );

			ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( 1, codempct );
			ps.setInt( 2, codfilialct );
			ps.setInt( 3, codcontr );
			ps.setInt( 4, coditcontr );
			ps.setString( 5, "T" );
			rs = ps.executeQuery();
			
			if( rs.next() ){
				result = new Integer( rs.getInt( "INDEXTAREFA" ) );		
			} else {
				result = 1;
			}
			
			rs.close();
			ps.close();
			getConn().commit();
			
		} finally {
			ps = null;
			rs = null;
			sql = null;
		}
		
		return result;
	}
	
	public Integer getNewIndiceItemSubTarefa(Integer codempta, Integer codfilialta, Integer codtarefa) throws SQLException	{
		
		Integer result = null;
		StringBuilder sql = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		try{

			sql = new StringBuilder();
			sql.append( "SELECT COALESCE(MAX(TA.INDEXTAREFA),0)+1 INDEXTAREFA " );
			sql.append( "FROM CRTAREFA TA " );
			sql.append( "WHERE CODEMPTA=? AND CODFILIALTA=? AND CODTAREFATA=?" );

			ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( 1, codempta );
			ps.setInt( 2, codfilialta );
			ps.setInt( 3, codtarefa );
			//ps.setString( 4, "S" );
			rs = ps.executeQuery();
			
			if( rs.next() ){
				result = new Integer( rs.getInt( "INDEXTAREFA" ) );		
			} else {
				result = 1;
			}
			
			rs.close();
			ps.close();
			getConn().commit();
			
		} finally {
			ps = null;
			rs = null;
			sql = null;
		}
		
		return result;
	}
	
	public Integer getCodContr(Integer codemp, Integer codfilial, Integer codtarefa) throws SQLException{
		Integer result = null;
		StringBuilder sql = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try{

			sql = new StringBuilder();
			sql.append( "SELECT CODCONTR " );
			sql.append( "FROM CRTAREFA TA " );
			sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODTAREFA=?" );

			ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( 1, codemp );
			ps.setInt( 2, codfilial );
			ps.setInt( 3, codtarefa );
			rs = ps.executeQuery();

			if( rs.next() ){
				result = new Integer( rs.getInt( "CODCONTR" ) );		
			} else {
				result = null;
			}
			rs.close();
			ps.close();
			getConn().commit();
		} finally {
			ps = null;
			rs = null;
			sql = null;
		}

		return result;
	}
	public Integer getCodItContr(Integer codemp, Integer codfilial, Integer codtarefa) throws SQLException{
		Integer result = null;
		StringBuilder sql = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try{

			sql = new StringBuilder();
			sql.append( "SELECT CODITCONTR " );
			sql.append( "FROM CRTAREFA TA " );
			sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODTAREFA=?" );

			ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( 1, codemp );
			ps.setInt( 2, codfilial );
			ps.setInt( 3, codtarefa );
			rs = ps.executeQuery();

			if( rs.next() ){
				result = new Integer( rs.getInt( "CODITCONTR" ) );		
			} else {
				result = null;
			}
			rs.close();
			ps.close();
			getConn().commit();
		} finally {
			ps = null;
			rs = null;
			sql = null;
		}

		return result;
	}
	
}


