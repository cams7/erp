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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.std.view.frame.crud.plain.FSerie;

public class DLSerie extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtNumSerie = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDtFabricSerie = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDtValidSerie = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JLabelPad lbNumSerie = new JLabelPad( "Número de série" );

	private JLabelPad lbCodProd = new JLabelPad( "Cód.prod." );

	private JLabelPad lbDescProd = new JLabelPad( "Descrição do produto" );

	private JLabelPad lbDtFabricSerie = new JLabelPad( "Data de Fabricação" );

	private JLabelPad lbDtValidSerie = new JLabelPad( "Data de validade" );

	private JLabelPad lbObsSerie = new JLabelPad( "Observações" );

	private JTextAreaPad txaObsSerie = new JTextAreaPad();

	private JScrollPane spnObsSerie = new JScrollPane( txaObsSerie );

	private ListaCampos lcNumSerie = new ListaCampos( this, "NS" );

	private boolean novo = true;

	private enum ORDEM_INSERT {
		CODEMP, CODFILIAL, CODPROD, NUMSERIE, DTFABRICSERIE, DTVALIDSERIE, OBSSERIE
	}

	private enum ORDEM_UPDATE {
		DTFABRICSERIE, DTVALIDSERIE, OBSSERIE, CODEMP, CODFILIAL, CODPROD, NUMSERIE
	}

	public DLSerie( Component orig, String numserie, Integer codprod, String descprod ) {

		super( orig );

		setConexao( Aplicativo.getInstace().con );
		lcNumSerie.setConexao( ( Aplicativo.getInstace().con ) );

		setTitulo( "Série" );
		setAtribos( 380, 300 );

		txtCodProd.setEditable( false );

		txtNumSerie.setRequerido( true );

		adic( lbNumSerie, 7, 0, 353, 20 );
		adic( txtNumSerie, 7, 20, 353, 20 );

		adic( lbCodProd, 7, 40, 60, 20 );
		adic( txtCodProd, 7, 60, 60, 20 );

		adic( lbDescProd, 70, 40, 290, 20 );
		adic( txtDescProd, 70, 60, 290, 20 );

		adic( lbDtFabricSerie, 7, 80, 175, 20 );
		adic( txtDtFabricSerie, 7, 100, 175, 20 );

		adic( lbDtValidSerie, 185, 80, 175, 20 );
		adic( txtDtValidSerie, 185, 100, 175, 20 );

		adic( lbObsSerie, 7, 120, 353, 20 );
		adic( spnObsSerie, 7, 140, 353, 60 );

		txtNumSerie.setVlrString( numserie );
		txtCodProd.setVlrInteger( codprod );
		txtDescProd.setVlrString( descprod );

		txtNumSerie.requestFocus();

		montaListaCampos();

	}

	private void montaListaCampos() {

		lcNumSerie.add( new GuardaCampo( txtNumSerie, "NumSerie", "Num.Série", ListaCampos.DB_PK, false ) );
		lcNumSerie.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcNumSerie.add( new GuardaCampo( txaObsSerie, "ObsSerie", "Observações", ListaCampos.DB_SI, false ) );
		lcNumSerie.add( new GuardaCampo( txtDtFabricSerie, "dtfabricserie", "Data fabricação", ListaCampos.DB_SI, false ) );
		lcNumSerie.add( new GuardaCampo( txtDtValidSerie, "dtvalidserie", "Data validade", ListaCampos.DB_SI, false ) );
		lcNumSerie.setDinWhereAdic( "CODPROD=#N", txtCodProd );
		lcNumSerie.setAutoLimpaPK( false );
		lcNumSerie.montaSql( false, "SERIE", "EQ" );
		lcNumSerie.setQueryCommit( false );
		lcNumSerie.setReadOnly( true );
		txtNumSerie.setTabelaExterna( lcNumSerie, FSerie.class.getCanonicalName() );
		txtNumSerie.setFK( true );
		txaObsSerie.setListaCampos( lcNumSerie );

	}

	public void setNumSerie( String numserie ) {

		txtNumSerie.setVlrString( numserie );
	}

	public void carregaSerie() {

		StringBuilder sql = new StringBuilder();

		try {

			sql.append( "select se.numserie, se.dtfabricserie, se.dtvalidserie, se.obsserie " );
			sql.append( "from eqserie se " );
			sql.append( "where se.codemp=? and se.codfilial=? and se.codprod=? and se.numserie=? " );

			PreparedStatement ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQSERIE" ) );
			ps.setInt( 3, txtCodProd.getVlrInteger() );
			ps.setString( 4, txtNumSerie.getVlrString() );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {

				txtDtFabricSerie.setVlrDate( rs.getDate( "dtfabricserie" ) );
				txtDtValidSerie.setVlrDate( rs.getDate( "dtvalidserie" ) );
				txaObsSerie.setVlrString( rs.getString( "obsserie" ) );

				novo = false;

			}

			con.commit();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private boolean gravaSerie() {

		StringBuilder sql = new StringBuilder();
		boolean bRet = false;
		PreparedStatement ps = null;

		try {

			if ( novo ) {

				sql.append( "INSERT INTO EQSERIE (CODEMP,CODFILIAL,CODPROD,NUMSERIE,DTFABRICSERIE,DTVALIDSERIE,OBSSERIE) VALUES(?,?,?,?,?,?,?)" );

				ps = con.prepareStatement( sql.toString() );

			}
			else {

				sql.append( "UPDATE EQSERIE SET DTFABRICSERIE=?, DTVALIDSERIE=? ,OBSSERIE=? " );
				sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODPROD=? AND NUMSERIE=? " );

				ps = con.prepareStatement( sql.toString() );

			}

			ps.setInt( novo ? ORDEM_INSERT.CODEMP.ordinal() + 1 : ORDEM_UPDATE.CODEMP.ordinal() + 1, Aplicativo.iCodEmp );
			ps.setInt( novo ? ORDEM_INSERT.CODFILIAL.ordinal() + 1 : ORDEM_UPDATE.CODFILIAL.ordinal() + 1, ListaCampos.getMasterFilial( "EQSERIE" ) );
			ps.setInt( novo ? ORDEM_INSERT.CODPROD.ordinal() + 1 : ORDEM_UPDATE.CODPROD.ordinal() + 1, txtCodProd.getVlrInteger() );

			ps.setString( novo ? ORDEM_INSERT.NUMSERIE.ordinal() + 1 : ORDEM_UPDATE.NUMSERIE.ordinal() + 1, txtNumSerie.getVlrString() );

			if ( txtDtFabricSerie.getVlrString().equals( "" ) ) {
				ps.setNull( novo ? ORDEM_INSERT.DTFABRICSERIE.ordinal() + 1 : ORDEM_UPDATE.DTFABRICSERIE.ordinal() + 1, Types.DATE );
			}
			else {
				ps.setDate( novo ? ORDEM_INSERT.DTFABRICSERIE.ordinal() + 1 : ORDEM_UPDATE.DTFABRICSERIE.ordinal() + 1, Funcoes.dateToSQLDate( txtDtFabricSerie.getVlrDate() ) );
			}

			if ( txtDtValidSerie.getVlrString().equals( "" ) ) {
				ps.setNull( novo ? ORDEM_INSERT.DTVALIDSERIE.ordinal() + 1 : ORDEM_UPDATE.DTVALIDSERIE.ordinal() + 1, Types.DATE );
			}
			else {
				ps.setDate( novo ? ORDEM_INSERT.DTVALIDSERIE.ordinal() + 1 : ORDEM_UPDATE.DTVALIDSERIE.ordinal() + 1, Funcoes.dateToSQLDate( txtDtValidSerie.getVlrDate() ) );
			}

			if ( txaObsSerie.getVlrString().equals( "" ) ) {
				ps.setNull( novo ? ORDEM_INSERT.OBSSERIE.ordinal() + 1 : ORDEM_UPDATE.OBSSERIE.ordinal() + 1, Types.CHAR );
			}
			else {
				ps.setString( novo ? ORDEM_INSERT.OBSSERIE.ordinal() + 1 : ORDEM_UPDATE.OBSSERIE.ordinal() + 1, txaObsSerie.getVlrString() );
			}

			ps.executeUpdate();

			ps.close();

			con.commit();

			bRet = true;

		} catch ( SQLException err ) {
			if ( err.getErrorCode() == ListaCampos.FB_PK_DUPLA ) {

				if ( Funcoes.mensagemConfirma( null, "Este número de série já existe!\nDeseja continuar?" ) == JOptionPane.YES_OPTION ) {
					bRet = true;
				}

			}
			else {
				Funcoes.mensagemErro( this, "Erro ao inserir registro na tabela EQSERIE!\n" + err.getMessage(), true, con, err );
			}
		}
		return bRet;
	}

	public String getNumSerie() {

		return txtNumSerie.getVlrString();

	}

	public Date getDtFabricSerie() {

		return txtDtFabricSerie.getVlrDate();
	}

	public Date getDtValidSerie() {

		return txtDtValidSerie.getVlrDate();
	}

	public String getObsSerie() {

		return txaObsSerie.getVlrString();
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			if ( txtNumSerie.getVlrString().length() == 0 ) {
				Funcoes.mensagemInforma( this, "Campo número de série é requerido!" );
				txtNumSerie.requestFocus();
			}
			else {
				if ( gravaSerie() )
					super.actionPerformed( evt );
			}
		}
		else if ( evt.getSource() == btCancel ) {
			super.actionPerformed( evt );
		}
	}
}
