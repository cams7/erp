/**
 * @version 12/12/2003 <BR>
 * @author Setpoint Informática Ltda./Marco Antonio Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 *         Pacote: leiautes <BR>
 *         Classe: @(#)NFModelcraft.java <BR>
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
 *         Layout da nota fiscal para a empresa Modelcraft Ltda.
 */

package org.freedom.layout.nf;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.component.Leiaute;
import org.freedom.library.functions.Funcoes;

public class NFMcraftSvco extends Leiaute {

	public boolean imprimir( ResultSet rs, ResultSet rsRec, ImprimeOS imp ) {

		boolean bRetorno;
		int iNumNota = 0;
		int iItImp = 0;
		int iLinPag = imp.verifLinPag( "NF" );
		String sTipoTran = "";
		String[] sNat = new String[ 2 ];
		// String[] sVencs = new String[5];
		// String[] sVals = new String[4];
		String sObs = "";
		String[] sMatObs = null;

		// String sIncra = "" ;
		// String[] sMarcs = {"*","**","***","****"}; //Tipos de Marcs.
		// String[] sMarcs2 = {"\"","\"\"","\"\"\"","\"\"\"\""}; //Tipos de Marcs.
		// String sHora = StringFunctions.strZero(""+cHora.get(Calendar.HOUR),2)+":"+StringFunctions.strZero(""+cHora.get(Calendar.MINUTE),2)+":"+StringFunctions.strZero(""+cHora.get(Calendar.SECOND),2);
		try {
			/*
			 * for (int i=0; i<4; i++) {
			 * 
			 * if (bFat) { if (rsRec.next()) { sVencs[i] = StringFunctions.sqlDateToStrDate(rsRec.getDate("DtVencItRec")); sVals[i] = Funcoes.strDecimalToStrCurrency(12,2,rsRec.getString("VlrParcItRec")); } else { bFat = false; sVencs[i] = ""; sVals[i] = ""; } } else { bFat = false; sVencs[i] = "";
			 * sVals[i] = ""; } }
			 */
			imp.limpaPags();
			boolean bNat = true;
			while ( rs.next() ) {
				if ( bNat ) {
					sNat[ 0 ] = rs.getString( "DescNat" );
					sNat[ 1 ] = Funcoes.setMascara( rs.getString( "CodNat" ), "#.###" );
					sMatObs = Funcoes.strToStrArray( rs.getString( "ObsVenda" ) != null ? rs.getString( "ObsVenda" ) : "", 3 );
					iNumNota = rs.getInt( "DocVenda" );
					bNat = false;

				}
				if ( imp.pRow() == 0 ) {
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() + imp.expandido() );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 4, 0, "" );
					imp.say( imp.pRow() + 0, 120, rs.getString( "DocVenda" ) != null ? StringFunctions.strZero( "" + iNumNota, 6 ) : "000000" );
					imp.say( imp.pRow() + 1, 0, "" );
					// imp.say(imp.pRow()+1,0,"");
					// imp.say(imp.pRow()+1,0,"");
					// imp.say(imp.pRow()+1,0,"");
					// imp.say(imp.pRow()+1,0,"");
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 0, 109, StringFunctions.sqlDateToStrDate( rs.getDate( "DtEmitVenda" ) ) );

					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					// imp.say(imp.pRow()+0,94,"X");
					imp.say( imp.pRow() + 0, 20, rs.getString( "RazCli" ) + " - " + rs.getInt( "CodCli" ) );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 20, Funcoes.copy( rs.getString( "EndCli" ), 0, 30 ).trim() + ", " + ( rs.getString( "NumCli" ) != null ? Funcoes.copy( rs.getString( "NumCli" ), 0, 6 ).trim() : "" ).trim() + " - "
							+ ( rs.getString( "ComplCli" ) != null ? Funcoes.copy( rs.getString( "ComplCli" ), 0, 9 ).trim() : "" ).trim() );
					imp.say( imp.pRow() + 0, 60, rs.getString( "BairCli" ) );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 0, 20, rs.getString( "CidCli" ) );
					imp.say( imp.pRow() + 0, 94, rs.getString( "UfCli" ) );
					imp.say( imp.pRow() + 0, 112, Funcoes.setMascara( rs.getString( "CepCli" ), "#####-###" ) );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 0, 30, rs.getString( "CpfCli" ) != null ? Funcoes.setMascara( rs.getString( "CpfCli" ), "###.###.###-##" ) : Funcoes.setMascara( rs.getString( "CnpjCli" ), "##.###.###/####-##" ) );
					imp.say( imp.pRow() + 0, 91, rs.getString( "RgCli" ) != null ? rs.getString( "RgCli" ) : rs.getString( "InscCli" ) );

					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					// imp.say(imp.pRow()+0,30,sNat[1]);
					imp.say( imp.pRow() + 0, 40, sNat[ 0 ] );
					imp.say( imp.pRow() + 1, 0, "" );

					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );

					// sIncra = rs.getString("IncraCli");
					// if (sIncra != null ){
					// imp.say(imp.pRow()+0,6,rs.getInt("CodCli")+" - "+rs.getString("RazCli")+"Incra:");
					// imp.say(imp.pRow()+0,71,rs.getString("IncraCli"));
					// }
					// else {
					// imp.say(imp.pRow()+0,6,rs.getInt("CodCli")+" - "+rs.getString("RazCli"));
					// }

					// imp.say(imp.pRow()+1,0,"");
					// imp.say(imp.pRow()+1,0,""+imp.comprimido());

					// imp.say(imp.pRow()+0,126,StringFunctions.sqlDateToStrDate(rs.getDate("DtSaidaVenda")));
					// imp.say(imp.pRow()+1,0,"");
					// imp.say(imp.pRow()+1,0,""+imp.comprimido());

					// imp.say(imp.pRow()+0,63,Funcoes.setMascara(rs.getString("FoneCli"),"(####)####-####")+" - "+Funcoes.setMascara(rs.getString("FaxCli"),"####-####"));

					// imp.say(imp.pRow()+0,128,sHora);
					// imp.say(imp.pRow()+1,0,"");
					// imp.say(imp.pRow()+1,0,"");
					// imp.say(imp.pRow()+1,0,""+imp.comprimido());
					// imp.say(imp.pRow()+0,37,sVencs[0]);
					// imp.say(imp.pRow()+0,63,sVencs[1]);
					// imp.say(imp.pRow()+0,89,sVencs[2]);
					// imp.say(imp.pRow()+0,117,sVencs[3]);
					// imp.say(imp.pRow()+1,0,"");
					// imp.say(imp.pRow()+1,0,""+imp.comprimido());
					// imp.say(imp.pRow()+0,37,sVals[0]);
					// imp.say(imp.pRow()+0,63,sVals[1]);
					// imp.say(imp.pRow()+0,89,sVals[2]);
					// imp.say(imp.pRow()+0,117,sVals[3]);
					// imp.say(imp.pRow()+1,0,"");
					// imp.say(imp.pRow()+1,0,"");
				}

				imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
				// imp.say(imp.pRow()+0,2,Funcoes.alinhaDir(rs.getInt("CodProd"),8));

				/*
				 * //Descrições adicionais colocadas junto a decrição do produto. String[] sDesc = Funcoes.strToStrArray(rs.getString("ObsItVenda"),20); int iConta = 0; while(!sDesc[iConta].equals("") && iConta < 20) { if (iConta > 0) //Soh naum pula uma linha na primeira.
				 * imp.say(imp.pRow()+1,0,""+imp.comprimido()); imp.say(imp.pRow()+0,14,sDesc[iConta]); iConta++; } if (iConta == 0) //Se naum imprimiu nenhuma descadic: imp.say(imp.pRow()+0,14,rs.getString("DescProd").trim());
				 */

				Vector<?> vDesc = Funcoes.strToVectorSilabas( rs.getString( "ObsItVenda" ) == null || rs.getString( "ObsItVenda" ).equals( "" ) ? ( rs.getString( "DescProd" ).trim() ) : rs.getString( "ObsItVenda" ), 70 );
				String sDesc = "";
				for ( int iConta = 0; ( ( iConta < 20 ) && ( vDesc.size() > iConta ) ); iConta++ ) {
					if ( !vDesc.elementAt( iConta ).toString().equals( "" ) )
						sDesc = vDesc.elementAt( iConta ).toString();
					else
						sDesc = "";

					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 8, sDesc );
				}

				// Continua o fluxo:
				// imp.say(imp.pRow()+0,84,Funcoes.copy(rs.getString("OrigFisc"),0,1)+Funcoes.copy(rs.getString("CodTratTrib"),0,2));
				// imp.say(imp.pRow()+0,89,rs.getString("CodUnid").substring(0,4));
				// imp.say(imp.pRow()+0,80,""+rs.getDouble("QtdItVenda"));

				imp.say( imp.pRow() + 0, 120, Funcoes.strDecimalToStrCurrency( 13, 2, "" + ( new BigDecimal( rs.getString( "VlrLiqItVenda" ) ) ).divide( new BigDecimal( rs.getDouble( "QtdItVenda" ) ), 2, BigDecimal.ROUND_HALF_UP ) ) );
				// imp.say(imp.pRow()+0,120,Funcoes.strDecimalToStrCurrency(13,2,rs.getString("VlrProdItVenda")));
				// imp.say(imp.pRow()+0,120,Funcoes.strDecimalToStrCurrency(13,2,""+rs.getString("VlrLiqItVenda")));
				// imp.say(imp.pRow()+0,135,""+rs.getDouble("PercICMSItVenda"));

				iItImp++;
				System.out.println( imp.pRow() + " = iItImp : " + iItImp );
				if ( ( iItImp == rs.getInt( 1 ) ) || ( imp.pRow() == 46 ) ) {
					if ( iItImp == rs.getInt( 1 ) ) {
						int iRow = imp.pRow();

						for ( int i = 0; i < ( 43 - iRow ); i++ ) {
							imp.say( imp.pRow() + 1, 0, "" );
						}

						if ( !sMatObs[ 0 ].equals( "" ) ) {
							imp.say( imp.pRow() + 1, 0, "" );
							imp.say( imp.pRow() + 0, 23, sMatObs[ 0 ] );
						}
						if ( !sMatObs[ 1 ].equals( "" ) ) {
							imp.say( imp.pRow() + 1, 0, "" );
							imp.say( imp.pRow() + 0, 23, sMatObs[ 1 ] );
						}
						if ( !sMatObs[ 2 ].equals( "" ) ) {
							imp.say( imp.pRow() + 1, 0, "" );
							imp.say( imp.pRow() + 0, 23, sMatObs[ 2 ] );
						}
						iRow = imp.pRow();
						for ( int i = 0; i < ( 46 - iRow ); i++ ) {
							imp.say( imp.pRow() + 1, 0, "" );
						}
						imp.say( imp.pRow() + 1, 0, "" );
						imp.say( imp.pRow() + 1, 0, "" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );

						// imp.say(imp.pRow()+0,4,Funcoes.strDecimalToStrCurrency(20,2,rs.getString("VlrBaseICMSVenda")));
						// imp.say(imp.pRow()+0,32,Funcoes.strDecimalToStrCurrency(20,2,rs.getString("VlrICMSVenda")));
						// imp.say(imp.pRow()+0,116,Funcoes.strDecimalToStrCurrency(20,2,rs.getString("VlrProdVenda")));
						// imp.say(imp.pRow()+0,114,Funcoes.strDecimalToStrCurrency(20,2,rs.getString("VlrLiqVenda")));
						imp.say( imp.pRow() + 1, 0, "" );
						imp.say( imp.pRow() + 1, 0, "" );
						imp.say( imp.pRow() + 1, 0, "" );
						imp.say( imp.pRow() + 1, 0, "" );
						imp.say( imp.pRow() + 1, 0, "" );
						imp.say( imp.pRow() + 1, 0, "" );
						imp.say( imp.pRow() + 1, 0, "" );
						imp.say( imp.pRow() + 1, 0, "" );
						imp.say( imp.pRow() + 1, 0, "" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						// imp.say(imp.pRow()+0,4,Funcoes.strDecimalToStrCurrency(20,2,rs.getString("VlrFreteVenda")));
						// imp.say(imp.pRow()+0,62,Funcoes.strDecimalToStrCurrency(20,2,rs.getString("VlrAdicVenda")));
						// imp.say(imp.pRow()+0,87,Funcoes.strDecimalToStrCurrency(20,2,rs.getString("VlrIPIVenda")));
						imp.say( imp.pRow() + 0, 114, Funcoes.strDecimalToStrCurrency( 20, 2, rs.getString( "VlrLiqVenda" ) ) );
						iItImp = 0;
						sObs += rs.getString( "ObsVenda" ) != null ? rs.getString( "ObsVenda" ).trim() : "";
					}
					else if ( imp.pRow() == 46 ) {
						imp.say( imp.pRow() + 1, 0, "" );
						imp.say( imp.pRow() + 1, 0, "" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 4, "***************" );
						imp.say( imp.pRow() + 0, 32, "***************" );
						imp.say( imp.pRow() + 0, 114, "***************" );
						imp.say( imp.pRow() + 1, 0, "" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 4, "***************" );
						imp.say( imp.pRow() + 0, 62, "***************" );
						imp.say( imp.pRow() + 0, 87, "***************" );
						imp.say( imp.pRow() + 0, 114, "***************" );
					}
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					// imp.say(imp.pRow()+0,6,rs.getString("RazTran"));
					// imp.say(imp.pRow()+0,84,rs.getString("TipoFreteVD").equals("C") ? "1" : "2");
					// imp.say(imp.pRow()+0,90,rs.getString("PlacaFreteVD"));
					// imp.say(imp.pRow()+0,105,rs.getString("UfFreteVD"));

					// sTipoTran = rs.getString("TipoTran");

					if ( sTipoTran == null )
						sTipoTran = "T";
					if ( sTipoTran.equals( "C" ) ) {
						// imp.say(imp.pRow()+0,111,Funcoes.setMascara(rs.getString("CnpjCli") != null ? rs.getString("CnpjCli") : "","##.###.###/####-##"));
					}

					else {
						// imp.say(imp.pRow()+0,111,Funcoes.setMascara(rs.getString("CnpjTran") != null ? rs.getString("CnpjTran") : "","##.###.###/####-##"));
					}

					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					// imp.say(imp.pRow()+0,6,Funcoes.copy(rs.getString("EndTran"),0,42)+", "+Funcoes.copy(rs.getString("NumTran"),0,6));
					// imp.say(imp.pRow()+0,69,rs.getString("CidTran"));
					// imp.say(imp.pRow()+0,105,rs.getString("UfTran"));

					if ( rs.getString( "TipoTran" ).compareTo( "C" ) == 0 ) {
						// imp.say(imp.pRow()+0,111,rs.getString("InscCli"));
					}
					else {
						// imp.say(imp.pRow()+0,111,rs.getString("InscTran"));
					}

					// imp.say(imp.pRow()+1,0,"");
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					// imp.say(imp.pRow()+0,6,rs.getString("QtdFreteVD"));
					// imp.say(imp.pRow()+0,26,rs.getString("EspFreteVD"));
					// imp.say(imp.pRow()+0,47,rs.getString("MarcaFreteVD"));
					// imp.say(imp.pRow()+0,93,rs.getString("PesoBrutVD"));
					// imp.say(imp.pRow()+0,120,rs.getString("PesoLiqVD"));
					System.out.println( imp.pRow() + " 1= Lins: " + iLinPag );
					// imp.say(imp.pRow()+1,0,"");
					// imp.say(imp.pRow()+1,0,"");
					// imp.say(imp.pRow()+3,0,""+imp.comprimido());
					// imp.say(imp.pRow()+0,25,Funcoes.alinhaDir(rs.getInt("CodCli"),10));
					// imp.say(imp.pRow()+0,40,Funcoes.alinhaDir(rs.getInt("CodVenda"),10));
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					// imp.say(imp.pRow()+1,0,"");
					// imp.say(imp.pRow()+0,120,rs.getString("DocVenda") != null ? StringFunctions.strZero(""+iNumNota,6) : "000000");
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );

					sMatObs = Funcoes.strToStrArray( sObs, 5 );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					System.out.println( imp.pRow() + " =T Lins: " + iLinPag );
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
			JOptionPane.showMessageDialog( null, "Erro ao consultar tabela de Venda!" + err.getMessage() );
			bRetorno = false;
		}
		return bRetorno;
	}
}
