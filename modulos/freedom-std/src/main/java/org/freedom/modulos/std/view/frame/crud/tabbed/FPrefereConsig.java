/**
 * @version 02/03/2009 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FPrefereGeral.java <BR>
 * 
 *                        Este programa é licenciado de acordo com a LPG-PC (Licenï¿½a Pï¿½blica Geral para Programas de Computador), <BR>
 *                        versï¿½o 2.1.0 ou qualquer versï¿½o posterior. <BR>
 *                        A LPG-PC deve acompanhar todas PUBLICAï¿½ï¿½ES, DISTRIBUIï¿½ï¿½ES e REPRODUÇÕES deste Programa. <BR>
 *                        Caso uma cï¿½pia da LPG-PC nï¿½o esteja disponï¿½vel junto com este Programa, vocï¿½ pode contatar <BR>
 *                        o LICENCIADOR ou entï¿½o pegar uma cï¿½pia em: <BR>
 *                        Licenï¿½a: http://www.lpg.adv.br/licencas/lpgpc.rtf <BR>
 *                        Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa ï¿½ preciso estar <BR>
 *                        de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                        Tela de cadastro das preferências do sistema. Esse cadastro ï¿½ utilizado para parametrizar o sistema de acordo com as necessidades especï¿½ficas da empresa.
 * 
 */

package org.freedom.modulos.std.view.frame.crud.tabbed;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FTabDados;
import org.freedom.modulos.fnc.library.swing.component.JTextFieldPlan;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FTipoMov;
import org.freedom.modulos.std.view.frame.crud.special.FPlanejamento;

public class FPrefereConsig extends FTabDados {

	private static final long serialVersionUID = 1L;

	/****************
	 * Lista Campos *
	 ****************/

	private ListaCampos lcTipoMov = new ListaCampos( this, "CO" );

	private ListaCampos lcTipoMovTv = new ListaCampos( this, "TV" );

	private ListaCampos lcTipoMovTP = new ListaCampos( this, "TP" );

	private ListaCampos lcPlanConsig = new ListaCampos( this, "PC" );

	private ListaCampos lcPlanVDConsig = new ListaCampos( this, "PV" );

	/****************
	 * Fields *
	 ****************/

	private JTextFieldPad txtCodTipoMov = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoMov = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodTipoMovTv = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoMovTv = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodTipoMovTp = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoMovTp = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPlan txtCodPlanConsig = new JTextFieldPlan( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldFK txtDescPlanConsig = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPlan txtCodPlanVDConsig = new JTextFieldPlan( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldFK txtDescPlanVDConsig = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	/****************
	 * Paineis *
	 ****************/

	private JPanelPad pinGeral = new JPanelPad( 330, 200 );

	public FPrefereConsig() {

		super();

		setTitulo( "Preferências Vendas Consignadas" );
		setAtribos( 30, 40, 420, 200 );
		lcCampos.setMensInserir( false );

		montaListaCampos();
		montaTela();
	}

	private void montaListaCampos() {

		/****************************
		 * Tipo de mov. consignação *
		 ***************************/

		lcTipoMov.add( new GuardaCampo( txtCodTipoMov, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false ) );
		lcTipoMov.add( new GuardaCampo( txtDescTipoMov, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false ) );
		lcTipoMov.montaSql( false, "TIPOMOV", "EQ" );
		lcTipoMov.setQueryCommit( false );
		lcTipoMov.setReadOnly( true );
		txtCodTipoMov.setTabelaExterna( lcTipoMov, FTipoMov.class.getCanonicalName() );

		/****************************
		 * Tipo de mov. ped. venda *
		 ***************************/

		lcTipoMovTv.add( new GuardaCampo( txtCodTipoMovTv, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false ) );
		lcTipoMovTv.add( new GuardaCampo( txtDescTipoMovTv, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false ) );
		lcTipoMovTv.montaSql( false, "TIPOMOV", "EQ" );
		lcTipoMovTv.setQueryCommit( false );
		lcTipoMovTv.setReadOnly( true );
		txtCodTipoMovTv.setTabelaExterna( lcTipoMovTv, FTipoMov.class.getCanonicalName() );

		/****************************
		 * Tipo de mov. Venda *
		 ***************************/

		lcTipoMovTP.add( new GuardaCampo( txtCodTipoMovTp, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false ) );
		lcTipoMovTP.add( new GuardaCampo( txtDescTipoMovTp, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false ) );
		lcTipoMovTP.montaSql( false, "TIPOMOV", "EQ" );
		lcTipoMovTP.setQueryCommit( false );
		lcTipoMovTP.setReadOnly( true );
		txtCodTipoMovTp.setTabelaExterna( lcTipoMovTP, FTipoMov.class.getCanonicalName() );

		/******************************
		 * Planejamento Consignacao *
		 *****************************/

		txtCodPlanConsig.setTabelaExterna( lcPlanConsig, FPlanejamento.class.getCanonicalName() );
		lcPlanConsig.add( new GuardaCampo( txtCodPlanConsig, "CodPlan", "Cód.plan.", ListaCampos.DB_PK, txtDescPlanConsig, false ) );
		lcPlanConsig.add( new GuardaCampo( txtDescPlanConsig, "DescPlan", "Descriçao do planejamento", ListaCampos.DB_SI, false ) );
		lcPlanConsig.setWhereAdic( "TIPOPLAN = 'R'" );
		lcPlanConsig.montaSql( false, "PLANEJAMENTO", "FN" );
		lcPlanConsig.setReadOnly( true );

		/***********************************
		 * Planejamento Venda Consignada *
		 ***********************************/

		txtCodPlanVDConsig.setTabelaExterna( lcPlanVDConsig, FPlanejamento.class.getCanonicalName() );
		txtCodPlanVDConsig.setFK( true );
		txtCodPlanVDConsig.setNomeCampo( "CodPlan" );
		lcPlanVDConsig.add( new GuardaCampo( txtCodPlanVDConsig, "CodPlan", "Cód.plan.", ListaCampos.DB_PK, txtDescPlanVDConsig, false ) );
		lcPlanVDConsig.add( new GuardaCampo( txtDescPlanVDConsig, "DescPlan", "Descriçao do planejamento", ListaCampos.DB_SI, false ) );
		lcPlanVDConsig.setWhereAdic( "TIPOPLAN = 'D'" );
		lcPlanVDConsig.montaSql( false, "PLANEJAMENTO", "FN" );
		lcPlanVDConsig.setReadOnly( true );

	}

	private void montaTela() {

		setPainel( pinGeral );
		adicTab( "Geral", pinGeral );

		// adicCampo( txtCodTipoMov, 7, 25, 75, 20, "CodTipoMovCo", "Cód.tp.mov.", ListaCampos.DB_FK, txtDescTipoMov, false );
		// adicDescFK( txtDescTipoMov, 85, 25, 250, 20, "DescTipoMov", "Tipo de movimento para venda consignada" );
		// adicCampo( txtCodTipoMovTv, 7, 70, 75, 20, "CodTipoMovTv", "Cód.tp.mov.", ListaCampos.DB_FK, txtDescTipoMovTv, false );
		// adicDescFK( txtDescTipoMovTv, 85, 70, 250, 20, "DescTipoMov", "Tipo de movimento para pedido de venda" );
		// adicCampo( txtCodTipoMovTp, 7, 110, 75, 20, "CodTipoMovTp", "Cód.tp.mov.", ListaCampos.DB_FK, txtDescTipoMovTp, false );
		// adicDescFK( txtDescTipoMovTp, 85, 110, 250, 20, "DescTipoMov", "Tipo de movimento para venda" );

		adicCampo( txtCodPlanConsig, 7, 25, 120, 20, "CodPlanConsig", "Cód.plan.remessa", ListaCampos.DB_FK, txtDescPlanConsig, false );
		adicDescFK( txtDescPlanConsig, 130, 25, 250, 20, "DescTipoMov", "Descrição do planejamento para remessa" );
		txtCodPlanConsig.setFK( true );
		txtCodPlanConsig.setNomeCampo( "CodPlan" );
		adicCampo( txtCodPlanVDConsig, 7, 70, 120, 20, "CodPlanVDConsig", "Cód.plan.vendas", ListaCampos.DB_FK, txtDescPlanVDConsig, false );
		adicDescFK( txtDescPlanVDConsig, 130, 70, 250, 20, "DescTipoMov", "Descrição do planejamento para vendas" );
		txtCodPlanVDConsig.setFK( true );
		txtCodPlanVDConsig.setNomeCampo( "CodPlan" );

		setListaCampos( false, "PREFERE7", "SG" );
		nav.setAtivo( 0, false );
		lcCampos.setPodeExc( false );
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcTipoMov.setConexao( cn );
		lcTipoMovTv.setConexao( cn );
		lcTipoMovTP.setConexao( cn );
		lcPlanConsig.setConexao( cn );
		lcPlanVDConsig.setConexao( cn );
		lcCampos.carregaDados();
	}
}
