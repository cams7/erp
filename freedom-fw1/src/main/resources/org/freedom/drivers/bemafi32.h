extern __stdcall Bematech_FI_AlteraSimboloMoeda( char * SimboloMoeda );
extern __stdcall Bematech_FI_ProgramaAliquota( char * Aliquota, int ICMS_ISS );
extern __stdcall Bematech_FI_ProgramaHorarioVerao();
extern __stdcall Bematech_FI_NomeiaDepartamento( int Indice, char * Departamento );
extern __stdcall Bematech_FI_NomeiaTotalizadorNaoSujeitoIcms( int Indice, char * Totalizador );
extern __stdcall Bematech_FI_ProgramaArredondamento();
extern __stdcall Bematech_FI_ProgramaTruncamento();
extern __stdcall Bematech_FI_LinhasEntreCupons( int Linhas );
extern __stdcall Bematech_FI_EspacoEntreLinhas( int Dots );
extern __stdcall Bematech_FI_ForcaImpactoAgulhas( int ForcaImpacto );

// Funções do Cupom Fiscal

extern __stdcall Bematech_FI_AbreCupom( char * CGC_CPF );
extern __stdcall Bematech_FI_VendeItem
     ( char * Codigo, char * Descricao, char * Aliquota, char * TipoQuantidade, char * Quantidade, short CasasDecimais,
     char * ValorUnitario, char * TipoDesconto, char * ValorDesconto );
//   extern __stdcall Bematech_FI_VendeItem( char* Codigo, char* Descricao, char* Aliquota, char* TipoQuantidade, char*
// Quantidade, short CasasDecimais, char* ValorUnitario, char* TipoDesconto, char* Desconto);
extern __stdcall Bematech_FI_VendeItemDepartamento( char * Codigo, char * Descricao, char * Aliquota, char * ValorUnitario,
     char * Quantidade, char * Acrescimo, char * Desconto, char * IndiceDepartamento, char * UnidadeMedida );
extern __stdcall Bematech_FI_CancelaItemAnterior();
extern __stdcall Bematech_FI_CancelaItemGenerico( char * NumeroItem );
extern __stdcall Bematech_FI_CancelaCupom();
extern __stdcall Bematech_FI_FechaCupomResumido( char * FormaPagamento, char * Mensagem );
extern __stdcall Bematech_FI_FechaCupom( char * FormaPagamento, char * AcrescimoDesconto, char * TipoAcrescimoDesconto,
     char * ValorAcrescimoDesconto, char * ValorPago, char * Mensagem );
extern __stdcall Bematech_FI_ResetaImpressora();
extern __stdcall Bematech_FI_IniciaFechamentoCupom( char * AcrescimoDesconto, char * TipoAcrescimoDesconto,
     char * ValorAcrescimoDesconto );
extern __stdcall Bematech_FI_EfetuaFormaPagamento( char * FormaPagamento, char * ValorFormaPagamento );
extern __stdcall Bematech_FI_EfetuaFormaPagamentoDescricaoForma( char * FormaPagamento, char * ValorFormaPagamento,
     char * DescricaoFormaPagto );
extern __stdcall Bematech_FI_TerminaFechamentoCupom( char * Mensagem );
extern __stdcall Bematech_FI_EstornoFormasPagamento( char * FormaOrigem, char * FormaDestino, char * Valor );
extern __stdcall Bematech_FI_UsaUnidadeMedida( char * UnidadeMedida );
extern __stdcall Bematech_FI_AumentaDescricaoItem( char * Descricao );

// Funções dos Relatórios Fiscais

extern __stdcall Bematech_FI_LeituraX();
extern __stdcall Bematech_FI_ReducaoZ( char * Data, char * Hora );
extern __stdcall Bematech_FI_LeituraMemoriaFiscalData( char * DataInicial, char * DataFinal );
extern __stdcall Bematech_FI_LeituraMemoriaFiscalReducao( char * ReducaoInicial, char * ReducaoFinal );
extern __stdcall Bematech_FI_LeituraMemoriaFiscalSerialData( char * DataInicial, char * DataFinal );
extern __stdcall Bematech_FI_LeituraMemoriaFiscalSerialReducao( char * ReducaoInicial, char * ReducaoFinal );

// Funções das Operações Não Fiscais

extern __stdcall Bematech_FI_RecebimentoNaoFiscal( char * IndiceTotalizador, char * Valor, char * FormaPagamento );
extern __stdcall Bematech_FI_AbreComprovanteNaoFiscalVinculado( char * FormaPagamento, char * Valor, char * NumeroCupom );
extern __stdcall Bematech_FI_UsaComprovanteNaoFiscalVinculadoTEF( char * Texto );
extern __stdcall Bematech_FI_UsaComprovanteNaoFiscalVinculado( char * Texto );
extern __stdcall Bematech_FI_FechaComprovanteNaoFiscalVinculado();
extern __stdcall Bematech_FI_Sangria( char * Valor );
extern __stdcall Bematech_FI_Suprimento( char * Valor, char * FormaPagamento );
extern __stdcall Bematech_FI_RelatorioGerencial( char * Texto );
extern __stdcall Bematech_FI_RelatorioGerencialTEF( char * Texto );
extern __stdcall Bematech_FI_FechaRelatorioGerencial();


// Funções de Informações da Impressora

extern __stdcall Bematech_FI_NumeroSerie( char * NumeroSerie );
extern __stdcall Bematech_FI_SubTotal( char * SubTotal );
extern __stdcall Bematech_FI_NumeroCupom( char * NumeroCupom );
extern __stdcall Bematech_FI_LeituraXSerial();
extern __stdcall Bematech_FI_VersaoFirmware( char * VersaoFirmware );
extern __stdcall Bematech_FI_CGC_IE( char * CGC, char * IE );
extern __stdcall Bematech_FI_GrandeTotal( char * GrandeTotal );
extern __stdcall Bematech_FI_Cancelamentos( char * ValorCancelamentos );
extern __stdcall Bematech_FI_Descontos( char * ValorDescontos );
extern __stdcall Bematech_FI_NumeroOperacoesNaoFiscais( char * NumeroOperacoes );
extern __stdcall Bematech_FI_NumeroCuponsCancelados( char * NumeroCancelamentos );
extern __stdcall Bematech_FI_NumeroIntervencoes( char * Numero__stdcallervencoes );
extern __stdcall Bematech_FI_NumeroReducoes( char * NumeroReducoes );
extern __stdcall Bematech_FI_NumeroSubstituicoesProprietario( char * NumeroSubstituicoes );
extern __stdcall Bematech_FI_UltimoItemVendido( char * NumeroItem );
extern __stdcall Bematech_FI_ClicheProprietario( char * Cliche );
extern __stdcall Bematech_FI_NumeroCaixa( char * NumeroCaixa );
extern __stdcall Bematech_FI_NumeroLoja( char * NumeroLoja );
extern __stdcall Bematech_FI_SimboloMoeda( char * SimboloMoeda );
extern __stdcall Bematech_FI_MinutosLigada( char * Minutos );
extern __stdcall Bematech_FI_MinutosImprimindo( char * Minutos );
extern __stdcall Bematech_FI_VerificaModoOperacao( char * Modo );
extern __stdcall Bematech_FI_VerificaEpromConectada( char * Flag );
extern __stdcall Bematech_FI_FlagsFiscais( int Flag );
extern __stdcall Bematech_FI_ValorPagoUltimoCupom( char * ValorCupom );
extern __stdcall Bematech_FI_DataHoraImpressora( char * Data, char * Hora );
extern __stdcall Bematech_FI_ContadoresTotalizadoresNaoFiscais( char * Contadores );
extern __stdcall Bematech_FI_VerificaTotalizadoresNaoFiscais( char * Totalizadores );
extern __stdcall Bematech_FI_DataHoraReducao( char * Data, char * Hora );
extern __stdcall Bematech_FI_DataMovimento( char * Data );
extern __stdcall Bematech_FI_VerificaTruncamento( char * Flag );
extern __stdcall Bematech_FI_Acrescimos( char * ValorAcrescimos );
extern __stdcall Bematech_FI_ContadorBilhetePassagem( char * ContadorPassagem );
extern __stdcall Bematech_FI_VerificaAliquotasIss( char * Flag );
extern __stdcall Bematech_FI_VerificaFormasPagamento( char * Formas );
extern __stdcall Bematech_FI_VerificaRecebimentoNaoFiscal( char * Recebimentos );
extern __stdcall Bematech_FI_VerificaDepartamentos( char * Departamentos );
extern __stdcall Bematech_FI_VerificaTipoImpressora( int TipoImpressora );
extern __stdcall Bematech_FI_VerificaTotalizadoresParciais( char * Totalizadores );
extern __stdcall Bematech_FI_RetornoAliquotas( char * Aliquotas );
extern __stdcall Bematech_FI_VerificaEstadoImpressora( int ACK, int ST1, int ST2 );
extern __stdcall Bematech_FI_VerificaEstadoImpressoraStr( char * ACK, char * ST1, char * ST2 );
extern __stdcall Bematech_FI_DadosUltimaReducao( char * DadosReducao );
extern __stdcall Bematech_FI_MonitoramentoPapel( int Linhas );
extern __stdcall Bematech_FI_VerificaIndiceAliquotasIss( char * Flag );
extern __stdcall Bematech_FI_ValorFormaPagamento( char * FormaPagamento, char * Valor );
extern __stdcall Bematech_FI_ValorTotalizadorNaoFiscal( char * Totalizador, char * Valor );

// Funções de Autenticação e Gaveta de Dinheiro

extern __stdcall Bematech_FI_Autenticacao();
extern __stdcall Bematech_FI_ProgramaCaracterAutenticacao( char * Parametros );
extern __stdcall Bematech_FI_AcionaGaveta();
extern __stdcall Bematech_FI_VerificaEstadoGaveta( int EstadoGaveta );

// Funções para a Impressora Restaurante

extern __stdcall Bematech_FIR_AbreCupomRestaurante( char * Mesa, char * CGC_CPF );
extern __stdcall Bematech_FIR_RegistraVenda( char * Mesa, char * Codigo, char * Descricao, char * Aliquota, char * Quantidade,
     char * ValorUnitario, char * FlagAcrescimoDesconto, char * ValorAcrescimoDesconto );
extern __stdcall Bematech_FIR_CancelaVenda( char * Mesa, char * Codigo, char * Descricao, char * Aliquota, char * Quantidade,
     char * ValorUnitario, char * FlagAcrescimoDesconto, char * ValorAcrescimoDesconto );
extern __stdcall Bematech_FIR_ConferenciaMesa( char * Mesa, char * FlagAcrescimoDesconto, char * TipoAcrescimoDesconto,
     char * ValorAcrescimoDesconto );
extern __stdcall Bematech_FIR_AbreConferenciaMesa( char * Mesa );
extern __stdcall Bematech_FIR_FechaConferenciaMesa( char * FlagAcrescimoDesconto, char * TipoAcrescimoDesconto,
     char * ValorAcrescimoDesconto );
extern __stdcall Bematech_FIR_TransferenciaMesa( char * MesaOrigem, char * MesaDestino );
extern __stdcall Bematech_FIR_ContaDividida( char * NumeroCupons, char * ValorPago, char * CGC_CPF );
extern __stdcall Bematech_FIR_FechaCupomContaDividida( char * NumeroCupons, char * FlagAcrescimoDesconto,
     char * TipoAcrescimoDesconto, char * ValorAcrescimoDesconto, char * FormasPagamento, char * ValorFormasPagamento,
     char * ValorPagoCliente, char * CGC_CPF );
extern __stdcall Bematech_FIR_TransferenciaItem( char * MesaOrigem, char * Codigo, char * Descricao, char * Aliquota,
     char * Quantidade, char * ValorUnitario, char * FlagAcrescimoDesconto,
     char * ValorAcrescimoDesconto, char * MesaDestino );
extern __stdcall Bematech_FIR_RelatorioMesasAbertas( int TipoRelatorio );
extern __stdcall Bematech_FIR_ImprimeCardapio();
extern __stdcall Bematech_FIR_RelatorioMesasAbertasSerial();
extern __stdcall Bematech_FIR_CardapioPelaSerial();
extern __stdcall Bematech_FIR_RegistroVendaSerial( char * Mesa );
extern __stdcall Bematech_FIR_VerificaMemoriaLivre( char * Bytes );
extern __stdcall Bematech_FIR_FechaCupomRestaurante( char * FormaPagamento, char * FlagAcrescimoDesconto,
     char * TipoAcrescimoDesconto, char * ValorAcrescimoDesconto, char * ValorFormaPagto, char * Mensagem );
extern __stdcall Bematech_FIR_FechaCupomResumidoRestaurante( char * FormaPagamento, char * Mensagem );

// Função para a Impressora Bilhete de Passagem

extern __stdcall Bematech_FI_AbreBilhetePassagem( char * ImprimeValorFinal, char * ImprimeEnfatizado, char * Embarque,
     char * Destino, char * Linha, char * Prefixo, char * Agente, char * Agencia, char * Data, char * Hora,
     char * Poltrona, char * Plataforma );

// Funções de Impressão de Cheques

extern __stdcall Bematech_FI_ProgramaMoedaSingular( char * MoedaSingular );
extern __stdcall Bematech_FI_ProgramaMoedaPlural( char * MoedaPlural );
extern __stdcall Bematech_FI_CancelaImpressaoCheque();
extern __stdcall Bematech_FI_VerificaStatusCheque( int StatusCheque );
extern __stdcall Bematech_FI_ImprimeCheque( char * Banco, char * Valor, char * Favorecido, char * Cidade,
     char * Data, char * Mensagem );
extern __stdcall Bematech_FI_IncluiCidadeFavorecido( char * Cidade, char * Favorecido );
extern __stdcall Bematech_FI_ImprimeCopiaCheque();

// Funções para o TEF
extern __stdcall Bematech_FI_IniciaModoTEF();
extern __stdcall Bematech_FI_FinalizaModoTEF();

/* extern __stdcall Bematech_FITEF_Status(char* Identificacao); extern __stdcall Bematech_FITEF_VendaCartao(char* Identificacao, char*
ValorCompra); extern __stdcall Bematech_FITEF_ConfirmaVenda(char* Identificacao, char* ValorCompra, char* Header);
extern __stdcall Bematech_FITEF_NaoConfirmaVendaImpressao(char* Identificacao, char* ValorCompra);
extern __stdcall Bematech_FITEF_CancelaVendaCartao(char* Identificacao, char* ValorCompra, char* Nsu, char* NumeroCupom, char*
Hora, char* Data, char* Rede); extern __stdcall Bematech_FITEF_ImprimeTEF(char* Identificacao, char* FormaPagamento, char* ValorCompra);
extern __stdcall Bematech_FITEF_ImprimeRelatorio(); extern __stdcall Bematech_FITEF_ADM(char* Identificacao);
extern __stdcall Bematech_FITEF_VendaCompleta(char* Identificacao, char* ValorCompra, char* FormaPagamento, char* Texto);
extern __stdcall Bematech_FITEF_ConfiguraDiretorioTef(char* PathREQ, char* PathRESP);
extern __stdcall Bematech_FITEF_VendaCheque(char* Identificacao, char* ValorCompra); extern __stdcall Bematech_FITEF_ApagaResiduos(); */
// Outras Funções

extern __stdcall Bematech_FI_AbrePortaSerial();
extern __stdcall Bematech_FI_RetornoImpressora( int ACK, int ST1, int ST2 );
extern __stdcall Bematech_FI_RetornoImpressoraStr( char * ACK, char * ST1, char * ST2 );
extern __stdcall Bematech_FI_FechaPortaSerial();
extern __stdcall Bematech_FI_MapaResumo();
extern __stdcall Bematech_FI_AberturaDoDia( char * ValorCompra, char * FormaPagamento );
extern __stdcall Bematech_FI_FechamentoDoDia();
extern __stdcall Bematech_FI_ImprimeConfiguracoesImpressora();
extern __stdcall Bematech_FI_ImprimeDepartamentos();
extern __stdcall Bematech_FI_RelatorioTipo60Analitico();
extern __stdcall Bematech_FI_RelatorioTipo60Mestre();
extern __stdcall Bematech_FI_VerificaImpressoraLigada();
