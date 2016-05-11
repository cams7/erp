/**
 * @version 12/08/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.cfg <BR>
 *         Classe: @(#)FTarefa.java <BR>
 * 
 *         Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *         modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *         na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *         Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *         sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *         Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *         Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *         escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA <BR>
 * <BR>
 * 
 *         Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.cfg.view.frame.crud.plain;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FFilho;

public class FAnaliseLog extends FFilho implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCab = new JPanelPad(0, 240);

	private JTablePad tab = new JTablePad();

	private JScrollPane spnTab = new JScrollPane(tab);

	private JPanelPad pnCli = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());

	private JTextFieldPad txtDataini = new JTextFieldPad(JTextFieldPad.TP_DATE, 10, 0);

	private JTextFieldPad txtDatafim = new JTextFieldPad(JTextFieldPad.TP_DATE, 10, 0);

	private JTextFieldPad txtIDObj = new JTextFieldPad(JTextFieldPad.TP_STRING, 30, 0);

	private JTextFieldFK txtDescObj = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldPad txtCodUsu = new JTextFieldPad(JTextFieldPad.TP_STRING, 8, 0);

	private JTextFieldFK txtNomeUsu = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JRadioGroup<?, ?> rgOperacao = null;

	private JRadioGroup<?, ?> rgEventLog = null;

	private ListaCampos lcObjeto = new ListaCampos(this);

	private ListaCampos lcLogCrud = new ListaCampos(this);

	private ListaCampos lcUsuario = new ListaCampos(this);

	private JButtonPad btSair = new JButtonPad("Sair", Icone.novo("btSair.png"));

	private JButtonPad btGerar = new JButtonPad("Gerar", Icone.novo("btGerar.png"));

	public FAnaliseLog() {
		super(false);
		setTitulo("Análise de Logs");
		setAtribos(50, 50, 650, 500);

		btSair.addActionListener(this);

		montaRadioGroup();
		montaListaCampos();
		montaTela();

	}

	public void montaListaCampos() {

		/*************
		 * SGOBJETO *
		 *************/
		txtIDObj.setNomeCampo("IDOBJ");
		lcObjeto.setUsaFI(false);
		lcObjeto.add(new GuardaCampo(txtIDObj, "IDOBJ", "Id.obj.", ListaCampos.DB_PK, false));
		lcObjeto.add(new GuardaCampo(txtDescObj, "DESCOBJ", "Descriçao do objeto", ListaCampos.DB_SI, false));
		lcObjeto.montaSql(false, "OBJETO", "SG");
		lcObjeto.setQueryCommit(false);
		lcUsuario.setReadOnly(true);
		txtIDObj.setChave(ListaCampos.DB_PK);
		txtIDObj.setListaCampos(lcObjeto);
		txtDescObj.setListaCampos(lcObjeto);
		txtIDObj.setTabelaExterna(lcObjeto, null);
		txtIDObj.setNomeCampo("IDOBJ");

		/*************
		 * SGUSUARIO *
		 *************/

		txtCodUsu.setNomeCampo("IdUsuario");
		lcUsuario.add(new GuardaCampo(txtCodUsu, "IDUSU", "ID Usuario", ListaCampos.DB_PK, txtNomeUsu, false));
		lcUsuario.add(new GuardaCampo(txtNomeUsu, "NOMEUSU", "Nome", ListaCampos.DB_SI, false));
		lcUsuario.montaSql(false, "USUARIO", "SG");
		lcUsuario.setQueryCommit(false);
		lcUsuario.setReadOnly(true);
		txtCodUsu.setChave(ListaCampos.DB_PK);
		txtCodUsu.setListaCampos(lcUsuario);
		txtNomeUsu.setListaCampos(lcUsuario);
		txtCodUsu.setTabelaExterna(lcUsuario, null);
		txtCodUsu.setNomeCampo("idusu");

	}

	public void montaTela() {
		GregorianCalendar cPeriodo = new GregorianCalendar();
		txtDatafim.setVlrDate(cPeriodo.getTime());
		cPeriodo.set(Calendar.DAY_OF_MONTH, cPeriodo.get(Calendar.DAY_OF_MONTH) - 30);
		txtDataini.setVlrDate(cPeriodo.getTime());
		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder(BorderFactory.createEtchedBorder());

		pinCab.adic(new JLabelPad("Periodo:"), 7, 5, 100, 20);
		pinCab.adic(lbLinha, 7, 25, 273, 40);
		pinCab.adic(new JLabelPad("De:"), 15, 35, 30, 20);
		pinCab.adic(txtDataini, 45, 35, 95, 20);
		pinCab.adic(new JLabelPad("Até:"), 145, 35, 30, 20);
		pinCab.adic(txtDatafim, 175, 35, 95, 20);

		pinCab.adic(txtCodUsu, 7, 83, 80, 20, "Cód.Usu.");
		pinCab.adic(txtNomeUsu, 90, 83, 270, 20, "Nome usuário");

		pinCab.adic(txtIDObj, 7, 123, 120, 20, "Tabela.");
		pinCab.adic(txtDescObj, 130, 123, 230, 20, "Descrição da tabela");

		pinCab.adic(rgOperacao, 370, 23, 250, 60, "Operação");
		pinCab.adic(rgEventLog, 370, 103, 250, 90, "Event Log");

		pinCab.adic(btGerar, 520, 196, 100, 30);

		adicBotaoSair();

		getTela().add(pnCli, BorderLayout.CENTER);
		pnCli.add(pinCab, BorderLayout.NORTH);
		pnCli.add(spnTab, BorderLayout.CENTER);

	}

	private void montaRadioGroup() {
		Vector<String> vLabsOP = new Vector<String>();
		Vector<String> vValsOP = new Vector<String>();

		vLabsOP.addElement("Todos");
		vLabsOP.addElement("Update");
		vLabsOP.addElement("Delete");
		vLabsOP.addElement("Insert");
		vValsOP.addElement("T");
		vValsOP.addElement("U");
		vValsOP.addElement("D");
		vValsOP.addElement("I");
		rgOperacao = new JRadioGroup<String, String>(2, 2, vLabsOP, vValsOP);
		rgOperacao.setVlrString("T");

		Vector<String> vLabsEV = new Vector<String>();
		Vector<String> vValsEV = new Vector<String>();

		vLabsEV.addElement("Todos");
		vLabsEV.addElement("Before \"Antes da operação\"");
		vLabsEV.addElement("After \"Após a operação\"");

		vValsEV.addElement("T");
		vValsEV.addElement("B");
		vValsEV.addElement("A");
		rgEventLog = new JRadioGroup<String, String>(3, 1, vLabsEV, vValsEV);
		rgEventLog.setVlrString("T");
	}

	public void setConexao(DbConnection cn) {

		super.setConexao(cn);
		lcObjeto.setConexao(cn);
		lcLogCrud.setConexao(cn);
		lcUsuario.setConexao(cn);
	}

	public void actionPerformed(ActionEvent evt) {

		if (evt.getSource() == btSair) {
			dispose();
		}
	}
}
