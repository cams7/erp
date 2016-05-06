
package org.freedom.ecf.driver;

import java.util.Date;
import org.freedom.ecf.layout.AbstractLayout;
import org.freedom.infra.comm.AbstractPort;

/**
 * Classe implementa metodos de acesso a comandos de impressão <BR>
 * Projeto: freedom-ecf <BR>
 * Pacote: org.freedom.ecf.driver <BR>
 * Classe: @(#)BematechMP20TH.java <BR>
 * <BR>
 * Este programa é licenciado de acordo com a LGPL (Lesser General Public License), <BR>
 * versão 2.1, Fevereiro de 1999 <BR>
 * A LGPL deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste Programa. <BR>
 * Caso uma cópia da LGPL não esteja disponível junto com este Programa, você pode contatar <BR>
 * o LICENCIADOR ou então pegar uma cópia em: <a href=http://creativecommons.org/licenses/LGPL/2.1/legalcode.pt> Creative Commons</a> <BR>
 * Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa é preciso estar de acordo com os termos da LGPL. <BR>
 * <BR>
 * 
 * @author Setpoint Informática Ltda. Robson Sanchez <BR>
 * @version 1.0.0 - 23/03/2009 <BR>
 * <BR>
 */
public class BematechMP20TH extends AbstractECFDriver {

	/**
	 * Sobrescrita a propriedade de result de impressora fiscal.
	 */
	protected boolean fiscal = false;

	/**
	 * Construtor da classe ECFBematech. <BR>
	 */
	public BematechMP20TH() {

		super();
	}

	/**
	 * Construtor da classe ECFBematech. <BR>
	 * Inicia a construção da classe chamando o construtor padrão da classe super <BR>
	 * e chama o metodo ativaPorta(int). <BR>
	 * 
	 * @param com
	 *            parametro para ativação da porta de comunicação.<BR>
	 * @param layoutNFiscal
	 */
	public BematechMP20TH( final int com, final String layoutNFiscal ) {

		super();
		activePort( com );
		setLayoutNFiscal( layoutNFiscal );
	}

	/**
	 * Construtor da classe ECFBematech. <BR>
	 * Inicia a construção da classe chamando o construtor padrão da classe super <BR>
	 * e chama o metodo ativaPorta(int). <BR>
	 * 
	 * @param port
	 *            parametro para ativação da porta comunicação.<BR>
	 * @param layoutNFiscal
	 */
	public BematechMP20TH( final String port, final String layoutNFiscal ) {

		super();
		activePort( AbstractPort.convPorta( port ) );
		setLayoutNFiscal( layoutNFiscal );
	}

	public BematechMP20TH( final String port, final AbstractLayout layoutNFiscal ) {

		super();
		activePort( AbstractPort.convPorta( port ) );
		setLayoutNFiscal( layoutNFiscal );
	}

	/**
	 * Prepara o comando conforme o protocolo de comunicação com a impressora. <BR>
	 * 
	 * @param CMD
	 *            comando a ser executado e seus parâmetros. <BR>
	 * @see org.freedom.ecf.driver.AbstractECFDriver#preparaCmd(byte[])
	 */
	public byte[] preparaCmd( final byte[] CMD ) {

		final int tamCMD = CMD != null ? CMD.length : 0;
		byte[] result = new byte[ tamCMD + 1 ];
		for ( int i = 0; i < result.length - 1; i++ ) {
			result[ i ] = CMD[ i ];
		}
		result[ result.length - 1 ] = (byte) 10;
		return result;
	}

	public STResult executaCmd( final byte[] CMD, final int tamresult ) {

		byte[] result = null;
		byte[] cmd = null;
		cmd = preparaCmd( CMD );
		result = enviaCmd( cmd, tamresult );
		return checkResult( result );
	}

	public STResult checkResult( final byte[] bytes ) {

		return STResult.getInstanceOk();
	}

	public STResult alteraSimboloMoeda( final String simbolo ) {

		if ( objLayoutNFiscal != null ) {
			objLayoutNFiscal.alteraSimboloMoeda( simbolo );
		}
		return STResult.getInstanceOk();
	}

	public STResult adicaoDeAliquotaTriburaria( final String aliq, final char opt ) {

		return STResult.getInstanceOk();
	}

	public STResult programaHorarioVerao() {

		return STResult.getInstanceOk();
	}

	public boolean isHorarioVerao() {

		return false;
	}

	public STResult nomeiaTotalizadorNaoSujeitoICMS( final int indice, final String desc ) {

		return STResult.getInstanceOk();
	}

	public STResult programaTruncamentoArredondamento( final char opt ) {

		return STResult.getInstanceOk();
	}

	public STResult programarEspacoEntreLinhas( final int espaco ) {

		return STResult.getInstanceOk();
	}

	public STResult programarLinhasEntreCupons( final int espaco ) {

		return STResult.getInstanceOk();
	}

	public STResult nomeiaDepartamento( final int index, final String descricao ) {

		return STResult.getInstanceOk();
	}

	public STResult aberturaDeCupom() {

		byte[] CMD = null;
		if ( objLayoutNFiscal != null ) {
			CMD = objLayoutNFiscal.aberturaDeCupom().getBytes();
		}
		return executaCmd( CMD, 0 );
	}

	public STResult aberturaDeCupom( final String cnpj ) {

		byte[] CMD = null;
		if ( objLayoutNFiscal != null ) {
			CMD = objLayoutNFiscal.aberturaDeCupom( cnpj ).getBytes();
		}
		return executaCmd( CMD, 0 );
	}

	public STResult programaUnidadeMedida( final String descUnid ) {

		if ( objLayoutNFiscal != null ) {
			objLayoutNFiscal.programaUnidadeMedida( descUnid );
		}
		return STResult.getInstanceOk();
	}

	public STResult aumentaDescItem( final String descricao ) {

		return STResult.getInstanceOk();
	}

	public STResult vendaItem( final String codProd, final String descProd, final String aliquota, final char tpqtd, final float qtd, final float valor, final char tpdesc, final float desconto ) {

		byte[] CMD = null;
		if ( objLayoutNFiscal != null ) {
			CMD = objLayoutNFiscal.vendaItem( codProd, descProd, aliquota, tpqtd, qtd, valor, tpdesc, desconto ).getBytes();
		}
		return executaCmd( CMD, 0 );
	}

	public STResult vendaItemTresCasas( final String codProd, final String descProd, final String aliquota, final char tpqtd, final float qtd, final float valor, final char tpdesc, final float desconto ) {

		return STResult.getInstanceOk();
	}

	public STResult vendaItemDepartamento( final String sitTrib, final float valor, final float qtd, final float desconto, final float acrescimo, final int departamento, final String unidade, final String codProd, final String descProd ) {

		return STResult.getInstanceOk();
	}

	public STResult cancelaItemAnterior() {

		return STResult.getInstanceOk();
	}

	public STResult cancelaItemGenerico( final int item ) {

		return STResult.getInstanceOk();
	}

	public STResult iniciaFechamentoCupom( final char opt, final float valor ) {

		byte[] CMD = null;
		if ( objLayoutNFiscal != null ) {
			CMD = objLayoutNFiscal.iniciaFechamentoCupom( opt, valor ).getBytes();
		}
		return executaCmd( CMD, 0 );
	}

	public STResult efetuaFormaPagamento( final String indice, final float valor, final String descForma ) {

		byte[] CMD = null;
		if ( objLayoutNFiscal != null ) {
			CMD = objLayoutNFiscal.efetuaFormaPagamento( indice, valor, descForma ).getBytes();
		}
		return executaCmd( CMD, 0 );
	}

	public STResult finalizaFechamentoCupom( final String mensagem ) {

		byte[] CMD = null;
		if ( objLayoutNFiscal != null ) {
			CMD = objLayoutNFiscal.finalizaFechamentoCupom( mensagem ).getBytes();
		}
		return executaCmd( CMD, 0 );
	}

	public STResult cancelaCupom() {

		return STResult.getInstanceOk();
	}

	public String programaFormaPagamento( final String descricao ) {

		String formaPagamento = null;
		if ( objLayoutNFiscal != null ) {
			formaPagamento = objLayoutNFiscal.programaFormaPagamento( descricao );
		}
		return formaPagamento;
	}

	public STResult estornoFormaPagamento( final String descOrigem, final String descDestino, final float valor ) {

		return STResult.getInstanceOk();
	}

	public STResult reducaoZ() {

		return STResult.getInstanceOk();
	}

	public STResult leituraX() {

		return STResult.getInstanceOk();
	}

	public STResult leituraMemoriaFiscal( final Date dataIni, final Date dataFim, final char tipo ) {

		return STResult.getInstanceOk();
	}

	public STResult leituraMemoriaFiscal( final int ini, final int fim, final char tipo ) {

		return STResult.getInstanceOk();
	}

	public STResult leituraXSerial() {

		return STResult.getInstanceOk();
	}

	public STResult relatorioGerencial( final String texto ) {

		byte[] CMD = null;
		if ( objLayoutNFiscal != null ) {
			CMD = objLayoutNFiscal.relatorioGerencial( texto ).getBytes();
		}
		return executaCmd( CMD, 0 );
	}

	public STResult fechamentoRelatorioGerencial() {

		byte[] CMD = null;
		if ( objLayoutNFiscal != null ) {
			CMD = objLayoutNFiscal.fechamentoRelatorioGerencial().getBytes();
		}
		return executaCmd( CMD, 0 );
	}

	public STResult comprovanteNFiscalNVinculado( final String opt, final float valor, final String formaPag ) {

		byte[] CMD = null;
		if ( objLayoutNFiscal != null ) {
			CMD = objLayoutNFiscal.comprovanteNFiscalNVinculado( opt, valor, formaPag ).getBytes();
		}
		return executaCmd( CMD, 0 );
	}

	public STResult abreComprovanteNFiscalVinculado( final String formaPag, final float valor, final int doc ) {

		return STResult.getInstanceOk();
	}

	public STResult usaComprovanteNFiscalVinculado( final String texto ) {

		return STResult.getInstanceOk();
	}

	public STResult autenticacaoDeDocumento() {

		return STResult.getInstanceOk();
	}

	public STResult programaCaracterParaAutenticacao( final int[] caracteres ) {

		return STResult.getInstanceOk();
	}

	public STResult acionaGavetaDinheiro( final int time ) {

		return STResult.getInstanceOk();
	}

	public String resultEstadoGavetaDinheiro() {

		return null;
	}

	public String getStatus() {

		return null;
	}

	public String resultAliquotas() {

		String aliquotas = "";
		if ( objLayoutNFiscal != null ) {
			aliquotas = objLayoutNFiscal.resultAliquotas();
		}
		return aliquotas;
	}

	public String resultTotalizadoresParciais() {

		return null;
	}

	public String resultSubTotal() {

		return null;
	}

	public String resultNumeroCupom() {

		return null;
	}

	public boolean resultDocumentoAberto() {

		return false;
	}

	public String resultVariaveis( final char var ) {

		return null;
	}

	public String resultEstadoPapel() {

		return null;
	}

	public String resultUltimaReducao() {

		return null;
	}

	public STResult programaMoedaSingular( final String nomeSingular ) {

		return STResult.getInstanceOk();
	}

	public STResult programaMoedaPlural( final String nomePlurar ) {

		return STResult.getInstanceOk();
	}

	public String resultStatusCheque() {

		return null;
	}

	public STResult cancelaImpressaoCheque() {

		return STResult.getInstanceOk();
	}

	public STResult imprimeCheque( final float valor, final String favorecido, final String localidade, final int dia, final int mes, final int ano ) {

		return STResult.getInstanceOk();
	}

	public void aguardaImpressao() {

	}

	public STResult habilitaCupomAdicional( final char opt ) {

		return STResult.getInstanceOk();
	}

	public STResult resetErro() {

		return STResult.getInstanceOk();
	}
}
