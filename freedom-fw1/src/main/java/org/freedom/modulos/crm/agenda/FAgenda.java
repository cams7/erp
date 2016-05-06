/**
 * @version 05/06/2003 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 * Projeto: Freedom <BR>
 * 
 * Pacote: org.freedom.modulos.atd <BR>
 * Classe:
 * @(#)FAgenda.java <BR>
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
 * Tela para manutenção da agenda de usuários
 * 
 */

package org.freedom.modulos.crm.agenda;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import lu.tudor.santec.bizcal.util.ObservableEventList;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.nachocalendar.CalendarFactory;
import net.sf.nachocalendar.components.DatePanel;
import net.sf.nachocalendar.components.MonthScroller;
import net.sf.nachocalendar.event.DateSelectionEvent;
import net.sf.nachocalendar.event.DateSelectionListener;
import net.sf.nachocalendar.event.MonthChangeEvent;
import net.sf.nachocalendar.model.DateSelectionModel;
import net.sf.nachocalendar.tasks.TaskDataModel;

import org.freedom.acao.RadioGroupEvent;
import org.freedom.acao.RadioGroupListener;
import org.freedom.acao.TabelaEditEvent;
import org.freedom.acao.TabelaEditListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.object.EmailBean;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.DLEnviarEmail;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.modulos.crm.agenda.visoes.DayViewPanel;
import org.freedom.modulos.crm.agenda.visoes.MonthViewPanel;
import org.freedom.modulos.crm.dao.DAOAgenda;
import org.freedom.modulos.crm.object.Agenda;

import bizcal.common.DayViewConfig;
import bizcal.common.Event;
import bizcal.swing.CalendarListener;
import bizcal.swing.util.FrameArea;
import bizcal.util.DateInterval;

public class FAgenda extends FFilho implements ActionListener, RadioGroupListener, DateSelectionListener, TabelaEditListener, MouseListener, CalendarListener, ChangeListener {

	private static final long serialVersionUID = 1L;

	private JTablePad tabAgd = new JTablePad();

	private JTablePad tabUsu = new JTablePad();

	private JTabbedPanePad tpnAgd = new JTabbedPanePad();

	private JTabbedPanePad tpnVisoes = new JTabbedPanePad();

	private JScrollPane spnAgd = new JScrollPane(tpnVisoes);

	private JScrollPane spnUsu = new JScrollPane(tabUsu);

	private JPanelPad pinCabAgd = new JPanelPad(0, 40);

	private JPanelPad pnAgd = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());

	private JPanelPad pnCalendar = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());

	private JPanelPad pnUsuarios = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());

	private JPanelPad pnRodAgd = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());

	private JTextFieldPad txtIdUsu = new JTextFieldPad(JTextFieldPad.TP_STRING, 8, 0);

	private JTextFieldFK txtNomeUsu = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JCheckBoxPad cbTodos = new JCheckBoxPad("Agendamentos de todos os usuários", "S", "N");

	private JCheckBoxPad cbPendentes = new JCheckBoxPad("", "S", "N");

	private JCheckBoxPad cbConcluidos = new JCheckBoxPad("", "S", "N");

	private JCheckBoxPad cbCancelados = new JCheckBoxPad("", "S", "N");

	private JCheckBoxPad cbBaixa = new JCheckBoxPad("", "S", "N");

	private JCheckBoxPad cbMedia = new JCheckBoxPad("", "S", "N");

	private JCheckBoxPad cbAlta = new JCheckBoxPad("", "S", "N");

	private JRadioGroup<?, ?> rgPeriodo = null;

	private JButtonPad btPrevImp = new JButtonPad(Icone.novo("btPrevimp.png"));

	private JButtonPad btImp = new JButtonPad(Icone.novo("btImprime.png"));

	private JButtonPad btNovo = new JButtonPad(Icone.novo("btNovo.png"));

	private JButtonPad btExcluir = new JButtonPad(Icone.novo("btExcluir.png"));

	private JButtonPad btSair = new JButtonPad("Sair", Icone.novo("btSair.png"));

	private DatePanel calendarpanel = CalendarFactory.createDatePanel();

	private ListaCampos lcUsu = new ListaCampos(this);

	// private Vector<String> vUsu = new Vector<String>();

	private int iCodAge = 0;

	private int iCodFilialAge = 0;

	private String sTipoAge = "";

	private MonthViewPanel monthViewPanel;

	private DayViewPanel weekViewPanel;

	private DayViewPanel dayViewPanel;

	private ObservableEventList eventDataList = new ObservableEventList();

	// private Vector<DateListener> dateListeners = new Vector<DateListener>();

	private boolean listener_rgperiodo = true;

	private boolean listener_calendar = false;

	private EventModel monthModel = new EventModel(eventDataList, EventModel.TYPE_MONTH);

	private EventModel weekModel = new EventModel(eventDataList, EventModel.TYPE_WEEK);

	private EventModel dayModel = new EventModel(eventDataList, EventModel.TYPE_DAY);

	private TaskDataModel tdm = new TaskDataModel();

	private DateSelectionModel dsm = null;

	private DayViewConfig dvc = new DayViewConfig();

	private JPanel pnMS = null;

	private MonthScroller ms = null;

	private static ImageIcon nenhuma = Icone.novo("clPriorBaixa.gif");

	private static ImageIcon baixa = Icone.novo("clPriorBaixa.gif");

	private static ImageIcon media = Icone.novo("clPriorMedia.gif");

	private static ImageIcon alta = Icone.novo("clPriorAlta.png");

	private static ImageIcon pend = Icone.novo("clAgdPend.png");

	private static ImageIcon canc = Icone.novo("clAgdCanc.png");

	private static ImageIcon conc = Icone.novo("clAgdFin.png");

	private JPanelPad pinStatus = new JPanelPad(569, 140);

	private JPanelPad pinPrior = new JPanelPad(569, 140);
	
	private DAOAgenda daoagenda = null;

	public FAgenda() {

		super(false);

		dvc.setDefaultDayStartHour(6);
		dvc.setDefaultDayEndHour(22);

		txtIdUsu.setVisible(false);
		txtIdUsu.setVlrString(Aplicativo.getUsuario().getIdusu());

		setTitulo("Agenda");
		setAtribos(10, 10, 880, 540);

		lcUsu.add(new GuardaCampo(txtIdUsu, "IdUsu", "ID Usuario", ListaCampos.DB_PK, false));
		lcUsu.add(new GuardaCampo(txtNomeUsu, "NomeUsu", "Nome", ListaCampos.DB_SI, false));
		lcUsu.montaSql(false, "USUARIO", "SG");
		lcUsu.setReadOnly(true);
		txtIdUsu.setTabelaExterna(lcUsu, null);

		txtIdUsu.setFK(true);
		txtIdUsu.setNomeCampo("IdUsu");

		tpnAgd.add("", pnAgd);
		tpnVisoes.add("   Lista de eventos  ", tabAgd);

		// Component cpn = tabAgd.getParent();

		pnAgd.add(pinCabAgd, BorderLayout.NORTH);

		calendarpanel.setAntiAliased(true);

		calendarpanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		calendarpanel.setBorder(BorderFactory.createEtchedBorder());
		calendarpanel.setModel(tdm);

		dsm = calendarpanel.getDateSelectionModel();
		Calendar cl = new GregorianCalendar();
		cl.setTime(new Date());
		//cl.set(Calendar.DAY_OF_MONTH, 27);
		dsm.setLeadSelectionDate(cl.getTime());

		pnCalendar.setBorder(BorderFactory.createEtchedBorder());
		pnCalendar.add(calendarpanel, BorderLayout.NORTH);
		pnCalendar.add(pnUsuarios, BorderLayout.CENTER);

		pnUsuarios.setBorder(BorderFactory.createEtchedBorder());
		pnUsuarios.setPreferredSize(calendarpanel.getPreferredSize());

		pnUsuarios.add(spnUsu);

		tabUsu.setRowHeight(23);

		tabUsu.adicColuna(""); // checkbox
		tabUsu.adicColuna("Outras agendas"); // Nomes
		tabUsu.adicColuna(""); // codage
		tabUsu.adicColuna(""); // tipoage

		tabUsu.setTamColuna(pnUsuarios.getPreferredSize().width - 30, 1);
		tabUsu.setTamColuna(20, 0);
		tabUsu.setColunaInvisivel(2);
		tabUsu.setColunaInvisivel(3);

		pnAgd.add(pnCalendar, BorderLayout.WEST);

		pnAgd.add(spnAgd, BorderLayout.CENTER);

		getTela().add(tpnAgd);

		Vector<String> vValsPeriodo = new Vector<String>();
		Vector<String> vLabsPeriodo = new Vector<String>();
		vValsPeriodo.addElement("D");
		vValsPeriodo.addElement("S");
		vValsPeriodo.addElement("M");
		vValsPeriodo.addElement("T");
		vLabsPeriodo.addElement("Dia");
		vLabsPeriodo.addElement("Semana");
		vLabsPeriodo.addElement("Mês");
		vLabsPeriodo.addElement("Todos");

		rgPeriodo = new JRadioGroup<String, String>(1, 3, vLabsPeriodo, vValsPeriodo);

		rgPeriodo.setVlrString("S");

		cbPendentes.setVlrString("S");

		cbBaixa.setVlrString("S");
		cbMedia.setVlrString("S");
		cbAlta.setVlrString("S");

		pinCabAgd.adic(cbTodos, 3, 4, 240, 30);
		pinCabAgd.adic(rgPeriodo, 250, 4, 315, 30);

		pinCabAgd.adic(pinStatus, 569, 4, 140, 30);

		pinStatus.adic(new JLabel(pend), 1, 4, 16, 16);
		pinStatus.adic(cbPendentes, 18, 0, 20, 25);

		pinStatus.adic(new JLabel(canc), 50, 4, 16, 16);
		pinStatus.adic(cbCancelados, 66, 0, 20, 25);

		pinStatus.adic(new JLabel(conc), 95, 4, 16, 16);
		pinStatus.adic(cbConcluidos, 111, 0, 20, 25);

		pinCabAgd.adic(pinPrior, 709, 4, 140, 30);

		pinPrior.adic(new JLabel(baixa), 1, 4, 16, 16);
		pinPrior.adic(cbBaixa, 18, 0, 20, 25);

		pinPrior.adic(new JLabel(media), 50, 4, 16, 16);
		pinPrior.adic(cbMedia, 66, 0, 20, 25);

		pinPrior.adic(new JLabel(alta), 95, 4, 16, 16);
		pinPrior.adic(cbAlta, 111, 0, 20, 25);

		tabAgd.setRowHeight(23);
		tabAgd.adicColuna("");
		tabAgd.adicColuna("Sit.");
		tabAgd.adicColuna("Prioridade");
		tabAgd.adicColuna("Assunto");
		tabAgd.adicColuna("Data ini.");
		tabAgd.adicColuna("Hora ini.");
		tabAgd.adicColuna("Data fim.");
		tabAgd.adicColuna("Hora fim.");
		tabAgd.adicColuna("");
		tabAgd.adicColuna("");

		tabAgd.setColunaInvisivel(0);
		tabAgd.setTamColuna(20, 1);
		tabAgd.setTamColuna(20, 2);
		tabAgd.setTamColuna(200, 3);
		tabAgd.setTamColuna(65, 4);
		tabAgd.setTamColuna(50, 5);
		tabAgd.setTamColuna(65, 6);
		tabAgd.setTamColuna(50, 7);
		tabAgd.setColunaInvisivel(8); // Codage
		tabAgd.setColunaInvisivel(9); // Codageemit

		dayViewPanel = new DayViewPanel(dayModel, dvc);
		tpnVisoes.add("   Diário  ", dayViewPanel);

		weekViewPanel = new DayViewPanel(weekModel);
		tpnVisoes.add("   Semanal  ", weekViewPanel);

		monthViewPanel = new MonthViewPanel(monthModel, this);
		tpnVisoes.add("   Mensal  ", monthViewPanel);

		JPanelPad pnBot = new JPanelPad(JPanelPad.TP_JPANEL, new GridLayout(1, 2));
		pnBot.setPreferredSize(new Dimension(90, 30));

		JPanelPad pnBot2 = new JPanelPad(JPanelPad.TP_JPANEL, new GridLayout(1, 2));
		pnBot2.setPreferredSize(new Dimension(300, 30));

		pnBot.add(btNovo);
		pnBot.add(btExcluir);

		pnBot2.add(btPrevImp);
		pnBot2.add(btImp);
		pnBot2.add(btSair);

		pnRodAgd.add(pnBot, BorderLayout.WEST);

		btSair.setPreferredSize(new Dimension(110, 30));

		pnRodAgd.add(pnBot2, BorderLayout.EAST);
		btSair.addActionListener(this);

		pnRodAgd.setBorder(BorderFactory.createEtchedBorder());
		pnAgd.add(pnRodAgd, BorderLayout.SOUTH);

		btNovo.addActionListener(this);
		btExcluir.addActionListener(this);
		btPrevImp.addActionListener(this);
		btImp.addActionListener(this);
		cbTodos.addActionListener(this);
		cbPendentes.addActionListener(this);
		cbCancelados.addActionListener(this);
		cbConcluidos.addActionListener(this);
		cbBaixa.addActionListener(this);
		cbMedia.addActionListener(this);
		cbAlta.addActionListener(this);
		rgPeriodo.addRadioGroupListener(this);
		weekViewPanel.addCalendarListener(this);
		dayViewPanel.addCalendarListener(this);
		dsm.addDateSelectionListener(this);
		tpnVisoes.addMouseListener(this);
		tabAgd.addMouseListener(this);
		tabUsu.addMouseListener(this);
		tabUsu.addTabelaEditListener(this);

		// Painel contendo o componente de seleção de meses.
		pnMS = ( JPanel ) calendarpanel.getComponent(0);
		// Componente de seleção de meses para inclusão do listener
		ms = ( MonthScroller ) pnMS.getComponent(0);
		// Inclusão do listener no monthscroller
		ms.addChangeListener(this);

	}

	private void buscaAgente() {
		Map<String, Object> agentes = daoagenda.buscaAgente();
		iCodAge = (Integer) agentes.get("CodAge");
		sTipoAge = (String) agentes.get("TipoAge");
		iCodFilialAge = (Integer) agentes.get("CodFilialAge");
		
	}
		/*
		try {

			String sSQL = "SELECT U.CODAGE,U.TIPOAGE,U.CODFILIALAE FROM SGUSUARIO U WHERE CODEMP=? AND CODFILIAL=? AND IDUSU=? AND U.ATIVOUSU='S' ";
			PreparedStatement ps = con.prepareStatement(sSQL);
			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, Aplicativo.iCodFilial);
			ps.setString(3, Aplicativo.getUsuario().getIdusu());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				iCodAge = rs.getInt(1);
				sTipoAge = rs.getString(2);
				iCodFilialAge = rs.getInt(3);
			}

			rs.close();
			ps.close();

			con.commit();

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}*/

	private void montaTabUsu() {
		StringBuffer sql = new StringBuffer();
		try {
			tabUsu.limpa();
			sql.append("SELECT U.PNOMEUSU, U.UNOMEUSU, U.CORAGENDA, U.CODAGE, U.TIPOAGE ");
			sql.append("FROM SGUSUARIO U, SGAGENTE A, SGACESSOEU EU  ");
			sql.append("WHERE U.CODEMP=? AND U.CODFILIAL=? ");
			sql.append("AND A.CODEMP=U.CODEMPAE AND A.CODFILIAL=U.CODFILIALAE ");
			sql.append("AND A.TIPOAGE=U.TIPOAGE AND A.CODAGE=U.CODAGE AND U.ATIVOUSU='S' ");
			sql.append("AND EU.CODEMP=U.CODEMP AND EU.CODFILIAL=U.CODFILIAL AND EU.IDUSU=U.IDUSU ");
			sql.append("AND EU.CODEMPFL=? AND EU.CODFILIALFL=? ");
			

			PreparedStatement ps = con.prepareStatement(sql.toString());
			int param = 1;
			ps.setInt(param++, Aplicativo.iCodEmp);
			ps.setInt(param++, Aplicativo.iCodFilialPad);
			ps.setInt(param++, Aplicativo.iCodEmp);
			ps.setInt(param++, Aplicativo.iCodFilialPad);

			ResultSet rs = ps.executeQuery();

			for (int i = 0; rs.next(); i++) {
				tabUsu.adicLinha();

				String pnome = rs.getString("PNOMEUSU");
				String unome = rs.getString("UNOMEUSU");
				int icor = rs.getInt("CORAGENDA");

				if (pnome != null) {
					pnome = pnome.trim().substring(0, 1).toUpperCase() + pnome.trim().substring(1);
				}
				if (unome != null) {
					unome = unome.trim().substring(0, 1).toUpperCase() + unome.trim().substring(1);
				}

				tabUsu.setValor(new Boolean(iCodAge == rs.getInt("CODAGE")), i, 0);
				tabUsu.setValor("  " + pnome + " " + unome, i, 1);
				tabUsu.setValor(new Integer(rs.getInt("CODAGE")), i, 2);
				tabUsu.setValor(rs.getString("TIPOAGE"), i, 3);

				// tabUsu.setColColor( i, 0, new Color(icor), icor < -10000 ?
				// Color.WHITE : Color.BLACK );
				tabUsu.setColColor(i, 1, new Color(icor), icor < -10000 ? Color.WHITE : Color.BLACK);

			}

			rs.close();
			ps.close();

			con.commit();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Vector<Vector<?>> getAgentes() {
		Vector<Vector<?>> ret = new Vector<Vector<?>>();
		try {
			Vector<Integer> codage = new Vector<Integer>();
			Vector<String> tipoage = new Vector<String>();
			for (int i = 0; tabUsu.getNumLinhas() > i; i++) {
				if (( Boolean ) tabUsu.getValor(i, 0)) {
					codage.addElement(( Integer ) tabUsu.getValor(i, 2));
					tipoage.addElement(( String ) tabUsu.getValor(i, 3));
				}
			}
			ret.addElement(codage);
			ret.addElement(tipoage);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	private void carregaTabAgd() {

		eventDataList.clear();

		Vector<Vector<?>> agentes = getAgentes();

		List<Event> eventos = carregaTabAgd(agentes, calendarpanel.getValues(), tabAgd, ( "S".equals(cbTodos.getVlrString()) ), con, this, rgPeriodo.getVlrString(), "S".equals(cbPendentes
				.getVlrString()), "S".equals(cbCancelados.getVlrString()), "S".equals(cbConcluidos.getVlrString()), "S".equals(cbBaixa.getVlrString()), "S".equals(cbMedia.getVlrString()), "S"
				.equals(cbAlta.getVlrString()), iCodAge);

		if (eventos != null) {
			eventDataList.addAll(eventos);
		}

		/*
		 * if ( "S".equals( cbTodos.getVlrString() ) ) { montaPanelUsu(); } else
		 * { tabUsu.limpa(); }
		 */

		marcaCalendario();

	}

	public void marcaCalendario() {
		StringBuffer sSQL = new StringBuffer();

		try {

			tdm.clear();

			sSQL.append("SELECT A.DTAINIAGD ");
			sSQL.append(" FROM SGAGENDA A, SGUSUARIO U ");
			sSQL.append(" WHERE A.CODEMP=? AND A.CODFILIAL=? ");
			sSQL.append(" AND U.CODEMPAE=A.CODEMP AND U.CODFILIALAE=A.CODFILIAL ");
			sSQL.append(" AND U.CODAGE=A.CODAGE AND U.TIPOAGE=A.TIPOAGE ");
			sSQL.append(" AND A.SITAGD='PE' ");

			if ("S".equals(cbTodos.getVlrString())) {
				sSQL.append(" AND (( A.CAAGD='PU') OR ( A.CODAGE=? AND A.TIPOAGE=? )) ");
			}
			else {
				sSQL.append(" AND A.CODAGE=? AND A.TIPOAGE=? ");
			}

			sSQL.append("GROUP BY 1");

			PreparedStatement ps = con.prepareStatement(sSQL.toString());
			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, Aplicativo.iCodFilialPad);
			ps.setInt(3, iCodAge);
			ps.setString(4, sTipoAge);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				tdm.addData(rs.getDate(1), "");
			}

			rs.close();
			ps.close();

			con.commit();

			calendarpanel.refresh();

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static List<Event> carregaTabAgd(final Vector<Vector<?>> agentes, final Object[] datas, final JTablePad tabAgd, final boolean todos, final DbConnection con, final Component cOrig,
			final String sPeriodo, boolean pendentes, boolean cancelados, boolean concluidos, boolean pbaixa, boolean pmedia, boolean palta, int icodage) {
		List<Event> eventos = new ArrayList<Event>();

		ImageIcon prioridade = null;

		ImageIcon situacao = null;

		try {

			ResultSet rs = DAOAgenda.consultaAgenda(agentes, datas, tabAgd, todos, con, cOrig, sPeriodo, pendentes, cancelados, concluidos, pbaixa, pmedia, palta, icodage);

			if (rs == null) {
				return null;
			}

			for (int i = 0; rs.next(); i++) {

				switch (rs.getInt("PriorAgd")) {
				case 2:// baixa
					prioridade = baixa;
					break;
				case 3:// media
					prioridade = media;
					break;
				case 4:// alta
					prioridade = alta;
					break;
				default:// nenhuma
					prioridade = nenhuma;
					break;
				}

				String sitagd = rs.getString("SitAgd");

				if ("PE".equals(sitagd)) {
					situacao = pend;
				}
				else if ("CA".equals(sitagd)) {
					situacao = canc;
				}
				else if ("FN".equals(sitagd)) {
					situacao = conc;
				}

				tabAgd.adicLinha();

				Calendar clini = new GregorianCalendar();

				Calendar clhoraini = new GregorianCalendar();
				Calendar clfim = new GregorianCalendar();
				Calendar clhorafim = new GregorianCalendar();

				String cod = rs.getString("CodAgd");
				String assunto = rs.getString("AssuntoAgd");
				String descagd = rs.getString("DescAgd");
				java.sql.Date dtini = rs.getDate("DtaIniAgd");
				Time horaini = rs.getTime("HrIniAgd");

				clini.setTime(dtini);
				clhoraini.setTime(horaini);

				clini.set(Calendar.HOUR, clhoraini.get(Calendar.HOUR));
				clini.set(Calendar.MINUTE, clhoraini.get(Calendar.MINUTE));

				java.sql.Date dtfim = rs.getDate("DtaFimAgd");
				Time horafim = rs.getTime("HrFimAgd");

				clfim.setTime(dtfim);
				clhorafim.setTime(horafim);

				clfim.set(Calendar.HOUR, clhorafim.get(Calendar.HOUR));
				clfim.set(Calendar.MINUTE, clhorafim.get(Calendar.MINUTE));

				Calendar clmeiodiaini = new GregorianCalendar();
				clmeiodiaini.setTime(horaini);
				clmeiodiaini.set(Calendar.AM_PM, Calendar.AM);
				clmeiodiaini.set(Calendar.HOUR, 12);
				clmeiodiaini.set(Calendar.MINUTE, 00);
				clmeiodiaini.set(Calendar.SECOND, 00);

				Calendar clmeiodiafim = new GregorianCalendar();
				clmeiodiafim.setTime(horafim);
				clmeiodiafim.set(Calendar.AM_PM, Calendar.AM);
				clmeiodiafim.set(Calendar.HOUR, 12);
				clmeiodiafim.set(Calendar.MINUTE, 00);
				clmeiodiafim.set(Calendar.SECOND, 00);

				Time meiodiaini = Funcoes.dateToSQLTime(( Date ) clmeiodiaini.getTime());
				Time meiodiafim = Funcoes.dateToSQLTime(( Date ) clmeiodiafim.getTime());

				if (horaini.after(meiodiaini)) {
					clini.set(Calendar.AM_PM, Calendar.PM);
				}
				else {
					clini.set(Calendar.AM_PM, Calendar.AM);
				}

				if (horafim.after(meiodiafim)) {
					clfim.set(Calendar.AM_PM, Calendar.PM);
				}
				else {
					clfim.set(Calendar.AM_PM, Calendar.AM);
				}

				int icor = rs.getInt("CORAGENDA");

				tabAgd.setValor(cod, i, 0);
				tabAgd.setValor(situacao, i, 1);
				tabAgd.setValor(prioridade, i, 2);
				tabAgd.setValor(assunto, i, 3);
				tabAgd.setValor(StringFunctions.sqlDateToStrDate(dtini), i, 4);
				tabAgd.setValor(horaini.toString(), i, 5);
				tabAgd.setValor(StringFunctions.sqlDateToStrDate(dtfim), i, 6);
				tabAgd.setValor(rs.getString("HrFimAgd"), i, 7);
				tabAgd.setValor(rs.getInt("CODAGE"), i, 8);
				tabAgd.setValor(rs.getInt("CODAGEEMIT"), i, 9);

				tabAgd.setColColor(i, 3, new Color(icor), icor < -10000 ? Color.WHITE : Color.BLACK);

				Event ev = new Event();
				ev.setId(cod);
				ev.setDescription(descagd);
				ev.setSummary(assunto);
				ev.setColor(new Color(icor));
				ev.setIcon(situacao);
				ev.setStart(clini.getTime());
				ev.setEnd(clfim.getTime());
				ev.setId(cod);

				eventos.add(ev.copy());

			}

			con.commit();

		}
		catch (SQLException err) {
			Funcoes.mensagemErro(cOrig, "Erro ao carregar agenda!\n" + err.getMessage(), true, con, err);
		}

		tabAgd.repaint();

		return eventos;

	}

/*	private Integer insertAgd(String hini, String hfim, String assunto, String descricao, String codfilialagd, String tipoagd, 
			String prioridade, String codagente, String tipoagente,
			Integer codfilialagt, Integer codagentee, String tipoagentee, String controleacesso, String status, String motivo, Date dtini, Date dtfim,
			boolean repete, int cont, Integer codagdar,
			String diatodo) {
*/
	private Integer insertAgd(String[] sRets, java.sql.Date dtini, java.sql.Date dtfim, boolean repete, int cont, Integer codagdar) {
		Integer ret = null;
		Agenda agenda = new Agenda();
		agenda.setHini(sRets[1] + ":00");
		agenda.setHfim(sRets[3] + ":00");
		agenda.setAssunto(sRets[4]);
		agenda.setDescricao(sRets[5]);
		agenda.setCodfilialagd(sRets[6]);
		agenda.setTipoagd(sRets[7]);
		agenda.setPrioridade(sRets[8]);
		agenda.setCodagente(sRets[9]);
		agenda.setTipoagente(sRets[10]);
		agenda.setCodfilialagt(iCodFilialAge);
		agenda.setCodagentee(iCodAge);
		agenda.setTipoagentee(sTipoAge);
		agenda.setControleacesso(sRets[11]);
		agenda.setStatus(sRets[12]);
		agenda.setMotivo(sRets[13]);
		agenda.setDtini(dtini);
		agenda.setDtfim(dtfim);
		agenda.setRepete(repete);
		agenda.setCont(cont);
		agenda.setCodagdar(codagdar);
		agenda.setDiatodo(sRets[14]);
		
		ret = daoagenda.insert(agenda);
		
		return ret;
	}
		
		/*String sql = "SELECT IRET FROM SGSETAGENDASP(0,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		ResultSet rs = null;
		Integer ret = codagdar;
		try {

			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, Aplicativo.iCodEmp);// código da empresa
			ps.setDate(2, Funcoes.dateToSQLDate(dtini));// data inicial
			ps.setString(3, hini);// hora inicial
			ps.setDate(4, Funcoes.dateToSQLDate(dtfim));// data final
			ps.setString(5, hfim);// hora final
			ps.setString(6, assunto);// assunto
			ps.setString(7, descricao);// descrição da atividade
			ps.setString(8, codfilialagd);// filial do tipo de agendamento
			ps.setString(9, tipoagd);// tipo do agendamento
			ps.setString(10, prioridade);// prioridade
			ps.setString(11, codagente);// código do agente
			ps.setString(12, tipoagente);// tipo do agente
			ps.setInt(13, codfilialagt);// filial do agente emitente
			ps.setInt(14, codagentee);// código do agente emitente
			ps.setString(15, tipoagente);// tipo do agente emitente
			ps.setString(16, controleacesso);// controle de acesso
			ps.setString(17, status);// status
			ps.setString(18, motivo);// Motivo / resolução

			if (repete && cont > 0 && codagdar != null) {
				ps.setInt(19, codagdar);
			}
			else {
				ps.setNull(19, Types.INTEGER);
			}

			ps.setString(20, diatodo);// indica se é de dia todo.

			rs = ps.executeQuery();

			if (rs.next() && cont == 0) {
				ret = rs.getInt("IRET");
			}

			ps.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return ret;

	}
*/
	private void novoAgd(Date dtini, Date dtfim) {

		HashMap<?, ?> periodicidade = null;

		if (txtIdUsu.getVlrString().equals("") || iCodAge == 0) {
			Funcoes.mensagemInforma(this, "O usuário ou o agente não foi identificado!");
			return;
		}

		if (dtini == null && dtfim == null) {
			if (calendarpanel.getValue() == null) {
				Funcoes.mensagemInforma(this, "Selecione uma data no painel!");
				return;
			}
			dtini = ( Date ) calendarpanel.getValue();
		}

		String sRets[];
		DLNovoAgen dl = new DLNovoAgen(txtIdUsu.getVlrString(), txtIdUsu.getVlrString(), dtini, dtfim, this, true);
		dl.setConexao(con);
		dl.setVisible(true);

		if (dl.OK) {

			sRets = dl.getValores();
			periodicidade = dl.getPeriodicidade();
			String tipo = ( String ) periodicidade.get("TIPO");

			try {

				if ("NR".equals(tipo)) {
					/*
					String hini, String hfim, String assunto, String descricao, String codfilialagd, String tipoagd, String prioridade, String codagente, String tipoagente,
					Integer codfilialagt, Integer codagentee, String tipoagentee, String controleacesso, String status, String motivo, Date dtini, Date dtfim, boolean repete, int cont, Integer codagdar,
					String diatodo*/
					
					insertAgd(sRets, Funcoes.strDateToSqlDate(sRets[0]), Funcoes.strDateToSqlDate(sRets[2]), false, 0, null);
					
				/*	insertAgd(sRets[1] + ":00", sRets[3] + ":00", sRets[4], sRets[5], sRets[6], sRets[7], sRets[8], sRets[9], sRets[10], iCodFilialAge, iCodAge, sTipoAge, sRets[11], sRets[12],
							sRets[13], Funcoes.strDateToSqlDate(sRets[0]), Funcoes.strDateToSqlDate(sRets[2]),sRets[14]);
*/
				}
				else {
					Integer codagdar = null;
					Date dtlimite = ( Date ) periodicidade.get("LIMITE");

					Calendar clrepete = new GregorianCalendar();
					clrepete.setTime(Funcoes.strDateToDate(sRets[0]));

					long numdias = Funcoes.getNumDias(clrepete.getTime(), dtlimite);

					int cont = 0;

					while (numdias > 0) {

						numdias = Funcoes.getNumDias(clrepete.getTime(), dtlimite);

						if ("TD".equals(tipo)) {
							
							codagdar = insertAgd(sRets,Funcoes.dateToSQLDate(clrepete.getTime()), Funcoes.dateToSQLDate(clrepete.getTime()),true, cont, codagdar);
							/*codagdar = insertAgd(sRets[1] + ":00", sRets[3] + ":00", sRets[4], sRets[5], sRets[6], sRets[7], sRets[8], sRets[9], sRets[10], iCodFilialAge, iCodAge, sTipoAge,
									sRets[11], sRets[12], sRets[13], Funcoes.dateToSQLDate(clrepete.getTime()), Funcoes.dateToSQLDate(clrepete.getTime()), true, cont, codagdar, sRets[14]);
*/
							clrepete.add(Calendar.DAY_OF_MONTH, ( Integer ) periodicidade.get("INTERVALO"));

						}
						else if ("TS".equals(tipo)) {
							if (clrepete.get(Calendar.DAY_OF_WEEK) < Calendar.SATURDAY) {

								codagdar = insertAgd(sRets, Funcoes.dateToSQLDate(clrepete.getTime()), Funcoes.dateToSQLDate(clrepete.getTime()), true, cont, codagdar);
								/*codagdar = insertAgd(sRets[1] + ":00", sRets[3] + ":00", sRets[4], sRets[5], sRets[6], sRets[7], sRets[8], sRets[9], sRets[10], iCodFilialAge, iCodAge, sTipoAge,
										sRets[11], sRets[12], sRets[13], Funcoes.dateToSQLDate(clrepete.getTime()), Funcoes.dateToSQLDate(clrepete.getTime()), true, cont, codagdar, sRets[14]);*/

							}

							clrepete.add(Calendar.DAY_OF_MONTH, 1);

						}
						else if ("T1".equals(tipo)) {
							if (( clrepete.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY ) || ( clrepete.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY )
									|| ( clrepete.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY )) {
								
								
								codagdar = insertAgd(sRets, Funcoes.dateToSQLDate(clrepete.getTime()), Funcoes.dateToSQLDate(clrepete.getTime()), true, cont, codagdar);
							}

							clrepete.add(Calendar.DAY_OF_MONTH, 1);

						}
						else if ("T2".equals(tipo)) {
							if (( clrepete.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY ) || ( clrepete.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY )) {
								codagdar = insertAgd(sRets, Funcoes.dateToSQLDate(clrepete.getTime()), Funcoes.dateToSQLDate(clrepete.getTime()), true, cont, codagdar);
								/*codagdar = insertAgd(sRets[1] + ":00", sRets[3] + ":00", sRets[4], sRets[5], sRets[6], sRets[7], sRets[8], sRets[9], sRets[10], iCodFilialAge, iCodAge, sTipoAge,
										sRets[11], sRets[12], sRets[13], Funcoes.dateToSQLDate(clrepete.getTime()), Funcoes.dateToSQLDate(clrepete.getTime()), true, cont, codagdar, sRets[14]);*/
							}

							clrepete.add(Calendar.DAY_OF_MONTH, 1);

						}
						else if ("SE".equals(tipo)) {
							codagdar = insertAgd(sRets, Funcoes.dateToSQLDate(clrepete.getTime()), Funcoes.dateToSQLDate(clrepete.getTime()), true, cont, codagdar);
							/*codagdar = insertAgd(sRets[1] + ":00", sRets[3] + ":00", sRets[4], sRets[5], sRets[6], sRets[7], sRets[8], sRets[9], sRets[10], iCodFilialAge, iCodAge, sTipoAge,
									sRets[11], sRets[12], sRets[13], Funcoes.dateToSQLDate(clrepete.getTime()), Funcoes.dateToSQLDate(clrepete.getTime()), true, cont, codagdar, sRets[14]);*/

							clrepete.add(Calendar.WEEK_OF_YEAR, ( Integer ) periodicidade.get("INTERVALO"));
						}
						else if ("ME".equals(tipo)) {
							codagdar = insertAgd(sRets, Funcoes.dateToSQLDate(clrepete.getTime()), Funcoes.dateToSQLDate(clrepete.getTime()), true, cont, codagdar);
						/*	codagdar = insertAgd(sRets[1] + ":00", sRets[3] + ":00", sRets[4], sRets[5], sRets[6], sRets[7], sRets[8], sRets[9], sRets[10], iCodFilialAge, iCodAge, sTipoAge,
									sRets[11], sRets[12], sRets[13], Funcoes.dateToSQLDate(clrepete.getTime()), Funcoes.dateToSQLDate(clrepete.getTime()), true, cont, codagdar, sRets[14]);*/

							clrepete.add(Calendar.MONTH, ( Integer ) periodicidade.get("INTERVALO"));
						}
						else if ("AN".equals(tipo)) {
							codagdar = insertAgd(sRets, Funcoes.dateToSQLDate(clrepete.getTime()), Funcoes.dateToSQLDate(clrepete.getTime()), true, cont, codagdar);
							/*codagdar = insertAgd(sRets[1] + ":00", sRets[3] + ":00", sRets[4], sRets[5], sRets[6], sRets[7], sRets[8], sRets[9], sRets[10], iCodFilialAge, iCodAge, sTipoAge,
									sRets[11], sRets[12], sRets[13], Funcoes.dateToSQLDate(clrepete.getTime()), Funcoes.dateToSQLDate(clrepete.getTime()), true, cont, codagdar, sRets[14]);*/

							clrepete.add(Calendar.YEAR, ( Integer ) periodicidade.get("INTERVALO"));
						}

						cont++;
					}
				}

				if ("S".equals(sRets[15])) { // Enviar email
					enviarEmail(sRets[9], sRets[10], sRets[4], // Assunto
							sRets[5], // Descrição do atendimento
							sRets[0], // Data Inicial
							sRets[2], // Data Final
							sRets[1], // Hora Inicial
							sRets[3] // Hora Final
					);
				}

				con.commit();

			}
			catch (SQLException err) {
				Funcoes.mensagemErro(this, "Erro ao salvar o agendamento!\n" + err.getMessage(), true, con, err);
				err.printStackTrace();
			}

			setaPeriodo(rgPeriodo.getVlrString());
		}
		dl.dispose();
	}

	public void editaAgd(String codagd, int codageemit, String tipoageemit, DbConnection con, Component cOrig) {

		try {

			String sSQL = "SELECT A.DTAINIAGD, A.HRINIAGD, A.DTAFIMAGD, A.HRFIMAGD," + "A.ASSUNTOAGD, A.DESCAGD, A.CAAGD, A.PRIORAGD, A.CODTIPOAGD, A.CODAGE, A.RESOLUCAOMOTIVO,"
					+ "U1.IDUSU IDUSUEMIT,A.SITAGD, U2.IDUSU, A.CODAGEEMIT, A.DIATODO FROM SGAGENDA A, SGUSUARIO U1, SGUSUARIO U2 " + "WHERE A.CODEMP=? AND A.CODFILIAL=? AND A.CODAGD=? "
					+ "AND U1.CODEMPAE=A.CODEMPAE AND U1.CODFILIALAE=A.CODFILIALAE " + "AND U1.CODAGE=A.CODAGEEMIT AND U1.TIPOAGE=A.TIPOAGEEMIT "
					+ "AND U2.CODEMPAE=A.CODEMP AND U2.CODFILIALAE=A.CODFILIAL " + "AND U2.CODAGE=A.CODAGE AND U2.TIPOAGE=A.TIPOAGE ";

			PreparedStatement ps = con.prepareStatement(sSQL);
			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, ListaCampos.getMasterFilial("SGAGENDA"));
			ps.setInt(3, Integer.parseInt(codagd));

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				Calendar calIni = new GregorianCalendar();
				Calendar calFim = new GregorianCalendar();

				DLNovoAgen dl = new DLNovoAgen(rs.getString("IDUSUEMIT"), rs.getString("IDUSU"), rs.getDate("DTAINIAGD"), rs.getDate("DTAFIMAGD"), cOrig, false);
				dl.setConexao(con);

				calIni.setTime(rs.getTime("HRINIAGD"));
				calFim.setTime(rs.getTime("HRFIMAGD"));

				String codage = rs.getString("CODAGE");
				String dtainiagd = StringFunctions.sqlDateToStrDate(rs.getDate("DTAINIAGD"));
				String hiniagd = StringFunctions.strZero("" + calIni.get(java.util.Calendar.HOUR_OF_DAY), 2) + ":" + StringFunctions.strZero("" + calIni.get(java.util.Calendar.MINUTE), 2);
				String dtafimagd = StringFunctions.sqlDateToStrDate(rs.getDate("DTAFIMAGD"));
				String hfimagd = StringFunctions.strZero("" + calFim.get(java.util.Calendar.HOUR_OF_DAY), 2) + ":" + StringFunctions.strZero("" + calFim.get(java.util.Calendar.MINUTE), 2);
				String assuntoagd = rs.getString("ASSUNTOAGD");
				String descagd = rs.getString("DESCAGD");
				String caagd = rs.getString("CAAGD");
				String prioragd = rs.getString("PRIORAGD");
				String codtipoagd = rs.getString("CODTIPOAGD");
				String sitagd = rs.getString("SITAGD");
				String motivoagd = rs.getString("RESOLUCAOMOTIVO");
				int codageemitagd = rs.getInt("CODAGEEMIT");
				String diatodo = rs.getString("DIATODO");

				dl.setValores(new String[] { codage, dtainiagd, hiniagd, dtafimagd, hfimagd, assuntoagd, descagd, caagd, prioragd, codtipoagd, sitagd, motivoagd, diatodo });

				dl.setVisible(true);

				if (dl.OK) {

					String[] sRets = dl.getValores();

					// Permite o salvamento pq agendamento pertence ou foi
					// criado pelo usuario atual
					if (Integer.parseInt(codage) == iCodAge || codageemit == codageemitagd) {

						try {

							sSQL = "EXECUTE PROCEDURE SGSETAGENDASP(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
							PreparedStatement ps2 = con.prepareStatement(sSQL);
							ps2.setInt(1, Integer.parseInt(( String ) codagd));
							ps2.setInt(2, Aplicativo.iCodEmp);// código da
							// empresa
							ps2.setDate(3, Funcoes.strDateToSqlDate(sRets[0]));// data
							// inicial
							ps2.setString(4, sRets[1] + ":00");// hora inicial
							ps2.setDate(5, Funcoes.strDateToSqlDate(sRets[2]));// data
							// final
							ps2.setString(6, sRets[3] + ":00");// hora final
							ps2.setString(7, sRets[4]);// assunto
							ps2.setString(8, sRets[5]);// descrição da atividade
							ps2.setString(9, sRets[6]);// filial do tipo de
							// agendamento
							ps2.setString(10, sRets[7]);// tipo do agendamento
							ps2.setString(11, sRets[8]);// prioridade
							ps2.setString(12, sRets[9]);// código do agente
							ps2.setString(13, sRets[10]);// tipo do agente
							ps2.setInt(14, ListaCampos.getMasterFilial("SGAGENTE"));// filial
							// do
							// agente
							// emitente
							ps2.setInt(15, codageemit);// código do agente
							// emitente
							ps2.setString(16, tipoageemit);// tipo do agente
							// emitente
							ps2.setString(17, sRets[11]);// controle de acesso
							ps2.setString(18, sRets[12]);// status
							ps2.setString(19, sRets[13]);// status
							ps2.setNull(20, Types.INTEGER);
							ps2.setString(21, sRets[14]);// status

							ps2.execute();
							ps2.close();

							con.commit();

						}
						catch (SQLException err) {
							Funcoes.mensagemErro(null, "Erro ao salvar o agendamento!\n" + err.getMessage(), true, con, err);
						}
					}
				}
				dl.dispose();
			}

			rs.close();
			ps.close();

			con.commit();

		}
		catch (SQLException err) {
			Funcoes.mensagemErro(null, "Erro ao buscar informações!\n" + err.getMessage(), true, con, err);
			err.printStackTrace();
		}

	}

	private void excluiAgd() {

		if (tabAgd.getLinhaSel() == -1) {
			Funcoes.mensagemInforma(this, "Selecione um item na lista!");
			return;
		}
		else if (( ( Integer ) tabAgd.getValor(tabAgd.getLinhaSel(), 9) ) != iCodAge) {
			Funcoes.mensagemInforma(this, "Não é possivel excluir este agendamento pois foi criado por outro usuário!");
			return;
		}
		else if (Funcoes.mensagemConfirma(this, "Deseja relamente excluir o agendamento '" + ( String ) tabAgd.getValor(tabAgd.getLinhaSel(), 0) + "'?") != JOptionPane.YES_OPTION) {
			return;
		}

		try {
			daoagenda.excluiAgd(Aplicativo.iCodEmp, ListaCampos.getMasterFilial("SGAGENDA"), 
					(String) tabAgd.getValor(tabAgd.getLinhaSel(), 0), (Integer) tabAgd.getValor(tabAgd.getLinhaSel(), 8), sTipoAge);
		}
		catch (SQLException err) {
			Funcoes.mensagemErro(this, "Erro ao excluir agendamento!\n" + err.getMessage(), true, con, err);
			err.printStackTrace();
		}
		carregaTabAgd();
	}

	private void enviarEmail(String codage, String tipoage, String assunto, String acao, String sdtini, String sdtfim, String shini, String shfim) {

		try {
			/*StringBuilder sql = new StringBuilder();

			sql.append("select u.idusu from sgusuario u ");
			sql.append("where u.codempae=? and u.codfilialae=? and u.codage=? and u.tipoage=?");

			PreparedStatement ps = con.prepareStatement(sql.toString());
			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, ListaCampos.getMasterFilial("SGUSUARIO"));
			ps.setString(3, codage);
			ps.setString(4, tipoage);

			ResultSet rs = ps.executeQuery();
			String userDestino = "";

			if (rs.next()) {
				userDestino = rs.getString("idusu");
			}*/
			
			
			String userDestino = daoagenda.getUserDestino(Aplicativo.iCodEmp, ListaCampos.getMasterFilial("SGUSUARIO"), codage, tipoage);

			StringBuilder sql = new StringBuilder();

			sql.append("select u.idusu,");
			sql.append("e.hostsmtp, e.portasmtp, e.usuarioremet, e.senharemet, e.criptsenha, e.usaautsmtp, e.usassl,");
			sql.append("e.emailremet de, e2.emailremet para, e.assinatremet assinatura ");
			sql.append("from tkconfemail e, sgusuario u ");
			sql.append("left outer join sgusuario u2 on u2.codemp=? and u2.codfilial=? and u2.idusu=? ");
			sql.append("left outer join tkconfemail e2 on e2.codemp=u2.codempce and e2.codfilial=u2.codfilialce and e2.codconfemail=u2.codconfemail ");
			sql.append("where u.codemp=? and u.codfilial=? and u.idusu=? and ");
			sql.append("e.codemp=u.codempce and e.codfilial=u.codfilialce and e.codconfemail=u.codconfemail");

			PreparedStatement ps = con.prepareStatement(sql.toString());
			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, ListaCampos.getMasterFilial("SGUSUARIO"));
			ps.setString(3, userDestino);
			ps.setInt(4, Aplicativo.iCodEmp);
			ps.setInt(5, ListaCampos.getMasterFilial("SGUSUARIO"));
			ps.setString(6, txtIdUsu.getVlrString());

			ResultSet rs = ps.executeQuery();
			EmailBean mail = new EmailBean();

			if (rs.next()) {
				mail.setHost(rs.getString("hostsmtp"));
				mail.setPorta(rs.getInt("portasmtp"));
				mail.setUsuario(rs.getString("usuarioremet"));
				mail.setSenha(rs.getString("senharemet"), rs.getString("criptsenha"));
				mail.setAutentica(rs.getString("usaautsmtp"));
				mail.setSsl(rs.getString("usassl"));
				mail.setDe(rs.getString("de"));
				mail.setPara(rs.getString("para"));
				mail.setAssinatura(rs.getString("assinatura"));
			}

			rs.close();
			ps.close();

			con.commit();

			mail.setAssunto("Freedom-ERP [novo agendamento] - " + assunto);
			mail.setCorpo(acao);

			/***************************************
			 * Envio de anexo contendo arquivo ICS
			 ***************************************/

			net.fortuna.ical4j.model.Calendar icsCalendar = new net.fortuna.ical4j.model.Calendar();
			icsCalendar.getProperties().add(new ProdId("-//Setpoint//Freedom-ERP//BR"));
			icsCalendar.getProperties().add(Version.VERSION_2_0);
			icsCalendar.getProperties().add(CalScale.GREGORIAN);

			java.util.Calendar startDate = new GregorianCalendar();
			java.util.Calendar endDate = new GregorianCalendar();

			Date dtini = Funcoes.strDateToDate(sdtini);
			Date dtfim = Funcoes.strDateToDate(sdtfim);

			startDate.setTime(dtini);
			endDate.setTime(dtfim);

			String eventName = assunto;
			DateTime start = new DateTime(startDate.getTime());
			DateTime end = new DateTime(endDate.getTime());

			VEvent meeting = new VEvent(start, end, eventName);

			UidGenerator ug = new UidGenerator("uidGen");
			Uid uid = ug.generateUid();

			meeting.getProperties().add(new Description(acao));

			meeting.getProperties().add(uid);

			icsCalendar.getComponents().add(meeting);

			Date dtev = new Date();
			String nomeevento = "evento" + dtev.getTime() + ".ics";

			FileOutputStream fout = new FileOutputStream(nomeevento);

			CalendarOutputter outputter = new CalendarOutputter();
			outputter.output(icsCalendar, fout);

			/*
			 * CalendarEventEntry myEntry = new CalendarEventEntry();
			 * myEntry.setContent(new
			 * PlainTextConstruct("Tennis with John April 11 3pm-3:30pm"));
			 * myEntry.setQuickAdd(true);
			 * 
			 * // Send the request and receive the response: CalendarEventEntry
			 * insertedEntry = myService.insert(postUrl, myEntry);
			 */

			/****************************************
			 * Fim da geração do anexo ICS
			 ****************************************/

			DLEnviarEmail enviaemail = new DLEnviarEmail(this, mail, nomeevento);
			enviaemail.preparar();

			if (enviaemail.preparado()) {
				enviaemail.setVisible(true);
			}
			else {
				enviaemail.dispose();
			}

		}
		catch (Exception e) {
			Funcoes.mensagemErro(this, "Erro ao enviar e-mail!\n" + e.getMessage(), true, con, e);
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent evt) {

		if (evt.getSource() == btSair) {
			dispose();
		}
		else if (evt.getSource() == btNovo) {
			novoAgd(( Date ) dsm.getSelectedDate(), ( Date ) dsm.getSelectedDate());
		}
		else if (evt.getSource() == btExcluir) {
			excluiAgd();
		}
		else if (evt.getSource() == btImp) {
			imprimir(false);
		}
		else if (evt.getSource() == btPrevImp) {
			imprimir(true);
		}
		else if (evt.getSource() == cbTodos) {
			marcaTodosUsu("S".equals(cbTodos.getVlrString()));
			carregaTabAgd();
		}
		else if (( evt.getSource() == cbPendentes ) || ( evt.getSource() == cbCancelados ) || ( evt.getSource() == cbConcluidos )) {
			carregaTabAgd();
		}
		else if (( evt.getSource() == cbBaixa ) || ( evt.getSource() == cbMedia ) || ( evt.getSource() == cbAlta )) {
			carregaTabAgd();
		}
	}

	private void marcaTodosUsu(Boolean marca) {
		try {
			for (int i = 0; tabUsu.getNumLinhas() > i; i++) {
				tabUsu.setValor(new Boolean(marca), i, 0);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setConexao(DbConnection cn) {

		super.setConexao(cn);
		lcUsu.setConexao(cn);
		lcUsu.carregaDados();

		daoagenda = new DAOAgenda(cn);
		
		buscaAgente();

		montaTabUsu();

		setaPeriodo(rgPeriodo.getVlrString());

		carregaTabAgd();

		tpnAgd.setTitleAt(0, "Agenda de " + txtNomeUsu.getVlrString());
		
	}

	public void valorAlterado(RadioGroupEvent evt) {
		if (evt.getSource() == rgPeriodo) {
			listener_calendar = false;
			if (listener_rgperiodo) {
				setaPeriodo(rgPeriodo.getVlrString());
			}
		}
	}

	public void setaPeriodo(String tipo) {
		Object[] oDatas = null;
		Calendar cl = new GregorianCalendar();
		try {
			if ("D".equals(tipo)) {
				oDatas = new Object[1];
				if (listener_rgperiodo) {
					oDatas[0] = new Date();
				}
				else if (listener_calendar) {
					oDatas[0] = ( Date ) calendarpanel.getValue();
				}

				DateSelectionModel dsm = calendarpanel.getDateSelectionModel();
				Date dt = dsm.getLeadSelectionDate();
				dayModel.setDate(dt);
			}
			else if ("S".equals(tipo)) {
				cl.setTime(new Date());
				cl.set(Calendar.DAY_OF_WEEK, 1);
				oDatas = new Object[7];
				int i = 0;
				while (7 > i) {
					oDatas[i] = cl.getTime();
					cl.set(Calendar.DAY_OF_WEEK, cl.get(Calendar.DAY_OF_WEEK) + 1);
					i++;
				}
			}
			else if ("M".equals(tipo)) {
				int mes = ms.getMonth();

				// pegar o ano painel também.

				Date dtini = Funcoes.getDataIniMes(mes, Funcoes.getAno(new Date()));
				Date dtfim = Funcoes.getDataFimMes(mes, Funcoes.getAno(new Date()));

				Long numdias = Funcoes.getNumDias(dtini, dtfim);
				int i = 0;
				oDatas = new Object[numdias.intValue()];

				cl.setTime(dtini);

				while (i < numdias.intValue()) {
					oDatas[i] = cl.getTime();
					i++;
					cl.set(Calendar.DAY_OF_MONTH, i + 1);
				}
			}

			if (listener_rgperiodo) {
				listener_calendar = false;
				calendarpanel.setValues(oDatas);
			}

			carregaTabAgd();

			listener_calendar = true;
			listener_rgperiodo = true;

			calendarpanel.refresh();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void monthDecreased(MonthChangeEvent arg0) {
		mesAlterado();
	}

	public void monthIncreased(MonthChangeEvent arg0) {
		mesAlterado();
	}

	public void mesAlterado() {
		rgPeriodo.setVlrString("M");
		setaPeriodo(rgPeriodo.getVlrString());
		monthModel.setDate(calendarpanel.getDate());
		carregaTabAgd();
	}

	public void mouseClicked(MouseEvent e) {
		/*
		 * if(e.getClickCount()==2) { rgPeriodo.setVlrString( "D" );
		 * setaPeriodo( rgPeriodo.getVlrString() ); }
		 */

		if (e.getSource() == tabAgd && e.getClickCount() == 2) {
			editaAgd(( String ) tabAgd.getValor(tabAgd.getSelectedRow(), 0), iCodAge, sTipoAge, con, e.getComponent());
			carregaTabAgd();
		}
		else if (e.getSource() == tabUsu) {
			if (tabUsu.getLinhaSel() > -1) {
				if (e.getClickCount() == 1 || e.getClickCount() == 2) {
					tabUsu.setValor(!( ( Boolean ) tabUsu.getValor(tabUsu.getLinhaSel(), 0) ).booleanValue(), tabUsu.getLinhaSel(), 0);
					carregaTabAgd();
				}
			}
		}
		else if (e.getSource() == tpnVisoes) {
			if (tpnVisoes.getSelectedIndex() == 1) {
				rgPeriodo.setVlrString("D");
				setaPeriodo("D");
			}
			else if (tpnVisoes.getSelectedIndex() == 2) {
				rgPeriodo.setVlrString("S");
				setaPeriodo("S");
			}
			else if (tpnVisoes.getSelectedIndex() == 3) {
				rgPeriodo.setVlrString("M");
				setaPeriodo("M");
			}
		}

	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void valueChanged(DateSelectionEvent dsl) {
		if (dsl.getSource() == dsm) {
			if (dsm.getSelectedDate() != null) {
				listener_rgperiodo = false;
				if (listener_calendar) {
					rgPeriodo.setVlrString("D");
					setaPeriodo(rgPeriodo.getVlrString());
				}
			}
		}
	}

	public void valorAlterado(TabelaEditEvent evt) {
	}

	public void closeCalendar(Object arg0) throws Exception {
	}

	public void copy(List<Event> arg0) throws Exception {
	}

	public void dateChanged(Date arg0) throws Exception {
	}

	public void dateSelected(Date arg0) throws Exception {
	}

	public void deleteEvent(Event arg0) throws Exception {
	}

	public void deleteEvents(List<Event> arg0) {
	}

	public void eventClicked(Object arg0, Event arg1, FrameArea arg2, MouseEvent arg3) {
	}

	public void eventSelected(Object arg0, Event arg1) throws Exception {
	}

	public void eventsSelected(List<Event> arg0) throws Exception {
	}

	public void selectionReset() throws Exception {
	}

	public void showEvent(Object arg0, Event arg1) throws Exception {
	}

	public void newCalendar() throws Exception {
	}

	public void newEvent(Object arg0, Date arg1) throws Exception {
	}

	public void paste(Object arg0, Date arg1) throws Exception {
	}

	public void eventDoubleClick(Object arg0, Event event, MouseEvent mevent) {
		editaAgd(( String ) event.getId(), 0, "", Aplicativo.getInstace().getConexao(), mevent.getComponent());
	}

	public void moved(Event arg0, Object arg1, Date arg2, Object arg3, Date arg4) throws Exception {
		// Implementar alteração de data e hora
	}

	public void newEvent(Object arg0, DateInterval arg1) throws Exception {
		novoAgd(arg1.getStartDate(), arg1.getEndDate());
	}

	public void resized(Event arg0, Object arg1, Date arg2, Date arg3) throws Exception {
		// Implementar alteração de data e hora
	}

	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == ms) {
			mesAlterado();
		}
	}

	/*private static ResultSet consultaAgenda(final Vector<Vector<?>> agentes, final Object[] datas, final JTablePad tabAgd, final boolean todos, final DbConnection con, final Component cOrig,
			final String sPeriodo, boolean pendentes, boolean cancelados, boolean concluidos, boolean pbaixa, boolean pmedia, boolean palta, int icodage) {
		ResultSet rs = null;
		// List<Event> eventos = new ArrayList<Event>();

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
*/
	private void imprimir(boolean visualizar) {
		Vector<Vector<?>> agentes = getAgentes();
		ResultSet rs = DAOAgenda.consultaAgenda(agentes, calendarpanel.getValues(), tabAgd, ( "S".equals(cbTodos.getVlrString()) ), con, this, rgPeriodo.getVlrString(), "S".equals(cbPendentes.getVlrString()),
				"S".equals(cbCancelados.getVlrString()), "S".equals(cbConcluidos.getVlrString()), "S".equals(cbBaixa.getVlrString()), "S".equals(cbMedia.getVlrString()), "S".equals(cbAlta
						.getVlrString()), iCodAge);

		FPrinterJob dlGr = new FPrinterJob("relatorios/agenda.jasper", "Agenda", null, rs, null, this);

		try {
			if (visualizar) {
				dlGr.setVisible(true);
			}
			else {
				JasperPrintManager.printReport(dlGr.getRelatorio(), true);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
