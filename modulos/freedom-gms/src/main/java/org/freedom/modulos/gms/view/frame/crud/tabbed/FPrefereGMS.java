/**
 * @version 02/03/2009 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.gms.view.frame.crud.tabbed <BR> 
 *         Classe:
 * @(#)FPrefereGMS.java <BR>
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

package org.freedom.modulos.gms.view.frame.crud.tabbed;

import java.math.BigDecimal;

import javax.swing.JScrollPane;

import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.exceptions.ExceptionCarregaDados;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FTabDados;
import org.freedom.modulos.gms.business.object.TipoMov;
import org.freedom.modulos.gms.business.object.TipoRecMerc;
import org.freedom.modulos.gms.view.frame.crud.detail.FTipoExpedicao;
import org.freedom.modulos.gms.view.frame.crud.detail.FTipoRecMerc;
import org.freedom.modulos.std.view.frame.crud.tabbed.FTransp;

public class FPrefereGMS extends FTabDados implements InsertListener {

	private static final long serialVersionUID = 1L;

	/****************
	 * Lista Campos *
	 ****************/

	private ListaCampos lcTipoRecMercRP = new ListaCampos( this, "TR" );

	private ListaCampos lcTipoRecMercCM = new ListaCampos( this, "CM" );

	private ListaCampos lcTipoMovCP = new ListaCampos( this, "TC" );

	private ListaCampos lcTipoRecMercOS = new ListaCampos( this, "TO" );

	private ListaCampos lcTipoExped = new ListaCampos( this, "TE" );

	private ListaCampos lcTipoMovDS = new ListaCampos( this, "DS" );

	private ListaCampos lcProdServ = new ListaCampos( this, "SE" );

	private ListaCampos lcCodTran = new ListaCampos( this, "TN" );

	private ListaCampos lcCodPagPP = new ListaCampos( this, "PP" );

	private JCheckBoxPad cbUsaPrecoPecaServ = new JCheckBoxPad( "Usar preço da peça no orçamento de serviços", "S", "N" );

	private JCheckBoxPad cbSincTicket = new JCheckBoxPad( "Sincronizar sequência do ticket Recebimento/Expedição", "S", "N" );

	private JCheckBoxPad cbSolCpHomologFor = new JCheckBoxPad( "Cotar com fornecedor homologado", "S", "N" );

	private JCheckBoxPad cbUtilRendaCot = new JCheckBoxPad( "Utiliza renda na cotação", "S", "N" );
	
	private JCheckBoxPad cbDetItemPainel = new JCheckBoxPad( "Detalhar item no Painel de controle de serviço", "S", "N" );

	private JTextAreaPad txaObsPadOC = new JTextAreaPad();

	private JScrollPane spnObsPadOC = new JScrollPane( txaObsPadOC );

	/****************
	 * Fields *
	 ****************/

	private JTextFieldPad txtCodTipoRecMercRP = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoRecMercRP = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodTipoExped = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoExped = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodTipoRecMercCM = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoRecMercCM = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodTipoMovTC = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoMovTC = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodTipoRecMercOS = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoRecMercOS = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodTipoMovDS = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoMovDS = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodTran = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeTran = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodPlanoPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescPlanoPag = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );
	
	private JTextFieldPad txtPercPrecoColetaCP = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, 2 );

	private JCheckBoxPad cbPermitDocColDupl = new JCheckBoxPad( "Permite número de documento duplicado", "S", "N" );

	/****************
	 * Paineis *
	 ****************/

	private JPanelPad pinGeral = new JPanelPad();

	private JPanelPad pinOS = new JPanelPad();

	private JPanelPad pinCompra = new JPanelPad();

	private JPanelPad pinColeta = new JPanelPad();

	private JTextFieldPad txtCodProdServ = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtRefProdServ = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldFK txtDescProdServ = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	public FPrefereGMS() {

		super();

		setTitulo( "Preferências GMS" );
		setAtribos( 30, 40, 440, 320 );
		lcCampos.setMensInserir( false );

		montaListaCampos();
		montaTela();

	}

	private void montaListaCampos() {

		/***************************************
		 * Tipo Rec. Mercadoria Com pesagem *
		 **************************************/

		lcTipoRecMercRP.add( new GuardaCampo( txtCodTipoRecMercRP, "CodTipoRecMerc", "Cód.Tipo.Rec.", ListaCampos.DB_PK, false ) );
		lcTipoRecMercRP.add( new GuardaCampo( txtDescTipoRecMercRP, "DescTipoRecMerc", "Tipo de recebimento para pesagem", ListaCampos.DB_SI, false ) );
		lcTipoRecMercRP.setWhereAdic( "TIPORECMERC='" + TipoRecMerc.TIPO_RECEBIMENTO_PESAGEM.getValue() + "'" );
		lcTipoRecMercRP.montaSql( false, "TIPORECMERC", "EQ" );
		lcTipoRecMercRP.setQueryCommit( false );
		lcTipoRecMercRP.setReadOnly( true );
		txtCodTipoRecMercRP.setTabelaExterna( lcTipoRecMercRP, FTipoRecMerc.class.getCanonicalName() );

		/***************************************
		 * Tipo Rec. Mercadoria Coleta *
		 **************************************/

		lcTipoRecMercCM.add( new GuardaCampo( txtCodTipoRecMercCM, "CodTipoRecMerc", "Cód.Tipo.Rec.", ListaCampos.DB_PK, false ) );
		lcTipoRecMercCM.add( new GuardaCampo( txtDescTipoRecMercCM, "DescTipoRecMerc", "Tipo de recebimento com coleta", ListaCampos.DB_SI, false ) );
		lcTipoRecMercCM.setWhereAdic( "TIPORECMERC='" + TipoRecMerc.TIPO_COLETA_DE_MATERIAIS.getValue() + "'" );
		lcTipoRecMercCM.montaSql( false, "TIPORECMERC", "EQ" );
		lcTipoRecMercCM.setQueryCommit( false );
		lcTipoRecMercCM.setReadOnly( true );
		txtCodTipoRecMercCM.setTabelaExterna( lcTipoRecMercCM, FTipoRecMerc.class.getCanonicalName() );

		/***************************************
		 * Tipo de movimento de compra *
		 **************************************/

		lcTipoMovCP.add( new GuardaCampo( txtCodTipoMovTC, "CodTipoMov", "Cód.Tipo.Rec.", ListaCampos.DB_PK, false ) );
		lcTipoMovCP.add( new GuardaCampo( txtDescTipoMovTC, "DescTipoMov", "Tipo de movimento para compra", ListaCampos.DB_SI, false ) );
		lcTipoMovCP.setWhereAdic( "ESTIPOMOV='" + TipoMov.ENTRADA.getValue() + "'" );
		lcTipoMovCP.montaSql( false, "TIPOMOV", "EQ" );
		lcTipoMovCP.setQueryCommit( false );
		lcTipoMovCP.setReadOnly( true );
		txtCodTipoMovTC.setTabelaExterna( lcTipoMovCP, FTipoMov.class.getCanonicalName() );

		/***************************************
		 * Tipo de recepcao de mercadoria para ordem de sevico *
		 **************************************/

		lcTipoRecMercOS.add( new GuardaCampo( txtCodTipoRecMercOS, "CodTipoRecMerc", "Cód.Tipo.Rec.", ListaCampos.DB_PK, false ) );
		lcTipoRecMercOS.add( new GuardaCampo( txtDescTipoRecMercOS, "DescTipoRecMerc", "Tipo de recepção e mercadorias para Ordem de Serviço", ListaCampos.DB_SI, false ) );
		lcTipoRecMercCM.setWhereAdic( "TIPORECMERC='" + TipoRecMerc.TIPO_ENTRADA_CONCERTO.getValue() + "'" );
		lcTipoRecMercOS.montaSql( false, "TIPORECMERC", "EQ" );
		lcTipoRecMercOS.setQueryCommit( false );
		lcTipoRecMercOS.setReadOnly( true );
		txtCodTipoRecMercOS.setTabelaExterna( lcTipoRecMercOS, FTipoRecMerc.class.getCanonicalName() );

		/*****************************************************************
		 * Tipo de movimento para devolução de peças consertadas serviço *
		 *****************************************************************/

		lcTipoMovDS.add( new GuardaCampo( txtCodTipoMovDS, "CodTipoMov", "Cód.Tipo.Mov.", ListaCampos.DB_PK, false ) );
		lcTipoMovDS.add( new GuardaCampo( txtDescTipoMovDS, "DescTipoMov", "Tipo de movimento para devolução de conserto", ListaCampos.DB_SI, false ) );
		lcTipoMovDS.setWhereAdic( "ESTIPOMOV='" + TipoMov.SAIDA.getValue() + "'" );
		lcTipoMovDS.montaSql( false, "TIPOMOV", "EQ" );
		lcTipoMovDS.setQueryCommit( false );
		lcTipoMovDS.setReadOnly( true );
		txtCodTipoMovDS.setTabelaExterna( lcTipoMovDS, FTipoMov.class.getCanonicalName() );

		/*****************************************************************
		 * Serviço padrão *
		 *****************************************************************/
		lcProdServ.add( new GuardaCampo( txtCodProdServ, "Codprod", "Cód.prod.", ListaCampos.DB_PK, txtDescProdServ, false ) );
		lcProdServ.add( new GuardaCampo( txtRefProdServ, "refprod", "referencia", ListaCampos.DB_SI, false ) );
		lcProdServ.add( new GuardaCampo( txtDescProdServ, "Descprod", "Descriçao do produto", ListaCampos.DB_SI, false ) );
		lcProdServ.setWhereAdic( "TIPOPROD='S'" );
		lcProdServ.montaSql( false, "PRODUTO", "EQ" );
		lcProdServ.setQueryCommit( false );
		lcProdServ.setReadOnly( true );
		txtCodProdServ.setTabelaExterna( lcProdServ, FProduto.class.getCanonicalName() );
		txtCodProdServ.setNomeCampo( "codprod" );

		/******************************************
		 * Tipo de expedição de produtos acabados *
		 ******************************************/

		lcTipoExped.add( new GuardaCampo( txtCodTipoExped, "CodTipoExped", "Cód.Tipo.Exp.", ListaCampos.DB_PK, false ) );
		lcTipoExped.add( new GuardaCampo( txtDescTipoExped, "DescTipoExped", "Tipo de expedição de produtos", ListaCampos.DB_SI, false ) );
		lcTipoExped.montaSql( false, "TIPOEXPEDICAO", "EQ" );
		lcTipoExped.setQueryCommit( false );
		lcTipoExped.setReadOnly( true );
		txtCodTipoExped.setTabelaExterna( lcTipoExped, FTipoExpedicao.class.getCanonicalName() );

		/***************************************
		 * Código da transportadora padão *
		 **************************************/

		lcCodTran.add( new GuardaCampo( txtCodTran, "CodTran", "Cód.Tran.", ListaCampos.DB_PK, false ) );
		lcCodTran.add( new GuardaCampo( txtNomeTran, "NomeTran", "Código da transportadora padão", ListaCampos.DB_SI, false ) );
		lcCodTran.montaSql( false, "TRANSP", "VD" );
		lcCodTran.setQueryCommit( false );
		lcCodTran.setReadOnly( true );
		txtCodTran.setTabelaExterna( lcCodTran, FTransp.class.getCanonicalName() );

		/***************************************
		 * Código do plano de pagamento padrão para coleta de entrada *
		 **************************************/

		lcCodPagPP.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.Plano.Pag.", ListaCampos.DB_PK, false ) );
		lcCodPagPP.add( new GuardaCampo( txtDescPlanoPag, "DescPlanoPag", "Código do plano de pagamento padrão para coleta de entrada", ListaCampos.DB_SI, false ) );
		lcCodPagPP.setWhereAdic( "ATIVOPLANOPAG='S' AND CVPLANOPAG IN ('C' , 'A') " );
		lcCodPagPP.montaSql( false, "PLANOPAG", "FN" );
		lcCodPagPP.setQueryCommit( false );
		lcCodPagPP.setReadOnly( true );
		txtCodPlanoPag.setTabelaExterna( lcCodPagPP, FTipoMov.class.getCanonicalName() );

		lcCampos.addInsertListener( this );

	}

	private void montaTela() {

		setPainel( pinGeral );
		adicTab( "Geral", pinGeral );

		adicCampo( txtCodTipoRecMercRP, 7, 20, 70, 20, "CodTipoRecMerc", "Cód.Tp.Rec.", ListaCampos.DB_FK, txtDescTipoRecMercRP, false );
		adicDescFK( txtDescTipoRecMercRP, 80, 20, 330, 20, "DescTipoRecMerc", "Tipo de recebimento padrão para pesagem" );
		txtCodTipoRecMercRP.setFK( true );
		txtCodTipoRecMercRP.setNomeCampo( "CodTipoRecMerc" );

		adicCampo( txtCodTipoMovTC, 7, 60, 70, 20, "CodTipoMovTC", "Cód.Tp.Mov.", ListaCampos.DB_FK, txtDescTipoMovTC, false );
		adicDescFK( txtDescTipoMovTC, 80, 60, 330, 20, "DescTipoMov", "Tipo de movimento padrão para compra" );
		txtCodTipoMovTC.setFK( true );
		txtCodTipoMovTC.setNomeCampo( "CodTipoMov" );

		adicCampo( txtCodTipoExped, 7, 100, 70, 20, "CodTipoExped", "Cód.Tp.Exp.", ListaCampos.DB_FK, txtDescTipoExped, false );
		adicDescFK( txtDescTipoExped, 80, 100, 330, 20, "DescTipoExped", "Tipo de expedição padrão" );
		txtCodTipoExped.setFK( true );
		txtCodTipoExped.setNomeCampo( "CodTipoExped" );

		adicDB( cbSincTicket, 7, 130, 380, 20, "sincticket", "", false );
		adicDB( cbDetItemPainel, 7, 150, 380, 20, "detItemPainelServ", "", false );
		
		setPainel( pinOS );
		adicTab( "Ordem de serviço", pinOS );

		adicCampo( txtCodTipoRecMercOS, 7, 20, 70, 20, "CodTipoRecMercOS", "Cód.Tp.Rec.", ListaCampos.DB_FK, txtDescTipoRecMercOS, false );
		adicDescFK( txtDescTipoRecMercOS, 80, 20, 330, 20, "DescTipoRecMerc", "Tipo de recebimento padrão para Ordem de Serviço" );
		txtCodTipoRecMercOS.setFK( true );
		txtCodTipoRecMercOS.setNomeCampo( "CodTipoRecMerc" );

		adicCampo( txtCodTipoMovDS, 7, 60, 70, 20, "CodTipoMovDS", "Cód.Tp.Mov.", ListaCampos.DB_FK, txtDescTipoMovDS, false );
		adicDescFK( txtDescTipoMovDS, 80, 60, 330, 20, "DescTipoMov", "Tipo de movimento para devolução de conserto" );
		txtCodTipoMovDS.setFK( true );
		txtCodTipoMovDS.setNomeCampo( "CodTipoMov" );

		adicCampo( txtCodProdServ, 7, 100, 70, 20, "CodProdSe", "Cód.Serv.", ListaCampos.DB_FK, txtDescProdServ, false );
		adicDescFK( txtDescProdServ, 80, 100, 330, 20, "DescProd", "Descrição do serviço padrão" );
		txtCodProdServ.setFK( true );
		txtCodProdServ.setNomeCampo( "CodProd" );

		adicDB( cbUsaPrecoPecaServ, 7, 130, 350, 20, "usaprecopecaserv", "", false );

		setPainel( pinCompra );
		adicTab( "Compra", pinCompra );

		adicDB( cbSolCpHomologFor, 7, 10, 380, 20, "SolCpHomologFor", "", false );
		adicDB( cbUtilRendaCot, 7, 33, 380, 20, "UtilRendaCot", "", false );

		setPainel( pinColeta );
		adicTab( "Coleta", pinColeta );

		adicDBLiv( txaObsPadOC, "ObsPadOC", "Observações", false );

		adicTab( "Observação padrão O.C.", spnObsPadOC );

		adicCampo( txtCodTipoRecMercCM, 7, 20, 70, 20, "CodTipoRecMercCM", "Cód.Tp.Rec.", ListaCampos.DB_FK, txtDescTipoRecMercCM, false );
		adicDescFK( txtDescTipoRecMercCM, 80, 20, 330, 20, "DescTipoRecMercCM", "Tipo de recebimento padrão para coleta" );
		txtCodTipoRecMercCM.setFK( true );
		txtCodTipoRecMercCM.setNomeCampo( "CodTipoRecMerc" );

		adicCampo( txtCodTran, 7, 60, 70, 20, "CodTran", "Cód.Tran.", ListaCampos.DB_FK, txtNomeTran, false );
		adicDescFK( txtNomeTran, 80, 60, 330, 20, "NomeTran", "Nome da Transportadora" );
		
		txtCodTran.setFK( true );
		txtCodTran.setNomeCampo( "CodTran" );

		adicCampo( txtCodPlanoPag, 7, 100, 70, 20, "CodPlanoPag", "Cód.Plano.Pag.", ListaCampos.DB_FK, txtDescPlanoPag, false );
		adicDescFK( txtDescPlanoPag, 80, 100, 330, 20, "DescPlanoPag", "Código do plano de pagamento padrão para coleta de entrada" );
		txtCodPlanoPag.setFK( true );
		txtCodPlanoPag.setNomeCampo( "CodPlanoPag" );
		
		adicCampo(txtPercPrecoColetaCP, 7, 140, 160, 20,"PercPrecoColetaCP", "% preço para gerar compra", ListaCampos.DB_SI, false);	

		adicDB( cbPermitDocColDupl, 7, 160, 330, 20, "PermitDocColDupl", "", true );
		
		setListaCampos( false, "PREFERE8", "SG" );
		nav.setAtivo( 0, false );
		lcCampos.setPodeExc( false );
	}

	public void setConexao( DbConnection cn ) { // throws ExceptionSetConexao {

		super.setConexao( cn );

		lcTipoRecMercRP.setConexao( cn );
		lcTipoRecMercCM.setConexao( cn );
		lcTipoRecMercOS.setConexao( cn );
		lcTipoMovCP.setConexao( cn );
		lcTipoMovDS.setConexao( cn );
		lcProdServ.setConexao( cn );
		lcTipoExped.setConexao( cn );
		lcCodTran.setConexao( cn );
		lcCodPagPP.setConexao( cn );

		try {
			lcCampos.carregaDados();
		}

		catch ( Exception e ) {
			new ExceptionCarregaDados( "Erro ao carregar dados do lista campos " + lcCampos.getName() );
		}

	}

	public void afterInsert( InsertEvent ievt ) {

		if ( ievt.getListaCampos() == lcCampos ) {
			cbSolCpHomologFor.setVlrString( "N" );
			cbUtilRendaCot.setVlrString( "S" );
			txtPercPrecoColetaCP.setVlrBigDecimal( new BigDecimal( "100" ) );
		}

	}

	public void beforeInsert( InsertEvent ievt ) {

		// TODO Auto-generated method stub

	}
}
