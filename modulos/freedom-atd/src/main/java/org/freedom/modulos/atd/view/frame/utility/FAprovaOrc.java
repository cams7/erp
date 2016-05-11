/**
 * @version 05/06/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.atd <BR>
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
 *         Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.atd.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.swing.JScrollPane;

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
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.library.type.StringDireita;

/**
 * Formulário de consulta de orçamento.
 * 
 * @version 1.1 18/08/2003
 * @author Anderson Sanchez
 * 
 */
public class FAprovaOrc extends FFilho implements ActionListener, TabelaEditListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCab = new JPanelPad( 0, 80 );

	private JPanelPad pnCli = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTextFieldPad txtCodOrc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtCodConv = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeConv = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtDtOrc = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldFK txtDtVal = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtVlrAceito = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 10, 2 );

	private JTextFieldPad txtVlrAprovado = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 10, 2 );

	private JTextFieldPad txtTotal = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 10, 2 );

	private JTablePad tab = new JTablePad();

	private JButtonPad btCalc = new JButtonPad( Icone.novo( "btExecutaRec.png" ) );

	private JButtonPad btCanc = new JButtonPad( Icone.novo( "btCancelar.png" ) );

	private JButtonPad btOk = new JButtonPad( Icone.novo( "btOk.png" ) );

	private ImageIcon imgEditaCampo = Icone.novo( "clEditar.gif" );

	private JScrollPane spnTab = new JScrollPane( tab );

	private ListaCampos lcOrc = new ListaCampos( this );

	private ListaCampos lcConv = new ListaCampos( this, "CV" );

	private JCheckBoxPad cbTodos = new JCheckBoxPad( "Aprovar todos os ítens.", "S", "N" );

	private JCheckBoxPad cbEmit = new JCheckBoxPad( "Buscar ítens emitidos..", "S", "N" );

	BigDecimal bVlrAceito = new BigDecimal( "0" );

	BigDecimal bVlrAprovado = new BigDecimal( "0" );

	BigDecimal bVlrTotal = new BigDecimal( "0" );

	boolean bRecalcula = true;

	public FAprovaOrc() {

		super( false );
		setTitulo( "Aprova Orçamento" );
		setAtribos( 15, 30, 785, 350 );

		btCalc.setToolTipText( "Recarregar ítens" );
		btCanc.setToolTipText( "Cancelar" );
		btOk.setToolTipText( "Confirmar aprovação" );

		btCalc.addActionListener( this );
		btCanc.addActionListener( this );
		btOk.addActionListener( this );

		JPanelPad pinRod = new JPanelPad( 685, 50 );

		lcOrc.add( new GuardaCampo( txtCodOrc, "CodOrc", "Cód.orç.", ListaCampos.DB_PK, false ), "txtCodOrc" );
		lcOrc.add( new GuardaCampo( txtCodConv, "CodConv", "Cód.conv.", ListaCampos.DB_SI, false ), "txtCodConv" );
		lcOrc.add( new GuardaCampo( txtDtOrc, "DtOrc", "Data", ListaCampos.DB_SI, false ), "txtDtOrc" );
		lcOrc.add( new GuardaCampo( txtDtVal, "DtVencOrc", "Validade", ListaCampos.DB_SI, false ), "txtDtVal" );

		lcOrc.montaSql( false, "ORCAMENTO", "VD" );
		lcOrc.setQueryCommit( false );
		lcOrc.setReadOnly( true );

		txtCodOrc.setNomeCampo( "CodOrc" );
		txtCodOrc.setPK( true );
		txtCodOrc.setListaCampos( lcOrc );

		txtVlrAceito.setAtivo( false );
		txtVlrAprovado.setAtivo( false );
		txtTotal.setAtivo( false );

		// FK Conveniado
		lcConv.add( new GuardaCampo( txtCodConv, "CodConv", "Cód.conv.", ListaCampos.DB_PK, false ), "txtCodConv" );
		lcConv.add( new GuardaCampo( txtNomeConv, "NomeConv", "Nome do conveniado", ListaCampos.DB_SI, false ), "txtNomeConv" );
		lcConv.montaSql( false, "CONVENIADO", "AT" );
		lcConv.setQueryCommit( false );
		lcConv.setReadOnly( true );
		txtCodConv.setTabelaExterna( lcConv, null );

		pinCab.adic( new JLabelPad( "Cód.orç." ), 7, 0, 100, 20 );
		pinCab.adic( txtCodOrc, 7, 20, 70, 20 );
		pinCab.adic( new JLabelPad( "Cód.conv." ), 82, 0, 250, 20 );
		pinCab.adic( txtCodConv, 82, 20, 70, 20 );
		pinCab.adic( new JLabelPad( "Nome do conveniado" ), 157, 0, 250, 20 );
		pinCab.adic( txtNomeConv, 157, 20, 203, 20 );
		pinCab.adic( new JLabelPad( "Data" ), 365, 0, 70, 20 );
		pinCab.adic( txtDtOrc, 365, 20, 70, 20 );
		pinCab.adic( new JLabelPad( "Validade" ), 440, 0, 70, 20 );
		pinCab.adic( txtDtVal, 440, 20, 70, 20 );

		cbTodos.setVlrString( "N" );
		pinCab.adic( cbTodos, 7, 45, 160, 20 );
		pinCab.adic( cbEmit, 180, 45, 160, 20 );

		pinRod.adic( btCalc, 10, 10, 57, 30 );
		pinRod.adic( btOk, 70, 10, 57, 30 );
		pinRod.adic( btCanc, 130, 10, 57, 30 );

		pinRod.adic( txtVlrAceito, 265, 20, 97, 20 );
		pinRod.adic( txtVlrAprovado, 365, 20, 97, 20 );
		pinRod.adic( txtTotal, 465, 20, 100, 20 );

		pinRod.adic( new JLabelPad( "Vlr. aceito" ), 265, 0, 97, 20 );
		pinRod.adic( new JLabelPad( "Vlr. aprov." ), 365, 0, 100, 20 );
		pinRod.adic( new JLabelPad( "Vlr. total" ), 465, 0, 100, 20 );

		getTela().add( pnCli, BorderLayout.CENTER );
		pnCli.add( pinCab, BorderLayout.NORTH );
		pnCli.add( pinRod, BorderLayout.SOUTH );
		pnCli.add( spnTab, BorderLayout.CENTER );

		tab.adicColuna( "Aceite" );
		tab.adicColuna( "Aprov." );
		tab.adicColuna( "Ítem" );
		tab.adicColuna( "Cód.prod." );
		tab.adicColuna( "Descrição do produto" );
		tab.adicColuna( "Qtd." );
		tab.adicColuna( "Vlr.unit." );
		tab.adicColuna( "Vlr.tot." );
		tab.adicColuna( "" );
		tab.adicColuna( "Autoriz." );
		tab.adicColuna( "Validade" );

		tab.setTamColuna( 50, 0 );
		tab.setTamColuna( 50, 1 );
		tab.setTamColuna( 50, 2 );
		tab.setTamColuna( 70, 3 );
		tab.setTamColuna( 220, 4 );
		tab.setTamColuna( 60, 5 );
		tab.setTamColuna( 70, 6 );
		tab.setTamColuna( 70, 7 );
		tab.setTamColuna( 20, 8 );
		tab.setTamColuna( 70, 9 );
		tab.setTamColuna( 100, 10 );

		tab.setColunaEditavel( 0, true );
		tab.setColunaEditavel( 1, true );
		tab.setColunaEditavel( 9, true );
		tab.setColunaEditavel( 10, true );

		tab.setDefaultEditor( String.class, new DefaultCellEditor( new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 ) ) );

		cbTodos.addMouseListener( new MouseAdapter() {

			public void mouseClicked( MouseEvent mevt ) {

				if ( mevt.getSource() == cbTodos && mevt.getClickCount() == 1 )
					aprovarTudo();
			}
		} );

		txtCodOrc.addKeyListener( new KeyAdapter() {

			public void keyPressed( KeyEvent kevt ) {

				if ( kevt.getKeyCode() == KeyEvent.VK_ENTER ) {
					montaTab();
				}
			}
		} );
	}

	public void aprovar() {

		if ( !avisa( 1 ) ) {
			int iLin = 0;
			String sSQL = "UPDATE VDITORCAMENTO SET ACEITEITORC=?, APROVITORC=?, NUMAUTORIZORC=?, VENCAUTORIZORC=? WHERE " + "CODEMP=? AND CODFILIAL=? AND CODITORC=? AND CODORC=?";
			boolean bAtStOrc = false;
			String sStatusAt = "OC";
			try {
				PreparedStatement ps = con.prepareStatement( sSQL );
				while ( iLin < tab.getRowCount() ) {

					if ( tab.getValor( iLin, 0 ).equals( new Boolean( "true" ) ) ) {
						ps.setString( 1, "S" );
					}
					else {
						ps.setString( 1, "N" );
					}
					if ( tab.getValor( iLin, 1 ).equals( new Boolean( "true" ) ) )
						ps.setString( 2, "S" );
					else
						ps.setString( 2, "N" );

					if ( tab.getValor( iLin, 0 ).equals( new Boolean( "true" ) ) && tab.getValor( iLin, 1 ).equals( new Boolean( "true" ) ) ) {
						bAtStOrc = true;
						sStatusAt = "OL";
					}
					else if ( !sStatusAt.equals( "OL" ) ) {
						bAtStOrc = true;
					}

					ps.setString( 3, tab.getValor( iLin, 9 ).toString().trim() );
					ps.setDate( 4, Funcoes.dateToSQLDate( (Date) tab.getValor( iLin, 10 ) ) );
					ps.setInt( 5, Aplicativo.iCodEmp );
					ps.setInt( 6, Aplicativo.iCodFilial );
					ps.setInt( 7, Integer.parseInt( tab.getValor( iLin, 2 ).toString() ) );
					ps.setInt( 8, txtCodOrc.getVlrInteger().intValue() );

					ps.execute();
					iLin++;
				}
				con.commit();

			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao atualizar a tabela ITORCAMENTO!\n" + err.getMessage(), true, con, err );
			}

			try {
				sSQL = "UPDATE VDORCAMENTO SET STATUSORC=? WHERE " + "CODEMP=? AND CODFILIAL=? AND CODORC=?";

				PreparedStatement ps2 = con.prepareStatement( sSQL );

				ps2.setString( 1, sStatusAt );
				ps2.setInt( 2, Aplicativo.iCodEmp );
				ps2.setInt( 3, Aplicativo.iCodFilial );
				ps2.setInt( 4, txtCodOrc.getVlrInteger().intValue() );

				if ( bAtStOrc ) {
					ps2.execute();
					con.commit();
				}
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao atualizar a tabela ORCAMENTO!\n" + err.getMessage(), true, con, err );
			}

			tab.addTabelaEditListener( this );
			bRecalcula = true;
		}
	}

	public void montaTab() {

		bRecalcula = false;
		String sSQL = "SELECT O.CODPROD,P.DESCPROD,O.QTDITORC,(O.QTDITORC*O.PRECOITORC)-O.VLRDESCITORC," + "O.VLRLIQITORC,O.NUMAUTORIZORC,O.ACEITEITORC,O.APROVITORC, O.CODITORC, O.VENCAUTORIZORC " + "FROM VDITORCAMENTO O, EQPRODUTO P, SGPREFERE2 PR WHERE O.CODEMP=? AND O.CODFILIAL=? AND "
				+ "P.CODPROD=O.CODPROD AND P.CODEMP=O.CODEMPPD AND P.CODFILIAL=O.CODFILIALPD AND PR.CODEMP=O.CODEMP AND PR.CODFILIAL=O.CODFILIAL " + "AND O.CODORC=? AND EMITITORC=?";
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

				if ( rs.getString( 7 ).trim().equals( "S" ) ) {
					vVals.addElement( new Boolean( true ) );
					bVlrAceito = bVlrAceito.add( new BigDecimal( rs.getString( 4 ) ) );
				}
				else
					vVals.addElement( new Boolean( false ) );

				if ( rs.getString( 8 ).trim().equals( "S" ) ) {
					vVals.addElement( new Boolean( true ) );
					bVlrAprovado = bVlrAprovado.add( new BigDecimal( rs.getString( 4 ) ) );
				}
				else
					vVals.addElement( new Boolean( false ) );

				vVals.addElement( rs.getString( 9 ) );
				vVals.addElement( rs.getString( 1 ) );
				vVals.addElement( rs.getString( 2 ) );

				strdQtd = new StringDireita( Funcoes.strDecimalToStrCurrency( 2, rs.getString( 3 ) ) );
				strdVlrAceite = new StringDireita( Funcoes.strDecimalToStrCurrency( 2, rs.getString( 4 ) ) );
				strdVlrAprovado = new StringDireita( Funcoes.strDecimalToStrCurrency( 2, rs.getString( 5 ) ) );

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

				bVlrTotal = bVlrTotal.add( new BigDecimal( rs.getString( 4 ) ) );

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
				if ( tab.getValor( iLin, 9 ).toString().trim().equals( "" ) )
					tab.setValor( "000000", iLin, 9 );
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
				bVlrAceito = bVlrAceito.add( ConversionFunctions.stringCurrencyToBigDecimal( tab.getValor( iLin, 6 ).toString() ) );
			}
			if ( tab.getValor( iLin, 1 ).equals( new Boolean( "true" ) ) ) {
				bVlrAprovado = bVlrAprovado.add( ConversionFunctions.stringCurrencyToBigDecimal( tab.getValor( iLin, 7 ).toString() ) );
			}
			iLin++;
		}
		txtVlrAceito.setVlrBigDecimal( bVlrAceito );
		txtVlrAprovado.setVlrBigDecimal( bVlrAprovado );

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcConv.setConexao( con );
		lcOrc.setConexao( con );
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btCalc )
			montaTab();
		else if ( evt.getSource() == btCanc )
			dispose();
		else if ( evt.getSource() == btOk )
			if ( Funcoes.mensagemConfirma( this, "Confirma os status para os ítens do orçamento?" ) == 0 )
				aprovar();
	}

	public boolean avisa( int iL ) {

		int iLin = 0;
		int i = iL;
		boolean bErro = false;
		while ( iLin < tab.getRowCount() ) {
			if ( tab.getValor( iLin, 0 ).equals( new Boolean( "true" ) ) && tab.getValor( iLin, 1 ).equals( new Boolean( "true" ) ) && tab.getValor( iLin, 9 ).toString().trim().equals( "" ) ) {
				i++;
			}
			iLin++;
		}
		if ( i > 1 ) {
			Funcoes.mensagemInforma( this, "Você deve informar o número da autorização para os ítens aprovados!" );
			i = 0;
			bErro = true;
		}
		return bErro;
	}

	public void valorAlterado( TabelaEditEvent evt ) {

		if ( bRecalcula ) {
			if ( ( tab.getColunaEditada() < 2 ) ) {
				recalcula();
				avisa( 0 );
			}
		}
		if ( bRecalcula ) {
			if ( tab.getValor( tab.getLinhaEditada(), 0 ).equals( new Boolean( "false" ) ) && tab.getValor( tab.getLinhaEditada(), 1 ).equals( new Boolean( "false" ) ) && ( tab.getValor( tab.getLinhaEditada(), 9 ).toString().trim() != "" ) )
				tab.setValor( "", tab.getLinhaEditada(), 9 );
		}
	}

}
