/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)ImprimeOrc.java <BR>
 * 
 *                     Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                     modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                     na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                     Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                     sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                     Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                     Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                     de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                     Classe para impressão de orçamento padrão módulo standard.
 * 
 */

package org.freedom.modulos.std.view.frame.utility;

import java.awt.BasicStroke;
import java.awt.Font;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import javax.swing.ImageIcon;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.ImprimeLayout;
import org.freedom.library.swing.frame.Aplicativo;

public class ImprimeOrc extends ImprimeLayout {

	private static final long serialVersionUID = 1L;

	private DbConnection con = null;

	private int iCodOrc = 0;

	private Font fnTopEmp = new Font( "Arial", Font.BOLD, 9 );

	private Font fnCabEmp = new Font( "Arial", Font.PLAIN, 8 );

	private Font fnCabEmpNeg = new Font( "Arial", Font.BOLD, 8 );

	private Font fnCabCli = new Font( "Arial", Font.PLAIN, 8 );

	private Font fnCabCliNeg = new Font( "Arial", Font.BOLD, 8 );

	private boolean bImpCab = true;

	private boolean[] pref = null;

	private int iResto = 0;

	public ImprimeOrc( int iCodO ) {

		iCodOrc = iCodO;
	}

	public void montaG() {

		montaRel();
	}

	private int impLabel( String sTexto, int iSalto, int iMargem, int iLargura, int iY ) {

		double iPixels = getFontMetrics( fnCabCli ).stringWidth( sTexto );
		double iNLinhas = iPixels / iLargura;
		int iNCaracteres = Funcoes.tiraChar( sTexto, "\n" ).length();
		int iNCaracPorLinha = (int) ( iNCaracteres / iNLinhas );
		Vector<?> vTextoSilabas = Funcoes.strToVectorSilabas( sTexto, iNCaracPorLinha );

		for ( int i = 0; vTextoSilabas.size() > i; i++ ) {
			drawTexto( vTextoSilabas.elementAt( i ).toString(), iMargem, iY );
			iY += iSalto;
		}
		return iY;
	}

	private void montaRel() {

		pref = getPref();

		try {

			StringBuilder sSQL = new StringBuilder();
			sSQL.append( "SELECT " );
			sSQL.append( "(SELECT COUNT(IO.CODITORC) FROM VDITORCAMENTO IO WHERE IO.CODEMP=O.CODEMP" );
			sSQL.append( " AND IO.CODFILIAL=O.CODFILIAL AND IO.CODORC=O.CODORC)," );
			sSQL.append( "IT.VLRPRODITORC/IT.QTDITORC," );
			sSQL.append( ( pref[ 0 ] ? "IT.REFPROD," : "IT.CODPROD," ) );
			sSQL.append( " O.VLRDESCITORC, O.CODORC,O.DTORC,O.DTVENCORC," );
			sSQL.append( " O.OBSORC,O.VLRLIQORC,P.CODBARPROD, P.DESCPROD,IT.QTDITORC,IT.VLRDESCITORC,IT.VLRLIQITORC," );
			sSQL.append( " O.VLRDESCORC,  O.VLRADICORC, O.VLRPRODORC, M.NOMEMUNIC, CL.CODCLI," );
			sSQL.append( " CL.RAZCLI,CL.ENDCLI,CL.NUMCLI,CL.BAIRCLI,CL.CIDCLI,CL.UFCLI,CL.DDDCLI," );
			sSQL.append( "CL.FONECLI,IT.OBSITORC,PL.DESCPLANOPAG,VD.NOMEVEND,CL.CONTCLI,O.PRAZOENTORC,O.CODVEND " );
			sSQL.append( " FROM VDORCAMENTO O, VDITORCAMENTO IT, EQPRODUTO P, SGFILIAL F, VDCLIENTE CL,FNPLANOPAG PL,VDVENDEDOR VD, SGMUNICIPIO M" );
			sSQL.append( " WHERE IT.CODORC=O.CODORC AND IT.CODEMP=O.CODEMP AND IT.CODFILIAL=O.CODFILIAL AND IT.TIPOORC=O.TIPOORC" );
			sSQL.append( " AND P.CODPROD=IT.CODPROD AND P.CODEMP=IT.CODEMPPD AND P.CODFILIAL=IT.CODFILIALPD" );
			sSQL.append( " AND F.CODEMP=O.CODEMP AND F.CODFILIAL=O.CODFILIAL" );
			sSQL.append( " AND O.CODPLANOPAG=PL.CODPLANOPAG AND O.CODFILIALPG=PL.CODFILIAL AND O.CODEMP=PL.CODEMP" );
			sSQL.append( " AND O.CODVEND=VD.CODVEND AND O.CODFILIALVD=VD.CODFILIAL AND O.CODEMPVD=VD.CODEMP" );
			sSQL.append( " AND O.TIPOORC = 'O' AND O.CODORC=? AND O.CODEMP=? AND O.CODFILIAL=?" );
			sSQL.append( " AND CL.CODEMP=O.CODEMP AND CL.CODFILIAL=O.CODFILIAL AND CL.CODCLI=O.CODCLI " );
			sSQL.append( " AND F.CODMUNIC=M.CODMUNIC AND F.CODPAIS=M.CODPAIS AND F.SIGLAUF=M.SIGLAUF " );
			sSQL.append( " ORDER BY IT.CODORC,IT.CODITORC" );

			PreparedStatement ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, iCodOrc );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "VDORCAMENTO" ) );
			ResultSet rs = ps.executeQuery();
			int iY = 245;
			for ( int i = 1; rs.next(); i++ ) {
				if ( bImpCab ) {
					montaCab( rs );
					iY = 220 + iResto;
					bImpCab = false;
					setFonte( fnCabEmp );
				}

				drawTexto( rs.getString( 3 ), 5, iY );
				drawTexto( Funcoes.setMascara( rs.getString( "CODBARPROD" ) != null ? rs.getString( "CODBARPROD" ).trim() : "", "##.###.##-######" ), 55, iY );

				String sVal0 = Funcoes.strDecimalToStrCurrency( 2, rs.getInt( "QtdItOrc" ) + "" );
				String sVal1 = Funcoes.strDecimalToStrCurrency( 2, rs.getString( 2 ) );
				String sVal2 = Funcoes.strDecimalToStrCurrency( 2, rs.getString( "VlrDescItOrc" ) );
				String sVal3 = Funcoes.strDecimalToStrCurrency( 2, rs.getString( "VlrLiqItOrc" ) );

				drawTexto( sVal0, 350, iY, getFontMetrics( fnCabEmp ).stringWidth( sVal0 ), AL_DIR );
				drawTexto( sVal1, 400, iY, getFontMetrics( fnCabEmp ).stringWidth( sVal1 ), AL_DIR );
				drawTexto( sVal2, 470, iY, getFontMetrics( fnCabEmp ).stringWidth( sVal2 ), AL_DIR );
				drawTexto( sVal3, 540, iY, getFontMetrics( fnCabEmp ).stringWidth( sVal3 ), AL_DIR );

				int iY2 = iY;
				if ( rs.getString( "ObsItOrc" ) != null ) {
					setFonte( fnCabCliNeg );
					iY = impLabel( rs.getString( "DescProd" ).trim(), 10, 110, 200, iY );
					setFonte( fnCabCli );
					iY2 = impLabel( rs.getString( "ObsItOrc" ), 10, 110, 200, iY + 3 ) - 5;
				}
				else {
					setFonte( fnCabCliNeg );
					iY2 = impLabel( rs.getString( "DescProd" ).trim(), 10, 110, 200, iY );
					setFonte( fnCabCli );
				}

				iY = iY2 + 15;

				if ( i >= rs.getInt( 1 ) ) {
					// Por ser o ultimo item termina tudo!!!
					montaTot( rs, true );
					setBordaRel();
					termPagina();
				}
				else if ( iY > 605 ) {
					// Se estourou o limite de linhas, pula pra outra pagina:
					iY = 230;
					montaTot( rs, false );
					setBordaRel();
					termPagina();
					bImpCab = true;
				}
			}

			// Fecha a conta e passa a regua:
			finaliza();
			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao montar o relatório!!!\n" + err.getMessage(), true, con, err );
		}
	}

	private void montaCab( ResultSet rs ) {

		int iY = 35;
		try {
			montaCabEmp( con );

			drawLinha( 0, iY, 0, 0, AL_LL );

			iY += 20;

			setFonte( fnTopEmp );
			drawTexto( "ORÇAMENTO", 0, iY, getFontMetrics( fnCabEmp ).stringWidth( "ORÇAMENTO" ), AL_CEN );
			setFonte( fnCabEmpNeg );

			iY += 10;

			drawLinha( 0, iY, 0, 0, AL_LL );

			iY += 10;

			setFonte( fnCabEmp );
			drawTexto( "Orçamento:   ", 5, iY );
			setFonte( fnCabEmpNeg );
			drawTexto( StringFunctions.strZero( "" + iCodOrc, 8 ), 70, iY );
			setFonte( fnCabEmp );
			drawTexto( "Data de emissão: ", 200, iY );
			drawTexto( StringFunctions.sqlDateToStrDate( rs.getDate( "DtOrc" ) ), 300, iY );

			setFonte( fnCabEmp );
			drawTexto( "Validade: ", 380, iY );
			if ( pref[ 1 ] )
				drawTexto( StringFunctions.sqlDateToStrDate( rs.getDate( "DtVencOrc" ) ), 480, iY );
			else {
				Date dtOrc = rs.getDate( "DtOrc" );
				Date dtVal = rs.getDate( "DtVencOrc" );
				long nDias = Funcoes.getNumDiasAbs( dtOrc, dtVal );
				String sDias = "";
				if ( nDias != 1 )
					sDias = " dias.";
				else {
					sDias = " dia.";
				}
				drawTexto( "" + nDias + sDias, 480, iY );
			}

			iY += 5;

			drawLinha( 0, iY, 0, 0, AL_LL );

			iY += 10;

			setFonte( fnCabCliNeg );
			drawTexto( "Cliente:", 5, iY );
			setFonte( fnCabCli );
			drawTexto( rs.getString( "CodCli" ) + " - " + rs.getString( "RazCli" ).trim(), 80, iY );
			setFonte( fnCabCliNeg );
			drawTexto( "A/C Sr(a):", 378, iY );
			setFonte( fnCabCli );
			drawTexto( rs.getString( "ContCli" ), 440, iY );

			iY += 15;
			setFonte( fnCabCliNeg );
			drawTexto( "Cond.Pagto.:", 378, iY );
			setFonte( fnCabCli );

			impLabel( rs.getString( "DescPlanoPag" ), 10, 452, 110, iY );

			// drawTexto("A/C Sr(a):",378,iY);
			// setFonte(fnCabCli);
			// drawTexto(rs.getString("ContCli"),440,iY);

			setFonte( fnCabCliNeg );
			drawTexto( "Cidade:", 5, iY );
			setFonte( fnCabCli );
			drawTexto( rs.getString( "CidCli" ) != null ? rs.getString( "CidCli" ).trim() + " / " + rs.getString( "UFCli" ) : "", 80, iY );

			System.out.print( "y no cliente" + iY );
			iY += 15;

			setFonte( fnCabCliNeg );
			drawTexto( "Endereço:", 5, iY );
			setFonte( fnCabCli );
			drawTexto( rs.getString( "EndCli" ) != null ? ( rs.getString( "EndCli" ).trim() + ", " + rs.getInt( "NumCli" ) ) : "", 80, iY );

			iY += 15;

			setFonte( fnCabCliNeg );
			drawTexto( "Bairro:", 5, iY );
			setFonte( fnCabCli );
			drawTexto( rs.getString( "BairCli" ), 80, iY );

			iY += 15;

			setFonte( fnCabCliNeg );
			drawTexto( "Telefone:", 5, iY );
			setFonte( fnCabCli );
			drawTexto( ( rs.getString( "DDDCli" ) != null ? "(" + rs.getString( "DDDCli" ) + ")" : "" ) + ( rs.getString( "FoneCli" ) != null ? Funcoes.setMascara( rs.getString( "FoneCli" ).trim(), "####-####" ) : "" ), 80, iY );

			iY += 15;

			setFonte( fnCabCliNeg );
			drawTexto( "Observação:", 5, iY );
			setFonte( fnCabCli );
			String[] sObs = Funcoes.strToStrArray( rs.getString( "ObsOrc" ), 2 );
			drawTexto( sObs[ 0 ], 80, iY );

			iY += 15;

			drawTexto( sObs[ 1 ], 80, iY );
			setFonte( fnCabCliNeg );

			drawTexto( "Prazo de Entrega:", 378, 120 );

			setFonte( fnCabCli );
			if ( rs.getString( "PRAZOENTORC" ) == null )
				drawTexto( "À Combinar.", 470, 120 );
			else
				drawTexto( rs.getString( "PRAZOENTORC" ) + " dias.", 470, 120 );
			iY += 5;

			setPincel( new BasicStroke( 2 ) );
			drawLinha( 0, iY, 0, 0, AL_LL );
			setPincel( new BasicStroke( 1 ) );
			drawLinha( 50, iY, 50, 610 );
			drawLinha( 108, iY, 108, 610 );
			drawLinha( 328, iY, 328, 610 );
			drawLinha( 365, iY, 365, 610 );
			drawLinha( 420, iY, 420, 610 );
			drawLinha( 490, iY, 490, 610 );

			iY += 15;

			setFonte( fnCabCliNeg );
			drawTexto( "Cod.", 5, iY );
			drawTexto( "Cod.Bar.", 55, iY );
			drawTexto( "Descrição", 110, iY );
			drawTexto( "Qtd.", 332, iY );
			drawTexto( "Vlr. Unit.", 373, iY );
			drawTexto( "Vlr. Desc.", 430, iY );
			drawTexto( "Vlr. Total.", 498, iY );

			iY += 5;

			drawLinha( 0, iY, 0, 0, AL_LL );

			iY += 405; // tamanho reservado para itens.

			drawLinha( 0, iY, 0, 0, AL_LL );
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao montar o cabeçalho do relatório!\n" + err.getMessage(), true, con, err );
		}
	}

	private void montaTot( ResultSet rs, boolean bValido ) {

		int iY = 610;

		setFont( fnCabEmp );

		try {
			if ( bValido ) {
				double dValAdic = rs.getDouble( "VlrAdicOrc" );
				double dValDesc = rs.getDouble( "VlrDescOrc" );
				dValDesc += rs.getDouble( 4 ); // Desconto por item.
				if ( dValDesc > 0 || dValAdic > 0 ) {
					String sValParc = Funcoes.strDecimalToStrCurrency( 2, rs.getString( "VlrProdOrc" ) );
					drawTexto( "Total parc.:", 410, iY + 10 );
					drawRetangulo( 490, iY, 0, 15, AL_BDIR );
					drawTexto( sValParc, 545, iY + 10, getFontMetrics( fnCabEmp ).stringWidth( sValParc ), AL_DIR );
					iY += 15;
					if ( dValDesc > 0 ) {
						String sValDesc = Funcoes.strDecimalToStrCurrency( 2, dValDesc + "" );
						drawTexto( "Desconto:", 410, iY + 10 );
						drawRetangulo( 490, iY, 0, 15, AL_BDIR );
						drawTexto( sValDesc, 545, iY + 10, getFontMetrics( fnCabEmp ).stringWidth( sValDesc ), AL_DIR );
						iY += 15;
					}
					if ( dValAdic > 0 ) {
						String sValAdic = Funcoes.strDecimalToStrCurrency( 2, dValAdic + "" );
						drawTexto( "Adicional:", 410, iY + 10 );
						drawRetangulo( 490, iY, 0, 15, AL_BDIR );
						drawTexto( sValAdic, 545, iY + 10, getFontMetrics( fnCabEmp ).stringWidth( sValAdic ), AL_DIR );
						iY += 15;
					}
				}
				String sValLiq = Funcoes.strDecimalToStrCurrency( 2, rs.getString( "VlrLiqOrc" ) );
				drawTexto( "Total liq.:", 420, iY + 10 );
				drawRetangulo( 490, iY, 0, 15, AL_BDIR );
				drawTexto( sValLiq, 540, iY + 10, getFontMetrics( fnCabEmp ).stringWidth( sValLiq ), AL_DIR );
			}
			else {// Caso ainda não tenha terminado o relatório não imprime valores e coloca 'x':
				String sValLiq = "xxxxxxxxxx";
				drawTexto( "Total liq.:", 410, iY + 10 );
				drawRetangulo( 490, iY, 0, 15, AL_BDIR );
				drawTexto( sValLiq, 550, iY + 10, getFontMetrics( fnCabEmp ).stringWidth( sValLiq ), AL_DIR );
			}

			// Linha de assinatura:

			drawTexto( rs.getString( "CidFilial" ).trim() + ", " + Funcoes.dateToStrExtenso( new Date() ), 70, 645 );

			if ( pref[ 2 ] ) {
				ImageIcon assinatura = getAssinatura( rs.getInt( "CODVEND" ) );
				if ( assinatura != null ) {
					int altura = 70;
					int largura = ( assinatura.getIconWidth() * altura ) / assinatura.getIconHeight();
					drawImagem( assinatura, 70, 650, largura, altura );
				}
			}

			drawLinha( 70, 720, 200, 720 );
			setFonte( fnCabCliNeg );
			drawTexto( rs.getString( "NomeVend" ), 90, 730 );

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao montar totalizadores do relatório!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	private ImageIcon getAssinatura( int codvend ) {

		ImageIcon assinatura = null;
		String sSQL = "SELECT IMGASSVEND FROM VDVENDEDOR WHERE CODEMP=? AND CODFILIAL=? AND CODVEND=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDVENDEDOR" ) );
			ps.setInt( 3, codvend );
			rs = ps.executeQuery();
			if ( rs.next() ) {

				byte[] bVals = new byte[ 650000 ];
				Blob bVal = rs.getBlob( "IMGASSVEND" );
				if ( bVal != null ) {
					bVal.getBinaryStream().read( bVals, 0, bVals.length );
					assinatura = new ImageIcon( bVals );
				}
			}
			rs.close();
			ps.close();
		} catch ( Exception err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar assinatura do vendedor!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}

		return assinatura;
	}

	private boolean[] getPref() {

		boolean bRetorno[] = new boolean[ 3 ];
		String sSQL = "SELECT USAREFPROD,TIPOVALIDORC,USAIMGASSORC FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				bRetorno[ 0 ] = "S".equals( rs.getString( "USAREFPROD" ).trim() );
				bRetorno[ 1 ] = "D".equals( rs.getString( "TIPOVALIDORC" ).trim() );
				bRetorno[ 2 ] = "S".equals( rs.getString( "USAIMGASSORC" ).trim() );
			}
			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage(), true, con, err );
		}
		return bRetorno;
	}

	public void setConexao( DbConnection cn ) {

		con = cn;
	}
}
