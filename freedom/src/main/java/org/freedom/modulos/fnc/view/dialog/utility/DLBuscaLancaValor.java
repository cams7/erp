/**
 * @version 12/05/2011 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 * Projeto: Freedom <BR>
 * 
 * Pacote: org.freedom.modulos.fnc.view.dialog.utility <BR>
 * Classe:
 * @(#)DLBuscaLancaValor.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * Dialog para entrada de informações sobre busca de lançamentos financeiros por valor.
 */

package org.freedom.modulos.fnc.view.dialog.utility;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.fnc.library.swing.component.JTextFieldPlan;

public class DLBuscaLancaValor extends FFDialogo implements KeyListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPlan txtValor1 = new JTextFieldPlan( JTextFieldPad.TP_DECIMAL, 10, Aplicativo.casasDecFin );
	private JTextFieldPlan txtValor2 = new JTextFieldPlan( JTextFieldPad.TP_DECIMAL, 10, Aplicativo.casasDecFin );
	private JCheckBoxPad cbFiltraData = new JCheckBoxPad( "Filtrar data", true, false );
	
	public DLBuscaLancaValor( Component cOrig ) {

		super( cOrig );
		
		setTitulo( "Informe o valor para pesquisa" );
		
		setAtribos( 370, 140 );

		txtValor1.setRequerido( true );

		adic( txtValor1		, 7		, 20, 90, 20, "Valor inicial" );
		adic( txtValor2		, 100	, 20, 90, 20, "Valor final" );
		adic( cbFiltraData	, 193	, 20, 150, 20, "" );

		
		txtValor1.requestFocus();
		txtValor1.addKeyListener( this );
		
		cbFiltraData.setVlrBoolean( true );
		
	}

	public boolean getFiltroData() {
		return cbFiltraData.getVlrBoolean();
	}
	
	public BigDecimal getValor1() {

		return txtValor1.getVlrBigDecimal().floatValue()>0 ? txtValor1.getVlrBigDecimal() : null;
	}

	public BigDecimal getValor2() {

		return txtValor2.getVlrBigDecimal().floatValue()>0 ? txtValor2.getVlrBigDecimal() : null;
		
	}
	
	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			if ( getValor1() == null && getValor2() == null ) {
				Funcoes.mensagemInforma( this, "Informe ao menos o valor inicial para busca" );
				txtValor1.requestFocus();

			}
			else {
				super.actionPerformed( evt );
			}
		}
		else if ( evt.getSource() == btCancel ) {
			super.actionPerformed( evt );
		}

	}

	public void keyTyped( KeyEvent kevt ) {
		
		if(kevt.getSource() == txtValor2 && ( (kevt.getKeyChar() == KeyEvent.VK_ENTER ) || ( kevt.getKeyChar() == KeyEvent.VK_TAB )) ) {
			
			btOK.doClick();

		} 
		
	}


}
