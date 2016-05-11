/**
 * @version 11/02/2002 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FGeraFiscal.java <BR>
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

package org.freedom.modulos.std.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import org.freedom.bmps.Icone;
import org.freedom.infra.functions.ConversionFunctions;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.modulos.std.view.dialog.utility.DLChecaLFSaida;

public class FGeraFiscal extends FFilho implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCliente = new JPanelPad( 600, 110 );

	private JPanelPad pnGrid = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 2, 1 ) );

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 0, 10 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 0, 10 );

	private JCheckBoxPad cbEntrada = new JCheckBoxPad( "Entrada", "S", "N" );

	private JCheckBoxPad cbSaida = new JCheckBoxPad( "Saida", "S", "N" );

	private JTablePad tab1 = new JTablePad();

	private JScrollPane spnTab1 = new JScrollPane( tab1 );

	private JTablePad tab2 = new JTablePad();

	private JScrollPane spnTab2 = new JScrollPane( tab2 );

	private JProgressBar pbAnd = new JProgressBar();

	private JButtonPad btVisual = new JButtonPad( Icone.novo( "btPesquisa.png" ) );

	private JButtonPad btChecar = new JButtonPad( Icone.novo( "btExecuta.png" ) );

	private JButtonPad btGerar = new JButtonPad( Icone.novo( "btGerar.png" ) );

	private Timer tim = null;

	private int iAnd = 0;

	private int iTotCompras = 0;

	private int iTotVendas = 0;

	private JLabelPad lbAnd = new JLabelPad( "Aguardando:" );

	private enum EColSaida {
		DTEMIT, DTSAIDA, NATOPER, CODEMIT, UF, ESPECIE, MODNOTA, SERIE, DOC, DOCFIM, PERCICMS, PERCIPI, VLRCONTABIL, VLRBASEICMS, VLRICMS, VLRISENTAS, VLROUTRAS, VLRBASEIPI, VLRIPI, E1, F1, E2, F2, E3, F3, SIT, VLRBASEICMSST, VLRICMSST, VLRACESSORIAS
	}

	public FGeraFiscal() {

		super( false );
		setTitulo( "Gerar Informações Fiscais" );
		setAtribos( 50, 50, 610, 400 );

		btVisual.setToolTipText( "Visualizar" );
		btChecar.setToolTipText( "Checar" );
		btGerar.setToolTipText( "Gerar" );

		btChecar.setEnabled( false );
		btGerar.setEnabled( false );

		Container c = getContentPane();
		c.setLayout( new BorderLayout() );

		adicBotaoSair();

		c.add( pinCliente, BorderLayout.NORTH );
		c.add( pnGrid, BorderLayout.CENTER );

		txtDataini.setRequerido( true );
		txtDatafim.setRequerido( true );

		pinCliente.adic( new JLabelPad( "Inicio" ), 7, 0, 110, 25 );
		pinCliente.adic( txtDataini, 7, 20, 110, 20 );
		pinCliente.adic( new JLabelPad( "Fim" ), 120, 0, 107, 25 );
		pinCliente.adic( txtDatafim, 120, 20, 107, 20 );
		pinCliente.adic( btVisual, 230, 15, 30, 30 );
		pinCliente.adic( btChecar, 263, 15, 30, 30 );
		pinCliente.adic( btGerar, 296, 15, 30, 30 );
		pinCliente.adic( cbEntrada, 7, 50, 150, 20 );
		pinCliente.adic( cbSaida, 170, 50, 150, 20 );
		pinCliente.adic( lbAnd, 7, 80, 110, 20 );
		pinCliente.adic( pbAnd, 120, 80, 210, 20 );

		pnGrid.add( spnTab1 );
		pnGrid.add( spnTab2 );

		tab1.adicColuna( "Dt.emissão" );
		tab1.adicColuna( "Dt.entrada" );
		tab1.adicColuna( "Nat.oper." );
		tab1.adicColuna( "Cód.for." );
		tab1.adicColuna( "UF." );
		tab1.adicColuna( "Espécie" );
		tab1.adicColuna( "Mod.nota" );
		tab1.adicColuna( "Série" );
		tab1.adicColuna( "Doc." );
		tab1.adicColuna( "%Icms" );
		tab1.adicColuna( "%Ipi" );
		tab1.adicColuna( "V.contabil" );
		tab1.adicColuna( "V.base Icms" );
		tab1.adicColuna( "V.Icms" );
		tab1.adicColuna( "V.isentas" );
		tab1.adicColuna( "V.outras" );
		tab1.adicColuna( "V.base Ipi" );
		tab1.adicColuna( "V.Ipi" );
		tab1.adicColuna( "E1" );
		tab1.adicColuna( "F1" );
		tab1.adicColuna( "E2" );
		tab1.adicColuna( "F2" );
		tab1.adicColuna( "E3" );
		tab1.adicColuna( "F3" );
		tab1.adicColuna( "SIT" );

		tab1.setTamColuna( 100, 0 );
		tab1.setTamColuna( 100, 1 );
		tab1.setTamColuna( 70, 2 );
		tab1.setTamColuna( 70, 3 );
		tab1.setTamColuna( 40, 4 );
		tab1.setTamColuna( 70, 5 );
		tab1.setTamColuna( 80, 6 );
		tab1.setTamColuna( 50, 7 );
		tab1.setTamColuna( 70, 8 );
		tab1.setTamColuna( 70, 9 );
		tab1.setTamColuna( 70, 10 );
		tab1.setTamColuna( 100, 11 );
		tab1.setTamColuna( 100, 12 );
		tab1.setTamColuna( 100, 13 );
		tab1.setTamColuna( 100, 14 );
		tab1.setTamColuna( 100, 15 );
		tab1.setTamColuna( 100, 16 );
		tab1.setTamColuna( 100, 17 );
		tab1.setTamColuna( 20, 18 );
		tab1.setTamColuna( 20, 19 );
		tab1.setTamColuna( 20, 20 );
		tab1.setTamColuna( 20, 21 );
		tab1.setTamColuna( 20, 22 );
		tab1.setTamColuna( 20, 23 );
		tab1.setTamColuna( 20, 24 );

		tab2.adicColuna( "Dt.emissão" );
		tab2.adicColuna( "Dt.saída" );
		tab2.adicColuna( "Nat.oper." );
		tab2.adicColuna( "Cód.cli." );
		tab2.adicColuna( "UF." );
		tab2.adicColuna( "Espécie" );
		tab2.adicColuna( "Mod.nota" );
		tab2.adicColuna( "Série" );
		tab2.adicColuna( "Doc." );
		tab2.adicColuna( "Doc.Fim" );
		tab2.adicColuna( "%Icms" );
		tab2.adicColuna( "%Ipi" );
		tab2.adicColuna( "V.contabil" );
		tab2.adicColuna( "V.base Icms" );
		tab2.adicColuna( "V.Icms" );
		tab2.adicColuna( "V.isentas" );
		tab2.adicColuna( "V.outras" );
		tab2.adicColuna( "V.base Ipi" );
		tab2.adicColuna( "V.Ipi" );
		tab2.adicColuna( "E1" );
		tab2.adicColuna( "F1" );
		tab2.adicColuna( "E2" );
		tab2.adicColuna( "F2" );
		tab2.adicColuna( "E3" );
		tab2.adicColuna( "F3" );
		tab2.adicColuna( "SIT" );
		tab2.adicColuna( "Base ICMS ST" );
		tab2.adicColuna( "V.ICMS ST" );
		tab2.adicColuna( "V.Acessórias" ); // Frete , seguro, outras

		tab2.setTamColuna( 100, EColSaida.DTEMIT.ordinal() );
		tab2.setTamColuna( 100, EColSaida.DTSAIDA.ordinal() );
		tab2.setTamColuna( 70, EColSaida.NATOPER.ordinal() );
		tab2.setTamColuna( 70, EColSaida.CODEMIT.ordinal() );
		tab2.setTamColuna( 40, EColSaida.UF.ordinal() );
		tab2.setTamColuna( 70, EColSaida.ESPECIE.ordinal() );
		tab2.setTamColuna( 80, EColSaida.MODNOTA.ordinal() );
		tab2.setTamColuna( 50, EColSaida.SERIE.ordinal() );
		tab2.setTamColuna( 70, EColSaida.DOC.ordinal() );
		tab2.setTamColuna( 70, EColSaida.DOCFIM.ordinal() );
		tab2.setTamColuna( 70, EColSaida.PERCICMS.ordinal() );
		tab2.setTamColuna( 70, EColSaida.PERCIPI.ordinal() );
		tab2.setTamColuna( 100, EColSaida.VLRCONTABIL.ordinal() );
		tab2.setTamColuna( 100, EColSaida.VLRBASEICMS.ordinal() );
		tab2.setTamColuna( 100, EColSaida.VLRICMS.ordinal() );
		tab2.setTamColuna( 100, EColSaida.VLRISENTAS.ordinal() );
		tab2.setTamColuna( 100, EColSaida.VLROUTRAS.ordinal() );
		tab2.setTamColuna( 100, EColSaida.VLRBASEIPI.ordinal() );
		tab2.setTamColuna( 100, EColSaida.VLRIPI.ordinal() );
		tab2.setTamColuna( 20, EColSaida.E1.ordinal() );
		tab2.setTamColuna( 20, EColSaida.F1.ordinal() );
		tab2.setTamColuna( 20, EColSaida.E2.ordinal() );
		tab2.setTamColuna( 20, EColSaida.F2.ordinal() );
		tab2.setTamColuna( 20, EColSaida.E3.ordinal() );
		tab2.setTamColuna( 20, EColSaida.SIT.ordinal() );
		tab2.setTamColuna( 100, EColSaida.VLRBASEICMSST.ordinal() );
		tab2.setTamColuna( 100, EColSaida.VLRICMSST.ordinal() );
		tab2.setTamColuna( 100, EColSaida.VLRACESSORIAS.ordinal() );

		colocaMes();
		btVisual.addActionListener( this );
		btChecar.addActionListener( this );
		btGerar.addActionListener( this );

		pbAnd.setStringPainted( true );
	}

	private void colocaMes() {

		GregorianCalendar cData = new GregorianCalendar();
		GregorianCalendar cDataIni = new GregorianCalendar();
		GregorianCalendar cDataFim = new GregorianCalendar();
		cDataIni.set( Calendar.MONTH, cData.get( Calendar.MONTH ) - 1 );
		cDataIni.set( Calendar.DATE, 1 );
		cDataFim.set( Calendar.DATE, -1 );
		txtDataini.setVlrDate( cDataIni.getTime() );
		txtDatafim.setVlrDate( cDataFim.getTime() );

	}

	public void iniGerar() {

		Thread th = new Thread( new Runnable() {

			public void run() {

				gerar();
			}
		} );
		try {
			th.start();
		} catch ( Exception err ) {
			Funcoes.mensagemErro( this, "Não foi possível criar processo!\n" + err.getMessage(), true, con, err );
		}
	}

	private void visualizar() {

		if ( !valida() ) {
			return;
		}
		try {
			String sSQL = "SELECT C.DTEMITCOMPRA,C.DTENTCOMPRA,IT.CODNAT,C.CODFOR,F.UFFOR,TM.ESPECIETIPOMOV,TM.CODMODNOTA," 
				+ "C.SERIE,C.DOCCOMPRA,IT.PERCICMSITCOMPRA,IT.PERCIPIITCOMPRA,C.CODEMPFR,C.CODFILIALFR,IT.CODEMPNT,IT.CODFILIALNT," 
				+ "TM.CODEMPMN,TM.CODFILIALMN,"
				+ "coalesce(SUM(VLRPRODITCOMPRA),0),coalesce(SUM(IT.VLRBASEICMSITCOMPRA),0),SUM(IT.VLRICMSITCOMPRA),coalesce(SUM(IT.VLRISENTASITCOMPRA),0)," + "coalesce(SUM(IT.VLROUTRASITCOMPRA),0),coalesce(SUM(IT.VLRBASEIPIITCOMPRA),0),coalesce(SUM(IT.VLRIPIITCOMPRA),0), C.STATUSCOMPRA "
					
				+ "FROM CPCOMPRA C, CPITCOMPRA IT, CPFORNECED F, EQTIPOMOV TM "

				+ "WHERE C.DTENTCOMPRA BETWEEN ? AND ? AND C.CODEMP=? AND C.CODFILIAL=? AND " + "IT.CODCOMPRA=C.CODCOMPRA AND IT.CODEMP=C.CODEMP AND IT.CODFILIAL=C.CODFILIAL AND " + "F.CODFOR = C.CODFOR AND F.CODEMP=C.CODEMPFR AND F.CODFILIAL=C.CODFILIALFR AND "

				+ "TM.CODTIPOMOV=C.CODTIPOMOV AND TM.CODEMP=C.CODEMPTM AND TM.CODFILIAL=C.CODFILIALTM AND TM.FISCALTIPOMOV='S' "
				+ "GROUP BY C.DTEMITCOMPRA,C.DTENTCOMPRA,IT.CODNAT,C.CODFOR,F.UFFOR,TM.ESPECIETIPOMOV,TM.CODMODNOTA," + "C.SERIE,C.DOCCOMPRA,IT.PERCICMSITCOMPRA,IT.PERCIPIITCOMPRA,C.CODEMPFR,C.CODFILIALFR,IT.CODEMPNT,IT.CODFILIALNT," + "TM.CODEMPMN,TM.CODFILIALMN,C.STATUSCOMPRA";

			if ( cbEntrada.getVlrString().equals( "S" ) ) {
				PreparedStatement ps = con.prepareStatement( sSQL );
				ps.setDate( 1, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				ps.setDate( 2, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
				ps.setInt( 3, Aplicativo.iCodEmp );
				ps.setInt( 4, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
				ResultSet rs = ps.executeQuery();
				tab1.limpa();
				iTotCompras = 0;
				while ( rs.next() ) {
					tab1.adicLinha();

					tab1.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( 1 ) ), iTotCompras, 0 );
					tab1.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( 2 ) ), iTotCompras, 1 );
					tab1.setValor( ( rs.getString( 3 ) != null ? rs.getString( 3 ) : "" ), iTotCompras, 2 );
					tab1.setValor( "" + rs.getInt( 4 ), iTotCompras, 3 );
					tab1.setValor( ( rs.getString( 5 ) != null ? rs.getString( 5 ) : "" ), iTotCompras, 4 );
					tab1.setValor( ( rs.getString( 6 ) != null ? rs.getString( 6 ) : "" ), iTotCompras, 5 );
					tab1.setValor( "" + rs.getInt( 7 ), iTotCompras, 6 );
					tab1.setValor( ( rs.getString( 8 ) != null ? rs.getString( 8 ) : "" ), iTotCompras, 7 );
					tab1.setValor( "" + rs.getInt( 9 ), iTotCompras, 8 );
					tab1.setValor( Funcoes.strDecimalToStrCurrency( 6, 2, rs.getString( 10 ) != null ? rs.getString( 10 ) : "" ), iTotCompras, 9 );
					tab1.setValor( Funcoes.strDecimalToStrCurrency( 6, 2, rs.getString( 11 ) != null ? rs.getString( 11 ) : "" ), iTotCompras, 10 );
					tab1.setValor( Funcoes.strDecimalToStrCurrency( 15, 2, rs.getString( 18 ) != null ? rs.getString( 18 ) : "" ), iTotCompras, 11 );
					tab1.setValor( Funcoes.strDecimalToStrCurrency( 15, 2, rs.getString( 19 ) != null ? rs.getString( 19 ) : "" ), iTotCompras, 12 );
					tab1.setValor( Funcoes.strDecimalToStrCurrency( 15, 2, rs.getString( 20 ) != null ? rs.getString( 20 ) : "" ), iTotCompras, 13 );
					tab1.setValor( Funcoes.strDecimalToStrCurrency( 15, 2, rs.getString( 21 ) != null ? rs.getString( 21 ) : "" ), iTotCompras, 14 );
					tab1.setValor( Funcoes.strDecimalToStrCurrency( 15, 2, rs.getString( 22 ) != null ? rs.getString( 22 ) : "" ), iTotCompras, 15 );
					tab1.setValor( Funcoes.strDecimalToStrCurrency( 15, 2, rs.getString( 23 ) != null ? rs.getString( 23 ) : "" ), iTotCompras, 16 );
					tab1.setValor( Funcoes.strDecimalToStrCurrency( 15, 2, rs.getString( 24 ) != null ? rs.getString( 24 ) : "" ), iTotCompras, 17 );
					tab1.setValor( "" + rs.getInt( 12 ), iTotCompras, 18 );
					tab1.setValor( "" + rs.getInt( 13 ), iTotCompras, 19 );
					tab1.setValor( "" + rs.getInt( 14 ), iTotCompras, 20 );
					tab1.setValor( "" + rs.getInt( 15 ), iTotCompras, 21 );
					tab1.setValor( "" + rs.getInt( 16 ), iTotCompras, 22 );
					tab1.setValor( "" + rs.getInt( 17 ), iTotCompras, 23 );
					tab1.setValor( "" + rs.getString( 25 ), iTotCompras, 24 );
					iTotCompras++;
				}

				// rs.close();
				// ps.close();

				con.commit();

			}

			if ( cbSaida.getVlrString().equals( "S" ) ) {

				sSQL = "SELECT V.TIPOVENDA, V.DTEMITVENDA,V.DTSAIDAVENDA,IV.CODNAT,"
						+ "V.CODCLI,C.UFCLI,TM.ESPECIETIPOMOV,TM.CODMODNOTA,V.SERIE,V.DOCVENDA, V.DOCVENDA DOCFIM, IV.PERCICMSITVENDA, "
						+ "IV.PERCICMSITVENDA ALIQLFISC, IV.PERCIPIITVENDA,V.CODEMPCL,V.CODFILIALCL,"
						+ "IV.CODEMPNT,IV.CODFILIALNT,TM.CODEMPMN,TM.CODFILIALMN,SUM(IV.VLRLIQITVENDA) VLRCONTABILITVENDA,"
						+ "SUM(IV.VLRBASEICMSITVENDA) VLRBASEICMSITVENDA,SUM(IV.VLRICMSITVENDA) VLRICMSITVENDA, "
						+ "SUM(IV.VLRISENTASITVENDA) VLRISENTASITVENDA, SUM(IV.VLROUTRASITVENDA) VLROUTRASITVENDA, "
						+ "SUM(IV.VLRBASEIPIITVENDA) VLRBASEIPIITVENDA, SUM(IV.VLRIPIITVENDA) VLRIPIITVENDA, "

						+ "V.STATUSVENDA,V.VLRBASEICMSSTVENDA,V.VLRICMSSTVENDA,FT.ADICFRETEVD,FT.VLRFRETEVD " // Informações para o registro 53

						+ "FROM EQTIPOMOV TM, VDCLIENTE C, EQPRODUTO P, VDITVENDA IV "
						
						+ "LEFT OUTER JOIN VDVENDA V ON V.codemp=iv.codemp and v.codfilial=iv.codfilial and v.codvenda=iv.codvenda and v.tipovenda=iv.tipovenda "
						
						+ "LEFT OUTER JOIN VDFRETEVD FT ON "
						+ "FT.CODEMP=V.CODEMP AND FT.CODFILIAL=V.CODFILIAL AND FT.TIPOVENDA=V.TIPOVENDA AND FT.CODVENDA=V.CODVENDA "

						+ "left outer join LFITCLFISCAL CF on "
						+ "CF.CODEMP=IV.CODEMPIF AND CF.CODFILIAL=IV.CODFILIALIF AND CF.CODFISC=IV.CODFISC AND CF.CODITFISC=IV.CODITFISC "
						
						+ "WHERE V.DTEMITVENDA BETWEEN ? AND ? AND V.CODEMP=? AND V.CODFILIAL=? AND V.TIPOVENDA='V' AND "
						+ "IV.TIPOVENDA=V.TIPOVENDA AND IV.CODVENDA=V.CODVENDA AND IV.CODEMP=V.CODEMP AND IV.CODFILIAL=V.CODFILIAL AND "
						+ "TM.CODTIPOMOV=V.CODTIPOMOV AND TM.CODEMP=V.CODEMPTM AND TM.CODFILIAL=V.CODFILIALTM AND TM.FISCALTIPOMOV='S' AND "
						+ "P.CODEMP=IV.CODEMPPD AND P.CODFILIAL=IV.CODFILIALPD AND P.CODPROD=IV.CODPROD AND "
						
						+ "C.CODCLI=V.CODCLI AND C.CODEMP=V.CODEMPCL AND C.CODFILIAL=V.CODFILIALCL "

						+ "GROUP BY V.TIPOVENDA, V.DTEMITVENDA,V.DTSAIDAVENDA,IV.CODNAT,"
						+ "V.CODCLI,C.UFCLI,TM.ESPECIETIPOMOV,TM.CODMODNOTA,V.SERIE,V.DOCVENDA, V.DOCVENDA, IV.PERCICMSITVENDA, "
						+ "IV.PERCICMSITVENDA, IV.PERCIPIITVENDA,V.CODEMPCL,V.CODFILIALCL,IV.CODEMPNT,IV.CODFILIALNT,"
						+ "TM.CODEMPMN,TM.CODFILIALMN,V.STATUSVENDA,V.VLRBASEICMSSTVENDA,V.VLRICMSSTVENDA,FT.ADICFRETEVD,FT.VLRFRETEVD "

						+ "UNION ALL " // ECF

						+ "SELECT V.TIPOVENDA,V.DTEMITVENDA, V.DTSAIDAVENDA, IV.CODNAT," + "V.CODCLI, C.UFCLI, TM.ESPECIETIPOMOV, TM.CODMODNOTA," + "V.SERIE, L.PRIMCUPOMLX DOCVENDA, L.ULTCUPOMLX DOCFIM,IV.PERCICMSITVENDA, cast(coalesce(CF.ALIQLFISC,0) as decimal(15,5)) aliqfisc, IV.PERCIPIITVENDA,"
						+ "V.CODEMPCL,V.CODFILIALCL,IV.CODEMPNT,IV.CODFILIALNT,TM.CODEMPMN,TM.CODFILIALMN," + "SUM(IV.VLRLIQITVENDA) VLRCONTABILITVENDA,SUM(CASE WHEN (CF.TIPOFISC='TT' AND (CF.REDFISC<=0 OR CF.REDFISC IS NULL)) " + "THEN IV.VLRLIQITVENDA WHEN CF.REDFISC>0 "
						+ "THEN (IV.VLRLIQITVENDA)-((IV.VLRLIQITVENDA)*CF.REDFISC/100) ELSE NULL END ) VLRBASEICMSITVENDA, " + "SUM(CASE WHEN CF.TIPOFISC='TT' AND (CF.REDFISC=0 OR CF.REDFISC IS NULL) " + "THEN IV.VLRLIQITVENDA*IV.PERCICMSITVENDA/100  WHEN CF.REDFISC>0 "
						+ "THEN ((IV.VLRLIQITVENDA)-(IV.VLRLIQITVENDA*CF.REDFISC/100))* CF.ALIQLFISC/100 ELSE NULL END) VLRICMSITVENDA, " + "SUM(CASE WHEN (CF.TIPOFISC='II' AND ( CF.REDFISC<=0 OR CF.REDFISC IS NULL)) THEN IV.VLRLIQITVENDA WHEN CF.REDFISC>0 "
						+ "THEN (IV.VLRLIQITVENDA)-(CASE WHEN (CF.TIPOFISC='TT' AND (CF.REDFISC<=0 OR CF.REDFISC IS NULL)) " + "THEN IV.VLRLIQITVENDA WHEN CF.REDFISC>0 THEN (IV.VLRLIQITVENDA)-((IV.VLRLIQITVENDA)*CF.REDFISC/100) "
						+ "ELSE NULL END ) ELSE NULL  END) AS VLRISENTASITVENDA, SUM(CASE WHEN (CF.TIPOFISC IN ('NN','FF')) THEN IV.VLRLIQITVENDA " + "WHEN (CF.TIPOFISC IN ('II','TT')) OR (CF.REDFISC>0) OR (CF.REDFISC IS NOT NULL) "
						+ "THEN NULL ELSE IV.VLRLIQITVENDA END) VLROUTRASITVENDA, SUM(IV.VLRBASEIPIITVENDA) VLRBASEIPIITVENDA, " + "SUM(IV.VLRIPIITVENDA) VLRIPIITVENDA, V.STATUSVENDA, V.VLRBASEICMSSTVENDA,V.VLRICMSSTVENDA,FT.ADICFRETEVD,FT.VLRFRETEVD "

						+ "FROM  VDITVENDA  IV, EQPRODUTO P, VDCLIENTE C, EQTIPOMOV TM, LFITCLFISCAL CF, VDVENDA V " + "LEFT OUTER JOIN PVLEITURAX L ON L.CODEMP=V.CODEMPCX AND L.CODFILIAL=V.CODFILIALCX AND " + "L.CODCAIXA=V.CODCAIXA AND L.DTLX=V.DTEMITVENDA " + "LEFT OUTER JOIN VDFRETEVD FT ON "
						+ "FT.CODEMP=V.CODEMP AND FT.CODFILIAL=V.CODFILIAL AND FT.TIPOVENDA=V.TIPOVENDA AND FT.CODVENDA=V.CODVENDA "

						+ "WHERE TM.CODEMP=V.CODEMPTM AND TM.CODFILIAL=V.CODFILIALTM AND TM.CODTIPOMOV=V.CODTIPOMOV AND " + "TM.FISCALTIPOMOV='S' AND V.CODEMP=? AND V.CODFILIAL=? AND "
						+ "SUBSTRING(V.STATUSVENDA FROM 1 FOR 1)<>'C' AND V.TIPOVENDA='E' AND V.DTEMITVENDA BETWEEN ? AND ? AND IV.CODEMP=V.CODEMP AND " + "IV.CODVENDA=V.CODVENDA AND ( IV.CANCITVENDA IS NULL OR IV.CANCITVENDA<>'S' ) AND "
						+ "P.CODEMP=IV.CODEMPPD AND P.CODFILIAL=IV.CODFILIALPD AND P.CODPROD=IV.CODPROD AND " + "CF.CODEMP=IV.CODEMPIF AND CF.CODFILIAL=IV.CODFILIALIF AND CF.CODFISC=IV.CODFISC AND CF.CODITFISC=IV.CODITFISC AND CF.GERALFISC='S' AND "
						+ "C.CODEMP=V.CODEMPCL AND C.CODFILIAL=V.CODFILIALCL AND C.CODCLI=V.CODCLI " + "GROUP BY V.TIPOVENDA,V.DTEMITVENDA, V.DTSAIDAVENDA, IV.CODNAT, V.CODCLI, C.UFCLI, TM.ESPECIETIPOMOV, TM.CODMODNOTA, "
						+ "V.SERIE, L.PRIMCUPOMLX, L.ULTCUPOMLX, IV.PERCICMSITVENDA, CF.ALIQLFISC, IV.PERCIPIITVENDA, " + "V.CODEMPCL,V.CODFILIALCL,IV.CODEMPNT,IV.CODFILIALNT,TM.CODEMPMN,TM.CODFILIALMN, V.STATUSVENDA, " + "V.VLRBASEICMSSTVENDA,V.VLRICMSSTVENDA,FT.ADICFRETEVD,FT.VLRFRETEVD "
						+ "ORDER BY 2,10";

				System.out.println("SQL gerafiscal:" + sSQL.toString());
				
				PreparedStatement ps2 = con.prepareStatement( sSQL );
				ps2.setDate( 1, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				ps2.setDate( 2, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
				ps2.setInt( 3, Aplicativo.iCodEmp );
				ps2.setInt( 4, ListaCampos.getMasterFilial( "VDVENDA" ) );
				ps2.setInt( 5, Aplicativo.iCodEmp );
				ps2.setInt( 6, ListaCampos.getMasterFilial( "VDVENDA" ) );
				ps2.setDate( 7, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				ps2.setDate( 8, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

				ResultSet rs2 = ps2.executeQuery();
				iTotVendas = 0;
				tab2.limpa();

				while ( rs2.next() ) {
					tab2.adicLinha();
					tab2.setValor( StringFunctions.sqlDateToStrDate( rs2.getDate( "DTEMITVENDA" ) ), iTotVendas, EColSaida.DTEMIT.ordinal() );
					tab2.setValor( StringFunctions.sqlDateToStrDate( rs2.getDate( "DTSAIDAVENDA" ) ), iTotVendas, EColSaida.DTSAIDA.ordinal() );
					tab2.setValor( rs2.getString( "CODNAT" ) != null ? rs2.getString( "CODNAT" ) : "", iTotVendas, EColSaida.NATOPER.ordinal() );
					tab2.setValor( "" + rs2.getInt( "CODCLI" ), iTotVendas, EColSaida.CODEMIT.ordinal() );
					tab2.setValor( rs2.getString( "UFCLI" ) != null ? rs2.getString( "UFCLI" ) : "", iTotVendas, EColSaida.UF.ordinal() );
					tab2.setValor( rs2.getString( "ESPECIETIPOMOV" ) != null ? rs2.getString( "ESPECIETIPOMOV" ) : "", iTotVendas, EColSaida.ESPECIE.ordinal() );
					tab2.setValor( "" + rs2.getInt( "CODMODNOTA" ), iTotVendas, EColSaida.MODNOTA.ordinal() );
					tab2.setValor( rs2.getString( "SERIE" ) != null ? rs2.getString( "SERIE" ) : "", iTotVendas, EColSaida.SERIE.ordinal() );
					tab2.setValor( "" + rs2.getInt( "DOCVENDA" ), iTotVendas, EColSaida.DOC.ordinal() );
					tab2.setValor( "" + rs2.getInt( "DOCFIM" ), iTotVendas, EColSaida.DOCFIM.ordinal() );
					tab2.setValor( Funcoes.strDecimalToStrCurrency( 6, 2, rs2.getString( "PERCICMSITVENDA" ) ), iTotVendas, EColSaida.PERCICMS.ordinal() );
					tab2.setValor( Funcoes.strDecimalToStrCurrency( 6, 2, rs2.getString( "PERCIPIITVENDA" ) ), iTotVendas, EColSaida.PERCIPI.ordinal() );
					tab2.setValor( Funcoes.strDecimalToStrCurrency( 15, 2, rs2.getString( "VLRCONTABILITVENDA" ) ), iTotVendas, EColSaida.VLRCONTABIL.ordinal() );
					tab2.setValor( Funcoes.strDecimalToStrCurrency( 15, 2, rs2.getString( "VLRBASEICMSITVENDA" ) ), iTotVendas, EColSaida.VLRBASEICMS.ordinal() );
					tab2.setValor( Funcoes.strDecimalToStrCurrency( 15, 2, rs2.getString( "VLRICMSITVENDA" ) ), iTotVendas, EColSaida.VLRICMS.ordinal() );
					tab2.setValor( Funcoes.strDecimalToStrCurrency( 15, 2, rs2.getString( "VLRISENTASITVENDA" ) ), iTotVendas, EColSaida.VLRISENTAS.ordinal() );
					tab2.setValor( Funcoes.strDecimalToStrCurrency( 15, 2, rs2.getString( "VLROUTRASITVENDA" ) ), iTotVendas, EColSaida.VLROUTRAS.ordinal() );
					tab2.setValor( Funcoes.strDecimalToStrCurrency( 15, 2, rs2.getString( "VLRBASEIPIITVENDA" ) ), iTotVendas, EColSaida.VLRBASEIPI.ordinal() );
					tab2.setValor( Funcoes.strDecimalToStrCurrency( 15, 2, rs2.getString( "VLRIPIITVENDA" ) ), iTotVendas, EColSaida.VLRIPI.ordinal() );
					tab2.setValor( "" + rs2.getInt( "CODEMPCL" ), iTotVendas, EColSaida.E1.ordinal() );
					tab2.setValor( "" + rs2.getInt( "CODFILIALCL" ), iTotVendas, EColSaida.F1.ordinal() );
					tab2.setValor( "" + rs2.getInt( "CODEMPNT" ), iTotVendas, EColSaida.E2.ordinal() );
					tab2.setValor( "" + rs2.getInt( "CODFILIALNT" ), iTotVendas, EColSaida.F2.ordinal() );
					tab2.setValor( "" + rs2.getInt( "CODEMPMN" ), iTotVendas, EColSaida.E3.ordinal() );
					tab2.setValor( "" + rs2.getInt( "CODFILIALMN" ), iTotVendas, EColSaida.F3.ordinal() );
					tab2.setValor( "" + rs2.getString( "STATUSVENDA" ), iTotVendas, EColSaida.SIT.ordinal() );

					tab2.setValor( Funcoes.strDecimalToStrCurrency( 15, 2, rs2.getString( "VLRBASEICMSSTVENDA" ) ), iTotVendas, EColSaida.VLRBASEICMSST.ordinal() );

					tab2.setValor( Funcoes.strDecimalToStrCurrency( 15, 2, rs2.getString( "VLRICMSSTVENDA" ) ), iTotVendas, EColSaida.VLRICMSST.ordinal() );

					Boolean adicfrete = "S".equals( rs2.getString( "ADICFRETEVD" ) );
					String vlracessorias = "0";

					if ( adicfrete ) {
						vlracessorias = rs2.getString( "VLRFRETEVD" );

					}

					tab2.setValor( Funcoes.strDecimalToStrCurrency( 15, 2, vlracessorias ), iTotVendas, EColSaida.VLRACESSORIAS.ordinal() );

					iTotVendas++;

					// return;

				}
				// rs2.close();
				// ps2.close();
				con.commit();
			}
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao realizar consulta!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
			return;
		}
		btGerar.setEnabled( false );
		if ( ( iTotVendas > 0 ) | ( iTotCompras > 0 ) ) {
			btChecar.setEnabled( true );
		}
	}

	private void gerar() {

		if ( ( iTotVendas + iTotCompras ) <= 0 ) {
			btGerar.setEnabled( false );
			return;
		}
		int iQuant = 0;
		iAnd = 0;
		String sSql = "";
		try {
			if ( iTotCompras > 0 ) {
				sSql = "SELECT COUNT(*) FROM LFLIVROFISCAL WHERE TIPOLF='E' AND DTESLF BETWEEN ? AND ? AND CODEMP=? AND CODFILIAL=? ";
				PreparedStatement psA = con.prepareStatement( sSql );
				psA.setDate( 1, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				psA.setDate( 2, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
				psA.setInt( 3, Aplicativo.iCodEmp );
				psA.setInt( 4, ListaCampos.getMasterFilial( "LFLIVROFISCAL" ) );
				ResultSet rsA = psA.executeQuery();
				iQuant = 0;
				if ( rsA.next() ) {
					iQuant = rsA.getInt( 1 );
				}
				;
				// rsA.close();
				// psA.close();
				con.commit();
				if ( iQuant > 0 ) {
					sSql = "DELETE FROM LFLIVROFISCAL WHERE TIPOLF='E' AND DTESLF BETWEEN ? AND ? AND CODEMP=? AND CODFILIAL=?";
					PreparedStatement psB = con.prepareStatement( sSql );
					psB.setDate( 1, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
					psB.setDate( 2, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
					psB.setInt( 3, Aplicativo.iCodEmp );
					psB.setInt( 4, ListaCampos.getMasterFilial( "LFLIVROFISCAL" ) );
					psB.executeUpdate();
					// psB.close();
					con.commit();
				}

			}
			// Livros fiscais de saída
			if ( iTotVendas > 0 ) {
				sSql = "SELECT COUNT(*) FROM LFLIVROFISCAL WHERE TIPOLF='S' AND DTEMITLF BETWEEN ? AND ? AND CODEMP=? AND CODFILIAL=?";
				PreparedStatement psC = con.prepareStatement( sSql );
				psC.setDate( 1, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				psC.setDate( 2, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
				psC.setInt( 3, Aplicativo.iCodEmp );
				psC.setInt( 4, ListaCampos.getMasterFilial( "LFLIVROFISCAL" ) );
				ResultSet rsC = psC.executeQuery();
				iQuant = 0;
				if ( rsC.next() ) {
					iQuant = rsC.getInt( 1 );
				}
				;
				// rsC.close();
				// psC.close();
				con.commit();
				if ( iQuant > 0 ) {
					sSql = "DELETE FROM LFLIVROFISCAL WHERE TIPOLF='S' AND DTEMITLF BETWEEN ? AND ?";
					PreparedStatement psD = con.prepareStatement( sSql );
					psD.setDate( 1, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
					psD.setDate( 2, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
					psD.executeUpdate();
					// psD.close();
					con.commit();
				}
			}

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Consulta não foi executada\n" + err.getMessage(), true, con, err );
			return;
		}

		tim = new Timer( 300, this );
		pbAnd.setMinimum( 0 );
		// System.out.println("V: "+iTotVendas+" | C: "+iTotCompras+"\n");
		pbAnd.setMaximum( iTotVendas + iTotCompras );
		tim.start();
		lbAnd.setText( "Gerando..." );

		sSql = "INSERT INTO LFLIVROFISCAL ( CODEMP,CODFILIAL,TIPOLF,ANOMESLF,CODLF,CODEMITLF,SERIELF," + "DOCINILF,DTEMITLF,DTESLF,CODNAT,DOCFIMLF,ESPECIELF,UFLF,VLRCONTABILLF," + "VLRBASEICMSLF,ALIQICMSLF,VLRICMSLF,VLRISENTASICMSLF,VLROUTRASICMSLF,"
				+ "VLRBASEIPILF,ALIQIPILF,VLRIPILF,VLRISENTASIPILF,VLROUTRASIPILF,CODMODNOTA," + "CODEMPET,CODFILIALET,CODEMPNT,CODFILIALNT,CODEMPMN,CODFILIALMN,SITUACAOLF," + "VLRBASEICMSSTLF,VLRICMSSTLF,VLRACESSORIASLF"
				+ ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		PreparedStatement psI;

		for ( int i = 0; i < iTotCompras; i++ ) {
			try {
				psI = con.prepareStatement( sSql );
				psI.setInt( 1, Aplicativo.iCodEmp );
				psI.setInt( 2, ListaCampos.getMasterFilial( "LFLIVROFISCAL" ) );
				psI.setString( 3, "E" );
				psI.setString( 4, ( ( "" + tab1.getValor( i, 1 ) ).substring( 6, 10 ) + ( "" + tab1.getValor( i, 1 ) ).substring( 3, 5 ) ).trim() );
				psI.setInt( 5, i );
				psI.setInt( 6, ( tab1.getValor( i, 3 ) + "" ).equals( "" ) ? 0 : Integer.parseInt( ( tab1.getValor( i, 3 ) + "" ) ) );
				psI.setString( 7, tab1.getValor( i, 7 ) + "" );
				psI.setInt( 8, ( tab1.getValor( i, 8 ) + "" ).equals( "" ) ? 0 : Integer.parseInt( ( tab1.getValor( i, 8 ) + "" ) ) );
				psI.setDate( 9, Funcoes.strDateToSqlDate( tab1.getValor( i, 0 ) + "" ) );
				psI.setDate( 10, Funcoes.strDateToSqlDate( tab1.getValor( i, 1 ) + "" ) );
				psI.setString( 11, tab1.getValor( i, 2 ) + "" );
				psI.setInt( 12, ( tab1.getValor( i, 8 ) + "" ).equals( "" ) ? 0 : Integer.parseInt( ( tab1.getValor( i, 8 ) + "" ) ) );
				psI.setString( 13, Funcoes.adicionaEspacos( tab1.getValor( i, 5 ) + "", 3 ) );
				psI.setString( 14, tab1.getValor( i, 4 ) + "" );
				psI.setBigDecimal( 15, ConversionFunctions.stringCurrencyToBigDecimal( tab1.getValor( i, 11 ) + "" ) ); // VLRCONTABIL
				psI.setBigDecimal( 16, ConversionFunctions.stringCurrencyToBigDecimal( tab1.getValor( i, 12 ) + "" ) ); // VLRBASEICMS
				psI.setBigDecimal( 17, ConversionFunctions.stringCurrencyToBigDecimal( tab1.getValor( i, 9 ) + "" ) ); // ALIQICMS
				psI.setBigDecimal( 18, ConversionFunctions.stringCurrencyToBigDecimal( tab1.getValor( i, 13 ) + "" ) ); // VLRICMS
				psI.setBigDecimal( 19, ConversionFunctions.stringCurrencyToBigDecimal( tab1.getValor( i, 14 ) + "" ) ); // VLRISENTAS ICMS
				psI.setBigDecimal( 20, ConversionFunctions.stringCurrencyToBigDecimal( tab1.getValor( i, 15 ) + "" ) ); // VLROUTRAS ICMS
				psI.setBigDecimal( 21, ConversionFunctions.stringCurrencyToBigDecimal( tab1.getValor( i, 16 ) + "" ) ); // VLRBASEIPI
				psI.setBigDecimal( 22, ConversionFunctions.stringCurrencyToBigDecimal( tab1.getValor( i, 10 ) + "" ) ); // ALIQIPI
				psI.setBigDecimal( 23, ConversionFunctions.stringCurrencyToBigDecimal( tab1.getValor( i, 17 ) + "" ) ); // VLRIPI
				psI.setBigDecimal( 24, ConversionFunctions.stringCurrencyToBigDecimal( "0" ) ); // VLRISENTAS IPI
				psI.setBigDecimal( 25, ConversionFunctions.stringCurrencyToBigDecimal( "0" ) ); // VLRIOUTRAS IPI
				psI.setInt( 26, ( tab1.getValor( i, 6 ) + "" ).equals( "" ) ? 0 : Integer.parseInt( ( tab1.getValor( i, 6 ) + "" ) ) ); // MODELO DE NOTA FISCAL
				if ( ( ! ( tab1.getValor( i, 18 ) + "" ).equals( "" ) ) || ( ! ( tab1.getValor( i, 18 ) + "" ).equals( "0" ) ) )
					psI.setInt( 27, Integer.parseInt( tab1.getValor( i, 18 ) + "" ) ); // CODEMPET
				else
					psI.setNull( 27, Types.INTEGER );
				if ( ( ! ( tab1.getValor( i, 19 ) + "" ).equals( "" ) ) || ( ! ( tab1.getValor( i, 19 ) + "" ).equals( "0" ) ) )
					psI.setInt( 28, Integer.parseInt( tab1.getValor( i, 19 ) + "" ) ); // CODFILIALET
				else
					psI.setNull( 28, Types.INTEGER );
				if ( ( ! ( tab1.getValor( i, 20 ) + "" ).equals( "" ) ) || ( ! ( tab1.getValor( i, 20 ) + "" ).equals( "0" ) ) )
					psI.setInt( 29, Integer.parseInt( tab1.getValor( i, 20 ) + "" ) ); // CODEMPNT
				else
					psI.setNull( 29, Types.INTEGER );
				if ( ( ! ( tab1.getValor( i, 21 ) + "" ).equals( "" ) ) || ( ! ( tab1.getValor( i, 21 ) + "" ).equals( "0" ) ) )
					psI.setInt( 30, Integer.parseInt( tab1.getValor( i, 21 ) + "" ) ); // CODFILIALNT
				else
					psI.setNull( 30, Types.INTEGER );
				if ( ( ! ( tab1.getValor( i, 22 ) + "" ).equals( "" ) ) || ( ! ( tab1.getValor( i, 22 ) + "" ).equals( "0" ) ) )
					psI.setInt( 31, Integer.parseInt( tab1.getValor( i, 22 ) + "" ) ); // CODEMPMN
				else
					psI.setNull( 31, Types.INTEGER );
				if ( ( ! ( tab1.getValor( i, 23 ) + "" ).equals( "" ) ) || ( ! ( tab1.getValor( i, 23 ) + "" ).equals( "0" ) ) )
					psI.setInt( 32, Integer.parseInt( tab1.getValor( i, 23 ) + "" ) ); // CODFILIALMN
				else
					psI.setNull( 32, Types.INTEGER );

				if ( "X".equals( tab1.getValor( i, 24 ).toString().substring( 0, 1 ) ) ) { // SITUACAOLF
					psI.setString( 33, "S" ); // Status para documento cancelado no sintegra.
				}
				else {
					psI.setString( 33, "N" ); // Status para documento normal no sintegra.
				}

				psI.setBigDecimal( 34, ConversionFunctions.stringCurrencyToBigDecimal( "0" ) );

				psI.setBigDecimal( 35, ConversionFunctions.stringCurrencyToBigDecimal( "0" ) );

				psI.setBigDecimal( 36, ConversionFunctions.stringCurrencyToBigDecimal( "0" ) );

				psI.executeUpdate();

				con.commit();

			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro gerando livros fiscais de compras!\n" + err.getMessage(), true, con, err );
				err.printStackTrace();
				break;
			}
			iAnd++;
			// System.out.println("And1:"+iAnd+"\n");
		}
		;

		for ( int i = 0; i < iTotVendas; i++ ) {
			try {
				psI = con.prepareStatement( sSql );
				psI.setInt( 1, Aplicativo.iCodEmp );
				psI.setInt( 2, ListaCampos.getMasterFilial( "LFLIVROFISCAL" ) );
				psI.setString( 3, "S" );
				psI.setString( 4, ( ( "" + tab2.getValor( i, EColSaida.DTEMIT.ordinal() ) ).substring( 6, 10 ) + ( "" + tab2.getValor( i, EColSaida.DTEMIT.ordinal() ) ).substring( 3, 5 ) ).trim() );
				psI.setInt( 5, i );
				psI.setInt( 6, ( tab2.getValor( i, EColSaida.CODEMIT.ordinal() ) + "" ).equals( "" ) ? 0 : Integer.parseInt( ( tab2.getValor( i, EColSaida.CODEMIT.ordinal() ) + "" ) ) );
				psI.setString( 7, tab2.getValor( i, EColSaida.SERIE.ordinal() ) + "" );
				psI.setInt( 8, ( tab2.getValor( i, EColSaida.DOC.ordinal() ) + "" ).equals( "" ) ? 0 : Integer.parseInt( ( tab2.getValor( i, EColSaida.DOC.ordinal() ) + "" ) ) );
				psI.setDate( 9, Funcoes.strDateToSqlDate( tab2.getValor( i, EColSaida.DTEMIT.ordinal() ) + "" ) );
				psI.setDate( 10, Funcoes.strDateToSqlDate( tab2.getValor( i, EColSaida.DTSAIDA.ordinal() ) + "" ) );
				psI.setString( 11, tab2.getValor( i, EColSaida.NATOPER.ordinal() ) + "" );
				psI.setInt( 12, ( tab2.getValor( i, EColSaida.DOCFIM.ordinal() ) + "" ).equals( "" ) ? 0 : Integer.parseInt( ( tab2.getValor( i, EColSaida.DOCFIM.ordinal() ) + "" ) ) );

				psI.setString( 13, Funcoes.adicionaEspacos( tab2.getValor( i, EColSaida.ESPECIE.ordinal() ) + "", 3 ) );
				psI.setString( 14, tab2.getValor( i, EColSaida.UF.ordinal() ) + "" );
				psI.setBigDecimal( 15, ConversionFunctions.stringCurrencyToBigDecimal( tab2.getValor( i, EColSaida.VLRCONTABIL.ordinal() ) + "" ) ); // VLRCONTABIL
				psI.setBigDecimal( 16, ConversionFunctions.stringCurrencyToBigDecimal( tab2.getValor( i, EColSaida.VLRBASEICMS.ordinal() ) + "" ) ); // VLRBASEICMS
				psI.setBigDecimal( 17, ConversionFunctions.stringCurrencyToBigDecimal( tab2.getValor( i, EColSaida.PERCICMS.ordinal() ) + "" ) ); // ALIQICMS
				psI.setBigDecimal( 18, ConversionFunctions.stringCurrencyToBigDecimal( tab2.getValor( i, EColSaida.VLRICMS.ordinal() ) + "" ) ); // VLRICMS
				psI.setBigDecimal( 19, ConversionFunctions.stringCurrencyToBigDecimal( tab2.getValor( i, EColSaida.VLRISENTAS.ordinal() ) + "" ) ); // VLRISENTAS ICMS
				psI.setBigDecimal( 20, ConversionFunctions.stringCurrencyToBigDecimal( tab2.getValor( i, EColSaida.VLROUTRAS.ordinal() ) + "" ) ); // VLROUTRAS ICMS
				psI.setBigDecimal( 21, ConversionFunctions.stringCurrencyToBigDecimal( tab2.getValor( i, EColSaida.VLRBASEIPI.ordinal() ) + "" ) ); // VLRBASEIPI
				psI.setBigDecimal( 22, ConversionFunctions.stringCurrencyToBigDecimal( tab2.getValor( i, EColSaida.PERCIPI.ordinal() ) + "" ) ); // ALIQIPI
				psI.setBigDecimal( 23, ConversionFunctions.stringCurrencyToBigDecimal( tab2.getValor( i, EColSaida.VLRIPI.ordinal() ) + "" ) ); // VLRIPI
				psI.setBigDecimal( 24, ConversionFunctions.stringCurrencyToBigDecimal( "0" ) ); // VLRISENTAS IPI
				psI.setBigDecimal( 25, ConversionFunctions.stringCurrencyToBigDecimal( "0" ) ); // VLRIOUTRAS IPI
				psI.setInt( 26, ( tab2.getValor( i, EColSaida.MODNOTA.ordinal() ) + "" ).equals( "" ) ? 0 : Integer.parseInt( ( tab2.getValor( i, EColSaida.MODNOTA.ordinal() ) + "" ) ) ); // MODELO DE NOTA FISCAL
				if ( ( ! ( tab2.getValor( i, EColSaida.E1.ordinal() ) + "" ).equals( "" ) ) || ( ! ( tab2.getValor( i, EColSaida.E1.ordinal() ) + "" ).equals( "0" ) ) )
					psI.setInt( 27, Integer.parseInt( tab2.getValor( i, EColSaida.E1.ordinal() ) + "" ) ); // CODEMPET
				else
					psI.setNull( 27, Types.INTEGER );
				if ( ( ! ( tab2.getValor( i, EColSaida.F1.ordinal() ) + "" ).equals( "" ) ) || ( ! ( tab2.getValor( i, EColSaida.F1.ordinal() ) + "" ).equals( "0" ) ) )
					psI.setInt( 28, Integer.parseInt( tab2.getValor( i, EColSaida.F1.ordinal() ) + "" ) ); // CODFILIALET
				else
					psI.setNull( 28, Types.INTEGER );
				if ( ( ! ( tab2.getValor( i, EColSaida.E2.ordinal() ) + "" ).equals( "" ) ) || ( ! ( tab2.getValor( i, EColSaida.E2.ordinal() ) + "" ).equals( "0" ) ) )
					psI.setInt( 29, Integer.parseInt( tab2.getValor( i, EColSaida.E2.ordinal() ) + "" ) ); // CODEMPNT
				else
					psI.setNull( 29, Types.INTEGER );
				if ( ( ! ( tab2.getValor( i, EColSaida.F2.ordinal() ) + "" ).equals( "" ) ) || ( ! ( tab2.getValor( i, EColSaida.F2.ordinal() ) + "" ).equals( "0" ) ) )
					psI.setInt( 30, Integer.parseInt( tab2.getValor( i, EColSaida.F2.ordinal() ) + "" ) ); // CODFILIALNT
				else
					psI.setNull( 30, Types.INTEGER );
				if ( ( ! ( tab2.getValor( i, EColSaida.E3.ordinal() ) + "" ).equals( "" ) ) || ( ! ( tab2.getValor( i, EColSaida.E3.ordinal() ) + "" ).equals( "0" ) ) )
					psI.setInt( 31, Integer.parseInt( tab2.getValor( i, EColSaida.E3.ordinal() ) + "" ) ); // CODEMPMN
				else
					psI.setNull( 31, Types.INTEGER );
				if ( ( ! ( tab2.getValor( i, EColSaida.F3.ordinal() ) + "" ).equals( "" ) ) || ( ! ( tab2.getValor( i, EColSaida.F3.ordinal() ) + "" ).equals( "0" ) ) )
					psI.setInt( 32, Integer.parseInt( tab2.getValor( i, EColSaida.F3.ordinal() ) + "" ) ); // CODFILIALMN
				else
					psI.setNull( 32, Types.INTEGER );

				if ( "C".equals( tab2.getValor( i, EColSaida.SIT.ordinal() ).toString().substring( 0, 1 ) ) ) { // SITUACAOLF
					psI.setString( 33, "S" ); // Status para documento cancelado no sintegra.
				}
				else {
					psI.setString( 33, "N" ); // Status para documento normal no sintegra.
				}

				psI.setBigDecimal( 34, ConversionFunctions.stringCurrencyToBigDecimal( tab2.getValor( i, EColSaida.VLRBASEICMSST.ordinal() ) + "" ) ); // BASE ICMSST
				psI.setBigDecimal( 35, ConversionFunctions.stringCurrencyToBigDecimal( tab2.getValor( i, EColSaida.VLRICMSST.ordinal() ) + "" ) ); // ICMSST
				psI.setBigDecimal( 36, ConversionFunctions.stringCurrencyToBigDecimal( tab2.getValor( i, EColSaida.VLRACESSORIAS.ordinal() ) + "" ) ); // BASE ICMSST

				psI.executeUpdate();

				con.commit();
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro gerando livros fiscais de compras!\n" + err.getMessage(), true, con, err );
				err.printStackTrace();
				break;
			}
			iAnd++;
			// System.out.println("And:"+iAnd+"\n");
		}
		;
		tim.stop();
		pbAnd.setValue( iAnd );
		pbAnd.updateUI();
		lbAnd.setText( "Pronto." );
		btGerar.setEnabled( false );
	}

	private boolean checar() {

		boolean bRetorno = false;
		int iTotErros = 0;
		DLChecaLFSaida dl = new DLChecaLFSaida();
		String sSql = "SELECT V1.CODVENDA,V1.SERIE," + "V1.DOCVENDA,V1.DTEMITVENDA " + "FROM VDVENDA V1,EQTIPOMOV TM " + " WHERE V1.DTEMITVENDA BETWEEN ? AND ? AND V1.CODEMP=? AND V1.CODFILIAL=? AND " + " TM.CODTIPOMOV=V1.CODTIPOMOV AND TM.CODEMP=V1.CODEMPTM AND "
				+ " TM.CODFILIAL=V1.CODFILIALTM AND " + " TM.FISCALTIPOMOV='S' AND " + " (SELECT COUNT(*) from VDVENDA V3 " + " WHERE V3.DOCVENDA=V1.DOCVENDA AND V3.CODEMP=V1.CODEMP AND V3.SERIE=V1.SERIE AND " + " V3.CODFILIAL=V1.CODFILIAL AND V3.TIPOVENDA=V1.TIPOVENDA AND "
				+ " V3.DTEMITVENDA BETWEEN ? AND ? AND V3.CODTIPOMOV=V1.CODTIPOMOV AND " + " ( V3.TIPOVENDA<>'E' OR V3.CODEMPCX=V1.CODEMPCX AND " + " V3.CODFILIALCX=V1.CODFILIALCX AND V3.CODCAIXA=V1.CODCAIXA ))>1 " + " ORDER BY V1.CODVENDA,V1.SERIE,V1.DOCVENDA";
		try {

			PreparedStatement psChec = con.prepareStatement( sSql );
			psChec.setDate( 1, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			psChec.setDate( 2, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			psChec.setInt( 3, Aplicativo.iCodEmp );
			psChec.setInt( 4, ListaCampos.getMasterFilial( "VDVENDA" ) );
			psChec.setDate( 5, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			psChec.setDate( 6, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			ResultSet rsChec = psChec.executeQuery();
			iTotErros = 0;
			dl.tab.limpa();
			while ( rsChec.next() ) {
				dl.tab.adicLinha();
				dl.tab.setValor( "" + rsChec.getInt( 1 ), iTotErros, 0 );
				dl.tab.setValor( rsChec.getString( 2 ), iTotErros, 1 );
				dl.tab.setValor( "" + rsChec.getInt( 3 ), iTotErros, 2 );
				dl.tab.setValor( StringFunctions.sqlDateToStrDate( rsChec.getDate( 4 ) ), iTotErros, 3 );
				dl.tab.setValor( "Numeração de NF. repetida", iTotErros, 4 );
				iTotErros++;
			}

			if ( iTotErros > 0 ) {
				btGerar.setEnabled( false );
				dl.setVisible( true );
			}
			else {
				bRetorno = true;
				btGerar.setEnabled( true );
			}

			// rsChec.close();
			// psChec.close();
			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao realizar consulta!\n" + err.getMessage(), true, con, err );
			bRetorno = false;
		}

		return bRetorno;
	}

	private boolean valida() {

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return false;
		}
		else if ( ( cbEntrada.getVlrString() != "S" ) & ( cbSaida.getVlrString() != "S" ) ) {
			Funcoes.mensagemInforma( this, "Nenhuma operação foi selecionada!" );
			return false;
		}
		return true;
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == tim ) {
			// System.out.println("Atualizando\n");
			pbAnd.setValue( iAnd + 1 );
			pbAnd.updateUI();
		}
		else if ( evt.getSource() == btGerar ) {
			iniGerar();
		}
		else if ( evt.getSource() == btChecar ) {
			checar();
		}
		else if ( evt.getSource() == btVisual ) {
			visualizar();
		}
	}
}
