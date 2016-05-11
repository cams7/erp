/**
 * @version 16/07/2003 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.pdv <BR>
 *         Classe:
 * @(#)DLCancCupom.java <BR>
 * 
 *                      Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                      modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                      na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                      Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                      sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                      Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                      Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                      de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                      Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.pdv;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.freedom.bmps.Icone;
import org.freedom.ecf.app.ControllerECF;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.exceptions.ExceptionCarregaDados;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.functions.Logger;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.AplicativoPDV;
import org.freedom.tef.app.ControllerTef;
import org.freedom.tef.app.ControllerTefEvent;
import org.freedom.tef.app.ControllerTefListener;
import org.freedom.tef.driver.text.TextTefAction;

public class DLCancCupom extends FDialogo implements ControllerTefListener, ActionListener, MouseListener, KeyListener {

	private static final long serialVersionUID = 1L;

	private final JPanelPad pinCab = new JPanelPad( 400, 90 );

	private final JPanelPad pnCli = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad pnLegenda = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 0, 2 ) );

	private final JTextFieldPad txtVenda = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldFK txtNota = new JTextFieldFK( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldFK txtSerie = new JTextFieldFK( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldFK txtData = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldFK txtValor = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 10, 2 );

	private final JTablePad tab = new JTablePad();

	private final JScrollPane spnTab = new JScrollPane( tab );

	private final JButtonPad btCanc = new JButtonPad( "Cancelar", Icone.novo( "btExcluir.png" ) );

	private final JButtonPad btExec = new JButtonPad( Icone.novo( "btExecuta.png" ) );

	private final JCheckBoxPad cbInteira = new JCheckBoxPad( "Cancelar venda inteira", "S", "N" );

	private final ListaCampos lcVenda = new ListaCampos( this, "VD" );

	private final ImageIcon imgCanc = Icone.novo( "clVencido.gif" );

	private final ImageIcon imgPago = Icone.novo( "clPago.gif" );

	private ImageIcon imgColuna = imgPago;

	private boolean bCancCupom = false;

	private String iCancItem = "";

	private final ControllerECF ecf;

	private ControllerTef tef;

	public DLCancCupom() {

		Funcoes.strDecimalToStrCurrency( 2, Funcoes.transValorInv( "15" ) + "" );
		setTitulo( "Cancela Venda", this.getClass().getName() );
		setAtribos( 100, 150, 715, 300 );

		ecf = new ControllerECF( AplicativoPDV.getEcfdriver(), AplicativoPDV.getPortaECF(), AplicativoPDV.bModoDemo, AplicativoPDV.getEcflayout() );

		montaListaCampos();
		montaTela();

		btCanc.setToolTipText( "Cancelar Agora." );
		btExec.setToolTipText( "Lista itens da venda" );

		btCanc.addActionListener( this );
		btExec.addActionListener( this );

		tab.addMouseListener( this );
		tab.addKeyListener( this );

		cbInteira.setVlrString( "N" );

		if ( AplicativoPDV.bTEFTerm ) {
			try {
				tef = AplicativoPDV.getControllerTef();
				tef.setControllerMessageListener( this );
			} catch ( Exception e ) {
				e.printStackTrace();
				Funcoes.mensagemErro( this, e.getMessage() );
			}
		}

		setToFrameLayout();
	}

	private void montaListaCampos() {

		txtVenda.setPK( true );
		lcVenda.add( new GuardaCampo( txtVenda, "CodVenda", "Código", ListaCampos.DB_PK, false ) );
		lcVenda.add( new GuardaCampo( txtNota, "DocVenda", "Nota", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtSerie, "Serie", "Série", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtData, "DtEmitVenda", "Data", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtValor, "VlrLiqVenda", "Valor", ListaCampos.DB_SI, false ) );
		lcVenda.montaSql( false, "VENDA", "VD" );
		lcVenda.setWhereAdic( "TIPOVENDA='E'" );
		lcVenda.setReadOnly( true );
		txtVenda.setListaCampos( lcVenda );
		txtVenda.setNomeCampo( "CodVenda" );
	}

	private void montaTela() {

		setPanel( pnCli );
		pnCli.add( spnTab, BorderLayout.CENTER );
		pnCli.add( pinCab, BorderLayout.NORTH );

		pnLegenda.add( new JLabelPad( "Não Cancelar", imgPago, SwingConstants.CENTER ) );
		pnLegenda.add( new JLabelPad( "Cancelar", imgCanc, SwingConstants.CENTER ) );

		pnRodape.add( pnLegenda, BorderLayout.WEST );

		pinCab.adic( new JLabelPad( "Cód da Venda" ), 7, 5, 80, 20 );
		pinCab.adic( txtVenda, 7, 25, 80, 20 );
		pinCab.adic( new JLabelPad( "Nota" ), 90, 5, 80, 20 );
		pinCab.adic( txtNota, 90, 25, 80, 20 );
		pinCab.adic( new JLabelPad( "Série" ), 173, 5, 30, 20 );
		pinCab.adic( txtSerie, 173, 25, 50, 20 );
		pinCab.adic( new JLabelPad( "Data" ), 226, 5, 80, 20 );
		pinCab.adic( txtData, 226, 25, 80, 20 );
		pinCab.adic( new JLabelPad( "Valor total" ), 309, 5, 120, 20 );
		pinCab.adic( txtValor, 309, 25, 120, 20 );
		pinCab.adic( btExec, 445, 15, 30, 30 );
		pinCab.adic( cbInteira, 7, 55, 200, 20 );
		pinCab.adic( btCanc, 540, 50, 120, 30 );

		tab.adicColuna( "" );
		tab.adicColuna( "Item" );
		tab.adicColuna( "Descrição" );
		tab.adicColuna( "Qtd" );
		tab.adicColuna( "Base ICMS" );
		tab.adicColuna( "Vlr. ICMS" );
		tab.adicColuna( "Preço" );
		tab.adicColuna( "Total" );
		tab.adicColuna( "Status" );

		tab.setTamColuna( 10, 0 );
		tab.setTamColuna( 28, 1 );
		tab.setTamColuna( 180, 2 );
		tab.setTamColuna( 60, 3 );
		tab.setTamColuna( 90, 4 );
		tab.setTamColuna( 90, 5 );
		tab.setTamColuna( 90, 6 );
		tab.setTamColuna( 100, 7 );
		tab.setTamColuna( 50, 8 );
	}

	private boolean processaTef() {

		String sSQL = null;
		String sNSU = null;
		String sRede = null;
		Date dTrans = null;
		BigDecimal bigVlr = null;

		try {

			sSQL = "SELECT NSUTEF,REDETEF,DTTRANSTEF,VLRTEF " + "FROM VDTEF " + "WHERE CODEMP=? AND CODFILIAL=? AND CODVENDA=? AND TIPOVENDA='E'";

			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDTEF" ) );
			ps.setInt( 3, txtVenda.getVlrInteger().intValue() );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {

				sNSU = rs.getString( "NSUTEF" );
				sRede = rs.getString( "REDETEF" );
				dTrans = Funcoes.sqlDateToDate( rs.getDate( "DTTRANSTEF" ) );
				bigVlr = rs.getBigDecimal( "VLRTEF" );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			err.printStackTrace();
			Logger.gravaLogTxt( "", Aplicativo.getUsuario().getIdusu(), Logger.LGEB_BD, "Erro ao buscar tef vinculado no banco: " + err.getMessage() );
			return false;
		}

		return tef.requestCancel( sNSU.trim(), sRede.trim(), dTrans, null, bigVlr, "" );
	}

	private boolean cancVenda() {

		boolean bRet = false;
		PreparedStatement ps = null;

		if ( isVendaComTef() ) {
			if ( processaTef() ) {
				Funcoes.mensagemInforma( this, "Não foi possível processar o cancelamento de TEF" );
				return false;
			}
		}
		// Primeiro estorna o pagamento:
		StringBuilder sql = new StringBuilder();
		sql.append( "UPDATE FNITRECEBER IR SET IR.STATUSITREC='R1' " );
		sql.append( "WHERE IR.CODEMP=? AND IR.CODFILIAL=? AND " );
		sql.append( "IR.CODREC IN (SELECT R.CODREC FROM FNRECEBER R " );
		sql.append( "WHERE R.CODEMP=IR.CODEMP AND R.CODFILIAL=IR.CODFILIAL " );
		sql.append( "AND R.CODEMPVA=? AND R.CODFILIALVA=? AND R.CODVENDA=? AND R.TIPOVENDA='E')" );

		try {

			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNRECEBER" ) );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "VDVENDA" ) );
			ps.setInt( 5, txtVenda.getVlrInteger().intValue() );
			ps.executeUpdate();
			ps.close();
			con.commit();

			bRet = true;

		} catch ( SQLException err ) {
			err.printStackTrace();
			Logger.gravaLogTxt( "", Aplicativo.getUsuario().getIdusu(), Logger.LGEB_BD, "Erro estornar o pagamento: " + err.getMessage() );
		}

		if ( bRet ) {

			sql.delete( 0, sql.length() );
			sql.append( "UPDATE VDVENDA SET STATUSVENDA='CV' " );
			sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODVENDA=? AND TIPOVENDA='E'" );

			try {

				ps = con.prepareStatement( sql.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "VDVENDA" ) );
				ps.setInt( 3, txtVenda.getVlrInteger().intValue() );
				ps.executeUpdate();
				ps.close();
				con.commit();

				bRet = true;

			} catch ( SQLException err ) {
				err.printStackTrace();
				Logger.gravaLogTxt( "", Aplicativo.getUsuario().getIdusu(), Logger.LGEB_BD, "Erro cancelar o cupom: " + err.getMessage() );
			}
		}

		return bRet;

	}

	private boolean cancItem( int iItem ) {

		boolean bRet = false;
		PreparedStatement ps = null;
		String sSQL = null;

		try {

			sSQL = "UPDATE VDITVENDA SET CANCITVENDA='S' " + "WHERE CODEMP=? AND CODFILIAL=? AND CODVENDA=? AND CODITVENDA=? AND TIPOVENDA='E'";

			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDVENDA" ) );
			ps.setInt( 3, txtVenda.getVlrInteger().intValue() );
			ps.setInt( 4, iItem );
			ps.executeUpdate();
			ps.close();
			con.commit();

			bRet = true;
		} catch ( SQLException err ) {
			err.printStackTrace();
			Logger.gravaLogTxt( "", Aplicativo.getUsuario().getIdusu(), Logger.LGEB_BD, "Erro cancelar o item " + err.getMessage() );
		}

		return bRet;

	}

	private void executaCanc() {

		if ( cbInteira.getVlrString().equals( "S" ) ) {
			if ( tab.getNumLinhas() <= 0 ) {
				Funcoes.mensagemErro( null, "Não a mais itens na venda\nEla não pode ser cancelada!" );
				return;
			}
			if ( Funcoes.mensagemConfirma( null, "Deseja realmente cancelar o cupom?" ) == JOptionPane.YES_OPTION ) {
				if ( ecf.cancelaCupom() ) {
					if ( cancVenda() ) {
						bCancCupom = true;
						btOK.doClick();
					}
				}
			}
		}
		else {
			for ( int i = 0; i < tab.getNumLinhas(); i++ ) {
				if ( ( (ImageIcon) tab.getValor( i, 0 ) ) == imgCanc ) {

					int iItem = Integer.parseInt( (String) tab.getValor( i, 1 ) );

					if ( Funcoes.mensagemConfirma( null, "Deseja realmente cancelar o item " + iItem + "?" ) == JOptionPane.YES_OPTION ) {
						if ( ecf.cancelaItem( iItem ) ) {
							if ( cancItem( iItem ) ) {
								iCancItem += "," + iItem;
								btOK.doClick();
							}
							else {
								Funcoes.mensagemErro( null, "Não foi possível cancelar o item no banco de dados." );
							}
						}
						else {
							Funcoes.mensagemErro( null, ecf.getMessageLog() );
						}
					}
				}
			}
		}

	}

	private void carregaTabela() {

		try {

			StringBuilder sql = new StringBuilder();
			sql.append( "SELECT IT.CODITVENDA,P.DESCPROD,IT.QTDITVENDA," );
			sql.append( "IT.VLRBASEICMSITVENDA,IT.VLRICMSITVENDA,IT.VLRPRODITVENDA " );
			sql.append( "FROM VDITVENDA IT, EQPRODUTO P " );
			sql.append( "WHERE IT.CODEMP=? AND IT.CODFILIAL=? AND IT.CODVENDA=? AND IT.TIPOVENDA='E' AND " );
			sql.append( "P.CODEMP=IT.CODEMPPD AND P.CODFILIAL=IT.CODFILIALPD AND P.CODPROD=IT.CODPROD AND " );
			sql.append( "(NOT IT.CANCITVENDA='S' OR IT.CANCITVENDA IS NULL) " );
			sql.append( "ORDER BY CODITVENDA" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDITVENDA" ) );
			ps.setInt( 3, txtVenda.getVlrInteger().intValue() );

			ResultSet rs = ps.executeQuery();

			tab.limpa();
			imgColuna = imgPago;
			int row = 0;

			while ( rs.next() ) {

				tab.adicLinha();
				tab.setValor( imgColuna, row, 0 );
				tab.setValor( String.valueOf( rs.getInt( "CODITVENDA" ) ), row, 1 );
				tab.setValor( rs.getString( "DESCPROD" ), row, 2 );
				tab.setValor( Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( rs.getDouble( "QTDITVENDA" ) ) ), row, 3 );
				tab.setValor( Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( rs.getDouble( "VLRBASEICMSITVENDA" ) ) ), row, 4 );
				tab.setValor( Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( rs.getDouble( "VLRICMSITVENDA" ) ) ), row, 5 );
				tab.setValor( Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( rs.getDouble( "VLRPRODITVENDA" ) ) ), row, 6 );
				tab.setValor( Funcoes.strDecimalToStrCurrency( 23, 2, String.valueOf( rs.getDouble( "VLRPRODITVENDA" ) * rs.getDouble( "QTDITVENDA" ) ) ), row, 7 );
				row++;
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro carregar ítens da venda!\n" + e.getMessage(), true, con, e );
		}
	}

	private void marcaItem( int iItem ) {

		if ( (ImageIcon) tab.getValor( iItem, 0 ) == imgCanc ) {
			imgColuna = imgPago;
		}
		else {
			imgColuna = imgCanc;
		}

		tab.setValor( imgColuna, iItem, 0 );
	}

	private boolean isVendaComTef() {

		int iRet = 0;

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT COUNT(*) FROM VDTEF WHERE CODEMP=? AND CODFILIAL=? AND CODVENDA=? AND TIPOVENDA='E'" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDTEF" ) );
			ps.setInt( 3, txtVenda.getVlrInteger().intValue() );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				iRet = rs.getInt( 1 );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
			Logger.gravaLogTxt( "", Aplicativo.getUsuario().getIdusu(), Logger.LGEB_BD, "Erro ao verificar tef vinculado no banco: " + e.getMessage() );
		}

		return iRet > 0;
	}

	public boolean isCancCupom() {

		return bCancCupom;
	}

	public int[] getCancItem() {

		String[] tmp = new String[] { "-1" };

		try {
			tmp = iCancItem.substring( 1 ).split( "," );
		} catch ( Exception e ) {
		}

		int[] ret = new int[ tmp.length ];

		for ( int i = 0; i < ret.length; i++ ) {
			ret[ i ] = Integer.parseInt( tmp[ i ] );

		}

		return ret;
	}

	public void setVenda( int iCodVenda ) {

		txtVenda.setVlrInteger( new Integer( iCodVenda ) );
	}

	public boolean actionTef( final ControllerTefEvent e ) {

		boolean actionTef = false;

		if ( e.getAction() == TextTefAction.WARNING ) {
			actionTef = true;
		}
		else if ( e.getAction() == TextTefAction.ERROR ) {
			Funcoes.mensagemErro( null, e.getMessage() );
			actionTef = true;
		}
		else if ( e.getAction() == TextTefAction.BEGIN_PRINT ) {
			actionTef = true;
		}
		else if ( e.getAction() == TextTefAction.PRINT ) {
			actionTef = ecf.relatorioGerencial( e.getMessage() );
		}
		else if ( e.getAction() == TextTefAction.END_PRINT ) {
			actionTef = ecf.fecharRelatorioGerencial();
		}
		else if ( e.getAction() == TextTefAction.RE_PRINT ) {
			actionTef = ecf.fecharRelatorioGerencial();
		}
		else if ( e.getAction() == TextTefAction.CONFIRM ) {
			actionTef = Funcoes.mensagemConfirma( this, e.getMessage() ) == JOptionPane.YES_OPTION;
		}

		return actionTef;
	}

	public void actionPerformed( ActionEvent e ) {

		if ( e.getSource() == btCanc ) {
			executaCanc();
			carregaTabela();
		}
		else if ( e.getSource() == btExec ) {
			carregaTabela();
		}

		super.actionPerformed( e );
	}

	public void mouseEntered( MouseEvent e ) {

	}

	public void mouseExited( MouseEvent e ) {

	}

	public void mousePressed( MouseEvent e ) {

	}

	public void mouseReleased( MouseEvent e ) {

	}

	public void mouseClicked( MouseEvent e ) {

		if ( e.getClickCount() == 2 && tab.getLinhaSel() >= 0 ) {
			marcaItem( tab.getLinhaSel() );
		}
	}

	public void keyTyped( KeyEvent e ) {

	}

	public void keyReleased( KeyEvent e ) {

	}

	public void keyPressed( KeyEvent e ) {

		if ( e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE ) {
			if ( e.getSource() == tab && tab.getLinhaSel() >= 0 ) {
				marcaItem( tab.getLinhaSel() );
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		lcVenda.setConexao( cn );
		super.setConexao( cn );

		if ( txtVenda.getVlrInteger().intValue() != 0 ) {
			txtVenda.setAtivo( false );
		}

		try {
			lcVenda.carregaDados();
			carregaTabela();
		} catch ( Exception e ) {
			new ExceptionCarregaDados( "Erro ao carregar dados do lista campos " + lcVenda.getName() );
		}

	}

}
