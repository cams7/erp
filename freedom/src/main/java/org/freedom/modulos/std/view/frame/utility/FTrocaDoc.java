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
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FTabDados;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.freedom.bmps.Icone;

public class FTrocaDoc extends FTabDados implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pnVenda = new JPanelPad( 330, 220 );

	private JPanelPad pnCompra = new JPanelPad( 330, 220 );

	private JPanelPad pnRod = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTextFieldPad txtCodVenda = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtSerie = new JTextFieldFK( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldFK txtVlrLiqVenda = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldFK txtStatusVenda = new JTextFieldFK( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtNovoDoc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 9, 0 );

	private JTextFieldPad txtCodCompra = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 9, 0 );

	private JTextFieldPad txtDocCompra = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 9, 0 );

	private JTextFieldFK txtVlrCompra = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private JButtonPad btTrocaDoc = new JButtonPad( Icone.novo( "btTrocaNumero.png" ) );

	private JButtonPad btTrocaDocCompra = new JButtonPad( Icone.novo( "btTrocaNumero.png" ) );

	private JButtonPad btSair = new JButtonPad( "Sair", Icone.novo( "btSair.png" ) );

	private ListaCampos lcVenda = new ListaCampos( this );

	private ListaCampos lcCompra = new ListaCampos( this );

	private JRadioGroup<?, ?> rgTipo = null;

	private Vector<String> vLabs1 = new Vector<String>();

	private Vector<String> vVals1 = new Vector<String>();

	public FTrocaDoc() {

		super( false );

		setTitulo( "Alteração do número do doc." );
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

		lcVenda.add( new GuardaCampo( txtCodVenda, "CodVenda", "Cód.venda", ListaCampos.DB_PK, false ) );
		lcVenda.add( new GuardaCampo( txtNovoDoc, "DocVenda", "Documento", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtSerie, "Serie", "Série", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtVlrLiqVenda, "VlrLiqVenda", "V. liq.", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtStatusVenda, "StatusVenda", "Status", ListaCampos.DB_SI, false ) );
		lcVenda.montaSql( false, "VENDA", "VD" );
		lcVenda.setReadOnly( true );
		txtCodVenda.setTabelaExterna( lcVenda, null );
		txtCodVenda.setFK( true );
		txtCodVenda.setNomeCampo( "CodVenda" );

		lcCompra.add( new GuardaCampo( txtCodCompra, "CodCompra", "Cód.compra", ListaCampos.DB_PK, true ) );
		lcCompra.add( new GuardaCampo( txtDocCompra, "DocCompra", "Documento", ListaCampos.DB_SI, false ) );
		lcCompra.add( new GuardaCampo( txtVlrCompra, "VlrLiqCompra", "Valor da compra", ListaCampos.DB_SI, false ) );
		lcCompra.montaSql( false, "COMPRA", "CP" );
		lcCompra.setReadOnly( true );
		txtCodCompra.setTabelaExterna( lcCompra, null );
		txtCodCompra.setFK( true );
		txtCodCompra.setNomeCampo( "CodCompra" );

	}

	private void montaTela() {

		setPainel( pnVenda );
		adicTab( "Venda", pnVenda );
		pnRodape.removeAll();
		pnRodape.add( btSair, BorderLayout.EAST );
		setListaCampos( lcVenda );

		btTrocaDoc.setToolTipText( "Alterar" );
		adic( new JLabelPad( "Nº pedido" ), 7, 0, 80, 20 );
		adic( txtCodVenda, 7, 20, 80, 20 );
		adic( new JLabelPad( "Série" ), 90, 0, 67, 20 );
		adic( txtSerie, 90, 20, 67, 20 );
		adic( new JLabelPad( "Valor" ), 160, 0, 100, 20 );
		adic( txtVlrLiqVenda, 160, 20, 100, 20 );
		adic( new JLabelPad( "Doc." ), 7, 40, 73, 20 );
		adic( txtNovoDoc, 7, 60, 73, 20 );
		adic( btTrocaDoc, 90, 50, 30, 30 );
		btSair.addActionListener( this );
		btTrocaDoc.addActionListener( this );

		setPainel( pnCompra );
		adicTab( "Compra", pnCompra );
		setListaCampos( lcCompra );

		adic( new JLabelPad( "Cód.Compra" ), 7, 0, 80, 20 );
		adic( txtCodCompra, 7, 20, 80, 20 );
		adic( new JLabelPad( "Valor" ), 90, 0, 100, 20 );
		adic( txtVlrCompra, 90, 20, 100, 20 );
		adic( new JLabelPad( "Dóc.Compra" ), 7, 40, 80, 20 );
		adic( txtDocCompra, 7, 60, 80, 20 );
		adic( btTrocaDocCompra, 90, 50, 30, 30 );
		btTrocaDocCompra.addActionListener( this );

	}

	private void trocarDocVenda() {

		if ( txtStatusVenda.getVlrString().equals( "" ) ) {

			Funcoes.mensagemInforma( this, "Nenhuma venda foi selecionada!" );
			txtCodVenda.requestFocus();
			return;
		}

		String sImpNota = "";
		String sSQL1 = "SELECT IMPNOTAVENDA FROM VDVENDA WHERE CODVENDA=? AND CODEMP=? AND CODFILIAL=? AND TIPOVENDA='V'";
		String sSQL2 = "UPDATE VDVENDA SET IMPNOTAVENDA = 'N',DOCVENDA=? WHERE CODVENDA=? AND CODEMP=? AND CODFILIAL=? AND TIPOVENDA='V'";
		String sSQL3 = "UPDATE VDVENDA SET IMPNOTAVENDA = ? WHERE CODVENDA=? AND CODEMP=? AND CODFILIAL=? AND TIPOVENDA='V'";
		String sSQL4 = "UPDATE FNRECEBER SET DOCREC=? WHERE CODVENDA=? AND CODEMP=? AND CODFILIAL=? AND TIPOVENDA='V'";

		try {

			// 1a. query:
			PreparedStatement ps = con.prepareStatement( sSQL1 );
			ps.setInt( 1, txtCodVenda.getVlrInteger().intValue() );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "VDVENDA" ) );
			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				sImpNota = rs.getString( "ImpNotaVenda" );
			}
			rs.close();
			ps.close();

			// 2a. query:
			ps = con.prepareStatement( sSQL2 );
			ps.setInt( 1, txtNovoDoc.getVlrInteger().intValue() );
			ps.setInt( 2, txtCodVenda.getVlrInteger().intValue() );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "VDVENDA" ) );
			ps.executeUpdate();
			ps.close();

			// 3a. query:
			ps = con.prepareStatement( sSQL3 );
			ps.setString( 1, sImpNota );
			ps.setInt( 2, txtCodVenda.getVlrInteger().intValue() );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "VDVENDA" ) );
			ps.executeUpdate();
			ps.close();

			// 4a. query:
			ps = con.prepareStatement( sSQL4 );
			ps.setInt( 1, txtNovoDoc.getVlrInteger().intValue() );
			ps.setInt( 2, txtCodVenda.getVlrInteger().intValue() );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "VDVENDA" ) );
			ps.executeUpdate();
			ps.close();

			con.commit();
			Funcoes.mensagemInforma( this, "Numero da nota Alterado com Sucesso!" );

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao alterar a venda!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	private void trocarDocCompra() {

		StringBuilder sUpdate1 = new StringBuilder();
		StringBuilder sUpdate2 = new StringBuilder();
		StringBuilder sUpdate3 = new StringBuilder();

		PreparedStatement ps = null;

		sUpdate1.append( "update cpcompra cp set cp.emmanut='S' where codemp=? and codfilial=? and codcompra=?" );
		sUpdate2.append( "update cpcompra cp set cp.doccompra=? where codemp=? and codfilial=? and codcompra=?" );
		sUpdate3.append( "update cpcompra cp set cp.emmanut='N' where codemp=? and codfilial=? and codcompra=?" );

		try {

			// Mudando registro para emmanut (Manutenção)
			ps = con.prepareStatement( sUpdate1.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
			ps.setInt( 3, txtCodCompra.getVlrInteger() );

			ps.executeUpdate();

			// Alterando o numero do documento

			ps = con.prepareStatement( sUpdate2.toString() );
			ps.setInt( 1, txtDocCompra.getVlrInteger() );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
			ps.setInt( 4, txtCodCompra.getVlrInteger() );

			int nroreg = ps.executeUpdate();

			// Tirando o registro de manutenção
			ps = con.prepareStatement( sUpdate3.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
			ps.setInt( 3, txtCodCompra.getVlrInteger() );

			ps.executeUpdate();

			con.commit();

			if ( nroreg > 0 ) {
				Funcoes.mensagemInforma( this, "N° do documento alterado com sucesso!" );
			}
			else {
				Funcoes.mensagemInforma( this, "Nenhum documento foi alterado!" );
			}

		} catch ( SQLException e ) {

			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao Alterar n° do documento\n" + e.getMessage() );
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btSair ) {
			dispose();
		}
		else if ( evt.getSource() == btTrocaDoc ) {
			trocarDocVenda();
		}
		else if ( evt.getSource() == btTrocaDocCompra ) {
			trocarDocCompra();
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcVenda.setConexao( cn );
	}
}
