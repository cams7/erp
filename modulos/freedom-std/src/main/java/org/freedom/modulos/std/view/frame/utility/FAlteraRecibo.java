/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Armando Nogueira <BR>
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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

public class FAlteraRecibo extends FFilho implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCli = new JPanelPad( 350, 100 );

	private JPanelPad pnRod = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTextFieldPad txtCodVenda = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodRec = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtVlrParcRec = new JTextFieldFK( JTextFieldFK.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldFK txtVlrParcItRec = new JTextFieldFK( JTextFieldFK.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JTextFieldPad txtReciboItRec = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtNParcItRec = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JButtonPad btTrocaDoc = new JButtonPad( Icone.novo( "btTrocaNumero.png" ) );

	private JButtonPad btSair = new JButtonPad( "Sair", Icone.novo( "btSair.png" ) );

	private ListaCampos lcReceber = new ListaCampos( this, "" );

	private ListaCampos lcItReceber = new ListaCampos( this, "" );

	public FAlteraRecibo() {

		super( false );
		setTitulo( "Troca de documento" );
		setAtribos( 50, 50, 270, 200 );

		btSair.addActionListener( this );
		btTrocaDoc.addActionListener( this );

		montaTela();
	}

	public void montaListaCampos() {

		lcReceber.add( new GuardaCampo( txtCodRec, "CodRec", "Cód.rec.", ListaCampos.DB_PK, true ) );
		lcReceber.add( new GuardaCampo( txtCodVenda, "CodVenda", "Cód.venda", ListaCampos.DB_SI, false ) );
		lcReceber.add( new GuardaCampo( txtVlrParcRec, "VlrParcRec", "Valor Total", ListaCampos.DB_SI, false ) );

		lcReceber.montaSql( false, "RECEBER", "FN" );
		lcReceber.setReadOnly( true );
		txtCodRec.setTabelaExterna( lcReceber, null );
		txtCodRec.setFK( true );
		txtCodRec.setNomeCampo( "CodRec" );

		lcItReceber.add( new GuardaCampo( txtCodRec, "CodRec", "Cód.rec.", ListaCampos.DB_PF, true ) );
		lcItReceber.add( new GuardaCampo( txtNParcItRec, "NParcItRec", "Nº parc.", ListaCampos.DB_PK, true ) );
		lcItReceber.add( new GuardaCampo( txtVlrParcItRec, "VlrParcItRec", "Vlr.parc.", ListaCampos.DB_SI, false ) );
		lcItReceber.add( new GuardaCampo( txtReciboItRec, "ReciboItRec", "Nº recibo", ListaCampos.DB_SI, false ) );
		lcItReceber.setMaster( lcReceber );
		lcItReceber.montaSql( false, "ITRECEBER", "FN" );
		lcItReceber.setReadOnly( true );

		txtNParcItRec.setTabelaExterna( lcItReceber, null );
		txtNParcItRec.setFK( true );
		txtNParcItRec.setNomeCampo( "NParcItRec" );
	}

	public void montaTela() {

		Container c = getContentPane();
		c.setLayout( new BorderLayout() );

		btSair.setPreferredSize( new Dimension( 100, 30 ) );

		pnRod.setPreferredSize( new Dimension( 350, 30 ) );
		pnRod.add( btSair, BorderLayout.EAST );

		c.add( pnRod, BorderLayout.SOUTH );
		c.add( pinCli, BorderLayout.CENTER );

		btTrocaDoc.setToolTipText( "Alterar" );

		txtNParcItRec.setRequerido( true );

		txtVlrParcRec.setSoLeitura( true );
		txtVlrParcItRec.setSoLeitura( true );
		txtCodVenda.setSoLeitura( true );

		pinCli.adic( new JLabelPad( "Cód.rec." ), 7, 0, 70, 20 );
		pinCli.adic( txtCodRec, 7, 20, 70, 20 );
		pinCli.adic( new JLabelPad( "Nº pedido" ), 80, 0, 70, 20 );
		pinCli.adic( txtCodVenda, 80, 20, 70, 20 );
		pinCli.adic( new JLabelPad( "Vlr.total" ), 153, 0, 70, 20 );
		pinCli.adic( txtVlrParcRec, 153, 20, 70, 20 );
		pinCli.adic( new JLabelPad( "Nº parcela" ), 7, 40, 100, 20 );
		pinCli.adic( txtNParcItRec, 7, 60, 70, 20 );
		pinCli.adic( new JLabelPad( "Vlr.parcela" ), 153, 40, 70, 20 );
		pinCli.adic( txtVlrParcItRec, 153, 60, 70, 20 );
		pinCli.adic( btTrocaDoc, 190, 90, 30, 30 );
		pinCli.adic( new JLabelPad( "Nº Recibo" ), 80, 40, 100, 20 );
		pinCli.adic( txtReciboItRec, 80, 60, 70, 20 );
	}

	private void trocaDoc() {

		PreparedStatement ps = null;
		String sSQL = "UPDATE FNITRECEBER SET RECIBOITREC=? WHERE CODREC=? AND CODEMP=? AND CODFILIAL=?";

		try {
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, txtReciboItRec.getVlrInteger().intValue() );
			ps.setInt( 2, txtCodRec.getVlrInteger().intValue() );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "FNITRECEBER" ) );
			ps.executeUpdate();
			ps.close();

			con.commit();
			Funcoes.mensagemInforma( this, "Numero do recibo alterado com Sucesso!" );

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao alterar numero do recibo!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btSair )
			dispose();

		else if ( evt.getSource() == btTrocaDoc )
			trocaDoc();

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcReceber.setConexao( cn );
		lcItReceber.setConexao( cn );
		montaListaCampos();
	}
}
