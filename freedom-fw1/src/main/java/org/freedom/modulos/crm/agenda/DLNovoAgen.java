/**
 * @version 06/01/2004 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.tmk <BR>
 *         Classe:
 * @(#)DLNovoAgend.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                      de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                      Novo agendamento
 * 
 */

package org.freedom.modulos.crm.agenda;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.JComboBoxEvent;
import org.freedom.acao.JComboBoxListener;
import org.freedom.acao.RadioGroupEvent;
import org.freedom.acao.RadioGroupListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;

public class DLNovoAgen extends FFDialogo implements CarregaListener, RadioGroupListener, JComboBoxListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad(JTextFieldPad.TP_DATE, 10, 0);

	private JTextFieldPad txtDatafim = new JTextFieldPad(JTextFieldPad.TP_DATE, 10, 0);

	private JTextFieldPad txtAssunto = new JTextFieldPad(JTextFieldPad.TP_STRING, 150, 0);

	private JTextFieldPad txtCodAge = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldPad txtCodAgeEmit = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);
	
	private JTextFieldPad txtTipoAgeEmit = new JTextFieldPad(JTextFieldPad.TP_STRING, 5, 0);

	private JTextFieldPad txtTipoAge = new JTextFieldPad(JTextFieldPad.TP_STRING, 5, 0);

	private JTextFieldFK txtDescAge = new JTextFieldFK(JTextFieldPad.TP_STRING, 150, 0);
	
	private JTextFieldFK txtDescAgeEmit = new JTextFieldFK(JTextFieldPad.TP_STRING, 150, 0);

	private JTextFieldPad txtIdUsu = new JTextFieldPad(JTextFieldPad.TP_STRING, 8, 0);

	private JTextFieldFK txtNomeUsu = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldPad txtIdUsuEmit = new JTextFieldPad(JTextFieldPad.TP_STRING, 8, 0);

	private JTextFieldFK txtNomeUsuEmit = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JSpinner txtHoraini = new JSpinner();

	private JSpinner txtHorafim = new JSpinner();

	private JComboBoxPad cbPrioridade = null;

	private JCheckBoxPad cbDiaTodo = new JCheckBoxPad("dia todo", "S", "N");

	private JCheckBoxPad cbEnviarEmail = new JCheckBoxPad("enviar email de aviso", "S", "N");

	private JComboBoxPad cbPeriodicidade = null;

	private JComboBoxPad cbTipo = null;

	private JRadioGroup<?, ?> rgCAAGD = null;

	private JRadioGroup<?, ?> rgSitAgd = null;

	private JTextAreaPad txaDescAtend = new JTextAreaPad();

	private JTextAreaPad txaResolucao = new JTextAreaPad();

	private JScrollPane spnDesc = new JScrollPane(txaDescAtend);

	private JScrollPane spnResolucao = new JScrollPane(txaResolucao);

	private JLabelPad lbImg = new JLabelPad(Icone.novo("bannerTMKagendamento.jpg"), SwingConstants.LEFT);

	private JButtonPad btTipoAGD = new JButtonPad(Icone.novo("btAdic2.gif"));

	private ListaCampos lcAgente = new ListaCampos(this);
	
	private ListaCampos lcAgenteEmit = new ListaCampos(this);

	private ListaCampos lcUsuEmit = new ListaCampos(this);

	private ListaCampos lcUsu = new ListaCampos(this);

	private Vector<String> vCodTipoAGD = new Vector<String>();

	private Vector<String> vDescTipoAGD = new Vector<String>();

	private JLabelPad lbDesc = new JLabelPad("   Resolução/motivo :");

	private JLabelPad lbNumRepet = new JLabelPad("Repetir a cada:");

	private JSpinner txtNumRepet = new JSpinner();

	private JLabelPad lbDataLimite = new JLabelPad("dias até:");

	private JTextFieldPad txtDataLimite = new JTextFieldPad(JTextFieldPad.TP_DATE, 10, 0);

	private JPanelPad pnRepet = new JPanelPad();
	
	public static enum PARAM_SGSETAAGENDASP { 
		CODAGD, CODEMP, DTAINIAGD, HRINIAGD, DTAFIMAGD, HRFIMAGD, ASSUNTOAGD, DESCAGD, CODFILIALTA, CODTIPOAGD, 
		PRIORAGD, CODAGE, TIPOAGE, CODFILIALAE, CODAGEEMIT, TIPOAGEEMIT, CAAGD, SITAGD, RESOLUCAOMOTIVO, CODAGDAR, DIATODO	
	}

	public DLNovoAgen(Component cOrig) {
		this("", "", null, null, cOrig, true);
	}

	public DLNovoAgen(String sIdUsuEmit, String sIdUsu, Date dataini, Date datafim, Component cOrig, boolean bNovo) {

		super(cOrig);
		setTitulo("Novo agendamento");
		setAtribos(30, 30,600,665);
		setLocationRelativeTo(Aplicativo.telaPrincipal);
		

		// Acertando o spinner
		GregorianCalendar agora = new GregorianCalendar();
		GregorianCalendar calini = new GregorianCalendar();
		GregorianCalendar calfim = new GregorianCalendar();
		GregorianCalendar cal1 = new GregorianCalendar();
		GregorianCalendar cal2 = new GregorianCalendar();

		if (dataini != null) {
			agora.setTime(dataini);
			agora.set(Calendar.HOUR_OF_DAY, calini.get(Calendar.HOUR_OF_DAY));
			agora.set(Calendar.MINUTE, calini.get(Calendar.MINUTE));

			calini.setTime(dataini);
			calfim.setTime(datafim);

			cal1.setTime(agora.getTime());
			cal2.setTime(agora.getTime());
		}

		calini.add(Calendar.DATE, 0);
		cal1.add(Calendar.YEAR, -100);
		cal2.add(Calendar.YEAR, 100);

		txtDataini.setVlrDate(calini.getTime());
		txtHoraini.setModel(new SpinnerDateModel(calini.getTime(), cal1.getTime(), cal2.getTime(), Calendar.HOUR_OF_DAY));
		txtHoraini.setEditor(new JSpinner.DateEditor(txtHoraini, "kk:mm"));
		txtDatafim.setVlrDate(calfim.getTime());
		txtHorafim.setModel(new SpinnerDateModel(calfim.getTime(), cal1.getTime(), cal2.getTime(), Calendar.HOUR_OF_DAY));
		txtHorafim.setEditor(new JSpinner.DateEditor(txtHorafim, "kk:mm"));

		// Construindo o combobox de tipo.

		cbTipo = new JComboBoxPad(vDescTipoAGD, vCodTipoAGD, JComboBoxPad.TP_STRING, 2, 0);

		Vector<String> vVals1 = new Vector<String>();
		vVals1.addElement("PU");
		vVals1.addElement("PR");
		Vector<String> vLabs1 = new Vector<String>();
		vLabs1.addElement("publico");
		vLabs1.addElement("privado");
		JRadioGroup<String, String> radioGroup2 = new JRadioGroup<String, String>(1, 2, vLabs1, vVals1);
		JRadioGroup<?, ?> radioGroup = radioGroup2;
		rgCAAGD = radioGroup;
		rgCAAGD.setVlrString("PU");

		Vector<String> vVals2 = new Vector<String>();
		vVals2.addElement("1");
		vVals2.addElement("2");
		vVals2.addElement("3");
		vVals2.addElement("4");
		Vector<String> vLabs2 = new Vector<String>();
		vLabs2.addElement("nenhuma");
		vLabs2.addElement("baixa");
		vLabs2.addElement("média");
		vLabs2.addElement("alta");
		cbPrioridade = new JComboBoxPad(vLabs2, vVals2, JComboBoxPad.TP_STRING, 2, 0);

		Vector<String> vValsSitAgd = new Vector<String>();
		vValsSitAgd.addElement("PE");
		vValsSitAgd.addElement("CA");
		vValsSitAgd.addElement("FN");
		Vector<String> vLabsSitAgd = new Vector<String>();
		vLabsSitAgd.addElement("Pendente");
		vLabsSitAgd.addElement("Cancelado");
		vLabsSitAgd.addElement("Concluído");

		Vector<String> vValsP = new Vector<String>();
		vValsP.addElement("NR");
		vValsP.addElement("TD");
		vValsP.addElement("TS");
		vValsP.addElement("T1");
		vValsP.addElement("T2");
		vValsP.addElement("SE");
		vValsP.addElement("ME");
		vValsP.addElement("AN");
		Vector<String> vLabsP = new Vector<String>();
		vLabsP.addElement("Não se repete");
		vLabsP.addElement("Todos os dias");
		vLabsP.addElement("Todos os dias da semana (seg. a sex.)");
		vLabsP.addElement("Todas as segundas quartas e sextas");
		vLabsP.addElement("Todas as terças e quintas");
		vLabsP.addElement("Semanal");
		vLabsP.addElement("Mensal");
		vLabsP.addElement("Anual");

		cbPeriodicidade = new JComboBoxPad(vLabsP, vValsP, JComboBoxPad.TP_STRING, 2, 0);

		rgSitAgd = new JRadioGroup<String, String>(1, 3, vLabsSitAgd, vValsSitAgd);

		lcUsuEmit.add(new GuardaCampo(txtIdUsuEmit, "IdUsu", "ID Usuario", ListaCampos.DB_PK, false));
		lcUsuEmit.add(new GuardaCampo(txtNomeUsuEmit, "NomeUsu", "Nome", ListaCampos.DB_SI, false));
		lcUsuEmit.add(new GuardaCampo(txtCodAgeEmit, "CodAge", "Cód.age.", ListaCampos.DB_FK, true));
		lcUsuEmit.montaSql(false, "USUARIO", "SG");
		lcUsuEmit.setReadOnly(true);
		txtIdUsuEmit.setTabelaExterna(lcUsuEmit, null);
		txtIdUsuEmit.setFK(true);
		txtIdUsuEmit.setNomeCampo("IdUsu");

		lcUsu.add(new GuardaCampo(txtIdUsu, "IdUsu", "ID Usuario", ListaCampos.DB_PK, false));
		lcUsu.add(new GuardaCampo(txtNomeUsu, "NomeUsu", "Nome", ListaCampos.DB_SI, false));
		lcUsu.add(new GuardaCampo(txtCodAge, "CodAge", "Cód.age.", ListaCampos.DB_FK, true));
		lcUsu.montaSql(false, "USUARIO", "SG");
		lcUsu.setReadOnly(true);
		txtIdUsu.setTabelaExterna(lcUsu, null);
		txtIdUsu.setFK(true);
		txtIdUsu.setNomeCampo("IdUsu");
		
		lcAgenteEmit.add(new GuardaCampo(txtCodAgeEmit, "CodAge", "Cód.age.", ListaCampos.DB_PK, true));
		lcAgenteEmit.add(new GuardaCampo(txtDescAgeEmit, "DescAge", "Descrição do agente", ListaCampos.DB_SI, false));
		lcAgenteEmit.add(new GuardaCampo(txtTipoAgeEmit, "TipoAge", "Tipo", ListaCampos.DB_SI, false));
		lcAgenteEmit.montaSql(false, "AGENTE", "SG");
		lcAgenteEmit.setReadOnly(true);
		txtCodAgeEmit.setTabelaExterna(lcAgenteEmit, null);
		txtCodAgeEmit .setFK(true);
		txtCodAgeEmit.setNomeCampo( "CodAge");

		lcAgente.add(new GuardaCampo(txtCodAge, "CodAge", "Cód.age.", ListaCampos.DB_PK, true));
		lcAgente.add(new GuardaCampo(txtDescAge, "DescAge", "Descrição do agente", ListaCampos.DB_SI, false));
		lcAgente.add(new GuardaCampo(txtTipoAge, "TipoAge", "Tipo", ListaCampos.DB_SI, false));
		lcAgente.montaSql(false, "AGENTE", "SG");
		lcAgente.setReadOnly(true);
		txtCodAge.setTabelaExterna(lcAgente, null);
		txtCodAge.setFK(true);
		txtCodAge.setNomeCampo("CodAge");

		c.add(lbImg, BorderLayout.NORTH);

		adic(new JLabelPad("Usuário"), 10, 5, 97, 20);
		adic(txtIdUsuEmit, 10, 25, 97, 20);
		adic(new JLabelPad("ID usu"), 110, 5, 100, 20);
		adic(txtIdUsu, 110, 25, 100, 20);
		adic(new JLabelPad("Nome do usuario"), 213, 5, 358, 20);
		adic(txtNomeUsu, 213, 25, 360, 20);

		adic(cbDiaTodo, 10, 55, 90, 20);
		adic(cbEnviarEmail, 100, 55, 150, 20);
		adic(rgCAAGD, 265, 50, 309, 30);

		adic(new JLabelPad("Inicio"), 10, 80, 70, 20);
		adic(txtDataini, 10, 100, 70, 20);
		adic(txtHoraini, 81, 100, 50, 20);
		adic(new JLabelPad("Fim"), 137, 80, 70, 20);
		adic(txtDatafim, 137, 100, 70, 20);
		adic(txtHorafim, 208, 100, 50, 20);

		adic(new JLabelPad("Prioridade"), 265, 80, 80, 20);
		adic(cbPrioridade, 265, 100, 80, 20);
		adic(new JLabelPad("Tipo"), 350, 80, 100, 20);
		adic(cbTipo, 350, 100, 197, 20);
		adic(btTipoAGD, 552, 100, 20, 20);

		adic(new JLabelPad("Repetição"), 10, 125, 80, 20);
		adic(cbPeriodicidade, 10, 145, 250, 20);

		pnRepet.setVisible(false);
		pnRepet.setBorder(BorderFactory.createEmptyBorder());
		pnRepet.adic(lbNumRepet, 3, 5, 95, 20);
		pnRepet.adic(txtNumRepet, 95, 5, 45, 20);
		pnRepet.adic(lbDataLimite, 150, 5, 60, 20);
		pnRepet.adic(txtDataLimite, 210, 5, 96, 20);

		adic(pnRepet, 269, 140, 305, 30);
	
		adic(new JLabelPad("Assunto"), 10, 170, 100, 21);
		adic(txtAssunto, 10, 190, 565, 21);
		txtAssunto.setRequerido(true);
		JLabelPad lbChamada = new JLabelPad("Ação:", SwingConstants.CENTER);
		lbChamada.setOpaque(true);
		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder(BorderFactory.createEtchedBorder());

		lbDesc.setOpaque(true);
		JLabelPad lbLinhaResolucao = new JLabelPad();
		lbLinhaResolucao.setBorder(BorderFactory.createEtchedBorder());

		adic(lbChamada, 20, 215, 60, 20);
		adic(lbLinha, 10, 225, 565, 2);
		adic(spnDesc, 10, 240, 565, 110);
		adic(rgSitAgd, 10, 353, 565, 27);
		adic(lbDesc, 20, 380, 130, 20);
		adic(lbLinhaResolucao, 10, 390, 565, 2);
		adic(spnResolucao, 10, 403, 565, 100);

		if (!"".equals(sIdUsuEmit)) {
			txtIdUsuEmit.setVlrString(sIdUsuEmit);
			txtIdUsuEmit.setAtivo(false);
		}

		if (!"".equals(sIdUsu)) {
			txtIdUsu.setVlrString(sIdUsu);
		}

		btTipoAGD.addActionListener(this);
		cbDiaTodo.addActionListener(this);
		rgSitAgd.addRadioGroupListener(this);
		cbPeriodicidade.addComboBoxListener(this);

		lcUsu.addCarregaListener(this);

		cbPeriodicidade.setEnabled(bNovo);
		cbEnviarEmail.setVlrString("N");

	}
	
	public Object[] getParamSP() {

		final Object[] sVal = new Object[ PARAM_SGSETAAGENDASP.values().length ];
		
		sVal[ PARAM_SGSETAAGENDASP.CODAGD.ordinal() ] = new Integer(0);
		sVal[ PARAM_SGSETAAGENDASP.CODEMP.ordinal() ] = Aplicativo.iCodEmp;
		sVal[ PARAM_SGSETAAGENDASP.DTAINIAGD.ordinal() ] = txtDataini.getVlrString();
		sVal[ PARAM_SGSETAAGENDASP.HRINIAGD.ordinal() ] = ( ( JSpinner.DateEditor ) txtHoraini.getEditor() ).getTextField().getText();
		sVal[ PARAM_SGSETAAGENDASP.DTAFIMAGD.ordinal() ] = txtDatafim.getVlrString();
		sVal[ PARAM_SGSETAAGENDASP.HRFIMAGD.ordinal() ] = ( ( JSpinner.DateEditor ) txtHorafim.getEditor() ).getTextField().getText();
		sVal[ PARAM_SGSETAAGENDASP.ASSUNTOAGD.ordinal() ] = txtAssunto.getVlrString();
		sVal[ PARAM_SGSETAAGENDASP.DESCAGD.ordinal() ] = txaDescAtend.getVlrString();
		sVal[ PARAM_SGSETAAGENDASP.CODFILIALTA.ordinal() ] = ListaCampos.getMasterFilial("SGTIPOAGENDA");
		sVal[ PARAM_SGSETAAGENDASP.CODTIPOAGD.ordinal() ] = vCodTipoAGD.elementAt(cbTipo.getSelectedIndex()) ;
		sVal[ PARAM_SGSETAAGENDASP.PRIORAGD.ordinal() ] = cbPrioridade.getVlrString();
		sVal[ PARAM_SGSETAAGENDASP.CODAGE.ordinal() ] = txtCodAge.getVlrInteger();
		sVal[ PARAM_SGSETAAGENDASP.TIPOAGE.ordinal() ] = txtTipoAge.getVlrString();
		sVal[ PARAM_SGSETAAGENDASP.CODFILIALAE.ordinal()] =  ListaCampos.getMasterFilial("SGUSUARIO");
		sVal[ PARAM_SGSETAAGENDASP.CODAGEEMIT.ordinal() ] = txtCodAgeEmit.getVlrInteger();
		sVal[ PARAM_SGSETAAGENDASP.TIPOAGEEMIT.ordinal() ] = txtTipoAgeEmit.getVlrString();
		sVal[ PARAM_SGSETAAGENDASP.CAAGD.ordinal() ] = rgCAAGD.getVlrString();
		sVal[ PARAM_SGSETAAGENDASP.SITAGD.ordinal() ] = rgSitAgd.getVlrString();
		sVal[ PARAM_SGSETAAGENDASP.RESOLUCAOMOTIVO.ordinal() ] = txaResolucao.getVlrString();
		sVal[ PARAM_SGSETAAGENDASP.CODAGDAR.ordinal() ] = null;
		sVal[ PARAM_SGSETAAGENDASP.DIATODO.ordinal() ] = cbDiaTodo.getVlrString();

		return sVal;
	}
	

	private void carregaTipoAgenda() {

		try {

			vCodTipoAGD.clear();
			vDescTipoAGD.clear();

			String sSQL = "SELECT CODTIPOAGD, DESCTIPOAGD FROM SGTIPOAGENDA WHERE CODEMP=? AND CODFILIAL=?";

			PreparedStatement ps = con.prepareStatement(sSQL);
			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, Aplicativo.iCodFilial);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				vCodTipoAGD.addElement(rs.getString("CODTIPOAGD"));
				vDescTipoAGD.addElement(rs.getString("DESCTIPOAGD"));
			}

			rs.close();
			ps.close();

			con.commit();

			if (vDescTipoAGD.size() <= 0) {
				Funcoes.mensagemInforma(this, "Nenhum tipo de agendamento foi encontrado!");
			}

			cbTipo.setItensGeneric(vDescTipoAGD, vCodTipoAGD);

		}
		catch (Exception e) {
			e.printStackTrace();
			Funcoes.mensagemErro(this, "Erro ao buscar tipos de agendamento!\n" + e.getMessage());
		}
	}
	
    public String[] getValores() {

        final String[] sVal = new String[16];

        sVal[0] = txtDataini.getVlrString();
        sVal[1] = ( ( JSpinner.DateEditor ) txtHoraini.getEditor() ).getTextField().getText();
        sVal[2] = txtDatafim.getVlrString();
        sVal[3] = ( ( JSpinner.DateEditor ) txtHorafim.getEditor() ).getTextField().getText();
        sVal[4] = txtAssunto.getVlrString();
        sVal[5] = txaDescAtend.getVlrString();
        sVal[6] = String.valueOf(Aplicativo.iCodFilial);
        sVal[7] = vCodTipoAGD.elementAt(cbTipo.getSelectedIndex());
        sVal[8] = cbPrioridade.getVlrString();
        sVal[9] = txtCodAge.getVlrString();
        sVal[10] = txtTipoAge.getVlrString();
        sVal[11] = rgCAAGD.getVlrString();
        sVal[12] = rgSitAgd.getVlrString();
        sVal[13] = txaResolucao.getVlrString();
        sVal[14] = cbDiaTodo.getVlrString();
        sVal[15] = cbEnviarEmail.getVlrString();

        return sVal;
}

	public void setValores(String[] sVal) {

		txtCodAge.setVlrString(sVal[0]);
		txtDataini.setVlrString(sVal[1]);
		( ( JSpinner.DateEditor ) txtHoraini.getEditor() ).getTextField().setText(sVal[2]);
		txtDatafim.setVlrString(sVal[3]);
		( ( JSpinner.DateEditor ) txtHorafim.getEditor() ).getTextField().setText(sVal[4]);
		txtAssunto.setVlrString(sVal[5]);
		txaDescAtend.setVlrString(sVal[6]);
		rgCAAGD.setVlrString(sVal[7]);
		cbPrioridade.setVlrString(sVal[8]);
		cbTipo.setVlrString(sVal[9]);
		rgSitAgd.setVlrString(sVal[10]);
		txaResolucao.setVlrString(sVal[11]);
		cbDiaTodo.setVlrString(sVal[12]);

		lcUsu.carregaDados();
		lcUsuEmit.carregaDados();
		lcAgente.carregaDados();
	}

	public void actionPerformed(ActionEvent evt) {

		if (evt.getSource() == btOK) {
			if ("".equals(txtAssunto.getVlrString())) {
				Funcoes.mensagemInforma(this, "Campo assunto é obrigatório!!!");
				return;
			}
			if ("".equals(txtCodAge.getVlrString())) {
				Funcoes.mensagemInforma(this, "Código do usuario inválido!");
				return;
			}
		}
		else if (evt.getSource() == btTipoAGD) {
			FTipoAgenda tipoAgd = new FTipoAgenda();
			
			if (Funcoes.verificaAcessoClasse(tipoAgd.getClass().getCanonicalName())) {
				tipoAgd.setConexao(con);
				tipoAgd.setVisible(true);
				tipoAgd.toFront();
				carregaTipoAgenda();
			} else {
				Funcoes.mensagemInforma(null, "O usuário " + Aplicativo.getUsuario().getIdusu() + " não possui acesso a tela solicitada (" + tipoAgd.getClass().getCanonicalName()
						+ ").\nSolicite a liberação do acesso ao administrador do sistema.");
			}
		}
		else if (evt.getSource() == cbDiaTodo) {
			if ("S".equals(cbDiaTodo.getVlrString())) {
				txtHoraini.setEnabled(false);
				txtHorafim.setEnabled(false);

				( ( JSpinner.DateEditor ) txtHoraini.getEditor() ).getTextField().setText("00:00");
				( ( JSpinner.DateEditor ) txtHorafim.getEditor() ).getTextField().setText("24:00");
			}
			else {
				txtHoraini.setEnabled(true);
				txtHorafim.setEnabled(true);
			}
		}
		super.actionPerformed(evt);

	}

	public void beforeCarrega(CarregaEvent e) {

		if (e.getListaCampos() == lcUsu) {
			lcAgente.carregaDados();
		}
	}

	public void afterCarrega(CarregaEvent e) {

		if (e.getListaCampos() == lcUsu) {
			lcAgente.carregaDados();
		}
	}

	public void setConexao(DbConnection cn) {

		super.setConexao(cn);
		lcUsu.setConexao(cn);
		lcUsuEmit.setConexao(cn);
		lcAgente.setConexao(cn);
		lcAgenteEmit.setConexao(cn);
		lcUsu.carregaDados();
		lcUsuEmit.carregaDados();
		lcAgente.carregaDados();
		lcAgenteEmit.carregaDados();

		carregaTipoAgenda();
	}

	public void valorAlterado(RadioGroupEvent evt) {

		if (evt.getSource() == rgSitAgd) {
			if (rgSitAgd.getVlrString().equals("PE")) {
				txaResolucao.setEnabled(false);
			}
			else {
				txaResolucao.setEnabled(true);
			}
		}
	}

	public void valorAlterado(JComboBoxEvent evt) {

		if (evt.getComboBoxPad() == cbPeriodicidade) {

			if ("NR".equals(cbPeriodicidade.getVlrString())) {
				pnRepet.setVisible(false);
			}
			else {

				if (Funcoes.getNumDias(txtDataini.getVlrDate(), txtDatafim.getVlrDate()) != 0) {
					Funcoes.mensagemInforma(this, "Eventos repetidos devem ser encerrados no mesmo dia!");
					cbPeriodicidade.setVlrString("NR");
					txtDatafim.requestFocus();
					return;
				}

				Calendar limite = new GregorianCalendar();
				limite.setTime(new Date());
				limite.set(Calendar.YEAR, limite.get(Calendar.YEAR) + 1);

				txtDataLimite.setVlrDate(limite.getTime());
				String tipo = cbPeriodicidade.getVlrString();

				if ("TD".equals(tipo)) {
					txtNumRepet.setValue(1);
					txtNumRepet.setEnabled(true);
					lbDataLimite.setText("dias até:");
				}
				else if ("TS".equals(tipo) || "T1".equals(tipo) || "T2".equals(tipo)) {
					txtNumRepet.setValue(0);
					txtNumRepet.setEnabled(false);
					lbDataLimite.setText("- - - - ");
				}
				else if ("SE".equals(tipo)) {
					txtNumRepet.setValue(1);
					txtNumRepet.setEnabled(true);
					lbDataLimite.setText("sem. até:");
				}
				else if ("ME".equals(tipo)) {
					txtNumRepet.setValue(1);
					txtNumRepet.setEnabled(true);
					lbDataLimite.setText("mes até:");
				}
				else if ("AN".equals(tipo)) {
					txtNumRepet.setValue(1);
					txtNumRepet.setEnabled(true);
					lbDataLimite.setText("anos até:");
					limite.set(Calendar.YEAR, limite.get(Calendar.YEAR) + 9);
					txtDataLimite.setVlrDate(limite.getTime());

				}

				pnRepet.setVisible(true);
			}
		}
	}

	public HashMap<?, ?> getPeriodicidade() {

		HashMap<Object, Object> ret = new HashMap<Object, Object>();
		try {
			ret.put("TIPO", cbPeriodicidade.getVlrString());
			ret.put("INTERVALO", txtNumRepet.getValue());
			ret.put("LIMITE", txtDataLimite.getVlrDate());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

}
