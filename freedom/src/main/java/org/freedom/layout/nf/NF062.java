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

public class NF062 extends Layout {

	public boolean imprimir( NF nf, ImprimeOS imp ) {

		boolean retorno = super.imprimir( nf, imp );
		boolean complementar = false;
		boolean bFat = true;
		boolean bNat = true;
		boolean bjatem = false;
		boolean bvlriss = true;
		final int MAXLINE = 42;
		final int MAXPROD = 24;
		int iNumNota = 0;
		int iItImp = 0;
		int iProdImp = 0;
		int iContaFrete = 0;
		int iLinPag = imp.verifLinPag( "NF" );
		int sizeObs = 0;
		int indexObs = 0;
		int indexSigla = 0;
		int indexDescFisc = 0;
		String sCodfisc = null;
		String sSigla = null;
		String sTemp = null;
		String sDescFisc = "";
		Vector<String> vDescFisc = new Vector<String>();
		String sObsVenda = "";
		String[] sValsCli = new String[ 4 ];
		String[] sNat = new String[ 2 ];
		String[] sVencs = new String[ 9 ];
		String[] sVals = new String[ 9 ];
		String[] sDuplics = new String[ 9 ];
		Vector<String> vClfisc = new Vector<String>();
		Vector<String> vSigla = new Vector<String>();
		Vector<String> vMens = new Vector<String>();
		Vector<String> vObs = new Vector<String>();

		try {

			// Teste comando para mudar o espaçamento vertical.

			imp.setaEspVert( ImprimeOS.EPSON_8PP );

			if ( cab.next() ) {
				iNumNota = cab.getInt( NF.C_DOC );
				vObs = Funcoes.strToVectorSilabas( cab.getString( NF.C_OBSPED ), 30 );
				vMens = Funcoes.strToVectorSilabas( cab.getString( NF.C_MENSAGENS ), 30 );
				vObs.addElement( "" );
				vObs.addAll( vMens );
			}

			complementar = "CO".equals( cab.getString( NF.C_TIPOMOV ) );

			for ( int i = 0; i < 9; i++ ) {
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

					imp.pulaLinha( 3, imp.comprimido() );

					if ( nf.getTipoNF() == NF.TPNF_ENTRADA ) {
						imp.say( 62, "X" );
					}
					else {
						imp.say( 56, "X" );
					}

					imp.say( 81, StringFunctions.strZero( String.valueOf( iNumNota ), 6 ) );
					imp.say( 102, StringFunctions.strZero( String.valueOf( iNumNota ), 6 ) );

					imp.pulaLinha( 5, imp.comprimido() );

					imp.say( 2, Funcoes.copy( sNat[ 0 ], 23 ) );
					imp.say( 26, sNat[ 1 ] );
					imp.pulaLinha( 3, imp.comprimido() );
					imp.say( 3, sValsCli[ 1 ] );
					imp.say( 57, !sValsCli[ 0 ].equals( "" ) ? Funcoes.setMascara( sValsCli[ 0 ], "###.###.###-##" ) : Funcoes.setMascara( cab.getString( NF.C_CNPJEMIT ), "##.###.###/####-##" ) );
					imp.say( 83, ( cab.getDate( NF.C_DTEMITPED ) != null ? Funcoes.dateToStrDate( cab.getDate( NF.C_DTEMITPED ) ) : "" ) );
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 3, Funcoes.copy( cab.getString( NF.C_ENDEMIT ), 0, 50 ).trim() + ", " + Funcoes.copy( cab.getString( NF.C_NUMEMIT ), 0, 6 ).trim() + " - " + Funcoes.copy( cab.getString( NF.C_COMPLEMIT ), 0, 9 ).trim() );
					imp.say( 44, Funcoes.copy( cab.getString( NF.C_BAIREMIT ), 0, 23 ) );
					imp.say( 66, Funcoes.setMascara( cab.getString( NF.C_CEPEMIT ), "#####-###" ) );

					if ( !itens.getString( NF.C_IMPDTSAIDA ).equals( "N" ) ) {
						imp.say( 83, ( cab.getDate( NF.C_DTSAIDA ) != null ? Funcoes.dateToStrDate( cab.getDate( NF.C_DTSAIDA ) ) : "" ) );
					}

					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 3, sValsCli[ 2 ] );
					imp.say( 30, ( !cab.getString( NF.C_DDDEMIT ).equals( "" ) ? "(" + cab.getString( NF.C_DDDEMIT ) + ")" : "" ) + ( !cab.getString( NF.C_FONEEMIT ).equals( "" ) ? Funcoes.setMascara( cab.getString( NF.C_FONEEMIT ).trim(), "####-####" ) : "" ).trim() );
					imp.say( 49, sValsCli[ 3 ] );
					imp.say( 54, !cab.getString( NF.C_RGEMIT ).equals( "" ) ? cab.getString( NF.C_RGEMIT ) : cab.getString( NF.C_INSCEMIT ) );
					imp.pulaLinha( 3, imp.comprimido() );
					// Fim do cabeçalho

				}

				// Monta a mensagem fiscal ...

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

				sCodfisc = itens.getString( NF.C_CODFISC ).trim();

				if ( !sCodfisc.equals( "" ) ) {
					for ( int i = 0; i < vClfisc.size() && !bjatem; i++ ) {
						if ( vClfisc.elementAt( i ) != null ) {
							if ( sCodfisc.equals( vClfisc.elementAt( i ) ) ) {
								sSigla = String.valueOf( (char) ( 64 + i ) );
								bjatem = true;
							}
							else {
								bjatem = false;
							}
						}
					}
					if ( !bjatem ) {
						vClfisc.addElement( sCodfisc );
						sSigla = String.valueOf( (char) ( 63 + vClfisc.size() ) );
						vSigla.addElement( sSigla + "=" + Funcoes.copy( sCodfisc, 8 ) );
						vDescFisc.add( sDescFisc );
						sDescFisc = "";
					}
				}
				bjatem = false;
				// Fim da classificação fiscal

				// Imprime os dados do item no corpo da nota

				imp.pulaLinha( 1, imp.comprimido() );

				if ( complementar ) {
					imp.say( 17, Funcoes.copy( itens.getString( NF.C_OBSITPED ).trim(), 48 ) );
				}
				else {
					imp.say( 2, itens.getString( NF.C_REFPROD ) );
					imp.say( 8, Funcoes.copy( itens.getString( NF.C_DESCPROD ).trim(), 23 ) );
					imp.say( 31, " - CF:" + sSigla );
					imp.say( 40, Funcoes.copy( itens.getString( NF.C_ORIGFISC ), 0, 1 ) + Funcoes.copy( itens.getString( NF.C_CODTRATTRIB ), 0, 2 ) );
					imp.say( 46, Funcoes.copy( itens.getString( NF.C_CODUNID ), 4 ) );
					imp.say( 49, Funcoes.strDecimalToStrCurrency( 8, 2, String.valueOf( itens.getBigDecimal( NF.C_QTDITPED ) ) ) );
					imp.say( 62, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( itens.getBigDecimal( NF.C_VLRPRODITPED ).divide( itens.getBigDecimal( NF.C_QTDITPED ) ) ) ) );
					imp.say( 76, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( itens.getBigDecimal( NF.C_VLRPRODITPED ) ) ) );

					imp.say( 90, Funcoes.strDecimalToStrCurrency( 2, 2, String.valueOf( itens.getBigDecimal( NF.C_PERCICMSITPED ) ) ) );

				}

				iProdImp++;

				iItImp++;

				// Fim da impressão do item

				if ( ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) || ( iProdImp == MAXPROD ) || ( imp.pRow() == MAXLINE ) ) {

					if ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) {
						imp.pulaLinha( MAXLINE - imp.pRow(), imp.comprimido() );

					}

					// Imprime totais

					if ( iContaFrete == 0 ) {
						frete.next();
						iContaFrete++;
					}

					if ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) {
						imp.pulaLinha( 33 - imp.pRow(), imp.comprimido() );
						imp.say( 2, Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( cab.getBigDecimal( NF.C_VLRBASEICMSPED ) ) ) );
						imp.say( 18, Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( cab.getBigDecimal( NF.C_VLRICMSPED ) ) ) );
						imp.say( 38, Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( cab.getBigDecimal( NF.C_VLRBASEICMSST ) ) ) );
						imp.say( 55, Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( cab.getBigDecimal( NF.C_VLRICMSST ) ) ) );
						imp.say( 75, Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( cab.getBigDecimal( NF.C_VLRPRODPED ) ) ) );

						imp.pulaLinha( 2, imp.comprimido() );

						if ( !complementar ) {
							imp.say( 2, Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( frete.getBigDecimal( NF.C_VLRFRETEPED ) ) ) );
							imp.say( 38, Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( cab.getBigDecimal( NF.C_VLRADICPED ) ) ) );
							imp.say( 55, Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( cab.getBigDecimal( NF.C_VLRIPIPED ) ) ) );
						}

						BigDecimal vlrliq = cab.getBigDecimal( NF.C_VLRLIQPED );
						// BigDecimal dificmssubst = cab.getBigDecimal( NF.C_VLRICMSST );
						BigDecimal vlradic = cab.getBigDecimal( NF.C_VLRADICPED );
						// BigDecimal vlrsoma = vlradic.add(dificmssubst);
						BigDecimal vlrtotnota = vlrliq.add( vlradic );

						imp.say( 75, Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( vlrtotnota ) ) );

						iItImp = 0;

					}
					else {
						imp.pulaLinha( 47 - imp.pRow(), imp.comprimido() );
						imp.say( 4, "********" );
						imp.say( 32, "********" );
						imp.say( 114, "********" );
						imp.pulaLinha( 2, imp.comprimido() );
						imp.say( 4, "*********" );
						imp.say( 58, "*********" );
						imp.say( 87, "*********" );
						imp.say( 114, "*********" );
					}

					// Fim da impressão dos totais

					// Imprime informações do frete

					imp.pulaLinha( 3, imp.comprimido() );

					if ( !complementar ) {

						imp.say( 4, Funcoes.copy( frete.getString( NF.C_RAZTRANSP ), 20 ) );
						imp.say( 56, "C".equals( frete.getString( NF.C_TIPOFRETE ) ) ? "1" : "2" );
						imp.say( 61, frete.getString( NF.C_PLACAFRETE ) );
						imp.say( 73, frete.getString( NF.C_UFFRETE ) );

						if ( "C".equals( frete.getString( NF.C_TIPOTRANSP ) ) ) {
							imp.say( 77, Funcoes.setMascara( cab.getString( NF.C_CNPJEMIT ), "##.###.###/####-##" ) );
						}
						else {
							if ( "".equals( frete.getString( NF.C_CNPJTRANSP ) ) ) {
								imp.say( 77, Funcoes.setMascara( frete.getString( NF.C_CPFTRANSP ), "###.###.###-##" ) );
							}
							else {
								imp.say( 77, Funcoes.setMascara( frete.getString( NF.C_CNPJTRANSP ), "##.###.###/####-##" ) );
							}
						}

						imp.pulaLinha( 2, imp.comprimido() );
						imp.say( 4, frete.getString( NF.C_ENDTRANSP ).trim() + ", " + frete.getInt( NF.C_NUMTRANSP ) );
						imp.say( 48, frete.getString( NF.C_CIDTRANSP ) );
						imp.say( 72, frete.getString( NF.C_UFTRANSP ) );

						if ( frete.getString( NF.C_TIPOTRANSP ).equals( "C" ) ) {
							imp.say( 77, cab.getString( NF.C_INSCEMIT ) );
						}
						else {
							imp.say( 77, frete.getString( NF.C_INSCTRANSP ) );
						}

						imp.pulaLinha( 2, imp.comprimido() );

						imp.say( 4, Funcoes.strDecimalToStrCurrency( 12, 2, String.valueOf( frete.getString( NF.C_QTDFRETE ) ) ) );
						imp.say( 21, Funcoes.copy( frete.getString( NF.C_ESPFRETE ), 27 ) );
						imp.say( 33, Funcoes.copy( frete.getString( NF.C_MARCAFRETE ), 22 ) );
						imp.say( 42, Funcoes.copy( frete.getString( NF.C_CONHECFRETEPED ), 20 ) );
						imp.say( 60, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( frete.getBigDecimal( NF.C_PESOBRUTO ) ) ) );
						imp.say( 77, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( frete.getBigDecimal( NF.C_PESOLIQ ) ) ) );
						imp.pulaLinha( 2, imp.comprimido() );

					}

					// Fim da impressão do frete

					// Imprime observação e classificações fiscais

					int sizeObsDescFisc = vDescFisc.size() > vObs.size() ? vDescFisc.size() : vObs.size();

					// imp.pulaLinha( 1, imp.comprimido());

					for ( int i = 0; i < 10 && i < sizeObsDescFisc; i++ ) {

						boolean blinha = false;

						imp.pulaLinha( 1, imp.comprimido() );

						if ( vSigla.size() > 0 && indexSigla < vSigla.size() ) {

							Vector<String> vDescFiscSigla = Funcoes.stringToVector( vDescFisc.elementAt( indexSigla ).toString() );

							imp.say( 2, "CF:" + vSigla.elementAt( indexSigla++ ) + " " );

							blinha = true;

							for ( int i2 = 0; i2 < vDescFiscSigla.size(); i2++ ) {
								if ( vDescFisc.size() > 0 && indexDescFisc < vDescFisc.size() ) {
									imp.say( ( blinha ? 16 : 2 ), Funcoes.copy( vDescFiscSigla.elementAt( i2 ), 30 ) );
									if ( blinha )
										imp.pulaLinha( 1, imp.comprimido() );
									blinha = false;
								}
							}

						}

					}

					sizeObs = vObs.size();

					for ( int i = 0; i < vObs.size(); i++ ) {
						if ( imp.pRow() < ( MAXLINE + 22 ) ) {
							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( 2, (String) vObs.elementAt( i ) );
						}
					}

					// Fim da observação

					// Imprime canhoto

					// imp.pulaLinha( 4, imp.comprimido() );
					// imp.say( 128, StringFunctions.strZero( String.valueOf( iNumNota ), 6 ) );

					imp.pulaLinha( iLinPag - imp.pRow() + 3, imp.comprimido() );
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
