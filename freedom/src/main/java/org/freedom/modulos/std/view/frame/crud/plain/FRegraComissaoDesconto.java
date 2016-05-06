/**
 *  Projeto: Freedom <BR>
 * 
 *  Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *  modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *  na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *  Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *  sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *  Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *  Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *  escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA <BR>
 * 
 * @author ffrizzo
 */

package org.freedom.modulos.std.view.frame.crud.plain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;

public class FRegraComissaoDesconto extends FDados implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCod = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 50, 0 );
	private JTextFieldPad txtDescricao = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );
	private JTextFieldPad txtDesconto = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );
	private JTextFieldPad txtComissao = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	public FRegraComissaoDesconto() {

		super( false );
		setTitulo( "Regras de Comissionamento" );
		setAtribos( 50, 50, 500, 170 );

		adicCampo( txtCod, 7, 20, 80, 20, "CodRegComisDesc", "Cod", ListaCampos.DB_PK, null, true );
		adicCampo( txtDescricao, 100, 20, 320, 20, "DescRegComisDesc", "Descrição", ListaCampos.DB_SI, null, true );
		adicCampo( txtDesconto, 7, 60, 80, 20, "Desconto", "% Desconto", ListaCampos.DB_SI, null, true );
		adicCampo( txtComissao, 100, 60, 80, 20, "Comissao", "% Comissão", ListaCampos.DB_SI, null, true );

		setListaCampos( true, "REGCOMISDESC", "VD" );

		lcCampos.setQueryInsert( false );
	}

	public void actionPerformed( ActionEvent evt ) {
		super.actionPerformed( evt );
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
	}
	
}
