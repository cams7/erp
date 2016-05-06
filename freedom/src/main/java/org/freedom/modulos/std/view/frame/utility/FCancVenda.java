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
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;

public class FCancVenda extends FFilho implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCli = new JPanelPad( 350, 100 );

	private JPanelPad pnRod = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTextFieldPad txtCodVenda = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 9, 0 );

	private JTextFieldPad txtDocVenda = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 9, 0 );

	private JTextFieldPad txtSerie = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtVlrLiqVenda = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtStatusVenda = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtBloqVenda = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtTipoVenda = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );
	
	private JTextAreaPad txaMotivoCancVenda = new JTextAreaPad(250);

	private JScrollPane spnMotivoCancVenda = new JScrollPane( txaMotivoCancVenda );

	private JButtonPad btCancelar = new JButtonPad( "Cancelar", Icone.novo( "btCancelar.png" ) );

	private JButtonPad btSair = new JButtonPad( "Sair", Icone.novo( "btSair.png" ) );

	private ListaCampos lcVenda = new ListaCampos( this );

	public FCancVenda() {

		super( false );
		setTitulo( "Cancelamento" );
		setAtribos( 50, 50, 350, 270 );

		Funcoes.setBordReq( txtCodVenda );
		txtDocVenda.setAtivo( false );
		txtSerie.setAtivo( false );
		txtVlrLiqVenda.setAtivo( false );
		//txaMotivoCancVenda.setBackground( Color.WHITE );

		lcVenda.add( new GuardaCampo( txtCodVenda, "CodVenda", "Cód.Venda", ListaCampos.DB_PK, null, false ) );
		lcVenda.add( new GuardaCampo( txtDocVenda, "DocVenda", "Documento", ListaCampos.DB_SI, null, false ) );
		lcVenda.add( new GuardaCampo( txtSerie, "Serie", "Série", ListaCampos.DB_SI, null, false ) );
		lcVenda.add( new GuardaCampo( txtBloqVenda, "BloqVenda", "Bloqueio", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtTipoVenda, "TipoVenda", "Tp.venda", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtVlrLiqVenda, "VlrLiqVenda", "V. Liq.", ListaCampos.DB_SI, null, false ) );
		lcVenda.add( new GuardaCampo( txtStatusVenda, "StatusVenda", "Status", ListaCampos.DB_SI, null, false ) );
		lcVenda.montaSql( false, "VENDA", "VD" );
		lcVenda.setReadOnly( true );
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

		btCancelar.setToolTipText( "Cancela Venda" );

		pinCli.adic( new JLabelPad( "Nº Pedido" ), 7, 0, 80, 20 );
		pinCli.adic( txtCodVenda, 7, 20, 80, 20 );
		pinCli.adic( new JLabelPad( "Doc." ), 90, 0, 67, 20 );
		pinCli.adic( txtDocVenda, 90, 20, 67, 20 );
		pinCli.adic( new JLabelPad( "Série" ), 160, 0, 67, 20 );
		pinCli.adic( txtSerie, 160, 20, 67, 20 );
		pinCli.adic( new JLabelPad( "Valor" ), 230, 0, 100, 20 );
		pinCli.adic( txtVlrLiqVenda, 230, 20, 100, 20 );
		pinCli.adic( new JLabelPad( "Motivo de cancelamento" ), 7, 40, 300, 20 );
		pinCli.adic( spnMotivoCancVenda, 7, 60, 300, 70 );
		
		pinCli.adic( btCancelar, 7, 150, 130, 30 );

		btSair.addActionListener( this );
		btCancelar.addActionListener( this );

	}

	public boolean cancelar( int iCodVenda, String sStatus, String motivocancvenda ) {

		boolean bRet = false;

		if ( iCodVenda == 0 ) {
			Funcoes.mensagemInforma( null, "Nenhuma venda foi selecionada!" );
			txtCodVenda.requestFocus();
		}
		else if ( sStatus.substring( 0, 1 ).equals( "C" ) )
			Funcoes.mensagemInforma( null, "Venda ja foi cancelada!!" );

		else if ( "VPD".indexOf( sStatus.substring( 0, 1 ) ) != -1 ) {

			if ( Funcoes.mensagemConfirma( null, "Deseja realmente cancelar esta venda?" ) == JOptionPane.YES_OPTION ) {

				PreparedStatement ps = null;
				String sSQL = "UPDATE VDVENDA SET MOTIVOCANCVENDA=?, STATUSVENDA = 'C" 
				+ sStatus.substring( 0, 1 ) 
				+ "' WHERE CODEMP=? AND CODFILIAL=? AND CODVENDA=? AND TIPOVENDA='V'";

				try {
 
					ps = con.prepareStatement( sSQL );
					int param = 1;
					ps.setString( param++, motivocancvenda );
					ps.setInt( param++, Aplicativo.iCodEmp );
					ps.setInt( param++, ListaCampos.getMasterFilial( "VDVENDA" ) );
					ps.setInt( param++, iCodVenda );
					ps.executeUpdate();

					ps.close();
					con.commit();

					bRet = true;

					FCancVendaOrc.cancelar( con, iCodVenda, txtTipoVenda.getVlrString(), txtStatusVenda.getVlrString(), txtBloqVenda.getVlrString() );

				} catch ( SQLException err ) {
					Funcoes.mensagemErro( null, "Erro ao cancelar a venda!\n" + err.getMessage(), true, con, err );
				} finally {
					ps = null;
					sSQL = null;
				}
			}

		}

		return bRet;

	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btSair )
			dispose();
		else if ( evt.getSource() == btCancelar ) {
			if ("".equals( txaMotivoCancVenda.getVlrString().trim()) ) {
				Funcoes.mensagemInforma( this, "Preencha o motivo do cancelamento !" );
				txaMotivoCancVenda.requestFocus();
				
			} else {
				cancelar( txtCodVenda.getVlrInteger().intValue(), txtStatusVenda.getVlrString(), txaMotivoCancVenda.getVlrString() );
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcVenda.setConexao( cn );
	}
}
