/**
 * @version 13/05/2004 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FRImpServ.java <BR>
 * 
 *                    Este programa é licenciado de acordo com a LPG-PC (Licença Pública Geral para Programas de Computador), <BR>
 *                    modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                    A LPG-PC deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste Programa. <BR>
 *                    Caso uma cópia da LPG-PC não esteja disponível junto com este Programa, você pode contatar <BR>
 *                    sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                    Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                    Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa é preciso estar <BR>
 *                    de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                    Relatório de impostos sobre serviços
 * 
 */

package org.freedom.modulos.std.view.frame.report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

public class FRImpServ extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JCheckBoxPad cbVendas = new JCheckBoxPad( "Só vendas?", "S", "N" );

	private JRadioGroup<?, ?> rgFormato = null;

	private String linhaFina = StringFunctions.replicate( "-", 133 );

	private String linhaLarga = StringFunctions.replicate( "=", 133 );

	private float fTotMesBase = 0;

	private float fTotMesISS = 0;

	private float fTotMesPIS = 0;

	private float fTotMesCOFINS = 0;

	private float fTotMesIR = 0;

	private float fTotMesCSocial = 0;

	private float fTotMesLiq = 0;

	private float fTotBase = 0;

	private float fTotISS = 0;

	private float fTotPIS = 0;

	private float fTotCOFINS = 0;

	private float fTotIR = 0;

	private float fTotCSocial = 0;

	private float fTotLiq = 0;

	public FRImpServ() {

		setTitulo( "Relatório de Impostos/Serviços" );
		setAtribos( 80, 80, 310, 210 );

		GregorianCalendar cal = new GregorianCalendar();
		cal.add( Calendar.DATE, -30 );
		txtDataini.setVlrDate( cal.getTime() );
		cal.add( Calendar.DATE, 30 );
		txtDatafim.setVlrDate( cal.getTime() );
		txtDataini.setRequerido( true );
		txtDatafim.setRequerido( true );

		cbVendas.setVlrString( "S" );

		rgFormato = new JRadioGroup<String, String>( 1, 2, new Object[] { "Detalhado", "Resumido" }, new Object[] { "D", "R" } );
		rgFormato.setVlrString( "D" );

		adic( new JLabelPad( "Período" ), 7, 0, 250, 20 );
		adic( txtDataini, 7, 20, 100, 20 );
		adic( txtDatafim, 110, 20, 100, 20 );
		adic( new JLabelPad( "Formato:" ), 7, 40, 250, 20 );
		adic( rgFormato, 7, 60, 203, 30 );
		adic( cbVendas, 7, 100, 100, 25 );

	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSql = null;
		ImprimeOS imp = null;
		int iMesAnt = -1;
		int iLinPag = 0;

		try {

			if ( txtDataini.getVlrString().length() < 10 || txtDatafim.getVlrString().length() < 10 ) {
				Funcoes.mensagemInforma( this, "Período inválido!" );
				return;
			}

			fTotMesBase = 0;
			fTotMesISS = 0;
			fTotMesPIS = 0;
			fTotMesCOFINS = 0;
			fTotMesIR = 0;
			fTotMesCSocial = 0;
			fTotMesLiq = 0;
			fTotBase = 0;
			fTotISS = 0;
			fTotPIS = 0;
			fTotCOFINS = 0;
			fTotIR = 0;
			fTotCSocial = 0;
			fTotLiq = 0;

			imp = new ImprimeOS( "", con );
			iLinPag = imp.verifLinPag() - 1;

			if ( rgFormato.getVlrString().equals( "D" ) ) {
				sSql = "SELECT V.DTEMITVENDA,V.CODVENDA,V.DOCVENDA,V.SERIE,V.CODCLI," + "C.RAZCLI,V.VLRBASEISSVENDA,V.VLRISSVENDA,V.VLRPISVENDA," + "V.VLRCOFINSVENDA,V.VLRIRVENDA,V.VLRCSOCIALVENDA,V.VLRLIQVENDA " + "FROM VDVENDA V,VDCLIENTE C, EQTIPOMOV TM " + "WHERE V.CODEMP=? AND V.CODFILIAL=? "
						+ "AND V.DTEMITVENDA BETWEEN ? AND ? " + "AND TM.CODTIPOMOV=V.CODTIPOMOV AND TM.CODEMP=V.CODEMPTM " + "AND TM.CODFILIAL=V.CODFILIALTM AND C.CODEMP=V.CODEMPCL " + "AND C.CODFILIAL=V.CODFILIALCL AND C.CODCLI=V.CODCLI "
						+ ( cbVendas.getVlrString().equals( "S" ) ? " AND (TM.TIPOMOV='VD' OR TM.TIPOMOV='SE') " : "" ) + "ORDER BY V.DTEMITVENDA, V.CODVENDA";
			}
			else {
				sSql = "SELECT EXTRACT(YEAR FROM V.DTEMITVENDA)," + "EXTRACT(MONTH FROM V.DTEMITVENDA),SUM(V.VLRBASEISSVENDA)," + "SUM(V.VLRISSVENDA),SUM(V.VLRPISVENDA),SUM(V.VLRCOFINSVENDA)," + "SUM(V.VLRIRVENDA),SUM(V.VLRCSOCIALVENDA),SUM(V.VLRLIQVENDA) " + "FROM VDVENDA V, EQTIPOMOV TM "
						+ "WHERE V.CODEMP=? AND V.CODFILIAL=? " + "AND V.DTEMITVENDA BETWEEN ? AND ? " + "AND TM.CODTIPOMOV=V.CODTIPOMOV AND TM.CODEMP=V.CODEMPTM " + "AND TM.CODFILIAL=V.CODFILIALTM " + ( cbVendas.getVlrString().equals( "S" ) ? " AND (TM.TIPOMOV='VD' OR TM.TIPOMOV='SE') " : "" )
						+ "GROUP BY EXTRACT(MONTH FROM V.DTEMITVENDA), EXTRACT(YEAR FROM V.DTEMITVENDA) " + "ORDER BY 1,2";
			}

			try {
				ps = con.prepareStatement( sSql );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "VDVENDA" ) );
				ps.setDate( 3, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				ps.setDate( 4, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
				rs = ps.executeQuery();

				imp.limpaPags();
				imp.montaCab();
				imp.setTitulo( "Relatorio de Impostos/Serviços" );
				imp.addSubTitulo( "IMPOSTOS SOBRE SERVICOS" );
				if ( cbVendas.getVlrString().equals( "S" ) )
					imp.addSubTitulo( "SOMENTE VENDAS" );
				imp.addSubTitulo( "PERIODO DE: " + txtDataini.getVlrString() + " ATE: " + txtDatafim.getVlrString() );

				if ( rgFormato.getVlrString().equals( "D" ) ) {
					while ( rs.next() ) {
						if ( imp.pRow() >= ( iLinPag - 1 ) ) {
							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( 0, "+" + linhaFina + "+" );
							imp.incPags();
							imp.eject();
						}
						if ( imp.pRow() == 0 ) {
							imp.impCab( 136, true );
							imp.say( 0, imp.comprimido() );
							imp.say( 0, 0, "|" );
							imp.say( 0, 135, "|" );
							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( 0, "|" + linhaFina + "|" );
							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( 0, "| Ped." );
							imp.say( 10, "| Doc" );
							imp.say( 19, "|Ser" );
							imp.say( 24, "| Emissão" );
							imp.say( 35, "| Cliente" );
							imp.say( 60, "| Base" );
							imp.say( 71, "| ISS" );
							imp.say( 81, "| PIS" );
							imp.say( 91, "| Cofins" );
							imp.say( 101, "| IR" );
							imp.say( 111, "| C.SOCIAL" );
							imp.say( 121, "| V.LIQ" );
							imp.say( 135, "|" );
							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( 0, "|" + linhaFina + "|" );

						}

						if ( iMesAnt != Funcoes.sqlDateToGregorianCalendar( rs.getDate( "DtEmitVenda" ) ).get( Calendar.MONTH ) && iMesAnt >= 0 ) {

							impTotMes( imp );
							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( 0, "| Ped." );
							imp.say( 10, "| Doc" );
							imp.say( 19, "|Ser" );
							imp.say( 24, "| Emissão" );
							imp.say( 35, "| Cliente" );
							imp.say( 60, "| Base" );
							imp.say( 71, "| ISS" );
							imp.say( 81, "| PIS" );
							imp.say( 91, "| Cofins" );
							imp.say( 101, "| IR" );
							imp.say( 111, "| C.SOCIAL" );
							imp.say( 121, "| V.LIQ" );
							imp.say( 135, "|" );
							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( 0, "|" + linhaFina + "|" );

						}

						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|" + StringFunctions.strZero( rs.getString( "CodVenda" ), 7 ) );
						imp.say( 10, "|" + StringFunctions.strZero( rs.getString( "DocVenda" ), 7 ) );
						imp.say( 19, "|" + Funcoes.copy( rs.getString( "Serie" ), 4 ) );
						imp.say( 23, "|" + StringFunctions.sqlDateToStrDate( rs.getDate( "DtEmitVenda" ) ) );
						imp.say( 35, "|" + Funcoes.copy( rs.getInt( "CodCli" ) + "-" + rs.getString( "RazCli" ), 24 ) );
						imp.say( 60, "|" + Funcoes.strDecimalToStrCurrency( 10, 2, rs.getString( "VlrBaseIssVenda" ) ) );
						imp.say( 71, "|" + Funcoes.strDecimalToStrCurrency( 9, 2, rs.getString( "VlrIssVenda" ) ) );
						imp.say( 81, "|" + Funcoes.strDecimalToStrCurrency( 9, 2, rs.getString( "VlrPisVenda" ) ) );
						imp.say( 91, "|" + Funcoes.strDecimalToStrCurrency( 9, 2, rs.getString( "VlrCofinsVenda" ) ) );
						imp.say( 101, "|" + Funcoes.strDecimalToStrCurrency( 9, 2, rs.getString( "VlrIRVenda" ) ) );
						imp.say( 111, "|" + Funcoes.strDecimalToStrCurrency( 9, 2, rs.getString( "VlrCSocialVenda" ) ) );
						imp.say( 121, "|" + Funcoes.strDecimalToStrCurrency( 13, 2, rs.getString( "VlrLiqVenda" ) ) );
						imp.say( 135, "|" );

						iMesAnt = Funcoes.sqlDateToGregorianCalendar( rs.getDate( "DtEmitVenda" ) ).get( Calendar.MONTH );

						fTotMesBase += rs.getDouble( "VlrBaseIssVenda" );
						fTotMesISS += rs.getDouble( "VlrIssVenda" );
						fTotMesPIS += rs.getDouble( "VlrPisVenda" );
						fTotMesCOFINS += rs.getDouble( "VlrCofinsVenda" );
						fTotMesIR += rs.getDouble( "VlrIRVenda" );
						fTotMesCSocial += rs.getDouble( "VlrCSocialVenda" );
						fTotMesLiq += rs.getDouble( "VlrLiqVenda" );

					}

				}
				else {

					while ( rs.next() ) {
						if ( imp.pRow() >= ( iLinPag - 1 ) ) {
							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( 0, "+" + linhaFina + "+" );
							imp.incPags();
							imp.eject();
						}
						if ( imp.pRow() == 0 ) {
							imp.impCab( 136, true );
							imp.say( 0, imp.comprimido() );
							imp.say( 0, "|" );
							imp.say( 135, "|" );
							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( 0, "|" + linhaFina + "|" );
							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( 0, "| Mes/Ano" );
							imp.say( 10, "| Base" );
							imp.say( 28, "| ISS" );
							imp.say( 46, "| PIS" );
							imp.say( 64, "| COFINS" );
							imp.say( 82, "| IR" );
							imp.say( 100, "| C.SOCIAL" );
							imp.say( 118, "| Tot. Venda." );
							imp.say( 135, "|" );
							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( 0, "|" + linhaFina + "|" );

						}

						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "| " + StringFunctions.strZero( rs.getString( 2 ), 2 ) + "/" + StringFunctions.strZero( rs.getString( 1 ), 4 ) );
						imp.say( 10, "| " + Funcoes.strDecimalToStrCurrency( 15, 2, rs.getString( 3 ) ) );
						imp.say( 28, "| " + Funcoes.strDecimalToStrCurrency( 15, 2, rs.getString( 4 ) ) );
						imp.say( 46, "| " + Funcoes.strDecimalToStrCurrency( 15, 2, rs.getString( 5 ) ) );
						imp.say( 64, "| " + Funcoes.strDecimalToStrCurrency( 15, 2, rs.getString( 6 ) ) );
						imp.say( 82, "| " + Funcoes.strDecimalToStrCurrency( 15, 2, rs.getString( 7 ) ) );
						imp.say( 100, "| " + Funcoes.strDecimalToStrCurrency( 15, 2, rs.getString( 8 ) ) );
						imp.say( 118, "| " + Funcoes.strDecimalToStrCurrency( 15, 2, rs.getString( 9 ) ) );
						imp.say( 135, "|" );
					}
				}

				impTotMes( imp );
				impTotGeral( imp );

				imp.eject();
				imp.fechaGravacao();

				rs.close();
				ps.close();
				con.commit();

			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro executando a consulta.\n" + err.getMessage(), true, con, err );
				err.printStackTrace();
			}

			if ( bVisualizar==TYPE_PRINT.VIEW )
				imp.preview( this );
			else
				imp.print();

		} finally {
			sSql = null;
			imp = null;
			ps = null;
			rs = null;
		}

	}

	private void impTotMes( ImprimeOS imp ) {

		imp.pulaLinha( 1, imp.comprimido() );
		imp.say( 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
		imp.pulaLinha( 1, imp.comprimido() );
		imp.say( 0, "|" );
		imp.say( 24, "TOTAIS DO MES" );
		imp.say( 60, "|" + Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( fTotMesBase ) ) );
		imp.say( 71, "|" + Funcoes.strDecimalToStrCurrency( 9, 2, String.valueOf( fTotMesISS ) ) );
		imp.say( 81, "|" + Funcoes.strDecimalToStrCurrency( 9, 2, String.valueOf( fTotMesPIS ) ) );
		imp.say( 91, "|" + Funcoes.strDecimalToStrCurrency( 9, 2, String.valueOf( fTotMesCOFINS ) ) );
		imp.say( 101, "|" + Funcoes.strDecimalToStrCurrency( 9, 2, String.valueOf( fTotMesIR ) ) );
		imp.say( 111, "|" + Funcoes.strDecimalToStrCurrency( 9, 2, String.valueOf( fTotMesCSocial ) ) );
		imp.say( 121, "|" + Funcoes.strDecimalToStrCurrency( 13, 2, String.valueOf( fTotMesLiq ) ) );
		imp.say( 135, "|" );
		imp.pulaLinha( 1, imp.comprimido() );
		imp.say( 0, "|" + linhaFina + "|" );

		fTotBase += fTotMesBase;
		fTotISS += fTotMesISS;
		fTotPIS += fTotMesPIS;
		fTotCOFINS += fTotMesCOFINS;
		fTotIR += fTotMesIR;
		fTotCSocial += fTotMesCSocial;
		fTotLiq += fTotMesLiq;
		fTotMesBase = 0;
		fTotMesISS = 0;
		fTotMesPIS = 0;
		fTotMesCOFINS = 0;
		fTotMesIR = 0;
		fTotMesCSocial = 0;
		fTotMesLiq = 0;

	}

	private void impTotGeral( ImprimeOS imp ) throws SQLException {

		imp.pulaLinha( 1, imp.comprimido() );
		imp.say( 0, "|" + linhaLarga + "|" );
		imp.pulaLinha( 1, imp.comprimido() );
		imp.say( 0, "|" );
		imp.say( 24, "TOTAIS GERAIS" );
		imp.say( 60, "|" + Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( fTotBase ) ) );
		imp.say( 71, "|" + Funcoes.strDecimalToStrCurrency( 9, 2, String.valueOf( fTotISS ) ) );
		imp.say( 81, "|" + Funcoes.strDecimalToStrCurrency( 9, 2, String.valueOf( fTotPIS ) ) );
		imp.say( 91, "|" + Funcoes.strDecimalToStrCurrency( 9, 2, String.valueOf( fTotCOFINS ) ) );
		imp.say( 101, "|" + Funcoes.strDecimalToStrCurrency( 9, 2, String.valueOf( fTotIR ) ) );
		imp.say( 111, "|" + Funcoes.strDecimalToStrCurrency( 9, 2, String.valueOf( fTotCSocial ) ) );
		imp.say( 121, "|" + Funcoes.strDecimalToStrCurrency( 13, 2, String.valueOf( fTotLiq ) ) );
		imp.say( 135, "|" );
		imp.pulaLinha( 1, imp.comprimido() );
		imp.say( 0, "+" + linhaLarga + "+" );

	}
}
