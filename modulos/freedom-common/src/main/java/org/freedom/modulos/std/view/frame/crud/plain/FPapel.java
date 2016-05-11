/**
 * @version 11/02/2002 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FPapel.java <BR>
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

package org.freedom.modulos.std.view.frame.crud.plain;

import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;

public class FPapel extends FDados {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodPapel = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtDescPapel = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtLinPapel = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtAltPapel = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtLargPapel = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtColPapel = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtClassNotaPapel = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	public FPapel() {

		super();
		// Remove o painel de impressão da classe FDados:
		pnRodape.remove( 2 );
		// Monta a tela:
		setTitulo( "Cadastro de tipos de papeis" );
		setAtribos( 60, 60, 444, 220 );

		adicCampo( txtCodPapel, 7, 20, 80, 20, "CodPapel", "Cód.papel", ListaCampos.DB_PK, true );
		adicCampo( txtDescPapel, 90, 20, 287, 20, "DescPapel", "Descrição do papel", ListaCampos.DB_SI, true );
		adicCampo( txtLinPapel, 7, 60, 95, 20, "LinPapel", "Num. linhas", ListaCampos.DB_SI, true );
		adicCampo( txtColPapel, 105, 60, 87, 20, "Colpapel", "Num. colunas", ListaCampos.DB_SI, true ); // LOM
		adicCampo( txtAltPapel, 194, 60, 92, 20, "AltPapel", "Altura", ListaCampos.DB_SI, true );
		adicCampo( txtLargPapel, 289, 60, 87, 20, "LargPapel", "Largura", ListaCampos.DB_SI, true );
		adicCampo( txtClassNotaPapel, 7, 100, 370, 20, "ClassNotaPapel", "Classe de layout de NF", ListaCampos.DB_SI, false );

		setListaCampos( false, "PAPEL", "SG" );
		lcCampos.setQueryInsert( false );
	}
}
