/**
 * @version 14/17/2003 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 *         Pacote: leiautes <BR>
 *         Classe: @(#)FichaMedida.java <BR>
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
 *         Classe para impressão do documento ficha de medida.
 */

package org.freedom.layout.atd;

import java.awt.Font;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.LeiauteGR;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;

public class FichaMedida extends LeiauteGR {

	private static final long serialVersionUID = 1L;

	private DbConnection con = null;

	private int iCodConv = 0;

	private String sAtend = "";

	private int iCodLev = 0;

	private Font fnTopEmp = new Font( "Arial", Font.BOLD, 11 );

	private Font fnCabEmp = new Font( "Arial", Font.PLAIN, 8 );

	private Font fnCabEmpNeg = new Font( "Arial", Font.BOLD, 8 );

	public void montaG() {

		montaCabEmp( con );
		montaCab();
	}

	private void montaCab() {

		try {
			String sSQL = "SELECT C.CODCONV,C.NOMECONV,C.CIDCONV,C.UFCONV,T.DESCTPCONV,C.DTNASCCONV," + "(SELECT TA.DESCTPATEND FROM ATATENDENTE A1, ATTIPOATEND TA WHERE " + "A1.CODEMP=C.CODEMPAE AND A1.CODFILIAL=C.CODFILIALAE AND A1.CODATEND=C.CODATEND AND "
					+ "TA.CODEMP=A1.CODEMPTA AND TA.CODFILIAL=A1.CODFILIALTA AND TA.CODTPATEND=A1.CODTPATEND)," + "(SELECT A2.NOMEATEND FROM ATATENDENTE A2 WHERE " + "A2.CODEMP=C.CODEMPAE AND A2.CODFILIAL=C.CODFILIALAE AND A2.CODATEND=C.CODATEND ),"
					+ "(SELECT EC.NOMEENC FROM ATENCAMINHADOR EC WHERE " + "EC.CODEMP=C.CODEMP AND EC.CODFILIAL=C.CODFILIAL AND EC.CODENC=C.CODENC )," + "(SELECT EC.CIDENC FROM ATENCAMINHADOR EC WHERE " + "EC.CODEMP=C.CODEMP AND EC.CODFILIAL=C.CODFILIAL AND EC.CODENC=C.CODENC ),"
					+ "C.DDDCONV,C.FONECONV,CL.RAZCLI, CL.CIDCLI" + " FROM ATCONVENIADO C, ATTIPOCONV T,VDCLIENTE CL" + " WHERE T.CODEMP=C.CODEMPTC AND T.CODFILIAL=C.CODFILIALTC AND T.CODTPCONV=C.CODTPCONV" + " AND CL.CODCLI=C.CODCLI AND CL.CODEMP=C.CODEMPCL AND CL.CODFILIAL=C.CODFILIALCL"
					+ " AND C.CODCONV=? AND C.CODEMP=? AND C.CODFILIAL=?";

			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, iCodConv );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "ATCONVENIADO" ) );
			ResultSet rs = ps.executeQuery();
			if ( rs.next() ) {
				int iY = 35;

				montaCabEmp( con );

				drawLinha( 0, iY, 0, 0, AL_LL );

				iY += 20;

				setFonte( fnTopEmp );
				drawTexto( "FICHA DE MEDIDA", 0, iY, getFontMetrics( fnCabEmp ).stringWidth( "FICHA DE MEDIDA" ), AL_CEN );
				setFonte( fnCabEmpNeg );

				iY += 10;

				drawLinha( 0, iY, 0, 0, AL_LL );

				iY += 10;

				setFonte( fnCabEmp );
				drawTexto( "Ficha Nº:   ", 5, iY );
				setFonte( fnCabEmpNeg );
				drawTexto( StringFunctions.strZero( "" + iCodLev, 8 ), 65, iY );
				drawTexto( "Emissão: ", 285, iY );
				setFonte( fnCabEmpNeg );
				drawTexto( Funcoes.dateToStrDataHora( new Date() ), 350, iY );

				iY += 5;

				drawLinha( 0, iY, 0, 0, AL_LL );

				iY += 15;

				setFonte( new Font( "Arial", Font.BOLD, 10 ) );
				drawTexto( "Cliente:    ", 5, iY );

				setFonte( new Font( "Arial", Font.PLAIN, 10 ) );
				drawTexto( rs.getString( "CodConv" ) + " - " + rs.getString( "NomeConv" ).trim(), 70, iY );

				setFonte( new Font( "Arial", Font.BOLD, 10 ) );
				drawTexto( "Atendente:", 285, iY );
				setFonte( new Font( "Arial", Font.PLAIN, 10 ) );
				drawTexto( sAtend, 370, iY );

				iY += 20;

				setFonte( new Font( "Arial", Font.BOLD, 10 ) );
				drawTexto( "Convênio:", 5, iY );
				setFonte( new Font( "Arial", Font.PLAIN, 10 ) );
				// drawTexto(rs.getString("DescTpConv"),60,iY);
				drawTexto( rs.getString( "DescTpConv" ).trim() + " / " + ( rs.getString( "RAZCLI" ) ).trim() + " / " + ( rs.getString( "CIDCLI" ) ).trim(), 70, iY );

				iY += 20;

				setFonte( new Font( "Arial", Font.BOLD, 10 ) );
				drawTexto( "Cidade:", 5, iY );
				setFonte( new Font( "Arial", Font.PLAIN, 10 ) );
				drawTexto( rs.getString( "CidConv" ) != null ? rs.getString( "CidConv" ).trim() + ( rs.getString( "UFConv" ) != null ? " / " + rs.getString( "UFConv" ) : "" ) : "", 60, iY );

				setFonte( new Font( "Arial", Font.BOLD, 10 ) );
				// drawTexto((rs.getString(7) != null ? rs.getString(7) : "").trim()+" ",285,iY);
				// setFonte(new Font("Arial",Font.PLAIN,10));
				// drawTexto(rs.getString(8),315+getFontMetrics(new Font("Arial",Font.BOLD,8)).stringWidth((rs.getString(7) != null ? rs.getString(7) : "").trim()+" :  "),iY);

				if ( rs.getString( 9 ) != null ) {
					setFonte( new Font( "Arial", Font.BOLD, 10 ) );
					drawTexto( "Unidade Enc. :", 285, iY );
					setFonte( new Font( "Arial", Font.PLAIN, 10 ) );
					drawTexto( Funcoes.copy( ( rs.getString( 9 ) != null ? rs.getString( 9 ) : "" ).trim() + " " + ( rs.getString( 10 ) != null ? " / " + rs.getString( 10 ).trim() : "" ), 30 ), 380, iY );
				}
				iY += 20;

				setFonte( new Font( "Arial", Font.BOLD, 10 ) );
				drawTexto( "Data de Nascimento:", 5, iY );
				setFonte( new Font( "Arial", Font.PLAIN, 8 ) );
				drawTexto( StringFunctions.sqlDateToStrDate( rs.getDate( "DtNascConv" ) ), 130, iY );

				setFonte( new Font( "Arial", Font.BOLD, 10 ) );
				drawTexto( "Cod. Produto: _________________", 285, iY );

				iY += 20;

				setFonte( new Font( "Arial", Font.BOLD, 10 ) );
				drawTexto( "Telefone:", 5, iY );
				setFonte( new Font( "Arial", Font.PLAIN, 10 ) );
				drawTexto( rs.getString( "dddconv" ), 60, iY );
				drawTexto( " - ", 73, iY );
				drawTexto( rs.getString( "FoneConv" ), 80, iY );

				setFonte( new Font( "Arial", Font.BOLD, 10 ) );
				drawTexto( "Tecnico: _______________, Ass.:_______________", 285, iY );

				iY += 10;

				// drawLinha(275,80,275,iY);
				drawLinha( 0, iY, 0, 0, AL_LL );

			}
			termPagina();
			finaliza();
			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao montar o cabeçalho do relatório!!!\n" + err.getMessage() );
		}
	}

	public void setParam( Vector<?> vParam ) {

		iCodConv = Integer.parseInt( "" + vParam.elementAt( 0 ) );
		sAtend = "" + vParam.elementAt( 1 );
		iCodLev = Integer.parseInt( "" + vParam.elementAt( 2 ) );
	}

	public void setConexao( DbConnection cn ) {

		con = cn;
	}
}
