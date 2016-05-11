/**
 * @version 12/08/2005 <BR>
 * @author Setpoint Informática Ltda./Alex Rodrigues <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)DLVisitas.java <BR>
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

package org.freedom.modulos.std.view.dialog.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class DLVisitas extends FFDialogo implements MouseListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodHist = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtAno = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCodAtend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeAtend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtHoraHist = new JTextFieldPad( JTextFieldPad.TP_TIME, 5, 0 );

	private JTextFieldPad txtDataHist = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextAreaPad txaHist = new JTextAreaPad( 1000 );

	private JPanelPad pnCab = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnRod = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinCab = new JPanelPad( 300, 70 );

	private JPanelPad pinCampos = new JPanelPad( 300, 130 );

	private JPanelPad pinNavHist = new JPanelPad( 510, 30 );

	private JTablePad tab = new JTablePad();

	private JLabel lbMes = new JLabel();

	private ListaCampos lcHistorico = new ListaCampos( this );

	private ListaCampos lcAtendente = new ListaCampos( this, "AE" );

	private Navegador navHist = new Navegador( false );

	public DLVisitas( Component cOrig, DbConnection con, Vector<?> vCli ) {

		super( cOrig );
		setTitulo( "Alteração de historico" );
		setAtribos( 520, 500 );
		setCampos( vCli );

		lcAtendente.add( new GuardaCampo( txtCodAtend, "CodAtend", "Cód.atend.", ListaCampos.DB_PK, false ) );
		lcAtendente.add( new GuardaCampo( txtNomeAtend, "NomeAtend", "Nome do atendente", ListaCampos.DB_SI, false ) );
		lcAtendente.montaSql( true, "ATENDENTE", "AT" );
		lcAtendente.setQueryCommit( true );
		lcAtendente.setReadOnly( false );
		txtCodAtend.setTabelaExterna( lcAtendente, null );
		txtCodAtend.setListaCampos( lcAtendente );

		lcHistorico.setNavegador( navHist );
		lcHistorico.add( new GuardaCampo( txtCodHist, "CodHistTK", "Cód.hist.", ListaCampos.DB_PK, true ) );
		lcHistorico.add( new GuardaCampo( txtDataHist, "DataHistTk", "Data", ListaCampos.DB_SI, true ) );
		lcHistorico.add( new GuardaCampo( txtHoraHist, "HoraHistTk", "Hora", ListaCampos.DB_SI, true ) );
		lcHistorico.add( new GuardaCampo( txtCodAtend, "CodAtend", "Atendente", ListaCampos.DB_FK, false ) );
		lcHistorico.add( new GuardaCampo( txaHist, "DescHistTK", "Observações", ListaCampos.DB_SI, true ) );
		// lcHistorico.setWhereAdic("CODEMPAE="+Aplicativo.iCodEmp+" AND CODFILIALAE="+ListaCampos.getMasterFilial("ATATENDENTE")+ " AND "+
		// "CODATEND="+txtCodAtend.getVlrInteger()+" AND "+
		// "CODEMPCL="+Aplicativo.iCodEmp+" AND CODFILIALCL="+ListaCampos.getMasterFilial("VDCLIENTE")+" AND "+
		// "CODCLI="+txtCodCli.getVlrInteger());

		lcHistorico.montaSql( true, "HISTORICO", "TK" );
		lcHistorico.setQueryCommit( true );
		lcHistorico.setReadOnly( false );
		txtCodHist.setTabelaExterna( lcHistorico, null );
		txtCodHist.setListaCampos( lcHistorico );

		navHist.setListaCampos( lcHistorico );

		System.out.println( "Atendente = " + txtCodAtend.getVlrInteger() );

		c.add( pnCab, BorderLayout.NORTH );
		pnCab.setPreferredSize( new Dimension( 300, 230 ) );
		pnCab.add( pinCab, BorderLayout.NORTH );
		setPainel( pinCab );

		adic( new JLabelPad( "Cód.cli." ), 7, 0, 80, 20 );
		adic( txtCodCli, 7, 20, 80, 20 );
		adic( new JLabelPad( "Razão social" ), 90, 0, 100, 20 );
		adic( txtRazCli, 90, 20, 240, 20 );
		adic( new JLabelPad( "Ano" ), 333, 0, 60, 20 );
		adic( txtAno, 333, 20, 60, 20 );
		adic( lbMes, 15, 45, 150, 20 );
		lbMes.setFont( new Font( "Arial", Font.BOLD, 14 ) );
		lbMes.setForeground( Color.blue );
		txtCodCli.setAtivo( false );
		txtAno.setAtivo( false );

		JScrollPane spnTabRec = new JScrollPane( tab );
		pnCab.add( spnTabRec, BorderLayout.CENTER );

		tab.adicColuna( "Cód.visita" );
		tab.adicColuna( "Contato" );
		tab.adicColuna( "Atendente" );
		tab.adicColuna( "Data" );
		tab.adicColuna( "Hora" );
		tab.adicColuna( "Observações" );
		tab.adicColuna( "Sit." );

		tab.setTamColuna( 70, 0 );
		tab.setTamColuna( 60, 1 );
		tab.setTamColuna( 70, 2 );
		tab.setTamColuna( 70, 3 );
		tab.setTamColuna( 70, 4 );
		tab.setTamColuna( 210, 5 );
		tab.setTamColuna( 50, 6 );

		tab.addMouseListener( this );

		c.add( pnRod, BorderLayout.CENTER );
		pnRod.add( pinCampos, BorderLayout.CENTER );
		setPainel( pinCampos );

		adic( new JLabelPad( "Cód.hist." ), 7, 0, 70, 20 );
		adic( txtCodHist, 7, 20, 70, 20 );
		adic( new JLabelPad( "Data" ), 80, 0, 80, 20 );
		adic( txtDataHist, 80, 20, 80, 20 );
		adic( new JLabelPad( "Hora" ), 163, 0, 70, 20 );
		adic( txtHoraHist, 163, 20, 70, 20 );
		adic( new JLabelPad( "Historico" ), 7, 40, 70, 20 );
		adic( new JScrollPane( txaHist ), 7, 60, 490, 80 );

		pnRod.add( pinNavHist, BorderLayout.SOUTH );
		pinNavHist.adic( navHist, 0, 0, 160, 25 );

	}

	public void setCampos( Vector<?> args ) {

		txtCodCli.setVlrInteger( (Integer) args.elementAt( 0 ) );
		txtRazCli.setVlrString( (String) args.elementAt( 1 ) );
		txtAno.setVlrInteger( ( (Integer) args.elementAt( 2 ) ) );
		lbMes.setText( getMes( ( (Integer) args.elementAt( 3 ) ).intValue() ) );
		txtCodAtend.setVlrInteger( ( (Integer) args.elementAt( 4 ) ) );
	}

	public void mouseEntered( MouseEvent e ) {

	}

	public void mouseExited( MouseEvent e ) {

	}

	public void mousePressed( MouseEvent e ) {

	}

	public void mouseReleased( MouseEvent e ) {

	}

	public void mouseClicked( MouseEvent mevt ) {

		if ( mevt.getClickCount() == 2 ) {
			if ( mevt.getSource() == tab && tab.getLinhaSel() >= 0 ) {
				carregaCampos();
			}
		}
	}

	public void carregaCampos() {

		int iLinha = tab.getLinhaSel();
		txtCodHist.setVlrInteger( (Integer) tab.getValor( iLinha, 0 ) );
		txtCodAtend.setVlrInteger( (Integer) tab.getValor( iLinha, 2 ) );
		txtDataHist.setVlrDate( (Date) tab.getValor( iLinha, 3 ) );
		txtHoraHist.setVlrString( (String) tab.getValor( iLinha, 4 ) );
		txaHist.setVlrString( (String) tab.getValor( iLinha, 5 ) );
	}

	public void carregaTabela( int month, int year ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		Vector<Object> vLinha = null;
		int ano = year;
		int mes = month;
		try {
			sql = "SELECT TK.CODHISTTK, TK.CODCTO, TK.CODATEND, TK.DATAHISTTK, " + "TK.HORAHISTTK, TK.DESCHISTTK, TK.SITHISTTK " + "FROM TKHISTORICO TK WHERE CODEMP=? AND CODFILIAL=? " + "AND EXTRACT(MONTH FROM TK.DATAHISTTK)=?" + "AND EXTRACT(YEAR FROM TK.DATAHISTTK)=?";
			ps = con.prepareStatement( sql );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "TKHISTORICO" ) );
			ps.setInt( 3, mes );
			ps.setInt( 4, ano );

			rs = ps.executeQuery();
			while ( rs.next() ) {

				vLinha = new Vector<Object>();
				vLinha.addElement( new Integer( rs.getInt( "CODHISTTK" ) ) );
				vLinha.addElement( new Integer( rs.getInt( "CODCTO" ) ) );
				vLinha.addElement( new Integer( rs.getInt( "CODATEND" ) ) );
				vLinha.addElement( rs.getDate( "DATAHISTTK" ) );
				vLinha.addElement( rs.getString( "HORAHISTTK" ) );
				vLinha.addElement( rs.getString( "DESCHISTTK" ) );
				vLinha.addElement( rs.getString( "SITHISTTK" ) );

				tab.adicLinha( vLinha );

			}
			rs.close();
			ps.close();
			con.commit();
		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro ao carregar historico!\n" + e.getMessage() );
		} finally {
			rs = null;
			ps = null;
			sql = null;
			vLinha = null;
			ano = 0;
			mes = 0;
		}
	}

	public String getMes( int mes ) {

		String[] meses = new String[] { "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro" };
		String retorno = meses[ mes - 1 ];
		return retorno;
	}

	public void actionPerformed( ActionEvent evt ) {

		super.actionPerformed( evt );
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcHistorico.setConexao( cn );
		lcAtendente.setConexao( cn );
	}
}
