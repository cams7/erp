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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.component.Leiaute;
import org.freedom.library.functions.Funcoes;

public class NFModelCraft extends Leiaute {

	private Vector<String[]> vClfiscal = null;

	public boolean imprimir( ResultSet rs, ResultSet rsRec, ImprimeOS imp ) {

		Calendar cHora = Calendar.getInstance();
		int iSigla = 0;
		boolean bRetorno;
		boolean bAchouCF = false;
		String sCodfisc = "";
		String sSigla = "";
		int iNumNota = 0;
		String sNumNota = "";
		int iItImp = 0;
		int iLinPag = imp.verifLinPag( "NF" );
		final int iLinMaxItens = 46;
		String sTipoTran = "";
		boolean bFat = true;
		String[] sNat = new String[ 2 ];
		String[] sVencs = new String[ 4 ];
		String[] sVals = new String[ 4 ];
		String[] sDuplics = new String[ 4 ];
		String[] sMatObs = null;
		String sHora = StringFunctions.strZero( "" + cHora.get( Calendar.HOUR_OF_DAY ), 2 ) + ":" + StringFunctions.strZero( "" + cHora.get( Calendar.MINUTE ), 2 ) + ":" + StringFunctions.strZero( "" + cHora.get( Calendar.SECOND ), 2 );
		vClfiscal = new Vector<String[]>();
		String sItemCF[] = new String[ 2 ];
		sItemCF[ 0 ] = "A";
		sItemCF[ 1 ] = "4419009900";
		vClfiscal.addElement( sItemCF );

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

				for ( int i = 0; i < 3; i++ ) {
					if ( bFat ) {
						if ( rsRec.next() ) {
							sDuplics[ i ] = sNumNota + "/" + rsRec.getInt( "NPARCITREC" );
							sVencs[ i ] = StringFunctions.sqlDateToStrDate( rsRec.getDate( "DtVencItRec" ) );
							sVals[ i ] = Funcoes.strDecimalToStrCurrency( 12, 2, rsRec.getString( "VlrParcItRec" ) );
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

				if ( bNat ) {
					sNat[ 0 ] = rs.getString( "DescNat" );
					sNat[ 1 ] = Funcoes.setMascara( rs.getString( "CodNat" ), "#.##" );
					sMatObs = Funcoes.strToStrArray( rs.getString( "ObsVenda" ) != null ? rs.getString( "ObsVenda" ) : "", 3 );

					// System.out.println("num nota: "+iNumNota);
					// System.out.println("rs 2"+rs.getInt(2));
					bNat = false;
				}
				if ( imp.pRow() == 0 ) {
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 91, "X" );
					// imp.say(imp.pRow()+0,130,sNumNota);
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					// imp.say(imp.pRow()+1,0,"");
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					// imp.say(imp.pRow()+2,0,""+imp.comprimido());
					imp.say( imp.pRow() + 0, 6, sNat[ 0 ] );
					imp.say( imp.pRow() + 0, 45, sNat[ 1 ] );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 9, rs.getInt( "CodCli" ) + " - " + rs.getString( "RazCli" ) );
					imp.say( imp.pRow() + 0, 89, rs.getString( "CpfCli" ) != null ? Funcoes.setMascara( rs.getString( "CpfCli" ), "###.###.###-##" ) : Funcoes.setMascara( rs.getString( "CnpjCli" ), "##.###.###/####-##" ) );
					imp.say( imp.pRow() + 0, 125, StringFunctions.sqlDateToStrDate( rs.getDate( "DtEmitVenda" ) ) );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 9, Funcoes.copy( rs.getString( "EndCli" ), 0, 50 ).trim() + ", " + ( rs.getString( "NumCli" ) != null ? Funcoes.copy( rs.getString( "NumCli" ), 0, 6 ).trim() : "" ).trim() + " - "
							+ ( rs.getString( "ComplCli" ) != null ? Funcoes.copy( rs.getString( "ComplCli" ), 0, 9 ).trim() : "" ).trim() );
					imp.say( imp.pRow() + 0, 72, rs.getString( "BairCli" ) != null ? Funcoes.copy( rs.getString( "BairCli" ), 0, 15 ) : "" );
					imp.say( imp.pRow() + 0, 104, Funcoes.setMascara( rs.getString( "CepCli" ), "#####-###" ) );
					imp.say( imp.pRow() + 0, 125, StringFunctions.sqlDateToStrDate( rs.getDate( "DtSaidaVenda" ) ) );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 9, rs.getString( "CidCli" ) );
					imp.say( imp.pRow() + 0, 58, ( rs.getString( "DDDCli" ) != null ? "(" + rs.getString( "DDDCli" ) + ")" : "" ) + ( rs.getString( "FoneCli" ) != null ? Funcoes.setMascara( rs.getString( "FoneCli" ).trim(), "####-####" ) : "" ).trim() );
					imp.say( imp.pRow() + 0, 83, rs.getString( "UfCli" ) );
					imp.say( imp.pRow() + 0, 90, rs.getString( "RgCli" ) != null ? rs.getString( "RgCli" ) : rs.getString( "InscCli" ) );
					imp.say( imp.pRow() + 0, 125, sHora );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );

					imp.say( imp.pRow() + 0, 59, sVencs[ 0 ] );
					imp.say( imp.pRow() + 0, 79, sVencs[ 1 ] );
					imp.say( imp.pRow() + 0, 99, sVencs[ 2 ] );
					imp.say( imp.pRow() + 0, 123, sVencs[ 3 ] );
					// imp.say(imp.pRow()+1,0,"");
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 59, sVals[ 0 ] );
					imp.say( imp.pRow() + 0, 79, sVals[ 1 ] );
					imp.say( imp.pRow() + 0, 99, sVals[ 2 ] );
					imp.say( imp.pRow() + 0, 123, sVals[ 3 ] );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					// imp.say(imp.pRow()+1,0,""+imp.comprimido());
				}

				imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );

				Vector<?> vDesc = Funcoes.strToVectorSilabas( rs.getString( "ObsItVenda" ) == null || rs.getString( "ObsItVenda" ).equals( "" ) ? ( rs.getString( "DescProd" ).trim() ) : rs.getString( "ObsItVenda" ), 46 );
				String sDesc = "";
				for ( int iConta = 0; ( ( iConta < 20 ) && ( vDesc.size() > iConta ) ); iConta++ ) {
					if ( !vDesc.elementAt( iConta ).toString().equals( "" ) )
						sDesc = vDesc.elementAt( iConta ).toString();
					else
						sDesc = "";

					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 9, sDesc );
				}

				sSigla = "";
				sCodfisc = rs.getString( "CodFisc" );
				if ( sCodfisc == null )
					sCodfisc = "";
				sCodfisc = sCodfisc.trim();
				bAchouCF = false;
				iSigla = 0;
				for ( int iCF = 0; iCF < vClfiscal.size(); iCF++ ) {
					if ( sCodfisc.equals( vClfiscal.elementAt( iCF )[ 1 ] ) ) {
						sSigla = vClfiscal.elementAt( iCF )[ 0 ];
						bAchouCF = true;
						break;
					}
					iSigla = iCF;
				}
				if ( !bAchouCF ) {
					sSigla = "" + ( (char) ( 66 + iSigla ) );
					sItemCF = new String[ 2 ];
					sItemCF[ 0 ] = sSigla;
					sItemCF[ 1 ] = sCodfisc;
					vClfiscal.addElement( sItemCF );
				}

				if ( vClfiscal.size() > 4 ) {
					JOptionPane.showMessageDialog( null, "Número máximo de classificações possíveis na mesma nota ultrapassado." );
				}

				imp.say( imp.pRow() + 0, 59, sSigla );
				imp.say( imp.pRow() + 0, 62, Funcoes.copy( rs.getString( "OrigFisc" ), 0, 1 ) + Funcoes.copy( rs.getString( "CodTratTrib" ), 0, 2 ) );
				imp.say( imp.pRow() + 0, 68, rs.getString( "CodUnid" ).substring( 0, 4 ) );
				imp.say( imp.pRow() + 0, 74, "" + rs.getDouble( "QtdItVenda" ) );
				imp.say( imp.pRow() + 0, 83, Funcoes.strDecimalToStrCurrency( 8, 2, "" + ( ( new BigDecimal( rs.getDouble( "VlrLiqItVenda" ) ) ).divide( new BigDecimal( rs.getDouble( "QtdItVenda" ) ), 2, BigDecimal.ROUND_HALF_UP ) ) ) );
				imp.say( imp.pRow() + 0, 98, Funcoes.strDecimalToStrCurrency( 13, 2, rs.getString( "VlrLiqItVenda" ) ) );
				imp.say( imp.pRow() + 0, 115, "" + rs.getDouble( "PercICMSItVenda" ) );
				imp.say( imp.pRow() + 0, 121, "" + rs.getDouble( "PercIPIItvenda" ) );
				imp.say( imp.pRow() + 0, 130, Funcoes.strDecimalToStrCurrency( 7, 2, rs.getString( "VlrIPIItvenda" ) ) );

				iItImp++;
				// System.out.println(imp.pRow()+" = iItImp : "+iItImp);
				if ( ( iItImp == rs.getInt( 1 ) ) || ( imp.pRow() >= iLinMaxItens ) ) {
					if ( imp.pRow() > iLinMaxItens ) {
						Funcoes.mensagemInforma( null, "Número de itens ultrapassa capacidade do formulário!" );
						imp.fechaGravacao();
						return false;

					}
					if ( iItImp == rs.getInt( 1 ) ) {
						int iRow = imp.pRow();
						for ( int i = 0; i < ( iLinMaxItens - 6 - iRow ); i++ ) {
							imp.say( imp.pRow() + 1, 0, "" );
						}
						if ( !sMatObs[ 0 ].equals( "" ) ) {
							imp.say( imp.pRow() + 1, 0, "" );
							imp.say( imp.pRow() + 0, 27, sMatObs[ 0 ] );
						}
						if ( !sMatObs[ 1 ].equals( "" ) ) {
							imp.say( imp.pRow() + 1, 0, "" );
							imp.say( imp.pRow() + 0, 27, sMatObs[ 1 ] );
						}
						if ( !sMatObs[ 2 ].equals( "" ) ) {
							imp.say( imp.pRow() + 1, 0, "" );
							imp.say( imp.pRow() + 0, 27, sMatObs[ 2 ] );
						}
						for ( int i = 0; i < ( iLinMaxItens - imp.pRow() ); i++ ) {
							imp.say( imp.pRow() + 1, 0, "" );
						}
						// System.out.println(imp.pRow()+" = iItImp - 2 : "+iItImp);
						// imp.say(imp.pRow()+1,0,"");
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 5, Funcoes.strDecimalToStrCurrency( 20, 2, rs.getString( "VlrBaseICMSVenda" ) ) );
						imp.say( imp.pRow() + 0, 33, Funcoes.strDecimalToStrCurrency( 20, 2, rs.getString( "VlrICMSVenda" ) ) );
						imp.say( imp.pRow() + 0, 114, Funcoes.strDecimalToStrCurrency( 20, 2, rs.getString( "VlrProdVenda" ) ) );
						// imp.say(imp.pRow()+0,113,Funcoes.strDecimalToStrCurrency(20,2,rs.getString("VlrLiqVenda")));
						imp.say( imp.pRow() + 1, 0, "" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 6, Funcoes.strDecimalToStrCurrency( 20, 2, rs.getString( "VlrFreteVenda" ) ) );
						imp.say( imp.pRow() + 0, 65, Funcoes.strDecimalToStrCurrency( 20, 2, rs.getString( "VlrAdicVenda" ) ) );
						imp.say( imp.pRow() + 0, 90, Funcoes.strDecimalToStrCurrency( 20, 2, rs.getString( "VlrIPIVenda" ) ) );
						imp.say( imp.pRow() + 0, 114, Funcoes.strDecimalToStrCurrency( 20, 2, rs.getString( "VlrLiqVenda" ) ) );
						iItImp = 0;
					}
					else if ( imp.pRow() == iLinMaxItens ) {
						imp.say( imp.pRow() + 1, 0, "" );
						imp.say( imp.pRow() + 1, 0, "" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 6, "***************" );
						imp.say( imp.pRow() + 0, 36, "***************" );
						imp.say( imp.pRow() + 0, 113, "***************" );
						imp.say( imp.pRow() + 1, 0, "" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 6, "***************" );
						imp.say( imp.pRow() + 0, 65, "***************" );
						imp.say( imp.pRow() + 0, 90, "***************" );
						imp.say( imp.pRow() + 0, 113, "***************" );
					}
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 0, 6, rs.getString( "RazTran" ) );
					imp.say( imp.pRow() + 0, 86, rs.getString( "TipoFreteVD" ).equals( "C" ) ? "1" : "2" );
					imp.say( imp.pRow() + 0, 93, rs.getString( "PlacaFreteVD" ) );
					imp.say( imp.pRow() + 0, 107, rs.getString( "UfFreteVD" ) );

					sTipoTran = rs.getString( "TipoTran" );
					if ( sTipoTran == null )
						sTipoTran = "T";

					if ( sTipoTran.equals( "C" ) ) {
						imp.say( imp.pRow() + 0, 114, Funcoes.setMascara( rs.getString( "CnpjCli" ) != null ? rs.getString( "CnpjCli" ) : "", "##.###.###/####-##" ) );
					}
					else {
						imp.say( imp.pRow() + 0, 114, Funcoes.setMascara( rs.getString( "CnpjTran" ) != null ? rs.getString( "CnpjTran" ) : "", "##.###.###/####-##" ) );
					}

					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 6, Funcoes.copy( rs.getString( "EndTran" ), 0, 42 ) + "   " + Funcoes.copy( rs.getString( "NumTran" ), 0, 6 ) );
					imp.say( imp.pRow() + 0, 77, Funcoes.copy( rs.getString( "CidTran" ), 0, 30 ) );
					imp.say( imp.pRow() + 0, 107, rs.getString( "UfTran" ) );

					sTipoTran = rs.getString( "TipoTran" );
					if ( sTipoTran == null )
						sTipoTran = "T";
					if ( sTipoTran.equals( "C" ) ) {
						imp.say( imp.pRow() + 0, 114, rs.getString( "InscCli" ) );
					}
					else {
						imp.say( imp.pRow() + 0, 114, rs.getString( "InscTran" ) );
					}

					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 6, rs.getString( "QtdFreteVD" ) );
					imp.say( imp.pRow() + 0, 26, rs.getString( "EspFreteVD" ) );
					imp.say( imp.pRow() + 0, 55, rs.getString( "MarcaFreteVD" ) );
					imp.say( imp.pRow() + 0, 106, rs.getString( "PesoBrutVD" ) );
					imp.say( imp.pRow() + 0, 129, rs.getString( "PesoLiqVD" ) );
					// System.out.println(imp.pRow()+" 1= Lins: "+iLinPag);
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );

					// String sEnt = "";
					// sEnt += rs.getString("EndEnt") != null ? rs.getString("EndEnt").trim() : "";
					// sEnt += rs.getString("NumEnt") != null ? ", "+rs.getString("NumEnt").trim() : "";
					// sEnt += rs.getString("ComplEnt") != null ? " - "+rs.getString("ComplEnt").trim() : "";
					// sEnt += rs.getString("BairEnt") != null ? "   "+rs.getString("CidEnt").trim() : "";
					// sEnt += rs.getString("CidEnt") != null ? " - "+rs.getString("CidEnt").trim() : "";
					// sEnt += rs.getString("UfEnt") != null ? "/"+rs.getString("UfEnt").trim() : "";
					// imp.say(imp.pRow()+0,3,sEnt);

					// if (!sDuplics[0].equals("")) {
					// imp.say(imp.pRow()+1,0,sDuplics[0]);
					// imp.say(imp.pRow()+0,17,sVencs[0]);
					// imp.say(imp.pRow()+0,29,sVals[0]);
					// }
					// if (!sDuplics[1].equals("")) {
					// imp.say(imp.pRow()+1,0,sDuplics[1]);
					// imp.say(imp.pRow()+0,17,sVencs[1]);
					// imp.say(imp.pRow()+0,29,sVals[1]);
					// }
					// if (!sDuplics[2].equals("")) {
					// imp.say(imp.pRow()+1,0,sDuplics[2]);
					// imp.say(imp.pRow()+0,17,sVencs[2]);
					// imp.say(imp.pRow()+0,29,sVals[2]);
					// }

					imp.say( imp.pRow() + 1, 0, "" );

					iSigla = 0;
					for ( int iCF = 1; iCF < vClfiscal.size(); iCF++ ) {
						// imp.say(imp.pRow()+1,10,( (String[]) vClfiscal.elementAt(iCF))[1] );
						imp.say( imp.pRow() + 1, 14, vClfiscal.elementAt( iCF )[ 1 ] );
						iSigla = iCF;
					}

					/*
					 * imp.say(imp.pRow()+0,0,sMatMens[0]); imp.say(imp.pRow()+1,0,""); imp.say(imp.pRow()+0,0,sMatMens[1]); imp.say(imp.pRow()+1,0,""); imp.say(imp.pRow()+0,0,sMatMens[2]); imp.say(imp.pRow()+0,63,sFiscAdic);
					 */
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 9 - iSigla, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					// imp.say(imp.pRow()+0,3,"Total: "+Funcoes.strDecimalToStrCurrency(20,2,rs.getString("VlrLiqVenda")));
					// imp.say(imp.pRow()+0,35,rs.getString("NomeCli") != null ? "Cliente: "+rs.getString("NomeCli") : "");
					// imp.say(imp.pRow()+0,100,"Emit.: "+Aplicativo.getUsuario().getIdusu());
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					// imp.say(imp.pRow()+0,128,sNumNota);

					for ( int i = imp.pRow(); i <= iLinPag; i++ ) {
						imp.say( imp.pRow() + 1, 0, "" );
					}
					imp.setPrc( 0, 0 );
					imp.incPags();
				}
			}
			imp.fechaGravacao();
			bRetorno = true;
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( null, "Erro ao consultar tabela de Venda!" + err.getMessage() );
			bRetorno = false;
		}
		return bRetorno;
	}
}
