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

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.business.component.NF;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.component.Layout;
import org.freedom.library.functions.Funcoes;

public class NF016 extends Layout {

	public boolean imprimir( NF nf, ImprimeOS imp ) {

		boolean bRetorno = super.imprimir( nf, imp );

		final int iLinMaxItens = 46;
		int iNumNota = 0;
		int iItImp = 0;
		int iLinPag = imp.verifLinPag( "NF" );
		boolean bFat = true;
		boolean bNat = true;
		String sNumNota = "";
		String sTipoTran = "";
		String sHora = null;
		String[] sNat = new String[ 2 ];
		String[] sVencs = new String[ 2 ];
		String[] sVals = new String[ 2 ];
		String[] sDuplics = new String[ 2 ];
		Calendar cHora = Calendar.getInstance();

		try {
			imp.limpaPags();

			sHora = StringFunctions.strZero( String.valueOf( cHora.get( Calendar.HOUR_OF_DAY ) ), 2 ) + ":" + StringFunctions.strZero( String.valueOf( cHora.get( Calendar.MINUTE ) ), 2 ) + ":" + StringFunctions.strZero( String.valueOf( cHora.get( Calendar.SECOND ) ), 2 );

			if ( cab.next() )
				iNumNota = cab.getInt( NF.C_DOC );

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
						sDuplics[ i ] = "********";
						sVencs[ i ] = "";
						sVals[ i ] = "";
					}
				}
				else {
					bFat = false;
					sDuplics[ i ] = "********";
					sVencs[ i ] = "";
					sVals[ i ] = "";
				}
			}

			while ( itens.next() ) {
				if ( bNat ) {
					sNat[ 0 ] = itens.getString( NF.C_DESCNAT );
					sNat[ 1 ] = Funcoes.setMascara( itens.getString( NF.C_CODNAT ), "#.##" );
					bNat = false;
				}
				if ( imp.pRow() == 0 ) {
					imp.pulaLinha( 3, imp.comprimido() );
					imp.say( 125, sNumNota );
					imp.pulaLinha( 2, imp.comprimido() );

					if ( nf.getTipoNF() == NF.TPNF_ENTRADA )
						imp.say( 103, "X" );
					else
						imp.say( 88, "X" );

					imp.pulaLinha( 5, imp.comprimido() );
					imp.say( 5, sNat[ 0 ] );
					imp.say( 52, sNat[ 1 ] );
					imp.pulaLinha( 3, imp.comprimido() );
					imp.say( 5, cab.getInt( NF.C_CODEMIT ) + " - " + cab.getString( NF.C_RAZEMIT ) );
					imp.say( 92, !cab.getString( NF.C_CPFEMIT ).equals( "" ) ? Funcoes.setMascara( cab.getString( NF.C_CPFEMIT ), "###.###.###-##" ) : Funcoes.setMascara( cab.getString( NF.C_CNPJEMIT ), "##.###.###/####-##" ) );
					imp.say( 125, Funcoes.dateToStrDate( cab.getDate( NF.C_DTEMITPED ) ) );
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 5, Funcoes.copy( cab.getString( NF.C_ENDEMIT ), 0, 50 ).trim() + ", " + Funcoes.copy( cab.getString( NF.C_NUMEMIT ), 0, 6 ).trim() + " - " + Funcoes.copy( cab.getString( NF.C_COMPLEMIT ), 0, 9 ).trim() );
					imp.say( 80, Funcoes.copy( cab.getString( NF.C_BAIREMIT ), 0, 15 ) );
					imp.say( 104, Funcoes.setMascara( cab.getString( NF.C_CEPEMIT ), "#####-###" ) );
					imp.say( 125, Funcoes.dateToStrDate( cab.getDate( NF.C_DTSAIDA ) ) );
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 5, cab.getString( NF.C_CIDEMIT ) );
					imp.say( 53, ( Funcoes.setMascara( cab.getString( NF.C_DDDEMIT ) + " ", "(####)" ) ) + ( Funcoes.setMascara( cab.getString( NF.C_FONEEMIT ).trim(), "####-####" ) ) );
					imp.say( 75, cab.getString( NF.C_UFEMIT ) );
					imp.say( 92, !cab.getString( NF.C_RGEMIT ).equals( "" ) ? cab.getString( NF.C_RGEMIT ) : cab.getString( NF.C_INSCEMIT ) );
					imp.say( 126, sHora );
					imp.pulaLinha( 3, imp.comprimido() );
				}

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 6, String.valueOf( itens.getInt( NF.C_CODPROD ) ) );
				imp.say( 11, itens.getString( NF.C_DESCPROD ).trim() );
				imp.say( 78, Funcoes.copy( itens.getString( NF.C_ORIGFISC ), 0, 1 ) + Funcoes.copy( itens.getString( NF.C_CODTRATTRIB ), 0, 2 ) );
				imp.say( 86, Funcoes.copy( itens.getString( NF.C_CODUNID ), 4 ) );
				imp.say( 92, String.valueOf( itens.getFloat( NF.C_QTDITPED ) ) );
				imp.say( 105, Funcoes.strDecimalToStrCurrency( 8, 2, String.valueOf( ( ( new BigDecimal( itens.getFloat( NF.C_VLRLIQITPED ) ) ).divide( new BigDecimal( itens.getFloat( NF.C_QTDITPED ) ), 2, BigDecimal.ROUND_HALF_UP ) ) ) ) );
				imp.say( 115, Funcoes.strDecimalToStrCurrency( 13, 2, String.valueOf( itens.getFloat( NF.C_VLRLIQITPED ) ) ) );
				imp.say( 132, Funcoes.strDecimalToStrCurrency( 3, 2, String.valueOf( itens.getFloat( NF.C_PERCICMSITPED ) ) ) );

				iItImp++;

				if ( ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) || ( imp.pRow() >= iLinMaxItens ) ) {

					frete.next();

					if ( imp.pRow() > iLinMaxItens ) {
						Funcoes.mensagemInforma( null, "Número de itens ultrapassa capacidade do formulário!" );
						imp.fechaGravacao();
						return false;
					}
					if ( iItImp == itens.getInt( NF.C_CONTAITENS ) ) {
						int iRow = imp.pRow();
						for ( int i = 0; i < ( iLinMaxItens - 6 - iRow ); i++ )
							imp.pulaLinha( 1, imp.comprimido() );

						for ( int i = 0; i < ( iLinMaxItens - imp.pRow() ); i++ )
							imp.pulaLinha( 1, imp.comprimido() );

						imp.pulaLinha( 2, imp.comprimido() );
						imp.say( 5, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRBASEICMSPED ) ) ) );
						imp.say( 33, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRICMSPED ) ) ) );
						imp.say( 114, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( itens.getFloat( NF.C_VLRPRODITPED ) ) ) );
						imp.pulaLinha( 2, imp.comprimido() );
						imp.say( 5, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( frete.getFloat( NF.C_VLRFRETEPED ) ) ) );
						imp.say( 62, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( itens.getFloat( NF.C_VLRADICITPED ) ) ) );
						imp.say( 88, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( itens.getFloat( NF.C_VLRIPIPED ) ) ) );
						imp.say( 114, Funcoes.strDecimalToStrCurrency( 20, 2, String.valueOf( cab.getFloat( NF.C_VLRLIQPED ) ) ) );
						iItImp = 0;
					}
					else if ( imp.pRow() == iLinMaxItens ) {
						imp.pulaLinha( 3, imp.comprimido() );
						imp.say( 6, "***************" );
						imp.say( 36, "***************" );
						imp.say( 113, "***************" );
						imp.pulaLinha( 2, imp.comprimido() );
						imp.say( 6, "***************" );
						imp.say( 65, "***************" );
						imp.say( 90, "***************" );
						imp.say( 113, "***************" );
					}
					imp.pulaLinha( 3, imp.comprimido() );
					imp.say( 6, frete.getString( NF.C_RAZTRANSP ) );
					imp.say( 72, frete.getString( NF.C_TIPOFRETE ).equals( "C" ) ? "1" : "2" );
					imp.say( 77, frete.getString( NF.C_PLACAFRETE ) );
					imp.say( 93, frete.getString( NF.C_UFFRETE ) );

					sTipoTran = frete.getString( NF.C_TIPOTRANSP );

					if ( sTipoTran.equals( "C" ) )
						imp.say( 116, Funcoes.setMascara( cab.getString( NF.C_CNPJEMIT ), "##.###.###/####-##" ) );
					else
						imp.say( 116, Funcoes.setMascara( frete.getString( NF.C_CNPJTRANSP ), "##.###.###/####-##" ) );

					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 6, Funcoes.copy( frete.getString( NF.C_ENDTRANSP ), 0, 42 ) + "   " + Funcoes.copy( String.valueOf( frete.getInt( NF.C_NUMTRANSP ) ), 0, 6 ) );
					imp.say( 77, Funcoes.copy( frete.getString( NF.C_CIDTRANSP ), 0, 30 ) );
					imp.say( 108, frete.getString( NF.C_UFTRANSP ) );

					if ( sTipoTran.equals( "C" ) )
						imp.say( 116, cab.getString( NF.C_INSCEMIT ) );
					else
						imp.say( 116, frete.getString( NF.C_INSCTRANSP ) );

					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 6, frete.getString( NF.C_QTDFRETE ) );
					imp.say( 26, frete.getString( NF.C_ESPFRETE ) );
					imp.say( 55, frete.getString( NF.C_MARCAFRETE ) );
					imp.say( 106, Funcoes.strDecimalToStrCurrency( 5, casasDec, String.valueOf( frete.getString( NF.C_PESOBRUTO ) ) ) );
					imp.say( 129, Funcoes.strDecimalToStrCurrency( 5, casasDec, String.valueOf( frete.getString( NF.C_PESOLIQ ) ) ) );
					imp.pulaLinha( 13, imp.comprimido() );
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
			err.printStackTrace();
			Funcoes.mensagemErro( null, "Erro ao consultar tabela de Venda!" + err.getMessage() );
			bRetorno = false;
		} finally {
			sNumNota = null;
			sTipoTran = null;
			sHora = null;
			sNat = null;
			sVencs = null;
			sVals = null;
			sDuplics = null;
			cHora = null;
			System.gc();
		}
		return bRetorno;
	}
}
