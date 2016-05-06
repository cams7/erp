/**
 * @version 17/06/2005 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.componentes <BR>
 * Classe:
 * @(#)Tabela.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * Objeto para guardar as informações necessárias para a criação e utilização de modelos de lote.
 */

package org.freedom.library.business.object;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;

public abstract class ModeloLote {

	private Vector<String> vLabels = new Vector<String>();

	private Vector<String> vLabelsAdic = new Vector<String>();

	private Vector<String> vLabelsColunas = new Vector<String>();

	private Vector<String> vValores = new Vector<String>();

	private Vector<String> vValoresAdic = new Vector<String>();

	private Vector<Integer> vTams = new Vector<Integer>();

	private Vector<String> vTamsAdic = new Vector<String>();

	private Vector<String> vMascaras = new Vector<String>();

	public static final String VLR_CODPROD = "#CODPROD#";

	public static final String VLR_DIA = "#DIA#";

	public static final String VLR_MES = "#MES#";

	public static final String VLR_ANO = "#ANO#";

	public static final String VLR_NPRODDIA = "#NPRODDIA#";

	public static final String VLR_LOTEPRINC = "#LOTEPRINC#";

	private String sTexto = "";

	public ModeloLote() {

	}

	public void adicOpcao(String sLabel, String sValor, Integer iTam) {

		vLabels.addElement(sLabel);
		vValores.addElement(sValor);
		vTams.addElement(iTam);
	}

	public Vector<String> getLabels() {
		return vLabels;
	}

	public Vector<String> getLabelsAdic() {
		return vLabelsAdic;
	}

	public Vector<Integer> getTams() {
		return vTams;
	}

	public Vector<String> getTamsAdic() {
		return vTamsAdic;
	}

	public Vector<String> getValores() {
		return vValores;
	}

	public Vector<String> getValoresAdic() {
		return vValoresAdic;
	}

	public Vector<String> getMascaras() {
		return vMascaras;
	}

	public Vector<String> getLabelsColunas() {
		return vLabelsColunas;
	}

	public void setTexto(String sTexto) {

		if (sTexto != null) {
			this.sTexto = sTexto;
		}
		getAdic();
	}

	public void getAdic() {

		vTamsAdic = new Vector<String>();
		vLabelsAdic = new Vector<String>();
		vValoresAdic = new Vector<String>();

		for (int i2 = 0; vValores.size() > i2; i2++) {
			if (( sTexto.indexOf(vValores.elementAt(i2).toString()) ) > ( -1 )) {
				vTamsAdic.addElement(vTams.elementAt(i2).toString());
				vLabelsAdic.addElement(vLabels.elementAt(i2).toString());
				vValoresAdic.addElement(vValores.elementAt(i2).toString());
			}
		}
	}

	public String getLote(final Integer iCodProd, final Date dData, final DbConnection con) {
		return getLote(iCodProd, dData, con, null);
	}

	public String getLote(final Integer iCodProd, final Date dData, final DbConnection con, final String lotePrincipal) {

		String sRetorno = sTexto;
		GregorianCalendar cal = new GregorianCalendar();

		try {

			cal.setTime(dData);
			Vector<String> vValAdic = getValoresAdic();

			if (sRetorno != null) {

				for (int i = 0; vValAdic.size() > i; i++) {

					String sValAdic = vValoresAdic.elementAt(i).toString();
					String sFragmento = sRetorno.substring(sRetorno.indexOf("[" + sValAdic));
					String sCampo = "";
					sFragmento = sFragmento.substring(0, ( "\\" + sFragmento ).indexOf("]"));

					int iTamAdic = Funcoes.contaChar(sFragmento, '-');

					if (sValAdic.equals(VLR_CODPROD)) {

						sCampo = String.valueOf(iCodProd);
						if (sCampo.length() < iTamAdic) {
							sCampo = StringFunctions.strZero(sCampo, iTamAdic);
						}
						else if (sCampo.length() > iTamAdic) {
							sCampo = sCampo.substring(0, iTamAdic);
						}
					}
					else if (sValAdic.equals(VLR_DIA)) {

						sCampo = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
						if (sCampo.length() < iTamAdic) {
							sCampo = StringFunctions.strZero(sCampo, iTamAdic);
						}
						else if (sCampo.length() > iTamAdic) {
							sCampo = sCampo.substring(0, iTamAdic);
						}
					}
					else if (sValAdic.equals(VLR_MES)) {

						sCampo = String.valueOf(cal.get(Calendar.MONTH) + 1);
						if (sCampo.length() < iTamAdic) {
							sCampo = StringFunctions.strZero(sCampo, iTamAdic);
						}
						else if (sCampo.length() > iTamAdic) {
							sCampo = sCampo.substring(0, iTamAdic);
						}
					}
					else if (sValAdic.equals(VLR_ANO)) {

						sCampo = String.valueOf(cal.get(Calendar.YEAR));
						if (sCampo.length() > iTamAdic) {
							sCampo = sCampo.substring(sCampo.length() - iTamAdic);
						}
					}
					else if (sValAdic.equals(VLR_NPRODDIA)) {

						try {
							String sSQL = "SELECT coalesce(count(1)+1,1) from ppop op " + "where op.codemppd=? and op.codfilialpd=? and op.codprod=? and op.dtfabrop = ?";
							PreparedStatement ps = con.prepareStatement(sSQL);
							ps.setInt(1, Aplicativo.iCodEmp);
							ps.setInt(2, ListaCampos.getMasterFilial("PPOP"));
							ps.setInt(3, iCodProd);
							ps.setDate(4, Funcoes.dateToSQLDate(dData));
							ResultSet rs = ps.executeQuery();
							if (rs.next()) {
								sCampo = rs.getString(1);
							}
						}
						catch (Exception err) {
							err.printStackTrace();
						}
					}
					else if (sValAdic.equals(VLR_LOTEPRINC) && lotePrincipal != null) {

						sCampo = lotePrincipal;
					}

					sRetorno = sRetorno.replaceAll("\\" + sFragmento, sCampo);
				}
			}
		}
		catch (Exception err) {
			err.printStackTrace();
		}

		return sRetorno;
	}
}
