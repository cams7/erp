/**
 * @version 30/01/2004 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: projetos.freedom.gms <BR>
 *         Classe: @(#)FAtribuicao.java <BR>
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
 *         Atribuições das pessoas que compôem o fluxo da RMA.
 * 
 */

package org.freedom.modulos.gms.view.dialog.utility;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Vector;

import javax.swing.JScrollPane;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;

public class DLImportacaoCompl extends FFDialogo implements ActionListener, PostListener, CarregaListener {
	
	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtID = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtDescAdic = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtVlrDespAdic = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 10, Aplicativo.casasDecPre );
	
	private JTextFieldFK txtSomatorio = new JTextFieldFK( JTextFieldFK.TP_DECIMAL, 10, Aplicativo.casasDecPre );
	
	private ListaCampos lcImportacaoCompl = new ListaCampos( this );
	
	private JPanelPad pnTabela = new JPanelPad( new BorderLayout() );
	
	private JTablePad tbImp = new JTablePad();
	
	private JScrollPane scrol = new JScrollPane( tbImp );
	
	private JButtonPad btAdic = new JButtonPad(Icone.novo( "btAdic.gif" ) );
	
	private JButtonPad btLimpa = new JButtonPad(Icone.novo( "btNada.png" ) );
	
	private JButtonPad btDeletaSelecionado = new JButtonPad(Icone.novo( "btExcluir.png" ) );
	
	Integer codemp = null;
	Integer codfilial = null;
	Integer codimp = null;
	
	public DLImportacaoCompl(Integer codemp, Integer codfilial, Integer codimp) {
		super();
		setTitulo( "Cadastro de atribuições" );
		setAtribos( 500, 280 );
		this.codemp = codemp;
		this.codfilial = codfilial;
		this.codimp = codimp;
		montaListaCampos();
		montaTela();
		addListener();

		
	}
	
	private void montaListaCampos(){
		
	/*	lcImportacaoCompl.add( new GuardaCampo( txtID, "ID", "Cód.Comp.", ListaCampos.DB_PK, null, false ) );
		lcImportacaoCompl.add( new GuardaCampo( txtDescAdic, "DESCADIC", "Doc.", ListaCampos.DB_SI, null, false ) );
		lcImportacaoCompl.add( new GuardaCampo( txtVlrDespAdic, "DESPADIC", "Serie", ListaCampos.DB_SI, null, false ) );

		txtID.setTabelaExterna( lcImportacaoCompl, null );
		txtID.setNomeCampo( "ID" );
		txtID.setFK( true );

		lcImportacaoCompl.setQueryCommit( false );
		lcImportacaoCompl.setReadOnly( true );

		txtID.setListaCampos( lcImportacaoCompl );
		lcImportacaoCompl.montaSql( false, "IMPORTACAOCOMPL", "CP" );*/
	}
	
	
	private void addListener(){
		btAdic.addActionListener( this );
		btAdic.setToolTipText( "Adiciona" );
		btLimpa.addActionListener( this );
		btDeletaSelecionado.setToolTipText( "Excluir" );
		btDeletaSelecionado.addActionListener( this );
		btLimpa.setToolTipText( "Exclui todos" );
	}
	
	
	private void montaTela(){
		
	//	adic( txtID, 7, 20, 70, 20, "ID" );
		adic( txtDescAdic, 7, 20, 230, 20, "DESCADIC");
		adic( txtVlrDespAdic, 240, 20, 100, 20, "VLRDESPADIC");
		
		adic( btAdic, 343, 15, 30, 30 );
		adic( btDeletaSelecionado, 400, 60, 30, 30 );
		adic( btLimpa, 400, 100, 30, 30 );
		adic( pnTabela, 7, 60, 390, 100  );
		adic( txtSomatorio, 297, 165, 100, 20 );
		
		
		tbImp.adicColuna( "Descrição adicional" );
		tbImp.adicColuna( "Vlr.desp.adic" );
		
		pnTabela.add( scrol, BorderLayout.CENTER );

		
		tbImp.setTamColuna( 280, 0 );
		tbImp.setTamColuna( 90, 1 );
		

	}
	
	
	
	private void deletaLinhaSelecionada() {
		if(tbImp.getSelectedRow() != -1){
			int linha = tbImp.getSelectedRow();
			txtSomatorio.setVlrBigDecimal( txtSomatorio.getVlrBigDecimal().subtract( (BigDecimal) tbImp.getValor( linha, 1 )));			
			
			tbImp.delLinha( linha );
		} else {
			Funcoes.mensagemInforma( this, "destaque não selecionado!" );
			txtDescAdic.requestFocus();
			return;
		}		
		
	}
	
	
	private void adicionaGrid() { 
		
		int colDescAdic = 0;
		int colVlrDespAdic = 1;
		
		int qtdLinhas = tbImp.getNumLinhas();
		
		if ( "".equals( txtDescAdic.getVlrString() ) ) {
			Funcoes.mensagemInforma( this, "Descrição não preenchido!" );
			txtDescAdic.requestFocus();
			return;
		}
		
		if ( txtVlrDespAdic.getVlrBigDecimal().compareTo( new BigDecimal(0) ) == 0) {
			Funcoes.mensagemInforma( this, "Valor não preenchido!" );
			txtVlrDespAdic.requestFocus();
			return;
		} else {
			if(txtSomatorio.getVlrBigDecimal().compareTo( new BigDecimal(0) ) == 0) {
				txtSomatorio.setVlrBigDecimal( txtVlrDespAdic.getVlrBigDecimal() );
			} else {
				txtSomatorio.setVlrBigDecimal( txtSomatorio.getVlrBigDecimal().add( txtVlrDespAdic.getVlrBigDecimal() ) );
			}
		}
		
		if(qtdLinhas > 0){
			String compare = null;
			boolean cadastrado = false;
			for(int i = 0; i < qtdLinhas; i++) {
				compare = tbImp.getValor( i, 0 ).toString();
				
				if(compare.equals( txtDescAdic.getVlrString() )) {
					cadastrado = true;
					break;
				}
				
			}
			
			if(cadastrado){
				Funcoes.mensagemInforma( this, "Descrição já adicionada!" );
				txtDescAdic.requestFocus();
				return;
			}
		}
		
		tbImp.adicLinha();
		
			tbImp.setValor( txtDescAdic.getVlrString(), qtdLinhas , colDescAdic );
			tbImp.setValor( txtVlrDespAdic.getVlrBigDecimal(), qtdLinhas , colVlrDespAdic );
			
	}
	
	

	private void limpaGrid() {

		tbImp.limpa();
		txtSomatorio.setVlrBigDecimal( new BigDecimal( 0  ) );
		
	}


	public void beforePost( PostEvent pevt ) {

	}

	public void afterCarrega( CarregaEvent cevt ) {
	}

	public void beforeCarrega( CarregaEvent cevt ) {

	}
	
	public Vector<Vector<Object>>  getDataVector(){
		return tbImp.getDataVector();
	}

	public void  setDataVector(Vector<Vector<Object>> dataVector ){
		tbImp.setDataVector( dataVector );
	}
	
	@Override
	public void actionPerformed( ActionEvent evt ) {
	
		if ( evt.getSource() == btAdic ) {
			adicionaGrid();
			txtDescAdic.requestFocus();
		} else if( evt.getSource() == btLimpa) {
			limpaGrid();
		} else if( evt.getSource() == btDeletaSelecionado) {
			deletaLinhaSelecionada();
		}
		
		super.actionPerformed( evt );
	}

	
	public void afterPost( PostEvent pevt ) {

		// TODO Auto-generated method stub
		
	}
	

	public void setConexao( DbConnection cn ) {
		super.setConexao( cn );
		
		lcImportacaoCompl.setConexao( cn );
	}

	public BigDecimal getVlrCompl() {
		if(txtSomatorio.getVlrBigDecimal() == null){
			return new BigDecimal( 0 );
		}
		return txtSomatorio.getVlrBigDecimal();
	}
	
	public void setVlrCompl(BigDecimal vlrComplTot) {
		txtSomatorio.setVlrBigDecimal(vlrComplTot);
	}
}
