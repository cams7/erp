/**
 * @version 17/09/2011 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.gpe.dao <BR>
 *         Classe:
 * @(#)DAOBatida.java <BR>
 * 
 *                Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *              Classe de acesso a dados para entidade PEBATIDA
 * 
 */
package org.freedom.modulos.gpe.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import org.freedom.infra.dao.AbstractDAO;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.modulos.gpe.business.object.Batida;
import org.freedom.modulos.gpe.business.object.Batida.COL_PROC;
import org.freedom.modulos.gpe.business.object.Batida.EColPonto;
import org.freedom.modulos.gpe.business.object.Batida.PARAM_PROC_CARREGA;
import org.freedom.modulos.gpe.business.object.Batida.PARAM_PROC_INSERE;
import org.freedom.modulos.gpe.business.object.Batida.PREFS;


public class DAOBatida extends AbstractDAO {
	private Object prefs[] = null;
	
	public DAOBatida( DbConnection connection ) {

		super( connection );
		
	}

	public void setPrefs(Integer codemp, Integer codfilial) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = null;
		
		prefs = new Object[ Batida.PREFS.values().length];
		
		try {
			sql = new StringBuilder("select lancapontoaf, coalesce(tolregponto,20) tolregponto  " );
			sql.append( "from sgprefere3 p " );
			sql.append( "where  p.codemp=? and p.codfilial=?" );
			
			ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( 1, codemp );
			ps.setInt( 2, codfilial );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				prefs[ PREFS.LANCAPONTOAF.ordinal() ] = rs.getString( PREFS.LANCAPONTOAF.toString());
				prefs[ PREFS.TOLREGPONTO.ordinal() ] = rs.getInt( PREFS.TOLREGPONTO.toString() );
			}
			rs.close();
			ps.close();
			getConn().commit();
		} finally {
			ps = null;
			rs = null;
			sql = null;
		}
	}
	
	public Batida carregaPonto(Integer codemp, Integer codfilial, String idusu, String aftela) throws SQLException {
		// aftela = tela de abertura (A) ou tela de fechamento (F)
		Batida result = new Batida();
		result.setCarregaponto( (String) prefs[ PREFS.LANCAPONTOAF.ordinal()] );
		if ( "S".equals( result.getCarregaponto() ) ) { // Verifica se o sistema está configurado para carregar tela de ponto
		   result.setCodempus( codemp );
		   result.setCodfiliaus( codfilial );
		   result.setIdusu( idusu );
		   result.setAftela( aftela );
		   result.setTolregponto( (Integer) prefs[ PREFS.TOLREGPONTO.ordinal()]);
		   result = executeProcCarregaPonto(result);
		} 
		return result;
	}
	
	private Batida executeProcCarregaPonto(Batida batida) throws SQLException {
		StringBuilder sql = new StringBuilder();
		/*
		 * CARREGAPONTO, DATAPONTO, HORAPONTO, CODEMPAE, 
		CODFILIALAE, CODEMPEP, CODFILIALEP, MATEMPR
		 */
		sql.append("select carregaponto, dataponto, horaponto, codempae, ");
		sql.append("codfilialae, codempep, codatend, codfilialep, matempr ");
		sql.append("from crcarregapontosp(?, ?, ?, ?, ?)");
		PreparedStatement ps = getConn().prepareStatement( sql.toString() );
		ps.setInt( PARAM_PROC_CARREGA.CODEMP.ordinal(), batida.getCodempus() );
		ps.setInt( PARAM_PROC_CARREGA.CODFILIAL.ordinal(), batida.getCodfilialus() );
		ps.setString( PARAM_PROC_CARREGA.IDUSU.ordinal(), batida.getIdusu() );
		ps.setString( PARAM_PROC_CARREGA.AFTELA.ordinal(), batida.getAftela() );
		ps.setInt( PARAM_PROC_CARREGA.TOLREGPONTO.ordinal(), batida.getTolregponto() );
		ResultSet rs = ps.executeQuery();
		/*	public static enum COL_PROC {CARREGAPONTO, DATAPONTO, HORAPONTO, CODEMPAE, 
		CODFILIALAE, CODEMPEP, CODFILIALEP, MATEMPR };*/
		if (rs.next()) {
			batida.setCarregaponto( rs.getString(COL_PROC.CARREGAPONTO.toString()) );
			batida.setDataponto( Funcoes.sqlDateToDate( rs.getDate(COL_PROC.DATAPONTO.toString())) );
			batida.setHoraponto( Funcoes.sqlTimeToStrTime( rs.getTime( COL_PROC.HORAPONTO.toString()) ) );
			batida.setCodempae( rs.getInt(COL_PROC.CODEMPAE.toString()) );
			batida.setCodfilialae( rs.getInt(COL_PROC.CODFILIALAE.toString()) );
			batida.setCodatend( rs.getInt(COL_PROC.CODATEND.toString()) );
			batida.setCodempep( rs.getInt(COL_PROC.CODEMPEP.toString()) );
			batida.setCodfilialep( rs.getInt(COL_PROC.CODFILIALEP.toString()) );
			batida.setMatempr( rs.getInt(COL_PROC.MATEMPR.toString()) );
		}
		rs.close();
		ps.close();
		getConn().commit();
		return batida;
	}
	
	public boolean executeProcInsereBatida(Batida batida) throws SQLException {
		boolean result = true;
		StringBuilder sql = new StringBuilder();
//NONE, CODEMP, CODFILIAL, DTBAT, HBAT, CODEMPEP, CODFILIALEP, MATEMPR
		sql.append("execute procedure crinserebatidasp(?, ?, ?, ?, ?, ?, ?) ");
		PreparedStatement ps = getConn().prepareStatement( sql.toString() );
		ps.setInt( PARAM_PROC_INSERE.CODEMP.ordinal(), batida.getCodemp() );
		ps.setInt( PARAM_PROC_INSERE.CODFILIAL.ordinal(), batida.getCodfilial() );
		ps.setDate( PARAM_PROC_INSERE.DTBAT.ordinal(), Funcoes.dateToSQLDate( batida.getDataponto() ) );
		ps.setTime( PARAM_PROC_INSERE.HBAT.ordinal(), Funcoes.strTimeTosqlTime( batida.getHoraponto()) );
		ps.setInt( PARAM_PROC_INSERE.CODEMPEP.ordinal(), batida.getCodempep() );
		ps.setInt( PARAM_PROC_INSERE.CODFILIALEP.ordinal(), batida.getCodfilialep() );
		ps.setInt( PARAM_PROC_INSERE.MATEMPR.ordinal(), batida.getMatempr() );
		try {
			result = ps.execute();
			ps.close();
			getConn().commit();
		} catch (SQLException e) {
			getConn().rollback();
			throw e;
		}
		return result;
	}
	
	public void excluiBatida(Integer codemp, Integer codfilial,  String dtbat, Integer codempep, Integer codfilialep, Integer matempr, String hbat )throws SQLException{
		PreparedStatement ps = null;
		StringBuilder sql = null;
		int hora = 	Integer.parseInt(hbat.substring(0,2));
		int minuto = 	Integer.parseInt( hbat.substring(3,5));
	
		try{
			sql = new StringBuilder( "delete from pebatida pb " );
			sql.append( "where pb.codemp=? and pb.codfilial=? and pb.dtbat=? and " );			
			sql.append( "pb.codempep=? and pb.codfilialep=? and pb.matempr=? and ");
			sql.append( "extract(hour from pb.hbat)=?  and " );
			sql.append( "extract(minute from pb.hbat)=?" );
			
			ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( 1, codemp );
			ps.setInt( 2, codfilial );
			ps.setDate( 3, Funcoes.strDateToSqlDate( dtbat ) );
			ps.setInt( 4, codempep );
			ps.setInt( 5, codfilialep );
			ps.setInt( 6,  matempr);
			ps.setInt( 7,  hora );
			ps.setInt( 8,  minuto );
			ps.executeUpdate();
			
			
		} finally {
			ps = null;
			sql = null;
		}
		
	}
	
	
	
	public Vector<Vector<Object>> loadBatida( Integer codemp , Integer codfilial, Integer matempr, Date dataini, Date datafim) throws SQLException{
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = null;
		Vector<Vector<Object >> result = new Vector<Vector<Object>>();
		
		Vector<Object> row = null;
			
			try{
				sql = new StringBuilder( "select pb.dtbat, " );
				sql.append( "pb.hbat, pb.tipobat, pb.matempr, pb.idusuins " );
				sql.append( "from pebatida pb " );
				sql.append( "where pb.codemp=? and pb.codfilial=? and pb.matempr =? and pb.dtbat between ? and ? " );
					
				ps = getConn().prepareStatement( sql.toString() );
				ps.setInt( 1, codemp );
				ps.setInt( 2, codfilial );
				ps.setInt( 3, matempr );
				ps.setDate( 4, Funcoes.dateToSQLDate( dataini ) );
				ps.setDate( 5, Funcoes.dateToSQLDate( datafim ) );
				rs = ps.executeQuery();
				
				while( rs.next() ){
					row = new Vector<Object>();
					row.addElement(new Boolean( false ) );
					row.addElement( StringFunctions.sqlDateToStrDate(rs.getDate( EColPonto.DTBAT.toString() ) ) );
					row.addElement( rs.getString(  EColPonto.HBAT.toString()  ) );
					row.addElement( rs.getString(  EColPonto.TIPOBAT.toString()  ) );
					row.addElement( new Integer(rs.getInt( EColPonto.MATEMPR.toString() ) ) );
					row.addElement( rs.getString(  EColPonto.IDUSUINS.toString()  ) );
					result.addElement( row );
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
