/**
 * @version 23/03/2004 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.pdv <BR>
 *         Classe:
 * @(#)FPrefere.java <BR>
 * 
 *                   Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                   modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                   na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                   Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                   sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                   Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                   Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                   de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                   Tela para cadastro de preferências do PDV
 * 
 */

package org.freedom.modulos.pdv;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FTabDados;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class FPreferePDV extends FTabDados {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinVenda = new JPanelPad();

	private JTextFieldPad txtCodTipoMov = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoMov = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodPlanoPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescPlanoPag = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCodProdFrete = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescProdeFrete = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JCheckBoxPad cbAutoFechaVenda = new JCheckBoxPad( "Fechar a venda apos buscar orçamento?", "S", "N" );

	private JCheckBoxPad cbHabReceber = new JCheckBoxPad( "Habilita aba receber no fechamento?", "S", "N" );

	private JCheckBoxPad cbAdicionais = new JCheckBoxPad( "Dados adicionais p/ frete no fechamento?", "S", "N" );

	private ListaCampos lcTipoMov = new ListaCampos( this, "TM" );

	private ListaCampos lcPlanoPag = new ListaCampos( this, "PP" );

	private ListaCampos lcCliente = new ListaCampos( this, "CL" );

	private ListaCampos lcProdFrete = new ListaCampos( this, "PD" );

	public FPreferePDV() {

		super();
		setTitulo( "Preferências do PDV" );
		setAtribos( 50, 50, 355, 395 );

		lcCampos.setMensInserir( false );

		lcTipoMov.add( new GuardaCampo( txtCodTipoMov, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, true ) );
		lcTipoMov.add( new GuardaCampo( txtDescTipoMov, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false ) );
		lcTipoMov.montaSql( false, "TIPOMOV", "EQ" );
		lcTipoMov.setReadOnly( true );
		txtCodTipoMov.setTabelaExterna( lcTipoMov, null );
		txtCodTipoMov.setFK( true );
		txtCodTipoMov.setNomeCampo( "CodTipoMov" );

		lcCliente.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.tp.mov.", ListaCampos.DB_PK, true ) );
		lcCliente.add( new GuardaCampo( txtRazCli, "RazCli", "Razão do cliente padrão", ListaCampos.DB_SI, false ) );
		lcCliente.montaSql( false, "CLIENTE", "VD" );
		lcCliente.setReadOnly( true );
		txtCodCli.setTabelaExterna( lcCliente, null );
		txtCodCli.setFK( true );
		txtCodCli.setNomeCampo( "CodCli" );

		lcPlanoPag.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.cli.", ListaCampos.DB_PK, true ) );
		lcPlanoPag.add( new GuardaCampo( txtDescPlanoPag, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPag.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag.setReadOnly( true );
		txtCodPlanoPag.setTabelaExterna( lcPlanoPag, null );

		lcProdFrete.add( new GuardaCampo( txtCodProdFrete, "CodProd", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcProdFrete.add( new GuardaCampo( txtDescProdeFrete, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProdFrete.montaSql( false, "PRODUTO", "EQ" );
		lcProdFrete.setReadOnly( true );
		txtCodProdFrete.setTabelaExterna( lcProdFrete, null );

		setPainel( pinVenda );
		adicTab( "Venda", pinVenda );
		adicCampo( txtCodTipoMov, 10, 30, 77, 20, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_FK, true );
		adicDescFK( txtDescTipoMov, 90, 30, 230, 20, "DescTipoMov", "Descrição do tipo de movimento" );
		adicCampo( txtCodPlanoPag, 10, 70, 77, 20, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_FK, true );
		adicDescFK( txtDescPlanoPag, 90, 70, 230, 20, "DescPlanoPag", "Descrição do plano de pagamento" );
		adicCampo( txtCodCli, 10, 110, 77, 20, "CodCli", "Cód.cli.", ListaCampos.DB_FK, true );
		adicDescFK( txtRazCli, 90, 110, 230, 20, "RazCli", "Razão do cliente padrão" );
		adicDB( cbAutoFechaVenda, 20, 155, 270, 20, "AutoFechaVenda", "", true );
		adicDB( cbHabReceber, 20, 175, 270, 20, "HabReceber", "", true );
		adicDB( cbAdicionais, 20, 195, 270, 20, "AdicPDV", "", true );
		adic( new JLabelPad( "Produto para frete." ), 20, 220, 250, 20 );
		adicCampo( txtCodProdFrete, 20, 255, 77, 20, "CodProd", "Cód.prod.", ListaCampos.DB_FK, false );
		adicDescFK( txtDescProdeFrete, 100, 255, 200, 20, "DescProd", "Descrição do produto" );

		JLabel lbBorda = new JLabel();
		lbBorda.setBorder( BorderFactory.createEtchedBorder() );
		JLabel lbOpcoes = new JLabelPad( "Opções", SwingConstants.CENTER );
		lbOpcoes.setOpaque( true );

		adic( lbOpcoes, 15, 135, 60, 20 );
		adic( lbBorda, 10, 145, 310, 140 );

		setListaCampos( false, "PREFERE4", "SG" );

		nav.setAtivo( 0, false );
		nav.setAtivo( 1, false );
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcTipoMov.setConexao( cn );
		lcPlanoPag.setConexao( cn );
		lcCliente.setConexao( cn );
		lcProdFrete.setConexao( cn );
		lcCampos.carregaDados();
	}
}
