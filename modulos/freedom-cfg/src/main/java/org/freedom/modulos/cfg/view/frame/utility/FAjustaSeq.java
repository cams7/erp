/**
 * @version 15/02/2005 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.cfg <BR>
 *         Classe: @(#)FAjustaSeq.java <BR>
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
 * 
 */

package org.freedom.modulos.cfg.view.frame.utility;

import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;

public class FAjustaSeq extends FDados {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtSgTab = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtNroSeq = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	public FAjustaSeq() {

		super();
		setTitulo( "Ajusta sequencia" );
		setAtribos( 50, 50, 350, 150 );

		adicCampo( txtSgTab, 7, 20, 50, 20, "SgTab", "Sigla", ListaCampos.DB_PK, true );
		adicCampo( txtNroSeq, 60, 20, 80, 20, "NroSeq", "Sequencia", ListaCampos.DB_SI, true );
		lcCampos.montaSql( false, "SEQUENCIA", "SG" );

	}

}
