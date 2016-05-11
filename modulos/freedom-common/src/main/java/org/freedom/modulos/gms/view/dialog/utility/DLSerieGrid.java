/*
 * Projeto: Freedom Pacote: org.freedom.modules.std Classe: @(#)DLBuscaVenda.java
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR> modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR> na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR> sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR> Veja a Licença Pública Geral GNU para maiores detalhes. <BR> Você deve ter recebido uma cópia da Licença Pública
 * Geral GNU junto com este programa, se não, <BR> escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA <BR> <BR>
 */

package org.freedom.modulos.gms.view.dialog.utility;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import org.freedom.acao.TabelaSelEvent;
import org.freedom.acao.TabelaSelListener;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.dialog.FFDialogo;

/**
 * Tela para registro dos números de série na compra.
 * 
 * @author Setpoint Informática Ltda./Anderson Sanchez
 * @version 23/03/2010
 */
public class DLSerieGrid extends FFDialogo implements MouseListener, TabelaSelListener {

	public static int TIPO_COMPRA = 0;

	public static int TIPO_VENDA = 1;

	public static int TIPO_RECMERC = 3;

	private static final long serialVersionUID = 1L;

	private JPanelPad panelGeral = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad panelMaster = new JPanelPad( 700, 60 );

	private JPanelPad panelGrid = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTablePad tabItens = new JTablePad();

	private Integer codemp;

	private Integer codfilial;

	private Integer codit;

	private Integer cod;

	private Integer codemppd;

	private Integer codfilialpd;

	private Integer codprod;

	private String descprod;

	private int tipo;

	private enum ITENS {
		SEQITSERIE, NUMSERIE, STATUS, DTFABRICSERIE, DTVALIDSERIE;
	}

	public DLSerieGrid() {

		super();

		setAtribos( 520, 320 );
		setResizable( true );
		montaListeneres();

		montaTela();

	}

	private void montaTela() {

		setPanel( panelGeral );

		// panelGeral.add( panelMaster, BorderLayout.SOUTH );

		// ***** Grid

		panelGeral.add( panelGrid, BorderLayout.CENTER );
		panelGrid.setBorder( BorderFactory.createEtchedBorder() );

		tabItens.setRowHeight( 21 );

		tabItens.adicColuna( "Seq." );
		tabItens.adicColuna( "Número de série" );
		tabItens.adicColuna( "" );
		tabItens.adicColuna( "Dt.Fabricação" );
		tabItens.adicColuna( "Dt.Validade" );

		tabItens.setTamColuna( 30, ITENS.SEQITSERIE.ordinal() );
		tabItens.setTamColuna( 270, ITENS.NUMSERIE.ordinal() );
		tabItens.setTamColuna( 20, ITENS.STATUS.ordinal() );
		tabItens.setTamColuna( 80, ITENS.DTFABRICSERIE.ordinal() );
		tabItens.setTamColuna( 80, ITENS.DTVALIDSERIE.ordinal() );

		// tabItens.setColunaEditavel( ITENS.NUMSERIE.ordinal(), true );
		// tabItens.setColunaEditavel( ITENS.DTFABRICSERIE.ordinal(), true );
		// tabItens.setColunaEditavel( ITENS.DTVALIDSERIE.ordinal(), true );

		panelGrid.add( new JScrollPane( tabItens ), BorderLayout.CENTER );

	}

	private void carregaSeries() {

		try {

			StringBuilder sql = new StringBuilder();

			sql.append( "select t1.seqitserie, se.numserie, se.dtfabricserie, se.dtvalidserie " );

			if ( tipo == TIPO_COMPRA ) {

				sql.append( ", t1.codcompra, t1.coditcompra, t1.seqitserie from cpitcompraserie t1 " );

			}
			else if ( tipo == TIPO_VENDA ) {
				sql.append( ", t1.codvenda, t1.coditvenda, t1.seqitserie from vditvendaserie t1 " );
			}
			else if ( tipo == TIPO_RECMERC ) {

				sql.append( ", t1.ticket, t1.coditrecmerc, t1.seqitserie from eqitrecmercserie t1 " );

			}

			sql.append( "left outer join eqserie se on " );
			sql.append( "se.codemp=t1.codemppd and se.codfilial=t1.codfilialpd and se.codprod=t1.codprod and se.numserie=t1.numserie " );
			sql.append( "where t1.codemp=? and t1.codfilial=? " );

			if ( tipo == TIPO_COMPRA ) {
				sql.append( "and t1.codcompra=? and t1.coditcompra=? " );
			}
			else if ( tipo == TIPO_VENDA ) {
				sql.append( "and t1.codvenda=? and t1.coditvenda=? and t1.tipovenda='V'" );
			}
			else if ( tipo == TIPO_RECMERC ) {
				sql.append( "and t1.ticket=? and t1.coditrecmerc=? " );
			}

			sql.append( "order by 5, 6, 1 " );

			PreparedStatement ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, getCodemp() );
			ps.setInt( 2, getCodfilial() );
			ps.setInt( 3, getCod() );
			ps.setInt( 4, getCodit() );

			ResultSet rs = ps.executeQuery();

			tabItens.limpa();

			int row = 0;
			while ( rs.next() ) {

				tabItens.adicLinha();
				tabItens.setValor( rs.getInt( ITENS.SEQITSERIE.toString() ), row, ITENS.SEQITSERIE.ordinal() );
				tabItens.setValor( rs.getString( ITENS.NUMSERIE.toString() ), row, ITENS.NUMSERIE.ordinal() );
				tabItens.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( ITENS.DTFABRICSERIE.toString() ) ), row, ITENS.DTFABRICSERIE.ordinal() );
				tabItens.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( ITENS.DTVALIDSERIE.toString() ) ), row, ITENS.DTVALIDSERIE.ordinal() );

				row++;
			}

			con.commit();

		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar series." + e.getMessage(), true, con, e );
		}
	}

	private void atualizaSeries() {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;

		try {
			sql.append( "update " );

			if ( tipo == TIPO_COMPRA ) {
				sql.append( "cpitcompraserie set " );
			}
			else if ( tipo == TIPO_VENDA ) {
				sql.append( "vditvendaserie set " );
			}
			else if ( tipo == TIPO_RECMERC ) {
				sql.append( "eqitrecmercserie set " );
			}

			sql.append( " codemppd=?, codfilialpd=?, codprod=?, numserie=? " );
			sql.append( " where codemp=? and codfilial=? and " );

			if ( tipo == TIPO_COMPRA ) {
				sql.append( " codcompra=? and coditcompra=? " );
			}
			else if ( tipo == TIPO_VENDA ) {
				sql.append( " codvenda=? and coditvenda=? and tipovenda='V' " );
			}
			else if ( tipo == TIPO_RECMERC ) {
				sql.append( " ticket=? and coditrecmerc=? " );
			}

			sql.append( " and seqitserie=? " );

			int i = 0;

			System.out.println( "SQL:" + sql.toString() );

			while ( i < tabItens.getNumLinhas() ) {

				ps = con.prepareStatement( sql.toString() );

				if ( "".equals( (String) tabItens.getValor( i, ITENS.NUMSERIE.ordinal() ) ) ) {
					i++;
					return;
				}

				ps.setInt( 1, getCodemppd() );
				ps.setInt( 2, getCodfilialpd() );
				ps.setInt( 3, getCodprod() );

				ps.setString( 4, (String) tabItens.getValor( i, ITENS.NUMSERIE.ordinal() ) );

				ps.setInt( 5, getCodemp() );
				ps.setInt( 6, getCodfilial() );
				ps.setInt( 7, getCod() );
				ps.setInt( 8, getCodit() );
				ps.setInt( 9, (Integer) tabItens.getValor( i, ITENS.SEQITSERIE.ordinal() ) );

				ps.executeUpdate();

				i++;
			}

			con.commit();

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	@ Override
	public void actionPerformed( ActionEvent e ) {

		/*
		 * if ( e.getSource() == btBuscar ) { carregaSeries(); } else {
		 */
		super.actionPerformed( e );
		// }
	}

	public void keyPressed( KeyEvent e ) {

		if ( e.getSource() == tabItens && e.getKeyCode() == KeyEvent.VK_ENTER ) {

			if ( tabItens.getNumLinhas() > 0 && btOK.isEnabled() ) {
				btOK.doClick();
			}
			else if ( !btOK.isEnabled() ) {
				if ( tabItens.getLinhaSel() == tabItens.getNumLinhas() - 1 ) {
					tabItens.setLinhaSel( tabItens.getNumLinhas() - 2 );
				}
				else {
					tabItens.setLinhaSel( tabItens.getLinhaSel() - 1 );
				}
			}
		}
		else if ( e.getKeyCode() == KeyEvent.VK_ESCAPE ) {
			btCancel.doClick();
		}
	}

	public Integer getCodemp() {

		return codemp;
	}

	public void setCodemp( Integer codemp ) {

		this.codemp = codemp;
	}

	public Integer getCodfilial() {

		return codfilial;
	}

	public void setCodfilial( Integer codfilial ) {

		this.codfilial = codfilial;
	}

	public Integer getCod() {

		return cod;
	}

	public void setCod( Integer cod ) {

		this.cod = cod;
	}

	public Integer getCodit() {

		return codit;
	}

	public void setCodit( Integer codit ) {

		this.codit = codit;
	}

	public Integer getCodemppd() {

		return codemppd;
	}

	public void setCodemppd( Integer codemppd ) {

		this.codemppd = codemppd;
	}

	public Integer getCodfilialpd() {

		return codfilialpd;
	}

	public void setCodfilialpd( Integer codfilialpd ) {

		this.codfilialpd = codfilialpd;
	}

	public Integer getCodprod() {

		return codprod;
	}

	public void setCodprod( Integer codprod ) {

		this.codprod = codprod;
	}

	public String getDescprod() {

		return descprod;
	}

	public void setDescprod( String descprod ) {

		this.descprod = descprod;
	}

	public void ok() {

		atualizaSeries();

		super.ok();
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		setTitulo( "Números de série para o produto " + getDescprod() + " desta compra." );
		carregaSeries();

	}

	public void mouseClicked( MouseEvent mevt ) {

		JTablePad tabEv = (JTablePad) mevt.getSource();

		if ( mevt.getClickCount() == 2 ) {
			if ( tabEv == tabItens && tabEv.getLinhaSel() > -1 ) {

				abreDLSerie();

			}
		}
	}

	private void montaListeneres() {

		tabItens.addTabelaSelListener( this );
		tabItens.addMouseListener( this );

	}

	private void abreDLSerie() {

		DLSerie dl = new DLSerie( this, null, getCodprod(), getDescprod() );

		if ( !"".equals( (String) tabItens.getValor( tabItens.getLinhaSel(), ITENS.NUMSERIE.ordinal() ) ) ) {
			dl.setNumSerie( (String) tabItens.getValor( tabItens.getLinhaSel(), ITENS.NUMSERIE.ordinal() ) );
			dl.carregaSerie();
		}

		dl.setVisible( true );

		if ( dl.OK ) {

			tabItens.setValor( dl.getNumSerie(), tabItens.getLinhaSel(), ITENS.NUMSERIE.ordinal() );
			tabItens.setValor( Funcoes.dateToStrDate( dl.getDtFabricSerie() ), tabItens.getLinhaSel(), ITENS.DTFABRICSERIE.ordinal() );
			tabItens.setValor( Funcoes.dateToStrDate( dl.getDtValidSerie() ), tabItens.getLinhaSel(), ITENS.DTVALIDSERIE.ordinal() );

		}
		dl.dispose();

	}

	public void mouseEntered( MouseEvent arg0 ) {

		// TODO Auto-generated method stub

	}

	public void mouseExited( MouseEvent arg0 ) {

		// TODO Auto-generated method stub

	}

	public void mousePressed( MouseEvent arg0 ) {

		// TODO Auto-generated method stub

	}

	public void mouseReleased( MouseEvent arg0 ) {

		// TODO Auto-generated method stub

	}

	public void valorAlterado( TabelaSelEvent evt ) {

		// TODO Auto-generated method stub

	}

	public int getTipo() {

		return tipo;
	}

	public void setTipo( int tipo ) {

		this.tipo = tipo;
	}

}
