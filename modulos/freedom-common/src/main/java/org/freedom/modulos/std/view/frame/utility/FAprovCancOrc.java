/**
 * @version 12/12/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FAprovaOrc.java <BR>
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
 *         Aprovação de orçamento.
 * 
 */

package org.freedom.modulos.std.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.TabelaEditEvent;
import org.freedom.acao.TabelaEditListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.functions.ConversionFunctions;
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
import org.freedom.library.swing.dialog.DLJustCanc;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.library.type.StringDireita;

public class FAprovCancOrc extends FFilho implements ActionListener, TabelaEditListener, CarregaListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCab = new JPanelPad( 0, 80 );

	private JPanelPad pnCli = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTextFieldPad txtCodOrc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtCodCli = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtDtOrc = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldFK txtDtVal = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtVlrAceito = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 10, 2 );

	private JTextFieldPad txtVlrAprovado = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 10, 2 );

	private JTextFieldPad txtTotal = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 10, 2 );

	private JTextFieldPad txtTipoOrc = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldFK txtStatusOrc = new JTextFieldFK( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtVlrLiqOrc = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTablePad tab = new JTablePad();

	private JButtonPad btRecarregar = new JButtonPad( Icone.novo( "btExecuta.png" ) );

	private JButtonPad btConfirmar = new JButtonPad( Icone.novo( "btOk.png" ) );

	private JButtonPad btCancelar = new JButtonPad( Icone.novo( "btCancelar.png" ) );

	private JButtonPad btSair = new JButtonPad( "Sair", Icone.novo( "btSair.png" ) );

	private ImageIcon imgEditaCampo = Icone.novo( "clEditar.gif" );

	private JScrollPane spnTab = new JScrollPane( tab );

	private ListaCampos lcOrc = new ListaCampos( this );

	private ListaCampos lcCli = new ListaCampos( this, "CL" );

	private JCheckBoxPad cbTodos = new JCheckBoxPad( "Todos os ítens.", "S", "N" );

	private JCheckBoxPad cbEmit = new JCheckBoxPad( "Buscar ítens emitidos.", "S", "N" );

	private BigDecimal bVlrAceito = new BigDecimal( "0" );

	private BigDecimal bVlrAprovado = new BigDecimal( "0" );

	private BigDecimal bVlrTotal = new BigDecimal( "0" );

	private JLabelPad lbStatus = new JLabelPad();

	boolean bRecalcula = true;

	public FAprovCancOrc() {

		super( false );

		setTitulo( "Aprovação ou cancelamento de orçamentos" );
		//setAtribos( 15, 30, 796, 380 );
		setAtribos( 15, 30, 950, 380 );

		btRecarregar.setToolTipText( "Recarregar ítens" );
		btConfirmar.setToolTipText( "Confirmar aprovação ou cancelamento dos ítens selecionados?" );

		btRecarregar.addActionListener( this );
		btConfirmar.addActionListener( this );
		btCancelar.addActionListener( this );
		btSair.addActionListener( this );

		lcOrc.addCarregaListener( this );

		lbStatus.setFont( new Font( "Arial", Font.BOLD, 13 ) );
		lbStatus.setForeground( Color.WHITE );
		lbStatus.setBackground( Color.GRAY );
		lbStatus.setOpaque( true );
		lbStatus.setHorizontalAlignment( SwingConstants.CENTER );
		lbStatus.setVisible( false );

		JPanelPad pinRod = new JPanelPad( 685, 50 );

		lcOrc.add( new GuardaCampo( txtCodOrc, "CodOrc", "N. orçamento", ListaCampos.DB_PK, null, false ) );
		lcOrc.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_FK, null, false ) );
		lcOrc.add( new GuardaCampo( txtDtOrc, "DtOrc", "Data", ListaCampos.DB_SI, null, false ) );
		lcOrc.add( new GuardaCampo( txtDtVal, "DtVencOrc", "Validade", ListaCampos.DB_SI, null, false ) );
		lcOrc.add( new GuardaCampo( txtTipoOrc, "TipoOrc", "Tp.Orc.", ListaCampos.DB_SI, false ) );
		lcOrc.add( new GuardaCampo( txtVlrLiqOrc, "VlrLiqOrc", "V.Liq.", ListaCampos.DB_SI, null, false ) );
		lcOrc.add( new GuardaCampo( txtStatusOrc, "StatusOrc", "Status", ListaCampos.DB_SI, null, false ) );

		lcOrc.montaSql( false, "ORCAMENTO", "VD" );
		lcOrc.setQueryCommit( false );
		lcOrc.setReadOnly( true );

		txtCodOrc.setNomeCampo( "CodOrc" );
		txtCodOrc.setPK( true );
		txtCodOrc.setListaCampos( lcOrc );

		txtVlrAceito.setAtivo( false );
		txtVlrAprovado.setAtivo( false );
		txtTotal.setAtivo( false );

		// FK Cliente
		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Código", ListaCampos.DB_PK, null, false ) );
		lcCli.add( new GuardaCampo( txtNomeCli, "NomeCli", "Nome", ListaCampos.DB_SI, null, false ) );
		lcCli.montaSql( false, "CLIENTE", "VD" );
		lcCli.setQueryCommit( false );
		lcCli.setReadOnly( true );
		txtCodCli.setTabelaExterna( lcCli, null );

		pinCab.adic( new JLabelPad( "N. orçamento" ), 7, 0, 120, 20 );
		pinCab.adic( txtCodOrc, 7, 20, 85, 20 );
		pinCab.adic( new JLabelPad( "Cód.cli." ), 95, 0, 250, 20 );
		pinCab.adic( txtCodCli, 95, 20, 50, 20 );
		pinCab.adic( new JLabelPad( "Razão do cliente" ), 148, 0, 250, 20 );
		pinCab.adic( txtNomeCli, 148, 20, 203, 20 );
		pinCab.adic( new JLabelPad( "Data" ), 354, 0, 75, 20 );
		pinCab.adic( txtDtOrc, 354, 20, 83, 20 );
		pinCab.adic( new JLabelPad( "Validade" ), 440, 0, 75, 20 );
		pinCab.adic( txtDtVal, 440, 20, 83, 20 );

		pinCab.adic( lbStatus, 660, 20, 110, 20 );

		cbTodos.setVlrString( "N" );
		pinCab.adic( cbTodos, 7, 45, 200, 20 );
		pinCab.adic( cbEmit, 210, 45, 200, 20 );

		pinRod.adic( btRecarregar, 10, 10, 57, 30 );
		pinRod.adic( btConfirmar, 70, 10, 57, 30 );
		pinRod.adic( btCancelar, 130, 10, 57, 30 );
		pinRod.adic( btSair, 660, 10, 100, 30 );

		pinRod.adic( txtVlrAceito, 230, 20, 97, 20 );
		pinRod.adic( txtVlrAprovado, 330, 20, 97, 20 );
		pinRod.adic( txtTotal, 430, 20, 100, 20 );

		pinRod.adic( new JLabelPad( "Vlr. aceito" ), 230, 0, 97, 20 );
		pinRod.adic( new JLabelPad( "Vlr. aprov." ), 330, 0, 97, 20 );
		pinRod.adic( new JLabelPad( "Vlr. total" ), 430, 0, 100, 20 );

		getTela().add( pnCli, BorderLayout.CENTER );
		pnCli.add( pinCab, BorderLayout.NORTH );
		pnCli.add( pinRod, BorderLayout.SOUTH );
		pnCli.add( spnTab, BorderLayout.CENTER );

		tab.adicColuna( "Aceite" );
		tab.adicColuna( "Aprov." );
		tab.adicColuna( "Canc." );

		tab.adicColuna( "Ítem" );
		tab.adicColuna( "Cód.prod." );
		tab.adicColuna( "Descrição do produto" );
		tab.adicColuna( "Qtd." );
		tab.adicColuna( "V.Unit." );
		tab.adicColuna( "V.Tot." );
		tab.adicColuna( "" );
		tab.adicColuna( "Autoriz." );
		tab.adicColuna( "Validade" );
		tab.adicColuna( "Observação" );

		tab.setTamColuna( 35, 0 );
		tab.setTamColuna( 35, 1 );
		tab.setTamColuna( 35, 2 );
		tab.setTamColuna( 35, 3 );
		tab.setTamColuna( 50, 4 );
		tab.setTamColuna( 220, 5 );
		tab.setTamColuna( 60, 6 );
		tab.setTamColuna( 70, 7 );
		tab.setTamColuna( 60, 8 );
		tab.setTamColuna( 20, 9 );
		tab.setTamColuna( 65, 10 );
		tab.setTamColuna( 75, 11 );
		tab.setTamColuna( 150, 12 );

		tab.setColunaEditavel( 0, true );
		tab.setColunaEditavel( 1, true );
		tab.setColunaEditavel( 2, true );
		tab.setColunaEditavel( 9, true );
		tab.setColunaEditavel( 10, true );

		tab.setDefaultEditor( String.class, new DefaultCellEditor( new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 ) ) );

		btCancelar.setToolTipText( "Cancela Orçamento" );

		cbTodos.addMouseListener( new MouseAdapter() {

			public void mouseClicked( MouseEvent mevt ) {

				if ( mevt.getSource() == cbTodos && mevt.getClickCount() == 1 )
					aprovarTudo();
			}
		} );

	}

	private void carregaStatus() {

		if ( "*".equals( txtStatusOrc.getVlrString() ) || "OA".equals( txtStatusOrc.getVlrString() ) ) {
			lbStatus.setText( "ABERTO/PENDENTE" );
			lbStatus.setBackground( Color.ORANGE );
			lbStatus.setVisible( true );
		}
		else if ( "OC".equals( txtStatusOrc.getVlrString() ) ) {
			lbStatus.setText( "COMPLETO/IMPRESSO" );
			lbStatus.setBackground( Color.BLUE );
			lbStatus.setVisible( true );
		}
		else if ( "OL".equals( txtStatusOrc.getVlrString() ) ) {
			lbStatus.setText( "LIBERADO/APROVADO" );
			lbStatus.setBackground( new Color( 45, 190, 60 ) );
			lbStatus.setVisible( true );
		}
		else if ( "OV".equals( txtStatusOrc.getVlrString() ) ) {
			lbStatus.setText( "FATURADO" );
			lbStatus.setBackground( Color.RED );
			lbStatus.setVisible( true );
		}
		else if ( "FP".equals( txtStatusOrc.getVlrString() ) ) {
			lbStatus.setText( "FAT. PARCIAL" );
			lbStatus.setBackground( Color.RED );
			lbStatus.setVisible( true );
		}
		else if ( "OP".equals( txtStatusOrc.getVlrString() ) ) {
			lbStatus.setText( "PRODUZIDO" );
			lbStatus.setBackground( Color.MAGENTA );
			lbStatus.setVisible( true );
		}
		else if ( "CA".equals( txtStatusOrc.getVlrString() ) ) {
			lbStatus.setText( "CANCELADO" );
			lbStatus.setBackground( Color.RED );
			lbStatus.setVisible( true );
		}

		else {
			lbStatus.setForeground( Color.WHITE );
			lbStatus.setBackground( Color.GRAY );
			lbStatus.setText( "" );
			lbStatus.setVisible( false );
		}

	}

	private boolean cancelar() {

		boolean bRet = false;
		DLJustCanc dl = null;
		String justificativa = null;

		if ( txtCodOrc.getVlrInteger() == 0 ) {
			Funcoes.mensagemInforma( null, "Nenhum orçamento foi selecionado!" );
			txtCodOrc.requestFocus();
		}
		else if ( txtStatusOrc.getVlrString().trim().equals( "CA" ) ) {
			Funcoes.mensagemInforma( null, "Orcamento ja foi cancelado!!" );
		}

		else if ( "* OA OC OL".indexOf( txtStatusOrc.getVlrString().trim() ) != -1 ) {

			if ( Funcoes.mensagemConfirma( null, "Deseja realmente cancelar este orçamento?" ) == JOptionPane.YES_OPTION ) {

				dl = new DLJustCanc();
				dl.setVisible( true );

				if ( dl.OK ) {

					try {

						justificativa = dl.getValor();

						PreparedStatement ps = null;
						String sSQL = "UPDATE VDORCAMENTO SET STATUSORC = 'CA', JUSTIFICCANCORC=? " + "WHERE CODEMP=? AND CODFILIAL=? AND CODORC=? AND TIPOORC=? ";

						ps = con.prepareStatement( sSQL );

						ps.setString( 1, justificativa );
						ps.setInt( 2, lcOrc.getCodEmp() );
						ps.setInt( 3, lcOrc.getCodFilial() );
						ps.setInt( 4, txtCodOrc.getVlrInteger() );
						ps.setString( 5, txtTipoOrc.getVlrString() );

						ps.executeUpdate();

						ps.close();

						con.commit();

						bRet = true;

						lcOrc.carregaDados();

					} catch ( SQLException err ) {
						Funcoes.mensagemErro( null, "Erro ao cancelar o orçamento!\n" + err.getMessage(), true, con, err );
					}
				}

			}

		}
		else {
			Funcoes.mensagemInforma( this, "Orcamento não pode ser cancelado, status atual:" + txtStatusOrc.getVlrString() );
		}

		return bRet;

	}

	public void confirmar() {

		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		String sSQL = null;
		String novostatusorc = null;
		boolean bAtStOrc = false;
		boolean cancorc = false;
		DLJustCanc dl = null;
		try {

			if ( tab.getRowCount() <= 0 ) {
				Funcoes.mensagemInforma( this, "Não ha nenhum ítem para ser confirmado" );
				return;
			}

			sSQL = "UPDATE VDITORCAMENTO SET ACEITEITORC=?, APROVITORC=?, NUMAUTORIZORC=?, VENCAUTORIZORC=?, CANCITORC=? " + "WHERE CODEMP=? AND CODFILIAL=? AND CODITORC=? AND CODORC=?";

			bAtStOrc = false;

			novostatusorc = "OL";

			try {
				ps = con.prepareStatement( sSQL );

				for ( int iLin = 0; iLin < tab.getRowCount(); iLin++ ) {

					if ( tab.getValor( iLin, 0 ).equals( new Boolean( "true" ) ) ) {
						ps.setString( 1, "S" );
					}
					else {
						ps.setString( 1, "N" );
					}

					if ( tab.getValor( iLin, 1 ).equals( new Boolean( "true" ) ) ) {
						ps.setString( 2, "S" );
					}
					else {
						ps.setString( 2, "N" );
					}

					if ( tab.getValor( iLin, 2 ).equals( new Boolean( "true" ) ) ) {
						ps.setString( 5, "S" );
						novostatusorc = "CA";
						cancorc = true;
					}
					else {
						ps.setString( 5, "N" );
						cancorc = false;
						novostatusorc = "OL";
					}

					if ( tab.getValor( iLin, 0 ).equals( new Boolean( "true" ) ) && tab.getValor( iLin, 1 ).equals( new Boolean( "true" ) ) ) {
						bAtStOrc = true;
						novostatusorc = "OL";
					}
					else if ( !novostatusorc.equals( "OL" ) ) {
						bAtStOrc = true;
					}

					ps.setString( 3, tab.getValor( iLin, 10 ).toString().trim() );
					ps.setDate( 4, Funcoes.dateToSQLDate( (Date) tab.getValor( iLin, 11 ) ) );
					ps.setInt( 6, Aplicativo.iCodEmp );
					ps.setInt( 7, Aplicativo.iCodFilial );
					ps.setInt( 8, Integer.parseInt( tab.getValor( iLin, 3 ).toString() ) );
					ps.setInt( 9, txtCodOrc.getVlrInteger().intValue() );

					ps.execute();
				}

			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao atualizar a tabela ITORCAMENTO!\n" + err.getMessage(), true, con, err );
			}

			try {
				sSQL = "UPDATE VDORCAMENTO SET STATUSORC=?, JUSTIFICCANCORC=? WHERE " + "CODEMP=? AND CODFILIAL=? AND CODORC=?";

				ps2 = con.prepareStatement( sSQL );

				String justificativa = "";

				if ( cancorc ) {

					novostatusorc = "CA";

					dl = new DLJustCanc();
					dl.setVisible( true );

					if ( dl.OK ) {
						justificativa = dl.getValor();
					}
					else {
						con.rollback();
						return;
					}

				}

				ps2.setString( 1, novostatusorc );
				ps2.setString( 2, justificativa );
				ps2.setInt( 3, Aplicativo.iCodEmp );
				ps2.setInt( 4, Aplicativo.iCodFilial );
				ps2.setInt( 5, txtCodOrc.getVlrInteger() );

				if ( bAtStOrc ) {
					ps2.execute();
					con.commit();
					if ( "CA".equals( novostatusorc ) ) {
						Funcoes.mensagemInforma( this, "Orçamento Cancelado com Sucesso" );
					}
					else {
						Funcoes.mensagemInforma( this, "Orçamento Aprovado com Sucesso" );
					}
				}

			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao atualizar a tabela ORCAMENTO!\n" + err.getMessage(), true, con, err );
			}

			tab.addTabelaEditListener( this );
			bRecalcula = true;

			lcOrc.carregaDados();

		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			ps = null;
			ps2 = null;
			sSQL = null;
			novostatusorc = null;
			bAtStOrc = false;
		}
	}

	public void montaTab() {

		bRecalcula = false;
		/*String sSQL = "SELECT IT.CODPROD,P.DESCPROD,IT.QTDITORC,((IT.QTDITORC*IT.PRECOITORC)-O.VLRDESCITORC) VLRACEITE," + "IT.VLRLIQITORC VLRAPROVADO,IT.NUMAUTORIZORC,IT.ACEITEITORC,IT.APROVITORC,IT.CODITORC,IT.VENCAUTORIZORC, IT.CANCITORC "
				+ "FROM VDITORCAMENTO IT, EQPRODUTO P, VDORCAMENTO O  WHERE  " + "P.CODPROD=IT.CODPROD AND P.CODEMP=IT.CODEMPPD AND P.CODFILIAL=IT.CODFILIALPD " + "AND IT.CODEMP=? AND IT.CODFILIAL=? AND IT.CODORC=? AND O.CODORC=IT.CODORC AND "
				+ "O.CODEMP=IT.CODEMP AND O.CODFILIAL=IT.CODFILIAL AND IT.EMITITORC=? AND NOT O.STATUSORC = '*'";*/
		
		String sSQL = "SELECT IT.CODPROD,P.DESCPROD,IT.QTDITORC,((IT.QTDITORC*IT.PRECOITORC)-IT.VLRDESCITORC) VLRACEITE," 			
			+ "IT.VLRLIQITORC VLRAPROVADO,IT.NUMAUTORIZORC,IT.ACEITEITORC,IT.APROVITORC,IT.CODITORC,IT.VENCAUTORIZORC, IT.CANCITORC, IT.OBSITORC "
			+ "FROM VDITORCAMENTO IT, EQPRODUTO P, VDORCAMENTO O  WHERE  " 
			+ "P.CODPROD=IT.CODPROD AND P.CODEMP=IT.CODEMPPD AND P.CODFILIAL=IT.CODFILIALPD " 
			+ "AND IT.CODEMP=? AND IT.CODFILIAL=? AND IT.CODORC=? AND O.CODORC=IT.CODORC AND "
			+ "O.CODEMP=IT.CODEMP AND O.CODFILIAL=IT.CODFILIAL AND IT.EMITITORC=? AND NOT O.STATUSORC = '*'";
		
		bVlrAceito = new BigDecimal( "0.0" );
		bVlrAprovado = new BigDecimal( "0.0" );
		bVlrTotal = new BigDecimal( "0.0" );

		StringDireita strdQtd = null;
		StringDireita strdVlrAceite = null;
		StringDireita strdVlrAprovado = null;

		tab.limpa();

		try {
			
			PreparedStatement ps = con.prepareStatement( sSQL );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			ps.setInt( 3, txtCodOrc.getVlrInteger().intValue() );
			ps.setString( 4, cbEmit.getVlrString() );

			ResultSet rs = ps.executeQuery();
			tab.limpa();

			while ( rs.next() ) {
				Vector<Object> vVals = new Vector<Object>();

				if ( rs.getString( "ACEITEITORC" ).trim().equals( "S" ) ) {
					vVals.addElement( new Boolean( true ) );
					bVlrAceito = bVlrAceito.add( rs.getBigDecimal( "VLRACEITE" ) );
				}
				else {
					vVals.addElement( new Boolean( false ) );
				}

				if ( rs.getString( "APROVITORC" ).trim().equals( "S" ) ) {
					vVals.addElement( new Boolean( true ) );
					bVlrAprovado = bVlrAprovado.add( rs.getBigDecimal( "VLRACEITE" ) );
				}
				else {
					vVals.addElement( new Boolean( false ) );
				}

				if ( "S".equals( rs.getString( "CANCITORC" ).trim() ) ) {
					vVals.addElement( new Boolean( true ) );
				}
				else {
					vVals.addElement( new Boolean( false ) );
				}

				vVals.addElement( rs.getString( "CODITORC" ) );
				vVals.addElement( rs.getString( "CODPROD" ) );
				vVals.addElement( rs.getString( "DESCPROD" ) );

				strdQtd = new StringDireita( Funcoes.strDecimalToStrCurrency( 2, rs.getString( "QTDITORC" ) ) );
				strdVlrAceite = new StringDireita( Funcoes.strDecimalToStrCurrency( 2, rs.getString( "VLRACEITE" ) ) );
				strdVlrAprovado = new StringDireita( Funcoes.strDecimalToStrCurrency( 2, rs.getString( "VLRAPROVADO" ) ) );

				vVals.addElement( strdQtd );
				vVals.addElement( strdVlrAceite );
				vVals.addElement( strdVlrAprovado );

				vVals.addElement( imgEditaCampo );
				vVals.addElement( rs.getString( 6 ) != null ? ( rs.getString( 6 ).trim() ) : "" );

				if ( rs.getDate( "VENCAUTORIZORC" ) == null ) {
					GregorianCalendar cPadrao = new GregorianCalendar();
					cPadrao.set( Calendar.DATE, cPadrao.get( Calendar.DATE ) + 60 );
					vVals.addElement( cPadrao.getTime() );
				}
				else
					vVals.addElement( Funcoes.sqlDateToDate( rs.getDate( "VENCAUTORIZORC" ) ) );
				
				vVals.addElement( rs.getString( "OBSITORC" ) != null ? ( rs.getString( "OBSITORC" ).trim() ) : "" );
				
				bVlrTotal = bVlrTotal.add( new BigDecimal( rs.getString( "VLRACEITE" ) ) );

				txtVlrAceito.setVlrBigDecimal( bVlrAceito );
				txtVlrAprovado.setVlrBigDecimal( bVlrAprovado );
				txtTotal.setVlrBigDecimal( bVlrTotal );

				tab.adicLinha( vVals );

			}
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela ITORCAMENTO!\n" + err.getMessage(), true, con, err );
		}
		tab.addTabelaEditListener( this );
		bRecalcula = true;
	}

	public void aprovarTudo() {

		int iLin = 0;
		if ( cbTodos.getVlrString() == "S" ) {
			while ( iLin < tab.getRowCount() ) {
				bRecalcula = false;
				tab.setValor( new Boolean( true ), iLin, 0 );
				tab.setValor( new Boolean( true ), iLin, 1 );
				if ( tab.getValor( iLin, 10 ).toString().trim().equals( "" ) )
					tab.setValor( "000000", iLin, 10 );
				iLin++;
			}
			bRecalcula = true;
		}
	}

	public void recalcula() {

		int iLin = 0;
		bVlrAceito = new BigDecimal( "0.0" );
		bVlrAprovado = new BigDecimal( "0.0" );
		bVlrTotal = new BigDecimal( "0.0" );

		while ( iLin < tab.getRowCount() ) {
			if ( tab.getValor( iLin, 0 ).equals( new Boolean( "true" ) ) ) {
				bVlrAceito = bVlrAceito.add( ConversionFunctions.stringCurrencyToBigDecimal( tab.getValor( iLin, 7 ).toString() ) );
			}
			if ( tab.getValor( iLin, 1 ).equals( new Boolean( "true" ) ) ) {
				bVlrAprovado = bVlrAprovado.add( ConversionFunctions.stringCurrencyToBigDecimal( tab.getValor( iLin, 8 ).toString() ) );
			}
			iLin++;
		}
		txtVlrAceito.setVlrBigDecimal( bVlrAceito );
		txtVlrAprovado.setVlrBigDecimal( bVlrAprovado );
		txtTotal.setVlrBigDecimal( bVlrTotal );
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcCli.setConexao( con );
		lcOrc.setConexao( con );
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btRecarregar ) {
			montaTab();
		}
		else if ( evt.getSource() == btSair ) {
			dispose();
		}
		else if ( evt.getSource() == btConfirmar ) {
			if ( Funcoes.mensagemConfirma( this, "Confirma os status para os ítens do orçamento?" ) == 0 ) {
				confirmar();
			}
		}
		else if ( evt.getSource() == btCancelar ) {
			cancelar();
		}

	}

	public void valorAlterado( TabelaEditEvent evt ) {

		if ( bRecalcula ) {
			if ( ( tab.getColunaEditada() < 2 ) ) {
				recalcula();
			}
		}
		if ( bRecalcula ) {
			if ( tab.getValor( tab.getLinhaEditada(), 0 ).equals( new Boolean( "false" ) ) && tab.getValor( tab.getLinhaEditada(), 1 ).equals( new Boolean( "false" ) ) && ( tab.getValor( tab.getLinhaEditada(), 10 ).toString().trim() != "" ) )
				tab.setValor( "", tab.getLinhaEditada(), 10 );
		}
	}

	public void afterCarrega( CarregaEvent cevt ) {

		if ( cevt.getListaCampos() == lcOrc ) {
			carregaStatus();
			montaTab();
		}

	}

	public void beforeCarrega( CarregaEvent cevt ) {

		// TODO Auto-generated method stub

	}

}
