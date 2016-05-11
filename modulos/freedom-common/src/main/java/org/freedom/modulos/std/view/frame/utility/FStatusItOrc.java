/**
 * @version 26/04/2004 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FStatusItOrc.java <BR>
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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.freedom.bmps.Icone;

public class FStatusItOrc extends FFilho implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCli = new JPanelPad( 350, 100 );

	private JPanelPad pnRod = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTextFieldPad txtCodOrc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtItem = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldFK txtVlrLiqItOrc = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldFK txtStatusOrc = new JTextFieldFK( JTextFieldPad.TP_STRING, 2, 0 );

	private JCheckBoxPad cbEmitItOrc = new JCheckBoxPad( "Emitido", "S", "N" );

	private JButtonPad btAltEmit = new JButtonPad( "Alterar", Icone.novo( "btOk.png" ) );

	private JButtonPad btSair = new JButtonPad( "Sair", Icone.novo( "btSair.png" ) );

	private ListaCampos lcItOrc = new ListaCampos( this, "" );

	private ListaCampos lcOrc = new ListaCampos( this, "" );

	public FStatusItOrc() {

		super( false );
		setTitulo( "Ajusta status do item de orçamento" );
		setAtribos( 50, 50, 340, 210 );

		lcOrc.add( new GuardaCampo( txtCodOrc, "CodOrc", "Cód.Orç.", ListaCampos.DB_PK, true ) );
		lcOrc.add( new GuardaCampo( txtStatusOrc, "StatusOrc", "Status", ListaCampos.DB_SI, false ) );

		lcOrc.montaSql( false, "ORCAMENTO", "VD" );
		lcOrc.setReadOnly( true );
		txtCodOrc.setTabelaExterna( lcOrc, null );
		txtCodOrc.setFK( true );

		lcItOrc.add( new GuardaCampo( txtCodOrc, "CodOrc", "Cód.Orç.", ListaCampos.DB_PK, true ) );
		lcItOrc.add( new GuardaCampo( txtItem, "CodItOrc", "Item", ListaCampos.DB_PK, true ) );
		lcItOrc.add( new GuardaCampo( txtVlrLiqItOrc, "VlrLiqItOrc", "Vlr.Liq.", ListaCampos.DB_SI, false ) );
		lcItOrc.add( new GuardaCampo( cbEmitItOrc, "EmitItOrc", "Emit.", ListaCampos.DB_SI, false ) );

		lcItOrc.montaSql( false, "ITORCAMENTO", "VD" );
		lcItOrc.setReadOnly( true );
		txtCodOrc.setListaCampos( lcItOrc );
		txtCodOrc.setPK( true );
		txtCodOrc.setNomeCampo( "CodOrc" );
		txtItem.setListaCampos( lcItOrc );
		txtItem.setPK( true );
		txtItem.setNomeCampo( "CodItOrc" );
		System.out.println( lcItOrc.getWhereAdic() );

		Container c = getContentPane();
		c.setLayout( new BorderLayout() );

		btSair.setPreferredSize( new Dimension( 100, 30 ) );

		pnRod.setPreferredSize( new Dimension( 350, 30 ) );
		pnRod.add( btSair, BorderLayout.EAST );

		c.add( pnRod, BorderLayout.SOUTH );
		c.add( pinCli, BorderLayout.CENTER );

		btAltEmit.setToolTipText( "Alterar" );

		pinCli.adic( new JLabelPad( "Nº orçamento" ), 7, 0, 80, 20 );
		pinCli.adic( txtCodOrc, 7, 20, 80, 20 );
		pinCli.adic( new JLabelPad( "Item" ), 90, 0, 67, 20 );
		pinCli.adic( txtItem, 90, 20, 67, 20 );
		pinCli.adic( new JLabelPad( "Valor" ), 160, 0, 100, 20 );
		pinCli.adic( txtVlrLiqItOrc, 160, 20, 100, 20 );
		pinCli.adic( new JLabelPad( "Status" ), 7, 40, 40, 20 );
		pinCli.adic( txtStatusOrc, 7, 60, 40, 20 );
		pinCli.adic( cbEmitItOrc, 60, 60, 73, 20 );
		pinCli.adic( btAltEmit, 7, 90, 120, 30 );

		btSair.addActionListener( this );
		btAltEmit.addActionListener( this );
	}

	private void trocar() {

		if ( txtItem.getVlrString().equals( "" ) ) {
			Funcoes.mensagemInforma( this, "Nenhum item foi selecionado!" );
			txtCodOrc.requestFocus();
			return;
		}
		String sSQL = "UPDATE VDITORCAMENTO SET EMITITORC=? WHERE CODORC=? AND " + "CODITORC=? AND CODEMP=? AND CODFILIAL=?";
		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setString( 1, cbEmitItOrc.getVlrString() );
			ps.setInt( 2, txtCodOrc.getVlrInteger().intValue() );
			ps.setInt( 3, txtItem.getVlrInteger().intValue() );
			ps.setInt( 4, Aplicativo.iCodEmp );
			ps.setInt( 5, ListaCampos.getMasterFilial( "VDORCAMENTO" ) );
			ps.executeUpdate();
			ps.close();
			con.commit();
			Funcoes.mensagemInforma( this, "intem alterado com sucesso!" );
		}

		catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao alterar o item!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btSair )
			dispose();
		else if ( evt.getSource() == btAltEmit )
			trocar();
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcItOrc.setConexao( cn );
		lcOrc.setConexao( cn );
	}
}
