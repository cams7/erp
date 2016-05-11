/**
 * @version 09/05/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.cfg <BR>
 *         Classe: @(#)FAcesso.java <BR>
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

package org.freedom.modulos.cfg.view.frame.crud.special;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import org.freedom.acao.ArvoreFace;
import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.JComboBoxEvent;
import org.freedom.acao.JComboBoxListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.JTreePad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.cfg.view.dialog.utility.DLCopiarPermissoes;

public class FAcesso extends FFDialogo implements ArvoreFace, CarregaListener, JComboBoxListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pnCliente = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinTop = new JPanelPad( 475, 90 );

	private JTextFieldPad txtCodUsu = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldFK txtNomeUsu = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private ListaCampos lcUsuario = new ListaCampos( this );

	private JTreePad arvore = new JTreePad();

	private JScrollPane spnArv = new JScrollPane( arvore );

	private Vector<String[]> vAcessos = new Vector<String[]>();

	private Vector<Object> vVals = new Vector<Object>();

	private Vector<String> vLabs = new Vector<String>();

	private JComboBoxPad cbFiliais = new JComboBoxPad( vLabs, vVals, JComboBoxPad.TP_INTEGER, 8, 0 );

	private JButtonPad btSalva = new JButtonPad( Icone.novo( "btGerar.png" ) );
	
	private JButtonPad btCopiaPermissoes = new JButtonPad( "Copiar",Icone.novo( "btCopiar.png" ) );

	boolean bEOF = false;

	int iConta = 0;

	public FAcesso() {

		super( Aplicativo.telaPrincipal );

		setTitulo( "Controle de acesso por menus." );
		setAtribos( 500, 450 );

		setToFrameLayout();

		btSalva.setPreferredSize( new Dimension( 30, 30 ) );
		pnRodape.add( btSalva, BorderLayout.WEST );
		btSalva.setEnabled( false );
		btSalva.setToolTipText( "Salvar permissões" );
		
		btCopiaPermissoes.setEnabled( false );
		btCopiaPermissoes.setToolTipText( "Copiar permissões de qual usuário?" );
		
		txtCodUsu.setNomeCampo( "IdUsuario" );
		lcUsuario.add( new GuardaCampo( txtCodUsu, "IDUSU", "ID Usuario", ListaCampos.DB_PK, txtNomeUsu, false ) );
		lcUsuario.add( new GuardaCampo( txtNomeUsu, "NOMEUSU", "Nome", ListaCampos.DB_SI, false ) );
		lcUsuario.montaSql( false, "USUARIO", "SG" );
		lcUsuario.setQueryCommit( false );
		lcUsuario.setReadOnly( true );
		txtCodUsu.setChave( ListaCampos.DB_PK );
		txtCodUsu.setListaCampos( lcUsuario );
		txtNomeUsu.setListaCampos( lcUsuario );
		txtCodUsu.setTabelaExterna( lcUsuario, null );
		txtCodUsu.setNomeCampo( "idusu" );

		setPanel( pnCliente );
		setPainel( pinTop );

		adic( new JLabelPad( "ID Usuário" ), 7, 0, 100, 20 );
		adic( txtCodUsu, 7, 20, 100, 20 );
		adic( new JLabelPad( "Nome" ), 110, 0, 250, 20 );
		adic( txtNomeUsu, 110, 20, 250, 20 );
		adic( new JLabelPad( "Filial" ), 7, 40, 250, 20 );
		adic( cbFiliais, 7, 60, 353, 20 );
		adic( btCopiaPermissoes, 363, 55, 100, 30);

		c.add( pinTop, BorderLayout.NORTH );
		c.add( spnArv, BorderLayout.CENTER );

		arvore.setModel( new DefaultTreeModel( new DefaultMutableTreeNode( "Acesso aos menus" ) ) );

		arvore.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );

		arvore.addMouseListener( new MouseAdapter() {

			public void mousePressed( MouseEvent e ) {

				upImagem( e.getX(), e.getY() );
			}
		} );

		arvore.setImgTrat( this );

		lcUsuario.addCarregaListener( this );
		cbFiliais.addActionListener( this );
		btSalva.addActionListener( this );
		btCopiaPermissoes.addActionListener( this );
		cbFiliais.addComboBoxListener( this );

	}

	public void montaArvore() {

		if ( ( cbFiliais.getVlrInteger() == null ) || ( cbFiliais.getVlrInteger().intValue() == 0 ) )
			return;
		DefaultMutableTreeNode men = new DefaultMutableTreeNode( "Acesso aos menus" );

		arvore.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );

		try {
			DefaultMutableTreeNode pai = null;
			DefaultMutableTreeNode item = null;
			int iCodSis = -1;
			int iCodModu = -1;
			int iCodSisAnt = -1;
			int iCodModuAnt = -1;

			String sSQL = "SELECT (SELECT AC.TPACESSOMU FROM SGACESSOMU AC WHERE AC.CODEMP = ? " + "AND AC.CODFILIAL = ? AND AC.IDUSU = ? AND AC.CODSIS=MN.CODSIS " + "AND AC.CODMODU=MN.CODMODU AND AC.CODMENU=MN.CODMENU),MN.CODSIS," + "SIS.DESCSIS,MN.CODMODU,MO.DESCMODU,CODMENU,DESCMENU,CODSISPAI,"
					+ "CODMODUPAI,CODMENUPAI FROM SGMENU MN,SGSISTEMA SIS,SGMODULO MO " + "WHERE SIS.CODSIS=MN.CODSIS AND MO.CODSIS=MN.CODSIS AND " + "MN.CODMENU!=0 AND " + "MO.CODMODU = MN.CODMODU ORDER BY MN.CODSIS,MN.CODMODU,MN.CODMENU";

			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, cbFiliais.getVlrInteger().intValue() );
			ps.setString( 3, txtCodUsu.getVlrString() );
			ResultSet rs = ps.executeQuery();
			bEOF = false;
			iConta = 0;
			vAcessos.clear();

			while ( rs.next() ) {
				iCodSis = rs.getInt( "CodSis" );
				iCodModu = rs.getInt( "CodModu" );
				if ( iCodSisAnt != iCodSis ) {
					pai = new DefaultMutableTreeNode( rs.getString( "DescSis" ) );
					men.add( pai );
					iCodSisAnt = iCodSis;
				}
				if ( iCodModuAnt != iCodModu ) {
					iCodModuAnt = iCodModu;
					item = new DefaultMutableTreeNode( rs.getString( "DescModu" ) );
					pai.add( adicFilho( rs, item, 1 ) );
				}
				if ( bEOF )
					break;
			}
			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar menus.\n" + err.getMessage(), true, con, err );
		}

		DefaultTreeModel mod = new DefaultTreeModel( men );
		arvore.setModel( mod );
	}

	private DefaultMutableTreeNode adicFilho( ResultSet rs, DefaultMutableTreeNode pai, int iNivel ) {

		int iSubMenuAnt = -1;
		int iSubMenu = -1;
		int iCodModuAnt = -1;
		DefaultMutableTreeNode item = null;
		try {
			while ( true ) {
				int iCodMenu = rs.getInt( "CodMenu" );
				int iCodModu = rs.getInt( "CodModu" );
				switch ( iNivel ) {
					case 1 :
						iSubMenu = Integer.parseInt( ( iCodMenu + "" ).substring( 0, 2 ) );
						break;
					case 2 :
						iSubMenu = Integer.parseInt( ( iCodMenu + "" ).substring( 2, 4 ) );
						break;
					case 3 :
						iSubMenu = Integer.parseInt( ( iCodMenu + "" ).substring( 4, 6 ) );
						break;
					case 4 :
						iSubMenu = Integer.parseInt( ( iCodMenu + "" ).substring( 6, 8 ) );
						break;
					case 5 :
						iSubMenu = Integer.parseInt( ( iCodMenu + "" ).substring( 8 ) );
						break;
				}
				if ( iCodModu != iCodModuAnt && iCodModuAnt != -1 )
					return pai;
				iCodModuAnt = iCodModu;
				if ( ( iSubMenuAnt != -1 ) && ( iSubMenu == 0 ) ) {
					return pai;
				}
				else if ( iSubMenu != iSubMenuAnt ) {
					item = new DefaultMutableTreeNode( Funcoes.copy( rs.getString( "DescMenu" ), 0, 40 ) + "[" + ( iConta++ ) + "]" );
					pai.add( item );
					adicAcesso( ( rs.getString( 1 ) == null ? 'A' : rs.getString( 1 ).toCharArray()[ 0 ] ), rs.getInt( "CodMenu" ), rs.getInt( "CodSis" ), rs.getInt( "CodModu" ), iNivel );
				}
				else {
					pai.add( adicFilho( rs, item, iNivel + 1 ) );
					if ( bEOF )
						return pai;
					continue;
				}
				iSubMenuAnt = iSubMenu;
				if ( !rs.next() ) {
					bEOF = true;
					break;
				}
			}
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar menus [nivel " + iNivel + " ].\n" + err.getMessage() );
		}
		return pai;
	}

	private void adicAcesso( char cAcesso, int iCodMen, int iCodSis, int iCodModu, int iNivel ) {

		String sVal[] = new String[ 5 ];
		switch ( cAcesso ) {
			case 'A' :
				sVal[ 0 ] = "0000";
				break;
			case 'B' :
				sVal[ 0 ] = "0001";
				break;
			case 'C' :
				sVal[ 0 ] = "0010";
				break;
			case 'D' :
				sVal[ 0 ] = "0011";
				break;
			case 'E' :
				sVal[ 0 ] = "0100";
				break;
			case 'F' :
				sVal[ 0 ] = "0101";
				break;
			case 'G' :
				sVal[ 0 ] = "0110";
				break;
			case 'H' :
				sVal[ 0 ] = "0111";
				break;
			case 'I' :
				sVal[ 0 ] = "1000";
				break;
			case 'J' :
				sVal[ 0 ] = "1001";
				break;
			case 'K' :
				sVal[ 0 ] = "1010";
				break;
			case 'L' :
				sVal[ 0 ] = "1011";
				break;
			case 'M' :
				sVal[ 0 ] = "1100";
				break;
			case 'N' :
				sVal[ 0 ] = "1101";
				break;
			case 'O' :
				sVal[ 0 ] = "1110";
				break;
			case 'P' :
				sVal[ 0 ] = "1111";
				break;
		}
		sVal[ 1 ] = "" + iCodMen;
		sVal[ 2 ] = "" + iCodModu;
		sVal[ 3 ] = "" + iCodSis;
		sVal[ 4 ] = "" + iNivel;
		vAcessos.add( sVal );
	}

	public ImageIcon getImagem( int iLinha, boolean bNoh, Object src ) {

		String sVal = ( (String) ( (DefaultMutableTreeNode) src ).getUserObject() );
		int iPrim = sVal.indexOf( '[' );
		int iUlt = sVal.indexOf( ']' );
		if ( iPrim == -1 || iUlt == -1 )
			return null;
		int iInd = Integer.parseInt( sVal.substring( iPrim + 1, iUlt ) );
		if ( iInd == -1 )
			return null;
		return Icone.novo( "ba" + ( ( iInd < vAcessos.size() ) && ( iInd != -1 ) ? vAcessos.elementAt( iInd )[ 0 ] : "0000" ) + ".gif" );
	}

	private void upImagem( int x, int y ) {

		char cAcesso[] = new char[ 4 ];
		int iDif = 0;
		int iLinha = arvore.getRowForLocation( x, y );
		int iNivelClick = 0;
		int iNivel = 0;
		if ( iLinha == -1 )
			return;
		DefaultMutableTreeNode src = (DefaultMutableTreeNode) arvore.getPathForRow( iLinha ).getLastPathComponent();
		String sVal = ( (String) src.getUserObject() );
		int iPrim = sVal.indexOf( '[' );
		int iUlt = sVal.indexOf( ']' );
		if ( iPrim == -1 || iUlt == -1 )
			return;
		int iInd = Integer.parseInt( sVal.substring( iPrim + 1, iUlt ) );
		if ( iInd == -1 )
			return;

		String sVals[] = vAcessos.elementAt( iInd );
		cAcesso = sVals[ 0 ].toCharArray();

		iDif = x - arvore.getRowBounds( iLinha ).x;

		if ( ( iDif >= 0 ) && ( iDif < 13 ) )
			cAcesso[ 0 ] = ( cAcesso[ 0 ] == '1' ? '0' : '1' );
		else if ( ( iDif >= 13 ) && ( iDif < 27 ) )
			cAcesso[ 1 ] = ( cAcesso[ 1 ] == '1' ? '0' : '1' );
		else if ( ( iDif >= 27 ) && ( iDif < 40 ) )
			cAcesso[ 2 ] = ( cAcesso[ 2 ] == '1' ? '0' : '1' );
		else if ( ( iDif >= 41 ) && ( iDif < 54 ) )
			cAcesso[ 3 ] = ( cAcesso[ 3 ] == '1' ? '0' : '1' );

		sVals[ 0 ] = new String( cAcesso );

		iNivelClick = Integer.parseInt( vAcessos.elementAt( iInd )[ 4 ] );
		vAcessos.setElementAt( sVals, iInd );

		for ( int i = iInd + 1; i < vAcessos.size(); i++ ) {
			char cAcessoFilho[] = new char[ 4 ];
			sVals = vAcessos.elementAt( i );

			iNivel = Integer.parseInt( ( sVals )[ 4 ] );

			if ( iNivel <= iNivelClick )
				break;

			cAcessoFilho = sVals[ 0 ].toCharArray();

			if ( ( iDif >= 0 ) && ( iDif < 13 ) )
				cAcessoFilho[ 0 ] = cAcesso[ 0 ];
			else if ( ( iDif >= 13 ) && ( iDif < 27 ) )
				cAcessoFilho[ 1 ] = cAcesso[ 1 ];
			else if ( ( iDif >= 27 ) && ( iDif < 40 ) )
				cAcessoFilho[ 2 ] = cAcesso[ 2 ];
			else if ( ( iDif >= 41 ) && ( iDif < 54 ) )
				cAcessoFilho[ 3 ] = cAcesso[ 3 ];

			sVals[ 0 ] = new String( cAcessoFilho );
			vAcessos.setElementAt( sVals, i );
		}
		arvore.updateUI();
		btSalva.setEnabled( true );
	}

	private void gravaAcessos() {

		for ( int i = 0; i < vAcessos.size(); i++ ) {
			try {
				String sSQL = "EXECUTE PROCEDURE SGUPACESSOMUSP (?,?,?,?,?,?,?)";
				PreparedStatement ps = con.prepareStatement( sSQL );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, cbFiliais.getVlrInteger().intValue() );
				ps.setString( 3, txtCodUsu.getVlrString() );
				ps.setString( 4, vAcessos.elementAt( i )[ 3 ] );
				ps.setString( 5, vAcessos.elementAt( i )[ 2 ] );
				ps.setString( 6, vAcessos.elementAt( i )[ 1 ] );
				if ( vAcessos.elementAt( i )[ 0 ].equals( "0000" ) )
					ps.setString( 7, "A" );
				else if ( vAcessos.elementAt( i )[ 0 ].equals( "0000" ) )
					ps.setString( 7, "B" );
				else if ( vAcessos.elementAt( i )[ 0 ].equals( "0010" ) )
					ps.setString( 7, "C" );
				else if ( vAcessos.elementAt( i )[ 0 ].equals( "0011" ) )
					ps.setString( 7, "D" );
				else if ( vAcessos.elementAt( i )[ 0 ].equals( "0100" ) )
					ps.setString( 7, "E" );
				else if ( vAcessos.elementAt( i )[ 0 ].equals( "0101" ) )
					ps.setString( 7, "F" );
				else if ( vAcessos.elementAt( i )[ 0 ].equals( "0110" ) )
					ps.setString( 7, "G" );
				else if ( vAcessos.elementAt( i )[ 0 ].equals( "0111" ) )
					ps.setString( 7, "H" );
				else if ( vAcessos.elementAt( i )[ 0 ].equals( "1000" ) )
					ps.setString( 7, "I" );
				else if ( vAcessos.elementAt( i )[ 0 ].equals( "1001" ) )
					ps.setString( 7, "J" );
				else if ( vAcessos.elementAt( i )[ 0 ].equals( "1010" ) )
					ps.setString( 7, "K" );
				else if ( vAcessos.elementAt( i )[ 0 ].equals( "1011" ) )
					ps.setString( 7, "L" );
				else if ( vAcessos.elementAt( i )[ 0 ].equals( "1100" ) )
					ps.setString( 7, "M" );
				else if ( vAcessos.elementAt( i )[ 0 ].equals( "1101" ) )
					ps.setString( 7, "N" );
				else if ( vAcessos.elementAt( i )[ 0 ].equals( "1110" ) )
					ps.setString( 7, "O" );
				else if ( vAcessos.elementAt( i )[ 0 ].equals( "1111" ) )
					ps.setString( 7, "P" );
				else
					ps.setString( 7, "A" );
				ps.execute();
				ps.close();
				con.commit();
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao gravar o acesso: " + i + "\n" + err.getMessage() );
				break;
			}
		}
	}

	private void montaCombo() {

		try {
			String sSQL = "SELECT FL.CODFILIAL,FL.NOMEFILIAL FROM SGFILIAL FL, SGACESSOEU AC WHERE " + "FL.CODEMP = ? AND LOWER(AC.IDUSU) = ? AND FL.CODEMP = AC.CODEMPFL AND FL.CODFILIAL = AC.CODFILIALFL";
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setString( 2, txtCodUsu.getVlrString().toLowerCase() );
			ResultSet rs = ps.executeQuery();
			vVals.clear();
			vLabs.clear();
			vVals.addElement( "" );
			vLabs.addElement( "<--Selecione-->" );
			while ( rs.next() ) {
				vVals.addElement( new Integer( rs.getInt( "CODFILIAL" ) ) );
				vLabs.addElement( rs.getString( "NOMEFILIAL" ) != null ? rs.getString( "NOMEFILIAL" ) : "" );
			}
			cbFiliais.setItens( vLabs, vVals );
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao montar a grade de filiais!\n" + err.getMessage() );
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcUsuario.setConexao( con );
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btSalva ) {
			if ( txtCodUsu.getVlrString().equals( "" ) ) {
				Funcoes.mensagemInforma( this, "Código do usuário inválido!!!" );
				return;
			}
			gravaAcessos();
			btSalva.setEnabled( false );
		}
		
		else if (evt.getSource() == btCopiaPermissoes) {
			DLCopiarPermissoes dlcopia = new DLCopiarPermissoes();
			dlcopia.setConexao( con );
			dlcopia.setVisible( true );
			
			try {

				if ( dlcopia.OK ) {
					copiarPermissoes(dlcopia.getUsuario());
					Funcoes.mensagemInforma( this, "Permissões copiadas com sucesso!!!" );
					montaArvore();
				}

			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao copiar permissões!\n" + err.getMessage() );
				err.printStackTrace();
			} finally {
				dlcopia.dispose();
			}
		}
		
		else if ( evt.getSource() == cbFiliais ) {
			montaArvore();
		}

		super.actionPerformed( evt );
	}
	
	private void copiarPermissoes(String usuario) throws SQLException {
		
		// Deleta todas as permissões do usuário para poder inserir suas novas permissões, de acordo com o usuário que foi selecionado para copia.
		String sqlDelete = "delete from sgacessomu where codemp=? and codfilial=? and idusu=? ";
		PreparedStatement ps = con.prepareStatement( sqlDelete );
		int param = 1;
		ps.setInt( param++, Aplicativo.iCodEmp );
		ps.setInt( param++, cbFiliais.getVlrInteger().intValue() );
		ps.setString( param++, txtCodUsu.getVlrString());
		
		ps.execute();
		ps.close();
		con.commit();

		//Realiza um select/insert das permissões do usuário selecionado para o usuário que receberá as novas permissões.
		StringBuilder sql = new StringBuilder();
		sql.append("insert into sgacessomu (codemp, codfilial, idusu, codsis, codmenu, codmodu, tpacessomu, planomu ) ");
		sql.append("select sg.codemp, sg.codfilial, '");
		sql.append(txtCodUsu.getVlrString());
		sql.append("' idusu, sg.codsis, sg.codmenu, sg.codmodu, sg.tpacessomu, sg.planomu from ");
		sql.append("sgacessomu sg where sg.codemp=? and sg.codfilial=? and sg.idusu=?");
		
		ps = con.prepareStatement( sql.toString() );
		param = 1;
		ps.setInt( param++, Aplicativo.iCodEmp );
		ps.setInt( param++, cbFiliais.getVlrInteger().intValue() );
		ps.setString( param++, usuario.trim() );
		
		ps.execute();
		ps.close();
		con.commit();
	}

	public void afterCarrega( CarregaEvent cevt ) {

		if ( cevt.ok ) {
			montaCombo();
			montaArvore();
		}
		else
			arvore.setModel( new DefaultTreeModel( new DefaultMutableTreeNode( "Acesso aos menus" ) ) );
		
		if (cevt.getListaCampos() == lcUsuario) {
			if ( txtCodUsu.getVlrInteger() <= 0) {
				cbFiliais.setVlrString( "" );
			}
		}
	}

	public void beforeCarrega( CarregaEvent cevt ) {

	}

	public void valorAlterado( JComboBoxEvent evt ) {
		if (evt.getComboBoxPad() == cbFiliais) {
			if(!"".equals( cbFiliais.getVlrString())) {
				btCopiaPermissoes.setEnabled( true );
			} else {
				btCopiaPermissoes.setEnabled( false );
			}
		}
	}
}
