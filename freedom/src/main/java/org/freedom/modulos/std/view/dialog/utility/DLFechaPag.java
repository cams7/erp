/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)DLFechaPag.java <BR>
 * 
 *                     Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                     modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                     na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                     Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                     sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                     Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                     Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                     de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                     Comentários sobre a classe...
 */

package org.freedom.modulos.std.view.dialog.utility;

import java.awt.Component;
import java.math.BigDecimal;
import java.util.Date;

import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;

public class DLFechaPag extends FFDialogo {

	private Component owner = null;

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtParcItPag = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JTextFieldPad txtDtVencItPag = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	public DLFechaPag( Component cOrig, BigDecimal bigParcItPag, Date dDtVencItPag ) {

		super( cOrig );

		setTitulo( "Parcela" );
		setAtribos( 250, 150 );

		txtParcItPag.setVlrBigDecimal( bigParcItPag );
		txtDtVencItPag.setVlrDate( dDtVencItPag );

		adic( new JLabelPad( "Valor" ), 7, 0, 100, 20 );
		adic( new JLabelPad( "Vencimento" ), 110, 0, 100, 20 );
		adic( txtParcItPag, 7, 20, 100, 20 );
		adic( txtDtVencItPag, 110, 20, 100, 20 );
	}

	public Object[] getValores() {

		Object[] oRetorno = new Object[ 2 ];
		oRetorno[ 0 ] = txtParcItPag.getVlrBigDecimal();
		oRetorno[ 1 ] = txtDtVencItPag.getVlrDate();
		return oRetorno;
	}
}
