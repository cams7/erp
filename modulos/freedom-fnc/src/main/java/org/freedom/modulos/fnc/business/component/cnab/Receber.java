package org.freedom.modulos.fnc.business.component.cnab;

import java.math.BigDecimal;
import java.util.Date;

import org.freedom.infra.functions.StringFunctions;

public class Receber {

	private int codrec;

	private int nrparcrec;

	private String docrec;

	private BigDecimal valor;

	private BigDecimal valorApagar;

	private Date emissao;

	private Date vencimento;

	private String conta;

	private String planejamento;

	private String centrocusto;

	private String code;

	private int codcliente;

	private String razcliente;
	
	private String status;
	
	private String obsitrec;

	public Receber() {

	}

	public Receber( int arg0, int arg1 ) {

		setCodrec( arg0 );
		setNrparcrec( arg1 );
	}

	
	public String getStatus() {
	
		return status;
	}

	
	public void setStatus( String status ) {
	
		this.status = status;
	}

	public Receber( String arg ) {

		this.code = arg;
		decode();
	}

	public int getCodrec() {

		return codrec;
	}

	public void setCodrec( int codrec ) {

		this.codrec = codrec;
	}

	public int getNrparcrec() {

		return nrparcrec;
	}

	public void setNrparcrec( int nrparcrec ) {

		this.nrparcrec = nrparcrec;
	}

	public String getConta() {

		return conta;
	}

	public void setConta( String conta ) {

		this.conta = conta;
	}

	public String getDocrec() {

		return docrec;
	}

	public void setDocrec( String docrec ) {

		this.docrec = docrec;
	}

	public Date getEmissao() {

		return emissao;
	}

	public void setEmissao( Date emissao ) {

		this.emissao = emissao;
	}

	public String getPlanejamento() {

		return planejamento;
	}

	public void setPlanejamento( String planejamento ) {

		this.planejamento = planejamento;
	}

	public String getCentrocusto() {

		return centrocusto;
	}

	public void setCentrocusto( String centrocusto ) {

		this.centrocusto = centrocusto;
	}

	public BigDecimal getValor() {

		return valor;
	}

	public void setValor( BigDecimal valor ) {

		this.valor = valor;
	}

	public BigDecimal getValorApagar() {

		return valorApagar;
	}

	public void setValorApagar( BigDecimal valorApagar ) {

		this.valorApagar = valorApagar;
	}

	public Date getVencimento() {

		return vencimento;
	}

	public void setVencimento( Date vencimento ) {

		this.vencimento = vencimento;
	}

	public int getCodcliente() {

		return codcliente;
	}

	public void setCodcliente( int codcliente ) {

		this.codcliente = codcliente;
	}

	public String getRazcliente() {

		return razcliente;
	}

	public void setRazcliente( String razcliente ) {

		this.razcliente = razcliente;
	}

	
	public String getObsitrec() {
	
		return obsitrec;
	}

	
	public void setObsitrec( String obsitrec ) {
	
		this.obsitrec = obsitrec;
	}

	public String encode() {

		this.code = StringFunctions.strZero( String.valueOf( getCodrec() ), 10 ) + StringFunctions.strZero( String.valueOf( getNrparcrec() ), 3 );

		return this.code;
	}

	public void decode() {

		if ( code != null && code.trim().length() > 10 ) {

			setCodrec( Integer.parseInt( code.substring( 0, 10 ) ) );
			setNrparcrec( Integer.parseInt( code.substring( 10 ) ) );
		}
	}

	public String toString() {

		return encode();
	}
}
