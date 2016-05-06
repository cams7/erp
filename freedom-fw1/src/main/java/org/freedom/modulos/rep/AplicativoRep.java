package org.freedom.modulos.rep;

import org.freedom.infra.model.jdbc.DbConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.freedom.library.business.object.EmailBean;
import org.freedom.library.dao.DAOEmail;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.AplicativoPD;
import org.freedom.library.swing.frame.FPrincipal;
import org.freedom.library.swing.frame.Login;
import org.freedom.modulos.rep.RPPrefereGeral.EPrefere;

public class AplicativoRep extends AplicativoPD {

	public AplicativoRep(String sIcone, String sSplash, int iCodSis, String sDescSis, int iCodModu, String sDescModu, String sDirImagem, FPrincipal telaP, Class<? extends Login> cLogin) {

		super(sIcone, sSplash, iCodSis, sDescSis, iCodModu, sDescModu, sDirImagem, telaP, cLogin);

	}

	@Override
	protected void carregaCasasDec() {

		try {

			PreparedStatement ps = con.prepareStatement("SELECT CASASDEC,CASASDECFIN FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?");
			ps.setInt(1, iCodEmp);
			ps.setInt(2, ListaCampos.getMasterFilial("SGPREFERE1"));
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				casasDec = rs.getInt("CASASDEC");
				casasDecFin = rs.getInt("CASASDECFIN");
			}

			rs.close();
			ps.close();

			con.commit();
		}
		catch (SQLException err) {
			Funcoes.mensagemErro(null, "Não foi possível obter o número de casas decimais!\n" + err.getMessage(), true, con, err);
		}
	}

	@Override
	protected void carregaBuscaProd() {

		bBuscaProdSimilar = false;
		bBuscaCodProdGen = false;
	}

	@Override
	public void createEmailBean() {

		List<Object> prefere = RPPrefereGeral.getPrefere(con);

		if (prefere.size() == EPrefere.values().length) {

			EmailBean mail = new EmailBean();
			mail.setHost(( String ) prefere.get(EPrefere.SERVIDORSMTP.ordinal()));
			mail.setPorta(( Integer ) prefere.get(EPrefere.PORTASMTP.ordinal()));
			mail.setUsuario(( String ) prefere.get(EPrefere.USUARIOSMTP.ordinal()));
			mail.setSenha(( ( String ) prefere.get(EPrefere.SENHASMTP.ordinal()) ).trim());
			mail.setDe(new DAOEmail(con, iCodEmp, iCodFilial).getEmailEmp());
			mail.setAutentica(( String ) prefere.get(EPrefere.AUTENTICASMTP.ordinal()));
			mail.setSsl(( String ) prefere.get(EPrefere.SSLSMTP.ordinal()));

			setEmailBean(mail);
		}
	}

	@Override
	public void updateEmailBean(EmailBean email) {

		try {

			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE SGPREFERE1 ");
			sql.append("SET SERVIDORSMTP=?,PORTASMTP=?,USUARIOSMTP=?,SENHASMTP=?,AUTENTICASMTP=?,SSLSMTP=? ");
			sql.append("WHERE CODEMP=? AND CODFILIAL=?");

			PreparedStatement ps = con.prepareStatement(sql.toString());
			ps.setString(1, email.getHost());
			ps.setInt(2, email.getPorta());
			ps.setString(3, email.getUsuario());
			ps.setString(4, email.getSenha());
			ps.setString(5, email.getAutentica());
			ps.setString(6, email.getSsl());
			ps.setInt(7, iCodEmp);
			ps.setInt(8, ListaCampos.getMasterFilial("SGPREFERE1"));
			ps.executeUpdate();
			ps.close();

			con.commit();

			setEmailBean(email);

		}
		catch (SQLException e) {
			Funcoes.mensagemErro(null, "Não foi gravar as alterações de configuração de email!\n" + e.getMessage());
		}
	}

	public static void atualizaEmailBean(DbConnection con) {

		List<Object> prefere = RPPrefereGeral.getPrefere(con);

		EmailBean mail = new EmailBean();
		mail.setHost(( String ) prefere.get(EPrefere.SERVIDORSMTP.ordinal()));
		mail.setPorta(( Integer ) prefere.get(EPrefere.PORTASMTP.ordinal()));
		mail.setUsuario(( String ) prefere.get(EPrefere.USUARIOSMTP.ordinal()));
		mail.setSenha(( ( String ) prefere.get(EPrefere.SENHASMTP.ordinal()) ).trim());
		mail.setDe(new DAOEmail(con, iCodEmp, iCodFilial).getEmailEmp());
		mail.setAutentica(( String ) prefere.get(EPrefere.AUTENTICASMTP.ordinal()));
		mail.setSsl(( String ) prefere.get(EPrefere.SSLSMTP.ordinal()));

		setEmailBean(mail);
	}

	public static String getEmailCliente(final int codcli, final DbConnection con) {

		String email = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sSQL = new StringBuilder();

		try {

			sSQL.append("SELECT EMAILCLI FROM RPCLIENTE WHERE CODEMP=? AND CODFILIAL=? AND CODCLI=?");
			ps = con.prepareStatement(sSQL.toString());
			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, ListaCampos.getMasterFilial("RPCLIENTE"));
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

	public static String getEmailFornecedor(final int codfor, final DbConnection con) {

		String email = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sSQL = new StringBuilder();

		try {

			sSQL.append("SELECT EMAILFOR FROM RPFORNECEDOR WHERE CODEMP=? AND CODFILIAL=? AND CODFOR=?");
			ps = con.prepareStatement(sSQL.toString());
			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, ListaCampos.getMasterFilial("RPFORNECEDOR"));
			ps.setInt(3, codfor);
			rs = ps.executeQuery();

			if (rs.next()) {

				email = rs.getString("EMAILFOR") != null ? rs.getString("EMAILFOR").trim() : "";
			}

			rs.close();
			ps.close();

			con.commit();

		}
		catch (Exception e) {
			Funcoes.mensagemErro(null, "Erro ao buscar email do fornecedor!\n" + e.getMessage());
			e.printStackTrace();
		}

		return email;
	}
}
