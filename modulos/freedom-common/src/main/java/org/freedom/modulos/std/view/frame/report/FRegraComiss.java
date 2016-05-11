/**
 * @version 20/05/2008 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FRegraFiscal.java <BR>
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
 *                       Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.std.view.frame.report;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDetalhe;
import org.freedom.modulos.gms.view.frame.crud.plain.FSecaoProd;
import org.freedom.modulos.std.view.frame.crud.tabbed.FVendedor;

public class FRegraComiss extends FDetalhe {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCab = new JPanelPad();

	private JPanelPad pinDet = new JPanelPad();

	private JTextFieldPad txtCodRegrComis = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );

	private JTextFieldPad txtDescRegrComis = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodTipoVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );

	private JTextFieldFK txtDescTipoVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtPercComis = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 7, 3 );
	
	private JTextFieldPad txtPercComisGeral = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 7, 3 );

	private JTextFieldPad txtSeqComis = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JCheckBoxPad cbObrig = new JCheckBoxPad( "Sim", "S", "N" );

	private ListaCampos lcTipoVend = new ListaCampos( this, "TV" );
	
	private ListaCampos lcSecao = new ListaCampos( this, "SC" );
	
	private JTextFieldPad txtCodSecao = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );
	
	private JTextFieldFK txtDescSecao = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private ListaCampos lcVend = new ListaCampos( this, "VD" );
	
	private JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );


	public FRegraComiss() {

		setTitulo( "Regras de comissionamento" );
		setAtribos( 50, 50, 600, 450 );

		pinCab = new JPanelPad( 440, 140 );
		setAltCab( 140 );

		//FK com a Secao de produção
		
		lcSecao.add( new GuardaCampo( txtCodSecao, "CodSecao", "Cód.Seção", ListaCampos.DB_PK, false ) );
		lcSecao.add( new GuardaCampo( txtDescSecao, "DescSecao", "Descrição da seção", ListaCampos.DB_SI, false ) );
		
		lcSecao.montaSql( false, "SECAO", "EQ" );
		lcSecao.setReadOnly( true );
		lcSecao.setQueryCommit( false );
		txtCodSecao.setTabelaExterna( lcSecao, FSecaoProd.class.getCanonicalName() );
		
		//FK com o vendedor
		
		lcVend.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.comiss.", ListaCampos.DB_PK, false ) );
		lcVend.add( new GuardaCampo( txtDescVend, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, false ) );
		
		lcVend.setDinWhereAdic( "CODTIPOVEND = #N", txtCodTipoVend );
//		lcVend.setDinWhereAdic( "CODSECAO = #S", txtCodSecao );
		lcVend.montaSql( false, "VENDEDOR", "VD" );
		lcVend.setQueryCommit( false );
		lcVend.setReadOnly( true );
		
		txtCodVend.setTabelaExterna( lcVend, FVendedor.class.getCanonicalName() );
		
		setListaCampos( lcCampos );
		setPainel( pinCab, pnCliCab );
		
		adicCampo( txtCodRegrComis, 7, 20, 80, 20, "CodRegrComis", "Cód.Regra", ListaCampos.DB_PK, true );
		adicCampo( txtDescRegrComis, 90, 20, 350, 20, "DescRegrComis", "Descrição da regra de comissionamento", ListaCampos.DB_SI, true );

		adicCampo( txtCodSecao, 7, 60, 80, 20, "CodSecao", "Cód.seção", ListaCampos.DB_FK, txtDescSecao, false );
		adicDescFK( txtDescSecao, 90, 60, 250, 20, "DescSecao", "Descrição da seção" );
		
		adicCampo( txtPercComisGeral, 343, 60, 97, 20, "PercComisGeral", "%Comis.", ListaCampos.DB_SI, false );
		
		

		setListaCampos( true, "REGRACOMIS", "VD" );
		
		lcCampos.setQueryInsert( true );

		lcTipoVend.add( new GuardaCampo( txtCodTipoVend, "CodTipoVend", "Cód.tp.comis.", ListaCampos.DB_PK, false ) );
		lcTipoVend.add( new GuardaCampo( txtDescTipoVend, "DescTipoVend", "Descrição do tipo de comissionado", ListaCampos.DB_SI, false ) );
		lcTipoVend.montaSql( false, "TIPOVEND", "VD" );
		lcTipoVend.setQueryCommit( false );
		lcTipoVend.setReadOnly( true );
		txtCodTipoVend.setTabelaExterna( lcTipoVend, null );

		setAltDet( 120 );

		pinDet = new JPanelPad( 600, 80 );
		
		setPainel( pinDet, pnDet );
		setListaCampos( lcDet );
		setNavegador( navRod );
		
		adicCampo( txtSeqComis, 7, 25, 30, 20, "SeqItRc", "Item", ListaCampos.DB_PK, true );
		
		adicCampo( txtCodTipoVend, 40, 25, 80, 20, "CodTipoVend", "Cód.tp.comis.", ListaCampos.DB_FK, txtDescTipoVend, true );
		adicDescFK( txtDescTipoVend, 123, 25, 280, 20, "DescTipoVend", "Descrição do tipo de comissionado" );
		adicCampo( txtPercComis, 406, 25, 55, 20, "PercComisItRc", "% Comis.", ListaCampos.DB_SI, true );
		
		adicDB( cbObrig, 463, 25, 87, 20, "ObrigItRc", "Obrigatório?", true );
		
		adicCampo( txtCodVend, 40, 65, 80, 20, "CodVend", "Cód.comiss.", ListaCampos.DB_FK, txtDescVend, false );
		adicDescFK( txtDescVend, 123, 65, 280, 20, "NomeVend", "Nome do comissionado" );
		
		setListaCampos( true, "ITREGRACOMIS", "VD" );
		lcDet.setQueryInsert( true );

		montaTab();
		tab.setTamColuna( 220, 2 );

		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
	}

	public void setConexao( DbConnection con ) {

		super.setConexao( con );
		
		lcTipoVend.setConexao( con );
		lcSecao.setConexao( con );
		lcVend.setConexao( con );
		
	}
}
