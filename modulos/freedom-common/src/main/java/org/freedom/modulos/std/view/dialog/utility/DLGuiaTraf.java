/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)DLGuiaTraf.java <BR>
 * 
 *                     Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                     modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                     na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                     Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                     sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                     Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                     Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                     de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                     Comentários sobre a classe...
 */

package org.freedom.modulos.std.view.dialog.utility;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DLGuiaTraf extends FFDialogo implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodCompra = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtCodEmp = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtCodFilial = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtCodItCompra = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 3, 0 );

	private JTextFieldPad txtDtEmissGuiaTraf = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtNumGuiaTraf = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtSeloGuiaTraf = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private JLabelPad lbCodCompra = new JLabelPad( "Cód.compra" );

	private JLabelPad lbCodEmp = new JLabelPad( "Cód.emp" );

	private JLabelPad lbCodFilial = new JLabelPad( "Cód.filial" );

	private JLabelPad lbCodItCompra = new JLabelPad( "Cód.item" );

	private JLabelPad lbNumGuiaTraf = new JLabelPad( "Número da guia" );

	private JLabelPad lbDataEmissGuia = new JLabelPad( "Data de emissão" );

	private JLabelPad lbSeloGuiaTraf = new JLabelPad( "Selo da Guia" );

	int codcompra;

	int coditcompra;

	DbConnection con = null;

	public DLGuiaTraf( int codcompra, int coditcompra, DbConnection con ) {

		super();
		setTitulo( "Guia de tráfego" );
		setAtribos( 350, 150 );

		txtDtEmissGuiaTraf.setRequerido( true );
		txtNumGuiaTraf.setRequerido( true );
		txtSeloGuiaTraf.setRequerido( true );

		adic( lbDataEmissGuia, 7, 0, 100, 20 );
		adic( txtDtEmissGuiaTraf, 7, 20, 100, 20 );
		adic( lbNumGuiaTraf, 110, 0, 100, 20 );
		adic( txtNumGuiaTraf, 110, 20, 100, 20 );
		adic( lbSeloGuiaTraf, 213, 0, 100, 20 );
		adic( txtSeloGuiaTraf, 213, 20, 100, 20 );

		this.codcompra = codcompra;
		this.coditcompra = coditcompra;
		this.con = con;

	}

	public boolean getGuiaTraf() {

		boolean retorno = false;
		StringBuffer sSQL = new StringBuffer();
		PreparedStatement ps = null;

		sSQL.append( "select f.dtemisguiatraf, f.nroguiatraf, f.nroseloguiatraf from eqguiatraf f " );
		sSQL.append( "where f.codemp=? and f.codfilial=? and f.codcompra=? and f.coditcompra=?" );

		try {

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQGUIATRAF" ) );
			ps.setInt( 3, codcompra );
			ps.setInt( 4, coditcompra );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {

				txtDtEmissGuiaTraf.setVlrDate( rs.getDate( "dtemisguiatraf" ) );
				txtNumGuiaTraf.setVlrString( rs.getString( "nroguiatraf" ) );
				txtSeloGuiaTraf.setVlrInteger( rs.getInt( "nroseloguiatraf" ) );
				retorno = true;
			}

		} catch ( SQLException err ) {

			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro buscar dados de guia de tráfego!" );
		}

		return retorno;
	}

	public void insertGuiaTraf() {

		StringBuffer sSQL = new StringBuffer();
		PreparedStatement ps = null;

		sSQL.append( "INSERT INTO EQGUIATRAF( CODEMP, CODFILIAL, CODCOMPRA, CODITCOMPRA, DTEMISGUIATRAF, " );
		sSQL.append( "NROGUIATRAF, NROSELOGUIATRAF ) VALUES (?,?,?,?,?,?,?)" );

		try {

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQGUIATRAF" ) );
			ps.setInt( 3, codcompra );
			ps.setInt( 4, coditcompra );
			ps.setDate( 5, Funcoes.dateToSQLDate( txtDtEmissGuiaTraf.getVlrDate() ) );
			ps.setInt( 6, txtNumGuiaTraf.getVlrInteger() );
			ps.setInt( 7, txtSeloGuiaTraf.getVlrInteger() );

			ps.executeUpdate();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {

			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao salvar dados na tabela EQGUIATRAF \n" + err.getMessage() );
		}

	}

	public void updatGuiaTraf() {

		StringBuffer sSQL = new StringBuffer();
		PreparedStatement ps = null;

		sSQL.append( "UPDATE EQGUIATRAF SET DTEMISGUIATRAF=?, " );
		sSQL.append( "NROGUIATRAF=?, NROSELOGUIATRAF=? WHERE CODEMP=? AND CODFILIAL=? AND CODCOMPRA=? AND CODITCOMPRA=?" );

		try {

			ps = con.prepareStatement( sSQL.toString() );
			ps.setDate( 1, Funcoes.dateToSQLDate( txtDtEmissGuiaTraf.getVlrDate() ) );
			ps.setInt( 2, txtNumGuiaTraf.getVlrInteger() );
			ps.setInt( 3, txtSeloGuiaTraf.getVlrInteger() );
			ps.setInt( 4, Aplicativo.iCodEmp );
			ps.setInt( 5, ListaCampos.getMasterFilial( "EQGUIATRAF" ) );
			ps.setInt( 6, codcompra );
			ps.setInt( 7, coditcompra );

			ps.executeUpdate();

			con.commit();

		} catch ( SQLException err ) {

			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao salvar dados na tabela EQGUIATRAF \n" + err.getMessage() );
		}
	}
}
