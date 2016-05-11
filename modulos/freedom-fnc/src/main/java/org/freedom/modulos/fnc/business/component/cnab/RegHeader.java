package org.freedom.modulos.fnc.business.component.cnab;

import java.util.Date;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.business.component.Banco;
import org.freedom.library.business.exceptions.ExceptionCnab;
import org.freedom.library.functions.Funcoes;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.ETipo;


public class RegHeader extends Reg {

	private String codBanco;

	private String loteServico;

	private int registroHeader;

	private int tipoInscEmp;

	private String razEmp;

	private String cpfCnpjEmp;

	private String codConvBanco;

	private String agencia;

	private String digAgencia;

	private String posto;
	
	private String conta;

	private String digConta;

	private String digAgConta;

	private String nomeBanco;

	private int tipoOperacao;

	private Date dataGeracao;

	private int horaGeracao;

	private Integer sequenciaArq;

	private String versaoLayout;

	private String densidadeArq;

	private String usoBanco;

	private String usoEmp;

	// private String COBRANÇA S/PAPEL
	private String usoVans;

	private String tipoServico;

	private String ocorrencias;

	public RegHeader() {

		setLoteServico( "0000" );
		setRegistroHeader( 0 );
		setTipoOperacao( 1 );
		setVersaoLayout( "030" );
	}

	public RegHeader( String line ) throws ExceptionCnab {

		this();
		parseLine( line );
	}

	public String getAgencia() {

		return agencia;
	}

	public void setAgencia( final String agencia ) {

		this.agencia = agencia;
	}

	public String getCodBanco() {

		return codBanco;
	}

	public void setCodBanco( final String codBanco ) {

		this.codBanco = codBanco;
	}

	/**
	 * Indentifica a empresa no banco para determinados tipos de serviços.<br>
	 * Observar as regras de preenchimento abaixo no que se refere ao header de serviço/lote:<br>
	 * "9999999994444CCVVV " / 20 bytes / , onde:<br>
	 * 999999999 - Código do convênio.<br>
	 * 4444 - Código do produto.<br>
	 * CC - Carteira de cobrança.<br>
	 * VVV - Variação da carteira de cobrança.<br>
	 */
	public String getCodConvBanco() {

		return codConvBanco;
	}

	public void setCodConvBanco( final String codConvenio ) {

		this.codConvBanco = codConvenio;
	}

	public String getConta() {

		return conta;
	}

	public void setConta( final String conta ) {

		this.conta = conta;
	}

	/**
	 * Inscrição da empresa. Conforme o tipo da inscrição.<br>
	 * 
	 * @see org.freedom.modulos.fnc.business.component.cnab.CnabUtil.Reg1#setTipoInscEmp(int tipoInscEmp )
	 */
	public String getCpfCnpjEmp() {

		return cpfCnpjEmp;
	}

	public void setCpfCnpjEmp( final String cpfCnpjEmp ) {

		this.cpfCnpjEmp = cpfCnpjEmp;
	}

	public Date getDataGeracao() {

		return dataGeracao;
	}

	public void setDataGeracao( final Date dataGeracao ) {

		this.dataGeracao = dataGeracao;
	}

	public String getDensidadeArq() {

		return densidadeArq;
	}

	public void setDensidadeArq( final String densidadeArq ) {

		this.densidadeArq = densidadeArq;
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

	public int getHoraGeracao() {

		return horaGeracao;
	}

	public void setHoraGeracao( final int horaGeracao ) {

		this.horaGeracao = horaGeracao;
	}

	public String getLoteServico() {

		return loteServico;
	}

	private void setLoteServico( final String loteServico ) {

		this.loteServico = loteServico;
	}

	public String getNomeBanco() {

		return nomeBanco;
	}

	public void setNomeBanco( final String nomeBanco ) {

		this.nomeBanco = nomeBanco;
	}

	public String getOcorrencias() {

		return ocorrencias;
	}

	public void setOcorrencias( final String ocorrencias ) {

		this.ocorrencias = ocorrencias;
	}

	public String getRazEmp() {

		return razEmp;
	}

	public void setRazEmp( final String razaoEmp ) {

		this.razEmp = razaoEmp;
	}

	public int getRegistroHeader() {

		return registroHeader;
	}

	private void setRegistroHeader( final int registroHeader ) {

		this.registroHeader = registroHeader;
	}

	public Integer getSequenciaArq() {

		return sequenciaArq;
	}

	public void setSequenciaArq( final Integer sequenciaArq ) {

		this.sequenciaArq = sequenciaArq;
	}

	/**
	 * Indica o tipo de inscrição da empresa.<br>
	 * 1 - CPF<br>
	 * 2 - CNPJ<br>
	 */
	public int getTipoInscEmp() {

		return tipoInscEmp;
	}

	public void setTipoInscEmp( final int tipoInscEmp ) {

		this.tipoInscEmp = tipoInscEmp;
	}

	public String getTipoServico() {

		return tipoServico;
	}

	public void setTipoServico( final String tipoServico ) {

		this.tipoServico = tipoServico;
	}

	public String getUsoBanco() {

		return usoBanco;
	}

	public void setUsoBanco( final String usoBanco ) {

		this.usoBanco = usoBanco;
	}

	public String getUsoEmp() {

		return usoEmp;
	}

	public void setUsoEmp( final String usoEmp ) {

		this.usoEmp = usoEmp;
	}

	public String getUsoVans() {

		return usoVans;
	}

	public void setUsoVans( final String usoVans ) {

		this.usoVans = usoVans;
	}

	/**
	 * Indica o tipo de operação.<br>
	 * 1 - Remessa.<br>
	 * 2 - Retorno.<br>
	 */
	public int getTipoOperacao() {

		return tipoOperacao;
	}

	private void setTipoOperacao( final int tipoOperacao ) {

		this.tipoOperacao = tipoOperacao;
	}

	public String getVersaoLayout() {

		return versaoLayout;
	}

	private void setVersaoLayout( final String versaoLayout ) {

		this.versaoLayout = versaoLayout;
	}

	@ Override
	public String getLine( String padraocnab ) throws ExceptionCnab {
		StringBuilder line = new StringBuilder();

		try {

			if ( padraocnab.equals( CNAB_240 ) ) {

				line.append( format( getCodBanco(), ETipo.$9, 3, 0 ) );
				line.append( getLoteServico() );
				line.append( getRegistroHeader() );
				line.append( StringFunctions.replicate( " ", 9 ) );
				line.append( format( getTipoInscEmp(), ETipo.$9, 1, 0 ) );
				line.append( format( getCpfCnpjEmp(), ETipo.$9, 14, 0 ) );
				line.append( format( getCodConvBanco(), ETipo.X, 20, 0 ) );
				line.append( format( getAgencia(), ETipo.$9, 5, 0 ) );
				line.append( format( getDigAgencia(), ETipo.X, 1, 0 ) );
				line.append( format( getConta(), ETipo.$9, 12, 0 ) );
				line.append( format( getDigConta(), ETipo.X, 1, 0 ) );
				line.append( format( getDigAgConta(), ETipo.X, 1, 0 ) );
				line.append( format( getRazEmp(), ETipo.X, 30, 0 ) );
				line.append( format( getNomeBanco(), ETipo.X, 30, 0 ) );
				line.append( StringFunctions.replicate( " ", 10 ) );
				line.append( format( getTipoOperacao(), ETipo.$9, 1, 0 ) );
				line.append( CnabUtil.dateToString( getDataGeracao(), null ) );
				line.append( format( getHoraGeracao(), ETipo.$9, 6, 0 ) );
				line.append( format( getSequenciaArq(), ETipo.$9, 6, 0 ) );
				line.append( getVersaoLayout() );
				line.append( format( getDensidadeArq(), ETipo.$9, 5, 0 ) );
				line.append( format( getUsoBanco(), ETipo.X, 20, 0 ) );
				line.append( format( getUsoEmp(), ETipo.X, 20, 0 ) );
				line.append( StringFunctions.replicate( " ", 11 ) );
				line.append( "CSP" );// indentifica cobrança sem papel.
				line.append( format( getUsoVans(), ETipo.$9, 3, 0 ) );
				line.append( format( getTipoServico(), ETipo.X, 2, 0 ) );
				line.append( format( getOcorrencias(), ETipo.X, 10, 0 ) );
				line.append( (char) 13 );
				line.append( (char) 10 );
			} else if ( padraocnab.equals( CNAB_400 ) ) {

				line.append( getRegistroHeader() ); // Posição 001 a 001 - Identificação do Registro
				line.append( "1" ); // Posição 002 a 002 - Identificação do Arquvo de remessa
				line.append( format( LITERAL_REM, ETipo.X, 7, 0 ) ); // Posição 003 a 009 - Literação de remassa
				line.append( "01" ); // Posição 010 a 011 - Código do serviço (01)
				line.append( format( LITERAL_SERV, ETipo.X, 15, 0 ) );// Posição 012 a 026 - Literal Serviço
				
				// Informação personalizada para o banco do brasil
				if(getCodBanco().equals( Banco.BANCO_DO_BRASIL )) {
					line.append( StringFunctions.strZero( getAgencia(), 4 ) );// Posição 027 a 030 - Prefixo da agencia
					line.append( getDigAgencia() );// Posição 031 a 031 - Digito verificador da agencia
					line.append( StringFunctions.strZero(getConta(),8 ));// Posição 032 a 039 - Numero da conta
					line.append( getDigConta());// Posição 040 a 040 - Digito verificador da conta
					
					if (getCodConvBanco().length() < 7) {
						line.append( StringFunctions.strZero( getCodConvBanco(), 6 ) );// Posição 041 a 046 - Código da Empresa
					} else {
						line.append( StringFunctions.strZero( "", 6 ) );// Posição 041 a 046 - Preencher com zeros
					}
				} else if(getCodBanco().equals( Banco.ITAU ) ) {
					line.append( StringFunctions.strZero( getAgencia(), 4 ) );// Posição 027 a 030 - Prefixo da agencia
					line.append( "00" );// 031 a 032
					line.append( format(getConta(), ETipo.$9, 5, 0) );// 033 a 037
					line.append( getDigConta() );// 038 a 038
					line.append( Funcoes.adicionaEspacos("", 8 ) );// 039 a 046
				} else if(getCodBanco().equals( Banco.SICRED )) {
					line.append( format(getConta(), ETipo.$9, 5, 0) );// Posição 027 a 030 - Prefixo da agencia
					//line.append( getDigAgencia() );// Posição 031 a 031 - Digito verificador da agencia
					line.append(format( getCpfCnpjEmp(), ETipo.$9, 14, 0 ));// Posição 032 a 045 - CIC/CFC do cedente
				} else {
					line.append( StringFunctions.strZero( getCodConvBanco(), 20 ) );// Posição 027 a 046 - Código da Empresa
				}
				if (getCodBanco().equals( Banco.SICRED )){
					line.append( StringFunctions.replicate( " ", 31 ) ); //Posição 046 a 076 - Filler
					line.append( Banco.SICRED ); //Posição 077 a 079 - número do Sicredi
					line.append(format("SICREDI",ETipo.X, 15,0 ));//Posição 080 a 094 - Literal Sicredi
					line.append( CnabUtil.dateToString( getDataGeracao(), DATA_08_AAAAMMDD) ); //Posição 95 a 102 - Data de gravação do arquivo AAAAMMDD
					line.append( StringFunctions.replicate( " ", 8)); //Posições 103 a 110 - Filler
					line.append( StringFunctions.strZero( getSequenciaArq()+"", 7 ) ); //Posição 111 a 117 - Número da remessa
					line.append( StringFunctions.replicate( " ", 273)  ); //Posição 118 a 390 - Filler
					line.append( format("2.00", ETipo.X,4,0 )); //Posição 391 a 394 - Versão do sistema
				} else {
					line.append( format( getRazEmp().toUpperCase(), ETipo.X, 30, 0 ) );// Posição 047 a 076 - Nome da Empresa
					line.append( format( getCodBanco(), ETipo.$9, 3, 0 ) );// Posição 077 a 079 - Número do banco na câmara de compensação
					line.append( format( getNomeBanco().toUpperCase(), ETipo.X, 15, 0 ) );// Posição 080 a 094 - Nome do banco por extenso
					line.append( CnabUtil.dateToString( getDataGeracao(), DATA_06 ) );// Posição 095 a 100 - Data da gravação do arquivo
				
					if(getCodBanco().equals( Banco.BANCO_DO_BRASIL )) {
						line.append( StringFunctions.strZero( getSequenciaArq()+"", 7 ) );// Posição 101 a 107 - Sequencial da remessa
						
						if (getCodConvBanco().length()<7) {
							line.append( StringFunctions.replicate( " ", 287 ) ); // Posição 108 a 394 - Espaço em branco
						} else {
							line.append( StringFunctions.replicate( " ", 22 ) ); // Posição 108 a 130 - Espaço em branco
							line.append( format( getCodConvBanco(), ETipo.X, 7, 0 ) ); // Posição 130 a 136 - Codigo do convenio lider 
							line.append( StringFunctions.replicate( " ", 258 ) ); // Posição 136 a 394 - Espaço em branco
						}
					} else if(getCodBanco().equals( Banco.ITAU )) {
						line.append( Funcoes.adicionaEspacos( "", 294 ) );//101 / 394
					} else{
						line.append( StringFunctions.replicate( " ", 8 ) );// Posição 101 a 108 - Espaço em branc
						line.append( LITERAL_SISTEMA );// Posição 109 a 110 - Literal do Sistema (MX - Micro a micro)
						line.append( format( getSequenciaArq(), ETipo.$9, 7, 0 ) ); // Posição 111 a 117 - Nro sequencial da remessa
						line.append( StringFunctions.replicate( " ", 277 ) ); // Posição 118 a 394 - Espaço em branco
					}
				}
				line.append( format( 1, ETipo.$9, 6, 0 ) ); // Sequencial do registro de um em um
				line.append( (char) 13 );
				line.append( (char) 10 );
			}

		} catch ( Exception e ) {
			throw new ExceptionCnab( "CNAB registro Header.\nErro ao escrever registro.\n" + e.getMessage() );
		}

		return StringFunctions.clearAccents( line.toString() );
	}

	@ Override
	public void parseLine( final String line ) throws ExceptionCnab {

		try {

			if ( line == null ) {
				throw new ExceptionCnab( "CNAB registro Header.\nLinha nula." );
			} else {
				if ( line.length() < 400 ) { // Padrão CNAB 240
					setCodBanco( line.substring( 0, 3 ) );
					setLoteServico( line.substring( 3, 7 ) );
					setRegistroHeader( line.substring( 7, 8 ).trim().length() > 0 ? Integer.parseInt( line.substring( 7, 8 ).trim() ) : 0 );
					setTipoInscEmp( line.substring( 17, 18 ).trim().length() > 0 ? Integer.parseInt( line.substring( 17, 18 ).trim() ) : 0 );
					setCpfCnpjEmp( line.substring( 18, 32 ) );
					setCodConvBanco( line.substring( 32, 52 ) );
					setAgencia( line.substring( 52, 57 ) );
					setDigAgencia( line.substring( 57, 58 ) );
					setConta( line.substring( 58, 70 ) );
					setDigConta( line.substring( 70, 71 ) );
					setDigAgConta( line.substring( 71, 72 ) );
					setRazEmp( line.substring( 72, 102 ) );
					setNomeBanco( line.substring( 102, 132 ) );
					setTipoOperacao( line.substring( 142, 143 ).trim().length() > 0 ? Integer.parseInt( line.substring( 142, 143 ).trim() ) : 0 );
					setDataGeracao( CnabUtil.stringDDMMAAAAToDate( line.substring( 143, 151 ).trim() ) );
					setHoraGeracao( line.substring( 151, 157 ).trim().length() > 0 ? Integer.parseInt( line.substring( 151, 157 ).trim() ) : 0 );
					setSequenciaArq( line.substring( 157, 163 ).trim().length() > 0 ? Integer.parseInt( line.substring( 157, 163 ).trim() ) : 0 );
					setVersaoLayout( line.substring( 163, 166 ) );
					setDensidadeArq( line.substring( 166, 171 ) );
					setUsoBanco( line.substring( 171, 191 ) );
					setUsoEmp( line.substring( 191, 211 ) );
					setUsoVans( line.substring( 225, 228 ) );
					setTipoServico( line.substring( 228, 230 ) );
					setOcorrencias( line.substring( 230 ) );
				} else { // Padrão CNAB 400
					System.out.println( "LENDO CNAB 400..." );
					// Identificador de retorno
					if ( "2".equals( line.substring( 1, 2 ) ) ) {
						setCodBanco( line.substring( 76, 79 ) );
						if("748".equals( getCodBanco())){
							parseLineSicredi( line.toString() );
						} else {
							setRegistroHeader( new Integer( line.substring( 0, 1 ) ).intValue() );
							setTipoOperacao( 2 );
							setCodConvBanco( line.substring( 26, 46 ) );
							setRazEmp( line.substring( 46, 76 ) );
							setNomeBanco( line.substring( 79, 94 ) );
							setDataGeracao( CnabUtil.stringDDMMAAToDate( line.substring( 94, 100 ).trim() ) );
							setSequenciaArq( Integer.parseInt( line.substring( 394, 400 ) ) );
						}
					} else {
						Funcoes.mensagemInforma( null, "Arquivo informado não representa um arquivo de retorno válido no padrão CNAB 400!" );
					}
				}
			}
		} catch ( Exception e ) {
			throw new ExceptionCnab( "CNAB registro Header.\nErro ao ler registro.\n" + e.getMessage() );
		}
	}
	
	private void parseLineSicredi(final String line) throws ExceptionCnab {
		setRegistroHeader( new Integer( line.substring( 0, 1 ) ).intValue() );
		setTipoOperacao( 2 );
		//setCodConvBanco( line.substring( 26, 46 ) ); - Não possui esse campo.
		//setRazEmp( line.substring( 46, 76 ) ); --Sicredi não possui esse campo.
		setCpfCnpjEmp( line.substring( 31,45 ) );
		setNomeBanco( line.substring( 79, 94 ) );
		setDataGeracao( CnabUtil.stringAAAAMMDDToDate( line.substring( 94, 102 ).trim() ) );
		setSequenciaArq( Integer.parseInt( line.substring( 394, 400 ) ) );
	}
}
