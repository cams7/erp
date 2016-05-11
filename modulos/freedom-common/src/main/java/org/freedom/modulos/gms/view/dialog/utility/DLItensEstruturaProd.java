/*
 * Projeto: Freedom Pacote: org.freedom.modules.std Classe: @(#)DLItensEstruturaProd.java
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
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import org.freedom.acao.TabelaSelEvent;
import org.freedom.acao.TabelaSelListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;

/**
 * Tela para seleção dos ítens de extrutura de um produto.
 * 
 * @author Setpoint Informática Ltda./Anderson Sanchez
 * @version 04/08/2010
 */
public class DLItensEstruturaProd extends FFDialogo implements MouseListener, TabelaSelListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad panelGeral = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad panelMaster = new JPanelPad( 700, 60 );

	private JPanelPad panelGrid = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTablePad tabItens = new JTablePad();

	private Integer codemp;

	private Integer codfilial;

	private Integer codit;

	private Integer codprod;

	private Integer codemppd;

	private Integer codfilialpd;

	private String descprod;
	
	private Integer seqest;

	private int tipo;
	
	private BigDecimal qtdprod = new BigDecimal(1);

	public enum ITENS {
		SELECAO, SEQITEST, CODPRODPD, REFPRODPD,  DESCPROD, QTDITEST, CODFASE, RMAAUTOITEST;
	}

	public DLItensEstruturaProd(BigDecimal qtdprodp) {

		super();
		
		if(qtdprodp != null) {
			qtdprod = qtdprodp;
		}
		
		if(con==null) {
			con = Aplicativo.getInstace().getConexao();
		}
		
		setAtribos( 520, 520 );
		setResizable( true );
		montaListeneres();
 
		montaTela();

	}

	private void montaTela() {

		setPanel( panelGeral );

		// ***** Grid

		panelGeral.add( panelGrid, BorderLayout.CENTER );
		panelGrid.setBorder( BorderFactory.createEtchedBorder() );

		tabItens.setRowHeight( 21 );

		tabItens.adicColuna( "" );
		tabItens.adicColuna( "" );	
		tabItens.adicColuna( "Cod.Prod." );
		tabItens.adicColuna( "Ref.Prod." );
		tabItens.adicColuna( "Descrição do produto" );
		tabItens.adicColuna( "Qtd." );
		tabItens.adicColuna( "Cod.Fase" );
		tabItens.adicColuna( "Gera RMA" );

		tabItens.setTamColuna( 30, ITENS.SELECAO.ordinal() );
		tabItens.setTamColuna( 30, ITENS.SEQITEST.ordinal() );
		tabItens.setTamColuna( 60, ITENS.CODPRODPD.ordinal() );
		tabItens.setTamColuna( 70, ITENS.REFPRODPD.ordinal() );		
		tabItens.setTamColuna( 200, ITENS.DESCPROD.ordinal() );
		tabItens.setTamColuna( 80, ITENS.QTDITEST.ordinal() ); 

		tabItens.setColunaInvisivel( ITENS.CODFASE.ordinal() );
		tabItens.setColunaInvisivel( ITENS.RMAAUTOITEST.ordinal() );
		
		tabItens.setColunaEditavel( ITENS.SELECAO.ordinal(), true );
		tabItens.setColunaEditavel( ITENS.QTDITEST.ordinal(), true );

		panelGrid.add( new JScrollPane( tabItens ), BorderLayout.CENTER );

	}

	public void carregaItens(boolean servico) {

		try {

			StringBuilder sql = new StringBuilder();

			// Se a sequência de estrutura não foi informada, deve considerar a sequencia nro. 1
			if(getSeqest()==null) {
				setSeqest( 1 );
			}

			sql.append( "select ie.seqitest, ie.codprodpd, ie.refprodpd, pd.descprod, ie.qtditest, " );
			
			sql.append( "ie.codempfs, ie.codfilialfs, ie.codfase, ie.rmaautoitest ");
			
			sql.append( "from ppitestrutura ie, eqproduto pd " );
			
			sql.append( "where pd.codprod=ie.codprodpd and pd.codfilial=ie.codfilialpd and pd.codemp=ie.codemppd and " );
			
			sql.append( "ie.codemp=? and ie.codfilial=? and ie.codprod=? and ie.seqest=? " );

			if(servico) {
				sql.append( " and pd.tipoprod='S' and ie.rmaautoitest='S' ");
			}
			
			sql.append( "order by ie.codfase, ie.seqitest " );
			
			PreparedStatement ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, getCodemp() );
			ps.setInt( 2, getCodfilial() );
			ps.setInt( 3, getCodProd() ); 
			ps.setInt( 4, getSeqest() ); 

			ResultSet rs = ps.executeQuery();

			tabItens.limpa();

			int row = 0;

			while ( rs.next() ) {

				tabItens.adicLinha();

				tabItens.setValor( new Boolean( servico ), row, ITENS.SELECAO.ordinal() );

				tabItens.setValor( rs.getInt( ITENS.SEQITEST.name() ), row, ITENS.SEQITEST.ordinal() );

				tabItens.setValor( rs.getInt( ITENS.CODPRODPD.name() ), row, ITENS.CODPRODPD.ordinal() );

				tabItens.setValor( rs.getString( ITENS.REFPRODPD.name() ), row, ITENS.REFPRODPD.ordinal() );
				
				tabItens.setValor( rs.getString( ITENS.DESCPROD.name() ), row, ITENS.DESCPROD.ordinal() );

				BigDecimal qtditest = rs.getBigDecimal( ITENS.QTDITEST.name());
				
				qtditest = qtditest.multiply( qtdprod );
				
				qtditest.setScale( 2 );
				
				tabItens.setValor( qtditest, row, ITENS.QTDITEST.ordinal() );
				 
				tabItens.setValor( rs.getInt( ITENS.CODFASE.name() ), row, ITENS.CODFASE.ordinal() );
				tabItens.setValor( rs.getString( ITENS.RMAAUTOITEST.name() ), row, ITENS.RMAAUTOITEST.ordinal() );
				

				row++;
			}
			
			if(servico) {
				btOK.doClick();
			}


		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar series." + e.getMessage(), true, con, e );
		}
	}

	public void actionPerformed( ActionEvent e ) {

		super.actionPerformed( e );

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

	public Integer getCodProd() {

		return codprod;
	}

	public void setCod( Integer cod ) {

		this.codprod = cod;
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

	
	public Integer getSeqest() {
	
		return seqest;
	}

	
	public void setSeqest( Integer seqest ) {
	
		this.seqest = seqest;
	}

	public Vector<HashMap<String,Object>> getValores() {
		
		Vector<HashMap<String,Object>> ret = new Vector<HashMap<String,Object>>();
		
		try {
			
			
			
			for(int i=0; i<tabItens.getNumLinhas() ; i++) {
		
				if( (Boolean) tabItens.getValor( i, ITENS.SELECAO.ordinal() ) ) { 
			
					HashMap<String, Object> item = new HashMap<String, Object>();
					
					item.put( ITENS.CODPRODPD.name(), tabItens.getValor( i, ITENS.CODPRODPD.ordinal() ) );
					item.put( ITENS.REFPRODPD.name(), tabItens.getValor( i, ITENS.REFPRODPD.ordinal() ) );
					item.put( ITENS.QTDITEST.name(), tabItens.getValor( i, ITENS.QTDITEST.ordinal() ) );
					item.put( ITENS.CODFASE.name(), tabItens.getValor( i, ITENS.CODFASE.ordinal() ) );
					item.put( "GERARMA", tabItens.getValor( i, ITENS.RMAAUTOITEST.ordinal() ) );
					
					
			
					ret.addElement( item );
				}
				
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public void ok() {
		super.ok();
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		setTitulo( "Estrutura do produto: " + getDescprod() );
//		carregaItens();

	}

	public void mouseClicked( MouseEvent mevt ) {

		JTablePad tabEv = (JTablePad) mevt.getSource();

		if ( mevt.getClickCount() == 2 ) {
			if ( tabEv == tabItens && tabEv.getLinhaSel() > -1 ) {

			}
		}
	}

	private void montaListeneres() {

		tabItens.addTabelaSelListener( this );
		tabItens.addMouseListener( this );

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




