 /*  BEMATECH - Ind. e Com. de Equip. Eletronicos S.A 
   Exemplo para utilizacao da biblioteca lib300fi para Linux
   Linguagem C, usando o compilador gcc do Linux
   Programadores: Claudenir Campos Andrade
                  Rodrigo Raimundo Olimpio
		  
   Descricao: Biblioteca para ser linkada com a Linguegem C ou qualquer linguagem
              DOS que possa fazer a likedicao com o compilador gcc do linux.
	      As funções são de alto nivel e devem ser chamadas sempre da forma
	      como estão prototipadas.
*****************************************************************************/
#include <stdio.h>

void ComandosInicializacao( void );
void ComandosCupomFiscal  ( void );
void RelatoriosFiscais    ( void );
void OperacoesNaoFiscais  ( void );
void InformacoesImpressora( void );
void GavetaAutenticacao   ( void );
void ImpressaoCheque      ( void );
void RetornoVariaveis     ( void );

int main (void)
{

int  RetFunction = 0;
int  Opcao = 0;
char Aux;

//Abertura da Porta Serial
  RetFunction=Bematech_FI_AbrePortaSerial("COM2");
  if(RetFunction!=0)
   {  system("clear");
      printf("ERRO AO ABRIR A PORTA SERIAL");
      exit(0);
   }
   
    do
    {
	system("clear");

	printf("                       Programa para Testes da Lib Linux\n\n");

	printf("\n [ 1 ] - Comandos de  Inicialização                 " );
	printf("\n [ 2 ] - Comandos do  Cupom Fiscal                  " );
	printf("\n [ 3 ] - Comandos dos Relatórios Fiscais            " );
	printf("\n [ 4 ] - Comandos das Operações Não Sujeitas ao ICMS" );
	printf("\n [ 5 ] - Comandos das Informações da Impressora     " );
	printf("\n [ 6 ] - Comandos da  Gaveta e de Autenticação      " );
	printf("\n [ 7 ] - Comandos de  Impressão de Cheques          " );
	printf("\n [ 0 ] - Sair" );
	
	printf("\n\n\n Digite a Opção: " );

	scanf( "%d", &Opcao);
	switch( Opcao )
	{
	    case 1: 
		ComandosInicializacao();
            	break;

	    case 2: 
            	ComandosCupomFiscal();
		break;

	    case 3: 
		RelatoriosFiscais();
            	break;

	    case 4: 
		OperacoesNaoFiscais();
            	break;

	    case 5: 
		InformacoesImpressora();
            	break;

	    case 6: 
		GavetaAutenticacao();
            	break;

	    case 7: 
		ImpressaoCheque();
            	break;
	}

    }
    while( Opcao != 0 );
    system("clear");
	
return 0;
}//Fim do Main

void ComandosInicializacao( void )
{
    int  Opcao = 0;
    int  Retorno[4];
    int  RetFunction = 0;
    char Var[3000];
    char Aux;

    do
    {

	memset(Var,  '\x0',3000);
	Retorno[0] = 0; Retorno[1] = 0; Retorno[2] = 0; Retorno[3] = 0;
	
	system("clear");

	printf("                       Comandos de Inicialização\n\n");

	printf("\n [  1 ] - Altera Símbolo Moeda                   ");
	printf("\n [  2 ] - Adiciona Alíquota                      ");
	printf("\n [  3 ] - Programa Horário de Verão              ");
	printf("\n [  4 ] - Nomeia Totalizador Não Sujeito ao ICMS ");
	printf("\n [  5 ] - Programa Arredondamento                ");
	printf("\n [  6 ] - Programa Truncamento                   ");
	printf("\n [  7 ] - Espaço Entre Linhas                    ");
	printf("\n [  8 ] - Linhas Entre Cupons                    ");
	printf("\n [  9 ] - Nomeia Departamento                    ");
	printf("\n [ 10 ] - Reseta Impressora                      ");
	printf("\n [  0 ] - Sair                                   ");
	
	printf("\n\n Digite a Opção: " );

	scanf( "%d", &Opcao);
	switch( Opcao )
	{
	    case 1: 
        	RetFunction=Bematech_FI_AlteraSimboloMoeda(" R",&Retorno);
            	break;

	    case 2: 
        	RetFunction=Bematech_FI_AdicionaAliquota("0100","0",&Retorno);
                break;

	    case 3: 
		RetFunction=Bematech_FI_ProgramaHorarioVerao(&Retorno);
                break;

	    case 4: 
                RetFunction=Bematech_FI_NomeiaTotalizadorNaoSujeitoICMS("01","Conta de Agua      ", &Retorno);
	        RetFunction=Bematech_FI_NomeiaTotalizadorNaoSujeitoICMS("02","Conta de Luz       ", &Retorno);
	        RetFunction=Bematech_FI_NomeiaTotalizadorNaoSujeitoICMS("03","Conta de Telefone  ", &Retorno);
                break;

	    case 5:  
		RetFunction=Bematech_FI_ProgramaArredondamento(&Retorno);
                break;

	    case 6:  
		RetFunction=Bematech_FI_ProgramaTruncamento(&Retorno);
                break;

	    case 7:  
		RetFunction=Bematech_FI_EspacoEntreLinhas(0,&Retorno);// default: 0
                break;   
	    
	    case 8:  
		RetFunction=Bematech_FI_LinhasEntreCupons(8,&Retorno); // default: 8
                break;   

	    case 9:
	        RetFunction=Bematech_FI_NomeiaDepartamento("02","Calcados  ", &Retorno);
                RetFunction=Bematech_FI_NomeiaDepartamento("02","Limpesa   ", &Retorno);
                RetFunction=Bematech_FI_NomeiaDepartamento("02","Frutas    ", &Retorno);
                break;

	    case 10:
		RetFunction=Bematech_FI_ResetaImpressora(&Retorno);
                break;   

	} // fim do switch

	if( Opcao > 0 && Opcao < 11 )
	{
	    printf("\r\nStatus da Impressora: ACK=%d, ST1=%d, ST2=%d",Retorno[0],Retorno[1],Retorno[2]);
	    printf("\r\nRetorno da Variavel = %s", Var);	
	    printf("\r\nPressione Enter para Continuar");
	    Aux = getchar();
	    Aux = getchar();
	}
    }
    while( Opcao != 0 );
}

void ComandosCupomFiscal( void )
{
    int  Opcao = 0;
    int  Retorno[4];
    int  RetFunction = 0;
    char Var[3000];
    char Aux;

    do
    {
	memset(Var,  '\x0',3000);
	Retorno[0] = 0; Retorno[1] = 0; Retorno[2] = 0; Retorno[3] = 0;
	
	system("clear");

	printf("                       Comandos do Cupom Fiscal\n\n");

	printf("\n [  1 ] - Abre Cupom                     " );
	printf("\n [  2 ] - Vende Item                     " );
	printf("\n [  3 ] - Vende Item com 3 Casas no Valor" );
	printf("\n [  4 ] - Vende Item com Departamento    " );
	printf("\n [  5 ] - Inicia Fechamento Cupom        " );
	printf("\n [  6 ] - Efetua Forma Pagamento	   " );
	printf("\n [  7 ] - Termina o Fechamento do Cupom  " );
	printf("\n [  8 ] - Verifica Forma de Pagamento	   " );
	printf("\n [  9 ] - Programa Formas de pagamento   " );
	printf("\n [ 10 ] - Cancela Item Anterior          " );
	printf("\n [ 11 ] - Cancela Item Genérico          " );
	printf("\n [ 12 ] - Cancela Cupom	 	   " );
	printf("\n [ 13 ] - Programa Unidade Medida        " );
	printf("\n [ 14 ] - Aumenta Descrição do Item      " );
	printf("\n [  0 ] - Sair" );
	
	printf("\n\n Digite a Opção: " );

	scanf( "%d", &Opcao);
	switch( Opcao )
	{
	    case 1: 
		RetFunction = Bematech_FI_AbreCupom("", &Retorno);
            	break;
	    
	    case 2:  
		RetFunction=Bematech_FI_VendeItem("1234567890123","Teste De Produto.............","II","0001","00000100","0000",&Retorno);
                RetFunction=Bematech_FI_VendeItem("1234567890123","Teste De Produto..Com Descon.","FF","0001","00000100","1000",&Retorno);
                RetFunction=Bematech_FI_VendeItem("1234567890123","Teste De Produto..Fracionario","FF","0001000","00000100","0000",&Retorno);
                RetFunction=Bematech_FI_VendeItem("1234567890123","Teste De Produto..Desc Valor.","FF","0001000","00000100","00000010",&Retorno);
        	break;

	    case 3:  
		RetFunction=Bematech_FI_VendeItem3CasasValor("1", "Venda Item 3 casas           ", "FF","0000480", "00010000","0000", &Retorno);
                break;

	    case 4:  
		RetFunction=Bematech_FI_VendeItemDepartamento("12345","Venda de Item com Departamento","01", "0001000","000001000", "0000000000","0000000010","01","KG",&Retorno);
                break;   

	    case 5:  
		RetFunction=Bematech_FI_IniciaFechamentoCupom("D","0000",&Retorno);
                break;

	    case 6:  
		RetFunction=Bematech_FI_EfetuaFormaPagamento("02","00000000000380","Descricao opcional da forma de pagamento",&Retorno);    
                break;

	    case 7:  
		RetFunction=Bematech_FI_TerminaFechamentoCupom("",&Retorno);   
                break;
		
	    case 8:  
		RetFunction=Bematech_FI_VerificaFormaPagamento("Cartao          ", Var ,&Retorno);
                break;

	    case 9:  
		RetFunction=Bematech_FI_ProgramaFormasPagamento("Cartao          |Cartao RedShop  |Cheque          |Cartao Visa     |Cart Master Card|Ticket          ",&Retorno);    
                break;

	    case 10:  
		RetFunction=Bematech_FI_CancelaItemAnterior(&Retorno);
                break;
	    
	    case 11:  
		RetFunction=Bematech_FI_CancelaItemGenerico("0001", &Retorno);
                break;

	    case 12:  RetFunction=Bematech_FI_CancelaCupom(&Retorno);
                break;
		
	    case 13:  
		RetFunction=Bematech_FI_ProgramaUnidadeMedida("KG",&Retorno);
                break;
   
	    case 14:  
		RetFunction=Bematech_FI_AumentaDescricaoItem("Descricao do item com ate 200 caracters",&Retorno);
                break;

	} // fim do switch

	if( Opcao != 0 && Opcao < 15 )
	{
	    printf("\r\nStatus da Impressora: ACK=%d, ST1=%d, ST2=%d",Retorno[0],Retorno[1],Retorno[2]);
	    printf("\r\nRetorno da Variavel = %s", Var);	
	    printf("\r\nPressione Enter para Continuar");
	    Aux = getchar();
	    Aux = getchar();
	}
    }
    while( Opcao != 0 );

}
   
void RelatoriosFiscais( void )
{
    int  Opcao = 0;
    int  Retorno[4];
    int  RetFunction = 0;
    char Var[3000];
    char Aux;

    do
    {
	memset(Var,  '\x0',3000);
	Retorno[0] = 0; Retorno[1] = 0; Retorno[2] = 0; Retorno[3] = 0;
	system("clear");

	printf("                         Comandos dos Relatórios Fiscais\n\n");

	printf("\n [ 1 ] - Leitura X                                     " );
	printf("\n [ 2 ] - Redução Z                                     " );
	printf("\n [ 3 ] - Leitura Memória Fiscal por Data               " );
	printf("\n [ 4 ] - Leitura da Memória Fiscal por Redução         " );
	printf("\n [ 5 ] - Leitura Memória Fiscal por Data pela Serial   " );
	printf("\n [ 6 ] - Leitura Memória Fiscal por Redução pela Serial" );
	printf("\n [ 0 ] - Sair" );

	printf("\n\n Digite a Opção: " );

	scanf( "%d", &Opcao);
	switch( Opcao )
	{
	    case 1: 
		RetFunction = Bematech_FI_LeituraX(&Retorno);
            	break;

	    case 2: 
		RetFunction = Bematech_FI_ReducaoZ(&Retorno);
            	break;

	    case 3: 
		RetFunction = Bematech_FI_LeituraMemoriaFiscalData("050401", "050401", &Retorno);
            	break;

	    case 4: 
		RetFunction = Bematech_FI_LeituraMemoriaFiscalReducao("0001", "0001", &Retorno);
            	break;

	    case 5: 
		RetFunction = Bematech_FI_LeituraMemoriaFiscalDataSerial("050401", "050401", &Retorno);
            	break;

	    case 6: 
		RetFunction = Bematech_FI_LeituraMemoriaFiscalReducaoSerial("0001", "0001", &Retorno);
            	break;

	    case 7: 
		RetFunction = Bematech_FI_LeituraMemoriaFiscalReducaoSerial("0001", "0001", &Retorno);
            	break;

	} // fim do switch

	if( Opcao != 0 && Opcao < 8 )
	{
	    printf("\r\nStatus da Impressora: ACK=%d, ST1=%d, ST2=%d",Retorno[0],Retorno[1],Retorno[2]);
	    printf("\r\nRetorno da Variavel = %s", Var);	
	    printf("\r\nPressione Enter para Continuar");
	    Aux = getchar();
	    Aux = getchar();
	}
    }
    while( Opcao != 0 );

}

void OperacoesNaoFiscais( void )
{
    int  Opcao = 0;
    int  Retorno[4];
    int  RetFunction = 0;
    char Var[3000];
    char Aux;

    do
    {
	memset(Var,  '\x0',3000);
	Retorno[0] = 0; Retorno[1] = 0; Retorno[2] = 0; Retorno[3] = 0;

	system("clear");

	printf("                       Comandos das Operações Não Fiscais\n\n");

	printf("\n [ 1 ] - Relatório Gerencial                   " );
	printf("\n [ 2 ] - Fecha Relatório Gerencial             " );
	printf("\n [ 3 ] - Suprimento                            " );
	printf("\n [ 4 ] - Sangria                               " );
	printf("\n [ 5 ] - Recebimento não Fiscal                " );
	printf("\n [ 6 ] - Abre Comprovante não Fiscal Vinculado " );
	printf("\n [ 7 ] - Usa Comprovante não Fiscal Vinculado  " );
	printf("\n [ 8 ] - Fecha Comprovante não Fiscal Vinculado" );
	printf("\n [ 0 ] - Sair" );
	
	printf("\n\n Digite a Opção: " );

	scanf( "%d", &Opcao);
	switch( Opcao )
	{
	    case 1: 
		RetFunction=Bematech_FI_RelatorioGerencial("1234567890..........1234567890..........1234567890..........1234567890..........1234567890..........1234567890..........",&Retorno);
                break;

	    case 2:
		RetFunction = Bematech_FI_FechaRelatorioGerencial(&Retorno);
            	break;

	    case 3:
		RetFunction = Bematech_FI_Suprimento("00000000005000", &Retorno);
            	break;

	    case 4:
		RetFunction = Bematech_FI_Sangria("00000000002000", &Retorno);
            	break;

	    case 5:
		RetFunction = Bematech_FI_RecebimentoNaoFiscal("01", "00000000003157", "Dinheiro        ", &Retorno);
            	break;

	    case 6:  
		RetFunction=Bematech_FI_AbreComprovanteNaoFiscalVinculado("Cartao          ", "", "", &Retorno);
                break;   

	    case 7:  
		RetFunction=Bematech_FI_UsaComprovanteNaoFiscalVinculado("As informacoes recebidas da operadora de cartao de credito devem ser impressas aqui.", &Retorno);
                break;   

	    case 8:  
		RetFunction=Bematech_FI_FechaComprovanteNaoFiscalVinculado(&Retorno);
                break;   

	} // fim do switch

	if( Opcao != 0 && Opcao < 9)
	{
	    printf("\r\nStatus da Impressora: ACK=%d, ST1=%d, ST2=%d",Retorno[0],Retorno[1],Retorno[2]);
	    printf("\r\nRetorno da Variavel = %s", Var);	
	    printf("\r\nPressione Enter para Continuar");
	    Aux = getchar();
	    Aux = getchar();
	}
    }
    while( Opcao != 0 );

}
void InformacoesImpressora( void )
{
    int  Opcao = 0;
    int  Retorno[4];
    int  RetFunction = 0;
    char Var[3000];
    char Aux;

    do
    {
	memset(Var,  '\x0',3000);
	Retorno[0] = 0; Retorno[1] = 0; Retorno[2] = 0; Retorno[3] = 0;

	system("clear");

	printf("                       Comandos de Informações da Impressora\n\n");

	printf("\n [ 1 ] - Leitura do Estado da Impressora       " );
	printf("\n [ 2 ] - Leitura das Alíquotas                 " );
	printf("\n [ 3 ] - Leitura dos Totalizadores Parciais    " );
	printf("\n [ 4 ] - SubTotal                              " );
	printf("\n [ 5 ] - Numero do Cupom                       " );
	printf("\n [ 6 ] - Monitoramento do Papel                " );
	printf("\n [ 7 ] - Dados da Última Redução Z             " );
	printf("\n [ 8 ] - Retorno de Variáveis - Comando 35     " );
	printf("\n [ 0 ] - Sair" );
	
	printf("\n\n Digite a Opção: " );

	scanf( "%d", &Opcao);
	switch( Opcao )
	{
	    case 1: 
		RetFunction = Bematech_FI_EstadoImpressora(&Retorno);
            	break;

	    case 2: 
		RetFunction = Bematech_FI_LeituraAliquotas(Var, &Retorno);
            	break;

	    case 3: 
		RetFunction = Bematech_FI_LeituraTotalizadoresParciais(Var, &Retorno);
            	break;

	    case 4: 
		RetFunction = Bematech_FI_SubTotal(Var, &Retorno);
            	break;

	    case 5: 
		RetFunction = Bematech_FI_NumeroCupom(Var, &Retorno);
            	break;

	    case 6: 
		RetFunction = Bematech_FI_MonitoramentoPapel(Var, &Retorno);
            	break;

	    case 7: 
		RetFunction = Bematech_FI_DadosUltimaReducao(Var, &Retorno);
            	break;

	    case 8: 
		RetornoVariaveis();
            	break;

	} // fim do switch

	if( Opcao != 0 && Opcao < 8 )
	{
	    printf("\r\nStatus da Impressora: ACK=%d, ST1=%d, ST2=%d",Retorno[0],Retorno[1],Retorno[2]);
	    printf("\r\nRetorno da Variavel = %s", Var);	
	    printf("\r\nPressione Enter para Continuar");
	    Aux = getchar();
	    Aux = getchar();
	}
    }
    while( Opcao != 0 );

}

void GavetaAutenticacao( void )
{
    int  Opcao = 0;
    int  Retorno[4];
    int  RetFunction = 0;
    char Var[3000];
    char Aux;

    do
    {
	memset(Var,  '\x0',3000);
	Retorno[0] = 0; Retorno[1] = 0; Retorno[2] = 0; Retorno[3] = 0;

	system("clear");

	printf("                     Comandos da Gaveta e Autenticação\n\n");

	printf("\n [ 1 ] - Aciona Gaveta                         " );
	printf("\n [ 2 ] - Status da Gaveta                      " );
	printf("\n [ 3 ] - Autenticação                          " );
	printf("\n [ 4 ] - Programa Caracter para Autenticação   " );
	printf("\n [ 0 ] - Sair" );

	printf("\n\n Digite a Opção: " );

	scanf( "%d", &Opcao);
	switch( Opcao )
	{
	    case 1: 
		RetFunction = Bematech_FI_AbreGaveta(&Retorno);
            	break;
	    
	    case 2:  
		RetFunction=Bematech_FI_EstadoGaveta(Var, &Retorno);
                break;

	    case 3:  
		RetFunction=Bematech_FI_Autenticacao(&Retorno);
                break;

	    case 4:  
		RetFunction=Bematech_FI_ProgramaCaracterAutenticacao("001|002|004|008|016|032|064|128|064|032|016|008|004|002|001|129|129|129", &Retorno);
                break;

	} // fim do switch

	if( Opcao != 0 && Opcao < 5 )
	{
	    printf("\r\nStatus da Impressora: ACK=%d, ST1=%d, ST2=%d",Retorno[0],Retorno[1],Retorno[2]);
	    printf("\r\nRetorno da Variavel = %s", Var);	
	    printf("\r\nPressione Enter para Continuar");
	    Aux = getchar();
	    Aux = getchar();
	}
    }
    while( Opcao != 0 );

}

void ImpressaoCheque( void )
{
    int  Opcao = 0;
    int  Retorno[4];
    int  RetFunction = 0;
    char Var[3000];
    char Aux;

    do
    {
	memset(Var,  '\x0',3000);
	Retorno[0] = 0; Retorno[1] = 0; Retorno[2] = 0; Retorno[3] = 0;

	system("clear");

	printf("                       Comandos de Impressão de Cheques\n\n");
	printf("\n [ 1 ] - Programa Moeda no Singular " );
	printf("\n [ 2 ] - Programa Moeda no Plural   " );
	printf("\n [ 3 ] - Status do Cheque           " );
	printf("\n [ 4 ] - Cancela Impressão do Cheque" );
	printf("\n [ 5 ] - Imprime Cheque             " );
	printf("\n [ 6 ] - Leitura do Cheque          " );
	printf("\n [ 0 ] - Sair" );

	printf("\n\n Digite a Opção: " );

	scanf( "%d", &Opcao);
	switch( Opcao )
	{
	    case 1: 
		RetFunction = Bematech_FI_ProgramaMoedaSingular("Real", &Retorno);
            	break;

	    case 2: 
		RetFunction = Bematech_FI_ProgramaMoedaPlural("Reais", &Retorno);
            	break;

	    case 3: 
		RetFunction = Bematech_FI_StatusCheque(Var, &Retorno);
            	break;

	    case 4: 
		RetFunction = Bematech_FI_CancelaImpressaoCheque(&Retorno);
            	break;

	    case 5: 
		RetFunction=Bematech_FI_ImprimeCheque("001","00000000015000","Bematech S/A                                 ","Curitiba                   ","20","03","2001","",&Retorno);
            	break;

	    case 6: 
		RetFunction = Bematech_FI_LeituraCheque(Var, &Retorno);
		// tem que chamar a funcao CancelaCheque pra impressora soltar
		// o cheque apos a leitura
		RetFunction = Bematech_FI_CancelaImpressaoCheque(&Retorno);
            	break;

	} // fim do switch

	if( Opcao != 0 && Opcao < 7 )
	{
	    printf("\r\nStatus da Impressora: ACK=%d, ST1=%d, ST2=%d",Retorno[0],Retorno[1],Retorno[2]);
	    printf("\r\nRetorno da Variavel = %s", Var);	
	    printf("\r\nPressione Enter para Continuar");
	    Aux = getchar();
	    Aux = getchar();
	}
    }
    while( Opcao != 0 );

}
   
void RetornoVariaveis( void )
{
    int  Opcao = 0;
    int  Retorno[4];
    int  RetFunction = 0;
    char Var[3000];
    char Aux;

    do
    {
	memset(Var,  '\x0',3000);
	Retorno[0] = 0; Retorno[1] = 0; Retorno[2] = 0; Retorno[3] = 0;
	
	system("clear");

	printf("                       Retorno de Variáveis - Comando 35 \n");

	printf("\n [  1 ] - Numero de Serie               [ 19 ] - Minutos Ligada");
	printf("\n [  2 ] - Versão do Firmware            [ 20 ] - Minutos Imprimindo             ");
	printf("\n [  3 ] - CGC/IE                        [ 21 ] - Flag de Intervenção Técnica    ");       
	printf("\n [  4 ] - Grande Total                  [ 22 ] - Flag de Eprom Conectada        ");       
	printf("\n [  5 ] - Cancelamentos                 [ 23 ] - Valor Pago no Último Cupom     ");       
	printf("\n [  6 ] - Descontos                     [ 24 ] - Data e Hora                    ");
	printf("\n [  7 ] - Contador Sequencial           [ 25 ] - Contadores Totaliz. Não Fiscais");
	printf("\n [  8 ] - Num. Operações Não Fiscais    [ 26 ] - Descrição Totaliz. Não Fiscais ");
	printf("\n [  9 ] - Numero Cupons Cancelados      [ 27 ] - Data da Última Redução Z       ");
	printf("\n [ 10 ] - Numero de Reduções            [ 28 ] - Data do Movimento              ");
	printf("\n [ 11 ] - Num. Intervenções Técnicas    [ 29 ] - Flag de Truncamento            ");
	printf("\n [ 12 ] - Num. Subst. Proprietário      [ 30 ] - Flag de Vinculação ao ISS      ");
	printf("\n [ 13 ] - Numero Ultimo Item Vendido    [ 31 ] - Acréscimos                     ");
	printf("\n [ 14 ] - Clichê do Proprietário        [ 32 ] - Contador de Bilhete de Passagem");
	printf("\n [ 15 ] - Numero do Caixa               [ 33 ] - Leitura das Formas de Pagamento");
	printf("\n [ 16 ] - Numero da Loja                [ 34 ] - Leitura dos Recebimentos(CNFNV)");
	printf("\n [ 17 ] - Moeda                         [ 35 ] - Leitura dos Departamentos      ");
	printf("\n [ 18 ] - Flags Fiscais                 [ 36 ] - Tipo da Impressora             ");

	printf("\n\n [ 0 ] - Sair" );
	
	printf("\n\n Digite a Opção: " );

	scanf( "%d", &Opcao);
	switch( Opcao )
	{
	    case 1: 
		RetFunction = Bematech_FI_NumeroSerie(Var, &Retorno);
            	break;
	    
	    case 2: 
		RetFunction = Bematech_FI_VersaoFirmware(Var, &Retorno);
            	break;

	    case 3: 
		RetFunction = Bematech_FI_CGCIE(Var, &Retorno);
            	break;

	    case 4: 
		RetFunction = Bematech_FI_GrandeTotal(Var, &Retorno);
            	break;

	    case 5: 
		RetFunction = Bematech_FI_Cancelamentos(Var, &Retorno);
            	break;

	    case 6: 
		RetFunction = Bematech_FI_Descontos(Var, &Retorno);
            	break;

	    case 7: 
		RetFunction = Bematech_FI_ContadorSequencial(Var, &Retorno);
            	break;

	    case 8: 
		RetFunction = Bematech_FI_NumeroOperacoesNaoFiscais(Var, &Retorno);
            	break;

	    case 9: 
		RetFunction = Bematech_FI_NumeroCuponsCancelados(Var, &Retorno);
            	break;

	    case 10: 
		RetFunction = Bematech_FI_NumeroReducoes(Var, &Retorno);
            	break;

	    case 11: 
		RetFunction = Bematech_FI_NumeroIntervencoesTecnicas(Var, &Retorno);
            	break;

	    case 12: 
		RetFunction = Bematech_FI_NumeroSubstituicoesProprietario(Var, &Retorno);
            	break;

	    case 13: 
		RetFunction = Bematech_FI_UltimoItemVendido(Var, &Retorno);
            	break;

	    case 14: 
		RetFunction = Bematech_FI_ClicheProprietario(Var, &Retorno);
            	break;

	    case 15: 
		RetFunction = Bematech_FI_NumeroCaixa(Var, &Retorno);
            	break;

	    case 16: 
		RetFunction = Bematech_FI_NumeroLoja(Var, &Retorno);
            	break;

	    case 17: 
		RetFunction = Bematech_FI_Moeda(Var, &Retorno);
            	break;

	    case 18: 
		RetFunction = Bematech_FI_FlagsFiscais(Var, &Retorno);
            	break;

	    case 19: 
		RetFunction = Bematech_FI_MinutosLigada(Var, &Retorno);
            	break;

	    case 20: 
		RetFunction = Bematech_FI_MinutosImprimindo(Var, &Retorno);
            	break;

	    case 21: 
		RetFunction = Bematech_FI_FlagIntervencaoTecnica(Var, &Retorno);
            	break;

	    case 22: 
		RetFunction = Bematech_FI_FlagEpromConectada(Var, &Retorno);
            	break;

	    case 23: 
		RetFunction = Bematech_FI_ValorPagoUltimoCupom(Var, &Retorno);
            	break;

	    case 24: 
		RetFunction = Bematech_FI_DataHora(Var, &Retorno);
            	break;

	    case 25: 
		RetFunction = Bematech_FI_ContadoresTotalizadoresNaoFiscais(Var, &Retorno);
            	break;

	    case 26: 
		RetFunction = Bematech_FI_DescricaoTotalizadoresNaoFiscais(Var, &Retorno);
            	break;

	    case 27: 
		RetFunction = Bematech_FI_DataUltimaReducao(Var, &Retorno);
            	break;

	    case 28: 
		RetFunction = Bematech_FI_DataMovimento(Var, &Retorno);
            	break;

	    case 29: 
		RetFunction = Bematech_FI_VerificaTruncamento(Var, &Retorno);
            	break;

	    case 30: 
		RetFunction = Bematech_FI_FlagsISS(Var, &Retorno);
            	break;

	    case 31: 
		RetFunction = Bematech_FI_Acrescimos(Var, &Retorno);
            	break;

	    case 32: 
		RetFunction = Bematech_FI_ContadorBilhetePassagem(Var, &Retorno);
            	break;

	    case 33: 
		RetFunction = Bematech_FI_LeituraFormasPagamento(Var, &Retorno);
            	break;

	    case 34: 
		RetFunction = Bematech_FI_LeituraRecebimentosNaoFiscais(Var, &Retorno);
            	break;

	    case 35: 
		RetFunction = Bematech_FI_LeituraDepartamentos(Var, &Retorno);
            	break;

	    case 36: 
		RetFunction = Bematech_FI_TipoImpressora(Var, &Retorno);
            	break;

	} // fim do switch

	if( Opcao > 0 && Opcao < 37 )
	{
	    printf( "Entrou no if " );
	    printf( "Opcao = %d", Opcao );
	    printf("\r\nStatus da Impressora: ACK=%d, ST1=%d, ST2=%d",Retorno[0],Retorno[1],Retorno[2]);
	    printf("\r\nRetorno da Variavel = %s", Var);	
	    printf("\r\nPressione Enter para Continuar");
	    Aux = getchar();
	    Aux = getchar();
	}
    }
    while( Opcao != 0 );
}
