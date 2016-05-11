/**
 * @version 30/05/2003 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.cfg <BR>
 *         Classe: @(#)FObjetoTb.java <BR>
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
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDetalhe;

import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;

public class FObjetoTb extends FDetalhe implements InsertListener, ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCab = new JPanelPad();

	private JPanelPad pinDet = new JPanelPad();

	private JTextFieldPad txtIDObj = new JTextFieldPad(JTextFieldPad.TP_STRING, 30, 0);

	private JTextFieldPad txtDescObj = new JTextFieldPad(JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldPad txtCodTb = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 5, 0);

	private JTextFieldFK txtDescTb = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JCheckBoxPad cbRequerido = null;

	private ListaCampos lcTabela = new ListaCampos(this, "TB");

	public FObjetoTb() {

		lcCampos.setUsaFI(false);
		lcDet.setUsaFI(false);

		setTitulo("Vinculo entre tabelas físicas e auxiliares");
		setAtribos(50, 20, 500, 350);
		setAltCab(90);
		pinCab = new JPanelPad(500, 90);

		txtDescObj.setAtivo(false);

		lcTabela.add(new GuardaCampo(txtCodTb, "CODTB", "Cód.tab.", ListaCampos.DB_PK, txtDescTb, false));
		lcTabela.add(new GuardaCampo(txtDescTb, "DESCTB", "Descriçao da tabela", ListaCampos.DB_SI, false));
		lcTabela.setReadOnly(true);
		lcTabela.montaSql(false, "TABELA", "SG");
		lcTabela.setQueryCommit(false);

		txtCodTb.setTabelaExterna(lcTabela, FTabela.class.getCanonicalName());
		txtCodTb.setNomeCampo("CODTB");

		setListaCampos(lcCampos);
		setPainel(pinCab, pnCliCab);

		adicCampo(txtIDObj, 7, 20, 100, 20, "IDObj", "Id.obj.", ListaCampos.DB_PK, true);
		adicCampo(txtDescObj, 110, 20, 350, 20, "DescObj", "Descrição do objeto", ListaCampos.DB_SI, true);
		lcCampos.setReadOnly(true);
		lcCampos.setQueryInsert(false);
		setListaCampos(false, "OBJETO", "SG");

		setAltDet(60);
		pinDet = new JPanelPad(590, 110);
		setPainel(pinDet, pnDet);
		setListaCampos(lcDet);
		setNavegador(navRod);

		cbRequerido = new JCheckBoxPad("Requerido", "S", "N");
		cbRequerido.setVlrString("N");

		adicCampo(txtCodTb, 7, 20, 70, 20, "CODTB", "Cód.tabela", ListaCampos.DB_PF, txtDescTb, true);
		adicDescFK(txtDescTb, 80, 20, 280, 20, "DESCTB", "Descrição da tabela");
		adicDB(cbRequerido, 365, 20, 90, 20, "ObrigObjTb", "", true);
		setListaCampos(false, "OBJETOTB", "SG");
		lcCampos.setQueryInsert(false);
		montaTab();

		tab.setTamColuna(50, 0);
		tab.setTamColuna(415, 1);
		tab.setTamColuna(30, 2);

		lcCampos.addInsertListener(this);
		btImp.addActionListener(this);
		btPrevimp.addActionListener(this);
	}

	public void setConexao(DbConnection cn) {

		lcTabela.setConexao(cn);
		super.setConexao(cn);
	}

	public void actionPerformed(ActionEvent evt) {

		super.actionPerformed(evt);
	}

	public void afterInsert(InsertEvent ievt) {

	}

	public void beforeInsert(InsertEvent ievt) {

	}

}
