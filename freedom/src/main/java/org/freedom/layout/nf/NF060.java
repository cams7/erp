/**
 * @version 22/05/2006 <BR>
 * @author Setpoint Informática Ltda./Alex Rodrigues <BR>
 * 
 *         Projeto: Freedom <BR>
 *         Pacote: layout <BR>
 * 
 *         Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *         modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *         na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *         Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *         sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *         Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *         Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *         de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *         Layout da nota fiscal para a empresa Iswara Ltda.
 */

package org.freedom.layout.nf;

import java.math.BigDecimal;
import java.util.Vector;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.business.component.NF;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.component.Layout;
import org.freedom.library.functions.Funcoes;

public class NF060 extends Layout {

	public boolean imprimir( NF nf, ImprimeOS imp ) {

		boolean retorno = super.imprimir( nf, imp );
		boolean complementar = false;
		boolean bFat = true;
		boolean bNat = true;
		boolean bjatem = false;
		boolean bvlriss = true;
		final int MAXLINE = 64;
		final int MAXPROD = 31;
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
		Vector<String> vObs = new Vector<String>();
		Vector<String> vMens = new Vector<String>();

		try {

			// alterando espaçamento vertical.
			imp.setaEspVert( ImprimeOS.EPSON_8PP );

			if ( cab.next() ) {
				iNumNota = cab.getInt( NF.C_DOC );
				sObsVenda = cab.getString( NF.C_OBSPED ).replace( "\n", "" );

				vMens = Funcoes.strToVectorSilabas( cab.getString( NF.C_MENSAGENS ), 120 );
				vObs.addElement( "" );
				vObs.addAll( vMens );
			}

			complementar = "CO".equals( cab.getString( NF.C_TIPOMOV ) );

			for ( int i = 0; i < 6; i++ ) {
				if ( bFat ) {
					if ( parc.next() ) {
						sDuplics[ i ] = StringFunctions.strZero( String.valueOf( iNumNota ), 6 ) + " / " + parc.getInt( NF.C_NPARCITREC );
						sVencs[ i ] = ( parc.getDate( NF.C_DTVENCTO ) != null ? Funcoes.dateToStrDate( parc.getDate( NF.C_DTVENCTO ) ) : "" );
						sVals[ i ] = Funcoes.strDecimalToStrCurrency( 12, 2, String.valueOf( parc.getBigDecimal( NF.C_VLRPARC ) ) );
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

					imp.pulaLinha( 2, imp.normal() + imp.comprimido() + imp.expandido() );

					if ( nf.getTipoNF() == NF.TPNF_ENTRADA ) {
						imp.say( 55, "X" );
					}
					else {
						imp.say( 48, "X" );
					}

					imp.say( 65, StringFunctions.strZero( String.valueOf( iNumNota ), 6 ) );

					imp.pulaLinha( 3, imp.normal() + imp.comprimido() );
					imp.say( 30, cab.getString( NF.C_ENDFILIAL ).trim() + "," + cab.getString( NF.C_NUMFILIAL ) + " - " + cab.getString( NF.C_BAIRFILIAL ).trim() + " - " + cab.getString( NF.C_CIDFILIAL ).trim() + " / " + cab.getString( NF.C_UFFILIAL ) + " - "
							+ Funcoes.setMascara( cab.getString( NF.C_CEPFILIAL ), "#####-###" ) );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 30, cab.getString( NF.C_WWWFILIAL ).trim() + " - Fone:" + Funcoes.setMascara( cab.getString( NF.C_FONEFILIAL ), "(####)####-####" ) );
					imp.pulaLinha( 1, imp.comprimido() );

					imp.pulaLinha( 2, imp.normal() + imp.comprimido() );
					imp.say( 4, sNat[ 0 ] );
					imp.say( 54, sNat[ 1 ] );
					imp.pulaLinha( 3, imp.comprimido() );
					imp.say( 4, sValsCli[ 1 ] );
					imp.say( 87, !sValsCli[ 0 ].equals( "" ) ? Funcoes.setMascara( sValsCli[ 0 ], "###.###.###-##" ) : Funcoes.setMascara( cab.getString( NF.C_CNPJEMIT ), "##.###.###/####-##" ) );
					imp.say( 127, ( cab.getDate( NF.C_DTEMITPED ) != null ? Funcoes.dateToStrDate( cab.getDate( NF.C_DTEMITPED ) ) : "" ) );
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 4, Funcoes.copy( cab.getString( NF.C_ENDEMIT ), 0, 50 ).trim() + ", " + Funcoes.copy( cab.getString( NF.C_NUMEMIT ), 0, 6 ).trim() + " - " + Funcoes.copy( cab.getString( NF.C_COMPLEMIT ), 0, 9 ).trim() );
					imp.say( 83, Funcoes.copy( cab.getString( NF.C_BAIREMIT ), 0, 23 ) );
					imp.say( 108, Funcoes.setMascara( cab.getString( NF.C_CEPEMIT ), "#####-###" ) );

					// if ( !itens.getString( NF.C_IMPDTSAIDA ).equals( "N" ) ) {
					// imp.say( 127, ( cab.getDate( NF.C_DTSAIDA ) != null ? Funcoes.dateToStrDate( cab.getDate( NF.C_DTSAIDA ) ) : "" ) );
					// }

					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 4, sValsCli[ 2 ] );
					imp.say( 47, ( !cab.getString( NF.C_DDDEMIT ).equals( "" ) ? "(" + cab.getString( NF.C_DDDEMIT ) + ")" : "" ) + ( !cab.getString( NF.C_FONEEMIT ).equals( "" ) ? Funcoes.setMascara( cab.getString( NF.C_FONEEMIT ).trim(), "####-####" ) : "" ).trim() );
					imp.say( 73, sValsCli[ 3 ] );
					imp.say( 87, !cab.getString( NF.C_RGEMIT ).equals( "" ) ? cab.getString( NF.C_RGEMIT ) : cab.getString( NF.C_INSCEMIT ) );

					// Fim do cabeçalho

					// Imprime dados da fatura

					imp.pulaLinha( 4, imp.comprimido() );
					imp.say( 8, sDuplics[ 0 ] );
					imp.say( 30, sVals[ 0 ] );
					imp.say( 54, sVencs[ 0 ] );
					imp.say( 78, sDuplics[ 1 ] );
					imp.say( 102, sVals[ 1 ] );
					imp.say( 127, sVencs[ 1 ] );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 8, sDuplics[ 2 ] );
					imp.say( 30, sVals[ 2 ] );
					imp.say( 54, sVencs[ 2 ] );
					imp.say( 78, sDuplics[ 3 ] );
					imp.say( 102, sVals[ 3 ] );
					imp.say( 127, sVencs[ 3 ] );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 8, sDuplics[ 4 ] );
					imp.say( 30, sVals[ 4 ] );
					imp.say( 54, sVencs[ 4 ] );
					imp.say( 78, sDuplics[ 5 ] );
					imp.say( 102, sVals[ 5 ] );
					imp.say( 127, sVencs[ 5 ] );

					imp.pulaLinha( 4, imp.comprimido() );

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

					if ( complementar ) {
						imp.say( 17, Funcoes.copy( itens.getString( NF.C_OBSITPED ).trim(), 48 ) );
					}
					else {
						imp.say( 4, itens.getString( NF.C_REFPROD ) );
						imp.say( 15, Funcoes.copy( itens.getString( NF.C_DESCPROD ).trim(), 48 ) );
						imp.say( 67, sSigla );
						imp.say( 72, Funcoes.copy( itens.getString( NF.C_ORIGFISC ), 0, 1 ) + Funcoes.copy( itens.getString( NF.C_CODTRATTRIB ), 0, 2 ) );
						imp.say( 77, Funcoes.copy( itens.getString( NF.C_CODUNID ), 4 ) );
						imp.say( 81, Funcoes.strDecimalToStrCurrency( 8, 2, String.valueOf( itens.getBigDecimal( NF.C_QTDITPED ) ) ) );
						imp.say( 94, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( itens.getBigDecimal( NF.C_VLRPRODITPED ).divide( itens.getBigDecimal( NF.C_QTDITPED ) ) ) ) );
						imp.say( 110, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( itens.getBigDecimal( NF.C_VLRPRODITPED ) ) ) );
						imp.say( 122, Funcoes.strDecimalToStrCurrency( 2, 0, String.valueOf( itens.getBigDecimal( NF.C_PERCICMSITPED ) ) ) + "%" );
						imp.say( 127, Funcoes.strDecimalToStrCurrency( 2, 0, String.valueOf( itens.getBigDecimal( NF.C_PERCIPIITPED ) ) ) + "%" );
						imp.say( 132, Funcoes.strDecimalToStrCurrency( 6, 2, String.valueOf( itens.getBigDecimal( NF.C_VLRIPIITPED ) ) ) );
					}

					iProdImp++;
				}
				else {
					// guarda serviço;
					vServico.addElement( new Object[] { "".equals( itens.getString( NF.C_OBSITPED ).trim() ) ? itens.getString( NF.C_DESCPROD ) : itens.getString( NF.C_OBSITPED ).trim(), itens.getBigDecimal( NF.C_QTDITPED ), itens.getBigDecimal( NF.C_VLRLIQITPED ),
							itens.getBigDecimal( NF.C_VLRISSITPED ) } );
				}

				iItImp++;

				// Fim da impressão do item

				if ( ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) || ( iProdImp == MAXPROD ) || ( imp.pRow() == MAXLINE ) ) {

					// Imprime observação no corpo da nota ...

					for ( int i = 0; i < vObs.size(); i++ ) {
						if ( imp.pRow() < MAXLINE ) {
							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( 8, (String) vObs.elementAt( i ) );
						}
					}

					if ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) {
						if ( cab.getBigDecimal( NF.C_VLRDESCITPED ).compareTo( new BigDecimal( 0 ) ) > 0 ) {
							// Imprime o desconto
							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( 8, "Valor do desconto : " + Funcoes.strDecimalToStrCurrency( 9, 2, String.valueOf( cab.getBigDecimal( NF.C_VLRDESCITPED ) ) ) );
						}
						if ( vServico.size() > 0 && indexServ < vServico.size() ) {
							imp.pulaLinha( 33 - imp.pRow(), imp.comprimido() );
						}
						else {
							imp.pulaLinha( MAXLINE - imp.pRow(), imp.comprimido() );
						}
					}

					// Imprime serviço

					if ( vServico.size() > 0 ) {
						for ( int row = 0; row < 11; row++ ) {
							imp.pulaLinha( 1, imp.comprimido() );
							if ( indexServ < vServico.size() ) {

								vDescServ = Funcoes.strToVectorSilabas( ( (String) vServico.get( indexServ )[ 0 ] ).trim(), 80 );

								for ( int i = 0; i < vDescServ.size(); i++ ) {
									imp.say( 4, (String) vDescServ.elementAt( i ) );
									if ( i == 0 ) {
										imp.say( 87, Funcoes.strDecimalToStrCurrency( 8, 2, String.valueOf( (BigDecimal) vServico.get( indexServ )[ 1 ] ) ) );
										imp.say( 100, Funcoes.strDecimalToStrCurrency( 12, 2, String.valueOf( (BigDecimal) vServico.get( indexServ )[ 2 ] ) ) );
									}
									if ( bvlriss && row == 6 ) {
										imp.say( 120, Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( cab.getBigDecimal( NF.C_VLRISS ) ) ) );
										bvlriss = false;
									}
									if ( i < ( vDescServ.size() - 1 ) ) {
										imp.pulaLinha( 1, imp.comprimido() );
									}
									if ( ( i > 0 ) && ( i == vDescServ.size() - 1 ) ) {
										row += vDescServ.size() - 1;
									}
								}
								indexServ++;
							}
							if ( bvlriss && row == 6 ) {
								imp.say( 120, Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( cab.getBigDecimal( NF.C_VLRISS ) ) ) );
								bvlriss = false;
							}
						}
						imp.say( 120, Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( cab.getBigDecimal( NF.C_BASEISS ) ) ) );
					}

					// Imprime totais

					if ( iContaFrete == 0 ) {
						frete.next();
						iContaFrete++;
					}

					if ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) {
						imp.pulaLinha( MAXLINE - imp.pRow(), imp.comprimido() );
						imp.say( 4, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getBigDecimal( NF.C_VLRBASEICMSPED ) ) ) );
						imp.say( 32, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getBigDecimal( NF.C_VLRICMSPED ) ) ) );

						BigDecimal baseiss = cab.getBigDecimal( NF.C_BASEISS );
						BigDecimal vlrprodped = cab.getBigDecimal( NF.C_VLRPRODPED );
						BigDecimal vlrprod = vlrprodped.subtract( baseiss );

						if ( !complementar ) {
							imp.say( 0, 0, imp.expandido() );
							imp.say( 50, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( vlrprod ) ) );
						}

						imp.pulaLinha( 2, imp.normal() + imp.comprimido() );

						if ( !complementar ) {
							imp.say( 4, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( frete.getBigDecimal( NF.C_VLRFRETEPED ) ) ) );
							imp.say( 58, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getBigDecimal( NF.C_VLRADICPED ) ) ) );
							imp.say( 87, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getBigDecimal( NF.C_VLRIPIPED ) ) ) );
						}

						if ( nf.getTipoNF() == NF.TPNF_ENTRADA ) {
							imp.say( 0, 0, imp.expandido() );
							imp.say( 50, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getBigDecimal( NF.C_VLRLIQPED ).add( cab.getBigDecimal( NF.C_VLRADICPED ) ) ) ) );
						}
						else {
							if ( !complementar ) {
								imp.say( 0, 0, imp.expandido() );
								imp.say( 50, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getBigDecimal( NF.C_VLRLIQPED ) ) ) );
							}
							iItImp = 0;
						}
					}
					else {
						imp.pulaLinha( MAXLINE - imp.pRow(), imp.normal() + imp.comprimido() );
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

					imp.pulaLinha( 3, imp.comprimido() );

					if ( !complementar ) {

						imp.say( 5, frete.getString( NF.C_RAZTRANSP ) != null ? ( frete.getString( NF.C_RAZTRANSP ).trim() + " - " + "(" + StringFunctions.strZero( frete.getString( NF.C_DDDTRANSP ), 4 ) + ")" + Funcoes.setMascara( frete.getString( NF.C_FONETRANSP ).trim(), "####-####" ) ) : "" );
						imp.say( 88, "C".equals( frete.getString( NF.C_TIPOFRETE ) ) ? "1" : "2" );
						imp.say( 95, frete.getString( NF.C_PLACAFRETE ) );
						imp.say( 107, frete.getString( NF.C_UFFRETE ) );

						if ( "C".equals( frete.getString( NF.C_TIPOTRANSP ) ) ) {
							imp.say( 116, Funcoes.setMascara( cab.getString( NF.C_CNPJEMIT ), "##.###.###/####-##" ) );
						}
						else {
							if ( "".equals( frete.getString( NF.C_CNPJTRANSP ) ) ) {
								imp.say( 116, Funcoes.setMascara( frete.getString( NF.C_CPFTRANSP ), "###.###.###-##" ) );
							}
							else {
								imp.say( 116, Funcoes.setMascara( frete.getString( NF.C_CNPJTRANSP ), "##.###.###/####-##" ) );
							}
						}

						imp.pulaLinha( 2, imp.comprimido() );
						imp.say( 5, frete.getString( NF.C_ENDTRANSP ) != null ? ( frete.getString( NF.C_ENDTRANSP ).trim() + ", " + frete.getInt( NF.C_NUMTRANSP ) ) : "" );
						imp.say( 76, frete.getString( NF.C_CIDTRANSP ) );
						imp.say( 107, frete.getString( NF.C_UFTRANSP ) );

						if ( "C".equals( frete.getString( NF.C_TIPOTRANSP ) ) ) {
							imp.say( 116, cab.getString( NF.C_INSCEMIT ) );
						}
						else {
							imp.say( 116, frete.getString( NF.C_INSCTRANSP ) );
						}

						imp.pulaLinha( 2, imp.comprimido() );

						imp.say( 3, Funcoes.strDecimalToStrCurrency( 12, 2, String.valueOf( frete.getString( NF.C_QTDFRETE ) ) ) );
						imp.say( 20, Funcoes.copy( frete.getString( NF.C_ESPFRETE ), 27 ) );
						imp.say( 49, Funcoes.copy( frete.getString( NF.C_MARCAFRETE ), 22 ) );
						imp.say( 76, Funcoes.copy( frete.getString( NF.C_CONHECFRETEPED ), 20 ) );
						imp.say( 108, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( frete.getBigDecimal( NF.C_PESOBRUTO ) ) ) );
						imp.say( 124, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( frete.getBigDecimal( NF.C_PESOLIQ ) ) ) );
						imp.pulaLinha( 2, imp.comprimido() );

					}

					// Fim da impressão do frete

					// Imprime observação e classificações fiscais

					if ( complementar ) {
						vObsVenda = Funcoes.strToVectorSilabas( sObsVenda, 40 );
						imp.pulaLinha( 5, imp.comprimido() );
					}
					else {
						vObsVenda = Funcoes.strToVectorSilabas( ( sDescFisc.length() > 0 ? sDescFisc + "\n" : "" ) + sObsVenda, 40 );
					}

					sizeObs = vSigla.size();
					sizeObs = vObsVenda.size() > sizeObs ? vObsVenda.size() : sizeObs;

					int aux = 0;
					for ( int i = 0; i < 8; i++ ) {
						if ( aux < sizeObs ) {
							imp.pulaLinha( 1, imp.comprimido() );
							if ( vSigla.size() > 0 && indexSigla < vSigla.size() && !complementar ) {
								imp.say( 5, vSigla.elementAt( indexSigla++ ) );
							}
							if ( vObsVenda.size() > 0 && indexObs < vObsVenda.size() ) {
								imp.say( 20, Funcoes.copy( (String) vObsVenda.elementAt( indexObs++ ), 40 ) );
							}
						}
						else {
							imp.pulaLinha( 1, imp.comprimido() );
						}
					}

					// Fim da observação

					// Imprime canhoto

					imp.pulaLinha( 6, imp.normal() + imp.comprimido() + imp.expandido() );
					imp.say( 66, StringFunctions.strZero( String.valueOf( iNumNota ), 6 ) );

					imp.pulaLinha( iLinPag - imp.pRow(), imp.normal() + imp.comprimido() );
					imp.setPrc( 0, 0 );
					imp.incPags();
				}
			}

			// imp.eject();
			imp.fechaGravacao();
			retorno = true;

		} catch ( Exception err ) {
			Funcoes.mensagemErro( null, "Erro ao montar nota \n" + err.getMessage() );
			err.printStackTrace();
		} finally {
			System.gc();
		}

		return retorno;

	}

}
