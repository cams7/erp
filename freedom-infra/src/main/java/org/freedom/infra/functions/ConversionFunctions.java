/**
 * @version 01/03/2010 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez
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
 * Classe de funções de tratamento de texto.
 */

package org.freedom.infra.functions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Blob;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public final class ConversionFunctions {

	public static BigDecimal stringToBigDecimal(Object vlr) {
		BigDecimal retorno = null;
		if (vlr == null) {
			retorno = new BigDecimal(0);
		}
		else {
			retorno = stringCurrencyToBigDecimal(vlr.toString());
		}
		return retorno;
	}

	public static String dateToStrTime(Date dVal) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(dVal);
		int iHora = cal.get(Calendar.HOUR_OF_DAY);
		int iMinuto = cal.get(Calendar.MINUTE);
		int iSegundo = cal.get(Calendar.SECOND);
		return StringFunctions.strZero(String.valueOf(iHora), 2) + ":" + StringFunctions.strZero(String.valueOf(iMinuto), 2) + ":" + iSegundo;
	}

	public static File blobToFile(String FileName, Blob blob) {

		File file = null;
		InputStream input = null;

		try {

			input = blob.getBinaryStream();
			file = new File(FileName);
			FileOutputStream outStream = new FileOutputStream(file);

			int length = -1;
			int size = input.available();
			byte[] buffer = new byte[size];

			// Queimando o arquivo no disco

			while (( length = input.read(buffer) ) != -1) {
				outStream.write(buffer, 0, length);
				outStream.flush();
			}

			input.close();
			outStream.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro geração do arquivo para conversão.");
		}

		return file;

	}

	public static BigDecimal stringCurrencyToBigDecimal(String strvalue) {

		BigDecimal retvalue = new BigDecimal("0");

		try {

			if ((strvalue == null) || ("".equals(strvalue.trim()))) {
				return new BigDecimal("0");
			}

			int pospoint = strvalue.indexOf('.');

			if (pospoint > -1) {
				strvalue = strvalue.substring(0, pospoint) + strvalue.substring(pospoint + 1);
			}
			
			//Solução paliativa  
			int pospoint2 = strvalue.indexOf('.');
			if(pospoint2 > -1) {
				strvalue = strvalue.substring(0, pospoint2) + strvalue.substring(pospoint2 + 1);
				
			}

			char[] charvalue = strvalue.toCharArray();

			int iPos = strvalue.indexOf(",");

			if (iPos >= 0) {
				charvalue[iPos] = '.';
			}

			strvalue = new String(charvalue);

			retvalue = new BigDecimal(strvalue.trim());

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return retvalue;
	}

	public static Date strDateToDate(String sVal) {
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
		return cal.getTime();
	}
	
	public static Date strDate6digToDate(String strdate) {

		GregorianCalendar cal = new GregorianCalendar();

		if (strdate.trim().length() == 8) {

			strdate = strdate.trim();

			try {

				int day = Integer.parseInt(strdate.substring(0, 2));

				int mounth = Integer.parseInt(strdate.substring(3, 5)) - 1;

				int year = Integer.parseInt(strdate.substring(6));

				cal = ( GregorianCalendar ) GregorianCalendar.getInstance();

				String milenio = ( cal.get(Calendar.YEAR) + "" ).substring(0, 2);

				year = Integer.parseInt(milenio + year);

				cal = new GregorianCalendar(year, mounth, day);

			}
			catch (Exception err) {
				err.printStackTrace();
				cal = null;
			}

		}
		else {
			cal = null;
		}
		if (cal == null) {
			return null;
		}

		return cal.getTime();

	}

	public static Time strTimetoTime(String strtime) {

		Time time = null;

		try {

			strtime = StringFunctions.clearString(strtime);

			int hours = Integer.parseInt(strtime.substring(0, 2));
			int minutes = Integer.parseInt(strtime.substring(2, 4));
			int seconds = 0;

			if (strtime.length() > 4) {

				seconds = Integer.parseInt(strtime.substring(4));

			}

			Calendar cal = new GregorianCalendar();
			cal = Calendar.getInstance();

			cal.set(Calendar.HOUR_OF_DAY, hours);
			cal.set(Calendar.MINUTE, minutes);
			cal.set(Calendar.SECOND, seconds);

			time = new Time(cal.getTimeInMillis());

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return time;

	}

	public static String XMLDocumentToString(Document doc) {
		String ret = null;

		try {

			// Pegando o XML e transformando em String.

			// set up a transformer
			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			trans.setOutputProperty(OutputKeys.INDENT, "yes");

			// create string from xml tree
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			DOMSource source = new DOMSource(doc);
			trans.transform(source, result);
			ret = sw.toString();

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return ret;

	}
	
	public static String bigDecimalToStr(BigDecimal vlr) {

		return bigDecimalToStr(vlr, null, null);

	}
	
	public static String bigDecimalToStr(BigDecimal vlr, Integer casasdec, Integer roundtype) {
		String retorno = null;
		
		if(roundtype!=null && casasdec!=null ) {
			vlr = vlr.setScale(casasdec, roundtype);
			
		}
		else if(casasdec!=null) {
			vlr = vlr.setScale(casasdec);
		}
		
		
		if (vlr == null) {
			retorno = "0";
		}
		else {
			retorno = String.valueOf(vlr);
			
			retorno = retorno.replace('.', ',');
			
			if(casasdec!=null) {
				
				String inteiro = retorno.substring(0, retorno.indexOf(','));
				
				String decimal = retorno.substring(retorno.indexOf(','));
				
				decimal = decimal.substring(0, casasdec+1 );
				
				retorno = inteiro + decimal;
				
			}
			
		}
		return retorno;
	}
	
	public static String dateToStrDate(Date dVal) {
		String ret = "";
		if (dVal != null) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(dVal);
			int iAno = cal.get(Calendar.YEAR);
			int iMes = cal.get(Calendar.MONTH) + 1;
			int iDia = cal.get(Calendar.DAY_OF_MONTH);
			ret = StringFunctions.strZero(String.valueOf(iDia), 2) + "/" + StringFunctions.strZero(String.valueOf(iMes), 2) + "/" + String.valueOf(iAno);
		}
		return ret;

	}

}
