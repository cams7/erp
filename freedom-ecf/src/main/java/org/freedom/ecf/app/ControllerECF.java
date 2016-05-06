
package org.freedom.ecf.app;

import static org.freedom.ecf.app.EParametro.PARAM_ACRECIMO;
import static org.freedom.ecf.app.EParametro.PARAM_ALIQUOTA;
import static org.freedom.ecf.app.EParametro.PARAM_CODIGO;
import static org.freedom.ecf.app.EParametro.PARAM_DEPARTAMENTO;
import static org.freedom.ecf.app.EParametro.PARAM_DESCONTO;
import static org.freedom.ecf.app.EParametro.PARAM_DESCRICAO;
import static org.freedom.ecf.app.EParametro.PARAM_DOCUMENTO;
import static org.freedom.ecf.app.EParametro.PARAM_FORMA_PAGAMENTO;
import static org.freedom.ecf.app.EParametro.PARAM_QUANTIDADE;
import static org.freedom.ecf.app.EParametro.PARAM_UNIDADE_MEDIDA;
import static org.freedom.ecf.app.EParametro.PARAM_VALOR_UNITARIO;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.freedom.ecf.driver.AbstractECFDriver;
import org.freedom.ecf.driver.STResult;
import org.freedom.ecf.layout.AbstractLayout;
import org.freedom.infra.comm.AbstractPort;

public class ControllerECF {

	private boolean modoDemostracao;

	private AbstractECFDriver ecf;

	private List<String> aliquotas;

	private String messageLog;

	private Logger logger;

	/**
	 * Contrutor de classe.<br>
	 * Invoca o construtor Control( String, int, boolean )<br>
	 * com a porta default Serial.COM1 e o estado de modo demostração false.<br>
	 * 
	 * @see org.freedom.infra.comm.Serial#COM1
	 * @param ecfdriver
	 *            nome do driver de comunicação serial.
	 * @throws IllegalArgumentException
	 *             caso o nome do driver seja invalido.
	 * @throws NullPointerException
	 *             caso o driver não consiga ser instânciado.
	 */
	public ControllerECF( final String ecfdriver ) throws IllegalArgumentException, NullPointerException {

		this( ecfdriver, AbstractPort.COM1, false );
	}

	/**
	 * Contrutor de classe.<br>
	 * Invoca o construtor Control( String, int, boolean )<br>
	 * com a porta default Serial.COM1<br>
	 * 
	 * @see org.freedom.infra.comm.Serial#COM1
	 * @param ecfdriver
	 *            nome do driver de comunicação serial.
	 * @param mododemostracao
	 *            estado de modo demostração.
	 * @throws IllegalArgumentException
	 *             caso o nome do driver seja invalido.
	 * @throws NullPointerException
	 *             caso o driver não consiga ser instânciado.
	 */
	public ControllerECF( final String ecfdriver, final boolean mododemostracao ) throws IllegalArgumentException, NullPointerException {

		this( ecfdriver, AbstractPort.COM1, mododemostracao );
	}

	/**
	 * Contrutor de classe.<br>
	 * Invoca o construtor Control( String, int, boolean )<br>
	 * com o estado de modo demostração false.<br>
	 * 
	 * @param ecfdriver
	 *            nome do driver de comunicação serial.
	 * @param porta
	 *            porta serial.
	 * @throws IllegalArgumentException
	 *             caso o nome do driver seja invalido.
	 * @throws NullPointerException
	 *             caso o driver não consiga ser instânciado.
	 */
	public ControllerECF( final String ecfdriver, final int porta ) throws IllegalArgumentException, NullPointerException {

		this( ecfdriver, porta, false );
	}

	/**
	 * Contrutor de classe.<br>
	 * Invoca o construtor Control( String, int, boolean )<br>
	 * com o estado de modo demostração false e convertendo o nome da porta para a constante indicativa.<br>
	 * 
	 * @see org.freedom.infra.comm.Serial#convPort(int)
	 * @param ecfdriver
	 *            nome do driver de comunicação serial.
	 * @param porta
	 *            nome da porta serial.
	 * @throws IllegalArgumentException
	 *             caso o nome do driver seja invalido.
	 * @throws NullPointerException
	 *             caso o driver não consiga ser instânciado.
	 */
	public ControllerECF( final String ecfdriver, final String porta ) throws IllegalArgumentException, NullPointerException {

		this( ecfdriver, AbstractPort.convPorta( porta ), false );
	}

	/**
	 * Contrutor de classe.<br>
	 * Invoca o construtor Control( String, int, boolean )<br>
	 * convertendo o nome da porta para a constante indicativa.<br>
	 * 
	 * @see org.freedom.infra.comm.Serial#convPort(int)
	 * @param ecfdriver
	 *            nome do driver de comunicação serial.
	 * @param porta
	 *            nome da porta serial.
	 * @param mododemostracao
	 *            estado de modo demostração.
	 * @param layout
	 * @throws IllegalArgumentException
	 *             caso o nome do driver seja invalido.
	 * @throws NullPointerException
	 *             caso o driver não consiga ser instânciado.
	 */
	public ControllerECF( final String ecfdriver, final String porta, final boolean mododemostracao, final AbstractLayout layout ) throws IllegalArgumentException, NullPointerException {

		this( ecfdriver, AbstractPort.convPorta( porta ), mododemostracao );
		setLayoutNFiscal( layout );
	}

	/**
	 * Contrutor de classe.<br>
	 * Valida o nome do driver de comunicação serial e instancia a classe com este nome<br>
	 * abrindo a porta serial e definindo o estado de modo demonstração.<br>
	 * 
	 * @param ecfdriver
	 *            nome do driver de comunicação serial.
	 * @param porta
	 *            porta serial
	 * @param mododemostracao
	 *            estado de modo demostração.
	 * @throws IllegalArgumentException
	 *             caso o nome do driver seja invalido.
	 * @throws NullPointerException
	 *             caso o driver não consiga ser instânciado.
	 */
	public ControllerECF( final String ecfdriver, final int porta, final boolean mododemostracao ) throws IllegalArgumentException, NullPointerException {

		this( ecfdriver, porta, mododemostracao, null );
	}

	/**
	 * Contrutor de classe.<br>
	 * Valida o nome do driver de comunicação serial e instancia a classe com este nome<br>
	 * abrindo a porta serial e definindo o estado de modo demonstração.<br>
	 * 
	 * @param ecfdriver
	 *            nome do driver de comunicação serial.
	 * @param porta
	 *            porta serial
	 * @param mododemostracao
	 *            estado de modo demostração.
	 * @param layout
	 * 
	 * @throws IllegalArgumentException
	 *             caso o nome do driver seja invalido.
	 * @throws NullPointerException
	 *             caso o driver não consiga ser instânciado.
	 */
	public ControllerECF( final String ecfdriver, final int porta, final boolean mododemostracao, final AbstractLayout layout ) throws IllegalArgumentException, NullPointerException {

		try {
			// logger = LoggerManager.getLogger( "log/freedomECF.log" ); ANDERSON IMPLEMENTAR NOVO LOGGER
		}
		catch ( RuntimeException e ) {
			e.printStackTrace();
		}
		setModoDemostracao( mododemostracao );
		if ( notIsModoDemostracao() ) {
			if ( ecfdriver == null || ecfdriver.trim().length() == 0 ) {
				throw new IllegalArgumentException( "Driver de impressora fiscal invalido." );
			}
			try {
				Object obj = Class.forName( ecfdriver.trim() ).newInstance();
				if ( obj instanceof AbstractECFDriver ) {
					this.ecf = (AbstractECFDriver) obj;
					this.ecf.activePort( porta > 0 ? porta : AbstractPort.COM1 );
				}
			}
			catch ( ClassNotFoundException e ) {
				e.printStackTrace();
			}
			catch ( InstantiationException e ) {
				e.printStackTrace();
			}
			catch ( IllegalAccessException e ) {
				e.printStackTrace();
			}
			if ( this.ecf == null ) {
				throw new NullPointerException( "Não foi possível carregar o driver da impressora.\n" + ecfdriver );
			}
		}
		setLayoutNFiscal( layout );
	}

	public void setModoDemostracao( final boolean modoDemostracao ) {

		this.modoDemostracao = modoDemostracao;
	}

	public boolean isModoDemostracao() {

		return this.modoDemostracao;
	}

	public boolean notIsModoDemostracao() {

		return !isModoDemostracao();
	}

	public void setLayoutNFiscal( final String layout ) {

		if ( this.ecf != null && layout != null ) {
			this.ecf.setLayoutNFiscal( layout );
		}
	}

	public void setLayoutNFiscal( final AbstractLayout layout ) {

		if ( this.ecf != null && layout != null ) {
			this.ecf.setLayoutNFiscal( layout );
		}
	}

	public void setMessageLog( final String message ) {

		this.messageLog = message;
	}

	public String getMessageLog() {

		return this.messageLog;
	}

	public boolean decodeReturn( final STResult strresult ) {

		boolean returnOfAction = false;
		if ( strresult != null ) {
			returnOfAction = !strresult.isInError();
			if ( strresult.isInError() ) {
				setMessageLog( strresult.getMessages() );
			}
		}
		return returnOfAction;
	}

	// Comandos ...
	public boolean programaAliquota( BigDecimal aliquota, final char tipoaliquota ) {

		boolean returnOfAction = true;
		if ( notIsModoDemostracao() ) {
			if ( aliquota == null || aliquota.floatValue() < 0.01f ) {
				returnOfAction = false;
				setMessageLog( "Aliquota inválida.[" + aliquota + "]" );
			}
			else {
				aliquota = aliquota.setScale( 2, BigDecimal.ROUND_HALF_UP );
				String sAliquota = ecf.parseParam( aliquota.floatValue(), 4, 2 );
				returnOfAction = decodeReturn( ecf.adicaoDeAliquotaTriburaria( sAliquota, tipoaliquota ) );
			}
			if ( !returnOfAction ) {
				whiterLogError( "[PROGRAMA ALIQUOTA] " );
			}
		}
		return returnOfAction;
	}

	public boolean programaMoeda( final String sigla, final String singular, final String plural ) {

		boolean returnOfAction = true;
		if ( notIsModoDemostracao() ) {
			if ( sigla == null || sigla.trim().length() == 0 ) {
				returnOfAction = false;
				setMessageLog( "Sigla inválida.[" + sigla + "]" );
			}
			else if ( singular == null || singular.trim().length() == 0 ) {
				returnOfAction = false;
				setMessageLog( "Descrição singular inválida.[" + singular + "]" );
			}
			else if ( plural == null || plural.trim().length() == 0 ) {
				returnOfAction = false;
				setMessageLog( "Descrição plural inválida.[" + plural + "]" );
			}
			else {
				returnOfAction = decodeReturn( ecf.alteraSimboloMoeda( sigla.replace( '$', ' ' ) ) );
				if ( returnOfAction ) {
					returnOfAction = decodeReturn( ecf.programaMoedaSingular( singular ) );
					if ( returnOfAction ) {
						returnOfAction = decodeReturn( ecf.programaMoedaPlural( plural ) );
					}
				}
			}
			if ( !returnOfAction ) {
				whiterLogError( "[PROGRAMA MOEDA] " );
			}
		}
		return returnOfAction;
	}

	public boolean setHorarioDeVerao( final boolean horariodeverao ) {

		boolean returnOfAction = true;
		if ( notIsModoDemostracao() ) {
			if ( ( !horariodeverao && ecf.isHorarioVerao() ) || ( horariodeverao && !ecf.isHorarioVerao() ) ) {
				returnOfAction = decodeReturn( ecf.programaHorarioVerao() );
			}
			if ( !returnOfAction ) {
				whiterLogError( "[PROGRAMA HORÁRIO DE VERÃO] " );
			}
		}
		return returnOfAction;
	}

	public boolean nomeiaDepartamento( final int index, final String departamento ) {

		boolean returnOfAction = true;
		if ( notIsModoDemostracao() ) {
			returnOfAction = decodeReturn( ecf.nomeiaDepartamento( index, departamento ) );
			if ( !returnOfAction ) {
				whiterLogError( "[NOMEIA DEPARTAMENTO] " );
			}
		}
		return returnOfAction;
	}

	public String programaFormaPagamento( final String formapagamento ) {

		String returnOfAction = null;
		if ( notIsModoDemostracao() ) {
			returnOfAction = ecf.programaFormaPagamento( tiraAcentos( formapagamento ) );
			if ( returnOfAction == null ) {
				setMessageLog( "Forma de pagamento não encontrada/programada." );
				whiterLogError( "[FORMA DE PAGAMENTO] " );
			}
		}
		return returnOfAction;
	}

	public boolean abrirGaveta() {

		return abrirGaveta( 205 );
	}

	public boolean abrirGaveta( final int time ) {

		boolean returnOfAction = true;
		if ( notIsModoDemostracao() ) {
			returnOfAction = decodeReturn( ecf.acionaGavetaDinheiro( time ) );
			if ( !returnOfAction ) {
				whiterLogError( "[ABERTURA DE GAVETA] " );
			}
		}
		return returnOfAction;
	}

	public boolean leituraX() {

		return leituraX( false );
	}

	public boolean leituraX( final boolean saidaSerial ) {

		boolean returnOfAction = true;
		if ( notIsModoDemostracao() ) {
			returnOfAction = saidaSerial ? decodeReturn( ecf.leituraXSerial() ) : decodeReturn( ecf.leituraX() );
			if ( !returnOfAction ) {
				whiterLogError( "[LEITURA X] " );
			}
		}
		return returnOfAction;
	}

	public boolean suprimento( final BigDecimal valor ) {

		return suprimento( valor, "Dinheiro" );
	}

	public boolean suprimento( final BigDecimal valor, String formaDePagamento ) {

		boolean returnOfAction = true;
		if ( notIsModoDemostracao() ) {
			if ( valor != null && valor.floatValue() > 0f ) {
				BigDecimal valorsuprimento = valor.setScale( 2, BigDecimal.ROUND_HALF_UP );
				if ( formaDePagamento == null || formaDePagamento.trim().length() == 0 ) {
					formaDePagamento = "Dinheiro";
				}
				returnOfAction = decodeReturn( ecf.comprovanteNFiscalNVinculado( AbstractECFDriver.SUPRIMENTO, valorsuprimento.floatValue(), formaDePagamento ) );
			}
			if ( !returnOfAction ) {
				whiterLogError( "[SUPRIMENTO] " );
			}
		}
		return returnOfAction;
	}

	public Integer getNumeroDocumento() {

		Integer returnOfAction = 99999998;
		if ( notIsModoDemostracao() ) {
			try {
				returnOfAction = Integer.parseInt( ecf.resultNumeroCupom() );
			}
			catch ( RuntimeException e ) {
				setMessageLog( e.getMessage() );
				whiterLogError( "[result DO COO] " );
			}
		}
		return returnOfAction;
	}

	public boolean abrirCupom() {

		return abreCupom( null );
	}

	public boolean abreCupom( final String cnpj_cpf ) {

		boolean returnOfAction = true;
		if ( notIsModoDemostracao() ) {
			returnOfAction = cnpj_cpf != null && cnpj_cpf.trim().length() > 0 ? decodeReturn( ecf.aberturaDeCupom( cnpj_cpf ) ) : decodeReturn( ecf.aberturaDeCupom() );
			if ( !returnOfAction ) {
				whiterLogError( "[ABERTURA DE CUPOM] " );
			}
		}
		return returnOfAction;
	}

	public boolean unidadeMedida( final String unidade ) {

		boolean returnOfAction = true;
		if ( notIsModoDemostracao() ) {
			if ( unidade != null && unidade.trim().length() > 0 ) {
				returnOfAction = decodeReturn( ecf.programaUnidadeMedida( unidade ) );
				if ( !returnOfAction ) {
					whiterLogError( "[UNIDADE DE MEDIDA] " );
				}
			}
			else {
				returnOfAction = false;
				setMessageLog( "unidade de medida inválida." );
				whiterLogError( "[UNIDADE DE MEDIDA] " );
			}
		}
		return returnOfAction;
	}

	public boolean aumetaDescricaoItem( final String descricao ) {

		boolean returnOfAction = true;
		if ( notIsModoDemostracao() ) {
			if ( descricao != null && descricao.trim().length() > 0 ) {
				returnOfAction = decodeReturn( ecf.aumentaDescItem( tiraAcentos( descricao ) ) );
				if ( !returnOfAction ) {
					whiterLogError( "[AUMENTA DESCRIÇÂO] " );
				}
			}
			else {
				returnOfAction = false;
				setMessageLog( "descrição do item inválida." );
				whiterLogError( "[AUMENTA DESCRIÇÂO] " );
			}
		}
		return returnOfAction;
	}

	private boolean validaParametro( final EParametro arg0, final Object arg1, final char arg2 ) {

		boolean actionOK = true;
		if ( arg0 == null ) {
			actionOK = false;
		}
		else if ( arg1 == null ) {
			setMessageLog( arg0.getName() + " inválido.[" + arg1 + "]" );
			actionOK = false;
		}
		else {
			switch ( arg0 ) {
			case PARAM_CODIGO: {
				if ( ( (String) arg1 ).trim().length() == 0 ) {
					setMessageLog( "Código do produto inválido.[" + arg1 + "]" );
					actionOK = false;
				}
				break;
			}
			case PARAM_DESCRICAO: {
				if ( ( (String) arg1 ).trim().length() == 0 ) {
					setMessageLog( "Descrição do item inválida.[" + arg1 + "]" );
					actionOK = false;
				}
				break;
			}
			case PARAM_ALIQUOTA: {
				if ( ( (BigDecimal) arg1 ).floatValue() <= 0.0f || ( (BigDecimal) arg1 ).floatValue() > 99.994f ) {
					setMessageLog( "Aliquota inválida.[" + arg1 + "]" );
					actionOK = false;
				}
				String strAliquota = getIndexAliquota( ( (BigDecimal) arg1 ).floatValue() );
				if ( strAliquota == null ) {
					setMessageLog( "Aliquota não encontrada.[" + arg1 + "]" );
					actionOK = false;
				}
				break;
			}
			case PARAM_TIPO_QUANTIDADE: {
				break;
			}
			case PARAM_QUANTIDADE: {
				if ( ( (BigDecimal) arg1 ).floatValue() <= 0.0f || ( arg2 == AbstractECFDriver.QTD_INTEIRO && ( (BigDecimal) arg1 ).floatValue() > 9999f ) || ( arg2 == AbstractECFDriver.QTD_DECIMAL && ( (BigDecimal) arg1 ).floatValue() > 9999.9994f ) ) {
					setMessageLog( "Quantidade inválida.[" + arg1 + "]" );
					actionOK = false;
				}
				break;
			}
			case PARAM_CASAS_DECIMAIS: {
				break;
			}
			case PARAM_VALOR_UNITARIO: {
				if ( ( (BigDecimal) arg1 ).floatValue() <= 0.0f || ( (BigDecimal) arg1 ).floatValue() > 999999.994f ) {
					setMessageLog( "Valor inválido.[" + arg1 + "]" );
					actionOK = false;
				}
				break;
			}
			case PARAM_TIPO_DESCONTO: {
				break;
			}
			case PARAM_DESCONTO: {
				if ( ( (BigDecimal) arg1 ).floatValue() < 0.0f || ( arg2 == AbstractECFDriver.DESCONTO_PERC && ( (BigDecimal) arg1 ).floatValue() > 99.994f )
						|| ( arg2 == AbstractECFDriver.DESCONTO_VALOR && ( (BigDecimal) arg1 ).floatValue() > 9999.9994f ) ) {
					setMessageLog( "Desconto inválido.[" + arg1 + "]" );
					actionOK = false;
				}
				break;
			}
			case PARAM_ACRECIMO: {
				if ( ( (BigDecimal) arg1 ).floatValue() < 0.0f || ( arg2 == AbstractECFDriver.ACRECIMO_PERC && ( (BigDecimal) arg1 ).floatValue() > 99.994f )
						|| ( arg2 == AbstractECFDriver.ACRECIMO_VALOR && ( (BigDecimal) arg1 ).floatValue() > 9999.9994f ) ) {
					setMessageLog( "Acrécimo inválido.[" + arg1 + "]" );
					actionOK = false;
				}
				break;
			}
			case PARAM_DEPARTAMENTO: {
				if ( (Integer) arg1 <= 0 && (Integer) arg1 > 20 ) {
					setMessageLog( "Departamento inválido.[" + arg1 + "]" );
					actionOK = false;
				}
				break;
			}
			case PARAM_UNIDADE_MEDIDA: {
				if ( ( (String) arg1 ).trim().length() == 0 ) {
					setMessageLog( "Unidade de medida inválida.[" + arg1 + "]" );
					actionOK = false;
				}
				break;
			}
			case PARAM_FORMA_PAGAMENTO: {
				if ( ( (String) arg1 ).trim().length() == 0 ) {
					setMessageLog( "Forma de pagamento inválida.[" + arg1 + "]" );
					actionOK = false;
				}
				break;
			}
			case PARAM_DOCUMENTO: {
				if ( ( (Integer) arg1 ) <= 0 ) {
					setMessageLog( "Número do documento inválido.[" + arg1 + "]" );
					actionOK = false;
				}
				break;
			}
			}
		}
		return actionOK;
	}

	public boolean vendaItem( final String codigo, final String descricao, final BigDecimal aliquota, final BigDecimal quantidade, final BigDecimal valor ) {

		return vendaItem( codigo, descricao, aliquota, quantidade, valor, new BigDecimal( "0" ) );
	}

	public boolean vendaItem( final String codigo, final String descricao, final BigDecimal aliquota, final BigDecimal quantidade, final BigDecimal valor, final BigDecimal desconto ) {

		return vendaItem( codigo, descricao, aliquota, null, AbstractECFDriver.QTD_DECIMAL, quantidade, AbstractECFDriver.DUAS_CASAS_DECIMAIS, valor, AbstractECFDriver.DESCONTO_VALOR, desconto );
	}

	public boolean vendaItem( final String codigo, final String descricao, final BigDecimal aliquota, final String tipoAliquota, final BigDecimal quantidade, final BigDecimal valor, final BigDecimal desconto ) {

		return vendaItem( codigo, descricao, aliquota, tipoAliquota, AbstractECFDriver.QTD_DECIMAL, quantidade, AbstractECFDriver.DUAS_CASAS_DECIMAIS, valor, AbstractECFDriver.DESCONTO_VALOR, desconto );
	}

	public boolean vendaItem( final String codigo, final String descricao, BigDecimal aliquota, final String tipoAliquota, final char tipoQuantidade, BigDecimal quantidade, final int casasDecimais, BigDecimal valor, final char tipoDesconto,
			BigDecimal desconto ) {

		boolean returnOfAction = true;
		if ( notIsModoDemostracao() ) {
			boolean actionOK = true;
			if ( !validaParametro( PARAM_CODIGO, codigo, '0' ) ) {
				actionOK = false;
			}
			else if ( !validaParametro( PARAM_DESCRICAO, descricao, '0' ) ) {
				actionOK = false;
			}
			else if ( !validaParametro( PARAM_QUANTIDADE, quantidade, tipoQuantidade ) ) {
				actionOK = false;
			}
			else if ( !validaParametro( PARAM_VALOR_UNITARIO, valor, '0' ) ) {
				actionOK = false;
			}
			else if ( !validaParametro( PARAM_DESCONTO, desconto, tipoDesconto ) ) {
				actionOK = false;
			}
			else {
				if ( tipoAliquota != null && ( !AbstractECFDriver.ALQ_INTEGRAL.equals( tipoAliquota ) ) ) {
					if ( !AbstractECFDriver.ALQ_ISENTA.equals( tipoAliquota ) && !AbstractECFDriver.ALQ_SUBSTITUICAO.equals( tipoAliquota ) && !AbstractECFDriver.ALQ_NAO_INSIDE.equals( tipoAliquota ) ) {
						setMessageLog( "Tipo de aliquota inválido.[" + tipoAliquota + "]" );
						actionOK = false;
					}
				}
				else if ( !validaParametro( PARAM_ALIQUOTA, aliquota, '0' ) ) {
					actionOK = false;
				}
			}
			if ( actionOK ) {
				String strAliquota = tipoAliquota != null && ( !AbstractECFDriver.ALQ_INTEGRAL.equals( tipoAliquota ) ) ? tipoAliquota : getIndexAliquota( aliquota.floatValue() );
				quantidade = tipoQuantidade == AbstractECFDriver.QTD_INTEIRO ? new BigDecimal( quantidade.intValue() ) : quantidade.setScale( 3, BigDecimal.ROUND_HALF_UP );
				desconto = desconto.setScale( 2, BigDecimal.ROUND_HALF_UP );
				int countWhite = 0;
				if ( casasDecimais == 3 ) {
					valor = valor.setScale( 3, BigDecimal.ROUND_HALF_UP );
					while ( !decodeReturn( ecf.vendaItemTresCasas( codigo, tiraAcentos( descricao ), strAliquota, tipoQuantidade, quantidade.floatValue(), valor.floatValue(), tipoDesconto, desconto.floatValue() ) ) ) {
						try {
							Thread.sleep( 2000 );
						}
						catch ( InterruptedException e ) {
							whiterLogError( e );
						}
						if ( ( countWhite++ ) >= 5 ) {
							returnOfAction = false;
							break;
						}
					}
				}
				else {
					valor = valor.setScale( 2, BigDecimal.ROUND_HALF_UP );
					while ( !decodeReturn( ecf.vendaItem( codigo, tiraAcentos( descricao ), strAliquota, tipoQuantidade, quantidade.floatValue(), valor.floatValue(), tipoDesconto, desconto.floatValue() ) ) ) {
						try {
							Thread.sleep( 2000 );
						}
						catch ( InterruptedException e ) {
							whiterLogError( e );
						}
						if ( ( countWhite++ ) >= 5 ) {
							returnOfAction = false;
							break;
						}
					}
				}
			}
			else {
				returnOfAction = false;
			}
			if ( !returnOfAction ) {
				whiterLogError( "[VENDA DE ITEM] " );
			}
		}
		return returnOfAction;
	}

	public boolean vendaItemDepartamento( final String codigo, final String descricao, final BigDecimal aliquota, final String tipoAliquota, BigDecimal valor, BigDecimal quantidade, final BigDecimal acrescimo, BigDecimal desconto,
			final int departamento, final String unidadeMedida ) {

		boolean returnOfAction = true;
		if ( notIsModoDemostracao() ) {
			boolean actionOK = true;
			if ( !validaParametro( PARAM_CODIGO, codigo, '0' ) ) {
				actionOK = false;
			}
			else if ( !validaParametro( PARAM_DESCRICAO, descricao, '0' ) ) {
				actionOK = false;
			}
			else if ( !validaParametro( PARAM_VALOR_UNITARIO, valor, '0' ) ) {
				actionOK = false;
			}
			else if ( !validaParametro( PARAM_QUANTIDADE, quantidade, AbstractECFDriver.QTD_DECIMAL ) ) {
				actionOK = false;
			}
			else if ( !validaParametro( PARAM_ACRECIMO, acrescimo, AbstractECFDriver.ACRECIMO_VALOR ) ) {
				actionOK = false;
			}
			else if ( !validaParametro( PARAM_DESCONTO, desconto, AbstractECFDriver.DESCONTO_VALOR ) ) {
				actionOK = false;
			}
			else if ( !validaParametro( PARAM_DEPARTAMENTO, departamento, '0' ) ) {
				actionOK = false;
			}
			else if ( !validaParametro( PARAM_UNIDADE_MEDIDA, unidadeMedida, '0' ) ) {
				actionOK = false;
			}
			else {
				if ( tipoAliquota != null ) {
					if ( !AbstractECFDriver.ALQ_INTEGRAL.equals( tipoAliquota ) || !AbstractECFDriver.ALQ_ISENTA.equals( tipoAliquota ) || !AbstractECFDriver.ALQ_SUBSTITUICAO.equals( tipoAliquota )
							|| !AbstractECFDriver.ALQ_NAO_INSIDE.equals( tipoAliquota ) ) {
						actionOK = false;
					}
				}
				else if ( !validaParametro( PARAM_ALIQUOTA, aliquota, '0' ) ) {
					actionOK = false;
				}
			}
			if ( actionOK ) {
				String strAliquota = tipoAliquota != null ? tipoAliquota : getIndexAliquota( aliquota.floatValue() );
				quantidade = quantidade.setScale( 3, BigDecimal.ROUND_HALF_UP );
				desconto = desconto.setScale( 2, BigDecimal.ROUND_HALF_UP );
				int countWhite = 0;
				valor = valor.setScale( 3, BigDecimal.ROUND_HALF_UP );
				while ( !decodeReturn( ecf.vendaItemDepartamento( strAliquota, valor.floatValue(), quantidade.floatValue(), desconto.floatValue(), acrescimo.floatValue(), departamento, unidadeMedida, codigo, descricao ) ) ) {
					try {
						Thread.sleep( 2000 );
					}
					catch ( InterruptedException e ) {
						whiterLogError( e );
					}
					if ( ( countWhite++ ) >= 5 ) {
						returnOfAction = false;
						break;
					}
				}
			}
			else {
				returnOfAction = false;
			}
			if ( !returnOfAction ) {
				whiterLogError( "[VENDA DEPARTAMENTO] " );
			}
		}
		return returnOfAction;
	}

	public BigDecimal getSubTotal() {

		BigDecimal returnOfAction = null;
		if ( notIsModoDemostracao() ) {
			try {
				String subtotal = ecf.resultSubTotal();
				if ( subtotal != null && subtotal.trim().length() == 14 ) {
					subtotal = subtotal.substring( 0, 12 ) + "." + subtotal.substring( 12 );
				}
				returnOfAction = new BigDecimal( subtotal );
			}
			catch ( RuntimeException e ) {
				setMessageLog( e.getMessage() );
				whiterLogError( "[CANCELAMENTO DE ITEM] " );
			}
		}
		return returnOfAction;
	}

	public boolean cancelaItem() {

		return cancelaItem( 0 );
	}

	public boolean cancelaItem( final int item ) {

		boolean returnOfAction = true;
		if ( notIsModoDemostracao() ) {
			returnOfAction = decodeReturn( item == 0 ? ecf.cancelaItemAnterior() : ecf.cancelaItemGenerico( item ) );
			if ( !returnOfAction ) {
				whiterLogError( "[CANCELAMENTO DE ITEM] " );
			}
		}
		return returnOfAction;
	}

	public boolean cancelaCupom() {

		boolean returnOfAction = true;
		if ( notIsModoDemostracao() ) {
			returnOfAction = decodeReturn( ecf.cancelaCupom() );
			if ( !returnOfAction ) {
				whiterLogError( "[CANCELAMENTO DE CUPOM] " );
			}
		}
		return returnOfAction;
	}

	public boolean iniciaFechamentoCupom() {

		return iniciaFechamentoCupom( AbstractECFDriver.DESCONTO_VALOR, new BigDecimal( "0.00" ) );
	}

	public boolean iniciaFechamentoCupom( final char opcao, BigDecimal percentual ) {

		boolean returnOfAction = true;
		if ( notIsModoDemostracao() ) {
			boolean actionOK = true;
			if ( opcao == AbstractECFDriver.ACRECIMO_PERC || opcao == AbstractECFDriver.ACRECIMO_VALOR ) {
				if ( !validaParametro( PARAM_ACRECIMO, percentual, opcao ) ) {
					actionOK = false;
				}
			}
			else if ( opcao == AbstractECFDriver.DESCONTO_PERC || opcao == AbstractECFDriver.DESCONTO_VALOR ) {
				if ( !validaParametro( PARAM_DESCONTO, percentual, opcao ) ) {
					actionOK = false;
				}
			}
			else {
				actionOK = false;
				setMessageLog( "Opção indefinida." );
			}
			if ( actionOK ) {
				percentual = percentual.setScale( 2, BigDecimal.ROUND_HALF_UP );
				returnOfAction = decodeReturn( ecf.iniciaFechamentoCupom( opcao, percentual.floatValue() ) );
			}
			else {
				returnOfAction = false;
			}
			if ( !returnOfAction ) {
				whiterLogError( "[INICIA FECHAMENTO DE CUPOM] " );
			}
		}
		return returnOfAction;
	}

	public boolean efetuaFormaPagamento( BigDecimal valor ) {

		return efetuaFormaPagamento( "Dinheiro", valor, null );
	}

	public boolean efetuaFormaPagamento( final String formaPagamento, BigDecimal valor ) {

		return efetuaFormaPagamento( formaPagamento, valor, null );
	}

	public boolean efetuaFormaPagamento( final String formaPagamento, BigDecimal valor, final String descricaoAuxiliar ) {

		boolean returnOfAction = true;
		if ( notIsModoDemostracao() ) {
			boolean actionOK = true;
			if ( !validaParametro( PARAM_FORMA_PAGAMENTO, formaPagamento, '0' ) ) {
				actionOK = false;
			}
			else if ( !validaParametro( PARAM_VALOR_UNITARIO, valor, '0' ) ) {
				actionOK = false;
			}
			if ( actionOK ) {
				// String sIndice = "Dinheiro".equals( formaPagamento.trim() )
				// ? "01" : programaFormaPagamento( formaPagamento );
				String sIndice = programaFormaPagamento( formaPagamento );
				valor = valor.setScale( 2, BigDecimal.ROUND_HALF_UP );
				returnOfAction = decodeReturn( ecf.efetuaFormaPagamento( sIndice, valor.floatValue(), descricaoAuxiliar ) );
			}
			else {
				returnOfAction = false;
			}
			if ( !returnOfAction ) {
				whiterLogError( "[EFETUA FORMA DE PAGAMENTO] " );
			}
		}
		return returnOfAction;
	}

	public boolean finalizaFechamentoCupom( final String mensagem ) {

		boolean returnOfAction = true;
		if ( notIsModoDemostracao() ) {
			returnOfAction = decodeReturn( ecf.finalizaFechamentoCupom( mensagem ) );
			if ( !returnOfAction ) {
				whiterLogError( "[FINALIZA FECHAMENTO DE CUPOM] " );
			}
		}
		return returnOfAction;
	}

	public boolean estornoFormaPagamento( final String pagamentoOrigem, final String pagamentoDestino, BigDecimal valor ) {

		boolean returnOfAction = true;
		if ( notIsModoDemostracao() ) {
			boolean actionOK = true;
			if ( pagamentoOrigem == null || pagamentoOrigem.trim().length() == 0 ) {
				setMessageLog( "Forma de pagamento de origem inválida.[" + pagamentoOrigem + "]" );
				actionOK = false;
			}
			else if ( pagamentoDestino == null || pagamentoDestino.trim().length() == 0 ) {
				setMessageLog( "Forma de pagamento de destino inválida.[" + pagamentoDestino + "]" );
				actionOK = false;
			}
			else if ( valor == null || valor.floatValue() <= 0.004f ) {
				setMessageLog( "Forma de pagamento de origem inválida.[" + pagamentoOrigem + "]" );
				actionOK = false;
			}
			if ( actionOK ) {
				valor = valor.setScale( 2, BigDecimal.ROUND_HALF_UP );
				returnOfAction = decodeReturn( ecf.estornoFormaPagamento( pagamentoOrigem, pagamentoDestino, valor.floatValue() ) );
			}
			else {
				returnOfAction = false;
			}
			if ( !returnOfAction ) {
				whiterLogError( "[ESTORNO DE FORMA DE PAGAMENTO] " );
			}
		}
		return returnOfAction;
	}

	public boolean relatorioGerencial( final String texto ) {

		boolean returnOfAction = true;
		if ( notIsModoDemostracao() ) {
			boolean actionOK = true;
			if ( texto == null ) {
				setMessageLog( "Texto inválido.[" + texto + "]" );
				actionOK = false;
			}
			if ( actionOK ) {
				returnOfAction = decodeReturn( ecf.relatorioGerencial( texto ) );
			}
			else {
				returnOfAction = false;
			}
			if ( !returnOfAction ) {
				whiterLogError( "[RELÁTORIO GERENCIAL] " );
			}
		}
		return returnOfAction;
	}

	public boolean fecharRelatorioGerencial() {

		boolean returnOfAction = true;
		if ( notIsModoDemostracao() ) {
			returnOfAction = decodeReturn( ecf.fechamentoRelatorioGerencial() );
			if ( !returnOfAction ) {
				whiterLogError( "[FECHAMENTO DO RELÁTORIO GERENCIAL] " );
			}
		}
		return returnOfAction;
	}

	public boolean abreComprovanteNaoFiscalVinculado( final String formaPagamento, BigDecimal valor, final Integer documento ) {

		boolean returnOfAction = true;
		if ( notIsModoDemostracao() ) {
			boolean actionOK = true;
			String formapagamentotmp = tiraAcentos( formaPagamento );
			if ( !validaParametro( PARAM_FORMA_PAGAMENTO, formapagamentotmp, '0' ) ) {
				actionOK = false;
			}
			else if ( !validaParametro( PARAM_VALOR_UNITARIO, valor, '0' ) ) {
				actionOK = false;
			}
			else if ( !validaParametro( PARAM_DOCUMENTO, documento, '0' ) ) {
				actionOK = false;
			}
			if ( actionOK ) {
				returnOfAction = decodeReturn( ecf.abreComprovanteNFiscalVinculado( formapagamentotmp, valor.floatValue(), documento ) );
			}
			else {
				returnOfAction = false;
			}
			if ( !returnOfAction ) {
				whiterLogError( "[ABERTURA DE COMPROVANTE NÂO FISCAL VINCULADO] " );
			}
		}
		return returnOfAction;
	}

	public boolean usaComprovanteNaoFiscalVinculado( final String texto ) {

		boolean returnOfAction = true;
		if ( notIsModoDemostracao() ) {
			if ( texto != null && texto.trim().length() > 0 ) {
				returnOfAction = decodeReturn( ecf.usaComprovanteNFiscalVinculado( texto ) );
			}
			if ( !returnOfAction ) {
				whiterLogError( "[USA DE COMPROVANTE NÂO FISCAL VINCULADO] " );
			}
		}
		return returnOfAction;
	}

	public boolean sangria( final BigDecimal valor ) {

		return sangria( valor, "Dinheiro" );
	}

	public boolean sangria( final BigDecimal valor, String formaDePagamento ) {

		boolean returnOfAction = true;
		if ( notIsModoDemostracao() ) {
			if ( valor != null && valor.floatValue() > 0f ) {
				BigDecimal valorsangria = valor.setScale( 2, BigDecimal.ROUND_HALF_UP );
				if ( formaDePagamento == null || formaDePagamento.trim().length() == 0 ) {
					formaDePagamento = "Dinheiro";
				}
				returnOfAction = decodeReturn( ecf.comprovanteNFiscalNVinculado( AbstractECFDriver.SANGRIA, valorsangria.floatValue(), formaDePagamento ) );
			}
			if ( !returnOfAction ) {
				whiterLogError( "[SANGRIA] " );
			}
		}
		return returnOfAction;
	}

	public boolean reducaoZ() {

		boolean returnOfAction = true;
		if ( notIsModoDemostracao() ) {
			returnOfAction = decodeReturn( ecf.reducaoZ() );
			if ( !returnOfAction ) {
				whiterLogError( "[REDUÇÃO Z] " );
			}
		}
		return returnOfAction;
	}

	public boolean autenticarDocumento() {

		boolean returnOfAction = true;
		if ( notIsModoDemostracao() ) {
			returnOfAction = decodeReturn( ecf.autenticacaoDeDocumento() );
			if ( !returnOfAction ) {
				whiterLogError( "[AUTENTICAÇÃO] " );
			}
		}
		return returnOfAction;
	}

	public Integer getNumeroCaixa() {

		Integer returnOfAction = null;
		if ( notIsModoDemostracao() ) {
			try {
				String caixa = ( ecf.resultVariaveis( AbstractECFDriver.V_NUM_CAIXA ) );
				returnOfAction = Integer.parseInt( caixa );
			}
			catch ( NumberFormatException e ) {
				setMessageLog( e.getMessage() );
				whiterLogError( "[NÚMERO DO CAIXA] " );
			}
		}
		return returnOfAction;
	}

	public List<String> getAliquotas() {

		List<String> returnOfAction = new ArrayList<String>();
		if ( notIsModoDemostracao() ) {
			String sAliquotas = ( ecf.resultAliquotas() ).trim();
			int tamanho = 0;
			if ( sAliquotas != null ) {
				tamanho = sAliquotas.length() / 4;
			}
			for ( int i = 0; i < tamanho; i++ ) {
				returnOfAction.add( sAliquotas.substring( i * 4, ( i * 4 ) + 4 ) );
			}
		}
		return returnOfAction;
	}

	public String getIndexAliquota( final float arg ) {

		String indexAliquota = null;
		try {
			if ( arg > 0.0f && arg < 99.99f ) {
				if ( aliquotas == null || aliquotas.size() == 0 ) {
					aliquotas = getAliquotas();
				}
				String tmp = ecf.floatToString( arg, 4, 2 );
				int index = 1;
				for ( String s : aliquotas ) {
					if ( s.equals( tmp ) ) {
						indexAliquota = ecf.strZero( String.valueOf( index ), 2 );
						break;
					}
					index++;
				}
			}
		}
		finally {
			System.gc();
		}
		return indexAliquota;
	}

	public Integer getNumeroReducoesZ() {

		Integer returnOfAction = new Integer( "0" );
		if ( notIsModoDemostracao() ) {
			returnOfAction = new Integer( ecf.resultVariaveis( AbstractECFDriver.V_REDUCOES ) );
		}
		return returnOfAction;
	}

	public Integer getNumeroCancelamentos() {

		Integer returnOfAction = new Integer( "0" );
		if ( notIsModoDemostracao() ) {
			returnOfAction = new Integer( ecf.resultVariaveis( AbstractECFDriver.V_CUPONS_CANC ) );
		}
		return returnOfAction;
	}

	public BigDecimal getTotalCancelamentos() {

		BigDecimal returnOfAction = new BigDecimal( "0.00" );
		if ( notIsModoDemostracao() ) {
			String str = ecf.resultVariaveis( AbstractECFDriver.V_CANCELAMENTOS );
			final BigDecimal cem = new BigDecimal( "100.00" );
			returnOfAction = new BigDecimal( str ).divide( cem );
		}
		return returnOfAction;
	}

	public BigDecimal getTotalDescontos() {

		BigDecimal returnOfAction = new BigDecimal( "0.00" );
		if ( notIsModoDemostracao() ) {
			String str = ecf.resultVariaveis( AbstractECFDriver.V_DESCONTOS );
			final BigDecimal cem = new BigDecimal( "100.00" );
			returnOfAction = new BigDecimal( str ).divide( cem );
		}
		return returnOfAction;
	}

	public BigDecimal getTotalIsensao() {

		BigDecimal returnOfAction = new BigDecimal( "0.00" );
		if ( notIsModoDemostracao() ) {
			final List<BigDecimal> totalizadores = getTotalizadoresParciais();
			returnOfAction = totalizadores.get( 0 );
		}
		return returnOfAction;
	}

	public BigDecimal getTotalNaoInsidencia() {

		BigDecimal returnOfAction = new BigDecimal( "0.00" );
		if ( notIsModoDemostracao() ) {
			final List<BigDecimal> totalizadores = getTotalizadoresParciais();
			returnOfAction = totalizadores.get( 1 );
		}
		return returnOfAction;
	}

	public BigDecimal getTotalSubstituicao() {

		BigDecimal returnOfAction = new BigDecimal( "0.00" );
		if ( notIsModoDemostracao() ) {
			final List<BigDecimal> totalizadores = getTotalizadoresParciais();
			returnOfAction = totalizadores.get( 2 );
		}
		return returnOfAction;
	}

	public BigDecimal getTotalSangria() {

		BigDecimal returnOfAction = new BigDecimal( "0.00" );
		if ( notIsModoDemostracao() ) {
			final List<BigDecimal> totalizadores = getTotalizadoresParciais();
			returnOfAction = totalizadores.get( 3 );
		}
		return returnOfAction;
	}

	public BigDecimal getTotalSuprimento() {

		BigDecimal returnOfAction = new BigDecimal( "0.00" );
		if ( notIsModoDemostracao() ) {
			final List<BigDecimal> totalizadores = getTotalizadoresParciais();
			returnOfAction = totalizadores.get( 4 );
		}
		return returnOfAction;
	}

	public BigDecimal getGrandeTotal() {

		BigDecimal returnOfAction = new BigDecimal( "0.00" );
		if ( notIsModoDemostracao() ) {
			final List<BigDecimal> totalizadores = getTotalizadoresParciais();
			returnOfAction = totalizadores.get( 5 );
		}
		return returnOfAction;
	}

	public List<BigDecimal> getTotalizadoresFiscais() {

		List<BigDecimal> returnOfAction = new ArrayList<BigDecimal>();
		if ( notIsModoDemostracao() ) {
			final BigDecimal cem = new BigDecimal( "100.00" );
			final String[] totalizadores = ecf.resultTotalizadoresParciais().split( "," );
			final String parciais = totalizadores[ 0 ];
			if ( parciais != null && parciais.length() >= 224 ) {
				for ( int i = 0; i < 16; i++ ) {
					returnOfAction.add( new BigDecimal( parciais.substring( i * 14, i * 14 + 14 ) ).divide( cem ) );
				}
			}
		}
		return returnOfAction;
	}

	public List<BigDecimal> getTotalizadoresNaoFiscais() {

		List<BigDecimal> returnOfAction = new ArrayList<BigDecimal>();
		if ( notIsModoDemostracao() ) {
			final BigDecimal cem = new BigDecimal( "100.00" );
			final String[] totalizadores = ecf.resultTotalizadoresParciais().split( "," );
			final String parciais = totalizadores[ 4 ];
			if ( parciais != null && parciais.length() >= 126 ) {
				for ( int i = 0; i < 9; i++ ) {
					returnOfAction.add( new BigDecimal( parciais.substring( i * 14, i * 14 + 14 ) ).divide( cem ) );
				}
			}
		}
		return returnOfAction;
	}

	public List<BigDecimal> getTotalizadoresParciais() {

		List<BigDecimal> returnOfAction = new ArrayList<BigDecimal>();
		if ( notIsModoDemostracao() ) {
			final BigDecimal cem = new BigDecimal( "100.00" );
			final String[] totalizadores = ecf.resultTotalizadoresParciais().split( "," );
			int index = 0;
			for ( String t : totalizadores ) {
				if ( index != 0 && index != 4 ) {
					returnOfAction.add( new BigDecimal( t ).divide( cem ) );
				}
				index++;
			}
		}
		return returnOfAction;
	}

	public String getStatusImpressora() {

		String returnOfAction = "";
		if ( notIsModoDemostracao() ) {
			returnOfAction = ecf.getStatus();
		}
		return returnOfAction;
	}

	public boolean isDocumentoAberto() {

		boolean returnOfAction = false;
		if ( notIsModoDemostracao() ) {
			returnOfAction = ecf.resultDocumentoAberto();
		}
		return returnOfAction;
	}

	public boolean reducaoZExecutada() {

		boolean returnOfAction = true;
		if ( notIsModoDemostracao() ) {
			try {
				returnOfAction = false;
				Calendar dataUltimaReducao = Calendar.getInstance();
				Calendar dataAtual = Calendar.getInstance();
				dataUltimaReducao.setTime( ultimaReducaoZ() );
				dataAtual.set( Calendar.AM_PM, 0 );
				dataAtual.set( Calendar.HOUR_OF_DAY, 0 );
				dataAtual.set( Calendar.HOUR, 0 );
				dataAtual.set( Calendar.MINUTE, 0 );
				dataAtual.set( Calendar.SECOND, 0 );
				dataAtual.set( Calendar.MILLISECOND, 0 );
				returnOfAction = dataUltimaReducao.compareTo( dataAtual ) == 0;
			}
			catch ( Exception e ) {
				setMessageLog( e.getMessage() );
				whiterLogError( "[REDUÇÂO Z] " );
			}
		}
		return returnOfAction;
	}

	public Date ultimaReducaoZ() {

		Calendar dataUltimaReducao = Calendar.getInstance();
		dataUltimaReducao.set(Calendar.DAY_OF_MONTH, -1);
		if ( notIsModoDemostracao() ) {
			try {
				String strDataUltimaReducao = ecf.resultVariaveis( AbstractECFDriver.V_DT_ULT_REDUCAO );
				final SimpleDateFormat sdf = new SimpleDateFormat( "ddMMyy", Locale.getDefault() );
				dataUltimaReducao = Calendar.getInstance();
				dataUltimaReducao.setTime( sdf.parse( strDataUltimaReducao ) );
			}
			catch ( Exception e ) {
				setMessageLog( e.getMessage() );
				whiterLogError( "[REDUÇÃO Z] " );
			}
		}
		return dataUltimaReducao.getTime();
	}

	/**
	 * Realiza a leitura da porta serial para capturar os dados retornados na mesma.<br>
	 * 
	 * @return leitura da porta serial.
	 */
	public String readSerialPort() {

		String strReturn = null;
		if ( notIsModoDemostracao() ) {
			strReturn = new String( ecf.getBytesLidos() );
		}
		return strReturn;
	}

	private void whiterLogError( final Throwable e ) {

		if ( logger != null ) {
			logger.error( getMessageLog(), e );
		}
	}

	private void whiterLogError( final String actionName ) {

		if ( logger != null ) {
			logger.error( actionName + getMessageLog() );
		}
	}

	// funções que devem ser esternas.
	public String tiraAcentos( final String arg ) {

		String sRet = "";
		if ( arg != null && arg.trim().length() > 0 ) {
			char cVals[] = arg.toCharArray();
			for ( int i = 0; i < cVals.length; i++ ) {
				cVals[ i ] = tiraAcento( cVals[ i ] );
			}
			sRet = new String( cVals );
		}
		return sRet;
	}

	public char tiraAcento( char cKey ) {

		char cTmp = cKey;
		if ( contido( cTmp, "ãâáà" ) ) {
			cTmp = 'a';
		}
		else if ( contido( cTmp, "ÃÂÁÀ" ) ) {
			cTmp = 'A';
		}
		else if ( contido( cTmp, "êéè" ) ) {
			cTmp = 'e';
		}
		else if ( contido( cTmp, "ÊÉÈ" ) ) {
			cTmp = 'E';
		}
		else if ( contido( cTmp, "îíì" ) ) {
			cTmp = 'i';
		}
		else if ( contido( cTmp, "ÎÍÌ" ) ) {
			cTmp = 'I';
		}
		else if ( contido( cTmp, "õôóò" ) ) {
			cTmp = 'o';
		}
		else if ( contido( cTmp, "ÕÔÓÒ" ) ) {
			cTmp = 'O';
		}
		else if ( contido( cTmp, "ûúùü" ) ) {
			cTmp = 'u';
		}
		else if ( contido( cTmp, "ÛÚÙÜ" ) ) {
			cTmp = 'U';
		}
		else if ( contido( cTmp, "ç" ) ) {
			cTmp = 'c';
		}
		else if ( contido( cTmp, "Ç" ) ) {
			cTmp = 'C';
		}
		return cTmp;
	}

	public boolean contido( char cTexto, String sTexto ) {

		boolean bresult = false;
		for ( int i = 0; i < sTexto.length(); i++ ) {
			if ( cTexto == sTexto.charAt( i ) ) {
				bresult = true;
				break;
			}
		}
		return bresult;
	}
}
