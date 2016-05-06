/**
 * @version 03/10/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva/Robson Sanchez
 *         <BR>
 * 
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.funcoes <BR>
 * Classe:
 * @(#)Funcoes.java <BR>
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
 * Comentários da classe.....
 */

package org.freedom.library.functions;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.filechooser.FileFilter;

import org.freedom.infra.functions.ConversionFunctions;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FSuporte;
import org.freedom.library.type.StringDireita;

import br.gov.sp.fazenda.dsge.brazilutils.uf.UF;
import br.gov.sp.fazenda.dsge.brazilutils.uf.ie.InscricaoEstadual;

public class Funcoes {

	public static final String sIEValida = "ISENTO";

	private static JDialog dlErro = null;

	private static Timer tim = null;
	


	public Funcoes() {
	}

	public static boolean executeURL(String os, String comando, String url) {
		boolean retorno = false;
		Vector<String> comandos = new Vector<String>();
		if (( comando != null ) && ( !comando.equals("") ))
			comandos.addElement(comando);
		if (os.equalsIgnoreCase("windows")) {
			comandos.addElement("start");
			comandos.addElement("firefox");
			comandos.addElement("explorer");
		}
		else if (os.equalsIgnoreCase("linux")) {
			comandos.addElement("firefox");
			comandos.addElement("konqueror");
			comandos.addElement("mozilla");
			comandos.addElement("nautilus");
		}
		for (int i = 0; i < comandos.size(); i++) {
			try {
				String[] exec = { comandos.elementAt(i).toString(), url };
				Runtime.getRuntime().exec(exec);
				retorno = true;
				break;
			}
			catch (IOException e) {
				retorno = false;
			}
		}
		return retorno;
	}

	public static String convertBigDecimalStrTime(BigDecimal val) {
		StringBuffer result = new StringBuffer();
		int intval = 0;
		int intdec = 0;
		if (val!=null) {
			intval = val.intValue();
			intdec = val.subtract(new BigDecimal(intval) ).multiply(new BigDecimal( 100) ).intValue();
			result.append(StringFunctions.strZero(String.valueOf( intval ),2));
			result.append(":");
			intdec = intdec * 60 / 100;
			result.append(StringFunctions.strZero(String.valueOf(intdec), 2));
			//result = String.valueOf(val.intValue())+":"+String.valueOf(val.*100/60);
			
		}
		return result.toString();
	}
	
	public static String getStringFormatedBigDecimal(BigDecimal bigVal, int iDecimal) {
		
		DecimalFormat df = null;
		
		String formato = "###";
		String valores = ".";
		
		BigDecimal bigValAbs = bigVal.abs(); 
		
		if(bigValAbs!=null && bigValAbs.floatValue()>=1) {
		
			if(bigValAbs.intValue()>=1000) {
				formato = "#,###,";
				valores = "0,000.";
			}
			else if(bigValAbs.intValue()>=10000) {
				formato = "##,###,";
			}
			else if(bigValAbs.intValue()>=100000) {
				formato = "###,###,";	
			}
			else if(bigValAbs.intValue()>=1000000) {
				formato = "#,###,###,";
			}
			else if(bigValAbs.intValue()>=10000000) {
				formato = "##,###,###,";
			}
			else if(bigValAbs.intValue()>=100000000) {
				formato = "###,###,###,";
			}
			else if(bigValAbs.intValue()>=1000000000) {
				formato = "#,###,###,###,";
			}
			
			for (int i = 0; i < iDecimal; i++) { 
				formato += "#";
				valores += "0";
	    	}
		
			df = new DecimalFormat(formato + valores );
			
		}
		else {
			df = new DecimalFormat("#,###0."+ StringFunctions.replicate("0", iDecimal));
		}
		/*
		if(iDecimal==0) {
			return df.format(bigVal).replaceAll(",", "");
		}
		else {
			return df.format(bigVal);	
		}
*/		
		return df.format(bigVal);
		
		
	}
	
	
	public static StringDireita bdToStr(BigDecimal vlr) {
		return bdToStr(vlr, Aplicativo.casasDecFin);
	}
	
	public static StringDireita bdToStrd(BigDecimal vlr) {
		return bdToStrd(vlr, Aplicativo.casasDecFin);
	}

	public static StringDireita bdToStr(BigDecimal vlr, int casasdec) {
		StringDireita retorno = null;
		if (vlr == null) {
			retorno = new StringDireita(Funcoes.strDecimalToStrCurrency(casasdec, "0").trim());
		}
		else {
			retorno = new StringDireita(Funcoes.strDecimalToStrCurrency(casasdec, String.valueOf(vlr)).trim());
		}
		return retorno;
	}

	public static StringDireita bdToStrd(BigDecimal vlr, int casasdec) {
		StringDireita retorno = null;
		if (vlr == null) {
			retorno = Funcoes.strDecimalToStrCurrencyd(casasdec, "0");
		}
		else {
			retorno = Funcoes.strDecimalToStrCurrencyd(casasdec, String.valueOf(vlr));
		}
		return retorno;
	}

	
	public static String getTimeString(Date data) {
		String bRetorno = "";
		if (data != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(data);
			bRetorno = StringFunctions.strZero(cal.get(Calendar.HOUR) + "", 2) + ":" + StringFunctions.strZero(cal.get(Calendar.MINUTE) + "", 2) + ":"
					+ StringFunctions.strZero(cal.get(Calendar.SECOND) + "", 2);
		}
		return bRetorno;
	}

	public static int contaChar(String sTexto, char cChar) {
		int iRet = 0;

		char[] cChars = sTexto.toCharArray();

		for (int i = 0; cChars.length > i; i++) {
			if (cChars[i] == cChar)
				iRet++;
		}

		return iRet;
	}

	public static HashMap<String, Object> getCodMunic(String nomeCidade, String UF, DbConnection con) {

		HashMap<String, Object> retorno = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		ResultSet rs = null;
		PreparedStatement ps = null;

		sql.append("SELECT CODMUNIC, CODPAIS, SIGLAUF FROM SGMUNICIPIO WHERE rtrim(lower(NOMEMUNIC))=?");

		if (UF != null) {
			sql.append(" AND SIGLAUF=? ");
		}

		try {
			ps = con.prepareStatement(sql.toString());
			ps.setString(1, nomeCidade.toLowerCase());

			if (UF != null) {
				ps.setString(2, UF.toUpperCase());
			}

			rs = ps.executeQuery();

			if (rs.next()) {
				retorno.put("CODMUNIC", rs.getString(1));
				retorno.put("CODPAIS", rs.getInt(2));
				retorno.put("SIGLAUF", rs.getString(3));
			}

		}
		catch (SQLException e) {
			e.printStackTrace();

		}

		return retorno;
	}

	public static String getURLWsCep(DbConnection con) {
		StringBuilder sql = new StringBuilder();
		ResultSet rs = null;
		PreparedStatement ps = null;
		String retorno = null;

		sql.append("SELECT URLWSCEP FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?");

		try {
			ps = con.prepareStatement(sql.toString());
			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, Aplicativo.iCodFilial);
			rs = ps.executeQuery();

			if (rs.next()) {
				retorno = rs.getString("URLWSCEP");
			}

		}
		catch (SQLException e) {
			e.printStackTrace();

		}

		return retorno;
	}

	public static double arredDouble(double deValor, int iDec) {
		BigDecimal bdValor = null;
		try {
			bdValor = new BigDecimal(deValor);
			bdValor = bdValor.setScale(iDec, BigDecimal.ROUND_HALF_UP);
			deValor = bdValor.doubleValue();
		}
		finally {
			bdValor = null;
		}
		return deValor;
	}

	public static float arredFloat(float fValor, int iDec) {
		BigDecimal bdValor = null;
		try {
			bdValor = new BigDecimal(fValor);
			bdValor = bdValor.setScale(iDec, BigDecimal.ROUND_HALF_UP);
			fValor = bdValor.floatValue();
		}
		finally {
			bdValor = null;
		}
		return fValor;
	}

	public static void espera(int iSec) {
		long iIni = getSeconds();
		long iFim = getSeconds();
		while (( iFim - iIni ) < iSec) {
			iFim = getSeconds();
		}
	}

	public static long getSeconds() {
		java.util.Date dtHora = new Date();
		return dtHora.getTime() / 1000;
	}

	public static boolean ehInteiro(String sNum) {
		boolean bRetorno = false;
		for (int i = 0; i < sNum.length(); i++) {
			if ("0123456789-".indexOf(sNum.charAt(i)) == -1) {
				bRetorno = false;
				break;
			}
			bRetorno = true;
		}
		return bRetorno;
	}

	public static int[] decodeDate(java.util.Date dtSel) {
		GregorianCalendar gcSel = new GregorianCalendar();
		int[] iRetorno = { 0, 0, 0 };
		try {
			gcSel.setTime(dtSel);
			iRetorno[0] = gcSel.get(Calendar.YEAR);
			iRetorno[1] = gcSel.get(Calendar.MONTH) + 1;
			iRetorno[2] = gcSel.get(Calendar.DAY_OF_MONTH);
		}
		finally {
			gcSel = null;
		}
		return iRetorno;
	}

	public static Date encodeDate(int iAno, int iMes, int iDia) {
		Date dtRetorno = new Date();
		GregorianCalendar gcSel = new GregorianCalendar();
		try {
			gcSel.setTime(dtRetorno);
			gcSel.set(Calendar.YEAR, iAno);
			gcSel.set(Calendar.MONTH, iMes - 1);
			gcSel.set(Calendar.DAY_OF_MONTH, iDia);
			dtRetorno = gcSel.getTime();
		}
		finally {
			gcSel = null;
		}
		return dtRetorno;
	}

	public static java.util.Date strTimeToDate(String sTime) {
		java.util.Date retorno = null;
		int hora = 0;
		int minuto = 0;
		int segundo = 0;
		try {
			try {
				hora = Integer.parseInt(sTime.substring(0, 2)) * 60 * 60 * 1000;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			try {
				minuto = Integer.parseInt(sTime.substring(3, 5)) * 60 * 1000;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			try {
				segundo = Integer.parseInt(sTime.substring(6, 8)) * 1000;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				retorno = new Date(hora + minuto + segundo);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			retorno = null;
		}
		return retorno;
	}

	public static java.util.Date encodeTime(Date dtSel, int iHora, int iMinuto, int iSegundo, int iMilesegundo) {
		Date dtRetorno = dtSel;
		GregorianCalendar gcSel = new GregorianCalendar();
		try {
			gcSel.setTime(dtSel);
			gcSel.set(Calendar.HOUR_OF_DAY, iHora);
			gcSel.set(Calendar.MINUTE, iMinuto);
			gcSel.set(Calendar.SECOND, iSegundo);
			gcSel.set(Calendar.MILLISECOND, iMilesegundo);
			dtSel = gcSel.getTime();
		}
		finally {
			gcSel = null;
		}
		return dtRetorno;
	}

	public synchronized static int mensagemConfirma(Component frame, String sMensagem) {
		String opt[] = { "Sim", "Não" };
		if (frame == null) {
			return JOptionPane.showOptionDialog(frame, sMensagem, "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opt, opt[0]);
		} else {
			return JOptionPane.showInternalOptionDialog(frame, sMensagem, "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opt, opt[0]);
		}
	}

	public static String adicEspacosEsquerda(String sTexto, int iEspacos) {
		if (iEspacos > sTexto.length()) {
			sTexto = StringFunctions.replicate(" ", iEspacos - sTexto.length()) + sTexto;
		}
		return sTexto;
	}

	public static String adicEspacosDireita(String sTexto, int iEspacos) {
		if (iEspacos > sTexto.length()) {
			sTexto = sTexto + StringFunctions.replicate(" ", iEspacos - sTexto.length());
		}
		return sTexto;
	}

	public static Vector<String> quebraLinha(Vector<String> vValor, int iTam) {
		Vector<String> vRetorno = new Vector<String>();
		String sLinha = null;
		String sValor = null;
		int iParte = 0;
		try {
			if (vValor != null) {
				for (int i = 0; i < vValor.size(); i++) {
					sLinha = vValor.elementAt(i).toString();
					while (!sLinha.equals("")) {
						if (sLinha.length() > iTam)
							iParte = iTam;
						else
							iParte = sLinha.length();
						sValor = sLinha.substring(0, iParte);
						vRetorno.addElement(sValor);
						if (sLinha.length() > iTam)
							sLinha = sLinha.substring(iParte);
						else
							sLinha = "";
					}
				}
			}
		}
		finally {
			sValor = null;
			sLinha = null;
		}
		return vRetorno;
	}

	public static Vector<String> ordenaVector(Vector<String> v, int iEspacos) {
		String s1 = "";
		String s2 = "";
		try {
			if (v != null) {
				for (int i1 = 1; i1 < v.size(); i1++) {
					s1 = adicEspacosEsquerda(v.elementAt(i1).toString().trim(), iEspacos);
					s2 = adicEspacosEsquerda(v.elementAt(i1 - 1).toString().trim(), iEspacos);
					if (s1.compareTo(s2) < 0) {
						for (int i2 = 0; i2 < i1; i2++) {
							s1 = adicEspacosEsquerda(v.elementAt(i1).toString(), iEspacos);
							s2 = adicEspacosEsquerda(v.elementAt(i2).toString(), iEspacos);
							if (s1.compareTo(s2) < 0) {
								for (int i3 = i2; i3 < i1; i3++) {
									// mostraVector(v);
									s1 = v.elementAt(i1).toString();
									s2 = v.elementAt(i3).toString();
									v.setElementAt(s2, i1);
									v.setElementAt(s1, i3);
									// mostraVector(v);
								}
							}
						}
					}
				}
			}
		}
		finally {
			s1 = null;
			s2 = null;
		}
		return v;
	}

	public static void mostraVector(Vector<?> v) {
		String sMostra = "";
		try {
			for (int i = 0; i < v.size(); i++) {
				sMostra += v.elementAt(i).toString() + ";";
			}
			mensagemInforma(null, sMostra);
		}
		finally {
			sMostra = null;
		}
	}

	public static long getNumDias(Date dt1, Date dt2) {
		long lResult = 0;
		long lDias1 = 0;
		long lDias2 = 0;
		GregorianCalendar cal1 = new GregorianCalendar();
		GregorianCalendar cal2 = new GregorianCalendar();
		cal1.setTime(dt1);
		cal1.set(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), cal1.get(Calendar.DATE), 0, 0, 0);
		cal2.setTime(dt2);
		cal2.set(cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH), cal2.get(Calendar.DATE), 0, 0, 0);

		lDias1 = cal1.getTimeInMillis();
		lDias2 = cal2.getTimeInMillis();

		lResult = ( lDias2 - lDias1 ) / ( 60 * 24 * 60 * 1000 );
		return lResult;
	}

	public static long getNumDiasAbs(Date dt1, Date dt2) {
		long lResult = 0;
		long lDias1 = 0;
		long lDias2 = 0;
		GregorianCalendar cal1 = new GregorianCalendar();
		GregorianCalendar cal2 = new GregorianCalendar();
		cal1.setTime(dt1);
		cal1.set(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), cal1.get(Calendar.DATE), 0, 0, 0);
		cal2.setTime(dt2);
		cal2.set(cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH), cal2.get(Calendar.DATE), 0, 0, 0);

		lDias1 = cal1.getTimeInMillis();
		lDias2 = cal2.getTimeInMillis();
		if (lDias1 > lDias2)
			lResult = ( lDias1 - lDias2 ) / ( 60 * 24 * 60 * 1000 );
		else
			lResult = ( lDias2 - lDias1 ) / ( 60 * 24 * 60 * 1000 );
		return lResult;
	}

	public static Date getDataFimMes(int iMes, int iAno) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, iMes);
		cal.set(Calendar.YEAR, iAno);
		int iUltimoDia = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, iUltimoDia);
		return cal.getTime();
	}

	public static int getDiaMes(Date data) {
		int retorno = 1;
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(data);
		retorno = cal.get(Calendar.DAY_OF_MONTH);
		return retorno;
	}

	public static int getAno(Date data) {
		int retorno = 1;
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(data);
		retorno = cal.get(Calendar.YEAR);
		return retorno;
	}

	public static int getMes(Date data) {
		int retorno = 1;
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(data);
		retorno = cal.get(Calendar.MONTH) + 1; // Deve-se somar 1 pois começa em
		// zero
		return retorno;
	}

	public static String getCidadeFilial(final DbConnection con) {

		String cidade = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sSQL = new StringBuilder();

		try {

			sSQL.append("SELECT M.NOMEMUNIC FROM SGFILIAL F, SGMUNICIPIO M WHERE F.CODEMP=? AND F.CODFILIAL=? ");
			sSQL.append("AND F.CODMUNIC=M.CODMUNIC AND F.CODPAIS=M.CODPAIS AND F.SIGLAUF=M.SIGLAUF ");
			ps = con.prepareStatement(sSQL.toString());
			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, ListaCampos.getMasterFilial("SGFILIAL"));
			rs = ps.executeQuery();

			if (rs.next()) {

				cidade = rs.getString("NOMEMUNIC") != null ? rs.getString("NOMEMUNIC").trim() : null;
			}

			rs.close();
			ps.close();

			con.commit();

		}
		catch (Exception e) {
			Funcoes.mensagemErro(null, "Erro ao buscar cidade da filial!\n" + e.getMessage());
			e.printStackTrace();
		}

		return cidade;
	}

	public static String getCasasDecUnid(String codUnid, DbConnection con) {

		String casasDec = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sSQL = new StringBuilder();

		try {

			sSQL.append("SELECT CASASDEC FROM EQUNIDADE WHERE CODEMP=? AND CODFILIAL=? AND CODUNID=?");
			ps = con.prepareStatement(sSQL.toString());
			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, ListaCampos.getMasterFilial("EQUNIDADE"));
			ps.setString(3, codUnid);
			rs = ps.executeQuery();

			if (rs.next()) {
				casasDec = rs.getString("CASASDEC");
			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		return casasDec;
	}

	public static String getCidadeDiaMesAnoExtenso(String cidade, Date data) {
		String retorno = "";
		try {

			retorno = cidade + ", " + getDiaMes(data) + " de " + ( getMesExtenso(data).toLowerCase() ) + " de " + getAno(data);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}

	public static String getMesExtenso(Date data) {
		String retorno = "";
		int mes = getMes(data);
		if (mes == 1) {
			retorno = "JANEIRO";
		}
		else if (mes == 2) {
			retorno = "FEVEREIRO";
		}
		else if (mes == 3) {
			retorno = "MARÇO";
		}
		else if (mes == 4) {
			retorno = "ABRIL";
		}
		else if (mes == 5) {
			retorno = "MAIO";
		}
		else if (mes == 6) {
			retorno = "JUNHO";
		}
		else if (mes == 7) {
			retorno = "JULHO";
		}
		else if (mes == 8) {
			retorno = "AGOSTO";
		}
		else if (mes == 9) {
			retorno = "SETEMBRO";
		}
		else if (mes == 10) {
			retorno = "OUTUBRO";
		}
		else if (mes == 11) {
			retorno = "NOVEMBRO";
		}
		else if (mes == 12) {
			retorno = "DEZEMBRO";
		}
		return retorno;
	}

	public static Date getDataIniMes(int iMes, int iAno) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, iMes);
		cal.set(Calendar.YEAR, iAno);
		return cal.getTime();
	}

	public synchronized static void mensagem(Component frame, String sMensagem, String sTitulo, int iOpcao) {
		// imgIcone = Aplicativo.imgIcone;
		//if (frame==null) {
			//JOptionPane.showMessageDialog(Aplicativo.telaPrincipal, sMensagem, sTitulo, iOpcao);
			JOptionPane.showMessageDialog(frame, sMensagem, sTitulo, iOpcao);
		/*} else {
			JOptionPane.showInternalMessageDialog(frame, sMensagem, sTitulo, iOpcao);
		}*/
	}

	// Retorna a data sem os valores de hora minuto e segundo, para comparação
	// real de data apenas
	// utilizando as funções before, after e compareTo.
	public static Date getDataPura(Date dtantes) {
		Date ret = null;
		try {
			Calendar cal = new GregorianCalendar();
			cal.set(getAno(dtantes), getMes(dtantes) - 1, getDiaMes(dtantes), 0, 0, 0);
			cal.set(Calendar.MILLISECOND, 0);
			ret = cal.getTime();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * Mostra uma mensagem sem botões não-modal por N segundos.
	 * 
	 * @see #mensagemTemp(JFrame,String,String,int)
	 */
	// public static FFDialogo mensagemTemp(final String sMensagem, final String
	// sTitulo,int iTempoShow) {
	// return mensagemTemp(null,sMensagem,sTitulo,iTempoShow);x
	// }

	/**
	 * Mostra uma mensagem sem botões não-modal por N segundos.
	 * 
	 * @see #mensagemTemp(String,String,int)
	 */
	public synchronized static FFDialogo mensagemTemp(JFrame fOrig, final String sMensagem, final String sTitulo, int iTempoShow) {
		final FFDialogo diag = new FFDialogo(fOrig, false);
		try {
			fOrig = fOrig == null ? fOrig = Aplicativo.telaPrincipal : fOrig;

			diag.setAtribos(300, 120);

			JPanelPad pn = new JPanelPad(new BorderLayout());
			pn.add(new JLabelPad(sMensagem, JLabel.CENTER), BorderLayout.CENTER);

			diag.setTela(pn);
			diag.setVisible(true);

			tim = new Timer(iTempoShow * 1000, new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					tim.stop();
					tim = null;
					diag.dispose();
				}
			});
			tim.start();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return diag;
	}

	public synchronized static void mensagemInforma(Component frame, String sMensagem) {
		mensagem(frame, sMensagem, "Informação", JOptionPane.INFORMATION_MESSAGE);
	}

	public synchronized static void mensagemErro(Component frame, String sMensagem, boolean bEnviar, Exception err) {
		mensagemErro(frame, sMensagem, bEnviar, Aplicativo.getInstace().getConexao(), err);
	}

	public static void mensagemErro(Component frame, String sMensagem, boolean bEnviar, DbConnection con, Exception err) {
		mensagem(frame, sMensagem, "Erro", JOptionPane.ERROR_MESSAGE);
		if (bEnviar)
			;
		if (mensagemConfirma(null, "Deseja enviar erro para o suporte?") == 0) {
			FSuporte tela = new FSuporte();
			tela.setConexao(con);
			if (err != null) {

				for (int i = 0; err.getStackTrace().length > i; i++) {
					if (i == 0) {
						sMensagem += "\nTrace:";

					}
					sMensagem += "\n" + err.getStackTrace()[i].toString();
				}
			}
			tela.setMensagem(sMensagem);
			tela.setVisible(true);
			tela.dispose();
		}
	}

	public synchronized static void mensagemErro(Component frame, String sMensagem) {
		mensagem(frame, sMensagem, "Erro", JOptionPane.ERROR_MESSAGE);
	}

	public static String trimFinal(String sVal) {
		if (sVal == null){
			return null;
		}
		
		char[] cVal = sVal.toCharArray();
		
		String sRetorno = sVal;
		
		for (int i = sVal.length() - 1; i >= 0; i--) {
			if (cVal[i] != ' ') {
				sRetorno = sVal.substring(0, i + 1);
				break;
			}
		}
		
		return sRetorno;
	}

	public static void setBordReq(JComponent comp) {
		setBordReq(comp, true);
	}

	public static void setBordReq(JComponent comp, boolean bReq) {
		if (bReq) {

			comp.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red), BorderFactory.createEtchedBorder()));
		}
		else {
			comp.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.darkGray), BorderFactory.createEtchedBorder()));
		}
	}

	public static void setBorder(JComponent comp, Color color, boolean border) {
		if (border) {
			comp.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, color), BorderFactory.createEtchedBorder()));
		}
		else {
			comp.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.darkGray), BorderFactory.createEtchedBorder()));
		}
	}

	public static char getPontoDec() {
		return ',';
	}

	public static String setPontoDec(String sVal) {
		int iLocal = sVal.indexOf('.') >= 0 ? sVal.indexOf('.') : sVal.indexOf(',');
		if (iLocal >= 0) {
			char[] cVal = new char[sVal.length()];
			cVal = sVal.toCharArray();
			cVal[iLocal] = getPontoDec();
			sVal = new String(cVal);
		}
		return sVal;
	}

	public static String substringByChar(String sVal, char cVal, boolean bOrient) {
		String sRetorno = "";
		sVal = copy(sVal, 0, sVal.length());
		char[] cStr = sVal.toCharArray();
		if (bOrient) {
			for (int i = 0; i < sVal.length(); i++) {
				if (cStr[i] == cVal)
					break;
				sRetorno += cStr[i];
			}
		}
		else {
			for (int i = ( sVal.length() - 1 ); i >= 0; i--) {
				if (cStr[i] == cVal)
					break;
				sRetorno = cStr[i] + sRetorno;
			}
		}
		return sRetorno;
	}

	public static String copy(String sTmp, int iPos, int iTam) {
		if (sTmp == null)
			sTmp = "";
		if (sTmp.length() < ( iTam + 1 )) {
			sTmp = sTmp + StringFunctions.replicate(" ", iTam - sTmp.length());
		}
		else {
			sTmp = sTmp.substring(iPos, iTam);
		}
		return sTmp;
	}

	public static String copy(String sTmp, int iTam) {
		return copy(sTmp, 0, iTam);
	}

	public static int contaMeses(Date dDataIni, Date dDataFim) {
		int iMeses = 0;
		GregorianCalendar cIni = new GregorianCalendar();
		GregorianCalendar cFim = new GregorianCalendar();
		cIni.setTime(dDataIni);
		cFim.setTime(dDataFim);

		try {
			iMeses = 1 + ( cFim.get(Calendar.MONTH) + ( 12 * ( ( cFim.get(Calendar.YEAR) - cIni.get(Calendar.YEAR) - 1 ) ) + ( 12 - cIni.get(Calendar.MONTH) ) ) );
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return iMeses;
	}

	public static String dateToStrExtenso(Date data) {
		String sRet = "";
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(data);
		int iDia = 0;
		int iAno = 0;
		int iMes = 0;
		if (data != null) {
			iDia = cal.get(Calendar.DAY_OF_MONTH);
			iMes = cal.get(Calendar.MONTH) + 1;
			iAno = cal.get(Calendar.YEAR);
		}
		sRet = StringFunctions.strZero("" + iDia, 2) + " de " + strMes(iMes).toLowerCase() + " de " + iAno;
		return sRet;
	}

	public static String strMes(int iMes) {
		String sRet = "";
		switch (iMes) {
		case 1:
			sRet = "Janeiro";
			break;
		case 2:
			sRet = "Fevereiro";
			break;
		case 3:
			sRet = "Março";
			break;
		case 4:
			sRet = "Abril";
			break;
		case 5:
			sRet = "Maio";
			break;
		case 6:
			sRet = "Junho";
			break;
		case 7:
			sRet = "Julho";
			break;
		case 8:
			sRet = "Agosto";
			break;
		case 9:
			sRet = "Setembro";
			break;
		case 10:
			sRet = "Outubro";
			break;
		case 11:
			sRet = "Novembro";
			break;
		case 12:
			sRet = "Dezembro";
			break;
		}
		return sRet;
	}

	public static String dataAAAAMMDD(Date data) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(data);
		int iDia = 0;
		int iAno = 0;
		int iMes = 0;
		if (data != null) {
			iDia = cal.get(Calendar.DAY_OF_MONTH);
			iMes = cal.get(Calendar.MONTH) + 1;
			iAno = cal.get(Calendar.YEAR);
		}
		return StringFunctions.strZero(iAno + "", 4) + StringFunctions.strZero(iMes + "", 2) + StringFunctions.strZero(iDia + "", 2);
	}

	public static String dataDDMMAAAA(Date data) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(data);
		int iDia = 0;
		int iAno = 0;
		int iMes = 0;
		if (data != null) {
			iDia = cal.get(Calendar.DAY_OF_MONTH);
			iMes = cal.get(Calendar.MONTH) + 1;
			iAno = cal.get(Calendar.YEAR);
		}
		return StringFunctions.strZero(iDia + "", 2) + StringFunctions.strZero(iMes + "", 2) + StringFunctions.strZero(iAno + "", 4);
	}

	public static String doubleToStrCurExtenso(double dVal, String sMoeda[]) {
		String sRet = "";
		String sVals[] = ( "" + dVal ).split("\\.");
		String sTmp = intToStrExtenso(new Integer(sVals[0]).intValue());
		if (!sTmp.trim().equals("")) {
			sRet = sTmp;
			if (sRet.substring(0, 2).indexOf("um") == 0)
				sRet += " " + sMoeda[2];
			else if (sRet.substring(sRet.length() - 2).indexOf("ão") == 0)
				sRet += " de " + sMoeda[3];
			else if (sRet.substring(sRet.length() - 3).indexOf("ões") == 0)
				sRet += " de " + sMoeda[3];
			else
				sRet += " " + sMoeda[3];
		}

		if (!sVals[1].equals("") && ( new Integer(sVals[1]) ).intValue() > 0) {
			if (!sRet.equals(""))
				sTmp = " e ";
			else
				sTmp = " ";
			sTmp += intToStrExtenso(new Integer(sVals[1]).intValue());
			if (!sTmp.trim().equals("")) {
				if (sTmp.substring(0, 2).indexOf("um") < 4) // 4 porque pode ter
					// o 'e' na frente.
					sRet += sTmp + " " + sMoeda[0];
				else
					sRet += sTmp + " " + sMoeda[1];
			}
		}
		return sRet;
	}

	public static String intToStrExtenso(int iVal) {
		String sRet = "";
		int iTmp = 0;
		String sNomes[][] = {
				{ "", "um", "dois", "três", "quatro", "cinco", "seis", "sete", "oito", "nove", "dez", "onze", "doze", "treze", "quatorze", "quinze", "dezesseis", "dezesete", "dezoito", "dezenove" },
				{ "", "", "vinte", "trinta", "quarenta", "cinquenta", "sessenta", "setenta", "oitenta", "noventa" },
				{ "", "cem", "duzentos", "trezentos", "quatrocentos", "quinhentos", "seiscentos", "setecentos", "oitocentos", "novecentos" } };
		if (iVal == 1000000000) {
			sRet += sNomes[0][1] + " bilhão";
			iVal = 0;
		}
		if (iVal > 999999999) {
			iTmp = iVal / 1000000000;
			sRet += intToStrExtenso(iTmp) + " bilhões";
			iVal -= iTmp * 1000000000;
			sRet += iVal > 0 ? " e " : "";
		}
		if (iVal == 1000000) {
			sRet += sNomes[0][1] + " milhão";
			iVal = 0;
		}
		if (iVal > 999999) {
			iTmp = iVal / 1000000;
			sRet += intToStrExtenso(iTmp) + " milhões";
			iVal -= iTmp * 1000000;
			sRet += iVal > 0 ? " e " : "";
		}
		if (iVal > 999) {
			iTmp = iVal / 1000;
			sRet += ( iTmp > 1 ) ? intToStrExtenso(iTmp) + " mil" : "mil";
			iVal -= iTmp * 1000;
			// Regra do 'e':
			if (iVal != 0) {
				int iCent = ( iVal / 100 );
				int iDez = ( iVal - ( iCent * 100 ) ) / 10;
				int iUnid = ( iVal - ( ( iDez * 10 ) + ( iCent * 100 ) ) );
				if (iCent == 0 || ( iDez == 0 && iUnid == 0 ))
					sRet += " e ";
				else
					sRet += " ";
			}
			else
				sRet += "";
		}
		if (iVal > 99) {
			iTmp = iVal / 100;
			sRet += sNomes[2][iTmp];
			iVal -= iTmp * 100;
			if (sRet.equals("cem") && iVal > 0)
				sRet = "cento";
			sRet += iVal > 0 ? " e " : "";
		}
		if (iVal > 19) {
			iTmp = iVal / 10;
			sRet += sNomes[1][iTmp];
			iVal -= iTmp * 10;
			sRet += iVal > 0 ? " e " : "";
		}
		if (iVal > 0) {
			iTmp = iVal;
			sRet += sNomes[0][iTmp];
		}
		return sRet;
	}

	public static String arrayToString(String[] lista, String sep) {
		String result = "";
		if (lista.length > 0) {
			result = lista[0]; // start with the first element
			for (int i = 1; i < lista.length; i++) {
				result = result + sep + lista[i];
			}
		}
		return result;
	}

	public static String arrayTraceToString(StackTraceElement[] lista, String sep, Integer lines) {
		String result = "";
		
		if(lines == null || lines > lista.length ) {
			lines = lista.length;
		}
		
		if (lines > 0) {
			result = lista[0].toString(); // start with the first element
			for (int i = 1; i < lines; i++) {
				result = result + sep + lista[i];
			}
		}
		return result;
	}
	
	public static String vectorToString(Vector<?> lista, String sep) {
		String result = "";
		if (lista.size() > 0) {
			result = lista.get(0).toString(); // start with the first element
			for (int i = 1; i < lista.size(); i++) {
				result = result + sep + lista.get(i);
			}
		}
		return result;
	}


	public static String adicionaEspacos(String sTexto, int iTamanho) {
		int iTamIni = 0;
		String sRetorno = "";
		if (sTexto == null)
			sTexto = "";
		iTamIni = sTexto.length();
		if (iTamIni > iTamanho) {
			sRetorno = sTexto.substring(0, iTamanho);
		}
		else {
			sRetorno = sTexto;
			for (int i = iTamIni; i < iTamanho; i++) {
				sRetorno = sRetorno + ' ';
			}
		}
		return sRetorno;
	}

	public static String alinhaCentro(int iValor, int iTam) {
		return alinhaCentro("" + iValor, iTam);
	}

	public static String alinhaCentro(String sVal, int iTam) {
		if (sVal != null)
			sVal = sVal.trim();
		else
			sVal = "";
		int iTamStr = sVal.length();
		if (iTamStr > iTam)
			sVal = sVal.substring(iTam);
		else {
			int iSpaceAdic = ( iTam - iTamStr - 1 ) / 2;
			sVal = StringFunctions.replicate(" ", iSpaceAdic) + sVal + StringFunctions.replicate(" ", iSpaceAdic);
		}
		return sVal;
	}

	public static String alinhaDir(int iValor, int iTam) {
		return alinhaDir("" + iValor, iTam);
	}

	public static String alinhaDir(String sVal, int iTam) {
		if (sVal != null)
			sVal = sVal.trim();
		else
			sVal = "";
		int iTamStr = sVal.length();
		if (iTamStr <= iTam) {
			sVal = StringFunctions.replicate(" ", iTam - iTamStr) + sVal;
		}
		return sVal;
	}

	public static BigDecimal transValorInv(String sVal) {
		BigDecimal bigRet = new BigDecimal("0.00");
		if (sVal == null)
			return bigRet;
		if (sVal.length() < 3)
			sVal = "0" + StringFunctions.replicate("0", 2 - sVal.length()) + sVal;

		sVal = sVal.substring(0, sVal.length() - 2) + "." + sVal.substring(sVal.length() - 2);
		bigRet = new BigDecimal(sVal);
		return bigRet;
	}

	public static String transValor(String sValor, int iTam, int iDec, boolean bZeroEsq) {
		if (sValor == null) {
			sValor = "0";
		}
		// System.out.println("Antes de converter: "+sValor);
		String sDec = "";
		String sResult = sValor;
		for (int i = 0; i < sValor.length(); i++) {
			if (( sValor.substring(i, i + 1).equals(".") ) | ( sValor.substring(i, i + 1).equals(",") )) {
				sResult = sValor.substring(0, i);
				sDec = sValor.substring(i + 1, sValor.length());
				if (sDec.length() < iDec) {
					// System.out.println("sDec e menor que idec");
					sDec = sDec + StringFunctions.replicate("0", iDec - sDec.length());
				}
				else if (sDec.length() > iDec) {
					// System.out.println("sDec e maior que idec");
					sDec = sDec.substring(0, iDec);
				}
				break;
			}
		}

		if (( sDec.trim().equals("") ) & ( iDec > 0 )) {
			sDec = StringFunctions.replicate("0", iDec);
		}
		if (sResult.length() > ( iTam - iDec )) {
			sResult = sResult.substring(sResult.length() - ( iTam - iDec ) - 1, ( iTam - iDec ));
		}
		if (bZeroEsq) {
			if (sResult.length() < ( iTam - iDec ))
				sResult = StringFunctions.replicate("0", ( iTam - iDec ) - sResult.length()) + sResult;
		}
		// System.out.println("Depois de converter: "+sResult+sDec);

		return sResult + sDec;
	}

	public static Integer getMaior(Integer[] numeros) {

		Integer maior = null;

		try {
			for (int i = 0; i < numeros.length; i++) {
				if (maior == null || maior < numeros[i]) {
					maior = numeros[i];
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return maior;

	}

	public static Integer getMenor(Integer[] numeros, Integer valorminimo) {

		Integer menor = null;

		try {
			for (int i = 0; i < numeros.length; i++) {
				if (menor == null || menor > numeros[i]) {
					if (numeros[i] > valorminimo) {
						menor = numeros[i];
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return menor;

	}

	public static void setImpAtrib(PrintRequestAttributeSet p) {
		HashPrintRequestAttributeSet pra = ( HashPrintRequestAttributeSet ) p;
		File fArq = new File("impres.cfg");
		try {
			if (!fArq.exists())
				if (!fArq.createNewFile())
					return;
			FileOutputStream foArq = new FileOutputStream(fArq);
			ObjectOutputStream obj = new ObjectOutputStream(foArq);
			obj.writeObject(pra);
			obj.flush();
			foArq.close();
		}
		catch (IOException err) {
			err.printStackTrace();
		}
	}

	public static PrintRequestAttributeSet getImpAtrib() {
		HashPrintRequestAttributeSet pra = new HashPrintRequestAttributeSet();
		File fArq = new File("impres.cfg");
		try {
			if (!fArq.exists())
				return pra;
			FileInputStream foArq = new FileInputStream(fArq);
			ObjectInputStream obj = new ObjectInputStream(foArq);
			pra = ( HashPrintRequestAttributeSet ) obj.readObject();
			foArq.close();
		}
		catch (Exception err) {
			err.printStackTrace();
		}
		return pra;
	}

	public static Component getOwnerTela(Component comp) {
		Component cFrame = null;
		Component cRetorno = null;
		cFrame = comp.getParent();
		if (cFrame != null) {
			for (int i = 1; 1 <= 10; i++) {
				if (( cFrame instanceof JFrame ) || ( cFrame instanceof JInternalFrame ) || ( cFrame instanceof JDialog )) {
					cRetorno = cFrame;
					break;
				}
				cFrame = cFrame.getParent();
			}
		}
		return cRetorno;
	}

	public static Vector<String> stringToVector(String sTexto, String sSep) {
		Vector<String> vRetorno = new Vector<String>();
		String sLinha = "";
		if (sTexto != null) {
			int iPos = sTexto.indexOf(sSep);
			int iPosIni = 0;
			int iTam = sTexto.length();
			if (( iPos == -1 ) & ( sTexto != null ))
				vRetorno.addElement(sTexto);
			while (iPos >= 0) {
				sLinha = sTexto.substring(iPosIni, iPos);
				vRetorno.addElement(sLinha);
				iPosIni = iPos + sSep.length();

				iPos = sTexto.indexOf(sSep, iPosIni);
				if (( iPos == -1 ) & ( iTam > iPosIni )) {
					sLinha = sTexto.substring(iPosIni);
					vRetorno.addElement(sLinha);
					break;
				}
			}
		}
		return vRetorno;
	}

	public static Vector<String> stringToVector(String sTexto) {
		Vector<String> vRetorno = new Vector<String>();
		String sLinha = "";
		char c10 = ( char ) 10;
		char c13 = ( char ) 13;
		if (sTexto != null) {
			int iPos = sTexto.indexOf(c13);
			int iPosIni = 0;
			int iTam = sTexto.length();
			if (iPos == -1)
				iPos = sTexto.indexOf(c10);
			if (( iPos == -1 ) & ( sTexto != null ))
				vRetorno.addElement(sTexto);
			while (iPos >= 0) {
				// System.out.println("PosIni: "+iPosIni);
				// System.out.println("Pos: "+iPos);
				sLinha = sTexto.substring(iPosIni, iPos);
				vRetorno.addElement(sLinha);
				iPosIni = iPos + 1;
				if (( iPosIni ) < iTam) {
					if (sTexto.charAt(iPosIni) == c10)
						iPosIni++;
				}
				iPos = sTexto.indexOf(c13, iPosIni);
				if (iPos == -1)
					iPos = sTexto.indexOf(c10, iPosIni);
				if (( iPos == -1 ) & ( iTam > iPosIni )) {
					sLinha = sTexto.substring(iPosIni);
					vRetorno.addElement(sLinha);
					break;
				}
			}
		}
		return vRetorno;
	}

	public static String tiraChar(String sVal, String sChar) {
		String sRetorno = sVal;
		sVal = sVal.trim();
		int iPos = sVal.indexOf(sChar);
		if (iPos >= 0) {
			if (iPos < ( sVal.length() - 1 ))
				sRetorno = sVal.substring(0, iPos) + sVal.substring(iPos + 1);
			else
				sRetorno = sVal.substring(0, iPos);
		}
		return sRetorno;
	}

	public static String tiraString(String sTexto, String sParc) {
		String sRetorno = sTexto;
		int iPos = 0;
		if (!sParc.equals("")) {
			while (iPos > -1) {
				iPos = sRetorno.indexOf(sParc);
				if (iPos > -1) {
					sRetorno = sRetorno.substring(0, iPos) + sRetorno.substring(iPos + sParc.length(), sRetorno.length());
				}
			}
		}
		return sRetorno;
	}

	public static String verData(String sData) {
		if (sData.length() < 10) {
			return "";
		}
		char cDate[] = sData.toCharArray();
		if (!Character.isDigit(cDate[0]))
			return "";
		else if (!Character.isDigit(cDate[1]))
			return "";
		else if (cDate[2] != '/')
			return "";
		else if (!Character.isDigit(cDate[3]))
			return "";
		else if (!Character.isDigit(cDate[4]))
			return "";
		else if (cDate[5] != '/')
			return "";
		else if (!Character.isDigit(cDate[6]))
			return "";
		else if (!Character.isDigit(cDate[7]))
			return "";
		else if (!Character.isDigit(cDate[8]))
			return "";
		else if (!Character.isDigit(cDate[9]))
			return "";
		else if (!validaData(sData))
			return "";
		return sData;
	}

	public static String verTime(String sTime) {
		if (sTime.length() != 8 && sTime.length() != 5) {
			return "";
		}
		char cTime[] = sTime.toCharArray();

		if (!Character.isDigit(cTime[0]))
			return "";
		else if (!Character.isDigit(cTime[1]))
			return "";
		else if (cTime[2] != ':')
			return "";
		else if (!Character.isDigit(cTime[3]))
			return "";
		else if (!Character.isDigit(cTime[4]))
			return "";

		if (sTime.length() == 8) {
			if (cTime[5] != ':')
				return "";
			else if (!Character.isDigit(cTime[6]))
				return "";
			else if (!Character.isDigit(cTime[7]))
				return "";
			else if (!validaTime(sTime))
				return "";
		}

		return sTime;
	}

	/**
	 * Retorna o path para um arquivo que será criado radomicamento no diretório
	 * TEMP.
	 * 
	 * @return - Path para o arquivo.
	 */
	public static String arquivoTemp() {
		int iNum = ( new Random() ).nextInt();
		String sNum = "" + iNum;
		return Aplicativo.strTemp + sNum.substring(0, ( sNum.length() > 8 ) ? 8 : sNum.length()) + ".tmp";
	}

	/**
	 * Retorna um File já verificado que foi selecionado pelo usuário.
	 * 
	 * @return - Ponteiro do arquivo.
	 * @see #criaArqTemp(String)
	 */
	public static File buscaArq(Component pai, String sExt) {
		File fRet = null;
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(Funcoes.getFiler(new String[] { sExt }));
		fc.setAcceptAllFileFilterUsed(false);
		if (fc.showSaveDialog(pai) == JFileChooser.APPROVE_OPTION) {
			fRet = fc.getSelectedFile();
			if (!fRet.getPath().trim().equals("")) {
				if (fRet.getPath().indexOf(".") == -1) {
					fRet = new File(fRet.getPath().trim() + "." + sExt);
				}
			}
		}
		try {
			if (fRet.exists()) {
				if (Funcoes.mensagemConfirma(pai, "Arquivo já existe, sobrescrever?") != JOptionPane.YES_OPTION)
					return null;
			}
			else
				fRet.createNewFile();
		}
		catch (IOException err) {
			Funcoes.mensagemErro(pai, "Erro ao verificar o arquivo!\n" + err.getMessage());
			err.printStackTrace();
		}
		return fRet;
	}

	public static FileFilter getFiler(final String sExts[]) {
		return new FileFilter() {
			public boolean accept(File fArq) {
				boolean bRet = false;
				if (fArq.isDirectory())
					return true;
				String sExt = getExt(fArq);
				for (int i = 0; i < sExts.length; i++)
					if (sExt.equals(sExts[i]))
						bRet = true;
				return bRet;
			}

			public String getDescription() {
				String sVal = "";
				String sVirg = "";
				for (int i = 0; i < sExts.length; i++) {
					sVal += sVirg + sExts[i];
					sVirg = ",";
				}
				return "Arquivo(s): " + sVal;
			}

			public String getExt(File f) {
				String ext = "";
				String s = f.getName();
				int i = s.lastIndexOf('.');

				if (i > 0 && i < s.length() - 1) {
					ext = s.substring(i + 1).toLowerCase();
				}
				return ext;
			}
		};
	}

	/**
	 * Retorna um File que foi gerado automáticamente no diretório temporário.
	 * 
	 * @return - Ponteiro do arquivo.
	 * @see #criaArqTemp(String)
	 */
	public static File criaArqTemp() {
		return criaArqTemp("tmp");
	}

	/**
	 * Retorna um File que foi gerado automáticamente no diretório temporário.
	 * Este arquivo será excluido automáticamente quando o java for finalizado.
	 * 
	 * @return - Ponteiro do arquivo.
	 */
	public static File criaArqTemp(String sExt) {
		File fRet = null;
		try {
			fRet = File.createTempFile("arq", "." + sExt);
		}
		catch (IOException err) {
			JOptionPane.showMessageDialog(null, "Erro ao criar arquivo temporário!!" + err.getMessage());
		}
		fRet.deleteOnExit();
		return fRet;
	}

	public static Date[] periodoMes(int iMes, int iAno) {
		Date[] dRets = new Date[2];
		GregorianCalendar cal = new GregorianCalendar(iAno, iMes, 1);
		dRets[0] = cal.getTime();
		cal.set(Calendar.DAY_OF_MONTH, cal.getMaximum(Calendar.DAY_OF_MONTH));
		dRets[1] = ( cal.getTime() );
		return dRets;
	}

	public static String binToStr(byte[] byteData) {
		if (byteData == null)
			return null;
		int iSrcIdx; // index into source (byteData)
		int iDestIdx; // index into destination (byteDest)
		byte byteDest[] = new byte[( ( byteData.length + 2 ) / 3 ) * 4];

		for (iSrcIdx = 0, iDestIdx = 0; iSrcIdx < byteData.length - 2; iSrcIdx += 3) {
			byteDest[iDestIdx++] = ( byte ) ( ( byteData[iSrcIdx] >>> 2 ) & 077 );
			byteDest[iDestIdx++] = ( byte ) ( ( byteData[iSrcIdx + 1] >>> 4 ) & 017 | ( byteData[iSrcIdx] << 4 ) & 077 );
			byteDest[iDestIdx++] = ( byte ) ( ( byteData[iSrcIdx + 2] >>> 6 ) & 003 | ( byteData[iSrcIdx + 1] << 2 ) & 077 );
			byteDest[iDestIdx++] = ( byte ) ( byteData[iSrcIdx + 2] & 077 );

			if (iSrcIdx < byteData.length) {
				byteDest[iDestIdx++] = ( byte ) ( ( byteData[iSrcIdx] >>> 2 ) & 077 );
				if (iSrcIdx < byteData.length - 1) {
					byteDest[iDestIdx++] = ( byte ) ( ( byteData[iSrcIdx + 1] >>> 4 ) & 017 | ( byteData[iSrcIdx] << 4 ) & 077 );
					byteDest[iDestIdx++] = ( byte ) ( ( byteData[iSrcIdx + 1] << 2 ) & 077 );
				}
				else
					byteDest[iDestIdx++] = ( byte ) ( ( byteData[iSrcIdx] << 4 ) & 077 );
			}

			for (iSrcIdx = 0; iSrcIdx < iDestIdx; iSrcIdx++) {
				if (byteDest[iSrcIdx] < 26)
					byteDest[iSrcIdx] = ( byte ) ( byteDest[iSrcIdx] + 'A' );
				else if (byteDest[iSrcIdx] < 52)
					byteDest[iSrcIdx] = ( byte ) ( byteDest[iSrcIdx] + 'a' - 26 );
				else if (byteDest[iSrcIdx] < 62)
					byteDest[iSrcIdx] = ( byte ) ( byteDest[iSrcIdx] + '0' - 52 );
				else if (byteDest[iSrcIdx] < 63)
					byteDest[iSrcIdx] = '+';
				else
					byteDest[iSrcIdx] = '/';
			}

			for (; iSrcIdx < byteDest.length; iSrcIdx++)
				byteDest[iSrcIdx] = '=';

		}

		return new String(byteDest);
	}

	public static String arredondaData(String sData) {
		if (sData.length() < 10)
			return "";
		int iDia = 0;
		int iMes = 0;
		int iAno = 0;
		int ano = Integer.parseInt(sData.substring(6, 10));
		int mes = Integer.parseInt(sData.substring(3, 5));
		int dia = Integer.parseInt(sData.substring(0, 2));
		if (mes > 12)
			mes = 13;
		if (dia > 32)
			dia = 32;
		GregorianCalendar cal = new GregorianCalendar(ano, mes - 1, dia);

		if (mes != ( cal.get(Calendar.MONTH) + 1 )) {
			cal.set(Calendar.DAY_OF_MONTH, 0);
			iDia = cal.get(Calendar.DAY_OF_MONTH);
			iMes = cal.get(Calendar.MONTH) + 1;
			iAno = cal.get(Calendar.YEAR);
		}
		return StringFunctions.strZero("" + iDia, 2) + "/" + StringFunctions.strZero("" + iMes, 2) + "/" + StringFunctions.strZero("" + iAno, 4);
	}

	public static boolean validaData(String data) {
		boolean retorno = true;
		GregorianCalendar cal = null;
		if (data.length() < 10)
			return false;
		int ano = Integer.parseInt(data.substring(6, 10));
		int mes = Integer.parseInt(data.substring(3, 5));
		int dia = Integer.parseInt(data.substring(0, 2));

		cal = new GregorianCalendar(ano, mes - 1, dia);

		if (( mes > 12 ) | ( ano == 0 ))
			retorno = false;
		else if (( mes != ( cal.get(Calendar.MONTH) + 1 ) ) | ( dia == 0 )) {
			retorno = false;
		}
		else if (( ano != ( cal.get(Calendar.YEAR) ) ) | ( mes == 0 ))
			retorno = false;
		return retorno;
	}

	public static boolean validaTime(String time) {
		boolean retorno = true;
		if (time.length() != 8 && time.length() != 5)
			return false;

		int hora = Integer.parseInt(time.substring(0, 2));
		int minuto = Integer.parseInt(time.substring(3, 5));
		int segundo = time.length() == 5 ? 0 : Integer.parseInt(time.substring(6, 8));

		if (( hora > 23 ) || ( hora < 0 ))
			retorno = false;
		else if (( minuto > 59 ) || ( minuto < 0 ))
			retorno = false;
		else if (( segundo > 59 ) || ( segundo < 0 ) && time.length() == 8)
			retorno = false;
		return retorno;
	}

	public static String timeStampToStrDate(Timestamp tVal) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(tVal);
		String sRetorno = StringFunctions.strZero("" + cal.get(Calendar.DATE), 2);
		sRetorno += "/" + StringFunctions.strZero("" + ( cal.get(Calendar.MONTH) + 1 ), 2);
		sRetorno += "/" + ( cal.get(Calendar.YEAR) );
		return sRetorno;
	}

	public static java.sql.Date dateToSQLDate(Date d) {
		return new java.sql.Date(d.getTime());
	}

	public static java.sql.Time dateToSQLTime(Date d) {
		return new java.sql.Time(d.getTime());
	}

	public static Date sqlDateToDate(java.sql.Date dVal) {
		Date dRetorno = null;
		if (dVal != null) {
			dRetorno = new Date(dVal.getTime());
		}
		return dRetorno;
	}

	public static GregorianCalendar sqlDateToGregorianCalendar(java.sql.Date dVal) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(dVal.getTime());
		return cal;
	}

	public static String[] strToStrArray(String sVals) {
		int iConta = 0;
		int iPos = 0;
		if (sVals != null) {
			while (iPos >= 0) {
				iPos = sVals.indexOf('\n', iPos + 1);
				iConta++;
			}
		}
		return strToStrArray(sVals, iConta);
	}

	public static String[] strToStrArray(String sVals, int iNumLinhas) {
		String[] sRetorno = new String[iNumLinhas];
		String sTemp = sVals != null ? sVals : "";
		int iPos;
		for (int i = 0; i < iNumLinhas; i++) {
			iPos = sTemp.indexOf('\n');
			if (iPos >= 0) {
				sRetorno[i] = sTemp.substring(0, iPos);
				if (iPos + 1 < sTemp.length())
					sTemp = sTemp.substring(iPos + 1, sTemp.length());
				else
					sTemp = "";
			}
			else if (( iPos == -1 ) && ( sTemp.length() == 0 )) {
				sRetorno[i] = "";
			}
			else {
				sRetorno[i] = sTemp;
				sTemp = "";
			}
		}
		return sRetorno;
	}

	public static String[] strToStrArray(String sVals, String sSep) {
		int iConta = 0;
		int iPos = 0;
		if (sVals != null) {
			while (iPos >= 0) {
				iPos = sVals.indexOf(sSep, iPos + 1);
				iConta++;
			}
		}
		return strToStrArray(sVals, iConta, sSep);
	}

	public static String[] strToStrArray(String sVals, int iNumLinhas, String sSep) {
		String[] sRetorno = new String[iNumLinhas];
		String sTemp = sVals != null ? sVals : "";
		int iPos;
		int iTamSep = sSep.length();
		for (int i = 0; i < iNumLinhas; i++) {
			iPos = sTemp.indexOf(sSep);
			if (iPos >= 0) {
				sRetorno[i] = sTemp.substring(0, iPos);
				if (iPos + 1 < sTemp.length())
					sTemp = sTemp.substring(iPos + iTamSep, sTemp.length());
				else
					sTemp = "";
			}
			else if (( iPos == -1 ) && ( sTemp.length() == 0 )) {
				sRetorno[i] = "";
			}
			else {
				sRetorno[i] = sTemp;
				sTemp = "";
			}
		}
		return sRetorno;
	}

	public static String strTostrQuebra(String sVals, int iNumChar) {
		String sRetorno = "";
		int iPos = 0;
		int iPosBranco = 0;
		int iPosEnter = 0;
		String sBusca = sVals;
		String sResto = "";
		if (( sVals != null ) && ( sVals.length() > iNumChar )) {
			while (( sBusca.length() > iNumChar )
					&& ( ( ( sBusca.indexOf(" ") > iNumChar ) || ( sBusca.indexOf("\n") > iNumChar ) ) || ( ( sBusca.indexOf(" ") < 0 ) || ( sBusca.indexOf("\n") < 0 ) ) )) {
				iPos = 0;
				iPosBranco = sBusca.lastIndexOf(" ");
				iPosEnter = sBusca.lastIndexOf("\n");
				if (( ( iPosBranco < iPosEnter ) && ( iPosBranco > 0 ) ) || ( ( iPosEnter < 0 ) && ( iPosBranco > 0 ) )) {
					iPos = iPosBranco;
				}
				else if (( ( iPosBranco > iPosEnter ) || ( iPosBranco == 0 ) ) && ( iPosEnter > 0 )) {
					iPos = iPosEnter;
				}
				if (( iPosEnter == iPosBranco ) || ( iPos > iNumChar ) || ( iPos == 0 )) {
					iPos = iNumChar;
				}
				if (iPos > iNumChar) {
					sResto = sBusca.substring(0, iPos);
					sBusca = sBusca.substring(iPos, sBusca.length());
					sRetorno += sResto + " ";
				}
				else {
					sRetorno = sVals;
					sBusca = "";
				}
			}
			sRetorno += sBusca;
		}
		else {
			return sBusca;
		}
		return sRetorno;
	}

	public static Vector<String> strToVectorSilabas(String sVals, int iNumColunas) {
		sVals = strTostrQuebra(sVals, iNumColunas);
		Vector<String> vRetorno = new Vector<String>();
		String[] sSeparadaPorEnter = null;
		String sLinhaAnt = "";
		String sLinhaNova = "";
		String sResto = "";
		String sLinhaCortada = "";
		if (sVals != null) {
			sSeparadaPorEnter = strToStrArray(sVals);
			for (int i = 0; sSeparadaPorEnter.length > i; i++) {
				if (!sResto.trim().equals(""))
					sLinhaAnt = sResto + " " + sSeparadaPorEnter[i];
				else
					sLinhaAnt = sSeparadaPorEnter[i];
				if (sLinhaAnt.length() > iNumColunas) {
					sLinhaCortada = sLinhaAnt.substring(0, iNumColunas < sLinhaAnt.length() ? iNumColunas : sLinhaAnt.length());
					if (sLinhaCortada.lastIndexOf(" ") > 0)
						sLinhaNova = sLinhaCortada.substring(0, sLinhaCortada.lastIndexOf(' '));
					else
						sLinhaNova = sLinhaCortada.substring(0, iNumColunas);

					sResto = sLinhaAnt.substring(sLinhaNova.length());

				}
				else {
					sLinhaNova = sLinhaAnt;
					sResto = "";
				}
				sLinhaNova = ( sLinhaNova.replaceAll(" +", " ") );
				if (sLinhaNova.indexOf(" ") == 0)
					vRetorno.addElement(sLinhaNova.substring(1));
				else
					vRetorno.addElement(sLinhaNova);
			}
			if (sResto.length() > 0) {
				while (sResto.length() > 0) {
					sLinhaAnt = sResto;
					sLinhaCortada = sLinhaAnt.substring(0, iNumColunas < sLinhaAnt.length() ? iNumColunas : sLinhaAnt.length());
					if (sLinhaAnt.length() > iNumColunas) {
						if (sLinhaCortada.lastIndexOf(" ") > 0) {
							sLinhaNova = sLinhaCortada.substring(0, sLinhaCortada.lastIndexOf(' '));
						}
						else if (sLinhaCortada.length() > iNumColunas)
							sLinhaNova = sLinhaCortada.substring(0, iNumColunas);
						else
							sLinhaNova = sLinhaCortada;

						sResto = sLinhaAnt.substring(sLinhaNova.length());

						sLinhaNova = ( sLinhaNova.replaceAll(" +", " ") );
						if (sLinhaNova.indexOf(" ") == 0)
							vRetorno.addElement(sLinhaNova.substring(1));
						else
							vRetorno.addElement(sLinhaNova);

					}
					else {
						sLinhaCortada = ( sLinhaCortada.replaceAll(" +", " ") );
						if (sLinhaCortada.indexOf(" ") == 0)
							vRetorno.addElement(sLinhaCortada.substring(1));
						else {
							vRetorno.addElement(sLinhaCortada);
						}
						sResto = "";
					}
				}
			}
		}
		return vRetorno;
	}

	public static String sqlTimeToStrTime(java.sql.Time t) {
		if (t == null)
			return "";
		//System.out.println("Data " + t);
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(t);
		int iHora = cal.get(Calendar.HOUR_OF_DAY);
		int iMinuto = cal.get(Calendar.MINUTE);
		int iSegundo = cal.get(Calendar.SECOND);
		//System.out.println(StringFunctions.strZero("" + iHora, 2) + ":" + StringFunctions.strZero("" + iMinuto, 2) + ":" + StringFunctions.strZero("" + iSegundo, 2));

		return StringFunctions.strZero("" + iHora, 2) + ":" + StringFunctions.strZero("" + iMinuto, 2) + ":" + StringFunctions.strZero("" + iSegundo, 2);
	}

	public static java.sql.Time strTimeTosqlTime(String stime ) {
		return strTimeToSqlTime(stime, true); 
	}
	
	public static java.sql.Time strTimeToSqlTime(String stime, boolean mileseg) {
		java.sql.Time ttime = null;

		try {

			if ( (stime == null) || "".equals(stime.trim() ) )
				return ttime;

//			System.out.println("Time: " + stime);
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(new Date());

			String[] sstime = stime.split(":");

			try {
				cal.set(Calendar.HOUR_OF_DAY, new Integer(sstime[0]).intValue());
			}
			catch (Exception e) {
				cal.set(Calendar.HOUR_OF_DAY, 0);
				e.printStackTrace();
			}
			try {
				cal.set(Calendar.MINUTE, new Integer(sstime[1]).intValue());
			}
			catch (Exception e) {
				cal.set(Calendar.MINUTE, 0);
				e.printStackTrace();
			}
			try {
				cal.set(Calendar.SECOND, new Integer(sstime[2]).intValue());
			}
			catch (Exception e) {
//				System.out.println("Tempo sem contagem de segundos.");
				cal.set(Calendar.SECOND, 0);
			}
			if ( ! mileseg) {
				try {
					cal.set(Calendar.MILLISECOND,0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//int iHora = cal.get(Calendar.HOUR_OF_DAY);
			//int iMinuto = cal.get(Calendar.MINUTE);
			//int iSegundo = cal.get(Calendar.SECOND);

			ttime = Funcoes.dateToSQLTime(cal.getTime());

	//		System.out.println(StringFunctions.strZero("" + iHora, 2) + ":" + StringFunctions.strZero("" + iMinuto, 2) + ":" + StringFunctions.strZero("" + iSegundo, 2));
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return ttime;
	}

	public static java.sql.Date strDateToSqlDate(String sVal) {
		GregorianCalendar cal = new GregorianCalendar();
		if (sVal.trim().length() == 10) {
			sVal = sVal.trim();
			try {
				int iAno = Integer.parseInt(sVal.substring(6));
				int iMes = Integer.parseInt(sVal.substring(3, 5)) - 1;
				int iDia = Integer.parseInt(sVal.substring(0, 2));
				cal = new GregorianCalendar(iAno, iMes, iDia);
			}
			catch (Exception err) {
				cal = null;
			}
		}
		else
			cal = null;
		if (cal == null)
			return null;
		return new java.sql.Date(cal.getTimeInMillis());
	}

	public static Date strDateToDate(String sVal) {
		return ConversionFunctions.strDateToDate(sVal);
	}

	public static String dateToStrDataHora(Date dVal) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(dVal);
		int iAno = cal.get(Calendar.YEAR);
		int iMes = cal.get(Calendar.MONTH) + 1;
		int iDia = cal.get(Calendar.DAY_OF_MONTH);
		int iHora = cal.get(Calendar.HOUR_OF_DAY);
		int iMinuto = cal.get(Calendar.MINUTE);
		int iSegundo = cal.get(Calendar.SECOND);
		return StringFunctions.strZero("" + iDia, 2) + "/" + StringFunctions.strZero("" + iMes, 2) + "/" + iAno + " - " + StringFunctions.strZero("" + iHora, 2) + ":"
				+ StringFunctions.strZero("" + iMinuto, 2) + ":" + StringFunctions.strZero("" + iSegundo, 2);
	}

	public static String dateToStrDate(Date dVal) {
		
		return ConversionFunctions.dateToStrDate(dVal);

	}

	public static String dateToStrTime(Date dVal) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(dVal);
		int iHora = cal.get(Calendar.HOUR_OF_DAY);
		int iMinuto = cal.get(Calendar.MINUTE);
		int iSegundo = cal.get(Calendar.SECOND);
		return StringFunctions.strZero(String.valueOf(iHora), 2) + ":" + StringFunctions.strZero(String.valueOf(iMinuto), 2) + ":" + iSegundo;
	}

	public static String strDateToStrDB(String sVal) {
		String sRet = "";
		if (sVal.trim().length() == 10) {
			sVal = sVal.trim();
			try {
				int iAno = Integer.parseInt(sVal.substring(6));
				int iMes = Integer.parseInt(sVal.substring(3, 5));
				int iDia = Integer.parseInt(sVal.substring(0, 2));
				sRet = iAno + "-" + iMes + "-" + iDia;
			}
			catch (Exception err) {
				sRet = "";
			}
		}
		else
			sRet = "";
		return sRet;
	}

	public static String dateToStrDB(Date d) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(d);
		String sDia = "" + cal.get(Calendar.DAY_OF_MONTH);
		String sMes = "" + ( cal.get(Calendar.MONTH) + 1 );
		String sAno = "" + cal.get(Calendar.YEAR);
		return sAno + "-" + sMes + "-" + sDia;
	}

	public static Timestamp dateToTimestamp(Date d) {
		return new Timestamp(d.getTime());
	}

	public static double strCurrencyToDouble(String sVal) {
		if (sVal == null)
			return 0;
		sVal = sVal.replace(getPontoDec(), '.');
		int iPos = sVal.lastIndexOf('.');
		int iPosTmp = -1;
		if (iPos >= 0) {
			while (iPos != ( iPosTmp = sVal.indexOf('.') )) {// Tirando os
																// pontos
				// de milhar.
				sVal = sVal.substring(0, iPosTmp) + sVal.substring(iPosTmp + 1);
				iPos--;
			}
		}
		return Double.parseDouble(sVal);
	}

	/**
	 * Aplica uma mascara sobre uma string. <BR>
	 * A mascara funciona da seguinte forma: <BR>
	 * Uma string contém a mascara, esta string <BR>
	 * será a matriz para transformação, esta matriz <BR>
	 * é composta de caracteres mas um caracter em especial <BR>
	 * o '#' que serve para busca de seu respectivo valor bruto. <BR>
	 * Por exemplo a mascara a seguir: <BR>
	 * "###.###,##". <BR>
	 * Se aplicarmos esta marcara sobre a string: <BR>
	 * "1022000" <BR>
	 * O Resultado será: "10.220,00".
	 * 
	 * @param sVal
	 *            - String com o valor 'bruto'.
	 * @param sMasc
	 *            - String com a mascara.
	 * @return Rotorna o valor bruto mascarado.
	 */
	public static String setMascara(String sVal, String sMasc) {
		if (sVal == null)
			return "";
		String texto = "";
		int i2 = 0;
		
		try {
		
			if (( sVal.length() > 0 ) & ( sMasc.length() > 0 ) & ( sMasc.length() > sVal.length() )) {
				char[] va = sVal.toCharArray();
				char[] ma = sMasc.toCharArray();
				for (int i = 0; i < sVal.length(); i++) {
					if (ma[i2] == '#') {
						texto += va[i];
					}
					else {
						texto += ma[i2];
	
						if (!Character.isDigit(ma[i2])) {
							texto += va[i];
							i2++;
						}
	
					}
					i2++;
				}
			}
			else
				texto = sVal;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return texto;
	}

	public static BigDecimal strDecimalToBigDecimal(int iDec, String sVal) {
		double deVal = 0;
		BigDecimal bigRet = null;
		try {
			if (sVal == null)
				sVal = "0";
			deVal = Float.parseFloat(sVal);
			deVal = Funcoes.arredDouble(deVal, iDec);
		}
		finally {
			bigRet = new BigDecimal(deVal);
		}
		return bigRet;
	}

	public static String strDecimalToStrCurrency(int iTam, int iDec, String sVal) {
		if (sVal == null)
			return StringFunctions.replicate(" ", iTam);

		sVal = strDecimalToStrCurrency(iDec, sVal);
		sVal = StringFunctions.replicate(" ", iTam - sVal.length()) + sVal;
		return sVal;
	}

	public static StringDireita strDecimalToStrCurrencyd(int iDec, String sVal) {
		return new StringDireita(strDecimalToStrCurrency(iDec, sVal));
	}

	public static String strDecimalToStrCurrency(int iDec, String sVal) {
		String sRetorno = "";
		if (sVal == null || sVal.trim().equals(""))
			return sRetorno;
		int iPonto = sVal.indexOf('.');
		char[] cVal = sVal.toCharArray();
		int iCont = ( iDec + 1 ) * -1;
		if (iPonto != -1) {
			cVal[iPonto] = ',';
			sVal = new String(cVal);
			sVal += StringFunctions.replicate("0", iDec - ( sVal.length() - ( iPonto + 1 ) ));
			sVal = sVal.substring(0, iPonto + 1 + iDec);
			cVal = sVal.toCharArray();
			for (int i = ( sVal.length() - 1 ); i >= 0; i--) {
				if (iCont == 3) {
					if (cVal[i] == '-')
						sRetorno = cVal[i] + sRetorno;
					else
						sRetorno = cVal[i] + "." + sRetorno;
					iCont = 1;
				}
				else {
					sRetorno = cVal[i] + sRetorno;
					iCont++;
				}
			}
		}
		else {
			sRetorno = sVal + ',' + StringFunctions.replicate("0", iDec);
		}
		if (iDec == 0) {
			sRetorno = sRetorno.substring(0, sRetorno.length() - 1);
		}
		return sRetorno;
	}

	public static void criaTelaErro(String sErro) {
		dlErro = new JDialog();
		dlErro.setSize(600, 350);
		dlErro.setLocationRelativeTo(dlErro);
		dlErro.setTitle("Relatório de Erros");
		Container c = dlErro.getContentPane();
		JPanelPad pnRod = new JPanelPad(JPanelPad.TP_JPANEL);
		JPanelPad pnOK = new JPanelPad(JPanelPad.TP_JPANEL);
		JTextArea txt = new JTextArea(sErro);
		txt.setEditable(false);
		JScrollPane spnTxt = new JScrollPane(txt);
		JButtonPad btOK = new JButtonPad("OK");
		c.setLayout(new BorderLayout());
		c.add(pnRod, BorderLayout.SOUTH);
		pnRod.setPreferredSize(new Dimension(400, 40));
		btOK.setPreferredSize(new Dimension(90, 30));
		pnRod.add(pnOK);
		pnOK.add(btOK);
		c.add(spnTxt, BorderLayout.CENTER);
		btOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				dlErro.dispose();
			}
		});
		dlErro.setModal(true);
		dlErro.setVisible(true);
	}

	public static void criaTelaErro(String sErro, Component cOrig) {
		FFDialogo dlErro = new FFDialogo(cOrig);
		dlErro.setAtribos(100, 100, 600, 350);
		dlErro.setTitle("Relatório de Erros");
		dlErro.setToFrameLayout();
		JTextArea txt = new JTextArea(sErro);
		JScrollPane spnTxt = new JScrollPane(txt);
		dlErro.setPanel(spnTxt);
		txt.setEditable(false);
		dlErro.setVisible(true);
		dlErro.dispose();
	}

	public static boolean ValidaCNPJ(String sDado) {
		byte[] D1 = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
		int DF1, DF2, DF3, DF4, DF5, DF6, Resto1, Resto2, PrimeiroDigito, SegundoDigito;
		String sConvert = " " + sDado.trim();
		char[] Dado = sConvert.toCharArray();
		boolean bRetorno = true;
		if (sDado.trim().length() == 14) {
			for (int i = 1; i <= 12; i++)
				if (Character.isDigit(Dado[i]))
					D1[i] = Byte.parseByte("" + Dado[i]);
				else
					bRetorno = false;
			if (bRetorno) {
				DF1 = 5 * D1[1] + 4 * D1[2] + 3 * D1[3] + 2 * D1[4] + 9 * D1[5] + 8 * D1[6] + 7 * D1[7] + 6 * D1[8] + 5 * D1[9] + 4 * D1[10] + 3 * D1[11] + 2 * D1[12];
				DF2 = DF1 / 11;
				DF3 = DF2 * 11;
				Resto1 = DF1 - DF3;
				if (( Resto1 == 0 ) | ( Resto1 == 1 ))
					PrimeiroDigito = 0;
				else
					PrimeiroDigito = 11 - Resto1;
				DF4 = 6 * D1[1] + 5 * D1[2] + 4 * D1[3] + 3 * D1[4] + 2 * D1[5] + 9 * D1[6] + 8 * D1[7] + 7 * D1[8] + 6 * D1[9] + 5 * D1[10] + 4 * D1[11] + 3 * D1[12] + 2 * PrimeiroDigito;
				DF5 = DF4 / 11;
				DF6 = DF5 * 11;
				Resto2 = DF4 - DF6;
				if (( Resto2 == 0 ) | ( Resto2 == 1 ))
					SegundoDigito = 0;
				else
					SegundoDigito = 11 - Resto2;
				if (( PrimeiroDigito != Integer.parseInt("" + Dado[13]) ) | ( SegundoDigito != Integer.parseInt("" + Dado[14]) ))
					bRetorno = false;
			}
		}
		else if (sDado.trim().length() != 0)
			bRetorno = false;
		return bRetorno;
	}

	public static boolean ValidaCPF(String sDado) {
		byte[] D1 = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		int DF1, DF2, DF3, DF4, DF5, DF6, Resto1, Resto2, PrimeiroDigito, SegundoDigito;
		String sConvert = sDado.trim();
		char[] Dado = sConvert.toCharArray();
		boolean bRetorno = true;
		if (sDado.trim().length() == 11) {
			for (int i = 1; i <= 9; i++)
				if (Character.isDigit(Dado[i]))
					D1[i] = Byte.parseByte("" + Dado[i - 1]);
				else
					bRetorno = false;
			if (bRetorno) {
				DF1 = 10 * D1[1] + 9 * D1[2] + 8 * D1[3] + 7 * D1[4] + 6 * D1[5] + 5 * D1[6] + 4 * D1[7] + 3 * D1[8] + 2 * D1[9];
				DF2 = DF1 / 11;
				DF3 = DF2 * 11;
				Resto1 = DF1 - DF3;
				if (( Resto1 == 0 ) || ( Resto1 == 1 ))
					PrimeiroDigito = 0;
				else
					PrimeiroDigito = 11 - Resto1;
				DF4 = 11 * D1[1] + 10 * D1[2] + 9 * D1[3] + 8 * D1[4] + 7 * D1[5] + 6 * D1[6] + 5 * D1[7] + 4 * D1[8] + 3 * D1[9] + 2 * PrimeiroDigito;
				DF5 = DF4 / 11;
				DF6 = DF5 * 11;
				Resto2 = DF4 - DF6;
				if (( Resto2 == 0 ) || ( Resto2 == 1 ))
					SegundoDigito = 0;
				else
					SegundoDigito = 11 - Resto2;
				if (( PrimeiroDigito != Integer.parseInt(Dado[9] + "") ) || ( SegundoDigito != Integer.parseInt(Dado[10] + "") ))
					bRetorno = false;
			}
		}
		else if (sDado.trim().length() != 0)
			bRetorno = false;
		return bRetorno;
	}

	public static boolean validaIE(String sIE, String sEstado) {
		boolean bRetorno = false;
		sEstado.toUpperCase();
		UF uf = UF.valueOf(sEstado);
		try {
			if ("ISENTO".equals(sIE.trim())) {
				return true;
			}
			if ( uf == null ) {
				bRetorno = false;
			} else {
				InscricaoEstadual ie = uf.getInscricaoEstadual();
				ie.setNumber(sIE);
				bRetorno = ie.isValid();
			}
			// testaCasoIE( sIE, ie );
		}
		catch (Exception e) {
			Funcoes.mensagemErro(null, "Erro ao validar a inscrição estadual!\n" + e.getMessage());
			e.printStackTrace();
		}
		return bRetorno;
	}

	public static boolean validaEmail(String email){    
		Pattern validaemail = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$");
		if (email==null) {
			return false;
		} else {
			return validaemail.matcher(email.trim()).matches();
		}
	}
	
	public static String formataIE(String sIE, String estado) {
		String ret = "";

		try {

			UF uf = UF.valueOf(estado);

			InscricaoEstadual IE = uf.getInscricaoEstadual();

			String mascara = IE.getMask();

			return setMascara(StringFunctions.clearString(sIE), mascara);

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

	// private static boolean testaCasoIE(String sIE, InscricaoEstadual IE) {
	// String mascara = IE.getMask();
	// sIEValida = setMascara( StringFunctions.clearString( sIE ),
	// IE.getMask());
	// return true;
	// }

	/*
	 * private static int[] carregaPosDV(Vector<String> vXIE) { int[] aRetorno =
	 * new int[2]; aRetorno[0] = -1; aRetorno[1] = -1; for (int i = 10; i <= 24;
	 * i++) { if (vXIE.elementAt(i) != null) { if (((String)
	 * vXIE.elementAt(i)).compareTo("DVX") == 0) aRetorno[0] = i - 10; else if
	 * (((String) vXIE.elementAt(i)).compareTo("DVY") == 0) aRetorno[1] = i -
	 * 10; } } return aRetorno; }
	 * 
	 * private static void montaTabPesoIE() {
	 * 
	 * Vector<Object> linha01 = new Vector<Object>(); Vector<Object> linha02 =
	 * new Vector<Object>(); Vector<Object> linha03 = new Vector<Object>();
	 * Vector<Object> linha04 = new Vector<Object>(); Vector<Object> linha05 =
	 * new Vector<Object>(); Vector<Object> linha06 = new Vector<Object>();
	 * Vector<Object> linha07 = new Vector<Object>(); Vector<Object> linha08 =
	 * new Vector<Object>(); Vector<Object> linha09 = new Vector<Object>();
	 * Vector<Object> linha10 = new Vector<Object>(); Vector<Object> linha11 =
	 * new Vector<Object>(); Vector<Object> linha12 = new Vector<Object>();
	 * Vector<Object> linha13 = new Vector<Object>();
	 * 
	 * linha01.addElement("P1"); linha01.addElement(new Integer(6));
	 * linha01.addElement(new Integer(5)); linha01.addElement(new Integer(4));
	 * linha01.addElement(new Integer(3)); linha01.addElement(new Integer(2));
	 * linha01.addElement(new Integer(9)); linha01.addElement(new Integer(8));
	 * linha01.addElement(new Integer(7)); linha01.addElement(new Integer(6));
	 * linha01.addElement(new Integer(5)); linha01.addElement(new Integer(4));
	 * linha01.addElement(new Integer(3)); linha01.addElement(new Integer(2));
	 * linha01.addElement(new Integer(0)); vPesoIE.addElement(linha01);
	 * 
	 * linha02.addElement("P2"); linha02.addElement(new Integer(5));
	 * linha02.addElement(new Integer(4)); linha02.addElement(new Integer(3));
	 * linha02.addElement(new Integer(2)); linha02.addElement(new Integer(9));
	 * linha02.addElement(new Integer(8)); linha02.addElement(new Integer(7));
	 * linha02.addElement(new Integer(6)); linha02.addElement(new Integer(5));
	 * linha02.addElement(new Integer(4)); linha02.addElement(new Integer(3));
	 * linha02.addElement(new Integer(2)); linha02.addElement(new Integer(0));
	 * linha02.addElement(new Integer(0)); vPesoIE.addElement(linha02);
	 * 
	 * linha03.addElement("P3"); linha03.addElement(new Integer(6));
	 * linha03.addElement(new Integer(5)); linha03.addElement(new Integer(4));
	 * linha03.addElement(new Integer(3)); linha03.addElement(new Integer(2));
	 * linha03.addElement(new Integer(9)); linha03.addElement(new Integer(8));
	 * linha03.addElement(new Integer(7)); linha03.addElement(new Integer(6));
	 * linha03.addElement(new Integer(5)); linha03.addElement(new Integer(4));
	 * linha03.addElement(new Integer(3)); linha03.addElement(new Integer(0));
	 * linha03.addElement(new Integer(2)); vPesoIE.addElement(linha03);
	 * 
	 * linha04.addElement("P4"); linha04.addElement(new Integer(0));
	 * linha04.addElement(new Integer(0)); linha04.addElement(new Integer(0));
	 * linha04.addElement(new Integer(0)); linha04.addElement(new Integer(0));
	 * linha04.addElement(new Integer(1)); linha04.addElement(new Integer(2));
	 * linha04.addElement(new Integer(3)); linha04.addElement(new Integer(4));
	 * linha04.addElement(new Integer(5)); linha04.addElement(new Integer(6));
	 * linha04.addElement(new Integer(7)); linha04.addElement(new Integer(8));
	 * linha04.addElement(new Integer(0)); vPesoIE.addElement(linha04);
	 * 
	 * linha05.addElement("P5"); linha05.addElement(new Integer(0));
	 * linha05.addElement(new Integer(0)); linha05.addElement(new Integer(0));
	 * linha05.addElement(new Integer(0)); linha05.addElement(new Integer(0));
	 * linha05.addElement(new Integer(1)); linha05.addElement(new Integer(2));
	 * linha05.addElement(new Integer(3)); linha05.addElement(new Integer(4));
	 * linha05.addElement(new Integer(5)); linha05.addElement(new Integer(6));
	 * linha05.addElement(new Integer(7)); linha05.addElement(new Integer(8));
	 * linha05.addElement(new Integer(0)); vPesoIE.addElement(linha05);
	 * 
	 * linha06.addElement("P6"); linha06.addElement(new Integer(0));
	 * linha06.addElement(new Integer(0)); linha06.addElement(new Integer(0));
	 * linha06.addElement(new Integer(9)); linha06.addElement(new Integer(8));
	 * linha06.addElement(new Integer(0)); linha06.addElement(new Integer(0));
	 * linha06.addElement(new Integer(7)); linha06.addElement(new Integer(6));
	 * linha06.addElement(new Integer(5)); linha06.addElement(new Integer(4));
	 * linha06.addElement(new Integer(3)); linha06.addElement(new Integer(2));
	 * linha06.addElement(new Integer(0)); vPesoIE.addElement(linha06);
	 * 
	 * linha07.addElement("P7"); linha07.addElement(new Integer(5));
	 * linha07.addElement(new Integer(4)); linha07.addElement(new Integer(3));
	 * linha07.addElement(new Integer(2)); linha07.addElement(new Integer(1));
	 * linha07.addElement(new Integer(9)); linha07.addElement(new Integer(8));
	 * linha07.addElement(new Integer(7)); linha07.addElement(new Integer(6));
	 * linha07.addElement(new Integer(5)); linha07.addElement(new Integer(4));
	 * linha07.addElement(new Integer(3)); linha07.addElement(new Integer(2));
	 * linha07.addElement(new Integer(0)); vPesoIE.addElement(linha07);
	 * 
	 * linha08.addElement("P8"); linha08.addElement(new Integer(8));
	 * linha08.addElement(new Integer(7)); linha08.addElement(new Integer(6));
	 * linha08.addElement(new Integer(5)); linha08.addElement(new Integer(4));
	 * linha08.addElement(new Integer(3)); linha08.addElement(new Integer(2));
	 * linha08.addElement(new Integer(7)); linha08.addElement(new Integer(6));
	 * linha08.addElement(new Integer(5)); linha08.addElement(new Integer(4));
	 * linha08.addElement(new Integer(3)); linha08.addElement(new Integer(2));
	 * linha08.addElement(new Integer(0)); vPesoIE.addElement(linha08);
	 * 
	 * linha09.addElement("P9"); linha09.addElement(new Integer(7));
	 * linha09.addElement(new Integer(6)); linha09.addElement(new Integer(5));
	 * linha09.addElement(new Integer(4)); linha09.addElement(new Integer(3));
	 * linha09.addElement(new Integer(2)); linha09.addElement(new Integer(7));
	 * linha09.addElement(new Integer(6)); linha09.addElement(new Integer(5));
	 * linha09.addElement(new Integer(4)); linha09.addElement(new Integer(3));
	 * linha09.addElement(new Integer(2)); linha09.addElement(new Integer(0));
	 * linha09.addElement(new Integer(0)); vPesoIE.addElement(linha09);
	 * 
	 * linha10.addElement("P10"); linha10.addElement(new Integer(0));
	 * linha10.addElement(new Integer(1)); linha10.addElement(new Integer(2));
	 * linha10.addElement(new Integer(1)); linha10.addElement(new Integer(1));
	 * linha10.addElement(new Integer(2)); linha10.addElement(new Integer(1));
	 * linha10.addElement(new Integer(2)); linha10.addElement(new Integer(1));
	 * linha10.addElement(new Integer(2)); linha10.addElement(new Integer(1));
	 * linha10.addElement(new Integer(2)); linha10.addElement(new Integer(0));
	 * linha10.addElement(new Integer(0)); vPesoIE.addElement(linha10);
	 * 
	 * linha11.addElement("P11"); linha11.addElement(new Integer(0));
	 * linha11.addElement(new Integer(3)); linha11.addElement(new Integer(2));
	 * linha11.addElement(new Integer(11)); linha11.addElement(new Integer(10));
	 * linha11.addElement(new Integer(9)); linha11.addElement(new Integer(8));
	 * linha11.addElement(new Integer(7)); linha11.addElement(new Integer(6));
	 * linha11.addElement(new Integer(5)); linha11.addElement(new Integer(4));
	 * linha11.addElement(new Integer(3)); linha11.addElement(new Integer(2));
	 * linha11.addElement(new Integer(0)); vPesoIE.addElement(linha11);
	 * 
	 * linha12.addElement("P12"); linha12.addElement(new Integer(0));
	 * linha12.addElement(new Integer(0)); linha12.addElement(new Integer(1));
	 * linha12.addElement(new Integer(3)); linha12.addElement(new Integer(4));
	 * linha12.addElement(new Integer(5)); linha12.addElement(new Integer(6));
	 * linha12.addElement(new Integer(7)); linha12.addElement(new Integer(8));
	 * linha12.addElement(new Integer(10)); linha12.addElement(new Integer(0));
	 * linha12.addElement(new Integer(0)); linha12.addElement(new Integer(0));
	 * linha12.addElement(new Integer(0)); vPesoIE.addElement(linha12);
	 * 
	 * linha13.addElement("P13"); linha13.addElement(new Integer(0));
	 * linha13.addElement(new Integer(0)); linha13.addElement(new Integer(3));
	 * linha13.addElement(new Integer(2)); linha13.addElement(new Integer(10));
	 * linha13.addElement(new Integer(9)); linha13.addElement(new Integer(8));
	 * linha13.addElement(new Integer(7)); linha13.addElement(new Integer(6));
	 * linha13.addElement(new Integer(5)); linha13.addElement(new Integer(4));
	 * linha13.addElement(new Integer(3)); linha13.addElement(new Integer(2));
	 * linha13.addElement(new Integer(0)); vPesoIE.addElement(linha13); }
	 * 
	 * private static void montaTabCalcIE() { Vector<Object> linha01 = new
	 * Vector<Object>(); Vector<Object> linha02 = new Vector<Object>();
	 * Vector<Object> linha03 = new Vector<Object>(); Vector<Object> linha04 =
	 * new Vector<Object>(); Vector<Object> linha05 = new Vector<Object>();
	 * Vector<Object> linha06 = new Vector<Object>(); Vector<Object> linha07 =
	 * new Vector<Object>(); Vector<Object> linha08 = new Vector<Object>();
	 * Vector<Object> linha09 = new Vector<Object>(); Vector<Object> linha10 =
	 * new Vector<Object>(); Vector<Object> linha11 = new Vector<Object>();
	 * Vector<Object> linha12 = new Vector<Object>(); Vector<Object> linha13 =
	 * new Vector<Object>(); Vector<Object> linha14 = new Vector<Object>();
	 * Vector<Object> linha15 = new Vector<Object>(); Vector<Object> linha16 =
	 * new Vector<Object>(); Vector<Object> linha17 = new Vector<Object>();
	 * Vector<Object> linha18 = new Vector<Object>(); Vector<Object> linha19 =
	 * new Vector<Object>(); Vector<Object> linha20 = new Vector<Object>();
	 * Vector<Object> linha21 = new Vector<Object>(); Vector<Object> linha22 =
	 * new Vector<Object>(); Vector<Object> linha23 = new Vector<Object>();
	 * Vector<Object> linha24 = new Vector<Object>(); Vector<Object> linha25 =
	 * new Vector<Object>(); Vector<Object> linha26 = new Vector<Object>();
	 * Vector<Object> linha27 = new Vector<Object>(); Vector<Object> linha28 =
	 * new Vector<Object>(); Vector<Object> linha29 = new Vector<Object>();
	 * Vector<Object> linha30 = new Vector<Object>(); Vector<Object> linha31 =
	 * new Vector<Object>(); Vector<Object> linha32 = new Vector<Object>();
	 * Vector<Object> linha33 = new Vector<Object>(); Vector<Object> linha34 =
	 * new Vector<Object>();
	 * 
	 * linha01.addElement("AC"); linha01.addElement(new Integer(1));
	 * linha01.addElement(new Integer(9)); linha01.addElement(new Integer(0));
	 * linha01.addElement("E"); linha01.addElement(new Integer(11));
	 * linha01.addElement("P1"); linha01.addElement(null);
	 * linha01.addElement(null); linha01.addElement(null);
	 * linha01.addElement(null); linha01.addElement(null);
	 * linha01.addElement(null); linha01.addElement(null);
	 * linha01.addElement(null); linha01.addElement("=0");
	 * linha01.addElement("=1"); linha01.addElement("0/9");
	 * linha01.addElement("0/9"); linha01.addElement("0/9");
	 * linha01.addElement("0/9"); linha01.addElement("0/9");
	 * linha01.addElement("0/9"); linha01.addElement("DVX");
	 * linha01.addElement("#########"); vIE.addElement(linha01);
	 * 
	 * linha02.addElement("AC"); linha02.addElement(new Integer(2));
	 * linha02.addElement(new Integer(13)); linha02.addElement(new Integer(0));
	 * linha02.addElement("E"); linha02.addElement(new Integer(11));
	 * linha02.addElement("P2"); linha02.addElement("E"); linha02.addElement(new
	 * Integer(11)); linha02.addElement("P1"); linha02.addElement(null);
	 * linha02.addElement("=0"); linha02.addElement("=1");
	 * linha02.addElement("0/9"); linha02.addElement("0/9");
	 * linha02.addElement("0/9"); linha02.addElement("0/9");
	 * linha02.addElement("0/9"); linha02.addElement("0/9");
	 * linha02.addElement("0/9"); linha02.addElement("0/9");
	 * linha02.addElement("0/9"); linha02.addElement("DVX");
	 * linha02.addElement("DVY"); linha02.addElement("########/###-##");
	 * vIE.addElement(linha02);
	 * 
	 * linha03.addElement("AL"); linha03.addElement(new Integer(1));
	 * linha03.addElement(new Integer(9)); linha03.addElement(new Integer(0));
	 * linha03.addElement("B,D"); linha03.addElement(new Integer(11));
	 * linha03.addElement("P1"); linha03.addElement(null);
	 * linha03.addElement(null); linha03.addElement(null);
	 * linha03.addElement(null); linha03.addElement(null);
	 * linha03.addElement(null); linha03.addElement(null);
	 * linha03.addElement(null); linha03.addElement("=2");
	 * linha03.addElement("=4"); linha03.addElement("=0,3,5,7,8");
	 * linha03.addElement("0/9"); linha03.addElement("0/9");
	 * linha03.addElement("0/9"); linha03.addElement("0/9");
	 * linha03.addElement("0/9"); linha03.addElement("DVX");
	 * linha03.addElement("#########"); vIE.addElement(linha03);
	 * 
	 * linha04.addElement("AP"); linha04.addElement(new Integer(1));
	 * linha04.addElement(new Integer(9)); linha04.addElement(new Integer(0));
	 * linha04.addElement("C,E"); linha04.addElement(new Integer(11));
	 * linha04.addElement("P1"); linha04.addElement(null);
	 * linha04.addElement(null); linha04.addElement(null);
	 * linha04.addElement(null); linha04.addElement(null);
	 * linha04.addElement(null); linha04.addElement(null);
	 * linha04.addElement(null); linha04.addElement("=0");
	 * linha04.addElement("=3"); linha04.addElement("0/9");
	 * linha04.addElement("0/9"); linha04.addElement("0/9");
	 * linha04.addElement("0/9"); linha04.addElement("0/9");
	 * linha04.addElement("0/9"); linha04.addElement("DVX");
	 * linha04.addElement("#########"); vIE.addElement(linha04);
	 * 
	 * linha05.addElement("AP"); linha05.addElement(new Integer(2));
	 * linha05.addElement(new Integer(9)); linha05.addElement(new Integer(1));
	 * linha05.addElement("C,E"); linha05.addElement(new Integer(11));
	 * linha05.addElement("P1"); linha05.addElement(null);
	 * linha05.addElement(null); linha05.addElement(null);
	 * linha05.addElement(null); linha05.addElement(null);
	 * linha05.addElement(null); linha05.addElement(null);
	 * linha05.addElement(null); linha05.addElement("=0");
	 * linha05.addElement("=3"); linha05.addElement("0/9");
	 * linha05.addElement("0/9"); linha05.addElement("0/9");
	 * linha05.addElement("0/9"); linha05.addElement("0/9");
	 * linha05.addElement("0/9"); linha05.addElement("DVX");
	 * linha05.addElement("#########"); vIE.addElement(linha05);
	 * 
	 * linha06.addElement("AP"); linha06.addElement(new Integer(3));
	 * linha06.addElement(new Integer(9)); linha06.addElement(new Integer(0));
	 * linha06.addElement("E"); linha06.addElement(new Integer(11));
	 * linha06.addElement("P1"); linha06.addElement(null);
	 * linha06.addElement(null); linha06.addElement(null);
	 * linha06.addElement(null); linha06.addElement(null);
	 * linha06.addElement(null); linha06.addElement(null);
	 * linha06.addElement(null); linha06.addElement("=0");
	 * linha06.addElement("=3"); linha06.addElement("0/9");
	 * linha06.addElement("0/9"); linha06.addElement("0/9");
	 * linha06.addElement("0/9"); linha06.addElement("0/9");
	 * linha06.addElement("0/9"); linha06.addElement("DVX");
	 * linha06.addElement("#########"); vIE.addElement(linha06);
	 * 
	 * linha07.addElement("AM"); linha07.addElement(new Integer(1));
	 * linha07.addElement(new Integer(9)); linha07.addElement(new Integer(0));
	 * linha07.addElement("E"); linha07.addElement(new Integer(11));
	 * linha07.addElement("P1"); linha07.addElement(null);
	 * linha07.addElement(null); linha07.addElement(null);
	 * linha07.addElement(null); linha07.addElement(null);
	 * linha07.addElement(null); linha07.addElement(null);
	 * linha07.addElement(null); linha07.addElement("=0");
	 * linha07.addElement("=4,7"); linha07.addElement("0/9");
	 * linha07.addElement("0/9"); linha07.addElement("0/9");
	 * linha07.addElement("0/9"); linha07.addElement("0/9");
	 * linha07.addElement("0/9"); linha07.addElement("DVX");
	 * linha07.addElement("##.###.###-#"); vIE.addElement(linha07);
	 * 
	 * linha08.addElement("BA"); linha08.addElement(new Integer(1));
	 * linha08.addElement(new Integer(8)); linha08.addElement(new Integer(0));
	 * linha08.addElement("E"); linha08.addElement(new Integer(10));
	 * linha08.addElement("P2"); linha08.addElement("E"); linha08.addElement(new
	 * Integer(10)); linha08.addElement("P3"); linha08.addElement(null);
	 * linha08.addElement(null); linha08.addElement(null);
	 * linha08.addElement(null); linha08.addElement(null);
	 * linha08.addElement(null); linha08.addElement("0/9");
	 * linha08.addElement("0/9"); linha08.addElement("0/9");
	 * linha08.addElement("0/9"); linha08.addElement("0/9");
	 * linha08.addElement("0/9"); linha08.addElement("DVY");
	 * linha08.addElement("DVX"); linha08.addElement("######-##");
	 * vIE.addElement(linha08);
	 * 
	 * linha09.addElement("BA"); linha09.addElement(new Integer(2));
	 * linha09.addElement(new Integer(8)); linha09.addElement(new Integer(0));
	 * linha09.addElement("E"); linha09.addElement(new Integer(11));
	 * linha09.addElement("P2"); linha09.addElement("E"); linha09.addElement(new
	 * Integer(11)); linha09.addElement("P3"); linha09.addElement(null);
	 * linha09.addElement(null); linha09.addElement(null);
	 * linha09.addElement(null); linha09.addElement(null);
	 * linha09.addElement(null); linha09.addElement("0/9");
	 * linha09.addElement("0/9"); linha09.addElement("0/9");
	 * linha09.addElement("0/9"); linha09.addElement("0/9");
	 * linha09.addElement("0/9"); linha09.addElement("DVY");
	 * linha09.addElement("DVX"); linha09.addElement("######-##");
	 * vIE.addElement(linha09);
	 * 
	 * linha10.addElement("CE"); linha10.addElement(new Integer(1));
	 * linha10.addElement(new Integer(9)); linha10.addElement(new Integer(0));
	 * linha10.addElement("E"); linha10.addElement(new Integer(11));
	 * linha10.addElement("P1"); linha10.addElement(null);
	 * linha10.addElement(null); linha10.addElement(null);
	 * linha10.addElement(null); linha10.addElement(null);
	 * linha10.addElement(null); linha10.addElement(null);
	 * linha10.addElement(null); linha10.addElement("=0");
	 * linha10.addElement("0/9"); linha10.addElement("0/9");
	 * linha10.addElement("0/9"); linha10.addElement("0/9");
	 * linha10.addElement("0/9"); linha10.addElement("0/9");
	 * linha10.addElement("0/9"); linha10.addElement("DVX");
	 * linha10.addElement("########-#"); vIE.addElement(linha10);
	 * 
	 * linha11.addElement("DF"); linha11.addElement(new Integer(1));
	 * linha11.addElement(new Integer(13)); linha11.addElement(new Integer(0));
	 * linha11.addElement("E"); linha11.addElement(new Integer(11));
	 * linha11.addElement("P2"); linha11.addElement("E"); linha11.addElement(new
	 * Integer(11)); linha11.addElement("P1"); linha11.addElement(null);
	 * linha11.addElement("=0"); linha11.addElement("=7");
	 * linha11.addElement("=3,4"); linha11.addElement("0/9");
	 * linha11.addElement("0/9"); linha11.addElement("0/9");
	 * linha11.addElement("0/9"); linha11.addElement("0/9");
	 * linha11.addElement("0/9"); linha11.addElement("0/9");
	 * linha11.addElement("0/9"); linha11.addElement("DVX");
	 * linha11.addElement("DVY"); linha11.addElement("###.########-##");
	 * vIE.addElement(linha11);
	 * 
	 * linha12.addElement("ES"); linha12.addElement(new Integer(1));
	 * linha12.addElement(new Integer(9)); linha12.addElement(new Integer(0));
	 * linha12.addElement("E"); linha12.addElement(new Integer(11));
	 * linha12.addElement("P1"); linha12.addElement(null);
	 * linha12.addElement(null); linha12.addElement(null);
	 * linha12.addElement(null); linha12.addElement(null);
	 * linha12.addElement(null); linha12.addElement(null);
	 * linha12.addElement(null); linha12.addElement("=0");
	 * linha12.addElement("=0,8"); linha12.addElement("0/9");
	 * linha12.addElement("0/9"); linha12.addElement("0/9");
	 * linha12.addElement("0/9"); linha12.addElement("0/9");
	 * linha12.addElement("0/9"); linha12.addElement("DVX");
	 * linha12.addElement("########-#"); vIE.addElement(linha12);
	 * 
	 * linha13.addElement("GO"); linha13.addElement(new Integer(1));
	 * linha13.addElement(new Integer(9)); linha13.addElement(new Integer(1));
	 * linha13.addElement("E"); linha13.addElement(new Integer(11));
	 * linha13.addElement("P1"); linha13.addElement(null);
	 * linha13.addElement(null); linha13.addElement(null);
	 * linha13.addElement(null); linha13.addElement(null);
	 * linha13.addElement(null); linha13.addElement(null);
	 * linha13.addElement(null); linha13.addElement("=1");
	 * linha13.addElement("=0,1,5"); linha13.addElement("0/9");
	 * linha13.addElement("0/9"); linha13.addElement("0/9");
	 * linha13.addElement("0/9"); linha13.addElement("0/9");
	 * linha13.addElement("0/9"); linha13.addElement("DVX");
	 * linha13.addElement("##.###.###-#"); vIE.addElement(linha13);
	 * 
	 * linha14.addElement("GO"); linha14.addElement(new Integer(2));
	 * linha14.addElement(new Integer(9)); linha14.addElement(new Integer(0));
	 * linha14.addElement("E"); linha14.addElement(new Integer(11));
	 * linha14.addElement("P1"); linha14.addElement(null);
	 * linha14.addElement(null); linha14.addElement(null);
	 * linha14.addElement(null); linha14.addElement(null);
	 * linha14.addElement(null); linha14.addElement(null);
	 * linha14.addElement(null); linha14.addElement("=1");
	 * linha14.addElement("=0,1,5"); linha14.addElement("0/9");
	 * linha14.addElement("0/9"); linha14.addElement("0/9");
	 * linha14.addElement("0/9"); linha14.addElement("0/9");
	 * linha14.addElement("0/9"); linha14.addElement("DVX");
	 * linha14.addElement("##.###.###-#"); vIE.addElement(linha14);
	 * 
	 * linha15.addElement("MA"); linha15.addElement(new Integer(1));
	 * linha15.addElement(new Integer(9)); linha15.addElement(new Integer(0));
	 * linha15.addElement("E"); linha15.addElement(new Integer(11));
	 * linha15.addElement("P1"); linha15.addElement(null);
	 * linha15.addElement(null); linha15.addElement(null);
	 * linha15.addElement(null); linha15.addElement(null);
	 * linha15.addElement(null); linha15.addElement(null);
	 * linha15.addElement(null); linha15.addElement("=1");
	 * linha15.addElement("=2"); linha15.addElement("0/9");
	 * linha15.addElement("0/9"); linha15.addElement("0/9");
	 * linha15.addElement("0/9"); linha15.addElement("0/9");
	 * linha15.addElement("0/9"); linha15.addElement("DVX");
	 * linha15.addElement("#########"); vIE.addElement(linha15);
	 * 
	 * linha16.addElement("MT"); linha16.addElement(new Integer(1));
	 * linha16.addElement(new Integer(11)); linha16.addElement(new Integer(0));
	 * linha16.addElement("E"); linha16.addElement(new Integer(11));
	 * linha16.addElement("P1"); linha16.addElement(null);
	 * linha16.addElement(null); linha16.addElement(null);
	 * linha16.addElement(null); linha16.addElement(null);
	 * linha16.addElement(null); linha16.addElement("0/9");
	 * linha16.addElement("0/9"); linha16.addElement("0/9");
	 * linha16.addElement("0/9"); linha16.addElement("0/9");
	 * linha16.addElement("0/9"); linha16.addElement("0/9");
	 * linha16.addElement("0/9"); linha16.addElement("0/9");
	 * linha16.addElement("0/9"); linha16.addElement("DVX");
	 * linha16.addElement("##########-#"); vIE.addElement(linha16);
	 * 
	 * linha17.addElement("MS"); linha17.addElement(new Integer(1));
	 * linha17.addElement(new Integer(9)); linha17.addElement(new Integer(0));
	 * linha17.addElement("E"); linha17.addElement(new Integer(11));
	 * linha17.addElement("P1"); linha17.addElement(null);
	 * linha17.addElement(null); linha17.addElement(null);
	 * linha17.addElement(null); linha17.addElement(null);
	 * linha17.addElement(null); linha17.addElement(null);
	 * linha17.addElement(null); linha17.addElement("=2");
	 * linha17.addElement("=8"); linha17.addElement("0/9");
	 * linha17.addElement("0/9"); linha17.addElement("0/9");
	 * linha17.addElement("0/9"); linha17.addElement("0/9");
	 * linha17.addElement("0/9"); linha17.addElement("DVX");
	 * linha17.addElement("#########"); vIE.addElement(linha17);
	 * 
	 * linha18.addElement("MG"); linha18.addElement(new Integer(1));
	 * linha18.addElement(new Integer(13)); linha18.addElement(new Integer(0));
	 * linha18.addElement("A,E"); linha18.addElement(new Integer(10));
	 * linha18.addElement("P10"); linha18.addElement("E");
	 * linha18.addElement(new Integer(11)); linha18.addElement("P11");
	 * linha18.addElement(null); linha18.addElement("0/9");
	 * linha18.addElement("0/9"); linha18.addElement("0/9");
	 * linha18.addElement("0/9"); linha18.addElement("0/9");
	 * linha18.addElement("0/9"); linha18.addElement("0/9");
	 * linha18.addElement("0/9"); linha18.addElement("0/9");
	 * linha18.addElement("0/9"); linha18.addElement("0/9");
	 * linha18.addElement("DVX"); linha18.addElement("DVY");
	 * linha18.addElement("###.######/####"); vIE.addElement(linha18);
	 * 
	 * linha19.addElement("PA"); linha19.addElement(new Integer(1));
	 * linha19.addElement(new Integer(9)); linha19.addElement(new Integer(0));
	 * linha19.addElement("E"); linha19.addElement(new Integer(11));
	 * linha19.addElement("P1"); linha19.addElement(null);
	 * linha19.addElement(null); linha19.addElement(null);
	 * linha19.addElement(null); linha19.addElement(null);
	 * linha19.addElement(null); linha19.addElement(null);
	 * linha19.addElement(null); linha19.addElement("=1");
	 * linha19.addElement("=5"); linha19.addElement("0/9");
	 * linha19.addElement("0/9"); linha19.addElement("0/9");
	 * linha19.addElement("0/9"); linha19.addElement("0/9");
	 * linha19.addElement("0/9"); linha19.addElement("DVX");
	 * linha19.addElement("########-#"); vIE.addElement(linha19);
	 * 
	 * linha20.addElement("PB"); linha20.addElement(new Integer(1));
	 * linha20.addElement(new Integer(9)); linha20.addElement(new Integer(0));
	 * linha20.addElement("E"); linha20.addElement(new Integer(11));
	 * linha20.addElement("P1"); linha20.addElement(null);
	 * linha20.addElement(null); linha20.addElement(null);
	 * linha20.addElement(null); linha20.addElement(null);
	 * linha20.addElement(null); linha20.addElement(null);
	 * linha20.addElement(null); linha20.addElement("=1");
	 * linha20.addElement("=6"); linha20.addElement("0/9");
	 * linha20.addElement("0/9"); linha20.addElement("0/9");
	 * linha20.addElement("0/9"); linha20.addElement("0/9");
	 * linha20.addElement("0/9"); linha20.addElement("DVX");
	 * linha20.addElement("########-#"); vIE.addElement(linha20);
	 * 
	 * linha21.addElement("PR"); linha21.addElement(new Integer(1));
	 * linha21.addElement(new Integer(10)); linha21.addElement(new Integer(0));
	 * linha21.addElement("E"); linha21.addElement(new Integer(11));
	 * linha21.addElement("P9"); linha21.addElement("E"); linha21.addElement(new
	 * Integer(11)); linha21.addElement("P8"); linha21.addElement(null);
	 * linha21.addElement(null); linha21.addElement(null);
	 * linha21.addElement(null); linha21.addElement("0/9");
	 * linha21.addElement("0/9"); linha21.addElement("0/9");
	 * linha21.addElement("0/9"); linha21.addElement("0/9");
	 * linha21.addElement("0/9"); linha21.addElement("0/9");
	 * linha21.addElement("0/9"); linha21.addElement("DVX");
	 * linha21.addElement("DVY"); linha21.addElement("########-##");
	 * vIE.addElement(linha21);
	 * 
	 * linha22.addElement("PE"); linha22.addElement(new Integer(1));
	 * linha22.addElement(new Integer(14)); linha22.addElement(new Integer(1));
	 * linha22.addElement("E"); linha22.addElement(new Integer(11));
	 * linha22.addElement("P7"); linha22.addElement(null);
	 * linha22.addElement(null); linha22.addElement(null);
	 * linha22.addElement("=1"); linha22.addElement("=8");
	 * linha22.addElement("1/9"); linha22.addElement("0/9");
	 * linha22.addElement("0/9"); linha22.addElement("0/9");
	 * linha22.addElement("0/9"); linha22.addElement("0/9");
	 * linha22.addElement("0/9"); linha22.addElement("0/9");
	 * linha22.addElement("0/9"); linha22.addElement("0/9");
	 * linha22.addElement("0/9"); linha22.addElement("DVX");
	 * linha22.addElement("#############-#"); vIE.addElement(linha22);
	 * 
	 * linha23.addElement("PI"); linha23.addElement(new Integer(1));
	 * linha23.addElement(new Integer(9)); linha23.addElement(new Integer(0));
	 * linha23.addElement("E"); linha23.addElement(new Integer(11));
	 * linha23.addElement("P1"); linha23.addElement(null);
	 * linha23.addElement(null); linha23.addElement(null);
	 * linha23.addElement(null); linha23.addElement(null);
	 * linha23.addElement(null); linha23.addElement(null);
	 * linha23.addElement(null); linha23.addElement("=1");
	 * linha23.addElement("=9"); linha23.addElement("0/9");
	 * linha23.addElement("0/9"); linha23.addElement("0/9");
	 * linha23.addElement("0/9"); linha23.addElement("0/9");
	 * linha23.addElement("0/9"); linha23.addElement("DVX");
	 * linha23.addElement("#########"); vIE.addElement(linha23);
	 * 
	 * linha24.addElement("RJ"); linha24.addElement(new Integer(1));
	 * linha24.addElement(new Integer(8)); linha24.addElement(new Integer(0));
	 * linha24.addElement("E"); linha24.addElement(new Integer(11));
	 * linha24.addElement("P8"); linha24.addElement(null);
	 * linha24.addElement(null); linha24.addElement(null);
	 * linha24.addElement(null); linha24.addElement(null);
	 * linha24.addElement(null); linha24.addElement(null);
	 * linha24.addElement(null); linha24.addElement(null);
	 * linha24.addElement("=1,7,8,9"); linha24.addElement("0/9");
	 * linha24.addElement("0/9"); linha24.addElement("0/9");
	 * linha24.addElement("0/9"); linha24.addElement("0/9");
	 * linha24.addElement("0/9"); linha24.addElement("DVX");
	 * linha24.addElement("##.###.##-#"); vIE.addElement(linha24);
	 * 
	 * linha25.addElement("RN"); linha25.addElement(new Integer(1));
	 * linha25.addElement(new Integer(9)); linha25.addElement(new Integer(0));
	 * linha25.addElement("B,D"); linha25.addElement(new Integer(11));
	 * linha25.addElement("P1"); linha25.addElement(null);
	 * linha25.addElement(null); linha25.addElement(null);
	 * linha25.addElement(null); linha25.addElement(null);
	 * linha25.addElement(null); linha25.addElement(null);
	 * linha25.addElement(null); linha25.addElement("=2");
	 * linha25.addElement("=0"); linha25.addElement("0/3");
	 * linha25.addElement("0/9"); linha25.addElement("0/9");
	 * linha25.addElement("0/9"); linha25.addElement("0/9");
	 * linha25.addElement("0/9"); linha25.addElement("DVX");
	 * linha25.addElement("##.###.###-#"); vIE.addElement(linha25);
	 * 
	 * linha26.addElement("RS"); linha26.addElement(new Integer(1));
	 * linha26.addElement(new Integer(10)); linha26.addElement(new Integer(0));
	 * linha26.addElement("E"); linha26.addElement(new Integer(11));
	 * linha26.addElement("P1"); linha26.addElement(null);
	 * linha26.addElement(null); linha26.addElement(null);
	 * linha26.addElement(null); linha26.addElement(null);
	 * linha26.addElement(null); linha26.addElement(null);
	 * linha26.addElement("0/4"); linha26.addElement("0/9");
	 * linha26.addElement("0/9"); linha26.addElement("0/9");
	 * linha26.addElement("0/9"); linha26.addElement("0/9");
	 * linha26.addElement("0/9"); linha26.addElement("0/9");
	 * linha26.addElement("0/9"); linha26.addElement("DVX");
	 * linha26.addElement("###/#######"); vIE.addElement(linha26);
	 * 
	 * linha27.addElement("RO"); linha27.addElement(new Integer(1));
	 * linha27.addElement(new Integer(9)); linha27.addElement(new Integer(1));
	 * linha27.addElement("E"); linha27.addElement(new Integer(11));
	 * linha27.addElement("P4"); linha27.addElement(null);
	 * linha27.addElement(null); linha27.addElement(null);
	 * linha27.addElement(null); linha27.addElement(null);
	 * linha27.addElement(null); linha27.addElement(null);
	 * linha27.addElement(null); linha27.addElement("1/9");
	 * linha27.addElement("0/9"); linha27.addElement("0/9");
	 * linha27.addElement("0/9"); linha27.addElement("0/9");
	 * linha27.addElement("0/9"); linha27.addElement("0/9");
	 * linha27.addElement("0/9"); linha27.addElement("DVX");
	 * linha27.addElement("###.#####-#"); vIE.addElement(linha27);
	 * 
	 * linha28.addElement("RO"); linha28.addElement(new Integer(2));
	 * linha28.addElement(new Integer(14)); linha28.addElement(new Integer(0));
	 * linha28.addElement("E"); linha28.addElement(new Integer(11));
	 * linha28.addElement("P1"); linha28.addElement(null);
	 * linha28.addElement(null); linha28.addElement(null);
	 * linha28.addElement("0/9"); linha28.addElement("0/9");
	 * linha28.addElement("0/9"); linha28.addElement("0/9");
	 * linha28.addElement("0/9"); linha28.addElement("0/9");
	 * linha28.addElement("0/9"); linha28.addElement("0/9");
	 * linha28.addElement("0/9"); linha28.addElement("0/9");
	 * linha28.addElement("0/9"); linha28.addElement("0/9");
	 * linha28.addElement("0/9"); linha28.addElement("DVX");
	 * linha28.addElement("#############-#"); vIE.addElement(linha28);
	 * 
	 * linha29.addElement("RR"); linha29.addElement(new Integer(1));
	 * linha29.addElement(new Integer(9)); linha29.addElement(new Integer(0));
	 * linha29.addElement("D"); linha29.addElement(new Integer(9));
	 * linha29.addElement("P5"); linha29.addElement(null);
	 * linha29.addElement(null); linha29.addElement(null);
	 * linha29.addElement(null); linha29.addElement(null);
	 * linha29.addElement(null); linha29.addElement(null);
	 * linha29.addElement(null); linha29.addElement("=2");
	 * linha29.addElement("=4"); linha29.addElement("0/9");
	 * linha29.addElement("0/9"); linha29.addElement("0/9");
	 * linha29.addElement("0/9"); linha29.addElement("0/9");
	 * linha29.addElement("0/9"); linha29.addElement("DVX");
	 * linha29.addElement("########-#"); vIE.addElement(linha29);
	 * 
	 * linha30.addElement("SC"); linha30.addElement(new Integer(1));
	 * linha30.addElement(new Integer(9)); linha30.addElement(new Integer(0));
	 * linha30.addElement("E"); linha30.addElement(new Integer(11));
	 * linha30.addElement("P1"); linha30.addElement(null);
	 * linha30.addElement(null); linha30.addElement(null);
	 * linha30.addElement(null); linha30.addElement(null);
	 * linha30.addElement(null); linha30.addElement(null);
	 * linha30.addElement(null); linha30.addElement("0/9");
	 * linha30.addElement("0/9"); linha30.addElement("0/9");
	 * linha30.addElement("0/9"); linha30.addElement("0/9");
	 * linha30.addElement("0/9"); linha30.addElement("0/9");
	 * linha30.addElement("0/9"); linha30.addElement("DVX");
	 * linha30.addElement("###.###.###"); vIE.addElement(linha30);
	 * 
	 * linha31.addElement("SP"); linha31.addElement(new Integer(1));
	 * linha31.addElement(new Integer(12)); linha31.addElement(new Integer(0));
	 * linha31.addElement("D"); linha31.addElement(new Integer(11));
	 * linha31.addElement("P12"); linha31.addElement("D");
	 * linha31.addElement(new Integer(11)); linha31.addElement("P13");
	 * linha31.addElement(null); linha31.addElement(null);
	 * linha31.addElement("0/9"); linha31.addElement("0/9");
	 * linha31.addElement("0/9"); linha31.addElement("0/9");
	 * linha31.addElement("0/9"); linha31.addElement("0/9");
	 * linha31.addElement("0/9"); linha31.addElement("0/9");
	 * linha31.addElement("DVX"); linha31.addElement("0/9");
	 * linha31.addElement("0/9"); linha31.addElement("DVY");
	 * linha31.addElement("###.###.###.###"); vIE.addElement(linha31);
	 * 
	 * linha32.addElement("SP"); linha32.addElement(new Integer(2));
	 * linha32.addElement(new Integer(13)); linha32.addElement(new Integer(0));
	 * linha32.addElement("D"); linha32.addElement(new Integer(11));
	 * linha32.addElement("P12"); linha32.addElement(null);
	 * linha32.addElement(null); linha32.addElement(null);
	 * linha32.addElement(null); linha32.addElement("=P");
	 * linha32.addElement("0/9"); linha32.addElement("0/9");
	 * linha32.addElement("0/9"); linha32.addElement("0/9");
	 * linha32.addElement("0/9"); linha32.addElement("0/9");
	 * linha32.addElement("0/9"); linha32.addElement("0/9");
	 * linha32.addElement("DVX"); linha32.addElement("0/9");
	 * linha32.addElement("0/9"); linha32.addElement("0/9");
	 * linha32.addElement("###.###.###.###"); vIE.addElement(linha32);
	 * 
	 * linha33.addElement("SE"); linha33.addElement(new Integer(1));
	 * linha33.addElement(new Integer(9)); linha33.addElement(new Integer(0));
	 * linha33.addElement("E"); linha33.addElement(new Integer(11));
	 * linha33.addElement("P1"); linha33.addElement(null);
	 * linha33.addElement(null); linha33.addElement(null);
	 * linha33.addElement(null); linha33.addElement(null);
	 * linha33.addElement(null); linha33.addElement(null);
	 * linha33.addElement(null); linha33.addElement("0/9");
	 * linha33.addElement("0/9"); linha33.addElement("0/9");
	 * linha33.addElement("0/9"); linha33.addElement("0/9");
	 * linha33.addElement("0/9"); linha33.addElement("0/9");
	 * linha33.addElement("0/9"); linha33.addElement("DVX");
	 * linha33.addElement("########-#"); vIE.addElement(linha33);
	 * 
	 * linha34.addElement("TO"); linha34.addElement(new Integer(1));
	 * linha34.addElement(new Integer(11)); linha34.addElement(new Integer(0));
	 * linha34.addElement("E"); linha34.addElement(new Integer(11));
	 * linha34.addElement("P6"); linha34.addElement(null);
	 * linha34.addElement(null); linha34.addElement(null);
	 * linha34.addElement(null); linha34.addElement(null);
	 * linha34.addElement(null); linha34.addElement("=2");
	 * linha34.addElement("=9"); linha34.addElement("=0,9");
	 * linha34.addElement("=1,2,3,9"); linha34.addElement("0/9");
	 * linha34.addElement("0/9"); linha34.addElement("0/9");
	 * linha34.addElement("0/9"); linha34.addElement("0/9");
	 * linha34.addElement("0/9"); linha34.addElement("DVX");
	 * linha34.addElement("##.##.######.#"); vIE.addElement(linha34); }
	 */
	public static String transValor(BigDecimal bdValor, int iTam, int iDec, boolean bZeroEsq) {
		String sValor;
		sValor = strDecimalToStrCurrency(iTam + 1, iDec, "" + bdValor);

		// System.out.println("sValor: "+sValor);
		if (sValor.indexOf(",") >= 0)
			sValor = sValor.replaceAll("\\,", "");
		// System.out.println("sValor: "+sValor);

		if (sValor.indexOf(".") >= 0)
			sValor = sValor.replaceAll("\\.", "");
		// System.out.println("sValor: "+sValor);

		if (bZeroEsq)
			if (sValor.indexOf(" ") >= 0)
				sValor = sValor.replace(' ', '0');

		// System.out.println("sValor: "+sValor);

		return sValor;
	}

	public static String transStatusECF(final char arg) {

		String actionReturn = "";
		switch (arg) {
		case 'A':
			actionReturn = "Aberto";
			break;
		case 'U':
			actionReturn = "Suprimento";
			break;
		case 'S':
			actionReturn = "Sangria";
			break;
		case 'V':
			actionReturn = "Venda";
			break;
		case 'F':
			actionReturn = "Fechado";
			break;
		case 'Z':
			actionReturn = "Fechado (com LeituraZ)";
			break;
		default:
			actionReturn = "Não identificado";
		}
		return actionReturn;
	}

	public static long somaTime(Time inicio, Time fim) {
		Long lIni = null;
		Long lFim = null;
		try {
			Calendar clLimpaDiasIni = new GregorianCalendar();
			clLimpaDiasIni.setTimeInMillis(inicio.getTime());
			clLimpaDiasIni.set(Calendar.HOUR_OF_DAY, 0);
			clLimpaDiasIni.set(Calendar.MINUTE, 0);
			clLimpaDiasIni.set(Calendar.SECOND, 0);
			clLimpaDiasIni.set(Calendar.MILLISECOND, 0);

			Calendar clLimpaDiasFim = new GregorianCalendar();
			clLimpaDiasFim.setTimeInMillis(fim.getTime());
			clLimpaDiasFim.set(Calendar.HOUR_OF_DAY, 0);
			clLimpaDiasFim.set(Calendar.MINUTE, 0);
			clLimpaDiasFim.set(Calendar.SECOND, 0);
			clLimpaDiasFim.set(Calendar.MILLISECOND, 0);

			lIni = inicio.getTime() - clLimpaDiasIni.getTimeInMillis();
			lFim = fim.getTime() - clLimpaDiasFim.getTimeInMillis();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return lIni + lFim;
	}

	public static long subtraiTime(Time inicio, Time fim) {
		if (fim!=null && inicio!=null) {
			return fim.getTime() - inicio.getTime();
		} else {
			return 0;
		}
	}

	public static String longTostrTime(long time) {
		StringBuffer resultado = new StringBuffer();

		// verifica quantidade de dias.
		long dias = time / ( 1000 * 60 * 60 * 24 );
		time = time % ( 1000 * 60 * 60 * 24 );

		if (dias > 0) {
			resultado.append(dias);
			resultado.append(" DD ");
		}

		long horas = time / ( 1000 * 60 * 60 );
		time = time % ( 1000 * 60 * 60 );

		if (horas < 10) {
			resultado.append("0");
		}
		resultado.append(horas);
		resultado.append(":");

		long minutos = time / ( 1000 * 60 );
		time = time % ( 1000 * 60 );

		if (minutos < 10) {
			resultado.append("0");
		}
		resultado.append(minutos);
		resultado.append(":");

		// verifica quantidade de segundos.
		long segundos = time / 1000;
		time = time % 1000;

		if (segundos < 10) {
			resultado.append("0");
		}
		resultado.append(segundos);

		return resultado.toString();

	}
	
	public static String longTostrTimeHoras(long time) {
		StringBuffer resultado = new StringBuffer();

		// verifica quantidade de dias.
		long dias = time / ( 1000 * 60 * 60 * 24 );
		time = time % ( 1000 * 60 * 60 * 24 );
		long horadia = 0;

		if (dias > 0) {
			horadia = dias * 24;
		}

		long horas = time / ( 1000 * 60 * 60 );
		time = time % ( 1000 * 60 * 60 );

		if (horas+horadia < 10) {
			resultado.append("0");
		}
		resultado.append(horas + horadia);
		resultado.append(":");

		long minutos = time / ( 1000 * 60 );
		time = time % ( 1000 * 60 );

		if (minutos < 10) {
			resultado.append("0");
		}
		resultado.append(minutos);
		resultado.append(":");

		// verifica quantidade de segundos.
		long segundos = time / 1000;
		time = time % 1000;

		if (segundos < 10) {
			resultado.append("0");
		}
		resultado.append(segundos);

		return resultado.toString();

	}

	public static Date somaMes(Date dt, int imeses) {
		Date dt2 = dt;
		Calendar cl = new GregorianCalendar();
		try {
			cl.setTime(dt);
			cl.add(Calendar.MONTH, imeses);
			dt2 = cl.getTime();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return dt2;
	}

	public static Date subtraiMes(Date dt, int imeses) {
		Date dt2 = dt;
		Calendar cl = new GregorianCalendar();
		try {
			cl.setTime(dt);
			cl.add(Calendar.MONTH, imeses * -1);
			dt2 = cl.getTime();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return dt2;
	}

	public static BigDecimal getMedia(BigDecimal[] valores) {

		BigDecimal media = null;
		BigDecimal total = new BigDecimal(0);

		Integer qtd = null;

		try {

			if (valores != null) {

				qtd = valores.length;

				for (int i = 0; i < qtd; i++) {

					if (valores[i] != null) {
						total = total.add(valores[i]);
					}

				}

				media = total.divide(new BigDecimal(qtd));
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return media;

	}

	public static boolean entreValores(Integer valor, Integer faixa1, Integer faixa2) {

		boolean ret = false;
		Integer valormenor = null;
		Integer valormaior = null;

		try {

			if (valor != null && faixa1 != null && faixa2 != null) {

				valormenor = faixa1.compareTo(faixa2) <= 0 ? faixa1 : faixa2;
				valormaior = faixa1.compareTo(faixa2) <= 0 ? faixa2 : faixa1;

				if (valor.compareTo(valormenor) >= 0 && valor.compareTo(valormaior) <= 0) {
					ret = true;
				}
				else {
					ret = false;
				}
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return ret;

	}

	public static boolean verificaAcessoClasse(String classe) {
		boolean ret = false;
		StringBuilder sql = new StringBuilder();
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			// Busca os registros de acesso às opções de menu para uma classe
			// especificada (acao menu)
			// Cujo o tipo de acesso (tpacessomu) seja diferente de 'A' (Sem
			// acesso) qualquer diferente de 'A'
			// Indica que o usuário tem acesso à classe (futuramente será
			// implementado o acesso combinado CRUD)

			sql.append("select distinct ");
			sql.append("mn.acaomenu, am.tpacessomu ");
			sql.append("from sgmenu mn, sgacessomu am, sgmodulo md ");
			sql.append("where ");
			sql.append("mn.codsis=am.codsis and mn.codmodu=am.codmodu and mn.codmenu=am.codmenu ");
			sql.append("and md.codsis=mn.codsis and md.codmodu=mn.codmodu ");
			sql.append("and am.codemp=? and am.codfilial=? and am.idusu=? and mn.acaomenu=? ");
			sql.append("and am.tpacessomu != 'A' ");

			System.out.println("sql para verificação de acesso:" + sql.toString());

			ps = Aplicativo.getInstace().getConexao().prepareStatement(sql.toString());

			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, Aplicativo.iCodFilial);
			ps.setString(3, Aplicativo.getUsuario().getIdusu());
			ps.setString(4, classe);

			rs = ps.executeQuery();

			if (rs.next() || "SYSDBA".equals(Aplicativo.getUsuario().getIdusu().toUpperCase())) {
				ret = true;
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}
	
    public static String completaTexto( String valor, int tam, String completa ) {
    	StringBuffer valorTexto = new StringBuffer( valor );
    	while ( valorTexto.length()<tam ) {
    		valorTexto.append( completa );
    	}
    	if ( valorTexto.length()>tam ) {
    		valorTexto.delete( tam, valorTexto.length() );
    	}
    	return valorTexto.toString();
    }
    /*
	public static boolean moveFile(File origem, String destino ) throws IOException {

		boolean movido = false;
		
		File dest = new File( destino );
		
		dest.createNewFile();

		InputStream in = null;

		OutputStream out = null;

		try{

			in = new FileInputStream( origem );

			out = new FileOutputStream(dest);

			byte[] buf = new byte[1024];

			int len;

			while((len = in.read(buf)) > 0){

				out.write(buf, 0, len);

			}
			
			origem.delete();
		
			movido = true;
			
		}
		catch ( Exception e ) {
			e.printStackTrace();
			mensagemErro( null, "Erro ao mover arquivo!\n"  + e.getMessage() );
		}

		finally{

			in.close();

			out.close();
		}
		
		return movido;

	}
	*/
	
    public static boolean moveFiles( String dirorigem, String dirdestino, String prefixo ) {
    	
    	boolean movidos = false;
    	
    	try {
    		

    		File diretorio_origem = new File(dirorigem);
    		
    		File arquivos[] = diretorio_origem.listFiles();

    		for ( int i = 0; i < arquivos.length; i++ ) {
    			
    			if (!arquivos[i].isDirectory()) {
    				System.out.println( "Movendo arquivo: " + arquivos[i].getName() + " " );
    	
    				moveFile(arquivos[i].toString(), dirdestino + "/" + (prefixo!=null ?prefixo : "") + arquivos[i].getName() );
    			}
    			
    		}
    	
    		movidos = true;
    		
    	}
    	catch (Exception e) {
    		e.printStackTrace();
		}
    	
    	return movidos;
    	
    }
    
	public static boolean moveFile( String origem, String destino) {
		
		boolean movido = false;
		
		try {
		
			// Cria channel na origem
			FileChannel oriChannel = new FileInputStream( origem ).getChannel();

			// Cria channel no destino
			FileChannel destChannel = new FileOutputStream( destino ).getChannel();
			
			// Copia conteúdo da origem no destino
			
			destChannel.transferFrom( oriChannel, 0, oriChannel.size());
	
			//	Fecha channels
			oriChannel.close();
			destChannel.close();
						
			File arquivo = new File(origem);
			
			arquivo.delete();
			
			movido = true;
			
		}
		catch (Exception e) {
			e.printStackTrace();
			mensagemErro( null, "Erro ao mover arquivo!\n"  + e.getMessage() );
		}
		
		return movido;
		
	}
	
	public static String getWeek(java.sql.Date date) {
		
		String ret = "";
		Integer week = null;
		Integer year = null;
		Integer month = null;
		
		try {
			
			Calendar cal = new GregorianCalendar();
			cal.setTime(date);
			
			month = cal.get( Calendar.MONTH );
			week = cal.get( Calendar.WEEK_OF_YEAR );
			year = cal.get( Calendar.YEAR );
			
			// Loucuras do Java
			// Se for dezembro e a semana estiver retornando 1 deve corrigir para 53 
			if(month.equals(11) && week.equals(1) ) {
				week = 53;
			}
			
			ret = year + " - " + StringFunctions.strZero( (week-1) + "", 2 );
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println( "Data:" + date.toString() + " semana:" + week);
		
		return ret;
		
	}

}
