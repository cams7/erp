/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)DLRProduto.java <BR>
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

package org.freedom.modulos.pcp.view.dialog.report;

import java.util.Vector;

import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.dialog.FFDialogo;

public class DLREstrutura extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JRadioGroup<?, ?> rgOrdem = null;

	private JRadioGroup<?, ?> rgModo = null;

	private JRadioGroup<?, ?> rgEstrut = null;

	private JRadioGroup<?, ?> rgImp = null;

	private JLabelPad lbModo = new JLabelPad( "Modo do Relatório:" );

	private JLabelPad lbEstrut = new JLabelPad( "Estrutura:" );

	private JLabelPad lbOrdem = new JLabelPad( "Ordenado por:" );

	private JLabelPad lbImp = new JLabelPad( "Impressão:" );

	private Vector<String> vLabs = new Vector<String>();

	private Vector<String> vVals = new Vector<String>();

	private Vector<String> vLabsModo = new Vector<String>();

	private Vector<String> vValsModo = new Vector<String>();

	private Vector<String> vLabsEstrut = new Vector<String>();

	private Vector<String> vValsEstrut = new Vector<String>();

	private Vector<String> vLabsImp = new Vector<String>();

	private Vector<String> vValsImp = new Vector<String>();

	public DLREstrutura() {

		setTitulo( "Relatório de Estrutura de Produtos" );
		setAtribos( 295, 320 );

		vLabsEstrut.addElement( "Atual" );
		vLabsEstrut.addElement( "Todas" );
		vValsEstrut.addElement( "A" );
		vValsEstrut.addElement( "T" );
		rgEstrut = new JRadioGroup<String, String>( 1, 2, vLabsEstrut, vValsEstrut );
		rgEstrut.setVlrString( "A" );

		vLabsModo.addElement( "Resumido" );
		vLabsModo.addElement( "Completo" );
		vValsModo.addElement( "R" );
		vValsModo.addElement( "C" );
		rgModo = new JRadioGroup<String, String>( 1, 2, vLabsModo, vValsModo );
		rgModo.setVlrString( "C" );

		vLabs.addElement( "Código" );
		vLabs.addElement( "Descrição" );
		vVals.addElement( "C" );
		vVals.addElement( "D" );
		rgOrdem = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgOrdem.setVlrString( "D" );

		vLabsImp.addElement( "Texto" );
		vLabsImp.addElement( "Gráfica" );
		vValsImp.addElement( "T" );
		vValsImp.addElement( "G" );
		rgImp = new JRadioGroup<String, String>( 1, 2, vLabsImp, vValsImp );
		rgImp.setVlrString( "G" );

		adic( lbEstrut, 10, 10, 70, 20 );
		adic( rgEstrut, 10, 30, 260, 30 );

		adic( lbModo, 10, 70, 150, 20 );
		adic( rgModo, 10, 90, 260, 30 );

		adic( lbOrdem, 10, 130, 150, 20 );
		adic( rgOrdem, 10, 150, 260, 30 );

		adic( lbImp, 10, 180, 150, 20 );
		adic( rgImp, 10, 200, 260, 30 );

	}

	public String[] getValores( boolean comref ) {

		String[] sRetorno = new String[ 4 ];
		String orderby = "";

		if ( rgModo.getVlrString().compareTo( "C" ) == 0 ) {

			String separador = "";

			if ( rgOrdem.getVlrString().compareTo( "D" ) == 0 ) {
				orderby += "PD.DESCPROD";
			}
			else {

				if ( !"".equals( orderby ) ) {
					separador = ",";
				}

				orderby += separador;

				if ( comref ) {
					orderby += "PD.REFPROD";
				}
				else {
					orderby += "PD.CODPROD";
				}
			}

			if ( !"".equals( orderby ) ) {
				separador = ",";
			}

			orderby += separador;

			if ( comref ) {
				orderby += "IT.REFPRODPD, E.SEQEST, IT.CODFASE";
			}
			else {
				orderby += "PI.DESCPROD, E.SEQEST, IT.CODFASE";
			}

		}
		else if ( rgOrdem.getVlrString().compareTo( "D" ) == 0 ) {
			orderby = "PD.DESCPROD";
		}
		else {

			if ( comref ) {
				orderby += "PD.REFPROD, E.SEQEST";
			}
			else {
				orderby += "PD.CODPROD, E.SEQEST";
			}

		}

		sRetorno[ 0 ] = orderby;
		sRetorno[ 1 ] = rgModo.getVlrString();
		sRetorno[ 2 ] = rgEstrut.getVlrString();
		sRetorno[ 3 ] = rgImp.getVlrString();

		return sRetorno;
	}

}
