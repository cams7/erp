/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 *         Pacote: layout <BR>
 *         Classe: @(#)NFPomiagro3.java <BR>
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
 *         Layout da nota fiscal para a empresa Pomiagro Ltda.
 */

package org.freedom.layout.nf;

import java.math.BigDecimal;
import java.util.Vector;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.business.component.NF;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.component.Layout;
import org.freedom.library.functions.Funcoes;

public class NF04 extends Layout {

	@ SuppressWarnings ( "unchecked" )
	public boolean imprimir( NF nf, ImprimeOS imp ) {

		boolean retorno = super.imprimir( nf, imp );
		boolean bFat = true;
		boolean bNat = true;
		final int MAXLINE = 26;
		int iNumNota = 0;
		int iItImp = 0;
		int iContaAst = 1;
		int iMesImp = 0;
		int iObsImp = 0;
		int iContaFrete = 0;
		int iLinPag = imp.verifLinPag( "NF" );
		String sTipoTran = null;
		String sObs = null;
		String sTmp = null;
		String sDescAdic = null;
		String sImpDtSaidaNat = null;
		String sIncra = null;
		String sClasFisc = null;
		String[] sValsCli = new String[ 4 ];
		String[] sNat = new String[ 2 ];
		String[] sDuplics = new String[ 6 ];
		String[] sVencs = new String[ 6 ];
		String[] sVals = new String[ 6 ];
		String[] sMatObs = new String[ 5 ];
		Vector vObs = new Vector();
		Vector vMens = new Vector();
		Vector vMensT1 = new Vector();
		Vector vMensT2 = new Vector();

		try {

			if ( cab.next() )
				iNumNota = cab.getInt( NF.C_DOC );

			for ( int i = 0; i < 6; i++ ) {
				if ( bFat ) {
					if ( parc.next() ) {
						sDuplics[ i ] = iNumNota + " / " + parc.getInt( NF.C_NPARCITREC );
						sVencs[ i ] = ( parc.getDate( NF.C_DTVENCTO ) != null ? Funcoes.dateToStrDate( parc.getDate( NF.C_DTVENCTO ) ) : "" );
						sVals[ i ] = Funcoes.strDecimalToStrCurrency( 12, 2, "" + parc.getFloat( NF.C_VLRPARC ) );
					}
					else {
						bFat = false;
						sDuplics[ i ] = "";
						sVencs[ i ] = "";
						sVals[ i ] = "";
					}
				}
				else {
					bFat = false;
					sVencs[ i ] = "";
					sVals[ i ] = "";
				}
			}

			imp.limpaPags();
			while ( itens.next() ) {

				if ( bNat ) {
					sNat[ 0 ] = itens.getString( NF.C_DESCNAT );
					sNat[ 1 ] = Funcoes.setMascara( itens.getString( NF.C_CODNAT ), "#.###" );
					bNat = false;
				}

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

					sIncra = cab.getString( NF.C_INCRAEMIT );
					sImpDtSaidaNat = itens.getString( NF.C_IMPDTSAIDA );

					imp.pulaLinha( 1, imp.comprimido() );

					if ( nf.getTipoNF() == NF.TPNF_ENTRADA )
						imp.say( 108, "X" );
					else
						imp.say( 95, "X" );

					imp.say( 128, StringFunctions.strZero( String.valueOf( iNumNota ), 6 ) );
					imp.pulaLinha( 3, imp.comprimido() );
					imp.say( 6, sNat[ 0 ] );
					imp.say( 51, sNat[ 1 ] );
					imp.pulaLinha( 3, imp.comprimido() );

					if ( !sIncra.equals( "" ) ) {
						imp.say( 6, cab.getInt( NF.C_CODEMIT ) + " - " + sValsCli[ 1 ] + "Incra:" );
						imp.say( 73, cab.getString( NF.C_INCRAEMIT ) );
					}
					else
						imp.say( imp.pRow(), 6, cab.getInt( NF.C_CODEMIT ) + " - " + sValsCli[ 1 ] );

					imp.say( 96, !sValsCli[ 0 ].equals( "" ) ? Funcoes.setMascara( sValsCli[ 0 ], "###.###.###-##" ) : Funcoes.setMascara( cab.getString( NF.C_CNPJEMIT ), "##.###.###/####-##" ) );
					imp.say( 126, ( cab.getDate( NF.C_DTEMITPED ) != null ? Funcoes.dateToStrDate( cab.getDate( NF.C_DTEMITPED ) ) : "" ) );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 6, Funcoes.copy( cab.getString( NF.C_ENDEMIT ), 0, 50 ).trim() + ", " + Funcoes.copy( cab.getString( NF.C_NUMEMIT ), 0, 6 ).trim() + " - " + Funcoes.copy( cab.getString( NF.C_COMPLEMIT ), 0, 9 ).trim() );
					imp.say( 85, Funcoes.copy( cab.getString( NF.C_BAIREMIT ), 0, 20 ) );
					imp.say( 108, Funcoes.setMascara( cab.getString( NF.C_CEPEMIT ), "#####-###" ) );

					if ( !sImpDtSaidaNat.equals( "N" ) )
						imp.say( 126, ( cab.getDate( NF.C_DTSAIDA ) != null ? Funcoes.dateToStrDate( cab.getDate( NF.C_DTSAIDA ) ) : "" ) );

					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 6, sValsCli[ 2 ] );
					imp.say( 65, ( !cab.getString( NF.C_DDDEMIT ).equals( "" ) ? "(" + cab.getString( NF.C_DDDEMIT ) + ")" : "" ) + ( !cab.getString( NF.C_FONEEMIT ).equals( "" ) ? Funcoes.setMascara( cab.getString( NF.C_FONEEMIT ).trim(), "####-####" ) : "" ).trim() );
					imp.say( 89, sValsCli[ 3 ] );
					imp.say( 96, !cab.getString( NF.C_RGEMIT ).equals( "" ) ? cab.getString( NF.C_RGEMIT ) : cab.getString( NF.C_INSCEMIT ) );
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 6, sDuplics[ 0 ] );
					imp.say( 30, sVencs[ 0 ] );
					imp.say( 55, sVals[ 0 ] );
					imp.say( 80, sDuplics[ 1 ] );
					imp.say( 105, sVencs[ 1 ] );
					imp.say( 125, sVals[ 1 ] );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 6, sDuplics[ 2 ] );
					imp.say( 30, sVencs[ 2 ] );
					imp.say( 55, sVals[ 2 ] );
					imp.say( 80, sDuplics[ 3 ] );
					imp.say( 105, sVencs[ 3 ] );
					imp.say( 125, sVals[ 3 ] );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 6, sDuplics[ 4 ] );
					imp.say( 30, sVencs[ 4 ] );
					imp.say( 55, sVals[ 4 ] );
					imp.say( 80, sDuplics[ 5 ] );
					imp.say( 105, sVencs[ 5 ] );
					imp.say( 125, sVals[ 5 ] );
					imp.pulaLinha( 2, imp.comprimido() );

				}

				// Descrições adicionais colocadas junto a decrição do produto.

				sDescAdic = "";
				// Gambs para colocar o lote:
				if ( ( itens.getDate( NF.C_VENCLOTE ) != null ) && ( !itens.getString( NF.C_CODLOTE ).equals( "" ) ) )
					sDescAdic = "  - L.:" + itens.getString( NF.C_CODLOTE ).trim() + ", VC.:" + Funcoes.dateToStrDate( itens.getDate( NF.C_VENCLOTE ) ).substring( 3 );

				sTmp = !itens.getString( NF.C_DESCFISC ).equals( "" ) ? itens.getString( NF.C_DESCFISC ).trim() : "";
				// Gambs para colocar arteriscos:
				if ( sTmp.length() > 0 ) {
					int i = 0;
					for ( ; i < vMensT1.size(); i++ ) {
						if ( ( (String[]) vMensT1.elementAt( i ) )[ 1 ].equals( sTmp ) && ( (String[]) vMensT1.elementAt( i ) )[ 0 ].indexOf( "*" ) == 0 ) {
							sDescAdic += " " + ( (String[]) vMensT1.elementAt( i ) )[ 0 ];
							break;
						}
					}
					if ( i == vMensT1.size() ) {
						vMensT1.add( new String[] { StringFunctions.replicate( "*", iContaAst++ ), sTmp } );
						sDescAdic += " " + ( (String[]) vMensT1.elementAt( i ) )[ 0 ];
						vMens.add( ( (String[]) vMensT1.elementAt( i ) )[ 0 ] + " - " + ( (String[]) vMensT1.elementAt( i ) )[ 1 ] );
					}
				}

				sTmp = !itens.getString( NF.C_DESCFISC2 ).equals( "" ) ? itens.getString( NF.C_DESCFISC2 ).trim() : "";
				sClasFisc = Funcoes.copy( itens.getString( NF.C_ORIGFISC ), 0, 1 ) + Funcoes.copy( itens.getString( NF.C_CODTRATTRIB ), 0, 2 );
				if ( sTmp.length() > 0 ) {
					int i = 0;
					for ( ; i < vMensT2.size(); i++ ) {
						if ( ( (String[]) vMensT2.elementAt( i ) )[ 0 ].equals( sClasFisc ) ) {
							break;
						}
					}
					if ( i == vMensT2.size() ) {
						vMensT2.add( new String[] { sClasFisc, sTmp } );
						vMens.add( ( (String[]) vMensT2.elementAt( i ) )[ 0 ] + " - " + ( (String[]) vMensT2.elementAt( i ) )[ 1 ] );
					}

				}

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 2, Funcoes.alinhaDir( itens.getInt( NF.C_CODPROD ), 8 ) );
				imp.say( 15, Funcoes.copy( itens.getString( NF.C_DESCPROD ).trim(), 0, 63 - sDescAdic.length() ) + sDescAdic );
				imp.say( 83, Funcoes.copy( itens.getString( NF.C_CODCLASSFISC ), 8 ) );
				imp.say( 95, sClasFisc );
				imp.say( 100, Funcoes.copy( itens.getString( NF.C_CODUNID ), 4 ) );
				imp.say( 105, String.valueOf( itens.getFloat( NF.C_QTDITPED ) ) );
				imp.say( 106, Funcoes.strDecimalToStrCurrency( 13, 2, String.valueOf( ( new BigDecimal( itens.getFloat( NF.C_VLRLIQITPED ) ) ).divide( new BigDecimal( itens.getFloat( NF.C_QTDITPED ) ), 2, BigDecimal.ROUND_HALF_UP ) ) ) );
				imp.say( 120, Funcoes.strDecimalToStrCurrency( 13, 2, String.valueOf( itens.getFloat( NF.C_VLRLIQITPED ) ) ) );
				imp.say( 136, ( String.valueOf( (int) itens.getFloat( NF.C_PERCICMSITPED ) ) ) );

				iItImp++;
				if ( ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) || ( imp.pRow() == MAXLINE - 1 ) ) {
					if ( iContaFrete == 0 ) {
						frete.next();
						iContaFrete++;
					}
					// Ganbs para não repetir menssagem.
					if ( sObs == null ) {
						sObs = cab.getString( NF.C_OBSPED ).trim();
						vObs = Funcoes.strToVectorSilabas( sObs, 75 );
					}

					sMatObs = new String[ 5 ];
					for ( int i = 0; i < sMatObs.length; i++ ) {
						if ( iMesImp < vMens.size() )
							sMatObs[ i ] = (String) vMens.elementAt( iMesImp++ );
						else if ( iObsImp < vObs.size() )
							sMatObs[ i ] = (String) vObs.elementAt( iObsImp++ );
					}

					imp.pulaLinha( MAXLINE - imp.pRow(), imp.comprimido() );

					if ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) {

						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 4, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRBASEICMSPED ) ) ) );
						imp.say( 32, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRICMSPED ) ) ) );
						imp.say( 114, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRPRODPED ) ) ) );
						imp.pulaLinha( 2, imp.comprimido() );
						imp.say( 4, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( frete.getFloat( NF.C_VLRFRETEPED ) ) ) );
						imp.say( 58, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( itens.getFloat( NF.C_VLRADICITPED ) ) ) );
						imp.say( 87, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRIPIPED ) ) ) );
						imp.say( 114, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRLIQPED ) ) ) );
						iItImp = 0;

					}
					else if ( imp.pRow() == MAXLINE ) {
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 4, "***************" );
						imp.say( 32, "***************" );
						imp.say( 114, "***************" );
						imp.pulaLinha( 2, imp.comprimido() );
						imp.say( 4, "***************" );
						imp.say( 58, "***************" );
						imp.say( 87, "***************" );
						imp.say( 114, "***************" );

					}

					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 6, frete.getString( NF.C_RAZTRANSP ) );
					imp.say( 85, frete.getString( NF.C_TIPOFRETE ).equals( "C" ) ? "1" : "2" );
					imp.say( 95, frete.getString( NF.C_PLACAFRETE ) );
					imp.say( 111, frete.getString( NF.C_UFFRETE ) );

					sTipoTran = frete.getString( NF.C_TIPOTRANSP );

					if ( sTipoTran.equals( "C" ) ) {
						imp.say( 116, Funcoes.setMascara( cab.getString( NF.C_CNPJEMIT ), "##.###.###/####-##" ) );
					}
					else {
						imp.say( 116, Funcoes.setMascara( frete.getString( NF.C_CNPJTRANSP ), "##.###.###/####-##" ) );
					}

					imp.pulaLinha( 2, imp.comprimido() );

					if ( sTipoTran.equals( "C" ) ) {
						imp.say( 6, Funcoes.copy( frete.getString( NF.C_ENDEMIT ), 0, 42 ) + ", " + Funcoes.copy( frete.getString( NF.C_NUMEMIT ), 0, 6 ) );
						imp.say( 75, frete.getString( NF.C_CIDEMIT ) );
						imp.say( 111, frete.getString( NF.C_UFEMIT ) );
					}
					else {
						imp.say( 6, Funcoes.copy( frete.getString( NF.C_ENDTRANSP ), 0, 42 ) + ", " + Funcoes.copy( frete.getString( NF.C_NUMTRANSP ), 0, 6 ) );
						imp.say( 75, frete.getString( NF.C_CIDTRANSP ) );
						imp.say( 111, frete.getString( NF.C_UFTRANSP ) );
					}

					if ( frete.getString( NF.C_TIPOTRANSP ).equals( "C" ) )
						imp.say( 116, cab.getString( NF.C_INSCEMIT ) );
					else
						imp.say( 116, frete.getString( NF.C_INSCTRANSP ) );

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 6, Funcoes.strDecimalToStrCurrency( 5, casasDec, String.valueOf( frete.getString( NF.C_QTDFRETE ) ) ) );
					imp.say( 26, frete.getString( NF.C_ESPFRETE ) );
					imp.say( 47, frete.getString( NF.C_MARCAFRETE ) );
					imp.say( 93, Funcoes.strDecimalToStrCurrency( 5, casasDec, String.valueOf( frete.getFloat( NF.C_PESOBRUTO ) ) ) );
					imp.say( 120, Funcoes.strDecimalToStrCurrency( 5, casasDec, String.valueOf( frete.getString( NF.C_PESOLIQ ) ) ) );
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 12, cab.getString( NF.C_CODVEND ) );
					imp.say( 21, cab.getString( NF.C_CODCLCOMIS ) );
					imp.say( 30, Funcoes.strDecimalToStrCurrency( 5, 2, String.valueOf( Funcoes.arredFloat( cab.getFloat( NF.C_PERCMCOMISPED ), 2 ) ) ).toString() );
					imp.say( 48, String.valueOf( cab.getInt( NF.C_CODPED ) ) );
					imp.say( 66, String.valueOf( iNumNota ) );
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 4, sMatObs[ 0 ] );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 4, sMatObs[ 1 ] );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 4, sMatObs[ 2 ] );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 4, sMatObs[ 3 ] );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 4, sMatObs[ 4 ] );
					imp.pulaLinha( 5, imp.comprimido() );
					imp.say( 128, StringFunctions.strZero( String.valueOf( iNumNota ), 6 ) );

					imp.pulaLinha( iLinPag - imp.pRow(), imp.comprimido() );

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
			sTipoTran = null;
			sObs = null;
			sTmp = null;
			sDescAdic = null;
			sImpDtSaidaNat = null;
			sIncra = null;
			sClasFisc = null;
			sValsCli = null;
			sNat = null;
			sDuplics = null;
			sVencs = null;
			sVals = null;
			sMatObs = null;
			vObs = null;
			vMens = null;
			vMensT1 = null;
			vMensT2 = null;
			System.gc();
		}

		return retorno;

	}

}
