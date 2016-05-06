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

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.business.component.NF;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.component.Layout;
import org.freedom.library.functions.Funcoes;

public class NF020 extends Layout {

	public boolean imprimir( NF nf, ImprimeOS imp ) {

		boolean retorno = super.imprimir( nf, imp );
		boolean bFat = true;
		boolean bNat = true;
		boolean bjatem = false;
		boolean bjatem1 = false;
		boolean bjatem2 = false;
		final int MAXLINE = 43;
		final int MAXPROD = 12;
		int iNumNota = 0;
		int iItImp = 0;
		int iProdImp = 0;
		int iContaFrete = 0;
		int iLinPag = imp.verifLinPag( "NF" );
		int sizeObs = 0;
		int indexDescFisc = 0;
		int indexSigla = 0;
		int indexServ = 0;
		String sCodfisc = null;
		String sSigla = null;
		String sTemp = null;
		String sDescFisc = "";
		String[] sValsCli = new String[ 4 ];
		String[] sNat = new String[ 2 ];
		String[] sVencs = new String[ 6 ];
		String[] sVals = new String[ 6 ];
		String[] sDuplics = new String[ 6 ];
		BigDecimal bdVlrIssServ = new BigDecimal( "0" );
		BigDecimal bdVlrTotServ = new BigDecimal( "0" );
		Vector<String> vClfisc = new Vector<String>();
		Vector<String> vSigla = new Vector<String>();
		Vector<?> vDescFisc = new Vector<Object>();
		Vector<Object[]> vServico = new Vector<Object[]>();
		Vector<?> vDescServ = new Vector<Object>();

		try {

			if ( cab.next() ) {
				iNumNota = cab.getInt( NF.C_DOC );
			}

			for ( int i = 0; i < 6; i++ ) {
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

			vClfisc.addElement( "" );

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

					imp.pulaLinha( 3, imp.comprimido() );

					if ( nf.getTipoNF() == NF.TPNF_ENTRADA ) {
						imp.say( 81, "X" );
					}
					else {
						imp.say( 85, "X" );
					}

					imp.pulaLinha( 7, imp.comprimido() );
					imp.say( 4, sNat[ 0 ] );
					imp.say( 39, sNat[ 1 ] );
					imp.pulaLinha( 3, imp.comprimido() );
					imp.say( 4, sValsCli[ 1 ] );
					imp.say( 95, !sValsCli[ 0 ].equals( "" ) ? Funcoes.setMascara( sValsCli[ 0 ], "###.###.###-##" ) : Funcoes.setMascara( cab.getString( NF.C_CNPJEMIT ), "##.###.###/####-##" ) );
					imp.say( 124, ( cab.getDate( NF.C_DTEMITPED ) != null ? Funcoes.dateToStrDate( cab.getDate( NF.C_DTEMITPED ) ) : "" ) );
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 4, Funcoes.copy( cab.getString( NF.C_ENDEMIT ), 0, 50 ).trim() + ", " + Funcoes.copy( cab.getString( NF.C_NUMEMIT ), 0, 6 ).trim() + " - " + Funcoes.copy( cab.getString( NF.C_COMPLEMIT ), 0, 9 ).trim() );
					imp.say( 62, Funcoes.copy( cab.getString( NF.C_BAIREMIT ), 0, 23 ) );
					imp.say( 99, Funcoes.setMascara( cab.getString( NF.C_CEPEMIT ), "#####-###" ) );

					if ( !itens.getString( NF.C_IMPDTSAIDA ).equals( "N" ) ) {
						imp.say( 124, ( cab.getDate( NF.C_DTSAIDA ) != null ? Funcoes.dateToStrDate( cab.getDate( NF.C_DTSAIDA ) ) : "" ) );
					}

					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 4, sValsCli[ 2 ] );
					imp.say( 55, ( !cab.getString( NF.C_DDDEMIT ).equals( "" ) ? "(" + cab.getString( NF.C_DDDEMIT ) + ")" : "" ) + ( !cab.getString( NF.C_FONEEMIT ).equals( "" ) ? Funcoes.setMascara( cab.getString( NF.C_FONEEMIT ).trim(), "####-####" ) : "" ).trim() );
					imp.say( 78, sValsCli[ 3 ] );
					imp.say( 95, !cab.getString( NF.C_RGEMIT ).equals( "" ) ? cab.getString( NF.C_RGEMIT ) : cab.getString( NF.C_INSCEMIT ) );

					// Fim do cabeçalho

					// Imprime dados da fatura

					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 4, sDuplics[ 0 ] );
					imp.say( 20, sVals[ 0 ] );
					imp.say( 36, sVencs[ 0 ] );
					imp.say( 50, sDuplics[ 1 ] );
					imp.say( 65, sVals[ 1 ] );
					imp.say( 80, sVencs[ 1 ] );
					imp.say( 94, sDuplics[ 2 ] );
					imp.say( 110, sVals[ 2 ] );
					imp.say( 125, sVencs[ 2 ] );
					imp.pulaLinha( 3, imp.comprimido() );

					// Fim dos dados da fatura

				}

				// Monta a observação

				sTemp = itens.getString( NF.C_DESCFISC ).trim();
				if ( sDescFisc.length() > 0 ) {
					if ( sDescFisc.indexOf( sTemp ) > -1 ) {
						bjatem1 = true;
					}
					if ( !bjatem1 ) {
						sDescFisc += sDescFisc;
					}
					bjatem1 = false;
				}
				else {
					sDescFisc = sTemp;
				}

				sDescFisc = itens.getString( NF.C_DESCFISC2 ).trim();
				if ( sDescFisc.length() > 0 ) {
					if ( sDescFisc.indexOf( sTemp ) > -1 ) {
						bjatem2 = true;
					}
					if ( !bjatem2 ) {
						sDescFisc += sDescFisc;
					}
					bjatem2 = false;
				}
				else {
					sDescFisc = sTemp;
				}

				// Fim da observação

				// Definição da sigla para a classificação fiscal

				sCodfisc = itens.getString( NF.C_CODFISC );

				if ( !sCodfisc.equals( "" ) ) {
					for ( int i = 0; i < vClfisc.size(); i++ ) {
						if ( vClfisc.elementAt( i ) != null ) {
							if ( sCodfisc.equals( vClfisc.elementAt( i ) ) ) {
								bjatem = true;
								sSigla = String.valueOf( (char) ( 64 + i ) );
							}
							else {
								bjatem = false;
							}
						}
					}
					if ( !bjatem ) {
						vClfisc.addElement( sCodfisc );
						sSigla = String.valueOf( (char) ( 63 + vClfisc.size() ) );
						vSigla.addElement( sSigla + " = " + sCodfisc );
					}
				}

				// Fim da classificação fiscal

				// Imprime os dados do item no corpo da nota

				if ( !"S".equals( itens.getString( NF.C_TIPOPROD ) ) ) {

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 4, itens.getString( NF.C_REFPROD ) );
					imp.say( 11, Funcoes.copy( itens.getString( NF.C_DESCPROD ).trim(), 48 ) );
					imp.say( 60, sSigla );
					imp.say( 65, Funcoes.copy( itens.getString( NF.C_ORIGFISC ), 0, 1 ) + Funcoes.copy( itens.getString( NF.C_CODTRATTRIB ), 0, 2 ) );
					imp.say( 75, Funcoes.copy( itens.getString( NF.C_CODUNID ), 4 ) );
					imp.say( 81, Funcoes.strDecimalToStrCurrency( 8, 2, String.valueOf( itens.getFloat( NF.C_QTDITPED ) ) ) );
					imp.say( 98, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( itens.getFloat( NF.C_VLRPRODITPED ) / itens.getFloat( NF.C_QTDITPED ) ) ) );
					imp.say( 119, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( itens.getFloat( NF.C_VLRPRODITPED ) ) ) );
					// imp.say( 120, ( (int) itens.getFloat( NF.C_PERCICMSITPED ) ) + "%" );
					// imp.say( 125, ( (int) itens.getFloat( NF.C_PERCIPIITPED ) ) + "%" );
					imp.say( 130, Funcoes.strDecimalToStrCurrency( 6, 2, String.valueOf( itens.getFloat( NF.C_VLRIPIITPED ) ) ) );
					iProdImp++;
				}
				else {
					// guarda serviço;
					vServico.addElement( new Object[] { "".equals( itens.getString( NF.C_OBSITPED ).trim() ) ? itens.getString( NF.C_DESCPROD ) : itens.getString( NF.C_OBSITPED ).trim(), new BigDecimal( itens.getFloat( NF.C_QTDITPED ) ), new BigDecimal( itens.getFloat( NF.C_VLRPRODITPED ) ),
							new BigDecimal( itens.getFloat( NF.C_VLRISSITPED ) ) } );
				}

				iItImp++;

				// Fim da impressão do item

				if ( ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) || ( iProdImp == MAXPROD ) || ( imp.pRow() == MAXLINE ) ) {

					if ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) {
						if ( cab.getFloat( NF.C_VLRDESCITPED ) > 0.0f ) {
							// Imprime o desconto
							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( 8, "Valor do desconto : " + Funcoes.strDecimalToStrCurrency( 9, 2, String.valueOf( cab.getFloat( NF.C_VLRDESCITPED ) ) ) );
						}
						if ( vServico.size() > 0 && indexServ < vServico.size() ) {
							imp.pulaLinha( 33 - imp.pRow(), imp.comprimido() );
						}
						else {
							imp.pulaLinha( MAXLINE - imp.pRow(), imp.comprimido() );
						}
					}

					if ( vServico.size() > 0 ) {
						// Imprime serviço
						iProdImp = 0;
						int contaLinha = 0;
						for ( int i = 0; i < vServico.size(); i++ ) {
							bdVlrTotServ = bdVlrTotServ.add( (BigDecimal) vServico.get( i )[ 2 ] ).setScale( 2, BigDecimal.ROUND_HALF_UP );
							bdVlrIssServ = bdVlrIssServ.add( (BigDecimal) vServico.get( i )[ 3 ] ).setScale( 2, BigDecimal.ROUND_HALF_UP );
						}
						for ( int i = 0; i < vServico.size(); i++ ) {
							vDescServ = Funcoes.strToVectorSilabas( (String) vServico.get( indexServ )[ 0 ], 80 );
							for ( int j = 0; j < vDescServ.size() && contaLinha < 10; j++ ) {
								contaLinha++;
								imp.pulaLinha( 1, imp.comprimido() );
								imp.say( 4, (String) vDescServ.elementAt( j ) );
								if ( j == 0 ) {
									imp.say( 87, Funcoes.strDecimalToStrCurrency( 8, 2, String.valueOf( (BigDecimal) vServico.get( indexServ )[ 1 ] ) ) );
									imp.say( 100, Funcoes.strDecimalToStrCurrency( 12, 2, String.valueOf( (BigDecimal) vServico.get( indexServ )[ 2 ] ) ) );
								}
								if ( contaLinha == 6 ) {
									imp.say( 120, Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( bdVlrIssServ ) ) );
								}
							}
							indexServ++;
						}
						imp.pulaLinha( 3 - contaLinha, imp.comprimido() );
						imp.say( 120, Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( bdVlrTotServ ) ) );
					}

					// Imprime totais

					if ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) {
						imp.pulaLinha( 41 - imp.pRow(), imp.comprimido() );
						imp.say( 4, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRBASEICMSPED ) ) ) );
						imp.say( 32, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRICMSPED ) ) ) );
						imp.say( 114, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( itens.getFloat( NF.C_VLRPRODITPED ) ) ) );
						imp.pulaLinha( 2, imp.comprimido() );
						imp.say( 4, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( frete.getFloat( NF.C_VLRFRETEPED ) ) ) );
						imp.say( 58, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( itens.getFloat( NF.C_VLRADICITPED ) ) ) );
						imp.say( 87, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRIPIPED ) ) ) );
						imp.say( 114, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRLIQPED ) ) ) );
						iItImp = 0;
					}
					else {
						imp.pulaLinha( 48 - imp.pRow(), imp.comprimido() );
						imp.say( 4, "********************" );
						imp.say( 32, "********************" );
						imp.say( 114, "********************" );
						imp.pulaLinha( 2, imp.comprimido() );
						imp.say( 4, "********************" );
						imp.say( 58, "********************" );
						imp.say( 87, "********************" );
						imp.say( 114, "********************" );
					}

					// Fim da impressão dos totais

					// Imprime informações do frete

					if ( iContaFrete == 0 ) {
						frete.next();
						iContaFrete++;
					}

					imp.pulaLinha( 3, imp.comprimido() );
					imp.say( 4, frete.getString( NF.C_RAZTRANSP ) );
					imp.say( 78, frete.getString( NF.C_TIPOFRETE ).equals( "C" ) ? "1" : "2" );
					imp.say( 90, frete.getString( NF.C_PLACAFRETE ) );
					imp.say( 101, frete.getString( NF.C_UFFRETE ) );

					if ( frete.getString( NF.C_TIPOTRANSP ).equals( "C" ) ) {
						imp.say( 116, Funcoes.setMascara( cab.getString( NF.C_CNPJEMIT ), "##.###.###/####-##" ) );
					}
					else {
						imp.say( 116, Funcoes.setMascara( frete.getString( NF.C_CNPJTRANSP ), "##.###.###/####-##" ) );
					}

					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 4, frete.getString( NF.C_ENDTRANSP ).trim() + ", " + frete.getInt( NF.C_NUMTRANSP ) );
					imp.say( 64, frete.getString( NF.C_CIDTRANSP ) );
					imp.say( 101, frete.getString( NF.C_UFTRANSP ) );

					if ( frete.getString( NF.C_TIPOTRANSP ).equals( "C" ) ) {
						imp.say( 116, cab.getString( NF.C_INSCEMIT ) );
					}
					else {
						imp.say( 116, frete.getString( NF.C_INSCTRANSP ) );
					}

					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 4, Funcoes.strDecimalToStrCurrency( 12, 2, String.valueOf( frete.getString( NF.C_QTDFRETE ) ) ) );
					imp.say( 25, Funcoes.copy( frete.getString( NF.C_ESPFRETE ), 27 ) );
					imp.say( 59, Funcoes.copy( frete.getString( NF.C_MARCAFRETE ), 22 ) );
					imp.say( 80, Funcoes.copy( frete.getString( NF.C_CONHECFRETEPED ), 20 ) );
					imp.say( 107, Funcoes.strDecimalToStrCurrency( 12, 2, String.valueOf( frete.getFloat( NF.C_PESOBRUTO ) ) ) );
					imp.say( 124, Funcoes.strDecimalToStrCurrency( 12, 2, String.valueOf( frete.getString( NF.C_PESOLIQ ) ) ) );
					imp.pulaLinha( 2, imp.comprimido() );

					// Fim da impressão do frete

					// Imprime observação e classificações fiscais

					vDescFisc = Funcoes.strToVectorSilabas( sDescFisc, 40 );

					sizeObs = vSigla.size();
					sizeObs = vDescFisc.size() > sizeObs ? vDescFisc.size() : sizeObs;

					int aux = 0;
					for ( int i = 0; i < 7; i++ ) {
						if ( aux < sizeObs ) {
							imp.pulaLinha( 1, imp.comprimido() );
							if ( vSigla.size() > 0 && indexSigla < vSigla.size() ) {
								imp.say( 4, vSigla.elementAt( indexSigla++ ) );
							}
							if ( vDescFisc.size() > 0 && indexDescFisc < vDescFisc.size() ) {
								imp.say( 20, Funcoes.copy( (String) vDescFisc.elementAt( indexDescFisc++ ), 40 ) );
							}
						}
						else {
							imp.pulaLinha( 1, imp.comprimido() );
						}
					}

					// Fim da observação

					// Imprime canhoto

					// imp.pulaLinha( 5, imp.comprimido() );
					// imp.say( 125, StringFunctions.strZero( String.valueOf( iNumNota ), 6 ) );

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
