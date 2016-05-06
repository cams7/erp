
package org.freedom.ecf.driver;

import java.util.ArrayList;
import java.util.List;

public class StatusElginX5 implements Status {

	public static StatusElginX5 ErroGeralFalRAM = new StatusElginX5( 1, RELEVANC_ERRO, "Não foi possível alocar mais memória" );

	public static StatusElginX5 ErroGeralPerdaRAM = new StatusElginX5( 2, RELEVANC_ERRO, "Memória RAM foi corrompida" );

	public static StatusElginX5 ErroMFDesconectada = new StatusElginX5( 1000, RELEVANC_ERRO, "Memória Fiscal foi desconectada" );

	public static StatusElginX5 ErroMFLeitura = new StatusElginX5( 1001, RELEVANC_ERRO, "Erro na leitura na Memória Fiscal" );

	public static StatusElginX5 ErroMFApenasLeitura = new StatusElginX5( 1002, RELEVANC_ERRO, "Memória está setada apenas para leitura" );

	public static StatusElginX5 ErroMFTamRegistro = new StatusElginX5( 1003, RELEVANC_ERRO, "Registro fora dos padrões (erro interno)" );

	public static StatusElginX5 ErroMFCheia = new StatusElginX5( 1004, RELEVANC_ERRO, "Memória Fiscal está lotada" );

	public static StatusElginX5 ErroMFCartuchosExcedidos = new StatusElginX5( 1005, RELEVANC_ERRO, "Número máximo de cartuchos excedidos" );

	public static StatusElginX5 ErroMFJaInicializada = new StatusElginX5( 1006, RELEVANC_ERRO, "Tentativa de gravar novo modelo de ECF" );

	public static StatusElginX5 ErroMFNaoInicializada = new StatusElginX5( 1007, RELEVANC_ERRO, "Tentativa de gravação de qualquer dado antes da inicialização da Memória Fiscal" );

	public static StatusElginX5 ErroMFUsuariosExcedidos = new StatusElginX5( 1008, RELEVANC_ERRO, "Número máximo de usuários foi atingido" );

	public static StatusElginX5 ErroMFIntervencoesExedidas = new StatusElginX5( 1009, RELEVANC_ERRO, "Número máxiom de intervenções foi atingido" );

	public static StatusElginX5 ErroMFVersoesExcedidas = new StatusElginX5( 1010, RELEVANC_ERRO, "Número máximo de versões foi atingido" );

	public static StatusElginX5 ErroMFReducoesExcedidas = new StatusElginX5( 1011, RELEVANC_ERRO, "Número máxiom de reduções foi atingido" );

	public static StatusElginX5 ErroMFGravacao = new StatusElginX5( 1012, RELEVANC_ERRO, "Erro na gravação de registro na memória fiscal" );

	public static StatusElginX5 ErroTransactDrvrLeitura = new StatusElginX5( 2000, RELEVANC_ERRO, "Erro na leitura no dispositivo físico" );

	public static StatusElginX5 ErroTransactDrvrEscrita = new StatusElginX5( 2001, RELEVANC_ERRO, "Erro de leitura no dispositivo" );

	public static StatusElginX5 ErroTransactDrvrDesconectado = new StatusElginX5( 2002, RELEVANC_ERRO, "Dispositivo de transações foi desconectado" );

	public static StatusElginX5 ErroTransactRegInvalido = new StatusElginX5( 3000, RELEVANC_ERRO, "Tipo de registro a ser gravado inválido" );

	public static StatusElginX5 ErroTransactCheio = new StatusElginX5( 3001, RELEVANC_ERRO, "registro de transações está esgotado" );

	public static StatusElginX5 ErroTransactTransAberta = new StatusElginX5( 3002, RELEVANC_ERRO, "Tentativa de abrir nova transação com trasação já aberta" );

	public static StatusElginX5 ErroTransactTransNaoAberta = new StatusElginX5( 3003, RELEVANC_ERRO, "Tentativa de fechar uma transação que não se encontra" );

	public static StatusElginX5 ErroContextDrvrLeitura = new StatusElginX5( 4000, RELEVANC_ERRO, "Erro de leitura de dispositivo físico" );

	public static StatusElginX5 ErroContextDrvrEscrita = new StatusElginX5( 4001, RELEVANC_ERRO, "Erro de escrita no dispositivo" );

	public static StatusElginX5 ErroContextDrvrDesconectado = new StatusElginX5( 4002, RELEVANC_ERRO, "Dispositivo de contexto foi desconectado" );

	public static StatusElginX5 ErroContextDrvrLeituraAposFim = new StatusElginX5( 4003, RELEVANC_ERRO, "Leitura após final do arquivo" );

	public static StatusElginX5 ErroContextDrvrEscritaAposFim = new StatusElginX5( 4004, RELEVANC_ERRO, "Escrita após final do arquivo" );

	public static StatusElginX5 ErroContextVersaoInvalida = new StatusElginX5( 5000, RELEVANC_ERRO, "versão de contexto fiscal no dispositivo não foi reconhecida" );

	public static StatusElginX5 ErroContextCRC = new StatusElginX5( 5001, RELEVANC_ERRO, "CRC do dispositivo está incorreto" );

	public static StatusElginX5 ErroContextLimitesExcedidos = new StatusElginX5( 5002, RELEVANC_ERRO, "Tentativa de escrita fora da área de contexto" );

	public static StatusElginX5 ErroRelogioInconsistente = new StatusElginX5( 6000, RELEVANC_ERRO, "Relório do ECF inconsistente" );

	public static StatusElginX5 ErroRelogioDataHoraInvalida = new StatusElginX5( 6001, RELEVANC_ERRO, "Data/hora informadas não estão consistentes" );

	public static StatusElginX5 ErroPrintSemMecanismo = new StatusElginX5( 7000, RELEVANC_ERRO, "Nenhum mecanismo de impressão presente" );

	public static StatusElginX5 ErroPrintDesconectado = new StatusElginX5( 7001, RELEVANC_ERRO, "Atual mecanismo de impressão está desconectado" );

	public static StatusElginX5 ErroPrintCapacidadeInesistente = new StatusElginX5( 7002, RELEVANC_ERRO, "Mecanismo não possui capacidade suficiente para realizar esta operação" );

	public static StatusElginX5 ErroPrintSemPapel = new StatusElginX5( 7003, RELEVANC_ERRO, "Impressora está sem papel para imprimir" );

	public static StatusElginX5 ErroPrintFaltouPapel = new StatusElginX5( 7004, RELEVANC_ERRO, "Faltou papel durante a impressão do comando" );

	public static StatusElginX5 ErroCMDForaDeSequencia = new StatusElginX5( 8000, RELEVANC_ERRO, "Comando fora de sequência" );

	public static StatusElginX5 ErroCMDCodigoInvalido = new StatusElginX5( 8001, RELEVANC_ERRO, "Código mercadoria não válido" );

	public static StatusElginX5 ErroCMDDescricaoInvalida = new StatusElginX5( 8002, RELEVANC_ERRO, "Descrição inválida" );

	public static StatusElginX5 ErroCMDQuantidadeInvalida = new StatusElginX5( 8003, RELEVANC_ERRO, "Quantidade não inválida" );

	public static StatusElginX5 ErroCMDAliquotaInvalida = new StatusElginX5( 8004, RELEVANC_ERRO, "Índice de alíquota não válido" );

	public static StatusElginX5 ErroCMDAliquotaNaoCarregada = new StatusElginX5( 8005, RELEVANC_ERRO, "Alíquota não caregada" );

	public static StatusElginX5 ErroCMDValorInvalido = new StatusElginX5( 8006, RELEVANC_ERRO, "Valor contém caracter inválido" );

	public static StatusElginX5 ErroCMDMontanteOperacao = new StatusElginX5( 8007, RELEVANC_ERRO, "Total da operação igual a 0(zero)" );

	public static StatusElginX5 ErroCMDAliquotaIndisponivel = new StatusElginX5( 8008, RELEVANC_ERRO, "Alíquota não disponível para carga" );

	public static StatusElginX5 ErroCMDValorAliquotaInvalido = new StatusElginX5( 8009, RELEVANC_ERRO, "Valor da alíquota não válido" );

	public static StatusElginX5 ErroCMDTrocaSTAposFechamento = new StatusElginX5( 8010, RELEVANC_ERRO, "Troca de situração tributária somente após Redução Z" );

	public static StatusElginX5 ErroCMDFormaPagamentoInvalida = new StatusElginX5( 8011, RELEVANC_ERRO, "Índice de Meio de Pagamento não válido" );

	public static StatusElginX5 ErroCMDPayIndisponivel = new StatusElginX5( 8012, RELEVANC_ERRO, "Meio de Pagamento indisponível para carga" );

	public static StatusElginX5 ErroCMDCupomTotalizadoEmZero = new StatusElginX5( 8013, RELEVANC_ERRO, "Cupom totalizado em 0(zero)" );

	public static StatusElginX5 ErroCMDFomraPagamentoIndefinida = new StatusElginX5( 8014, RELEVANC_ERRO, "Meio de Pagamento não definido" );

	public static StatusElginX5 ErroCMDtracaUsuarioAposFechamento = new StatusElginX5( 8015, RELEVANC_ERRO, "Carga de usuário permitido somente após Redução Z" );

	public static StatusElginX5 ErroCMDSemMovimento = new StatusElginX5( 8016, RELEVANC_ERRO, "Dia sem movimento" );

	public static StatusElginX5 ErroCMDPagamentoIncompleto = new StatusElginX5( 8017, RELEVANC_ERRO, "Total pago inferior ao total do cupom" );

	public static StatusElginX5 ErroCMDGerencialNaoDefinido = new StatusElginX5( 8018, RELEVANC_ERRO, "Gerencial não definido" );

	public static StatusElginX5 ErroGerencialInvalido = new StatusElginX5( 8019, RELEVANC_ERRO, "Índice de Gerencial fora da faixa" );

	public static StatusElginX5 ErroCMDGerencialIndisponivel = new StatusElginX5( 8020, RELEVANC_ERRO, "Gerencial não disponível para carga" );

	public static StatusElginX5 ErroCMDNomeGerencialInvalido = new StatusElginX5( 8021, RELEVANC_ERRO, "Nome do Gerencial inválido" );

	public static StatusElginX5 ErroCMDNaoHaMaisRelatoriosLivres = new StatusElginX5( 8022, RELEVANC_ERRO, "Esgotado número de Gerenciais" );

	public static StatusElginX5 ErroCMDAcertoHVPermitidoAposZ = new StatusElginX5( 8023, RELEVANC_ERRO, "Acerto do horário de verão somente após a Redução Z" );

	public static StatusElginX5 ErroCMDHorarioVeraoJaRealizado = new StatusElginX5( 8024, RELEVANC_ERRO, "Já acertou horário de verão" );

	public static StatusElginX5 ErroCMDAliquotasIndisponiveis = new StatusElginX5( 8025, RELEVANC_ERRO, "Sem Alíquotas disponíveis para carga" );

	public static StatusElginX5 ErroCMDItemInexistente = new StatusElginX5( 8026, RELEVANC_ERRO, "Item não vendido no cupom" );

	public static StatusElginX5 ErroCMDQtdCancInvalida = new StatusElginX5( 8027, RELEVANC_ERRO, "Quantidade a ser cancelada maior do que a quantidade vendida" );

	public static StatusElginX5 ErroCMDCampoCabecalhoInvalido = new StatusElginX5( 8028, RELEVANC_ERRO, "Cabeçalho possui campos(s) inválido(s)" );

	public static StatusElginX5 ErroCMDNomeDepartamentoInvalido = new StatusElginX5( 8029, RELEVANC_ERRO, "NomeDepartamento não válido" );

	public static StatusElginX5 ErroCMDDepartamentoNaoEncontrado = new StatusElginX5( 8030, RELEVANC_ERRO, "Departamento não encontrado" );

	public static StatusElginX5 ErroCMDDepartamentoIndefinido = new StatusElginX5( 8031, RELEVANC_ERRO, "Departamento não definido" );

	public static StatusElginX5 ErroCMDFormasPagamentosIndisponiveis = new StatusElginX5( 8032, RELEVANC_ERRO, "Não há Meio de Pagamento disponível" );

	public static StatusElginX5 ErroCMDAltPagamentosSoAposZ = new StatusElginX5( 8033, RELEVANC_ERRO, "Alteração de Meio de Pagamento somente após a Redução Z" );

	public static StatusElginX5 ErroCMDNomeNalFiscalInvalido = new StatusElginX5( 8034, RELEVANC_ERRO, "Nome do Documento Não Fiscal não pode ser vazio" );

	public static StatusElginX5 ErroCMDDocsFiscaisIndisponiveis = new StatusElginX5( 8035, RELEVANC_ERRO, "Não há mais Documentos Não Fiscais disponíveis" );

	public static StatusElginX5 ErroCMDnaoFislcaIndisponivel = new StatusElginX5( 8036, RELEVANC_ERRO, "Documento Não Fiscal indisponível para carga" );

	public static StatusElginX5 ErroCMDReducaoInvalida = new StatusElginX5( 8037, RELEVANC_ERRO, "Número de redução inicial inválida" );

	public static StatusElginX5 ErroCMDCabecalhoJaImpresso = new StatusElginX5( 8038, RELEVANC_ERRO, "Cabeçalho do documento á foi impresso" );

	public static StatusElginX5 ErroCMDLinhasSuplementaresExcedidas = new StatusElginX5( 8039, RELEVANC_ERRO, "Número máximo de linhas de propaganda excedidas" );

	public static StatusElginX5 ErroHorarioVeraoAtualizado = new StatusElginX5( 8040, RELEVANC_ERRO, "Relógio já está no estado desejado" );

	public static StatusElginX5 ErroCMDValorAcrescimoInvalido = new StatusElginX5( 8041, RELEVANC_ERRO, "Valor do acréscimo inconsistente" );

	public static StatusElginX5 ErroCMDNaohaMeiodePagamento = new StatusElginX5( 8042, RELEVANC_ERRO, "Não há meio de pagamento definido" );

	public static StatusElginX5 ErroCMDCOOVinculadoInvalido = new StatusElginX5( 8043, RELEVANC_ERRO, "COO do documento vinculado inválido" );

	public static StatusElginX5 ErroCMDIndiceItemInvalido = new StatusElginX5( 8044, RELEVANC_ERRO, "Índice do item inexistente no contexto" );

	public static StatusElginX5 ErroCMDCodigoNaoEncontrado = new StatusElginX5( 8045, RELEVANC_ERRO, "Código de item não encontrado no cupom atual" );

	public static StatusElginX5 ErroCMDPercentualDescontoInvalido = new StatusElginX5( 8046, RELEVANC_ERRO, "Percentual do desconto ultrapassou 100%" );

	public static StatusElginX5 ErroCMDDescontoItemInvalido = new StatusElginX5( 8047, RELEVANC_ERRO, "Desconto do item inválido" );

	public static StatusElginX5 ErroCMDFaltaDefinirValor = new StatusElginX5( 8048, RELEVANC_ERRO, "Falta definir valor percentual ou absoluto em operação de desconto/acréscimo" );

	public static StatusElginX5 ErroCMDItemCancelado = new StatusElginX5( 8049, RELEVANC_ERRO, "Tentativa de operação sobre item cancelado" );

	public static StatusElginX5 ErroCMDCancelaAcrDescInvalido = new StatusElginX5( 8050, RELEVANC_ERRO, "Cancelamento de acréscimo/desconto inválidos" );

	public static StatusElginX5 ErroCMDAcrDescInvalido = new StatusElginX5( 8051, RELEVANC_ERRO, "Operação de acréscimo/desconto inválida" );

	public static StatusElginX5 ErroCMDNaoHaMaisDepartamentosLivres = new StatusElginX5( 8052, RELEVANC_ERRO, "Número de Departamentos esgotados" );

	public static StatusElginX5 ErroCMDIndiceNaoFiscalInvalido = new StatusElginX5( 8053, RELEVANC_ERRO, "Índice de Documento Não Fiscal fora da faixa" );

	public static StatusElginX5 ErroCMDTrocaNaoFiscalAposZ = new StatusElginX5( 8054, RELEVANC_ERRO, "Troca de Documento Não Fiscal somente após a Redução Z" );

	public static StatusElginX5 ErroCMDInscricaoInvalida = new StatusElginX5( 8055, RELEVANC_ERRO, "CNPJ e/ou Inscrição Estadual inválida(s)" );

	public static StatusElginX5 ErroCMDVinculadoParametrosInsuficientes = new StatusElginX5( 8056, RELEVANC_ERRO, "Falta(m) parâmentro(s) no comando de abertura de Comprovante Crédito ou Débito" );

	public static StatusElginX5 ErroCMDNaoFiscalIndefinido = new StatusElginX5( 8057, RELEVANC_ERRO, "Código e Nome do Documento Não Fiscal indefinidos" );

	public static StatusElginX5 ErroCMDFaltaAliquotaVenda = new StatusElginX5( 8058, RELEVANC_ERRO, "Alíquota não definida no comando de venda" );

	public static StatusElginX5 ErroCMDFaltaMeioPagamento = new StatusElginX5( 8059, RELEVANC_ERRO, "Código e Nome do Meio de Pagamento não definidos" );

	public static StatusElginX5 ErroCMDFaltaParametro = new StatusElginX5( 8060, RELEVANC_ERRO, "Parâmetro de comando não informado" );

	public static StatusElginX5 ErroCMDNaoHaDocNaoFiscaisDefinidos = new StatusElginX5( 8061, RELEVANC_ERRO, "Não há Documentos Não Fiscais definidos" );

	public static StatusElginX5 ErroCMDOperacaoJaCancelada = new StatusElginX5( 8062, RELEVANC_ERRO, "Acréscimo/Desconto de item já cancelado" );

	public static StatusElginX5 ErroCMDNaohaAcresDescItem = new StatusElginX5( 8063, RELEVANC_ERRO, "Não há acréscimo/desconto em item" );

	public static StatusElginX5 ErroCMDItemAcrescido = new StatusElginX5( 8064, RELEVANC_ERRO, "Item já possui acréscimo" );

	public static StatusElginX5 ErroCMDOperSoEmICMS = new StatusElginX5( 8065, RELEVANC_ERRO, "Operação de acréscimo em item ou subtotal só é válido para ICMS" );

	public static StatusElginX5 ErroCMDFaltaInformaValor = new StatusElginX5( 8066, RELEVANC_ERRO, "Valor do Comprovante Crédito ou Débito não informado" );

	public static StatusElginX5 ErroCMDCOOInvalido = new StatusElginX5( 8067, RELEVANC_ERRO, "COO inválido" );

	public static StatusElginX5 ErroCMDIndiceInvalido = new StatusElginX5( 8068, RELEVANC_ERRO, "Índice do Meio de Pagamento no cupom inválido" );

	public static StatusElginX5 ErroCMDCupomNaoEncontrado = new StatusElginX5( 8069, RELEVANC_ERRO, "Documento Não Fiscal não encontrado" );

	public static StatusElginX5 ErroCMDSequenciaPagamentoNaoEncontrada = new StatusElginX5( 8070, RELEVANC_ERRO, "Sequência de pagamento não encontrada no cupom" );

	public static StatusElginX5 ErroCMDPagamentoNaoPermiteCDC = new StatusElginX5( 8071, RELEVANC_ERRO, "Meio de Pagamento não permite CDC" );

	public static StatusElginX5 ErroCMDUltimaFormaPagamentoInv = new StatusElginX5( 8072, RELEVANC_ERRO, "Valor insuficiente para pagar o cupom" );

	public static StatusElginX5 ErroCMDMeioPagamentoNEncontrado = new StatusElginX5( 8073, RELEVANC_ERRO, "Meio de Pagamento origem ou destino não encontrado no último cupom emitido" );

	public static StatusElginX5 ErroCMDValorEstornoInvalido = new StatusElginX5( 8074, RELEVANC_ERRO, "Valor do estorno não pode exceder o valor do pagamento no meio origem" );

	public static StatusElginX5 ErroCMDMeiosPagamentoOrigemDestinoIguais = new StatusElginX5( 8075, RELEVANC_ERRO, "Meios de Pagamento origem e destino devem ser diferentes no estorno" );

	public static StatusElginX5 ErroCMDPercentualInvalido = new StatusElginX5( 8076, RELEVANC_ERRO, "Percentual da alíquota inválido" );

	public static StatusElginX5 ErroCMDNaoHouveOpSubtotal = new StatusElginX5( 8077, RELEVANC_ERRO, "Não houve operação em subtotal para ser cancelada" );

	public static StatusElginX5 ErroCMDOpSubtotalInvalida = new StatusElginX5( 8078, RELEVANC_ERRO, "Só é permitida uma operação de acréscimo em suvtotal por cupom" );

	public static StatusElginX5 ErroCMDTextoAdicional = new StatusElginX5( 8079, RELEVANC_ERRO, "Texto adicional do meio de pagamento dever ter no máximo 2 linhas" );

	public static StatusElginX5 ErroCMDPrecoUnitarioInvalido = new StatusElginX5( 8080, RELEVANC_ERRO, "Preço unitário ultrapassou o número máximo de dígitos permitido" );

	public static StatusElginX5 ErroCMDDepartamentoInvalido = new StatusElginX5( 8081, RELEVANC_ERRO, "Código do departamento fora da faixa" );

	public static StatusElginX5 ErroCDMDescontoInvalido = new StatusElginX5( 8082, RELEVANC_ERRO, "O valor do desconto não pode zerar o valor do cupom ou ser maior que o item" );

	public static StatusElginX5 ErroCMDPercentualAcrescimoInvalido = new StatusElginX5( 8083, RELEVANC_ERRO, "Percentual de acréscimo não pode ser superior a 999,99%" );

	public static StatusElginX5 ErroCMDAcrescimoInvalido = new StatusElginX5( 8084, RELEVANC_ERRO, "Valor do acréscimo ultrapassa o número máximo de digitos permitido (13 dígitos)" );

	public static StatusElginX5 ErroCMDNaoHouveVendaEmICMS = new StatusElginX5( 8085, RELEVANC_ERRO, "Cupom sem venda em alíquota de ICMS" );

	public static StatusElginX5 ErroCMDCancelamentoInvalido = new StatusElginX5( 8086, RELEVANC_ERRO, "Cancelamento inválido" );

	public static StatusElginX5 ErroCMDCliche = new StatusElginX5( 8087, RELEVANC_ERRO, "Texto de cliche do usuário dever ter no máximo três linhas" );

	public static StatusElginX5 ErroCMDNaoHouveVendaNaoFiscal = new StatusElginX5( 8088, RELEVANC_ERRO, "Não houve venda de item não fiscal" );

	public static StatusElginX5 ErroCMDDataInvalida = new StatusElginX5( 8089, RELEVANC_ERRO, "A data não pode ser inferior a data do último documento emitido" );

	public static StatusElginX5 ErroCMDHoraInvalida = new StatusElginX5( 8090, RELEVANC_ERRO, "A hora informada no comando não pode ser inferior ao horário do último documento" );

	public static StatusElginX5 ErroCMDEstorno = new StatusElginX5( 8091, RELEVANC_ERRO, "Sem função" );

	public static StatusElginX5 ErroCMDAcertoRelogio = new StatusElginX5( 8092, RELEVANC_ERRO, "Estado inválido para ajuste de relogio ou horário de verão" );

	public static StatusElginX5 ErroCMDCDCInvalido = new StatusElginX5( 8093, RELEVANC_ERRO, "A operação de CDC deve preceder as operações de estorno de meio de pagamento" );

	public static StatusElginX5 ErroSenhaInvalida = new StatusElginX5( 8094, RELEVANC_ERRO, "Senha inválida para inicialização do proprietário" );

	public static StatusElginX5 ErroCMDMecanismoCheque = new StatusElginX5( 8095, RELEVANC_ERRO, "Erro gerado pelo mecanismo de cheques" );

	public static StatusElginX5 ErroFaltaIniciarDia = new StatusElginX5( 8096, RELEVANC_ERRO, "Comando válido somente após a abertura do dia" );

	public static StatusElginX5 ErroMFDNenhumCartuchoVazio = new StatusElginX5( 9000, RELEVANC_ERRO, "Não foi encontrado nenhum cartucho de dados vazion para ser inicializado" );

	public static StatusElginX5 ErroMFDCartuchoInexistente = new StatusElginX5( 9001, RELEVANC_ERRO, "Cartucho com o número de série informado não foi encontrado" );

	public static StatusElginX5 ErroMFDNumSerie = new StatusElginX5( 9002, RELEVANC_ERRO, "Número de série do ECF é inválido na inicialização" );

	public static StatusElginX5 ErroMFDCartuchoDesconhecido = new StatusElginX5( 9003, RELEVANC_ERRO, "Cartucho de MFD desconctado ou com problemas" );

	public static StatusElginX5 ErroMFDEscrita = new StatusElginX5( 9004, RELEVANC_ERRO, "Erro de escrita no dispositivo de MDF" );

	public static StatusElginX5 ErroMFDSeek = new StatusElginX5( 9005, RELEVANC_ERRO, "Erro na tentativa de posicionar ponteiro de leitura" );

	public static StatusElginX5 ErroMFDBadBadSector = new StatusElginX5( 9006, RELEVANC_ERRO, "Endereçõ do bad Sector informado é inválido" );

	public static StatusElginX5 ErroMFDLeitura = new StatusElginX5( 9007, RELEVANC_ERRO, "Erro de leitura na MFD" );

	public static StatusElginX5 ErroMFDLeituraAlemEOF = new StatusElginX5( 9008, RELEVANC_ERRO, "Tentativa de leitura além dos limites da MFD" );

	public static StatusElginX5 ErroMFDEsgotada = new StatusElginX5( 9009, RELEVANC_ERRO, "MFD não possui mais espaço para escrita" );

	public static StatusElginX5 ErroMFDLeituraInterrompida = new StatusElginX5( 9010, RELEVANC_ERRO, "Leitura da MFD serial é interrompida por comando diferente de LeImpressao" );

	public static StatusElginX5 ErroBNFEstadoInvalido = new StatusElginX5( 10000, RELEVANC_ERRO, "Estado inválido para registro sendo codificado" );

	public static StatusElginX5 ErroBNDParametroInvalido = new StatusElginX5( 10001, RELEVANC_ERRO, "Inconsistência nos parâmentros lidos no Logger" );

	public static StatusElginX5 ErroBNFRegistroInvalido = new StatusElginX5( 10002, RELEVANC_ERRO, "Registro inválido detectado no Logger" );

	public static StatusElginX5 ErroBNFErroMFD = new StatusElginX5( 10003, RELEVANC_ERRO, "Erro interno" );

	public static StatusElginX5 ErroProtParamInvalido = new StatusElginX5( 11000, RELEVANC_ERRO, "Parâmetro repassado ao comando é inválido" );

	public static StatusElginX5 ErroProtParamSintaxe = new StatusElginX5( 11001, RELEVANC_ERRO, "Erro de sintaxe na lista de parâmetros" );

	public static StatusElginX5 ErroProtParamValorInvalido = new StatusElginX5( 11002, RELEVANC_ERRO, "Valor inválido para parâmetro do comando" );

	public static StatusElginX5 ErroProtParamStringInvalido = new StatusElginX5( 11003, RELEVANC_ERRO, "String contém sequência de caracteres inválidos" );

	public static StatusElginX5 ErroProtParamRedefinido = new StatusElginX5( 11004, RELEVANC_ERRO, "Parâmetro foi declarado 2 ou mais vezes na lista" );

	public static StatusElginX5 ErroProtParamIndefinido = new StatusElginX5( 11005, RELEVANC_ERRO, "Parâmetro obrigatório ausente na lista" );

	public static StatusElginX5 ErroProtComandoIncexistente = new StatusElginX5( 11006, RELEVANC_ERRO, "Não existe o comando no protocolo" );

	public static StatusElginX5 ErroProtSequenciaComando = new StatusElginX5( 11007, RELEVANC_ERRO, "Estado atual não permite a execução deste comando" );

	public static StatusElginX5 ErroProtAborta2aVia = new StatusElginX5( 11008, RELEVANC_ERRO, "Sinalização indicando que comando aborta a impressão da segunda via" );

	public static StatusElginX5 ErroProtSemRetorno = new StatusElginX5( 11009, RELEVANC_ERRO, "Sinalização indicando que comando não possui retorno" );

	public static StatusElginX5 ErroProtTimeout = new StatusElginX5( 11010, RELEVANC_ERRO, "Tempo de execução esgotado" );

	public static StatusElginX5 ErroProtNomeRegistrador = new StatusElginX5( 11011, RELEVANC_ERRO, "Nome de registrador inválido" );

	public static StatusElginX5 ErroProttipoRegistrador = new StatusElginX5( 11012, RELEVANC_ERRO, "Tipo de registrador inválido" );

	public static StatusElginX5 ErroProtSomenteLeitura = new StatusElginX5( 11013, RELEVANC_ERRO, "Tentativa de escrita em registrador de apenas leitura" );

	public static StatusElginX5 ErroProtSomenteEscrita = new StatusElginX5( 11014, RELEVANC_ERRO, "Tentativa de leitura em registrador de apenas escrita" );

	public static StatusElginX5 ErroProtComandoDiferenteAnterior = new StatusElginX5( 11015, RELEVANC_ERRO, "Comando recebido diferente do anterior no buffer de recepção" );

	public static StatusElginX5 ErroProtFilaCheia = new StatusElginX5( 11016, RELEVANC_ERRO, "Fila de comando cheia" );

	public static StatusElginX5 ErroProtIndiceRegistrador = new StatusElginX5( 11017, RELEVANC_ERRO, "Índice de registrador indexado fora dos limites" );

	public static StatusElginX5 ErroProtNumEmissoesExcedido = new StatusElginX5( 11018, RELEVANC_ERRO, "Número de emissões do Logger foi excedido na Intervenção Técnica" );

	public static StatusElginX5 ErroMathDivisaoPorZero = new StatusElginX5( 11019, RELEVANC_ERRO, "Divisão por 0(zero) nas rotinas de BDC" );

	public static StatusElginX5 ErroApenasIntTecnica = new StatusElginX5( 15001, RELEVANC_ERRO, "Comando aceito apenas em modo de Intervenção Técnica" );

	public static StatusElginX5 ErroECFIntTecnica = new StatusElginX5( 15002, RELEVANC_ERRO, "Comando não pode ser executado em modo de Intervenção Técnica" );

	public static StatusElginX5 ErroMFDPresente = new StatusElginX5( 15003, RELEVANC_ERRO, "Já existe MFD presente neste ECF" );

	public static StatusElginX5 ErroSemMFD = new StatusElginX5( 15004, RELEVANC_ERRO, "Não existe MFD neste ECF" );

	public static StatusElginX5 ErroRAMInconsistente = new StatusElginX5( 15005, RELEVANC_ERRO, "Memória RAM do ECF não esta consistente" );

	public static StatusElginX5 ErroMemoriaFiscalDesconectada = new StatusElginX5( 15006, RELEVANC_ERRO, "Memória fiscal não encontrada" );

	public static StatusElginX5 ErroDiaFechado = new StatusElginX5( 15007, RELEVANC_ERRO, "Dia já fechado" );

	public static StatusElginX5 ErroDiaAberto = new StatusElginX5( 15008, RELEVANC_ERRO, "Dia aberto" );

	public static StatusElginX5 ErroZPendente = new StatusElginX5( 15009, RELEVANC_ERRO, "Falta Redução Z" );

	public static StatusElginX5 ErroMecanismoNaoConfigurado = new StatusElginX5( 15010, RELEVANC_ERRO, "Mecanismo impressor não selecionado" );

	public static StatusElginX5 ErroSemPapel = new StatusElginX5( 15011, RELEVANC_ERRO, "Sem bobina de papel na estação de documento fiscal" );

	public static StatusElginX5 ErroDocumentoEncerrado = new StatusElginX5( 15012, RELEVANC_ERRO, "Tentativa de finalizar documento já encerrado" );

	public static StatusElginX5 ErroSemSinalDTR = new StatusElginX5( 15013, RELEVANC_ERRO, "Não há sinal de DTR" );

	public static StatusElginX5 ErroSemInscricoes = new StatusElginX5( 15014, RELEVANC_ERRO, "Sem inscrições do usuário no ECF" );

	public static StatusElginX5 ErroSemCliche = new StatusElginX5( 15015, RELEVANC_ERRO, "Sem dados do proprietário no ECF" );

	public static StatusElginX5 ErroEmLinha = new StatusElginX5( 15016, RELEVANC_ERRO, "ECF encontra-se indevidamente em linha" );

	public static StatusElginX5 ErroForaDeLinha = new StatusElginX5( 15017, RELEVANC_ERRO, "ECF não encontra-se em linha para executar o comando" );

	public static StatusElginX5 ErroMecanismoBloqueado = new StatusElginX5( 15018, RELEVANC_ERRO, "Mecanismo está indisponível para impressão" );

	private String message;

	private String messageComplementar;

	private int code;

	private int relevanc;

	private static final List<StatusElginX5> statusList = new ArrayList<StatusElginX5>();
	static {
		statusList.add( ErroGeralFalRAM );
		statusList.add( ErroGeralPerdaRAM );
		statusList.add( ErroMFDesconectada );
		statusList.add( ErroMFLeitura );
		statusList.add( ErroMFApenasLeitura );
		statusList.add( ErroMFTamRegistro );
		statusList.add( ErroMFCheia );
		statusList.add( ErroMFCartuchosExcedidos );
		statusList.add( ErroMFJaInicializada );
		statusList.add( ErroMFNaoInicializada );
		statusList.add( ErroMFUsuariosExcedidos );
		statusList.add( ErroMFIntervencoesExedidas );
		statusList.add( ErroMFVersoesExcedidas );
		statusList.add( ErroMFReducoesExcedidas );
		statusList.add( ErroMFGravacao );
		statusList.add( ErroTransactDrvrLeitura );
		statusList.add( ErroTransactDrvrEscrita );
		statusList.add( ErroTransactDrvrDesconectado );
		statusList.add( ErroTransactRegInvalido );
		statusList.add( ErroTransactCheio );
		statusList.add( ErroTransactTransAberta );
		statusList.add( ErroTransactTransNaoAberta );
		statusList.add( ErroContextDrvrLeitura );
		statusList.add( ErroContextDrvrEscrita );
		statusList.add( ErroContextDrvrDesconectado );
		statusList.add( ErroContextDrvrLeituraAposFim );
		statusList.add( ErroContextDrvrEscritaAposFim );
		statusList.add( ErroContextVersaoInvalida );
		statusList.add( ErroContextCRC );
		statusList.add( ErroContextLimitesExcedidos );
		statusList.add( ErroRelogioInconsistente );
		statusList.add( ErroRelogioDataHoraInvalida );
		statusList.add( ErroPrintSemMecanismo );
		statusList.add( ErroPrintDesconectado );
		statusList.add( ErroPrintCapacidadeInesistente );
		statusList.add( ErroPrintSemPapel );
		statusList.add( ErroPrintFaltouPapel );
		statusList.add( ErroCMDForaDeSequencia );
		statusList.add( ErroCMDCodigoInvalido );
		statusList.add( ErroCMDDescricaoInvalida );
		statusList.add( ErroCMDQuantidadeInvalida );
		statusList.add( ErroCMDAliquotaInvalida );
		statusList.add( ErroCMDAliquotaNaoCarregada );
		statusList.add( ErroCMDValorInvalido );
		statusList.add( ErroCMDMontanteOperacao );
		statusList.add( ErroCMDAliquotaIndisponivel );
		statusList.add( ErroCMDValorAliquotaInvalido );
		statusList.add( ErroCMDTrocaSTAposFechamento );
		statusList.add( ErroCMDFormaPagamentoInvalida );
		statusList.add( ErroCMDPayIndisponivel );
		statusList.add( ErroCMDCupomTotalizadoEmZero );
		statusList.add( ErroCMDFomraPagamentoIndefinida );
		statusList.add( ErroCMDtracaUsuarioAposFechamento );
		statusList.add( ErroCMDSemMovimento );
		statusList.add( ErroCMDPagamentoIncompleto );
		statusList.add( ErroCMDGerencialNaoDefinido );
		statusList.add( ErroGerencialInvalido );
		statusList.add( ErroCMDGerencialIndisponivel );
		statusList.add( ErroCMDNomeGerencialInvalido );
		statusList.add( ErroCMDNaoHaMaisRelatoriosLivres );
		statusList.add( ErroCMDAcertoHVPermitidoAposZ );
		statusList.add( ErroCMDHorarioVeraoJaRealizado );
		statusList.add( ErroCMDAliquotasIndisponiveis );
		statusList.add( ErroCMDItemInexistente );
		statusList.add( ErroCMDQtdCancInvalida );
		statusList.add( ErroCMDCampoCabecalhoInvalido );
		statusList.add( ErroCMDNomeDepartamentoInvalido );
		statusList.add( ErroCMDDepartamentoNaoEncontrado );
		statusList.add( ErroCMDDepartamentoIndefinido );
		statusList.add( ErroCMDFormasPagamentosIndisponiveis );
		statusList.add( ErroCMDAltPagamentosSoAposZ );
		statusList.add( ErroCMDNomeNalFiscalInvalido );
		statusList.add( ErroCMDDocsFiscaisIndisponiveis );
		statusList.add( ErroCMDnaoFislcaIndisponivel );
		statusList.add( ErroCMDReducaoInvalida );
		statusList.add( ErroCMDCabecalhoJaImpresso );
		statusList.add( ErroCMDLinhasSuplementaresExcedidas );
		statusList.add( ErroHorarioVeraoAtualizado );
		statusList.add( ErroCMDValorAcrescimoInvalido );
		statusList.add( ErroCMDNaohaMeiodePagamento );
		statusList.add( ErroCMDCOOVinculadoInvalido );
		statusList.add( ErroCMDIndiceItemInvalido );
		statusList.add( ErroCMDCodigoNaoEncontrado );
		statusList.add( ErroCMDPercentualDescontoInvalido );
		statusList.add( ErroCMDDescontoItemInvalido );
		statusList.add( ErroCMDFaltaDefinirValor );
		statusList.add( ErroCMDItemCancelado );
		statusList.add( ErroCMDCancelaAcrDescInvalido );
		statusList.add( ErroCMDAcrDescInvalido );
		statusList.add( ErroCMDNaoHaMaisDepartamentosLivres );
		statusList.add( ErroCMDIndiceNaoFiscalInvalido );
		statusList.add( ErroCMDTrocaNaoFiscalAposZ );
		statusList.add( ErroCMDInscricaoInvalida );
		statusList.add( ErroCMDVinculadoParametrosInsuficientes );
		statusList.add( ErroCMDNaoFiscalIndefinido );
		statusList.add( ErroCMDFaltaAliquotaVenda );
		statusList.add( ErroCMDFaltaMeioPagamento );
		statusList.add( ErroCMDFaltaParametro );
		statusList.add( ErroCMDNaoHaDocNaoFiscaisDefinidos );
		statusList.add( ErroCMDOperacaoJaCancelada );
		statusList.add( ErroCMDNaohaAcresDescItem );
		statusList.add( ErroCMDItemAcrescido );
		statusList.add( ErroCMDOperSoEmICMS );
		statusList.add( ErroCMDFaltaInformaValor );
		statusList.add( ErroCMDCOOInvalido );
		statusList.add( ErroCMDIndiceInvalido );
		statusList.add( ErroCMDCupomNaoEncontrado );
		statusList.add( ErroCMDSequenciaPagamentoNaoEncontrada );
		statusList.add( ErroCMDPagamentoNaoPermiteCDC );
		statusList.add( ErroCMDUltimaFormaPagamentoInv );
		statusList.add( ErroCMDMeioPagamentoNEncontrado );
		statusList.add( ErroCMDValorEstornoInvalido );
		statusList.add( ErroCMDMeiosPagamentoOrigemDestinoIguais );
		statusList.add( ErroCMDPercentualInvalido );
		statusList.add( ErroCMDNaoHouveOpSubtotal );
		statusList.add( ErroCMDOpSubtotalInvalida );
		statusList.add( ErroCMDTextoAdicional );
		statusList.add( ErroCMDPrecoUnitarioInvalido );
		statusList.add( ErroCMDDepartamentoInvalido );
		statusList.add( ErroCDMDescontoInvalido );
		statusList.add( ErroCMDPercentualAcrescimoInvalido );
		statusList.add( ErroCMDAcrescimoInvalido );
		statusList.add( ErroCMDNaoHouveVendaEmICMS );
		statusList.add( ErroCMDCancelamentoInvalido );
		statusList.add( ErroCMDCliche );
		statusList.add( ErroCMDNaoHouveVendaNaoFiscal );
		statusList.add( ErroCMDDataInvalida );
		statusList.add( ErroCMDHoraInvalida );
		statusList.add( ErroCMDEstorno );
		statusList.add( ErroCMDAcertoRelogio );
		statusList.add( ErroCMDCDCInvalido );
		statusList.add( ErroSenhaInvalida );
		statusList.add( ErroCMDMecanismoCheque );
		statusList.add( ErroFaltaIniciarDia );
		statusList.add( ErroMFDNenhumCartuchoVazio );
		statusList.add( ErroMFDCartuchoInexistente );
		statusList.add( ErroMFDNumSerie );
		statusList.add( ErroMFDCartuchoDesconhecido );
		statusList.add( ErroMFDEscrita );
		statusList.add( ErroMFDSeek );
		statusList.add( ErroMFDBadBadSector );
		statusList.add( ErroMFDLeitura );
		statusList.add( ErroMFDLeituraAlemEOF );
		statusList.add( ErroMFDEsgotada );
		statusList.add( ErroMFDLeituraInterrompida );
		statusList.add( ErroBNFEstadoInvalido );
		statusList.add( ErroBNDParametroInvalido );
		statusList.add( ErroBNFRegistroInvalido );
		statusList.add( ErroBNFErroMFD );
		statusList.add( ErroProtParamInvalido );
		statusList.add( ErroProtParamSintaxe );
		statusList.add( ErroProtParamValorInvalido );
		statusList.add( ErroProtParamStringInvalido );
		statusList.add( ErroProtParamRedefinido );
		statusList.add( ErroProtParamIndefinido );
		statusList.add( ErroProtComandoIncexistente );
		statusList.add( ErroProtSequenciaComando );
		statusList.add( ErroProtAborta2aVia );
		statusList.add( ErroProtSemRetorno );
		statusList.add( ErroProtTimeout );
		statusList.add( ErroProtNomeRegistrador );
		statusList.add( ErroProttipoRegistrador );
		statusList.add( ErroProtSomenteLeitura );
		statusList.add( ErroProtSomenteEscrita );
		statusList.add( ErroProtComandoDiferenteAnterior );
		statusList.add( ErroProtFilaCheia );
		statusList.add( ErroProtIndiceRegistrador );
		statusList.add( ErroProtNumEmissoesExcedido );
		statusList.add( ErroMathDivisaoPorZero );
		statusList.add( ErroApenasIntTecnica );
		statusList.add( ErroECFIntTecnica );
		statusList.add( ErroMFDPresente );
		statusList.add( ErroSemMFD );
		statusList.add( ErroRAMInconsistente );
		statusList.add( ErroMemoriaFiscalDesconectada );
		statusList.add( ErroDiaFechado );
		statusList.add( ErroDiaAberto );
		statusList.add( ErroZPendente );
		statusList.add( ErroMecanismoNaoConfigurado );
		statusList.add( ErroSemPapel );
		statusList.add( ErroDocumentoEncerrado );
		statusList.add( ErroSemSinalDTR );
		statusList.add( ErroSemInscricoes );
		statusList.add( ErroSemCliche );
		statusList.add( ErroEmLinha );
		statusList.add( ErroForaDeLinha );
		statusList.add( ErroMecanismoBloqueado );
	}

	public static StatusElginX5 getStatusElginX5( int code, String messageComplementar ) {

		StatusElginX5 status = null;
		for ( StatusElginX5 sd : statusList ) {
			if ( sd.getCode() == code ) {
				status = sd;
				status.setMessageComplementar( messageComplementar );
				break;
			}
		}
		return status;
	}

	public StatusElginX5( int code, int relevanc, String message ) {
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

		return message + ( getMessageComplementar() == null ? "" : ( ": " + getMessageComplementar() ) );
	}

	public String getMessageComplementar() {

		return messageComplementar;
	}

	public void setMessageComplementar( String messageComplementar ) {

		this.messageComplementar = messageComplementar;
	}

	public void setRelevanc( int relevanc ) {

		this.relevanc = relevanc;
	}

	public int getRelevanc() {

		return relevanc;
	}
}
