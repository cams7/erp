
package org.freedom.ecf.test;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.freedom.ecf.driver.ECFBematech;
import org.freedom.ecf.driver.STResult;

public class ECFBematechTest extends TestCase {

	public ECFBematechTest( String name ) {

		super( name );
	}

	public void testComandosDeInicializacao() {

		ECFBematech ecf = new ECFBematech( "COM1" );
		assertTrue( trataresultFuncao( ecf.alteraSimboloMoeda( "R" ) ) );
		assertTrue( trataresultFuncao( ecf.adicaoDeAliquotaTriburaria( "0001", ECFBematech.ICMS ) ) );
		assertTrue( trataresultFuncao( ecf.programaHorarioVerao() ) );
		assertTrue( trataresultFuncao( ecf.nomeiaTotalizadorNaoSujeitoICMS( 4, "Totalizador teste" ) ) );
		assertTrue( trataresultFuncao( ecf.programaTruncamentoArredondamento( '1' ) ) );
		assertTrue( trataresultFuncao( ecf.programarEspacoEntreLinhas( 8 ) ) );
		assertTrue( trataresultFuncao( ecf.programarLinhasEntreCupons( 5 ) ) );
		assertTrue( trataresultFuncao( ecf.nomeiaDepartamento( 2, "Teste" ) ) );
	}

	public void testComandosDeCupomFiscal() {

		ECFBematech ecf = new ECFBematech( "COM1" );
		System.out.print( "aberturaDeCupom > " );
		assertTrue( trataresultFuncao( ecf.aberturaDeCupom() ) );
		System.out.print( "aberturaDeCupom String > " );
		assertTrue( trataresultFuncao( ecf.aberturaDeCupom( "00.000.000/0000-00 CNPJ e nome do Cliente" ) ) );
		System.out.print( "programaUnidadeMedida > " );
		assertTrue( trataresultFuncao( ecf.programaUnidadeMedida( "Kg" ) ) );
		System.out.print( "vendaItem > " );
		assertTrue( trataresultFuncao( ecf.vendaItem( "0000000000001", "Produto Teste                ", "FF", 'I', 1f, 10f, 'D', 0f ) ) );
		System.out.print( "cancelaItemAnterior > " );
		assertTrue( trataresultFuncao( ecf.cancelaItemAnterior() ) );
		System.out.print( "aumentaDescItem > " );
		assertTrue( trataresultFuncao( ecf.aumentaDescItem( "Descricao do item aumentada para 60 caracteres" ) ) );
		System.out.print( "vendaItemTresCasas > " );
		assertTrue( trataresultFuncao( ecf.vendaItemTresCasas( "1234567890002", "Produto Teste                ", "FF", 'I', 2f, 2.050f, 'D', 0.10f ) ) );
		System.out.print( "vendaItemDepartamento > " );
		assertTrue( trataresultFuncao( ecf.vendaItemDepartamento( "FF", 1f, 10f, 0.50f, 0.50f, 2, "Kg", "1234567890003", "Descricao do produto" ) ) );
		System.out.print( "cancelaItemGenerico 2 > " );
		assertTrue( trataresultFuncao( ecf.cancelaItemGenerico( 2 ) ) );
		System.out.print( "iniciaFechamentoCupom > " );
		assertTrue( trataresultFuncao( ecf.iniciaFechamentoCupom( ECFBematech.ACRECIMO_VALOR, 0.0f ) ) );
		System.out.print( "efetuaFormaPagamento Dinheiro > " );
		assertTrue( trataresultFuncao( ecf.efetuaFormaPagamento( "01", 5.00f, null ) ) );
		System.out.print( "programaFormaPagamento > " );
		String f2 = ecf.programaFormaPagamento( "Cheque          " );
		System.out.println( f2 );
		assertTrue( !"".equals( f2 ) );
		System.out.print( "efetuaFormaPagamento Cheque > " );
		assertTrue( trataresultFuncao( ecf.efetuaFormaPagamento( f2, 5.50f, "Cheque          " ) ) );
		System.out.print( "estornoFormaPagamento > " );
		assertTrue( trataresultFuncao( ecf.estornoFormaPagamento( "Cheque          ", "Dinheiro", 5.50f ) ) );
		System.out.print( "finalizaFechamentoCupom > " );
		assertTrue( trataresultFuncao( ecf.finalizaFechamentoCupom( "Obrigado e volte sempre pra testar!" ) ) );
		// System.out.print( "cancelaCupom > " );
		// assertTrue( trataresultFuncao( ecf.cancelaCupom() ) );
	}

	public void testComandosDeOperacoesNaoFiscais() {

		ECFBematech ecf = new ECFBematech( "COM1" );
		System.out.print( "relatorioGerencial > " );
		assertTrue( trataresultFuncao( ecf.relatorioGerencial( "Abrindo Relatorio Gerencial" ) ) );
		System.out.print( "relatorioGerencial usando > " );
		assertTrue( trataresultFuncao( ecf.relatorioGerencial( "Utilizando Relatorio Gerencial" ) ) );
		System.out.print( "fechamentoRelatorioGerencial > " );
		assertTrue( trataresultFuncao( ecf.fechamentoRelatorioGerencial() ) );
		System.out.print( "comprovanteNFiscalNVinculado suprimento > " );
		assertTrue( trataresultFuncao( ecf.comprovanteNFiscalNVinculado( ECFBematech.SUPRIMENTO, 50f, "Dinheiro        " ) ) );
		System.out.print( "comprovanteNFiscalNVinculado sangria > " );
		assertTrue( trataresultFuncao( ecf.comprovanteNFiscalNVinculado( ECFBematech.SANGRIA, 45f, "Dinheiro        " ) ) );
		System.out.print( "comprovanteNFiscalNVinculado não ICMS > " );
		assertTrue( trataresultFuncao( ecf.comprovanteNFiscalNVinculado( "02", 35f, "Dinheiro        " ) ) );
		testComandosDeCupomFiscal();
		System.out.print( "abreComprovanteNFiscalVinculado > " );
		assertTrue( trataresultFuncao( ecf.abreComprovanteNFiscalVinculado( "Cheque          ", 5.5f, Integer.parseInt( ecf.resultNumeroCupom() ) ) ) );
		System.out.print( "usaComprovanteNFiscalVinculado > " );
		assertTrue( trataresultFuncao( ecf.usaComprovanteNFiscalVinculado( "Usando o Comprovante Nao fiscal Vinculado" ) ) );
		System.out.print( "fechamentoRelatorioGerencial > " );
		assertTrue( trataresultFuncao( ecf.fechamentoRelatorioGerencial() ) );
	}

	public void testComandosDeAutenticacao() {

		ECFBematech ecf = new ECFBematech( "COM1" );
		System.out.print( "programaCaracterParaAutenticacao > " );
		// int [] sesc = {143,137,137,249,0,255,137,137,137,0,143,137,137,249,0,255,129,129 };
		int[] sesc = { 1, 2, 4, 8, 16, 32, 64, 128, 64, 32, 16, 8, 4, 2, 1, 129, 129, 129 };
		assertTrue( trataresultFuncao( ecf.programaCaracterParaAutenticacao( sesc ) ) );
		testComandosDeCupomFiscal();
		System.out.print( "autenticacaoDeDocumento > " );
		assertTrue( trataresultFuncao( ecf.autenticacaoDeDocumento() ) );
	}

	public void testComandosDeRelatoriosFiscais() {

		ECFBematech ecf = new ECFBematech( "COM1" );
		System.out.print( "leituraX > " );
		assertTrue( trataresultFuncao( ecf.leituraX() ) );
		System.out.print( "leituraXSerial > " );
		assertTrue( trataresultFuncao( ecf.leituraXSerial() ) );
		System.out.println( new String( ecf.getBytesLidos() ) );
		Calendar cal = Calendar.getInstance();
		Date hoje = cal.getTime();
		cal.set( Calendar.MONTH, cal.get( Calendar.MONTH ) - 1 );
		Date antes = cal.getTime();
		System.out.print( "leituraMemoriaFiscal data > " );
		assertTrue( trataresultFuncao( ecf.leituraMemoriaFiscal( antes, hoje, ECFBematech.IMPRESSAO ) ) );
		System.out.println( new String( ecf.getBytesLidos() ) );
		System.out.print( "leituraMemoriaFiscal data result > " );
		assertTrue( trataresultFuncao( ecf.leituraMemoriaFiscal( antes, hoje, ECFBematech.result ) ) );
		System.out.println( new String( ecf.getBytesLidos() ) );
		System.out.print( "leituraMemoriaFiscal redução > " );
		assertTrue( trataresultFuncao( ecf.leituraMemoriaFiscal( 605, 610, ECFBematech.IMPRESSAO ) ) );
		System.out.println( new String( ecf.getBytesLidos() ) );
		System.out.print( "leituraMemoriaFiscal redução result > " );
		assertTrue( trataresultFuncao( ecf.leituraMemoriaFiscal( 605, 610, ECFBematech.result ) ) );
		System.out.println( new String( ecf.getBytesLidos() ) );
		System.out.print( "reducaoZ > " );
		assertTrue( trataresultFuncao( ecf.reducaoZ() ) );
	}

	public void testComandosDeInformacoesDaImpressora() {

		ECFBematech ecf = new ECFBematech( "COM1" );
		System.out.print( "leitura do estado > " );
		System.out.println( ecf.getStatus() );
		System.out.print( "result de aliquotas > " );
		System.out.println( ecf.resultAliquotas() );
		System.out.print( "result de totalizadores parcias > " );
		System.out.println( ecf.resultTotalizadoresParciais() );
		System.out.print( "result de número do cupom > " );
		System.out.println( ecf.resultNumeroCupom() );
		System.out.println( "result de variáveis > " );
		System.out.println( "\tGrande total > " + ecf.resultVariaveis( ECFBematech.V_GRANDE_TOTAL ) );
		System.out.println( "\tFlags Fiscais > " + ecf.resultVariaveis( ECFBematech.V_FLAG_FISCAL ) );
		System.out.print( "result do estado do papel > " );
		System.out.println( ecf.resultEstadoPapel() );
		System.out.print( "result da ultima redução Z > " );
		System.out.println( ecf.resultUltimaReducao() );
		System.out.print( "sub total > > " );
		System.out.println( ecf.resultSubTotal() );
	}

	public void testComandosDeImpressaoDeCheques() {

		ECFBematech ecf = new ECFBematech( "COM1" );
		/*
		 * System.out.print( "programaMoedaSingular > " ); assertTrue( trataresultFuncao( ecf.programaMoedaSingular( "Real" ) ) );
		 * 
		 * System.out.print( "programaMoedaPlural > " ); assertTrue( trataresultFuncao( ecf.programaMoedaPlural( "Reais" ) ) );
		 */
		System.out.print( "imprimeCheque > " );
		System.out.println( ecf.imprimeCheque( 1.30f, "Favorecido                                   ", "Localidade                 ", 19, 11, 2007 ) );
		System.out.print( "resultStatusCheque > " );
		System.out.println( ecf.resultStatusCheque() );
	}

	public boolean trataresultFuncao( final STResult result ) {

		boolean bresult = false;
		String sMensagem = "";
		int iresult = result.getFirstCode();
		switch ( iresult ) {
		case -1:
			sMensagem = "Erro na envocação do metodo!";
			break;
		case 0:
			sMensagem = "Erro de comunicação física";
			break;
		case 1:
			sMensagem = "";
			break;
		case -2:
			sMensagem = "Parâmetro inválido na função.";
			break;
		case -3:
			sMensagem = "Aliquota não programada";
			break;
		case -4:
			sMensagem = "O arquivo de inicialização BEMAFI32.INI não foi encontrado no diretório de sistema do Windows";
			break;
		case -5:
			sMensagem = "Erro ao abrir a porta de comunicação";
			break;
		case -8:
			sMensagem = "Erro ao criar ou gravar no arquivo STATUS.TXT ou result.TXT";
			break;
		case -27:
			sMensagem = "Status da impressora diferente de 6,0,0 (ACK, ST1 e ST2)";
			break;
		case -30:
			sMensagem = "Função não compatível com a impressora YANCO";
			break;
		case -31:
			sMensagem = "Forma de pagamento não finalizada";
			break;
		default:
			sMensagem = "result indefinido: " + iresult;
			break;
		}
		if ( "".equals( sMensagem.trim() ) ) {
			bresult = true;
			sMensagem = "COMANDO EXECUTADO";
		}
		else {
			sMensagem = "[" + iresult + "] " + sMensagem;
		}
		System.out.println( sMensagem );
		return bresult;
	}
}
