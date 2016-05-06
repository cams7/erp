package org.freedom.library.business.componet.integration;

import java.io.File;
import java.io.FileWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;

public class FreedomContabil extends Contabil {

	@Override
	public void createFile(File filecontabil) throws Exception {

		sizeMax = readrows.size();

		if (sizeMax == 0) {
			throw new Exception("Nenhum registro encontrado para exportação!");
		}

		fireActionListenerForMaxSize();

		filecontabil.createNewFile();

		FileWriter filewritercontabil = new FileWriter(filecontabil);

		sizeMax = readrows.size();
		fireActionListenerForMaxSize();

		progressInRows = 1;

		for (String row : readrows) {

			filewritercontabil.write(row);
			filewritercontabil.write(RETURN);
			filewritercontabil.flush();

			progressInRows++;

			fireActionListenerProgressInRows();
		}

		filewritercontabil.close();
	}

	public void execute(final DbConnection con, final Date dtini, final Date dtfim) throws Exception {

		StringBuilder sql = new StringBuilder();
		StringBuilder row = new StringBuilder();

		sql.append("SELECT L.TIPOLF,L.CODEMP,L.CODFILIAL,L.CODEMITLF, ");
		sql.append("(CASE WHEN L.TIPOLF='S' THEN");
		sql.append("	COALESCE(C.CNPJCLI,C.CPFCLI)");
		sql.append("	ELSE");
		sql.append("	COALESCE(F.CNPJFOR, F.CPFFOR)");
		sql.append("	END) CNPJEMIT,");
		sql.append("L.DTEMITLF, L.ESPECIELF, L.SERIELF,");
		sql.append("L.DOCINILF, L.DOCFIMLF, L.CODNAT,");
		sql.append("L.ALIQICMSLF,");
		sql.append("L.ALIQIPILF,");
		sql.append("L.OBSLF,");
		sql.append("SUM(L.VLRBASEICMSLF) VLRBASECIMSLF,");
		sql.append("SUM(L.VLRICMSLF) VLRICMSLF,");
		sql.append("SUM(L.VLRISENTASICMSLF) VLRISENTASICMSLF,");
		sql.append("SUM(L.VLROUTRASICMSLF) VLROUTRASICMSLF,");
		sql.append("SUM(L.VLRBASEIPILF) VLRBASEIPILF,");
		sql.append("SUM(L.VLRIPILF) VLRIPILF,");
		sql.append("SUM(L.VLRISENTASIPILF) VLRISENTASIPILF,");
		sql.append("SUM(L.VLROUTRASIPILF) VLROUTRASIPILF ");
		sql.append("FROM  LFLIVROFISCAL L ");
		sql.append("LEFT OUTER JOIN VDCLIENTE C ON ");
		sql.append("	C.CODEMP=L.CODEMP AND");
		sql.append("	C.CODFILIAL=L.CODFILIAL AND");
		sql.append("	C.CODCLI=L.CODEMITLF AND");
		sql.append("	L.TIPOLF='S' ");
		sql.append("LEFT OUTER JOIN CPFORNECED F ON");
		sql.append("	F.CODEMP=L.CODEMP AND");
		sql.append("	F.CODFILIAL=L.CODFILIAL AND");
		sql.append("	F.CODFOR=L.CODEMITLF AND");
		sql.append("	L.TIPOLF='E' ");
		sql.append("WHERE L.CODEMP=? AND L.CODFILIAL=? AND L.DTEMITLF BETWEEN ? AND ? ");
		sql.append("GROUP BY L.TIPOLF,L.CODEMP,L.CODFILIAL,L.CODEMITLF,");
		sql.append("5,L.DTEMITLF,L.ESPECIELF,L.SERIELF,");
		sql.append("L.DOCINILF,L.DOCFIMLF,L.CODNAT,");
		sql.append("L.ALIQICMSLF,L.ALIQIPILF,L.OBSLF ");
		sql.append("ORDER BY L.DTEMITLF");

		PreparedStatement ps = con.prepareStatement(sql.toString());
		ps.setInt(1, Aplicativo.iCodEmp);
		ps.setInt(2, ListaCampos.getMasterFilial("LFLIVROFISCAL"));
		ps.setDate(3, Funcoes.dateToSQLDate(dtini));
		ps.setDate(4, Funcoes.dateToSQLDate(dtfim));

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {

			row.delete(0, row.length());

			row.append(rs.getString("CODEMITLF"));
			row.append(rs.getString("CNPJEMIT"));
			row.append(rs.getString("DTEMITLF"));
			row.append(rs.getString("ESPECIELF"));
			row.append(rs.getString("SERIELF"));
			row.append(rs.getString("DOCINILF"));
			row.append(rs.getString("DOCFIMLF"));
			row.append(rs.getString("CODNAT"));
			row.append(rs.getString("ALIQICMSLF"));
			row.append(rs.getString("ALIQIPILF"));
			row.append(rs.getString("OBSLF"));
			row.append(rs.getString("VLRBASECIMSLF"));
			row.append(rs.getString("VLRICMSLF"));
			row.append(rs.getString("VLRISENTASICMSLF"));
			row.append(rs.getString("VLROUTRASICMSLF"));
			row.append(rs.getString("VLROUTRASICMSLF"));
			row.append(rs.getString("VLRBASEIPILF"));
			row.append(rs.getString("VLRIPILF"));
			row.append(rs.getString("VLRISENTASIPILF"));
			row.append(rs.getString("VLROUTRASIPILF"));

			readrows.add(row.toString());
		}

		rs.close();
		ps.close();

		con.commit();
	}
}
