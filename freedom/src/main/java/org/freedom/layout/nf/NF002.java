/**
 * @version 22/05/2006 <BR>
 * @author Setpoint Informática Ltda./Alex Rodrigues <BR>
 * 
 *         Projeto: Freedom <BR>
 *         Pacote: layout <BR>
 *         Classe:
 * @(#)NFIswara.java <BR>
 * 
 *                   Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                   modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                   na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                   Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                   sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                   Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                   Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                   de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                   Layout da nota fiscal para a empresa Iswara Ltda.
 */

package org.freedom.layout.nf;

import java.math.BigDecimal;
import java.util.Vector;

import org.freedom.library.business.component.NF;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.component.Layout;
import org.freedom.library.functions.Funcoes;

public class NF002 extends Layout {

	public boolean imprimir( NF nf, ImprimeOS imp ) {

		boolean retorno = super.imprimir( nf, imp );
		boolean bFat = true;
		boolean bjatem = false;
		boolean bvlriss = true;
		final int MAXLINE = 29;
		final int MAXPROD = 12;
		int iNumNota = 0;
		int iItImp = 0;
		int iProdImp = 0;
		int iContaFrete = 0;
		int iLinPag = imp.verifLinPag( "NF" );
		int sizeObs = 0;
		int indexObs = 0;
		int indexSigla = 0;
		int indexServ = 0;
		String sCodfisc = null;
		String sSigla = null;
		String sTemp = null;
		String sDescFisc = "";
		String sObsVenda = "";
		String[] sValsCli = new String[ 4 ];
		String[] sNat = new String[ 2 ];
		String[] sVencs = new String[ 9 ];
		String[] sVals = new String[ 9 ];
		String[] sDuplics = new String[ 9 ];
		Vector<?> vObsVenda = new Vector<Object>();
		Vector<String> vClfisc = new Vector<String>();
		Vector<String> vSigla = new Vector<String>();
		Vector<?> vDescServ = new Vector<Object>();
		Vector<Object[]> vServico = new Vector<Object[]>();
		BigDecimal vlricmsorig = new BigDecimal( 0 );
		Float vlrTotImp = new Float( 0 );
		String sDesc = "";

		try {

			imp.setaEspVert( ImprimeOS.EPSON_6PP );

			if ( cab.next() ) {
				iNumNota = cab.getInt( NF.C_DOC );
				// sObsVenda = cab.getString( NF.C_OBSPED ).replace( "\n", "" );
				sObsVenda = cab.getString( NF.C_OBSPED );
			}
			imp.limpaPags();

			vClfisc.addElement( "" );

			while ( itens.next() ) {

				if ( adic.next() ) {
					sValsCli[ 0 ] = !adic.getString( NF.C_CPFEMITAUX ).equals( "" ) ? adic.getString( NF.C_CPFEMITAUX ) : cab.getString( NF.C_CPFEMIT );
					sValsCli[ 1 ] = !adic.getString( NF.C_NOMEEMITAUX ).equals( "" ) ? adic.getString( NF.C_NOMEEMITAUX ) : cab.getString( NF.C_RAZEMIT );
					sValsCli[ 2 ] = !adic.getString( NF.C_CIDEMITAUX ).equals( "" ) ? adic.getString( NF.C_CIDEMITAUX ) : cab.getString( NF.C_CIDEMIT );
					sValsCli[ 3 ] = !adic.getString( NF.C_UFEMITAUX ).equals( "" ) ? adic.getString( NF.C_UFEMITAUX ) : cab.getString( NF.C_UFEMIT );
				}
				else {
					sValsCli[ 0 ] = cab.getString( NF.C_CPFEMIT );
					sValsCli[ 1 ] = cab.getString( NF.C_RAZEMIT );
					sValsCli[ 2 ] = cab.getString( NF.C_CIDEMIT );
					sValsCli[ 3 ] = cab.getString( NF.C_UFEMIT );
				}

				if ( imp.pRow() == 0 ) {

					// Imprime cabeçalho da nota

					imp.pulaLinha( 4, imp.comprimido() );
					imp.say( 118, ( cab.getDate( NF.C_DTEMITPED ) != null ? Funcoes.dateToStrDate( cab.getDate( NF.C_DTEMITPED ) ) : "" ) );
					imp.pulaLinha( 3, imp.comprimido() );
					imp.say( 6, sValsCli[ 1 ] );
					imp.say( 109, !sValsCli[ 0 ].trim().equals( "" ) ? Funcoes.setMascara( sValsCli[ 0 ], "###.###.###-##" ) : Funcoes.setMascara( cab.getString( NF.C_CNPJEMIT ), "##.###.###/####-##" ) );
					imp.pulaLinha( 3, imp.comprimido() );
					imp.say( 6, Funcoes.copy( cab.getString( NF.C_ENDEMIT ), 0, 50 ).trim() + ", " + Funcoes.copy( cab.getString( NF.C_NUMEMIT ), 0, 6 ).trim() + " - " + Funcoes.copy( cab.getString( NF.C_COMPLEMIT ), 0, 9 ).trim() );
					imp.say( 109, Funcoes.copy( cab.getString( NF.C_INSCEMIT ), 0, 23 ) );
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 58, Funcoes.setMascara( cab.getString( NF.C_CEPEMIT ), "#####-###" ) );
					imp.say( 88, sValsCli[ 2 ] );
					imp.say( 132, sValsCli[ 3 ] );
					imp.pulaLinha( 3, imp.comprimido() );

					// Fim do cabeçalho

				}

				// Imprime os dados do item no corpo da nota

				Vector<?> vDesc = Funcoes.strToVectorSilabas( itens.getString( NF.C_OBSITPED ) == null || itens.getString( NF.C_OBSITPED ).equals( "" ) ? ( itens.getString( NF.C_DESCPROD ).trim() ) : itens.getString( NF.C_OBSITPED ), 60 );

				for ( int iConta = 0; ( ( iConta < 20 ) && ( vDesc.size() > iConta ) ); iConta++ ) {

					if ( !vDesc.elementAt( iConta ).toString().equals( "" ) ) {
						sDesc = vDesc.elementAt( iConta ).toString();
					}
					else {
						sDesc = "";
					}

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 8, sDesc );
				}

				BigDecimal vlrproditped = itens.getBigDecimal( NF.C_VLRPRODITPED );
				BigDecimal qtditped = itens.getBigDecimal( NF.C_QTDITPED );
				BigDecimal precoitped = vlrproditped.divide( qtditped, 2, BigDecimal.ROUND_HALF_UP );

				imp.say( 79, Funcoes.copy( itens.getString( NF.C_CODUNID ), 4 ) );
				imp.say( 84, Funcoes.strDecimalToStrCurrency( 10, 2, itens.getString( NF.C_QTDITPED ) ) );
				imp.say( 89, Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( precoitped ) ) );
				imp.say( 100, Funcoes.strDecimalToStrCurrency( 15, 2, cab.getString( NF.C_VLRDESCITPED ) ) );
				imp.say( 115, Funcoes.strDecimalToStrCurrency( 18, 2, String.valueOf( itens.getString( NF.C_VLRPRODITPED ) ) ) );

				iProdImp++;

				// Fim da impressão do item

				iItImp++;

				if ( ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) || ( imp.pRow() == MAXLINE - 1 ) ) {

					if ( iContaFrete == 0 ) {
						frete.next();
						iContaFrete++;
					}

					imp.pulaLinha( MAXLINE - imp.pRow(), imp.comprimido() );

					// Imprime totais

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 7, Funcoes.strDecimalToStrCurrency( 13, 2, String.valueOf( cab.getFloat( NF.C_BASEISS ) ) ) );
					imp.say( 28, Funcoes.strDecimalToStrCurrency( 5, 2, String.valueOf( cab.getFloat( NF.C_PERCISS ) ) + " %" ) );
					imp.say( 44, Funcoes.strDecimalToStrCurrency( 13, 2, String.valueOf( cab.getFloat( NF.C_VLRISS ) ) ) );
					imp.say( 69, Funcoes.strDecimalToStrCurrency( 13, 2, String.valueOf( cab.getFloat( NF.C_VLRPRODPED ) ) ) );
					imp.say( 85, Funcoes.strDecimalToStrCurrency( 13, 2, String.valueOf( cab.getFloat( NF.C_VLRDESCVENDA ) ) ) );

					imp.pulaLinha( 2, imp.comprimido() );

					if ( cab.getString( NF.C_IMPPISVENDA ).equals( "S" ) ) {

						imp.say( 33, "Pis:" );
						imp.say( 41, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( cab.getFloat( NF.C_VLRPISVENDA ) ) ) );
						vlrTotImp += cab.getFloat( NF.C_VLRPISVENDA );

					}
					if ( cab.getString( NF.C_IMPCOFINSVENDA ).equals( "S" ) ) {

						imp.say( 53, "Cofins:" );
						imp.say( 63, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( cab.getFloat( NF.C_VLRCOFINSVENDA ) ) ) );
						vlrTotImp += cab.getFloat( NF.C_VLRCOFINSVENDA );
					}

					imp.pulaLinha( 2, imp.comprimido() );
					if ( cab.getString( NF.C_IMPIRVENDA ).equals( "S" ) ) {

						imp.say( 33, "IR:" );
						imp.say( 41, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( cab.getFloat( NF.C_VLRIRVENDA ) ) ) );
						vlrTotImp += cab.getFloat( NF.C_VLRIRVENDA );

					}
					imp.say( 82, cab.getString( NF.C_DESCPLANOPAG ) );
					if ( cab.getString( NF.C_IMPCSOCIALVENDA ).equals( "S" ) ) {

						imp.say( 53, "C.Social:" );
						imp.say( 63, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( cab.getFloat( NF.C_VLRCSOCIALVENDA ) ) ) );
						vlrTotImp += cab.getFloat( NF.C_VLRCSOCIALVENDA );

					}
					imp.pulaLinha( 1, imp.comprimido() );
					if ( cab.getString( NF.C_IMPIISSVENDA ).equals( "S" ) ) {

						imp.say( 33, "B.Imp.:" );
						imp.say( 41, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( cab.getFloat( NF.C_BASEISS ) ) ) );
						vlrTotImp += cab.getFloat( NF.C_VLRISS );

					}
					if ( vlrTotImp > 0 ) {

						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 33, "T.Imp.:" );
						imp.say( 41, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( vlrTotImp ) ) );
					}
					imp.say( 115, Funcoes.strDecimalToStrCurrency( 18, 2, String.valueOf( cab.getString( NF.C_VLRLIQPED ) ) ) );

					// Fim da impressão dos totais

					// Imprime observação e classificações fiscais

					vObsVenda = Funcoes.strToVectorSilabas( ( sDescFisc.length() > 0 ? sDescFisc + "\n" : "" ) + sObsVenda, 60 );

					sizeObs = vSigla.size();
					sizeObs = vObsVenda.size() > sizeObs ? vObsVenda.size() : sizeObs;

					int aux = 0;
					imp.pulaLinha( 4, imp.comprimido() );
					for ( int i = 0; i < 8; i++ ) {
						if ( i < vObsVenda.size() ) {
							imp.say( 8, Funcoes.copy( (String) vObsVenda.elementAt( i ), 100 ) );
						}
						imp.pulaLinha( 1, imp.comprimido() );
					}

					// Fim da observação

					imp.pulaLinha( 5, imp.comprimido() );

					imp.setPrc( 0, 0 );
					imp.incPags();

				}
			}

			imp.fechaGravacao();
			retorno = true;

		} catch ( Exception err ) {
			Funcoes.mensagemErro( null, "Erro ao montar nota \n" + err.getMessage() );
			err.printStackTrace();
		} finally {
			sValsCli = null;
			sNat = null;
			sVencs = null;
			sVals = null;
			System.gc();
		}

		return retorno;

	}

}
