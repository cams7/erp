/**
 * @version 14/12/2007 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FTransfEstoque.java <BR>
 * 
 *                         Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                         modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                         na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                         Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                         sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                         Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                         Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                         de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 */

package org.freedom.modulos.std.view.frame.utility;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.bmps.Icone;

public class FTransfEstoque extends FFilho implements ActionListener, CarregaListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtRefProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodBarProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtQtdPod = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtCodAlmoxOrig = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescAlmoxOrig = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodAlmoxDest = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescAlmoxDest = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtSaldoProdOrig = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, 5 );

	private JTextFieldFK txtSaldoProdDest = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, 5 );

	private JTextFieldPad txtQtdTrans = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private ListaCampos lcProduto = new ListaCampos( this );

	private ListaCampos lcAlmoxOrig = new ListaCampos( this );

	private ListaCampos lcAlmoxDest = new ListaCampos( this );

	private JPanelPad pnCenter = new JPanelPad( 300, 300 );

	private JButtonPad btExecuta = new JButtonPad( Icone.novo( "btExecuta.png" ) );

	private int codAlmox = 0;

	private int sldprod = 1;

	public FTransfEstoque() {

		super( true );
		setTitulo( "Tranferência de produtos/almoxarifados" );
		setAtribos( 10, 30, 450, 300 );
		adicBotaoSair();

		montaTela();
		montaListaCampos();

	}

	private void montaTela() {

		add( pnCenter );

		pnCenter.adic( new JLabelPad( "Cód.Prod" ), 10, 5, 80, 20 );
		pnCenter.adic( txtCodProd, 10, 25, 80, 20 );
		pnCenter.adic( new JLabelPad( "Descrição do produto" ), 100, 5, 250, 20 );
		pnCenter.adic( txtDescProd, 100, 25, 250, 20 );
		pnCenter.adic( new JLabelPad( "Cód.Alm.Orig" ), 10, 45, 80, 20 );
		pnCenter.adic( txtCodAlmoxOrig, 10, 65, 80, 20 );
		pnCenter.adic( new JLabelPad( "Descrição do almoxarifado de origen" ), 100, 45, 250, 20 );
		pnCenter.adic( txtDescAlmoxOrig, 100, 65, 250, 20 );
		pnCenter.adic( new JLabelPad( "Saldo" ), 357, 45, 50, 20 );
		pnCenter.adic( txtSaldoProdOrig, 357, 65, 50, 20 );
		pnCenter.adic( new JLabelPad( "Cód.Alm.Dest" ), 10, 85, 80, 20 );
		pnCenter.adic( txtCodAlmoxDest, 10, 105, 80, 20 );
		pnCenter.adic( new JLabelPad( "Descrição do almoxarifado de destino" ), 100, 85, 250, 20 );
		pnCenter.adic( txtDescAlmoxDest, 100, 105, 250, 20 );
		pnCenter.adic( new JLabelPad( "Saldo" ), 357, 85, 50, 20 );
		pnCenter.adic( txtSaldoProdDest, 357, 105, 50, 20 );

		pnCenter.adic( new JLabelPad( "Quantidade" ), 10, 130, 70, 20 );
		pnCenter.adic( txtQtdTrans, 10, 150, 70, 20 );
		pnCenter.adic( btExecuta, 10, 180, 30, 30 );

		btExecuta.addActionListener( this );
		lcProduto.addCarregaListener( this );
	}

	private void montaListaCampos() {

		/**
		 * LC Produto
		 */

		lcProduto.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.produto", ListaCampos.DB_PK, true ) );
		lcProduto.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProduto.add( new GuardaCampo( txtRefProd, "RefProd", "Ref. produto", ListaCampos.DB_SI, false ) );
		lcProduto.add( new GuardaCampo( txtCodBarProd, "CodBarProd", "Cód. Barras", ListaCampos.DB_SI, false ) );
		txtCodProd.setTabelaExterna( lcProduto, null );
		txtCodProd.setNomeCampo( "CodProd" );
		txtCodProd.setFK( true );
		lcProduto.setReadOnly( true );
		lcProduto.montaSql( false, "PRODUTO", "EQ" );

		/**
		 * LC almoxarifado de origen
		 */

		lcAlmoxOrig.add( new GuardaCampo( txtCodAlmoxOrig, "CodAlmox", "Cód.almox.", ListaCampos.DB_PK, true ) );
		lcAlmoxOrig.add( new GuardaCampo( txtDescAlmoxOrig, "DescAlmox", "Descrição do almox.", ListaCampos.DB_SI, false ) );
		txtCodAlmoxOrig.setTabelaExterna( lcAlmoxOrig, null );
		txtCodAlmoxOrig.setNomeCampo( "CodAlmox" );
		txtCodAlmoxOrig.setFK( true );
		lcAlmoxOrig.setReadOnly( true );
		lcAlmoxOrig.montaSql( false, "ALMOX", "EQ" );

		/**
		 *LC almoxarifado de destino
		 */

		lcAlmoxDest.add( new GuardaCampo( txtCodAlmoxDest, "CodAlmox", "Cód.almox.", ListaCampos.DB_PK, true ) );
		lcAlmoxDest.add( new GuardaCampo( txtDescAlmoxDest, "DescAlmox", "Descrição do almox.", ListaCampos.DB_SI, false ) );
		txtCodAlmoxDest.setTabelaExterna( lcAlmoxDest, null );
		txtCodAlmoxDest.setNomeCampo( "CodAlmox" );
		txtCodAlmoxDest.setFK( true );
		lcAlmoxDest.setReadOnly( true );
		lcAlmoxDest.montaSql( false, "ALMOX", "EQ" );

	}

	private void fazTransf() {

		StringBuffer sSQLDelet = new StringBuffer();
		StringBuffer sSQLInsert = new StringBuffer();

		if ( "".equals( txtCodProd.getVlrString() ) ) {
			Funcoes.mensagemInforma( this, "Código do produto é requerido!" );
		}
		else if ( "".equals( txtCodAlmoxOrig.getVlrString() ) ) {
			Funcoes.mensagemInforma( this, "Almoxarifado de origen é requerido!" );
		}
		else if ( "".equals( txtCodAlmoxDest.getVlrString() ) ) {
			Funcoes.mensagemInforma( this, "Almoxarifado de destino é requerido!" );
		}
		else if ( txtCodAlmoxOrig.getVlrString().equals( txtCodAlmoxDest.getVlrString() ) ) {
			Funcoes.mensagemInforma( this, "O almoxarifado de destino não pode ser igual ao de origem" );

		}

	}

	private void getAlmoxarifado() {

		StringBuffer sSQL = new StringBuffer();
		int codprod = txtCodProd.getVlrInteger();

		sSQL.append( "SELECT P.CODALMOX, P.SLDPROD FROM EQPRODUTO P " );
		sSQL.append( "WHERE P.CODEMP=? AND CODFILIAL=? AND CODPROD=?" );

		try {
			PreparedStatement ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			ps.setInt( 3, codprod );
			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {

				codAlmox = rs.getInt( "CODALMOX" );
				sldprod = rs.getInt( "SLDPROD" );

				txtCodAlmoxOrig.setVlrInteger( codAlmox );
				txtSaldoProdOrig.setVlrInteger( sldprod );
			}

		} catch ( SQLException e ) {
			Funcoes.mensagemInforma( this, "Erro ao buscar dados do produto\n" + e.getMessage() );
			e.printStackTrace();

		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcProduto.setConexao( cn );
		lcAlmoxOrig.setConexao( cn );
		lcAlmoxDest.setConexao( cn );

	}

	public void actionPerformed( ActionEvent e ) {

		if ( e.getSource() == btExecuta ) {
			fazTransf();
		}
	}

	public void afterCarrega( CarregaEvent cevt ) {

	}

	public void beforeCarrega( CarregaEvent cevt ) {

		if ( cevt.getListaCampos() == lcProduto ) {
			getAlmoxarifado();
		}
	}
}
