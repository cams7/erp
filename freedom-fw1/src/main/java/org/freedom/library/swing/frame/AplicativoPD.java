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

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JMenuItem;

import org.freedom.bmps.Icone;
import org.freedom.bmps.Imagem;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.object.EmailBean;
import org.freedom.library.business.object.Empresa;
import org.freedom.library.business.object.Tab;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.modulos.std.FPrefereGeral;

public class AplicativoPD extends Aplicativo implements ActionListener, KeyListener {

	public AplicativoPD() {

		Locale.setDefault(new Locale("pt", "BR"));
		
	}

	public AplicativoPD(String sIcone, String sSplash, int iCodSis, String sDescSis, int iCodModu, String sDescModu, String sDirImagem, final FPrincipal telaP, Class<? extends Login> cLogin) {

		if (sDirImagem != null) {
			Imagem.dirImages = sDirImagem;
			Icone.dirImages = sDirImagem;
		}
		if (System.getProperty("ARQLOG") != null)
			ligaLog(System.getProperty("ARQLOG"));
		strSplash = sSplash;
		Locale.setDefault(new Locale("pt", "BR"));
		vOpcoes = new Vector<JMenuItem>();
		vBotoes = new Vector<JButtonPad>();

		telaPrincipal = telaP;
		this.iCodSis = iCodSis;
		this.iCodModu = iCodModu;
		this.sDescSis = sDescSis;
		this.sDescModu = sDescModu;
		this.cLoginExec = cLogin;

		telaP.addWindowStateListener( new WindowStateListener() {
			
			@Override
			public void windowStateChanged(WindowEvent e) {
				System.out.println("redimensionou!");
				telaP.reposicionaImagens();
				
			}
		});
		
		
		
		imgIcone = Icone.novo(sIcone);
		telaPrincipal.setIconImage(imgIcone.getImage());

		setSplashName(sSplash);
		iniConexao();
		carregaCasasDec();
		carregaBuscaProd();
		createEmailBean();
		getMultiAlmox();
		buscaInfoUsuAtual();
		setaInfoTela();
		
		
	}

	public void setaSysdba() {

		if (getUsuario().getIdusu().toUpperCase().trim().equals("SYSDBA")) {
			iXPanel = 30;
			btAtualMenu.setBorder(null);
			btAtualMenu.setContentAreaFilled(false);
			pinBotoes.adic(btAtualMenu, 0, 0, 30, 30);

		}
		else
			iXPanel = 0;
	}

	public void setaInfoTela() {

		telaPrincipal.setIdent(sDescSis.trim() + " - " + sDescModu.trim());
		telaPrincipal.setConexao(con); // Variável de conexão da Classe

		telaPrincipal.statusBar.setUsuario(getUsuario().getIdusu());// Variavel de usuario da
		telaPrincipal.statusBar.setCodFilial(iCodFilial);
		telaPrincipal.statusBar.setNomeFilial(sNomeFilial);
		telaPrincipal.statusBar.setNumEst(iNumEst);
		telaPrincipal.statusBar.setDescEst(getDescEst());

		setaSysdba();
		infoProxy();

		telaPrincipal.adicCompInBar(pinBotoes, BorderLayout.WEST);

		btAtualMenu.addActionListener(this);
		bModoDemo = getModoDemo();

		// validaPrefere();

	}

	public void iniConexao() {

		strBanco = getParameter("banco");
		strDriver = getParameter("driver");
		bAutoCommit = "S".toUpperCase().equals(getParameter("autocommit"));

		strCharSetRel = getParameter("charSetRel");
		if (strCharSetRel == null || "".equals(strCharSetRel)) {
			strCharSetRel = "ISO8859-1";
		}

		strTemp = getParameter("temp");
		strOS = getParameter("os").toLowerCase();
		strBrowser = getParameter("browser");

		tefSend = getParameter("tef_path_send");
		tefRequest = getParameter("tef_path_request");
		tefFlags = getParameter("tef_path_flags");

		try {
			iCodEmp = Integer.parseInt(getParameter("codemp"));
		}
		catch (Exception err) {
			Funcoes.mensagemErro(null, "Não foi possível carregar o parâmetro 'codemp'\n" + err.getMessage(), true, con, err);
		}

		try {
			if (!getParameter("codfilial").equals("")) {
				iCodFilialParam = Integer.parseInt(getParameter("codfilial"));
			}
		}
		catch (Exception err) {
			Funcoes.mensagemErro(null, "Não foi possível carregar o parâmetro 'codfilialparam'\n" + err.getMessage(), true, con, err);
		}

		try {
			iNumEst = Integer.parseInt(getParameter("numterm"));
		}
		catch (Exception err) {
			Funcoes.mensagemErro(null, "Não foi possível carregar o parâmetro 'numterm'\n" + err.getMessage(), true, con, err);
		}

		if (strBanco == null) {
			Funcoes.mensagemInforma(null, "Parametro banco nao foi preenchido");
			return;
		}
		if (strDriver == null) {
			Funcoes.mensagemInforma(null, "Parametro driver nao foi preenchido");
			return;
		}
		
		try {
			if (getParameter("fbversao") != null && !getParameter("fbversao").equals("") ) { 
				strFbVersao = getParameter("fbversao");
			} else {
				strFbVersao = FIREBIRD_15;
			}
		}
		catch (Exception err) {
			Funcoes.mensagemErro(null, "Não foi possível carregar o parâmetro 'fbversao'\n" + err.getMessage(), true, con, err);
		}

		con = conexao();

		if (con == null) {
			System.exit(1);
		}
		try {
			con.setAutoCommit(bAutoCommit);
			// con.setTransactionIsolation(DbConnection.TRANSACTION_SERIALIZABLE);
			con.getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		}
		catch (SQLException err) {
			Funcoes.mensagemErro(null, err.getMessage());
		}

		tbObjetos = new Tab();
		tbObjetos.montaLista(con, iCodEmp, "SGOBJETO", "TB");

		empresa = new Empresa(con);
	}

	public static int[] gravaLog(String sClas, String sTipo, String sDesc, String sObs, DbConnection con) {

		return gravaLog(getUsuario().getIdusu(), sClas, sTipo, sDesc, sObs, con);
	}

	public static int[] gravaLog(String sIDUSU, String sClas, String sTipo, String sDesc, String sObs, DbConnection con) {

		int iRet[] = new int[2];
		String sSQL = "SELECT CODFILIAL,CODLOG FROM SGLOGSP01(?,?,?,?)";
		try {
			PreparedStatement ps = con.prepareStatement(sSQL);
			// ps.setInt(1, iCodEmp);
			// ps.setString(2, sIDUSU);
			ps.setString(1, sClas);
			ps.setString(2, sTipo);
			ps.setString(3, sDesc);
			ps.setString(4, sObs);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				iRet[0] = rs.getInt("CodFilial");
				iRet[1] = rs.getInt("CodLog");
			}
			rs.close();
			ps.close();
			con.commit();
		}
		catch (SQLException err) {
			Funcoes.mensagemErro(null, "Erro ao gravar LOG!!\n" + err.getMessage(), true, con, err);
			err.printStackTrace();
		}
		return iRet;
	}

	public boolean getModoDemo() {

		String sSQL = "SELECT MODODEMOEST FROM SGESTACAO WHERE CODEST=" + iNumEst + "AND CODEMP=" + iCodEmp + " AND CODFILIAL=" + ListaCampos.getMasterFilial("SGESTACAO");
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean bModo = true;
		try {
			ps = con.prepareStatement(sSQL);
			rs = ps.executeQuery();
			if (!rs.next())
				Funcoes.mensagemErro(null, "Estação de trabalho não cadastrado!");
			else {
				if (rs.getString("ModoDemoEst").equals("S"))
					bModo = true;
				else
					bModo = false;
			}
			con.commit();
		}
		catch (SQLException err) {
			Funcoes.mensagemErro(null, err.getMessage(), true, con, err);
			return true;
		}
		return bModo;
	}

	protected void buscaInfoUsuAtual() {
		
		StringBuilder sql = new StringBuilder();
		 sql.append(" select codemp, codfilial, nomeusu, codempig");
		 sql.append(" , codfilialig, idgrpusu, codempcc, codfilialcc");
		 sql.append(" , anocc, codcc, pnomeusu, unomeusu, comentusu");
		 sql.append(" , baixocustousu, codalmox, codempam, codfilialam");
		 sql.append(" , abregavetausu, aprovcpsolicitacaousu, almoxarifeusu");
		 sql.append(" , codempae, codfilialae, tipoage, codage, aprovrmausu");
		 sql.append(" , comprasusu, altparcvenda, aprovreceita, ativcli");
		 sql.append(" , liberacredusu, coragenda, codempce, codfilialce");
		 sql.append(" , codconfemail, cancelaop, vendapatrimusu, rmaoutcc");
		 sql.append(" , ativousu, visualizalucr, liberacampopesagem,");
		 sql.append("  aprovordcp, acesopbtcadlote, acesopbtrma, acesopbtqualid");
		 sql.append("  , acesopbtdistr, acesopbtfase, acesopbtcanc, acesopbtsubprod");
		 sql.append("  , acesopbtremessa, acesopbtretorno, acesopveritens");
		 sql.append("  , dtins, hins, idusuins, dtalt, halt, idusualt ");
		 sql.append("  from sgusuario");
		 sql.append(" where codemp=? and codfilial=? and idusu=? ");

//		String sSQL = "SELECT ANOCC,CODCC,CODEMPCC,CODFILIALCC,APROVRMAUSU " + "FROM SGUSUARIO WHERE CODEMP=? AND CODFILIAL=? " + "AND IDUSU=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = con.prepareStatement(sql.toString());
			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, ListaCampos.getMasterFilial("SGUSUARIO"));
			ps.setString(3, getUsuario().getIdusu());
			rs = ps.executeQuery();
			if (rs.next()) {
				getUsuario().setCodemp(rs.getInt("codemp"));
				getUsuario().setCodfilial(rs.getInt("codfilial"));
				getUsuario().setCodcc( rs.getString("codcc") );
				getUsuario().setAnocc( rs.getInt("anocc") );
				getUsuario().setNomeusu( rs.getString("nomeusu") );
				getUsuario().setCodempig( rs.getInt("codempig") );
				getUsuario().setCodfilialig( rs.getInt("codfilialig") );
				getUsuario().setIdgrpusu( rs.getString("idgrpusu") );
				getUsuario().setCodempcc( rs.getInt("codempcc") );
				getUsuario().setCodfilialcc( rs.getInt("codfilialcc") );
				getUsuario().setPnomeusu( rs.getString("pnomeusu") );
				getUsuario().setUnomeusu( rs.getString("unomeusu") );
				getUsuario().setComentusu( rs.getString("comentusu") );
				getUsuario().setBaixocustousu( rs.getString("baixocustousu") );
				getUsuario().setCodempam( rs.getInt("codempam") );
				getUsuario().setCodfilialam( rs.getInt("codfilialam") );
				getUsuario().setCodalmox( rs.getInt("codalmox") );
				getUsuario().setAbregavetausu( rs.getString("abregavetausu") );
				getUsuario().setAprovcpsolicitacaousu( rs.getString("aprovcpsolicitacaousu") );
				getUsuario().setAlmoxarifeusu( rs.getString("almoxarifeusu") );
				getUsuario().setCodempae( rs.getInt("codempae") );
				getUsuario().setCodfilialae( rs.getInt("codfilialae") );
				getUsuario().setTipoage( rs.getString("tipoage") );
				getUsuario().setCodage( rs.getInt("codage") );
				getUsuario().setAprovrmausu( rs.getString("aprovrmausu") );
				getUsuario().setComprasusu( rs.getString("comprasusu") );
				getUsuario().setAltparcvenda( rs.getString("altparcvenda") );
				getUsuario().setAprovreceita( rs.getString("aprovreceita") );
				getUsuario().setAtivcli( rs.getString("ativcli") );
				getUsuario().setLiberacredusu( rs.getString("liberacredusu") );
				getUsuario().setCoragenda( rs.getInt("coragenda") );
				getUsuario().setCodempce( rs.getInt("codempce") );
				getUsuario().setCodfilialce( rs.getInt("codfilialce") );
				getUsuario().setCodconfemail( rs.getInt("codconfemail") );
				getUsuario().setCancelaop( rs.getString("cancelaop") );
				getUsuario().setVendapatrimusu( rs.getString("vendapatrimusu") );
				getUsuario().setRmaoutcc( rs.getString("rmaoutcc") );
				getUsuario().setAtivousu( rs.getString("ativousu") );
				getUsuario().setVisualizalucr( rs.getString("visualizalucr") );
				getUsuario().setLiberacampopesagem( rs.getString("liberacampopesagem") );
				getUsuario().setAprovordcp( rs.getString("aprovordcp") );
				getUsuario().setAcesopbtcadlote( rs.getString("acesopbtcadlote") );
				getUsuario().setAcesopbtrma( rs.getString("acesopbtrma") );
				getUsuario().setAcesopbtqualid( rs.getString("acesopbtqualid") );
				getUsuario().setAcesopbtdistr( rs.getString("acesopbtdistr") );
				getUsuario().setAcesopbtfase( rs.getString("acesopbtfase") );
				getUsuario().setAcesopbtcanc( rs.getString("acesopbtcanc") );
				getUsuario().setAcesopbtsubprod( rs.getString("acesopbtsubprod") );
				getUsuario().setAcesopbtremessa( rs.getString("acesopbtremessa") );
				getUsuario().setAcesopbtretorno( rs.getString("acesopbtretorno") );
				getUsuario().setAcesopveritens( rs.getString("acesopveritens") );
				getUsuario().setDtins( Funcoes.sqlDateToDate(rs.getDate("dtins")) );
				getUsuario().setHins( rs.getString("hins") );
				getUsuario().setIdusuins( rs.getString("idusuins") );
				getUsuario().setDtalt( Funcoes.sqlDateToDate(rs.getDate("dtalt")) );
				getUsuario().setHins( rs.getString("halt") );
				getUsuario().setIdusualt( rs.getString("idusualt") );
			
			}
			rs.close();
			ps.close();
			con.commit();

		}
		catch (SQLException err) {
			err.printStackTrace();
			killProg(1, "Erro ao carregar informações da tabela de usuários!\n" + err.getMessage());
		}
	}

	public boolean verifAcesso(int iCodSisP, int iCodModuP, int iCodMenuP) {

		boolean bRet = false;
		if (getUsuario().getIdusu().toUpperCase().equals("SYSDBA"))
			return true;
		try {

			String sTmp = "";
			String sSQL = "SELECT TPACESSOMU FROM SGACESSOMU WHERE CODEMP = ? " + "AND CODFILIAL = ? " + "AND IDUSU = ? " + "AND CODSIS = ? " + "AND CODMODU = ? " + "AND CODMENU = ?";
			PreparedStatement ps = con.prepareStatement(sSQL);
			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, Aplicativo.iCodFilial);
			ps.setString(3, getUsuario().getIdusu());
			ps.setInt(4, iCodSisP);
			ps.setInt(5, iCodModuP);
			ps.setInt(6, iCodMenuP);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				sTmp = rs.getString("TPACESSOMU");
				if (sTmp == null)
					return bRet;
				if (sTmp.toCharArray()[0] > 'A')
					bRet = true;
			}
			rs.close();
			ps.close();
		}
		catch (SQLException err) {
			killProg(1, "Erro ao verificar acessos para arvore de menus!\n" + err.getMessage());
		}
		return bRet;
	}

	public String getDescEst() {

		String sSQL = "SELECT DESCEST FROM SGESTACAO WHERE CODEST=" + iNumEst + " AND CODEMP=" + iCodEmp + " AND CODFILIAL=" + ListaCampos.getMasterFilial("SGESTACAO");
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sDesc = "";
		try {
			ps = con.prepareStatement(sSQL);
			rs = ps.executeQuery();
			if (!rs.next())
				sDesc = "ESTAÇÃO DE TRABALHO NÃO CADASTRADA";
			else
				sDesc = rs.getString("DescEst");
			con.commit();
		}
		catch (SQLException err) {
			Funcoes.mensagemErro(null, err.getMessage(), true, con, err);
			return "NÃO FOI POSSÍVEL REGISTRAR A ESTAÇÃO DE TRABALHO! ! !";
		}
		return sDesc;
	}
	
	public void infoProxy() {
		
		StringBuilder sql = new StringBuilder("select px.hostproxy, px.portaproxy, px.usuproxy, px.senhaproxy, px.autproxy ");
		sql.append( "from sgestacao es ");
		sql.append( "inner join sgproxyweb px on px.codemp=es.codemppx and px.codfilial=es.codfilialpx and px.codproxy=es.codproxy ");
		sql.append( "where es.codest=? and es.codemp=? and es.codfilial=? ");
				
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			ps = con.prepareStatement(sql.toString());
			
			ps.setInt(1, iNumEst);
			ps.setInt(2, iCodEmp);
			ps.setInt(3, ListaCampos.getMasterFilial("SGESTACAO"));
			
			rs = ps.executeQuery();
			
			if (rs.next()) {
							
				this.isAutproxy( rs.getString("autproxy").equals("S") );
				this.setHttpproxy(rs.getString("hostproxy"));
				this.setPortaproxy(rs.getString("portaproxy"));
				this.setUsuarioproxy(rs.getString("usuproxy"));
				this.setSenhaproxy(rs.getString("senhaproxy"));			
				
			}
			
			con.commit();
		}
		catch (SQLException err) {
			Funcoes.mensagemErro(null, err.getMessage(), true, con, err);		
		}
		
	}

	public void validaPrefere() {

		if (iCodModu == 11 /* Modulo de representações volta */) {
			return;
		}

		String sSQL = "SELECT CODEMP FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		// String sDesc = "";

		try {
			ps = con.prepareStatement(sSQL);
			ps.setInt(1, iCodEmp);
			ps.setInt(2, iCodFilial);

			rs = ps.executeQuery();

			if (!rs.next()) {
				FPrefereGeral tela = new FPrefereGeral();
				telaPrincipal.criatela("Preferências gerais", tela, con);
				tela.setTelaPrim(telaPrincipal);
			}
			con.commit();
		}
		catch (SQLException err) {
			Funcoes.mensagemErro(null, err.getMessage(), true, con, err);
		}

	}

	public void getMultiAlmox() {

		String sSQL = "SELECT MULTIALMOXEMP FROM SGEMPRESA WHERE CODEMP=?";
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement(sSQL);
			ps.setInt(1, iCodEmp);
			rs = ps.executeQuery();

			if (rs.next())
				sMultiAlmoxEmp = ( rs.getString("MULTIALMOXEMP") == null ? "N" : rs.getString("MULTIALMOXEMP") );
			else
				sMultiAlmoxEmp = "N";
			con.commit();
		}
		catch (SQLException err) {
			Funcoes.mensagemErro(null, err.getMessage());
		}
	}

	protected void carregaCasasDec() {

		String sSQL = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			sSQL = "SELECT P.CASASDEC, P.CASASDECFIN, P.CASASDECPRE, COALESCE(F.SIMPLESFILIAL,'N') SIMPLESFILIAL, CODMOEDA "
				 + "FROM SGPREFERE1 P, SGFILIAL F WHERE F.CODEMP=P.CODEMP AND F.CODFILIAL=P.CODFILIAL AND P.CODEMP=? AND P.CODFILIAL=?";
			
			ps = con.prepareStatement(sSQL);
			ps.setInt(1, iCodEmp);
			ps.setInt(2, ListaCampos.getMasterFilial("SGPREFERE1"));
			rs = ps.executeQuery();
			
			if (rs.next()) {
				casasDec = rs.getInt("CASASDEC");
				casasDecFin = rs.getInt("CASASDECFIN");
				casasDecPre = rs.getInt("CASASDECPRE");
				simples = "S".equals(rs.getString("SIMPLESFILIAL"));
				codmoeda = rs.getString("CODMOEDA");
			}

			rs.close();
			ps.close();
			con.commit();
		}
		catch (SQLException err) {
			Funcoes.mensagemErro(null, "Não foi possível obter o número de casas decimais!\n" + err.getMessage(), true, con, err);
		}
		finally {
			sSQL = null;
			ps = null;
			rs = null;
		}
	}

	protected void carregaBuscaProd() {

		String sSQL = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			sSQL = "SELECT BUSCAPRODSIMILAR,BUSCACODPRODGEN FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
			ps = con.prepareStatement(sSQL);
			ps.setInt(1, iCodEmp);
			ps.setInt(2, ListaCampos.getMasterFilial("SGPREFERE1"));
			rs = ps.executeQuery();
			if (rs.next()) {
				bBuscaProdSimilar = "S".equals(rs.getString("BUSCAPRODSIMILAR"));
				bBuscaCodProdGen = "S".equals(rs.getString("BUSCACODPRODGEN"));
			}
			rs.close();
			ps.close();
			con.commit();
		}
		catch (SQLException err) {
			err.printStackTrace();
		}
		finally {
			sSQL = null;
			ps = null;
			rs = null;
		}
	}

	public static String carregaFiltro(DbConnection conF, int iCodEmpF) {

		String sSQL = "SELECT FILTRO FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
		// String sSQL = "INSERT INTO TESTE (TESTE1) VALUES ('2001-3-23')";
		PreparedStatement ps = null;
		ResultSet rs = null;
		sFiltro = "";
		try {
			ps = conF.prepareStatement(sSQL);
			ps.setInt(1, iCodEmpF);
			ps.setInt(2, ListaCampos.getMasterFilial("SGPREFERE1"));
			rs = ps.executeQuery();
			if (!rs.next()) {
				sFiltro = "";
				Funcoes.mensagemInforma(null, "Preferências não foram cadastradas!");
			}
			else
				sFiltro = rs.getString("FILTRO");
			// rs.close();
			// ps.close();
			conF.commit();
		}
		catch (SQLException err) {
			Funcoes.mensagemErro(null, "NÃO FOI POSSÍVEL CARREGAR OS FILTROS! " + err.getMessage(), true, conF, err);
			// return "NÃO FOI POSSÍVEL CARREGAR OS FILTROSL! ! !";
		}
		if (sFiltro == null)
			sFiltro = "('S','N')";
		else if (sFiltro.trim().equals(""))
			sFiltro = "('S','N')";
		else if (sFiltro.trim().equals("SN"))
			sFiltro = "('S','N')";
		else if (sFiltro.trim().equals("S"))
			sFiltro = "('S')";
		else if (sFiltro.trim().equals("N"))
			sFiltro = "('N')";
		return sFiltro;
	}

	public void setFiltro(char cFiltro) {

		sFiltro = "";
		switch (cFiltro) {
		case '1':
			sFiltro = "S";
			break;
		case '2':
			sFiltro = "N";
			break;
		case '3':
			sFiltro = "SN";
			break;
		}
		try {
			String sSQL = "UPDATE SGPREFERE1 SET FILTRO=? WHERE CODEMP=?";
			PreparedStatement ps = con.prepareStatement(sSQL);
			ps.setString(1, sFiltro);
			ps.setInt(2, Aplicativo.iCodEmp);
			ps.execute();
			con.commit();
			Funcoes.mensagemInforma(null, "Filtros atualizados para: " + sFiltro);
		}
		catch (SQLException err) {
			Funcoes.mensagemErro(null, "Erro ao atualizar filtro.\n" + err.getMessage(), true, con, err);
		}
	}

	public void keyPressed(KeyEvent kevt) {

		if (kevt.getKeyCode() == KeyEvent.VK_CONTROL)
			bCtrl = true;
		if (bCtrl) {
			if (kevt.getKeyCode() == KeyEvent.VK_F10)
				setFiltro('1');
			else if (kevt.getKeyCode() == KeyEvent.VK_F11)
				setFiltro('2');
			else if (kevt.getKeyCode() == KeyEvent.VK_F12)
				setFiltro('3');
		}
	}

	@Override
	public void createEmailBean() {

		ResultSet rs = null;
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();

		sql = new StringBuilder();

		try {
			sql.append("select u.idusu,");
			sql.append("e.hostsmtp, e.portasmtp, e.usuarioremet, e.senharemet, e.criptsenha, e.usaautsmtp, e.usassl,");
			sql.append("e.emailremet de, e.assinatremet assinatura ");
			sql.append("from tkconfemail e, sgusuario u ");
			sql.append("where u.codemp=? and u.codfilial=? and u.idusu=? and ");
			sql.append("e.codemp=u.codempce and e.codfilial=u.codfilialce and e.codconfemail=u.codconfemail");

			ps = con.prepareStatement(sql.toString());
			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, ListaCampos.getMasterFilial("SGUSUARIO"));
			ps.setString(3, getUsuario().getIdusu());

			rs = ps.executeQuery();
			EmailBean email = new EmailBean();

			if (rs.next()) {
				email.setHost(rs.getString("hostsmtp"));
				email.setPorta(rs.getInt("portasmtp"));
				email.setUsuario(rs.getString("usuarioremet"));
				email.setSenha(rs.getString("senharemet"), rs.getString("criptsenha"));
				email.setAutentica(rs.getString("usaautsmtp"));
				email.setSsl(rs.getString("usassl"));
				email.setDe(rs.getString("de"));
				email.setAssinatura(rs.getString("assinatura"));
				setEmailBean(email);
			}
			else {
				createEmailBeanPrefere();
			}

			rs.close();
			ps.close();

			con.commit();
		}
		catch (SQLException e) {
			// Funcoes.mensagemErro( null,
			// "Não foi possível carregar as informações para envio de email!\n"
			// + e.getMessage() );
			System.out.println("Não foi possível carregar as informações para envio de email, através dos dados do usuário!\n" + e.getMessage());
		}

	}

	public void createEmailBeanPrefere() {

		ResultSet rs = null;
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		EmailBean email = new EmailBean();
		sql.append("SELECT P3.SMTPMAIL, P3.SMTPAUTMAIL, P3.USERMAIL, P3.PASSMAIL, ");
		sql.append("P3.SMTPSSLMAIL, P3.PORTAMAIL, P3.ENDMAIL ");
		sql.append("FROM SGPREFERE3 P3 ");
		sql.append("WHERE P3.CODEMP=? AND P3.CODFILIAL=?");
		try {
			ps = con.prepareStatement(sql.toString());
			ps.setInt(1, iCodEmp);
			ps.setInt(2, ListaCampos.getMasterFilial("SGPREFERE3"));
			rs = ps.executeQuery();
			if (rs.next()) {
				email.setHost(rs.getString("SMTPMAIL"));
				email.setAutentica(rs.getString("SMTPAUTMAIL"));
				email.setSsl(rs.getString("SMTPSSLMAIL"));
				email.setPorta(rs.getInt("PORTAMAIL"));
				email.setUsuario(rs.getString("USERMAIL"));
				email.setSenha(rs.getString("PASSMAIL"));
				email.setDe(rs.getString("ENDMAIL"));
				setEmailBean(email);
			}
			rs.close();
			ps.close();
			con.commit();
		}
		catch (SQLException e) {
			Funcoes.mensagemErro(null, "Não foi possível carregar as informações para envio de emial!\n" + e.getMessage());
		}

	}

	@Override
	public void updateEmailBean(EmailBean email) {

		try {

			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE SGPREFERE3 P3 ");
			sql.append("SET P3.SMTPMAIL=?, P3.SMTPAUTMAIL=?, ");
			sql.append("P3.USERMAIL=?, P3.PASSMAIL=?, ");
			sql.append("P3.SMTPSSLMAIL=?, P3.PORTAMAIL=?, P3.ENDMAIL=? ");
			sql.append("WHERE P3.CODEMP=? AND P3.CODFILIAL=?");

			PreparedStatement ps = con.prepareStatement(sql.toString());
			ps.setString(1, email.getHost());
			ps.setString(2, email.getAutentica());
			ps.setString(3, email.getUsuario());
			ps.setString(4, email.getSenha());
			ps.setString(5, email.getSsl());
			ps.setInt(6, email.getPorta());
			ps.setString(7, email.getDe());
			ps.setInt(8, iCodEmp);
			ps.setInt(9, ListaCampos.getMasterFilial("SGPREFERE3"));
			ps.executeUpdate();
			ps.close();

			con.commit();

			setEmailBean(email);

		}
		catch (SQLException e) {
			Funcoes.mensagemErro(null, "Não foi gravar as alterações de configuração de email!\n" + e.getMessage());
		}

	}


}
