/**
 * @version 04/2007 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * @author Alex Rodrigues<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.rep <BR>
 *         Classe:
 * @(#)RPConsPedido.java <BR>
 * 
 *                       Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                       modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                       na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                       Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                       sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                       Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                       Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                       de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                       Tela de consulta de pedidos.
 * 
 */

package org.freedom.modulos.rep;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
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
import org.freedom.library.swing.frame.FFilho;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.type.TYPE_PRINT;

public class RPComissao extends FFilho implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;

	private final JPanelPad panelConsulta = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad panelCampos = new JPanelPad();

	private final JPanelPad panelTabela = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad panelLegenda = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 0, 3 ) );

	private final JPanelPad panelPrint = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 2 ) );

	private final JPanelPad panelCenterRodape = new JPanelPad( JPanelPad.TP_JPANEL, new FlowLayout( FlowLayout.CENTER, 0, 0 ) );

	private final JPanelPad panelRodape;

	private final JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldFK txtNomeVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private final JTextFieldPad txtDtIni = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtDtFim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JCheckBoxPad cbNaoPagas = new JCheckBoxPad( "Não Pagas", "S", "N" );

	private final JCheckBoxPad cbPagasParcial = new JCheckBoxPad( "Pagas Parcialmente", "S", "N" );

	private final JCheckBoxPad cbPagas = new JCheckBoxPad( "Pagas", "S", "N" );

	private final JButtonPad btPesquisar = new JButtonPad( "Pesquisar", Icone.novo( "btObs1.png" ) );

	private final JButtonPad btImp = new JButtonPad( Icone.novo( "btImprime.png" ) );

	private final JButtonPad btPrevimp = new JButtonPad( Icone.novo( "btPrevimp.png" ) );

	private final JTablePad tabConsulta = new JTablePad();

	private final ImageIcon imgNaoPago = Icone.novo( "clVencido.gif" );

	private final ImageIcon imgPago = Icone.novo( "clPago.gif" );

	private final ImageIcon imgPagoParcial = Icone.novo( "clPagoParcial.gif" );

	private ImageIcon imgColuna = null;

	private final ListaCampos lcVendedor = new ListaCampos( this, "" );

	public RPComissao() {

		super( false );
		setTitulo( "Manutenção de Comissões" );
		setAtribos( 50, 50, 700, 400 );

		panelRodape = adicBotaoSair();
		montaListaCampos();
		montaTela();

		Calendar cal = Calendar.getInstance();
		txtDtFim.setVlrDate( cal.getTime() );
		cal.set( cal.get( Calendar.YEAR ), cal.get( Calendar.MONTH ) - 1, cal.get( Calendar.DATE ) );
		txtDtIni.setVlrDate( cal.getTime() );

		btPesquisar.addActionListener( this );

		btImp.setToolTipText( "Imprimir (Ctrl + I)" );
		btPrevimp.setToolTipText( "Visualizar Impressão (Ctrl + P)" );
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );

		tabConsulta.addMouseListener( this );
	}

	private void montaListaCampos() {

		/************
		 * VENDEDOR *
		 ************/

		lcVendedor.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.vend.", ListaCampos.DB_PK, false ) );
		lcVendedor.add( new GuardaCampo( txtNomeVend, "NomeVend", "Nome do vendedor", ListaCampos.DB_SI, false ) );
		lcVendedor.montaSql( false, "VENDEDOR", "RP" );
		lcVendedor.setQueryCommit( false );
		lcVendedor.setReadOnly( true );
		txtCodVend.setTabelaExterna( lcVendedor, null );
		txtCodVend.setFK( true );
		txtCodVend.setNomeCampo( "CODVEND" );
	}

	private void montaTela() {

		panelCampos.setPreferredSize( new Dimension( 400, 160 ) );

		panelCampos.adic( new JLabel( "Cód.vend." ), 7, 10, 100, 20 );
		panelCampos.adic( txtCodVend, 7, 30, 100, 20 );
		panelCampos.adic( new JLabel( "Nome do comissionado" ), 110, 10, 260, 20 );
		panelCampos.adic( txtNomeVend, 110, 30, 360, 20 );

		Funcoes.setBordReq( txtCodVend );

		JLabel periodo = new JLabel( "Periodo", SwingConstants.CENTER );
		periodo.setOpaque( true );
		JLabel borda = new JLabel();
		borda.setBorder( BorderFactory.createEtchedBorder() );

		panelCampos.adic( periodo, 20, 55, 60, 20 );
		panelCampos.adic( borda, 7, 65, 226, 75 );

		panelCampos.adic( txtDtIni, 20, 95, 80, 20 );
		panelCampos.adic( new JLabel( "até", SwingConstants.CENTER ), 100, 95, 40, 20 );
		panelCampos.adic( txtDtFim, 140, 95, 80, 20 );

		JLabel filtros = new JLabel( "Filtros", SwingConstants.CENTER );
		filtros.setOpaque( true );
		JLabel borda2 = new JLabel();
		borda2.setBorder( BorderFactory.createEtchedBorder() );

		panelCampos.adic( filtros, 250, 55, 60, 20 );
		panelCampos.adic( borda2, 240, 65, 230, 75 );

		panelCampos.adic( cbNaoPagas, 250, 75, 200, 20 );
		panelCampos.adic( cbPagasParcial, 250, 95, 200, 20 );
		panelCampos.adic( cbPagas, 250, 115, 200, 20 );

		panelCampos.adic( btPesquisar, 490, 30, 180, 30 );

		panelTabela.add( new JScrollPane( tabConsulta ), BorderLayout.CENTER );

		panelConsulta.add( panelCampos, BorderLayout.NORTH );
		panelConsulta.add( panelTabela, BorderLayout.CENTER );

		pnCliente.add( panelConsulta, BorderLayout.CENTER );

		panelLegenda.add( new JLabelPad( "Não Pago", imgNaoPago, SwingConstants.CENTER ) );
		panelLegenda.add( new JLabelPad( "Pago Parcial", imgPagoParcial, SwingConstants.CENTER ) );
		panelLegenda.add( new JLabelPad( "Pago", imgPago, SwingConstants.CENTER ) );
		panelRodape.add( panelLegenda, BorderLayout.WEST );

		panelPrint.setPreferredSize( new Dimension( 80, 28 ) );
		panelPrint.add( btImp );
		panelPrint.add( btPrevimp );

		panelCenterRodape.add( panelPrint );

		panelRodape.add( panelCenterRodape, BorderLayout.CENTER );

		tabConsulta.adicColuna( "" );
		tabConsulta.adicColuna( "Pedido" );
		tabConsulta.adicColuna( "Item" );
		tabConsulta.adicColuna( "Comissão" );
		tabConsulta.adicColuna( "Pago" );
		tabConsulta.adicColuna( "Em aberto" );
		tabConsulta.adicColuna( "Pagamento" );

		tabConsulta.setTamColuna( 20, EConsulta.STATUS.ordinal() );
		tabConsulta.setTamColuna( 100, EConsulta.PEDIDO.ordinal() );
		tabConsulta.setTamColuna( 60, EConsulta.ITEM.ordinal() );
		tabConsulta.setTamColuna( 120, EConsulta.VALORCOMISS.ordinal() );
		tabConsulta.setTamColuna( 120, EConsulta.VALORPAGO.ordinal() );
		tabConsulta.setTamColuna( 120, EConsulta.VALORABERTO.ordinal() );
		tabConsulta.setTamColuna( 120, EConsulta.DATA.ordinal() );
	}

	private void carregaTabela() {

		if ( txtCodVend.getVlrInteger() == 0 ) {
			Funcoes.mensagemInforma( this, "Informe o vendedor!" );
			txtCodVend.requestFocus();
			return;
		}

		if ( txtDtIni.getVlrDate() != null && txtDtFim.getVlrDate() != null ) {
			if ( txtDtFim.getVlrDate().before( txtDtIni.getVlrDate() ) ) {
				Funcoes.mensagemInforma( this, "Data final inferior a inicial!" );
				return;
			}
		}

		try {

			tabConsulta.limpa();

			String status = "";

			if ( "S".equals( cbNaoPagas.getVlrString() ) ) {
				status += "'PE'";
			}
			if ( "S".equals( cbPagasParcial.getVlrString() ) ) {
				status += status.length() > 0 ? ",'PP'" : "'PP'";
			}
			if ( "S".equals( cbPagas.getVlrString() ) ) {
				status += status.length() > 0 ? ",'CF'" : "'CF'";
			}

			StringBuilder sql = new StringBuilder();

			sql.append( "SELECT " );
			sql.append( "  C.CODPED, C.CODITPED, C.STATUSCOMISS," );
			sql.append( "  C.VLRCOMISS, COALESCE(C.VLRPAGO,0) VLRPAGO, C.VLRCOMISS-COALESCE(C.VLRPAGO,0) VLRABERTO, C.DTPAGO " );
			sql.append( "FROM RPCOMISSAO C, RPFATURAMENTO F " );
			sql.append( "WHERE " );
			sql.append( "  C.CODEMP=? AND C.CODFILIAL=? AND C.CODEMPVD=C.CODEMP AND C.CODFILIALVD=C.CODFILIAL AND C.CODVEND=? AND " );
			sql.append( "  F.CODEMP=C.CODEMP AND F.CODFILIAL=C.CODFILIAL AND F.CODPED=C.CODPED AND F.CODITPED=C.CODITPED AND " );
			sql.append( "  STATUSCOMISS IN (" + status + ") AND (" );
			sql.append( "  C.DTPAGO BETWEEN ? AND ? " );
			if ( "S".equals( cbNaoPagas.getVlrString() ) ) {
				sql.append( "  OR F.DTFATURADO BETWEEN ? AND ? " );
			}
			sql.append( "  ) " );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			int param = 1;
			ps.setInt( param++, AplicativoRep.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "RPCOMISSAO" ) );
			ps.setInt( param++, txtCodVend.getVlrInteger() );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDtIni.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDtFim.getVlrDate() ) );
			if ( "S".equals( cbNaoPagas.getVlrString() ) ) {
				ps.setDate( param++, Funcoes.dateToSQLDate( txtDtIni.getVlrDate() ) );
				ps.setDate( param++, Funcoes.dateToSQLDate( txtDtFim.getVlrDate() ) );
			}

			ResultSet rs = ps.executeQuery();

			for ( int i = 0; rs.next(); i++ ) {

				if ( "PE".equals( rs.getString( "STATUSCOMISS" ) ) ) {
					imgColuna = imgNaoPago;
				}
				else if ( "PP".equals( rs.getString( "STATUSCOMISS" ) ) ) {
					imgColuna = imgPagoParcial;
				}
				else if ( "CF".equals( rs.getString( "STATUSCOMISS" ) ) ) {
					imgColuna = imgPago;
				}

				tabConsulta.adicLinha();
				tabConsulta.setValor( imgColuna, i, EConsulta.STATUS.ordinal() );
				tabConsulta.setValor( rs.getInt( "CODPED" ), i, EConsulta.PEDIDO.ordinal() );
				tabConsulta.setValor( rs.getInt( "CODITPED" ), i, EConsulta.ITEM.ordinal() );
				tabConsulta.setValor( rs.getBigDecimal( "VLRCOMISS" ).setScale( AplicativoRep.casasDecFin, BigDecimal.ROUND_HALF_UP ), i, EConsulta.VALORCOMISS.ordinal() );
				tabConsulta.setValor( rs.getBigDecimal( "VLRPAGO" ).setScale( AplicativoRep.casasDecFin, BigDecimal.ROUND_HALF_UP ), i, EConsulta.VALORPAGO.ordinal() );
				tabConsulta.setValor( rs.getBigDecimal( "VLRABERTO" ).setScale( AplicativoRep.casasDecFin, BigDecimal.ROUND_HALF_UP ), i, EConsulta.VALORABERTO.ordinal() );
				tabConsulta.setValor( rs.getDate( "DTPAGO" ) != null ? Funcoes.dateToStrDate( rs.getDate( "DTPAGO" ) ) : null, i, EConsulta.DATA.ordinal() );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "erro ao carregar tabela!", true, con, e );
		}
	}

	private void alteraComissao() {

		if ( tabConsulta.getLinhaSel() >= 0 ) {
			Integer codped = (Integer) tabConsulta.getValor( tabConsulta.getLinhaSel(), EConsulta.PEDIDO.ordinal() );
			Integer item = (Integer) tabConsulta.getValor( tabConsulta.getLinhaSel(), EConsulta.ITEM.ordinal() );

			DLAlterComiss dl = new DLAlterComiss();
			dl.setVisible( true );

			if ( dl.OK ) {

				try {

					StringBuilder sql = new StringBuilder();

					sql.append( "UPDATE RPCOMISSAO SET " );
					sql.append( "  VLRPAGO=?, DTPAGO=? " );
					sql.append( "WHERE " );
					sql.append( "  CODEMP=? AND CODFILIAL=? AND CODPED=? AND CODITPED=? AND " );
					sql.append( "  CODEMPVD=CODEMP AND CODFILIALVD=CODFILIAL AND CODVEND=? " );

					PreparedStatement ps = con.prepareStatement( sql.toString() );
					int param = 1;
					ps.setBigDecimal( param++, dl.getValor() );
					if ( dl.getData() != null ) {
						ps.setDate( param++, Funcoes.dateToSQLDate( dl.getData() ) );
					}
					else {
						ps.setNull( param++, Types.DATE );
					}
					ps.setInt( param++, AplicativoRep.iCodEmp );
					ps.setInt( param++, ListaCampos.getMasterFilial( "RPCOMISSAO" ) );
					ps.setInt( param++, codped );
					ps.setInt( param++, item );
					ps.setInt( param++, txtCodVend.getVlrInteger() );
					ps.executeUpdate();
					ps.close();

					con.commit();

					carregaTabela();
				} catch ( Exception e ) {
					e.printStackTrace();
					Funcoes.mensagemErro( this, "erro ao alterar comissão!", true, con, e );
				}
			}

			dl.dispose();
		}
	}

	public void imprimir( TYPE_PRINT visualizar ) {

		try {

			String status = "";

			if ( "S".equals( cbNaoPagas.getVlrString() ) ) {
				status += "'PE'";
			}
			if ( "S".equals( cbPagasParcial.getVlrString() ) ) {
				status += status.length() > 0 ? ",'PP'" : "'PP'";
			}
			if ( "S".equals( cbPagas.getVlrString() ) ) {
				status += status.length() > 0 ? ",'CF'" : "'CF'";
			}

			StringBuilder sql = new StringBuilder();

			sql.append( "SELECT " );
			sql.append( "  C.CODPED, C.CODITPED, C.STATUSCOMISS," );
			sql.append( "  C.VLRCOMISS, COALESCE(C.VLRPAGO,0) VLRPAGO, C.VLRCOMISS-COALESCE(C.VLRPAGO,0) VLRABERTO, C.DTPAGO " );
			sql.append( "FROM RPCOMISSAO C, RPFATURAMENTO F " );
			sql.append( "WHERE " );
			sql.append( "  C.CODEMP=? AND C.CODFILIAL=? AND C.CODEMPVD=C.CODEMP AND C.CODFILIALVD=C.CODFILIAL AND C.CODVEND=? AND " );
			sql.append( "  F.CODEMP=C.CODEMP AND F.CODFILIAL=C.CODFILIAL AND F.CODPED=C.CODPED AND F.CODITPED=C.CODITPED AND " );
			sql.append( "  STATUSCOMISS IN (" + status + ") AND (" );
			sql.append( "  C.DTPAGO BETWEEN ? AND ? " );
			if ( "S".equals( cbNaoPagas.getVlrString() ) ) {
				sql.append( "  OR F.DTFATURADO BETWEEN ? AND ? " );
			}
			sql.append( "  ) " );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			int param = 1;
			ps.setInt( param++, AplicativoRep.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "RPCOMISSAO" ) );
			ps.setInt( param++, txtCodVend.getVlrInteger() );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDtIni.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDtFim.getVlrDate() ) );
			if ( "S".equals( cbNaoPagas.getVlrString() ) ) {
				ps.setDate( param++, Funcoes.dateToSQLDate( txtDtIni.getVlrDate() ) );
				ps.setDate( param++, Funcoes.dateToSQLDate( txtDtFim.getVlrDate() ) );
			}

			ResultSet rs = ps.executeQuery();

			HashMap<String, Object> hParam = new HashMap<String, Object>();

			hParam.put( "CODEMP", AplicativoRep.iCodEmp );
			hParam.put( "NOMEVEND", txtNomeVend.getVlrString() );
			hParam.put( "DTINI", txtDtIni.getVlrDate() );
			hParam.put( "DTFIM", txtDtFim.getVlrDate() );
			hParam.put( "REPORT_CONNECTION", con.getConnection() );

			FPrinterJob dlGr = new FPrinterJob( "modulos/rep/relatorios/rpcomissoes.jasper", "COMISSÕES", null, rs, hParam, this );

			if ( visualizar == TYPE_PRINT.VIEW ) {
				dlGr.setVisible( true );
			}
			else {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			}
		} catch ( Exception e ) {
			Funcoes.mensagemErro( this, "Erro ao montar relatorio!\n" + e.getMessage() );
			e.printStackTrace();
		}

	}

	public void actionPerformed( ActionEvent e ) {

		if ( e.getSource() == btPesquisar ) {
			carregaTabela();
		}
		else if ( e.getSource() == btImp ) {
			imprimir( TYPE_PRINT.PRINT);
		}
		else if ( e.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
	}

	public void mouseClicked( MouseEvent e ) {

		if ( e.getClickCount() == 2 && e.getSource() == tabConsulta ) {

			alteraComissao();
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

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcVendedor.setConexao( cn );
	}

	private enum EConsulta {
		STATUS, PEDIDO, ITEM, VALORCOMISS, VALORPAGO, VALORABERTO, DATA, SEL
	}

	private class DLAlterComiss extends FDialogo {

		private static final long serialVersionUID = 1L;

		private final JTextFieldPad txtValor = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, AplicativoRep.casasDecFin );

		private final JTextFieldPad txtData = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

		public DLAlterComiss() {

			super();
			setAtribos( 230, 180 );

			Date data = null;
			data = Funcoes.strDateToDate( (String) tabConsulta.getValor( tabConsulta.getLinhaSel(), EConsulta.DATA.ordinal() ) );
			txtValor.setVlrBigDecimal( (BigDecimal) tabConsulta.getValor( tabConsulta.getLinhaSel(), EConsulta.VALORPAGO.ordinal() ) );
			txtData.setVlrDate( data != null ? data : Calendar.getInstance().getTime() );

			adic( new JLabel( "Valor da comissão" ), 10, 10, 190, 20 );
			adic( txtValor, 10, 30, 200, 20 );
			adic( new JLabel( "Data da comissão" ), 10, 50, 190, 20 );
			adic( txtData, 10, 70, 200, 20 );
		}

		private BigDecimal getValor() {

			return txtValor.getVlrBigDecimal();
		}

		private Date getData() {

			return txtData.getVlrString().trim().length() > 0 ? txtData.getVlrDate() : null;
		}
	}
}
