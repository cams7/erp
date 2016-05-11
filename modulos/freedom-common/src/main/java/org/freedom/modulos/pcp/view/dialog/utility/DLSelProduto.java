/**
 * @version 17/07/2008 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.pcp <BR>
 *         Classe:
 * @(#)DLFechaQual.java <BR>
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
 *                      Comentários sobre a classe...
 */
package org.freedom.modulos.pcp.view.dialog.utility;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.gms.business.object.TipoProd;

import java.util.HashMap;
import java.util.Vector;

public class DLSelProduto extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtRefProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private final JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private final ListaCampos lcProd = new ListaCampos( this, "" );

	private Integer codprod;

	public DLSelProduto( Component cOrig) {
		super(cOrig);
		
		setTitulo( "Qualidade" );
		setAtribos( 360, 150 );

		montaListaCampos();
		montaTela();


	}

	private void montaTela() {

		adic( txtCodProd, 7, 20, 80, 20, "Cód.prod" );
		adic( txtDescProd, 90, 20, 250, 20, "Descrição do produto" );

	}

	public void montaListaCampos() {

		/*************
		 * Produto *
		 *************/

		lcProd.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcProd.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtRefProd, "RefProd", "Referência", ListaCampos.DB_SI, false ) );
		lcProd.setWhereAdic( "ATIVOPROD='S' AND TIPOPROD IN ('" + TipoProd.PRODUTO_ACABADO.getValue() + "','" + TipoProd.PRODUTO_INTERMEDIARIO.getValue() + "')" );
		txtCodProd.setTabelaExterna( lcProd, null );
		txtCodProd.setNomeCampo( "CodProd" );
		txtCodProd.setFK( true );
		lcProd.setReadOnly( true );
		lcProd.montaSql( false, "PRODUTO", "EQ" );
	}
	
	public void setConexao( DbConnection con ) {

		super.setConexao( con );
		lcProd.setConexao( con );
	}
	
	
	public void actionPerformed( ActionEvent evt ) {
		if ( evt.getSource() == btOK ) {
			codprod = txtCodProd.getVlrInteger();
			
			if (codprod <= 0) {
				Funcoes.mensagemInforma( null, "Código do produto é obrigatório!!!" );
				return;
			}
		}
		
		super.actionPerformed( evt );
	}

	
	public Integer getCodprod() {
	
		return codprod;
	}

}
