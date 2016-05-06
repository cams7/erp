
package org.freedom.ecf.driver;

public enum EStatus {
	RETORNO_OK( 0, "RETORNO_OK" ), IMPRESSORA_OK( 100, "Impressora OK" ), RETORNO_INDEFINIDO( 101, "Retorno indefinido: " ),
	// Status da impressora Bematech...
	BEMA_FIM_DE_PAPEL( 200, "Fim de papel." ), BEMA_POUCO_PAPEL( 201, "Pouco papel." ), BEMA_RELOGIO_ERROR( 203, "Erro no relógio." ), BEMA_IMPRESSORA_EM_ERRO( 204, "Impressora em erro." ), BEMA_NO_ESC( 205, "Primeiro dado do comando não foi ESC." ), BEMA_NO_COMMAND(
			206, "Comando inexistente." ), BEMA_CUPOM_FISCAL_ABERTO( 207, "Cupom fiscal aberto." ), BEMA_NU_PARAMS_INVALIDO( 208, "Número de parâmetro de CMD inválido." ), BEMA_TP_PARAM_INVALIDO( 209, "Tipo de parâmetro de CMD inválido." ), BEMA_OUT_OF_MEMORY(
			210, "Memória fiscal lotada." ), BEMA_MEMORY_ERROR( 211, "Erro na memória RAM CMOS não volátil." ), BEMA_NO_ALIQUOTA( 212, "Alíquota não programada." ), BEMA_OUT_OF_ALIQUOTA( 213, "Capacidade de alíquotas esgotada." ), BEMA_NO_ACESESS_CANCELAMENTO(
			214, "Cancelamento não permitido." ), BEMA_NO_CNPJ_IE( 215, "CNPJ/IE do proprietário não programados." ), BEMA_COMMAND_NO_EXECUTE( 216, "Comando não executado." ), BEMA_ERRO_COMUNICACAO( 0, "Erro de comunicação física." ), BEMA_PARAMETRO_INVALIDO(
			-2, "Parâmetro inválido na função." ), BEMA_ALIQUOTA_NAO_PROGRAMADA( -3, "Aliquota não programada." ), BEMA_ARQ_INI_NAO_ENCONTRADO( -4, "O arquivo de inicialização não encontrado." ), BEMA_ERRO_ABRIR_PORTA( -5,
			"Erro ao abrir a porta de comunicação." ), BEMA_ERRO_GRAVAR_RETORNO( -8, "Erro ao criar ou gravar no arquivo STATUS.TXT ou RETORNO.TXT" ), BEMA_NAO_STATUS_600( -27, "Status da impressora diferente de 6,0,0 (ACK, ST1 e ST2)" ), BEMA_FUNCAO_NAO_COMPATIVEL(
			-30, "Função não compatível com a impressora YANCO" ), BEMA_FORMA_PAGAMENTO_NAO_FINALIZADA( -31, "Forma de pagamento não finalizada" ),
	// Status da impressra daruma;
	DARUMA_ERROR_N1( -1, "Comando não implementado pelo driver de comunicação." ), DARUMA_ERROR_01( 1, "Comando habilitado somente em modo manutenção." ), DARUMA_ERROR_02( 2, "Falha na gravação da Memória Fiscal." ), DARUMA_ERROR_03( 3,
			"Capaciadade de Memória Fiscal esgotada." ), DARUMA_ERROR_04( 4, "Data fornecida não coincide com a data interna da IF." ), DARUMA_ERROR_05( 5, "Impressora Fiscal bloqueada por erro fiscal." ), DARUMA_ERROR_06( 6,
			"Erro no campo de controle da Memória Fiscal." ), DARUMA_ERROR_07( 7, "Existem uma memória fiscal instalada." ), DARUMA_ERROR_08( 8, "Senha incorreta." ), DARUMA_ERROR_09( 9, "Comando habilitado somente em modo operação." ), DARUMA_ERROR_10(
			10, "Documento aberto." ), DARUMA_ERROR_11( 11, "Documento não aberto." ), DARUMA_ERROR_12( 12, "Não a documento a cancelar." ), DARUMA_ERROR_13( 13, "Campo númerico com valores inválidos." ), DARUMA_ERROR_14( 14,
			"Capacidade máxima da memória foi atingida." ), DARUMA_ERROR_15( 15, "Item solicitado não foi encontrado." ), DARUMA_ERROR_16( 16, "Erro na sintaxe do comando." ), DARUMA_ERROR_17( 17, "Overflow nos cáuculos internos." ), DARUMA_ERROR_18(
			18, "Alíquota de inposto não definida para o totalizador selecionado." ), DARUMA_ERROR_19( 19, "Memória fiscal vazia." ), DARUMA_ERROR_20( 20, "Nenhum campo requer atualização." ), DARUMA_ERROR_21( 21, "Repita o comando de Redução Z." ), DARUMA_ERROR_22(
			22, "Redução Z do dia já foi executada." ), DARUMA_ERROR_23( 23, "Redução Z pendente." ), DARUMA_ERROR_24( 24, "Valor de desconto ou acrécimo inválido." ), DARUMA_ERROR_25( 25, "Caracteres não estampáveis foram encontrados." ), DARUMA_ERROR_26(
			26, "Comando não pode ser executado." ), DARUMA_ERROR_27( 27, "Operação abortada." ), DARUMA_ERROR_28( 28, "Campo númerico em zero não permitido." ), DARUMA_ERROR_29( 29, "Documento anterior não foi cupom fiscal." ), DARUMA_ERROR_30( 30,
			"Foi selecionado um documento não fiscal inválido ou não programando." ), DARUMA_ERROR_31( 31, "Não pode autenticar." ), DARUMA_ERROR_32( 32, "Desconto de INSS não habilitado" ), DARUMA_ERROR_33( 33,
			"Emita comprovantes não fiscais vinculados pendentes." ), DARUMA_ERROR_34( 34, "Impressora fiscal bloqueada por erro fiscal." ), DARUMA_ERROR_35( 35, "Relógio interno inoperante." ), DARUMA_ERROR_36( 36,
			"Versão do firmware gravada na MF não coincide ccom a esperada." ), DARUMA_ERROR_38( 38, "Foi selecionada uma forma de pagamento inválida." ), DARUMA_ERROR_39( 39, "Erro na sequencia de fechamento do documento." ), DARUMA_ERROR_40( 40,
			"Já foi emitido algum documento após a ultíma redução Z." ), DARUMA_ERROR_41( 41, "Data/Hora fornecida é anterior a última gravada na Memória Fiscal." ), DARUMA_ERROR_42( 42, "Leitura X do início do dia ainda não foi emitida." ), DARUMA_ERROR_43(
			43, "Não pode mais emitir CNF Vinculado solicitado." ), DARUMA_ERROR_44( 44, "Forma de pagamento já definida." ), DARUMA_ERROR_45( 45, "Campo em brando ou contendo caracter de controle." ), DARUMA_ERROR_47( 47,
			"Nenhum periférico homologado conectado a porta auxiliar." ), DARUMA_ERROR_48( 48, "Valor do estorno superior ao total acumulado nesta forma de pagamento." ), DARUMA_ERROR_49( 49,
			"Forma de pagamento a ser estornada não foi encontrada na memória." ), DARUMA_ERROR_50( 50, "Impressora sem papel." ), DARUMA_ERROR_61( 61, "Operação interrompida por falta de energia elétrica." ), DARUMA_ERROR_70( 71,
			"Falha na comunidação com o módulo impressor." ), DARUMA_ERROR_80( 81, "Periférico conectado a porta auxiliar não homologado." ), DARUMA_ERROR_81( 82, "Banco não cadastrado." ), DARUMA_ERROR_82( 83, "Nada a imprimir." ), DARUMA_ERROR_83(
			84, "Extenso não cabe no cheque." ), DARUMA_ERROR_84( 85, "Leitor de CMC-7 não instalado." ), DARUMA_ERROR_86( 86, "Não há mais memória para o cadastro de bancos." ), DARUMA_ERROR_87( 87,
			"Impressão no verso somento após a impressão da frente do cheque." ), DARUMA_ERROR_88( 88, "Valor inválido para o cheque." ), DARUMA_WARNING_01( 1001, "Near End foi detectado." ), DARUMA_WARNING_02( 1002, "Execute redução Z." ), DARUMA_WARNING_04(
			1004, "Queda de energia." ), DARUMA_WARNING_10( 1010, "Bateria interna requer substituição." ), DARUMA_WARNING_20( 1020, "Operação habilitada somente em MIT." ), DARUMA_STATUS_S1_B3_0( 2130, 3, "Gaveta fechada." ), DARUMA_STATUS_S1_B3_1(
			2131, 3, "Gaveta aberta." ), DARUMA_STATUS_S1_B1_0( 2110, 3, "Tampa fechada." ), DARUMA_STATUS_S1_B1_1( 2111, 1, "Tampa aberta." ), DARUMA_STATUS_S1_B0_0( 2100, 1, "Modo bobina não selecionado." ), DARUMA_STATUS_S1_B0_1( 2101, 3,
			"Selecionado modo bobina." ), DARUMA_STATUS_S2_B3_0( 2230, 3, "Slip não selecionado." ), DARUMA_STATUS_S2_B3_1( 2231, 3, "Slip presente." ), DARUMA_STATUS_S2_B2_0( 2222, 1, "Sem documento em posição de autenticação." ), DARUMA_STATUS_S2_B2_1(
			2220, 2, "Documento posicionado para autenticação." ), DARUMA_STATUS_S2_B1_0( 2210, 3, "Papel presente." ), DARUMA_STATUS_S2_B1_1( 2211, 1, "Fim da bobina de papel." ), DARUMA_STATUS_S2_B0_0( 2200, 3, "Bobina de papel OK." ), DARUMA_STATUS_S2_B0_1(
			2201, 2, "Near end detectado." ), DARUMA_STATUS_S3_B3_0( 2330, 2, "Modo manutenção." ), DARUMA_STATUS_S3_B3_1( 2331, 3, "Modo operação." ), DARUMA_STATUS_S3_B2_0( 2320, 3, "Estrape de operação fechado." ), DARUMA_STATUS_S3_B2_1( 2321, 3,
			"Estrape de operação aberto." ), DARUMA_STATUS_S3_B1_0( 2310, 3, "Impressora operacional." ), DARUMA_STATUS_S3_B1_1( 2311, 1, "Impressora com erro Fiscal(Bloqueada)." ), DARUMA_STATUS_S3_B0_0( 2300, 3, "Impressora On Line." ), DARUMA_STATUS_S3_B0_1(
			2301, 2, "Impressora Off Line." ), DARUMA_STATUS_S4_B2_0( 2420, 3, "Impressão em operação." ), DARUMA_STATUS_S4_B2_1( 2421, 1, "Redução Z de hoje já emitido." ), DARUMA_STATUS_S4_B1_0( 2410, 1,
			"Leitura X do início do dia ainda não emitida." ), DARUMA_STATUS_S4_B1_1( 2411, 1, "Leitura X do início do dia já emitida." ), DARUMA_STATUS_S4_B0_0( 2400, 3, "Pode configurar a IF." ), DARUMA_STATUS_S4_B0_1( 2401, 1,
			"Emitiu algum documento após a redução Z." ), DARUMA_STATUS_S5_B3_0( 2530, 3, "Modo normal." ), DARUMA_STATUS_S5_B3_1( 2531, 2, "Modo treinamento." ), DARUMA_STATUS_S5_B2_0( 2520, 3, "MF presente." ), DARUMA_STATUS_S5_B2_1( 2521, 3,
			"MF ausente ou não inicializada." ), DARUMA_STATUS_S5_B1_0( 2510, 3, "Buffer de comunicação não vazio." ), DARUMA_STATUS_S5_B1_1( 2511, 3, "Buffer de coumunicação vazio." ), DARUMA_STATUS_S5_B0_0( 2500, 3, "Impressão em andamento." ), DARUMA_STATUS_S5_B0_1(
			2501, 3, "Impressão encerrada." ), DARUMA_STATUS_S6_B3_0( 2630, 3, "Não imprimindo slip." ), DARUMA_STATUS_S6_B3_1( 2631, 3, "Imprimindo slip." ), DARUMA_STATUS_S6_B2_0( 2620, 1, "Não autenticado." ), DARUMA_STATUS_S6_B2_1( 2621, 3,
			"Autenticado." ), DARUMA_STATUS_S7_B3_0( 2730, 1, "Falha de energia." ), DARUMA_STATUS_S7_B3_1( 2731, 3, "VAC superior a 90V." ), DARUMA_STATUS_S7_B2_0( 2720, 3, "Substitua bateria do RTC." ), DARUMA_STATUS_S7_B2_1( 2721, 3,
			"Bateria OK." ), DARUMA_STATUS_S7_B0_0( 2700, 3, "MF de 1M Bytes." ), DARUMA_STATUS_S7_B0_1( 2701, 3, "MF de 512K Bytes." ), DARUMA_STATUS_S9_B3_0( 2930, 3, "Checksum da MF atualizado." ), DARUMA_STATUS_S9_B3_1( 2931, 3,
			"Atualizando checksum da MF." ), DARUMA_STATUS_S9_B0_0( 2900, 3, "Totalizador fiscais OK." ), DARUMA_STATUS_S9_B0_1( 2901, 1, "Erro de consistência nos totalizadores fiscais." ), DARUMA_STATUS_S10_B3_0( 21030, 3, "MF Ok." ), DARUMA_STATUS_S10_B3_1(
			21031, 1, "Erro na leitura da MF ou MF substituida." ), DARUMA_STATUS_S10_B2_0( 21020, 3, "Gravação da MF Ok." ), DARUMA_STATUS_S10_B2_1( 21021, 1, "Erro na gravação da MF." ), DARUMA_STATUS_S10_B1_0( 21010, 3, "Relógio interno Ok." ), DARUMA_STATUS_S10_B1_1(
			21011, 1, "Erro no relógio interno." ), DARUMA_STATUS_S10_B0_0( 21000, 3, "Clichê do proprietário Ok." ), DARUMA_STATUS_S10_B0_1( 21001, 3, "Clichê do prorietário danificado." );

	private String message;

	private int code;

	private int relevanc;

	EStatus( final int code, final String message ) {

		this.code = code;
		this.message = message;
	}

	EStatus( final int code, final int relevanc, final String message ) {

		this.code = code;
		this.message = message;
		this.relevanc = relevanc;
	}

	public void setMessage( final String arg ) {

		this.message = arg;
	}

	public String getMessage() {

		return this.message;
	}

	public int getCode() {

		return this.code;
	}

	public int getRelevanc() {

		return this.relevanc;
	}
}
