/**
 * @version 05/03/2010 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 *         Pacote: leiautes <BR>
 *         Classe: @(#)NFMaviGesso.java <BR>
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
 *         Layout da nota fiscal para a empresa 12 Filial 2
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

public class NF12_02 extends Leiaute {

	private String sMensAdic = "";

	private String sNumNota = "";

	public boolean imprimir( ResultSet rs, ResultSet rsRec, ImprimeOS imp ) {

		Calendar cHora = Calendar.getInstance();
		boolean bRetorno;
		int iNumNota = 0;
		int iProd = 0;
		String sTipoTran = "";
		boolean bFat = true;
		boolean bTotalizou = false;
		Vector<String> vValores = new Vector<String>();
		String[] sNat = new String[ 4 ];
		String[] sVencs = new String[ 6 ];
		String[] sVals = new String[ 6 ];
		String[] sDuplics = new String[ 6 ];
		// String[] sMatObs = null;

		String sHora = StringFunctions.strZero( "" + cHora.get( Calendar.HOUR_OF_DAY ), 2 ) + ":" + StringFunctions.strZero( "" + cHora.get( Calendar.MINUTE ), 2 ) + ":" + StringFunctions.strZero( "" + cHora.get( Calendar.SECOND ), 2 );
		try {
			imp.limpaPags();
			boolean bNat = true;

			while ( rs.next() ) {
				iNumNota = rs.getInt( "DocVenda" );
				if ( iNumNota == 0 ) {
					sNumNota = "000000";
				}
				else {
					sNumNota = StringFunctions.strZero( "" + iNumNota, 6 );
				}
				for ( int i = 0; i < 6; i++ ) {
					if ( bFat ) {
						if ( rsRec.next() ) {
							sDuplics[ i ] = sNumNota + "/" + rsRec.getInt( "NPARCITREC" );
							if ( ( rsRec.getDate( "DtVencItRec" ) != null ) && ( rsRec.getDate( "DtVencItRec" ).equals( rs.getDate( "DtEmitVenda" ) ) ) ) {
								sVencs[ i ] = "A VISTA";
							}
							else {
								sVencs[ i ] = StringFunctions.sqlDateToStrDate( rsRec.getDate( "DtVencItRec" ) );
							}
							sVals[ i ] = Funcoes.strDecimalToStrCurrency( 12, 2, rsRec.getString( "VlrParcItRec" ) );
						}
						else {
							bFat = false;
							sDuplics[ i ] = " ";
							sVencs[ i ] = " ";
							sVals[ i ] = " ";
						}
					}

				}

				if ( bNat ) {
					sNat[ 0 ] = Funcoes.adicionaEspacos( rs.getString( "DescNat" ), 25 );
					sNat[ 1 ] = Funcoes.setMascara( rs.getString( "CodNat" ), "#.###" );
					// sMatObs = Funcoes.strToStrArray(rs.getString("ObsVenda") != null ? rs.getString("ObsVenda") : "",3);
					bNat = false;
				}
				if ( imp.pRow() == 0 ) {

					imp.say( imp.pRow() + 2, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 1, 87, "X" );

					imp.say( imp.pRow() + 4, 0, "" + imp.comprimido() );

					imp.say( imp.pRow() + 2, 3, sNat[ 0 ] );
					imp.say( imp.pRow() + 0, 41, sNat[ 1 ] );

					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 6, rs.getInt( "CodCli" ) + " - " + rs.getString( "RazCli" ) );
					imp.say( imp.pRow() + 0, 81, rs.getString( "CpfCli" ) != null ? Funcoes.setMascara( rs.getString( "CpfCli" ), "###.###.###-##" ) : Funcoes.setMascara( rs.getString( "CnpjCli" ), "##.###.###/####-##" ) );
					imp.say( imp.pRow() + 0, 122, StringFunctions.sqlDateToStrDate( rs.getDate( "DtEmitVenda" ) ) );

					imp.say( imp.pRow() + 2, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 6, Funcoes.copy( rs.getString( "EndCli" ), 0, 50 ).trim() + ", " + ( rs.getString( "NumCli" ) != null ? Funcoes.copy( rs.getString( "NumCli" ), 0, 6 ).trim() : "" ).trim() + " - "
							+ ( rs.getString( "ComplCli" ) != null ? Funcoes.copy( rs.getString( "ComplCli" ), 0, 9 ).trim() : "" ).trim() );
					imp.say( imp.pRow() + 0, 68, rs.getString( "BairCli" ) != null ? Funcoes.copy( rs.getString( "BairCli" ), 0, 15 ) : "" );
					imp.say( imp.pRow() + 0, 90, Funcoes.setMascara( rs.getString( "CepCli" ), "#####-###" ) );
					imp.say( imp.pRow() + 0, 122, StringFunctions.sqlDateToStrDate( rs.getDate( "DtSaidaVenda" ) ) );

					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 10, rs.getString( "CidCli" ) );
					String sFone = ( rs.getString( "DDDCli" ) != null ? "(" + rs.getString( "DDDCli" ).trim() + ")" : "" ) + Funcoes.setMascara( rs.getString( "FoneCli" ) != null ? Funcoes.copy( rs.getString( "FoneCli" ), 8 ) : "", "####-####" );
					imp.say( imp.pRow() + 0, 60, sFone );

					imp.say( imp.pRow() + 0, 81, rs.getString( "UfCli" ) );
					imp.say( imp.pRow() + 0, 102, rs.getString( "RgCli" ) != null ? rs.getString( "RgCli" ) : rs.getString( "InscCli" ) );
					imp.say( imp.pRow() + 0, 122, sHora );

					imp.say( imp.pRow() + 3, 0, "" + imp.comprimido() );

					imp.say( imp.pRow() + 0, 6, sDuplics[ 0 ] );
					imp.say( imp.pRow() + 0, 60, sVals[ 0 ] );
					imp.say( imp.pRow() + 0, 110, sVencs[ 0 ] );

					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );

					imp.say( imp.pRow() + 0, 6, sDuplics[ 1 ] );
					imp.say( imp.pRow() + 0, 60, sVals[ 1 ] );
					imp.say( imp.pRow() + 0, 110, sVencs[ 1 ] );

					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );

					imp.say( imp.pRow() + 0, 6, sDuplics[ 2 ] );
					imp.say( imp.pRow() + 0, 60, sVals[ 2 ] );
					imp.say( imp.pRow() + 0, 110, sVencs[ 2 ] );

					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );

					imp.say( imp.pRow() + 0, 6, sDuplics[ 3 ] );
					imp.say( imp.pRow() + 0, 60, sVals[ 3 ] );
					imp.say( imp.pRow() + 0, 110, sVencs[ 3 ] );

					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );

					imp.say( imp.pRow() + 0, 6, sDuplics[ 4 ] );
					imp.say( imp.pRow() + 0, 60, sVals[ 4 ] );
					imp.say( imp.pRow() + 0, 110, sVencs[ 4 ] );

					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );

					imp.say( imp.pRow() + 0, 6, sDuplics[ 5 ] );
					imp.say( imp.pRow() + 0, 60, sVals[ 5 ] );
					imp.say( imp.pRow() + 0, 129, sVencs[ 5 ] );

					imp.say( imp.pRow() + 2, 0, "" );
				}

				if ( !rs.getString( "TipoProd" ).equals( "S" ) ) {

					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 3, rs.getString( "CodProd" ) );

					Vector<?> vDesc = Funcoes.strToVectorSilabas( rs.getString( "ObsItVenda" ) == null || rs.getString( "ObsItVenda" ).equals( "" ) ? ( rs.getString( "DescProd" ).trim() ) : rs.getString( "ObsItVenda" ), 46 );
					String sDesc = "";

					for ( int iConta = 0; ( ( iConta < 20 ) && ( vDesc.size() > iConta ) ); iConta++ ) {

						if ( !vDesc.elementAt( iConta ).toString().equals( "" ) ) {
							sDesc = vDesc.elementAt( iConta ).toString();
						}
						else {
							sDesc = "";
						}
						if ( iConta > 0 ) {
							imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						}

						imp.say( imp.pRow() + 0, 12, sDesc );

						sMensAdic = rs.getString( 5 ) != null ? rs.getString( 5 ).trim() : "";

					}

					iProd = iProd + vDesc.size();

					imp.say( imp.pRow() + 0, 83, Funcoes.copy( rs.getString( "CodUnid" ), 4 ) );
					imp.say( imp.pRow() + 0, 93, "" + rs.getDouble( "QtdItVenda" ) );
					imp.say( imp.pRow() + 0, 102, Funcoes.strDecimalToStrCurrency( 8, 2, "" + ( ( new BigDecimal( rs.getDouble( "VlrLiqItVenda" ) ) ).divide( new BigDecimal( rs.getDouble( "QtdItVenda" ) ), 2, BigDecimal.ROUND_HALF_UP ) ) ) );
					imp.say( imp.pRow() + 0, 114, Funcoes.strDecimalToStrCurrency( 13, 2, rs.getString( "VlrLiqItVenda" ) ) );
					imp.say( imp.pRow() + 0, 131, "" + rs.getDouble( "PercICMSItVenda" ) );

				}

				if ( !bTotalizou ) {

					vValores.addElement( rs.getString( "VlrBaseICMSVenda" ) == null ? "0" : rs.getString( "VlrBaseICMSVenda" ) ); // 0
					vValores.addElement( rs.getString( "VlrICMSVenda" ) == null ? "0" : rs.getString( "VlrICMSVenda" ) ); // 1
					vValores.addElement( "" + rs.getBigDecimal( "VlrLiqVenda" ).subtract( rs.getBigDecimal( "VlrFreteVenda" ) ).subtract( rs.getBigDecimal( "VlrAdicVenda" ) ).subtract( rs.getBigDecimal( "VlrIPIVenda" ) ) ); // 2

					vValores.addElement( rs.getString( "VlrFreteVenda" ) );// 3
					vValores.addElement( rs.getString( "VlrAdicVenda" ) );// 4
					vValores.addElement( rs.getString( "VlrIPIVenda" ) );// 5
					vValores.addElement( rs.getString( "VlrLiqVenda" ) );// 6
					vValores.addElement( rs.getString( "RazTran" ) );// 7
					vValores.addElement( rs.getString( "TipoFreteVD" ) );// 8
					vValores.addElement( rs.getString( "PlacaFreteVD" ) );// 9
					vValores.addElement( rs.getString( "UfFreteVD" ) ); // 10
					sTipoTran = rs.getString( "TipoTran" );
					vValores.addElement( sTipoTran );// 11
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
			if ( imp.pRow() < 24 ) {
				imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
				for ( int i = 0; i < 3; i++ ) {
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					// imp.say(imp.pRow()+0,23,sMatObs[i]);
				}
			}

			impTotais( imp, vValores );
			imp.fechaGravacao();
			bRetorno = true;
			if ( iProd > 23 ) {
				Funcoes.mensagemInforma( null, "Podem haver erros na impressão da nota fiscal." + "\n" + "Produtos ultrapassam 23 linhas! \n" + iProd + " linhas." );
			}
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( null, "Erro ao consultar tabela de Venda!" + err.getMessage() );
			bRetorno = false;
		}
		return bRetorno;
	}

	private void impTotais( ImprimeOS imp, Vector<String> vValores ) {

		try {

			for ( int i = 0; ( imp.pRow() < 46 ); i++ ) {
				imp.say( imp.pRow() + 1, 0, "" );
			}

			imp.say( imp.pRow() + 0, 4, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 0 ).toString() ) ); // BASE ICM
			imp.say( imp.pRow() + 0, 30, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 1 ).toString() ) ); // VLR ICM
			imp.say( imp.pRow() + 0, 115, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 2 ).toString() ) ); // VLR TOT PROD
			imp.say( imp.pRow() + 2, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 0, 4, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 3 ).toString() ) );// VLR FRETE
			imp.say( imp.pRow() + 0, 58, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 4 ).toString() ) );// VLR ADIC
			imp.say( imp.pRow() + 0, 78, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 5 ).toString() ) );// VLR IPI
			imp.say( imp.pRow() + 0, 115, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 6 ).toString() ) );// VLR LIQ VD
			imp.say( imp.pRow() + 3, 0, "" + imp.comprimido() );

			imp.say( imp.pRow() + 0, 8, vValores.elementAt( 7 ).toString() ); // RAZ TRANSP
			imp.say( imp.pRow() + 0, 80, vValores.elementAt( 8 ).toString().equals( "C" ) ? "1" : "2" ); // TIPO FRETE
			imp.say( imp.pRow() + 0, 87, vValores.elementAt( 9 ).toString() ); // PLACA FRETE
			imp.say( imp.pRow() + 0, 103, vValores.elementAt( 10 ).toString() ); // UF FRETE

			String sTipoTran = vValores.elementAt( 11 ).toString();
			if ( sTipoTran == null )
				sTipoTran = "T";

			if ( sTipoTran.equals( "C" ) ) {
				imp.say( imp.pRow() + 0, 118, "" ); // TIPO DE TRANSP

			}
			else {
				imp.say( imp.pRow() + 0, 118, Funcoes.setMascara( vValores.elementAt( 13 ).toString() != null ? vValores.elementAt( 13 ).toString() : "", "##.###.###/####-##" ) );
			}
			imp.say( imp.pRow() + 2, 0, "" + imp.comprimido() );

			imp.say( imp.pRow() + 0, 8, vValores.elementAt( 14 ).toString().trim() ); // END TRANSP
			imp.say( imp.pRow() + 0, 68, vValores.elementAt( 16 ).toString().trim() ); // CID TRANSP
			imp.say( imp.pRow() + 0, 102, vValores.elementAt( 17 ).toString().trim() );// UF TRANSP
			imp.say( imp.pRow() + 0, 121, vValores.elementAt( 18 ).toString() ); // INSC TRANSP

			imp.say( imp.pRow() + 2, 0, "" + imp.comprimido() );

			imp.say( imp.pRow() + 0, 8, vValores.elementAt( 19 ).toString() ); // QTD FRETE
			imp.say( imp.pRow() + 0, 27, vValores.elementAt( 20 ).toString() );// ESP FRETE
			imp.say( imp.pRow() + 0, 52, vValores.elementAt( 21 ).toString() );// MARCA FRETE
			imp.say( imp.pRow() + 0, 103, vValores.elementAt( 22 ).toString() );// PESO BRUTO
			imp.say( imp.pRow() + 0, 128, vValores.elementAt( 23 ).toString() );// PESO LIQ

			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );

			String[] sMatObs = Funcoes.strToStrArray( sMensAdic, 5 );
			imp.say( imp.pRow() + 1, 0, "" );
			imp.say( imp.pRow() + 0, 6, sMatObs[ 0 ] );
			// imp.say(imp.pRow()+0,43,"Vendedor: "+vValores.elementAt(25).toString()); //COD VEND
			imp.say( imp.pRow() + 1, 0, "" );
			imp.say( imp.pRow() + 0, 6, sMatObs[ 1 ] );
			imp.say( imp.pRow() + 0, 30, vValores.elementAt( 26 ).toString().substring( 0, 20 ) );
			imp.say( imp.pRow() + 0, 60, sNumNota );
			imp.say( imp.pRow() + 1, 0, "" );
			imp.say( imp.pRow() + 0, 6, sMatObs[ 2 ] );
			imp.say( imp.pRow() + 1, 0, "" );
			imp.say( imp.pRow() + 0, 6, sMatObs[ 3 ] );
			imp.say( imp.pRow() + 1, 0, "" );
			imp.say( imp.pRow() + 0, 6, sMatObs[ 4 ] );

			imp.say( imp.pRow() + 4, 0, "" + imp.comprimido() );
			// imp.say(imp.pRow()+0,125,sNumNota);
			imp.say( imp.pRow() + 3, 0, "" + imp.comprimido() );

			imp.setPrc( 0, 0 );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
}
