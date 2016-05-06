/**
 * @version 05/06/2000 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.telas <BR>
 * Classe:
 * @(#)Aplicativo.java <BR>
 * 
 * Este programa é licenciado de acordo com a LPG-PC (Licença Pública Geral para
 * Programas de Computador), <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * A LPG-PC deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste
 * Programa. <BR>
 * Caso uma cópia da LPG-PC não esteja disponível junto com este Programa, você
 * pode contatar <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa é
 * preciso estar <BR>
 * de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * Comentários da classe.....
 */

package org.freedom.library.swing.frame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

import org.freedom.bmps.Icone;
import org.freedom.infra.beans.Usuario;
import org.freedom.infra.functions.SystemFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.object.EmailBean;
import org.freedom.library.business.object.Empresa;
import org.freedom.library.business.object.Tab;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JMenuItemPad;
import org.freedom.library.swing.component.JMenuPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.dialog.FDialogo;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.util.SwingParams;

public abstract class Aplicativo implements ActionListener, KeyListener {

	public final static int TP_OPCAO_MENU = 0;

	public final static int TP_OPCAO_ITEM = 1;

	public final static String FIREBIRD_15 = "15";

	public final static String FIREBIRD_25 = "25";

	public static int casasDec = 2;

	public static int casasDecFin = 2;

	public static String codmoeda = "";

	public static Boolean simples = false;

	public static int casasDecPre = 2;

	public DbConnection con = null; // Variavel de conexao com o banco de dados

	public DbConnection con_nfe = null; // Variavel de conexao com o banco
										// deconNFE
	// dados de nfe

	public static FPrincipal telaPrincipal = null;

	public static Component framePrinc = null;

	// public static String strUsuario = "";

	// public static String strSenha = "";

	// public static String strCodCCUsu = "";

	// public static String strAnoCCUsu = "";

	public static String strTemp = "";

	public static String strCharSetRel = "";

	public static String strOS = "";

	public static String strBrowser = "";

	public static String strSplash = "";

	public static String strLookAndFeel = "";

	public static String strTamanhoFonte = "";

	public static String strFbVersao = "";

	public static int iCodEmp = 0;

	public static int iCodFilial = 0;

	public static int iCodFilialParam = 0;

	public static String sNomeFilial = "";

	public static int iCodFilialMz = 0;

	public static int iCodFilialPad = 0;

	public static int iNumEst = 0;

	public static String strBanco = "";

	public static String strBancoNFE = "";

	public static String strDriver = "";

	public static String tefSend = "";

	public static String tefRequest = "";

	public static String tefFlags = "";

	public static Tab tbObjetos = null;

	public static ImageIcon imgIcone = null;

	public static Vector<String> vArqINI = null;

	public String[][][] sConfig = new String[0][0][0];

	public JPanelPad pinBotoes = new JPanelPad(30, 30);

	// public ReflectionPanel pinBotoes = new ReflectionPanel(30,30);

	public int iXPanel = 0;

	public static boolean bBuscaProdSimilar = false;

	public static boolean bBuscaCodProdGen = false;

	public static String sMultiAlmoxEmp = "N";

	protected static String sFiltro = "";

	protected boolean bCtrl = true;

	protected static boolean bAutoCommit = false;

	protected String sSplashImg = "";

	protected JButtonPad btAtualMenu = new JButtonPad(Icone.novo("btAtualMenu.png"));

	protected Vector<JMenuItem> vOpcoes = null;

	protected Vector<JButtonPad> vBotoes = null;

	protected int iCodSis = 0;

	protected int iCodModu = 0;

	protected String sDescSis = "";

	protected String sDescModu = "";

	protected DbConnection conIB;

	private static Vector<String> vEquipeSis = new Vector<String>();

	private static String nomesis = "";

	public static String nomemodulo = "";

	private static String mantenedor = "";

	public static String sArqIni = "";

	private static String sArqINI = "";

	private static String emailsuporte = "";

	public static Empresa empresa = null;

	public static boolean bModoDemo = true;

	protected Class<? extends Login> cLoginExec = null;

	public static boolean bSuporte = true;

	private static EmailBean emailbean = null;

	private static Aplicativo instance = null;

	private String httpproxy;

	private String portaproxy;

	private String usuarioproxy;

	private String senhaproxy;

	private Boolean autproxy = false;

	private static Usuario usuario;

	public Boolean isAutproxy() {
		return autproxy;
	}

	public void isAutproxy(Boolean autproxy) {
		this.autproxy = autproxy;
	}

	public String getHttpproxy() {
		return httpproxy;
	}

	public void setHttpproxy(String httpproxy) {
		this.httpproxy = httpproxy;

		System.setProperty("http.proxySet", "true");
		System.setProperty("http.proxyHost", httpproxy);

	}

	public String getPortaproxy() {
		return portaproxy;
	}

	public void setPortaproxy(String portaproxy) {
		this.portaproxy = portaproxy;
		System.setProperty("http.proxyPort", portaproxy);
	}

	public String getUsuarioproxy() {
		return usuarioproxy;
	}

	public void setUsuarioproxy(String usuarioproxy) {
		this.usuarioproxy = usuarioproxy;
	}

	public String getSenhaproxy() {
		return senhaproxy;
	}

	public void setSenhaproxy(String senhaproxy) {
		this.senhaproxy = senhaproxy;
	}

	public PopupMenu pm = new PopupMenu();

	public Aplicativo() {

		Locale.setDefault(new Locale("pt", "BR"));

		instance = this;

		setEquipeSis();

		setEmailSuporte("suporte@stpinf.com");
		setNomeSis("Freedom-ERP");
		setMantenedor("Setpoint Informática Ltda.");

	}

	public static Vector<String> getEquipeSis() {
		return vEquipeSis;
	}

	protected void setEquipeSis() {

		vEquipeSis.add("Robson Sanchez: Direção/Analise");
		vEquipeSis.add("Anderson Sanchez: Direção/Desenvolvimento");
		vEquipeSis.add("Luiz Frederico: Supervisão/Suporte");
		vEquipeSis.add("Heraldo Luciano: Consultoria/Testes/Documentação");
		vEquipeSis.add("Sergio Murilo: Suporte/Testes");
		vEquipeSis.add("Alan Alexandre Oliveira: Desenvolvimento/Consultoria");
		vEquipeSis.add("Fernando Oliveira: Desenvolvimento");
		vEquipeSis.add("Moyzes Braz: Arte gráfica");
		vEquipeSis.add("Franchelle Gomes: Suporte/Testes");
		vEquipeSis.add("Bruno Nascimento: Desenvolvimento/Suporte");
		vEquipeSis.add("Fabiano Frizzo: Desenvolvimento");
		vEquipeSis.add("Vinícius Cintra Domingos: Colaborador/Desenvolvimento");
		vEquipeSis.add("Sergio Diogo Toews: Designer/Web Designer");

	}

	protected void setEmailSuporte(String emailsuporte) {
		Aplicativo.emailsuporte = emailsuporte;
	}

	protected void setNomeSis(String nomesis) {
		Aplicativo.nomesis = nomesis;
	}

	protected void setMantenedor(String mantenedor) {
		Aplicativo.mantenedor = mantenedor;
	}

	public static String getNomeSis() {
		return nomesis;
	}

	public static String getEmailSuporte() {
		return emailsuporte;
	}

	public static String getMantenedor() {
		return mantenedor;
	}

	public static Aplicativo getInstace() {
		return instance;
	}

	public DbConnection getConIB() {

		return conIB;
	}

	public DbConnection getConexao() {
		return con;
	}

	public DbConnection getConexaoNFE() {
		return con_nfe;
	}

	public static void setLookAndFeel(String sNomeArqIni) {

		if (sNomeArqIni == null)
			sNomeArqIni = "freedom.ini";
		sArqIni = sNomeArqIni;
		sArqINI = System.getProperty("ARQINI") != null ? System.getProperty("ARQINI") : sNomeArqIni;
		vArqINI = SystemFunctions.getIniFile(sArqINI);

		try {
			strLookAndFeel = getParameter("lookandfeel");
			strTamanhoFonte = getParameter("tamanhofonte");

			if (!strLookAndFeel.equals("")) {
				UIManager.setLookAndFeel(strLookAndFeel);
			} else {

				if (!"".equals(strTamanhoFonte)) {
					SwingParams.TAMANHO_FONTE = Integer.parseInt(strTamanhoFonte);
				}

				UIManager.put("InternalFrame.titleFont", SwingParams.getFontbold());
				UIManager.put("ToolTip.font", SwingParams.getFontitalicmed());
				UIManager.put("Label.font", SwingParams.getFontbold());
				UIManager.put("Button.font", SwingParams.getFontbold());
				UIManager.put("TextField.font", SwingParams.getFontpad());
				UIManager.put("Spinner.font", SwingParams.getFontpad());
				UIManager.put("CheckBox.font", SwingParams.getFontbold());
				UIManager.put("Menu.font", SwingParams.getFontbold());
				UIManager.put("TitledBorder.font", SwingParams.getFontbold());
				UIManager.put("ComboBox.font", SwingParams.getFontpad());
				UIManager.put("TabbedPane.font", SwingParams.getFontbold());
				UIManager.put("MenuItem.font", SwingParams.getFontbold());
				UIManager.put("PasswordField.font", SwingParams.getFontboldmax());
				UIManager.put("PasswordField.foreground", Color.RED);
				UIManager.put("RadioButton.font", SwingParams.getFontbold());
				UIManager.put("TextArea.font", SwingParams.getFontpad());
				UIManager.put("TableHeader.font", SwingParams.getFontboldmed());
				UIManager.put("Table.font", SwingParams.getFontpadmed());

			}

		} catch (Exception err) {
			err.printStackTrace();
		}

	}

	public abstract void setaSysdba();

	public abstract void setaInfoTela();

	public JMenuItem getOpcao(int iOpcao) {

		JMenuItem miRetorno = null;
		JMenuItem miTemp = null;
		int iCodMenu = -1;
		try {
			for (int i = 0; i < vOpcoes.size(); i++) {
				miTemp = vOpcoes.elementAt(i);
				if (miTemp != null) {
					if (miTemp instanceof JMenuPad)
						iCodMenu = ((JMenuPad) miTemp).getCodMenu();
					else if (miTemp instanceof JMenuItemPad)
						iCodMenu = ((JMenuItemPad) miTemp).getCodItem();
					if (iCodMenu == iOpcao) {
						miRetorno = miTemp;
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return miRetorno;
	}

	protected void ligaLog(String sArq) {

		File fArq = new File(sArq);
		try {
			if (!fArq.exists())
				fArq.createNewFile();
			FileOutputStream foArq = new FileOutputStream(fArq, true);
			System.setErr(new PrintStream(foArq));
		} catch (Exception err) {
			err.printStackTrace();
		}
	}

	public void addOpcao(int iSuperMenu, int iTipo, String sCaption, String titulo, char cAtalho, int iOpcao,
			int iNivel, boolean bExec, Class<? extends IFilho> tela) {

		JMenuItem mOpcao = null;
		JMenuPad mpMaster = null;
		try {
			if (iTipo == TP_OPCAO_MENU) {
				mOpcao = (new JMenuPad(iCodSis, iCodModu, iOpcao, iNivel));
			} else if (iTipo == TP_OPCAO_ITEM) {
				mOpcao = (new JMenuItemPad(iCodSis, iCodModu, iOpcao, iNivel, tela, titulo));
			}
			mOpcao.setText(sCaption);
			mOpcao.setMnemonic(cAtalho);

			if (bExec)
				mOpcao.addActionListener(this);
			if (iSuperMenu == -1) {
				telaPrincipal.adicMenu((JMenuPad) mOpcao);
			} else {
				mpMaster = (JMenuPad) getOpcao(iSuperMenu);
				if (mpMaster != null) {
					if (bExec)
						((JMenuItemPad) mOpcao).setEnabled(verifAcesso(iCodSis, iCodModu, iOpcao));
					mpMaster.add(mOpcao);
				}
			}
			vOpcoes.addElement(mOpcao);
		} catch (Exception e) {
			Funcoes.mensagemInforma(null, e.getMessage());
			e.printStackTrace();
		}
	}

	public void addSeparador(int iSuperMenu) {

		Object oSuper = null;
		try {
			try {
				oSuper = getOpcao(iSuperMenu);
				if (oSuper != null) {
					if (oSuper instanceof JMenu) {
						((JMenu) oSuper).addSeparator();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			oSuper = null;
		}

	}

	public JButtonPad addBotao(String sImagem, String sToolTip, String titulo, int iCodMenu,
			Class<? extends IFilho> tela) {

		JButtonPad btOpcao = null;
		try {

			btOpcao = new JButtonPad(iCodSis, iCodModu, iCodMenu, tela, titulo, false);

			btOpcao.setIcon(Icone.novo(sImagem));

			btOpcao.setContentAreaFilled(false);
			btOpcao.setBorderPainted(false);

			if (sToolTip != null) {
				btOpcao.setToolTipText(sToolTip);

			}
			vBotoes.add(btOpcao);
			adicTelaBotao(btOpcao);
			return btOpcao;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void show() {

		telaPrincipal.setVisible(true);

	}

	public void adicTelaBotao(JButtonPad bt) {

		pinBotoes.setBorder(null);

		bt.setEnabled(verifAcesso(bt.getCodSistema(), bt.getCodModulo(), bt.getCodItem()));

		pinBotoes.adic(bt, iXPanel, 0, 30, 30);

		bt.addActionListener(this);
		iXPanel += 30;

	}

	protected abstract void buscaInfoUsuAtual();

	public abstract boolean verifAcesso(int iCodSisP, int iCodModuP, int iCodMenuP);

	public void adicTelaMenu(JMenuPad menu, JMenuItemPad item) {

		item.setEnabled(verifAcesso(item.getCodSistema(), item.getCodModulo(), item.getCodItem()));
		menu.add(item);
		item.addActionListener(this);
	}

	public void actionPerformed(ActionEvent evt) {
		Object oTemp = evt.getSource();
		int iCodMenu = -1;
		if (oTemp != null) {
			if (oTemp instanceof JButtonPad) {
				if (((JButtonPad) oTemp) == btAtualMenu) {
					atualizaMenus();
				} else {
					iCodMenu = ((JButtonPad) oTemp).getCodItem();
				}
			} else if (oTemp instanceof JMenuItemPad && !(((JMenuItem) oTemp).getText().equals("Sair"))
					&& !(((JMenuItem) oTemp).getText().equals("Sobre"))
					&& !(((JMenuItem) oTemp).getText().equals("Atalhos"))
					&& !(((JMenuItem) oTemp).getText().equals("Suporte"))) {
				iCodMenu = ((JMenuItemPad) oTemp).getCodItem();
			} else if (oTemp instanceof JMenuPad && !(((JMenuItem) oTemp).getText().equals("Sobre"))) {
				iCodMenu = ((JMenuPad) oTemp).getCodMenu();
			} else if (oTemp instanceof JMenuItemPad) {
				if (((JMenuItem) oTemp).getText().equals("Sair")) {
					telaPrincipal.fecharJanela();
				} else if (((JMenuItem) oTemp).getText().equals("Sobre")) {
					FSobre tela = new FSobre();
					tela.setVisible(true);
					tela.dispose();
				} else if (((JMenuItem) oTemp).getText().equals("Atalhos")) {
					FAtalhos tela = new FAtalhos();
					tela.setVisible(true);
					tela.dispose();
				} else if (((JMenuItem) oTemp).getText().equals("Suporte")) {
					FSuporte tela = new FSuporte();
					tela.setConexao(con);
					tela.setVisible(true);
					tela.dispose();
				}
			}
			if (iCodMenu != -1) {
				Class<? extends IFilho> telaClass = null;
				String titulo = "";
				if (oTemp instanceof JMenuItemPad) {
					telaClass = ((JMenuItemPad) oTemp).getTela();
					if (telaClass != null) {
						titulo = ((JMenuItemPad) oTemp).getTitulo();
					}
				} else if (oTemp instanceof JButtonPad) {
					telaClass = ((JButtonPad) oTemp).getTela();
					if (telaClass != null) {
						titulo = ((JButtonPad) oTemp).getTitulo();
					}
				}
				if (telaClass != null) {

					abreTela(titulo, telaClass);

				}

			}
		}

	}

	public Object abreTela(String titulo, Class<? extends IFilho> telaClass) {
		String name = telaClass.getName();
		Object obj = null;
		Object result = null;
		if (!telaPrincipal.temTela(name)) {
			try {
				obj = telaClass.newInstance();
				if (obj instanceof FFDialogo) {
					FFDialogo tela = (FFDialogo) obj;

					Class<?> partypes[] = new Class[2];
					partypes[0] = DbConnection.class;
					partypes[1] = DbConnection.class;
					Method meth = null;
					try {
						meth = telaClass.getMethod("setConexao", partypes);
					} catch (NoSuchMethodException e) {
					}

					telaPrincipal.criatela(titulo, tela, con);
					tela.setTelaPrim(telaPrincipal);

					if (meth != null) {
						Object arglist[] = new Object[2];
						arglist[0] = con;
						arglist[1] = conIB;
						meth.invoke(obj, arglist);

					}
					result = tela;
				} else if (obj instanceof FFilho) {
					FFilho tela = (FFilho) obj;

					Class<?> partypes[] = new Class[2];
					partypes[0] = DbConnection.class;
					partypes[1] = DbConnection.class;
					Method meth = null;
					try {
						meth = telaClass.getMethod("setConexao", partypes);
					} catch (NoSuchMethodException e) {
					}

					telaPrincipal.criatela(titulo, tela, con);
					tela.setTelaPrim(telaPrincipal);

					if (meth != null) {
						Object arglist[] = new Object[2];
						arglist[0] = con;
						arglist[1] = conIB;
						meth.invoke(obj, arglist);
					}
					result = tela;
				} else if (obj instanceof FDialogo) {
					FDialogo tela = (FDialogo) obj;

					Class<?> partypes[] = new Class[2];
					partypes[0] = DbConnection.class;
					partypes[1] = DbConnection.class;
					Method meth = null;
					try {
						meth = telaClass.getMethod("setConexao", partypes);
					} catch (NoSuchMethodException e) {
					}

					telaPrincipal.criatela(titulo, tela, con);

					if (meth != null) {
						Object arglist[] = new Object[2];
						arglist[0] = con;
						arglist[1] = conIB;
						meth.invoke(obj, arglist);
					}
					result = tela;
				} else {
					Funcoes.mensagemInforma(framePrinc,
							"Tela construída com " + telaClass.getName() + "\n Não pode ser inciada.");
				}
				obj = null;
			} catch (NullPointerException err) {

				StackTraceElement ste = err.getStackTrace()[0];

				int linha = ste.getLineNumber();
				String classe = ste.getClassName();
				String metodo = ste.getMethodName();

				Funcoes.mensagemErro(framePrinc, "Erro de ponteiro nulo ao abrir a tela!\n" + "Classe:" + classe + "\n"
						+ "Metodo:" + metodo + "\n" + "Linha:" + linha, true, con, err);

				err.printStackTrace();
			} catch (Exception err) {
				Funcoes.mensagemErro(framePrinc, err.getMessage(), true, con, err);

				err.printStackTrace();
			}
		}
		return result;
	}

	public void atualizaMenus() {

		JMenuBar menuBar = telaPrincipal.bar;

		try {

			for (int i = 0; i < menuBar.getMenuCount(); i++) {

				if (!upMenuDB(menuBar.getMenu(i), new JMenuPad())) {
					break;
				}

				buscaMenuItem(menuBar.getMenu(i));

			}
		} catch (Exception e) {
			e.printStackTrace();
			Funcoes.mensagemErro(null, "Erro ao atualizar menus!\n" + e.getMessage());
		}

		Funcoes.mensagemInforma(null, "Menus atualizados com sucesso!");

	}

	private void buscaMenuItem(JMenu men) {

		for (int i = 0; i < men.getItemCount(); i++) {
			JMenuItem it = men.getItem(i);
			if (it instanceof JMenuPad) {
				if (!upMenuDB(it, (JMenuPad) men))
					break;
				buscaMenuItem((JMenu) it);
			} else if (it instanceof JMenuItemPad) {
				if (!upMenuDB((JMenuItemPad) it, (JMenuPad) men))
					break;
			}
		}
	}

	private boolean upMenuDB(JMenuItem men, JMenuPad menPai) {

		boolean bRet = false;
		Class<? extends IFilho> tela = null;
		String sNomeMenu = null;
		String sAcaoMenu = null;
		int iCodMenu = 0;

		try {
			if (men instanceof JMenuItemPad) {
				iCodMenu = ((JMenuItemPad) men).getCodItem();
				tela = ((JMenuItemPad) men).getTela();
			} else if (men instanceof JMenuPad) {
				iCodMenu = ((JMenuPad) men).getCodMenu();
			}
			if (tela != null) {
				sNomeMenu = tela.getName();
				sAcaoMenu = tela.getName();
			} else {
				sNomeMenu = "" + iCodMenu;
				sAcaoMenu = "" + iCodMenu;
			}
			if (iCodMenu != 0) {
				PreparedStatement ps = con.prepareStatement("EXECUTE PROCEDURE SGUPMENUSP01(?,?,?,?,?,?,?,?,?,?,?)");
				ps.setInt(1, this.iCodSis);
				ps.setString(2, this.sDescSis);
				ps.setInt(3, this.iCodModu);
				ps.setString(4, Funcoes.copy(this.sDescModu, 50));
				ps.setInt(5, iCodMenu);
				ps.setString(6, men.getText());
				ps.setString(7, sNomeMenu);
				ps.setString(8, sAcaoMenu);

				if (menPai.getCodMenu() == 0) {
					ps.setNull(9, java.sql.Types.INTEGER);
					ps.setNull(10, java.sql.Types.INTEGER);
					ps.setNull(11, java.sql.Types.INTEGER);
				} else {
					ps.setInt(9, menPai.getCodModulo());
					ps.setInt(10, menPai.getCodModulo());
					ps.setInt(11, menPai.getCodMenu());
				}
				ps.execute();
				ps.close();
				con.commit();
			}
			bRet = true;
		} catch (SQLException err) {
			Funcoes.mensagemInforma(telaPrincipal,
					"Não foi possível atualizar a base de menus!\n" + err + "\n" + this.iCodSis + "," + this.sDescSis
							+ "\n" + this.iCodModu + "," + this.sDescModu + "\n" + "acao: " + sAcaoMenu + "\n" + "nome:"
							+ sNomeMenu + "\n" + iCodMenu + "," + men.getText() + "\n" + "," + menPai.getCodMenu());
		} finally {
			tela = null;
			sNomeMenu = null;
			sAcaoMenu = null;
			iCodMenu = 0;
		}
		return bRet;
	}

	public void adicTelaMenu(JButtonPad bt) {

		iXPanel += 30;
		bt.setEnabled(verifAcesso(bt.getCodSistema(), bt.getCodModulo(), bt.getCodItem()));
		pinBotoes.adic(bt, iXPanel, 0, 30, 30);
		bt.addActionListener(this);
	}

	public void ajustaMenu() {

		pinBotoes.setPreferredSize(new Dimension(iXPanel + 4, 30));
		Object oMenu = getOpcao(100000000);
		JMenuItem miSair = null;
		if (oMenu != null) {
			if (oMenu instanceof JMenuPad) {
				miSair = new JMenuItemPad("Sair", 'r');
				miSair.addActionListener(this);
				((JMenuPad) oMenu).addSeparator();
				((JMenuPad) oMenu).add(miSair);
			}
		}
		JMenuPad mAjuda = new JMenuPad("Ajuda");
		JMenuItem miSobre = new JMenuItemPad("Sobre");

		miSobre.addActionListener(this);

		mAjuda.add(miSobre);
		JMenuItem miAtalhos = new JMenuItemPad("Atalhos");
		miAtalhos.addActionListener(this);
		mAjuda.add(miAtalhos);

		if (bSuporte) {
			mAjuda.addSeparator();
			JMenuItem miSuporte = new JMenuItemPad("Suporte");
			miSuporte.addActionListener(this);
			mAjuda.add(miSuporte);
		}

		telaPrincipal.bar.add(mAjuda);

	}

	public abstract void iniConexao();

	public void setSplashName(String sImg) {

		sSplashImg = sImg;
	}

	public static boolean getAutoCommit() {

		return bAutoCommit;
	}

	public Object criaLogin() {

		Object retorno = null;
		try {
			retorno = cLoginExec.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}

	public DbConnection conexao() {

		DbConnection conRetorno = null;
		strBanco = getParameter("banco");
		strDriver = getParameter("driver");

		if (getUsuario() == null) {
			Login lgBanco = (Login) criaLogin();
			lgBanco.execLogin(strBanco, strDriver, sSplashImg, iNumEst);
			if (!lgBanco.OK)
				System.exit(0);
			setUsuario(lgBanco.getUsuario());
			iCodFilial = lgBanco.getFilial();
			sNomeFilial = lgBanco.getNomeFilial();
			iCodFilialMz = lgBanco.getFilialMz();
			iCodFilialPad = lgBanco.getFilialPad();

			try {
				conRetorno = lgBanco.getConection();
				con_nfe = lgBanco.conNFE;
			} catch (Exception e) {
				Funcoes.mensagemErro(null, "Erro de conexão!\n" + e.getMessage());
				e.printStackTrace();
			}
			lgBanco.dispose();
		}

		if (getUsuario() == null) {
			return null;
		} else if (getUsuario().getSenha() == null) {
			return null;
		}

		return conRetorno;

	}

	public abstract boolean getModoDemo();

	public abstract String getDescEst();

	public abstract void validaPrefere();

	public abstract void getMultiAlmox();

	public DbConnection conexaoIB(String strDriverP, String strBancoP) {
		try {
			conIB = new DbConnection(strDriverP, strBancoP, getUsuario().getIdusu(), getUsuario().getSenha());
		} catch (java.sql.SQLException e) {
			if (e.getErrorCode() == 335544472)
				return null;
			Funcoes.mensagemErro(null,
					"[internal]:Não foi possível estabelecer conexão com o banco de dados.\n" + e.getMessage());
			return null;
		}
		return conIB;

	}

	public String[][][] getConfig2() {

		Vector<Vector<Object>> vSessao = new Vector<Vector<Object>>();
		Vector<Object> vValSessao = new Vector<Object>();
		Vector<String> vValCampo = new Vector<String>();
		String sTmp = "";
		boolean bLeSessao = false;
		boolean bLeCampo = false;
		int iTam = 0;
		int i = 0;
		int iContaCampo = 0;
		int iMaxCampo = 0;
		char c = (char) 0;
		File fArq = null;
		FileReader frArq = null;
		try {
			fArq = new File(sArqIni);
			frArq = new FileReader(fArq);
			iTam = (int) fArq.length();
			try {
				while (i < iTam) {
					// Vector vValSessao = null;
					if (c != (char) 10)
						c = (char) frArq.read();
					// System.out.println("N. Char SES:"+(new
					// Character(c)).hashCode()+"\n");
					if ((!bLeSessao) & (!bLeCampo) & (c == '[')) {
						bLeSessao = true;
					} else if ((c == ']') & (bLeSessao)) {
						vValSessao = new Vector<Object>();
						bLeSessao = false;
						vValSessao.addElement(new String(sTmp));
						vSessao.addElement(vValSessao);
						// System.out.println("Sessao: "+sTmp);
						sTmp = "";
						iContaCampo = 0;
					} else if (bLeSessao) {
						sTmp += c;
					} else if (!bLeSessao) {
						bLeCampo = true;
						while (i < iTam) {
							c = (char) frArq.read();
							// System.out.println("N. Char CAM:"+(new
							// Character(c)).hashCode()+(bLeCampo ? " OK" : "
							// NO"));
							if ((c == (char) 10) & (bLeCampo)) {
								bLeCampo = false;
								vValCampo.addElement(new String(sTmp));
								vValSessao.addElement(vValCampo);
								// System.out.println("Valor: "+sTmp);
								sTmp = "";
								iContaCampo++;
								break;
							} else if ((c == '=') & (bLeCampo)) {
								vValCampo.addElement(sTmp);
								System.out.println("Campo: " + sTmp);
								sTmp = "";
							} else if (bLeCampo) {
								sTmp += c;
							}
							i++;
						}
						if (iContaCampo > iMaxCampo) {
							iMaxCampo = iContaCampo;
						}
					}
					i++;
				}
				frArq.close();

			} catch (IOException err) {
				Funcoes.mensagemErro(null, "Erro ao carregar arquivo de configuração!\n" + err.getMessage());
				System.exit(0);
			}

		} catch (FileNotFoundException err) {
			Funcoes.mensagemErro(null, "Erro ao carregar arquivo de configuração!\n" + err.getMessage());
			System.exit(0);
		}
		Funcoes.mensagemErro(null, "TESTE: " + vSessao.size() + " ~ " + iMaxCampo);
		String[][][] sRetorno = new String[vSessao.size()][iMaxCampo][2];
		for (int iS = 0; iS < (vSessao.size()); iS++) {
			sRetorno[iS][0][0] = (String) vSessao.elementAt(iS).elementAt(0);
			for (int iC = 1; iC < iMaxCampo; iC++) {
				sRetorno[iS][iC][0] = (String) ((Vector) vSessao.elementAt(iS).elementAt(iC)).elementAt(0);
				sRetorno[iS][iC][1] = (String) ((Vector) vSessao.elementAt(iS).elementAt(iC)).elementAt(1);
			}
		}
		return sRetorno;
	}

	public static String getValorSecao(String sSecao, String sParam) {

		return getValorSecao(sSecao, sParam, vArqINI);

	}

	public static String getValorSecao(String sSecao, String sParam, Vector<String> vArq) {

		String sLinha = "";
		String sLabel = "";
		int iLocal = 0;
		for (int i = 0; i < vArq.size(); i++) {
			sLinha = vArq.elementAt(i).trim();
			if (sLinha.indexOf(sSecao) > 0) {
				for (int i2 = i + 1; i2 < vArq.size(); i2++) {
					sLinha = vArq.elementAt(i2);
					if (sLinha.indexOf('[') > 0) {
						break;
					} else if (sLinha.indexOf('=') > 0) {
						iLocal = sLinha.indexOf('=');
						sLabel = sLinha.substring(0, iLocal).trim();
						if (sLabel.equals(sParam.trim())) {
							return sLinha.substring(iLocal + 1);
						}
					}
				}
			}
		}
		return "";
	}

	public static String getParameter(String sParam) {
		return getValorSecao("parametros", sParam);
	}

	public static String getParameter(String sParam, Vector<String> vArq) {

		return getValorSecao("parametros", sParam, vArq);
	}

	public static void killProg(int iTerm, String sMess) {

		Funcoes.mensagemErro(null, sMess);
		System.exit(iTerm);
	}

	protected abstract void carregaCasasDec();

	public void keyReleased(KeyEvent kevt) {

		if (kevt.getKeyCode() == KeyEvent.VK_CONTROL)
			bCtrl = false;
	}

	public void keyTyped(KeyEvent kevt) {

	}

	public static void setEmailBean(final EmailBean mail) {
		emailbean = mail;
	}

	public static EmailBean getEmailBean() {
		EmailBean clone = null;
		if (emailbean != null)
			clone = emailbean.getClone();
		return clone;
	}

	public abstract void createEmailBean();

	public abstract void updateEmailBean(EmailBean email);

	public static Usuario getUsuario() {
		return usuario;
	}

	public static void setUsuario(Usuario usuarioobj) {
		usuario = usuarioobj;
	}

}
