/**
 * @version 23/11/2004 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FBloqVenda.java <BR>
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

package org.freedom.modulos.std.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JOptionPane;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.modulos.pdv.FVenda;

public class FBloqVenda extends FFilho implements ActionListener, CarregaListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCli = new JPanelPad( 350, 100 );

	private JPanelPad pnRod = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTextFieldPad txtCodVenda = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 9, 0 );

	private JTextFieldPad txtDocVenda = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 9, 0 );

	private JTextFieldPad txtSerie = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtTipoVenda = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtBloqVenda = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldPad txtVlrLiqVenda = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtStatusVenda = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtDataIni = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDataFim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JCheckBoxPad cbBloquear = new JCheckBoxPad( "Bloquear", "S", "N" );

	private JButtonPad btBloquear = new JButtonPad( "Executar", Icone.novo( "btExecuta.png" ) );

	private JButtonPad btSair = new JButtonPad( "Sair", Icone.novo( "btSair.png" ) );

	private ListaCampos lcVenda = new ListaCampos( this );

	public FBloqVenda() {

		super( false );
		setTitulo( "Bloqueio e desbloqueio de vendas" );
		setAtribos( 50, 50, 380, 230 );

		// Funcoes.setBordReq(txtCodVenda);
		txtDocVenda.setAtivo( false );
		txtSerie.setAtivo( false );
		txtVlrLiqVenda.setAtivo( false );
		txtStatusVenda.setAtivo( false );
		txtBloqVenda.setAtivo( false );
		cbBloquear.setVlrString( "S" );

		lcVenda.add( new GuardaCampo( txtCodVenda, "CodVenda", "Nº pedido", ListaCampos.DB_PK, false ) );
		lcVenda.add( new GuardaCampo( txtDocVenda, "DocVenda", "Documento", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtSerie, "Serie", "Série", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtTipoVenda, "TipoVenda", "Tp.venda", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtBloqVenda, "BloqVenda", "Bloqueio", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtVlrLiqVenda, "VlrLiqVenda", "V. Liq.", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtStatusVenda, "StatusVenda", "Status", ListaCampos.DB_SI, false ) );

		lcVenda.setWhereAdic( "TIPOVENDA='V'" );
		lcVenda.montaSql( false, "VENDA", "VD" );
		lcVenda.setReadOnly( true );
		lcVenda.addCarregaListener( this );
		txtCodVenda.setTabelaExterna( lcVenda, FVenda.class.getCanonicalName() );
		txtCodVenda.setFK( true );
		txtCodVenda.setNomeCampo( "CodVenda" );

		Container c = getContentPane();
		c.setLayout( new BorderLayout() );

		btSair.setPreferredSize( new Dimension( 100, 30 ) );

		pnRod.setPreferredSize( new Dimension( 350, 30 ) );
		pnRod.add( btSair, BorderLayout.EAST );

		c.add( pnRod, BorderLayout.SOUTH );
		c.add( pinCli, BorderLayout.CENTER );

		btBloquear.setToolTipText( "Bloqueia venda" );

		pinCli.adic( new JLabelPad( "Nº Pedido" ), 7, 0, 80, 20 );
		pinCli.adic( txtCodVenda, 7, 20, 80, 20 );
		pinCli.adic( new JLabelPad( "Doc." ), 90, 0, 67, 20 );
		pinCli.adic( txtDocVenda, 90, 20, 67, 20 );
		pinCli.adic( new JLabelPad( "Série" ), 160, 0, 67, 20 );
		pinCli.adic( txtSerie, 160, 20, 67, 20 );
		pinCli.adic( new JLabelPad( "Valor" ), 230, 0, 100, 20 );
		pinCli.adic( txtVlrLiqVenda, 230, 20, 100, 20 );
		pinCli.adic( new JLabelPad( "Situação" ), 7, 40, 60, 20 );
		pinCli.adic( txtStatusVenda, 7, 60, 60, 20 );
		pinCli.adic( new JLabelPad( "Bloqueada" ), 70, 40, 70, 20 );
		pinCli.adic( txtBloqVenda, 70, 60, 70, 20 );
		pinCli.adic( new JLabelPad( "De" ), 143, 40, 90, 20 );
		pinCli.adic( txtDataIni, 143, 60, 90, 20 );
		pinCli.adic( new JLabelPad( "Até" ), 236, 40, 90, 20 );
		pinCli.adic( txtDataFim, 236, 60, 90, 20 );
		pinCli.adic( cbBloquear, 7, 90, 90, 20 );
		pinCli.adic( btBloquear, 100, 90, 120, 30 );

		btSair.addActionListener( this );
		btBloquear.addActionListener( this );
	}

	public void beforeCarrega( CarregaEvent ce ) {

		if ( ce.getListaCampos() == lcVenda ) {

		}
	}

	public void afterCarrega( CarregaEvent ce ) {

		if ( ce.getListaCampos() == lcVenda ) {
			if ( txtBloqVenda.getVlrString().equals( "S" ) )
				cbBloquear.setVlrString( "N" );
			else
				cbBloquear.setVlrString( "S" );
		}
	}

	public void bloquear() {

		int iCodVenda = 0;
		String sTipoVenda = null;
		String sStatus = null;
		String sBloqVenda = null;
		String sSQL = null;
		String sSQL2 = null;
		String sTexto = null;
		Date dtIni = null;
		Date dtFim = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;

		try {
			iCodVenda = txtCodVenda.getVlrInteger().intValue();
			sTipoVenda = txtTipoVenda.getVlrString();
			sStatus = txtStatusVenda.getVlrString();
			sBloqVenda = cbBloquear.getVlrString();
			dtIni = txtDataIni.getVlrDate();
			dtFim = txtDataFim.getVlrDate();

			if ( ( iCodVenda == 0 ) && ( ( txtDataIni.getVlrString().trim().equals( "" ) || txtDataFim.getVlrString().trim().equals( "" ) ) ) ) {
				Funcoes.mensagemInforma( this, "Selecione uma venda ou período!" );
				txtCodVenda.requestFocus();
				return;
			}
			else {
				if ( sBloqVenda.equals( "N" ) )
					sTexto = "desbloquear";
				else
					sTexto = "bloquear";
				if ( iCodVenda != 0 ) {
					txtDataIni.setVlrString( "" );
					txtDataFim.setVlrString( "" );
					if ( sStatus.substring( 0, 1 ).equals( "C" ) ) {
						Funcoes.mensagemInforma( this, "Venda cancelada!" );
						txtCodVenda.requestFocus();
						return;
					}
				}
				else {
					if ( dtIni.compareTo( dtFim ) > 0 ) {
						Funcoes.mensagemInforma( this, "Período inválido!" );
						txtDataIni.requestFocus();
						return;
					}

				}
			}
			if ( Funcoes.mensagemConfirma( this, "Deseja realmente " + sTexto + "?" ) == JOptionPane.YES_OPTION ) {
				sSQL = "EXECUTE PROCEDURE VDBLOQVENDASP(?,?,?,?,?)";
				if ( iCodVenda != 0 ) {
					ps = con.prepareStatement( sSQL );
					ps.setInt( 1, Aplicativo.iCodEmp );
					ps.setInt( 2, ListaCampos.getMasterFilial( "VDVENDA" ) );
					ps.setString( 3, sTipoVenda );
					ps.setInt( 4, iCodVenda );
					ps.setString( 5, sBloqVenda );
					ps.executeUpdate();
					ps.close();
					con.commit();
					lcVenda.carregaDados();
				}
				else {
					sSQL2 = "SELECT TIPOVENDA, CODVENDA " + "FROM VDVENDA V WHERE CODEMP=? AND CODFILIAL=? AND " + "DTEMITVENDA BETWEEN ? AND ? AND BLOQVENDA!=?";
					ps2 = con.prepareStatement( sSQL2 );
					ps2.setInt( 1, Aplicativo.iCodEmp );
					ps2.setInt( 2, ListaCampos.getMasterFilial( "VDVENDA" ) );
					ps2.setDate( 3, Funcoes.dateToSQLDate( dtIni ) );
					ps2.setDate( 4, Funcoes.dateToSQLDate( dtFim ) );
					ps2.setString( 5, sBloqVenda );

					rs2 = ps2.executeQuery();
					while ( rs2.next() ) {
						ps = con.prepareStatement( sSQL );
						ps.setInt( 1, Aplicativo.iCodEmp );
						ps.setInt( 2, ListaCampos.getMasterFilial( "VDVENDA" ) );
						ps.setString( 3, rs2.getString( "TIPOVENDA" ) );
						ps.setInt( 4, rs2.getInt( "CODVENDA" ) );
						ps.setString( 5, sBloqVenda );
						ps.executeUpdate();
						ps.close();
					}
					rs2.close();
					ps2.close();
					con.commit();
				}
			}
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro bloqueando ou desbloqueando venda!\n" + err.getMessage(), true, con, err );
		} finally {
			iCodVenda = 0;
			sTipoVenda = null;
			sStatus = null;
			sBloqVenda = null;
			sSQL = null;
			sSQL2 = null;
			sTexto = null;
			ps = null;
			rs2 = null;
			ps2 = null;
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btSair )
			dispose();
		else if ( evt.getSource() == btBloquear )
			bloquear();
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcVenda.setConexao( cn );
	}
}
