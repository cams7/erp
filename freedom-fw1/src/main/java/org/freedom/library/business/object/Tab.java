/**
 * @version 23/01/2003 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 *
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.componentes <BR>
 * Classe: @(#)TabObjeto.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA <BR> <BR>
 *
 * Comentários da classe.....
 */

package org.freedom.library.business.object;

import org.freedom.infra.model.jdbc.DbConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.Vector;

public class Tab {
	public static final int IDOBJ = 0;
	public static final int DESCOBJ = 2;
	public static final int TIPOOBJ = 3;
	public static final int COMENTOBJ = 4;
	public static final int USOMEOBJ = 5;

	private Vector<BD> vObjetos = null;

	public Tab() {
		vObjetos = new Vector<BD>();
	}

	public boolean montaLista(DbConnection con, int iCodEmp, String sTabela, String sTipoObj) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean bRetorno = false;
		String sSql = "";
		try {
			if (!con.isClosed()) {
				sSql = "SELECT IDOBJ,DESCOBJ,TIPOOBJ,COMENTOBJ,USOMEOBJ FROM " + sTabela + " WHERE CODEMP=? AND TIPOOBJ=?";
				ps = con.prepareStatement(sSql);
				ps.setInt(1, iCodEmp);
				ps.setString(2, sTipoObj);
				rs = ps.executeQuery();
				while (rs.next()) {
					vObjetos.add(new BD(rs.getString("IDOBJ"), rs.getString("DESCOBJ"), rs.getString("TIPOOBJ"), rs.getString("COMENTOBJ"), rs.getString("USOMEOBJ")));
				}
				rs.close();
				con.commit();
			}
		}
		catch (SQLException e) {
			bRetorno = false;
		}
		return bRetorno;
	}

	public boolean getUsoMe(String sTabela) {
		boolean bRetorno = true;
		if (( vObjetos != null ) && ( sTabela != null )) {
			for (int i = 0; i < vObjetos.size(); i++) {
				if (vObjetos.elementAt(i).getIDObj().toUpperCase().trim().equals(sTabela.toUpperCase().trim())) {
					if (!vObjetos.elementAt(i).getUsomeObj().equals("S"))
						bRetorno = false;
					break;
				}
			}
		}

		return bRetorno;
	}

}
