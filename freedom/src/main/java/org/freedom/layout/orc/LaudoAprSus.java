/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 *         Pacote: leiautes <BR>
 *         Classe: @(#)LaudoAprSus.java <BR>
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
 *         Comentários para a classe...
 */

package org.freedom.layout.orc;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import org.freedom.library.component.LeiauteGR;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;

public class LaudoAprSus extends LeiauteGR {

	private static final long serialVersionUID = 1L;

	private DbConnection con = null;

	private Font fnSubTitulo = new Font( "Times New Roman", Font.PLAIN, 7 );

	private Font fnSubTituloB = new Font( "Times New Roman", Font.BOLD, 7 );

	private Font fnTituloBanner = new Font( "Times New Roman", Font.BOLD, 8 );

	private Font fnTitulo = new Font( "Times New Roman", Font.BOLD, 11 );

	private Font fnConteudo = new Font( "Courier", Font.PLAIN, 10 );

	private Font fnLogo = new Font( "Times New Roman", Font.BOLD, 11 );

	private Font fnLogo2 = new Font( "Times New Roman", Font.PLAIN, 10 );

	Vector<?> vParamOrc = new Vector<Object>();

	public void montaG() {

		montaRel();
	}

	private void montaRel() {

		int iCodOrc = Integer.parseInt( vParamOrc.elementAt( 0 ).toString() );
		int iYPosProd = 327;
		imprimeRodape( false );
		try {
			String sSQLCab = "SELECT " + "(SELECT COUNT(IO.CODITORC) FROM VDITORCAMENTO IO WHERE IO.CODEMP=O.CODEMP" + " AND IO.CODFILIAL=O.CODFILIAL AND IO.CODORC=O.CODORC)," + "(SELECT A.NOMEATEND FROM ATATENDENTE A WHERE A.CODATEND=O.CODATEND"
					+ " AND A.CODEMP=O.CODEMPAE AND A.CODFILIAL=O.CODFILIALAE)," + " O.CODORC,O.TXT01,C.CPFCONV, C.IDENTIFICCONV, C.MAECONV, C.CEPCONV, C.DTNASCCONV," + " C.SEXOCONV, C.CODCONV,C.NOMECONV,C.CIDCONV,C.UFCONV,C.ENDCONV,C.BAIRCONV,C.FONECONV,"
					+ " T.DESCTPCONV,O.OBSORC,O.VLRLIQORC,O.VLRDESCORC, O.VLRDESCITORC,O.VLRADICORC," + "O.VLRPRODORC,F.CIDFILIAL,C.NUMCONV,C.CNSCONV" + " FROM VDORCAMENTO O, ATCONVENIADO C, ATTIPOCONV T, SGFILIAL F"
					+ " WHERE T.CODEMP=C.CODEMPTC AND T.CODFILIAL=C.CODFILIALTC AND T.CODTPCONV=C.CODTPCONV" + " AND C.CODCONV=O.CODCONV AND C.CODEMP=O.CODEMPCV AND C.CODFILIAL=O.CODFILIALCV" + " AND F.CODEMP=O.CODEMP AND F.CODFILIAL=O.CODFILIAL"
					+ " AND O.TIPOORC = 'O' AND O.CODORC=? AND O.CODEMP=? AND O.CODFILIAL=?";

			PreparedStatement psCab = con.prepareStatement( sSQLCab );
			psCab.setInt( 1, iCodOrc );
			psCab.setInt( 2, Aplicativo.iCodEmp );
			psCab.setInt( 3, ListaCampos.getMasterFilial( "VDORCAMENTO" ) );
			ResultSet rsCab = psCab.executeQuery();

			if ( !rsCab.next() )
				return;

			String sSQL = "SELECT " + "IT.VLRPRODITORC/IT.QTDITORC,IT.CODPROD," + " P.DESCPROD,P.CODBARPROD,IT.QTDITORC,IT.VLRDESCITORC,IT.VLRLIQITORC" + " FROM VDITORCAMENTO IT, EQPRODUTO P WHERE" + " P.CODPROD=IT.CODPROD AND P.CODEMP=IT.CODEMPPD AND P.CODFILIAL=IT.CODFILIALPD"
					+ " AND IT.TIPOORC = 'O' AND IT.CODORC=? AND IT.CODEMP=? AND IT.CODFILIAL=?";

			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, iCodOrc );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "VDORCAMENTO" ) );
			ResultSet rs = ps.executeQuery();

			montaCab( rsCab );
			for ( int i = 1; ( rs.next() ); i++ ) {
				setFonte( fnConteudo );
				drawTexto( Funcoes.setMascara( rs.getString( "CODBARPROD" ) != null ? rs.getString( "CODBARPROD" ).trim() : "", "##.###.######-##" ), 9, iYPosProd );
				drawTexto( rs.getString( "DESCPROD" ), 109, iYPosProd );
				iYPosProd = iYPosProd + 29;
				if ( ( i % 4 ) == 0 && i < rsCab.getInt( 1 ) ) {
					termPagina();
					montaCab( rsCab );
					iYPosProd = 327;
					i = 1;
				}

			}
			finaliza();
			rsCab.close();
			psCab.close();
			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao montar o cabeçalho do relatório!!!\n" + err.getMessage() );
			err.printStackTrace();
		}

		termPagina();
		finaliza();
	}

	private void montaCab( ResultSet rs ) {

		try {
			// ImageIcon img = Icone.novo("CruzVermelha.gif");
			// double dFatProp = dAltLogo/(double)img.getIconHeight();

			// drawImagem(img,1,1,(int)(img.getIconWidth()*dFatProp),(int)dAltLogo);

			setPincel( new BasicStroke( (float) 0.1 ) );

			drawRetangulo( 0, 0, 145, 32 );
			drawRetangulo( 152, 0, 260, 32 );

			drawRetangulo( 1, 1, 30, 30 );

			setCor( new Color( 255, 0, 0 ), new Color( 255, 0, 0 ) );
			drawRetangulo( 4, 11, 24, 10, "S" );
			drawRetangulo( 11, 4, 10, 24, "S" );

			setCor( new Color( 255, 255, 255 ), new Color( 0, 0, 0 ) );

			setPincel( new BasicStroke( 1 ) );

			setFonte( fnLogo );
			drawTexto( "SUS", 35, 20 );
			setFonte( fnLogo2 );
			drawTexto( "Sistema", 60, 10 );
			drawTexto( "Único de", 60, 20 );
			drawTexto( "Saúde", 60, 30 );

			drawTexto( "Ministério", 100, 10 );
			drawTexto( "da", 100, 20 );
			drawTexto( "Saúde", 100, 30 );

			setFonte( fnTituloBanner );
			drawTexto( "LAUDO MÉDICO PARA EMISSÃO DE APAC", 153, 9, 260, AL_BCEN );
			drawTexto( "REABILITAÇÃO FÍSICA / ÓRTESES,PRÓTESES", 153, 17, 260, AL_BCEN );
			drawTexto( "E MEIOS AUXILIARES DE LOCOMOÇÃO", 153, 26, 260, AL_BCEN );

			drawRetangulo( 422, 0, 70, 32, AL_BDIR );
			setFonte( fnSubTitulo );
			drawTexto( "N° de Prontuário", 425, 7 );

			// Informações da entidade
			setFonte( fnTitulo );
			drawTexto( "Identificação da Unidade", 0, 50 );

			drawRetangulo( 0, 52, 0, 35, AL_LL );

			drawRetangulo( 5, 57, 380, 24 );
			setFonte( fnSubTitulo );
			drawTexto( "Nome", 9, 65 );

			drawRetangulo( 390, 57, 5, 24, AL_CDIR );
			drawTexto( "CGC/CNPJ", 394, 65 );

			String sSQL = "SELECT NOMEEMP,CNPJEMP FROM SGEMPRESA WHERE CODEMP=?";
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ResultSet rs2 = ps.executeQuery();
			if ( rs2.next() ) {
				setFonte( fnConteudo );
				drawTexto( rs2.getString( "NOMEEMP" ).toUpperCase(), 9, 74 );
				drawTexto( Funcoes.setMascara( rs2.getString( "CnpjEmp" ), "##.###.###/####-##" ), 394, 74 );
			}
			rs2.close();
			ps.close();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao montar o cabeçalho da empresa!!!\n" + err.getMessage() );
			err.printStackTrace();
		}

		try {

			// Informações do paciente
			setFonte( fnTitulo );
			drawTexto( "Dados do Paciente", 0, 100 );
			drawRetangulo( 0, 102, 0, 187, AL_LL );

			// Linha 1
			setFonte( fnSubTitulo );
			drawRetangulo( 5, 107, 5, 24, AL_CDIR );
			drawTexto( "Nome do Paciente", 9, 115 );
			setFonte( fnConteudo );
			drawTexto( rs.getString( "NomeConv" ) != null ? rs.getString( "NomeConv" ).trim() : "", 9, 125 );

			// Linha 2
			setFonte( fnSubTitulo );
			drawRetangulo( 5, 136, 115, 24 );
			drawTexto( "CPF do Paciente", 9, 144 );
			setFonte( fnConteudo );
			drawTexto( rs.getString( "CPFCONV" ) != null ? rs.getString( "CPFCONV" ).trim() : "", 9, 154 );

			setFonte( fnSubTitulo );
			drawRetangulo( 125, 136, 5, 24, AL_CDIR );
			drawTexto( "Nome da Mãe", 129, 144 );
			setFonte( fnConteudo );
			drawTexto( rs.getString( "MAECONV" ) != null ? rs.getString( "MAECONV" ).trim() : "", 129, 154 );

			// Linha 3
			setFonte( fnSubTitulo );
			drawRetangulo( 5, 165, 360, 24 );
			drawTexto( "Endereço (Logradouro, nro., complemento, bairro)", 9, 173 );
			setFonte( fnConteudo );
			drawTexto( rs.getString( "ENDCONV" ) != null ? rs.getString( "ENDCONV" ).trim() + ( rs.getString( "NUMCONV" ) != null ? ", " + rs.getString( "NUMCONV" ) : "" ) : "", 9, 183 );

			setFonte( fnSubTitulo );
			drawRetangulo( 370, 165, 45, 24 );
			drawTexto( "DDD", 374, 173 );
			setFonte( fnConteudo );
			drawTexto( rs.getString( "FONECONV" ) != null ? rs.getString( "FONECONV" ).trim().substring( 0, 4 ) : "", 374, 183 );

			setFonte( fnSubTitulo );
			drawRetangulo( 420, 165, 5, 24, AL_CDIR );
			drawTexto( "Telefone", 424, 173 );
			setFonte( fnConteudo );
			drawTexto( rs.getString( "FONECONV" ) != null ? Funcoes.setMascara( rs.getString( "FONECONV" ).substring( 4 ), "####-####" ) : "", 424, 183 );

			// Linha 4
			setFonte( fnSubTitulo );
			drawRetangulo( 5, 194, 165, 24 );
			drawTexto( "Município", 9, 202 );
			setFonte( fnConteudo );
			drawTexto( rs.getString( "CIDCONV" ) != null ? rs.getString( "CIDCONV" ).trim() : "", 9, 212 );

			setFonte( fnSubTitulo );
			drawRetangulo( 175, 194, 35, 24 );
			drawTexto( "UF", 179, 202 );
			setFonte( fnConteudo );
			drawTexto( rs.getString( "UFCONV" ) != null ? rs.getString( "UFCONV" ) : "", 179, 212 );

			setFonte( fnSubTitulo );
			drawRetangulo( 215, 194, 70, 24 );
			drawTexto( "CEP", 219, 202 );
			setFonte( fnConteudo );
			drawTexto( rs.getString( "CEPCONV" ) != null ? Funcoes.setMascara( rs.getString( "CEPCONV" ).trim(), "####-###" ) : "", 219, 212 );

			setFonte( fnSubTitulo );
			drawRetangulo( 290, 194, 100, 24 );
			drawTexto( "Dta. Nasc.", 294, 202 );
			setFonte( fnConteudo );
			drawTexto( rs.getDate( "DtNascConv" ) != null ? StringFunctions.sqlDateToStrDate( rs.getDate( "DtNascConv" ) ) : "", 294, 212 );

			setFonte( fnSubTitulo );
			drawRetangulo( 395, 194, 5, 24, AL_CDIR );
			drawTexto( "Sexo", 399, 202 );
			setFonte( fnConteudo );
			if ( rs.getString( "SEXOCONV" ).equals( "M" ) ) {
				drawElipse( 399, 204, 10, 10, "S" );
				drawTexto( "M", 412, 212 );
				drawElipse( 452, 204, 10, 10, "N" );
				drawTexto( "F", 467, 212 );
			}
			else {
				drawElipse( 399, 204, 10, 10, "N" );
				drawTexto( "M", 412, 212 );
				drawElipse( 452, 204, 10, 10, "S" );
				drawTexto( "F", 467, 212 );
			}

			// Linha5
			setFonte( fnSubTitulo );
			drawRetangulo( 5, 223, 250, 24 );
			drawTexto( "CÓDIGO DE TRANSAÇÃO CMCE", 9, 231 );
			setFonte( fnConteudo );
			drawTexto( rs.getString( "TXT01" ) != null ? rs.getString( "TXT01" ).trim() : "", 9, 241 );

			setFonte( fnSubTitulo );
			drawRetangulo( 260, 223, 5, 24, AL_CDIR );
			drawTexto( "Nro. CARTÃO DO SUS", 264, 231 );
			drawTexto( rs.getString( "CNSCONV" ) != null ? rs.getString( "CNSCONF" ).trim() : "", 264, 241 );
			setFonte( fnConteudo );
			drawTexto( rs.getString( "IDENTIFICCONV" ) != null ? rs.getString( "IDENTIFICCONV" ).trim() : "", 264, 241 );

			// Linha6
			setFonte( fnSubTitulo );
			drawRetangulo( 5, 252, 60, 30 );
			drawTexto( "CONVÊNIO", 9, 267 );

			drawRetangulo( 65, 252, 40, 10 );
			drawTexto( "SIM", 65, 260, 40, AL_BCEN );
			drawRetangulo( 65, 262, 40, 10 );
			drawRetangulo( 65, 272, 40, 10 );

			drawRetangulo( 105, 252, 40, 10 );
			drawTexto( "NÃO", 105, 260, 40, AL_BCEN );
			drawRetangulo( 105, 262, 40, 10 );
			drawRetangulo( 105, 272, 40, 10 );

			drawRetangulo( 150, 252, 5, 30, AL_CDIR );
			drawTexto( "Nome do Convênio", 154, 260 );

			// Impressão de esqueleto para produtos
			setFonte( fnTitulo );
			drawTexto( "Dados da Solicitação", 0, 302 );
			drawRetangulo( 0, 304, 0, 153, AL_LL );

			// Linha 1
			setFonte( fnSubTitulo );
			drawRetangulo( 5, 309, 95, 24 );
			drawRetangulo( 105, 309, 5, 24, AL_CDIR );
			drawTexto( "Código do Procedimento", 9, 317 );
			drawTexto( "Nome do Procedimento", 109, 317 );

			// Linha 2
			setFonte( fnSubTitulo );
			drawRetangulo( 5, 338, 95, 24 );
			drawRetangulo( 105, 338, 5, 24, AL_CDIR );
			drawTexto( "Código do Procedimento", 9, 346 );
			drawTexto( "Nome do Procedimento", 109, 346 );

			// Linha 3
			setFonte( fnSubTitulo );
			drawRetangulo( 5, 367, 95, 24 );
			drawRetangulo( 105, 367, 5, 24, AL_CDIR );
			drawTexto( "Código do Procedimento", 9, 373 );
			drawTexto( "Nome do Procedimento", 109, 373 );

			// Linha 4
			setFonte( fnSubTitulo );
			drawRetangulo( 5, 396, 95, 24 );
			drawRetangulo( 105, 396, 5, 24, AL_CDIR );
			drawTexto( "Código do Procedimento", 9, 404 );
			drawTexto( "Nome do Procedimento", 109, 404 );

			// Linha 5
			setFonte( fnSubTituloB );
			drawRetangulo( 5, 427, 110, 24 );
			drawRetangulo( 120, 427, 5, 24, AL_CDIR );
			drawTexto( "CPF do Médico", 9, 435 );
			drawTexto( "Nome do Médico", 124, 435 );
			try {
				String sSQL = "SELECT A.NOMEATEND,A.CPFATEND FROM SGPREFERE2 P, ATATENDENTE A WHERE P.CODEMP=? AND P.CODFILIAL=? AND A.CODEMP=P.CODEMPAE AND A.CODFILIAL=P.CODFILIALAE AND A.CODATEND=P.CODATEND";
				PreparedStatement ps = con.prepareStatement( sSQL );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, Aplicativo.iCodFilial );
				ResultSet rs2 = ps.executeQuery();
				if ( rs2.next() ) {
					setFonte( fnConteudo );
					drawTexto( Funcoes.setMascara( rs2.getString( "CPFATEND" ), "###.###.###-##" ), 9, 445 );
					drawTexto( rs2.getString( "NOMEATEND" ), 124, 445 );
				}
				rs2.close();
				ps.close();
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao pesquisar informações do médico padrão!!!\n" + err.getMessage() );
				err.printStackTrace();
			}

			// Impressão de esqueleto para observações
			drawRetangulo( 0, 469, 0, 150, AL_LL );

			// Linha 1
			setFonte( fnSubTitulo );
			drawRetangulo( 5, 474, 450, 30 );
			drawRetangulo( 460, 474, 5, 30, AL_CDIR );
			drawTexto( "DIAGNÓSTICO DA INCAPACIDADE", 9, 482 );
			drawTexto( "CID 10", 464, 482 );

			// Linha 2
			setFonte( fnSubTitulo );
			drawRetangulo( 5, 509, 5, 105, AL_CDIR );
			drawTexto( "OBSERVAÇÕES", 9, 517 );

			// Impressão de esqueleto para considerações finais
			drawRetangulo( 0, 629, 0, 50, AL_LL );

			// Linha 1
			setFonte( fnSubTitulo );
			drawRetangulo( 5, 639, 100, 30 );
			drawRetangulo( 110, 639, 5, 30, AL_CDIR );
			drawTexto( "DATA", 9, 647 );
			drawTexto( "ASSINATURA E CARIMBO DO MÉDICO", 114, 647 );
			setFonte( fnConteudo );
			drawTexto( Funcoes.dateToStrDate( new Date() ), 9, 660 );
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao montar dados do paciente!!!\n" + err.getMessage() );
			err.printStackTrace();
		}
	}

	public void setParam( Vector<?> vParam ) {

		vParamOrc = vParam;
	}

	public void setConexao( DbConnection cn ) {

		con = cn;
	}
}
