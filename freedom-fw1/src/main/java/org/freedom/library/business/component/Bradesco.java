package org.freedom.library.business.component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import javax.swing.text.DefaultFormatter;
import javax.swing.text.NumberFormatter;

import org.freedom.infra.functions.StringFunctions;

public class Bradesco extends Banco {

	private final String CONSTANTE_CAMPO_LIVRE = "0"; // Constante para geração
	// do campo livre

	private String agencia = "";

	private String conta = "";

	private String codcli = "";

	private String carteira = "09";

	private int moeda = 9; // Real

	private BigDecimal valortitulo = null;

	private String codigofornagencia = "";

	private String dvcodigofornagencia = "";

	private long fatvenc = 0;

	private long doc = 0;
	
	private long seq = 0;
	
	private long nparc = 0;

	private long rec = 0;

	private String tpnossonumero = "D";
	
	private String modalidade = "";

	private String convenio = "";
	
	private Date dtemit = null;

	public Bradesco() {
		super();
	}

	//Bradesco(String, String, String, Long, BigDecimal, String, String, 
	//	Long, Long, String, String, String, String
	
	public Bradesco(String codbanco, String codmoeda, String dvbanco, Long fatvenc, BigDecimal vlrtitulo, String tpnossonumero, String convenio,
			Long doc, Long seq, Long rec, Long nparc, final Date dtemit, String agencia, String contap, String carteira,
			String modalidade) {

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
		setDoc(doc);
		setSeq(seq);
		setModalidade(modalidade);
		setConta(contap);
		setDtemit(dtemit);
	}

	private String getCampoLivre() {

		String agencialimpa = getAgencia();
		String contalimpa = getConta();

		if (agencialimpa.indexOf("-") > 0) {
			agencialimpa = agencialimpa.substring(0, agencialimpa.indexOf("-"));
		}

		if (contalimpa.indexOf("-") > 0) {
			contalimpa = contalimpa.substring(0, contalimpa.indexOf("-"));
		}

		agencialimpa = StringFunctions.strZero(agencialimpa, 4);
		contalimpa = StringFunctions.strZero(contalimpa, 7);

		String parte1 = agencialimpa;

		String parte2 = StringFunctions.strZero(getCarteiraBanco(), 2);
		String parte3 = getNossoNumero();
		String parte4 = contalimpa;

		/*
		 * System.out.println("CL1:" + parte1); System.out.println("CL2:" +
		 * parte2); System.out.println("CL3:" + parte3);
		 * System.out.println("CL4:" + parte4);
		 */

		String livre = parte1 + parte2 + parte3 + parte4 + CONSTANTE_CAMPO_LIVRE;

		// System.out.println("campolivre:" + livre);

		return livre;
	}

	private String getCampo1() {

		String campo = getNumero() + String.valueOf(moeda) + getCampoLivre().substring(0, 5);

		return getDigitoCampo(campo, 2);
	}

	/**
	 * ver documentacao do banco para saber qual a ordem deste campo
	 */
	private String getCampo2() {

		String campo = getCampoLivre().substring(5, 15);
		return getDigitoCampo(campo, 1);
	}

	/**
	 * ver documentacao do banco para saber qual a ordem deste campo
	 */

	private String getCampo3() {

		String livre = getCampoLivre();
		String campo = livre.substring(15);
		String campo3 = getDigitoCampo(campo, 1);

		return campo3;
	}

	/**
	 * Dígito verificador do código de barras
	 */
	private String getCampo4() {

		String parte1 = getNumero(); // 01-03 --- Código do banco
		String parte2 = String.valueOf(getMoeda()); // 04-04 --- Código da moeda
		// (9-real)

		String parte4 = String.valueOf(getFatvenc()); // 06-09 --- Fator de
		// vencimento
		String parte5 = getValorTitulo(); // 10-19 --- Valor do documento
		String parte6 = getCampoLivre(); // 20-44 --- Campo Livre

		String campo = parte1 + parte2 + parte4 + parte5 + parte6;

		/*
		 * System.out.println("PARTE1 :" + parte1);
		 * System.out.println("PARTE2 :" + parte2);
		 * System.out.println("PARTE4 :" + parte4);
		 * System.out.println("PARTE5 :" + parte5);
		 * System.out.println("PARTE6 :" + parte6);
		 */

		// System.out.println("CAMPO :" + campo);

		String campo4 = getDigitoCodigoBarras(campo);

		// String campo4 = getModulo11( campo, 9 );

		return campo4;
	}

	/**
	 * ver documentacao do banco para saber qual a ordem deste campo
	 */
	private String getCampo5() {

		String campo = getFatvenc() + getValorTitulo();
		return campo;
	}

	/**
	 * Metodo para monta o desenho do codigo de barras A ordem destes campos
	 * tambem variam de banco para banco, entao e so olhar na documentacao do
	 * seu banco qual a ordem correta
	 */
	public String getCodigoBarras() {

		String parte1 = getNumero();
		String parte2 = String.valueOf(getMoeda());
		String parte3 = getCampo4();
		String parte4 = getCampo5();
		String parte5 = getCampoLivre();

		String codbarras = parte1 + parte2 + parte3 + parte4 + parte5;

		return codbarras;
	}

	public String geraLinhaDig() {

		String parte1 = getCampo1();
		// System.out.println( "PT1:" + parte1 );

		String parte2 = getCampo2();
		// System.out.println( "PT2:" + parte2 );

		String parte3 = getCampo3();
		// System.out.println( "PT3:" + parte3 );

		String parte4 = getCampo4();
		// System.out.println( "PT4:" + parte4 );

		String parte5 = getCampo5();
		// System.out.println( "PT5:" + parte5 );

		String linhadig = parte1 + parte2 + parte3 + parte4 + parte5;

		// System.out.println( "LINHA DIG:" + linhadig );

		return linhadig;
	}

	public String getLinhaDigFormatted() {

		String linha = geraLinhaDig();

		String pt1 = linha.substring(0, 5) + ".";
		String pt2 = linha.substring(6, 10) + " ";
		String pt3 = linha.substring(11, 15) + ".";
		String pt4 = linha.substring(16, 20) + " ";
		String pt5 = linha.substring(21, 25) + ".";
		String pt6 = linha.substring(26, 30) + " ";
		String pt7 = linha.substring(31, 32) + " ";
		String pt8 = linha.substring(33);

		String linhaformatada = pt1 + pt2 + pt3 + pt4 + pt5 + pt6 + pt7 + pt8;

		return linhaformatada;
	}

	/**
	 * Metodo que concatena os campo para formar a linha digitavel E necessario
	 * tambem olhar a documentacao do banco para saber a ordem correta a linha
	 * digitavel
	 */
	public String geraLinhaDig(String codbar, Long fatvenc, BigDecimal vlrtitulo) {
		return geraLinhaDig();
	}

	public String getNossoNumeroFormatted() {
		StringBuilder result = new StringBuilder();
		//result.append( getCarteiraBanco().substring(0, 1));
		String tmpNossoNumero = getNossoNumero();
		int tanNossoNumero = tmpNossoNumero.length();
		if (tanNossoNumero>10) {
			result.append(tmpNossoNumero.substring(tanNossoNumero-10));
		} else {
			result.append(tmpNossoNumero);
		}
		result.append("-");
		result.append(getDvNossoNumero());
		return result.toString();
	}

	@Override
	public String digVerif(String codigo, int modulo) {
		return null;
	}

	@Override
	public String digVerif(String codigo, int modulo, boolean digx) {
		return null;
	}

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
	
	public String getNumCli(String tpnossonumero, String modalidade, String convenio, Long doc, Long seq, Long rec, Long nparc) {

		final StringBuffer retorno = new StringBuffer();

		retorno.append(getNumCli(tpnossonumero, doc, seq, rec, nparc, 9));

		return retorno.toString();
	}

	/**
	 * Modulo 11 Retorna o digito verificador do codigo de barras (4o campo da
	 * linha digitavel. Voce deve passar como parametro a string do campo
	 * conforme o seu banco.
	 * 
	 * @return Modulo 11 - Retorna o digito verificador do codigo de barras (4o
	 *         campo da linha digitavel. Voce deve passar como parametro a
	 *         string do campo conforme o seu banco.
	 */
	public String getDigitoCodigoBarras(String campo) {

		// Esta rotina faz o calcula no modulo 11 - 23456789

		int multiplicador = 4;
		int multiplicacao = 0;
		int soma_campo = 0;

		for (int i = 0; i < campo.length(); i++) {
			multiplicacao = Integer.parseInt(campo.substring(i, 1 + i)) * multiplicador;
			soma_campo = soma_campo + multiplicacao;
			multiplicador = ( ( multiplicador + 5 ) % 8 ) + 2;
		}

		int dac = 11 - ( soma_campo % 11 );

		if (dac == 0 || dac == 1 || dac > 9) {
			dac = 1;
		}

		campo = String.valueOf(dac);

		return campo;
	}

	public String getNossoNumero() {

		String nn = strZero(geraNossoNumero(getTpnossonumero(), getModalidade(), getConvenio(), getDoc(), getSeq(), getRec(), getNparc(), getDtemit()), 11);

		// String nn = "11922200667";

		if (nn.length() > 11) {
			nn = nn.substring(0, 11);
		}

		return nn;
	}

	public String getTpnossonumero() {
		return tpnossonumero;
	}

	public void setTpnossonumero(String tpnossonumero) {
		this.tpnossonumero = tpnossonumero;
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

	public String getModalidade() {
		return modalidade;
	}

	public void setModalidade(String modalidade) {
		this.modalidade = modalidade;
	}

	public String getDvNossoNumero() {

		String dv = getModulo11(getCarteiraBanco() + getNossoNumero(), 7);
		// dv = "800000000002783";
		// dv = getModulo11( dv, 9 );
		return dv;

	}

	public String geraCodBar() {

		String parte1 = getNumero(); // 01-03 --- Código do banco
		String parte2 = String.valueOf(getMoeda()); // 04-04 --- Código da moeda
		// (9-real)
		String parte3 = String.valueOf(getCampo4()); // 05-05 --- Dígito
		// verificador geral do
		// código de barras
		String parte4 = String.valueOf(getFatvenc()); // 06-09 --- Fator de
		// vencimento
		String parte5 = getValorTitulo(); // 10-19 --- Valor do documento
		String parte6 = getCampoLivre(); // 20-44 --- Campo Livre

		String numero = parte1 + parte2 + parte3 + parte4 + parte5 + parte6;

		System.out.println("CODBAR:" + numero);

		return numero;
	}

	public String geraCodBar(String codbanco, String codmoeda, String dvbanco, Long fatvenc, BigDecimal vlrtitulo, String convenio, 
			String tpnossonumero, Long doc, Long seq, Long rec, Long nparc, final Date dtemit, String agencia, String conta,
			String carteira, String modalidade) {

		setMoeda(new Integer(codmoeda).intValue());
		setValorTitulo(vlrtitulo);
		setCodcli(convenio);
		setConvenio(convenio);
		setAgencia(agencia);
		setCarteira(carteira);
		setFatvenc(fatvenc);
		setDtemit(dtemit);

		return getNumero() + String.valueOf(getMoeda()) + String.valueOf(getCampo4()) + String.valueOf(getCampo5()) + getCampoLivre();
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
		return carteira.trim();
	}

	public void setCarteira(String carteira) {
		this.carteira = carteira;
	}

	public int getMoeda() {
		return moeda;
	}

	public void setMoeda(int moeda) {
		this.moeda = moeda;
	}

	public BigDecimal getValorTituloBigDecimal() {
		return this.valortitulo;
	}

	public String getValorTitulo() {

		String zeros = "0000000000";
		DefaultFormatter formatter = new NumberFormatter(new DecimalFormat("#,##0.00"));

		String valor = "";

		try {
			valor = formatter.valueToString(getValorTituloBigDecimal());
		}
		catch (Exception ex) {
		}

		valor = valor.replace(",", "").replace(".", "");
		String valorTitulo = zeros.substring(0, zeros.length() - valor.length()) + valor;

		return valorTitulo;
	}

	public void setValorTitulo(BigDecimal valortitulo) {
		this.valortitulo = valortitulo;
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

	public long getFatvenc() {
		return fatvenc;
	}

	public void setFatvenc(long fatvenc) {
		this.fatvenc = fatvenc;
	}

	// Retorna o codigo da carteira de cobrança no formato interno do banco,
	// (implementar caso seja diferente conforme CEF)
	// devem ser implementados para as demais carteiras.
	private String getCarteiraBanco() {

		return getCarteira();

	}

	public String getConvenio() {
		return convenio;
	}

	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	public Date getDtemit() {
		return dtemit;
	}

	public void setDtemit(Date dtemit) {
		this.dtemit = dtemit;
	}

	public String getNumero() {
		return Banco.BRADESCO;
	}
	
	/*
	 * 	public abstract String geraNossoNumero(final String tpnossonumero, final String modalidade, final String convenio, final Long doc, 
			final Long seq, final Long rec, final Long nparc);

	public abstract String geraNossoNumero(final String tpnossonumero, final String modalidade, final String convenio, final Long doc, 
			final Long seq, final Long rec, final Long nparc, final boolean comdigito);

	public abstract String geraNossoNumero(final String tpnossonumero, final String modalidade, final String convenio, final Long doc, 
			final Long seq, final Long rec, final Long nparc, final boolean comdigito, final boolean comtraco);*/
	

	public String geraNossoNumero(final String tpnossonumero, final String modalidade, final String convenio, final Long doc, final Long seq, final Long rec, final Long nparc, final Date dtemit) {
		return geraNossoNumero(tpnossonumero, modalidade, convenio, doc, seq, rec, nparc, dtemit, true);
	}

	public String geraNossoNumero(final String tpnossonumero, final String modalidade, final String convenio, final Long doc, final Long seq, final Long rec, final Long nparc, final Date dtemit, final boolean comdigito) {
		return geraNossoNumero(tpnossonumero, modalidade, convenio, doc, seq, rec, nparc, dtemit, comdigito, false);
	}

	public String geraNossoNumero(final String tpnossonumero, final String modalidade, final String convenio, final Long doc, final Long seq, final Long rec, final Long nparc, final Date dtemit, final boolean comdigito, final boolean comtraco) {

		final StringBuffer retorno = new StringBuffer();

		retorno.append(getNumCli(tpnossonumero, modalidade, convenio, doc, seq, rec, nparc));

		return retorno.toString();
	}

	public String getSiglaCarteiraBanco() {
		return StringFunctions.strZero(getCarteira().toString(), 2);
	}

	public String getModulo11(String campo, int type) {

		// Modulo 11 - 234567 (type = 7)
		// Modulo 11 - 23456789 (type = 9)

		int multiplicador = 2;
		int multiplicacao = 0;
		int soma_campo = 0;

		for (int i = campo.length(); i > 0; i--) {
			int vlrposicao = Integer.parseInt(campo.substring(i - 1, i));
			multiplicacao = vlrposicao * multiplicador;

			soma_campo = soma_campo + multiplicacao;

			multiplicador++;
			if (multiplicador > 7 && type == 7)
				multiplicador = 2;
			else if (multiplicador > 9 && type == 9)
				multiplicador = 2;
		}

		String dac = "0";

		if (soma_campo % 11 == 1) {
			dac = "P";
		}
		else if (soma_campo % 11 == 0) {
			dac = "0";
		}
		else {
			dac = new Integer(( 11 - ( soma_campo % 11 ) )).toString();
		}

		return dac;
	}

	@Override
	public String digVerif(String codigo) {
		// TODO Auto-generated method stub
		return null;
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
