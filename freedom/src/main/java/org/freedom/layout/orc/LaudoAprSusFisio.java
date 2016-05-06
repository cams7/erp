/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 *         Pacote: leiautes <BR>
 *         Classe: @(#)LaudoAprSusFisio.java <BR>
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import org.freedom.library.component.LeiauteGR;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;

public class LaudoAprSusFisio extends LeiauteGR {

	private static final long serialVersionUID = 1L;

	private DbConnection con = null;

	private Font fnSubTitulo = new Font( "Times New Roman", Font.PLAIN, 7 );

	private Font fnTituloBanner = new Font( "Times New Roman", Font.BOLD, 8 );

	private Font fnTitulo = new Font( "Times New Roman", Font.BOLD, 11 );

	private Font fnConteudo = new Font( "Courier", Font.PLAIN, 10 );

	private Font fnCabCli = new Font( "Times New Roman", Font.PLAIN, 9 );

	private Font fnLogo = new Font( "Times New Roman", Font.BOLD, 11 );

	private Font fnLogo2 = new Font( "Times New Roman", Font.PLAIN, 10 );

	Vector<?> vParamOrc = new Vector<Object>();

	String sCidade;

	public void montaG() {

		montaRel();
	}

	private void montaRel() {

		int iCodOrc = Integer.parseInt( vParamOrc.elementAt( 0 ).toString() );
		int iYPosProd = 327;
		imprimeRodape( false );
		try {
			String sSQLCab = "SELECT " 
		            + "(SELECT COUNT(IO.CODITORC) FROM VDITORCAMENTO IO WHERE IO.CODEMP=O.CODEMP" 
		            + " AND IO.CODFILIAL=O.CODFILIAL AND IO.CODORC=O.CODORC)," 
		            + "(SELECT A.NOMEATEND FROM ATATENDENTE A WHERE A.CODATEND=O.CODATEND"
					+ " AND A.CODEMP=O.CODEMPAE AND A.CODFILIAL=O.CODFILIALAE)," 
		            + " O.CODORC,O.TXT01,C.CPFCONV, C.IDENTIFICCONV, C.MAECONV, C.CEPCONV, C.DTNASCCONV," 
					+ " C.SEXOCONV, C.CODCONV,C.NOMECONV,O.DTORC,O.DTVENCORC,C.CIDCONV,C.UFCONV,C.ENDCONV,C.BAIRCONV,C.DDDCONV,C.FONECONV,"
					+ " T.DESCTPCONV,O.OBSORC,O.VLRLIQORC,O.VLRDESCORC, O.VLRDESCITORC,O.VLRADICORC,O.VLRPRODORC,F.CIDFILIAL,C.NUMCONV, C.CNSCONV" 
					+ " FROM VDORCAMENTO O, ATCONVENIADO C, ATTIPOCONV T, SGFILIAL F"
					+ " WHERE T.CODEMP=C.CODEMPTC AND T.CODFILIAL=C.CODFILIALTC AND T.CODTPCONV=C.CODTPCONV" 
					+ " AND C.CODCONV=O.CODCONV AND C.CODEMP=O.CODEMPCV AND C.CODFILIAL=O.CODFILIALCV" 
					+ " AND F.CODEMP=O.CODEMP AND F.CODFILIAL=O.CODFILIAL"
					+ " AND O.TIPOORC = 'O' AND O.CODORC=? AND O.CODEMP=? AND O.CODFILIAL=?";

			PreparedStatement psCab = con.prepareStatement( sSQLCab );
			psCab.setInt( 1, iCodOrc );
			psCab.setInt( 2, Aplicativo.iCodEmp );
			psCab.setInt( 3, ListaCampos.getMasterFilial( "VDORCAMENTO" ) );
			ResultSet rsCab = psCab.executeQuery();

			if ( !rsCab.next() )
				return;

			String sSQL = "SELECT " 
					+ "IT.VLRPRODITORC/IT.QTDITORC,IT.CODPROD," 
					+ " DESCPROD,P.CODFABPROD,IT.QTDITORC,IT.VLRDESCITORC,IT.VLRLIQITORC" 
					+ " FROM VDITORCAMENTO IT, EQPRODUTO P WHERE" 
					+ " P.CODPROD=IT.CODPROD AND P.CODEMP=IT.CODEMPPD AND P.CODFILIAL=IT.CODFILIALPD"
					+ " AND IT.TIPOORC = 'O' AND IT.CODORC=? AND IT.CODEMP=? AND IT.CODFILIAL=?";

			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, iCodOrc );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "VDORCAMENTO" ) );
			ResultSet rs = ps.executeQuery();

			montaCab( rsCab );
			for ( int i = 1; ( rs.next() ); i++ ) {
				setFonte( fnConteudo );
				drawTexto( Funcoes.setMascara( rs.getString( "CODFABPROD" ) != null ? rs.getString( "CODFABPROD" ) : "", "##.###.######-##" ), 9, iYPosProd );
				drawTexto( rs.getString( "DESCPROD" ), 109, iYPosProd );
				iYPosProd = iYPosProd + 29;
				if ( ( i % 4 ) == 0 && i < rsCab.getInt( 1 ) ) {
					termPagina();
					montaCab( rsCab );
					iYPosProd = 327;
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

			setPincel( new BasicStroke( (float) 0.1 ) );

			drawRetangulo( 0, 0, 145, 32 );
			drawRetangulo( 152, 0, 260, 32 );

			drawRetangulo( 1, 1, 30, 30 );

			setCor( new Color( 255, 0, 0 ), new Color( 255, 0, 0 ) );
			drawRetangulo( 4, 11, 24, 10, "S" );
			drawRetangulo( 11, 4, 10, 24, "S" );

			setCor( new Color( 255, 255, 255 ), new Color( 0, 0, 0 ) );

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
			// drawTexto("LAUDO MÉDICO PARA EMISSÃO DE APAC",153,9,260,AL_BCEN);
			drawTexto( "REABILITAÇÃO FÍSICA / ÓRTESES, PRÓTESES", 153, 15, 260, AL_BCEN );
			drawTexto( "E MEIOS AUXILIARES DE LOCOMOÇÃO", 153, 24, 260, AL_BCEN );

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

			String sSQL = "SELECT NOMEEMP,CNPJEMP,CIDEMP FROM SGEMPRESA WHERE CODEMP=?";
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ResultSet rs2 = ps.executeQuery();
			if ( rs2.next() ) {
				setFonte( fnConteudo );
				drawTexto( rs2.getString( "NOMEEMP" ).toUpperCase(), 9, 74 );
				drawTexto( Funcoes.setMascara( rs2.getString( "CnpjEmp" ), "##.###.###/####-##" ), 394, 74 );
				sCidade = rs2.getString( "CIDEMP" ).toUpperCase();
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
			drawRetangulo( 0, 102, 0, 600, AL_LL );

			// Linha 1
			setFonte( fnSubTitulo );
			drawRetangulo( 5, 107, 182, 24, AL_CDIR );
			drawTexto( "Nome do Paciente", 9, 115 );
			setFonte( fnConteudo );
			drawTexto( rs.getString( "NomeConv" ) != null ? rs.getString( "NomeConv" ).trim() : "", 9, 125 );

			setFonte( fnSubTitulo );
			drawRetangulo( 390, 107, 5, 24, AL_CDIR );
			drawTexto( "Numero CNS.", 394, 115 );
			setFonte( fnConteudo );
			drawTexto( rs.getString( "CnsConv" ) != null ? rs.getString( "CnsCOnv" ).trim() : "", 394, 125 );

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
			drawTexto( getString(rs.getString( "ENDCONV" )).trim()+
					", "+getString(rs.getString( "NUMCONV" ))+
					", "+getString(rs.getString( "BAIRCONV" )).trim(), 9, 183 );

			setFonte( fnSubTitulo );
			drawRetangulo( 370, 165, 45, 24 );
			drawTexto( "DDD", 374, 173 );
			setFonte( fnConteudo );
			drawTexto( rs.getString( "DDDCONV" ) != null ? rs.getString( "DDDCONV" ).trim() : "", 376, 183 );

			setFonte( fnSubTitulo );
			drawRetangulo( 420, 165, 5, 24, AL_CDIR );
			drawTexto( "Telefone", 424, 173 );
			setFonte( fnConteudo );
			drawTexto( rs.getString( "FONECONV" ) != null ? Funcoes.setMascara( rs.getString( "FONECONV" ).trim(), "####-####" ) : "", 424, 183 ); //

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

			setFonte( fnCabCli );

			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime( rs.getDate( "DtOrc" ) );
			int iMes = cal.get( Calendar.MONTH ) + 1;
			String sMes = Funcoes.strMes( iMes ).toUpperCase();

			drawTexto( "DECLARO QUE NO MÊS DE " + sMes + " O PACIENTE IDENTIFICADO ACIMA, FOI SUBMETIDO AOS PROCEDIMENTOS", 9, 231 );
			drawTexto( "ABAIXO RELACIONADOS, CONFORME ASSINATURA(S) DO PACIENTE RESPONSÁVEL ABAIXO.", 9, 243 );

			drawTexto( sCidade.trim() + ", " + Funcoes.dateToStrExtenso( new Date() ).toUpperCase(), 9, 265 );

			drawLinha( 180, 285, 340, 285 );
			drawTexto( "Assinatura / Carimbo da Unidade", 190, 295 );

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

			setFonte( fnSubTitulo );
			drawTexto( "Atenção: assine apenas uma vez para procedimentos de órteses, próteses e/ou meios auxiliares de locomoção.", 9, 438 );

			drawRetangulo( 5, 445, 5, 215, AL_CDIR );

			drawLinha( 5, 460, 5, 0, AL_CDIR ); // Linhas Horizontais
			drawLinha( 5, 470, 5, 0, AL_CDIR ); // Linhas Horizontais
			drawLinha( 5, 480, 5, 0, AL_CDIR ); // Linhas Horizontais
			drawLinha( 5, 490, 5, 0, AL_CDIR ); // Linhas Horizontais
			drawLinha( 5, 500, 5, 0, AL_CDIR ); // Linhas Horizontais
			drawLinha( 5, 510, 5, 0, AL_CDIR ); // Linhas Horizontais
			drawLinha( 5, 520, 5, 0, AL_CDIR ); // Linhas Horizontais
			drawLinha( 5, 530, 5, 0, AL_CDIR ); // Linhas Horizontais
			drawLinha( 5, 540, 5, 0, AL_CDIR ); // Linhas Horizontais
			drawLinha( 5, 550, 5, 0, AL_CDIR ); // Linhas Horizontais
			drawLinha( 5, 560, 5, 0, AL_CDIR ); // Linhas Horizontais
			drawLinha( 5, 570, 5, 0, AL_CDIR ); // Linhas Horizontais
			drawLinha( 5, 580, 5, 0, AL_CDIR ); // Linhas Horizontais
			drawLinha( 5, 590, 5, 0, AL_CDIR ); // Linhas Horizontais
			drawLinha( 5, 600, 5, 0, AL_CDIR ); // Linhas Horizontais
			drawLinha( 5, 610, 5, 0, AL_CDIR ); // Linhas Horizontais
			drawLinha( 5, 620, 5, 0, AL_CDIR ); // Linhas Horizontais
			drawLinha( 5, 630, 5, 0, AL_CDIR ); // Linhas Horizontais
			drawLinha( 5, 640, 5, 0, AL_CDIR ); // Linhas Horizontais
			drawLinha( 5, 650, 5, 0, AL_CDIR ); // Linhas Horizontais

			drawLinha( 35, 445, 35, 660 ); // Linhas Verticais
			drawLinha( 100, 445, 100, 660 ); // Linhas Verticais

			drawLinha( 295, 445, 295, 660 );
			drawLinha( 330, 445, 330, 660 );
			drawLinha( 365, 445, 365, 660 );
			drawLinha( 400, 445, 400, 660 );

			drawLinha( 435, 445, 435, 660 );
			drawLinha( 470, 445, 470, 660 );
			drawLinha( 500, 445, 500, 660 );
			drawLinha( 530, 445, 530, 660 );

			setFonte( fnCabCli );

			drawTexto( "DATA", 30, 456, 70, AL_BCEN );
			drawTexto( "ASSINATURA", 0, 456, 400, AL_BCEN );

			drawTexto( "NUT", 112, 456, 400, AL_BCEN );
			drawTexto( "ENF", 147, 456, 400, AL_BCEN );
			drawTexto( "MED", 182, 456, 400, AL_BCEN );
			drawTexto( "ASS", 217, 456, 400, AL_BCEN );

			drawTexto( "FISIO", 252, 456, 400, AL_BCEN );
			drawTexto( "PSICO", 286, 456, 400, AL_BCEN );
			drawTexto( "TO", 315, 456, 400, AL_BCEN );
			drawTexto( "FONO", 346, 456, 400, AL_BCEN );

			drawTexto( "1", 5, 468, 30, AL_BCEN );
			drawTexto( "2", 5, 478, 30, AL_BCEN );
			drawTexto( "3", 5, 488, 30, AL_BCEN );
			drawTexto( "4", 5, 498, 30, AL_BCEN );
			drawTexto( "5", 5, 508, 30, AL_BCEN );
			drawTexto( "6", 5, 518, 30, AL_BCEN );
			drawTexto( "7", 5, 528, 30, AL_BCEN );
			drawTexto( "8", 5, 538, 30, AL_BCEN );
			drawTexto( "9", 5, 548, 30, AL_BCEN );
			drawTexto( "10", 5, 558, 30, AL_BCEN );
			drawTexto( "11", 5, 568, 30, AL_BCEN );
			drawTexto( "12", 5, 578, 30, AL_BCEN );
			drawTexto( "13", 5, 588, 30, AL_BCEN );
			drawTexto( "14", 5, 598, 30, AL_BCEN );
			drawTexto( "15", 5, 608, 30, AL_BCEN );
			drawTexto( "16", 5, 618, 30, AL_BCEN );
			drawTexto( "17", 5, 628, 30, AL_BCEN );
			drawTexto( "18", 5, 638, 30, AL_BCEN );
			drawTexto( "19", 5, 648, 30, AL_BCEN );
			drawTexto( "20", 5, 658, 30, AL_BCEN );

			setFonte( fnSubTitulo );
			drawTexto( "Este procedimento está sendo pago pelo SUS. Não é permitido cobrança complementar. Central de Atendimento ao Paciente: 350 9326.", 5, 670 );
			drawTexto( "ASSINATURA DO PACIENTE", 5, 695 );
			drawLinha( 120, 695, 320, 695 );

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao montar dados do paciente!!!\n" + err.getMessage() );
			err.printStackTrace();
		}
	}

	private String getString(String str) {
		if (str==null) {
			return "";
		} else {
			return str;
		}
	}
	
	public void setParam( Vector<?> vParam ) {

		vParamOrc = vParam;
	}

	public void setConexao( DbConnection cn ) {

		con = cn;
	}
}
