
package org.freedom.ecf.driver;

import java.util.Date;

/**
 * Classe implementa metodos de acesso a comandos de impressão <BR>
 * Projeto: freedom-ecf <BR>
 * Pacote: org.freedom.ecf.driver <BR>
 * Classe: @(#)ECFBematech.java <BR>
 * <BR>
 * Este programa é licenciado de acordo com a LGPL (Lesser General Public License), <BR>
 * versão 2.1, Fevereiro de 1999 <BR>
 * A LGPL deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste Programa. <BR>
 * Caso uma cópia da LGPL não esteja disponível junto com este Programa, você pode contatar <BR>
 * o LICENCIADOR ou então pegar uma cópia em: <a href=http://creativecommons.org/licenses/LGPL/2.1/legalcode.pt> Creative Commons</a> <BR>
 * Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa é preciso estar de acordo com os termos da LGPL. <BR>
 * <BR>
 * 
 * @author Setpoint Informática Ltda. Robson Sanchez/Alex Rodrigues <BR>
 * @version 1.0.0 - 05/04/2006 <BR>
 * <BR>
 */
public class ECFBematech extends AbstractECFDriver {

	/**
	 * Construtor da classe ECFBematech. <BR>
	 */
	public ECFBematech() {

		super();
	}

	/**
	 * Construtor da classe ECFBematech. <BR>
	 * Inicia a construção da classe chamando o construtor padrão da classe super <BR>
	 * e chama o metodo ativaPorta(int). <BR>
	 * 
	 * @param com
	 *            parametro para ativação da porta serial.<BR>
	 */
	public ECFBematech( final int com ) {

		super();
		activePort( com );
	}

	/**
	 * Construtor da classe ECFBematech. <BR>
	 * Inicia a construção da classe chamando o construtor padrão da classe super <BR>
	 * e chama o metodo ativaPorta(int). <BR>
	 * 
	 * @param port
	 *            parametro para ativação da porta serial.<BR>
	 */
	public ECFBematech( final String port ) {

		super();
		activePort( port );
	}

	/**
	 * Prepara o comando conforme o protocolo de comunicação com a impressora. <BR>
	 * O protocolo de comunicação com a impressora é estruturado <BR>
	 * em blocos e possui a seguinte forma: <BR>
	 * <BR>
	 * STX - byte indicativo de inicio de transmissão. <BR>
	 * NBL -byte nenos significativo, da soma do número de bytes que serão enviados. <BR>
	 * NBH - byte mais significativo, da soma do número de bytes que serão enviados. <BR>
	 * CMD - Sequência de bytes que compõe o comando e seus parâmetros. <BR>
	 * CSL - byte menos significativo, da soma dos valores dos bytes que compõem o camando e seu parâmetros(CMD). <BR>
	 * 
	 * @param CMD
	 *            comando a ser executado e seus parâmetros. <BR>
	 * @see org.freedom.ecf.driver.AbstractECFDriver#preparaCmd(byte[])
	 */
	public byte[] preparaCmd( final byte[] CMD ) {

		final int tamCMD = CMD.length;
		final int tam = tamCMD + 2;
		int soma = 0;
		byte CSL = 0;
		byte CSH = 0;
		final byte NBL = (byte) ( tam % 256 );
		final byte NBH = (byte) ( tam / 256 );
		byte[] result = new byte[ 5 + tamCMD ];
		result[ 0 ] = STX;
		result[ 1 ] = NBL;
		result[ 2 ] = NBH;
		for ( int i = 0; i < tamCMD; i++ ) {
			soma += CMD[ i ];
			result[ i + 3 ] = CMD[ i ];
		}
		CSL = (byte) ( soma % 256 );
		CSH = (byte) ( soma / 256 );
		result[ result.length - 2 ] = CSL;
		result[ result.length - 1 ] = CSH;
		return result;
	}

	/**
	 * Este metodo executa o comando chamando os metodos <BR>
	 * preparaCmd(byte[]) <BR>
	 * enviaCmd(byte[]) <BR>
	 * aguardaImpressal(byte[]) <BR>
	 * e devolve o resultado do emvio do camando. <BR>
	 * 
	 * @param CMD
	 *            comando a ser executado e seus parâmetros. <BR>
	 * @see org.freedom.ecf.driver.AbstractECFDriver#executaCmd(byte[], int)
	 */
	public STResult executaCmd( final byte[] CMD, final int tamresult ) {

		byte[] result = null;
		byte[] cmd = null;
		cmd = preparaCmd( CMD );
		result = enviaCmd( cmd, tamresult );
		return checkResult( result );
	}

	/**
	 * Converte o result dos comandos do formato BCD para ASCII. <BR>
	 * 
	 * @param bcdParam
	 *            result a ser convertido. <BR>
	 * @return String com o resultado da converção. <BR>
	 */
	private String bcdToAsc( final byte[] bcdParam ) {

		final StringBuffer result = new StringBuffer();
		int bcd = 0;
		byte byteBH = 0;
		byte byteBL = 0;
		for ( int i = 0; i < bcdParam.length; i++ ) {
			bcd = bcdParam[ i ];
			// Ajuste dos bytes para o padrão de cálculo (o java trabalha com bytes de -128 a 127)
			if ( bcd < 0 ) {
				bcd += 256;
			}
			byteBH = (byte) ( bcd / 16 );
			byteBL = (byte) ( bcd % 16 );
			result.append( byteBH );
			result.append( byteBL );
		}
		return result.toString();
	}

	/**
	 * Formata os retorna enviado pela impressora <BR>
	 * separando o STATUS do estado da impressora dos dados do result, onde <BR>
	 * ACK (06) - byte indicativo de recebimento correto. <BR>
	 * ST1 2 ST2 - bytes de estado da impressora. <BR>
	 * NAK (15h ou 21d) - byte indicativo de recebimento incorreto. <BR>
	 * <BR>
	 * O result tem a seguinte sintaxe : <BR>
	 * [ACK][result solicitado][ST1][ST2] <BR>
	 * 
	 * @param bytes
	 *            bytes retornados pela porta serial.<BR>
	 * @return result indece para a mensagem.
	 * @see org.freedom.ecf.driver.AbstractECFDriver#checkResult(byte[])
	 */
	public STResult checkResult( final byte[] bytes ) {

		STResult result = new STResult();
		byte ack = 0;
		byte st1 = 0;
		byte st2 = 0;
		if ( bytes != null ) {
			ack = bytes[ 0 ];
			if ( bytes.length > 3 ) {
				st1 = bytes[ bytes.length - 2 ];
				st2 = bytes[ bytes.length - 1 ];
				final byte[] bytesLidos = new byte[ bytes.length - 3 ];
				System.arraycopy( bytes, 1, bytesLidos, 0, bytesLidos.length );
				setBytesLidos( bytesLidos );
			}
			if ( ack == ACK && st1 == 0 && st2 == 0 ) {
				result = STResult.getInstanceOk();
			}
			else {
				result.addAll( checkST1( st1 ) );
				result.addAll( checkST2( st2 ) );
			}
		}
		else {
			result = STResult.getInstanceNotComunication();
		}
		return result;
	}

	/**
	 * Auxilia o metodo checkResult.<BR>
	 * 
	 * @param ST1
	 * @return result checado
	 */
	private STResult checkST1( final byte ST1 ) {

		int st1 = ST1;
		STResult result = new STResult();
		// compatibilização do valor de byte de result.
		if ( st1 < 0 ) {
			st1 += 128;
		}
		if ( st1 > 127 ) {
			st1 -= 128;
			result.add( StatusBematech.BEMA_FIM_DE_PAPEL );
		}
		if ( st1 > 63 ) {
			st1 -= 64;
			result.add( StatusBematech.BEMA_POUCO_PAPEL );
		}
		if ( st1 > 31 ) {
			st1 -= 32;
			result.add( StatusBematech.BEMA_RELOGIO_ERROR );
		}
		if ( st1 > 15 ) {
			st1 -= 16;
			result.add( StatusBematech.BEMA_IMPRESSORA_EM_ERRO );
		}
		if ( st1 > 7 ) {
			st1 -= 8;
			result.add( StatusBematech.BEMA_NO_ESC );
		}
		if ( st1 > 3 ) {
			st1 -= 4;
			result.add( StatusBematech.BEMA_NO_COMMAND );
		}
		if ( st1 > 1 ) {
			st1 -= 2;
			result.add( StatusBematech.BEMA_CUPOM_FISCAL_ABERTO );
		}
		if ( st1 > 0 ) {
			st1 -= 1;
			result.add( StatusBematech.BEMA_NU_PARAMS_INVALIDO );
		}
		return result;
	}

	/**
	 * Auxilia o metodo checkResult.<BR>
	 * 
	 * @param ST2
	 * @return result checado
	 */
	private STResult checkST2( final byte ST2 ) {

		int st2 = ST2;
		STResult result = new STResult();
		// compatibilização do valor de byte de result.
		if ( st2 < 0 ) {
			st2 += 128;
		}
		if ( st2 > 127 ) {
			st2 -= 128;
			result.add( StatusBematech.BEMA_TP_PARAM_INVALIDO );
		}
		if ( st2 > 63 ) {
			st2 -= 64;
			result.add( StatusBematech.BEMA_OUT_OF_MEMORY );
		}
		if ( st2 > 31 ) {
			st2 -= 32;
			result.add( StatusBematech.BEMA_MEMORY_ERROR );
		}
		if ( st2 > 15 ) {
			st2 -= 16;
			result.add( StatusBematech.BEMA_NO_ALIQUOTA );
		}
		if ( st2 > 7 ) {
			st2 -= 8;
			result.add( StatusBematech.BEMA_OUT_OF_ALIQUOTA );
		}
		if ( st2 > 3 ) {
			st2 -= 4;
			result.add( StatusBematech.BEMA_NO_ACESESS_CANCELAMENTO );
		}
		if ( st2 > 1 ) {
			st2 -= 2;
			result.add( StatusBematech.BEMA_NO_CNPJ_IE );
		}
		if ( st2 > 0 ) {
			st2 -= 1;
			result.add( StatusBematech.BEMA_COMMAND_NO_EXECUTE );
		}
		return result;
	}

	/**
	 * Através do comando 01, pode-se alterar o símbolo da moeda usando como tamanho de parâmetro<br>
	 * dois caracteres ASCII alfanuméricos. Ex: “" R" (um espaço em branco e a letra R maiúscula).<br>
	 * O símbolo monetário “$” já está programado, sendo assim, não precisa ser inserido.<br>
	 * 
	 * @param simbolo
	 *            simboleo da moeda.<br>
	 * @return estado da impressora.<br>
	 */
	public STResult alteraSimboloMoeda( final String simbolo ) {

		byte[] CMD = { ESC, 1 };
		final int tamanho = 2;
		String tmp = simbolo.trim();
		if ( tamanho < tmp.length() ) {
			tmp = tmp.substring( 0, tamanho );
		}
		else {
			tmp = replicate( " ", tamanho - tmp.length() );
			tmp += simbolo;
		}
		CMD = adicBytes( CMD, tmp.getBytes() );
		return executaCmd( CMD, 3 );
	}

	/**
	 * Poderá ser adicionado até 16 alíquotas tributárias.<br>
	 * Sempre verifique se já exite alíquotas programadas, pois<br>
	 * não é permitido alterar as alíquotas que já existem, nem removê-las, apenas adicioná-las.<br>
	 * 
	 * @param aliq
	 *            percentual da alíquota.<br>
	 * @param opt
	 *            indica opção da alíquota, se é ICMS OU ISS.<br>
	 * @see org.freedom.ecf.driver.AbstractECFDriver#ISS
	 * @see org.freedom.ecf.driver.AbstractECFDriver#ICMS
	 * @return estado da impressora.<br>
	 */
	public STResult adicaoDeAliquotaTriburaria( final String aliq, final char opt ) {

		byte[] CMD = { ESC, 7 };
		final StringBuffer buf = new StringBuffer();
		buf.append( parseParam( aliq, 4, false ) );
		if ( ISS == opt ) {
			buf.append( opt );
		}
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		return executaCmd( CMD, 3 );
	}

	/**
	 * Este comando deve ser utilizado após uma Redução Z.<br>
	 * Para entrar no Horário de Verão, simplesmente envie este comando à impressora.<br>
	 * Para sair d o Horário de Verão, você deverá esperar pelo menos 1 hora e em seguida enviar o comando.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult programaHorarioVerao() {

		final byte[] CMD = { ESC, 18 };
		return executaCmd( CMD, 3 );
	}

	public boolean isHorarioVerao() {

		boolean returnOfAction = false;
		String flags = resultVariaveis( AbstractECFDriver.V_FLAG_FISCAL );
		int iflag = Integer.parseInt( flags );
		if ( iflag > 127 ) {
			iflag -= 128;
		}
		if ( iflag > 63 ) {
			iflag -= 64;
		}
		if ( iflag > 31 ) {
			iflag -= 32;
		}
		if ( iflag > 15 ) {
			iflag -= 16;
		}
		if ( iflag > 7 ) {
			iflag -= 8;
		}
		if ( iflag > 3 ) {
			iflag -= 4;
			returnOfAction = true;
		}
		return returnOfAction;
	}

	/**
	 * Você poderá nomear até 50 (01 até 50) totalizadores não sujeitos para recebimentos,<br>
	 * como no exemplo abaixo, está sendo nomeado ao primeiro totalizador (01) a "Conta de Luz".<br>
	 * Estes totalizadores devem ser nomeados somente após uma Redução Z.<br>
	 * 
	 * @param indice
	 *            indica a posição do totalizador.<br>
	 * @param desc
	 *            descrição do totalizador.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult nomeiaTotalizadorNaoSujeitoICMS( final int indice, final String desc ) {

		byte[] CMD = { ESC, 40 };
		final StringBuffer buf = new StringBuffer();
		buf.append( parseParam( indice, 2 ) );
		buf.append( parseParam( desc, 19, false ) );
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		return executaCmd( CMD, 3 );
	}

	/**
	 * Este comando define o tratamento de casas decimais.<br>
	 * Como default, o formato é truncar. Caso queira arredondamento,<br>
	 * utilize como parâmetro um número ímpar.<br>
	 * 
	 * Exemplo:<br>
	 * <br>
	 * <p style="background=#eeffee">
	 * // para definir Arredondamento.<br>
	 * programaTruncamentoArredondamento( '1' );<br>
	 * </p>
	 * <br>
	 * 
	 * @param opt
	 *            definição de truncamento/arredondamento.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult programaTruncamentoArredondamento( final char opt ) {

		byte[] CMD = { ESC, 39 };
		CMD = adicBytes( CMD, parseParam( opt, 1 ).getBytes() );
		return executaCmd( CMD, 3 );
	}

	/**
	 * Você poderá programar espaços entre as linhas do Cupom (em dots) através deste comando.<br>
	 * Uma linha é igual a 8 dots.<br>
	 * Este comando só será executado caso não tenha havido movimentação no dia, ou após a Redução Z.<br>
	 * Este comando só está disponível para a impressora MP-40 FI II.<br>
	 * 
	 * @param espaco
	 *            dots de espaçamento.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult programarEspacoEntreLinhas( final int espaco ) {

		byte[] CMD = { ESC, 60 };
		CMD = adicBytes( CMD, parseParam( (char) espaco ).getBytes() );
		return executaCmd( CMD, 3 );
	}

	/**
	 * Você poderá programar o número de linhas entre os cupons.<br>
	 * Este comando deve ser executado no início das operações,<br>
	 * sendo que possibilita a impressão de um Relatório Gerêncial ou de um Comprovante Não Fiscal,<br>
	 * logo após a impressão do Cupom Fiscal, sem espaços em branco.<br>
	 * 
	 * @param espaco
	 *            número de linhas.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult programarLinhasEntreCupons( final int espaco ) {

		byte[] CMD = { ESC, 61 };
		CMD = adicBytes( CMD, parseParam( (char) espaco ).getBytes() );
		return executaCmd( CMD, 3 );
	}

	/**
	 * O Departamento só será nomeado, caso não tenha havido movimentação no dia ou logo após uma Redução Z.<br>
	 * Você poderá nomear até 20 Departamentos.<br>
	 * O índice 01 é “Geral” e já vem programado na impressora. O tamanho do parâmetro é de 10 bytes.<br>
	 * Este Departamento tem por finalidade armazenar, no dia, a quantidade e o valor de uma<br>
	 * determinada venda, exemplo: departamento Vestuário (tudo que foi vendido de calças, camisas,<br>
	 * blusas e etc), departamento Gasolina (tudo que foi vendido em gasolina) e etc.<br>
	 * 
	 * @param index
	 *            indicador do departamento.<br>
	 * @param descricao
	 *            descrição do departamento.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult nomeiaDepartamento( final int index, final String descricao ) {

		byte[] CMD = { ESC, 65 };
		final StringBuffer buf = new StringBuffer();
		buf.append( parseParam( index, 2 ) );
		buf.append( parseParam( descricao, 20, false ) );
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		return executaCmd( CMD, 3 );
	}

	/**
	 * Abertura de Cupom Fiscal. <br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult aberturaDeCupom() {

		final byte[] CMD = { ESC, 0 };
		return executaCmd( CMD, 3 );
	}

	/**
	 * Abertura de Cupom Fiscal, informando o CNPJ/CPF do cliente.<br>
	 * 
	 * @param cnpj
	 *            CNPJ/CPF do cliente.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult aberturaDeCupom( final String cnpj ) {

		byte[] CMD = { ESC, 0 };
		CMD = adicBytes( CMD, parseParam( cnpj, 29, false ).getBytes() );
		return executaCmd( CMD, 3 );
	}

	/**
	 * Programa na memória da impressora a unidade de medida que deseja usar no próximo comando<br>
	 * de Venda de Item. Este comando tem validade somente para a impressão de um Item,<br>
	 * voltando ao default que é sem a unidade de medida.<br>
	 * É necessário programá-la, novamente, caso deseje usá-la para a próxima venda.<br>
	 * No exemplo abaixo, está sendo programada a unidade Kg.<br>
	 * 
	 * Exemplo:<br>
	 * <br>
	 * <p style="background=#000000">
	 * // para definir a unidade de medida para o próximo item.<br>
	 * programaUnidadeMedida( "Kg" );// Kilograma<br>
	 * </p>
	 * <br>
	 * 
	 * @param descUnid
	 *            unidade de medida.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult programaUnidadeMedida( final String descUnid ) {

		byte[] CMD = { ESC, 62, 51 };
		CMD = adicBytes( CMD, parseParam( descUnid, 2, false ).getBytes() );
		return executaCmd( CMD, 3 );
	}

	/**
	 * O próximo comando de Venda de Item imprimirá a Descrição com este tamanho.<br>
	 * Este comando tem validade somente para a impressão de um Item,<br>
	 * voltando ao padrão que é de 29 caracteres.<br>
	 * 
	 * @param descricao
	 *            descrição do item.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult aumentaDescItem( final String descricao ) {

		byte[] CMD = { ESC, 62, 52 };
		CMD = adicBytes( CMD, parseParam( descricao, 200 ).getBytes() );
		return executaCmd( CMD, 3 );
	}

	/**
	 * Este comando executa a venda de item utilizando valor com duas casas decimais.<br>
	 * Para execução deste comando obrigatoriamente o cupom deve estar aberto.<br>
	 * 
	 * @param codProd
	 *            código do produto.<br>
	 * @param descProd
	 *            descrição do produto.<br>
	 * @param aliquota
	 *            aliquota do item.<br>
	 * @param tpqtd
	 *            tipo de quantidade, inteiro ou decimal.<br>
	 * @param qtd
	 *            quantidade do item.<br>
	 * @param valor
	 *            valor do item.<br>
	 * @param tpdesc
	 *            tipo do desconto, percentual ou valor.<br>
	 * @param desconto
	 *            valor do desconto.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult vendaItem( final String codProd, final String descProd, final String aliquota, final char tpqtd, final float qtd, final float valor, final char tpdesc, final float desconto ) {

		byte[] CMD = { ESC, 9 };
		final StringBuffer buf = new StringBuffer();
		buf.append( parseParam( codProd, 13 ) );
		buf.append( parseParam( descProd, 29 ) );
		buf.append( parseParam( aliquota, 2 ) );
		if ( tpqtd == QTD_DECIMAL ) {
			buf.append( parseParam( qtd, 7, 3 ) );
		}
		else if ( tpqtd == QTD_INTEIRO ) {
			buf.append( parseParam( qtd, 4, 0 ) );
		}
		buf.append( parseParam( valor, 8, 2 ) );
		if ( tpdesc == DESCONTO_VALOR ) {
			buf.append( parseParam( desconto, 8, 2 ) );
		}
		else if ( tpdesc == DESCONTO_PERC ) {
			buf.append( parseParam( desconto, 4, 0 ) );
		}
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		return executaCmd( CMD, 3 );
	}

	/**
	 * Este comando executa a venda de item utilizando valor com três casas decimais.<br>
	 * Para execução deste comando obrigatoriamente o cupom deve estar aberto.<br>
	 * 
	 * @param codProd
	 *            código do produto.<br>
	 * @param descProd
	 *            descrição do produto.<br>
	 * @param aliquota
	 *            aliquota do item.<br>
	 * @param tpqtd
	 *            tipo de quantidade, inteiro ou decimal.<br>
	 * @param qtd
	 *            quantidade do item.<br>
	 * @param valor
	 *            valor do item.<br>
	 * @param tpdesc
	 *            tipo do desconto, percentual ou valor.<br>
	 * @param desconto
	 *            valor do desconto.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult vendaItemTresCasas( final String codProd, final String descProd, final String aliquota, final char tpqtd, final float qtd, final float valor, final char tpdesc, final float desconto ) {

		byte[] CMD = { ESC, 56 };
		final StringBuffer buf = new StringBuffer();
		buf.append( parseParam( codProd, 13 ) );
		buf.append( parseParam( descProd, 29 ) );
		buf.append( parseParam( aliquota, 2 ) );
		if ( tpqtd == QTD_DECIMAL ) {
			buf.append( parseParam( qtd, 7, 3 ) );
		}
		else if ( tpqtd == QTD_INTEIRO ) {
			buf.append( parseParam( qtd, 4, 0 ) );
		}
		buf.append( parseParam( valor, 8, 3 ) );
		if ( tpdesc == DESCONTO_VALOR ) {
			buf.append( parseParam( desconto, 8, 2 ) );
		}
		else if ( tpdesc == DESCONTO_PERC ) {
			buf.append( parseParam( desconto, 4, 0 ) );
		}
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		return executaCmd( CMD, 3 );
	}

	/**
	 * Este comando executa a venda de item utilizando valor com três casas decimais.<br>
	 * Para execução deste comando obrigatoriamente o cupom deve estar aberto.<br>
	 * 
	 * @param sitTrib
	 *            situação tributária do item.<br>
	 * @param valor
	 *            valor do item.<br>
	 * @param qtd
	 *            quantidade do item.<br>
	 * @param desconto
	 *            valor do desconto no item<br>
	 * @param acrescimo
	 *            valor de acrécimo no item<br>
	 * @param departamento
	 *            indice do departamento<br>
	 * @param unidade
	 *            unidade de medida do item<br>
	 * @param codProd
	 *            código do produto.<br>
	 * @param descProd
	 *            descrição do produto.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult vendaItemDepartamento( final String sitTrib, final float valor, final float qtd, final float desconto, final float acrescimo, final int departamento, final String unidade, final String codProd, final String descProd ) {

		byte[] CMD = { ESC, 63 };
		final StringBuffer buf = new StringBuffer( 312 );
		buf.append( parseParam( sitTrib, 2, false ) );
		buf.append( parseParam( valor, 9, 3 ) );
		buf.append( parseParam( qtd, 7, 3 ) );
		buf.append( parseParam( desconto, 10, 2 ) );
		buf.append( parseParam( acrescimo, 10, 2 ) );
		buf.append( parseParam( departamento, 2 ) );
		buf.append( "00000000000000000000" );
		buf.append( parseParam( unidade, 2, false ) );
		buf.append( parseParam( codProd + (char) 0, 49, false ) );
		buf.append( parseParam( descProd + (char) 0, 200, false ) );
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		return executaCmd( CMD, 3 );
	}

	/**
	 * Este comando executa o cancelamento da venda do ultimo item.<br>
	 * O item neste caso só poderá ser cancelado após a sua venda.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult cancelaItemAnterior() {

		final byte[] CMD = { ESC, 13 };
		return executaCmd( CMD, 3 );
	}

	/**
	 * Este comando executa o cancelamento da venda do item<br>
	 * indicado pelo indice passado por parametro.<br>
	 * O item neste caso só poderá ser cancelado após a sua venda.<br>
	 * 
	 * @param item
	 *            indice do item a ser cancelado.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult cancelaItemGenerico( final int item ) {

		byte[] CMD = { ESC, 31 };
		CMD = adicBytes( CMD, parseParam( item, 4 ).getBytes() );
		return executaCmd( CMD, 3 );
	}

	/**
	 * Através deste comando, é dado o início ao fechamento do cupom.<br>
	 * A impressora imprimirá o TOTAL das vendas. Os parâmetros que estão sendo passados<br>
	 * por este exemplo são: opção de Desconto ( "D" ) ou de Acrécimo ( "A" ) e o Percentual.<br>
	 * 
	 * @param opt
	 *            opção de Desconto ou Acrécimo.<br>
	 * @param valor
	 *            Percentual.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult iniciaFechamentoCupom( final char opt, final float valor ) {

		byte[] CMD = { ESC, 32 };
		int tamanho = 14;
		if ( opt == ACRECIMO_PERC || opt == DESCONTO_PERC ) {
			tamanho = 4;
		}
		final StringBuffer buf = new StringBuffer();
		buf.append( parseParam( opt ) );
		buf.append( parseParam( valor, tamanho, 2 ) );
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		return executaCmd( CMD, 3 );
	}

	/**
	 * Através deste comando, é informado a Forma de Pagamento<br>
	 * que o cliente usou para o pagamento da conta.<br>
	 * Caso a Forma de Pagamento exceda o valor total do Cupom,<br>
	 * não serão mais permitidas novas formas.<br>
	 * 
	 * @param indice
	 *            indice da forma de pagamento.<br>
	 * @param valor
	 *            valor.<br>
	 * @param descForma
	 *            descrição complemetar para a forma de pagamento.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult efetuaFormaPagamento( final String indice, final float valor, final String descForma ) {

		byte[] CMD = { ESC, 72 };
		final StringBuffer buf = new StringBuffer();
		buf.append( parseParam( indice, 2 ) );
		buf.append( parseParam( valor, 14, 2 ) );
		buf.append( parseParam( descForma, 80, true ) );
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		return executaCmd( CMD, 3 );
	}

	/**
	 * Este comando fecha o Cupom, passando como parâmetro a mensagem promocional.<br>
	 * Esta mensagem possui um tamanho máximo de 492 caracteres, sendo limitada em até 8 linhas.<br>
	 * Se não houver nenhum item vendido, não será permitido o fechamento do Cupom.<br>
	 * 
	 * @param mensagem
	 *            menssagem promocional de final de cupom.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult finalizaFechamentoCupom( final String mensagem ) {

		byte[] CMD = { ESC, 34, ESC };
		CMD = adicBytes( CMD, parseParam( mensagem, 492, true ).getBytes() );
		return executaCmd( CMD, 3 );
	}

	/**
	 * Este comando estará habilitado para sua execução, em qualquer parte do cupom,<br>
	 * desde que haja pelo menos um item vendido.<br>
	 * Só é permitido o cancelamento do último cupom fiscal impresso.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult cancelaCupom() {

		final byte[] CMD = { ESC, 14 };
		return executaCmd( CMD, 3 );
	}

	/**
	 * São permitidos até 49 formas de pagamento, no tamanho de 16 caracteres, sendo que a 01 será<br>
	 * sempre “Dinheiro” (Default). Este comando poderá ser executado a qualquer hora do dia, retornando<br>
	 * pela porta serial o índice da forma programada.<br>
	 * Após a sua totalização na Redução Z todas as formas<br>
	 * de pagamento programadas serão apagadas da impressora, permanecendo somente a forma 01<br>
	 * (Dinheiro). É programado, apenas, uma forma por vez.<br>
	 * 
	 * @param descricao
	 *            descrição da forma de pagamento.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public String programaFormaPagamento( final String descricao ) {

		byte[] CMD = { ESC, 71 };
		CMD = adicBytes( CMD, parseParam( descricao, 16 ).getBytes() );
		executaCmd( CMD, 4 );
		return new String( getBytesLidos() );
	}

	/**
	 * Este comando permite estornar valores de uma Forma de Pagamento e inserir em<br>
	 * outra Forma de Pagamento.<br>
	 * Observações: O valor a ser estornado não pode exceder o total da Forma de Pagamento de<br>
	 * Origem. Este comando só será executado se o Cupom Fiscal estiver fechado.<br>
	 * 
	 * @param descOrigem
	 *            descrição da forma de pagamento de origem.<br>
	 * @param descDestino
	 *            descrição da forma de pagamento de destino.<br>
	 * @param valor
	 *            valor a estornar.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult estornoFormaPagamento( final String descOrigem, final String descDestino, final float valor ) {

		byte[] CMD = { ESC, 74 };
		final StringBuffer buf = new StringBuffer();
		buf.append( parseParam( descOrigem, 16, false ) );
		buf.append( parseParam( descDestino, 16, false ) );
		buf.append( parseParam( valor, 14, 2 ) );
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		return executaCmd( CMD, 3 );
	}

	/**
	 * Executa a Redução Z para a data atual da impressora fiscal.<BR>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult reducaoZ() {

		final byte[] CMD = { ESC, 5 };
		return executaCmd( CMD, 3 );
	}

	/**
	 * Executa a Leitura X na impressora fiscal.<BR>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult leituraX() {

		final byte[] CMD = { ESC, 6 };
		return executaCmd( CMD, 3 );
	}

	/**
	 * Executa a leitura da memoria fiscal entre datas, e pode ser impresso ou capturado na porta serial.<br>
	 * 
	 * @param dataIni
	 *            data de inicio.<br>
	 * @param dataFim
	 *            data limite.<br>
	 * @param tipo
	 *            I para impressão e R para porta serial.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult leituraMemoriaFiscal( final Date dataIni, final Date dataFim, final char tipo ) {

		byte[] CMD = { ESC, 8 };
		final StringBuffer buf = new StringBuffer();
		buf.append( parseParam( dataIni ) );
		buf.append( parseParam( dataFim ) );
		buf.append( tipo );
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		return executaCmd( CMD, 3 );
	}

	/**
	 * Executa a leitura da memoria fiscal entre reduções, e pode ser impresso ou capturado na porta serial.<br>
	 * 
	 * @param ini
	 *            data de inicio.<br>
	 * @param fim
	 *            data limite.<br>
	 * @param tipo
	 *            I para impressão e R para porta serial.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult leituraMemoriaFiscal( final int ini, final int fim, final char tipo ) {

		byte[] CMD = { ESC, 8 };
		final StringBuffer buf = new StringBuffer();
		buf.append( parseParam( ini, 6 ) );
		buf.append( parseParam( fim, 6 ) );
		buf.append( tipo );
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		return executaCmd( CMD, 3 );
	}

	/**
	 * Através deste comando, você obtem a Leitura X pela porta Serial.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult leituraXSerial() {

		final byte[] CMD = { ESC, 69 };
		return executaCmd( CMD, 3 );
	}

	/**
	 * Neste relatório é permitido a impressão de um texto qualquer, com no máximo 620 caracteres<br>
	 * que poderá ser enviado várias vezes. Com a execução deste comando, a impressora imprimirá, antes,<br>
	 * uma Leitura “X”. Este relatório está limitado a 10 (dez) minutos de duração. Se não for enviando o<br>
	 * comando para fechar este relatório, após 10 minutos, a impressora fechará automaticamente.<br>
	 * 
	 * @param texto
	 *            texto a ser impresso no relatório.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult relatorioGerencial( final String texto ) {

		byte[] CMD = { ESC, 20 };
		CMD = adicBytes( CMD, parseParam( texto, 620, true ).getBytes() );
		return executaCmd( CMD, 3 );
	}

	/**
	 * Este comando poderá ser usado para Fechar o Relatório Gerêncial e também o Comprovante<br>
	 * Não Fiscal Vinculado.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult fechamentoRelatorioGerencial() {

		final byte[] CMD = { ESC, 21 };
		return executaCmd( CMD, 3 );
	}

	/**
	 * Este comprovante é usado em casos de Suprimento (entrada de dinheiro em caixa, usado<br>
	 * normalmente no início do dia - Abertura de Caixa), Sangrias (retira de dinheiro do caixa) ou para<br>
	 * Recebimentos não sujeitos ao ICMS.<br>
	 * 
	 * Obs.: No caso de recebimentos não sujeitos a ICMS o totalizador deve estar previamente programado.<br>
	 * 
	 * @param opt
	 *            "SA" para sangria,<br>
	 *            "SU" para suprimento ou<br>
	 *            o indice o totalizador não sujeito a ICMS.<br>
	 * @param valor
	 *            valor do comprovante.<br>
	 * @param formaPag
	 *            forma de pagamento.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult comprovanteNFiscalNVinculado( final String opt, final float valor, final String formaPag ) {

		byte[] CMD = { ESC, 25 };
		final StringBuffer buf = new StringBuffer();
		buf.append( parseParam( opt, 2 ) );
		buf.append( parseParam( valor, 14, 2 ) );
		buf.append( parseParam( formaPag, 16 ) );
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		return executaCmd( CMD, 3 );
	}

	/**
	 * Só será executado logo após um Cupom Fiscal ou de um Comprovante Não Fiscal Não Vinculado<br>
	 * (Recebimentos), além disto, a Forma de Pagamento deve ter sido utilizada no último Cupom. Este<br>
	 * Comprovante é usado para a impressão do TEF (Transferência Eletrônica de Fundo) e também para<br>
	 * compras à prazo.<br>
	 * É possivél também vincular o comprovante não fiscal para um cupom já impresso através do COO<br>
	 * (Contado de Ordem de Operação).<br>
	 * 
	 * Obs.: Não utilizado para a forma de pagamento "Dinheiro".<br>
	 * 
	 * @param formaPag
	 *            forma de pagamento.<br>
	 * @param valor
	 *            valor do comprovante.<br>
	 * @param doc
	 *            número de COO.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult abreComprovanteNFiscalVinculado( final String formaPag, final float valor, final int doc ) {

		byte[] CMD = { ESC, 66 };
		final StringBuffer buf = new StringBuffer();
		buf.append( parseParam( formaPag, 16, false ) );
		buf.append( parseParam( valor, 14, 2 ) );
		buf.append( parseParam( doc, 6 ) );
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		return executaCmd( CMD, 3 );
	}

	/**
	 * Este Comprovante pode ser usado para descrever melhor a Forma de Pagamento passado no<br>
	 * Cupom Fiscal, pode ser passado até 620 caracteres. Este Comprovante possui 2 minutos de impressão,<br>
	 * sendo que o comando poderá ser enviado várias vezes dentro deste tempo. Após estes 2 minutos o<br>
	 * Comprovante fechará automaticamente, caso contrário deverá ser enviado o comando de Fechamento<br>
	 * do Relatório Gerêncial.<br>
	 * 
	 * @param texto
	 *            texto a ser impresso.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult usaComprovanteNFiscalVinculado( final String texto ) {

		byte[] CMD = { ESC, 67 };
		CMD = adicBytes( CMD, parseParam( texto, 620, false ).getBytes() );
		return executaCmd( CMD, 3 );
	}

	/**
	 * Este comando deverá ser executado após um Recebimento Não Sujeito ao ICMS ou ao término<br>
	 * de um Cupom Fiscal. A impressora irá aguardar 5 (cinco) segundos para que seja inserido o documento,<br>
	 * caso contrário, a impressora retornará ao estado normal de operação, indicando o “status” de<br>
	 * “comando não executado.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult autenticacaoDeDocumento() {

		final byte[] CMD = { ESC, 16 };
		return executaCmd( CMD, 3 );
	}

	/**
	 * Você poderá criar um caracter para a autenticação de seus documentos através deste comando.<br>
	 * Cada byte é uma coluna, onde o bit menos significativo corresponde à agulha mais alta da<br>
	 * cabeça de impressão. Será impresso: AUT: logo, data, loja, ECF, COO e o valor.<br>
	 * 
	 * @param caracteres
	 *            caracteres para formatação da logo.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult programaCaracterParaAutenticacao( final int[] caracteres ) {

		byte[] CMD = { ESC, 64 };
		byte[] bytes = new byte[ caracteres.length ];
		for ( int i = 0; i < caracteres.length; i++ ) {
			bytes[ i ] = (byte) ( caracteres[ i ] - 128 );
		}
		CMD = adicBytes( CMD, bytes );
		return executaCmd( CMD, 3 );
	}

	/**
	 * Este comando executa a abertura da gaveta de dinheiro, no tempo(em milisegundos)<br>
	 * passado pro parametro.<br>
	 * 
	 * @param time
	 *            tempo para abertura da gaveta.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult acionaGavetaDinheiro( final int time ) {

		final byte[] CMD = { ESC, 22, (byte) time };
		return executaCmd( CMD, 3 );
	}

	/**
	 * Verifica o estado da Gaveta atual, se a mesma está aberta ou fechada. Neste caso,<br>
	 * deverá ler a porta de comunicação da impressora.<br>
	 * 
	 * @return estado da gaveta de dinheiro.<br>
	 *         00 - Sensor em Nível Zero.<br>
	 *         FF - Sensor em Nível Um.<br>
	 */
	public String resultEstadoGavetaDinheiro() {

		final byte[] CMD = { ESC, 23 };
		executaCmd( CMD, 4 );
		return bcdToAsc( getBytesLidos() );
	}

	/**
	 * Através deste comando é possível verificar o estado da impressora atual.<br>
	 * 
	 * @see org.freedom.ecf.driver.EStatus
	 * @return estado da impressora.<br>
	 */
	public String getStatus() {

		final byte[] CMD = { ESC, 19 };
		STResult stresult = executaCmd( CMD, 3 );
		return stresult.getMessages();
	}

	/**
	 * Retorna as alíquotas programadas, atualmente, na Impressora.<br>
	 * 
	 * @return string concatenda com as aliquotas programadas(cada aliquota tem quatro digitos)<br>
	 */
	public String resultAliquotas() {

		final byte[] CMD = { ESC, 26 };
		executaCmd( CMD, 36 );
		final String aliquotas = bcdToAsc( getBytesLidos() );
		return aliquotas.substring( aliquotas.length() - 64 );
	}

	/**
	 * Através deste comando são retornados, via serial:<br>
	 * <br>
	 * Totalizadores Parciais Tributados : 112 bytes<br>
	 * Isenção : 7 bytes<br>
	 * Não Incidência : 7 bytes<br>
	 * Substituição : 7 bytes<br>
	 * Totalizadores Não Sujeitos ao ICMS : 63 bytes<br>
	 * Sangria : 7 bytes<br>
	 * Suprimentos : 7 bytes<br>
	 * Grande Total : 9 bytes<br>
	 * <br>
	 * 
	 * @return totalizadores parciais<br>
	 */
	public String resultTotalizadoresParciais() {

		final byte[] CMD = { ESC, 27 };
		executaCmd( CMD, 222 );
		final int[] tam = { 224, 14, 14, 14, 126, 14, 14, 18 };
		final String totalizadores = bcdToAsc( getBytesLidos() );
		final StringBuffer result = new StringBuffer();
		int index = 0;
		for ( int i = 0; i < tam.length; i++ ) {
			if ( i > 0 ) {
				result.append( ',' );
			}
			result.append( totalizadores.substring( index, ( index + tam[ i ] ) ) );
			index += tam[ i ];
		}
		return result.toString();
	}

	/**
	 * Retorna o subtotal do cupom do último cupom.<br>
	 * 
	 * @return subtotal<br>
	 */
	public String resultSubTotal() {

		final byte[] CMD = { ESC, 29 };
		executaCmd( CMD, 10 );
		return bcdToAsc( getBytesLidos() );
	}

	/**
	 * Este comando poderá ser utilizado logo após a abertura de um Cupom Fiscal, assim recebendo<br>
	 * o seu número, ou após qualquer Cupom fechado.<br>
	 * 
	 * @return número do cupom<br>
	 */
	public String resultNumeroCupom() {

		final byte[] CMD = { ESC, 30 };
		executaCmd( CMD, 6 );
		return bcdToAsc( getBytesLidos() );
	}

	public boolean resultDocumentoAberto() {

		String status = getStatus();
		return status.indexOf( StatusBematech.BEMA_CUPOM_FISCAL_ABERTO.getMessage() ) > -1;
	}

	/**
	 * Retorna a imformação da impressora comforme o parametro especificado.<br>
	 * 
	 * <table border=1>
	 * <tr>
	 * <td><b>paramtro<b></td>
	 * <td><b>variável<b></td>
	 * <td><b>tamanho<b></td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>número de serie</td>
	 * <td>15 caracteres ASCII</td>
	 * </tr>
	 * <tr>
	 * <td>1</td>
	 * <td>Versão do FIRMWARE</td>
	 * <td>2 bytes</td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>CNPJ/IE</td>
	 * <td>33 caracteres ASCII</td>
	 * </tr>
	 * <tr>
	 * <td>3</td>
	 * <td>Grande Total</td>
	 * <td>9 bytes</td>
	 * </tr>
	 * <tr>
	 * <td>4</td>
	 * <td>Cancelamentos</td>
	 * <td>7 bytes</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>Descontos</td>
	 * <td>7 bytes</td>
	 * </tr>
	 * <tr>
	 * <td>6</td>
	 * <td>Contador Seqüêncial</td>
	 * <td>3 bytes</td>
	 * </tr>
	 * <tr>
	 * <td>7</td>
	 * <td>Número de Operações Não Fiscais</td>
	 * <td>3 bytes</td>
	 * <tr>
	 * <tr>
	 * <td>8</td>
	 * <td>Número de Cupons Cancelados</td>
	 * <td>2 bytes</td>
	 * </tr>
	 * <tr>
	 * <td>9</td>
	 * <td>Número de Reduções</td>
	 * <td>2 bytes</td>
	 * </tr>
	 * <tr>
	 * <td>10</td>
	 * <td>Número de Intervenções Técnicas</td>
	 * <td>2 bytes</td>
	 * </tr>
	 * <tr>
	 * <td>11</td>
	 * <td>Número de Intervenções Técnicas</td>
	 * <td>2 bytes</td>
	 * </tr>
	 * <tr>
	 * <td>12</td>
	 * <td>Número do Último Item Vendido</td>
	 * <td>2 bytes</td>
	 * </tr>
	 * <tr>
	 * <td>13</td>
	 * <td>Cliche do Proprietário</td>
	 * <td>186 caracteres ASCII</td>
	 * </tr>
	 * <tr>
	 * <td>14</td>
	 * <td>Número do Caixa</td>
	 * <td>2 bytes</td>
	 * </tr>
	 * <tr>
	 * <td>15</td>
	 * <td>Nümero da Loja</td>
	 * <td>2 bytes</td>
	 * </tr>
	 * <tr>
	 * <td>16</td>
	 * <td>Moeda</td>
	 * <td>2 caracteres ASCII</td>
	 * </tr>
	 * <tr>
	 * <td>17</td>
	 * <td>FLAGS FISCAIS</td>
	 * <td>1 byte</td>
	 * </tr>
	 * <tr>
	 * <td>18</td>
	 * <td>Minutos Ligada</td>
	 * <td>2 bytes</td>
	 * </tr>
	 * <tr>
	 * <td>19</td>
	 * <td>Minutos Imprimindo</td>
	 * <td>2 bytes</td>
	 * </tr>
	 * <tr>
	 * <td>20</td>
	 * <td>FLAG de Intervenção Técnica</td>
	 * <td>1 byte</td>
	 * </tr>
	 * <tr>
	 * <td>21</td>
	 * <td>FLAG de EPROM Conectada</td>
	 * <td>1 byte</td>
	 * </tr>
	 * <tr>
	 * <td>22</td>
	 * <td>Valor Pago no Último Cupom</td>
	 * <td>7 bytes</td>
	 * </tr>
	 * <tr>
	 * <td>23</td>
	 * <td>Data e Hora Atual(DDMMAAHHMMSS)</td>
	 * <td>6 bytes</td>
	 * </tr>
	 * <tr>
	 * <td>24</td>
	 * <td>Contadores dos Totalizadores Não Sujeitos ao ICMS</td>
	 * <td>9 bytes</td>
	 * </tr>
	 * <tr>
	 * <td>25</td>
	 * <td>Descrição dos Totalizadores Nao Sujeitos ao ICMS</td>
	 * <td>9 totalizadores com 19 caracteres</td>
	 * </tr>
	 * <tr>
	 * <td>26</td>
	 * <td>Data da Última Redução (DDMMAA)</td>
	 * <td>3 bytes</td>
	 * </tr>
	 * <tr>
	 * <td>27</td>
	 * <td>Data do Movimento (DDMMAA)</td>
	 * <td>3 bytes</td>
	 * </tr>
	 * <tr>
	 * <td>28</td>
	 * <td>FLAG de Truncamento</td>
	 * <td>1 byte</td>
	 * </tr>
	 * <tr>
	 * <td>29</td>
	 * <td>FLAG de Vinculação ao ISS</td>
	 * <td>2 bytes</td>
	 * </tr>
	 * <tr>
	 * <td>30</td>
	 * <td>Totalizador de Acréscimo</td>
	 * <td>7 bytes</td>
	 * </tr>
	 * <tr>
	 * <td>31</td>
	 * <td>Contador de Bilhete de Passagem</td>
	 * <td>3 bytes</td>
	 * </tr>
	 * <tr>
	 * <td>32</td>
	 * <td>Formas de Pagamento</td>
	 * <td></td>
	 * </tr>
	 * </table>
	 * <br>
	 * Para mais informações consulte a documentação da impressora.<br>
	 * <br>
	 * 
	 * @param var
	 *            indicação da informação.<br>
	 * 
	 * @return informação da impressora.<br>
	 */
	public String resultVariaveis( final char var ) {

		final byte[] CMD = { ESC, 35, (byte) var };
		/*
		 * o tamanho dos bytes de result varia conforme o parametro.
		 */
		executaCmd( CMD, 0 );
		String result = "";
		if ( var == V_NUM_SERIE || var == V_CNPJ_IE || var == V_CLICHE || var == V_MOEDA || var == V_DEPARTAMENTOS ) {
			result = new String( getBytesLidos() );
		}
		else if ( var == V_DT_ULT_REDUCAO ) {
			result = bcdToAsc( getBytesLidos() ).substring( 0, 6 );
		}
		else {
			result = bcdToAsc( getBytesLidos() );
		}
		return result;
	}

	/**
	 * Este comando só terá efeito quando a impressora indicar “Pouco Papel.<br>
	 * A impressora retorna ACK n1 n2 ST1 ST2. Onde n1+(n2*256) é o número de linhas impressas na condição de “Pouco Papel.<br>
	 * 
	 * @return estado do papel<br>
	 */
	public String resultEstadoPapel() {

		final byte[] CMD = { ESC, 62, 54 };
		executaCmd( CMD, 5 );
		return bcdToAsc( getBytesLidos() );
	}

	/**
	 * Leitura dos Dados da Última Redução.<br>
	 * 
	 * @return última redução<br>
	 */
	public String resultUltimaReducao() {

		final byte[] CMD = { ESC, 62, 55 };
		executaCmd( CMD, 308 );
		return bcdToAsc( getBytesLidos() );
	}

	/**
	 * Com este comando pode-se programar a moeda no singular.<br>
	 * Este comando possui o parâmetro com a descrição da moeda no tamanho de 19 bytes.<br>
	 * 
	 * @param nomeSingular
	 *            descrição.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult programaMoedaSingular( final String nomeSingular ) {

		byte[] CMD = { ESC, 58 };
		final StringBuffer buf = new StringBuffer();
		buf.append( parseParam( nomeSingular, 19, false ) );
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		return executaCmd( CMD, 3 );
	}

	/**
	 * Com este comando pode-se programar a moeda no plural.<br>
	 * Este comando possui o parâmetro com a descrição da moeda no tamanho de 22 bytes.<br>
	 * 
	 * @param nomePlurar
	 *            descrição.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult programaMoedaPlural( final String nomePlurar ) {

		byte[] CMD = { ESC, 59 };
		final StringBuffer buf = new StringBuffer();
		buf.append( parseParam( nomePlurar, 19, false ) );
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		return executaCmd( CMD, 3 );
	}

	/**
	 * Este comando retorna pela porta serial 1 byte correspondendo ao estado atual de<br>
	 * inserção ou não do cheque.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public String resultStatusCheque() {

		byte[] CMD = { ESC, 62, 48 };
		executaCmd( CMD, 3 );
		return bcdToAsc( getBytesLidos() );
	}

	/**
	 * <br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult cancelaImpressaoCheque() {

		byte[] CMD = { ESC, 62, 49 };
		return executaCmd( CMD, 3 );
	}

	/**
	 * Função para impressão de cheques.<br>
	 * <br>
	 * 
	 * @param valor
	 *            valor do cheque.
	 * @param favorecido
	 *            favorecido pelo cheque.
	 * @param localidade
	 *            praça do cheque
	 * @param dia
	 *            dia para composição da data.
	 * @param mes
	 *            mês para composição da data.
	 * @param ano
	 *            ano para composição da data.
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult imprimeCheque( final float valor, final String favorecido, final String localidade, final int dia, final int mes, final int ano ) {

		byte[] CMD = { ESC, 57 };
		final StringBuffer buf = new StringBuffer();
		buf.append( parseParam( valor, 14, 2 ) );
		buf.append( parseParam( favorecido, 45, false ) );
		buf.append( parseParam( localidade, 27, false ) );
		buf.append( parseParam( dia, 2 ) );
		buf.append( parseParam( mes, 2 ) );
		buf.append( parseParam( ano, 4 ) );
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		final byte[] posicoes = { 55, 10, 1, 6, 18, 50, 54, 71, 2, 5, 8, 10, 12, 0 };
		CMD = adicBytes( CMD, posicoes );
		return executaCmd( CMD, 3 );
	}

	public void aguardaImpressao() {

		byte[] CMD = { ESC, 19 };
		byte[] result = new byte[ 1 ];
		CMD = preparaCmd( CMD );
		while ( result.length < 2 ) {
			// depois que entra do laço e ocorre algum erro no envio do comando
			// a condição de result == null valida o laço
			// tornando ele um laço infinito...
			result = enviaCmd( CMD, 3 );
			try {
				Thread.sleep( 100 );
			}
			catch ( InterruptedException e ) {
			}
		}
	}

	public STResult habilitaCupomAdicional( final char opt ) {

		byte[] CMD = { ESC, 68 };
		CMD = adicBytes( CMD, parseParam( opt, 1 ).getBytes() );
		return executaCmd( CMD, 3 );
	}

	public STResult resetErro() {

		final byte[] CMD = { ESC, 70 };
		return executaCmd( CMD, 3 );
	}
}
