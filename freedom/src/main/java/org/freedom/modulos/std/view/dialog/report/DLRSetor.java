/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)DLRSetor.java <BR>
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

package org.freedom.modulos.std.view.dialog.report;

import java.util.Vector;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.modulos.std.view.frame.crud.plain.FSetor;

public class DLRSetor extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JRadioGroup<?, ?> rgOrdem = null;

	private JCheckBoxPad cbRota = new JCheckBoxPad( "Imprimir clientes/rota", "S", "N" );
	
	private JLabelPad lbOrdem = new JLabelPad( "Ordenar por:" );

	private JRadioGroup<?, ?> rgTipo = null;
	
	private ListaCampos lcSetor = new ListaCampos( this, "SR" );

	private JTextFieldPad txtCodSetor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescSetor = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	
	public DLRSetor() {

		setTitulo( "Ordem do Relatório" );
		setAtribos( 300, 260 );

		/***************
		 * SETOR       *
		 **************/
		
		txtCodSetor.setNomeCampo( "codsetor" );
		lcSetor.add( new GuardaCampo( txtCodSetor, "CodSetor", "Cód.setor", ListaCampos.DB_PK, txtDescSetor, false ) );
		lcSetor.add( new GuardaCampo( txtDescSetor, "DescSetor", "Descrição do setor", ListaCampos.DB_SI, false ) );
		lcSetor.montaSql( false, "SETOR", "VD" );
		lcSetor.setReadOnly( true );
		lcSetor.setQueryCommit( false );
		txtCodSetor.setTabelaExterna( lcSetor, FSetor.class.getCanonicalName() );
		txtCodSetor.setListaCampos( lcSetor );
		txtDescSetor.setListaCampos( lcSetor );
		txtCodSetor.setFK( true );
		
		Vector<String> vLabs = new Vector<String>();
		Vector<String> vVals = new Vector<String>();

		vLabs.addElement( "Código" );
		vLabs.addElement( "Descrição" );
		vVals.addElement( "C" );
		vVals.addElement( "D" );
		rgOrdem = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgOrdem.setVlrString( "D" );
		adic( lbOrdem, 7, 0, 80, 15 );
		adic( rgOrdem, 7, 20, 270, 30 );

		Vector<String> vLabs1 = new Vector<String>();
		Vector<String> vVals1 = new Vector<String>();

		vVals1.addElement( "G" );
		vVals1.addElement( "T" );
		vLabs1.addElement( "Grafico" );
		vLabs1.addElement( "Texto" );
		rgTipo = new JRadioGroup<String, String>( 1, 2, vLabs1, vVals1 );
		rgTipo.setVlrString( "G" );
		
		cbRota.setVlrString( "S" );
		
		adic( rgTipo, 7, 60, 270, 30 );
		
		adic(cbRota,7, 100 , 270 , 20);
		
		adic( txtCodSetor, 7, 140, 70, 20, "Cód.setor" );
		adic( txtDescSetor, 80, 140, 195, 20, "Descrição do setor" );

	}

	public String getValor() {

		String sRetorno = "DESCSETOR";

		if ( rgOrdem.getVlrString().compareTo( "C" ) == 0 )
			sRetorno = "CODSETOR";

		return sRetorno;
	}

	public boolean isRota() {
		return "S".equals(cbRota.getVlrString());
	}
	
	public Integer getCodSetor() {
		return txtCodSetor.getVlrInteger();
	}
	
	public String getTipo() {

		return rgTipo.getVlrString();
	}
	
	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcSetor.setConexao( con );


	}

	
}
