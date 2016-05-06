/**
 * @version 19/03/2005 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 * Projeto: Freedom <BR>
 * 
 * Pacote: org.freedom.comutacao <BR>
 * Classe:
 * @(#)TesteBema.java <BR>
 * 
 * Este programa é licenciado de acordo com a LPG-PC (Licença Pública Geral para
 * Programas de Computador), <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * A LPG-PC deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste
 * Programa. <BR>
 * Caso uma cópia da LPG-PC não esteja disponível junto com este Programa, você
 * pode contatar <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa é
 * preciso estar <BR>
 * de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * Classe de interface com TEF.
 *  
 */

package org.freedom.comutacao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.frame.Aplicativo;

public class Tef {
	boolean bAtivo = false;

	File fTmp;

	File fEnvio;

	File fStatus;

	File fRetorno;

	File fAtivo;

	static final String ARQ_TMP = "IntPos.tmp";

	static final String ARQ_ENVIO = "IntPos.001";

	static final String ARQ_STATUS = "intpos.sts";

	static final String ARQ_RETORNO = "intpos.001";

	static final String ARQ_ATIVO = "ativo.001";

	static final String TEF_HEADER = "000-000";

	static final String TEF_IDENTIFICACAO = "001-000";

	static final String TEF_DOC_FISCAL = "002-000";

	static final String TEF_VAL_TOTAL = "003-000";

	static final String TEF_ST_TRANSACAO = "009-000";

	static final String TEF_NOME_REDE = "010-000";

	static final String TEF_TIPO_TRANSACAO = "011-000";

	static final String TEF_NSU = "012-000";

	static final String TEF_AUTORIZACAO = "013-000";

	static final String TEF_LOTE_TRANSACAO = "014-000";

	static final String TEF_TIM_SERVER = "015-000";

	static final String TEF_TIM_LOCAL = "016-000";

	static final String TEF_DT_COMPROVANTE = "022-000";

	static final String TEF_HR_COMPROVANTE = "023-000";

	static final String TEF_NSU_CANCELADO = "025-000";

	static final String TEF_FINALIZACAO = "027-000";

	static final String TEF_QTD_LINHAS = "028-000";

	static final String IMP_BASE = "029-"; // Base para impressao do comprovante

	// (somar esta mais 3 digitos):
	// 029-001,029-002...

	static final String TEF_MSG_OPERADOR = "030-000";

	static final String TEF_ADMINISTRADORA = "040-000";

	static final String TEF_EOT = "999-999"; // "E"nd "O"f "T"rasaction. (final

	// do arquivo)

	private long lIdentUniq = 1;

	public Tef(String sPathEnv, String sPathRet) {
		Properties p;
		fTmp = new File(sPathEnv + "/" + ARQ_TMP);
		fEnvio = new File(sPathEnv + "/" + ARQ_ENVIO);
		fStatus = new File(sPathRet + "/" + ARQ_STATUS);
		fRetorno = new File(sPathRet + "/" + ARQ_RETORNO);
		fAtivo = new File(sPathRet + "/" + ARQ_ATIVO);

		if (!verifTef()) {
			Funcoes.mensagemInforma(null, "Gerenciador Padrão de TEF não está ativo!");
		}
		else if (existeInfo("CRT", fRetorno, 1, 0)) {
			p = leRetorno();
			if (p != null)
				naoConfirmaVenda(p);
		}

	}

	public boolean enviaArquivo(String sConteudo[]) {
		boolean bRet = false;

		if (fTmp.exists())
			fTmp.delete();
		try {
			PrintStream psEnvio = new PrintStream(new FileOutputStream(fTmp));
			for (int i = 0; i < sConteudo.length; i++)
				psEnvio.println(sConteudo[i]);
			psEnvio.println(TEF_EOT + " = 0");
			psEnvio.close();
			fTmp.renameTo(fEnvio);
			bRet = true;
		}
		catch (IOException err) {
			Funcoes.mensagemErro(null, "Não foi possível gravar o arquivo temporário de TEF!");
			err.printStackTrace();
		}
		return bRet;
	}

	public boolean verifTef() {
		boolean bRet = false;

		if (!fAtivo.exists() || !fAtivo.canRead())
			return false;

		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream(fAtivo));
			if (prop.getProperty(TEF_HEADER, "").equals("TEF"))
				bRet = true;
			prop.clear();
		}
		catch (IOException err) {
			Funcoes.mensagemErro(null, "Não foi possível verificar se o módulo TEF está ativo!");
			err.printStackTrace();
		}
		return bRet;
	}

	public boolean existeStatus(String sCab, long lIdent) {
		// 7 segundos é padrao do GP.
		boolean bRet = existeInfo(sCab, fStatus, 7, ( lIdent > 0 ? lIdent : 0 ));

		if (!bRet)
			Funcoes.mensagemInforma(null, "Gerenciador Padrão de TEF não está ativo!");

		return bRet;
	}

	public boolean existeRetorno(String sCab, long lIdent) {
		return existeInfo(sCab, fRetorno, 180, ( lIdent > 0 ? lIdent : 0 ));
	}

	public boolean existeInfo(String sCab, File fArq, int iTentativas, long lIdent) {
		// Cada tentiva leva por volta de 1 segundo.
		// String sLinha;
		boolean bRet = false;
		int iConta = 1;
		while (true) {
			try {
				Thread.sleep(1000);
			}
			catch (Exception err) {
				break;
			}
			if (fArq.exists() && fArq.canRead()) {
				try {
					Properties prop = new Properties();
					FileInputStream fis = new FileInputStream(fArq);
					prop.load(fis);
					fis.close();
					// Se for necessário identificação o lIdent será maior a 0;
					if (prop.getProperty(TEF_HEADER, "").equals(sCab) && lIdent > 0 && prop.getProperty(TEF_IDENTIFICACAO, "").equals("" + lIdent))
						bRet = true;
					// Se não for necessário identificação o lIdent será igual a
					// 0;
					else if (prop.getProperty(TEF_HEADER, "").equals(sCab) && lIdent == 0)
						bRet = true;
					prop.clear();
				}
				catch (IOException err) {
					Funcoes.mensagemErro(null, "Não foi possível ler o retorno da TEF!");
					err.printStackTrace();
				}
			}
			if (!bRet && iConta < iTentativas)
				iConta++;
			else
				break;
		}
		return bRet;
	}

	private Properties leRetorno() {
		Properties pRet = null;
		try {
			FileInputStream fis = new FileInputStream(fRetorno);
			pRet = new Properties();
			pRet.load(fis);
			fis.close();
			// Se não existe linhas para imprimir então apaga o arquivo,
			// caso contrário só irá apagar o arquivo na confirmação.
			if (Integer.parseInt(pRet.getProperty(TEF_QTD_LINHAS, "0")) == 0) {
				fRetorno.delete();
			}
			fStatus.delete();
		}
		catch (Exception err) {
			Funcoes.mensagemErro(null, "Não foi possível carregar o retorno da TEF!");
			err.printStackTrace();
		}
		return pRet;
	}

	private void confirmaRetorno() {
		fRetorno.delete();
	}

	public Properties solicVenda(int iNumCupom, BigDecimal bigVal) {
		// String pRet = null;
		boolean bRet;
		// int iConta;
		if (!verifTef())
			return null;
		// Pega uma identificação e já deixa outra disponível;
		long lIdent = this.lIdentUniq++;

		bRet = enviaArquivo(new String[] { ( TEF_HEADER + " = " + "CRT" ), ( TEF_IDENTIFICACAO + " = " + lIdent ), ( TEF_DOC_FISCAL + " = " + iNumCupom ),
				( TEF_VAL_TOTAL + " = " + ( Funcoes.transValor(bigVal, 12, 2, false) ).trim() ) });
		if (!bRet || !existeStatus("CRT", lIdent) || !existeRetorno("CRT", lIdent))
			return null;

		return leRetorno();
	}

	public Properties solicCancelamento(String sNSU, String sRede, Date dTrans, BigDecimal bigVal) {
		// String pRet = null;
		boolean bRet;
		// int iConta;
		if (!verifTef())
			return null;
		// Pega uma identificação e já deixa outra disponível;
		long lIdent = this.lIdentUniq++;

		bRet = enviaArquivo(new String[] { ( TEF_HEADER + " = " + "CNC" ), ( TEF_IDENTIFICACAO + " = " + lIdent ), ( TEF_VAL_TOTAL + " = " + Funcoes.transValor(bigVal, 12, 2, false) ),
				( TEF_NOME_REDE + " = " + sRede ), ( TEF_NSU + " = " + sNSU ), ( TEF_DT_COMPROVANTE + " = " + ( new SimpleDateFormat("ddMMyyyy") ).format(dTrans) ),
				( TEF_HR_COMPROVANTE + " = " + ( new SimpleDateFormat("HHmmss") ).format(dTrans) ) });
		if (!bRet || !existeStatus("CNC", lIdent) || !existeRetorno("CNC", lIdent))
			return null;

		return leRetorno();
	}

	public boolean confirmaCNF(Properties prop) {
		// String pRet = null;
		boolean bRet;
		// int iConta;
		if (!verifTef())
			return false;
		confirmaRetorno();
		bRet = enviaArquivo(new String[] { ( TEF_HEADER + " = " + "CNF" ), ( TEF_IDENTIFICACAO + " = " + prop.getProperty(TEF_IDENTIFICACAO) ),
				( TEF_NOME_REDE + " = " + prop.getProperty(TEF_NOME_REDE) ), ( TEF_NSU + " = " + prop.getProperty(TEF_NSU) ), ( TEF_FINALIZACAO + " = " + prop.getProperty(TEF_FINALIZACAO) ) });
		if (!bRet || !existeStatus("CNF", Long.parseLong(prop.getProperty(TEF_IDENTIFICACAO, "0"))))
			return false;

		fStatus.delete();

		return bRet;
	}

	public boolean confirmaVenda(Properties prop) {
		// String pRet = null;
		boolean bRet;
		// int iConta;
		if (!verifTef())
			return false;
		confirmaRetorno();
		bRet = enviaArquivo(new String[] { ( TEF_HEADER + " = " + "CNF" ), ( TEF_IDENTIFICACAO + " = " + prop.getProperty(TEF_DOC_FISCAL) ),
				( TEF_DOC_FISCAL + " = " + prop.getProperty(TEF_DOC_FISCAL) ), ( TEF_NOME_REDE + " = " + prop.getProperty(TEF_NOME_REDE) ), ( TEF_NSU + " = " + prop.getProperty(TEF_NSU) ),
				( TEF_FINALIZACAO + " = " + prop.getProperty(TEF_FINALIZACAO) ) });
		if (!bRet || !existeStatus("CNF", Long.parseLong(prop.getProperty(TEF_DOC_FISCAL, "0"))))
			return false;

		fStatus.delete();

		return bRet;
	}

	public boolean naoConfirmaVenda(Properties prop) {
		// String pRet = null;
		boolean bRet;
		// int iConta;
		if (!verifTef())
			return false;

		confirmaRetorno();

		bRet = enviaArquivo(new String[] { ( TEF_HEADER + " = " + "NCN" ), ( TEF_IDENTIFICACAO + " = " + prop.getProperty(TEF_DOC_FISCAL) ),
				( TEF_DOC_FISCAL + " = " + prop.getProperty(TEF_DOC_FISCAL) ), ( TEF_NOME_REDE + " = " + prop.getProperty(TEF_NOME_REDE) ), ( TEF_NSU + " = " + prop.getProperty(TEF_NSU) ),
				( TEF_FINALIZACAO + " = " + prop.getProperty(TEF_FINALIZACAO) ) });
		if (!bRet || !existeStatus("NCN", Long.parseLong(prop.getProperty(TEF_DOC_FISCAL))))
			return false;

		Funcoes.mensagemErro(null, "Cancelada a Transação:\n" + "Doc No: " + prop.getProperty(TEF_DOC_FISCAL) + "\n" + "Rede: " + prop.getProperty(TEF_NOME_REDE) + "\n" + "Valor: "
				+ Funcoes.strDecimalToStrCurrency(2, Funcoes.transValorInv(prop.getProperty(TEF_VAL_TOTAL, "000")) + ""));

		fStatus.delete();

		return bRet;
	}

	public boolean naoConfirmaCNF(Properties prop) {
		// String pRet = null;
		boolean bRet;
		// int iConta;
		if (!verifTef())
			return false;

		confirmaRetorno();

		bRet = enviaArquivo(new String[] { ( TEF_HEADER + " = " + "NCN" ), ( TEF_IDENTIFICACAO + " = " + prop.getProperty(TEF_DOC_FISCAL) ),
				( TEF_NOME_REDE + " = " + prop.getProperty(TEF_NOME_REDE) ), ( TEF_NSU + " = " + prop.getProperty(TEF_NSU) ), ( TEF_FINALIZACAO + " = " + prop.getProperty(TEF_FINALIZACAO) ) });
		if (!bRet || !existeStatus("NCN", Long.parseLong(prop.getProperty(TEF_IDENTIFICACAO))))
			return false;

		Funcoes.mensagemErro(null, "Cancelada a Transação:\n" + "Rede: " + prop.getProperty(TEF_NOME_REDE) + "\n" + "NSU: " + prop.getProperty(TEF_NSU) + "\n" + "Valor: "
				+ Funcoes.strDecimalToStrCurrency(2, Funcoes.transValorInv(prop.getProperty(TEF_VAL_TOTAL, "000")) + ""));

		fStatus.delete();

		return bRet;
	}

	public Properties solicAdm() {
		// String pRet = null;
		boolean bRet;
		// int iConta;
		if (!verifTef())
			return null;

		// Pega uma identificação e já deixa outra disponível;
		long lIdent = this.lIdentUniq++;

		bRet = enviaArquivo(new String[] { ( TEF_HEADER + " = " + "ADM" ), ( TEF_IDENTIFICACAO + " = " + lIdent ) });

		if (!bRet || !existeStatus("ADM", lIdent) || !existeRetorno("ADM", lIdent))
			return null;

		return leRetorno();
	}

	public boolean validaTef(final Properties prop) {
		boolean bRet = false;
		if (!prop.getProperty(TEF_ST_TRANSACAO, "").equals("0")) {
			bRet = false;
		}
		else
			bRet = true;
		if (!prop.getProperty(TEF_MSG_OPERADOR, "").equals("")) {
			if (Integer.parseInt(prop.getProperty(TEF_QTD_LINHAS, "0")) > 0)
				Funcoes.mensagemTemp(Aplicativo.telaPrincipal, prop.getProperty(TEF_MSG_OPERADOR), "TEF", 5);
			else
				Funcoes.mensagemInforma(null, prop.getProperty(TEF_MSG_OPERADOR));
		}
		return bRet;
	}

	public static String retNsu(Properties prop) {
		// Se for uma transação de cancelamento busca primeiro o NSU cancelado:
		String sRet = prop.getProperty(TEF_NSU_CANCELADO);
		if (sRet == null)
			sRet = prop.getProperty(TEF_NSU);
		return sRet;
	}

	public static String retRede(Properties prop) {
		String sRet = prop.getProperty(TEF_NOME_REDE);
		return sRet;
	}

	public static Date retData(Properties prop) {
		DateFormat df = new SimpleDateFormat("ddMMyyyy");
		Date dRet = null;
		try {
			dRet = df.parse(prop.getProperty(TEF_DT_COMPROVANTE));
		}
		catch (Exception err) {
		}
		return dRet;
	}

	public static Date retDataHora(Properties prop) {
		DateFormat df = new SimpleDateFormat("ddMMHHmmss");
		Date dRet = null;
		try {
			dRet = df.parse(prop.getProperty(TEF_DT_COMPROVANTE));
		}
		catch (Exception err) {
		}
		return dRet;
	}

	public static Date retHora(Properties prop) {
		DateFormat df = new SimpleDateFormat("HHmmss");
		Date dRet = null;
		try {
			dRet = df.parse(prop.getProperty(TEF_DT_COMPROVANTE));
		}
		catch (Exception err) {
		}
		return dRet;
	}

	public static BigDecimal retValor(Properties prop) {
		return Funcoes.transValorInv(prop.getProperty(TEF_VAL_TOTAL, "000"));
	}

	public Object[] retImpTef(Properties prop) {
		Vector<String> vRet = new Vector<String>();
		String sLinha = null;
		int iNumLinhas = Integer.parseInt(prop.getProperty(TEF_QTD_LINHAS, "0"));
		for (int i = 1; i < iNumLinhas; i++) {
			if (( sLinha = prop.getProperty(IMP_BASE + StringFunctions.strZero("" + i, 3)) ) != null)
				vRet.addElement(sLinha.replaceAll("\"", ""));
			else
				break;
		}
		return vRet.toArray();
	}

}