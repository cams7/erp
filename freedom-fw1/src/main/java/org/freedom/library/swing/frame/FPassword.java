/**
 * @version 06/02/2006 <BR>
 * @author Setpoint Informática Ltda./Alex Rodrigues <BR>
 * 
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.telas <BR>
 * Classe:
 * @(#)FPassword.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * FFDialog para validação de senha.
 */
package org.freedom.library.swing.frame;

import java.awt.Component;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPasswordFieldPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class FPassword extends FFDialogo {

	public static final long serialVersionUID = 1L;

	/**
	 * Permissão para vender o produto abaixo do custo.
	 */
	public static final int BAIXO_CUSTO = 0;

	/**
	 * Permissão para abrir gaveta do PDV.
	 */
	public static final int ABRE_GAVETA = 1;

	/**
	 * Permissão para alterar as parcelas no fechamento da venda.
	 */
	public static final int ALT_PARC_VENDA = 2;

	/**
	 * Permissão para venda de produto com receita.
	 */
	public static final int APROV_RECEITA_PROD = 3;

	/**
	 * Permissão para visualização de tela de libera crédito.
	 */
	public static final int LIBERA_CRED = 4;

	/**
	 * Permissão para visualização de tela de libera crédito.
	 */
	public static final int VENDA_IMOBLIZIADO = 5;

	/**
	 * Permissão para digitacao do peso nas telas de pesagem.
	 */
	public static final int LIBERA_CAMPO_PESAGEM = 6;

	private JTextFieldPad txtUsu = new JTextFieldPad(JTextFieldPad.TP_STRING, 8, 0);

	private JPasswordFieldPad txtPass = new JPasswordFieldPad(10);

	private String[] param = null;

	private int tipo = 0;

	private int[] log = null;

	/**
	 * 
	 * @param arg0
	 *            Component pai.
	 * @param arg1
	 *            Tipo da permissão.
	 * @param arg2
	 *            Parametros para o log(para o tipo BAIXO_CUSTO).
	 * @param arg3
	 *            Titulo.
	 * @param arg4
	 *            Conexão.
	 */
	public FPassword(Component arg0, int arg1, String[] arg2, String arg3, DbConnection arg4) {

		super(arg0);
		tipo = arg1;
		setParam(arg2);
		setTitulo(arg3);
		setConexao(arg4);
		montaTela();
	}

	public FPassword(Component arg0, int arg1, String arg2, DbConnection arg3) {

		this(arg0, arg1, null, arg2, arg3);
	}

	private void montaTela() {

		setAtribos(300, 140);
		adic(new JLabelPad("Usuário: "), 7, 10, 100, 20);
		adic(new JLabelPad("Senha: "), 7, 30, 100, 20);
		adic(txtUsu, 110, 10, 150, 20);
		adic(txtPass, 110, 30, 150, 20);
		adic(new JLabelPad("Senha: "), 7, 30, 100, 20);

		eUltimo();

		txtUsu.setVlrString(Aplicativo.getUsuario().getIdusu());
		setPrimeiroFoco(txtPass);
	}

	public void execShow() {

		setVisible(true);
		firstFocus();
	}

	public void ok() {

		boolean ret = false;

		switch (tipo) {
		case BAIXO_CUSTO:
			ret = getBaixoCusto();
			break;
		case ABRE_GAVETA:
			ret = getAbreGaveta();
			break;
		case ALT_PARC_VENDA:
			ret = getAltParcVenda();
			break;
		case APROV_RECEITA_PROD:
			ret = getAprovReceitaProd();
			break;
		case LIBERA_CRED:
			ret = getLiberaCredito();
			break;
		case VENDA_IMOBLIZIADO:
			ret = getVendaImobilizado();
			break;
		case LIBERA_CAMPO_PESAGEM:
			ret = getLiberaCampoPesagem();
			break;
		default:
			break;
		}

		OK = ret;
		setVisible(false);
	}

	private boolean getBaixoCusto() {

		boolean ret = getPermissao(BAIXO_CUSTO);

		if (ret) {

			log = AplicativoPD.gravaLog(txtUsu.getVlrString().toLowerCase().trim(), "PR", "LIB", "Liberação de " + param[0] + " abaixo do custo", param[0] + " [" + param[1] + "], " + // codigo
					// da
					// tabela
					"Item: [" + param[2] + "], " + // codigo do item
					"Produto: [" + param[3] + "], " + // codigo do produto
					"Preço: [" + param[4] + "]" // preço do produto
			, con);
		}

		return ret;
	}

	private boolean getAbreGaveta() {

		return getPermissao(ABRE_GAVETA);
	}

	private boolean getAltParcVenda() {

		return getPermissao(ALT_PARC_VENDA);
	}

	private boolean getAprovReceitaProd() {

		return getPermissao(APROV_RECEITA_PROD);
	}

	private boolean getLiberaCredito() {

		return getPermissao(LIBERA_CRED);
	}

	private boolean getLiberaCampoPesagem() {

		return getPermissao(LIBERA_CAMPO_PESAGEM);
	}

	private boolean getVendaImobilizado() {

		return getPermissao(VENDA_IMOBLIZIADO);
	}

	private boolean getPermissao(int tipo) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		Properties props = null;
		String sIDUsu = null;
		StringBuffer sSQL = new StringBuffer();
		boolean[] permissoes = new boolean[7];

		try {

			props = new Properties();
			sIDUsu = txtUsu.getVlrString().toLowerCase().trim();
			props.put("user", sIDUsu);
			props.put("password", txtPass.getVlrString());

			if ("".equals(sIDUsu) || "".equals(txtPass.getVlrString().trim())) {

				Funcoes.mensagemErro(this, "Campo em branco!");
				return false;
			}

			DriverManager.getConnection(Aplicativo.strBanco, props).close();

			sSQL.append("SELECT BAIXOCUSTOUSU, ABREGAVETAUSU, ALTPARCVENDA, APROVRECEITA, LIBERACREDUSU, VENDAPATRIMUSU, LIBERACAMPOPESAGEM ");
			sSQL.append("FROM SGUSUARIO ");
			sSQL.append("WHERE IDUSU=? AND CODEMP=? AND CODFILIAL=?");

			ps = con.prepareStatement(sSQL.toString());
			ps.setString(1, sIDUsu);
			ps.setInt(2, Aplicativo.iCodEmp);
			ps.setInt(3, Aplicativo.iCodFilial);

			rs = ps.executeQuery();

			if (rs.next()) {

				permissoes[0] = "S".equals(rs.getString(1));
				permissoes[1] = "S".equals(rs.getString(2));
				permissoes[2] = "S".equals(rs.getString(3));
				permissoes[3] = "S".equals(rs.getString(4));
				permissoes[4] = "S".equals(rs.getString(5));
				permissoes[5] = "S".equals(rs.getString(6));
				permissoes[6] = "S".equals(rs.getString(7));

			}

			if (!permissoes[tipo]) {

				Funcoes.mensagemErro(this, "Ação não permitida para este usuário ! ! !");
			}

		}
		catch (SQLException sqle) {
			if (sqle.getErrorCode() == 335544472) {

				Funcoes.mensagemErro(this, "Nome do usuário ou senha inválidos ! ! !");
			}
			else {

				Funcoes.mensagemErro(this, "Erro ao verificar senha.", true, con, sqle);
			}

			sqle.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ps = null;
			rs = null;
			props = null;
			sIDUsu = null;
			sSQL = null;
		}

		return permissoes[tipo];
	}

	public String[] getLog() {

		return new String[] { String.valueOf(Aplicativo.iCodEmp), String.valueOf(log[0]), String.valueOf(log[1]) };
	}

	public void setParam(String[] arg) {

		param = arg;
	}

	public void setTitulo(String arg) {

		if (!( arg != null && arg.trim().length() > 0 )) {

			arg = "Permissão";
		}

		super.setTitulo(arg);
	}

}
