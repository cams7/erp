package org.freedom.library.business.component;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class Hsbc extends Banco {
	
	private String agencia = "";
	private String conta = "";
	private String codcli = "";
	private String carteira = "";
	private int moeda = 9; // Real
	private String codigofornagencia = "";
	private String dvcodigofornagencia = "";
	private Long fatvenc = new Long(0);
	private long doc = 0;
	private long seq = 0;
	private long nparc = 0;
	private long rec = 0;
	private String tpnossonumero = "D";
	private String modalidade = "";
	private String convenio = "";
	private BigDecimal valorTitulo;
	private Date dtemit = null;
	// Boletos HSBC possuem 2 tipos de identificador para nosso número
	// 4 Vincula: "Vencimento", "código do cedente" e "código do documento";
	// 5 Vincula: "código do cedente" e "código do documento";
	// Utilizaremos o 5 com número sequencial ou número do documento nas parcelas.
	public static String TIPO_IDENT_VENCTO_SACADO_CEDENTE = "4";
	public static String TIPO_IDENT_SACADO_CEDENTE = "5";
	
	public Hsbc() {
		super();
	}
	
	public Hsbc(String codbanco, String codmoeda, String dvbanco, Long fatvenc,
			BigDecimal vlrtitulo, String tpnossonumero, String convenio,
			Long doc, Long seq, Long rec, Long nparc, final Date dtemit, String agencia, String contap, String carteira, String modalidade) {

		setMoeda(new Integer(codmoeda).intValue());
		setTpnossonumero(tpnossonumero);
		setValorTitulo(vlrtitulo);
		setCodcli(convenio);
		setConvenio(convenio);
		setAgencia(agencia);
		setCarteira(carteira);
		setFatvenc(fatvenc);
		setRec(rec);
		setNparc(nparc);
		setModalidade(modalidade);
		setConta(contap);
		setSeq(seq);
		setDoc(doc);
		setDtemit(dtemit);
	}

	public BigDecimal getValorTitulo() {
		return valorTitulo;
	}

	public void setValorTitulo(BigDecimal valorTitulo) {
		this.valorTitulo = valorTitulo;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getConta() {
		return conta;
	}

	public void setConta(String conta) {
		this.conta = conta;
	}

	public String getCodcli() {
		return codcli;
	}

	public void setCodcli(String codcli) {
		this.codcli = codcli;
	}

	public String getCarteira() {
		return carteira;
	}

	public void setCarteira(String carteira) {
		if ("CNR".equalsIgnoreCase(carteira)) {
			this.carteira = "2"; // Carteira CNR é tratada como "2" para boletos HSBC
		} else {
			this.carteira = carteira;
		}
	}

	public int getMoeda() {
		return moeda;
	}

	public void setMoeda(int moeda) {
		this.moeda = moeda;
	}

	public String getCodigofornagencia() {
		return codigofornagencia;
	}

	public void setCodigofornagencia(String codigofornagencia) {
		this.codigofornagencia = codigofornagencia;
	}

	public String getDvcodigofornagencia() {
		return dvcodigofornagencia;
	}

	public void setDvcodigofornagencia(String dvcodigofornagencia) {
		this.dvcodigofornagencia = dvcodigofornagencia;
	}

	public Long getFatvenc() {
		return fatvenc;
	}

	public void setFatvenc(Long fatvenc) {
		this.fatvenc = fatvenc;
	}

	public long getDoc() {
		return doc;
	}

	public void setDoc(long doc) {
		this.doc = doc;
	}

	public long getSeq() {
		return seq;
	}

	public void setSeq(long seq) {
		this.seq = seq;
	}

	public long getNparc() {
		return nparc;
	}

	public void setNparc(long nparc) {
		this.nparc = nparc;
	}

	public long getRec() {
		return rec;
	}

	public void setRec(long rec) {
		this.rec = rec;
	}

	public String getTpnossonumero() {
		return tpnossonumero;
	}

	public void setTpnossonumero(String tpnossonumero) {
		this.tpnossonumero = tpnossonumero;
	}

	public String getModalidade() {
		return modalidade;
	}

	public void setModalidade(String modalidade) {
		this.modalidade = modalidade;
	}

	public String getConvenio() {
		return convenio;
	}

	public void setConvenio(String convenio) {
		if (convenio==null) {
			this.convenio = null;
		} else {
			this.convenio = convenio.trim();
		}
	}

	public Date getDtemit() {
		return dtemit;
	}

	public void setDtemit(Date dtemit) {
		this.dtemit = dtemit;
	}

	@Override
	public String geraCodBar() {
		StringBuffer barcode = new StringBuffer();
		StringBuffer parte1 = new StringBuffer();
		StringBuffer parte2 = new StringBuffer();

		String bufCodbanco = strZero(HSBC, 3);
		String bufCodmoeda = strZero(String.valueOf(moeda), 1);
		
		String bufFatvenc = strZero(fatvenc.toString(), 4);
		String bufVlrtitulo = geraVlrtitulo(valorTitulo);
		
		// Código do cedente
		
		String bufConvenio = geraConvenio(convenio, HSBC);
		
		String bufModalidade = modalidade;
		
		String bufNossoNumero = geraNossoNumero(tpnossonumero, bufModalidade, bufConvenio, doc, seq, rec, nparc, dtemit, false);
		
		//String bufAgencia = strZero(getCodSig(agencia)[0], 4);
		//String bufConta = strZero(getCodSig(conta)[0], 5);
		
		String bufCarteira = null;
		if ("2".equals(getCarteira())) {
			bufCarteira = getCarteira();
		} else {
			bufCarteira = strZero(getCarteira(), 1);
		}
		// Seta o nosso número para utilização futura
		parte1.append(bufCodbanco);
		parte1.append(bufCodmoeda);
		
		parte2.append(bufFatvenc);
		parte2.append(bufVlrtitulo);
		parte2.append(bufConvenio); // Cedente
		parte2.append(bufNossoNumero);
		parte2.append(getDataJuliano(Integer.parseInt(bufFatvenc),bufModalidade));
		parte2.append(bufCarteira);
		
		barcode.append(parte1);
		barcode.append(digVerif(parte1.toString() + parte2.toString(), 11, true));
		barcode.append(parte2);
//		barcode.append(strZero("", 3));

		return barcode.toString();
	}
	
	private String getDataJuliano(int fatorvencto, String modalidade) {
		String result = null;
		if (TIPO_IDENT_SACADO_CEDENTE.equals(modalidade)) {
			result = "0000";
		} else if (TIPO_IDENT_VENCTO_SACADO_CEDENTE.equals(modalidade)) {
			Calendar vencto = getDataFatorVenc(fatorvencto);
			int dataJuliano = vencto.get(Calendar.DAY_OF_YEAR);
			int anoJuliano = vencto.get(Calendar.YEAR);
			result = getDataJuliano(dataJuliano, anoJuliano);
			//System.out.println(dataJuliano);
			//System.out.println(anoJuliano);
			//System.out.println("Criar tratamento para data no formato Juliano !!!! ");
		}
		return result;
	}
	
	private String getDataJuliano(int dataJuliano, int anoJuliano) {
		StringBuffer result = new StringBuffer();
		result.append(strZero(String.valueOf(dataJuliano),3));
		result.append(strZero(String.valueOf(anoJuliano),4).substring(3));
		return result.toString();
	}
	
	public long getFatorVencimento(Date data) {
		long result = 0;
		Calendar calc = Calendar.getInstance();
		Calendar dtbanc = Calendar.getInstance();
	    dtbanc.set(1997, 9, 7, 0, 0, 0);
	  //  System.out.println(result.getTime());

		calc.setTime(data);
		calc.set(Calendar.HOUR, 0);
		calc.set(Calendar.MINUTE,0);
		calc.set(Calendar.SECOND,0);
		calc.set(Calendar.MILLISECOND,0);
		result = calc.getTime().getTime()-dtbanc.getTime().getTime();
		result = result / 1000 / 60 / 60 / 24;
//		calc.add(//field, amount)
		
		return result;
	}
	public Calendar getDataFatorVenc(int fatorvencto) {
	    Calendar result = Calendar.getInstance();
	    result.set(1997, 9, 7, 0, 0, 0);
	  //  System.out.println(result.getTime());
        result.add(Calendar.DATE , fatorvencto);
	   // System.out.println(result.getTime());
	    return result;
	}
	
	@Override
	public String geraCodBar(String codbanco, String codmoeda, String dvbanco,
			Long fatvenc, BigDecimal vlrtitulo, String convenio,
			String tpnossonumero, Long doc, Long seq, Long rec, Long nparc, final Date dtemit,
			String agencia, String conta, String carteira, String modalidade) {
		
		setMoeda(new Integer(codmoeda).intValue());
		setTpnossonumero(tpnossonumero);
		setValorTitulo(vlrtitulo);
		setCodcli(convenio);
		setConvenio(convenio);
		setAgencia(agencia);
		setCarteira(carteira);
		setFatvenc(fatvenc);
		setRec(rec);
		setNparc(nparc);
		setModalidade(modalidade);
		setConta(conta);
		setSeq(seq);
		setDoc(doc);
		setDtemit(dtemit);

		String barcode = geraCodBar();
		return barcode;
	}
	
	@Override
	public String geraLinhaDig(String codbar, Long fatvenc, BigDecimal vlrtitulo) {
		StringBuilder linhaDig = new StringBuilder();
		
		String bufCodbanco = codbar.substring(0, 3);
		String bufCodmoeda = codbar.substring(3, 4);
		String bufConvenio = codbar.substring(19, 26); // Convênio/cedente
		String bufNossoNumero = codbar.substring(26, 39); // Nosso número 13 dígitos
		String bufVenctoJuliano = codbar.substring(39, 43); // Data no formato Juliano
		
		String bufCarteira = codbar.substring(43, 44);
		
		String bufFatvenc = strZero(fatvenc.toString(), 4);
		String bufVlrtitulo = codbar.substring(9,19);
		//String bufVlrtitulo = geraVlrtitulo(vlrtitulo);
		String campo1 = bufCodbanco + bufCodmoeda + bufConvenio.substring(0,5);
		
		String campo2 =  bufConvenio.substring(5);
		campo2 = campo2 + bufNossoNumero.substring(0, 8);

		String campo3 = bufNossoNumero.substring(8, 13);
		campo3 = campo3 + bufVenctoJuliano;
		campo3 = campo3 + bufCarteira; // Produto 2 carteira CNR
		
		String campo4 = codbar.substring(4, 5); // Digito verificador do código de barras DAC
		
		String campo5 = bufFatvenc + bufVlrtitulo;
		
		linhaDig.append(campo1.substring(0,5));
		linhaDig.append(".");
		linhaDig.append(campo1.substring(5,9));
		linhaDig.append(digVerif(campo1, 10));
		linhaDig.append(" ");
		
		linhaDig.append(campo2.substring(0,5));
		linhaDig.append(".");
		linhaDig.append(campo2.substring(5,10));
		linhaDig.append(digVerif(campo2, 10));
		linhaDig.append(" ");
		
		linhaDig.append(campo3.substring(0,5));
		linhaDig.append(".");
		linhaDig.append(campo3.substring(5,10));
		linhaDig.append(digVerif(campo3, 10));
		linhaDig.append(" ");
		
		linhaDig.append(campo4);
		linhaDig.append(" ");
		
		linhaDig.append(campo5);
		
		return linhaDig.toString();
	}

	@Override
	public String geraNossoNumero(String tpnossonumero, String modalidade,
			String convenio, Long doc, Long seq, Long rec, Long nparc, final Date dtemit) {
		return geraNossoNumero(tpnossonumero, modalidade, convenio, doc, seq, rec, nparc, dtemit, true);
	}

	@Override
	public String geraNossoNumero(String tpnossonumero, String modalidade,
			String convenio, Long doc, Long seq, Long rec, Long nparc, final Date dtvencto,
			boolean comdigito) {
		return geraNossoNumero(tpnossonumero, modalidade, convenio, doc, seq, rec, nparc, dtemit, comdigito, false);
	}

	@Override
	public String geraNossoNumero(String tpnossonumero, String modalidade,
			String convenio, Long doc, Long seq, Long rec, Long nparc, final Date dtvencto,
			boolean comdigito, boolean comtraco) {
		
		// Modalidade igual ao tipo de identificação.
		
		StringBuffer retorno = new StringBuffer();
		StringBuffer tmpretorno = new StringBuffer();
		retorno.append(getNumCli(tpnossonumero, modalidade, convenio, doc, seq, rec, nparc));

		if (comdigito) {
			retorno.append(digVerif(retorno.toString(), 11));
			retorno.append(modalidade);
			if (TIPO_IDENT_VENCTO_SACADO_CEDENTE.equals(modalidade)) {
				tmpretorno = somaSacadoCedente(retorno.toString(), convenio.toString(), dtvencto);
			} else {
				tmpretorno = somaSacadoCedente(retorno.toString(), convenio.toString());
			}
			
			retorno.append(digVerif(tmpretorno.toString(), 11));
		} 
		
		return strZero(retorno.toString(),13);
	}
	
	public static String getNumCli(String tpnossonumero, Long doc, Long seq, Long rec, Long nparc, int tam) {

		final StringBuffer retorno = new StringBuffer();
		final Long numcli = geraNumcli(tpnossonumero, doc, seq, rec);
        int reduznparc = 3;
        if ( "S".equals(tpnossonumero) ) {
        	reduznparc = 0;
        }
        
		if (numcli == null) { 
			retorno.append(strZero("0", tam - reduznparc));
		} else if (numcli.toString().length() > tam - reduznparc) {
			// Remover caracteres a mais da esquerda para direita
			retorno.append(numcli.toString().substring(numcli.toString().length() - ( tam - reduznparc )));
		} else {
			retorno.append(numcli.toString());
		}

		if (reduznparc==3) {
			if (nparc == null) {
				retorno.append("000");
			} else {
				retorno.append(strZero(nparc.toString(), reduznparc));
			}
		}
		return retorno.toString();
	}

	private StringBuffer somaSacadoCedente(String sacado, String cedente) {
		return somaSacadoCedente(sacado, cedente, null);
	}
	
	private StringBuffer somaSacadoCedente(String sacado, String cedente, Date dtvencto) {
		StringBuffer result = new StringBuffer();
		long lsacado = Long.parseLong(sacado);
		long lcedente = Long.parseLong(cedente);
		long lvencto = 0;
		if (dtvencto != null) {
			lvencto = Long.parseLong(getStrDateDDMMAA(dtvencto));
		}
		long lresult = lsacado+lcedente+lvencto;
		result.append(lresult);
		return result;
	}
	
	private String getStrDateDDMMAA(Date dta) {
		StringBuilder result = new StringBuilder();
		Calendar cld = Calendar.getInstance();
		cld.setTime(dta);
		result.append(strZero(String.valueOf(cld.get(Calendar.DAY_OF_MONTH)),2));
		result.append(strZero(String.valueOf(cld.get(Calendar.MONTH)+1),2));
		result.append(strZero(String.valueOf(cld.get(Calendar.YEAR)),4).substring(2));
		return result.toString();
	}
	
	public String getNossoNumero() {
		return geraNossoNumero(getTpnossonumero(), getModalidade(), getConvenio(), getDoc(), getSeq(), getRec(), getNparc(), getDtemit());
	}
	
	@Override
	public String getNossoNumeroFormatted() {
		return super.getNossoNumeroFormatted();
	}

	@Override
	public String[] getCodSig(String codigo) {
		final String[] retorno = new String[2];
		final StringBuffer buffer = new StringBuffer();
		final String valido = "0123456789X";

		for (int i = 0; i < codigo.length(); i++) {

			if (valido.indexOf(codigo.charAt(i)) > -1) {
				buffer.append(codigo.charAt(i));
			}
			else if (codigo.charAt(i) == '-') {
				retorno[0] = buffer.toString();
				buffer.delete(0, buffer.length());
			}
		}
		if (retorno[0] == null) {
			retorno[0] = buffer.toString();
			retorno[1] = "";
		}
		else {
			retorno[1] = buffer.toString();
		}

		return retorno;
	}
	
	@Override
	public String getNumCli(String tpnossonumero, String modalidade, String convenio, Long doc, Long seq, Long rec, Long nparc) {

		// Modalidade é igual ao tipo de identificador.
		StringBuffer retorno = new StringBuffer();

		retorno.append(getNumCli(tpnossonumero, doc, seq, rec, nparc, 13));

		return retorno.toString();
	}

	@Override
	public String digVerif(String codigo) {
		return digVerif( getCodSig(getAgencia())[0] + getCodSig(getConta())[0] + getCarteira() + codigo, 10);
	}
	
	@Override
	public String digVerif(String codigo, int modulo) {
		return digVerif(codigo, modulo, false);
	}

	@Override
	public String digVerif(String codigo, int modulo, boolean crescente) {

		int[] peso;

		if (modulo == 10) {
			peso = new int[2];
			peso[0] = 2;
			peso[1] = 1;
		} else {
			if (crescente) {
				peso = new int[8];
				for (int i = 1; i <= peso.length;  i++) {
					peso[i-1] =  i + 1;
				}
			} else {
				peso = new int[8];
				for (int i = peso.length - 1; i > -1; i--) {
					peso[i] = peso.length - ( i - 1 );
				}
			}
		}

		int soma = 0;
		int calc = 0;
		int posi = 0;
		int resto = 0;
		String str = "";
		String dig = null;

		for (int i = codigo.length() - 1; i > -1; i--) {
			if (modulo == 11) {
				calc = Integer.parseInt(codigo.substring(i, i + 1)) * ( peso[posi] );
			} else {
				// transforma o valor do produto em String
				str = String.valueOf(Integer.parseInt(codigo.substring(i, i + 1)) * ( peso[posi] ));
				// soma os valores. Exemplo: para resultado do produto = 14
				// (1+4=5).
				calc = 0;
				for (int s = 0; s < str.length(); s++) {
					calc += Integer.parseInt(str.substring(s, s + 1));
				}
			}
			soma += calc;
			posi++;
			if (posi >= peso.length) {
				posi = 0;
			}
		}

		resto = soma % modulo;

		if (modulo == 10) {
			if (resto==0) {
				dig = "0";
			} else {
				dig = String.valueOf(modulo - resto);
			}
		} else {
			if (crescente) {
				if (resto==0 || resto==1 || resto==10) {
					dig = "1";
				} else {
					dig = String.valueOf(modulo-resto);
				}
			} else {
				if (resto==10) {
					dig = "0";
				}
				else {
					dig = String.valueOf(resto);
				}
			}
		}
		
		return dig;
	}

	@Override
	public String formatNossonumero(String nossonumero) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPosto() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String setPosto(String posto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String formatConvenio(String convenio) {
		// TODO Auto-generated method stub
		return null;
	}

}
