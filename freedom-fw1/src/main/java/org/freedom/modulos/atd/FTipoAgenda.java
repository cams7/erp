/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Alex Rodrigues <BR>
 *
 * Projeto: Freedom <BR>
 *  
 * Pacote: org.freedom.modulos.grh <BR>
 * Classe: @(#)FTipoAgenda.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA <BR> <BR>
 *
 * Tela de cadastro de tipos de agendamento
 * 
 */

package org.freedom.modulos.atd;

import java.awt.BorderLayout;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.dialog.FDialogo;

import javax.swing.JLabel;

public class FTipoAgenda extends FDialogo {

	private static final long serialVersionUID = 1L;
	private JTextFieldPad txtCodTipoAGD = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 5, 0);
	private JTextFieldPad txtDescTipoAGD = new JTextFieldPad(JTextFieldPad.TP_STRING, 40, 0);
	public ListaCampos lcCampos = new ListaCampos(null);
	public Navegador nav = new Navegador(false);

	public FTipoAgenda() {

		super();
		setTitulo("Tipos de Agendamentos", this.getClass().getName());
		setAtribos(345, 140);
		setToFrameLayout();

		pnRodape.add(nav, BorderLayout.WEST);
		nav.setListaCampos(lcCampos);
		lcCampos.setNavegador(nav);

		lcCampos.add(new GuardaCampo(txtCodTipoAGD, "CodTipoAGD", "Cód.tp.agd.", ListaCampos.DB_PK, true));
		lcCampos.add(new GuardaCampo(txtDescTipoAGD, "DescTipoAGD", "Descrição do tipo de agendamento", ListaCampos.DB_SI, true));
		lcCampos.montaSql(true, "TIPOAGENDA", "SG");
		lcCampos.setReadOnly(false);
		txtCodTipoAGD.setTabelaExterna(lcCampos, null);
		txtCodTipoAGD.setFK(true);
		txtCodTipoAGD.setNomeCampo("CodTipoAGD");

		adic(new JLabel("Cód.tp.agd."), 7, 10, 70, 20);
		adic(txtCodTipoAGD, 7, 30, 70, 20);
		adic(new JLabel("Descrição do tipo de agendamento"), 80, 10, 240, 20);
		adic(txtDescTipoAGD, 80, 30, 240, 20);

		lcCampos.setQueryInsert(false);

	}

	public void setConexao(DbConnection con) {
		// super.setConexao(con);
		lcCampos.setConexao(con);
	}

}
