/**
 * @version 29/08/2011 <BR>
 * @author Setpoint Informática Ltda./Bruno Nascimento <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)DLEditQtd.java <BR>
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
 *         Classe que permite a edição da quantidade no Orçamento.
 */

package org.freedom.modulos.std.view.dialog.utility;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.math.MathContext;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;

public class DLEditQtd extends FFDialogo {
	
	private static final long serialVersionUID = 1L;
	
	private JTextFieldFK txtCoditorc = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtCodProd = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldFK  txtQtditorc = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, 2 );
	
	private JTextFieldPad txtQtdAFatItorc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );
	
	private JTextFieldFK txtQtdFatItorc = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JLabelPad lbCoditorc = new JLabelPad( "Item" );
	
	private JLabelPad lbCodProd = new JLabelPad( "Cód.Prod" );

	private JLabelPad lbDescProd = new JLabelPad( "Descrição do produto" );

	private JLabelPad lbQtd = new JLabelPad( "Qtd" );

	private JLabelPad lbQtdAFat = new JLabelPad( "Qtd a faturar" );
	
	private JLabelPad lbQtdFat = new JLabelPad( "Qtd faturada" );
	
	private JPanelPad panelGeral = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );
	
	private JPanelPad pinCab = new JPanelPad( 0, 80 );

	private boolean[] prefs;
	
	private int coditorc;
	private int codprod;
	private String descprod;
	private BigDecimal qtditorc;
	private BigDecimal qtdafatitorc;
	private BigDecimal qtdfatitorc;
	
	public DLEditQtd( int coditorc, int codprod, String descprod, BigDecimal qtditorc, BigDecimal qtdafatitorc, BigDecimal qtdfatitorc ) {
		super();
		setCoditorc( coditorc );
		setCodprod( codprod );
		setDescprod( descprod );
		setQtditorc( qtditorc );
		setQtdafatitorc( qtdafatitorc );
		setQtdfatitorc( qtdfatitorc );
		setTitulo( "Editar quantidade", this.getClass().getName() );
		setAtribos( 475, 225 );
		setResizable( true );
		montaTela();
	}
	

	public void montaTela(){
		
		setPanel( panelGeral );
		panelGeral.add( pinCab );
			

		pinCab.adic( lbCoditorc, 7, 5, 80, 20 );
		pinCab.adic( txtCoditorc, 7, 25, 80, 20 );
		pinCab.adic( lbCodProd, 90, 5, 80, 20 );
		pinCab.adic( txtCodProd, 90, 25, 80, 20 );
		pinCab.adic( lbDescProd, 173, 5, 250, 20 );
		pinCab.adic( txtDescProd, 173, 25, 250, 20 );
		
		pinCab.adic( lbQtd, 7, 65, 80, 20 );
		pinCab.adic( txtQtditorc, 7, 85, 80, 20 );
		pinCab.adic( lbQtdAFat, 175, 65, 80, 20 );
		pinCab.adic( txtQtdAFatItorc, 175, 85, 80, 20 );
		pinCab.adic( lbQtdFat, 343, 65, 80, 20 );
		pinCab.adic( txtQtdFatItorc, 343, 85, 80, 20 );
		
		txtCoditorc.setVlrInteger( coditorc );
		txtCodProd.setVlrInteger( codprod );
		txtDescProd.setVlrString( descprod );
		txtQtditorc.setVlrBigDecimal( qtditorc );
		txtQtdAFatItorc.setVlrBigDecimal( qtdafatitorc );
		txtQtdFatItorc.setVlrBigDecimal( qtdfatitorc );
		
	}

	
	public void actionPerformed( ActionEvent evt ) {
			
	
		
		if ( evt.getSource() == btOK ){
			setQtdafatitorc( txtQtdAFatItorc.getVlrBigDecimal() );
            if ( ! consisteForm() ) {
            	return;
            }
		}
		super.actionPerformed( evt );
	}  	

			
	private boolean consisteForm(){
		boolean result = true;

		if(	txtQtdAFatItorc.getVlrBigDecimal().compareTo( new BigDecimal( 0) ) <= 0 ) {
			Funcoes.mensagemInforma( this, "Informe um valor maior que 0 !" );
			result = false;
		}
		else if( txtQtdAFatItorc.getVlrBigDecimal().compareTo( 
				txtQtditorc.getVlrBigDecimal().subtract( txtQtdFatItorc.getVlrBigDecimal(), 
						(new MathContext( Aplicativo.casasDec )) ) ) > 0   ) {
			Funcoes.mensagemInforma( this, "Quantidade a faturar selecionada maior que o saldo !" );
			result = false;
		}

		return result;
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		montaTela();
	}
	
	public void keyPressed( KeyEvent kevt ) {

		if ( kevt.getSource() == btOK ) {
			btOK.doClick();
		}

		super.keyPressed( kevt );

	}
	
	public int getCoditorc() {
		
		return coditorc;
	}

	
	public void setCoditorc( int coditorc ) {
	
		this.coditorc = coditorc;
	}

	
	public String getDescprod() {
	
		return descprod;
	}

	
	public void setDescprod( String descprod ) {
	
		this.descprod = descprod;
	}

	
	public BigDecimal getQtditorc() {
	
		return qtditorc;
	}

	
	public void setQtditorc( BigDecimal qtditorc ) {
	
		this.qtditorc = qtditorc;
	}

	
	public BigDecimal getQtdafatitorc() {
	
		return qtdafatitorc;
	}

	
	public void setQtdafatitorc( BigDecimal qtdafatitorc ) {
	
		this.qtdafatitorc = qtdafatitorc;
	}

	
	public BigDecimal getQtdfatitorc() {
	
		return qtdfatitorc;
	}

	
	public void setQtdfatitorc( BigDecimal qtdfatitorc ) {
	
		this.qtdfatitorc = qtdfatitorc;
	}

	
	public int getCodprod() {
	
		return codprod;
	}

	
	public void setCodprod( int codprod ) {
	
		this.codprod = codprod;
	}
	
}
