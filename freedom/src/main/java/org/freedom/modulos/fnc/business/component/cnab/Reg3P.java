package org.freedom.modulos.fnc.business.component.cnab;

import java.math.BigDecimal;
import java.util.Date;

import org.freedom.library.business.exceptions.ExceptionCnab;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.ETipo;

public class Reg3P extends Reg3 {

	private String agencia;

	private String digAgencia;

	private String conta;

	private String digConta;

	private String digAgConta;

	private String identTitulo;

	private int codCarteira;

	private int formaCadTitulo;

	private int tipoDoc;

	private int identEmitBol;

	private int identDist;

	private String docCobranca;

	private Date dtVencTitulo;

	private BigDecimal vlrTitulo;

	private String agenciaCob;

	private String digAgenciaCob;

	private int especieTit;

	private char aceite;

	private Date dtEmitTit;

	private int codJuros;

	private Date dtJuros;

	private BigDecimal vlrJurosTaxa;
	
	private BigDecimal vlrPercMulta;
	
	//private BigDecimal vlrJuros;

	private int codDesc;

	private Date dtDesc;

	private BigDecimal vlrpercConced;

	private BigDecimal vlrIOF;

	private BigDecimal vlrAbatimento;

	private String identTitEmp;

	private int codProtesto;

	private int diasProtesto;

	private int codBaixaDev;

	private int diasBaixaDevol;

	private int codMoeda;

	private String contrOperCred;

	public Reg3P() {

		super( 'P' );
	}

	public Reg3P( final String line ) throws ExceptionCnab {

		this();
		parseLine( line );
	}

	public char getAceite() {

		return aceite;
	}

	/**
	 * A - Aceite.<br>
	 * N - Não aceite.<br>
	 */
	public void setAceite( final char aceite ) {

		this.aceite = aceite;
	}

	public String getAgencia() {

		return agencia;
	}

	public void setAgencia( final String agencia ) {

		this.agencia = agencia;
	}

	public String getAgenciaCob() {

		return agenciaCob;
	}

	public void setAgenciaCob( final String agenciaCob ) {

		this.agenciaCob = agenciaCob;
	}

	public int getCodBaixaDev() {

		return codBaixaDev;
	}

	/**
	 * Código para baixa/devolução.<br>
	 * 1 - Baixar/Devolver.<br>
	 * 2 - Não baixar/ Não devolver.<br>
	 */
	public void setCodBaixaDev( final int codBaixaDev ) {

		this.codBaixaDev = codBaixaDev;
	}

	public int getCodCarteira() {

		return codCarteira;
	}

	/**
	 * Carteira.<br>
	 * 1 - Cobrança simples.<br>
	 * 2 - Cobrança vinculada.<br>
	 * 3 - Cobrança caucionada.<br>
	 * 4 - Cobrança descontada.<br>
	 * 7 - Cobrança direta especial / carteira 17.
	 */
	public void setCodCarteira( final int codCarteira ) {

		this.codCarteira = codCarteira;
	}

	public int getCodDesc() {

		return codDesc;
	}

	/**
	 * Código do desconto.<br>
	 * 1 - Valor fixo até a data informada.<br>
	 * 2 - Percentual até a data informada.<br>
	 * 3 - Valor por antecipação por dia corrido.<br>
	 * 4 - Valor por antecipação por dia util.<br>
	 * 5 - Percentual sobre o valor nominal dia corrido.<br>
	 * 6 - Percentual sobre o valor nominal dia util.<br>
	 * Obs.: Para as opções 1 e 2 será obrigatório a informação da data.<br>
	 */
	public void setCodDesc( final int codDesc ) {

		this.codDesc = codDesc;
	}

	public int getCodJuros() {

		return codJuros;
	}

	/**
	 * Código do juros de mora.<br>
	 * 1 - Valor por dia.<br>
	 * 2 - Taxa mensal.<br>
	 * 3 - Isento.<br>
	 */
	public void setCodJuros( final int codJuros ) {

		this.codJuros = codJuros;
	}

	public int getCodMoeda() {

		return codMoeda;
	}

	/**
	 * Código da moeda.<br>
	 * 01 - Reservado para uso futuro.<br>
	 * 02 - Dolar americano comercial/venda.<br>
	 * 03 - Dolar americano turismo/venda.<br>
	 * 04 - ITRD.<br>
	 * 05 - IDTR.<br>
	 * 06 - UFIR diária.<br>
	 * 07 - UFIR mensal.<br>
	 * 08 - FAJ-TR.<br>
	 * 09 - Real.<br>
	 */
	public void setCodMoeda( final int codMoeda ) {

		this.codMoeda = codMoeda;
	}

	public int getCodProtesto() {

		return codProtesto;
	}

	/**
	 * Código para protesto.<br>
	 * 1 - Dias corridos.<br>
	 * 2 - Dias utéis.<br>
	 * 3 - Não protestar.<br>
	 */
	public void setCodProtesto( final int codProtesto ) {

		this.codProtesto = codProtesto;
	}

	public String getConta() {

		return conta;
	}

	public void setConta( final String conta ) {

		this.conta = conta;
	}

	public String getContrOperCred() {

		return contrOperCred;
	}

	public void setContrOperCred( final String contrOperCred ) {

		this.contrOperCred = contrOperCred;
	}

	public int getDiasBaixaDevol() {

		return diasBaixaDevol;
	}

	/**
	 * Número de dias para a Baixa / Devolução.<br>
	 */
	public void setDiasBaixaDevol( final int diasBaixaDevol ) {

		this.diasBaixaDevol = diasBaixaDevol;
	}

	public int getDiasProtesto() {

		return diasProtesto;
	}

	/**
	 * Número de dias para protesto.<br>
	 */
	public void setDiasProtesto( final int diasProtesto ) {

		this.diasProtesto = diasProtesto;
	}

	public String getDigAgConta() {

		return digAgConta;
	}

	public void setDigAgConta( final String digAgConta ) {

		this.digAgConta = digAgConta;
	}

	public String getDigAgencia() {

		return digAgencia;
	}

	public void setDigAgencia( final String digAgencia ) {

		this.digAgencia = digAgencia;
	}

	public String getDigAgenciaCob() {

		return digAgenciaCob;
	}

	public void setDigAgenciaCob( final String digAgenciaCob ) {

		this.digAgenciaCob = digAgenciaCob;
	}

	public String getDigConta() {

		return digConta;
	}

	public void setDigConta( final String digConta ) {

		this.digConta = digConta;
	}

	public String getDocCobranca() {

		return docCobranca;
	}

	/**
	 * Número utilizado pelo cliente para identificação do titulo.<br>
	 */
	public void setDocCobranca( final String docCobranca ) {

		this.docCobranca = docCobranca;
	}

	public Date getDtDesc() {

		return dtDesc;
	}

	public void setDtDesc( final Date dtDesc ) {

		this.dtDesc = dtDesc;
	}

	public Date getDtEmitTit() {

		return dtEmitTit;
	}

	public void setDtEmitTit( final Date dtEmitTit ) {

		this.dtEmitTit = dtEmitTit;
	}

	public Date getDtJuros() {

		return dtJuros;
	}

	/**
	 * Se inválida ou não informada, será assumida a data do vencimento.<br>
	 */
	public void setDtJuros( final Date dtJuros ) {

		this.dtJuros = dtJuros;
	}

	public Date getDtVencTitulo() {

		return dtVencTitulo;
	}

	/**
	 * Data de vencimento do titulo.<br>
	 * A vista - preencher com 11111111.<br>
	 * Contra-apresentação - preencher com 99999999.<br>
	 * Obs.: O prazo legal para vencimento "a vista" ou "contra apresentação"<br>
	 * é de 15 dias da data do registro no banco.<br>
	 */
	public void setDtVencTitulo( final Date dtVencTitulo ) {

		this.dtVencTitulo = dtVencTitulo;
	}

	public int getEspecieTit() {

		return especieTit;
	}

	/**
	 * Especie do titulo CB 240.<br>
	 * 01 - CH Cheque.<br>
	 * 02 - DM Duplicata mercantíl.<br>
	 * 03 - DMI Duplicata mercantíl p/ indicação.<br>
	 * 04 - DS Duplicata de serviço.<br>
	 * 05 - DSI DUplicata de serviçõ p/ indicação.<br>
	 * 06 - DR Duplicata rural.<br>
	 * 07 - LC Letra de cambio.<br>
	 * 08 - NCC Nota de crédito comercial.<br>
	 * 09 - NCE Nota de crédito a exportação.<br>
	 * 10 - NCI Nota de crédito indústria.<br>
	 * 11 - NCR Nota de crédito rural.<br>
	 * 12 - NP Nota promissória.<br>
	 * 13 - NPR Nota promissória rural.<br>
	 * 14 - TM Triplicata mercantíl.<br>
	 * 15 - TS Triplicata de serviço.<br>
	 * 16 - NS Nota de seguro.<br>
	 * 17 - RC Recibo.<br>
	 * 18 - FAT Fatura.<br>
	 * 19 - ND Nota de débito.<br>
	 * 20 - AP Apolice de seguro.<br>
	 * 21 - ME Mensalidade escolar.<br>
	 * 22 - PC Parcela de consórcio.<br>
	 * 99 - Outros.<br>
	 */
	public void setEspecieTit( final int especieTit ) {

		this.especieTit = especieTit;
	}

	public int getFormaCadTitulo() {

		return formaCadTitulo;
	}

	/**
	 * Forma de cadastramento do titulo.<br>
	 * 1 - Com cadastro.<br>
	 * 2 - Sem cadastro.<br>
	 */
	public void setFormaCadTitulo( final int formaCadTitulo ) {

		this.formaCadTitulo = formaCadTitulo;
	}

	public int getIdentDist() {

		return identDist;
	}

	/**
	 * Identificação da distribuição.<br>
	 * 1 - Banco.<br>
	 * 2 - Cliente.<br>
	 */
	public void setIdentDist( final int identDist ) {

		this.identDist = identDist;
	}

	public int getIdentEmitBol() {

		return identEmitBol;
	}

	/**
	 * Identificação da emissão de bloqueto.<br>
	 * 1 - Banco emite.<br>
	 * 2 - Cliente emite.<br>
	 * 3 - Banco pré-emite e o cliente completa.<br>
	 * 4 - Banco reemite.<br>
	 * 5 - Banco não reemite.<br>
	 * 6 - Cobrança sem papel.<br>
	 * Obs.: Os campos 4 e 5 só serão aceitos para código de movimento para remessa 31.
	 */
	public void setIdentEmitBol( final int identEmitBol ) {

		this.identEmitBol = identEmitBol;
	}

	public String getIdentTitEmp() {

		return identTitEmp;
	}

	public void setIdentTitEmp( final String identTitEmp ) {

		this.identTitEmp = identTitEmp;
	}

	public String getIdentTitulo() {

		return identTitulo;
	}

	/**
	 * Nosso número.<br>
	 */
	public void setIdentTitulo( final String identTitulo ) {

		this.identTitulo = identTitulo;
	}

	public int getTipoDoc() {

		return tipoDoc;
	}

	/**
	 * Tipo de documento.<br>
	 * 1 - Tradicional.<br>
	 * 2 - Escrutiral.<br>
	 */
	public void setTipoDoc( final int tipoDoc ) {

		this.tipoDoc = tipoDoc;
	}

	public BigDecimal getVlrAbatimento() {

		return vlrAbatimento;
	}

	public void setVlrAbatimento( final BigDecimal vlrAbatimento ) {

		this.vlrAbatimento = vlrAbatimento;
	}

	public BigDecimal getVlrIOF() {

		return vlrIOF;
	}

	public void setVlrIOF( final BigDecimal vlrIOF ) {

		this.vlrIOF = vlrIOF;
	}

	public BigDecimal getVlrJurosTaxa() {

		return vlrJurosTaxa;
	}

	public void setVlrJurosTaxa( final BigDecimal vlrJurosTaxa ) {

		this.vlrJurosTaxa = vlrJurosTaxa;
	}

/*		public BigDecimal getVlrJuros() {

		return vlrJuros;
	}

	public void setVlrJuros( final BigDecimal vlrJuros ) {

		this.vlrJuros = vlrJuros;
	} */
	
	

	public BigDecimal getVlrpercConced() {

		return vlrpercConced;
	}

	public void setVlrpercConced( final BigDecimal vlrpercConced ) {

		this.vlrpercConced = vlrpercConced;
	}

	public BigDecimal getVlrTitulo() {

		return vlrTitulo;
	}

	public void setVlrTitulo( final BigDecimal vlrTitulo ) {

		this.vlrTitulo = vlrTitulo;
	}

	
	public BigDecimal getVlrPercMulta() {
	
		return vlrPercMulta;
	}

	
	public void setVlrPercMulta( BigDecimal vlrPercMulta ) {
	
		this.vlrPercMulta = vlrPercMulta;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.freedom.modulos.fnc.CnabUtil.Reg3#getLine()
	 */
	@ Override
	public String getLine( String padraocnab ) throws ExceptionCnab {

		StringBuilder line = new StringBuilder();

		try {
			line.append( super.getLineReg3( padraocnab ) );
			line.append( format( getAgencia(), ETipo.$9, 5, 0 ) );
			line.append( format( getDigAgencia(), ETipo.X, 1, 0 ) );
			line.append( format( getConta(), ETipo.$9, 12, 0 ) );
			line.append( format( getDigConta(), ETipo.X, 1, 0 ) );
			line.append( format( getDigAgConta(), ETipo.X, 1, 0 ) );
			line.append( format( getIdentTitulo(), ETipo.X, 20, 0 ) );
			line.append( format( getCodCarteira(), ETipo.$9, 1, 0 ) );
			line.append( format( getFormaCadTitulo(), ETipo.$9, 1, 0 ) );
			line.append( format( getTipoDoc(), ETipo.$9, 1, 0 ) );
			line.append( format( getIdentEmitBol(), ETipo.$9, 1, 0 ) );
			line.append( format( getIdentDist(), ETipo.$9, 1, 0 ) );
			line.append( format( getDocCobranca(), ETipo.X, 15, 0 ) );
			line.append( CnabUtil.dateToString( getDtVencTitulo(), null ) );
			line.append( format( getVlrTitulo(), ETipo.$9, 15, 2 ) );
			line.append( format( getAgenciaCob(), ETipo.$9, 5, 0 ) );
			line.append( format( getDigAgenciaCob(), ETipo.$9, 1, 0 ) );
			line.append( format( getEspecieTit(), ETipo.$9, 2, 0 ) );
			line.append( format( getAceite(), ETipo.X, 1, 0 ) );
			line.append( CnabUtil.dateToString( getDtEmitTit(), null ) );
			line.append( format( getCodJuros(), ETipo.$9, 1, 0 ) );
			line.append( CnabUtil.dateToString( getDtJuros(), null ) );
			line.append( format( getVlrJurosTaxa(), ETipo.$9, 15, 2 ) );
			line.append( format( getCodDesc(), ETipo.$9, 1, 0 ) );
			line.append( CnabUtil.dateToString( getDtDesc(), null ) );
			line.append( format( getVlrpercConced(), ETipo.$9, 15, 2 ) );
			line.append( format( getVlrIOF(), ETipo.$9, 15, 2 ) );
			line.append( format( getVlrAbatimento(), ETipo.$9, 15, 2 ) );
			line.append( format( getIdentTitEmp(), ETipo.X, 25, 0 ) );
			line.append( format( getCodProtesto(), ETipo.$9, 1, 0 ) );
			line.append( format( getDiasProtesto(), ETipo.$9, 2, 0 ) );
			line.append( format( getCodBaixaDev(), ETipo.$9, 1, 0 ) );
			line.append( format( getDiasBaixaDevol(), ETipo.$9, 3, 0 ) );
			line.append( format( getCodMoeda(), ETipo.$9, 2, 0 ) );
			line.append( format( getContrOperCred(), ETipo.$9, 10, 0 ) );
			line.append( " " );
			line.append( (char) 13 );
			line.append( (char) 10 );
		} catch ( Exception e ) {
			throw new ExceptionCnab( "CNAB registro 3 segmento P.\nErro ao escrever registro.\n" + e.getMessage() );
		}

		return line.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.freedom.modulos.fnc.CnabUtil.Reg3#parseLine(java.lang.String)
	 */
	@ Override
	public void parseLine( String line ) throws ExceptionCnab {

		try {

			if ( line == null ) {
				throw new ExceptionCnab( "Linha nula." );
			}
			else {

				super.parseLineReg3( line );
				setAgencia( line.substring( 17, 22 ) );
				setDigAgencia( line.substring( 22, 23 ) );
				setConta( line.substring( 23, 35 ) );
				setDigConta( line.substring( 35, 36 ) );
				setDigAgConta( line.substring( 36, 37 ) );
				setIdentTitulo( line.substring( 37, 57 ) );
				setCodCarteira( line.substring( 57, 58 ).trim().length() > 0 ? Integer.parseInt( line.substring( 57, 58 ).trim() ) : 0 );
				setFormaCadTitulo( line.substring( 58, 59 ).trim().length() > 0 ? Integer.parseInt( line.substring( 58, 59 ).trim() ) : 0 );
				setTipoDoc( line.substring( 59, 60 ).trim().length() > 0 ? Integer.parseInt( line.substring( 59, 60 ).trim() ) : 0 );
				setIdentEmitBol( line.substring( 60, 61 ).trim().length() > 0 ? Integer.parseInt( line.substring( 60, 61 ).trim() ) : 0 );
				setIdentDist( line.substring( 61, 62 ).trim().length() > 0 ? Integer.parseInt( line.substring( 61, 62 ).trim() ) : 0 );
				setDocCobranca( line.substring( 62, 77 ) );
				setDtVencTitulo( CnabUtil.stringDDMMAAAAToDate( line.substring( 77, 85 ).trim() ) );
				setVlrTitulo( CnabUtil.strToBigDecimal( line.substring( 85, 100 ) ) );
				setAgenciaCob( line.substring( 100, 105 ) );
				setDigAgenciaCob( line.substring( 105, 106 ).trim() );
				setEspecieTit( line.substring( 106, 108 ).trim().length() > 0 ? Integer.parseInt( line.substring( 106, 108 ).trim() ) : 0 );
				setAceite( line.substring( 108, 109 ).charAt( 0 ) );
				setDtEmitTit( CnabUtil.stringDDMMAAAAToDate( line.substring( 109, 117 ).trim() ) );
				setCodJuros( line.substring( 117, 118 ).trim().length() > 0 ? Integer.parseInt( line.substring( 117, 118 ).trim() ) : 0 );
				setDtJuros( CnabUtil.stringDDMMAAAAToDate( line.substring( 118, 126 ).trim() ) );
				setVlrJurosTaxa( CnabUtil.strToBigDecimal( line.substring( 126, 141 ) ) );
				setCodDesc( line.substring( 141, 142 ).trim().length() > 0 ? Integer.parseInt( line.substring( 141, 142 ).trim() ) : 0 );
				setDtDesc( CnabUtil.stringDDMMAAAAToDate( line.substring( 142, 150 ).trim() ) );
				setVlrpercConced( CnabUtil.strToBigDecimal( line.substring( 150, 165 ) ) );
				setVlrIOF( CnabUtil.strToBigDecimal( line.substring( 165, 180 ) ) );
				setVlrAbatimento( CnabUtil.strToBigDecimal( line.substring( 180, 195 ) ) );
				setIdentTitEmp( line.substring( 195, 220 ) );
				setCodProtesto( line.substring( 220, 221 ).trim().length() > 0 ? Integer.parseInt( line.substring( 220, 221 ).trim() ) : 0 );
				setDiasProtesto( line.substring( 221, 223 ).trim().length() > 0 ? Integer.parseInt( line.substring( 221, 223 ).trim() ) : 0 );
				setCodBaixaDev( line.substring( 223, 224 ).trim().length() > 0 ? Integer.parseInt( line.substring( 223, 224 ).trim() ) : 0 );
				setDiasBaixaDevol( line.substring( 224, 227 ).trim().length() > 0 ? Integer.parseInt( line.substring( 224, 227 ).trim() ) : 0 );
				setCodMoeda( line.substring( 227, 229 ).trim().length() > 0 ? Integer.parseInt( line.substring( 227, 229 ).trim() ) : 0 );
				setContrOperCred( line.substring( 229, 239 ) );
			}
		} catch ( Exception e ) {
			throw new ExceptionCnab( "CNAB registro 3 segmento P.\nErro ao ler registro.\n" + e.getMessage() );
		}
	}
}