/**
 * @version 10/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 *
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.componentes <BR>
 * Classe: @(#)JGroupField.java <BR>
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
 * Comentários da classe.....
 */

package org.freedom.library.swing.component;

import java.awt.Component;
import java.awt.Dimension;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.DeleteEvent;
import org.freedom.acao.DeleteListener;
import org.freedom.acao.EditEvent;
import org.freedom.acao.EditListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;

public class JGroupField extends JScrollPane implements CarregaListener, InsertListener, EditListener, DeleteListener, PostListener {
	private static final long serialVersionUID = 1L;
	private JLayeredPane lpComp = new JLayeredPane();
	private String sTab = "";
	private String sColM = "";
	private String sColD = "";
	private ListaCampos lco = null;
	private Vector<String> vCods = new Vector<String>();
	private Vector<JTextFieldPad> vTxts = new Vector<JTextFieldPad>();
	private DbConnection con = null;

	public JGroupField() {
		setBorder(BorderFactory.createEmptyBorder());
	}

	/**
	 * Ajusta os campos do JGroupField. <BR>
	 * Prepara o JGroupField para ser exibido na<BR>
	 * tela.<BR>
	 * 
	 * @param sTabela
	 *            - Nome da tabela de ligação<BR>
	 *            entre o detalhe e os campos de JGroupField.<BR>
	 * @param sColMaster
	 *            - Nome da coluna da tabela<BR>
	 *            de ligação responsável pelo codigo da Master.<BR>
	 * @param sColDet
	 *            - Nome da coluna da tabela<BR>
	 *            de ligação responsável pelo codigo do Detalhe.<BR>
	 * @param lcDetalhe
	 *            - ListaCampos do detalhe.<BR>
	 *            Este lista campos é passado para capturar<Br>
	 *            os eventos do detelhe e Salvar/Editar/Excluir/<Br>
	 *            Inserir os dados na tabela de ligação.<BR>
	 * @param cn
	 *            - Conexao do DB, é muito importante<BR>
	 *            que seja passado uma conexão ativa, para que <BR>
	 *            o JGroupField consiga montar os itens a serem<BR>
	 *            exibidos na tela.<BR>
	 * 
	 */
	public void setCampos(String sTabela, String sColMaster, String sColDet, ListaCampos lcDetalhe, DbConnection cn) {

		int iY = 0;
		if (sTabela == null)
			return;

		try {
			PreparedStatement ps = cn.prepareStatement("SELECT TB.CODTB,TB.DESCTB,OT.OBRIGOBJTB FROM SGOBJETO OB, SGOBJETOTB OT, SGTABELA TB "
					+ "WHERE OB.CODEMP=? AND OB.IDOBJ=? AND OB.TIPOOBJ='TB' " + "AND OT.CODEMP=OB.CODEMP AND OT.IDOBJ=OB.IDOBJ " + "AND OT.CODEMPTB=? AND OT.CODFILIALTB=? AND TB.CODEMP=OT.CODEMPTB "
					+ "AND TB.CODFILIAL=OT.CODFILIALTB AND TB.CODTB=OT.CODTB");
			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setString(2, sTabela);
			ps.setInt(3, Aplicativo.iCodEmp);
			ps.setInt(4, ListaCampos.getMasterFilial("SGTABELA"));
			ResultSet rs = ps.executeQuery();
			vCods.clear();
			vTxts.clear();
			for (int i = 0; rs.next(); i++) {

				ListaCampos lcCampos = new ListaCampos(getOwnerTela(), "TB");
				JTextFieldPad txtCod = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);
				JTextFieldFK txtDesc = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

				lcCampos.add(new GuardaCampo(txtCod, "CodItTb", "Código", ListaCampos.DB_PK, false), "txtCodVendx");
				lcCampos.add(new GuardaCampo(txtDesc, "DescItTb", "Descriçao", ListaCampos.DB_SI, false), "txtCodVendx");
				lcCampos.setWhereAdic("CODTB=" + rs.getInt("CodTb"));
				lcCampos.montaSql(false, "ITTABELA", "SG");
				lcCampos.setQueryCommit(false);
				lcCampos.setReadOnly(true);
				txtCod.setTabelaExterna(lcCampos, null);
				txtCod.setListaCampos(lcDetalhe);
				txtCod.setNomeCampo("CodItTb");
				txtCod.setFK(true);
				lcCampos.setConexao(cn);

				if (rs.getString("OBRIGOBJTB").equals("S"))
					txtCod.setRequerido(true);

				JLabelPad lbRef = new JLabelPad("Código e descrição de " + rs.getString("DescTb").trim());
				lbRef.setBounds(0, iY, 330, 20);

				txtCod.setBounds(0, iY + 20, 77, 20);
				txtDesc.setBounds(80, iY + 20, 250, 20);

				lpComp.add(lbRef);
				lpComp.add(txtCod);
				lpComp.add(txtDesc);

				iY += 40;

				vCods.add(rs.getString("CodTb"));
				vTxts.add(txtCod);
			}
			rs.close();
			ps.close();
		}
		catch (SQLException err) {
			Funcoes.mensagemErro(getOwnerTela(), "Erro ao buscar tabela base.\n" + err.getMessage());
		}

		lcDetalhe.addCarregaListener(this);
		lcDetalhe.addPostListener(this);
		lcDetalhe.addInsertListener(this);
		lcDetalhe.addEditListener(this);
		lcDetalhe.addDeleteListener(this);

		con = cn;
		lco = lcDetalhe;
		sTab = sTabela;
		sColM = sColMaster;
		sColD = sColDet;

		lpComp.setPreferredSize(new Dimension(300, iY));
		setViewportView(lpComp);
		getVerticalScrollBar().setUnitIncrement(10);
	}

	public Component getOwnerTela() {
		Component cFrame = null;
		Component cRetorno = null;
		cFrame = this.getParent();
		if (cFrame != null) {
			for (int i = 1; 1 <= 10; i++) {
				if (( cFrame instanceof JFrame ) || ( cFrame instanceof JInternalFrame ) || ( cFrame instanceof JDialog )) {
					cRetorno = cFrame;
					break;
				}
				cFrame = cFrame.getParent();
			}
		}
		return cRetorno;
	}

	public void beforeInsert(InsertEvent ievt) {
	}

	public void beforeEdit(EditEvent pevt) {
	}

	public void beforeCarrega(CarregaEvent cevt) {
	}

	public void beforePost(PostEvent pevt) {
	}

	public void afterEdit(EditEvent pevt) {
		String sVal = "";
		for (int i = 0; i < vCods.size(); i++) {
			if (( sVal = vTxts.elementAt(i).getVlrString() ).equals(""))
				continue;
			try {
				String sSQL = "DELETE FROM " + sTab + " WHERE CODEMP=? AND CODFILIAL=? AND " + sColM + "=? AND " + sColD + "=? AND CODEMPTB=? AND CODFILIALTB=? AND CODTB=? AND CODITTB=?";
				PreparedStatement ps = con.prepareStatement(sSQL);
				ps.setInt(1, Aplicativo.iCodEmp);
				ps.setInt(2, lco.getMaster().getCodFilial());
				ps.setInt(3, lco.getMaster().getCampo(sColM).getVlrInteger().intValue());
				ps.setInt(4, lco.getCampo(sColD).getVlrInteger().intValue());
				ps.setInt(5, Aplicativo.iCodEmp);
				ps.setInt(6, ListaCampos.getMasterFilial("SGITTABELA"));
				ps.setString(7, "" + vCods.elementAt(i));
				ps.setString(8, sVal);
				ps.execute();
				ps.close();
			}
			catch (SQLException err) {
				Funcoes.mensagemErro(this, "Erro ao apagar codigos do JGroupField!\n" + err.getMessage());
			}
		}
	}

	public void afterInsert(InsertEvent ievt) {
	}

	public void afterDelete(DeleteEvent devt) {
	}

	public void afterPost(PostEvent pevt) {
		String sVal = "";
		if (pevt.ok) {
			for (int i = 0; i < vCods.size(); i++) {
				try {
					if (( sVal = vTxts.elementAt(i).getVlrString() ).equals(""))
						continue;

					String sSQL = "INSERT INTO " + sTab + " (CODEMP,CODFILIAL," + sColM + "," + sColD + ",CODEMPTB,CODFILIALTB,CODTB,CODITTB)" + " VALUES (?,?,?,?,?,?,?,?)";
					PreparedStatement ps = con.prepareStatement(sSQL);
					ps.setInt(1, Aplicativo.iCodEmp);
					ps.setInt(2, lco.getMaster().getCodFilial());
					ps.setInt(3, lco.getMaster().getCampo(sColM).getVlrInteger().intValue());
					ps.setInt(4, lco.getCampo(sColD).getVlrInteger().intValue());
					ps.setInt(5, Aplicativo.iCodEmp);
					ps.setInt(6, ListaCampos.getMasterFilial("SGITTABELA"));
					ps.setString(7, "" + vCods.elementAt(i));
					ps.setString(8, sVal);
					ps.execute();
					ps.close();
					con.commit();
				}
				catch (SQLException err) {
					Funcoes.mensagemErro(this, "Erro ao inserir codigos do JGroupField!\n" + err.getMessage());
				}
			}
		}
	}

	public void beforeDelete(DeleteEvent devt) {
		String sVal = "";
		for (int i = 0; i < vCods.size(); i++) {
			if (( sVal = vTxts.elementAt(i).getVlrString() ).equals(""))
				continue;
			try {
				String sSQL = "DELETE FROM " + sTab + " WHERE CODEMP=? AND CODFILIAL=? AND " + sColM + "=? AND " + sColD + "=? AND CODEMPTB=? AND CODFILIALTB=? AND CODTB=? AND CODITTB=?";
				PreparedStatement ps = con.prepareStatement(sSQL);
				ps.setInt(1, Aplicativo.iCodEmp);
				ps.setInt(2, lco.getMaster().getCodFilial());
				ps.setInt(3, lco.getMaster().getCampo(sColM).getVlrInteger().intValue());
				ps.setInt(4, lco.getCampo(sColD).getVlrInteger().intValue());
				ps.setInt(5, Aplicativo.iCodEmp);
				ps.setInt(6, ListaCampos.getMasterFilial("SGITTABELA"));
				ps.setString(7, "" + vCods.elementAt(i));
				ps.setString(8, sVal);
				ps.execute();
				ps.close();
				con.commit();
				vTxts.elementAt(i).getTabelaExterna().limpaCampos(true);
			}
			catch (SQLException err) {
				Funcoes.mensagemErro(this, "Erro ao apagar codigos do JGroupField!\n" + err.getMessage());
			}
		}
	}

	public void afterCarrega(CarregaEvent cevt) {
		if (cevt.ok) {
			for (int i = 0; i < vCods.size(); i++) {
				try {
					String sSQL = "SELECT CODITTB FROM " + sTab + " WHERE CODEMP=? AND CODFILIAL=? AND " + sColM + "=? AND " + sColD + "=? AND CODEMPTB=? AND CODFILIALTB=? AND CODTB=?";
					PreparedStatement ps = con.prepareStatement(sSQL);
					ps.setInt(1, Aplicativo.iCodEmp);
					ps.setInt(2, lco.getMaster().getCodFilial());
					ps.setInt(3, lco.getMaster().getCampo(sColM).getVlrInteger().intValue());
					ps.setInt(4, lco.getCampo(sColD).getVlrInteger().intValue());
					ps.setInt(5, Aplicativo.iCodEmp);
					ps.setInt(6, ListaCampos.getMasterFilial("SGITTABELA"));
					ps.setString(7, "" + vCods.elementAt(i));
					ResultSet rs = ps.executeQuery();
					if (rs.next()) {
						if (rs.getString("CodItTB") != null) {
							vTxts.elementAt(i).setVlrString(rs.getString("CodItTB"));
							vTxts.elementAt(i).getTabelaExterna().carregaDados();
						}
						else
							vTxts.elementAt(i).setVlrString("");
					}
					rs.close();
					ps.close();
				}
				catch (SQLException err) {
					Funcoes.mensagemErro(this, "Erro ao buscar codigos do JGroupField!\n" + err.getMessage());
				}
			}
		}
	}

	public void edit(EditEvent eevt) {
	}
}
