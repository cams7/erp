/**
 * @version 01/08/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva/Robson Sanchez/Anderson Sanchez
 *         <BR>
 * 
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.componentes <BR>
 * Classe:
 * @(#)ListaCampos.java <BR>
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
 * Classe de acesso direto a dados SQL.
 */

package org.freedom.library.persistence;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.text.JTextComponent;

import org.freedom.acao.CancelEvent;
import org.freedom.acao.CancelListener;
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
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.exceptions.ExceptionCarregaDados;
import org.freedom.library.business.exceptions.ExceptionCarregaItem;
import org.freedom.library.business.exceptions.ExceptionLimpaComponent;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.component.PainelImagem;
import org.freedom.library.swing.frame.Aplicativo;

public class ListaCampos extends Container implements PostListener,
InsertListener, EditListener, CancelListener, DeleteListener,
CarregaListener, MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ListaCampos em modo espera. Sem trasação para o momento.
	 */
	public static final int LCS_NONE = -1;

	/**
	 * ListaCampos em modo carregado. Sem trasação para o momento.
	 */
	public static final int LCS_SELECT = 0;

	/**
	 * ListaCampos em modo de edição. Trasação ativa, aguardando post ou
	 * cancela.
	 */
	public static final int LCS_EDIT = 1;

	/**
	 * ListaCampos em modo de inserção. Trasação ativa, aguardando post ou
	 * cancela.
	 */
	public static final int LCS_INSERT = 2;

	/**
	 * ListaCampos em modo carregado (veja detalhes). Sem trasação para o
	 * momento. Desabilitado as funcoes: post, edit, insert
	 */
	public static final int LCS_READ_ONLY = 3;

	/**
	 * ListaCampos em modo carregado (veja detalhes). Sem trasação para o
	 * momento. Habilitado as funcoes: post, edit, insert
	 */
	public static final int LCS_WRITE_READ = 4;

	/**
	 * Constante de erro do interbase quando a PK é duplicada.
	 */
	public static int FB_PK_DUPLA = 335544665;

	/**
	 * Constante de erro do interbase quando a integridade é quebrada.
	 */

	public static final int PRIMEIRO_REGISTRO = 0;

	public static final int ULTIMO_REGISTRO = 9;

	public static final int PROXIMO_REGISTRO = 1;

	public static final int ANTERIOR_REGISTRO = -1;

	public static int FB_FK_INVALIDA = 335544466;

	private int lcState = LCS_NONE;

	private int lcStateAnt = LCS_NONE;

	private DbConnection con = null;

	private PreparedStatement sqlMax = null;

	private PreparedStatement sqlLC = null;

	private PreparedStatement sqlItens = null;

	private ResultSet rsMax = null;

	private ResultSet rsLC = null;

	private ResultSet rsItens = null;

	private String sPK = "";

	private String sSQLTab = "";

	private boolean bAutoInc;

	private boolean bConfirmaDelecao = true;

	private boolean readOnly = false;

	private boolean bCancelPost = false;

	private boolean bCancelEdit = false;

	private boolean bCancelCarrega = false;

	private boolean bCancelInsert = false;

	private boolean bCancelDelete = false;

	private boolean bCancelCancel = false;

	private boolean muiltiselecaoF2 = false;

	private String sTabela;

	public String sSigla;

	private String sArea;

	private String sSQLSelect = "";

	private String sSQLMax = "";

	private String sSQLInsert = "";

	private String sSQLUpdate = "";

	private String sSQLDelete = "";

	private String sOrdem = "";

	private Vector<Object> vCache = new Vector<Object>();

	private Navegador nvLC = null;

	private PostListener posLis = this;

	private EditListener editLis = this;

	private InsertListener insLis = this;

	private DeleteListener delLis = this;

	private CancelListener canLis = this;

	private CarregaListener carLis = this;

	private ListaCampos lcMaster = null;

	// private ListaCampos lcM = null;

	private Vector<ListaCampos> vLcDetalhe = new Vector<ListaCampos>();

	private JTablePad tab = null;

	private int[] iTipos = new int[1];

	private String[] sMascs = new String[1];

	private boolean bMaster = false;

	private boolean bDetalhe = false;

	private int iNumDescs = 0;

	private Vector<JTextFieldPad> vDescFK = new Vector<JTextFieldPad>();

	private String sWhereAdic = "";

	private String sWhereAdicMax = "";

	private String sWhereAdicSubSel = "";

	private boolean[] bCamposCanc = null;

	private boolean bQueryCommit = true;

	private boolean bQueryInsert = true;

	private boolean bUsaME = true;

	private boolean bTiraFI = false;

	private boolean bAutoLimpaPK = true;

	private boolean bPodeIns = true;

	private boolean bPodeExc = true;

	private boolean bPodeCommit = true;

	public Vector<JTextComponent> vTxtValor = null;

	private JTextComponent txtValor = null;

	private int iCodEmp = Aplicativo.iCodEmp;

	private int iCodFilial = 0;

	private int iNumPKs = 0;

	private Component cOwner = null;

	private boolean mensInserir = true;

	public static final byte DB_PF = 3; // Não implementado

	public static final byte DB_FK = 2; // Foreign Key

	public static final byte DB_PK = 1; // Primary Key

	public static final byte DB_SI = -3; // Single

	private String sSepU = ""; // Separador para query de update

	private String sSepI = ""; // Separador para query de insert

	private String sSepT = ""; // Separador para query da tabela

	private String sSepM = ""; // Separador para query de max

	private String sSepD = ""; // Separador para query de delete

	private String sCampoSQL = "";

	private String sWhereT = ""; // Where para query da tabela

	private String sSepParam = "";

	private Integer iParamPost = new Integer(0);

	private int iParamMax = 0;

	private int iParamC = 0;

	private int iParamDelete = 0;

	private int iOrdemT = 1;

	private String sSchema = ""; // Schema/Owner da tabela

	private boolean validarcpf = true;

	private boolean canedit = true;

	private boolean loginstab = false;

	private Map<String, Integer> fieldsInsert = new LinkedHashMap<String, Integer>();

	private Map<String, Integer> fieldsUpdate = new LinkedHashMap<String, Integer>();

	// private boolean carregando = false;

	public int getCodEmp() {
		return iCodEmp;
	}

	/**
	 * Construtor da classe (nada em especial). <BR>
	 * Simplesmente carrega as heranças.
	 * 
	 */
	public ListaCampos(Component cOwner) {
		this.cOwner = cOwner;
	}

	public ListaCampos(Component cOwner, String sSig) {
		this.cOwner = cOwner;
		sSigla = sSig;
	}

	public void setNumPKs(int iNumPKs) {
		this.iNumPKs = iNumPKs;
	}

	public int getNumPKs() {
		return this.iNumPKs;
	}

	public void setReadOnly(boolean RO) {
		readOnly = RO;
		if (RO)
			setState(LCS_READ_ONLY);
		else {
			setState(LCS_WRITE_READ);
			setState(LCS_NONE);
		}
	}

	public boolean isValidarcpf() {
		return validarcpf;
	}

	public void setValidarcpf(boolean validacpf) {
		this.validarcpf = validacpf;
	}

	/**
	 * Retorna o estado atual do ListaCampos. <BR>
	 * Retorna o estado que o ListaCampos esta <BR>
	 * no momento, por exemplo: Se o ListaCampos <BR>
	 * ja realizou a query e ja distribuiu os <BR>
	 * resultados; o estado dele será LCS_SELECT. Para mais informações com as
	 * constantes, de uma <BR>
	 * olhada na ajuda delas.
	 * 
	 * @return LCS_NONE,LCS_INSERT,LCS_EDIT,LCS_SELECT. <BR>
	 *         Tipos especiais: <BR>
	 *         LCS_READ_ONLY,LCS_WRITE_READ
	 * @see #setState
	 * 
	 */
	public int getStatus() {
		return lcState;
	}

	/**
	 * Pega o status anterior.
	 * 
	 * @see #getStatus
	 * 
	 */
	public int getStatusAnt() {
		return lcStateAnt;
	}

	/**
	 * Retorno o owner do componente. <BR>
	 * O Owner de retorno dever ser um Frame <BR>
	 */
	public Component getOwnerTela() {
		Component cFrame = null;
		Component cRetorno = null;
		cFrame = this.getParent();
		if (cFrame != null) {
			for (@SuppressWarnings("unused")
			int i = 1; 1 <= 10; i++) {
				if ((cFrame instanceof JFrame)
						|| (cFrame instanceof JInternalFrame)
						|| (cFrame instanceof JDialog)) {
					cRetorno = cFrame;
					break;
				}
				cFrame = cFrame.getParent();
			}
		}
		return cRetorno;
	}

	/**
	 * Ajusta o estado atual do ListaCampos. <BR>
	 * Ajusta o estado que o ListaCampos esta <BR>
	 * no momento, por exemplo: Se o ListaCampos <BR>
	 * ja realizou a query e ja distribuiu os <BR>
	 * resultados; o estado deverá ser LCS_SELECT. Para mais informações com as
	 * constantes, de uma <BR>
	 * olhada na ajuda delas.
	 * 
	 * @param Estado
	 *            LCS_NONE,LCS_INSERT,LCS_EDIT,LCS_SELECT. <BR>
	 *            Tipos especiais: <BR>
	 *            LCS_READ_ONLY,LCS_WRITE_READ
	 * @see #getStatus
	 * 
	 */
	public boolean setState(int iLc) {
		if (!(lcState == LCS_READ_ONLY) || iLc == LCS_WRITE_READ) {
			lcStateAnt = lcState;
			lcState = iLc;
		}
		if (nvLC != null) {
			if (lcState == LCS_NONE) {
				nvLC.visivel("SAVE", false); // Salvar
				nvLC.visivel("CANCEL", false); // Cancelar
				nvLC.visivel("DELETE", bPodeExc); // Excluir
				nvLC.visivel("NEW", bPodeIns); // Novo
				nvLC.visivel("EDIT", true); // Editar
			} else if (lcState == LCS_SELECT) {
				nvLC.visivel("SAVE", false); // Salvar
				nvLC.visivel("CANCEL", false); // Cancelar
				nvLC.visivel("DELETE", bPodeExc); // Excluir
				nvLC.visivel("NEW", bPodeIns); // Novo
				nvLC.visivel("EDIT", true); // Editar
			} else if (lcState == LCS_INSERT) {
				nvLC.visivel("SAVE", true); // Salvar
				nvLC.visivel("CANCEL", true); // Cancelar
				nvLC.visivel("DELETE", false); // Excluir
				nvLC.visivel("NEW", false); // Novo
				nvLC.visivel("EDIT", false); // Editar
			} else if (lcState == LCS_EDIT) {
				nvLC.visivel("SAVE", true); // Salvar
				nvLC.visivel("CANCEL", true); // Cancelar
				nvLC.visivel("DELETE", bPodeExc); // Excluir
				nvLC.visivel("NEW", bPodeIns); // Novo
				nvLC.visivel("EDIT", false); // Editar
			} else if (lcState == LCS_READ_ONLY) {
				if (nvLC != null) {
					if (nvLC.bDet) {
						nvLC.setAtivo(4, false); // Novo
						nvLC.setAtivo(5, false); // Excluir
						nvLC.setAtivo(6, false); // Editar
						nvLC.setAtivo(7, false); // Salvar
						nvLC.setAtivo(8, false); // Cancelar
					} else {
						nvLC.setAtivo(0, false); // Novo
						nvLC.setAtivo(1, false); // Excluir
						nvLC.setAtivo(2, false); // Editar
						nvLC.setAtivo(3, false); // Salvar
						nvLC.setAtivo(4, false); // Cancelar
					}
				}
			} else if (lcState == LCS_WRITE_READ) {
				if (nvLC != null) {
					if (nvLC.bDet) {
						nvLC.setAtivo(4, bPodeIns); // Novo
						nvLC.setAtivo(5, bPodeExc); // Excluir
						nvLC.setAtivo(6, true); // Editar
						nvLC.setAtivo(7, true); // Salvar
						nvLC.setAtivo(8, true); // Cancelar
					} else {
						nvLC.setAtivo(0, bPodeIns); // Novo
						nvLC.setAtivo(1, bPodeExc); // Excluir
						nvLC.setAtivo(2, true); // Editar
						nvLC.setAtivo(3, true); // Salvar
						nvLC.setAtivo(4, true); // Cancelar
					}
				}
			}
		}
		return true;
	}

	/**
	 * Ajusta a tabela para este ListaCampos. <BR>
	 * Ajusta a tabela para um ListaCampos Master.
	 * 
	 * @param JTablePad
	 *            A tabela que desaja adicionar
	 * @see #getTab
	 * 
	 */
	public void setTabela(JTablePad tb) {
		tab = tb;
		tab.addMouseListener(this);
	}

	public void setMensInserir(boolean mensInserir) {
		this.mensInserir = mensInserir;
	}

	public boolean getMensInserir() {
		return this.mensInserir;
	}

	public void adicDetalhe(ListaCampos lc) {
		if (lc != null) {
			vLcDetalhe.addElement(lc);
			bMaster = true;
		}
	}

	public void setMaster(ListaCampos lc) {
		lcMaster = lc;
		bDetalhe = true;
	}

	public ListaCampos getDetalhe(int ind) {
		return vLcDetalhe.elementAt(ind);
	}

	/**
	 * Retorna se o ListaCampos é Master. <BR>
	 * Retorna se o ListaCampos é Master, para <BR>
	 * entender isso é necessário ter um conhecimento <BR>
	 * sobre o conceiro Master-Detail, onde Master é <BR>
	 * a tebela mãe ex: Pedido e o Detail é a tabela <BR>
	 * filho ex: ITPedido.
	 * 
	 * @return Se verdeiro a tabela é Master <BR>
	 *         se false ela não é.<BR>
	 *         Atencao!! quando o retorno for false ela não é <BR>
	 *         necessáriamente Detail.
	 * 
	 */
	public ListaCampos getMaster() {
		return lcMaster;
	}

	/**
	 * Retorna tabela deste ListaCampos. <BR>
	 * Retorna a tabela de um ListaCampos Master.
	 * 
	 * @return Tabela que pertencente a este ListaCampos Master
	 * @see #setTabela
	 * 
	 */
	public JTablePad getTab() {
		return tab;
	}

	/**
	 * Retorna sigla deste ListaCampos. <BR>
	 * Retorna a sigla da tabela do listacampos. <BR>
	 * Essa função é muito usada para definir o <BR>
	 * campo de codemp para FKs.
	 * 
	 * @return Uma string com 2 caracteres que representam a sigla da tabela.
	 * @see #ListaCampos
	 * 
	 */
	public String getSigla() {
		if (sSigla != null) {
			return sSigla.toLowerCase();
		}
		return sSigla;
	}

	/**
	 * Retorna area deste ListaCampos. <BR>
	 * Retorna a area da tabela do listacampos. <BR>
	 * Essa função eh muito usada para montar os <BR>
	 * nomes das tabelas. <BR>
	 * 
	 * @return Uma string com 2 caracteres que representam a area da tabela.
	 * @see #montaSql
	 * 
	 */
	public String getArea() {
		return sArea;
	}

	/**
	 * Ajusta o ListaCampos para limpar a PK. <BR>
	 * Ajusta o ListaCampos para limpar a PK caso <BR>
	 * não retorne linhas de dados do carregaDados.
	 * 
	 * @param AutoLimpaPK
	 *            Se true limpa a PK.
	 * @see #getAutoLimpaPK #carregaDados
	 * 
	 */

	public void setAutoLimpaPK(boolean bVal) {
		bAutoLimpaPK = bVal;
	}

	/**
	 * Ajusta o ListaCampos para permitir ou não a inserção. <BR>
	 * 
	 * @param bVal
	 *            - Se false não será possível criar novo registro.
	 * @see #getPodeIns
	 * 
	 */

	public void setPodeIns(boolean bVal) {
		bPodeIns = bVal;
		setState(lcState);
	}

	/**
	 * Retorna se o ListaCampos permite inserção. <BR>
	 * 
	 * @return bPodeIns - Se false não será possível criar novo registro.
	 * @see #setPodeIns
	 * 
	 */

	public boolean getPodeIns() {
		return bPodeIns;
	}

	/**
	 * Ajusta o ListaCampos para permitir ou não exclusão. <BR>
	 * 
	 * @param bVal
	 *            - Se false não será possível excluir registro.
	 * @see #getPodeExc
	 * 
	 */

	public void setPodeExc(boolean bVal) {
		bPodeExc = bVal;
		setState(lcState);
	}

	/**
	 * Retorna se o ListaCampos permite exclusão. <BR>
	 * 
	 * @return bPodeExc - Se false não será possível excluir registro.
	 * @see #setPodeExc
	 * 
	 */

	public boolean getPodeExc() {
		return bPodeExc;
	}

	/**
	 * Ajusta o ListaCampos para restrições de Commit no SELECT. <BR>
	 * Ajusta o ListaCampos para realizar ou não commit <BR>
	 * após a query.
	 * 
	 * @param Realizar
	 *            Se true realiza commit se false não realiza.
	 * @see #getQueryCommit
	 * 
	 */

	public void setQueryCommit(boolean bVal) {
		bQueryCommit = bVal;
	}

	/**
	 * Ajusta o ListaCampos para restrições de Commit. <BR>
	 * Ajusta o ListaCampos para realizar ou não commit <BR>
	 * após qualque SQL.
	 * 
	 * @param Realizar
	 *            Se true realiza commit se false não realiza.
	 * @see #getPodeCommit
	 * 
	 */

	public void setPodeCommit(boolean bVal) {
		bPodeCommit = bVal;
	}

	/**
	 * Retorna se o listaCampos vai limpar a PK. <BR>
	 * Retorna se o lisaCampos vai limpar a PK caso <BR>
	 * a SELECT não retorne nenhuma linha de dados <BR>
	 * no carregaDados.
	 * 
	 * @return AutoLimpaPK Se true limpa a PK.
	 * @see #setAutoLimpaPK #carregaDados
	 * 
	 */

	public boolean getAutoLimpaPK() {
		return bAutoLimpaPK;
	}

	/**
	 * Retorna true se o ListaCampos esta ajustado para QueryCommit. <BR>
	 * 
	 * @return QueryCommit.
	 * @see #setQueryCommit
	 * 
	 */
	public boolean getQueryCommit() {
		return bQueryCommit;
	}

	/**
	 * Retorna true se o ListaCampos esta ajustado para executar Commit. <BR>
	 * 
	 * @return PodeCommit.
	 * @see #setPodeCommit
	 * 
	 */
	public boolean getPodeCommit() {
		return bPodeCommit;
	}

	/**
	 * Ajusta o ListaCampos para restrições de Multi Empresa. <BR>
	 * Ajusta o ListaCampos para não realizar tratamento por <BR>
	 * código de empresa e filial.
	 * 
	 * @param Se
	 *            false não realiza tratamento, de true(padrão) realiza.
	 * 
	 */
	public void setUsaME(boolean bVal) {
		bUsaME = bVal;
	}

	/**
	 * Ajusta o ListaCampos para restrições de Filial. <BR>
	 * Ajusta o ListaCampos para não realizar tratamento por <BR>
	 * código de filial.
	 * 
	 * @param Se
	 *            false não realiza tratamento, de true(padrão) realiza.
	 * 
	 */
	public void setUsaFI(boolean bVal) {
		bTiraFI = !bVal;
	}

	/**
	 * Retorna se o ListaCampos tem restrições de Multi Empresa. <BR>
	 * 
	 * @return Se falso não realiza tratamento, de true(padrão) realiza.
	 * 
	 */
	public boolean getUsaFI() {
		return !bTiraFI;
	}

	/**
	 * Retorna se o ListaCampos tem restrições de Filial. <BR>
	 * 
	 * @return Se falso não realiza tratamento, de true(padrão) realiza.
	 * 
	 */
	public boolean getUsaME() {
		return bUsaME;
	}

	/**
	 * Ajusta o ListaCampos para restrições de Inserção. <BR>
	 * Ajusta o ListaCampos para realizar ou não carregaDados <BR>
	 * após a inserção.
	 * 
	 * @param Carregar
	 *            Se true executa o carregaDados se false não carrega.
	 * @see #carregaDados
	 * @see #getQueryInsert
	 * 
	 */
	public void setQueryInsert(boolean bVal) {
		bQueryInsert = bVal;
	}

	/**
	 * Retorna true se o ListaCampos esta ajustado para QueryInsert. <BR>
	 * 
	 * @return QueryInsert.
	 * @see #setQueryInsert
	 * 
	 */
	public boolean getQueryInsert() {
		return bQueryInsert;
	}

	/**
	 * Ajusta o navegador para este ListaCampos. <BR>
	 * Ajusta o navegador que disponibilizará os <BR>
	 * controles para gravar, excluir, desfazer...etc.
	 * 
	 * @param Navegador
	 *            A classe já instânciada do navegador.
	 * 
	 */
	public void setNavegador(Navegador nv) {
		nvLC = nv;
	}

	public Navegador getNavegador() {
		return nvLC;
	}

	/**
	 * Retorna o código da filial. <BR>
	 * Retorna o código da filial deviadamente tratado, <BR>
	 * ou seja foi vereficado se a tabela é ME ou não. <BR>
	 * IMPORTANTE: O código só é trabalhado após a execução <BR>
	 * da função setConexao.
	 * 
	 * @return Caso a tabela seja ME o código da matriz é retornado, <BR>
	 *         caso contrário é retornado a filial selecionada no login.
	 * @see #setConexao
	 * 
	 */
	public int getCodFilial() {
		return iCodFilial;
	}

	/**
	 * Retorna a condição adicional. <BR>
	 * Retorna a condição adicional ajustada para este <BR>
	 * lista campos. Geralmente esta condição vem herdada <BR>
	 * desde o JTextFieldPad porque quando ajustada pelo <BR>
	 * método do JTextFieldPad também é ajusta para a Dialog <BR>
	 * de consulta.
	 * 
	 * @return Condição adicional.
	 * @see #setWhereAdic
	 * 
	 */
	public String getWhereAdic() {
		return sWhereAdic;
	}

	/**
	 * Retorna a condição adicional para auto incremento de chave. <BR>
	 * Retorna a condição adicional ajustada para este <BR>
	 * 
	 * @return Condição adicional.
	 * @see #setWhereAdicMax
	 * 
	 */
	public String getWhereAdicMax() {
		return sWhereAdicMax;
	}

	/**
	 * Ajusta a condição adicional. <BR>
	 * Ajusta a condição adicional para este ListaCampos, <BR>
	 * bom isto é usado para ajustar uma condição que o <BR>
	 * ListaCampos não tenha montado sozinho. Geralmente <BR>
	 * esta condição vem herdada desde o JTextFieldPad porque <BR>
	 * quando ajustada pelo método do JTextFieldPad também <BR>
	 * é ajusta para a Dialog de consulta.
	 * 
	 * @param WhereAdic
	 *            Condição adicional.
	 * @see #getWhereAdic
	 * 
	 */
	public void setWhereAdic(String sW) {
		sWhereAdic = sW;
	}

	/**
	 * Ajusta a condição adicional do select max. <BR>
	 * 
	 * @param WhereAdicMax
	 *            Condição adicional.
	 * @see #getWhereAdicMax
	 * 
	 */
	public void setWhereAdicMax(String sW) {
		sWhereAdicMax = sW;
	}

	/**
	 * Retorna a condição adicional para consulta de tabela. <BR>
	 * Retorna a condição adicional ajustada para este <BR>
	 * 
	 * @return Condição adicional.
	 * @see #setWhereAdicTab
	 * 
	 */
	public String getWhereAdicSubSel() {
		return sWhereAdicSubSel;
	}

	/**
	 * Ajusta a condição adicional do sub select para tab. <BR>
	 * 
	 * @param WhereAdicSubSel
	 *            Condição adicional.
	 * @see #getWhereAdicSubSel
	 * 
	 */
	public void setWhereAdicSubSel(String sT) {
		sWhereAdicSubSel = sT;
	}

	/**
	 * Adiciona dinamicamente o valor para o where. <BR>
	 * Busca na hora do carregadados() o valor where e adiciona-o no lugar de #_ <BR>
	 * onde '_' pode ser S (String), D (Date) ou N (Outros). <BR>
	 * Ex: setDinWhereAdic("TESTE = #N",txtTeste); <BR>
	 * 
	 */
	public void setDinWhereAdic(String sDinWhere, JTextComponent jtValor) {
		sWhereAdic += (sWhereAdic.trim().equals("")
				|| sDinWhere.trim().equals("") ? "" : " and ")
				+ sDinWhere;
		txtValor = jtValor;
		if (vTxtValor == null) {
			vTxtValor = new Vector<JTextComponent>();
		}
		vTxtValor.addElement(txtValor);
	}

	public void montaPostCarregaItens(ListaCampos lc) {
		GuardaCampo gcComp = null;
		ListaCampos lcM = lc.getMaster();
		if (lcM != null) {
			try {
				for (int i = 0; i < lcM.getComponentCount(); i++) {
					gcComp = (GuardaCampo) lcM.getComponent(i);
					if (gcComp.ehPK()) {
						if (gcComp.getTipo() == JTextFieldPad.TP_STRING)
							sqlItens.setString(iOrdemT, gcComp.getVlrString());
						else if (gcComp.getTipo() == JTextFieldPad.TP_INTEGER)
							sqlItens.setInt(iOrdemT, gcComp.getVlrInteger()
									.intValue());
						else if (gcComp.getTipo() == JTextFieldPad.TP_DATE)
							sqlItens.setDate(iOrdemT,
									Funcoes.dateToSQLDate(gcComp.getVlrDate()));
						else if (gcComp.getTipo() == JTextFieldPad.TP_TIME)
							sqlItens.setTime(iOrdemT,
									Funcoes.dateToSQLTime(gcComp.getVlrDate()));
						iOrdemT++;
					}
				}
				montaPostCarregaItens(lcM);
			} catch (Exception err) {
				err.printStackTrace();
			}
		}

	}

	public void montaPostMax(ListaCampos lc) {
		ListaCampos lcM = lc.getMaster();
		if (lcM != null) {
			try {
				for (int i = 0; i < lcM.getComponentCount(); i++) {
					if (((GuardaCampo) lcM.getComponent(i)).ehPK()) {

						if (Funcoes.contaChar(sSQLMax, '?') >= iParamMax) {

							if (((GuardaCampo) lcM.getComponent(i)).getTipo() == JTextFieldPad.TP_INTEGER) {
								sqlMax.setInt(iParamMax, ((GuardaCampo) lcM
										.getComponent(i)).getVlrInteger()
										.intValue());
								iParamMax++;
							} else {
								sqlMax.setString(iParamMax, ((GuardaCampo) lcM
										.getComponent(i)).getVlrString());
								iParamMax++;
							}

						}
					}
				}
				montaPostMax(lcM);
			} catch (Exception err) {
				err.printStackTrace();
			}
		}

	}

	public void montaPostCircular4(ListaCampos lc) {
		GuardaCampo comp = null;
		ListaCampos lcM = lc.getMaster();
		if (lcM != null) {
			try {
				for (int i = 0; i < lcM.getComponentCount(); i++) {
					comp = (GuardaCampo) lcM.getComponent(i);
					if (comp.ehPK()) {
						if (comp.getTipo() == JTextFieldPad.TP_INTEGER) {
							sqlLC.setInt(iParamC, comp.getVlrInteger()
									.intValue());
							iParamC++;
						} else if (comp.getTipo() == JTextFieldPad.TP_STRING) {
							sqlLC.setString(iParamC, comp.getVlrString());
							iParamC++;
						} else if (comp.getTipo() == JTextFieldPad.TP_DATE) {
							sqlLC.setDate(iParamC,
									Funcoes.dateToSQLDate(comp.getVlrDate()));
							iParamC++;
						} else if (comp.getTipo() == JTextFieldPad.TP_TIME) {
							sqlLC.setTime(iParamC,
									Funcoes.dateToSQLTime(comp.getVlrTime()));
							iParamC++;
						}

					}
				}
				montaPostCircular4(lcM);
			} catch (Exception err) {
				err.printStackTrace();
			}
		}
	}

	/**
	 * Carrega itens do detalhe. <BR>
	 * Atualiza os dados em uma tabela devidamente adicionada <BR>
	 * pela função setTabela(). Geralmente este método é utilizado para
	 * atualizar os dados de um JTable em um form FDetalhe.
	 * 
	 * @see #setTabela
	 * 
	 */
	public void carregaItens() {
		iOrdemT = 1;
		GuardaCampo gcComp = null;
		if ((bDetalhe) & (tab != null)) {
			limpaCampos(true);
			tab.limpa();
			try {
				String sTmp = sSQLTab;

				for (int i = 0; i < getComponentCount(); i++) {
					gcComp = (GuardaCampo) getComponent(i);
					if (gcComp.ehFK()) {
						ListaCampos lcExt = gcComp.getCampo()
								.getTabelaExterna();
						if ((lcExt != null)
								&& (!"".equals(lcExt.getWhereAdic()))) {
							sTmp = lcExt.inDinWhereAdic(sTmp, lcExt.vTxtValor);
						}
					}
				}

				String sNovaSelect = inDinWhereAdic(sTmp, vTxtValor);
				sqlItens = con.prepareStatement(sNovaSelect);
				if (bUsaME) {
					sqlItens.setInt(iOrdemT, iCodEmp);
					iOrdemT++;
				}
				if (bUsaME && !bTiraFI) {
					sqlItens.setInt(iOrdemT, iCodFilial);
					iOrdemT++;
				}

				montaPostCarregaItens(this);

				rsItens = sqlItens.executeQuery();
				for (int iLin = 0; rsItens.next(); iLin++) {
					tab.adicLinha();
					int colfim = 0;
					for (int iCol = 0; iCol < (getComponentCount() + iNumDescs); iCol++) {
						if (rsItens.getString(iCol + 1) == null) {
							tab.setValor("", iLin, iCol);
						} else if (iTipos[iCol] == JTextFieldPad.TP_BOOLEAN) {
							if ("S".equals(rsItens.getString(iCol + 1))) {
								tab.setValor(new Boolean(true), iLin, iCol);
							} else {
								tab.setValor(new Boolean(false), iLin, iCol);
							}
						} else if (iTipos[iCol] == JTextFieldPad.TP_STRING) {
							tab.setValor(rsItens.getString(iCol + 1), iLin,
									iCol);
						} else if (iTipos[iCol] == JTextFieldPad.TP_INTEGER) {
							tab.setValor(new Integer(rsItens.getInt(iCol + 1)),
									iLin, iCol);
						} else if (iTipos[iCol] == JTextFieldPad.TP_DATE) {
							tab.setValor(
									StringFunctions.sqlDateToStrDate(rsItens
											.getDate(iCol + 1)), iLin, iCol);
						} else if (iTipos[iCol] == JTextFieldPad.TP_TIME) {
							tab.setValor(Funcoes.sqlTimeToStrTime(rsItens
									.getTime(iCol + 1)), iLin, iCol);
						} else if (iTipos[iCol] == JTextFieldPad.TP_DECIMAL) {
							tab.setValor(Funcoes.setPontoDec(rsItens
									.getString(iCol + 1)), iLin, iCol);
						}
						/*
						 * else if (iTipos[iCol] == JTextFieldPad.TP_NUMERIC) {
						 * tab
						 * .setValor(Funcoes.setPontoDec(rsItens.getString(iCol
						 * + 1)), iLin, iCol); }
						 */
						else if (iTipos[iCol] == JTextFieldPad.TP_BYTES) {
							tab.setValor("# BINÁRIO #", iLin, iCol);
						}
						colfim = iCol;
					}
					if (loginstab) {
						tab.setValor(rsItens.getString("idusuins"), iLin,
								++colfim);
						tab.setValor(
								Funcoes.dateToStrDate(rsItens.getDate("dtins")),
								iLin, ++colfim);
						tab.setValor(
								Funcoes.dateToStrTime(rsItens.getTime("hins")),
								iLin, ++colfim);
						tab.setValor(rsItens.getString("idusualt"), iLin,
								++colfim);
						tab.setValor(
								Funcoes.dateToStrDate(rsItens.getDate("dtalt")),
								iLin, ++colfim);
						tab.setValor(
								Funcoes.dateToStrTime(rsItens.getTime("halt")),
								iLin, ++colfim);
					}
				}
				if ((bQueryCommit) && (bPodeCommit))
					con.commit();

			} catch (SQLException err) {
				Funcoes.mensagemErro(cOwner, "Erro ao montar grid para tabela "
						+ sTabela.toLowerCase() + "\n" + err.getMessage());
				err.printStackTrace();
			}
			try {
				carregaItem(0);
			} catch (Exception e) {
				new ExceptionCarregaDados(
						"Erro ao carregar dados do primeiro item");
			}
		}
	}

	/**
	 * Carrega um item da tabela. <BR>
	 * Carrega no detalhe o item da linha 'n' na tabela.
	 * 
	 * @see #setTabela
	 * 
	 */
	public boolean carregaItem(int ind) throws ExceptionCarregaItem {
		boolean bRetorno = false;
		GuardaCampo gcCampo = null;
		// int iSalto = 0;
		if ((tab != null) && (bDetalhe)) {
			/*
			 * if (tab.getNumLinhas() > 0) { for (int i = 0; i <
			 * getComponentCount(); i++) { gcCampo = ((GuardaCampo)
			 * getComponent(i)); if (gcCampo.ehPK()) {
			 * gcCampo.getCampo().setVlrString("" + tab.getValor(ind,
			 * i+iSalto)); } else iSalto++; } bRetorno = carregaDados(); }
			 */
			try {
				// carregando = true;

				int iComp = 0;
				for (int i = 0; i < tab.getNumColunas(); i++) {
					gcCampo = ((GuardaCampo) getComponent(iComp));
					// gcCampo.setCarregando(carregando);
					if (gcCampo.ehPK()) {
						gcCampo.getCampo().setVlrString(
								tab.getValor(ind, i) != null ? String
										.valueOf(tab.getValor(ind, i)) : "");
						iComp++;
					}
					if (gcCampo.ehFK() && gcCampo.getDescFK() != null) {
						i++;
					}
				}
			} finally {
				// carregando = false;
			}

			int iComp = 0;
			for (int i = 0; i < tab.getNumColunas(); i++) {
				gcCampo = ((GuardaCampo) getComponent(iComp));
				// gcCampo.setCarregando(carregando);
			}

			try {
				bRetorno = carregaDados();
			} catch (Exception e) {
				e.printStackTrace();
				throw new ExceptionCarregaItem("Erro ao carregar dados");
			}
		}
		return bRetorno;
	}

	private String montaSubSelect(Component comp, int iNpk) {
		GuardaCampo gc = (GuardaCampo) comp;
		GuardaCampo gcFK = null;
		GuardaCampo gcFKCampo = null;
		String sRetorno = "";
		String sWhere = "";
		String sAnd = "";
		ListaCampos lcFK = gc.getCampo().getTabelaExterna();
		// System.out.println("*******" + gc.getCampo().getNomeCampo());
		// System.out.println("******" + lcFK.getNomeTabela());

		sAnd = "";
		boolean bPrim = true;
		int i;
		int i2 = 0;
		int iPK = -1;
		int iFK = 0;
		for (i = 0; i < lcFK.getComponentCount(); i++) {
			gcFK = (GuardaCampo) lcFK.getComponent(i);

			if (gcFK.ehPK()) {
				/*
				 * Se for a primeira PK ele linka com o nome do campo que esta
				 * neste listaCampos, assim ele consegue fazer ex.:
				 * CODPROD=CODPRODPD
				 */

				if (iNpk > 1) {
					while ((getComponentCount() > i2) & (iPK <= iNpk)) {
						if ((((GuardaCampo) getComponent(i2)).ehPK())
								|| (((GuardaCampo) getComponent(i2)).ehFK())) {
							if (((GuardaCampo) getComponent(i2)).ehPK())
								iPK++;

							if (((GuardaCampo) getComponent(i2)).ehFK()) {
								String sTabelaExternaComp = ((GuardaCampo) getComponent(i2))
										.getCampo().getTabelaExterna()
										.getNomeTabela().toLowerCase();
								String sTabelaExternaFK = lcFK.getNomeTabela().toLowerCase();

								if (sTabelaExternaComp.equalsIgnoreCase(sTabelaExternaFK)) {

									GuardaCampo gcPK = (GuardaCampo) getComponent(i2);
									// Se existirem FKs para tabelas com o mesmo
									// nome, haverá problemas um erro na linha
									// abaixo
									// devido ao uso de HashMap no método
									// montaTab, que poderá ser resolvido
									// adicionando algum
									// diferenciador no nome da tabela como por
									// exemplo um espaço em branco.
									GuardaCampo gcFK2 = (GuardaCampo) lcFK
											.getComponent(iFK);
									iFK++;

									sWhere = sWhere + sAnd
											+ gcFK2.getNomeCampo()
											+ " = master."
											+ gcPK.getNomeCampo();
									sAnd = " and ";

								}
							}
						}
						i2++;
					}
				} else if (!bPrim)
					sWhere = sWhere + sAnd + gcFK.getNomeCampo() + " = master."
							+ gcFK.getNomeCampo();
				else {
					sWhere = sWhere + sAnd + gcFK.getNomeCampo() + " = master."
							+ gc.getNomeCampo();
					bPrim = false;
				}
				sAnd = " and ";
			} else if (gcFKCampo == null)
				gcFKCampo = (GuardaCampo) lcFK.getComponent(i);
		}
		sWhere += lcFK.getWhereAdic().equals("") ? "" : sAnd
				+ lcFK.getWhereAdic();
		sRetorno = "(select "
				+ (gcFKCampo != null ? gcFKCampo : gcFK).getNomeCampo()
				+ " from "
				+ lcFK.getNomeTabela()
				+ " where "
				+ (lcFK.getUsaME() ? "codemp=master.codemp"
						+ lcFK.getSigla()
						+ (lcFK.getUsaFI() ? " and codfilial=master.codfilial"
								+ lcFK.getSigla() : "") + " and " : "")
								+ sWhere
								+ (lcFK.getWhereAdicSubSel() != null
								&& lcFK.getWhereAdicSubSel().trim().length() > 0 ? " and "
								+ lcFK.getWhereAdicSubSel()
								: "") + ")";
		return sRetorno;
	}

	public void setOrdem(String sOrd) {
		sOrdem = " order by " + sOrd;
	}

	public String inDinWhereAdic(String sVal, Vector<JTextComponent> vTxtVal) {
		if (vTxtVal == null)
			return sVal;
		String sValOrig = sVal;
		String sRet = "";
		for (int i = 0; i < vTxtVal.size(); i++) {
			txtValor = vTxtVal.elementAt(i);
			int iPos = sValOrig.indexOf("#");
			if (iPos == -1)
				return sValOrig;
			sRet = sValOrig.substring(0, iPos);
			switch (sValOrig.charAt(iPos + 1)) {
			case 'N':
				sRet += (txtValor.getText().trim().equals("") ? "0" : txtValor
						.getText().trim());
				break;
			case 'S':
				sRet += "'"
						+ (txtValor.getText().trim().equals("") ? "" : txtValor
								.getText().trim()) + "'";
				break;
			case 'D':
				sRet += "'"
						+ (txtValor.getText().trim().equals("") ? "" : Funcoes
								.strDateToStrDB(txtValor.getText().trim()))
								+ "'";
				break;
			}
			sRet += sValOrig.substring(iPos + 2);
			sValOrig = sRet;
		}
		return sRet;
	}

	public void montaTab() {
		GuardaCampo comp = null;
		String sTitulo = "";
		String sSubSelect = "";
		String sNome = "";
		sSepT = " ";
		sWhereT = "";
		HashMap<String, Integer> hmTabelasExternas = new HashMap<String, Integer>();
		int i = 0;
		int iTot = 0;
		sSQLTab = "select ";
		iTipos = new int[150];
		sMascs = new String[150];
		iTot = getComponentCount();
		iNumDescs = 0;
		vDescFK = new Vector<JTextFieldPad>();

		while (i < iTot) {
			comp = (GuardaCampo) getComponent(i - iNumDescs);
			sTitulo = comp.getTituloCampo();
			sNome = comp.getNomeCampo();
			if (comp.getComponente() instanceof JCheckBoxPad) {
				iTipos[i] = JTextFieldPad.TP_BOOLEAN;
			} else {
				iTipos[i] = comp.getTipo();
			}
			if (comp.getCampo() != null)
				sMascs[i] = comp.getCampo().getStrMascara();
			tab.adicColuna(sTitulo);
			sSQLTab += sSepT + sNome;
			sSepT = ",";
			i++;
			vDescFK.addElement(null);
			if (comp.ehFK() && (comp.getDescFK() != null)) {
				int iNpk = comp.getCampo().getTabelaExterna() != null ? comp
						.getCampo().getTabelaExterna().getNumPKs() : comp
						.getCampo().getListaCampos().getNumPKs();
						String sSigla = comp.getCampo().getTabelaExterna() != null ? comp
								.getCampo().getTabelaExterna().getSigla()
								: comp.getCampo().getListaCampos().getSigla();
								if (sSigla != null) {
									if (sSigla.length() > 0) {
										String sNomeTab = comp.getCampo().getListaCampos()
												.getNomeTabela()
												+ sSigla;
										int iP = hmTabelasExternas.get(sNomeTab) == null ? 0
												: (hmTabelasExternas.get(sNomeTab)).intValue();
										hmTabelasExternas.put(comp.getCampo().getListaCampos()
												.getNomeTabela()
												+ sSigla, new Integer(iP + 1));
										if (iNpk == (iP + 1)) { // Implementado para incluir
											// descrição da fk apenas após
											// ultimo campo da pk.
											hmTabelasExternas.put(comp.getCampo()
													.getListaCampos().getNomeTabela(),
													new Integer(iP + 1));
											sSubSelect = montaSubSelect(comp, iNpk);
											if (sSubSelect.trim().length() != 0) {
												sSQLTab += sSepT + sSubSelect;
												sTitulo = comp.getDescFK().getLabel();
												tab.adicColuna(sTitulo);
												iTipos[i] = comp.getDescFK().getTipo();
												vDescFK.addElement(comp.getDescFK());
												i++;
												iTot++;
												iNumDescs++;
											}
										}
									}
								}
			}
		}
		if (loginstab) {
			sSQLTab += " , idusuins, dtins, hins, idusualt, dtalt, halt ";
			tab.adicColuna("Id.Usu.Ins");
			tab.adicColuna("Dt.Ins");
			tab.adicColuna("H.Ins");
			tab.adicColuna("Id.Usu.Alt");
			tab.adicColuna("Dt.Alt");
			tab.adicColuna("H.Alt");
		}

		if (bTiraFI)
			sSQLTab += " from " + sTabela.toLowerCase()
			+ " master where master.codemp=?";
		// alterado aqui para gerar codemp dinâmico + iCodEmp;
		else if (bUsaME)
			sSQLTab += " from "
					+ sTabela.toLowerCase()
					+ " master where master.codemp=?"
					/* + iCodEmp --código da empresa dinâmico */+ " and master.codfilial=?";
		else
			sSQLTab += " from " + sTabela.toLowerCase() + " master ";

		sSepT = (bTiraFI || bUsaME ? " and " : " where ");

		montaWhereCircular(this);

		sSQLTab += sWhereAdic.trim().equals("") ? "" : sSepT + sWhereAdic;

		if (sWhereT.length() > 0)
			sSQLTab += sWhereT + sOrdem;

		// System.out.println("SQL DO GRID:" + sSQLTab);

	}

	public Vector<GuardaCampo> getCamposPK() {
		GuardaCampo comp = null;
		GuardaCampo gcCampo = null;
		Vector<GuardaCampo> vRetorno = new Vector<GuardaCampo>();
		try {
			for (int i = 0; i < getComponentCount(); i++) {
				comp = (GuardaCampo) getComponent(i);
				if (comp instanceof GuardaCampo) {
					gcCampo = comp;
					if (gcCampo.ehPK())
						vRetorno.addElement(gcCampo);
				}
			}
		} finally {
			comp = null;
			gcCampo = null;
		}
		return vRetorno;
	}

	public void montaWhereCircular(ListaCampos lc) {
		ListaCampos lcM = lc.getMaster();

		if (lcM != null) {
			for (int i2 = 0; i2 < lcM.getComponentCount(); i2++) {
				if (((GuardaCampo) lcM.getComponent(i2)).ehPK()) {
					sWhereT += sSepT
							+ ((GuardaCampo) lcM.getComponent(i2))
							.getNomeCampo() + "=?";
					sSepT = " and ";
				}
			}
			montaWhereCircular(lcM);
		}
	}

	public void montaWhereCircular2(ListaCampos lc) {
		GuardaCampo comp = null;
		ListaCampos lcM = lc.getMaster();
		if (lcM != null) {
			if (bAutoInc) {
				String sCampo = "";
				for (int i = 0; i < lcM.getComponentCount(); i++) {
					comp = (GuardaCampo) lcM.getComponent(i);
					if (comp.ehPK()) {
						sCampo = comp.getNomeCampo();
						sSQLMax += sSepM + sCampo + "=?";
						sSepM = " and ";
					}
				}
			}
			for (int i = 0; i < lcM.getComponentCount(); i++) {
				comp = (GuardaCampo) lcM.getComponent(i);
				if (comp.ehPK()) {
					sCampoSQL = comp.getNomeCampo();
					if (fieldsUpdate.get(sCampoSQL)==null) {
						sSQLUpdate += sSepU + sCampoSQL + "=?";
						fieldsUpdate.put(sCampoSQL, new Integer(fieldsUpdate.size()+1));
					}
					sSQLSelect += sSepParam + sCampoSQL + "=?";
					sSQLDelete += sSepD + sCampoSQL + "=?";
					sSepD = sSepU = sSepParam = " and ";

					if (comp.ehFK()) {
						ListaCampos lcExt = comp.getCampo().getTabelaExterna();
						if ((lcExt != null) && lcExt.getUsaME()) {
							String campoCodemp = "codemp" + lcExt.getSigla().toLowerCase();
							String campoCodfilial = "codfilial" + lcExt.getSigla().toLowerCase();
							if (fieldsUpdate.get(campoCodemp)==null) {
								sSQLUpdate += sSepU + campoCodemp+ "=?";
								fieldsUpdate.put(campoCodemp, new Integer(fieldsUpdate.size()+1));
							}
							if (lcExt.getUsaFI()) {
								if (fieldsUpdate.get(campoCodfilial)==null) {
									sSQLUpdate += sSepU + campoCodfilial + "=?";
									fieldsUpdate.put(campoCodfilial, new Integer(fieldsUpdate.size()+1));
								}
							}
							// sSQLSelect += sSepParam + sCampoSQL + "=?";
							// sSQLDelete += sSepD + sCampoSQL + "=?";
							// sSepD = sSepU = sSepParam = " and ";
						}
					}
				}
			}
			montaWhereCircular2(lcM);
		}

	}

	public void montaCorpoSQLCircular2(ListaCampos lc) {
		GuardaCampo comp = null;
		ListaCampos lcM = lc.getMaster();
		if (lcM != null) {
			String sCampo = "";
			for (int i = 0; i < lcM.getComponentCount(); i++) {
				comp = (GuardaCampo) lcM.getComponent(i);
				if (comp.ehPK()) {
					sCampo = comp.getNomeCampo();
					if (fieldsInsert.get(sCampo) == null) {
						sSQLInsert += sSepI + sCampo;
						fieldsInsert.put(sCampo, fieldsInsert.size()+1);
					}
					sSepI = ",";
					if (comp.ehFK()) {
						ListaCampos lcExt = comp.getCampo().getTabelaExterna();
						if ((lcExt != null) && lcExt.getUsaME()) {

							if (lcExt.getUsaFI()) {
								String campoCodemp = "codemp"
										+ lcExt.getSigla();
								String campoCodfilial = "codfilial"
										+ lcExt.getSigla();
								// Condição para evitar duplicação de codemp e
								// codfilial
								if (fieldsInsert.get(campoCodemp) == null) {
									fieldsInsert.put(campoCodemp, fieldsInsert.size()+1);
									sSQLInsert += "," + campoCodemp;
								}
								if (fieldsInsert.get(campoCodfilial) == null) {
									fieldsInsert.put(campoCodfilial, fieldsInsert.size() + 1);
									sSQLInsert += "," + campoCodfilial;
								}
							}
							if (!comp.ehPK()) {
								String sigla = lcExt.getSigla();
								if (sigla==null) {
									sigla = "";
								}
							    String campoCodemp = "codemp"+sigla;
							    String campoCodfilial = "codfilial"+sigla;
							    if (fieldsUpdate.get(campoCodemp)==null) {
									sSQLUpdate += ","+campoCodemp+ "=?";
									fieldsUpdate.put(campoCodemp, new Integer(fieldsUpdate.size()+1));
							    }
							    if (lcExt.getUsaFI() ) {
							    	if (fieldsUpdate.get(campoCodfilial)==null) {
										sSQLUpdate +=  ","+campoCodfilial+ "=?";
										fieldsUpdate.put(campoCodfilial, new Integer(fieldsUpdate.size()+1));
							    	}
							    }
							}
						}
					}

				}
			}
			montaCorpoSQLCircular2(lcM);
		}
	}

	/*
	 * public void montaSqlCircular1(ListaCampos lc) { GuardaCampo comp = null;
	 * HashMap<String, Integer> hmTabelasExternas = new HashMap<String,
	 * Integer>(); lcM = lc.getMaster(); if (lcM != null) { for (int i = 0; i <
	 * lcM.getComponentCount(); i++) { comp = ( GuardaCampo )
	 * lcM.getComponent(i); if (comp.ehPK()) { sSQLInsert += sSepParam + "?";
	 * sSepParam = ","; if (comp.ehFK()) { String sSigla =
	 * comp.getCampo().getTabelaExterna() != null ?
	 * comp.getCampo().getTabelaExterna().getSigla() :
	 * comp.getCampo().getListaCampos().getSigla(); if (sSigla != null) { if
	 * (sSigla.length() > 0) { String sNomeTab =
	 * comp.getCampo().getListaCampos().getNomeTabela(); if
	 * (hmTabelasExternas.get(sNomeTab) == null) {
	 * hmTabelasExternas.put(comp.getCampo().getListaCampos().getNomeTabela(),
	 * new Integer(i)); sSQLInsert += sSepParam + "?,?"; } } } } } }
	 * montaSqlCircular1(lcM); } }
	 */
	public String getSqlSelect() {
		return sSQLSelect;
	}

	public synchronized void montaSql(boolean bAuto, String sTab, String sA) {

		sSepParam = "";
		sSepD = "";
		sSepU = "";
		sSepI = "";
		sSepM = "";
		sCampoSQL = "";
		String sWhereEmp = "";

		bAutoInc = bAuto;
		sArea = sA;
		sTabela = sSchema.toLowerCase() + sArea.toLowerCase() + sTab.toLowerCase();
		if (bUsaME)
			iCodFilial = getMasterFilial(sTabela);
		sPK = retPK();
		this.iNumPKs = 0;

		/*if ("eqproduto".equalsIgnoreCase(sTab)) {
			System.out.println("Iniciando sql insert eqproduto");
		}*/
		if (bAutoInc) {
			/*
			 * Bom esse negocio que vem ai é para colar o codemp e o codfilial
			 * caso seja necessário e se não for necessário, colocar o 'WHERE'
			 * se este listacampos não for detalhe....porque se for detalhe é
			 * preciso colocar as pks do listacampos master.
			 */
			if (bTiraFI)
				sSQLMax = "select max(" + sPK + ") from "
						+ sTabela.toLowerCase() + " where codemp=?" /*
						 * + iCodEmp
						 */;
			else if (bUsaME)
				sSQLMax = "select max(" + sPK + ") from "
						+ sTabela.toLowerCase() + " where codemp=?"
						+ /*
						 * iCodEmp +
						 */" and codfilial=?";
			else
				sSQLMax = "select max(" + sPK + ") from "
						+ sTabela.toLowerCase() + (bDetalhe ? " where " : "");

			if (!"".equals(sWhereAdicMax)) {
				if (sSQLMax.indexOf("where") >= 0) {
					sSQLMax += " and " + sWhereAdicMax;
				} else {
					sSQLMax += "where " + sWhereAdicMax;
				}
			}
		}
		if (bTiraFI) {
			sSQLInsert = "insert into " + sTabela.toLowerCase() + " (codemp,";
			fieldsInsert.put("codemp", new Integer(1));
		} else if (bUsaME) {
			sSQLInsert = "insert into " + sTabela.toLowerCase()
					+ " (codemp,codfilial,";
			fieldsInsert.put("codemp", new Integer(1));
			fieldsInsert.put("codfilial", new Integer(2));
		} else {
			sSQLInsert = "insert into " + sTabela.toLowerCase() + " (";
		}

		sSQLUpdate = "update " + sTabela.toLowerCase() + " set  ";
		sSQLSelect = "select ";
		sSQLDelete = "delete";

		GuardaCampo comp = null;

		if ((bDetalhe) && (lcMaster != null)) {
			montaCorpoSQLCircular2(this);
		}

		sSepParam = "";
		for (int i = 0; i < getComponentCount(); i++) {
			comp = (GuardaCampo) getComponent(i);
			GuardaCampo gcCampo = comp;
			sCampoSQL = gcCampo.getNomeCampo();
			if (!gcCampo.getSoLeitura()) {
				if (!gcCampo.ehPK()) {
					if (fieldsUpdate.get(sCampoSQL)==null) {
						sSQLUpdate += sSepU + sCampoSQL + "=?";
						sSepU = ",";
						fieldsUpdate.put(sCampoSQL, new Integer(fieldsUpdate.size()+1));
					}
				}
				if (fieldsInsert.get(sCampoSQL) == null) {
					fieldsInsert.put(sCampoSQL, fieldsInsert.size()+1);
					sSQLInsert += sSepI + sCampoSQL;
				}
				if (gcCampo.ehFK()) {
					ListaCampos lcExt = gcCampo.getCampo().getTabelaExterna();
					if ((lcExt != null) && lcExt.getUsaME()) {
						if (lcExt.getUsaFI()) {
							String campoCodemp = "codemp" + lcExt.getSigla();
							String campoCodfilial = "codfilial"
									+ lcExt.getSigla();
							// Condição para evitar duplicação de codemp e
							// codfilial
							if (fieldsInsert.get(campoCodemp) == null) {
								fieldsInsert.put(campoCodemp, fieldsInsert.size() + 1);
								sSQLInsert += "," + campoCodemp;
							}
							if (fieldsInsert.get(campoCodfilial) == null) {
								fieldsInsert.put(campoCodfilial, fieldsInsert.size() + 1);
								sSQLInsert += "," + campoCodfilial;
							}
						}
						if (!gcCampo.ehPK()) {
							String sigla = lcExt.getSigla();
							if (sigla==null) {
								sigla = "";
							}
						    String campoCodemp = "codemp"+sigla;
						    String campoCodfilial = "codfilial"+sigla;
						    if (fieldsUpdate.get(campoCodemp)==null) {
								sSQLUpdate += ","+campoCodemp+ "=?";
								fieldsUpdate.put(campoCodemp, new Integer(fieldsUpdate.size()+1));
						    }
						    if (lcExt.getUsaFI() ) {
						    	if (fieldsUpdate.get(campoCodfilial)==null) {
									sSQLUpdate +=  ","+campoCodfilial+ "=?";
									fieldsUpdate.put(campoCodfilial, new Integer(fieldsUpdate.size()+1));
						    	}
						    }
						}
					}
				}
				sSepI = ",";
			}

			if (gcCampo.ehPK())
				this.iNumPKs++;

			sSQLSelect += sSepParam + sCampoSQL;
			sSepParam = ",";
		}
		sSepParam = "";

		sWhereEmp = bUsaME ? "codemp=?" + (bTiraFI ? "" : " and codfilial = ?")
				: "";

		sSQLUpdate += " where ";
		sSQLDelete += " from " + sTabela.toLowerCase() + " where " + sWhereEmp;
		sSQLSelect += " from " + sTabela.toLowerCase() + " where " + sWhereEmp;

		sSepD = sSepM = sSepParam = (bTiraFI || bUsaME ? " and " : "");

/*		if ("sgprefere1".equals(sTabela.toLowerCase())) {
			System.out.println(sSQLUpdate);
		}
		*/
		if (!sWhereAdic.trim().equals("")) {
			sSQLSelect += sSepParam + sWhereAdic;
			sSepParam = " and ";
		}

		// Monta o where com as pks da master:

		sSepU = "";
		if ((bDetalhe) && (lcMaster != null)) {
			montaWhereCircular2(this);
		}

		// Coloca no where as pks deste lista campos:

		for (int i = 0; i < getComponentCount(); i++) {
			GuardaCampo gcCampo = ((GuardaCampo) getComponent(i));
			ListaCampos lcExt = null;
			if (gcCampo.ehPK()) {
				if (bDetalhe) {
					gcCampo.getCampo().cancelaDLF2();
				}
				if (!gcCampo.getSoLeitura()) {
					sCampoSQL = gcCampo.getNomeCampo();
					if (fieldsUpdate.get(sCampoSQL)==null) {
						sSQLUpdate += sSepU + sCampoSQL + "=?";
						fieldsUpdate.put(sCampoSQL, new Integer(fieldsUpdate.size()+1));
					}
					sSQLDelete += sSepD + sCampoSQL + "=?";
					if (gcCampo.ehFK()) {
						lcExt = gcCampo.getCampo().getTabelaExterna();
						if ((lcExt != null) && lcExt.getUsaME()) {
							String campoCodemp = "codemp"+ lcExt.getSigla().toLowerCase();
							if (fieldsUpdate.get(campoCodemp)==null) {
								sSQLUpdate += " and "+campoCodemp+ "=?";
								fieldsUpdate.put(campoCodemp, new Integer(fieldsUpdate.size()+1));
							}
							if (lcExt.getUsaFI()) {
								String campoCodfilial = "codfilial" + lcExt.getSigla().toLowerCase();
								if (fieldsUpdate.get(campoCodfilial)==null) {
									sSQLUpdate += " and  "+campoCodfilial+"=?";
									fieldsUpdate.put(campoCodfilial, new Integer(fieldsUpdate.size()+1));
								}
							}
							sSQLDelete += " and codemp"
									+ lcExt.getSigla()
									+ "=?"
									+ (lcExt.getUsaFI() ? " and codfilial"
											+ lcExt.getSigla() + "=?" : "");
						}
					}
					sSepU = " and ";
				}
				sSQLSelect += sSepParam + sCampoSQL + "=?";
				if (gcCampo.ehFK()) {
					lcExt = gcCampo.getCampo().getTabelaExterna();
					if ((lcExt != null) && lcExt.getUsaME()) {
						sSQLSelect += " and codemp"
								+ lcExt.getSigla()
								+ "=?"
								+ (lcExt.getUsaFI() ? " and codfilial"
										+ lcExt.getSigla() + "=?" : "");
					}
				}
				sSepD = sSepParam = " and ";
			}
		}

		if (bTiraFI) {
			if (fieldsUpdate.get("codemp")==null) {
				sSQLUpdate += sSepU + "codemp=?";
				fieldsUpdate.put("codemp", new Integer(fieldsUpdate.size()+1));
				if ("".equals(sSepU.trim())) {
					sSepU = " and ";
				}
			}
		}
		else if (bUsaME) {
			if (fieldsUpdate.get("codemp")==null) {
				sSQLUpdate += sSepU + "codemp=?";
				fieldsUpdate.put("codemp", new Integer(fieldsUpdate.size()+1));
				if ("".equals(sSepU.trim())) {
					sSepU = " and ";
				}
			}
			if (fieldsUpdate.get("codfilial")==null) {
				sSQLUpdate += sSepU + "codfilial=?";
				fieldsUpdate.put("codfilial", new Integer(fieldsUpdate.size()+1));
				if ("".equals(sSepU.trim())) {
					sSepU = " and ";
				}
			}
		}
		

		/*
		 * if (bTiraFI) sSQLInsert += ") values (?,"; else if (bUsaME)
		 * sSQLInsert += ") values (?, ?,"; else sSQLInsert += ") values (";
		 */
		sSQLInsert += ") values (";
		sSepParam = "";

		/*if ("eqproduto".equalsIgnoreCase(sTabela.toLowerCase())) {
			System.out.println("Tamanho query: "+fieldsInsert.size());
			Iterator<String> it = fieldsInsert.keySet().iterator(); 
			while ( it.hasNext() ) {
				String tmp = it.next();
				System.out.println(tmp+"-"+fieldsInsert.get(tmp));
			}
		}*/
		for (int i = 0; i < fieldsInsert.size(); i++) {
			sSQLInsert += sSepParam + "?";
			sSepParam = ",";
		}
		/*
		 * for (int i = 0; i < getComponentCount(); i++) { GuardaCampo gcCampo =
		 * ( ( GuardaCampo ) getComponent(i) );
		 * 
		 * if (!gcCampo.getSoLeitura()) { sSQLInsert = sSQLInsert + sSepParam +
		 * "?"; if (gcCampo.ehFK()) { ListaCampos lcExt =
		 * gcCampo.getCampo().getTabelaExterna(); if (( lcExt != null ) &&
		 * lcExt.getUsaME()) { sSQLInsert += ",?" + ( lcExt.getUsaFI() ? ",?" :
		 * "" ); } } sSepParam = ","; } }
		 */
		sSQLInsert += ")";

		bCamposCanc = new boolean[getComponentCount()];

	}

	public void first(int tipo) {
		if ((bDetalhe) && (tab != null) && (tab.getNumLinhas() > 0)) {
			tab.setLinhaSel(0);
			try {
				carregaItem(0);
			} catch (Exception e) {
				new ExceptionCarregaDados("Erro ao carregar primeiro item");
			}
		}
		// Metodo utilizado para carregar o ultimo registro sem utilização de
		// ponteiro.
		else if (!bDetalhe) {

			carregaRegistro(PRIMEIRO_REGISTRO, tipo);

		}

	}

	private void carregaRegistro(int registro, int tipo) {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		GuardaCampo campopk = null;

		try {

			campopk = getCamposPK().elementAt(0);

			if (ANTERIOR_REGISTRO == registro || ULTIMO_REGISTRO == registro) {

				sql.append("select max(");

			} else if (PROXIMO_REGISTRO == registro
					|| PRIMEIRO_REGISTRO == registro) {

				sql.append("select min(");

			}

			sql.append(campopk.getNomeCampo());
			sql.append(") from ");
			sql.append(getNomeTabela());
			sql.append(" where codemp=? and codfilial=? ");

			if (ANTERIOR_REGISTRO == registro) {
				sql.append(" and ");
				sql.append(campopk.getNomeCampo());
				sql.append(" < ? ");
			} else if (PROXIMO_REGISTRO == registro) {
				sql.append(" and ");
				sql.append(campopk.getNomeCampo());
				sql.append(" > ? ");
			}

			ps = con.prepareStatement(sql.toString());

			ps.setInt(1, getCodEmp());
			ps.setInt(2, getCodFilial());

			if (ANTERIOR_REGISTRO == registro || PROXIMO_REGISTRO == registro) {

				if (tipo == Types.INTEGER) {
					ps.setInt(3, getVlrIntegerPK());
				} else if (tipo == Types.CHAR) {
					ps.setString(3, getVlrStringPK());
				}
			}

			rs = ps.executeQuery();

			if (rs.next()) {
				String reg = rs.getString(1);

				if (reg != null && reg.length() > 0) {
					setVlrPK(reg);
					boolean changeedit = false;
					try {
						if (canedit) {
							canedit = false;
							changeedit = true;
						}
						carregaDados();
					} finally {
						if (changeedit) {
							canedit = true;
						}
					}

				} else {
					System.out.println("Não existe o registro solicitado!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void prior(int tipo) {

		int iLin = 0;

		if ((bDetalhe) && (tab != null) && (tab.getNumLinhas() > 0)) {
			iLin = getNumLinha();
			if (iLin > 0) {
				tab.setLinhaSel(iLin - 1);
				try {
					carregaItem(iLin - 1);
				} catch (Exception e) {
					new ExceptionCarregaDados("Erro ao carregar item anterior");
				}
			}
		}
		// Metodo utilizado para carregar o registro anterior sem utilização de
		// ponteiro.
		else if (!bDetalhe) {

			carregaRegistro(ANTERIOR_REGISTRO, tipo);

		}
	}

	private void setVlrPK(String vlr) {

		// Ainda não está preparado para Chave Composta!!!

		try {
			getCamposPK().elementAt(0).setVlrString(vlr);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private Integer getVlrIntegerPK() {

		// Ainda não está preparado para Chave Composta!!!

		Integer ret = null;

		try {
			ret = getCamposPK().elementAt(0).getVlrInteger();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;

	}

	private String getVlrStringPK() {

		// Ainda não está preparado para Chave Composta!!!

		String ret = null;

		try {
			ret = getCamposPK().elementAt(0).getVlrString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;

	}

	public void next(int tipo) {
		int iLin = 0;
		int iNumLinhas = 0;
		if ((bDetalhe) && (tab != null)) {
			iNumLinhas = tab.getNumLinhas();
			if (iNumLinhas > 0) {
				iLin = getNumLinha();
				if (iLin < (iNumLinhas - 1)) {
					tab.setLinhaSel(iLin + 1);
					try {
						carregaItem(iLin + 1);
					} catch (Exception e) {
						new ExceptionCarregaDados(
								"Erro ao carregar proximo item");
					}
				}
			}
		}
		// Metodo utilizado para carregar o registro posterior sem utilização de
		// ponteiro.
		else if (!bDetalhe) {

			carregaRegistro(PROXIMO_REGISTRO, tipo);

		}
	}

	public void last(int tipo) {
		if ((bDetalhe) && (tab != null) && (tab.getNumLinhas() > 0)) {
			tab.setLinhaSel(tab.getNumLinhas() - 1);
			try {
				carregaItem(tab.getNumLinhas() - 1);
			} catch (Exception e) {
				new ExceptionCarregaDados("Erro ao carregar ultimo item");
			}
		}
		// Metodo utilizado para carregar o ultimo registro sem utilização de
		// ponteiro.
		else if (!bDetalhe) {

			carregaRegistro(ULTIMO_REGISTRO, tipo);

		}
	}

	private int getNumLinha() {
		int iCol = -1;
		int iRetorno = -1;
		for (int i = 0; i < getComponentCount(); i++) {
			if (((GuardaCampo) getComponent(i)).ehPK()) {
				iCol = i;
				break;
			}
		}
		if (iCol >= 0) {
			for (int i = 0; i < tab.getNumLinhas(); i++) {
				if (((GuardaCampo) getComponent(iCol)).getCampo().getText()
						.trim().compareTo(("" + tab.getValor(i, iCol)).trim()) == 0) {
					tab.setLinhaSel(i);
					iRetorno = i;
					break;
				}
			}
		}
		return iRetorno;
	}

	public boolean carregaDados() {
		iParamC = 1;
		boolean bResultado = true;
		GuardaCampo comp = null;
		fireBeforeCarrega();
		if (bCancelCarrega) {
			bCancelCarrega = false;
			bResultado = false;
		} else if (lcState == LCS_EDIT) {

			// System.out.println("ListaCampo: "+this.getNomeTabela());

			if (Funcoes.mensagemConfirma(cOwner,
					"Registro ainda não foi salvo! Deseja salvar?") == 0) {
				cancel(false);
				post();
			} else {
				cancel(true);
				bResultado = false;
			}
		}
		vCache = new Vector<Object>();
		if (bResultado) {
			if (con == null) {
				Funcoes.mensagemErro(this, "Conexão nula!!");
				return false;
			}
			try {
				String sNovaSelect = inDinWhereAdic(sSQLSelect, vTxtValor);
				sqlLC = con.prepareStatement(sNovaSelect);
				if (bUsaME) {
					sqlLC.setInt(iParamC, iCodEmp);
					iParamC++;
				}
				if (bUsaME && !bTiraFI) {
					sqlLC.setInt(iParamC, iCodFilial);
					iParamC++;
				}
				if ((bDetalhe) && (lcMaster != null)) {
					montaPostCircular4(this);

				}

				bResultado = montaPostCircular5(this);
				if (!bResultado)
					return bResultado;

				rsLC = sqlLC.executeQuery();
				// Marca flag indicando que está carregando dados -- Utilizado
				// no Combobox para evitar mudança de situação para edição.
				// if (this.getNomeTabela().equalsIgnoreCase("LFITCLFISCAL")) {

				// }

				try {

					// carregando = true;

					if (rsLC.next()) {
						for (int i = 0; i < getComponentCount(); i++) {
							comp = (GuardaCampo) getComponent(i);
							// comp.setCarregando(carregando);
							if (!bCamposCanc[i]) {
								if (comp.getTipo() == JTextFieldPad.TP_BYTES) {
									Blob bVal = rsLC.getBlob(comp
											.getNomeCampo());
									if (bVal != null) {
										comp.setVlrBytes(bVal.getBinaryStream());
									}
								} else if (rsLC.getString(comp.getNomeCampo()) != null) {
									if (comp.getTipo() == JTextFieldPad.TP_INTEGER) {
										comp.setVlrInteger(new Integer(rsLC
												.getInt(comp.getNomeCampo())));
									} else if (comp.getTipo() == JTextFieldPad.TP_STRING) {
										comp.setVlrString(rsLC.getString(comp
												.getNomeCampo()));
									} else if (comp.getTipo() == JTextFieldPad.TP_DECIMAL) {
										comp.setVlrBigDecimal(new java.math.BigDecimal(
												rsLC.getString(comp
														.getNomeCampo())));
									}
									/*
									 * else if (comp.getTipo() ==
									 * JTextFieldPad.TP_NUMERIC) {
									 * comp.setVlrBigDecimal(new
									 * java.math.BigDecimal
									 * (rsLC.getString(comp.getNomeCampo()))); }
									 */
									/*
									 * else if (comp.getTipo() ==
									 * JTextFieldPad.TP_DOUBLE) {
									 * comp.setVlrDouble(new
									 * Double(rsLC.getDouble
									 * (comp.getNomeCampo()))); }
									 */
									else if (comp.getTipo() == JTextFieldPad.TP_DATE) {
										comp.setVlrString(StringFunctions.sqlDateToStrDate(rsLC
												.getDate(comp.getNomeCampo())));
									} else if (comp.getTipo() == JTextFieldPad.TP_TIME) {
										comp.setVlrString(Funcoes.sqlTimeToStrTime(rsLC
												.getTime(comp.getNomeCampo())));
									}
								} else {
									try {
										comp.limpa();
									} catch (ExceptionLimpaComponent e) {
										e.printStackTrace();
									}
								}
								if (comp.ehFK())
									comp.atualizaFK();
							} else if (!bCamposCanc[i]) {
								try {
									comp.limpa();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						bResultado = true;
					} else {
						if (!this.bPodeIns) {
							limpaCampos(false);
							limpaDetalhes();
							Funcoes.mensagemInforma(cOwner,
									"Registro não foi encontrado!");
						}
						bResultado = false;
					}
					if ((bQueryCommit) && (bPodeCommit))
						con.commit(); // MOD
				} finally {
					// carregando = false;
				}
			} catch (SQLException err) {
				// JOptionPane.showMessageDialog(null,"Erro ao carregar dados da
				// tabela: "+sTabela+"\n"+err.getMessage());
				Funcoes.mensagemErro(cOwner,
						"Erro ao carregar dados da tabela: " + sTabela.toLowerCase() + "\n"
								+ err.getMessage());
				err.printStackTrace();
				return false;
			}

			if (bResultado) {
				if (lcState != LCS_SELECT)
					setState(LCS_SELECT);
			} else {
				if (readOnly) {
					limpaCampos(bAutoLimpaPK);
				} else {
					if (lcState != LCS_INSERT && bPodeIns) {
						if (!mensInserir)
							insert(false);
						else if (Funcoes.mensagemConfirma(cOwner,
								"Registro não encontrado! Inserir?") == 0) {
							insert(false);
						}
					} else {
						bResultado = false;
					}
				}
			}
		}
		fireAfterCarrega(bResultado);
		return bResultado;
	}

	public void limpaTudo() {
		if (vCache != null)
			vCache.clear();
		if (getComponentCount() > 0)
			removeAll();
		if (lcMaster != null) {
			lcMaster.removeDetalhe(this);
		}
		lcMaster = null;
		if (vDescFK != null)
			vDescFK.clear();
		if (vTxtValor != null)
			vTxtValor.clear();
		sMascs = new String[1];
	}

	public void removeDetalhe(ListaCampos lc) {
		if (vLcDetalhe != null)
			vLcDetalhe.remove(lc);
	}

	public void limpaDetalhes() {
		if (vLcDetalhe != null)
			for (int i = 0; i < vLcDetalhe.size(); i++) {
				if (vLcDetalhe.elementAt(i) instanceof ListaCampos)
					vLcDetalhe.elementAt(i).limpaCampos(true);
			}
	}

	public String getNovoCodigo() {
		String retorno = "1";
		iParamMax = 1;
		if (bAutoInc) {
			try {
				if (con == null)
					Funcoes.mensagemInforma(cOwner, "Não criou a conexão"); // JOptionPane.showMessageDialog(
				// null,
				// "Não
				// criou
				// a
				// conexão");
				sqlMax = con.prepareStatement(sSQLMax);
				if (bUsaME) {
					sqlMax.setInt(iParamMax, iCodEmp);
					iParamMax++;
				}
				if (bUsaME && !bTiraFI) {
					sqlMax.setInt(iParamMax, iCodFilial);
					iParamMax++;
				}
				if (bDetalhe) {
					montaPostMax(this);
				}

				rsMax = sqlMax.executeQuery();

				if (rsMax == null)
					Funcoes.mensagemInforma(cOwner, "RS NULL");// JOptionPane.showMessageDialog(
				else {
					try {
						while (rsMax.next()) {
							retorno = rsMax.getString("MAX");
						}

						if (retorno.trim().length() <= 0) {
							retorno = "1";
						} else {
							retorno = "" + (Integer.parseInt(retorno) + 1);
						}
					} catch (Exception ex) {
						retorno = "1";
					}
				}
				if ((bQueryCommit) && (bPodeCommit))
					con.commit(); // MOD
			} catch (SQLException err) {
				Funcoes.mensagemErro(cOwner,
						"Erro do getNovoCodigo da Tabela: " + sTabela.toLowerCase() + "\n"
								+ err.getMessage());
				err.printStackTrace();
			}
		} else
			retorno = "";
		return retorno;
	}

	public String retPK() {
		GuardaCampo comp = null;
		String retorno = "";
		for (int i = 0; i < getComponentCount(); i++) {
			comp = (GuardaCampo) getComponent(i);
			if (comp.ehPK()) {
				retorno = comp.getNomeCampo();
				break;
			}
		}
		return retorno;
	}

	public static int getMasterFilial(String sT) {
		int iRet = Aplicativo.tbObjetos.getUsoMe(sT) ? Aplicativo.iCodFilialMz
				: Aplicativo.iCodFilial;
		return iRet;
	}

	public void setConexao(DbConnection cn) {
		con = cn;
		// if (bUsaME)
		// iCodFilial = getMasterFilial(sTabela);
		// System.out.println("[TABELA: " + sTabela + ", Filial F: " +
		// Aplicativo.iCodFilial + " Filial M: " + Aplicativo.iCodFilialMz +
		// "]");
		/*if ("eqproduto".equalsIgnoreCase(sTabela)) {
			System.out.println("eqproduto - aqui deveria aparecer o insert do produto");
		}*/
		if (!"".equals(sSQLSelect))
			System.out.println("SELECT -> " + sSQLSelect);
		if (!"".equals(sSQLInsert))
			System.out.println("INSERT  -> " + sSQLInsert);
		if (!"".equals(sSQLDelete))
			System.out.println("DELETE  -> " + sSQLDelete);
		if (!"".equals(sSQLUpdate))
			System.out.println("UPDATE  -> " + sSQLUpdate);
		if (!"".equals(sSQLTab))
			System.out.println("TAB  -> " + sSQLTab);
		if (!"".equals(sSQLMax))
			System.out.println("MAX  -> " + sSQLMax + "\n");
	}

	public void limpaCampos(boolean bLimpaPK) {
		GuardaCampo comp = null;
		for (int i = 0; i < getComponentCount(); i++) {
			comp = (GuardaCampo) getComponent(i);
			if (comp.ehPK()) {
				if (bLimpaPK) {
					try {
						comp.limpa();
					} catch (Exception e) {
						new ExceptionLimpaComponent(
								"Erro ao limpar componente da PK");
					}
				}
			} else {
				try {
					comp.limpa();
				} catch (Exception e) {
					new ExceptionLimpaComponent("Erro ao limpar componente");
				}

			}
		}
	}

	public Campo getCampo(String sNome) {
		Campo retorno = null;
		GuardaCampo comp = null;
		for (int i = 0; i < getComponentCount(); i++) {
			comp = (GuardaCampo) getComponent(i);
			if ((comp.getNomeCampo().toLowerCase().equals(sNome.toLowerCase()))
					&& (comp.getCampo() != null)) {
				retorno = comp.getCampo();
				break;
			}
		}
		return retorno;
	}

	public boolean montaPostCircular5(ListaCampos lc) {
		GuardaCampo comp = null;
		ListaCampos lcM = lc;
		if (lcM != null) {
			try {
				for (int i = 0; i < lcM.getComponentCount(); i++) {
					comp = (GuardaCampo) lcM.getComponent(i);
					if (comp.ehPK()) {
						if (comp.ehNulo()) {
							// System.out.println("Campo nulo : " +
							// comp.getNomeCampo());
							// System.out.println("ListaCampos: " +
							// this.getNomeTabela());
							return false;
						}
						if (Funcoes.contaChar(sSQLSelect, '?') >= iParamC) {
							if (comp.getTipo() == JTextFieldPad.TP_INTEGER) {
								sqlLC.setInt(iParamC, comp.getVlrInteger()
										.intValue());
								vCache.addElement(comp.getVlrInteger());
								iParamC++;
							} else if (comp.getTipo() == JTextFieldPad.TP_STRING) {
								sqlLC.setString(iParamC, comp.getVlrString());
								vCache.addElement(comp.getVlrString());
								iParamC++;
							} else if (comp.getTipo() == JTextFieldPad.TP_DATE) {
								sqlLC.setDate(iParamC, Funcoes
										.dateToSQLDate(comp.getVlrDate()));
								vCache.addElement(comp.getVlrDate());
								iParamC++;
							} else if (comp.getTipo() == JTextFieldPad.TP_TIME) {
								sqlLC.setTime(iParamC, Funcoes
										.dateToSQLTime(comp.getVlrTime()));
								vCache.addElement(comp.getVlrTime());
								iParamC++;
							}

							if (comp.ehFK()) {
								ListaCampos lcExt = comp.getCampo()
										.getTabelaExterna();
								if (lcExt != null) {
									if (lcExt.getUsaME() && lcExt.getUsaFI()) {
										if (!comp.getSoLeitura()) {
											if (comp.ehNulo()) {
												if (Funcoes.contaChar(
														sSQLSelect, '?') >= iParamC)
													sqlLC.setNull(iParamC,
															Types.INTEGER);
												iParamC++;
												if (Funcoes.contaChar(
														sSQLSelect, '?') >= iParamC)
													sqlLC.setNull(iParamC,
															Types.INTEGER);
												iParamC++;
											} else {
												if (Funcoes.contaChar(
														sSQLSelect, '?') >= iParamC)
													sqlLC.setInt(iParamC,
															iCodEmp);
												iParamC++;
												if (Funcoes.contaChar(
														sSQLSelect, '?') >= iParamC)
													sqlLC.setInt(iParamC, lcExt
															.getCodFilial());
												iParamC++;
											}
										}
									}
								}
							}
						}
					}
				}
				montaPostCircular5(lcM.getMaster());
			} catch (Exception err) {
				err.printStackTrace();
			}
		}
		return true;
	}

	public void montaPostCircular(ListaCampos lc) {
		GuardaCampo comp = null;
		ListaCampos lcM = lc.getMaster();
		if (lcM != null) {
			try {
				for (int iMaster = 0; iMaster < lcM.getComponentCount(); iMaster++) {
					comp = (GuardaCampo) lcM.getComponent(iMaster);
					String nomeCampo = comp.getNomeCampo();

					// *Trace *
					if (lcState==LCS_EDIT) {
						if (fieldsUpdate.get(nomeCampo) != null) {
						/*	if ("fnsublanca".equalsIgnoreCase(sTabela)) {
								System.out.print("\nCampo: " + nomeCampo + " ");
								if (comp.ehNulo()) {
									System.out.print("valor: null");
								} else {
									System.out.print(", valor: "+comp.getVlrString());
								}
							}*/
							Integer iParamPostAnt = iParamPost;
							iParamPost = (Integer) fieldsUpdate.get(nomeCampo);
							if (iParamPost == null) {
								iParamPost = iParamPostAnt;
							}
						} 
					} else {
						if (fieldsInsert.get(nomeCampo) != null) {
							/*if ("fnsublanca".equalsIgnoreCase(sTabela)) {
								System.out.print("\nCampo: " + nomeCampo + " ");
								if (comp.ehNulo()) {
									System.out.print("valor: null");
								} else {
									System.out.print(", valor: "+comp.getVlrString());
								}
							}*/
							Integer iParamPostAnt = iParamPost;
							iParamPost = (Integer) fieldsInsert.get(nomeCampo);
							if (iParamPost == null) {
								iParamPost = iParamPostAnt;
							}
						} 
					}
/*					if ("fnsublanca".equalsIgnoreCase(sTabela)) {
						System.out.println(", parametro: "+iParamPost);
					}
*/
					if (comp.ehPK()) {
						if (comp.ehNulo()) {
							if (comp.getTipo() == JTextFieldPad.TP_INTEGER) {
								sqlLC.setNull(iParamPost, Types.INTEGER);
							} else if (comp.getTipo() == JTextFieldPad.TP_STRING) {
								sqlLC.setNull(iParamPost, Types.CHAR);
							} else if (comp.getTipo() == JTextFieldPad.TP_DECIMAL) {
								sqlLC.setNull(iParamPost, Types.DECIMAL);
							}
							/*
							 * else if (comp.getTipo() ==
							 * JTextFieldPad.TP_NUMERIC) {
							 * sqlLC.setNull(iParamPost, Types.NUMERIC);
							 * } else if (comp.getTipo() ==
							 * JTextFieldPad.TP_DOUBLE) {
							 * sqlLC.setNull(iParamPost, Types.DOUBLE);
							 * }
							 */
							else if (comp.getTipo() == JTextFieldPad.TP_DATE) {
								sqlLC.setNull(iParamPost, Types.DATE);
							} else if (comp.getTipo() == JTextFieldPad.TP_TIME) {
								sqlLC.setNull(iParamPost, Types.TIME);
							} else if (comp.getTipo() == JTextFieldPad.TP_BYTES) {
								if (((PainelImagem) comp.getComponente()).foiAlterado()) {
									sqlLC.setNull(iParamPost, Types.BINARY);
								} else {
									sqlLC.setNull(iParamPost, Types.BLOB);
								}
							}
						} else {
							if (comp.getTipo() == JTextFieldPad.TP_INTEGER) {
								sqlLC.setInt(iParamPost, comp.getVlrInteger().intValue());
							} else if (comp.getTipo() == JTextFieldPad.TP_STRING) {
								sqlLC.setString(iParamPost,comp.getVlrString());
							} else if (comp.getTipo() == JTextFieldPad.TP_DECIMAL) {
								sqlLC.setBigDecimal(iParamPost,comp.getVlrBigDecimal());
							}
							/*
							 * else if (comp.getTipo() ==
							 * JTextFieldPad.TP_NUMERIC) {
							 * sqlLC.setBigDecimal(iParamPost,
							 * comp.getVlrBigDecimal()); } else if
							 * (comp.getTipo() ==
							 * JTextFieldPad.TP_DOUBLE) {
							 * sqlLC.setDouble(iParamPost, (
							 * comp.getVlrDouble() ).doubleValue()); }
							 */
							else if (comp.getTipo() == JTextFieldPad.TP_DATE) {
								sqlLC.setDate(iParamPost, Funcoes
										.dateToSQLDate(comp
												.getVlrDate()));
							} else if (comp.getTipo() == JTextFieldPad.TP_TIME) {
								sqlLC.setTime(iParamPost, Funcoes
										.dateToSQLTime(comp
												.getVlrTime()));
							} else if (comp.getTipo() == JTextFieldPad.TP_BYTES) {
								if (((PainelImagem) comp
										.getComponente()).foiAlterado()) {
									sqlLC.setBinaryStream(iParamPost,
											comp.getVlrBytes()
											.getInputStream(),
											comp.getVlrBytes()
											.getTamanho());
								} else {
									sqlLC.setBytes(iParamPost, comp
											.getVlrBytes().getBytes());
								}
							}

						}

						iParamPost++;
						if (comp.ehFK()) {
							String sSigla = comp.getCampo().getTabelaExterna() != null 
									? comp.getCampo().getTabelaExterna().getSigla() 
									: comp.getCampo().getListaCampos().getSigla();
							if (sSigla != null) {
								if (sSigla.length() > 0) {
									ListaCampos lcExt = comp.getCampo().getTabelaExterna();
									Integer iParamPostCodemp = null;
									Integer iParamPostCodfilial = null;
									String campoCodemp = "codemp" + sSigla;
									String campoCodfilial = "codfilial" + sSigla;
									if (lcState==LCS_INSERT) {
										iParamPostCodemp = fieldsInsert.get(campoCodemp);
										iParamPostCodfilial = fieldsInsert.get(campoCodfilial);
									} else {
										iParamPostCodemp = fieldsUpdate.get(campoCodemp);
										iParamPostCodfilial = fieldsUpdate.get(campoCodfilial);
									}
									sqlLC.setInt(iParamPostCodemp,lcExt.getCodEmp());
									sqlLC.setInt(iParamPostCodfilial,lcExt.getCodFilial());
									/*if ("eqproduto".equals(sTabela.toLowerCase())) {
										System.out.print("\nCampo: "+campoCodemp);
										System.out.print(lcExt.getCodEmp());
										System.out.println("Param: "+iParamPostCodemp);
										System.out.print("\nCampo: "+campoCodfilial);
										System.out.print(lcExt.getCodFilial());
										System.out.println("Param: "+iParamPostCodfilial);
									}
									*/
									iParamPost = iParamPostCodfilial+1;
								}
							}
						}
					}
				}
				montaPostCircular(lcM);
			} catch (Exception err) {
				err.printStackTrace();
			}
		}

	}

	public boolean post() {
		boolean bRetorno = true;
		boolean bParam = false;
		boolean cancelAfterPost = false;
		iParamPost = 1;
		GuardaCampo comp = null;
		fireBeforePost();
		if (bCancelPost) {
			cancelAfterPost = true;
			bCancelPost = false;
			bRetorno = false;

		}
		boolean bParamMaster = true;
		/*if (sTabela.equalsIgnoreCase("ppitop")) {
			System.out.println("Trace");
		}*/
		if (bRetorno) {
			try {
				if (lcState == LCS_EDIT) {
					sqlLC = con.prepareStatement(sSQLUpdate);
				} else if (lcState == LCS_INSERT) {
					/*if (sTabela.equalsIgnoreCase("eqproduto")) {
						System.out.println(sSQLInsert);
					}*/
					sqlLC = con.prepareStatement(sSQLInsert);
				}
				if ((lcState == LCS_EDIT) || (lcState == LCS_INSERT)) {
					for (int i = 0; i < getComponentCount(); i++) {
						comp = (GuardaCampo) getComponent(i);
						if ((comp.getRequerido()) && (comp.getCampo() != null)) {
							if (comp.getCampo().getText().trim().length() < 1) {
								if (comp.ehFK()) {
									Funcoes.mensagemInforma(cOwner,
											"O campo \""
													+ comp.getLabel()
													+ " "
													+ comp.getDescFK()
													.getLabel()
													+ "\" é requerido!");
									comp.getComponente().requestFocus();
									bRetorno = false;
									return false;
								}
								Funcoes.mensagemInforma(cOwner, "O campo \""
										+ comp.getLabel() + "\" é requerido!");
								comp.getComponente().requestFocus();
								bRetorno = false;
								return false;
							}
						}
						if (comp.getCampo() != null) {
							if (comp.getCampo().getMascara() == JTextFieldPad.MC_CNPJ) {
								if (!Funcoes.ValidaCNPJ(comp.getVlrString())) {
									Funcoes.mensagemErro(cOwner,
											"CNPJ inválido!");
									comp.getComponente().requestFocus();
									bRetorno = false;
									return false;
								}
							} else if (comp.getCampo().getMascara() == JTextFieldPad.MC_CPF
									&& isValidarcpf()) {
								if (!Funcoes.ValidaCPF(comp.getCampo()
										.getVlrString())) {
									Funcoes.mensagemErro(cOwner,
											"CPF inválido!");
									comp.getCampo().requestFocus();
									bRetorno = false;
									return false;
								}
							}
						}
						if (lcState == LCS_INSERT) {
							if ((bUsaME) && (iParamPost != null) && (iParamPost == 1)) {
								sqlLC.setInt(iParamPost, iCodEmp);
								iParamPost++;
								if (!bTiraFI) {
									sqlLC.setInt(iParamPost, iCodFilial);
									iParamPost++;
								}
							}
							bParam = true;
							if ((bDetalhe) && (lcMaster != null) && (bParamMaster)) {
								montaPostCircular(this);
							}
							bParamMaster = false;
						} else {
							if ((comp.ehPK()) || (comp.getSoLeitura()))
								bParam = false;
							else
								bParam = true;
						}
						if (bParam) {
							comp = (GuardaCampo) getComponent(i);
							if (!comp.getSoLeitura()) {

								String nomeCampo = comp.getNomeCampo();

								if (lcState == LCS_INSERT) {
									if (fieldsInsert.get(nomeCampo) != null) {
/*										if ("fnsublanca"	.equalsIgnoreCase(sTabela.toLowerCase())) {
											System.out.println("Campo: "+ nomeCampo + " ");
											if (comp.ehNulo()) {
												System.out.print("null\n");
											} else {
												System.out.print(comp.getVlrString()+"\n");
											}
										}*/
										Integer iParamPostAnt = iParamPost;
										iParamPost = (Integer) fieldsInsert.get(nomeCampo);
										if (iParamPost == null) {
											iParamPost = iParamPostAnt;
										}
									}
								} else {
									if (fieldsUpdate.get(nomeCampo) != null) {
/*										if ("fnsublanca".equalsIgnoreCase(sTabela)) {
											System.out.println("Campo: "+ nomeCampo + " ");
											if (comp.ehNulo()) {
												System.out.print("null\n");
											} else {
												System.out.print(comp.getVlrString()+"\n");
											}
										}*/
										Integer iParamPostAnt = iParamPost;
										iParamPost = (Integer) fieldsUpdate.get(nomeCampo);
										if (iParamPost == null) {
											iParamPost = iParamPostAnt;
										}
									}
									
								}
								if (comp.ehNulo()) {
									if (comp.getTipo() == JTextFieldPad.TP_INTEGER) {
										sqlLC.setNull(iParamPost, Types.INTEGER);
									} else if (comp.getTipo() == JTextFieldPad.TP_STRING) {
										sqlLC.setNull(iParamPost, Types.CHAR);
									} else if (comp.getTipo() == JTextFieldPad.TP_DECIMAL) {
										sqlLC.setNull(iParamPost, Types.DECIMAL);
									}
									/*
									 * else if (comp.getTipo() ==
									 * JTextFieldPad.TP_NUMERIC) {
									 * sqlLC.setNull(iParamPost, Types.NUMERIC);
									 * } else if (comp.getTipo() ==
									 * JTextFieldPad.TP_DOUBLE) {
									 * sqlLC.setNull(iParamPost, Types.DOUBLE);
									 * }
									 */
									else if (comp.getTipo() == JTextFieldPad.TP_DATE) {
										sqlLC.setNull(iParamPost, Types.DATE);
									} else if (comp.getTipo() == JTextFieldPad.TP_TIME) {
										sqlLC.setNull(iParamPost, Types.TIME);
									} else if (comp.getTipo() == JTextFieldPad.TP_BYTES) {
										if (((PainelImagem) comp
												.getComponente()).foiAlterado()) {
											sqlLC.setNull(iParamPost,
													Types.BINARY);
										} else {
											sqlLC.setNull(iParamPost,
													Types.BLOB);
										}
									}
								} else {
									if (comp.getTipo() == JTextFieldPad.TP_INTEGER) {
										if (comp.ehNulo())
											sqlLC.setNull(iParamPost,
													Types.INTEGER);
										else
											sqlLC.setInt(iParamPost, comp
													.getVlrInteger().intValue());
									} else if (comp.getTipo() == JTextFieldPad.TP_BOOLEAN) {
										sqlLC.setString(iParamPost,
												comp.getVlrString());
									} else if (comp.getTipo() == JTextFieldPad.TP_STRING) {
										sqlLC.setString(iParamPost,
												comp.getVlrString());
									} else if (comp.getTipo() == JTextFieldPad.TP_DECIMAL) {
										sqlLC.setBigDecimal(iParamPost,
												comp.getVlrBigDecimal());
									}
									/*
									 * else if (comp.getTipo() ==
									 * JTextFieldPad.TP_NUMERIC) {
									 * sqlLC.setBigDecimal(iParamPost,
									 * comp.getVlrBigDecimal()); } else if
									 * (comp.getTipo() ==
									 * JTextFieldPad.TP_DOUBLE) {
									 * sqlLC.setDouble(iParamPost, (
									 * comp.getVlrDouble() ).doubleValue()); }
									 */
									else if (comp.getTipo() == JTextFieldPad.TP_DATE) {
										sqlLC.setDate(iParamPost, Funcoes
												.dateToSQLDate(comp
														.getVlrDate()));
									} else if (comp.getTipo() == JTextFieldPad.TP_TIME) {
										sqlLC.setTime(iParamPost, Funcoes
												.dateToSQLTime(comp
														.getVlrTime()));
									} else if (comp.getTipo() == JTextFieldPad.TP_BYTES) {
										if (!((PainelImagem) comp
												.getComponente()).ehNulo()) {
											if (((PainelImagem) comp
													.getComponente())
													.foiAlterado()) {
												sqlLC.setBinaryStream(
														iParamPost,
														comp.getVlrBytes()
														.getInputStream(),
														comp.getVlrBytes()
														.getTamanho());
											} else {
												sqlLC.setBytes(iParamPost, comp
														.getVlrBytes()
														.getBytes());
											}
										}
									} else {
										sqlLC.setNull(iParamPost, Types.BLOB);
									}
								}
								iParamPost++;
								if (comp.ehFK()) {
									ListaCampos lcExt = comp.getCampo()
											.getTabelaExterna();
									if (lcExt != null) {
										if (lcExt.getUsaME()
												&& lcExt.getUsaFI()) {
											if (!comp.getSoLeitura()) {
												String sigla = lcExt.getSigla();
												Integer iParamPostCodemp = null;
												Integer iParamPostCodfilial = null;
												if (sigla == null) {
													sigla = "";
												}
												if (lcState==LCS_INSERT) {
													String campoCodemp = "codemp" + sigla;
													String campoCodfilial = "codfilial"	+ sigla;
													iParamPostCodemp = fieldsInsert.get(campoCodemp);
													iParamPostCodfilial = fieldsInsert.get(campoCodfilial);
												}
												else {
													String campoCodemp = "codemp" + sigla;
													String campoCodfilial = "codfilial"	+ sigla;
													iParamPostCodemp = fieldsUpdate.get(campoCodemp);
													iParamPostCodfilial = fieldsUpdate.get(campoCodfilial);
												}
												if (comp.ehNulo()) {
													sqlLC.setNull(iParamPostCodemp, Types.INTEGER);
													sqlLC.setNull(iParamPostCodfilial, Types.INTEGER);
												} else {
													sqlLC.setInt( iParamPostCodemp,	iCodEmp);
													sqlLC.setInt( iParamPostCodfilial, lcExt.getCodFilial());
												}
												iParamPost = iParamPostCodfilial;
												/*if (iParamPost>=15) {
													System.out.println("Trace");
												}*/

											}
											iParamPost++;
										}
									}
								}
							}
						}
					}
				}
				if (lcState == LCS_EDIT) {
					//iParamPost = fieldsUpdate.size()+1; 
					for (int i = 0; i < getComponentCount(); i++) {
						comp = (GuardaCampo) getComponent(i);
						if ((comp.getRequerido()) && (comp.getCampo() != null)) {
							if (comp.getCampo().getText().trim().length() < 1) {
								if (comp.ehFK()) {
									Funcoes.mensagemInforma(cOwner,
											"O campo \""
													+ comp.getLabel()
													+ " "
													+ comp.getDescFK()
													.getLabel()
													+ "\" é requerido!");
									comp.getComponente().requestFocus();
									bRetorno = false;
									break;
								}
								Funcoes.mensagemInforma(cOwner, "O campo \""
										+ comp.getLabel()
										+ "\" é requerido ! ! !");
								comp.getComponente().requestFocus();
								bRetorno = false;
								break;
							}
						}
						if ((bDetalhe) && (lcMaster != null) && (bParamMaster)) {
							montaPostCircular(this);
						}
						bParamMaster = false;
						comp = (GuardaCampo) getComponent(i);
						String tmpCampo = comp.getNomeCampo().toLowerCase();
						if (fieldsUpdate.get(tmpCampo) != null) {
							Integer iParamPostAnt = iParamPost;
							iParamPost = (Integer) fieldsUpdate.get(tmpCampo);
							if (iParamPost == null) {
								iParamPost = iParamPostAnt;
							}
						} 

						if (comp.ehPK()) {
							if (comp.getTipo() == JTextFieldPad.TP_INTEGER) {
								sqlLC.setInt(iParamPost, comp.getVlrInteger()
										.intValue());
							} else if (comp.getTipo() == JTextFieldPad.TP_STRING) {
								sqlLC.setString(iParamPost, comp.getVlrString());
							} else if (comp.getTipo() == JTextFieldPad.TP_DATE) {
								sqlLC.setDate(iParamPost, Funcoes
										.dateToSQLDate(comp.getVlrDate()));
							} else if (comp.getTipo() == JTextFieldPad.TP_TIME) {
								sqlLC.setTime(iParamPost, Funcoes
										.dateToSQLTime(comp.getVlrTime()));
							}
							iParamPost++;
							if (comp.ehFK()) {
								ListaCampos lcExt = comp.getCampo().getTabelaExterna();
								if (lcExt != null) {
									if (lcExt.getUsaME() && lcExt.getUsaFI()) {
										if (!comp.getSoLeitura()) {
											String campoCodemp = "codemp"+lcExt.getSigla().toLowerCase();
											String campoCodfilial = "codfilial"+lcExt.getSigla().toLowerCase();
											Integer iParamCodemp = fieldsUpdate.get(campoCodemp);
											Integer iParamCodfilial = fieldsUpdate.get(campoCodfilial);
											if (comp.ehNulo()) {
												if (iParamCodemp!=null) {
													iParamPost = iParamCodemp;
													sqlLC.setNull(iParamPost,Types.INTEGER);
												}
												if (iParamCodfilial!=null) {
													iParamPost = iParamCodfilial;
													sqlLC.setNull(iParamPost,Types.INTEGER);
												}
											} else {
												if (iParamCodemp!=null) {
													iParamPost = iParamCodemp;
													sqlLC.setInt(iParamPost,iCodEmp);
												}
												if (iParamCodfilial!=null) {
													iParamPost = iParamCodfilial;
													sqlLC.setInt(iParamPost,lcExt.getCodFilial());
												}
											}
										}
										iParamPost++;
									}
								}
							}
						}
					}
					if (bUsaME) {
						if (fieldsUpdate.get("codemp") != null) {
							Integer iParamPostAnt = iParamPost;
							iParamPost = (Integer) fieldsUpdate.get("codemp");
							if (iParamPost == null) {
								iParamPost = iParamPostAnt;
							}
						} 
						sqlLC.setInt(iParamPost, iCodEmp);
						iParamPost++;
					}
					if (bUsaME && !bTiraFI) {
						if (fieldsUpdate.get("codfilial") != null) {
							Integer iParamPostAnt = iParamPost;
							iParamPost = (Integer) fieldsUpdate.get("codfilial");
							if (iParamPost == null) {
								iParamPost = iParamPostAnt;
							}
						} 
						sqlLC.setInt(iParamPost, iCodFilial);
						//iParamPost ++;
					}
				}
				if (sTabela.equalsIgnoreCase("ppitop")) {
					System.out.println(sTabela);
					System.out.println("--------------------------------");
					System.out.println(sSQLUpdate);
					for (String teste: fieldsUpdate.keySet()) {
						System.out.println(teste +":"+ fieldsUpdate.get(teste));
					}
					System.out.println("--------------------------------");
					//for (sqlLC.)

				}
				sqlLC.executeUpdate();
				if (bPodeCommit)
					con.commit();
				if (bDetalhe) {
					if (lcState == LCS_EDIT) {
						carregaGridEdit(bRetorno);
					} else {
						carregaGridInsert(bRetorno);
					}
				}
				setState(LCS_SELECT);
				if (bQueryInsert) {
					try {
						carregaDados();
					} catch (Exception e) {
						new ExceptionCarregaDados(
								"Erro ao carregar dados no post");
					}
				}
				bRetorno = true;
			} catch (SQLException err) {
				Funcoes.mensagemErro(
						cOwner,
						"Erro ao salvar dados! \nTabela: "
								+ (sTabela == null ? "INDEFINIDA " : sTabela)
								+ "\n"
								+ "Campo: "
								+ (comp == null || comp.getNomeCampo() == null ? "INDEFINIDO "
										: comp.getNomeCampo())
										+ "\nTipo de erro: " + err.getMessage());
				err.printStackTrace();
				return false;
			}
		}
		boolean caneditold = this.canedit;
		try {
			this.canedit = true;
			if (!cancelAfterPost) {
				cancelAfterPost = false;
				fireAfterPost(bRetorno);
			}
		} finally {
			this.canedit = caneditold;
		}
		return bRetorno;
	}

	public boolean edit() {
		boolean bRetorno = true;
		fireBeforeEdit();
		if (bCancelEdit) {
			bCancelEdit = false;
			bRetorno = false;
		} else if (lcState == LCS_NONE)
			insert(bAutoInc);
		else if (lcState == LCS_SELECT)
			setState(LCS_EDIT);
		fireAfterEdit(bRetorno);
		// System.out.println(this.getNomeTabela());
		return bRetorno;
	}

	public boolean insert(boolean bNovaPK) {
		boolean bInsert = true;
		fireBeforeInsert();
		if (bCancelInsert || !bPodeIns) {
			bCancelInsert = false;
			bInsert = false;
		}
		if (bInsert) {
			if (bMaster) {
				for (int i = 0; i < vLcDetalhe.size(); i++) {
					if (vLcDetalhe.elementAt(i).getTab() != null) {
						vLcDetalhe.elementAt(i).getTab().limpa();
					}
					vLcDetalhe.elementAt(i).limpaCampos(true);
					if (vLcDetalhe.elementAt(i).nvLC != null) {
						String sNameDet = vLcDetalhe.elementAt(i).nvLC
								.getName();
						String sNameMaster = this.nvLC.getName();
						if (!((sNameDet == null) || (sNameMaster == null))) {
							if (!sNameDet.equals(sNameMaster))
								vLcDetalhe.elementAt(i).setState(LCS_NONE);
						}
					}
				}
			}
			boolean bPost = true;
			if ((lcState == LCS_EDIT) | (lcState == LCS_INSERT)) {
				bPost = post();
			}
			if (bPost) {
				Campo CampoPK = getCampo(sPK);
				limpaCampos(bNovaPK);
				setState(LCS_INSERT);
				if ((CampoPK != null) && (bNovaPK)) {
					CampoPK.setVlrString(getNovoCodigo());
				}
			}
		}
		fireAfterInsert(bInsert);
		return bInsert;
	}

	public void setConfirmaDelecao(boolean bConfirma) {
		bConfirmaDelecao = bConfirma;
	}

	public void montaDeleteCircular(ListaCampos lc) {
		GuardaCampo comp = null;
		ListaCampos lcM = lc.getMaster();
		if (lcM != null) {
			try {
				for (int iMaster = 0; iMaster < lcM.getComponentCount(); iMaster++) {
					comp = (GuardaCampo) lcM.getComponent(iMaster);
					if (comp.ehPK()) {
						if (comp.ehNulo()) {
							if (comp.getTipo() == JTextFieldPad.TP_INTEGER) {
								sqlLC.setNull(iParamDelete, Types.INTEGER);
							} else if (comp.getTipo() == JTextFieldPad.TP_STRING) {
								sqlLC.setNull(iParamDelete, Types.CHAR);
							} else if (comp.getTipo() == JTextFieldPad.TP_DATE) {
								sqlLC.setNull(iParamDelete, Types.DATE);
							} else if (comp.getTipo() == JTextFieldPad.TP_TIME) {
								sqlLC.setNull(iParamDelete, Types.TIME);
							}
						} else {
							if (comp.getTipo() == JTextFieldPad.TP_INTEGER) {
								sqlLC.setInt(iParamDelete, comp.getVlrInteger()
										.intValue());
							} else if (comp.getTipo() == JTextFieldPad.TP_STRING) {
								sqlLC.setString(iParamDelete,
										comp.getVlrString());
							} else if (comp.getTipo() == JTextFieldPad.TP_DATE) {
								sqlLC.setDate(iParamDelete, Funcoes
										.dateToSQLDate(comp.getVlrDate()));
							} else if (comp.getTipo() == JTextFieldPad.TP_TIME) {
								sqlLC.setTime(iParamDelete, Funcoes
										.dateToSQLTime(comp.getVlrTime()));
							}
						}
						iParamDelete++;
					}
				}
				montaDeleteCircular(lcM);
			} catch (Exception err) {
				err.printStackTrace();
			}
		}

	}

	public boolean delete() {
		iParamDelete = 1;
		boolean bRetorno = true;
		boolean bDeletar = true;
		GuardaCampo comp = null;
		fireBeforeDelete();
		if (bCancelDelete || !bPodeExc) {
			bCancelDelete = false;
			bRetorno = false;
		}
		if (bRetorno) {
			if ((lcState == LCS_EDIT) | (lcState == LCS_SELECT)) {
				if (bConfirmaDelecao) {
					if (Funcoes.mensagemConfirma(cOwner, "Confirma exclusão?") != 0) {
						bDeletar = false;
					}
				}
				if (bDeletar) {
					boolean bParamMaster = true;
					try {
						sqlLC = con.prepareStatement(sSQLDelete);
						if (bUsaME) {
							sqlLC.setInt(iParamDelete, iCodEmp);
							iParamDelete++;
						}
						if (bUsaME && !bTiraFI) {
							sqlLC.setInt(iParamDelete, iCodFilial);
							iParamDelete++;
						}
						for (int i = 0; i < getComponentCount(); i++) {
							comp = (GuardaCampo) getComponent(i);
							if ((bDetalhe) && (lcMaster != null)
									&& (bParamMaster)) {
								montaDeleteCircular(this);
							}
							bParamMaster = false;
							comp = (GuardaCampo) getComponent(i);
							if (comp.ehPK()) {
								/*System.out.println("CAMPO: "+ comp.getNomeCampo() + " IPARAM: "	
							     + iParamDelete + " VALOR: "	+ comp.getCampo().getVlrInteger());*/
								if (comp.getCampo().getTipoCampo() == JTextFieldPad.TP_INTEGER) {
									sqlLC.setInt(iParamDelete, comp.getCampo()
											.getVlrInteger().intValue());
								} else if (comp.getCampo().getTipoCampo() == JTextFieldPad.TP_STRING) {
									sqlLC.setString(iParamDelete, comp
											.getCampo().getVlrString());
								} else if (comp.getTipo() == JTextFieldPad.TP_DATE) {
									sqlLC.setDate(iParamDelete, Funcoes
											.dateToSQLDate(comp.getVlrDate()));
								} else if (comp.getTipo() == JTextFieldPad.TP_TIME) {
									sqlLC.setTime(iParamDelete, Funcoes
											.dateToSQLTime(comp.getVlrTime()));
								}
								iParamDelete++;
								if (comp.ehFK()) {
									ListaCampos lcExt = comp.getCampo()
											.getTabelaExterna();
									if (lcExt != null) {
										if (lcExt.getUsaME()
												&& lcExt.getUsaFI()) {
											if (!comp.getSoLeitura())
												/*System.out.println("FILIAL: "
														+ comp.getNomeCampo()
														+ " IPARAM: "
														+ iParamDelete
														+ " VALOR: "
														+ lcExt.getCodFilial());*/
											if (comp.ehNulo()) {
												sqlLC.setNull(iParamDelete,
														Types.INTEGER);
												iParamDelete++;
												sqlLC.setNull(iParamDelete,
														Types.INTEGER);
											} else {
												sqlLC.setInt(iParamDelete,
														iCodEmp);
												iParamDelete++;
												sqlLC.setInt(iParamDelete,
														lcExt.getCodFilial());
											}
											iParamDelete++;
										}
									}
								}
							}
						}
						sqlLC.execute();
						if (bPodeCommit)
							con.commit();
						if (bDetalhe)
							carregaGridDelete(bRetorno);
						bRetorno = true;
						limpaCampos(true);
						setState(LCS_NONE);
					} catch (SQLException err) {
						if (err.getErrorCode() == FB_FK_INVALIDA) {

							StringBuilder mensagem = new StringBuilder(
									"Este registro não pode ser apagado, pois possui vínculo com a tabela: ");

							String errormessage = err.getMessage();

							String tabela = errormessage.substring(errormessage
									.indexOf("on table") + 9);

							mensagem.append(tabela + "!\n");

							mensagem.append("Mensagem original:\n");

							mensagem.append(errormessage.substring(errormessage
									.indexOf("violation")));

							Funcoes.mensagemErro(cOwner, mensagem.toString());

							bRetorno = false;
						} else if (err.getErrorCode() == FB_PK_DUPLA) {
							Funcoes.mensagemErro(cOwner,
									"A chave de registro duplicada. Escolha outro código!");
							bRetorno = false;
						} else {
							Funcoes.mensagemErro(cOwner,
									"Erro ao deletar registro na tabela "
											+ sTabela + "\n" + err.getMessage());
							err.printStackTrace();
							bRetorno = false;
						}
					}
				}
			}
		}
		fireAfterDelete(bRetorno);
		return bRetorno;
	}

	public void cancLerCampo(int ind, boolean bVal) {
		bCamposCanc[ind] = bVal;
	}

	public boolean cancel(boolean carrega) {
		int iContaVal = 0;
		boolean bRetorno = true;
		GuardaCampo comp = null;
		fireBeforeCancel();
		if (bCancelCancel) {
			bCancelCancel = false;
			bRetorno = false;
		} else if (vCache.size() < 1) {
			limpaCampos(true);
			setState(LCS_NONE);
			bRetorno = false;
		}
		if (bRetorno) {
			for (int i = 0; i < getComponentCount(); i++) {
				comp = (GuardaCampo) getComponent(i);
				if (comp.ehPK()) {
					if (vCache.size()>iContaVal) {
						if (vCache.elementAt(iContaVal) instanceof Integer)
							comp.setVlrInteger((Integer) vCache
									.elementAt(iContaVal));
						else if (vCache.elementAt(iContaVal) instanceof String)
							comp.setVlrString((String) vCache.elementAt(iContaVal));
						iContaVal++;
					}
				}
			}
			if ((carrega) & (lcState != LCS_INSERT)) {
				setState(LCS_SELECT);
				try {
					carregaDados();
				} catch (Exception e) {
					new ExceptionCarregaDados(
							"Erro ao carregar dados no cancel");
				}
			} else if ((carrega) & (lcState == LCS_INSERT)) {
				limpaCampos(true);
				setState(LCS_NONE);
			}
		}
		fireAfterCancel(bRetorno);
		return bRetorno;
	}

	private void carregaGridInsert(boolean b) {
		Vector<Object> vVals = new Vector<Object>();
		int iContaDesc = 0;
		if ((b) && (tab != null)) {
			for (int i = 0; i < (getComponentCount() + iNumDescs); i++) {
				if ((vDescFK.size() > 0) && (vDescFK.elementAt(i) != null)) {
					vVals.addElement(vDescFK.elementAt(i).getVlrString());
					iContaDesc++;
				} else if (((GuardaCampo) getComponent(i - iContaDesc))
						.getTipo() == JTextFieldPad.TP_BYTES) {
					vVals.addElement("# BINÁRIO #");
				} else if (((GuardaCampo) getComponent(i - iContaDesc))
						.getTipo() == JTextFieldPad.TP_INTEGER) {
					vVals.addElement(((GuardaCampo) getComponent(i - iContaDesc))
							.getVlrInteger());
				}
				// *********
				else if (((GuardaCampo) getComponent(i - iContaDesc))
						.getComponente() instanceof JCheckBoxPad) {
					if ("S".equals(((GuardaCampo) getComponent(i - iContaDesc))
							.getVlr())) {
						vVals.addElement(new Boolean(true));
					} else {
						vVals.addElement(new Boolean(false));
					}
				}
				// **********
				else {
					vVals.addElement(((GuardaCampo) getComponent(i - iContaDesc))
							.getVlrString());
				}
			}
			tab.adicLinha(vVals);
		}
	}

	private void carregaGridEdit(boolean b) {
		int iContaDesc = 0;
		if ((b) && (tab != null)) {
			int iLin = getNumLinha();
			if (iLin >= 0) {
				for (int i = 0; i < (getComponentCount() + iNumDescs); i++) {
					if ((vDescFK.size() > 0) & (vDescFK.elementAt(i) != null)) {
						tab.setValor(vDescFK.elementAt(i).getText(), iLin, i);
						iContaDesc++;
					} else if (((GuardaCampo) getComponent(i - iContaDesc))
							.getTipo() == JTextFieldPad.TP_BYTES) {
						tab.setValor("# BINÁRIO #", iLin, i);
					} else if (((GuardaCampo) getComponent(i - iContaDesc))
							.getTipo() == JTextFieldPad.TP_INTEGER) {
						tab.setValor(
								((GuardaCampo) getComponent(i - iContaDesc))
								.getVlrInteger(), iLin, i);
					}
					// *********
					else if (((GuardaCampo) getComponent(i - iContaDesc))
							.getComponente() instanceof JCheckBoxPad) {
						if ("S".equals(((GuardaCampo) getComponent(i
								- iContaDesc)).getVlr())) {
							tab.setValor(new Boolean(true), iLin, i);
						} else {
							tab.setValor(new Boolean(false), iLin, i);
						}
					}
					// **********
					else {
						tab.setValor(
								((GuardaCampo) getComponent(i - iContaDesc))
								.getVlr(), iLin, i);
					}
				}
			}
		}
	}

	private void carregaGridDelete(boolean b) {
		if ((b) && (tab != null)) {
			int iLin = getNumLinha();
			tab.tiraLinha(iLin);
		}
	}

	public void cancelPost() {
		bCancelPost = true;
	}

	public void cancelInsert() {
		bCancelInsert = true;
	}

	public void cancelEdit() {
		bCancelEdit = true;
	}

	public void cancelDelete() {
		bCancelDelete = true;
	}

	public void cancelCarrega() {
		bCancelCarrega = true;
	}

	public void cancelCancel() {
		bCancelCancel = true;
	}

	public void addPostListener(PostListener pLis) {
		posLis = pLis;
	}

	public void addInsertListener(InsertListener iLis) {
		insLis = iLis;
	}

	public void addEditListener(EditListener eLis) {
		editLis = eLis;
	}

	public void addDeleteListener(DeleteListener dLis) {
		delLis = dLis;
	}

	public void addCancelListener(CancelListener cLis) {
		canLis = cLis;
	}

	public void addCarregaListener(CarregaListener cLis) {
		carLis = cLis;
	}

	private void fireBeforeInsert() {
		insLis.beforeInsert(new InsertEvent(this));
	}

	private void fireBeforeEdit() {
		editLis.beforeEdit(new EditEvent(this));
	}

	private void fireBeforeDelete() {
		delLis.beforeDelete(new DeleteEvent(this));
	}

	private void fireBeforeCancel() {
		canLis.beforeCancel(new CancelEvent(this));
	}

	private void fireBeforeCarrega() {
		carLis.beforeCarrega(new CarregaEvent(this));
	}

	private void fireBeforePost() {
		posLis.beforePost(new PostEvent(this));
	}

	private void fireAfterInsert(boolean b) {
		InsertEvent ievt = new InsertEvent(this);
		ievt.ok = b;
		insLis.afterInsert(ievt);
	}

	private void fireAfterEdit(boolean b) {
		EditEvent eevt = new EditEvent(this);
		eevt.ok = b;
		editLis.afterEdit(eevt);
	}

	private void fireAfterDelete(boolean b) {
		DeleteEvent devt = new DeleteEvent(this);
		devt.ok = b;
		delLis.afterDelete(devt);
	}

	private void fireAfterCancel(boolean b) {
		CancelEvent cevt = new CancelEvent(this);
		cevt.ok = b;
		canLis.afterCancel(cevt);
	}

	private void fireAfterPost(boolean b) {
		PostEvent pevt = new PostEvent(this);
		pevt.ok = b;
		posLis.afterPost(pevt);
	}

	private void fireAfterCarrega(boolean b) {
		// boolean caneditold = this.canedit;
		// try {
		// this.canedit = false;
		if (b) {
			if (bMaster) {
				for (ListaCampos lcTmp : vLcDetalhe) {
					// lcTmp.setCanedit(this.canedit);
					lcTmp.carregaItens();
				}
			}
		}
		CarregaEvent cevt = new CarregaEvent(this);
		cevt.ok = b;
		carLis.afterCarrega(cevt);
		// Habilita novamente o flag editável
		/*
		 * } finally { if (b) { if (bMaster) { for (ListaCampos lcTmp:
		 * vLcDetalhe ) { lcTmp.setCanedit(caneditold); } } } }
		 */
		// Desabilita flag indicando carregamento
		// if (this.getNomeTabela().equalsIgnoreCase("LFITCLFISCAL")) {
		// setCarregando(false);
		// }
	}

	public void mouseClicked(MouseEvent mevt) {
		if ((mevt.getSource() == tab) & (mevt.getClickCount() == 2)
				& (tab.getLinhaSel() >= 0)) {
			boolean caneditold = isCanedit();
			try {
				setCanedit(false);
				carregaItem(tab.getLinhaSel());
			} catch (Exception e) {
				e.printStackTrace();
				new ExceptionCarregaDados("Erro ao carregar dados do grid");
			} finally {
				setCanedit(caneditold);
			}
		}
	}

	public void beforePost(PostEvent pevt) {
	}

	public void beforeInsert(InsertEvent ievt) {
	}

	public void beforeEdit(EditEvent eevt) {
	}

	public void beforeDelete(DeleteEvent devt) {
	}

	public void beforeCancel(CancelEvent cevt) {
	}

	public void beforeCarrega(CarregaEvent cevt) {
	}

	public void afterInsert(InsertEvent ievt) {
	}

	public void afterEdit(EditEvent eevt) {
	}

	public void afterDelete(DeleteEvent devt) {
	}

	public void afterCancel(CancelEvent cevt) {
	}

	public void afterCarrega(CarregaEvent cevt) {
	}

	public void afterPost(PostEvent pevt) {
	}

	public void mouseEntered(MouseEvent mevt) {
	}

	public void mouseExited(MouseEvent mevt) {
	}

	public void mousePressed(MouseEvent mevt) {
	}

	public void mouseReleased(MouseEvent mevt) {
	}

	public void edit(EditEvent eevt) {
	}

	public DbConnection getConexao() {
		return con;
	}

	public String getNomeTabela() {
		if (sTabela != null) {
			return sTabela.toLowerCase();
		}
		return sTabela;
	}

	public void setSQLMax(String sql) {
		this.sSQLMax = sql;
	}

	/**
	 * @return Returns the sSchema.
	 */
	public String getSchema() {
		return sSchema;
	}

	/**
	 * @param schema
	 *            The sSchema to set.
	 */
	public void setSchema(String schema) {
		sSchema = schema.trim() + ".";
	}

	public boolean isCanedit() {
		return canedit;
	}

	public void setCanedit(boolean canedit) {
		this.canedit = canedit;
	}

	public boolean isMuiltiselecaoF2() {
		return muiltiselecaoF2;
	}

	public void setMuiltiselecaoF2(boolean muiltiselecaoF2) {
		this.muiltiselecaoF2 = muiltiselecaoF2;
	}

	public boolean isLoginstab() {
		return loginstab;
	}

	public void setLoginstab(boolean loginstab) {
		this.loginstab = loginstab;
	}

	// public boolean isCarregando() {
	// return carregando;
	// }

	// public void setCarregando(boolean carregando) {
	// this.carregando = carregando;
	// }
}