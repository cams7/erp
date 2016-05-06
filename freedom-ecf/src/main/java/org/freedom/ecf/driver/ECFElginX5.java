
package org.freedom.ecf.driver;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.comm.SerialPort;

/**
 * Classe implementa metodos de acesso a comandos de impressão <BR>
 * Projeto: freedom-ecf <BR>
 * Pacote: org.freedom.ecf.driver <BR>
 * Classe: @(#)ECFElginX5.java <BR>
 * <BR>
 * Este programa é licenciado de acordo com a LGPL (Lesser General Public License), <BR>
 * versão 2.1, Fevereiro de 1999 <BR>
 * A LGPL deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste Programa. <BR>
 * Caso uma cópia da LGPL não esteja disponível junto com este Programa, você pode contatar <BR>
 * o LICENCIADOR ou então pegar uma cópia em: <a href=http://creativecommons.org/licenses/LGPL/2.1/legalcode.pt> Creative Commons</a> <BR>
 * Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa é preciso estar de acordo com os termos da LGPL. <BR>
 * <BR>
 * 
 * @author Setpoint Informática Ltda. Alex Rodrigues <BR>
 * @version 1.0.0 - 28/05/2009 <BR>
 * <BR>
 */
public class ECFElginX5 extends AbstractECFDriver {

	private boolean relatorioGerencialAberto = false;

	public ECFElginX5() {

		super();
		TIMEOUT_READ = 1000;
		configSerialParams();
	}

	public ECFElginX5( final int com ) {

		super();
		configSerialParams();
		activePort( com );
	}

	public ECFElginX5( final String port ) {

		super();
		configSerialParams();
		activePort( port );
	}

	private void configSerialParams() {

		serialParams.setTimeout( 150 );
		serialParams.setBauderate( 115200 );
		serialParams.setDatabits( SerialPort.DATABITS_8 );
		serialParams.setStopbits( SerialPort.STOPBITS_1 );
		serialParams.setParity( SerialPort.PARITY_EVEN );
	}

	public byte[] preparaCmd( byte[] CMD ) {

		StringBuilder cmd = new StringBuilder();
		cmd.append( "{0;" );
		cmd.append( new String( CMD ) );
		cmd.append( ";" );
		cmd.append( cmd.length() - 1 );
		cmd.append( "}" );
		return cmd.toString().getBytes();
	}

	public STResult executaCmd( final byte[] CMD, final int tamresult ) {

		byte[] result = null;
		byte[] cmd = null;
		cmd = preparaCmd( CMD );
		result = enviaCmd( cmd, tamresult );
		return checkResult( result );
	}

	public STResult executa( final String comando, String... parametros ) {

		return executa( comando, 1, parametros );
	}

	private STResult executa( final String comando, int tamanhoResultado, String... parametros ) {

		StringBuilder cmd = new StringBuilder();
		cmd.append( comando );
		cmd.append( ";" );
		for ( int i = 0; i < parametros.length; i++ ) {
			if ( parametros[ i ] != null && parametros[ i ].trim().length() > 0 ) {
				if ( i > 0 ) {
					cmd.append( " " );
				}
				cmd.append( parametros[ i ] );
			}
		}
		return executaCmd( cmd.toString().getBytes(), tamanhoResultado );
	}

	public STResult checkResult( final byte[] bytes ) {

		STResult result = new STResult();
		if ( bytes != null ) {
			String resposta = new String( bytes );
			StringTokenizer token = new StringTokenizer( resposta, ";" );
			final String codigoErroSucesso;
			final String mensagemErroSucesso;
			if ( token.countTokens() == 3 ) {
				token.nextToken();
				codigoErroSucesso = token.nextToken();
				mensagemErroSucesso = null;
			}
			else if ( token.countTokens() == 4 ) {
				token.nextToken();
				codigoErroSucesso = token.nextToken();
				// Aqui retorna a reposta do comando. Em caso de erro, retorna "NomeErro" (string que contém o nome do erro) e a "Circunstância" (string, com o texto explicativo sobre a circunstância do erro).
				mensagemErroSucesso = token.nextToken();
			}
			else {
				codigoErroSucesso = null;
				mensagemErroSucesso = null;
			}
			setBytesLidos( ( mensagemErroSucesso == null ? "" : mensagemErroSucesso ).getBytes() );
			// Sem erros, definindo resposta.
			if ( Integer.parseInt( codigoErroSucesso ) == 0 ) {
				result = STResult.getInstanceOk();
			}
			else {
				result.add( StatusElginX5.getStatusElginX5( Integer.parseInt( codigoErroSucesso ), getRetornos().get( "Circunstancia" ) ) );
			}
		}
		else {
			result = STResult.getInstanceNotComunication();
		}
		return result;
	}

	private String formatString( String text, int size ) {

		String st = ( text == null ? "" : text );
		return "\"" + ( st.length() > size ? st.substring( 0, size ) : st ) + "\"";
	}

	private String formatInteger( int number, int size ) {

		String si = String.valueOf( number );
		return si.length() > size ? si.substring( 0, size ) : si;
	}

	private String formatFloat( float number, int size, int decimal ) {

		BigDecimal bd = new BigDecimal( number ).setScale( decimal, BigDecimal.ROUND_HALF_UP );
		String sf = String.valueOf( bd );
		sf = sf.replace( '.', ',' );
		return ( sf.length() + ( sf.indexOf( ',' ) > -1 ? -1 : 0 ) ) > size ? sf.substring( 0, size ) : sf;
	}

	private String formatDate( Date date, boolean delimitador ) {

		SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy" );
		String data = sdf.format( date );
		return delimitador ? ( "#" + data + "#" ) : data;
	}

	private Map<String, String> getRetornos() {

		Map<String, String> retornos = new HashMap<String, String>();
		String resposta = new String( getBytesLidos() );
		char[] chs = resposta.toCharArray();
		String key = null;
		String value = null;
		String tmp = null;
		StringBuilder buffer = new StringBuilder();
		int indexIgual = 0;
		int indexEnd = 0;
		for ( int i = 0; i < chs.length; i++ ) {
			if ( '=' == chs[ i ] ) {
				key = buffer.toString();
				buffer.delete( 0, buffer.length() );
				i++;
				tmp = resposta.substring( i );
				indexIgual = tmp.indexOf( '=' );
				tmp = indexIgual > -1 ? tmp.substring( 0, indexIgual ) : tmp;
				if ( indexIgual > -1 ) {
					indexEnd = tmp.lastIndexOf( " " );
					value = tmp.substring( 0, indexEnd );
					if ( '\"' == value.charAt( 0 ) ) {
						value = value.substring( 1, value.length() - 1 );
					}
					i += ++indexEnd;
					retornos.put( key, value );
				}
				else {
					value = tmp;
					if ( '\"' == value.charAt( 0 ) ) {
						value = value.substring( 1, value.length() - 1 );
					}
					retornos.put( key, value );
					break;
				}
			}
			buffer.append( chs[ i ] );
		}
		return retornos;
	}

	public STResult adicaoDeAliquotaTriburaria( final String aliq, final char opt ) {

		String aliquota = AbstractECFDriver.ICMS == opt ? null : "AliquotaICMS=false";
		String valor = "PercentualAliquota=" + formatFloat( Float.parseFloat( aliq ) / 100, 4, 2 );
		return executa( "DefineAliquota", aliquota, valor );
	}

	public STResult programaHorarioVerao() {

		return STResult.getInstanceNotImplemented();
	}

	public boolean isHorarioVerao() {

		return false;
	}

	public STResult nomeiaTotalizadorNaoSujeitoICMS( final int indice, final String desc ) {

		return executa( "DefineNaoFiscal", "CodNaoFiscal=" + indice, "NomeNaoFiscal=" + formatString( desc, 80 ) );
	}

	public STResult programaTruncamentoArredondamento( final char opt ) {

		return STResult.getInstanceNotImplemented();
	}

	public STResult programarEspacoEntreLinhas( final int espaco ) {

		return STResult.getInstanceNotImplemented();
	}

	public STResult programarLinhasEntreCupons( final int espaco ) {

		return STResult.getInstanceNotImplemented();
	}

	public STResult nomeiaDepartamento( final int index, final String descricao ) {

		return STResult.getInstanceNotImplemented();
	}

	public STResult aberturaDeCupom() {

		return executa( "AbreCupomFiscal" );
	}

	public STResult aberturaDeCupom( final String cnpj ) {

		return executa( "AbreCupomFiscal" );
	}

	public STResult programaUnidadeMedida( final String descUnid ) {

		return STResult.getInstanceNotImplemented();
	}

	public STResult aumentaDescItem( final String descricao ) {

		return STResult.getInstanceNotImplemented();
	}

	public STResult vendaItem( final String codProd, final String descProd, final String aliquota, final char tpqtd, final float qtd, final float valor, final char tpdesc, final float desc ) {

		String codAliquota = "CodAliquota=" + aliquota;
		String codProduto = "CodProduto=" + formatString( codProd, 48 );
		String nomeProduto = "NomeProduto=" + formatString( descProd, 200 );
		String precoUnitario = "PrecoUnitario=" + formatFloat( valor, 15, 2 );
		String quantidade = "Quantidade=";
		if ( tpqtd == QTD_DECIMAL ) {
			quantidade += formatFloat( qtd, 7, 3 );
		}
		else if ( tpqtd == QTD_INTEIRO ) {
			quantidade += formatFloat( qtd, 4, 0 );
		}
		STResult result = null;
		result = executa( "VendeItem", codAliquota, codProduto, nomeProduto, precoUnitario, quantidade );
		if ( STResult.getInstanceOk() == result && desc > 0 ) {
			String desconto = null;
			if ( tpdesc == ACRECIMO_VALOR ) {
				desconto = "ValorAcrescimo=" + formatFloat( desc, 12, 2 );
			}
			else if ( tpdesc == ACRECIMO_PERC ) {
				desconto = "ValorPercentual=" + formatFloat( desc, 4, 2 );
			}
			if ( tpdesc == DESCONTO_VALOR ) {
				desconto = "ValorAcrescimo=" + formatFloat( ( desc < 0 ? desc : desc * -1 ), 12, 2 );
			}
			else if ( tpdesc == DESCONTO_PERC ) {
				desconto = "ValorPercentual=" + formatFloat( ( desc < 0 ? desc : desc * -1 ), 4, 2 );
			}
			executa( "AcresceItemFiscal", "Cancelar=false", desconto );
		}
		return result;
	}

	public STResult vendaItemTresCasas( final String codProd, final String descProd, final String aliquota, final char tpqtd, final float qtd, final float valor, final char tpdesc, final float desconto ) {

		return STResult.getInstanceNotImplemented();
	}

	public STResult vendaItemDepartamento( final String sitTrib, final float valor, final float qtd, final float desconto, final float acrescimo, final int departamento, final String unidade, final String codProd, final String descProd ) {

		return STResult.getInstanceNotImplemented();
	}

	public STResult cancelaItemAnterior() {

		return executa( "CancelaItemFiscal" );
	}

	public STResult cancelaItemGenerico( final int item ) {

		return executa( "CancelaItemFiscal", "NumItem=" + item );
	}

	public STResult iniciaFechamentoCupom( final char opt, final float valor ) {

		float vlr = ( opt == ACRECIMO_VALOR || opt == ACRECIMO_PERC ) ? valor : ( valor * -1 );
		if ( vlr > 0 ) {
			String cancelar = "Cancelar=false";
			String valorAcrescimo = "ValorAcrescimo=" + formatFloat( vlr, 15, 2 );
			String valorPercentual = "ValorPercentual=" + formatFloat( vlr, 15, 2 );
			return executa( "AcresceSubtotal", cancelar, ( ( opt == ACRECIMO_VALOR || opt == DESCONTO_VALOR ) ? valorAcrescimo : valorPercentual ) );
		}
		else {
			return STResult.getInstanceOk();
		}
	}

	public STResult efetuaFormaPagamento( final String indice, final float valor, final String descForma ) {

		String codMeioPagamento = "CodMeioPagamento=" + indice;
		String pValor = "Valor=" + formatFloat( valor, 15, 2 );
		String textoAdicional = "TextoAdicional=" + formatString( descForma, 80 );
		return executa( "PagaCupom", codMeioPagamento, pValor, textoAdicional );
	}

	public STResult finalizaFechamentoCupom( final String mensagem ) {

		String textoPromocional = "TextoPromocional=" + formatString( mensagem, 492 );
		return executa( "EncerraDocumento", textoPromocional );
	}

	public STResult cancelaCupom() {

		return executa( "CancelaCupom" );
	}

	public String programaFormaPagamento( final String descricao ) {

		String nomeMeioPagamento = "NomeMeioPagamento=" + formatString( descricao, 16 );
		STResult result = executa( "LeMeioPagamento", nomeMeioPagamento );
		String meioPagamento = null;
		if ( STResult.getInstanceOk() == result ) {
			meioPagamento = getRetornos().get( "CodMeioPagamentoProgram" );
		}
		else {
			result = executa( "DefineMeioPagamento", nomeMeioPagamento );
			if ( STResult.getInstanceOk() == result ) {
				meioPagamento = getRetornos().get( "CodMeioPagamentoProgram" );
			}
		}
		return meioPagamento;
	}

	public STResult estornoFormaPagamento( final String descOrigem, final String descDestino, final float valor ) {

		String nomeMeioPagamentoOrigem = "NomeMeioPagamentoOrig=" + formatString( descOrigem, 16 );
		String nomeMeioPagamentoDestino = "NomeMeioPagamentoDest=" + formatString( descDestino, 16 );
		String pValor = "Valor=" + formatFloat( valor, 15, 2 );
		return executa( "EstornaMeioPagamento", nomeMeioPagamentoOrigem, nomeMeioPagamentoDestino, pValor );
	}

	public STResult reducaoZ() {

		return executa( "EmiteReducaoZ" );
	}

	public STResult leituraX() {

		return executa( "EmiteLeituraX" );
	}

	public STResult leituraMemoriaFiscal( final Date dataIni, final Date dataFim, final char tipo ) {

		String dataInicial = "DataInicial=" + formatDate( dataIni, true );
		String dataFinal = "DataFinal=" + formatDate( dataFim, true );
		String destino = "Destino=" + formatString( String.valueOf( tipo ), 1 );
		STResult result = executa( "EmiteLeituraMF", dataInicial, dataFinal, destino, "LeituraSimplificada=t" );
		if ( ECFElginX5.SERIAL == tipo ) {
			result = executa( "LeImpressao" );
		}
		return result;
	}

	public STResult leituraMemoriaFiscal( final int ini, final int fim, final char tipo ) {

		String reducaoInicial = "ReducaoInicial=" + formatInteger( ini, 6 );
		String reducaoFinal = "ReducaoFinal=" + formatInteger( fim, 6 );
		String destino = "Destino=" + formatString( String.valueOf( tipo ), 1 );
		return executa( "EmiteLeituraMF", reducaoInicial, reducaoFinal, destino, "LeituraSimplificada=t" );
	}

	public STResult leituraXSerial() {

		STResult result = executa( "EmiteLeituraX", "Destino=\"S\"" );
		if ( STResult.getInstanceOk() == result ) {
			executa( "LeImpressao" );
			String textoImpressao = getRetornos().get( "TextoImpressao" );
			if ( textoImpressao != null ) {
				setBytesLidos( textoImpressao.getBytes() );
			}
		}
		return result;
	}

	public STResult relatorioGerencial( final String texto ) {

		STResult result = null;
		if ( relatorioGerencialAberto ) {
			result = executa( "ImprimeTexto", "TextoLivre=" + formatString( texto, 492 ) );
		}
		else {
			result = executa( "AbreGerencial", "CodGerencial=00" );
			relatorioGerencialAberto = true;
		}
		return result;
	}

	public STResult fechamentoRelatorioGerencial() {

		relatorioGerencialAberto = false;
		return executa( "EncerraDocumento" );
	}

	public STResult comprovanteNFiscalNVinculado( final String opt, final float valor, final String formaPag ) {

		STResult result = executa( "AbreCupomNaoFiscal" );
		if ( STResult.getInstanceOk() != result ) {
			return result;
		}
		String nomeNaoFiscal = "NomeNaoFiscal=";
		if ( AbstractECFDriver.SANGRIA.equals( opt ) ) {
			nomeNaoFiscal += formatString( "SANGRIA", 19 );
		}
		else if ( AbstractECFDriver.SUPRIMENTO.equals( opt ) ) {
			nomeNaoFiscal += formatString( "SUPRIMENTO", 19 );
		}
		else {
			nomeNaoFiscal += formatString( opt, 19 );
		}
		String pValor = "Valor=" + formatFloat( valor, 15, 2 );
		result = executa( "EmiteItemNaoFiscal", nomeNaoFiscal, pValor );
		if ( STResult.getInstanceOk() != result ) {
			return result;
		}
		String meioPagamento = "CodMeioPagamento=" + programaFormaPagamento( formaPag );
		result = executa( "PagaCupom", pValor, meioPagamento );
		if ( STResult.getInstanceOk() != result ) {
			return result;
		}
		result = executa( "EncerraDocumento" );
		return result;
	}

	public STResult abreComprovanteNFiscalVinculado( final String meioPagamento, final float vlr, final int doc ) {

		String nomeMeioPagamento = "NomeMeioPagamento=" + formatString( meioPagamento, 16 );
		String valor = "Valor=" + formatFloat( vlr, 15, 2 );
		String coo = "COO=" + formatInteger( doc, 6 );
		return executa( "AbreCreditoDebito", nomeMeioPagamento, valor, coo );
	}

	public STResult usaComprovanteNFiscalVinculado( final String texto ) {

		return executa( "ImprimeTexto", "TextoLivre=" + formatString( texto, 492 ) );
	}

	public STResult autenticacaoDeDocumento() {

		return STResult.getInstanceNotImplemented();
	}

	public STResult programaCaracterParaAutenticacao( final int[] caracteres ) {

		return STResult.getInstanceNotImplemented();
	}

	public STResult acionaGavetaDinheiro( final int time ) {

		String tempAcionamentoGaveta = "TempoAcionamentoGaveta=" + formatInteger( time, 8 );
		return executa( "AbreGaveta", tempAcionamentoGaveta );
	}

	public String resultEstadoGavetaDinheiro() {

		return null;
	}

	public String getStatus() {

		// STResult result = executa( "LeInteiro", "NomeInteiro=\"EstadoGeralECF\"" );
		return "";
	}

	public String resultAliquotas() {

		StringBuilder aliquotas = new StringBuilder();
		STResult result = null;
		String[] aq = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15" };
		for ( String a : aq ) {
			result = executa( "LeAliquota", "CodAliquotaProgramavel=" + a );
			if ( STResult.getInstanceOk() == result ) {
				String aliquota = getRetornos().get( "PercentualAliquota" );
				if ( aliquota != null ) {
					aliquotas.append( strZero( aliquota.replace( ",", "" ), 4 ) );
				}
			}
			else {
				aliquotas.append( strZero( "0", 4 ) );
			}
		}
		return aliquotas.toString();
	}

	public String resultTotalizadoresParciais() {

		StringBuilder totalizadores = new StringBuilder();
		String parciaisTributados = parciaisTributados();
		String isencao = "0,00";
		String naoInsidencia = "0,00";
		String substituicao = "0,00";
		String naoSujeitosAIcms = naoSujeitosAIcms();
		String sangria = "0,00";
		String suprimento = "0,00";
		String grandeTotal = "0,00";
		STResult result = executa( "LeMoeda", "NomeDadoMonetario=\"TotalDiaIsencaoICMS\"" );
		if ( STResult.getInstanceOk() == result ) {
			isencao = getRetornos().get( "ValorMoeda" );
		}
		result = executa( "LeMoeda", "NomeDadoMonetario=\"TotalDiaNaoTributadoICMS\"" );
		if ( STResult.getInstanceOk() == result ) {
			naoInsidencia = getRetornos().get( "ValorMoeda" );
		}
		result = executa( "LeMoeda", "NomeDadoMonetario=\"TotalDiaSubstituicaoTributariaICMS\"" );
		if ( STResult.getInstanceOk() == result ) {
			substituicao = getRetornos().get( "ValorMoeda" );
		}
		sangria = naoSujeitosAIcms.substring( 0, 14 );
		suprimento = naoSujeitosAIcms.substring( 14, 28 );
		result = executa( "LeMoeda", "NomeDadoMonetario=\"GT\"" );
		if ( STResult.getInstanceOk() == result ) {
			grandeTotal = getRetornos().get( "ValorMoeda" );
		}
		totalizadores.append( parciaisTributados ).append( "," );
		totalizadores.append( isencao ).append( "," );
		totalizadores.append( naoInsidencia ).append( "," );
		totalizadores.append( substituicao ).append( "," );
		totalizadores.append( naoSujeitosAIcms ).append( "," );
		totalizadores.append( sangria ).append( "," );
		totalizadores.append( suprimento ).append( "," );
		totalizadores.append( grandeTotal );
		return totalizadores.toString();
	}

	private String parciaisTributados() {

		StringBuilder aliquotas = new StringBuilder();
		STResult result = null;
		String[] aq = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15" };
		for ( String a : aq ) {
			result = executa( "LeMoeda", "NomeDadoMonetario=\"TotalDiaValorAliquota[" + a + "]\"" );
			if ( STResult.getInstanceOk() == result ) {
				String aliquota = getRetornos().get( "ValorMoeda" );
				if ( aliquota != null ) {
					aliquotas.append( strZero( aliquota.substring( 0, aliquota.length() - 2 ).replace( ",", "" ), 14 ) );
				}
			}
			else {
				aliquotas.append( strZero( "0", 14 ) );
			}
		}
		return aliquotas.toString();
	}

	private String naoSujeitosAIcms() {

		StringBuilder naofiscais = new StringBuilder();
		STResult result = null;
		String[] aq = { "00", "01", "02", "03", "04", "05", "06", "07", "08" };
		for ( String a : aq ) {
			result = executa( "LeMoeda", "NomeDadoMonetario=\"TotalDiaNaoFiscal[" + a + "]\"" );
			if ( STResult.getInstanceOk() == result ) {
				String naofiscal = getRetornos().get( "ValorMoeda" );
				if ( naofiscal != null ) {
					naofiscais.append( strZero( naofiscal.substring( 0, naofiscal.length() - 2 ).replace( ",", "" ), 14 ) );
				}
			}
			else {
				naofiscais.append( strZero( "0", 14 ) );
			}
		}
		return naofiscais.toString();
	}

	public String resultSubTotal() {

		return null;
	}

	public String resultNumeroCupom() {

		String coo = "000000";
		STResult result = executa( "LeInteiro", "NomeInteiro=\"coo\"" );
		if ( STResult.getInstanceOk() == result ) {
			coo = getRetornos().get( "ValorInteiro" );
		}
		return coo;
	}

	public boolean resultDocumentoAberto() {

		boolean aberto = false;
		STResult result = executa( "LeIndicador", "NomeIndicador=\"DocumentoAberto\"" );
		if ( STResult.getInstanceOk() == result ) {
			aberto = ( 1 == Integer.parseInt( getRetornos().get( "ValorNumericoIndicador" ) ) );
		}
		return aberto;
	}

	public String resultVariaveis( final char var ) {

		STResult result = null;
		String variavel = null;
		boolean texto = false;
		boolean inteiro = false;
		boolean moeda = false;
		boolean data = false;
		if ( V_DT_ULT_REDUCAO == var ) {
			variavel = resultUltimaReducao();
		}
		else if ( V_REDUCOES == var ) {
			result = executa( "LeInteiro", "NomeInteiro=\"CRZ\"" );
			inteiro = true;
		}
		else if ( V_NUM_CAIXA == var ) {
			result = executa( "LeInteiro", "NomeInteiro=\"ECF\"" );
			inteiro = true;
		}
		else if ( V_CUPONS_CANC == var ) {
			result = executa( "LeInteiro", "NomeInteiro=\"CFC\"" );
			inteiro = true;
		}
		else if ( V_CANCELAMENTOS == var ) {
			result = executa( "LeMoeda", "NomeDadoMonetario=\"TotalDiaCancelamentoItens\"" );
			moeda = true;
		}
		else if ( V_DESCONTOS == var ) {
			result = executa( "LeMoeda", "NomeDadoMonetario=\"TotalDiaDescontosItens\"" );
			moeda = true;
		}
		if ( STResult.getInstanceOk() == result ) {
			if ( texto ) {
				variavel = getRetornos().get( "ValorTexto" );
			}
			else if ( inteiro ) {
				variavel = getRetornos().get( "ValorInteiro" );
			}
			else if ( moeda ) {
				variavel = getRetornos().get( "ValorMoeda" );
				variavel = variavel.replace( ",", "." );
			}
			else if ( data ) {
				variavel = getRetornos().get( "ValorData" );
			}
		}
		return variavel;
	}

	public String resultEstadoPapel() {

		return null;
	}

	public String resultUltimaReducao() {

		STResult result = executa( "LeTexto", "NomeTexto=\"DadosUltimaReducaoZ\"" );
		String ultimaReducao = null;
		if ( STResult.getInstanceOk() == result ) {
			ultimaReducao = getRetornos().get( "ValorTexto" );
			ultimaReducao = ultimaReducao.substring( 582, 588 );
		}
		return ultimaReducao;
	}

	public STResult alteraSimboloMoeda( final String simbolo ) {

		String nome = "NomeTexto=\"SimboloMoeda\"";
		String valor = "ValorTexto=" + formatString( simbolo, 4 );
		return executa( "EscreveTexto", nome, valor );
	}

	public STResult programaMoedaSingular( final String nomeSingular ) {

		String nome = "NomeTexto=\"NomeMoedaSingular\"";
		String valor = "ValorTexto=" + formatString( nomeSingular, 19 );
		return executa( "EscreveTexto", nome, valor );
	}

	public STResult programaMoedaPlural( final String nomePlurar ) {

		String nome = "NomeTexto=\"NomeMoedaPlural\"";
		String valor = "ValorTexto=" + formatString( nomePlurar, 22 );
		return executa( "EscreveTexto", nome, valor );
	}

	public String resultStatusCheque() {

		return null;
	}

	public STResult cancelaImpressaoCheque() {

		return STResult.getInstanceNotImplemented();
	}

	public STResult imprimeCheque( final float valor, final String favorecido, final String localidade, final int dia, final int mes, final int ano ) {

		return STResult.getInstanceNotImplemented();
	}

	public void aguardaImpressao() {

		return;
	}

	public STResult habilitaCupomAdicional( final char opt ) {

		return STResult.getInstanceNotImplemented();
	}

	public STResult resetErro() {

		return STResult.getInstanceNotImplemented();
	}
}
