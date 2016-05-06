
package org.freedom.ecf.app;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class PrinterMemory {

	// Parametros
	private String empresa;

	private String endereco;

	private String cidade;

	private String telefone;

	private int linhasFim = 6;

	private String unidadeMedida = "UN";

	private String simboloMoeda = "R$";

	private float[] aliquotas = new float[ 16 ];

	private String[] meiosPagamentos = new String[ 16 ];

	// Contadores e totais
	private int contatorItens = 0;

	private BigDecimal subTotalCupom = new BigDecimal( "0.00" );

	private BigDecimal pagoCupom = new BigDecimal( "0.00" );

	private BigDecimal trocoCupom = new BigDecimal( "0.00" );

	// Flags
	private boolean flagCupomIniciado = false;

	private boolean flagFechamentoCupomIniciado = false;

	private boolean flagRelatorioGerencial = false;

	public PrinterMemory() {

		meiosPagamentos[ 0 ] = "Dinheiro";
	}

	public String getEmpresa() {

		return empresa;
	}

	public void setEmpresa( String empresa ) {

		this.empresa = empresa;
	}

	public String getEndereco() {

		return endereco;
	}

	public void setEndereco( String endereco ) {

		this.endereco = endereco;
	}

	public String getCidade() {

		return cidade;
	}

	public void setCidade( String cidade ) {

		this.cidade = cidade;
	}

	public String getTelefone() {

		return telefone;
	}

	public void setTelefone( String telefone ) {

		this.telefone = telefone;
	}

	public int getLinhasFim() {

		return linhasFim;
	}

	public void setLinhasFim( int lineEnd ) {

		this.linhasFim = lineEnd;
	}

	public String getUnidadeMedida() {

		return unidadeMedida;
	}

	public void setUnidadeMedida( String unidadeMedida ) {

		this.unidadeMedida = unidadeMedida;
	}

	public String getSimboloMoeda() {

		return simboloMoeda;
	}

	public void setSimboloMoeda( String simboloMoeda ) {

		this.simboloMoeda = simboloMoeda;
	}

	public int lengthMeiosPagamentos() {

		return meiosPagamentos.length;
	}

	public float programaAliquota( float valor ) {

		float aliquota = -1;
		int menorIndice = 0;
		for ( int i = 0; i < aliquotas.length; i++ ) {
			if ( aliquotas[ i ] == valor ) {
				aliquota = aliquotas[ i ];
			}
			else if ( i < menorIndice ) {
				menorIndice = i;
			}
		}
		if ( aliquota == -1 ) {
			aliquotas[ menorIndice ] = valor;
			aliquota = valor;
		}
		return aliquota;
	}

	public String getAliquotas() {

		DecimalFormat df = new DecimalFormat( "00.00" );
		String aliquotas = "";
		for ( float aliquota : this.aliquotas ) {
			aliquotas += df.format( aliquota ).replace( ".", "" ).replace( ",", "" );
		}
		return aliquotas;
	}

	public int programaFormaPagamento( String descricao ) {

		int indice = -1;
		int menorIndice = 0;
		for ( int i = 0; i < meiosPagamentos.length; i++ ) {
			if ( meiosPagamentos[ i ] != null && meiosPagamentos[ i ].equals( descricao ) ) {
				indice = i;
			}
			else if ( i < menorIndice ) {
				menorIndice = i;
			}
		}
		if ( indice == -1 ) {
			meiosPagamentos[ menorIndice ] = descricao;
			indice = menorIndice;
		}
		return indice;
	}

	public String getDescricaoFormaPagamento( int indice ) {

		return ( indice > -1 && indice < 16 ) ? meiosPagamentos[ indice ] : null;
	}

	public int getContatorItens() {

		return contatorItens;
	}

	public int rolaContatorItens() {

		return this.contatorItens++;
	}

	public void addSubTotalCupom( BigDecimal subTotalCupom ) {

		if ( subTotalCupom != null ) {
			this.subTotalCupom = this.subTotalCupom.add( subTotalCupom );
		}
	}

	public void subtractSubTotalCupom( BigDecimal subTotalCupom ) {

		if ( subTotalCupom != null ) {
			this.subTotalCupom = this.subTotalCupom.subtract( subTotalCupom );
		}
	}

	public BigDecimal getSubTotalCupom() {

		return subTotalCupom;
	}

	public void addPagoCupom( BigDecimal pagoCupom ) {

		if ( pagoCupom != null ) {
			this.pagoCupom = this.pagoCupom.add( pagoCupom );
		}
	}

	public void subtractPagoCupom( BigDecimal pagoCupom ) {

		if ( pagoCupom != null ) {
			this.pagoCupom = this.pagoCupom.subtract( pagoCupom );
		}
	}

	public BigDecimal getTrocoCupom() {

		return trocoCupom;
	}

	public boolean cupomIniciado() {

		return flagCupomIniciado;
	}

	public boolean fechamentoCupomIniciado() {

		return flagFechamentoCupomIniciado;
	}

	public boolean relatorioGerencialAberto() {

		return flagRelatorioGerencial;
	}

	public void abreCupom() {

		this.contatorItens = 1;
		this.subTotalCupom = new BigDecimal( "0.00" );
		this.pagoCupom = new BigDecimal( "0.00" );
		this.trocoCupom = new BigDecimal( "0.00" );
		flagCupomIniciado = true;
	}

	public void iniciarFechamentoCupom() {

		flagFechamentoCupomIniciado = true;
	}

	public void terminaFechamentoCupom() {

		flagFechamentoCupomIniciado = false;
		trocoCupom = pagoCupom.subtract( subTotalCupom );
	}

	public void abreRelatorioGerencial() {

		flagRelatorioGerencial = true;
	}

	public void fechaRelatorioGerencial() {

		flagRelatorioGerencial = false;
	}
}
