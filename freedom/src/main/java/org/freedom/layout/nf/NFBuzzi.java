/**
 * @version 10/12/2003 <BR>
 * @author Setpoint Informática Ltda./Marco Antonio Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 *         Pacote: leiautes <BR>
 *         Classe: @(#)NFBuzzi.java <BR>
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
 *         Layout da nota fiscal para a empresa Iswara Ltda.
 */
package org.freedom.layout.nf;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Vector;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.component.Leiaute;
import org.freedom.library.functions.Funcoes;

public class NFBuzzi extends Leiaute {

	private String sMensAdic = "";

	private String sNumNota = "";

	public boolean imprimir( ResultSet rs, ResultSet rsRec, ImprimeOS imp ) {

		Calendar cHora = Calendar.getInstance();
		boolean bRetorno;
		int iNumNota = 0;
		int iProd = 0;
		boolean bFat = true;
		boolean bNat = true;
		boolean bTotalizou = false;
		boolean bjatem = false;
		String sTipoTran = "";
		String sCodfisc = "";
		String sSigla = "";
		String sDesc = null;
		String sHora = null;
		String[] sNat = new String[ 4 ];
		String[] sVencs = new String[ 6 ];
		String[] sVals = new String[ 6 ];
		String[] sDuplics = new String[ 6 ];
		String[] sMatObs = null;
		Vector<String> vValores = new Vector<String>();
		Vector<?> vDesc = null;
		Vector<String> vClfisc = new Vector<String>();
		Vector<String> vSigla = new Vector<String>();

		try {

			imp.limpaPags();

			sHora = StringFunctions.strZero( String.valueOf( cHora.get( Calendar.HOUR_OF_DAY ) ), 2 ) + ":" + StringFunctions.strZero( String.valueOf( cHora.get( Calendar.MINUTE ) ), 2 ) + ":" + StringFunctions.strZero( String.valueOf( cHora.get( Calendar.SECOND ) ), 2 );

			vClfisc.addElement( "" );

			while ( rs.next() ) {

				iNumNota = rs.getInt( "DocVenda" );

				if ( iNumNota == 0 )
					sNumNota = "000000";
				else
					sNumNota = StringFunctions.strZero( String.valueOf( iNumNota ), 6 );

				for ( int i = 0; i < 6; i++ ) {
					if ( bFat ) {
						if ( rsRec.next() ) {
							sDuplics[ i ] = sNumNota + "/" + rsRec.getInt( "NPARCITREC" );
							sVencs[ i ] = StringFunctions.sqlDateToStrDate( rsRec.getDate( "DtVencItRec" ) );
							sVals[ i ] = Funcoes.strDecimalToStrCurrency( 12, 2, rsRec.getString( "VlrParcItRec" ) );
						}
						else {
							bFat = false;
							sDuplics[ i ] = "********";
							sVencs[ i ] = "********";
							sVals[ i ] = "********";
						}
					}
					else {
						bFat = false;
						sDuplics[ i ] = "********";
						sVencs[ i ] = "********";
						sVals[ i ] = "********";
					}
				}

				if ( bNat ) {
					sNat[ 0 ] = rs.getString( "DescNat" );
					sNat[ 1 ] = Funcoes.setMascara( rs.getString( "CodNat" ), "#.##" );
					sMatObs = Funcoes.strToStrArray( rs.getString( "ObsVenda" ) != null ? rs.getString( "ObsVenda" ) : "", 3 );
					bNat = false;
				}

				if ( imp.pRow() == 0 ) {
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 90, "X" );
					imp.say( 124, sNumNota );
					imp.pulaLinha( 5, imp.comprimido() );
					imp.say( 6, sNat[ 0 ] );
					imp.say( 46, sNat[ 1 ] );
					imp.pulaLinha( 3, imp.comprimido() );
					imp.say( 6, rs.getInt( "CodCli" ) + " - " + rs.getString( "RazCli" ) );
					imp.say( 90, rs.getString( "CpfCli" ) != null ? Funcoes.setMascara( rs.getString( "CpfCli" ), "###.###.###-##" ) : Funcoes.setMascara( rs.getString( "CnpjCli" ), "##.###.###/####-##" ) );
					imp.say( 125, StringFunctions.sqlDateToStrDate( rs.getDate( "DtEmitVenda" ) ) );
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 6, Funcoes.copy( rs.getString( "EndCli" ), 0, 50 ).trim() + ", " + ( rs.getString( "NumCli" ) != null ? Funcoes.copy( rs.getString( "NumCli" ), 0, 6 ).trim() : "" ).trim() + " - "
							+ ( rs.getString( "ComplCli" ) != null ? Funcoes.copy( rs.getString( "ComplCli" ), 0, 9 ).trim() : "" ).trim() );
					imp.say( 76, rs.getString( "BairCli" ) != null ? Funcoes.copy( rs.getString( "BairCli" ), 0, 15 ) : "" );
					imp.say( 106, Funcoes.setMascara( rs.getString( "CepCli" ), "#####-###" ) );
					imp.say( 125, StringFunctions.sqlDateToStrDate( rs.getDate( "DtSaidaVenda" ) ) );
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 6, rs.getString( "CidCli" ) );
					imp.say( 50, ( rs.getString( "DDDCli" ) != null ? "(" + rs.getString( "DDDCli" ) + ")" : "" ) + ( rs.getString( "FoneCli" ) != null ? Funcoes.setMascara( rs.getString( "FoneCli" ).trim(), "####-####" ) : "" ) );
					imp.say( 83, rs.getString( "UfCli" ) );
					imp.say( 90, rs.getString( "RgCli" ) != null ? rs.getString( "RgCli" ) : rs.getString( "InscCli" ) );
					imp.say( 125, sHora );
					imp.pulaLinha( 5, imp.comprimido() );
					imp.say( 8, sVals[ 0 ] );
					imp.say( 36, sVencs[ 0 ] );
					imp.say( 55, sVals[ 1 ] );
					imp.say( 83, sVencs[ 1 ] );
					imp.say( 102, sVals[ 2 ] );
					imp.say( 130, sVencs[ 2 ] );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 8, sVals[ 3 ] );
					imp.say( 36, sVencs[ 3 ] );
					imp.say( 55, sVals[ 4 ] );
					imp.say( 83, sVencs[ 4 ] );
					imp.say( 102, sVals[ 5 ] );
					imp.say( 130, sVencs[ 5 ] );
					imp.pulaLinha( 3, imp.comprimido() );

				}

				if ( !rs.getString( "TipoProd" ).equals( "S" ) ) {

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 8, rs.getString( "CodProd" ) );

					vDesc = Funcoes.strToVectorSilabas( ( rs.getString( "ObsItVenda" ) == null || rs.getString( "ObsItVenda" ).equals( "" ) ) ? rs.getString( "DescProd" ).trim() : rs.getString( "ObsItVenda" ), 46 );

					for ( int i = 0; ( ( i < 20 ) && ( vDesc.size() > i ) ); i++ ) {
						if ( !vDesc.elementAt( i ).toString().equals( "" ) )
							sDesc = vDesc.elementAt( i ).toString();
						else
							sDesc = "";

						if ( i > 0 )
							imp.pulaLinha( 1, imp.comprimido() );

						imp.say( 16, sDesc );

						iProd = iProd + vDesc.size();

						sMensAdic = rs.getString( 5 ) != null ? rs.getString( 5 ).trim() : "";
					}

					sCodfisc = rs.getString( "CodFisc" ) != null ? rs.getString( "CodFisc" ) : "";

					if ( !sCodfisc.equals( "" ) ) {

						for ( int i = 0; i < vClfisc.size(); i++ ) {

							if ( vClfisc.elementAt( i ) != null ) {
								if ( sCodfisc.equals( vClfisc.elementAt( i ) ) ) {
									bjatem = true;
									sSigla = String.valueOf( (char) ( 64 + i ) );
								}
								else
									bjatem = false;
							}

						}

						if ( !bjatem ) {
							vClfisc.addElement( sCodfisc );
							sSigla = String.valueOf( (char) ( 63 + vClfisc.size() ) );
							vSigla.addElement( sSigla + " = " + sCodfisc );
						}

					}

					imp.say( 69, Funcoes.copy( rs.getString( "OrigFisc" ), 0, 1 ) + Funcoes.copy( rs.getString( "CodTratTrib" ), 0, 2 ) );
					imp.say( 73, sSigla );
					imp.say( 79, rs.getString( "CodUnid" ).substring( 0, 4 ) );
					imp.say( 82, String.valueOf( rs.getDouble( "QtdItVenda" ) ) );
					imp.say( 92, Funcoes.strDecimalToStrCurrency( 8, 2, String.valueOf( ( ( new BigDecimal( rs.getDouble( "VlrLiqItVenda" ) ) ).divide( new BigDecimal( rs.getDouble( "QtdItVenda" ) ), 2, BigDecimal.ROUND_HALF_UP ) ) ) ) );
					imp.say( 106, Funcoes.strDecimalToStrCurrency( 13, 2, rs.getString( "VlrLiqItVenda" ) ) );
					imp.say( 122, String.valueOf( rs.getDouble( "PercICMSItVenda" ) ) );

				}

				if ( !bTotalizou ) {

					vValores.addElement( rs.getString( "VlrBaseICMSVenda" ) ); // 0
					vValores.addElement( rs.getString( "VlrICMSVenda" ) ); // 1
					vValores.addElement( String.valueOf( rs.getBigDecimal( "VlrLiqVenda" ).subtract( rs.getBigDecimal( "VlrFreteVenda" ) ).subtract( rs.getBigDecimal( "VlrAdicVenda" ) ).subtract( rs.getBigDecimal( "VlrIPIVenda" ) ) ) ); // 2
					vValores.addElement( rs.getString( "VlrFreteVenda" ) );// 3
					vValores.addElement( rs.getString( "VlrAdicVenda" ) );// 4
					vValores.addElement( rs.getString( "VlrIPIVenda" ) );// 5
					vValores.addElement( rs.getString( "VlrLiqVenda" ) );// 6
					vValores.addElement( rs.getString( "RazTran" ) );// 7
					vValores.addElement( rs.getString( "TipoFreteVD" ) );// 8
					vValores.addElement( rs.getString( "PlacaFreteVD" ) );// 9
					vValores.addElement( rs.getString( "UfFreteVD" ) ); // 10
					vValores.addElement( sTipoTran = rs.getString( "TipoTran" ) );// 11
					vValores.addElement( rs.getString( "CnpjCli" ) );// 12
					vValores.addElement( rs.getString( "CnpjTran" ) ); // 13
					vValores.addElement( rs.getString( "EndTran" ) != null ? rs.getString( "EndTran" ) : "" );// 14

					if ( sTipoTran.equals( "C" ) ) {

						vValores.addElement( "" );// 15
						vValores.addElement( "" );// 16
						vValores.addElement( "" );// 17
						vValores.addElement( "" ); // 18

					}
					else {

						vValores.addElement( rs.getString( "NumTran" ) != null ? rs.getString( "NumTran" ) : "" );// 15
						vValores.addElement( rs.getString( "CidTran" ) != null ? rs.getString( "CidTran" ) : "" );// 16
						vValores.addElement( rs.getString( "UfTran" ) != null ? rs.getString( "UfTran" ) : "" );// 17
						vValores.addElement( rs.getString( "InscTran" ) != null ? rs.getString( "InscTran" ) : "" ); // 18

					}

					vValores.addElement( rs.getString( "QtdFreteVD" ) );// 19
					vValores.addElement( rs.getString( "EspFreteVD" ) );// 20
					vValores.addElement( rs.getString( "MarcaFreteVD" ) );// 21
					vValores.addElement( rs.getString( "PesoBrutVD" ) );// 22
					vValores.addElement( rs.getString( "PesoLiqVD" ) );// 23
					vValores.addElement( rs.getString( "VlrIssVenda" ) );// 24
					vValores.addElement( rs.getString( "CodVend" ) );// 25

					if ( rs.getString( "NomeVend" ) == null )
						vValores.addElement( StringFunctions.replicate( " ", 25 ) ); // 26
					else
						vValores.addElement( rs.getString( "NomeVend" ) + StringFunctions.replicate( " ", 25 - rs.getString( "NomeVend" ).length() ) );

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

			if ( imp.pRow() < 36 ) {

				imp.pulaLinha( 1, imp.comprimido() );

				for ( int i = 0; i < 3; i++ ) {
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 23, sMatObs[ i ] );
				}

			}

			impTotais( imp, vValores );

			imp.fechaGravacao();

			bRetorno = true;

			if ( iProd > 20 )
				Funcoes.mensagemInforma( null, "Podem haver erros na impressão da nota fiscal." + "\n" + "Produtos ultrapassam vinte linhas!" );

		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( null, "Erro ao consultar tabela de Venda!" + err.getMessage() );
			bRetorno = false;
		}

		return bRetorno;

	}

	private void impTotais( ImprimeOS imp, Vector<String> vValores ) {

		Vector<?> vObs = null;

		try {

			imp.pulaLinha( 45 - imp.pRow(), imp.comprimido() );

			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 6, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 0 ).toString() ) );
			imp.say( 35, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 1 ).toString() ) );
			imp.say( 117, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 2 ).toString() ) );
			imp.pulaLinha( 2, imp.comprimido() );
			imp.say( 6, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 3 ).toString() ) );
			imp.say( 65, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 4 ).toString() ) );
			imp.say( 90, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 5 ).toString() ) );
			imp.say( 117, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 6 ).toString() ) );
			imp.pulaLinha( 3, imp.comprimido() );
			imp.say( 8, vValores.elementAt( 7 ).toString() );
			imp.say( 80, vValores.elementAt( 8 ).toString().equals( "C" ) ? "1" : "2" );
			imp.say( 87, vValores.elementAt( 9 ).toString() );
			imp.say( 103, vValores.elementAt( 10 ).toString() );

			if ( !vValores.elementAt( 11 ).toString().equals( "C" ) )
				imp.say( 118, Funcoes.setMascara( vValores.elementAt( 13 ).toString() != null ? vValores.elementAt( 13 ).toString() : "", "##.###.###/####-##" ) );

			imp.pulaLinha( 2, imp.comprimido() );
			imp.say( 8, vValores.elementAt( 14 ).toString().trim() );
			imp.say( 68, vValores.elementAt( 16 ).toString().trim() );
			imp.say( 102, vValores.elementAt( 17 ).toString().trim() );
			imp.say( 121, vValores.elementAt( 18 ).toString() );
			imp.pulaLinha( 2, imp.comprimido() );
			imp.say( 8, vValores.elementAt( 19 ).toString() );
			imp.say( 25, vValores.elementAt( 20 ).toString() );
			imp.say( 48, vValores.elementAt( 21 ).toString() );
			imp.say( 103, vValores.elementAt( 22 ).toString() );
			imp.say( 128, vValores.elementAt( 23 ).toString() );
			imp.pulaLinha( 2, imp.comprimido() );

			vObs = Funcoes.quebraLinha( Funcoes.stringToVector( sMensAdic ), 37 );

			for ( int i = 0; i < vObs.size(); i++ ) {

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 5, vObs.elementAt( i ).toString() );

				if ( i == 0 )
					imp.say( 43, "Vendedor: " + vValores.elementAt( 25 ).toString() );

				if ( i == 1 )
					imp.say( 43, vValores.elementAt( 26 ).toString().substring( 0, 20 ) );

			}

			imp.pulaLinha( 7, imp.comprimido() );
			imp.say( 128, sNumNota );
			imp.pulaLinha( 3, imp.comprimido() );

			imp.setPrc( 0, 0 );

		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( null, "Erro ao imprimir totais!" + e.getMessage() );
		} finally {
			vObs = null;
		}

	}

}
