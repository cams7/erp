/**
 * @version 22/05/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.cfg <BR>
 *         Classe: @(#)FMenuObj.java <BR>
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

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDetalhe;

public class FMenuObj extends FDetalhe {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCab = new JPanelPad();

	private JPanelPad pinDet = new JPanelPad();

	private JTextFieldPad txtCodMenu = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 9, 0);

	private JTextFieldFK txtDescMenu = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldPad txtCodModu = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 9, 0);

	private JTextFieldFK txtDescModu = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldPad txtCodSis = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 9, 0);

	private JTextFieldFK txtDescSis = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldPad txtIDObj = new JTextFieldPad(JTextFieldPad.TP_STRING, 30, 0);

	private JTextFieldFK txtDescObj = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private ListaCampos lcMenu = new ListaCampos(this);

	private ListaCampos lcModu = new ListaCampos(this);

	private ListaCampos lcSis = new ListaCampos(this);

	private ListaCampos lcObj = new ListaCampos(this, "");

	public FMenuObj() {

		setTitulo("Cadastro de Menu x Objeto Sistema");
		setAtribos(50, 50, 367, 360);

		lcCampos.setUsaME(false);
		lcDet.setUsaME(false);
		lcMenu.setUsaME(false);
		lcModu.setUsaME(false);
		lcSis.setUsaME(false);
		lcObj.setUsaFI(false);

		lcMenu.add(new GuardaCampo(txtCodMenu, "CODMENU", "Cód.menu", ListaCampos.DB_PK, txtDescMenu, false));
		lcMenu.add(new GuardaCampo(txtDescMenu, "DESCMENU", "Descriçao do menu", ListaCampos.DB_SI, false));
		lcMenu.montaSql(false, "MENU", "SG");
		lcMenu.setQueryCommit(false);
		lcMenu.setReadOnly(true);
		txtCodMenu.setTabelaExterna(lcMenu, FMenuObj.class.getCanonicalName());

		lcModu.add(new GuardaCampo(txtCodModu, "CODMODU", "Cód.modu.", ListaCampos.DB_PK, txtDescModu, false));
		lcModu.add(new GuardaCampo(txtDescModu, "DESCMODU", "Descriçao do modulo", ListaCampos.DB_SI, false));
		lcModu.montaSql(false, "MODULO", "SG");
		lcModu.setQueryCommit(false);
		lcModu.setReadOnly(true);
		txtCodModu.setTabelaExterna(lcModu, null);

		lcSis.add(new GuardaCampo(txtCodSis, "CODSIS", "Cód.sis.", ListaCampos.DB_PK, txtDescSis, false));
		lcSis.add(new GuardaCampo(txtDescSis, "DESCSIS", "Descriçao do sistema", ListaCampos.DB_SI, false));
		lcSis.montaSql(false, "SISTEMA", "SG");
		lcSis.setQueryCommit(false);
		lcSis.setReadOnly(true);
		txtCodSis.setTabelaExterna(lcSis, null);

		lcObj.add(new GuardaCampo(txtIDObj, "IDOBJ", "Id.obj.", ListaCampos.DB_PK, false));
		lcObj.add(new GuardaCampo(txtDescObj, "DESCOBJ", "Descriçao do objeto", ListaCampos.DB_SI, false));
		lcObj.montaSql(false, "OBJETO", "SG");
		lcObj.setQueryCommit(false);
		lcObj.setReadOnly(true);
		txtIDObj.setTabelaExterna(lcObj, FObjetoTb.class.getCanonicalName());

		pinCab = new JPanelPad(350, 165);
		setAltCab(165);
		setListaCampos(lcCampos);
		setPainel(pinCab, pnCliCab);
		adicCampo(txtCodSis, 7, 20, 80, 20, "CODSIS", "Cód.sis.", ListaCampos.DB_PF, true);
		adicDescFK(txtDescSis, 90, 20, 240, 20, "DESCSIS", "Descrição do sistema");
		adicCampo(txtCodModu, 7, 60, 80, 20, "CODMODU", "Cód.modu", ListaCampos.DB_PF, true);
		adicDescFK(txtDescModu, 90, 60, 240, 20, "DESCMODU", "Descrição do modulo");
		adicCampo(txtCodMenu, 7, 100, 80, 20, "CODMENU", "Cód.menu", ListaCampos.DB_PF, true);
		adicDescFK(txtDescMenu, 90, 100, 240, 20, "DESCMENU", "Descrição do menu");
		lcCampos.setReadOnly(true);
		setListaCampos(false, "MENU", "SG");

		setAltDet(60);
		pinDet = new JPanelPad(350, 90);
		setPainel(pinDet, pnDet);
		setListaCampos(lcDet);
		setNavegador(navRod);
		adicCampo(txtIDObj, 7, 20, 80, 20, "IDOBJ", "Id.obj", ListaCampos.DB_PK, true);
		adicDescFK(txtDescObj, 90, 20, 240, 20, "DESCOBJ", "Descrição do objeto");
		setListaCampos(false, "MENUOBJ", "SG");
		montaTab();

		tab.setTamColuna(200, 1);
	}

	public void setConexao(DbConnection cn) {

		super.setConexao(cn);
		lcMenu.setConexao(cn);
		lcModu.setConexao(cn);
		lcSis.setConexao(cn);
		lcObj.setConexao(cn);
	}
}
