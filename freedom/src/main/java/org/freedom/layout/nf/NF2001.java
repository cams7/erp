/**
 * @version 10/12/2003 <BR>
 * @author Setpoint Informática Ltda./Alex Rodrigues <BR>
 * 
 *         Projeto: Freedom <BR>
 *         Pacote: leiautes <BR>
 *         Classe: @(#)NFBuzzi2.java <BR>
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.business.component.NF;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.component.Layout;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.frame.Aplicativo;

public class NF2001 extends Layout {

	private String sMensAdic = "";

	private String sNumNota = "";

	private NF nf = null;

	public boolean imprimir( NF nf, ImprimeOS imp ) {

		boolean retorno = super.imprimir( nf, imp );
		Calendar cHora = Calendar.getInstance();
		Vector<String> vValores = new Vector<String>();
		Vector<String> vClfisc = new Vector<String>();
		Vector<String> vSigla = new Vector<String>();
		Vector<?> vDesc = null;
		String sDesc = null;
		String sCodfisc = null;
		String sSigla = null;
		String sTipoTran = null;
		String sHora = null;
		String[] sNat = new String[ 4 ];
		String[] sVencs = new String[ 4 ];
		String[] sVals = new String[ 4 ];
		String[] sDuplics = new String[ 4 ];
		String[] sDadosEmp = null;
		int iNumNota = 0;
		int iProd = 0;
		boolean bMensage = false;
		boolean bFat = true;
		boolean bTotalizou = false;
		boolean bjatem = false;
		boolean bNat = true;

		try {

			this.nf = nf;

			sDadosEmp = getDadosEmp();

			imp.limpaPags();
			vClfisc.addElement( "" );

			sHora = StringFunctions.strZero( String.valueOf( cHora.get( Calendar.HOUR_OF_DAY ) ), 2 ) + ":" + StringFunctions.strZero( String.valueOf( cHora.get( Calendar.MINUTE ) ), 2 ) + ":" + StringFunctions.strZero( String.valueOf( cHora.get( Calendar.SECOND ) ), 2 );

			if ( cab.next() )
				sNumNota = String.valueOf( cab.getInt( NF.C_DOC ) );
			else
				sNumNota = StringFunctions.strZero( String.valueOf( iNumNota ), 6 );

			for ( int i = 0; i < 4; i++ ) {
				if ( bFat ) {
					if ( parc.next() ) {
						sDuplics[ i ] = sNumNota + "/" + parc.getInt( NF.C_NPARCITREC );
						sVencs[ i ] = Funcoes.dateToStrDate( parc.getDate( NF.C_DTVENCTO ) );
						sVals[ i ] = Funcoes.strDecimalToStrCurrency( 12, 2, parc.getString( NF.C_VLRPARC ) );
					}
					else {
						bFat = false;
						sDuplics[ i ] = "********";
						sVencs[ i ] = "**********";
						sVals[ i ] = "************";
					}
				}
			}

			while ( itens.next() ) {

				if ( bNat ) {
					sNat[ 0 ] = itens.getString( NF.C_DESCNAT );
					sNat[ 1 ] = Funcoes.setMascara( itens.getString( NF.C_CODNAT ), "#.##" );
					bNat = false;
				}

				if ( imp.pRow() == 0 ) {
					imp.pulaLinha( 1, imp.comprimido() );

					if ( nf.getTipoNF() == NF.TPNF_ENTRADA )
						imp.say( 102, "X" );
					else
						imp.say( 90, "X" );

					imp.say( 128, sNumNota );
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 25, sDadosEmp[ 0 ] );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 25, sDadosEmp[ 1 ] + sDadosEmp[ 2 ] + ( sDadosEmp[ 3 ] != null ? " - " + Funcoes.setMascara( sDadosEmp[ 3 ], "(####)####-####" ) : "" ) + ( sDadosEmp[ 4 ] != null ? " - " + Funcoes.setMascara( sDadosEmp[ 4 ], "####-####" ) : "" ) );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 25, sDadosEmp[ 5 ] + ( sDadosEmp[ 8 ] != null ? "  -  " + Funcoes.setMascara( sDadosEmp[ 8 ], "##.###-###" ) : "" ) + ( sDadosEmp[ 6 ] != null ? "  -  " + sDadosEmp[ 6 ] : "" ) + sDadosEmp[ 7 ] );
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 6, sNat[ 0 ] );
					imp.say( 52, sNat[ 1 ] );
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 6, cab.getString( NF.C_RAZEMIT ) );
					imp.say( 90, !cab.getString( NF.C_CPFEMIT ).equals( "" ) ? Funcoes.setMascara( cab.getString( NF.C_CPFEMIT ), "###.###.###-##" ) : Funcoes.setMascara( cab.getString( NF.C_CNPJEMIT ), "##.###.###/####-##" ) );
					imp.say( 126, Funcoes.dateToStrDate( cab.getDate( NF.C_DTEMITPED ) ) );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 6, Funcoes.copy( cab.getString( NF.C_ENDEMIT ), 0, 50 ).trim() + ", " + ( !cab.getString( NF.C_NUMEMIT ).equals( "" ) ? Funcoes.copy( cab.getString( NF.C_NUMEMIT ), 0, 6 ).trim() : "" ).trim() + " - "
							+ ( !cab.getString( NF.C_COMPLEMIT ).equals( "" ) ? Funcoes.copy( cab.getString( NF.C_COMPLEMIT ), 0, 9 ).trim() : "" ).trim() );
					imp.say( 84, !cab.getString( NF.C_BAIREMIT ).equals( "" ) ? Funcoes.copy( cab.getString( NF.C_BAIREMIT ), 0, 20 ) : "" );
					imp.say( 107, Funcoes.setMascara( cab.getString( NF.C_CEPEMIT ), "#####-###" ) );
					imp.say( 126, Funcoes.dateToStrDate( cab.getDate( NF.C_DTSAIDA ) ) );
					imp.pulaLinha( 2, imp.comprimido() );
					imp.say( 6, cab.getString( NF.C_CIDEMIT ) );
					imp.say( 50, Funcoes.setMascara( cab.getString( NF.C_DDDEMIT ) + " ", "(####)" ) + Funcoes.setMascara( cab.getString( NF.C_FONEEMIT ).trim(), "####-####" ) );
					imp.say( 73, cab.getString( NF.C_UFEMIT ) );
					imp.say( 90, !cab.getString( NF.C_RGEMIT ).equals( "" ) ? cab.getString( NF.C_RGEMIT ) : cab.getString( NF.C_INSCEMIT ) );
					imp.say( 127, sHora );
					imp.pulaLinha( 3, imp.comprimido() );
					imp.say( 12, sDuplics[ 0 ] );
					imp.say( 28, sVals[ 0 ] );
					imp.say( 55, sVencs[ 0 ] );
					imp.say( 78, sDuplics[ 1 ] );
					imp.say( 102, sVals[ 1 ] );
					imp.say( 125, sVencs[ 1 ] );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 12, sDuplics[ 2 ] );
					imp.say( 28, sVals[ 2 ] );
					imp.say( 55, sVencs[ 2 ] );
					imp.say( 78, sDuplics[ 3 ] );
					imp.say( 102, sVals[ 3 ] );
					imp.say( 125, sVencs[ 3 ] );
					imp.pulaLinha( 3, imp.comprimido() );
				}

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 4, String.valueOf( itens.getInt( NF.C_CODPROD ) ) );
				vDesc = Funcoes.strToVectorSilabas( itens.getString( NF.C_OBSITPED ).equals( "" ) ? ( itens.getString( NF.C_DESCPROD ).trim() ) : itens.getString( NF.C_OBSITPED ), 50 );

				for ( int iConta = 0; ( ( iConta < 20 ) && ( vDesc.size() > iConta ) ); iConta++ ) {
					if ( !vDesc.elementAt( iConta ).toString().equals( "" ) )
						sDesc = vDesc.elementAt( iConta ).toString();
					else
						sDesc = "";

					if ( iConta > 0 )
						imp.pulaLinha( 1, imp.comprimido() );

					imp.say( 14, sDesc );
					iProd = iProd + vDesc.size();

					sMensAdic = itens.getString( NF.C_DESCFISC ).trim();
				}

				sCodfisc = itens.getString( NF.C_CODFISC );

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

				imp.say( 68, sSigla );
				imp.say( 73, Funcoes.copy( itens.getString( NF.C_ORIGFISC ), 0, 1 ) + Funcoes.copy( itens.getString( NF.C_CODTRATTRIB ), 0, 2 ) );
				imp.say( 79, Funcoes.copy( itens.getString( NF.C_CODUNID ), 4 ) );
				imp.say( 82, Funcoes.alinhaDir( String.valueOf( itens.getFloat( NF.C_QTDITPED ) ), 8 ) );
				imp.say( 92, Funcoes.strDecimalToStrCurrency( 12, 2, String.valueOf( ( ( new BigDecimal( itens.getFloat( NF.C_VLRLIQITPED ) ) ).divide( new BigDecimal( itens.getFloat( NF.C_QTDITPED ) ), 2, BigDecimal.ROUND_HALF_UP ) ) ) ) );
				imp.say( 107, Funcoes.strDecimalToStrCurrency( 13, 2, String.valueOf( itens.getFloat( NF.C_VLRLIQITPED ) ) ) );
				imp.say( 123, String.valueOf( itens.getFloat( NF.C_PERCICMSITPED ) ) );
				imp.say( 127, String.valueOf( itens.getFloat( NF.C_PERCIPIITPED ) ) );
				imp.say( 133, Funcoes.strDecimalToStrCurrency( 5, 2, itens.getFloat( NF.C_VLRIPIITPED ) + "" ) );

				if ( ( iProd + 2 ) >= 25 ) {
					bMensage = true;
					break;
				}

				if ( !bTotalizou ) {
					frete.next();

					sTipoTran = frete.getString( NF.C_TIPOTRANSP );
					vValores.addElement( String.valueOf( cab.getFloat( NF.C_VLRBASEICMSPED ) ) ); // 0
					vValores.addElement( String.valueOf( cab.getFloat( NF.C_VLRICMSPED ) ) ); // 1
					vValores.addElement( String.valueOf( cab.getFloat( NF.C_VLRLIQPED ) ) ); // 2
					vValores.addElement( String.valueOf( frete.getFloat( NF.C_VLRFRETEPED ) ) );// 3
					vValores.addElement( String.valueOf( itens.getFloat( NF.C_VLRADICITPED ) ) );// 4
					vValores.addElement( String.valueOf( cab.getFloat( NF.C_VLRIPIPED ) ) );// 5
					vValores.addElement( String.valueOf( ( cab.getFloat( NF.C_VLRLIQPED ) - frete.getFloat( NF.C_VLRFRETEPED ) + itens.getFloat( NF.C_VLRADICITPED ) - itens.getFloat( NF.C_VLRIPIPED ) ) ) );// 6
					vValores.addElement( frete.getString( NF.C_RAZTRANSP ) );// 7
					vValores.addElement( frete.getString( NF.C_TIPOFRETE ) );// 8
					vValores.addElement( frete.getString( NF.C_PLACAFRETE ) );// 9
					vValores.addElement( frete.getString( NF.C_UFFRETE ) );// 10
					vValores.addElement( sTipoTran );// 11
					vValores.addElement( cab.getString( NF.C_CNPJEMIT ) );// 12
					vValores.addElement( frete.getString( NF.C_CNPJTRANSP ) );// 13
					vValores.addElement( frete.getString( NF.C_ENDTRANSP ) );// 14

					if ( sTipoTran.equals( "C" ) ) {
						vValores.addElement( "" );// 15
						vValores.addElement( "" );// 16
						vValores.addElement( "" );// 17
						vValores.addElement( "" );// 18
					}
					else {
						vValores.addElement( String.valueOf( frete.getInt( NF.C_NUMTRANSP ) ) );// 15
						vValores.addElement( frete.getString( NF.C_CIDTRANSP ) );// 16
						vValores.addElement( frete.getString( NF.C_UFTRANSP ) );// 17
						vValores.addElement( frete.getString( NF.C_INSCTRANSP ) ); // 18
					}

					vValores.addElement( String.valueOf( frete.getFloat( NF.C_QTDFRETE ) ) );// 19
					vValores.addElement( frete.getString( NF.C_ESPFRETE ) );// 20
					vValores.addElement( frete.getString( NF.C_MARCAFRETE ) );// 21
					vValores.addElement( String.valueOf( frete.getFloat( NF.C_PESOBRUTO ) ) );// 22
					vValores.addElement( String.valueOf( frete.getFloat( NF.C_PESOLIQ ) ) );// 23
					vValores.addElement( String.valueOf( itens.getFloat( NF.C_VLRISSITPED ) ) );// 24
					vValores.addElement( String.valueOf( cab.getInt( NF.C_CODVEND ) ) );// 25

					if ( cab.getString( NF.C_NOMEVEND ).equals( "" ) )
						vValores.addElement( StringFunctions.replicate( " ", 25 ) ); // 26
					else
						vValores.addElement( cab.getString( NF.C_NOMEVEND ) + StringFunctions.replicate( " ", 25 - cab.getString( NF.C_NOMEVEND ).length() ) );

					bTotalizou = true;
				}

			}

			impTotais( imp, vValores, vSigla );

			imp.fechaGravacao();

			retorno = true;

			if ( bMensage ) {
				if ( Funcoes.mensagemConfirma( null, "Podem conter erros na disposição da nota.\n" + "A descrição dos produtos ultrapassa o limite na nota.\n" + "Deseja imprimir?" ) == JOptionPane.YES_NO_CANCEL_OPTION )
					retorno = false;
			}

		} catch ( Exception err ) {
			Funcoes.mensagemErro( null, "Erro ao montar Nota Fiscal!" + err.getMessage() );
			retorno = false;
		} finally {
			cHora = null;
			vValores = null;
			vClfisc = null;
			vSigla = null;
			vDesc = null;
			sDesc = null;
			sCodfisc = null;
			sSigla = null;
			sTipoTran = null;
			sHora = null;
			sNat = null;
			sVencs = null;
			sVals = null;
			sDuplics = null;
		}

		return retorno;
	}

	private void impTotais( ImprimeOS imp, Vector<String> vValores, Vector<String> vSigla ) {

		Vector<?> vObs = null;

		try {

			imp.pulaLinha( 47 - imp.pRow(), imp.comprimido() );

			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 6, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 0 ).toString() ) );
			imp.say( 35, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 1 ).toString() ) );
			imp.say( 117, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 6 ).toString() ) );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 6, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 3 ).toString() ) );
			imp.say( 65, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 4 ).toString() ) );
			imp.say( 90, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 5 ).toString() ) );
			imp.say( 117, Funcoes.strDecimalToStrCurrency( 20, 2, vValores.elementAt( 2 ).toString() ) );
			imp.pulaLinha( 2, imp.comprimido() );
			imp.say( 6, vValores.elementAt( 7 ) != null ? vValores.elementAt( 7 ).toString() : "" );
			imp.say( 88, vValores.elementAt( 8 ) != null ? ( vValores.elementAt( 8 ).toString().equals( "C" ) ? "1" : "2" ) : "" );
			imp.say( 92, vValores.elementAt( 9 ) != null ? vValores.elementAt( 9 ).toString() : "" );
			imp.say( 108, vValores.elementAt( 10 ) != null ? vValores.elementAt( 10 ).toString() : "" );

			if ( vValores.elementAt( 11 ).toString().equals( "C" ) )
				imp.say( 118, "" );
			else
				imp.say( 118, Funcoes.setMascara( vValores.elementAt( 13 ) != null ? vValores.elementAt( 13 ).toString() : "", "##.###.###/####-##" ) );

			imp.pulaLinha( 2, imp.comprimido() );

			imp.say( 6, vValores.elementAt( 14 ) != null ? vValores.elementAt( 14 ).toString().trim() : "" );
			imp.say( 72, vValores.elementAt( 16 ) != null ? vValores.elementAt( 16 ).toString().trim() : "" );
			imp.say( 106, vValores.elementAt( 17 ) != null ? vValores.elementAt( 17 ).toString().trim() : "" );
			imp.say( 117, vValores.elementAt( 18 ) != null ? vValores.elementAt( 18 ).toString() : "" );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 6, vValores.elementAt( 19 ) != null ? vValores.elementAt( 19 ).toString() : "" );
			imp.say( 20, vValores.elementAt( 20 ) != null ? vValores.elementAt( 20 ).toString() : "" );
			imp.say( 42, vValores.elementAt( 21 ) != null ? vValores.elementAt( 21 ).toString() : "" );
			imp.say( 105, vValores.elementAt( 22 ) != null ? vValores.elementAt( 22 ).toString() : "" );
			imp.say( 128, vValores.elementAt( 23 ) != null ? vValores.elementAt( 23 ).toString() : "" );
			imp.pulaLinha( 1, imp.comprimido() );

			int pos = 1;
			for ( int i = 0; i < vSigla.size(); i++ ) {
				if ( pos == 1 ) {
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 6, vSigla.elementAt( i ) );
					pos = 2;
				}
				else if ( pos == 2 ) {
					imp.say( 28, vSigla.elementAt( i ) );
					pos = 3;
				}
				else if ( pos == 3 ) {
					imp.say( 50, vSigla.elementAt( i ) );
					pos = 1;
				}
			}

			vObs = Funcoes.quebraLinha( Funcoes.stringToVector( sMensAdic ), 80 );
			for ( int i = 0; i < vObs.size(); i++ ) {
				if ( imp.pRow() < 64 ) {
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 6, vObs.elementAt( i ).toString() );
				}
			}

			for ( int i = 0; ( imp.pRow() < 66 ); i++ )
				imp.pulaLinha( 1, imp.comprimido() );

			imp.say( 128, sNumNota );

			imp.setPrc( 0, 0 );

		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			vObs = null;
		}
	}

	private String[] getDadosEmp() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;
		String[] retorno = null;

		try {

			sSQL = "SELECT RAZFILIAL, ENDFILIAL, NUMFILIAL, FONEFILIAL, FAXFILIAL, " + "BAIRFILIAL, CIDFILIAL, UFFILIAL, CEPFILIAL " + "FROM SGFILIAL " + "WHERE CODEMP=? AND CODFILIAL=?";
			ps = nf.getConexao().prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			rs = ps.executeQuery();

			if ( rs.next() ) {
				retorno = new String[] { rs.getString( 1 ).trim(), rs.getString( 2 ).trim(), rs.getString( 3 ).trim(), rs.getString( 4 ).trim(), rs.getString( 5 ).trim(), rs.getString( 6 ).trim(), rs.getString( 7 ).trim(), rs.getString( 8 ).trim(), rs.getString( 9 ).trim() };

			}

		} catch ( SQLException e ) {
			Funcoes.mensagemErro( null, "Erro ao buscar dados da empresa.\n" + e.getMessage(), true, nf.getConexao(), e );
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}

		return retorno;
	}
}
