/**
 * @version 14/11/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.telas <BR>
 * Classe:
 * @(#)FPrincipal.java <BR>
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
 * Comentários para a classe...
 */

package org.freedom.library.swing.frame;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.freedom.bmps.Icone;
import org.freedom.plugin.AbstractBackground;

public class FPrincipal2 extends FPrincipal implements ActionListener, MouseListener {
	private static final long serialVersionUID = 1L;

	private AbstractBackground fundo = null;

	public FPrincipal2(AbstractBackground fundo, String sImgFundo) {
		super(null, sImgFundo);
		this.fundo = fundo;
	}

	public void inicializaTela() {
		setBgColor(new Color(255, 255, 255));
		addLinks(Icone.novo("lgSTP2.jpg"), Icone.novo("lgFreedom2.jpg"));
		adicBotoes();
		adicAgenda();
	}

	public void setConexao(DbConnection con) {
		super.setConexao(con);
		fundo.setConexao(con);
		addFundo(fundo);
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
				// Funcoes.mensagemErro(null,
				// "Não foi possível fechar a conexao com o banco de dados!" );
			}
		}
		System.exit(0);
	}

	@Override
	public void windowOpen() {
		
	}

}