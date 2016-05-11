/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FCancVenda.java <BR>
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
 */
package org.freedom.modulos.std.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;

import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FTabDados;
import org.freedom.modulos.gms.view.frame.crud.detail.FCompra;

public class FTrocaSeqItens extends FTabDados implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pnVenda = new JPanelPad( 330, 220 );

	private JPanelPad pnCompra = new JPanelPad( 330, 220 );

	private JPanelPad pnRod = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTextFieldPad txtCodVenda = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 9, 0 );
	
	private JTextFieldPad txtCodItVenda = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtTipoVenda = new JTextFieldFK( JTextFieldPad.TP_STRING, 2, 0 );
	
	private JTextFieldPad txtDocVenda = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 9, 0 );
	
	private JTextFieldPad txtCodCompra = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 9, 0 );
	
	private JTextFieldPad txtCodItCompra = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtDocCompra = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 9, 0 );

	private JButtonPad btTrocaDoc = new JButtonPad( Icone.novo( "btTrocaNumero.png" ) );

	private JButtonPad btTrocaDocCompra = new JButtonPad( Icone.novo( "btTrocaNumero.png" ) );

	private JButtonPad btSair = new JButtonPad( "Sair", Icone.novo( "btSair.png" ) );

	private ListaCampos lcVenda = new ListaCampos( this );
	
	private ListaCampos lcItVenda = new ListaCampos( this );

	private ListaCampos lcCompra = new ListaCampos( this );
	
	private ListaCampos lcItCompra = new ListaCampos( this );

	private JRadioGroup<?, ?> rgTipo = null;

	private Vector<String> vLabs1 = new Vector<String>();

	private Vector<String> vVals1 = new Vector<String>();

	public FTrocaSeqItens() {

		super( false );

		setTitulo( " Reorganização de seq. de itens." );
		setAtribos( 50, 50, 350, 220 );
		txtCodVenda.setRequerido( true );

		vLabs1.addElement( "Compra" );
		vLabs1.addElement( "Venda" );
		vVals1.addElement( "C" );
		vVals1.addElement( "V" );

		rgTipo = new JRadioGroup<String, String>( 1, 2, vLabs1, vVals1 );
		rgTipo.setVlrString( "C" );

		montaTela();
		montaListaCampos();

	}

	private void montaListaCampos() {

		lcVenda.add( new GuardaCampo( txtCodVenda, "CodVenda", "Cód.Venda", ListaCampos.DB_PK, false ) );
		lcVenda.add( new GuardaCampo( txtDocVenda, "DocVenda", "Documento", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtTipoVenda, "TipoVenda", "Tp. Venda", ListaCampos.DB_SI, false ) );
		lcVenda.montaSql( false, "VENDA", "VD" );
		lcVenda.setReadOnly( true );
		txtCodVenda.setTabelaExterna( lcVenda, null );
		txtCodVenda.setFK( true );
		txtCodVenda.setNomeCampo( "CodVenda" );
		
		
		lcItVenda.add( new GuardaCampo( txtCodItVenda, "CodItVenda", "Cód.It.Venda", ListaCampos.DB_PK, true ) );
		
		lcItVenda.add( new GuardaCampo( txtCodVenda, "CodVenda", "Cód.Venda", ListaCampos.DB_PK, true ) );
		lcItVenda.add( new GuardaCampo( txtTipoVenda, "TipoVenda", "Tp.Venda", ListaCampos.DB_SI, false ) );
		lcItVenda.setDinWhereAdic( "CODVENDA=#N", txtCodVenda );
		lcItVenda.montaSql( false, "ITVENDA", "VD" );
		lcItVenda.setQueryCommit( false );
		lcItVenda.setReadOnly( true );
		txtCodItVenda.setTabelaExterna( lcItVenda, null );
		
		lcCompra.add( new GuardaCampo( txtCodCompra, "CodCompra", "Cód.compra", ListaCampos.DB_PK, true ) );
		lcCompra.add( new GuardaCampo( txtDocCompra, "DocCompra", "Documento", ListaCampos.DB_SI, false ) );
		lcCompra.montaSql( false, "COMPRA", "CP" );
		lcCompra.setReadOnly( true );
		txtCodCompra.setTabelaExterna( lcCompra, FCompra.class.getCanonicalName() );
		txtCodCompra.setFK( true );
		txtCodCompra.setNomeCampo( "CodCompra" );
		
		lcItCompra.add( new GuardaCampo( txtCodItCompra, "CodItCompra", "Cód.It.Compra", ListaCampos.DB_PK, false ) );
		lcItCompra.add( new GuardaCampo( txtCodCompra, "CodCompra", "Cód.Compra", ListaCampos.DB_PK, true ) );
		lcItCompra.setDinWhereAdic( "CODCOMPRA=#N", txtCodCompra );
		lcItCompra.montaSql( false, "ITCOMPRA", "CP" );
		lcItCompra.setQueryCommit( false );
		lcItCompra.setReadOnly( true );
		txtCodItCompra.setTabelaExterna( lcItCompra, FCompra.class.getCanonicalName() );
	}

	private void montaTela() {

		setPainel( pnVenda );
		adicTab( "Venda", pnVenda );
		pnRodape.removeAll();
		pnRodape.add( btSair, BorderLayout.EAST );
		setListaCampos( lcVenda );

		btTrocaDoc.setToolTipText( "Alterar" );
		adic( txtCodVenda, 7, 20, 80, 20, "Nº Pedido" );
		adic( btTrocaDoc, 90, 10, 30, 30 );
		btSair.addActionListener( this );
		btTrocaDoc.addActionListener( this );

		setPainel( pnCompra );
		adicTab( "Compra", pnCompra );
		setListaCampos( lcCompra );

		adic( txtCodCompra, 7, 20, 80, 20 ,"Nº Compra");
		adic( btTrocaDocCompra, 90, 10, 30, 30 );
		btTrocaDocCompra.addActionListener( this );

	}

	private void trocarSeqVenda() {
	
		StringBuffer sql = new StringBuffer();
		
		sql.append( "EXECUTE PROCEDURE VDREORGVENDASP(?,?,?,?) " );

		try {

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDITVENDA" ) );
			ps.setString( 3, txtTipoVenda.getVlrString() );
			ps.setInt( 4, txtCodVenda.getVlrInteger().intValue() );
			ps.execute();
			ps.close();
			con.commit();
			Funcoes.mensagemInforma( this, "Seq. do Item de Venda Ajustado com Sucesso!!" );

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao Alterar Seq. de Venda!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	private void trocarSeqCompra() {

		StringBuffer sql = new StringBuffer();
		sql.append( "EXECUTE PROCEDURE CPREORGCOMPRASP(?,?,?) " );

		try {
			
			PreparedStatement  ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "CPITCOMPRA" ) );
			ps.setInt( 3, txtCodCompra.getVlrInteger() );

			ps.execute();
			ps.close();
			con.commit();
			Funcoes.mensagemInforma( this, "Seq. do Item de Compra Ajustar com Sucesso!" );

		} catch ( SQLException e ) {

			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao Ajustar Seq. de Compra\n" + e.getMessage() );
		}
	}


	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btSair ) {
			dispose();
		}
		else if ( evt.getSource() == btTrocaDoc ) {
			trocarSeqVenda();
		}
		
		else if ( evt.getSource() == btTrocaDocCompra ) {
			trocarSeqCompra();
		}
		
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcVenda.setConexao( cn );
		lcItVenda.setConexao( cn );
		lcCompra.setConexao( cn );
		lcItCompra.setConexao( cn );
	}
}
