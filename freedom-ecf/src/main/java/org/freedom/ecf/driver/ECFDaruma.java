
package org.freedom.ecf.driver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.freedom.infra.comm.Serial;

/**
 * Classe implementa metodos de acesso a comandos de impressão <BR>
 * Projeto: freedom-ecf <BR>
 * Pacote: org.freedom.ecf.driver <BR>
 * Classe: @(#)ECFDaruma.java <BR>
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
 * @version 1.0.0 - 10/12/2007 <BR>
 * <BR>
 */
public class ECFDaruma extends AbstractECFDriver {

	private static final char CEND = (char) 255;

	private static Map<String, String> formasDePagamento;

	private String descricaoDoProduto;

	private String unidadeDeMedida;

	private int indexItemAtual = 0;

	private boolean relatorioGerencialAberto = false;

	/**
	 * Construtor da classe ECFDaruma. <BR>
	 */
	public ECFDaruma() {

		super();
	}

	/**
	 * Construtor da classe ECFDaruma. <BR>
	 * Inicia a construção da classe chamando o construtor padrão da classe super <BR>
	 * e chama o metodo ativaPorta(int). <BR>
	 * 
	 * @param com
	 *            parametro para ativação da porta serial.<BR>
	 */
	public ECFDaruma( final int com ) {

		this();
		activePort( com );
		setFormasDePagamento( resultFormasDePagamento() );
	}

	/**
	 * Construtor da classe ECFDaruma. <BR>
	 * Inicia a construção da classe chamando o construtor padrão da classe super <BR>
	 * e chama o metodo ativaPorta(int). <BR>
	 * 
	 * @param port
	 *            parametro para ativação da porta serial.<BR>
	 */
	public ECFDaruma( final String port ) {

		this( Serial.convPorta( port ) );
	}

	public static Map<String, String> getFormasDePagamento() {

		return formasDePagamento;
	}

	public static void setFormasDePagamento( Map<String, String> formasDePagamento ) {

		ECFDaruma.formasDePagamento = formasDePagamento;
	}

	public String getDescricaoDoProduto() {

		return descricaoDoProduto;
	}

	public void setDescricaoDoProduto( String descricaoDoProduto ) {

		this.descricaoDoProduto = descricaoDoProduto;
	}

	public String getUnidadeDeMedida() {

		return unidadeDeMedida;
	}

	public void setUnidadeDeMedida( String unidadeDeMedida ) {

		this.unidadeDeMedida = unidadeDeMedida;
	}

	public int getIndexItemAtual() {

		return indexItemAtual;
	}

	public void setIndexItemAtual( int indexItemAtual ) {

		this.indexItemAtual = indexItemAtual;
	}

	/**
	 * Prepara o comando conforme o protocolo de comunicação com a impressora. <BR>
	 * O protocolo de comunicação com a impressora é estruturado <BR>
	 * em blocos e possui a seguinte forma: <BR>
	 * <BR>
	 * CMD - Sequência de bytes que compõe o comando e seus parâmetros. <BR>
	 * 
	 * @param CMD
	 *            comando a ser executado e seus parâmetros. <BR>
	 * @see org.freedom.ecf.driver.AbstractECFDriver#preparaCmd(byte[])
	 */
	public byte[] preparaCmd( final byte[] CMD ) {

		final int tamCMD = CMD.length;
		final int tam = tamCMD + 1;
		byte[] result = new byte[ tam ];
		for ( int i = 0; i < tam; i++ ) {
			if ( i == ( tam - 1 ) ) {
				result[ i ] = 13;
			}
			else {
				result[ i ] = CMD[ i ];
			}
		}
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

		return executaCmd( CMD, tamresult, true );
	}

	public STResult executaCmd( final byte[] CMD, final int tamresult, final boolean checkcmd ) {

		byte[] result = null;
		byte[] cmd = null;
		cmd = preparaCmd( CMD );
		result = enviaCmd( cmd, tamresult );
		STResult cmdresult = checkcmd ? checkResult( result ) : checkResult2( result );
		return cmdresult;
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
		int erro = 0;
		int warning = 0;
		byte e1 = 0;
		byte e2 = 0;
		byte w1 = 0;
		byte w2 = 0;
		byte[] bytesLidos;
		if ( bytes != null ) {
			if ( bytes.length > 2 ) {
				e1 = bytes[ 2 ];
			}
			if ( bytes.length > 3 ) {
				e2 = bytes[ 3 ];
			}
			if ( bytes.length > 4 ) {
				w1 = bytes[ 4 ];
			}
			if ( bytes.length > 5 ) {
				w2 = bytes[ 5 ];
			}
			if ( bytes.length > 7 ) {
				bytesLidos = new byte[ bytes.length - 7 ];
				System.arraycopy( bytes, 6, bytesLidos, 0, bytesLidos.length );
				setBytesLidos( bytesLidos );
			}
			erro = checkError( e1, e2 );
			warning = checkWarning( w1, w2 );
			if ( erro != 0 ) {
				result.add( StatusDaruma.getStatusDaruma( erro ) );
			}
			else if ( warning != 1000 ) {
				result.add( StatusDaruma.getStatusDaruma( warning ) );
			}
		}
		return result;
	}

	public STResult checkResult2( final byte[] bytes ) {

		STResult result = STResult.getInstanceOk();
		byte[] bytesLidos;
		if ( bytes != null ) {
			if ( bytes.length > 2 ) {
				bytesLidos = new byte[ bytes.length - 2 ];
				System.arraycopy( bytes, 1, bytesLidos, 0, bytesLidos.length );
				setBytesLidos( bytesLidos );
			}
		}
		return result;
	}

	/**
	 * Auxilia o metodo checkResult.<BR>
	 * 
	 * @param ST1
	 * @return result checado
	 */
	private int checkError( byte e1, byte e2 ) {

		int result = Integer.parseInt( "" + (char) e1 + (char) e2 );
		return result;
	}

	/**
	 * Auxilia o metodo checkResult.<BR>
	 * 
	 * @param ST2
	 * @return result checado
	 */
	private int checkWarning( byte w1, byte w2 ) {

		// O Código na EStatus tem o 10 na frente para diferenciar dos de erros.
		int result = Integer.parseInt( ( "10" + (char) w1 + (char) w2 ).trim() );
		return result;
	}

	public STResult alteraSimboloMoeda( final String simbolo ) {

		return STResult.getInstanceNotImplemented();
	}

	public STResult adicaoDeAliquotaTriburaria( final String aliq, final char opt ) {

		final char CCMD = (char) 220;
		byte[] CMD = { ESC, (byte) CCMD };
		final StringBuffer buf = new StringBuffer();
		buf.append( AbstractECFDriver.ISS == opt ? "S" : "C" );
		buf.append( parseParam( aliq, 4, false ) );
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		return executaCmd( CMD, 8 );
	}

	public STResult programaHorarioVerao() {

		final char CCMD = (char) 228;
		byte[] CMD = { ESC, (byte) CCMD };
		final StringBuffer buf = new StringBuffer();
		buf.append( "xx" );
		buf.append( isHorarioVerao() ? 0 : 1 );
		buf.append( "xxxxxxxxxxxxxxxxx" );
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		return executaCmd( CMD, 7 );
	}

	public boolean isHorarioVerao() {

		final char CCMD = (char) 229;
		final byte[] CMD = { ESC, (byte) CCMD };
		executaCmd( CMD, 33 );
		final String result = new String( getBytesLidos() );
		boolean ativo = ( result.length() > 2 && result.charAt( 2 ) == '1' );
		return ativo;
	}

	public STResult nomeiaTotalizadorNaoSujeitoICMS( final int indice, final String desc ) {

		return nomeiaTotalizadorNaoSujeitoICMS( '+', indice, desc );
	}

	private STResult nomeiaTotalizadorNaoSujeitoICMS( final char tipo, final int indice, final String desc ) {

		final char CCMD = (char) 223;
		byte[] CMD = { ESC, (byte) CCMD };
		final StringBuffer buf = new StringBuffer();
		buf.append( tipo );
		buf.append( parseParam( indice, 2 ) );
		buf.append( parseParam( desc, 21 ) );
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		return executaCmd( CMD, 33 );
	}

	public STResult programaTruncamentoArredondamento( final char opt ) {

		// ainda não utilizada pela controller.
		return STResult.getInstanceNotImplemented();
	}

	public STResult programarEspacoEntreLinhas( final int espaco ) {

		// ainda não utilizada pela controller.
		return STResult.getInstanceNotImplemented();
	}

	public STResult programarLinhasEntreCupons( final int espaco ) {

		// ainda não utilizada pela controller.
		return STResult.getInstanceNotImplemented();
	}

	public STResult nomeiaDepartamento( final int index, final String descricao ) {

		// ainda não utilizada pela controller.
		return STResult.getInstanceNotImplemented();
	}

	/**
	 * Abertura de Cupom Fiscal. <br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult aberturaDeCupom() {

		final char CCMD = (char) 200;
		final byte[] CMD = { ESC, (byte) CCMD };
		setIndexItemAtual( 0 );
		return executaCmd( CMD, 13 );
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

		STResult result = indendificacaoConsumidor( cnpj );
		if ( !result.isInError() ) {
			result = aberturaDeCupom();
		}
		return result;
	}

	public STResult indendificacaoConsumidor( final String texto ) {

		final char CCMD = (char) 208;
		byte[] CMD = { ESC, (byte) CCMD };
		final StringBuffer buf = new StringBuffer();
		buf.append( parseParam( texto, 153 ) );
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		return executaCmd( CMD, 7 );
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
	 * <p style="background=#EEEEEE">
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

		setUnidadeDeMedida( descUnid );
		return STResult.getInstanceOk();
	}

	public STResult aumentaDescItem( final String descricao ) {

		setDescricaoDoProduto( descricao );
		return STResult.getInstanceOk();
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

		/*
		 * Descrição de produto.
		 */
		final char CCMD = (char) 202;
		byte[] CMD = { ESC, (byte) CCMD };
		final StringBuffer buf = new StringBuffer();
		buf.append( parseParam( aliquota, 2 ) );
		buf.append( parseParam( codProd, 18 ) );
		if ( tpdesc == DESCONTO_PERC ) {
			buf.append( 0 );
		}
		else if ( tpdesc == DESCONTO_VALOR ) {
			buf.append( 1 );
		}
		else if ( tpdesc == ACRECIMO_PERC ) {
			buf.append( 2 );
		}
		else if ( tpdesc == ACRECIMO_VALOR ) {
			buf.append( 3 );
		}
		buf.append( parseParam( desconto, 9, 2 ) );
		buf.append( parseParam( valor, 10, 3 ) );
		buf.append( parseParam( qtd, 8, 3 ) );
		if ( getUnidadeDeMedida() != null ) {
			buf.append( parseParam( getUnidadeDeMedida(), 2 ) );
			setUnidadeDeMedida( null );
		}
		else {
			buf.append( parseParam( "  ", 2 ) );
		}
		if ( getDescricaoDoProduto() != null ) {
			buf.append( parseParam( getDescricaoDoProduto(), 199 ).trim() );
			setDescricaoDoProduto( null );
		}
		else {
			buf.append( parseParam( descProd, 28 ).trim() );
		}
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		CMD = adicBytes( CMD, new byte[ ] { (byte) CEND } );
		final STResult result = executaCmd( CMD, 21 );
		if ( !result.isInError() ) {
			indexItemAtual++;
		}
		return result;
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

		/*
		 * Descrição de produto.
		 */
		final char CCMD = (char) 202;
		byte[] CMD = { ESC, (byte) CCMD };
		final StringBuffer buf = new StringBuffer();
		buf.append( parseParam( aliquota, 2 ) );
		buf.append( parseParam( codProd, 18 ) );
		if ( tpdesc == DESCONTO_PERC ) {
			buf.append( 0 );
		}
		else if ( tpdesc == DESCONTO_VALOR ) {
			buf.append( 1 );
		}
		else if ( tpdesc == ACRECIMO_PERC ) {
			buf.append( 2 );
		}
		else if ( tpdesc == ACRECIMO_VALOR ) {
			buf.append( 3 );
		}
		buf.append( parseParam( desconto, 9, 2 ) );
		buf.append( parseParam( valor, 10, 3 ) );
		buf.append( parseParam( qtd, 8, 3 ) );
		if ( getUnidadeDeMedida() != null ) {
			buf.append( parseParam( getUnidadeDeMedida(), 2 ) );
			setUnidadeDeMedida( null );
		}
		else {
			buf.append( parseParam( "  ", 2 ) );
		}
		if ( getDescricaoDoProduto() != null ) {
			buf.append( parseParam( getDescricaoDoProduto(), 199 ).trim() );
			setDescricaoDoProduto( null );
		}
		else {
			buf.append( parseParam( descProd, 28 ).trim() );
		}
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		CMD = adicBytes( CMD, new byte[ ] { (byte) CEND } );
		final STResult result = executaCmd( CMD, 21 );
		if ( !result.isInError() ) {
			indexItemAtual++;
		}
		return result;
	}

	public STResult vendaItemDepartamento( final String sitTrib, final float valor, final float qtd, final float desconto, final float acrescimo, final int departamento, final String unidade, final String codProd, final String descProd ) {

		return STResult.getInstanceNotImplemented();
	}

	public STResult cancelaItemAnterior() {

		final STResult result = cancelaItemGenerico( getIndexItemAtual() );
		return result;
	}

	public STResult cancelaItemGenerico( final int item ) {

		final char CCMD = (char) 204;
		byte[] CMD = { ESC, (byte) CCMD };
		final StringBuffer buf = new StringBuffer();
		buf.append( parseParam( item, 3 ) );
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		return executaCmd( CMD, 10 );
	}

	public STResult iniciaFechamentoCupom( final char opt, final float valor ) {

		/*
		 * Totalização de Cupom Fiscal.
		 */
		final char CCMD = (char) 206;
		byte[] CMD = { ESC, (byte) CCMD };
		final StringBuffer buf = new StringBuffer();
		if ( opt == DESCONTO_PERC ) {
			buf.append( 0 );
		}
		else if ( opt == DESCONTO_VALOR ) {
			buf.append( 1 );
		}
		else if ( opt == ACRECIMO_PERC ) {
			buf.append( 2 );
		}
		else if ( opt == ACRECIMO_VALOR ) {
			buf.append( 3 );
		}
		buf.append( parseParam( valor, 12, 2 ) );
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		return executaCmd( CMD, 19 );
	}

	public STResult efetuaFormaPagamento( final String indice, final float valor, final String descForma ) {

		/*
		 * Descrição da forma de pagamento.
		 */
		final char CCMD = (char) 207;
		byte[] CMD = { ESC, (byte) CCMD };
		final StringBuffer buf = new StringBuffer();
		buf.append( parseParam( indice, 2 ) );
		buf.append( parseParam( valor, 12, 2 ) );
		buf.append( parseParam( descForma, 47 ).trim() );
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		CMD = adicBytes( CMD, new byte[ ] { (byte) CEND } );
		return executaCmd( CMD, 19 );
	}

	public STResult finalizaFechamentoCupom( final String mensagem ) {

		/*
		 * Fechamento de cupom fiscal com mesagem promocional.
		 */
		final char CCMD = (char) 209;
		byte[] CMD = { ESC, (byte) CCMD };
		final StringBuffer buf = new StringBuffer();
		buf.append( parseParam( "S", 1 ).trim() );
		buf.append( parseParam( mensagem, 383 ).trim() );
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		CMD = adicBytes( CMD, new byte[ ] { (byte) CEND } );
		return executaCmd( CMD, 19 );
	}

	public STResult cancelaCupom() {

		final char CCMD = (char) 211;
		byte[] CMD = { ESC, (byte) CCMD };
		return executaCmd( CMD, 13 );
	}

	public String programaFormaPagamento( final String descricao ) {

		String indexFormaDePagamento = null;
		if ( descricao != null && "Dinheiro".equals( descricao ) ) {
			indexFormaDePagamento = "01";
		}
		else {
			String formaDePagamento = null;
			String indexFirstNull = null;
			for ( int index = 51; index <= 66; index++ ) {
				formaDePagamento = getFormasDePagamento().get( String.valueOf( index ) );
				if ( formaDePagamento != null && descricao != null && descricao.trim().equals( formaDePagamento.trim() ) ) {
					indexFormaDePagamento = String.valueOf( index );
					break;
				}
				else if ( indexFirstNull == null && "ÿÿÿÿÿÿÿÿÿÿÿÿÿÿÿÿÿÿÿÿÿ".equals( formaDePagamento ) ) {
					indexFirstNull = String.valueOf( index );
				}
			}
			if ( indexFormaDePagamento == null && descricao != null ) {
				final char CCMD = (char) 222;
				byte[] CMD = { ESC, (byte) CCMD };
				final StringBuffer buf = new StringBuffer();
				buf.append( indexFirstNull );
				buf.append( parseParam( descricao, 21 ) );
				CMD = adicBytes( CMD, buf.toString().getBytes() );
				executaCmd( CMD, 9 );
				indexFormaDePagamento = new String( getBytesLidos() );
			}
		}
		return indexFormaDePagamento;
	}

	public STResult estornoFormaPagamento( final String descricaoOrigem, final String descricaoDestino, final float valor ) {

		String indexFormaDePagamentoOrigem = null;
		String indexFormaDePagamentoDestino = null;
		if ( descricaoOrigem != null && "Dinheiro".equals( descricaoOrigem ) ) {
			indexFormaDePagamentoOrigem = "01";
		}
		else {
			String formaDePagamentoOrigem = null;
			for ( int index = 51; index <= 66; index++ ) {
				formaDePagamentoOrigem = getFormasDePagamento().get( String.valueOf( index ) );
				if ( formaDePagamentoOrigem != null && descricaoOrigem != null && descricaoOrigem.trim().equals( formaDePagamentoOrigem.trim() ) ) {
					indexFormaDePagamentoOrigem = String.valueOf( index );
					break;
				}
			}
		}
		if ( descricaoDestino != null && "Dinheiro".equals( descricaoDestino ) ) {
			indexFormaDePagamentoDestino = "01";
		}
		else {
			String formaDePagamentoDestino = null;
			for ( int index = 51; index <= 66; index++ ) {
				formaDePagamentoDestino = getFormasDePagamento().get( String.valueOf( index ) );
				if ( formaDePagamentoDestino != null && descricaoDestino != null && descricaoDestino.trim().equals( formaDePagamentoDestino.trim() ) ) {
					indexFormaDePagamentoDestino = String.valueOf( index );
					break;
				}
			}
		}
		final char CCMD = (char) 219;
		byte[] CMD = { ESC, (byte) CCMD };
		final StringBuffer buf = new StringBuffer();
		buf.append( indexFormaDePagamentoOrigem );
		buf.append( indexFormaDePagamentoDestino );
		buf.append( parseParam( Integer.parseInt( resultNumeroCupom() ), 6 ) );
		buf.append( parseParam( valor, 12, 2 ) );
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		return executaCmd( CMD, 17 );
	}

	/**
	 * Executa a Redução Z para a data atual da impressora fiscal.<BR>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult reducaoZ() {

		final char CCMD = (char) 252;
		byte[] CMD = { ESC, (byte) CCMD };
		final SimpleDateFormat sdt = new SimpleDateFormat( "ddMMyyHHmmss" );
		final String time = sdt.format( Calendar.getInstance().getTime() );
		CMD = adicBytes( CMD, time.getBytes() );
		return executaCmd( CMD, 13 );
	}

	/**
	 * Executa a Leitura X na impressora fiscal.<BR>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult leituraX() {

		final char CCMD = (char) 250;
		final byte[] CMD = { ESC, (byte) CCMD };
		return executaCmd( CMD, 13 );
	}

	public STResult leituraXSerial() {

		final char CCMD = (char) 251;
		final byte[] CMD = { ESC, (byte) CCMD };
		return executaCmd( CMD, 13 );
	}

	public STResult leituraMemoriaFiscal( final Date dataIni, final Date dataFim, final char tipo ) {

		final char CCMD = (char) 251;
		byte[] CMD = { ESC, (byte) CCMD };
		final StringBuffer buf = new StringBuffer();
		buf.append( AbstractECFDriver.IMPRESSAO == tipo ? "x" : "s" );
		buf.append( parseParam( dataIni ) );
		buf.append( parseParam( dataFim ) );
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		return executaCmd( CMD, 15, AbstractECFDriver.IMPRESSAO == tipo );
	}

	public STResult leituraMemoriaFiscal( final int ini, final int fim, final char tipo ) {

		final char CCMD = (char) 251;
		byte[] CMD = { ESC, (byte) CCMD };
		final StringBuffer buf = new StringBuffer();
		buf.append( AbstractECFDriver.IMPRESSAO == tipo ? "x" : "s" );
		buf.append( parseParam( ini, 6 ) );
		buf.append( parseParam( fim, 6 ) );
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		return executaCmd( CMD, 15, AbstractECFDriver.IMPRESSAO == tipo );
	}

	public STResult relatorioGerencial( final String texto ) {

		if ( !relatorioGerencialAberto ) {
			final char CCMD = (char) 210;
			byte[] CMD = { ESC, (byte) CCMD };
			final StringBuffer buf = new StringBuffer();
			buf.append( parseParam( 1, 2 ) );
			CMD = adicBytes( CMD, buf.toString().getBytes() );
			executaCmd( CMD, 15 );
			relatorioGerencialAberto = true;
		}
		return usaComprovanteNFiscalVinculado( texto );
	}

	public STResult fechamentoRelatorioGerencial() {

		final char CCMD = (char) 216;
		byte[] CMD = { ESC, (byte) CCMD };
		relatorioGerencialAberto = false;
		return executaCmd( CMD, 7 );
	}

	public STResult comprovanteNFiscalNVinculado( final String opt, final float valor, final String formaPag ) {

		final char ccmd = (char) 234;
		final byte[] cmd = { ESC, (byte) ccmd };
		executaCmd( cmd, 1431 );
		// verifica os comprovantes não fiscais não vinculados
		final List<String> cnfnv = new ArrayList<String>();
		final String returnCommand = new String( getBytesLidos() );
		int offset = 26;
		if ( returnCommand != null && returnCommand.length() > 26 ) {
			String tmp = null;
			for ( int i = 0; i < 16; i++ ) {
				tmp = returnCommand.substring( offset, offset += 22 );
				if ( !"+ÿÿÿÿÿÿÿÿÿÿÿÿÿÿÿÿÿÿÿÿÿ".equals( tmp ) ) {
					cnfnv.add( tmp );
				}
			}
		}
		String id = opt;
		// procura o indíce da sangria, se não encontrar cria.
		if ( AbstractECFDriver.SANGRIA.equals( opt ) ) {
			int index = 0;
			for ( String s : cnfnv ) {
				index++;
				if ( "-SANGRIA".equals( s.trim() ) ) {
					id = parseParam( index, 2 );
					break;
				}
			}
			if ( index == cnfnv.size() ) {
				id = parseParam( nomeiaTotalizadorNaoSujeitoICMS( '-', cnfnv.size(), "SANGRIA" ).getFirstCode(), 2 );
			}
		}
		// procura o indíce da suprimento, se não encontrar cria.
		else if ( AbstractECFDriver.SUPRIMENTO.equals( opt ) ) {
			int index = 0;
			for ( String s : cnfnv ) {
				index++;
				if ( "+SUPRIMENTO".equals( s.trim() ) ) {
					id = parseParam( index, 2 );
					break;
				}
			}
			if ( index == cnfnv.size() && cnfnv.size() > 0 ) {
				id = parseParam( nomeiaTotalizadorNaoSujeitoICMS( '+', cnfnv.size(), "SUPRIMENTO" ).getFirstCode(), 2 );
			}
		}
		final char CCMD = (char) 212;
		byte[] CMD = { ESC, (byte) CCMD };
		final StringBuffer buf = new StringBuffer();
		buf.append( parseParam( id, 2 ) );
		buf.append( 1 );
		buf.append( parseParam( 0f, 12, 2 ) );
		buf.append( parseParam( valor, 12, 2 ) );
		buf.append( parseParam( " ", 40 ) );
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		CMD = adicBytes( CMD, new byte[ ] { (byte) CEND } );
		return executaCmd( CMD, 27 );
	}

	public STResult abreComprovanteNFiscalVinculado( final String formaPag, final float valor, final int doc ) {

		final char CCMD = (char) 213;
		byte[] CMD = { ESC, (byte) CCMD };
		final StringBuffer buf = new StringBuffer();
		buf.append( programaFormaPagamento( formaPag ) );
		buf.append( parseParam( doc, 6 ) );
		buf.append( parseParam( valor, 12, 2 ) );
		CMD = adicBytes( CMD, buf.toString().getBytes() );
		return executaCmd( CMD, 15 );
	}

	public STResult usaComprovanteNFiscalVinculado( final String texto ) {

		final char CCMD = (char) 215;
		byte[] CMD = { ESC, (byte) CCMD };
		byte[] CMDT = null;
		final char[] str = texto.toCharArray();
		String tmp = "";
		STResult result = STResult.getInstanceOk();
		int index = 0;
		for ( int i = 0; i < str.length; i++ ) {
			if ( index++ == 47 || i == str.length || str[ i ] == 10 ) {
				CMDT = adicBytes( CMD, parseParam( tmp, 48 ).getBytes() );
				result = executaCmd( CMDT, 7 );
				if ( result.isInError() ) {
					return result;
				}
				tmp = "";
				index = 0;
			}
			if ( str[ i ] != 10 ) {
				tmp += String.valueOf( str[ i ] );
			}
		}
		return result;
	}

	public STResult autenticacaoDeDocumento() {

		return STResult.getInstanceNotImplemented();
	}

	public STResult programaCaracterParaAutenticacao( final int[] caracteres ) {

		return STResult.getInstanceNotImplemented();
	}

	public STResult acionaGavetaDinheiro( final int time ) {

		// *** sem gaveta pra testar.
		final char CCMD = 'p';
		byte[] CMD = { ESC, (byte) CCMD, 1, 0, 0 };
		return executaCmd( CMD, 7 );
	}

	public String resultEstadoGavetaDinheiro() {

		return "-1";
	}

	/**
	 * Através deste comando é possível verificar o estado atual da impressora.<br>
	 * 
	 * @see org.freedom.ecf.driver.EStatus
	 * @return estado da impressora.<br>
	 */
	public String getStatus() {

		final StringBuilder status = new StringBuilder();
		final byte[] CMD = { GS, ENQ };
		executaCmd( CMD, 14, false );
		final String tmp = new String( getBytesLidos() );
		final char[] chs = tmp.toCharArray();
		final int value[][] = new int[ 16 ][ 4 ];
		value[ 0 ] = new int[ ] { 0, 0, 0, 0 };
		value[ 1 ] = new int[ ] { 0, 0, 0, 1 };
		value[ 2 ] = new int[ ] { 0, 0, 1, 0 };
		value[ 3 ] = new int[ ] { 0, 0, 1, 1 };
		value[ 4 ] = new int[ ] { 0, 1, 0, 0 };
		value[ 5 ] = new int[ ] { 0, 1, 0, 1 };
		value[ 6 ] = new int[ ] { 0, 1, 1, 0 };
		value[ 7 ] = new int[ ] { 0, 1, 1, 1 };
		value[ 8 ] = new int[ ] { 1, 0, 0, 0 };
		value[ 9 ] = new int[ ] { 1, 0, 0, 1 };
		value[ 10 ] = new int[ ] { 1, 0, 1, 0 };
		value[ 11 ] = new int[ ] { 1, 0, 1, 1 };
		value[ 12 ] = new int[ ] { 1, 1, 0, 0 };
		value[ 13 ] = new int[ ] { 1, 1, 0, 1 };
		value[ 14 ] = new int[ ] { 1, 1, 1, 0 };
		value[ 15 ] = new int[ ] { 1, 1, 1, 1 };
		int index = 1;
		String key = "";
		StatusDaruma statusDaruma = null;
		for ( char ch : chs ) {
			if ( index != 8 && index <= 10 ) {
				for ( int i = 3; i >= 0; i-- ) {
					key = "";
					key += 2;
					key += index;
					key += i;
					key += value[ Integer.parseInt( String.valueOf( ch ), 16 ) ][ i ];
					statusDaruma = StatusDaruma.getStatusDaruma( Integer.parseInt( key ) );
					if ( statusDaruma != null ) {
						status.append( statusDaruma.getMessage() + "\n" );
					}
				}
			}
			index++;
		}
		return status.toString();
	}

	public String resultAliquotas() {

		final char CCMD = (char) 231;
		final byte[] CMD = { ESC, (byte) CCMD };
		executaCmd( CMD, 94 );
		final String aliquotas = new String( getBytesLidos() );
		final StringBuffer result = new StringBuffer();
		char[] chs = aliquotas.toCharArray();
		for ( char c : chs ) {
			if ( Character.isDigit( c ) ) {
				result.append( c );
			}
		}
		return result.toString();
	}

	public Map<String, String> resultFormasDePagamento() {

		final char CCMD = (char) 234;
		final byte[] CMD = { ESC, (byte) CCMD };
		executaCmd( CMD, 1431 );
		Map<String, String> returnAction = new HashMap<String, String>();
		final String[] keys = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "12", "14", "15", "16", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "62", "64", "65", "66" };
		final String returnCommand = new String( getBytesLidos() );
		int offset = 714;
		if ( returnCommand != null && returnCommand.length() > 714 ) {
			for ( int i = 0; i < 32; i++ ) {
				returnAction.put( keys[ i ], returnCommand.substring( offset + 1, offset += 22 ) );
			}
		}
		else {
			return null;
		}
		return returnAction;
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

		String result = "";
		char CCMD = (char) 236;
		byte[] CMD = { ESC, (byte) CCMD };
		executaCmd( CMD, 400 );
		String tmp = new String( getBytesLidos() );
		String parciaisTributados = "NNN";
		String isencao = tmp.substring( 134, 148 );
		String naoInsidencia = tmp.substring( 148, 162 );
		String substituicao = tmp.substring( 120, 134 );
		String naoSujeitosAIcms = "NNN";
		String sangria = "NNN";
		String suprimento = "NNN";
		String grandeTotal = tmp.substring( 18, 36 );
		result = parciaisTributados + "," + isencao + "," + naoInsidencia + "," + substituicao + "," + naoSujeitosAIcms + "," + sangria + "," + suprimento + "," + grandeTotal;
		return result;
	}

	public String resultSubTotal() {

		final char CCMD = (char) 205;
		final byte[] CMD = { ESC, (byte) CCMD };
		executaCmd( CMD, 19 );
		return new String( getBytesLidos() );
	}

	/**
	 * Este comando poderá ser utilizado logo após a abertura de um Cupom Fiscal, assim recebendo<br>
	 * o seu número, ou após qualquer Cupom fechado.<br>
	 * 
	 * @return número do cupom<br>
	 */
	public String resultNumeroCupom() {

		final char CCMD = (char) 239;
		final byte[] CMD = { ESC, (byte) CCMD };
		executaCmd( CMD, 227 );
		final String str = new String( getBytesLidos() );
		String result = "99999998";
		if ( str != null && str.length() >= 16 ) {
			result = str.substring( 2, 8 );
		}
		return result;
	}

	public boolean resultDocumentoAberto() {

		final char CCMD = (char) 235;
		final byte[] CMD = { ESC, (byte) CCMD };
		executaCmd( CMD, 65 );
		final char[] chr = new String( getBytesLidos() ).toCharArray();
		if ( chr != null && chr.length >= 3 ) {
			if ( '1' == chr[ 2 ] ) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Retorna a imformação da impressora comforme o parametro especificado.<br>
	 * 
	 * @param var
	 *            indicação da informação.<br>
	 * 
	 * @return informação da impressora.<br>
	 */
	public String resultVariaveis( final char var ) {

		String result = "-1";
		if ( var == V_NUM_CAIXA ) {
			final char CCMD = (char) 233;
			final byte[] CMD = { ESC, (byte) CCMD };
			executaCmd( CMD, 57 );
			String tmp = new String( getBytesLidos() );
			if ( tmp != null && tmp.trim().length() > 22 ) {
				result = tmp.substring( 22, 26 );
			}
		}
		else if ( var == V_REDUCOES || var == V_CUPONS_CANC || var == V_DESCONTOS || var == V_CANCELAMENTOS ) {
			final char CCMD = (char) 237;
			final byte[] CMD = { ESC, (byte) CCMD };
			executaCmd( CMD, 1008 );
			String tmp = new String( getBytesLidos() );
			if ( var == V_CUPONS_CANC && tmp != null && tmp.trim().length() > 24 ) {
				result = tmp.substring( 18, 24 );
			}
			else if ( var == V_REDUCOES && tmp != null && tmp.trim().length() > 54 ) {
				result = tmp.substring( 48, 54 );
			}
			else if ( var == V_DESCONTOS && tmp != null && tmp.trim().length() > 952 ) {
				result = tmp.substring( 938, 952 );
			}
			else if ( var == V_CANCELAMENTOS && tmp != null && tmp.trim().length() > 966 ) {
				result = tmp.substring( 952, 966 );
			}
		}
		else if ( var == V_DT_ULT_REDUCAO ) {
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

		return "-1"; // ainda não utilizada pela controller.
	}

	/**
	 * Leitura dos Dados da Última Redução.<br>
	 * 
	 * @return última redução<br>
	 */
	public String resultUltimaReducao() {

		return "-1"; // ainda não utilizada pela controller.
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

		return STResult.getInstanceNotImplemented();
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

		return STResult.getInstanceNotImplemented();
	}

	/**
	 * Este comando retorna pela porta serial 1 byte correspondendo ao estado atual de<br>
	 * inserção ou não do cheque.<br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public String resultStatusCheque() {

		return "-1";
	}

	/**
	 * <br>
	 * 
	 * @return estado da impressora.<br>
	 */
	public STResult cancelaImpressaoCheque() {

		return STResult.getInstanceNotImplemented();
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

		return STResult.getInstanceNotImplemented();
	}

	public void aguardaImpressao() {

	// ainda não utilizada pela controller.
	}

	public STResult habilitaCupomAdicional( final char opt ) {

		return STResult.getInstanceNotImplemented();
	}

	public STResult resetErro() {

		return STResult.getInstanceNotImplemented();
	}
}
