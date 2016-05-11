/**
 * @version 28/08/2012 <BR>
 * @author Setpoint Informática Ltda./Sérgio Murilo Macedo<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std.view.frame.crud.detail <BR>
 *         Classe: @(#)FCalcCusto.java <BR>
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
 *         Cadastro de custo fórmula para custo de aquisição.
 * 
 */

package org.freedom.modulos.std.view.frame.crud.detail;

import java.util.List;
import java.util.Vector;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDetalhe;
import org.freedom.modulos.std.business.constant.Impostos;

public class FCalcCusto extends FDetalhe implements CarregaListener, InsertListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCab = new JPanelPad();

	private JPanelPad pinDet = new JPanelPad();

	private JTextFieldPad txtCodCalc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDescCalc = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );
	
	private JTextFieldPad txtSeqItCalc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private Vector<String> vValsSigla = new Vector<String>();

	private Vector<String> vLabsSigla = new Vector<String>();
	
	private JTextFieldPad txtSiglaCalc = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtOperacaoCalc = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );
	
	private JComboBoxPad cbSiglaCalc = new JComboBoxPad( vLabsSigla, vValsSigla, JComboBoxPad.TP_STRING, 10, 0 );
	
	private Vector<String> vValsOperacao = new Vector<String>();

	private Vector<String> vLabsOperacao = new Vector<String>();
	
	private JComboBoxPad cbOperacaoCalc = new JComboBoxPad( vLabsOperacao, vValsOperacao, JComboBoxPad.TP_STRING, 1, 0 );

	public FCalcCusto() {

		setTitulo( "Custo de Aquisição" );
		setAtribos( 50, 20, 500, 400 );
		montaComboBox();
		montaTela();
	}
	
	public void montaTela(){
		
		setAltCab( 90 );
		pinCab = new JPanelPad( 500, 80 );
		setListaCampos( lcCampos );
		setPainel( pinCab, pnCliCab );
		nav.setNavigation( true );
		
		adicCampo( txtCodCalc, 7, 20, 70, 20, "CodCalc", "Cód.calc", ListaCampos.DB_PK, true );
		adicCampo( txtDescCalc, 80, 20, 250, 20, "DescCalc", "Descrição do custo de aquisição", ListaCampos.DB_SI, true );
		
		setListaCampos( true, "CALCCUSTO", "LF" );
		setAltDet( 80 );
		pinDet = new JPanelPad( 500, 80 );
		setPainel( pinDet, pnDet );
		setListaCampos( lcDet );
		setNavegador( navRod );
		
		/*
		adicCampoInvisivel( txtSiglaCalc, "SiglaCalc", "Sigla", ListaCampos.DB_FK, false);
		adicCampoInvisivel( txtOperacaoCalc, "OperacaoCalc", "Operação", ListaCampos.DB_FK, false);
		*/
		adicCampo( txtSeqItCalc, 7, 20, 70, 25, "SeqItCalc", "Seq.", ListaCampos.DB_PK, true );
		adicDB( cbSiglaCalc, 80, 20, 120, 25, "SiglaCalc", "Sigla", true);
		adicDB( cbOperacaoCalc, 203, 20, 200, 25, "OperacaoCalc", "Operação", true);
		
		/*
		adicCampo( txtCodItModG, 7, 20, 70, 20, "CodItModG", "Item", ListaCampos.DB_PK, true );
		adicCampo( txtCodVarG, 80, 20, 77, 20, "CodVarG", "Cód.var.g.", ListaCampos.DB_FK, true );
		adicDescFK( txtDescVarG, 160, 20, 197, 20, "DescVarG", "Descrição da variante" );
		adicCampo( txtDescItModG, 360, 20, 200, 20, "DescItModG", "Descrição", ListaCampos.DB_SI, true );
		adicCampo( txtRefItModG, 7, 60, 87, 20, "RefItModG", "Ref.inicial", ListaCampos.DB_SI, true );
		adicCampo( txtCodFabItModG, 100, 60, 87, 20, "CodFabItModG", "Cód.fab.inic.", ListaCampos.DB_SI, true );
		adicCampo( txtCodBarItModG, 190, 60, 100, 20, "CodBarItModG", "Cód.bar.inic.", ListaCampos.DB_SI, true );*/
		setListaCampos( true, "ITCALCCUSTO", "LF" );
		montaTab();
	}
	

	
	public void montaComboBox(){
		
		vValsSigla.addElement( "N" );
		vLabsSigla.addElement( "<Não Selecionado>" );
	
		List<Impostos> impostos = Impostos.getImpostos();
		for(Impostos imp : impostos){
			vValsSigla.addElement( imp.getValue() );
			vLabsSigla.addElement( imp.getValue() );
		}
		cbSiglaCalc.setItensGeneric( vLabsSigla, vValsSigla );
			
		vLabsOperacao.addElement( "<Não Selecionado>" );
		vLabsOperacao.addElement( "Adição" );
		vLabsOperacao.addElement( "Subtração" );
		vValsOperacao.addElement(  "T" );
		vValsOperacao.addElement(  "+" );
		vValsOperacao.addElement(  "-" );
		cbOperacaoCalc.setItensGeneric( vLabsOperacao, vValsOperacao );
		
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		//lcProd.setConexao( cn );
		//lcVarG.setConexao( cn );
		//txtCodProd.setBuscaAdic( new DLBuscaProd( con, "CODPROD", lcProd.getWhereAdic() ) );
	}

	public void beforeInsert( InsertEvent ievt ) {
	
	}

	public void afterInsert( InsertEvent ievt ) {
		if(ievt.getListaCampos() == lcDet) {
			cbSiglaCalc.limpa();
			cbOperacaoCalc.limpa();
		}	
	}

	public void beforeCarrega( CarregaEvent cevt ) {
		
	}

	public void afterCarrega( CarregaEvent cevt ) {
		 if( cevt.getListaCampos() == lcDet){
			 
				cbSiglaCalc.setVlrInteger(txtSiglaCalc.getVlrInteger());
				cbOperacaoCalc.setVlrInteger(txtOperacaoCalc.getVlrInteger());
				
			 }
	}
}
