/**
 * @version 14/11/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 *
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.telas <BR>
 * Classe: @(#)FLogin.java <BR>
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
 * Comentários para a classe...
 */

package org.freedom.library.swing.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Properties;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.border.BevelBorder;

import org.freedom.bmps.Icone;
import org.freedom.infra.beans.Usuario;
import org.freedom.infra.functions.SystemFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JPasswordFieldPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FDialogo;
import org.freedom.library.swing.util.SwingParams;

public abstract class Login extends FDialogo implements ActionListener, FocusListener {
	private static final long serialVersionUID = 1L;
	protected JTextFieldPad txtUsuario = new JTextFieldPad(JTextFieldPad.TP_STRING, 15, 0);
	protected JPasswordFieldPad txpSenha = new JPasswordFieldPad(9);
	protected Vector<Integer> vVals = new Vector<Integer>();
	protected Vector<String> vLabs = new Vector<String>();
	protected JComboBoxPad cbEmp = new JComboBoxPad(vLabs, vVals, JComboBoxPad.TP_INTEGER, 8, 0);
	protected String nfe = "";
	protected String nfse = "";
	protected String strBanco = "";
	protected String strBanconfe = "";
	protected String strDriver = "";
	protected String sUsuAnt = "";
	protected int iFilialMz = 0;
	protected DbConnection conLogin = null;
	protected DbConnection conNFE = null;
	protected JLabelPad lbInstrucoes = new JLabelPad("");
	protected Properties props = new Properties();
	public boolean bAdmin = false;
	protected int iFilialPadrao = 0;
	protected int iCodEst = 0;
	protected int tries = 0;
	private JPanelPad pnMaster = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());

	private JPanelPad pnSplash = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());
	private JPanelPad pnVersao = new JPanelPad();
	private JPanelPad pnCampos = new JPanelPad();
	private JLabelPad lbVersao = new JLabelPad();

	private String versao = "";

	public abstract void inicializaLogin();

	public void execLogin(String sBanco, String sDriver, String sImg, int iCodEstP) {

		strBanco = sBanco;
		strDriver = sDriver;

		this.iCodEst = iCodEstP;

		c.add(pnMaster);

		pnMaster.add(pnSplash, BorderLayout.NORTH);
		pnMaster.add(pnCampos, BorderLayout.CENTER);
		pnMaster.add(pnVersao, BorderLayout.SOUTH);

		ImageIcon ic = Icone.novo(sImg);
		JLabelPad lbImg = new JLabelPad(Icone.novo(sImg));

		int iWidth = ic.getIconWidth();
		int iHeight = ic.getIconHeight();

		
		if (SystemFunctions.getOS() == SystemFunctions.OS_WINDOWS && SystemFunctions.getWindowsVersion()==SystemFunctions.OS_VERSION_WINDOWS_SEVEN) {
			setAtribos(iWidth + 17, iHeight + 203);
		} 
		else {
			setAtribos(iWidth + 7, iHeight + 193);
		}


		lbImg.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

		pnSplash.add(lbImg, BorderLayout.CENTER);
		pnMaster.setBorder(new BevelBorder(BevelBorder.RAISED));

		pnCampos.setBackground(new Color(228, 228, 228));
		pnCampos.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
		pnVersao.setBorder(BorderFactory.createEmptyBorder());
		pnVersao.setBackground(Color.WHITE);

		JLabelPad lbUsuario = new JLabelPad("Usuário");
		lbUsuario.setForeground(new Color(49, 49, 89));

		pnCampos.adic(lbUsuario, 7, 0, 180, 20);
		pnCampos.adic(txtUsuario, 7, 20, 180, 20);

		JLabelPad lbSenha = new JLabelPad("Senha");
		lbSenha.setForeground(new Color(49, 49, 89));

		pnCampos.adic(lbSenha, 190, 0, 100, 20);
		pnCampos.adic(txpSenha, 190, 20, 100, 20);

		JLabelPad lbFilial = new JLabelPad("Filial");
		lbFilial.setForeground(new Color(49, 49, 89));

		pnCampos.adic(lbFilial, 7, 40, 200, 20);
		pnCampos.adic(cbEmp, 7, 60, 283, 20);

		pnCampos.adic(lbInstrucoes, 7, 83, 300, 20);

		pnVersao.setPreferredSize(new Dimension(10, 20));

		versao = SystemFunctions.getVersionSis(this.getClass());

		lbVersao.setText("  Versão: " + versao);

		lbVersao.setFont(SwingParams.getFontpadmed());
		lbVersao.setForeground(Color.DARK_GRAY);

		pnVersao.adic(lbVersao, 0, 0, 300, 20);

		setVisible(true);

	}

	public Login() {
		String sUsuarioTst = Aplicativo.getParameter("usuariotst");
		String sSenhaTst = Aplicativo.getParameter("senhatst");

		setTitulo("Login", this.getClass().getName());
		lbInstrucoes.setForeground(new Color(74, 65, 120));
		txtUsuario.addFocusListener(this);
		txpSenha.addFocusListener(this);
		cbEmp.addFocusListener(this);
		btOK.addFocusListener(this);

		if (( sUsuarioTst != null ) && ( sSenhaTst != null )) {
			if (( sUsuarioTst.length() > 0 ) && ( sSenhaTst.length() > 0 )) {
				txtUsuario.setVlrString(sUsuarioTst);
				txpSenha.setVlrString(sSenhaTst);
				// btOK.doClick();
			}
		}
	}
	
	public Usuario getUsuario() {
		Usuario result = new Usuario();
		result.setIdusu(txtUsuario.getText().trim().toLowerCase());
		result.setSenha(txpSenha.getVlrString());
		return result;
	}

/*	public String[] getStrVals() {
		String[] ret = new String[3];
		ret[0] = txtUsuario.getText().trim().toLowerCase();
		ret[1] = txpSenha.getVlrString();
		return ret;
	}
*/
	public int getFilial() {
		if (cbEmp.getVlrInteger().intValue() == 0)
			return 1;
		return cbEmp.getVlrInteger().intValue();
	}

	public String getNomeFilial() {
		String retorno = ( String ) cbEmp.getItemAt(cbEmp.getSelectedIndex());
		if (retorno == null) {
			retorno = "DEMONSTRAÇÃO";
		}
		return retorno;
	}

	public int getFilialMz() {
		return iFilialMz;
	}

	public int getFilialPad() {
		return iFilialPadrao;
	}

	public abstract DbConnection getConection() throws Exception;

	protected abstract boolean execConexao(String sUsu, String sConexao);

	protected abstract boolean montaCombo(String sUsu);

	protected abstract boolean adicConFilial(final DbConnection conX);

	public void focusLost(FocusEvent fevt) {
	}

	public void focusGained(FocusEvent fevt) {

		if (fevt.getSource() == txtUsuario)
			lbInstrucoes.setText("Digite sua identificação de usuário!");
		else if (fevt.getSource() == txpSenha)
			lbInstrucoes.setText("Digite sua senha!");
		else if (fevt.getSource() == cbEmp) {
			if (!sUsuAnt.equals(txtUsuario.getVlrString().trim().toLowerCase()))
				btOK.requestFocus();
			else
				lbInstrucoes.setText("Selecione a filial!");
		}
		else if (fevt.getSource() == btOK) {
			if (!sUsuAnt.equals(txtUsuario.getVlrString().trim().toLowerCase())) {
				lbInstrucoes.setText("Pressione espaço p/ conectar ao banco de dados!");
				if (tries == 0) {
					btOK.doClick();
					tries++;
				}
			}
			else
				lbInstrucoes.setText("Pressione espaço p/ entrar no sistema!");
		}

	}

	public void actionPerformed(ActionEvent evt) {

		String sUsu = txtUsuario.getText().trim().toLowerCase();

		if (evt.getSource() == btOK) {
			if (sUsu.trim().equals("")) {
				Funcoes.mensagemInforma(this, "Usuario em branco!");
				txtUsuario.requestFocus();
				return;
			}
			else if (txpSenha.getVlrString().trim().equals("")) {
				Funcoes.mensagemInforma(this, "Senha em branco!");
				txpSenha.requestFocus();
				return;
			}

			if (sUsu.equals("sysdba"))
				bAdmin = true;

			if (!sUsuAnt.equals(sUsu)) {
				if (!execConexao(sUsu, txpSenha.getVlrString().trim()))
					return;
				montaCombo(sUsu);
				cbEmp.requestFocus();
				if (cbEmp.getItemCount() == 1)
					btOK.doClick();
				return;
			}
			else if (( cbEmp.getVlrInteger().intValue() == 0 ) && ( !bAdmin )) {
				if (sUsuAnt.equals(sUsu)) {
					Funcoes.mensagemInforma(this, "Filial não foi selecionada!");
					cbEmp.requestFocus();
					return;
				}
			}
			// Setar a filial independentemente do usuário
			// Incluído para corrigir problemas nas procedures
			iFilialPadrao = cbEmp.getVlrInteger();
		}
		super.actionPerformed(evt);

	}

	public static DbConnection getConexao(final String strBanco, final String strDriver, final String sUsu, final String sSenha) {
		DbConnection cRetorno = null;
		// Properties params = new Properties();
		/*
		 * try { Class.forName(strDriver); } catch
		 * (java.lang.ClassNotFoundException e) {
		 * System.out.println("Driver nao foi encontrado:\n"
		 * +strDriver+"\n"+e.getMessage ()); }
		 */

		try {
			// params.put("user", sUsu);
			// params.put("password", sSenha);
			// cRetorno = DriverManager.getDbConnection(strBanco, params);
			cRetorno = new DbConnection(strDriver, strBanco, sUsu, sSenha);
			cRetorno.setAutoCommit(false);
		}
		catch (java.sql.SQLException e) {
			if (e.getErrorCode() == 335544472)
				System.out.println("Nome do usuário ou senha inválidos ! ! !");
			else
				System.out.println("Não foi possível estabelecer conexão com o banco de dados.\n" + e.getMessage());
			e.printStackTrace();
			return cRetorno;
		}
		return cRetorno;
	}
}
