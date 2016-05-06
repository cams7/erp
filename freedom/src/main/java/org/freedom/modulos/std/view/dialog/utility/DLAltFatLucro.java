/**
 * @version 01/09/2009 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)DLAltComisVenda.java <BR>
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

package org.freedom.modulos.std.view.dialog.utility;

import java.awt.Component;
import java.math.BigDecimal;

import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;

public class DLAltFatLucro extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtFatLucr = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private int iCodVenda = 0;

	public DLAltFatLucro( Component cOrig, BigDecimal fatLucro ) {

		super( cOrig );

		setTitulo( "Alteração de fator de lucratividade" );
		setAtribos( 250, 130 );

		if ( fatLucro != null ) {
			txtFatLucr.setVlrBigDecimal( fatLucro );
		}

		adic( new JLabelPad( "Fator" ), 7, 0, 133, 20 );
		adic( txtFatLucr, 7, 20, 140, 20 );

	}

	public BigDecimal getValor() {

		return txtFatLucr.getVlrBigDecimal();
	}
}
