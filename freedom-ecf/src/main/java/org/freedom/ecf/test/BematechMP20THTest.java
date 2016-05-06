
package org.freedom.ecf.test;

import junit.framework.TestCase;

import org.freedom.ecf.app.PrinterMemory;
import org.freedom.ecf.driver.BematechMP20TH;
import org.freedom.ecf.driver.STResult;
import org.freedom.ecf.layout.LayoutDemonstration;

public class BematechMP20THTest extends TestCase {

	private LayoutDemonstration layout = new LayoutDemonstration();

	public BematechMP20THTest( String name ) {

		super( name );
		PrinterMemory memory = new PrinterMemory();
		memory.setEmpresa( "Setpoint Informatica ltda" );
		memory.setEndereco( "Joao Leopoldo Jacomel, 12475" );
		memory.setCidade( "Pinhas - PR" );
		memory.setTelefone( "(041) 3668-6500" );
		layout.setMemory( memory );
	}

	public void testGenerico() {

		BematechMP20TH ecf = new BematechMP20TH( "COM1", "org.freedom.ecf.layout.LayoutDemonstration" );
		assertTrue( trataresultFuncao( ecf.leituraX() ) );
	}

	public void testCancelaCupom() {

		BematechMP20TH ecf = new BematechMP20TH( "COM1", "org.freedom.ecf.layout.LayoutDemonstration" );
		assertTrue( trataresultFuncao( ecf.cancelaCupom() ) );
	}

	/*
	 * public void testComandosDeInicializacao() {
	 * 
	 * BematechMP20TH ecf = new BematechMP20TH( "COM1" );
	 * 
	 * assertTrue( trataresultFuncao( ecf.alteraSimboloMoeda( "R" ) ) );
	 * 
	 * assertTrue( trataresultFuncao( ecf.adicaoDeAliquotaTriburaria( "0001", BematechMP20TH.ICMS ) ) );
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
	 * assertTrue( trataresultFuncao( ecf.nomeiaDepartamento( 2, "Teste" ) ) ); }
	 */
	public void testComandosDeCupomFiscal() {

		BematechMP20TH ecf = new BematechMP20TH( "COM1", layout );
		// System.out.print( "aberturaDeCupom > " );
		// assertTrue( trataresultFuncao( ecf.aberturaDeCupom() ) );
		assertTrue( trataresultFuncao( ecf.aberturaDeCupom( "00.000.000/0000-00 Cliente Fulando de Tal" ) ) );
		assertTrue( trataresultFuncao( ecf.programaUnidadeMedida( "Kg" ) ) );
		assertTrue( trataresultFuncao( ecf.vendaItem( "12345678901234", "Produto Teste", "FF", 'I', 1f, 10f, 'D', 0f ) ) );
		assertTrue( trataresultFuncao( ecf.vendaItem( "12345678901234", "Descrição para o produto teste ", "FF", 'I', 1.5f, 10f, 'D', 0f ) ) );
		assertTrue( trataresultFuncao( ecf.vendaItem( "12345678901234", "Descrição muito longa para o produto de teste", "FF", 'I', 1.4999f, 10f, 'D', 0f ) ) );
		assertTrue( trataresultFuncao( ecf.vendaItem( "12345678901234", "Produto", "FF", 'I', 1.499f, 10f, 'D', 0f ) ) );
		/*
		 * System.out.print( "cancelaItemAnterior > " ); assertTrue( trataresultFuncao( ecf.cancelaItemAnterior() ) );
		 * 
		 * System.out.print( "aumentaDescItem > " ); assertTrue( trataresultFuncao( ecf.aumentaDescItem( "Descricao do item aumentada para 60 caracteres" ) ) );
		 * 
		 * System.out.print( "vendaItemTresCasas > " ); assertTrue( trataresultFuncao( ecf.vendaItemTresCasas( "123456", "Produto Teste", "FF", 'I', 2f, 2.050f, 'D', 0.10f ) ) );
		 * 
		 * System.out.print( "vendaItemDepartamento > " ); assertTrue( trataresultFuncao( ecf.vendaItemDepartamento( "FF", 1f, 10f, 0.50f, 0.50f, 2, "Kg", "1234567890003", "Descricao do produto" ) ) );
		 */
		assertTrue( trataresultFuncao( ecf.iniciaFechamentoCupom( BematechMP20TH.ACRECIMO_VALOR, 0.0f ) ) );
		String f1 = ecf.programaFormaPagamento( "Dinheiro" );
		assertTrue( trataresultFuncao( ecf.efetuaFormaPagamento( f1, 10.00f, "" ) ) );
		String f2 = ecf.programaFormaPagamento( "Cheque" );
		assertTrue( trataresultFuncao( ecf.efetuaFormaPagamento( f2, 5.0f, "" ) ) );
		assertTrue( trataresultFuncao( ecf.finalizaFechamentoCupom( "Obrigado e volte sempre pra testar!" ) ) );
		/*
		 * System.out.print( "estornoFormaPagamento > " ); assertTrue( trataresultFuncao( ecf.estornoFormaPagamento( "CHEQUE", "Dinheiro", 5.0f ) ) );
		 */
	}

	public void testComandosDeOperacoesNaoFiscais() {

	// BematechMP20TH ecf = new BematechMP20TH( "COM1", layout );
	// System.out.println( "relatorioGerencial > " );
	// assertTrue( trataresultFuncao( ecf.relatorioGerencial( "Abrindo Relatorio Gerencial" ) ) );
	// assertTrue( trataresultFuncao( ecf.relatorioGerencial( "Utilizando Relatorio Gerencial - blablabla blablabla blablabla blablabla blablabla blablabla" ) ) );
	// assertTrue( trataresultFuncao( ecf.fechamentoRelatorioGerencial() ) );
	// System.out.println( "comprovanteNFiscalNVinculado suprimento > " );
	// assertTrue( trataresultFuncao( ecf.comprovanteNFiscalNVinculado( BematechMP20TH.SUPRIMENTO, 50f, "Dinheiro        " ) ) );
	// System.out.println( "comprovanteNFiscalNVinculado sangria > " );
	// assertTrue( trataresultFuncao( ecf.comprovanteNFiscalNVinculado( BematechMP20TH.SANGRIA, 45f, "Dinheiro        " ) ) );
	// System.out.println( "comprovanteNFiscalNVinculado não ICMS > " );
	// assertTrue( trataresultFuncao( ecf.comprovanteNFiscalNVinculado( "02", 35f, "Dinheiro        " ) ) );
	/*
	 * testComandosDeCupomFiscal(); System.out.println( "abreComprovanteNFiscalVinculado > " ); assertTrue( trataresultFuncao( ecf.abreComprovanteNFiscalVinculado( "Cheque          ", 5.5f, Integer.parseInt( ecf.resultNumeroCupom() ) ) ) );
	 * System.out.println( "usaComprovanteNFiscalVinculado > " ); assertTrue( trataresultFuncao( ecf.usaComprovanteNFiscalVinculado( "Usando o Comprovante Nao fiscal Vinculado" ) ) ); System.out.println( "fechamentoRelatorioGerencial > " );
	 * assertTrue( trataresultFuncao( ecf.fechamentoRelatorioGerencial() ) );
	 */
	}

	/*
	 * public void testComandosDeAutenticacao() {
	 * 
	 * BematechMP20TH ecf = new BematechMP20TH( "COM1" );
	 * 
	 * System.out.print( "programaCaracterParaAutenticacao > " ); //int [] sesc = {143,137,137,249,0,255,137,137,137,0,143,137,137,249,0,255,129,129 }; int [] sesc = {1,2,4,8,16,32,64,128,64,32,16,8,4,2,1,129,129,129 }; assertTrue( trataresultFuncao(
	 * ecf.programaCaracterParaAutenticacao( sesc ) ) );
	 * 
	 * testComandosDeCupomFiscal(); System.out.print( "autenticacaoDeDocumento > " ); assertTrue( trataresultFuncao( ecf.autenticacaoDeDocumento() ) ); }
	 */
	/*
	 * public void testComandosDeRelatoriosFiscais() {
	 * 
	 * BematechMP20TH ecf = new BematechMP20TH( "COM1", layout );
	 * 
	 * System.out.print( "leituraX > " ); assertTrue( trataresultFuncao( ecf.leituraX() ) );
	 * 
	 * System.out.print( "leituraXSerial > " ); assertTrue( trataresultFuncao( ecf.leituraXSerial() ) ); System.out.println( new String( ecf.getBytesLidos() ) );
	 * 
	 * Calendar cal = Calendar.getInstance(); Date hoje = cal.getTime(); cal.set( Calendar.MONTH, cal.get( Calendar.MONTH ) - 1 ); Date antes = cal.getTime();
	 * 
	 * System.out.print( "leituraMemoriaFiscal data > " ); assertTrue( trataresultFuncao( ecf.leituraMemoriaFiscal( antes, hoje, BematechMP20TH.IMPRESSAO ) ) ); System.out.println( new String( ecf.getBytesLidos() ) );
	 * 
	 * System.out.print( "leituraMemoriaFiscal data result > " ); assertTrue( trataresultFuncao( ecf.leituraMemoriaFiscal( antes, hoje, BematechMP20TH.SERIAL ) ) ); System.out.println( new String( ecf.getBytesLidos() ) );
	 * 
	 * System.out.print( "leituraMemoriaFiscal redução > " ); assertTrue( trataresultFuncao( ecf.leituraMemoriaFiscal( 2, 13, BematechMP20TH.IMPRESSAO ) ) ); System.out.println( new String( ecf.getBytesLidos() ) );
	 * 
	 * System.out.print( "leituraMemoriaFiscal redução result > " ); assertTrue( trataresultFuncao( ecf.leituraMemoriaFiscal( 2, 13, BematechMP20TH.SERIAL ) ) ); System.out.println( new String( ecf.getBytesLidos() ) ); }
	 */
	/*
	 * public void testComandosDeInformacoesDaImpressora() {
	 * 
	 * BematechMP20TH ecf = new BematechMP20TH( "COM1" );
	 * 
	 * System.out.print( "leitura do estado > " ); System.out.println( ecf.getStatus() );
	 * 
	 * System.out.print( "result de aliquotas > " ); System.out.println( ecf.resultAliquotas() );
	 * 
	 * System.out.print( "result de totalizadores parcias > " ); System.out.println( ecf.resultTotalizadoresParciais() );
	 * 
	 * System.out.print( "result de número do cupom > " ); System.out.println( ecf.resultNumeroCupom() );
	 * 
	 * System.out.println( "result de variáveis > " ); System.out.println( "\tGrande total > " + ecf.resultVariaveis( BematechMP20TH.V_GRANDE_TOTAL ) ); System.out.println( "\tFlags Fiscais > " + ecf.resultVariaveis( BematechMP20TH.V_FLAG_FISCAL ) );
	 * 
	 * System.out.print( "result do estado do papel > " ); System.out.println( ecf.resultEstadoPapel() );
	 * 
	 * System.out.print( "result da ultima redução Z > " ); System.out.println( ecf.resultUltimaReducao() );
	 * 
	 * System.out.print( "sub total > > " ); System.out.println( ecf.resultSubTotal() ); }
	 */
	/*
	 * public void testComandosDeImpressaoDeCheques() {
	 * 
	 * BematechMP20TH ecf = new BematechMP20TH( "COM1" );
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

		return true;
	}
}
