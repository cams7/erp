/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.atd <BR>
 *         Classe: @(#)ImprimeOrc.java <BR>
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
 *         Classe para impressão de orçamento padrão módulo atendimento.
 * 
 */

package org.freedom.modulos.atd.view.frame.report;

import java.awt.BasicStroke;
import java.awt.Font;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.ImprimeLayout;
import org.freedom.library.swing.frame.Aplicativo;

@ Deprecated
public class ImprimeOrc extends ImprimeLayout {

	private static final long serialVersionUID = 1L;

	private DbConnection con = null;

	private int iCodOrc = 0;

	private Font fnTopEmp = new Font( "Arial", Font.BOLD, 11 );

	private Font fnCabEmp = new Font( "Arial", Font.PLAIN, 8 );

	private Font fnCabEmpNeg = new Font( "Arial", Font.BOLD, 8 );

	private Font fnCabCli = new Font( "Arial", Font.PLAIN, 10 );

	private Font fnCabCliNeg = new Font( "Arial", Font.BOLD, 10 );

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

		boolean bImpCab = true;
		try {
			String sSQL = "SELECT " + "(SELECT COUNT(IO.CODITORC) FROM VDITORCAMENTO IO WHERE IO.CODEMP=O.CODEMP" + " AND IO.CODFILIAL=O.CODFILIAL AND IO.CODORC=O.CODORC)," + "(SELECT A.NOMEATEND FROM ATATENDENTE A WHERE A.CODATEND=O.CODATEND"
					+ " AND A.CODEMP=O.CODEMPAE AND A.CODFILIAL=O.CODFILIALAE)," + "IT.VLRPRODITORC/IT.QTDITORC," + "(SELECT ATM.DOCATENDO FROM ATATENDIMENTO  ATM, ATATENDIMENTOORC AC WHERE " + "ATM.CODEMP=AC.CODEMP AND ATM.CODFILIAL=AC.CODFILIAL AND ATM.CODATENDO=AC.CODATENDO"
					+ " AND O.CODEMP=AC.CODEMPOC AND O.CODFILIAL=AC.CODFILIALOC AND O.CODORC=AC.CODORC " + "AND O.TIPOORC=AC.TIPOORC)," + ( comRef() ? "IT.REFPROD," : "IT.CODPROD," )
					+ "O.VLRDESCITORC, O.CODORC,C.CODCONV,C.NOMECONV,O.DTORC,O.DTVENCORC,C.CIDCONV,C.UFCONV,C.ENDCONV,C.BAIRCONV,C.FONECONV," + " T.DESCTPCONV,O.OBSORC,O.VLRLIQORC,P.CODBARPROD, P.DESCPROD,IT.QTDITORC,IT.VLRDESCITORC,IT.VLRLIQITORC,"
					+ " O.VLRDESCORC,  O.VLRADICORC, O.VLRPRODORC, F.CIDFILIAL, CL.RAZCLI,CL.CIDCLI,C.NUMCONV, " + "(SELECT NOMEENC FROM ATENCAMINHADOR EC WHERE EC.CODEMP=C.CODEMPEC AND EC.CODFILIAL=C.CODFILIAL AND EC.CODENC=C.CODENC),"
					+ "(SELECT CIDENC FROM ATENCAMINHADOR EC WHERE EC.CODEMP=C.CODEMPEC AND EC.CODFILIAL=C.CODFILIAL AND EC.CODENC=C.CODENC)" + " FROM VDORCAMENTO O, ATCONVENIADO C, ATTIPOCONV T, VDITORCAMENTO IT, EQPRODUTO P, SGFILIAL F, VDCLIENTE CL,FNPLANOPAG PL "
					+ " WHERE T.CODEMP=C.CODEMPTC AND T.CODFILIAL=C.CODFILIALTC AND T.CODTPCONV=C.CODTPCONV" + " AND IT.CODORC=O.CODORC AND IT.CODEMP=O.CODEMP AND IT.CODFILIAL=O.CODFILIAL" + " AND P.CODPROD=IT.CODPROD AND P.CODEMP=IT.CODEMPPD AND P.CODFILIAL=IT.CODFILIALPD"
					+ " AND O.CODPLANOPAG=PL.CODPLANOPAG AND O.CODFILIALPG=PL.CODFILIAL AND O.CODEMP=PL.CODEMP" + " AND C.CODCONV=O.CODCONV AND C.CODEMP=O.CODEMPCV AND C.CODFILIAL=O.CODFILIALCV" + " AND F.CODEMP=O.CODEMP AND F.CODFILIAL=O.CODFILIAL"
					+ " AND O.TIPOORC = 'O' AND O.CODORC=? AND O.CODEMP=? AND O.CODFILIAL=?" + " AND CL.CODEMP=O.CODEMP AND CL.CODFILIAL=O.CODFILIAL AND CL.CODCLI=C.CODCLI";

			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, iCodOrc );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "VDORCAMENTO" ) );
			ResultSet rs = ps.executeQuery();
			int iY = 245;
			for ( int i = 1; rs.next(); i++ ) {
				if ( bImpCab ) {
					iY = montaCab( rs ) + 30;
					bImpCab = false;
					setFonte( fnCabEmp );
				}

				drawTexto( rs.getString( 5 ), 5, iY );
				drawTexto( Funcoes.setMascara( rs.getString( "CODBARPROD" ) != null ? rs.getString( "CODBARPROD" ).trim() : "", "##.###.##-######" ), 55, iY );
				drawTexto( Funcoes.copy( rs.getString( "DescProd" ).trim(), 0, 42 ), 110, iY );

				String sVal0 = Funcoes.strDecimalToStrCurrency( 2, rs.getInt( "QtdItOrc" ) + "" );
				String sVal1 = Funcoes.strDecimalToStrCurrency( 2, rs.getString( 3 ) );
				String sVal2 = Funcoes.strDecimalToStrCurrency( 2, rs.getString( "VlrDescItOrc" ) );
				String sVal3 = Funcoes.strDecimalToStrCurrency( 2, rs.getString( "VlrLiqItOrc" ) );
				drawTexto( sVal0, 350, iY, getFontMetrics( fnCabEmp ).stringWidth( sVal0 ), AL_DIR );
				drawTexto( sVal1, 400, iY, getFontMetrics( fnCabEmp ).stringWidth( sVal1 ), AL_DIR );
				drawTexto( sVal2, 470, iY, getFontMetrics( fnCabEmp ).stringWidth( sVal2 ), AL_DIR );
				drawTexto( sVal3, 550, iY, getFontMetrics( fnCabEmp ).stringWidth( sVal3 ), AL_DIR );

				iY += 15;

				if ( i >= rs.getInt( 1 ) ) {
					// Por ser o ultimo item termina tudo!!!
					montaTot( rs, true );
					setBordaRel();
					termPagina();
				}
				else if ( iY > 610 ) {
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

	private int montaCab( ResultSet rs ) {

		int iY = 35;
		int iRetY = 0;
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
			drawTexto( StringFunctions.strZero( "" + iCodOrc, 8 ), 65, iY );
			setFonte( fnCabEmp );
			drawTexto( "Medida:", 140, iY );
			setFonte( fnCabEmp );
			drawTexto( "Data de emissão: ", 275, iY );
			setFonte( fnCabEmpNeg );
			drawTexto( StringFunctions.sqlDateToStrDate( rs.getDate( "DtOrc" ) ), 350, iY );
			setFonte( fnCabEmpNeg );
			drawTexto( StringFunctions.strZero( "" + ( rs.getInt( 4 ) ), 8 ), 190, iY );

			// drawTexto(rs.getString(4),190,iY);

			setFonte( fnCabEmp );
			drawTexto( "Data de validade: ", 440, iY );
			setFonte( fnCabEmpNeg );
			drawTexto( StringFunctions.sqlDateToStrDate( rs.getDate( "DtVencOrc" ) ), 515, iY );

			iY += 5;

			drawLinha( 0, iY, 0, 0, AL_LL );

			iY += 10;

			setFonte( fnCabCliNeg );
			drawTexto( "Cliente:", 5, iY );
			setFonte( fnCabCli );
			drawTexto( rs.getString( "CodConv" ) + " - " + rs.getString( "NomeConv" ).trim(), 80, iY );
			setFonte( fnCabCliNeg );
			if ( rs.getString( 32 ) != null ) {
				drawTexto( "Unidade Enc.:", 285, iY );
				setFonte( new Font( "Arial", Font.PLAIN, 10 ) );
				drawTexto( Funcoes.copy( ( rs.getString( 32 ) != null ? rs.getString( 32 ) : "" ).trim() + " " + ( rs.getString( 33 ) != null ? " / " + rs.getString( 33 ).trim() : "" ), 30 ), 380, iY );
			}
			// drawTexto((rs.getString(32) != null ? rs.getString(32) : "").trim()+" "+(rs.getString(33) != null ? " / "+rs.getString(33).trim():""),390,iY);
			// drawTexto(Funcoes.copy((rs.getString(32) != null ? rs.getString(32) : "").trim()+" "+(rs.getString(33) != null ? " / "+rs.getString(33).trim():""),20),390,iY);

			iY += 15;

			setFonte( fnCabCliNeg );
			drawTexto( "Cidade:", 5, iY );
			setFonte( fnCabCli );
			drawTexto( rs.getString( "CidConv" ) != null ? rs.getString( "CidConv" ).trim() + " / " + rs.getString( "UFConv" ) : "", 80, iY );

			iY += 15;

			setFonte( fnCabCliNeg );
			drawTexto( "Endereço:", 5, iY );
			setFonte( fnCabCli );
			// drawTexto(rs.getString("EndConv"),80,iY);
			drawTexto( rs.getString( "ENDCONV" ) != null ? rs.getString( "ENDCONV" ).trim() + ( rs.getString( "NUMCONV" ) != null ? ", " + rs.getString( "NUMCONV" ) : "" ) : "", 80, iY );

			iY += 15;

			setFonte( fnCabCliNeg );
			drawTexto( "Bairro:", 5, iY );
			setFonte( fnCabCli );
			drawTexto( rs.getString( "BairConv" ), 80, iY );

			iY += 15;

			setFonte( fnCabCliNeg );
			drawTexto( "Telefone:", 5, iY );
			setFonte( fnCabCli );
			drawTexto( Funcoes.setMascara( rs.getString( "FoneConv" ), "(####)####-####" ), 80, iY );

			iY += 15;

			setFonte( fnCabCliNeg );
			drawTexto( "Atendente:", 5, iY );
			setFonte( fnCabCli );
			drawTexto( rs.getString( 2 ), 80, iY );

			iY += 15;

			setFonte( fnCabCliNeg );
			drawTexto( "Convênio:", 5, iY );
			setFonte( fnCabCli );
			drawTexto( rs.getString( "DescTpConv" ).trim() + " / " + ( rs.getString( "RAZCLI" ) ).trim() + " / " + ( rs.getString( "CIDCLI" ) ).trim(), 80, iY );

			iY += 15;

			setFonte( fnCabCliNeg );
			drawTexto( "Observação:", 5, iY );
			setFonte( fnCabCli );

			iY = impLabel( ( rs.getString( "ObsOrc" ) == null ? "" : rs.getString( "ObsOrc" ) ), 10, 80, 460, iY );
			iRetY = iY;

			System.out.println( "Posicao ao acabar obs" + iY );

			setPincel( new BasicStroke( 2 ) );
			drawLinha( 0, iY, 0, 0, AL_LL );
			setPincel( new BasicStroke( 1 ) );
			drawLinha( 50, iY, 50, 610 );
			drawLinha( 108, iY, 108, 610 );
			drawLinha( 328, iY, 328, 610 );
			drawLinha( 365, iY, 365, 610 );
			drawLinha( 420, iY, 420, 610 );
			drawLinha( 490, iY, 490, 610 );

			iY += 10;

			setFonte( fnCabCliNeg );
			drawTexto( "Cod.", 5, iY );
			drawTexto( "Cod.Bar.", 55, iY );
			drawTexto( "Descrição", 110, iY );
			drawTexto( "Qtd.", 332, iY );
			drawTexto( "Vlr. Unit.", 375, iY );
			drawTexto( "Vlr. Desc.", 430, iY );
			drawTexto( "Vlr. Total.", 498, iY );

			iY += 5;

			drawLinha( 0, iY, 0, 0, AL_LL );

			System.out.println( "iY da linha Linha " + iY );
			drawLinha( 0, 610, 0, 0, AL_LL );
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao montar o cabeçalho do relatório!!!\n" + err.getMessage(), true, con, err );
		}
		return iRetY;
	}

	private void montaTot( ResultSet rs, boolean bValido ) {

		int iY = 610;

		setFont( fnCabEmp );

		try {
			if ( bValido ) {
				double dValAdic = rs.getDouble( "VlrAdicOrc" );
				double dValDesc = rs.getDouble( "VlrDescOrc" );
				dValDesc += rs.getDouble( 6 );
				if ( dValDesc > 0 || dValAdic > 0 ) {
					String sValParc = Funcoes.strDecimalToStrCurrency( 2, rs.getString( "VlrProdOrc" ) );
					drawTexto( "Total parc.:", 410, iY + 10 );
					drawRetangulo( 490, iY, 0, 15, AL_BDIR );
					drawTexto( sValParc, 550, iY + 10, getFontMetrics( fnCabEmp ).stringWidth( sValParc ), AL_DIR );
					iY += 15;
					if ( dValDesc > 0 ) {
						String sValDesc = Funcoes.strDecimalToStrCurrency( 2, dValDesc + "" );
						drawTexto( "Desconto:", 410, iY + 10 );
						drawRetangulo( 490, iY, 0, 15, AL_BDIR );
						drawTexto( sValDesc, 550, iY + 10, getFontMetrics( fnCabEmp ).stringWidth( sValDesc ), AL_DIR );
						iY += 15;
					}
					if ( dValAdic > 0 ) {
						String sValAdic = Funcoes.strDecimalToStrCurrency( 2, dValAdic + "" );
						drawTexto( "Adicional:", 410, iY + 10 );
						drawRetangulo( 490, iY, 0, 15, AL_BDIR );
						drawTexto( sValAdic, 550, iY + 10, getFontMetrics( fnCabEmp ).stringWidth( sValAdic ), AL_DIR );
						iY += 15;
					}
				}
				String sValLiq = Funcoes.strDecimalToStrCurrency( 2, rs.getString( "VlrLiqOrc" ) );
				drawTexto( "Total liq.:", 420, iY + 10 );
				drawRetangulo( 490, iY, 0, 15, AL_BDIR );
				drawTexto( sValLiq, 550, iY + 10, getFontMetrics( fnCabEmp ).stringWidth( sValLiq ), AL_DIR );
			}
			else {// Caso ainda não tenha terminado o relatório não imprime valores e coloca 'x':
				String sValLiq = "xxxxxxxxxx";
				drawTexto( "Total liq.:", 410, iY + 10 );
				drawRetangulo( 490, iY, 0, 15, AL_BDIR );
				drawTexto( sValLiq, 550, iY + 10, getFontMetrics( fnCabEmp ).stringWidth( sValLiq ), AL_DIR );
			}

			// Linha de assinatura:

			drawTexto( rs.getString( "CidFilial" ).trim() + ", " + Funcoes.dateToStrExtenso( new Date() ), 70, 650 );
			drawLinha( 70, 690, 200, 690 );
			drawTexto( "Assinatura", 120, 700 );
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao montar totalizadores do relatório!!!\n" + err.getMessage(), true, con, err );
		}
	}

	private boolean comRef() {

		boolean bRetorno = false;
		String sSQL = "SELECT USAREFPROD FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				if ( rs.getString( "UsaRefProd" ).trim().equals( "S" ) )
					bRetorno = true;
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
