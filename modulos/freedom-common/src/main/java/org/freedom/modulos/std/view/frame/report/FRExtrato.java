/**
 * @version 08/12/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FRExtrato.java <BR>
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
 *         Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.std.view.frame.report;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.AplicativoPD;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

public class FRExtrato extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodConta = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldFK txtDescConta = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private ListaCampos lcConta = new ListaCampos( this );

	private JRadioGroup<?, ?> rgTipoRel = null;

	public FRExtrato() {

		setTitulo( "Extrato" );
		setAtribos( 80, 80, 350, 250 );

		txtCodConta.setRequerido( true );
		lcConta.add( new GuardaCampo( txtCodConta, "NumConta", "Cód.conta", ListaCampos.DB_PK, false ) );
		lcConta.add( new GuardaCampo( txtDescConta, "DescConta", "Descrição da conta", ListaCampos.DB_SI, false ) );
		lcConta.montaSql( false, "CONTA", "FN" );
		lcConta.setReadOnly( true );
		txtCodConta.setTabelaExterna( lcConta, null );
		txtCodConta.setFK( true );
		txtCodConta.setNomeCampo( "NumConta" );

		Vector<String> vVals2 = new Vector<String>();
		Vector<String> vLabs2 = new Vector<String>();
		vVals2.addElement( "G" );
		vVals2.addElement( "T" );
		vLabs2.addElement( "Grafico" );
		vLabs2.addElement( "Texto" );
		rgTipoRel = new JRadioGroup<String, String>( 1, 2, vLabs2, vVals2 );
		rgTipoRel.setVlrString( "G" );

		JLabel periodo = new JLabel( "Período", SwingConstants.CENTER );
		periodo.setOpaque( true );
		adic( periodo, 25, 10, 80, 20 );
		JLabel borda = new JLabel();
		borda.setBorder( BorderFactory.createEtchedBorder() );
		adic( borda, 7, 20, 296, 45 );
		adic( txtDataini, 25, 35, 110, 20 );
		adic( new JLabel( "até", SwingConstants.CENTER ), 135, 35, 40, 20 );
		adic( txtDatafim, 175, 35, 110, 20 );

		adic( rgTipoRel, 7, 70, 295, 30 );
		adic( new JLabelPad( "Nº conta" ), 7, 100, 80, 20 );
		adic( txtCodConta, 7, 120, 90, 20 );
		adic( new JLabelPad( "Descrição da conta" ), 100, 100, 200, 20 );
		adic( txtDescConta, 100, 120, 200, 20 );

		GregorianCalendar cPeriodo = new GregorianCalendar();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataini.setVlrDate( cPeriodo.getTime() );
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcConta.setConexao( cn );
	}

	public boolean temAcessoConta() {

		StringBuilder sql = new StringBuilder();
		ResultSet rs = null;
		boolean ret = false;
		try {
			sql.append( "SELECT CO.NUMCONTA FROM FNCONTA CO " );
			sql.append( "WHERE CO.CODEMP=? AND CO.CODFILIAL=? AND CO.NUMCONTA=? AND " );
			sql.append( "( TUSUCONTA='S' OR EXISTS (SELECT * FROM FNCONTAUSU CU " );
			sql.append( "WHERE CU.CODEMP=CO.CODEMP AND CU.CODFILIAL=CO.CODFILIAL AND " );
			sql.append( "CU.NUMCONTA=CO.NUMCONTA AND CU.CODEMPUS=" + Aplicativo.iCodEmp );
			sql.append( " AND CU.CODFILIALUS=" + ListaCampos.getMasterFilial( "SGUSUARIO" ) );
			sql.append( " AND CU.IDUSU='" + Aplicativo.getUsuario().getIdusu() + "'))" );

			System.out.println( sql.toString() );

			PreparedStatement ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNCONTA" ) );
			ps.setString( 3, txtCodConta.getVlrString() );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				return true;
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return ret;

	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		if ( temAcessoConta() ) {

			String sCodConta = txtCodConta.getVlrString();
			ResultSet rs = null;

			if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
				Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
				return;
			}
			else if ( txtCodConta.getVlrString().equals( "" ) ) {
				Funcoes.mensagemInforma( this, "Número da conta é requerido!" );
				return;
			}

			StringBuilder sSQL = new StringBuilder();

			sSQL.append( "SELECT S.DATASL,L.HISTBLANCA,L.DOCLANCA,SL.VLRSUBLANCA,S.SALDOSL FROM FNSALDOLANCA S," );
			sSQL.append( "FNLANCA L,FNCONTA C, FNSUBLANCA SL WHERE L.FLAG IN " );
			sSQL.append( AplicativoPD.carregaFiltro( con, org.freedom.library.swing.frame.Aplicativo.iCodEmp ) );
			sSQL.append( " AND C.CODEMP=? AND C.CODFILIAL=? AND C.NUMCONTA=? " );
			sSQL.append( "AND L.CODEMP=? AND L.CODFILIAL=? AND L.CODLANCA=SL.CODLANCA " );
			sSQL.append( "AND S.CODPLAN=SL.CODPLAN AND S.CODEMP=SL.CODEMPPN AND S.CODFILIAL=SL.CODFILIALPN " );
			sSQL.append( "AND SL.DATASUBLANCA BETWEEN ? AND ? AND S.DATASL=SL.DATASUBLANCA " );
			sSQL.append( "AND SL.CODPLAN=C.CODPLAN AND SL.CODEMPPN=C.CODEMPPN AND SL.CODFILIALPN=C.CODFILIALPN " );
			sSQL.append( "AND SL.CODEMP=? AND SL.CODFILIAL=? ORDER BY S.DATASL,L.CODLANCA" );

			try {

				PreparedStatement ps = con.prepareStatement( sSQL.toString() );

				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "FNCONTA" ) );
				ps.setString( 3, sCodConta );
				ps.setInt( 4, Aplicativo.iCodEmp );
				ps.setInt( 5, ListaCampos.getMasterFilial( "FNLANCA" ) );
				ps.setDate( 6, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				ps.setDate( 7, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
				ps.setInt( 8, Aplicativo.iCodEmp );
				ps.setInt( 9, ListaCampos.getMasterFilial( "FNSUBLANCA" ) );

				rs = ps.executeQuery();

			} catch ( Exception e ) {

				e.printStackTrace();
				Funcoes.mensagemErro( this, "Erro ao buscar dados " + e.getMessage() );
			}

			if ( "T".equals( rgTipoRel.getVlrString() ) ) {

				imprimiTexto( rs, bVisualizar, "" );
			}
			else {
				imprimiGrafico( rs, bVisualizar, "Conta: " + txtCodConta.getVlrString() + " - " + txtDescConta.getVlrString() );
			}
		}
		else {
			Funcoes.mensagemInforma( this, "Você não possui acesso a essa conta!" );
		}

	}

	private BigDecimal buscaSaldoAnt() {

		BigDecimal bigRetorno = new BigDecimal( "0.00" );
		StringBuilder sSQL = new StringBuilder();

		sSQL.append( "SELECT S.SALDOSL FROM FNSALDOLANCA S, FNCONTA C " );
		sSQL.append( "WHERE C.NUMCONTA=? AND C.CODEMP=? AND C.CODFILIAL=? " );
		sSQL.append( "AND S.CODEMP=C.CODEMPPN AND S.CODFILIAL=C.CODFILIALPN " );
		sSQL.append( "AND S.CODPLAN=C.CODPLAN AND S.DATASL=" );
		sSQL.append( "(SELECT MAX(S1.DATASL) FROM FNSALDOLANCA S1 " );
		sSQL.append( "WHERE S1.DATASL < ? AND S1.CODPLAN=S.CODPLAN " );
		sSQL.append( "AND S1.CODEMP=S.CODEMP AND S1.CODFILIAL=S.CODFILIAL)" );

		try {

			PreparedStatement ps = con.prepareStatement( sSQL.toString() );
			ps.setString( 1, txtCodConta.getVlrString() );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "FNCONTA" ) );
			ps.setDate( 4, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ResultSet rs = ps.executeQuery();
			if ( rs.next() ) {
				bigRetorno = new BigDecimal( rs.getString( "SaldoSL" ) );
			}
			rs.close();
			ps.close();

		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar saldo anterior!\n" + e.getMessage(), true, con, e );
		}
		return bigRetorno;
	}

	public void imprimiTexto( ResultSet rs, TYPE_PRINT bVisualizar, String sCab ) {

		BigDecimal bTotal = new BigDecimal( "0" );
		BigDecimal bSaldo = new BigDecimal( "0" );
		BigDecimal bSaldoLinha = new BigDecimal( "0" );
		BigDecimal bVlrDeb = new BigDecimal( "0" );
		BigDecimal bVlrCred = new BigDecimal( "0" );
		BigDecimal bVlrTotDeb = new BigDecimal( "0" );
		BigDecimal bVlrTotCred = new BigDecimal( "0" );
		BigDecimal bAnt = buscaSaldoAnt();
		ImprimeOS imp = new ImprimeOS( "", con );
		String linhafina = StringFunctions.replicate( "-", 133 );
		String sDataLanca = "";
		String sConta = "";
		String sCodConta = txtCodConta.getVlrString();

		boolean bPrim = true;
		int linPag = imp.verifLinPag() - 1;

		try {

			imp.limpaPags();
			imp.setTitulo( "Extrato Bancário" );
			imp.addSubTitulo( "EXTRATO BANCÁRIO" );
			sConta = "CONTA: " + sCodConta + " - " + txtDescConta.getVlrString();
			imp.addSubTitulo( sConta );

			while ( rs.next() ) {

				if ( !bPrim ) {

					if ( ! ( sDataLanca.equals( rs.getString( "DataSL" ) ) ) ) {
						bTotal = new BigDecimal( rs.getString( "SaldoSL" ) );
					}
				}
				else {
					bTotal = new BigDecimal( rs.getString( "SaldoSL" ) );
				}

				if ( imp.pRow() == linPag ) {
					imp.eject();
					imp.incPags();
				}
				if ( imp.pRow() == 0 ) {

					imp.montaCab();
					imp.impCab( 136, true );

					imp.say( 0, imp.comprimido() );
					imp.say( 0, "|" + linhafina + "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "| Data" );
					imp.say( 14, "| Historico" );
					imp.say( 69, "| Doc" );
					imp.say( 82, "| Débito" );
					imp.say( 100, "| Crédito" );
					imp.say( 118, "| Saldo" );
					imp.say( 135, "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + linhafina + "|" );

					if ( bPrim ) {

						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|" );
						imp.say( 103, "Saldo Anterior: " + Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, bAnt.toString() ) );
						imp.say( 135, "|" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|" + linhafina + "|" );
						bSaldoLinha = new BigDecimal( bAnt.toString() );
					}
				}
				bPrim = false;
				bSaldo = new BigDecimal( rs.getBigDecimal( "SaldoSL" ).toString() );
				bAnt = bSaldo;
				sDataLanca = rs.getString( "DataSL" );
				bSaldoLinha = bSaldoLinha.add( rs.getBigDecimal( "VlrSubLanca" ) );

				if ( rs.getFloat( "VlrSubLanca" ) < 0 ) {
					bVlrDeb = new BigDecimal( rs.getBigDecimal( "VlrSubLanca" ).toString() ).abs();
					bVlrCred = new BigDecimal( "0.00" );
					bVlrTotDeb = bVlrTotDeb.add( new BigDecimal( bVlrDeb.toString() ) );
				}
				else {
					bVlrCred = new BigDecimal( rs.getBigDecimal( "VlrSubLanca" ).toString() );
					bVlrDeb = new BigDecimal( "0.00" );
					bVlrTotCred = bVlrTotCred.add( new BigDecimal( bVlrCred.toString() ) );
				}

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "| " + StringFunctions.sqlDateToStrDate( rs.getDate( "DataSL" ) ) );
				imp.say( 14, "| " + Funcoes.copy( rs.getString( "HistBLanca" ), 0, 50 ) );
				imp.say( 69, "| " + Funcoes.alinhaDir( rs.getString( "DocLanca" ), 10 ) );
				imp.say( 82, "|" );
				if ( bVlrDeb.floatValue() != 0 ) {
					imp.say( imp.pRow() + 0, 84, Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, bVlrDeb.toString() ) );
				}
				imp.say( 100, "|" );
				if ( bVlrCred.floatValue() != 0 ) {
					imp.say( 0, 102, Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, bVlrCred.toString() ) );
				}
				imp.say( 118, "|" + Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, bSaldoLinha.toString() ) );
				imp.say( 135, "|" );
			}
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "|" + linhafina + "|" );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "|" );
			imp.say( 82, "|" );
			imp.say( 84, Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, bVlrTotDeb.toString() ) );
			imp.say( 100, "|" );
			imp.say( 102, Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, bVlrTotCred.toString() ) );
			imp.say( 118, "|" );
			imp.say( 119, Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, bTotal.toString() ) );
			imp.say( 135, "|" );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "+" + linhafina + "+" );

			imp.eject();
			imp.fechaGravacao();

			rs.close();
			con.commit();

		} catch ( Exception e ) {

			e.printStackTrace();
		}
		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
	}

	private void imprimiGrafico( final ResultSet rs, final TYPE_PRINT bVisualizar, final String sCab ) {

		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();
		BigDecimal bAnt = buscaSaldoAnt();

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "FNCONTA" ) );
		hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
		hParam.put( "FILTROS", sCab );
		hParam.put( "SALDO_ANT", bAnt );

		dlGr = new FPrinterJob( "relatorios/Extrato.jasper", "Extrato de contas", sCab, rs, hParam, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de Extratos de contas!" + err.getMessage(), true, con, err );
			}
		}
	}
}
