/**
 * @version 10/12/2003 <BR>
 * @author Setpoint Informática Ltda./Alex Rodrigues <BR>
 * 
 *         Projeto: Freedom <BR>
 *         Pacote: leiautes <BR>
 *         Classe: @(#)NF018.java <BR>
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
 *         Layout da nota fiscal para a empresa 018 Ltda.
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

public class NF018 extends Layout {

	public boolean imprimir( NF nf, ImprimeOS imp ) {

		boolean bRetorno = super.imprimir( nf, imp );
		boolean bFat = true;
		boolean bNat = true;
		int iNumNota = 0;
		int numMax = 36;
		int iItImp = 0;
		int iLinPag = imp.verifLinPag( "NF" );
		int iContaObs = 0; // Contador para a gamb de asterisco
		int iContaObs2 = 0; // Contador para a gamb de '"'
		Calendar cHora = Calendar.getInstance();
		Vector<?> vMens = null;
		String[] sNat = new String[ 2 ];
		String[] sVencs = new String[ 2 ];
		String[] sVals = new String[ 2 ];
		String[] sMatObs = null;
		String[] sMarcs = { "\"", "\"\"", "\"\"\"", "\"\"\"\"" }; // Tipos de Marcs.
		String[] sMarcs2 = { "*", "**", "***", "****" }; // Tipos de Marcs.
		String sNumNota = null;
		String sTipoTran = null;
		String sMens = "";
		String sTmp = null;
		String sTmp2 = null;
		String sHora;

		try {
			imp.limpaPags();

			sHora = StringFunctions.strZero( String.valueOf( cHora.get( Calendar.HOUR_OF_DAY ) ), 2 ) + ":" + StringFunctions.strZero( String.valueOf( cHora.get( Calendar.MINUTE ) ), 2 ) + ":" + StringFunctions.strZero( String.valueOf( cHora.get( Calendar.SECOND ) ), 2 );

			cab.next();

			iNumNota = cab.getInt( NF.C_DOC );

			if ( iNumNota == 0 )
				sNumNota = "000000";
			else
				sNumNota = StringFunctions.strZero( String.valueOf( iNumNota ), 6 );

			for ( int i = 0; i < 2; i++ ) {
				if ( bFat ) {
					if ( parc.next() ) {
						sVencs[ i ] = Funcoes.dateToStrDate( parc.getDate( NF.C_DTVENCTO ) );
						sVals[ i ] = Funcoes.strDecimalToStrCurrency( 10, 2, parc.getString( NF.C_VLRPARC ) );
					}
					else {
						bFat = false;
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

			while ( itens.next() ) {

				if ( bNat ) {
					sNat[ 0 ] = Funcoes.copy( itens.getString( NF.C_DESCNAT ), 40 );
					sNat[ 1 ] = Funcoes.setMascara( String.valueOf( itens.getInt( NF.C_CODNAT ) ), "#.##" );
					sMatObs = Funcoes.strToStrArray( cab.getString( NF.C_OBSPED ), 3 );
					bNat = false;
				}

				if ( imp.pRow() == 0 ) {
					imp.pulaLinha( 1, imp.comprimido() );

					if ( nf.getTipoNF() == NF.TPNF_ENTRADA )
						imp.say( 105, "X" );
					else
						imp.say( 88, "X" );

					imp.pulaLinha( 6, imp.comprimido() );
					imp.say( 2, sNat[ 0 ] );
					imp.say( 50, sNat[ 1 ] );
					imp.pulaLinha( 3, imp.comprimido() );
					imp.say( 2, cab.getInt( NF.C_CODEMIT ) + " - " + cab.getString( NF.C_RAZEMIT ) );
					imp.say( 92, !cab.getString( NF.C_CPFEMIT ).equals( "" ) ? Funcoes.setMascara( cab.getString( NF.C_CPFEMIT ), "###.###.###-##" ) : Funcoes.setMascara( cab.getString( NF.C_CNPJEMIT ), "##.###.###/####-##" ) );
					imp.say( 126, Funcoes.dateToStrDate( cab.getDate( NF.C_DTEMITPED ) ) );
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 2, Funcoes.copy( cab.getString( NF.C_ENDEMIT ), 0, 50 ).trim() + ", " + ( Funcoes.copy( "" + cab.getInt( NF.C_NUMEMIT ), 0, 6 ).trim() ).trim() + " - "
							+ ( cab.getString( NF.C_COMPLEMIT ) != null ? Funcoes.copy( cab.getString( NF.C_COMPLEMIT ), 0, 9 ).trim() : "" ).trim() );
					imp.say( 72, Funcoes.copy( cab.getString( NF.C_BAIREMIT ), 0, 15 ) );
					imp.say( 105, Funcoes.setMascara( cab.getString( NF.C_CEPEMIT ), "#####-###" ) );
					imp.say( 126, Funcoes.dateToStrDate( cab.getDate( NF.C_DTSAIDA ) ) );
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 2, cab.getString( NF.C_CIDEMIT ) );
					imp.say( 56, ( Funcoes.setMascara( cab.getString( NF.C_DDDEMIT ) + " ", "(####)" ) ) + ( Funcoes.setMascara( cab.getString( NF.C_FONEEMIT ).trim(), "####-####" ) ) );
					imp.say( 83, cab.getString( NF.C_UFEMIT ) );
					imp.say( 92, !cab.getString( NF.C_RGEMIT ).equals( "" ) ? cab.getString( NF.C_RGEMIT ) : cab.getString( NF.C_INSCEMIT ) );
					imp.say( 126, sHora );
					imp.pulaLinha( 3, imp.comprimido() );
					imp.say( 42, sVals[ 0 ] );
					imp.say( 56, sVencs[ 0 ] );
					imp.say( 112, sVals[ 1 ] );
					imp.say( 126, sVencs[ 1 ] );
					imp.pulaLinha( 4, imp.comprimido() );
				}

				sTmp = itens.getString( NF.C_DESCFISC ).trim() + '\n';
				// Gambs para colocar '"':
				if ( sTmp.length() > 0 ) {
					if ( sMens.indexOf( sTmp ) == -1 && iContaObs < 4 )// Esta mensagem ainda não esta na obs então posso adiciona-la.
						sMens += sMarcs[ iContaObs++ ] + " " + sTmp;
				}
				sTmp2 = itens.getString( NF.C_DESCFISC2 ).trim() + '\n';
				// Gambs para colocar '*'
				if ( sTmp2.trim().length() > 0 && !sTmp.equals( sTmp2 ) ) {
					if ( sMens.indexOf( sTmp2 ) == -1 && iContaObs2 < 4 ) // Esta mensagem ainda não esta na obs então posso adiciona-la.
						sMens += sMarcs2[ iContaObs2++ ] + " " + sTmp2;
				}

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 4, itens.getString( NF.C_DESCPROD ) );
				imp.say( 59, Funcoes.copy( itens.getString( NF.C_ORIGFISC ), 0, 1 ) + Funcoes.copy( itens.getString( NF.C_CODTRATTRIB ), 0, 2 ) );
				imp.say( 65, Funcoes.copy( itens.getString( NF.C_CODUNID ), 4 ) );
				imp.say( 71, Funcoes.strDecimalToStrCurrency( 10, 3, String.valueOf( itens.getFloat( NF.C_QTDITPED ) ) ) );
				imp.say( 90, Funcoes.strDecimalToStrCurrency( 8, 2, String.valueOf( ( ( new BigDecimal( itens.getFloat( NF.C_VLRLIQITPED ) ) ).divide( new BigDecimal( itens.getFloat( NF.C_QTDITPED ) ), 2, BigDecimal.ROUND_HALF_UP ) ) ) ) );
				imp.say( 101, Funcoes.strDecimalToStrCurrency( 13, 2, String.valueOf( itens.getFloat( NF.C_VLRLIQITPED ) ) ) );
				imp.say( 116, String.valueOf( itens.getFloat( NF.C_PERCICMSITPED ) ) );
				imp.say( 121, String.valueOf( itens.getFloat( NF.C_PERCIPIITPED ) ) );
				imp.say( 128, Funcoes.strDecimalToStrCurrency( 7, 2, "" + itens.getFloat( NF.C_VLRIPIITPED ) ) );

				iItImp++;
				if ( ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) || ( imp.pRow() == numMax ) ) {

					frete.next();

					if ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) {
						int iRow = imp.pRow();
						if ( !sMatObs[ 0 ].equals( "" ) ) {
							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( 23, sMatObs[ 0 ] );
						}
						if ( !sMatObs[ 1 ].equals( "" ) ) {
							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( 23, sMatObs[ 1 ] );
						}
						if ( !sMatObs[ 2 ].equals( "" ) ) {
							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( 23, sMatObs[ 2 ] );
						}
						for ( int i = 0; i < ( numMax - iRow ); i++ )
							imp.pulaLinha( 1, imp.comprimido() );

						imp.pulaLinha( 3, imp.comprimido() );
						imp.say( 1, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRBASEICMSPED ) ) ) );
						imp.say( 32, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRICMSPED ) ) ) );
						imp.say( 116, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRLIQPED ) ) ) );
						imp.pulaLinha( 2, imp.comprimido() );
						imp.say( 2, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( frete.getFloat( NF.C_VLRFRETEPED ) ) ) );
						imp.say( 60, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( itens.getFloat( NF.C_VLRADICITPED ) ) ) );
						imp.say( 87, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRIPIPED ) ) ) );
						imp.say( 116, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRLIQPED ) ) ) );
						iItImp = 0;
					}
					else if ( imp.pRow() == numMax ) {
						imp.pulaLinha( 3, imp.comprimido() );
						imp.say( 2, "***************" );
						imp.say( 32, "***************" );
						imp.say( 116, "***************" );
						imp.pulaLinha( 2, imp.comprimido() );
						imp.say( 2, "***************" );
						imp.say( 62, "***************" );
						imp.say( 87, "***************" );
						imp.say( 116, "***************" );
					}

					imp.pulaLinha( 3, imp.comprimido() );

					if ( frete.getString( NF.C_TIPOFRETE ) != null ) {
						imp.say( 4, frete.getString( NF.C_RAZTRANSP ) );
						imp.say( 85, frete.getString( NF.C_TIPOFRETE ).equals( "C" ) ? "1" : "2" );
						imp.say( 88, frete.getString( NF.C_PLACAFRETE ) );
						imp.say( 102, frete.getString( NF.C_UFFRETE ) );
					}

					sTipoTran = frete.getString( NF.C_TIPOTRANSP );

					if ( sTipoTran.equals( "C" ) )
						imp.say( 115, Funcoes.setMascara( cab.getString( NF.C_CNPJEMIT ), "##.###.###/####-##" ) );
					else
						imp.say( 115, Funcoes.setMascara( frete.getString( NF.C_CNPJTRANSP ), "##.###.###/####-##" ) );

					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 4, Funcoes.copy( frete.getString( NF.C_ENDTRANSP ), 0, 42 ) + ", " + Funcoes.copy( frete.getString( NF.C_NUMTRANSP ), 0, 6 ) );
					imp.say( 76, frete.getString( NF.C_CIDTRANSP ) );
					imp.say( 110, frete.getString( NF.C_UFTRANSP ) );

					if ( sTipoTran.equals( "C" ) )
						imp.say( 115, cab.getString( NF.C_INSCEMIT ) );
					else
						imp.say( 115, frete.getString( NF.C_INSCTRANSP ) );

					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 4, String.valueOf( frete.getFloat( NF.C_QTDFRETE ) ) );
					imp.say( 27, frete.getString( NF.C_ESPFRETE ) );
					imp.say( 55, frete.getString( NF.C_MARCAFRETE ) );
					imp.say( 102, String.valueOf( frete.getFloat( NF.C_PESOBRUTO ) ) );
					imp.say( 125, String.valueOf( frete.getFloat( NF.C_PESOLIQ ) ) );
					imp.pulaLinha( 2, imp.comprimido() );

					vMens = Funcoes.strToVectorSilabas( sMens, 60 );

					for ( int i = 0; i < vMens.size(); i++ ) {
						if ( vMens.elementAt( i ) != null ) {
							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( 4, (String) vMens.elementAt( i ) );
						}
					}

					imp.pulaLinha( 9, imp.comprimido() );
					imp.say( 125, sNumNota );

					for ( int i = imp.pRow(); i <= iLinPag; i++ )
						imp.pulaLinha( 1, imp.comprimido() );

					imp.setPrc( 0, 0 );
					imp.incPags();
				}
			}

			imp.fechaGravacao();
			bRetorno = true;
		} catch ( Exception err ) {
			Funcoes.mensagemErro( null, "Erro ao montar nf de venda.\n" + err.getMessage() );
			err.printStackTrace();
			bRetorno = false;
		} finally {
			cHora = null;
			vMens = null;
			sNat = null;
			sVencs = null;
			sVals = null;
			sMatObs = null;
			sMarcs = null;
			sMarcs2 = null;
			sNumNota = null;
			sTipoTran = null;
			sMens = null;
			sTmp = null;
			sTmp2 = null;
			sHora = null;
			System.gc();
		}
		return bRetorno;
	}
}
