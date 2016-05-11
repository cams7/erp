/**
 * @version 30/05/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.cfg <BR>
 *         Classe:
 * @(#)FUsuario.java <BR>
 * 
 *                   Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                   modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                   na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                   Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                   sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                   Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                   Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                   de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                   Formulário de cadastro de usuários do sistema.
 * 
 */

package org.freedom.modulos.cfg.view.frame.crud.tabbed;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JList;

import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.CheckBoxEvent;
import org.freedom.acao.CheckBoxListener;
import org.freedom.acao.DeleteEvent;
import org.freedom.acao.DeleteListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JPasswordFieldPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FTabDados;
import org.freedom.modulos.cfg.view.frame.crud.plain.FGrupoUsu;
import org.freedom.modulos.crm.view.frame.crud.plain.FConfEmail;
import org.freedom.modulos.std.view.frame.crud.plain.FAlmox;
import org.freedom.modulos.std.view.frame.crud.special.FCentroCusto;

public class FUsuario extends FTabDados implements PostListener, DeleteListener, CarregaListener, ChangeListener,
		InsertListener, ActionListener, CheckBoxListener {

	private static final long serialVersionUID = 1L;

	private static Vector<String> vCodDisp = new Vector<String>();

	private static Vector<String> vCodEmp = new Vector<String>();

	private static Vector<String> vDisp = new Vector<String>();

	private static Vector<String> vEmp = new Vector<String>();

	private JButtonPad btAdicEmp = new JButtonPad(Icone.novo("btFlechaDir.png"));

	private JButtonPad btDelEmp = new JButtonPad(Icone.novo("btFlechaEsq.png"));

	private JCheckBoxPad cbAbreGaveta = new JCheckBoxPad("Abrir gaveta no PDV", "S", "N");

	private JCheckBoxPad cbAlmoxarife = new JCheckBoxPad("Atuar como Almoxarife", "S", "N");

	private JCheckBoxPad cbAltParc = new JCheckBoxPad("Alterar parcelas da venda", "S", "N");

	private JCheckBoxPad cbBaixoCusto = new JCheckBoxPad("Vender abaixo do custo", "S", "N");

	private JCheckBoxPad cbCompra = new JCheckBoxPad("Realizar compras", "S", "N");

	private JCheckBoxPad cbReceita = new JCheckBoxPad("Vender produto com receita", "S", "N");

	private JCheckBoxPad cbAtivCli = new JCheckBoxPad("Ativar clientes", "S", "N");

	private JCheckBoxPad cbLiberaCred = new JCheckBoxPad("Liberar crédito", "S", "N");

	private JCheckBoxPad cbCancelaOP = new JCheckBoxPad("Cancelar OP de outro usuário", "S", "N");

	private JCheckBoxPad cbVendaImobilizado = new JCheckBoxPad("Vender imobilizado", "S", "N");

	private JCheckBoxPad cbRMAOutCC = new JCheckBoxPad("Lançar RMA em outros C.C.", "S", "N");

	private JCheckBoxPad cbVisualizaLucr = new JCheckBoxPad("Visualizar Lucratividade", "S", "N");

	private JCheckBoxPad cbLiberaCampoPesagem = new JCheckBoxPad("Libera campo pesagem", "S", "N");

	private JCheckBoxPad cbAprovOrdCP = new JCheckBoxPad("Aprova ordens de compra", "S", "N");

	private JList<Object> lsDisp = new JList<>();

	private JList<Object> lsEmp = new JList<>();

	private JPanelPad pinAcesso = new JPanelPad();

	private JPanelPad pinAcessoPCP = new JPanelPad();

	private JPanelPad pinCor = new JPanelPad(JPanelPad.TP_JPANEL, new GridLayout(1, 1));

	private JPanelPad pinGeral = new JPanelPad();

	private JPanelPad pinEmail = new JPanelPad();

	private JRadioGroup<?, ?> rgAprovaRMA = null;

	private JRadioGroup<?, ?> rgAprovaSolicitacao = null;

	private JTextAreaPad txaComentUsu = new JTextAreaPad();

	private JScrollPane spnDisp = new JScrollPane(lsDisp);

	private JScrollPane spnEmp = new JScrollPane(lsEmp);

	private JScrollPane spnObs = new JScrollPane(txaComentUsu);

	private JPasswordFieldPad txpConfirma = new JPasswordFieldPad(8);

	private JPasswordFieldPad txpSenha = new JPasswordFieldPad(8);

	private JTextFieldPad txtAnoCC = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 4, 0);

	private JTextFieldPad txtCodAlmox = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldPad txtCodConfEmail = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldPad txtCodCC = new JTextFieldPad(JTextFieldPad.TP_STRING, 19, 0);

	private JTextFieldFK txtDescAlmox = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldFK txtNomeRemet = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldFK txtDescCC = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldFK txtDescGrup = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldPad txtIDGrpUsu = new JTextFieldPad(JTextFieldPad.TP_STRING, 8, 0);

	private JTextFieldPad txtIDUsu = new JTextFieldPad(JTextFieldPad.TP_STRING, 8, 0);

	private JTextFieldPad txtNomeUsu = new JTextFieldPad(JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldPad txtPNomeUsu = new JTextFieldPad(JTextFieldPad.TP_STRING, 20, 0);

	private JTextFieldPad txtUNomeUsu = new JTextFieldPad(JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldPad txtCorAgenda = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 10, 0);

	private JCheckBoxPad cbAcesOpBtCadlote = new JCheckBoxPad("Botão cadastro de lotes", "S", "N");

	private JCheckBoxPad cbAcesOpBtRma = new JCheckBoxPad("Botão RMA", "S", "N");

	private JCheckBoxPad cbAcesOpBtQualid = new JCheckBoxPad("Botão controle de qualidade", "S", "N");

	private JCheckBoxPad cbAcesOpBtDistr = new JCheckBoxPad("Botão distribuição", "S", "N");

	private JCheckBoxPad cbAcesOpBtFase = new JCheckBoxPad("Botão fases", "S", "N");

	private JCheckBoxPad cbAcesOpBtCanc = new JCheckBoxPad("Botão cancelamento de OP", "S", "N");

	private JCheckBoxPad cbAcesOpBtSubprod = new JCheckBoxPad("Botão sub-produtos", "S", "N");

	private JCheckBoxPad cbAcesOpBtRemessa = new JCheckBoxPad("Botão remessa", "S", "N");

	private JCheckBoxPad cbAcesOpBtRetorno = new JCheckBoxPad("Botão retorno", "S", "N");

	private JCheckBoxPad cbAcesOpBtVeritens = new JCheckBoxPad("Grade de visualização de itens", "S", "N");

	private DbConnection conIB = null;

	private ListaCampos lcAlmox = new ListaCampos(this, "AM");

	private ListaCampos lcConfEmail = new ListaCampos(this, "CE");

	private ListaCampos lcCC = new ListaCampos(this, "CC");

	private ListaCampos lcGrup = new ListaCampos(this, "IG");

	private Vector<String> vAprovaRMALab = new Vector<String>();

	private Vector<String> vAprovaRMAVal = new Vector<String>();

	private Vector<String> vAprovaSolicitacaoLab = new Vector<String>();

	private Vector<String> vAprovaSolicitacaoVal = new Vector<String>();

	private JColorChooser tcc = new JColorChooser();

	private JPanelPad pnPermissoes = new JPanelPad();

	private JPanelPad pnAcessoOP = new JPanelPad();

	private JPanelPad pnAprovacoes = new JPanelPad();

	public FUsuario() {

		super();
		setTitulo("Cadastro de Usuários");
		setAtribos(50, 50, 470, 490);

		vAprovaSolicitacaoLab.add("Nenhuma");
		vAprovaSolicitacaoLab.add("Mesmo Centro de Custo");
		vAprovaSolicitacaoLab.add("Todas");

		vAprovaSolicitacaoVal.add("ND");
		vAprovaSolicitacaoVal.add("CC");
		vAprovaSolicitacaoVal.add("TD");

		vAprovaRMALab.add("Nenhuma");
		vAprovaRMALab.add("Mesmo Centro de Custo");
		vAprovaRMALab.add("Todas");

		vAprovaRMAVal.add("ND");
		vAprovaRMAVal.add("CC");
		vAprovaRMAVal.add("TD");

		rgAprovaSolicitacao = new JRadioGroup<String, String>(3, 1, vAprovaSolicitacaoLab, vAprovaSolicitacaoVal);
		rgAprovaRMA = new JRadioGroup<String, String>(3, 1, vAprovaRMALab, vAprovaRMAVal);

		txpSenha.setListaCampos(lcCampos);
		txpConfirma.setListaCampos(lcCampos);
		setBordaReq(txpSenha);
		setBordaReq(txpConfirma);

		lcGrup.add(new GuardaCampo(txtIDGrpUsu, "IDGRPUSU", "ID grupo", ListaCampos.DB_PK, true));
		lcGrup.add(new GuardaCampo(txtDescGrup, "NOMEGRPUSU", "Descriçao do grupo", ListaCampos.DB_SI, false));
		lcGrup.montaSql(false, "GRPUSU", "SG");
		lcGrup.setQueryCommit(false);
		lcGrup.setReadOnly(true);
		txtIDGrpUsu.setTabelaExterna(lcGrup, FGrupoUsu.class.getCanonicalName());

		lcCC.add(new GuardaCampo(txtCodCC, "CodCC", "Cód.c.c.", ListaCampos.DB_PK, false));
		lcCC.add(new GuardaCampo(txtAnoCC, "AnoCC", "Ano.c.c.", ListaCampos.DB_PK, false));
		lcCC.add(new GuardaCampo(txtDescCC, "DescCC", "Descriçao do centro de custo", ListaCampos.DB_SI, false));
		lcCC.montaSql(false, "CC", "FN");
		lcCC.setQueryCommit(false);
		lcCC.setReadOnly(true);

		txtAnoCC.setTabelaExterna(lcCC, FCentroCusto.class.getCanonicalName());
		txtAnoCC.setNomeCampo("anocc");
		txtCodCC.setTabelaExterna(lcCC, FCentroCusto.class.getCanonicalName());
		txtCodCC.setNomeCampo("codcc");

		lcAlmox.add(new GuardaCampo(txtCodAlmox, "CodAlmox", "Cod.almox.", ListaCampos.DB_PK, false));
		lcAlmox.add(new GuardaCampo(txtDescAlmox, "DescAlmox", "Descrição do almoxarifado", ListaCampos.DB_SI, false));
		lcAlmox.montaSql(false, "ALMOX", "EQ");
		lcAlmox.setQueryCommit(false);
		lcAlmox.setReadOnly(true);
		txtCodAlmox.setTabelaExterna(lcAlmox, FAlmox.class.getCanonicalName());

		lcConfEmail.add(new GuardaCampo(txtCodConfEmail, "CodConfEmail", "Cd.Conf.Email", ListaCampos.DB_PK, false));
		lcConfEmail.add(new GuardaCampo(txtNomeRemet, "NomeRemet", "Nome do remetente", ListaCampos.DB_SI, false));
		lcConfEmail.montaSql(false, "CONFEMAIL", "TK");
		lcConfEmail.setQueryCommit(false);
		lcConfEmail.setReadOnly(true);
		txtCodConfEmail.setTabelaExterna(lcConfEmail, FConfEmail.class.getCanonicalName());

		adicTab("Usuario", pinGeral);
		setPainel(pinGeral);

		adicCampo(txtIDUsu, 7, 20, 80, 20, "IDUsu", "ID.usu.", ListaCampos.DB_PK, true);
		adicCampo(txtNomeUsu, 90, 20, 350, 20, "NomeUsu", "Nome do usuário", ListaCampos.DB_SI, true);
		adicCampo(txtPNomeUsu, 7, 60, 180, 20, "PNomeUsu", "Primeiro nome", ListaCampos.DB_SI, true);
		adicCampo(txtUNomeUsu, 190, 60, 250, 20, "UNomeUsu", "Último nome", ListaCampos.DB_SI, true);
		adicCampo(txtIDGrpUsu, 7, 100, 70, 20, "IDGRPUSU", "ID.grupo", ListaCampos.DB_FK, true);
		adicDescFK(txtDescGrup, 80, 100, 216, 20, "NOMEGRPUSU", "Descrição do grupo do usuário");
		adicCampoInvisivel(txtCorAgenda, "CORAGENDA", "", ListaCampos.DB_SI, false);
		adic(new JLabelPad("Senha"), 300, 80, 70, 20);
		adic(txpSenha, 300, 100, 70, 20);
		adic(new JLabelPad("Confirma"), 373, 80, 70, 20);
		adic(txpConfirma, 373, 100, 70, 20);
		adicCampo(txtAnoCC, 7, 140, 50, 20, "AnoCC", "Ano c.c.", ListaCampos.DB_FK, txtDescCC, false);
		adicCampo(txtCodCC, 60, 140, 110, 20, "CodCC", "Cód.c.c.", ListaCampos.DB_FK, txtDescCC, false);
		adicDescFK(txtDescCC, 173, 140, 269, 20, "DescCC", "Descrição do centro de custo");

		adicDBLiv(txaComentUsu, "ComentUsu", "Comentário", false);
		adic(new JLabelPad("Comentário"), 7, 160, 100, 20);
		adic(spnObs, 7, 180, 435, 60);
		adic(new JLabelPad("Filiais disponíveis:"), 7, 240, 120, 20);
		adic(spnDisp, 7, 260, 195, 100);
		adic(btAdicEmp, 210, 275, 30, 30);
		adic(btDelEmp, 210, 315, 30, 30);
		adic(new JLabelPad("Acesso:"), 247, 240, 158, 20);
		adic(spnEmp, 247, 260, 195, 100);

		adicTab("Acesso", pinAcesso);
		pinAcesso.adic(pnPermissoes, 3, 0, 440, 170);
		pnPermissoes.setBorder(BorderFactory.createTitledBorder("Permissões"));

		setPainel(pnPermissoes);

		adicDB(cbBaixoCusto, 4, 0, 180, 20, "BaixoCustoUsu", "", false);
		adicDB(cbReceita, 4, 20, 190, 20, "AprovReceita", "", false);
		adicDB(cbAltParc, 4, 40, 180, 20, "AltParcVenda", "", false);
		adicDB(cbAbreGaveta, 4, 60, 180, 20, "AbreGavetaUsu", "", false);
		adicDB(cbAlmoxarife, 4, 80, 180, 20, "AlmoxarifeUsu", "", false);
		adicDB(cbCompra, 4, 100, 200, 20, "ComprasUsu", "", false);
		adicDB(cbAtivCli, 200, 00, 200, 20, "AtivCli", "", true);
		adicDB(cbLiberaCred, 200, 20, 200, 20, "LiberaCredUsu", "", true);
		adicDB(cbCancelaOP, 200, 40, 250, 20, "CancelaOP", "", false);
		adicDB(cbVendaImobilizado, 200, 60, 200, 20, "VENDAPATRIMUSU", "", false);
		adicDB(cbRMAOutCC, 200, 80, 200, 20, "RMAOUTCC", "", false);
		adicDB(cbVisualizaLucr, 200, 100, 200, 20, "VISUALIZALUCR", "", false);

		adicDB(cbLiberaCampoPesagem, 200, 120, 200, 20, "LIBERACAMPOPESAGEM", "", false);

		txtCodAlmox.setRequerido(cbAlmoxarife.isSelected());

		pinAcesso.adic(pnAprovacoes, 3, 175, 440, 220);
		pnAprovacoes.setBorder(BorderFactory.createTitledBorder("Aprovações"));

		setPainel(pnAprovacoes);

		adicCampo(txtCodAlmox, 4, 20, 100, 20, "CodAlmox", "Cód.almox.", ListaCampos.DB_FK, false);
		adicDescFK(txtDescAlmox, 107, 20, 318, 20, "DescAlmox", "Descrição do almoxarifado");
		adicDB(rgAprovaSolicitacao, 4, 65, 200, 80, "AprovCPSolicitacaoUsu", "Solicitação de compra", true);
		adicDB(rgAprovaRMA, 225, 65, 200, 80, "AprovRMAUsu", "Requisição de material", true);
		adicDB(cbAprovOrdCP, 7, 150, 200, 20, "AprovOrdCP", "", true);

		// Acessos especiais ao PCP
		adicTab("Acesso PCP", pinAcessoPCP);
		pinAcessoPCP.adic(pnAcessoOP, 3, 0, 440, 240);
		pnAcessoOP.setBorder(BorderFactory.createTitledBorder("Permissões OP"));
		setPainel(pnAcessoOP);
		adicDB(cbAcesOpBtCadlote, 4, 0, 220, 20, "AcesOpBtCadlote", "", true);
		adicDB(cbAcesOpBtRma, 4, 20, 220, 20, "AcesOpBtRma", "", true);
		adicDB(cbAcesOpBtQualid, 4, 40, 220, 20, "AcesOpBtQualid", "", true);
		adicDB(cbAcesOpBtDistr, 4, 60, 220, 20, "AcesOpBtDistr", "", true);
		adicDB(cbAcesOpBtFase, 4, 80, 220, 20, "AcesOpBtFase", "", true);
		adicDB(cbAcesOpBtCanc, 4, 100, 220, 20, "AcesOpBtCanc", "", true);
		adicDB(cbAcesOpBtSubprod, 4, 120, 220, 20, "AcesOpBtSubprod", "", true);
		adicDB(cbAcesOpBtRemessa, 4, 140, 220, 20, "AcesOpBtRemessa", "", true);
		adicDB(cbAcesOpBtRetorno, 4, 160, 220, 20, "AcesOpBtRetorno", "", true);
		adicDB(cbAcesOpBtVeritens, 4, 180, 220, 20, "AcesOpVeritens", "", true);

		adicTab("Cor", pinCor);
		setPainel(pinCor);

		adicTab("Email", pinEmail);
		setPainel(pinEmail);

		adicCampo(txtCodConfEmail, 7, 20, 100, 20, "CodConfEmail", "Cd.C.Email", ListaCampos.DB_FK, false);
		adicDescFK(txtNomeRemet, 110, 20, 330, 20, "NomeRemet", "Nome do remetente");

		setListaCampos(false, "USUARIO", "SG");
		lcCampos.setQueryInsert(false);

		lcCampos.addCarregaListener(this);
		lcCC.addCarregaListener(this);
		lcCampos.addPostListener(this);
		lcCampos.addInsertListener(this);
		lcCampos.addDeleteListener(this);

		cbAlmoxarife.addCheckBoxListener(this);

		btAdicEmp.addActionListener(this);
		btDelEmp.addActionListener(this);

		super.setBordaReq(txpSenha);
		super.setBordaReq(txpConfirma);

		pinCor.add(tcc, BorderLayout.CENTER);
		tcc.getSelectionModel().addChangeListener(this);

	}

	private void adicionaEmp() {

		if (lsDisp.isSelectionEmpty()) {
			return;
		}
		for (int i = lsDisp.getMaxSelectionIndex(); i >= 0; i--) {

			if (lsDisp.isSelectedIndex(i)) {

				vEmp.add(vDisp.elementAt(i));
				vDisp.remove(i);
				vCodEmp.add(vCodDisp.elementAt(i));
				vCodDisp.remove(i);
			}
		}

		lsDisp.setListData(vDisp);
		lsEmp.setListData(vEmp);
		lcCampos.edit();
	}

	private int buscaAnoBaseCC() {

		int iRet = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = con.prepareStatement("SELECT ANOCENTROCUSTO FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?");
			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, ListaCampos.getMasterFilial("SGPREFERE1"));

			rs = ps.executeQuery();

			if (rs.next()) {
				iRet = rs.getInt("ANOCENTROCUSTO");
			}

			rs.close();
			ps.close();

			con.commit();
		} catch (SQLException err) {
			err.printStackTrace();
			Funcoes.mensagemErro(this, "Erro ao buscar o ano-base para o centro de custo.\n" + err.getMessage(), true,
					con, err);
		} finally {
			ps = null;
			rs = null;
		}
		return iRet;
	}

	private void carregaAcesso() {

		int iPos = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();

		try {

			sSQL.append("SELECT FL.CODFILIAL,FL.NOMEFILIAL ");
			sSQL.append("FROM SGFILIAL FL, SGACESSOEU AC ");
			sSQL.append("WHERE AC.IDUSU=? AND FL.CODEMP=AC.CODEMPFL AND FL.CODFILIAL=AC.CODFILIALFL");

			ps = con.prepareStatement(sSQL.toString());
			ps.setString(1, txtIDUsu.getVlrString());

			rs = ps.executeQuery();

			vEmp.clear();
			vCodEmp.clear();

			while (rs.next()) {

				vCodEmp.addElement(rs.getString("CodFilial"));
				vEmp.addElement(rs.getString("NomeFilial") != null ? rs.getString("NomeFilial").trim() : "");

				iPos = vCodDisp.indexOf(rs.getString("CodFilial"));

				if (vCodDisp == null) {
					vCodDisp.remove(iPos);
					vDisp.remove(iPos);
				}
			}

			rs.close();
			ps.close();

			con.commit();

			lsEmp.setListData(vEmp);
			lsDisp.setListData(vDisp);
		} catch (SQLException err) {
			err.printStackTrace();
			Funcoes.mensagemInforma(this, "Não foi carregar as filiais que o usuário tem acesso.\n" + err);
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}
	}

	private void carregaDisp() {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = con.prepareStatement("SELECT CODFILIAL,NOMEFILIAL FROM SGFILIAL WHERE CODEMP=?");
			ps.setInt(1, Aplicativo.iCodEmp);

			rs = ps.executeQuery();

			vDisp.clear();
			vCodDisp.clear();

			while (rs.next()) {

				vCodDisp.addElement(rs.getString("CodFilial"));
				vDisp.addElement(rs.getString("NomeFilial") != null ? rs.getString("NomeFilial").trim() : "");
			}

			rs.close();
			ps.close();

			con.commit();

			lsDisp.setListData(vDisp);
		} catch (SQLException err) {
			err.printStackTrace();
			Funcoes.mensagemErro(this, "Não foi carregar as filiais diponíveis.\n" + err);
		} finally {
			ps = null;
			rs = null;
		}
	}

	private void gravaAcesso() {

		String sSep = null;
		String sSqlI = null;
		String sSqlD = null;
		String sSqlG = null;

		PreparedStatement ps = null;

		try {

			sSep = "";
			sSqlI = "";

			for (int i = 0; i < vCodEmp.size(); i++) {

				sSqlI += sSep + vCodEmp.elementAt(i);
				sSep = ",";
			}

			sSqlD = "DELETE FROM SGACESSOEU WHERE IDUSU=? AND CODEMP=?";

			ps = con.prepareStatement(sSqlD);
			ps.setString(1, txtIDUsu.getVlrString());
			ps.setInt(2, Aplicativo.iCodEmp);

			ps.executeUpdate();

			ps.close();

			con.commit();

			if (vCodEmp.size() > 0) {

				sSqlI = "INSERT INTO SGACESSOEU (CODEMP,CODFILIAL,IDUSU,CODEMPFL,CODFILIALFL) " + "SELECT CODEMP,"
						+ Aplicativo.iCodFilial + ",'" + txtIDUsu.getVlrString() + "',CODEMP,CODFILIAL FROM SGFILIAL "
						+ "WHERE CODEMP=? AND CODFILIAL IN (" + sSqlI + ")";

				ps = con.prepareStatement(sSqlI);
				ps.setInt(1, Aplicativo.iCodEmp);
				ps.executeUpdate();
				ps.close();
				con.commit();
			}

			sSqlG = "GRANT " + txtIDGrpUsu.getVlrString().trim() + " TO USER \"" + txtIDUsu.getVlrString().trim()
					+ "\"";

			ps = con.prepareStatement(sSqlG);

			ps.executeUpdate();

			ps.close();

			con.commit();
		} catch (SQLException err) {
			err.printStackTrace();
			Funcoes.mensagemInforma(this, "Erro ao cadastrar o acesso!\n" + err.getMessage());
		} finally {
			sSep = null;
			sSqlI = null;
			sSqlD = null;
			sSqlG = null;
			ps = null;
		}
	}

	private void removeEmp() {

		if (lsEmp.isSelectionEmpty()) {
			return;
		}

		for (int i = lsEmp.getMaxSelectionIndex(); i >= 0; i--) {

			if (lsEmp.isSelectedIndex(i)) {

				vDisp.add(vEmp.elementAt(i));
				vEmp.remove(i);
				vCodDisp.add(vCodEmp.elementAt(i));
				vCodEmp.remove(i);
			}
		}

		lsDisp.setListData(vDisp);
		lsEmp.setListData(vEmp);
		lcCampos.edit();
	}

	public void actionPerformed(ActionEvent evt) {

		if (evt.getSource() == btAdicEmp) {

			adicionaEmp();
		} else if (evt.getSource() == btDelEmp) {

			removeEmp();
		}

		super.actionPerformed(evt);
	}

	public void beforeCarrega(CarregaEvent pevt) {

		if (pevt.getListaCampos() == lcCC) {

			// System.out.println( "Carrega CC" );
		} else if ((pevt.getListaCampos() == lcCampos) && (txtIDUsu.getText() != null)) {
			txtIDUsu.setText(txtIDUsu.getText().toLowerCase());
		}

	}

	public void afterCarrega(CarregaEvent pevt) {

		carregaDisp();
		carregaAcesso();
		txpSenha.setVlrString("88888888");
		txpConfirma.setVlrString("88888888");
		txtCodAlmox.setRequerido(cbAlmoxarife.isSelected());

		Color cor = new Color(txtCorAgenda.getVlrInteger());

		tcc.setColor(cor);
		tcc.repaint();
		tcc.revalidate();
	}

	public void beforeDelete(DeleteEvent devt) {

		PreparedStatement ps = null;

		if ("SYSDBA".equals(txtIDUsu.getVlrString().toUpperCase())) {

			return;
		}

		try {

			ps = conIB.prepareStatement("execute procedure deleteuser(?)");
			ps.setString(1, txtIDUsu.getVlrString());

			ps.execute();

			ps.close();

			con.commit();
		} catch (SQLException err) {
			err.printStackTrace();
			Funcoes.mensagemInforma(this, "Não foi possível excluir usuário no banco de dados.\n" + err);
			devt.cancela();
		} finally {
			ps = null;
		}
	}

	public void afterDelete(DeleteEvent devt) {

	}

	public void beforeInsert(InsertEvent ievt) {

	}

	public void afterInsert(InsertEvent ievt) {

		carregaDisp();
		carregaAcesso();
		cbAtivCli.setVlrString("N");

		cbAcesOpBtCadlote.setVlrString("S");
		cbAcesOpBtRma.setVlrString("S");
		cbAcesOpBtQualid.setVlrString("S");
		cbAcesOpBtDistr.setVlrString("S");
		cbAcesOpBtFase.setVlrString("S");
		cbAcesOpBtCanc.setVlrString("S");
		cbAcesOpBtSubprod.setVlrString("S");
		cbAcesOpBtRemessa.setVlrString("S");
		cbAcesOpBtRetorno.setVlrString("S");
		cbAcesOpBtVeritens.setVlrString("S");
		cbAprovOrdCP.setVlrString("N");

	}

	public void beforePost(PostEvent pevt) {

		if (!txpSenha.getVlrString().equals(txpConfirma.getVlrString())) {

			pevt.cancela();
			Funcoes.mensagemInforma(this, "Senha diferente da confirmação!");
			txpSenha.requestFocus();

			return;
		} else if ("".equals(txpSenha.getVlrString().trim())) {

			pevt.cancela();
			Funcoes.mensagemInforma(this, "Senha em branco!");
			txpSenha.requestFocus();

			return;
		} else if (txpSenha.getVlrString().length() > 8) {

			pevt.cancela();
			Funcoes.mensagemInforma(this, "A senha não pode ultrapassar 8 caracteres!");
			txpSenha.requestFocus();

			return;
		} else if (!"ADM".equalsIgnoreCase(txtIDGrpUsu.getVlrString())
				&& (!"SPED".equalsIgnoreCase(txtIDGrpUsu.getVlrString()))) {

			pevt.cancela();
			Funcoes.mensagemInforma(this, "Só os grupos \"ADM\" e \"SPED\" estão disponíveis!");
			txtIDGrpUsu.requestFocus();

			return;
		} else {

			// PreparedStatement ps = null;
			// ResultSet rs = null;

			try {

				if ((lcCampos.getStatus() == ListaCampos.LCS_INSERT) && (txtIDUsu.getText() != null)) {
					txtIDUsu.setText(txtIDUsu.getText().toLowerCase());
				}

				if ((lcCampos.getStatus() == ListaCampos.LCS_INSERT)
						|| (lcCampos.getStatus() == ListaCampos.LCS_EDIT)) {
					if (Aplicativo.FIREBIRD_25.equals(Aplicativo.strFbVersao)) {
						adicionaUser25();
					} else {
						changePassword15();
					}
				}
			} catch (Exception err) {
				err.printStackTrace();
				Funcoes.mensagemInforma(this, "Não foi possível criar usuário no banco de dados.\n" + err);
				pevt.cancela();
			} finally {
				// ps = null;
				// rs = null;
			}
		}
	}

	public void changePassword15() {

		boolean bCheck = false;
		try {

			PreparedStatement ps = conIB.prepareStatement("SELECT SRET FROM CHECKUSER(?)");
			ps.setString(1, txtIDUsu.getVlrString());

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				if ("S".equals(rs.getString(1).trim())) {

					if (lcCampos.getStatus() == ListaCampos.LCS_INSERT) {

						Funcoes.mensagemInforma(this,
								"Atenção!!\n" + "O usuário não será inserido no banco de dados ISC4, \n"
										+ "pois o mesmo já foi cadastrado.");
					}

					bCheck = true;
				}
			}

			rs.close();

			if (bCheck) {

				if (!"88888888".equals(txpSenha.getVlrString())
						&& !"SYSDBA".equals(txtIDUsu.getVlrString().toUpperCase())) {
					ps.close();
					ps = conIB.prepareStatement("EXECUTE PROCEDURE CHANGEPASSWORD(?,?)");
				} else {

					return;
				}
			} else {
				ps.close();
				ps = conIB.prepareStatement("EXECUTE PROCEDURE ADDUSER(?,?)");
			}

			ps.setString(1, txtIDUsu.getVlrString());
			ps.setString(2, txpSenha.getVlrString());

			ps.execute();

			ps.close();

			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
			Funcoes.mensagemInforma(this, "A senha não foi alterada!");
		}
	}

	public void adicionaUser25() {

		PreparedStatement ps = null;
		// ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		StringBuilder sqlalter = new StringBuilder();

		if (!"88888888".equals(txpSenha.getVlrString()) && !"SYSDBA".equals(txtIDUsu.getVlrString().toUpperCase())) {
			sql.append("create user ");
			sql.append(txtIDUsu.getVlrString());
			sql.append(" password '");
			sql.append(txpSenha.getVlrString());
			sql.append("' ");

			try {
				ps = con.prepareStatement(sql.toString());
				ps.execute();
				ps.close();
				con.commit();
			} catch (SQLException errsql) {
				errsql.printStackTrace();
			}

			System.out.println(sql.toString());

			if (lcCampos.getStatus() == ListaCampos.LCS_EDIT) {
				// Utiliza mesma query utilizada acima, trocando apenas o
				// keyword create por alter.
				sqlalter.append(sql.toString().replace("create", "alter"));
				try {
					ps = con.prepareStatement(sqlalter.toString());
					System.out.println(sqlalter.toString());
					ps.execute();
					ps.close();
					con.commit();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void afterPost(PostEvent pevt) {

		if (pevt.ok) {

			gravaAcesso();
		}
	}

	public void valorAlterado(CheckBoxEvent evt) {

		boolean checked = evt.getCheckBox().isSelected();
		txtCodAlmox.setRequerido(checked);
	}

	public void setConexao(DbConnection cn, DbConnection cnIB) {

		super.setConexao(cn);

		conIB = cnIB;
		lcGrup.setConexao(cn);
		lcCC.setConexao(cn);
		lcCC.setWhereAdic("NIVELCC=10 AND ANOCC=" + buscaAnoBaseCC());
		lcAlmox.setConexao(cn);
		lcConfEmail.setConexao(cn);

		if (conIB == null) {
			Funcoes.mensagemInforma(this,
					"A conexão com o banco de dados de usuário está nula,\n"
							+ "provavelmente o usuário qual você conectou não possui\n"
							+ "permissão para acessar este banco de dados.\n"
							+ "Por favor, contate o administrador do sistema.");
			dispose();
		}
	}

	public void stateChanged(ChangeEvent e) {

		Color cor = tcc.getColor();
		txtCorAgenda.setVlrInteger(cor.getRGB());
	}

}
