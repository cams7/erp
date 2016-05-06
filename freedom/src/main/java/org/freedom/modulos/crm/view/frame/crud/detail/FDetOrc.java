/**
 * @version 12/08/2011 <BR>
 * @author Setpoint Informática Ltda./Bruno Nascimento<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.atd <BR>
 *         Classe: @(#)FDetOrc.java <BR>
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
 *         Detalhamento de Orçamento.
 * 
 */

package org.freedom.modulos.crm.view.frame.crud.detail;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JScrollPane;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDetalhe;
import org.freedom.modulos.std.view.frame.crud.detail.FOrcamento;
import org.freedom.modulos.std.view.frame.crud.detail.FPlanoPag;
import org.freedom.modulos.std.view.frame.crud.plain.FCLComis;
import org.freedom.modulos.std.view.frame.crud.tabbed.FVendedor;




public class FDetOrc extends FDetalhe implements ActionListener, CarregaListener {

	private static final long serialVersionUID = 1L;
	
	//Campos puxado da classe FOrcamento
	private JTextFieldPad txtCodOrc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtCodGo = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtDtOrc = new JTextFieldFK( JTextFieldFK.TP_DATE, 10, 0 );
	
	private JTextFieldFK txtDtVencOrc = new JTextFieldFK( JTextFieldFK.TP_DATE, 10, 0 );
	
	private JTextFieldFK txtCodCli = new JTextFieldFK(JTextFieldFK.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtPrazoEntOrc = new JTextFieldFK( JTextFieldFK.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtCodPlanoPag = new JTextFieldFK( JTextFieldFK.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtCodVend = new JTextFieldFK( JTextFieldFK.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );
	
	private JTextFieldFK txtDescPlanoPag = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );
	
	private JTextFieldFK txtNomeVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldFK txtDescItGO = new JTextFieldFK( JTextFieldPad.TP_STRING, 100, 0 );
	
	//Classe FDetOrc
		
	private JTextFieldPad txtTitDetOrc = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextAreaPad txtAtivDetOrc = new JTextAreaPad( 10000 );

	private JTextFieldPad txtSeqDetOrc = new JTextFieldPad( JTextFieldPad.TP_INTEGER , 8, 0 );

	private JTextAreaPad txtTextoItDetOrc = new JTextAreaPad( 10000 );
	
    private JTextFieldPad txtCodGO = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

    private JTextFieldPad txtSeqItGo = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );
	
	private JScrollPane jspDetTexto = new JScrollPane( txtTextoItDetOrc );
	
	private JScrollPane jspDetAtiv = new JScrollPane( txtAtivDetOrc );
	
	private JCheckBoxPad cbItensDetOrc = new JCheckBoxPad( "Itens da Proposta", "S", "N" );

	private ListaCampos lcItGrupOrc = new ListaCampos( this, "GO" ) ;
	
	private ListaCampos lcOrc = new ListaCampos( this );
	
	private ListaCampos lcCli = new ListaCampos( this );
	
	private ListaCampos lcVend = new ListaCampos( this );
	
	private ListaCampos lcPlanoPag = new ListaCampos( this );
	
	private JPanelPad pinCab = new JPanelPad(); 

	private JPanelPad pinDet = new JPanelPad();
	
	
	//Lista Campos
	
	public FDetOrc() {

		super();

		nav.setNavigation( true );

		setTitulo( "Cadastro de Detalhamento de Orçamento" );
		setAtribos( 50, 50, 790, 490 );

		setAltCab( 190 );
		pinCab = new JPanelPad( 420, 90 );
		setListaCampos( lcCampos );
		setPainel( pinCab, pnCliCab );
		
	
		// FK Plano de pagamento
		lcPlanoPag.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_PK, false ) );
		lcPlanoPag.add( new GuardaCampo( txtDescPlanoPag, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPag.setWhereAdic( "ATIVOPLANOPAG='S' AND CVPLANOPAG IN ('V','A')" );
		txtCodPlanoPag.setTabelaExterna( lcPlanoPag, FPlanoPag.class.getCanonicalName() );
		txtDescPlanoPag.setListaCampos( lcPlanoPag );
		
		lcPlanoPag.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag.setQueryCommit( false );
		lcPlanoPag.setReadOnly( true );
		
		// FK Vendedor
		lcVend.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.comiss.", ListaCampos.DB_PK, false ) );
		lcVend.add( new GuardaCampo( txtNomeVend, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, false ) );

		txtCodVend.setTabelaExterna( lcVend, FVendedor.class.getCanonicalName() );
		txtNomeVend.setListaCampos( lcVend );
		lcVend.montaSql( false, "VENDEDOR", "VD" );
		
		//FK CLIENTE
		lcCli.add( new GuardaCampo(txtCodCli, "CodCli", "Cód.Cliente", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo(txtRazCli, "RazCli", "Razão do cliente", ListaCampos.DB_SI, false ) );
		
		lcCli.montaSql( false, "CLIENTE", "VD" );
		lcCli.setQueryCommit( false );
		lcCli.setReadOnly( true );
		txtCodCli.setTabelaExterna( lcCli, FCLComis.class.getCanonicalName() );
	
		//FK ORÇAMENTO
		lcOrc.add( new GuardaCampo( txtCodOrc, "CodOrc", "Cód.Orç.", ListaCampos.DB_PK, false ) );
		lcOrc.add( new GuardaCampo( txtDtOrc, "DtOrc", "Data", ListaCampos.DB_SI, false ) );
		lcOrc.add( new GuardaCampo(txtCodCli, "CodCli", "Cód.Cliente", ListaCampos.DB_FK, txtRazCli ,false ) );
		lcOrc.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.comiss.", ListaCampos.DB_FK, txtNomeVend, false ) );
		lcOrc.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.p.pg.", ListaCampos.DB_FK,  txtDescPlanoPag, false ) );
		lcOrc.add( new GuardaCampo( txtPrazoEntOrc, "PrazoEntOrc", "Dias p/ entrega", ListaCampos.DB_SI, false ) );
		lcOrc.add( new GuardaCampo( txtDtVencOrc, "DtVencOrc", "Dt.valid.", ListaCampos.DB_SI, false ) );
	
		lcOrc.montaSql( false, "ORCAMENTO", "VD" );
		lcOrc.setQueryCommit( false );
		lcOrc.setReadOnly( true );
		txtCodOrc.setTabelaExterna( lcOrc, FOrcamento.class.getCanonicalName() );
		
		
		//FK ITGRUPORC
		lcItGrupOrc.add( new GuardaCampo( txtCodGo, "CodGo", "Cód. Agrupamento", ListaCampos.DB_PK, false ) );
		lcItGrupOrc.add( new GuardaCampo( txtSeqItGo, "SeqItGO", "Seq.Grupo", ListaCampos.DB_PK, true ) );
		lcItGrupOrc.add( new GuardaCampo( txtDescItGO, "DescItGo", "Texto Detalhado", ListaCampos.DB_SI, false ) );
		lcItGrupOrc.montaSql( false, "ITGRUPORC", "VD" );
		lcItGrupOrc.setQueryCommit( false );
		lcItGrupOrc.setReadOnly( true );
		txtCodGo.setTabelaExterna( lcItGrupOrc, FGrupoOrc.class.getCanonicalName() );		
		txtSeqItGo.setTabelaExterna( lcItGrupOrc, FGrupoOrc.class.getCanonicalName() );		
		lcItGrupOrc.addCarregaListener( this );
		
		//Adiciona Campos.
	    //Campos Orçamento.
		adicCampo( txtCodOrc, 7, 20, 90, 20, "CodOrc", "Nº orçamento", ListaCampos.DB_PK, true );
		adicDescFK( txtDtOrc, 440, 20, 107, 20, "DtOrc", "Data" );
		adicDescFK( txtCodCli, 100, 20, 87, 20, "CodCli", "Cód.cli." );
		adicDescFK( txtRazCli, 190, 20, 247, 20, "RazCli", "Razão social do cliente" );
		adicDescFK( txtCodPlanoPag, 420, 60, 77, 20, "CodPlanoPag", "Cód.p.pg." );
		adicDescFK( txtDescPlanoPag, 500, 60, 240, 20, "DescPlanoPag", "Descrição do plano de pagamento" );
		adicDescFK( txtDtVencOrc, 550, 20, 87, 20, "DtVencOrc", "Dt.valid." );
		adicDescFK( txtPrazoEntOrc, 640, 20, 100, 20, "PrazoEntOrc", "Dias p/ entrega" );
		adicDescFK( txtCodVend, 7, 60, 90, 20, "CodVend", "Cód.comiss." );
		adicDescFK( txtNomeVend, 100, 60, 315, 20, "NomeVend", "Nome do comissionado" );
			
		//Campos FDetOrc
		adicCampo( txtTitDetOrc, 7, 100, 381, 20, "TITDETORC", "Título", ListaCampos.DB_SI, true );
		adicDBLiv( txtAtivDetOrc, "AtivDetOrc", "Resumo Ativadade", true );
		adic( jspDetAtiv, 395, 100, 347, 40, "Resumo Atividade" );
		setListaCampos( true, "DETORC", "VD" );

		setAltDet( 140 );
		setPainel( pinDet, pnDet );
		setListaCampos( lcDet );
		setNavegador( navRod );

		//Campos FITDETORC
		
		adicCampo( txtSeqDetOrc, 7, 20, 70, 20, "SeqDetOrc", "SeqDetOrc", ListaCampos.DB_PK, true );
		adicDBLiv( txtTextoItDetOrc, "TextoItDetOrc", "Texto Detalhado", true );
		adic( jspDetTexto, 232, 20, 400, 80, "Descrição do Item de Agrupamento" );
		adicDB( cbItensDetOrc,  7, 60, 180, 20, "ItensDetOrc", "", true );
        adicCampo( txtCodGO, 80, 20 , 70, 20, "CodGO", "Cód.Agrupamento", ListaCampos.DB_FK, txtDescItGO, true);
        adicCampo( txtSeqItGo, 155, 20, 70, 20, "SeqItGo", "Seq.Agrupamento", ListaCampos.DB_FK, txtDescItGO, true );
		adicDescFKInvisivel( txtDescItGO, "DescItGo", "Plano atendente." );
		setListaCampos( true, "ITDETORC", "VD" );
		
		montaTab();
		tab.setTamColuna( 70, 0 );
		tab.setTamColuna( 300, 1 );

	}
	

	public void actionPerformed( ActionEvent evt ) {

		super.actionPerformed( evt );
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcOrc.setConexao( cn );
		lcCli.setConexao( cn );
		lcVend.setConexao( cn );
		lcPlanoPag.setConexao( cn );
		lcItGrupOrc.setConexao( cn );
	}


	public void afterCarrega( CarregaEvent cevt ) {

		if (cevt.getListaCampos()==lcItGrupOrc) {
			if ( (lcDet.getStatus()==ListaCampos.LCS_INSERT) && ("".equals(txtTextoItDetOrc.getVlrString().trim())) ) {
				txtTextoItDetOrc.setVlrString( txtDescItGO.getVlrString() );
			}
		}
		
	}


	public void beforeCarrega( CarregaEvent cevt ) {

		// TODO Auto-generated method stub
		
	}

}
