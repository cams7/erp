package org.freedom.library.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.freedom.infra.dao.AbstractDAO;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;

public class DAOEmail extends AbstractDAO {

	public DAOEmail(DbConnection connection, Integer codemp, Integer codfilial) {
		super(connection, codemp, codfilial);
	}
	
	public String getEmailEmp() {

		String email = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sSQL = new StringBuilder();

		try {

			sSQL.append("SELECT EMAILFILIAL FROM SGFILIAL WHERE CODEMP=? AND CODFILIAL=?");
			ps = getConn().prepareStatement(sSQL.toString());
			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, ListaCampos.getMasterFilial("SGFILIAL"));
			rs = ps.executeQuery();

			if (rs.next()) {

				email = rs.getString("EMAILFILIAL") != null ? rs.getString("EMAILFILIAL").trim() : null;
			}

			rs.close();
			ps.close();

			getConn().commit();

		}
		catch (Exception e) {
			Funcoes.mensagemErro(null, "Erro ao buscar email da filial!\n" + e.getMessage());
			e.printStackTrace();
		}

		return email;
	}

	public String getEmailCli(final int codcli, final DbConnection con) {

		String email = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sSQL = new StringBuilder();

		try {

			sSQL.append("SELECT EMAILCLI FROM VDCLIENTE WHERE CODEMP=? AND CODFILIAL=? AND CODCLI=?");
			ps = con.prepareStatement(sSQL.toString());
			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, ListaCampos.getMasterFilial("VDCLIENTE"));
			ps.setInt(3, codcli);
			rs = ps.executeQuery();

			if (rs.next()) {

				email = rs.getString("EMAILCLI") != null ? rs.getString("EMAILCLI").trim() : "";
			}

			rs.close();
			ps.close();

			con.commit();

		}
		catch (Exception e) {
			Funcoes.mensagemErro(null, "Erro ao buscar email do cliente!\n" + e.getMessage());
			e.printStackTrace();
		}

		return email;
	}


}
