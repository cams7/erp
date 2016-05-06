
package org.freedom.ecf.test;

import java.math.BigDecimal;
import java.util.List;

import org.freedom.ecf.app.ControllerECF;
import org.freedom.ecf.driver.AbstractECFDriver;

import junit.framework.TestCase;

public class ControlTest extends TestCase {

	public ControlTest( String name ) {

		super( name );
	}

	public void testInstanciarControl() {

		ControllerECF control = null;
		try {
			control = new ControllerECF( "org.freedom.ecf.driver.ECFBematech" );
		}
		catch ( IllegalArgumentException e ) {
		}
		catch ( NullPointerException e ) {
		}
		assertTrue( "Control instaciado", control != null );
	}

	public void testNaoInstanciarControl_Ecf_Nulo() {

		boolean assertresult = false;
		try {
			new ControllerECF( "nome errado" );
		}
		catch ( NullPointerException e ) {
			assertresult = true;
		}
		assertTrue( "Control não instanciado por não carregar ecfdriver.", assertresult );
	}

	public void testNaoInstanciarControl_Null() {

		boolean assertresult = false;
		try {
			new ControllerECF( null );
		}
		catch ( IllegalArgumentException e ) {
			assertresult = true;
		}
		assertTrue( "Control não instanciado por driver nulo.", assertresult );
	}

	public void testNaoInstanciarControl_Invalido() {

		boolean assertresult = false;
		try {
			new ControllerECF( "" );
		}
		catch ( IllegalArgumentException e ) {
			assertresult = true;
		}
		assertTrue( "Control não instanciado por parametro invalido.", assertresult );
	}

	public void testProgramaHorarioVerao() {

		ControllerECF control = new ControllerECF( "org.freedom.ecf.driver.ECFBematech" );
		System.out.print( "programando horário de verão > " );
		assertTrue( control.setHorarioDeVerao( false ) );
		System.out.println( control.getMessageLog() );
	}

	public void testNomeiaDepartamento() {

		ControllerECF control = new ControllerECF( "org.freedom.ecf.driver.ECFBematech" );
		System.out.print( "nomeia deparatamento > " );
		assertTrue( control.nomeiaDepartamento( 2, "Vestuario" ) );
		System.out.println( control.getMessageLog() );
	}

	public void testLeituraX() {

		ControllerECF control = new ControllerECF( "org.freedom.ecf.driver.ECFDaruma" );
		System.out.print( "leitura X > " );
		assertTrue( control.leituraX() );
		System.out.println( control.getMessageLog() );
		System.out.print( "leitura X > " );
		assertTrue( control.leituraX( true ) );
		System.out.println( control.getMessageLog() );
		System.out.println( control.readSerialPort() );
	}

	public void testSuprimento() {

		ControllerECF control = new ControllerECF( "org.freedom.ecf.driver.ECFBematech" );
		System.out.println( "suprimento > " );
		assertTrue( control.suprimento( new BigDecimal( "53.795" ) ) );
		System.out.println( "suprimento Cheque > " );
		assertTrue( control.suprimento( new BigDecimal( "53.793" ), "Cheque" ) );
	}

	public void testNumeroDocumento() {

		ControllerECF control = new ControllerECF( "org.freedom.ecf.driver.ECFBematech" );
		System.out.print( "número do documento > " );
		Integer tmp = null;
		System.out.println( tmp = control.getNumeroDocumento() );
		assertTrue( tmp != null );
	}

	public void testSubTotal() {

		ControllerECF control = new ControllerECF( "org.freedom.ecf.driver.ECFBematech" );
		System.out.print( "sub total > " );
		BigDecimal tmp = null;
		System.out.println( tmp = control.getSubTotal() );
		assertTrue( tmp != null );
	}

	public void testNumeroCaixa() {

		ControllerECF control = new ControllerECF( "org.freedom.ecf.driver.ECFBematech" );
		System.out.print( "número do caixa > " );
		Integer tmp = null;
		System.out.println( tmp = control.getNumeroCaixa() );
		assertTrue( tmp != null );
	}

	public void testVerificaEstado() {

		// ControllerECF control = new ControllerECF( "org.freedom.ecf.driver.ECFBematech" );
		System.out.print( "verifica estado da impressora > " );
		String tmp = null;
		// System.out.println( tmp = control.getStatusImpressora() );
		assertTrue( tmp != null );
	}

	public void testCupomFiscal() {

		ControllerECF control = new ControllerECF( "org.freedom.ecf.driver.ECFBematech" );
		System.out.print( "abre cupom fiscal > " );
		assertTrue( control.abreCupom( "00.000.000/000-00" ) );
		System.out.println( control.getMessageLog() );
		/*
		 * System.out.print( "unidade de medida > " ); assertTrue( control.unidadeMedida( "Kg" ) ); System.out.println( control.getMessageLog() );
		 * 
		 * System.out.print( "aumenta descrição do item > " ); assertTrue( control.aumetaDescricaoItem( "123456789à123456789â123456789ã123456789ü123456789ç123456789 123456789 123456789 123456789 123456789 " +
		 * "123456789à123456789â123456789ã123456789ü123456789ç123456789 123456789 123456789 123456789 123456789 " + "123456789à123456789â123456789ã123456789ü123456789ç123456789 123456789 123456789 123456789 123456789 " ) ); System.out.println(
		 * control.getMessageLog() );
		 */
		System.out.print( "venda item > " );
		assertTrue( control.vendaItem( "1", "PRODUTO TESTE", new BigDecimal( "18" ), new BigDecimal( "1" ), new BigDecimal( "20.0" ) ) );
		System.out.println( control.getMessageLog() );
		/*
		 * System.out.print( "venda item > " ); assertTrue( control.vendaItem( "1", "PRODUTO TESTE áàâã éèê íì óòôõ úùûü", new BigDecimal( "18" ), new BigDecimal( "1.5" ), new BigDecimal( "15.334" ), new BigDecimal( "0" ) ) ); System.out.println(
		 * control.getMessageLog() );
		 * 
		 * System.out.print( "venda item > " ); assertTrue( control.vendaItem( "1", "PRODUTO TESTE", new BigDecimal( "18" ), AbstractECFDriver.QTD_INTEIRO, new BigDecimal( "2" ), AbstractECFDriver.TRES_CASAS_DECIMAIS, new BigDecimal( "15.334" ),
		 * AbstractECFDriver.DESCONTO_PERC, new BigDecimal( "0" ) ) ); System.out.println( control.getMessageLog() );
		 * 
		 * System.out.print( "venda item > " ); assertTrue( control.vendaItem( "1", "PRODUTO TESTE", new BigDecimal( "18" ), AbstractECFDriver.QTD_DECIMAL, new BigDecimal( "1.5" ), AbstractECFDriver.TRES_CASAS_DECIMAIS, new BigDecimal( "15.334" ),
		 * AbstractECFDriver.DESCONTO_PERC, new BigDecimal( "0" ) ) ); System.out.println( control.getMessageLog() );
		 * 
		 * System.out.print( "venda item departamento > " ); assertTrue( control.vendaItemDepartamento( "1", "PRODUTO TESTE", new BigDecimal( "18" ), new BigDecimal( "15.334" ), new BigDecimal( "1.5" ), new BigDecimal( "0" ), new BigDecimal( "0" ),
		 * 2, "kg" ) ); System.out.println( control.getMessageLog() );
		 * 
		 * System.out.print( "cancela item anterior > " ); assertTrue( control.cancelaItem() ); System.out.println( control.getMessageLog() );
		 * 
		 * System.out.print( "cancela item generico (2) > " ); assertTrue( control.cancelaItem( 2 ) ); System.out.println( control.getMessageLog() );
		 */
		System.out.print( "inicia fechamento > " );
		assertTrue( control.iniciaFechamentoCupom( AbstractECFDriver.DESCONTO_VALOR, new BigDecimal( "3.99" ) ) );
		System.out.println( control.getMessageLog() );
		System.out.print( "efetua forma de pagamento Dinheiro > " );
		assertTrue( control.efetuaFormaPagamento( new BigDecimal( "20" ) ) );
		System.out.println( control.getMessageLog() );
		/*
		 * System.out.print( "efetua forma de pagamento Cheque > " ); assertTrue( control.efetuaFormaPagamento( "Cheque", new BigDecimal( "35" ), "Cheque de terceiro" ) ); System.out.println( control.getMessageLog() );
		 * 
		 * System.out.print( "efetua forma de pagamento Cartão > " ); assertTrue( control.efetuaFormaPagamento( "Cartão", new BigDecimal( "20" ) ) ); System.out.println( control.getMessageLog() );
		 */
		System.out.print( "finaliza cupom > " );
		assertTrue( control.finalizaFechamentoCupom( " Obrigado e volte sempre!" ) );
		System.out.println( control.getMessageLog() );
		/*
		 * System.out.print( "cancelamento de cupom> " ); assertTrue( control.cancelaCupom() ); System.out.println( control.getMessageLog() );
		 * 
		 * System.out.print( "estorna forma de pagamento> " ); assertTrue( control.estornoFormaPagamento( "Cheque", "Cartão", new BigDecimal( "35" ) ) ); System.out.println( control.getMessageLog() );
		 */
	}

	public void testCancelaCupom() {

		ControllerECF control = new ControllerECF( "org.freedom.ecf.driver.ECFBematech" );
		System.out.print( "cancelamento de cupom fiscal > " );
		assertTrue( control.cancelaCupom() );
		System.out.println( control.getMessageLog() );
	}

	public void testComprovanteNaoFiscalVinculado() {

		ControllerECF control = new ControllerECF( "org.freedom.ecf.driver.ECFBematech" );
		testCupomFiscal();
		System.out.print( "abre comprovante não fiscal vinculado > " );
		assertTrue( control.abreComprovanteNaoFiscalVinculado( "Cartão", new BigDecimal( "20" ), control.getNumeroDocumento() ) );
		System.out.println( control.getMessageLog() );
		System.out.print( "usando comprovante não fiscal vinculado > " );
		assertTrue( control.usaComprovanteNaoFiscalVinculado( getMensagemTef() ) );
		System.out.println( control.getMessageLog() );
		System.out.print( "fechando comprovante não fiscal vinculado > " );
		assertTrue( control.fecharRelatorioGerencial() );
		System.out.println( control.getMessageLog() );
	}

	public void testRelatorioGerencial() {

		ControllerECF control = new ControllerECF( "org.freedom.ecf.driver.ECFBematech" );
		System.out.print( "relatório gerencial > " );
		assertTrue( control.relatorioGerencial( getMensagemTef() ) );
		System.out.println( control.getMessageLog() );
		System.out.print( "fechamento de relatorio gerencial > " );
		assertTrue( control.fecharRelatorioGerencial() );
		System.out.println( control.getMessageLog() );
	}

	public void testReducaoZ() {

		ControllerECF control = new ControllerECF( "org.freedom.ecf.driver.ECFDaruma" );
		System.out.print( "redução Z > " );
		assertTrue( control.reducaoZ() );
		System.out.println( control.getMessageLog() );
	}

	public void testReducaoZExecutada() {

		ControllerECF control = new ControllerECF( "org.freedom.ecf.driver.ECFBematech" );
		System.out.print( "redução Z executada > " );
		boolean tmp = control.reducaoZExecutada();
		System.out.print( tmp ? "sim" : "não" );
	}

	public void testSangria() {

		ControllerECF control = new ControllerECF( "org.freedom.ecf.driver.ECFBematech" );
		System.out.println( "sangria > " );
		assertTrue( control.sangria( new BigDecimal( "53.795" ) ) );
		System.out.println( "sangria Cheque > " );
		assertTrue( control.suprimento( new BigDecimal( "53.793" ), "Cheque" ) );
	}

	public void testGetAllAliquotas() {

		ControllerECF control = new ControllerECF( "org.freedom.ecf.driver.ECFDaruma" );
		List<String> aliquotas = control.getAliquotas();
		for ( String s : aliquotas ) {
			System.out.println( s );
		}
	}

	public void testGetIndexAliquota() {

		ControllerECF control = new ControllerECF( "org.freedom.ecf.driver.ECFBematech" );
		assertTrue( control.getIndexAliquota( 18f ) != null );
		assertTrue( control.getIndexAliquota( 20.18f ) == null );
		assertTrue( control.getIndexAliquota( 200.185f ) == null );
		assertTrue( control.getIndexAliquota( 0.01f ) != null );
	}

	public void testProgramaMoeda() {

		ControllerECF control = new ControllerECF( "org.freedom.ecf.driver.ECFBematech" );
		System.out.print( "programa moeda > " );
		assertTrue( control.programaMoeda( "TT", "Teste", "Testes" ) );
		System.out.println( control.getMessageLog() );
	}

	private String getMensagemTef() {

		String tef = "*SIMULADO* REDECARD\n" + "  DEBITO A VISTA\n" + "\n" + "  ESTAB: 000000000000005\n" + "  N.CARTAO: 589916******7898\n" + "  DATA: 14/09  HORA: 16:02:12\n" + "  TERMINAL: SW000002  DOC: 140012\n" + "  AUTORIZACAO: 140012\n"
				+ "  VALOR: 2,00\n" + "  <ARQC:>--> 817EAAA98EDE82E5 \n" + "  TRANSACAO AUTORIZADA MEDIANTE\n" + "  USO DE SENHA \n" + "                                 (SiTef)\n" + "\n" + "\n" + "  *SIMULADO* REDECARD\n" + "  DEBITO A VISTA\n"
				+ "\n" + "  ESTAB: 000000000000005\n" + "  N.CARTAO: 589916******7898 \n" + "  DATA: 14/09  HORA: 16:02:12\n" + "  TERMINAL: SW000002  DOC: 140012\n" + "  AUTORIZACAO: 140012\n" + "  VALOR: 2,00\n"
				+ "  <ARQC:>--> 817EAAA98EDE82E5 \n" + "  TRANSACAO AUTORIZADA MEDIANTE\n" + "  USO DE SENHA\n" + "                               (SiTef)";
		return tef;
	}
}
