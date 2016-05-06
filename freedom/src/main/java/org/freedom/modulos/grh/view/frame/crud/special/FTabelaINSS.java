/**
 * @version 30/07/2010 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FSecaoProd.java <BR>
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
 *         Tela para cadastro de tabela de INSS
 * 
 */

package org.freedom.modulos.grh.view.frame.crud.special;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JScrollPane;

import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.type.TYPE_PRINT;

public class FTabelaINSS extends FDados implements ActionListener, PostListener, MouseListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodTabINSS = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 2, 0 );

	private JTextFieldPad txtTeto = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 8, Aplicativo.casasDecFin );
	
	private JTextFieldPad txtAliquota = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 8, Aplicativo.casasDecFin );
	
	private JPanelPad pnCampos = new JPanelPad(500,60 );
	
	private JTablePad tab = null;
	
	public FTabelaINSS() {

		super();

		setTitulo( "Tabela de INSS" );
		setAtribos( 50, 50, 350, 300 );
		criaTabela();

		lcCampos.setUsaME( false );
		
		ajustaPaineis();
		
		setPainel( pnCampos );
		
		adicCampo( txtCodTabINSS, 7, 20, 60, 20, "CodTabINSS", "Seq.", ListaCampos.DB_PK, true );
		adicCampo( txtTeto, 70, 20, 87, 20, "Teto", "Teto", ListaCampos.DB_SI, true );
		adicCampo( txtAliquota, 160, 20, 87, 20, "Aliquota", "Aliquota", ListaCampos.DB_SI, true );

		setListaCampos( true, "TABELAINSS", "RH" );

		lcCampos.setUsaFI( false );
		
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		lcCampos.setQueryInsert( false );
		lcCampos.addPostListener( this );
		tab.addMouseListener( this );

		setImprimir( true );
		
		carregaTabela();

	}
	
	private void ajustaPaineis() {
		pnCliente.removeAll();
		
		pnCliente.add( pnCampos, BorderLayout.SOUTH );
		pnCliente.add( new JScrollPane( tab ), BorderLayout.CENTER );
		
	}
	
	private void criaTabela() {

		// Tabela de pesagens

		tab = new JTablePad();
		tab.setRowHeight( 21 );

		tab.adicColuna( "Seq." );
		tab.adicColuna( "Teto" );
		tab.adicColuna( "Alíquota" );

		tab.setTamColuna( 30, 0 );
		tab.setTamColuna( 100, 1 );
		tab.setTamColuna( 80, 2 );

	}
	
	private void carregaTabela() {
		
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			tab.limpa();
			
			sql.append( "SELECT CODTABINSS, TETO, ALIQUOTA FROM RHTABELAINSS ORDER BY CODTABINSS" );
			
			ps = Aplicativo.getInstace().con.prepareStatement( sql.toString() );
			
			rs = ps.executeQuery();
			
			int lin = 0;
			int col = 0;
			
			while(rs.next()) {

				tab.adicLinha();
				
				col = 0;
				
				tab.setValor( rs.getInt( "CODTABINSS" ), lin, col++ );
				tab.setValor( rs.getBigDecimal( "TETO" ), lin, col++ );
				tab.setValor( rs.getBigDecimal( "ALIQUOTA" ), lin, col++ );
				
				lin ++;
				
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
		else if ( evt.getSource() == btImp )
			imprimir( TYPE_PRINT.PRINT);
		super.actionPerformed( evt );
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		imp.montaCab();
		imp.setTitulo( "Tabela de INSS" );

		String sSQL = "SELECT CODTABINSS, TETO, ALIQUOTA FROM RHTABELAINSS ORDER BY CODTABINSS";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = con.prepareStatement( sSQL );
			rs = ps.executeQuery();
			imp.limpaPags();

			while ( rs.next() ) {

				if ( imp.pRow() == 0 ) {
					imp.impCab( 80, false );
					imp.say( imp.pRow() + 0, 0, "" + imp.normal() );
					imp.say( imp.pRow() + 0, 0, "" );
					imp.say( imp.pRow() + 0, 2, "Seq." );
					imp.say( imp.pRow() + 0, 8, "Teto" );
					imp.say( imp.pRow() + 0, 25, "Alíquota" );
					imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
					imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "-", 79 ) );
				}

				imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
				imp.say( imp.pRow() + 0, 2, rs.getString( "CodTabINSS" ) );
				imp.say( imp.pRow() + 0, 8, Funcoes.strDecimalToStrCurrency( Aplicativo.casasDecFin, rs.getString( "Teto" )) );
				imp.say( imp.pRow() + 0, 25, Funcoes.strDecimalToStrCurrency( Aplicativo.casasDecFin, rs.getString( "Aliquota" )) );

				if ( imp.pRow() >= linPag ) {
					imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
					imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "=", 79 ) );
					imp.incPags();
					imp.eject();
				}
			}

			imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
			imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "=", 79 ) );
			imp.eject();

			imp.fechaGravacao();

			con.commit();
			
		} 
		catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela de INSS!\n" + err.getMessage(), true, con, err );
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
	}
	
	public void afterPost( PostEvent pevt ) {

		super.beforePost( pevt );

		if ( pevt.getListaCampos() == lcCampos ) {
			carregaTabela();
		}
	}

	public void mouseClicked( MouseEvent mevt ) {

		if ( mevt.getSource() == tab && mevt.getClickCount() == 2 ) {
			txtCodTabINSS .setVlrInteger( (Integer) tab.getValor( tab.getLinhaSel(), 0 ) );
			lcCampos.carregaDados();			
		}

	}


	public void mouseEntered( MouseEvent arg0 ) {

		// TODO Auto-generated method stub
		
	}

	public void mouseExited( MouseEvent arg0 ) {

		// TODO Auto-generated method stub
		
	}

	public void mousePressed( MouseEvent arg0 ) {

		// TODO Auto-generated method stub
		
	}

	public void mouseReleased( MouseEvent arg0 ) {

		// TODO Auto-generated method stub
		
	}
}
