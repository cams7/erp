/**
 * @version 22/12/2009 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez<BR>
 * 
 *         Projeto: Freedom <BR>
 *         Pacote: layout <BR>
 *         Classe:
 * @(#)NF070.java <BR>
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
 *                Layout da nota fiscal para a empresa 70.
 */

package org.freedom.layout.nf;

import java.math.BigDecimal;
import java.util.Vector;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.business.component.NF;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.component.Layout;
import org.freedom.library.functions.Funcoes;

public class NF070 extends Layout {

	public boolean imprimir( NF nf, ImprimeOS imp ) {

		boolean retorno = super.imprimir( nf, imp );
		boolean complementar = false;
		boolean bFat = true;
		boolean bNat = true;
		boolean bjatem = false;
		boolean bvlriss = true;
		final int MAXLINE = 36;
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

		try {

			imp.setaEspVert( ImprimeOS.EPSON_8PP );

			if ( cab.next() ) {
				iNumNota = cab.getInt( NF.C_DOC );
				// if ( nf.getTipoNF() == NF.TPNF_ENTRADA ) {
				// sObsVenda = cab.getString( NF.C_OBSPED );
				// }
				// else {
				sObsVenda = cab.getString( NF.C_OBSPED ).replace( "\n", "" );
				// }

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

					imp.pulaLinha( 1, imp.normal() );

					imp.say( 74, StringFunctions.strZero( String.valueOf( iNumNota ), 8 ) );

					imp.pulaLinha( 2 );

					if ( nf.getTipoNF() == NF.TPNF_ENTRADA ) {
						imp.say( 58, "X" );
					}
					else {
						imp.say( 50, "X" );
					}

					imp.pulaLinha( 5 );
					imp.say( 2, sNat[ 0 ] );
					imp.say( 23, sNat[ 1 ] );
					imp.pulaLinha( 3 );
					imp.say( 2, sValsCli[ 1 ] );
					imp.say( 50, !cab.getString( NF.C_RGEMIT ).equals( "" ) ? cab.getString( NF.C_RGEMIT ) : cab.getString( NF.C_INSCEMIT ) );

					imp.say( 71, ( cab.getDate( NF.C_DTEMITPED ) != null ? Funcoes.dateToStrDate( cab.getDate( NF.C_DTEMITPED ) ) : "" ) );

					// /*****////

					imp.pulaLinha( 2 );
					imp.say( 2, Funcoes.copy( cab.getString( NF.C_ENDEMIT ), 0, 50 ).trim() + ", " + Funcoes.copy( cab.getString( NF.C_NUMEMIT ), 0, 6 ).trim() + " - " + Funcoes.copy( cab.getString( NF.C_COMPLEMIT ), 0, 9 ).trim() );
					imp.say( 35, Funcoes.copy( cab.getString( NF.C_BAIREMIT ), 0, 23 ) );
					imp.say( 56, Funcoes.setMascara( cab.getString( NF.C_CEPEMIT ), "#####-###" ) );

					if ( !itens.getString( NF.C_IMPDTSAIDA ).equals( "N" ) ) {
						imp.say( 71, ( cab.getDate( NF.C_DTSAIDA ) != null ? Funcoes.dateToStrDate( cab.getDate( NF.C_DTSAIDA ) ) : "" ) );
					}

					imp.pulaLinha( 2 );
					imp.say( 2, sValsCli[ 2 ] );
					imp.say( 30, ( !cab.getString( NF.C_DDDEMIT ).equals( "" ) ? "(" + cab.getString( NF.C_DDDEMIT ) + ")" : "" ) + ( !cab.getString( NF.C_FONEEMIT ).equals( "" ) ? Funcoes.setMascara( cab.getString( NF.C_FONEEMIT ).trim(), "####-####" ) : "" ).trim() );
					imp.say( 47, sValsCli[ 3 ] );

					imp.pulaLinha( 3 );

					// Fim do cabeçalho
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

					imp.pulaLinha( 1 );

					if ( complementar ) {
						imp.say( 8, Funcoes.copy( itens.getString( NF.C_OBSITPED ).trim(), 48 ) );
					}
					else {

						imp.say( 2, itens.getString( NF.C_REFPROD ) );
						imp.say( 8, itens.getString( NF.C_DESCPROD ).trim() );
						imp.say( 41, Funcoes.copy( itens.getString( NF.C_CODUNID ), 4 ).trim() );
						imp.say( 44, Funcoes.strDecimalToStrCurrency( 10, 4, String.valueOf( itens.getBigDecimal( NF.C_QTDITPED ) ) ) );
						imp.say( 51, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( itens.getBigDecimal( NF.C_VLRPRODITPED ).divide( itens.getBigDecimal( NF.C_QTDITPED ), 2, BigDecimal.ROUND_HALF_UP ) ) ) );

						imp.say( 67, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( itens.getBigDecimal( NF.C_VLRPRODITPED ) ) ) );

						// imp.say( 121, Funcoes.strDecimalToStrCurrency( 2, 0, String.valueOf( itens.getBigDecimal( NF.C_PERCICMSITPED ) ) ) );
						// imp.say( 124, Funcoes.strDecimalToStrCurrency( 2, 0, String.valueOf( itens.getBigDecimal( NF.C_PERCIPIITPED ) ) ) );
						// imp.say( 129, Funcoes.strDecimalToStrCurrency( 6, 2, String.valueOf( itens.getBigDecimal( NF.C_VLRIPIITPED ) ) ) );

					}

					iProdImp++;

					vlricmsorig = vlricmsorig.add( ( itens.getBigDecimal( NF.C_PERCICMSITPED ).multiply( itens.getBigDecimal( NF.C_VLRPRODITPED ).divide( new BigDecimal( 100 ) ) ) ) );

				}

				// Fim da impressão do item

				iItImp++;
				if ( ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) || ( imp.pRow() == MAXLINE - 1 ) ) {

					if ( iContaFrete == 0 ) {
						frete.next();
						iContaFrete++;
					}

					// Imprimir o destaque do funrural.

					if ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) {
						imp.pulaLinha( MAXPROD - itens.getInt( NF.C_CONTAITENS ) );

						imp.say( 8, "-" + Funcoes.strDecimalToStrCurrency( 3, 1, String.valueOf( itens.getBigDecimal( NF.C_ALIQFUNRURALITCOMPRA ) ) ) + "% FUNRUAL" );
						imp.say( 67, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( cab.getBigDecimal( NF.C_VLRFUNRURALCOMPRA ) ) ) );

					}

					imp.pulaLinha( 5 );

					// Imprime totais

					if ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) {

						// imp.pulaLinha( 3 );
						// imp.say( 4, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getBigDecimal( NF.C_VLRBASEICMSPED ) ) ) );
						// imp.say( 25, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getBigDecimal( NF.C_VLRICMSPED ) ) ) );

						BigDecimal vlrliq = cab.getBigDecimal( NF.C_VLRLIQPED );
						BigDecimal vlradic = cab.getBigDecimal( NF.C_VLRADICPED );
						BigDecimal vlrtotnota = vlrliq.add( vlradic );
						BigDecimal vlrprod = cab.getBigDecimal( NF.C_VLRPRODPED );
						BigDecimal vlripi = cab.getBigDecimal( NF.C_VLRIPIPED );
						BigDecimal vlrfrete = frete.getBigDecimal( NF.C_VLRFRETEPED );
						BigDecimal vlrdesc = cab.getBigDecimal( NF.C_VLRDESCVENDA );

						vlrliq = vlrliq.add( vlradic );
						vlrliq = vlrliq.add( vlripi );
						vlrliq = vlrliq.subtract( vlrdesc );

						if ( !complementar ) {

							// imp.say( 61, Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( cab.getBigDecimal( NF.C_VLRBASEICMSST ) ) ) );
							// imp.say( 90, Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( cab.getBigDecimal( NF.C_VLRICMSST ) ) ) );
							imp.say( 58, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getBigDecimal( NF.C_VLRPRODPED ) ) ) );
						}

						imp.pulaLinha( 2 );

						if ( !complementar ) {
							// imp.say( 48, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( ( nf.getTipoNF() == NF.TPNF_ENTRADA ?
							// cab.getBigDecimal( NF.C_VLRFRETEPED ) :
							// frete.getBigDecimal( NF.C_VLRFRETEPED ) ) ) ) );

							// imp.say( 71, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getBigDecimal( NF.C_VLRADICPED ) ) ) );
							// imp.say( 90, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getBigDecimal( NF.C_VLRIPIPED ) ) ) );

						}

						if ( nf.getTipoNF() == NF.TPNF_ENTRADA ) {

							imp.say( 58, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( vlrliq ) ) );
						}
						else {
							if ( !complementar ) {
								imp.say( 58, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( vlrtotnota ) ) ); // VALOR TOTAL DA NOTA
							}
						}
					}
					else {
						imp.pulaLinha( 0 );
						imp.say( 4, "********************" );
						imp.say( 32, "********************" );
						imp.say( 114, "********************" );
						imp.pulaLinha( 2 );
						imp.say( 58, "********************" );
						imp.say( 58, "********************" );
						imp.say( 87, "********************" );
						imp.say( 114, "********************" );
					}

					// Fim da impressão dos totais

					// Imprime informações do frete

					imp.pulaLinha( 3 );

					if ( !complementar ) {

						// imp.say( 1, frete.getString( NF.C_RAZTRANSP ) );
						imp.say( 49, "C".equals( frete.getString( NF.C_TIPOFRETE ) ) ? "1" : "2" );
						// imp.say( 106, frete.getString( NF.C_UFFRETE ) );

						if ( "C".equals( frete.getString( NF.C_TIPOTRANSP ) ) ) {
							// imp.say( 112, Funcoes.setMascara( cab.getString( NF.C_CNPJEMIT ), "##.###.###/####-##" ) );
						}
						else {
							if ( "".equals( frete.getString( NF.C_CNPJTRANSP ) ) ) {
								// imp.say( 112, Funcoes.setMascara( frete.getString( NF.C_CPFTRANSP ), "###.###.###-##" ) );
							}
							else {
								// imp.say( 112, Funcoes.setMascara( frete.getString( NF.C_CNPJTRANSP ), "##.###.###/####-##" ) );
							}
						}

						imp.pulaLinha( 3 );
						if ( frete.getString( NF.C_ENDTRANSP ) != null ) {
							// imp.say( 1, frete.getString( NF.C_ENDTRANSP ).trim() + ( !(frete.getString( NF.C_ENDTRANSP ) == null) ? (", " + frete.getInt( NF.C_NUMTRANSP )) : "" ));
							imp.say( 72, frete.getString( NF.C_CIDTRANSP ) );
							imp.say( 106, frete.getString( NF.C_UFTRANSP ) );
						}

						if ( frete.getString( NF.C_TIPOTRANSP ) != null && frete.getString( NF.C_TIPOTRANSP ).equals( "C" ) ) {
							imp.say( 112, cab.getString( NF.C_INSCEMIT ) );
						}
						else {
							imp.say( 112, frete.getString( NF.C_INSCTRANSP ) );
						}

						imp.pulaLinha( 0 );
						if ( frete.getString( NF.C_QTDFRETE ) != null )
							// imp.say( 1, Funcoes.strDecimalToStrCurrency( 12, 2, String.valueOf( frete.getString( NF.C_QTDFRETE ) ) ) );
							// imp.say( 21, Funcoes.copy( frete.getString( NF.C_ESPFRETE ), 27 ) );
							// imp.say( 82, Funcoes.copy( frete.getString( NF.C_CONHECFRETEPED ), 20 ) );
							imp.say( 50, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( frete.getBigDecimal( NF.C_PESOBRUTO ) ) ) );
						imp.say( 63, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( frete.getBigDecimal( NF.C_PESOLIQ ) ) ) );
						imp.pulaLinha( 2 );
					}
					else {
						imp.pulaLinha( 6 );
					}

					// Fim da impressão do frete

					// Imprime observação e classificações fiscais

					if ( nf.getTipoNF() == NF.TPNF_ENTRADA ) {
						vObsVenda = Funcoes.strToVectorSilabas( sObsVenda, 70 );
					}
					else {
						vObsVenda = Funcoes.strToVectorSilabas( ( sDescFisc.length() > 0 ? sDescFisc + "\n" : "" ) + sObsVenda, 40 );
					}

					sizeObs = vSigla.size();
					sizeObs = vObsVenda.size() > sizeObs ? vObsVenda.size() : sizeObs;

					int aux = 0;
					imp.pulaLinha( 2 );
					for ( int i = 0; i < 4; i++ ) {
						if ( i < vObsVenda.size() ) {
							if ( nf.getTipoNF() == NF.TPNF_ENTRADA ) {
								imp.say( 2, Funcoes.copy( (String) vObsVenda.elementAt( i ), 70 ) );
							}
							else {
								imp.say( 2, Funcoes.copy( (String) vObsVenda.elementAt( i ), 40 ) );
							}
						}
						imp.pulaLinha( 1 );
					}

					BigDecimal vlrdiferido = vlricmsorig.setScale( 2, BigDecimal.ROUND_HALF_UP ).subtract( ( cab.getBigDecimal( NF.C_VLRICMSPED ).setScale( 2, BigDecimal.ROUND_HALF_UP ) ) );

					if ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) {
						if ( !complementar ) {
							if ( vlrdiferido.compareTo( new BigDecimal( 0 ) ) < 0 ) {
								vlrdiferido = new BigDecimal( 0 );
							}
							if ( vlrdiferido.compareTo( new BigDecimal( 0 ) ) > 0 ) {
								imp.say( 2, "VALOR DO ICMS DIFERIDO:" + Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( vlrdiferido ) ) );
							}
						}
					}
					else {
						imp.say( 2, "***********************" );
					}

					// Fim da observação

					imp.pulaLinha( 5 );
					// imp.say( 120, StringFunctions.strZero( String.valueOf( iNumNota ), 6 ) );

					// Imprime canhoto

					imp.pulaLinha( 4 );

					imp.pulaLinha( iLinPag - imp.pRow() );
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
