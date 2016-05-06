/**
 * @version 03/02/2004 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.gms <BR>
 *         Classe: @(#)FAribUsu.java <BR>
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
 *         Formulário de cadastro de usuários do sistema.
 * 
 */

package org.freedom.modulos.gms.view.frame.crud.special;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JScrollPane;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.modulos.cfg.view.frame.crud.tabbed.FUsuario;

public class FAtribUsu extends FFilho implements CarregaListener, ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCli = new JPanelPad();

	private JTextFieldPad txtCodUsu = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldFK txtNomeUsu = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private static Vector<String> vAtrib = new Vector<String>();

	private static Vector<String> vDisp = new Vector<String>();

	private static Vector<String> vCodAtrib = new Vector<String>();

	private static Vector<String> vCodDisp = new Vector<String>();

	private JList lsAtrib = new JList();

	private JList lsDisp = new JList();

	private JScrollPane spnAtrib = new JScrollPane( lsAtrib );

	private JScrollPane spnDisp = new JScrollPane( lsDisp );

	private JButtonPad btAdicAtrib = new JButtonPad( Icone.novo( "btFlechaDir.png" ) );

	private JButtonPad btDelAtrib = new JButtonPad( Icone.novo( "btFlechaEsq.png" ) );

	private JButtonPad btSalvar = new JButtonPad( Icone.novo( "btSalvar.gif" ) );

	private ListaCampos lcUsu = new ListaCampos( this, "" );

	public FAtribUsu() {

		super( false );
		setTitulo( "Atribuições por Usuário" );
		setAtribos( 50, 50, 395, 240 );

		lcUsu.add( new GuardaCampo( txtCodUsu, "IDUsu", "ID", ListaCampos.DB_PK, txtNomeUsu, false ) );
		lcUsu.add( new GuardaCampo( txtNomeUsu, "NomeUsu", "Nome nome do usuário", ListaCampos.DB_SI, false ) );
		lcUsu.montaSql( false, "USUARIO", "SG" );
		lcUsu.setQueryCommit( false );
		lcUsu.setReadOnly( true );
		txtCodUsu.setFK( true );
		txtCodUsu.setNomeCampo( "IDUsu" );
		txtCodUsu.setTabelaExterna( lcUsu, FUsuario.class.getCanonicalName() );

		getTela().add( pinCli, BorderLayout.CENTER );

		pinCli.adic( new JLabelPad( "ID" ), 7, 0, 200, 20 );
		pinCli.adic( txtCodUsu, 7, 20, 80, 20 );
		pinCli.adic( new JLabelPad( "Nome do usuário" ), 90, 0, 200, 20 );
		pinCli.adic( txtNomeUsu, 90, 20, 280, 20 );

		pinCli.adic( new JLabelPad( "Atribuições disponíveis:" ), 7, 40, 160, 20 );
		pinCli.adic( spnDisp, 7, 60, 160, 100 );
		pinCli.adic( btAdicAtrib, 175, 75, 30, 30 );
		pinCli.adic( btDelAtrib, 175, 115, 30, 30 );
		pinCli.adic( new JLabelPad( "Acesso:" ), 212, 40, 158, 20 );
		pinCli.adic( spnAtrib, 212, 60, 158, 100 );

		btSalvar.setEnabled( false );
		btSalvar.setPreferredSize( new Dimension( 30, 0 ) );
		adicBotaoSair().add( btSalvar, BorderLayout.WEST );

		btAdicAtrib.addActionListener( this );
		btDelAtrib.addActionListener( this );
	}

	private void adicionaEmp() {

		if ( lsDisp.isSelectionEmpty() )
			return;
		for ( int i = lsDisp.getMaxSelectionIndex(); i >= 0; i-- ) {
			if ( lsDisp.isSelectedIndex( i ) ) {
				vAtrib.add( vDisp.elementAt( i ) );
				vDisp.remove( i );
				vCodAtrib.add( vCodDisp.elementAt( i ) );
				vCodDisp.remove( i );
			}
		}
		lsDisp.setListData( vDisp );
		lsAtrib.setListData( vAtrib );
		btSalvar.setEnabled( true );
	}

	private void removeAtrib() {

		if ( lsAtrib.isSelectionEmpty() )
			return;
		for ( int i = lsAtrib.getMaxSelectionIndex(); i >= 0; i-- ) {
			if ( lsAtrib.isSelectedIndex( i ) ) {
				vDisp.add( vAtrib.elementAt( i ) );
				vAtrib.remove( i );
				vCodDisp.add( vCodAtrib.elementAt( i ) );
				vCodAtrib.remove( i );
			}
		}
		lsDisp.setListData( vDisp );
		lsAtrib.setListData( vAtrib );
		btSalvar.setEnabled( true );
	}

	private void carregaDisp() {

		try {
			PreparedStatement ps;
			ps = con.prepareStatement( "SELECT IDATRIB,DESCATRIB FROM SGATRIBUICAO WHERE CODEMP=? AND CODFILIAL=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGATRIBUICAO" ) );
			ResultSet rs = ps.executeQuery();
			vDisp.clear();
			vCodDisp.clear();
			while ( rs.next() ) {
				vCodDisp.addElement( rs.getString( "IdAtrib" ) );
				vDisp.addElement( rs.getString( "DescAtrib" ) != null ? rs.getString( "DescAtrib" ).trim() : "" );
			}
			rs.close();
			ps.close();
			con.commit();
			lsDisp.setListData( vDisp );
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Não foi carregar as atribuições diponíveis.\n" + err.getMessage(), true, con, err );
		}
	}

	private void carregaAcesso() {

		if ( txtCodUsu.getVlrString().trim().equals( "" ) ) {
			Funcoes.mensagemInforma( this, "ID do usuário em branco!" );
			return;
		}

		int iPos = 0;
		try {
			String sSQL = "SELECT A.IDATRIB,A.DESCATRIB FROM SGATRIBUICAO A, SGATRIBUSU AU WHERE " + "A.CODEMP = AU.CODEMP AND A.CODFILIAL = AU.CODFILIAL" + "AND AU.IDUSU = ? AND CODEMPUU=? AND CODFILIALUU=?";
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setString( 1, txtCodUsu.getVlrString() );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, lcUsu.getCodFilial() );
			ResultSet rs = ps.executeQuery();
			vAtrib.clear();
			vCodAtrib.clear();
			while ( rs.next() ) {
				vCodAtrib.addElement( rs.getString( "IDAtrib" ) );
				vAtrib.addElement( rs.getString( "DescAtrib" ) != null ? rs.getString( "DescAtrib" ).trim() : "" );

				iPos = vCodDisp.indexOf( rs.getString( "DescAtrib" ) );

				vCodDisp.remove( iPos );
				vDisp.remove( iPos );
			}
			rs.close();
			ps.close();
			con.commit();
			lsAtrib.setListData( vAtrib );
			lsDisp.setListData( vDisp );
		} catch ( SQLException err ) {
			Funcoes.mensagemInforma( this, "Não foi carregar as filiais que o usuário tem acesso.\n" + err );
		}
	}

	/*
	 * private void gravaAcesso() { String sSep = ""; String sSqlI = ""; String sSqlD = ""; for (int i=0; i<vCodAtrib.size();i++) { sSqlI += sSep + vCodAtrib.elementAt(i); sSep = ","; }
	 * 
	 * try { sSqlD = "DELETE FROM SGACESSOEU WHERE IDUSU=? AND CODEMP=?"; PreparedStatement ps = con.prepareStatement(sSqlD); ps.setString(1,txtCodUsu.getVlrString()); ps.setInt(2,Aplicativo.iCodEmp); ps.executeUpdate(); ps.close(); if (!con.getAutoCommit()) con.commit(); if (vCodAtrib.size() > 0) {
	 * sSqlI = "INSERT INTO SGACESSOEU (CODEMP,CODFILIAL,IDUSU,CODEMPFL,CODFILIALFL) "+ "SELECT CODEMP,"+Aplicativo.iCodFilial+",'"+txtCodUsu.getVlrString()+"',CODEMP,CODFILIAL FROM SGFILIAL "+ "WHERE CODEMP=? AND CODFILIAL IN ("+sSqlI+")"; ps = con.prepareStatement(sSqlI);
	 * ps.setInt(1,Aplicativo.iCodEmp); ps.executeUpdate(); ps.close(); if (!con.getAutoCommit()) con.commit(); btSalvar.setEnabled(false); } } catch (SQLException err) { Funcoes.mensagemInforma(this,"Erro ao cadastrar o acesso!\n"+err.getMessage()); } }
	 */
	public void afterCarrega( CarregaEvent pevt ) {

		carregaDisp();
		carregaAcesso();
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btAdicAtrib )
			adicionaEmp();
		else if ( evt.getSource() == btDelAtrib )
			removeAtrib();
	}

	public void beforeCarrega( CarregaEvent pevt ) {

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcUsu.setConexao( cn );
	}

}
