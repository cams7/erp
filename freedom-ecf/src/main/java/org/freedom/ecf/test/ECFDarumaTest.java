
package org.freedom.ecf.test;

import junit.framework.TestCase;
import org.freedom.ecf.driver.AbstractECFDriver;
import org.freedom.ecf.driver.ECFDaruma;
import org.freedom.ecf.driver.STResult;

public class ECFDarumaTest extends TestCase {

	public ECFDarumaTest( String name ) {

		super( name );
	}

	public void testComandosDeInicializacao() {

	/*
	 * ECFDaruma ecf = new ECFDaruma( "COM1" );
	 * 
	 * assertTrue( trataresultFuncao( ecf, ecf.alteraSimboloMoeda( "R" ) ) );
	 * 
	 * assertTrue( trataresultFuncao( ecf, ecf.adicaoDeAliquotaTriburaria( "1200", ECFDaruma.ICMS ) ) );
	 * 
	 * assertTrue( trataresultFuncao( ecf, ecf.programaHorarioVerao() ) );
	 * 
	 * assertTrue( trataresultFuncao( ecf, ecf.nomeiaTotalizadorNaoSujeitoICMS( 4, "Totalizador teste" ) ) );
	 * 
	 * assertTrue( trataresultFuncao( ecf, ecf.programaTruncamentoArredondamento( '1' ) ) );
	 * 
	 * assertTrue( trataresultFuncao( ecf, ecf.programarEspacoEntreLinhas( 8 ) ) );
	 * 
	 * assertTrue( trataresultFuncao( ecf, ecf.programarLinhasEntreCupons( 5 ) ) );
	 * 
	 * assertTrue( trataresultFuncao( ecf, ecf.nomeiaDepartamento( 2, "Teste" ) ) );
	 */
	}

	public void testCancelaCupom() {

		ECFDaruma ecf = new ECFDaruma( "COM1" );
		System.out.print( "cancelamento de Cupom > " );
		assertTrue( trataresultFuncao( ecf, ecf.cancelaCupom() ) );
	}

	public void testComandosDeCupomFiscal() {

		ECFDaruma ecf = new ECFDaruma( "COM1" );
		System.out.print( "aberturaDeCupom > " );
		assertTrue( trataresultFuncao( ecf, ecf.aberturaDeCupom() ) );
		/*
		 * System.out.print( "aberturaDeCupom String > " ); assertTrue( trataresultFuncao( ecf, ecf.aberturaDeCupom( "00.000.000/0000-00         " + "Nome do Cliente                           " + "Endereço do Cliente nº99" ) ) );
		 */
		System.out.print( "vendaItem > " );
		assertTrue( trataresultFuncao( ecf, ecf.vendaItem( "0000000000001", "Produto Teste                ", "FF", 'I', 1f, 10f, 'D', 0f ) ) );
		/*
		 * System.out.print( "subtotal > " ); System.out.println( ecf.resultSubTotal() );
		 * 
		 * System.out.print( "programaUnidadeMedida > " ); assertTrue( trataresultFuncao( ecf, ecf.programaUnidadeMedida( "Kg" ) ) );
		 * 
		 * System.out.print( "vendaItem > " ); assertTrue( trataresultFuncao( ecf, ecf.vendaItem( "0000000000001", "Produto Teste                ", "FF", 'I', 1f, 10f, 'D', 0f ) ) );
		 * 
		 * System.out.print( "cancelaItemAnterior > " ); assertTrue( trataresultFuncao( ecf, ecf.cancelaItemAnterior() ) );
		 * 
		 * System.out.print( "aumentaDescItem > " ); assertTrue( trataresultFuncao( ecf, ecf.aumentaDescItem( "Descricao do item aumentada para 60 caracteres" ) ) );
		 * 
		 * System.out.print( "vendaItemTresCasas > " ); assertTrue( trataresultFuncao( ecf, ecf.vendaItemTresCasas( "1234567890002", "Produto Teste                ", "FF", 'I', 2f, 2.050f, 'D', 0.10f ) ) );
		 * 
		 * System.out.print( "vendaItemDepartamento > " ); assertTrue( trataresultFuncao( ecf, ecf.vendaItemDepartamento( "FF", 1f, 10f, 0.50f, 0.50f, 2, "Kg", "1234567890003", "Descricao do produto" ) ) );
		 * 
		 * System.out.print( "cancelaItemGenerico 2 > " ); assertTrue( trataresultFuncao( ecf, ecf.cancelaItemGenerico( 2 ) ) );
		 */
		System.out.print( "iniciaFechamentoCupom > " );
		assertTrue( trataresultFuncao( ecf, ecf.iniciaFechamentoCupom( ECFDaruma.ACRECIMO_VALOR, 0.00f ) ) );
		/*
		 * System.out.print( "efetuaFormaPagamento Dinheiro > " ); assertTrue( trataresultFuncao( ecf, ecf.efetuaFormaPagamento( "01", 10.00f, null ) ) );
		 */
		System.out.print( "programaFormaPagamento > " );
		String f2 = ecf.programaFormaPagamento( "Cheque          " );
		System.out.println( f2 );
		assertTrue( !"".equals( f2 ) );
		System.out.print( "efetuaFormaPagamento Cheque > " );
		assertTrue( trataresultFuncao( ecf, ecf.efetuaFormaPagamento( f2, 10f, "Cheque" ) ) );
		/*
		 * System.out.print( "estornoFormaPagamento > " ); assertTrue( trataresultFuncao( ecf, ecf.estornoFormaPagamento( "Cheque          ", "Dinheiro", 5.50f ) ) );
		 */
		System.out.print( "finalizaFechamentoCupom > " );
		assertTrue( trataresultFuncao( ecf, ecf.finalizaFechamentoCupom( "Obrigado e volte sempre pra testar!" ) ) );
		// System.out.print( "cancelaCupom > " );
		// assertTrue( trataresultFuncao( ecf.cancelaCupom() ) );
	}

	public void testComandosDeOperacoesNaoFiscais() {

		ECFDaruma ecf = new ECFDaruma( "COM1" );
		/*
		 * System.out.print( "relatorioGerencial > " ); assertTrue( trataresultFuncao( ecf, ecf.relatorioGerencial( "Abrindo Relatorio Gerencial                   \n" ) ) );
		 * 
		 * System.out.print( "relatorioGerencial usando > " ); assertTrue( trataresultFuncao( ecf, ecf.relatorioGerencial( "Utilizando Relatorio Gerencial                \n" ) ) );
		 * 
		 * System.out.print( "fechamentoRelatorioGerencial > " ); assertTrue( trataresultFuncao( ecf, ecf.fechamentoRelatorioGerencial() ) );
		 */
		System.out.print( "comprovanteNFiscalNVinculado suprimento > " );
		assertTrue( trataresultFuncao( ecf, ecf.comprovanteNFiscalNVinculado( ECFDaruma.SUPRIMENTO, 50f, "Dinheiro        " ) ) );
		System.out.print( "comprovanteNFiscalNVinculado sangria > " );
		assertTrue( trataresultFuncao( ecf, ecf.comprovanteNFiscalNVinculado( ECFDaruma.SANGRIA, 45f, "Dinheiro        " ) ) );
		System.out.print( "comprovanteNFiscalNVinculado não ICMS > " );
		assertTrue( trataresultFuncao( ecf, ecf.comprovanteNFiscalNVinculado( "03", 35f, "Dinheiro        " ) ) );
		/*
		 * testComandosDeCupomFiscal(); System.out.print( "abreComprovanteNFiscalVinculado > " ); assertTrue( trataresultFuncao( ecf, ecf.abreComprovanteNFiscalVinculado( "Cheque          ", 10f, Integer.parseInt( ecf.resultNumeroCupom() ) ) ) );
		 * 
		 * System.out.print( "usaComprovanteNFiscalVinculado > " ); assertTrue( trataresultFuncao( ecf, ecf.usaComprovanteNFiscalVinculado( "Comprovante \nTeste\nteste\nteste..." ) ) );
		 * 
		 * System.out.print( "fechamentoRelatorioGerencial > " ); assertTrue( trataresultFuncao( ecf, ecf.fechamentoRelatorioGerencial() ) );
		 */
	}

	public void testComandosDeAutenticacao() {

		// ECFDaruma ecf = new ECFDaruma( "COM1" );
		System.out.print( "programaCaracterParaAutenticacao > " );
		/*
		 * int [] sesc = {143,137,137,249,0,255,137,137,137,0,143,137,137,249,0,255,129,129 }; int [] sesc = {1,2,4,8,16,32,64,128,64,32,16,8,4,2,1,129,129,129 }; assertTrue( trataresultFuncao( ecf.programaCaracterParaAutenticacao( sesc ) ) );
		 * 
		 * testComandosDeCupomFiscal(); System.out.print( "autenticacaoDeDocumento > " ); assertTrue( trataresultFuncao( ecf.autenticacaoDeDocumento() ) );
		 */
	}

	public void testComandosDeRelatoriosFiscais() {

		ECFDaruma ecf = new ECFDaruma( "COM1" );
		// System.out.print( "leituraX > " );
		assertTrue( trataresultFuncao( ecf, ecf.leituraX() ) );
		/*
		 * System.out.print( "leituraXSerial > " ); assertTrue( trataresultFuncao( ecf.leituraXSerial() ) ); System.out.println( new String( ecf.getBytesLidos() ) );
		 * 
		 * Calendar cal = Calendar.getInstance(); Date hoje = cal.getTime(); cal.set( Calendar.MONTH, cal.get( Calendar.MONTH ) - 1 ); Date antes = cal.getTime();
		 * 
		 * //System.out.print( "leituraMemoriaFiscal data > " ); //assertTrue( trataresultFuncao( ecf, ecf.leituraMemoriaFiscal( antes, hoje, ECFDaruma.IMPRESSAO ) ) ); //System.out.println( new String( ecf.getBytesLidos() ) );
		 * 
		 * System.out.print( "leituraMemoriaFiscal data result > " ); assertTrue( trataresultFuncao( ecf, ecf.leituraMemoriaFiscal( antes, hoje, ECFDaruma.result ) ) ); System.out.println( new String( ecf.getBytesLidos() ) );
		 * 
		 * System.out.print( "leituraMemoriaFiscal redução > " ); assertTrue( trataresultFuncao( ecf, ecf.leituraMemoriaFiscal( 605, 610, ECFDaruma.IMPRESSAO ) ) ); System.out.println( new String( ecf.getBytesLidos() ) );
		 * 
		 * System.out.print( "leituraMemoriaFiscal redução result > " ); assertTrue( trataresultFuncao( ecf, ecf.leituraMemoriaFiscal( 605, 610, ECFDaruma.result ) ) ); System.out.println( new String( ecf.getBytesLidos() ) );
		 */
		System.out.print( "reducaoZ > " );
		assertTrue( trataresultFuncao( ecf, ecf.reducaoZ() ) );
		System.out.print( "leituraX > " );
		assertTrue( trataresultFuncao( ecf, ecf.leituraX() ) );
	}

	public void testComandosDeInformacoesDaImpressora() {

		ECFDaruma ecf = new ECFDaruma( "COM1" );
		// System.out.print( "leitura do estado > " );
		// System.out.println( ecf.getStatus() );
		// System.out.print( "result de aliquotas > " );
		// System.out.println( ecf.resultAliquotas() );
		System.out.print( "result de totalizadores parcias > " );
		System.out.println( ecf.resultTotalizadoresParciais() );
		// System.out.print( "result de número do cupom > " );
		// System.out.println( ecf.resultNumeroCupom() );
		// System.out.println( "result de variáveis > " );
		// System.out.println( "\tGrande total > " + ecf.resultVariaveis( ECFDaruma.V_GRANDE_TOTAL ) );
		// System.out.println( "\tFlags Fiscais > " + ecf.resultVariaveis( ECFDaruma.V_FLAG_FISCAL ) );
		// System.out.print( "result do estado do papel > " );
		// System.out.println( ecf.resultEstadoPapel() );
		// System.out.print( "result da ultima redução Z > " );
		// System.out.println( ecf.resultUltimaReducao() );
	}

	public void testComandosDeImpressaoDeCheques() {

	/*
	 * ECFDaruma ecf = new ECFDaruma( "COM1" );
	 * 
	 * System.out.print( "programaMoedaSingular > " ); assertTrue( trataresultFuncao( ecf.programaMoedaSingular( "Real" ) ) );
	 * 
	 * System.out.print( "programaMoedaPlural > " ); assertTrue( trataresultFuncao( ecf.programaMoedaPlural( "Reais" ) ) );
	 * 
	 * System.out.print( "imprimeCheque > " ); System.out.println( ecf.imprimeCheque( 1.30f, "Favorecido                                   ", "Localidade                 ", 19, 11, 2007 ) );
	 * 
	 * System.out.print( "resultStatusCheque > " ); System.out.println( ecf.resultStatusCheque() );
	 */
	}

	public void testVariaveis() {

		ECFDaruma ecf = new ECFDaruma( "COM1" );
		System.out.println( "Número do caixa > " + ecf.resultVariaveis( AbstractECFDriver.V_NUM_CAIXA ) );
		System.out.println( "Cancelamentos > " + ecf.resultVariaveis( AbstractECFDriver.V_CUPONS_CANC ) );
		System.out.println( "Última redução > " + ecf.resultVariaveis( AbstractECFDriver.V_REDUCOES ) );
		System.out.println( "Total Cancelamentos > " + ecf.resultVariaveis( AbstractECFDriver.V_CANCELAMENTOS ) );
		System.out.println( "Total Descontos > " + ecf.resultVariaveis( AbstractECFDriver.V_DESCONTOS ) );
	}

	public void testGetStatus() {

		try {
			ECFDaruma ecf = new ECFDaruma( "COM1" );
			System.out.print( "result de status > " );
			ecf.getStatus();
			System.out.println( new String( ecf.getBytesLidos() ) );
		}
		catch ( RuntimeException e ) {
			e.printStackTrace();
		}
	}

	public boolean trataresultFuncao( final ECFDaruma ecf, final STResult arg ) {

		boolean returnOfAction = !arg.isInError();
		System.out.println( arg.getMessages() );
		return returnOfAction;
	}
}
