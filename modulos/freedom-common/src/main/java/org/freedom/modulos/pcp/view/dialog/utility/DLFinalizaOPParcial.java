/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)DLDescontItVenda.java <BR>
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
 *         Comentários sobre a classe...
 */

package org.freedom.modulos.pcp.view.dialog.utility;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.Date;

import javax.swing.JScrollPane;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;

public class DLFinalizaOPParcial extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtQtdPrevOP = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 3 );

	private JTextFieldPad txtQtdEnt = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 3 );

	private JTextFieldPad txtDtEnt = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtHEnt = new JTextFieldPad( JTextFieldPad.TP_TIME, 5, 0 );
	
	private JTextAreaPad txaObsEnt = new JTextAreaPad();

	private JLabelPad lObsEnt = new JLabelPad( "Observações" );

	boolean bJust = false;

	public DLFinalizaOPParcial( Component cOrig, BigDecimal qtdprev ) {

		super( cOrig );

		setTitulo( "Finalização parcial da OP." );
		setAtribos( 250, 300 );

		txtQtdPrevOP.setVlrBigDecimal( qtdprev );
		txtQtdPrevOP.setAtivo( false );

		adic( txtQtdPrevOP, 7, 25, 110, 20, "Qtd. prevista" );
		adic( txtQtdEnt, 120, 25, 110, 20, "Qtd. produzida:" );

		adic( txtDtEnt, 7, 65, 110, 20, "Data" );
		adic( txtHEnt, 120, 65, 110, 20, "Hora" );

		adic( lObsEnt, 7, 85, 220, 20 );
		adic( new JScrollPane( txaObsEnt ), 7, 105, 222, 110 );
		
		txtDtEnt.setVlrDate( new Date() );
		txtHEnt.setVlrTime( new Date() );

	}

	public void focusGained( FocusEvent fevt ) {

	}

	public BigDecimal getQtdEnt() {

		return txtQtdEnt.getVlrBigDecimal();
	}

	public String getObs() {

		return txaObsEnt.getVlrString();
	}
	
	public Date getDtEnt() {

		return txtDtEnt.getVlrDate();
	}
	
	public String getHEnt() {

		return txtHEnt.getVlrString();
	}
	
	public void keyPressed( KeyEvent kevt ) {

		super.keyPressed( kevt );
	}

	public void ok() {
		
		BigDecimal qtdent = txtQtdEnt.getVlrBigDecimal();		
		BigDecimal qtdprev = txtQtdPrevOP.getVlrBigDecimal();
		
		if( ( qtdent!=null && qtdent.floatValue()>0) && qtdent.compareTo( qtdprev )<=0 ){
			
			super.ok();
			
		}
		else {			
			
			Funcoes.mensagemInforma( this, "A quantidade informada não é válida!\n pois é maior que a quantidade prevista ou nula." );
			
		}
		
	}
}
