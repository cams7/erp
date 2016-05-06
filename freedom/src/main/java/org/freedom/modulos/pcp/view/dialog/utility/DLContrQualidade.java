/**
 * @version 17/07/2008 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.pcp <BR>
 *         Classe:
 * @(#)DLContrQualidade.java <BR>
 * 
 *                           Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                           modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                           na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                           Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                           sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                           Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                           Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                           de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                           Comentários sobre a classe...
 */
package org.freedom.modulos.pcp.view.dialog.utility;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;

import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;

public class DLContrQualidade extends FFDialogo implements MouseListener, ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pnControl = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinCab = new JPanelPad( 400, 45 );

	private JTablePad tabControl = new JTablePad();

	private JScrollPane spnTabRec = new JScrollPane( tabControl );

	private JTextFieldPad txtCodOP = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtSeqOP = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodProdEst = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtRefProdEst = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtSeqEst = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtDescEst = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtQtdDist = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private JTextFieldPad txtQtdPrev = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private JTextFieldPad txtQtdDistpOp = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private JTextFieldPad txtQtdProd = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private JLabelPad lbTxtPendente = new JLabelPad( "Pendente" );

	private JLabelPad lbTxtFinalizada = new JLabelPad( "Aprovada" );

	private JLabelPad lbTxtAtrasado = new JLabelPad( "Recusada" );

	private JLabelPad lbTxtCorrigido = new JLabelPad( "Corrigido" );

	private ImageIcon imgPendente = Icone.novo( "clIndisponivelParc.gif" );

	private ImageIcon imgAprovada = Icone.novo( "clEfetivado.gif" );

	private ImageIcon imgRecusado = Icone.novo( "clVencido.gif" );

	private ImageIcon imgCorrigidas = Icone.novo( "clPagoParcial.gif" );

	private JLabelPad lbImgCorrigido = new JLabelPad( imgCorrigidas );

	private JLabelPad lbImgPendente = new JLabelPad( imgPendente );

	private JLabelPad lbImgFinalizada = new JLabelPad( imgAprovada );

	private JLabelPad lbImgAtrasado = new JLabelPad( imgRecusado );

	private JPanelPad pnBot = new JPanelPad( new FlowLayout( FlowLayout.LEFT, 5, 5 ) );

	private JPanelPad pnBotSair = new JPanelPad( new FlowLayout( FlowLayout.LEFT, 5, 5 ) );

	private ImageIcon imgStatus = null;

	private Font fontLegenda = new Font( "Arial", Font.PLAIN, 9 );

	private String sEstAnalise = "";

	public JButtonPad btOK = new JButtonPad( "OK", Icone.novo( "btOk.png" ) );

	public JButtonPad btACorretiva = new JButtonPad( "Nova ação", Icone.novo( "btNovaAcaoCorretiva.png" ) );

	public JButtonPad btAbreAcao = new JButtonPad( "Abre ação", Icone.novo( "btAbrirAcaoCorretiva.png" ) );

	private enum EcolPPOPCQ {

		STATUS, SEQOPCQ, CODESTANALISE, DESCTPANALISE, VLRMIN, VLRMAX, VLRAFER, DESCAFER, TIPOEXPEC, SEQAC, CODUNID

	};

	public DLContrQualidade( DbConnection con, boolean bPref ) {

		setTitulo( "Controle de qualidade" );
		setAtribos( 640, 380 );
		setConexao( con );
		montaTela( bPref );

		lbTxtPendente.setFont( fontLegenda );
		lbTxtFinalizada.setFont( fontLegenda );
		lbTxtAtrasado.setFont( fontLegenda );
		lbTxtCorrigido.setFont( fontLegenda );

		btOK.addActionListener( this );
		btACorretiva.addActionListener( this );
		btAbreAcao.addActionListener( this );
	}

	private void montaTela( boolean bPref ) {

		pinCab.setPreferredSize( new Dimension( 400, 100 ) );
		pnControl.add( pinCab, BorderLayout.NORTH );
		pnControl.add( spnTabRec, BorderLayout.CENTER );
		c.add( pnControl, BorderLayout.CENTER );

		setPainel( pinCab );

		adic( new JLabelPad( "Nº.OP." ), 7, 0, 80, 20 );
		adic( txtCodOP, 7, 20, 80, 20 );

		adic( new JLabelPad( "Seq.OP." ), 90, 0, 80, 20 );
		adic( txtSeqOP, 90, 20, 80, 20 );

		if ( bPref ) {
			adic( new JLabelPad( "Referência" ), 173, 0, 80, 20 );
			adic( txtRefProdEst, 173, 20, 80, 20 );
		}
		else {
			adic( new JLabelPad( "Cód.prod." ), 173, 0, 80, 20 );
			adic( txtCodProdEst, 173, 20, 80, 20 );
		}

		adic( new JLabelPad( "Qtd.prev." ), 256, 0, 80, 20 );
		adic( txtQtdPrev, 256, 20, 80, 20 );

		adic( new JLabelPad( "Qtd.prod." ), 339, 0, 80, 20 );
		adic( txtQtdProd, 339, 20, 80, 20 );

		adic( new JLabelPad( "Qtd.dist." ), 422, 0, 80, 20 );
		adic( txtQtdDistpOp, 422, 20, 80, 20 );

		adic( new JLabelPad( "Qtd.dist.at." ), 505, 0, 80, 20 );
		adic( txtQtdDist, 505, 20, 80, 20 );

		adic( new JLabelPad( "Seq.Est." ), 7, 40, 80, 20 );
		adic( txtSeqEst, 7, 60, 80, 20 );

		adic( new JLabelPad( "Descrição da estrutura principal" ), 90, 40, 250, 20 );
		adic( txtDescEst, 90, 60, 330, 20 );

		pnRodape.removeAll();
		pnRodape.add( pnBot, BorderLayout.WEST );
		pnBot.tiraBorda();
		pnBot.add( btACorretiva );
		pnBot.add( btAbreAcao );
		pnBot.add( lbImgPendente );
		pnBot.add( lbTxtPendente );
		pnBot.add( lbImgAtrasado );
		pnBot.add( lbTxtAtrasado );
		pnBot.add( lbImgFinalizada );
		pnBot.add( lbTxtFinalizada );
		pnBot.add( lbImgCorrigido );
		pnBot.add( lbTxtCorrigido );

		btOK.setPreferredSize( new Dimension( 80, 30 ) );
		pnBotSair.add( btOK );
		pnRodape.add( pnBotSair, BorderLayout.EAST );

		txtCodOP.setAtivo( false );
		txtSeqOP.setAtivo( false );
		txtCodProdEst.setAtivo( false );
		txtRefProdEst.setAtivo( false );
		txtSeqEst.setAtivo( false );
		txtDescEst.setAtivo( false );
		txtQtdProd.setAtivo( false );
		txtQtdPrev.setAtivo( false );
		txtQtdDist.setAtivo( false );
		txtQtdDistpOp.setAtivo( false );

		tabControl.adicColuna( "" );
		tabControl.adicColuna( "Seq.op.cq" );
		tabControl.adicColuna( "Cod.Estr.Análise" );
		tabControl.adicColuna( "Desc.Estr.Análise" );
		tabControl.adicColuna( "Vlr.Mín" );
		tabControl.adicColuna( "Vlr.Máx." );
		tabControl.adicColuna( "Valor aferido" );
		tabControl.adicColuna( "Desc.Aferido" );
		tabControl.adicColuna( "Tipo" );
		tabControl.adicColuna( "SEQAC" );
		tabControl.adicColuna( "Cod.Unid." );

		tabControl.setTamColuna( 10, EcolPPOPCQ.STATUS.ordinal() );
		tabControl.setTamColuna( 20, EcolPPOPCQ.SEQOPCQ.ordinal() );
		tabControl.setTamColuna( 50, EcolPPOPCQ.CODESTANALISE.ordinal() );
		tabControl.setTamColuna( 185, EcolPPOPCQ.DESCTPANALISE.ordinal() );
		tabControl.setTamColuna( 70, EcolPPOPCQ.VLRMIN.ordinal() );
		tabControl.setTamColuna( 70, EcolPPOPCQ.VLRMAX.ordinal() );
		tabControl.setTamColuna( 75, EcolPPOPCQ.VLRAFER.ordinal() );
		tabControl.setTamColuna( 140, EcolPPOPCQ.DESCAFER.ordinal() );
		tabControl.setTamColuna( 30, EcolPPOPCQ.TIPOEXPEC.ordinal() );
		tabControl.setTamColuna( 70, EcolPPOPCQ.CODUNID.ordinal() );

		tabControl.addMouseListener( this );

		tabControl.setColunaInvisivel( 8 );
		tabControl.setColunaInvisivel( 9 );
		tabControl.setColunaInvisivel( EcolPPOPCQ.CODUNID.ordinal() );

	}

	public void carregaTabela( int iCodop, int iSeqop ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sSQL = new StringBuilder();
		Vector<Object> vLinha = null;
		BigDecimal bVlrAfer = null;
		BigDecimal bVlrMin = null;
		BigDecimal bVlrmax = null;
		boolean ablBt = false;
		boolean ablBtVis = false;

		sSQL.append( "SELECT PQ.SEQOPCQ, PQ.CODESTANALISE, coalesce(PQ.VLRAFER,0.00) vlrafer , PQ.DESCAFER, PA.DESCTPANALISE, PA.TIPOEXPEC, " );
		sSQL.append( "coalesce(PE.VLRMIN,0.00) vlrmin, coalesce(PE.VLRMAX,0.00) vlrmax, PQ.STATUS, PQ.SEQAC, PA.CODUNID " );
		sSQL.append( "FROM PPOPCQ PQ, PPESTRUANALISE PE, PPTIPOANALISE PA WHERE PQ.CODEMP=? AND PQ.CODFILIAL=? AND " );
		sSQL.append( "PQ.CODOP=? AND PQ.SEQOP=? AND PE.CODEMP=PQ.CODEMPEA AND " );
		sSQL.append( "PE.CODFILIAL=PQ.CODFILIALEA AND PE.CODESTANALISE=PQ.CODESTANALISE AND " );
		sSQL.append( "PA.CODEMP=PE.CODEMPTA AND PA.CODFILIAL=PE.CODFILIALTA AND " );
		sSQL.append( "PA.CODTPANALISE=PE.CODTPANALISE" );

		try {

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PPOPCQ" ) );
			ps.setInt( 3, iCodop );
			ps.setInt( 4, iSeqop );

			rs = ps.executeQuery();

			int i = 0;
			while ( rs.next() ) {

				if ( rs.getString( "STATUS" ).equals( "PE" ) ) {
					imgStatus = imgPendente;
				}
				else if ( rs.getString( "STATUS" ).equals( "AP" ) ) {
					imgStatus = imgAprovada;
				}
				else if ( rs.getString( "STATUS" ).equals( "RC" ) ) {
					imgStatus = imgRecusado;
					if ( !ablBt ) {
						ablBt = true;
					}
				}
				if ( rs.getInt( "SEQAC" ) > 0 ) {
					imgStatus = imgCorrigidas;
					if ( !ablBtVis ) {
						ablBtVis = true;
					}
				}

				tabControl.adicLinha();

				tabControl.setValor( imgStatus, i, EcolPPOPCQ.STATUS.ordinal() );
				tabControl.setValor( rs.getInt( "SEQOPCQ" ), i, EcolPPOPCQ.SEQOPCQ.ordinal() );
				tabControl.setValor( rs.getInt( "CODESTANALISE" ), i, EcolPPOPCQ.CODESTANALISE.ordinal() );
				tabControl.setValor( rs.getString( "DESCTPANALISE" ), i, EcolPPOPCQ.DESCTPANALISE.ordinal() );
				tabControl.setValor( rs.getBigDecimal( "VLRMIN" ), i, EcolPPOPCQ.VLRMIN.ordinal() );
				tabControl.setValor( rs.getBigDecimal( "VLRMAX" ), i, EcolPPOPCQ.VLRMAX.ordinal() );
				tabControl.setValor( rs.getBigDecimal( "VLRAFER" ), i, EcolPPOPCQ.VLRAFER.ordinal() );
				tabControl.setValor( rs.getString( "DESCAFER" ), i, EcolPPOPCQ.DESCAFER.ordinal() );
				tabControl.setValor( rs.getString( "TIPOEXPEC" ), i, EcolPPOPCQ.TIPOEXPEC.ordinal() );
				tabControl.setValor( rs.getInt( "SEQAC" ), i, EcolPPOPCQ.SEQAC.ordinal() );
				tabControl.setValor( rs.getString( "CODUNID" ), i, EcolPPOPCQ.CODUNID.ordinal() );
				i++;

			}
			btACorretiva.setEnabled( ablBt );
			btAbreAcao.setEnabled( ablBtVis );

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
		}
	}

	private void alteraQual( boolean editable ) {

		int iLinha = tabControl.getLinhaSel();
		StringBuffer sSQL = new StringBuffer();
		PreparedStatement ps = null;

		try {

			String sDescAnalise = (String) tabControl.getValor( iLinha, EcolPPOPCQ.DESCTPANALISE.ordinal() );
			BigDecimal bVlrMin = tabControl.getValor( iLinha, EcolPPOPCQ.VLRMIN.ordinal() ) == null || tabControl.getValor( iLinha, EcolPPOPCQ.VLRMIN.ordinal() ).equals( "" ) ? new BigDecimal( 0 ) : (BigDecimal) tabControl.getValor( iLinha, EcolPPOPCQ.VLRMIN.ordinal() );
			BigDecimal bVlrMax = tabControl.getValor( iLinha, EcolPPOPCQ.VLRMAX.ordinal() ) == null || tabControl.getValor( iLinha, EcolPPOPCQ.VLRMAX.ordinal() ).equals( "" ) ? new BigDecimal( 0 ) : (BigDecimal) tabControl.getValor( iLinha, EcolPPOPCQ.VLRMAX.ordinal() );
			BigDecimal bVlrAfer = tabControl.getValor( iLinha, EcolPPOPCQ.VLRAFER.ordinal() ) == null || tabControl.getValor( iLinha, EcolPPOPCQ.VLRAFER.ordinal() ).equals( "" ) ? new BigDecimal( 0 ) : (BigDecimal) tabControl.getValor( iLinha, EcolPPOPCQ.VLRAFER.ordinal() );
			String sUnidade = (String) tabControl.getValor( iLinha, EcolPPOPCQ.CODUNID.ordinal() );

			String status = "PE";

			if ( imgPendente.equals( tabControl.getValor( tabControl.getLinhaSel(), EcolPPOPCQ.STATUS.ordinal() ) ) ) {
				status = "PE";
			}
			else if ( imgAprovada.equals( tabControl.getValor( tabControl.getLinhaSel(), EcolPPOPCQ.STATUS.ordinal() ) ) ) {
				status = "AP";
			}
			else if ( imgRecusado.equals( tabControl.getValor( tabControl.getLinhaSel(), EcolPPOPCQ.STATUS.ordinal() ) ) ) {
				status = "RC";
			}
			else if ( imgCorrigidas.equals( tabControl.getValor( tabControl.getLinhaSel(), EcolPPOPCQ.STATUS.ordinal() ) ) ) {
				status = "AP";
			}

			String sAfer = (String) tabControl.getValor( iLinha, EcolPPOPCQ.DESCAFER.ordinal() );
			String sTipo = (String) tabControl.getValor( iLinha, EcolPPOPCQ.TIPOEXPEC.ordinal() );

			String sUpdate = "";

			DLFechaQual dl = new DLFechaQual( sDescAnalise, sTipo, bVlrMin, bVlrMax, bVlrAfer, sAfer, status, editable, sUnidade, con );
			dl.setVisible( true );

			if ( "MM".equals( sTipo ) ) {
				sUpdate = " VLRAFER=?, STATUS=?";
			}
			else if ( "DT".equals( sTipo ) ) {
				sUpdate = " DESCAFER=?, STATUS=? ";
			}

			HashMap<String, Object> hsRet = dl.getValor();
			BigDecimal bValor = (BigDecimal) hsRet.get( "VLRAFER" );

			if ( dl.OK && editable ) {

				sSQL.append( "UPDATE PPOPCQ SET" + sUpdate + "WHERE " );
				sSQL.append( "CODEMP=? AND CODFILIAL=? AND CODOP=? AND SEQOP=? AND SEQOPCQ=?" );

				ps = con.prepareStatement( sSQL.toString() );

				if ( "MM".equals( sTipo ) ) {

					if ( ( bValor == null ) ) {
						bValor = new BigDecimal( 0 );
						status = "PE";
					}
					else {
						if ( bValor.compareTo( bVlrMin ) < 0 || bValor.compareTo( bVlrMax ) > 0 ) {
							status = "RC";
						}
						else if ( bValor.compareTo( bVlrMin ) >= 0 && bValor.compareTo( bVlrMax ) <= 0 ) {
							status = "AP";
						}
					}

					BigDecimal vlrAfer = (BigDecimal) hsRet.get( "VLRAFER" );
					ps.setBigDecimal( 1, vlrAfer );
					ps.setString( 2, status );
					ps.setInt( 3, Aplicativo.iCodEmp );
					ps.setInt( 4, ListaCampos.getMasterFilial( "PPOPCQ" ) );
					ps.setInt( 5, txtCodOP.getVlrInteger() );
					ps.setInt( 6, txtSeqOP.getVlrInteger() );
					ps.setInt( 7, (Integer) tabControl.getValor( iLinha, EcolPPOPCQ.SEQOPCQ.ordinal() ) );

				}
				else if ( "DT".equals( sTipo ) ) {

					String descAfer = (String) hsRet.get( "DESCAFER" );
					ps.setString( 1, descAfer );
					ps.setString( 2, dl.getStatus() );
					ps.setInt( 3, Aplicativo.iCodEmp );
					ps.setInt( 4, ListaCampos.getMasterFilial( "PPOPCQ" ) );
					ps.setInt( 5, txtCodOP.getVlrInteger() );
					ps.setInt( 6, txtSeqOP.getVlrInteger() );
					ps.setInt( 7, (Integer) tabControl.getValor( iLinha, EcolPPOPCQ.SEQOPCQ.ordinal() ) );
				}

				ps.executeUpdate();

				con.commit();

				tabControl.limpa();
				carregaTabela( txtCodOP.getVlrInteger(), txtSeqOP.getVlrInteger() );

			}
		} catch ( Exception e ) {

			e.getMessage();
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao salvar aferimento!" );
		}
	}

	public void carregaCampos( Object[] sValores ) {

		txtCodOP.setVlrInteger( (Integer) sValores[ 0 ] );
		txtSeqOP.setVlrInteger( (Integer) sValores[ 1 ] );
		txtCodProdEst.setVlrInteger( (Integer) sValores[ 2 ] );
		txtRefProdEst.setVlrString( (String) sValores[ 3 ] );
		txtSeqEst.setVlrInteger( (Integer) sValores[ 4 ] );
		txtDescEst.setVlrString( (String) sValores[ 5 ] );
		txtQtdProd.setVlrBigDecimal( (BigDecimal) sValores[ 6 ] );
		txtQtdPrev.setVlrBigDecimal( (BigDecimal) sValores[ 7 ] );
		txtQtdDist.setVlrBigDecimal( new BigDecimal( 0 ) );
	}

	private void acaoCorretiva( int linhaSel ) {

		Object[] keys = new Object[ 8 ];
		Integer ac = (Integer) ( linhaSel >= 0 && tabControl.getValor( linhaSel, EcolPPOPCQ.SEQAC.ordinal() ) != null ? tabControl.getValor( linhaSel, EcolPPOPCQ.SEQAC.ordinal() ) : 0 );

		keys[ DLAcaoCorretiva.EAc.CODOP.ordinal() ] = txtCodOP.getVlrInteger();
		keys[ DLAcaoCorretiva.EAc.SEQOP.ordinal() ] = txtSeqOP.getVlrInteger();
		keys[ DLAcaoCorretiva.EAc.SEQOPAC.ordinal() ] = ac;
		keys[ DLAcaoCorretiva.EAc.CODPRODEST.ordinal() ] = txtCodProdEst.getVlrInteger();
		keys[ DLAcaoCorretiva.EAc.REFPRODEST.ordinal() ] = txtRefProdEst.getVlrString();
		keys[ DLAcaoCorretiva.EAc.SEQEST.ordinal() ] = txtSeqEst.getVlrInteger();
		keys[ DLAcaoCorretiva.EAc.DESCEST.ordinal() ] = txtDescEst.getVlrString();

		if ( ac > 0 ) {
			DLAcaoCorretiva dl = new DLAcaoCorretiva( con, keys );
			dl.setVisible( true );
			if ( dl.OK ) {
				ok();
			}
		}
		else {
			DLAcoesCorretivas dl = new DLAcoesCorretivas( con, keys );
			dl.setVisible( true );
			if ( dl.OK ) {
				ok();
			}
		}

	}

	public void actionPerformed( ActionEvent evt ) {

		super.actionPerformed( evt );

		if ( evt.getSource() == btOK ) {
			dispose();
		}
		else if ( evt.getSource() == btACorretiva ) {
			acaoCorretiva( -1 );
		}
		else if ( evt.getSource() == btAbreAcao ) {
			if ( tabControl.getLinhaSel() < 0 ) {
				Funcoes.mensagemInforma( this, "Selecione uma ação no grid!" );
				return;
			}
			else {
				acaoCorretiva( tabControl.getLinhaSel() );
			}
		}
	}

	public void mouseClicked( MouseEvent mevt ) {

		if ( mevt.getClickCount() == 2 ) {
			if ( mevt.getSource() == tabControl && tabControl.getLinhaSel() >= 0 ) {
				alteraQual( imgPendente.equals( tabControl.getValor( tabControl.getLinhaSel(), EcolPPOPCQ.STATUS.ordinal() ) ) );
			}
		}
	}

	public void mouseEntered( MouseEvent e ) {

	}

	public void mouseExited( MouseEvent e ) {

	}

	public void mousePressed( MouseEvent e ) {

	}

	public void mouseReleased( MouseEvent e ) {

	}
}
