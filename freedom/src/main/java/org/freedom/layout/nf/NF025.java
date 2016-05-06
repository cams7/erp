/**
 * @version 06/06/2006 <BR>
 * @author Setpoint Informática Ltda./Alex Rodrigues <BR>
 * 
 *         Projeto: Freedom <BR>
 *         Pacote: layout <BR>
 *         Classe:
 * @(#)NF025.java <BR>
 * 
 *                Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                Layout da nota fiscal para a empresa 025.
 */

package org.freedom.layout.nf;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Vector;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.business.component.NF;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.component.Layout;
import org.freedom.library.functions.Funcoes;

public class NF025 extends Layout {

	public boolean imprimir( NF nf, ImprimeOS imp ) {

		boolean retorno = super.imprimir( nf, imp );
		boolean bFat = true;
		boolean bNat = true;
		final int MAXLINE = 50;
		final int MAXOBS = 75;
		int iNumNota = 0;
		int iItImp = 0;
		int iContaFrete = 0;
		int iLinPag = imp.verifLinPag( "NF" );
		int PARC = 6;
		int indexDescFisc = 0;
		int indexObs = 0;
		String sTemp = null;
		StringBuffer sDescFisc = new StringBuffer();
		String[] sValsCli = new String[ 4 ];
		String[] sNat = new String[ 2 ];
		String[] sDuplic = new String[ PARC ];
		String[] sVencs = new String[ PARC ];
		String[] sVals = new String[ PARC ];
		BigDecimal bdQtdItem = new BigDecimal( 0 );
		BigDecimal bdVlrItem = new BigDecimal( 0 );
		BigDecimal bdVlrLiqItem = new BigDecimal( 0 );
		Vector<?> vObs = new Vector<Object>();
		Vector<?> vDescFisc = new Vector<Object>();

		try {

			if ( cab.next() ) {
				iNumNota = cab.getInt( NF.C_DOC );
				vObs = Funcoes.strToVectorSilabas( cab.getString( NF.C_OBSPED ), MAXOBS );
			}

			for ( int i = 0; i < PARC; i++ ) {
				if ( bFat ) {
					if ( parc.next() ) {
						sDuplic[ i ] = String.valueOf( parc.getInt( NF.C_NPARCITREC ) );
						sVencs[ i ] = ( parc.getDate( NF.C_DTVENCTO ) != null ? Funcoes.dateToStrDate( parc.getDate( NF.C_DTVENCTO ) ) : "" );
						sVals[ i ] = Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( parc.getFloat( NF.C_VLRPARC ) ) );
					}
					else {
						bFat = false;
					}
				}
				else {
					bFat = false;
				}
			}

			imp.limpaPags();

			while ( itens.next() ) {

				if ( bNat ) {
					sNat[ 0 ] = Funcoes.copy( itens.getString( NF.C_DESCNAT ).trim(), 32 );
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

					// Imprime cabeçalho da nota ...

					imp.pulaLinha( 1, imp.normal() );

					if ( nf.getTipoNF() == NF.TPNF_ENTRADA ) {
						imp.say( 60, "X" );
					}
					else {
						imp.say( 49, "X" );
					}

					imp.say( 73, StringFunctions.strZero( String.valueOf( iNumNota ), 7 ) );
					imp.pulaLinha( 5, imp.comprimido() );
					imp.say( 0, sNat[ 0 ] );
					imp.say( 43, sNat[ 1 ] );
					imp.pulaLinha( 3, imp.comprimido() );
					imp.say( 0, sValsCli[ 1 ] );
					imp.say( 88, !sValsCli[ 0 ].equals( "" ) ? Funcoes.setMascara( sValsCli[ 0 ], "###.###.###-##" ) : Funcoes.setMascara( cab.getString( NF.C_CNPJEMIT ), "##.###.###/####-##" ) );
					imp.say( 124, ( cab.getDate( NF.C_DTEMITPED ) != null ? Funcoes.dateToStrDate( cab.getDate( NF.C_DTEMITPED ) ) : "" ) );
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 0, Funcoes.copy( cab.getString( NF.C_ENDEMIT ), 0, 50 ).trim() + ", " + Funcoes.copy( cab.getString( NF.C_NUMEMIT ), 0, 6 ).trim() + " - " + Funcoes.copy( cab.getString( NF.C_COMPLEMIT ), 0, 9 ).trim() );
					imp.say( 62, Funcoes.copy( cab.getString( NF.C_BAIREMIT ), 0, 23 ) );
					imp.say( 100, Funcoes.setMascara( cab.getString( NF.C_CEPEMIT ), "#####-###" ) );

					if ( !itens.getString( NF.C_IMPDTSAIDA ).equals( "N" ) ) {
						imp.say( 124, ( cab.getDate( NF.C_DTSAIDA ) != null ? Funcoes.dateToStrDate( cab.getDate( NF.C_DTSAIDA ) ) : "" ) );
					}

					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 0, sValsCli[ 2 ] );
					imp.say( 50, ( !cab.getString( NF.C_DDDEMIT ).equals( "" ) ? "(" + cab.getString( NF.C_DDDEMIT ) + ")" : "" ) + ( !cab.getString( NF.C_FONEEMIT ).equals( "" ) ? Funcoes.setMascara( cab.getString( NF.C_FONEEMIT ).trim(), "####-####" ) : "" ).trim() );
					imp.say( 76, sValsCli[ 3 ] );
					imp.say( 88, !cab.getString( NF.C_RGEMIT ).equals( "" ) ? cab.getString( NF.C_RGEMIT ) : cab.getString( NF.C_INSCEMIT ) );
					imp.say( 125, Funcoes.getTimeString( Calendar.getInstance().getTime() ) );

					// Fim do cabeçalho ...

					// Imprime dados da fatura ...

					imp.pulaLinha( 3, imp.comprimido() );
					int pos = 0;
					for ( int i = 0; i < PARC; i++ ) {
						if ( i == ( PARC / 2 ) ) {
							imp.pulaLinha( 1, imp.comprimido() );
							pos = 0;
						}

						imp.say( pos, sDuplic[ i ] != null ? sDuplic[ i ] + " | " + sVencs[ i ] + " - " + sVals[ i ] : "" );
						pos += 45;
					}

					imp.pulaLinha( 3, imp.comprimido() );

					// Fim dos dados da fatura ...

				}

				// Monta a observação ...

				sTemp = itens.getString( NF.C_DESCFISC ).trim();

				if ( !sTemp.equals( "" ) ) {
					if ( sDescFisc.length() > 0 ) {
						if ( ! ( sDescFisc.indexOf( sTemp ) > -1 ) ) {
							sDescFisc.append( sTemp );
						}
					}
					else {
						sDescFisc.append( sTemp );
					}
				}

				sTemp = itens.getString( NF.C_DESCFISC2 ).trim();
				if ( !sTemp.equals( "" ) ) {
					if ( sDescFisc.length() > 0 ) {
						if ( ! ( sDescFisc.indexOf( sTemp ) > -1 ) ) {
							sDescFisc.append( sTemp );
						}
					}
					else {
						sDescFisc.append( sTemp );
					}
				}

				// Fim da observação ...

				// Imprime os dados do item no corpo da nota ...

				bdQtdItem = new BigDecimal( String.valueOf( itens.getFloat( NF.C_QTDITPED ) ) ).setScale( 3, BigDecimal.ROUND_HALF_UP );
				bdVlrItem = new BigDecimal( String.valueOf( itens.getFloat( NF.C_VLRPRODITPED ) / itens.getFloat( NF.C_QTDITPED ) ) ).setScale( 2, BigDecimal.ROUND_HALF_UP );
				bdVlrLiqItem = new BigDecimal( String.valueOf( itens.getFloat( NF.C_VLRLIQITPED ) ) ).setScale( 2, BigDecimal.ROUND_HALF_UP );

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, itens.getString( NF.C_DESCPROD ) );
				imp.say( 65, itens.getString( NF.C_CODFISC ) );
				imp.say( 79, Funcoes.copy( itens.getString( NF.C_ORIGFISC ), 1 ) + Funcoes.copy( itens.getString( NF.C_CODTRATTRIB ), 2 ) );
				imp.say( 84, Funcoes.copy( itens.getString( NF.C_CODUNID ), 2 ) );
				imp.say( 89, Funcoes.strDecimalToStrCurrency( 8, 3, bdQtdItem.toString() ) );
				imp.say( 99, Funcoes.strDecimalToStrCurrency( 9, 3, bdVlrItem.toString() ) );
				imp.say( 113, Funcoes.strDecimalToStrCurrency( 11, 3, bdVlrLiqItem.toString() ) );
				imp.say( 129, ( (int) itens.getFloat( NF.C_PERCICMSITPED ) ) + "%" );
				imp.say( 133, ( (int) itens.getFloat( NF.C_PERCIPIITPED ) ) + "%" );

				// Fim da impressão do item ...

				iItImp++;
				if ( ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) || ( imp.pRow() == MAXLINE - 1 ) ) {

					if ( iContaFrete == 0 ) {
						frete.next();
						iContaFrete++;
					}

					// Imprime o desconto
					if ( cab.getFloat( NF.C_VLRDESCITPED ) > 0.0f ) {
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 94, "Valor do desconto : " + Funcoes.strDecimalToStrCurrency( 11, 3, String.valueOf( cab.getFloat( NF.C_VLRDESCITPED ) ) ) );
					}
					// Fim da impressão do desconto ...

					imp.pulaLinha( MAXLINE - imp.pRow(), imp.comprimido() );

					// Imprime totais ...

					if ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) {

						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 4, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRBASEICMSPED ) + ( "S".equals( frete.getString( NF.C_ADICFRETEBASEICM ) ) ? frete.getFloat( NF.C_VLRFRETEPED ) : 0f ) ) ) );
						imp.say( 32, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRICMSPED ) + ( "S".equals( frete.getString( NF.C_ADICFRETEBASEICM ) ) ? frete.getFloat( NF.C_VLRICMSFRETEVD ) : 0f ) ) ) );
						imp.say( 114, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRPRODPED ) ) ) );
						imp.pulaLinha( 2, imp.comprimido() );
						imp.say( 4, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( frete.getFloat( NF.C_VLRFRETEPED ) ) ) );
						imp.say( 58, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRADICPED ) ) ) );
						imp.say( 87, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRIPIPED ) ) ) );
						imp.say( 114, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRLIQPED ) ) ) );
						iItImp = 0;

					}
					else if ( imp.pRow() == MAXLINE ) {

						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 4, "********************" );
						imp.say( 32, "********************" );
						imp.say( 114, "********************" );
						imp.pulaLinha( 2, imp.comprimido() );
						imp.say( 4, "********************" );
						imp.say( 58, "********************" );
						imp.say( 87, "********************" );
						imp.say( 114, "********************" );

					}

					// Fim da impressão dos totais ...

					// Imprime informações do frete ...

					imp.pulaLinha( 3, imp.comprimido() );
					imp.say( 0, frete.getString( NF.C_RAZTRANSP ) );
					imp.say( 84, frete.getString( NF.C_TIPOFRETE ).equals( "C" ) ? "1" : "2" );
					imp.say( 90, frete.getString( NF.C_PLACAFRETE ) );
					imp.say( 104, frete.getString( NF.C_UFFRETE ) );

					if ( frete.getString( NF.C_TIPOTRANSP ).equals( "C" ) )
						imp.say( 116, Funcoes.setMascara( cab.getString( NF.C_CNPJEMIT ), "##.###.###/####-##" ) );
					else
						imp.say( 116, Funcoes.setMascara( frete.getString( NF.C_CNPJTRANSP ), "##.###.###/####-##" ) );

					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 0, frete.getString( NF.C_ENDTRANSP ).trim() + ( frete.getInt( NF.C_NUMTRANSP ) > 0 ? ", " + frete.getInt( NF.C_NUMTRANSP ) : "" ) );
					imp.say( 70, frete.getString( NF.C_CIDTRANSP ) );
					imp.say( 104, frete.getString( NF.C_UFTRANSP ) );

					if ( frete.getString( NF.C_TIPOTRANSP ).equals( "C" ) )
						imp.say( 116, cab.getString( NF.C_INSCEMIT ) );
					else
						imp.say( 116, frete.getString( NF.C_INSCTRANSP ) );

					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 6, Funcoes.strDecimalToStrCurrency( 10, 0, String.valueOf( frete.getString( NF.C_QTDFRETE ) ) ) );
					imp.say( 20, Funcoes.copy( frete.getString( NF.C_ESPFRETE ), 22 ) );
					imp.say( 46, frete.getString( NF.C_MARCAFRETE ) );
					imp.say( 95, Funcoes.strDecimalToStrCurrency( 13, 2, String.valueOf( frete.getFloat( NF.C_PESOBRUTO ) ) ) );
					imp.say( 114, Funcoes.strDecimalToStrCurrency( 13, 2, String.valueOf( frete.getString( NF.C_PESOLIQ ) ) ) );
					imp.pulaLinha( 2, imp.comprimido() );

					// Fim da impressão do frete ...

					// Imprime observação e classificações fiscais ...

					vDescFisc = Funcoes.strToVectorSilabas( sDescFisc.toString(), MAXOBS );

					for ( int i = 0; i < 6; i++ ) {
						imp.pulaLinha( 1, imp.comprimido() );
						if ( vDescFisc.size() > 0 && indexDescFisc < vDescFisc.size() )
							imp.say( 0, (String) vDescFisc.elementAt( indexDescFisc++ ) );
						else if ( vObs.size() > 0 && indexObs < vObs.size() )
							imp.say( 0, (String) vObs.elementAt( indexObs++ ) );
					}

					// Fim da observação ...

					// Imprime canhoto ...

					imp.pulaLinha( 5, imp.normal() );
					imp.say( 73, StringFunctions.strZero( String.valueOf( iNumNota ), 7 ) );

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
			sValsCli = null;
			sNat = null;
			sVencs = null;
			sVals = null;
			System.gc();
		}

		return retorno;

	}

}
