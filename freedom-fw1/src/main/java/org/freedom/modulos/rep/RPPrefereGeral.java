/**
 * @version 02/2007 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * @author Alex Rodrigues<BR>
 * 
 * Projeto: Freedom <BR>
 * 
 * Pacote: org.freedom.modulos.rep <BR>
 * Classe:
 * @(#)RPPrefereGeral.java <BR>
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
 * Tela para cadastro de preferências para o sistema.
 * 
 */

package org.freedom.modulos.rep;

import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.freedom.acao.PostEvent;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JPasswordFieldPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDados;

public class RPPrefereGeral extends FDados implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final JCheckBoxPad cbFatLucr = new JCheckBoxPad("Mostrar fator de lucratividade?", "S", "N");

	private final JCheckBoxPad cbUsaRefProd = new JCheckBoxPad("Usar referência para produto?", "S", "N");

	private final JCheckBoxPad cbOrdemPed = new JCheckBoxPad("Ordena pedido por ordem alfabetíca?", "S", "N");

	private final JCheckBoxPad cbEnviaCopia = new JCheckBoxPad("Enviar cópia de pedido para empresa?", "S", "N");

	// private final JCheckBoxPad cbIpiComis = new JCheckBoxPad(
	// "Incluir IPI no calculo da comissão?", "S", "N" );

	// private final JCheckBoxPad cbIPIPed = new JCheckBoxPad(
	// "Imprimir IPI no pedido?", "S", "N" );

	// private final JCheckBoxPad cbCodBarProd = new JCheckBoxPad(
	// "Usar código de barras no pedido?", "S", "N" );

	// private final JCheckBoxPad cbEndCliPed = new JCheckBoxPad(
	// "Incluir endereço dos clientes no pedido?", "S", "N" );

	private final JTextFieldPad txtServidorSMTP = new JTextFieldPad(JTextFieldPad.TP_STRING, 50, 0);

	private final JTextFieldPad txtPortaSMTP = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 10, 0);

	private final JTextFieldPad txtUsuarioSMTP = new JTextFieldPad(JTextFieldPad.TP_STRING, 30, 0);

	private final JPasswordFieldPad txtSenhaSMTP = new JPasswordFieldPad(30);

	private final JCheckBoxPad cbAutenticaSMTP = new JCheckBoxPad("Autenticar ?", "S", "N");

	private final JCheckBoxPad cbSSLSMTP = new JCheckBoxPad("Usa SSL ?", "S", "N");

	private final JTextFieldPad txtCasasDesc = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 5, 0);

	private final JTextFieldPad txtCasasDescFin = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 5, 0);

	private final JTextFieldPad txtCodMoeda = new JTextFieldPad(JTextFieldPad.TP_STRING, 4, 0);

	private final JTextFieldFK txtNomeMoeda = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private final JTextFieldPad txtLayoutPed = new JTextFieldPad(JTextFieldPad.TP_STRING, 15, 0);

	private final JTextFieldPad txtCodUnid = new JTextFieldPad(JTextFieldPad.TP_STRING, 8, 0);

	private final JTextFieldFK txtDescUnid = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private final JTextFieldPad txtCodGrupo = new JTextFieldPad(JTextFieldPad.TP_STRING, 8, 0);

	private final JTextFieldFK txtDescGrupo = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JRadioGroup<?, ?> rgTipo = null;

	private final ListaCampos lcMoeda = new ListaCampos(this, "MO");

	private final ListaCampos lcUnid = new ListaCampos(this, "UD");

	private final ListaCampos lcGrupo = new ListaCampos(this, "GP");

	private Vector<String> vLabs1 = new Vector<String>();

	private Vector<String> vVals1 = new Vector<String>();

	public RPPrefereGeral() {

		super(false);
		setTitulo("Preferências gerais");
		setAtribos(50, 50, 425, 650);

		montaListaCampos();
		montaTela();
		setListaCampos(false, "PREFERE1", "SG");

		nav.setAtivo(0, false);
		lcCampos.setPodeExc(false);

		lcCampos.addPostListener(this);
	}

	private void montaListaCampos() {

		/*********
		 * MOEDA *
		 *********/

		lcMoeda.add(new GuardaCampo(txtCodMoeda, "CodMoeda", "Cód.moeda", ListaCampos.DB_PK, false));
		lcMoeda.add(new GuardaCampo(txtNomeMoeda, "SingMoeda", "Descrição da moeda", ListaCampos.DB_SI, false));
		lcMoeda.montaSql(false, "MOEDA", "RP");
		lcMoeda.setQueryCommit(false);
		lcMoeda.setReadOnly(true);
		txtCodMoeda.setTabelaExterna(lcMoeda, null);

		/***********
		 * UNIDADE *
		 ***********/

		lcUnid.add(new GuardaCampo(txtCodUnid, "CodUnid", "Cód.unidade", ListaCampos.DB_PK, false));
		lcUnid.add(new GuardaCampo(txtDescUnid, "DescUnid", "Descrição da unidade", ListaCampos.DB_SI, false));
		lcUnid.montaSql(false, "UNIDADE", "RP");
		lcUnid.setQueryCommit(false);
		lcUnid.setReadOnly(true);
		txtCodUnid.setTabelaExterna(lcUnid, null);

		/***********
		 * GRUPO *
		 ***********/

		lcGrupo.add(new GuardaCampo(txtCodGrupo, "CodGrup", "Cód.grupo", ListaCampos.DB_PK, false));
		lcGrupo.add(new GuardaCampo(txtDescGrupo, "DescGrup", "Descrição do grupo", ListaCampos.DB_SI, false));
		lcGrupo.montaSql(false, "GRUPO", "RP");
		lcGrupo.setQueryCommit(false);
		lcGrupo.setReadOnly(true);
		txtCodGrupo.setTabelaExterna(lcGrupo, null);
	}

	private void montaTela() {

		JLabel geral = new JLabel("Geral", SwingConstants.CENTER);
		geral.setOpaque(true);
		JLabel linha1 = new JLabel();
		linha1.setBorder(BorderFactory.createEtchedBorder());

		adic(geral, 27, 0, 80, 20);
		adic(linha1, 7, 10, 397, 110);

		adicDB(cbUsaRefProd, 17, 20, 300, 20, "UsaRefProd", null, true);
		adicDB(cbOrdemPed, 17, 40, 300, 20, "OrdemPed", null, true);
		adicDB(cbEnviaCopia, 17, 60, 300, 20, "EnviaCopia", null, true);
		adicDB(cbFatLucr, 17, 80, 300, 20, "MostraFatLucr", null, true);

		// adicDB( cbIpiComis, 17, 40, 300, 20, "IPIPed", null, true );
		// adicDB( cbIPIPed, 17, 60, 300, 20, "CodBarProd", null, true );
		// adicDB( cbCodBarProd, 17, 80, 300, 20, "EndCliPed", null, true );
		// adicDB( cbEndCliPed, 17, 100, 300, 20, "OrdemPed", null, true );

		JLabel preco = new JLabel("Tipo de calculo do lucro", SwingConstants.CENTER);
		preco.setOpaque(true);
		JLabel linha4 = new JLabel();
		linha4.setBorder(BorderFactory.createEtchedBorder());

		adic(preco, 27, 130, 150, 20);
		adic(linha4, 7, 140, 397, 75);

		vLabs1.addElement("Venda");
		vLabs1.addElement("Custo");
		vVals1.addElement("V");
		vVals1.addElement("C");

		rgTipo = new JRadioGroup<String, String>(1, 2, vLabs1, vVals1);
		rgTipo.setVlrString("V");

		adicDB(rgTipo, 70, 160, 250, 35, "TpCalcLucro", "", false);

		JLabel email = new JLabel("E - Mail", SwingConstants.CENTER);
		email.setOpaque(true);
		JLabel linha2 = new JLabel();
		linha2.setBorder(BorderFactory.createEtchedBorder());

		adic(email, 27, 225, 80, 20);
		adic(linha2, 7, 235, 397, 100);

		adicCampo(txtServidorSMTP, 17, 260, 230, 20, "ServidorSMTP", "Servidor de SMTP", ListaCampos.DB_SI, false);
		adicCampo(txtPortaSMTP, 250, 260, 41, 20, "PortaSMTP", "Porta", ListaCampos.DB_SI, false);
		adicDB(cbAutenticaSMTP, 294, 260, 100, 20, "AutenticaSMTP", "", false);
		adicCampo(txtUsuarioSMTP, 17, 300, 137, 20, "UsuarioSMTP", "Id do usuario", ListaCampos.DB_SI, false);
		adicCampo(txtSenhaSMTP, 157, 300, 134, 20, "SenhaSMTP", "Senha do usuario", ListaCampos.DB_SI, false);
		adicDB(cbSSLSMTP, 294, 300, 100, 20, "SSLSMTP", "", false);

		JLabel campos = new JLabel("Campos", SwingConstants.CENTER);
		campos.setOpaque(true);
		JLabel linha3 = new JLabel();
		linha3.setBorder(BorderFactory.createEtchedBorder());

		adic(campos, 27, 340, 80, 20);
		adic(linha3, 7, 350, 397, 220);

		adicCampo(txtCasasDesc, 17, 380, 150, 20, "CasasDec", "Decimais", ListaCampos.DB_SI, false);
		adicCampo(txtLayoutPed, 240, 400, 154, 20, "LayoutPed", "Classe para pedido", ListaCampos.DB_SI, false);
		adicCampo(txtCasasDescFin, 17, 420, 150, 20, "CasasDecFin", "Decimais ( financeiro )", ListaCampos.DB_SI, false);
		adicCampo(txtCodMoeda, 17, 460, 100, 20, "CodMoeda", "Cód.moeda", ListaCampos.DB_FK, txtNomeMoeda, false);
		adicDescFK(txtNomeMoeda, 120, 460, 274, 20, "SingMoeda", "Descrição da moeda");
		adicCampo(txtCodUnid, 17, 500, 100, 20, "CodUnid", "Cód.Unid.", ListaCampos.DB_FK, txtDescUnid, false);
		adicDescFK(txtDescUnid, 120, 500, 274, 20, "DescUnid", "Descrição da unidade");
		adicCampo(txtCodGrupo, 17, 540, 100, 20, "CodGrup", "Cód.grupo", ListaCampos.DB_FK, txtDescGrupo, false);
		adicDescFK(txtDescGrupo, 120, 540, 274, 20, "DescGrup", "Descrição do grupo");

	}

	@Override
	public void afterPost(PostEvent pevt) {

		super.afterPost(pevt);

		AplicativoRep.atualizaEmailBean(con);
	}

	@Override
	public synchronized void setConexao(DbConnection cn) {

		super.setConexao(cn);
		lcMoeda.setConexao(cn);
		lcUnid.setConexao(cn);
		lcGrupo.setConexao(cn);
		lcCampos.carregaDados();
	}

	public static List<Object> getPrefere(final DbConnection con) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sSQL = new StringBuilder();
		List<Object> prefere = new ArrayList<Object>(EPrefere.values().length);

		try {

			sSQL.append("SELECT IPICOMIS,IPIPED,CODBARPROD,ENDCLIPED,ORDEMPED,");
			sSQL.append("SERVIDORSMTP,PORTASMTP,USUARIOSMTP,SENHASMTP,AUTENTICASMTP,SSLSMTP,");
			sSQL.append("CASASDEC,CASASDECFIN,CODMOEDA,LAYOUTPED,USAREFPROD, ENVIACOPIA, TPCALCLUCRO, CODGRUP, CODUNID, MOSTRAFATLUCR ");
			sSQL.append("FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?");
			ps = con.prepareStatement(sSQL.toString());
			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, ListaCampos.getMasterFilial("SGPREFERE1"));
			rs = ps.executeQuery();

			if (rs.next()) {

				prefere.add(EPrefere.IPICOMIS.ordinal(), rs.getString("IPICOMIS"));
				prefere.add(EPrefere.IPIPED.ordinal(), rs.getString("IPIPED"));
				prefere.add(EPrefere.CODBARPROD.ordinal(), rs.getString("CODBARPROD"));
				prefere.add(EPrefere.ENDCLIPED.ordinal(), rs.getString("ENDCLIPED"));
				prefere.add(EPrefere.ORDEMPED.ordinal(), rs.getString("ORDEMPED"));
				prefere.add(EPrefere.SERVIDORSMTP.ordinal(), rs.getString("SERVIDORSMTP"));
				prefere.add(EPrefere.PORTASMTP.ordinal(), rs.getInt("PORTASMTP"));
				prefere.add(EPrefere.USUARIOSMTP.ordinal(), rs.getString("USUARIOSMTP"));
				prefere.add(EPrefere.SENHASMTP.ordinal(), rs.getString("SENHASMTP"));
				prefere.add(EPrefere.AUTENTICASMTP.ordinal(), rs.getString("AUTENTICASMTP"));
				prefere.add(EPrefere.SSLSMTP.ordinal(), rs.getString("SSLSMTP"));
				prefere.add(EPrefere.CASASDEC.ordinal(), rs.getInt("CASASDEC"));
				prefere.add(EPrefere.CASASDECFIN.ordinal(), rs.getInt("CASASDECFIN"));
				prefere.add(EPrefere.CODMOEDA.ordinal(), rs.getString("CODMOEDA"));
				prefere.add(EPrefere.LAYOUTPED.ordinal(), rs.getString("LAYOUTPED"));
				prefere.add(EPrefere.USAREFPROD.ordinal(), rs.getString("USAREFPROD"));
				prefere.add(EPrefere.ENVIACOPIA.ordinal(), rs.getString("ENVIACOPIA"));
				prefere.add(EPrefere.TPCALCLUCRO.ordinal(), rs.getString("TPCALCLUCRO"));
				prefere.add(EPrefere.CODGRUPO.ordinal(), rs.getString("CODGRUP"));
				prefere.add(EPrefere.CODUNID.ordinal(), rs.getString("CODUNID"));
				prefere.add(EPrefere.MOSTRAFATLUCRO.ordinal(), rs.getString("MOSTRAFATLUCR"));
			}

			rs.close();
			ps.close();

			con.commit();

		}
		catch (Exception e) {
			Funcoes.mensagemErro(null, "Erro ao buscar preferências!\n" + e.getMessage());
			e.printStackTrace();
		}

		return prefere;
	}

	public enum EPrefere {
		IPICOMIS, IPIPED, CODBARPROD, ENDCLIPED, ORDEMPED, SERVIDORSMTP, PORTASMTP, USUARIOSMTP, SENHASMTP, AUTENTICASMTP, SSLSMTP, CASASDEC, CASASDECFIN, CODMOEDA, LAYOUTPED, USAREFPROD, ENVIACOPIA, TPCALCLUCRO, CODGRUPO, CODUNID, MOSTRAFATLUCRO;
	}
}