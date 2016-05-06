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
import org.freedom.library.swing.frame.Aplicativo;

public class NF044 extends Layout {

	public boolean imprimir( NF nf, ImprimeOS imp ) {

		boolean retorno = super.imprimir( nf, imp );
		boolean bFat = true;
		boolean bNat = true;
		boolean bjatem = false;
		boolean bvlriss = true;
		final int MAXLINE = 40;
		final int MAXPROD = 14;
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
		Vector<Object[]> vServico = new Vector<Object[]>();
		Object[] itemServ = null;

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

			vClfisc.addElement( "" );

			while ( itens.next() ) {

				if ( bNat ) {
					sNat[ 0 ] = Funcoes.copy( itens.getString( NF.C_DESCNAT ).trim(), 44 );
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

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 132, StringFunctions.strZero( String.valueOf( iNumNota ), 6 ) );

					// Imprime cabeçalho da nota

					imp.pulaLinha( 1, imp.comprimido() );

					if ( nf.getTipoNF() == NF.TPNF_ENTRADA ) {
						imp.say( 104, "X" );
					}
					else {
						imp.say( 90, "X" );
					}

					imp.pulaLinha( 4, imp.comprimido() );
					imp.say( 2, sNat[ 0 ] );
					imp.say( 51, sNat[ 1 ] );
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 14, sValsCli[ 1 ] );
					imp.say( 98, !sValsCli[ 0 ].equals( "" ) ? Funcoes.setMascara( sValsCli[ 0 ], "###.###.###-##" ) : Funcoes.setMascara( cab.getString( NF.C_CNPJEMIT ), "##.###.###/####-##" ) );
					imp.say( 124, ( cab.getDate( NF.C_DTEMITPED ) != null ? Funcoes.dateToStrDate( cab.getDate( NF.C_DTEMITPED ) ) : "" ) );
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 2, Funcoes.copy( cab.getString( NF.C_ENDEMIT ), 0, 50 ).trim() + ", " + Funcoes.copy( cab.getString( NF.C_NUMEMIT ), 0, 6 ).trim() + " - " + Funcoes.copy( cab.getString( NF.C_COMPLEMIT ), 0, 9 ).trim() );
					imp.say( 80, Funcoes.copy( cab.getString( NF.C_BAIREMIT ), 0, 23 ) );
					imp.say( 101, Funcoes.setMascara( cab.getString( NF.C_CEPEMIT ), "#####-###" ) );

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 8, sValsCli[ 2 ] );
					imp.say( 68, ( !cab.getString( NF.C_DDDEMIT ).equals( "" ) ? "(" + cab.getString( NF.C_DDDEMIT ) + ")" : "" ) + ( !cab.getString( NF.C_FONEEMIT ).equals( "" ) ? Funcoes.setMascara( cab.getString( NF.C_FONEEMIT ).trim(), "####-####" ) : "" ).trim() );
					imp.say( 86, sValsCli[ 3 ] );
					imp.say( 102, !cab.getString( NF.C_RGEMIT ).equals( "" ) ? cab.getString( NF.C_RGEMIT ) : cab.getString( NF.C_INSCEMIT ) );

					// Fim do cabeçalho

					// Imprime dados da fatura

					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 4, sDuplics[ 0 ] );
					imp.say( 20, sVals[ 0 ] );
					imp.say( 36, sVencs[ 0 ] );
					imp.say( 50, sDuplics[ 1 ] );
					imp.say( 65, sVals[ 1 ] );
					imp.say( 80, sVencs[ 1 ] );
					imp.say( 96, sDuplics[ 2 ] );
					imp.say( 112, sVals[ 2 ] );
					imp.say( 127, sVencs[ 2 ] );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 4, sDuplics[ 3 ] );
					imp.say( 20, sVals[ 3 ] );
					imp.say( 36, sVencs[ 3 ] );
					imp.say( 50, sDuplics[ 4 ] );
					imp.say( 65, sVals[ 4 ] );
					imp.say( 80, sVencs[ 4 ] );
					imp.say( 96, sDuplics[ 5 ] );
					imp.say( 112, sVals[ 5 ] );
					imp.say( 127, sVencs[ 5 ] );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 4, sDuplics[ 6 ] );
					imp.say( 20, sVals[ 6 ] );
					imp.say( 36, sVencs[ 6 ] );
					imp.say( 50, sDuplics[ 7 ] );
					imp.say( 65, sVals[ 7 ] );
					imp.say( 80, sVencs[ 7 ] );
					imp.say( 96, sDuplics[ 8 ] );
					imp.say( 112, sVals[ 8 ] );
					imp.say( 127, sVencs[ 8 ] );
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

				// Imprime os dados do item no corpo da nota e serviços

				if ( !"S".equals( itens.getString( NF.C_TIPOPROD ) ) ) {

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 2, itens.getString( NF.C_REFPROD ) );
					imp.say( 17, Funcoes.copy( itens.getString( NF.C_DESCPROD ).trim(), 48 ) );
					imp.say( 73, sSigla );
					imp.say( 80, Funcoes.copy( itens.getString( NF.C_ORIGFISC ), 0, 1 ) + Funcoes.copy( itens.getString( NF.C_CODTRATTRIB ), 0, 2 ) );
					imp.say( 86, Funcoes.copy( itens.getString( NF.C_CODUNID ), 4 ) );
					imp.say( 88, Funcoes.strDecimalToStrCurrency( 8, 2, String.valueOf( itens.getFloat( NF.C_QTDITPED ) ) ) );
					imp.say( 99, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( itens.getFloat( NF.C_VLRPRODITPED ) / itens.getFloat( NF.C_QTDITPED ) ) ) );
					imp.say( 120, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( itens.getFloat( NF.C_VLRPRODITPED ) ) ) );
					iProdImp++;
				}
				else {

					itemServ = new Object[ 5 ];
					itemServ[ 0 ] = new BigDecimal( itens.getFloat( NF.C_QTDITPED ) );
					itemServ[ 1 ] = Funcoes.copy( itens.getString( NF.C_DESCPROD ).trim(), 48 );
					itemServ[ 2 ] = new BigDecimal( itens.getFloat( NF.C_VLRPRODITPED ) );
					itemServ[ 3 ] = itens.getString( NF.C_REFPROD );
					itemServ[ 4 ] = String.valueOf( itens.getFloat( NF.C_VLRPRODITPED ) / itens.getFloat( NF.C_QTDITPED ) );
					vServico.addElement( itemServ );

				}

				// Fim da impressão do item e serviços.
				iItImp++;
				if ( ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) || ( imp.pRow() == MAXLINE - 1 ) ) {

					if ( iContaFrete == 0 ) {
						frete.next();
						iContaFrete++;
					}

					// imp.pulaLinha( MAXLINE - imp.pRow(), imp.comprimido());

					// Imprime totais
					for ( int iserv = 0; iserv < 10; iserv++ ) {
						if ( iserv < vServico.size() ) {
							itemServ = vServico.elementAt( iserv );
							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( 2, String.valueOf( itemServ[ 3 ] ) );
							imp.say( 17, Funcoes.copy( String.valueOf( itemServ[ 1 ] ), 48 ) );
							imp.say( 88, Funcoes.strDecimalToStrCurrency( 8, 2, String.valueOf( itemServ[ 0 ] ) ) );
							imp.say( 99, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( itemServ[ 4 ] ) ) );
							imp.say( 120, Funcoes.strDecimalToStrCurrency( 10, 2, itemServ[ 2 ].toString() ) );
						}
						else if ( ( iserv % 2 ) == 0 ) {
							imp.pulaLinha( 1, imp.comprimido() );
						}
					}

					if ( cab.getFloat( NF.C_VLRDESCVENDA ) > 0 ) {

						imp.say( 17, "Valor do desconto : " + Funcoes.strDecimalToStrCurrency( 9, 2, String.valueOf( cab.getFloat( NF.C_VLRDESCVENDA ) ) ) );
					}

					if ( cab.getFloat( NF.C_VLRDESCITPED ) > 0 ) {

						imp.say( 17, "Valor do desconto : " + Funcoes.strDecimalToStrCurrency( 9, 2, String.valueOf( cab.getFloat( NF.C_VLRDESCITPED ) ) ) );
					}

					imp.pulaLinha( MAXLINE - imp.pRow(), imp.comprimido() );

					if ( vServico.size() > 0 ) {

						imp.say( 35, Funcoes.strDecimalToStrCurrency( 20, Aplicativo.casasDecFin, String.valueOf( cab.getFloat( NF.C_BASEISS ) ) ) );
						imp.say( 58, Funcoes.strDecimalToStrCurrency( 20, Aplicativo.casasDecFin, String.valueOf( cab.getFloat( NF.C_PERCISS ) ) ) );
						imp.say( 87, Funcoes.strDecimalToStrCurrency( 20, Aplicativo.casasDecFin, String.valueOf( cab.getFloat( NF.C_VLRISS ) ) ) );
						imp.say( 114, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRPRODPED ) ) ) );
					}

					if ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) {
						imp.pulaLinha( 3, imp.comprimido() );
						imp.say( 114, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRPRODPED ) - cab.getFloat( NF.C_BASEISS ) ) ) );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 4, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( frete.getFloat( NF.C_VLRFRETEPED ) ) ) );
						imp.say( 58, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRADICPED ) ) ) );
						imp.say( 87, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRIPIPED ) ) ) );
						imp.say( 114, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRLIQPED ) ) ) );
						iItImp = 0;
					}
					else {
						imp.pulaLinha( 0, imp.comprimido() );
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
					imp.say( 2, frete.getString( NF.C_RAZTRANSP ) );
					imp.say( 79, "C".equals( frete.getString( NF.C_TIPOFRETE ) ) ? "1" : "2" );
					imp.say( 90, frete.getString( NF.C_PLACAFRETE ) );
					imp.say( 105, frete.getString( NF.C_UFFRETE ) );

					if ( "C".equals( frete.getString( NF.C_TIPOTRANSP ) ) ) {
						imp.say( 112, Funcoes.setMascara( cab.getString( NF.C_CNPJEMIT ), "##.###.###/####-##" ) );
					}
					else {
						if ( "".equals( frete.getString( NF.C_CNPJTRANSP ) ) ) {
							imp.say( 112, Funcoes.setMascara( frete.getString( NF.C_CPFTRANSP ), "###.###.###-##" ) );
						}
						else {
							imp.say( 116, Funcoes.setMascara( frete.getString( NF.C_CNPJTRANSP ), "##.###.###/####-##" ) );
						}
					}

					imp.pulaLinha( 1, imp.comprimido() );
					if ( frete.getString( NF.C_ENDTRANSP ) != null ) {
						imp.say( 2, frete.getString( NF.C_ENDTRANSP ).trim() + ", " + frete.getInt( NF.C_NUMTRANSP ) );
						imp.say( 74, frete.getString( NF.C_CIDTRANSP ) );
						imp.say( 105, frete.getString( NF.C_UFTRANSP ) );
					}

					if ( frete.getString( NF.C_TIPOTRANSP ) != null ) {
						if ( frete.getString( NF.C_TIPOTRANSP ).equals( "C" ) ) {
							imp.say( 121, cab.getString( NF.C_INSCEMIT ) );
						}
						else {
							imp.say( 121, frete.getString( NF.C_INSCTRANSP ) );
						}
					}

					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 4, Funcoes.strDecimalToStrCurrency( 12, 2, String.valueOf( frete.getString( NF.C_QTDFRETE ) ) ) );
					imp.say( 21, Funcoes.copy( frete.getString( NF.C_ESPFRETE ), 27 ) );
					imp.say( 50, Funcoes.copy( frete.getString( NF.C_MARCAFRETE ), 22 ) );
					imp.say( 82, Funcoes.copy( frete.getString( NF.C_CONHECFRETEPED ), 20 ) );
					imp.say( 108, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( frete.getFloat( NF.C_PESOBRUTO ) ) ) );
					imp.say( 124, Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( frete.getFloat( NF.C_PESOLIQ ) ) ) );
					imp.pulaLinha( 1, imp.comprimido() );

					// Fim da impressão do frete

					// Imprime observação e classificações fiscais

					vObsVenda = Funcoes.strToVectorSilabas( ( sDescFisc.length() > 0 ? sDescFisc + "\n" : "" ) + sObsVenda, 40 );

					sizeObs = vSigla.size();
					sizeObs = vObsVenda.size() > sizeObs ? vObsVenda.size() : sizeObs;

					int aux = 0;
					for ( int i = 0; i < 7; i++ ) {
						if ( aux < sizeObs ) {
							imp.pulaLinha( 1, imp.comprimido() );
							if ( vSigla.size() > 0 && indexSigla < vSigla.size() ) {
								imp.say( 2, vSigla.elementAt( indexSigla++ ) );
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

					imp.pulaLinha( 4, imp.comprimido() );
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
			sValsCli = null;
			sNat = null;
			sVencs = null;
			sVals = null;
			System.gc();
		}

		return retorno;

	}

}
