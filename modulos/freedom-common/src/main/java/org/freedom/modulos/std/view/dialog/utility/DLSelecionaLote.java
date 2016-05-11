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

package org.freedom.modulos.std.view.dialog.utility;

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
import org.freedom.library.swing.frame.Aplicativo;

import java.util.Date;

public class DLSelecionaLote extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodLote = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDataINILote = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldFK txtVenctoLote = new JTextFieldFK( JTextFieldFK.TP_DATE, 10, 0 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JLabelPad lbCodLote = new JLabelPad( "Cód.lote" );

	private JLabelPad lbCodProd = new JLabelPad( "Cód.prod." );

	private JLabelPad lbDescProd = new JLabelPad( "Descrição do produto" );

	private JLabelPad lbDataINILote = new JLabelPad( "Data Inicial" );

	private JLabelPad lbVenctoLote = new JLabelPad( "Vencimento" );

	private ListaCampos lcLote = new ListaCampos( this, "LE" );

	private JTextFieldFK txtSldLiqProd = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private JTextFieldPad txtDtSaidaVenda = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	public DLSelecionaLote( Component cOrig, String sCodProd, String sDescProd, DbConnection cn ) {

		super( cOrig );
		setConexao( cn );
		setTitulo( "Lote" );
		setAtribos( 395, 180 );

		adic( lbCodLote, 7, 0, 150, 20 );
		adic( txtCodLote, 7, 20, 150, 20 );
		adic( lbDataINILote, 160, 0, 102, 20 );
		adic( txtDataINILote, 160, 20, 102, 20 );
		adic( lbVenctoLote, 265, 0, 120, 20 );
		adic( txtVenctoLote, 265, 20, 102, 20 );
		adic( lbCodProd, 7, 40, 300, 20 );
		adic( txtCodProd, 7, 60, 80, 20 );
		adic( lbDescProd, 90, 40, 278, 20 );
		adic( txtDescProd, 90, 60, 278, 20 );

		txtCodProd.setVlrString( sCodProd );
		txtDescProd.setVlrString( sDescProd );
		txtCodLote.requestFocus();
		txtDtSaidaVenda.setVlrDate( new Date() );
		txtCodProd.setEditable( false );

		// FK de Lotes

		txtCodLote.setNomeCampo( "codlote" );

		lcLote.add( new GuardaCampo( txtCodLote, "CodLote", "Lote", ListaCampos.DB_PK, txtVenctoLote, true ) );
		lcLote.add( new GuardaCampo( txtVenctoLote, "VenctoLote", "Dt.vencto.", ListaCampos.DB_SI, false ) );
		lcLote.add( new GuardaCampo( txtDataINILote, "DIniLote", "Dt.Inicio", ListaCampos.DB_SI, false ) );
		lcLote.add( new GuardaCampo( txtSldLiqProd, "SldLiqLote", "Saldo", ListaCampos.DB_SI, false ) );
		lcLote.setDinWhereAdic( "CODPROD=#N AND (VENCTOLOTE >= #D )", txtCodProd );
		lcLote.setDinWhereAdic( "", txtDtSaidaVenda );
		txtCodLote.setFK( true );
		txtCodLote.setTabelaExterna( lcLote, null );
		txtCodLote.setNomeCampo( "codlote" );
		lcLote.setQueryCommit( false );
		lcLote.setReadOnly( true );
		lcLote.montaSql( false, "LOTE", "EQ" );

		lcLote.setConexao( cn );

		/*
		 * txtVenctoLote.setListaCampos( lcLote ); txtVenctoLote.setNomeCampo( "VenctoLote" ); txtVenctoLote.setLabel( "Vencimento" );
		 */

		// txtCodProd.setVlrString( sCodProd );

	}

	public String getValor() {

		return txtCodLote.getVlrString();
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			if ( !"".equals( txtCodLote.getVlrString() ) ) {
				super.actionPerformed( evt );
			}
			else {
				Funcoes.mensagemErro( null, "Lote inválido!" );
				return;
			}
		}
		else if ( evt.getSource() == btCancel ) {
			super.actionPerformed( evt );
		}
	}
}
