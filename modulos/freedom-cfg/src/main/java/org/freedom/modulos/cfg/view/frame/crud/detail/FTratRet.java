/**
 * @version 12/08/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.cfg <BR>
 *         Classe: @(#)FTratRet.java <BR>
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

package org.freedom.modulos.cfg.view.frame.crud.detail;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDetalhe;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.cfg.view.dialog.report.DLRFluxo;
import org.freedom.modulos.cfg.view.frame.crud.plain.FTarefa;

public class FTratRet extends FDetalhe implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodProc = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldPad txtCodTar = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldPad txtCodTar2 = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldFK txtDescTar = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldPad txtCodItem = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldPad txtCodRet = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldPad txtCodItProc = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JPanelPad pinCab = new JPanelPad();

	private JPanelPad pinDet = new JPanelPad();

	private Vector<String> vValsTipo = new Vector<String>();

	private Vector<String> vLabsTipo = new Vector<String>();

	private JComboBoxPad cbTipo = null;

	private ListaCampos lcTarefa = new ListaCampos(this, "TA");

	private ListaCampos lcItProc = new ListaCampos(this, "GT");

	int iCodProc = 0;

	int iCodItem = 0;

	public FTratRet(int iCodP, int iCodI) {

		iCodProc = iCodP;
		iCodItem = iCodI;

		setTitulo("Tratamento de retornos");
		setAtribos(100, 100, 450, 350);

		setAltCab(90);
		pinCab = new JPanelPad(420, 90);
		setListaCampos(lcCampos);
		setPainel(pinCab, pnCliCab);

		txtCodProc.setAtivo(false);
		txtCodItem.setAtivo(false);
		txtCodTar.setAtivo(false);

		// Montando tipos:

		vValsTipo.add("");
		vValsTipo.add("01");
		vValsTipo.add("02");
		vValsTipo.add("03");
		vLabsTipo.add("<--Selecione-->");
		vLabsTipo.add("IR PARA");
		vLabsTipo.add("ABORTA");
		vLabsTipo.add("AGUARDE");
		cbTipo = new JComboBoxPad(vLabsTipo, vValsTipo, JComboBoxPad.TP_STRING, 2, 0);

		lcTarefa.add(new GuardaCampo(txtCodTar, "CodTarefa", "Cód.tarefa", ListaCampos.DB_PK, true));
		lcTarefa.add(new GuardaCampo(txtDescTar, "DescTarefa", "Descrição da tarefa", ListaCampos.DB_SI, false));
		lcTarefa.montaSql(false, "TAREFA", "SG");
		lcTarefa.setQueryCommit(false);
		lcTarefa.setReadOnly(true);
		txtCodTar.setTabelaExterna(lcTarefa, FTarefa.class.getCanonicalName());

		lcItProc.add(new GuardaCampo(txtCodItProc, "SeqItProc", "Item.", ListaCampos.DB_PK, false));
		lcItProc.add(new GuardaCampo(txtCodTar2, "CodTarefa", "Cód.tarefa.", ListaCampos.DB_SI, false));
		lcItProc.setWhereAdic("CodProc=" + iCodProc);
		lcItProc.montaSql(false, "ITPROCESSO", "SG");
		lcItProc.setQueryCommit(false);
		lcItProc.setReadOnly(true);
		txtCodItProc.setTabelaExterna(lcItProc, null);

		adicCampo(txtCodProc, 7, 20, 70, 20, "CodProc", "Cód.proc.", ListaCampos.DB_PK, true);
		adicCampo(txtCodItem, 80, 20, 37, 20, "SeqItProc", "Item", ListaCampos.DB_PK, true);
		adicCampo(txtCodTar, 120, 20, 70, 20, "CodTarefa", "Cód.tarefa", ListaCampos.DB_FK, txtDescTar, true);
		adicDescFK(txtDescTar, 193, 20, 197, 20, "DescTar", "Descrição da tarefa");
		setListaCampos(false, "ITPROCESSO", "SG");

		setAltDet(60);
		setPainel(pinDet, pnDet);
		setListaCampos(lcDet);
		setNavegador(navRod);

		adicCampo(txtCodRet, 7, 20, 40, 20, "CodTratRet", "Ret.", ListaCampos.DB_PK, true);
		adicDB(cbTipo, 50, 18, 227, 25, "TipoTratRet", "Tipo", true);
		adicCampo(txtCodItProc, 280, 20, 77, 20, "SeqItProcGT", "Passo", ListaCampos.DB_FK, false);
		setListaCampos(true, "TRATARET", "SG");

		montaTab();
		tab.setTamColuna(40, 0);
		tab.setTamColuna(80, 1);

		btImp.addActionListener(this);
		btPrevimp.addActionListener(this);

		lcCampos.setReadOnly(true);
		setImprimir(true);
	}

	public void actionPerformed(ActionEvent evt) {

		if (evt.getSource() == btPrevimp) {
			imprimir(TYPE_PRINT.VIEW);
		} else if (evt.getSource() == btImp)
			imprimir(TYPE_PRINT.PRINT);
		super.actionPerformed(evt);
	}

	public void setConexao(DbConnection cn) {

		super.setConexao(cn);
		lcTarefa.setConexao(cn);
		lcItProc.setConexao(cn);
		txtCodProc.setVlrInteger(new Integer(iCodProc));
		txtCodItem.setVlrInteger(new Integer(iCodItem));
		lcCampos.carregaDados();
	}

	private void imprimir(TYPE_PRINT bVisualizar) {

		ImprimeOS imp = new ImprimeOS("", con);
		int linPag = imp.verifLinPag() - 1;
		imp.montaCab();
		imp.setTitulo("Relatório de Fluxos");
		DLRFluxo dl = new DLRFluxo(this);
		dl.setVisible(true);
		if (dl.OK == false) {
			dl.dispose();
			return;
		}
		String sSQL = "SELECT CODFLUXO,DESCFLUXO FROM ATFLUXO ORDER BY " + dl.getValor();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(sSQL);
			rs = ps.executeQuery();
			imp.limpaPags();
			while (rs.next()) {
				if (imp.pRow() == 0) {
					imp.impCab(80, false);
					imp.say(imp.pRow() + 0, 0, "" + imp.normal());
					imp.say(imp.pRow() + 0, 0, "");
					imp.say(imp.pRow() + 0, 2, "Código");
					imp.say(imp.pRow() + 0, 25, "Descrição");
					imp.say(imp.pRow() + 1, 0, "" + imp.normal());
					imp.say(imp.pRow() + 0, 0, StringFunctions.replicate("-", 80));
				}
				imp.say(imp.pRow() + 1, 0, "" + imp.normal());
				imp.say(imp.pRow() + 0, 2, rs.getString("CodFluxo"));
				imp.say(imp.pRow() + 0, 25, rs.getString("DescFluxo"));
				if (imp.pRow() >= linPag) {
					imp.incPags();
					imp.eject();
				}
			}

			imp.say(imp.pRow() + 1, 0, "" + imp.normal());
			imp.say(imp.pRow() + 0, 0, StringFunctions.replicate("=", 80));
			imp.eject();

			imp.fechaGravacao();

			// rs.close();
			// ps.close();
			con.commit();
			dl.dispose();
		} catch (SQLException err) {
			Funcoes.mensagemErro(this, "Erro consulta tabela de fluxos!\n" + err.getMessage(), true, con, err);
		}

		if (bVisualizar == TYPE_PRINT.VIEW) {
			imp.preview(this);
		} else {
			imp.print();
		}
	}
}
