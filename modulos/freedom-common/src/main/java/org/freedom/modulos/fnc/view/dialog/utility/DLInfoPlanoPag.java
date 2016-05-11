/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)DLLote.java <BR>
 * 
 *                 Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                 modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                 na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                 Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                 sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                 Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                 Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                 de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                 Comentários sobre a classe...
 */

package org.freedom.modulos.fnc.view.dialog.utility;

import java.awt.Component;
import java.awt.event.ActionEvent;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.modulos.fnc.library.swing.component.JTextFieldPlan;

public class DLInfoPlanoPag extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JTextFieldPlan txtCodPlanoPag = new JTextFieldPlan( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescPlanoPag = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JLabelPad lbCodPlanoPag = new JLabelPad( "Cód.Plan." );

	private JLabelPad lbDescPlanoPag = new JLabelPad( "Descrição do plano de pagamento" );

	private ListaCampos lcPlanoPag = new ListaCampos( this, "PG" );

	public DLInfoPlanoPag( Component cOrig, DbConnection cn ) {

		super( cOrig );

		setConexao( cn );
		setTitulo( "Informe o plano de pagamento" );
		setAtribos( 370, 140 );

		montaListaCampos();

		txtCodPlanoPag.setRequerido( true );

		adic( lbCodPlanoPag, 7, 0, 80, 20 );
		adic( txtCodPlanoPag, 7, 20, 80, 20 );

		adic( lbDescPlanoPag, 90, 0, 250, 20 );
		adic( txtDescPlanoPag, 90, 20, 250, 20 );

		txtCodPlanoPag.requestFocus();
	}

	private void montaListaCampos() {

		lcPlanoPag.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_PK, txtDescPlanoPag, true ) );
		lcPlanoPag.add( new GuardaCampo( txtDescPlanoPag, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPag.setWhereAdic( "ATIVOPLANOPAG='S' AND CVPLANOPAG IN ('C','A')" );
		lcPlanoPag.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag.setQueryCommit( false );
		lcPlanoPag.setReadOnly( true );
		txtCodPlanoPag.setTabelaExterna( lcPlanoPag, null );
		txtCodPlanoPag.setFK( true );
		txtCodPlanoPag.setNomeCampo( "codplanopag" );

	}

	public Integer getValor() {

		return txtCodPlanoPag.getVlrInteger();
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			if ( txtCodPlanoPag.getText().trim().length() == 0 ) {
				Funcoes.mensagemInforma( this, "Informe o plano de pagamento!" );
				txtCodPlanoPag.requestFocus();

			}
			else {
				super.actionPerformed( evt );
			}
		}
		else if ( evt.getSource() == btCancel ) {
			super.actionPerformed( evt );
		}

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcPlanoPag.setConexao( cn );
	}

}
