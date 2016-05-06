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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.business.component.NF;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.component.Layout;
import org.freedom.library.functions.Funcoes;

public class NF047 extends Layout {

	public boolean imprimir( NF nf, ImprimeOS imp ) {

		boolean retorno = super.imprimir( nf, imp );
		boolean bFat = true;
		boolean bNat = true;
		boolean bjatem = false;
		boolean bvlriss = true;
		final int MAXLINE = 37;
		final int MAXPROD = 20;
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
		Vector<String> vObsVenda = new Vector<String>();
		Map<String, String> clfiscal = new HashMap<String, String>();
		// Vector<String> vClfisc = new Vector<String>();
		// Vector<String> vSigla = new Vector<String>();
		Vector<?> vDescServ = new Vector<Object>();
		Vector<Object[]> vServico = new Vector<Object[]>();
		Calendar cHora = Calendar.getInstance();
		String sHora = StringFunctions.strZero( String.valueOf( cHora.get( Calendar.HOUR_OF_DAY ) ), 2 ) + ":" + StringFunctions.strZero( String.valueOf( cHora.get( Calendar.MINUTE ) ), 2 ) + ":" + StringFunctions.strZero( String.valueOf( cHora.get( Calendar.SECOND ) ), 2 );

		imp.setMargem( 0 );

		try {

			if ( cab.next() ) {
				iNumNota = cab.getInt( NF.C_DOC );
				sObsVenda = cab.getString( NF.C_OBSPED ).replace( "\n", "" );
			}

			for ( int i = 0; i < 9; i++ ) {
				if ( bFat ) {
					if ( parc.next() ) {
						sDuplics[ i ] = StringFunctions.strZero( String.valueOf( iNumNota ), 6 ) + " / " + parc.getInt( NF.C_NPARCITREC );
						sVencs[ i ] = ( parc.getDate( NF.C_DTVENCTO ) != null ? Funcoes.dateToStrDate( parc.getDate( NF.C_DTVENCTO ) ) : "" );
						sVals[ i ] = Funcoes.strDecimalToStrCurrency( 12, 2, String.valueOf( parc.getFloat( NF.C_VLRPARC ) ) );
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

			// vClfisc.addElement( "" );

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

					// Imprime cabeçalho da nota

					imp.pulaLinha( 2, imp.comprimido() );

					if ( nf.getTipoNF() == NF.TPNF_ENTRADA ) {
						imp.say( 120, "X" );
					}
					else {
						imp.say( 96, "X" );
					}

					imp.say( 146, StringFunctions.strZero( String.valueOf( iNumNota ), 6 ) );
					imp.pulaLinha( 6, imp.comprimido() );
					imp.say( 0, sNat[ 0 ] ); // Descrição da natureza da operação
					imp.say( 40, sNat[ 1 ] ); // CFOP
					imp.pulaLinha( 3, imp.comprimido() );
					imp.say( 0, sValsCli[ 1 ] ); // Razão do cliente
					imp.say( 96, !sValsCli[ 0 ].equals( "" ) ? Funcoes.setMascara( sValsCli[ 0 ], "###.###.###-##" ) : Funcoes.setMascara( cab.getString( NF.C_CNPJEMIT ), "##.###.###/####-##" ) );
					imp.say( 144, ( cab.getDate( NF.C_DTEMITPED ) != null ? Funcoes.dateToStrDate( cab.getDate( NF.C_DTEMITPED ) ) : "" ) );
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 0, Funcoes.copy( cab.getString( NF.C_ENDEMIT ), 0, 50 ).trim() + ", " + Funcoes.copy( cab.getString( NF.C_NUMEMIT ), 0, 6 ).trim() + " - " + Funcoes.copy( cab.getString( NF.C_COMPLEMIT ), 0, 9 ).trim() );
					imp.say( 76, Funcoes.copy( cab.getString( NF.C_BAIREMIT ), 0, 23 ) );
					imp.say( 116, Funcoes.setMascara( cab.getString( NF.C_CEPEMIT ), "#####-###" ) );

					if ( !itens.getString( NF.C_IMPDTSAIDA ).equals( "N" ) ) {
						imp.say( 144, ( cab.getDate( NF.C_DTSAIDA ) != null ? Funcoes.dateToStrDate( cab.getDate( NF.C_DTSAIDA ) ) : "" ) );
					}

					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 0, sValsCli[ 2 ] ); // Municipio
					imp.say( 56, ( !cab.getString( NF.C_DDDEMIT ).equals( "" ) ? "(" + cab.getString( NF.C_DDDEMIT ) + ")" : "" ) + ( !cab.getString( NF.C_FONEEMIT ).equals( "" ) ? Funcoes.setMascara( cab.getString( NF.C_FONEEMIT ).trim(), "####-####" ) : "" ).trim() );
					imp.say( 90, sValsCli[ 3 ] ); // UF
					imp.say( 96, !cab.getString( NF.C_RGEMIT ).equals( "" ) ? cab.getString( NF.C_RGEMIT ) : cab.getString( NF.C_INSCEMIT ) );
					imp.say( 144, sHora );

					imp.pulaLinha( 3, imp.comprimido() );

					// Fim do cabeçalho

					imp.say( 0, sDuplics[ 0 ] );
					imp.say( 24, sVencs[ 0 ] );
					imp.say( 52, sVals[ 0 ] );
					imp.say( 86, sDuplics[ 1 ] );
					imp.say( 111, sVencs[ 1 ] );
					imp.say( 138, sVals[ 1 ] );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, sDuplics[ 2 ] );
					imp.say( 24, sVencs[ 2 ] );
					imp.say( 52, sVals[ 2 ] );
					imp.say( 86, sDuplics[ 3 ] );
					imp.say( 111, sVencs[ 3 ] );
					imp.say( 138, sVals[ 3 ] );
					imp.pulaLinha( 2, imp.comprimido() );

					// Fim dos dados da fatura

				}

				// Monta a menssagem fiscal ...

				sTemp = itens.getString( NF.C_DESCFISC ).trim();
				if ( sDescFisc.indexOf( sTemp ) == -1 ) {
					sDescFisc += sTemp;
				}
				sTemp = itens.getString( NF.C_DESCFISC2 ).trim();
				if ( sDescFisc.indexOf( sTemp ) == -1 ) {
					sDescFisc += sTemp;
				}

				// Fim da menssagem fiscal ...

				// Definição da sigla para a classificação fiscal

				sCodfisc = itens.getString( NF.C_CODFISC );
				if ( clfiscal.get( sCodfisc ) == null ) {
					clfiscal.put( sCodfisc, String.valueOf( (char) ( 65 + clfiscal.size() ) ) );
				}

				sSigla = clfiscal.get( sCodfisc );

				/*
				 * if ( !sCodfisc.equals( "" ) ) { for ( int i = 0; i < vClfisc.size(); i++ ) { if ( vClfisc.elementAt( i ) != null ) { if ( sCodfisc.equals( vClfisc.elementAt( i ) ) ) { bjatem = true; sSigla = String.valueOf( (char) ( 64 + i ) ); } else { bjatem = false; } } } if ( !bjatem ) {
				 * vClfisc.addElement( sCodfisc ); sSigla = String.valueOf( (char) ( 63 + vClfisc.size() ) ); vSigla.addElement( sSigla + " = " + sCodfisc ); } }
				 */

				// Fim da classificação fiscal

				// Imprime os dados do item no corpo da nota

				BigDecimal vlricmsorig = new BigDecimal( 0 );

				if ( !"S".equals( itens.getString( NF.C_TIPOPROD ) ) ) {

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, itens.getString( NF.C_CODBAR ) );
					imp.say( 16, Funcoes.copy( itens.getString( NF.C_DESCPROD ).trim(), 50 ) );
					imp.say( 77, sSigla ); // cf
					imp.say( 81, Funcoes.copy( itens.getString( NF.C_ORIGFISC ), 0, 1 ) + Funcoes.copy( itens.getString( NF.C_CODTRATTRIB ), 0, 2 ) );
					imp.say( 86, Funcoes.copy( itens.getString( NF.C_CODUNID ), 4 ) );
					imp.say( 91, Funcoes.strDecimalToStrCurrency( 4, 0, String.valueOf( itens.getFloat( NF.C_QTDITPED ) ) ) );
					imp.say( 98, Funcoes.strDecimalToStrCurrency( 12, 2, String.valueOf( itens.getFloat( NF.C_VLRPRODITPED ) / itens.getFloat( NF.C_QTDITPED ) ) ) );
					imp.say( 115, Funcoes.strDecimalToStrCurrency( 12, 2, String.valueOf( itens.getFloat( NF.C_VLRPRODITPED ) ) ) );
					imp.say( 128, Funcoes.strDecimalToStrCurrency( 12, 2, String.valueOf( itens.getFloat( NF.C_VLRDESCITPROD ) ) ) );
					imp.say( 141, Funcoes.strDecimalToStrCurrency( 5, 2, String.valueOf( itens.getFloat( NF.C_PERCICMSITPED ) ) ) );
					imp.say( 148, Funcoes.strDecimalToStrCurrency( 5, 2, String.valueOf( itens.getFloat( NF.C_PERCIPIITPED ) ) ) );
					imp.say( 154, Funcoes.strDecimalToStrCurrency( 6, 2, String.valueOf( itens.getFloat( NF.C_VLRIPIITPED ) ) ) );
					iProdImp++;

					vlricmsorig = vlricmsorig.add( ( new BigDecimal( itens.getFloat( NF.C_PERCICMSITPED ) ).multiply( new BigDecimal( itens.getFloat( NF.C_VLRPRODITPED ) ).divide( new BigDecimal( 100 ) ) ) ) );

					// xxx
				}

				// Fim da impressão do item

				iItImp++;
				if ( ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) || ( imp.pRow() == MAXLINE - 1 ) ) {

					if ( iContaFrete == 0 ) {
						frete.next();
						iContaFrete++;
					}

					imp.pulaLinha( MAXLINE - imp.pRow(), imp.comprimido() );

					// Imprime totais

					if ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) {
						imp.pulaLinha( 3, imp.comprimido() );
						imp.say( 0, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRBASEICMSPED ) ) ) );
						imp.say( 31, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRICMSPED ) ) ) );
						imp.say( 128, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRPRODPED ) ) ) );
						imp.pulaLinha( 2, imp.comprimido() );
						imp.say( 0, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( ( nf.getTipoNF() == NF.TPNF_ENTRADA ? cab.getFloat( NF.C_VLRFRETEPED ) : frete.getFloat( NF.C_VLRFRETEPED ) ) ) ) );

						imp.say( 63, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRADICPED ) ) ) );
						imp.say( 96, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRIPIPED ) ) ) );

						if ( nf.getTipoNF() == NF.TPNF_ENTRADA ) {
							imp.say( 109, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRPRODPED ) + cab.getFloat( NF.C_VLRIPIPED ) + cab.getFloat( NF.C_VLRADICPED ) + cab.getFloat( NF.C_VLRFRETEPED ) ) ) );
						}
						else {
							imp.say( 128, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRLIQPED ) ) ) );
						}
						iItImp = 0;

					}
					else {
						imp.pulaLinha( 0, imp.comprimido() );
						imp.say( 0, "********************" );
						imp.say( 22, "********************" );
						imp.say( 108, "********************" );
						imp.pulaLinha( 2, imp.comprimido() );
						imp.say( 0, "********************" );
						imp.say( 55, "********************" );
						imp.say( 84, "********************" );
						imp.say( 109, "********************" );
					}

					// Fim da impressão dos totais

					// Imprime informações do frete

					imp.pulaLinha( 3, imp.comprimido() );
					imp.say( 0, frete.getString( NF.C_RAZTRANSP ) );
					imp.say( 86, "C".equals( frete.getString( NF.C_TIPOFRETE ) ) ? "1" : "2" );
					imp.say( 114, frete.getString( NF.C_UFFRETE ) );

					if ( "C".equals( frete.getString( NF.C_TIPOTRANSP ) ) ) {
						imp.say( 120, Funcoes.setMascara( cab.getString( NF.C_CNPJEMIT ), "##.###.###/####-##" ) );
					}
					else {
						if ( "".equals( frete.getString( NF.C_CNPJTRANSP ) ) ) {
							imp.say( 120, Funcoes.setMascara( frete.getString( NF.C_CPFTRANSP ), "###.###.###-##" ) );
						}
						else {
							imp.say( 120, Funcoes.setMascara( frete.getString( NF.C_CNPJTRANSP ), "##.###.###/####-##" ) );
						}
					}

					imp.pulaLinha( 2, imp.comprimido() );
					if ( frete.getString( NF.C_ENDTRANSP ) != null ) {
						imp.say( 0, frete.getString( NF.C_ENDTRANSP ).trim() + ( ! ( frete.getString( NF.C_ENDTRANSP ) == null ) ? ( ", " + frete.getInt( NF.C_NUMTRANSP ) ) : "" ) );
						imp.say( 72, frete.getString( NF.C_CIDTRANSP ) );
						imp.say( 114, frete.getString( NF.C_UFTRANSP ) );
					}

					if ( frete.getString( NF.C_TIPOTRANSP ) != null && frete.getString( NF.C_TIPOTRANSP ).equals( "C" ) ) {
						imp.say( 120, cab.getString( NF.C_INSCEMIT ) );
					}
					else {
						imp.say( 120, frete.getString( NF.C_INSCTRANSP ) );
					}

					imp.pulaLinha( 2, imp.comprimido() );
					if ( frete.getString( NF.C_QTDFRETE ) != null )
						imp.say( 0, Funcoes.strDecimalToStrCurrency( 12, 2, String.valueOf( frete.getString( NF.C_QTDFRETE ) ) ) );
					imp.say( 22, Funcoes.copy( frete.getString( NF.C_ESPFRETE ), 27 ) );
					imp.say( 62, Funcoes.copy( frete.getString( NF.C_MARCAFRETE ), 27 ) );
					imp.say( 94, StringFunctions.strZero( String.valueOf( iNumNota ), 6 ) );
					imp.say( 123, Funcoes.copy( frete.getString( NF.C_PESOBRUTO ), 4 ) + "KG" );
					imp.say( 144, Funcoes.copy( frete.getString( NF.C_PESOLIQ ), 4 ) + "KG" );

					imp.pulaLinha( 1, imp.comprimido() );

					// Fim da impressão do frete

					// Imprime observação e classificações fiscais

					vObsVenda = Funcoes.strToVectorSilabas( ( sDescFisc.length() > 0 ? sDescFisc + "\n" : "" ) + sObsVenda, 70 );
					vObsVenda.addElement( "PRAZO DE PAGAMENTO: " + cab.getString( NF.C_DESCPLANOPAG ) );

					// sizeObs = vSigla.size();
					sizeObs = clfiscal.size();
					sizeObs = vObsVenda.size() > sizeObs ? vObsVenda.size() : sizeObs;

					imp.pulaLinha( 1, imp.comprimido() );
					int colCl = 0;
					int linCl = 0;
					Object[] codclfiscal = clfiscal.keySet().toArray();
					Object[] posclfiscal = clfiscal.values().toArray();
					for ( int i = 0; i < 6; i++ ) {
						if ( i < posclfiscal.length ) {
							if ( i == 0 ) {
								imp.pulaLinha( 1, imp.comprimido() );
								linCl++;
							}
							imp.say( colCl, String.valueOf( posclfiscal[ i ] ) + " = " + String.valueOf( codclfiscal[ i ] ) );
							colCl += 20;
						}
						if ( colCl == 60 ) {
							colCl = 0;
						}
					}
					/*
					 * for ( int i=0; i<6; i++ ) { if (i<vSigla.size()) { if (i==0) { imp.pulaLinha( 1, imp.comprimido() ); linCl ++; } imp.say( colCl , ((String) vSigla.elementAt( i )).trim()); colCl += 20; } if (colCl==60) { colCl = 0; } }
					 */
					linCl++;
					imp.pulaLinha( 1, imp.comprimido() );
					for ( int i = 0; i < 4; i++ ) {
						if ( i < vObsVenda.size() ) {
							imp.say( 0, Funcoes.copy( (String) vObsVenda.elementAt( i ), 70 ) );
						}
						imp.pulaLinha( 1, imp.comprimido() );
						linCl++;
					}

					Float vlrdiferido = vlricmsorig.subtract( new BigDecimal( cab.getFloat( NF.C_VLRICMSPED ) ).setScale( 2, BigDecimal.ROUND_CEILING )

					).floatValue();

					// imp.say( 2, "VALOR DO ICMS DIFERIDO:" + Funcoes.strDecimalToStrCurrency(10,2,String.valueOf( vlrdiferido )));

					// Fim da observação

					imp.pulaLinha( 10 - linCl, "" );
					imp.say( imp.pRow() + 1, imp.comprimido() );
					imp.say( 146, StringFunctions.strZero( String.valueOf( iNumNota ), 6 ) );

					// Imprime canhoto

					imp.pulaLinha( 4, imp.comprimido() );

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
