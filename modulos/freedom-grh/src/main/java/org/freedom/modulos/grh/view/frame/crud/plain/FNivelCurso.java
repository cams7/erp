/**
 * @version 28/01/2008 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.grh <BR>
 *         Classe:
 * @(#)FNivelCurso.java <BR>
 * 
 *                      Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                      modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                      na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                      Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                      sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                      Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                      Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                      de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                      Formulário para cadastro de níveis de cursos, para uso nas funções de recrutamento e seleção.
 * 
 */

package org.freedom.modulos.grh.view.frame.crud.plain;

import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;

public class FNivelCurso extends FDados {

	private static final long serialVersionUID = 1L;

	private final JTextFieldPad txtCodNivelCurso = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private final JTextFieldPad txtDescNivelCurso = new JTextFieldPad( JTextFieldPad.TP_STRING, 60, 0 );

	public FNivelCurso() {

		super();

		nav.setNavigation( true );
		setTitulo( "Cadastro de níveis de cursos" );
		setAtribos( 50, 50, 380, 135 );

		montaTela();

		setImprimir( false );
	}

	private void montaTela() {

		adicCampo( txtCodNivelCurso, 7, 20, 70, 20, "CodNivelCurso", "Cód.Nível", ListaCampos.DB_PK, true );
		adicCampo( txtDescNivelCurso, 80, 20, 250, 20, "DescNivelCurso", "Descrição do nível", ListaCampos.DB_SI, true );
		setListaCampos( true, "NIVELCURSO", "RH" );
		lcCampos.setQueryInsert( false );
	}
}
