package org.freedom.modulos.crm.dao;

import java.awt.Component;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.freedom.infra.dao.AbstractDAO;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.crm.object.Agenda;


public class DAOAgenda extends AbstractDAO {

	public DAOAgenda( DbConnection connection ) {
		super( connection );
	}

	public void excluiAgd(Integer codemp, Integer codfilial, String codagd, Integer codage, String tipoage) throws SQLException {
		String sql = "DELETE FROM SGAGENDA WHERE CODAGD=? AND CODEMP=? AND CODFILIAL=? AND CODAGE=? AND TIPOAGE=?";
		PreparedStatement ps = getConn().prepareStatement(sql);
		int param = 1;

		ps.setString(param++, codagd);
		ps.setInt(param++, codemp);
		ps.setInt(param++, codfilial);
		ps.setInt(param++, codage);
		ps.setString(param++, tipoage);
		ps.execute();
		ps.close();

		getConn().commit();
	}

	public String getUserDestino(Integer codemp, Integer codfilial, String codage, String tipoage) throws SQLException {
		StringBuilder sql = new StringBuilder();

		sql.append("select u.idusu from sgusuario u ");
		sql.append("where u.codempae=? and u.codfilialae=? and u.codage=? and u.tipoage=?");
		int param = 1;

		PreparedStatement ps = getConn().prepareStatement(sql.toString());
		ps.setInt(param++, codemp);
		ps.setInt(param++, codfilial);
		ps.setString(param++, codage);
		ps.setString(param++, tipoage);

		ResultSet rs = ps.executeQuery();
		String userDestino = "";

		if (rs.next()) {
			userDestino = rs.getString("idusu");
		}

		return userDestino;
	}


	public static ResultSet consultaAgenda(final Vector<Vector<?>> agentes, final Object[] datas, final JTablePad tabAgd,
			final boolean todos, final DbConnection con, final Component cOrig,
			final String sPeriodo, boolean pendentes, boolean cancelados, boolean concluidos, boolean pbaixa, 
			boolean pmedia, boolean palta, int icodage) {

		ResultSet rs = null;
		Vector<?> vcodage = agentes.elementAt(0);
		Vector<?> vtipoage = agentes.elementAt(1);

		String scodages = Funcoes.vectorToString(vcodage, ",");
		String stipoage = "'" + Funcoes.vectorToString(vtipoage, "','") + "'";

		boolean selagendapropria = false;

		for (int i = 0; i < vcodage.size(); i++) {
			if (icodage == ( ( Integer ) vcodage.elementAt(i) ).intValue()) {
				selagendapropria = true;
				break;
			}
		}

		if (scodages.length() > 0) {

			tabAgd.limpa();
			Object[] oDatas = datas;
			String sDatas = "";

			if (oDatas == null || oDatas.length == 0) {
				oDatas = new Object[1];
				oDatas[0] = new Date();
			}

			for (int i = 0; i < oDatas.length; i++) {
				if (i == 0) {
					sDatas = "'" + Funcoes.dateToStrDB(( Date ) oDatas[i]) + "'";
				}
				else {
					sDatas = sDatas + "," + "'" + Funcoes.dateToStrDB(( Date ) oDatas[i]) + "'";
				}
			}

			StringBuffer sSQL = new StringBuffer();
			sSQL.append("SELECT A.CODAGD,A.SITAGD,A.DTAINIAGD,A.HRINIAGD,A.DTAFIMAGD,");
			sSQL.append("A.HRFIMAGD,A.ASSUNTOAGD,A.PRIORAGD,U.IDUSU,");
			sSQL.append("(SELECT FIRST 1 U2.CORAGENDA FROM SGUSUARIO U2 ");
			sSQL.append("WHERE U2.CODEMPAE=A.CODEMPAE AND U2.CODFILIALAE=A.CODFILIALAE AND U2.CODAGE=A.CODAGE AND U2.TIPOAGE=A.TIPOAGE) AS CORAGENDA, ");
			sSQL.append(" A.DESCAGD, A.CODAGE, A.CODAGEEMIT, ");
			sSQL.append("rtrim(coalesce(u.pnomeusu,'')) || ' ' || rtrim(coalesce(u.unomeusu,'')) as usuario ,A.DIATODO ");
			sSQL.append(" FROM SGAGENDA A, SGUSUARIO U ");
			sSQL.append(" WHERE A.CODEMP=? AND A.CODFILIAL=? ");
			sSQL.append(" AND U.CODEMPAE=A.CODEMP AND U.CODFILIALAE=A.CODFILIAL ");
			sSQL.append(" AND U.CODAGE=A.CODAGE AND U.TIPOAGE=A.TIPOAGE ");

			if (todos) {
				sSQL.append(" AND ((( A.CAAGD='PU') AND ( A.CODAGE IN (" + scodages + ") AND A.TIPOAGE IN (" + stipoage + ") )) ");
			}
			else {
				sSQL.append(" AND ((A.CAAGD='PU' AND A.CODAGE IN (" + scodages + ") AND A.TIPOAGE IN (" + stipoage + ")) ");
			}

			if (selagendapropria) {
				sSQL.append(" OR (A.CODAGE=" + icodage + ") ) ");
			}

			Vector<String> vfiltros = new Vector<String>();
			if (pendentes) {
				vfiltros.addElement("'PE'");
			}
			if (cancelados) {
				vfiltros.addElement("'CA'");
			}
			if (concluidos) {
				vfiltros.addElement("'FN'");
			}

			String sfiltros = Funcoes.vectorToString(vfiltros, ",");

			if (( sfiltros != null ) && ( !"".equals(sfiltros) )) {
				sSQL.append(" AND A.SITAGD IN (" + sfiltros + ") ");
			}

			if (!"T".equals(sPeriodo)) {
				sSQL.append(" AND DTAINIAGD IN(" + sDatas + ") ");
			}

			Vector<String> vprioridade = new Vector<String>();
			if (pbaixa) {
				vprioridade.addElement("0,1,2");
			}
			if (pmedia) {
				vprioridade.addElement("3");
			}
			if (palta) {
				vprioridade.addElement("4");
			}

			String sprioridade = Funcoes.vectorToString(vprioridade, ",");

			if (( sprioridade != null ) && ( !"".equals(sprioridade) )) {
				sSQL.append(" AND A.PRIORAGD IN (" + sprioridade + ") ");
			}

			sSQL.append(" ORDER BY A.DTAINIAGD DESC,A.HRINIAGD DESC,A.DTAFIMAGD DESC,A.HRFIMAGD DESC ");

			try {

				PreparedStatement ps = con.prepareStatement(sSQL.toString());
				ps.setInt(1, Aplicativo.iCodEmp);
				ps.setInt(2, Aplicativo.iCodFilialPad);

				System.out.println(sSQL.toString());
				rs = ps.executeQuery();

			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			tabAgd.limpa();
			rs = null;
		}
		return rs;
	}


	public Map<String, Object> buscaAgente() {
		Map<String, Object> result = new HashMap<String, Object>();
		int param = 1;
		try {

			String sSQL = "SELECT U.CODAGE,U.TIPOAGE,U.CODFILIALAE FROM SGUSUARIO U WHERE CODEMP=? AND CODFILIAL=? AND IDUSU=? AND U.ATIVOUSU='S' ";
			PreparedStatement ps = getConn().prepareStatement(sSQL);
			ps.setInt(param++, Aplicativo.iCodEmp);
			ps.setInt(param++, Aplicativo.iCodFilial);
			ps.setString(param++, Aplicativo.getUsuario().getIdusu());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				result.put("CodAge", rs.getInt("CODAGE"));
				result.put("TipoAge", rs.getString("TIPOAGE"));
				result.put("CodFilialAge", rs.getInt("CODFILIALAE"));
			}

			rs.close();
			ps.close();

			getConn().commit();

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public Integer insert(Agenda agenda) {

		String sql = "SELECT IRET FROM SGSETAGENDASP(0,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		ResultSet rs = null;
		Integer ret = agenda.getCodagdar();
		Integer param = 1;
		try {

			PreparedStatement ps = getConn().prepareStatement(sql);
			ps.setInt(param++, Aplicativo.iCodEmp);// código da empresa
			ps.setDate(param++, Funcoes.dateToSQLDate(agenda.getDtini()));// data inicial
			ps.setString(param++, agenda.getHini());// hora inicial
			ps.setDate(param++, Funcoes.dateToSQLDate(agenda.getDtfim()));// data final
			ps.setString(param++, agenda.getHfim());// hora final
			ps.setString(param++, agenda.getAssunto());// assunto
			ps.setString(param++, agenda.getDescricao());// descrição da atividade
			ps.setString(param++, agenda.getCodfilialagd());// filial do tipo de agendamento
			ps.setString(param++, agenda.getTipoagd());// tipo do agendamento
			ps.setString(param++, agenda.getPrioridade());// prioridade
			ps.setString(param++, agenda.getCodagente());// código do agente
			ps.setString(param++, agenda.getTipoagente());// tipo do agente
			ps.setInt(param++, agenda.getCodfilialagt());// filial do agente emitente
			ps.setInt(param++, agenda.getCodagentee());// código do agente emitente
			ps.setString(param++, agenda.getTipoagente());// tipo do agente emitente
			ps.setString(param++, agenda.getControleacesso());// controle de acesso
			ps.setString(param++, agenda.getStatus());// status
			ps.setString(param++, agenda.getMotivo());// Motivo / resolução

			if (agenda.isRepete() && agenda.getCont() > 0 && agenda.getCodagdar() != null) {
				ps.setInt(param++, agenda.getCodagdar());
			}
			else {
				ps.setNull(param++, Types.INTEGER);
			}

			ps.setString(param++, agenda.getDiatodo());// indica se é de dia todo.

			rs = ps.executeQuery();

			if (rs.next() && agenda.getCont() == 0) {
				ret = rs.getInt("IRET");
			}

			ps.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

}
