/**
 * @version 14/01/2004<BR>
 * @author Setpoint Informática Ltda./Marco Antonio Sanchez <BR>
 *         iPos Projeto: Freedom <BR>
 *         UmontaCab Pacote: org.freedom.layout <BR>
 *         Classe: @(#)OrcMCraft.java <BR>
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
 *         Orcamento padronizado para Modelcraft...
 */

package org.freedom.layout.orc;

import java.awt.Font;
import java.math.BigDecimal;
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

public class OrcMCraft extends LeiauteGR {

	private static final long serialVersionUID = 1L;

	private DbConnection con = null;

	private Font fnTitulo = new Font( "Times New Roman", Font.BOLD, 15 );

	private Font fnCabCliIta = new Font( "Times New Roman", Font.ITALIC, 14 );

	Vector<?> vParamOrc = new Vector<Object>();

	final int iPosIniItens = 360;

	final int iPosMaxItens = 740;

	final int iSaltoProd = 13;

	int iYPosProd = 0;

	int iYObsVal = 0;

	BigDecimal bigTot = new BigDecimal( 0 );

	String sDescPlan;

	public void montaG() {

		montaRel();
	}

	private int impLabelSilabas( String sTexto, int iSalto, int iMargem, int iLargura, int iY, ResultSet rs ) {

		double iPixels = getFontMetrics( fnCabCliIta ).stringWidth( sTexto );
		double iNLinhas = iPixels / iLargura;
		int iNCaracteres = Funcoes.tiraChar( sTexto, "\n" ).length();
		int iNCaracPorLinha = (int) ( iNCaracteres / iNLinhas );
		Vector<?> vTextoSilabas = Funcoes.strToVectorSilabas( sTexto, iNCaracPorLinha );
		for ( int i = 0; vTextoSilabas.size() > i; i++ ) {

			if ( iY > iPosMaxItens && rs != null ) {
				iYPosProd = iY = iPosIniItens - 300;
				termPagina();
			}

			setFonte( fnCabCliIta );

			drawTexto( vTextoSilabas.elementAt( i ).toString(), iMargem, iY );
			iY += iSalto;
		}
		return iY;
	}

	private void montaTot( ResultSet rs ) {

		int iY = iYPosProd + 10;
		try {
			setFont( fnCabCliIta );

			String sValLiq = Funcoes.strDecimalToStrCurrency( 2, bigTot.toString() );
			drawTexto( "Total liq.:", 340, iY );
			drawTexto( sValLiq, 475, iY, getFontMetrics( fnCabCliIta ).stringWidth( sValLiq ), AL_DIR );
			bigTot = new BigDecimal( 0 );

			iY += 30;
			setFonte( fnCabCliIta );

			drawTexto( "Prazo de Entrega:", 80, iY );

			setFonte( fnCabCliIta );

			if ( rs.getString( "PRAZOENTORC" ) == null )
				drawTexto( "À Combinar.", 230, iY );
			else
				drawTexto( rs.getString( "PRAZOENTORC" ) + " dias.", 230, iY );

			iY += 20;

			drawTexto( "Pagamento.:", 80, iY );

			iY = impLabelSilabas( sDescPlan, 15, 230, 230, iY, rs );

			iY += 10;

			iY = impLabelSilabas( "    A validade do orçamento é de 15 dias.Após esta data, os preços e " + "demais condições, ficarão sujeitos a alterações, por ocasião da confirmação " + "do pedido.", 15, 76, 500, iY, rs );

			iY += 20;

			iY = impLabelSilabas( "Atenciosamente,", 15, 260, 200, iY, rs );

			iY += 30;

			setFonte( fnCabCliIta );
			drawLinha( 230, iY, 405, iY );
			drawTexto( rs.getString( "NOMEVEND" ), 264, iY + 13 );

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao montar final do orçamento!!!\n" + err.getMessage() );
			err.printStackTrace();
		}
	}

	private void montaRel() {

		setMargemPdf( 5, 5 );
		int iCodOrc = Integer.parseInt( vParamOrc.elementAt( 0 ).toString() );
		iYPosProd = iPosIniItens;
		imprimeRodape( false );
		try {
			String sSQLCab = "SELECT " + "(SELECT COUNT(IO.CODITORC) FROM VDITORCAMENTO IO WHERE IO.CODEMP=O.CODEMP" + " AND IO.CODFILIAL=O.CODFILIAL AND IO.CODORC=O.CODORC)," + "O.CODORC,O.OBSORC,O.VLRLIQORC,O.VLRDESCORC, O.VLRDESCITORC,O.VLRPRODORC,F.CIDFILIAL,"
					+ "C.RAZCLI,C.CONTCLI,C.FONECLI,C.DDDCLI,C.FAXCLI,VD.NOMEVEND,O.CODPLANOPAG,O.CODEMPPG," + "O.CODFILIALPG,O.PRAZOENTORC " + " FROM VDORCAMENTO O,SGFILIAL F, VDCLIENTE C, VDVENDEDOR VD,FNPLANOPAG PG"
					+ " WHERE C.CODEMP=O.CODEMPCL AND C.CODFILIAL=O.CODFILIALCL AND C.CODCLI=O.CODCLI" + " AND O.CODVEND=VD.CODVEND AND O.CODFILIALVD=VD.CODFILIAL AND O.CODEMPVD=VD.CODEMP" + " AND F.CODEMP=O.CODEMP AND F.CODFILIAL=O.CODFILIAL"
					+ " AND O.TIPOORC = 'O' AND O.CODORC=? AND O.CODEMP=? AND O.CODFILIAL=? AND " + " O.CODPLANOPAG=PG.CODPLANOPAG AND O.CODEMPPG=PG.CODEMP AND O.CODFILIALPG=PG.CODFILIAL";

			PreparedStatement psCab = con.prepareStatement( sSQLCab );
			psCab.setInt( 1, iCodOrc );
			psCab.setInt( 2, Aplicativo.iCodEmp );
			psCab.setInt( 3, ListaCampos.getMasterFilial( "VDORCAMENTO" ) );
			ResultSet rsCab = psCab.executeQuery();
			if ( !rsCab.next() )
				return;
			montaCab( rsCab );
			montaCabecalho();

			int iCodPg = rsCab.getInt( "CODPLANOPAG" );
			int iCodEmpPg = rsCab.getInt( "CODEMPPG" );
			int iCodFilialPg = rsCab.getInt( "CODFILIALPG" );

			String sSQL = "SELECT DESCPARCPAG FROM FNPARCPAG WHERE CODPLANOPAG=? AND CODEMP=? AND CODFILIAL=?";

			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, iCodPg );
			ps.setInt( 2, iCodEmpPg );
			ps.setInt( 3, iCodFilialPg );
			ResultSet rs = ps.executeQuery();
			sDescPlan = "";

			while ( rs.next() ) {
				sDescPlan += rs.getString( 1 ) + "\n";
			}
			rs.close();
			ps.close();

			sSQL = "SELECT " + "IT.VLRPRODITORC/IT.QTDITORC,IT.CODPROD," + " P.REFPROD,P.DESCPROD,IT.QTDITORC,IT.VLRDESCITORC," + "IT.VLRLIQITORC,IT.OBSITORC" + " FROM VDITORCAMENTO IT, EQPRODUTO P WHERE" + " P.CODPROD=IT.CODPROD AND P.CODEMP=IT.CODEMPPD AND P.CODFILIAL=IT.CODFILIALPD"
					+ " AND IT.TIPOORC = 'O' AND IT.CODORC=? AND IT.CODEMP=? AND IT.CODFILIAL=? " + " ORDER BY IT.CODORC,IT.CODITORC";

			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, iCodOrc );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "VDORCAMENTO" ) );
			rs = ps.executeQuery();
			for ( int i = 1; ( rs.next() ); i++ ) {

				int iY2 = iYPosProd;
				setFonte( fnCabCliIta );
				String sVal = Funcoes.strDecimalToStrCurrency( 2, rs.getString( "VLRLIQITORC" ) );
				drawTexto( sVal, 470, iYPosProd + 5, getFontMetrics( fnCabCliIta ).stringWidth( sVal ), AL_DIR );
				String sVal1 = Funcoes.strDecimalToStrCurrency( 2, rs.getString( "QTDITORC" ) );
				drawTexto( sVal1, 350, iYPosProd + 5, getFontMetrics( fnCabCliIta ).stringWidth( sVal1 ), AL_DIR );

				bigTot = bigTot.add( new BigDecimal( rs.getString( "VLRLIQITORC" ) ) );

				if ( rs.getString( "ObsItOrc" ) != null ) {
					setFonte( fnCabCliIta );
					iY2 = impLabelSilabas( rs.getString( "ObsItOrc" ), iSaltoProd, 60, 350, iYPosProd + 3, rsCab ) - 5;
				}
				else {
					setFonte( fnCabCliIta );
					iY2 = impLabelSilabas( rs.getString( "DescProd" ).trim(), iSaltoProd, 60, 350, iYPosProd, rsCab );
					setFonte( fnCabCliIta );
				}

				iYPosProd = iY2;

				iYPosProd += 15;

				if ( i >= rsCab.getInt( 1 ) ) {
					// Por ser o ultimo item termina tudo!!!
					montaTot( rsCab );
					termPagina();
				}

				else if ( iYPosProd > iPosMaxItens ) {
					// Se estourou o limite de linhas, pula pra outra pagina:
					termPagina();
					iYPosProd = iPosIniItens - 300;
				}

			}

			rsCab.close();
			psCab.close();
			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao montar o cabeçalho do relatório!!!\n" + err.getMessage() );
			err.printStackTrace();
		}

		finaliza();
	}

	private void montaCabecalho() {

		StringBuilder sSQL = new StringBuilder();
		ResultSet rs = null;
		PreparedStatement ps = null;

		sSQL.append( "SELECT E.ENDFILIAL, E.CNPJFILIAL, E.FONEFILIAL, E.RAZFILIAL FROM SGFILIAL E " );
		sSQL.append( "WHERE E.CODEMP=? AND E.CODFILIAL=? " );

		try {

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGFILIAL" ) );

			rs = ps.executeQuery();

		} catch ( SQLException err ) {

			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar dados da empresa" );
		}

		try {

			while ( rs.next() ) {

				drawTexto( rs.getString( "RAZFILIAL" ), 60, 40 );
				drawTexto( "Endereço:", 60, 60 );
				drawTexto( rs.getString( "ENDFILIAL" ), 120, 60 );
				drawTexto( "Telefone:", 60, 80 );
				drawTexto( rs.getString( "FONEFILIAL" ), 120, 80 );
				drawTexto( "CNPJ:", 60, 100 );
				drawTexto( rs.getString( "CNPJFILIAL" ).substring( 0, 2 ) + "." + rs.getString( "CNPJFILIAL" ).substring( 2, 5 ) + "." + rs.getString( "CNPJFILIAL" ).substring( 5, 8 ) + "/" + rs.getString( "CNPJFILIAL" ).substring( 8, 12 ) + "-" + rs.getString( "CNPJFILIAL" ).substring( 12, 14 ),
						120, 100 );

			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private void montaCab( ResultSet rs ) {

		try {

			setFonte( fnTitulo );
			drawTexto( "ORÇAMENTO", 200, 130 );

			setFonte( fnCabCliIta );
			drawTexto( rs.getString( "CidFilial" ).trim() + ", " + Funcoes.dateToStrExtenso( new Date() ), 60, 160 );
			// Linha 1

			setFonte( fnTitulo );
			drawTexto( "" + rs.getInt( "CodOrc" ), 440, 160 );

			setFonte( fnCabCliIta );
			drawTexto( "À", 60, 180 );
			setFonte( fnCabCliIta );
			drawTexto( rs.getString( "RazCli" ) != null ? rs.getString( "RazCli" ).trim() : "", 60, 195 );

			// Linha 2
			setFonte( fnCabCliIta );
			drawTexto( "A/C:", 60, 210 );
			setFonte( fnCabCliIta );
			drawTexto( "Sr(a) : " + rs.getString( "ContCli" ), 101, 210 );

			setFonte( fnCabCliIta );
			drawTexto( "Fone : ", 60, 225 );
			drawTexto( ( rs.getString( "DDDCli" ) != null ? "(" + rs.getString( "DDDCli" ) + ")" : "" ) + ( rs.getString( "FoneCli" ) != null ? Funcoes.setMascara( rs.getString( "FoneCli" ).trim(), "####-####" ) : "" ).trim() + "    Fax.:  "
					+ ( rs.getString( "FaxCli" ) != null ? Funcoes.setMascara( rs.getString( "FaxCli" ), "####-####" ) : "" ), 101, 225 );

			setFonte( fnCabCliIta );
			drawTexto( "Prezado(a) Senhor(a);", 60, 260 );

			setFonte( fnCabCliIta );

			impLabelSilabas( "Conforme a solitação de V.Sa., temos o máximo prazer de passar " + "às suas mãos a nossa oferta, valendo as condições gerais de venda e " + "fornecimento conforme abaixo descriminados", 15, 60, 500, 290, rs );

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao montar dados do cliente!!!\n" + err.getMessage() );
			err.printStackTrace();
		}
	}

	public void setParam( Vector<?> vParam ) {

		vParamOrc = vParam;
	}

	public void setConexao( DbConnection cn ) {

		con = cn;
	}
}
