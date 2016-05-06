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

package org.freedom.modulos.gms.view.dialog.utility;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;

public class DLLote extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodLote = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDataINILote = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtVenctoLote = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDiasAvisoLote = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JLabelPad lbCodLote = new JLabelPad( "Cód.lote" );

	private JLabelPad lbCodProd = new JLabelPad( "Cód.prod." );

	private JLabelPad lbDescProd = new JLabelPad( "Descrição do produto" );

	private JLabelPad lbDataINILote = new JLabelPad( "Data ini." );

	private JLabelPad lbVenctoLote = new JLabelPad( "Vencimento" );

	private JLabelPad lbQtdProdLote = new JLabelPad( "Qtd. no lote" );

	private JLabelPad lbDiasAvisoLote = new JLabelPad( "Dias para aviso" );

	private JTextFieldPad txtQtdProdLote = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 11, 3 );
	
	private Boolean revalidarLote = false;

	public static final int LOTE_VALIDO = 0;

	public static final int LOTE_INVALIDO = -1;

	public DLLote( Component cOrig, String sCodLote, String sCodProd, String sDescProd, DbConnection cn ) {
		this( cOrig, sCodLote, sCodProd, sDescProd, null, cn );
		
		this.revalidarLote = false;
		
		txtCodLote.setEditable( true );
		txtCodLote.requestFocus();
	}
	
	public DLLote( Component cOrig, String sCodLote, String sCodProd, String sDescProd, Date sDataLote, DbConnection cn ) {
		super( cOrig );
		setConexao( cn );
		setTitulo( "Lote" );
		setAtribos( 400, 220 );

		txtCodProd.setEditable( false );

		txtCodLote.setRequerido( true );
		txtVenctoLote.setRequerido( true );

		adic( lbCodLote, 7, 0, 80, 20 );
		adic( txtCodLote, 7, 20, 80, 20 );
		adic( lbDataINILote, 90, 0, 97, 20 );
		adic( txtDataINILote, 90, 20, 97, 20 );
		adic( lbVenctoLote, 190, 0, 100, 20 );
		adic( txtVenctoLote, 190, 20, 100, 20 );
		adic( lbCodProd, 7, 40, 300, 20 );
		adic( txtCodProd, 7, 60, 80, 20 );
		adic( lbDescProd, 90, 40, 300, 20 );
		adic( txtDescProd, 90, 60, 200, 20 );
		adic( lbQtdProdLote, 7, 80, 300, 20 );
		adic( txtQtdProdLote, 7, 100, 80, 20 );

		adic( lbDiasAvisoLote, 90, 80, 150, 20 );
		adic( txtDiasAvisoLote, 90, 100, 80, 20 );

		txtCodLote.setVlrString( sCodLote );
		txtCodProd.setVlrString( sCodProd );
		txtDescProd.setVlrString( sDescProd );
		//txtVenctoLote.setVlrDate( sDataLote );

		txtDataINILote.requestFocus();
		txtCodLote.setEditable( false );
		
		this.revalidarLote = true;
	}

	private boolean gravaLote() {
		boolean bRet = false;
		
		String sSQL = null;
		PreparedStatement ps = null;
		
		
		if(revalidarLote){
			sSQL = "UPDATE EQLOTE SET DINILOTE = ?, VENCTOLOTE = ?, QTDPRODLOTE = ?, DIASAVISOLOTE = ? ";
			sSQL += "WHERE CODEMP = ? AND CODFILIAL = ? AND CODLOTE = ? AND CODPROD = ?";
			ps = null;
			try {
				ps = con.prepareStatement( sSQL );
				ps.setInt( 5, Aplicativo.iCodEmp );
				ps.setInt( 6, ListaCampos.getMasterFilial( "EQLOTE" ) );
				ps.setString( 7, txtCodLote.getText().trim() );
				ps.setInt( 8, txtCodProd.getVlrInteger().intValue() );

				if ( txtDataINILote.getVlrString().equals( "" ) )
					ps.setNull( 1, Types.DATE );
				else
					ps.setDate( 1, Funcoes.dateToSQLDate( txtDataINILote.getVlrDate() ) );
				ps.setDate( 2, Funcoes.dateToSQLDate( txtVenctoLote.getVlrDate() ) );

				if ( txtQtdProdLote.getVlrBigDecimal() == null ) {
					ps.setNull( 3, Types.DECIMAL );
				}
				else {
					ps.setBigDecimal( 3, txtQtdProdLote.getVlrBigDecimal() );
				}

				if ( txtDiasAvisoLote.getVlrInteger() == null ) {
					ps.setInt( 4, 0 );
				}
				else {
					ps.setInt( 4, txtDiasAvisoLote.getVlrInteger() );
				}

				ps.executeUpdate();
				ps.close();
				con.commit();
				bRet = true;
			} catch ( SQLException err ) {
				if ( err.getErrorCode() == ListaCampos.FB_PK_DUPLA )
					Funcoes.mensagemErro( this, "Este lote já existe!" );
				else
					Funcoes.mensagemErro( this, "Erro ao inserir registro na tabela EQLOTE!\n" + err.getMessage(), true, con, err );
			}
		}else{
			sSQL = "INSERT INTO EQLOTE (CODEMP,CODFILIAL,CODLOTE,CODPROD,DINILOTE,VENCTOLOTE, QTDPRODLOTE, DIASAVISOLOTE) VALUES(?,?,?,?,?,?,?,?)";
			ps = null;
			try {
				ps = con.prepareStatement( sSQL );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "EQLOTE" ) );
				ps.setString( 3, txtCodLote.getText().trim() );
				ps.setInt( 4, txtCodProd.getVlrInteger().intValue() );

				if ( txtDataINILote.getVlrString().equals( "" ) )
					ps.setNull( 5, Types.DATE );
				else
					ps.setDate( 5, Funcoes.dateToSQLDate( txtDataINILote.getVlrDate() ) );
				ps.setDate( 6, Funcoes.dateToSQLDate( txtVenctoLote.getVlrDate() ) );

				if ( txtQtdProdLote.getVlrBigDecimal() == null ) {
					ps.setNull( 7, Types.DECIMAL );
				}
				else {
					ps.setBigDecimal( 7, txtQtdProdLote.getVlrBigDecimal() );
				}

				if ( txtDiasAvisoLote.getVlrInteger() == null ) {
					ps.setInt( 8, 0 );
				}
				else {
					ps.setInt( 8, txtDiasAvisoLote.getVlrInteger() );
				}

				ps.executeUpdate();
				ps.close();
				con.commit();
				bRet = true;
			} catch ( SQLException err ) {
				if ( err.getErrorCode() == ListaCampos.FB_PK_DUPLA )
					Funcoes.mensagemErro( this, "Este lote já existe!" );
				else
					Funcoes.mensagemErro( this, "Erro ao inserir registro na tabela EQLOTE!\n" + err.getMessage(), true, con, err );
			}
		}
		return bRet;
	}

	public String getValor() {
		return txtCodLote.getVlrString();
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			if ( txtCodLote.getText().trim().length() == 0 ) {
				Funcoes.mensagemInforma( this, "Campo Código do lote é requerido!" );
				txtCodLote.requestFocus();
			}else if ( txtVenctoLote.getText().trim().length() == 0 ) {
				Funcoes.mensagemInforma( this, "Campo Vencimento do lote é requerido!" );
				txtVenctoLote.requestFocus();
			}else if(txtVenctoLote.getVlrDate() != null &&
					txtVenctoLote.getVlrDate().compareTo( new Date() ) < 0){
				Funcoes.mensagemInforma( this, "Lote Vencido!" );
				txtVenctoLote.requestFocus();
			}else {
				if ( gravaLote() )
					super.actionPerformed( evt );
			}
		}
		else if ( evt.getSource() == btCancel ) {
			super.actionPerformed( evt );
		}
	}
}
