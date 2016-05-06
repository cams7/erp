/**
 * @version 14/11/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.telas <BR>
 * Classe:
 * @(#)FPrincipal.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * Comentários para a classe...
 */

package org.freedom.library.swing.frame;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.freedom.bmps.Icone;
import org.freedom.library.functions.Funcoes;

public class FPrincipalPD extends FPrincipal implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;

	public FPrincipalPD(String sDirImagem, String sImgFundo) {

		this(sDirImagem, sImgFundo, null, null, null, null, null);
	}

	public FPrincipalPD(String sDirImagem, String sImgFundo, String sImgLogoSis, String sImgLogoEmp, Color bgcolor, String urlempresa, String urlsistema) {

		super(sDirImagem, sImgFundo, sImgLogoSis, sImgLogoEmp, bgcolor, urlempresa, urlsistema);

	}

	public void inicializaTela() {
		addFundo();
		addLinks(Icone.novo(imgLogoEmp), Icone.novo(imgLogoSis));
		setBgColor(padrao);
		adicBotoes();
	}

	public void remConFilial() {

		String sSQL = "EXECUTE PROCEDURE SGFIMCONSP";
		try {
			PreparedStatement ps = con.prepareStatement(sSQL);
			// ps.setInt( 1, Aplicativo.iCodEmp );
			// ps.setInt( 2, Aplicativo.iCodFilialPad );
			// ps.setString( 3, Aplicativo.getUsuario().getIdusu() );
			ps.execute();
			ps.close();
			con.commit();
		}
		catch (SQLException err) {
			Funcoes.mensagemErro(null, "Erro ao remover filial ativa no banco!\n" + err.getMessage());
		}
	}

	public void fecharJanela() {

		if (con != null) {
			try {
				remConFilial();
				con.close();
			}
			catch (java.sql.SQLException e) {
				System.out.println("Não foi possível fechar a conexao com o banco de dados!");
				// Funcoes.mensagemErro( null,
				// "Não foi possível fechar a conexao com o banco de dados!" );
			}
		}
		System.exit(0);
	}
	
	public void windowOpen() {
	   System.out.println("Janela aberta");	
	}
}
