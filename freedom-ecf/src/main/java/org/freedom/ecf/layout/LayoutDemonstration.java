
package org.freedom.ecf.layout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Timer;

import org.freedom.ecf.driver.AbstractECFDriver;

public class LayoutDemonstration extends AbstractLayout implements ActionListener {

	private Timer timerRelatorioGerencial = new Timer( 2000, this );

	public LayoutDemonstration() {

		super();
	}

	public void loadMemory( File file ) {

	}

	private void printHead() {

		printCenter( memory.getEmpresa() );
		println( memory.getEndereco() );
		print( memory.getCidade() );
		printRight( memory.getTelefone() );
		SimpleDateFormat df = new SimpleDateFormat( "dd/MM/yyyy" );
		Date date = Calendar.getInstance().getTime();
		print( df.format( date ) );
		df = new SimpleDateFormat( "HH:mm:ss" );
		printRight( df.format( date ) );
		printline();
	}

	private void printFooter() {

		println();
		printline();
		SimpleDateFormat df = new SimpleDateFormat( "dd/MM/yyyy" );
		Date date = Calendar.getInstance().getTime();
		print( df.format( date ) );
		df = new SimpleDateFormat( "HH:mm:ss" );
		printRight( df.format( date ) );
		printline( "=" );
		for ( int i = 0; i < memory.getLinhasFim(); i++ ) {
			println();
		}
	}

	public void actionPerformed( ActionEvent e ) {

		if ( e.getSource() == timerRelatorioGerencial ) {
			if ( memory.relatorioGerencialAberto() ) {
				fechamentoRelatorioGerencial();
			}
		}
	}

	private String exit( String texto ) {

		System.out.print( "\n" + texto );
		return texto;
	}

	@Override
	public String resultAliquotas() {

		return memory.getAliquotas();
	}

	@Override
	public String programaFormaPagamento( String descricao ) {

		return formatZero( memory.programaFormaPagamento( descricao ), 2 );
	}

	@Override
	public String alteraSimboloMoeda( String simbolo ) {

		if ( simbolo != null ) {
			memory.setSimboloMoeda( simbolo.substring( 0, 4 ) );
		}
		return simbolo;
	}

	@Override
	public String aberturaDeCupom() {

		return aberturaDeCupom( null );
	}

	@Override
	public String aberturaDeCupom( String cnpj ) {

		printHead();
		if ( cnpj != null ) {
			println();
			print( "Cliente: " );
			print( formatString( cnpj, 39 ) );
			println();
			println();
		}
		printCenter( "CUPOM NAO FISCAL" );
		println( "ITEM   CODIGO             DESCRICAO" );
		print( "      QTDxUNITARIO" );
		printRight( "VALOR(" + memory.getSimboloMoeda() + ") " );
		printline( false );
		memory.abreCupom();
		return exit( printBuffer() );
	}

	@Override
	public String programaUnidadeMedida( String unidadeMedida ) {

		memory.setUnidadeMedida( formatString( unidadeMedida, 2 ) );
		return memory.getUnidadeMedida();
	}

	@Override
	public String vendaItem( String codProd, String descProd, String aliquota, char tpqtd, float qtd, float valor, char tpdesc, float desconto ) {

		print( formatString( formatZero( memory.rolaContatorItens(), 3 ) + " ", 4 ) );
		print( " " );
		print( formatString( codProd, 13 ) );
		print( " " );
		print( formatString( descProd, 29 ) );
		println();
		print( formatString( memory.getUnidadeMedida(), 2 ) );
		print( formatStringRigth( formatDecimal( qtd, 3 ), 7 ) );
		print( "x" );
		print( formatDecimal( valor, 2 ) );
		printRight( formatCurrency( valor * qtd ), false );
		memory.addSubTotalCupom( new BigDecimal( valor * qtd ) );
		return exit( printBuffer() );
	}

	@Override
	public String iniciaFechamentoCupom( char opt, float valor ) {

		BigDecimal acrescimo = null;
		BigDecimal desconto = null;
		if ( valor > 0 ) {
			switch ( opt ) {
			case AbstractECFDriver.ACRECIMO_PERC:
				acrescimo = new BigDecimal( ( memory.getSubTotalCupom().floatValue() / 100 ) * valor );
				break;
			case AbstractECFDriver.ACRECIMO_VALOR:
				acrescimo = new BigDecimal( valor );
				break;
			case AbstractECFDriver.DESCONTO_PERC:
				desconto = new BigDecimal( ( memory.getSubTotalCupom().floatValue() / 100 ) * valor );
				break;
			case AbstractECFDriver.DESCONTO_VALOR:
				desconto = new BigDecimal( valor );
				break;
			default:
				break;
			}
		}
		if ( acrescimo != null ) {
			printRight( formatStringRigth( repeat( "-", 20 ), siseLine ) );
			printRight( formatCurrency( memory.getSubTotalCupom() ) );
			print( "acréscimo" );
			printRight( formatCurrency( acrescimo ) );
			memory.addSubTotalCupom( acrescimo );
		}
		else if ( desconto != null ) {
			print( "desconto" );
			printRight( formatCurrency( desconto ) );
			memory.subtractSubTotalCupom( desconto );
		}
		printRight( formatStringRigth( repeat( "-", 20 ), siseLine ) );
		print( "TOTAL" );
		printRight( formatCurrency( memory.getSubTotalCupom() ), false );
		memory.iniciarFechamentoCupom();
		return exit( printBuffer() );
	}

	@Override
	public String efetuaFormaPagamento( String indice, float valor, String descForma ) {

		if ( !memory.fechamentoCupomIniciado() ) {
			iniciaFechamentoCupom( AbstractECFDriver.DESCONTO_PERC, 0 );
		}
		print( memory.getDescricaoFormaPagamento( Integer.parseInt( indice ) ) );
		printRight( formatCurrency( valor ), false );
		memory.addPagoCupom( new BigDecimal( valor ) );
		return exit( printBuffer() );
	}

	@Override
	public String finalizaFechamentoCupom( String mensagem ) {

		memory.terminaFechamentoCupom();
		if ( memory.getTrocoCupom().floatValue() > 0 ) {
			print( "troco" );
			printRight( formatCurrency( memory.getTrocoCupom() ), false );
		}
		if ( mensagem != null && mensagem.trim().length() > 0 ) {
			printline();
			print( formatString( mensagem, siseLine ) );
		}
		printFooter();
		return exit( printBuffer() );
	}

	@Override
	public String comprovanteNFiscalNVinculado( String opt, float valor, String formaPag ) {

		printHead();
		printCenter( "COMPROVANTE NAO FISCAL" );
		printCenter( "nao e documento fiscal" );
		println();
		if ( AbstractECFDriver.SUPRIMENTO.equals( opt ) ) {
			print( "SUPRIMENTO: " );
		}
		else if ( AbstractECFDriver.SANGRIA.equals( opt ) ) {
			print( "SANGRIA: " );
		}
		else {
			print( formaPag + ": " );
		}
		printRight( formatCurrency( new BigDecimal( valor ) ) );
		printFooter();
		return exit( printBuffer() );
	}

	@Override
	public String relatorioGerencial( String texto ) {

		if ( memory.relatorioGerencialAberto() ) {
			print( texto );
		}
		else {
			printHead();
			printCenter( "Relatorio Gerencial" );
			printCenter( "Nao  e  documento  fiscal" );
			println();
			print( texto );
			memory.abreRelatorioGerencial();
			timerRelatorioGerencial.start();
		}
		return exit( printBuffer() );
	}

	@Override
	public String fechamentoRelatorioGerencial() {

		if ( memory.relatorioGerencialAberto() ) {
			printFooter();
			memory.fechaRelatorioGerencial();
			timerRelatorioGerencial.stop();
		}
		return exit( printBuffer() );
	}
}
