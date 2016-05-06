
package org.freedom.tef.driver.dedicate;

import java.util.ArrayList;
import java.util.List;

import org.freedom.tef.app.TefAction;

public class DedicatedAction implements TefAction {

	public static final DedicatedAction ERRO = new DedicatedAction( -1 );

	public static final DedicatedAction WARNING = new DedicatedAction( -2 );

	public static final DedicatedAction CHECK_TICKET = new DedicatedAction( -3 );

	public static final DedicatedAction PRINT_TICKET = new DedicatedAction( -4 );

	/**
	 * Está de volvendo um valor para, se desejado, ser armazenado pela automação.
	 */
	public static final DedicatedAction ARMAZENAR = new DedicatedAction( 0 );

	/**
	 * Mensagem para visor do eperador.
	 */
	public static final DedicatedAction MENSAGEM_OPERADOR = new DedicatedAction( 1 );

	/**
	 * Mensagem para visor do cliente.
	 */
	public static final DedicatedAction MENSAGEM_CLIENTE = new DedicatedAction( 2 );

	/**
	 * Mensagem para os dois visores.
	 */
	public static final DedicatedAction MENSAGEM_TODOS = new DedicatedAction( 3 );

	/**
	 * Texto que deverá ser utilizado como cabeçalho na apresentação do menu( Comando 21 ).
	 */
	public static final DedicatedAction CABECALHO_MENU = new DedicatedAction( 4 );

	/**
	 * Deve remover a mensagem apresentada no visor do operador.
	 */
	public static final DedicatedAction REMOVER_MESAGEM_OPERADOR = new DedicatedAction( 11 );

	/**
	 * Deve remover a mensagem apresentada no visor do cliente.
	 */
	public static final DedicatedAction REMOVER_MESAGEM_CLIENTE = new DedicatedAction( 12 );

	/**
	 * Deve remover a mensagem apresentada no visor do operador e do cliente.
	 */
	public static final DedicatedAction REMOVER_MESAGEM_TODOS = new DedicatedAction( 13 );

	/**
	 * Deve remover o texto utilizado como cabeçalho na apresentação do menu.
	 */
	public static final DedicatedAction REMOVER_CABECALHO_MENU = new DedicatedAction( 14 );

	/**
	 * Cabeçalho a ser apresentado pela aplicação.
	 */
	public static final DedicatedAction CABECALHO = new DedicatedAction( 15 );

	/**
	 * Remover cabeçalho.
	 */
	public static final DedicatedAction REMOVER_CABECALHO = new DedicatedAction( 16 );

	/**
	 * Deve obter uma resposta do tipo SIM/NÂO. No retorno o primenro carácter presente em Buffer deve conter 0 se confirma e 1 se cancela.
	 */
	public static final DedicatedAction RETORNAR_CONFIRMACAO = new DedicatedAction( 20 );

	/**
	 * Deve apresentar um menu de opções e permitir que o usuário selecione uma delas. Na chamada o parâmetro Buffer contém as opções da forma que ele desejar (não sendo necessário incluir os índices 1,2,...) e após a seleção feira pelo usuário,
	 * retornar em Buffer o índice i escolhido pelo operador ( em ASCII )
	 */
	public static final DedicatedAction MOSTRAR_MENU = new DedicatedAction( 21 );

	/**
	 * Deve aquardar uma tecla do operador. É utilizada quando se deseja que o operador seja avisado de alguma mensagem apresentada na tela.
	 */
	public static final DedicatedAction AGUADAR_TECLA_OPERADOR = new DedicatedAction( 22 );

	/**
	 * Este comando indica que a rotina está perguntando para a aplicação se ele deseja interromper o processo de coleta de dados ou não. Esse código ocorre quando a DLL está acessando algum periférico e permite que a automação interrompa esse acesso
	 * (por exemplo: aquardando a passagem de um cartão pela lietora ou a difitração de senha pelo cliente)
	 */
	public static final DedicatedAction CONFIRMAR_IMTERRUPSAO = new DedicatedAction( 23 );

	/**
	 * Deve ser lido um campo cujo tamanho está entre Tamanho Minimo e Tamanho Máximo. O campo lido de ser devolvido em Buffer
	 */
	public static final DedicatedAction LER_CAMPO = new DedicatedAction( 30 );

	/**
	 * Deve ser lido o número de um cheque. A coleta pode ser feita via leitura de CMC-7 ou pela digutação da primeira linha do cheque. No retorno deve ser devolvido em Buffer "0:" ou "1:" seguido do número coletado manualmente ou pela leitura do
	 * CMC-7, respectivamente. Quando o número for coletado manualmente o formato é o sequinte: Compensação (3), Banco (3), Agencia(4), C1 (1), Conta Conrrente (10), C2 (1), Número do cheque (6), e C3(1), nesta ordem. Notar que estes campos são os
	 * que estão na parte superior de um cheque e na ordem apresentada.
	 */
	// Sugerimos que na coleta seja apresentada uma interface que perminta ao operador indentificar
	// e digitar adequadamente estas informações de forma que a consulta não seja feita com dados errados,
	// retornando como bom um cheque com problemas.
	public static final DedicatedAction LER_CHEQUE = new DedicatedAction( 31 );

	/**
	 * Deve ser lido um campo monetário ou seja, aceita o delimitador de centavos e devolvido no parâmetro Buffer.
	 */
	public static final DedicatedAction LER_VALOR = new DedicatedAction( 34 );

	/**
	 * Deve ser lido um código em barras ou o mesmo deve ser coletado manualmente. No retorno Buffer deve conter "0:" ou "1:" sequido do código em barras coletado manualmente ou pela leitora, respectivamente
	 */
	public static final DedicatedAction LER_CODIGO_DE_BARRAS = new DedicatedAction( 35 );

	private int code;

	private static final List<DedicatedAction> actions = new ArrayList<DedicatedAction>();
	static {
		actions.add( ERRO );
		actions.add( WARNING );
		actions.add( ARMAZENAR );
		actions.add( MENSAGEM_OPERADOR );
		actions.add( MENSAGEM_CLIENTE );
		actions.add( MENSAGEM_TODOS );
		actions.add( CABECALHO_MENU );
		actions.add( REMOVER_MESAGEM_OPERADOR );
		actions.add( REMOVER_MESAGEM_CLIENTE );
		actions.add( REMOVER_MESAGEM_TODOS );
		actions.add( REMOVER_CABECALHO_MENU );
		actions.add( CABECALHO );
		actions.add( REMOVER_CABECALHO );
		actions.add( RETORNAR_CONFIRMACAO );
		actions.add( MOSTRAR_MENU );
		actions.add( AGUADAR_TECLA_OPERADOR );
		actions.add( CONFIRMAR_IMTERRUPSAO );
		actions.add( LER_CAMPO );
		actions.add( LER_CHEQUE );
		actions.add( LER_VALOR );
		actions.add( LER_CODIGO_DE_BARRAS );
	}

	private DedicatedAction( final Integer code ) {

		this.code = code;
	}

	public int code() {

		return code;
	}

	public static DedicatedAction getDedicatedAction( final Integer code ) {

		for ( DedicatedAction da : actions ) {
			if ( da.code == code ) {
				return da;
			}
		}
		return null;
	}

	@Override
	public boolean equals( Object obj ) {

		if ( obj == null || !( obj instanceof DedicatedAction ) ) {
			return false;
		}
		return this.code == ( (DedicatedAction) obj ).code();
	}
}
