/**
 * @version 05/03/2005 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FCancVendaOrc.java <BR>
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
 *         Tela de cancelamento de vínculos entre venda e orçamento.
 * 
 */

package org.freedom.modulos.std.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;

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
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;

public class FCancVendaOrc extends FFilho implements ActionListener, CarregaListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCli = new JPanelPad( 350, 100 );

	private JPanelPad pnRod = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTextFieldPad txtCodVenda = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtDocVenda = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 9, 0 );

	private JTextFieldPad txtSerie = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtTipoVenda = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtBloqVenda = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtVlrLiqVenda = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtStatusVenda = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JButtonPad btCancelar = new JButtonPad( "Executar", Icone.novo( "btExecuta.png" ) );

	private JButtonPad btSair = new JButtonPad( "Sair", Icone.novo( "btSair.png" ) );

	private ListaCampos lcVenda = new ListaCampos( this );

	public FCancVendaOrc() {

		super( false );
		setTitulo( "Canc. de vínc. venda x orçamento" );
		setAtribos( 50, 50, 380, 230 );

		Funcoes.setBordReq( txtCodVenda );
		txtDocVenda.setAtivo( false );
		txtSerie.setAtivo( false );
		txtVlrLiqVenda.setAtivo( false );
		txtStatusVenda.setAtivo( false );
		txtBloqVenda.setAtivo( false );

		lcVenda.add( new GuardaCampo( txtCodVenda, "CodVenda", "Nro.pedido", ListaCampos.DB_PK, true ) );
		lcVenda.add( new GuardaCampo( txtDocVenda, "DocVenda", "Documento", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtSerie, "Serie", "Série", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtTipoVenda, "TipoVenda", "Tp.venda", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtBloqVenda, "BloqVenda", "Bloqueio", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtVlrLiqVenda, "VlrLiqVenda", "V. Liq.", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtStatusVenda, "StatusVenda", "Status", ListaCampos.DB_SI, false ) );
		lcVenda.montaSql( false, "VENDA", "VD" );
		lcVenda.setReadOnly( true );
		lcVenda.addCarregaListener( this );
		txtCodVenda.setTabelaExterna( lcVenda, null );
		txtCodVenda.setFK( true );
		txtCodVenda.setNomeCampo( "CodVenda" );

		Container c = getContentPane();
		c.setLayout( new BorderLayout() );

		btSair.setPreferredSize( new Dimension( 100, 30 ) );

		pnRod.setPreferredSize( new Dimension( 350, 30 ) );
		pnRod.add( btSair, BorderLayout.EAST );

		c.add( pnRod, BorderLayout.SOUTH );
		c.add( pinCli, BorderLayout.CENTER );

		btCancelar.setToolTipText( "Executar exclusão de vínculo." );

		pinCli.adic( new JLabelPad( "Nº pedido" ), 7, 0, 80, 20 );
		pinCli.adic( txtCodVenda, 7, 20, 80, 20 );
		pinCli.adic( new JLabelPad( "Doc." ), 90, 0, 67, 20 );
		pinCli.adic( txtDocVenda, 90, 20, 67, 20 );
		pinCli.adic( new JLabelPad( "Série" ), 160, 0, 67, 20 );
		pinCli.adic( txtSerie, 160, 20, 67, 20 );
		pinCli.adic( new JLabelPad( "Valor" ), 230, 0, 100, 20 );
		pinCli.adic( txtVlrLiqVenda, 230, 20, 100, 20 );
		pinCli.adic( new JLabelPad( "Situação" ), 7, 40, 70, 20 );
		pinCli.adic( txtStatusVenda, 7, 60, 70, 20 );
		pinCli.adic( new JLabelPad( "Bloqueada" ), 80, 40, 70, 20 );
		pinCli.adic( txtBloqVenda, 80, 60, 70, 20 );
		pinCli.adic( btCancelar, 7, 90, 120, 30 );

		btSair.addActionListener( this );
		btCancelar.addActionListener( this );
	}

	public void beforeCarrega( CarregaEvent ce ) {

		if ( ce.getListaCampos() == lcVenda ) {

		}
	}

	public void afterCarrega( CarregaEvent ce ) {

		if ( ce.getListaCampos() == lcVenda ) {

		}
	}

	public static void cancelar( DbConnection con, final int iCodVenda, final String sTipoVenda, final String sStatus, String sBloqVenda ) {

		String sSQL = null;
		PreparedStatement ps = null;

		try {
			if ( iCodVenda == 0 ) {
				Funcoes.mensagemInforma( null, "Nenhuma venda foi selecionada!" );
				return;
			}
			if ( sBloqVenda.equals( "S" ) ) {
				sBloqVenda = "N";
				Funcoes.mensagemInforma( null, "Esta venda encontra-se bloqueada!" );
				return;
			}
			if ( Funcoes.mensagemConfirma( null, "Confirma a exclusão do vínculo venda x orçamento?" ) == JOptionPane.YES_OPTION ) {

				sSQL = "DELETE FROM VDVENDAORC WHERE CODEMP=? AND CODFILIAL=? AND TIPOVENDA=? AND " + "CODVENDA=?";
				ps = con.prepareStatement( sSQL );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "VDVENDA" ) );
				ps.setString( 3, sTipoVenda );
				ps.setInt( 4, iCodVenda );
				ps.executeUpdate();
				ps.close();
				con.commit();
			}
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( null, "Erro cancelando vínculo venda x orçamento!\n" + err.getMessage(), true, con, err );
		} finally {
			sBloqVenda = null;
			sSQL = null;
			ps = null;
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btSair )
			dispose();
		else if ( evt.getSource() == btCancelar ) {
			cancelar( con, txtCodVenda.getVlrInteger().intValue(), txtTipoVenda.getVlrString(), txtStatusVenda.getVlrString(), txtBloqVenda.getVlrString() );
			lcVenda.carregaDados();
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcVenda.setConexao( cn );
	}

}
