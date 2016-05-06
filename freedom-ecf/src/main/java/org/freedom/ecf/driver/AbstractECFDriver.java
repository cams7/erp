
package org.freedom.ecf.driver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.comm.ParallelPortEvent;
import javax.comm.ParallelPortEventListener;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import org.freedom.ecf.layout.AbstractLayout;
import org.freedom.infra.comm.AbstractPort;
import org.freedom.infra.comm.CtrlPort;
import org.freedom.infra.comm.SerialParams;
import org.freedom.infra.functions.SystemFunctions;

/**
 * @version 05/04/2006 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)AbstractECFDriver.java <BR>
 * 
 *         Este programa é licenciado de acordo com a LGPL (Lesser General Public License), <BR>
 *         versão 2.1, Fevereiro de 1999 <BR>
 *         A LGPL deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste Programa. <BR>
 *         Caso uma cópia da LGPL não esteja disponível junto com este Programa, você pode contatar <BR>
 *         o LICENCIADOR ou então pegar uma cópia em: <a href=http://creativecommons.org/licenses/LGPL/2.1/legalcode.pt> Creative Commons</a> <BR>
 *         Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa é preciso estar de acordo com os termos da LGPL. <BR>
 * <BR>
 * @author Robson Sanchez/Setpoint Informática Ltda. <BR>
 *         criada: 05/04/2006. <BR>
 * <BR>
 */
public abstract class AbstractECFDriver implements SerialPortEventListener, ParallelPortEventListener {

	protected static int TIMEOUT_READ = 30000;

	public static final byte STX = 2;

	public static final byte ENQ = 5;

	public static final byte ACK = 6;

	public static final byte NAK = 21;

	public static final byte ESC = 27;

	public static final byte GS = 29;

	public static final int TIMEOUT_ACK = 500;

	public static final char ACRECIMO_PERC = 'A';

	public static final char ACRECIMO_VALOR = 'a';

	public static final char DESCONTO_PERC = 'D';

	public static final char DESCONTO_VALOR = 'd';

	public static final char QTD_INTEIRO = 'I';

	public static final char QTD_DECIMAL = 'D';

	public static final String ALQ_ISENTA = "II";

	public static final String ALQ_SUBSTITUICAO = "FF";

	public static final String ALQ_NAO_INSIDE = "NN";

	public static final String ALQ_INTEGRAL = "TT";

	public static final char IMPRESSAO = 'I';

	public static final char SERIAL = 'S';

	public static final char result = 'R';

	public static final char ICMS = '0';

	public static final char ISS = '1';

	public static final char TRUNCA = '0';

	public static final char ARREDONDA = '1';

	public static final char DES_CUPOM_ADIC = '0';

	public static final char HAB_CUPOM_ADIC = '1';

	public static final char V_NUM_SERIE = 0;

	public static final char V_VER_FIRMWARE = 1;

	public static final char V_CNPJ_IE = 2;

	public static final char V_GRANDE_TOTAL = 3;

	public static final char V_CANCELAMENTOS = 4;

	public static final char V_DESCONTOS = 5;

	public static final char V_CONT_SEQ = 6;

	public static final char V_OP_N_FISCAIS = 7;

	public static final char V_CUPONS_CANC = 8;

	public static final char V_REDUCOES = 9;

	public static final char V_NUM_INT_TEC = 10;

	public static final char V_NUM_SUB_PROP = 11;

	public static final char V_NUM_ULT_ITEM = 12;

	public static final char V_CLICHE = 13;

	public static final char V_NUM_CAIXA = 14;

	public static final char V_NUM_LOJA = 15;

	public static final char V_MOEDA = 16;

	public static final char V_FLAG_FISCAL = 17;

	public static final char V_TMP_LIGADA = 18;

	public static final char V_TMP_IMPRIMNDO = 19;

	public static final char V_FLAG_TEC = 20;

	public static final char V_FLAG_EPROM = 21;

	public static final char V_VLR_ULT_CUPOM = 22;

	public static final char V_DT_HORA = 23;

	public static final char V_TOT_NICMS = 24;

	public static final char V_DESC_TOT_NICMS = 25;

	public static final char V_DT_ULT_REDUCAO = 26;

	public static final char V_DT_MOVIMENTO = 27;

	public static final char V_FLAG_TRUNCA = 28;

	public static final char V_FLAG_VINC_ISS = 29;

	public static final char V_TOT_ACRECIMOS = 30;

	public static final char V_CONT_BILHETE = 31;

	public static final char V_FORMAS_PAG = 32;

	public static final char V_CNF_NVINCULADO = 33;

	public static final char V_DEPARTAMENTOS = 34;

	public static final char V_TIPO_IMP = 253;

	public static final String SUPRIMENTO = "SU";

	public static final String SANGRIA = "SA";

	public static int DUAS_CASAS_DECIMAIS = 2;

	public static int TRES_CASAS_DECIMAIS = 3;

	private static byte[] bytesLidos = new byte[ 3 ];

	private static byte[] buffer = null;

	private static boolean leuEvento = false;

	private boolean outputWrite;

	protected AbstractLayout objLayoutNFiscal = null;

	protected String porta;

	protected SerialPort portaSerial = null;

	protected SerialParams serialParams = new SerialParams();

	protected boolean fiscal = true;

	public AbstractECFDriver() {

		Locale.setDefault( new Locale( "pt", "BR" ) );
	}

	public void setLayoutNFiscal( final AbstractLayout layoutNFiscal ) {

		objLayoutNFiscal = layoutNFiscal;
	}

	public void setLayoutNFiscal( final String layoutNFiscal ) {

		if ( layoutNFiscal != null ) {
			try {
				Class<?> layoutClass = Class.forName( layoutNFiscal );
				try {
					setLayoutNFiscal( (AbstractLayout) layoutClass.newInstance() );
				}
				catch ( InstantiationException e ) {
					e.printStackTrace();
				}
				catch ( IllegalAccessException e ) {
					e.printStackTrace();
				}
			}
			catch ( ClassNotFoundException e ) {
				e.printStackTrace();
			}
		}
	}

	public byte[] adicBytes( final byte[] variavel, final byte[] incremental ) {

		byte[] result = new byte[ variavel.length + incremental.length ];
		for ( int i = 0; i < result.length; i++ ) {
			if ( i < variavel.length ) {
				result[ i ] = variavel[ i ];
			}
			else {
				result[ i ] = incremental[ i - variavel.length ];
			}
		}
		return result;
	}

	public boolean activePort( final String portstr ) {

		return activePort( AbstractPort.convPorta( portstr ) );
	}

	public boolean activePort( final int com ) {

		boolean result = CtrlPort.getInstance().isActive();
		if ( !result ) {
			result = CtrlPort.getInstance().activePort( com, serialParams, this );
		}
		return result;
	}

	public void setBytesLidos( final byte[] arg ) {

		bytesLidos = new byte[ arg.length ];
		System.arraycopy( arg, 0, bytesLidos, 0, bytesLidos.length );
	}

	public byte[] getBytesLidos() {

		final byte[] result = new byte[ bytesLidos.length ];
		System.arraycopy( bytesLidos, 0, result, 0, result.length );
		return result;
	}

	public void parallelEvent( ParallelPortEvent event ) {

		byte[] result = null;
		byte[] bufferTmp = null;
		byte[] tmp = null;
		InputStream input = null;
		input = CtrlPort.getInstance().getInput();
		try {
			if ( event.getEventType() == SerialPortEvent.DATA_AVAILABLE ) {
				result = new byte[ input.available() ];
				if ( result != null ) {
					input.read( result );
					if ( buffer == null ) {
						bufferTmp = result;
					}
					else {
						leuEvento = true;
						tmp = buffer;
						bufferTmp = new byte[ tmp.length + result.length ];
						for ( int i = 0; i < bufferTmp.length; i++ ) {
							if ( i < tmp.length ) {
								bufferTmp[ i ] = tmp[ i ];
							}
							else {
								bufferTmp[ i ] = result[ i - tmp.length ];
							}
						}
					}
					buffer = bufferTmp;
				}
			}
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}
	}

	public void serialEvent( final SerialPortEvent event ) {

		byte[] result = null;
		byte[] bufferTmp = null;
		byte[] tmp = null;
		InputStream input = null;
		input = CtrlPort.getInstance().getInput();
		try {
			if ( event.getEventType() == SerialPortEvent.DATA_AVAILABLE ) {
				result = new byte[ input.available() ];
				if ( result != null ) {
					input.read( result );
					if ( buffer == null ) {
						bufferTmp = result;
					}
					else {
						leuEvento = true;
						tmp = buffer;
						bufferTmp = new byte[ tmp.length + result.length ];
						for ( int i = 0; i < bufferTmp.length; i++ ) {
							if ( i < tmp.length ) {
								bufferTmp[ i ] = tmp[ i ];
							}
							else {
								bufferTmp[ i ] = result[ i - tmp.length ];
							}
						}
					}
					buffer = bufferTmp;
				}
			}
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}
	}

	public byte[] enviaCmd( final byte[] CMD ) {

		return enviaCmd( CMD, 0 );
	}

	public byte[] enviaCmd( final byte[] CMD, final int tamEsperado ) {

		return enviaCmd( CMD, CtrlPort.getInstance().getPortnsel(), tamEsperado );
	}

	public byte[] enviaCmd( final byte[] CMD, final int com, final int tamresult ) {

		long tempo = 0;
		long tempoAtual = 0;
		boolean isserial = AbstractPort.isSerial( com );
		leuEvento = false;
		buffer = null;
		if ( activePort( com ) ) {
			try {
				tempo = System.currentTimeMillis();
				outputWrite = false;
				Thread tee = new Thread( new Runnable() {

					public void run() {

						writeOutput( CMD );
					}
				} );
				tee.start();
				tee.join( TIMEOUT_READ );
				if ( !outputWrite ) {
					tee.interrupt();
					return null;
				}
				if ( isserial ) {
					do {
						Thread.sleep( TIMEOUT_ACK );
						tempoAtual = System.currentTimeMillis();
					} while ( ( tempoAtual - tempo ) < ( TIMEOUT_READ ) && ( buffer == null || buffer.length < tamresult || !leuEvento ) );
				}
			}
			catch ( InterruptedException e ) {
				e.printStackTrace();
			}
		}
		return buffer;
	}

	private void writeOutput( final byte[] CMD ) {

		try {
			final OutputStream output = CtrlPort.getInstance().getOutput();
			output.flush();
			output.write( CMD );
			closeOutput();
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}
	}

	private void closeOutput() {

		try {
			final OutputStream output = CtrlPort.getInstance().getOutput();
			output.close();
			outputWrite = true;
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}
	}

	public String parseParam( final String param, final int tamanho, final boolean terminador ) {

		final StringBuffer tmp = new StringBuffer();
		if ( param != null ) {
			if ( terminador ) {
				String stmp = param;
				if ( stmp.length() >= tamanho ) {
					stmp = ( stmp.substring( 0, param.length() - 2 ) );
				}
				tmp.append( stmp + ( AbstractPort.OS_LINUX == SystemFunctions.getOS() ? (char) 13 : (char) 10 ) );
				// tmp.append( (char) 10 + stmp + (char) 10 );
			}
			else {
				tmp.append( parseParam( param, tamanho ) );
			}
		}
		return tmp.toString();
	}

	public String parseParam( final String param, final int tamanho ) {

		final StringBuffer tmp = new StringBuffer();
		if ( param != null ) {
			if ( tamanho < param.length() ) {
				tmp.append( param.substring( 0, tamanho ) );
			}
			else {
				tmp.append( param );
				tmp.append( replicate( " ", tamanho - param.length() ) );
			}
		}
		return tmp.toString();
	}

	public String parseParam( final int param, final int tamanho ) {

		return strZero( String.valueOf( param ), tamanho );
	}

	public String parseParam( final char param ) {

		return String.valueOf( param );
	}

	public String parseParam( final float param, final int tamanho, final int casasdec ) {

		return floatToString( param, tamanho, casasdec );
	}

	public String parseParam( final Date param ) {

		final SimpleDateFormat sdf = new SimpleDateFormat( "ddMMyy", Locale.getDefault() );
		return sdf.format( param ).trim();
	}

	public String replicate( final String texto, final int quant ) {

		final StringBuffer sresult = new StringBuffer();
		for ( int i = 0; i < quant; i++ ) {
			sresult.append( texto );
		}
		return sresult.toString();
	}

	public String floatToString( final float param, final int tamanho, final int casasdec ) {

		final StringBuffer str = new StringBuffer();
		final char[] strTochar = String.valueOf( param ).toCharArray();
		final int index = String.valueOf( param ).indexOf( "." );
		for ( int i = 0; i < index; i++ ) {
			str.append( strTochar[ i ] );
		}
		for ( int i = 0; i < casasdec; i++ ) {
			if ( ( index + i ) + 1 < strTochar.length ) {
				str.append( strTochar[ ( index + i ) + 1 ] );
			}
			else {
				str.append( 0 );
			}
		}
		return strZero( str.toString(), tamanho );
	}

	public String strZero( final String val, final int zeros ) {

		String str = val;
		if ( str == null ) {
			str = "";
		}
		final StringBuffer sresult = new StringBuffer();
		sresult.append( replicate( "0", zeros - str.trim().length() ) );
		sresult.append( str.trim() );
		return sresult.toString();
	}

	public boolean isFiscal() {

		return fiscal;
	}

	public abstract byte[] preparaCmd( byte[] CMD );

	public abstract STResult executaCmd( byte[] CMD, int tamresult );

	public abstract STResult checkResult( byte[] bytes );

	public abstract STResult aberturaDeCupom();// 0

	public abstract STResult aberturaDeCupom( String cnpj );// 0

	public abstract STResult alteraSimboloMoeda( String simbolo );// 1

	public abstract STResult leituraX();// 5

	public abstract STResult reducaoZ();// 6

	public abstract STResult adicaoDeAliquotaTriburaria( String aliq, char opt );// 7

	public abstract STResult leituraMemoriaFiscal( Date dataIni, Date dataFim, char tipo );// 8

	public abstract STResult leituraMemoriaFiscal( int ini, int fim, char tipo );// 8

	public abstract STResult vendaItem( String codProd, String descProd, String aliquota, char tpqtd, float qtd, float valor, char tpdesc, float desconto );// 9

	public abstract STResult cancelaItemAnterior();// 13

	public abstract STResult cancelaCupom();// 14

	public abstract STResult autenticacaoDeDocumento();// 16

	public abstract STResult programaHorarioVerao();// 18

	public abstract boolean isHorarioVerao();// 18

	public abstract String getStatus();// 19;

	public abstract STResult relatorioGerencial( String texto );// 20

	public abstract STResult fechamentoRelatorioGerencial();// 21

	public abstract STResult acionaGavetaDinheiro( int time );// 22

	public abstract String resultEstadoGavetaDinheiro();// 23

	public abstract STResult comprovanteNFiscalNVinculado( String opt, float valor, String formaPag );// 25

	public abstract String resultAliquotas();// 26

	public abstract String resultTotalizadoresParciais();// 27

	public abstract String resultSubTotal();// 29

	public abstract String resultNumeroCupom();// 30

	public abstract boolean resultDocumentoAberto();

	public abstract STResult cancelaItemGenerico( int item );// 31

	public abstract STResult iniciaFechamentoCupom( char opt, float percentual );// 32

	public abstract STResult finalizaFechamentoCupom( String mensagem );// 34

	public abstract String resultVariaveis( char var );// 35

	public abstract STResult programaTruncamentoArredondamento( char opt );// 39

	public abstract STResult nomeiaTotalizadorNaoSujeitoICMS( int indice, String desc );// 40

	public abstract STResult vendaItemTresCasas( String codProd, String descProd, String aliquota, char tpqtd, float qtd, float valor, char tpdesc, float desconto );// 56

	public abstract STResult imprimeCheque( final float valor, final String favorecido, final String localidade, final int dia, final int mes, final int ano ); // 57

	public abstract STResult programaMoedaSingular( String nomeSingular );// 58

	public abstract STResult programaMoedaPlural( String nomePlurar );// 59

	public abstract STResult programarEspacoEntreLinhas( int espaco );// 60

	public abstract STResult programarLinhasEntreCupons( int espaco );// 61

	public abstract String resultStatusCheque();// 62 48

	public abstract STResult cancelaImpressaoCheque();// 62 49

	public abstract STResult programaUnidadeMedida( String descUnid );// 62 51

	public abstract STResult aumentaDescItem( String descricao );// 62 52

	public abstract String resultEstadoPapel();// 62 54

	public abstract String resultUltimaReducao();// 62 55

	public abstract STResult vendaItemDepartamento( String sitTrib, float valor, float qtd, float desconto, float acrescimo, int departamento, String unidade, String codProd, String descProd );// 63

	public abstract STResult programaCaracterParaAutenticacao( int[] caracteres );// 64

	public abstract STResult nomeiaDepartamento( int index, String descricao );// 65

	public abstract STResult abreComprovanteNFiscalVinculado( String formaPag, float valor, int doc );// 66

	public abstract STResult usaComprovanteNFiscalVinculado( String texto );// 67

	public abstract STResult habilitaCupomAdicional( char opt );// 68

	public abstract STResult leituraXSerial();// 69

	public abstract STResult resetErro();// 70

	public abstract String programaFormaPagamento( String descricao );// 71

	public abstract STResult efetuaFormaPagamento( String indice, float valor, String descForma );// 72

	public abstract STResult estornoFormaPagamento( String descOrigem, String descDestino, float valor );// 74
}
