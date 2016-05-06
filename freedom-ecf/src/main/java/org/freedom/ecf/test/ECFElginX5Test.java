
package org.freedom.ecf.test;

import junit.framework.TestCase;

import org.freedom.ecf.driver.ECFElginX5;
import org.freedom.ecf.driver.STResult;

public class ECFElginX5Test extends TestCase {

	private static final String PORTA = "COM4";

	public ECFElginX5Test( String name ) {

		super( name );
	}

	public void testGenerico() {

		ECFElginX5 ecf = new ECFElginX5( PORTA );
		trataresultFuncao( ecf.executa( "DefineMeioPagamento", "NomeMeioPagamento=\"DINHEIRO\"", "PermiteVinculado=true" ) );
		trataresultFuncao( ecf.executa( "DefineMeioPagamento", "NomeMeioPagamento=\"CHEQUE\"", "PermiteVinculado=true" ) );
		trataresultFuncao( ecf.executa( "DefineMeioPagamento", "NomeMeioPagamento=\"CARTAO\"", "PermiteVinculado=true" ) );
		ecf.leituraX();
	}

	public void testCancelaCupom() {

		ECFElginX5 ecf = new ECFElginX5( PORTA );
		assertTrue( trataresultFuncao( ecf.cancelaCupom() ) );
	}

	/*
	 * public void testComandosDeInicializacao() {
	 * 
	 * ECFElginX5 ecf = new ECFElginX5( PORTA );
	 * 
	 * assertTrue( trataresultFuncao( ecf.alteraSimboloMoeda( "R" ) ) );
	 * 
	 * assertTrue( trataresultFuncao( ecf.adicaoDeAliquotaTriburaria( "0001", ECFElginX5.ICMS ) ) );
	 * 
	 * assertTrue( trataresultFuncao( ecf.programaHorarioVerao() ) );
	 * 
	 * assertTrue( trataresultFuncao( ecf.nomeiaTotalizadorNaoSujeitoICMS( 4, "Totalizador teste" ) ) );
	 * 
	 * assertTrue( trataresultFuncao( ecf.programaTruncamentoArredondamento( '1' ) ) );
	 * 
	 * assertTrue( trataresultFuncao( ecf.programarEspacoEntreLinhas( 8 ) ) );
	 * 
	 * assertTrue( trataresultFuncao( ecf.programarLinhasEntreCupons( 5 ) ) );
	 * 
	 * assertTrue( trataresultFuncao( ecf.nomeiaDepartamento( 2, "Teste" ) ) );
	 * 
	 * //ecf.programaFormaPagamento( "CHEQUE" ); }
	 */
	public void testComandosDeCupomFiscal() {

		ECFElginX5 ecf = new ECFElginX5( PORTA );
		System.out.print( "aberturaDeCupom > " );
		assertTrue( trataresultFuncao( ecf.aberturaDeCupom() ) );
		// System.out.print( "aberturaDeCupom String > " );
		// assertTrue( trataresultFuncao( ecf.aberturaDeCupom( "00.000.000/0000-00 CNPJ e nome do Cliente" ) ) );
		// System.out.print( "programaUnidadeMedida > " );
		// assertTrue( trataresultFuncao( ecf.programaUnidadeMedida( "Kg" ) ) );
		System.out.print( "vendaItem > " );
		assertTrue( trataresultFuncao( ecf.vendaItem( "000001", "Produto Teste", "FF", 'I', 1f, 10f, 'D', 0f ) ) );
		// System.out.print( "cancelaItemAnterior > " );
		// assertTrue( trataresultFuncao( ecf.cancelaItemAnterior() ) );
		// System.out.print( "aumentaDescItem > " );
		// assertTrue( trataresultFuncao( ecf.aumentaDescItem( "Descricao do item aumentada para 60 caracteres" ) ) );
		// System.out.print( "vendaItemTresCasas > " );
		// assertTrue( trataresultFuncao( ecf.vendaItemTresCasas(
		// "123456", "Produto Teste", "FF", 'I', 2f, 2.050f, 'D', 0.10f ) ) );
		// System.out.print( "vendaItemDepartamento > " );
		// assertTrue( trataresultFuncao( ecf.vendaItemDepartamento(
		// "FF", 1f, 10f, 0.50f, 0.50f, 2, "Kg", "1234567890003", "Descricao do produto" ) ) );
		// System.out.print( "iniciaFechamentoCupom > " );
		// assertTrue( trataresultFuncao( ecf.iniciaFechamentoCupom( ECFElginX5.DESCONTO_PERC, 20.0f ) ) );
		System.out.print( "efetuaFormaPagamento Dinheiro > " );
		String indice = ecf.programaFormaPagamento( "Cheque" );
		assertTrue( trataresultFuncao( ecf.efetuaFormaPagamento( indice, 10.00f, "" ) ) );
		// System.out.print( "programaFormaPagamento > " );
		// String f2 = ecf.programaFormaPagamento( "Cheque          " );
		// System.out.println( f2 );
		// assertTrue( ! "".equals( f2 ) );
		// System.out.print( "efetuaFormaPagamento Cheque > " );
		// assertTrue( trataresultFuncao( ecf.efetuaFormaPagamento( "01", 5.0f, "" ) ) );
		System.out.print( "finalizaFechamentoCupom > " );
		assertTrue( trataresultFuncao( ecf.finalizaFechamentoCupom( "Obrigado e volte sempre pra testar!" ) ) );
		// System.out.print( "estornoFormaPagamento > " );
		// assertTrue( trataresultFuncao( ecf.estornoFormaPagamento( "CHEQUE", "Dinheiro", 5.0f ) ) );
	}

	public void testComandosDeOperacoesNaoFiscais() {

		ECFElginX5 ecf = new ECFElginX5( PORTA );
		// System.out.print( "relatorioGerencial > " );
		// assertTrue( trataresultFuncao( ecf.relatorioGerencial(
		// "Abrindo Relatorio Gerencial" ) ) );
		//		
		// System.out.print( "relatorioGerencial usando > " );
		// assertTrue( trataresultFuncao( ecf.relatorioGerencial(
		// "\n\nUtilizando Relatorio Gerencial\n\n" ) ) );
		//		
		// System.out.print( "fechamentoRelatorioGerencial > " );
		// assertTrue( trataresultFuncao( ecf.fechamentoRelatorioGerencial() ) );
		// System.out.print( "comprovanteNFiscalNVinculado suprimento > " );
		// assertTrue( trataresultFuncao( ecf.comprovanteNFiscalNVinculado( ECFElginX5.SUPRIMENTO, 50f, "Dinheiro        " ) ) );
		// System.out.print( "comprovanteNFiscalNVinculado sangria > " );
		// assertTrue( trataresultFuncao( ecf.comprovanteNFiscalNVinculado( ECFElginX5.SANGRIA, 45f, "Dinheiro        " ) ) );
		// System.out.print( "comprovanteNFiscalNVinculado não ICMS > " );
		// assertTrue( trataresultFuncao( ecf.comprovanteNFiscalNVinculado( "02", 35f, "Dinheiro        " ) ) );
		testComandosDeCupomFiscal();
		System.out.print( "abreComprovanteNFiscalVinculado > " );
		assertTrue( trataresultFuncao( ecf.abreComprovanteNFiscalVinculado( "Cheque", 10f, Integer.parseInt( ecf.resultNumeroCupom() ) ) ) );
		System.out.print( "usaComprovanteNFiscalVinculado > " );
		assertTrue( trataresultFuncao( ecf.usaComprovanteNFiscalVinculado( "Usando o Comprovante Nao fiscal Vinculado" ) ) );
		System.out.print( "fechamentoRelatorioGerencial > " );
		assertTrue( trataresultFuncao( ecf.fechamentoRelatorioGerencial() ) );
	}

	/*
	 * public void testComandosDeAutenticacao() {
	 * 
	 * ECFElginX5 ecf = new ECFElginX5( "COM1" );
	 * 
	 * System.out.print( "programaCaracterParaAutenticacao > " ); //int [] sesc = {143,137,137,249,0,255,137,137,137,0,143,137,137,249,0,255,129,129 }; int [] sesc = {1,2,4,8,16,32,64,128,64,32,16,8,4,2,1,129,129,129 }; assertTrue( trataresultFuncao(
	 * ecf.programaCaracterParaAutenticacao( sesc ) ) );
	 * 
	 * testComandosDeCupomFiscal(); System.out.print( "autenticacaoDeDocumento > " ); assertTrue( trataresultFuncao( ecf.autenticacaoDeDocumento() ) ); }
	 */
	public void testComandosDeRelatoriosFiscais() {

		ECFElginX5 ecf = new ECFElginX5( PORTA );
		System.out.print( "leituraX > " );
		assertTrue( trataresultFuncao( ecf.leituraX() ) );
		// System.out.print( "leituraXSerial > " );
		// assertTrue( trataresultFuncao( ecf.leituraXSerial() ) );
		// System.out.println( new String( ecf.getBytesLidos() ) );
		// Calendar cal = Calendar.getInstance();
		// Date hoje = cal.getTime();
		// cal.set( Calendar.MONTH, cal.get( Calendar.MONTH ) - 1 );
		// Date antes = cal.getTime();
		// System.out.print( "leituraMemoriaFiscal data > " );
		// assertTrue( trataresultFuncao( ecf.leituraMemoriaFiscal( antes, hoje, ECFElginX5.IMPRESSAO ) ) );
		// System.out.println( new String( ecf.getBytesLidos() ) );
		// System.out.print( "leituraMemoriaFiscal data result > " );
		// assertTrue( trataresultFuncao( ecf.leituraMemoriaFiscal( antes, hoje, ECFElginX5.SERIAL ) ) );
		// System.out.println( new String( ecf.getBytesLidos() ) );
		/*
		 * System.out.print( "leituraMemoriaFiscal redução > " ); assertTrue( trataresultFuncao( ecf.leituraMemoriaFiscal( 2, 13, ECFElginX5.IMPRESSAO ) ) ); System.out.println( new String( ecf.getBytesLidos() ) );
		 * 
		 * System.out.print( "leituraMemoriaFiscal redução result > " ); assertTrue( trataresultFuncao( ecf.leituraMemoriaFiscal( 2, 13, ECFElginX5.SERIAL ) ) ); System.out.println( new String( ecf.getBytesLidos() ) );
		 */
	}

	public void testComandosDeInformacoesDaImpressora() {

		ECFElginX5 ecf = new ECFElginX5( PORTA );
		/*
		 * System.out.print( "leitura do estado > " ); System.out.println( ecf.getStatus() );
		 */
		// System.out.print( "result de aliquotas > " ); //OK
		// System.out.println( ecf.resultAliquotas() );
		// System.out.print( "result de totalizadores parcias > " );
		// System.out.println( ecf.resultTotalizadoresParciais() );
		// System.out.println( "result de número do cupom > " ); //OK
		// System.out.println( ecf.resultNumeroCupom() );
		// System.out.println( "result de variáveis > " );
		// System.out.println( "\tGrande total > " + ecf.resultVariaveis( ECFElginX5.V_GRANDE_TOTAL ) );
		// System.out.println( "\tFlags Fiscais > " + ecf.resultVariaveis( ECFElginX5.V_FLAG_FISCAL ) );
		//		
		// System.out.print( "result do estado do papel > " );
		// System.out.println( ecf.resultEstadoPapel() );
		System.out.print( "result da ultima redução Z > " );
		System.out.println( ecf.resultUltimaReducao() );
		// System.out.print( "sub total > > " );
		// System.out.println( ecf.resultSubTotal() );
	}

	/*
	 * public void testComandosDeImpressaoDeCheques() {
	 * 
	 * ECFElginX5 ecf = new ECFElginX5( PORTA );
	 * 
	 * System.out.print( "programaMoedaSingular > " ); assertTrue( trataresultFuncao( ecf.programaMoedaSingular( "Real" ) ) );
	 * 
	 * System.out.print( "programaMoedaPlural > " ); assertTrue( trataresultFuncao( ecf.programaMoedaPlural( "Reais" ) ) );
	 * 
	 * System.out.print( "imprimeCheque > " ); System.out.println( ecf.imprimeCheque( 1.30f, "Favorecido                                   ", "Localidade                 ", 19, 11, 2007 ) );
	 * 
	 * System.out.print( "resultStatusCheque > " ); System.out.println( ecf.resultStatusCheque() );
	 * 
	 * }
	 */
	public boolean trataresultFuncao( final STResult result ) {

		boolean returnOfAction = !result.isInError();
		System.out.println( result.getMessages() );
		return returnOfAction;
	}
}
