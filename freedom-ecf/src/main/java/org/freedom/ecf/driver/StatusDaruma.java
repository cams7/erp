
package org.freedom.ecf.driver;

import java.util.ArrayList;
import java.util.List;

public class StatusDaruma implements Status {

	public static final StatusDaruma DARUMA_ERROR_01 = new StatusDaruma( 1, RELEVANC_ERRO, "Comando habilitado somente em modo manutenção." );

	public static final StatusDaruma DARUMA_ERROR_02 = new StatusDaruma( 2, RELEVANC_ERRO, "Falha na gravação da Memória Fiscal." );

	public static final StatusDaruma DARUMA_ERROR_03 = new StatusDaruma( 3, RELEVANC_ERRO, "Capaciadade de Memória Fiscal esgotada." );

	public static final StatusDaruma DARUMA_ERROR_04 = new StatusDaruma( 4, RELEVANC_ERRO, "Data fornecida não coincide com a data interna da IF." );

	public static final StatusDaruma DARUMA_ERROR_05 = new StatusDaruma( 5, RELEVANC_ERRO, "Impressora Fiscal bloqueada por erro fiscal." );

	public static final StatusDaruma DARUMA_ERROR_06 = new StatusDaruma( 6, RELEVANC_ERRO, "Erro no campo de controle da Memória Fiscal." );

	public static final StatusDaruma DARUMA_ERROR_07 = new StatusDaruma( 7, RELEVANC_ERRO, "Existem uma memória fiscal instalada." );

	public static final StatusDaruma DARUMA_ERROR_08 = new StatusDaruma( 8, RELEVANC_ERRO, "Senha incorreta." );

	public static final StatusDaruma DARUMA_ERROR_09 = new StatusDaruma( 9, RELEVANC_ERRO, "Comando habilitado somente em modo operação." );

	public static final StatusDaruma DARUMA_ERROR_10 = new StatusDaruma( 10, RELEVANC_ERRO, "Documento aberto." );

	public static final StatusDaruma DARUMA_ERROR_11 = new StatusDaruma( 11, RELEVANC_ERRO, "Documento não aberto." );

	public static final StatusDaruma DARUMA_ERROR_12 = new StatusDaruma( 12, RELEVANC_ERRO, "Não a documento a cancelar." );

	public static final StatusDaruma DARUMA_ERROR_13 = new StatusDaruma( 13, RELEVANC_ERRO, "Campo númerico com valores inválidos." );

	public static final StatusDaruma DARUMA_ERROR_14 = new StatusDaruma( 14, RELEVANC_ERRO, "Capacidade máxima da memória foi atingida." );

	public static final StatusDaruma DARUMA_ERROR_15 = new StatusDaruma( 15, RELEVANC_ERRO, "Item solicitado não foi encontrado." );

	public static final StatusDaruma DARUMA_ERROR_16 = new StatusDaruma( 16, RELEVANC_ERRO, "Erro na sintaxe do comando." );

	public static final StatusDaruma DARUMA_ERROR_17 = new StatusDaruma( 17, RELEVANC_ERRO, "Overflow nos cáuculos internos." );

	public static final StatusDaruma DARUMA_ERROR_18 = new StatusDaruma( 18, RELEVANC_ERRO, "Alíquota de inposto não definida para o totalizador selecionado." );

	public static final StatusDaruma DARUMA_ERROR_19 = new StatusDaruma( 19, RELEVANC_ERRO, "Memória fiscal vazia." );

	public static final StatusDaruma DARUMA_ERROR_20 = new StatusDaruma( 20, RELEVANC_ERRO, "Nenhum campo requer atualização." );

	public static final StatusDaruma DARUMA_ERROR_21 = new StatusDaruma( 21, RELEVANC_ERRO, "Repita o comando de Redução Z." );

	public static final StatusDaruma DARUMA_ERROR_22 = new StatusDaruma( 22, RELEVANC_ERRO, "Redução Z do dia já foi executada." );

	public static final StatusDaruma DARUMA_ERROR_23 = new StatusDaruma( 23, RELEVANC_ERRO, "Redução Z pendente." );

	public static final StatusDaruma DARUMA_ERROR_24 = new StatusDaruma( 24, RELEVANC_ERRO, "Valor de desconto ou acrécimo inválido." );

	public static final StatusDaruma DARUMA_ERROR_25 = new StatusDaruma( 25, RELEVANC_ERRO, "Caracteres não estampáveis foram encontrados." );

	public static final StatusDaruma DARUMA_ERROR_26 = new StatusDaruma( 26, RELEVANC_ERRO, "Comando não pode ser executado." );

	public static final StatusDaruma DARUMA_ERROR_27 = new StatusDaruma( 27, RELEVANC_ERRO, "Operação abortada." );

	public static final StatusDaruma DARUMA_ERROR_28 = new StatusDaruma( 28, RELEVANC_ERRO, "Campo númerico em zero não permitido." );

	public static final StatusDaruma DARUMA_ERROR_29 = new StatusDaruma( 29, RELEVANC_ERRO, "Documento anterior não foi cupom fiscal." );

	public static final StatusDaruma DARUMA_ERROR_30 = new StatusDaruma( 30, RELEVANC_ERRO, "Foi selecionado um documento não fiscal inválido ou não programando." );

	public static final StatusDaruma DARUMA_ERROR_31 = new StatusDaruma( 31, RELEVANC_ERRO, "Não pode autenticar." );

	public static final StatusDaruma DARUMA_ERROR_32 = new StatusDaruma( 32, RELEVANC_ERRO, "Desconto de INSS não habilitado" );

	public static final StatusDaruma DARUMA_ERROR_33 = new StatusDaruma( 33, RELEVANC_ERRO, "Emita comprovantes não fiscais vinculados pendentes." );

	public static final StatusDaruma DARUMA_ERROR_34 = new StatusDaruma( 34, RELEVANC_ERRO, "Impressora fiscal bloqueada por erro fiscal." );

	public static final StatusDaruma DARUMA_ERROR_35 = new StatusDaruma( 35, RELEVANC_ERRO, "Relógio interno inoperante." );

	public static final StatusDaruma DARUMA_ERROR_36 = new StatusDaruma( 36, RELEVANC_ERRO, "Versão do firmware gravada na MF não coincide ccom a esperada." );

	public static final StatusDaruma DARUMA_ERROR_38 = new StatusDaruma( 38, RELEVANC_ERRO, "Foi selecionada uma forma de pagamento inválida." );

	public static final StatusDaruma DARUMA_ERROR_39 = new StatusDaruma( 39, RELEVANC_ERRO, "Erro na sequencia de fechamento do documento." );

	public static final StatusDaruma DARUMA_ERROR_40 = new StatusDaruma( 40, RELEVANC_ERRO, "Já foi emitido algum documento após a ultíma redução Z." );

	public static final StatusDaruma DARUMA_ERROR_41 = new StatusDaruma( 41, RELEVANC_ERRO, "Data/Hora fornecida é anterior a última gravada na Memória Fiscal." );

	public static final StatusDaruma DARUMA_ERROR_42 = new StatusDaruma( 42, RELEVANC_ERRO, "Leitura X do início do dia ainda não foi emitida." );

	public static final StatusDaruma DARUMA_ERROR_43 = new StatusDaruma( 43, RELEVANC_ERRO, "Não pode mais emitir CNF Vinculado solicitado." );

	public static final StatusDaruma DARUMA_ERROR_44 = new StatusDaruma( 44, RELEVANC_ERRO, "Forma de pagamento já definida." );

	public static final StatusDaruma DARUMA_ERROR_45 = new StatusDaruma( 45, RELEVANC_ERRO, "Campo em brando ou contendo caracter de controle." );

	public static final StatusDaruma DARUMA_ERROR_47 = new StatusDaruma( 47, RELEVANC_ERRO, "Nenhum periférico homologado conectado a porta auxiliar." );

	public static final StatusDaruma DARUMA_ERROR_48 = new StatusDaruma( 48, RELEVANC_ERRO, "Valor do estorno superior ao total acumulado nesta forma de pagamento." );

	public static final StatusDaruma DARUMA_ERROR_49 = new StatusDaruma( 49, RELEVANC_ERRO, "Forma de pagamento a ser estornada não foi encontrada na memória." );

	public static final StatusDaruma DARUMA_ERROR_50 = new StatusDaruma( 50, RELEVANC_ERRO, "Impressora sem papel." );

	public static final StatusDaruma DARUMA_ERROR_61 = new StatusDaruma( 61, RELEVANC_ERRO, "Operação interrompida por falta de energia elétrica." );

	public static final StatusDaruma DARUMA_ERROR_70 = new StatusDaruma( 71, RELEVANC_ERRO, "Falha na comunidação com o módulo impressor." );

	public static final StatusDaruma DARUMA_ERROR_80 = new StatusDaruma( 81, RELEVANC_ERRO, "Periférico conectado a porta auxiliar não homologado." );

	public static final StatusDaruma DARUMA_ERROR_81 = new StatusDaruma( 82, RELEVANC_ERRO, "Banco não cadastrado." );

	public static final StatusDaruma DARUMA_ERROR_82 = new StatusDaruma( 83, RELEVANC_ERRO, "Nada a imprimir." );

	public static final StatusDaruma DARUMA_ERROR_83 = new StatusDaruma( 84, RELEVANC_ERRO, "Extenso não cabe no cheque." );

	public static final StatusDaruma DARUMA_ERROR_84 = new StatusDaruma( 85, RELEVANC_ERRO, "Leitor de CMC-7 não instalado." );

	public static final StatusDaruma DARUMA_ERROR_86 = new StatusDaruma( 86, RELEVANC_ERRO, "Não há mais memória para o cadastro de bancos." );

	public static final StatusDaruma DARUMA_ERROR_87 = new StatusDaruma( 87, RELEVANC_ERRO, "Impressão no verso somento após a impressão da frente do cheque." );

	public static final StatusDaruma DARUMA_ERROR_88 = new StatusDaruma( 88, RELEVANC_ERRO, "Valor inválido para o cheque." );

	public static final StatusDaruma DARUMA_WARNING_01 = new StatusDaruma( 1001, RELEVANC_WARNING, "Near End foi detectado." );

	public static final StatusDaruma DARUMA_WARNING_02 = new StatusDaruma( 1002, RELEVANC_WARNING, "Execute redução Z." );

	public static final StatusDaruma DARUMA_WARNING_04 = new StatusDaruma( 1004, RELEVANC_WARNING, "Queda de energia." );

	public static final StatusDaruma DARUMA_WARNING_10 = new StatusDaruma( 1010, RELEVANC_WARNING, "Bateria interna requer substituição." );

	public static final StatusDaruma DARUMA_WARNING_20 = new StatusDaruma( 1020, RELEVANC_WARNING, "Operação habilitada somente em MIT." );

	public static final StatusDaruma DARUMA_STATUS_S1_B3_0 = new StatusDaruma( 2130, RELEVANC_MESSAGE, "Gaveta fechada." );

	public static final StatusDaruma DARUMA_STATUS_S1_B3_1 = new StatusDaruma( 2131, RELEVANC_MESSAGE, "Gaveta aberta." );

	public static final StatusDaruma DARUMA_STATUS_S1_B1_0 = new StatusDaruma( 2110, RELEVANC_MESSAGE, "Tampa fechada." );

	public static final StatusDaruma DARUMA_STATUS_S1_B1_1 = new StatusDaruma( 2111, RELEVANC_ERRO, "Tampa aberta." );

	public static final StatusDaruma DARUMA_STATUS_S1_B0_0 = new StatusDaruma( 2100, RELEVANC_ERRO, "Modo bobina não selecionado." );

	public static final StatusDaruma DARUMA_STATUS_S1_B0_1 = new StatusDaruma( 2101, RELEVANC_MESSAGE, "Selecionado modo bobina." );

	public static final StatusDaruma DARUMA_STATUS_S2_B3_0 = new StatusDaruma( 2230, RELEVANC_MESSAGE, "Slip não selecionado." );

	public static final StatusDaruma DARUMA_STATUS_S2_B3_1 = new StatusDaruma( 2231, RELEVANC_MESSAGE, "Slip presente." );

	public static final StatusDaruma DARUMA_STATUS_S2_B2_0 = new StatusDaruma( 2222, RELEVANC_ERRO, "Sem documento em posição de autenticação." );

	public static final StatusDaruma DARUMA_STATUS_S2_B2_1 = new StatusDaruma( 2220, RELEVANC_WARNING, "Documento posicionado para autenticação." );

	public static final StatusDaruma DARUMA_STATUS_S2_B1_0 = new StatusDaruma( 2210, RELEVANC_MESSAGE, "Papel presente." );

	public static final StatusDaruma DARUMA_STATUS_S2_B1_1 = new StatusDaruma( 2211, RELEVANC_ERRO, "Fim da bobina de papel." );

	public static final StatusDaruma DARUMA_STATUS_S2_B0_0 = new StatusDaruma( 2200, RELEVANC_MESSAGE, "Bobina de papel OK." );

	public static final StatusDaruma DARUMA_STATUS_S2_B0_1 = new StatusDaruma( 2201, RELEVANC_WARNING, "Near end detectado." );

	public static final StatusDaruma DARUMA_STATUS_S3_B3_0 = new StatusDaruma( 2330, RELEVANC_WARNING, "Modo manutenção." );

	public static final StatusDaruma DARUMA_STATUS_S3_B3_1 = new StatusDaruma( 2331, RELEVANC_MESSAGE, "Modo operação." );

	public static final StatusDaruma DARUMA_STATUS_S3_B2_0 = new StatusDaruma( 2320, RELEVANC_MESSAGE, "Estrape de operação fechado." );

	public static final StatusDaruma DARUMA_STATUS_S3_B2_1 = new StatusDaruma( 2321, RELEVANC_MESSAGE, "Estrape de operação aberto." );

	public static final StatusDaruma DARUMA_STATUS_S3_B1_0 = new StatusDaruma( 2310, RELEVANC_MESSAGE, "Impressora operacional." );

	public static final StatusDaruma DARUMA_STATUS_S3_B1_1 = new StatusDaruma( 2311, RELEVANC_ERRO, "Impressora com erro Fiscal = new StatusDaruma(Bloqueada)." );

	public static final StatusDaruma DARUMA_STATUS_S3_B0_0 = new StatusDaruma( 2300, RELEVANC_MESSAGE, "Impressora On Line." );

	public static final StatusDaruma DARUMA_STATUS_S3_B0_1 = new StatusDaruma( 2301, RELEVANC_WARNING, "Impressora Off Line." );

	public static final StatusDaruma DARUMA_STATUS_S4_B2_0 = new StatusDaruma( 2420, RELEVANC_MESSAGE, "Impressão em operação." );

	public static final StatusDaruma DARUMA_STATUS_S4_B2_1 = new StatusDaruma( 2421, RELEVANC_ERRO, "Redução Z de hoje já emitido." );

	public static final StatusDaruma DARUMA_STATUS_S4_B1_0 = new StatusDaruma( 2410, RELEVANC_ERRO, "Leitura X do início do dia ainda não emitida." );

	public static final StatusDaruma DARUMA_STATUS_S4_B1_1 = new StatusDaruma( 2411, RELEVANC_ERRO, "Leitura X do início do dia já emitida." );

	public static final StatusDaruma DARUMA_STATUS_S4_B0_0 = new StatusDaruma( 2400, RELEVANC_MESSAGE, "Pode configurar a IF." );

	public static final StatusDaruma DARUMA_STATUS_S4_B0_1 = new StatusDaruma( 2401, RELEVANC_ERRO, "Emitiu algum documento após a redução Z." );

	public static final StatusDaruma DARUMA_STATUS_S5_B3_0 = new StatusDaruma( 2530, RELEVANC_MESSAGE, "Modo normal." );

	public static final StatusDaruma DARUMA_STATUS_S5_B3_1 = new StatusDaruma( 2531, RELEVANC_WARNING, "Modo treinamento." );

	public static final StatusDaruma DARUMA_STATUS_S5_B2_0 = new StatusDaruma( 2520, RELEVANC_MESSAGE, "MF presente." );

	public static final StatusDaruma DARUMA_STATUS_S5_B2_1 = new StatusDaruma( 2521, RELEVANC_MESSAGE, "MF ausente ou não inicializada." );

	public static final StatusDaruma DARUMA_STATUS_S5_B1_0 = new StatusDaruma( 2510, RELEVANC_MESSAGE, "Buffer de comunicação não vazio." );

	public static final StatusDaruma DARUMA_STATUS_S5_B1_1 = new StatusDaruma( 2511, RELEVANC_MESSAGE, "Buffer de coumunicação vazio." );

	public static final StatusDaruma DARUMA_STATUS_S5_B0_0 = new StatusDaruma( 2500, RELEVANC_MESSAGE, "Impressão em andamento." );

	public static final StatusDaruma DARUMA_STATUS_S5_B0_1 = new StatusDaruma( 2501, RELEVANC_MESSAGE, "Impressão encerrada." );

	public static final StatusDaruma DARUMA_STATUS_S6_B3_0 = new StatusDaruma( 2630, RELEVANC_MESSAGE, "Não imprimindo slip." );

	public static final StatusDaruma DARUMA_STATUS_S6_B3_1 = new StatusDaruma( 2631, RELEVANC_MESSAGE, "Imprimindo slip." );

	public static final StatusDaruma DARUMA_STATUS_S6_B2_0 = new StatusDaruma( 2620, RELEVANC_ERRO, "Não autenticado." );

	public static final StatusDaruma DARUMA_STATUS_S6_B2_1 = new StatusDaruma( 2621, RELEVANC_MESSAGE, "Autenticado." );

	public static final StatusDaruma DARUMA_STATUS_S7_B3_0 = new StatusDaruma( 2730, RELEVANC_ERRO, "Falha de energia." );

	public static final StatusDaruma DARUMA_STATUS_S7_B3_1 = new StatusDaruma( 2731, RELEVANC_MESSAGE, "VAC superior a 90V." );

	public static final StatusDaruma DARUMA_STATUS_S7_B2_0 = new StatusDaruma( 2720, RELEVANC_MESSAGE, "Substitua bateria do RTC." );

	public static final StatusDaruma DARUMA_STATUS_S7_B2_1 = new StatusDaruma( 2721, RELEVANC_MESSAGE, "Bateria OK." );

	public static final StatusDaruma DARUMA_STATUS_S7_B0_0 = new StatusDaruma( 2700, RELEVANC_MESSAGE, "MF de 1M Bytes." );

	public static final StatusDaruma DARUMA_STATUS_S7_B0_1 = new StatusDaruma( 2701, RELEVANC_MESSAGE, "MF de 512K Bytes." );

	public static final StatusDaruma DARUMA_STATUS_S9_B3_0 = new StatusDaruma( 2930, RELEVANC_MESSAGE, "Checksum da MF atualizado." );

	public static final StatusDaruma DARUMA_STATUS_S9_B3_1 = new StatusDaruma( 2931, RELEVANC_MESSAGE, "Atualizando checksum da MF." );

	public static final StatusDaruma DARUMA_STATUS_S9_B0_0 = new StatusDaruma( 2900, RELEVANC_MESSAGE, "Totalizador fiscais OK." );

	public static final StatusDaruma DARUMA_STATUS_S9_B0_1 = new StatusDaruma( 2901, RELEVANC_ERRO, "Erro de consistência nos totalizadores fiscais." );

	public static final StatusDaruma DARUMA_STATUS_S10_B3_0 = new StatusDaruma( 21030, RELEVANC_MESSAGE, "MF Ok." );

	public static final StatusDaruma DARUMA_STATUS_S10_B3_1 = new StatusDaruma( 21031, RELEVANC_ERRO, "Erro na leitura da MF ou MF substituida." );

	public static final StatusDaruma DARUMA_STATUS_S10_B2_0 = new StatusDaruma( 21020, RELEVANC_MESSAGE, "Gravação da MF Ok." );

	public static final StatusDaruma DARUMA_STATUS_S10_B2_1 = new StatusDaruma( 21021, RELEVANC_ERRO, "Erro na gravação da MF." );

	public static final StatusDaruma DARUMA_STATUS_S10_B1_0 = new StatusDaruma( 21010, RELEVANC_MESSAGE, "Relógio interno Ok." );

	public static final StatusDaruma DARUMA_STATUS_S10_B1_1 = new StatusDaruma( 21011, RELEVANC_ERRO, "Erro no relógio interno." );

	public static final StatusDaruma DARUMA_STATUS_S10_B0_0 = new StatusDaruma( 21000, RELEVANC_MESSAGE, "Clichê do proprietário Ok." );

	public static final StatusDaruma DARUMA_STATUS_S10_B0_1 = new StatusDaruma( 21001, RELEVANC_MESSAGE, "Clichê do prorietário danificado." );

	private static final List<StatusDaruma> statusList = new ArrayList<StatusDaruma>();

	private String message;

	private int code;

	private int relevanc;
	static {
		statusList.add( DARUMA_ERROR_01 );
		statusList.add( DARUMA_ERROR_02 );
		statusList.add( DARUMA_ERROR_03 );
		statusList.add( DARUMA_ERROR_04 );
		statusList.add( DARUMA_ERROR_05 );
		statusList.add( DARUMA_ERROR_06 );
		statusList.add( DARUMA_ERROR_07 );
		statusList.add( DARUMA_ERROR_08 );
		statusList.add( DARUMA_ERROR_09 );
		statusList.add( DARUMA_ERROR_10 );
		statusList.add( DARUMA_ERROR_11 );
		statusList.add( DARUMA_ERROR_12 );
		statusList.add( DARUMA_ERROR_13 );
		statusList.add( DARUMA_ERROR_14 );
		statusList.add( DARUMA_ERROR_15 );
		statusList.add( DARUMA_ERROR_16 );
		statusList.add( DARUMA_ERROR_17 );
		statusList.add( DARUMA_ERROR_18 );
		statusList.add( DARUMA_ERROR_19 );
		statusList.add( DARUMA_ERROR_20 );
		statusList.add( DARUMA_ERROR_21 );
		statusList.add( DARUMA_ERROR_22 );
		statusList.add( DARUMA_ERROR_23 );
		statusList.add( DARUMA_ERROR_24 );
		statusList.add( DARUMA_ERROR_25 );
		statusList.add( DARUMA_ERROR_26 );
		statusList.add( DARUMA_ERROR_27 );
		statusList.add( DARUMA_ERROR_28 );
		statusList.add( DARUMA_ERROR_29 );
		statusList.add( DARUMA_ERROR_30 );
		statusList.add( DARUMA_ERROR_31 );
		statusList.add( DARUMA_ERROR_32 );
		statusList.add( DARUMA_ERROR_33 );
		statusList.add( DARUMA_ERROR_34 );
		statusList.add( DARUMA_ERROR_35 );
		statusList.add( DARUMA_ERROR_36 );
		statusList.add( DARUMA_ERROR_38 );
		statusList.add( DARUMA_ERROR_39 );
		statusList.add( DARUMA_ERROR_40 );
		statusList.add( DARUMA_ERROR_41 );
		statusList.add( DARUMA_ERROR_42 );
		statusList.add( DARUMA_ERROR_43 );
		statusList.add( DARUMA_ERROR_44 );
		statusList.add( DARUMA_ERROR_45 );
		statusList.add( DARUMA_ERROR_47 );
		statusList.add( DARUMA_ERROR_48 );
		statusList.add( DARUMA_ERROR_49 );
		statusList.add( DARUMA_ERROR_50 );
		statusList.add( DARUMA_ERROR_61 );
		statusList.add( DARUMA_ERROR_70 );
		statusList.add( DARUMA_ERROR_80 );
		statusList.add( DARUMA_ERROR_81 );
		statusList.add( DARUMA_ERROR_82 );
		statusList.add( DARUMA_ERROR_83 );
		statusList.add( DARUMA_ERROR_84 );
		statusList.add( DARUMA_ERROR_86 );
		statusList.add( DARUMA_ERROR_87 );
		statusList.add( DARUMA_ERROR_88 );
		statusList.add( DARUMA_WARNING_01 );
		statusList.add( DARUMA_WARNING_02 );
		statusList.add( DARUMA_WARNING_04 );
		statusList.add( DARUMA_WARNING_10 );
		statusList.add( DARUMA_WARNING_20 );
		statusList.add( DARUMA_STATUS_S1_B3_0 );
		statusList.add( DARUMA_STATUS_S1_B3_1 );
		statusList.add( DARUMA_STATUS_S1_B1_0 );
		statusList.add( DARUMA_STATUS_S1_B1_1 );
		statusList.add( DARUMA_STATUS_S1_B0_0 );
		statusList.add( DARUMA_STATUS_S1_B0_1 );
		statusList.add( DARUMA_STATUS_S2_B3_0 );
		statusList.add( DARUMA_STATUS_S2_B3_1 );
		statusList.add( DARUMA_STATUS_S2_B2_0 );
		statusList.add( DARUMA_STATUS_S2_B2_1 );
		statusList.add( DARUMA_STATUS_S2_B1_0 );
		statusList.add( DARUMA_STATUS_S2_B1_1 );
		statusList.add( DARUMA_STATUS_S2_B0_0 );
		statusList.add( DARUMA_STATUS_S2_B0_1 );
		statusList.add( DARUMA_STATUS_S3_B3_0 );
		statusList.add( DARUMA_STATUS_S3_B3_1 );
		statusList.add( DARUMA_STATUS_S3_B2_0 );
		statusList.add( DARUMA_STATUS_S3_B2_1 );
		statusList.add( DARUMA_STATUS_S3_B1_0 );
		statusList.add( DARUMA_STATUS_S3_B1_1 );
		statusList.add( DARUMA_STATUS_S3_B0_0 );
		statusList.add( DARUMA_STATUS_S3_B0_1 );
		statusList.add( DARUMA_STATUS_S4_B2_0 );
		statusList.add( DARUMA_STATUS_S4_B2_1 );
		statusList.add( DARUMA_STATUS_S4_B1_0 );
		statusList.add( DARUMA_STATUS_S4_B1_1 );
		statusList.add( DARUMA_STATUS_S4_B0_0 );
		statusList.add( DARUMA_STATUS_S4_B0_1 );
		statusList.add( DARUMA_STATUS_S5_B3_0 );
		statusList.add( DARUMA_STATUS_S5_B3_1 );
		statusList.add( DARUMA_STATUS_S5_B2_0 );
		statusList.add( DARUMA_STATUS_S5_B2_1 );
		statusList.add( DARUMA_STATUS_S5_B1_0 );
		statusList.add( DARUMA_STATUS_S5_B1_1 );
		statusList.add( DARUMA_STATUS_S5_B0_0 );
		statusList.add( DARUMA_STATUS_S5_B0_1 );
		statusList.add( DARUMA_STATUS_S6_B3_0 );
		statusList.add( DARUMA_STATUS_S6_B3_1 );
		statusList.add( DARUMA_STATUS_S6_B2_0 );
		statusList.add( DARUMA_STATUS_S6_B2_1 );
		statusList.add( DARUMA_STATUS_S7_B3_0 );
		statusList.add( DARUMA_STATUS_S7_B3_1 );
		statusList.add( DARUMA_STATUS_S7_B2_0 );
		statusList.add( DARUMA_STATUS_S7_B2_1 );
		statusList.add( DARUMA_STATUS_S7_B0_0 );
		statusList.add( DARUMA_STATUS_S7_B0_1 );
		statusList.add( DARUMA_STATUS_S9_B3_0 );
		statusList.add( DARUMA_STATUS_S9_B3_1 );
		statusList.add( DARUMA_STATUS_S9_B0_0 );
		statusList.add( DARUMA_STATUS_S9_B0_1 );
		statusList.add( DARUMA_STATUS_S10_B3_0 );
		statusList.add( DARUMA_STATUS_S10_B3_1 );
		statusList.add( DARUMA_STATUS_S10_B2_0 );
		statusList.add( DARUMA_STATUS_S10_B2_1 );
		statusList.add( DARUMA_STATUS_S10_B1_0 );
		statusList.add( DARUMA_STATUS_S10_B1_1 );
		statusList.add( DARUMA_STATUS_S10_B0_0 );
		statusList.add( DARUMA_STATUS_S10_B0_1 );
	}

	public static StatusDaruma getStatusDaruma( int code ) {

		StatusDaruma statusDaruma = null;
		for ( StatusDaruma sd : statusList ) {
			if ( sd.getCode() == code ) {
				statusDaruma = sd;
				break;
			}
		}
		return statusDaruma;
	}

	public StatusDaruma( int code, int relevanc, String message ) {

		this.code = code;
		this.relevanc = relevanc;
		this.message = message;
	}

	public void setCode( int code ) {

		this.code = code;
	}

	public int getCode() {

		return code;
	}

	public void setMessage( String message ) {

		this.message = message;
	}

	public String getMessage() {

		return message;
	}

	public void setRelevanc( int relevanc ) {

		this.relevanc = relevanc;
	}

	public int getRelevanc() {

		return relevanc;
	}
}
