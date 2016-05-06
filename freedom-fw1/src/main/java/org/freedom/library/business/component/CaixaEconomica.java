package org.freedom.library.business.component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import javax.swing.text.DefaultFormatter;
import javax.swing.text.NumberFormatter;

public class CaixaEconomica extends Banco {

	// private final String CD_COBRANCA_SIMPLES = "11"; // Cobrança Simples

	// private final String CD_COBRANCA_RAPIDA = "12"; // Cobrança rápida

	private final String CD_COBRANCA_SR = "14"; // Cobrança sem registro

	// private final String CD_COBRANCA_DESCONTADA = "41"; // Cobrança
	// descontada

	// private final String SG_COBRANCA_SIMPLES = "CS"; // Sigla Cobrança
	// Simples

	// private final String SG_COBRANCA_RAPIDA = "CR"; // Sigla Cobrança rápida

	private final String SG_COBRANCA_SR = "SR"; // Cobrança sem registro

	// private final String SG_COBRANCA_DESCONTADA = "DE"; // Cobrança
	// descontada

	private final String BC_COBRANCA_SR = "80"; // Cobrança sem registro

	private final String CONSTANTE_CAMPO_LIVRE = "7"; // Constante para geração
	// do campo livre

	private String agencia = "";

	private String codcli = "";

	private String carteira = "14";

	private int moeda = 9; // Real

	private BigDecimal valortitulo = null;

	private String codigofornagencia = "";

	private String dvcodigofornagencia = "";

	private long fatvenc = 0;

	private String tpnossonumero = "D";
	
	private long doc = 0;
	
	private long seq = 0;
	
	private long nparc = 0;

	private long rec = 0;

	private String modalidade = "";

	private String convenio = "";
	
	private Date dtemit = null;

	public CaixaEconomica(String codbanco) {
		super();
	}
	
	public CaixaEconomica(String codbanco, String codmoeda, String dvbanco, Long fatvenc, BigDecimal vlrtitulo, String convenio, 
			String tpnossonumero, Long doc, Long seq, Long rec, Long nparc, final Date dtemit, String agencia, String conta, String carteira,
			String modalidade) {

		setMoeda(new Integer(codmoeda).intValue());
		setValorTitulo(vlrtitulo);
		setCodcli(convenio);
		setConvenio(convenio);
		setAgencia(agencia);
		setCarteira(carteira);
		setFatvenc(fatvenc);
		setTpnossonumero(tpnossonumero);
		setDoc(doc);
		setSeq(seq);
		setRec(rec);
		setNparc(nparc);
		setModalidade(modalidade);
		setDtemit(dtemit);
	}

	private String getCampoLivre() {

		String parte1 = getCodcli();
		String parte2 = getAgencia();
		String parte3 = getCarteiraBanco().substring(0, 1);
		String parte6 = getNossoNumero();
		String livre = parte1.substring(0, 5) + parte2.substring(0, 4) + parte3 + CONSTANTE_CAMPO_LIVRE + parte6;

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
	 * ver documentacao do banco para saber qual a ordem deste campo
	 */
	private String getCampo4() {

		String parte1 = getNumero();
		String parte2 = String.valueOf(getMoeda());
		String parte3 = String.valueOf(getFatvenc());
		String parte4 = getValorTitulo();
		String parte5 = getCampoLivre();

		String campo = parte1 + parte2 + parte3 + parte4 + parte5;
		String campo4 = getDigitoCodigoBarras(campo);

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

		String parte1 = getCampo1().substring(0, 5);
		System.out.println("PT1:" + parte1);
		String parte2 = getCampo1().substring(5);
		System.out.println("PT2:" + parte2);
		String parte3 = getCampo2().substring(0, 5);
		System.out.println("PT3:" + parte3);
		String parte4 = getCampo2().substring(5);
		System.out.println("PT4:" + parte4);
		String parte5 = getCampo3();
		System.out.println("PT5:" + parte5);
		String parte7 = getCampo4();
		System.out.println("PT7:" + parte7);
		String parte8 = getCampo5();
		System.out.println("PT8:" + parte8);

		String linhadig = parte1 + parte2 + parte3 + parte4 + parte5 + parte7 + parte8;

		System.out.println("LINHA DIG:" + linhadig);

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
		return ( getCarteiraBanco().substring(0, 1) ) + getNossoNumero() + "-" + getDvNossoNumero();
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

		String nn = "";

		nn = strZero(geraNossoNumero(getTpnossonumero(), getModalidade(), getConvenio(), getDoc(), getSeq(), getRec(), getNparc(), getDtemit()), 14);

		if (nn.length() > 14) {
			nn = nn.substring(0, 14);
		}

		return nn;
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

	public String getModalidade() {
		return modalidade;
	}

	public void setModalidade(String modalidade) {
		this.modalidade = modalidade;
	}

	public String getDvNossoNumero() {

		String dv = getModulo11(( getCarteiraBanco().substring(0, 1) ) + getNossoNumero(), 9);
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

		return numero;
	}

	public String geraCodBar(String codbanco, String codmoeda, String dvbanco, Long fatvenc, BigDecimal vlrtitulo, String convenio, 
			String tpnossonumero, Long doc, Long seq, Long rec, Long nparc, final Date dtemit, String agencia, String conta,
			String carteira, String modalidade) {

		setMoeda(new Integer(codmoeda).intValue());
		setValorTitulo(vlrtitulo);
		setCodcli(convenio);
		setConvenio(convenio);
		setTpnossonumero(tpnossonumero);
		setDoc(doc);
		setSeq(seq);
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
	// devem ser implementados para as demais carteiras.
	private String getCarteiraBanco() {

		String ret = "";

		if (CD_COBRANCA_SR.equals(getCarteira())) {
			ret = BC_COBRANCA_SR;
		}

		return ret;
	}

	// Retorna a sigla da carteira de cobrança no formato interno do banco,
	// devem ser implementados para as demais carteiras.
	public String getSiglaCarteiraBanco() {

		String ret = "";

		if (CD_COBRANCA_SR.equals(getCarteira())) {
			ret = SG_COBRANCA_SR;
		}

		return ret;
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
		return Banco.CAIXA_ECONOMICA;
	}

	public String geraNossoNumero(final String tpnossonumero, final String modalidade, final String convenio, 
			final Long doc, final Long seq, final Long rec, final Long nparc, final Date dtemit) {
		return geraNossoNumero(tpnossonumero, modalidade, convenio, doc, seq, rec, nparc, dtemit, true);
	}

	public String geraNossoNumero(final String tpnossonumero, final String modalidade, final String convenio, 
			final Long doc, final Long seq, final Long rec, final Long nparc, final Date dtemit, final boolean comdigito) {
		return geraNossoNumero(tpnossonumero, modalidade, convenio, doc, seq, rec, nparc, dtemit, comdigito, false);
	}

	public String geraNossoNumero(final String tpnossonumero, final String modalidade, final String convenio, 
			final Long doc, final Long seq, final Long rec, final Long nparc, final Date dtemit, final boolean comdigito, final boolean comtraco) {

		final StringBuffer retorno = new StringBuffer();

		retorno.append(getNumCli(tpnossonumero, modalidade, convenio, doc, seq, rec, nparc));

		return retorno.toString();
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