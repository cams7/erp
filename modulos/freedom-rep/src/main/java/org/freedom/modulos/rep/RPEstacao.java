/**
 * @version 02/2007 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * @author Alex Rodrigues<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.rep <BR>
 *         Classe:
 * @(#)RPEstacao.java <BR>
 * 
 *                    Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                    modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                    na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                    Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                    sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                    Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                    Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                    de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                    Tela de cadastros de estações de trabalho.
 * 
 */

package org.freedom.modulos.rep;

import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;

public class RPEstacao extends FDados {

	private static final long serialVersionUID = 1L;

	private final JTextFieldPad txtCodEst = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldPad txtDescEst = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	public RPEstacao() {

		super( false );
		setTitulo( "Estação de trabalho" );
		setAtribos( 50, 50, 420, 140 );

		montaTela();
		setListaCampos( true, "ESTACAO", "SG" );
	}

	private void montaTela() {

		adicCampo( txtCodEst, 7, 30, 80, 20, "CodEst", "Cód.est.", ListaCampos.DB_PK, true );
		adicCampo( txtDescEst, 90, 30, 305, 20, "DescEst", "Descrição da estação de trabalho", ListaCampos.DB_SI, true );
	}
}
