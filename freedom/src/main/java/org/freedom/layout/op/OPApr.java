/**
 * @version 23/04/2004<BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 *         iPos Projeto: Freedom <BR>
 *         Pacote: leiautes <BR>
 *         Classe: @(#)OPSwara.java <BR>
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
 *         m
 * 
 *         Layout de Ordem de produção personalizada para empresa Swara...
 */

package org.freedom.layout.op;

import java.awt.Font;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import org.freedom.library.component.LeiauteGR;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;

public class OPApr extends LeiauteGR {

	private static final long serialVersionUID = 1L;

	private DbConnection con = null;

	private Font fnTitulo = new Font( "Times New Roman", Font.BOLD, 14 );

	private Font fnArial9 = new Font( "Arial", Font.PLAIN, 9 );

	private Font fnArial9N = new Font( "Arial", Font.BOLD, 9 );

	Vector<?> vParamOP = new Vector<Object>();

	final int iPosIniItens = 400;

	final int iPosMaxItens = 740;

	int iYPosProd = 0;

	int iY = 160;

	Vector<Vector<String>> vItens = new Vector<Vector<String>>();

	Vector<String> vItem = new Vector<String>();

	int iCodOP = 0;

	String sDescProd = "";

	String sLote = "";

	String sQtd = "";

	Double dbQtd = new Double( 1 );

	String sDtFabrica = "";

	String sDtValidade = "";

	public void montaG() {

		montaRel();
	}

	public void setParam( Vector<?> vParam ) {

		vParamOP = vParam;
	}

	@ SuppressWarnings ( "unchecked" )
	private void montaRel() {

		setMargemPdf( 5, 5 );
		iCodOP = Integer.parseInt( vParamOP.elementAt( 0 ).toString() );
		iYPosProd = iPosIniItens;
		try {
			String sSQL = "SELECT ITOP.CODOP,ITOP.SEQITOP,OP.DTEMITOP,OP.CODPROD,(SELECT PROD2.DESCPROD FROM EQPRODUTO PROD2 WHERE PROD2.CODPROD=OP.CODPROD  AND PROD2.CODEMP=OP.CODEMPPD  AND PROD2.CODFILIAL=OP.CODFILIALPD),"
					+ "EST.DESCEST,EST.QTDEST,OP.DTFABROP,OP.QTDPREVPRODOP,DTVALIDPDOP,OP.DTINS,ITOP.CODPROD,PROD.DESCPROD,UNID.DESCUNID,ITOP.CODLOTE,ITOP.QTDITOP,OP.QTDPREVPRODOP,ITOP.CODFASE,OP.CODLOTE " + "FROM PPESTRUTURA EST,PPOP OP, PPITOP ITOP, EQUNIDADE UNID, EQPRODUTO PROD "
					+ "WHERE EST.CODPROD=OP.CODPROD AND ITOP.CODOP=OP.CODOP AND UNID.CODUNID=PROD.CODUNID " + "AND PROD.CODPROD = ITOP.CODPROD AND OP.CODOP=? AND OP.CODEMP=? AND OP.CODFILIAL=?";

			PreparedStatement ps = con.prepareStatement( sSQL );

			System.out.println( "SQL:" + sSQL );

			ps.setInt( 1, iCodOP );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "PPOP" ) );

			ResultSet rs = ps.executeQuery();

			while ( rs.next() ) {
				vItem = new Vector<String>();
				vItem.addElement( ( rs.getString( 12 ) != null ? rs.getString( 12 ) : "" ) ); // Código
				vItem.addElement( ( rs.getString( 13 ) != null ? rs.getString( 13 ) : "" ) ); // Descrição
				vItem.addElement( ( rs.getString( 16 ) != null ? Funcoes.strDecimalToStrCurrency( 3, rs.getString( 16 ) ) : "0" ) ); // Quantidade
				vItem.addElement( ( rs.getString( 14 ) != null ? rs.getString( 14 ) : "" ) ); // Unidade
				vItem.addElement( ( rs.getString( 15 ) != null ? rs.getString( 15 ) : "" ) ); // Lote
				vItem.addElement( ( rs.getString( 18 ) != null ? rs.getString( 18 ) : "0" ) ); // Fase
				vItens.addElement( (Vector<String>) vItem.clone() );
				System.out.println( "Adicionou:" + rs.getString( 13 ) );
			}

			sDescProd = ( rs.getString( 5 ) != null ? rs.getString( 5 ).trim() : "" );
			// sQtd = (rs.getString(16)!=null?Funcoes.strDecimalToStrCurrency(3,rs.getString(16)):"");
			// sQtd = (rs.getString(9)!=null?Funcoes.strDecimalToStrCurrency(3,rs.getString(9)):"");
			dbQtd = ( rs.getString( 9 ) != null ? new Double( Funcoes.strDecimalToBigDecimal( 3, rs.getString( 9 ) ).doubleValue() ) : dbQtd );
			sQtd = dbQtd.toString();
			sDtFabrica = ( rs.getDate( 8 ) != null ? StringFunctions.sqlDateToStrDate( rs.getDate( 8 ) ) : "" );
			sDtValidade = ( rs.getDate( 10 ) != null ? StringFunctions.sqlDateToStrDate( rs.getDate( 10 ) ) : "" );
			sLote = ( rs.getString( 19 ) != null ? rs.getString( 19 ).trim() : "" );
			montaCabEmp( con );
			montaCab();

			sSQL = "SELECT OPF.SEQOF, OPF.CODFASE,F.DESCFASE,F.TIPOFASE,OPF.TEMPOOF,OPF.CODRECP,REC.DESCRECP " + "FROM PPOPFASE OPF, PPFASE F, PPRECURSO REC WHERE " + "F.CODFASE = OPF.CODFASE AND F.CODEMP = OPF.CODEMPFS  AND F.CODFILIAL = OPF.CODFILIALFS "
					+ "AND REC.CODRECP = OPF.CODRECP AND REC.CODEMP = OPF.CODEMPRP AND REC.CODFILIAL = OPF.CODFILIALRP " + "AND OPF.CODOP=? AND OPF.CODEMP=? AND OPF.CODFILIAL=? " + "ORDER BY OPF.SEQOF";

			PreparedStatement psFases = con.prepareStatement( sSQL );

			psFases.setInt( 1, iCodOP );
			psFases.setInt( 2, Aplicativo.iCodEmp );
			psFases.setInt( 3, ListaCampos.getMasterFilial( "PPOPFASE" ) );

			ResultSet rsFases = psFases.executeQuery();

			System.out.println( "Query das fases:" + sSQL );

			montaFases( rsFases );

			rs.close();
			ps.close();

			impAssinatura();
			termPagina();
			finaliza();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao montar o cabeçalho do relatório!!!\n" + err.getMessage() );
			err.printStackTrace();
		}
	}

	private void montaFases( ResultSet rsFases ) {

		try {
			while ( rsFases.next() ) {
				if ( rsFases.getString( 4 ).equals( "EX" ) ) {
					impFaseEx( rsFases );
				}
				else if ( rsFases.getString( 4 ).equals( "CQ" ) ) {
					impFaseCq( rsFases );
				}
				else if ( rsFases.getString( 4 ).equals( "EB" ) ) {
					impFaseEB( rsFases );
				}
			}
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
	}

	private void impFaseEx( ResultSet rsFases ) {

		try {

			int iCodFaseF = rsFases.getInt( 2 );
			int iCodFaseI = 0;
			setFonte( fnTitulo );

			System.out.println( "Entrou no imprime fase do tipo EX. iCodFaseF:" + iCodFaseF );
			iY = iY + 12;
			int iYIni = iY;

			drawTexto( "FASE: " + rsFases.getString( 1 ).trim(), 0, iY, 50, AL_CEN );

			iY = iY + 12;

			setFonte( fnArial9N );
			drawTexto( "Recurso:", 10, iY );
			setFonte( fnArial9 );
			drawTexto( rsFases.getString( 7 ), 60, iY );

			setFonte( fnArial9N );
			drawTexto( "Tempo estimado(min.):", 220, iY );
			setFonte( fnArial9 );
			Double dbQtdEstr = new Double( rsFases.getFloat( 5 ) / 60 );
			drawTexto( ( dbQtdEstr.floatValue() ) * ( dbQtd.floatValue() ) + "", 330, iY );
			iY = iY + 10;
			drawLinha( 5, iY, 5, 0, AL_CDIR );
			iY = iY + 12;
			setFonte( fnArial9N );
			drawTexto( "Cód.", 10, iY );
			drawTexto( "Descrição", 50, iY );
			drawTexto( "Qtd.", 300, iY );
			drawTexto( "Unidade", 350, iY );
			drawTexto( "Lote", 450, iY );
			drawLinha( 5, iY + 5, 5, 0, AL_CDIR );
			iY = iY + 15;
			setFonte( fnArial9 );

			String sCod = "";
			String sDesc = "";
			String sQtd = "";
			String sUnid = "";
			String sLote = "";

			for ( int i = 0; vItens.size() > i; i++ ) {
				sCod = vItens.elementAt( i ).elementAt( 0 ).toString();
				sDesc = vItens.elementAt( i ).elementAt( 1 ).toString();
				sQtd = vItens.elementAt( i ).elementAt( 2 ).toString();
				sUnid = vItens.elementAt( i ).elementAt( 3 ).toString();
				sLote = vItens.elementAt( i ).elementAt( 4 ).toString();
				iCodFaseI = Integer.parseInt( vItens.elementAt( i ).elementAt( 5 ).toString() );

				if ( iCodFaseI == iCodFaseF ) {
					drawTexto( sCod, 10, iY ); // Codigo
					drawTexto( sDesc, 50, iY ); // Descrição
					drawTexto( Funcoes.alinhaDir( sQtd, 15 ), 270, iY );// Quantidade
					drawTexto( sUnid, 350, iY );// Unidade
					drawTexto( sLote, 450, iY );// Lote
					iY = iY + 12;
				}
			}
			iY = iY + 10;
			drawRetangulo( 5, iYIni - 15, 5, iY - iYIni, AL_CDIR );

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private void impFaseEB( ResultSet rsFases ) {

		int iCodFaseF = 0;
		int iCodFaseI = 0;
		int iYIni = 0;

		try {
			iCodFaseF = rsFases.getInt( 2 );

			setFonte( fnTitulo );
			iY = iY + 10;
			iYIni = iY;

			drawTexto( "FASE: " + rsFases.getString( 1 ).trim(), 0, iY, getFontMetrics( fnTitulo ).stringWidth( "  " + "FASE: " + rsFases.getString( 1 ).trim() + "  " ), AL_CEN );

			iY = iY + 15;
			drawTexto( rsFases.getString( 3 ).trim().toUpperCase(), 0, iY, getFontMetrics( fnTitulo ).stringWidth( "  " + rsFases.getString( 3 ).trim().toUpperCase() + "  " ), AL_CEN );
			iY = iY + 13;

			setFonte( fnArial9N );
			drawTexto( "Recurso:", 10, iY );
			setFonte( fnArial9 );
			drawTexto( rsFases.getString( 7 ), 60, iY );

			setFonte( fnArial9N );
			drawTexto( "Tempo estimado(min.):", 220, iY );
			setFonte( fnArial9 );
			drawTexto( ( rsFases.getFloat( 5 ) / 60 ) + "", 330, iY );
			iY = iY + 10;

			drawLinha( 5, iY, 5, 0, AL_CDIR );

			iY = iY + 5;

			setFonte( fnArial9N );
			iY = iY + 14;
			drawTexto( "Embalagens a serem descarregadas", 60, iY );
			iY = iY + 16;
			setFonte( fnArial9N );
			drawTexto( "Cód.   Tipo de Embalagem    Lote          Qtd. Emb.", 20, iY );
			iY = iY + 20;
			setFonte( fnArial9 );
			iY = iY + 15;
			iY = iY + 15;

			int iX = 280;
			iY = iY - 65;
			setFonte( fnArial9N );
			drawTexto( "Descarregamento, pesagem e rotulagem", 50 + iX, iY );
			iY = iY + 16;
			drawTexto( "Cód.   Tipo de Embalagem    Lote          Qtd. Emb.", 12 + iX, iY );
			setFonte( fnArial9 );
			iY = iY + 12;

			String sCod = "";
			String sDesc = "";
			String sQtd = "";
			String sUnid = "";
			String sLote = "";

			for ( int i = 0; vItens.size() > i; i++ ) {
				sCod = vItens.elementAt( i ).elementAt( 0 ).toString();
				sDesc = vItens.elementAt( i ).elementAt( 1 ).toString();
				sLote = vItens.elementAt( i ).elementAt( 4 ).toString();
				sQtd = vItens.elementAt( i ).elementAt( 2 ).toString();
				sUnid = vItens.elementAt( i ).elementAt( 3 ).toString();

				iCodFaseI = Integer.parseInt( vItens.elementAt( i ).elementAt( 5 ).toString() );

				if ( iCodFaseI == iCodFaseF ) {
					drawTexto( sCod, 18, iY ); // Codigo
					drawTexto( sCod, iX + 12, iY ); // Codigo
					drawTexto( sDesc.substring( 0, 20 ), 47, iY ); // Descrição
					drawTexto( sDesc.substring( 0, 20 ), iX + 42, iY ); // Descrição
					drawTexto( sLote, 145, iY );// Lote
					drawTexto( sLote, iX + 140, iY );// Lote
					drawTexto( Funcoes.alinhaDir( sQtd, 15 ) + " " + sUnid, 170, iY );// Quantidade
					drawTexto( Funcoes.alinhaDir( sQtd, 15 ) + " " + sUnid, iX + 162, iY );// Quantidade
					iY = iY + 12;
				}
			}

			setFonte( fnArial9N );
			iY = iY + 15;
			drawTexto( "OBS.:____________________________________________", 12 + iX, iY );
			iY = iY + 15;
			drawTexto( "_________________________________________________", 12 + iX, iY );
			iY = iY + 15;
			drawTexto( "_________________________________________________", 12 + iX, iY );
			iY = iY + 20;
			drawTexto( "Nome:____________________________________________", 12 + iX, iY );
			iY = iY + 15;
			drawTexto( "Data:____________________________________________", 12 + iX, iY );
			iY = iY + 30;

			drawLinha( 280, iYIni + 43, 280, iYIni + 43 + iY - iYIni - 55 );
			drawRetangulo( 10, iYIni + 43, 10, iY - iYIni - 55, AL_CDIR );
			drawRetangulo( 5, iYIni - 15, 5, iY - iYIni + 8, AL_CDIR );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private void impFaseCq( ResultSet rsFases ) {

		try {

			setFonte( fnTitulo );
			iY = iY + 10;
			int iYIni = iY;

			drawTexto( "FASE: " + rsFases.getString( 1 ).trim(), 0, iY, getFontMetrics( fnTitulo ).stringWidth( "  " + "FASE: " + rsFases.getString( 1 ).trim() + "  " ), AL_CEN );

			iY = iY + 15;
			drawTexto( rsFases.getString( 3 ).trim().toUpperCase(), 0, iY, getFontMetrics( fnTitulo ).stringWidth( "  " + rsFases.getString( 3 ).trim().toUpperCase() + "  " ), AL_CEN );
			iY = iY + 13;

			setFonte( fnArial9N );
			drawTexto( "Recurso:", 10, iY );
			setFonte( fnArial9 );
			drawTexto( rsFases.getString( 7 ), 60, iY );

			setFonte( fnArial9N );
			drawTexto( "Tempo estimado(min.):", 220, iY );
			setFonte( fnArial9 );
			drawTexto( ( rsFases.getFloat( 5 ) / 60 ) + "", 330, iY );
			iY = iY + 10;

			drawLinha( 5, iY, 5, 0, AL_CDIR );

			iY = iY + 5;
			drawLinha( 280, iY, 280, iY + 100 );

			drawRetangulo( 10, iY, 10, 100, AL_CDIR );

			setFonte( fnArial9N );
			iY = iY + 14;
			drawTexto( "PRODUÇÃO", 110, iY );
			iY = iY + 16;
			setFonte( fnArial9N );
			drawTexto( "Amostra retirada para análise", 75, iY );
			iY = iY + 20;
			setFonte( fnArial9 );
			drawTexto( "Quantidade:", 15, iY );
			drawLinha( 70, iY, 240, iY );
			iY = iY + 15;
			drawTexto( "Nome:", 15, iY );
			drawLinha( 70, iY, 240, iY );
			iY = iY + 15;
			drawTexto( "Data:", 15, iY );
			drawLinha( 70, iY, 240, iY );

			int iX = 280;
			iY = iY - 65;
			setFonte( fnArial9N );
			drawTexto( "LABORATÓRIO", 110 + iX, iY );
			iY = iY + 16;
			drawTexto( "Amostra retirada para análise", 75 + iX, iY );
			iY = iY + 20;
			setFonte( fnArial9 );
			drawTexto( "Resultado:", 15 + iX, iY );
			drawLinha( 70 + iX, iY, 240 + iX, iY );
			iY = iY + 15;
			drawTexto( "Nome:", 15 + iX, iY );
			drawLinha( 70 + iX, iY, 240 + iX, iY );
			iY = iY + 15;
			drawTexto( "Data:", 15 + iX, iY );
			drawLinha( 70 + iX, iY, 240 + iX, iY );
			iY = iY + 39;

			drawRetangulo( 5, iYIni - 15, 5, iY - iYIni, AL_CDIR );

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private void impAssinatura() {

		String sSql = "SELECT NOMERESP,IDENTPROFRESP,CARGORESP FROM SGPREFERE5 WHERE CODEMP=? AND CODFILIAL=?";
		String sNome = "";
		String sCargo = "";
		String sID = "";
		try {

			PreparedStatement ps = null;
			ResultSet rs = null;
			ps = con.prepareStatement( sSql );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE5" ) );
			rs = ps.executeQuery();

			if ( rs.next() ) {
				if ( rs.getString( "NOMERESP" ) != null )
					sNome = rs.getString( "NOMERESP" ).trim();
				if ( rs.getString( "CARGORESP" ) != null )
					sCargo = rs.getString( "CARGORESP" ).trim();
				if ( rs.getString( "IDENTPROFRESP" ) != null )
					sID = rs.getString( "IDENTPROFRESP" ).trim();
			}

			rs.close();
			ps.close();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		iY = iY + 30;
		setFonte( fnArial9N );
		drawLinha( 0, iY, 300, 0, AL_CEN );
		iY = iY + 10;
		drawTexto( sNome, 0, iY, getFontMetrics( fnArial9N ).stringWidth( "  " + sNome + "  " ), AL_CEN );
		iY = iY + 12;
		drawTexto( sCargo, 0, iY, getFontMetrics( fnArial9N ).stringWidth( "  " + sCargo + "  " ), AL_CEN );
		iY = iY + 12;
		drawTexto( sID, 0, iY, getFontMetrics( fnArial9N ).stringWidth( "  " + sID + "  " ), AL_CEN );

	}

	private void montaCab() {

		try {
			setBordaRel();
			setFonte( fnTitulo );
			drawLinha( 0, 35, 0, 0, AL_BDIR );
			drawRetangulo( 5, 40, 5, 107, AL_CDIR );
			drawTexto( "ORDEM DE PRODUÇÃO", 0, 55, 150, AL_CEN );
			setFonte( fnArial9N );
			drawTexto( "O.P. número:", 10, 70 );
			drawTexto( "Produto:", 10, 82 );
			drawTexto( "Quantidade:", 10, 94 );
			drawTexto( "Data de fabricação:", 10, 106 );
			drawTexto( "Data de validade:", 10, 118 );
			drawTexto( "Data de emissão:", 10, 130 );
			drawTexto( "Lote:", 10, 142 );

			setFonte( fnArial9 );

			drawTexto( iCodOP + "", 110, 70 ); // Código da OP
			drawTexto( sDescProd, 110, 82 ); // Descrição do produto a ser fabricado
			drawTexto( sQtd, 110, 94 ); // qtd. a fabricar
			drawTexto( sDtFabrica, 110, 106 ); // Data de fabricação
			drawTexto( sDtValidade, 110, 118 ); // Data de validade
			drawTexto( Funcoes.dateToStrDate( new Date() ), 110, 130 );
			drawTexto( sLote, 110, 142 );

		} catch ( Exception err ) {
			Funcoes.mensagemErro( this, "Erro ao montar dados do cliente!!!\n" + err.getMessage() );
			err.printStackTrace();
		}
	}

	public void stParam( Vector<?> vParam ) {

		vParamOP = vParam;
	}

	public void setConexao( DbConnection cn ) {

		con = cn;
	}
}
