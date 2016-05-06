/**
 * @version 10/12/2003 <BR>
 * @author Setpoint Informática Ltda./Alex Rodrigues <BR>
 * 
 *         Projeto: Freedom <BR>
 *         Pacote: leiautes <BR>
 *         Classe:
 * @(#)NF011.java <BR>
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
 *                Layout da nota fiscal para a empresa Mathias e Buzzi.
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

public class NF011 extends Layout {

	private String sMensAdic = "";

	private String sNumNota = "";

	public boolean imprimir( NF nf, ImprimeOS imp ) {

		boolean retorno = super.imprimir( nf, imp );
		int iNumNota = 0;
		int iProd = 0;
		final int MAXPROD = 15;
		boolean bTotalizou = false;
		boolean bjatem = false;
		String sCodfisc = "";
		String sSigla = "";
		String sDesc = "";
		String sHora = null;
		String[] sNat = new String[ 4 ];
		String[] sVencs = new String[ 6 ];
		String[] sVals = new String[ 6 ];
		Vector<?> vMatObs = null;
		Vector<?> vDesc = null;
		Vector<String> vValores = new Vector<String>();
		Vector<String> vClfisc = new Vector<String>();
		Vector<String> vSigla = new Vector<String>();
		Calendar cHora = Calendar.getInstance();

		try {

			imp.limpaPags();

			vClfisc.addElement( "" );

			sHora = StringFunctions.strZero( String.valueOf( cHora.get( Calendar.HOUR_OF_DAY ) ), 2 ) + ":" + StringFunctions.strZero( String.valueOf( cHora.get( Calendar.MINUTE ) ), 2 ) + ":" + StringFunctions.strZero( String.valueOf( cHora.get( Calendar.SECOND ) ), 2 );

			if ( cab.next() ) {

				sNumNota = StringFunctions.strZero( String.valueOf( cab.getInt( NF.C_DOC ) ), 6 );
				vMatObs = Funcoes.strToVectorSilabas( cab.getString( NF.C_OBSPED ), 70 );
			}
			else {

				sNumNota = StringFunctions.strZero( String.valueOf( iNumNota ), 6 );
			}

			while ( itens.next() ) {

				for ( int i = 0; i < 6; i++ ) {

					if ( parc.next() ) {

						sVencs[ i ] = Funcoes.dateToStrDate( parc.getDate( NF.C_DTVENCTO ) );
						sVals[ i ] = Funcoes.strDecimalToStrCurrency( 12, 2, String.valueOf( parc.getBigDecimal( NF.C_VLRPARC ).setScale( 2, BigDecimal.ROUND_HALF_UP ) ) );
					}
					else {

						sVencs[ i ] = "**********";
						sVals[ i ] = "************";
					}
				}

				sNat[ 0 ] = itens.getString( NF.C_DESCNAT );
				sNat[ 1 ] = Funcoes.setMascara( itens.getString( NF.C_CODNAT ), "#.##" );

				if ( imp.pRow() == 0 ) {

					imp.pulaLinha( 2, imp.comprimido() );

					if ( nf.getTipoNF() == NF.TPNF_ENTRADA ) {

						imp.say( 102, "X" );
					}
					else {

						imp.say( 88, "X" );
					}

					imp.say( 124, sNumNota );
					imp.pulaLinha( 5, imp.comprimido() );
					imp.say( 6, sNat[ 0 ] );
					imp.say( 46, sNat[ 1 ] );
					imp.pulaLinha( 3, imp.comprimido() );
					imp.say( 6, cab.getInt( NF.C_CODEMIT ) + " - " + cab.getString( NF.C_RAZEMIT ) );
					imp.say( 90, !cab.getString( NF.C_CPFEMIT ).equals( "" ) ? Funcoes.setMascara( cab.getString( NF.C_CPFEMIT ), "###.###.###-##" ) : Funcoes.setMascara( cab.getString( NF.C_CNPJEMIT ), "##.###.###/####-##" ) );
					imp.say( 125, cab.getDate( NF.C_DTEMITPED ) != null ? Funcoes.dateToStrDate( cab.getDate( NF.C_DTEMITPED ) ) : "" );
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 6, Funcoes.copy( cab.getString( NF.C_ENDEMIT ), 0, 50 ).trim() + ", " + Funcoes.copy( String.valueOf( cab.getInt( NF.C_NUMEMIT ) ), 0, 6 ) + " - " + ( !cab.getString( NF.C_COMPLEMIT ).equals( "" ) ? Funcoes.copy( cab.getString( NF.C_COMPLEMIT ), 0, 9 ).trim() : "" ) );
					imp.say( 76, !cab.getString( NF.C_BAIREMIT ).equals( "" ) ? Funcoes.copy( cab.getString( NF.C_BAIREMIT ), 0, 15 ) : "" );
					imp.say( 106, Funcoes.setMascara( cab.getString( NF.C_CEPEMIT ), "#####-###" ) );
					imp.say( 125, cab.getDate( NF.C_DTSAIDA ) != null ? Funcoes.dateToStrDate( cab.getDate( NF.C_DTSAIDA ) ) : "" );
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 6, cab.getString( NF.C_CIDEMIT ) );
					imp.say( 50, ( !cab.getString( NF.C_DDDEMIT ).equals( "" ) ? "(" + cab.getString( NF.C_DDDEMIT ) + ")" : "" ) + ( !cab.getString( NF.C_FONEEMIT ).equals( "" ) ? Funcoes.setMascara( cab.getString( NF.C_FONEEMIT ), "####-####" ) : "" ) );
					imp.say( 83, cab.getString( NF.C_UFEMIT ) );
					imp.say( 90, !cab.getString( NF.C_RGEMIT ).equals( "" ) ? cab.getString( NF.C_RGEMIT ) : cab.getString( NF.C_INSCEMIT ) );
					imp.say( 126, sHora );
					imp.pulaLinha( 5, imp.comprimido() );
					imp.say( 8, sVals[ 0 ] );
					imp.say( 36, sVencs[ 0 ] );
					imp.say( 55, sVals[ 1 ] );
					imp.say( 83, sVencs[ 1 ] );
					imp.say( 102, sVals[ 2 ] );
					imp.say( 126, sVencs[ 2 ] );
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 8, sVals[ 3 ] );
					imp.say( 36, sVencs[ 3 ] );
					imp.say( 55, sVals[ 4 ] );
					imp.say( 83, sVencs[ 4 ] );
					imp.say( 102, sVals[ 5 ] );
					imp.say( 126, sVencs[ 5 ] );
					imp.pulaLinha( 3, imp.comprimido() );

				}

				if ( !"S".equals( itens.getString( NF.C_TIPOPROD ) ) && iProd < MAXPROD ) {

					imp.pulaLinha( 1, imp.comprimido() );

					imp.say( 6, String.valueOf( itens.getInt( NF.C_CODPROD ) ) );

					vDesc = Funcoes.strToVectorSilabas( ( "".equals( itens.getString( NF.C_OBSITPED ) ) ? itens.getString( NF.C_DESCPROD ).trim() : itens.getString( NF.C_OBSITPED ).trim() ) + itens.getString( NF.C_CODLOTE ), 46 );

					for ( int i = 0; ( i < ( MAXPROD - iProd ) ) && ( vDesc.size() > i ); i++ ) {

						sDesc = vDesc.elementAt( i ).toString();

						if ( i > 0 ) {

							imp.pulaLinha( 1, imp.comprimido() );
							iProd++;
						}

						imp.say( 16, sDesc );
					}

					sMensAdic = itens.getString( NF.C_DESCFISC ) + " - ";
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

					if ( iProd < MAXPROD ) {

						imp.say( 66, Funcoes.copy( itens.getString( NF.C_ORIGFISC ), 0, 1 ) + Funcoes.copy( itens.getString( NF.C_CODTRATTRIB ), 0, 2 ) );
						imp.say( 73, sSigla );

						imp.say( 76, Funcoes.copy( itens.getString( NF.C_CODUNID ), 4 ) );
						imp.say( 79, String.valueOf( itens.getBigDecimal( NF.C_QTDITPED ) ) );
						imp.say( 89, Funcoes.strDecimalToStrCurrency( 8, 2, String.valueOf( itens.getBigDecimal( NF.C_VLRPRODITPED ).divide( itens.getBigDecimal( NF.C_QTDITPED ), 2, BigDecimal.ROUND_HALF_UP ) ) ) );
						imp.say( 103, Funcoes.strDecimalToStrCurrency( 13, 2, String.valueOf( itens.getBigDecimal( NF.C_VLRLIQITPED ) ) ) );
						imp.say( 118, String.valueOf( itens.getBigDecimal( NF.C_PERCICMSITPED ) ) );
						iProd++;
					}
				}

				if ( !bTotalizou ) {

					frete.next();

					vValores.addElement( String.valueOf( cab.getBigDecimal( NF.C_VLRBASEICMSPED ) ) );// 0
					vValores.addElement( String.valueOf( cab.getBigDecimal( NF.C_VLRICMSPED ) ) );// 1
					vValores.addElement( String.valueOf( cab.getBigDecimal( NF.C_VLRPRODPED ) ) );// 2
					vValores.addElement( String.valueOf( frete.getBigDecimal( NF.C_VLRFRETEPED ) ) );// 3
					vValores.addElement( String.valueOf( itens.getBigDecimal( NF.C_VLRADICITPED ) ) );// 4
					vValores.addElement( String.valueOf( cab.getBigDecimal( NF.C_VLRIPIPED ) ) );// 5
					vValores.addElement( String.valueOf( cab.getBigDecimal( NF.C_VLRLIQPED ) ) );// 6
					vValores.addElement( frete.getString( NF.C_RAZTRANSP ) );// 7
					vValores.addElement( frete.getString( NF.C_TIPOFRETE ) );// 8
					vValores.addElement( frete.getString( NF.C_PLACAFRETE ) );// 9
					vValores.addElement( frete.getString( NF.C_UFFRETE ) );// 10
					vValores.addElement( frete.getString( NF.C_TIPOTRANSP ) );// 11
					vValores.addElement( cab.getString( NF.C_CNPJEMIT ) );// 12
					vValores.addElement( frete.getString( NF.C_CNPJTRANSP ) ); // 13
					vValores.addElement( frete.getString( NF.C_ENDTRANSP ) );// 14

					if ( frete.getString( NF.C_TIPOTRANSP ).equals( "C" ) ) {

						vValores.addElement( "" );// 15
						vValores.addElement( "" );// 16
						vValores.addElement( "" );// 17
						vValores.addElement( "" );// 18
					}
					else {

						vValores.addElement( String.valueOf( frete.getInt( NF.C_NUMTRANSP ) ) );// 15
						vValores.addElement( frete.getString( NF.C_CIDTRANSP ) );// 16
						vValores.addElement( frete.getString( NF.C_UFTRANSP ) );// 17
						vValores.addElement( frete.getString( NF.C_INSCTRANSP ) );// 18
					}

					vValores.addElement( String.valueOf( frete.getBigDecimal( NF.C_QTDFRETE ).setScale( 0 ) ) );// 19
					vValores.addElement( frete.getString( NF.C_ESPFRETE ) );// 20
					vValores.addElement( frete.getString( NF.C_MARCAFRETE ) );// 21
					vValores.addElement( String.valueOf( frete.getBigDecimal( NF.C_PESOBRUTO ) ) );// 22
					vValores.addElement( String.valueOf( frete.getBigDecimal( NF.C_PESOLIQ ) ) );// 23
					vValores.addElement( String.valueOf( itens.getBigDecimal( NF.C_VLRISSITPED ) ) );// 24
					vValores.addElement( String.valueOf( cab.getInt( NF.C_CODVEND ) ) );// 25

					if ( cab.getString( NF.C_NOMEVEND ) == null ) {

						vValores.addElement( StringFunctions.replicate( " ", 25 ) );// 26
					}
					else {

						vValores.addElement( cab.getString( NF.C_NOMEVEND ) + StringFunctions.replicate( " ", 25 - cab.getString( NF.C_NOMEVEND ).length() ) );
					}

					bTotalizou = true;
				}
			}

			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 16, "CLASSIFICACAO FISCAL" );

			int pos = 1;

			for ( int i = 0; i < vSigla.size(); i++ ) {

				if ( pos == 1 ) {

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 16, vSigla.elementAt( i ) );
					pos = 2;
				}
				else {

					imp.say( 35, vSigla.elementAt( i ) );
					pos = 1;
					iProd++;
				}
			}

			if ( cab.getBigDecimal( NF.C_VLRDESCITPED ).doubleValue() > 0 ) {

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 16, "Valor do desconto : " + Funcoes.strDecimalToStrCurrency( 9, 2, String.valueOf( cab.getBigDecimal( NF.C_VLRDESCITPED ) ) ) );
			}

			if ( vMatObs != null ) {

				imp.pulaLinha( ( 45 - vMatObs.size() ) - imp.pRow(), imp.comprimido() );

				for ( int i = 0; i < vMatObs.size(); i++ ) {

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 10, (String) vMatObs.elementAt( i ) );
				}
			}
			/*
			 * imp.pulaLinha( 43 - imp.pRow(), imp.comprimido() ); imp.say( 10, "PRORROGADO O PRAZO DE VALIDADE DA EMISSÃO DAS NOTAS FISCAIS PARA 18 MESES." ); imp.pulaLinha( 1, imp.comprimido() ); imp.say( 10, "DE ACORDO COM O DECRETO N. 5502 DE 10 DE OUTUBRO DE 2005," ); imp.pulaLinha( 1,
			 * imp.comprimido() ); imp.say( 10, "COM VIGENCIA A PARTIR DE 1 DE SETEMBRO DE 2005." );
			 */
			impTotais( imp, vValores );

			imp.fechaGravacao();

			retorno = true;

			if ( iProd >= MAXPROD ) {

				Funcoes.mensagemInforma( null, "Podem haver erros na impressão da nota fiscal.\n" + "Produtos ultrapassam " + MAXPROD + " linhas!" );
			}

		} catch ( Exception err ) {

			err.printStackTrace();
			Funcoes.mensagemErro( null, "Erro ao montar Nota Fiscal!\n" + err.getMessage() );
			retorno = false;
		} finally {

			sCodfisc = null;
			sSigla = null;
			sHora = null;
			sNat = null;
			sVencs = null;
			sVals = null;
			vMatObs = null;
			vValores = null;
			vClfisc = null;
			vSigla = null;
			cHora = null;
			System.gc();
		}

		return retorno;

	}

	private void impTotais( ImprimeOS imp, Vector<String> vValores ) {

		Vector<?> vObs = null;

		try {

			imp.pulaLinha( 47 - imp.pRow(), imp.comprimido() );
			imp.say( 6, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 0 ).toString() ) );
			imp.say( 35, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 1 ).toString() ) );
			imp.say( 117, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 2 ).toString() ) );
			imp.pulaLinha( 2, imp.comprimido() );
			imp.say( 6, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 3 ).toString() ) );
			imp.say( 65, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 4 ).toString() ) );
			imp.say( 90, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 5 ).toString() ) );
			imp.say( 117, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 6 ).toString() ) );
			imp.pulaLinha( 3, imp.comprimido() );
			imp.say( 8, vValores.elementAt( 7 ) != null ? vValores.elementAt( 7 ).toString() : "" );
			imp.say( 80, vValores.elementAt( 8 ) != null ? ( vValores.elementAt( 8 ).toString().equals( "C" ) ? "1" : "2" ) : "" );
			imp.say( 87, vValores.elementAt( 9 ) != null ? vValores.elementAt( 9 ).toString() : "" );
			imp.say( 103, vValores.elementAt( 10 ) != null ? vValores.elementAt( 10 ).toString() : "" );

			if ( !vValores.elementAt( 11 ).toString().equals( "C" ) ) {

				imp.say( 118, Funcoes.setMascara( vValores.elementAt( 13 ) != null ? vValores.elementAt( 13 ).toString() : "", "##.###.###/####-##" ) );
			}

			imp.pulaLinha( 2, imp.comprimido() );
			imp.say( 8, vValores.elementAt( 14 ) != null ? vValores.elementAt( 14 ).toString().trim() : "" );
			imp.say( 68, vValores.elementAt( 16 ) != null ? vValores.elementAt( 16 ).toString().trim() : "" );
			imp.say( 102, vValores.elementAt( 17 ) != null ? vValores.elementAt( 17 ).toString().trim() : "" );
			imp.say( 121, vValores.elementAt( 18 ) != null ? vValores.elementAt( 18 ).toString() : "" );
			imp.pulaLinha( 2, imp.comprimido() );
			imp.say( 8, vValores.elementAt( 19 ) != null ? vValores.elementAt( 19 ).toString() : "" );
			imp.say( 25, vValores.elementAt( 20 ) != null ? vValores.elementAt( 20 ).toString() : "" );
			imp.say( 48, vValores.elementAt( 21 ) != null ? vValores.elementAt( 21 ).toString() : "" );
			imp.say( 103, vValores.elementAt( 22 ) != null ? vValores.elementAt( 22 ).toString() : "" );
			imp.say( 128, vValores.elementAt( 23 ) != null ? vValores.elementAt( 23 ).toString() : "" );
			imp.pulaLinha( 2, imp.comprimido() );

			vObs = Funcoes.quebraLinha( Funcoes.stringToVector( sMensAdic ), 37 );

			for ( int i = 0; i < vObs.size(); i++ ) {

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 4, vObs.elementAt( i ).toString() );

				if ( i == 0 ) {

					imp.say( 45, "Vendedor: " + ( vValores.elementAt( 25 ) != null ? vValores.elementAt( 25 ).toString() : "" ) );
				}
				if ( i == 1 ) {

					imp.say( 45, vValores.elementAt( 26 ) != null ? vValores.elementAt( 26 ).toString().substring( 0, 20 ) : "" );
				}
			}

			imp.pulaLinha( 5, imp.comprimido() );
			imp.say( 124, sNumNota );
			imp.pulaLinha( 3, imp.comprimido() );

			imp.setPrc( 0, 0 );

		} catch ( Exception e ) {

			e.printStackTrace();
			Funcoes.mensagemErro( null, "Erro ao imprimir totais.\n" + e.getMessage() );
		} finally {

			vObs = null;
		}
	}

}
