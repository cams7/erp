/**
 * @version 07/2007 <BR>
 * @author Setpoint Informática Ltda. / Alex Rodrigues<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FRemCnab.java <BR>
 * 
 *                   Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                   modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                   na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                   Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                   sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                   Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                   Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                   de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                   Tela de remessa de arquivo de CNAB.
 * 
 */

package org.freedom.modulos.fnc.view.frame.utility;

import java.awt.FileDialog;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.business.component.Banco;
import org.freedom.library.business.exceptions.ExceptionCnab;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.fnc.business.component.cnab.CnabUtil;
import org.freedom.modulos.fnc.business.component.cnab.Reg;
import org.freedom.modulos.fnc.business.component.cnab.Reg1;
import org.freedom.modulos.fnc.business.component.cnab.Reg3P;
import org.freedom.modulos.fnc.business.component.cnab.Reg3Q;
import org.freedom.modulos.fnc.business.component.cnab.Reg3R;
import org.freedom.modulos.fnc.business.component.cnab.Reg3S;
import org.freedom.modulos.fnc.business.component.cnab.Reg5;
import org.freedom.modulos.fnc.business.component.cnab.RegHeader;
import org.freedom.modulos.fnc.business.component.cnab.RegT400;
import org.freedom.modulos.fnc.business.component.cnab.RegTrailer;
import org.freedom.modulos.fnc.business.component.siacc.SiaccUtil;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.EColrec;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.EPrefs;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.StuffCli;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.StuffRec;
import org.freedom.modulos.fnc.view.dialog.report.DLRRemessa;

public class FRemCnab extends FRemFBN {

	public static final long serialVersionUID = 1L;

	private final CnabUtil cnabutil = new CnabUtil();

	public static int loteServico = 0;

	private int seqLoteServico = 1;

	private int seqregistro = 1;

	private int codMovimento = 1;

	public FRemCnab() {

		super( TIPO_FEBRABAN_CNAB );
	}

	private String[] getCliente( final int codcli ) {

		String[] args = new String[ 10 ];

		try {

			StringBuilder sql = new StringBuilder();
			sql.append( "SELECT C.RAZCLI, C.ENDCLI, C.NUMCLI, C.BAIRCLI, C.CEPCLI, C.CIDCLI, C.UFCLI, C.CNPJCLI, C.CPFCLI " );
			sql.append( "FROM VDCLIENTE C " );
			sql.append( "WHERE C.CODEMP=? AND C.CODFILIAL=? AND C.CODCLI=?  " );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
			ps.setInt( 3, codcli );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {

				for ( int i = 0; i < args.length - 1; i++ ) {
					args[ i ] = rs.getString( i + 1 );
				}

				args[ DadosCliente.CNPJCPF.ordinal() ] = args[ DadosCliente.CNPJ.ordinal() ] != null ? "2" : "1";
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}

		return args;
	}

	private HashMap<String, Object> getCarteiraCobranca( final int codrec, final int nparc ) {

		int carteira = 0;
		String variacao = "";
		String codCarteiraCnab = "";
		HashMap<String, Object> ret = new HashMap<String, Object>();

		try {

			StringBuilder sql = new StringBuilder();
			sql.append( "SELECT CB.CARTCOBCNAB, CB.VARIACAOCARTCOB, CB.CODCARTCOB, CB.CODCARTCOBCNAB " );
			sql.append( "FROM FNCARTCOB CB, FNITRECEBER IR " );
			sql.append( "WHERE CB.CODEMPBO=IR.CODEMPBO AND CB.CODFILIAL=IR.CODFILIALBO AND CB.CODBANCO=IR.CODBANCO AND " );
			sql.append( "CB.CODEMP=IR.CODEMPCB AND CB.CODFILIAL=IR.CODFILIALCB AND CB.CODCARTCOB=IR.CODCARTCOB AND " );
			sql.append( "IR.CODEMP=? AND IR.CODFILIAL=? AND IR.CODREC=? AND IR.NPARCITREC=?" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNCARTCOB" ) );
			ps.setInt( 3, codrec );
			ps.setInt( 4, nparc );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {

				carteira = rs.getInt( "CARTCOBCNAB" );
				variacao = rs.getString( "VARIACAOCARTCOB" ) != null ? rs.getString( "VARIACAOCARTCOB" ).trim() : "";
				codCarteiraCnab = rs.getString( "CODCARTCOBCNAB" ) != null ? rs.getString( "CODCARTCOBCNAB" ).trim() : "";

			}

			ret.put( "CARTEIRA", carteira );
			ret.put( "VARIACAO", variacao );
			ret.put( "CODCARTEIRACNAB", codCarteiraCnab );

			rs.close();
			ps.close();
			con.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}

		return ret;
	}

	private RegHeader getRegHeader() throws ExceptionCnab {

		RegHeader reg = new RegHeader();

		reg.setCodBanco( txtCodBanco.getVlrString() );
		reg.setTipoInscEmp( 2 );
		reg.setCpfCnpjEmp( (String) prefs.get( EPrefs.CNPFEMP ) );
		reg.setCodConvBanco( (String) prefs.get( EPrefs.CODCONV ) );
		reg.setAgencia( (String) prefs.get( EPrefs.AGENCIA ) );
		reg.setDigAgencia( (String) prefs.get( EPrefs.DIGAGENCIA ) );
		reg.setPosto( (String) prefs.get( EPrefs.POSTOCONTA ) );
		reg.setConta( (String) prefs.get( EPrefs.NUMCONTA ) );
		reg.setDigConta( (String) prefs.get( EPrefs.DIGCONTA ) );
		reg.setDigAgConta( null );
		reg.setRazEmp( ( (String) prefs.get( FbnUtil.EPrefs.NOMEEMPCNAB ) ).toUpperCase() );
		reg.setNomeBanco( txtNomeBanco.getVlrString().toUpperCase() );

		Calendar cal = Calendar.getInstance();

		reg.setDataGeracao( cal.getTime() );
		reg.setHoraGeracao( new Integer( String.valueOf( cal.get( Calendar.HOUR_OF_DAY ) ) + String.valueOf( cal.get( Calendar.MINUTE ) ) + String.valueOf( cal.get( Calendar.SECOND ) ) ) );
		reg.setSequenciaArq( (Integer) prefs.get( EPrefs.NROSEQ ) );
		reg.setUsoBanco( null );
		reg.setUsoEmp( null );
		reg.setUsoVans( null );

		reg.setTipoServico( null );
		reg.setOcorrencias( null );

		return reg;
	}

	private Reg1 getReg1( final StuffRec rec ) throws ExceptionCnab {

		Reg1 reg = new Reg1();

		reg.setCodBanco( txtCodBanco.getVlrString() );
		reg.setLoteServico( loteServico );
		reg.setTipoOperacao( "R" );
		reg.setFormaLancamento( "00" );
		reg.setTipoInscEmp( 2 );
		reg.setCpfCnpjEmp( (String) prefs.get( EPrefs.CNPFEMP ) );
		reg.setCodConvBanco( (String) prefs.get( EPrefs.CODCONV ) );
		reg.setAgencia( (String) prefs.get( EPrefs.AGENCIA ) );
		reg.setDigAgencia( (String) prefs.get( EPrefs.DIGAGENCIA ) );
		reg.setPosto( (String) prefs.get( EPrefs.POSTOCONTA ) );
		reg.setConta( (String) prefs.get( EPrefs.NUMCONTA ) );
		reg.setDigConta( (String) prefs.get( EPrefs.DIGCONTA ) );
		reg.setDigAgConta( null );
		reg.setRazEmp( (String) prefs.get( FbnUtil.EPrefs.NOMEEMP ) );
		reg.setMsg1( null );
		reg.setMsg2( null );
		reg.setNrRemRet( (Integer) prefs.get( FbnUtil.EPrefs.NROSEQ ) );
		reg.setDataRemRet( Calendar.getInstance().getTime() );
		reg.setDataCred( null );

		return reg;
	}

	// Registro de transação CNAB 400
	private RegT400 getRegT400( final StuffRec rec, int seqregistro ) throws ExceptionCnab {

		RegT400 reg = new RegT400();

		try {

			Banco banco = FbnUtil.getBanco( txtCodBanco.getVlrString() );

			reg.setCodBanco( txtCodBanco.getVlrString() );
			reg.setLoteServico( loteServico );
			reg.setTipoOperacao( "R" );
			reg.setFormaLancamento( "00" );
			reg.setTipoInscEmp( 2 );
			reg.setCpfCnpjEmp( (String) prefs.get( EPrefs.CNPFEMP ) );
			reg.setCodConvBanco( (String) prefs.get( EPrefs.CODCONV ) );
			reg.setAgencia( (String) prefs.get( EPrefs.AGENCIA ) );
			reg.setDigAgencia( (String) prefs.get( EPrefs.DIGAGENCIA ) );
			reg.setConta( (String) prefs.get( EPrefs.NUMCONTA ) );
			reg.setDigConta( (String) prefs.get( EPrefs.DIGCONTA ) );
			reg.setDigAgConta( null );
			reg.setRazEmp( (String) prefs.get( FbnUtil.EPrefs.NOMEEMP ) );
			reg.setMsg1( null );
			reg.setMsg2( null );
			reg.setNrRemRet( (Integer) prefs.get( FbnUtil.EPrefs.NROSEQ ) );
			reg.setDataRemRet( Calendar.getInstance().getTime() );
			reg.setDataCred( null );

			HashMap<String, Object> infocarteira = getCarteiraCobranca( rec.getCodrec(), rec.getNParcitrec() );

			reg.setCodCarteira( (Integer) infocarteira.get( "CARTEIRA" ) );
			reg.setCodCarteiraCnab( (String) infocarteira.get( "CODCARTEIRACNAB" ) );

			if ( Banco.SICRED.equals( txtCodBanco.getVlrString() ) ) {
				reg.setIdentTitEmp( Banco.getIdentTitEmp( (long) rec.getCodrec(), (long) rec.getNParcitrec(), 10 ) );
			}
			else {
				reg.setIdentTitEmp( Banco.getIdentTitEmp( (long) rec.getCodrec(), (long) rec.getNParcitrec(), 25 ) );
			}

			if ( ( Banco.BANCO_DO_BRASIL.equals( txtCodBanco.getVlrString() ) ) && ( reg.getCodConvBanco().length() >= 7 ) ) {
				reg.setIdentTitulo( StringFunctions.strZero(
						banco.geraNossoNumero( (String) prefs.get( EPrefs.TPNOSSONUMERO ), (String) prefs.get( EPrefs.MDECOB ), reg.getCodConvBanco(), Long.parseLong( rec.getDocrec().toString() ), Long.parseLong( rec.getSeqrec().toString() ), Long.parseLong( rec.getCodrec().toString() ),
								Long.parseLong( rec.getNParcitrec().toString() ), rec.getDtemit(), false ), 17 ) );
			}
			else if ( Banco.SICRED.equals( txtCodBanco.getVlrString() ) ) {
				reg.setIdentTitulo( StringFunctions.strZero(
						banco.geraNossoNumero( (String) prefs.get( EPrefs.TPNOSSONUMERO ), (String) prefs.get( EPrefs.MDECOB ), reg.getCodConvBanco(), Long.parseLong( rec.getDocrec().toString() ), Long.parseLong( rec.getSeqrec().toString() ), Long.parseLong( rec.getCodrec().toString() ),
								Long.parseLong( rec.getNParcitrec().toString() ), rec.getDtemit(), true ), 9 ) );
			}
			else if ( Banco.ITAU.equals( txtCodBanco.getVlrString() ) ) {
				reg.setIdentTitulo( StringFunctions.strZero(
						banco.geraNossoNumero( (String) prefs.get( EPrefs.TPNOSSONUMERO ), (String) prefs.get( EPrefs.MDECOB ), reg.getCodConvBanco(), Long.parseLong( rec.getDocrec().toString() ), Long.parseLong( rec.getSeqrec().toString() ), Long.parseLong( rec.getCodrec().toString() ),
								Long.parseLong( rec.getNParcitrec().toString() ), rec.getDtemit(), true ), 8 ) );
			}
			else {
				reg.setIdentTitulo( StringFunctions.strZero(
						banco.geraNossoNumero( (String) prefs.get( EPrefs.TPNOSSONUMERO ), (String) prefs.get( EPrefs.MDECOB ), reg.getCodConvBanco(), Long.parseLong( rec.getDocrec().toString() ), Long.parseLong( rec.getSeqrec().toString() ), Long.parseLong( rec.getCodrec().toString() ),
								Long.parseLong( rec.getNParcitrec().toString() ), rec.getDtemit(), true ), 11 ) );
			}

			if ( ( Banco.BANCO_DO_BRASIL.equals( txtCodBanco.getVlrString() ) ) && ( reg.getCodConvBanco().length() < 7 ) ) {
				reg.setDigNossoNumero( banco.digVerif( reg.getIdentTitulo(), 11, true ) );
			}
			else if ( !Banco.BANCO_DO_BRASIL.equals( txtCodBanco.getVlrString() ) ) {
				reg.setDigNossoNumero( banco.getModulo11( reg.getCodCarteira() + reg.getIdentTitulo(), 7 ) );
			}

			reg.setVlrPercMulta( new BigDecimal( 0 ) );

			reg.setVariacaoCarteira( (String) infocarteira.get( "VARIACAO" ) );

			reg.setCodMovimento( codMovimento );

			reg.setIdentEmitBol( (Integer) prefs.get( EPrefs.IDENTEMITBOL ) );

			reg.setAceite( ( (String) prefs.get( EPrefs.ACEITE ) ).charAt( 0 ) );

			if ( "S".equals( (String) prefs.get( EPrefs.IMPDOCBOL ) ) ) {
				reg.setDocCobranca( String.valueOf( rec.getDocrec() ) + "/" + String.valueOf( rec.getNParcitrec() ) );
			}
			else {
				reg.setDocCobranca( banco.getNumCli( (String) prefs.get( EPrefs.TPNOSSONUMERO ), (String) prefs.get( EPrefs.MDECOB ), (String) prefs.get( EPrefs.CONVCOB ), Long.parseLong( rec.getDocrec().toString() ), Long.parseLong( rec.getSeqrec().toString() ),
						Long.parseLong( rec.getCodrec().toString() ), Long.parseLong( rec.getNParcitrec().toString() ) ) );
			}
			reg.setDtVencTitulo( CnabUtil.stringAAAAMMDDToDate( rec.getArgs()[ EColrec.DTVENC.ordinal() ] ) );

			reg.setVlrTitulo( new BigDecimal( rec.getArgs()[ EColrec.VLRAPAG.ordinal() ] ) );

			reg.setEspecieTit( (Integer) prefs.get( EPrefs.ESPECTIT ) );

			reg.setDtEmitTit( CnabUtil.stringAAAAMMDDToDate( rec.getArgs()[ EColrec.DTREC.ordinal() ] ) );

			reg.setCodJuros( (Integer) prefs.get( EPrefs.CODJUROS ) );

			reg.setVlrJurosTaxa( (BigDecimal) prefs.get( EPrefs.VLRPERCJUROS ) );

			reg.setVlrPercMulta( (BigDecimal) prefs.get( EPrefs.VLRPERCMULTA ) );

			if ( "S".equals( rec.getArgs()[ EColrec.DESCPONT.ordinal() ] ) ) {
				reg.setDescpont( true );
			}
			else {
				reg.setDescpont( false );
			}

			reg.setDtDesc( CnabUtil.stringAAAAMMDDToDate( rec.getArgs()[ EColrec.DTVENC.ordinal() ] ) ); // Data limite para desconto (Implementar) Foi informada a data do vencimento.

			// reg.setVlrDesc( (BigDecimal) prefs.get( EPrefs.VLRPERCDESC ) ); // Valor de desconto concedido para antecipação.
			reg.setVlrDesc( new BigDecimal( rec.getArgs()[ EColrec.VLRDESC.ordinal() ] ) );// Valor de desconto concedido para antecipação.

			reg.setVlrIOF( new BigDecimal( 0 ) ); // Só deve ser preenchido por empresas de seguro

			reg.setVlrAbatimento( new BigDecimal( 0 ) );

			reg.setCodProtesto( (Integer) prefs.get( EPrefs.CODPROT ) );

			reg.setDiasProtesto( (Integer) prefs.get( EPrefs.DIASPROT ) );

			String[] dadosCliente = getCliente( Integer.parseInt( rec.getArgs()[ EColrec.CODCLI.ordinal() ] ) );

			reg.setTipoInscCli( Integer.parseInt( dadosCliente[ DadosCliente.CNPJCPF.ordinal() ] ) );

			if ( 2 == reg.getTipoInscCli() ) {
				reg.setCpfCnpjCli( dadosCliente[ DadosCliente.CNPJ.ordinal() ] );
			}
			else if ( 1 == reg.getTipoInscCli() ) {
				reg.setCpfCnpjCli( dadosCliente[ DadosCliente.CPF.ordinal() ] );
			}
			else {
				reg.setTipoInscCli( 0 );
				reg.setCpfCnpjCli( "0" );
			}

			reg.setRazCli( StringFunctions.clearAccents( dadosCliente[ DadosCliente.RAZCLI.ordinal() ] ).toUpperCase() );

			String logradouro = dadosCliente[ DadosCliente.ENDCLI.ordinal() ].trim();
			String numero = dadosCliente[ DadosCliente.NUMCLI.ordinal() ];
			String bairro = dadosCliente[ DadosCliente.BAIRCLI.ordinal() ];
			reg.setBairCli( bairro );

			String endereco_completo = logradouro + ( numero != null ? ", " + numero : "" );

			reg.setEndCli( StringFunctions.clearAccents( endereco_completo ).toUpperCase() );

			// reg.setEndCli( ( dadosCliente[ DadosCliente.ENDCLI.ordinal() ].trim() + ( dadosCliente[ DadosCliente.NUMCLI.ordinal() ] == null ? " " : ( ", " + dadosCliente[ DadosCliente.NUMCLI.ordinal() ].trim() ) ) ) + "-" + dadosCliente[ DadosCliente.BAIRCLI.ordinal() ] == null ? "" :
			// dadosCliente[ DadosCliente.BAIRCLI.ordinal() ].trim() );

			// reg.setBairCli( dadosCliente[ DadosCliente.BAIRCLI.ordinal() ] );
			reg.setCepCli( dadosCliente[ DadosCliente.CEPCLI.ordinal() ] );
			reg.setCidCli( dadosCliente[ DadosCliente.CIDCLI.ordinal() ] );
			reg.setUfCli( dadosCliente[ DadosCliente.UFCLI.ordinal() ] );

			reg.setInstrucoes( (Integer) prefs.get( EPrefs.CODINSTR ) );
			reg.setOutrasInstrucoes( (Integer) prefs.get( EPrefs.CODOUTINSTR ) );

			reg.setTipoInscAva( 0 );
			reg.setCpfCnpjAva( null );
			reg.setRazAva( null );

			reg.setMsg1( null );
			reg.setMsg2( null );
			reg.setSeqregistro( seqregistro );
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return reg;
	}

	private Reg3P getReg3P( final StuffRec rec ) throws ExceptionCnab {

		Banco banco = FbnUtil.getBanco( txtCodBanco.getVlrString() );

		Reg3P reg = new Reg3P();

		reg.setCodBanco( txtCodBanco.getVlrString() );
		reg.setLoteServico( loteServico );
		reg.setSeqLote( seqLoteServico++ );
		reg.setCodMovimento( codMovimento );
		reg.setAgencia( (String) prefs.get( EPrefs.AGENCIA ) );
		reg.setDigAgencia( (String) prefs.get( EPrefs.DIGAGENCIA ) );
		reg.setConta( (String) prefs.get( EPrefs.NUMCONTA ) );
		reg.setDigConta( (String) prefs.get( EPrefs.DIGCONTA ) );
		reg.setDigAgConta( null );

		HashMap<String, Object> infocarteira = getCarteiraCobranca( rec.getCodrec(), rec.getNParcitrec() );

		reg.setIdentTitulo( banco.geraNossoNumero( (String) prefs.get( EPrefs.TPNOSSONUMERO ), (String) prefs.get( EPrefs.MDECOB ), (String) prefs.get( EPrefs.CONVCOB ), Long.parseLong( rec.getDocrec().toString() ), Long.parseLong( rec.getSeqrec().toString() ),
				Long.parseLong( rec.getCodrec().toString() ), Long.parseLong( rec.getNParcitrec().toString() ), rec.getDtemit(), true ) );

		reg.setCodCarteira( (Integer) infocarteira.get( "CARTEIRA" ) );
		reg.setFormaCadTitulo( (Integer) prefs.get( EPrefs.FORCADTIT ) );
		reg.setTipoDoc( (Integer) prefs.get( EPrefs.TIPODOC ) );
		reg.setIdentEmitBol( (Integer) prefs.get( EPrefs.IDENTEMITBOL ) );
		reg.setIdentDist( (Integer) prefs.get( EPrefs.IDENTDISTBOL ) );

		if ( "S".equals( (String) prefs.get( EPrefs.IMPDOCBOL ) ) ) {
			reg.setDocCobranca( String.valueOf( rec.getDocrec() ) + "/" + String.valueOf( rec.getNParcitrec() ) );
		}
		else {
			reg.setDocCobranca( banco.getNumCli( (String) prefs.get( EPrefs.TPNOSSONUMERO ), (String) prefs.get( EPrefs.MDECOB ), (String) prefs.get( EPrefs.CONVCOB ), Long.parseLong( rec.getDocrec().toString() ), Long.parseLong( rec.getSeqrec().toString() ),
					Long.parseLong( rec.getCodrec().toString() ), Long.parseLong( rec.getNParcitrec().toString() ) ) );
		}

		reg.setDtVencTitulo( CnabUtil.stringAAAAMMDDToDate( rec.getArgs()[ EColrec.DTVENC.ordinal() ] ) );
		reg.setVlrTitulo( new BigDecimal( rec.getArgs()[ EColrec.VLRAPAG.ordinal() ] ) );
		reg.setAgenciaCob( null );
		reg.setDigAgenciaCob( null );
		reg.setEspecieTit( (Integer) prefs.get( EPrefs.ESPECTIT ) );
		reg.setAceite( ( (String) prefs.get( EPrefs.ACEITE ) ).charAt( 0 ) );
		reg.setDtEmitTit( CnabUtil.stringAAAAMMDDToDate( rec.getArgs()[ EColrec.DTREC.ordinal() ] ) );
		reg.setCodJuros( (Integer) prefs.get( EPrefs.CODJUROS ) );

		// se for isento de juros.
		if ( 3 == reg.getCodJuros() ) {
			reg.setDtJuros( null );
		}
		else {
			reg.setDtJuros( CnabUtil.stringAAAAMMDDToDate( rec.getArgs()[ EColrec.DTVENC.ordinal() ] ) );
		}

		reg.setVlrJurosTaxa( (BigDecimal) prefs.get( EPrefs.VLRPERCJUROS ) );

		reg.setVlrPercMulta( (BigDecimal) prefs.get( EPrefs.VLRPERCMULTA ) );

		reg.setCodDesc( (Integer) prefs.get( EPrefs.CODDESC ) );
		reg.setDtDesc( null );
		reg.setVlrpercConced( (BigDecimal) prefs.get( EPrefs.VLRPERCDESC ) );
		reg.setVlrIOF( new BigDecimal( 0 ) );
		reg.setVlrAbatimento( new BigDecimal( 0 ) );
		reg.setIdentTitEmp( Banco.getIdentTitEmp( (long) rec.getCodrec(), (long) rec.getNParcitrec(), 14 ) );
		reg.setCodProtesto( (Integer) prefs.get( EPrefs.CODPROT ) );
		reg.setDiasProtesto( (Integer) prefs.get( EPrefs.DIASPROT ) );
		reg.setCodBaixaDev( (Integer) prefs.get( EPrefs.CODBAIXADEV ) );
		reg.setDiasBaixaDevol( (Integer) prefs.get( EPrefs.DIASBAIXADEV ) );
		reg.setCodMoeda( 9 );
		reg.setContrOperCred( null );
		return reg;
	}

	private Reg3Q getReg3Q( final StuffRec rec ) throws ExceptionCnab {

		Reg3Q reg = new Reg3Q();

		reg.setCodBanco( txtCodBanco.getVlrString() );
		reg.setLoteServico( loteServico );
		reg.setSeqLote( seqLoteServico++ );
		reg.setCodMovimento( codMovimento );

		String[] dadosCliente = getCliente( Integer.parseInt( rec.getArgs()[ EColrec.CODCLI.ordinal() ] ) );

		reg.setTipoInscCli( Integer.parseInt( dadosCliente[ DadosCliente.CNPJCPF.ordinal() ] ) );

		if ( 2 == reg.getTipoInscCli() ) {
			reg.setCpfCnpjCli( dadosCliente[ DadosCliente.CNPJ.ordinal() ] );
		}
		else if ( 1 == reg.getTipoInscCli() ) {
			reg.setCpfCnpjCli( dadosCliente[ DadosCliente.CPF.ordinal() ] );
		}
		else {
			reg.setTipoInscCli( 0 );
			reg.setCpfCnpjCli( "0" );
		}

		reg.setRazCli( dadosCliente[ DadosCliente.RAZCLI.ordinal() ] );
		reg.setEndCli( dadosCliente[ DadosCliente.ENDCLI.ordinal() ].trim() + ", " + dadosCliente[ DadosCliente.NUMCLI.ordinal() ] + "-" + dadosCliente[ DadosCliente.BAIRCLI.ordinal() ] );

		// reg.setBairCli( dadosCliente[ DadosCliente.BAIRCLI.ordinal() ] );
		reg.setCepCli( dadosCliente[ DadosCliente.CEPCLI.ordinal() ] );
		reg.setCidCli( dadosCliente[ DadosCliente.CIDCLI.ordinal() ] );
		reg.setUfCli( dadosCliente[ DadosCliente.UFCLI.ordinal() ] );

		reg.setTipoInscAva( 0 );
		reg.setCpfCnpjAva( null );
		reg.setRazAva( null );
		// reg.setCodCompensacao( 0 );
		// reg.setNossoNumero( null );

		return reg;
	}

	private Reg3R getReg3R( final StuffRec rec ) throws ExceptionCnab {

		Reg3R reg = new Reg3R();

		reg.setCodBanco( txtCodBanco.getVlrString() );
		reg.setLoteServico( loteServico );
		reg.setSeqLote( seqLoteServico++ );
		reg.setCodMovimento( codMovimento );

		reg.setCodDesc2( 6 );
		reg.setDataDesc2( null );
		reg.setVlrPercConced2( new BigDecimal( 0 ) );
		reg.setCodDesc3( 0 );
		reg.setDataDesc3( null );
		reg.setVlrPercConced3( new BigDecimal( 0 ) );
		reg.setCodMulta( 2 );
		reg.setDataMulta( null );
		reg.setVlrPercMulta( new BigDecimal( 0 ) );
		reg.setMsgSacado( null );
		reg.setMsg3( null );
		reg.setMsg4( null );

		reg.setCodBancoDeb( null );
		reg.setAgenciaDeb( null );
		reg.setContaDeb( null );

		// sreg.setCodOcorrSacado( 0 );

		return reg;
	}

	private Reg3S getReg3S( final StuffRec rec ) throws ExceptionCnab {

		Reg3S reg = new Reg3S();

		reg.setCodBanco( txtCodBanco.getVlrString() );
		reg.setLoteServico( loteServico );
		reg.setSeqLote( seqLoteServico++ );
		reg.setCodMovimento( codMovimento );

		reg.setTipoImpressao( 3 );
		reg.setLinhaImp( 10 );// verificar...
		reg.setMsgImp( null );
		reg.setTipoChar( 1 );
		reg.setMsg5( null );// verificar...
		reg.setMsg6( null );// verificar...
		reg.setMsg7( null );// verificar...
		reg.setMsg8( null );// verificar...
		reg.setMsg9( null );// verificar...

		return reg;
	}

	/*
	 * 
	 * private Reg3T getReg3T( final StuffRec rec ) {
	 * 
	 * Reg3T reg = new Reg3T();
	 * 
	 * reg.setCodBanco( txtCodBanco.getVlrString() ); reg.setLoteServico( loteServico ); reg.setSeqLote( seqLoteServico++ ); reg.setCodMovimento( codMovimento ); reg.setAgencia( (String) prefs.get( EPrefs.AGENCIA ) ); reg.setDigAgencia( (String) prefs.get( EPrefs.DIGAGENCIA ) ); reg.setConta(
	 * (String) prefs.get( EPrefs.NUMCONTA ) ); reg.setDigConta( (String) prefs.get( EPrefs.DIGCONTA ) ); reg.setDigAgConta( null ); reg.setIdentTitBanco( Boleto.geraNossoNumero( getModalidade( txtCodBanco.getVlrInteger() ), (String)prefs.get( EPrefs.CODCONV ), Long.parseLong(
	 * rec.getDocrec().toString() ), Long.parseLong( rec.getNParcitrec().toString() ) ) ); reg.setCarteira( getCarteiraCobranca( rec.getCodrec(), rec.getNParcitrec() ) ); reg.setDocCob( rec.getArgs()[ EColrec.DOCREC.ordinal() ] ); reg.setDataVencTit( null ); reg.setVlrTitulo( new BigDecimal(0) );
	 * 
	 * reg.setCodBanco( null ); reg.setAgenciaCob( null ); reg.setDigAgenciaCob( null );
	 * 
	 * reg.setIdentTitEmp( rec.getCodrec().toString() ); reg.setCodMoeda( 9 );
	 * 
	 * String[] dadosCliente = getCliente( Integer.parseInt( rec.getArgs()[ EColrec.CODCLI.ordinal() ] ) );
	 * 
	 * reg.setTipoInscCli( Integer.parseInt( dadosCliente[ DadosCliente.CNPJCPF.ordinal()] ) );
	 * 
	 * if ( 2 == reg.getTipoInscCli() ) { reg.setCpfCnpjCli( dadosCliente[ DadosCliente.CNPJ.ordinal() ] ); } else if ( 1 == reg.getTipoInscCli() ) { reg.setCpfCnpjCli( dadosCliente[ DadosCliente.CPF.ordinal() ] ); } else { reg.setTipoInscCli( 0 ); reg.setCpfCnpjCli( "0" ); }
	 * 
	 * reg.setRazCli( dadosCliente[ DadosCliente.RAZCLI.ordinal() ] ); reg.setContratoCred( (String) prefs.get( EPrefs.CODCONV ) ); reg.setVlrTarifa( new BigDecimal(0) ); reg.setCodRejeicoes( null );
	 * 
	 * return reg;}
	 * 
	 * private Reg3U getReg3U( final StuffRec rec ) {
	 * 
	 * Reg3U reg = new Reg3U();
	 * 
	 * reg.setCodBanco( txtCodBanco.getVlrString() ); reg.setLoteServico( loteServico ); reg.setSeqLote( seqLoteServico++ ); reg.setCodMovimento( codMovimento );
	 * 
	 * reg.setVlrJurosMulta( new BigDecimal(0) ); reg.setVlrDesc( new BigDecimal(0) ); reg.setVlrAbatCancel( new BigDecimal(0) ); reg.setVlrIOF( new BigDecimal(0) ); reg.setVlrPago( new BigDecimal(0) ); reg.setVlrLiqCred( new BigDecimal(0) ); reg.setVlrOutrasDesp( new BigDecimal(0) );
	 * reg.setVlrOutrosCred( new BigDecimal(0) ); reg.setDataOcorr( null ); reg.setDataEfetvCred( null ); reg.setCodOcorrSac( null ); reg.setDataOcorrSac( null ); reg.setVlrOcorrSac( new BigDecimal(0) ); reg.setCompOcorrSac( null ); reg.setCodBancoCompens( null ); //reg.setNossoNrCompens( null );
	 * 
	 * return reg;}
	 */
	private Reg5 getReg5() throws ExceptionCnab {

		Reg5 reg = new Reg5();

		reg.setCodBanco( txtCodBanco.getVlrString() );
		reg.setLoteServico( loteServico );
		reg.setQtdRegistros( ++seqLoteServico );
		reg.setQtdSimples( 0 );
		reg.setVlrSimples( new BigDecimal( 0 ) );
		reg.setQtdVinculado( 0 );
		reg.setVlrVinculado( new BigDecimal( 0 ) );
		reg.setQtdCalculado( 0 );
		reg.setVlrCalculado( new BigDecimal( 0 ) );
		reg.setQtdDescontado( 0 );
		reg.setVlrDescontado( new BigDecimal( 0 ) );
		reg.setAvisoLanca( null );

		return reg;
	}

	private RegTrailer getRegTrailer( int seqregistro ) throws ExceptionCnab {

		RegTrailer reg = new RegTrailer();

		reg.setCodBanco( txtCodBanco.getVlrString() );
		reg.setQtdLotes( loteServico );
		reg.setQtdRegistros( seqLoteServico + 2 );
		reg.setConta( (String) prefs.get( EPrefs.NUMCONTA ) );
		reg.setQtdConsilacoes( 0 );
		reg.setSeqregistro( seqregistro );

		return reg;
	}

	protected boolean execExporta( String convCob ) {

		boolean retorno = false;
		String sFileName = null;
		BufferedWriter bw = null;
		HashSet<StuffCli> hsCli = new HashSet<StuffCli>();
		HashSet<StuffRec> hsRec = new HashSet<StuffRec>();

		lbStatus.setText( "     Gerando nosso número ..." );

		ajustaNossoNumero();

		retorno = setPrefs( convCob );

		if ( retorno ) {

			if ( Banco.BRADESCO.equals( txtCodBanco.getVlrString() ) ) {
				Calendar clhoje = new GregorianCalendar();
				clhoje = Calendar.getInstance();
				String dia = StringFunctions.strZero( clhoje.get( Calendar.DAY_OF_MONTH ) + "", 2 );
				String mes = StringFunctions.strZero( ( clhoje.get( Calendar.MONTH ) + 1 ) + "", 2 );
				String seq = StringFunctions.strZero( prefs.get( EPrefs.NROSEQ ) + "", 2 );
				sFileName = "CB" + dia + mes + seq + ".REM";
			}
			else if ( Banco.SICRED.equals( txtCodBanco.getVlrString() ) ) {
				// 19221
				Calendar clhoje = new GregorianCalendar();
				clhoje = Calendar.getInstance();
				String dia = StringFunctions.strZero( clhoje.get( Calendar.DAY_OF_MONTH ) + "", 2 );
				String mes = StringFunctions.strZero( ( clhoje.get( Calendar.MONTH ) + 1 ) + "", 2 );
				String mesEditado = null;
				if ( "12".equals( mes ) ) {
					mesEditado = "D";
				}
				else if ( "11".equals( mes ) ) {
					mesEditado = "N";
				}
				else if ( "10".equals( mes ) ) {
					mesEditado = "O";
				}
				else {
					mesEditado = mes.substring( 1, 2 );
				}
				String seq = StringFunctions.strZero( prefs.get( EPrefs.NROSEQ ) + "", 2 );
				sFileName = (String) prefs.get( EPrefs.NUMCONTA ) + mesEditado + dia + ".CRM";
			}
			else {
				sFileName = "remessa" + prefs.get( EPrefs.NROSEQ ) + ".txt";
			}
		}
		else {
			return retorno;
		}

		FileDialog fileDialogCnab = new FileDialog( Aplicativo.telaPrincipal, "Exportar arquivo.", FileDialog.SAVE );

		Object caminhoremessa = prefs.get( EPrefs.CAMINHOREMESSA );
		Object backupremessa = prefs.get( EPrefs.BACKUPREMESSA );

		if ( caminhoremessa != null ) {

			// Deve verificar se existem outros arquivos na pasta e solicitar backup

			if ( backupremessa != null ) {

				File diretorio_origem = new File( caminhoremessa.toString() );

				File arquivos[] = diretorio_origem.listFiles();

				if ( arquivos!=null && arquivos.length > 0 ) {

					if ( Funcoes.mensagemConfirma( this, "Existem " + arquivos.length + " arquivos na pasta de remessas,\nDeseja realizar o backup desses arquivos?" ) == JOptionPane.YES_OPTION ) {

						String prefixoback = "BKP_" + new Date().getTime() + "_";

						if ( Funcoes.moveFiles( caminhoremessa.toString(), backupremessa.toString(), prefixoback ) ) {

							Funcoes.mensagemInforma( this, "Backup realizado com sucesso!" );

						}

					}

				}

			}

			fileDialogCnab.setDirectory( caminhoremessa.toString() );

		}

		fileDialogCnab.setFile( sFileName );
		fileDialogCnab.setVisible( true );

		sFileName = fileDialogCnab.getDirectory() + fileDialogCnab.getFile();

		if ( fileDialogCnab.getFile() == null ) {
			lbStatus.setText( "" );
			return retorno;
		}

		if ( consisteExporta( hsCli, hsRec, false, sFileName ) ) {

			retorno = setPrefs( "" );

			if ( retorno ) {

				lbStatus.setText( "     criando arquivo ..." );

				try {

					File fileCnab = new File( sFileName );
					fileCnab.createNewFile();

					FileWriter fw = new FileWriter( fileCnab );
					bw = new BufferedWriter( fw );

					lbStatus.setText( "     gravando arquivo ..." );

					String padraocnab = prefs.get( EPrefs.PADRAOCNAB ).toString().trim();

					retorno = gravaRemessa( bw, hsCli, hsRec, padraocnab );

				} catch ( IOException ioError ) {

					Funcoes.mensagemErro( this, "Erro Criando o arquivo!\n " + sFileName + "\n" + ioError.getMessage() );

					ioError.printStackTrace();

					lbStatus.setText( "" );

					return retorno;

				}

				lbStatus.setText( "     pronto ..." );

				prefs.put( EPrefs.NROSEQ, ( (Integer) prefs.get( EPrefs.NROSEQ ) ).intValue() + 1 );
				updatePrefere();
				atualizaSitremessaExp( hsCli, hsRec, sFileName );
			}

		}

		if ( Funcoes.mensagemConfirma( this, "Deseja imprimir relação de títulos exportados?" ) == JOptionPane.YES_OPTION ) {
			imprimir( TYPE_PRINT.VIEW, true, sFileName );
		}

		return retorno;
	}

	private void atualizaSitremessaExp( HashSet<SiaccUtil.StuffCli> hsCli, HashSet<SiaccUtil.StuffRec> hsRec, String filename ) {

		setSitremessa( hsRec, "01" );
		persisteDados( hsCli, hsRec, filename );
		updatePrefere();
		atualizaNossoNumero( hsRec );

	}

	boolean atualizaNossoNumero( HashSet<SiaccUtil.StuffRec> hsRec ) {

		boolean retorno = false;

		try {

			PreparedStatement ps = null;

			Integer codrec;
			Integer nparcitrec;
			String nossonumero;

			StringBuilder sql = new StringBuilder();
			sql.append( "update fnitreceber set nossonumero=? where codemp=? and codfilial=? and codrec=? and nparcitrec=? " );

			for ( FbnUtil.StuffRec stfRec : hsRec ) {

				codrec = stfRec.getCodrec();
				nparcitrec = stfRec.getNParcitrec();
				nossonumero = stfRec.getNossonumero();

				ps = con.prepareStatement( sql.toString() );

				ps.setString( 1, nossonumero );
				ps.setInt( 2, Aplicativo.iCodEmp );
				ps.setInt( 3, ListaCampos.getMasterFilial( "FNITRECEBER" ) );
				ps.setInt( 4, codrec );
				ps.setInt( 5, nparcitrec );

				ps.executeUpdate();

			}

			ps.close();
			con.commit();

			retorno = true;

		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro atualizando situação do contas a receber!\n" + e.getMessage() );
		}

		return retorno;
	}

	private void setSitremessa( HashSet<SiaccUtil.StuffRec> hsRec, final String sit ) {

		for ( SiaccUtil.StuffRec sr : hsRec ) {
			sr.setSitremessa( sit );
		}
	}

	private boolean gravaRemessa( final BufferedWriter bw, final HashSet<StuffCli> hsCli, final HashSet<StuffRec> hsRec, String padraocnab ) {

		boolean retorno = false;

		try {

			loteServico++;
			seqLoteServico = 1;
			seqregistro = 1;

			int regs = 0;

			ArrayList<Reg> registros = new ArrayList<Reg>();

			registros.add( getRegHeader() );

			seqregistro++;

			if ( padraocnab.equals( Reg.CNAB_240 ) ) {
				registros.add( getReg1( null ) );
			}

			for ( StuffRec rec : hsRec ) {
				if ( padraocnab.equals( Reg.CNAB_240 ) ) {
					registros.add( getReg3P( rec ) );
					registros.add( getReg3Q( rec ) );
				}
				if ( padraocnab.equals( Reg.CNAB_400 ) ) {
					registros.add( getRegT400( rec, seqregistro ) );
					seqregistro++;
				}

				regs++;
			}

			if ( padraocnab.equals( Reg.CNAB_240 ) ) {
				registros.add( getReg5() );
			}

			registros.add( getRegTrailer( seqregistro ) );

			for ( Reg reg : registros ) {
				bw.write( reg.getLine( padraocnab ) );
			}

			bw.flush();
			bw.close();

			System.out.println( "[ " + regs + " ] registros gravados." );

		} catch ( ExceptionCnab e ) {

			Funcoes.mensagemErro( this, e.getMessage() );

			e.printStackTrace();
			lbStatus.setText( "" );
			retorno = false;

		} catch ( IOException ioError ) {

			Funcoes.mensagemErro( this, "Erro gravando no arquivo!\n" + ioError.getMessage() );
			ioError.printStackTrace();
			lbStatus.setText( "" );
			retorno = false;

		}

		return retorno;
	}

	public void imprimir( TYPE_PRINT visualizar ) {

		imprimir( visualizar, false, null );

	}

	public void imprimir( TYPE_PRINT visualizar, boolean exportados, String filename ) {

		if ( txtCodBanco.getVlrString().equals( "" ) ) {
			Funcoes.mensagemInforma( this, "Código do banco é requerido!" );
			return;
		}

		try {

			DLRRemessa dl = new DLRRemessa( this );
			String formato = null;

			dl.setVisible( true );

			if ( dl.OK == true ) {

				formato = dl.getFormato();
				dl.dispose();

			}
			else {
				dl.dispose();
				return;
			}

			ResultSet rs = executeQuery( exportados, filename );

			HashMap<String, Object> hParam = new HashMap<String, Object>();

			hParam.put( "CODEMP", Aplicativo.iCodEmp );
			hParam.put( "REPORT_CONNECTION", con.getConnection() );
			hParam.put( "SUBREPORT_DIR", "org/freedom/relatorios/" );

			if ( filename != null ) {

				hParam.put( "FILTROS", "Títulos registrados no arquivo: " + filename );

			}

			FPrinterJob dlGr = null;

			if ( "R".equals( formato ) ) {
				dlGr = new FPrinterJob( "relatorios/RemCNAB.jasper", "RELATÓRIO DE REMESSA", null, rs, hParam, this );
			}
			else {
				dlGr = new FPrinterJob( "relatorios/RemSiacci.jasper", "RELATÓRIO DE REMESSA", null, rs, hParam, this );
			}

			if ( visualizar == TYPE_PRINT.VIEW ) {
				dlGr.setVisible( true );
			}
			else {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			}

			rs.close();

			con.commit();
		} catch ( Exception e ) {
			Funcoes.mensagemErro( this, "Erro ao montar relatorio!\n" + e.getMessage() );
			e.printStackTrace();
		}
	}

	@ Override
	public void mouseClicked( MouseEvent e ) {

		if ( e.getSource() == tab ) {

			if ( e.getClickCount() == 2 && tab.getLinhaSel() > -1 ) {

				/*
				 * if ( !"00".equals( tab.getValor( tab.getLinhaSel(), EColTab.COL_SITRET.ordinal() ) ) ) {
				 * 
				 * Funcoes.mensagemInforma( this, "Registro rejeitado!\n" + getMenssagemRet( (String) tab.getValor( tab.getLinhaSel(), EColTab.COL_SITRET.ordinal() ) ) ); }
				 */
			}
			calcSelecionado();
		}

	}

	protected void ajustaNossoNumero() {

		String nossonumero = "";
		Integer codrec = null;
		Integer nparcitrec = null;

		Integer setregistro = 1;

		for ( int i = 0; i < tab.getNumLinhas(); i++ ) {

			codrec = Integer.parseInt( tab.getValor( i, EColTab.COL_CODREC.ordinal() ).toString() );
			nparcitrec = Integer.parseInt( tab.getValor( i, EColTab.COL_NRPARC.ordinal() ).toString() );

			nossonumero = FbnUtil.getNossoNumero( con, codrec, nparcitrec, TIPO_FEBRABAN_CNAB, seqregistro );

			tab.setValor( nossonumero, i, EColTab.NOSSO_NUMERO.ordinal() );

			setregistro++;

		}
	}

	enum DadosCliente {
		RAZCLI, ENDCLI, NUMCLI, BAIRCLI, CEPCLI, CIDCLI, UFCLI, CNPJ, CPF, CNPJCPF;
	}
}
