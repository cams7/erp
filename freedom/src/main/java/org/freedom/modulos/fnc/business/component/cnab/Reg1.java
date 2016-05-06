package org.freedom.modulos.fnc.business.component.cnab;

import java.util.Date;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.business.exceptions.ExceptionCnab;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.ETipo;


public class Reg1 extends Reg {

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

	public Reg1() {

		setRegistroHeader( 1 );
		setTipoServico( "01" );
		setVersaoLayout( "020" );
	}

	public Date getDataCred() {

		return dataCred;
	}

	public void setDataCred( Date dataCred ) {

		this.dataCred = dataCred;
	}

	public Reg1( final String line ) throws ExceptionCnab {

		this();
		parseLine( line );
	}

	public String getAgencia() {

		return agencia;
	}

	public void setAgencia( final String agencia ) {

		this.agencia = agencia;
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

		this.codConvBanco = codConvBanco;
	}

	public String getConta() {

		return conta;
	}

	public void setConta( final String conta ) {

		this.conta = conta;
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

	
	public String getPosto() {
	
		return posto;
	}

	
	public void setPosto( String posto ) {
	
		this.posto = posto;
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
	 * Sequencial e não deve ser repetido dentro do arquivo.<br>
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

	/**
	 * Indica o número da versão do layout do lote, composto de:<br>
	 * versão : 2 digitos.<br>
	 * release: 1 digito.<br>
	 */
	private void setVersaoLayout( final String versaoLayout ) {

		this.versaoLayout = versaoLayout;
	}

	@ Override
	public String getLine( String padraocnab ) throws ExceptionCnab {

		StringBuilder line = new StringBuilder();

		try {
			if ( padraocnab.equals( CNAB_240 ) ) {
				line.append( format( getCodBanco(), ETipo.$9, 3, 0 ) );
				line.append( format( getLoteServico(), ETipo.$9, 4, 0 ) );
				line.append( getRegistroHeader() );
				line.append( format( getTipoOperacao(), ETipo.X, 1, 0 ) );
				line.append( getTipoServico() );
				line.append( format( getFormaLancamento(), ETipo.$9, 2, 0 ) );
				line.append( getVersaoLayout() );
				line.append( ' ' );
				line.append( format( getTipoInscEmp(), ETipo.$9, 1, 0 ) );
				line.append( format( getCpfCnpjEmp(), ETipo.$9, 15, 0 ) );
				line.append( format( getCodConvBanco(), ETipo.X, 20, 0 ) );
				line.append( format( getAgencia(), ETipo.$9, 5, 0 ) );
				line.append( format( getDigAgencia(), ETipo.X, 1, 0 ) );
				line.append( format( getConta(), ETipo.$9, 12, 0 ) );
				line.append( format( getDigConta(), ETipo.X, 1, 0 ) );
				line.append( format( getDigAgConta(), ETipo.X, 1, 0 ) );
				line.append( format( getRazEmp(), ETipo.X, 30, 0 ) );
				line.append( format( getMsg1(), ETipo.X, 40, 0 ) );
				line.append( format( getMsg2(), ETipo.X, 40, 0 ) );
				line.append( format( getNrRemRet(), ETipo.$9, 8, 0 ) );
				line.append( CnabUtil.dateToString( getDataRemRet(), null ) );
				line.append( CnabUtil.dateToString( getDataCred(), null ) );
				line.append( StringFunctions.replicate( " ", 33 ) );
				line.append( (char) 13 );
				line.append( (char) 10 );
			}

		} catch ( Exception e ) {
			throw new ExceptionCnab( "CNAB registro 1.\nErro ao escrever registro.\n" + e.getMessage() );
		}

		return line.toString();
	}

	@ Override
	public void parseLine( final String line ) throws ExceptionCnab {

		try {

			if ( line == null ) {
				throw new ExceptionCnab( "CNAB registro 1.\nLinha nula." );
			}
			else {

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
		} catch ( Exception e ) {
			throw new ExceptionCnab( "CNAB registro 1.\nErro ao ler registro.\n" + e.getMessage() );
		}
	}

}