/**
 * @version 10/12/2003 <BR>
 * @author Setpoint Informática Ltda./Marco Antonio Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 *         Pacote: leiautes <BR>
 *         Classe: @(#)NFIswara.java <BR>
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
 *         Layout da nota fiscal para a empresa ModelCraft
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

public class NF017 extends Layout {

	public boolean imprimir( NF nf, ImprimeOS imp ) {

		boolean bRetorno = super.imprimir( nf, imp );
		final int iLinMax = 42;
		int iNumNota = 0;
		int iItImp = 0;
		int iLinPag = imp.verifLinPag( "NF" );
		int iContaMens = 1;
		boolean bjatem = false;
		boolean bFat = true;
		boolean bNat = true;
		String sCodfisc = "";
		String sSigla = "";
		String sNumNota = "";
		String sTipoTran = "";
		String sObs = "";
		String sDescAdic = "";
		String sDesc = "";
		String sClasFisc = null;
		String sTmp = null;
		String sHora = null;
		String[] sNat = new String[ 2 ];
		String[] sVencs = new String[ 2 ];
		String[] sVals = new String[ 2 ];
		String[] sDuplics = new String[ 2 ];
		Vector<?> vMatObs = new Vector<Object>();
		Vector<String> vClfiscal = new Vector<String>();
		Vector<String> vSigla = new Vector<String>();
		Vector<String[]> vMens = new Vector<String[]>();
		Vector<?> vDesc = null;
		Vector<?> vObs = null;
		Calendar cHora = Calendar.getInstance();

		try {
			imp.limpaPags();

			sHora = StringFunctions.strZero( String.valueOf( cHora.get( Calendar.HOUR_OF_DAY ) ), 2 ) + ":" + StringFunctions.strZero( String.valueOf( cHora.get( Calendar.MINUTE ) ), 2 ) + ":" + StringFunctions.strZero( String.valueOf( cHora.get( Calendar.SECOND ) ), 2 );

			vMens.clear();

			if ( cab.next() ) {
				iNumNota = cab.getInt( NF.C_DOC );
				vMatObs = Funcoes.strToVectorSilabas( cab.getString( NF.C_OBSPED ), 100 );
			}

			if ( iNumNota == 0 )
				sNumNota = "000000";
			else
				sNumNota = StringFunctions.strZero( String.valueOf( iNumNota ), 6 );

			for ( int i = 0; i < 2; i++ ) {
				if ( bFat ) {
					if ( parc.next() ) {
						sDuplics[ i ] = sNumNota + "/" + parc.getInt( NF.C_NPARCITREC );
						sVencs[ i ] = Funcoes.dateToStrDate( parc.getDate( NF.C_DTVENCTO ) );
						sVals[ i ] = Funcoes.strDecimalToStrCurrency( 12, 2, parc.getString( NF.C_VLRPARC ) );
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
					sDuplics[ i ] = "";
					sVencs[ i ] = "";
					sVals[ i ] = "";
				}
			}

			while ( itens.next() ) {

				if ( bNat ) {
					sNat[ 0 ] = itens.getString( NF.C_DESCNAT ).substring( 0, 40 ).trim();
					sNat[ 1 ] = Funcoes.setMascara( itens.getString( NF.C_CODNAT ), "#.##" );
					bNat = false;
				}
				if ( imp.pRow() == 0 ) {
					imp.pulaLinha( 4, imp.comprimido() );

					if ( nf.getTipoNF() == NF.TPNF_ENTRADA )
						imp.say( 106, "X" );
					else
						imp.say( 92, "X" );

					imp.pulaLinha( 4, imp.comprimido() );
					imp.say( 9, sNat[ 0 ] );
					imp.say( 50, sNat[ 1 ] );
					imp.pulaLinha( 3, imp.comprimido() );
					imp.say( 9, cab.getInt( NF.C_CODEMIT ) + " - " + cab.getString( NF.C_RAZEMIT ) );
					imp.say( 92, !cab.getString( NF.C_CPFEMIT ).equals( "" ) ? Funcoes.setMascara( cab.getString( NF.C_CPFEMIT ), "###.###.###-##" ) : Funcoes.setMascara( cab.getString( NF.C_CNPJEMIT ), "##.###.###/####-##" ) );
					imp.say( 127, Funcoes.dateToStrDate( cab.getDate( NF.C_DTEMITPED ) ) );
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 9, Funcoes.copy( cab.getString( NF.C_ENDEMIT ), 0, 50 ).trim() + ", " + ( ! ( cab.getInt( NF.C_NUMEMIT ) == 0 ) ? Funcoes.copy( String.valueOf( cab.getInt( NF.C_NUMEMIT ) ), 0, 6 ).trim() : "" ).trim() + " - "
							+ ( Funcoes.copy( cab.getString( NF.C_COMPLEMIT ), 0, 9 ) ).trim() );
					imp.say( 76, !cab.getString( NF.C_BAIREMIT ).equals( "" ) ? Funcoes.copy( cab.getString( NF.C_BAIREMIT ), 0, 15 ) : "" );
					imp.say( 105, Funcoes.setMascara( cab.getString( NF.C_CEPEMIT ), "#####-###" ) );
					imp.say( 127, Funcoes.dateToStrDate( cab.getDate( NF.C_DTSAIDA ) ) );
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 9, cab.getString( NF.C_CIDEMIT ) );
					imp.say( 56, ( Funcoes.setMascara( cab.getString( NF.C_DDDEMIT ) + " ", "(####)" ) ) + ( Funcoes.setMascara( cab.getString( NF.C_FONEEMIT ).trim(), "####-####" ) ) );
					imp.say( 78, cab.getString( NF.C_UFEMIT ) );
					imp.say( 92, !cab.getString( NF.C_RGEMIT ).equals( "" ) ? cab.getString( NF.C_RGEMIT ) : cab.getString( NF.C_INSCEMIT ) );
					imp.say( 127, sHora );
					imp.pulaLinha( 4, imp.comprimido() );
					imp.say( 25, sDuplics[ 0 ] );
					imp.say( 43, sVals[ 0 ] );
					imp.say( 66, sVencs[ 0 ] );
					imp.say( 84, sDuplics[ 1 ] );
					imp.say( 102, sVals[ 1 ] );
					imp.say( 125, sVencs[ 1 ] );
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 9, cab.getString( NF.C_ENDCOBEMIT ) + " , " + cab.getString( NF.C_NUMCOBEMIT ) + " - " + cab.getString( NF.C_BAIRCOBEMIT ) + " / " + cab.getString( NF.C_CIDCOBEMIT ) + " - " + cab.getString( NF.C_UFCOBEMIT ) );
					imp.pulaLinha( 2, imp.comprimido() );
				}

				// Gambs para colocar o lote:
				if ( ( itens.getDate( NF.C_VENCLOTE ) != null ) && ( !itens.getString( NF.C_CODLOTE ).equals( "" ) ) )
					sDescAdic = "  - " + itens.getString( NF.C_CODLOTE ).trim();

				// Gambs para colocar arteriscos:
				sTmp = itens.getString( NF.C_DESCFISC ).trim();

				if ( sTmp.length() > 0 ) {
					int iLinha;
					for ( iLinha = 0; iLinha < vMens.size(); iLinha++ ) {
						if ( vMens.elementAt( iLinha )[ 1 ].equals( sTmp ) && vMens.elementAt( iLinha )[ 0 ].indexOf( "*" ) == 0 ) {
							sDescAdic += " " + vMens.elementAt( iLinha )[ 0 ];
							break;
						}
					}
					if ( iLinha == vMens.size() ) {
						vMens.add( new String[] { StringFunctions.replicate( "*", iContaMens++ ), sTmp } );
						sDescAdic += " " + vMens.elementAt( iLinha )[ 0 ];
					}
				}

				sTmp = itens.getString( NF.C_DESCFISC2 ).trim();
				sClasFisc = Funcoes.copy( itens.getString( NF.C_ORIGFISC ), 0, 1 ) + Funcoes.copy( itens.getString( NF.C_CODTRATTRIB ), 0, 2 );

				if ( sTmp.length() > 0 ) {
					int iLinha;
					for ( iLinha = 0; iLinha < vMens.size(); iLinha++ ) {
						if ( vMens.elementAt( iLinha )[ 0 ].equals( sClasFisc ) )
							break;
					}
					if ( iLinha == vMens.size() )
						vMens.add( new String[] { sClasFisc, sTmp } );
				}

				sCodfisc = Funcoes.copy( itens.getString( NF.C_CODFISC ).trim(), 8 );
				if ( !sCodfisc.equals( "" ) ) {
					for ( int i = 0; i < vClfiscal.size(); i++ ) {
						if ( vClfiscal.elementAt( i ) != null ) {
							if ( sCodfisc.equals( vClfiscal.elementAt( i ) ) ) {
								bjatem = true;
								sSigla = String.valueOf( (char) ( 65 + i ) );
							}
							else
								bjatem = false;
						}
					}
					if ( !bjatem ) {
						vClfiscal.addElement( sCodfisc );
						sSigla = String.valueOf( (char) ( 64 + vClfiscal.size() ) );
						vSigla.addElement( sSigla + " = " + sCodfisc );
					}
				}

				// imprimindo produtos
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 1, Funcoes.alinhaDir( itens.getInt( NF.C_CODPROD ), 8 ) );

				// Gamb pra escrever mais de uma linha pra descrição do produto
				vDesc = Funcoes.strToVectorSilabas( ( itens.getString( NF.C_OBSITPED ).equals( "" ) ? ( itens.getString( NF.C_DESCPROD ).trim() ) : itens.getString( NF.C_OBSITPED ) ), 50 );
				for ( int i = 0; i < vDesc.size(); i++ ) {
					if ( vDesc.elementAt( i ) != null )
						sDesc = ( (String) vDesc.elementAt( i ) );
					else
						sDesc = "";

					if ( i > 0 )
						imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 11, sDesc );
				}

				imp.say( 66, sSigla );
				imp.say( 70, Funcoes.copy( itens.getString( NF.C_ORIGFISC ), 0, 1 ) + Funcoes.copy( itens.getString( NF.C_CODTRATTRIB ), 0, 2 ) );
				imp.say( 75, Funcoes.copy( itens.getString( NF.C_CODUNID ), 4 ) );
				imp.say( 81, String.valueOf( itens.getFloat( NF.C_QTDITPED ) ) );
				imp.say( 93, Funcoes.strDecimalToStrCurrency( 8, 2, String.valueOf( ( ( new BigDecimal( itens.getFloat( NF.C_VLRLIQITPED ) ) ).divide( new BigDecimal( itens.getFloat( NF.C_QTDITPED ) ), 2, BigDecimal.ROUND_HALF_UP ) ) ) ) );
				imp.say( 104, Funcoes.strDecimalToStrCurrency( 13, 2, String.valueOf( itens.getFloat( NF.C_VLRLIQITPED ) ) ) );
				imp.say( 119, String.valueOf( itens.getFloat( NF.C_PERCICMSITPED ) ) );
				imp.say( 125, String.valueOf( itens.getFloat( NF.C_PERCIPIITPED ) ) );
				imp.say( 128, Funcoes.strDecimalToStrCurrency( 7, 2, "" + itens.getFloat( NF.C_VLRIPIPED ) ) );

				iItImp++;
				if ( ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) || ( imp.pRow() > iLinMax ) ) {

					frete.next();

					if ( ( imp.pRow() + vMatObs.size() ) > iLinMax ) {
						Funcoes.mensagemInforma( null, "Número de linhas ultrapassa capacidade do formulário!" );
						imp.fechaGravacao();
						return false;
					}
					if ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) {

						for ( int i = 0; i < vMatObs.size(); i++ ) {
							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( 14, ( vMatObs.elementAt( i ) != null ? (String) vMatObs.elementAt( i ) : "" ) );
						}

						for ( int i = 0; i < ( iLinMax - imp.pRow() ); i++ )
							imp.pulaLinha( 1, imp.comprimido() );

						iItImp = 0;
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 4, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRBASEICMSPED ) ) ) );
						imp.say( 31, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRICMSPED ) ) ) );
						imp.say( 112, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( itens.getFloat( NF.C_VLRPRODITPED ) ) ) );
						imp.pulaLinha( 2, imp.comprimido() );
						imp.say( 4, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( frete.getFloat( NF.C_VLRFRETEPED ) ) ) );
						imp.say( 61, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( itens.getFloat( NF.C_VLRADICITPED ) ) ) );
						imp.say( 88, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( itens.getFloat( NF.C_VLRIPIPED ) ) ) );
						imp.say( 112, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRLIQPED ) ) ) );
					}

					imp.pulaLinha( 3, imp.comprimido() );
					imp.say( 9, frete.getString( NF.C_RAZTRANSP ) );
					imp.say( 80, frete.getString( NF.C_TIPOFRETE ).equals( "C" ) ? "1" : "2" );
					imp.say( 91, frete.getString( NF.C_PLACAFRETE ) );
					imp.say( 107, frete.getString( NF.C_UFFRETE ) );

					sTipoTran = frete.getString( NF.C_TIPOTRANSP );
					if ( sTipoTran.equals( "C" ) )
						imp.say( 119, Funcoes.setMascara( cab.getString( NF.C_CNPJEMIT ), "##.###.###/####-##" ) );
					else
						imp.say( 119, Funcoes.setMascara( frete.getString( NF.C_CNPJTRANSP ), "##.###.###/####-##" ) );

					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 9, Funcoes.copy( frete.getString( NF.C_ENDTRANSP ), 0, 42 ) + "   " + Funcoes.copy( "" + frete.getInt( NF.C_NUMTRANSP ), 0, 6 ) );
					imp.say( 70, Funcoes.copy( frete.getString( NF.C_CIDTRANSP ), 0, 30 ) );
					imp.say( 107, frete.getString( NF.C_UFTRANSP ) );

					if ( sTipoTran.equals( "C" ) )
						imp.say( 119, cab.getString( NF.C_INSCEMIT ) );
					else
						imp.say( 119, frete.getString( NF.C_INSCTRANSP ) );

					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 9, frete.getString( NF.C_QTDFRETE ) );
					imp.say( 30, frete.getString( NF.C_ESPFRETE ) );
					imp.say( 50, frete.getString( NF.C_MARCAFRETE ) );
					imp.say( 104, Funcoes.strDecimalToStrCurrency( 5, casasDec, String.valueOf( frete.getString( NF.C_PESOBRUTO ) ) ) );
					imp.say( 124, Funcoes.strDecimalToStrCurrency( 5, casasDec, String.valueOf( frete.getString( NF.C_PESOLIQ ) ) ) );
					imp.pulaLinha( 2, imp.comprimido() );

					for ( int i = 0; i < vSigla.size(); i++ ) {
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 9, vSigla.elementAt( i ) );
					}

					for ( int i = 0; i < vMens.size(); i++ )
						sObs += vMens.elementAt( i )[ 0 ] + " - " + vMens.elementAt( i )[ 1 ] + "\n";

					vObs = Funcoes.strToVectorSilabas( sObs, 80 );

					for ( int i = 0; ( ( i < 20 ) && ( vObs.size() > i ) ); i++ ) {
						if ( vObs.elementAt( i ) != null )
							sObs = ( (String) vObs.elementAt( i ) );
						else
							sObs = "";

						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 9, sObs );
					}

					for ( int i = imp.pRow(); i <= iLinPag; i++ )
						imp.pulaLinha( 1, imp.comprimido() );
				}
			}
			imp.fechaGravacao();
			bRetorno = true;
		} catch ( Exception err ) {
			Funcoes.mensagemErro( null, "Erro ao consultar tabela de Venda!" + err.getMessage() );
			err.printStackTrace();
			bRetorno = false;
		} finally {
			sCodfisc = null;
			sSigla = null;
			sNumNota = null;
			sTipoTran = null;
			sObs = null;
			sDescAdic = null;
			sDesc = null;
			sClasFisc = null;
			sTmp = null;
			sHora = null;
			sNat = null;
			sVencs = null;
			sVals = null;
			sDuplics = null;
			vMatObs = null;
			vClfiscal = null;
			vSigla = null;
			vMens = null;
			vDesc = null;
			vObs = null;
			cHora = null;
			System.gc();
		}
		return bRetorno;
	}
}
