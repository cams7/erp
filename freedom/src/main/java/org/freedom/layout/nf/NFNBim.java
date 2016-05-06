/**
 * @version 10/12/2003 <BR>
 * @author Setpoint Informática Ltda./Marco Antonio Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 *         Pacote: leiautes <BR>
 *         Classe: @(#)NFNBim.java <BR>
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

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Vector;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.component.Leiaute;
import org.freedom.library.functions.Extenso;
import org.freedom.library.functions.Funcoes;

public class NFNBim extends Leiaute {

	private BigDecimal bigSomaServ = new BigDecimal( 0 );

	@ SuppressWarnings ( "unchecked" )
	public boolean imprimir( ResultSet rs, ResultSet rsRec, ImprimeOS imp ) {

		Calendar cHora = Calendar.getInstance();
		boolean bRetorno;
		int iNumNota = 0;
		String sNumNota = "";
		int iProd = 0;
		int iServ = 0;
		String sTipoTran = "";
		boolean bFat = true;
		boolean bTotalizou = false;
		Vector<String> vValores = new Vector<String>();
		Vector<Vector<Object>> vDescServ = new Vector<Vector<Object>>();
		String[] sNat = new String[ 4 ];
		String[] sVencs = new String[ 4 ];
		String[] sVals = new String[ 4 ];
		String[] sDuplics = new String[ 4 ];
		String[] sMatObs = null;
		// String sFiscAdic = "";
		bigSomaServ = new BigDecimal( 0 );
		// String[] sMarcs = {"\"","\"\"","\"\"\"","\"\"\"\""}; //Tipos de Marcs.
		// String[] sMarcs2 = {"*","**","***","****"}; //Tipos de Marcs.
		String sHora = StringFunctions.strZero( "" + cHora.get( Calendar.HOUR_OF_DAY ), 2 ) + ":" + StringFunctions.strZero( "" + cHora.get( Calendar.MINUTE ), 2 ) + ":" + StringFunctions.strZero( "" + cHora.get( Calendar.SECOND ), 2 );
		try {
			imp.limpaPags();
			boolean bNat = true;
			// Vector vServ = new Vector();
			// int iServAtual = 0;

			while ( rs.next() ) {

				iNumNota = rs.getInt( "DocVenda" );
				if ( iNumNota == 0 ) {
					sNumNota = "000000";
				}
				else {
					sNumNota = StringFunctions.strZero( "" + iNumNota, 6 );
				}
				for ( int i = 0; i < 4; i++ ) {
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

				}

				if ( bNat ) {
					sNat[ 0 ] = rs.getString( "DescNat" );
					sNat[ 1 ] = Funcoes.setMascara( rs.getString( "CodNat" ), "#.##" );
					sMatObs = Funcoes.strToStrArray( rs.getString( "ObsVenda" ) != null ? rs.getString( "ObsVenda" ) : "", 3 );
					bNat = false;
				}
				if ( imp.pRow() == 0 ) {
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					// imp.say(imp.pRow()+1,0,""+imp.comprimido());
					// imp.say(imp.pRow()+1,0,""+imp.comprimido());
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 107, "X" );
					imp.say( imp.pRow() + 0, 130, sNumNota );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 0, 8, sNat[ 0 ] );
					imp.say( imp.pRow() + 0, 51, sNat[ 1 ] );
					imp.say( imp.pRow() + 1, 0, "" );
					// imp.say(imp.pRow()+1,0,"");
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 9, rs.getInt( "CodCli" ) + " - " + rs.getString( "RazCli" ) );
					imp.say( imp.pRow() + 0, 95, rs.getString( "CpfCli" ) != null ? Funcoes.setMascara( rs.getString( "CpfCli" ), "###.###.###-##" ) : Funcoes.setMascara( rs.getString( "CnpjCli" ), "##.###.###/####-##" ) );
					imp.say( imp.pRow() + 0, 126, StringFunctions.sqlDateToStrDate( rs.getDate( "DtEmitVenda" ) ) );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 9, Funcoes.copy( rs.getString( "EndCli" ), 0, 50 ).trim() + ", " + ( rs.getString( "NumCli" ) != null ? Funcoes.copy( rs.getString( "NumCli" ), 0, 6 ).trim() : "" ).trim() + " - "
							+ ( rs.getString( "ComplCli" ) != null ? Funcoes.copy( rs.getString( "ComplCli" ), 0, 9 ).trim() : "" ).trim() );
					imp.say( imp.pRow() + 0, 82, rs.getString( "BairCli" ) != null ? Funcoes.copy( rs.getString( "BairCli" ), 0, 15 ) : "" );
					imp.say( imp.pRow() + 0, 106, Funcoes.setMascara( rs.getString( "CepCli" ), "#####-###" ) );
					imp.say( imp.pRow() + 0, 126, StringFunctions.sqlDateToStrDate( rs.getDate( "DtSaidaVenda" ) ) );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 9, rs.getString( "CidCli" ) );
					imp.say( imp.pRow() + 0, 65, ( rs.getString( "DDDCli" ) != null ? "(" + rs.getString( "DDDCli" ) + ")" : "" ) + ( rs.getString( "FoneCli" ) != null ? Funcoes.setMascara( rs.getString( "FoneCli" ).trim(), "####-####" ) : "" ) );
					imp.say( imp.pRow() + 0, 87, rs.getString( "UfCli" ) );
					imp.say( imp.pRow() + 0, 98, rs.getString( "RgCli" ) != null ? rs.getString( "RgCli" ) : rs.getString( "InscCli" ) );
					imp.say( imp.pRow() + 0, 126, sHora );
					// imp.say(imp.pRow()+1,0,"");
					// imp.say(imp.pRow()+1,0,"");
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 11, sDuplics[ 0 ] );
					imp.say( imp.pRow() + 0, 31, sVencs[ 0 ] );
					imp.say( imp.pRow() + 0, 46, sVals[ 0 ] );
					imp.say( imp.pRow() + 0, 69, sDuplics[ 1 ] );
					imp.say( imp.pRow() + 0, 91, sVencs[ 1 ] );
					imp.say( imp.pRow() + 0, 106, sVals[ 1 ] );

					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );

					imp.say( imp.pRow() + 0, 11, sDuplics[ 2 ] );
					imp.say( imp.pRow() + 0, 31, sVencs[ 2 ] );
					imp.say( imp.pRow() + 0, 46, sVals[ 2 ] );

					imp.say( imp.pRow() + 0, 69, sDuplics[ 3 ] );
					imp.say( imp.pRow() + 0, 91, sVencs[ 3 ] );
					imp.say( imp.pRow() + 0, 106, sVals[ 3 ] );

					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );

					imp.say( imp.pRow() + 0, 11, rs.getString( "CidCob" ) );
					imp.say( imp.pRow() + 0, 46, rs.getString( "EndCob" ) != null ? rs.getString( "EndCob" ).trim() + ", " + ( rs.getString( "NumCob" ) != null ? rs.getString( "NumCob" ) : "" ) : "" );
					imp.say( imp.pRow() + 1, 0, "" );
					// imp.say(imp.pRow()+1,0,"");

					String sValorTotLiqVenda = Extenso.extenso( rs.getDouble( "VlrLiqVenda" ), "real", "reais", "centavo", "centavos" ).toUpperCase();
					imp.say( imp.pRow() + 1, 9, sValorTotLiqVenda );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
					imp.say( imp.pRow() + 1, 0, "" );
				}

				if ( !rs.getString( "TipoProd" ).equals( "S" ) ) {
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 11, rs.getString( "CodProd" ) );
					Vector<?> vDesc = Funcoes.strToVectorSilabas( rs.getString( "ObsItVenda" ) == null || rs.getString( "ObsItVenda" ).equals( "" ) ? ( rs.getString( "DescProd" ).trim() ) : rs.getString( "ObsItVenda" ), 46 );
					String sDesc = "";
					for ( int iConta = 0; ( ( iConta < 20 ) && ( vDesc.size() > iConta ) ); iConta++ ) {
						if ( !vDesc.elementAt( iConta ).toString().equals( "" ) )
							sDesc = vDesc.elementAt( iConta ).toString();
						else
							sDesc = "";
						if ( iConta > 0 )
							imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 19, sDesc );
						iProd = iProd + vDesc.size();
					}

					/*
					 * String sSigla = ""; String sCodfisc= rs.getString("CodFisc");
					 * 
					 * if (sCodfisc == null) sSigla=""; sCodfisc = sCodfisc.trim(); if (sCodfisc.equals("84220202")) sSigla = "1"; else if (sCodfisc.equals("84220500")) sSigla = "2"; else if (sCodfisc.equals("84631700")) sSigla = "3"; else if (sCodfisc.equals("84631800")) sSigla = "4"; else if
					 * (sCodfisc.equals("84229000")) sSigla = "5"; else if (sCodfisc.equals("84220399")) sSigla = "6"; else { if (!sFiscAdic.equals("")) JOptionPane.showMessageDialog(null,"Mais de um produto sem classificacao definida,sigla assinalada em branco."); else { sSigla = " "; sFiscAdic =
					 * sCodfisc ; }
					 * 
					 * }
					 */
					// imp.say(imp.pRow()+0,56,sSigla);
					imp.say( imp.pRow() + 0, 68, Funcoes.copy( rs.getString( "OrigFisc" ), 0, 1 ) + Funcoes.copy( rs.getString( "CodTratTrib" ), 0, 2 ) );
					imp.say( imp.pRow() + 0, 74, rs.getString( "CodUnid" ).substring( 0, 4 ) );
					imp.say( imp.pRow() + 0, 81, "" + rs.getDouble( "QtdItVenda" ) );
					imp.say( imp.pRow() + 0, 91, Funcoes.strDecimalToStrCurrency( 8, 2, "" + ( ( new BigDecimal( rs.getDouble( "VlrLiqItVenda" ) ) ).divide( new BigDecimal( rs.getDouble( "QtdItVenda" ) ), 2, BigDecimal.ROUND_HALF_UP ) ) ) );
					imp.say( imp.pRow() + 0, 101, Funcoes.strDecimalToStrCurrency( 13, 2, rs.getString( "VlrLiqItVenda" ) ) );
					imp.say( imp.pRow() + 0, 118, "" + rs.getDouble( "PercICMSItVenda" ) );
					imp.say( imp.pRow() + 0, 124, "" + rs.getDouble( "PercIPIItvenda" ) );
					imp.say( imp.pRow() + 0, 132, Funcoes.strDecimalToStrCurrency( 7, 2, rs.getString( "VlrIPIItvenda" ) ) );
				}
				else {
					Vector<Serializable> vDesc = new Vector<Serializable>();
					vDesc.addElement( Funcoes.strToVectorSilabas( rs.getString( "ObsItVenda" ) == null || rs.getString( "ObsItVenda" ).equals( "" ) ? ( rs.getString( "DescProd" ).trim() ) : rs.getString( "ObsItVenda" ), 45 ) );
					vDesc.addElement( Funcoes.strDecimalToStrCurrency( 13, 2, rs.getString( "VlrLiqItVenda" ) ) );
					if ( vDesc != null ) {
						vDescServ.addElement( (Vector<Object>) vDesc.clone() );
					}

					bigSomaServ = bigSomaServ.add( new BigDecimal( rs.getDouble( "VlrLiqItVenda" ) ) );

					iServ = iServ + vDescServ.size();

				}

				if ( !bTotalizou ) {

					vValores.addElement( rs.getString( "VlrBaseICMSVenda" ) ); // 0
					vValores.addElement( rs.getString( "VlrICMSVenda" ) ); // 1
					vValores.addElement( rs.getBigDecimal( "VlrLiqVenda" ).subtract( bigSomaServ ) + "" );// 2
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
					vValores.addElement( rs.getString( "EndTran" ) );// 14

					if ( sTipoTran.equals( "C" ) ) {
						vValores.addElement( rs.getString( "NumCli" ) != null ? rs.getString( "NumCli" ) : "" );// 15
						vValores.addElement( rs.getString( "CidCli" ) != null ? rs.getString( "CidCli" ) : "" );// 16
						vValores.addElement( rs.getString( "UfCli" ) != null ? rs.getString( "UfCli" ) : "" );// 17
						vValores.addElement( rs.getString( "InscCli" ) != null ? rs.getString( "InscCli" ) : "" ); // 18
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

					bTotalizou = true;
				}
				else
					vValores.set( 2, rs.getBigDecimal( "VlrLiqVenda" ).subtract( bigSomaServ ) + "" );

			}
			if ( imp.pRow() < 38 ) {
				imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
				for ( int i = 0; i < 3; i++ ) {
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 20, sMatObs[ i ] );
				}
			}
			if ( vDescServ.size() > 0 ) {
				impServ( vDescServ, bigSomaServ, vValores.elementAt( 24 ).toString(), imp );
			}
			impTotais( imp, vValores );
			imp.fechaGravacao();
			bRetorno = true;
			if ( iProd > 20 ) {
				Funcoes.mensagemInforma( null, "Podem haver erros na impressão da nota fiscal." + "\n" + "Produtos ultrapassam vinte linhas!" );
			}
			if ( iServ > 4 ) {
				Funcoes.mensagemInforma( null, "Podem haver erros na impressão da nota fiscal." + "\n" + "Servicos ultrapassam quatro linhas!" );
			}
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( null, "Erro ao consultar tabela de Venda!" + err.getMessage() );
			bRetorno = false;
		}
		return bRetorno;
	}

	private void impServ( Vector<Vector<Object>> vServs, BigDecimal bigSomaServ, String sVlrIss, ImprimeOS imp ) {

		for ( int i = 0; imp.pRow() < 42; i++ ) {
			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
		}
		int iImp = 0;
		if ( vServs != null ) {
			for ( int i = 0; vServs.size() > i; i++ ) {
				Vector<?> vServ = vServs.elementAt( i );
				if ( vServ != null ) {

					Vector<?> vDescServ = (Vector<?>) vServ.elementAt( 0 );
					String sVlrServ = vServ.elementAt( 1 ).toString();

					String sDesc = "";

					for ( int i2 = 0; ( vDescServ.size() > i2 ); i2++ ) {
						sDesc = vDescServ.elementAt( i2 ).toString();

						imp.say( imp.pRow() + 1, 11, sDesc );

						if ( i2 == 0 )
							imp.say( imp.pRow() + 0, 94, sVlrServ );
						if ( iImp == 1 ) {
							imp.say( imp.pRow() + 0, 120, Funcoes.strDecimalToStrCurrency( 13, 2, sVlrIss ) );
						}
						else if ( iImp == 3 )
							imp.say( imp.pRow() + 0, 120, Funcoes.strDecimalToStrCurrency( 13, 2, bigSomaServ + "" ) );

						iImp++;

					}

				}
			}
			while ( iImp < 4 ) {
				if ( iImp == 1 )
					imp.say( imp.pRow() + 1, 120, Funcoes.strDecimalToStrCurrency( 13, 2, sVlrIss ) );
				else if ( iImp == 3 )
					imp.say( imp.pRow() + 1, 120, Funcoes.strDecimalToStrCurrency( 13, 2, bigSomaServ + "" ) );
				else
					imp.say( imp.pRow() + 1, 05, "" );
				iImp++;
			}
		}
	}

	private void impTotais( ImprimeOS imp, Vector<String> vValores ) {

		try {
			for ( int i = 0; ( imp.pRow() < 49 ); i++ ) {
				imp.say( imp.pRow() + 1, 0, "" );
			}
			// imp.say(imp.pRow()+1,0,"");
			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 0, 8, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 0 ).toString() ) );
			imp.say( imp.pRow() + 0, 38, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 1 ).toString() ) );
			// imp.say(imp.pRow()+1,0,""+imp.comprimido());

			imp.say( imp.pRow() + 0, 118, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 2 ).toString() ) );

			imp.say( imp.pRow() + 1, 0, "" );
			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 0, 8, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 3 ).toString() ) );
			imp.say( imp.pRow() + 0, 68, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 4 ).toString() ) );
			imp.say( imp.pRow() + 0, 93, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 5 ).toString() ) );
			imp.say( imp.pRow() + 0, 118, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 6 ).toString() ) );
			// imp.say(imp.pRow()+1,0,"");
			imp.say( imp.pRow() + 1, 0, "" );
			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );

			imp.say( imp.pRow() + 0, 11, vValores.elementAt( 7 ).toString() );
			imp.say( imp.pRow() + 0, 86, vValores.elementAt( 8 ).toString().equals( "C" ) ? "1" : "2" );
			imp.say( imp.pRow() + 0, 95, vValores.elementAt( 9 ).toString() );
			imp.say( imp.pRow() + 0, 110, vValores.elementAt( 10 ).toString() );

			String sTipoTran = vValores.elementAt( 11 ).toString();
			if ( sTipoTran == null )
				sTipoTran = "T";

			if ( sTipoTran.equals( "C" ) ) {
				imp.say( imp.pRow() + 0, 120, Funcoes.setMascara( vValores.elementAt( 12 ).toString() != null ? vValores.elementAt( 12 ).toString() : "", "##.###.###/####-##" ) );

			}
			else {
				imp.say( imp.pRow() + 0, 120, Funcoes.setMascara( vValores.elementAt( 13 ).toString() != null ? vValores.elementAt( 13 ).toString() : "", "##.###.###/####-##" ) );
			}
			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			// imp.say(imp.pRow()+0,2,Funcoes.copy(vValores.elementAt(11).toString(),0,42)+", "+Funcoes.copy(vValores.elementAt(12).toString(),0,6));

			imp.say( imp.pRow() + 1, 0, "" );
			imp.say( imp.pRow() + 0, 11, vValores.elementAt( 14 ).toString().trim() );
			// imp.say(imp.pRow()+0,50,vValores.elementAt(15).toString());
			imp.say( imp.pRow() + 0, 76, vValores.elementAt( 16 ).toString().trim() );
			imp.say( imp.pRow() + 0, 109, vValores.elementAt( 17 ).toString().trim() );
			imp.say( imp.pRow() + 0, 124, vValores.elementAt( 18 ).toString() );

			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );

			imp.say( imp.pRow() + 0, 11, vValores.elementAt( 19 ).toString() );
			imp.say( imp.pRow() + 0, 28, vValores.elementAt( 20 ).toString() );
			imp.say( imp.pRow() + 0, 51, vValores.elementAt( 21 ).toString() );
			imp.say( imp.pRow() + 0, 106, vValores.elementAt( 22 ).toString() );
			imp.say( imp.pRow() + 0, 130, vValores.elementAt( 23 ).toString() );

			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 0, 2, "" );
			imp.say( imp.pRow() + 1, 0, "" );
			imp.say( imp.pRow() + 1, 0, "" );
			imp.say( imp.pRow() + 1, 0, "" );
			imp.say( imp.pRow() + 1, 0, "" );
			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 1, 0, "" );
			imp.say( imp.pRow() + 1, 0, "" );
			// imp.say(imp.pRow()+0,124,sNumNota);

			imp.setPrc( 0, 0 );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

}
