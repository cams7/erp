/**
 * @version 27/07/2011 <BR>
 * @author Setpoint Informática Ltda./Bruno Nascimento <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.crm <BR>
 *         Classe: @(#)FImagem.java <BR>
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
 *         Tela de inserção de imagem.
 * 
 */

package org.freedom.modulos.std.view.frame.crud.plain;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.freedom.acao.CarregaEvent;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.PainelImagem;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.std.view.frame.crud.plain.FCategoriaImg;
import org.freedom.modulos.std.view.dialog.report.DLRImagem;

public class FImagem extends FDados implements ActionListener, ChangeListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodImg= new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtDescImg = new JTextFieldPad( JTextFieldPad.TP_STRING, 100, 0 );
	
	private JTextFieldPad txtCodCatImg= new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );
	
	private JTextFieldFK txtDescCatImg = new JTextFieldFK( JTextFieldPad.TP_STRING, 100, 0 );
	
	private PainelImagem imFotoImg = new PainelImagem( 262144 ); // 256 KBytes
	
	private ListaCampos lcCatImg = new ListaCampos( this, "CI" );
	
	private JPanelPad pinCor = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );
	
	public FImagem() {

		this( false, null );
		 
	}
		
	public FImagem( boolean novo, DbConnection pcon ) {

		super();

		nav.setNavigation( true );

		setTitulo( "Inserção de Imagens" );
		setAtribos( 50, 50, 590, 440 );
		
		montaListaCampos();
		
		adicCampo( txtCodImg, 7, 20, 80, 20, "CODIMG", "Cod.Imagem.", ListaCampos.DB_PK, true );		
		adicCampo( txtDescImg, 90, 20, 415, 20, "DESCIMG", "Descrição da Imagem", ListaCampos.DB_SI, true );
		
		adicCampo( txtCodCatImg, 7, 60, 80, 20, "CODCATIMG", "Cod.CatImg.", ListaCampos.DB_FK, txtDescCatImg, true );		
		adicDescFK( txtDescCatImg, 90, 60, 415, 20, "DESCCATIMG", "Descrição da Categoria da Imagem" );
		
		adicDB( imFotoImg, 7, 100, 500, 200, "BinImg", "Foto:(Max 256Kb)", true ); 
	
		setListaCampos( true, "IMAGEM", "SG" );

		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		lcCampos.setQueryInsert( false );
			

		setImprimir( true );
		
		if ( novo ) {
			setConexao( pcon );
			lcCampos.insert( true );
		}

	}

	
	
	public void setCodCatImg( Integer codcatimg ) {

		txtCodCatImg.setVlrInteger( codcatimg );
		lcCatImg.carregaDados();
	} 

	private void montaListaCampos() {	
		
		lcCatImg.add( new GuardaCampo( txtCodCatImg, "CODCATIMG", "Cod.Imagem.", ListaCampos.DB_PK, true ) );
		lcCatImg.add( new GuardaCampo( txtDescCatImg, "DESCCATIMG", "Descrição da Imagem", ListaCampos.DB_SI, false ) );
		lcCatImg.montaSql( false, "CATIMG", "SG" );
		lcCatImg.setQueryCommit( false );
		lcCatImg.setReadOnly( true );
		txtCodCatImg.setTabelaExterna( lcCatImg, FCategoriaImg.class.getCanonicalName() );
		
	}
	
	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp )
			imprimir( TYPE_PRINT.VIEW );
		else if ( evt.getSource() == btImp )
			imprimir( TYPE_PRINT.PRINT); 
		super.actionPerformed( evt );
	}
	
	

	private void imprimir( TYPE_PRINT bVisualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;
		DLRImagem sl = null;
		ImprimeOS imp = null;
		int linPag = 0;

		sl = new DLRImagem();
		sl.setVisible( true );
		if ( sl.OK == false ) {
			sl.dispose();
			return;
		}

		try {

			imp = new ImprimeOS( "", con );
			linPag = imp.verifLinPag() - 1;
			imp.montaCab();
			imp.setTitulo( "Relatório de Imagens" );
			imp.limpaPags();

			sSQL = "SELECT CODIMG,DESCIMG FROM SGIMAGEM WHERE CODEMPCI=? AND CODFILIALCI=? ORDER BY " + sl.getValor();

			ps = con.prepareStatement( sSQL );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGIMAGEM" ) );
			
			rs = ps.executeQuery();
			
			while ( rs.next() ) {
				if ( imp.pRow() == 0 ) {
					imp.impCab( 80, false );
					imp.say( imp.pRow(), 0, imp.normal() );
					imp.say( imp.pRow(), 2, "Cód.Imagem.." );
					imp.say( imp.pRow(), 30, "Descrição da Imagem" );
					imp.say( imp.pRow() + 1, 0, imp.normal() );
					imp.say( imp.pRow(), 0, StringFunctions.replicate( "-", 79 ) );
				}
				imp.say( imp.pRow() + 1, 0, imp.normal() );
				imp.say( imp.pRow(), 2, rs.getString( "CodImg" ) );
				imp.say( imp.pRow(), 30, rs.getString( "DescImg" ) );
				if ( imp.pRow() >= linPag ) {
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, StringFunctions.replicate( "-", 79 ) );
					imp.incPags();
					imp.eject();
				}
			}

			imp.say( imp.pRow() + 1, 0, imp.normal() );
			imp.say( imp.pRow(), 0, StringFunctions.replicate( "=", 79 ) );
			imp.eject();

			imp.fechaGravacao();
			con.commit();
			sl.dispose();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela de Imagens!" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
			sl = null;
		}

		if ( bVisualizar==TYPE_PRINT.VIEW )
			imp.preview( this );
		else
			imp.print();
	}

	public void afterCarrega( CarregaEvent pevt ) {
		
}
	public void stateChanged( ChangeEvent e ) {
	
}
	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );


		lcCatImg.setConexao( cn );


	}
}