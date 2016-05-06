/**
 * @version 20/04/2011 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.crm <BR>
 *         Classe: @(#)FQualificacao.java <BR>
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
 *         Tela de cadastro de qualificações de chamados.
 * 
 */

package org.freedom.modulos.crm.view.frame.crud.plain;

import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;

public class FAmbienteAval extends FDados {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodAmbAval= new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDescAmbAval = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldPad txtSiglaAmbAval = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );
	
	public FAmbienteAval() {

		super();

		nav.setNavigation( true );

		setTitulo( "Ambiente" );
		
		setAtribos( 50, 50, 480, 150);
		
		adicCampo( txtCodAmbAval, 7, 20, 100, 20, "CodAmbAval", "Cód.Amb.Aval.", ListaCampos.DB_PK, true );		
		adicCampo( txtDescAmbAval, 110, 20, 300, 20, "DescAmbAval", "Descrição do ambiente da avaliação", ListaCampos.DB_SI, true );
		adicCampo( txtSiglaAmbAval, 413, 20, 45, 20, "SiglaAmbAval", "Sigla", ListaCampos.DB_SI, true );
		
		setListaCampos( true, "AMBIENTEAVAL", "CR" );
		
	
		lcCampos.setQueryInsert( false );
		
	}

}
