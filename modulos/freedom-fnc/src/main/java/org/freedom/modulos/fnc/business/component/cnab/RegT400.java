package org.freedom.modulos.fnc.business.component.cnab;

import java.math.BigDecimal;
import java.util.Date;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.business.component.Banco;
import org.freedom.library.business.exceptions.ExceptionCnab;
import org.freedom.library.functions.Funcoes;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.ETipo;


public class RegT400 extends Reg {

	private String codBanco;

	private int loteServico;

	private int registroHeader;

	private String tipoOperacao;

	private String tipoServico;

	private String formaLancamento;

	private String versaoLayout;

	private int tipoInscEmp;

	private String cpfCnpjEmp;

	private String codConvBanco;

	private String agencia;
	
	private char aceite;

	private String digAgencia;

	private String posto;
	
	private String conta;

	private String digConta;

	private String digAgConta;

	private String razEmp;

	private String msg1;

	private String msg2;

	private int nrRemRet;

	private Date dataRemRet;

	private Date dataCred;
	
	private Date dataLiquidacao;

	private Integer codCarteira;
	
	private String codCarteiraCnab;

	private String identTitulo;
	
	private String variacaocarteira;

	private String identTitEmp;

	private BigDecimal vlrPercMulta;

	private String digNossoNumero;

	private int codMovimento;

	private String docCobranca;

	private Date dtVencTitulo;

	private BigDecimal vlrTitulo;

	private int especieTit;

	private Date dtEmitTit;

	private int codJuros;

	private BigDecimal vlrJurosTaxa;

	private Date dtDesc;

	private BigDecimal vlrDesc;

	private BigDecimal vlrIOF;

	private BigDecimal vlrAbatimento;

	private int tipoInscCli;

	private String cpfCnpjCli;

	private String razCli;

	private String endCli;

	private String bairCli;

	private String cepCli;

	private int seqregistro;

	private String codRejeicoes;

	private BigDecimal vlrJurosMulta;

	private BigDecimal vrlAbatCancel;

	private BigDecimal vlrPago;

	private BigDecimal vlrLiqCred;

	private BigDecimal vlrOutrasDesp;

	private BigDecimal vlrOutrosCred;

	private BigDecimal vlrOcorrSac;
	
	private Integer instrucoes;
	
	private Integer OutrasInstrucoes;

	private int identEmitBol = 2;
	
	private int codProtesto = 0;
	
	private int diasProtesto = 0;
	
	private String tpCobranca = "A";
	
	private boolean descpont = false;
	
	public int getCodProtesto() {

		return codProtesto;
	}

	public BigDecimal calcVlrJuros(int codjur, BigDecimal vlrapagar, BigDecimal perc) {
		/**
		 * Código do juros de mora.<br>
		 * 1 - Valor por dia.<br>
		 * 2 - Taxa mensal.<br>
		 * 3 - Isento.<br>
		 */
		BigDecimal retorno = new BigDecimal(0);
		if (vlrapagar!=null && perc!=null && 
				perc.doubleValue()>0 && vlrapagar.doubleValue()>0 && 
				(codjur==1 || codjur==2) ) {
			if (codjur==1) {
			    retorno = vlrapagar.multiply( perc ).divide( new BigDecimal(100) );
			} else if (codjur==2) {
			    retorno = vlrapagar.multiply( perc ).divide( new BigDecimal(100) ) ;
			    retorno = retorno.divide( new BigDecimal(30), BigDecimal.ROUND_HALF_UP );
			}
		}
		return retorno;
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

	public int getDiasProtesto() {

		return diasProtesto;
	}

	/**
	 * Número de dias para protesto.<br>
	 */
	public void setDiasProtesto( final int diasProtesto ) {

		this.diasProtesto = diasProtesto;
	}
	
	public String getCpfCnpjCli() {

		return cpfCnpjCli;
	}

	public String getIdentTitEmp() {

		return identTitEmp;
	}

	public void setIdentTitEmp( String identTitEmp ) {

		this.identTitEmp = identTitEmp;
	}

	public BigDecimal getVlrJurosMulta() {

		return vlrJurosMulta;
	}

	public void setVlrJurosMulta( BigDecimal vlrJurosMulta ) {

		this.vlrJurosMulta = vlrJurosMulta;
	}

	public BigDecimal getVrlAbatCancel() {

		return vrlAbatCancel;
	}

	public void setVrlAbatCancel( BigDecimal vrlAbatCancel ) {

		this.vrlAbatCancel = vrlAbatCancel;
	}

	public BigDecimal getVlrPago() {

		return vlrPago;
	}

	public void setVlrPago( BigDecimal vlrPago ) {

		this.vlrPago = vlrPago;
	}

	public BigDecimal getVlrLiqCred() {

		return vlrLiqCred;
	}

	public void setVlrLiqCred( BigDecimal vlrLiqCred ) {

		this.vlrLiqCred = vlrLiqCred;
	}

	public BigDecimal getVlrOutrasDesp() {

		return vlrOutrasDesp;
	}

	public void setVlrOutrasDesp( BigDecimal vlrOutrasDesp ) {

		this.vlrOutrasDesp = vlrOutrasDesp;
	}

	public BigDecimal getVlrOutrosCred() {

		return vlrOutrosCred;
	}

	public void setVlrOutrosCred( BigDecimal vlrOutrosCred ) {

		this.vlrOutrosCred = vlrOutrosCred;
	}

	public BigDecimal getVlrOcorrSac() {

		return vlrOcorrSac;
	}

	public void setVlrOcorrSac( BigDecimal vlrOcorrSac ) {

		this.vlrOcorrSac = vlrOcorrSac;
	}

	public String getCodRejeicoes() {

		return codRejeicoes;
	}

	public void setCodRejeicoes( String codRejeicoes ) {

		this.codRejeicoes = codRejeicoes;
	}

	public void setCpfCnpjCli( String cpfCnpjCli ) {

		this.cpfCnpjCli = cpfCnpjCli;
	}

	public int getSeqregistro() {

		return seqregistro;
	}

	public void setSeqregistro( int seqregistro ) {

		this.seqregistro = seqregistro;
	}

	public String getRazCli() {

		return razCli;
	}

	public void setRazCli( String razCli ) {

		this.razCli = razCli;
	}

	public String getEndCli() {

		return endCli;
	}

	public void setEndCli( String endCli ) {

		this.endCli = endCli;
	}

	public String getBairCli() {

		return bairCli;
	}

	public void setBairCli( String bairCli ) {

		this.bairCli = bairCli;
	}

	public String getCepCli() {

		return cepCli;
	}

	public void setCepCli( String cepCli ) {

		this.cepCli = cepCli;
	}

	public String getCidCli() {

		return cidCli;
	}

	public void setCidCli( String cidCli ) {

		this.cidCli = cidCli;
	}

	public String getUfCli() {

		return ufCli;
	}

	public void setUfCli( String ufCli ) {

		this.ufCli = ufCli;
	}

	public int getTipoInscAva() {

		return tipoInscAva;
	}

	public void setTipoInscAva( int tipoInscAva ) {

		this.tipoInscAva = tipoInscAva;
	}

	
	public Date getDataLiquidacao() {
	
		return dataLiquidacao;
	}

	
	public void setDataLiquidacao( Date dataLiquidacao ) {
	
		this.dataLiquidacao = dataLiquidacao;
	}

	public String getCpfCnpjAva() {

		return cpfCnpjAva;
	}

	public void setCpfCnpjAva( String cpfCnpjAva ) {

		this.cpfCnpjAva = cpfCnpjAva;
	}

	public String getRazAva() {

		return razAva;
	}

	public void setRazAva( String razAva ) {

		this.razAva = razAva;
	}

	private String cidCli;

	private String ufCli;

	private int tipoInscAva;

	private String cpfCnpjAva;

	private String razAva;

	public BigDecimal getVlrJurosTaxa() {

		return vlrJurosTaxa;
	}

	public int getTipoInscCli() {

		return tipoInscCli;
	}

	public void setTipoInscCli( int tipoInscCli ) {

		this.tipoInscCli = tipoInscCli;
	}

	public void setVlrJurosTaxa( BigDecimal vlrJurosTaxa ) {

		this.vlrJurosTaxa = vlrJurosTaxa;
	}

	public BigDecimal getVlrAbatimento() {

		return vlrAbatimento;
	}

	public void setVlrAbatimento( BigDecimal vlrAbatimento ) {

		this.vlrAbatimento = vlrAbatimento;
	}

	public BigDecimal getVlrIOF() {

		return vlrIOF;
	}

	public void setVlrIOF( BigDecimal vlrIOF ) {

		this.vlrIOF = vlrIOF;
	}

	public Date getDtDesc() {

		return dtDesc;
	}

	public BigDecimal getVlrDesc() {

		return vlrDesc;
	}

	public void setVlrDesc( BigDecimal vlrDesc ) {

		this.vlrDesc = vlrDesc;
	}

	public void setDtDesc( Date dtDesc ) {

		this.dtDesc = dtDesc;
	}

	public int getCodMovimento() {

		return codMovimento;
	}

	public int getCodJuros() {

		return codJuros;
	}

	public void setCodJuros( int codJuros ) {

		this.codJuros = codJuros;
	}

	public BigDecimal getVlrTitulo() {

		return vlrTitulo;
	}

	public int getEspecieTit() {

		return especieTit;
	}

	public Date getDtEmitTit() {

		return dtEmitTit;
	}

	public void setDtEmitTit( Date dtEmitTit ) {

		this.dtEmitTit = dtEmitTit;
	}

	public void setEspecieTit( int especieTit ) {

		this.especieTit = especieTit;
	}

	public void setVlrTitulo( BigDecimal vlrTitulo ) {

		this.vlrTitulo = vlrTitulo;
	}

	public void setCodMovimento( int codMovimento ) {

		this.codMovimento = codMovimento;
	}

	public Date getDtVencTitulo() {

		return dtVencTitulo;
	}

	public void setDtVencTitulo( Date dtVencTitulo ) {

		this.dtVencTitulo = dtVencTitulo;
	}

	public String getDocCobranca() {

		return docCobranca;
	}

	public void setDocCobranca( String docCobranca ) {

		this.docCobranca = docCobranca;
	}

	public String getDigNossoNumero() {

		return digNossoNumero;
	}

	public void setDigNossoNumero( String digNossoNumero ) {

		this.digNossoNumero = digNossoNumero;
	}

	public RegT400() {

		setRegistroHeader( 1 );
		setTipoServico( "01" );
		setVersaoLayout( "020" );
	}

	public RegT400( final String line ) throws ExceptionCnab {

		this();
		parseLine( line );
	}
	
	public String getAgencia() {

		return agencia;
	}

	public void setAgencia( final String agencia ) {

		this.agencia = agencia;
	}

	
	
	public char getAceite() {
	
		return aceite;
	}

	
	public void setAceite( char aceite ) {
	
		this.aceite = aceite;
	}

	public String getPosto() {
	
		return posto;
	}

	
	public void setPosto( String posto ) {
	
		this.posto = posto;
	}

	public String getCpfCnpjEmp() {

		return cpfCnpjEmp;
	}

	/**
	 * Inscrição da empresa. Conforme o tipo da inscrição.<br>
	 * 
	 * @see org.freedom.modulos.fnc.business.component.cnab.CnabUtil.Reg1#setTipoInscEmp(int tipoInscEmp )
	 */
	public void setCpfCnpjEmp( final String cnpjEmp ) {

		this.cpfCnpjEmp = cnpjEmp;
	}

	public String getCodBanco() {

		return codBanco;
	}

	public void setCodBanco( final String codBanco ) {

		this.codBanco = codBanco;
	}

	public String getCodConvBanco() {

		return codConvBanco;
	}

	/**
	 * Indentifica a empresa no banco para determinados tipos de serviços.<br>
	 * Observar as regras de preenchimento abaixo no que se refere ao headre de serviço/lote:<br>
	 * "9999999994444CCVVV " / 20 bytes / , onde:<br>
	 * 999999999 - Código do convênio.<br>
	 * 4444 - Código do produto.<br>
	 * CC - Carteira de cobrança.<br>
	 * VVV - Variação da carteira de cobrança.<br>
	 */
	public void setCodConvBanco( final String codConvBanco ) {

		if (codConvBanco == null) {
		    this.codConvBanco = "";
		} else {
			this.codConvBanco = codConvBanco;
		}
	}

	public String getConta() {

		return conta;
	}

	public void setConta( final String conta ) {

		this.conta = conta;
	}

	public Date getDataCred() {

		return dataCred;
	}

	public void setDataCred( final Date dataCred ) {

		this.dataCred = dataCred;
	}

	public Date getDataRemRet() {

		return dataRemRet;
	}

	public void setDataRemRet( final Date dataRemRet ) {

		this.dataRemRet = dataRemRet;
	}

	public String getDigAgConta() {

		return digAgConta;
	}

	public void setDigAgConta( final String digAgeConta ) {

		this.digAgConta = digAgeConta;
	}

	public String getDigAgencia() {

		return digAgencia;
	}

	public void setDigAgencia( final String digAgencia ) {

		this.digAgencia = digAgencia;
	}

	public String getDigConta() {

		return digConta;
	}

	public void setDigConta( final String digConta ) {

		this.digConta = digConta;
	}

	public String getFormaLancamento() {

		return formaLancamento;
	}

	/**
	 * Este campo não será utilizado para combrança.<br>
	 */
	public void setFormaLancamento( final String formaLancamento ) {

		this.formaLancamento = formaLancamento;
	}

	public int getLoteServico() {

		return loteServico;
	}

	/**
	 * Indentifica um Lote de Serviço.<br>
	 * Sequencial e nmão deve ser repetido dentro do arquivo.<br>
	 * As numerações 0000 e 9999 <br>
	 * são exclusivas para o Header e para o Trailer do arquivo respectivamente.<br>
	 */
	public void setLoteServico( final int loteServico ) {

		this.loteServico = loteServico;
	}

	public String getMsg1() {

		return msg1;
	}

	/**
	 * As menssagens serão impressas em todos os bloquetos referentes 1 e 2 ao mesmo lote.<br>
	 * Estes campos não serão utilizados no arquivo de retorno.<br>
	 */
	public void setMsg1( final String msg1 ) {

		this.msg1 = msg1;
	}

	/**
	 * As menssagens serão impressas em todos os bloquetos referentes 1 e 2 ao mesmo lote.<br>
	 * Estes campos não serão utilizados no arquivo de retorno.<br>
	 */
	public String getMsg2() {

		return msg2;
	}

	public void setMsg2( final String msg2 ) {

		this.msg2 = msg2;
	}

	public String getRazEmp() {

		return razEmp;
	}

	public void setRazEmp( final String nomeEmp ) {

		this.razEmp = nomeEmp;
	}

	public int getNrRemRet() {

		return nrRemRet;
	}

	public void setNrRemRet( final int nrRemRet ) {

		this.nrRemRet = nrRemRet;
	}

	public int getRegistroHeader() {

		return registroHeader;
	}

	/**
	 * Indica o tipo de registro.<br>
	 */
	private void setRegistroHeader( final int registroHeader ) {

		this.registroHeader = registroHeader;
	}

	public int getTipoInscEmp() {

		return tipoInscEmp;
	}

	/**
	 * Indica o tipo de inscrição da empresa.<br>
	 * 1 - CPF.<br>
	 * 2 - CNPJ.<br>
	 */
	public void setTipoInscEmp( final int tipoInscEmp ) {

		this.tipoInscEmp = tipoInscEmp;
	}

	public String getTipoOperacao() {

		return tipoOperacao;
	}

	/**
	 * Indica a operação que devera ser realizada com os registros Detalhe do Lote.<br>
	 * Deve constar apenas um tipo por Lote:<br>
	 * C - Lançamento a Crédito.<br>
	 * D - Lançamento a Débito.<br>
	 * E - Extrato para conciliação.<br>
	 * I - Informações de titulos capturados do próprio banco.<br>
	 * R - Arquivo de remessa.<br>
	 * T - Arquivo de retorno.<br>
	 */
	public void setTipoOperacao( final String tipoOperacao ) {

		this.tipoOperacao = tipoOperacao;
	}

	public String getTipoServico() {

		return tipoServico;
	}

	public Integer getCodCarteira() {

		return codCarteira;
	}
	
	public String getVariacaoCarteira() {

		return variacaocarteira;
	}
	
	public void setVariacaoCarteira(String var) {

		variacaocarteira = var;
	}

	public void setCodCarteira( final Integer codCarteira ) {

		this.codCarteira = codCarteira;
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

	/**
	 * Indica o tipo de serviço que o lote contém.<br>
	 * 01 - Cobrança.<br>
	 * 02 - Cobrança em papel.<br>
	 * 03 - Bloqueto eletronico.<br>
	 * 04 - Conciliação bancária.<br>
	 * 05 - Débitos.<br>
	 * 10 - Pagamento dividendos.<br>
	 * 20 - Pagamento fornecedor.<br>
	 * 30 - Pagamento salários.<br>
	 * 50 - Pagamento sinistro segurados.<br>
	 * 60 - Pagamento despesa viajante em trânsito.<br>
	 */
	private void setTipoServico( final String tipoServico ) {

		this.tipoServico = tipoServico;
	}

	public String getVersaoLayout() {

		return versaoLayout;
	}

	public BigDecimal getVlrPercMulta() {

		return vlrPercMulta;
	}

	public void setVlrPercMulta( BigDecimal vlrPercMulta ) {

		this.vlrPercMulta = vlrPercMulta;
	}

	/**
	 * Indica o número da versão do layout do lote, composto de:<br>
	 * versão : 2 digitos.<br>
	 * release: 1 digito.<br>
	 */
	private void setVersaoLayout( final String versaoLayout ) {

		this.versaoLayout = versaoLayout;
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
	
	public String getCodCarteiraCnab() {
		return codCarteiraCnab;
	}

	
	public void setCodCarteiraCnab( String codCarteiraCnab ) {
		this.codCarteiraCnab = codCarteiraCnab;
	}

	
	public Integer getInstrucoes() {
	
		return instrucoes;
	}

	
	public void setInstrucoes( Integer instrucoes ) {
	
		this.instrucoes = instrucoes;
	}

	
	
	public boolean isDescpont() {
	
		return descpont;
	}

	
	public void setDescpont( boolean descpont ) {
	
		this.descpont = descpont;
	}

	public Integer getOutrasInstrucoes() {
	
		return OutrasInstrucoes;
	}

	
	public void setOutrasInstrucoes( Integer outrasInstrucoes ) {
	
		OutrasInstrucoes = outrasInstrucoes;
	}

	@ Override
	public String getLine( String padraocnab ) throws ExceptionCnab {

		StringBuilder line = new StringBuilder();

		try {
			if(getCodBanco().equals( Banco.ITAU )){
				return getLineItau( padraocnab );
			}
			else if(getCodBanco().equals( Banco.SICRED )) {
				return getLineSicredi( padraocnab );
			}
			
			if( getCodBanco().equals( Banco.BANCO_DO_BRASIL ) && (getCodConvBanco().length()>=7) ) {
				// Convênios menores que 1.000.000
				line.append( "7" ); // Tipo de registro 1	
				
			} else {
				line.append( "1" ); // Tipo de registro 1	
			}

			/*************************************************/
			/**                                             **/
			/** Implementação para o banco do Brasil CBR641 **/
			/**                                             **/
			/*************************************************/
			
			if(getCodBanco().equals( Banco.BANCO_DO_BRASIL )) {					
				line.append( format( getTipoInscEmp(), ETipo.$9, 2, 0 ) );// 002 a 003 - Tipo de inscrição da empresa
				line.append( format( getCpfCnpjEmp(), ETipo.$9, 14, 0 ) );//004 a 017 - Numero do CPF/CNPJ da Empresa
				line.append( format( getAgencia(), ETipo.$9, 4, 0 ) );//018 a 021 - Agencia
				line.append( format( getDigAgencia(), ETipo.X, 1, 0 ) );//022 a 022 - Digito da agencia
				line.append( format( getConta(), ETipo.$9, 8, 0 ) );//023 a 030 - Numero da conta corrente
				line.append( format( getDigConta(), ETipo.X, 1, 0 ) );//031 a 031 - Digito da conta corrente
				
				if (getCodConvBanco().length()<7) {
					line.append( format( getCodConvBanco(), ETipo.X, 6, 0 ) );//032 a 037 - Convenio
				} else {
					line.append( format( getCodConvBanco(), ETipo.X, 7, 0 ) );//032 a 038 - Convenio
				}
				line.append( format( getIdentTitEmp(), ETipo.X, 25, 0 ) ); // Convênio < 1.000.000 = Posição 38 a 62 - Nro de controle do participante (nosso numero)
																		    // Convênio >= 1.000.000 = Posição 39 a 63	
				if (getCodConvBanco().length()<7) {
					// Convênio < 1.000.000
					line.append( format( getIdentTitulo(), ETipo.X, 11, 0 ) ); // Convênio < 1.000.000 = Posição 063 a 073 - Nosso numero
					line.append( format( getDigNossoNumero(), ETipo.$9, 1, 0 ) ); // Posição 074 a 074 - Digito verificador do nosso numero
				} else {
					// Convênio >= 1.000.000
					line.append( format( getIdentTitulo(), ETipo.X, 17, 0 ) ); // Convênio >= 1.000.000 = Posição 064 a 080 - Nosso numero
				}
																			// Convênio >= 1.000.000 = Posição 064 a 074 - Nosso numero
				line.append( "00" ); // Conv. < 100.000.00 = Posição 075 a 076 / Conv >= 1.000.000 = Posição 081 a 082 - Numero da prestacao informar zeros
				line.append( "00" ); // Posição 077 a 078 / 083 a 084 - Grupo de valor informar zeros
				line.append( StringFunctions.replicate( " ", 3 ) );// Posição 079 a 081 / 085 a 087 - Preencher com brancos
				line.append( " " );// Posição 082 a 082 / 088 a 088 -Indicativo de mensagem Preencher com brancos
				line.append( StringFunctions.replicate( " ", 3 ) );// Posição 083 a 085 / 089 a 091 - Preencher com brancos
				line.append( format( getVariacaoCarteira(), ETipo.X, 3, 0 ) ); // Posição 086 a 088 / 092 a 094 - Variacao da carteira
				line.append( "0" ); // Posição 089 a 089 / 095 a 095 - Conta caução
				if (getCodConvBanco().length()<7) {
					line.append( "00000" ); // Posição 090 a 094 - Codigo de responsabilidade
					line.append( "0" ); // Posição 095 a 095 - Digito do codigo de responsabilidade
				} 
				line.append( "000000" ); // Posição 096 a 101 - Numero do borderô
				line.append( StringFunctions.replicate( " ", 5 ) );// Posição 102 a 106 - Tipo de cobrança Preencher com brancos
				line.append( StringFunctions.strZero( getCodCarteira() + "", 2 ) ); // Posição 107 a 108 - Código da Carteira de cobranca
				
			}
			
			/*************************************************/
			/**                                             **/
			/** Implementação para outros bancos (BRADESCO) **/
			/**                                             **/
			/*************************************************/
			
			else {
			
				line.append( StringFunctions.replicate( "0", 5 ) ); // Opcional - Agencia para debito em conta
				line.append( "0" ); // Opcional - Dígito da Agencia para debito em conta
				line.append( StringFunctions.replicate( "0", 5 ) ); // Opcional - Razão da conta para debito
				line.append( StringFunctions.replicate( "0", 7 ) ); // Opcional - Conta do sacado para debito
				line.append( "0" ); // Opcional - Dígito da conta para debito
				// Identificação da Empresa cedente no banco
				line.append( "0" ); // Posição 21 a 21 - Zero
				
				line.append( StringFunctions.strZero( getCodCarteira() + "", 3 ) ); // Posição 22 a 24 - Código da Carteira
				line.append( format( getAgencia(), ETipo.$9, 5, 0 ) ); // Posição 25 a 29 - Código da Agência Cedente
				line.append( format( getConta(), ETipo.$9, 7, 0 ) ); // Posição 30 a 36 - Conta Corrente
				line.append( format( getDigConta(), ETipo.X, 1, 0 ) ); // Posição 37 a 37 - Dígito da conta
				// fim da idendificação

				line.append( format( getIdentTitEmp(), ETipo.X, 25, 0 ) ); // Posição 38 a 62 - Nro de controle do participante (nosso numero)
				
				line.append(StringFunctions.replicate( "0", 3 ) ); // Posição 63 a 65 - Nro do banco para débito em conta
				//line.append( format( getCodBanco(), ETipo.$9, 3, 0 ) ); // Posição 63 a 65 - Nro do banco

				if ( getVlrPercMulta().floatValue() > 0 ) {
					line.append( "2" ); // Posição 66 a 66 - Se = 2 considerar multa se = 0 sem multa.
					line.append( format( getVlrPercMulta(), ETipo.$9, 4, 2 ) ); // Posição 67 a 70 - Percentual de multa
				}
				else {
					line.append( "0" ); // Posição 66 a 66 - Se = 2 considerar multa se = 0 sem multa.
					line.append( StringFunctions.replicate( "0", 4 ) ); // Posição 67 a 70 - Percentual de multa (preenchido com zeros)
				}

				line.append( format( getIdentTitulo(), ETipo.X, 11, 0 ) ); // Posição 71 a 81 - Identificação do título no banco (nosso numero)

				line.append( format( getDigNossoNumero(), ETipo.$9, 1, 0 ) ); // Posição 82 a 82 - Digito verificador do nosso numero

				// Implementar futuramente
				line.append( format( 0, ETipo.$9, 10, 2 ) ); // Posição 83 a 92 - Valor do Desconto bonif./dia

				line.append( format( getIdentEmitBol(), ETipo.$9, 1, 0 ) ); // Posição 93 a 93 - Condição para Emissão da papeleta de cobrança - 1-Banco emite e processa, 2- Cliente emite e o Banco Processa.

				line.append( "N" ); // Posição 94 a 94 - Ident. se emite boleto para deb. automaticao

				line.append( StringFunctions.replicate( " ", 10 ) ); // Posição 95 a 104 - Identificação da operacao do banco (em branco)

				line.append( " " ); // Posição 105 a 105 - Indicador de rateio crédito 'R' = sim / " "= não
				line.append( "2" ); // Posição 106 a 106 - Endereçamento para aviso do debito autom. 1 = emite aviso / 2 =não emite

				line.append( StringFunctions.replicate( " ", 2 ) ); // Posição 107 a 108 - Branco
				
			}
			
		

			line.append( format( getCodMovimento(), ETipo.$9, 2, 0 ) ); // Posição 109 a 110 - Identificação da ocorrência

			line.append( StringFunctions.strZero( getDocCobranca(), 10 ) ); // Posição 111 a 120 - Nro do documento

			line.append( CnabUtil.dateToString( getDtVencTitulo(), "DDMMAA" ) ); // Posição 121 a 126 - Data do vencimento do título

			line.append( format( getVlrTitulo(), ETipo.$9, 13, 2 ) ); // Posição 127 a 139 - Valor do título

			if(getCodBanco().equals( Banco.BANCO_DO_BRASIL )) {	
				line.append( Banco.BANCO_DO_BRASIL ); // Posição 140 a 142 - Banco encarregado da cobrança ( preencher com 001 )
				line.append( "0000" ); // Posição 143 a 146 - Agencia depositária ( preencher com zeros )
				line.append( " " ); // Posição 147 a 147 - Digito Verificador do Prefixo da Agencia. Preencher com branco.
			}
			else {
				line.append( "000" ); // Posição 140 a 142 - Banco encarregado da cobrança ( preencher com zeros )
				line.append( "00000" ); // Posição 143 a 147 - Agencia depositária ( preencher com zeros )
			}

			// line.append( format( getEspecieTit(), ETipo.$9, 2, 0 ) ); // Posição 148 a 149 - Espécie de Título

			/*
			 * 01-Dulicata; 02-Nota Promissoria; 03-Nota de seguro 04-Cobrança seriada 05-Recibo 10-Letras de câmbio 11-Nota de débito 12-Duplicata de serv. 99-Outros;
			 */
			line.append( format( getEspecieTit(), ETipo.$9, 2, 0 ) ); // Posição 148 a 149 - Espécie de Título (Implementada de forma fixa pois difere do código no padrão cnab 240
//			line.append( format( "01", ETipo.$9, 2, 0 ) ); // Posição 148 a 149 - Espécie de Título (Implementada de forma fixa pois difere do código no padrão cnab 240

			line.append( "N" ); // Posição 150 a 150 - Identificação (Sempre "N");

			line.append( CnabUtil.dateToString( getDtEmitTit(), "DDMMAA" ) ); // Posição 151 a 156 - Data de emissão do título

			/*

			if(getCodBanco().equals( Banco.BANCO_DO_BRASIL )) {
				
				line.append( format( getCodJuros(), ETipo.$9, 2, 0 ) ); // Posição 157 a 158 - 1° Instrução - Código para juros
				line.append( StringFunctions.strZero( getDiasProtesto() + "", 2 ) ); // Posição 159 a 160 - 2° Instrução - Numero de dias para protesto
				
			}
			else {
			*/
			line.append( format( getCodProtesto(), ETipo.$9, 2, 0 ) ); // Posição 157 a 158 - 1° Instrução - Código para juros
			line.append( StringFunctions.strZero( getDiasProtesto() + "", 2 ) ); // Posição 159 a 160 - 2° Instrução - Numero de dias para protesto
				
			//}

			if ( (getCodJuros()==1) || (getCodJuros()==2) ) { // Se Juros/Mora diária
				line.append( format( calcVlrJuros(getCodJuros(), getVlrTitulo(), getVlrJurosTaxa()), ETipo.$9, 13, 2 ) ); // Posição 161 a 173 - (se for do tipo mora diária) Mora por dia de atraso
			}
			else {
				line.append( StringFunctions.replicate( "0", 13 ) ); // Posição 161 a 173 - (Se não for do tipo mora diária) Mora por dia de atraso
			}

			if ( ( getCodBanco().equals( Banco.BANCO_DO_BRASIL ) ) || ( ! isDescpont() ) ) { // Revisar caso seja necessário implementar para Banco do Brasil
				line.append( "000000" ); // Posição 174 a 179 - Data limite para concessão de desconto (informar 000000)	
			}
			else{
				line.append( CnabUtil.dateToString( getDtDesc(), "DDMMAA" ) ); // Posição 174 a 179 - Data limete para concessão de desconto
			}

			line.append( format( getVlrDesc(), ETipo.$9, 13, 2 ) ); // Posição 180 a 192 - Valor de desconto

			line.append( StringFunctions.replicate( "0", 13 ) ); // Posição 193 a 205 - (Valor do IOF (Apenas para empresas seguradoras))

			line.append( format( getVlrAbatimento(), ETipo.$9, 13, 2 ) ); // Posição 206 a 218 - Valor do Abatimento a ser concedido ou cancelado (no caso de transação de abatimento)

			line.append( StringFunctions.strZero( getTipoInscCli() + "", 2 ) );// Posição 219 a 220 - Identificação do tipo de inscrição do sacado -- 01:CPF, 02:CNPJ

			line.append( format( getCpfCnpjCli(), ETipo.$9, 14, 0 ) );// Posição 221 a 234 - CNPJ/CPF
			
			if(getCodBanco().equals( Banco.BANCO_DO_BRASIL )) {
				line.append( format( StringFunctions.clearString( StringFunctions.clearAccents( getRazCli()), " " ).toUpperCase(), ETipo.X, 37, 0 ) );// Posição 235 a 271 - Nome do Sacado
				line.append( StringFunctions.replicate( " ", 3 ) ); // Posição 272 a 274 - Brancos
				line.append( format( StringFunctions.clearString( StringFunctions.clearAccents( getEndCli() ), " " ).toUpperCase(), ETipo.X, 52, 0 ) );// Posição 275 a 326 - Endereço Completo
			}
			else {
				line.append( format( getRazCli(), ETipo.X, 40, 0 ) );// Posição 235 a 274 - Nome do Sacado
				line.append( format( getEndCli(), ETipo.X, 40, 0 ) );// Posição 275 a 314 - Endereço Completo
				line.append( format( getMsg1(),   ETipo.X, 12, 0 ) );// Posição 315 a 326 - Mensagem 1
			}
			
			line.append( format( getCepCli(), ETipo.$9, 8, 0 ) ); // Posição 327 a 334 - Cep

			if(getCodBanco().equals( Banco.BANCO_DO_BRASIL )) {
				line.append( format( StringFunctions.clearAccents( getCidCli() ).toUpperCase(), ETipo.X, 15, 0 ) );// Posição 335 a 349 - Cidade do sacado
				line.append( format( getUfCli(), ETipo.X, 2, 0 ) );// Posição 350 a 351 - UF do sacado
				line.append( format( StringFunctions.clearString( StringFunctions.clearAccents( getRazAva()), " " ).toUpperCase(), ETipo.X, 40, 0 ) );// Posição 352 a 391 - Sacado/Avalista ou Mensagem 2
				line.append( StringFunctions.replicate( " ", 2 ) ); // Posição 392 a 393 - Informar Brancos
				line.append( " " ); // Posição 394 a 394 - Brancos para completar registro
			}
			else {
				line.append( format( getRazAva(), ETipo.X, 60, 0 ) );// Posição 335 a 394 - Sacado/Avalista ou Mensagem 2
			}
			
			line.append( format( getSeqregistro(), ETipo.$9, 6, 0 ) );// Posição 395 a 400 - Não Sequencial do registro

			line.append( (char) 13 ); 

			line.append( (char) 10 );

		} catch ( Exception e ) {
			throw new ExceptionCnab( "CNAB registro 1.\nErro ao escrever registro.\n" + e.getMessage() );
		}

		return line.toString();
	}
	
	private String getLineItau(String padraocnab) throws ExceptionCnab{
		StringBuilder line = new StringBuilder();
		
		try{
			
			line.append( "1" ); // Tipo de registro 1	
			line.append( format( getTipoInscEmp(), ETipo.$9, 2, 0 ) );// 002 a 003 - Tipo de inscrição da empresa
			line.append( format( getCpfCnpjEmp(), ETipo.$9, 14, 0 ) );//004 a 017 - Numero do CPF/CNPJ da Empresa
			line.append( format( getAgencia(), ETipo.$9, 4, 0 ) );//018 a 021 - Agencia
			line.append( "00" );//022 a 023 - Digito da agencia
			line.append( format( getConta(), ETipo.$9, 5, 0 ) );//024 a 028 - Numero da conta corrente
			line.append( format( getDigConta(), ETipo.X, 1, 0 ) );//029 a 029 - Digito da conta corrente
			line.append( format( "", ETipo.X, 4, 0) );//030 a 033
			line.append( "0000" );//034 - 037
			line.append( format(getIdentTitEmp(), ETipo.X, 25, 0 ) );//38 a 62
			line.append( format( getIdentTitulo(), ETipo.X, 8, 0 ) ); // 063 a 070 - Nosso numero
			line.append( "0000000000000"); // 71 a 83 - Quandtidade de moeda variável -- Preencher com zeros para REAL.
			line.append( format( getCodCarteira(), ETipo.$9, 3, 0) );// 084 a 086 Número da carteira no banco
			line.append( format( "", ETipo.X, 21, 0) );//087 a 107 Uso do banco 
			line.append( getCodCarteiraCnab() );//108 a 108
			line.append( format( getCodMovimento(), ETipo.$9, 2, 0 ) );//109 a 110 Identificação da ocorrência
			
			line.append( format( getDocCobranca(), ETipo.X, 10, 0) );//111 a 120 Número do documento

			line.append( CnabUtil.dateToString( getDtVencTitulo(), "DDMMAA" ) ); // Posição 121 a 126 - Data do vencimento do título
			line.append( format( getVlrTitulo(), ETipo.$9, 13, 2 ) ); // Posição 127 a 139 - Valor do título
			line.append( Banco.ITAU );//140 a 142
			line.append( "00000" );//143 a 147
			line.append( format( getEspecieTit(), ETipo.$9, 2, 0 ) ); // Posição 148 a 149 - Espécie de Título (Implementada de forma fixa pois difere do código no padrão cnab 240
			line.append( "N" ); // Posição 150 a 150 - Identificação (Sempre "N");
			line.append( CnabUtil.dateToString( getDtEmitTit(), "DDMMAA" ) ); // Posição 151 a 156 - Data de emissão do título
			
			line.append( format( getCodProtesto(), ETipo.$9, 2, 0 ) ); // Posição 157 a 158 - 1° Instrução - Código para juros
			line.append( StringFunctions.strZero( getDiasProtesto() + "", 2 ) ); // Posição 159 a 160 - 2° Instrução - Numero de dias para protesto
			
			if ( (getCodJuros() == 1) || (getCodJuros() ==2 ) ) { // Se Juros/Mora diária
				line.append( format( calcVlrJuros(getCodJuros(), getVlrTitulo(), getVlrJurosTaxa()), ETipo.$9, 13, 2 ) ); // Posição 161 a 173 - (se for do tipo mora diária) Mora por dia de atraso
			} else {
				line.append( StringFunctions.replicate( "0", 13 ) ); // Posição 161 a 173 - (Se não for do tipo mora diária) Mora por dia de atraso
			}
			
			line.append( CnabUtil.dateToString( getDtDesc(), "DDMMAA" ) ); // Posição 174 a 179 - Data limete para concessão de desconto
			line.append( format( getVlrDesc(), ETipo.$9, 13, 2 ) ); // Posição 180 a 192 - Valor de desconto
			line.append( StringFunctions.replicate( "0", 13 ) ); // Posição 193 a 205 - (Valor do IOF (Apenas para empresas seguradoras))
			line.append( format( getVlrAbatimento(), ETipo.$9, 13, 2 ) ); // Posição 206 a 218 - Valor do Abatimento a ser concedido ou cancelado (no caso de transação de abatimento)
			line.append( StringFunctions.strZero( getTipoInscCli() + "", 2 ) );// Posição 219 a 220 - Identificação do tipo de inscrição do sacado -- 01:CPF, 02:CNPJ
			line.append( format( getCpfCnpjCli(), ETipo.$9, 14, 0 ) );// Posição 221 a 234 - CNPJ/CPF
			line.append( format( getRazCli(), ETipo.X, 30, 0 ) );// Posição 235 a 264 - Nome do Sacado
			line.append( format( "", ETipo.X, 10, 0) );//Posiçãp 265 a 274
			line.append( format( getEndCli(), ETipo.X, 40, 0 ) );// Posição 275 a 314
			line.append( format( getBairCli(), ETipo.X, 12, 0 ) );// Posição 315 a 326
			line.append( format( getCepCli(), ETipo.$9, 8, 0 ) );// Posição 327 a 334
			line.append( format( getCidCli(), ETipo.X, 15, 0 ) );// Posição 335 a 349
			line.append( format( getUfCli(), ETipo.X, 2, 0 ) );// Posição 350 a 351
			line.append( format( getRazAva(), ETipo.X, 30, 0 ) );// Posição 352 a 381
			line.append( format( "", ETipo.X, 4, 0 ) );// Posição 382 a 385
			line.append( format( "", ETipo.$9, 6, 0 ) );// Posição 386 a 391
			line.append( format( "", ETipo.$9, 2, 0 ) );// Posição 392 a 393
			line.append( " " );// Posição 394 a 394 Complemento brancos
			line.append( format( getSeqregistro(), ETipo.$9, 6, 0 ) );// Posição 395 a 400 - Não Sequencial do registro
			
			line.append( (char) 13 ); 
			line.append( (char) 10 );

		} catch ( Exception e ) {
			throw new ExceptionCnab( "CNAB registro 1.\nErro ao escrever registro.\n" + e.getMessage() );
		}

		return StringFunctions.clearAccents( line.toString() );
	}
	
	private String getLineSicredi(String padraocnab) throws ExceptionCnab{
		
		StringBuilder line = new StringBuilder();
		
		try{
		
			line.append( "1" ); // Posição 001 a 001 - Tipo de registro 1	
			line.append( "A" ); // 002 a 002 - Tipo de cobrança ( Sicredi com Registro)
			line.append( "A" ); //003 a 003 - Tipo de carteira
			line.append( "A" ); //004 a 004 - Tipo de impressão
			line.append( StringFunctions.replicate( " ", 12 ) );  //005 a 016 - Filler
			line.append( "A" ); //017 a 017 - Tipo de moeda  ("A" = REAL)
			line.append(  "A" ); //018 a 018 - Tipo de desconto ("A" = VALOR) ("B"=PERCENTUAL)
			line.append( "A" ); //019 a 019 - Tipo de juros (ide opção acima)
			line.append( StringFunctions.replicate( " ", 28 ) ); //020 - 047 - Filler
			line.append( format( getIdentTitulo(), ETipo.X, 9, 0 ) ); // 048 a 055 - Nosso numero
			line.append( StringFunctions.replicate( " ", 6 ) ); //057 - 062 - Filler
			line.append(  CnabUtil.dateToString( getDtEmitTit(), "AAAAMMDD" )  ); //063 - 070 - Data da Instrução --verificar
			
			/**
			 * Tabela de Complemento para Instrução "31 - Alteração de outros dados".
			 * A - Desconto
			 * B - Juros por dia
			 * C - Desconto por dia de antecipação
			 * D - Data limite para concessão de desconto
			 * E - Cancelamento de protesto automático
			 * F - Carteira de cobrança - não disponível
			 */
			if( getInstrucoes() == 31){
				line.append( format((char) getOutrasInstrucoes().intValue(), ETipo.X, 1,0 )); //071 - 071 - Campo alterado, quando instrução "31" - Manual sicredi Cnab 400 --preencher com tabela acima.
			} else {
				line.append( format("", ETipo.X, 1,0 )); //071 - 071 - Campo alterado, quando instrução "31" - Manual sicredi Cnab 400 --preencher com tabela acima.
			}
				line.append( "N" );  //072 a 072 - Postagem do título
			line.append( " " );  //073 a 073 - Filler
			line.append( "B" );  //074 a 074 - Emissão do bloqueto("A"=Impressão pelo Sicredi) ("B"=Impressão pelo Cedente)
			line.append( "00" );  //075 a 076 - Número da parcela do carnê
			line.append( "00" );  //077 a 078 - Número total de parcelas do carnê
			line.append( StringFunctions.replicate( " ", 4 )  );  //079 a 082 - Filler
			//implementar futuramente
			line.append( StringFunctions.replicate( "0", 10 ) );  //083 a 092 - Valor de desconto por dia de antecipação
			
			if ( getVlrPercMulta().floatValue() > 0 ) {
				line.append( format( getVlrPercMulta(), ETipo.$9, 4, 2 ) ); //093 a 096 - % multa por pagamento em atraso
			}
			else {
				line.append( StringFunctions.replicate( "0", 4 ) ); //093 a 096 - % multa por pagamento em atraso
			}
			line.append( StringFunctions.replicate( " ", 12 ) );  //097 a 108 - Filler
			/**
			 * Tabela conf.tabela de instruções
			 * 01 - Cadastro de título
			 * 02 - Pedido de baixa
			 * 04 - concessão de abatimento
			 * 05 - Cancelamento de abatimento concedido
			 * 06 - Alteração de vencimento
			 * 09 - Pedido de protesto
			 * 18 - Sustar protesto e baixar título
			 * 19 - Sustar protesto e manter em carteira
			 * 31 - Alteração de outros dados
			 */
			line.append( format(getInstrucoes(), ETipo.$9, 2,0) );  //109 a 110 - Instrução - preencher com tabela acima!
			line.append( format(getIdentTitEmp(), ETipo.X, 10, 0 ) );//111 a 120 - Seu número
			line.append( CnabUtil.dateToString( getDtVencTitulo(), "DDMMAA" ) ); // Posição 121 a 126 - Data do vencimento do título
			line.append( format( getVlrTitulo(), ETipo.$9, 13, 2 ) ); // Posição 127 a 139 - Valor do título
			line.append( StringFunctions.replicate( " ", 9 ) );  // Posição 140 a 148 - Filler
			/**
			 * Tabela Espécie do Documento
			 * A - Duplicata Mercantil por Indicação (DMI)
			 * B - Duplicata Rural (DR)
			 * C - Nota Promissória (NP)
			 * D - Nota Promissória Rural (NR)
			 * E - Nota de Seguros (NS)
			 * G - Recibo (RC)
			 * H - Letra de Câmbio (LC)
			 * I - Nota de Débito (ND)
			 * J - Duplicata de Serviço por indicação (DSI)
			 * K - Outros(OS)
			 */
			line.append( format( (char) getEspecieTit(), ETipo.X, 1,0 ) ); // Posição 149 a 149 - Espécie de documento
			line.append( format( getAceite(), ETipo.X, 1, 0 ) ); // Posição 150 a 150 - Aceite do título
			line.append( CnabUtil.dateToString( getDtEmitTit(), "DDMMAA" ) ); // Posição 151 a 156 - Data de emissão do título
			line.append( format( getCodProtesto(), ETipo.$9, 2, 0 ) ); // Posição 157 a 158 - Instrução de protesto automático, '00' - Não protestar, '06' -protestar automaticamente
			line.append( StringFunctions.strZero( getDiasProtesto() + "", 2 ) ); // Posição 159 a 160 - 2° Instrução - Numero de dias para protesto
			
			if ( (getCodJuros() == 1) || (getCodJuros() ==2 ) ) { // Se Juros/Mora diária
				line.append( format( calcVlrJuros(getCodJuros(), getVlrTitulo(), getVlrJurosTaxa()), ETipo.$9, 13, 2 ) ); // Posição 161 a 173 - (se for do tipo mora diária) Mora por dia de atraso
			} else {
				line.append( StringFunctions.replicate( "0", 13 ) ); // Posição 161 a 173 - (Se não for do tipo mora diária) Mora por dia de atraso
			}
		
			line.append( CnabUtil.dateToString( getDtDesc(), "DDMMAA" ) ); // Posição 174 a 179 - Data limete para concessão de desconto
			//line.append( format( getVlrDesc(), ETipo.$9, 13, 2 ) ); // Posição 180 a 192 - Valor de desconto
			line.append( format( getVlrDesc(), ETipo.$9, 13, 2 ) ); // Posição 180 a 192 - Valor de desconto
			line.append(StringFunctions.replicate( "0", 13 )); // Posição 193 a 205 - Filler
			line.append( format( getVlrAbatimento(), ETipo.$9, 13, 2 ) ); // Posição 206 a 218 - Valor do Abatimento a ser concedido ou cancelado (no caso de transação de abatimento)
			line.append( StringFunctions.strZero( getTipoInscCli() + "", 1 ) );// Posição 219 a 220 - Identificação do tipo de inscrição do sacado -- 01:CPF, 02:CNPJ		
			line.append( StringFunctions.replicate( "0", 1 ) ); // Posição 220 a 220 - Filler
			line.append( format( getCpfCnpjCli(), ETipo.$9, 14, 0 ) ); // Posição 221 a 234 - CIC/CGC do sacado
			line.append( format( getRazCli(), ETipo.X, 40, 0 ) ); // Posição 235 a 274 - Nome do sacado
			line.append( format( getEndCli(), ETipo.X, 40, 0 ) ); // Posição 275 a 314 - Endereço do sacado
			line.append( StringFunctions.replicate( "0", 5 ) ); // Posição 315 a 319 - Código do sacado da cooperativa cedente
			line.append( StringFunctions.replicate( "0", 6 )); // Posição 320 a 325 - Filter Zeros
			line.append( StringFunctions.replicate( " ", 1 )); // Posição 326 a 326 - Filter brancos
			line.append(  format ( getCepCli(), ETipo.$9, 8, 0 ) ); // Posição 327 a 334 - CEP do sacado 
			line.append( StringFunctions.replicate( "0", 5 )); // Posição 335 a 339 - Código do Sacado junto ao cliente
			if (getCpfCnpjAva()==null || "".equals( getCpfCnpjAva() )) {
				line.append( StringFunctions.replicate( " ", 55 ));
			} else {
				line.append( format( getCpfCnpjAva(), ETipo.$9, 14, 0)); // Posição 340 a 353 - CIC/CGC do sacador avalista
				line.append( format( getRazAva(), ETipo.X, 41, 0 ) ); // Posição 354 a 394 - Nome do sacaodor avalista
			}
			line.append( format( getSeqregistro(), ETipo.$9, 6, 0 ) ); // Posição 395 a 400 -Número seqüencial do registro
			
			line.append( (char) 13 ); 

			line.append( (char) 10 );
			
		} catch ( Exception e ) {
			throw new ExceptionCnab( "CNAB registro 1.\nErro ao escrever registro.\n" + e.getMessage() );
		}

		return StringFunctions.clearAccents( line.toString() );
	}

	@ Override
	public void parseLine( final String line ) throws ExceptionCnab {

		try {

			if ( line == null ) {
				throw new ExceptionCnab( "CNAB registro 1.\nLinha nula." );
			}
			
			else {

				if ( line.length() < 400 ) { // Padrão CNAB 240
					setCodBanco( line.substring( 0, 3 ) );
					setLoteServico( line.substring( 3, 7 ).trim().length() > 0 ? Integer.parseInt( line.substring( 3, 7 ).trim() ) : 0 );
					setRegistroHeader( line.substring( 7, 8 ).trim().length() > 0 ? Integer.parseInt( line.substring( 7, 8 ).trim() ) : 0 );
					setTipoOperacao( line.substring( 8, 9 ) );
					setTipoServico( line.substring( 9, 11 ) );
					setFormaLancamento( line.substring( 11, 13 ) );
					setVersaoLayout( line.substring( 13, 16 ) );
					setTipoInscEmp( line.substring( 17, 18 ).trim().length() > 0 ? Integer.parseInt( line.substring( 17, 18 ).trim() ) : 0 );
					setCpfCnpjEmp( line.substring( 18, 33 ) );
					setCodConvBanco( line.substring( 33, 53 ) );
					setAgencia( line.substring( 53, 58 ) );
					setDigAgencia( line.substring( 58, 59 ) );
					setConta( line.substring( 59, 71 ) );
					setDigConta( line.substring( 71, 72 ) );
					setDigAgConta( line.substring( 72, 73 ) );
					setRazEmp( line.substring( 73, 103 ) );
					setMsg1( line.substring( 103, 143 ) );
					setMsg2( line.substring( 143, 183 ) );
					setNrRemRet( line.substring( 183, 191 ).trim().length() > 0 ? Integer.parseInt( line.substring( 183, 191 ).trim() ) : 0 );
					setDataRemRet( CnabUtil.stringDDMMAAAAToDate( line.substring( 191, 199 ).trim() ) );
					setDataCred( CnabUtil.stringDDMMAAAAToDate( line.substring( 199, 207 ).trim() ) );
				}
				else { // Padrão CNAB 400


					if ( ("1".equals( line.substring( 0, 1 ) ) ) && ( Banco.SICRED.equals( getCodBanco() ) ) )  {
						parseLineSicredi(line);
					} 
					else if ( ("1".equals( line.substring( 0, 1 ) ) ) && ( Banco.ITAU.equals( getCodBanco() ) ) )  {
						parseLineItau(line);
					}
					else if ( "1".equals( line.substring( 0, 1 ) ) ) { // Posição 01 a 01 - Identificação do Registro DETALHE

						setCodCarteira(  new Integer( line.substring( 82, 85 ) ) ); // Posição 83 a 85 - Número da carteira
						setCodRejeicoes( line.substring( 108, 110 ) );// Posição 109 a 110 - Código das ocorrências (vide pg.45)

						setIdentTitEmp( line.substring( 37, 62 ) ); // Posição 38 a 62 - Nro Controle do Participante

						setVlrOutrasDesp( CnabUtil.strToBigDecimal( line.substring( 188, 201 ) ) );

						setVlrJurosMulta( CnabUtil.strToBigDecimal( line.substring( 201, 214 ) ) );
						setVlrIOF( CnabUtil.strToBigDecimal( line.substring( 214, 227 ) ) );
						setVlrAbatimento( CnabUtil.strToBigDecimal( line.substring( 227, 240 ) ) );
						setVlrDesc( CnabUtil.strToBigDecimal( line.substring( 240, 253 ) ) );

						setVlrPago( CnabUtil.strToBigDecimal( line.substring( 253, 266 ) ) );
						
//						setVlrPago( CnabUtil.strToBigDecimal( line.substring( 305, 318 ) ) );
						
						setVlrJurosTaxa( CnabUtil.strToBigDecimal( line.substring( 266, 279 ) ) );
						setVlrOutrosCred( CnabUtil.strToBigDecimal( line.substring( 279, 292 ) ) );
						
						// Posição 111 a 116 - Data da Entrada/Liquidação (DDMMAA) 
						setDataLiquidacao( CnabUtil.stringDDMMAAToDate( line.substring( 110, 116 ).trim() ) );
												
						if(getCodBanco().equals( Banco.BANCO_DO_BRASIL )) {							
							//Posição 176 a 181 -Data do Credito.
							setDataCred( CnabUtil.stringDDMMAAToDate( line.substring( 175, 181 ).trim() ) );
							
						}
						else {
							setDataCred( CnabUtil.stringDDMMAAToDate( line.substring( 295, 301 ).trim() ) );
						}
						
					
						System.out.println( "Rejeição...." + line.substring( 86,88 ));
						
					} else if ( "7".equals( line.substring( 0, 1 ) ) ) { // Posição 01 a 01 - Identificação do Registro DETALHE

						setIdentTitEmp( line.substring( 38, 63 ) ); // Posição 39 a 63 - Nro Controle do Participante
						setCodCarteira(  new Integer( line.substring( 82, 85 ) ) ); // Posição 83 a 85 - Número da carteira
						setCodRejeicoes( line.substring( 108, 110 ) );// Posição 109 a 110 - Código das ocorrências (vide pg.45)
						//setVlrPago( CnabUtil.strToBigDecimal( line.substring( 152, 165 ) ) ); // 153 a 165 - Valor do título 
						setVlrOutrasDesp( CnabUtil.strToBigDecimal( line.substring( 188, 201 ) ) ); // 189 a 201 - Outras despesas
						setVlrJurosMulta( CnabUtil.strToBigDecimal( line.substring( 201, 214 ) ) ); // 202 a 214 - Juros do desconto
						setVlrIOF( CnabUtil.strToBigDecimal( line.substring( 214, 227 ) ) ); // 215 a 227 - IOF do desconto
						setVlrAbatimento( CnabUtil.strToBigDecimal( line.substring( 227, 240 ) ) ); // 228 a 240 - Valor do abatimento
						setVlrDesc( CnabUtil.strToBigDecimal( line.substring( 240, 253 ) ) ); // 241 a 253 - Desconto concedido 
						setVlrPago( CnabUtil.strToBigDecimal( line.substring( 253, 266 ) ) ); // 254 a 266 - Valor recebido (valor recebido parcial)
						setVlrJurosTaxa( CnabUtil.strToBigDecimal( line.substring( 266, 279 ) ) ); // 267 a 279 - Juros de mora
						setVlrOutrosCred( CnabUtil.strToBigDecimal( line.substring( 279, 292 ) ) ); // 280 a 292 - Outros recebimentos
						
						setDataLiquidacao( CnabUtil.stringDDMMAAToDate( line.substring( 110, 116 ).trim() ) );
						
						if(getCodBanco().equals( Banco.BANCO_DO_BRASIL )) {
							setDataCred( CnabUtil.stringDDMMAAToDate( line.substring( 175, 181 ).trim() ) ); // 176 a 181 - Data de crédito (DDMMAA) 
							//setDataCred( CnabUtil.stringDDMMAAToDate( line.substring( 110, 116 ).trim() ) ); // 111 a 116 - Data de liquidação (DDMMAA) 	
						}
						else {
							setDataCred( CnabUtil.stringDDMMAAToDate( line.substring( 295, 301 ).trim() ) );
						}
						
						System.out.println( "Rejeição...." + line.substring( 86,88 ));
					}
					else {
						Funcoes.mensagemErro( null, "Erro na leitura do arquivo de retorno.\nNão é um registro de transação válido para o padrão CNAB 400!" );
					}

				}

			}
		} catch ( Exception e ) {
			throw new ExceptionCnab( "CNAB registro 1.\nErro ao ler registro.\n" + e.getMessage() );
		}
	}
	
	private void parseLineSicredi(final String line) throws ExceptionCnab {
		//setCodCarteira(  new Integer( line.substring( 82, 85 ) ) ); // Posição 83 a 85 - Número da carteira
		setCodRejeicoes( line.substring( 108, 110 ) );// Posição 109 a 110 - Código das ocorrências (vide pg.45)
		//seu número
		setIdentTitEmp( line.substring( 116, 126 ) ); // Posição 38 a 62 - Nro Controle do Participante

		
		setVlrOutrasDesp( CnabUtil.strToBigDecimal( line.substring( 188, 201 ) ) );
		
	//	setVlrIOF( CnabUtil.strToBigDecimal( line.substring( 214, 227 ) ) );
		setVlrAbatimento( CnabUtil.strToBigDecimal( line.substring( 227, 240 ) ) );
		setVlrDesc( CnabUtil.strToBigDecimal( line.substring( 240, 253 ) ) );

		setVlrPago( CnabUtil.strToBigDecimal( line.substring( 253, 266 ) ) );
		
		setVlrJurosTaxa( CnabUtil.strToBigDecimal( line.substring( 266, 279 ) ) );
		
		setVlrJurosMulta( CnabUtil.strToBigDecimal( line.substring( 279, 292 ) ) );
		
		//setVlrOutrosCred( CnabUtil.strToBigDecimal( line.substring( 279, 292 ) ) );
		
		//Posição 111 a 116 - Data de ocorrência/liquidação
		setDataLiquidacao( CnabUtil.stringDDMMAAToDate( line.substring( 110, 116 ).trim() ) );
		
		//Posição 329 a 336 -Data do credito na conta corrente
		setDataCred( CnabUtil.stringAAAAMMDDToDate( line.substring(  328, 336 ).trim() ) );
		
	}

	private void parseLineItau(final String line) throws ExceptionCnab {

		//setCodCarteira(  new Integer( line.substring( 82, 85 ) ) ); // Posição 83 a 85 - Número da carteira
		
		//seu número
		setIdentTitEmp( line.substring( 37, 62 ) ); // Posição 38 a 62 - Nro Controle do Participante

		setCodRejeicoes( line.substring( 108, 110 ) );// Posição 109 a 110 - Código das ocorrências (vide pg.45)

		//Posição 111 a 116 - Data de ocorrência/liquidação
		setDataLiquidacao( CnabUtil.stringDDMMAAToDate( line.substring( 110, 116 ).trim() ) );

	//	setVlrIOF( CnabUtil.strToBigDecimal( line.substring( 214, 227 ) ) );
		setVlrAbatimento( CnabUtil.strToBigDecimal( line.substring( 227, 240 ) ) );
		setVlrDesc( CnabUtil.strToBigDecimal( line.substring( 240, 253 ) ) );

		setVlrPago( CnabUtil.strToBigDecimal( line.substring( 253, 266 ) ) );  //  254 a 266 - Valor lançado em conta corrente - Valor principal
		
		setVlrJurosTaxa( CnabUtil.strToBigDecimal( line.substring( 266, 279 ) ) );
		
		setVlrJurosMulta( CnabUtil.strToBigDecimal( line.substring( 279, 292 ) ) );
		
		//setVlrOutrosCred( CnabUtil.strToBigDecimal( line.substring( 279, 292 ) ) );
		
		setDataCred( CnabUtil.stringAAAAMMDDToDate( line.substring(  295, 301 ).trim() ) ); //Posição 296 a 301 -Data do credito na conta corrente
		
	}
	
}