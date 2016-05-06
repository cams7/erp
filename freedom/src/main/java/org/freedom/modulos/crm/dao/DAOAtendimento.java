/**
 * @version 02/08/2011 <BR>
 * @author Setpoint Tecnologia em Informática Ltda./Robson Sanchez <BR>
 *
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.infra.dao <BR>
 * Classe: @(#)AbstractDAO.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA <BR> <BR>
 *
 * Classe base para implementações de métodos de acesso a dados para tabela ATATENDIMENTO
 */

package org.freedom.modulos.crm.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.freedom.infra.dao.AbstractDAO;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.crm.business.component.EstagioCheck;
import org.freedom.modulos.crm.business.object.Atendimento;
import org.freedom.modulos.crm.business.object.Atendimento.EColAtend;
import org.freedom.modulos.crm.business.object.Atendimento.EColExped;
import org.freedom.modulos.crm.business.object.Atendimento.INIFINTURNO;
import org.freedom.modulos.crm.business.object.Atendimento.PARAM_PRIM_LANCA;
import org.freedom.modulos.crm.business.object.Atendimento.PREFS;
import org.freedom.modulos.crm.business.object.Atendimento.PROC_IU;
import org.freedom.modulos.crm.business.object.SaldoContrato;
import org.freedom.modulos.crm.view.frame.utility.FCRM.COL_ATENDIMENTO;
import org.freedom.modulos.gpe.business.object.Batida;

public class DAOAtendimento extends AbstractDAO {

	private Object prefs[] = null;
	private enum COLBAT {INITURNO, INIINTTURNO, FININTTURNO, FINTURNO};
	private enum COLBATLANCTO {BATIDA, LANCTO, DIF, POS};

	// loop para checar estágio
	// Stágios: EPE = estágio pendente/sem nenhuma revisão
	//			E1I = estágio 1 situação (I) inconsistência
	//			E1O = estágio 1 situação (O) OK
	// Estágio 1, verifica a necessidade de inserção de horários no ponto

	public DAOAtendimento( DbConnection cn )  {

		super( cn );
		//setPrefs();

	}

	private Integer getSequencia(Integer codemp, Integer codfilial, String tab) throws SQLException {
		Integer result = null;
		StringBuilder sql = new StringBuilder("select iseq from spgeranum( ?, ?, ? )");
		PreparedStatement ps = getConn().prepareStatement( sql.toString() );
		ps.setInt( 1, codemp );
		ps.setInt( 2, codfilial );
		ps.setString( 3, tab );
		ResultSet rs = ps.executeQuery();
		return result;
	}

	private void updateHoraatendo(Integer codemp, Integer codfilial, Integer codatendo,
			String horaatendo, String horaatendofin) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append( "update atatendimento set horaatendo=?, horaatendofin=? " );
		sql.append( "where codemp=? and codfilial=? and codatendo=?" );
		PreparedStatement ps = getConn().prepareStatement( sql.toString() );
		ps.setTime( 1, Funcoes.strTimeToSqlTime( horaatendo, false ) );
		ps.setTime( 2, Funcoes.strTimeToSqlTime( horaatendofin, false ) );
		ps.setInt( 3, codemp );
		ps.setInt( 4, codfilial );
		ps.setInt( 5, codatendo );
		ps.executeUpdate();
		ps.close();
		try {
			getConn().commit();
		} catch (SQLException e) {
			getConn().rollback();
		}
	}
	public Atendimento loadAtendo(Integer codemp, Integer codfilial, Integer codatendo)throws SQLException {
		Atendimento result = null;
		StringBuilder sql = new StringBuilder();
		sql.append( "select " );
		sql.append( "atd.codempto, atd.codfilialto, atd.codtpatendo, " );
		sql.append( "atd.codempsa, atd.codfilialsa, atd.codsetat, ");
		sql.append( "atd.obsatendo, atd.obsinterno, atd.statusatendo, " );
		sql.append( "atd.codempcl, atd.codfilialcl, atd.codcli, atd.codempct, ");
		sql.append( "atd.codfilialct, atd.codcontr, atd.coditcontr, " );
		sql.append( "atd.codempca, atd.codfilialca, atd.codclasatendo," );
		sql.append( "atd.codempch, atd.codfilialch, atd.codchamado, "); 
		sql.append( "atd.codempea, atd.codfilialea, atd.codespec, " );
		sql.append( "atd.codempta, atd.codfilialta, atd.codtarefa, " );
		sql.append( "atc.codempoc, atc.codfilialoc, atc.tipoorc, atc.codorc, atd.sitatendo " );
		sql.append( "from atatendimento atd " );
		sql.append( "left outer join atatendimentoorc atc on ");
		sql.append( "atc.codemp =atd.codemp and atc.codfilial=atd.codfilial and atc.codatendo=atd.codatendo ");
		sql.append( "where " );
		sql.append( "atd.codemp=? and atd.codfilial=? and atd.codatendo=? " );

		if (codatendo!= null) {
			PreparedStatement ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( 1, codemp );
			ps.setInt( 2, codfilial );
			ps.setInt( 3, codatendo );
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				result = new Atendimento();
				result.setCodemp( codemp );
				result.setCodfilial( codfilial );
				result.setCodempto( rs.getInt( "codempto" ) );
				result.setCodfilialto( rs.getInt( "codfilialto" ) );
				result.setCodtpatendo( rs.getInt( "codtpatendo" ) );
				result.setCodempsa( rs.getInt(  "codempsa" ) );
				result.setCodfilialsa( rs.getInt( "codfilialsa" ) );
				result.setCodsetat( rs.getInt("codsetat" ) );
				result.setObsatendo( rs.getString(  "obsatendo" ) );
				result.setObsinterno( rs.getString( "obsinterno" ) );
				result.setStatusatendo( rs.getString("statusatendo" ) );
				result.setCodempcl( rs.getInt( "codempcl" ) );
				result.setCodfilialcl( rs.getInt("codfilialcl"));
				result.setCodcli( rs.getInt( "codcli" ) );
				if ( rs.getString( "coditcontr" )!=null ) {
					result.setCodempct( rs.getInt("codempct") );
					result.setCodfilialct( rs.getInt( "codfilialct" ) );
					result.setCodcontr( rs.getInt( "codcontr" ) );
					result.setCoditcontr( rs.getInt( "coditcontr" ) );
				}
				result.setCodempca( rs.getInt( "codempca" ) );
				result.setCodfilialca( rs.getInt( "codfilialca" ) );
				result.setCodclasatendo( rs.getInt( "codclasatendo" ) );
				if ( rs.getString( "codchamado" )!=null ) {
					result.setCodempch( rs.getInt( "codempch" ) );
					result.setCodfilialch ( rs.getInt( "codfilialch" ) );
					result.setCodchamado( rs.getInt( "codchamado" ) );
				}
				result.setCodempea( rs.getInt( "codempea" ) );
				result.setCodfilialea( rs.getInt( "codfilialea" ) );
				result.setCodespec( rs.getInt( "codespec" ) );
				if ( rs.getString( "codtarefa" )!=null ) {
					result.setCodempta( rs.getInt( "codempta" ) );
					result.setCodfilialta ( rs.getInt( "codfilialta" ) );
					result.setCodtarefa( rs.getInt( "codtarefa" ) );
				}
				if ( rs.getString( "codorc" )!=null ) {
					result.setCodempoc( rs.getInt( "codempoc" ) );
					result.setCodfilialoc ( rs.getInt( "codfilialoc" ) );
					result.setTipoorc(  rs.getString( "tipoorc" ) );
					result.setCodorc( rs.getInt( "codorc" ) );
				}
				result.setDocatendo( "0" );
				result.setConcluichamado( "N" );

				if ( rs.getString( "sitatendo" )!=null ) {
					result.setSitatendo( rs.getString( "sitatendo" ) );
				}
			}

		}
		return result;
	}

	public SaldoContrato loadSaldoContrato(final int codempcl, final int codfilialcl, final int codcli, final int codempct
			, final int codfilialct, final int codcontr, final int coditcontr, final Date dtini, final Date dtfin) throws SQLException {
		SaldoContrato result = null;
		StringBuilder  sql = new StringBuilder();
		int param = 1;
		sql.append( "select a.qtditcontr, a.qtdhoras, a.saldomes, a.mes, a.ano,excedentemescob, a.excedentemes ");
		sql.append( " from atresumoatendosp02(?, ?, ?, ?, ?, ?, ?, ?, ?) a ");
		PreparedStatement ps = getConn().prepareStatement( sql.toString() );
		ps.setInt( param++, codempcl );
		ps.setInt( param++, codfilialcl );
		ps.setInt( param++, codcli );
		ps.setInt( param++, codempct );
		ps.setInt( param++, codfilialct );
		ps.setInt( param++, codcontr );
		ps.setInt( param++, coditcontr );
		ps.setDate( param++, Funcoes.dateToSQLDate( dtini) );
		ps.setDate( param++, Funcoes.dateToSQLDate( dtfin) );
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			result = new SaldoContrato();
			result.setQtditcontr( rs.getBigDecimal( "qtditcontr" ) );
			result.setQtdhoras( rs.getBigDecimal( "qtdhoras" ) );
			result.setSaldomes( rs.getBigDecimal( "saldomes" ) );
			result.setMes( rs.getInt( "mes" ) );
			result.setAno( rs.getInt( "ano" ) );
			result.setExcedentemescob( rs.getBigDecimal( "excedentemescob" ) );
			result.setExcedentemes( rs.getBigDecimal( "excedentemes" ) );
		}

		rs.close();
		ps.close();
		getConn().commit();

		return result;
	}

	public Atendimento loadModelAtend(Integer codemp, Integer codfilial, Integer codempmo, Integer codfilialmo, Integer codmodel) throws SQLException {
		Atendimento result = null;
		Integer codatendo = null;

		StringBuilder sql = new StringBuilder("select ");
		sql.append( "mod.codempto, mod.codfilialto, mod.codtpatendo, " );
		sql.append( "mod.codempsa, mod.codfilialsa, mod.codsetat, ");
		sql.append( "mod.obsatendo, mod.obsinterno, mod.statusatendo, " );
		sql.append( "mod.codempcl, mod.codfilialcl, mod.codcli, mod.codempct, ");
		sql.append( "mod.codfilialct, mod.codcontr, mod.coditcontr, " );
		sql.append( "mod.codempca, mod.codfilialca, mod.codclasatendo," );
		sql.append( "mod.codempch, mod.codfilialch, mod.codchamado, "); 
		sql.append( "mod.codempea, mod.codfilialea, mod.codespec " );
		sql.append( "from atmodatendo mod " );
		sql.append( "where " );
		sql.append( "mod.codemp=? and mod.codfilial=? and mod.codmodel=? " );

		if (codmodel!= null) {
			codatendo = getSequencia(codemp, codfilial, "AT");		
			PreparedStatement ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( 1, codempmo );
			ps.setInt( 2, codfilialmo );
			ps.setInt( 3, codmodel );
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				result = new Atendimento();
				result.setCodemp( codemp );
				result.setCodfilial( codfilial );
				result.setCodempto( rs.getInt( "codempto" ) );
				result.setCodfilialto( rs.getInt( "codfilialto" ) );
				result.setCodtpatendo( rs.getInt( "codtpatendo" ) );
				result.setCodempsa( rs.getInt(  "codempsa" ) );
				result.setCodfilialsa( rs.getInt( "codfilialsa" ) );
				result.setCodsetat( rs.getInt("codsetat" ) );
				result.setObsatendo( rs.getString(  "obsatendo" ) );
				result.setObsinterno( rs.getString( "obsinterno" ) );
				result.setStatusatendo( rs.getString("statusatendo" ) );
				result.setCodempcl( rs.getInt( "codempcl" ) );
				result.setCodfilialcl( rs.getInt("codfilialcl"));
				result.setCodcli( rs.getInt( "codcli" ) );
				if ( rs.getString( "coditcontr" )!=null ) {
					result.setCodempct( rs.getInt("codempct") );
					result.setCodfilialct( rs.getInt( "codfilialct" ) );
					result.setCodcontr( rs.getInt( "codcontr" ) );
					result.setCoditcontr( rs.getInt( "coditcontr" ) );
				}
				result.setCodempca( rs.getInt( "codempca" ) );
				result.setCodfilialca( rs.getInt( "codfilialca" ) );
				result.setCodclasatendo( rs.getInt( "codclasatendo" ) );
				if ( rs.getString( "codchamado" )!=null ) {
					result.setCodempch( rs.getInt( "codempch" ) );
					result.setCodfilialch (rs.getInt( "codfilialch" ) );
					result.setCodchamado( rs.getInt( "codchamado" ) );
				}
				result.setCodempea( rs.getInt( "codempea" ) );
				result.setCodfilialea( rs.getInt( "codfilialea" ) );
				result.setCodespec( rs.getInt( "codespec" ) );
				result.setDocatendo( "0" );
				result.setConcluichamado( "N" );
			}

		}
		return result;
	}

	public int getTotInconsistencia(Vector<Vector<Object>> vexped, Vector<Vector<Object>> vatend) {
		int result = 0;
		String sit = null;
		for (Vector<Object> row: vexped) { 
			sit = ( String) row.elementAt( EColExped.SITREVEXPED.ordinal() );
			if ( (sit!=null) && (sit.charAt( 1 )== 'I')) {
				result ++;
			}
		}
		for (Vector<Object> row: vatend) {
			sit = ( String) row.elementAt( EColAtend.SITREVATENDO.ordinal() );
			if ( (sit!=null) && (sit.charAt( 1 )=='I' )) {
				result ++;
			}
		}
		return result;
	}

	public void gerarEstagio345(Vector<Vector<Object>> vatend, Integer codemp, Integer codfilial,
			Integer codempmo, Integer codfilialmo,
			Integer codempae, Integer codfilialae, Integer codatend,
			Integer codempus, Integer codfilialus, String idusu) throws SQLException {
		Atendimento atendimento = null;
		Object codmodel = null;
		Integer codatendo = null;
		String strcodmodel = null;
		Date dataatendo = null;
		Date dataatendofin = null;
		String horaini = null;
		String horafin = null;
		String sitrev = null;
		for (Vector<Object> row: vatend) {
			sitrev = (String) row.elementAt( EColAtend.SITREVATENDO.ordinal() );
			if ((sitrev.equals( EstagioCheck.E3I.getValueTab() )) || 
					(sitrev.equals( EstagioCheck.E4I.getValueTab() )) || 
					(sitrev.equals( EstagioCheck.E5I.getValueTab() ))) {
				codmodel = row.elementAt( EColAtend.CODMODEL.ordinal() );
				if ("".equals( codmodel )) {
					codmodel = null;
				} 
				horaini = getTimeString( row.elementAt( EColAtend.HORAINI.ordinal() ) );
				horafin = getTimeString( row.elementAt( EColAtend.HORAFIN.ordinal() ) );				
				if ( (codmodel==null) && (sitrev.equals( EstagioCheck.E3I.getValueTab() ) ) ) {
					codatendo = (Integer) row.elementAt( EColAtend.CODATENDO.ordinal() );
					updateHoraatendo( codemp, codfilial, codatendo, horaini, horafin ); 

				} else {
					dataatendo = Funcoes.strDateToDate( (String) row.elementAt( EColAtend.DATAATENDO.ordinal() ) ); 
					dataatendofin = Funcoes.strDateToDate( (String) row.elementAt( EColAtend.DATAATENDO.ordinal() ) );
					atendimento = loadModelAtend( codemp, codfilial, codempmo, codfilialmo, (Integer) codmodel );
					atendimento.setDataatendo( dataatendo );
					atendimento.setDataatendofin( dataatendofin );
					atendimento.setHoraatendo( horaini );
					atendimento.setHoraatendofin( horafin );
					atendimento.setCodempae( codempae );
					atendimento.setCodfilialae( codfilialae );
					atendimento.setCodatend( codatend );
					atendimento.setCodempus( codempus );
					atendimento.setCodfilialus( codfilialus );
					atendimento.setIdusu( idusu );

					insert(atendimento);
				}
			}
		}
	}

	public void insertIntervaloAtend(Integer codemp, Integer codfilial, 
			Date dataatendo, Date dataatendofin, 
			String horaini, String horafim,
			Integer codempae, Integer codfilialae, Integer codatend,
			Integer codempus, Integer codfilialus, String idusu) throws SQLException {

		Atendimento intervalo = loadModelAtend( codemp, codfilial, (Integer) prefs[PREFS.CODEMPMI.ordinal()], 
				(Integer) prefs[PREFS.CODFILIALMI.ordinal()], (Integer) prefs[PREFS.CODMODELMI.ordinal()] );
		intervalo.setDataatendo( dataatendo );
		intervalo.setDataatendofin( dataatendofin );
		intervalo.setHoraatendo( horaini );
		intervalo.setHoraatendofin( horafim );
		intervalo.setCodempae( codempae );
		intervalo.setCodfilialae( codfilialae );
		intervalo.setCodatend( codatend );
		intervalo.setCodempus( codempus );
		intervalo.setCodfilialus( codfilialus );
		intervalo.setIdusu( idusu );

		insert(intervalo);
	}

	public void insertIntervaloChegada(Integer codemp, Integer codfilial, 
			Date dataatendo, Date dataatendofin, 
			String horaini, String horafim,
			Integer codempae, Integer codfilialae, Integer codatend,
			Integer codempus, Integer codfilialus, String idusu) throws SQLException {

		Atendimento intervalo = loadModelAtend( codemp, codfilial, (Integer) prefs[PREFS.CODEMPME.ordinal()], 
				(Integer) prefs[PREFS.CODFILIALME.ordinal()], (Integer) prefs[PREFS.CODMODELME.ordinal()] );
		intervalo.setDataatendo( dataatendo );
		intervalo.setDataatendofin( dataatendofin );
		intervalo.setHoraatendo( horaini );
		intervalo.setHoraatendofin( horafim );
		intervalo.setCodempae( codempae );
		intervalo.setCodfilialae( codfilialae );
		intervalo.setCodatend( codatend );
		intervalo.setCodempus( codempus );
		intervalo.setCodfilialus( codfilialus );
		intervalo.setIdusu( idusu );

		insert(intervalo);
	}

	public void insertFaltaJustificada(Integer codemp, Integer codfilial, 
			Date dataatendo, Date dataatendofin, 
			String horaini, String horafim,
			Integer codempae, Integer codfilialae, Integer codatend,
			Integer codempus, Integer codfilialus, String idusu) throws SQLException {

		Atendimento falta = loadModelAtend( codemp, codfilial, (Integer) prefs[PREFS.CODEMPFJ.ordinal()], 
				(Integer) prefs[PREFS.CODFILIALFJ.ordinal()], (Integer) prefs[PREFS.CODMODELFJ.ordinal()] );
		falta.setCodemp( codemp );
		falta.setCodfilial( codfilial );
		falta.setDataatendo( dataatendo );
		falta.setDataatendofin( dataatendofin );
		falta.setHoraatendo( horaini );
		falta.setHoraatendofin( horafim );
		falta.setCodempae( codempae );
		falta.setCodfilialae( codfilialae );
		falta.setCodatend( codatend );
		falta.setCodempus( codempus );
		falta.setCodfilialus( codfilialus );
		falta.setIdusu( idusu );

		insert(falta);
	}

	public void insertFaltaInjustificada(Integer codemp, Integer codfilial, 
			Date dataatendo, Date dataatendofin, 
			String horaini, String horafim,
			Integer codempae, Integer codfilialae, Integer codatend,
			Integer codempus, Integer codfilialus, String idusu) throws SQLException {

		Atendimento intervalo = loadModelAtend( codemp, codfilial, (Integer) prefs[PREFS.CODEMPFI.ordinal()], 
				(Integer) prefs[PREFS.CODFILIALFI.ordinal()], (Integer) prefs[PREFS.CODMODELFI.ordinal()] );
		intervalo.setCodemp( codemp );
		intervalo.setCodfilial( codfilial );
		intervalo.setDataatendo( dataatendo );
		intervalo.setDataatendofin( dataatendofin );
		intervalo.setHoraatendo( horaini );
		intervalo.setHoraatendofin( horafim );
		intervalo.setCodempae( codempae );
		intervalo.setCodfilialae( codfilialae );
		intervalo.setCodatend( codatend );
		intervalo.setCodempus( codempus );
		intervalo.setCodfilialus( codfilialus );
		intervalo.setIdusu( idusu );

		insert(intervalo);
	}

	public int getAtendente(Integer Matempr){

		StringBuffer sql = new StringBuffer();
		StringBuffer where = new StringBuffer();
		int iRet = 0;
		try {

			sql.append( "select codatend from ATATENDENTE  "  );
			sql.append(" where  matempr = " + Matempr );

			PreparedStatement ps = getConn().prepareStatement( sql.toString() );
			ResultSet rs = ps.executeQuery();
			if ( rs.next() ) {
				iRet = rs.getInt( "Codatend" );
				return iRet;
			}				
			rs.close();
			ps.close();
			getConn().commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
		}

		return iRet;
	}

	public void setPrefs(Integer codemp, Integer codfilial) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = null;

		prefs = new Object[ Atendimento.PREFS.values().length];

		try {
			sql = new StringBuilder("select codempmi, codfilialmi, codmodelmi,  " );
			sql.append( "codempme, mi.descmodel descmodelmi, "); 
			sql.append( "codfilialme, codmodelme, me.descmodel descmodelme, tempomaxint, coalesce(tolregponto,20) tolregponto, "); 
			sql.append( "mi.codempea codempia, mi.codfilialea codfilialia, ea.codespec codespecia, ea.descespec descespecia , " );
			sql.append( "codempfi, codfilialfi, codmodelfi, fi.descmodel descmodelfi, " );
			sql.append( "codempfj, codfilialfj, codmodelfj, fj.descmodel descmodelfj, " );
			sql.append( "codempap, codfilialap, codmodelap, ap.descmodel descmodelap, p.BLOQATENDIMENTO, p.periodobloq, ");
			sql.append( "p.codmodelmc, p.controleacesatend , p.agendaobrigorc " );
			sql.append( "from sgprefere3 p " );
			sql.append( "left outer join atmodatendo mi " );
			sql.append( "on mi.codemp=p.codempmi and mi.codfilial=p.codfilialmi and mi.codmodel=p.codmodelmi ");
			sql.append( "left outer join atmodatendo me " );
			sql.append( "on me.codemp=p.codempme and me.codfilial=p.codfilialme and me.codmodel=p.codmodelme ");
			sql.append( "left outer join atespecatend ea " );
			sql.append( "on ea.codemp=mi.codempea and ea.codfilial=mi.codfilialea and ea.codespec=mi.codespec ");
			sql.append( "left outer join atmodatendo fi " );
			sql.append( "on fi.codemp=p.codempfi and fi.codfilial=p.codfilialfi and fi.codmodel=p.codmodelfi ");
			sql.append( "left outer join atmodatendo fj " );
			sql.append( "on fj.codemp=p.codempfj and fj.codfilial=p.codfilialfj and fj.codmodel=p.codmodelfj ");
			sql.append( "left outer join atmodatendo ap " );
			sql.append( "on ap.codemp=p.codempap and ap.codfilial=p.codfilialap and ap.codmodel=p.codmodelap ");
			sql.append( "where  p.codemp=? and p.codfilial=?" );

			ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( 1, codemp );
			ps.setInt( 2, codfilial );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				prefs[ PREFS.CODEMPMI.ordinal() ] = new Integer(rs.getInt( PREFS.CODEMPMI.toString() ));
				prefs[ PREFS.CODFILIALMI.ordinal() ] = new Integer(rs.getInt( PREFS.CODFILIALMI.toString() ));
				prefs[ PREFS.CODMODELMI.ordinal() ] = new Integer(rs.getInt( PREFS.CODMODELMI.toString() ));
				prefs[ PREFS.DESCMODELMI.ordinal() ] = rs.getString( PREFS.DESCMODELMI.toString() );
				prefs[ PREFS.CODEMPME.ordinal() ] = new Integer(rs.getInt( PREFS.CODEMPME.toString() ));
				prefs[ PREFS.CODFILIALME.ordinal() ] = new Integer(rs.getInt( PREFS.CODFILIALME.toString() ));
				prefs[ PREFS.CODMODELME.ordinal() ] = new Integer(rs.getInt( PREFS.CODMODELME.toString() ));
				prefs[ PREFS.DESCMODELME.ordinal() ] = rs.getString( PREFS.DESCMODELME.toString() );
				prefs[ PREFS.TEMPOMAXINT.ordinal() ] = new Integer(rs.getInt( PREFS.TEMPOMAXINT.toString() ));
				prefs[ PREFS.TOLREGPONTO.ordinal() ] = new Integer(rs.getInt( PREFS.TOLREGPONTO.toString() ));
				prefs[ PREFS.CODEMPIA.ordinal() ] = new Integer(rs.getInt( PREFS.CODEMPIA.toString() ));
				prefs[ PREFS.CODFILIALIA.ordinal() ] = new Integer(rs.getInt( PREFS.CODFILIALIA.toString() ));
				prefs[ PREFS.CODESPECIA.ordinal() ] = new Integer(rs.getInt( PREFS.CODESPECIA.toString() ));
				prefs[ PREFS.DESCESPECIA.ordinal() ] = rs.getString( PREFS.DESCESPECIA.toString() );
				if (rs.getString(  PREFS.CODMODELFI.toString() )!=null) {
					prefs[ PREFS.CODEMPFI.ordinal() ] = new Integer(rs.getInt( PREFS.CODEMPFI.toString() ));
					prefs[ PREFS.CODFILIALFI.ordinal() ] = new Integer( rs.getInt( PREFS.CODFILIALFI.toString() ));
					prefs[ PREFS.CODMODELFI.ordinal() ] = new Integer( rs.getInt(  PREFS.CODMODELFI.toString() ));
					prefs[ PREFS.DESCMODELFI.ordinal() ] = rs.getString( PREFS.DESCMODELFI.toString() );
				}
				if (rs.getString(  PREFS.CODMODELFJ.toString() )!=null) {
					prefs[ PREFS.CODEMPFJ.ordinal() ] = new Integer(rs.getInt( PREFS.CODEMPFJ.toString() ));
					prefs[ PREFS.CODFILIALFJ.ordinal() ] = new Integer( rs.getInt( PREFS.CODFILIALFJ.toString() ));
					prefs[ PREFS.CODMODELFJ.ordinal() ] = new Integer( rs.getInt(  PREFS.CODMODELFJ.toString() ));
					prefs[ PREFS.DESCMODELFJ.ordinal() ] = rs.getString( PREFS.DESCMODELFJ.toString() );
				}
				prefs[ PREFS.CODEMPAP.ordinal() ] = new Integer(rs.getInt( PREFS.CODEMPAP.toString() ));
				prefs[ PREFS.CODFILIALAP.ordinal() ] = new Integer( rs.getInt( PREFS.CODFILIALAP.toString() ));
				prefs[ PREFS.CODMODELAP.ordinal() ] = new Integer( rs.getInt(  PREFS.CODMODELAP.toString() ));
				prefs[ PREFS.DESCMODELAP.ordinal() ] = rs.getString( PREFS.DESCMODELAP.toString() );
				prefs[ PREFS.BLOQATENDIMENTO.ordinal() ] = rs.getString( PREFS.BLOQATENDIMENTO.toString() );
				prefs[ PREFS.PERIODOBLOQ.ordinal() ] = new Integer(rs.getInt( PREFS.PERIODOBLOQ.toString() ));
				prefs[ PREFS.CODMODELMC.ordinal() ] = new Integer(rs.getInt( PREFS.CODMODELMC.toString() ));
				prefs[ PREFS.CONTROLEACESATEND.ordinal() ] = new Boolean("S".equals(rs.getString( PREFS.CONTROLEACESATEND.toString())));
				prefs[ PREFS.AGENDAOBRIGORC.ordinal() ] = new Boolean("S".equals(rs.getString( PREFS.AGENDAOBRIGORC.toString())));
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

	public Object[] getPrefs() {
		return this.prefs;
	}

	// Retorna o primeiro ou o último lançamento, dependendo da requisão (Abertura ou Fechamento).
	public String getHoraPrimUltLanca(Integer codemp, Integer codfilial, 
			Date dataatendo, String horaini, String horafim,
			Integer codempae, Integer codfilialae, Integer codatend,
			String aftela) throws SQLException {
		String result = null;
		StringBuilder sql = new StringBuilder();
		sql.append( "select first 1 a.horaatendo, a.horaatendofin from atatendimento a ");
		sql.append( "where a.codemp=? and a.codfilial=? and a.dataatendo=? and ");
		sql.append( "a.codempae=? and a.codfilialae=? and a.codatend=? and " );
		if ("A".equals( aftela )) {
			sql.append( "a.horaatendo>=? " );
			sql.append( "order by a.dataatendo, a.horaatendo" );
		} else {
			sql.append( "a.horaatendofin<=? " );
			sql.append( "order by a.dataatendo desc, a.horaatendofin desc" );
		}
		PreparedStatement ps = getConn().prepareStatement( sql.toString() );
		//NONE, CODEMP, CODFILIAL, DATAATENDO, CODEMPAE, CODFILIALAE, CODATEND, HORAATENDO, HORAATENDOFIN}
		ps.setInt( PARAM_PRIM_LANCA.CODEMP.ordinal(), codemp );
		ps.setInt( PARAM_PRIM_LANCA.CODFILIAL.ordinal(), codfilial );
		ps.setDate( PARAM_PRIM_LANCA.DATAATENDO.ordinal(), Funcoes.dateToSQLDate( dataatendo ) );
		ps.setInt( PARAM_PRIM_LANCA.CODEMPAE.ordinal(), codempae );
		ps.setInt( PARAM_PRIM_LANCA.CODFILIALAE.ordinal(), codfilialae );
		ps.setInt( PARAM_PRIM_LANCA.CODATEND.ordinal(), codatend );
		if ("A".equals( aftela )) {
			ps.setTime( PARAM_PRIM_LANCA.HORAATENDO.ordinal(), Funcoes.strTimeToSqlTime( horaini, false ) );
		} else {
			ps.setTime( PARAM_PRIM_LANCA.HORAATENDO.ordinal(), Funcoes.strTimeToSqlTime( horafim, false ) );
		}
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			if ("A".equals( aftela )) {
				result = rs.getString( PARAM_PRIM_LANCA.HORAATENDO.toString() );
			} else {
				result = rs.getString( PARAM_PRIM_LANCA.HORAATENDOFIN.toString() );
			}
		}
		rs.close();
		ps.close();
		getConn().commit();
		return result;
	}

	public void insert(Atendimento atd) throws SQLException {

		StringBuilder sql = new StringBuilder();

		sql.append( "EXECUTE PROCEDURE ATATENDIMENTOIUSP(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" );

		PreparedStatement ps = getConn().prepareStatement( sql.toString() );

		ps.setString( PROC_IU.IU.ordinal(), "I" ); // Define o modo insert para a procedure
		ps.setInt( PROC_IU.CODEMP.ordinal() , atd.getCodemp() ); // Código da empresa
		ps.setInt( PROC_IU.CODFILIAL.ordinal(), atd.getCodfilial() ); // Código da filial

		if ( atd.getCodatendo() == null ) {
			ps.setInt( PROC_IU.CODATENDO.ordinal(), Types.INTEGER );
		}
		else {
			ps.setInt( PROC_IU.CODATENDO.ordinal() , atd.getCodatendo() ); // Código do atendimento
		}

		if ( atd.getCodtpatendo() == null) {
			ps.setNull( PROC_IU.CODEMPTO.ordinal(), Types.INTEGER ); // Código da empresa tipo atendimento
			ps.setNull( PROC_IU.CODFILIALTO.ordinal(), Types.INTEGER ); // Código da filial tipo atendimento
			ps.setNull( PROC_IU.CODTPATENDO.ordinal(), Types.INTEGER ); // Código tipo atendimento
		}
		else {
			ps.setInt( PROC_IU.CODEMPTO.ordinal(), atd.getCodempto() ); // Código da empresa tipo atendimento
			ps.setInt( PROC_IU.CODFILIALTO.ordinal(), atd.getCodfilialto() ); // Código da filial tipo atendimento
			ps.setInt( PROC_IU.CODTPATENDO.ordinal(), atd.getCodtpatendo() ); // // Código tipo atendimento
		}

		if ( atd.getCodatend() == null) {
			ps.setNull( PROC_IU.CODEMPAE.ordinal(), Types.INTEGER ); // Código da empresa atendente
			ps.setNull( PROC_IU.CODFILIALAE.ordinal(), Types.INTEGER ); // Código da filial atendente
			ps.setNull( PROC_IU.CODATEND.ordinal(), Types.INTEGER ); // Código atendente
		}
		else {
			ps.setInt( PROC_IU.CODEMPAE.ordinal(), atd.getCodempae() ); // Código da empresa atendente
			ps.setInt( PROC_IU.CODFILIALAE.ordinal(), atd.getCodfilialae() ); // Código da filial atendente
			ps.setInt( PROC_IU.CODATEND.ordinal(), atd.getCodatend() ); // Código atendente
		}

		if ( atd.getCodatend() == null) {
			ps.setNull( PROC_IU.CODEMPSA.ordinal(), Types.INTEGER ); // Código da empresa situação atendimento
			ps.setNull( PROC_IU.CODFILIALSA.ordinal(), Types.INTEGER ); // Código da filial do contrato
			ps.setNull( PROC_IU.CODSETOR.ordinal(), Types.INTEGER ); // Código do contrato
		}
		else {
			ps.setInt( PROC_IU.CODEMPSA.ordinal(), atd.getCodempae() ); // Código da empresa do contrato
			ps.setInt( PROC_IU.CODFILIALSA.ordinal(), atd.getCodfilialae() ); // Código da filial do contrato
			ps.setInt( PROC_IU.CODSETOR.ordinal() , atd.getCodsetat() ); // Setor de atendimento
		}

		if ( atd.getIdusu() == null ) {
			ps.setInt( PROC_IU.CODEMPUS.ordinal(), Types.INTEGER ); // Código Empresa Usuário
			ps.setInt( PROC_IU.CODFILIALUS.ordinal(), Types.INTEGER ); // Código Filial Usuário
			ps.setInt( PROC_IU.IDUSU.ordinal(), Types.INTEGER ); // Id Usuário
		}
		else
		{
			ps.setInt( PROC_IU.CODEMPUS.ordinal(), atd.getCodempus() ); // Código Empresa Usuário
			ps.setInt( PROC_IU.CODFILIALUS.ordinal(), atd.getCodfilialus() ); // Código Filial Usuário
			ps.setString( PROC_IU.IDUSU.ordinal(), atd.getIdusu() ); // Id Usuário
		}

		ps.setString( PROC_IU.DOCATENDO.ordinal() , atd.getDocatendo() ); // Nro. do atendimento
		ps.setDate( PROC_IU.DATAATENDO.ordinal() ,Funcoes.dateToSQLDate( atd.getDataatendo() ) ); // Data de inicio do atendimento
		ps.setDate( PROC_IU.DATAATENDOFIN.ordinal(), Funcoes.dateToSQLDate( atd.getDataatendofin() ) ); // Data final do atendimento
		ps.setTime( PROC_IU.HORAATENDO.ordinal() , Funcoes.strTimeToSqlTime( atd.getHoraatendo(), false ) ) ; // Hora inicial do atendimento
		ps.setTime( PROC_IU.HORAATENDOFIN.ordinal()	, Funcoes.strTimeToSqlTime( atd.getHoraatendofin(), false ) ) ; // Hora final do atendimento
		ps.setString( PROC_IU.OBSATENDO.ordinal(), atd.getObsatendo() ); // Descrição do atendimento

		if (atd.getObsinterno()==null) {
			ps.setNull( PROC_IU.OBSINTERNO.ordinal(), Types.CHAR );
		} else {
			ps.setString( PROC_IU.OBSINTERNO.ordinal(), atd.getObsinterno() ); // Observações internas
		}
		ps.setString( PROC_IU.CONCLUICHAMADO.ordinal(), atd.getConcluichamado() );

		ps.setString( PROC_IU.STATUSATENDO.ordinal(), atd.getStatusatendo() ); // Status atendimento

		if ( atd.getCodcli() == null) {
			ps.setNull( PROC_IU.CODEMPCL.ordinal(), Types.INTEGER ); // Código da empresa do contrato
			ps.setNull( PROC_IU.CODFILIALCL.ordinal(), Types.INTEGER ); // Código da filial do contrato
			ps.setNull( PROC_IU.CODCLI.ordinal(), Types.INTEGER ); // Código do contrato
		}
		else {
			ps.setInt( PROC_IU.CODEMPCL.ordinal(), atd.getCodempcl() ); // Código da empresa do cliente
			ps.setInt( PROC_IU.CODFILIALCL.ordinal(), atd.getCodfilialcl() ); // Código da filial do cliente
			ps.setInt( PROC_IU.CODCLI.ordinal() , atd.getCodcli() ); // Código do cliente
		} 

		if ( atd.getCodcontr() == null) {
			ps.setNull( PROC_IU.CODEMPCT.ordinal(), Types.INTEGER ); // Código da empresa do contrato
			ps.setNull( PROC_IU.CODFILIALCT.ordinal(), Types.INTEGER ); // Código da filial do contrato
			ps.setNull( PROC_IU.CODCONTR.ordinal(), Types.INTEGER ); // Código do contrato
			ps.setNull( PROC_IU.CODITCONTR.ordinal(), Types.INTEGER ); // Código do item do contrato
		}
		else {
			ps.setInt( PROC_IU.CODEMPCT.ordinal(), Aplicativo.iCodEmp ); // Código da empresa do contrato
			ps.setInt( PROC_IU.CODFILIALCT.ordinal(), Aplicativo.iCodFilialPad ); // Código da filial do contrato
			ps.setInt( PROC_IU.CODCONTR.ordinal(), atd.getCodcontr()  ); // Código do contrato
			ps.setInt( PROC_IU.CODITCONTR.ordinal(), atd.getCoditcontr()  ); // Código do ítem de contrato
		}

		if ( atd.getCodrec()== null ) {
			ps.setNull( PROC_IU.CODEMPIR.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODFILIALIR.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODREC.ordinal(), Types.INTEGER ); // Código do contas a receber
			ps.setNull( PROC_IU.NPARCITREC.ordinal(), Types.INTEGER ); // Código do ítem do contas a receber
		}
		else {
			ps.setInt( PROC_IU.CODEMPIR.ordinal(), atd.getCodempir() ); // Código do contas a receber
			ps.setInt( PROC_IU.CODFILIALIR.ordinal(), atd.getCodfilialir() ); // Código do ítem do contas a receber
			ps.setInt( PROC_IU.CODREC.ordinal(), atd.getCodrec() ); // Código do contas a receber
			ps.setInt( PROC_IU.NPARCITREC.ordinal(), atd.getNparcitrec() ); // Código do ítem do contas a receber
		}

		if ( atd.getCodchamado() == null ) {
			ps.setNull( PROC_IU.CODEMPCH.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODFILIALCH.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODCHAMADO.ordinal(), Types.INTEGER );
		}
		else {
			ps.setInt( PROC_IU.CODEMPCH.ordinal(), atd.getCodempch() ); // Código da empresa do chamado
			ps.setInt( PROC_IU.CODFILIALCH.ordinal(), atd.getCodfilialch() ); // Código da filial do chamado
			ps.setInt( PROC_IU.CODCHAMADO.ordinal() ,  atd.getCodchamado() ); // Código do chamado

		}

		if ( atd.getCodespec() == null  ) {
			ps.setNull( PROC_IU.CODEMPEA.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODFILIALEA.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODESPEC.ordinal(), Types.INTEGER );
		}
		else {
			ps.setInt( PROC_IU.CODEMPEA.ordinal(), atd.getCodempae() ); // Código da empresa do especificação
			ps.setInt( PROC_IU.CODFILIALEA.ordinal(),atd.getCodfilialae() ); // Código da filial da especificação
			ps.setInt( PROC_IU.CODESPEC.ordinal(), atd.getCodespec() ); // Código da especificação
		}

		if ( atd.getCodtarefa() == null  ) {
			ps.setNull( PROC_IU.CODEMPTA.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODFILIALTA.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODTAREFA.ordinal(), Types.INTEGER );
		}
		else {
			ps.setInt( PROC_IU.CODEMPTA.ordinal(), atd.getCodempta() ); // Código da empresa do especificação
			ps.setInt( PROC_IU.CODFILIALTA.ordinal(),atd.getCodfilialta() ); // Código da filial da especificação
			ps.setInt( PROC_IU.CODTAREFA.ordinal(), atd.getCodtarefa() ); // Código da especificação
		}

		if ( atd.getCodorc() == null  ) {
			ps.setNull( PROC_IU.CODEMPOC.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODFILIALOC.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.TIPOORC.ordinal(), Types.VARCHAR );
			ps.setNull( PROC_IU.CODORC.ordinal(), Types.INTEGER );
		}
		else {
			ps.setInt( PROC_IU.CODEMPOC.ordinal(), atd.getCodempoc() ); 
			ps.setInt( PROC_IU.CODFILIALOC.ordinal(),atd.getCodfilialoc() );
			ps.setString( PROC_IU.TIPOORC.ordinal(), atd.getTipoorc() ); 
			ps.setInt( PROC_IU.CODORC.ordinal(), atd.getCodorc() );
		}

		if ( atd.getSitatendo() == null  ) {
			ps.setNull( PROC_IU.SITATENDO.ordinal(), Types.CHAR );
		} else {
			ps.setString( PROC_IU.SITATENDO.ordinal(), atd.getSitatendo());
		}

		if (atd.getCodagd() == null) {
			ps.setNull( PROC_IU.CODEMPAG.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODFILIALAG.ordinal(), Types.SMALLINT);
			ps.setNull( PROC_IU.TIPOAGE.ordinal(), Types.CHAR );
			ps.setNull( PROC_IU.CODAGE.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODAGD.ordinal(), Types.INTEGER );
		} else {
			ps.setInt( PROC_IU.CODEMPAG.ordinal(), atd.getCodempag() ); 
			ps.setInt( PROC_IU.CODFILIALAG.ordinal(),atd.getCodfilialag() );
			ps.setString( PROC_IU.TIPOAGE.ordinal(), atd.getTipoage() ); 
			ps.setInt( PROC_IU.CODAGE.ordinal(), atd.getCodage() );
			ps.setInt( PROC_IU.CODAGD.ordinal(), atd.getCodagd() );
		}

		ps.execute();
		ps.close();

		getConn().commit();

	}

	public Integer getCodOrc(Integer codemp, Integer codfilial, Integer codatendo) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append( "select codorc from atatendimentoorc where codemp=? and codfilial=? and codatendo=? " );
		PreparedStatement ps = getConn().prepareStatement( sql.toString() );
		int param = 1;
		int codorc = 0;
		ps.setInt( param++, codemp );
		ps.setInt( param++, codfilial );
		ps.setInt( param++, codatendo );
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			codorc = rs.getInt( "codorc" );
		}

		return codorc;

	}

	public Integer getCodCliOrc(Integer codemp, Integer codfilial, String tipoorc, Integer codorc) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append( "select codcli from vdorcamento where codemp=? and codfilial=? and tipoorc=? and codorc=? " );
		PreparedStatement ps = getConn().prepareStatement( sql.toString() );
		int param = 1;
		int codcli = 0;
		ps.setInt( param++, codemp );
		ps.setInt( param++, codfilial );
		ps.setString( param++, tipoorc );
		ps.setInt( param++, codorc );
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			codcli = rs.getInt( "codcli" );
		}

		return codcli;

	}


	public void update(Atendimento atd) throws Exception {

		StringBuilder sql = new StringBuilder();

		sql.append( "EXECUTE PROCEDURE ATATENDIMENTOIUSP ");
		sql.append( "( ");
		for (int i=1; i<PROC_IU.values().length; i++) {
			if (i>1) {
				sql.append( ", ");
			}
			sql.append( "?" );
		}
		sql.append(" )");

		PreparedStatement ps = getConn().prepareStatement( sql.toString() );

		ps.setString( PROC_IU.IU.ordinal(), "U" ); // Define o modo insert para a procedure
		ps.setInt( PROC_IU.CODEMP.ordinal() , atd.getCodemp() ); // Código da empresa
		ps.setInt( PROC_IU.CODFILIAL.ordinal(), atd.getCodfilial() ); // Código da filial

		if ( atd.getCodatendo() == null ) {
			ps.setInt( PROC_IU.CODATENDO.ordinal(), Types.INTEGER );
		}
		else {
			ps.setInt( PROC_IU.CODATENDO.ordinal() , atd.getCodatendo() ); // Código do atendimento
		}


		if( atd.getCodtpatendo() == null ){
			ps.setNull( PROC_IU.CODEMPTO.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODFILIALTO.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODTPATENDO.ordinal(), Types.INTEGER );
		} 
		else {
			ps.setInt( PROC_IU.CODEMPTO.ordinal(), atd.getCodempto() );
			ps.setInt( PROC_IU.CODFILIALTO.ordinal(), atd.getCodfilialto() );
			ps.setInt( PROC_IU.CODTPATENDO.ordinal(), atd.getCodtpatendo() );
		}
		if ( atd.getCodatend() == null) {
			ps.setNull( PROC_IU.CODEMPAE.ordinal(), Types.INTEGER ); // Código da empresa atendente
			ps.setNull( PROC_IU.CODFILIALAE.ordinal(), Types.INTEGER ); // Código da filial atendente
			ps.setNull( PROC_IU.CODATEND.ordinal(), Types.INTEGER ); // Código atendente
		}
		else {
			ps.setInt( PROC_IU.CODEMPAE.ordinal(), atd.getCodempae() ); // Código da empresa atendente
			ps.setInt( PROC_IU.CODFILIALAE.ordinal(), atd.getCodfilialae() ); // Código da filial atendente
			ps.setInt( PROC_IU.CODATEND.ordinal(), atd.getCodatend() ); // Código atendente
		}


		ps.setString( PROC_IU.DOCATENDO.ordinal() , atd.getDocatendo() );
		ps.setDate( PROC_IU.DATAATENDO.ordinal(), Funcoes.dateToSQLDate( atd.getDataatendo() ) );
		ps.setDate( PROC_IU.DATAATENDOFIN.ordinal(), Funcoes.dateToSQLDate( atd.getDataatendofin() ) );
		ps.setTime( PROC_IU.HORAATENDO.ordinal(), Funcoes.strTimeToSqlTime( atd.getHoraatendo(), false) );
		ps.setTime( PROC_IU.HORAATENDOFIN.ordinal(), Funcoes.strTimeToSqlTime( atd.getHoraatendofin(), false ) );
		ps.setString( PROC_IU.OBSATENDO.ordinal(), atd.getObsatendo() );

		if ( atd.getCodcli() == null) {
			ps.setNull( PROC_IU.CODEMPCL.ordinal(), Types.INTEGER ); // Código da empresa do contrato
			ps.setNull( PROC_IU.CODFILIALCL.ordinal(), Types.INTEGER ); // Código da filial do contrato
			ps.setNull( PROC_IU.CODCLI.ordinal(), Types.INTEGER ); // Código do contrato
		}
		else {
			ps.setInt( PROC_IU.CODEMPCL.ordinal(), atd.getCodempcl() ); // Código da empresa do cliente
			ps.setInt( PROC_IU.CODFILIALCL.ordinal(), atd.getCodfilialcl() ); // Código da filial do cliente
			ps.setInt( PROC_IU.CODCLI.ordinal() , atd.getCodcli() ); // Código do cliente
		} 

		if( atd.getCodsetat() == null ){
			ps.setNull( PROC_IU.CODEMPSA.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODFILIALSA.ordinal(), Types.INTEGER );
			ps.setNull(  PROC_IU.CODSETOR.ordinal(), Types.INTEGER );
		} 
		else {
			ps.setInt( PROC_IU.CODEMPSA.ordinal(), atd.getCodempsa() );
			ps.setInt( PROC_IU.CODFILIALSA.ordinal(), atd.getCodfilialsa() );
			ps.setInt( PROC_IU.CODSETOR.ordinal(), atd.getCodsetat() );
		}

		if ( atd.getIdusu() == null ) {
			ps.setInt( PROC_IU.CODEMPUS.ordinal(), Types.INTEGER ); // Código Empresa Usuário
			ps.setInt( PROC_IU.CODFILIALUS.ordinal(), Types.INTEGER ); // Código Filial Usuário
			ps.setInt( PROC_IU.IDUSU.ordinal(), Types.INTEGER ); // Id Usuário
		}
		else
		{
			ps.setInt( PROC_IU.CODEMPUS.ordinal(), atd.getCodempus() ); // Código Empresa Usuário
			ps.setInt( PROC_IU.CODFILIALUS.ordinal(), atd.getCodfilialus() ); // Código Filial Usuário
			ps.setString( PROC_IU.IDUSU.ordinal(), atd.getIdusu() ); // Id Usuário
		}

		if ( atd.getCodrec()== null ) {
			ps.setNull( PROC_IU.CODEMPIR.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODFILIALIR.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODREC.ordinal(), Types.INTEGER ); // Código do contas a receber
			ps.setNull( PROC_IU.NPARCITREC.ordinal(), Types.INTEGER ); // Código do ítem do contas a receber
		}
		else {
			ps.setInt( PROC_IU.CODEMPIR.ordinal(), atd.getCodempir() ); // Código do contas a receber
			ps.setInt( PROC_IU.CODFILIALIR.ordinal(), atd.getCodfilialir() ); // Código do ítem do contas a receber
			ps.setInt( PROC_IU.CODREC.ordinal(), atd.getCodrec() ); // Código do contas a receber
			ps.setInt( PROC_IU.NPARCITREC.ordinal(), atd.getNparcitrec() ); // Código do ítem do contas a receber
		}

		if ( atd.getCodchamado() == null ) {
			ps.setNull( PROC_IU.CODEMPCH.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODFILIALCH.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODCHAMADO.ordinal(), Types.INTEGER );
		}
		else {
			ps.setInt( PROC_IU.CODEMPCH.ordinal(), atd.getCodempch() ); // Código da empresa do chamado
			ps.setInt( PROC_IU.CODFILIALCH.ordinal(), atd.getCodfilialch() ); // Código da filial do chamado
			ps.setInt( PROC_IU.CODCHAMADO.ordinal(), atd.getCodchamado() ); // Código do chamado
		}

		if ( atd.getCodcontr() == null ) {
			ps.setNull( PROC_IU.CODEMPCT.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODFILIALCT.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODCONTR.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODITCONTR.ordinal(), Types.INTEGER );
		}
		else {
			ps.setInt( PROC_IU.CODEMPCT.ordinal(), atd.getCodempct() );
			ps.setInt( PROC_IU.CODFILIALCT.ordinal(), atd.getCodfilialct() );
			ps.setInt( PROC_IU.CODCONTR.ordinal(), atd.getCodcontr() );
			ps.setInt( PROC_IU.CODITCONTR.ordinal(), atd.getCoditcontr() );
		}

		ps.setString( PROC_IU.STATUSATENDO.ordinal(), atd.getStatusatendo() );

		if (atd.getObsinterno()==null) {
			ps.setNull( PROC_IU.OBSINTERNO.ordinal(), Types.CHAR );
		} else {
			ps.setString( PROC_IU.OBSINTERNO.ordinal(), atd.getObsinterno() ); // Observações internas
		}

		ps.setString( PROC_IU.CONCLUICHAMADO.ordinal(), atd.getConcluichamado() );

		if ( atd.getCodespec() == null ) {
			ps.setNull( PROC_IU.CODEMPEA.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODFILIALEA.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODESPEC.ordinal(), Types.INTEGER );
		}
		else {

			ps.setInt( PROC_IU.CODEMPEA.ordinal(), atd.getCodempea() );
			ps.setInt(  PROC_IU.CODFILIALEA.ordinal(), atd.getCodfilialea() );
			ps.setInt( PROC_IU.CODESPEC.ordinal(), atd.getCodespec() );
		}

		if ( atd.getCodtarefa() == null  ) {
			ps.setNull( PROC_IU.CODEMPTA.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODFILIALTA.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODTAREFA.ordinal(), Types.INTEGER );
		}
		else {
			ps.setInt( PROC_IU.CODEMPTA.ordinal(), atd.getCodempta() ); 
			ps.setInt( PROC_IU.CODFILIALTA.ordinal(),atd.getCodfilialta() ); 
			ps.setInt( PROC_IU.CODTAREFA.ordinal(), atd.getCodtarefa() ); // Código da tarefa
		}

		if ( atd.getCodorc() == null  ) {
			ps.setNull( PROC_IU.CODEMPOC.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODFILIALOC.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.TIPOORC.ordinal(), Types.CHAR );
			ps.setNull( PROC_IU.CODORC.ordinal(), Types.INTEGER );
		}
		else {
			ps.setInt( PROC_IU.CODEMPOC.ordinal(), atd.getCodempoc());
			ps.setInt( PROC_IU.CODFILIALOC.ordinal(), atd.getCodfilialoc() );
			ps.setString( PROC_IU.TIPOORC.ordinal(), atd.getTipoorc() );
			ps.setInt( PROC_IU.CODORC.ordinal(), atd.getCodorc() );
		}

		if ( atd.getSitatendo() == null  ) {
			ps.setNull( PROC_IU.SITATENDO.ordinal(), Types.CHAR );
		} else {
			ps.setString( PROC_IU.SITATENDO.ordinal(), atd.getSitatendo());
		}

		if (atd.getCodagd() == null) {
			ps.setNull( PROC_IU.CODEMPAG.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODFILIALAG.ordinal(), Types.SMALLINT);
			ps.setNull( PROC_IU.TIPOAGE.ordinal(), Types.CHAR );
			ps.setNull( PROC_IU.CODAGE.ordinal(), Types.INTEGER );
			ps.setNull( PROC_IU.CODAGD.ordinal(), Types.INTEGER );
		} else {
			ps.setInt( PROC_IU.CODEMPAG.ordinal(), atd.getCodempag() ); 
			ps.setInt( PROC_IU.CODFILIALAG.ordinal(),atd.getCodfilialag() );
			ps.setString( PROC_IU.TIPOAGE.ordinal(), atd.getTipoage() ); 
			ps.setInt( PROC_IU.CODAGE.ordinal(), atd.getCodage() );
			ps.setInt( PROC_IU.CODAGD.ordinal(), atd.getCodagd() );
		}


		ps.executeUpdate();
		ps.close();

		getConn().commit();		
	}

	public String checarSitrevEstagio1234(final Vector<Vector<Object>> vexped, Vector<Vector<Object>> vatend) {
		String result = (String) EstagioCheck.EPE.getValue();
		//String temp = null;
		for (Vector<Object> row: vexped) {
			result = (String) row.elementAt( EColExped.SITREVEXPED.ordinal() );
			if ( EstagioCheck.EPE.getValueTab().equals(result) ) {
				result = (String) EstagioCheck.EPE.getValue();
			} else if ( EstagioCheck.E1O.getValueTab().equals(result) ) {
				result = (String) EstagioCheck.E1O.getValue();
			} else if ( EstagioCheck.E2O.getValueTab().equals(result) ) {
				result = (String) EstagioCheck.E2O.getValue();
			} else if ( EstagioCheck.E1I.getValueTab().equals(result) ) {
				result = (String) EstagioCheck.E1I.getValue();
				break;
			} else if ( EstagioCheck.E2I.getValueTab().equals(result) ) {
				result = (String) EstagioCheck.E2I.getValue();
				break;
			}
		}
		// Verifica se a posição 3 da string é igual "O" (Ok), então parte para verificar o vetor de atendimentos
		if (result.substring( 2 ).equals("O")) {
			for (Vector<Object> row: vatend) {
				result = (String) row.elementAt( EColAtend.SITREVATENDO.ordinal() );
				if ( EstagioCheck.EPE.getValueTab().equals(result) ) {
					result = (String) EstagioCheck.EPE.getValue();
				} else if ( EstagioCheck.E3O.getValueTab().equals(result) ) {
					result = (String) EstagioCheck.E3O.getValue();
				} else if (EstagioCheck.E3I.getValueTab().equals( result ) ) {
					result = (String)  EstagioCheck.E3I.getValue();
					break;
				} else if ( EstagioCheck.E4O.getValueTab().equals(result) ) {
					result = (String) EstagioCheck.E4O.getValue();
				} else if (EstagioCheck.E4I.getValueTab().equals( result ) ) {
					result = (String)  EstagioCheck.E4I.getValue();
					break;
				} else if (EstagioCheck.E5I.getValueTab().equals( result ) ) {
					result = (String)  EstagioCheck.E5I.getValue();
					break;
				}
			}
		}
		return result;
	}

	public String checarSitrevEstagio2(final Vector<Vector<Object>> vexped) {
		String result = (String) EstagioCheck.EPE.getValue();
		String temp = null;
		for (Vector<Object> row: vexped) {
			temp = (String) row.elementAt( EColExped.SITREVEXPED.ordinal() );
			if ( EstagioCheck.E1I.getValueTab().equals(temp) ) {
				result = (String) EstagioCheck.E1I.getValue();
				break;
			}
		}
		return result;
	}

	/*
	 * Este médoto irá retornar um vetor com as batidas a registrar 
	 */
	public Vector<Batida> getRegistroBatidas(Vector<Vector<Object>> vexped, int nbatidas) {
		Vector<Batida> result = new Vector<Batida>();
		Batida batida = null;
		int colini = EColExped.HFIMTURNO.ordinal() + nbatidas + 1;
		Date dtbat = null;
		String hbat = null;
		for (Vector<Object> row: vexped) {
			dtbat = Funcoes.strDateToDate( (String) row.elementAt( EColExped.DTEXPED.ordinal() ) );
			for (int i=colini; i<row.size(); i++) {
				hbat = (String) row.elementAt( i );
				// Se a hora a registrar não for nula nem em branco
				if ( ! ( (hbat==null) || (hbat.trim().equals( "" ) ) ) ) {
					batida = new Batida();
					batida.setDataponto( dtbat );
					batida.setHoraponto( hbat );
					result.addElement( batida );
				}
			}
		}
		return result;
	}

	public String checarSitrevAtend(final Vector<Vector<Object>> vatend) {
		// loop para checar estágio
		// Stágios: EPE = estágio pendente/sem nenhuma revisão
		//			E1I = estágio 1 situação (I) inconsistência
		//			E1O = estágio 1 situação (O) OK
		// Estágio 1, verifica a necessidade de intervalos entre atendimentos

		String sitrev = null;
		String sitrevant = null;
		for (Vector<Object> row: vatend) {
			sitrev = "E" + row.elementAt( EColAtend.SITREVATENDO.ordinal() );
			if ( EstagioCheck.EPE.getValue().equals( sitrev ) ) {
				break;
			} else if (sitrevant==null) {
				sitrevant=sitrev;
			} else if ( sitrevant.compareTo( sitrev ) > 0 ) {
				sitrevant=sitrev;
			} else if ( sitrev.compareTo( sitrevant ) > 0 ) {
				sitrev=sitrevant;
			}
		}
		return sitrev;
	}

	public String checarSitrevExped(final Vector<Vector<Object>> vexped) {
		// loop para checar estágio
		// Stágios: EPE = estágio pendente/sem nenhuma revisão
		//			E1I = estágio 1 situação (I) inconsistência
		//			E1O = estágio 1 situação (O) OK
		// Estágio 1, verifica a necessidade de intervalos entre atendimentos

		String sitrev = null;
		String sitrevant = null;
		for (Vector<Object> row: vexped) {
			sitrev = "E" + row.elementAt( EColExped.SITREVEXPED.ordinal() );
			if ( EstagioCheck.EPE.getValue().equals( sitrev ) ) {
				break;
			} else if (sitrevant==null) {
				sitrevant=sitrev;
			} else if ( sitrevant.compareTo( sitrev ) > 0 ) {
				sitrevant=sitrev;
			} else if ( sitrev.compareTo( sitrevant ) > 0 ) {
				sitrev=sitrevant;
			}
		}
		return sitrev;
	}

	public boolean checar(final Vector<Vector<Object>> vexped, final Vector<Vector<Object>> vatend, final int nbatidas) {
		boolean result = false;
		// Verifica o menor estágio da revisão
		String sitrev = checarSitrevExped(vexped); 
		if (EstagioCheck.EPE.getValue().equals( sitrev )) {
			result = checarEstagio1(vexped, vatend, nbatidas);
			if (!result) {
				result = checarEstagio2(vexped, nbatidas);
				if (!result) {
					result = checarEstagio3( vexped, vatend, nbatidas );
					if (!result) {
						result = checarEstagio4( vexped, vatend, nbatidas );
						if (!result) {
							result = checarEstagio5( vexped, vatend, nbatidas );
							//if (!result) {
							//result = checarEstagio6( vexped, vatend );
							//}

						}
					}
				}
			}
			//result = checarEstagio2(vatend);
		}
		return result;
	}

	// Atualiza lista de atendimentos com horário de batidas do início e fim do turno
	public boolean checarEstagio3(final Vector<Vector<Object>> vexped, final Vector<Vector<Object>> vatend, final int nbatidas) {
		boolean result = false;
		int posini = EColExped.HFIMTURNO.ordinal()+1;
		int numcols = posini + nbatidas;
		Vector<Object> atend = null;
		Vector<String> batidas = null;
		Vector<String> lanctos = null;
		Vector<Object[]> lanctosBatidas = null;
		String dtatend = null;

		for (Vector<Object> row: vexped) {
			batidas = getBatidas( row, posini, numcols );        	
			if ( (batidas!=null) && (batidas.size()>=4) ) {
				// Data de registro do ponto e atendimento
				dtatend = (String) row.elementAt( EColExped.DTEXPED.ordinal() );
				//Busca lancamentos para data do registro de ponto;
				lanctos = getHorariosLanctos( dtatend, vatend );
				if (lanctos.size()>0) {
					lanctosBatidas = getHorariosLanctosBatidas( batidas, lanctos );
					result = applyLanctoBatidas(vatend, nbatidas, lanctosBatidas, dtatend);
				}
			}
		}
		for (Vector<Object> row:vatend) {
			if (row.elementAt( EColAtend.SITREVATENDO.ordinal() ) .equals( EstagioCheck.EPE.getValueTab() )) {
				row.setElementAt( EstagioCheck.E3O.getValueTab(), EColAtend.SITREVATENDO.ordinal() );
				row.setElementAt( EstagioCheck.E3O.getImg(), EColAtend.SITREVATENDOIMG.ordinal() );
			}
		}
		return result;
	}

	private boolean applyLanctoBatidas(final Vector<Vector<Object>> vatend, 
			final int nbatidas, Vector<Object[]> lanctosBatidas, String dtatend) {
		Object[] lanctobatida = null;
		String inifinturno = null;
		String inifinturnoant = null;
		String horatemp1 = null;
		String horatemp2 = null;
		String dtatendant = null;
		long intervalo = 0;
		int intervalomin = 0;
		int intervaloant = 0;
		int posatend = -1;
		int posatendant = -1;
		boolean result = false;
		// tolerância de intervalo igual a 50% da tolerância de tempo para batida do ponto
		int tolintervalo = (Integer) prefs[PREFS.TOLREGPONTO.ordinal()]/2;

		if (lanctosBatidas.size()>0) {
			for (int lb=0; lb<lanctosBatidas.size(); lb++) {
				lanctobatida = lanctosBatidas.elementAt( lb );
				// Se o resto da divisão da posição da batida por 2 for maior que zero (impar), significa que é fechamento de turno
				if ( lb%2>0 ) {
					posatend = locateAtend(vatend, dtatend, (String) lanctobatida[COLBATLANCTO.LANCTO.ordinal()], true );
					inifinturno = INIFINTURNO.F.toString();
				} else {
					posatend = locateAtend(vatend, dtatend, (String) lanctobatida[COLBATLANCTO.LANCTO.ordinal()], false );
					inifinturno = INIFINTURNO.I.toString();
				}
				if (posatend>-1) {
					inifinturnoant = (String) vatend.elementAt( posatend ).elementAt( EColAtend.INIFINTURNO.ordinal() ); 
				}
				if ( (posatend>-1) && ("".equals( inifinturnoant )) ) {

					// Se encontrou o lançamento ajusta a coluna de batida
					horatemp1 = (String) lanctobatida[COLBATLANCTO.BATIDA.ordinal()];
					vatend.elementAt( posatend ).setElementAt( horatemp1, EColAtend.HORABATIDA.ordinal() );
					// Coloca flag indicado I - Início ou F - Final de turno
					vatend.elementAt( posatend ).setElementAt( inifinturno, EColAtend.INIFINTURNO.ordinal() );
					// Se for início de turno deverá recalcular o intervalo entre atendimentos
					horatemp2 = (String) lanctobatida[COLBATLANCTO.LANCTO.ordinal()];
					if (inifinturno.equals( INIFINTURNO.I.toString() )) {
						intervalo = Funcoes.subtraiTime( Funcoes.strTimeToSqlTime( horatemp1, false  ), Funcoes.strTimeToSqlTime( horatemp2, false ) );
					} else {
						intervalo = Funcoes.subtraiTime( Funcoes.strTimeToSqlTime( horatemp2, false  ), Funcoes.strTimeToSqlTime( horatemp1, false ) );         							
					}
					intervalomin = (int) intervalo / 1000 / 60;
					// Guarda o intervalo anterior para utilização abaixo
					intervaloant = (Integer) vatend.elementAt( posatend ).elementAt( EColAtend.INTERVATENDO.ordinal() );
					vatend.elementAt( posatend ).setElementAt( new Integer(intervalomin), EColAtend.INTERVATENDO.ordinal() );     							
					if ( intervalomin==0 ) {
						if ( (inifinturno.equals( INIFINTURNO.F.toString() )) && (intervaloant>0) ) {
							vatend.elementAt( posatend ).setElementAt( new Integer(intervaloant), EColAtend.INTERVATENDO.ordinal() ); 							
						}
					} else {

						result = true;
						vatend.elementAt( posatend ).setElementAt( EstagioCheck.E3I.getValueTab(), EColAtend.SITREVATENDO.ordinal() );
						vatend.elementAt( posatend ).setElementAt( EstagioCheck.E3I.getImg(), EColAtend.SITREVATENDOIMG.ordinal() );

						//horatemp1 = (String) vatend.elementAt( posatend ).elementAt( EColAtend.HORAATENDO.ordinal() );
						//horatemp2 = (String) vatend.elementAt( posatend ).elementAt( EColAtend.HORAATENDOFIN.ordinal() );

						// Testar quando o intervalo for negativo e não existir lançamento anterior
						if ( intervalomin<0 ) {
							posatendant = posatend - 1;
							if (posatendant>-1) {
								dtatendant = (String) vatend.elementAt( posatendant ).elementAt( EColAtend.DATAATENDO.ordinal() );
								if (dtatend.equals( dtatendant )) {
									inifinturnoant = (String) vatend.elementAt( posatendant ).elementAt( EColAtend.INIFINTURNO.ordinal() );
									if ( ( (inifinturno.equals( INIFINTURNO.F.toString() ) ) && 
											(INIFINTURNO.I.toString().equals( inifinturnoant )) ) ||
											( (inifinturno.equals( INIFINTURNO.I.toString() ) ) ) && 
											( (!INIFINTURNO.F.toString().equals( inifinturnoant )) ) ) {
										intervalomin = intervalomin * -1;
									}
								}
							}
						}
						if (intervalomin > 0) {

							// Caso o intervalo seja maior o a tolerância
							if (intervalomin>tolintervalo) {
								// Intervalo igula a tolerância
								intervalomin = tolintervalo;
								vatend.elementAt( posatend ).setElementAt( new Integer(intervalomin), EColAtend.INTERVATENDO.ordinal() );
								// Verificação do lançamento
								if ( inifinturno.equals( INIFINTURNO.I.toString() ) ) {
									horatemp2 = Funcoes.longTostrTime( Funcoes.somaTime( Funcoes.strTimeToSqlTime( horatemp1, false ),
											Funcoes.strTimeToSqlTime( 
													Funcoes.longTostrTime( (long) intervalomin * 1000 * 60 ) , false ) ) );
								} else {
									horatemp2 = Funcoes.longTostrTime( Funcoes.subtraiTime( Funcoes.strTimeToSqlTime( 
											Funcoes.longTostrTime( (long) intervalomin * 1000 * 60 ) , false), 
											Funcoes.strTimeToSqlTime( horatemp1, false )) );	     	 									
								}
							}

							vatend.elementAt( posatend ).setElementAt( prefs[PREFS.CODMODELME.ordinal()], EColAtend.CODMODEL.ordinal() );
							vatend.elementAt( posatend ).setElementAt( prefs[PREFS.DESCMODELME.ordinal()], EColAtend.DESCMODEL.ordinal() );

							horatemp1 = Funcoes.copy( horatemp1, 5 );
							horatemp2 = Funcoes.copy( horatemp2, 5 );

							if (inifinturno.equals( INIFINTURNO.I.toString() )) {
								vatend.elementAt( posatend ).setElementAt( horatemp1 , EColAtend.HORAINI.ordinal() );
								vatend.elementAt( posatend ).setElementAt( horatemp2 , EColAtend.HORAFIN.ordinal() );
							} else {
								vatend.elementAt( posatend ).setElementAt( horatemp2 , EColAtend.HORAINI.ordinal() );
								vatend.elementAt( posatend ).setElementAt( horatemp1 , EColAtend.HORAFIN.ordinal() );
							}     					
						} else {
							horatemp1 = (String) vatend.elementAt( posatend ).elementAt( EColAtend.HORAATENDO.ordinal() );
							horatemp2 = (String) vatend.elementAt( posatend ).elementAt( EColAtend.HORAATENDOFIN.ordinal() );
							if ( inifinturno.equals( INIFINTURNO.I.toString() ) ) {
								horatemp1 = (String) vatend.elementAt( posatend ).elementAt( EColAtend.HORABATIDA.ordinal() );
							} else {
								horatemp2 = (String) vatend.elementAt( posatend ).elementAt( EColAtend.HORABATIDA.ordinal() );
							}
							vatend.elementAt( posatend ).setElementAt( horatemp1 , EColAtend.HORAINI.ordinal() );
							vatend.elementAt( posatend ).setElementAt( horatemp2 , EColAtend.HORAFIN.ordinal() ); 
						}
					}
				}
			}
		}   	
		return result;
	}

	private int locateAtend(Vector<Vector<Object>> vatend, String dataatendo, String horaatendo, boolean atendfin ) {
		int result = -1;
		String hora = null;
		String data = null;
		for (int i=0; i<vatend.size(); i++) {
			data = (String) vatend.elementAt( i ).elementAt( EColAtend.DATAATENDO.ordinal() );
			// Caso seja para verificar o horário final
			if (atendfin) {
				hora = (String) vatend.elementAt( i ).elementAt( EColAtend.HORAATENDOFIN.ordinal() );
			} else {
				hora = (String) vatend.elementAt( i ).elementAt( EColAtend.HORAATENDO.ordinal() );
			}
			if ( (horaatendo.equals(hora )) && (dataatendo.equals(data )) ) {
				result = i;
				break;
			}
		}
		return result;
	}

	public boolean checarEstagio2(final Vector<Vector<Object>> vexped, final int nbatidas) {
		boolean result = false;
		int posini = EColExped.HFIMTURNO.ordinal()+1;
		int numcols = posini + nbatidas;
		Vector<String> batidas = null;
		long intervalo1 = 0;
		long intervalo2 = 0;
		float intervhoras1 = 0;
		float intervhoras2 = 0;
		String tempo = "";
		float TEMPOMAXTURNO = 6f;

		for (Vector<Object> row: vexped) {
			batidas = getBatidas( row, posini, numcols );        	
			if ( (batidas!=null) && (batidas.size()>=4) ) {
				//Verifica o intervalor entre batidas
				intervalo1 = Funcoes.subtraiTime(
						Funcoes.strTimeToSqlTime( getTimeString( batidas.elementAt( COLBAT.INITURNO.ordinal() ) ), false ), 
						Funcoes.strTimeToSqlTime( getTimeString( batidas.elementAt( COLBAT.INIINTTURNO.ordinal() ) ), false )
						);
				// Calcula o intervalo em horas
				intervhoras1 = intervalo1 / 1000f / 60f / 60f;

				intervalo2 = Funcoes.subtraiTime(
						Funcoes.strTimeToSqlTime( getTimeString( (batidas.elementAt( COLBAT.FININTTURNO.ordinal() ) ) ) , false  ), 
						Funcoes.strTimeToSqlTime( getTimeString( (batidas.elementAt( COLBAT.FINTURNO.ordinal() ) ) ) , false  )
						);
				intervhoras2 = intervalo2 / 1000f / 60f / 60f;
				if ( (intervhoras1>TEMPOMAXTURNO) || (intervhoras2>TEMPOMAXTURNO) ) {
					if ( intervhoras1>TEMPOMAXTURNO ) {
						tempo = Funcoes.longTostrTime( intervalo1 );
						row.setElementAt( tempo, numcols );
					}
					if ( intervhoras2>TEMPOMAXTURNO ) {
						tempo = Funcoes.longTostrTime( intervalo2 );
						row.setElementAt( tempo, numcols + 1 );
					}
					row.setElementAt( EstagioCheck.E2I.getImg(), EColExped.SITREVEXPEDIMG.ordinal() );
					row.setElementAt( EstagioCheck.E2I.getValueTab(), EColExped.SITREVEXPED.ordinal() );
					result = true;
				}
			}
			if (!result) {
				row.setElementAt( EstagioCheck.E2O.getImg(), EColExped.SITREVEXPEDIMG.ordinal() );
				row.setElementAt( EstagioCheck.E2O.getValueTab(), EColExped.SITREVEXPED.ordinal() );
			}
		}
		return result;
	}

	public boolean checarEstagio1(final Vector<Vector<Object>> vexped, final Vector<Vector<Object>> vatend, int nbatidas) {
		boolean result = false;
		Vector<Object> row = null;
		int numcols = 0;
		int qtdant = 0;
		String dtexped = null;
		Vector<String> batidas = null;
		Vector<String> turno = null;
		Vector<String> turnosembatida = null;
		Vector<String> hlanctos = null;
		Vector<String> hlanctosturno = null;
		qtdant = EColExped.HFIMTURNO.ordinal()+1;
		numcols = qtdant + nbatidas;
		for (int r=0; r<vexped.size(); r++) {
			row = vexped.elementAt( r );
			dtexped = (String) row.elementAt( EColExped.DTEXPED.ordinal() );
			// Retorna vetor com informações do turno de trabalho
			turno = getTurno( getTimeString( row.elementAt( EColExped.HINITURNO.ordinal() ) ),
					getTimeString( row.elementAt( EColExped.HINIINTTURNO.ordinal() ) ),
					getTimeString( row.elementAt( EColExped.HFIMINTTURNO.ordinal() ) ),
					getTimeString( row.elementAt( EColExped.HFIMTURNO.ordinal() ) ) );
			// retorna as batidas registradas
			batidas = getBatidas(row, qtdant, numcols);
			// caso o número de batidas seja menor que o esperado, buscar informações.
			if (batidas.size()<turno.size()) {
				row.setElementAt( EstagioCheck.E1I.getImg(), EColExped.SITREVEXPEDIMG.ordinal() );
				row.setElementAt( EstagioCheck.E1I.getValueTab(), EColExped.SITREVEXPED.ordinal() );
				// Verifica os horários do truno sem batidas
				turnosembatida = getTurnosembatida( batidas, turno);
				hlanctos = getHorariosLanctos(dtexped, vatend );
				// Cruza turno sem batidas com lançamentos
				hlanctosturno = getHorariosLanctosTurno(turnosembatida, hlanctos);
				// Salva no vetor os horários com lançamentos
				result = setHorariosLanctos(row, numcols, hlanctosturno );
			} else {
				row.setElementAt( EstagioCheck.E1O.getImg(), EColExped.SITREVEXPEDIMG.ordinal() );
				row.setElementAt( EstagioCheck.E1O.getValueTab(), EColExped.SITREVEXPED.ordinal() );
			}
		}
		return result;
	}

	public static String getTimeString(Object hora) {
		return Funcoes.copy((String) hora,5);
	}

	private boolean setHorariosLanctos(final Vector<Object> row, int numcols, Vector<String> hlanctos) {
		boolean result = false;
		int tot = numcols + hlanctos.size();
		int pos = 0;
		for (int i=numcols; i<tot; i++) {
			row.setElementAt( hlanctos.elementAt( pos ), i );
			result = true;
			pos ++;
		}
		return result;
	}

	private Vector<Object[]> getHorariosLanctosBatidas(Vector<String> batidas, Vector<String> lanctos) {
		Vector<Object[]> result = new Vector<Object[]>();
		Vector<Object[]> lanctosbatidas = new Vector<Object[]>();
		Vector<Object[]> temp = new Vector<Object[]>();
		Vector<Object[]> resulttmp = new Vector<Object[]>();
		Object[] batlancto = null;
		Object[] batlanctotmp = null;
		String hlancto = null;
		String hbatida = null;
		int posdif = -1;
		int posmin = -1;
		long dif = 0;
		long difant = 0;
		int iniloop = 0;
		// Cria vetor com batidas x lançamentos
		for ( int i=0; i<batidas.size(); i++) {
			hbatida = batidas.elementAt( i );
			for ( int t=0; t<lanctos.size(); t++ ) {
				hlancto = lanctos.elementAt( t );
				dif = Funcoes.subtraiTime( Funcoes.strTimeToSqlTime( hlancto, false ), Funcoes.strTimeToSqlTime( hbatida, false ));
				dif = dif / 1000 / 60;
				if (dif<0) {
					dif = dif * -1;
				}
				/*   			if (dif==0) {
    				System.out.println("Batida: "+hbatida);
    				System.out.println("Lancto: "+hlancto);
    			} */
				batlancto = new Object[COLBATLANCTO.values().length];
				batlancto[COLBATLANCTO.BATIDA.ordinal()] = hbatida;
				batlancto[COLBATLANCTO.LANCTO.ordinal()] = hlancto;
				batlancto[COLBATLANCTO.DIF.ordinal()] = new Long(dif);
				batlancto[COLBATLANCTO.POS.ordinal()] = new Integer(i);
				lanctosbatidas.addElement( batlancto );
			}
		}
		// Clonar lançamentos x batidas
		for ( int i=0; i<lanctosbatidas.size(); i++ ) {
			temp.addElement( lanctosbatidas.elementAt( i ) );
		}

		// Faz um loop até que o temp não contenha elementos
		while (temp.size()>0) {
			posdif = -1;
			difant = 0;
			for (int i=0; i<temp.size(); i++ ) {
				batlancto = temp.elementAt( i );
				hbatida = (String) batlancto[COLBATLANCTO.BATIDA.ordinal()];
				hlancto = (String) batlancto[COLBATLANCTO.LANCTO.ordinal()];
				dif = (Long) batlancto[COLBATLANCTO.DIF.ordinal()];
				if (posdif==-1) {
					posdif = i;
				} else {
					difant = (Long) temp.elementAt(posdif )[COLBATLANCTO.DIF.ordinal()];
					if ( dif<difant ) {
						posdif = i;
					}
				}
			}
			if (posdif>-1) {
				batlancto = temp.elementAt( posdif );
				resulttmp.add( batlancto );
				hbatida = (String) batlancto[COLBATLANCTO.BATIDA.ordinal()];
				hlancto = (String) batlancto[COLBATLANCTO.LANCTO.ordinal()];
				temp.removeElement( batlancto );
				iniloop = temp.size()-1;
				for (int i=iniloop; ( (i>=0) && (temp.size()>0) ); i--) {
					batlanctotmp = temp.elementAt( i );
					if ( (hbatida.equals( batlanctotmp[COLBATLANCTO.BATIDA.ordinal()] )) ||
							(hlancto.equals( batlanctotmp[COLBATLANCTO.LANCTO.ordinal()] )) ) {
						temp.removeElement( batlanctotmp );
					}
				}
			}

		}

		// Rotinas para ordenar o vetor de retorno
		iniloop = resulttmp.size();
		while (result.size()!=iniloop) {
			posmin = -1;
			for (int i=0; i<resulttmp.size(); i++) {
				batlancto = resulttmp.elementAt( i );
				hbatida = (String) batlancto[COLBATLANCTO.BATIDA.ordinal()];
				hlancto = (String) batlancto[COLBATLANCTO.LANCTO.ordinal()];
				if  (posmin == -1) { 
					posmin = i;
				} else {
					batlanctotmp = resulttmp.elementAt( posmin );
					if (hbatida.compareTo( (String) batlanctotmp[COLBATLANCTO.BATIDA.ordinal()] )<0) {
						posmin = i;
					}
				}
			}
			if (posmin!=-1) {
				batlanctotmp = resulttmp.elementAt( posmin );
				result.addElement( batlanctotmp );
				resulttmp.removeElementAt( posmin );
			}
		}

		return result;
	}

	private Vector<String> getHorariosLanctosTurno(Vector<String> turnosembatida, Vector<String> hlanctos) {
		Vector<String> result = new Vector<String>();
		Vector<String> temp = new Vector<String>();
		String hbat = null;
		String hturno = null;
		int posdif = -1;
		long dif = 0;
		long difant = 0;
		// Clonar lançamentos
		for ( int i=0; i<hlanctos.size(); i++ ) {
			temp.add( hlanctos.elementAt( i ) );
		}
		// Comparar atendimentos com horário do turno sem batidas
		for ( int i=0; i<turnosembatida.size(); i++) {
			hturno = turnosembatida.elementAt( i );
			posdif = -1;
			for ( int t=0; t<temp.size(); t++ ) {
				hbat = temp.elementAt( t );
				dif = Funcoes.subtraiTime( Funcoes.strTimeToSqlTime( hbat, false ), Funcoes.strTimeToSqlTime( hturno, false ));
				if (dif<0) {
					dif = dif * -1;
				}
				if (posdif==-1) {
					difant = dif;
					posdif = t;
				} else if (dif<difant) {
					difant = dif;
					posdif = t;
				}
			}
			if (posdif>-1) {
				result.addElement( temp.elementAt(posdif) );
				temp.remove( posdif );
			}
		}
		return result;
	}

	private Vector<String> getHorariosLanctos(String dtatend, Vector<Vector<Object>> vatend) {
		Vector<String> result = new Vector<String>();
		Vector<Object> row = null;
		//Vector<Object> rowant = null;
		int pos = primeiroAtend( vatend, dtatend );
		String dataatendo = null;
		String horaatendo = null;
		String horaatendofin = null;
		String horaatendofinant = null;
		int intervalo = 0;
		if (pos>-1) {
			// Verifica o primeiro lançamento na data e grava como possível horário para ponto
			row = vatend.elementAt( pos );
			dataatendo = (String) row.elementAt( EColAtend.DATAATENDO.ordinal() );
			horaatendo = (String) row.elementAt( EColAtend.HORAATENDO.ordinal() );
			horaatendofin = (String) row.elementAt( EColAtend.HORAATENDOFIN.ordinal() );
			result.add( horaatendo );
			pos++;
			// loop enquando for a data a pesquisar
			while ( (pos<vatend.size()) && (dtatend.equals( dataatendo )) ) {
				// hora final do atendimento anterior
				horaatendofinant = (String) row.elementAt( EColAtend.HORAATENDOFIN.ordinal() );
				row = vatend.elementAt( pos );
				dataatendo = (String) row.elementAt( EColAtend.DATAATENDO.ordinal() );
				horaatendo = (String) row.elementAt( EColAtend.HORAATENDO.ordinal() );
				horaatendofin = (String) row.elementAt( EColAtend.HORAATENDOFIN.ordinal() );
				intervalo = (Integer) row.elementAt( EColAtend.INTERVATENDO.ordinal() );
				// Se tiver intervalo adiciona a hora final anterior e a hora inicial do lançamento atual
				if (!dtatend.equals( dataatendo )) {
					result.add( horaatendofinant );
				} else if (intervalo>0) {
					result.add( horaatendofinant );
					result.add( horaatendo );
				}
				pos++;
			}
			// adiciona o horário final para fechar o turno de trabalho
			result.add( horaatendofin );
		}
		return result;
	}

	private Vector<String> getTurno( String hiniturno, String hiniintturno, String hfimintturno, String hfimturno ) {
		Vector<String> result = new Vector<String>();
		if (hiniintturno.equals( hfimintturno )) { // verifica se é meio período
			result.add( hiniturno );
			result.add( hfimturno );
		} else {
			result.add( hiniturno );
			result.add( hiniintturno );
			result.add( hfimintturno );
			result.add( hfimturno );
		} 	
		return result;
	}

	private Vector<String> getTurnosembatida(Vector<String> batidas, Vector<String> turnos) {
		Vector<String> result = new Vector<String>();
		String hbat = null;
		String hturno = null;
		int posdif = -1;
		long dif = 0;
		long difant = 0;
		// Clonar turnos
		for ( int i=0; i<turnos.size(); i++ ) {
			result.add( turnos.elementAt( i ) );
		}
		// Comparar batidas com horários do turno de trabalho
		for ( int i=0; i<batidas.size(); i++) {
			hbat = batidas.elementAt( i );
			posdif = -1;
			for ( int t=0; t<result.size(); t++ ) {
				hturno = result.elementAt( t );
				dif = Funcoes.subtraiTime( Funcoes.strTimeToSqlTime( hturno, false ), Funcoes.strTimeToSqlTime( hbat, false ));
				if (dif<0) {
					dif = dif * -1;
				}
				if (posdif==-1) {
					difant = dif;
					posdif = t;
				} else if (dif<difant) {
					difant = dif;
					posdif = t;
				}
			}
			if (posdif>-1) {
				result.remove( posdif );
			}
		}
		return result;
	}

	private Vector<String> getBatidas(Vector<Object> row, int posini, int numcols) {
		Vector<String> result = new Vector<String>();
		String hbat = null;
		if (row!=null) {
			for (int i=posini; i<numcols; i++ ) {
				hbat = getTimeString((String) row.elementAt( i ));

				if ( (hbat != null ) && ( ! "".equals( hbat.trim() ) )) {
					result.add( hbat );
				}
			}
		}
		return result;
	}

	public boolean checarEstagio5(final Vector<Vector<Object>>  vexped, final Vector<Vector<Object>>  vatend, final int nbatidas) {
		boolean result = false;
		Integer codmodel = (Integer) prefs[PREFS.CODMODELAP.ordinal()];
		String descmodel = (String) prefs[PREFS.DESCMODELAP.ordinal()];
		String dtatendo = null;
		String horaini = null;
		String horafin = null;
		String horaintervalo = null;
		String horaatendo = null;
		int totalmin = 0;
		int intervalo = 0;
		// Abre um loop até o final do vetor de atendimentos
		for (Vector<Object> row: vatend) {
			dtatendo = (String) row.elementAt( EColAtend.DATAATENDO.ordinal() );
			horaatendo = (String) row.elementAt( EColAtend.HORAATENDO.ordinal() );
			intervalo = (Integer) row.elementAt( EColAtend.INTERVATENDO.ordinal() );
			// Se o horário estiver fora do turno de trabalho não deve compor intervalo
			if ( ! verificHorarioTurno( vexped, dtatendo, horaatendo, nbatidas )) {
				intervalo = 0;
			}
			// Se foi detectado que existe um intervalo sem lançamentos
			if (intervalo>0) {
				// Transforma intervalo em minutos para string de time
				horaintervalo = Funcoes.longTostrTime( (long) intervalo * 1000 * 60 );
				horafin = Funcoes.copy( (String) row.elementAt( EColAtend.HORAATENDO.ordinal() ), 5);
				horaini = Funcoes.copy( Funcoes.longTostrTime( 
						Funcoes.subtraiTime( Funcoes.strTimeToSqlTime( horaintervalo, false ),
								Funcoes.strTimeToSqlTime( horafin, false )
								)
						), 5);
				row.setElementAt( horaini, EColAtend.HORAINI.ordinal() );
				row.setElementAt( horafin, EColAtend.HORAFIN.ordinal() );
				row.setElementAt( EstagioCheck.E5I.getValueTab(), EColAtend.SITREVATENDO.ordinal() );
				row.setElementAt( EstagioCheck.E5I.getImg(), EColAtend.SITREVATENDOIMG.ordinal() );
				row.setElementAt( codmodel , EColAtend.CODMODEL.ordinal() );
				row.setElementAt( descmodel, EColAtend.DESCMODEL.ordinal() );

				result = true;
			}
			else {
				row.setElementAt( EstagioCheck.E5O.getValueTab(), EColAtend.SITREVATENDO.ordinal() );
				row.setElementAt( EstagioCheck.E5O.getImg(), EColAtend.SITREVATENDOIMG.ordinal() );
			}
		}
		return result;
	}    

	public boolean checarEstagio6(final Vector<Vector<Object>>  vexped, final Vector<Vector<Object>>  vatend) {
		boolean result = false;
		Integer codmodel = (Integer) prefs[PREFS.CODMODELAP.ordinal()];
		String descmodel = (String) prefs[PREFS.DESCMODELAP.ordinal()];
		String dataatendo = null;
		String dataatendoant = null;
		String horaini = null;
		String horafin = null;
		String horaintervalo = null;
		String horaatendo = null;
		String inifinatendo = null;
		String inifinatendoant = null;
		Vector<Object> rowant = null;
		// Abre um loop até o final do vetor de atendimentos
		for (Vector<Object> row: vatend) {
			if ( rowant==null ) {
				rowant=row;
			} else {
				dataatendo = (String) row.elementAt( EColAtend.DATAATENDO.ordinal() );
				inifinatendo = (String) row.elementAt( EColAtend.INIFINTURNO.ordinal() );
				dataatendoant = (String) rowant.elementAt( EColAtend.DATAATENDO.ordinal() );
				inifinatendoant = (String) rowant.elementAt( EColAtend.INIFINTURNO.ordinal() );
				if ( ( inifinatendoant.equals( INIFINTURNO.F ) && (inifinatendo.equals( INIFINTURNO.I )) ) || 
						( inifinatendoant.equals( "" ) && (inifinatendo.equals( INIFINTURNO.F )) ) ) {
					row.setElementAt( EstagioCheck.E6O.getValueTab() , EColAtend.SITREVATENDO.ordinal() );
					row.setElementAt( EstagioCheck.E6O.getImg() , EColAtend.SITREVATENDOIMG.ordinal() );
				} else {
					row.setElementAt( EstagioCheck.E6I.getValueTab() , EColAtend.SITREVATENDO.ordinal() );
					row.setElementAt( EstagioCheck.E6I.getImg() , EColAtend.SITREVATENDOIMG.ordinal() );
				}
			}
		}
		return result;
	}    

	private boolean verificHorarioTurno(Vector<Vector<Object>> vexped, String data, String hora, int nbatidas) {
		boolean result = true;
		String hini = null;
		String hfin = null;
		int posini = EColExped.HFIMTURNO.ordinal()+1;
		int numcols = posini + nbatidas;
		Vector<String> batidas = null;
		Vector<Object> rowbat = null;
		for (Vector<Object> row: vexped) {
			if (row.elementAt( EColExped.DTEXPED.ordinal() ).equals( data )) {
				rowbat = row;
				break;
			}
		}
		batidas = getBatidas( rowbat, posini, numcols );
		if (batidas.size()>0) {
			hini = batidas.elementAt( 0 );
			if (batidas.size()>=4) {
				// quarto elemento
				hfin = batidas.elementAt( 3 );
			} else if (batidas.size()>=2) {
				// segundo elemento
				hfin = batidas.elementAt( 1 );
			}
			if ( (hora.compareTo( hini )<0) || (hora.compareTo( hfin )>0) ) {
				result = false;
			}
		}
		return result;
	}

	public boolean checarEstagio4(final Vector<Vector<Object>>  vexped, final Vector<Vector<Object>>  vatend, int nbatidas) {
		boolean result = false;
		//    	Integer codemp = (Integer) prefs[PREFS.CODEMPMI.ordinal()];
		//    	Integer codfilial = (Integer) prefs[PREFS.CODFILIALMI.ordinal()];
		Integer codmodel = (Integer) prefs[PREFS.CODMODELMI.ordinal()];
		String descmodel = (String) prefs[PREFS.DESCMODELMI.ordinal()];
		int tempomaxint = (Integer) prefs[PREFS.TEMPOMAXINT.ordinal()];
		Integer codespecia = (Integer) prefs[PREFS.CODESPECIA.ordinal()];
		String descespecia = (String) prefs[PREFS.DESCESPECIA.ordinal()];
		String dtatendopos = null;
		String dtatendo = null;
		String horaini = null;
		String horafin = null;
		String horaatendo = null;
		String horaintervalo = null;
		Vector<Object> row = null;
		Vector<Object> rowPos = null;
		int totalmin = 0;
		int intervalo = 0;
		int intervaloinserir = 0;
		int totintervalo = 0;
		int pos = 0;
		// Abre um loop até o final do vetor de atendimentos
		for (int i=0; i<vatend.size(); i++) {
			row = vatend.elementAt( i );
			dtatendo = (String) row.elementAt( EColAtend.DATAATENDO.ordinal() );
			horaatendo = (String) row.elementAt( EColAtend.HORAATENDO.ordinal() );
			intervalo = (Integer) row.elementAt( EColAtend.INTERVATENDO.ordinal() );
			// Se o horário estiver fora do turno de trabalho não deve compor intervalo
			if ( ! verificHorarioTurno( vexped, dtatendo, horaatendo, nbatidas )) {
				intervalo = 0;
			}
			// Se foi detectado que existe um intervalo sem lançamentos
			if (intervalo>0) {
				pos = primeiroAtend(vatend, dtatendo); // Checar as datas do mesmo dia, para verificar se não ultrapassará o limite para intervalos.
				dtatendopos = dtatendo;
				totalmin = 0;
				while ( ( pos<vatend.size() ) && (dtatendo.equals( dtatendopos ) ) ) {
					rowPos = vatend.elementAt( pos );
					dtatendopos = (String) rowPos.elementAt( EColAtend.DATAATENDO.ordinal() );
					if ( codespecia.equals((Integer) rowPos.elementAt( EColAtend.CODESPEC.ordinal() ))	) {
						totalmin += ( (Integer) rowPos.elementAt( EColAtend.TOTALMIN.ordinal() ) );
					}
					pos ++;
				}
				// Verificar se é possível inserir lançamentos
				intervaloinserir = intervalo;
				if (intervaloinserir>tempomaxint) {
					intervaloinserir = tempomaxint;
				}
				intervaloinserir = intervaloinserir - totalmin;
				// Se restar intervalo a inserir
				if ( row != null) {
					if ( (row!=null) && (intervaloinserir>0) ) {
						horaintervalo = Funcoes.longTostrTime( (long) intervaloinserir * 1000 * 60 );
						horafin = getTimeString( row.elementAt( EColAtend.HORAATENDO.ordinal() ) );
						horaini = getTimeString( Funcoes.longTostrTime( 
								Funcoes.subtraiTime( Funcoes.strTimeToSqlTime( horaintervalo, false ),
										Funcoes.strTimeToSqlTime( horafin, false )
										)
								) );
						row.setElementAt( horaini, EColAtend.HORAINI.ordinal() );
						row.setElementAt( horafin, EColAtend.HORAFIN.ordinal() );
						row.setElementAt( EstagioCheck.E4I.getValueTab(), EColAtend.SITREVATENDO.ordinal() );
						row.setElementAt( EstagioCheck.E4I.getImg(), EColAtend.SITREVATENDOIMG.ordinal() );
						row.setElementAt( codmodel , EColAtend.CODMODEL.ordinal() );
						row.setElementAt( descmodel, EColAtend.DESCMODEL.ordinal() );

						result = true;
					} else {
						row.setElementAt( EstagioCheck.E4O.getValueTab(), EColAtend.SITREVATENDO.ordinal() );
						row.setElementAt( EstagioCheck.E4O.getImg(), EColAtend.SITREVATENDOIMG.ordinal() );
					}
				}
			}
			else if ( row!=null ) {
				row.setElementAt( EstagioCheck.E4O.getValueTab(), EColAtend.SITREVATENDO.ordinal() );
				row.setElementAt( EstagioCheck.E4O.getImg(), EColAtend.SITREVATENDOIMG.ordinal() );
			}
		}
		return result;
	}

	public int primeiroAtend(Vector<Vector<Object>> vatend, String dtatend ) {
		int result = -1;
		Vector<Object> row = null;
		for (int i=0; i<vatend.size(); i++) {
			row = vatend.elementAt( i );
			if (dtatend.equals( row.elementAt( EColAtend.DATAATENDO.ordinal() ) ) ) {
				result = i;
				break;
			}
		}
		return result;
	}

	public Integer locateSetor(int codemp, int codfilial, int codempae, 
			int codfilialae, int codatend) {
		Integer result = null;
		ResultSet rs = null;
		int param = 1;
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		try {
			sql.append( "select first 1 s.codsetat from atsetor s, atsetoratendente sa " );
			sql.append( "where sa.codemp=s.codemp and sa.codfilial=s.codfilial and sa.codsetat=s.codsetat and " );
			sql.append( "s.codemp=? and s.codfilial=? and " );
			sql.append( "sa.codempae=? and sa.codfilialae=? and sa.codatend=? " );
			ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codempae );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codatend );
			rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getInt( "CODSETAT" );
			}
			rs.close();
			ps.close();
			getConn().commit();
		} catch (SQLException e) {
			try {
				e.printStackTrace();
				getConn().rollback();
			} catch (Exception err) {
				e.printStackTrace();
			}
		}
		return result;
	}



	public boolean bloquearAtendimentos(Integer codatendo, String data, String hora, boolean acesatdoaltout, Integer codatendpadrao, Integer codatendatendo)  throws SQLException{
		boolean result = false;
		String bloqAtendimento = (String) prefs[org.freedom.modulos.crm.business.object.Atendimento.PREFS.BLOQATENDIMENTO.ordinal()];

		StringBuilder sql = null;
		PreparedStatement ps = null;
		int param = 1;
		ResultSet rs = null;
		double periodobloq = 0;
		double intervalo = 0;

		if ("S".equals( bloqAtendimento)) {

			sql = new StringBuilder();
			sql.append( "select abs(cast(cast('now' as timestamp) ");
			sql.append( " - cast(coalesce(cast(ate.dataatendo + ate.horaatendo as timestamp) ");
			sql.append( ", cast(cast( '");
			sql.append( data.replace( "/", "." ) );
			sql.append( "' as date)+cast( '");
			sql.append( hora ); 
			sql.append( "' as time) as timestamp)) as timestamp) as decimal(15,3))*24) intervalo ");
			sql.append( ", cast( sg.periodobloq as decimal(15,3)) periodobloq ");

			sql.append( "from sgprefere3 sg ");
			sql.append( "left outer join  atatendimento ate on ate.codemp=? ");
			sql.append( "and ate.codfilial=? and ate.codatendo=? ");
			sql.append( "where sg.codemp=? and sg.codfilial=? ");

			ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "ATATENDIMENTO" ) );
			ps.setInt( param++, codatendo );
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "SGPREFERE3" ) );
			rs = ps.executeQuery();

			if (rs.next()) {
				intervalo = rs.getDouble( "intervalo" );
				periodobloq = rs.getDouble( "periodobloq" );
				result =  intervalo > periodobloq ;
			}
		}
		// Se não bloqueou pela regra anterior, se o atendende não tiver permissão de verificar os lançamentos e os lançamentos não pertencerem a ele. 

		if ( codatendpadrao != null && result==false && acesatdoaltout==false && codatendatendo.equals( codatendpadrao )==false ) {
			result = true;
		}

		return result;
	}


	public void insertAtendoAgenda(Integer codemp, Integer codfilial, Integer codatendo,
			Integer codempae, Integer codfilialae, String tipoage, Integer codage, Integer codagd) throws SQLException {

		StringBuilder sql = new StringBuilder();
		int param = 1;
		sql.append( "insert into atatendoagenda (codemp, codfilial, codatendo, codempae, codfilialae, tipoage, codage, codagd) " );
		sql.append( " values (?, ?, ?, ?, ?, ?, ?, ? ) " );

		PreparedStatement ps = getConn().prepareStatement( sql.toString() );

		ps.setInt( param++ , codemp ); // Código da empresa
		ps.setInt( param++ , codfilial ); // Código da filial
		ps.setInt( param++ , codatendo ); // Código do atendimento
		ps.setInt( param++ , codempae ); // Código da empresa agenda
		ps.setInt( param++ , codfilialae ); // Código da filail agenda
		ps.setString( param++ , tipoage ); // Tipo de agendamento
		ps.setInt( param++ , codage ); // Código do agendamento
		ps.setInt( param++ , codagd ); // Código do agendamento

		ps.execute();
		ps.close();

		getConn().commit();
	}
	
	public void excluirAtendimento(Integer codemp, Integer codfilial, Integer codatendo) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append( "DELETE FROM ATATENDIMENTO WHERE CODATENDO=? AND CODEMP=? AND CODFILIAL=?" );
		PreparedStatement ps = getConn().prepareStatement( sql.toString() );
		ps.setInt( 1, codatendo );
		ps.setInt( 2, codemp );
		ps.setInt( 3, codfilial );
		ps.execute();
		ps.close();
		getConn().commit();
	}

	public Vector<Vector<Object>> carregaGridPorCliente(Integer codempcl, Integer codfilialcl, Integer codcli, Integer codatend, boolean acesatdolerout) throws SQLException {
		StringBuilder sql = new StringBuilder();
		Vector<Object> vVals = null;
		Vector<Vector<Object>> vector = null;
		
		sql.append( "select a.codatendo, a.docatendo, a.statusatendo, a.dataatendo, a.desctpatendo" );
		sql.append( ", a.dataatendofin, a.horaatendofin, a.obsatendo, a.codatend, a.codorc" );
		sql.append( ", a.nomeatend, a.horaatendo, a.codchamado, a.codcli, a.codespec, a.descespec, a.nomecli" );
		sql.append( ", coalesce(a.mrelcobespec, 'N') mrelcobespec, coalesce(a.bhespec, 'N') bhespec" );
		sql.append( ", coalesce(a.contmetaespec, 'N') contmetaespec, coalesce(a.cobcliespec, 'N') cobcliespec " );
		sql.append( ", a.totalmin, a.totalgeral, a.totalcobcli, a.codorc  " );
		sql.append( "from atatendimentovw02 a " );
		sql.append( "where  " );
		sql.append( "a.codempcl=? and a.codfilialcl=? and a.codcli=? " );
		if (!acesatdolerout) 
			sql.append( " and codatend=?" );
		
		
		PreparedStatement ps = getConn().prepareStatement( sql.toString() );
		int param = 1;
		ps.setInt( param++, codempcl );
		ps.setInt( param++, codfilialcl );
		ps.setInt( param++, codcli );
		if (!acesatdolerout)
			ps.setInt( param++, codatend);	
		ResultSet rs = ps.executeQuery();
		vector =  new Vector<Vector<Object>>();
		
		while(rs.next()) {
			vVals = new Vector<Object>();
			//DOCATENDO, STATUSATENDO, DATAATENDO, CODATENDO, CODORC, DATAATENDOFIN, NOMECLI, OBSATENDO, CODATEND, NOMEATEND, HORAATENDO, HORAATENDOFIN, TEMPO, TEMPOCOB, CODCHAMADO, CODCLI, CODESPEC, DESCESPEC
			vVals.addElement( rs.getString( COL_ATENDIMENTO.DOCATENDO.name() ) );
			vVals.addElement( rs.getString( COL_ATENDIMENTO.STATUSATENDO.name() ));
			vVals.addElement( StringFunctions.sqlDateToStrDate( rs.getDate( COL_ATENDIMENTO.DATAATENDO.name() ) ) );
			vVals.addElement( new Integer( rs.getInt( COL_ATENDIMENTO.CODATENDO.name() ) ) );
			vVals.addElement( new Integer( rs.getInt( COL_ATENDIMENTO.CODORC.name() ) ));
			vVals.addElement( StringFunctions.sqlDateToStrDate( rs.getDate( COL_ATENDIMENTO.DATAATENDOFIN.name() ) ));
			vVals.addElement( rs.getString( COL_ATENDIMENTO.CODCLI.name() ).trim() + " " + rs.getString( COL_ATENDIMENTO.NOMECLI.name() ) );
			vVals.addElement( rs.getString( COL_ATENDIMENTO.OBSATENDO.name() ));
			vVals.addElement( new Integer( rs.getInt( COL_ATENDIMENTO.CODATEND.name() ) ) );
			vVals.addElement( rs.getString( COL_ATENDIMENTO.NOMEATEND.name() ).trim() );
			vVals.addElement( Funcoes.copy( rs.getTime( COL_ATENDIMENTO.HORAATENDO.name() ).toString(), 5 ) );
			vVals.addElement( Funcoes.copy( rs.getTime( COL_ATENDIMENTO.HORAATENDOFIN.name() ).toString(), 5 ) );
			vVals.addElement( rs.getBigDecimal( "TOTALGERAL" ) );
			vVals.addElement( rs.getBigDecimal( "TOTALCOBCLI" ));
			vVals.addElement( rs.getInt( COL_ATENDIMENTO.CODCHAMADO.name() ) );
			vVals.addElement( rs.getInt( COL_ATENDIMENTO.CODCLI.name() ));
			vVals.addElement( rs.getInt( COL_ATENDIMENTO.CODESPEC.name() ) );
			vVals.addElement( rs.getString( COL_ATENDIMENTO.CODESPEC.name() ) != null ? 
					( rs.getString( COL_ATENDIMENTO.CODESPEC.name() ).trim() + " " + rs.getString( COL_ATENDIMENTO.DESCESPEC.name() ).trim() ) : "" );

			vector.add( vVals );
		}
		
		return vector;
	}
	
	
	public Map<String, Object> paramAtendente(Integer codemp, Integer codfilial) throws SQLException {
		Map<String, Object> result = null;
		
		Integer codatend_atual = org.freedom.modulos.crm.business.component.Atendimento.buscaAtendente();
		StringBuilder sql = null;
		
		
		if ( codatend_atual != null ) {
			sql = new StringBuilder();
			sql.append( "select AcesAtdoLerOut, AcesAtdoAltOut, AcesAtdoDelLan, AcesAtdoDelOut, ");
			sql.append( "acestrocomis, acestrocomisout, codvend " );
			sql.append( "from atatendente ")
			   .append( "where codemp=? and codfilial=? and codatend=?" );
			int param = 1;
			
			PreparedStatement ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codatend_atual );
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				result = new HashMap<String, Object>();
				result.put( "acesatdolerout",  "S".equals(rs.getString("AcesAtdoLerOut")));
				result.put( "acesatdoaltout",  "S".equals(rs.getString("AcesAtdoAltOut")));
				result.put( "acesatdodellan",  "S".equals(rs.getString("AcesAtdoDelLan")));
				result.put( "acesatdodelout",  "S".equals(rs.getString("AcesAtdoDelOut")));
				result.put( "acestrocomis",  "S".equals(rs.getString("acestrocomis")));
				result.put( "acestrocomisout",  "S".equals(rs.getString("acestrocomisout")));
				result.put( "codvend",  rs.getInt("codvend"));
				result.put( "codatend", codatend_atual );			
			}
	
		}
		
		return result;
	}
}
