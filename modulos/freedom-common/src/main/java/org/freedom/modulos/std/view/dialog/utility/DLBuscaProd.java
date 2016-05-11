/**
 * @version 23/02/2004 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva/Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)DLBuscaProd.java <BR>
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
 *                      Tela para busca de produtos por similaridade ou códigos alternativos.
 */

package org.freedom.modulos.std.view.dialog.utility;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.dialog.DLF3;
import org.freedom.library.swing.frame.Aplicativo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.freedom.acao.TabelaSelEvent;
import org.freedom.acao.TabelaSelListener;

public class DLBuscaProd extends DLF3 implements TabelaSelListener {

	private static final long serialVersionUID = 1L;

	private String sCol = null;

	private Vector<?> vValsProd = new Vector<Object>();

	public int iPadrao = 0;

	boolean bRet = false;

	String sWhereAdic = "";

	public DLBuscaProd( DbConnection con, String sCol, String sWhere ) {

		this.sCol = sCol;
		setConexao( con );
		sWhereAdic = sWhere;
		setAtribos( 575, 260 );

		tab.adicColuna( "Cód." );
		tab.adicColuna( "Ref." );
		tab.adicColuna( "Descrição" );
		tab.adicColuna( "Saldo" );
		tab.setTamColuna( 70, 0 );// código.
		tab.setTamColuna( 70, 1 );// Referencia.
		tab.setTamColuna( 300, 2 );
		tab.setTamColuna( 90, 5 );
		tab.addTabelaSelListener( this );

		tab.addKeyListener( this );
		btOK.addActionListener( this );

	}

	public Object getValor() {

		return oRetVal;
	}

	public boolean setValor( Object oVal, String sTipo ) {

		StringBuffer sSQL = new StringBuffer();

		if ( sTipo.equals( "similar" ) ) {

			if ( sWhereAdic == null ) {
				sWhereAdic = "";
			}

			sSQL.append( "SELECT SIM.CODPROD,PROD.REFPROD,PROD.DESCPROD,PROD.SLDPROD " );
			sSQL.append( "FROM EQPRODUTO PROD,EQITSIMILAR SIM " );
			sSQL.append( "WHERE  SIM.CODEMP = PROD.CODEMP AND SIM.CODFILIAL=PROD.CODFILIAL " );
			sSQL.append( "AND SIM.CODSIM = (SELECT SIM2.CODSIM FROM EQITSIMILAR SIM2 " );
			sSQL.append( "                   WHERE SIM2.CODEMP=PROD.CODEMP AND SIM2.CODFILIAL = PROD.CODFILIAL " );
			sSQL.append( "                   AND SIM2." + sCol + "=?) " );
			sSQL.append( "AND PROD.CODEMP = ? AND PROD.CODFILIAL = ? " );
			sSQL.append( sWhereAdic.equals( "" ) ? "" : "AND " + sWhereAdic );
			sSQL.append( " AND PROD.CODPROD=SIM.CODPROD" );

			setTitulo( "Produtos similares à " + oVal.toString() );

		}
		else {
			sSQL.append( "SELECT ALT.CODPROD,ALT.REFPROD,PROD.DESCPROD,PROD.SLDPROD FROM eqcodaltprod ALT, EQPRODUTO PROD " );
			sSQL.append( " WHERE PROD.CODEMP = ALT.CODEMP AND PROD.CODFILIAL=ALT.codfilial AND PROD.CODPROD = ALT.CODPROD " );
			sSQL.append( " AND ALT.CODALTPROD = ? AND ALT.CODEMP=? AND ALT.CODFILIAL = ? " );

			setTitulo( "Produtos encontrados com o código alternativo:" + oVal.toString() );
		}

		try {
			PreparedStatement ps = con.prepareStatement( sSQL.toString() );
			String sVal = oVal.toString();
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			ps.setString( 1, sVal );

			tab.limpa();
			tab.removeAll();
			vValsProd.clear();

			ResultSet rs = ps.executeQuery();

			int iCont = 0;
			while ( rs.next() ) {
				bRet = true;

				if ( rs.getString( 1 ).equals( sVal ) ) {
					iPadrao = iCont;
				}

				tab.adicLinha( new Object[] { rs.getString( 1 ) != null ? rs.getString( 1 ) : "", rs.getString( 2 ) != null ? rs.getString( 2 ) : "", rs.getString( 3 ) != null ? rs.getString( 3 ).trim() : "", rs.getString( 4 ) != null ? rs.getString( 4 ) : "", } );

				if ( "REFPROD".equals( sCol.toUpperCase() ) ) {
					oRetVal = rs.getString( 1 ) != null ? rs.getString( 1 ) : "";
				}
				else {
					oRetVal = rs.getString( 2 ) != null ? rs.getString( 2 ) : "";
				}
				iCont++;
			}
			rs.close();
			ps.close();
			con.commit();

			if ( bRet ) {
				tab.requestFocus();
				tab.setLinhaSel( iPadrao );
				setVisible( true );
			}
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar código auxiliar!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
		return bRet;
	}

	public void actionPerformed( ActionEvent evt ) {

		super.actionPerformed( evt );

		if ( evt.getSource() == btOK ) {
			if ( tab.getNumLinhas() > 0 ) {
				buscaValores();
			}
		}
	}

	public void valorAlterado( TabelaSelEvent tsevt ) {

	}

	public void setValor( Object oVal ) {

	}

	public void keyPressed( KeyEvent kevt ) {

		if ( kevt.getSource() == tab && kevt.getKeyCode() == KeyEvent.VK_ENTER ) {
			if ( tab.getNumLinhas() > 0 ) {
				buscaValores();
				btOK.doClick();
			}
		}
		else if ( kevt.getKeyCode() == KeyEvent.VK_ESCAPE ) {
			btCancel.doClick();
		}

	}

	public void buscaValores() {

		if ( tab.getNumLinhas() > 0 ) {
			if ( bRet ) {
				if ( "REFPROD".equals( sCol.toUpperCase() ) ) {
					oRetVal = tab.getValueAt( tab.getLinhaSel(), 1 );
				}
				else {
					oRetVal = tab.getValueAt( tab.getLinhaSel(), 0 );
				}
			}
		}
	}

}
