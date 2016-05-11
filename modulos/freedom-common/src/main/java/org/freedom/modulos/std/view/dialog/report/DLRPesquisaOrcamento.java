/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)DLRPedido.java <BR>
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

import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.component.JCheckBoxPad;
import java.util.Vector;

public class DLRPesquisaOrcamento extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JRadioGroup<?, ?> rgModo = null;

	private JLabelPad lbModo = new JLabelPad( "Modo da impressão:" );

	private Vector<String> vMLabs = new Vector<String>();

	private Vector<String> vMVals = new Vector<String>();
	
	private JCheckBoxPad cbInfoFatu = new JCheckBoxPad( "Inf. Faturamento", "S", "N" );
	
	private JCheckBoxPad cbItensOrc = new JCheckBoxPad( "Ítens Orçamento.", "S", "N" );

	public DLRPesquisaOrcamento() {

		setTitulo( "Ordem do Relatório" );
		setAtribos( 360, 200 );
		
		vMLabs.addElement( "Grafica" );
		vMLabs.addElement( "Texto" );
		vMVals.addElement( "G" );
		vMVals.addElement( "T" );
		rgModo = new JRadioGroup<String, String>( 1, 2, vMLabs, vMVals );
		rgModo.setVlrString( "G" );

		adic( lbModo, 7, 10, 120, 15 );
		adic( rgModo, 7, 30, 330, 30 );
		
		adic( cbInfoFatu, 9, 77, 120, 20 );
		adic( cbItensOrc, 150, 77, 130, 20 );
	}

	public String getModo() {

		String sRetorno = "";
		if ( rgModo.getVlrString().compareTo( "G" ) == 0 )
			sRetorno = "G";
		else if ( rgModo.getVlrString().compareTo( "T" ) == 0 )
			sRetorno = "T";
		return sRetorno;
	}
	
	public String getInfoFatu() {
	
		String sRetorno = cbInfoFatu.getVlrString();
		return sRetorno;
	}
	
	public String getItensOrc() {
		
		String sRetorno = cbItensOrc.getVlrString();
		return sRetorno;
	}
}
