 /*    @(#) lib20fi.a - 12/06/98 - Copyright (c) 1997-1998 Bematech S/A      *
 * ========================================================================= */

/*** Jorge - 22out98 11:16
     - Alterado para NAO usar as rotinas do C ANSI 
	- Implementado venda de item com 3 casas decimais - case 56

 *** Jorge - 01set98 10:23 
	- Alterado para receber caracteres alfanumerico no codigo do produto   

 *** Rafael Ottmann - 12/2004	
 	 - Revisão Geral do Código portado
 char  *_LAN_lib20fi_ =
       "@(#) lib20fi - Copyright (c) 1997-1998 Bematech Ltda\n"
       "               Gerado em: " __date;

/*
 *    Includes do sistema
 */

#include   <errno.h>
#include   <fcntl.h>
#include   <stdio.h>
#include   <termio.h>
#include   <stdlib.h>
#include   <unistd.h>
//#include   <varargs.h>
#include <stdarg.h>

/*
 *    Condigues de retorno possmveis desta biblioteca
 *
 *     0. Operagco normal
 *     1. Porta serial ja aberta ou ja fechada
 *     2. Erro na abertura de porta serial
 *     3. Erro na recuperagco da configuragco da porta
 *     4. Erro na reconfiguragco da porta
 *     5. Erro de recuperagco de versco de impressora
 *     6. Erro no fechamento da porta serial
 *     7. Parametro invalido para impressora fiscal
 *     8. Erro de escrita na porta serial
 *     9. Erro de leitura na porta serial
 *    10. Tempo de espera por impressora esgotado
 *    11. Retry por rejeigco de comando
 *    12. Erro na criagco/abertura do arquivo fiscal
 *    13. Erro de escrita no arquivo fiscal
 *    14. Erro ao abrir o arquivo bancos.txt 
 *    15. Erro ao abrir o arquivo retorno.txt 
 *
 */

 /*
 *    Protstipos das fungues internas
 */

/*
 *    Devices defaults para COM1 & COM2
 */


#define    LIB_TTY1A           "/dev/ttyS0"  
#define    LIB_TTY1B           "/dev/ttyS1"  
#define    LIB_TTY1C           "/dev/ttyS2"  

#define    LIB_NOME            "memoria.20fi"
#define    LIB_CRIA            ( O_RDWR | O_CREAT | O_TRUNC )
#define    LIB_MODO            0660
#define    LIB_RETRY           4
#define    LIB_TEMPO           90

#define    STX                 0x02
#define    ETX                 0x03
#define    ACK                 0x06
#define    NAK                 0x15



// int Bematech_FI_LeituraX(int *);



/*
 *    Variaveis gerais de controle
 */



 int       CmdEspecial=0;
 int       libTrace = -1,             /* controle de trace de acompanhamento */
           libErrno,                  /* erro de sistema detectado           */
           libContr,                  /* controle de acesso a porta serial   */
           libIndex,                  /* tamanho dos parametros passados     */
           libError,                  /* erro na conversco de parametros     */
           libTable,                  /* dado de tabela pri-definida         */
           libComdo,                  /* comando em execugco na impressora   */
           libTotal,                  /* tamanho total para comando a enviar */
           libLidos,                  /* nzmero de bytes a serem lidos       */
           libRetno,                  /* condigco de retorno interno         */
           libLista,                  /* indica listagem de memsria fiscal   */
           libFisco;                  /* controle de acesso ao arquivo fiscal*/
unsigned int libCksum;                /* checksum do comando                 */

 double    libBarra;                  /* csdigo de barras do produto         */
 struct    termio    libTermio [2];   /* controle de acesso ` TTY fiscal     */

/*
 *    Comando de parametros gerais de impressora
 */
 int LeituraSerial = 0; //variavel para indicar que e um cmd de leit. pela serial
 int Verifica=0;
 int libCmd35 [] = { 15,   2, 33,  9,  7,  7,  3,  3,    2,    2,   2,  2,
                      2, 186,  2,  2,  2,  1,  2,  2,    1,    1,   7,  6,
                     18, 171,  6,  3,  1,  2,  7,  3, 1925, 1550, 600 };
 int       libInd35;

/*
 *    Recuperagco de versco do firmware
 */

 char          libVersao [] = { 0x1B, '|', '3', '5', '|', '1', '|', 0x1B, 0 },
               libInform [10];
 unsigned int  libStatus; 

/*
 *    Tabelas de dados pri definidos
 */
 char Dummy[14];
 int      *libTabela,
           libTab_51 [] = { 'I', 'R', 0 },
           libTab_52 [] = { 'A', 'a', 'D', 'd', 0 },
           libTab_53 [] = { '0', '1', 0 },
           libTab_54 [] = { 0x3031, 0x3032, 0x3033, 0x3034, 0x3035,
                            0x3036, 0x3037, 0x3038, 0x3039, 0x3130,
                            0x3131, 0x3132, 0x3133, 0x3134, 0x3135,
                            0x3136, 0x4646, 0x4949, 0x4c4c, 0 },
           libTab_55 [] = { 0x5341, 0x5555 },
           libTab_56 [] = { 0x2331, 0x2332, 0x2333, 0x2334, 0x2335,
                            0x2336, 0x2337, 0x2338, 0x2339, 0 };

/*
 *    Ponteiros e buffers para geragco de comando fiscal
 */
 char      CMD[300];
 char      libsub62 [3];              /* sub-comando do case 62              */
 char      libparam62 [256];          /* parametro do sub-comando do case 62 */
 int       libFlag;                   /* acricimo / decricimo de prego       */
 int       lib_i, lib_j;              /* contadores                          */
 unsigned char libDados [2000];       /* area para cspia dos parametros      */
 unsigned char libTexto [4096];       /* area para preparagco do comando     */
 unsigned char *libParms,             /* ponteiro de pesquisa de parametros  */
               *libPrint;             /* ponterio de contrugco do comando    */
  


/* ========================================================================= *
 *                   _Trace                                                  *
 * ========================================================================= *
 *
 *    Msdulo .......
 *                   lib20fi.a
 *
 *    Sintaxe ......
 *                   void _Trace ( va_list )
 *
 *    Entrada ......
 *                   Nenhuma
 *
 *    Samda ........
 *                   Nenhuma
 *
 *    Propssito ....
 *                   Apresenta dados de operagco nesta biblioteca. Estes dados
 *                   estarco disponmveis a partir do momento que a variavel de
 *                   ambiente "TRACE" for ativada.
 *
 *    Chamadas .....
 *                   Nenhuma
 *
 *    Criado por ...
 *                   L.A.N.
 *
 * ========================================================================= */

int
 _Trace ( char *libFormat, ... )
 {

 va_list   libParms;
 char     *libAmbte;

/*
 *    Testa se gera informagco de TRACE
 */

 if ( libTrace == -1 )
      {
      libTrace = 0;
      if (( libAmbte = ( char * ) getenv ( "TRACE" )) != NULL )
           libTrace = strcmp ( libAmbte, "1" ) ? 0 : 1;
      }

/*
 *    Testa se gera informagco de TRACE
 */

 if ( libTrace )
      {
      va_start ( libParms, libFormat);
      vfprintf ( stderr, libFormat, libParms );
      }

 }    /* _Trace */

int
 _Trace2 ( char *libFormat, ... )
 {

 va_list   libParms;
 char     *libAmbte;


      {
      va_start ( libParms, libFormat);
      vfprintf ( stderr, libFormat, libParms );
      }

 }    /* _Trace *

/* ========================================================================= *
 *                   _Recupera                                               *
 * ========================================================================= *
 *
 *    Msdulo .......
 *                   lib20fi.a
 *
 *    Sintaxe ......
 *                   int _Recupera ( int libForma )
 *
 *    Entrada ......
 *                   libForma
 *                      Descrigco: Forma de parametro esperado
 *                      Uso:       Leitura
 *                      Valor:     Valores de 0 a 4
 *
 *    Samda ........
 *                   0: Parametro recuperado normalmente
 *                   7: Parametro invalido para impressora fiscal
 *
 *    Propssito ....
 *                   Recupera prsximo parametro da impressora fiscal seguindo
 *                   as restrigues de tipo de cadeia esperada:
 *
 *                     0. Delimitador inicial / final de parametros
 *                     1. Numirico
 *                     2. Alfabitico
 *                     3. Alfanumirico com brancos
 *                     4. Comentario (caracter entre 0x20 e 0x7E)
 *                     5. Binario
 *
 *                   Os parametros sco copiados para dentro do buffer libDados
 *                   a partir do buffer de entrada libParms ati que seja achado
 *                   o delimitador ('|') ou detectado um transbordo. A forma de
 *                   passagem de paremetros segue a regra:
 *
 *                     ESC | parm_1 | parm_2 | ... | parm_n | ESC
 *
 *    Chamadas .....
 *                   Nenhuma
 *
 *    Criado por ...
 *                   L.A.N.
 *
 * ========================================================================= */

 _Recupera ( libForma )
 int libForma;

 {

/*
 *    Recupera prsximo parametro do buffer
 */
 //printf("Entrou na funcao Recupera\n");
 //getch();
 for ( libTable = 0, libError = 0, libIndex = 0; ( libError == 0 ) &&
     ( libIndex < 620 ) && ( *libParms != '|' ); libParms++ )

 //printf("Vai entrar no case %d da funcao recupera\n", libForma);
 //getch();

      switch ( libForma )
           {

          /* Delimitador inicial de cadeia */

           case 0:

                if ( *libParms == 0x1B )
                     libIndex++;
                else
                     libError = 1;
                break;

          /* Cadeia numirica */

           case 1:

                if ( isdigit ( *libParms ))
                     libDados [libIndex++] = *libParms;
                else
                     libError = 1;
                break;

          /* Cadeia alfanumirica com brancos */

           case 2:

                if ( isalpha ( *libParms ) || isdigit ( *libParms ) ||
                   ( *libParms == ' ' ))
                     libDados [libIndex++] = *libParms;
                else
                     libError = 1;
                break;

          /* Delimitadores de linha para comentario */

           case 3:

                if (( *libParms == 0x0D ) || ( *libParms == 0x0A ))
                     {
                     libDados [libIndex++] = *libParms;
                     break;
                     }

          /* Caracteres ou descritores de produto */

           case 4:

                if (( *libParms >= 0x20 ) && ( *libParms <= 0xFE ))
                     libDados [libIndex++] = *libParms;
                else
                     libError = 1;
                break;

          /* Cadeia com valor binario */

           case 5:

                libDados [libIndex++] = *libParms;
                libTable = ( libTable << 8 ) + *libParms;
                break;
           }

/*
 *    Coloca terminador e salta delimitador
 */

 libDados [libIndex] = 0;
 if ( *libParms == '|' )
      libParms++;

/*
 *    Testa se parametro valido para tabela pri-definida
 */

 if ( libForma == 5 )
      {
      for ( ; *libTabela && ( *libTabela != libTable ); libTabela++ );
      libError = ( *libTabela == 0 );
      }

/*
 *    Testa se parametro valido
 */

 if ( libError || ( libIndex == 0 ))
      {
      _Trace ( "    _Recupera ( %s ) :: Parametro Invalido\n", libDados );
      return ( 7 );
      }

/*
 *    Retorna condigco da pesquisa
 */

 if ( libForma )
      _Trace ( "    _Recupera ( %s )\n", libDados );
 return ( 0 );

 }    /* _Recupera*/

/* ========================================================================= *
 *                   _Converte                                               *
 * ========================================================================= *
 *
 *    Msdulo .......
 *                   lib20fi.a
 *
 *    Sintaxe ......
 *                   void _Converte ( char *libSaida,
 *                                    char *libEntrada,
 *                                    int   libTamanho )
 *
 *    Entrada ......
 *                   libSaida
 *                      Descrigco: Cadeia ASCII decimal convertida
 *                      Uso:       Escrita
 *                      Valor:     ---
 *
 *                   libEntrada
 *                      Descrigco: Cadeia BCD a converter
 *                      Uso:       Leitura
 *                      Valor:     ---
 *
 *                   libTamanho
 *                      Descrigco: Tamanho da cadeia
 *                      Uso:       Leitura
 *                      Valor:     ---
 *
 *    Samda ........
 *                   Nenhuma
 *
 *    Propssito ....
 *                   Converte cadeia BCD em cadeia ASCII decimal ou um byte
 *                   para seu correspondente hexadecimal.
 *
 *    Chamadas .....
 *                   Nenhuma
 *
 *    Criado por ...
 *                   L.A.N.
 *
 * ========================================================================= */

 _Converte ( libSaida, libEntrada, libTamanho )

 char *libSaida;
 char *libEntrada;
 int libTamanho;

 {

 char     *libSalva;                  /* salva ponteiro de samda de dados    */


/*
 *    Converte cadeia Hexadecimal para ASCII (sempre 1 byte)
 */

 libSalva = libSaida;
 if ( ! libTamanho )
      {

     /* Primeiro nible */

      *libSaida = '0' + (( *libEntrada >> 4 ) & 0x0F );
      if ( *libSaida > '9' )
           *libSaida += 7;
      libSaida++;

     /* Segundo nible */

      *libSaida = '0' + ( *libEntrada & 0x0F );
      if ( *libSaida > '9' )
           *libSaida += 7;
      libSaida++;
      }

/*
 *    Converte cadeia BCD para ASCII pelo tamanho fornecido
 */

 else
      for ( ; libTamanho--; libEntrada++ )
           {
           *libSaida++ = (( *libEntrada >> 4 ) & 0x0F ) + '0';
           *libSaida++ = ( *libEntrada & 0x0F ) + '0';
           }

/*
 *    Coloca terminador na cadeia convertida
 */

 *libSaida = 0;
 _Trace ( "    _Converte ( %s )\n", libSalva );

 }    /* _Converte */

/* ========================================================================= *
 *                   _Imprime                                                *
 * ========================================================================= *
 *
 *    Msdulo .......
 *                   lib20fi.a
 *
 *    Sintaxe ......
 *                   int _Imprime ( void )
 *
 *    Entrada ......
 *                   Nenuma
 *
 *    Samda ........
 *                   0: Impressco normal de comando
 *                   #: Erro de execugco
 *
 *    Propssito ....
 *                   Envia comando fiscal para a impressora e espera os dados
 *                   de retorno (ACK ou NACK e bytes de estado).
 *
 *    Chamadas .....
 *                   Nenhuma
 *
 *    Criado por ...
 *                   L.A.N.
 *
 * ========================================================================= */

 _Imprime ()

 {

 int       libRetry,                  /* contador de "retrys" nos comandos   */
           libTempo,                  /* tempo maximo de espera por resposta */
           libConta,                  /* contador de bytes de resposta       */
           libRetno;                  /* condigco de retorno write / read    */
 

	   int ackTimeOut = 10;
 

 _Trace ( "--> _Imprime\n" );

/*
 /*    Loop de envio de mensagem para impressora fiscal
 */

 //printf( "\r\n Estou dentra da funcao Imprime");
 libRetry = 0;
 //printf( "\r\n libIndex: %d", libIndex);
 while ( libIndex != libLidos )
 {

     /*
      *    Envia comando de impressco fiscal
      */
      //printf( "\r\n libTexto: %s", libTexto);

    
      if ( (libRetno = write ( libContr, libTexto, libTotal )) != libTotal )
       {
           libErrno = errno;
	   //printf( "\r\n Erro de escrita na porta");

           _Trace ( "<-- _Imprime ( 8 ) :: Errno = %d\n", errno );
           return ( 8 );
       }
      //printf( "\r\nEscritos: %d", libRetno);
     /*
      *    Inicia controle de leitura da impressora
      */
	    //printf( "\r\n Inicia o controle de leitura da impressora");
	    //printf( "\r\n Inicia o controle de leitura da impressora");

      libIndex = 0;
      libConta = 0;
      memset( libDados, '\0', sizeof( libDados ) );
      libTempo = LIB_TEMPO;

     /*
      *    Le registro de confirmagco / resposta da impressora
      */

      //printf( "\r\n libConta: %d \r\n libLidos: %d", libConta, libLidos );

      if ( LeituraSerial ) // comando de leitura pela serial
      {
         unsigned char Caracter = 0;
         FILE * idArquivoRetorno;

	 if( ( idArquivoRetorno = fopen("Retorno.txt", "wb" ) ) == NULL )
	 {
	    printf("\r\n Erro na criacao do arquivo retorno.txt");
	    return 15; // erro ao criar o arquivo retorno.txt
	 }
	 
	 //printf( "\r\n Criei o arquivo retorno.txt");
	 //printf( "\r\n Vou ler os dados na porta");
         
	 libConta = 0;
	 while ( Caracter != 3 && libDados[2] != 1 ) // Caracter != EOT && ST2 != 1
	 {
	    if( ( libRetno = read ( libContr, &Caracter, 1 ) ) == 1 )
    	    {

		if ( libConta )
                     ++libConta;
                else
                {
		    /* Determina se resposta NOK */
        
		    if ( Caracter == NAK )
		    {
            		libDados[0] = 21;
			break;
		    }
	
		    if ( Caracter == ACK )
			libConta = 1;
		}
		
		if ( libConta > 0 && libConta < 4 ) // ACK, ST1, ST2 - guarda na variavel
		    libDados[ libConta -1 ] = Caracter;
		
		else // Bytes de retorno - grava no arquivo
		    putc( Caracter, idArquivoRetorno );
		
		continue;
        
	    }// if read...
	    
	    if ( libRetno == -1 ) // erro na leitura da porta
            {
                libErrno = errno;
                _Trace ( "<-- _Imprime ( 9 ) :: Errno = %d\n", errno );
                return ( 9 );
            }
	    
            /*
            *    Testa se timeout - impressora nco responde
            */

            if ( --libTempo <= 0 )
            {
                _Trace ( "<-- _Imprime ( 10 )\n" );
                return ( 10 );
            }
	    
	    if (libConta == 0 &&--ackTimeOut <= 0 )
	    {
		_Trace ("<-- _Imprime(10) - ack timeou\n");
		return (10);
	    }
	 } // while Caracter != 13
	 //printf( "\r\n Dados lidos");
	 
	 fclose( idArquivoRetorno );
	 //printf( "\r\n Fechei o arquivo retorno.txt");
         LeituraSerial = 0;
	 libIndex = 1; // só para forcar a saida do while principal

      } //if leituraserial

      else // retorno dos comandos na variavel
      {
         while ( libConta < libLidos )
         {
	    /*
            *    Comanda leitura do prsximo caracter
            */
            
            if (( libRetno = read ( libContr, &libDados [libIndex], 1 )) == 1 )
            {
		  
		//printf("\r\n\r\nLi um byte%c",libDados[libIndex]);
		
                /* Incrementa contador da resposta */
                
		//printf("\r\nResposta %d",libDados[libIndex]);
                
	        //printf( "\r\n libConta: %d", libConta);
		if ( libConta )
                     ++libConta;
                else
                {

                    /* Determina se resposta NOK */

                     if ( libDados [libIndex] == NAK )
                     {
			//printf("\r\nRetornou NAK");    
			++libIndex;
                        break;
		     }

                    /* Determina se inmcio da resposta OK */

                     if ( libDados [libIndex] == ACK )
                     {      
			//printf("\r\nRetornou ACK");
		        libConta = 1;
		     }	
                } //else do if(libConta)

               /* Incrementa contador de bytes lidos */

                ++libIndex;
                continue;
            
	    }// if do read

          /*
           *    Testa se erro de leitura
           */

           if ( libRetno == -1 )
           {
                libErrno = errno;
                _Trace ( "<-- _Imprime ( 9 ) :: Errno = %d\n", errno );
                return ( 9 );
           }

	//} // else

          /*
           *    Testa se timeout - impressora nco responde
           */
	    //printf( "\r\n Testando o time-out");

           if ( --libTempo <= 0 )
                {
                _Trace ( "<-- _Imprime ( 10 )\n" );
                return ( 10 );
                }
	   if ( libConta == 0 && --ackTimeOut <= 0)
	   {
		   return (10);
	   }
         } // fim do while

         /*
    	 *    Ajusta buffer caso caracter NAK
         */

         if ( libDados [libIndex - 1] == NAK )
         {
	     libIndex = 0;
             _Trace ( "    Retry ...\n" );
             if ( ++libRetry < LIB_RETRY )
                 continue;

             _Trace ( "<-- _Imprime ( 11 )\n" );
             return ( 11 );
         }

         /*
         *    Ajusta buffer caso caracteres invalidos no inmcio
         */

         if ( libIndex > libLidos )
         {
             memcpy ( libDados, &libDados [libIndex - libLidos], libLidos );
             libIndex = libLidos;
         }
    
      } // else

 } //fim do while principal
/*
 *    Retorna condigco normal
 */

 _Trace ( "<-- _Imprime ( 0 )\n" );
// printf( "\r\n Estou retornando da funcao imprime");

 return ( 0 );

 }    /* _Imprime */

/* ========================================================================= *
 *                   _Arquiva                                                *
 * ========================================================================= *
 *
 *    Msdulo .......
 *                   lib20fi.a
 *
 *    Sintaxe ......
 *                   int _Arquiva ( void )
 *
 *    Entrada ......
 *                   Nenuma
 *
 *    Samda ........
 *                   0: Arquivamento normal de dados
 *                   #: Erro de execugco
 *
 *    Propssito ....
 *                   Recebe dados fiscais da memsria e grava-os num arquivo.
 *
 *    Chamadas .....
 *                   Nenhuma
 *
 *    Criado por ...
 *                   L.A.N.
 *
 * ========================================================================= */

 _Arquiva ()

 {

 _Trace ( "--> _Arquiva\n" );

/*
 *    Cria / abre truncando arquivo de memsria fiscal
 */

 if (( libFisco = open ( LIB_NOME, LIB_CRIA, LIB_MODO )) == -1 )
      {
      libErrno = errno;
      _Trace ( "<-- _Arquiva ( 12 ) :: Errno = %d\n", errno );
      return ( 12 );
      }

/*
 *    Prepara area para leitura de memsria fiscal
 */

 libLidos = 0;
 libDados [0] = 0;
 while ( libDados [libLidos] != ETX )
      {

     /*
      *    Le informagues fiscais
      */

      libRetno = read ( libContr, &libDados [libLidos], 1 );
      if ( libRetno == 0 )
           continue;

     /*
      *    Trata erro de leitura
      */

      if ( libRetno < 0 )
           {
           libErrno = errno;
           _Trace ( "<-- _Arquiva ( 9 ) :: Errno = %d\n", errno );
           return ( 9 );
           }

     /*
      *    Testa se buffer de leitura cheio ou fim de transmissco
      */

      if (( libDados [libLidos] != ETX ) &&
          ( ++libLidos < sizeof ( libDados )))
           continue;

     /*
      *    Grava arquivo de memsria fiscal
      */

      if ( write ( libFisco, libDados, libLidos ) != libLidos )
           {
           libErrno = errno;
           _Trace ( "<-- _Arquiva ( 13 ) :: Errno = %d\n", errno );
           return ( 13 );
           }

     /*
      *    Reinicia contador
      */

      if ( libDados [libLidos] != ETX )
           libLidos = 0;
      }

/*
 *    Retorna condigco normal
 */

 _Trace ( "<-- _Arquiva ( 0 )\n" );
 return ( 0 );

 }    /* _Arquiva */

/* ========================================================================= *
 *                   AbrePorta                                               *
 * ========================================================================= *
 *
 *    Msdulo .......
 *                   lib20fi.a
 *
 *    Sintaxe ......
 *                   int AbrePorta ( char *libDevice )
 *
 *    Entrada ......
 *                   libDevice
 *                      Descrigco: Device associada a impressora fiscal
 *                      Uso:       Leitura
 *                      Valor:     /dev/tty__  ou COM1 ou COM2
 *
 *    Samda ........
 *                   0: Abertura normal
 *                   #: Erro de execugco
 *
 *    Propssito ....
 *                   Abre e configura uma porta serial especificada para obter
 *                   acesso ` impressora fiscal.
 *
 *    Chamadas .....
 *                   Formata
 *                   _Convert
 *
 *    Criado por ...
 *                   L.A.N.
 *
 * ========================================================================= */

 int Bematech_FI_AbrePortaSerial(porta)
 char *porta;
 {
    return AbrePorta(porta);
    
 }
 AbrePorta ( libDevice )

 char *libDevice; 

 {

 _Trace ( "--> AbrePorta ( %s )\n", libDevice );

/*
 *    Testa se porta serial ja aberta
 */

 if ( libContr > 0 )
      {
      _Trace ( "<-- AbrePorta ( 1 )\n" );
      return ( 1 );
      }

/*
 *    Converte strings para devices defaults
 */

 if ( ! strcmp ( libDevice, "COM1" ))
      libDevice = LIB_TTY1A;
 
 if ( ! strcmp ( libDevice, "COM2" ))
      libDevice = LIB_TTY1B;
	  
 if ( ! strcmp ( libDevice, "COM3" ))
      libDevice = LIB_TTY1C;


/*
 *    Abre TTY que controla a impressora
 */
 //printf("\r\n\r\n Vou abrir a Porta Serial\r\n\r\n");
  //libDevice="/dev/ttyS1";
 //printf("\r\n Porta=%s\r\n",libDevice);
 
 if (( libContr = open ( libDevice, O_RDWR|O_NOCTTY|O_NDELAY )) == -1 )
      {
        libErrno = errno;
       _Trace ( "<-- AbrePorta ( 2 ) :: Errno = %d\n", errno );
        return ( 2 );
      }
//printf("\r\n\r\nPorta aberta\r\n\r\n");


/*
 *    Le configuragco desta TTY
 */
   
   
 if ( ioctl ( libContr, TCGETA, &libTermio [0] ) == -1 )
      {
      libErrno = errno;
      _Trace ( "<-- AbrePorta ( 3 ) :: Errno = %d\n", errno );
      close(libContr);
      libContr = 0;
      return ( 3 );
      }

/*
 *    Altera configuragco para comunicagco padrco
 */

 memcpy ( &libTermio [1], &libTermio [0], sizeof ( struct termio ));
 libTermio [1].c_iflag = (IGNBRK);
 libTermio [1].c_oflag = 0;
 libTermio [1].c_cflag = B9600 | CREAD | CS8;
// libTermio [1].c_cflag |= CRTSCTS;
 libTermio [1].c_lflag = NOFLSH;
 libTermio [1].c_line  = 0;
 libTermio [1].c_cc [VMIN] = 0;
 libTermio [1].c_cc [VTIME] = 2;

/*
 *    Escreve nova configuragco para operar impressora
 */

 if ( ioctl ( libContr, TCSETA, &libTermio [1] ) == -1 )
      {
      libErrno = errno;
      _Trace ( "<-- AbrePorta ( 4 ) :: Errno = %d\n", errno );
      close(libContr);
      libContr = 0;
      return ( 4 );
      }

 {
   int flags = fcntl(libContr,F_GETFL,0);
   fcntl(libContr,F_SETFL,flags & ~O_NDELAY);
 }
   

/*
 *    Recupera revisco do microcsdigo da impressora
 */

/* if (( libRetno =  
 Formata ( libVersao, libInform, &libStatus )) == 0 )
      {
      libIndex = atoi ( &libInform [2] );
      _Trace ( "    Versco MP-20FI: %d.%02d\n",
               libIndex / 100, libIndex % 100 );
      }
*///Claudenir
/*
 *    Retorna condigco de acesso ` impressora
 */

 _Trace ( "<-- AbrePorta ( %d )\n", libRetno );
 //printf("\r\n\r\nRetorno OK");
 
 return ( 0 );

 }    /* AbrePorta */

/* ===================:====================================================== *
 *                   FechaPorta                                              *
 * ========================================================================= *
 *
 *    Msdulo .......
 *                   lib20fi.a
 *
 *    Sintaxe ......
 *                   int FechaPorta ( void )
 *
 *    Entrada ......
 *                   Nenhuma
 *
 *    Samda ........
 *                   0: Fechamento normal
 *                   #: Erro de execugco
 *
 *    Propssito ....
 *                   Restaura a configuragco da porta serial em uso fechando-a
 *                   em seguida.
 *
 *    Chamadas .....
 *                   Nenhuma
 *
 *    Criado por ...
 *                   L.A.N.
 *
 * ========================================================================= */
 int Bematech_FI_FechaPorta()
  {
   return FechaPorta();
  }
 FechaPorta ()

 {

 _Trace ( "--> FechaPorta\n" );

/*
 *    Testa se porta serial ja fechada
 */

 if ( libContr == 0 )
      {
      _Trace ( "<-- FechaPorta ( 1 )\n" );
      return ( 1 );
      }

/*
 *    Escreve configuragco original da impressora
 */

 if ( ioctl ( libContr, TCSETA, &libTermio [0] ) == -1 )
      {
//     libErrno = errno;
//      _Trace ( "<-- FechaPorta ( 4 ) :: Errno = %d\n", errno );
//      return ( 4 );
      }

/*
 *    Fecha TTY que controla a impressora
 */

 if ( close ( libContr ) == -1 )
      {
      libErrno = errno;
      _Trace ( "<-- FechaPorta ( 6 ) :: Errno = %d\n", errno );
      return ( 6 );
      }

/*
 *    Retorna condigco normal
 */

 libContr = 0;
 _Trace ( "<-- FechaPorta ( 0 )\n" );
 return ( 0 );

 }    /* FechaPorta */

/* ========================================================================= *
 *                   lib20fi.a                                               *
 * ========================================================================= *
 *
 *    Msdulo .......
 *                   lib20fi.a
 *
 *    Sintaxe ......
 *                   int Formata ( char *libComando,
 *                                 char *libRetorno,
 *                                 int  *libStatus )
 *
 *    Entrada ......
 *                   libComando
 *                      Descrigco: Comando a ser enviado ` impressora
 *                      Uso:       Leitura
 *                      Valor:     ---
 *
 *                   libRetorno
 *                      Descrigco: Informagues lidas da impressora
 *                      Uso:       Escrita
 *                      Valor:     Dependente do comando
 *
 *                   libStatus
 *                      Descrigco: Mascara de estado desta impressora
 *                      Uso:       Escrita
 *                      Valor:     Ver manual da impressora
 *
 *    Samda ........
 *                   0: Tirmino normal de execugco
 *                   #: Erro de execugco
 *
 *    Propssito ....
 *                   Prepara e envia ` impressora fiscal um comando de usuario
 *                   bem como le possmveis informagues de retorno. A forma de
 *                   recepgco de parametros como a de retorno dos mesmos segue
 *                   o critirio exposto na rotina "_Recupera".
 *
 *    Chamadas .....
 *                   _Recupera
 *                   _Imprime
 *                   _Arquiva
 *                   _Converte
 *
 *    Criado por ...
 *                   L.A.N.
 *
 * ========================================================================= */

 Formata ( libComando, libRetorno, libStatus )

 char *libComando;
 char *libRetorno;
 unsigned int  *libStatus; 
 {
  int  Comando = 0;
  int  Contador = 0;
  int  Qtde = 0, QtdByteLido = 1;
  int  FlagInformacao = 0; 
  char Auxiliar[50];
  char String[50];
  FILE *idHandle;

  memset( Auxiliar, '\x0', 50 );
  memset( String,   '\x0', 50 );

  _Trace ( "--> Formata-1\n" );
  CmdEspecial=0;
  Verifica=0;
/*
 *    Inicia contexto para comando de impressco
 */

  
 memset ( libTexto, 0, sizeof ( libTexto ));
 libPrint = &libTexto [3];
 libLidos = 0;
 libLista = 0;
 libParms = libComando;


/*
 *    Recupera comando fiscal pretendido
 */
 //if(Verifica=0)
  if( _Recupera ( 0 ) || _Recupera ( 1 ))
    {
      _Trace ( "<-- Formata-2 ( 7 )\n" );
      return ( 7 );
    }

/*
 *    Seleciona comando pedido por usuario
 */
 libComdo=atol(libDados);
 //printf("\r\nlibComdo=%d",libComdo);
 //if (libComdo==62) 
//     {Verifica=1;libComdo=35;}
 
// printf("\r\nlibComdo=%d",libComdo);
 switch ( libComdo)// = atol ( libDados ))
 {
	        
     /*
      *    Abertura de Cupom Fiscal
      *   --------------------------
      */

      case 0:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x00;
           
	   if(Verifica=1)//Tem CGC
	    {_Recupera ( 4 ); 
             strcpy ( libPrint, libDados );
             libPrint += 29;
            }
	  /* #: Controle de resposta */
           libLidos = 3;
           break;

     /*
      *    Alteragco do Smmbolo da Moeda Corrente
      *   ----------------------------------------
      */

      case 1:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x01;

          /* 1: Smmbolo da moeda */

           if ( _Recupera ( 2 ) ||
              ( libIndex < 1 || libIndex > 2 ))
                break;

           sprintf ( libPrint, "%-2s", libDados );
           libPrint += 2;

          /* #: Controle de resposta */
           //printf("Troca ficou %s",libPrint);
           libLidos = 3;
           break;

     /*
      *    Emissco de Redugco Z
      *   ----------------------
      */

      case 5:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x05;

          /* #: Controle de resposta */

           libLidos = 3;
           break;

     /*
      *    Emissco de Leitura X
      *   ----------------------
      */

      case 6:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x06;

          /* #: Controle de resposta */

           libLidos = 3;
           break;

     /*
      *    Adicao de Aliquota Tributaria
      *   -------------------------------
      */

      case 7:
	   //printf("Entrou no case de adicao de aliquota\n");
	   //getch();
           *libPrint++ = 0x1B;
           *libPrint++ = 0x07;

          /* 1: Valor da almquota */
	   //printf("Vai chamar a funcao recupera(1) dentro do case 7\n");

           if ( _Recupera ( 1 ) ||
              ( libIndex < 2 ) || ( libIndex > 4 ))
                break;

	   //printf("Saiu da funcao recupera dentro do case 7\n");
           sprintf ( libPrint, "%04d", atol ( libDados ));
           //printf("LibPrint : \n%s", libPrint);
	   libPrint += 4;
           
           if ( _Recupera ( 2 ) ||
              ( libIndex < 1 ) || ( libIndex > 1 ))
                break;

           sprintf ( libPrint, "%-1s", libDados );
           libPrint += 1;
	   //strncat(libPrint,Dummy);
	   
	   //libPrint += 5;
	   //printf("\r\nLibDados=%s\r\n",Dummy);
          /* #: Controle de resposta */

           libLidos = 3;
           break;

     /*
      *    Emissco de Leitura de Memsria Fiscal
      *   --------------------------------------
      */

      case 8:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x08;

           if ( _Recupera(1) )
                break;

	   if( strlen( libDados ) == 1 ) // leitura por reducoes
	   {
		// 1- Parametro fixo de 1 byte 
		
		if( libIndex != 1 )
		    break;

		sprintf( libPrint, "%s", libDados );
		libPrint += 1;
		
		// 2 - Parametro fixo de 1 byte 
		
        	if ( _Recupera(1) || libIndex != 1 )
		    break;

		sprintf( libPrint, "%s", libDados );
		libPrint += 1;

		// 3 - Reducao inicial 
		
        	if ( _Recupera(1) || libIndex != 4 )
		    break;

		sprintf( libPrint, "%s", libDados );
		libPrint += 4;

		// 4 - Parametro fixo de 1 byte 
		
		if( _Recupera(1) || libIndex != 1 )
		    break;

		sprintf( libPrint, "%s", libDados );
		libPrint += 1;
		
		// 5 - Parametro fixo de 1 byte 
		
        	if ( _Recupera(1) || libIndex != 1 )
		    break;

		sprintf( libPrint, "%s", libDados );
		libPrint += 1;

		// 6 - Reducao Final 
		
        	if ( _Recupera(1) || libIndex != 4 )
		    break;

		sprintf( libPrint, "%s", libDados );
		libPrint += 4;
	   }

	   else // Leitura por datas
	   {
		// 1- dia inicial 
		
		if( libIndex != 2 )
		    break;

		sprintf( libPrint, "%s", libDados );
		libPrint += 2;
		
		// 2 - Mes inicial               
		
        	if ( _Recupera(1) || libIndex != 2 )
		    break;

		sprintf( libPrint, "%s", libDados );
		libPrint += 2;

		// 3 - Ano inicial  
		
        	if ( _Recupera(1) || libIndex != 2 )
		    break;

		sprintf( libPrint, "%s", libDados );
		libPrint += 2;

		// 4 - dia final   
		
		if( _Recupera(1) || libIndex != 2 )
		    break;

		sprintf( libPrint, "%s", libDados );
		libPrint += 2;
		
		// 5 - Mes final                 
		
        	if ( _Recupera(1) || libIndex != 2 )
		    break;

		sprintf( libPrint, "%s", libDados );
		libPrint += 2;

		// 6 - Ano final    
		
        	if ( _Recupera(1) || libIndex != 2 )
		    break;

		sprintf( libPrint, "%s", libDados );
		libPrint += 2;

	   }
          
	  // 7: Saida: "I" - impressora  "R" - Serial

           if ( _Recupera ( 2 ) || libIndex != 1 )
                break;

           sprintf ( libPrint, "%s", libDados );
           libPrint += 1;

	   /* #: Controle de resposta */

	   if( libDados[0] == 'R' || libDados[0] == 'r' ) // leitura pela serial
	   {
		libIndex = 0;      // para entrar no while da funcao Imprime
		LeituraSerial = 1; // comando de leitura pela serial
		libLidos = 1; 
	   }
	    
	   else
		libLidos = 3;

           break;

     /*
      *    Venda de Item
      *   ---------------
      */

      case 9:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x09;

          /* 1: Csdigo de Barras */

/***       if ( _Recupera ( 1 ) ||
 ***          ( libIndex < 1 ) || ( libIndex > 13 ))
 ***            break;
 ***
 ***       libBarra = atof ( libDados );
 ***       sprintf ( libPrint, "%013.0f", libBarra );
 ***       libPrint += 13;
 ***/
           
           if ( _Recupera ( 4 ) ||
              ( libIndex < 1 ) || ( libIndex > 13 ))
                break;
    
           sprintf ( libPrint, "%-13s", libDados );
           libPrint += 13;

          /* 2: Descrigco do Produto */

           if ( _Recupera ( 4 ) ||
              ( libIndex < 1 ) || ( libIndex > 29 ))
                break;

           sprintf ( libPrint, "%-29s", libDados );
           libPrint += 29;

          /* 3: Almquota Fiscal: "01" a "16", "II", "FF" ou "NN" */

           libTabela = libTab_54;
/***       if ( _Recupera ( 5 )) ***/
/***            break;           ***/
           if ( _Recupera ( 4 ))
                break;

           strcpy ( libPrint, libDados );
           libPrint += 2;

          /* 4: Quantidade do Produto */

           if ( _Recupera ( 1 ) ||
              ( libIndex < 1 ) || ( libIndex > 7 ))
                break;

           if ( libIndex > 4 )
                {
                sprintf ( libPrint, "%07d", atol ( libDados ));
                libPrint += 7;
                }
           else
                {
                sprintf ( libPrint, "%04d", atol ( libDados ));
                libPrint += 4;
                }

          /* 5: Valor do Produto */

           if ( _Recupera ( 1 ) ||
              ( libIndex < 2 ) || ( libIndex > 8 ))
                break;

           sprintf ( libPrint, "%08d", atol ( libDados ));
           libPrint += 8;

          /* 6: Desconto no Produto */

           if ( _Recupera ( 1 ) ||
              ( libIndex < 2 ) || ( libIndex > 8 ))
                break;

           if ( libIndex > 4 )
                {
                sprintf ( libPrint, "%08d", atol ( libDados ));
                libPrint += 8;
                }
           else
                {
                sprintf ( libPrint, "%04d", atol ( libDados ));
                libPrint += 4;
                }

          /* #: Controle de resposta */

           libLidos = 3;
           break;

      case 56:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x38;

          /* 1: Csdigo de Barras */

/***       if ( _Recupera ( 1 ) ||
 ***          ( libIndex < 1 ) || ( libIndex > 13 ))
 ***            break;
 ***
 ***       libBarra = atof ( libDados );
 ***       sprintf ( libPrint, "%013.0f", libBarra );
 ***       libPrint += 13;
 ***/

           if ( _Recupera ( 4 ) || 
              ( libIndex < 1 ) || ( libIndex > 13 ))
                break;
    
           sprintf ( libPrint, "%-13s", libDados );
           libPrint += 13;

          /* 2: Descrigco do Produto */

           if ( _Recupera ( 4 ) ||
              ( libIndex < 1 ) || ( libIndex > 29 ))
                break;

           sprintf ( libPrint, "%-29s", libDados );
           libPrint += 29;

          /* 3: Almquota Fiscal: "01" a "16", "II", "FF" ou "NN" */

           libTabela = libTab_54;
/***       if ( _Recupera ( 5 )) ***/
/***            break;           ***/
           if ( _Recupera ( 4 ))
                break;

           strcpy ( libPrint, libDados );
           libPrint += 2;

          /* 4: Quantidade do Produto */

           if ( _Recupera ( 1 ) ||
              ( libIndex < 1 ) || ( libIndex > 7 ))
                break;

           if ( libIndex > 4 )
                {
                sprintf ( libPrint, "%07d", atol ( libDados ));
                libPrint += 7;
                }
           else
                {
                sprintf ( libPrint, "%04d", atol ( libDados ));
                libPrint += 4;
                }

          /* 5: Valor do Produto */

           if ( _Recupera ( 1 ) ||
              ( libIndex < 2 ) || ( libIndex > 8 ))
                break;

           sprintf ( libPrint, "%08d", atol ( libDados ));
           libPrint += 8;

          /* 6: Desconto no Produto */

           if ( _Recupera ( 1 ) ||
              ( libIndex < 2 ) || ( libIndex > 8 ))
                break;

           if ( libIndex > 4 )
                {
                sprintf ( libPrint, "%08d", atol ( libDados ));
                libPrint += 8;
                }
           else
                {
                sprintf ( libPrint, "%04d", atol ( libDados ));
                libPrint += 4;
                }

          /* #: Controle de resposta */

           libLidos = 3;
           break;

     /*
      *    Fechamento de Cupom Fiscal sem Formas de Pagamento
      *   ----------------------------------------------------
      */

      case 10:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x0A;

          /* 1: Valor do acriscimo ou desconto percentual */

           if ( _Recupera ( 1 ) ||
              ( libIndex < 2 ) || ( libIndex > 4 ))
                break;

           sprintf ( libPrint, "%04d", atol ( libDados ));
           libPrint += 4;

          /* 2: Valor pago */

           if ( _Recupera ( 1 ) ||
              ( libIndex < 2 ) || ( libIndex > 14 ))
                break;

           sprintf ( libPrint, "%014d", atol ( libDados ));
           libPrint += 14;

          /* 3: Indicagco de acriscimo ou desconto */

           libTabela = libTab_52;
           if ( _Recupera ( 5 ))
                break;

           *libPrint++ = libDados [0];
           libFlag = 0;
           switch ( libDados [0] )
                {

               /* 2: Por valor */

                case 'a':
                case 'd':

                     if ( _Recupera ( 1 ) ||
                        ( libIndex < 2 ) || ( libIndex > 14 ))
                          break;

                     sprintf ( libPrint, "%014d", atol ( libDados ));
                     libPrint += 14;

               /* 2: Por percentual (valor ja fornecido) */

                case 'A':
                case 'D':

                     libFlag = 1;
                     break;
                }
           if ( ! libFlag )
                break;

          /* 4: Mensagem promocional */

           if ( ! _Recupera ( 3 ))
                {
                if ( libIndex > 496 )
                     libIndex = 496;
                strncpy ( libPrint, libDados, libIndex );
                libPrint += libIndex;
                }

          /* #: Controle de resposta */

           libLidos = 3;
           break;

     /*
      *    Cancelamento de Item Anterior
      *   -------------------------------
      */

      case 13:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x0D;

          /* #: Controle de resposta */

           libLidos = 3;
           break;

     /*
      *    Cancelamento de Cupom
      *   -----------------------
      */

      case 14:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x0E;

          /* #: Controle de resposta */

           libLidos = 3;
           break;

     /*
      *    Autenticagco de Documento (Modo 1)
      *   ------------------------------------
      */

      case 16:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x10;

          /* #: Controle de resposta */

           libLidos = 3;
           break;

     /*
      *    Programagco de Horario de Verco
      *   ---------------------------------
      */

      case 18:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x12;

          /* #: Controle de resposta */

           libLidos = 3;
           break;

     /*
      *    Leitura de Estado da Impressora
      *   ---------------------------------
      */

      case 19:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x13;

          /* #: Controle de resposta */

           libLidos = 3;
           break;

     /*
      *    Relatsrio Nco Sujeito ao ICMS
      *   -------------------------------
      */

      case 20:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x14;

          /* 1: Informes para o cupom */

           if ( ! _Recupera ( 3 ))
                {
                if ( libIndex > 620 )
                     libIndex = 620;
                strncpy ( libPrint, libDados, libIndex );
                libPrint += libIndex;
                }

          /* #: Controle de resposta */

           libLidos = 3;
           break;

     /*
      *    Fechamento de Relatsrio Nco Sujeito ao ICMS
      *   ---------------------------------------------
      */

      case 21:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x15;

          /* #: Controle de resposta */

           libLidos = 3;
           break;

     /*
      *    Acionamento da Gaveta de Dinheiro
      *   -----------------------------------
      */

      case 22:

           /* luc */

           _Trace ( "<-- SAV Comando 22 \n" );
           *libPrint++ = 0x1B;
           *libPrint++ = 0x16;

         /* 1: Tempo em milisegundos */

           /*
           if ( _Recupera ( 1 ) ||
              ( atol ( libDados ) > 255 ))
                break;
           */

           _Trace ( "<-- SAV libDados: %s \n",libDados );
           *libPrint++ = atol ( libDados );

          /* #: Controle de resposta */

           libLidos = 3;
           break;

     /*
      *    Leitura do Estado da Gaveta de Dinheiro
      *   -----------------------------------------
      */

      case 23:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x17;

          /* #: Controle de resposta */

           libLidos = 4;
           break;

     /*
      *    Sangria, Suprimento e CNFNV
      *   -----------------------------
      */

      case 25:
           *libPrint++ = 0x1B;
           *libPrint++ = 0x19;

          /* 1: Tipo de recebimento: "SA", "SU" ou "01" a "50" */

           if ( _Recupera ( 3 ) || libIndex != 2 )   
                break;
		
	   strcpy ( libPrint, libDados );
	   libPrint += 2; 
	   
	   /* Valor */
           if ( _Recupera ( 1 ) || libIndex != 14 )   
                break;
		
	   strcpy ( libPrint, libDados );
	   libPrint += 14; 

          /* 2: Descricao da forma de pagto */
          if ( libParms[0] != 27 ) // ESC
	  {
	     if ( _Recupera ( 3 ) || 
	        ( libIndex < 1 || libIndex > 16 ) )   
                 break;
		
	     sprintf ( libPrint, "%-16s", libDados );
	     libPrint += 16; 	   
	  }
          /* #: Controle de resposta */

           libLidos = 3;
           break;


     /*
      *    Retorno de Aliquotas
      *   ----------------------
      */

      case 26:
           CmdEspecial=26;
	   
           *libPrint++ = 0x1B;
           *libPrint++ = 0x1A;

          /* #: Controle de resposta */
           
           libLidos = 36;
           break;

     /*
      *    Retorno de Totalizadores Parciais
      *   -----------------------------------
      */

      case 27:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x1B;

          /* #: Controle de resposta */

           libLidos = 222;
           break;

     /*
      *    Retorno de Subtotal
      *   ---------------------
      */

      case 29:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x1D;
	   

          /* #: Controle de resposta */

           libLidos = 10;
           break;

     /*
      *    Retorno do Nzmero do Cupom
      *   ----------------------------
      */

      case 30:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x1E;

          /* #: Controle de resposta */
//printf("\r\nCase 30 com LibLidos 6");
           libLidos = 6;
           break;

     /*
      *    Cancelamento de Item Genirico
      *   -------------------------------
      */

      case 31:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x1F;

          /* 1: Nzmero do item */

           if ( _Recupera ( 1 ) ||
              ( libIndex < 1 ) || ( libIndex > 4 ))
                break;

           sprintf ( libPrint, "%04d", atol ( libDados ));
           libPrint += 4;
	   
	   //printf("\r\nLib=%i",libDados[0]);
	   //printf("\r\nLib=%i",libDados[1]);
           //printf("\r\nLib=%i",libDados[2]);
	   //printf("\r\nLib=%i",libDados[3]);
           //printf("\r\nLib=%i",libDados[4]);
	   //printf("\r\nLib=%i",libDados[5]);
          /* #: Controle de resposta */

           libLidos = 3;
           break;

     /*
      *    Inicia Fechamento de Cupom com Formas de Pagamento
      *   ----------------------------------------------------
      */

      case 32:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x20;

          /* 1: Indicagco de acriscimo ou desconto */

           libTabela = libTab_52;
           if ( _Recupera ( 2 ))
                break;

           *libPrint++ = libDados [0];
           libFlag = 0;
           switch ( libDados [0] )
                {

               /* 2: Por valor */

                case 'a':
                case 'd':

                     if ( _Recupera ( 1 ) ||
                        ( libIndex < 2 ) || ( libIndex > 14 ))
                          break;

                     sprintf ( libPrint, "%014d", atol ( libDados ));
                     libPrint += 14;
                     libFlag = 1;
                     break;

               /* 2: Por percentual */

                case 'A':
                case 'D':

                     if ( _Recupera ( 1 ) ||
                        ( libIndex < 2 ) || ( libIndex > 4 ))
                          break;

                     sprintf ( libPrint, "%04d", atol ( libDados ));
                     libPrint += 4;
                     libFlag = 1;
                     break;
                }

           if ( ! libFlag )
                break;

          /* #: Controle de resposta */

           libLidos = 3;
           break;

     /*
      *    Efetua Formas de Pagamento
      *   ----------------------------
      */

      case 33:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x21;

          /* 1: Descrigco da forma */

           if ( _Recupera ( 4 ) ||
              ( libIndex < 1 ) || ( libIndex > 22 ))
                break;

           sprintf ( libPrint, "%-22s", libDados );
           libPrint += 22;

          /* 2: Valor pago */

           if ( _Recupera ( 1 ) ||
              ( libIndex < 2 ) || ( libIndex > 14 ))
                break;

           sprintf ( libPrint, "%014d", atol ( libDados ));
           libPrint += 14;

          /* #: Controle de resposta */

           libLidos = 3;
           break;

     /*
      *    Termina Fechamento de Cupom com Formas de Pagamento
      *   -----------------------------------------------------
      */

      case 34:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x22;

          /* 1: Mensagem promocional */

          if ( libParms[0] != 27 ) // ESC
	  { 
		if ( _Recupera ( 3 ) )
		    break;
		    
                if ( libIndex > 490 )
                    libIndex = 490;
	
		strncpy ( libPrint, &libDados[0], libIndex );
            	libPrint += libIndex;

        	*libPrint++ = 0x0D; // chr(13)
                *libPrint++ = 0x0A; // chr(10)
	  }
          
	  /* #: Controle de resposta */

           libLidos = 3;
           break;

     /*
      *    Retorno de Variaveis
      *   ----------------------
      */

      case 35:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x23;

	   //Comando = Bematech_FI_AbrePortaSerial("COM2");
    
          /* 1: Informagco pretendida */

           if ( _Recupera ( 1 ) ||
              ( libIndex < 1 ) || ( libIndex > 3 ))
                break;

	   libInd35 = atol(libDados);
           *libPrint++ = libInd35;

          /* #: Controle de resposta */

           if ( libInd35 == 253 )
        	libLidos = 6;

	   else
		libLidos = libCmd35[libInd35] + 3;

           break;

     /*
      *    Vinculagco ao ICMS ou ISS
      *   ---------------------------
      */

      case 38:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x26;
           
	   
          /* 1: Mascara de vinculagco (ICMS -> par ou ISS -> impar) */

           if ( _Recupera ( 1 ) ||
              ( libIndex != 16 ))
                break;

           strcpy ( libPrint, libDados );
           libPrint += 16;

          /* #: Controle de resposta */

           libLidos = 3;
           break;

     /*
      *    Programa Truncamento / Arredondamento
      *   ---------------------------------------
      */

      case 39:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x27;

          /* 1: Define forma de tratamento */

          // alterado por Rodrigo 
	    
          /* bTabela = libTab_53;
           if ( _Recupera ( 5 ))
                break; */

           if ( _Recupera ( 1 ))
                break;

	   Comando = atoi( libDados );
    	   sprintf ( libPrint, "%c", Comando );
	   libPrint += 1;

          /* #: Controle de resposta */

           libLidos = 3;
           break;

     /*
      *    Nomeia Totalizadores Parciais nco Sujeitos ao ICMS
      *   ----------------------------------------------------
      */

      case 40:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x28;

          /* 1: Nzmero do totalizador: "01" a "50" */

           if ( _Recupera ( 1 ) || libIndex != 2 )
                break;

           strcpy ( libPrint, libDados );
           libPrint += 2;

          /* 2: Descrigco do Totalizador */

           if ( _Recupera ( 2 ) ||
              ( libIndex < 1 ) || ( libIndex > 19 ))
                break;

           sprintf ( libPrint, "%-19s", libDados );
           libPrint += 19;

          /* #: Controle de resposta */

           libLidos = 3;
           break;

     /*
      *    Impressao de cheques                               
      *   ---------------------
      */

     case 57:
           *libPrint++=0x1B;
	   *libPrint++=0x39;

	   if( _Recupera(1) )
		break;
		
	   if( libIndex == 1 ) // impressao cheque na impressora modelo BR400
	   {                   // que possui um parametro opcional ("0" a "2")
	        strcpy(libPrint, libDados); // com o tipo de insercao do cheque.
		libPrint += 1; 

		/* 1 - Valor do cheque */
		if ( _Recupera(1) || libIndex != 14 )
            	    break;	   
	   
		// printf("\nValor: %s", libDados );
		strcpy(libPrint, libDados);
		libPrint += 14;    

           }                 

	   else // modelo MP40
	   {
		/* 1 - Valor do cheque */
		if ( libIndex != 14 )
            	    break;	   
	   
		// printf("\nValor: %s", libDados );
		strcpy(libPrint, libDados);
		libPrint += 14;    
	   }
	   /* 2 - Favorecido */
           if ( _Recupera(4) ||
	      ( libIndex < 1 || libIndex > 45 ))
                break;	   
	   
	   //printf("\nFavorecido: %s", libDados );
           sprintf ( libPrint, "%-45s", libDados );
	   libPrint += 45;    

	   /* 3 - Localidade */
	   if ( _Recupera(4) || 
              ( libIndex < 1 || libIndex > 27 ))
                break;	   
	   
	   //printf("\nLocalidae: %s", libDados );
           sprintf ( libPrint, "%-27s", libDados );
	   libPrint += 27;    

	   /* 4 - Dia */
	   if ( _Recupera(1) || libIndex != 2 )
                break;	   
	   
	   //printf("\nDia: %s", libDados );
	   strcpy(libPrint, libDados);
	   libPrint += 2;    

	   /* 5 - Mes */
	   if ( _Recupera(1) || libIndex != 2 )
                break;	   
	   
	   //printf("\nMes: %s", libDados );
	   strcpy(libPrint, libDados);
	   libPrint += 2;

	   /* 6 - Ano */
	   if ( _Recupera(1) || libIndex != 4 )
                break;	   
	   
	   //printf("\nAno: %s", libDados );
	   strcpy(libPrint, libDados);
	   libPrint += 4;

	   /* 7 - Coluna do valor */
	   if ( _Recupera(1) || libIndex != 2 )
                break;	   
	   
	   //printf("\nColunaValor: %s", libDados );
	   Comando = atoi( libDados );
    	   sprintf ( libPrint, "%c", Comando );
	   libPrint += 1;	 

	   /* 8 - Coluna Extenso 1 */
	   if ( _Recupera(1) || libIndex != 2 )
                break;	   
	   
 	   //printf("\nColunaExtenso1: %s", libDados );
	   Comando = atoi( libDados );
    	   sprintf ( libPrint, "%c", Comando );
	   libPrint += 1;	 

	   /* 9 - Coluna do extenso 2 */
	   if ( _Recupera(1) || libIndex != 2 )
                break;	   
	   
	   //printf("\nColunaExtenso2: %s", libDados );
	   Comando = atoi( libDados );
    	   sprintf ( libPrint, "%c", Comando );
	   libPrint += 1;

	   /* 10 - Coluna do favorecido */
	   if ( _Recupera(1) || libIndex != 2 )
                break;	   
	   
	   //printf("\nColunaFavorecido: %s", libDados );
	   Comando = atoi( libDados );
    	   sprintf ( libPrint, "%c", Comando );
	   libPrint += 1;

	   /* 11 - Coluna da localidade */
	   if ( _Recupera(1) || libIndex != 2 )
                break;	   
	   
	   //printf("\nColunaLocalidade: %s", libDados );
	   Comando = atoi( libDados );
    	   sprintf ( libPrint, "%c", Comando );
	   libPrint += 1;

	   /* 12 - Coluna do dia */
	   if ( _Recupera(1) || libIndex != 2 )
                break;	   
	   
	   //printf("\nColunaDia: %s", libDados );
	   Comando = atoi( libDados );
    	   sprintf ( libPrint, "%c", Comando );
	   libPrint += 1;

	   /* 13 - Coluna do Mes */
	   if ( _Recupera(1) || libIndex != 2 )
                break;	   
	   
	   //printf("\nColunaMes: %s", libDados );
	   Comando = atoi( libDados );
    	   sprintf ( libPrint, "%c", Comando );
	   libPrint += 1;

	   /* 14 - Coluna do ano */
	   if ( _Recupera(1) || libIndex != 2 )
                break;	   
	   
	   //printf("\nColunaAno: %s", libDados );
	   Comando = atoi( libDados );
    	   sprintf ( libPrint, "%c", Comando );
	   libPrint += 1;

	   /* 15 - linha do valor */
	   if ( _Recupera(1) || libIndex != 2 )
                break;	   
	   
	   //printf("\nLinhaValor: %s", libDados );
	   Comando = atoi( libDados );
    	   sprintf ( libPrint, "%c", Comando );
	   libPrint += 1;

	   /* 16 -  linha do extenso 1 */
	   if ( _Recupera(1) || libIndex != 2 )
                break;	   
	   
	   //printf("\nLinhaExtenso1: %s", libDados );
	   Comando = atoi( libDados );
    	   sprintf ( libPrint, "%c", Comando );
	   libPrint += 1;


	   /* 17 - linha do extenso 2 */
	   if ( _Recupera(1) || libIndex != 2 )
                break;	   
	   
	   //printf("\nLinhaExtenso2: %s", libDados );
	   Comando = atoi( libDados );
    	   sprintf ( libPrint, "%c", Comando );
	   libPrint += 1;


	   /* 18 - linha do favorecido */
	   if ( _Recupera(1) || libIndex != 2 )
                break;	   
	   
	   //printf("\nLinhaFavorecido: %s", libDados );
	   Comando = atoi( libDados );
    	   sprintf ( libPrint, "%c", Comando );
	   libPrint += 1;

	   /* 19 - linha da localidade */
	   if ( _Recupera(1) || libIndex != 2 )
                break;	   
	   
	   //printf("\nLinhaLocalidade: %s", libDados );
	   Comando = atoi( libDados );
    	   sprintf ( libPrint, "%c", Comando );
	   libPrint += 1;

	   if( libParms[0] != 27 ) // ESC - ha mensagem opcional
	   {
	        /* 20 - memsagem */
		if ( _Recupera(4) || 
		   ( libIndex < 1 || libIndex > 120 ))
			break;	   
	   
		//printf("\nMensagem: %s", libDados );
        	sprintf ( libPrint, "%s", libDados );
		libPrint += strlen( libDados );
	   }
	   else
	   {
		Comando = atoi( libDados );
    		sprintf ( libPrint, "\x0" );
		libPrint += 1;
	   
	   }

          /* #: Controle de resposta */

           libLidos=3;	   
	   break;


     /*
      *    Programa Moeda no Singular
      *   ---------------------------
      */

      case 58:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x3A;

	   /* 1 - Nome da moeda no singular */
	   if ( _Recupera(2) || 
	      ( libIndex < 1 || libIndex > 19 ))
                break;	   
	
	   sprintf(libPrint, "%-19s", libDados);
	   libPrint += 19;       

          /* #: Controle de resposta */

           libLidos = 3;
           break;

     /*
      *    Programa Moeda no Plural
      *   -------------------------
      */

      case 59:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x3B;

	   /* 1 - Programa nome da moeda no plural */
	   if ( _Recupera(2) || 
	      ( libIndex < 1 || libIndex > 22 ))
                break;	   
	
	   sprintf(libPrint, "%-22s", libDados);
	   libPrint += 22;       

          /* #: Controle de resposta */

           libLidos = 3;
           break;

     /*
      *   Programa Espacamento entre Linhas 
      *   ---------------------------------
      */

     case 60:
           *libPrint++=0x1B;
	   *libPrint++=0x3C;
	   
	   /* 1 - Espacamento em dots entre as linhas */
	   if ( _Recupera(3) || 
	      ( libIndex < 1 || libIndex > 3 ))
                break;	   
	
	   Comando = atoi( libDados );
	   sprintf(libPrint, "%c", Comando);
	   libPrint += 1;       

          /* #: Controle de resposta */

           libLidos=3;	   
	   break;

     /*
      *   Porgrama Linhas entre Cupons 
      *   ----------------------------
      */

     case 61:
           *libPrint++=0x1B;
	   *libPrint++=0x3D;
	   
	   /* 1 - Espacamento em linhas entre os cupons */
	   if ( _Recupera(3) || 
	      ( libIndex < 1 || libIndex > 3 ))
                break;	   
	
	   Comando = atoi( libDados );
	   sprintf(libPrint, "%c", Comando);
	   libPrint += 1;    

          /* #: Controle de resposta */

           libLidos=3;	   
	   break;

      case 62:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x3E;

           if ( _Recupera ( 1 ))
                break;

           libComdo = atol(libDados);
	   switch ( libComdo )
	   {
		
		case 48: // Status do cheque                                           

                    *libPrint++ = 0x30;

        	    /* #: Controle de resposta */

        	    libLidos = 4;
        	    break; 		
		    
		case 49: // Cancela a impressao do cheque                           

                    *libPrint++ = 0x31;

        	    /* #: Controle de resposta */

        	    libLidos = 3;
        	    break; 		

		case 51: // Programa Unidade de Medida                                 

                    *libPrint++ = 0x33;
	            /* 2: Unidade de Medida */

        	    if ( _Recupera ( 2 ) ||
            		( libIndex < 1 ) || ( libIndex > 2 ))
            		break;

        	    sprintf ( libPrint, "%-2s", libDados );
        	    libPrint += 2;

        	    /* #: Controle de resposta */

        	    libLidos = 3;
        	    break;    

		case 52: // Aumenta a descricao do item                                

                    *libPrint++ = 0x34;
	            /* 2: Descricao do item com ate 200 caracteres */

        	    if ( _Recupera ( 3 ) ||
            		( libIndex < 1 ) || ( libIndex > 200 ))
            		break;
	
		    sprintf ( libPrint, "%s", libDados );
        	    libPrint += strlen( libDados );
		    

        	    /* #: Controle de resposta */

        	    libLidos = 3;
        	    break; 
		       
		case 54: // Monitoramento do Estado do Papel                        

                    *libPrint++ = 0x36;
		    libComdo = 62;
		    libInd35 = 54;

        	    /* #: Controle de resposta */

        	    libLidos = 5;
        	    break; 		

		case 55: // Dados da Ultima Reducao Z                   

                    *libPrint++ = 0x37;
		    libComdo = 62;
		    libInd35 = 55;

        	    /* #: Controle de resposta */

        	    libLidos = 311;
        	    break; 		
	   }

	   break;

     /*
      *    Venda de Item com Departamento
      *   --------------------------------
      */

      case 63:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x3F;

          /* 1: Indice da Situacao Tributaria */

           if ( _Recupera ( 4 ) ||
              ( libIndex < 1 ) || ( libIndex > 2 ))
                break;
     
	   //printf("\r\nAliquota: %s|", libDados );
           sprintf ( libPrint, "%s", libDados );
           libPrint += 2;
           
          /* 2: Valor Unitario */
           
	   if ( _Recupera ( 1 ) ||
              ( libIndex < 1 ) || ( libIndex > 9 ))
                break;
    
	   //printf("\r\nValor   : %s|", libDados );
           sprintf ( libPrint, "%s", libDados );
           libPrint += 9;

          /* 3: Quantidade Fracionaria */

           if ( _Recupera ( 1 ) ||
              ( libIndex < 1 ) || ( libIndex > 7 ))
                break;

	   //printf("\r\nQtde    : %s|", libDados );
           sprintf ( libPrint, "%s", libDados );
           libPrint += 7;

          /* 4: Desconto por Valor */

           if ( _Recupera ( 1 ) ||
              ( libIndex < 1 ) || ( libIndex > 10 ))
                break;

	   //printf("\r\nDesconto: %s|", libDados );
           sprintf ( libPrint, "%s", libDados );
           libPrint += 10;

          /* 5: Acrescimo por Valor */

           if ( _Recupera ( 1 ) ||
              ( libIndex < 1 ) || ( libIndex > 10 ))
                break;

	   //printf("\r\nAcrescim: %s|", libDados );
           sprintf ( libPrint, "%s", libDados );
           libPrint += 10;

          /* 6: Indice do Departamento */

           if ( _Recupera ( 1 ) ||
              ( libIndex < 1 ) || ( libIndex > 2 ))
                break;

	   //printf("\r\nDepto   : %s|", libDados );
           sprintf ( libPrint, "%s", libDados );
           libPrint += 2;

          /* 7: Parametro nao Usado - deve ser mandado 20 zeros. */

           if ( _Recupera ( 1 ) )
		break;
		
	   //printf("\r\nParametr: %s|", libDados );
           sprintf ( libPrint, "%s", libDados );
           libPrint += 20;
	   
          /* 8 Unidade de Medida */

	   if ( _Recupera ( 3 ) ||
              ( libIndex < 1 ) || ( libIndex > 2 ))
		break;

	   //printf("\r\nUnMedida: %s|", libDados );
           sprintf ( libPrint, "%s", libDados );
           libPrint += 2;
	   
          /* 9: Codigo do Produto */

           if ( _Recupera ( 1 ) ||
              ( libIndex < 1 ) || ( libIndex > 49 ))
                break;

           sprintf ( libPrint, "%s", libDados );
           libPrint += strlen( libDados );
           if ( strlen( libDados ) < 49 )
	   {
		strcpy( libPrint, "\x00" );
		libPrint++;
	   }
	   //printf("\r\nCodigo  : %s|", libDados );

          /* 6: Descricao do Produto com ate 49 caracteres */

           if ( _Recupera ( 3 ) ||
              ( libIndex < 1 ) || ( libIndex > 201 ))
                break;

           sprintf ( libPrint, "%-201s", libDados );
	   libPrint += strlen( libDados );
           if ( strlen( libDados ) < 201 )
	   {
		strcpy( libPrint, "\x00" );
		libPrint++;
	   }
	   //printf("\r\nProduto : %s|", libDados );
	        
          /* #: Controle de resposta */

           libLidos = 3;
           break;

     /*
      *   Programa Caracter Autenticacao
      *   ------------------------------
      */

      case 64:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x40;
	   

	   for( Contador = 0; Contador < 18; Contador++ )
	   {
	      if ( _Recupera ( 1 ) || libIndex != 3 )
                  break;
     
              //printf("\r\nlibDados: %s", libDados );
	      Comando = atoi( libDados );
	      sprintf ( libPrint, "%c", Comando );
              libPrint += 1;
	   }    

           /* #: Controle de resposta */

           libLidos = 3;
           break;


      /*
      *    Nomeia Departamentos 
      *   ----------------------
      */

      case 65:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x41;

          /* 1: Numero do departamento: "01" a "20" */

           if ( _Recupera ( 1 ) || libIndex != 2 )
                break;

           strcpy ( libPrint, libDados );
           libPrint += 2;

          /* 2: Descricao do Departamento */

           if ( _Recupera ( 2 ) ||
              ( libIndex < 1 ) || ( libIndex > 10 ))
                break;

           sprintf ( libPrint, "%-10s", libDados );
           libPrint += 10;

          /* #: Controle de resposta */

           libLidos = 3;
           break;

     /*
      *   Abre Comprovante Nao Fiscal Vinculado 
      *   -------------------------------------
      */
      case 66:
           *libPrint++ = 0x1B;
           *libPrint++ = 0x42;

          /* 1: Descricao da forma de pagamento */

           if ( _Recupera ( 3 ) || 
	      ( libIndex < 1 || libIndex > 16 ) )   
                break;
		
	   sprintf( libPrint, "%-16s", libDados );
	   libPrint += 16; 
	   
          /* 2: Valor */
          if ( libParms[0] != 124 && libParms[0] != 27 ) // "|" e ESC
	  {
	     if ( _Recupera ( 1 ) || libIndex != 14 )   
                 break;
		
	     sprintf ( libPrint, "%s", libDados );
	     libPrint += 14; 	   
	  }

          /* 2: Numero do Cupom ao qual ser refere o comprovante */
          if ( libParms[0] != 124 && libParms[0] != 27 ) // "|" e ESC
	  {
	     if ( _Recupera ( 1 ) || libIndex != 6 )   
                 break;
		
	     sprintf ( libPrint, "%s", libDados );
	     libPrint += 6; 	   
	  }
    
          else
	    *libParms = '\x1B'; // ESC

          /* #: Controle de resposta */
           libLidos = 3;
           break;

     /*
      *   Usa Comprovante Nao Fiscal Vinculado 
      *   ------------------------------------
      */
      case 67:
           *libPrint++ = 0x1B;
           *libPrint++ = 0x43;

          /* 1: Texto a ser impresso no comprovante */

           if ( _Recupera ( 3 ) || 
	      ( libIndex < 1 || libIndex > 620 ) )   
                break;
		
	   sprintf( libPrint, "%s", libDados );
	   libPrint += strlen( libDados ); 
	   
          /* #: Controle de resposta */
           libLidos = 3;
           break;

      /*
      *   Leitura X Serial 
      *   ----------------
      */

      case 69:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x45;

           /* #: Controle de resposta */
	   libIndex = 0;      // para entrar no while da funcao Imprime
	   LeituraSerial = 1; // comando de leitura pela serial
	   libLidos = 1; 
           break;

     /*
      *   Reseta impressora em caso de Erro 
      *   ---------------------------------
      */

     case 70:
           *libPrint++=0x1B;
	   *libPrint++=0x46;
	   
          /* #: Controle de resposta */

           libLidos=3;	   
	   break;

     /*
      *    Programa e Verifica as Formas de Pagamento
      *   -------------------------------------------- 
      */

      case 71:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x47;

          /* 1: Descricao da Forma de Pagamento */

           if ( _Recupera ( 3 ) ||
              ( libIndex < 1 ) || ( libIndex > 16 ))
                break;

           sprintf ( libPrint, "%-16s", libDados );
           libPrint += 16;

          /* #: Controle de resposta */

           libLidos = 5;
           break;

     /*
      *    Efetua Forma de Pagamento  
      *   ---------------------------
      */

      case 72:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x48;

          /* 1: Indice */

           if ( _Recupera ( 2 ) || 
              ( libIndex < 1 ) || ( libIndex > 2 ))
                break;

           //printf ( "\n libDados Indice: %s", libDados );
           sprintf ( libPrint, "%-2s", libDados );
           libPrint += 2;

          /* 2: Valor */

           if ( _Recupera ( 2 ) ||
              ( libIndex < 1 ) || ( libIndex > 14 ))
                break;

           //printf ( "\n libDados Valor : %s", libDados );
           sprintf ( libPrint, "%014d", atoi(libDados) );
           libPrint += 14;

          /* 3: Descricao opcional */

	   if( libParms[0] != 27 ) // ESC - ha mensagem opcional
	   {

	      if ( _Recupera ( 4 ) ||  ( libIndex > 80 ))
                  break;

              //printf ( "\n libDados Opcional: %s", libDados );
              sprintf ( libPrint, "%s", libDados );
              libPrint += strlen( libDados );
	   }
          
	  /* #: Controle de resposta */

           libLidos = 3;
           break;

     /*
      *    Programa Formas de Pagamento  
      *   ------------------------------
      */

      case 73:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x49;

	   while( strlen( libParms ) > 1 )
	   {
		if( _Recupera(3) || libIndex != 16 )
		    break;

                //printf("\r\n libParms: |%s|", libParms );  
		sprintf( libPrint, "%s", libDados );
		libPrint += 16;
	   }
	  /* #: Controle de resposta */

           libLidos = 3;
           break;

     /*
      *    Leitura do cheque - Somente no modelo BR400 da Unisys  
      *   ------------------------------------------------------
      */

      case 76:

           *libPrint++ = 0x1B;
           *libPrint++ = 0x4C;

	  /* #: Controle de resposta */

           libLidos = 42;
           break;

      }
//fim dos comandos

/*
 *    Testa se analise de comando completa
 */ 

 //printf("\nVerifica: %d", Verifica);
 if(Verifica==0)
 //printf("\nlibLidos: %d\n libParms: %c\n", libLidos, *libParms);
  {if (( libLidos == 0 ) || ( *libParms != 0x1B ))
      {
      _Trace ( "<-- Formata-3 Erro ( 7 )\n" );
      printf("ERRO FATAL");
      return ( 7 );
      }
  }      
/*
 *    Inclui tamanho do comando e seu checksum
 */

// printf( "\r\n Vou calcular o checksun");
 
 libTotal = libPrint - &libTexto [3] + 2;
 libTexto [0] = STX;
 libTexto [1] = ( char )( libTotal % 256 );
 libTexto [2] = ( char )( libTotal / 256 );
 

 libPrint = &libTexto [3];
 for ( libCksum = 0, libIndex = libTotal - 2; libIndex--; )
      libCksum += ( unsigned ) *libPrint++;

 *libPrint++ = ( char )( libCksum % 256 );
 *libPrint++ = ( char )( libCksum / 256 );

 libTotal += 3;
 
//printf( "\r\n Calculei o checksun"); 
 //printf("\r\nConteúdo do LibTexto %s\r\n",libPrint);

/*
 *    Envia comando de impressco / leitura de dados fiscais
 */
//printf( "\r\n Vou entrar na funcao Imprime");

 if (( libRetno = _Imprime ()) != 0 )
      {
      _Trace ( "<-- Formata-4 ( %d )\n", libRetno );
      return ( libRetno );
      }
//printf( "\r\n Saí da funcao Imprime");

/*
 *    Copia dados obtidos da impressora
 */

 *libStatus = ( libDados [libLidos - 2] << 8 ) + libDados [libLidos - 2];
 
 libStatus[0]=libDados[0];
 libStatus[1]=libDados[1];
 libStatus[2]=libDados[2];
 
  //printf("\r\nRetorno de LibSatus %d,%d,%d,%d",libDados[0],libDados[1],libStatus[2],libStatus[3]);
  //printf("\r\n");
  /*for(libLista;libLista<100;libLista++)
    {printf("N%d=%d ",libLista,libDados[libLista]);
    
    }
  */

 if (( libLidos -= 3 ) == 0 )
      {

     /*
      *    Retorna condigco normal
      */
      //printf("\n libLista: %d", libLista );
      if ( libLista == 'R' )
      {

          /* Salva informagues fiscais num arquivo */

           libLista = 0;
           libRetno = _Arquiva ();

          /* Fecha controle de arquivo fiscal */

           if ( libFisco > 0 )
                close ( libFisco );
           libFisco = 0;
      }

     /*
      *    Retorna condigco normal
      */

      _Trace ( "<-- Formata-5 ( %d )\n", libRetno );
      return ( libRetno );
      }

/*
 *    Coloca delimitadores iniciais
 */

// *libRetorno++ = 0x1B;
// *libRetorno++ = '|';

/*
 *    Copia parametros para buffer samda
 */
 //printf ("\n verificando o libComdo" );
 //printf ("\n libComdo: %d", libComdo );
 
 switch ( libComdo )
      {

     /* Leitura do Estado da Gaveta de Dinheiro */

      case 23:

            libStatus[0]=libDados[0];
	    libStatus[1]=libDados[2];
	    libStatus[2]=libDados[3];
            if( libDados[1] == 255 ) //gaveta aberta
		sprintf( libRetorno, "1" );
	    else
		sprintf( libRetorno, "0" );
	    
	    //sprintf( libRetorno, "%d", libDados[1] );
	    libRetorno += 1;
            break;

     /* Retorno de Aliquotas */

      case 26:
           
           libStatus[0]=libDados[0];
	   libStatus[1]=libDados[34];
	   libStatus[2]=libDados[35];
	              
	   _Converte ( libRetorno, &libDados [1], 1 );
           libRetorno += 2;
	    
	   
           for ( libIndex = 2; libIndex < 34; libIndex += 2 )
              {
                *libRetorno++ = '|';
                _Converte ( libRetorno, &libDados [libIndex], 2 );
                libRetorno += 4;
              }
	   
	   	         
	  break;

     /* Retorno dos Totalizadores Parciais */

      case 27:

           libStatus[0]=libDados[0];
	   libStatus[1]=libDados[220];
	   libStatus[2]=libDados[221];

           for ( libIndex = 1; libIndex < 211; libIndex += 7 )
           {
                _Converte ( libRetorno, &libDados [libIndex], 7 );
                libRetorno += 14;
                *libRetorno++ = '|';
           }
           _Converte ( libRetorno, &libDados [libIndex], 9 );
           libRetorno += 18;
           break;

     /* Retorno do Subtotal */

      case 29:
    	   libStatus[0]=libDados[0];
	   libStatus[1]=libDados[8];
	   libStatus[2]=libDados[9];

           _Converte ( libRetorno, &libDados [1], 7 );
           libRetorno += 14;
           break;

     /* Retorno do Nzmero do Cupom */

      case 30:
           libStatus[0]=libDados[0];
	   libStatus[1]=libDados[4];
	   libStatus[2]=libDados[5];
	   
           _Converte ( libRetorno, &libDados [1], 3 );
           libRetorno += 6;
           //printf("\r\nNumero Cupom %c,%c,%c,%c,%c,%c\r\n",libRetorno[0],libRetorno[1],libRetorno[2],libRetorno[3],libRetorno[4],libRetorno[5]);
	   break;

      /* Retorno de Variaveis */
                  
      case 35:
     
           switch ( libInd35 )
           {

	       /* Cadeias ASCII */

                case  0: // Numero de serie
                case  2: // CGC IE
                case 13: // Cliche do proprietario
                case 16: // Moeda

		     libStatus[0] = libDados[0];
		     libStatus[1] = libDados[libLidos + 1];
		     libStatus[2] = libDados[libLidos + 2];
		     memcpy( libRetorno, &libDados[1], libLidos );
                     libRetorno += libLidos;
                     break;

               /* Mascaras Hexadecimais */

                case 17: // Flags fiscais
		     libStatus[0]=libDados[0];
		     libStatus[1]=libDados[2];
		     libStatus[2]=libDados[3];
		     
        //             _Converte ( libRetorno, &libDados [1], 0 );
        //             libRetorno += 2;
    		     sprintf(libRetorno, "%3d", libDados[1]);
                     libRetorno += 3;
	             break;

                case 20: // Flag de intervencao tecnica
                case 21: // Flag de Eprom conectada
		     libStatus[0]=libDados[0];
		     libStatus[1]=libDados[2];
		     libStatus[2]=libDados[3];
		     
                     if( libDados[1] == 85 ) // 55h
		        sprintf( libRetorno, "1" );
		    
		    else
		        sprintf( libRetorno, "0" );

		    libRetorno += 1;
		    break;

               /*Devolução de Dois Bytes <<< */
	       case 29: // Flags de vinculacao ao ISS
	            
	            libStatus[0]=libDados[0];
		    libStatus[1]=libDados[3];
		    libStatus[2]=libDados[4];
		    
		    Comando = libDados[1];
		    
		    if( Comando >= 128 )
		    {
			Comando -= 128;
			sprintf( libRetorno, "01|" );
			libRetorno += 3;
		    }

		    if( Comando >= 64 )
		    {
			Comando -= 64;
			sprintf( libRetorno, "02|" );
			libRetorno += 3;
		    }

		    if( Comando >= 32 )
		    {
			Comando -= 32;
			sprintf( libRetorno, "03|" );
			libRetorno += 3;
		    }

		    if( Comando >= 16 )
		    {
			Comando -= 16;
			sprintf( libRetorno, "04|" );
			libRetorno += 3;
		    }

		    if( Comando >= 8 )
		    {
			Comando -= 8;
			sprintf( libRetorno, "05|" );
			libRetorno += 3;
		    }

		    if( Comando >= 4 )
		    {
			Comando -= 4;
			sprintf( libRetorno, "06|" );
			libRetorno += 3;
		    }

		    if( Comando >= 2 )
		    {
			Comando -= 2;
			sprintf( libRetorno, "07|" );
			libRetorno += 3;
		    }

		    if( Comando >= 1 )
		    {
			Comando -= 1;
			sprintf( libRetorno, "08|" );
			libRetorno += 3;
		    }

		    Comando = libDados[2];

		    if( Comando >= 128 )
		    {
			Comando -= 128;
			sprintf( libRetorno, "09|" );
			libRetorno += 3;
		    }

		    if( Comando >= 64 )
		    {
			Comando -= 64;
			sprintf( libRetorno, "10|" );
			libRetorno += 3;
		    }

		    if( Comando >= 32 )
		    {
			Comando -= 32;
			sprintf( libRetorno, "11|" );
			libRetorno += 3;
		    }

		    if( Comando >= 16 )
		    {
			Comando -= 16;
			sprintf( libRetorno, "12|" );
			libRetorno += 3;
		    }

		    if( Comando >= 8 )
		    {
			Comando -= 8;
			sprintf( libRetorno, "13|" );
			libRetorno += 3;
		    }

		    if( Comando >= 4 )
		    {
			Comando -= 4;
			sprintf( libRetorno, "14|" );
			libRetorno += 3;
		    }

		    if( Comando >= 2 )
		    {
			Comando -= 2;
			sprintf( libRetorno, "15|" );
			libRetorno += 3;
		    }

		    if( Comando >= 1 )
		    {
			Comando -= 1;
			sprintf( libRetorno, "16|" );
			libRetorno += 3;
		    }
		    --libRetorno;
		    sprintf( libRetorno, "\x00" );
	
		    break;
	       
                case 24: // Contadores dos totalizadores nao sujeitos ao ICMS
                     libStatus[0] = libDados[0];
                     libStatus[1] = libDados[19];
                     libStatus[2] = libDados[20];
		     for( libIndex = 1; libIndex < 17; libIndex += 2 )
                     {
		          _Converte( libRetorno, &libDados[libIndex], 2 );
                          libRetorno += 4;
                          *libRetorno++ = '|';
                     }
	             _Converte( libRetorno, &libDados[libIndex], 2 );
                     libRetorno += 4;
		     break;


                case 25: // Descricao dos totalizadores nao sujeitos ao ICMS

        	    libStatus[0]=libDados[0];
		    libStatus[1]=libDados[172];
		    libStatus[2]=libDados[173];

                     for ( libIndex = 1; libIndex < 172; libIndex += 19 )
                          {
                          memcpy ( libRetorno, &libDados [libIndex], 19 );
                          libRetorno += 19;
                          *libRetorno++ = '|';
                          }
                     break;

               /* Flags de configuragco */

                case 28: // Flag de truncamento

		     libStatus[0]=libDados[0];
		     libStatus[1]=libDados[2];
		     libStatus[2]=libDados[3]; 
		     if( libDados[1] == 255 ) // FF - arredondamento
			 sprintf( libRetorno, "1" );
		     else
			sprintf( libRetorno, "0" );
		     
		     libRetorno += 1;
                     break;

                case 32: // Leitura formas pagamento
		     libStatus[0]=libDados[0];
		     libStatus[1]=libDados[1926];
		     libStatus[2]=libDados[1927]; 

		/*     if( ( idHandle = fopen("Retorno.txt", "wb" ) ) == NULL )
		     {
			 printf("\r\n Erro na criacao do arquivo retorno.txt");
			 return 15; // erro ao criar o arquivo retorno.txt
		     }
		*/
		     Contador = 1;		     
		     for( Comando = 2; Comando < 1926; Comando++ )
		     {
			if ( Comando < 834 ) // 832 bytes ASCII - Descricao Forma Pagto
			{
			    if ( libDados[Comando] == '\0' )
			    {
				sprintf( libRetorno, "%s", " " );
				
				// grava em arquivo texto
				//fprintf( idHandle, "%s", " " );
			    }
				
			    else
			    {
				sprintf( libRetorno, "%c", libDados[Comando] );

				// grava em arquivo texto
				//fprintf( idHandle, "%c", libDados[Comando] );
			    }
			    
			    libRetorno++;
			    
			    if( Contador++ == 16 )
			    {
				// grava em arquivo texto
				//fprintf(idHandle, "\x0d\x0a");
				Contador = 1;
			    }

			}// fim if Comando < 834

			else if ( Comando < 1874 ) // 1040 bytes BCD - Valor forma Pagto
			{
			    sprintf( Auxiliar + strlen( Auxiliar ), "%c%c", libDados[Comando] / 0x10 + '0',
				     libDados[Comando] % 0x10  + '0' );

			    if( Contador++ == 10 )
			    {
				sprintf( String, "%s", "00" );
				strncpy( String + 2, &Auxiliar[0], 18 );
				sprintf( libRetorno, "%s", String );
				libRetorno += 20;
				
				// grava em arquivo texto
			        //fprintf( idHandle, "%s", String );
				//fprintf(idHandle, "\x0d\x0a");
				Contador = 1;

				memset( Auxiliar, '\x0', 50 );
				memset( String,   '\x0', 50 );
			    }
			
			}// comando < 1874

			else if ( Comando < 1926 ) // 52 bytes Decimal - Bytes de vinculacao
			{
			    sprintf( libRetorno,"%d", libDados[Comando] );
			    libRetorno++;
			    // grava em arquivo texto
			    //fprintf( idHandle, "%d", libDados[Comando] );

			    if( Contador++ == 20 )
			    {
				// grava em arquivo texto
				//fprintf(idHandle, "\x0d\x0a");
				Contador = 1;
			    }
			} 

		     
		     }// fim do for	

	             //fclose( idHandle );
		     break;

                case 33: // Leitura recebimentos nao fiscais

		     libStatus[0]=libDados[0];
		     libStatus[1]=libDados[1551];
		     libStatus[2]=libDados[1552]; 

		     /*if( ( idHandle = fopen("Retorno.txt", "wb" ) ) == NULL )
		     {
			 printf("\r\n Erro na criacao do arquivo retorno.txt");
			 return 15; // erro ao criar o arquivo retorno.txt
		     }*/

		     for( Contador = 1; Contador < 1551; Contador++ )
		     {

		        if ( FlagInformacao == 0 ) // 50 bytes Decimal
		        {
			    if ( libDados[Contador] == 1 )
				Qtde = libDados[Contador];
			    else
				Qtde += libDados[Contador] * 256;


			    if ( QtdByteLido++ == 2 )
			    {	
				// grava em arquivo texto
				//fprintf( idHandle,   "%04d", Qtde );

			        sprintf( libRetorno, "%04d", Qtde );
				libRetorno += 4;
				FlagInformacao = 1;
				QtdByteLido = 1;
				Qtde = 0;
			    }
		        } // if < 1552

		        else if ( FlagInformacao == 1 ) // Bytes ref. ao Valor
		        {
			    sprintf( Auxiliar + strlen( Auxiliar ), "%c%c", 
				     libDados[Contador] / 0x10 + '0',
				     libDados[Contador] % 0x10 + '0' );

			    if ( QtdByteLido++ == 10 )
			    {	
				sprintf( String, "00" );
				strncpy( String + strlen( String ), &Auxiliar[0], 18 );
				sprintf( libRetorno, "%s", String );
				libRetorno += 20;
				
				FlagInformacao = 2;
				QtdByteLido = 1;
				
				// grava no arquivo texto
				//fprintf( idHandle, "%s", String );

				memset( Auxiliar, '\x0', 25 );
				memset( String,   '\x0', 25 );
			    }
		        } // else FlagInformacao = 1

		
		        else if ( FlagInformacao == 2 ) // Bytes ref. a descricao
		        {
			    if ( libDados[Contador] == '\0' )
			    {
				sprintf( libRetorno, "%s", " " );
				//grava no arquivo texto
				//fprintf( idHandle,   "%s", " " );
			    }
			    else
			    {
				sprintf( libRetorno, "%c", libDados[Contador] );
				//grava no arquivo texto
				//fprintf( idHandle,   "%c", libDados[Contador] );
			    }

			    libRetorno += 1;
			    if ( QtdByteLido++ == 19 )
			    {	
				FlagInformacao = 0;
				QtdByteLido = 1;

				// salta de linha no arquivo texto
				//fprintf( idHandle,   "\x0d\x0a" );
			    }
		        
			}// FlagInformacao = 2
		     
		     }// fim do for

		     //fclose( idHandle );
                     break;

                case 34: // Leitura departamentos

		     libStatus[0]=libDados[0];
		     libStatus[1]=libDados[601];
		     libStatus[2]=libDados[602]; 

		     /*if( ( idHandle = fopen("Retorno.txt", "wb" ) ) == NULL )
		     {
			 printf("\r\n Erro na criacao do arquivo retorno.txt");
			 return 15; // erro ao criar o arquivo retorno.txt
		     }*/

		     for( Contador = 1; Contador < 601; Contador++ )
		     {
		        if ( FlagInformacao == 0 ) // bytes Decimal
			{
//			    sprintf( libRetorno, "%c%c", libDados[Contador] / 0x10 + '0',
//				     libDados[Contador] % 0x10  + '0' );
			    sprintf( Auxiliar + strlen( Auxiliar ), "%c%c", libDados[Contador] / 0x10 + '0',
				     libDados[Contador] % 0x10  + '0' );

//			    libRetorno += 2;
			    
			    if ( QtdByteLido++ == 20 )
			    {
				    sprintf( String, "%s", "00" );
				    strncpy( String + 2, &Auxiliar[0], 18 );
				    sprintf( libRetorno, "%s", String );
				    libRetorno += 20;
				    
				    // grava no arquivo texto
				    //fprintf( idHandle, String );

				    memset( String, '\0', 50 );
				    sprintf( String, "%s", "0" );
				    strncpy( String + 1, &Auxiliar[20], 19 );
				    sprintf( libRetorno, "%s", String );
				    libRetorno += 20;

				    // grava no arquivo texto
				    //fprintf( idHandle, String );

				    FlagInformacao = 1;
				    QtdByteLido = 1;

				    memset( Auxiliar, '\x0', 50 );
				    memset( String,   '\x0', 50 );
			    }

			}// FlagInformacao = 0

			else if ( FlagInformacao == 1 ) // Bytes ref. a descricao
			{
			    if ( libDados[Contador] == '\0' )
			    {
				sprintf( libRetorno, "%s", " " );

				// grava no arquivo texto
				//fprintf( idHandle, " " );
			    }
			    
			    else
			    {
				sprintf( libRetorno, "%c", libDados[Contador] );

				// grava no arquivo texto
				//fprintf( idHandle, "%c", libDados[Contador] );
			    }

			    
			    libRetorno++;
			    
			    if ( QtdByteLido++ == 10 )
			    {
				 FlagInformacao = 0;
				 QtdByteLido = 1;

				 // grava no arquivo texto
				 //fprintf( idHandle, "\x0d\x0a" );
			    }
			}
// FlagInformacao = 1

		     } // for
		     //fclose( idHandle );


                     break;

                case 253: // Tipo Impressora
		     libStatus[0]=libDados[0];
		     libStatus[1]=libDados[4];
		     libStatus[2]=libDados[5]; 

		     if( libDados[1] == 255 && libDados[2] == 255 && 
		         libDados[3] == 255 ) // Fiscal, gaveta, autentic.
			 sprintf( libRetorno, "%s", "1" );

		     else if( libDados[1] == 255 && libDados[2] == 255 && 
		              libDados[3] == 0 ) // Fiscal, gaveta, cutter
			 sprintf( libRetorno, "%s","2" );
		     
		     else if( libDados[1] == 255 && libDados[2] == 0 && 
		              libDados[3] == 255 ) // Fiscal, presenter, autentic
			 sprintf( libRetorno, "%s","3" );

		     else if( libDados[1] == 255 && libDados[2] == 0 && 
		              libDados[3] == 0 ) // Fiscal, presenter, cutter
			 sprintf( libRetorno, "%s","4" );

		     else if( libDados[1] == 0 && libDados[2] == 255 && 
		              libDados[3] == 255 ) // Bilhete, gaveta, autenticacao
			 sprintf( libRetorno, "%s","5" );

		     else if( libDados[1] == 0 && libDados[2] == 255 && 
		              libDados[3] == 0 ) // Bilhete, gaveta, cutter
			 sprintf( libRetorno, "%s","6" );

		     else if( libDados[1] == 0 && libDados[2] == 0 && 
		              libDados[3] == 255 ) // Bilhete, presenter, autentic
			 sprintf( libRetorno, "%s","7" );

		     else if( libDados[1] == 0 && libDados[2] == 0 && 
		              libDados[3] == 0 ) // Bilhete, presenter, cutter
			 sprintf( libRetorno, "%s","8" );

		     libRetorno++;
		     break;

                default: // Valores BCD com parametro unico */

		     libStatus[0] = libDados[0];
		     libStatus[1] = libDados[libLidos + 1];
		     libStatus[2] = libDados[libLidos + 2];
		     _Converte ( libRetorno, &libDados [1], libLidos );
                     libRetorno += ( 2 * libLidos );
                     break;
           } // fim do switch
           break;

      case 48: /* retorno do status do cheque */
            libStatus[0]=libDados[0];
	    libStatus[1]=libDados[2];
	    libStatus[2]=libDados[3];
            
	    if( libDados[1] == 18 ) //cheque em impressao
		sprintf( libRetorno, "2" );

	    else if( libDados[1] == 22 ) // cheque posicionado
		sprintf( libRetorno, "3" );

	    else if( libDados[1] == 118 ) // impressora ok
		sprintf( libRetorno, "1" );

	    else  // 122 - aguardando o posicionamento do cheque
		sprintf( libRetorno, "4" );

	    libRetorno += 1;
            break;

      case 62: 
    	   switch ( libInd35 )
           {

               case 54: // Retorno do monitoramento do estado do papel
	    	    libStatus[0]=libDados[0];
		    libStatus[1]=libDados[3];
		    libStatus[2]=libDados[4]; 
                    //printf( "\r\nlibDados[1]: %d", libDados[1] );
                    //printf( "\r\nlibDados[2]: %d", libDados[2] );
		    Comando = libDados[1] + ( libDados[2] * 256 );
                    //printf( "\r\nComando    : %d", Comando );
                    sprintf(libRetorno, "%d", Comando );
		    libRetorno += strlen(libRetorno);
                    //printf( "\r\nComando    : %d", Comando );
	       break;
               
	       case 55: //Retorno dos dados da UIltima Reducao
	    	    libStatus[0]=libDados[0];
		    libStatus[1]=libDados[309];
		    libStatus[2]=libDados[310]; 
                    _Converte ( libRetorno, &libDados [1], 308 );
                    libRetorno += 616;
		    break;
	   }
	   break;
	   
      case 71: /* retorno do indice da forma de pagamento */
            libStatus[0]=libDados[0];
	    libStatus[1]=libDados[3];
	    libStatus[2]=libDados[4];
            memcpy ( libRetorno, &libDados [1], 2 );
            libRetorno += 2;
            break;

      case 76: /* retorno dos dados do cheque*/
            libStatus[0]=libDados[0];
	    libStatus[1]=libDados[40];
	    libStatus[2]=libDados[41];
            memcpy ( libRetorno, &libDados [1], 39 );
            libRetorno += 39;
            break;
      }

/*
 *    Coloca delimitadores finais
 */
 //*libRetorno++ = '|';
 //*libRetorno++ = 0x1B;


/*
 *    Retorna condigco normal
 */

 _Trace ( "<-- Formata-6 ( 0 )\n" );
 return ( 0 );

 }    /* Formata */


/* ========================================================================= *
 *                   LeCoordenadasBanco                                               *
 * ========================================================================= *
 *
 *    Msdulo .......
 *                   lib20fi.a
 *
 *    Sintaxe ......
 *                   int LeCoordenadasBanco ( Banco, Coordenadas )
 *
 *    Entrada ......
 *                   libForma
 *                      Descrigco: Codigo do banco            
 *                      Uso:       Leitura
 *
 *    Saida ........
 *                   As coordenadas dos campos do banco
 *
 *    Proposito ....
 *                   Ler as coordenadas do banco no arquivo bancos.txt   
 *
 *    Criado por ...
 *                   Rodrigo R. Olimpio
 *
 * ========================================================================= */

 /*LeCoordenadasBanco ( Banco, Coordenadas )
 char *Banco, *Coordenadas;
 {
    char LinhaArquivo[50];
    char BancoLido[4];
    FILE * idArquivo;
    
    memset( LinhaArquivo, '\0', 50 );
    memset( BancoLido,    '\0',  4 );
    
    if ( ( idArquivo = fopen("Bancos.txt", "r" )) == NULL )
	return 0;
    
    while (!EOF)
    {
	printf("\n Lendo o arquivo " );
	fgets( LinhaArquivo, 45, idArquivo );
	strncpy( BancoLido, &LinhaArquivo[0], 3 );
	if ( strcmp( Banco, BancoLido ) == 0 )
	   break;
    }
    
    strncpy( Coordenadas, &LinhaArquivo[3], 41 );
    printf ( "\nCoordenadas: %s", Coordenadas );
    fclose( idArquivo );
    return (1);
}
*/ 

        	/*===================================
		*     Comandos de Inicializacao
		*===================================*/

int Bematech_FI_AlteraSimboloMoeda(Moeda,Retorno)
 char *Moeda;
{
 char Var[3];
 sprintf(CMD,"\x1b|01|%s|\x1b",Moeda);
 return Formata(CMD,Var,Retorno);

}

int Bematech_FI_AdicionaAliquota(Ali,Tipo,Retorno)
 char *Ali,*Tipo;
{
 char Var[3];
 sprintf(CMD,"\x1b|07|%s|%s|\x1b",Ali,Tipo);
 return Formata(CMD,Var,Retorno);

}

int Bematech_FI_ProgramaHorarioVerao(Retorno)
{
 char Var[3];
 sprintf(CMD,"\x1b|18|\x1b");
 return Formata(CMD,Var,Retorno);

}

int Bematech_FI_NomeiaTotalizadorNaoSujeitoICMS(Indice, Descricao, Retorno)
 char *Indice,*Descricao;
{
 char Var[3];
 sprintf(CMD,"\x1b|40|%s|%s|\x1b",Indice,Descricao);
 //printf( "\r\nCMD: %s", CMD );
 return Formata(CMD,Var,Retorno);

}

int Bematech_FI_ProgramaArredondamento(Retorno)
{
 char Var[3];
 sprintf(CMD,"\x1b|39|1|\x1b");
 return Formata(CMD,Var,Retorno);
}

int Bematech_FI_ProgramaTruncamento(Retorno)
{
 char Var[3];
 sprintf(CMD,"\x1b|39|0|\x1b");
 return Formata(CMD,Var,Retorno);
}

int Bematech_FI_EspacoEntreLinhas(Dots,Retorno)
 int Dots;
{
 char Var[3];

 sprintf(CMD,"\x1b|60|%d|\x1b",Dots);

 return Formata(CMD,Var,Retorno);

}

int Bematech_FI_LinhasEntreCupons(Dots,Retorno)
 int Dots;
{
 char Var[3];

 sprintf(CMD,"\x1b|61|%d|\x1b",Dots);

 return Formata(CMD,Var,Retorno);

}

int Bematech_FI_NomeiaDepartamento(Indice,Descricao,Retorno)
 char *Indice, *Descricao;
{
 char Var[3];
 sprintf(CMD,"\x1b|65|%s|%s|\x1b",Indice, Descricao);
 return Formata(CMD,Var,Retorno);

}

int Bematech_FI_ResetaImpressora(Retorno)
{
 char Var[3];
 sprintf(CMD,"\x1b|70|\x1b");
 return Formata(CMD,Var,Retorno);

}

        	/*===================================
		*     Comandos do Cupom Fiscal
		*===================================*/


int Bematech_FI_CancelaItemAnterior(Retorno)
{
 char Var[3];
 sprintf( CMD,"\x1b|13|\x1b" );
 return Formata(CMD,Var,Retorno);

}

int Bematech_FI_CancelaItemGenerico(NUMERO,Retorno)
 char *NUMERO;
 int Retorno[3];
{
 char Var[3];
 sprintf(CMD,"\x1b|31|%s|\x1b",NUMERO);
 return Formata(CMD,Var,Retorno);

}

int Bematech_FI_CancelaCupom(Retorno)
 int Retorno[3];
{
 char Var[3];
 sprintf(CMD,"\x1b|14|\x1b");
 return Formata(CMD,Var,Retorno);

}


int Bematech_FI_AbreCupom(CGC,Retorno)
 char *CGC;
 int Retorno[3];
{
  char Var; 
  if(CGC=="")
      sprintf(CMD,"\x1b|0|\x1b");
  else
    { Verifica=1;sprintf(CMD,"\x1b|0|%s|\x1b",CGC);}
      
  Verifica=0;
  return Formata(CMD,Var,Retorno);
  
}

int Bematech_FI_VendeItem(CODIGO,DESCRICAO,ALIQUOTA,QUANTIDADE,VALOR,DESCONTO,Retorno)
 char *CODIGO,*DESCRICAO,*ALIQUOTA,*QUANTIDADE,*VALOR,*DESCONTO;
 int Retorno[3];
{
  char Var;
  sprintf(CMD,"\x1b|9|%s|%s|%s|%s|%s|%s|\x1b",CODIGO,DESCRICAO,ALIQUOTA,QUANTIDADE,VALOR,DESCONTO);

  return Formata(CMD,Var,Retorno);
}

int Bematech_FI_VendeItem3CasasValor(Codigo,Descricao,Aliquota,Quantidade,
                                     Valor,AcrescimoDesconto,Retorno)
 char *Codigo,*Descricao,*Aliquota,*Quantidade,*Valor,*AcrescimoDesconto;
{
  char Var[3];
  sprintf(CMD,"\x1b|56|%s|%s|%s|%s|%s|%s|\x1b",Codigo,Descricao,Aliquota,
          Quantidade,Valor,AcrescimoDesconto);

  return Formata(CMD,Var,Retorno);
}

int Bematech_FI_VendeItemDepartamento(Codigo,Descricao,Aliquota,Quantidade,
                                      Valor,Acrescimo,Desconto,Departamento,
				      UnidadeMedida,Retorno)
 char *Codigo,*Descricao,*Aliquota,*Quantidade,*Valor,*Acrescimo,*Desconto;
 char *Departamento, *UnidadeMedida;
{
  char Var[3];
  char Medida[3];
  
  memset( Medida, '\0', 3 );

  if ( UnidadeMedida == 0 || UnidadeMedida[0] == '\0' )
	sprintf( Medida, "  ");

  else
  {
	if ( strlen( UnidadeMedida ) == 1 )
	    sprintf( Medida, "%s ", UnidadeMedida );
	
	else
	    strcpy( Medida, UnidadeMedida ); 
  }
  sprintf(CMD,"\x1b|63|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|\x1b",Aliquota, Valor,
          Quantidade, Desconto, Acrescimo, Departamento,"00000000000000000000",
	  Medida, Codigo, Descricao );
  
//  printf("\n CMD: %s", CMD);
  return Formata(CMD,Var,Retorno);
}


int Bematech_FI_IniciaFechamentoCupom( AcrescimoDesconto, Valor, Retorno )
 char * AcrescimoDesconto, * Valor;
{
    char Var[3];
    
    sprintf(CMD,"%s%s|%s%s","\x1b|32|", AcrescimoDesconto, Valor, "|\x1b" );

    return Formata(CMD,Var,Retorno);        
} 

int Bematech_FI_VerificaFormaPagamento( FormaPagto, Var, Retorno )
 char *FormaPagto;
{
    sprintf(CMD,"%s%s","\x1b|71|", FormaPagto);
    sprintf(&CMD[21],"%s","|\x1b");

    return Formata(CMD,Var,Retorno);        
} 

int Bematech_FI_ProgramaFormasPagamento( FormasPagto, Retorno )
 char *FormasPagto;
{
    char Var[3];
    sprintf(CMD,"\x1b|73|%s|\x1b", FormasPagto);

    return Formata(CMD,Var,Retorno);        
} 

int Bematech_FI_EfetuaFormaPagamento( IndiceForma,ValorForma,DescOpcional,Retorno )
 char *IndiceForma, *ValorForma, *DescOpcional;
{
    char Var[3];
    sprintf(CMD,"%s%s|%s","\x1b|72|", IndiceForma, ValorForma);
    if ( DescOpcional[0] == 0 || DescOpcional == '\0' )
    {
         sprintf(&CMD[22],"%s","|\x1b");
    }
      
    else
    {
         sprintf(&CMD[22],"|%s", DescOpcional);
         sprintf(&CMD[22 + strlen( DescOpcional ) + 1],"%s","|\x1b");
    }

      return Formata(CMD,Var,Retorno);        
} 

int Bematech_FI_TerminaFechamentoCupom( Mensagem, Retorno )
 char *Mensagem;
{
    char Var[3];
    
    if( Mensagem == 0 || Mensagem[0] == '\0' )
	sprintf(CMD,"\x1b|34|\x1b" );
    else
	sprintf(CMD,"%s%s%s","\x1b|34|", Mensagem, "|\x1b" );
        
    return Formata(CMD,Var,Retorno);        
} 

int Bematech_FI_FechaCupomResumido(FORMA,VALOR,MSG,Retorno)
 char *FORMA,*MSG,*VALOR;
 int Retorno[3];
 
{
   char Var[15];
   char Indice[2];
   int  Ret=0;
  
   sprintf(CMD,"\x1b|32|D|0000|\x1b");
   Ret = Formata(CMD,Var,Retorno);
   
   //for(;Ret<1000;Ret);
   if ( Ret !=0 ) return Ret;
   
   sprintf(CMD,"\x1b|71|");
   strncat(CMD,FORMA,16);strcat(CMD,"|\x1b");
   Ret = Formata(CMD,Var,Retorno);
   if ( Ret != 0 ) return Ret;
 
   //for(;Ret<1000;Ret++);      
   //printf("%s",CMD);
   
   
//   Indice[0]=libDados[1];Indice[1]=libDados[2];
   Indice[0] = Var[0]; Indice[1] = Var[1];
   
   
   sprintf(CMD,"\x1b|72|%c%c|%s|\x1b",Indice[0],Indice[1],VALOR);
   Ret = Formata(CMD,Var,Retorno);
   if ( Ret != 0 ) return Ret;
	  
   
   sprintf(CMD,"\x1b|34|%s\x0D\x0A|\x1b",MSG);
   return Formata(CMD,Var,Retorno);
   
  
}

int Bematech_FI_FechaCupom(FLAG,DESC,QTD,FORMAS,VLR,MSG,Retorno)
 char *FLAG,*DESC,*FORMAS,*VLR,*MSG;
 int QTD;
{
  char Var[3];
  char Indice[10];
  int conta=0;
  int conta2=0;
  
  sprintf(CMD,"%s","\x1b|32|");
  strcat(CMD,FLAG);strcat(CMD,"|");
  strcat(CMD,DESC);strcat(CMD,"|\x1b");
  
  //printf("CMD = %s",CMD);
  //gets(Var);
  
  //printf("\r\n CMD: %s", CMD );
  Formata(CMD,Var,Retorno);
  
  for (;QTD != 0;QTD--) 
    { 
      sprintf(CMD,"%s","\x1b|71|");
      strncat(&CMD[5],&FORMAS[conta],16);
      sprintf(&CMD[21],"%s","|\x1b");
      conta=conta+17;

//gets(Var);
      //printf("\r\n CMD: %s", CMD );
      Formata(CMD,Var,Retorno);        

      /*printf("VAR=%c",libDados[0]);
      printf("VAR=%c",libDados[1]);
      printf("VAR=%c",libDados[2])*/
            
      
      sprintf(CMD,"%s","\x1b|72|");
      sprintf(&CMD[5],"%c%c|",libDados[1],libDados[2]);
      strncat(&CMD[8],&VLR[conta2],14);
      sprintf(&CMD[22],"%s","|\x1b");
      conta2=conta2+15;

      //printf("\r\nCMD= %s ",CMD);
      
      Formata(CMD,Var,Retorno);
    } // for

    sprintf(CMD,"\x1b|34|%s\x0D\x0A|\x1b",MSG);
    Formata(CMD,Var,Retorno);
    
  return 0;
 }

int Bematech_FI_ProgramaUnidadeMedida( UnidMedida, Retorno )
 char *UnidMedida;
{
    char Var[3];
    sprintf(CMD,"%s%s","\x1b|62|51|", UnidMedida);
    sprintf(&CMD[8 + strlen( UnidMedida )],"%s","|\x1b");
    //printf("\n CMD: %s", CMD);
    return Formata(CMD,Var,Retorno);        
} 

int Bematech_FI_AumentaDescricaoItem( DescricaoItem, Retorno )
 char *DescricaoItem;
{
    char Var[3];
    sprintf(CMD,"%s%s%s","\x1b|62|52|", DescricaoItem, "|\x1b");
    //printf("\n CMD: %s", CMD);
    return Formata(CMD,Var,Retorno);        
} 


        	/*===================================
		* Comandos de Relatorios Fiscais 
		*===================================*/
 


int Bematech_FI_ReducaoZ(Retorno)
 int Retorno[3];
{ char Var;
  return Formata("\x1b|05|\x1b", Var, Retorno);
 
}

int Bematech_FI_LeituraX(Retorno)
 int  Retorno[3];
 {
    char Var;
    return Formata ( "\x1b|06|\x1b" , Var , Retorno);      
 }

int Bematech_FI_LeituraMemoriaFiscalData(DataInicial,DataFinal,Retorno)
 char *DataInicial, *DataFinal;
{
  char Var[3];

  memset( CMD, '\0', 300 );

  sprintf(CMD, "%s", "\x1b|8|");
  strncpy(CMD + strlen( CMD ),&DataInicial[0], 2 );
  sprintf(CMD + strlen( CMD ), "%s","|");
  strncpy(CMD + strlen( CMD ),&DataInicial[2], 2 );
  sprintf(CMD + strlen( CMD ), "%s","|");
  strncpy(CMD + strlen( CMD ),&DataInicial[4], 2 );
  sprintf(CMD + strlen( CMD ), "%s","|");
  strncpy(CMD + strlen( CMD ),&DataFinal[0], 2 );
  sprintf(CMD + strlen( CMD ), "%s","|");
  strncpy(CMD + strlen( CMD ),&DataFinal[2], 2 );
  sprintf(CMD + strlen( CMD ), "%s","|");
  strncpy(CMD + strlen( CMD ),&DataFinal[4], 2 );
  sprintf(CMD + strlen( CMD ), "%s","|I|\x1b");

 // printf("\r\n CMD: %s", CMD );
  return Formata(CMD,Var,Retorno);
}

int Bematech_FI_LeituraMemoriaFiscalDataSerial(DataInicial,DataFinal,Retorno)
 char *DataInicial, *DataFinal;
{
  char Var[3];
  
  memset( CMD, '\0', 300 );
  
  sprintf(CMD,"%s", "\x1b|8|");
  strncpy(CMD + strlen( CMD ),&DataInicial[0], 2 );
  sprintf(CMD + strlen( CMD ), "%s", "|");
  strncpy(CMD + strlen( CMD ),&DataInicial[2], 2 );
  sprintf(CMD + strlen( CMD ), "%s","|");
  strncpy(CMD + strlen( CMD ),&DataInicial[4], 2 );
  sprintf(CMD + strlen( CMD ), "%s","|");
  strncpy(CMD + strlen( CMD ),&DataFinal[0], 2 );
  sprintf(CMD + strlen( CMD ), "%s","|");
  strncpy(CMD + strlen( CMD ),&DataFinal[2], 2 );
  sprintf(CMD + strlen( CMD ), "%s","|");
  strncpy(CMD + strlen( CMD ),&DataFinal[4], 2 );
  sprintf(CMD + strlen( CMD ), "%s","|R|\x1b");

//  printf("\r\n CMD: %s", CMD );
  return Formata(CMD,Var,Retorno);
}

int Bematech_FI_LeituraMemoriaFiscalReducao(ReducaoInicial,ReducaoFinal,Retorno)
 char *ReducaoInicial, *ReducaoFinal;
{ 
 char Var[3];
 sprintf(CMD,"\x1b|8|0|0|%s|0|0|%s|I|\x1b", ReducaoInicial, ReducaoFinal);
 return Formata(CMD,Var,Retorno);
}

int Bematech_FI_LeituraMemoriaFiscalReducaoSerial(ReducaoInicial,ReducaoFinal,Retorno)
 char *ReducaoInicial, *ReducaoFinal;
{ 
 char Var[3];
 sprintf(CMD,"\x1b|8|0|0|%s|0|0|%s|R|\x1b", ReducaoInicial, ReducaoFinal);
 return Formata(CMD,Var,Retorno);
}

int Bematech_FI_LeituraXSerial(Retorno)
{
  char Var[3];
  return Formata("\x1b|69|\x1b",Var,Retorno);
}

        	/*===================================
		* Comandos das Operacoes Nao Fiscais
		*===================================*/


int Bematech_FI_RelatorioGerencial(MSG,Retorno)
 char *MSG;
 int Retorno[3];
 {
   char Var[3];
   sprintf(CMD,"\x1b|20|%s\x0d\x0a|\x1b",MSG);
   return Formata(CMD,Var,Retorno);
 
 }

int Bematech_FI_FechaRelatorioGerencial(Retorno)
 int Retorno[3];
 {
  char Var[3];
  return Formata("\x1b|21|\x1b",Var,Retorno);
 }

int Bematech_FI_Sangria(Valor,Retorno)
 char *Valor;
 int Retorno[3];
{
  char Var[3];
  Verifica=1;
  sprintf(CMD,"\x1b|25|SA|%s|\x1b",Valor);
  sprintf(Dummy,"%s",Valor);
  Formata(CMD,Var,Retorno);
  Verifica=0;
  return 0; 
}

int Bematech_FI_Suprimento(Valor,Retorno)
 char *Valor;
 int Retorno[3];
{
  char Var[3];
  Verifica=1;
  sprintf(CMD,"\x1b|25|SU|%s|\x1b",Valor);
  sprintf(Dummy,"%s",Valor);
  //gets(Var);
  Formata(CMD,Var,Retorno);
  Verifica=0;
  return 0;
   
}

int Bematech_FI_RecebimentoNaoFiscal(Indice, Valor, FormaPagto, Retorno)
 char *Indice, *Valor, *FormaPagto;
{
  char Var[3];
  sprintf(CMD, "\x1b|25|%s|%s|%s|\x1b", Indice, Valor, FormaPagto);

  return Formata(CMD,Var,Retorno);
   
}

int Bematech_FI_AbreComprovanteNaoFiscalVinculado( FormaPagto, Valor, NumeroCupom, Retorno)
 char *FormaPagto, *Valor, *NumeroCupom;
{
  char Var[3];

  sprintf(CMD, "\x1b|66|%s", FormaPagto);

  if( Valor != 0 || Valor[0] != '\0' )
     sprintf(CMD + strlen( CMD ), "|%s", Valor);

  if( NumeroCupom != 0 || NumeroCupom[0] != '\0' )
     sprintf(CMD + strlen( CMD ), "|%s", NumeroCupom);      

  sprintf(CMD + strlen( CMD ), "|\x1b");      

  return Formata(CMD,Var,Retorno);
   
}

int Bematech_FI_UsaComprovanteNaoFiscalVinculado(Texto, Retorno)
 char *Texto;
{
  char Var[3];
  sprintf(CMD, "\x1b|67|%s|\x1b", Texto);

  return Formata(CMD,Var,Retorno);
   
}

int Bematech_FI_FechaComprovanteNaoFiscalVinculado(Retorno)
{
  char Var[3];
  return Formata("\x1b|21|\x1b",Var,Retorno);
}


        	/*===================================
		* Comandos de Autenticacao 
		*===================================*/


int Bematech_FI_Autenticacao(Retorno)
{
  char Var[3];

  return Formata("\x1b|16|\x1b",Var,Retorno);
}

int Bematech_FI_ProgramaCaracterAutenticacao(Valores, Retorno)
 char *Valores;
{
  char Var[3];
  sprintf(CMD, "\x1b|64|%s|\x1b",Valores);

 // printf("\r\nCMD: %s", CMD );
  return Formata(CMD,Var,Retorno);
}

 
        	/*===================================
		*   Comandos da Gaveta de Dinheiro
		*===================================*/


int Bematech_FI_AbreGaveta(Retorno)
{
  char Var[3];
  Formata("\x1b|22|\x1b",Var,Retorno);
}

int Bematech_FI_EstadoGaveta(Var,Retorno)
{
  Formata("\x1b|23|\x1b",Var,Retorno);
}

        	/*=======================================
		*  Comandos de Informacoes da impressora
		*======================================*/

int Bematech_FI_EstadoImpressora(Retorno)
{
   char * Var[3];    
   return  Formata("\x1b|19|\x1b",Var,Retorno);
}

int Bematech_FI_LeituraAliquotas(Var,Retorno)
 {
   return  Formata("\x1b|26|\x1b",Var,Retorno);
 }

int Bematech_FI_LeituraTotalizadoresParciais(Var,Retorno)
{
   return  Formata("\x1b|27|\x1b",Var,Retorno);
}

int Bematech_FI_SubTotal(Var,Retorno)
{
   return  Formata("\x1b|29|\x1b",Var,Retorno);
}

int Bematech_FI_NumeroCupom(Var,Retorno)
{ 
   return Formata("\x1b|30|\x1b",Var,Retorno);
} 

int Bematech_FI_NumeroSerie(Var,Retorno)
{ 
   return Formata("\x1b|35|00|\x1b",Var,Retorno);
} 

int Bematech_FI_VersaoFirmware(Var,Retorno)
{
  return Formata("\x1b|35|01|\x1b",Var,Retorno);
}

int Bematech_FI_CGCIE(Var,Retorno)
{ 
   return Formata("\x1b|35|02|\x1b",Var,Retorno);
} 

int Bematech_FI_GrandeTotal(Var,Retorno)
{
  return Formata("\x1b|35|03|\x1b",Var,Retorno);
}

int Bematech_FI_Cancelamentos(Var,Retorno)
{
  return Formata("\x1b|35|04|\x1b",Var,Retorno);
}

int Bematech_FI_Descontos(Var,Retorno)
{
  return Formata("\x1b|35|05|\x1b",Var,Retorno);
}

int Bematech_FI_ContadorSequencial(Var,Retorno)
{
  return Formata("\x1b|35|06|\x1b",Var,Retorno);
}

int Bematech_FI_NumeroOperacoesNaoFiscais(Var,Retorno)
{
  return Formata("\x1b|35|07|\x1b",Var,Retorno);
}

int Bematech_FI_NumeroCuponsCancelados(Var,Retorno)
{
  return Formata("\x1b|35|08|\x1b",Var,Retorno);
}

int Bematech_FI_NumeroReducoes(Var,Retorno)
{
  return Formata("\x1b|35|09|\x1b",Var,Retorno);
}

int Bematech_FI_NumeroIntervencoesTecnicas(Var,Retorno)
{
  return Formata("\x1b|35|10|\x1b",Var,Retorno);
}

int Bematech_FI_NumeroSubstituicoesProprietario(Var,Retorno)
{
  return Formata("\x1b|35|11|\x1b",Var,Retorno);
}

int Bematech_FI_UltimoItemVendido(Var,Retorno)
{
  return Formata("\x1b|35|12|\x1b",Var,Retorno);
}

int Bematech_FI_ClicheProprietario(Var,Retorno)
{
  return Formata("\x1b|35|13|\x1b",Var,Retorno);
}

int Bematech_FI_NumeroCaixa(Var,Retorno)
{
  return Formata("\x1b|35|14|\x1b",Var,Retorno);
}

int Bematech_FI_NumeroLoja(Var,Retorno)
{
  return Formata("\x1b|35|15|\x1b",Var,Retorno);
}

int Bematech_FI_Moeda(Var,Retorno)
{
  return Formata("\x1b|35|16|\x1b",Var,Retorno);
}

int Bematech_FI_FlagsFiscais(Var,Retorno)
{
 int cont=0;
 
 return Formata("\x1b|35|17|\x1b",Var,Retorno);
}

int Bematech_FI_MinutosLigada(Var,Retorno)
{
  return Formata("\x1b|35|18|\x1b",Var,Retorno);
}

int Bematech_FI_MinutosImprimindo(Var,Retorno)
{
  return Formata("\x1b|35|19|\x1b",Var,Retorno);
}

int Bematech_FI_FlagIntervencaoTecnica(Var,Retorno)
{
  return Formata("\x1b|35|20|\x1b",Var,Retorno);
}

int Bematech_FI_FlagEpromConectada(Var,Retorno)
{
  return Formata("\x1b|35|21|\x1b",Var,Retorno);
}

int Bematech_FI_ValorPagoUltimoCupom(Var,Retorno)
{
  return Formata("\x1b|35|22|\x1b",Var,Retorno);
}

int Bematech_FI_DataHora(Var,Retorno)
{
  return Formata("\x1b|35|23|\x1b",Var,Retorno);
}

int Bematech_FI_ContadoresTotalizadoresNaoFiscais(Var,Retorno)
{
  return Formata("\x1b|35|24|\x1b",Var,Retorno);
}

int Bematech_FI_DescricaoTotalizadoresNaoFiscais(Var,Retorno)
{
  return Formata("\x1b|35|25|\x1b",Var,Retorno);
}

int Bematech_FI_DataUltimaReducao(Var,Retorno)
{
  return Formata("\x1b|35|26|\x1b",Var,Retorno);
}

int Bematech_FI_DataMovimento(Var,Retorno)
{
  return Formata("\x1b|35|27|\x1b",Var,Retorno);
}

int Bematech_FI_VerificaTruncamento(Var,Retorno)
{
  return Formata("\x1b|35|28|\x1b",Var,Retorno);
}

int Bematech_FI_FlagsISS(Var,Retorno)
{
  return Formata("\x1b|35|29|\x1b",Var,Retorno);
  
}

int Bematech_FI_Acrescimos(Var,Retorno)
{
  return Formata("\x1b|35|30|\x1b",Var,Retorno);
}

int Bematech_FI_LeituraFormasPagamento(Var,Retorno)
{
  return Formata("\x1b|35|32|\x1b",Var,Retorno);
}

int Bematech_FI_LeituraRecebimentosNaoFiscais(Var,Retorno)
{
  return Formata("\x1b|35|33|\x1b",Var,Retorno);
}

int Bematech_FI_LeituraDepartamentos(Var,Retorno)
{
  return Formata("\x1b|35|34|\x1b",Var,Retorno);
}

int Bematech_FI_TipoImpressora(Var,Retorno)
{
  return Formata("\x1b|35|253|\x1b",Var,Retorno);
}

int Bematech_FI_ContadorBilhetePassagem(Var,Retorno)
{
  return Formata("\x1b|35|31|\x1b",Var,Retorno);
}

int Bematech_FI_MonitoramentoPapel(Var,Retorno)
{
   return  Formata("\x1b|62|54|\x1b",Var,Retorno);
}

int Bematech_FI_DadosUltimaReducao(Var,Retorno)
{
 return Formata("\x1b|62|55|\x1b",Var,Retorno);
}

        	/*=======================================
		*    Comandos de Impressao de Cheque      
		*======================================*/

int Bematech_FI_ProgramaMoedaSingular(MoedaSingular,Retorno)
 char *MoedaSingular;
{
  char Var[3];
  sprintf(CMD, "\x1b|58|%s|\x1b",MoedaSingular);

  return Formata(CMD, Var, Retorno);
}

int Bematech_FI_ProgramaMoedaPlural(MoedaPlural,Retorno)
 char *MoedaPlural;
{
  char Var[3];  
  sprintf(CMD, "\x1b|59|%s|\x1b",MoedaPlural);

  return Formata(CMD, Var, Retorno);
}

int Bematech_FI_StatusCheque(Var,Retorno)
{
  return Formata("\x1b|62|48|\x1b",Var, Retorno);
}

int Bematech_FI_CancelaImpressaoCheque(Retorno)
{
  char Var[3];  
  return Formata("\x1b|62|49|\x1b",Var, Retorno);
}

int Bematech_FI_ImprimeCheque(Codigo,Valor,Favorecido,Localidade,Dia,Mes,Ano,Msg,Retorno)
 char *Codigo,*Valor,*Favorecido,*Localidade,*Dia,*Mes,*Ano,*Msg;
{
  char Var[3];
  char Coordenadas[50];
  char LinhaArquivo[50];
  char BancoLido[4];
  FILE * idArquivo;
    
  memset( LinhaArquivo, '\0', 50 );
  memset( BancoLido,    '\0',  4 );
  memset( Coordenadas,  '\0', 50 );

  if ( ( idArquivo = fopen("bancos.txt", "r" )) == NULL )
  {
    printf("\n Erro na abertura do arquivo bancos.txt" );
    return 14; // erro ao abrir o arquivo bancos.txt
  }
    
  while ( !feof( idArquivo ))
  {
     fgets( LinhaArquivo, 45, idArquivo );
     strncpy( BancoLido, &LinhaArquivo[0], 3 );
     if ( strcmp( Codigo, BancoLido ) == 0 )
	   break;
  }
    
  strncpy( Coordenadas, &LinhaArquivo[5], 38 );
  fclose( idArquivo );
  
  if ( Msg[0] == 0 || Msg == '\0' ) // nao tem mensagem
  {
     sprintf(CMD,"\x1b|57|%s|%s|%s|%s|%s|%s|%s|\x1b",
          Valor,Favorecido,Localidade,Dia,Mes,Ano,Coordenadas);     
  }
      
  else // concatena mensagem
  {
     sprintf(CMD,"\x1b|57|%s|%s|%s|%s|%s|%s|%s|%s|\x1b",
          Valor,Favorecido,Localidade,Dia,Mes,Ano,Coordenadas,Msg);
  }

  //printf( "\n CMD : %s", CMD );	  
  return Formata(CMD,Var,Retorno);
  //return 1;
}

int Bematech_FI_ImprimeChequeBR400(Modo,Codigo,Valor,Favorecido,Localidade,Dia,Mes,Ano,Msg,Retorno)
 char *Modo, *Codigo,*Valor,*Favorecido,*Localidade,*Dia,*Mes,*Ano,*Msg;
{
  char Var[3];
  char Coordenadas[50];
  char LinhaArquivo[50];
  char BancoLido[4];
  FILE * idArquivo;
    
  memset( LinhaArquivo, '\0', 50 );
  memset( BancoLido,    '\0',  4 );
  memset( Coordenadas,  '\0', 50 );

  if ( ( idArquivo = fopen("bancos.txt", "r" )) == NULL )
  {
    printf("\n Erro na abertura do arquivo bancos.txt" );
    return 14; // erro ao abrir o arquivo bancos.txt
  }
    
  while ( !feof( idArquivo ))
  {
     fgets( LinhaArquivo, 45, idArquivo );
     strncpy( BancoLido, &LinhaArquivo[0], 3 );
     if ( strcmp( Codigo, BancoLido ) == 0 )
	   break;
  }
    
  strncpy( Coordenadas, &LinhaArquivo[5], 38 );
  fclose( idArquivo );
  
  if ( Msg[0] == 0 || Msg == '\0' ) // nao tem mensagem
  {
     sprintf(CMD,"\x1b|57|%s|%s|%s|%s|%s|%s|%s|%s|\x1b",
          Modo,Valor,Favorecido,Localidade,Dia,Mes,Ano,Coordenadas);     
  }
      
  else // concatena mensagem
  {
     sprintf(CMD,"\x1b|57|%s|%s|%s|%s|%s|%s|%s|%s|%s|\x1b",
          Modo,Valor,Favorecido,Localidade,Dia,Mes,Ano,Coordenadas,Msg);
  }

  //printf( "\n CMD : %s", CMD );	  
  return Formata(CMD,Var,Retorno);
  //return 1;
}

int Bematech_FI_LeituraCheque(Var, Retorno)
{
  return Formata("\x1b|76|\x1b",Var, Retorno);
}
