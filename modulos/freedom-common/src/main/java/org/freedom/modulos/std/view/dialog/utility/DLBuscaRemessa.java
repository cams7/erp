/*
 * Projeto: Freedom Pacote: org.freedom.modules.std Classe: @(#)DLBuscaVenda.java
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR> modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR> na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR> sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR> Veja a Licença Pública Geral GNU para maiores detalhes. <BR> Você deve ter recebido uma cópia da Licença Pública
 * Geral GNU junto com este programa, se não, <BR> escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA <BR> <BR>
 */

package org.freedom.modulos.std.view.dialog.utility;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import org.freedom.bmps.Icone;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;

/**
 * Busca nota de remessa para importar itens para remessa consignada.
 * 
 * @author Setpoint Informática Ltda./Alex Rodrigues
 * @version 27/08/2009
 */
public class DLBuscaRemessa extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JPanelPad panelGeral = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad panelMaster = new JPanelPad( 700, 60 );

	private JPanelPad panelGrid = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTextFieldPad txtDataIni = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDataFim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JButtonPad btBuscar = new JButtonPad( Icone.novo( "btExecuta.png" ) );

	private JTablePad tabItens = new JTablePad();

	private Integer codigoVenda;

	private Integer codigoItem;

	private Integer codigoProduto;

	private enum ITENS {
		CODVENDA, ITEM, CODPROD, DESCPROD, QUANTIDADE, PRECO, CODCLI, RAZCLI, CODPLANOPAG, DESCPLANOPAG;
	}

	public DLBuscaRemessa( Integer produto ) {

		super();
		setTitulo( "Selecione o item de nota de remessa" );
		setAtribos( 700, 320 );
		setResizable( true );

		montaTela();

		btBuscar.addActionListener( this );

		Calendar calendar = Calendar.getInstance();
		txtDataFim.setVlrDate( calendar.getTime() );
		calendar.set( Calendar.DAY_OF_YEAR, 1 );
		txtDataIni.setVlrDate( calendar.getTime() );

		codigoProduto = produto;
	}

	private void montaTela() {

		setPanel( panelGeral );
		panelGeral.add( panelMaster, BorderLayout.NORTH );

		JPanelPad pnPeriodo = new JPanelPad();
		pnPeriodo.setBorder( BorderFactory.createTitledBorder( "Período" ) );
		pnPeriodo.adic( new JLabelPad( "De:" ), 4, 0, 20, 20 );
		pnPeriodo.adic( txtDataIni, 25, 0, 75, 20 );
		pnPeriodo.adic( new JLabelPad( "Até:" ), 105, 0, 25, 20 );
		pnPeriodo.adic( txtDataFim, 130, 0, 75, 20 );

		panelMaster.adic( pnPeriodo, 4, 4, 220, 50 );
		panelMaster.adic( btBuscar, 224, 12, 30, 40 );

		// ***** Grid

		panelGeral.add( panelGrid, BorderLayout.CENTER );
		panelGrid.setBorder( BorderFactory.createEtchedBorder() );

		tabItens.adicColuna( "Venda" );
		tabItens.adicColuna( "Item" );
		tabItens.adicColuna( "Código" );
		tabItens.adicColuna( "Descrição do produto" );
		tabItens.adicColuna( "Quantidade" );
		tabItens.adicColuna( "Preço" );
		tabItens.adicColuna( "Cód.cli." );
		tabItens.adicColuna( "Razão social do cliente" );
		tabItens.adicColuna( "Cód.pl.pag." );
		tabItens.adicColuna( "Descrição do plano de pagamento" );

		tabItens.setTamColuna( 80, ITENS.CODVENDA.ordinal() );
		tabItens.setTamColuna( 60, ITENS.ITEM.ordinal() );
		tabItens.setTamColuna( 80, ITENS.CODPROD.ordinal() );
		tabItens.setTamColuna( 230, ITENS.DESCPROD.ordinal() );
		tabItens.setTamColuna( 80, ITENS.QUANTIDADE.ordinal() );
		tabItens.setTamColuna( 80, ITENS.PRECO.ordinal() );
		tabItens.setTamColuna( 80, ITENS.CODCLI.ordinal() );
		tabItens.setTamColuna( 230, ITENS.RAZCLI.ordinal() );
		tabItens.setTamColuna( 80, ITENS.CODPLANOPAG.ordinal() );
		tabItens.setTamColuna( 230, ITENS.DESCPLANOPAG.ordinal() );

		panelGrid.add( new JScrollPane( tabItens ), BorderLayout.CENTER );
	}

	private void carregaVendas() {

		try {

			StringBuilder selectVendas = new StringBuilder();
			selectVendas.append( "SELECT I.CODVENDA, I.CODITVENDA, I.CODPROD, P.DESCPROD, I.QTDITVENDA, I.PRECOITVENDA, " );
			selectVendas.append( "V.CODCLI, C.RAZCLI, V.CODPLANOPAG, PG.DESCPLANOPAG " );
			selectVendas.append( "FROM VDITVENDA I, VDVENDA V, VDCLIENTE C, FNPLANOPAG PG, EQPRODUTO P, EQTIPOMOV TM " );
			selectVendas.append( "WHERE I.CODEMP=V.CODEMP AND I.CODFILIAL=V.CODFILIAL AND I.CODVENDA=V.CODVENDA AND I.TIPOVENDA=V.TIPOVENDA AND " );
			selectVendas.append( "V.CODEMP=? AND V.CODFILIAL=? AND V.DTEMITVENDA BETWEEN ? AND ? AND " );
			selectVendas.append( "P.CODEMP=I.CODEMPPD AND P.CODFILIAL=I.CODFILIALPD AND P.CODPROD=I.CODPROD AND P.CODPROD=? AND " );
			selectVendas.append( "C.CODEMP=V.CODEMPCL AND C.CODFILIAL=V.CODFILIALCL AND C.CODCLI=V.CODCLI AND " );
			selectVendas.append( "PG.CODEMP=V.CODEMPPG AND PG.CODFILIAL=V.CODFILIALPG AND PG.CODPLANOPAG=V.CODPLANOPAG AND " );
			selectVendas.append( "TM.CODEMP=V.CODEMPTM AND TM.CODFILIAL=V.CODFILIALTM AND TM.CODTIPOMOV=V.CODTIPOMOV AND TM.TIPOMOV='VR' " );
			selectVendas.append( "ORDER BY I.CODVENDA, I.CODITVENDA" );

			PreparedStatement ps = con.prepareStatement( selectVendas.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDITVENDA" ) );
			ps.setDate( 3, Funcoes.dateToSQLDate( txtDataIni.getVlrDate() ) );
			ps.setDate( 4, Funcoes.dateToSQLDate( txtDataFim.getVlrDate() ) );
			ps.setInt( 5, codigoProduto );

			ResultSet rs = ps.executeQuery();

			tabItens.limpa();

			int row = 0;
			while ( rs.next() ) {

				tabItens.adicLinha();
				tabItens.setValor( rs.getInt( "CODVENDA" ), row, ITENS.CODVENDA.ordinal() );
				tabItens.setValor( rs.getInt( "CODITVENDA" ), row, ITENS.ITEM.ordinal() );
				tabItens.setValor( rs.getInt( "CODPROD" ), row, ITENS.CODPROD.ordinal() );
				tabItens.setValor( rs.getString( "DESCPROD" ), row, ITENS.DESCPROD.ordinal() );
				tabItens.setValor( Funcoes.bdToStr( rs.getBigDecimal( "QTDITVENDA" ) ), row, ITENS.QUANTIDADE.ordinal() );
				tabItens.setValor( Funcoes.bdToStr( rs.getBigDecimal( "PRECOITVENDA" ) ), row, ITENS.PRECO.ordinal() );
				tabItens.setValor( rs.getInt( "CODCLI" ), row, ITENS.CODCLI.ordinal() );
				tabItens.setValor( rs.getString( "RAZCLI" ), row, ITENS.RAZCLI.ordinal() );
				tabItens.setValor( rs.getInt( "CODPLANOPAG" ), row, ITENS.CODPLANOPAG.ordinal() );
				tabItens.setValor( rs.getString( "DESCPLANOPAG" ), row, ITENS.DESCPLANOPAG.ordinal() );

				row++;
			}

			con.commit();

		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar nota de remessa.\n" + e.getMessage(), true, con, e );
		}
	}

	public HashMap<String, Integer> getVenda() {

		HashMap<String, Integer> venda = new HashMap<String, Integer>();

		try {

			venda.put( "codemp", Aplicativo.iCodEmp );
			venda.put( "codfilial", ListaCampos.getMasterFilial( "VDITVENDA" ) );
			venda.put( "codvenda", codigoVenda );
			venda.put( "coditvenda", codigoItem );
		} catch ( Exception e ) {
			Funcoes.mensagemErro( null, "Erro ao selecionar ítem de compra!" );
		}

		return venda;
	}

	@ Override
	public void actionPerformed( ActionEvent e ) {

		if ( e.getSource() == btBuscar ) {
			carregaVendas();
		}
		else {
			super.actionPerformed( e );
		}
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

	public void ok() {

		if ( tabItens.getLinhaSel() > -1 ) {
			codigoVenda = (Integer) tabItens.getValueAt( tabItens.getLinhaSel(), ITENS.CODVENDA.ordinal() );
			codigoItem = (Integer) tabItens.getValueAt( tabItens.getLinhaSel(), ITENS.ITEM.ordinal() );
		}

		super.ok();
	}
}
