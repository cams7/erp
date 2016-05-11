/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Alex Rodrigues <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)DLFechaDistrib.java <BR>
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
package org.freedom.modulos.pcp.view.dialog.utility;

import java.awt.Component;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.pcp.business.object.ModLote;
import org.freedom.modulos.pcp.view.frame.crud.detail.FOP;

public class DLFechaDistrib extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtQtdDist = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private JTextFieldPad txtLote = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtSeqDist = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 15, 0 );

	private JTextFieldPad txtDescProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtDtFabProd = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDtValid = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private Vector<Object> vBuscaLote = new Vector<Object>();

	private String lotePrincipal;

	public DLFechaDistrib( Component cOrig, int iSeqDist, int iCodProd, String sDescProd, float ftQtdade ) {

		super( cOrig );
		setTitulo( "Quantidade" );
		setAtribos( 310, 220 );

		adic( new JLabelPad( "Cód.Prod" ), 7, 10, 80, 20 );
		adic( txtCodProd, 7, 30, 80, 20 );
		adic( new JLabelPad( "Descrição da estrutura" ), 90, 10, 180, 20 );
		adic( txtDescProd, 90, 30, 190, 20 );
		adic( new JLabelPad( "Seq.dist." ), 7, 50, 80, 20 );
		adic( txtSeqDist, 7, 70, 80, 20 );
		adic( new JLabelPad( "Quantidade" ), 90, 50, 90, 20 );
		adic( txtQtdDist, 90, 70, 90, 20 );
		adic( new JLabelPad( "Lote" ), 183, 50, 80, 20 );
		adic( txtLote, 183, 70, 97, 20 );
		adic( new JLabelPad( "Validade" ), 183, 90, 80, 20 );
		adic( txtDtValid, 183, 110, 97, 20 );

		txtCodProd.setVlrInteger( new Integer( iCodProd ) );
		txtDescProd.setVlrString( sDescProd );
		txtSeqDist.setVlrInteger( new Integer( iSeqDist ) );
		txtQtdDist.setVlrBigDecimal( new BigDecimal( ftQtdade ) );
		txtCodProd.setAtivo( false );
		txtDescProd.setAtivo( false );
		txtSeqDist.setAtivo( false );
		txtLote.setAtivo( false );
		txtDtValid.setAtivo( false );
		btOK.addActionListener( this );
	}

	public void setLotePrincipal( String lotePrincipal ) {

		this.lotePrincipal = lotePrincipal;
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		if ( getUsaLote().equals( "S" ) ) {
			txtLote.setAtivo( true );
			if ( getUsaModLote() || getModLotePrinc() ) {
				setModLote();
			}
			else {
				buscaLote( txtCodProd.getVlrInteger().intValue() );
				txtLote.setVlrString( (String) vBuscaLote.elementAt( 0 ) );
				txtDtValid.setVlrDate( (java.util.Date) vBuscaLote.elementAt( 1 ) );
			}
		}
		else {
			txtLote.setAtivo( false );
		}
	}

	public void setModLote() {

		Object[] modLote = getModLote();
		txtLote.setVlrString( (String) modLote[ 0 ] );
		txtDtFabProd.setVlrDate( (Date) modLote[ 1 ] );
		txtDtValid.setVlrDate( (Date) modLote[ 2 ] );
	}

	public Object getValor( int index ) {

		Object[] oRetorno = new Object[ 3 ];
		oRetorno[ 0 ] = txtQtdDist.getVlrBigDecimal();
		oRetorno[ 1 ] = txtLote.getVlrString();
		oRetorno[ 2 ] = txtDtValid.getVlrString();
		return oRetorno[ index ];
	}

	private void buscaLote( int iCodProd ) {

		Object lote = null;

		try {

			StringBuilder sql = new StringBuilder();
			sql.append( "SELECT MIN(L.CODLOTE) FROM EQLOTE L " );
			sql.append( "WHERE L.CODEMP=? AND L.CODFILIAL=? AND L.CODPROD=? AND L.SLDLIQLOTE>0 AND " );
			sql.append( "L.VENCTOLOTE=" );
			sql.append( "(SELECT MIN(VENCTOLOTE) FROM EQLOTE LS WHERE LS.CODPROD=L.CODPROD " );
			sql.append( "AND LS.CODFILIAL=L.CODFILIAL AND LS.CODEMP=L.CODEMP AND LS.SLDLIQLOTE>0 " );
			sql.append( "AND VENCTOLOTE >= CAST('today' AS DATE))" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQLOTE" ) );
			ps.setInt( 3, iCodProd );
			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				lote = rs.getString( 1 );

				ps = con.prepareStatement( "SELECT VENCTOLOTE FROM EQLOTE WHERE CODEMP=? AND CODFILIAL=? AND CODLOTE=?" );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "EQLOTE" ) );
				ps.setString( 3, (String) lote );
				rs = ps.executeQuery();
				if ( rs.next() ) {
					vBuscaLote.addElement( lote );
					vBuscaLote.addElement( rs.getDate( 1 ) );
				}
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar lote!\n" + e );
		}
	}

	public boolean getModLotePrinc() {

		boolean modeloDeLotePrincipal = false;

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT GLOTEOPP FROM PPESTRUTURA WHERE CODEMP=? AND CODFILIAL=? AND CODPROD=? AND SEQEST=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			ps.setInt( 3, txtCodProd.getVlrInteger().intValue() );
			ps.setInt( 4, txtSeqDist.getVlrInteger().intValue() );
			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				modeloDeLotePrincipal = "S".equals( rs.getString( "GLOTEOPP" ) );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( null, "Erro ao verificar uso de\nmodelo de pote da OP principal\n" + e.getMessage() );
		}

		return modeloDeLotePrincipal;
	}

	public boolean getUsaModLote() {

		boolean usaModeloDeLote = false;

		try {
			PreparedStatement ps = con.prepareStatement( "SELECT CODMODLOTE FROM PPESTRUTURA WHERE CODEMP=? AND CODFILIAL=? AND CODPROD=? AND SEQEST=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PPESTRUTURA" ) );
			ps.setInt( 3, txtCodProd.getVlrInteger().intValue() );
			ps.setInt( 4, txtSeqDist.getVlrInteger().intValue() );
			ResultSet rs = ps.executeQuery();
			if ( rs.next() ) {
				usaModeloDeLote = rs.getString( 1 ) != null;
			}
			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar modelo de lote para estrutura!\n", true, con, e );
		}

		return usaModeloDeLote;
	}

	public String getUsaLote() {

		String usaLote = "";

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT CLOTEPROD FROM EQPRODUTO WHERE CODEMP=? AND CODFILIAL=? AND CODPROD=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			ps.setInt( 3, txtCodProd.getVlrInteger().intValue() );
			ResultSet rs = ps.executeQuery();
			if ( rs.next() ) {
				usaLote = rs.getString( 1 );
			}
			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar obrigatoriedade de lote no produto!\n", true, con, e );
		}

		return usaLote;
	}

	public boolean existeLote( int iCodProd, String sCodLote ) {

		return FOP.existeLote( con, iCodProd, sCodLote );
	}

	public Object[] getModLote() {

		Object[] lote = null;
		int iDiasValid = 0;
		int iCodProd = txtCodProd.getVlrInteger().intValue();
		int iSeqEst = txtSeqDist.getVlrInteger().intValue();
		Date dtVenctoLote = null;// data de vencimento(data de fabricação + dias de validade
		Date dtFabProd = null;// data de fabricação
		String sModLote = null;// modelo do lote
		String sCodLote = null;// codigo do lote
		ModLote ObjMl = null;

		try {

			StringBuilder sql = new StringBuilder();
			sql.append( "SELECT E.CODMODLOTE, M.TXAMODLOTE, E.NRODIASVALID " );
			sql.append( "FROM PPESTRUTURA E, EQMODLOTE M " );
			sql.append( "WHERE E.CODEMP=? AND E.CODFILIAL=? AND E.CODPROD=? AND E.SEQEST=? AND " );
			sql.append( "E.CODEMPML=M.CODEMP AND E.CODFILIALML=M.CODFILIAL AND E.CODMODLOTE=M.CODMODLOTE" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQLOTE" ) );
			ps.setInt( 3, iCodProd );
			ps.setInt( 4, iSeqEst );
			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				sCodLote = rs.getString( 1 );
				sModLote = rs.getString( 2 );
				iDiasValid = rs.getInt( 3 );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar lote!\n" + e );
		}

		if ( !"".equals( sCodLote ) ) {

			dtFabProd = new Date();
			ObjMl = new ModLote();
			ObjMl.setTexto( sModLote );
			if ( getModLotePrinc() ) {
				sCodLote = ObjMl.getLote( iCodProd, dtFabProd, con, lotePrincipal );
			}
			else {
				sCodLote = ObjMl.getLote( iCodProd, dtFabProd, con );
			}

			Calendar cal = Calendar.getInstance();
			cal.setTime( dtFabProd );
			cal.add( GregorianCalendar.DAY_OF_YEAR, iDiasValid );
			dtVenctoLote = cal.getTime();

			lote = new Object[ 5 ];
			lote[ 0 ] = sCodLote;
			lote[ 1 ] = dtFabProd;
			lote[ 2 ] = dtVenctoLote;
			lote[ 3 ] = sModLote;
			lote[ 4 ] = new Integer( iDiasValid );
		}

		return lote;
	}

	public boolean gravaLote() {

		boolean bret = false;

		int iCodProd = txtCodProd.getVlrInteger().intValue();
		String sCodLote = txtLote.getVlrString();
		Object lote[] = getModLote();
		Object retorno[] = null;

		if ( lote != null ) {
			if ( ( !existeLote( iCodProd, sCodLote ) ) ) {
				txtLote.setVlrString( sCodLote );
				txtDtValid.setVlrDate( (Date) lote[ 2 ] );
				retorno = FOP.gravaLote( con, true, (String) lote[ 3 ], getUsaLote(), (String) lote[ 3 ], iCodProd, (Date) lote[ 1 ], ( (Integer) lote[ 4 ] ).intValue(), sCodLote );
				bret = ( (Boolean) retorno[ 2 ] ).booleanValue();
			}
			else if ( Funcoes.mensagemConfirma( null, "Lote já cadastrado para o produto!\nDeseja usa-lo?" ) == JOptionPane.YES_OPTION ) {
				bret = true;
			}
			else {
				bret = false;
			}
		}

		return bret;
	}
}
