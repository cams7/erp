/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FSintegra.java <BR>
 * 
 *                    Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                    modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                    na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                    Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                    sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                    Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                    Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                    de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                    Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.lvf.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import org.freedom.bmps.Icone;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;

public class FSintegra extends FFilho implements ActionListener {

	protected static final String ISO88591 = "ISO8859_1";

	private static final long serialVersionUID = 1L;

	// private FileWriter fwSintegra;

	private FileOutputStream fosSintegra;

	private OutputStreamWriter oswSintegra;

	private String sFileName = "";

	private String sCnpjEmp = "";

	private String sInscEmp = "";

	private JPanelPad pinCliente = new JPanelPad( 700, 490 );

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JCheckBoxPad cbEntrada = new JCheckBoxPad( "Entrada", "S", "N" );

	private JCheckBoxPad cbSaida = new JCheckBoxPad( "Saida", "S", "N" );

	private JCheckBoxPad cbInventario = new JCheckBoxPad( "Inventário", "S", "N" );

	private JCheckBoxPad cbConsumidor = new JCheckBoxPad( "NF Consumidor", "S", "N" );

	private JCheckBoxPad cbFrete = new JCheckBoxPad( "Frete", "S", "N" );

	private JButtonPad btGerar = new JButtonPad( Icone.novo( "btGerar.png" ) );

	private String CR = "" + ( (char) 13 ) + "" + ( (char) 10 );

	private JRadioGroup<?, ?> rgConvenio;

	private JRadioGroup<?, ?> rgNatoper;

	private JRadioGroup<?, ?> rgFinalidade;

	private Vector<String> vLabConvenio = new Vector<String>();

	private Vector<String> vValConvenio = new Vector<String>();

	private Vector<String> vLabNatoper = new Vector<String>();

	private Vector<String> vValNatoper = new Vector<String>();

	private Vector<String> vLabFinalidade = new Vector<String>();

	private Vector<String> vValFinalidade = new Vector<String>();

	private JLabelPad lbAnd = new JLabelPad();

	private int iCodEmp = Aplicativo.iCodEmp;

	private String sUsaRefProd = "N";

	private boolean contribipi = false;

	private Vector<String> vProd75 = new Vector<String>();

	StringBuffer sBuffer75 = new StringBuffer();

	public FSintegra() {

		super( false );
		setTitulo( "Exportar Sintegra" );
		setAtribos( 50, 20, 710, 470 );

		btGerar.setToolTipText( "Exporta arquivo" );

		Container c = getContentPane();
		c.setLayout( new BorderLayout() );

		adicBotaoSair();

		c.add( pinCliente, BorderLayout.CENTER );

		txtDataini.setTipo( JTextFieldPad.TP_DATE, 0, 10 );
		txtDatafim.setTipo( JTextFieldPad.TP_DATE, 0, 10 );
		txtDataini.setRequerido( true );
		txtDatafim.setRequerido( true );

		vLabConvenio.addElement( "Convênio 57/95" );
		vLabConvenio.addElement( "Convênio 69/02" );
		vLabConvenio.addElement( "Convênio 57/95 (a partir de Jan.2004)" );
		vValConvenio.addElement( "1" );
		vValConvenio.addElement( "2" );
		vValConvenio.addElement( "3" );

		rgConvenio = new JRadioGroup<String, String>( 3, 1, vLabConvenio, vValConvenio );
		rgConvenio.setVlrString( "3" );

		vLabNatoper.addElement( "Interestaduais - Somente operações sujeitas ao regime de substituição tributária" );
		vLabNatoper.addElement( "Interestaduais - Operações com ou sem substituição tributária" );
		vLabNatoper.addElement( "Totalidade das operações do informante" );
		vValNatoper.addElement( "1" );
		vValNatoper.addElement( "2" );
		vValNatoper.addElement( "3" );
		rgNatoper = new JRadioGroup<String, String>( 3, 3, vLabNatoper, vValNatoper );
		rgNatoper.setVlrString( "3" );

		vLabFinalidade.addElement( "Normal" );
		vLabFinalidade.addElement( "Retificação total de arquivo" );
		vLabFinalidade.addElement( "Retificação aditíva de arquivo" );
		vLabFinalidade.addElement( "Retificação corretiva de arquivo" );
		vLabFinalidade.addElement( "Desfaziamento" );
		vValFinalidade.addElement( "1" );
		vValFinalidade.addElement( "2" );
		vValFinalidade.addElement( "3" );
		vValFinalidade.addElement( "4" );
		vValFinalidade.addElement( "5" );
		rgFinalidade = new JRadioGroup<String, String>( 5, 5, vLabFinalidade, vValFinalidade );

		pinCliente.adic( new JLabelPad( "Inicio" ), 7, 0, 110, 25 );
		pinCliente.adic( txtDataini, 7, 20, 110, 20 );
		pinCliente.adic( new JLabelPad( "Fim" ), 120, 0, 107, 25 );
		pinCliente.adic( txtDatafim, 120, 20, 107, 20 );
		pinCliente.adic( btGerar, 296, 15, 30, 30 );
		pinCliente.adic( cbEntrada, 7, 50, 100, 20 );

		pinCliente.adic( cbSaida, 130, 50, 100, 20 );
		pinCliente.adic( cbConsumidor, 253, 50, 150, 20 );
		pinCliente.adic( cbInventario, 430, 50, 100, 20 );
		pinCliente.adic( cbFrete, 590, 50, 100, 20 );

		pinCliente.adic( rgConvenio, 7, 80, 680, 80 );
		pinCliente.adic( rgNatoper, 7, 170, 680, 80 );
		pinCliente.adic( rgFinalidade, 7, 260, 680, 110 );

		pinCliente.adic( lbAnd, 7, 380, 680, 20 );
		lbAnd.setForeground( Color.BLUE );
		lbAnd.setText( "Aguardando..." );
		colocaMes();
		btGerar.addActionListener( this );

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

	/*
	 * private void colocaTrimestre() { int iMesAtual = 0; GregorianCalendar cData = new GregorianCalendar(); GregorianCalendar cDataIni = new GregorianCalendar(); GregorianCalendar cDataFim = new GregorianCalendar(); iMesAtual = cData.get(Calendar.MONTH)+1; if (iMesAtual < 4) { cDataIni = new
	 * GregorianCalendar(cData.get(Calendar.YEAR)-1,10-1,1); cDataFim = new GregorianCalendar(cData.get(Calendar.YEAR)-1,12-1,31); } else if ((iMesAtual > 3) & (iMesAtual < 7)) { cDataIni = new GregorianCalendar(cData.get(Calendar.YEAR),1-1,1); cDataFim = new
	 * GregorianCalendar(cData.get(Calendar.YEAR),3-1,31); } else if ((iMesAtual > 6) & (iMesAtual < 10)) { cDataIni = new GregorianCalendar(cData.get(Calendar.YEAR),4-1,1); cDataFim = new GregorianCalendar(cData.get(Calendar.YEAR),6-1,30); } else if (iMesAtual > 9) { cDataIni = new
	 * GregorianCalendar(cData.get(Calendar.YEAR),7-1,1); cDataFim = new GregorianCalendar(cData.get(Calendar.YEAR),9-1,30); } txtDataini.setVlrDate(cDataIni.getTime()); txtDatafim.setVlrDate(cDataFim.getTime()); }
	 */

	public void iniGerar() {

		Thread th = new Thread( new Runnable() {

			public void run() {

				gerar();
			}
		} );

		try {
			th.start();
		} catch ( Exception err ) {
			Funcoes.mensagemInforma( this, "Não foi possível criar processo!\n" + err.getMessage() );
		}

	}

	private void gerar() {

		String sSql = "";
		String serieecf = "";
		StringBuffer sBuffer = new StringBuffer();
		int iTot50 = 0;
		int iTot51 = 0;
		int iTot53 = 0;
		int iTot54 = 0;
		int iTot60 = 0;
		int iTot61 = 0;
		int iTot70 = 0;
		int iTot74 = 0;
		int iTot75 = 0;
		int iTotreg = 0;
		int iCodEmp = 0;

		iCodEmp = Aplicativo.iCodEmp;

		if ( !valida() ) {
			return;
		}

		if ( sFileName.trim().equals( "" ) ) {
			Funcoes.mensagemInforma( this, "Selecione o arquivo!" );
			return;
		}

		// System.setProperty(SUN_JNU_ENCODING, ISO88591);
		// System.setProperty(FILE_ENCODING, ISO88591);

		File fSintegra = new File( sFileName );

		if ( fSintegra.exists() ) {
			if ( Funcoes.mensagemConfirma( this, "Arquivo: '" + sFileName + "' já existe! Deseja sobrescrever?" ) != 0 ) {
				return;
			}
		}

		try {
			fSintegra.createNewFile();
		} catch ( IOException err ) {
			Funcoes.mensagemErro( this, "Erro limpando arquivo: " + sFileName + "\n" + err.getMessage(), true, con, err );
			return;
		}

		try {
			// fwSintegra = new FileWriter( fSintegra );
			fosSintegra = new FileOutputStream( fSintegra );
			oswSintegra = new OutputStreamWriter( fosSintegra, ISO88591 );

		} catch ( IOException ioError ) {
			Funcoes.mensagemErro( this, "Erro Criando o arquivo: " + sFileName + "\n" + ioError.getMessage() );
			return;
		}

		btGerar.setEnabled( false );

		// REGISTRO TIPO 1 HEADER DO ARQUIVO

		PreparedStatement ps;
		ResultSet rs;

		try {

			serieecf = getSerieECF();

			sSql = "SELECT P.USAREFPROD,F.CONTRIBIPIFILIAL FROM SGPREFERE1 P, SGFILIAL F " + "WHERE P.CODEMP=? AND P.CODFILIAL=? AND F.CODEMP=P.CODEMP AND F.CODFILIAL=P.CODFILIAL ";
			ps = con.prepareStatement( sSql );
			ps.setInt( 1, iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			rs = ps.executeQuery();
			if ( rs.next() ) {

				sUsaRefProd = rs.getString( "USAREFPROD" );
				contribipi = "S".equals( rs.getString( "CONTRIBIPIFILIAL" ) );

				if ( sUsaRefProd == null )
					sUsaRefProd = "N";
			}

			con.commit();

		} catch ( SQLException sqlErr ) {
			sqlErr.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao consultar preferencias.\n" + sqlErr.getMessage() );
		}

		geraRegistro10e11();

		iTot50 = geraRegistro50( serieecf ); // ICMS

		if ( contribipi ) {
			iTot51 = geraRegistro51( serieecf ); // IPI
		}

		iTot53 = geraRegistro53( serieecf ); // Substituição Tributária
		iTot54 = geraRegistro54( serieecf ); // Produto
		iTot60 = geraRegistro60(); // PDV
		iTot61 = geraRegistro61( serieecf ); // Saídas
		iTot70 = geraRegistro70(); // Conhecimentos de Frete;
		iTot75 = geraRegistro75(); // Código de produtos - Deve ser gerado antes, pois gera informações para uso no registro 74
		iTot74 = geraRegistro74(); // 

		iTotreg = iTot50 + +iTot51 + iTot53 + iTot54 + iTot60 + iTot61 + iTot70 + iTot74 + iTot75 + 3;

		if ( iTot50 > 0 )
			sBuffer.append( retorna90( sBuffer.toString(), "50", iTot50 ) );

		if ( iTot51 > 0 )
			sBuffer.append( retorna90( sBuffer.toString(), "51", iTot51 ) );

		if ( iTot53 > 0 )
			sBuffer.append( retorna90( sBuffer.toString(), "53", iTot53 ) );

		if ( iTot54 > 0 )
			sBuffer.append( retorna90( sBuffer.toString(), "54", iTot54 ) );

		if ( iTot60 > 0 )
			sBuffer.append( retorna90( sBuffer.toString(), "60", iTot60 ) );

		if ( iTot61 > 0 )
			sBuffer.append( retorna90( sBuffer.toString(), "61", iTot61 ) );

		if ( iTot70 > 0 )
			sBuffer.append( retorna90( sBuffer.toString(), "70", iTot70 ) );

		if ( iTot74 > 0 )
			sBuffer.append( retorna90( sBuffer.toString(), "74", iTot74 ) );

		if ( iTot75 > 0 )
			sBuffer.append( retorna90( sBuffer.toString(), "75", iTot75 ) );

		sBuffer.append( "99" );
		sBuffer.append( StringFunctions.strZero( String.valueOf( iTotreg ), 8 ) );
		sBuffer.append( StringFunctions.replicate( " ", 125 - sBuffer.length() ) + "1" + CR );
		gravaBuffer( sBuffer.toString() );

		try {
			oswSintegra.close();
		} catch ( IOException ioError ) {
			ioError.printStackTrace();
			Funcoes.mensagemInforma( this, "Fechando o arquivo: " + sFileName + "\n" + ioError.getMessage() );
		}

		Funcoes.mensagemInforma( this, "Arquivo exportado!" );
		lbAnd.setText( "Pronto." );
		btGerar.setEnabled( true );

	}

	private void geraRegistro10e11() {

		PreparedStatement ps;
		ResultSet rs;
		StringBuffer sSql = new StringBuffer();
		StringBuffer sBuffer = new StringBuffer();
		String sConvenio = rgConvenio.getVlrString();
		String sNatoper = rgNatoper.getVlrString();
		String sFinalidade = rgFinalidade.getVlrString();

		try {

			sSql.append( "SELECT F.CODEMP,F.CNPJFILIAL,F.INSCFILIAL,F.RAZFILIAL,M.NOMEMUNIC,F.SIGLAUF,F.FAXFILIAL," );
			sSql.append( "F.BAIRFILIAL,F.ENDFILIAL,F.NUMFILIAL,E.NOMECONTEMP,F.FONEFILIAL,F.CEPFILIAL " );
			sSql.append( "FROM SGFILIAL F,SGEMPRESA E, SGMUNICIPIO M WHERE " );
			sSql.append( "E.CODEMP=F.CODEMP AND F.CODEMP=? AND F.CODFILIAL=? " );
			sSql.append( "AND F.CODMUNIC=M.CODMUNIC AND F.CODPAIS=M.CODPAIS AND F.SIGLAUF=M.SIGLAUF " );

			ps = con.prepareStatement( sSql.toString() );
			ps.setInt( 1, iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			rs = ps.executeQuery();

			if ( rs.next() ) {

				sBuffer.delete( 0, sBuffer.length() );

				sCnpjEmp = Funcoes.adicionaEspacos( rs.getString( "CNPJFILIAL" ), 14 );
				sInscEmp = Funcoes.adicionaEspacos( StringFunctions.clearString( rs.getString( "INSCFILIAL" ) ), 14 );

				sBuffer.append( "10" );
				sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "CNPJFILIAL" ), 14 ) );
				sBuffer.append( Funcoes.adicionaEspacos( StringFunctions.clearString( rs.getString( "INSCFILIAL" ) ), 14 ) );
				sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "RAZFILIAL" ), 35 ) );
				sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "NOMEMUNIC" ), 30 ) );
				sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "SIGLAUF" ), 2 ) );
				sBuffer.append( StringFunctions.strZero( rs.getString( "FAXFILIAL" ), 10 ) );
				sBuffer.append( Funcoes.dataAAAAMMDD( txtDataini.getVlrDate() ) );
				sBuffer.append( Funcoes.dataAAAAMMDD( txtDatafim.getVlrDate() ) );
				sBuffer.append( sConvenio );
				sBuffer.append( sNatoper );
				sBuffer.append( sFinalidade + CR );

				sBuffer.append( "11" );
				sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "ENDFILIAL" ), 34 ) );
				sBuffer.append( StringFunctions.strZero( String.valueOf( rs.getInt( "NUMFILIAL" ) ), 5 ) );
				sBuffer.append( StringFunctions.replicate( " ", 22 ) );
				sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "BAIRFILIAL" ), 15 ) );
				sBuffer.append( StringFunctions.strZero( rs.getString( "CEPFILIAL" ), 8 ) );
				sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "NOMECONTEMP" ), 28 ) );
				sBuffer.append( StringFunctions.strZero( StringFunctions.clearString( rs.getString( "FONEFILIAL" ) ), 12 ) + CR );

				gravaBuffer( sBuffer.toString() );

			}
			rs.close();
			ps.close();
			con.commit();

		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao gerar registro 10 e 11!\n" + e.getMessage(), true, con, e );
		}

	}

	private int geraRegistro50( String serieecf ) {

		PreparedStatement ps;
		String cnpjcli = "";
		String insccli = "";
		String cnpjfor = "";
		String cpffor = "";
		String inscfor = "";

		ResultSet rs;
		StringBuffer sSql = new StringBuffer();
		StringBuffer sBuffer = new StringBuffer();
		String sConvenio = rgConvenio.getVlrString();
		int cont = 0;

		try {

			if ( "S".equals( cbEntrada.getVlrString() ) ) {
				// REGISTRO 50 LIVROS FISCAIS DE ENTRADA
				sSql.append( "SELECT LF.TIPOLF,LF.ANOMESLF,LF.ESPECIELF,LF.DOCINILF," );
				sSql.append( "LF.DOCFIMLF,LF.SERIELF,LF.CODEMITLF,LF.UFLF,LF.DTEMITLF," );
				sSql.append( "LF.DTESLF," );
				sSql.append( "SUM(LF.VLRCONTABILLF) AS VLRCONTABILLF," );
				sSql.append( "SUM(LF.VLRBASEICMSLF) AS VLRBASEICMSLF," );
				sSql.append( "LF.ALIQICMSLF," );
				sSql.append( "SUM(LF.VLRICMSLF) AS VLRICMSLF," );
				sSql.append( "SUM(LF.VLRISENTASICMSLF) AS VLRISENTASICMSLF," );
				sSql.append( "SUM(LF.VLROUTRASICMSLF) AS VLROUTRASICMSLF," );
				sSql.append( "LF.CODNAT,LF.CODMODNOTA,F.CNPJFOR,f.cpffor, F.INSCFOR,LF.SITUACAOLF, F.PESSOAFOR " );
				sSql.append( "FROM LFLIVROFISCAL LF,CPFORNECED F " );
				sSql.append( "WHERE LF.DTESLF BETWEEN ? AND ? AND " );
				sSql.append( "LF.CODEMP=? AND LF.CODFILIAL=? AND " );
				sSql.append( "F.CODFOR=LF.CODEMITLF AND F.CODEMP=LF.CODEMPET AND " );
				sSql.append( "F.CODFILIAL=LF.CODFILIALET AND LF.TIPOLF='E' "); // eMISSÃO DE NOTA DE COMPRA DE FORNECEDOR PESSOA FISICA... AND F.PESSOAFOR='J' " );

				// Incluído de acordo com o íten 11.1.3 do manual do sintegra.

				sSql.append( "GROUP BY 1,2,3,4,5,6,7,8,9,10,13,17,18,19,20,21,22,23 " );
				sSql.append( "ORDER BY LF.DTESLF,LF.DOCINILF" );
				ps = con.prepareStatement( sSql.toString() );
				ps.setDate( 1, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				ps.setDate( 2, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
				ps.setInt( 3, iCodEmp );
				ps.setInt( 4, ListaCampos.getMasterFilial( "LFLIVROFISCAL" ) );
				rs = ps.executeQuery();

				lbAnd.setText( "Gerando Entrada..." );

				while ( rs.next() ) {

					sBuffer.delete( 0, sBuffer.length() );

					cnpjfor = rs.getString( "CNPJFOR" );
					inscfor = rs.getString( "INSCFOR" );

					if ( ( inscfor == null ) || ( "ISENTA".equals( inscfor.trim() ) ) ) {
						inscfor = "ISENTO";
					}

					/* 01 */sBuffer.append( "50" );
					if("J".equals( rs.getString( "PESSOAFOR" ) )) {
						
						cnpjfor = rs.getString( "CNPJFOR" );
						
						/* 02 */sBuffer.append( Funcoes.adicionaEspacos( cnpjfor, 14 ) );
					
					}
					else {
						
						cpffor = rs.getString( "CPFFOR" );
						
						/* 02 */sBuffer.append( StringFunctions.strZero( cpffor, 14 ));
						
					}
				
					/* 03 */sBuffer.append( Funcoes.adicionaEspacos( StringFunctions.clearString( inscfor ), 14 ) );
					/* 04 */sBuffer.append( Funcoes.dataAAAAMMDD( Funcoes.sqlDateToDate( rs.getDate( "DTESLF" ) ) ) );
					/* 05 */sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "UFLF" ), 2 ) );
					/* 06 */sBuffer.append( StringFunctions.strZero( rs.getInt( "CODMODNOTA" ) + "", 2 ) );
					/* 07 */sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "SERIELF" ), 3 ) );
					/* 08 */sBuffer.append( StringFunctions.strZero( rs.getInt( "DOCINILF" ) + "", 6 ) );
					/* 09 */sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "CODNAT" ), ( sConvenio.equals( "1" ) ? 3 : 4 ) ) );
					/* 10 */sBuffer.append( ( sConvenio.equals( "1" ) ? "" : "T" ) ); // Emitente da nota fiscal P-Própio ou T-Terceiros
					/* 11 */sBuffer.append( Funcoes.transValor( rs.getString( "VLRCONTABILLF" ), 13, 2, true ) );
					/* 12 */sBuffer.append( Funcoes.transValor( rs.getString( "VLRBASEICMSLF" ), 13, 2, true ) );
					/* 13 */sBuffer.append( Funcoes.transValor( rs.getString( "VLRICMSLF" ), 13, 2, true ) );
					/* 14 */sBuffer.append( Funcoes.transValor( rs.getString( "VLRISENTASICMSLF" ), 13, 2, true ) );
					/* 15 */sBuffer.append( Funcoes.transValor( rs.getString( "VLROUTRASICMSLF" ), 13, 2, true ) );
					/* 16 */sBuffer.append( Funcoes.transValor( rs.getString( "ALIQICMSLF" ), 4, 2, true ) );
					/* 17 */sBuffer.append( rs.getString( "SITUACAOLF" ) + CR );

					gravaBuffer( sBuffer.toString() );
					cont++;

				}

				rs.close();
				ps.close();

				con.commit();

			}

			if ( "S".equals( cbSaida.getVlrString() ) ) {

				// REGISTRO 50 LIVROS FISCAIS DE SAIDA

				sSql.delete( 0, sSql.length() );

				sSql.append( "SELECT LF.TIPOLF,LF.ANOMESLF,LF.ESPECIELF,LF.DOCINILF," );
				sSql.append( "LF.DOCFIMLF,LF.SERIELF,LF.CODEMITLF,LF.UFLF,LF.DTEMITLF," );
				sSql.append( "LF.DTESLF," );
				sSql.append( "SUM(LF.VLRCONTABILLF) AS VLRCONTABILLF," );
				sSql.append( "SUM(LF.VLRBASEICMSLF) AS VLRBASEICMSLF," );
				sSql.append( "LF.ALIQICMSLF," );
				sSql.append( "SUM(LF.VLRICMSLF) AS VLRICMSLF," );
				sSql.append( "SUM(LF.VLRISENTASICMSLF) AS VLRISENTASICMSLF," );
				sSql.append( "SUM(LF.VLROUTRASICMSLF) AS VLROUTRASICMSLF," );
				sSql.append( "LF.CODNAT,LF.CODMODNOTA,C.CNPJCLI,C.INSCCLI, C.PESSOACLI, C.CPFCLI, LF.SITUACAOLF " );
				sSql.append( "FROM LFLIVROFISCAL LF,VDCLIENTE C " );
				sSql.append( "WHERE LF.DTEMITLF BETWEEN ? AND ? " );
				sSql.append( "AND LF.CODEMP=? AND LF.CODFILIAL=? " );
				sSql.append( "AND C.CODCLI=LF.CODEMITLF AND C.CODEMP=LF.CODEMPET " );
				sSql.append( "AND C.CODFILIAL=LF.CODFILIALET AND C.PESSOACLI='J' " );
				sSql.append( "AND LF.TIPOLF='S' AND LF.SERIELF<>? " );
				sSql.append( "GROUP BY 1,2,3,4,5,6,7,8,9,10,13,17,18,19,20,21,22,23 " );
				sSql.append( "ORDER BY LF.DTEMITLF,LF.DOCINILF" );

				ps = con.prepareStatement( sSql.toString() );
				ps.setDate( 1, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				ps.setDate( 2, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
				ps.setInt( 3, iCodEmp );
				ps.setInt( 4, ListaCampos.getMasterFilial( "LFLIVROFISCAL" ) );
				ps.setString( 5, serieecf );
				rs = ps.executeQuery();

				lbAnd.setText( "Gerando Saídas..." );

				while ( rs.next() ) {

					sBuffer.delete( 0, sBuffer.length() );
					if ( "F".equals( rs.getString( "PESSOACLI" ) ) ) {
						cnpjcli = StringFunctions.replicate( "0", 14 );// rs.getString( "CPFCLI" );
						insccli = StringFunctions.replicate( "0", 14 );
					}
					else {
						cnpjcli = rs.getString( "CNPJCLI" );
						insccli = rs.getString( "INSCCLI" );
						if ( ( insccli == null ) || ( "ISENTA".equals( insccli ) ) ) {
							insccli = "ISENTO";
						}

					}
					/* 01 */sBuffer.append( "50" );
					/* 02 */sBuffer.append( Funcoes.adicionaEspacos( cnpjcli, 14 ) );
					/* 03 */sBuffer.append( Funcoes.adicionaEspacos( StringFunctions.clearString( insccli ), 14 ) );
					/* 04 */sBuffer.append( Funcoes.dataAAAAMMDD( Funcoes.sqlDateToDate( rs.getDate( "DTEMITLF" ) ) ) );
					/* 05 */sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "UFLF" ), 2 ) );
					/* 06 */sBuffer.append( StringFunctions.strZero( String.valueOf( rs.getInt( "CODMODNOTA" ) ), 2 ) );
					/* 07 */sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "SERIELF" ), 3 ) );
					/* 07 sBuffer.append( "  " ); //SUBSERIE */
					/* 08 */sBuffer.append( StringFunctions.strZero( String.valueOf( rs.getInt( "DOCINILF" ) ), 6 ) );
					/* 09 */sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "CODNAT" ), ( sConvenio.equals( "1" ) ? 3 : 4 ) ) );
					/* 10 */sBuffer.append( ( sConvenio.equals( "1" ) ? "" : "P" ) ); // Emitente da nota fiscal P-Própio ou T-Terceiros
					/* 11 */sBuffer.append( Funcoes.transValor( rs.getString( "VLRCONTABILLF" ), 13, 2, true ) );
					/* 12 */sBuffer.append( Funcoes.transValor( rs.getString( "VLRBASEICMSLF" ), 13, 2, true ) );
					/* 13 */sBuffer.append( Funcoes.transValor( rs.getString( "VLRICMSLF" ), 13, 2, true ) );
					/* 14 */sBuffer.append( Funcoes.transValor( rs.getString( "VLRISENTASICMSLF" ), 13, 2, true ) );
					/* 15 */sBuffer.append( Funcoes.transValor( rs.getString( "VLROUTRASICMSLF" ), 13, 2, true ) );
					/* 16 */sBuffer.append( Funcoes.transValor( rs.getString( "ALIQICMSLF" ), 4, 2, true ) );
					/* 17 */sBuffer.append( rs.getString( "SITUACAOLF" ) + CR );

					gravaBuffer( sBuffer.toString() );
					cont++;
				}

				rs.close();
				ps.close();

				con.commit();

			}

		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao gerar registro 50!\n" + e.getMessage(), true, con, e );
		}

		return cont;

	}

	/* *********************************************
	 * REGISTRO 51 LIVROS FISCAIS DE ENTRADA/SAIDA REFERENTES AO IPI*********************************************
	 */
	private int geraRegistro51( String serieecf ) {

		PreparedStatement ps;
		String cnpjcli = "";
		String insccli = "";
		String cnpjfor = "";
		String inscfor = "";
		ResultSet rs;
		StringBuffer sSql = new StringBuffer();
		StringBuffer sBuffer = new StringBuffer();
		String sConvenio = rgConvenio.getVlrString();
		int cont = 0;

		try {

			if ( "S".equals( cbEntrada.getVlrString() ) ) {

				// LIVROS FISCAIS DE ENTRADA

				sSql.append( "SELECT LF.TIPOLF,LF.ANOMESLF,LF.ESPECIELF,LF.DOCINILF," );
				sSql.append( "LF.DOCFIMLF,LF.SERIELF,LF.CODEMITLF,LF.UFLF,LF.DTEMITLF," );
				sSql.append( "LF.DTESLF," );
				sSql.append( "SUM(LF.VLRCONTABILLF) AS VLRCONTABILLF," );
				sSql.append( "SUM(LF.VLRBASEIPILF) AS VLRBASEIPILF," );
				sSql.append( "LF.ALIQIPILF," );
				sSql.append( "SUM(LF.VLRIPILF) AS VLRIPILF," );
				sSql.append( "SUM(LF.VLRISENTASIPILF) AS VLRISENTASIPILF," );
				sSql.append( "SUM(LF.VLROUTRASIPILF) AS VLROUTRASIPILF," );
				sSql.append( "LF.CODNAT,LF.CODMODNOTA,F.CNPJFOR,F.INSCFOR,LF.SITUACAOLF " );
				sSql.append( "FROM LFLIVROFISCAL LF,CPFORNECED F " );
				sSql.append( "WHERE LF.DTESLF BETWEEN ? AND ? AND " );
				sSql.append( "LF.CODEMP=? AND LF.CODFILIAL=? AND " );
				sSql.append( "F.CODFOR=LF.CODEMITLF AND F.CODEMP=LF.CODEMPET AND " );
				sSql.append( "F.CODFILIAL=LF.CODFILIALET AND LF.TIPOLF='E' AND F.PESSOAFOR='J' " );

				// Incluído de acordo com o íten 11.1.3 do manual do sintegra.

				sSql.append( "GROUP BY 1,2,3,4,5,6,7,8,9,10,13,17,18,19,20,21 " );
				sSql.append( "ORDER BY LF.DTESLF,LF.DOCINILF" );
				ps = con.prepareStatement( sSql.toString() );
				ps.setDate( 1, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				ps.setDate( 2, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
				ps.setInt( 3, iCodEmp );
				ps.setInt( 4, ListaCampos.getMasterFilial( "LFLIVROFISCAL" ) );
				rs = ps.executeQuery();

				lbAnd.setText( "Gerando Entrada..." );

				while ( rs.next() ) {

					sBuffer.delete( 0, sBuffer.length() );

					cnpjfor = rs.getString( "CNPJFOR" );
					inscfor = rs.getString( "INSCFOR" );

					if ( ( inscfor == null ) || ( "ISENTA".equals( inscfor.trim() ) ) ) {
						inscfor = "ISENTO";
					}

					/* 01 */sBuffer.append( "51" );
					/* 02 */sBuffer.append( Funcoes.adicionaEspacos( cnpjfor, 14 ) );
					/* 03 */sBuffer.append( Funcoes.adicionaEspacos( StringFunctions.clearString( inscfor ), 14 ) );
					/* 04 */sBuffer.append( Funcoes.dataAAAAMMDD( Funcoes.sqlDateToDate( rs.getDate( "DTESLF" ) ) ) );
					/* 05 */sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "UFLF" ), 2 ) );
					/* 06 */sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "SERIELF" ), 3 ) );
					/* 07 */sBuffer.append( StringFunctions.strZero( rs.getInt( "DOCINILF" ) + "", 6 ) );
					/* 08 */sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "CODNAT" ), ( sConvenio.equals( "1" ) ? 3 : 4 ) ) );
					/* 09 */sBuffer.append( Funcoes.transValor( rs.getString( "VLRCONTABILLF" ), 13, 2, true ) );
					/* 10 */sBuffer.append( Funcoes.transValor( rs.getString( "VLRIPILF" ), 13, 2, true ) );
					/* 11 */sBuffer.append( Funcoes.transValor( rs.getString( "VLRISENTASIPILF" ), 13, 2, true ) );
					/* 12 */sBuffer.append( Funcoes.transValor( rs.getString( "VLROUTRASIPILF" ), 13, 2, true ) );
					/* 13 */sBuffer.append( StringFunctions.replicate( " ", 20 ) );
					/* 14 */sBuffer.append( rs.getString( "SITUACAOLF" ) + CR );

					gravaBuffer( sBuffer.toString() );
					cont++;

				}

				rs.close();
				ps.close();

				con.commit();

			}

			if ( "S".equals( cbSaida.getVlrString() ) ) {

				// LIVROS FISCAIS DE SAIDA

				sSql.delete( 0, sSql.length() );

				sSql.append( "SELECT LF.TIPOLF,LF.ANOMESLF,LF.ESPECIELF,LF.DOCINILF," );
				sSql.append( "LF.DOCFIMLF,LF.SERIELF,LF.CODEMITLF,LF.UFLF,LF.DTEMITLF," );
				sSql.append( "LF.DTESLF," );
				sSql.append( "SUM(LF.VLRCONTABILLF) AS VLRCONTABILLF," );
				sSql.append( "SUM(LF.VLRBASEIPILF) AS VLRBASEIPILF," );
				sSql.append( "LF.ALIQIPILF," );
				sSql.append( "SUM(LF.VLRIPILF) AS VLRIPILF," );
				sSql.append( "SUM(LF.VLRISENTASIPILF) AS VLRISENTASIPILF," );
				sSql.append( "SUM(LF.VLROUTRASIPILF) AS VLROUTRASIPILF," );
				sSql.append( "LF.CODNAT,LF.CODMODNOTA,C.CNPJCLI,C.INSCCLI, C.PESSOACLI, C.CPFCLI, LF.SITUACAOLF " );
				sSql.append( "FROM LFLIVROFISCAL LF,VDCLIENTE C " );
				sSql.append( "WHERE LF.DTEMITLF BETWEEN ? AND ? " );
				sSql.append( "AND LF.CODEMP=? AND LF.CODFILIAL=? " );
				sSql.append( "AND C.CODCLI=LF.CODEMITLF AND C.CODEMP=LF.CODEMPET " );
				sSql.append( "AND C.CODFILIAL=LF.CODFILIALET AND C.PESSOACLI='J' " );
				sSql.append( "AND LF.TIPOLF='S' AND LF.SERIELF<>? " );
				sSql.append( "GROUP BY 1,2,3,4,5,6,7,8,9,10,13,17,18,19,20,21,22,23 " );
				sSql.append( "ORDER BY LF.DTEMITLF,LF.DOCINILF" );

				ps = con.prepareStatement( sSql.toString() );
				ps.setDate( 1, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				ps.setDate( 2, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
				ps.setInt( 3, iCodEmp );
				ps.setInt( 4, ListaCampos.getMasterFilial( "LFLIVROFISCAL" ) );
				ps.setString( 5, serieecf );
				rs = ps.executeQuery();

				lbAnd.setText( "Gerando Saídas..." );

				while ( rs.next() ) {

					sBuffer.delete( 0, sBuffer.length() );
					if ( "F".equals( rs.getString( "PESSOACLI" ) ) ) {
						cnpjcli = StringFunctions.replicate( "0", 14 );// rs.getString( "CPFCLI" );
						insccli = StringFunctions.replicate( "0", 14 );
					}
					else {
						cnpjcli = rs.getString( "CNPJCLI" );
						insccli = rs.getString( "INSCCLI" );

						if ( ( insccli == null ) || ( "ISENTA".equals( insccli.trim() ) ) ) {
							insccli = "ISENTO";
						}

					}

					/* 01 */sBuffer.append( "51" );
					/* 02 */sBuffer.append( Funcoes.adicionaEspacos( cnpjcli, 14 ) );
					/* 03 */sBuffer.append( Funcoes.adicionaEspacos( StringFunctions.clearString( insccli ), 14 ) );
					/* 04 */sBuffer.append( Funcoes.dataAAAAMMDD( Funcoes.sqlDateToDate( rs.getDate( "DTEMITLF" ) ) ) );
					/* 05 */sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "UFLF" ), 2 ) );
					/* 06 */sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "SERIELF" ), 3 ) );
					/* 08 */sBuffer.append( StringFunctions.strZero( rs.getInt( "DOCINILF" ) + "", 6 ) );
					/* 09 */sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "CODNAT" ), ( sConvenio.equals( "1" ) ? 3 : 4 ) ) );
					/* 10 */sBuffer.append( Funcoes.transValor( rs.getString( "VLRCONTABILLF" ), 13, 2, true ) );
					/* 11 */sBuffer.append( Funcoes.transValor( rs.getString( "VLRIPILF" ), 13, 2, true ) );
					/* 12 */sBuffer.append( Funcoes.transValor( rs.getString( "VLRISENTASIPILF" ), 13, 2, true ) );
					/* 13 */sBuffer.append( Funcoes.transValor( rs.getString( "VLROUTRASIPILF" ), 13, 2, true ) );
					/* 14 */sBuffer.append( StringFunctions.replicate( " ", 20 ) );
					/* 15 */sBuffer.append( rs.getString( "SITUACAOLF" ) + CR );

					gravaBuffer( sBuffer.toString() );
					cont++;
				}

				rs.close();
				ps.close();

				con.commit();

			}

		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao gerar registro 51!\n" + e.getMessage(), true, con, e );
		}

		return cont;

	}

	/* *********************************************
	 * REGISTRO 53 LIVROS FISCAIS DE ENTRADA/SAIDA REFERENTES A SUBSTITUIÇÃO TRIBUTÁRIA*********************************************
	 */
	private int geraRegistro53( String serieecf ) {

		PreparedStatement ps;
		String cnpjcli = "";
		String insccli = "";
		ResultSet rs;
		StringBuffer sSql = new StringBuffer();
		StringBuffer sBuffer = new StringBuffer();
		String sConvenio = rgConvenio.getVlrString();
		int cont = 0;

		try {

			if ( "S".equals( cbSaida.getVlrString() ) ) {

				// LIVROS FISCAIS DE SAIDA

				sSql.delete( 0, sSql.length() );

				sSql.append( "SELECT LF.TIPOLF,LF.ANOMESLF,LF.ESPECIELF,LF.DOCINILF," );
				sSql.append( "LF.DOCFIMLF,LF.SERIELF,LF.CODEMITLF,LF.UFLF,LF.DTEMITLF," );
				sSql.append( "LF.DTESLF," );
				sSql.append( "SUM(LF.VLRCONTABILLF) AS VLRCONTABILLF," );
				sSql.append( "SUM(LF.VLRBASEIPILF) AS VLRBASEIPILF," );
				sSql.append( "LF.ALIQIPILF," );
				sSql.append( "SUM(LF.VLRIPILF) AS VLRIPILF," );
				sSql.append( "SUM(LF.VLRISENTASIPILF) AS VLRISENTASIPILF," );
				sSql.append( "SUM(LF.VLROUTRASIPILF) AS VLROUTRASIPILF," );

				sSql.append( "LF.CODNAT,LF.CODMODNOTA,C.CNPJCLI,C.INSCCLI, C.PESSOACLI, C.CPFCLI, LF.SITUACAOLF," );

				sSql.append( "SUM(LF.VLRBASEICMSSTLF) AS VLRBASEICMSSTLF," );
				sSql.append( "SUM(LF.VLRICMSSTLF) AS VLRICMSSTLF," );
				sSql.append( "SUM(LF.VLRACESSORIASLF) AS VLRACESSORIASLF " );

				sSql.append( "FROM LFLIVROFISCAL LF,VDCLIENTE C " );
				sSql.append( "WHERE LF.DTEMITLF BETWEEN ? AND ? " );
				sSql.append( "AND LF.CODEMP=? AND LF.CODFILIAL=? " );
				sSql.append( "AND C.CODCLI=LF.CODEMITLF AND C.CODEMP=LF.CODEMPET " );
				sSql.append( "AND C.CODFILIAL=LF.CODFILIALET AND C.PESSOACLI='J' " );
				sSql.append( "AND LF.TIPOLF='S' AND LF.SERIELF<>? " );

				sSql.append( "AND LF.VLRICMSSTLF>0 " );

				sSql.append( "GROUP BY 1,2,3,4,5,6,7,8,9,10,13,17,18,19,20,21,22,23 " );
				sSql.append( "ORDER BY LF.DTEMITLF,LF.DOCINILF" );

				System.out.println("SQL REG53:" + sSql.toString());
				
				ps = con.prepareStatement( sSql.toString() );
				ps.setDate( 1, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				ps.setDate( 2, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
				ps.setInt( 3, iCodEmp );
				ps.setInt( 4, ListaCampos.getMasterFilial( "LFLIVROFISCAL" ) );
				ps.setString( 5, serieecf );
				rs = ps.executeQuery();

				lbAnd.setText( "Gerando Saídas..." );

				while ( rs.next() ) {

					sBuffer.delete( 0, sBuffer.length() );
					if ( "F".equals( rs.getString( "PESSOACLI" ) ) ) {
						cnpjcli = StringFunctions.replicate( "0", 14 );// rs.getString( "CPFCLI" );
						insccli = StringFunctions.replicate( "0", 14 );
					}
					else {
						cnpjcli = rs.getString( "CNPJCLI" );
						insccli = rs.getString( "INSCCLI" );

						if ( ( insccli == null ) || ( "ISENTA".equals( insccli.trim() ) ) ) {
							insccli = "ISENTO";
						}

					}

					/* 01 */sBuffer.append( "53" );
					/* 02 */sBuffer.append( Funcoes.adicionaEspacos( cnpjcli, 14 ) );
					/* 03 */sBuffer.append( Funcoes.adicionaEspacos( StringFunctions.clearString( insccli ), 14 ) );
					/* 04 */sBuffer.append( Funcoes.dataAAAAMMDD( Funcoes.sqlDateToDate( rs.getDate( "DTEMITLF" ) ) ) );
					/* 05 */sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "UFLF" ), 2 ) );
					/* 06 */sBuffer.append( StringFunctions.strZero( String.valueOf( rs.getInt( "CODMODNOTA" ) ), 2 ) );
					/* 07 */sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "SERIELF" ), 3 ) );
					/* 09 */sBuffer.append( StringFunctions.strZero( rs.getInt( "DOCINILF" ) + "", 6 ) );
					/* 10 */sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "CODNAT" ), ( sConvenio.equals( "1" ) ? 3 : 4 ) ) );
					/* 10 */sBuffer.append( ( sConvenio.equals( "1" ) ? "" : "P" ) ); // Emitente da nota fiscal P-Própio ou T-Terceiros
					/* 11 */sBuffer.append( Funcoes.transValor( rs.getString( "VLRBASEICMSSTLF" ), 13, 2, true ) );
					/* 12 */sBuffer.append( Funcoes.transValor( rs.getString( "VLRICMSSTLF" ), 13, 2, true ) );
					/* 13 */sBuffer.append( Funcoes.transValor( rs.getString( "VLRACESSORIASLF" ), 13, 2, true ) );
					/* 14 */sBuffer.append( rs.getString( "SITUACAOLF" ) );
					/* 15 */sBuffer.append( StringFunctions.replicate( " ", 30 ) + CR );

					gravaBuffer( sBuffer.toString() );
					cont++;
				}

				rs.close();
				ps.close();

				con.commit();

			}

		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao gerar registro 50!\n" + e.getMessage(), true, con, e );
		}

		return cont;

	}

	/* *********************************************
	 * REGISTRO 70 LIVROS FISCAIS DE ENTRADA/SAIDA REFERENTES A CONHECIMENTOS DE FRETE*********************************************
	 */
	private int geraRegistro70() {

		PreparedStatement ps;
		String cnpj = "";
		String insc = "";
		ResultSet rs;
		StringBuffer sql = new StringBuffer();
		StringBuffer buffer = new StringBuffer();
		String sConvenio = rgConvenio.getVlrString();
		int cont = 0;

		try {

			if ( ( "S".equals( cbFrete.getVlrString() ) ) ) {

				sql.append( "select tr.cnpjtran, tr.insctran, fr.dtemitfrete, coalesce(tr.siglauf, tr.uftran) as uf," );
				sql.append( "tm.codmodnota,fr.serie,fr.docfrete,fr.codnat,fr.vlrfrete,fr.vlrbaseicmsfrete,fr.vlricmsfrete," );
				sql.append( "fr.tipofrete " );
				sql.append( "from lffrete fr, vdtransp tr, eqtipomov tm " );
				sql.append( "where " );
				sql.append( "tr.codemp=fr.codemptn and tr.codfilial=fr.codfilialtn and tr.codtran=fr.codtran " );
				sql.append( "and tm.codemp=fr.codemptm and tm.codfilial=fr.codfilialtm and tm.codtipomov=fr.codtipomov " );
				sql.append( "and fr.codemp=? and fr.codfilial=? and fr.dtemitfrete between ? and ? " );
				sql.append( "order by fr.dtemitfrete" );

				System.out.println( "Registro70:" + sql.toString() );

				ps = con.prepareStatement( sql.toString() );

				ps.setInt( 1, iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "LFFRETE" ) );
				ps.setDate( 3, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				ps.setDate( 4, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

				rs = ps.executeQuery();

				lbAnd.setText( "Gerando Conhecimentos de frete..." );

				while ( rs.next() ) {

					buffer.delete( 0, buffer.length() );

					cnpj = rs.getString( "CNPJTRAN" );
					insc = rs.getString( "INSCTRAN" );

					if ( ( insc == null ) || ( "ISENTA".equals( insc.trim() ) ) ) {
						insc = "ISENTO";
					}

					/* 01 */buffer.append( "70" );
					/* 02 */buffer.append( Funcoes.adicionaEspacos( cnpj, 14 ) );
					/* 03 */buffer.append( Funcoes.adicionaEspacos( StringFunctions.clearString( insc ), 14 ) );
					/* 04 */buffer.append( Funcoes.dataAAAAMMDD( Funcoes.sqlDateToDate( rs.getDate( "dtemitfrete" ) ) ) );
					/* 05 */buffer.append( Funcoes.adicionaEspacos( rs.getString( "UF" ), 2 ) );
					/* 06 */buffer.append( StringFunctions.strZero( String.valueOf( rs.getInt( "CODMODNOTA" ) ), 2 ) );

					/* 07 */buffer.append( Funcoes.adicionaEspacos( rs.getString( "SERIE" ), 3 ) );

					/* 09 */buffer.append( StringFunctions.strZero( rs.getInt( "DOCFRETE" ) + "", 6 ) );
					/* 10 */buffer.append( Funcoes.adicionaEspacos( rs.getString( "CODNAT" ), ( sConvenio.equals( "1" ) ? 3 : 4 ) ) );

					/* 11 */buffer.append( Funcoes.transValor( rs.getString( "VLRFRETE" ), 13, 2, true ) );
					/* 12 */buffer.append( Funcoes.transValor( rs.getString( "VLRBASEICMSFRETE" ), 14, 2, true ) );
					/* 13 */buffer.append( Funcoes.transValor( rs.getString( "VLRICMSFRETE" ), 14, 2, true ) );
					/* 14 */buffer.append( Funcoes.transValor( "0", 14, 2, true ) ); // Isentas
					/* 15 */buffer.append( Funcoes.transValor( "0", 14, 2, true ) ); // Outras
					/* 16 */buffer.append( Funcoes.adicionaEspacos( rs.getString( "TIPOFRETE" ), 1 ) );
					/* 17 */buffer.append( "N" + CR );

					gravaBuffer( buffer.toString() );
					cont++;

				}

				rs.close();
				ps.close();

				con.commit();
			}

		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao gerar registro 70!\n" + e.getMessage(), true, con, e );
		}

		return cont;

	}

	private String getSerieECF() throws SQLException {

		String serie = "";
		StringBuffer sql = new StringBuffer();
		ResultSet rs;
		PreparedStatement ps;
		sql.append( "SELECT TM.SERIE FROM SGPREFERE4 P, EQTIPOMOV TM " );
		sql.append( "WHERE P.CODEMP=? AND P.CODFILIAL=? AND  " );
		sql.append( "TM.CODEMP=P.CODEMPTM AND TM.CODFILIAL=P.CODFILIALTM AND  " );
		sql.append( "TM.CODTIPOMOV=P.CODTIPOMOV " );
		ps = con.prepareStatement( sql.toString() );
		ps.setInt( 1, Aplicativo.iCodEmp );
		ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE4" ) );
		rs = ps.executeQuery();
		if ( rs.next() ) {
			serie = rs.getString( "SERIE" );
		}
		rs.close();
		ps.close();
		con.commit();
		return serie;
	}

	private int geraRegistro54( String serieecf ) {

		PreparedStatement ps;
		ResultSet rs;
		StringBuffer sSql = new StringBuffer();
		StringBuffer sBuffer = new StringBuffer();
		String sConvenio = rgConvenio.getVlrString();
		String cnpjcli = "";
		String sDocTmp = "";
		int iOrdem = 0;
		int cont = 0;

		try {

			if ( "S".equals( cbEntrada.getVlrString() ) ) {

				// REGISTRO 54 INFORMAÇÕES DOS ITENS DAS NOTAS FISCAIS DE ENTRADA

				sSql.append( "SELECT C.DTENTCOMPRA,C.DOCCOMPRA,IC.CODITCOMPRA,C.CODFOR," );
				sSql.append( "F.UFFOR,F.CNPJFOR,IC.CODNAT,IC.CODPROD,P.REFPROD," );
				sSql.append( "C.DTEMITCOMPRA,C.SERIE,TM.CODMODNOTA,IC.PERCICMSITCOMPRA," );

				sSql.append( "(case when IC.qtditcompracanc>0 then ic.qtditcompracanc else ic.qtditcompra end) qtditcompra,");
				
				sSql.append( "IC.VLRLIQITCOMPRA,IC.VLRBASEICMSITCOMPRA," );
				
				sSql.append( "IC.PERCICMSITCOMPRA,IC.VLRBASEICMSITCOMPRA,IC.VLRIPIITCOMPRA," );
				sSql.append( "CF.ORIGFISC, CF.CODTRATTRIB, C.CODCOMPRA, F.CPFFOR, F.PESSOAFOR " );
				
				sSql.append( "FROM CPCOMPRA C,CPFORNECED F,CPITCOMPRA IC,EQTIPOMOV TM,EQPRODUTO P, LFITCLFISCAL CF " );
				sSql.append( "WHERE C.DTENTCOMPRA BETWEEN ? AND ? AND C.CODEMP=? AND C.CODFILIAL=? AND " );
				sSql.append( "IC.CODCOMPRA=C.CODCOMPRA AND  IC.CODEMP=C.CODEMP AND IC.CODFILIAL=C.CODFILIAL AND " );
				sSql.append( "TM.CODTIPOMOV=C.CODTIPOMOV AND TM.CODEMP=C.CODEMPTM AND TM.CODFILIAL=C.CODFILIALTM AND " );
				sSql.append( "F.CODFOR=C.CODFOR AND F.CODEMP=C.CODEMPFR AND F.CODFILIAL=C.CODFILIALFR AND " );
				sSql.append( "P.CODPROD=IC.CODPROD AND P.CODEMP=IC.CODEMPPD AND P.CODFILIAL=IC.CODFILIALPD AND " );
				sSql.append( "CF.CODFISC=P.CODFISC AND CF.CODEMP=P.CODEMPFC AND CF.CODFILIAL=P.CODFILIALFC AND CF.GERALFISC='S' AND " );
				sSql.append( "TM.FISCALTIPOMOV='S' "); // COMPRAS DE PESSOA FISICA DEVEM ENTRAR... F.PESSOAFOR='J' " +
				sSql.append( " and p.tipoprod<>'O' "); 
				sSql.append( "ORDER BY C.DTENTCOMPRA,C.DOCCOMPRA,IC.CODITCOMPRA" );

				ps = con.prepareStatement( sSql.toString() );
				ps.setDate( 1, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				ps.setDate( 2, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
				ps.setInt( 3, iCodEmp );
				ps.setInt( 4, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
				rs = ps.executeQuery();

				lbAnd.setText( "Gerando Itens NF Entrada..." );

				sDocTmp = "";

				while ( rs.next() ) {
					if ( !sDocTmp.equals( "" + rs.getInt( "DOCCOMPRA" ) ) ) {
						iOrdem = 1;
					}

					/* 01 */sBuffer.append( "54" );
					
					
					if("J".equals( rs.getString( "PESSOAFOR" ) )) {
					/* 02 */sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "CNPJFOR" ), 14 ) );
					}
					else {
					/* 02 */sBuffer.append( StringFunctions.strZero( rs.getString( "CPFFOR" ), 14 ) );
					}
					
					/* 03 */sBuffer.append( StringFunctions.strZero( String.valueOf( rs.getString( "CODMODNOTA" ) == null ? 0 : rs.getInt( "CODMODNOTA" ) ), 2 ) );
					/* 04 */sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "SERIE" ), 3 ) );
					// sBuffer.append( "  "); //Subsérie
					/* 05 */sBuffer.append( StringFunctions.strZero( String.valueOf( rs.getInt( "DOCCOMPRA" ) ), 6 ) );
					/* 06 */sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "CODNAT" ), ( sConvenio.equals( "1" ) ? 3 : 4 ) ) );
					/* 07 */sBuffer.append( Funcoes.copy( ( sConvenio.equals( "1" ) ? "" : ( rs.getString( "ORIGFISC" ).trim() + rs.getString( "CODTRATTRIB" ).trim() ) ), 3 ) );
					/* 08 */sBuffer.append( StringFunctions.strZero( String.valueOf( rs.getInt( "CODITCOMPRA" ) ), 3 ) );
					/* 09 */sBuffer.append( Funcoes.adicionaEspacos( rs.getString( ( sUsaRefProd.equals( "S" ) ? "REFPROD" : "CODPROD" ) ), 14 ) );
					/* 10 */sBuffer.append( Funcoes.transValor( rs.getString( "QTDITCOMPRA" ), ( sConvenio.equals( "1" ) ? 13 : 11 ), 3, true ) );
					/* 11 */sBuffer.append( Funcoes.transValor( rs.getString( "VLRLIQITCOMPRA" ), 12, 2, true ) );
					/* 12 */sBuffer.append( Funcoes.transValor( "0", 12, 2, true ) );
					/* 13 */sBuffer.append( Funcoes.transValor( rs.getString( "VLRBASEICMSITCOMPRA" ), 12, 2, true ) );
					/* 14 */sBuffer.append( Funcoes.transValor( "0", 12, 2, true ) );
					/* 15 */sBuffer.append( Funcoes.transValor( rs.getString( "VLRIPIITCOMPRA" ), 12, 2, true ) );
					/* 16 */sBuffer.append( Funcoes.transValor( rs.getString( "PERCICMSITCOMPRA" ), 4, 2, true ) + CR );

					gravaBuffer( sBuffer.toString() );
					sBuffer.delete( 0, sBuffer.length() );
					sDocTmp = String.valueOf( rs.getInt( "DOCCOMPRA" ) );
					iOrdem++;
					cont++;
				}

				rs.close();
				ps.close();

				con.commit();

			}

			if ( "S".equals( cbSaida.getVlrString() ) ) {

				// REGISTRO 54 INFORMAÇÕES DOS ITENS DAS NOTAS FISCAIS DE SAÍDA

				sSql.delete( 0, sSql.length() );

				sSql.append( "SELECT V.DTSAIDAVENDA,V.DOCVENDA,IV.CODITVENDA,V.CODCLI," );
				sSql.append( "C.UFCLI, C.PESSOACLI, C.CNPJCLI, C.CPFCLI, IV.CODNAT,IV.CODPROD,P.REFPROD," );
				sSql.append( "V.DTEMITVENDA,V.SERIE,TM.CODMODNOTA,IV.PERCICMSITVENDA," );
				
				sSql.append( "(case when Iv.qtditvendacanc>0 then iv.qtditvendacanc else iv.qtditvenda end) qtditvenda,");
				
				sSql.append( "IV.VLRLIQITVENDA,IV.VLRBASEICMSITVENDA," );
				sSql.append( "IV.PERCICMSITVENDA,IV.VLRBASEICMSITVENDA,IV.VLRIPIITVENDA," );
				sSql.append( "CF.ORIGFISC,CF.CODTRATTRIB " );
				
				sSql.append( "FROM VDVENDA V,VDCLIENTE C,VDITVENDA IV,EQTIPOMOV TM,EQPRODUTO P,LFITCLFISCAL CF " );
				sSql.append( "WHERE V.DTEMITVENDA BETWEEN ? AND ? " );
				sSql.append( "AND V.CODEMP=? AND V.CODFILIAL=? AND V.TIPOVENDA='V' " );
				sSql.append( "AND IV.CODEMP=V.CODEMP AND IV.CODFILIAL=V.CODFILIAL AND IV.TIPOVENDA=V.TIPOVENDA " );
				sSql.append( "AND IV.CODVENDA=V.CODVENDA ");
				sSql.append( "AND TM.CODEMP=V.CODEMPTM AND TM.CODFILIAL=V.CODFILIALTM AND TM.CODTIPOMOV=V.CODTIPOMOV AND TM.FISCALTIPOMOV='S' " );
				sSql.append( "AND C.CODEMP=V.CODEMPCL AND C.CODFILIAL=V.CODFILIALCL AND C.CODCLI=V.CODCLI AND C.PESSOACLI='J' ");
				sSql.append( "AND P.CODEMP=IV.CODEMPPD AND P.CODFILIAL=IV.CODFILIALPD AND P.CODPROD=IV.CODPROD ");
				
				sSql.append( "AND CF.CODFISC=IV.CODFISC AND CF.CODEMP=IV.CODEMPIF AND CF.CODFILIAL=IV.CODFILIALIF AND CF.CODITFISC=IV.CODITFISC " );
				sSql.append( " and p.tipoprod<>'O' ");

				sSql.append( "ORDER BY V.DTEMITVENDA,V.DOCVENDA,IV.CODITVENDA" );

				System.out.println("SQL REG54:" + sSql.toString());
				
				ps = con.prepareStatement( sSql.toString() );
				ps.setDate( 1, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				ps.setDate( 2, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
				ps.setInt( 3, iCodEmp );
				ps.setInt( 4, ListaCampos.getMasterFilial( "VDVENDA" ) );
				rs = ps.executeQuery();

				lbAnd.setText( "Gerando Itens NF Saída..." );

				sDocTmp = "";

				while ( rs.next() ) {

					if ( !sDocTmp.equals( String.valueOf( rs.getInt( "DOCVENDA" ) ) ) ) {
						iOrdem = 1;
					}

					sBuffer.delete( 0, sBuffer.length() );

					if ( "F".equals( rs.getString( "PESSOACLI" ) ) ) {
						cnpjcli = StringFunctions.replicate( "0", 14 );// rs.getString( "CPFCLI" );
					}
					else {
						cnpjcli = rs.getString( "CNPJCLI" );
					}

					/* 01 */sBuffer.append( "54" );
					/* 02 */sBuffer.append( Funcoes.adicionaEspacos( cnpjcli, 14 ) );
					/* 03 */sBuffer.append( StringFunctions.strZero( String.valueOf( rs.getString( "CODMODNOTA" ) == null ? 0 : rs.getInt( "CODMODNOTA" ) ), 2 ) );
					/* 04 */sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "SERIE" ), 3 ) );
					/* 05 */sBuffer.append( ( sConvenio.equals( "1" ) ? StringFunctions.replicate( " ", 2 ) : "" ) ); // Sub serie
					/* 06 */sBuffer.append( StringFunctions.strZero( String.valueOf( rs.getInt( "DOCVENDA" ) ), 6 ) );
					/* 07 */sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "CODNAT" ), ( sConvenio.equals( "1" ) ? 3 : 4 ) ) );
					/* 08 */sBuffer.append( Funcoes.copy( ( sConvenio.equals( "1" ) ? "" : rs.getString( "ORIGFISC" ).trim() + rs.getString( "CODTRATTRIB" ).trim() ), 3 ) );
					/* 09 */sBuffer.append( StringFunctions.strZero( String.valueOf( rs.getInt( "CODITVENDA" ) ), 3 ) );
					/* 10 */sBuffer.append( Funcoes.adicionaEspacos( rs.getString( ( sUsaRefProd.equals( "S" ) ? "REFPROD" : "CODPROD" ) ), 14 ) );
					/* 11 */sBuffer.append( Funcoes.transValor( rs.getString( "QTDITVENDA" ), ( sConvenio.equals( "1" ) ? 13 : 11 ), 3, true ) );
					/* 12 */sBuffer.append( Funcoes.transValor( rs.getString( "VLRLIQITVENDA" ), 12, 2, true ) );
					/* 13 */sBuffer.append( Funcoes.transValor( "0", 12, 2, true ) );
					/* 14 */sBuffer.append( Funcoes.transValor( rs.getString( "VLRBASEICMSITVENDA" ), 12, 2, true ) );
					/* 15 */sBuffer.append( Funcoes.transValor( "0", 12, 2, true ) );
					/* 16 */sBuffer.append( Funcoes.transValor( rs.getString( "VLRIPIITVENDA" ), 12, 2, true ) );
					/* 17 */sBuffer.append( Funcoes.transValor( rs.getString( "PERCICMSITVENDA" ), 4, 2, true ) + CR );

					gravaBuffer( sBuffer.toString() );
					iOrdem++;
					cont++;

					sDocTmp = String.valueOf( rs.getInt( "DOCVENDA" ) );

				}

				rs.close();
				ps.close();

				con.commit();

			}

		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao gerar registro 54!\n" + e.getMessage(), true, con, e );
		}

		return cont;

	}

	private int geraRegistro60() {

		PreparedStatement ps;
		ResultSet rs;
		StringBuffer sSql = new StringBuffer();
		StringBuffer sBuffer = new StringBuffer();
		String sAliq = "";
		String sCampo = "";
		float fValor = 0;
		int cont = 0;
		int codvenda = 0;

		if ( "S".equals( cbSaida.getVlrString() ) ) {

			try {
				// REGISTRO 60 INFORMAÇÕES DOS ITENS DAS NOTAS FISCAIS DE SAÍDA POR ECF

				sSql.delete( 0, sSql.length() );

				sSql.append( "SELECT FIRST 1 V.CODVENDA " );
				sSql.append( "FROM VDITVENDA I, VDVENDA V " );
				sSql.append( "WHERE I.CODEMP=? AND I.CODFILIAL=? AND I.TIPOVENDA='E' " );
				sSql.append( "AND V.CODEMP=I.CODEMP AND V.CODFILIAL=I.CODFILIAL " );
				sSql.append( "AND V.CODVENDA=I.CODVENDA AND V.TIPOVENDA=I.TIPOVENDA " );
				sSql.append( "AND V.DTEMITVENDA BETWEEN ? AND ? " );
				ps = con.prepareStatement( sSql.toString() );
				ps.setInt( 1, iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "VDITVENDA" ) );
				ps.setDate( 3, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				ps.setDate( 4, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
				rs = ps.executeQuery();
				if ( rs.next() ) {
					codvenda = rs.getInt( "CODVENDA" );
				}
				rs.close();
				ps.close();
				con.commit();
				if ( codvenda == 0 ) {
					return codvenda;
				}

				sSql.delete( 0, sSql.length() );
				sSql.append( "SELECT L.DTLX, L.CODCAIXA, L.PRIMCUPOMLX, L.ULTCUPOMLX, L.NUMREDLX, L.TGTOTAL,L.VLRCONTABILLX," );
				sSql.append( "( SELECT I.NSERIEIMP FROM PVCAIXA C, SGESTACAOIMP EI, SGIMPRESSORA I " );
				sSql.append( "        WHERE C.CODEMP=L.CODEMP AND C.CODFILIAL=L.CODFILIAL AND C.CODCAIXA=L.CODCAIXA " );
				sSql.append( "        AND EI.CODEMP=C.CODEMPET AND EI.CODFILIAL=C.CODFILIALET AND EI.CODEST=C.CODEST " );
				sSql.append( "		 AND I.CODEMP=EI.CODEMPIP AND I.CODFILIAL=EI.CODFILIALIP AND I.CODIMP=EI.CODIMP AND I.TIPOIMP IN (6,8) ) AS NUMSERIEIMP," );
				sSql.append( "L.TSUBSTITUICAO,L.TISENCAO,L.TNINCIDENCIA," );
				sSql.append( "L.ALIQ01,L.ALIQ02,L.ALIQ03,L.ALIQ04,L.ALIQ05,L.ALIQ06,L.ALIQ07,L.ALIQ08," );
				sSql.append( "L.ALIQ09,L.ALIQ10,L.ALIQ11,L.ALIQ01,L.ALIQ12,L.ALIQ13,L.ALIQ14,L.ALIQ15,L.ALIQ16," );
				sSql.append( "L.TT01,L.TT02,L.TT03,L.TT04,L.TT05,L.TT06,L.TT07,L.TT08," );
				sSql.append( "L.TT09,L.TT10,L.TT11,L.TT12,L.TT13,L.TT14,L.TT15,L.TT16 " );
				sSql.append( "FROM PVLEITURAX L " );
				sSql.append( "WHERE L.DTLX BETWEEN ? AND ? " );
				sSql.append( "AND L.CODEMP=? AND L.CODFILIAL=? " );
				sSql.append( "ORDER BY L.DTLX" );

				ps = con.prepareStatement( sSql.toString() );
				ps.setDate( 1, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				ps.setDate( 2, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
				ps.setInt( 3, iCodEmp );
				ps.setInt( 4, ListaCampos.getMasterFilial( "PVLEITURAX" ) );
				rs = ps.executeQuery();

				lbAnd.setText( "Gerando registro de ECF..." );

				while ( rs.next() ) {

					/* 01 */sBuffer.append( "60" );
					/* 02 */sBuffer.append( "M" );
					/* 03 */sBuffer.append( Funcoes.dataAAAAMMDD( Funcoes.sqlDateToDate( rs.getDate( "DTLX" ) ) ) );
					/* 04 */sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "NUMSERIEIMP" ), 20 ) );
					/* 05 */sBuffer.append( StringFunctions.strZero( String.valueOf( rs.getInt( "CODCAIXA" ) ), 3 ) );
					/* 06 */sBuffer.append( "2D" ); // por se tratar de emição por ECF.
					/* 07 */sBuffer.append( StringFunctions.strZero( String.valueOf( rs.getInt( "PRIMCUPOMLX" ) ), 6 ) );
					/* 08 */sBuffer.append( StringFunctions.strZero( String.valueOf( rs.getInt( "ULTCUPOMLX" ) ), 6 ) );
					/* 09 */sBuffer.append( StringFunctions.strZero( String.valueOf( rs.getInt( "NUMREDLX" ) ), 6 ) );
					/* ?? */sBuffer.append( "000" );
					/* 10 */sBuffer.append( Funcoes.transValor( String.valueOf( rs.getInt( "VLRCONTABILLX" ) ), 16, 2, true ) );
					/* 11 */sBuffer.append( Funcoes.transValor( String.valueOf( rs.getInt( "TGTOTAL" ) ), 16, 2, true ) );
					/* 12 */sBuffer.append( StringFunctions.replicate( " ", 37 ) + CR );

					gravaBuffer( sBuffer.toString() );
					sBuffer.delete( 0, sBuffer.length() );
					cont++;

					// 19 é o número de aliquotas.
					for ( int i = 1; i <= 19; i++ ) {

						fValor = 0;

						if ( i <= 16 ) {
							sCampo = "ALIQ" + StringFunctions.strZero( String.valueOf( i ), 2 );
							sAliq = rs.getString( sCampo );
							sCampo = "TT" + StringFunctions.strZero( String.valueOf( i ), 2 );
							fValor = rs.getFloat( sCampo );
						}
						else {
							if ( i == 17 ) {
								sAliq = "F   ";
								sCampo = "TSUBSTITUICAO";
							}
							else if ( i == 18 ) {
								sAliq = "I   ";
								sCampo = "TISENCAO";
							}
							else if ( i == 19 ) {
								sAliq = "N   ";
								sCampo = "TNINCIDENCIA";
							}
							fValor = rs.getFloat( sCampo );
						}

						if ( ( sAliq == null ) || ( fValor == 0 ) )
							continue;

						/* 01 */sBuffer.append( "60" );
						/* 02 */sBuffer.append( "A" );
						/* 03 */sBuffer.append( Funcoes.dataAAAAMMDD( Funcoes.sqlDateToDate( rs.getDate( "DTLX" ) ) ) );
						/* 04 */sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "NUMSERIEIMP" ), 20 ) );

						if ( i >= 16 )
							/* 05 */sBuffer.append( Funcoes.adicionaEspacos( sAliq, 4 ) );
						else
							/* 05 */sBuffer.append( Funcoes.transValor( sAliq, 4, 2, true ) );

						/* 06 */sBuffer.append( Funcoes.transValor( String.valueOf( fValor ), 12, 2, true ) );
						/* 07 */sBuffer.append( StringFunctions.replicate( " ", 79 ) + CR );

						gravaBuffer( sBuffer.toString() );
						sBuffer.delete( 0, sBuffer.length() );
						cont++;
					}
				}
				rs.close();
				ps.close();
				con.commit();

				sSql.delete( 0, sSql.length() );

				sSql.append( "SELECT EXTRACT (YEAR FROM V.DTEMITVENDA) AS ANO, EXTRACT (MONTH FROM V.DTEMITVENDA) AS MES , " );
				sSql.append( "I.CODPROD, I.QTDITVENDA, (I.QTDITVENDA * I.PRECOITVENDA) AS VLRBRUTO, I.VLRBASEICMSITVENDA, " );
				sSql.append( "I.TIPOFISC, I.PERCICMSITVENDA " );
				sSql.append( "FROM VDITVENDA I, VDVENDA V " );
				sSql.append( "WHERE I.CODEMP=? AND I.CODFILIAL=? AND I.TIPOVENDA='E' " );
				sSql.append( "AND V.CODEMP=I.CODEMP AND V.CODFILIAL=I.CODFILIAL " );
				sSql.append( "AND V.CODVENDA=I.CODVENDA AND V.TIPOVENDA=I.TIPOVENDA " );
				sSql.append( "AND V.DTEMITVENDA BETWEEN ? AND ? " );
				sSql.append( "GROUP BY 3,1,2,8,7,4,5,6 " );
				sSql.append( "ORDER BY 3,1,2,8,7,4,5,6" );

				ps = con.prepareStatement( sSql.toString() );
				ps.setInt( 1, iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "VDITVENDA" ) );
				ps.setDate( 3, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				ps.setDate( 4, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
				rs = ps.executeQuery();

				lbAnd.setText( "Gerando registro dos itens de ECF..." );

				while ( rs.next() ) {

					sBuffer.delete( 0, sBuffer.length() );

					/* 01 */sBuffer.append( "60" );
					/* 02 */sBuffer.append( "R" );
					/* 03 */sBuffer.append( StringFunctions.strZero( rs.getString( "MES" ), 2 ) );
					/* 03 */sBuffer.append( StringFunctions.strZero( rs.getString( "ANO" ), 4 ) );
					/* 04 */sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "CODPROD" ).trim(), 14 ) );
					/* 05 */sBuffer.append( Funcoes.transValor( rs.getString( "QTDITVENDA" ), 13, 3, true ) );
					/* 06 */sBuffer.append( Funcoes.transValor( String.valueOf( rs.getBigDecimal( "VLRBRUTO" ).setScale( 2, BigDecimal.ROUND_HALF_UP ) ), 16, 2, true ) );
					/* 07 */sBuffer.append( Funcoes.transValor( rs.getString( "VLRBASEICMSITVENDA" ), 16, 2, true ) );

					if ( "TT".equals( rs.getString( "TIPOFISC" ).trim() ) ) {
						/* 08 */sBuffer.append( Funcoes.transValor( rs.getString( "PERCICMSITVENDA" ), 4, 2, true ) );
					}
					else {
						/* 08 */sBuffer.append( Funcoes.copy( rs.getString( "TIPOFISC" ), 1 ) + "   " );
					}

					/* 09 */sBuffer.append( StringFunctions.replicate( " ", 54 ) + CR );

					gravaBuffer( sBuffer.toString() );
					cont++;
				}
				rs.close();
				ps.close();
				con.commit();
			} catch ( Exception e ) {
				e.printStackTrace();
				Funcoes.mensagemErro( this, "Erro ao gerar registro 60!\n" + e.getMessage(), true, con, e );
			}

		}

		return cont;

	}

	private int geraRegistro61( String serieecf ) {

		PreparedStatement ps;
		ResultSet rs;
		StringBuffer sSql = new StringBuffer();
		StringBuffer sBuffer = new StringBuffer();
		String sConvenio = rgConvenio.getVlrString();
		int cont = 0;

		try {

			if ( "S".equals( cbConsumidor.getVlrString() ) && "1".equals( sConvenio ) ) {

				// REGISTRO 61 LIVROS FISCAIS DE SAIDA

				sSql.append( "SELECT LF.TIPOLF,LF.ANOMESLF,LF.ESPECIELF,LF.DOCINILF," );
				sSql.append( "LF.DOCFIMLF,LF.SERIELF,LF.CODEMITLF,LF.UFLF,LF.DTEMITLF," );
				sSql.append( "LF.DTESLF,LF.VLRCONTABILLF,LF.VLRBASEICMSLF,LF.ALIQICMSLF," );
				sSql.append( "LF.VLRICMSLF,LF.VLRISENTASICMSLF,LF.VLROUTRASICMSLF," );
				sSql.append( "LF.CODNAT,LF.CODMODNOTA,C.CNPJCLI,C.INSCCLI " );
				sSql.append( "FROM LFLIVROFISCAL LF,VDCLIENTE C " );
				sSql.append( "WHERE LF.DTEMITLF BETWEEN ? AND ? AND LF.CODEMP=? AND LF.CODFILIAL=? AND " );
				sSql.append( "C.CODCLI=LF.CODEMITLF AND C.CODEMP=LF.CODEMPET AND C.CODFILIAL=LF.CODFILIALET AND " );
				sSql.append( "LF.TIPOLF='S' AND C.PESSOACLI='F' AND LF.SERIELF<>? " );
				sSql.append( "ORDER BY LF.DTEMITLF" );

				System.out.println("SQL REG61:" + sSql.toString());
				
				ps = con.prepareStatement( sSql.toString() );
				ps.setDate( 1, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				ps.setDate( 2, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
				ps.setInt( 3, iCodEmp );
				ps.setInt( 4, ListaCampos.getMasterFilial( "LFLIVROFISCAL" ) );
				ps.setString( 5, serieecf );
				rs = ps.executeQuery();

				lbAnd.setText( "Gerando NF Saída (Consumidor)..." );

				while ( rs.next() ) {

					sBuffer.delete( 0, sBuffer.length() );

					sBuffer.append( "61" + StringFunctions.replicate( " ", 14 ) );
					sBuffer.append( StringFunctions.replicate( " ", 14 ) );
					sBuffer.append( Funcoes.dataAAAAMMDD( Funcoes.sqlDateToDate( rs.getDate( "DTEMITLF" ) ) ) );
					sBuffer.append( StringFunctions.strZero( rs.getInt( "CODMODNOTA" ) + "", 2 ) );
					sBuffer.append( Funcoes.adicionaEspacos( rs.getString( "SERIELF" ), 3 ) );
					sBuffer.append( StringFunctions.replicate( " ", 2 ) ); // Sub serie
					sBuffer.append( StringFunctions.strZero( rs.getInt( "DOCINILF" ) + "", 6 ) );
					sBuffer.append( StringFunctions.strZero( rs.getInt( "DOCFIMLF" ) + "", 6 ) );
					sBuffer.append( Funcoes.transValor( rs.getString( "VLRCONTABILLF" ), 13, 2, true ) );
					sBuffer.append( Funcoes.transValor( rs.getString( "VLRBASEICMSLF" ), 13, 2, true ) );
					sBuffer.append( Funcoes.transValor( rs.getString( "VLRICMSLF" ), 12, 2, true ) );
					sBuffer.append( Funcoes.transValor( rs.getString( "VLRISENTASICMSLF" ), 13, 2, true ) );
					sBuffer.append( Funcoes.transValor( rs.getString( "VLROUTRASICMSLF" ), 13, 2, true ) );
					sBuffer.append( Funcoes.transValor( rs.getString( "ALIQICMSLF" ), 4, 2, true ) + CR );

					gravaBuffer( sBuffer.toString() );
					cont++;
				}

				rs.close();
				ps.close();

				con.commit();

			}

		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao gerar registro 61!\n" + e.getMessage(), true, con, e );
		}

		return cont;

	}

	private int geraRegistro74() {

		PreparedStatement ps;
		ResultSet rs;
		StringBuilder sql = new StringBuilder();
		StringBuffer sBuffer = new StringBuffer();

		String sConvenio = rgConvenio.getVlrString();

		int iParam = 1;
		int cont = 0;

		try {

			if ( ( "S".equals( cbInventario.getVlrString() ) ) ) {

				// REGISTRO 74 INVENTARIO

				sql.append( "SELECT P.CODPROD,P.REFPROD, P.SALDO, P.VLRESTOQ, '1' CODPOSSE, F.CNPJFILIAL, F.INSCFILIAL, F.SIGLAUF " );
				sql.append( "FROM EQRELINVPRODSP(?,?,?,null,null,null,?,null,null,null) P, SGFILIAL F, EQPRODUTO PD " );
				sql.append( "LEFT OUTER JOIN LFITCLFISCAL CF ON CF.CODFISC=PD.CODFISC AND CF.CODEMP=PD.CODEMPFC AND CF.CODFILIAL=PD.CODFILIALFC " );
				sql.append( "WHERE P.SALDO > 0 " );
				sql.append( "AND F.CODEMP=? AND F.CODFILIAL=? " );
				sql.append( "AND PD.CODEMP=? AND PD.CODFILIAL=? AND PD.CODPROD=P.CODPROD AND PD.ATIVOPROD='S' and pd.tipoprod<>'O' AND CF.GERALFISC='S' " ); 
			
				// Filtro para não incluir produtos do patrimônio. 
				sql.append( "and pd.tipoprod<>'O' " ); 

				sql.append( "ORDER BY " + ( sUsaRefProd.equals( "S" ) ? "P.REFPROD" : "p.CODPROD" ) );

				ps = con.prepareStatement( sql.toString() );

				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
//				ps.setString( 3, "M" ); // Custo MPM
				ps.setString( 3, "P" ); // Custo PEPS
				ps.setDate( 4, Funcoes.dateToSQLDate( Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) ) );
				ps.setInt( 5, Aplicativo.iCodEmp );
				ps.setInt( 6, Aplicativo.iCodFilial );
				ps.setInt( 7, Aplicativo.iCodEmp );
				ps.setInt( 8, ListaCampos.getMasterFilial( "EQPRODUTO" ) );

				rs = ps.executeQuery();
				lbAnd.setText( "Gerando registro de inventário..." );

				while ( rs.next() ) {

					sBuffer.delete( 0, sBuffer.length() );

					btGerar.setEnabled( false );

					sBuffer.append( "74" );
					sBuffer.append( Funcoes.dataAAAAMMDD( txtDatafim.getVlrDate() ) );
					sBuffer.append( Funcoes.adicionaEspacos( rs.getString( ( sUsaRefProd.equals( "S" ) ? "REFPROD" : "CODPROD" ) ), 14 ) );
					sBuffer.append( Funcoes.transValor( rs.getString( "SALDO" ), 13, 3, true ) );
					sBuffer.append( Funcoes.transValor( rs.getString( "VLRESTOQ" ), 13, 2, true ) );
					sBuffer.append( rs.getString( "CODPOSSE" ) );
					sBuffer.append( "00000000000000" );
					sBuffer.append( StringFunctions.replicate( " ", 14 ) );
					sBuffer.append( rs.getString( "SIGLAUF" ) );
					sBuffer.append( StringFunctions.replicate( " ", 45 ) + CR );

					gravaBuffer( sBuffer.toString() );

					cont++;

				}

				gravaBuffer( sBuffer75.toString() ); // Gravando Buffer do registro 75 .

				rs.close();
				ps.close();
				con.commit();

			}

		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao gerar registro 74!\n" + e.getMessage(), true, con, e );
		}

		return cont;

	}

	private int geraRegistro75() {

		PreparedStatement ps;
		ResultSet rs;
		StringBuffer sSql = new StringBuffer();
		StringBuffer sBuffer = new StringBuffer();
		final int COL_75_CODPROD = 1;
		final int COL_75_REFPROD = 2;
		final int COL_75_DESCPROD = 3;
		final int COL_75_PERCIPI = 4;
		final int COL_75_PERCICMS = 5;
		final int COL_75_CODUNID = 6;
		final int COL_75_ORIGFISC = 7;
		final int COL_75_CODTRATTRIB = 8;
		final int COL_75_CODNCM = 9;
		String sConvenio = rgConvenio.getVlrString();
		String sSqlEntrada = "";
		String sSqlSaida = "";
		String sSqlConsumidor = "";
		String sSqlInventario = "";
		int iParam = 1;
		int cont = 0;

		try {

			if ( ( "S".equals( cbEntrada.getVlrString() ) ) || ( "S".equals( cbSaida.getVlrString() ) || "S".equals( cbConsumidor.getVlrString() ) || "S".equals( cbInventario.getVlrString() ) ) ) {

				// REGISTRO 75 TABELA DE PRODUTOS ENTRADAS, SAIDAS, CONSUMIDOR

				sSqlEntrada = "";
				sSqlSaida = "";
				sSqlConsumidor = "";

				if ( cbEntrada.getVlrString().equals( "S" ) ) {
					sSqlEntrada = "SELECT IC.CODPROD,P.REFPROD,P.DESCPROD,COALESCE(CF.ALIQIPIFISC,0)," 
						+ "COALESCE(CF.ALIQLFISC,0),P.CODUNID,CF.ORIGFISC,CF.CODTRATTRIB,P.CODFISC " 
						+ "FROM CPCOMPRA C,CPITCOMPRA IC,EQTIPOMOV TM,EQPRODUTO P,LFITCLFISCAL CF, CPFORNECED F "
						+ "WHERE C.DTENTCOMPRA BETWEEN ? AND ? AND C.CODEMP=? AND C.CODFILIAL=? AND " 
						+ "IC.CODCOMPRA=C.CODCOMPRA AND IC.CODEMP=C.CODEMP AND IC.CODFILIAL=C.CODFILIAL AND "
						+ "TM.CODTIPOMOV=C.CODTIPOMOV AND TM.CODEMP=C.CODEMPTM AND TM.CODFILIAL=C.CODFILIALTM AND "
						+ "P.CODPROD=IC.CODPROD AND P.CODEMP=IC.CODEMPPD AND P.CODFILIAL=IC.CODFILIALPD AND " 
						+ "CF.CODFISC=P.CODFISC AND CF.CODEMP=P.CODEMPFC AND CF.CODFILIAL=P.CODFILIALFC AND CF.GERALFISC='S' AND "
						+ "F.CODFOR=C.CODFOR AND F.CODEMP=C.CODEMPFR AND F.CODFILIAL=C.CODFILIALFR AND "
						+ "TM.FISCALTIPOMOV='S' "
						// Filtro para não incluir produtos do patrimônio. 
						+"and p.tipoprod<>'O' "; 

					
					
					
				}
				if ( cbSaida.getVlrString().equals( "S" ) ) {
					sSqlSaida = "SELECT IV.CODPROD,P.REFPROD,P.DESCPROD,COALESCE(CF.ALIQIPIFISC, 0), " 
						+ "COALESCE(CF.ALIQLFISC, 0),P.CODUNID,CF.ORIGFISC,CF.CODTRATTRIB,P.CODFISC " 
						+ "FROM VDVENDA V,VDITVENDA IV,EQTIPOMOV TM,EQPRODUTO P,VDCLIENTE C,LFITCLFISCAL CF "
							+ "WHERE V.DTEMITVENDA BETWEEN ? AND ? AND V.CODEMP=? AND V.CODFILIAL=? AND " 
							+ "C.CODCLI=V.CODCLI AND C.CODEMP=V.CODEMPCL AND C.CODFILIAL=V.CODFILIALCL AND " 
							+ "IV.CODVENDA=V.CODVENDA AND IV.TIPOVENDA=V.TIPOVENDA AND IV.CODEMP=V.CODEMP AND "
							+ "IV.CODFILIAL=V.CODFILIAL AND " + "TM.CODTIPOMOV=V.CODTIPOMOV AND TM.CODEMP=V.CODEMPTM AND " 
							+ "TM.CODFILIAL=V.CODFILIALTM AND TM.FISCALTIPOMOV='S' AND " 
							+ "( (C.PESSOACLI='J' AND V.TIPOVENDA='V') OR (V.TIPOVENDA='E') ) AND "
							+ "P.CODPROD=IV.CODPROD AND P.CODEMP=IV.CODEMPPD AND P.CODFILIAL=IV.CODFILIALPD AND " 
							+ "CF.CODFISC=P.CODFISC AND CF.CODEMP=P.CODEMPFC AND CF.CODFILIAL=P.CODFILIALFC AND CF.GERALFISC='S' AND TM.FISCALTIPOMOV='S' "
							// Filtro para não incluir produtos do patrimônio. 
							+"and p.tipoprod<>'O' "; 


					// Amarrado com o ítem padrão da classificação fiscal pois não deve repetir de acordo com as vendas as alíquotas
					// Informadas devem ser as correspondetes a vendas / ou compras dentro do estado.

				}
				if ( cbConsumidor.getVlrString().equals( "S" ) && ( sConvenio.equals( "1" ) ) ) // IV.PERCICMSITVENDA
					sSqlConsumidor = "SELECT IV.CODPROD,P.REFPROD,P.DESCPROD,COALESCE(CF.ALIQIPIFISC,0)," 
						+ "COALESCE(CF.ALIQLFISC,0),P.CODUNID,CF.ORIGFISC,CF.CODTRATTRIB,P.CODFISC " 
						+ "FROM VDVENDA V,VDITVENDA IV,EQTIPOMOV TM,EQPRODUTO P,VDCLIENTE C,LFITCLFISCAL CF "
							+ "WHERE V.DTEMITVENDA BETWEEN ? AND ? AND V.CODEMP=? AND V.CODFILIAL=? AND " 
							+ "C.CODCLI=V.CODCLI AND C.CODEMP=V.CODEMPCL AND C.CODFILIAL=V.CODFILIALCL AND " 
							+ "C.PESSOACLI='F' AND V.TIPOVENDA='V' AND "
							+ "IV.CODVENDA=V.CODVENDA AND IV.TIPOVENDA=V.TIPOVENDA AND IV.CODEMP=V.CODEMP AND " 
							+ "IV.CODFILIAL=V.CODFILIAL AND " + "TM.CODTIPOMOV=V.CODTIPOMOV AND TM.CODEMP=V.CODEMPTM AND " 
							+ "TM.CODFILIAL=V.CODFILIALTM AND TM.FISCALTIPOMOV='S' AND "
							+ "P.CODPROD=IV.CODPROD AND P.CODEMP=IV.CODEMPPD AND P.CODFILIAL=IV.CODFILIALPD AND " 
							+ "CF.CODFISC=P.CODFISC AND CF.CODEMP=P.CODEMPFC AND CF.CODFILIAL=P.CODFILIALFC AND CF.GERALFISC='S' AND TM.FISCALTIPOMOV='S' "
							// Filtro para não incluir produtos do patrimônio. 
							+"and p.tipoprod<>'O' "; 


				
				if ( cbInventario.getVlrString().equals( "S" ) ) {
					sSqlInventario = "SELECT P.CODPROD,P.REFPROD, p.descprod, COALESCE(CF.ALIQIPIFISC,0),COALESCE(CF.ALIQLFISC,0), PD.CODUNID,CF.ORIGFISC,CF.CODTRATTRIB,CF.CODFISC "
								   + "FROM EQRELINVPRODSP(?,?,?,null,null,null,?,null,null,null) P, LFITCLFISCAL CF, eqproduto pd "
								   + "WHERE P.SALDO > 0 "
								   + "and pd.codemp=? and pd.codfilial=? and pd.codprod=p.codprod AND PD.ATIVOPROD='S' "
 								   + "AND CF.CODFISC=Pd.CODFISC AND CF.CODEMP=Pd.CODEMPFC AND CF.CODFILIAL=Pd.CODFILIALFC "
 								   + "AND CF.GERALFISC='S' "
 									// Filtro para não incluir produtos do patrimônio. 
 									+"and pd.tipoprod<>'O' "; 


				}	
				

				
				if ( !sSqlEntrada.equals( "" ) ) 
					sSql.append( sSqlEntrada );

				if ( !sSqlSaida.equals( "" ) )
					sSql.append( ( sSql.length() > 0 ? " UNION " : "" ) + sSqlSaida );

				if ( !sSqlConsumidor.equals( "" ) )
					sSql.append( ( sSql.length() > 0 ? " UNION " : "" ) + sSqlConsumidor );

				if ( !sSqlInventario.equals( "" ) )
					sSql.append( ( sSql.length() > 0 ? " UNION " : "" ) + sSqlInventario );

				

				sSql.append( " GROUP BY 1,2,3,4,5,6,7,8,9 " );
				

				System.out.println( "reg75:" + sSql.toString() );

				ps = con.prepareStatement( sSql.toString() );
				iParam = 1;
				if ( !sSqlEntrada.equals( "" ) ) {
					ps.setDate( iParam++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
					ps.setDate( iParam++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
					ps.setInt( iParam++, iCodEmp );
					ps.setInt( iParam++, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
				}
				if ( !sSqlSaida.equals( "" ) ) {
					ps.setDate( iParam++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
					ps.setDate( iParam++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
					ps.setInt( iParam++, iCodEmp );
					ps.setInt( iParam++, ListaCampos.getMasterFilial( "VDVENDA" ) );
				}
				if ( !sSqlConsumidor.equals( "" ) ) {
					ps.setDate( iParam++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
					ps.setDate( iParam++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
					ps.setInt( iParam++, iCodEmp );
					ps.setInt( iParam++, ListaCampos.getMasterFilial( "VDVENDA" ) );
				}
				if ( !sSqlInventario.equals( "" ) ) { 
					 
					ps.setInt( iParam++, iCodEmp );
					ps.setInt( iParam++, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
					ps.setString( iParam++, "P" );
					ps.setDate( iParam++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
					ps.setInt( iParam++, iCodEmp );
					ps.setInt( iParam++, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
					
				}
				
				
				rs = ps.executeQuery();
				lbAnd.setText( "Gerando Tabela de Produtos de entradas e saídas..." );

				vProd75.clear();
				sBuffer75 = new StringBuffer();

				while ( rs.next() ) {

					sBuffer.delete( 0, sBuffer.length() );

					btGerar.setEnabled( false );

					sBuffer.append( "75" );
					sBuffer.append( Funcoes.dataAAAAMMDD( txtDataini.getVlrDate() ) );
					sBuffer.append( Funcoes.dataAAAAMMDD( txtDatafim.getVlrDate() ) );
					sBuffer.append( Funcoes.adicionaEspacos( rs.getString( ( sUsaRefProd.equals( "S" ) ? COL_75_REFPROD : COL_75_CODPROD ) ), 14 ) );
					sBuffer.append( Funcoes.copy( rs.getString( COL_75_CODNCM ), 8 ) );
					sBuffer.append( Funcoes.adicionaEspacos( rs.getString( COL_75_DESCPROD ), 53 ) );
					sBuffer.append( Funcoes.adicionaEspacos( rs.getString( COL_75_CODUNID ), 4 ) );// 97
					sBuffer.append( Funcoes.copy( rs.getString( COL_75_ORIGFISC ).trim() + rs.getString( COL_75_CODTRATTRIB ).trim(), 3 ) );
					sBuffer.append( Funcoes.transValor( rs.getString( COL_75_PERCIPI ), 4, 2, true ) );
					sBuffer.append( Funcoes.transValor( rs.getString( COL_75_PERCICMS ), 4, 2, true ) );
					sBuffer.append( StringFunctions.strZero( "0", 4 ) );
					sBuffer.append( Funcoes.transValor( "0", 14, 2, true ) + CR );

					// Caso não seja necessário a geração do registro 74 grava o buffer agora.
					// Do contrario as informações do registro 75 serão gravadas após o registro 74.

					if ( "N".equals( cbInventario.getVlrString() ) ) {
						gravaBuffer( sBuffer.toString() );
					}
					else {
						sBuffer75.append( sBuffer );
					}

					if ( !vProd75.contains( rs.getString( "CODPROD" ) ) ) {
						vProd75.addElement( rs.getString( "CODPROD" ) );
					}

					cont++;

				}

				rs.close();
				ps.close();
				con.commit();

			}

		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao gerar registro 75!\n" + e.getMessage(), true, con, e );
		}

		return cont;

	}

	private String retorna90( String sBufferAnt, String sTipo, int iTot ) {

		StringBuffer sBuf = new StringBuffer();
		if ( "".equals( sBufferAnt.trim() ) ) {
			sBuf.append( "90" );
			sBuf.append( sCnpjEmp );
			sBuf.append( sInscEmp );
		}
		sBuf.append( sTipo );
		sBuf.append( StringFunctions.strZero( String.valueOf( iTot ), 8 ) );

		return sBuf.toString();

	}

	private void gravaBuffer( String sBuf ) {

		try {

			oswSintegra.write( sBuf );
			oswSintegra.flush();
		} catch ( IOException err ) {
			Funcoes.mensagemErro( this, "Erro grando no arquivo: '" + sFileName + "\n" + err.getMessage(), true, con, err );
		}

	}

	private boolean valida() {

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return false;
		}
		else if ( ( cbEntrada.getVlrString() != "S" ) & ( cbSaida.getVlrString() != "S" ) & ( cbInventario.getVlrString() != "S" ) & ( cbFrete.getVlrString() != "S" ) ) {
			Funcoes.mensagemInforma( this, "Nenhuma operação foi selecionada!" );
			return false;
		}

		FileDialog fdSintegra = null;
		fdSintegra = new FileDialog( Aplicativo.telaPrincipal, "Salvar sintegra", FileDialog.SAVE );
		fdSintegra.setFile( "Receita.txt" );
		fdSintegra.setVisible( true );
		if ( fdSintegra.getFile() == null )
			return false;

		sFileName = fdSintegra.getDirectory() + fdSintegra.getFile();

		return true;

	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btGerar ) {
			// iniGerar();
			gerar();
		}
	}

}
