package org.freedom.library.business.componet.integration;

import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;

public class SafeContabil extends Contabil {

	@Override
	public void createFile(File filecontabil) throws Exception {

		sizeMax = readrows.size();

		if (sizeMax == 0) {
			throw new Exception("Nenhum registro encontrado para exportação!");
		}

		fireActionListenerForMaxSize();

		File entradas = new File(filecontabil.getPath() + "\\SAFECONTABIL.TXT");
		entradas.createNewFile();

		FileWriter filewritercontabil = new FileWriter(entradas);

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

	protected static String getSqlCompras() {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT F.CODFOR CODIGO, F.CODCONTDEB CONTADEB, F.CODCONTCRED CONTACRED, C.VLRLIQCOMPRA VALOR,");
		sql.append("C.SERIE, C.DOCCOMPRA DOC, H.TXAHISTPAD, C.DTENTCOMPRA DATA, C.CODFILIAL, F.RAZFOR PORTADOR, ");
		sql.append("F.RAZFOR HISTORICO ");
		sql.append("FROM CPCOMPRA C, EQTIPOMOV T, CPFORNECED F ");
		sql.append("LEFT OUTER JOIN FNHISTPAD H ");
		sql.append("ON H.CODEMP=F.CODEMPHP AND H.CODFILIAL=F.CODFILIALHP AND H.CODHIST=F.CODHIST ");
		sql.append("WHERE C.CODEMP=? AND C.CODFILIAL=? AND C.DTENTCOMPRA BETWEEN ? AND ? AND ");
		sql.append("T.CODEMP=C.CODEMPTM AND T.CODFILIAL=C.CODFILIALTM AND ");
		sql.append("T.CODTIPOMOV=C.CODTIPOMOV AND T.FISCALTIPOMOV='S' AND ");
		sql.append("F.CODEMP=C.CODEMPFR AND F.CODFILIAL=C.CODFILIALFR AND F.CODFOR=C.CODFOR ");
		sql.append("ORDER BY DTENTCOMPRA");

		return sql.toString();
	}

	protected static String getSqlComprasPagas() {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT F.CODCONTCRED CONTADEB, CT.CODCONTCRED CONTACRED, ");
		sql.append("S.VLRSUBLANCA VALOR, C.SERIE, C.DOCCOMPRA DOC,");
		sql.append("H.TXAHISTPAD, S.DATASUBLANCA DATA, S.CODFILIAL, F.RAZFOR PORTADOR, ");
		sql.append("L.HISTBLANCA HISTORICO ");
		sql.append("FROM FNSUBLANCA S, FNLANCA L, FNITPAGAR I, CPFORNECED F, FNCONTA CT,");
		sql.append("FNHISTPAD H, FNPAGAR P, CPCOMPRA C ");
		sql.append("WHERE S.CODEMP=? AND S.CODFILIAL=? AND S.DATASUBLANCA BETWEEN ? AND ? AND ");
		sql.append("S.CODSUBLANCA<>0 AND ");
		sql.append("L.CODEMP=S.CODEMP AND L.CODFILIAL=S.CODFILIAL AND L.CODLANCA=S.CODLANCA AND ");
		sql.append("I.CODEMP=L.CODEMPPG AND I.CODFILIAL=L.CODFILIALPG AND I.CODPAG=L.CODPAG AND I.NPARCPAG=L.NPARCPAG AND ");
		sql.append("P.CODEMP=I.CODEMP AND P.CODFILIAL=I.CODFILIAL AND P.CODPAG=I.CODPAG AND ");
		sql.append("F.CODEMP=S.CODEMPFR AND F.CODFILIAL=S.CODFILIALFR AND F.CODFOR=S.CODFOR AND ");
		sql.append("CT.CODEMPPN=L.CODEMPPN AND CT.CODFILIALPN=L.CODFILIALPN AND CT.CODPLAN=L.CODPLAN AND ");
		sql.append("H.CODEMP=CT.CODEMPHP AND H.CODFILIAL=CT.CODFILIALHP AND H.CODHIST=CT.CODHIST AND ");
		sql.append("C.CODEMP=P.CODEMPCP AND C.CODFILIAL=P.CODFILIALCP AND C.CODCOMPRA=P.CODCOMPRA ");
		sql.append("ORDER BY DATASUBLANCA");

		return sql.toString();
	}

	protected static String getSqlContasPagar() {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT F.CODFOR CODIGO, F.CODCONTDEB CONTADEB, F.CODCONTCRED CONTACRED, P.VLRPARCPAG VALOR,");
		sql.append("' ' SERIE, P.DOCPAG DOC, H.TXAHISTPAD, P.DATAPAG DATA, P.CODFILIAL, F.RAZFOR PORTADOR, ");
		sql.append("P.OBSPAG HISTORICO ");
		sql.append("FROM FNPAGAR P, CPFORNECED F ");
		sql.append("LEFT OUTER JOIN FNHISTPAD H ");
		sql.append("ON H.CODEMP=F.CODEMPHP AND H.CODFILIAL=F.CODFILIALHP AND H.CODHIST=F.CODHIST ");
		sql.append("WHERE P.CODEMP=? AND P.CODFILIAL=? AND P.DATAPAG BETWEEN ? AND ? AND ");
		sql.append("F.CODEMP=P.CODEMPFR AND F.CODFILIAL=P.CODFILIALFR AND F.CODFOR=P.CODFOR AND ");
		sql.append("P.CODCOMPRA IS NULL ");
		sql.append("ORDER BY P.DATAPAG ");

		return sql.toString();
	}

	protected static String getSqlContasPagas() {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT F.CODCONTCRED CONTADEB, CT.CODCONTCRED CONTACRED, ");
		sql.append("S.VLRSUBLANCA VALOR, ' ' SERIE, P.DOCPAG DOC,");
		sql.append("H.TXAHISTPAD, S.DATASUBLANCA DATA, S.CODFILIAL, F.RAZFOR PORTADOR, ");
		sql.append("L.HISTBLANCA HISTORICO ");
		sql.append("FROM FNSUBLANCA S, FNLANCA L, FNITPAGAR I, CPFORNECED F, FNCONTA CT, ");
		sql.append("FNHISTPAD H, FNPAGAR P ");
		sql.append("WHERE S.CODEMP=? AND S.CODFILIAL=? AND S.DATASUBLANCA BETWEEN ? AND ? AND ");
		sql.append("S.CODSUBLANCA<>0 AND ");
		sql.append("L.CODEMP=S.CODEMP AND L.CODFILIAL=S.CODFILIAL AND L.CODLANCA=S.CODLANCA AND ");
		sql.append("I.CODEMP=L.CODEMPPG AND I.CODFILIAL=L.CODFILIALPG AND I.CODPAG=L.CODPAG AND I.NPARCPAG=L.NPARCPAG AND ");
		sql.append("P.CODEMP=I.CODEMP AND P.CODFILIAL=I.CODFILIAL AND P.CODPAG=I.CODPAG AND ");
		sql.append("F.CODEMP=S.CODEMPFR AND F.CODFILIAL=S.CODFILIALFR AND F.CODFOR=S.CODFOR AND ");
		sql.append("CT.CODEMPPN=L.CODEMPPN AND CT.CODFILIALPN=L.CODFILIALPN AND CT.CODPLAN=L.CODPLAN AND ");
		sql.append("H.CODEMP=CT.CODEMPHP AND H.CODFILIAL=CT.CODFILIALHP AND H.CODHIST=CT.CODHIST AND ");
		sql.append("P.CODCOMPRA IS NULL ");
		sql.append("ORDER BY DATASUBLANCA");

		return sql.toString();
	}

	protected static String getSqlVendas() {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT C.CODCLI CODIGO, C.CODCONTDEB CONTADEB, C.CODCONTCRED CONTACRED, V.VLRLIQVENDA VALOR,");
		sql.append("V.SERIE, V.DOCVENDA DOC, H.TXAHISTPAD, V.DTEMITVENDA DATA, C.CODFILIAL, C.RAZCLI PORTADOR, ");
		sql.append("C.RAZCLI HISTORICO ");
		sql.append("FROM VDVENDA V, EQTIPOMOV T, VDCLIENTE C ");
		sql.append("LEFT OUTER JOIN FNHISTPAD H ");
		sql.append("ON H.CODEMP=C.CODEMPHP AND H.CODFILIAL=C.CODFILIALHP AND H.CODHIST=C.CODHIST ");
		sql.append("WHERE V.CODEMP=? AND V.CODFILIAL=? AND V.DTEMITVENDA BETWEEN ? AND ? AND ");
		sql.append("NOT SUBSTR(V.STATUSVENDA,1,1)='C' AND V.VLRLIQVENDA>0 AND ");
		sql.append("T.CODEMP=V.CODEMPTM AND T.CODFILIAL=V.CODFILIALTM AND ");
		sql.append("T.CODTIPOMOV=V.CODTIPOMOV AND T.FISCALTIPOMOV='S' AND ");
		sql.append("C.CODEMP=V.CODEMPCL AND C.CODFILIAL=V.CODFILIALCL AND C.CODCLI=V.CODCLI ");
		sql.append("ORDER BY DTEMITVENDA");

		return sql.toString();
	}

	protected static String getSqlVendasRecebebidas() {

		StringBuilder sql = new StringBuilder();
		/*
		 * Incluído um case para enviar lançamentos de vendas com recibo, nas
		 * quais o tipofiscal é igual a 'N'.
		 */
		sql.append("SELECT (CASE WHEN TM.FISCALTIPOMOV='S' THEN CT.CODCONTDEB ELSE CT.CODCONTDEB END) CONTADEB, ");
		sql.append("(CASE WHEN TM.FISCALTIPOMOV='S' THEN C.CODCONTDEB ELSE PN.CODCONTCRED END) CONTACRED , ");
		sql.append("(S.VLRSUBLANCA*-1) VALOR, V.SERIE, V.DOCVENDA DOC,");
		sql.append("H.TXAHISTPAD, S.DATASUBLANCA DATA, S.CODFILIAL, C.RAZCLI PORTADOR, ");
		sql.append("L.HISTBLANCA HISTORICO ");
		sql.append("FROM FNSUBLANCA S, FNLANCA L, FNITRECEBER I, VDCLIENTE C, FNCONTA CT,");
		sql.append("FNHISTPAD H, FNRECEBER R, VDVENDA V, EQTIPOMOV TM, FNPLANEJAMENTO PN ");
		sql.append("WHERE S.CODEMP=? AND S.CODFILIAL=? AND S.DATASUBLANCA BETWEEN ? AND ? AND ");
		sql.append("S.CODSUBLANCA<>0 AND ");
		sql.append("TM.CODEMP=V.CODEMPTM AND TM.CODFILIAL=V.CODFILIALTM AND TM.CODTIPOMOV=V.CODTIPOMOV AND  ");
		sql.append("L.CODEMP=S.CODEMP AND L.CODFILIAL=S.CODFILIAL AND L.CODLANCA=S.CODLANCA AND ");
		sql.append("I.CODEMP=L.CODEMPRC AND I.CODFILIAL=L.CODFILIALRC AND I.CODREC=L.CODREC AND I.NPARCITREC=L.NPARCITREC AND ");
		sql.append("R.CODEMP=I.CODEMP AND R.CODFILIAL=I.CODFILIAL AND R.CODREC=I.CODREC AND ");
		sql.append("C.CODEMP=S.CODEMPCL AND C.CODFILIAL=S.CODFILIALCL AND C.CODCLI=S.CODCLI AND ");
		sql.append("CT.CODEMPPN=L.CODEMPPN AND CT.CODFILIALPN=L.CODFILIALPN AND CT.CODPLAN=L.CODPLAN AND ");
		sql.append("H.CODEMP=CT.CODEMPHP AND H.CODFILIAL=CT.CODFILIALHP AND H.CODHIST=CT.CODHIST AND ");
		sql.append("V.CODEMP=R.CODEMPVA AND V.CODFILIAL=R.CODFILIALVA AND V.TIPOVENDA=R.TIPOVENDA AND V.CODVENDA=R.CODVENDA AND ");
		sql.append("PN.CODEMP=S.CODEMPPN AND PN.CODFILIAL=S.CODFILIALPN AND PN.CODPLAN=S.CODPLAN ");
		sql.append("ORDER BY DATASUBLANCA");

		return sql.toString();
	}

	protected static String getSqlContasReceber() {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT C.CODCLI CODIGO, C.CODCONTDEB CONTADEB, C.CODCONTCRED CONTACRED, ");
		sql.append("R.VLRPARCREC VALOR,");
		sql.append("' ' SERIE, R.DOCREC DOC, H.TXAHISTPAD, R.DATAREC DATA, R.CODFILIAL, C.RAZCLI PORTADOR, ");
		sql.append("R.OBSREC HISTORICO ");
		sql.append("FROM FNRECEBER R, VDCLIENTE C ");
		sql.append("LEFT OUTER JOIN FNHISTPAD H ");
		sql.append("ON H.CODEMP=C.CODEMPHP AND H.CODFILIAL=C.CODFILIALHP AND H.CODHIST=C.CODHIST ");
		sql.append("WHERE R.CODEMP=? AND R.CODFILIAL=? AND R.DATAREC BETWEEN ? AND ? AND ");
		sql.append("C.CODEMP=R.CODEMPCL AND C.CODFILIAL=R.CODFILIALCL AND C.CODCLI=R.CODCLI AND ");
		sql.append("R.CODVENDA IS NULL ");
		sql.append("ORDER BY R.DATAREC");

		return sql.toString();
	}

	protected static String getSqlContasRecebidas() {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT CT.CODCONTDEB CONTADEB, C.CODCONTDEB CONTACRED,");
		sql.append("(S.VLRSUBLANCA*-1) VALOR, ' ' SERIE, R.DOCREC DOC,");
		sql.append("H.TXAHISTPAD, S.DATASUBLANCA DATA, S.CODFILIAL, C.RAZCLI PORTADOR, ");
		sql.append("L.HISTBLANCA HISTORICO ");
		sql.append("FROM FNSUBLANCA S, FNLANCA L, FNITRECEBER I, VDCLIENTE C, FNCONTA CT, ");
		sql.append("FNHISTPAD H, FNRECEBER R ");
		sql.append("WHERE S.CODEMP=? AND S.CODFILIAL=? AND S.DATASUBLANCA BETWEEN ? AND ? AND ");
		sql.append("S.CODSUBLANCA<>0 AND ");
		sql.append("L.CODEMP=S.CODEMP AND L.CODFILIAL=S.CODFILIAL AND L.CODLANCA=S.CODLANCA AND ");
		sql.append("I.CODEMP=L.CODEMPRC AND I.CODFILIAL=L.CODFILIALRC AND I.CODREC=L.CODREC AND I.NPARCITREC=L.NPARCITREC AND ");
		sql.append("R.CODEMP=I.CODEMP AND R.CODFILIAL=I.CODFILIAL AND R.CODREC=I.CODREC AND ");
		sql.append("C.CODEMP=S.CODEMPCL AND C.CODFILIAL=S.CODFILIALCL AND C.CODCLI=S.CODCLI AND ");
		sql.append("CT.CODEMPPN=L.CODEMPPN AND CT.CODFILIALPN=L.CODFILIALPN AND CT.CODPLAN=L.CODPLAN AND ");
		sql.append("H.CODEMP=CT.CODEMPHP AND H.CODFILIAL=CT.CODFILIALHP AND H.CODHIST=CT.CODHIST AND ");
		sql.append("R.CODVENDA IS NULL ");
		sql.append("ORDER BY DATASUBLANCA");

		return sql.toString();
	}

	protected static String getSqlLancamentosAvulcos() {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT (CASE WHEN S.VLRSUBLANCA>0 THEN COALESCE(P.CODCONTDEB,CTT.CODCONTDEB) ELSE CT.CODCONTDEB END) CONTADEB, ");
		sql.append("(CASE WHEN S.VLRSUBLANCA>0 THEN CT.CODCONTCRED ELSE COALESCE(P.CODCONTCRED,CTT.CODCONTCRED) END) CONTACRED, ");
		sql.append("(CASE WHEN S.VLRSUBLANCA <0 THEN S.VLRSUBLANCA*-1 ELSE S.VLRSUBLANCA END) VALOR , ");
		sql.append("' ' SERIE, L.DOCLANCA DOC, ");
		sql.append("COALESCE(H.TXAHISTPAD, HH.TXAHISTPAD) TXAHISTPAD, S.DATASUBLANCA DATA, S.CODFILIAL, L.HISTBLANCA AS PORTADOR, ");
		sql.append("L.HISTBLANCA HISTORICO ");
		sql.append("FROM FNSUBLANCA S, FNLANCA L, FNCONTA CT, FNPLANEJAMENTO P ");
		sql.append("LEFT OUTER JOIN FNHISTPAD H ");
		sql.append("ON H.CODEMP=P.CODEMPHP AND H.CODFILIAL=P.CODFILIALHP AND H.CODHIST=P.CODHIST ");
		sql.append("LEFT OUTER JOIN FNCONTA CTT ");
		sql.append("ON CTT.CODEMPPN=P.CODEMP AND CTT.CODFILIALPN=P.CODFILIAL AND CTT.CODPLAN=P.CODPLAN ");
		sql.append("LEFT OUTER JOIN FNHISTPAD HH ");
		sql.append("ON HH.CODEMP=CTT.CODEMPHP AND HH.CODFILIAL=CTT.CODFILIALHP AND HH.CODHIST=CTT.CODHIST ");
		sql.append("WHERE S.CODEMP=? AND S.CODFILIAL=? AND S.DATASUBLANCA BETWEEN ? AND ? AND ");
		sql.append("S.CODSUBLANCA<>0 AND L.CODEMP=S.CODEMP AND L.CODFILIAL=S.CODFILIAL AND L.CODLANCA=S.CODLANCA AND ");
		sql.append("L.CODPAG IS NULL AND L.CODREC IS NULL AND CT.CODEMPPN=L.CODEMPPN AND ");
		sql.append("CT.CODFILIALPN=L.CODFILIALPN AND CT.CODPLAN=L.CODPLAN AND ");
		sql.append("P.CODEMP=S.CODEMPPN AND P.CODFILIAL=S.CODFILIALPN AND P.CODPLAN=S.CODPLAN ");
		sql.append("ORDER BY DATASUBLANCA");

		return sql.toString();
	}

	public enum ETipo {

		COMPRAS("Compras", getSqlCompras(), "CPCOMPRA"), COMPRAS_PAGAS("Compras pagas", getSqlComprasPagas(), "FNSUBLANCA"), VENDAS("Vendas", getSqlVendas(), "VDVENDA"), VENDAS_RECEBIDAS(
				"Vendas recebidas", getSqlVendasRecebebidas(), "FNSUBLANCA"), CONTAS_PAGAR("Contas a pagar", getSqlContasPagar(), "FNPAGAR"), CONTAS_PAGAS("Contas pagas", getSqlContasPagas(),
				"FNSUBLANCA"), CONTAS_RECEBER("Contas a receber", getSqlContasReceber(), "FNRECEBER"), CONTAS_RECEBIDAS("Contas recebidas", getSqlContasRecebidas(), "FNSUBLANCA"), LANCAMENTOS_AVULSOS(
				"Lançamentos avulsos", getSqlLancamentosAvulcos(), "FNSUBLANCA");

		String descricao = "";
		String tabela = "";
		String sql = "";

		ETipo(String descricao, String sql, String tabela) {

			this.descricao = descricao;
			this.sql = sql;
			this.tabela = tabela;
		}

		public String getDescricao() {
			return this.descricao;
		}

		public String getSql() {
			return this.sql;
		}

		public String getTabela() {
			return this.tabela;
		}
	}

	private void formatSafe(final StringBuilder row, SafeContabilVO scvo) {

		row.append(format(scvo.getContadeb(), CHAR, 11, 0));
		row.append(format(scvo.getContacred(), CHAR, 11, 0));
		row.append(format(scvo.getValor(), NUMERIC, 14, 2));
		row.append(StringFunctions.replicate(" ", 14)); // Centro de custo
		row.append(format(scvo.getDocumento(), CHAR, 12, 0));
		row.append(format(scvo.getHistorico(), CHAR, 250, 0));
		row.append(format(scvo.getData(), DATE, 0, 0));
		row.append(format(scvo.getFilial(), NUMERIC, 5, 0));
		row.append(StringFunctions.replicate(" ", 25));
	}

	private String format(Object obj, int tipo, int tam, int dec) {

		String retorno = null;
		String str = null;

		if (obj == null) {
			str = "";
		}
		else {
			str = obj.toString();
		}

		if (tipo == NUMERIC) {
			if (dec > 0) {
				retorno = Funcoes.transValor(str, tam - 1, dec, true);
				retorno = retorno.substring(0, tam - dec - 1) + "," + retorno.substring(tam - dec - 1);
			}
			else {
				retorno = StringFunctions.strZero(str, tam);
			}
		}
		else if (tipo == CHAR) {
			retorno = Funcoes.adicionaEspacos(str, tam);
		}
		else if (tipo == DATE) {
			int[] args = Funcoes.decodeDate(( Date ) obj);
			retorno = StringFunctions.strZero(String.valueOf(args[2]), 2) + StringFunctions.strZero(String.valueOf(args[1]), 2) + StringFunctions.strZero(String.valueOf(args[0]), 4);
		}

		return retorno;
	}

	/**
	 * Este metodo é funcional somente para o layout do sistema Safe Contábil.
	 * 
	 * @param rs
	 * @param historico
	 * @return
	 */
	private String decodeHistorico(final ResultSet rs, final String historico) {

		String decode = null;

		if (historico != null) {

			try {

				String tmp = historico;

				tmp = tmp.replaceAll("<DOCUMENTO>", rs.getString("DOC") != null ? rs.getString("DOC").trim() : "");
				tmp = tmp.replaceAll("<VALOR>", String.valueOf(( rs.getBigDecimal("VALOR") != null ? rs.getBigDecimal("VALOR") : new BigDecimal("0.00") ).setScale(2, BigDecimal.ROUND_HALF_UP)));
				tmp = tmp.replaceAll("<SERIE>", rs.getString("SERIE") != null ? rs.getString("SERIE").trim() : "");
				tmp = tmp.replaceAll("<DATA>", Funcoes.dateToStrDate(rs.getDate("DATA")));
				tmp = tmp.replaceAll("<PORTADOR>", rs.getString("PORTADOR") != null ? rs.getString("PORTADOR").trim() : "");
				tmp = tmp.replaceAll("<HISTORICO>", rs.getString("HISTORICO") != null ? rs.getString("HISTORICO").trim() : "");

				decode = tmp;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		return decode;
	}

	public List<SafeContabilVO> execute(final DbConnection con, final Date dtini, final Date dtfim) throws Exception {

		List<SafeContabilVO> erros = new ArrayList<SafeContabilVO>();

		for (ETipo tipo : ETipo.values()) {
			executeSqlSafe(con, tipo, readrows, erros, dtini, dtfim);
		}

		return erros;
	}

	private void executeSqlSafe(final DbConnection con, final ETipo tipo, final List<String> readrows, final List<SafeContabilVO> erros, final Date dtini, final Date dtfim) throws Exception {

		StringBuilder row = new StringBuilder();

		PreparedStatement ps = con.prepareStatement(tipo.getSql());
		ps.setInt(1, Aplicativo.iCodEmp);
		ps.setInt(2, ListaCampos.getMasterFilial(tipo.getTabela()));
		ps.setDate(3, Funcoes.dateToSQLDate(dtini));
		ps.setDate(4, Funcoes.dateToSQLDate(dtfim));

		ResultSet rs = ps.executeQuery();

		SafeContabilVO sbvo = null;

		while (rs.next()) {

			row.delete(0, row.length());

			sbvo = new SafeContabilVO();

			sbvo.setContadeb(rs.getString("CONTADEB"));
			sbvo.setContacred(rs.getString("CONTACRED"));
			sbvo.setValor(rs.getBigDecimal("VALOR"));
			sbvo.setDocumento(rs.getString("DOC"));
			sbvo.setHistorico(decodeHistorico(rs, rs.getString("TXAHISTPAD")));
			sbvo.setData(rs.getDate("DATA"));
			sbvo.setFilial(rs.getInt("CODFILIAL"));
			sbvo.setTipo(tipo);

			if (sbvo.valido()) {
				formatSafe(row, sbvo);
				readrows.add(row.toString());
			}
			else {
				erros.add(sbvo);
			}
		}

		rs.close();
		ps.close();

		con.commit();
	}

	public class SafeContabilVO {

		private String contadeb;

		private String contacred;

		private BigDecimal valor;

		private String centrocusto;

		private String documento;

		private String historico;

		private Date data;

		private Integer filial;

		private ETipo tipo;

		public String getCentrocusto() {
			return centrocusto;
		}

		public void setCentrocusto(final String centrocusto) {
			this.centrocusto = centrocusto;
		}

		public String getContacred() {
			return contacred;
		}

		public void setContacred(final String contacred) {
			this.contacred = contacred;
		}

		public String getContadeb() {
			return contadeb;
		}

		public void setContadeb(final String contadeb) {
			this.contadeb = contadeb;
		}

		public Date getData() {
			return data;
		}

		public void setData(final Date data) {
			this.data = data;
		}

		public String getDocumento() {
			return documento;
		}

		public void setDocumento(final String documento) {
			this.documento = documento;
		}

		public Integer getFilial() {
			return filial;
		}

		public void setFilial(final Integer filial) {
			this.filial = filial;
		}

		public String getHistorico() {
			return historico;
		}

		public void setHistorico(final String historico) {
			this.historico = historico;
		}

		public ETipo getTipo() {
			return tipo;
		}

		public void setTipo(final ETipo tipo) {
			this.tipo = tipo;
		}

		public BigDecimal getValor() {
			return valor;
		}

		public void setValor(final BigDecimal valor) {
			this.valor = valor;
		}

		public boolean valido() {

			boolean valido = false;

			if (( getContacred() != null && getContacred().trim().length() > 0 ) && ( getContadeb() != null && getContadeb().trim().length() > 0 )
					&& ( getValor() != null && getValor().floatValue() > 0 ) && ( getDocumento() != null && getDocumento().trim().length() > 0 )
					&& ( getHistorico() != null && getHistorico().trim().length() > 0 ) && ( getData() != null ) && ( getFilial() > 0 )) {
				valido = true;
			}

			return valido;
		}

		@Override
		public String toString() {

			return "[" + getDocumento() + "," + getContacred() + "," + getContadeb() + "," + getValor() + "," + getData() + "]";
		}
	}
}
